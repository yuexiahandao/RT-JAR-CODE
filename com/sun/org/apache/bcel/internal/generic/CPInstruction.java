/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class CPInstruction extends Instruction
/*     */   implements TypedInstruction, IndexedInstruction
/*     */ {
/*     */   protected int index;
/*     */ 
/*     */   CPInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected CPInstruction(short opcode, int index)
/*     */   {
/*  91 */     super(opcode, (short)3);
/*  92 */     setIndex(index);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/* 100 */     out.writeByte(this.opcode);
/* 101 */     out.writeShort(this.index);
/*     */   }
/*     */ 
/*     */   public String toString(boolean verbose)
/*     */   {
/* 114 */     return super.toString(verbose) + " " + this.index;
/*     */   }
/*     */ 
/*     */   public String toString(ConstantPool cp)
/*     */   {
/* 121 */     Constant c = cp.getConstant(this.index);
/* 122 */     String str = cp.constantToString(c);
/*     */ 
/* 124 */     if ((c instanceof ConstantClass)) {
/* 125 */       str = str.replace('.', '/');
/*     */     }
/* 127 */     return com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[this.opcode] + " " + str;
/*     */   }
/*     */ 
/*     */   protected void initFromFile(ByteSequence bytes, boolean wide)
/*     */     throws IOException
/*     */   {
/* 138 */     setIndex(bytes.readUnsignedShort());
/* 139 */     this.length = 3;
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 145 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index)
/*     */   {
/* 152 */     if (index < 0) {
/* 153 */       throw new ClassGenException("Negative index value: " + index);
/*     */     }
/* 155 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cpg)
/*     */   {
/* 161 */     ConstantPool cp = cpg.getConstantPool();
/* 162 */     String name = cp.getConstantString(this.index, (byte)7);
/*     */ 
/* 164 */     if (!name.startsWith("[")) {
/* 165 */       name = "L" + name + ";";
/*     */     }
/* 167 */     return Type.getType(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.CPInstruction
 * JD-Core Version:    0.6.2
 */