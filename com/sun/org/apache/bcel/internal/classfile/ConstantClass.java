/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantClass extends Constant
/*     */   implements ConstantObject
/*     */ {
/*     */   private int name_index;
/*     */ 
/*     */   public ConstantClass(ConstantClass c)
/*     */   {
/*  79 */     this(c.getNameIndex());
/*     */   }
/*     */ 
/*     */   ConstantClass(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  90 */     this(file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public ConstantClass(int name_index)
/*     */   {
/*  98 */     super((byte)7);
/*  99 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 110 */     v.visitConstantClass(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 121 */     file.writeByte(this.tag);
/* 122 */     file.writeShort(this.name_index);
/*     */   }
/*     */ 
/*     */   public final int getNameIndex()
/*     */   {
/* 128 */     return this.name_index;
/*     */   }
/*     */ 
/*     */   public final void setNameIndex(int name_index)
/*     */   {
/* 134 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public Object getConstantValue(ConstantPool cp)
/*     */   {
/* 141 */     Constant c = cp.getConstant(this.name_index, (byte)1);
/* 142 */     return ((ConstantUtf8)c).getBytes();
/*     */   }
/*     */ 
/*     */   public String getBytes(ConstantPool cp)
/*     */   {
/* 148 */     return (String)getConstantValue(cp);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 155 */     return super.toString() + "(name_index = " + this.name_index + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantClass
 * JD-Core Version:    0.6.2
 */