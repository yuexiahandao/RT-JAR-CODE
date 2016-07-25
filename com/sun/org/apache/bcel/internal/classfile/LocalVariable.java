/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class LocalVariable
/*     */   implements Constants, Cloneable, Node, Serializable
/*     */ {
/*     */   private int start_pc;
/*     */   private int length;
/*     */   private int name_index;
/*     */   private int signature_index;
/*     */   private int index;
/*     */   private ConstantPool constant_pool;
/*     */ 
/*     */   public LocalVariable(LocalVariable c)
/*     */   {
/*  89 */     this(c.getStartPC(), c.getLength(), c.getNameIndex(), c.getSignatureIndex(), c.getIndex(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   LocalVariable(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 101 */     this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
/*     */   }
/*     */ 
/*     */   public LocalVariable(int start_pc, int length, int name_index, int signature_index, int index, ConstantPool constant_pool)
/*     */   {
/* 118 */     this.start_pc = start_pc;
/* 119 */     this.length = length;
/* 120 */     this.name_index = name_index;
/* 121 */     this.signature_index = signature_index;
/* 122 */     this.index = index;
/* 123 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 134 */     v.visitLocalVariable(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 145 */     file.writeShort(this.start_pc);
/* 146 */     file.writeShort(this.length);
/* 147 */     file.writeShort(this.name_index);
/* 148 */     file.writeShort(this.signature_index);
/* 149 */     file.writeShort(this.index);
/*     */   }
/*     */ 
/*     */   public final ConstantPool getConstantPool()
/*     */   {
/* 155 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/* 160 */     return this.length;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 168 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, (byte)1);
/* 169 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   public final int getNameIndex()
/*     */   {
/* 175 */     return this.name_index;
/*     */   }
/*     */ 
/*     */   public final String getSignature()
/*     */   {
/* 182 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
/*     */ 
/* 184 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   public final int getSignatureIndex()
/*     */   {
/* 190 */     return this.signature_index;
/*     */   }
/*     */ 
/*     */   public final int getIndex()
/*     */   {
/* 195 */     return this.index;
/*     */   }
/*     */ 
/*     */   public final int getStartPC()
/*     */   {
/* 200 */     return this.start_pc;
/*     */   }
/*     */ 
/*     */   public final void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 206 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public final void setLength(int length)
/*     */   {
/* 213 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public final void setNameIndex(int name_index)
/*     */   {
/* 220 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public final void setSignatureIndex(int signature_index)
/*     */   {
/* 227 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public final void setIndex(int index)
/*     */   {
/* 233 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public final void setStartPC(int start_pc)
/*     */   {
/* 239 */     this.start_pc = start_pc;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 246 */     String name = getName(); String signature = Utility.signatureToString(getSignature());
/*     */ 
/* 248 */     return "LocalVariable(start_pc = " + this.start_pc + ", length = " + this.length + ", index = " + this.index + ":" + signature + " " + name + ")";
/*     */   }
/*     */ 
/*     */   public LocalVariable copy()
/*     */   {
/*     */     try
/*     */     {
/* 257 */       return (LocalVariable)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 260 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.LocalVariable
 * JD-Core Version:    0.6.2
 */