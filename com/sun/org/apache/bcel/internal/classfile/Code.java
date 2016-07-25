/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class Code extends Attribute
/*     */ {
/*     */   private int max_stack;
/*     */   private int max_locals;
/*     */   private int code_length;
/*     */   private byte[] code;
/*     */   private int exception_table_length;
/*     */   private CodeException[] exception_table;
/*     */   private int attributes_count;
/*     */   private Attribute[] attributes;
/*     */ 
/*     */   public Code(Code c)
/*     */   {
/*  98 */     this(c.getNameIndex(), c.getLength(), c.getMaxStack(), c.getMaxLocals(), c.getCode(), c.getExceptionTable(), c.getAttributes(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   Code(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 113 */     this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[])null, (CodeException[])null, (Attribute[])null, constant_pool);
/*     */ 
/* 118 */     this.code_length = file.readInt();
/* 119 */     this.code = new byte[this.code_length];
/* 120 */     file.readFully(this.code);
/*     */ 
/* 125 */     this.exception_table_length = file.readUnsignedShort();
/* 126 */     this.exception_table = new CodeException[this.exception_table_length];
/*     */ 
/* 128 */     for (int i = 0; i < this.exception_table_length; i++) {
/* 129 */       this.exception_table[i] = new CodeException(file);
/*     */     }
/*     */ 
/* 134 */     this.attributes_count = file.readUnsignedShort();
/* 135 */     this.attributes = new Attribute[this.attributes_count];
/* 136 */     for (int i = 0; i < this.attributes_count; i++) {
/* 137 */       this.attributes[i] = Attribute.readAttribute(file, constant_pool);
/*     */     }
/*     */ 
/* 143 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public Code(int name_index, int length, int max_stack, int max_locals, byte[] code, CodeException[] exception_table, Attribute[] attributes, ConstantPool constant_pool)
/*     */   {
/* 163 */     super((byte)2, name_index, length, constant_pool);
/*     */ 
/* 165 */     this.max_stack = max_stack;
/* 166 */     this.max_locals = max_locals;
/*     */ 
/* 168 */     setCode(code);
/* 169 */     setExceptionTable(exception_table);
/* 170 */     setAttributes(attributes);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 181 */     v.visitCode(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 192 */     super.dump(file);
/*     */ 
/* 194 */     file.writeShort(this.max_stack);
/* 195 */     file.writeShort(this.max_locals);
/* 196 */     file.writeInt(this.code_length);
/* 197 */     file.write(this.code, 0, this.code_length);
/*     */ 
/* 199 */     file.writeShort(this.exception_table_length);
/* 200 */     for (int i = 0; i < this.exception_table_length; i++) {
/* 201 */       this.exception_table[i].dump(file);
/*     */     }
/* 203 */     file.writeShort(this.attributes_count);
/* 204 */     for (int i = 0; i < this.attributes_count; i++)
/* 205 */       this.attributes[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final Attribute[] getAttributes()
/*     */   {
/* 212 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public LineNumberTable getLineNumberTable()
/*     */   {
/* 218 */     for (int i = 0; i < this.attributes_count; i++) {
/* 219 */       if ((this.attributes[i] instanceof LineNumberTable))
/* 220 */         return (LineNumberTable)this.attributes[i];
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   public LocalVariableTable getLocalVariableTable()
/*     */   {
/* 229 */     for (int i = 0; i < this.attributes_count; i++) {
/* 230 */       if ((this.attributes[i] instanceof LocalVariableTable))
/* 231 */         return (LocalVariableTable)this.attributes[i];
/*     */     }
/* 233 */     return null;
/*     */   }
/*     */ 
/*     */   public final byte[] getCode()
/*     */   {
/* 239 */     return this.code;
/*     */   }
/*     */ 
/*     */   public final CodeException[] getExceptionTable()
/*     */   {
/* 245 */     return this.exception_table;
/*     */   }
/*     */ 
/*     */   public final int getMaxLocals()
/*     */   {
/* 250 */     return this.max_locals;
/*     */   }
/*     */ 
/*     */   public final int getMaxStack()
/*     */   {
/* 256 */     return this.max_stack;
/*     */   }
/*     */ 
/*     */   private final int getInternalLength()
/*     */   {
/* 263 */     return 8 + this.code_length + 2 + 8 * this.exception_table_length + 2;
/*     */   }
/*     */ 
/*     */   private final int calculateLength()
/*     */   {
/* 275 */     int len = 0;
/*     */ 
/* 277 */     for (int i = 0; i < this.attributes_count; i++) {
/* 278 */       len += this.attributes[i].length + 6;
/*     */     }
/* 280 */     return len + getInternalLength();
/*     */   }
/*     */ 
/*     */   public final void setAttributes(Attribute[] attributes)
/*     */   {
/* 287 */     this.attributes = attributes;
/* 288 */     this.attributes_count = (attributes == null ? 0 : attributes.length);
/* 289 */     this.length = calculateLength();
/*     */   }
/*     */ 
/*     */   public final void setCode(byte[] code)
/*     */   {
/* 296 */     this.code = code;
/* 297 */     this.code_length = (code == null ? 0 : code.length);
/*     */   }
/*     */ 
/*     */   public final void setExceptionTable(CodeException[] exception_table)
/*     */   {
/* 304 */     this.exception_table = exception_table;
/* 305 */     this.exception_table_length = (exception_table == null ? 0 : exception_table.length);
/*     */   }
/*     */ 
/*     */   public final void setMaxLocals(int max_locals)
/*     */   {
/* 313 */     this.max_locals = max_locals;
/*     */   }
/*     */ 
/*     */   public final void setMaxStack(int max_stack)
/*     */   {
/* 320 */     this.max_stack = max_stack;
/*     */   }
/*     */ 
/*     */   public final String toString(boolean verbose)
/*     */   {
/* 329 */     StringBuffer buf = new StringBuffer("Code(max_stack = " + this.max_stack + ", max_locals = " + this.max_locals + ", code_length = " + this.code_length + ")\n" + Utility.codeToString(this.code, this.constant_pool, 0, -1, verbose));
/*     */ 
/* 334 */     if (this.exception_table_length > 0) {
/* 335 */       buf.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");
/*     */ 
/* 337 */       for (int i = 0; i < this.exception_table_length; i++) {
/* 338 */         buf.append(this.exception_table[i].toString(this.constant_pool, verbose) + "\n");
/*     */       }
/*     */     }
/* 341 */     if (this.attributes_count > 0) {
/* 342 */       buf.append("\nAttribute(s) = \n");
/*     */ 
/* 344 */       for (int i = 0; i < this.attributes_count; i++) {
/* 345 */         buf.append(this.attributes[i].toString() + "\n");
/*     */       }
/*     */     }
/* 348 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 355 */     return toString(true);
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 362 */     Code c = (Code)clone();
/* 363 */     c.code = ((byte[])this.code.clone());
/* 364 */     c.constant_pool = constant_pool;
/*     */ 
/* 366 */     c.exception_table = new CodeException[this.exception_table_length];
/* 367 */     for (int i = 0; i < this.exception_table_length; i++) {
/* 368 */       c.exception_table[i] = this.exception_table[i].copy();
/*     */     }
/* 370 */     c.attributes = new Attribute[this.attributes_count];
/* 371 */     for (int i = 0; i < this.attributes_count; i++) {
/* 372 */       c.attributes[i] = this.attributes[i].copy(constant_pool);
/*     */     }
/* 374 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Code
 * JD-Core Version:    0.6.2
 */