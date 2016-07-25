/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class Synthetic extends Attribute
/*     */ {
/*     */   private byte[] bytes;
/*     */ 
/*     */   public Synthetic(Synthetic c)
/*     */   {
/*  84 */     this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public Synthetic(int name_index, int length, byte[] bytes, ConstantPool constant_pool)
/*     */   {
/*  98 */     super((byte)7, name_index, length, constant_pool);
/*  99 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   Synthetic(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 113 */     this(name_index, length, (byte[])null, constant_pool);
/*     */ 
/* 115 */     if (length > 0) {
/* 116 */       this.bytes = new byte[length];
/* 117 */       file.readFully(this.bytes);
/* 118 */       System.err.println("Synthetic attribute with length > 0");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 129 */     v.visitSynthetic(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 139 */     super.dump(file);
/* 140 */     if (this.length > 0)
/* 141 */       file.write(this.bytes, 0, this.length);
/*     */   }
/*     */ 
/*     */   public final byte[] getBytes()
/*     */   {
/* 146 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public final void setBytes(byte[] bytes)
/*     */   {
/* 152 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 159 */     StringBuffer buf = new StringBuffer("Synthetic");
/*     */ 
/* 161 */     if (this.length > 0) {
/* 162 */       buf.append(" " + Utility.toHexString(this.bytes));
/*     */     }
/* 164 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 171 */     Synthetic c = (Synthetic)clone();
/*     */ 
/* 173 */     if (this.bytes != null) {
/* 174 */       c.bytes = ((byte[])this.bytes.clone());
/*     */     }
/* 176 */     c.constant_pool = constant_pool;
/* 177 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Synthetic
 * JD-Core Version:    0.6.2
 */