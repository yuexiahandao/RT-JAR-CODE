/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class IINC extends LocalVariableInstruction
/*     */ {
/*     */   private boolean wide;
/*     */   private int c;
/*     */ 
/*     */   IINC()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IINC(int n, int c)
/*     */   {
/*  85 */     this.opcode = 132;
/*  86 */     this.length = 3;
/*     */ 
/*  88 */     setIndex(n);
/*  89 */     setIncrement(c);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  97 */     if (this.wide) {
/*  98 */       out.writeByte(196);
/*     */     }
/* 100 */     out.writeByte(this.opcode);
/*     */ 
/* 102 */     if (this.wide) {
/* 103 */       out.writeShort(this.n);
/* 104 */       out.writeShort(this.c);
/*     */     } else {
/* 106 */       out.writeByte(this.n);
/* 107 */       out.writeByte(this.c);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void setWide() {
/* 112 */     if ((this.wide = (this.n > 65535) || (Math.abs(this.c) > 127) ? 1 : 0) != 0)
/*     */     {
/* 114 */       this.length = 6;
/*     */     }
/* 116 */     else this.length = 3;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 124 */     this.wide = wide;
/*     */ 
/* 126 */     if (wide) {
/* 127 */       this.length = 6;
/* 128 */       this.n = bytes.readUnsignedShort();
/* 129 */       this.c = bytes.readShort();
/*     */     } else {
/* 131 */       this.length = 3;
/* 132 */       this.n = bytes.readUnsignedByte();
/* 133 */       this.c = bytes.readByte();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 141 */     return super.toString(verbose) + " " + this.c;
/*     */   }
/*     */ 
/*     */   public final void setIndex(int n)
/*     */   {
/* 148 */     if (n < 0) {
/* 149 */       throw new ClassGenException("Negative index value: " + n);
/*     */     }
/* 151 */     this.n = n;
/* 152 */     setWide();
/*     */   }
/*     */ 
/*     */   public final int getIncrement()
/*     */   {
/* 158 */     return this.c;
/*     */   }
/*     */ 
/*     */   public final void setIncrement(int c)
/*     */   {
/* 164 */     this.c = c;
/* 165 */     setWide();
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cp)
/*     */   {
/* 171 */     return Type.INT;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 183 */     v.visitLocalVariableInstruction(this);
/* 184 */     v.visitIINC(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.IINC
 * JD-Core Version:    0.6.2
 */