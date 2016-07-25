/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantLong extends Constant
/*     */   implements ConstantObject
/*     */ {
/*     */   private long bytes;
/*     */ 
/*     */   public ConstantLong(long bytes)
/*     */   {
/*  80 */     super((byte)5);
/*  81 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public ConstantLong(ConstantLong c)
/*     */   {
/*  87 */     this(c.getBytes());
/*     */   }
/*     */ 
/*     */   ConstantLong(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  97 */     this(file.readLong());
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 107 */     v.visitConstantLong(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 117 */     file.writeByte(this.tag);
/* 118 */     file.writeLong(this.bytes);
/*     */   }
/*     */ 
/*     */   public final long getBytes()
/*     */   {
/* 123 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public final void setBytes(long bytes)
/*     */   {
/* 128 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 134 */     return super.toString() + "(bytes = " + this.bytes + ")";
/*     */   }
/*     */ 
/*     */   public Object getConstantValue(ConstantPool cp)
/*     */   {
/* 140 */     return new Long(this.bytes);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantLong
 * JD-Core Version:    0.6.2
 */