/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class RET extends Instruction
/*     */   implements IndexedInstruction, TypedInstruction
/*     */ {
/*     */   private boolean wide;
/*     */   private int index;
/*     */ 
/*     */   RET()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RET(int index)
/*     */   {
/*  81 */     super((short)169, (short)2);
/*  82 */     setIndex(index);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  90 */     if (this.wide) {
/*  91 */       out.writeByte(196);
/*     */     }
/*  93 */     out.writeByte(this.opcode);
/*     */ 
/*  95 */     if (this.wide)
/*  96 */       out.writeShort(this.index);
/*     */     else
/*  98 */       out.writeByte(this.index);
/*     */   }
/*     */ 
/*     */   private final void setWide() {
/* 102 */     if ((this.wide = this.index > 255 ? 1 : 0) != 0)
/* 103 */       this.length = 4;
/*     */     else
/* 105 */       this.length = 2;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 113 */     this.wide = wide;
/*     */ 
/* 115 */     if (wide) {
/* 116 */       this.index = bytes.readUnsignedShort();
/* 117 */       this.length = 4;
/*     */     } else {
/* 119 */       this.index = bytes.readUnsignedByte();
/* 120 */       this.length = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 127 */     return this.index;
/*     */   }
/*     */ 
/*     */   public final void setIndex(int n)
/*     */   {
/* 133 */     if (n < 0) {
/* 134 */       throw new ClassGenException("Negative index value: " + n);
/*     */     }
/* 136 */     this.index = n;
/* 137 */     setWide();
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 144 */     return super.toString(verbose) + " " + this.index;
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 150 */     return ReturnaddressType.NO_TARGET;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 162 */     v.visitRET(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.RET
 * JD-Core Version:    0.6.2
 */