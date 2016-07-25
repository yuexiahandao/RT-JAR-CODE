/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.util.Arrays;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public abstract class Certificate
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3585440601605666277L;
/*     */   private final String type;
/*     */ 
/*     */   protected Certificate(String paramString)
/*     */   {
/*  78 */     this.type = paramString;
/*     */   }
/*     */ 
/*     */   public final String getType()
/*     */   {
/*  87 */     return this.type;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 102 */     if (this == paramObject) {
/* 103 */       return true;
/*     */     }
/* 105 */     if (!(paramObject instanceof Certificate))
/* 106 */       return false;
/*     */     try
/*     */     {
/* 109 */       byte[] arrayOfByte1 = X509CertImpl.getEncodedInternal(this);
/* 110 */       byte[] arrayOfByte2 = X509CertImpl.getEncodedInternal((Certificate)paramObject);
/*     */ 
/* 112 */       return Arrays.equals(arrayOfByte1, arrayOfByte2); } catch (CertificateException localCertificateException) {
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 125 */     int i = 0;
/*     */     try {
/* 127 */       byte[] arrayOfByte = X509CertImpl.getEncodedInternal(this);
/* 128 */       for (int j = 1; j < arrayOfByte.length; j++) {
/* 129 */         i += arrayOfByte[j] * j;
/*     */       }
/* 131 */       return i; } catch (CertificateException localCertificateException) {
/*     */     }
/* 133 */     return i;
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
/*     */ 
/*     */   protected Object writeReplace()
/*     */     throws ObjectStreamException
/*     */   {
/*     */     try
/*     */     {
/* 265 */       return new CertificateRep(this.type, getEncoded());
/*     */     } catch (CertificateException localCertificateException) {
/* 267 */       throw new NotSerializableException("java.security.cert.Certificate: " + this.type + ": " + localCertificateException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class CertificateRep
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8563758940495660020L;
/*     */     private String type;
/*     */     private byte[] data;
/*     */ 
/*     */     protected CertificateRep(String paramString, byte[] paramArrayOfByte)
/*     */     {
/* 225 */       this.type = paramString;
/* 226 */       this.data = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     protected Object readResolve()
/*     */       throws ObjectStreamException
/*     */     {
/*     */       try
/*     */       {
/* 241 */         CertificateFactory localCertificateFactory = CertificateFactory.getInstance(this.type);
/* 242 */         return localCertificateFactory.generateCertificate(new ByteArrayInputStream(this.data));
/*     */       }
/*     */       catch (CertificateException localCertificateException) {
/* 245 */         throw new NotSerializableException("java.security.cert.Certificate: " + this.type + ": " + localCertificateException.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.Certificate
 * JD-Core Version:    0.6.2
 */