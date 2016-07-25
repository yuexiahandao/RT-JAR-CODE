/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class LocalVariableTypeTable extends Attribute
/*     */ {
/*     */   private static final long serialVersionUID = -1082157891095177114L;
/*     */   private int local_variable_type_table_length;
/*     */   private LocalVariable[] local_variable_type_table;
/*     */ 
/*     */   public LocalVariableTypeTable(LocalVariableTypeTable c)
/*     */   {
/*  59 */     this(c.getNameIndex(), c.getLength(), c.getLocalVariableTypeTable(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public LocalVariableTypeTable(int name_index, int length, LocalVariable[] local_variable_table, ConstantPool constant_pool)
/*     */   {
/*  67 */     super((byte)12, name_index, length, constant_pool);
/*  68 */     setLocalVariableTable(local_variable_table);
/*     */   }
/*     */ 
/*     */   LocalVariableTypeTable(int nameIdx, int len, DataInputStream dis, ConstantPool cpool) throws IOException {
/*  72 */     this(nameIdx, len, (LocalVariable[])null, cpool);
/*     */ 
/*  74 */     this.local_variable_type_table_length = dis.readUnsignedShort();
/*  75 */     this.local_variable_type_table = new LocalVariable[this.local_variable_type_table_length];
/*     */ 
/*  77 */     for (int i = 0; i < this.local_variable_type_table_length; i++)
/*  78 */       this.local_variable_type_table[i] = new LocalVariable(dis, cpool);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/*  83 */     v.visitLocalVariableTypeTable(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/*  89 */     super.dump(file);
/*  90 */     file.writeShort(this.local_variable_type_table_length);
/*  91 */     for (int i = 0; i < this.local_variable_type_table_length; i++)
/*  92 */       this.local_variable_type_table[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final LocalVariable[] getLocalVariableTypeTable() {
/*  96 */     return this.local_variable_type_table;
/*     */   }
/*     */ 
/*     */   public final LocalVariable getLocalVariable(int index) {
/* 100 */     for (int i = 0; i < this.local_variable_type_table_length; i++) {
/* 101 */       if (this.local_variable_type_table[i].getIndex() == index)
/* 102 */         return this.local_variable_type_table[i];
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   public final void setLocalVariableTable(LocalVariable[] local_variable_table)
/*     */   {
/* 109 */     this.local_variable_type_table = local_variable_table;
/* 110 */     this.local_variable_type_table_length = (local_variable_table == null ? 0 : local_variable_table.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 119 */     StringBuilder buf = new StringBuilder();
/*     */ 
/* 121 */     for (int i = 0; i < this.local_variable_type_table_length; i++) {
/* 122 */       buf.append(this.local_variable_type_table[i].toString());
/*     */ 
/* 124 */       if (i < this.local_variable_type_table_length - 1) buf.append('\n');
/*     */     }
/*     */ 
/* 127 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 135 */     LocalVariableTypeTable c = (LocalVariableTypeTable)clone();
/*     */ 
/* 137 */     c.local_variable_type_table = new LocalVariable[this.local_variable_type_table_length];
/* 138 */     for (int i = 0; i < this.local_variable_type_table_length; i++) {
/* 139 */       c.local_variable_type_table[i] = this.local_variable_type_table[i].copy();
/*     */     }
/* 141 */     c.constant_pool = constant_pool;
/* 142 */     return c;
/*     */   }
/*     */   public final int getTableLength() {
/* 145 */     return this.local_variable_type_table_length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.LocalVariableTypeTable
 * JD-Core Version:    0.6.2
 */