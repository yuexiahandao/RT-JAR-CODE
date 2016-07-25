/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class Deprecated extends Attribute
/*     */ {
/*     */   private byte[] bytes;
/*     */ 
/*     */   public Deprecated(Deprecated c)
/*     */   {
/*  80 */     this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public Deprecated(int name_index, int length, byte[] bytes, ConstantPool constant_pool)
/*     */   {
/*  92 */     super((byte)8, name_index, length, constant_pool);
/*  93 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   Deprecated(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 107 */     this(name_index, length, (byte[])null, constant_pool);
/*     */ 
/* 109 */     if (length > 0) {
/* 110 */       this.bytes = new byte[length];
/* 111 */       file.readFully(this.bytes);
/* 112 */       System.err.println("Deprecated attribute with length > 0");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 124 */     v.visitDeprecated(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 135 */     super.dump(file);
/*     */ 
/* 137 */     if (this.length > 0)
/* 138 */       file.write(this.bytes, 0, this.length);
/*     */   }
/*     */ 
/*     */   public final byte[] getBytes()
/*     */   {
/* 144 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public final void setBytes(byte[] bytes)
/*     */   {
/* 150 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 157 */     return com.sun.org.apache.bcel.internal.Constants.ATTRIBUTE_NAMES[8];
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 164 */     Deprecated c = (Deprecated)clone();
/*     */ 
/* 166 */     if (this.bytes != null) {
/* 167 */       c.bytes = ((byte[])this.bytes.clone());
/*     */     }
/* 169 */     c.constant_pool = constant_pool;
/* 170 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Deprecated
 * JD-Core Version:    0.6.2
 */