/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.FilterGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NumberType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ final class Predicate extends Expression
/*     */   implements Closure
/*     */ {
/*  63 */   private Expression _exp = null;
/*     */ 
/*  70 */   private boolean _canOptimize = true;
/*     */ 
/*  76 */   private boolean _nthPositionFilter = false;
/*     */ 
/*  82 */   private boolean _nthDescendant = false;
/*     */ 
/*  87 */   int _ptype = -1;
/*     */ 
/*  92 */   private String _className = null;
/*     */ 
/*  97 */   private ArrayList _closureVars = null;
/*     */ 
/* 102 */   private Closure _parentClosure = null;
/*     */ 
/* 107 */   private Expression _value = null;
/*     */ 
/* 112 */   private Step _step = null;
/*     */ 
/*     */   public Predicate(Expression exp)
/*     */   {
/* 118 */     this._exp = exp;
/* 119 */     this._exp.setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser)
/*     */   {
/* 127 */     super.setParser(parser);
/* 128 */     this._exp.setParser(parser);
/*     */   }
/*     */ 
/*     */   public boolean isNthPositionFilter()
/*     */   {
/* 136 */     return this._nthPositionFilter;
/*     */   }
/*     */ 
/*     */   public boolean isNthDescendant()
/*     */   {
/* 144 */     return this._nthDescendant;
/*     */   }
/*     */ 
/*     */   public void dontOptimize()
/*     */   {
/* 151 */     this._canOptimize = false;
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/* 159 */     return this._exp.hasPositionCall();
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall()
/*     */   {
/* 167 */     return this._exp.hasLastCall();
/*     */   }
/*     */ 
/*     */   public boolean inInnerClass()
/*     */   {
/* 177 */     return this._className != null;
/*     */   }
/*     */ 
/*     */   public Closure getParentClosure()
/*     */   {
/* 184 */     if (this._parentClosure == null) {
/* 185 */       SyntaxTreeNode node = getParent();
/*     */       do {
/* 187 */         if ((node instanceof Closure)) {
/* 188 */           this._parentClosure = ((Closure)node);
/* 189 */           break;
/*     */         }
/* 191 */         if ((node instanceof TopLevelElement)) {
/*     */           break;
/*     */         }
/* 194 */         node = node.getParent();
/* 195 */       }while (node != null);
/*     */     }
/* 197 */     return this._parentClosure;
/*     */   }
/*     */ 
/*     */   public String getInnerClassName()
/*     */   {
/* 205 */     return this._className;
/*     */   }
/*     */ 
/*     */   public void addVariable(VariableRefBase variableRef)
/*     */   {
/* 212 */     if (this._closureVars == null) {
/* 213 */       this._closureVars = new ArrayList();
/*     */     }
/*     */ 
/* 217 */     if (!this._closureVars.contains(variableRef)) {
/* 218 */       this._closureVars.add(variableRef);
/*     */ 
/* 221 */       Closure parentClosure = getParentClosure();
/* 222 */       if (parentClosure != null)
/* 223 */         parentClosure.addVariable(variableRef);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPosType()
/*     */   {
/* 235 */     if (this._ptype == -1) {
/* 236 */       SyntaxTreeNode parent = getParent();
/* 237 */       if ((parent instanceof StepPattern)) {
/* 238 */         this._ptype = ((StepPattern)parent).getNodeType();
/*     */       }
/* 240 */       else if ((parent instanceof AbsoluteLocationPath)) {
/* 241 */         AbsoluteLocationPath path = (AbsoluteLocationPath)parent;
/* 242 */         Expression exp = path.getPath();
/* 243 */         if ((exp instanceof Step)) {
/* 244 */           this._ptype = ((Step)exp).getNodeType();
/*     */         }
/*     */       }
/* 247 */       else if ((parent instanceof VariableRefBase)) {
/* 248 */         VariableRefBase ref = (VariableRefBase)parent;
/* 249 */         VariableBase var = ref.getVariable();
/* 250 */         Expression exp = var.getExpression();
/* 251 */         if ((exp instanceof Step)) {
/* 252 */           this._ptype = ((Step)exp).getNodeType();
/*     */         }
/*     */       }
/* 255 */       else if ((parent instanceof Step)) {
/* 256 */         this._ptype = ((Step)parent).getNodeType();
/*     */       }
/*     */     }
/* 259 */     return this._ptype;
/*     */   }
/*     */ 
/*     */   public boolean parentIsPattern() {
/* 263 */     return getParent() instanceof Pattern;
/*     */   }
/*     */ 
/*     */   public Expression getExpr() {
/* 267 */     return this._exp;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 271 */     return "pred(" + this._exp + ')';
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 286 */     com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type texp = this._exp.typeCheck(stable);
/*     */ 
/* 289 */     if ((texp instanceof ReferenceType)) {
/* 290 */       this._exp = new CastExpr(this._exp, texp = com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Real);
/*     */     }
/*     */ 
/* 296 */     if ((texp instanceof ResultTreeType)) {
/* 297 */       this._exp = new CastExpr(this._exp, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean);
/* 298 */       this._exp = new CastExpr(this._exp, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Real);
/* 299 */       texp = this._exp.typeCheck(stable);
/*     */     }
/*     */ 
/* 303 */     if ((texp instanceof NumberType))
/*     */     {
/* 305 */       if (!(texp instanceof IntType)) {
/* 306 */         this._exp = new CastExpr(this._exp, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int);
/*     */       }
/*     */ 
/* 309 */       if (this._canOptimize)
/*     */       {
/* 311 */         this._nthPositionFilter = ((!this._exp.hasLastCall()) && (!this._exp.hasPositionCall()));
/*     */ 
/* 315 */         if (this._nthPositionFilter) {
/* 316 */           SyntaxTreeNode parent = getParent();
/* 317 */           this._nthDescendant = (((parent instanceof Step)) && ((parent.getParent() instanceof AbsoluteLocationPath)));
/*     */ 
/* 319 */           return this._type = com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.NodeSet;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 324 */       this._nthPositionFilter = (this._nthDescendant = 0);
/*     */ 
/* 327 */       QName position = getParser().getQNameIgnoreDefaultNs("position");
/*     */ 
/* 329 */       PositionCall positionCall = new PositionCall(position);
/*     */ 
/* 331 */       positionCall.setParser(getParser());
/* 332 */       positionCall.setParent(this);
/*     */ 
/* 334 */       this._exp = new EqualityExpr(0, positionCall, this._exp);
/*     */ 
/* 336 */       if (this._exp.typeCheck(stable) != com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean) {
/* 337 */         this._exp = new CastExpr(this._exp, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean);
/*     */       }
/* 339 */       return this._type = com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean;
/*     */     }
/*     */ 
/* 343 */     if (!(texp instanceof BooleanType)) {
/* 344 */       this._exp = new CastExpr(this._exp, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean);
/*     */     }
/* 346 */     return this._type = com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Boolean;
/*     */   }
/*     */ 
/*     */   private void compileFilter(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 362 */     this._className = getXSLTC().getHelperClassName();
/* 363 */     FilterGenerator filterGen = new FilterGenerator(this._className, "java.lang.Object", toString(), 33, new String[] { "com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListFilter" }, classGen.getStylesheet());
/*     */ 
/* 372 */     ConstantPoolGen cpg = filterGen.getConstantPool();
/* 373 */     int length = this._closureVars == null ? 0 : this._closureVars.size();
/*     */ 
/* 376 */     for (int i = 0; i < length; i++) {
/* 377 */       VariableBase var = ((VariableRefBase)this._closureVars.get(i)).getVariable();
/*     */ 
/* 379 */       filterGen.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
/*     */     }
/*     */ 
/* 385 */     InstructionList il = new InstructionList();
/* 386 */     TestGenerator testGen = new TestGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[] { com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;") }, new String[] { "node", "position", "last", "current", "translet", "iterator" }, "test", this._className, il, cpg);
/*     */ 
/* 407 */     LocalVariableGen local = testGen.addLocalVariable("document", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), null, null);
/*     */ 
/* 410 */     String className = classGen.getClassName();
/* 411 */     il.append(filterGen.loadTranslet());
/* 412 */     il.append(new CHECKCAST(cpg.addClass(className)));
/* 413 */     il.append(new GETFIELD(cpg.addFieldref(className, "_dom", "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;")));
/*     */ 
/* 415 */     local.setStart(il.append(new ASTORE(local.getIndex())));
/*     */ 
/* 418 */     testGen.setDomIndex(local.getIndex());
/*     */ 
/* 420 */     this._exp.translate(filterGen, testGen);
/* 421 */     il.append(IRETURN);
/*     */ 
/* 423 */     filterGen.addEmptyConstructor(1);
/* 424 */     filterGen.addMethod(testGen);
/*     */ 
/* 426 */     getXSLTC().dumpClass(filterGen.getJavaClass());
/*     */   }
/*     */ 
/*     */   public boolean isBooleanTest()
/*     */   {
/* 435 */     return this._exp instanceof BooleanExpr;
/*     */   }
/*     */ 
/*     */   public boolean isNodeValueTest()
/*     */   {
/* 444 */     if (!this._canOptimize) return false;
/* 445 */     return (getStep() != null) && (getCompareValue() != null);
/*     */   }
/*     */ 
/*     */   public Step getStep()
/*     */   {
/* 455 */     if (this._step != null) {
/* 456 */       return this._step;
/*     */     }
/*     */ 
/* 460 */     if (this._exp == null) {
/* 461 */       return null;
/*     */     }
/*     */ 
/* 465 */     if ((this._exp instanceof EqualityExpr)) {
/* 466 */       EqualityExpr exp = (EqualityExpr)this._exp;
/* 467 */       Expression left = exp.getLeft();
/* 468 */       Expression right = exp.getRight();
/*     */ 
/* 471 */       if ((left instanceof CastExpr)) {
/* 472 */         left = ((CastExpr)left).getExpr();
/*     */       }
/* 474 */       if ((left instanceof Step)) {
/* 475 */         this._step = ((Step)left);
/*     */       }
/*     */ 
/* 479 */       if ((right instanceof CastExpr)) {
/* 480 */         right = ((CastExpr)right).getExpr();
/*     */       }
/* 482 */       if ((right instanceof Step)) {
/* 483 */         this._step = ((Step)right);
/*     */       }
/*     */     }
/* 486 */     return this._step;
/*     */   }
/*     */ 
/*     */   public Expression getCompareValue()
/*     */   {
/* 496 */     if (this._value != null) {
/* 497 */       return this._value;
/*     */     }
/*     */ 
/* 501 */     if (this._exp == null) {
/* 502 */       return null;
/*     */     }
/*     */ 
/* 506 */     if ((this._exp instanceof EqualityExpr)) {
/* 507 */       EqualityExpr exp = (EqualityExpr)this._exp;
/* 508 */       Expression left = exp.getLeft();
/* 509 */       Expression right = exp.getRight();
/*     */ 
/* 512 */       if ((left instanceof LiteralExpr)) {
/* 513 */         this._value = left;
/* 514 */         return this._value;
/*     */       }
/*     */ 
/* 517 */       if (((left instanceof VariableRefBase)) && (left.getType() == com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String))
/*     */       {
/* 520 */         this._value = left;
/* 521 */         return this._value;
/*     */       }
/*     */ 
/* 525 */       if ((right instanceof LiteralExpr)) {
/* 526 */         this._value = right;
/* 527 */         return this._value;
/*     */       }
/*     */ 
/* 530 */       if (((right instanceof VariableRefBase)) && (right.getType() == com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String))
/*     */       {
/* 533 */         this._value = right;
/* 534 */         return this._value;
/*     */       }
/*     */     }
/* 537 */     return null;
/*     */   }
/*     */ 
/*     */   public void translateFilter(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 548 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 549 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 552 */     compileFilter(classGen, methodGen);
/*     */ 
/* 555 */     il.append(new NEW(cpg.addClass(this._className)));
/* 556 */     il.append(DUP);
/* 557 */     il.append(new INVOKESPECIAL(cpg.addMethodref(this._className, "<init>", "()V")));
/*     */ 
/* 561 */     int length = this._closureVars == null ? 0 : this._closureVars.size();
/*     */ 
/* 563 */     for (int i = 0; i < length; i++) {
/* 564 */       VariableRefBase varRef = (VariableRefBase)this._closureVars.get(i);
/* 565 */       VariableBase var = varRef.getVariable();
/* 566 */       com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type varType = var.getType();
/*     */ 
/* 568 */       il.append(DUP);
/*     */ 
/* 571 */       Closure variableClosure = this._parentClosure;
/* 572 */       while ((variableClosure != null) && 
/* 573 */         (!variableClosure.inInnerClass())) {
/* 574 */         variableClosure = variableClosure.getParentClosure();
/*     */       }
/*     */ 
/* 578 */       if (variableClosure != null) {
/* 579 */         il.append(ALOAD_0);
/* 580 */         il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), var.getEscapedName(), varType.toSignature())));
/*     */       }
/*     */       else
/*     */       {
/* 586 */         il.append(var.loadInstruction());
/*     */       }
/*     */ 
/* 590 */       il.append(new PUTFIELD(cpg.addFieldref(this._className, var.getEscapedName(), varType.toSignature())));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 604 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 605 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 607 */     if ((this._nthPositionFilter) || (this._nthDescendant)) {
/* 608 */       this._exp.translate(classGen, methodGen);
/*     */     }
/* 610 */     else if ((isNodeValueTest()) && ((getParent() instanceof Step))) {
/* 611 */       this._value.translate(classGen, methodGen);
/* 612 */       il.append(new CHECKCAST(cpg.addClass("java.lang.String")));
/* 613 */       il.append(new PUSH(cpg, ((EqualityExpr)this._exp).getOp()));
/*     */     }
/*     */     else {
/* 616 */       translateFilter(classGen, methodGen);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Predicate
 * JD-Core Version:    0.6.2
 */