/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantMethodref extends ConstantCP
/*     */ {
/*     */   public ConstantMethodref(ConstantMethodref c)
/*     */   {
/*  74 */     super((byte)10, c.getClassIndex(), c.getNameAndTypeIndex());
/*     */   }
/*     */ 
/*     */   ConstantMethodref(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  85 */     super((byte)10, file);
/*     */   }
/*     */ 
/*     */   public ConstantMethodref(int class_index, int name_and_type_index)
/*     */   {
/*  94 */     super((byte)10, class_index, name_and_type_index);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 105 */     v.visitConstantMethodref(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantMethodref
 * JD-Core Version:    0.6.2
 */