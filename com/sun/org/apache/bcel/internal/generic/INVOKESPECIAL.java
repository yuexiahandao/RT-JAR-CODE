/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class INVOKESPECIAL extends InvokeInstruction
/*     */ {
/*     */   INVOKESPECIAL()
/*     */   {
/*     */   }
/*     */ 
/*     */   public INVOKESPECIAL(int index)
/*     */   {
/*  79 */     super((short)183, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  83 */     Class[] cs = new Class[4 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  85 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  88 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 3)] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
/*  89 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 2)] = ExceptionConstants.ABSTRACT_METHOD_ERROR;
/*  90 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1)] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*  91 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.NULL_POINTER_EXCEPTION;
/*     */ 
/*  93 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 106 */     v.visitExceptionThrower(this);
/* 107 */     v.visitTypedInstruction(this);
/* 108 */     v.visitStackConsumer(this);
/* 109 */     v.visitStackProducer(this);
/* 110 */     v.visitLoadClass(this);
/* 111 */     v.visitCPInstruction(this);
/* 112 */     v.visitFieldOrMethod(this);
/* 113 */     v.visitInvokeInstruction(this);
/* 114 */     v.visitINVOKESPECIAL(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL
 * JD-Core Version:    0.6.2
 */