/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class ConstantFieldref extends ConstantCP
/*     */ {
/*     */   public ConstantFieldref(ConstantFieldref c)
/*     */   {
/*  74 */     super((byte)9, c.getClassIndex(), c.getNameAndTypeIndex());
/*     */   }
/*     */ 
/*     */   ConstantFieldref(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  85 */     super((byte)9, file);
/*     */   }
/*     */ 
/*     */   public ConstantFieldref(int class_index, int name_and_type_index)
/*     */   {
/*  94 */     super((byte)9, class_index, name_and_type_index);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 105 */     v.visitConstantFieldref(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantFieldref
 * JD-Core Version:    0.6.2
 */