/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class FormatNumberCall extends FunctionCall
/*     */ {
/*     */   private Expression _value;
/*     */   private Expression _format;
/*     */   private Expression _name;
/*  49 */   private QName _resolvedQName = null;
/*     */ 
/*     */   public FormatNumberCall(QName fname, Vector arguments) {
/*  52 */     super(fname, arguments);
/*  53 */     this._value = argument(0);
/*  54 */     this._format = argument(1);
/*  55 */     this._name = (argumentCount() == 3 ? argument(2) : null);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  61 */     getStylesheet().numberFormattingUsed();
/*     */ 
/*  63 */     Type tvalue = this._value.typeCheck(stable);
/*  64 */     if (!(tvalue instanceof RealType)) {
/*  65 */       this._value = new CastExpr(this._value, Type.Real);
/*     */     }
/*  67 */     Type tformat = this._format.typeCheck(stable);
/*  68 */     if (!(tformat instanceof StringType)) {
/*  69 */       this._format = new CastExpr(this._format, Type.String);
/*     */     }
/*  71 */     if (argumentCount() == 3) {
/*  72 */       Type tname = this._name.typeCheck(stable);
/*     */ 
/*  74 */       if ((this._name instanceof LiteralExpr)) {
/*  75 */         LiteralExpr literal = (LiteralExpr)this._name;
/*  76 */         this._resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());
/*     */       }
/*  79 */       else if (!(tname instanceof StringType)) {
/*  80 */         this._name = new CastExpr(this._name, Type.String);
/*     */       }
/*     */     }
/*  83 */     return this._type = Type.String;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  87 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  88 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  90 */     this._value.translate(classGen, methodGen);
/*  91 */     this._format.translate(classGen, methodGen);
/*     */ 
/*  93 */     int fn3arg = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "formatNumber", "(DLjava/lang/String;Ljava/text/DecimalFormat;)Ljava/lang/String;");
/*     */ 
/*  98 */     int get = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "getDecimalFormat", "(Ljava/lang/String;)Ljava/text/DecimalFormat;");
/*     */ 
/* 103 */     il.append(classGen.loadTranslet());
/* 104 */     if (this._name == null) {
/* 105 */       il.append(new PUSH(cpg, ""));
/*     */     }
/* 107 */     else if (this._resolvedQName != null) {
/* 108 */       il.append(new PUSH(cpg, this._resolvedQName.toString()));
/*     */     }
/*     */     else {
/* 111 */       this._name.translate(classGen, methodGen);
/*     */     }
/* 113 */     il.append(new INVOKEVIRTUAL(get));
/* 114 */     il.append(new INVOKESTATIC(fn3arg));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FormatNumberCall
 * JD-Core Version:    0.6.2
 */