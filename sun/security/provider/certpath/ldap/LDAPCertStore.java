/*      */ package sun.security.provider.certpath.ldap;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URI;
/*      */ import java.security.AccessController;
/*      */ import java.security.InvalidAlgorithmParameterException;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.PublicKey;
/*      */ import java.security.cert.CRL;
/*      */ import java.security.cert.CRLException;
/*      */ import java.security.cert.CRLSelector;
/*      */ import java.security.cert.CertSelector;
/*      */ import java.security.cert.CertStore;
/*      */ import java.security.cert.CertStoreException;
/*      */ import java.security.cert.CertStoreParameters;
/*      */ import java.security.cert.CertStoreSpi;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.LDAPCertStoreParameters;
/*      */ import java.security.cert.X509CRL;
/*      */ import java.security.cert.X509CRLSelector;
/*      */ import java.security.cert.X509CertSelector;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.naming.NameNotFoundException;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.BasicAttributes;
/*      */ import javax.naming.directory.DirContext;
/*      */ import javax.naming.directory.InitialDirContext;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.HexDumpEncoder;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.provider.certpath.X509CertificatePair;
/*      */ import sun.security.util.Cache;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.x509.X500Name;
/*      */ 
/*      */ public class LDAPCertStore extends CertStoreSpi
/*      */ {
/*  109 */   private static final Debug debug = Debug.getInstance("certpath");
/*      */   private static final boolean DEBUG = false;
/*      */   private static final String USER_CERT = "userCertificate;binary";
/*      */   private static final String CA_CERT = "cACertificate;binary";
/*      */   private static final String CROSS_CERT = "crossCertificatePair;binary";
/*      */   private static final String CRL = "certificateRevocationList;binary";
/*      */   private static final String ARL = "authorityRevocationList;binary";
/*      */   private static final String DELTA_CRL = "deltaRevocationList;binary";
/*  124 */   private static final String[] STRING0 = new String[0];
/*      */ 
/*  126 */   private static final byte[][] BB0 = new byte[0][];
/*      */ 
/*  128 */   private static final Attributes EMPTY_ATTRIBUTES = new BasicAttributes();
/*      */   private static final int DEFAULT_CACHE_SIZE = 750;
/*      */   private static final int DEFAULT_CACHE_LIFETIME = 30;
/*      */   private static final int LIFETIME;
/*      */   private static final String PROP_LIFETIME = "sun.security.certpath.ldap.cache.lifetime";
/*      */   private static final String PROP_DISABLE_APP_RESOURCE_FILES = "sun.security.certpath.ldap.disable.app.resource.files";
/*      */   private CertificateFactory cf;
/*      */   private DirContext ctx;
/*  170 */   private boolean prefetchCRLs = false;
/*      */   private final Cache valueCache;
/*  174 */   private int cacheHits = 0;
/*  175 */   private int cacheMisses = 0;
/*  176 */   private int requests = 0;
/*      */ 
/*  219 */   private static final Cache certStoreCache = Cache.newSoftMemoryCache(185);
/*      */ 
/*      */   public LDAPCertStore(CertStoreParameters paramCertStoreParameters)
/*      */     throws InvalidAlgorithmParameterException
/*      */   {
/*  189 */     super(paramCertStoreParameters);
/*  190 */     if (!(paramCertStoreParameters instanceof LDAPCertStoreParameters)) {
/*  191 */       throw new InvalidAlgorithmParameterException("parameters must be LDAPCertStoreParameters");
/*      */     }
/*      */ 
/*  194 */     LDAPCertStoreParameters localLDAPCertStoreParameters = (LDAPCertStoreParameters)paramCertStoreParameters;
/*      */ 
/*  197 */     createInitialDirContext(localLDAPCertStoreParameters.getServerName(), localLDAPCertStoreParameters.getPort());
/*      */     try
/*      */     {
/*  201 */       this.cf = CertificateFactory.getInstance("X.509");
/*      */     } catch (CertificateException localCertificateException) {
/*  203 */       throw new InvalidAlgorithmParameterException("unable to create CertificateFactory for X.509");
/*      */     }
/*      */ 
/*  206 */     if (LIFETIME == 0)
/*  207 */       this.valueCache = Cache.newNullCache();
/*  208 */     else if (LIFETIME < 0)
/*  209 */       this.valueCache = Cache.newSoftMemoryCache(750);
/*      */     else
/*  211 */       this.valueCache = Cache.newSoftMemoryCache(750, LIFETIME);
/*      */   }
/*      */ 
/*      */   static synchronized CertStore getInstance(LDAPCertStoreParameters paramLDAPCertStoreParameters)
/*      */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*      */   {
/*  222 */     CertStore localCertStore = (CertStore)certStoreCache.get(paramLDAPCertStoreParameters);
/*  223 */     if (localCertStore == null) {
/*  224 */       localCertStore = CertStore.getInstance("LDAP", paramLDAPCertStoreParameters);
/*  225 */       certStoreCache.put(paramLDAPCertStoreParameters, localCertStore);
/*      */     }
/*  227 */     else if (debug != null) {
/*  228 */       debug.println("LDAPCertStore.getInstance: cache hit");
/*      */     }
/*      */ 
/*  231 */     return localCertStore;
/*      */   }
/*      */ 
/*      */   private void createInitialDirContext(String paramString, int paramInt)
/*      */     throws InvalidAlgorithmParameterException
/*      */   {
/*  243 */     String str = "ldap://" + paramString + ":" + paramInt;
/*  244 */     Hashtable localHashtable1 = new Hashtable();
/*  245 */     localHashtable1.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
/*      */ 
/*  247 */     localHashtable1.put("java.naming.provider.url", str);
/*      */ 
/*  250 */     boolean bool = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.certpath.ldap.disable.app.resource.files"))).booleanValue();
/*      */ 
/*  252 */     if (bool) {
/*  253 */       if (debug != null) {
/*  254 */         debug.println("LDAPCertStore disabling app resource files");
/*      */       }
/*  256 */       localHashtable1.put("com.sun.naming.disable.app.resource.files", "true");
/*      */     }
/*      */     try
/*      */     {
/*  260 */       this.ctx = new InitialDirContext(localHashtable1);
/*      */ 
/*  265 */       Hashtable localHashtable2 = this.ctx.getEnvironment();
/*  266 */       if (localHashtable2.get("java.naming.referral") == null)
/*  267 */         this.ctx.addToEnvironment("java.naming.referral", "follow");
/*      */     }
/*      */     catch (NamingException localNamingException) {
/*  270 */       if (debug != null) {
/*  271 */         debug.println("LDAPCertStore.engineInit about to throw InvalidAlgorithmParameterException");
/*      */ 
/*  273 */         localNamingException.printStackTrace();
/*      */       }
/*  275 */       InvalidAlgorithmParameterException localInvalidAlgorithmParameterException = new InvalidAlgorithmParameterException("unable to create InitialDirContext using supplied parameters");
/*      */ 
/*  277 */       localInvalidAlgorithmParameterException.initCause(localNamingException);
/*  278 */       throw ((InvalidAlgorithmParameterException)localInvalidAlgorithmParameterException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Collection<X509Certificate> getCertificates(LDAPRequest paramLDAPRequest, String paramString, X509CertSelector paramX509CertSelector)
/*      */     throws CertStoreException
/*      */   {
/*      */     byte[][] arrayOfByte;
/*      */     try
/*      */     {
/*  445 */       arrayOfByte = paramLDAPRequest.getValues(paramString);
/*      */     } catch (NamingException localNamingException) {
/*  447 */       throw new CertStoreException(localNamingException);
/*      */     }
/*      */ 
/*  450 */     int i = arrayOfByte.length;
/*  451 */     if (i == 0) {
/*  452 */       return Collections.emptySet();
/*      */     }
/*      */ 
/*  455 */     ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  457 */     for (int j = 0; j < i; j++) {
/*  458 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte[j]);
/*      */       try {
/*  460 */         Certificate localCertificate = this.cf.generateCertificate(localByteArrayInputStream);
/*  461 */         if (paramX509CertSelector.match(localCertificate))
/*  462 */           localArrayList.add((X509Certificate)localCertificate);
/*      */       }
/*      */       catch (CertificateException localCertificateException) {
/*  465 */         if (debug != null) {
/*  466 */           debug.println("LDAPCertStore.getCertificates() encountered exception while parsing cert, skipping the bad data: ");
/*      */ 
/*  468 */           HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*  469 */           debug.println("[ " + localHexDumpEncoder.encodeBuffer(arrayOfByte[j]) + " ]");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  475 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private Collection<X509CertificatePair> getCertPairs(LDAPRequest paramLDAPRequest, String paramString)
/*      */     throws CertStoreException
/*      */   {
/*      */     byte[][] arrayOfByte;
/*      */     try
/*      */     {
/*  493 */       arrayOfByte = paramLDAPRequest.getValues(paramString);
/*      */     } catch (NamingException localNamingException) {
/*  495 */       throw new CertStoreException(localNamingException);
/*      */     }
/*      */ 
/*  498 */     int i = arrayOfByte.length;
/*  499 */     if (i == 0) {
/*  500 */       return Collections.emptySet();
/*      */     }
/*      */ 
/*  503 */     ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  506 */     for (int j = 0; j < i; j++) {
/*      */       try {
/*  508 */         X509CertificatePair localX509CertificatePair = X509CertificatePair.generateCertificatePair(arrayOfByte[j]);
/*      */ 
/*  510 */         localArrayList.add(localX509CertificatePair);
/*      */       } catch (CertificateException localCertificateException) {
/*  512 */         if (debug != null) {
/*  513 */           debug.println("LDAPCertStore.getCertPairs() encountered exception while parsing cert, skipping the bad data: ");
/*      */ 
/*  516 */           HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*  517 */           debug.println("[ " + localHexDumpEncoder.encodeBuffer(arrayOfByte[j]) + " ]");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  523 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private Collection<X509Certificate> getMatchingCrossCerts(LDAPRequest paramLDAPRequest, X509CertSelector paramX509CertSelector1, X509CertSelector paramX509CertSelector2)
/*      */     throws CertStoreException
/*      */   {
/*  547 */     Collection localCollection = getCertPairs(paramLDAPRequest, "crossCertificatePair;binary");
/*      */ 
/*  551 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  553 */     for (X509CertificatePair localX509CertificatePair : localCollection)
/*      */     {
/*      */       X509Certificate localX509Certificate;
/*  555 */       if (paramX509CertSelector1 != null) {
/*  556 */         localX509Certificate = localX509CertificatePair.getForward();
/*  557 */         if ((localX509Certificate != null) && (paramX509CertSelector1.match(localX509Certificate))) {
/*  558 */           localArrayList.add(localX509Certificate);
/*      */         }
/*      */       }
/*  561 */       if (paramX509CertSelector2 != null) {
/*  562 */         localX509Certificate = localX509CertificatePair.getReverse();
/*  563 */         if ((localX509Certificate != null) && (paramX509CertSelector2.match(localX509Certificate))) {
/*  564 */           localArrayList.add(localX509Certificate);
/*      */         }
/*      */       }
/*      */     }
/*  568 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public synchronized Collection<X509Certificate> engineGetCertificates(CertSelector paramCertSelector)
/*      */     throws CertStoreException
/*      */   {
/*  594 */     if (debug != null) {
/*  595 */       debug.println("LDAPCertStore.engineGetCertificates() selector: " + String.valueOf(paramCertSelector));
/*      */     }
/*      */ 
/*  599 */     if (paramCertSelector == null) {
/*  600 */       paramCertSelector = new X509CertSelector();
/*      */     }
/*  602 */     if (!(paramCertSelector instanceof X509CertSelector)) {
/*  603 */       throw new CertStoreException("LDAPCertStore needs an X509CertSelector to find certs");
/*      */     }
/*      */ 
/*  606 */     X509CertSelector localX509CertSelector = (X509CertSelector)paramCertSelector;
/*  607 */     int i = localX509CertSelector.getBasicConstraints();
/*  608 */     String str1 = localX509CertSelector.getSubjectAsString();
/*  609 */     String str2 = localX509CertSelector.getIssuerAsString();
/*  610 */     HashSet localHashSet = new HashSet();
/*  611 */     if (debug != null)
/*  612 */       debug.println("LDAPCertStore.engineGetCertificates() basicConstraints: " + i);
/*      */     LDAPRequest localLDAPRequest;
/*  621 */     if (str1 != null) {
/*  622 */       if (debug != null) {
/*  623 */         debug.println("LDAPCertStore.engineGetCertificates() subject is not null");
/*      */       }
/*      */ 
/*  626 */       localLDAPRequest = new LDAPRequest(str1);
/*  627 */       if (i > -2) {
/*  628 */         localLDAPRequest.addRequestedAttribute("crossCertificatePair;binary");
/*  629 */         localLDAPRequest.addRequestedAttribute("cACertificate;binary");
/*  630 */         localLDAPRequest.addRequestedAttribute("authorityRevocationList;binary");
/*  631 */         if (this.prefetchCRLs) {
/*  632 */           localLDAPRequest.addRequestedAttribute("certificateRevocationList;binary");
/*      */         }
/*      */       }
/*  635 */       if (i < 0) {
/*  636 */         localLDAPRequest.addRequestedAttribute("userCertificate;binary");
/*      */       }
/*      */ 
/*  639 */       if (i > -2) {
/*  640 */         localHashSet.addAll(getMatchingCrossCerts(localLDAPRequest, localX509CertSelector, null));
/*  641 */         if (debug != null) {
/*  642 */           debug.println("LDAPCertStore.engineGetCertificates() after getMatchingCrossCerts(subject,xsel,null),certs.size(): " + localHashSet.size());
/*      */         }
/*      */ 
/*  646 */         localHashSet.addAll(getCertificates(localLDAPRequest, "cACertificate;binary", localX509CertSelector));
/*  647 */         if (debug != null) {
/*  648 */           debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(subject,CA_CERT,xsel),certs.size(): " + localHashSet.size());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  653 */       if (i < 0) {
/*  654 */         localHashSet.addAll(getCertificates(localLDAPRequest, "userCertificate;binary", localX509CertSelector));
/*  655 */         if (debug != null) {
/*  656 */           debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(subject,USER_CERT, xsel),certs.size(): " + localHashSet.size());
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  662 */       if (debug != null) {
/*  663 */         debug.println("LDAPCertStore.engineGetCertificates() subject is null");
/*      */       }
/*      */ 
/*  666 */       if (i == -2) {
/*  667 */         throw new CertStoreException("need subject to find EE certs");
/*      */       }
/*  669 */       if (str2 == null) {
/*  670 */         throw new CertStoreException("need subject or issuer to find certs");
/*      */       }
/*      */     }
/*  673 */     if (debug != null) {
/*  674 */       debug.println("LDAPCertStore.engineGetCertificates() about to getMatchingCrossCerts...");
/*      */     }
/*      */ 
/*  677 */     if ((str2 != null) && (i > -2)) {
/*  678 */       localLDAPRequest = new LDAPRequest(str2);
/*  679 */       localLDAPRequest.addRequestedAttribute("crossCertificatePair;binary");
/*  680 */       localLDAPRequest.addRequestedAttribute("cACertificate;binary");
/*  681 */       localLDAPRequest.addRequestedAttribute("authorityRevocationList;binary");
/*  682 */       if (this.prefetchCRLs) {
/*  683 */         localLDAPRequest.addRequestedAttribute("certificateRevocationList;binary");
/*      */       }
/*      */ 
/*  686 */       localHashSet.addAll(getMatchingCrossCerts(localLDAPRequest, null, localX509CertSelector));
/*  687 */       if (debug != null) {
/*  688 */         debug.println("LDAPCertStore.engineGetCertificates() after getMatchingCrossCerts(issuer,null,xsel),certs.size(): " + localHashSet.size());
/*      */       }
/*      */ 
/*  692 */       localHashSet.addAll(getCertificates(localLDAPRequest, "cACertificate;binary", localX509CertSelector));
/*  693 */       if (debug != null) {
/*  694 */         debug.println("LDAPCertStore.engineGetCertificates() after getCertificates(issuer,CA_CERT,xsel),certs.size(): " + localHashSet.size());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  699 */     if (debug != null) {
/*  700 */       debug.println("LDAPCertStore.engineGetCertificates() returning certs");
/*      */     }
/*  702 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   private Collection<X509CRL> getCRLs(LDAPRequest paramLDAPRequest, String paramString, X509CRLSelector paramX509CRLSelector)
/*      */     throws CertStoreException
/*      */   {
/*      */     byte[][] arrayOfByte;
/*      */     try
/*      */     {
/*  722 */       arrayOfByte = paramLDAPRequest.getValues(paramString);
/*      */     } catch (NamingException localNamingException) {
/*  724 */       throw new CertStoreException(localNamingException);
/*      */     }
/*      */ 
/*  727 */     int i = arrayOfByte.length;
/*  728 */     if (i == 0) {
/*  729 */       return Collections.emptySet();
/*      */     }
/*      */ 
/*  732 */     ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  734 */     for (int j = 0; j < i; j++) {
/*      */       try {
/*  736 */         CRL localCRL = this.cf.generateCRL(new ByteArrayInputStream(arrayOfByte[j]));
/*  737 */         if (paramX509CRLSelector.match(localCRL))
/*  738 */           localArrayList.add((X509CRL)localCRL);
/*      */       }
/*      */       catch (CRLException localCRLException) {
/*  741 */         if (debug != null) {
/*  742 */           debug.println("LDAPCertStore.getCRLs() encountered exception while parsing CRL, skipping the bad data: ");
/*      */ 
/*  744 */           HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*  745 */           debug.println("[ " + localHexDumpEncoder.encodeBuffer(arrayOfByte[j]) + " ]");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  750 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public synchronized Collection<X509CRL> engineGetCRLs(CRLSelector paramCRLSelector)
/*      */     throws CertStoreException
/*      */   {
/*  776 */     if (debug != null) {
/*  777 */       debug.println("LDAPCertStore.engineGetCRLs() selector: " + paramCRLSelector);
/*      */     }
/*      */ 
/*  781 */     if (paramCRLSelector == null) {
/*  782 */       paramCRLSelector = new X509CRLSelector();
/*      */     }
/*  784 */     if (!(paramCRLSelector instanceof X509CRLSelector)) {
/*  785 */       throw new CertStoreException("need X509CRLSelector to find CRLs");
/*      */     }
/*  787 */     X509CRLSelector localX509CRLSelector = (X509CRLSelector)paramCRLSelector;
/*  788 */     HashSet localHashSet = new HashSet();
/*      */ 
/*  792 */     X509Certificate localX509Certificate = localX509CRLSelector.getCertificateChecking();
/*      */     Object localObject1;
/*  793 */     if (localX509Certificate != null) {
/*  794 */       localObject1 = new HashSet();
/*  795 */       localObject2 = localX509Certificate.getIssuerX500Principal();
/*  796 */       ((Collection)localObject1).add(((X500Principal)localObject2).getName("RFC2253"));
/*      */     }
/*      */     else
/*      */     {
/*  800 */       localObject1 = localX509CRLSelector.getIssuerNames();
/*  801 */       if (localObject1 == null) {
/*  802 */         throw new CertStoreException("need issuerNames or certChecking to find CRLs");
/*      */       }
/*      */     }
/*      */ 
/*  806 */     for (Object localObject2 = ((Collection)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { Object localObject3 = ((Iterator)localObject2).next();
/*      */       String str;
/*  808 */       if ((localObject3 instanceof byte[])) {
/*      */         try {
/*  810 */           X500Principal localX500Principal = new X500Principal((byte[])localObject3);
/*  811 */           str = localX500Principal.getName("RFC2253");
/*      */         } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */         }
/*      */       }
/*      */       else {
/*  816 */         str = (String)localObject3;
/*      */ 
/*  819 */         Object localObject4 = Collections.emptySet();
/*      */         LDAPRequest localLDAPRequest;
/*  820 */         if ((localX509Certificate == null) || (localX509Certificate.getBasicConstraints() != -1)) {
/*  821 */           localLDAPRequest = new LDAPRequest(str);
/*  822 */           localLDAPRequest.addRequestedAttribute("crossCertificatePair;binary");
/*  823 */           localLDAPRequest.addRequestedAttribute("cACertificate;binary");
/*  824 */           localLDAPRequest.addRequestedAttribute("authorityRevocationList;binary");
/*  825 */           if (this.prefetchCRLs)
/*  826 */             localLDAPRequest.addRequestedAttribute("certificateRevocationList;binary");
/*      */           try
/*      */           {
/*  829 */             localObject4 = getCRLs(localLDAPRequest, "authorityRevocationList;binary", localX509CRLSelector);
/*  830 */             if (((Collection)localObject4).isEmpty())
/*      */             {
/*  833 */               this.prefetchCRLs = true;
/*      */             }
/*  835 */             else localHashSet.addAll((Collection)localObject4); 
/*      */           }
/*      */           catch (CertStoreException localCertStoreException)
/*      */           {
/*  838 */             if (debug != null) {
/*  839 */               debug.println("LDAPCertStore.engineGetCRLs non-fatal error retrieving ARLs:" + localCertStoreException);
/*      */ 
/*  841 */               localCertStoreException.printStackTrace();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  848 */         if ((((Collection)localObject4).isEmpty()) || (localX509Certificate == null)) {
/*  849 */           localLDAPRequest = new LDAPRequest(str);
/*  850 */           localLDAPRequest.addRequestedAttribute("certificateRevocationList;binary");
/*  851 */           localObject4 = getCRLs(localLDAPRequest, "certificateRevocationList;binary", localX509CRLSelector);
/*  852 */           localHashSet.addAll((Collection)localObject4);
/*      */         }
/*      */       } }
/*  855 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   static LDAPCertStoreParameters getParameters(URI paramURI)
/*      */   {
/*  860 */     String str = paramURI.getHost();
/*  861 */     if (str == null) {
/*  862 */       return new SunLDAPCertStoreParameters();
/*      */     }
/*  864 */     int i = paramURI.getPort();
/*  865 */     return i == -1 ? new SunLDAPCertStoreParameters(str) : new SunLDAPCertStoreParameters(str, i);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  148 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.security.certpath.ldap.cache.lifetime"));
/*      */ 
/*  150 */     if (str != null)
/*  151 */       LIFETIME = Integer.parseInt(str);
/*      */     else
/*  153 */       LIFETIME = 30;
/*      */   }
/*      */ 
/*      */   static class LDAPCRLSelector extends X509CRLSelector
/*      */   {
/*      */     private X509CRLSelector selector;
/*      */     private Collection<X500Principal> certIssuers;
/*      */     private Collection<X500Principal> issuers;
/*      */     private HashSet<Object> issuerNames;
/*      */ 
/*      */     LDAPCRLSelector(X509CRLSelector paramX509CRLSelector, Collection<X500Principal> paramCollection, String paramString)
/*      */       throws IOException
/*      */     {
/* 1048 */       this.selector = (paramX509CRLSelector == null ? new X509CRLSelector() : paramX509CRLSelector);
/* 1049 */       this.certIssuers = paramCollection;
/* 1050 */       this.issuerNames = new HashSet();
/* 1051 */       this.issuerNames.add(paramString);
/* 1052 */       this.issuers = new HashSet();
/* 1053 */       this.issuers.add(new X500Name(paramString).asX500Principal());
/*      */     }
/*      */ 
/*      */     public Collection<X500Principal> getIssuers()
/*      */     {
/* 1059 */       return Collections.unmodifiableCollection(this.issuers);
/*      */     }
/*      */ 
/*      */     public Collection<Object> getIssuerNames() {
/* 1063 */       return Collections.unmodifiableCollection(this.issuerNames);
/*      */     }
/*      */     public BigInteger getMinCRL() {
/* 1066 */       return this.selector.getMinCRL();
/*      */     }
/*      */     public BigInteger getMaxCRL() {
/* 1069 */       return this.selector.getMaxCRL();
/*      */     }
/*      */     public Date getDateAndTime() {
/* 1072 */       return this.selector.getDateAndTime();
/*      */     }
/*      */     public X509Certificate getCertificateChecking() {
/* 1075 */       return this.selector.getCertificateChecking();
/*      */     }
/*      */ 
/*      */     public boolean match(CRL paramCRL)
/*      */     {
/* 1080 */       this.selector.setIssuers(this.certIssuers);
/* 1081 */       boolean bool = this.selector.match(paramCRL);
/* 1082 */       this.selector.setIssuers(this.issuers);
/* 1083 */       return bool;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LDAPCertSelector extends X509CertSelector
/*      */   {
/*      */     private X500Principal certSubject;
/*      */     private X509CertSelector selector;
/*      */     private X500Principal subject;
/*      */ 
/*      */     LDAPCertSelector(X509CertSelector paramX509CertSelector, X500Principal paramX500Principal, String paramString)
/*      */       throws IOException
/*      */     {
/*  939 */       this.selector = (paramX509CertSelector == null ? new X509CertSelector() : paramX509CertSelector);
/*  940 */       this.certSubject = paramX500Principal;
/*  941 */       this.subject = new X500Name(paramString).asX500Principal();
/*      */     }
/*      */ 
/*      */     public X509Certificate getCertificate()
/*      */     {
/*  947 */       return this.selector.getCertificate();
/*      */     }
/*      */     public BigInteger getSerialNumber() {
/*  950 */       return this.selector.getSerialNumber();
/*      */     }
/*      */     public X500Principal getIssuer() {
/*  953 */       return this.selector.getIssuer();
/*      */     }
/*      */     public String getIssuerAsString() {
/*  956 */       return this.selector.getIssuerAsString();
/*      */     }
/*      */     public byte[] getIssuerAsBytes() throws IOException {
/*  959 */       return this.selector.getIssuerAsBytes();
/*      */     }
/*      */ 
/*      */     public X500Principal getSubject() {
/*  963 */       return this.subject;
/*      */     }
/*      */ 
/*      */     public String getSubjectAsString() {
/*  967 */       return this.subject.getName();
/*      */     }
/*      */ 
/*      */     public byte[] getSubjectAsBytes() throws IOException {
/*  971 */       return this.subject.getEncoded();
/*      */     }
/*      */     public byte[] getSubjectKeyIdentifier() {
/*  974 */       return this.selector.getSubjectKeyIdentifier();
/*      */     }
/*      */     public byte[] getAuthorityKeyIdentifier() {
/*  977 */       return this.selector.getAuthorityKeyIdentifier();
/*      */     }
/*      */     public Date getCertificateValid() {
/*  980 */       return this.selector.getCertificateValid();
/*      */     }
/*      */     public Date getPrivateKeyValid() {
/*  983 */       return this.selector.getPrivateKeyValid();
/*      */     }
/*      */     public String getSubjectPublicKeyAlgID() {
/*  986 */       return this.selector.getSubjectPublicKeyAlgID();
/*      */     }
/*      */     public PublicKey getSubjectPublicKey() {
/*  989 */       return this.selector.getSubjectPublicKey();
/*      */     }
/*      */     public boolean[] getKeyUsage() {
/*  992 */       return this.selector.getKeyUsage();
/*      */     }
/*      */     public Set<String> getExtendedKeyUsage() {
/*  995 */       return this.selector.getExtendedKeyUsage();
/*      */     }
/*      */     public boolean getMatchAllSubjectAltNames() {
/*  998 */       return this.selector.getMatchAllSubjectAltNames();
/*      */     }
/*      */     public Collection<List<?>> getSubjectAlternativeNames() {
/* 1001 */       return this.selector.getSubjectAlternativeNames();
/*      */     }
/*      */     public byte[] getNameConstraints() {
/* 1004 */       return this.selector.getNameConstraints();
/*      */     }
/*      */     public int getBasicConstraints() {
/* 1007 */       return this.selector.getBasicConstraints();
/*      */     }
/*      */     public Set<String> getPolicy() {
/* 1010 */       return this.selector.getPolicy();
/*      */     }
/*      */     public Collection<List<?>> getPathToNames() {
/* 1013 */       return this.selector.getPathToNames();
/*      */     }
/*      */ 
/*      */     public boolean match(Certificate paramCertificate)
/*      */     {
/* 1019 */       this.selector.setSubject(this.certSubject);
/* 1020 */       boolean bool = this.selector.match(paramCertificate);
/* 1021 */       this.selector.setSubject(this.subject);
/* 1022 */       return bool;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class LDAPRequest
/*      */   {
/*      */     private final String name;
/*      */     private Map<String, byte[][]> valueMap;
/*      */     private final List<String> requestedAttributes;
/*      */ 
/*      */     LDAPRequest(String arg2)
/*      */     {
/*      */       Object localObject;
/*  305 */       this.name = localObject;
/*  306 */       this.requestedAttributes = new ArrayList(5);
/*      */     }
/*      */ 
/*      */     String getName() {
/*  310 */       return this.name;
/*      */     }
/*      */ 
/*      */     void addRequestedAttribute(String paramString) {
/*  314 */       if (this.valueMap != null) {
/*  315 */         throw new IllegalStateException("Request already sent");
/*      */       }
/*  317 */       this.requestedAttributes.add(paramString);
/*      */     }
/*      */ 
/*      */     byte[][] getValues(String paramString)
/*      */       throws NamingException
/*      */     {
/*  333 */       String str = this.name + "|" + paramString;
/*  334 */       byte[][] arrayOfByte = (byte[][])LDAPCertStore.this.valueCache.get(str);
/*  335 */       if (arrayOfByte != null) {
/*  336 */         LDAPCertStore.access$108(LDAPCertStore.this);
/*  337 */         return arrayOfByte;
/*      */       }
/*  339 */       LDAPCertStore.access$208(LDAPCertStore.this);
/*  340 */       Map localMap = getValueMap();
/*  341 */       arrayOfByte = (byte[][])localMap.get(paramString);
/*  342 */       return arrayOfByte;
/*      */     }
/*      */ 
/*      */     private Map<String, byte[][]> getValueMap()
/*      */       throws NamingException
/*      */     {
/*  360 */       if (this.valueMap != null) {
/*  361 */         return this.valueMap;
/*      */       }
/*      */ 
/*  370 */       this.valueMap = new HashMap(8);
/*  371 */       String[] arrayOfString = (String[])this.requestedAttributes.toArray(LDAPCertStore.STRING0);
/*      */       Attributes localAttributes;
/*      */       try
/*      */       {
/*  374 */         localAttributes = LDAPCertStore.this.ctx.getAttributes(this.name, arrayOfString);
/*      */       }
/*      */       catch (NameNotFoundException localNameNotFoundException)
/*      */       {
/*  378 */         localAttributes = LDAPCertStore.EMPTY_ATTRIBUTES;
/*      */       }
/*  380 */       for (String str : this.requestedAttributes) {
/*  381 */         Attribute localAttribute = localAttributes.get(str);
/*  382 */         byte[][] arrayOfByte = getAttributeValues(localAttribute);
/*  383 */         cacheAttribute(str, arrayOfByte);
/*  384 */         this.valueMap.put(str, arrayOfByte);
/*      */       }
/*  386 */       return this.valueMap;
/*      */     }
/*      */ 
/*      */     private void cacheAttribute(String paramString, byte[][] paramArrayOfByte)
/*      */     {
/*  393 */       String str = this.name + "|" + paramString;
/*  394 */       LDAPCertStore.this.valueCache.put(str, paramArrayOfByte);
/*      */     }
/*      */ 
/*      */     private byte[][] getAttributeValues(Attribute paramAttribute)
/*      */       throws NamingException
/*      */     {
/*      */       Object localObject1;
/*  405 */       if (paramAttribute == null) {
/*  406 */         localObject1 = LDAPCertStore.BB0;
/*      */       } else {
/*  408 */         localObject1 = new byte[paramAttribute.size()][];
/*  409 */         int i = 0;
/*  410 */         NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*  411 */         while (localNamingEnumeration.hasMore()) {
/*  412 */           Object localObject2 = localNamingEnumeration.next();
/*  413 */           if ((LDAPCertStore.debug != null) && 
/*  414 */             ((localObject2 instanceof String))) {
/*  415 */             LDAPCertStore.debug.println("LDAPCertStore.getAttrValues() enum.next is a string!: " + localObject2);
/*      */           }
/*      */ 
/*  419 */           byte[] arrayOfByte = (byte[])localObject2;
/*  420 */           localObject1[(i++)] = arrayOfByte;
/*      */         }
/*      */       }
/*  423 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SunLDAPCertStoreParameters extends LDAPCertStoreParameters
/*      */   {
/*  879 */     private volatile int hashCode = 0;
/*      */ 
/*      */     SunLDAPCertStoreParameters(String paramString, int paramInt) {
/*  882 */       super(paramInt);
/*      */     }
/*      */     SunLDAPCertStoreParameters(String paramString) {
/*  885 */       super();
/*      */     }
/*      */     SunLDAPCertStoreParameters() {
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  891 */       if (!(paramObject instanceof LDAPCertStoreParameters)) {
/*  892 */         return false;
/*      */       }
/*  894 */       LDAPCertStoreParameters localLDAPCertStoreParameters = (LDAPCertStoreParameters)paramObject;
/*  895 */       return (getPort() == localLDAPCertStoreParameters.getPort()) && (getServerName().equalsIgnoreCase(localLDAPCertStoreParameters.getServerName()));
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  899 */       if (this.hashCode == 0) {
/*  900 */         int i = 17;
/*  901 */         i = 37 * i + getPort();
/*  902 */         i = 37 * i + getServerName().toLowerCase().hashCode();
/*  903 */         this.hashCode = i;
/*      */       }
/*  905 */       return this.hashCode;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ldap.LDAPCertStore
 * JD-Core Version:    0.6.2
 */