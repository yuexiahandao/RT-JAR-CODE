/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import com.sun.corba.se.impl.io.TypeMismatchException;
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ 
/*     */ public final class RepIdDelegator
/*     */   implements RepositoryIdStrings, RepositoryIdUtility, RepositoryIdInterface
/*     */ {
/*     */   private RepositoryId delegate;
/*     */ 
/*     */   public String createForAnyType(Class paramClass)
/*     */   {
/*  49 */     return RepositoryId.createForAnyType(paramClass);
/*     */   }
/*     */ 
/*     */   public String createForJavaType(Serializable paramSerializable)
/*     */     throws TypeMismatchException
/*     */   {
/*  55 */     return RepositoryId.createForJavaType(paramSerializable);
/*     */   }
/*     */ 
/*     */   public String createForJavaType(Class paramClass)
/*     */     throws TypeMismatchException
/*     */   {
/*  61 */     return RepositoryId.createForJavaType(paramClass);
/*     */   }
/*     */ 
/*     */   public String createSequenceRepID(Object paramObject) {
/*  65 */     return RepositoryId.createSequenceRepID(paramObject);
/*     */   }
/*     */ 
/*     */   public String createSequenceRepID(Class paramClass) {
/*  69 */     return RepositoryId.createSequenceRepID(paramClass);
/*     */   }
/*     */ 
/*     */   public RepositoryIdInterface getFromString(String paramString) {
/*  73 */     return new RepIdDelegator(RepositoryId.cache.getId(paramString));
/*     */   }
/*     */ 
/*     */   public boolean isChunkedEncoding(int paramInt)
/*     */   {
/*  79 */     return RepositoryId.isChunkedEncoding(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isCodeBasePresent(int paramInt) {
/*  83 */     return RepositoryId.isCodeBasePresent(paramInt);
/*     */   }
/*     */ 
/*     */   public String getClassDescValueRepId() {
/*  87 */     return RepositoryId.kClassDescValueRepID;
/*     */   }
/*     */ 
/*     */   public String getWStringValueRepId() {
/*  91 */     return "IDL:omg.org/CORBA/WStringValue:1.0";
/*     */   }
/*     */ 
/*     */   public int getTypeInfo(int paramInt) {
/*  95 */     return RepositoryId.getTypeInfo(paramInt);
/*     */   }
/*     */ 
/*     */   public int getStandardRMIChunkedNoRepStrId() {
/*  99 */     return RepositoryId.kPreComputed_StandardRMIChunked_NoRep;
/*     */   }
/*     */ 
/*     */   public int getCodeBaseRMIChunkedNoRepStrId() {
/* 103 */     return RepositoryId.kPreComputed_CodeBaseRMIChunked_NoRep;
/*     */   }
/*     */ 
/*     */   public int getStandardRMIChunkedId() {
/* 107 */     return RepositoryId.kPreComputed_StandardRMIChunked;
/*     */   }
/*     */ 
/*     */   public int getCodeBaseRMIChunkedId() {
/* 111 */     return RepositoryId.kPreComputed_CodeBaseRMIChunked;
/*     */   }
/*     */ 
/*     */   public int getStandardRMIUnchunkedId() {
/* 115 */     return RepositoryId.kPreComputed_StandardRMIUnchunked;
/*     */   }
/*     */ 
/*     */   public int getCodeBaseRMIUnchunkedId() {
/* 119 */     return RepositoryId.kPreComputed_CodeBaseRMIUnchunked;
/*     */   }
/*     */ 
/*     */   public int getStandardRMIUnchunkedNoRepStrId() {
/* 123 */     return RepositoryId.kPreComputed_StandardRMIUnchunked_NoRep;
/*     */   }
/*     */ 
/*     */   public int getCodeBaseRMIUnchunkedNoRepStrId() {
/* 127 */     return RepositoryId.kPreComputed_CodeBaseRMIUnchunked_NoRep;
/*     */   }
/*     */ 
/*     */   public Class getClassFromType()
/*     */     throws ClassNotFoundException
/*     */   {
/* 133 */     return this.delegate.getClassFromType();
/*     */   }
/*     */ 
/*     */   public Class getClassFromType(String paramString)
/*     */     throws ClassNotFoundException, MalformedURLException
/*     */   {
/* 139 */     return this.delegate.getClassFromType(paramString);
/*     */   }
/*     */ 
/*     */   public Class getClassFromType(Class paramClass, String paramString)
/*     */     throws ClassNotFoundException, MalformedURLException
/*     */   {
/* 146 */     return this.delegate.getClassFromType(paramClass, paramString);
/*     */   }
/*     */ 
/*     */   public String getClassName() {
/* 150 */     return this.delegate.getClassName();
/*     */   }
/*     */ 
/*     */   public RepIdDelegator()
/*     */   {
/*     */   }
/*     */ 
/*     */   private RepIdDelegator(RepositoryId paramRepositoryId)
/*     */   {
/* 159 */     this.delegate = paramRepositoryId;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 165 */     if (this.delegate != null) {
/* 166 */       return this.delegate.toString();
/*     */     }
/* 168 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 172 */     if (this.delegate != null) {
/* 173 */       return this.delegate.equals(paramObject);
/*     */     }
/* 175 */     return super.equals(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.RepIdDelegator
 * JD-Core Version:    0.6.2
 */