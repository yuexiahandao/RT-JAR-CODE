/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ @Deprecated
/*     */ public class SolarisPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7840670002439379038L;
/*  52 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  56 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private String name;
/*     */ 
/*     */   public SolarisPrincipal(String paramString)
/*     */   {
/*  78 */     if (paramString == null) {
/*  79 */       throw new NullPointerException(rb.getString("provided.null.name"));
/*     */     }
/*  81 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  92 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return rb.getString("SolarisPrincipal.") + this.name;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 121 */     if (paramObject == null) {
/* 122 */       return false;
/*     */     }
/* 124 */     if (this == paramObject) {
/* 125 */       return true;
/*     */     }
/* 127 */     if (!(paramObject instanceof SolarisPrincipal))
/* 128 */       return false;
/* 129 */     SolarisPrincipal localSolarisPrincipal = (SolarisPrincipal)paramObject;
/*     */ 
/* 131 */     if (getName().equals(localSolarisPrincipal.getName()))
/* 132 */       return true;
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 144 */     return this.name.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.SolarisPrincipal
 * JD-Core Version:    0.6.2
 */