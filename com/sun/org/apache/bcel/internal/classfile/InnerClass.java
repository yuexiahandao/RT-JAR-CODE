/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class InnerClass
/*     */   implements Cloneable, Node
/*     */ {
/*     */   private int inner_class_index;
/*     */   private int outer_class_index;
/*     */   private int inner_name_index;
/*     */   private int inner_access_flags;
/*     */ 
/*     */   public InnerClass(InnerClass c)
/*     */   {
/*  82 */     this(c.getInnerClassIndex(), c.getOuterClassIndex(), c.getInnerNameIndex(), c.getInnerAccessFlags());
/*     */   }
/*     */ 
/*     */   InnerClass(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  93 */     this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public InnerClass(int inner_class_index, int outer_class_index, int inner_name_index, int inner_access_flags)
/*     */   {
/* 106 */     this.inner_class_index = inner_class_index;
/* 107 */     this.outer_class_index = outer_class_index;
/* 108 */     this.inner_name_index = inner_name_index;
/* 109 */     this.inner_access_flags = inner_access_flags;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 120 */     v.visitInnerClass(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 130 */     file.writeShort(this.inner_class_index);
/* 131 */     file.writeShort(this.outer_class_index);
/* 132 */     file.writeShort(this.inner_name_index);
/* 133 */     file.writeShort(this.inner_access_flags);
/*     */   }
/*     */ 
/*     */   public final int getInnerAccessFlags()
/*     */   {
/* 138 */     return this.inner_access_flags;
/*     */   }
/*     */ 
/*     */   public final int getInnerClassIndex() {
/* 142 */     return this.inner_class_index;
/*     */   }
/*     */ 
/*     */   public final int getInnerNameIndex() {
/* 146 */     return this.inner_name_index;
/*     */   }
/*     */ 
/*     */   public final int getOuterClassIndex() {
/* 150 */     return this.outer_class_index;
/*     */   }
/*     */ 
/*     */   public final void setInnerAccessFlags(int inner_access_flags)
/*     */   {
/* 155 */     this.inner_access_flags = inner_access_flags;
/*     */   }
/*     */ 
/*     */   public final void setInnerClassIndex(int inner_class_index)
/*     */   {
/* 161 */     this.inner_class_index = inner_class_index;
/*     */   }
/*     */ 
/*     */   public final void setInnerNameIndex(int inner_name_index)
/*     */   {
/* 167 */     this.inner_name_index = inner_name_index;
/*     */   }
/*     */ 
/*     */   public final void setOuterClassIndex(int outer_class_index)
/*     */   {
/* 173 */     this.outer_class_index = outer_class_index;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 179 */     return "InnerClass(" + this.inner_class_index + ", " + this.outer_class_index + ", " + this.inner_name_index + ", " + this.inner_access_flags + ")";
/*     */   }
/*     */ 
/*     */   public final String toString(ConstantPool constant_pool)
/*     */   {
/* 189 */     String inner_class_name = constant_pool.getConstantString(this.inner_class_index, (byte)7);
/*     */ 
/* 191 */     inner_class_name = Utility.compactClassName(inner_class_name);
/*     */     String outer_class_name;
/* 193 */     if (this.outer_class_index != 0) {
/* 194 */       String outer_class_name = constant_pool.getConstantString(this.outer_class_index, (byte)7);
/*     */ 
/* 196 */       outer_class_name = Utility.compactClassName(outer_class_name);
/*     */     }
/*     */     else {
/* 199 */       outer_class_name = "<not a member>";
/*     */     }
/*     */     String inner_name;
/*     */     String inner_name;
/* 201 */     if (this.inner_name_index != 0) {
/* 202 */       inner_name = ((ConstantUtf8)constant_pool.getConstant(this.inner_name_index, (byte)1)).getBytes();
/*     */     }
/*     */     else {
/* 205 */       inner_name = "<anonymous>";
/*     */     }
/* 207 */     String access = Utility.accessToString(this.inner_access_flags, true);
/* 208 */     access = access + " ";
/*     */ 
/* 210 */     return "InnerClass:" + access + inner_class_name + "(\"" + outer_class_name + "\", \"" + inner_name + "\")";
/*     */   }
/*     */ 
/*     */   public InnerClass copy()
/*     */   {
/*     */     try
/*     */     {
/* 219 */       return (InnerClass)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.InnerClass
 * JD-Core Version:    0.6.2
 */