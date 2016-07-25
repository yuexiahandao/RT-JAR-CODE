/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public abstract class JsrInstruction extends BranchInstruction
/*     */   implements UnconditionalBranch, TypedInstruction, StackProducer
/*     */ {
/*     */   JsrInstruction(short opcode, InstructionHandle target)
/*     */   {
/*  70 */     super(opcode, target);
/*     */   }
/*     */ 
/*     */   JsrInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/*  82 */     return new ReturnaddressType(physicalSuccessor());
/*     */   }
/*     */ 
/*     */   public InstructionHandle physicalSuccessor()
/*     */   {
/*  97 */     InstructionHandle ih = this.target;
/*     */ 
/* 100 */     while (ih.getPrev() != null) {
/* 101 */       ih = ih.getPrev();
/*     */     }
/*     */ 
/* 104 */     while (ih.getInstruction() != this) {
/* 105 */       ih = ih.getNext();
/*     */     }
/* 107 */     InstructionHandle toThis = ih;
/*     */ 
/* 109 */     while (ih != null) {
/* 110 */       ih = ih.getNext();
/* 111 */       if ((ih != null) && (ih.getInstruction() == this)) {
/* 112 */         throw new RuntimeException("physicalSuccessor() called on a shared JsrInstruction.");
/*     */       }
/*     */     }
/*     */ 
/* 116 */     return toThis.getNext();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.JsrInstruction
 * JD-Core Version:    0.6.2
 */