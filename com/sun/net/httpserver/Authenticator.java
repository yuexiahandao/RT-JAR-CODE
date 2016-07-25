/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ public abstract class Authenticator
/*     */ {
/*     */   public abstract Result authenticate(HttpExchange paramHttpExchange);
/*     */ 
/*     */   public static class Failure extends Authenticator.Result
/*     */   {
/*     */     private int responseCode;
/*     */ 
/*     */     public Failure(int paramInt)
/*     */     {
/*  55 */       this.responseCode = paramInt;
/*     */     }
/*     */ 
/*     */     public int getResponseCode()
/*     */     {
/*  62 */       return this.responseCode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class Result
/*     */   {
/*     */   }
/*     */ 
/*     */   public static class Retry extends Authenticator.Result
/*     */   {
/*     */     private int responseCode;
/*     */ 
/*     */     public Retry(int paramInt)
/*     */     {
/*  97 */       this.responseCode = paramInt;
/*     */     }
/*     */ 
/*     */     public int getResponseCode()
/*     */     {
/* 104 */       return this.responseCode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Success extends Authenticator.Result
/*     */   {
/*     */     private HttpPrincipal principal;
/*     */ 
/*     */     public Success(HttpPrincipal paramHttpPrincipal)
/*     */     {
/*  75 */       this.principal = paramHttpPrincipal;
/*     */     }
/*     */ 
/*     */     public HttpPrincipal getPrincipal()
/*     */     {
/*  81 */       return this.principal;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.Authenticator
 * JD-Core Version:    0.6.2
 */