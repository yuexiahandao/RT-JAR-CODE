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
/*    */ final class FilteredAbsoluteLocationPath extends Expression
/*    */ {
/*    */   private Expression _path;
/*    */ 
/*    */   public FilteredAbsoluteLocationPath()
/*    */   {
/* 48 */     this._path = null;
/*    */   }
/*    */ 
/*    */   public FilteredAbsoluteLocationPath(Expression path) {
/* 52 */     this._path = path;
/* 53 */     if (path != null)
/* 54 */       this._path.setParent(this);
/*    */   }
/*    */ 
/*    */   public void setParser(Parser parser)
/*    */   {
/* 59 */     super.setParser(parser);
/* 60 */     if (this._path != null)
/* 61 */       this._path.setParser(parser);
/*    */   }
/*    */ 
/*    */   public Expression getPath()
/*    */   {
/* 66 */     return this._path;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 70 */     return "FilteredAbsoluteLocationPath(" + (this._path != null ? this._path.toString() : "null") + ')';
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError
/*    */   {
/* 75 */     if (this._path != null) {
/* 76 */       Type ptype = this._path.typeCheck(stable);
/* 77 */       if ((ptype instanceof NodeType)) {
/* 78 */         this._path = new CastExpr(this._path, Type.NodeSet);
/*    */       }
/*    */     }
/* 81 */     return this._type = Type.NodeSet;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 85 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 86 */     InstructionList il = methodGen.getInstructionList();
/* 87 */     if (this._path != null) {
/* 88 */       int initDFI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.DupFilterIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*    */ 
/* 104 */       LocalVariableGen pathTemp = methodGen.addLocalVariable("filtered_absolute_location_path_tmp", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*    */ 
/* 108 */       this._path.translate(classGen, methodGen);
/* 109 */       pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
/*    */ 
/* 112 */       il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.DupFilterIterator")));
/* 113 */       il.append(DUP);
/* 114 */       pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
/*    */ 
/* 117 */       il.append(new INVOKESPECIAL(initDFI));
/*    */     }
/*    */     else {
/* 120 */       int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*    */ 
/* 123 */       il.append(methodGen.loadDOM());
/* 124 */       il.append(new INVOKEINTERFACE(git, 1));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FilteredAbsoluteLocationPath
 * JD-Core Version:    0.6.2
 */