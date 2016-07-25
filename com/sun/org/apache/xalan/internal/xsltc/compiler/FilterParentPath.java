/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class FilterParentPath extends Expression
/*     */ {
/*     */   private Expression _filterExpr;
/*     */   private Expression _path;
/*  52 */   private boolean _hasDescendantAxis = false;
/*     */ 
/*     */   public FilterParentPath(Expression filterExpr, Expression path) {
/*  55 */     (this._path = path).setParent(this);
/*  56 */     (this._filterExpr = filterExpr).setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  60 */     super.setParser(parser);
/*  61 */     this._filterExpr.setParser(parser);
/*  62 */     this._path.setParser(parser);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  66 */     return "FilterParentPath(" + this._filterExpr + ", " + this._path + ')';
/*     */   }
/*     */ 
/*     */   public void setDescendantAxis() {
/*  70 */     this._hasDescendantAxis = true;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  79 */     Type ftype = this._filterExpr.typeCheck(stable);
/*  80 */     if (!(ftype instanceof NodeSetType)) {
/*  81 */       if ((ftype instanceof ReferenceType)) {
/*  82 */         this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
/*     */       }
/*  89 */       else if ((ftype instanceof NodeType)) {
/*  90 */         this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
/*     */       }
/*     */       else {
/*  93 */         throw new TypeCheckError(this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  98 */     Type ptype = this._path.typeCheck(stable);
/*  99 */     if (!(ptype instanceof NodeSetType)) {
/* 100 */       this._path = new CastExpr(this._path, Type.NodeSet);
/*     */     }
/*     */ 
/* 103 */     return this._type = Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 107 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 108 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 110 */     int initSI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*     */ 
/* 127 */     this._filterExpr.translate(classGen, methodGen);
/* 128 */     LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_parent_path_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 132 */     filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
/*     */ 
/* 134 */     this._path.translate(classGen, methodGen);
/* 135 */     LocalVariableGen pathTemp = methodGen.addLocalVariable("filter_parent_path_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 139 */     pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
/*     */ 
/* 141 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator")));
/* 142 */     il.append(DUP);
/* 143 */     filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
/* 144 */     pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
/*     */ 
/* 147 */     il.append(new INVOKESPECIAL(initSI));
/*     */ 
/* 150 */     if (this._hasDescendantAxis) {
/* 151 */       int incl = cpg.addMethodref("com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 154 */       il.append(new INVOKEVIRTUAL(incl));
/*     */     }
/*     */ 
/* 157 */     SyntaxTreeNode parent = getParent();
/*     */ 
/* 159 */     boolean parentAlreadyOrdered = ((parent instanceof RelativeLocationPath)) || ((parent instanceof FilterParentPath)) || ((parent instanceof KeyCall)) || ((parent instanceof CurrentCall)) || ((parent instanceof DocumentCall));
/*     */ 
/* 166 */     if (!parentAlreadyOrdered) {
/* 167 */       int order = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "orderNodes", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 170 */       il.append(methodGen.loadDOM());
/* 171 */       il.append(SWAP);
/* 172 */       il.append(methodGen.loadContextNode());
/* 173 */       il.append(new INVOKEINTERFACE(order, 3));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FilterParentPath
 * JD-Core Version:    0.6.2
 */