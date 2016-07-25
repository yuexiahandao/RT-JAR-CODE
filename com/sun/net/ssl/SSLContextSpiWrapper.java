/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.SecureRandom;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ final class SSLContextSpiWrapper extends SSLContextSpi
/*     */ {
/*     */   private SSLContext theSSLContext;
/*     */ 
/*     */   SSLContextSpiWrapper(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 286 */     this.theSSLContext = SSLContext.getInstance(paramString, paramProvider);
/*     */   }
/*     */ 
/*     */   protected void engineInit(KeyManager[] paramArrayOfKeyManager, TrustManager[] paramArrayOfTrustManager, SecureRandom paramSecureRandom)
/*     */     throws KeyManagementException
/*     */   {
/*     */     javax.net.ssl.KeyManager[] arrayOfKeyManager;
/*     */     int j;
/*     */     int i;
/* 300 */     if (paramArrayOfKeyManager != null) {
/* 301 */       arrayOfKeyManager = new javax.net.ssl.KeyManager[paramArrayOfKeyManager.length];
/* 302 */       j = 0; for (i = 0; j < paramArrayOfKeyManager.length; )
/*     */       {
/* 308 */         if (!(paramArrayOfKeyManager[j] instanceof javax.net.ssl.KeyManager))
/*     */         {
/* 316 */           if ((paramArrayOfKeyManager[j] instanceof X509KeyManager)) {
/* 317 */             arrayOfKeyManager[i] = new X509KeyManagerJavaxWrapper((X509KeyManager)paramArrayOfKeyManager[j]);
/*     */ 
/* 320 */             i++;
/*     */           }
/*     */         }
/*     */         else {
/* 324 */           arrayOfKeyManager[i] = ((javax.net.ssl.KeyManager)paramArrayOfKeyManager[j]);
/* 325 */           i++;
/*     */         }
/* 327 */         j++;
/*     */       }
/*     */ 
/* 335 */       if (i != j) {
/* 336 */         arrayOfKeyManager = (javax.net.ssl.KeyManager[])SSLSecurity.truncateArray(arrayOfKeyManager, new javax.net.ssl.KeyManager[i]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 341 */       arrayOfKeyManager = null;
/*     */     }
/*     */     javax.net.ssl.TrustManager[] arrayOfTrustManager;
/* 345 */     if (paramArrayOfTrustManager != null) {
/* 346 */       arrayOfTrustManager = new javax.net.ssl.TrustManager[paramArrayOfTrustManager.length];
/*     */ 
/* 348 */       j = 0; for (i = 0; j < paramArrayOfTrustManager.length; )
/*     */       {
/* 352 */         if (!(paramArrayOfTrustManager[j] instanceof javax.net.ssl.TrustManager))
/*     */         {
/* 354 */           if ((paramArrayOfTrustManager[j] instanceof X509TrustManager)) {
/* 355 */             arrayOfTrustManager[i] = new X509TrustManagerJavaxWrapper((X509TrustManager)paramArrayOfTrustManager[j]);
/*     */ 
/* 358 */             i++;
/*     */           }
/*     */         } else {
/* 361 */           arrayOfTrustManager[i] = ((javax.net.ssl.TrustManager)paramArrayOfTrustManager[j]);
/* 362 */           i++;
/*     */         }
/* 364 */         j++;
/*     */       }
/*     */ 
/* 367 */       if (i != j) {
/* 368 */         arrayOfTrustManager = (javax.net.ssl.TrustManager[])SSLSecurity.truncateArray(arrayOfTrustManager, new javax.net.ssl.TrustManager[i]);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 373 */       arrayOfTrustManager = null;
/*     */     }
/*     */ 
/* 376 */     this.theSSLContext.init(arrayOfKeyManager, arrayOfTrustManager, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   protected SSLSocketFactory engineGetSocketFactory()
/*     */   {
/* 381 */     return this.theSSLContext.getSocketFactory();
/*     */   }
/*     */ 
/*     */   protected SSLServerSocketFactory engineGetServerSocketFactory()
/*     */   {
/* 386 */     return this.theSSLContext.getServerSocketFactory();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.SSLContextSpiWrapper
 * JD-Core Version:    0.6.2
 */