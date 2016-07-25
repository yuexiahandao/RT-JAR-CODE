/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
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
/*     */ final class ParentPattern extends RelativePathPattern
/*     */ {
/*     */   private final Pattern _left;
/*     */   private final RelativePathPattern _right;
/*     */ 
/*     */   public ParentPattern(Pattern left, RelativePathPattern right)
/*     */   {
/*  48 */     (this._left = left).setParent(this);
/*  49 */     (this._right = right).setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  53 */     super.setParser(parser);
/*  54 */     this._left.setParser(parser);
/*  55 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */   public StepPattern getKernelPattern() {
/*  63 */     return this._right.getKernelPattern();
/*     */   }
/*     */ 
/*     */   public void reduceKernelPattern() {
/*  67 */     this._right.reduceKernelPattern();
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  71 */     this._left.typeCheck(stable);
/*  72 */     return this._right.typeCheck(stable);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  76 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  77 */     InstructionList il = methodGen.getInstructionList();
/*  78 */     LocalVariableGen local = methodGen.addLocalVariable2("ppt", Util.getJCRefType("I"), null);
/*     */ 
/*  83 */     com.sun.org.apache.bcel.internal.generic.Instruction loadLocal = new ILOAD(local.getIndex());
/*     */ 
/*  85 */     com.sun.org.apache.bcel.internal.generic.Instruction storeLocal = new ISTORE(local.getIndex());
/*     */ 
/*  88 */     if (this._right.isWildcard()) {
/*  89 */       il.append(methodGen.loadDOM());
/*  90 */       il.append(SWAP);
/*     */     }
/*  92 */     else if ((this._right instanceof StepPattern)) {
/*  93 */       il.append(DUP);
/*  94 */       local.setStart(il.append(storeLocal));
/*     */ 
/*  96 */       this._right.translate(classGen, methodGen);
/*     */ 
/*  98 */       il.append(methodGen.loadDOM());
/*  99 */       local.setEnd(il.append(loadLocal));
/*     */     }
/*     */     else {
/* 102 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 104 */       if ((this._right instanceof AncestorPattern)) {
/* 105 */         il.append(methodGen.loadDOM());
/* 106 */         il.append(SWAP);
/*     */       }
/*     */     }
/*     */ 
/* 110 */     int getParent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getParent", "(I)I");
/*     */ 
/* 113 */     il.append(new INVOKEINTERFACE(getParent, 2));
/*     */ 
/* 115 */     SyntaxTreeNode p = getParent();
/* 116 */     if ((p == null) || ((p instanceof Instruction)) || ((p instanceof TopLevelElement)))
/*     */     {
/* 119 */       this._left.translate(classGen, methodGen);
/*     */     }
/*     */     else {
/* 122 */       il.append(DUP);
/* 123 */       InstructionHandle storeInst = il.append(storeLocal);
/*     */ 
/* 125 */       if (local.getStart() == null) {
/* 126 */         local.setStart(storeInst);
/*     */       }
/*     */ 
/* 129 */       this._left.translate(classGen, methodGen);
/*     */ 
/* 131 */       il.append(methodGen.loadDOM());
/* 132 */       local.setEnd(il.append(loadLocal));
/*     */     }
/*     */ 
/* 135 */     methodGen.removeLocalVariable(local);
/*     */ 
/* 141 */     if ((this._right instanceof AncestorPattern)) {
/* 142 */       AncestorPattern ancestor = (AncestorPattern)this._right;
/* 143 */       this._left.backPatchFalseList(ancestor.getLoopHandle());
/*     */     }
/*     */ 
/* 146 */     this._trueList.append(this._right._trueList.append(this._left._trueList));
/* 147 */     this._falseList.append(this._right._falseList.append(this._left._falseList));
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 151 */     return "Parent(" + this._left + ", " + this._right + ')';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ParentPattern
 * JD-Core Version:    0.6.2
 */