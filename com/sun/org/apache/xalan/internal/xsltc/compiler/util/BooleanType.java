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
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class BooleanType extends Type
/*     */ {
/*     */   public String toString()
/*     */   {
/*  58 */     return "boolean";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  62 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  66 */     return "Z";
/*     */   }
/*     */ 
/*     */   public boolean isSimple() {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  74 */     return com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  86 */     if (type == Type.String) {
/*  87 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  89 */     else if (type == Type.Real) {
/*  90 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/*  92 */     else if (type == Type.Reference) {
/*  93 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/*     */     else {
/*  96 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/*  98 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 111 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 112 */     InstructionList il = methodGen.getInstructionList();
/* 113 */     BranchHandle falsec = il.append(new IFEQ(null));
/* 114 */     il.append(new PUSH(cpg, "true"));
/* 115 */     BranchHandle truec = il.append(new GOTO(null));
/* 116 */     falsec.setTarget(il.append(new PUSH(cpg, "false")));
/* 117 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 128 */     methodGen.getInstructionList().append(I2D);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 140 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 141 */     InstructionList il = methodGen.getInstructionList();
/* 142 */     il.append(new NEW(cpg.addClass("java.lang.Boolean")));
/* 143 */     il.append(DUP_X1);
/* 144 */     il.append(SWAP);
/* 145 */     il.append(new INVOKESPECIAL(cpg.addMethodref("java.lang.Boolean", "<init>", "(Z)V")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 155 */     if (clazz == Boolean.TYPE) {
/* 156 */       methodGen.getInstructionList().append(NOP);
/*     */     }
/* 159 */     else if (clazz.isAssignableFrom(Boolean.class)) {
/* 160 */       translateTo(classGen, methodGen, Type.Reference);
/*     */     }
/*     */     else {
/* 163 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 165 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 174 */     translateTo(classGen, methodGen, clazz);
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 182 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 190 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 191 */     InstructionList il = methodGen.getInstructionList();
/* 192 */     il.append(new CHECKCAST(cpg.addClass("java.lang.Boolean")));
/* 193 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.Boolean", "booleanValue", "()Z")));
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot)
/*     */   {
/* 199 */     return new ILOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 203 */     return new ISTORE(slot);
/*     */   }
/*     */ 
/*     */   public BranchInstruction GT(boolean tozero) {
/* 207 */     return tozero ? new IFGT(null) : new IF_ICMPGT(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction GE(boolean tozero)
/*     */   {
/* 212 */     return tozero ? new IFGE(null) : new IF_ICMPGE(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction LT(boolean tozero)
/*     */   {
/* 217 */     return tozero ? new IFLT(null) : new IF_ICMPLT(null);
/*     */   }
/*     */ 
/*     */   public BranchInstruction LE(boolean tozero)
/*     */   {
/* 222 */     return tozero ? new IFLE(null) : new IF_ICMPLE(null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType
 * JD-Core Version:    0.6.2
 */