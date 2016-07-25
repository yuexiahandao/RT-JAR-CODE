/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class When extends Instruction
/*     */ {
/*     */   private Expression _test;
/*  42 */   private boolean _ignore = false;
/*     */ 
/*     */   public void display(int indent) {
/*  45 */     indent(indent);
/*  46 */     Util.println("When");
/*  47 */     indent(indent + 4);
/*  48 */     System.out.print("test ");
/*  49 */     Util.println(this._test.toString());
/*  50 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public Expression getTest() {
/*  54 */     return this._test;
/*     */   }
/*     */ 
/*     */   public boolean ignore() {
/*  58 */     return this._ignore;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser) {
/*  62 */     this._test = parser.parseExpression(this, "test", null);
/*     */ 
/*  66 */     Object result = this._test.evaluateAtCompileTime();
/*  67 */     if ((result != null) && ((result instanceof Boolean))) {
/*  68 */       this._ignore = (!((Boolean)result).booleanValue());
/*     */     }
/*     */ 
/*  71 */     parseChildren(parser);
/*     */ 
/*  74 */     if (this._test.isDummy())
/*  75 */       reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  88 */     if (!(this._test.typeCheck(stable) instanceof BooleanType)) {
/*  89 */       this._test = new CastExpr(this._test, Type.Boolean);
/*     */     }
/*     */ 
/*  92 */     if (!this._ignore) {
/*  93 */       typeCheckContents(stable);
/*     */     }
/*     */ 
/*  96 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 104 */     ErrorMsg msg = new ErrorMsg("STRAY_WHEN_ERR", this);
/* 105 */     getParser().reportError(3, msg);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.When
 * JD-Core Version:    0.6.2
 */