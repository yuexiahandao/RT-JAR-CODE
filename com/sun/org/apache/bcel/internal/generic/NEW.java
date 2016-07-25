/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class NEW extends CPInstruction
/*     */   implements LoadClass, AllocationInstruction, ExceptionThrower, StackProducer
/*     */ {
/*     */   NEW()
/*     */   {
/*     */   }
/*     */ 
/*     */   public NEW(int index)
/*     */   {
/*  78 */     super((short)187, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  82 */     Class[] cs = new Class[2 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
/*     */ 
/*  84 */     System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
/*     */ 
/*  87 */     cs[(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length + 1)] = ExceptionConstants.INSTANTIATION_ERROR;
/*  88 */     cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
/*     */ 
/*  90 */     return cs;
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
/*  94 */     return (ObjectType)getType(cpg);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 106 */     v.visitLoadClass(this);
/* 107 */     v.visitAllocationInstruction(this);
/* 108 */     v.visitExceptionThrower(this);
/* 109 */     v.visitStackProducer(this);
/* 110 */     v.visitTypedInstruction(this);
/* 111 */     v.visitCPInstruction(this);
/* 112 */     v.visitNEW(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.NEW
 * JD-Core Version:    0.6.2
 */