/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantValue extends Attribute
/*     */ {
/*     */   private int constantvalue_index;
/*     */ 
/*     */   public ConstantValue(ConstantValue c)
/*     */   {
/*  80 */     this(c.getNameIndex(), c.getLength(), c.getConstantValueIndex(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   ConstantValue(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  95 */     this(name_index, length, file.readUnsignedShort(), constant_pool);
/*     */   }
/*     */ 
/*     */   public ConstantValue(int name_index, int length, int constantvalue_index, ConstantPool constant_pool)
/*     */   {
/* 108 */     super((byte)1, name_index, length, constant_pool);
/* 109 */     this.constantvalue_index = constantvalue_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 120 */     v.visitConstantValue(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 130 */     super.dump(file);
/* 131 */     file.writeShort(this.constantvalue_index);
/*     */   }
/*     */ 
/*     */   public final int getConstantValueIndex()
/*     */   {
/* 136 */     return this.constantvalue_index;
/*     */   }
/*     */ 
/*     */   public final void setConstantValueIndex(int constantvalue_index)
/*     */   {
/* 142 */     this.constantvalue_index = constantvalue_index;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 149 */     Constant c = this.constant_pool.getConstant(this.constantvalue_index);
/*     */     String buf;
/* 155 */     switch (c.getTag()) { case 5:
/* 156 */       buf = "" + ((ConstantLong)c).getBytes(); break;
/*     */     case 4:
/* 157 */       buf = "" + ((ConstantFloat)c).getBytes(); break;
/*     */     case 6:
/* 158 */       buf = "" + ((ConstantDouble)c).getBytes(); break;
/*     */     case 3:
/* 159 */       buf = "" + ((ConstantInteger)c).getBytes(); break;
/*     */     case 8:
/* 161 */       int i = ((ConstantString)c).getStringIndex();
/* 162 */       c = this.constant_pool.getConstant(i, (byte)1);
/* 163 */       buf = "\"" + Utility.convertString(((ConstantUtf8)c).getBytes()) + "\"";
/* 164 */       break;
/*     */     case 7:
/*     */     default:
/* 167 */       throw new IllegalStateException("Type of ConstValue invalid: " + c);
/*     */     }
/*     */ 
/* 170 */     return buf;
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 177 */     ConstantValue c = (ConstantValue)clone();
/* 178 */     c.constant_pool = constant_pool;
/* 179 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantValue
 * JD-Core Version:    0.6.2
 */