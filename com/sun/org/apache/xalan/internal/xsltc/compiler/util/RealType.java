/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.DLOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.DSTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ 
/*     */ public final class RealType extends NumberType
/*     */ {
/*     */   public String toString()
/*     */   {
/*  53 */     return "real";
/*     */   }
/*     */ 
/*     */   public boolean identicalTo(Type other) {
/*  57 */     return this == other;
/*     */   }
/*     */ 
/*     */   public String toSignature() {
/*  61 */     return "D";
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
/*  65 */     return com.sun.org.apache.bcel.internal.generic.Type.DOUBLE;
/*     */   }
/*     */ 
/*     */   public int distanceTo(Type type)
/*     */   {
/*  72 */     if (type == this) {
/*  73 */       return 0;
/*     */     }
/*  75 */     if (type == Type.Int) {
/*  76 */       return 1;
/*     */     }
/*     */ 
/*  79 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
/*     */   {
/*  91 */     if (type == Type.String) {
/*  92 */       translateTo(classGen, methodGen, (StringType)type);
/*     */     }
/*  94 */     else if (type == Type.Boolean) {
/*  95 */       translateTo(classGen, methodGen, (BooleanType)type);
/*     */     }
/*  97 */     else if (type == Type.Reference) {
/*  98 */       translateTo(classGen, methodGen, (ReferenceType)type);
/*     */     }
/* 100 */     else if (type == Type.Int) {
/* 101 */       translateTo(classGen, methodGen, (IntType)type);
/*     */     }
/*     */     else {
/* 104 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
/*     */ 
/* 106 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
/*     */   {
/* 118 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 119 */     InstructionList il = methodGen.getInstructionList();
/* 120 */     il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "realToString", "(D)Ljava/lang/String;")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 133 */     InstructionList il = methodGen.getInstructionList();
/* 134 */     FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
/* 135 */     il.append(ICONST_1);
/* 136 */     BranchHandle truec = il.append(new GOTO(null));
/* 137 */     falsel.backPatch(il.append(ICONST_0));
/* 138 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, IntType type)
/*     */   {
/* 148 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 149 */     InstructionList il = methodGen.getInstructionList();
/* 150 */     il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "realToInt", "(D)I")));
/*     */   }
/*     */ 
/*     */   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
/*     */   {
/* 165 */     FlowList flowlist = new FlowList();
/* 166 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 167 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 170 */     il.append(DUP2);
/* 171 */     LocalVariableGen local = methodGen.addLocalVariable("real_to_boolean_tmp", com.sun.org.apache.bcel.internal.generic.Type.DOUBLE, null, null);
/*     */ 
/* 174 */     local.setStart(il.append(new DSTORE(local.getIndex())));
/*     */ 
/* 177 */     il.append(DCONST_0);
/* 178 */     il.append(DCMPG);
/* 179 */     flowlist.add(il.append(new IFEQ(null)));
/*     */ 
/* 183 */     il.append(new DLOAD(local.getIndex()));
/* 184 */     local.setEnd(il.append(new DLOAD(local.getIndex())));
/* 185 */     il.append(DCMPG);
/* 186 */     flowlist.add(il.append(new IFNE(null)));
/* 187 */     return flowlist;
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
/*     */   {
/* 198 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 199 */     InstructionList il = methodGen.getInstructionList();
/* 200 */     il.append(new NEW(cpg.addClass("java.lang.Double")));
/* 201 */     il.append(DUP_X2);
/* 202 */     il.append(DUP_X2);
/* 203 */     il.append(POP);
/* 204 */     il.append(new INVOKESPECIAL(cpg.addMethodref("java.lang.Double", "<init>", "(D)V")));
/*     */   }
/*     */ 
/*     */   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 215 */     InstructionList il = methodGen.getInstructionList();
/* 216 */     if (clazz == Character.TYPE) {
/* 217 */       il.append(D2I);
/* 218 */       il.append(I2C);
/*     */     }
/* 220 */     else if (clazz == Byte.TYPE) {
/* 221 */       il.append(D2I);
/* 222 */       il.append(I2B);
/*     */     }
/* 224 */     else if (clazz == Short.TYPE) {
/* 225 */       il.append(D2I);
/* 226 */       il.append(I2S);
/*     */     }
/* 228 */     else if (clazz == Integer.TYPE) {
/* 229 */       il.append(D2I);
/*     */     }
/* 231 */     else if (clazz == Long.TYPE) {
/* 232 */       il.append(D2L);
/*     */     }
/* 234 */     else if (clazz == Float.TYPE) {
/* 235 */       il.append(D2F);
/*     */     }
/* 237 */     else if (clazz == Double.TYPE) {
/* 238 */       il.append(NOP);
/*     */     }
/* 241 */     else if (clazz.isAssignableFrom(Double.class)) {
/* 242 */       translateTo(classGen, methodGen, Type.Reference);
/*     */     }
/*     */     else {
/* 245 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 247 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
/*     */   {
/* 257 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 259 */     if ((clazz == Character.TYPE) || (clazz == Byte.TYPE) || (clazz == Short.TYPE) || (clazz == Integer.TYPE))
/*     */     {
/* 261 */       il.append(I2D);
/*     */     }
/* 263 */     else if (clazz == Long.TYPE) {
/* 264 */       il.append(L2D);
/*     */     }
/* 266 */     else if (clazz == Float.TYPE) {
/* 267 */       il.append(F2D);
/*     */     }
/* 269 */     else if (clazz == Double.TYPE) {
/* 270 */       il.append(NOP);
/*     */     }
/*     */     else {
/* 273 */       ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
/*     */ 
/* 275 */       classGen.getParser().reportError(2, err);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 284 */     translateTo(classGen, methodGen, Type.Reference);
/*     */   }
/*     */ 
/*     */   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 292 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 293 */     InstructionList il = methodGen.getInstructionList();
/* 294 */     il.append(new CHECKCAST(cpg.addClass("java.lang.Double")));
/* 295 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.Double", "doubleValue", "()D")));
/*     */   }
/*     */ 
/*     */   public Instruction ADD()
/*     */   {
/* 301 */     return InstructionConstants.DADD;
/*     */   }
/*     */ 
/*     */   public Instruction SUB() {
/* 305 */     return InstructionConstants.DSUB;
/*     */   }
/*     */ 
/*     */   public Instruction MUL() {
/* 309 */     return InstructionConstants.DMUL;
/*     */   }
/*     */ 
/*     */   public Instruction DIV() {
/* 313 */     return InstructionConstants.DDIV;
/*     */   }
/*     */ 
/*     */   public Instruction REM() {
/* 317 */     return InstructionConstants.DREM;
/*     */   }
/*     */ 
/*     */   public Instruction NEG() {
/* 321 */     return InstructionConstants.DNEG;
/*     */   }
/*     */ 
/*     */   public Instruction LOAD(int slot) {
/* 325 */     return new DLOAD(slot);
/*     */   }
/*     */ 
/*     */   public Instruction STORE(int slot) {
/* 329 */     return new DSTORE(slot);
/*     */   }
/*     */ 
/*     */   public Instruction POP() {
/* 333 */     return POP2;
/*     */   }
/*     */ 
/*     */   public Instruction CMP(boolean less) {
/* 337 */     return less ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
/*     */   }
/*     */ 
/*     */   public Instruction DUP() {
/* 341 */     return DUP2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType
 * JD-Core Version:    0.6.2
 */