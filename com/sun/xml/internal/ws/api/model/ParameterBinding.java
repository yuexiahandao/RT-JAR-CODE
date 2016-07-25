/*     */ package com.sun.xml.internal.ws.api.model;
/*     */ 
/*     */ public final class ParameterBinding
/*     */ {
/*  47 */   public static final ParameterBinding BODY = new ParameterBinding(Kind.BODY, null);
/*     */ 
/*  51 */   public static final ParameterBinding HEADER = new ParameterBinding(Kind.HEADER, null);
/*     */ 
/*  56 */   public static final ParameterBinding UNBOUND = new ParameterBinding(Kind.UNBOUND, null);
/*     */   public final Kind kind;
/*     */   private String mimeType;
/*     */ 
/*     */   public static ParameterBinding createAttachment(String mimeType)
/*     */   {
/*  67 */     return new ParameterBinding(Kind.ATTACHMENT, mimeType);
/*     */   }
/*     */ 
/*     */   private ParameterBinding(Kind kind, String mimeType)
/*     */   {
/*  90 */     this.kind = kind;
/*  91 */     this.mimeType = mimeType;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     return this.kind.toString();
/*     */   }
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 110 */     if (!isAttachment())
/* 111 */       throw new IllegalStateException();
/* 112 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   public boolean isBody() {
/* 116 */     return this == BODY;
/*     */   }
/*     */ 
/*     */   public boolean isHeader() {
/* 120 */     return this == HEADER;
/*     */   }
/*     */ 
/*     */   public boolean isUnbound() {
/* 124 */     return this == UNBOUND;
/*     */   }
/*     */ 
/*     */   public boolean isAttachment() {
/* 128 */     return this.kind == Kind.ATTACHMENT;
/*     */   }
/*     */ 
/*     */   public static enum Kind
/*     */   {
/*  74 */     BODY, HEADER, UNBOUND, ATTACHMENT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.ParameterBinding
 * JD-Core Version:    0.6.2
 */