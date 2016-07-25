/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class ProcessingInstructionPattern extends StepPattern
/*     */ {
/*  48 */   private String _name = null;
/*  49 */   private boolean _typeChecked = false;
/*     */ 
/*     */   public ProcessingInstructionPattern(String name)
/*     */   {
/*  55 */     super(3, 7, null);
/*  56 */     this._name = name;
/*     */   }
/*     */ 
/*     */   public double getDefaultPriority()
/*     */   {
/*  64 */     return this._name != null ? 0.0D : -0.5D;
/*     */   }
/*     */   public String toString() {
/*  67 */     if (this._predicates == null) {
/*  68 */       return "processing-instruction(" + this._name + ")";
/*     */     }
/*  70 */     return "processing-instruction(" + this._name + ")" + this._predicates;
/*     */   }
/*     */ 
/*     */   public void reduceKernelPattern() {
/*  74 */     this._typeChecked = true;
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  82 */     if (hasPredicates())
/*     */     {
/*  84 */       int n = this._predicates.size();
/*  85 */       for (int i = 0; i < n; i++) {
/*  86 */         Predicate pred = (Predicate)this._predicates.elementAt(i);
/*  87 */         pred.typeCheck(stable);
/*     */       }
/*     */     }
/*  90 */     return Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  94 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  95 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  98 */     int gname = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeName", "(I)Ljava/lang/String;");
/*     */ 
/* 101 */     int cmp = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
/*     */ 
/* 105 */     il.append(methodGen.loadCurrentNode());
/* 106 */     il.append(SWAP);
/*     */ 
/* 109 */     il.append(methodGen.storeCurrentNode());
/*     */ 
/* 112 */     if (!this._typeChecked) {
/* 113 */       il.append(methodGen.loadCurrentNode());
/* 114 */       int getType = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*     */ 
/* 117 */       il.append(methodGen.loadDOM());
/* 118 */       il.append(methodGen.loadCurrentNode());
/* 119 */       il.append(new INVOKEINTERFACE(getType, 2));
/* 120 */       il.append(new PUSH(cpg, 7));
/* 121 */       this._falseList.add(il.append(new IF_ICMPEQ(null)));
/*     */     }
/*     */ 
/* 125 */     il.append(new PUSH(cpg, this._name));
/*     */ 
/* 127 */     il.append(methodGen.loadDOM());
/* 128 */     il.append(methodGen.loadCurrentNode());
/* 129 */     il.append(new INVOKEINTERFACE(gname, 2));
/*     */ 
/* 131 */     il.append(new INVOKEVIRTUAL(cmp));
/* 132 */     this._falseList.add(il.append(new IFEQ(null)));
/*     */ 
/* 135 */     if (hasPredicates()) {
/* 136 */       int n = this._predicates.size();
/* 137 */       for (int i = 0; i < n; i++) {
/* 138 */         Predicate pred = (Predicate)this._predicates.elementAt(i);
/* 139 */         Expression exp = pred.getExpr();
/* 140 */         exp.translateDesynthesized(classGen, methodGen);
/* 141 */         this._trueList.append(exp._trueList);
/* 142 */         this._falseList.append(exp._falseList);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 148 */     InstructionHandle restore = il.append(methodGen.storeCurrentNode());
/* 149 */     backPatchTrueList(restore);
/* 150 */     BranchHandle skipFalse = il.append(new GOTO(null));
/*     */ 
/* 153 */     restore = il.append(methodGen.storeCurrentNode());
/* 154 */     backPatchFalseList(restore);
/* 155 */     this._falseList.add(il.append(new GOTO(null)));
/*     */ 
/* 158 */     skipFalse.setTarget(il.append(NOP));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ProcessingInstructionPattern
 * JD-Core Version:    0.6.2
 */