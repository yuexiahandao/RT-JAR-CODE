/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ICONST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Step extends RelativeLocationPath
/*     */ {
/*     */   private int _axis;
/*     */   private Vector _predicates;
/*  72 */   private boolean _hadPredicates = false;
/*     */   private int _nodeType;
/*     */ 
/*     */   public Step(int axis, int nodeType, Vector predicates)
/*     */   {
/*  80 */     this._axis = axis;
/*  81 */     this._nodeType = nodeType;
/*  82 */     this._predicates = predicates;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser)
/*     */   {
/*  89 */     super.setParser(parser);
/*  90 */     if (this._predicates != null) {
/*  91 */       int n = this._predicates.size();
/*  92 */       for (int i = 0; i < n; i++) {
/*  93 */         Predicate exp = (Predicate)this._predicates.elementAt(i);
/*  94 */         exp.setParser(parser);
/*  95 */         exp.setParent(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 104 */     return this._axis;
/*     */   }
/*     */ 
/*     */   public void setAxis(int axis)
/*     */   {
/* 111 */     this._axis = axis;
/*     */   }
/*     */ 
/*     */   public int getNodeType()
/*     */   {
/* 118 */     return this._nodeType;
/*     */   }
/*     */ 
/*     */   public Vector getPredicates()
/*     */   {
/* 125 */     return this._predicates;
/*     */   }
/*     */ 
/*     */   public void addPredicates(Vector predicates)
/*     */   {
/* 132 */     if (this._predicates == null) {
/* 133 */       this._predicates = predicates;
/*     */     }
/*     */     else
/* 136 */       this._predicates.addAll(predicates);
/*     */   }
/*     */ 
/*     */   private boolean hasParentPattern()
/*     */   {
/* 146 */     SyntaxTreeNode parent = getParent();
/* 147 */     return ((parent instanceof ParentPattern)) || ((parent instanceof ParentLocationPath)) || ((parent instanceof UnionPathExpr)) || ((parent instanceof FilterParentPath));
/*     */   }
/*     */ 
/*     */   private boolean hasParentLocationPath()
/*     */   {
/* 157 */     return getParent() instanceof ParentLocationPath;
/*     */   }
/*     */ 
/*     */   private boolean hasPredicates()
/*     */   {
/* 164 */     return (this._predicates != null) && (this._predicates.size() > 0);
/*     */   }
/*     */ 
/*     */   private boolean isPredicate()
/*     */   {
/* 171 */     SyntaxTreeNode parent = this;
/* 172 */     while (parent != null) {
/* 173 */       parent = parent.getParent();
/* 174 */       if ((parent instanceof Predicate)) return true;
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAbbreviatedDot()
/*     */   {
/* 183 */     return (this._nodeType == -1) && (this._axis == 13);
/*     */   }
/*     */ 
/*     */   public boolean isAbbreviatedDDot()
/*     */   {
/* 191 */     return (this._nodeType == -1) && (this._axis == 10);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 203 */     this._hadPredicates = hasPredicates();
/*     */ 
/* 208 */     if (isAbbreviatedDot()) {
/* 209 */       this._type = ((hasParentPattern()) || (hasPredicates()) || (hasParentLocationPath()) ? Type.NodeSet : Type.Node);
/*     */     }
/*     */     else
/*     */     {
/* 213 */       this._type = Type.NodeSet;
/*     */     }
/*     */ 
/* 217 */     if (this._predicates != null) {
/* 218 */       int n = this._predicates.size();
/* 219 */       for (int i = 0; i < n; i++) {
/* 220 */         Expression pred = (Expression)this._predicates.elementAt(i);
/* 221 */         pred.typeCheck(stable);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 226 */     return this._type;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 237 */     translateStep(classGen, methodGen, hasPredicates() ? this._predicates.size() - 1 : -1);
/*     */   }
/*     */ 
/*     */   private void translateStep(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex)
/*     */   {
/* 243 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 244 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 246 */     if (predicateIndex >= 0) {
/* 247 */       translatePredicates(classGen, methodGen, predicateIndex);
/*     */     } else {
/* 249 */       int star = 0;
/* 250 */       String name = null;
/* 251 */       XSLTC xsltc = getParser().getXSLTC();
/*     */ 
/* 253 */       if (this._nodeType >= 14) {
/* 254 */         Vector ni = xsltc.getNamesIndex();
/*     */ 
/* 256 */         name = (String)ni.elementAt(this._nodeType - 14);
/* 257 */         star = name.lastIndexOf('*');
/*     */       }
/*     */ 
/* 262 */       if ((this._axis == 2) && (this._nodeType != 2) && (this._nodeType != -1) && (!hasParentPattern()) && (star == 0))
/*     */       {
/* 266 */         int iter = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getTypedAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 269 */         il.append(methodGen.loadDOM());
/* 270 */         il.append(new PUSH(cpg, 2));
/* 271 */         il.append(new PUSH(cpg, this._nodeType));
/* 272 */         il.append(new INVOKEINTERFACE(iter, 3));
/* 273 */         return;
/*     */       }
/*     */ 
/* 276 */       SyntaxTreeNode parent = getParent();
/*     */ 
/* 278 */       if (isAbbreviatedDot()) {
/* 279 */         if (this._type == Type.Node)
/*     */         {
/* 281 */           il.append(methodGen.loadContextNode());
/*     */         }
/* 284 */         else if ((parent instanceof ParentLocationPath))
/*     */         {
/* 286 */           int init = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator", "<init>", "(I)V");
/*     */ 
/* 289 */           il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.SingletonIterator")));
/* 290 */           il.append(DUP);
/* 291 */           il.append(methodGen.loadContextNode());
/* 292 */           il.append(new INVOKESPECIAL(init));
/*     */         }
/*     */         else {
/* 295 */           int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 298 */           il.append(methodGen.loadDOM());
/* 299 */           il.append(new PUSH(cpg, this._axis));
/* 300 */           il.append(new INVOKEINTERFACE(git, 2));
/*     */         }
/*     */ 
/* 303 */         return;
/*     */       }
/*     */ 
/* 307 */       if (((parent instanceof ParentLocationPath)) && ((parent.getParent() instanceof ParentLocationPath)))
/*     */       {
/* 309 */         if ((this._nodeType == 1) && (!this._hadPredicates)) {
/* 310 */           this._nodeType = -1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 315 */       switch (this._nodeType) {
/*     */       case 2:
/* 317 */         this._axis = 2;
/*     */       case -1:
/* 320 */         int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 323 */         il.append(methodGen.loadDOM());
/* 324 */         il.append(new PUSH(cpg, this._axis));
/* 325 */         il.append(new INVOKEINTERFACE(git, 2));
/* 326 */         break;
/*     */       case 0:
/*     */       default:
/* 328 */         if (star > 1)
/*     */         {
/*     */           String namespace;
/*     */           String namespace;
/* 330 */           if (this._axis == 2)
/* 331 */             namespace = name.substring(0, star - 2);
/*     */           else {
/* 333 */             namespace = name.substring(0, star - 1);
/*     */           }
/* 335 */           int nsType = xsltc.registerNamespace(namespace);
/* 336 */           int ns = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNamespaceAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 339 */           il.append(methodGen.loadDOM());
/* 340 */           il.append(new PUSH(cpg, this._axis));
/* 341 */           il.append(new PUSH(cpg, nsType));
/* 342 */           il.append(new INVOKEINTERFACE(ns, 3));
/* 343 */         }break;
/*     */       case 1:
/*     */       }
/*     */ 
/* 347 */       int ty = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getTypedAxisIterator", "(II)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 351 */       il.append(methodGen.loadDOM());
/* 352 */       il.append(new PUSH(cpg, this._axis));
/* 353 */       il.append(new PUSH(cpg, this._nodeType));
/* 354 */       il.append(new INVOKEINTERFACE(ty, 3));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen, int predicateIndex)
/*     */   {
/* 371 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 372 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 374 */     int idx = 0;
/*     */ 
/* 376 */     if (predicateIndex < 0) {
/* 377 */       translateStep(classGen, methodGen, predicateIndex);
/*     */     }
/*     */     else {
/* 380 */       Predicate predicate = (Predicate)this._predicates.get(predicateIndex--);
/*     */ 
/* 389 */       if (predicate.isNodeValueTest()) {
/* 390 */         Step step = predicate.getStep();
/*     */ 
/* 392 */         il.append(methodGen.loadDOM());
/*     */ 
/* 395 */         if (step.isAbbreviatedDot()) {
/* 396 */           translateStep(classGen, methodGen, predicateIndex);
/* 397 */           il.append(new ICONST(0));
/*     */         }
/*     */         else
/*     */         {
/* 402 */           ParentLocationPath path = new ParentLocationPath(this, step);
/* 403 */           this._parent = (step._parent = path);
/*     */           try
/*     */           {
/* 406 */             path.typeCheck(getParser().getSymbolTable());
/*     */           } catch (TypeCheckError e) {
/*     */           }
/* 409 */           translateStep(classGen, methodGen, predicateIndex);
/* 410 */           path.translateStep(classGen, methodGen);
/* 411 */           il.append(new ICONST(1));
/*     */         }
/* 413 */         predicate.translate(classGen, methodGen);
/* 414 */         idx = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNodeValueIterator", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;ILjava/lang/String;Z)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 417 */         il.append(new INVOKEINTERFACE(idx, 5));
/*     */       }
/* 420 */       else if (predicate.isNthDescendant()) {
/* 421 */         il.append(methodGen.loadDOM());
/*     */ 
/* 423 */         il.append(new PUSH(cpg, predicate.getPosType()));
/* 424 */         predicate.translate(classGen, methodGen);
/* 425 */         il.append(new ICONST(0));
/* 426 */         idx = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNthDescendant", "(IIZ)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 429 */         il.append(new INVOKEINTERFACE(idx, 4));
/*     */       }
/* 432 */       else if (predicate.isNthPositionFilter()) {
/* 433 */         idx = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;I)V");
/*     */ 
/* 446 */         translatePredicates(classGen, methodGen, predicateIndex);
/* 447 */         LocalVariableGen iteratorTemp = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 451 */         iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
/*     */ 
/* 454 */         predicate.translate(classGen, methodGen);
/* 455 */         LocalVariableGen predicateValueTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType("I"), null, null);
/*     */ 
/* 459 */         predicateValueTemp.setStart(il.append(new ISTORE(predicateValueTemp.getIndex())));
/*     */ 
/* 462 */         il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.NthIterator")));
/* 463 */         il.append(DUP);
/* 464 */         iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
/*     */ 
/* 466 */         predicateValueTemp.setEnd(il.append(new ILOAD(predicateValueTemp.getIndex())));
/*     */ 
/* 468 */         il.append(new INVOKESPECIAL(idx));
/*     */       }
/*     */       else {
/* 471 */         idx = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;ILcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;)V");
/*     */ 
/* 489 */         translatePredicates(classGen, methodGen, predicateIndex);
/* 490 */         LocalVariableGen iteratorTemp = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 494 */         iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
/*     */ 
/* 497 */         predicate.translateFilter(classGen, methodGen);
/* 498 */         LocalVariableGen filterTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter;"), null, null);
/*     */ 
/* 502 */         filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
/*     */ 
/* 505 */         il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListIterator")));
/* 506 */         il.append(DUP);
/*     */ 
/* 508 */         iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
/*     */ 
/* 510 */         filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
/*     */ 
/* 512 */         il.append(methodGen.loadCurrentNode());
/* 513 */         il.append(classGen.loadTranslet());
/* 514 */         if (classGen.isExternal()) {
/* 515 */           String className = classGen.getClassName();
/* 516 */           il.append(new CHECKCAST(cpg.addClass(className)));
/*     */         }
/* 518 */         il.append(new INVOKESPECIAL(idx));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 527 */     StringBuffer buffer = new StringBuffer("step(\"");
/* 528 */     buffer.append(Axis.getNames(this._axis)).append("\", ").append(this._nodeType);
/* 529 */     if (this._predicates != null) {
/* 530 */       int n = this._predicates.size();
/* 531 */       for (int i = 0; i < n; i++) {
/* 532 */         Predicate pred = (Predicate)this._predicates.elementAt(i);
/* 533 */         buffer.append(", ").append(pred.toString());
/*     */       }
/*     */     }
/* 536 */     return ')';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Step
 * JD-Core Version:    0.6.2
 */