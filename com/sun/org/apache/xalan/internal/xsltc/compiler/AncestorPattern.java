/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class AncestorPattern extends RelativePathPattern
/*     */ {
/*     */   private final Pattern _left;
/*     */   private final RelativePathPattern _right;
/*     */   private InstructionHandle _loop;
/*     */ 
/*     */   public AncestorPattern(RelativePathPattern right)
/*     */   {
/*  54 */     this(null, right);
/*     */   }
/*     */ 
/*     */   public AncestorPattern(Pattern left, RelativePathPattern right) {
/*  58 */     this._left = left;
/*  59 */     (this._right = right).setParent(this);
/*  60 */     if (left != null)
/*  61 */       left.setParent(this);
/*     */   }
/*     */ 
/*     */   public InstructionHandle getLoopHandle()
/*     */   {
/*  66 */     return this._loop;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  70 */     super.setParser(parser);
/*  71 */     if (this._left != null) {
/*  72 */       this._left.setParser(parser);
/*     */     }
/*  74 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public boolean isWildcard()
/*     */   {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   public StepPattern getKernelPattern() {
/*  83 */     return this._right.getKernelPattern();
/*     */   }
/*     */ 
/*     */   public void reduceKernelPattern() {
/*  87 */     this._right.reduceKernelPattern();
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  91 */     if (this._left != null) {
/*  92 */       this._left.typeCheck(stable);
/*     */     }
/*  94 */     return this._right.typeCheck(stable);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  99 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 100 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 106 */     LocalVariableGen local = methodGen.addLocalVariable2("app", Util.getJCRefType("I"), il.getEnd());
/*     */ 
/* 110 */     com.sun.org.apache.bcel.internal.generic.Instruction loadLocal = new ILOAD(local.getIndex());
/*     */ 
/* 112 */     com.sun.org.apache.bcel.internal.generic.Instruction storeLocal = new ISTORE(local.getIndex());
/*     */ 
/* 115 */     if ((this._right instanceof StepPattern)) {
/* 116 */       il.append(DUP);
/* 117 */       il.append(storeLocal);
/* 118 */       this._right.translate(classGen, methodGen);
/* 119 */       il.append(methodGen.loadDOM());
/* 120 */       il.append(loadLocal);
/*     */     }
/*     */     else {
/* 123 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 125 */       if ((this._right instanceof AncestorPattern)) {
/* 126 */         il.append(methodGen.loadDOM());
/* 127 */         il.append(SWAP);
/*     */       }
/*     */     }
/*     */ 
/* 131 */     if (this._left != null) {
/* 132 */       int getParent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getParent", "(I)I");
/*     */ 
/* 135 */       InstructionHandle parent = il.append(new INVOKEINTERFACE(getParent, 2));
/*     */ 
/* 137 */       il.append(DUP);
/* 138 */       il.append(storeLocal);
/* 139 */       this._falseList.add(il.append(new IFLT(null)));
/* 140 */       il.append(loadLocal);
/*     */ 
/* 142 */       this._left.translate(classGen, methodGen);
/*     */ 
/* 144 */       SyntaxTreeNode p = getParent();
/* 145 */       if ((p != null) && (!(p instanceof Instruction)) && (!(p instanceof TopLevelElement)))
/*     */       {
/* 151 */         il.append(loadLocal);
/*     */       }
/*     */ 
/* 154 */       BranchHandle exit = il.append(new GOTO(null));
/* 155 */       this._loop = il.append(methodGen.loadDOM());
/* 156 */       il.append(loadLocal);
/* 157 */       local.setEnd(this._loop);
/* 158 */       il.append(new GOTO(parent));
/* 159 */       exit.setTarget(il.append(NOP));
/* 160 */       this._left.backPatchFalseList(this._loop);
/*     */ 
/* 162 */       this._trueList.append(this._left._trueList);
/*     */     }
/*     */     else {
/* 165 */       il.append(POP2);
/*     */     }
/*     */ 
/* 172 */     if ((this._right instanceof AncestorPattern)) {
/* 173 */       AncestorPattern ancestor = (AncestorPattern)this._right;
/* 174 */       this._falseList.backPatch(ancestor.getLoopHandle());
/*     */     }
/*     */ 
/* 177 */     this._trueList.append(this._right._trueList);
/* 178 */     this._falseList.append(this._right._falseList);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 182 */     return "AncestorPattern(" + this._left + ", " + this._right + ')';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AncestorPattern
 * JD-Core Version:    0.6.2
 */