/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*      */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.DUP;
/*      */ import com.sun.org.apache.bcel.internal.generic.GOTO_W;
/*      */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*      */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*      */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.SWITCH;
/*      */ import com.sun.org.apache.bcel.internal.generic.TargetLostException;
/*      */ import com.sun.org.apache.bcel.internal.generic.Type;
/*      */ import com.sun.org.apache.bcel.internal.util.InstructionFinder;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Vector;
/*      */ 
/*      */ final class Mode
/*      */   implements Constants
/*      */ {
/*      */   private final QName _name;
/*      */   private final Stylesheet _stylesheet;
/*      */   private final String _methodName;
/*      */   private Vector _templates;
/*   90 */   private Vector _childNodeGroup = null;
/*      */ 
/*   95 */   private TestSeq _childNodeTestSeq = null;
/*      */ 
/*  100 */   private Vector _attribNodeGroup = null;
/*      */ 
/*  105 */   private TestSeq _attribNodeTestSeq = null;
/*      */ 
/*  110 */   private Vector _idxGroup = null;
/*      */ 
/*  115 */   private TestSeq _idxTestSeq = null;
/*      */   private Vector[] _patternGroups;
/*      */   private TestSeq[] _testSeq;
/*  131 */   private Hashtable _neededTemplates = new Hashtable();
/*      */ 
/*  136 */   private Hashtable _namedTemplates = new Hashtable();
/*      */ 
/*  141 */   private Hashtable _templateIHs = new Hashtable();
/*      */ 
/*  146 */   private Hashtable _templateILs = new Hashtable();
/*      */ 
/*  151 */   private LocationPathPattern _rootPattern = null;
/*      */ 
/*  157 */   private Hashtable _importLevels = null;
/*      */ 
/*  162 */   private Hashtable _keys = null;
/*      */   private int _currentIndex;
/*      */ 
/*      */   public Mode(QName name, Stylesheet stylesheet, String suffix)
/*      */   {
/*  178 */     this._name = name;
/*  179 */     this._stylesheet = stylesheet;
/*  180 */     this._methodName = ("applyTemplates" + suffix);
/*  181 */     this._templates = new Vector();
/*  182 */     this._patternGroups = new Vector[32];
/*      */   }
/*      */ 
/*      */   public String functionName()
/*      */   {
/*  193 */     return this._methodName;
/*      */   }
/*      */ 
/*      */   public String functionName(int min, int max) {
/*  197 */     if (this._importLevels == null) {
/*  198 */       this._importLevels = new Hashtable();
/*      */     }
/*  200 */     this._importLevels.put(new Integer(max), new Integer(min));
/*  201 */     return this._methodName + '_' + max;
/*      */   }
/*      */ 
/*      */   private String getClassName()
/*      */   {
/*  208 */     return this._stylesheet.getClassName();
/*      */   }
/*      */ 
/*      */   public Stylesheet getStylesheet() {
/*  212 */     return this._stylesheet;
/*      */   }
/*      */ 
/*      */   public void addTemplate(Template template) {
/*  216 */     this._templates.addElement(template);
/*      */   }
/*      */ 
/*      */   private Vector quicksort(Vector templates, int p, int r) {
/*  220 */     if (p < r) {
/*  221 */       int q = partition(templates, p, r);
/*  222 */       quicksort(templates, p, q);
/*  223 */       quicksort(templates, q + 1, r);
/*      */     }
/*  225 */     return templates;
/*      */   }
/*      */ 
/*      */   private int partition(Vector templates, int p, int r) {
/*  229 */     Template x = (Template)templates.elementAt(p);
/*  230 */     int i = p - 1;
/*  231 */     int j = r + 1;
/*      */     while (true) {
/*  233 */       if (x.compareTo((Template)templates.elementAt(--j)) <= 0) {
/*  234 */         while (x.compareTo((Template)templates.elementAt(++i)) < 0);
/*  235 */         if (i >= j) break;
/*  236 */         templates.set(j, templates.set(i, templates.elementAt(j)));
/*      */       }
/*      */     }
/*  239 */     return j;
/*      */   }
/*      */ 
/*      */   public void processPatterns(Hashtable keys)
/*      */   {
/*  248 */     this._keys = keys;
/*      */ 
/*  260 */     this._templates = quicksort(this._templates, 0, this._templates.size() - 1);
/*      */ 
/*  273 */     Enumeration templates = this._templates.elements();
/*  274 */     while (templates.hasMoreElements())
/*      */     {
/*  276 */       Template template = (Template)templates.nextElement();
/*      */ 
/*  283 */       if ((template.isNamed()) && (!template.disabled())) {
/*  284 */         this._namedTemplates.put(template, this);
/*      */       }
/*      */ 
/*  288 */       Pattern pattern = template.getPattern();
/*  289 */       if (pattern != null) {
/*  290 */         flattenAlternative(pattern, template, keys);
/*      */       }
/*      */     }
/*  293 */     prepareTestSequences();
/*      */   }
/*      */ 
/*      */   private void flattenAlternative(Pattern pattern, Template template, Hashtable keys)
/*      */   {
/*  307 */     if ((pattern instanceof IdKeyPattern)) {
/*  308 */       IdKeyPattern idkey = (IdKeyPattern)pattern;
/*  309 */       idkey.setTemplate(template);
/*  310 */       if (this._idxGroup == null) this._idxGroup = new Vector();
/*  311 */       this._idxGroup.add(pattern);
/*      */     }
/*  314 */     else if ((pattern instanceof AlternativePattern)) {
/*  315 */       AlternativePattern alt = (AlternativePattern)pattern;
/*  316 */       flattenAlternative(alt.getLeft(), template, keys);
/*  317 */       flattenAlternative(alt.getRight(), template, keys);
/*      */     }
/*  320 */     else if ((pattern instanceof LocationPathPattern)) {
/*  321 */       LocationPathPattern lpp = (LocationPathPattern)pattern;
/*  322 */       lpp.setTemplate(template);
/*  323 */       addPatternToGroup(lpp);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addPatternToGroup(LocationPathPattern lpp)
/*      */   {
/*  333 */     if ((lpp instanceof IdKeyPattern)) {
/*  334 */       addPattern(-1, lpp);
/*      */     }
/*      */     else
/*      */     {
/*  339 */       StepPattern kernel = lpp.getKernelPattern();
/*  340 */       if (kernel != null) {
/*  341 */         addPattern(kernel.getNodeType(), lpp);
/*      */       }
/*  343 */       else if ((this._rootPattern == null) || (lpp.noSmallerThan(this._rootPattern)))
/*      */       {
/*  345 */         this._rootPattern = lpp;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addPattern(int kernelType, LocationPathPattern pattern)
/*      */   {
/*  355 */     int oldLength = this._patternGroups.length;
/*  356 */     if (kernelType >= oldLength) {
/*  357 */       Vector[] newGroups = new Vector[kernelType * 2];
/*  358 */       System.arraycopy(this._patternGroups, 0, newGroups, 0, oldLength);
/*  359 */       this._patternGroups = newGroups;
/*      */     }
/*      */     Vector patterns;
/*      */     Vector patterns;
/*  365 */     if (kernelType == -1)
/*      */     {
/*      */       Vector patterns;
/*  366 */       if (pattern.getAxis() == 2) {
/*  367 */         patterns = this._attribNodeGroup == null ? (this._attribNodeGroup = new Vector(2)) : this._attribNodeGroup;
/*      */       }
/*      */       else
/*      */       {
/*  371 */         patterns = this._childNodeGroup == null ? (this._childNodeGroup = new Vector(2)) : this._childNodeGroup;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  376 */       patterns = this._patternGroups[kernelType] == null ? (this._patternGroups[kernelType] =  = new Vector(2)) : this._patternGroups[kernelType];
/*      */     }
/*      */ 
/*  381 */     if (patterns.size() == 0) {
/*  382 */       patterns.addElement(pattern);
/*      */     }
/*      */     else {
/*  385 */       boolean inserted = false;
/*  386 */       for (int i = 0; i < patterns.size(); i++) {
/*  387 */         LocationPathPattern lppToCompare = (LocationPathPattern)patterns.elementAt(i);
/*      */ 
/*  390 */         if (pattern.noSmallerThan(lppToCompare)) {
/*  391 */           inserted = true;
/*  392 */           patterns.insertElementAt(pattern, i);
/*  393 */           break;
/*      */         }
/*      */       }
/*  396 */       if (!inserted)
/*  397 */         patterns.addElement(pattern);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void completeTestSequences(int nodeType, Vector patterns)
/*      */   {
/*  407 */     if (patterns != null)
/*  408 */       if (this._patternGroups[nodeType] == null) {
/*  409 */         this._patternGroups[nodeType] = patterns;
/*      */       }
/*      */       else {
/*  412 */         int m = patterns.size();
/*  413 */         for (int j = 0; j < m; j++)
/*  414 */           addPattern(nodeType, (LocationPathPattern)patterns.elementAt(j));
/*      */       }
/*      */   }
/*      */ 
/*      */   private void prepareTestSequences()
/*      */   {
/*  427 */     Vector starGroup = this._patternGroups[1];
/*  428 */     Vector atStarGroup = this._patternGroups[2];
/*      */ 
/*  431 */     completeTestSequences(3, this._childNodeGroup);
/*      */ 
/*  434 */     completeTestSequences(1, this._childNodeGroup);
/*      */ 
/*  437 */     completeTestSequences(7, this._childNodeGroup);
/*      */ 
/*  440 */     completeTestSequences(8, this._childNodeGroup);
/*      */ 
/*  443 */     completeTestSequences(2, this._attribNodeGroup);
/*      */ 
/*  445 */     Vector names = this._stylesheet.getXSLTC().getNamesIndex();
/*  446 */     if ((starGroup != null) || (atStarGroup != null) || (this._childNodeGroup != null) || (this._attribNodeGroup != null))
/*      */     {
/*  449 */       int n = this._patternGroups.length;
/*      */ 
/*  452 */       for (int i = 14; i < n; i++) {
/*  453 */         if (this._patternGroups[i] != null)
/*      */         {
/*  455 */           String name = (String)names.elementAt(i - 14);
/*      */ 
/*  457 */           if (isAttributeName(name))
/*      */           {
/*  459 */             completeTestSequences(i, atStarGroup);
/*      */ 
/*  462 */             completeTestSequences(i, this._attribNodeGroup);
/*      */           }
/*      */           else
/*      */           {
/*  466 */             completeTestSequences(i, starGroup);
/*      */ 
/*  469 */             completeTestSequences(i, this._childNodeGroup);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  474 */     this._testSeq = new TestSeq[14 + names.size()];
/*      */ 
/*  476 */     int n = this._patternGroups.length;
/*  477 */     for (int i = 0; i < n; i++) {
/*  478 */       Vector patterns = this._patternGroups[i];
/*  479 */       if (patterns != null) {
/*  480 */         TestSeq testSeq = new TestSeq(patterns, i, this);
/*      */ 
/*  482 */         testSeq.reduce();
/*  483 */         this._testSeq[i] = testSeq;
/*  484 */         testSeq.findTemplates(this._neededTemplates);
/*      */       }
/*      */     }
/*      */ 
/*  488 */     if ((this._childNodeGroup != null) && (this._childNodeGroup.size() > 0)) {
/*  489 */       this._childNodeTestSeq = new TestSeq(this._childNodeGroup, -1, this);
/*  490 */       this._childNodeTestSeq.reduce();
/*  491 */       this._childNodeTestSeq.findTemplates(this._neededTemplates);
/*      */     }
/*      */ 
/*  502 */     if ((this._idxGroup != null) && (this._idxGroup.size() > 0)) {
/*  503 */       this._idxTestSeq = new TestSeq(this._idxGroup, this);
/*  504 */       this._idxTestSeq.reduce();
/*  505 */       this._idxTestSeq.findTemplates(this._neededTemplates);
/*      */     }
/*      */ 
/*  508 */     if (this._rootPattern != null)
/*      */     {
/*  510 */       this._neededTemplates.put(this._rootPattern.getTemplate(), this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void compileNamedTemplate(Template template, ClassGenerator classGen)
/*      */   {
/*  516 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  517 */     InstructionList il = new InstructionList();
/*  518 */     String methodName = Util.escape(template.getName().toString());
/*      */ 
/*  520 */     int numParams = 0;
/*  521 */     if (template.isSimpleNamedTemplate()) {
/*  522 */       Vector parameters = template.getParameters();
/*  523 */       numParams = parameters.size();
/*      */     }
/*      */ 
/*  527 */     Type[] types = new Type[4 + numParams];
/*      */ 
/*  529 */     String[] names = new String[4 + numParams];
/*  530 */     types[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*  531 */     types[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*  532 */     types[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*  533 */     types[3] = Type.INT;
/*  534 */     names[0] = "document";
/*  535 */     names[1] = "iterator";
/*  536 */     names[2] = "handler";
/*  537 */     names[3] = "node";
/*      */ 
/*  542 */     for (int i = 4; i < 4 + numParams; i++) {
/*  543 */       types[i] = Util.getJCRefType("Ljava/lang/Object;");
/*  544 */       names[i] = ("param" + String.valueOf(i - 4));
/*      */     }
/*      */ 
/*  547 */     NamedMethodGenerator methodGen = new NamedMethodGenerator(1, Type.VOID, types, names, methodName, getClassName(), il, cpg);
/*      */ 
/*  553 */     il.append(template.compile(classGen, methodGen));
/*  554 */     il.append(RETURN);
/*      */ 
/*  556 */     classGen.addMethod(methodGen);
/*      */   }
/*      */ 
/*      */   private void compileTemplates(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
/*      */   {
/*  563 */     Enumeration templates = this._namedTemplates.keys();
/*  564 */     while (templates.hasMoreElements()) {
/*  565 */       Template template = (Template)templates.nextElement();
/*  566 */       compileNamedTemplate(template, classGen);
/*      */     }
/*      */ 
/*  569 */     templates = this._neededTemplates.keys();
/*  570 */     while (templates.hasMoreElements()) {
/*  571 */       Template template = (Template)templates.nextElement();
/*  572 */       if (template.hasContents())
/*      */       {
/*  574 */         InstructionList til = template.compile(classGen, methodGen);
/*  575 */         til.append(new GOTO_W(next));
/*  576 */         this._templateILs.put(template, til);
/*  577 */         this._templateIHs.put(template, til.getStart());
/*      */       }
/*      */       else
/*      */       {
/*  581 */         this._templateIHs.put(template, next);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendTemplateCode(InstructionList body) {
/*  587 */     Enumeration templates = this._neededTemplates.keys();
/*  588 */     while (templates.hasMoreElements()) {
/*  589 */       Object iList = this._templateILs.get(templates.nextElement());
/*      */ 
/*  591 */       if (iList != null)
/*  592 */         body.append((InstructionList)iList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void appendTestSequences(InstructionList body)
/*      */   {
/*  598 */     int n = this._testSeq.length;
/*  599 */     for (int i = 0; i < n; i++) {
/*  600 */       TestSeq testSeq = this._testSeq[i];
/*  601 */       if (testSeq != null) {
/*  602 */         InstructionList il = testSeq.getInstructionList();
/*  603 */         if (il != null)
/*  604 */           body.append(il);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void compileGetChildren(ClassGenerator classGen, MethodGenerator methodGen, int node)
/*      */   {
/*  613 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  614 */     InstructionList il = methodGen.getInstructionList();
/*  615 */     int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getChildren", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/*  618 */     il.append(methodGen.loadDOM());
/*  619 */     il.append(new ILOAD(node));
/*  620 */     il.append(new INVOKEINTERFACE(git, 2));
/*      */   }
/*      */ 
/*      */   private InstructionList compileDefaultRecursion(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
/*      */   {
/*  629 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  630 */     InstructionList il = new InstructionList();
/*  631 */     String applyTemplatesSig = classGen.getApplyTemplatesSig();
/*  632 */     int git = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getChildren", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/*  635 */     int applyTemplates = cpg.addMethodref(getClassName(), functionName(), applyTemplatesSig);
/*      */ 
/*  638 */     il.append(classGen.loadTranslet());
/*  639 */     il.append(methodGen.loadDOM());
/*      */ 
/*  641 */     il.append(methodGen.loadDOM());
/*  642 */     il.append(new ILOAD(this._currentIndex));
/*  643 */     il.append(new INVOKEINTERFACE(git, 2));
/*  644 */     il.append(methodGen.loadHandler());
/*  645 */     il.append(new INVOKEVIRTUAL(applyTemplates));
/*  646 */     il.append(new GOTO_W(next));
/*  647 */     return il;
/*      */   }
/*      */ 
/*      */   private InstructionList compileDefaultText(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next)
/*      */   {
/*  657 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  658 */     InstructionList il = new InstructionList();
/*      */ 
/*  660 */     int chars = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "characters", "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
/*      */ 
/*  663 */     il.append(methodGen.loadDOM());
/*  664 */     il.append(new ILOAD(this._currentIndex));
/*  665 */     il.append(methodGen.loadHandler());
/*  666 */     il.append(new INVOKEINTERFACE(chars, 3));
/*  667 */     il.append(new GOTO_W(next));
/*  668 */     return il;
/*      */   }
/*      */ 
/*      */   private InstructionList compileNamespaces(ClassGenerator classGen, MethodGenerator methodGen, boolean[] isNamespace, boolean[] isAttribute, boolean attrFlag, InstructionHandle defaultTarget)
/*      */   {
/*  677 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/*  678 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*      */ 
/*  681 */     Vector namespaces = xsltc.getNamespaceIndex();
/*  682 */     Vector names = xsltc.getNamesIndex();
/*  683 */     int namespaceCount = namespaces.size() + 1;
/*  684 */     int namesCount = names.size();
/*      */ 
/*  686 */     InstructionList il = new InstructionList();
/*  687 */     int[] types = new int[namespaceCount];
/*  688 */     InstructionHandle[] targets = new InstructionHandle[types.length];
/*      */ 
/*  690 */     if (namespaceCount > 0) {
/*  691 */       boolean compiled = false;
/*      */ 
/*  694 */       for (int i = 0; i < namespaceCount; i++) {
/*  695 */         targets[i] = defaultTarget;
/*  696 */         types[i] = i;
/*      */       }
/*      */ 
/*  700 */       for (int i = 14; i < 14 + namesCount; i++) {
/*  701 */         if ((isNamespace[i] != 0) && (isAttribute[i] == attrFlag)) {
/*  702 */           String name = (String)names.elementAt(i - 14);
/*  703 */           String namespace = name.substring(0, name.lastIndexOf(':'));
/*  704 */           int type = xsltc.registerNamespace(namespace);
/*      */ 
/*  706 */           if ((i < this._testSeq.length) && (this._testSeq[i] != null))
/*      */           {
/*  708 */             targets[type] = this._testSeq[i].compile(classGen, methodGen, defaultTarget);
/*      */ 
/*  712 */             compiled = true;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  718 */       if (!compiled) return null;
/*      */ 
/*  721 */       int getNS = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getNamespaceType", "(I)I");
/*      */ 
/*  724 */       il.append(methodGen.loadDOM());
/*  725 */       il.append(new ILOAD(this._currentIndex));
/*  726 */       il.append(new INVOKEINTERFACE(getNS, 2));
/*  727 */       il.append(new SWITCH(types, targets, defaultTarget));
/*  728 */       return il;
/*      */     }
/*      */ 
/*  731 */     return null;
/*      */   }
/*      */ 
/*      */   public void compileApplyTemplates(ClassGenerator classGen)
/*      */   {
/*  740 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/*  741 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  742 */     Vector names = xsltc.getNamesIndex();
/*      */ 
/*  745 */     Type[] argTypes = new Type[3];
/*      */ 
/*  747 */     argTypes[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/*  748 */     argTypes[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*  749 */     argTypes[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/*      */ 
/*  751 */     String[] argNames = new String[3];
/*  752 */     argNames[0] = "document";
/*  753 */     argNames[1] = "iterator";
/*  754 */     argNames[2] = "handler";
/*      */ 
/*  756 */     InstructionList mainIL = new InstructionList();
/*  757 */     MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName(), getClassName(), mainIL, classGen.getConstantPool());
/*      */ 
/*  763 */     methodGen.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
/*      */ 
/*  766 */     mainIL.append(NOP);
/*      */ 
/*  771 */     LocalVariableGen current = methodGen.addLocalVariable2("current", Type.INT, null);
/*      */ 
/*  774 */     this._currentIndex = current.getIndex();
/*      */ 
/*  778 */     InstructionList body = new InstructionList();
/*  779 */     body.append(NOP);
/*      */ 
/*  783 */     InstructionList ilLoop = new InstructionList();
/*  784 */     ilLoop.append(methodGen.loadIterator());
/*  785 */     ilLoop.append(methodGen.nextNode());
/*  786 */     ilLoop.append(DUP);
/*  787 */     ilLoop.append(new ISTORE(this._currentIndex));
/*      */ 
/*  791 */     BranchHandle ifeq = ilLoop.append(new IFLT(null));
/*  792 */     BranchHandle loop = ilLoop.append(new GOTO_W(null));
/*  793 */     ifeq.setTarget(ilLoop.append(RETURN));
/*  794 */     InstructionHandle ihLoop = ilLoop.getStart();
/*      */ 
/*  796 */     current.setStart(mainIL.append(new GOTO_W(ihLoop)));
/*      */ 
/*  799 */     current.setEnd(loop);
/*      */ 
/*  802 */     InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
/*      */ 
/*  804 */     InstructionHandle ihRecurse = ilRecurse.getStart();
/*      */ 
/*  807 */     InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
/*      */ 
/*  809 */     InstructionHandle ihText = ilText.getStart();
/*      */ 
/*  812 */     int[] types = new int[14 + names.size()];
/*  813 */     for (int i = 0; i < types.length; i++) {
/*  814 */       types[i] = i;
/*      */     }
/*      */ 
/*  818 */     boolean[] isAttribute = new boolean[types.length];
/*  819 */     boolean[] isNamespace = new boolean[types.length];
/*  820 */     for (int i = 0; i < names.size(); i++) {
/*  821 */       String name = (String)names.elementAt(i);
/*  822 */       isAttribute[(i + 14)] = isAttributeName(name);
/*  823 */       isNamespace[(i + 14)] = isNamespaceName(name);
/*      */     }
/*      */ 
/*  827 */     compileTemplates(classGen, methodGen, ihLoop);
/*      */ 
/*  830 */     TestSeq elemTest = this._testSeq[1];
/*  831 */     InstructionHandle ihElem = ihRecurse;
/*  832 */     if (elemTest != null) {
/*  833 */       ihElem = elemTest.compile(classGen, methodGen, ihRecurse);
/*      */     }
/*      */ 
/*  836 */     TestSeq attrTest = this._testSeq[2];
/*  837 */     InstructionHandle ihAttr = ihText;
/*  838 */     if (attrTest != null) {
/*  839 */       ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
/*      */     }
/*      */ 
/*  842 */     InstructionList ilKey = null;
/*  843 */     if (this._idxTestSeq != null) {
/*  844 */       loop.setTarget(this._idxTestSeq.compile(classGen, methodGen, body.getStart()));
/*  845 */       ilKey = this._idxTestSeq.getInstructionList();
/*      */     }
/*      */     else {
/*  848 */       loop.setTarget(body.getStart());
/*      */     }
/*      */ 
/*  853 */     if (this._childNodeTestSeq != null)
/*      */     {
/*  855 */       double nodePrio = this._childNodeTestSeq.getPriority();
/*  856 */       int nodePos = this._childNodeTestSeq.getPosition();
/*  857 */       double elemPrio = -1.797693134862316E+308D;
/*  858 */       int elemPos = -2147483648;
/*      */ 
/*  860 */       if (elemTest != null) {
/*  861 */         elemPrio = elemTest.getPriority();
/*  862 */         elemPos = elemTest.getPosition();
/*      */       }
/*  864 */       if ((elemPrio == (0.0D / 0.0D)) || (elemPrio < nodePrio) || ((elemPrio == nodePrio) && (elemPos < nodePos)))
/*      */       {
/*  867 */         ihElem = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
/*      */       }
/*      */ 
/*  871 */       TestSeq textTest = this._testSeq[3];
/*  872 */       double textPrio = -1.797693134862316E+308D;
/*  873 */       int textPos = -2147483648;
/*      */ 
/*  875 */       if (textTest != null) {
/*  876 */         textPrio = textTest.getPriority();
/*  877 */         textPos = textTest.getPosition();
/*      */       }
/*  879 */       if ((textPrio == (0.0D / 0.0D)) || (textPrio < nodePrio) || ((textPrio == nodePrio) && (textPos < nodePos)))
/*      */       {
/*  882 */         ihText = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
/*  883 */         this._testSeq[3] = this._childNodeTestSeq;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  888 */     InstructionHandle elemNamespaceHandle = ihElem;
/*  889 */     InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
/*      */ 
/*  892 */     if (nsElem != null) elemNamespaceHandle = nsElem.getStart();
/*      */ 
/*  895 */     InstructionHandle attrNamespaceHandle = ihAttr;
/*  896 */     InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
/*      */ 
/*  899 */     if (nsAttr != null) attrNamespaceHandle = nsAttr.getStart();
/*      */ 
/*  902 */     InstructionHandle[] targets = new InstructionHandle[types.length];
/*  903 */     for (int i = 14; i < targets.length; i++) {
/*  904 */       TestSeq testSeq = this._testSeq[i];
/*      */ 
/*  906 */       if (isNamespace[i] != 0) {
/*  907 */         if (isAttribute[i] != 0)
/*  908 */           targets[i] = attrNamespaceHandle;
/*      */         else {
/*  910 */           targets[i] = elemNamespaceHandle;
/*      */         }
/*      */       }
/*  913 */       else if (testSeq != null) {
/*  914 */         if (isAttribute[i] != 0) {
/*  915 */           targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
/*      */         }
/*      */         else {
/*  918 */           targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
/*      */         }
/*      */       }
/*      */       else {
/*  922 */         targets[i] = ihLoop;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  928 */     targets[0] = (this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse);
/*      */ 
/*  933 */     targets[9] = (this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse);
/*      */ 
/*  938 */     targets[3] = (this._testSeq[3] != null ? this._testSeq[3].compile(classGen, methodGen, ihText) : ihText);
/*      */ 
/*  943 */     targets[13] = ihLoop;
/*      */ 
/*  946 */     targets[1] = elemNamespaceHandle;
/*      */ 
/*  949 */     targets[2] = attrNamespaceHandle;
/*      */ 
/*  952 */     InstructionHandle ihPI = ihLoop;
/*  953 */     if (this._childNodeTestSeq != null) ihPI = ihElem;
/*  954 */     if (this._testSeq[7] != null) {
/*  955 */       targets[7] = this._testSeq[7].compile(classGen, methodGen, ihPI);
/*      */     }
/*      */     else
/*      */     {
/*  959 */       targets[7] = ihPI;
/*      */     }
/*      */ 
/*  962 */     InstructionHandle ihComment = ihLoop;
/*  963 */     if (this._childNodeTestSeq != null) ihComment = ihElem;
/*  964 */     targets[8] = (this._testSeq[8] != null ? this._testSeq[8].compile(classGen, methodGen, ihComment) : ihComment);
/*      */ 
/*  969 */     targets[4] = ihLoop;
/*      */ 
/*  972 */     targets[11] = ihLoop;
/*      */ 
/*  975 */     targets[10] = ihLoop;
/*      */ 
/*  978 */     targets[6] = ihLoop;
/*      */ 
/*  981 */     targets[5] = ihLoop;
/*      */ 
/*  984 */     targets[12] = ihLoop;
/*      */ 
/*  988 */     for (int i = 14; i < targets.length; i++) {
/*  989 */       TestSeq testSeq = this._testSeq[i];
/*      */ 
/*  991 */       if ((testSeq == null) || (isNamespace[i] != 0)) {
/*  992 */         if (isAttribute[i] != 0)
/*  993 */           targets[i] = attrNamespaceHandle;
/*      */         else {
/*  995 */           targets[i] = elemNamespaceHandle;
/*      */         }
/*      */ 
/*      */       }
/*  999 */       else if (isAttribute[i] != 0) {
/* 1000 */         targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
/*      */       }
/*      */       else {
/* 1003 */         targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1008 */     if (ilKey != null) body.insert(ilKey);
/*      */ 
/* 1011 */     int getType = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*      */ 
/* 1014 */     body.append(methodGen.loadDOM());
/* 1015 */     body.append(new ILOAD(this._currentIndex));
/* 1016 */     body.append(new INVOKEINTERFACE(getType, 2));
/*      */ 
/* 1019 */     InstructionHandle disp = body.append(new SWITCH(types, targets, ihLoop));
/*      */ 
/* 1022 */     appendTestSequences(body);
/*      */ 
/* 1024 */     appendTemplateCode(body);
/*      */ 
/* 1027 */     if (nsElem != null) body.append(nsElem);
/*      */ 
/* 1029 */     if (nsAttr != null) body.append(nsAttr);
/*      */ 
/* 1032 */     body.append(ilRecurse);
/*      */ 
/* 1034 */     body.append(ilText);
/*      */ 
/* 1037 */     mainIL.append(body);
/*      */ 
/* 1039 */     mainIL.append(ilLoop);
/*      */ 
/* 1041 */     peepHoleOptimization(methodGen);
/* 1042 */     classGen.addMethod(methodGen);
/*      */ 
/* 1045 */     if (this._importLevels != null) {
/* 1046 */       Enumeration levels = this._importLevels.keys();
/* 1047 */       while (levels.hasMoreElements()) {
/* 1048 */         Integer max = (Integer)levels.nextElement();
/* 1049 */         Integer min = (Integer)this._importLevels.get(max);
/* 1050 */         compileApplyImports(classGen, min.intValue(), max.intValue());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void compileTemplateCalls(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next, int min, int max)
/*      */   {
/* 1058 */     Enumeration templates = this._neededTemplates.keys();
/* 1059 */     while (templates.hasMoreElements()) {
/* 1060 */       Template template = (Template)templates.nextElement();
/* 1061 */       int prec = template.getImportPrecedence();
/* 1062 */       if ((prec >= min) && (prec < max))
/* 1063 */         if (template.hasContents()) {
/* 1064 */           InstructionList til = template.compile(classGen, methodGen);
/* 1065 */           til.append(new GOTO_W(next));
/* 1066 */           this._templateILs.put(template, til);
/* 1067 */           this._templateIHs.put(template, til.getStart());
/*      */         }
/*      */         else
/*      */         {
/* 1071 */           this._templateIHs.put(template, next);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void compileApplyImports(ClassGenerator classGen, int min, int max)
/*      */   {
/* 1079 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/* 1080 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 1081 */     Vector names = xsltc.getNamesIndex();
/*      */ 
/* 1084 */     this._namedTemplates = new Hashtable();
/* 1085 */     this._neededTemplates = new Hashtable();
/* 1086 */     this._templateIHs = new Hashtable();
/* 1087 */     this._templateILs = new Hashtable();
/* 1088 */     this._patternGroups = new Vector[32];
/* 1089 */     this._rootPattern = null;
/*      */ 
/* 1092 */     Vector oldTemplates = this._templates;
/*      */ 
/* 1095 */     this._templates = new Vector();
/* 1096 */     Enumeration templates = oldTemplates.elements();
/*      */     Template template;
/* 1100 */     for (; templates.hasMoreElements(); 
/* 1100 */       addTemplate(template)) { template = (Template)templates.nextElement();
/* 1099 */       int prec = template.getImportPrecedence();
/* 1100 */       if ((prec < min) || (prec >= max)); } processPatterns(this._keys);
/*      */ 
/* 1107 */     Type[] argTypes = new Type[4];
/*      */ 
/* 1109 */     argTypes[0] = Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
/* 1110 */     argTypes[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/* 1111 */     argTypes[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
/* 1112 */     argTypes[3] = Type.INT;
/*      */ 
/* 1114 */     String[] argNames = new String[4];
/* 1115 */     argNames[0] = "document";
/* 1116 */     argNames[1] = "iterator";
/* 1117 */     argNames[2] = "handler";
/* 1118 */     argNames[3] = "node";
/*      */ 
/* 1120 */     InstructionList mainIL = new InstructionList();
/* 1121 */     MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName() + '_' + max, getClassName(), mainIL, classGen.getConstantPool());
/*      */ 
/* 1127 */     methodGen.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
/*      */ 
/* 1131 */     LocalVariableGen current = methodGen.addLocalVariable2("current", Type.INT, null);
/*      */ 
/* 1134 */     this._currentIndex = current.getIndex();
/*      */ 
/* 1136 */     mainIL.append(new ILOAD(methodGen.getLocalIndex("node")));
/* 1137 */     current.setStart(mainIL.append(new ISTORE(this._currentIndex)));
/*      */ 
/* 1141 */     InstructionList body = new InstructionList();
/* 1142 */     body.append(NOP);
/*      */ 
/* 1146 */     InstructionList ilLoop = new InstructionList();
/* 1147 */     ilLoop.append(RETURN);
/* 1148 */     InstructionHandle ihLoop = ilLoop.getStart();
/*      */ 
/* 1151 */     InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
/*      */ 
/* 1153 */     InstructionHandle ihRecurse = ilRecurse.getStart();
/*      */ 
/* 1156 */     InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
/*      */ 
/* 1158 */     InstructionHandle ihText = ilText.getStart();
/*      */ 
/* 1161 */     int[] types = new int[14 + names.size()];
/* 1162 */     for (int i = 0; i < types.length; i++) {
/* 1163 */       types[i] = i;
/*      */     }
/*      */ 
/* 1166 */     boolean[] isAttribute = new boolean[types.length];
/* 1167 */     boolean[] isNamespace = new boolean[types.length];
/* 1168 */     for (int i = 0; i < names.size(); i++) {
/* 1169 */       String name = (String)names.elementAt(i);
/* 1170 */       isAttribute[(i + 14)] = isAttributeName(name);
/* 1171 */       isNamespace[(i + 14)] = isNamespaceName(name);
/*      */     }
/*      */ 
/* 1175 */     compileTemplateCalls(classGen, methodGen, ihLoop, min, max);
/*      */ 
/* 1178 */     TestSeq elemTest = this._testSeq[1];
/* 1179 */     InstructionHandle ihElem = ihRecurse;
/* 1180 */     if (elemTest != null) {
/* 1181 */       ihElem = elemTest.compile(classGen, methodGen, ihLoop);
/*      */     }
/*      */ 
/* 1185 */     TestSeq attrTest = this._testSeq[2];
/* 1186 */     InstructionHandle ihAttr = ihLoop;
/* 1187 */     if (attrTest != null) {
/* 1188 */       ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
/*      */     }
/*      */ 
/* 1192 */     InstructionList ilKey = null;
/* 1193 */     if (this._idxTestSeq != null) {
/* 1194 */       ilKey = this._idxTestSeq.getInstructionList();
/*      */     }
/*      */ 
/* 1199 */     if (this._childNodeTestSeq != null)
/*      */     {
/* 1201 */       double nodePrio = this._childNodeTestSeq.getPriority();
/* 1202 */       int nodePos = this._childNodeTestSeq.getPosition();
/* 1203 */       double elemPrio = -1.797693134862316E+308D;
/* 1204 */       int elemPos = -2147483648;
/*      */ 
/* 1206 */       if (elemTest != null) {
/* 1207 */         elemPrio = elemTest.getPriority();
/* 1208 */         elemPos = elemTest.getPosition();
/*      */       }
/*      */ 
/* 1211 */       if ((elemPrio == (0.0D / 0.0D)) || (elemPrio < nodePrio) || ((elemPrio == nodePrio) && (elemPos < nodePos)))
/*      */       {
/* 1214 */         ihElem = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
/*      */       }
/*      */ 
/* 1218 */       TestSeq textTest = this._testSeq[3];
/* 1219 */       double textPrio = -1.797693134862316E+308D;
/* 1220 */       int textPos = -2147483648;
/*      */ 
/* 1222 */       if (textTest != null) {
/* 1223 */         textPrio = textTest.getPriority();
/* 1224 */         textPos = textTest.getPosition();
/*      */       }
/*      */ 
/* 1227 */       if ((textPrio == (0.0D / 0.0D)) || (textPrio < nodePrio) || ((textPrio == nodePrio) && (textPos < nodePos)))
/*      */       {
/* 1230 */         ihText = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
/* 1231 */         this._testSeq[3] = this._childNodeTestSeq;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1236 */     InstructionHandle elemNamespaceHandle = ihElem;
/* 1237 */     InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
/*      */ 
/* 1240 */     if (nsElem != null) elemNamespaceHandle = nsElem.getStart();
/*      */ 
/* 1243 */     InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
/*      */ 
/* 1246 */     InstructionHandle attrNamespaceHandle = ihAttr;
/* 1247 */     if (nsAttr != null) attrNamespaceHandle = nsAttr.getStart();
/*      */ 
/* 1250 */     InstructionHandle[] targets = new InstructionHandle[types.length];
/* 1251 */     for (int i = 14; i < targets.length; i++) {
/* 1252 */       TestSeq testSeq = this._testSeq[i];
/*      */ 
/* 1254 */       if (isNamespace[i] != 0) {
/* 1255 */         if (isAttribute[i] != 0)
/* 1256 */           targets[i] = attrNamespaceHandle;
/*      */         else {
/* 1258 */           targets[i] = elemNamespaceHandle;
/*      */         }
/*      */       }
/* 1261 */       else if (testSeq != null) {
/* 1262 */         if (isAttribute[i] != 0) {
/* 1263 */           targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
/*      */         }
/*      */         else {
/* 1266 */           targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
/*      */         }
/*      */       }
/*      */       else {
/* 1270 */         targets[i] = ihLoop;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1275 */     targets[0] = (this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse);
/*      */ 
/* 1279 */     targets[9] = (this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse);
/*      */ 
/* 1284 */     targets[3] = (this._testSeq[3] != null ? this._testSeq[3].compile(classGen, methodGen, ihText) : ihText);
/*      */ 
/* 1289 */     targets[13] = ihLoop;
/*      */ 
/* 1292 */     targets[1] = elemNamespaceHandle;
/*      */ 
/* 1295 */     targets[2] = attrNamespaceHandle;
/*      */ 
/* 1298 */     InstructionHandle ihPI = ihLoop;
/* 1299 */     if (this._childNodeTestSeq != null) ihPI = ihElem;
/* 1300 */     if (this._testSeq[7] != null) {
/* 1301 */       targets[7] = this._testSeq[7].compile(classGen, methodGen, ihPI);
/*      */     }
/*      */     else
/*      */     {
/* 1306 */       targets[7] = ihPI;
/*      */     }
/*      */ 
/* 1310 */     InstructionHandle ihComment = ihLoop;
/* 1311 */     if (this._childNodeTestSeq != null) ihComment = ihElem;
/* 1312 */     targets[8] = (this._testSeq[8] != null ? this._testSeq[8].compile(classGen, methodGen, ihComment) : ihComment);
/*      */ 
/* 1317 */     targets[4] = ihLoop;
/*      */ 
/* 1320 */     targets[11] = ihLoop;
/*      */ 
/* 1323 */     targets[10] = ihLoop;
/*      */ 
/* 1326 */     targets[6] = ihLoop;
/*      */ 
/* 1329 */     targets[5] = ihLoop;
/*      */ 
/* 1332 */     targets[12] = ihLoop;
/*      */ 
/* 1337 */     for (int i = 14; i < targets.length; i++) {
/* 1338 */       TestSeq testSeq = this._testSeq[i];
/*      */ 
/* 1340 */       if ((testSeq == null) || (isNamespace[i] != 0)) {
/* 1341 */         if (isAttribute[i] != 0)
/* 1342 */           targets[i] = attrNamespaceHandle;
/*      */         else {
/* 1344 */           targets[i] = elemNamespaceHandle;
/*      */         }
/*      */ 
/*      */       }
/* 1348 */       else if (isAttribute[i] != 0) {
/* 1349 */         targets[i] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
/*      */       }
/*      */       else {
/* 1352 */         targets[i] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1357 */     if (ilKey != null) body.insert(ilKey);
/*      */ 
/* 1360 */     int getType = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*      */ 
/* 1363 */     body.append(methodGen.loadDOM());
/* 1364 */     body.append(new ILOAD(this._currentIndex));
/* 1365 */     body.append(new INVOKEINTERFACE(getType, 2));
/*      */ 
/* 1368 */     InstructionHandle disp = body.append(new SWITCH(types, targets, ihLoop));
/*      */ 
/* 1371 */     appendTestSequences(body);
/*      */ 
/* 1373 */     appendTemplateCode(body);
/*      */ 
/* 1376 */     if (nsElem != null) body.append(nsElem);
/*      */ 
/* 1378 */     if (nsAttr != null) body.append(nsAttr);
/*      */ 
/* 1381 */     body.append(ilRecurse);
/*      */ 
/* 1383 */     body.append(ilText);
/*      */ 
/* 1386 */     mainIL.append(body);
/*      */ 
/* 1389 */     current.setEnd(body.getEnd());
/*      */ 
/* 1392 */     mainIL.append(ilLoop);
/*      */ 
/* 1394 */     peepHoleOptimization(methodGen);
/* 1395 */     classGen.addMethod(methodGen);
/*      */ 
/* 1398 */     this._templates = oldTemplates;
/*      */   }
/*      */ 
/*      */   private void peepHoleOptimization(MethodGenerator methodGen)
/*      */   {
/* 1405 */     InstructionList il = methodGen.getInstructionList();
/* 1406 */     InstructionFinder find = new InstructionFinder(il);
/*      */ 
/* 1414 */     String pattern = "loadinstruction pop";
/*      */ 
/* 1416 */     for (Iterator iter = find.search(pattern); iter.hasNext(); ) {
/* 1417 */       InstructionHandle[] match = (InstructionHandle[])iter.next();
/*      */       try {
/* 1419 */         if ((!match[0].hasTargeters()) && (!match[1].hasTargeters())) {
/* 1420 */           il.delete(match[0], match[1]);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (TargetLostException e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1432 */     pattern = "iload iload swap istore";
/* 1433 */     for (Iterator iter = find.search(pattern); iter.hasNext(); ) {
/* 1434 */       InstructionHandle[] match = (InstructionHandle[])iter.next();
/*      */       try {
/* 1436 */         ILOAD iload1 = (ILOAD)match[0].getInstruction();
/*      */ 
/* 1438 */         ILOAD iload2 = (ILOAD)match[1].getInstruction();
/*      */ 
/* 1440 */         ISTORE istore = (ISTORE)match[3].getInstruction();
/*      */ 
/* 1443 */         if ((!match[1].hasTargeters()) && (!match[2].hasTargeters()) && (!match[3].hasTargeters()) && (iload1.getIndex() == iload2.getIndex()) && (iload2.getIndex() == istore.getIndex()))
/*      */         {
/* 1449 */           il.delete(match[1], match[3]);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (TargetLostException e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1461 */     pattern = "loadinstruction loadinstruction swap";
/* 1462 */     for (Iterator iter = find.search(pattern); iter.hasNext(); ) {
/* 1463 */       InstructionHandle[] match = (InstructionHandle[])iter.next();
/*      */       try {
/* 1465 */         if ((!match[0].hasTargeters()) && (!match[1].hasTargeters()) && (!match[2].hasTargeters()))
/*      */         {
/* 1469 */           Instruction load_m = match[1].getInstruction();
/* 1470 */           il.insert(match[0], load_m);
/* 1471 */           il.delete(match[1], match[2]);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (TargetLostException e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1483 */     pattern = "aload aload";
/* 1484 */     for (Iterator iter = find.search(pattern); iter.hasNext(); ) {
/* 1485 */       InstructionHandle[] match = (InstructionHandle[])iter.next();
/*      */       try {
/* 1487 */         if (!match[1].hasTargeters()) {
/* 1488 */           ALOAD aload1 = (ALOAD)match[0].getInstruction();
/*      */ 
/* 1490 */           ALOAD aload2 = (ALOAD)match[1].getInstruction();
/*      */ 
/* 1493 */           if (aload1.getIndex() == aload2.getIndex()) {
/* 1494 */             il.insert(match[1], new DUP());
/* 1495 */             il.delete(match[1]);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (TargetLostException e)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public InstructionHandle getTemplateInstructionHandle(Template template) {
/* 1506 */     return (InstructionHandle)this._templateIHs.get(template);
/*      */   }
/*      */ 
/*      */   private static boolean isAttributeName(String qname)
/*      */   {
/* 1513 */     int col = qname.lastIndexOf(':') + 1;
/* 1514 */     return qname.charAt(col) == '@';
/*      */   }
/*      */ 
/*      */   private static boolean isNamespaceName(String qname)
/*      */   {
/* 1522 */     int col = qname.lastIndexOf(':');
/* 1523 */     return (col > -1) && (qname.charAt(qname.length() - 1) == '*');
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Mode
 * JD-Core Version:    0.6.2
 */