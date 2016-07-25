/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class UnixNumericGroupPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3941535899328403223L;
/*     */   private String name;
/*     */   private boolean primaryGroup;
/*     */ 
/*     */   public UnixNumericGroupPrincipal(String paramString, boolean paramBoolean)
/*     */   {
/*  77 */     if (paramString == null) {
/*  78 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("invalid.null.input.value", "sun.security.util.AuthResources"));
/*     */ 
/*  82 */       Object[] arrayOfObject = { "name" };
/*  83 */       throw new NullPointerException(localMessageFormat.format(arrayOfObject));
/*     */     }
/*     */ 
/*  86 */     this.name = paramString;
/*  87 */     this.primaryGroup = paramBoolean;
/*     */   }
/*     */ 
/*     */   public UnixNumericGroupPrincipal(long paramLong, boolean paramBoolean)
/*     */   {
/* 104 */     this.name = new Long(paramLong).toString();
/* 105 */     this.primaryGroup = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 118 */     return this.name;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 131 */     return new Long(this.name).longValue();
/*     */   }
/*     */ 
/*     */   public boolean isPrimaryGroup()
/*     */   {
/* 145 */     return this.primaryGroup;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     if (this.primaryGroup) {
/* 160 */       localMessageFormat = new MessageFormat(ResourcesMgr.getString("UnixNumericGroupPrincipal.Primary.Group.name", "sun.security.util.AuthResources"));
/*     */ 
/* 164 */       arrayOfObject = new Object[] { this.name };
/* 165 */       return localMessageFormat.format(arrayOfObject);
/*     */     }
/* 167 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("UnixNumericGroupPrincipal.Supplementary.Group.name", "sun.security.util.AuthResources"));
/*     */ 
/* 171 */     Object[] arrayOfObject = { this.name };
/* 172 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 193 */     if (paramObject == null) {
/* 194 */       return false;
/*     */     }
/* 196 */     if (this == paramObject) {
/* 197 */       return true;
/*     */     }
/* 199 */     if (!(paramObject instanceof UnixNumericGroupPrincipal))
/* 200 */       return false;
/* 201 */     UnixNumericGroupPrincipal localUnixNumericGroupPrincipal = (UnixNumericGroupPrincipal)paramObject;
/*     */ 
/* 203 */     if ((getName().equals(localUnixNumericGroupPrincipal.getName())) && (isPrimaryGroup() == localUnixNumericGroupPrincipal.isPrimaryGroup()))
/*     */     {
/* 205 */       return true;
/* 206 */     }return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 217 */     return toString().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.UnixNumericGroupPrincipal
 * JD-Core Version:    0.6.2
 */