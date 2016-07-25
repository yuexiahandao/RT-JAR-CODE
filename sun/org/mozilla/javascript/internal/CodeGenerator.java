/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import sun.org.mozilla.javascript.internal.ast.AstRoot;
/*      */ import sun.org.mozilla.javascript.internal.ast.FunctionNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.Jump;
/*      */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*      */ 
/*      */ class CodeGenerator extends Icode
/*      */ {
/*      */   private static final int MIN_LABEL_TABLE_SIZE = 32;
/*      */   private static final int MIN_FIXUP_TABLE_SIZE = 40;
/*      */   private CompilerEnvirons compilerEnv;
/*      */   private boolean itsInFunctionFlag;
/*      */   private boolean itsInTryFlag;
/*      */   private InterpreterData itsData;
/*      */   private ScriptNode scriptOrFn;
/*      */   private int iCodeTop;
/*      */   private int stackDepth;
/*      */   private int lineNumber;
/*      */   private int doubleTableTop;
/*   76 */   private ObjToIntMap strings = new ObjToIntMap(20);
/*      */   private int localTop;
/*      */   private int[] labelTable;
/*      */   private int labelTableTop;
/*      */   private long[] fixupTable;
/*      */   private int fixupTableTop;
/*   84 */   private ObjArray literalIds = new ObjArray();
/*      */   private int exceptionTableTop;
/*      */   private static final int ECF_TAIL = 1;
/*      */ 
/*      */   public InterpreterData compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean)
/*      */   {
/*   96 */     this.compilerEnv = paramCompilerEnvirons;
/*      */ 
/*  103 */     new NodeTransformer().transform(paramScriptNode);
/*      */ 
/*  110 */     if (paramBoolean)
/*  111 */       this.scriptOrFn = paramScriptNode.getFunctionNode(0);
/*      */     else {
/*  113 */       this.scriptOrFn = paramScriptNode;
/*      */     }
/*  115 */     this.itsData = new InterpreterData(paramCompilerEnvirons.getLanguageVersion(), this.scriptOrFn.getSourceName(), paramString, ((AstRoot)paramScriptNode).isInStrictMode());
/*      */ 
/*  119 */     this.itsData.topLevel = true;
/*      */ 
/*  121 */     if (paramBoolean)
/*  122 */       generateFunctionICode();
/*      */     else {
/*  124 */       generateICodeFromTree(this.scriptOrFn);
/*      */     }
/*  126 */     return this.itsData;
/*      */   }
/*      */ 
/*      */   private void generateFunctionICode()
/*      */   {
/*  131 */     this.itsInFunctionFlag = true;
/*      */ 
/*  133 */     FunctionNode localFunctionNode = (FunctionNode)this.scriptOrFn;
/*      */ 
/*  135 */     this.itsData.itsFunctionType = localFunctionNode.getFunctionType();
/*  136 */     this.itsData.itsNeedsActivation = localFunctionNode.requiresActivation();
/*  137 */     if (localFunctionNode.getFunctionName() != null) {
/*  138 */       this.itsData.itsName = localFunctionNode.getName();
/*      */     }
/*  140 */     if ((!localFunctionNode.getIgnoreDynamicScope()) && 
/*  141 */       (this.compilerEnv.isUseDynamicScope())) {
/*  142 */       this.itsData.useDynamicScope = true;
/*      */     }
/*      */ 
/*  145 */     if (localFunctionNode.isGenerator()) {
/*  146 */       addIcode(-62);
/*  147 */       addUint16(localFunctionNode.getBaseLineno() & 0xFFFF);
/*      */     }
/*      */ 
/*  150 */     generateICodeFromTree(localFunctionNode.getLastChild());
/*      */   }
/*      */ 
/*      */   private void generateICodeFromTree(Node paramNode)
/*      */   {
/*  155 */     generateNestedFunctions();
/*      */ 
/*  157 */     generateRegExpLiterals();
/*      */ 
/*  159 */     visitStatement(paramNode, 0);
/*  160 */     fixLabelGotos();
/*      */ 
/*  162 */     if (this.itsData.itsFunctionType == 0)
/*  163 */       addToken(64);
/*      */     Object localObject;
/*  166 */     if (this.itsData.itsICode.length != this.iCodeTop)
/*      */     {
/*  169 */       localObject = new byte[this.iCodeTop];
/*  170 */       System.arraycopy(this.itsData.itsICode, 0, localObject, 0, this.iCodeTop);
/*  171 */       this.itsData.itsICode = ((byte[])localObject);
/*      */     }
/*  173 */     if (this.strings.size() == 0) {
/*  174 */       this.itsData.itsStringTable = null;
/*      */     } else {
/*  176 */       this.itsData.itsStringTable = new String[this.strings.size()];
/*  177 */       localObject = this.strings.newIterator();
/*  178 */       for (((ObjToIntMap.Iterator)localObject).start(); !((ObjToIntMap.Iterator)localObject).done(); ((ObjToIntMap.Iterator)localObject).next()) {
/*  179 */         String str = (String)((ObjToIntMap.Iterator)localObject).getKey();
/*  180 */         int i = ((ObjToIntMap.Iterator)localObject).getValue();
/*  181 */         if (this.itsData.itsStringTable[i] != null) Kit.codeBug();
/*  182 */         this.itsData.itsStringTable[i] = str;
/*      */       }
/*      */     }
/*  185 */     if (this.doubleTableTop == 0) {
/*  186 */       this.itsData.itsDoubleTable = null;
/*  187 */     } else if (this.itsData.itsDoubleTable.length != this.doubleTableTop) {
/*  188 */       localObject = new double[this.doubleTableTop];
/*  189 */       System.arraycopy(this.itsData.itsDoubleTable, 0, localObject, 0, this.doubleTableTop);
/*      */ 
/*  191 */       this.itsData.itsDoubleTable = ((double[])localObject);
/*      */     }
/*  193 */     if ((this.exceptionTableTop != 0) && (this.itsData.itsExceptionTable.length != this.exceptionTableTop))
/*      */     {
/*  196 */       localObject = new int[this.exceptionTableTop];
/*  197 */       System.arraycopy(this.itsData.itsExceptionTable, 0, localObject, 0, this.exceptionTableTop);
/*      */ 
/*  199 */       this.itsData.itsExceptionTable = ((int[])localObject);
/*      */     }
/*      */ 
/*  202 */     this.itsData.itsMaxVars = this.scriptOrFn.getParamAndVarCount();
/*      */ 
/*  205 */     this.itsData.itsMaxFrameArray = (this.itsData.itsMaxVars + this.itsData.itsMaxLocals + this.itsData.itsMaxStack);
/*      */ 
/*  209 */     this.itsData.argNames = this.scriptOrFn.getParamAndVarNames();
/*  210 */     this.itsData.argIsConst = this.scriptOrFn.getParamAndVarConst();
/*  211 */     this.itsData.argCount = this.scriptOrFn.getParamCount();
/*      */ 
/*  213 */     this.itsData.encodedSourceStart = this.scriptOrFn.getEncodedSourceStart();
/*  214 */     this.itsData.encodedSourceEnd = this.scriptOrFn.getEncodedSourceEnd();
/*      */ 
/*  216 */     if (this.literalIds.size() != 0)
/*  217 */       this.itsData.literalIds = this.literalIds.toArray();
/*      */   }
/*      */ 
/*      */   private void generateNestedFunctions()
/*      */   {
/*  225 */     int i = this.scriptOrFn.getFunctionCount();
/*  226 */     if (i == 0) return;
/*      */ 
/*  228 */     InterpreterData[] arrayOfInterpreterData = new InterpreterData[i];
/*  229 */     for (int j = 0; j != i; j++) {
/*  230 */       FunctionNode localFunctionNode = this.scriptOrFn.getFunctionNode(j);
/*  231 */       CodeGenerator localCodeGenerator = new CodeGenerator();
/*  232 */       localCodeGenerator.compilerEnv = this.compilerEnv;
/*  233 */       localCodeGenerator.scriptOrFn = localFunctionNode;
/*  234 */       localCodeGenerator.itsData = new InterpreterData(this.itsData);
/*  235 */       localCodeGenerator.generateFunctionICode();
/*  236 */       arrayOfInterpreterData[j] = localCodeGenerator.itsData;
/*      */     }
/*  238 */     this.itsData.itsNestedFunctions = arrayOfInterpreterData;
/*      */   }
/*      */ 
/*      */   private void generateRegExpLiterals()
/*      */   {
/*  243 */     int i = this.scriptOrFn.getRegexpCount();
/*  244 */     if (i == 0) return;
/*      */ 
/*  246 */     Context localContext = Context.getContext();
/*  247 */     RegExpProxy localRegExpProxy = ScriptRuntime.checkRegExpProxy(localContext);
/*  248 */     Object[] arrayOfObject = new Object[i];
/*  249 */     for (int j = 0; j != i; j++) {
/*  250 */       String str1 = this.scriptOrFn.getRegexpString(j);
/*  251 */       String str2 = this.scriptOrFn.getRegexpFlags(j);
/*  252 */       arrayOfObject[j] = localRegExpProxy.compileRegExp(localContext, str1, str2);
/*      */     }
/*  254 */     this.itsData.itsRegExpLiterals = arrayOfObject;
/*      */   }
/*      */ 
/*      */   private void updateLineNumber(Node paramNode)
/*      */   {
/*  259 */     int i = paramNode.getLineno();
/*  260 */     if ((i != this.lineNumber) && (i >= 0)) {
/*  261 */       if (this.itsData.firstLinePC < 0) {
/*  262 */         this.itsData.firstLinePC = i;
/*      */       }
/*  264 */       this.lineNumber = i;
/*  265 */       addIcode(-26);
/*  266 */       addUint16(i & 0xFFFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   private RuntimeException badTree(Node paramNode)
/*      */   {
/*  272 */     throw new RuntimeException(paramNode.toString());
/*      */   }
/*      */ 
/*      */   private void visitStatement(Node paramNode, int paramInt)
/*      */   {
/*  277 */     int i = paramNode.getType();
/*  278 */     Node localNode1 = paramNode.getFirstChild();
/*      */     int j;
/*  279 */     switch (i)
/*      */     {
/*      */     case 109:
/*  283 */       j = paramNode.getExistingIntProp(1);
/*  284 */       int n = this.scriptOrFn.getFunctionNode(j).getFunctionType();
/*      */ 
/*  292 */       if (n == 3) {
/*  293 */         addIndexOp(-20, j);
/*      */       }
/*  295 */       else if (n != 1) {
/*  296 */         throw Kit.codeBug();
/*      */       }
/*      */ 
/*  304 */       if (!this.itsInFunctionFlag) {
/*  305 */         addIndexOp(-19, j);
/*  306 */         stackChange(1);
/*  307 */         addIcode(-5);
/*  308 */         stackChange(-1);
/*      */       }
/*      */ 
/*  311 */       break;
/*      */     case 123:
/*      */     case 128:
/*      */     case 129:
/*      */     case 130:
/*      */     case 132:
/*  318 */       updateLineNumber(paramNode);
/*      */     case 136:
/*      */     case 2:
/*      */     case 3:
/*      */     case 141:
/*      */     case 160:
/*      */     case 114:
/*      */     case 131:
/*      */     case 6:
/*      */     case 7:
/*      */     case 5:
/*      */     case 135:
/*      */     case 125:
/*      */     case 133:
/*      */     case 134:
/*      */     case 81:
/*      */     case 57:
/*      */     case 50:
/*      */     case 51:
/*      */     case 4:
/*      */     case 64:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case -62:
/*      */     default:
/*  321 */       while (localNode1 != null) {
/*  322 */         visitStatement(localNode1, paramInt);
/*  323 */         localNode1 = localNode1.getNext(); continue;
/*      */ 
/*  328 */         visitExpression(localNode1, 0);
/*  329 */         addToken(2);
/*  330 */         stackChange(-1);
/*  331 */         break;
/*      */ 
/*  334 */         addToken(3);
/*  335 */         break;
/*      */ 
/*  339 */         j = allocLocal();
/*  340 */         paramNode.putIntProp(2, j);
/*  341 */         updateLineNumber(paramNode);
/*  342 */         while (localNode1 != null) {
/*  343 */           visitStatement(localNode1, paramInt);
/*  344 */           localNode1 = localNode1.getNext();
/*      */         }
/*  346 */         addIndexOp(-56, j);
/*  347 */         releaseLocal(j);
/*      */ 
/*  349 */         break;
/*      */ 
/*  352 */         addIcode(-64);
/*  353 */         break;
/*      */ 
/*  356 */         updateLineNumber(paramNode);
/*      */ 
/*  360 */         visitExpression(localNode1, 0);
/*  361 */         for (Object localObject = (Jump)localNode1.getNext(); 
/*  362 */           localObject != null; 
/*  363 */           localObject = (Jump)((Jump)localObject).getNext())
/*      */         {
/*  365 */           if (((Jump)localObject).getType() != 115)
/*  366 */             throw badTree((Node)localObject);
/*  367 */           Node localNode2 = ((Jump)localObject).getFirstChild();
/*  368 */           addIcode(-1);
/*  369 */           stackChange(1);
/*  370 */           visitExpression(localNode2, 0);
/*  371 */           addToken(46);
/*  372 */           stackChange(-1);
/*      */ 
/*  375 */           addGoto(((Jump)localObject).target, -6);
/*  376 */           stackChange(-1);
/*      */         }
/*  378 */         addIcode(-4);
/*  379 */         stackChange(-1);
/*      */ 
/*  381 */         break;
/*      */ 
/*  384 */         markTargetLabel(paramNode);
/*  385 */         break;
/*      */ 
/*  390 */         localObject = ((Jump)paramNode).target;
/*  391 */         visitExpression(localNode1, 0);
/*  392 */         addGoto((Node)localObject, i);
/*  393 */         stackChange(-1);
/*      */ 
/*  395 */         break;
/*      */ 
/*  399 */         localObject = ((Jump)paramNode).target;
/*  400 */         addGoto((Node)localObject, i);
/*      */ 
/*  402 */         break;
/*      */ 
/*  406 */         localObject = ((Jump)paramNode).target;
/*  407 */         addGoto((Node)localObject, -23);
/*      */ 
/*  409 */         break;
/*      */ 
/*  414 */         stackChange(1);
/*  415 */         int k = getLocalBlockRef(paramNode);
/*  416 */         addIndexOp(-24, k);
/*  417 */         stackChange(-1);
/*  418 */         while (localNode1 != null) {
/*  419 */           visitStatement(localNode1, paramInt);
/*  420 */           localNode1 = localNode1.getNext();
/*      */         }
/*  422 */         addIndexOp(-25, k);
/*      */ 
/*  424 */         break;
/*      */ 
/*  428 */         updateLineNumber(paramNode);
/*  429 */         visitExpression(localNode1, 0);
/*  430 */         addIcode(i == 133 ? -4 : -5);
/*  431 */         stackChange(-1);
/*  432 */         break;
/*      */ 
/*  436 */         Jump localJump = (Jump)paramNode;
/*  437 */         int i1 = getLocalBlockRef(localJump);
/*  438 */         int i2 = allocLocal();
/*      */ 
/*  440 */         addIndexOp(-13, i2);
/*      */ 
/*  442 */         int i3 = this.iCodeTop;
/*  443 */         boolean bool = this.itsInTryFlag;
/*  444 */         this.itsInTryFlag = true;
/*  445 */         while (localNode1 != null) {
/*  446 */           visitStatement(localNode1, paramInt);
/*  447 */           localNode1 = localNode1.getNext();
/*      */         }
/*  449 */         this.itsInTryFlag = bool;
/*      */ 
/*  451 */         Node localNode3 = localJump.target;
/*  452 */         if (localNode3 != null) {
/*  453 */           int i4 = this.labelTable[getTargetLabel(localNode3)];
/*      */ 
/*  455 */           addExceptionHandler(i3, i4, i4, false, i1, i2);
/*      */         }
/*      */ 
/*  459 */         Node localNode4 = localJump.getFinally();
/*  460 */         if (localNode4 != null) {
/*  461 */           int i5 = this.labelTable[getTargetLabel(localNode4)];
/*      */ 
/*  463 */           addExceptionHandler(i3, i5, i5, true, i1, i2);
/*      */         }
/*      */ 
/*  468 */         addIndexOp(-56, i2);
/*  469 */         releaseLocal(i2);
/*      */ 
/*  471 */         break;
/*      */ 
/*  475 */         int m = getLocalBlockRef(paramNode);
/*  476 */         i1 = paramNode.getExistingIntProp(14);
/*  477 */         String str = localNode1.getString();
/*  478 */         localNode1 = localNode1.getNext();
/*  479 */         visitExpression(localNode1, 0);
/*  480 */         addStringPrefix(str);
/*  481 */         addIndexPrefix(m);
/*  482 */         addToken(57);
/*  483 */         addUint8(i1 != 0 ? 1 : 0);
/*  484 */         stackChange(-1);
/*      */ 
/*  486 */         break;
/*      */ 
/*  489 */         updateLineNumber(paramNode);
/*  490 */         visitExpression(localNode1, 0);
/*  491 */         addToken(50);
/*  492 */         addUint16(this.lineNumber & 0xFFFF);
/*  493 */         stackChange(-1);
/*  494 */         break;
/*      */ 
/*  497 */         updateLineNumber(paramNode);
/*  498 */         addIndexOp(51, getLocalBlockRef(paramNode));
/*  499 */         break;
/*      */ 
/*  502 */         updateLineNumber(paramNode);
/*  503 */         if (paramNode.getIntProp(20, 0) != 0)
/*      */         {
/*  505 */           addIcode(-63);
/*  506 */           addUint16(this.lineNumber & 0xFFFF);
/*  507 */         } else if (localNode1 != null) {
/*  508 */           visitExpression(localNode1, 1);
/*  509 */           addToken(4);
/*  510 */           stackChange(-1);
/*      */         } else {
/*  512 */           addIcode(-22);
/*      */ 
/*  514 */           break;
/*      */ 
/*  517 */           updateLineNumber(paramNode);
/*  518 */           addToken(64);
/*  519 */           break;
/*      */ 
/*  524 */           visitExpression(localNode1, 0);
/*  525 */           addIndexOp(i, getLocalBlockRef(paramNode));
/*  526 */           stackChange(-1);
/*  527 */           break;
/*      */ 
/*  530 */           break;
/*      */ 
/*  533 */           throw badTree(paramNode);
/*      */         }
/*      */       }
/*      */     }
/*  536 */     if (this.stackDepth != paramInt)
/*  537 */       throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private void visitExpression(Node paramNode, int paramInt)
/*      */   {
/*  543 */     int i = paramNode.getType();
/*  544 */     Node localNode1 = paramNode.getFirstChild();
/*  545 */     int j = this.stackDepth;
/*      */     int k;
/*      */     int m;
/*      */     int i2;
/*      */     Object localObject;
/*      */     int i4;
/*      */     int i5;
/*      */     int i1;
/*  546 */     switch (i)
/*      */     {
/*      */     case 109:
/*  550 */       k = paramNode.getExistingIntProp(1);
/*  551 */       FunctionNode localFunctionNode = this.scriptOrFn.getFunctionNode(k);
/*      */ 
/*  553 */       if (localFunctionNode.getFunctionType() != 2) {
/*  554 */         throw Kit.codeBug();
/*      */       }
/*  556 */       addIndexOp(-19, k);
/*  557 */       stackChange(1);
/*      */ 
/*  559 */       break;
/*      */     case 54:
/*  563 */       k = getLocalBlockRef(paramNode);
/*  564 */       addIndexOp(54, k);
/*  565 */       stackChange(1);
/*      */ 
/*  567 */       break;
/*      */     case 89:
/*  571 */       Node localNode2 = paramNode.getLastChild();
/*  572 */       while (localNode1 != localNode2) {
/*  573 */         visitExpression(localNode1, 0);
/*  574 */         addIcode(-4);
/*  575 */         stackChange(-1);
/*  576 */         localNode1 = localNode1.getNext();
/*      */       }
/*      */ 
/*  579 */       visitExpression(localNode1, paramInt & 0x1);
/*      */ 
/*  581 */       break;
/*      */     case 138:
/*  586 */       stackChange(1);
/*  587 */       break;
/*      */     case 30:
/*      */     case 38:
/*      */     case 70:
/*  593 */       if (i == 30)
/*  594 */         visitExpression(localNode1, 0);
/*      */       else {
/*  596 */         generateCallFunAndThis(localNode1);
/*      */       }
/*  598 */       m = 0;
/*  599 */       while ((localNode1 = localNode1.getNext()) != null) {
/*  600 */         visitExpression(localNode1, 0);
/*  601 */         m++;
/*      */       }
/*  603 */       i2 = paramNode.getIntProp(10, 0);
/*      */ 
/*  605 */       if (i2 != 0)
/*      */       {
/*  607 */         addIndexOp(-21, m);
/*  608 */         addUint8(i2);
/*  609 */         addUint8(i == 30 ? 1 : 0);
/*  610 */         addUint16(this.lineNumber & 0xFFFF);
/*      */       }
/*      */       else
/*      */       {
/*  615 */         if ((i == 38) && ((paramInt & 0x1) != 0) && (!this.compilerEnv.isGenerateDebugInfo()) && (!this.itsInTryFlag))
/*      */         {
/*  618 */           i = -55;
/*      */         }
/*  620 */         addIndexOp(i, m);
/*      */       }
/*      */ 
/*  623 */       if (i == 30)
/*      */       {
/*  625 */         stackChange(-m);
/*      */       }
/*      */       else
/*      */       {
/*  629 */         stackChange(-1 - m);
/*      */       }
/*  631 */       if (m > this.itsData.itsMaxCalleeArgs) {
/*  632 */         this.itsData.itsMaxCalleeArgs = m;
/*      */       }
/*      */ 
/*  635 */       break;
/*      */     case 104:
/*      */     case 105:
/*  640 */       visitExpression(localNode1, 0);
/*  641 */       addIcode(-1);
/*  642 */       stackChange(1);
/*  643 */       m = this.iCodeTop;
/*  644 */       i2 = i == 105 ? 7 : 6;
/*  645 */       addGotoOp(i2);
/*  646 */       stackChange(-1);
/*  647 */       addIcode(-4);
/*  648 */       stackChange(-1);
/*  649 */       localNode1 = localNode1.getNext();
/*      */ 
/*  651 */       visitExpression(localNode1, paramInt & 0x1);
/*  652 */       resolveForwardGoto(m);
/*      */ 
/*  654 */       break;
/*      */     case 102:
/*  658 */       localObject = localNode1.getNext();
/*  659 */       Node localNode4 = ((Node)localObject).getNext();
/*  660 */       visitExpression(localNode1, 0);
/*  661 */       i4 = this.iCodeTop;
/*  662 */       addGotoOp(7);
/*  663 */       stackChange(-1);
/*      */ 
/*  665 */       visitExpression((Node)localObject, paramInt & 0x1);
/*  666 */       i5 = this.iCodeTop;
/*  667 */       addGotoOp(5);
/*  668 */       resolveForwardGoto(i4);
/*  669 */       this.stackDepth = j;
/*      */ 
/*  671 */       visitExpression(localNode4, paramInt & 0x1);
/*  672 */       resolveForwardGoto(i5);
/*      */ 
/*  674 */       break;
/*      */     case 33:
/*      */     case 34:
/*  678 */       visitExpression(localNode1, 0);
/*  679 */       localNode1 = localNode1.getNext();
/*  680 */       addStringOp(i, localNode1.getString());
/*  681 */       break;
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 31:
/*      */     case 36:
/*      */     case 46:
/*      */     case 47:
/*      */     case 52:
/*      */     case 53:
/*  706 */       visitExpression(localNode1, 0);
/*  707 */       localNode1 = localNode1.getNext();
/*  708 */       visitExpression(localNode1, 0);
/*  709 */       addToken(i);
/*  710 */       stackChange(-1);
/*  711 */       break;
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 32:
/*      */     case 126:
/*  719 */       visitExpression(localNode1, 0);
/*  720 */       if (i == 126) {
/*  721 */         addIcode(-4);
/*  722 */         addIcode(-50);
/*      */       } else {
/*  724 */         addToken(i);
/*      */       }
/*  726 */       break;
/*      */     case 67:
/*      */     case 69:
/*  730 */       visitExpression(localNode1, 0);
/*  731 */       addToken(i);
/*  732 */       break;
/*      */     case 35:
/*      */     case 139:
/*  737 */       visitExpression(localNode1, 0);
/*  738 */       localNode1 = localNode1.getNext();
/*  739 */       localObject = localNode1.getString();
/*  740 */       localNode1 = localNode1.getNext();
/*  741 */       if (i == 139) {
/*  742 */         addIcode(-1);
/*  743 */         stackChange(1);
/*  744 */         addStringOp(33, (String)localObject);
/*      */ 
/*  746 */         stackChange(-1);
/*      */       }
/*  748 */       visitExpression(localNode1, 0);
/*  749 */       addStringOp(35, (String)localObject);
/*  750 */       stackChange(-1);
/*      */ 
/*  752 */       break;
/*      */     case 37:
/*      */     case 140:
/*  756 */       visitExpression(localNode1, 0);
/*  757 */       localNode1 = localNode1.getNext();
/*  758 */       visitExpression(localNode1, 0);
/*  759 */       localNode1 = localNode1.getNext();
/*  760 */       if (i == 140) {
/*  761 */         addIcode(-2);
/*  762 */         stackChange(2);
/*  763 */         addToken(36);
/*  764 */         stackChange(-1);
/*      */ 
/*  766 */         stackChange(-1);
/*      */       }
/*  768 */       visitExpression(localNode1, 0);
/*  769 */       addToken(37);
/*  770 */       stackChange(-2);
/*  771 */       break;
/*      */     case 68:
/*      */     case 142:
/*  775 */       visitExpression(localNode1, 0);
/*  776 */       localNode1 = localNode1.getNext();
/*  777 */       if (i == 142) {
/*  778 */         addIcode(-1);
/*  779 */         stackChange(1);
/*  780 */         addToken(67);
/*      */ 
/*  782 */         stackChange(-1);
/*      */       }
/*  784 */       visitExpression(localNode1, 0);
/*  785 */       addToken(68);
/*  786 */       stackChange(-1);
/*  787 */       break;
/*      */     case 8:
/*      */     case 73:
/*  792 */       localObject = localNode1.getString();
/*  793 */       visitExpression(localNode1, 0);
/*  794 */       localNode1 = localNode1.getNext();
/*  795 */       visitExpression(localNode1, 0);
/*  796 */       addStringOp(i, (String)localObject);
/*  797 */       stackChange(-1);
/*      */ 
/*  799 */       break;
/*      */     case 155:
/*  803 */       localObject = localNode1.getString();
/*  804 */       visitExpression(localNode1, 0);
/*  805 */       localNode1 = localNode1.getNext();
/*  806 */       visitExpression(localNode1, 0);
/*  807 */       addStringOp(-59, (String)localObject);
/*  808 */       stackChange(-1);
/*      */ 
/*  810 */       break;
/*      */     case 137:
/*  814 */       int n = -1;
/*      */ 
/*  817 */       if ((this.itsInFunctionFlag) && (!this.itsData.itsNeedsActivation))
/*  818 */         n = this.scriptOrFn.getIndexForNameNode(paramNode);
/*  819 */       if (n == -1) {
/*  820 */         addStringOp(-14, paramNode.getString());
/*  821 */         stackChange(1);
/*      */       } else {
/*  823 */         addVarOp(55, n);
/*  824 */         stackChange(1);
/*  825 */         addToken(32);
/*      */       }
/*      */ 
/*  828 */       break;
/*      */     case 39:
/*      */     case 41:
/*      */     case 49:
/*  833 */       addStringOp(i, paramNode.getString());
/*  834 */       stackChange(1);
/*  835 */       break;
/*      */     case 106:
/*      */     case 107:
/*  839 */       visitIncDec(paramNode, localNode1);
/*  840 */       break;
/*      */     case 40:
/*  844 */       double d = paramNode.getDouble();
/*  845 */       i4 = (int)d;
/*  846 */       if (i4 == d) {
/*  847 */         if (i4 == 0) {
/*  848 */           addIcode(-51);
/*      */ 
/*  850 */           if (1.0D / d < 0.0D)
/*  851 */             addToken(29);
/*      */         }
/*  853 */         else if (i4 == 1) {
/*  854 */           addIcode(-52);
/*  855 */         } else if ((short)i4 == i4) {
/*  856 */           addIcode(-27);
/*      */ 
/*  858 */           addUint16(i4 & 0xFFFF);
/*      */         } else {
/*  860 */           addIcode(-28);
/*  861 */           addInt(i4);
/*      */         }
/*      */       } else {
/*  864 */         i5 = getDoubleIndex(d);
/*  865 */         addIndexOp(40, i5);
/*      */       }
/*  867 */       stackChange(1);
/*      */ 
/*  869 */       break;
/*      */     case 55:
/*  873 */       if (this.itsData.itsNeedsActivation) Kit.codeBug();
/*  874 */       i1 = this.scriptOrFn.getIndexForNameNode(paramNode);
/*  875 */       addVarOp(55, i1);
/*  876 */       stackChange(1);
/*      */ 
/*  878 */       break;
/*      */     case 56:
/*  882 */       if (this.itsData.itsNeedsActivation) Kit.codeBug();
/*  883 */       i1 = this.scriptOrFn.getIndexForNameNode(localNode1);
/*  884 */       localNode1 = localNode1.getNext();
/*  885 */       visitExpression(localNode1, 0);
/*  886 */       addVarOp(56, i1);
/*      */ 
/*  888 */       break;
/*      */     case 156:
/*  892 */       if (this.itsData.itsNeedsActivation) Kit.codeBug();
/*  893 */       i1 = this.scriptOrFn.getIndexForNameNode(localNode1);
/*  894 */       localNode1 = localNode1.getNext();
/*  895 */       visitExpression(localNode1, 0);
/*  896 */       addVarOp(156, i1);
/*      */ 
/*  898 */       break;
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 63:
/*  905 */       addToken(i);
/*  906 */       stackChange(1);
/*  907 */       break;
/*      */     case 61:
/*      */     case 62:
/*  911 */       addIndexOp(i, getLocalBlockRef(paramNode));
/*  912 */       stackChange(1);
/*  913 */       break;
/*      */     case 48:
/*  917 */       i1 = paramNode.getExistingIntProp(4);
/*  918 */       addIndexOp(48, i1);
/*  919 */       stackChange(1);
/*      */ 
/*  921 */       break;
/*      */     case 65:
/*      */     case 66:
/*  925 */       visitLiteral(paramNode, localNode1);
/*  926 */       break;
/*      */     case 157:
/*  929 */       visitArrayComprehension(paramNode, localNode1, localNode1.getNext());
/*  930 */       break;
/*      */     case 71:
/*  933 */       visitExpression(localNode1, 0);
/*  934 */       addStringOp(i, (String)paramNode.getProp(17));
/*  935 */       break;
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*  942 */       i1 = paramNode.getIntProp(16, 0);
/*      */ 
/*  944 */       int i3 = 0;
/*      */       do {
/*  946 */         visitExpression(localNode1, 0);
/*  947 */         i3++;
/*  948 */         localNode1 = localNode1.getNext();
/*  949 */       }while (localNode1 != null);
/*  950 */       addIndexOp(i, i1);
/*  951 */       stackChange(1 - i3);
/*      */ 
/*  953 */       break;
/*      */     case 146:
/*  958 */       updateLineNumber(paramNode);
/*  959 */       visitExpression(localNode1, 0);
/*  960 */       addIcode(-53);
/*  961 */       stackChange(-1);
/*  962 */       i1 = this.iCodeTop;
/*  963 */       visitExpression(localNode1.getNext(), 0);
/*  964 */       addBackwardGoto(-54, i1);
/*      */ 
/*  966 */       break;
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*  971 */       visitExpression(localNode1, 0);
/*  972 */       addToken(i);
/*  973 */       break;
/*      */     case 72:
/*  976 */       if (localNode1 != null) {
/*  977 */         visitExpression(localNode1, 0);
/*      */       } else {
/*  979 */         addIcode(-50);
/*  980 */         stackChange(1);
/*      */       }
/*  982 */       addToken(72);
/*  983 */       addUint16(paramNode.getLineno() & 0xFFFF);
/*  984 */       break;
/*      */     case 159:
/*  987 */       Node localNode3 = paramNode.getFirstChild();
/*  988 */       Node localNode5 = localNode3.getNext();
/*  989 */       visitExpression(localNode3.getFirstChild(), 0);
/*  990 */       addToken(2);
/*  991 */       stackChange(-1);
/*  992 */       visitExpression(localNode5.getFirstChild(), 0);
/*  993 */       addToken(3);
/*  994 */       break;
/*      */     case 50:
/*      */     case 51:
/*      */     case 57:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 64:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 90:
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 100:
/*      */     case 101:
/*      */     case 103:
/*      */     case 108:
/*      */     case 110:
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 115:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 120:
/*      */     case 121:
/*      */     case 122:
/*      */     case 123:
/*      */     case 124:
/*      */     case 125:
/*      */     case 127:
/*      */     case 128:
/*      */     case 129:
/*      */     case 130:
/*      */     case 131:
/*      */     case 132:
/*      */     case 133:
/*      */     case 134:
/*      */     case 135:
/*      */     case 136:
/*      */     case 141:
/*      */     case 143:
/*      */     case 144:
/*      */     case 145:
/*      */     case 147:
/*      */     case 148:
/*      */     case 149:
/*      */     case 150:
/*      */     case 151:
/*      */     case 152:
/*      */     case 153:
/*      */     case 154:
/*      */     case 158:
/*      */     default:
/*  998 */       throw badTree(paramNode);
/*      */     }
/* 1000 */     if (j + 1 != this.stackDepth)
/* 1001 */       Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private void generateCallFunAndThis(Node paramNode)
/*      */   {
/* 1008 */     int i = paramNode.getType();
/*      */     Object localObject;
/* 1009 */     switch (i) {
/*      */     case 39:
/* 1011 */       localObject = paramNode.getString();
/*      */ 
/* 1013 */       addStringOp(-15, (String)localObject);
/* 1014 */       stackChange(2);
/* 1015 */       break;
/*      */     case 33:
/*      */     case 36:
/* 1019 */       localObject = paramNode.getFirstChild();
/* 1020 */       visitExpression((Node)localObject, 0);
/* 1021 */       Node localNode = ((Node)localObject).getNext();
/* 1022 */       if (i == 33) {
/* 1023 */         String str = localNode.getString();
/*      */ 
/* 1025 */         addStringOp(-16, str);
/* 1026 */         stackChange(1);
/*      */       } else {
/* 1028 */         visitExpression(localNode, 0);
/*      */ 
/* 1030 */         addIcode(-17);
/*      */       }
/* 1032 */       break;
/*      */     default:
/* 1036 */       visitExpression(paramNode, 0);
/*      */ 
/* 1038 */       addIcode(-18);
/* 1039 */       stackChange(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void visitIncDec(Node paramNode1, Node paramNode2)
/*      */   {
/* 1047 */     int i = paramNode1.getExistingIntProp(13);
/* 1048 */     int j = paramNode2.getType();
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 1049 */     switch (j) {
/*      */     case 55:
/* 1051 */       if (this.itsData.itsNeedsActivation) Kit.codeBug();
/* 1052 */       int k = this.scriptOrFn.getIndexForNameNode(paramNode2);
/* 1053 */       addVarOp(-7, k);
/* 1054 */       addUint8(i);
/* 1055 */       stackChange(1);
/* 1056 */       break;
/*      */     case 39:
/* 1059 */       localObject1 = paramNode2.getString();
/* 1060 */       addStringOp(-8, (String)localObject1);
/* 1061 */       addUint8(i);
/* 1062 */       stackChange(1);
/* 1063 */       break;
/*      */     case 33:
/* 1066 */       localObject1 = paramNode2.getFirstChild();
/* 1067 */       visitExpression((Node)localObject1, 0);
/* 1068 */       localObject2 = ((Node)localObject1).getNext().getString();
/* 1069 */       addStringOp(-9, (String)localObject2);
/* 1070 */       addUint8(i);
/* 1071 */       break;
/*      */     case 36:
/* 1074 */       localObject1 = paramNode2.getFirstChild();
/* 1075 */       visitExpression((Node)localObject1, 0);
/* 1076 */       localObject2 = ((Node)localObject1).getNext();
/* 1077 */       visitExpression((Node)localObject2, 0);
/* 1078 */       addIcode(-10);
/* 1079 */       addUint8(i);
/* 1080 */       stackChange(-1);
/* 1081 */       break;
/*      */     case 67:
/* 1084 */       localObject1 = paramNode2.getFirstChild();
/* 1085 */       visitExpression((Node)localObject1, 0);
/* 1086 */       addIcode(-11);
/* 1087 */       addUint8(i);
/* 1088 */       break;
/*      */     default:
/* 1091 */       throw badTree(paramNode1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void visitLiteral(Node paramNode1, Node paramNode2)
/*      */   {
/* 1098 */     int i = paramNode1.getType();
/*      */ 
/* 1100 */     Object[] arrayOfObject = null;
/*      */     int j;
/* 1101 */     if (i == 65) {
/* 1102 */       j = 0;
/* 1103 */       for (Node localNode = paramNode2; localNode != null; localNode = localNode.getNext())
/* 1104 */         j++;
/*      */     }
/* 1106 */     else if (i == 66) {
/* 1107 */       arrayOfObject = (Object[])paramNode1.getProp(12);
/* 1108 */       j = arrayOfObject.length;
/*      */     } else {
/* 1110 */       throw badTree(paramNode1);
/*      */     }
/* 1112 */     addIndexOp(-29, j);
/* 1113 */     stackChange(2);
/* 1114 */     while (paramNode2 != null) {
/* 1115 */       int k = paramNode2.getType();
/* 1116 */       if (k == 151) {
/* 1117 */         visitExpression(paramNode2.getFirstChild(), 0);
/* 1118 */         addIcode(-57);
/* 1119 */       } else if (k == 152) {
/* 1120 */         visitExpression(paramNode2.getFirstChild(), 0);
/* 1121 */         addIcode(-58);
/*      */       } else {
/* 1123 */         visitExpression(paramNode2, 0);
/* 1124 */         addIcode(-30);
/*      */       }
/* 1126 */       stackChange(-1);
/* 1127 */       paramNode2 = paramNode2.getNext();
/*      */     }
/* 1129 */     if (i == 65) {
/* 1130 */       int[] arrayOfInt = (int[])paramNode1.getProp(11);
/* 1131 */       if (arrayOfInt == null) {
/* 1132 */         addToken(65);
/*      */       } else {
/* 1134 */         int n = this.literalIds.size();
/* 1135 */         this.literalIds.add(arrayOfInt);
/* 1136 */         addIndexOp(-31, n);
/*      */       }
/*      */     } else {
/* 1139 */       int m = this.literalIds.size();
/* 1140 */       this.literalIds.add(arrayOfObject);
/* 1141 */       addIndexOp(66, m);
/*      */     }
/* 1143 */     stackChange(-1);
/*      */   }
/*      */ 
/*      */   private void visitArrayComprehension(Node paramNode1, Node paramNode2, Node paramNode3)
/*      */   {
/* 1153 */     visitStatement(paramNode2, this.stackDepth);
/* 1154 */     visitExpression(paramNode3, 0);
/*      */   }
/*      */ 
/*      */   private int getLocalBlockRef(Node paramNode)
/*      */   {
/* 1159 */     Node localNode = (Node)paramNode.getProp(3);
/* 1160 */     return localNode.getExistingIntProp(2);
/*      */   }
/*      */ 
/*      */   private int getTargetLabel(Node paramNode)
/*      */   {
/* 1165 */     int i = paramNode.labelId();
/* 1166 */     if (i != -1) {
/* 1167 */       return i;
/*      */     }
/* 1169 */     i = this.labelTableTop;
/* 1170 */     if ((this.labelTable == null) || (i == this.labelTable.length)) {
/* 1171 */       if (this.labelTable == null) {
/* 1172 */         this.labelTable = new int[32];
/*      */       } else {
/* 1174 */         int[] arrayOfInt = new int[this.labelTable.length * 2];
/* 1175 */         System.arraycopy(this.labelTable, 0, arrayOfInt, 0, i);
/* 1176 */         this.labelTable = arrayOfInt;
/*      */       }
/*      */     }
/* 1179 */     this.labelTableTop = (i + 1);
/* 1180 */     this.labelTable[i] = -1;
/*      */ 
/* 1182 */     paramNode.labelId(i);
/* 1183 */     return i;
/*      */   }
/*      */ 
/*      */   private void markTargetLabel(Node paramNode)
/*      */   {
/* 1188 */     int i = getTargetLabel(paramNode);
/* 1189 */     if (this.labelTable[i] != -1)
/*      */     {
/* 1191 */       Kit.codeBug();
/*      */     }
/* 1193 */     this.labelTable[i] = this.iCodeTop;
/*      */   }
/*      */ 
/*      */   private void addGoto(Node paramNode, int paramInt)
/*      */   {
/* 1198 */     int i = getTargetLabel(paramNode);
/* 1199 */     if (i >= this.labelTableTop) Kit.codeBug();
/* 1200 */     int j = this.labelTable[i];
/*      */ 
/* 1202 */     if (j != -1) {
/* 1203 */       addBackwardGoto(paramInt, j);
/*      */     } else {
/* 1205 */       int k = this.iCodeTop;
/* 1206 */       addGotoOp(paramInt);
/* 1207 */       int m = this.fixupTableTop;
/* 1208 */       if ((this.fixupTable == null) || (m == this.fixupTable.length)) {
/* 1209 */         if (this.fixupTable == null) {
/* 1210 */           this.fixupTable = new long[40];
/*      */         } else {
/* 1212 */           long[] arrayOfLong = new long[this.fixupTable.length * 2];
/* 1213 */           System.arraycopy(this.fixupTable, 0, arrayOfLong, 0, m);
/* 1214 */           this.fixupTable = arrayOfLong;
/*      */         }
/*      */       }
/* 1217 */       this.fixupTableTop = (m + 1);
/* 1218 */       this.fixupTable[m] = (i << 32 | k);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fixLabelGotos()
/*      */   {
/* 1224 */     for (int i = 0; i < this.fixupTableTop; i++) {
/* 1225 */       long l = this.fixupTable[i];
/* 1226 */       int j = (int)(l >> 32);
/* 1227 */       int k = (int)l;
/* 1228 */       int m = this.labelTable[j];
/* 1229 */       if (m == -1)
/*      */       {
/* 1231 */         throw Kit.codeBug();
/*      */       }
/* 1233 */       resolveGoto(k, m);
/*      */     }
/* 1235 */     this.fixupTableTop = 0;
/*      */   }
/*      */ 
/*      */   private void addBackwardGoto(int paramInt1, int paramInt2)
/*      */   {
/* 1240 */     int i = this.iCodeTop;
/*      */ 
/* 1242 */     if (i <= paramInt2) throw Kit.codeBug();
/* 1243 */     addGotoOp(paramInt1);
/* 1244 */     resolveGoto(i, paramInt2);
/*      */   }
/*      */ 
/*      */   private void resolveForwardGoto(int paramInt)
/*      */   {
/* 1250 */     if (this.iCodeTop < paramInt + 3) throw Kit.codeBug();
/* 1251 */     resolveGoto(paramInt, this.iCodeTop);
/*      */   }
/*      */ 
/*      */   private void resolveGoto(int paramInt1, int paramInt2)
/*      */   {
/* 1256 */     int i = paramInt2 - paramInt1;
/*      */ 
/* 1258 */     if ((0 <= i) && (i <= 2)) throw Kit.codeBug();
/* 1259 */     int j = paramInt1 + 1;
/* 1260 */     if (i != (short)i) {
/* 1261 */       if (this.itsData.longJumps == null) {
/* 1262 */         this.itsData.longJumps = new UintMap();
/*      */       }
/* 1264 */       this.itsData.longJumps.put(j, paramInt2);
/* 1265 */       i = 0;
/*      */     }
/* 1267 */     byte[] arrayOfByte = this.itsData.itsICode;
/* 1268 */     arrayOfByte[j] = ((byte)(i >> 8));
/* 1269 */     arrayOfByte[(j + 1)] = ((byte)i);
/*      */   }
/*      */ 
/*      */   private void addToken(int paramInt)
/*      */   {
/* 1274 */     if (!Icode.validTokenCode(paramInt)) throw Kit.codeBug();
/* 1275 */     addUint8(paramInt);
/*      */   }
/*      */ 
/*      */   private void addIcode(int paramInt)
/*      */   {
/* 1280 */     if (!Icode.validIcode(paramInt)) throw Kit.codeBug();
/*      */ 
/* 1282 */     addUint8(paramInt & 0xFF);
/*      */   }
/*      */ 
/*      */   private void addUint8(int paramInt)
/*      */   {
/* 1287 */     if ((paramInt & 0xFFFFFF00) != 0) throw Kit.codeBug();
/* 1288 */     byte[] arrayOfByte = this.itsData.itsICode;
/* 1289 */     int i = this.iCodeTop;
/* 1290 */     if (i == arrayOfByte.length) {
/* 1291 */       arrayOfByte = increaseICodeCapacity(1);
/*      */     }
/* 1293 */     arrayOfByte[i] = ((byte)paramInt);
/* 1294 */     this.iCodeTop = (i + 1);
/*      */   }
/*      */ 
/*      */   private void addUint16(int paramInt)
/*      */   {
/* 1299 */     if ((paramInt & 0xFFFF0000) != 0) throw Kit.codeBug();
/* 1300 */     byte[] arrayOfByte = this.itsData.itsICode;
/* 1301 */     int i = this.iCodeTop;
/* 1302 */     if (i + 2 > arrayOfByte.length) {
/* 1303 */       arrayOfByte = increaseICodeCapacity(2);
/*      */     }
/* 1305 */     arrayOfByte[i] = ((byte)(paramInt >>> 8));
/* 1306 */     arrayOfByte[(i + 1)] = ((byte)paramInt);
/* 1307 */     this.iCodeTop = (i + 2);
/*      */   }
/*      */ 
/*      */   private void addInt(int paramInt)
/*      */   {
/* 1312 */     byte[] arrayOfByte = this.itsData.itsICode;
/* 1313 */     int i = this.iCodeTop;
/* 1314 */     if (i + 4 > arrayOfByte.length) {
/* 1315 */       arrayOfByte = increaseICodeCapacity(4);
/*      */     }
/* 1317 */     arrayOfByte[i] = ((byte)(paramInt >>> 24));
/* 1318 */     arrayOfByte[(i + 1)] = ((byte)(paramInt >>> 16));
/* 1319 */     arrayOfByte[(i + 2)] = ((byte)(paramInt >>> 8));
/* 1320 */     arrayOfByte[(i + 3)] = ((byte)paramInt);
/* 1321 */     this.iCodeTop = (i + 4);
/*      */   }
/*      */ 
/*      */   private int getDoubleIndex(double paramDouble)
/*      */   {
/* 1326 */     int i = this.doubleTableTop;
/* 1327 */     if (i == 0) {
/* 1328 */       this.itsData.itsDoubleTable = new double[64];
/* 1329 */     } else if (this.itsData.itsDoubleTable.length == i) {
/* 1330 */       double[] arrayOfDouble = new double[i * 2];
/* 1331 */       System.arraycopy(this.itsData.itsDoubleTable, 0, arrayOfDouble, 0, i);
/* 1332 */       this.itsData.itsDoubleTable = arrayOfDouble;
/*      */     }
/* 1334 */     this.itsData.itsDoubleTable[i] = paramDouble;
/* 1335 */     this.doubleTableTop = (i + 1);
/* 1336 */     return i;
/*      */   }
/*      */ 
/*      */   private void addGotoOp(int paramInt)
/*      */   {
/* 1341 */     byte[] arrayOfByte = this.itsData.itsICode;
/* 1342 */     int i = this.iCodeTop;
/* 1343 */     if (i + 3 > arrayOfByte.length) {
/* 1344 */       arrayOfByte = increaseICodeCapacity(3);
/*      */     }
/* 1346 */     arrayOfByte[i] = ((byte)paramInt);
/*      */ 
/* 1348 */     this.iCodeTop = (i + 1 + 2);
/*      */   }
/*      */ 
/*      */   private void addVarOp(int paramInt1, int paramInt2)
/*      */   {
/* 1353 */     switch (paramInt1) {
/*      */     case 156:
/* 1355 */       if (paramInt2 < 128) {
/* 1356 */         addIcode(-61);
/* 1357 */         addUint8(paramInt2);
/* 1358 */         return;
/*      */       }
/* 1360 */       addIndexOp(-60, paramInt2);
/* 1361 */       return;
/*      */     case 55:
/*      */     case 56:
/* 1364 */       if (paramInt2 < 128) {
/* 1365 */         addIcode(paramInt1 == 55 ? -48 : -49);
/* 1366 */         addUint8(paramInt2);
/* 1367 */         return;
/*      */       }
/*      */ 
/*      */     case -7:
/* 1371 */       addIndexOp(paramInt1, paramInt2);
/* 1372 */       return;
/*      */     }
/* 1374 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private void addStringOp(int paramInt, String paramString)
/*      */   {
/* 1379 */     addStringPrefix(paramString);
/* 1380 */     if (Icode.validIcode(paramInt))
/* 1381 */       addIcode(paramInt);
/*      */     else
/* 1383 */       addToken(paramInt);
/*      */   }
/*      */ 
/*      */   private void addIndexOp(int paramInt1, int paramInt2)
/*      */   {
/* 1389 */     addIndexPrefix(paramInt2);
/* 1390 */     if (Icode.validIcode(paramInt1))
/* 1391 */       addIcode(paramInt1);
/*      */     else
/* 1393 */       addToken(paramInt1);
/*      */   }
/*      */ 
/*      */   private void addStringPrefix(String paramString)
/*      */   {
/* 1399 */     int i = this.strings.get(paramString, -1);
/* 1400 */     if (i == -1) {
/* 1401 */       i = this.strings.size();
/* 1402 */       this.strings.put(paramString, i);
/*      */     }
/* 1404 */     if (i < 4) {
/* 1405 */       addIcode(-41 - i);
/* 1406 */     } else if (i <= 255) {
/* 1407 */       addIcode(-45);
/* 1408 */       addUint8(i);
/* 1409 */     } else if (i <= 65535) {
/* 1410 */       addIcode(-46);
/* 1411 */       addUint16(i);
/*      */     } else {
/* 1413 */       addIcode(-47);
/* 1414 */       addInt(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addIndexPrefix(int paramInt)
/*      */   {
/* 1420 */     if (paramInt < 0) Kit.codeBug();
/* 1421 */     if (paramInt < 6) {
/* 1422 */       addIcode(-32 - paramInt);
/* 1423 */     } else if (paramInt <= 255) {
/* 1424 */       addIcode(-38);
/* 1425 */       addUint8(paramInt);
/* 1426 */     } else if (paramInt <= 65535) {
/* 1427 */       addIcode(-39);
/* 1428 */       addUint16(paramInt);
/*      */     } else {
/* 1430 */       addIcode(-40);
/* 1431 */       addInt(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addExceptionHandler(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5)
/*      */   {
/* 1439 */     int i = this.exceptionTableTop;
/* 1440 */     int[] arrayOfInt = this.itsData.itsExceptionTable;
/* 1441 */     if (arrayOfInt == null) {
/* 1442 */       if (i != 0) Kit.codeBug();
/* 1443 */       arrayOfInt = new int[12];
/* 1444 */       this.itsData.itsExceptionTable = arrayOfInt;
/* 1445 */     } else if (arrayOfInt.length == i) {
/* 1446 */       arrayOfInt = new int[arrayOfInt.length * 2];
/* 1447 */       System.arraycopy(this.itsData.itsExceptionTable, 0, arrayOfInt, 0, i);
/* 1448 */       this.itsData.itsExceptionTable = arrayOfInt;
/*      */     }
/* 1450 */     arrayOfInt[(i + 0)] = paramInt1;
/* 1451 */     arrayOfInt[(i + 1)] = paramInt2;
/* 1452 */     arrayOfInt[(i + 2)] = paramInt3;
/* 1453 */     arrayOfInt[(i + 3)] = (paramBoolean ? 1 : 0);
/* 1454 */     arrayOfInt[(i + 4)] = paramInt4;
/* 1455 */     arrayOfInt[(i + 5)] = paramInt5;
/*      */ 
/* 1457 */     this.exceptionTableTop = (i + 6);
/*      */   }
/*      */ 
/*      */   private byte[] increaseICodeCapacity(int paramInt)
/*      */   {
/* 1462 */     int i = this.itsData.itsICode.length;
/* 1463 */     int j = this.iCodeTop;
/* 1464 */     if (j + paramInt <= i) throw Kit.codeBug();
/* 1465 */     i *= 2;
/* 1466 */     if (j + paramInt > i) {
/* 1467 */       i = j + paramInt;
/*      */     }
/* 1469 */     byte[] arrayOfByte = new byte[i];
/* 1470 */     System.arraycopy(this.itsData.itsICode, 0, arrayOfByte, 0, j);
/* 1471 */     this.itsData.itsICode = arrayOfByte;
/* 1472 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private void stackChange(int paramInt)
/*      */   {
/* 1477 */     if (paramInt <= 0) {
/* 1478 */       this.stackDepth += paramInt;
/*      */     } else {
/* 1480 */       int i = this.stackDepth + paramInt;
/* 1481 */       if (i > this.itsData.itsMaxStack) {
/* 1482 */         this.itsData.itsMaxStack = i;
/*      */       }
/* 1484 */       this.stackDepth = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private int allocLocal()
/*      */   {
/* 1490 */     int i = this.localTop;
/* 1491 */     this.localTop += 1;
/* 1492 */     if (this.localTop > this.itsData.itsMaxLocals) {
/* 1493 */       this.itsData.itsMaxLocals = this.localTop;
/*      */     }
/* 1495 */     return i;
/*      */   }
/*      */ 
/*      */   private void releaseLocal(int paramInt)
/*      */   {
/* 1500 */     this.localTop -= 1;
/* 1501 */     if (paramInt != this.localTop) Kit.codeBug();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.CodeGenerator
 * JD-Core Version:    0.6.2
 */