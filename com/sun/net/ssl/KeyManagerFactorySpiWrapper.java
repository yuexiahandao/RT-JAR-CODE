/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ 
/*     */ final class KeyManagerFactorySpiWrapper extends KeyManagerFactorySpi
/*     */ {
/*     */   private KeyManagerFactory theKeyManagerFactory;
/*     */ 
/*     */   KeyManagerFactorySpiWrapper(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 448 */     this.theKeyManagerFactory = KeyManagerFactory.getInstance(paramString, paramProvider);
/*     */   }
/*     */ 
/*     */   protected void engineInit(KeyStore paramKeyStore, char[] paramArrayOfChar)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 455 */     this.theKeyManagerFactory.init(paramKeyStore, paramArrayOfChar);
/*     */   }
/*     */ 
/*     */   protected KeyManager[] engineGetKeyManagers()
/*     */   {
/* 463 */     javax.net.ssl.KeyManager[] arrayOfKeyManager = this.theKeyManagerFactory.getKeyManagers();
/*     */ 
/* 466 */     KeyManager[] arrayOfKeyManager1 = new KeyManager[arrayOfKeyManager.length];
/*     */ 
/* 468 */     int j = 0; for (int i = 0; j < arrayOfKeyManager.length; ) {
/* 469 */       if (!(arrayOfKeyManager[j] instanceof KeyManager))
/*     */       {
/* 472 */         if ((arrayOfKeyManager[j] instanceof X509KeyManager)) {
/* 473 */           arrayOfKeyManager1[i] = new X509KeyManagerComSunWrapper((X509KeyManager)arrayOfKeyManager[j]);
/*     */ 
/* 476 */           i++;
/*     */         }
/*     */       } else {
/* 479 */         arrayOfKeyManager1[i] = ((KeyManager)arrayOfKeyManager[j]);
/* 480 */         i++;
/*     */       }
/* 482 */       j++;
/*     */     }
/*     */ 
/* 485 */     if (i != j) {
/* 486 */       arrayOfKeyManager1 = (KeyManager[])SSLSecurity.truncateArray(arrayOfKeyManager1, new KeyManager[i]);
/*     */     }
/*     */ 
/* 490 */     return arrayOfKeyManager1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.KeyManagerFactorySpiWrapper
 * JD-Core Version:    0.6.2
 */