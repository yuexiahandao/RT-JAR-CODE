/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class JSR extends JsrInstruction
/*     */   implements VariableLengthInstruction
/*     */ {
/*     */   JSR()
/*     */   {
/*     */   }
/*     */ 
/*     */   public JSR(InstructionHandle target)
/*     */   {
/*  75 */     super((short)168, target);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  83 */     this.index = getTargetOffset();
/*  84 */     if (this.opcode == 168) {
/*  85 */       super.dump(out);
/*     */     } else {
/*  87 */       this.index = getTargetOffset();
/*  88 */       out.writeByte(this.opcode);
/*  89 */       out.writeInt(this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int updatePosition(int offset, int max_offset) {
/*  94 */     int i = getTargetOffset();
/*     */ 
/*  96 */     this.position += offset;
/*     */ 
/*  98 */     if (Math.abs(i) >= 32767 - max_offset) {
/*  99 */       this.opcode = 201;
/* 100 */       this.length = 5;
/* 101 */       return 2;
/*     */     }
/*     */ 
/* 104 */     return 0;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 116 */     v.visitStackProducer(this);
/* 117 */     v.visitVariableLengthInstruction(this);
/* 118 */     v.visitBranchInstruction(this);
/* 119 */     v.visitJsrInstruction(this);
/* 120 */     v.visitJSR(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.JSR
 * JD-Core Version:    0.6.2
 */