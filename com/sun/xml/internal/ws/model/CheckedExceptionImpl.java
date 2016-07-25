/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.addressing.WsaActionUtil;
/*     */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*     */ import com.sun.xml.internal.ws.api.model.ExceptionType;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ 
/*     */ public final class CheckedExceptionImpl
/*     */   implements CheckedException
/*     */ {
/*     */   private final Class exceptionClass;
/*     */   private final TypeReference detail;
/*     */   private final ExceptionType exceptionType;
/*     */   private final JavaMethodImpl javaMethod;
/*     */   private String messageName;
/*  58 */   private String faultAction = "";
/*     */ 
/*     */   public CheckedExceptionImpl(JavaMethodImpl jm, Class exceptionClass, TypeReference detail, ExceptionType exceptionType)
/*     */   {
/*  71 */     this.detail = detail;
/*  72 */     this.exceptionType = exceptionType;
/*  73 */     this.exceptionClass = exceptionClass;
/*  74 */     this.javaMethod = jm;
/*     */   }
/*     */ 
/*     */   public AbstractSEIModelImpl getOwner() {
/*  78 */     return this.javaMethod.owner;
/*     */   }
/*     */ 
/*     */   public JavaMethod getParent() {
/*  82 */     return this.javaMethod;
/*     */   }
/*     */ 
/*     */   public Class getExceptionClass()
/*     */   {
/*  90 */     return this.exceptionClass;
/*     */   }
/*     */ 
/*     */   public Class getDetailBean() {
/*  94 */     return (Class)this.detail.type;
/*     */   }
/*     */ 
/*     */   public Bridge getBridge() {
/*  98 */     return getOwner().getBridge(this.detail);
/*     */   }
/*     */ 
/*     */   public TypeReference getDetailType() {
/* 102 */     return this.detail;
/*     */   }
/*     */ 
/*     */   public ExceptionType getExceptionType() {
/* 106 */     return this.exceptionType;
/*     */   }
/*     */ 
/*     */   public String getMessageName() {
/* 110 */     return this.messageName;
/*     */   }
/*     */ 
/*     */   public void setMessageName(String messageName) {
/* 114 */     this.messageName = messageName;
/*     */   }
/*     */ 
/*     */   public String getFaultAction() {
/* 118 */     return this.faultAction;
/*     */   }
/*     */ 
/*     */   public void setFaultAction(String faultAction) {
/* 122 */     this.faultAction = faultAction;
/*     */   }
/*     */ 
/*     */   public String getDefaultFaultAction() {
/* 126 */     return WsaActionUtil.getDefaultFaultAction(this.javaMethod, this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.CheckedExceptionImpl
 * JD-Core Version:    0.6.2
 */