/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class InnerClasses extends Attribute
/*     */ {
/*     */   private InnerClass[] inner_classes;
/*     */   private int number_of_classes;
/*     */ 
/*     */   public InnerClasses(InnerClasses c)
/*     */   {
/*  82 */     this(c.getNameIndex(), c.getLength(), c.getInnerClasses(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   public InnerClasses(int name_index, int length, InnerClass[] inner_classes, ConstantPool constant_pool)
/*     */   {
/*  97 */     super((byte)6, name_index, length, constant_pool);
/*  98 */     setInnerClasses(inner_classes);
/*     */   }
/*     */ 
/*     */   InnerClasses(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/* 113 */     this(name_index, length, (InnerClass[])null, constant_pool);
/*     */ 
/* 115 */     this.number_of_classes = file.readUnsignedShort();
/* 116 */     this.inner_classes = new InnerClass[this.number_of_classes];
/*     */ 
/* 118 */     for (int i = 0; i < this.number_of_classes; i++)
/* 119 */       this.inner_classes[i] = new InnerClass(file);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 129 */     v.visitInnerClasses(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 139 */     super.dump(file);
/* 140 */     file.writeShort(this.number_of_classes);
/*     */ 
/* 142 */     for (int i = 0; i < this.number_of_classes; i++)
/* 143 */       this.inner_classes[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final InnerClass[] getInnerClasses()
/*     */   {
/* 149 */     return this.inner_classes;
/*     */   }
/*     */ 
/*     */   public final void setInnerClasses(InnerClass[] inner_classes)
/*     */   {
/* 155 */     this.inner_classes = inner_classes;
/* 156 */     this.number_of_classes = (inner_classes == null ? 0 : inner_classes.length);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 163 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 165 */     for (int i = 0; i < this.number_of_classes; i++) {
/* 166 */       buf.append(this.inner_classes[i].toString(this.constant_pool) + "\n");
/*     */     }
/* 168 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 175 */     InnerClasses c = (InnerClasses)clone();
/*     */ 
/* 177 */     c.inner_classes = new InnerClass[this.number_of_classes];
/* 178 */     for (int i = 0; i < this.number_of_classes; i++) {
/* 179 */       c.inner_classes[i] = this.inner_classes[i].copy();
/*     */     }
/* 181 */     c.constant_pool = constant_pool;
/* 182 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.InnerClasses
 * JD-Core Version:    0.6.2
 */