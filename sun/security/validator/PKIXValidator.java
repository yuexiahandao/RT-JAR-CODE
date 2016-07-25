/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.AlgorithmConstraints;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.CertPathBuilder;
/*     */ import java.security.cert.CertPathValidator;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CollectionCertStoreParameters;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.PKIXCertPathBuilderResult;
/*     */ import java.security.cert.PKIXCertPathValidatorResult;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.provider.certpath.AlgorithmChecker;
/*     */ 
/*     */ public final class PKIXValidator extends Validator
/*     */ {
/*  59 */   private static final boolean checkTLSRevocation = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("com.sun.net.ssl.checkRevocation"))).booleanValue();
/*     */   private static final boolean TRY_VALIDATOR = true;
/*     */   private final Set<X509Certificate> trustedCerts;
/*     */   private final PKIXBuilderParameters parameterTemplate;
/*  68 */   private int certPathLength = -1;
/*     */   private final Map<X500Principal, List<PublicKey>> trustedSubjects;
/*     */   private final CertificateFactory factory;
/*     */   private final boolean plugin;
/*     */ 
/*     */   PKIXValidator(String paramString, Collection<X509Certificate> paramCollection)
/*     */   {
/*  77 */     super("PKIX", paramString);
/*  78 */     if ((paramCollection instanceof Set))
/*  79 */       this.trustedCerts = ((Set)paramCollection);
/*     */     else {
/*  81 */       this.trustedCerts = new HashSet(paramCollection);
/*     */     }
/*  83 */     HashSet localHashSet = new HashSet();
/*  84 */     for (Iterator localIterator1 = paramCollection.iterator(); localIterator1.hasNext(); ) { localX509Certificate = (X509Certificate)localIterator1.next();
/*  85 */       localHashSet.add(new TrustAnchor(localX509Certificate, null)); }
/*     */     X509Certificate localX509Certificate;
/*     */     try {
/*  88 */       this.parameterTemplate = new PKIXBuilderParameters(localHashSet, null);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/*  90 */       throw new RuntimeException("Unexpected error: " + localInvalidAlgorithmParameterException.toString(), localInvalidAlgorithmParameterException);
/*     */     }
/*  92 */     setDefaultParameters(paramString);
/*     */ 
/*  99 */     this.trustedSubjects = new HashMap();
/* 100 */     for (Iterator localIterator2 = paramCollection.iterator(); localIterator2.hasNext(); ) { localX509Certificate = (X509Certificate)localIterator2.next();
/* 101 */       X500Principal localX500Principal = localX509Certificate.getSubjectX500Principal();
/*     */       Object localObject;
/* 103 */       if (this.trustedSubjects.containsKey(localX500Principal)) {
/* 104 */         localObject = (List)this.trustedSubjects.get(localX500Principal);
/*     */       } else {
/* 106 */         localObject = new ArrayList();
/* 107 */         this.trustedSubjects.put(localX500Principal, localObject);
/*     */       }
/* 109 */       ((List)localObject).add(localX509Certificate.getPublicKey()); }
/*     */     try
/*     */     {
/* 112 */       this.factory = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException localCertificateException) {
/* 114 */       throw new RuntimeException("Internal error", localCertificateException);
/*     */     }
/* 116 */     this.plugin = paramString.equals("plugin code signing");
/*     */   }
/*     */ 
/*     */   PKIXValidator(String paramString, PKIXBuilderParameters paramPKIXBuilderParameters)
/*     */   {
/* 123 */     super("PKIX", paramString);
/* 124 */     this.trustedCerts = new HashSet();
/* 125 */     for (Iterator localIterator = paramPKIXBuilderParameters.getTrustAnchors().iterator(); localIterator.hasNext(); ) { localObject1 = (TrustAnchor)localIterator.next();
/* 126 */       localObject2 = ((TrustAnchor)localObject1).getTrustedCert();
/* 127 */       if (localObject2 != null)
/* 128 */         this.trustedCerts.add(localObject2);
/*     */     }
/* 139 */     Object localObject1;
/*     */     Object localObject2;
/* 131 */     this.parameterTemplate = paramPKIXBuilderParameters;
/*     */ 
/* 138 */     this.trustedSubjects = new HashMap();
/* 139 */     for (localIterator = this.trustedCerts.iterator(); localIterator.hasNext(); ) { localObject1 = (X509Certificate)localIterator.next();
/* 140 */       localObject2 = ((X509Certificate)localObject1).getSubjectX500Principal();
/*     */       Object localObject3;
/* 142 */       if (this.trustedSubjects.containsKey(localObject2)) {
/* 143 */         localObject3 = (List)this.trustedSubjects.get(localObject2);
/*     */       } else {
/* 145 */         localObject3 = new ArrayList();
/* 146 */         this.trustedSubjects.put(localObject2, localObject3);
/*     */       }
/* 148 */       ((List)localObject3).add(((X509Certificate)localObject1).getPublicKey()); }
/*     */     try
/*     */     {
/* 151 */       this.factory = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException localCertificateException) {
/* 153 */       throw new RuntimeException("Internal error", localCertificateException);
/*     */     }
/* 155 */     this.plugin = paramString.equals("plugin code signing");
/*     */   }
/*     */ 
/*     */   public Collection<X509Certificate> getTrustedCertificates()
/*     */   {
/* 162 */     return this.trustedCerts;
/*     */   }
/*     */ 
/*     */   public int getCertPathLength()
/*     */   {
/* 176 */     return this.certPathLength;
/*     */   }
/*     */ 
/*     */   private void setDefaultParameters(String paramString)
/*     */   {
/* 184 */     if ((paramString == "tls server") || (paramString == "tls client"))
/*     */     {
/* 186 */       this.parameterTemplate.setRevocationEnabled(checkTLSRevocation);
/*     */     }
/* 188 */     else this.parameterTemplate.setRevocationEnabled(false);
/*     */   }
/*     */ 
/*     */   public PKIXBuilderParameters getParameters()
/*     */   {
/* 198 */     return this.parameterTemplate;
/*     */   }
/*     */ 
/*     */   X509Certificate[] engineValidate(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, AlgorithmConstraints paramAlgorithmConstraints, Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 206 */     if ((paramArrayOfX509Certificate == null) || (paramArrayOfX509Certificate.length == 0)) {
/* 207 */       throw new CertificateException("null or zero-length certificate chain");
/*     */     }
/*     */ 
/* 212 */     PKIXBuilderParameters localPKIXBuilderParameters = (PKIXBuilderParameters)this.parameterTemplate.clone();
/*     */ 
/* 214 */     AlgorithmChecker localAlgorithmChecker = null;
/* 215 */     if (paramAlgorithmConstraints != null) {
/* 216 */       localAlgorithmChecker = new AlgorithmChecker(paramAlgorithmConstraints);
/* 217 */       localPKIXBuilderParameters.addCertPathChecker(localAlgorithmChecker);
/*     */     }
/*     */ 
/* 223 */     Object localObject1 = null;
/*     */     X509Certificate[] arrayOfX509Certificate;
/* 224 */     for (int i = 0; i < paramArrayOfX509Certificate.length; i++) {
/* 225 */       localObject2 = paramArrayOfX509Certificate[i];
/* 226 */       localX500Principal = ((X509Certificate)localObject2).getSubjectX500Principal();
/* 227 */       if ((i != 0) && (!localX500Principal.equals(localObject1)))
/*     */       {
/* 230 */         return doBuild(paramArrayOfX509Certificate, paramCollection, localPKIXBuilderParameters);
/*     */       }
/*     */ 
/* 239 */       if ((this.trustedCerts.contains(localObject2)) || ((this.trustedSubjects.containsKey(localX500Principal)) && (((List)this.trustedSubjects.get(localX500Principal)).contains(((X509Certificate)localObject2).getPublicKey()))))
/*     */       {
/* 243 */         if (i == 0) {
/* 244 */           return new X509Certificate[] { paramArrayOfX509Certificate[0] };
/*     */         }
/*     */ 
/* 247 */         arrayOfX509Certificate = new X509Certificate[i];
/* 248 */         System.arraycopy(paramArrayOfX509Certificate, 0, arrayOfX509Certificate, 0, i);
/* 249 */         return doValidate(arrayOfX509Certificate, localPKIXBuilderParameters);
/*     */       }
/* 251 */       localObject1 = ((X509Certificate)localObject2).getIssuerX500Principal();
/*     */     }
/*     */ 
/* 255 */     X509Certificate localX509Certificate = paramArrayOfX509Certificate[(paramArrayOfX509Certificate.length - 1)];
/* 256 */     Object localObject2 = localX509Certificate.getIssuerX500Principal();
/* 257 */     X500Principal localX500Principal = localX509Certificate.getSubjectX500Principal();
/* 258 */     if ((this.trustedSubjects.containsKey(localObject2)) && (isSignatureValid((List)this.trustedSubjects.get(localObject2), localX509Certificate)))
/*     */     {
/* 260 */       return doValidate(paramArrayOfX509Certificate, localPKIXBuilderParameters);
/*     */     }
/*     */ 
/* 264 */     if (this.plugin)
/*     */     {
/* 268 */       if (paramArrayOfX509Certificate.length > 1) {
/* 269 */         arrayOfX509Certificate = new X509Certificate[paramArrayOfX509Certificate.length - 1];
/*     */ 
/* 271 */         System.arraycopy(paramArrayOfX509Certificate, 0, arrayOfX509Certificate, 0, arrayOfX509Certificate.length);
/*     */         try
/*     */         {
/* 275 */           localPKIXBuilderParameters.setTrustAnchors(Collections.singleton(new TrustAnchor(paramArrayOfX509Certificate[(paramArrayOfX509Certificate.length - 1)], null)));
/*     */         }
/*     */         catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
/*     */         {
/* 280 */           throw new CertificateException(localInvalidAlgorithmParameterException);
/*     */         }
/* 282 */         doValidate(arrayOfX509Certificate, localPKIXBuilderParameters);
/*     */       }
/*     */ 
/* 286 */       throw new ValidatorException(ValidatorException.T_NO_TRUST_ANCHOR);
/*     */     }
/*     */ 
/* 292 */     return doBuild(paramArrayOfX509Certificate, paramCollection, localPKIXBuilderParameters);
/*     */   }
/*     */ 
/*     */   private boolean isSignatureValid(List<PublicKey> paramList, X509Certificate paramX509Certificate)
/*     */   {
/* 297 */     if (this.plugin) {
/* 298 */       for (PublicKey localPublicKey : paramList)
/*     */         try {
/* 300 */           paramX509Certificate.verify(localPublicKey);
/* 301 */           return true;
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/* 306 */       return false;
/*     */     }
/* 308 */     return true;
/*     */   }
/*     */ 
/*     */   private static X509Certificate[] toArray(CertPath paramCertPath, TrustAnchor paramTrustAnchor) throws CertificateException
/*     */   {
/* 313 */     List localList = paramCertPath.getCertificates();
/*     */ 
/* 315 */     X509Certificate[] arrayOfX509Certificate = new X509Certificate[localList.size() + 1];
/* 316 */     localList.toArray(arrayOfX509Certificate);
/* 317 */     X509Certificate localX509Certificate = paramTrustAnchor.getTrustedCert();
/* 318 */     if (localX509Certificate == null) {
/* 319 */       throw new ValidatorException("TrustAnchor must be specified as certificate");
/*     */     }
/*     */ 
/* 322 */     arrayOfX509Certificate[(arrayOfX509Certificate.length - 1)] = localX509Certificate;
/* 323 */     return arrayOfX509Certificate;
/*     */   }
/*     */ 
/*     */   private void setDate(PKIXBuilderParameters paramPKIXBuilderParameters)
/*     */   {
/* 330 */     Date localDate = this.validationDate;
/* 331 */     if (localDate != null)
/* 332 */       paramPKIXBuilderParameters.setDate(localDate);
/*     */   }
/*     */ 
/*     */   private X509Certificate[] doValidate(X509Certificate[] paramArrayOfX509Certificate, PKIXBuilderParameters paramPKIXBuilderParameters) throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 339 */       setDate(paramPKIXBuilderParameters);
/*     */ 
/* 342 */       CertPathValidator localCertPathValidator = CertPathValidator.getInstance("PKIX");
/* 343 */       CertPath localCertPath = this.factory.generateCertPath(Arrays.asList(paramArrayOfX509Certificate));
/* 344 */       this.certPathLength = paramArrayOfX509Certificate.length;
/* 345 */       PKIXCertPathValidatorResult localPKIXCertPathValidatorResult = (PKIXCertPathValidatorResult)localCertPathValidator.validate(localCertPath, paramPKIXBuilderParameters);
/*     */ 
/* 348 */       return toArray(localCertPath, localPKIXCertPathValidatorResult.getTrustAnchor());
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 350 */       throw new ValidatorException("PKIX path validation failed: " + localGeneralSecurityException.toString(), localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private X509Certificate[] doBuild(X509Certificate[] paramArrayOfX509Certificate, Collection<X509Certificate> paramCollection, PKIXBuilderParameters paramPKIXBuilderParameters)
/*     */     throws CertificateException
/*     */   {
/*     */     try
/*     */     {
/* 360 */       setDate(paramPKIXBuilderParameters);
/*     */ 
/* 363 */       X509CertSelector localX509CertSelector = new X509CertSelector();
/* 364 */       localX509CertSelector.setCertificate(paramArrayOfX509Certificate[0]);
/* 365 */       paramPKIXBuilderParameters.setTargetCertConstraints(localX509CertSelector);
/*     */ 
/* 368 */       ArrayList localArrayList = new ArrayList();
/*     */ 
/* 370 */       localArrayList.addAll(Arrays.asList(paramArrayOfX509Certificate));
/* 371 */       if (paramCollection != null) {
/* 372 */         localArrayList.addAll(paramCollection);
/*     */       }
/* 374 */       CertStore localCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(localArrayList));
/*     */ 
/* 376 */       paramPKIXBuilderParameters.addCertStore(localCertStore);
/*     */ 
/* 379 */       CertPathBuilder localCertPathBuilder = CertPathBuilder.getInstance("PKIX");
/* 380 */       PKIXCertPathBuilderResult localPKIXCertPathBuilderResult = (PKIXCertPathBuilderResult)localCertPathBuilder.build(paramPKIXBuilderParameters);
/*     */ 
/* 383 */       return toArray(localPKIXCertPathBuilderResult.getCertPath(), localPKIXCertPathBuilderResult.getTrustAnchor());
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 385 */       throw new ValidatorException("PKIX path building failed: " + localGeneralSecurityException.toString(), localGeneralSecurityException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.PKIXValidator
 * JD-Core Version:    0.6.2
 */