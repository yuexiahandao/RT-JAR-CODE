/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class PUTSTATIC extends FieldInstruction
/*     */   implements ExceptionThrower, PopInstruction
/*     */ {
/*     */   PUTSTATIC()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PUTSTATIC(int index)
/*     */   {
/*  81 */     super((short)179, index);
/*     */   }
/*     */   public int consumeStack(ConstantPoolGen cpg) {
/*  84 */     return getFieldSize(cpg);
/*     */   }
/*     */   public Class[] getExceptions() {
/*  87 */     Class[] cs = new Class[1 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  89 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  91 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*     */ 
/*  94 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 107 */     v.visitExceptionThrower(this);
/* 108 */     v.visitStackConsumer(this);
/* 109 */     v.visitPopInstruction(this);
/* 110 */     v.visitTypedInstruction(this);
/* 111 */     v.visitLoadClass(this);
/* 112 */     v.visitCPInstruction(this);
/* 113 */     v.visitFieldOrMethod(this);
/* 114 */     v.visitFieldInstruction(this);
/* 115 */     v.visitPUTSTATIC(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.PUTSTATIC
 * JD-Core Version:    0.6.2
 */