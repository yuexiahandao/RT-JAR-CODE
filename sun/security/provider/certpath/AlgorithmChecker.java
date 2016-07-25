/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AlgorithmConstraints;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.CryptoPrimitive;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.security.spec.DSAPublicKeySpec;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import sun.security.util.DisabledAlgorithmConstraints;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X509CRLImpl;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public final class AlgorithmChecker extends PKIXCertPathChecker
/*     */ {
/*     */   private final AlgorithmConstraints constraints;
/*     */   private final PublicKey trustedPubKey;
/*     */   private PublicKey prevPubKey;
/*  77 */   private static final Set<CryptoPrimitive> SIGNATURE_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.of(CryptoPrimitive.SIGNATURE));
/*     */ 
/*  81 */   private static final DisabledAlgorithmConstraints certPathDefaultConstraints = new DisabledAlgorithmConstraints("jdk.certpath.disabledAlgorithms");
/*     */ 
/*     */   public AlgorithmChecker(TrustAnchor paramTrustAnchor)
/*     */   {
/*  93 */     this(paramTrustAnchor, certPathDefaultConstraints);
/*     */   }
/*     */ 
/*     */   public AlgorithmChecker(AlgorithmConstraints paramAlgorithmConstraints)
/*     */   {
/* 107 */     this.prevPubKey = null;
/* 108 */     this.trustedPubKey = null;
/* 109 */     this.constraints = paramAlgorithmConstraints;
/*     */   }
/*     */ 
/*     */   public AlgorithmChecker(TrustAnchor paramTrustAnchor, AlgorithmConstraints paramAlgorithmConstraints)
/*     */   {
/* 125 */     if (paramTrustAnchor == null) {
/* 126 */       throw new IllegalArgumentException("The trust anchor cannot be null");
/*     */     }
/*     */ 
/* 130 */     if (paramTrustAnchor.getTrustedCert() != null)
/* 131 */       this.trustedPubKey = paramTrustAnchor.getTrustedCert().getPublicKey();
/*     */     else {
/* 133 */       this.trustedPubKey = paramTrustAnchor.getCAPublicKey();
/*     */     }
/*     */ 
/* 136 */     this.prevPubKey = this.trustedPubKey;
/* 137 */     this.constraints = paramAlgorithmConstraints;
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 143 */     if (!paramBoolean) {
/* 144 */       if (this.trustedPubKey != null)
/* 145 */         this.prevPubKey = this.trustedPubKey;
/*     */       else
/* 147 */         this.prevPubKey = null;
/*     */     }
/*     */     else
/* 150 */       throw new CertPathValidatorException("forward checking not supported");
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions()
/*     */   {
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 172 */     if ((!(paramCertificate instanceof X509Certificate)) || (this.constraints == null))
/*     */     {
/* 174 */       return;
/*     */     }
/*     */ 
/* 177 */     X509CertImpl localX509CertImpl = null;
/*     */     try {
/* 179 */       localX509CertImpl = X509CertImpl.toImpl((X509Certificate)paramCertificate);
/*     */     } catch (CertificateException localCertificateException1) {
/* 181 */       throw new CertPathValidatorException(localCertificateException1);
/*     */     }
/*     */ 
/* 184 */     PublicKey localPublicKey = localX509CertImpl.getPublicKey();
/* 185 */     String str = localX509CertImpl.getSigAlgName();
/*     */ 
/* 187 */     AlgorithmId localAlgorithmId = null;
/*     */     try {
/* 189 */       localAlgorithmId = (AlgorithmId)localX509CertImpl.get("x509.algorithm");
/*     */     } catch (CertificateException localCertificateException2) {
/* 191 */       throw new CertPathValidatorException(localCertificateException2);
/*     */     }
/*     */ 
/* 194 */     AlgorithmParameters localAlgorithmParameters = localAlgorithmId.getParameters();
/*     */ 
/* 197 */     if (!this.constraints.permits(SIGNATURE_PRIMITIVE_SET, str, localAlgorithmParameters))
/*     */     {
/* 200 */       throw new CertPathValidatorException("Algorithm constraints check failed: " + str, null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
/*     */     }
/*     */ 
/* 206 */     boolean[] arrayOfBoolean = localX509CertImpl.getKeyUsage();
/* 207 */     if ((arrayOfBoolean != null) && (arrayOfBoolean.length < 9))
/* 208 */       throw new CertPathValidatorException("incorrect KeyUsage extension", null, null, -1, PKIXReason.INVALID_KEY_USAGE);
/*     */     Object localObject;
/* 213 */     if (arrayOfBoolean != null) {
/* 214 */       localObject = EnumSet.noneOf(CryptoPrimitive.class);
/*     */ 
/* 217 */       if ((arrayOfBoolean[0] != 0) || (arrayOfBoolean[1] != 0) || (arrayOfBoolean[5] != 0) || (arrayOfBoolean[6] != 0))
/*     */       {
/* 222 */         ((Set)localObject).add(CryptoPrimitive.SIGNATURE);
/*     */       }
/*     */ 
/* 225 */       if (arrayOfBoolean[2] != 0) {
/* 226 */         ((Set)localObject).add(CryptoPrimitive.KEY_ENCAPSULATION);
/*     */       }
/*     */ 
/* 229 */       if (arrayOfBoolean[3] != 0) {
/* 230 */         ((Set)localObject).add(CryptoPrimitive.PUBLIC_KEY_ENCRYPTION);
/*     */       }
/*     */ 
/* 233 */       if (arrayOfBoolean[4] != 0) {
/* 234 */         ((Set)localObject).add(CryptoPrimitive.KEY_AGREEMENT);
/*     */       }
/*     */ 
/* 240 */       if ((!((Set)localObject).isEmpty()) && 
/* 241 */         (!this.constraints.permits((Set)localObject, localPublicKey))) {
/* 242 */         throw new CertPathValidatorException("algorithm constraints check failed", null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 250 */     if (this.prevPubKey != null) {
/* 251 */       if ((str != null) && 
/* 252 */         (!this.constraints.permits(SIGNATURE_PRIMITIVE_SET, str, this.prevPubKey, localAlgorithmParameters)))
/*     */       {
/* 255 */         throw new CertPathValidatorException("Algorithm constraints check failed: " + str, null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
/*     */       }
/*     */ 
/* 262 */       if (((localPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)localPublicKey).getParams() == null))
/*     */       {
/* 265 */         if (!(this.prevPubKey instanceof DSAPublicKey)) {
/* 266 */           throw new CertPathValidatorException("Input key is not of a appropriate type for inheriting parameters");
/*     */         }
/*     */ 
/* 270 */         localObject = ((DSAPublicKey)this.prevPubKey).getParams();
/* 271 */         if (localObject == null) {
/* 272 */           throw new CertPathValidatorException("Key parameters missing");
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 277 */           BigInteger localBigInteger = ((DSAPublicKey)localPublicKey).getY();
/* 278 */           KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
/* 279 */           DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(localBigInteger, ((DSAParams)localObject).getP(), ((DSAParams)localObject).getQ(), ((DSAParams)localObject).getG());
/*     */ 
/* 283 */           localPublicKey = localKeyFactory.generatePublic(localDSAPublicKeySpec);
/*     */         } catch (GeneralSecurityException localGeneralSecurityException) {
/* 285 */           throw new CertPathValidatorException("Unable to generate key with inherited parameters: " + localGeneralSecurityException.getMessage(), localGeneralSecurityException);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 292 */     this.prevPubKey = localPublicKey;
/*     */   }
/*     */ 
/*     */   void trySetTrustAnchor(TrustAnchor paramTrustAnchor)
/*     */   {
/* 312 */     if (this.prevPubKey == null) {
/* 313 */       if (paramTrustAnchor == null) {
/* 314 */         throw new IllegalArgumentException("The trust anchor cannot be null");
/*     */       }
/*     */ 
/* 319 */       if (paramTrustAnchor.getTrustedCert() != null)
/* 320 */         this.prevPubKey = paramTrustAnchor.getTrustedCert().getPublicKey();
/*     */       else
/* 322 */         this.prevPubKey = paramTrustAnchor.getCAPublicKey();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void check(PublicKey paramPublicKey, X509CRL paramX509CRL)
/*     */     throws CertPathValidatorException
/*     */   {
/* 336 */     X509CRLImpl localX509CRLImpl = null;
/*     */     try {
/* 338 */       localX509CRLImpl = X509CRLImpl.toImpl(paramX509CRL);
/*     */     } catch (CRLException localCRLException) {
/* 340 */       throw new CertPathValidatorException(localCRLException);
/*     */     }
/*     */ 
/* 343 */     AlgorithmId localAlgorithmId = localX509CRLImpl.getSigAlgId();
/* 344 */     check(paramPublicKey, localAlgorithmId);
/*     */   }
/*     */ 
/*     */   static void check(PublicKey paramPublicKey, AlgorithmId paramAlgorithmId)
/*     */     throws CertPathValidatorException
/*     */   {
/* 355 */     String str = paramAlgorithmId.getName();
/* 356 */     AlgorithmParameters localAlgorithmParameters = paramAlgorithmId.getParameters();
/*     */ 
/* 358 */     if (!certPathDefaultConstraints.permits(SIGNATURE_PRIMITIVE_SET, str, paramPublicKey, localAlgorithmParameters))
/*     */     {
/* 360 */       throw new CertPathValidatorException("algorithm check failed: " + str + " is disabled", null, null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.AlgorithmChecker
 * JD-Core Version:    0.6.2
 */