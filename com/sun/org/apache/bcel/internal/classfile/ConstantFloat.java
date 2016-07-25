/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantFloat extends Constant
/*     */   implements ConstantObject
/*     */ {
/*     */   private float bytes;
/*     */ 
/*     */   public ConstantFloat(float bytes)
/*     */   {
/*  80 */     super((byte)4);
/*  81 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public ConstantFloat(ConstantFloat c)
/*     */   {
/*  88 */     this(c.getBytes());
/*     */   }
/*     */ 
/*     */   ConstantFloat(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  98 */     this(file.readFloat());
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 108 */     v.visitConstantFloat(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 118 */     file.writeByte(this.tag);
/* 119 */     file.writeFloat(this.bytes);
/*     */   }
/*     */ 
/*     */   public final float getBytes()
/*     */   {
/* 124 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public final void setBytes(float bytes)
/*     */   {
/* 129 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 136 */     return super.toString() + "(bytes = " + this.bytes + ")";
/*     */   }
/*     */ 
/*     */   public Object getConstantValue(ConstantPool cp)
/*     */   {
/* 142 */     return new Float(this.bytes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantFloat
 * JD-Core Version:    0.6.2
 */