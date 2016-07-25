/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ import javax.net.ssl.SSLContext;
/*     */ 
/*     */ public class HttpsConfigurator
/*     */ {
/*     */   private SSLContext context;
/*     */ 
/*     */   public HttpsConfigurator(SSLContext paramSSLContext)
/*     */   {
/*  80 */     if (paramSSLContext == null) {
/*  81 */       throw new NullPointerException("null SSLContext");
/*     */     }
/*  83 */     this.context = paramSSLContext;
/*     */   }
/*     */ 
/*     */   public SSLContext getSSLContext()
/*     */   {
/*  91 */     return this.context;
/*     */   }
/*     */ 
/*     */   public void configure(HttpsParameters paramHttpsParameters)
/*     */   {
/* 113 */     paramHttpsParameters.setSSLParameters(getSSLContext().getDefaultSSLParameters());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpsConfigurator
 * JD-Core Version:    0.6.2
 */