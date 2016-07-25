/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class StackMap extends Attribute
/*     */   implements Node
/*     */ {
/*     */   private int map_length;
/*     */   private StackMapEntry[] map;
/*     */ 
/*     */   public StackMap(int name_index, int length, StackMapEntry[] map, ConstantPool constant_pool)
/*     */   {
/*  91 */     super((byte)11, name_index, length, constant_pool);
/*     */ 
/*  93 */     setStackMap(map);
/*     */   }
/*     */ 
/*     */   StackMap(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 107 */     this(name_index, length, (StackMapEntry[])null, constant_pool);
/*     */ 
/* 109 */     this.map_length = file.readUnsignedShort();
/* 110 */     this.map = new StackMapEntry[this.map_length];
/*     */ 
/* 112 */     for (int i = 0; i < this.map_length; i++)
/* 113 */       this.map[i] = new StackMapEntry(file, constant_pool);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 124 */     super.dump(file);
/* 125 */     file.writeShort(this.map_length);
/* 126 */     for (int i = 0; i < this.map_length; i++)
/* 127 */       this.map[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final StackMapEntry[] getStackMap()
/*     */   {
/* 133 */     return this.map;
/*     */   }
/*     */ 
/*     */   public final void setStackMap(StackMapEntry[] map)
/*     */   {
/* 139 */     this.map = map;
/*     */ 
/* 141 */     this.map_length = (map == null ? 0 : map.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 148 */     StringBuffer buf = new StringBuffer("StackMap(");
/*     */ 
/* 150 */     for (int i = 0; i < this.map_length; i++) {
/* 151 */       buf.append(this.map[i].toString());
/*     */ 
/* 153 */       if (i < this.map_length - 1) {
/* 154 */         buf.append(", ");
/*     */       }
/*     */     }
/* 157 */     buf.append(')');
/*     */ 
/* 159 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 166 */     StackMap c = (StackMap)clone();
/*     */ 
/* 168 */     c.map = new StackMapEntry[this.map_length];
/* 169 */     for (int i = 0; i < this.map_length; i++) {
/* 170 */       c.map[i] = this.map[i].copy();
/*     */     }
/* 172 */     c.constant_pool = constant_pool;
/* 173 */     return c;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 184 */     v.visitStackMap(this);
/*     */   }
/*     */   public final int getMapLength() {
/* 187 */     return this.map_length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.StackMap
 * JD-Core Version:    0.6.2
 */