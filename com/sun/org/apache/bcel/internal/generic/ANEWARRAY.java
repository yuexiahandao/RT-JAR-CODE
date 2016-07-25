/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class ANEWARRAY extends CPInstruction
/*     */   implements LoadClass, AllocationInstruction, ExceptionThrower, StackProducer
/*     */ {
/*     */   ANEWARRAY()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ANEWARRAY(int index)
/*     */   {
/*  77 */     super((short)189, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  81 */     Class[] cs = new Class[1 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
/*     */ 
/*  83 */     System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
/*     */ 
/*  85 */     cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
/*     */ 
/*  87 */     return cs;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/*  99 */     v.visitLoadClass(this);
/* 100 */     v.visitAllocationInstruction(this);
/* 101 */     v.visitExceptionThrower(this);
/* 102 */     v.visitStackProducer(this);
/* 103 */     v.visitTypedInstruction(this);
/* 104 */     v.visitCPInstruction(this);
/* 105 */     v.visitANEWARRAY(this);
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
/* 109 */     Type t = getType(cpg);
/*     */ 
/* 111 */     if ((t instanceof ArrayType)) {
/* 112 */       t = ((ArrayType)t).getBasicType();
/*     */     }
/*     */ 
/* 115 */     return (t instanceof ObjectType) ? (ObjectType)t : null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ANEWARRAY
 * JD-Core Version:    0.6.2
 */