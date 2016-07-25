/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class LOOKUPSWITCH extends Select
/*     */ {
/*     */   LOOKUPSWITCH()
/*     */   {
/*     */   }
/*     */ 
/*     */   public LOOKUPSWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target)
/*     */   {
/*  78 */     super((short)171, match, targets, target);
/*     */ 
/*  80 */     this.length = ((short)(9 + this.match_length * 8));
/*     */ 
/*  82 */     this.fixed_length = this.length;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  90 */     super.dump(out);
/*  91 */     out.writeInt(this.match_length);
/*     */ 
/*  93 */     for (int i = 0; i < this.match_length; i++) {
/*  94 */       out.writeInt(this.match[i]);
/*  95 */       out.writeInt(this.indices[i] = getTargetOffset(this.targets[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 104 */     super.initFromFile(bytes, wide);
/*     */ 
/* 106 */     this.match_length = bytes.readInt();
/* 107 */     this.fixed_length = ((short)(9 + this.match_length * 8));
/* 108 */     this.length = ((short)(this.fixed_length + this.padding));
/*     */ 
/* 110 */     this.match = new int[this.match_length];
/* 111 */     this.indices = new int[this.match_length];
/* 112 */     this.targets = new InstructionHandle[this.match_length];
/*     */ 
/* 114 */     for (int i = 0; i < this.match_length; i++) {
/* 115 */       this.match[i] = bytes.readInt();
/* 116 */       this.indices[i] = bytes.readInt();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 130 */     v.visitVariableLengthInstruction(this);
/* 131 */     v.visitStackProducer(this);
/* 132 */     v.visitBranchInstruction(this);
/* 133 */     v.visitSelect(this);
/* 134 */     v.visitLOOKUPSWITCH(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LOOKUPSWITCH
 * JD-Core Version:    0.6.2
 */