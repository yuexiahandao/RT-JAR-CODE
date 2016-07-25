/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.net.Socket;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ 
/*     */ final class X509KeyManagerJavaxWrapper
/*     */   implements javax.net.ssl.X509KeyManager
/*     */ {
/*     */   private X509KeyManager theX509KeyManager;
/*     */ 
/*     */   X509KeyManagerJavaxWrapper(X509KeyManager paramX509KeyManager)
/*     */   {
/* 503 */     this.theX509KeyManager = paramX509KeyManager;
/*     */   }
/*     */ 
/*     */   public String[] getClientAliases(String paramString, Principal[] paramArrayOfPrincipal) {
/* 507 */     return this.theX509KeyManager.getClientAliases(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public String chooseClientAlias(String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, Socket paramSocket)
/*     */   {
/* 514 */     if (paramArrayOfString == null) {
/* 515 */       return null;
/*     */     }
/*     */ 
/* 521 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*     */       String str;
/* 522 */       if ((str = this.theX509KeyManager.chooseClientAlias(paramArrayOfString[i], paramArrayOfPrincipal)) != null)
/*     */       {
/* 524 */         return str;
/*     */       }
/*     */     }
/* 526 */     return null;
/*     */   }
/*     */ 
/*     */   public String chooseEngineClientAlias(String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, SSLEngine paramSSLEngine)
/*     */   {
/* 540 */     if (paramArrayOfString == null) {
/* 541 */       return null;
/*     */     }
/*     */ 
/* 547 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*     */       String str;
/* 548 */       if ((str = this.theX509KeyManager.chooseClientAlias(paramArrayOfString[i], paramArrayOfPrincipal)) != null)
/*     */       {
/* 550 */         return str;
/*     */       }
/*     */     }
/* 553 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getServerAliases(String paramString, Principal[] paramArrayOfPrincipal) {
/* 557 */     return this.theX509KeyManager.getServerAliases(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public String chooseServerAlias(String paramString, Principal[] paramArrayOfPrincipal, Socket paramSocket)
/*     */   {
/* 563 */     if (paramString == null) {
/* 564 */       return null;
/*     */     }
/* 566 */     return this.theX509KeyManager.chooseServerAlias(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public String chooseEngineServerAlias(String paramString, Principal[] paramArrayOfPrincipal, SSLEngine paramSSLEngine)
/*     */   {
/* 578 */     if (paramString == null) {
/* 579 */       return null;
/*     */     }
/* 581 */     return this.theX509KeyManager.chooseServerAlias(paramString, paramArrayOfPrincipal);
/*     */   }
/*     */ 
/*     */   public X509Certificate[] getCertificateChain(String paramString)
/*     */   {
/* 586 */     return this.theX509KeyManager.getCertificateChain(paramString);
/*     */   }
/*     */ 
/*     */   public PrivateKey getPrivateKey(String paramString) {
/* 590 */     return this.theX509KeyManager.getPrivateKey(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.X509KeyManagerJavaxWrapper
 * JD-Core Version:    0.6.2
 */