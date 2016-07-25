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
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ 
/*     */ final class ParentLocationPath extends RelativeLocationPath
/*     */ {
/*     */   private Expression _step;
/*     */   private final RelativeLocationPath _path;
/*     */   private Type stype;
/*  51 */   private boolean _orderNodes = false;
/*  52 */   private boolean _axisMismatch = false;
/*     */ 
/*     */   public ParentLocationPath(RelativeLocationPath path, Expression step) {
/*  55 */     this._path = path;
/*  56 */     this._step = step;
/*  57 */     this._path.setParent(this);
/*  58 */     this._step.setParent(this);
/*     */ 
/*  60 */     if ((this._step instanceof Step))
/*  61 */       this._axisMismatch = checkAxisMismatch();
/*     */   }
/*     */ 
/*     */   public void setAxis(int axis)
/*     */   {
/*  66 */     this._path.setAxis(axis);
/*     */   }
/*     */ 
/*     */   public int getAxis() {
/*  70 */     return this._path.getAxis();
/*     */   }
/*     */ 
/*     */   public RelativeLocationPath getPath() {
/*  74 */     return this._path;
/*     */   }
/*     */ 
/*     */   public Expression getStep() {
/*  78 */     return this._step;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  82 */     super.setParser(parser);
/*  83 */     this._step.setParser(parser);
/*  84 */     this._path.setParser(parser);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  88 */     return "ParentLocationPath(" + this._path + ", " + this._step + ')';
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  92 */     this.stype = this._step.typeCheck(stable);
/*  93 */     this._path.typeCheck(stable);
/*     */ 
/*  95 */     if (this._axisMismatch) enableNodeOrdering();
/*     */ 
/*  97 */     return this._type = Type.NodeSet;
/*     */   }
/*     */ 
/*     */   public void enableNodeOrdering() {
/* 101 */     SyntaxTreeNode parent = getParent();
/* 102 */     if ((parent instanceof ParentLocationPath))
/* 103 */       ((ParentLocationPath)parent).enableNodeOrdering();
/*     */     else
/* 105 */       this._orderNodes = true;
/*     */   }
/*     */ 
/*     */   public boolean checkAxisMismatch()
/*     */   {
/* 116 */     int left = this._path.getAxis();
/* 117 */     int right = ((Step)this._step).getAxis();
/*     */ 
/* 119 */     if (((left == 0) || (left == 1)) && ((right == 3) || (right == 4) || (right == 5) || (right == 10) || (right == 11) || (right == 12)))
/*     */     {
/* 126 */       return true;
/*     */     }
/* 128 */     if (((left == 3) && (right == 0)) || (right == 1) || (right == 10) || (right == 11))
/*     */     {
/* 133 */       return true;
/*     */     }
/* 135 */     if ((left == 4) || (left == 5)) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (((left == 6) || (left == 7)) && ((right == 6) || (right == 10) || (right == 11) || (right == 12)))
/*     */     {
/* 143 */       return true;
/*     */     }
/* 145 */     if (((left == 11) || (left == 12)) && ((right == 4) || (right == 5) || (right == 6) || (right == 7) || (right == 10) || (right == 11) || (right == 12)))
/*     */     {
/* 153 */       return true;
/*     */     }
/* 155 */     if ((right == 6) && (left == 3))
/*     */     {
/* 160 */       if ((this._path instanceof Step)) {
/* 161 */         int type = ((Step)this._path).getNodeType();
/* 162 */         if (type == 2) return true;
/*     */       }
/*     */     }
/*     */ 
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 172 */     this._path.translate(classGen, methodGen);
/*     */ 
/* 174 */     translateStep(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateStep(ClassGenerator classGen, MethodGenerator methodGen) {
/* 178 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 179 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 190 */     LocalVariableGen pathTemp = methodGen.addLocalVariable("parent_location_path_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 194 */     pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
/*     */ 
/* 196 */     this._step.translate(classGen, methodGen);
/* 197 */     LocalVariableGen stepTemp = methodGen.addLocalVariable("parent_location_path_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 201 */     stepTemp.setStart(il.append(new ASTORE(stepTemp.getIndex())));
/*     */ 
/* 204 */     int initSI = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*     */ 
/* 210 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator")));
/* 211 */     il.append(DUP);
/*     */ 
/* 213 */     pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
/* 214 */     stepTemp.setEnd(il.append(new ALOAD(stepTemp.getIndex())));
/*     */ 
/* 217 */     il.append(new INVOKESPECIAL(initSI));
/*     */ 
/* 220 */     Expression stp = this._step;
/* 221 */     if ((stp instanceof ParentLocationPath)) {
/* 222 */       stp = ((ParentLocationPath)stp).getStep();
/*     */     }
/* 224 */     if (((this._path instanceof Step)) && ((stp instanceof Step))) {
/* 225 */       int path = ((Step)this._path).getAxis();
/* 226 */       int step = ((Step)stp).getAxis();
/* 227 */       if (((path == 5) && (step == 3)) || ((path == 11) && (step == 10)))
/*     */       {
/* 229 */         int incl = cpg.addMethodref("com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 232 */         il.append(new INVOKEVIRTUAL(incl));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 242 */     if (this._orderNodes) {
/* 243 */       int order = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "orderNodes", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 246 */       il.append(methodGen.loadDOM());
/* 247 */       il.append(SWAP);
/* 248 */       il.append(methodGen.loadContextNode());
/* 249 */       il.append(new INVOKEINTERFACE(order, 3));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ParentLocationPath
 * JD-Core Version:    0.6.2
 */