/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public class FCONST extends Instruction
/*     */   implements ConstantPushInstruction, TypedInstruction
/*     */ {
/*     */   private float value;
/*     */ 
/*     */   FCONST()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FCONST(float f)
/*     */   {
/*  79 */     super((short)11, (short)1);
/*     */ 
/*  81 */     if (f == 0.0D)
/*  82 */       this.opcode = 11;
/*  83 */     else if (f == 1.0D)
/*  84 */       this.opcode = 12;
/*  85 */     else if (f == 2.0D)
/*  86 */       this.opcode = 13;
/*     */     else {
/*  88 */       throw new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f);
/*     */     }
/*  90 */     this.value = f;
/*     */   }
/*     */   public Number getValue() {
/*  93 */     return new Float(this.value);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  98 */     return Type.FLOAT;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 110 */     v.visitPushInstruction(this);
/* 111 */     v.visitStackProducer(this);
/* 112 */     v.visitTypedInstruction(this);
/* 113 */     v.visitConstantPushInstruction(this);
/* 114 */     v.visitFCONST(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FCONST
 * JD-Core Version:    0.6.2
 */