/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ 
/*     */ public class CHECKCAST extends CPInstruction
/*     */   implements LoadClass, ExceptionThrower, StackProducer, StackConsumer
/*     */ {
/*     */   CHECKCAST()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CHECKCAST(int index)
/*     */   {
/*  79 */     super((short)192, index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions()
/*     */   {
/*  85 */     Class[] cs = new Class[1 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
/*     */ 
/*  87 */     System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
/*     */ 
/*  89 */     cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.CLASS_CAST_EXCEPTION;
/*     */ 
/*  91 */     return cs;
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
/*  95 */     Type t = getType(cpg);
/*     */ 
/*  97 */     if ((t instanceof ArrayType)) {
/*  98 */       t = ((ArrayType)t).getBasicType();
/*     */     }
/* 100 */     return (t instanceof ObjectType) ? (ObjectType)t : null;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 112 */     v.visitLoadClass(this);
/* 113 */     v.visitExceptionThrower(this);
/* 114 */     v.visitStackProducer(this);
/* 115 */     v.visitStackConsumer(this);
/* 116 */     v.visitTypedInstruction(this);
/* 117 */     v.visitCPInstruction(this);
/* 118 */     v.visitCHECKCAST(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.CHECKCAST
 * JD-Core Version:    0.6.2
 */