/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class UnixNumericUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4329764253802397821L;
/*     */   private String name;
/*     */ 
/*     */   public UnixNumericUserPrincipal(String paramString)
/*     */   {
/*  67 */     if (paramString == null) {
/*  68 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  72 */       Object[] arrayOfObject = { "name" };
/*  73 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*     */ 
/*  76 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public UnixNumericUserPrincipal(long paramLong)
/*     */   {
/*  89 */     this.name = new Long(paramLong).toString();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 102 */     return this.name;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 115 */     return new Long(this.name).longValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("UnixNumericUserPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/* 132 */     Object[] arrayOfObject = { this.name };
/* 133 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 153 */     if (paramObject == null) {
/* 154 */       return false;
/*     */     }
/* 156 */     if (this == paramObject) {
/* 157 */       return true;
/*     */     }
/* 159 */     if (!(paramObject instanceof UnixNumericUserPrincipal))
/* 160 */       return false;
/* 161 */     UnixNumericUserPrincipal localUnixNumericUserPrincipal = (UnixNumericUserPrincipal)paramObject;
/*     */ 
/* 163 */     if (getName().equals(localUnixNumericUserPrincipal.getName()))
/* 164 */       return true;
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 176 */     return this.name.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.UnixNumericUserPrincipal
 * JD-Core Version:    0.6.2
 */