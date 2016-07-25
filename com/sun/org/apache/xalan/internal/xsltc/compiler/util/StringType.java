/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public class StringType extends Type
/*     */ {
/*     */   public String toString()
/*     */   {
/*  49 */     return "string";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  53 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  57 */     return "Ljava/lang/String;";
/*     */   }
/*     */ 
/*     */   public boolean isSimple() {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  65 */     return com.sun.org.apache.bcel.internal.generic.Type.STRING;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  77 */     if (type == Type.Boolean) {
/*  78 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/*  80 */     else if (type == Type.Real) {
/*  81 */       translateTo(classGen, methodGen, (RealType)type);
/*     */     }
/*  83 */     else if (type == Type.Reference) {
/*  84 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/*  86 */     else if (type != Type.ObjectString)
/*     */     {
/*  90 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/*  92 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 103 */     InstructionList il = methodGen.getInstructionList();
/* 104 */     FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
/* 105 */     il.append(ICONST_1);
/* 106 */     BranchHandle truec = il.append(new GOTO(null));
/* 107 */     falsel.backPatch(il.append(ICONST_0));
/* 108 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
/*     */   {
/* 119 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 120 */     InstructionList il = methodGen.getInstructionList();
/* 121 */     il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "stringToReal", "(Ljava/lang/String;)D")));
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 136 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 137 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 139 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "length", "()I")));
/*     */ 
/* 141 */     return new FlowList(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 152 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 164 */     if (clazz.isAssignableFrom(String.class)) {
/* 165 */       methodGen.getInstructionList().append(NOP);
/*     */     }
/*     */     else {
/* 168 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 170 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 182 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 183 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 185 */     if (clazz.getName().equals("java.lang.String"))
/*     */     {
/* 187 */       il.append(DUP);
/* 188 */       BranchHandle ifNonNull = il.append(new IFNONNULL(null));
/* 189 */       il.append(POP);
/* 190 */       il.append(new PUSH(cpg, ""));
/* 191 */       ifNonNull.setTarget(il.append(NOP));
/*     */     }
/*     */     else {
/* 194 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 196 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 205 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 213 */     methodGen.getInstructionList().append(NOP);
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 220 */     return "java.lang.String";
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot)
/*     */   {
/* 225 */     return new ALOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 229 */     return new ASTORE(slot);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType
 * JD-Core Version:    0.6.2
 */