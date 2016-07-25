/*     */ package java.security.cert;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.x509.NameConstraintsExtension;
/*     */ 
/*     */ public class TrustAnchor
/*     */ {
/*     */   private final PublicKey pubKey;
/*     */   private final String caName;
/*     */   private final X500Principal caPrincipal;
/*     */   private final X509Certificate trustedCert;
/*     */   private byte[] ncBytes;
/*     */   private NameConstraintsExtension nc;
/*     */ 
/*     */   public TrustAnchor(X509Certificate paramX509Certificate, byte[] paramArrayOfByte)
/*     */   {
/* 126 */     if (paramX509Certificate == null) {
/* 127 */       throw new NullPointerException("the trustedCert parameter must be non-null");
/*     */     }
/* 129 */     this.trustedCert = paramX509Certificate;
/* 130 */     this.pubKey = null;
/* 131 */     this.caName = null;
/* 132 */     this.caPrincipal = null;
/* 133 */     setNameConstraints(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public TrustAnchor(X500Principal paramX500Principal, PublicKey paramPublicKey, byte[] paramArrayOfByte)
/*     */   {
/* 165 */     if ((paramX500Principal == null) || (paramPublicKey == null)) {
/* 166 */       throw new NullPointerException();
/*     */     }
/* 168 */     this.trustedCert = null;
/* 169 */     this.caPrincipal = paramX500Principal;
/* 170 */     this.caName = paramX500Principal.getName();
/* 171 */     this.pubKey = paramPublicKey;
/* 172 */     setNameConstraints(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public TrustAnchor(String paramString, PublicKey paramPublicKey, byte[] paramArrayOfByte)
/*     */   {
/* 208 */     if (paramPublicKey == null) {
/* 209 */       throw new NullPointerException("the pubKey parameter must be non-null");
/*     */     }
/* 211 */     if (paramString == null) {
/* 212 */       throw new NullPointerException("the caName parameter must be non-null");
/*     */     }
/* 214 */     if (paramString.length() == 0) {
/* 215 */       throw new IllegalArgumentException("the caName parameter must be a non-empty String");
/*     */     }
/*     */ 
/* 218 */     this.caPrincipal = new X500Principal(paramString);
/* 219 */     this.pubKey = paramPublicKey;
/* 220 */     this.caName = paramString;
/* 221 */     this.trustedCert = null;
/* 222 */     setNameConstraints(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final X509Certificate getTrustedCert()
/*     */   {
/* 232 */     return this.trustedCert;
/*     */   }
/*     */ 
/*     */   public final X500Principal getCA()
/*     */   {
/* 244 */     return this.caPrincipal;
/*     */   }
/*     */ 
/*     */   public final String getCAName()
/*     */   {
/* 256 */     return this.caName;
/*     */   }
/*     */ 
/*     */   public final PublicKey getCAPublicKey()
/*     */   {
/* 267 */     return this.pubKey;
/*     */   }
/*     */ 
/*     */   private void setNameConstraints(byte[] paramArrayOfByte)
/*     */   {
/* 274 */     if (paramArrayOfByte == null) {
/* 275 */       this.ncBytes = null;
/* 276 */       this.nc = null;
/*     */     } else {
/* 278 */       this.ncBytes = ((byte[])paramArrayOfByte.clone());
/*     */       try
/*     */       {
/* 281 */         this.nc = new NameConstraintsExtension(Boolean.FALSE, paramArrayOfByte);
/*     */       } catch (IOException localIOException) {
/* 283 */         IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(localIOException.getMessage());
/*     */ 
/* 285 */         localIllegalArgumentException.initCause(localIOException);
/* 286 */         throw localIllegalArgumentException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final byte[] getNameConstraints()
/*     */   {
/* 312 */     return this.ncBytes == null ? null : (byte[])this.ncBytes.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 321 */     StringBuffer localStringBuffer = new StringBuffer();
/* 322 */     localStringBuffer.append("[\n");
/* 323 */     if (this.pubKey != null) {
/* 324 */       localStringBuffer.append("  Trusted CA Public Key: " + this.pubKey.toString() + "\n");
/* 325 */       localStringBuffer.append("  Trusted CA Issuer Name: " + String.valueOf(this.caName) + "\n");
/*     */     }
/*     */     else {
/* 328 */       localStringBuffer.append("  Trusted CA cert: " + this.trustedCert.toString() + "\n");
/*     */     }
/* 330 */     if (this.nc != null)
/* 331 */       localStringBuffer.append("  Name Constraints: " + this.nc.toString() + "\n");
/* 332 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.TrustAnchor
 * JD-Core Version:    0.6.2
 */