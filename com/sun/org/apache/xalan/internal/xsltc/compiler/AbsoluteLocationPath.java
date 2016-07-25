/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*    */ 
/*    */ final class AbsoluteLocationPath extends Expression
/*    */ {
/*    */   private Expression _path;
/*    */ 
/*    */   public AbsoluteLocationPath()
/*    */   {
/* 49 */     this._path = null;
/*    */   }
/*    */ 
/*    */   public AbsoluteLocationPath(Expression path) {
/* 53 */     this._path = path;
/* 54 */     if (path != null)
/* 55 */       this._path.setParent(this);
/*    */   }
/*    */ 
/*    */   public void setParser(Parser parser)
/*    */   {
/* 60 */     super.setParser(parser);
/* 61 */     if (this._path != null)
/* 62 */       this._path.setParser(parser);
/*    */   }
/*    */ 
/*    */   public Expression getPath()
/*    */   {
/* 67 */     return this._path;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 71 */     return "AbsoluteLocationPath(" + (this._path != null ? this._path.toString() : "null") + ')';
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError
/*    */   {
/* 76 */     if (this._path != null) {
/* 77 */       Type ptype = this._path.typeCheck(stable);
/* 78 */       if ((ptype instanceof NodeType)) {
/* 79 */         this._path = new CastExpr(this._path, Type.NodeSet);
/*    */       }
/*    */     }
/* 82 */     return this._type = Type.NodeSet;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 86 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 87 */     InstructionList il = methodGen.getInstructionList();
/* 88 */     if (this._path != null) {
/* 89 */       int initAI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*    */ 
/* 105 */       this._path.translate(classGen, methodGen);
/* 106 */       LocalVariableGen relPathIterator = methodGen.addLocalVariable("abs_location_path_tmp", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*    */ 
/* 110 */       relPathIterator.setStart(il.append(new ASTORE(relPathIterator.getIndex())));
/*    */ 
/* 114 */       il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator")));
/* 115 */       il.append(DUP);
/* 116 */       relPathIterator.setEnd(il.append(new ALOAD(relPathIterator.getIndex())));
/*    */ 
/* 120 */       il.append(new INVOKESPECIAL(initAI));
/*    */     }
/*    */     else {
/* 123 */       int gitr = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*    */ 
/* 126 */       il.append(methodGen.loadDOM());
/* 127 */       il.append(new INVOKEINTERFACE(gitr, 1));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AbsoluteLocationPath
 * JD-Core Version:    0.6.2
 */