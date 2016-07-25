/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TABLESWITCH extends Select
/*     */ {
/*     */   TABLESWITCH()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TABLESWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target)
/*     */   {
/*  84 */     super((short)170, match, targets, target);
/*     */ 
/*  86 */     this.length = ((short)(13 + this.match_length * 4));
/*     */ 
/*  88 */     this.fixed_length = this.length;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  96 */     super.dump(out);
/*     */ 
/*  98 */     int low = this.match_length > 0 ? this.match[0] : 0;
/*  99 */     out.writeInt(low);
/*     */ 
/* 101 */     int high = this.match_length > 0 ? this.match[(this.match_length - 1)] : 0;
/* 102 */     out.writeInt(high);
/*     */ 
/* 104 */     for (int i = 0; i < this.match_length; i++)
/* 105 */       out.writeInt(this.indices[i] = getTargetOffset(this.targets[i]));
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 113 */     super.initFromFile(bytes, wide);
/*     */ 
/* 115 */     int low = bytes.readInt();
/* 116 */     int high = bytes.readInt();
/*     */ 
/* 118 */     this.match_length = (high - low + 1);
/* 119 */     this.fixed_length = ((short)(13 + this.match_length * 4));
/* 120 */     this.length = ((short)(this.fixed_length + this.padding));
/*     */ 
/* 122 */     this.match = new int[this.match_length];
/* 123 */     this.indices = new int[this.match_length];
/* 124 */     this.targets = new InstructionHandle[this.match_length];
/*     */ 
/* 126 */     for (int i = low; i <= high; i++) {
/* 127 */       this.match[(i - low)] = i;
/*     */     }
/* 129 */     for (int i = 0; i < this.match_length; i++)
/* 130 */       this.indices[i] = bytes.readInt();
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 144 */     v.visitVariableLengthInstruction(this);
/* 145 */     v.visitStackProducer(this);
/* 146 */     v.visitBranchInstruction(this);
/* 147 */     v.visitSelect(this);
/* 148 */     v.visitTABLESWITCH(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.TABLESWITCH
 * JD-Core Version:    0.6.2
 */