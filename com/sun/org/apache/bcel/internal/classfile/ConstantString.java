/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantString extends Constant
/*     */   implements ConstantObject
/*     */ {
/*     */   private int string_index;
/*     */ 
/*     */   public ConstantString(ConstantString c)
/*     */   {
/*  79 */     this(c.getStringIndex());
/*     */   }
/*     */ 
/*     */   ConstantString(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  89 */     this(file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public ConstantString(int string_index)
/*     */   {
/*  96 */     super((byte)8);
/*  97 */     this.string_index = string_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 107 */     v.visitConstantString(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 117 */     file.writeByte(this.tag);
/* 118 */     file.writeShort(this.string_index);
/*     */   }
/*     */ 
/*     */   public final int getStringIndex()
/*     */   {
/* 123 */     return this.string_index;
/*     */   }
/*     */ 
/*     */   public final void setStringIndex(int string_index)
/*     */   {
/* 128 */     this.string_index = string_index;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 135 */     return super.toString() + "(string_index = " + this.string_index + ")";
/*     */   }
/*     */ 
/*     */   public Object getConstantValue(ConstantPool cp)
/*     */   {
/* 141 */     Constant c = cp.getConstant(this.string_index, (byte)1);
/* 142 */     return ((ConstantUtf8)c).getBytes();
/*     */   }
/*     */ 
/*     */   public String getBytes(ConstantPool cp)
/*     */   {
/* 148 */     return (String)getConstantValue(cp);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantString
 * JD-Core Version:    0.6.2
 */