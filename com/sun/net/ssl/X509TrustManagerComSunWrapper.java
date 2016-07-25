/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ final class X509TrustManagerComSunWrapper
/*     */   implements X509TrustManager
/*     */ {
/*     */   private javax.net.ssl.X509TrustManager theX509TrustManager;
/*     */ 
/*     */   X509TrustManagerComSunWrapper(javax.net.ssl.X509TrustManager paramX509TrustManager)
/*     */   {
/* 666 */     this.theX509TrustManager = paramX509TrustManager;
/*     */   }
/*     */ 
/*     */   public boolean isClientTrusted(X509Certificate[] paramArrayOfX509Certificate)
/*     */   {
/*     */     try {
/* 672 */       this.theX509TrustManager.checkClientTrusted(paramArrayOfX509Certificate, "UNKNOWN");
/* 673 */       return true; } catch (CertificateException localCertificateException) {
/*     */     }
/* 675 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isServerTrusted(X509Certificate[] paramArrayOfX509Certificate)
/*     */   {
/*     */     try
/*     */     {
/* 682 */       this.theX509TrustManager.checkServerTrusted(paramArrayOfX509Certificate, "UNKNOWN");
/* 683 */       return true; } catch (CertificateException localCertificateException) {
/*     */     }
/* 685 */     return false;
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getAcceptedIssuers()
/*     */   {
/* 690 */     return this.theX509TrustManager.getAcceptedIssuers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.X509TrustManagerComSunWrapper
 * JD-Core Version:    0.6.2
 */