/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class NameBase extends FunctionCall
/*     */ {
/*  42 */   private Expression _param = null;
/*  43 */   private Type _paramType = Type.Node;
/*     */ 
/*     */   public NameBase(QName fname)
/*     */   {
/*  49 */     super(fname);
/*     */   }
/*     */ 
/*     */   public NameBase(QName fname, Vector arguments)
/*     */   {
/*  56 */     super(fname, arguments);
/*  57 */     this._param = argument(0);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  68 */     switch (argumentCount()) {
/*     */     case 0:
/*  70 */       this._paramType = Type.Node;
/*  71 */       break;
/*     */     case 1:
/*  73 */       this._paramType = this._param.typeCheck(stable);
/*  74 */       break;
/*     */     default:
/*  76 */       throw new TypeCheckError(this);
/*     */     }
/*     */ 
/*  80 */     if ((this._paramType != Type.NodeSet) && (this._paramType != Type.Node) && (this._paramType != Type.Reference))
/*     */     {
/*  83 */       throw new TypeCheckError(this);
/*     */     }
/*     */ 
/*  86 */     return this._type = Type.String;
/*     */   }
/*     */ 
/*     */   public Type getType() {
/*  90 */     return this._type;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  99 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 100 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 102 */     il.append(methodGen.loadDOM());
/*     */ 
/* 105 */     if (argumentCount() == 0) {
/* 106 */       il.append(methodGen.loadContextNode());
/*     */     }
/* 109 */     else if (this._paramType == Type.Node) {
/* 110 */       this._param.translate(classGen, methodGen);
/*     */     }
/* 112 */     else if (this._paramType == Type.Reference) {
/* 113 */       this._param.translate(classGen, methodGen);
/* 114 */       il.append(new INVOKESTATIC(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;")));
/*     */ 
/* 121 */       il.append(methodGen.nextNode());
/*     */     }
/*     */     else
/*     */     {
/* 125 */       this._param.translate(classGen, methodGen);
/* 126 */       this._param.startIterator(classGen, methodGen);
/* 127 */       il.append(methodGen.nextNode());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NameBase
 * JD-Core Version:    0.6.2
 */