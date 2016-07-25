/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class DocumentCall extends FunctionCall
/*     */ {
/*  46 */   private Expression _arg1 = null;
/*  47 */   private Expression _arg2 = null;
/*     */   private Type _arg1Type;
/*     */ 
/*     */   public DocumentCall(QName fname, Vector arguments)
/*     */   {
/*  54 */     super(fname, arguments);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  64 */     int ac = argumentCount();
/*  65 */     if ((ac < 1) || (ac > 2)) {
/*  66 */       ErrorMsg msg = new ErrorMsg("ILLEGAL_ARG_ERR", this);
/*  67 */       throw new TypeCheckError(msg);
/*     */     }
/*  69 */     if (getStylesheet() == null) {
/*  70 */       ErrorMsg msg = new ErrorMsg("ILLEGAL_ARG_ERR", this);
/*  71 */       throw new TypeCheckError(msg);
/*     */     }
/*     */ 
/*  75 */     this._arg1 = argument(0);
/*     */ 
/*  77 */     if (this._arg1 == null) {
/*  78 */       ErrorMsg msg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
/*  79 */       throw new TypeCheckError(msg);
/*     */     }
/*     */ 
/*  82 */     this._arg1Type = this._arg1.typeCheck(stable);
/*  83 */     if ((this._arg1Type != Type.NodeSet) && (this._arg1Type != Type.String)) {
/*  84 */       this._arg1 = new CastExpr(this._arg1, Type.String);
/*     */     }
/*     */ 
/*  88 */     if (ac == 2) {
/*  89 */       this._arg2 = argument(1);
/*     */ 
/*  91 */       if (this._arg2 == null) {
/*  92 */         ErrorMsg msg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
/*  93 */         throw new TypeCheckError(msg);
/*     */       }
/*     */ 
/*  96 */       Type arg2Type = this._arg2.typeCheck(stable);
/*     */ 
/*  98 */       if (arg2Type.identicalTo(Type.Node)) {
/*  99 */         this._arg2 = new CastExpr(this._arg2, Type.NodeSet);
/* 100 */       } else if (!arg2Type.identicalTo(Type.NodeSet))
/*     */       {
/* 103 */         ErrorMsg msg = new ErrorMsg("DOCUMENT_ARG_ERR", this);
/* 104 */         throw new TypeCheckError(msg);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     return this._type = Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 116 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 117 */     InstructionList il = methodGen.getInstructionList();
/* 118 */     int ac = argumentCount();
/*     */ 
/* 120 */     int domField = cpg.addFieldref(classGen.getClassName(), "_dom", "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*     */ 
/* 124 */     String docParamList = null;
/* 125 */     if (ac == 1)
/*     */     {
/* 127 */       docParamList = "(Ljava/lang/Object;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
/*     */     }
/*     */     else
/*     */     {
/* 131 */       docParamList = "(Ljava/lang/Object;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
/*     */     }
/*     */ 
/* 134 */     int docIdx = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.LoadDocument", "documentF", docParamList);
/*     */ 
/* 139 */     this._arg1.translate(classGen, methodGen);
/* 140 */     if (this._arg1Type == Type.NodeSet) {
/* 141 */       this._arg1.startIterator(classGen, methodGen);
/*     */     }
/*     */ 
/* 144 */     if (ac == 2)
/*     */     {
/* 146 */       this._arg2.translate(classGen, methodGen);
/* 147 */       this._arg2.startIterator(classGen, methodGen);
/*     */     }
/*     */ 
/* 151 */     il.append(new PUSH(cpg, getStylesheet().getSystemId()));
/* 152 */     il.append(classGen.loadTranslet());
/* 153 */     il.append(DUP);
/* 154 */     il.append(new GETFIELD(domField));
/* 155 */     il.append(new INVOKESTATIC(docIdx));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.DocumentCall
 * JD-Core Version:    0.6.2
 */