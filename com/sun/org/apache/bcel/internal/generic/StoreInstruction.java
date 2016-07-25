/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public abstract class StoreInstruction extends LocalVariableInstruction
/*     */   implements PopInstruction
/*     */ {
/*     */   StoreInstruction(short canon_tag, short c_tag)
/*     */   {
/*  76 */     super(canon_tag, c_tag);
/*     */   }
/*     */ 
/*     */   protected StoreInstruction(short opcode, short c_tag, int n)
/*     */   {
/*  85 */     super(opcode, c_tag, n);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/*  97 */     v.visitStackConsumer(this);
/*  98 */     v.visitPopInstruction(this);
/*  99 */     v.visitTypedInstruction(this);
/* 100 */     v.visitLocalVariableInstruction(this);
/* 101 */     v.visitStoreInstruction(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.StoreInstruction
 * JD-Core Version:    0.6.2
 */