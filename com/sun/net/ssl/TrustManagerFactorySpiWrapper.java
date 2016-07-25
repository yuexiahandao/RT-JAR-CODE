/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ final class TrustManagerFactorySpiWrapper extends TrustManagerFactorySpi
/*     */ {
/*     */   private TrustManagerFactory theTrustManagerFactory;
/*     */ 
/*     */   TrustManagerFactorySpiWrapper(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 397 */     this.theTrustManagerFactory = TrustManagerFactory.getInstance(paramString, paramProvider);
/*     */   }
/*     */ 
/*     */   protected void engineInit(KeyStore paramKeyStore) throws KeyStoreException
/*     */   {
/* 402 */     this.theTrustManagerFactory.init(paramKeyStore);
/*     */   }
/*     */ 
/*     */   protected TrustManager[] engineGetTrustManagers()
/*     */   {
/* 410 */     javax.net.ssl.TrustManager[] arrayOfTrustManager = this.theTrustManagerFactory.getTrustManagers();
/*     */ 
/* 413 */     TrustManager[] arrayOfTrustManager1 = new TrustManager[arrayOfTrustManager.length];
/*     */ 
/* 415 */     int j = 0; for (int i = 0; j < arrayOfTrustManager.length; ) {
/* 416 */       if (!(arrayOfTrustManager[j] instanceof TrustManager))
/*     */       {
/* 419 */         if ((arrayOfTrustManager[j] instanceof X509TrustManager)) {
/* 420 */           arrayOfTrustManager1[i] = new X509TrustManagerComSunWrapper((X509TrustManager)arrayOfTrustManager[j]);
/*     */ 
/* 423 */           i++;
/*     */         }
/*     */       } else {
/* 426 */         arrayOfTrustManager1[i] = ((TrustManager)arrayOfTrustManager[j]);
/* 427 */         i++;
/*     */       }
/* 429 */       j++;
/*     */     }
/*     */ 
/* 432 */     if (i != j) {
/* 433 */       arrayOfTrustManager1 = (TrustManager[])SSLSecurity.truncateArray(arrayOfTrustManager1, new TrustManager[i]);
/*     */     }
/*     */ 
/* 437 */     return arrayOfTrustManager1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.TrustManagerFactorySpiWrapper
 * JD-Core Version:    0.6.2
 */