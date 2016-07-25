/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class PMGClass extends Attribute
/*     */ {
/*     */   private int pmg_class_index;
/*     */   private int pmg_index;
/*     */ 
/*     */   public PMGClass(PMGClass c)
/*     */   {
/*  80 */     this(c.getNameIndex(), c.getLength(), c.getPMGIndex(), c.getPMGClassIndex(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   PMGClass(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  95 */     this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
/*     */   }
/*     */ 
/*     */   public PMGClass(int name_index, int length, int pmg_index, int pmg_class_index, ConstantPool constant_pool)
/*     */   {
/* 108 */     super((byte)9, name_index, length, constant_pool);
/* 109 */     this.pmg_index = pmg_index;
/* 110 */     this.pmg_class_index = pmg_class_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 121 */     System.err.println("Visiting non-standard PMGClass object");
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 132 */     super.dump(file);
/* 133 */     file.writeShort(this.pmg_index);
/* 134 */     file.writeShort(this.pmg_class_index);
/*     */   }
/*     */ 
/*     */   public final int getPMGClassIndex()
/*     */   {
/* 140 */     return this.pmg_class_index;
/*     */   }
/*     */ 
/*     */   public final void setPMGClassIndex(int pmg_class_index)
/*     */   {
/* 146 */     this.pmg_class_index = pmg_class_index;
/*     */   }
/*     */ 
/*     */   public final int getPMGIndex()
/*     */   {
/* 152 */     return this.pmg_index;
/*     */   }
/*     */ 
/*     */   public final void setPMGIndex(int pmg_index)
/*     */   {
/* 158 */     this.pmg_index = pmg_index;
/*     */   }
/*     */ 
/*     */   public final String getPMGName()
/*     */   {
/* 165 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.pmg_index, (byte)1);
/*     */ 
/* 167 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   public final String getPMGClassName()
/*     */   {
/* 174 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.pmg_class_index, (byte)1);
/*     */ 
/* 176 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 183 */     return "PMGClass(" + getPMGName() + ", " + getPMGClassName() + ")";
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 190 */     return (PMGClass)clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.PMGClass
 * JD-Core Version:    0.6.2
 */