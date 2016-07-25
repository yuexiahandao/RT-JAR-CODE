/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.NotActiveException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ResourceBundle;
/*     */ import sun.security.x509.X500Name;
/*     */ 
/*     */ @Deprecated
/*     */ public class X500Principal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8222422609431628648L;
/*  57 */   private static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public ResourceBundle run()
/*     */     {
/*  61 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*     */     }
/*     */   });
/*     */   private String name;
/*     */   private transient X500Name thisX500Name;
/*     */ 
/*     */   public X500Principal(String paramString)
/*     */   {
/*  89 */     if (paramString == null)
/*  90 */       throw new NullPointerException(rb.getString("provided.null.name"));
/*     */     try
/*     */     {
/*  93 */       this.thisX500Name = new X500Name(paramString);
/*     */     } catch (Exception localException) {
/*  95 */       throw new IllegalArgumentException(localException.toString());
/*     */     }
/*     */ 
/*  98 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 109 */     return this.thisX500Name.getName();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 120 */     return this.thisX500Name.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 136 */     if (paramObject == null) {
/* 137 */       return false;
/*     */     }
/* 139 */     if (this == paramObject) {
/* 140 */       return true;
/*     */     }
/* 142 */     if ((paramObject instanceof X500Principal)) {
/* 143 */       X500Principal localX500Principal = (X500Principal)paramObject;
/*     */       try {
/* 145 */         X500Name localX500Name = new X500Name(localX500Principal.getName());
/* 146 */         return this.thisX500Name.equals(localX500Name);
/*     */       }
/*     */       catch (Exception localException) {
/* 149 */         return false;
/*     */       }
/*     */     }
/* 151 */     if ((paramObject instanceof Serializable))
/*     */     {
/* 154 */       return paramObject.equals(this.thisX500Name);
/*     */     }
/*     */ 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 168 */     return this.thisX500Name.hashCode();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, NotActiveException, ClassNotFoundException
/*     */   {
/* 179 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 182 */     this.thisX500Name = new X500Name(this.name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.X500Principal
 * JD-Core Version:    0.6.2
 */