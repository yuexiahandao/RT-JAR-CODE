/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class FilterExpr extends Expression
/*     */ {
/*     */   private Expression _primary;
/*     */   private final Vector _predicates;
/*     */ 
/*     */   public FilterExpr(Expression primary, Vector predicates)
/*     */   {
/*  65 */     this._primary = primary;
/*  66 */     this._predicates = predicates;
/*  67 */     primary.setParent(this);
/*     */   }
/*     */ 
/*     */   protected Expression getExpr() {
/*  71 */     if ((this._primary instanceof CastExpr)) {
/*  72 */       return ((CastExpr)this._primary).getExpr();
/*     */     }
/*  74 */     return this._primary;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  78 */     super.setParser(parser);
/*  79 */     this._primary.setParser(parser);
/*  80 */     if (this._predicates != null) {
/*  81 */       int n = this._predicates.size();
/*  82 */       for (int i = 0; i < n; i++) {
/*  83 */         Expression exp = (Expression)this._predicates.elementAt(i);
/*  84 */         exp.setParser(parser);
/*  85 */         exp.setParent(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  91 */     return "filter-expr(" + this._primary + ", " + this._predicates + ")";
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 102 */     Type ptype = this._primary.typeCheck(stable);
/* 103 */     boolean canOptimize = this._primary instanceof KeyCall;
/*     */ 
/* 105 */     if (!(ptype instanceof NodeSetType)) {
/* 106 */       if ((ptype instanceof ReferenceType)) {
/* 107 */         this._primary = new CastExpr(this._primary, Type.NodeSet);
/*     */       }
/*     */       else {
/* 110 */         throw new TypeCheckError(this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 115 */     int n = this._predicates.size();
/* 116 */     for (int i = 0; i < n; i++) {
/* 117 */       Predicate pred = (Predicate)this._predicates.elementAt(i);
/*     */ 
/* 119 */       if (!canOptimize) {
/* 120 */         pred.dontOptimize();
/*     */       }
/* 122 */       pred.typeCheck(stable);
/*     */     }
/* 124 */     return this._type = Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 132 */     translateFilterExpr(classGen, methodGen, this._predicates == null ? -1 : this._predicates.size() - 1);
/*     */   }
/*     */ 
/*     */   private void translateFilterExpr(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex)
/*     */   {
/* 138 */     if (predicateIndex >= 0) {
/* 139 */       translatePredicates(classGen, methodGen, predicateIndex);
/*     */     }
/*     */     else
/* 142 */       this._primary.translate(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex)
/*     */   {
/* 155 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 156 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 159 */     if (predicateIndex < 0) {
/* 160 */       translateFilterExpr(classGen, methodGen, predicateIndex);
/*     */     }
/*     */     else
/*     */     {
/* 164 */       Predicate predicate = (Predicate)this._predicates.get(predicateIndex--);
/*     */ 
/* 167 */       translatePredicates(classGen, methodGen, predicateIndex);
/*     */ 
/* 169 */       if (predicate.isNthPositionFilter()) {
/* 170 */         int nthIteratorIdx = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)V");
/*     */ 
/* 185 */         LocalVariableGen iteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 189 */         iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
/*     */ 
/* 192 */         predicate.translate(classGen, methodGen);
/* 193 */         LocalVariableGen predicateValueTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("I"), null, null);
/*     */ 
/* 197 */         predicateValueTemp.setStart(il.append(new ISTORE(predicateValueTemp.getIndex())));
/*     */ 
/* 200 */         il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator")));
/* 201 */         il.append(DUP);
/* 202 */         iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
/*     */ 
/* 204 */         predicateValueTemp.setEnd(il.append(new ILOAD(predicateValueTemp.getIndex())));
/*     */ 
/* 206 */         il.append(new INVOKESPECIAL(nthIteratorIdx));
/*     */       }
/*     */       else {
/* 209 */         int initCNLI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;ZLcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;ILcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;)V");
/*     */ 
/* 225 */         LocalVariableGen nodeIteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 229 */         nodeIteratorTemp.setStart(il.append(new ASTORE(nodeIteratorTemp.getIndex())));
/*     */ 
/* 232 */         predicate.translate(classGen, methodGen);
/* 233 */         LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;"), null, null);
/*     */ 
/* 237 */         filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
/*     */ 
/* 240 */         il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListIterator")));
/* 241 */         il.append(DUP);
/*     */ 
/* 244 */         nodeIteratorTemp.setEnd(il.append(new ALOAD(nodeIteratorTemp.getIndex())));
/*     */ 
/* 246 */         il.append(ICONST_1);
/* 247 */         filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
/* 248 */         il.append(methodGen.loadCurrentNode());
/* 249 */         il.append(classGen.loadTranslet());
/* 250 */         il.append(new INVOKESPECIAL(initCNLI));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FilterExpr
 * JD-Core Version:    0.6.2
 */