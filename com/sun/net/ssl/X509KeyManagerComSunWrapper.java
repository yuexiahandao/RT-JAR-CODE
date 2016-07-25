/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ final class X509KeyManagerComSunWrapper
/*     */   implements X509KeyManager
/*     */ {
/*     */   private javax.net.ssl.X509KeyManager theX509KeyManager;
/*     */ 
/*     */   X509KeyManagerComSunWrapper(javax.net.ssl.X509KeyManager paramX509KeyManager)
/*     */   {
/* 631 */     this.theX509KeyManager = paramX509KeyManager;
/*     */   }
/*     */ 
/*     */   public String[] getClientAliases(String paramString, Principal[] paramArrayOfPrincipal) {
/* 635 */     return this.theX509KeyManager.getClientAliases(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public String chooseClientAlias(String paramString, Principal[] paramArrayOfPrincipal) {
/* 639 */     String[] arrayOfString = { paramString };
/* 640 */     return this.theX509KeyManager.chooseClientAlias(arrayOfString, paramArrayOfPrincipal, null);
/*     */   }
/*     */ 
/*     */   public String[] getServerAliases(String paramString, Principal[] paramArrayOfPrincipal) {
/* 644 */     return this.theX509KeyManager.getServerAliases(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public String chooseServerAlias(String paramString, Principal[] paramArrayOfPrincipal) {
/* 648 */     return this.theX509KeyManager.chooseServerAlias(paramString, paramArrayOfPrincipal, null);
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getCertificateChain(String paramString)
/*     */   {
/* 653 */     return this.theX509KeyManager.getCertificateChain(paramString);
/*     */   }
/*     */ 
/*     */   public PrivateKey getPrivateKey(String paramString) {
/* 657 */     return this.theX509KeyManager.getPrivateKey(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.X509KeyManagerComSunWrapper
 * JD-Core Version:    0.6.2
 */