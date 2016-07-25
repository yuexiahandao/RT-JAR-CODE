/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ import java.security.Principal;
/*     */ 
/*     */ public class HttpPrincipal
/*     */   implements Principal
/*     */ {
/*     */   private String username;
/*     */   private String realm;
/*     */ 
/*     */   public HttpPrincipal(String paramString1, String paramString2)
/*     */   {
/*  46 */     if ((paramString1 == null) || (paramString2 == null)) {
/*  47 */       throw new NullPointerException();
/*     */     }
/*  49 */     this.username = paramString1;
/*  50 */     this.realm = paramString2;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  60 */     if (!(paramObject instanceof HttpPrincipal)) {
/*  61 */       return false;
/*     */     }
/*  63 */     HttpPrincipal localHttpPrincipal = (HttpPrincipal)paramObject;
/*  64 */     return (this.username.equals(localHttpPrincipal.username)) && (this.realm.equals(localHttpPrincipal.realm));
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  73 */     return this.username;
/*     */   }
/*     */ 
/*     */   public String getUsername()
/*     */   {
/*  80 */     return this.username;
/*     */   }
/*     */ 
/*     */   public String getRealm()
/*     */   {
/*  87 */     return this.realm;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  95 */     return (this.username + this.realm).hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 102 */     return getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpPrincipal
 * JD-Core Version:    0.6.2
 */