/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class LocalVariableTable extends Attribute
/*     */ {
/*     */   private int local_variable_table_length;
/*     */   private LocalVariable[] local_variable_table;
/*     */ 
/*     */   public LocalVariableTable(LocalVariableTable c)
/*     */   {
/*  81 */     this(c.getNameIndex(), c.getLength(), c.getLocalVariableTable(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public LocalVariableTable(int name_index, int length, LocalVariable[] local_variable_table, ConstantPool constant_pool)
/*     */   {
/*  95 */     super((byte)5, name_index, length, constant_pool);
/*  96 */     setLocalVariableTable(local_variable_table);
/*     */   }
/*     */ 
/*     */   LocalVariableTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 110 */     this(name_index, length, (LocalVariable[])null, constant_pool);
/*     */ 
/* 112 */     this.local_variable_table_length = file.readUnsignedShort();
/* 113 */     this.local_variable_table = new LocalVariable[this.local_variable_table_length];
/*     */ 
/* 115 */     for (int i = 0; i < this.local_variable_table_length; i++)
/* 116 */       this.local_variable_table[i] = new LocalVariable(file, constant_pool);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 127 */     v.visitLocalVariableTable(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 138 */     super.dump(file);
/* 139 */     file.writeShort(this.local_variable_table_length);
/* 140 */     for (int i = 0; i < this.local_variable_table_length; i++)
/* 141 */       this.local_variable_table[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final LocalVariable[] getLocalVariableTable()
/*     */   {
/* 148 */     return this.local_variable_table;
/*     */   }
/*     */ 
/*     */   public final LocalVariable getLocalVariable(int index)
/*     */   {
/* 154 */     for (int i = 0; i < this.local_variable_table_length; i++) {
/* 155 */       if (this.local_variable_table[i].getIndex() == index)
/* 156 */         return this.local_variable_table[i];
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public final void setLocalVariableTable(LocalVariable[] local_variable_table)
/*     */   {
/* 163 */     this.local_variable_table = local_variable_table;
/* 164 */     this.local_variable_table_length = (local_variable_table == null ? 0 : local_variable_table.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 172 */     StringBuffer buf = new StringBuffer("");
/*     */ 
/* 174 */     for (int i = 0; i < this.local_variable_table_length; i++) {
/* 175 */       buf.append(this.local_variable_table[i].toString());
/*     */ 
/* 177 */       if (i < this.local_variable_table_length - 1) {
/* 178 */         buf.append('\n');
/*     */       }
/*     */     }
/* 181 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 188 */     LocalVariableTable c = (LocalVariableTable)clone();
/*     */ 
/* 190 */     c.local_variable_table = new LocalVariable[this.local_variable_table_length];
/* 191 */     for (int i = 0; i < this.local_variable_table_length; i++) {
/* 192 */       c.local_variable_table[i] = this.local_variable_table[i].copy();
/*     */     }
/* 194 */     c.constant_pool = constant_pool;
/* 195 */     return c;
/*     */   }
/*     */   public final int getTableLength() {
/* 198 */     return this.local_variable_table_length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.LocalVariableTable
 * JD-Core Version:    0.6.2
 */