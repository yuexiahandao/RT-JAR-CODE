/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ final class X509TrustManagerJavaxWrapper
/*     */   implements javax.net.ssl.X509TrustManager
/*     */ {
/*     */   private X509TrustManager theX509TrustManager;
/*     */ 
/*     */   X509TrustManagerJavaxWrapper(X509TrustManager paramX509TrustManager)
/*     */   {
/* 600 */     this.theX509TrustManager = paramX509TrustManager;
/*     */   }
/*     */ 
/*     */   public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 606 */     if (!this.theX509TrustManager.isClientTrusted(paramArrayOfX509Certificate))
/* 607 */       throw new CertificateException("Untrusted Client Certificate Chain");
/*     */   }
/*     */ 
/*     */   public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
/*     */     throws CertificateException
/*     */   {
/* 615 */     if (!this.theX509TrustManager.isServerTrusted(paramArrayOfX509Certificate))
/* 616 */       throw new CertificateException("Untrusted Server Certificate Chain");
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getAcceptedIssuers()
/*     */   {
/* 622 */     return this.theX509TrustManager.getAcceptedIssuers();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.X509TrustManagerJavaxWrapper
 * JD-Core Version:    0.6.2
 */