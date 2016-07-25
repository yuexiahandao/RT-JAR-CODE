/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO_W;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class AbsolutePathPattern extends LocationPathPattern
/*     */ {
/*     */   private final RelativePathPattern _left;
/*     */ 
/*     */   public AbsolutePathPattern(RelativePathPattern left)
/*     */   {
/*  52 */     this._left = left;
/*  53 */     if (left != null)
/*  54 */       left.setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser)
/*     */   {
/*  59 */     super.setParser(parser);
/*  60 */     if (this._left != null)
/*  61 */       this._left.setParser(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  65 */     return this._left == null ? Type.Root : this._left.typeCheck(stable);
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   public StepPattern getKernelPattern() {
/*  73 */     return this._left != null ? this._left.getKernelPattern() : null;
/*     */   }
/*     */ 
/*     */   public void reduceKernelPattern() {
/*  77 */     this._left.reduceKernelPattern();
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  81 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  82 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  84 */     if (this._left != null) {
/*  85 */       if ((this._left instanceof StepPattern)) {
/*  86 */         LocalVariableGen local = methodGen.addLocalVariable2("apptmp", Util.getJCRefType("I"), null);
/*     */ 
/*  91 */         il.append(DUP);
/*  92 */         local.setStart(il.append(new ISTORE(local.getIndex())));
/*  93 */         this._left.translate(classGen, methodGen);
/*  94 */         il.append(methodGen.loadDOM());
/*  95 */         local.setEnd(il.append(new ILOAD(local.getIndex())));
/*  96 */         methodGen.removeLocalVariable(local);
/*     */       }
/*     */       else {
/*  99 */         this._left.translate(classGen, methodGen);
/*     */       }
/*     */     }
/*     */ 
/* 103 */     int getParent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getParent", "(I)I");
/*     */ 
/* 106 */     int getType = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*     */ 
/* 110 */     InstructionHandle begin = il.append(methodGen.loadDOM());
/* 111 */     il.append(SWAP);
/* 112 */     il.append(new INVOKEINTERFACE(getParent, 2));
/* 113 */     if ((this._left instanceof AncestorPattern)) {
/* 114 */       il.append(methodGen.loadDOM());
/* 115 */       il.append(SWAP);
/*     */     }
/* 117 */     il.append(new INVOKEINTERFACE(getType, 2));
/* 118 */     il.append(new PUSH(cpg, 9));
/*     */ 
/* 120 */     BranchHandle skip = il.append(new IF_ICMPEQ(null));
/* 121 */     this._falseList.add(il.append(new GOTO_W(null)));
/* 122 */     skip.setTarget(il.append(NOP));
/*     */ 
/* 124 */     if (this._left != null) {
/* 125 */       this._left.backPatchTrueList(begin);
/*     */ 
/* 131 */       if ((this._left instanceof AncestorPattern)) {
/* 132 */         AncestorPattern ancestor = (AncestorPattern)this._left;
/* 133 */         this._falseList.backPatch(ancestor.getLoopHandle());
/*     */       }
/* 135 */       this._falseList.append(this._left._falseList);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 140 */     return "absolutePathPattern(" + (this._left != null ? this._left.toString() : ")");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AbsolutePathPattern
 * JD-Core Version:    0.6.2
 */