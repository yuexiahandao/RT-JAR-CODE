/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class GETSTATIC extends FieldInstruction
/*     */   implements PushInstruction, ExceptionThrower
/*     */ {
/*     */   GETSTATIC()
/*     */   {
/*     */   }
/*     */ 
/*     */   public GETSTATIC(int index)
/*     */   {
/*  80 */     super((short)178, index);
/*     */   }
/*     */   public int produceStack(ConstantPoolGen cpg) {
/*  83 */     return getFieldSize(cpg);
/*     */   }
/*     */   public Class[] getExceptions() {
/*  86 */     Class[] cs = new Class[1 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
/*     */ 
/*  88 */     System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
/*     */ 
/*  90 */     cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
/*     */ 
/*  93 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 106 */     v.visitStackProducer(this);
/* 107 */     v.visitPushInstruction(this);
/* 108 */     v.visitExceptionThrower(this);
/* 109 */     v.visitTypedInstruction(this);
/* 110 */     v.visitLoadClass(this);
/* 111 */     v.visitCPInstruction(this);
/* 112 */     v.visitFieldOrMethod(this);
/* 113 */     v.visitFieldInstruction(this);
/* 114 */     v.visitGETSTATIC(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.GETSTATIC
 * JD-Core Version:    0.6.2
 */