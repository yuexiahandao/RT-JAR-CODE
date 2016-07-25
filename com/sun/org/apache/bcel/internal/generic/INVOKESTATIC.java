/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class INVOKESTATIC extends InvokeInstruction
/*     */ {
/*     */   INVOKESTATIC()
/*     */   {
/*     */   }
/*     */ 
/*     */   public INVOKESTATIC(int index)
/*     */   {
/*  78 */     super((short)184, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  82 */     Class[] cs = new Class[2 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  84 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  87 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
/*  88 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1)] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*     */ 
/*  90 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 103 */     v.visitExceptionThrower(this);
/* 104 */     v.visitTypedInstruction(this);
/* 105 */     v.visitStackConsumer(this);
/* 106 */     v.visitStackProducer(this);
/* 107 */     v.visitLoadClass(this);
/* 108 */     v.visitCPInstruction(this);
/* 109 */     v.visitFieldOrMethod(this);
/* 110 */     v.visitInvokeInstruction(this);
/* 111 */     v.visitINVOKESTATIC(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.INVOKESTATIC
 * JD-Core Version:    0.6.2
 */