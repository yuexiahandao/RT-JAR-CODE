/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class INVOKEVIRTUAL extends InvokeInstruction
/*     */ {
/*     */   INVOKEVIRTUAL()
/*     */   {
/*     */   }
/*     */ 
/*     */   public INVOKEVIRTUAL(int index)
/*     */   {
/*  78 */     super((short)182, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  82 */     Class[] cs = new Class[4 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  84 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  87 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 3)] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
/*  88 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 2)] = ExceptionConstants.ABSTRACT_METHOD_ERROR;
/*  89 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1)] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*  90 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.NULL_POINTER_EXCEPTION;
/*     */ 
/*  92 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 105 */     v.visitExceptionThrower(this);
/* 106 */     v.visitTypedInstruction(this);
/* 107 */     v.visitStackConsumer(this);
/* 108 */     v.visitStackProducer(this);
/* 109 */     v.visitLoadClass(this);
/* 110 */     v.visitCPInstruction(this);
/* 111 */     v.visitFieldOrMethod(this);
/* 112 */     v.visitInvokeInstruction(this);
/* 113 */     v.visitINVOKEVIRTUAL(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL
 * JD-Core Version:    0.6.2
 */