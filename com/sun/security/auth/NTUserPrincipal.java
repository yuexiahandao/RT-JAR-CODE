/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8737649811939033735L;
/*     */   private String name;
/*     */ 
/*     */   public NTUserPrincipal(String paramString)
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  69 */       Object[] arrayOfObject = { "name" };
/*  70 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*  72 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  83 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  94 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTUserPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  98 */     Object[] arrayOfObject = { this.name };
/*  99 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 117 */     if (paramObject == null) {
/* 118 */       return false;
/*     */     }
/* 120 */     if (this == paramObject) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (!(paramObject instanceof NTUserPrincipal))
/* 124 */       return false;
/* 125 */     NTUserPrincipal localNTUserPrincipal = (NTUserPrincipal)paramObject;
/*     */ 
/* 127 */     if (this.name.equals(localNTUserPrincipal.getName()))
/* 128 */       return true;
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 140 */     return getName().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTUserPrincipal
 * JD-Core Version:    0.6.2
 */