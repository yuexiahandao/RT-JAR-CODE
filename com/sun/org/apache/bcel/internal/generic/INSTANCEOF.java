/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class INSTANCEOF extends CPInstruction
/*     */   implements LoadClass, ExceptionThrower, StackProducer, StackConsumer
/*     */ {
/*     */   INSTANCEOF()
/*     */   {
/*     */   }
/*     */ 
/*     */   public INSTANCEOF(int index)
/*     */   {
/*  76 */     super((short)193, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions() {
/*  80 */     return ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION;
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
/*  84 */     Type t = getType(cpg);
/*     */ 
/*  86 */     if ((t instanceof ArrayType)) {
/*  87 */       t = ((ArrayType)t).getBasicType();
/*     */     }
/*  89 */     return (t instanceof ObjectType) ? (ObjectType)t : null;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 101 */     v.visitLoadClass(this);
/* 102 */     v.visitExceptionThrower(this);
/* 103 */     v.visitStackProducer(this);
/* 104 */     v.visitStackConsumer(this);
/* 105 */     v.visitTypedInstruction(this);
/* 106 */     v.visitCPInstruction(this);
/* 107 */     v.visitINSTANCEOF(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.INSTANCEOF
 * JD-Core Version:    0.6.2
 */