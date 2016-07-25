/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class StackMapEntry
/*     */   implements Cloneable
/*     */ {
/*     */   private int byte_code_offset;
/*     */   private int number_of_locals;
/*     */   private StackMapType[] types_of_locals;
/*     */   private int number_of_stack_items;
/*     */   private StackMapType[] types_of_stack_items;
/*     */   private ConstantPool constant_pool;
/*     */ 
/*     */   StackMapEntry(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  88 */     this(file.readShort(), file.readShort(), null, -1, null, constant_pool);
/*     */ 
/*  90 */     this.types_of_locals = new StackMapType[this.number_of_locals];
/*  91 */     for (int i = 0; i < this.number_of_locals; i++) {
/*  92 */       this.types_of_locals[i] = new StackMapType(file, constant_pool);
/*     */     }
/*  94 */     this.number_of_stack_items = file.readShort();
/*  95 */     this.types_of_stack_items = new StackMapType[this.number_of_stack_items];
/*  96 */     for (int i = 0; i < this.number_of_stack_items; i++)
/*  97 */       this.types_of_stack_items[i] = new StackMapType(file, constant_pool);
/*     */   }
/*     */ 
/*     */   public StackMapEntry(int byte_code_offset, int number_of_locals, StackMapType[] types_of_locals, int number_of_stack_items, StackMapType[] types_of_stack_items, ConstantPool constant_pool)
/*     */   {
/* 105 */     this.byte_code_offset = byte_code_offset;
/* 106 */     this.number_of_locals = number_of_locals;
/* 107 */     this.types_of_locals = types_of_locals;
/* 108 */     this.number_of_stack_items = number_of_stack_items;
/* 109 */     this.types_of_stack_items = types_of_stack_items;
/* 110 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 121 */     file.writeShort(this.byte_code_offset);
/*     */ 
/* 123 */     file.writeShort(this.number_of_locals);
/* 124 */     for (int i = 0; i < this.number_of_locals; i++) {
/* 125 */       this.types_of_locals[i].dump(file);
/*     */     }
/* 127 */     file.writeShort(this.number_of_stack_items);
/* 128 */     for (int i = 0; i < this.number_of_stack_items; i++)
/* 129 */       this.types_of_stack_items[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 136 */     StringBuffer buf = new StringBuffer("(offset=" + this.byte_code_offset);
/*     */ 
/* 138 */     if (this.number_of_locals > 0) {
/* 139 */       buf.append(", locals={");
/*     */ 
/* 141 */       for (int i = 0; i < this.number_of_locals; i++) {
/* 142 */         buf.append(this.types_of_locals[i]);
/* 143 */         if (i < this.number_of_locals - 1) {
/* 144 */           buf.append(", ");
/*     */         }
/*     */       }
/* 147 */       buf.append("}");
/*     */     }
/*     */ 
/* 150 */     if (this.number_of_stack_items > 0) {
/* 151 */       buf.append(", stack items={");
/*     */ 
/* 153 */       for (int i = 0; i < this.number_of_stack_items; i++) {
/* 154 */         buf.append(this.types_of_stack_items[i]);
/* 155 */         if (i < this.number_of_stack_items - 1) {
/* 156 */           buf.append(", ");
/*     */         }
/*     */       }
/* 159 */       buf.append("}");
/*     */     }
/*     */ 
/* 162 */     buf.append(")");
/*     */ 
/* 164 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public void setByteCodeOffset(int b) {
/* 168 */     this.byte_code_offset = b; } 
/* 169 */   public int getByteCodeOffset() { return this.byte_code_offset; } 
/* 170 */   public void setNumberOfLocals(int n) { this.number_of_locals = n; } 
/* 171 */   public int getNumberOfLocals() { return this.number_of_locals; } 
/* 172 */   public void setTypesOfLocals(StackMapType[] t) { this.types_of_locals = t; } 
/* 173 */   public StackMapType[] getTypesOfLocals() { return this.types_of_locals; } 
/* 174 */   public void setNumberOfStackItems(int n) { this.number_of_stack_items = n; } 
/* 175 */   public int getNumberOfStackItems() { return this.number_of_stack_items; } 
/* 176 */   public void setTypesOfStackItems(StackMapType[] t) { this.types_of_stack_items = t; } 
/* 177 */   public StackMapType[] getTypesOfStackItems() { return this.types_of_stack_items; }
/*     */ 
/*     */ 
/*     */   public StackMapEntry copy()
/*     */   {
/*     */     try
/*     */     {
/* 184 */       return (StackMapEntry)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 187 */     return null;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 198 */     v.visitStackMapEntry(this);
/*     */   }
/*     */ 
/*     */   public final ConstantPool getConstantPool()
/*     */   {
/* 204 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public final void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 210 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.StackMapEntry
 * JD-Core Version:    0.6.2
 */