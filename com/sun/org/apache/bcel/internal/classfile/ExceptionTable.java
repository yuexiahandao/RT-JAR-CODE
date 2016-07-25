/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ExceptionTable extends Attribute
/*     */ {
/*     */   private int number_of_exceptions;
/*     */   private int[] exception_index_table;
/*     */ 
/*     */   public ExceptionTable(ExceptionTable c)
/*     */   {
/*  84 */     this(c.getNameIndex(), c.getLength(), c.getExceptionIndexTable(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public ExceptionTable(int name_index, int length, int[] exception_index_table, ConstantPool constant_pool)
/*     */   {
/*  98 */     super((byte)3, name_index, length, constant_pool);
/*  99 */     setExceptionIndexTable(exception_index_table);
/*     */   }
/*     */ 
/*     */   ExceptionTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 113 */     this(name_index, length, (int[])null, constant_pool);
/*     */ 
/* 115 */     this.number_of_exceptions = file.readUnsignedShort();
/* 116 */     this.exception_index_table = new int[this.number_of_exceptions];
/*     */ 
/* 118 */     for (int i = 0; i < this.number_of_exceptions; i++)
/* 119 */       this.exception_index_table[i] = file.readUnsignedShort();
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 130 */     v.visitExceptionTable(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 141 */     super.dump(file);
/* 142 */     file.writeShort(this.number_of_exceptions);
/* 143 */     for (int i = 0; i < this.number_of_exceptions; i++)
/* 144 */       file.writeShort(this.exception_index_table[i]);
/*     */   }
/*     */ 
/*     */   public final int[] getExceptionIndexTable()
/*     */   {
/* 150 */     return this.exception_index_table;
/*     */   }
/*     */ 
/*     */   public final int getNumberOfExceptions() {
/* 154 */     return this.number_of_exceptions;
/*     */   }
/*     */ 
/*     */   public final String[] getExceptionNames()
/*     */   {
/* 160 */     String[] names = new String[this.number_of_exceptions];
/* 161 */     for (int i = 0; i < this.number_of_exceptions; i++) {
/* 162 */       names[i] = this.constant_pool.getConstantString(this.exception_index_table[i], 7).replace('/', '.');
/*     */     }
/*     */ 
/* 165 */     return names;
/*     */   }
/*     */ 
/*     */   public final void setExceptionIndexTable(int[] exception_index_table)
/*     */   {
/* 173 */     this.exception_index_table = exception_index_table;
/* 174 */     this.number_of_exceptions = (exception_index_table == null ? 0 : exception_index_table.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 181 */     StringBuffer buf = new StringBuffer("");
/*     */ 
/* 184 */     for (int i = 0; i < this.number_of_exceptions; i++) {
/* 185 */       String str = this.constant_pool.getConstantString(this.exception_index_table[i], (byte)7);
/*     */ 
/* 187 */       buf.append(Utility.compactClassName(str, false));
/*     */ 
/* 189 */       if (i < this.number_of_exceptions - 1) {
/* 190 */         buf.append(", ");
/*     */       }
/*     */     }
/* 193 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 200 */     ExceptionTable c = (ExceptionTable)clone();
/* 201 */     c.exception_index_table = ((int[])this.exception_index_table.clone());
/* 202 */     c.constant_pool = constant_pool;
/* 203 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ExceptionTable
 * JD-Core Version:    0.6.2
 */