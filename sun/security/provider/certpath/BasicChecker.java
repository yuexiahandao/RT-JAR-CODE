/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertPathValidatorException.BasicReason;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.PKIXCertPathChecker;
/*     */ import java.security.cert.PKIXReason;
/*     */ import java.security.cert.TrustAnchor;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.security.spec.DSAPublicKeySpec;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ class BasicChecker extends PKIXCertPathChecker
/*     */ {
/*  62 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private final PublicKey trustedPubKey;
/*     */   private final X500Principal caName;
/*     */   private final Date testDate;
/*     */   private final String sigProvider;
/*     */   private final boolean sigOnly;
/*     */   private X500Principal prevSubject;
/*     */   private PublicKey prevPubKey;
/*     */ 
/*     */   BasicChecker(TrustAnchor paramTrustAnchor, Date paramDate, String paramString, boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/*  84 */     if (paramTrustAnchor.getTrustedCert() != null) {
/*  85 */       this.trustedPubKey = paramTrustAnchor.getTrustedCert().getPublicKey();
/*  86 */       this.caName = paramTrustAnchor.getTrustedCert().getSubjectX500Principal();
/*     */     } else {
/*  88 */       this.trustedPubKey = paramTrustAnchor.getCAPublicKey();
/*  89 */       this.caName = paramTrustAnchor.getCA();
/*     */     }
/*  91 */     this.testDate = paramDate;
/*  92 */     this.sigProvider = paramString;
/*  93 */     this.sigOnly = paramBoolean;
/*  94 */     init(false);
/*     */   }
/*     */ 
/*     */   public void init(boolean paramBoolean)
/*     */     throws CertPathValidatorException
/*     */   {
/* 102 */     if (!paramBoolean) {
/* 103 */       this.prevPubKey = this.trustedPubKey;
/* 104 */       this.prevSubject = this.caName;
/*     */     } else {
/* 106 */       throw new CertPathValidatorException("forward checking not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isForwardCheckingSupported()
/*     */   {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   public Set<String> getSupportedExtensions() {
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   public void check(Certificate paramCertificate, Collection<String> paramCollection)
/*     */     throws CertPathValidatorException
/*     */   {
/* 133 */     X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
/*     */ 
/* 135 */     if (!this.sigOnly) {
/* 136 */       verifyTimestamp(localX509Certificate, this.testDate);
/* 137 */       verifyNameChaining(localX509Certificate, this.prevSubject);
/*     */     }
/* 139 */     verifySignature(localX509Certificate, this.prevPubKey, this.sigProvider);
/*     */ 
/* 141 */     updateState(localX509Certificate);
/*     */   }
/*     */ 
/*     */   private void verifySignature(X509Certificate paramX509Certificate, PublicKey paramPublicKey, String paramString)
/*     */     throws CertPathValidatorException
/*     */   {
/* 155 */     String str = "signature";
/* 156 */     if (debug != null)
/* 157 */       debug.println("---checking " + str + "...");
/*     */     try
/*     */     {
/* 160 */       paramX509Certificate.verify(paramPublicKey, paramString);
/*     */     } catch (SignatureException localSignatureException) {
/* 162 */       throw new CertPathValidatorException(str + " check failed", localSignatureException, null, -1, CertPathValidatorException.BasicReason.INVALID_SIGNATURE);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 166 */       throw new CertPathValidatorException(str + " check failed", localException);
/*     */     }
/*     */ 
/* 169 */     if (debug != null)
/* 170 */       debug.println(str + " verified.");
/*     */   }
/*     */ 
/*     */   private void verifyTimestamp(X509Certificate paramX509Certificate, Date paramDate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 179 */     String str = "timestamp";
/* 180 */     if (debug != null)
/* 181 */       debug.println("---checking " + str + ":" + paramDate.toString() + "...");
/*     */     try
/*     */     {
/* 184 */       paramX509Certificate.checkValidity(paramDate);
/*     */     } catch (CertificateExpiredException localCertificateExpiredException) {
/* 186 */       throw new CertPathValidatorException(str + " check failed", localCertificateExpiredException, null, -1, CertPathValidatorException.BasicReason.EXPIRED);
/*     */     }
/*     */     catch (CertificateNotYetValidException localCertificateNotYetValidException) {
/* 189 */       throw new CertPathValidatorException(str + " check failed", localCertificateNotYetValidException, null, -1, CertPathValidatorException.BasicReason.NOT_YET_VALID);
/*     */     }
/*     */ 
/* 193 */     if (debug != null)
/* 194 */       debug.println(str + " verified.");
/*     */   }
/*     */ 
/*     */   private void verifyNameChaining(X509Certificate paramX509Certificate, X500Principal paramX500Principal)
/*     */     throws CertPathValidatorException
/*     */   {
/* 203 */     if (paramX500Principal != null)
/*     */     {
/* 205 */       String str = "subject/issuer name chaining";
/* 206 */       if (debug != null) {
/* 207 */         debug.println("---checking " + str + "...");
/*     */       }
/* 209 */       X500Principal localX500Principal = paramX509Certificate.getIssuerX500Principal();
/*     */ 
/* 212 */       if (X500Name.asX500Name(localX500Principal).isEmpty()) {
/* 213 */         throw new CertPathValidatorException(str + " check failed: " + "empty/null issuer DN in certificate is invalid", null, null, -1, PKIXReason.NAME_CHAINING);
/*     */       }
/*     */ 
/* 219 */       if (!localX500Principal.equals(paramX500Principal)) {
/* 220 */         throw new CertPathValidatorException(str + " check failed", null, null, -1, PKIXReason.NAME_CHAINING);
/*     */       }
/*     */ 
/* 225 */       if (debug != null)
/* 226 */         debug.println(str + " verified.");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateState(X509Certificate paramX509Certificate)
/*     */     throws CertPathValidatorException
/*     */   {
/* 236 */     PublicKey localPublicKey = paramX509Certificate.getPublicKey();
/* 237 */     if (debug != null) {
/* 238 */       debug.println("BasicChecker.updateState issuer: " + paramX509Certificate.getIssuerX500Principal().toString() + "; subject: " + paramX509Certificate.getSubjectX500Principal() + "; serial#: " + paramX509Certificate.getSerialNumber().toString());
/*     */     }
/*     */ 
/* 243 */     if (((localPublicKey instanceof DSAPublicKey)) && (((DSAPublicKey)localPublicKey).getParams() == null))
/*     */     {
/* 246 */       localPublicKey = makeInheritedParamsKey(localPublicKey, this.prevPubKey);
/* 247 */       if (debug != null) debug.println("BasicChecker.updateState Made key with inherited params");
/*     */     }
/*     */ 
/* 250 */     this.prevPubKey = localPublicKey;
/* 251 */     this.prevSubject = paramX509Certificate.getSubjectX500Principal();
/*     */   }
/*     */ 
/*     */   static PublicKey makeInheritedParamsKey(PublicKey paramPublicKey1, PublicKey paramPublicKey2)
/*     */     throws CertPathValidatorException
/*     */   {
/* 267 */     if ((!(paramPublicKey1 instanceof DSAPublicKey)) || (!(paramPublicKey2 instanceof DSAPublicKey)))
/*     */     {
/* 269 */       throw new CertPathValidatorException("Input key is not appropriate type for inheriting parameters");
/*     */     }
/*     */ 
/* 272 */     DSAParams localDSAParams = ((DSAPublicKey)paramPublicKey2).getParams();
/* 273 */     if (localDSAParams == null)
/* 274 */       throw new CertPathValidatorException("Key parameters missing"); PublicKey localPublicKey;
/*     */     try {
/* 276 */       BigInteger localBigInteger = ((DSAPublicKey)paramPublicKey1).getY();
/* 277 */       KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
/* 278 */       DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(localBigInteger, localDSAParams.getP(), localDSAParams.getQ(), localDSAParams.getG());
/*     */ 
/* 282 */       localPublicKey = localKeyFactory.generatePublic(localDSAPublicKeySpec);
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 284 */       throw new CertPathValidatorException("Unable to generate key with inherited parameters: " + localGeneralSecurityException.getMessage(), localGeneralSecurityException);
/*     */     }
/*     */ 
/* 288 */     return localPublicKey;
/*     */   }
/*     */ 
/*     */   PublicKey getPublicKey()
/*     */   {
/* 297 */     return this.prevPubKey;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.BasicChecker
 * JD-Core Version:    0.6.2
 */