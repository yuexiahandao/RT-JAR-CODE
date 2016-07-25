/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ 
/*     */ public final class UserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 892106070870210969L;
/*     */   private final String name;
/*     */ 
/*     */   public UserPrincipal(String paramString)
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       throw new NullPointerException("null name is illegal");
/*     */     }
/*  67 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  77 */     if (this == paramObject) {
/*  78 */       return true;
/*     */     }
/*  80 */     if ((paramObject instanceof UserPrincipal)) {
/*  81 */       return this.name.equals(((UserPrincipal)paramObject).getName());
/*     */     }
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  92 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 101 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 110 */     return this.name;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.UserPrincipal
 * JD-Core Version:    0.6.2
 */