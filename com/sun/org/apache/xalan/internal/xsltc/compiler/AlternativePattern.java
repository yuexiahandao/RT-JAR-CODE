/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ final class AlternativePattern extends Pattern
/*     */ {
/*     */   private final Pattern _left;
/*     */   private final Pattern _right;
/*     */ 
/*     */   public AlternativePattern(Pattern left, Pattern right)
/*     */   {
/*  47 */     this._left = left;
/*  48 */     this._right = right;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  52 */     super.setParser(parser);
/*  53 */     this._left.setParser(parser);
/*  54 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public Pattern getLeft() {
/*  58 */     return this._left;
/*     */   }
/*     */ 
/*     */   public Pattern getRight() {
/*  62 */     return this._right;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  69 */     this._left.typeCheck(stable);
/*  70 */     this._right.typeCheck(stable);
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public double getPriority() {
/*  75 */     double left = this._left.getPriority();
/*  76 */     double right = this._right.getPriority();
/*     */ 
/*  78 */     if (left < right) {
/*  79 */       return left;
/*     */     }
/*  81 */     return right;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  85 */     return "alternative(" + this._left + ", " + this._right + ')';
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  89 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  91 */     this._left.translate(classGen, methodGen);
/*  92 */     InstructionHandle gotot = il.append(new GOTO(null));
/*  93 */     il.append(methodGen.loadContextNode());
/*  94 */     this._right.translate(classGen, methodGen);
/*     */ 
/*  96 */     this._left._trueList.backPatch(gotot);
/*  97 */     this._left._falseList.backPatch(gotot.getNext());
/*     */ 
/*  99 */     this._trueList.append(this._right._trueList.add(gotot));
/* 100 */     this._falseList.append(this._right._falseList);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AlternativePattern
 * JD-Core Version:    0.6.2
 */