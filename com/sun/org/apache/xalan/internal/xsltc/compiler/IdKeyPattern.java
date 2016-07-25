/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ abstract class IdKeyPattern extends LocationPathPattern
/*     */ {
/*  44 */   protected RelativePathPattern _left = null;
/*  45 */   private String _index = null;
/*  46 */   private String _value = null;
/*     */ 
/*     */   public IdKeyPattern(String index, String value) {
/*  49 */     this._index = index;
/*  50 */     this._value = value;
/*     */   }
/*     */ 
/*     */   public String getIndexName() {
/*  54 */     return this._index;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  58 */     return Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   public void setLeft(RelativePathPattern left) {
/*  66 */     this._left = left;
/*     */   }
/*     */ 
/*     */   public StepPattern getKernelPattern() {
/*  70 */     return null;
/*     */   }
/*     */   public void reduceKernelPattern() {
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  76 */     return "id/keyPattern(" + this._index + ", " + this._value + ')';
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  86 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  87 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  90 */     int getKeyIndex = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex;");
/*     */ 
/*  96 */     int lookupId = cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex", "containsID", "(ILjava/lang/Object;)I");
/*     */ 
/*  99 */     int lookupKey = cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/KeyIndex", "containsKey", "(ILjava/lang/Object;)I");
/*     */ 
/* 102 */     int getNodeIdent = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeIdent", "(I)I");
/*     */ 
/* 108 */     il.append(classGen.loadTranslet());
/* 109 */     il.append(new PUSH(cpg, this._index));
/* 110 */     il.append(new INVOKEVIRTUAL(getKeyIndex));
/*     */ 
/* 114 */     il.append(SWAP);
/* 115 */     il.append(new PUSH(cpg, this._value));
/* 116 */     if ((this instanceof IdPattern))
/*     */     {
/* 118 */       il.append(new INVOKEVIRTUAL(lookupId));
/*     */     }
/*     */     else
/*     */     {
/* 122 */       il.append(new INVOKEVIRTUAL(lookupKey));
/*     */     }
/*     */ 
/* 125 */     this._trueList.add(il.append(new IFNE(null)));
/* 126 */     this._falseList.add(il.append(new GOTO(null)));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.IdKeyPattern
 * JD-Core Version:    0.6.2
 */