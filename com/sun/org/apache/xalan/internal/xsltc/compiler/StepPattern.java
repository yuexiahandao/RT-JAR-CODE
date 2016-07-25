/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO_W;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class StepPattern extends RelativePathPattern
/*     */ {
/*     */   private static final int NO_CONTEXT = 0;
/*     */   private static final int SIMPLE_CONTEXT = 1;
/*     */   private static final int GENERAL_CONTEXT = 2;
/*     */   protected final int _axis;
/*     */   protected final int _nodeType;
/*     */   protected Vector _predicates;
/*  75 */   private Step _step = null;
/*  76 */   private boolean _isEpsilon = false;
/*     */   private int _contextCase;
/*  79 */   private double _priority = 1.7976931348623157E+308D;
/*     */ 
/*     */   public StepPattern(int axis, int nodeType, Vector predicates) {
/*  82 */     this._axis = axis;
/*  83 */     this._nodeType = nodeType;
/*  84 */     this._predicates = predicates;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  88 */     super.setParser(parser);
/*  89 */     if (this._predicates != null) {
/*  90 */       int n = this._predicates.size();
/*  91 */       for (int i = 0; i < n; i++) {
/*  92 */         Predicate exp = (Predicate)this._predicates.elementAt(i);
/*  93 */         exp.setParser(parser);
/*  94 */         exp.setParent(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNodeType() {
/* 100 */     return this._nodeType;
/*     */   }
/*     */ 
/*     */   public void setPriority(double priority) {
/* 104 */     this._priority = priority;
/*     */   }
/*     */ 
/*     */   public StepPattern getKernelPattern() {
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isWildcard() {
/* 112 */     return (this._isEpsilon) && (!hasPredicates());
/*     */   }
/*     */ 
/*     */   public StepPattern setPredicates(Vector predicates) {
/* 116 */     this._predicates = predicates;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   protected boolean hasPredicates() {
/* 121 */     return (this._predicates != null) && (this._predicates.size() > 0);
/*     */   }
/*     */ 
/*     */   public double getDefaultPriority() {
/* 125 */     if (this._priority != 1.7976931348623157E+308D) {
/* 126 */       return this._priority;
/*     */     }
/*     */ 
/* 129 */     if (hasPredicates()) {
/* 130 */       return 0.5D;
/*     */     }
/*     */ 
/* 133 */     switch (this._nodeType) {
/*     */     case -1:
/* 135 */       return -0.5D;
/*     */     case 0:
/* 137 */       return 0.0D;
/*     */     }
/* 139 */     return this._nodeType >= 14 ? 0.0D : -0.5D;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 145 */     return this._axis;
/*     */   }
/*     */ 
/*     */   public void reduceKernelPattern() {
/* 149 */     this._isEpsilon = true;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 153 */     StringBuffer buffer = new StringBuffer("stepPattern(\"");
/* 154 */     buffer.append(Axis.getNames(this._axis)).append("\", ").append(this._isEpsilon ? "epsilon{" + Integer.toString(this._nodeType) + "}" : Integer.toString(this._nodeType));
/*     */ 
/* 159 */     if (this._predicates != null)
/* 160 */       buffer.append(", ").append(this._predicates.toString());
/* 161 */     return ')';
/*     */   }
/*     */ 
/*     */   private int analyzeCases() {
/* 165 */     boolean noContext = true;
/* 166 */     int n = this._predicates.size();
/*     */ 
/* 168 */     for (int i = 0; (i < n) && (noContext); i++) {
/* 169 */       Predicate pred = (Predicate)this._predicates.elementAt(i);
/* 170 */       if ((pred.isNthPositionFilter()) || (pred.hasPositionCall()) || (pred.hasLastCall()))
/*     */       {
/* 174 */         noContext = false;
/*     */       }
/*     */     }
/*     */ 
/* 178 */     if (noContext) {
/* 179 */       return 0;
/*     */     }
/* 181 */     if (n == 1) {
/* 182 */       return 1;
/*     */     }
/* 184 */     return 2;
/*     */   }
/*     */ 
/*     */   private String getNextFieldName() {
/* 188 */     return "__step_pattern_iter_" + getXSLTC().nextStepPatternSerial();
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 192 */     if (hasPredicates())
/*     */     {
/* 194 */       int n = this._predicates.size();
/* 195 */       for (int i = 0; i < n; i++) {
/* 196 */         Predicate pred = (Predicate)this._predicates.elementAt(i);
/* 197 */         pred.typeCheck(stable);
/*     */       }
/*     */ 
/* 201 */       this._contextCase = analyzeCases();
/*     */ 
/* 203 */       Step step = null;
/*     */ 
/* 206 */       if (this._contextCase == 1) {
/* 207 */         Predicate pred = (Predicate)this._predicates.elementAt(0);
/* 208 */         if (pred.isNthPositionFilter()) {
/* 209 */           this._contextCase = 2;
/* 210 */           step = new Step(this._axis, this._nodeType, this._predicates);
/*     */         } else {
/* 212 */           step = new Step(this._axis, this._nodeType, null);
/*     */         }
/* 214 */       } else if (this._contextCase == 2) {
/* 215 */         int len = this._predicates.size();
/* 216 */         for (int i = 0; i < len; i++) {
/* 217 */           ((Predicate)this._predicates.elementAt(i)).dontOptimize();
/*     */         }
/*     */ 
/* 220 */         step = new Step(this._axis, this._nodeType, this._predicates);
/*     */       }
/*     */ 
/* 223 */       if (step != null) {
/* 224 */         step.setParser(getParser());
/* 225 */         step.typeCheck(stable);
/* 226 */         this._step = step;
/*     */       }
/*     */     }
/* 229 */     return this._axis == 3 ? Type.Element : Type.Attribute;
/*     */   }
/*     */ 
/*     */   private void translateKernel(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 234 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 235 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 237 */     if (this._nodeType == 1) {
/* 238 */       int check = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "isElement", "(I)Z");
/*     */ 
/* 240 */       il.append(methodGen.loadDOM());
/* 241 */       il.append(SWAP);
/* 242 */       il.append(new INVOKEINTERFACE(check, 2));
/*     */ 
/* 245 */       BranchHandle icmp = il.append(new IFNE(null));
/* 246 */       this._falseList.add(il.append(new GOTO_W(null)));
/* 247 */       icmp.setTarget(il.append(NOP));
/*     */     }
/* 249 */     else if (this._nodeType == 2) {
/* 250 */       int check = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "isAttribute", "(I)Z");
/*     */ 
/* 252 */       il.append(methodGen.loadDOM());
/* 253 */       il.append(SWAP);
/* 254 */       il.append(new INVOKEINTERFACE(check, 2));
/*     */ 
/* 257 */       BranchHandle icmp = il.append(new IFNE(null));
/* 258 */       this._falseList.add(il.append(new GOTO_W(null)));
/* 259 */       icmp.setTarget(il.append(NOP));
/*     */     }
/*     */     else
/*     */     {
/* 263 */       int getEType = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*     */ 
/* 266 */       il.append(methodGen.loadDOM());
/* 267 */       il.append(SWAP);
/* 268 */       il.append(new INVOKEINTERFACE(getEType, 2));
/* 269 */       il.append(new PUSH(cpg, this._nodeType));
/*     */ 
/* 272 */       BranchHandle icmp = il.append(new IF_ICMPEQ(null));
/* 273 */       this._falseList.add(il.append(new GOTO_W(null)));
/* 274 */       icmp.setTarget(il.append(NOP));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void translateNoContext(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 280 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 281 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 284 */     il.append(methodGen.loadCurrentNode());
/* 285 */     il.append(SWAP);
/*     */ 
/* 288 */     il.append(methodGen.storeCurrentNode());
/*     */ 
/* 291 */     if (!this._isEpsilon) {
/* 292 */       il.append(methodGen.loadCurrentNode());
/* 293 */       translateKernel(classGen, methodGen);
/*     */     }
/*     */ 
/* 297 */     int n = this._predicates.size();
/* 298 */     for (int i = 0; i < n; i++) {
/* 299 */       Predicate pred = (Predicate)this._predicates.elementAt(i);
/* 300 */       Expression exp = pred.getExpr();
/* 301 */       exp.translateDesynthesized(classGen, methodGen);
/* 302 */       this._trueList.append(exp._trueList);
/* 303 */       this._falseList.append(exp._falseList);
/*     */     }
/*     */ 
/* 308 */     InstructionHandle restore = il.append(methodGen.storeCurrentNode());
/* 309 */     backPatchTrueList(restore);
/* 310 */     BranchHandle skipFalse = il.append(new GOTO(null));
/*     */ 
/* 313 */     restore = il.append(methodGen.storeCurrentNode());
/* 314 */     backPatchFalseList(restore);
/* 315 */     this._falseList.add(il.append(new GOTO(null)));
/*     */ 
/* 318 */     skipFalse.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   private void translateSimpleContext(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 324 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 325 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 329 */     LocalVariableGen match = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
/*     */ 
/* 332 */     match.setStart(il.append(new ISTORE(match.getIndex())));
/*     */ 
/* 335 */     if (!this._isEpsilon) {
/* 336 */       il.append(new ILOAD(match.getIndex()));
/* 337 */       translateKernel(classGen, methodGen);
/*     */     }
/*     */ 
/* 341 */     il.append(methodGen.loadCurrentNode());
/* 342 */     il.append(methodGen.loadIterator());
/*     */ 
/* 345 */     int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.MatchingIterator", "<init>", "(ILcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
/*     */ 
/* 357 */     this._step.translate(classGen, methodGen);
/* 358 */     LocalVariableGen stepIteratorTemp = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 362 */     stepIteratorTemp.setStart(il.append(new ASTORE(stepIteratorTemp.getIndex())));
/*     */ 
/* 365 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.MatchingIterator")));
/* 366 */     il.append(DUP);
/* 367 */     il.append(new ILOAD(match.getIndex()));
/* 368 */     stepIteratorTemp.setEnd(il.append(new ALOAD(stepIteratorTemp.getIndex())));
/*     */ 
/* 370 */     il.append(new INVOKESPECIAL(index));
/*     */ 
/* 373 */     il.append(methodGen.loadDOM());
/* 374 */     il.append(new ILOAD(match.getIndex()));
/* 375 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getParent", "(I)I");
/* 376 */     il.append(new INVOKEINTERFACE(index, 2));
/*     */ 
/* 379 */     il.append(methodGen.setStartNode());
/*     */ 
/* 382 */     il.append(methodGen.storeIterator());
/* 383 */     match.setEnd(il.append(new ILOAD(match.getIndex())));
/* 384 */     il.append(methodGen.storeCurrentNode());
/*     */ 
/* 387 */     Predicate pred = (Predicate)this._predicates.elementAt(0);
/* 388 */     Expression exp = pred.getExpr();
/* 389 */     exp.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 392 */     InstructionHandle restore = il.append(methodGen.storeIterator());
/* 393 */     il.append(methodGen.storeCurrentNode());
/* 394 */     exp.backPatchTrueList(restore);
/* 395 */     BranchHandle skipFalse = il.append(new GOTO(null));
/*     */ 
/* 398 */     restore = il.append(methodGen.storeIterator());
/* 399 */     il.append(methodGen.storeCurrentNode());
/* 400 */     exp.backPatchFalseList(restore);
/* 401 */     this._falseList.add(il.append(new GOTO(null)));
/*     */ 
/* 404 */     skipFalse.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   private void translateGeneralContext(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 409 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 410 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 412 */     int iteratorIndex = 0;
/* 413 */     BranchHandle ifBlock = null;
/*     */ 
/* 415 */     String iteratorName = getNextFieldName();
/*     */ 
/* 418 */     LocalVariableGen node = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
/*     */ 
/* 421 */     node.setStart(il.append(new ISTORE(node.getIndex())));
/*     */ 
/* 424 */     LocalVariableGen iter = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 429 */     if (!classGen.isExternal()) {
/* 430 */       Field iterator = new Field(2, cpg.addUtf8(iteratorName), cpg.addUtf8("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, cpg.getConstantPool());
/*     */ 
/* 435 */       classGen.addField(iterator);
/* 436 */       iteratorIndex = cpg.addFieldref(classGen.getClassName(), iteratorName, "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 440 */       il.append(classGen.loadTranslet());
/* 441 */       il.append(new GETFIELD(iteratorIndex));
/* 442 */       il.append(DUP);
/* 443 */       iter.setStart(il.append(new ASTORE(iter.getIndex())));
/* 444 */       ifBlock = il.append(new IFNONNULL(null));
/* 445 */       il.append(classGen.loadTranslet());
/*     */     }
/*     */ 
/* 449 */     this._step.translate(classGen, methodGen);
/* 450 */     InstructionHandle iterStore = il.append(new ASTORE(iter.getIndex()));
/*     */ 
/* 453 */     if (!classGen.isExternal()) {
/* 454 */       il.append(new ALOAD(iter.getIndex()));
/* 455 */       il.append(new PUTFIELD(iteratorIndex));
/* 456 */       ifBlock.setTarget(il.append(NOP));
/*     */     }
/*     */     else
/*     */     {
/* 460 */       iter.setStart(iterStore);
/*     */     }
/*     */ 
/* 464 */     il.append(methodGen.loadDOM());
/* 465 */     il.append(new ILOAD(node.getIndex()));
/* 466 */     int index = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getParent", "(I)I");
/*     */ 
/* 468 */     il.append(new INVOKEINTERFACE(index, 2));
/*     */ 
/* 471 */     il.append(new ALOAD(iter.getIndex()));
/* 472 */     il.append(SWAP);
/* 473 */     il.append(methodGen.setStartNode());
/*     */ 
/* 485 */     LocalVariableGen node2 = methodGen.addLocalVariable("step_pattern_tmp3", Util.getJCRefType("I"), null, null);
/*     */ 
/* 489 */     BranchHandle skipNext = il.append(new GOTO(null));
/* 490 */     InstructionHandle next = il.append(new ALOAD(iter.getIndex()));
/* 491 */     node2.setStart(next);
/* 492 */     InstructionHandle begin = il.append(methodGen.nextNode());
/* 493 */     il.append(DUP);
/* 494 */     il.append(new ISTORE(node2.getIndex()));
/* 495 */     this._falseList.add(il.append(new IFLT(null)));
/*     */ 
/* 497 */     il.append(new ILOAD(node2.getIndex()));
/* 498 */     il.append(new ILOAD(node.getIndex()));
/* 499 */     iter.setEnd(il.append(new IF_ICMPLT(next)));
/*     */ 
/* 501 */     node2.setEnd(il.append(new ILOAD(node2.getIndex())));
/* 502 */     node.setEnd(il.append(new ILOAD(node.getIndex())));
/* 503 */     this._falseList.add(il.append(new IF_ICMPNE(null)));
/*     */ 
/* 505 */     skipNext.setTarget(begin);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 509 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 510 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 512 */     if (hasPredicates()) {
/* 513 */       switch (this._contextCase) {
/*     */       case 0:
/* 515 */         translateNoContext(classGen, methodGen);
/* 516 */         break;
/*     */       case 1:
/* 519 */         translateSimpleContext(classGen, methodGen);
/* 520 */         break;
/*     */       default:
/* 523 */         translateGeneralContext(classGen, methodGen);
/* 524 */         break;
/*     */       }
/*     */     }
/* 527 */     else if (isWildcard()) {
/* 528 */       il.append(POP);
/*     */     }
/*     */     else
/* 531 */       translateKernel(classGen, methodGen);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.StepPattern
 * JD-Core Version:    0.6.2
 */