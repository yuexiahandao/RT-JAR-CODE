/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.ExceptionConstants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantFloat;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantString;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class LDC extends CPInstruction
/*     */   implements PushInstruction, ExceptionThrower, TypedInstruction
/*     */ {
/*     */   LDC()
/*     */   {
/*     */   }
/*     */ 
/*     */   public LDC(int index)
/*     */   {
/*  79 */     super((short)19, index);
/*  80 */     setSize();
/*     */   }
/*     */ 
/*     */   protected final void setSize()
/*     */   {
/*  85 */     if (this.index <= 255) {
/*  86 */       this.opcode = 18;
/*  87 */       this.length = 2;
/*     */     } else {
/*  89 */       this.opcode = 19;
/*  90 */       this.length = 3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/*  99 */     out.writeByte(this.opcode);
/*     */ 
/* 101 */     if (this.length == 2)
/* 102 */       out.writeByte(this.index);
/*     */     else
/* 104 */       out.writeShort(this.index);
/*     */   }
/*     */ 
/*     */   public final void setIndex(int index)
/*     */   {
/* 111 */     super.setIndex(index);
/* 112 */     setSize();
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 121 */     this.length = 2;
/* 122 */     this.index = bytes.readUnsignedByte();
/*     */   }
/*     */ 
/*     */   public Object getValue(ConstantPoolGen cpg) {
/* 126 */     Constant c = cpg.getConstantPool().getConstant(this.index);
/*     */ 
/* 128 */     switch (c.getTag()) {
/*     */     case 8:
/* 130 */       int i = ((ConstantString)c).getStringIndex();
/* 131 */       c = cpg.getConstantPool().getConstant(i);
/* 132 */       return ((ConstantUtf8)c).getBytes();
/*     */     case 4:
/* 135 */       return new Float(((ConstantFloat)c).getBytes());
/*     */     case 3:
/* 138 */       return new Integer(((ConstantInteger)c).getBytes());
/*     */     }
/*     */ 
/* 141 */     throw new RuntimeException("Unknown or invalid constant type at " + this.index);
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cpg)
/*     */   {
/* 146 */     switch (cpg.getConstantPool().getConstant(this.index).getTag()) { case 8:
/* 147 */       return Type.STRING;
/*     */     case 4:
/* 148 */       return Type.FLOAT;
/*     */     case 3:
/* 149 */       return Type.INT;
/*     */     }
/* 151 */     throw new RuntimeException("Unknown or invalid constant type at " + this.index);
/*     */   }
/*     */ 
/*     */   public Class[] getExceptions()
/*     */   {
/* 156 */     return ExceptionConstants.EXCS_STRING_RESOLUTION;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 168 */     v.visitStackProducer(this);
/* 169 */     v.visitPushInstruction(this);
/* 170 */     v.visitExceptionThrower(this);
/* 171 */     v.visitTypedInstruction(this);
/* 172 */     v.visitCPInstruction(this);
/* 173 */     v.visitLDC(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.LDC
 * JD-Core Version:    0.6.2
 */