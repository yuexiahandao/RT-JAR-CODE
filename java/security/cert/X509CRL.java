/*     */ package java.security.cert;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Set;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.x509.X509CRLImpl;
/*     */ 
/*     */ public abstract class X509CRL extends CRL
/*     */   implements X509Extension
/*     */ {
/*     */   private transient X500Principal issuerPrincipal;
/*     */ 
/*     */   protected X509CRL()
/*     */   {
/* 125 */     super("X.509");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 141 */     if (this == paramObject) {
/* 142 */       return true;
/*     */     }
/* 144 */     if (!(paramObject instanceof X509CRL))
/* 145 */       return false;
/*     */     try
/*     */     {
/* 148 */       byte[] arrayOfByte1 = X509CRLImpl.getEncodedInternal(this);
/* 149 */       byte[] arrayOfByte2 = X509CRLImpl.getEncodedInternal((X509CRL)paramObject);
/*     */ 
/* 151 */       return Arrays.equals(arrayOfByte1, arrayOfByte2); } catch (CRLException localCRLException) {
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 164 */     int i = 0;
/*     */     try {
/* 166 */       byte[] arrayOfByte = X509CRLImpl.getEncodedInternal(this);
/* 167 */       for (int j = 1; j < arrayOfByte.length; j++) {
/* 168 */         i += arrayOfByte[j] * j;
/*     */       }
/* 170 */       return i; } catch (CRLException localCRLException) {
/*     */     }
/* 172 */     return i;
/*     */   }
/*     */ 
/*     */   public abstract byte[] getEncoded()
/*     */     throws CRLException;
/*     */ 
/*     */   public abstract void verify(PublicKey paramPublicKey)
/*     */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
/*     */ 
/*     */   public abstract void verify(PublicKey paramPublicKey, String paramString)
/*     */     throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
/*     */ 
/*     */   public abstract int getVersion();
/*     */ 
/*     */   public abstract Principal getIssuerDN();
/*     */ 
/*     */   public X500Principal getIssuerX500Principal()
/*     */   {
/* 291 */     if (this.issuerPrincipal == null) {
/* 292 */       this.issuerPrincipal = X509CRLImpl.getIssuerX500Principal(this);
/*     */     }
/* 294 */     return this.issuerPrincipal;
/*     */   }
/*     */ 
/*     */   public abstract Date getThisUpdate();
/*     */ 
/*     */   public abstract Date getNextUpdate();
/*     */ 
/*     */   public abstract X509CRLEntry getRevokedCertificate(BigInteger paramBigInteger);
/*     */ 
/*     */   public X509CRLEntry getRevokedCertificate(X509Certificate paramX509Certificate)
/*     */   {
/* 349 */     X500Principal localX500Principal1 = paramX509Certificate.getIssuerX500Principal();
/* 350 */     X500Principal localX500Principal2 = getIssuerX500Principal();
/* 351 */     if (!localX500Principal1.equals(localX500Principal2)) {
/* 352 */       return null;
/*     */     }
/* 354 */     return getRevokedCertificate(paramX509Certificate.getSerialNumber());
/*     */   }
/*     */ 
/*     */   public abstract Set<? extends X509CRLEntry> getRevokedCertificates();
/*     */ 
/*     */   public abstract byte[] getTBSCertList()
/*     */     throws CRLException;
/*     */ 
/*     */   public abstract byte[] getSignature();
/*     */ 
/*     */   public abstract String getSigAlgName();
/*     */ 
/*     */   public abstract String getSigAlgOID();
/*     */ 
/*     */   public abstract byte[] getSigAlgParams();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.X509CRL
 * JD-Core Version:    0.6.2
 */