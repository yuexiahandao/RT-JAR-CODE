/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class ConstantCP extends Constant
/*     */ {
/*     */   protected int class_index;
/*     */   protected int name_and_type_index;
/*     */ 
/*     */   public ConstantCP(ConstantCP c)
/*     */   {
/*  80 */     this(c.getTag(), c.getClassIndex(), c.getNameAndTypeIndex());
/*     */   }
/*     */ 
/*     */   ConstantCP(byte tag, DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  92 */     this(tag, file.readUnsignedShort(), file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   protected ConstantCP(byte tag, int class_index, int name_and_type_index)
/*     */   {
/* 101 */     super(tag);
/* 102 */     this.class_index = class_index;
/* 103 */     this.name_and_type_index = name_and_type_index;
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 114 */     file.writeByte(this.tag);
/* 115 */     file.writeShort(this.class_index);
/* 116 */     file.writeShort(this.name_and_type_index);
/*     */   }
/*     */ 
/*     */   public final int getClassIndex()
/*     */   {
/* 122 */     return this.class_index;
/*     */   }
/*     */ 
/*     */   public final int getNameAndTypeIndex()
/*     */   {
/* 127 */     return this.name_and_type_index;
/*     */   }
/*     */ 
/*     */   public final void setClassIndex(int class_index)
/*     */   {
/* 133 */     this.class_index = class_index;
/*     */   }
/*     */ 
/*     */   public String getClass(ConstantPool cp)
/*     */   {
/* 140 */     return cp.constantToString(this.class_index, (byte)7);
/*     */   }
/*     */ 
/*     */   public final void setNameAndTypeIndex(int name_and_type_index)
/*     */   {
/* 147 */     this.name_and_type_index = name_and_type_index;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 154 */     return super.toString() + "(class_index = " + this.class_index + ", name_and_type_index = " + this.name_and_type_index + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantCP
 * JD-Core Version:    0.6.2
 */