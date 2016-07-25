/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantNameAndType extends Constant
/*     */ {
/*     */   private int name_index;
/*     */   private int signature_index;
/*     */ 
/*     */   public ConstantNameAndType(ConstantNameAndType c)
/*     */   {
/*  81 */     this(c.getNameIndex(), c.getSignatureIndex());
/*     */   }
/*     */ 
/*     */   ConstantNameAndType(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  92 */     this(file.readUnsignedShort(), file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public ConstantNameAndType(int name_index, int signature_index)
/*     */   {
/* 102 */     super((byte)12);
/* 103 */     this.name_index = name_index;
/* 104 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 115 */     v.visitConstantNameAndType(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 126 */     file.writeByte(this.tag);
/* 127 */     file.writeShort(this.name_index);
/* 128 */     file.writeShort(this.signature_index);
/*     */   }
/*     */ 
/*     */   public final int getNameIndex()
/*     */   {
/* 134 */     return this.name_index;
/*     */   }
/*     */ 
/*     */   public final String getName(ConstantPool cp)
/*     */   {
/* 139 */     return cp.constantToString(getNameIndex(), (byte)1);
/*     */   }
/*     */ 
/*     */   public final int getSignatureIndex()
/*     */   {
/* 145 */     return this.signature_index;
/*     */   }
/*     */ 
/*     */   public final String getSignature(ConstantPool cp)
/*     */   {
/* 150 */     return cp.constantToString(getSignatureIndex(), (byte)1);
/*     */   }
/*     */ 
/*     */   public final void setNameIndex(int name_index)
/*     */   {
/* 157 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public final void setSignatureIndex(int signature_index)
/*     */   {
/* 164 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 171 */     return super.toString() + "(name_index = " + this.name_index + ", signature_index = " + this.signature_index + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType
 * JD-Core Version:    0.6.2
 */