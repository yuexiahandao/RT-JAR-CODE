/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class UnixPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2951667807323493631L;
/*     */   private String name;
/*     */ 
/*     */   public UnixPrincipal(String paramString)
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  69 */       Object[] arrayOfObject = { "name" };
/*  70 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*     */ 
/*  73 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  84 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("UnixPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  99 */     Object[] arrayOfObject = { this.name };
/* 100 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 118 */     if (paramObject == null) {
/* 119 */       return false;
/*     */     }
/* 121 */     if (this == paramObject) {
/* 122 */       return true;
/*     */     }
/* 124 */     if (!(paramObject instanceof UnixPrincipal))
/* 125 */       return false;
/* 126 */     UnixPrincipal localUnixPrincipal = (UnixPrincipal)paramObject;
/*     */ 
/* 128 */     if (getName().equals(localUnixPrincipal.getName()))
/* 129 */       return true;
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 141 */     return this.name.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.UnixPrincipal
 * JD-Core Version:    0.6.2
 */