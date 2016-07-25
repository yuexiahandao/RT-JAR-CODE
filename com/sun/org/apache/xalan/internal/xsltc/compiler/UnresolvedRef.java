/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ final class UnresolvedRef extends VariableRefBase
/*     */ {
/*  37 */   private QName _variableName = null;
/*  38 */   private VariableRefBase _ref = null;
/*     */ 
/*     */   public UnresolvedRef(QName name)
/*     */   {
/*  42 */     this._variableName = name;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  46 */     return this._variableName;
/*     */   }
/*     */ 
/*     */   private ErrorMsg reportError() {
/*  50 */     ErrorMsg err = new ErrorMsg("VARIABLE_UNDEF_ERR", this._variableName, this);
/*     */ 
/*  52 */     getParser().reportError(3, err);
/*  53 */     return err;
/*     */   }
/*     */ 
/*     */   private VariableRefBase resolve(Parser parser, SymbolTable stable)
/*     */   {
/*  59 */     VariableBase ref = parser.lookupVariable(this._variableName);
/*  60 */     if (ref == null) {
/*  61 */       ref = (VariableBase)stable.lookupName(this._variableName);
/*     */     }
/*  63 */     if (ref == null) {
/*  64 */       reportError();
/*  65 */       return null;
/*     */     }
/*     */ 
/*  69 */     this._variable = ref;
/*  70 */     addParentDependency();
/*     */ 
/*  72 */     if ((ref instanceof Variable)) {
/*  73 */       return new VariableRef((Variable)ref);
/*     */     }
/*  75 */     if ((ref instanceof Param)) {
/*  76 */       return new ParameterRef((Param)ref);
/*     */     }
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*     */     ErrorMsg err;
/*  82 */     if (this._ref != null) {
/*  83 */       String name = this._variableName.toString();
/*  84 */       err = new ErrorMsg("CIRCULAR_VARIABLE_ERR", name, this);
/*     */     }
/*     */ 
/*  87 */     if ((this._ref = resolve(getParser(), stable)) != null) {
/*  88 */       return this._type = this._ref.typeCheck(stable);
/*     */     }
/*  90 */     throw new TypeCheckError(reportError());
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  94 */     if (this._ref != null)
/*  95 */       this._ref.translate(classGen, methodGen);
/*     */     else
/*  97 */       reportError();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 101 */     return "unresolved-ref()";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.UnresolvedRef
 * JD-Core Version:    0.6.2
 */