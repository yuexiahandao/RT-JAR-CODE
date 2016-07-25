/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.CertSelector;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateRevokedException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXParameters;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.AuthorityInfoAccessExtension;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.URIName;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ class OCSPChecker extends PKIXCertPathChecker
/*     */ {
/*     */   static final String OCSP_ENABLE_PROP = "ocsp.enable";
/*     */   static final String OCSP_URL_PROP = "ocsp.responderURL";
/*     */   static final String OCSP_CERT_SUBJECT_PROP = "ocsp.responderCertSubjectName";
/*     */   static final String OCSP_CERT_ISSUER_PROP = "ocsp.responderCertIssuerName";
/*     */   static final String OCSP_CERT_NUMBER_PROP = "ocsp.responderCertSerialNumber";
/*     */   private static final String HEX_DIGITS = "0123456789ABCDEFabcdef";
/*  74 */   private static final Debug DEBUG = Debug.getInstance("certpath");
/*     */   private static final boolean dump = false;
/*     */   private int remainingCerts;
/*     */   private X509Certificate[] certs;
/*     */   private CertPath cp;
/*     */   private PKIXParameters pkixParams;
/*  85 */   private boolean onlyEECert = false;
/*     */ 
/*     */   OCSPChecker(CertPath paramCertPath, PKIXParameters paramPKIXParameters)
/*     */     throws CertPathValidatorException
/*     */   {
/*  96 */     this(paramCertPath, paramPKIXParameters, false);
/*     */   }
/*     */ 
/*     */   OCSPChecker(CertPath paramCertPath, PKIXParameters paramPKIXParameters, boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 102 */     this.cp = paramCertPath;
/* 103 */     this.pkixParams = paramPKIXParameters;
/* 104 */     this.onlyEECert = paramBoolean;
/* 105 */     List localList = this.cp.getCertificates();
/* 106 */     this.certs = ((X509Certificate[])localList.toArray(new X509Certificate[localList.size()]));
/* 107 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 116 */     if (!paramBoolean)
/* 117 */       this.remainingCerts = (this.certs.length + 1);
/*     */     else
/* 119 */       throw new CertPathValidatorException("Forward checking not supported");
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions() {
/* 129 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 146 */     this.remainingCerts -= 1;
/*     */ 
/* 148 */     X509CertImpl localX509CertImpl = null;
/*     */     try {
/* 150 */       localX509CertImpl = X509CertImpl.toImpl((X509Certificate)paramCertificate);
/*     */     } catch (CertificateException localCertificateException) {
/* 152 */       throw new CertPathValidatorException(localCertificateException);
/*     */     }
/*     */ 
/* 155 */     if ((this.onlyEECert) && (localX509CertImpl.getBasicConstraints() != -1)) {
/* 156 */       if (DEBUG != null) {
/* 157 */         DEBUG.println("Skipping revocation check, not end entity cert");
/*     */       }
/* 159 */       return;
/*     */     }
/*     */ 
/* 170 */     String[] arrayOfString = getOCSPProperties();
/*     */ 
/* 173 */     URI localURI = getOCSPServerURI(localX509CertImpl, arrayOfString[0]);
/*     */ 
/* 177 */     X500Principal localX500Principal1 = null;
/* 178 */     X500Principal localX500Principal2 = null;
/* 179 */     BigInteger localBigInteger = null;
/* 180 */     if (arrayOfString[1] != null) {
/* 181 */       localX500Principal1 = new X500Principal(arrayOfString[1]);
/* 182 */     } else if ((arrayOfString[2] != null) && (arrayOfString[3] != null)) {
/* 183 */       localX500Principal2 = new X500Principal(arrayOfString[2]);
/*     */ 
/* 185 */       String str = stripOutSeparators(arrayOfString[3]);
/* 186 */       localBigInteger = new BigInteger(str, 16);
/* 187 */     } else if ((arrayOfString[2] != null) || (arrayOfString[3] != null)) {
/* 188 */       throw new CertPathValidatorException("Must specify both ocsp.responderCertIssuerName and ocsp.responderCertSerialNumber properties");
/*     */     }
/*     */ 
/* 196 */     int i = 0;
/* 197 */     if ((localX500Principal1 != null) || (localX500Principal2 != null)) {
/* 198 */       i = 1;
/*     */     }
/*     */ 
/* 203 */     Object localObject1 = null;
/* 204 */     int j = 1;
/* 205 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 207 */     if (this.remainingCerts < this.certs.length) {
/* 208 */       localObject1 = this.certs[this.remainingCerts];
/* 209 */       j = 0;
/*     */ 
/* 213 */       if (i == 0) {
/* 214 */         localArrayList.add(localObject1);
/* 215 */         if (DEBUG != null)
/* 216 */           DEBUG.println("Responder's certificate is the same as the issuer of the certificate being validated");
/*     */       }
/*     */     }
/*     */     Object localObject5;
/*     */     Object localObject6;
/*     */     Object localObject7;
/* 225 */     if ((j != 0) || (i != 0))
/*     */     {
/* 227 */       if ((DEBUG != null) && (i != 0)) {
/* 228 */         DEBUG.println("Searching trust anchors for issuer or responder certificate");
/*     */       }
/*     */ 
/* 233 */       localObject2 = this.pkixParams.getTrustAnchors().iterator();
/*     */ 
/* 235 */       if (!((Iterator)localObject2).hasNext()) {
/* 236 */         throw new CertPathValidatorException("Must specify at least one trust anchor");
/*     */       }
/*     */ 
/* 240 */       localObject3 = localX509CertImpl.getIssuerX500Principal();
/*     */ 
/* 242 */       byte[] arrayOfByte = null;
/*     */ 
/* 244 */       while ((((Iterator)localObject2).hasNext()) && ((j != 0) || (i != 0)))
/*     */       {
/* 246 */         localObject4 = (TrustAnchor)((Iterator)localObject2).next();
/* 247 */         localObject5 = ((TrustAnchor)localObject4).getTrustedCert();
/* 248 */         localObject6 = ((X509Certificate)localObject5).getSubjectX500Principal();
/*     */ 
/* 257 */         if ((j != 0) && (((X500Principal)localObject3).equals(localObject6)))
/*     */         {
/* 261 */           if (arrayOfByte == null) {
/* 262 */             arrayOfByte = localX509CertImpl.getIssuerKeyIdentifier();
/* 263 */             if ((arrayOfByte == null) && 
/* 264 */               (DEBUG != null)) {
/* 265 */               DEBUG.println("No issuer key identifier (AKID) in the certificate being validated");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 272 */           localObject7 = null;
/* 273 */           if ((arrayOfByte != null) && ((localObject7 = getKeyId((X509Certificate)localObject5)) != null))
/*     */           {
/* 276 */             if (Arrays.equals(arrayOfByte, (byte[])localObject7))
/*     */             {
/* 280 */               if (DEBUG != null) {
/* 281 */                 DEBUG.println("Issuer certificate key ID: " + String.format(new StringBuilder().append("0x%0").append(arrayOfByte.length * 2).append("x").toString(), new Object[] { new BigInteger(1, arrayOfByte) }));
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 288 */             localObject1 = localObject5;
/* 289 */             j = 0;
/*     */ 
/* 293 */             if ((i == 0) && (localArrayList.isEmpty())) {
/* 294 */               localArrayList.add(localObject5);
/* 295 */               if (DEBUG != null) {
/* 296 */                 DEBUG.println("Responder's certificate is the same as the issuer of the certificate being validated");
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/* 304 */         else if (i != 0)
/*     */         {
/* 308 */           if (((localX500Principal1 != null) && (localX500Principal1.equals(localObject6))) || ((localX500Principal2 != null) && (localBigInteger != null) && (localX500Principal2.equals(((X509Certificate)localObject5).getIssuerX500Principal())) && (localBigInteger.equals(((X509Certificate)localObject5).getSerialNumber()))))
/*     */           {
/* 317 */             localArrayList.add(localObject5);
/*     */           }
/*     */         }
/*     */       }
/* 321 */       if (localObject1 == null) {
/* 322 */         throw new CertPathValidatorException("No trusted certificate for " + localX509CertImpl.getIssuerDN());
/*     */       }
/*     */ 
/* 327 */       if (i != 0) {
/* 328 */         if (DEBUG != null) {
/* 329 */           DEBUG.println("Searching cert stores for responder's certificate");
/*     */         }
/*     */ 
/* 332 */         localObject4 = null;
/* 333 */         if (localX500Principal1 != null) {
/* 334 */           localObject4 = new X509CertSelector();
/* 335 */           ((X509CertSelector)localObject4).setSubject(localX500Principal1);
/* 336 */         } else if ((localX500Principal2 != null) && (localBigInteger != null))
/*     */         {
/* 338 */           localObject4 = new X509CertSelector();
/* 339 */           ((X509CertSelector)localObject4).setIssuer(localX500Principal2);
/* 340 */           ((X509CertSelector)localObject4).setSerialNumber(localBigInteger);
/*     */         }
/* 342 */         if (localObject4 != null) {
/* 343 */           localObject5 = this.pkixParams.getCertStores();
/* 344 */           for (localObject6 = ((List)localObject5).iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (CertStore)((Iterator)localObject6).next();
/*     */             try {
/* 346 */               localArrayList.addAll(((CertStore)localObject7).getCertificates((CertSelector)localObject4));
/*     */             }
/*     */             catch (CertStoreException localCertStoreException)
/*     */             {
/* 351 */               if (DEBUG != null) {
/* 352 */                 DEBUG.println("CertStore exception:" + localCertStoreException);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     if ((i != 0) && (localArrayList.isEmpty())) {
/* 363 */       throw new CertPathValidatorException("Cannot find the responder's certificate (set using the OCSP security properties).");
/*     */     }
/*     */ 
/* 368 */     if (DEBUG != null) {
/* 369 */       DEBUG.println("Located " + localArrayList.size() + " trusted responder certificate(s)");
/*     */     }
/*     */ 
/* 377 */     Object localObject2 = null;
/* 378 */     Object localObject3 = null;
/*     */     try {
/* 380 */       localObject2 = new CertId((X509Certificate)localObject1, localX509CertImpl.getSerialNumberObject());
/*     */ 
/* 382 */       localObject3 = OCSP.check(Collections.singletonList(localObject2), localURI, localArrayList, this.pkixParams.getDate());
/*     */     }
/*     */     catch (Exception localException) {
/* 385 */       if ((localException instanceof CertPathValidatorException)) {
/* 386 */         throw ((CertPathValidatorException)localException);
/*     */       }
/*     */ 
/* 390 */       throw new CertPathValidatorException(localException);
/*     */     }
/*     */ 
/* 394 */     OCSPResponse.SingleResponse localSingleResponse = ((OCSPResponse)localObject3).getSingleResponse((CertId)localObject2);
/* 395 */     Object localObject4 = localSingleResponse.getCertStatus();
/* 396 */     if (localObject4 == OCSP.RevocationStatus.CertStatus.REVOKED) {
/* 397 */       localObject5 = new CertificateRevokedException(localSingleResponse.getRevocationTime(), localSingleResponse.getRevocationReason(), ((X509Certificate)localArrayList.get(0)).getSubjectX500Principal(), localSingleResponse.getSingleExtensions());
/*     */ 
/* 401 */       throw new CertPathValidatorException(((Throwable)localObject5).getMessage(), (Throwable)localObject5, null, -1, CertPathValidatorException.BasicReason.REVOKED);
/*     */     }
/* 403 */     if (localObject4 == OCSP.RevocationStatus.CertStatus.UNKNOWN)
/* 404 */       throw new CertPathValidatorException("Certificate's revocation status is unknown", null, this.cp, this.remainingCerts - 1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
/*     */   }
/*     */ 
/*     */   private static URI getOCSPServerURI(X509CertImpl paramX509CertImpl, String paramString)
/*     */     throws CertPathValidatorException
/*     */   {
/* 421 */     if (paramString != null) {
/*     */       try {
/* 423 */         return new URI(paramString);
/*     */       } catch (URISyntaxException localURISyntaxException) {
/* 425 */         throw new CertPathValidatorException(localURISyntaxException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 430 */     AuthorityInfoAccessExtension localAuthorityInfoAccessExtension = paramX509CertImpl.getAuthorityInfoAccessExtension();
/*     */ 
/* 432 */     if (localAuthorityInfoAccessExtension == null) {
/* 433 */       throw new CertPathValidatorException("Must specify the location of an OCSP Responder");
/*     */     }
/*     */ 
/* 437 */     List localList = localAuthorityInfoAccessExtension.getAccessDescriptions();
/* 438 */     for (AccessDescription localAccessDescription : localList) {
/* 439 */       if (localAccessDescription.getAccessMethod().equals(AccessDescription.Ad_OCSP_Id))
/*     */       {
/* 442 */         GeneralName localGeneralName = localAccessDescription.getAccessLocation();
/* 443 */         if (localGeneralName.getType() == 6) {
/* 444 */           URIName localURIName = (URIName)localGeneralName.getName();
/* 445 */           return localURIName.getURI();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 450 */     throw new CertPathValidatorException("Cannot find the location of the OCSP Responder");
/*     */   }
/*     */ 
/*     */   private static String[] getOCSPProperties()
/*     */   {
/* 458 */     String[] arrayOfString = new String[4];
/*     */ 
/* 460 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 463 */         this.val$properties[0] = Security.getProperty("ocsp.responderURL");
/* 464 */         this.val$properties[1] = Security.getProperty("ocsp.responderCertSubjectName");
/*     */ 
/* 466 */         this.val$properties[2] = Security.getProperty("ocsp.responderCertIssuerName");
/*     */ 
/* 468 */         this.val$properties[3] = Security.getProperty("ocsp.responderCertSerialNumber");
/*     */ 
/* 470 */         return null;
/*     */       }
/*     */     });
/* 474 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static String stripOutSeparators(String paramString)
/*     */   {
/* 481 */     char[] arrayOfChar = paramString.toCharArray();
/* 482 */     StringBuilder localStringBuilder = new StringBuilder();
/* 483 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 484 */       if ("0123456789ABCDEFabcdef".indexOf(arrayOfChar[i]) != -1) {
/* 485 */         localStringBuilder.append(arrayOfChar[i]);
/*     */       }
/*     */     }
/* 488 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static byte[] getKeyId(X509Certificate paramX509Certificate)
/*     */   {
/* 495 */     X509CertImpl localX509CertImpl = null;
/* 496 */     byte[] arrayOfByte = null;
/*     */     try
/*     */     {
/* 499 */       localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate);
/* 500 */       arrayOfByte = localX509CertImpl.getSubjectKeyIdentifier();
/*     */ 
/* 502 */       if ((arrayOfByte == null) && 
/* 503 */         (DEBUG != null)) {
/* 504 */         DEBUG.println("No subject key identifier (SKID) in the certificate (Subject: " + paramX509Certificate.getSubjectX500Principal() + ")");
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (CertificateException localCertificateException)
/*     */     {
/* 512 */       if (DEBUG != null) {
/* 513 */         DEBUG.println("Error parsing X.509 certificate (Subject: " + paramX509Certificate.getSubjectX500Principal() + ") " + localCertificateException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 518 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.OCSPChecker
 * JD-Core Version:    0.6.2
 */