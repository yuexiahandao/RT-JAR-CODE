/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantInteger extends Constant
/*     */   implements ConstantObject
/*     */ {
/*     */   private int bytes;
/*     */ 
/*     */   public ConstantInteger(int bytes)
/*     */   {
/*  82 */     super((byte)3);
/*  83 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public ConstantInteger(ConstantInteger c)
/*     */   {
/*  90 */     this(c.getBytes());
/*     */   }
/*     */ 
/*     */   ConstantInteger(DataInputStream file)
/*     */     throws IOException
/*     */   {
/* 101 */     this(file.readInt());
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 112 */     v.visitConstantInteger(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 123 */     file.writeByte(this.tag);
/* 124 */     file.writeInt(this.bytes);
/*     */   }
/*     */ 
/*     */   public final int getBytes()
/*     */   {
/* 130 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public final void setBytes(int bytes)
/*     */   {
/* 136 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 143 */     return super.toString() + "(bytes = " + this.bytes + ")";
/*     */   }
/*     */ 
/*     */   public Object getConstantValue(ConstantPool cp)
/*     */   {
/* 149 */     return new Integer(this.bytes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantInteger
 * JD-Core Version:    0.6.2
 */