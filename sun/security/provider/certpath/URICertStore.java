/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CRLSelector;
/*     */ import java.security.cert.CertSelector;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.CertStoreParameters;
/*     */ import java.security.cert.CertStoreSpi;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509CRLSelector;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.util.Cache;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.GeneralNameInterface;
/*     */ import sun.security.x509.URIName;
/*     */ 
/*     */ class URICertStore extends CertStoreSpi
/*     */ {
/*  93 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private static final int CHECK_INTERVAL = 30000;
/*     */   private static final int CACHE_SIZE = 185;
/*     */   private final CertificateFactory factory;
/* 106 */   private Collection<X509Certificate> certs = Collections.emptySet();
/*     */   private X509CRL crl;
/*     */   private long lastChecked;
/*     */   private long lastModified;
/*     */   private URI uri;
/* 123 */   private boolean ldap = false;
/*     */   private CertStore ldapCertStore;
/*     */   private String ldapPath;
/*     */   private static final int DEFAULT_CRL_CONNECT_TIMEOUT = 15000;
/* 162 */   private static final int CRL_CONNECT_TIMEOUT = initializeTimeout();
/*     */ 
/* 216 */   private static final Cache certStoreCache = Cache.newSoftMemoryCache(185);
/*     */ 
/*     */   private static int initializeTimeout()
/*     */   {
/* 170 */     Integer localInteger = (Integer)AccessController.doPrivileged(new GetIntegerAction("com.sun.security.crl.timeout"));
/*     */ 
/* 172 */     if ((localInteger == null) || (localInteger.intValue() < 0)) {
/* 173 */       return 15000;
/*     */     }
/*     */ 
/* 177 */     return localInteger.intValue() * 1000;
/*     */   }
/*     */ 
/*     */   URICertStore(CertStoreParameters paramCertStoreParameters)
/*     */     throws InvalidAlgorithmParameterException, NoSuchAlgorithmException
/*     */   {
/* 187 */     super(paramCertStoreParameters);
/* 188 */     if (!(paramCertStoreParameters instanceof URICertStoreParameters)) {
/* 189 */       throw new InvalidAlgorithmParameterException("params must be instanceof URICertStoreParameters");
/*     */     }
/*     */ 
/* 192 */     this.uri = ((URICertStoreParameters)paramCertStoreParameters).uri;
/*     */ 
/* 194 */     if (this.uri.getScheme().toLowerCase(Locale.ENGLISH).equals("ldap")) {
/* 195 */       if (LDAP.helper() == null)
/* 196 */         throw new NoSuchAlgorithmException("LDAP not present");
/* 197 */       this.ldap = true;
/* 198 */       this.ldapCertStore = LDAP.helper().getCertStore(this.uri);
/* 199 */       this.ldapPath = this.uri.getPath();
/*     */ 
/* 201 */       if (this.ldapPath.charAt(0) == '/')
/* 202 */         this.ldapPath = this.ldapPath.substring(1);
/*     */     }
/*     */     try
/*     */     {
/* 206 */       this.factory = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException localCertificateException) {
/* 208 */       throw new RuntimeException();
/*     */     }
/*     */   }
/*     */ 
/*     */   static synchronized CertStore getInstance(URICertStoreParameters paramURICertStoreParameters)
/*     */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*     */   {
/* 220 */     if (debug != null) {
/* 221 */       debug.println("CertStore URI:" + paramURICertStoreParameters.uri);
/*     */     }
/* 223 */     Object localObject = (CertStore)certStoreCache.get(paramURICertStoreParameters);
/* 224 */     if (localObject == null) {
/* 225 */       localObject = new UCS(new URICertStore(paramURICertStoreParameters), null, "URI", paramURICertStoreParameters);
/* 226 */       certStoreCache.put(paramURICertStoreParameters, localObject);
/*     */     }
/* 228 */     else if (debug != null) {
/* 229 */       debug.println("URICertStore.getInstance: cache hit");
/*     */     }
/*     */ 
/* 232 */     return localObject;
/*     */   }
/*     */ 
/*     */   static CertStore getInstance(AccessDescription paramAccessDescription)
/*     */   {
/* 240 */     if (!paramAccessDescription.getAccessMethod().equals(AccessDescription.Ad_CAISSUERS_Id)) {
/* 241 */       return null;
/*     */     }
/* 243 */     GeneralNameInterface localGeneralNameInterface = paramAccessDescription.getAccessLocation().getName();
/* 244 */     if (!(localGeneralNameInterface instanceof URIName)) {
/* 245 */       return null;
/*     */     }
/* 247 */     URI localURI = ((URIName)localGeneralNameInterface).getURI();
/*     */     try {
/* 249 */       return getInstance(new URICertStoreParameters(localURI));
/*     */     }
/*     */     catch (Exception localException) {
/* 252 */       if (debug != null) {
/* 253 */         debug.println("exception creating CertStore: " + localException);
/* 254 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Collection<X509Certificate> engineGetCertificates(CertSelector paramCertSelector)
/*     */     throws CertStoreException
/*     */   {
/* 277 */     if (this.ldap) {
/* 278 */       X509CertSelector localX509CertSelector = (X509CertSelector)paramCertSelector;
/*     */       try {
/* 280 */         localX509CertSelector = LDAP.helper().wrap(localX509CertSelector, localX509CertSelector.getSubject(), this.ldapPath);
/*     */       } catch (IOException localIOException1) {
/* 282 */         throw new CertStoreException(localIOException1);
/*     */       }
/*     */ 
/* 286 */       return this.ldapCertStore.getCertificates(localX509CertSelector);
/*     */     }
/*     */ 
/* 293 */     long l1 = System.currentTimeMillis();
/* 294 */     if (l1 - this.lastChecked < 30000L) {
/* 295 */       if (debug != null) {
/* 296 */         debug.println("Returning certificates from cache");
/*     */       }
/* 298 */       return getMatchingCerts(this.certs, paramCertSelector);
/*     */     }
/* 300 */     this.lastChecked = l1;
/* 301 */     InputStream localInputStream = null;
/*     */     try {
/* 303 */       URLConnection localURLConnection = this.uri.toURL().openConnection();
/* 304 */       if (this.lastModified != 0L) {
/* 305 */         localURLConnection.setIfModifiedSince(this.lastModified);
/*     */       }
/* 307 */       localInputStream = localURLConnection.getInputStream();
/* 308 */       long l2 = this.lastModified;
/* 309 */       this.lastModified = localURLConnection.getLastModified();
/*     */       Object localObject1;
/* 310 */       if (l2 != 0L) {
/* 311 */         if (l2 == this.lastModified) {
/* 312 */           if (debug != null) {
/* 313 */             debug.println("Not modified, using cached copy");
/*     */           }
/* 315 */           return getMatchingCerts(this.certs, paramCertSelector);
/* 316 */         }if ((localURLConnection instanceof HttpURLConnection))
/*     */         {
/* 318 */           localObject1 = (HttpURLConnection)localURLConnection;
/* 319 */           if (((HttpURLConnection)localObject1).getResponseCode() == 304)
/*     */           {
/* 321 */             if (debug != null) {
/* 322 */               debug.println("Not modified, using cached copy");
/*     */             }
/* 324 */             return getMatchingCerts(this.certs, paramCertSelector);
/*     */           }
/*     */         }
/*     */       }
/* 328 */       if (debug != null) {
/* 329 */         debug.println("Downloading new certificates...");
/*     */       }
/* 331 */       this.certs = this.factory.generateCertificates(localInputStream);
/*     */ 
/* 333 */       return getMatchingCerts(this.certs, paramCertSelector);
/*     */     } catch (IOException localIOException2) {
/* 335 */       if (debug != null) {
/* 336 */         debug.println("Exception fetching certificates:");
/* 337 */         localIOException2.printStackTrace();
/*     */       }
/*     */     } catch (CertificateException localCertificateException) {
/* 340 */       if (debug != null) {
/* 341 */         debug.println("Exception fetching certificates:");
/* 342 */         localCertificateException.printStackTrace();
/*     */       }
/*     */     } finally {
/* 345 */       if (localInputStream != null) {
/*     */         try {
/* 347 */           localInputStream.close();
/*     */         }
/*     */         catch (IOException localIOException8)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 354 */     this.lastModified = 0L;
/* 355 */     this.certs = Collections.emptySet();
/* 356 */     return this.certs;
/*     */   }
/*     */ 
/*     */   private static Collection<X509Certificate> getMatchingCerts(Collection<X509Certificate> paramCollection, CertSelector paramCertSelector)
/*     */   {
/* 367 */     if (paramCertSelector == null) {
/* 368 */       return paramCollection;
/*     */     }
/* 370 */     ArrayList localArrayList = new ArrayList(paramCollection.size());
/*     */ 
/* 372 */     for (X509Certificate localX509Certificate : paramCollection) {
/* 373 */       if (paramCertSelector.match(localX509Certificate)) {
/* 374 */         localArrayList.add(localX509Certificate);
/*     */       }
/*     */     }
/* 377 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public synchronized Collection<X509CRL> engineGetCRLs(CRLSelector paramCRLSelector)
/*     */     throws CertStoreException
/*     */   {
/* 397 */     if (this.ldap) {
/* 398 */       X509CRLSelector localX509CRLSelector = (X509CRLSelector)paramCRLSelector;
/*     */       try {
/* 400 */         localX509CRLSelector = LDAP.helper().wrap(localX509CRLSelector, null, this.ldapPath);
/*     */       } catch (IOException localIOException1) {
/* 402 */         throw new CertStoreException(localIOException1);
/*     */       }
/*     */ 
/* 406 */       return this.ldapCertStore.getCRLs(localX509CRLSelector);
/*     */     }
/*     */ 
/* 412 */     long l1 = System.currentTimeMillis();
/* 413 */     if (l1 - this.lastChecked < 30000L) {
/* 414 */       if (debug != null) {
/* 415 */         debug.println("Returning CRL from cache");
/*     */       }
/* 417 */       return getMatchingCRLs(this.crl, paramCRLSelector);
/*     */     }
/* 419 */     this.lastChecked = l1;
/* 420 */     InputStream localInputStream = null;
/*     */     try {
/* 422 */       URLConnection localURLConnection = this.uri.toURL().openConnection();
/* 423 */       if (this.lastModified != 0L) {
/* 424 */         localURLConnection.setIfModifiedSince(this.lastModified);
/*     */       }
/* 426 */       localURLConnection.setConnectTimeout(CRL_CONNECT_TIMEOUT);
/* 427 */       localInputStream = localURLConnection.getInputStream();
/* 428 */       long l2 = this.lastModified;
/* 429 */       this.lastModified = localURLConnection.getLastModified();
/*     */       Object localObject1;
/* 430 */       if (l2 != 0L) {
/* 431 */         if (l2 == this.lastModified) {
/* 432 */           if (debug != null) {
/* 433 */             debug.println("Not modified, using cached copy");
/*     */           }
/* 435 */           return getMatchingCRLs(this.crl, paramCRLSelector);
/* 436 */         }if ((localURLConnection instanceof HttpURLConnection))
/*     */         {
/* 438 */           localObject1 = (HttpURLConnection)localURLConnection;
/* 439 */           if (((HttpURLConnection)localObject1).getResponseCode() == 304)
/*     */           {
/* 441 */             if (debug != null) {
/* 442 */               debug.println("Not modified, using cached copy");
/*     */             }
/* 444 */             return getMatchingCRLs(this.crl, paramCRLSelector);
/*     */           }
/*     */         }
/*     */       }
/* 448 */       if (debug != null) {
/* 449 */         debug.println("Downloading new CRL...");
/*     */       }
/* 451 */       this.crl = ((X509CRL)this.factory.generateCRL(localInputStream));
/* 452 */       return getMatchingCRLs(this.crl, paramCRLSelector);
/*     */     } catch (IOException localIOException2) {
/* 454 */       if (debug != null) {
/* 455 */         debug.println("Exception fetching CRL:");
/* 456 */         localIOException2.printStackTrace();
/*     */       }
/*     */     } catch (CRLException localCRLException) {
/* 459 */       if (debug != null) {
/* 460 */         debug.println("Exception fetching CRL:");
/* 461 */         localCRLException.printStackTrace();
/*     */       }
/*     */     } finally {
/* 464 */       if (localInputStream != null) {
/*     */         try {
/* 466 */           localInputStream.close();
/*     */         }
/*     */         catch (IOException localIOException8)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/* 473 */     this.lastModified = 0L;
/* 474 */     this.crl = null;
/* 475 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   private static Collection<X509CRL> getMatchingCRLs(X509CRL paramX509CRL, CRLSelector paramCRLSelector)
/*     */   {
/* 484 */     if ((paramCRLSelector == null) || ((paramX509CRL != null) && (paramCRLSelector.match(paramX509CRL)))) {
/* 485 */       return Collections.singletonList(paramX509CRL);
/*     */     }
/* 487 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   private static class LDAP
/*     */   {
/*     */     private static final String CERT_STORE_HELPER = "sun.security.provider.certpath.ldap.LDAPCertStoreHelper";
/* 133 */     private static final CertStoreHelper helper = (CertStoreHelper)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public CertStoreHelper run()
/*     */       {
/*     */         try {
/* 138 */           Class localClass = Class.forName("sun.security.provider.certpath.ldap.LDAPCertStoreHelper", true, null);
/* 139 */           return (CertStoreHelper)localClass.newInstance();
/*     */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 141 */           return null;
/*     */         } catch (InstantiationException localInstantiationException) {
/* 143 */           throw new AssertionError(localInstantiationException);
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 145 */           throw new AssertionError(localIllegalAccessException);
/*     */         }
/*     */       }
/*     */     });
/*     */ 
/*     */     static CertStoreHelper helper()
/*     */     {
/* 149 */       return helper;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UCS extends CertStore
/*     */   {
/*     */     protected UCS(CertStoreSpi paramCertStoreSpi, Provider paramProvider, String paramString, CertStoreParameters paramCertStoreParameters)
/*     */     {
/* 531 */       super(paramProvider, paramString, paramCertStoreParameters);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class URICertStoreParameters
/*     */     implements CertStoreParameters
/*     */   {
/*     */     private final URI uri;
/* 496 */     private volatile int hashCode = 0;
/*     */ 
/* 498 */     URICertStoreParameters(URI paramURI) { this.uri = paramURI; }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 501 */       if (!(paramObject instanceof URICertStoreParameters)) {
/* 502 */         return false;
/*     */       }
/* 504 */       URICertStoreParameters localURICertStoreParameters = (URICertStoreParameters)paramObject;
/* 505 */       return this.uri.equals(localURICertStoreParameters.uri);
/*     */     }
/*     */     public int hashCode() {
/* 508 */       if (this.hashCode == 0) {
/* 509 */         int i = 17;
/* 510 */         i = 37 * i + this.uri.hashCode();
/* 511 */         this.hashCode = i;
/*     */       }
/* 513 */       return this.hashCode;
/*     */     }
/*     */     public Object clone() {
/*     */       try {
/* 517 */         return super.clone();
/*     */       }
/*     */       catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 520 */         throw new InternalError(localCloneNotSupportedException.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.URICertStore
 * JD-Core Version:    0.6.2
 */