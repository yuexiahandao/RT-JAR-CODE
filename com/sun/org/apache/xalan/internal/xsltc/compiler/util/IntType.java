/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFGE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFGT;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPGE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPGT;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPLE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class IntType extends NumberType
/*     */ {
/*     */   public String toString()
/*     */   {
/*  60 */     return "int";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  64 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  68 */     return "I";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  72 */     return com.sun.org.apache.bcel.internal.generic.Type.INT;
/*     */   }
/*     */ 
/*     */   public int distanceTo(Type type)
/*     */   {
/*  79 */     if (type == this) {
/*  80 */       return 0;
/*     */     }
/*  82 */     if (type == Type.Real) {
/*  83 */       return 1;
/*     */     }
/*     */ 
/*  86 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  96 */     if (type == Type.Real) {
/*  97 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/*  99 */     else if (type == Type.String) {
/* 100 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/* 102 */     else if (type == Type.Boolean) {
/* 103 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/* 105 */     else if (type == Type.Reference) {
/* 106 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/*     */     else {
/* 109 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 111 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 122 */     methodGen.getInstructionList().append(I2D);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 133 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 134 */     InstructionList il = methodGen.getInstructionList();
/* 135 */     il.append(new INVOKESTATIC(cpg.addMethodref("java.lang.Integer", "toString", "(I)Ljava/lang/String;")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 148 */     InstructionList il = methodGen.getInstructionList();
/* 149 */     BranchHandle falsec = il.append(new IFEQ(null));
/* 150 */     il.append(ICONST_1);
/* 151 */     BranchHandle truec = il.append(new GOTO(null));
/* 152 */     falsec.setTarget(il.append(ICONST_0));
/* 153 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 166 */     InstructionList il = methodGen.getInstructionList();
/* 167 */     return new FlowList(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 179 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 180 */     InstructionList il = methodGen.getInstructionList();
/* 181 */     il.append(new NEW(cpg.addClass("java.lang.Integer")));
/* 182 */     il.append(DUP_X1);
/* 183 */     il.append(SWAP);
/* 184 */     il.append(new INVOKESPECIAL(cpg.addMethodref("java.lang.Integer", "<init>", "(I)V")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 195 */     InstructionList il = methodGen.getInstructionList();
/* 196 */     if (clazz == Character.TYPE) {
/* 197 */       il.append(I2C);
/*     */     }
/* 199 */     else if (clazz == Byte.TYPE) {
/* 200 */       il.append(I2B);
/*     */     }
/* 202 */     else if (clazz == Short.TYPE) {
/* 203 */       il.append(I2S);
/*     */     }
/* 205 */     else if (clazz == Integer.TYPE) {
/* 206 */       il.append(NOP);
/*     */     }
/* 208 */     else if (clazz == Long.TYPE) {
/* 209 */       il.append(I2L);
/*     */     }
/* 211 */     else if (clazz == Float.TYPE) {
/* 212 */       il.append(I2F);
/*     */     }
/* 214 */     else if (clazz == Double.TYPE) {
/* 215 */       il.append(I2D);
/*     */     }
/* 218 */     else if (clazz.isAssignableFrom(Double.class)) {
/* 219 */       il.append(I2D);
/* 220 */       Type.Real.translateTo(classGen, methodGen, Type.Reference);
/*     */     }
/*     */     else {
/* 223 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 225 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 234 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 242 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 243 */     InstructionList il = methodGen.getInstructionList();
/* 244 */     il.append(new CHECKCAST(cpg.addClass("java.lang.Integer")));
/* 245 */     int index = cpg.addMethodref("java.lang.Integer", "intValue", "()I");
/*     */ 
/* 248 */     il.append(new INVOKEVIRTUAL(index));
/*     */   }
/*     */ 
/*     */   public Instruction ADD() {
/* 252 */     return InstructionConstants.IADD;
/*     */   }
/*     */ 
/*     */   public Instruction SUB() {
/* 256 */     return InstructionConstants.ISUB;
/*     */   }
/*     */ 
/*     */   public Instruction MUL() {
/* 260 */     return InstructionConstants.IMUL;
/*     */   }
/*     */ 
/*     */   public Instruction DIV() {
/* 264 */     return InstructionConstants.IDIV;
/*     */   }
/*     */ 
/*     */   public Instruction REM() {
/* 268 */     return InstructionConstants.IREM;
/*     */   }
/*     */ 
/*     */   public Instruction NEG() {
/* 272 */     return InstructionConstants.INEG;
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot) {
/* 276 */     return new ILOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 280 */     return new ISTORE(slot);
/*     */   }
/*     */ 
/*     */   public BranchInstruction GT(boolean tozero) {
/* 284 */     return tozero ? new IFGT(null) : new IF_ICMPGT(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction GE(boolean tozero)
/*     */   {
/* 289 */     return tozero ? new IFGE(null) : new IF_ICMPGE(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction LT(boolean tozero)
/*     */   {
/* 294 */     return tozero ? new IFLT(null) : new IF_ICMPLT(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction LE(boolean tozero)
/*     */   {
/* 299 */     return tozero ? new IFLE(null) : new IF_ICMPLE(null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType
 * JD-Core Version:    0.6.2
 */