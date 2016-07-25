/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class PUTFIELD extends FieldInstruction
/*     */   implements PopInstruction, ExceptionThrower
/*     */ {
/*     */   PUTFIELD()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PUTFIELD(int index)
/*     */   {
/*  82 */     super((short)181, index);
/*     */   }
/*     */   public int consumeStack(ConstantPoolGen cpg) {
/*  85 */     return getFieldSize(cpg) + 1;
/*     */   }
/*     */   public Class[] getExceptions() {
/*  88 */     Class[] cs = new Class[2 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  90 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  93 */     cs[(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1)] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*     */ 
/*  95 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.NULL_POINTER_EXCEPTION;
/*     */ 
/*  98 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 111 */     v.visitExceptionThrower(this);
/* 112 */     v.visitStackConsumer(this);
/* 113 */     v.visitPopInstruction(this);
/* 114 */     v.visitTypedInstruction(this);
/* 115 */     v.visitLoadClass(this);
/* 116 */     v.visitCPInstruction(this);
/* 117 */     v.visitFieldOrMethod(this);
/* 118 */     v.visitFieldInstruction(this);
/* 119 */     v.visitPUTFIELD(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.PUTFIELD
 * JD-Core Version:    0.6.2
 */