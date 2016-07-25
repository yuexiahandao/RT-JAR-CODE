/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
/*     */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*     */ import com.sun.org.apache.bcel.internal.generic.NOP;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordFactGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Sort extends Instruction
/*     */   implements Closure
/*     */ {
/*     */   private Expression _select;
/*     */   private AttributeValue _order;
/*     */   private AttributeValue _caseOrder;
/*     */   private AttributeValue _dataType;
/*     */   private String _lang;
/*  81 */   private String _data = null;
/*     */ 
/*  84 */   private String _className = null;
/*  85 */   private ArrayList _closureVars = null;
/*  86 */   private boolean _needsSortRecordFactory = false;
/*     */ 
/*     */   public boolean inInnerClass()
/*     */   {
/*  95 */     return this._className != null;
/*     */   }
/*     */ 
/*     */   public Closure getParentClosure()
/*     */   {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   public String getInnerClassName()
/*     */   {
/* 110 */     return this._className;
/*     */   }
/*     */ 
/*     */   public void addVariable(VariableRefBase variableRef)
/*     */   {
/* 117 */     if (this._closureVars == null) {
/* 118 */       this._closureVars = new ArrayList();
/*     */     }
/*     */ 
/* 122 */     if (!this._closureVars.contains(variableRef)) {
/* 123 */       this._closureVars.add(variableRef);
/* 124 */       this._needsSortRecordFactory = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setInnerClassName(String className)
/*     */   {
/* 131 */     this._className = className;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 139 */     SyntaxTreeNode parent = getParent();
/* 140 */     if ((!(parent instanceof ApplyTemplates)) && (!(parent instanceof ForEach)))
/*     */     {
/* 142 */       reportError(this, parser, "STRAY_SORT_ERR", null);
/* 143 */       return;
/*     */     }
/*     */ 
/* 147 */     this._select = parser.parseExpression(this, "select", "string(.)");
/*     */ 
/* 150 */     String val = getAttribute("order");
/* 151 */     if (val.length() == 0) val = "ascending";
/* 152 */     this._order = AttributeValue.create(this, val, parser);
/*     */ 
/* 155 */     val = getAttribute("data-type");
/* 156 */     if (val.length() == 0) {
/*     */       try {
/* 158 */         com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type type = this._select.typeCheck(parser.getSymbolTable());
/* 159 */         if ((type instanceof IntType))
/* 160 */           val = "number";
/*     */         else
/* 162 */           val = "text";
/*     */       }
/*     */       catch (TypeCheckError e) {
/* 165 */         val = "text";
/*     */       }
/*     */     }
/* 168 */     this._dataType = AttributeValue.create(this, val, parser);
/*     */ 
/* 170 */     this._lang = getAttribute("lang");
/*     */ 
/* 174 */     val = getAttribute("case-order");
/* 175 */     this._caseOrder = AttributeValue.create(this, val, parser);
/*     */   }
/*     */ 
/*     */   public com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 184 */     com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type tselect = this._select.typeCheck(stable);
/*     */ 
/* 188 */     if (!(tselect instanceof StringType)) {
/* 189 */       this._select = new CastExpr(this._select, com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String);
/*     */     }
/*     */ 
/* 192 */     this._order.typeCheck(stable);
/* 193 */     this._caseOrder.typeCheck(stable);
/* 194 */     this._dataType.typeCheck(stable);
/* 195 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Void;
/*     */   }
/*     */ 
/*     */   public void translateSortType(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 204 */     this._dataType.translate(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateSortOrder(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 209 */     this._order.translate(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateCaseOrder(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 214 */     this._caseOrder.translate(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateLang(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 219 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 220 */     InstructionList il = methodGen.getInstructionList();
/* 221 */     il.append(new PUSH(cpg, this._lang));
/*     */   }
/*     */ 
/*     */   public void translateSelect(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 231 */     this._select.translate(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void translateSortIterator(ClassGenerator classGen, MethodGenerator methodGen, Expression nodeSet, Vector sortObjects)
/*     */   {
/* 251 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 252 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 255 */     int init = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator", "<init>", "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory;)V");
/*     */ 
/* 270 */     LocalVariableGen nodesTemp = methodGen.addLocalVariable("sort_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
/*     */ 
/* 275 */     LocalVariableGen sortRecordFactoryTemp = methodGen.addLocalVariable("sort_tmp2", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory;"), null, null);
/*     */ 
/* 281 */     if (nodeSet == null) {
/* 282 */       int children = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 286 */       il.append(methodGen.loadDOM());
/* 287 */       il.append(new PUSH(cpg, 3));
/* 288 */       il.append(new INVOKEINTERFACE(children, 2));
/*     */     }
/*     */     else {
/* 291 */       nodeSet.translate(classGen, methodGen);
/*     */     }
/*     */ 
/* 294 */     nodesTemp.setStart(il.append(new ASTORE(nodesTemp.getIndex())));
/*     */ 
/* 298 */     compileSortRecordFactory(sortObjects, classGen, methodGen);
/* 299 */     sortRecordFactoryTemp.setStart(il.append(new ASTORE(sortRecordFactoryTemp.getIndex())));
/*     */ 
/* 302 */     il.append(new NEW(cpg.addClass("com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator")));
/* 303 */     il.append(DUP);
/* 304 */     nodesTemp.setEnd(il.append(new ALOAD(nodesTemp.getIndex())));
/* 305 */     sortRecordFactoryTemp.setEnd(il.append(new ALOAD(sortRecordFactoryTemp.getIndex())));
/*     */ 
/* 307 */     il.append(new INVOKESPECIAL(init));
/*     */   }
/*     */ 
/*     */   public static void compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 318 */     String sortRecordClass = compileSortRecord(sortObjects, classGen, methodGen);
/*     */ 
/* 321 */     boolean needsSortRecordFactory = false;
/* 322 */     int nsorts = sortObjects.size();
/* 323 */     for (int i = 0; i < nsorts; i++) {
/* 324 */       Sort sort = (Sort)sortObjects.elementAt(i);
/* 325 */       needsSortRecordFactory |= sort._needsSortRecordFactory;
/*     */     }
/*     */ 
/* 328 */     String sortRecordFactoryClass = "com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory";
/* 329 */     if (needsSortRecordFactory) {
/* 330 */       sortRecordFactoryClass = compileSortRecordFactory(sortObjects, classGen, methodGen, sortRecordClass);
/*     */     }
/*     */ 
/* 335 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 336 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 348 */     LocalVariableGen sortOrderTemp = methodGen.addLocalVariable("sort_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
/*     */ 
/* 352 */     il.append(new PUSH(cpg, nsorts));
/* 353 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/* 354 */     for (int level = 0; level < nsorts; level++) {
/* 355 */       Sort sort = (Sort)sortObjects.elementAt(level);
/* 356 */       il.append(DUP);
/* 357 */       il.append(new PUSH(cpg, level));
/* 358 */       sort.translateSortOrder(classGen, methodGen);
/* 359 */       il.append(AASTORE);
/*     */     }
/* 361 */     sortOrderTemp.setStart(il.append(new ASTORE(sortOrderTemp.getIndex())));
/*     */ 
/* 363 */     LocalVariableGen sortTypeTemp = methodGen.addLocalVariable("sort_type_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
/*     */ 
/* 367 */     il.append(new PUSH(cpg, nsorts));
/* 368 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/* 369 */     for (int level = 0; level < nsorts; level++) {
/* 370 */       Sort sort = (Sort)sortObjects.elementAt(level);
/* 371 */       il.append(DUP);
/* 372 */       il.append(new PUSH(cpg, level));
/* 373 */       sort.translateSortType(classGen, methodGen);
/* 374 */       il.append(AASTORE);
/*     */     }
/* 376 */     sortTypeTemp.setStart(il.append(new ASTORE(sortTypeTemp.getIndex())));
/*     */ 
/* 378 */     LocalVariableGen sortLangTemp = methodGen.addLocalVariable("sort_lang_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
/*     */ 
/* 382 */     il.append(new PUSH(cpg, nsorts));
/* 383 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/* 384 */     for (int level = 0; level < nsorts; level++) {
/* 385 */       Sort sort = (Sort)sortObjects.elementAt(level);
/* 386 */       il.append(DUP);
/* 387 */       il.append(new PUSH(cpg, level));
/* 388 */       sort.translateLang(classGen, methodGen);
/* 389 */       il.append(AASTORE);
/*     */     }
/* 391 */     sortLangTemp.setStart(il.append(new ASTORE(sortLangTemp.getIndex())));
/*     */ 
/* 393 */     LocalVariableGen sortCaseOrderTemp = methodGen.addLocalVariable("sort_case_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
/*     */ 
/* 397 */     il.append(new PUSH(cpg, nsorts));
/* 398 */     il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
/* 399 */     for (int level = 0; level < nsorts; level++) {
/* 400 */       Sort sort = (Sort)sortObjects.elementAt(level);
/* 401 */       il.append(DUP);
/* 402 */       il.append(new PUSH(cpg, level));
/* 403 */       sort.translateCaseOrder(classGen, methodGen);
/* 404 */       il.append(AASTORE);
/*     */     }
/* 406 */     sortCaseOrderTemp.setStart(il.append(new ASTORE(sortCaseOrderTemp.getIndex())));
/*     */ 
/* 409 */     il.append(new NEW(cpg.addClass(sortRecordFactoryClass)));
/* 410 */     il.append(DUP);
/* 411 */     il.append(methodGen.loadDOM());
/* 412 */     il.append(new PUSH(cpg, sortRecordClass));
/* 413 */     il.append(classGen.loadTranslet());
/*     */ 
/* 415 */     sortOrderTemp.setEnd(il.append(new ALOAD(sortOrderTemp.getIndex())));
/* 416 */     sortTypeTemp.setEnd(il.append(new ALOAD(sortTypeTemp.getIndex())));
/* 417 */     sortLangTemp.setEnd(il.append(new ALOAD(sortLangTemp.getIndex())));
/* 418 */     sortCaseOrderTemp.setEnd(il.append(new ALOAD(sortCaseOrderTemp.getIndex())));
/*     */ 
/* 421 */     il.append(new INVOKESPECIAL(cpg.addMethodref(sortRecordFactoryClass, "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
/*     */ 
/* 432 */     ArrayList dups = new ArrayList();
/*     */ 
/* 434 */     for (int j = 0; j < nsorts; j++) {
/* 435 */       Sort sort = (Sort)sortObjects.get(j);
/* 436 */       int length = sort._closureVars == null ? 0 : sort._closureVars.size();
/*     */ 
/* 439 */       for (int i = 0; i < length; i++) {
/* 440 */         VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
/*     */ 
/* 443 */         if (!dups.contains(varRef))
/*     */         {
/* 445 */           VariableBase var = varRef.getVariable();
/*     */ 
/* 448 */           il.append(DUP);
/* 449 */           il.append(var.loadInstruction());
/* 450 */           il.append(new PUTFIELD(cpg.addFieldref(sortRecordFactoryClass, var.getEscapedName(), var.getType().toSignature())));
/*     */ 
/* 453 */           dups.add(varRef);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen, String sortRecordClass)
/*     */   {
/* 462 */     XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
/* 463 */     String className = xsltc.getHelperClassName();
/*     */ 
/* 465 */     NodeSortRecordFactGenerator sortRecordFactory = new NodeSortRecordFactGenerator(className, "com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory", className + ".java", 49, new String[0], classGen.getStylesheet());
/*     */ 
/* 473 */     ConstantPoolGen cpg = sortRecordFactory.getConstantPool();
/*     */ 
/* 476 */     int nsorts = sortObjects.size();
/* 477 */     ArrayList dups = new ArrayList();
/*     */ 
/* 479 */     for (int j = 0; j < nsorts; j++) {
/* 480 */       Sort sort = (Sort)sortObjects.get(j);
/* 481 */       int length = sort._closureVars == null ? 0 : sort._closureVars.size();
/*     */ 
/* 484 */       for (int i = 0; i < length; i++) {
/* 485 */         VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
/*     */ 
/* 488 */         if (!dups.contains(varRef))
/*     */         {
/* 490 */           VariableBase var = varRef.getVariable();
/* 491 */           sortRecordFactory.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
/*     */ 
/* 495 */           dups.add(varRef);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 500 */     com.sun.org.apache.bcel.internal.generic.Type[] argTypes = new com.sun.org.apache.bcel.internal.generic.Type[7];
/*     */ 
/* 502 */     argTypes[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/* 503 */     argTypes[1] = Util.getJCRefType("Ljava/lang/String;");
/* 504 */     argTypes[2] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/Translet;");
/* 505 */     argTypes[3] = Util.getJCRefType("[Ljava/lang/String;");
/* 506 */     argTypes[4] = Util.getJCRefType("[Ljava/lang/String;");
/* 507 */     argTypes[5] = Util.getJCRefType("[Ljava/lang/String;");
/* 508 */     argTypes[6] = Util.getJCRefType("[Ljava/lang/String;");
/*     */ 
/* 510 */     String[] argNames = new String[7];
/* 511 */     argNames[0] = "document";
/* 512 */     argNames[1] = "className";
/* 513 */     argNames[2] = "translet";
/* 514 */     argNames[3] = "order";
/* 515 */     argNames[4] = "type";
/* 516 */     argNames[5] = "lang";
/* 517 */     argNames[6] = "case_order";
/*     */ 
/* 520 */     InstructionList il = new InstructionList();
/* 521 */     MethodGenerator constructor = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, "<init>", className, il, cpg);
/*     */ 
/* 528 */     il.append(ALOAD_0);
/* 529 */     il.append(ALOAD_1);
/* 530 */     il.append(ALOAD_2);
/* 531 */     il.append(new ALOAD(3));
/* 532 */     il.append(new ALOAD(4));
/* 533 */     il.append(new ALOAD(5));
/* 534 */     il.append(new ALOAD(6));
/* 535 */     il.append(new ALOAD(7));
/* 536 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory", "<init>", "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
/*     */ 
/* 545 */     il.append(RETURN);
/*     */ 
/* 548 */     il = new InstructionList();
/* 549 */     MethodGenerator makeNodeSortRecord = new MethodGenerator(1, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecord;"), new com.sun.org.apache.bcel.internal.generic.Type[] { com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "node", "last" }, "makeNodeSortRecord", className, il, cpg);
/*     */ 
/* 558 */     il.append(ALOAD_0);
/* 559 */     il.append(ILOAD_1);
/* 560 */     il.append(ILOAD_2);
/* 561 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory", "makeNodeSortRecord", "(II)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecord;")));
/*     */ 
/* 563 */     il.append(DUP);
/* 564 */     il.append(new CHECKCAST(cpg.addClass(sortRecordClass)));
/*     */ 
/* 567 */     int ndups = dups.size();
/* 568 */     for (int i = 0; i < ndups; i++) {
/* 569 */       VariableRefBase varRef = (VariableRefBase)dups.get(i);
/* 570 */       VariableBase var = varRef.getVariable();
/* 571 */       com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type varType = var.getType();
/*     */ 
/* 573 */       il.append(DUP);
/*     */ 
/* 576 */       il.append(ALOAD_0);
/* 577 */       il.append(new GETFIELD(cpg.addFieldref(className, var.getEscapedName(), varType.toSignature())));
/*     */ 
/* 582 */       il.append(new PUTFIELD(cpg.addFieldref(sortRecordClass, var.getEscapedName(), varType.toSignature())));
/*     */     }
/*     */ 
/* 586 */     il.append(POP);
/* 587 */     il.append(ARETURN);
/*     */ 
/* 589 */     constructor.setMaxLocals();
/* 590 */     constructor.setMaxStack();
/* 591 */     sortRecordFactory.addMethod(constructor);
/* 592 */     makeNodeSortRecord.setMaxLocals();
/* 593 */     makeNodeSortRecord.setMaxStack();
/* 594 */     sortRecordFactory.addMethod(makeNodeSortRecord);
/* 595 */     xsltc.dumpClass(sortRecordFactory.getJavaClass());
/*     */ 
/* 597 */     return className;
/*     */   }
/*     */ 
/*     */   private static String compileSortRecord(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 606 */     XSLTC xsltc = ((Sort)sortObjects.firstElement()).getXSLTC();
/* 607 */     String className = xsltc.getHelperClassName();
/*     */ 
/* 610 */     NodeSortRecordGenerator sortRecord = new NodeSortRecordGenerator(className, "com.sun.org.apache.xalan.internal.xsltc.dom.NodeSortRecord", "sort$0.java", 49, new String[0], classGen.getStylesheet());
/*     */ 
/* 618 */     ConstantPoolGen cpg = sortRecord.getConstantPool();
/*     */ 
/* 621 */     int nsorts = sortObjects.size();
/* 622 */     ArrayList dups = new ArrayList();
/*     */ 
/* 624 */     for (int j = 0; j < nsorts; j++) {
/* 625 */       Sort sort = (Sort)sortObjects.get(j);
/*     */ 
/* 628 */       sort.setInnerClassName(className);
/*     */ 
/* 630 */       int length = sort._closureVars == null ? 0 : sort._closureVars.size();
/*     */ 
/* 632 */       for (int i = 0; i < length; i++) {
/* 633 */         VariableRefBase varRef = (VariableRefBase)sort._closureVars.get(i);
/*     */ 
/* 636 */         if (!dups.contains(varRef))
/*     */         {
/* 638 */           VariableBase var = varRef.getVariable();
/* 639 */           sortRecord.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
/*     */ 
/* 643 */           dups.add(varRef);
/*     */         }
/*     */       }
/*     */     }
/* 647 */     MethodGenerator init = compileInit(sortObjects, sortRecord, cpg, className);
/*     */ 
/* 649 */     MethodGenerator extract = compileExtract(sortObjects, sortRecord, cpg, className);
/*     */ 
/* 651 */     sortRecord.addMethod(init);
/* 652 */     sortRecord.addMethod(extract);
/*     */ 
/* 654 */     xsltc.dumpClass(sortRecord.getJavaClass());
/* 655 */     return className;
/*     */   }
/*     */ 
/*     */   private static MethodGenerator compileInit(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className)
/*     */   {
/* 668 */     InstructionList il = new InstructionList();
/* 669 */     MethodGenerator init = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, "<init>", className, il, cpg);
/*     */ 
/* 676 */     il.append(ALOAD_0);
/* 677 */     il.append(new INVOKESPECIAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.dom.NodeSortRecord", "<init>", "()V")));
/*     */ 
/* 682 */     il.append(RETURN);
/*     */ 
/* 684 */     return init;
/*     */   }
/*     */ 
/*     */   private static MethodGenerator compileExtract(Vector sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className)
/*     */   {
/* 695 */     InstructionList il = new InstructionList();
/*     */ 
/* 698 */     CompareGenerator extractMethod = new CompareGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.STRING, new com.sun.org.apache.bcel.internal.generic.Type[] { Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;"), com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), com.sun.org.apache.bcel.internal.generic.Type.INT }, new String[] { "dom", "current", "level", "translet", "last" }, "extractValueFromDOM", className, il, cpg);
/*     */ 
/* 717 */     int levels = sortObjects.size();
/* 718 */     int[] match = new int[levels];
/* 719 */     InstructionHandle[] target = new InstructionHandle[levels];
/* 720 */     InstructionHandle tblswitch = null;
/*     */ 
/* 723 */     if (levels > 1)
/*     */     {
/* 725 */       il.append(new ILOAD(extractMethod.getLocalIndex("level")));
/*     */ 
/* 727 */       tblswitch = il.append(new NOP());
/*     */     }
/*     */ 
/* 731 */     for (int level = 0; level < levels; level++) {
/* 732 */       match[level] = level;
/* 733 */       Sort sort = (Sort)sortObjects.elementAt(level);
/* 734 */       target[level] = il.append(NOP);
/* 735 */       sort.translateSelect(sortRecord, extractMethod);
/* 736 */       il.append(ARETURN);
/*     */     }
/*     */ 
/* 740 */     if (levels > 1)
/*     */     {
/* 742 */       InstructionHandle defaultTarget = il.append(new PUSH(cpg, ""));
/*     */ 
/* 744 */       il.insert(tblswitch, new TABLESWITCH(match, target, defaultTarget));
/* 745 */       il.append(ARETURN);
/*     */     }
/*     */ 
/* 748 */     return extractMethod;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Sort
 * JD-Core Version:    0.6.2
 */