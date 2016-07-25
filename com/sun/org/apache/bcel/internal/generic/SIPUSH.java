/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class SIPUSH extends Instruction
/*     */   implements ConstantPushInstruction
/*     */ {
/*     */   private short b;
/*     */ 
/*     */   SIPUSH()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SIPUSH(short b)
/*     */   {
/*  80 */     super((short)17, (short)3);
/*  81 */     this.b = b;
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  88 */     super.dump(out);
/*  89 */     out.writeShort(this.b);
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/*  96 */     return super.toString(verbose) + " " + this.b;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 104 */     this.length = 3;
/* 105 */     this.b = bytes.readShort();
/*     */   }
/*     */   public Number getValue() {
/* 108 */     return new Integer(this.b);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 113 */     return Type.SHORT;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 125 */     v.visitPushInstruction(this);
/* 126 */     v.visitStackProducer(this);
/* 127 */     v.visitTypedInstruction(this);
/* 128 */     v.visitConstantPushInstruction(this);
/* 129 */     v.visitSIPUSH(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.SIPUSH
 * JD-Core Version:    0.6.2
 */