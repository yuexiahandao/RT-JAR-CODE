/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTDomainPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4408637351440771220L;
/*     */   private String name;
/*     */ 
/*     */   public NTDomainPrincipal(String paramString)
/*     */   {
/*  68 */     if (paramString == null) {
/*  69 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  73 */       Object[] arrayOfObject = { "name" };
/*  74 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*  76 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  89 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTDomainPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/* 104 */     Object[] arrayOfObject = { this.name };
/* 105 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 123 */     if (paramObject == null) {
/* 124 */       return false;
/*     */     }
/* 126 */     if (this == paramObject) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (!(paramObject instanceof NTDomainPrincipal))
/* 130 */       return false;
/* 131 */     NTDomainPrincipal localNTDomainPrincipal = (NTDomainPrincipal)paramObject;
/*     */ 
/* 133 */     if (this.name.equals(localNTDomainPrincipal.getName()))
/* 134 */       return true;
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 146 */     return getName().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTDomainPrincipal
 * JD-Core Version:    0.6.2
 */