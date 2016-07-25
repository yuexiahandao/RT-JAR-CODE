/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ public class MissingResourceFailureException extends XMLSignatureException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  42 */   Reference uninitializedReference = null;
/*     */ 
/*     */   public MissingResourceFailureException(String paramString, Reference paramReference)
/*     */   {
/*  52 */     super(paramString);
/*     */ 
/*  54 */     this.uninitializedReference = paramReference;
/*     */   }
/*     */ 
/*     */   public MissingResourceFailureException(String paramString, Object[] paramArrayOfObject, Reference paramReference)
/*     */   {
/*  68 */     super(paramString, paramArrayOfObject);
/*     */ 
/*  70 */     this.uninitializedReference = paramReference;
/*     */   }
/*     */ 
/*     */   public MissingResourceFailureException(String paramString, Exception paramException, Reference paramReference)
/*     */   {
/*  85 */     super(paramString, paramException);
/*     */ 
/*  87 */     this.uninitializedReference = paramReference;
/*     */   }
/*     */ 
/*     */   public MissingResourceFailureException(String paramString, Object[] paramArrayOfObject, Exception paramException, Reference paramReference)
/*     */   {
/* 103 */     super(paramString, paramArrayOfObject, paramException);
/*     */ 
/* 105 */     this.uninitializedReference = paramReference;
/*     */   }
/*     */ 
/*     */   public void setReference(Reference paramReference)
/*     */   {
/* 115 */     this.uninitializedReference = paramReference;
/*     */   }
/*     */ 
/*     */   public Reference getReference()
/*     */   {
/* 128 */     return this.uninitializedReference;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.MissingResourceFailureException
 * JD-Core Version:    0.6.2
 */