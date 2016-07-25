/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class UnionPathExpr extends Expression
/*     */ {
/*     */   private final Expression _pathExpr;
/*     */   private final Expression _rest;
/*  49 */   private boolean _reverse = false;
/*     */   private Expression[] _components;
/*     */ 
/*     */   public UnionPathExpr(Expression pathExpr, Expression rest)
/*     */   {
/*  55 */     this._pathExpr = pathExpr;
/*  56 */     this._rest = rest;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  60 */     super.setParser(parser);
/*     */ 
/*  62 */     Vector components = new Vector();
/*  63 */     flatten(components);
/*  64 */     int size = components.size();
/*  65 */     this._components = ((Expression[])components.toArray(new Expression[size]));
/*  66 */     for (int i = 0; i < size; i++) {
/*  67 */       this._components[i].setParser(parser);
/*  68 */       this._components[i].setParent(this);
/*  69 */       if ((this._components[i] instanceof Step)) {
/*  70 */         Step step = (Step)this._components[i];
/*  71 */         int axis = step.getAxis();
/*  72 */         int type = step.getNodeType();
/*     */ 
/*  74 */         if ((axis == 2) || (type == 2)) {
/*  75 */           this._components[i] = this._components[0];
/*  76 */           this._components[0] = step;
/*     */         }
/*     */ 
/*  79 */         if (Axis.isReverse(axis)) this._reverse = true;
/*     */       }
/*     */     }
/*     */ 
/*  83 */     if ((getParent() instanceof Expression)) this._reverse = false; 
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  87 */     int length = this._components.length;
/*  88 */     for (int i = 0; i < length; i++) {
/*  89 */       if (this._components[i].typeCheck(stable) != Type.NodeSet) {
/*  90 */         this._components[i] = new CastExpr(this._components[i], Type.NodeSet);
/*     */       }
/*     */     }
/*  93 */     return this._type = Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  97 */     return "union(" + this._pathExpr + ", " + this._rest + ')';
/*     */   }
/*     */ 
/*     */   private void flatten(Vector components) {
/* 101 */     components.addElement(this._pathExpr);
/* 102 */     if (this._rest != null)
/* 103 */       if ((this._rest instanceof UnionPathExpr)) {
/* 104 */         ((UnionPathExpr)this._rest).flatten(components);
/*     */       }
/*     */       else
/* 107 */         components.addElement(this._rest);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 113 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 114 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 116 */     int init = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.UnionIterator", "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
/*     */ 
/* 119 */     int iter = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.UnionIterator", "addIterator", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/UnionIterator;");
/*     */ 
/* 124 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.UnionIterator")));
/* 125 */     il.append(DUP);
/* 126 */     il.append(methodGen.loadDOM());
/* 127 */     il.append(new INVOKESPECIAL(init));
/*     */ 
/* 130 */     int length = this._components.length;
/* 131 */     for (int i = 0; i < length; i++) {
/* 132 */       this._components[i].translate(classGen, methodGen);
/* 133 */       il.append(new INVOKEVIRTUAL(iter));
/*     */     }
/*     */ 
/* 137 */     if (this._reverse) {
/* 138 */       int order = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "orderNodes", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 141 */       il.append(methodGen.loadDOM());
/* 142 */       il.append(SWAP);
/* 143 */       il.append(methodGen.loadContextNode());
/* 144 */       il.append(new INVOKEINTERFACE(order, 3));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.UnionPathExpr
 * JD-Core Version:    0.6.2
 */