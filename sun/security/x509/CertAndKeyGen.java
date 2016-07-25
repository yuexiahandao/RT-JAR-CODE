/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import java.util.Random;
/*     */ import sun.security.pkcs.PKCS10;
/*     */ 
/*     */ public final class CertAndKeyGen
/*     */ {
/*     */   private SecureRandom prng;
/*     */   private String sigAlg;
/*     */   private KeyPairGenerator keyGen;
/*     */   private PublicKey publicKey;
/*     */   private PrivateKey privateKey;
/*     */ 
/*     */   public CertAndKeyGen(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  75 */     this.keyGen = KeyPairGenerator.getInstance(paramString1);
/*  76 */     this.sigAlg = paramString2;
/*     */   }
/*     */ 
/*     */   public CertAndKeyGen(String paramString1, String paramString2, String paramString3)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/*  93 */     if (paramString3 == null)
/*  94 */       this.keyGen = KeyPairGenerator.getInstance(paramString1);
/*     */     else {
/*     */       try {
/*  97 */         this.keyGen = KeyPairGenerator.getInstance(paramString1, paramString3);
/*     */       }
/*     */       catch (Exception localException) {
/* 100 */         this.keyGen = KeyPairGenerator.getInstance(paramString1);
/*     */       }
/*     */     }
/* 103 */     this.sigAlg = paramString2;
/*     */   }
/*     */ 
/*     */   public void setRandom(SecureRandom paramSecureRandom)
/*     */   {
/* 116 */     this.prng = paramSecureRandom;
/*     */   }
/*     */ 
/*     */   public void generate(int paramInt)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     KeyPair localKeyPair;
/*     */     try
/*     */     {
/* 144 */       if (this.prng == null) {
/* 145 */         this.prng = new SecureRandom();
/*     */       }
/* 147 */       this.keyGen.initialize(paramInt, this.prng);
/* 148 */       localKeyPair = this.keyGen.generateKeyPair();
/*     */     }
/*     */     catch (Exception localException) {
/* 151 */       throw new IllegalArgumentException(localException.getMessage());
/*     */     }
/*     */ 
/* 154 */     this.publicKey = localKeyPair.getPublic();
/* 155 */     this.privateKey = localKeyPair.getPrivate();
/*     */ 
/* 160 */     if ((!"X.509".equalsIgnoreCase(this.publicKey.getFormat())) && (!"X509".equalsIgnoreCase(this.publicKey.getFormat())))
/*     */     {
/* 162 */       throw new IllegalArgumentException("Public key format is " + this.publicKey.getFormat() + ", must be X.509");
/*     */     }
/*     */   }
/*     */ 
/*     */   public X509Key getPublicKey()
/*     */   {
/* 180 */     if (!(this.publicKey instanceof X509Key)) {
/* 181 */       return null;
/*     */     }
/* 183 */     return (X509Key)this.publicKey;
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKeyAnyway()
/*     */   {
/* 194 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */   public PrivateKey getPrivateKey()
/*     */   {
/* 207 */     return this.privateKey;
/*     */   }
/*     */ 
/*     */   public X509Certificate getSelfCertificate(X500Name paramX500Name, Date paramDate, long paramLong)
/*     */     throws CertificateException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 234 */     return getSelfCertificate(paramX500Name, paramDate, paramLong, null);
/*     */   }
/*     */ 
/*     */   public X509Certificate getSelfCertificate(X500Name paramX500Name, Date paramDate, long paramLong, CertificateExtensions paramCertificateExtensions)
/*     */     throws CertificateException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/*     */     try
/*     */     {
/* 247 */       Date localDate = new Date();
/* 248 */       localDate.setTime(paramDate.getTime() + paramLong * 1000L);
/*     */ 
/* 250 */       CertificateValidity localCertificateValidity = new CertificateValidity(paramDate, localDate);
/*     */ 
/* 253 */       X509CertInfo localX509CertInfo = new X509CertInfo();
/*     */ 
/* 255 */       localX509CertInfo.set("version", new CertificateVersion(2));
/*     */ 
/* 257 */       localX509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & 0x7FFFFFFF));
/*     */ 
/* 259 */       AlgorithmId localAlgorithmId = AlgorithmId.getAlgorithmId(this.sigAlg);
/* 260 */       localX509CertInfo.set("algorithmID", new CertificateAlgorithmId(localAlgorithmId));
/*     */ 
/* 262 */       localX509CertInfo.set("subject", new CertificateSubjectName(paramX500Name));
/* 263 */       localX509CertInfo.set("key", new CertificateX509Key(this.publicKey));
/* 264 */       localX509CertInfo.set("validity", localCertificateValidity);
/* 265 */       localX509CertInfo.set("issuer", new CertificateIssuerName(paramX500Name));
/* 266 */       if (paramCertificateExtensions != null) localX509CertInfo.set("extensions", paramCertificateExtensions);
/*     */ 
/* 268 */       X509CertImpl localX509CertImpl = new X509CertImpl(localX509CertInfo);
/* 269 */       localX509CertImpl.sign(this.privateKey, this.sigAlg);
/*     */ 
/* 271 */       return localX509CertImpl;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 274 */       throw new CertificateEncodingException("getSelfCert: " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public X509Certificate getSelfCertificate(X500Name paramX500Name, long paramLong)
/*     */     throws CertificateException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 284 */     return getSelfCertificate(paramX500Name, new Date(), paramLong);
/*     */   }
/*     */ 
/*     */   public PKCS10 getCertRequest(X500Name paramX500Name)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/* 304 */     PKCS10 localPKCS10 = new PKCS10(this.publicKey);
/*     */     try
/*     */     {
/* 307 */       Signature localSignature = Signature.getInstance(this.sigAlg);
/* 308 */       localSignature.initSign(this.privateKey);
/* 309 */       localPKCS10.encodeAndSign(paramX500Name, localSignature);
/*     */     }
/*     */     catch (CertificateException localCertificateException) {
/* 312 */       throw new SignatureException(this.sigAlg + " CertificateException");
/*     */     }
/*     */     catch (IOException localIOException) {
/* 315 */       throw new SignatureException(this.sigAlg + " IOException");
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 319 */       throw new SignatureException(this.sigAlg + " unavailable?");
/*     */     }
/* 321 */     return localPKCS10;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CertAndKeyGen
 * JD-Core Version:    0.6.2
 */