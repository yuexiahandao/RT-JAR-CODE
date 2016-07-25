/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public class LCONST extends Instruction
/*     */   implements ConstantPushInstruction, TypedInstruction
/*     */ {
/*     */   private long value;
/*     */ 
/*     */   LCONST()
/*     */   {
/*     */   }
/*     */ 
/*     */   public LCONST(long l)
/*     */   {
/*  79 */     super((short)9, (short)1);
/*     */ 
/*  81 */     if (l == 0L)
/*  82 */       this.opcode = 9;
/*  83 */     else if (l == 1L)
/*  84 */       this.opcode = 10;
/*     */     else {
/*  86 */       throw new ClassGenException("LCONST can be used only for 0 and 1: " + l);
/*     */     }
/*  88 */     this.value = l;
/*     */   }
/*     */   public Number getValue() {
/*  91 */     return new Long(this.value);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  96 */     return Type.LONG;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 108 */     v.visitPushInstruction(this);
/* 109 */     v.visitStackProducer(this);
/* 110 */     v.visitTypedInstruction(this);
/* 111 */     v.visitConstantPushInstruction(this);
/* 112 */     v.visitLCONST(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LCONST
 * JD-Core Version:    0.6.2
 */