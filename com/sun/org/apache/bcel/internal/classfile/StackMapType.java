/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class StackMapType
/*     */   implements Cloneable
/*     */ {
/*     */   private byte type;
/*  75 */   private int index = -1;
/*     */   private ConstantPool constant_pool;
/*     */ 
/*     */   StackMapType(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  85 */     this(file.readByte(), -1, constant_pool);
/*     */ 
/*  87 */     if (hasIndex()) {
/*  88 */       setIndex(file.readShort());
/*     */     }
/*  90 */     setConstantPool(constant_pool);
/*     */   }
/*     */ 
/*     */   public StackMapType(byte type, int index, ConstantPool constant_pool)
/*     */   {
/*  98 */     setType(type);
/*  99 */     setIndex(index);
/* 100 */     setConstantPool(constant_pool);
/*     */   }
/*     */ 
/*     */   public void setType(byte t) {
/* 104 */     if ((t < 0) || (t > 8))
/* 105 */       throw new RuntimeException("Illegal type for StackMapType: " + t);
/* 106 */     this.type = t;
/*     */   }
/*     */   public byte getType() {
/* 109 */     return this.type; } 
/* 110 */   public void setIndex(int t) { this.index = t; }
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 115 */     return this.index;
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 125 */     file.writeByte(this.type);
/* 126 */     if (hasIndex())
/* 127 */       file.writeShort(getIndex());
/*     */   }
/*     */ 
/*     */   public final boolean hasIndex()
/*     */   {
/* 133 */     return (this.type == 7) || (this.type == 8);
/*     */   }
/*     */ 
/*     */   private String printIndex()
/*     */   {
/* 138 */     if (this.type == 7)
/* 139 */       return ", class=" + this.constant_pool.constantToString(this.index, (byte)7);
/* 140 */     if (this.type == 8) {
/* 141 */       return ", offset=" + this.index;
/*     */     }
/* 143 */     return "";
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 150 */     return "(type=" + com.sun.org.apache.bcel.internal.Constants.ITEM_NAMES[this.type] + printIndex() + ")";
/*     */   }
/*     */ 
/*     */   public StackMapType copy()
/*     */   {
/*     */     try
/*     */     {
/* 158 */       return (StackMapType)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   public final ConstantPool getConstantPool()
/*     */   {
/* 167 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public final void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 173 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.StackMapType
 * JD-Core Version:    0.6.2
 */