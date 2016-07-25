/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Constant
/*     */   implements Cloneable, Node, Serializable
/*     */ {
/*     */   protected byte tag;
/*     */ 
/*     */   Constant(byte tag)
/*     */   {
/*  82 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */   public abstract void accept(Visitor paramVisitor);
/*     */ 
/*     */   public abstract void dump(DataOutputStream paramDataOutputStream)
/*     */     throws IOException;
/*     */ 
/*     */   public final byte getTag()
/*     */   {
/*  99 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     return com.sun.org.apache.bcel.internal.Constants.CONSTANT_NAMES[this.tag] + "[" + this.tag + "]";
/*     */   }
/*     */ 
/*     */   public Constant copy()
/*     */   {
/*     */     try
/*     */     {
/* 113 */       return (Constant)super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   public Object clone() throws CloneNotSupportedException {
/* 120 */     return super.clone();
/*     */   }
/*     */ 
/*     */   static final Constant readConstant(DataInputStream file)
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 132 */     byte b = file.readByte();
/*     */ 
/* 134 */     switch (b) { case 7:
/* 135 */       return new ConstantClass(file);
/*     */     case 9:
/* 136 */       return new ConstantFieldref(file);
/*     */     case 10:
/* 137 */       return new ConstantMethodref(file);
/*     */     case 11:
/* 138 */       return new ConstantInterfaceMethodref(file);
/*     */     case 8:
/* 140 */       return new ConstantString(file);
/*     */     case 3:
/* 141 */       return new ConstantInteger(file);
/*     */     case 4:
/* 142 */       return new ConstantFloat(file);
/*     */     case 5:
/* 143 */       return new ConstantLong(file);
/*     */     case 6:
/* 144 */       return new ConstantDouble(file);
/*     */     case 12:
/* 145 */       return new ConstantNameAndType(file);
/*     */     case 1:
/* 146 */       return new ConstantUtf8(file);
/*     */     case 2: }
/* 148 */     throw new ClassFormatException("Invalid byte tag in constant pool: " + b);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Constant
 * JD-Core Version:    0.6.2
 */