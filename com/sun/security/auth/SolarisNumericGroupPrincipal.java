/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ @Deprecated
/*     */ public class SolarisNumericGroupPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2345199581042573224L;
/*  55 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  59 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private String name;
/*     */   private boolean primaryGroup;
/*     */ 
/*     */   public SolarisNumericGroupPrincipal(String paramString, boolean paramBoolean)
/*     */   {
/*  91 */     if (paramString == null) {
/*  92 */       throw new NullPointerException(rb.getString("provided.null.name"));
/*     */     }
/*  94 */     this.name = paramString;
/*  95 */     this.primaryGroup = paramBoolean;
/*     */   }
/*     */ 
/*     */   public SolarisNumericGroupPrincipal(long paramLong, boolean paramBoolean)
/*     */   {
/* 112 */     this.name = new Long(paramLong).toString();
/* 113 */     this.primaryGroup = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 126 */     return this.name;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 139 */     return new Long(this.name).longValue();
/*     */   }
/*     */ 
/*     */   public boolean isPrimaryGroup()
/*     */   {
/* 153 */     return this.primaryGroup;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 166 */     return rb.getString("SolarisNumericGroupPrincipal.Supplementary.Group.") + this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 190 */     if (paramObject == null) {
/* 191 */       return false;
/*     */     }
/* 193 */     if (this == paramObject) {
/* 194 */       return true;
/*     */     }
/* 196 */     if (!(paramObject instanceof SolarisNumericGroupPrincipal))
/* 197 */       return false;
/* 198 */     SolarisNumericGroupPrincipal localSolarisNumericGroupPrincipal = (SolarisNumericGroupPrincipal)paramObject;
/*     */ 
/* 200 */     if ((getName().equals(localSolarisNumericGroupPrincipal.getName())) && (isPrimaryGroup() == localSolarisNumericGroupPrincipal.isPrimaryGroup()))
/*     */     {
/* 202 */       return true;
/* 203 */     }return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 214 */     return toString().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.SolarisNumericGroupPrincipal
 * JD-Core Version:    0.6.2
 */