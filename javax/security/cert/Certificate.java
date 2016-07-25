/*     */ package javax.security.cert;
/*     */ 
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ 
/*     */ public abstract class Certificate
/*     */ {
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  77 */     if (this == paramObject)
/*  78 */       return true;
/*  79 */     if (!(paramObject instanceof Certificate))
/*  80 */       return false;
/*     */     try {
/*  82 */       byte[] arrayOfByte1 = getEncoded();
/*  83 */       byte[] arrayOfByte2 = ((Certificate)paramObject).getEncoded();
/*     */ 
/*  85 */       if (arrayOfByte1.length != arrayOfByte2.length)
/*  86 */         return false;
/*  87 */       for (int i = 0; i < arrayOfByte1.length; i++)
/*  88 */         if (arrayOfByte1[i] != arrayOfByte2[i])
/*  89 */           return false;
/*  90 */       return true; } catch (CertificateException localCertificateException) {
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 103 */     int i = 0;
/*     */     try {
/* 105 */       byte[] arrayOfByte = getEncoded();
/* 106 */       for (int j = 1; j < arrayOfByte.length; j++) {
/* 107 */         i += arrayOfByte[j] * j;
/*     */       }
/* 109 */       return i; } catch (CertificateException localCertificateException) {
/*     */     }
/* 111 */     return i;
/*     */   }
/*     */ 
/*     */   public abstract byte[] getEncoded()
/*     */     throws CertificateEncodingException;
/*     */ 
/*     */   public abstract void verify(PublicKey paramPublicKey)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
/*     */ 
/*     */   public abstract void verify(PublicKey paramPublicKey, String paramString)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException;
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract PublicKey getPublicKey();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.cert.Certificate
 * JD-Core Version:    0.6.2
 */