/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class GOTO extends GotoInstruction
/*     */   implements VariableLengthInstruction
/*     */ {
/*     */   GOTO()
/*     */   {
/*     */   }
/*     */ 
/*     */   public GOTO(InstructionHandle target)
/*     */   {
/*  75 */     super((short)167, target);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  83 */     this.index = getTargetOffset();
/*  84 */     if (this.opcode == 167) {
/*  85 */       super.dump(out);
/*     */     } else {
/*  87 */       this.index = getTargetOffset();
/*  88 */       out.writeByte(this.opcode);
/*  89 */       out.writeInt(this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset)
/*     */   {
/*  97 */     int i = getTargetOffset();
/*     */ 
/*  99 */     this.position += offset;
/*     */ 
/* 101 */     if (Math.abs(i) >= 32767 - max_offset) {
/* 102 */       this.opcode = 200;
/* 103 */       this.length = 5;
/* 104 */       return 2;
/*     */     }
/*     */ 
/* 107 */     return 0;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 119 */     v.visitVariableLengthInstruction(this);
/* 120 */     v.visitUnconditionalBranch(this);
/* 121 */     v.visitBranchInstruction(this);
/* 122 */     v.visitGotoInstruction(this);
/* 123 */     v.visitGOTO(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.GOTO
 * JD-Core Version:    0.6.2
 */