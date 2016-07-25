/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.ldap.LdapName;
/*     */ 
/*     */ public final class LdapPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6820120005580754861L;
/*     */   private final String nameString;
/*     */   private final LdapName name;
/*     */ 
/*     */   public LdapPrincipal(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/*  75 */     if (paramString == null) {
/*  76 */       throw new NullPointerException("null name is illegal");
/*     */     }
/*  78 */     this.name = getLdapName(paramString);
/*  79 */     this.nameString = paramString;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  89 */     if (this == paramObject) {
/*  90 */       return true;
/*     */     }
/*  92 */     if ((paramObject instanceof LdapPrincipal)) {
/*     */       try
/*     */       {
/*  95 */         return this.name.equals(getLdapName(((LdapPrincipal)paramObject).getName()));
/*     */       }
/*     */       catch (InvalidNameException localInvalidNameException)
/*     */       {
/*  99 */         return false;
/*     */       }
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 111 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 120 */     return this.nameString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return this.name.toString();
/*     */   }
/*     */ 
/*     */   private LdapName getLdapName(String paramString) throws InvalidNameException
/*     */   {
/* 136 */     return new LdapName(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.LdapPrincipal
 * JD-Core Version:    0.6.2
 */