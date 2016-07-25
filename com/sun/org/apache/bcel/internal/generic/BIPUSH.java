/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class BIPUSH extends Instruction
/*     */   implements ConstantPushInstruction
/*     */ {
/*     */   private byte b;
/*     */ 
/*     */   BIPUSH()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BIPUSH(byte b)
/*     */   {
/*  83 */     super((short)16, (short)2);
/*  84 */     this.b = b;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  91 */     super.dump(out);
/*  92 */     out.writeByte(this.b);
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/*  99 */     return super.toString(verbose) + " " + this.b;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 107 */     this.length = 2;
/* 108 */     this.b = bytes.readByte();
/*     */   }
/*     */   public Number getValue() {
/* 111 */     return new Integer(this.b);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 116 */     return Type.BYTE;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 128 */     v.visitPushInstruction(this);
/* 129 */     v.visitStackProducer(this);
/* 130 */     v.visitTypedInstruction(this);
/* 131 */     v.visitConstantPushInstruction(this);
/* 132 */     v.visitBIPUSH(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.BIPUSH
 * JD-Core Version:    0.6.2
 */