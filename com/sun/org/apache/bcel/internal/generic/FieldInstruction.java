/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ 
/*     */ public abstract class FieldInstruction extends FieldOrMethod
/*     */   implements TypedInstruction
/*     */ {
/*     */   FieldInstruction()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected FieldInstruction(short opcode, int index)
/*     */   {
/*  84 */     super(opcode, index);
/*     */   }
/*     */ 
/*     */   public String toString(ConstantPool cp)
/*     */   {
/*  91 */     return com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[this.opcode] + " " + cp.constantToString(this.index, (byte)9);
/*     */   }
/*     */ 
/*     */   protected int getFieldSize(ConstantPoolGen cpg)
/*     */   {
/*  98 */     return getType(cpg).getSize();
/*     */   }
/*     */ 
/*     */   public Type getType(ConstantPoolGen cpg)
/*     */   {
/* 104 */     return getFieldType(cpg);
/*     */   }
/*     */ 
/*     */   public Type getFieldType(ConstantPoolGen cpg)
/*     */   {
/* 110 */     return Type.getType(getSignature(cpg));
/*     */   }
/*     */ 
/*     */   public String getFieldName(ConstantPoolGen cpg)
/*     */   {
/* 116 */     return getName(cpg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FieldInstruction
 * JD-Core Version:    0.6.2
 */