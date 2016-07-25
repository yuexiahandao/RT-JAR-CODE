/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantCP;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
/*     */ 
/*     */ public abstract class FieldOrMethod extends CPInstruction
/*     */   implements LoadClass
/*     */ {
/*     */   FieldOrMethod()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected FieldOrMethod(short opcode, int index)
/*     */   {
/*  79 */     super(opcode, index);
/*     */   }
/*     */ 
/*     */   public String getSignature(ConstantPoolGen cpg)
/*     */   {
/*  85 */     ConstantPool cp = cpg.getConstantPool();
/*  86 */     ConstantCP cmr = (ConstantCP)cp.getConstant(this.index);
/*  87 */     ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());
/*     */ 
/*  89 */     return ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
/*     */   }
/*     */ 
/*     */   public String getName(ConstantPoolGen cpg)
/*     */   {
/*  95 */     ConstantPool cp = cpg.getConstantPool();
/*  96 */     ConstantCP cmr = (ConstantCP)cp.getConstant(this.index);
/*  97 */     ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());
/*  98 */     return ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
/*     */   }
/*     */ 
/*     */   public String getClassName(ConstantPoolGen cpg)
/*     */   {
/* 104 */     ConstantPool cp = cpg.getConstantPool();
/* 105 */     ConstantCP cmr = (ConstantCP)cp.getConstant(this.index);
/* 106 */     return cp.getConstantString(cmr.getClassIndex(), (byte)7).replace('/', '.');
/*     */   }
/*     */ 
/*     */   public ObjectType getClassType(ConstantPoolGen cpg)
/*     */   {
/* 112 */     return new ObjectType(getClassName(cpg));
/*     */   }
/*     */ 
/*     */   public ObjectType getLoadClassType(ConstantPoolGen cpg)
/*     */   {
/* 118 */     return getClassType(cpg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FieldOrMethod
 * JD-Core Version:    0.6.2
 */