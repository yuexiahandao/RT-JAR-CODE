/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class LineNumberTable extends Attribute
/*     */ {
/*     */   private int line_number_table_length;
/*     */   private LineNumber[] line_number_table;
/*     */ 
/*     */   public LineNumberTable(LineNumberTable c)
/*     */   {
/*  82 */     this(c.getNameIndex(), c.getLength(), c.getLineNumberTable(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public LineNumberTable(int name_index, int length, LineNumber[] line_number_table, ConstantPool constant_pool)
/*     */   {
/*  96 */     super((byte)4, name_index, length, constant_pool);
/*  97 */     setLineNumberTable(line_number_table);
/*     */   }
/*     */ 
/*     */   LineNumberTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 111 */     this(name_index, length, (LineNumber[])null, constant_pool);
/* 112 */     this.line_number_table_length = file.readUnsignedShort();
/* 113 */     this.line_number_table = new LineNumber[this.line_number_table_length];
/*     */ 
/* 115 */     for (int i = 0; i < this.line_number_table_length; i++)
/* 116 */       this.line_number_table[i] = new LineNumber(file);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 126 */     v.visitLineNumberTable(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 136 */     super.dump(file);
/* 137 */     file.writeShort(this.line_number_table_length);
/* 138 */     for (int i = 0; i < this.line_number_table_length; i++)
/* 139 */       this.line_number_table[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final LineNumber[] getLineNumberTable()
/*     */   {
/* 145 */     return this.line_number_table;
/*     */   }
/*     */ 
/*     */   public final void setLineNumberTable(LineNumber[] line_number_table)
/*     */   {
/* 151 */     this.line_number_table = line_number_table;
/*     */ 
/* 153 */     this.line_number_table_length = (line_number_table == null ? 0 : line_number_table.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 161 */     StringBuffer buf = new StringBuffer();
/* 162 */     StringBuffer line = new StringBuffer();
/*     */ 
/* 164 */     for (int i = 0; i < this.line_number_table_length; i++) {
/* 165 */       line.append(this.line_number_table[i].toString());
/*     */ 
/* 167 */       if (i < this.line_number_table_length - 1) {
/* 168 */         line.append(", ");
/*     */       }
/* 170 */       if (line.length() > 72) {
/* 171 */         line.append('\n');
/* 172 */         buf.append(line);
/* 173 */         line.setLength(0);
/*     */       }
/*     */     }
/*     */ 
/* 177 */     buf.append(line);
/*     */ 
/* 179 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public int getSourceLine(int pos)
/*     */   {
/* 189 */     int l = 0; int r = this.line_number_table_length - 1;
/*     */ 
/* 191 */     if (r < 0) {
/* 192 */       return -1;
/*     */     }
/* 194 */     int min_index = -1; int min = -1;
/*     */     do
/*     */     {
/* 199 */       int i = (l + r) / 2;
/* 200 */       int j = this.line_number_table[i].getStartPC();
/*     */ 
/* 202 */       if (j == pos)
/* 203 */         return this.line_number_table[i].getLineNumber();
/* 204 */       if (pos < j)
/* 205 */         r = i - 1;
/*     */       else {
/* 207 */         l = i + 1;
/*     */       }
/*     */ 
/* 213 */       if ((j < pos) && (j > min)) {
/* 214 */         min = j;
/* 215 */         min_index = i;
/*     */       }
/*     */     }
/* 217 */     while (l <= r);
/*     */ 
/* 222 */     if (min_index < 0) {
/* 223 */       return -1;
/*     */     }
/* 225 */     return this.line_number_table[min_index].getLineNumber();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 232 */     LineNumberTable c = (LineNumberTable)clone();
/*     */ 
/* 234 */     c.line_number_table = new LineNumber[this.line_number_table_length];
/* 235 */     for (int i = 0; i < this.line_number_table_length; i++) {
/* 236 */       c.line_number_table[i] = this.line_number_table[i].copy();
/*     */     }
/* 238 */     c.constant_pool = constant_pool;
/* 239 */     return c;
/*     */   }
/*     */   public final int getTableLength() {
/* 242 */     return this.line_number_table_length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.LineNumberTable
 * JD-Core Version:    0.6.2
 */