/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ @Deprecated
/*     */ public class SolarisNumericUserPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3178578484679887104L;
/*  54 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  58 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private String name;
/*     */ 
/*     */   public SolarisNumericUserPrincipal(String paramString)
/*     */   {
/*  82 */     if (paramString == null) {
/*  83 */       throw new NullPointerException(rb.getString("provided.null.name"));
/*     */     }
/*  85 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public SolarisNumericUserPrincipal(long paramLong)
/*     */   {
/*  98 */     this.name = new Long(paramLong).toString();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 111 */     return this.name;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 124 */     return new Long(this.name).longValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     return rb.getString("SolarisNumericUserPrincipal.") + this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 157 */     if (paramObject == null) {
/* 158 */       return false;
/*     */     }
/* 160 */     if (this == paramObject) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(paramObject instanceof SolarisNumericUserPrincipal))
/* 164 */       return false;
/* 165 */     SolarisNumericUserPrincipal localSolarisNumericUserPrincipal = (SolarisNumericUserPrincipal)paramObject;
/*     */ 
/* 167 */     if (getName().equals(localSolarisNumericUserPrincipal.getName()))
/* 168 */       return true;
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 180 */     return this.name.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.SolarisNumericUserPrincipal
 * JD-Core Version:    0.6.2
 */