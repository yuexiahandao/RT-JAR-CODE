/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayComprehension;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.Assignment;
/*      */ import sun.org.mozilla.javascript.internal.ast.AstNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.AstRoot;
/*      */ import sun.org.mozilla.javascript.internal.ast.Block;
/*      */ import sun.org.mozilla.javascript.internal.ast.BreakStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.CatchClause;
/*      */ import sun.org.mozilla.javascript.internal.ast.ConditionalExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.ContinueStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.DestructuringForm;
/*      */ import sun.org.mozilla.javascript.internal.ast.DoLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ElementGet;
/*      */ import sun.org.mozilla.javascript.internal.ast.EmptyExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.ExpressionStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.ForInLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ForLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.FunctionCall;
/*      */ import sun.org.mozilla.javascript.internal.ast.FunctionNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.IfStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.InfixExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.Jump;
/*      */ import sun.org.mozilla.javascript.internal.ast.Label;
/*      */ import sun.org.mozilla.javascript.internal.ast.LabeledStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.LetNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.Loop;
/*      */ import sun.org.mozilla.javascript.internal.ast.Name;
/*      */ import sun.org.mozilla.javascript.internal.ast.NewExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.NumberLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.ObjectLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.ObjectProperty;
/*      */ import sun.org.mozilla.javascript.internal.ast.ParenthesizedExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.PropertyGet;
/*      */ import sun.org.mozilla.javascript.internal.ast.RegExpLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.ReturnStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.Scope;
/*      */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.StringLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.SwitchCase;
/*      */ import sun.org.mozilla.javascript.internal.ast.SwitchStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.Symbol;
/*      */ import sun.org.mozilla.javascript.internal.ast.ThrowStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.TryStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.UnaryExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.VariableDeclaration;
/*      */ import sun.org.mozilla.javascript.internal.ast.VariableInitializer;
/*      */ import sun.org.mozilla.javascript.internal.ast.WhileLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.WithStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlDotQuery;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlElemRef;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlFragment;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlMemberGet;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlPropRef;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlRef;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlString;
/*      */ import sun.org.mozilla.javascript.internal.ast.Yield;
/*      */ 
/*      */ public final class IRFactory extends Parser
/*      */ {
/*      */   private static final int LOOP_DO_WHILE = 0;
/*      */   private static final int LOOP_WHILE = 1;
/*      */   private static final int LOOP_FOR = 2;
/*      */   private static final int ALWAYS_TRUE_BOOLEAN = 1;
/*      */   private static final int ALWAYS_FALSE_BOOLEAN = -1;
/*   69 */   private Decompiler decompiler = new Decompiler();
/*      */ 
/*      */   public IRFactory()
/*      */   {
/*      */   }
/*      */ 
/*      */   public IRFactory(CompilerEnvirons paramCompilerEnvirons) {
/*   76 */     this(paramCompilerEnvirons, paramCompilerEnvirons.getErrorReporter());
/*      */   }
/*      */ 
/*      */   public IRFactory(CompilerEnvirons paramCompilerEnvirons, ErrorReporter paramErrorReporter) {
/*   80 */     super(paramCompilerEnvirons, paramErrorReporter);
/*      */   }
/*      */ 
/*      */   public ScriptNode transformTree(AstRoot paramAstRoot)
/*      */   {
/*   88 */     this.currentScriptOrFn = paramAstRoot;
/*   89 */     this.inUseStrictDirective = paramAstRoot.isInStrictMode();
/*   90 */     int i = this.decompiler.getCurrentOffset();
/*      */ 
/*   96 */     ScriptNode localScriptNode = (ScriptNode)transform(paramAstRoot);
/*      */ 
/*   98 */     int j = this.decompiler.getCurrentOffset();
/*   99 */     localScriptNode.setEncodedSourceBounds(i, j);
/*      */ 
/*  102 */     if (this.compilerEnv.isGeneratingSource()) {
/*  103 */       localScriptNode.setEncodedSource(this.decompiler.getEncodedSource());
/*      */     }
/*      */ 
/*  106 */     this.decompiler = null;
/*  107 */     return localScriptNode;
/*      */   }
/*      */ 
/*      */   public Node transform(AstNode paramAstNode)
/*      */   {
/*  115 */     switch (paramAstNode.getType()) {
/*      */     case 157:
/*  117 */       return transformArrayComp((ArrayComprehension)paramAstNode);
/*      */     case 65:
/*  119 */       return transformArrayLiteral((ArrayLiteral)paramAstNode);
/*      */     case 129:
/*  121 */       return transformBlock(paramAstNode);
/*      */     case 120:
/*  123 */       return transformBreak((BreakStatement)paramAstNode);
/*      */     case 38:
/*  125 */       return transformFunctionCall((FunctionCall)paramAstNode);
/*      */     case 121:
/*  127 */       return transformContinue((ContinueStatement)paramAstNode);
/*      */     case 118:
/*  129 */       return transformDoLoop((DoLoop)paramAstNode);
/*      */     case 128:
/*  131 */       return paramAstNode;
/*      */     case 119:
/*  133 */       if ((paramAstNode instanceof ForInLoop)) {
/*  134 */         return transformForInLoop((ForInLoop)paramAstNode);
/*      */       }
/*  136 */       return transformForLoop((ForLoop)paramAstNode);
/*      */     case 109:
/*  139 */       return transformFunction((FunctionNode)paramAstNode);
/*      */     case 36:
/*  141 */       return transformElementGet((ElementGet)paramAstNode);
/*      */     case 33:
/*  143 */       return transformPropertyGet((PropertyGet)paramAstNode);
/*      */     case 102:
/*  145 */       return transformCondExpr((ConditionalExpression)paramAstNode);
/*      */     case 112:
/*  147 */       return transformIf((IfStatement)paramAstNode);
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 160:
/*  154 */       return transformLiteral(paramAstNode);
/*      */     case 39:
/*  157 */       return transformName((Name)paramAstNode);
/*      */     case 40:
/*  159 */       return transformNumber((NumberLiteral)paramAstNode);
/*      */     case 30:
/*  161 */       return transformNewExpr((NewExpression)paramAstNode);
/*      */     case 66:
/*  163 */       return transformObjectLiteral((ObjectLiteral)paramAstNode);
/*      */     case 48:
/*  165 */       return transformRegExp((RegExpLiteral)paramAstNode);
/*      */     case 4:
/*  167 */       return transformReturn((ReturnStatement)paramAstNode);
/*      */     case 136:
/*  169 */       return transformScript((ScriptNode)paramAstNode);
/*      */     case 41:
/*  171 */       return transformString((StringLiteral)paramAstNode);
/*      */     case 114:
/*  173 */       return transformSwitch((SwitchStatement)paramAstNode);
/*      */     case 50:
/*  175 */       return transformThrow((ThrowStatement)paramAstNode);
/*      */     case 81:
/*  177 */       return transformTry((TryStatement)paramAstNode);
/*      */     case 117:
/*  179 */       return transformWhileLoop((WhileLoop)paramAstNode);
/*      */     case 123:
/*  181 */       return transformWith((WithStatement)paramAstNode);
/*      */     case 72:
/*  183 */       return transformYield((Yield)paramAstNode);
/*      */     }
/*  185 */     if ((paramAstNode instanceof ExpressionStatement)) {
/*  186 */       return transformExprStmt((ExpressionStatement)paramAstNode);
/*      */     }
/*  188 */     if ((paramAstNode instanceof Assignment)) {
/*  189 */       return transformAssignment((Assignment)paramAstNode);
/*      */     }
/*  191 */     if ((paramAstNode instanceof UnaryExpression)) {
/*  192 */       return transformUnary((UnaryExpression)paramAstNode);
/*      */     }
/*  194 */     if ((paramAstNode instanceof XmlMemberGet)) {
/*  195 */       return transformXmlMemberGet((XmlMemberGet)paramAstNode);
/*      */     }
/*  197 */     if ((paramAstNode instanceof InfixExpression)) {
/*  198 */       return transformInfix((InfixExpression)paramAstNode);
/*      */     }
/*  200 */     if ((paramAstNode instanceof VariableDeclaration)) {
/*  201 */       return transformVariables((VariableDeclaration)paramAstNode);
/*      */     }
/*  203 */     if ((paramAstNode instanceof ParenthesizedExpression)) {
/*  204 */       return transformParenExpr((ParenthesizedExpression)paramAstNode);
/*      */     }
/*  206 */     if ((paramAstNode instanceof LabeledStatement)) {
/*  207 */       return transformLabeledStatement((LabeledStatement)paramAstNode);
/*      */     }
/*  209 */     if ((paramAstNode instanceof LetNode)) {
/*  210 */       return transformLetNode((LetNode)paramAstNode);
/*      */     }
/*  212 */     if ((paramAstNode instanceof XmlRef)) {
/*  213 */       return transformXmlRef((XmlRef)paramAstNode);
/*      */     }
/*  215 */     if ((paramAstNode instanceof XmlLiteral)) {
/*  216 */       return transformXmlLiteral((XmlLiteral)paramAstNode);
/*      */     }
/*  218 */     throw new IllegalArgumentException("Can't transform: " + paramAstNode);
/*      */   }
/*      */ 
/*      */   private Node transformArrayComp(ArrayComprehension paramArrayComprehension)
/*      */   {
/*  243 */     int i = paramArrayComprehension.getLineno();
/*  244 */     Scope localScope1 = createScopeNode(157, i);
/*  245 */     String str = this.currentScriptOrFn.getNextTempName();
/*  246 */     pushScope(localScope1);
/*      */     try {
/*  248 */       defineSymbol(153, str, false);
/*  249 */       Node localNode1 = new Node(129, i);
/*  250 */       Node localNode2 = createCallOrNew(30, createName("Array"));
/*  251 */       Node localNode3 = new Node(133, createAssignment(90, createName(str), localNode2), i);
/*      */ 
/*  256 */       localNode1.addChildToBack(localNode3);
/*  257 */       localNode1.addChildToBack(arrayCompTransformHelper(paramArrayComprehension, str));
/*  258 */       localScope1.addChildToBack(localNode1);
/*  259 */       localScope1.addChildToBack(createName(str));
/*  260 */       return localScope1;
/*      */     } finally {
/*  262 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node arrayCompTransformHelper(ArrayComprehension paramArrayComprehension, String paramString)
/*      */   {
/*  268 */     this.decompiler.addToken(83);
/*  269 */     int i = paramArrayComprehension.getLineno();
/*  270 */     Node localNode1 = transform(paramArrayComprehension.getResult());
/*      */ 
/*  272 */     List localList = paramArrayComprehension.getLoops();
/*  273 */     int j = localList.size();
/*      */ 
/*  276 */     Node[] arrayOfNode1 = new Node[j];
/*  277 */     Node[] arrayOfNode2 = new Node[j];
/*      */     Object localObject2;
/*  279 */     for (int k = 0; k < j; k++) {
/*  280 */       localObject1 = (ArrayComprehensionLoop)localList.get(k);
/*  281 */       this.decompiler.addName(" ");
/*  282 */       this.decompiler.addToken(119);
/*  283 */       if (((ArrayComprehensionLoop)localObject1).isForEach()) {
/*  284 */         this.decompiler.addName("each ");
/*      */       }
/*  286 */       this.decompiler.addToken(87);
/*      */ 
/*  288 */       AstNode localAstNode = ((ArrayComprehensionLoop)localObject1).getIterator();
/*  289 */       String str = null;
/*  290 */       if (localAstNode.getType() == 39) {
/*  291 */         str = localAstNode.getString();
/*  292 */         this.decompiler.addName(str);
/*      */       }
/*      */       else {
/*  295 */         decompile(localAstNode);
/*  296 */         str = this.currentScriptOrFn.getNextTempName();
/*  297 */         defineSymbol(87, str, false);
/*  298 */         localNode1 = createBinary(89, createAssignment(90, localAstNode, createName(str)), localNode1);
/*      */       }
/*      */ 
/*  304 */       localObject2 = createName(str);
/*      */ 
/*  307 */       defineSymbol(153, str, false);
/*  308 */       arrayOfNode1[k] = localObject2;
/*      */ 
/*  310 */       this.decompiler.addToken(52);
/*  311 */       arrayOfNode2[k] = transform(((ArrayComprehensionLoop)localObject1).getIteratedObject());
/*  312 */       this.decompiler.addToken(88);
/*      */     }
/*      */ 
/*  316 */     Node localNode2 = createCallOrNew(38, createPropertyGet(createName(paramString), null, "push", 0));
/*      */ 
/*  321 */     Object localObject1 = new Node(133, localNode2, i);
/*      */ 
/*  323 */     if (paramArrayComprehension.getFilter() != null) {
/*  324 */       this.decompiler.addName(" ");
/*  325 */       this.decompiler.addToken(112);
/*  326 */       this.decompiler.addToken(87);
/*  327 */       localObject1 = createIf(transform(paramArrayComprehension.getFilter()), (Node)localObject1, null, i);
/*  328 */       this.decompiler.addToken(88);
/*      */     }
/*      */ 
/*  332 */     int m = 0;
/*      */     try {
/*  334 */       for (n = j - 1; n >= 0; n--) {
/*  335 */         localObject2 = (ArrayComprehensionLoop)localList.get(n);
/*  336 */         Scope localScope = createLoopNode(null, ((ArrayComprehensionLoop)localObject2).getLineno());
/*      */ 
/*  338 */         pushScope(localScope);
/*  339 */         m++;
/*  340 */         localObject1 = createForIn(153, localScope, arrayOfNode1[n], arrayOfNode2[n], (Node)localObject1, ((ArrayComprehensionLoop)localObject2).isForEach());
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*      */       int n;
/*  348 */       for (int i1 = 0; i1 < m; i1++) {
/*  349 */         popScope();
/*      */       }
/*      */     }
/*      */ 
/*  353 */     this.decompiler.addToken(84);
/*      */ 
/*  357 */     localNode2.addChildToBack(localNode1);
/*  358 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Node transformArrayLiteral(ArrayLiteral paramArrayLiteral) {
/*  362 */     if (paramArrayLiteral.isDestructuring()) {
/*  363 */       return paramArrayLiteral;
/*      */     }
/*  365 */     this.decompiler.addToken(83);
/*  366 */     List localList = paramArrayLiteral.getElements();
/*  367 */     Node localNode = new Node(65);
/*  368 */     ArrayList localArrayList = null;
/*  369 */     for (int i = 0; i < localList.size(); i++) {
/*  370 */       AstNode localAstNode = (AstNode)localList.get(i);
/*  371 */       if (localAstNode.getType() != 128) {
/*  372 */         localNode.addChildToBack(transform(localAstNode));
/*      */       } else {
/*  374 */         if (localArrayList == null) {
/*  375 */           localArrayList = new ArrayList();
/*      */         }
/*  377 */         localArrayList.add(Integer.valueOf(i));
/*      */       }
/*  379 */       if (i < localList.size() - 1)
/*  380 */         this.decompiler.addToken(89);
/*      */     }
/*  382 */     this.decompiler.addToken(84);
/*  383 */     localNode.putIntProp(21, paramArrayLiteral.getDestructuringLength());
/*      */ 
/*  385 */     if (localArrayList != null) {
/*  386 */       int[] arrayOfInt = new int[localArrayList.size()];
/*  387 */       for (int j = 0; j < localArrayList.size(); j++)
/*  388 */         arrayOfInt[j] = ((Integer)localArrayList.get(j)).intValue();
/*  389 */       localNode.putProp(11, arrayOfInt);
/*      */     }
/*  391 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Node transformAssignment(Assignment paramAssignment) {
/*  395 */     AstNode localAstNode = removeParens(paramAssignment.getLeft());
/*  396 */     Object localObject = null;
/*  397 */     if (isDestructuring(localAstNode)) {
/*  398 */       decompile(localAstNode);
/*  399 */       localObject = localAstNode;
/*      */     } else {
/*  401 */       localObject = transform(localAstNode);
/*      */     }
/*  403 */     this.decompiler.addToken(paramAssignment.getType());
/*  404 */     return createAssignment(paramAssignment.getType(), (Node)localObject, transform(paramAssignment.getRight()));
/*      */   }
/*      */ 
/*      */   private Node transformBlock(AstNode paramAstNode)
/*      */   {
/*  410 */     if ((paramAstNode instanceof Scope))
/*  411 */       pushScope((Scope)paramAstNode);
/*      */     try
/*      */     {
/*  414 */       ArrayList localArrayList = new ArrayList();
/*  415 */       for (Object localObject1 = paramAstNode.iterator(); ((Iterator)localObject1).hasNext(); ) { localNode = (Node)((Iterator)localObject1).next();
/*  416 */         localArrayList.add(transform((AstNode)localNode));
/*      */       }
/*  419 */       Node localNode;
/*  418 */       paramAstNode.removeChildren();
/*  419 */       for (localObject1 = localArrayList.iterator(); ((Iterator)localObject1).hasNext(); ) { localNode = (Node)((Iterator)localObject1).next();
/*  420 */         paramAstNode.addChildToBack(localNode);
/*      */       }
/*  422 */       return paramAstNode;
/*      */     } finally {
/*  424 */       if ((paramAstNode instanceof Scope))
/*  425 */         popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformBreak(BreakStatement paramBreakStatement)
/*      */   {
/*  431 */     this.decompiler.addToken(120);
/*  432 */     if (paramBreakStatement.getBreakLabel() != null) {
/*  433 */       this.decompiler.addName(paramBreakStatement.getBreakLabel().getIdentifier());
/*      */     }
/*  435 */     this.decompiler.addEOL(82);
/*  436 */     return paramBreakStatement;
/*      */   }
/*      */ 
/*      */   private Node transformCondExpr(ConditionalExpression paramConditionalExpression) {
/*  440 */     Node localNode1 = transform(paramConditionalExpression.getTestExpression());
/*  441 */     this.decompiler.addToken(102);
/*  442 */     Node localNode2 = transform(paramConditionalExpression.getTrueExpression());
/*  443 */     this.decompiler.addToken(103);
/*  444 */     Node localNode3 = transform(paramConditionalExpression.getFalseExpression());
/*  445 */     return createCondExpr(localNode1, localNode2, localNode3);
/*      */   }
/*      */ 
/*      */   private Node transformContinue(ContinueStatement paramContinueStatement) {
/*  449 */     this.decompiler.addToken(121);
/*  450 */     if (paramContinueStatement.getLabel() != null) {
/*  451 */       this.decompiler.addName(paramContinueStatement.getLabel().getIdentifier());
/*      */     }
/*  453 */     this.decompiler.addEOL(82);
/*  454 */     return paramContinueStatement;
/*      */   }
/*      */ 
/*      */   private Node transformDoLoop(DoLoop paramDoLoop) {
/*  458 */     paramDoLoop.setType(132);
/*  459 */     pushScope(paramDoLoop);
/*      */     try {
/*  461 */       this.decompiler.addToken(118);
/*  462 */       this.decompiler.addEOL(85);
/*  463 */       Node localNode1 = transform(paramDoLoop.getBody());
/*  464 */       this.decompiler.addToken(86);
/*  465 */       this.decompiler.addToken(117);
/*  466 */       this.decompiler.addToken(87);
/*  467 */       Node localNode2 = transform(paramDoLoop.getCondition());
/*  468 */       this.decompiler.addToken(88);
/*  469 */       this.decompiler.addEOL(82);
/*  470 */       return createLoop(paramDoLoop, 0, localNode1, localNode2, null, null);
/*      */     }
/*      */     finally {
/*  473 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformElementGet(ElementGet paramElementGet)
/*      */   {
/*  480 */     Node localNode1 = transform(paramElementGet.getTarget());
/*  481 */     this.decompiler.addToken(83);
/*  482 */     Node localNode2 = transform(paramElementGet.getElement());
/*  483 */     this.decompiler.addToken(84);
/*  484 */     return new Node(36, localNode1, localNode2);
/*      */   }
/*      */ 
/*      */   private Node transformExprStmt(ExpressionStatement paramExpressionStatement) {
/*  488 */     Node localNode = transform(paramExpressionStatement.getExpression());
/*  489 */     this.decompiler.addEOL(82);
/*  490 */     return new Node(paramExpressionStatement.getType(), localNode, paramExpressionStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformForInLoop(ForInLoop paramForInLoop) {
/*  494 */     this.decompiler.addToken(119);
/*  495 */     if (paramForInLoop.isForEach())
/*  496 */       this.decompiler.addName("each ");
/*  497 */     this.decompiler.addToken(87);
/*      */ 
/*  499 */     paramForInLoop.setType(132);
/*  500 */     pushScope(paramForInLoop);
/*      */     try {
/*  502 */       int i = -1;
/*  503 */       AstNode localAstNode = paramForInLoop.getIterator();
/*  504 */       if ((localAstNode instanceof VariableDeclaration)) {
/*  505 */         i = ((VariableDeclaration)localAstNode).getType();
/*      */       }
/*  507 */       Node localNode1 = transform(localAstNode);
/*  508 */       this.decompiler.addToken(52);
/*  509 */       Node localNode2 = transform(paramForInLoop.getIteratedObject());
/*  510 */       this.decompiler.addToken(88);
/*  511 */       this.decompiler.addEOL(85);
/*  512 */       Node localNode3 = transform(paramForInLoop.getBody());
/*  513 */       this.decompiler.addEOL(86);
/*  514 */       return createForIn(i, paramForInLoop, localNode1, localNode2, localNode3, paramForInLoop.isForEach());
/*      */     }
/*      */     finally {
/*  517 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformForLoop(ForLoop paramForLoop) {
/*  522 */     this.decompiler.addToken(119);
/*  523 */     this.decompiler.addToken(87);
/*  524 */     paramForLoop.setType(132);
/*      */ 
/*  527 */     Scope localScope = this.currentScope;
/*  528 */     this.currentScope = paramForLoop;
/*      */     try {
/*  530 */       Node localNode1 = transform(paramForLoop.getInitializer());
/*  531 */       this.decompiler.addToken(82);
/*  532 */       Node localNode2 = transform(paramForLoop.getCondition());
/*  533 */       this.decompiler.addToken(82);
/*  534 */       Node localNode3 = transform(paramForLoop.getIncrement());
/*  535 */       this.decompiler.addToken(88);
/*  536 */       this.decompiler.addEOL(85);
/*  537 */       Node localNode4 = transform(paramForLoop.getBody());
/*  538 */       this.decompiler.addEOL(86);
/*  539 */       return createFor(paramForLoop, localNode1, localNode2, localNode3, localNode4);
/*      */     } finally {
/*  541 */       this.currentScope = localScope;
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformFunction(FunctionNode paramFunctionNode) {
/*  546 */     int i = paramFunctionNode.getFunctionType();
/*  547 */     int j = this.decompiler.markFunctionStart(i);
/*  548 */     Node localNode1 = decompileFunctionHeader(paramFunctionNode);
/*  549 */     int k = this.currentScriptOrFn.addFunction(paramFunctionNode);
/*      */ 
/*  551 */     Parser.PerFunctionVariables localPerFunctionVariables = new Parser.PerFunctionVariables(this, paramFunctionNode);
/*      */     try
/*      */     {
/*  555 */       Node localNode2 = (Node)paramFunctionNode.getProp(23);
/*  556 */       paramFunctionNode.removeProp(23);
/*      */ 
/*  558 */       int m = paramFunctionNode.getBody().getLineno();
/*  559 */       this.nestingOfFunction += 1;
/*  560 */       Node localNode3 = transform(paramFunctionNode.getBody());
/*      */ 
/*  562 */       if (!paramFunctionNode.isExpressionClosure()) {
/*  563 */         this.decompiler.addToken(86);
/*      */       }
/*  565 */       paramFunctionNode.setEncodedSourceBounds(j, this.decompiler.markFunctionEnd(j));
/*      */ 
/*  567 */       if ((i != 2) && (!paramFunctionNode.isExpressionClosure()))
/*      */       {
/*  570 */         this.decompiler.addToken(1);
/*      */       }
/*      */ 
/*  573 */       if (localNode2 != null) {
/*  574 */         localNode3.addChildToFront(new Node(133, localNode2, m));
/*      */       }
/*      */ 
/*  578 */       int n = paramFunctionNode.getFunctionType();
/*  579 */       Node localNode4 = initFunction(paramFunctionNode, k, localNode3, n);
/*  580 */       if (localNode1 != null) {
/*  581 */         localNode4 = createAssignment(90, localNode1, localNode4);
/*  582 */         if (n != 2) {
/*  583 */           localNode4 = createExprStatementNoReturn(localNode4, paramFunctionNode.getLineno());
/*      */         }
/*      */       }
/*  586 */       return localNode4;
/*      */     }
/*      */     finally {
/*  589 */       this.nestingOfFunction -= 1;
/*  590 */       localPerFunctionVariables.restore();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformFunctionCall(FunctionCall paramFunctionCall) {
/*  595 */     Node localNode = createCallOrNew(38, transform(paramFunctionCall.getTarget()));
/*  596 */     localNode.setLineno(paramFunctionCall.getLineno());
/*  597 */     this.decompiler.addToken(87);
/*  598 */     List localList = paramFunctionCall.getArguments();
/*  599 */     for (int i = 0; i < localList.size(); i++) {
/*  600 */       AstNode localAstNode = (AstNode)localList.get(i);
/*  601 */       localNode.addChildToBack(transform(localAstNode));
/*  602 */       if (i < localList.size() - 1) {
/*  603 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/*  606 */     this.decompiler.addToken(88);
/*  607 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Node transformIf(IfStatement paramIfStatement) {
/*  611 */     this.decompiler.addToken(112);
/*  612 */     this.decompiler.addToken(87);
/*  613 */     Node localNode1 = transform(paramIfStatement.getCondition());
/*  614 */     this.decompiler.addToken(88);
/*  615 */     this.decompiler.addEOL(85);
/*  616 */     Node localNode2 = transform(paramIfStatement.getThenPart());
/*  617 */     Node localNode3 = null;
/*  618 */     if (paramIfStatement.getElsePart() != null) {
/*  619 */       this.decompiler.addToken(86);
/*  620 */       this.decompiler.addToken(113);
/*  621 */       this.decompiler.addEOL(85);
/*  622 */       localNode3 = transform(paramIfStatement.getElsePart());
/*      */     }
/*  624 */     this.decompiler.addEOL(86);
/*  625 */     return createIf(localNode1, localNode2, localNode3, paramIfStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformInfix(InfixExpression paramInfixExpression) {
/*  629 */     Node localNode1 = transform(paramInfixExpression.getLeft());
/*  630 */     this.decompiler.addToken(paramInfixExpression.getType());
/*  631 */     Node localNode2 = transform(paramInfixExpression.getRight());
/*  632 */     if ((paramInfixExpression instanceof XmlDotQuery)) {
/*  633 */       this.decompiler.addToken(88);
/*      */     }
/*  635 */     return createBinary(paramInfixExpression.getType(), localNode1, localNode2);
/*      */   }
/*      */ 
/*      */   private Node transformLabeledStatement(LabeledStatement paramLabeledStatement) {
/*  639 */     for (Object localObject1 = paramLabeledStatement.getLabels().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Label)((Iterator)localObject1).next();
/*  640 */       this.decompiler.addName(((Label)localObject2).getName());
/*  641 */       this.decompiler.addEOL(103);
/*      */     }
/*  643 */     localObject1 = paramLabeledStatement.getFirstLabel();
/*  644 */     Object localObject2 = transform(paramLabeledStatement.getStatement());
/*      */ 
/*  648 */     Node localNode1 = Node.newTarget();
/*  649 */     Node localNode2 = new Node(129, (Node)localObject1, (Node)localObject2, localNode1);
/*  650 */     ((Label)localObject1).target = localNode1;
/*      */ 
/*  652 */     return localNode2;
/*      */   }
/*      */ 
/*      */   private Node transformLetNode(LetNode paramLetNode) {
/*  656 */     pushScope(paramLetNode);
/*      */     try {
/*  658 */       this.decompiler.addToken(153);
/*  659 */       this.decompiler.addToken(87);
/*  660 */       Node localNode = transformVariableInitializers(paramLetNode.getVariables());
/*  661 */       this.decompiler.addToken(88);
/*  662 */       paramLetNode.addChildToBack(localNode);
/*  663 */       int i = paramLetNode.getType() == 158 ? 1 : 0;
/*  664 */       if (paramLetNode.getBody() != null) {
/*  665 */         if (i != 0)
/*  666 */           this.decompiler.addName(" ");
/*      */         else {
/*  668 */           this.decompiler.addEOL(85);
/*      */         }
/*  670 */         paramLetNode.addChildToBack(transform(paramLetNode.getBody()));
/*  671 */         if (i == 0) {
/*  672 */           this.decompiler.addEOL(86);
/*      */         }
/*      */       }
/*  675 */       return paramLetNode;
/*      */     } finally {
/*  677 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformLiteral(AstNode paramAstNode) {
/*  682 */     this.decompiler.addToken(paramAstNode.getType());
/*  683 */     return paramAstNode;
/*      */   }
/*      */ 
/*      */   private Node transformName(Name paramName) {
/*  687 */     this.decompiler.addName(paramName.getIdentifier());
/*  688 */     return paramName;
/*      */   }
/*      */ 
/*      */   private Node transformNewExpr(NewExpression paramNewExpression) {
/*  692 */     this.decompiler.addToken(30);
/*  693 */     Node localNode = createCallOrNew(30, transform(paramNewExpression.getTarget()));
/*  694 */     localNode.setLineno(paramNewExpression.getLineno());
/*  695 */     List localList = paramNewExpression.getArguments();
/*  696 */     this.decompiler.addToken(87);
/*  697 */     for (int i = 0; i < localList.size(); i++) {
/*  698 */       AstNode localAstNode = (AstNode)localList.get(i);
/*  699 */       localNode.addChildToBack(transform(localAstNode));
/*  700 */       if (i < localList.size() - 1) {
/*  701 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/*  704 */     this.decompiler.addToken(88);
/*  705 */     if (paramNewExpression.getInitializer() != null) {
/*  706 */       localNode.addChildToBack(transformObjectLiteral(paramNewExpression.getInitializer()));
/*      */     }
/*  708 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Node transformNumber(NumberLiteral paramNumberLiteral) {
/*  712 */     this.decompiler.addNumber(paramNumberLiteral.getNumber());
/*  713 */     return paramNumberLiteral;
/*      */   }
/*      */ 
/*      */   private Node transformObjectLiteral(ObjectLiteral paramObjectLiteral) {
/*  717 */     if (paramObjectLiteral.isDestructuring()) {
/*  718 */       return paramObjectLiteral;
/*      */     }
/*      */ 
/*  723 */     this.decompiler.addToken(85);
/*  724 */     List localList = paramObjectLiteral.getElements();
/*  725 */     Node localNode1 = new Node(66);
/*      */     Object[] arrayOfObject;
/*      */     int i;
/*      */     int j;
/*  727 */     if (localList.isEmpty()) {
/*  728 */       arrayOfObject = ScriptRuntime.emptyArgs;
/*      */     } else {
/*  730 */       i = localList.size(); j = 0;
/*  731 */       arrayOfObject = new Object[i];
/*  732 */       for (ObjectProperty localObjectProperty : localList) {
/*  733 */         if (localObjectProperty.isGetter())
/*  734 */           this.decompiler.addToken(151);
/*  735 */         else if (localObjectProperty.isSetter()) {
/*  736 */           this.decompiler.addToken(152);
/*      */         }
/*      */ 
/*  739 */         arrayOfObject[(j++)] = getPropKey(localObjectProperty.getLeft());
/*      */ 
/*  743 */         if ((!localObjectProperty.isGetter()) && (!localObjectProperty.isSetter())) {
/*  744 */           this.decompiler.addToken(66);
/*      */         }
/*      */ 
/*  747 */         Node localNode2 = transform(localObjectProperty.getRight());
/*  748 */         if (localObjectProperty.isGetter())
/*  749 */           localNode2 = createUnary(151, localNode2);
/*  750 */         else if (localObjectProperty.isSetter()) {
/*  751 */           localNode2 = createUnary(152, localNode2);
/*      */         }
/*  753 */         localNode1.addChildToBack(localNode2);
/*      */ 
/*  755 */         if (j < i) {
/*  756 */           this.decompiler.addToken(89);
/*      */         }
/*      */       }
/*      */     }
/*  760 */     this.decompiler.addToken(86);
/*  761 */     localNode1.putProp(12, arrayOfObject);
/*  762 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private Object getPropKey(Node paramNode)
/*      */   {
/*      */     String str;
/*      */     Object localObject;
/*  767 */     if ((paramNode instanceof Name)) {
/*  768 */       str = ((Name)paramNode).getIdentifier();
/*  769 */       this.decompiler.addName(str);
/*  770 */       localObject = ScriptRuntime.getIndexObject(str);
/*  771 */     } else if ((paramNode instanceof StringLiteral)) {
/*  772 */       str = ((StringLiteral)paramNode).getValue();
/*  773 */       this.decompiler.addString(str);
/*  774 */       localObject = ScriptRuntime.getIndexObject(str);
/*  775 */     } else if ((paramNode instanceof NumberLiteral)) {
/*  776 */       double d = ((NumberLiteral)paramNode).getNumber();
/*  777 */       this.decompiler.addNumber(d);
/*  778 */       localObject = ScriptRuntime.getIndexObject(d);
/*      */     } else {
/*  780 */       throw Kit.codeBug();
/*      */     }
/*  782 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Node transformParenExpr(ParenthesizedExpression paramParenthesizedExpression) {
/*  786 */     AstNode localAstNode = paramParenthesizedExpression.getExpression();
/*  787 */     this.decompiler.addToken(87);
/*  788 */     int i = 1;
/*  789 */     while ((localAstNode instanceof ParenthesizedExpression)) {
/*  790 */       this.decompiler.addToken(87);
/*  791 */       i++;
/*  792 */       localAstNode = ((ParenthesizedExpression)localAstNode).getExpression();
/*      */     }
/*  794 */     Node localNode = transform(localAstNode);
/*  795 */     for (int j = 0; j < i; j++) {
/*  796 */       this.decompiler.addToken(88);
/*      */     }
/*  798 */     localNode.putProp(19, Boolean.TRUE);
/*  799 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Node transformPropertyGet(PropertyGet paramPropertyGet) {
/*  803 */     Node localNode = transform(paramPropertyGet.getTarget());
/*  804 */     String str = paramPropertyGet.getProperty().getIdentifier();
/*  805 */     this.decompiler.addToken(108);
/*  806 */     this.decompiler.addName(str);
/*  807 */     return createPropertyGet(localNode, null, str, 0);
/*      */   }
/*      */ 
/*      */   private Node transformRegExp(RegExpLiteral paramRegExpLiteral) {
/*  811 */     this.decompiler.addRegexp(paramRegExpLiteral.getValue(), paramRegExpLiteral.getFlags());
/*  812 */     this.currentScriptOrFn.addRegExp(paramRegExpLiteral);
/*  813 */     return paramRegExpLiteral;
/*      */   }
/*      */ 
/*      */   private Node transformReturn(ReturnStatement paramReturnStatement) {
/*  817 */     if (Boolean.TRUE.equals(paramReturnStatement.getProp(25)))
/*  818 */       this.decompiler.addName(" ");
/*      */     else {
/*  820 */       this.decompiler.addToken(4);
/*      */     }
/*  822 */     AstNode localAstNode = paramReturnStatement.getReturnValue();
/*  823 */     Node localNode = localAstNode == null ? null : transform(localAstNode);
/*  824 */     this.decompiler.addEOL(82);
/*  825 */     return localAstNode == null ? new Node(4, paramReturnStatement.getLineno()) : new Node(4, localNode, paramReturnStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformScript(ScriptNode paramScriptNode)
/*      */   {
/*  831 */     this.decompiler.addToken(136);
/*  832 */     if (this.currentScope != null) Kit.codeBug();
/*  833 */     this.currentScope = paramScriptNode;
/*  834 */     Node localNode1 = new Node(129);
/*  835 */     for (Object localObject = paramScriptNode.iterator(); ((Iterator)localObject).hasNext(); ) { Node localNode2 = (Node)((Iterator)localObject).next();
/*  836 */       localNode1.addChildToBack(transform((AstNode)localNode2));
/*      */     }
/*  838 */     paramScriptNode.removeChildren();
/*  839 */     localObject = localNode1.getFirstChild();
/*  840 */     if (localObject != null) {
/*  841 */       paramScriptNode.addChildrenToBack((Node)localObject);
/*      */     }
/*  843 */     return paramScriptNode;
/*      */   }
/*      */ 
/*      */   private Node transformString(StringLiteral paramStringLiteral) {
/*  847 */     this.decompiler.addString(paramStringLiteral.getValue());
/*  848 */     return Node.newString(paramStringLiteral.getValue());
/*      */   }
/*      */ 
/*      */   private Node transformSwitch(SwitchStatement paramSwitchStatement)
/*      */   {
/*  891 */     this.decompiler.addToken(114);
/*  892 */     this.decompiler.addToken(87);
/*  893 */     Node localNode1 = transform(paramSwitchStatement.getExpression());
/*  894 */     this.decompiler.addToken(88);
/*  895 */     paramSwitchStatement.addChildToBack(localNode1);
/*      */ 
/*  897 */     Node localNode2 = new Node(129, paramSwitchStatement, paramSwitchStatement.getLineno());
/*  898 */     this.decompiler.addEOL(85);
/*      */ 
/*  900 */     for (SwitchCase localSwitchCase : paramSwitchStatement.getCases()) {
/*  901 */       AstNode localAstNode1 = localSwitchCase.getExpression();
/*  902 */       Node localNode3 = null;
/*      */ 
/*  904 */       if (localAstNode1 != null) {
/*  905 */         this.decompiler.addToken(115);
/*  906 */         localNode3 = transform(localAstNode1);
/*      */       } else {
/*  908 */         this.decompiler.addToken(116);
/*      */       }
/*  910 */       this.decompiler.addEOL(103);
/*      */ 
/*  912 */       List localList = localSwitchCase.getStatements();
/*  913 */       Block localBlock = new Block();
/*  914 */       if (localList != null) {
/*  915 */         for (AstNode localAstNode2 : localList) {
/*  916 */           localBlock.addChildToBack(transform(localAstNode2));
/*      */         }
/*      */       }
/*  919 */       addSwitchCase(localNode2, localNode3, localBlock);
/*      */     }
/*  921 */     this.decompiler.addEOL(86);
/*  922 */     closeSwitch(localNode2);
/*  923 */     return localNode2;
/*      */   }
/*      */ 
/*      */   private Node transformThrow(ThrowStatement paramThrowStatement) {
/*  927 */     this.decompiler.addToken(50);
/*  928 */     Node localNode = transform(paramThrowStatement.getExpression());
/*  929 */     this.decompiler.addEOL(82);
/*  930 */     return new Node(50, localNode, paramThrowStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformTry(TryStatement paramTryStatement) {
/*  934 */     this.decompiler.addToken(81);
/*  935 */     this.decompiler.addEOL(85);
/*  936 */     Node localNode1 = transform(paramTryStatement.getTryBlock());
/*  937 */     this.decompiler.addEOL(86);
/*      */ 
/*  939 */     Block localBlock = new Block();
/*  940 */     for (Object localObject1 = paramTryStatement.getCatchClauses().iterator(); ((Iterator)localObject1).hasNext(); ) { CatchClause localCatchClause = (CatchClause)((Iterator)localObject1).next();
/*  941 */       this.decompiler.addToken(124);
/*  942 */       this.decompiler.addToken(87);
/*      */ 
/*  944 */       String str = localCatchClause.getVarName().getIdentifier();
/*  945 */       this.decompiler.addName(str);
/*      */ 
/*  947 */       Object localObject2 = null;
/*  948 */       AstNode localAstNode = localCatchClause.getCatchCondition();
/*  949 */       if (localAstNode != null) {
/*  950 */         this.decompiler.addName(" ");
/*  951 */         this.decompiler.addToken(112);
/*  952 */         localObject2 = transform(localAstNode);
/*      */       } else {
/*  954 */         localObject2 = new EmptyExpression();
/*      */       }
/*  956 */       this.decompiler.addToken(88);
/*  957 */       this.decompiler.addEOL(85);
/*      */ 
/*  959 */       Node localNode2 = transform(localCatchClause.getBody());
/*  960 */       this.decompiler.addEOL(86);
/*      */ 
/*  962 */       localBlock.addChildToBack(createCatch(str, (Node)localObject2, localNode2, localCatchClause.getLineno()));
/*      */     }
/*      */ 
/*  965 */     localObject1 = null;
/*  966 */     if (paramTryStatement.getFinallyBlock() != null) {
/*  967 */       this.decompiler.addToken(125);
/*  968 */       this.decompiler.addEOL(85);
/*  969 */       localObject1 = transform(paramTryStatement.getFinallyBlock());
/*  970 */       this.decompiler.addEOL(86);
/*      */     }
/*  972 */     return createTryCatchFinally(localNode1, localBlock, (Node)localObject1, paramTryStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformUnary(UnaryExpression paramUnaryExpression)
/*      */   {
/*  977 */     int i = paramUnaryExpression.getType();
/*  978 */     if (i == 74) {
/*  979 */       return transformDefaultXmlNamepace(paramUnaryExpression);
/*      */     }
/*  981 */     if (paramUnaryExpression.isPrefix()) {
/*  982 */       this.decompiler.addToken(i);
/*      */     }
/*  984 */     Node localNode = transform(paramUnaryExpression.getOperand());
/*  985 */     if (paramUnaryExpression.isPostfix()) {
/*  986 */       this.decompiler.addToken(i);
/*      */     }
/*  988 */     if ((i == 106) || (i == 107)) {
/*  989 */       return createIncDec(i, paramUnaryExpression.isPostfix(), localNode);
/*      */     }
/*  991 */     return createUnary(i, localNode);
/*      */   }
/*      */ 
/*      */   private Node transformVariables(VariableDeclaration paramVariableDeclaration) {
/*  995 */     this.decompiler.addToken(paramVariableDeclaration.getType());
/*  996 */     transformVariableInitializers(paramVariableDeclaration);
/*      */ 
/* 1000 */     AstNode localAstNode = paramVariableDeclaration.getParent();
/* 1001 */     if ((!(localAstNode instanceof Loop)) && (!(localAstNode instanceof LetNode)))
/*      */     {
/* 1003 */       this.decompiler.addEOL(82);
/*      */     }
/* 1005 */     return paramVariableDeclaration;
/*      */   }
/*      */ 
/*      */   private Node transformVariableInitializers(VariableDeclaration paramVariableDeclaration) {
/* 1009 */     List localList = paramVariableDeclaration.getVariables();
/* 1010 */     int i = localList.size(); int j = 0;
/* 1011 */     for (VariableInitializer localVariableInitializer : localList) {
/* 1012 */       AstNode localAstNode1 = localVariableInitializer.getTarget();
/* 1013 */       AstNode localAstNode2 = localVariableInitializer.getInitializer();
/*      */ 
/* 1015 */       Object localObject = null;
/* 1016 */       if (localVariableInitializer.isDestructuring()) {
/* 1017 */         decompile(localAstNode1);
/* 1018 */         localObject = localAstNode1;
/*      */       } else {
/* 1020 */         localObject = transform(localAstNode1);
/*      */       }
/*      */ 
/* 1023 */       Node localNode1 = null;
/* 1024 */       if (localAstNode2 != null) {
/* 1025 */         this.decompiler.addToken(90);
/* 1026 */         localNode1 = transform(localAstNode2);
/*      */       }
/*      */ 
/* 1029 */       if (localVariableInitializer.isDestructuring()) {
/* 1030 */         if (localNode1 == null) {
/* 1031 */           paramVariableDeclaration.addChildToBack((Node)localObject);
/*      */         } else {
/* 1033 */           Node localNode2 = createDestructuringAssignment(paramVariableDeclaration.getType(), (Node)localObject, localNode1);
/*      */ 
/* 1035 */           paramVariableDeclaration.addChildToBack(localNode2);
/*      */         }
/*      */       } else {
/* 1038 */         if (localNode1 != null) {
/* 1039 */           ((Node)localObject).addChildToBack(localNode1);
/*      */         }
/* 1041 */         paramVariableDeclaration.addChildToBack((Node)localObject);
/*      */       }
/* 1043 */       if (j++ < i - 1) {
/* 1044 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/* 1047 */     return paramVariableDeclaration;
/*      */   }
/*      */ 
/*      */   private Node transformWhileLoop(WhileLoop paramWhileLoop) {
/* 1051 */     this.decompiler.addToken(117);
/* 1052 */     paramWhileLoop.setType(132);
/* 1053 */     pushScope(paramWhileLoop);
/*      */     try {
/* 1055 */       this.decompiler.addToken(87);
/* 1056 */       Node localNode1 = transform(paramWhileLoop.getCondition());
/* 1057 */       this.decompiler.addToken(88);
/* 1058 */       this.decompiler.addEOL(85);
/* 1059 */       Node localNode2 = transform(paramWhileLoop.getBody());
/* 1060 */       this.decompiler.addEOL(86);
/* 1061 */       return createLoop(paramWhileLoop, 1, localNode2, localNode1, null, null);
/*      */     } finally {
/* 1063 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node transformWith(WithStatement paramWithStatement) {
/* 1068 */     this.decompiler.addToken(123);
/* 1069 */     this.decompiler.addToken(87);
/* 1070 */     Node localNode1 = transform(paramWithStatement.getExpression());
/* 1071 */     this.decompiler.addToken(88);
/* 1072 */     this.decompiler.addEOL(85);
/* 1073 */     Node localNode2 = transform(paramWithStatement.getStatement());
/* 1074 */     this.decompiler.addEOL(86);
/* 1075 */     return createWith(localNode1, localNode2, paramWithStatement.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformYield(Yield paramYield) {
/* 1079 */     this.decompiler.addToken(72);
/* 1080 */     Node localNode = paramYield.getValue() == null ? null : transform(paramYield.getValue());
/* 1081 */     if (localNode != null) {
/* 1082 */       return new Node(72, localNode, paramYield.getLineno());
/*      */     }
/* 1084 */     return new Node(72, paramYield.getLineno());
/*      */   }
/*      */ 
/*      */   private Node transformXmlLiteral(XmlLiteral paramXmlLiteral)
/*      */   {
/* 1091 */     Node localNode1 = new Node(30, paramXmlLiteral.getLineno());
/* 1092 */     List localList = paramXmlLiteral.getFragments();
/*      */ 
/* 1094 */     XmlString localXmlString = (XmlString)localList.get(0);
/* 1095 */     boolean bool1 = localXmlString.getXml().trim().startsWith("<>");
/* 1096 */     localNode1.addChildToBack(createName(bool1 ? "XMLList" : "XML"));
/*      */ 
/* 1098 */     Node localNode2 = null;
/* 1099 */     for (XmlFragment localXmlFragment : localList)
/*      */     {
/*      */       Object localObject;
/* 1100 */       if ((localXmlFragment instanceof XmlString)) {
/* 1101 */         localObject = ((XmlString)localXmlFragment).getXml();
/* 1102 */         this.decompiler.addName((String)localObject);
/* 1103 */         if (localNode2 == null)
/* 1104 */           localNode2 = createString((String)localObject);
/*      */         else
/* 1106 */           localNode2 = createBinary(21, localNode2, createString((String)localObject));
/*      */       }
/*      */       else {
/* 1109 */         localObject = (XmlExpression)localXmlFragment;
/* 1110 */         boolean bool2 = ((XmlExpression)localObject).isXmlAttribute();
/*      */ 
/* 1112 */         this.decompiler.addToken(85);
/*      */         Node localNode3;
/* 1113 */         if ((((XmlExpression)localObject).getExpression() instanceof EmptyExpression))
/* 1114 */           localNode3 = createString("");
/*      */         else {
/* 1116 */           localNode3 = transform(((XmlExpression)localObject).getExpression());
/*      */         }
/* 1118 */         this.decompiler.addToken(86);
/* 1119 */         if (bool2)
/*      */         {
/* 1121 */           localNode3 = createUnary(75, localNode3);
/* 1122 */           Node localNode4 = createBinary(21, createString("\""), localNode3);
/*      */ 
/* 1125 */           localNode3 = createBinary(21, localNode4, createString("\""));
/*      */         }
/*      */         else
/*      */         {
/* 1129 */           localNode3 = createUnary(76, localNode3);
/*      */         }
/* 1131 */         localNode2 = createBinary(21, localNode2, localNode3);
/*      */       }
/*      */     }
/*      */ 
/* 1135 */     localNode1.addChildToBack(localNode2);
/* 1136 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private Node transformXmlMemberGet(XmlMemberGet paramXmlMemberGet) {
/* 1140 */     XmlRef localXmlRef = paramXmlMemberGet.getMemberRef();
/* 1141 */     Node localNode = transform(paramXmlMemberGet.getLeft());
/* 1142 */     int i = localXmlRef.isAttributeAccess() ? 2 : 0;
/* 1143 */     if (paramXmlMemberGet.getType() == 143) {
/* 1144 */       i |= 4;
/* 1145 */       this.decompiler.addToken(143);
/*      */     } else {
/* 1147 */       this.decompiler.addToken(108);
/*      */     }
/* 1149 */     return transformXmlRef(localNode, localXmlRef, i);
/*      */   }
/*      */ 
/*      */   private Node transformXmlRef(XmlRef paramXmlRef)
/*      */   {
/* 1154 */     int i = paramXmlRef.isAttributeAccess() ? 2 : 0;
/*      */ 
/* 1156 */     return transformXmlRef(null, paramXmlRef, i);
/*      */   }
/*      */ 
/*      */   private Node transformXmlRef(Node paramNode, XmlRef paramXmlRef, int paramInt) {
/* 1160 */     if ((paramInt & 0x2) != 0)
/* 1161 */       this.decompiler.addToken(147);
/* 1162 */     Name localName = paramXmlRef.getNamespace();
/* 1163 */     String str = localName != null ? localName.getIdentifier() : null;
/* 1164 */     if (str != null) {
/* 1165 */       this.decompiler.addName(str);
/* 1166 */       this.decompiler.addToken(144);
/*      */     }
/* 1168 */     if ((paramXmlRef instanceof XmlPropRef)) {
/* 1169 */       localObject = ((XmlPropRef)paramXmlRef).getPropName().getIdentifier();
/* 1170 */       this.decompiler.addName((String)localObject);
/* 1171 */       return createPropertyGet(paramNode, str, (String)localObject, paramInt);
/*      */     }
/* 1173 */     this.decompiler.addToken(83);
/* 1174 */     Object localObject = transform(((XmlElemRef)paramXmlRef).getExpression());
/* 1175 */     this.decompiler.addToken(84);
/* 1176 */     return createElementGet(paramNode, str, (Node)localObject, paramInt);
/*      */   }
/*      */ 
/*      */   private Node transformDefaultXmlNamepace(UnaryExpression paramUnaryExpression)
/*      */   {
/* 1181 */     this.decompiler.addToken(116);
/* 1182 */     this.decompiler.addName(" xml");
/* 1183 */     this.decompiler.addName(" namespace");
/* 1184 */     this.decompiler.addToken(90);
/* 1185 */     Node localNode = transform(paramUnaryExpression.getOperand());
/* 1186 */     return createUnary(74, localNode);
/*      */   }
/*      */ 
/*      */   private void addSwitchCase(Node paramNode1, Node paramNode2, Node paramNode3)
/*      */   {
/* 1195 */     if (paramNode1.getType() != 129) throw Kit.codeBug();
/* 1196 */     Jump localJump1 = (Jump)paramNode1.getFirstChild();
/* 1197 */     if (localJump1.getType() != 114) throw Kit.codeBug();
/*      */ 
/* 1199 */     Node localNode = Node.newTarget();
/* 1200 */     if (paramNode2 != null) {
/* 1201 */       Jump localJump2 = new Jump(115, paramNode2);
/* 1202 */       localJump2.target = localNode;
/* 1203 */       localJump1.addChildToBack(localJump2);
/*      */     } else {
/* 1205 */       localJump1.setDefault(localNode);
/*      */     }
/* 1207 */     paramNode1.addChildToBack(localNode);
/* 1208 */     paramNode1.addChildToBack(paramNode3);
/*      */   }
/*      */ 
/*      */   private void closeSwitch(Node paramNode)
/*      */   {
/* 1213 */     if (paramNode.getType() != 129) throw Kit.codeBug();
/* 1214 */     Jump localJump = (Jump)paramNode.getFirstChild();
/* 1215 */     if (localJump.getType() != 114) throw Kit.codeBug();
/*      */ 
/* 1217 */     Node localNode1 = Node.newTarget();
/*      */ 
/* 1220 */     localJump.target = localNode1;
/*      */ 
/* 1222 */     Node localNode2 = localJump.getDefault();
/* 1223 */     if (localNode2 == null) {
/* 1224 */       localNode2 = localNode1;
/*      */     }
/*      */ 
/* 1227 */     paramNode.addChildAfter(makeJump(5, localNode2), localJump);
/*      */ 
/* 1229 */     paramNode.addChildToBack(localNode1);
/*      */   }
/*      */ 
/*      */   private Node createExprStatementNoReturn(Node paramNode, int paramInt) {
/* 1233 */     return new Node(133, paramNode, paramInt);
/*      */   }
/*      */ 
/*      */   private Node createString(String paramString) {
/* 1237 */     return Node.newString(paramString);
/*      */   }
/*      */ 
/*      */   private Node createCatch(String paramString, Node paramNode1, Node paramNode2, int paramInt)
/*      */   {
/* 1250 */     if (paramNode1 == null) {
/* 1251 */       paramNode1 = new Node(128);
/*      */     }
/* 1253 */     return new Node(124, createName(paramString), paramNode1, paramNode2, paramInt);
/*      */   }
/*      */ 
/*      */   private Node initFunction(FunctionNode paramFunctionNode, int paramInt1, Node paramNode, int paramInt2)
/*      */   {
/* 1259 */     paramFunctionNode.setFunctionType(paramInt2);
/* 1260 */     paramFunctionNode.addChildToBack(paramNode);
/*      */ 
/* 1262 */     int i = paramFunctionNode.getFunctionCount();
/* 1263 */     if (i != 0)
/*      */     {
/* 1265 */       paramFunctionNode.setRequiresActivation();
/*      */     }
/*      */ 
/* 1268 */     if (paramInt2 == 2) {
/* 1269 */       localObject = paramFunctionNode.getFunctionName();
/* 1270 */       if ((localObject != null) && (((Name)localObject).length() != 0) && (paramFunctionNode.getSymbol(((Name)localObject).getIdentifier()) == null))
/*      */       {
/* 1279 */         paramFunctionNode.putSymbol(new Symbol(109, ((Name)localObject).getIdentifier()));
/* 1280 */         localNode = new Node(133, new Node(8, Node.newString(49, ((Name)localObject).getIdentifier()), new Node(63)));
/*      */ 
/* 1285 */         paramNode.addChildrenToFront(localNode);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1290 */     Object localObject = paramNode.getLastChild();
/* 1291 */     if ((localObject == null) || (((Node)localObject).getType() != 4)) {
/* 1292 */       paramNode.addChildToBack(new Node(4));
/*      */     }
/*      */ 
/* 1295 */     Node localNode = Node.newString(109, paramFunctionNode.getName());
/* 1296 */     localNode.putIntProp(1, paramInt1);
/* 1297 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Scope createLoopNode(Node paramNode, int paramInt)
/*      */   {
/* 1306 */     Scope localScope = createScopeNode(132, paramInt);
/* 1307 */     if (paramNode != null) {
/* 1308 */       ((Jump)paramNode).setLoop(localScope);
/*      */     }
/* 1310 */     return localScope;
/*      */   }
/*      */ 
/*      */   private Node createFor(Scope paramScope, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4)
/*      */   {
/* 1315 */     if (paramNode1.getType() == 153)
/*      */     {
/* 1319 */       Scope localScope = Scope.splitScope(paramScope);
/* 1320 */       localScope.setType(153);
/* 1321 */       localScope.addChildrenToBack(paramNode1);
/* 1322 */       localScope.addChildToBack(createLoop(paramScope, 2, paramNode4, paramNode2, new Node(128), paramNode3));
/*      */ 
/* 1324 */       return localScope;
/*      */     }
/* 1326 */     return createLoop(paramScope, 2, paramNode4, paramNode2, paramNode1, paramNode3);
/*      */   }
/*      */ 
/*      */   private Node createLoop(Jump paramJump, int paramInt, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4)
/*      */   {
/* 1332 */     Node localNode1 = Node.newTarget();
/* 1333 */     Node localNode2 = Node.newTarget();
/* 1334 */     if ((paramInt == 2) && (paramNode2.getType() == 128)) {
/* 1335 */       paramNode2 = new Node(45);
/*      */     }
/* 1337 */     Jump localJump = new Jump(6, paramNode2);
/* 1338 */     localJump.target = localNode1;
/* 1339 */     Node localNode3 = Node.newTarget();
/*      */ 
/* 1341 */     paramJump.addChildToBack(localNode1);
/* 1342 */     paramJump.addChildrenToBack(paramNode1);
/* 1343 */     if ((paramInt == 1) || (paramInt == 2))
/*      */     {
/* 1345 */       paramJump.addChildrenToBack(new Node(128, paramJump.getLineno()));
/*      */     }
/* 1347 */     paramJump.addChildToBack(localNode2);
/* 1348 */     paramJump.addChildToBack(localJump);
/* 1349 */     paramJump.addChildToBack(localNode3);
/*      */ 
/* 1351 */     paramJump.target = localNode3;
/* 1352 */     Object localObject = localNode2;
/*      */ 
/* 1354 */     if ((paramInt == 1) || (paramInt == 2))
/*      */     {
/* 1356 */       paramJump.addChildToFront(makeJump(5, localNode2));
/*      */ 
/* 1358 */       if (paramInt == 2) {
/* 1359 */         int i = paramNode3.getType();
/* 1360 */         if (i != 128) {
/* 1361 */           if ((i != 122) && (i != 153)) {
/* 1362 */             paramNode3 = new Node(133, paramNode3);
/*      */           }
/* 1364 */           paramJump.addChildToFront(paramNode3);
/*      */         }
/* 1366 */         Node localNode4 = Node.newTarget();
/* 1367 */         paramJump.addChildAfter(localNode4, paramNode1);
/* 1368 */         if (paramNode4.getType() != 128) {
/* 1369 */           paramNode4 = new Node(133, paramNode4);
/* 1370 */           paramJump.addChildAfter(paramNode4, localNode4);
/*      */         }
/* 1372 */         localObject = localNode4;
/*      */       }
/*      */     }
/*      */ 
/* 1376 */     paramJump.setContinue((Node)localObject);
/* 1377 */     return paramJump;
/*      */   }
/*      */ 
/*      */   private Node createForIn(int paramInt, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4, boolean paramBoolean)
/*      */   {
/* 1386 */     int i = -1;
/* 1387 */     int j = 0;
/*      */ 
/* 1389 */     int k = paramNode2.getType();
/*      */     Node localNode1;
/* 1390 */     if ((k == 122) || (k == 153)) {
/* 1391 */       localNode2 = paramNode2.getLastChild();
/* 1392 */       m = localNode2.getType();
/* 1393 */       if ((m == 65) || (m == 66))
/*      */       {
/* 1395 */         k = i = m;
/* 1396 */         localNode1 = localNode2;
/* 1397 */         j = 0;
/* 1398 */         if ((localNode2 instanceof ArrayLiteral))
/* 1399 */           j = ((ArrayLiteral)localNode2).getDestructuringLength();
/* 1400 */       } else if (m == 39) {
/* 1401 */         localNode1 = Node.newString(39, localNode2.getString());
/*      */       } else {
/* 1403 */         reportError("msg.bad.for.in.lhs");
/* 1404 */         return null;
/*      */       }
/* 1406 */     } else if ((k == 65) || (k == 66)) {
/* 1407 */       i = k;
/* 1408 */       localNode1 = paramNode2;
/* 1409 */       j = 0;
/* 1410 */       if ((paramNode2 instanceof ArrayLiteral))
/* 1411 */         j = ((ArrayLiteral)paramNode2).getDestructuringLength();
/*      */     } else {
/* 1413 */       localNode1 = makeReference(paramNode2);
/* 1414 */       if (localNode1 == null) {
/* 1415 */         reportError("msg.bad.for.in.lhs");
/* 1416 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1420 */     Node localNode2 = new Node(141);
/* 1421 */     int m = i != -1 ? 60 : paramBoolean ? 59 : 58;
/*      */ 
/* 1425 */     Node localNode3 = new Node(m, paramNode3);
/* 1426 */     localNode3.putProp(3, localNode2);
/* 1427 */     Node localNode4 = new Node(61);
/* 1428 */     localNode4.putProp(3, localNode2);
/* 1429 */     Node localNode5 = new Node(62);
/* 1430 */     localNode5.putProp(3, localNode2);
/*      */ 
/* 1432 */     Node localNode6 = new Node(129);
/*      */     Node localNode7;
/* 1434 */     if (i != -1) {
/* 1435 */       localNode7 = createDestructuringAssignment(paramInt, localNode1, localNode5);
/* 1436 */       if ((!paramBoolean) && ((i == 66) || (j != 2)))
/*      */       {
/* 1441 */         reportError("msg.bad.for.in.destruct");
/*      */       }
/*      */     } else {
/* 1444 */       localNode7 = simpleAssignment(localNode1, localNode5);
/*      */     }
/* 1446 */     localNode6.addChildToBack(new Node(133, localNode7));
/* 1447 */     localNode6.addChildToBack(paramNode4);
/*      */ 
/* 1449 */     paramNode1 = createLoop((Jump)paramNode1, 1, localNode6, localNode4, null, null);
/* 1450 */     paramNode1.addChildToFront(localNode3);
/* 1451 */     if ((k == 122) || (k == 153))
/* 1452 */       paramNode1.addChildToFront(paramNode2);
/* 1453 */     localNode2.addChildToBack(paramNode1);
/*      */ 
/* 1455 */     return localNode2;
/*      */   }
/*      */ 
/*      */   private Node createTryCatchFinally(Node paramNode1, Node paramNode2, Node paramNode3, int paramInt)
/*      */   {
/* 1476 */     int i = (paramNode3 != null) && ((paramNode3.getType() != 129) || (paramNode3.hasChildren())) ? 1 : 0;
/*      */ 
/* 1481 */     if ((paramNode1.getType() == 129) && (!paramNode1.hasChildren()) && (i == 0))
/*      */     {
/* 1484 */       return paramNode1;
/*      */     }
/*      */ 
/* 1487 */     boolean bool = paramNode2.hasChildren();
/*      */ 
/* 1490 */     if ((i == 0) && (!bool))
/*      */     {
/* 1492 */       return paramNode1;
/*      */     }
/*      */ 
/* 1495 */     Node localNode1 = new Node(141);
/* 1496 */     Jump localJump = new Jump(81, paramNode1, paramInt);
/* 1497 */     localJump.putProp(3, localNode1);
/*      */     Node localNode2;
/*      */     Node localNode3;
/*      */     Node localNode4;
/* 1499 */     if (bool)
/*      */     {
/* 1501 */       localNode2 = Node.newTarget();
/* 1502 */       localJump.addChildToBack(makeJump(5, localNode2));
/*      */ 
/* 1505 */       localNode3 = Node.newTarget();
/* 1506 */       localJump.target = localNode3;
/*      */ 
/* 1508 */       localJump.addChildToBack(localNode3);
/*      */ 
/* 1558 */       localNode4 = new Node(141);
/*      */ 
/* 1561 */       Node localNode5 = paramNode2.getFirstChild();
/* 1562 */       int j = 0;
/* 1563 */       int k = 0;
/* 1564 */       while (localNode5 != null) {
/* 1565 */         int m = localNode5.getLineno();
/*      */ 
/* 1567 */         Node localNode7 = localNode5.getFirstChild();
/* 1568 */         Node localNode8 = localNode7.getNext();
/* 1569 */         Node localNode9 = localNode8.getNext();
/* 1570 */         localNode5.removeChild(localNode7);
/* 1571 */         localNode5.removeChild(localNode8);
/* 1572 */         localNode5.removeChild(localNode9);
/*      */ 
/* 1578 */         localNode9.addChildToBack(new Node(3));
/* 1579 */         localNode9.addChildToBack(makeJump(5, localNode2));
/*      */         Node localNode10;
/* 1583 */         if (localNode8.getType() == 128) {
/* 1584 */           localNode10 = localNode9;
/* 1585 */           j = 1;
/*      */         } else {
/* 1587 */           localNode10 = createIf(localNode8, localNode9, null, m);
/*      */         }
/*      */ 
/* 1593 */         Node localNode11 = new Node(57, localNode7, createUseLocal(localNode1));
/*      */ 
/* 1595 */         localNode11.putProp(3, localNode4);
/* 1596 */         localNode11.putIntProp(14, k);
/* 1597 */         localNode4.addChildToBack(localNode11);
/*      */ 
/* 1600 */         localNode4.addChildToBack(createWith(createUseLocal(localNode4), localNode10, m));
/*      */ 
/* 1605 */         localNode5 = localNode5.getNext();
/* 1606 */         k++;
/*      */       }
/* 1608 */       localJump.addChildToBack(localNode4);
/* 1609 */       if (j == 0)
/*      */       {
/* 1611 */         Node localNode6 = new Node(51);
/* 1612 */         localNode6.putProp(3, localNode1);
/* 1613 */         localJump.addChildToBack(localNode6);
/*      */       }
/*      */ 
/* 1616 */       localJump.addChildToBack(localNode2);
/*      */     }
/*      */ 
/* 1619 */     if (i != 0) {
/* 1620 */       localNode2 = Node.newTarget();
/* 1621 */       localJump.setFinally(localNode2);
/*      */ 
/* 1624 */       localJump.addChildToBack(makeJump(135, localNode2));
/*      */ 
/* 1627 */       localNode3 = Node.newTarget();
/* 1628 */       localJump.addChildToBack(makeJump(5, localNode3));
/*      */ 
/* 1630 */       localJump.addChildToBack(localNode2);
/* 1631 */       localNode4 = new Node(125, paramNode3);
/* 1632 */       localNode4.putProp(3, localNode1);
/* 1633 */       localJump.addChildToBack(localNode4);
/*      */ 
/* 1635 */       localJump.addChildToBack(localNode3);
/*      */     }
/* 1637 */     localNode1.addChildToBack(localJump);
/* 1638 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private Node createWith(Node paramNode1, Node paramNode2, int paramInt) {
/* 1642 */     setRequiresActivation();
/* 1643 */     Node localNode1 = new Node(129, paramInt);
/* 1644 */     localNode1.addChildToBack(new Node(2, paramNode1));
/* 1645 */     Node localNode2 = new Node(123, paramNode2, paramInt);
/* 1646 */     localNode1.addChildrenToBack(localNode2);
/* 1647 */     localNode1.addChildToBack(new Node(3));
/* 1648 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private Node createIf(Node paramNode1, Node paramNode2, Node paramNode3, int paramInt)
/*      */   {
/* 1653 */     int i = isAlwaysDefinedBoolean(paramNode1);
/* 1654 */     if (i == 1)
/* 1655 */       return paramNode2;
/* 1656 */     if (i == -1) {
/* 1657 */       if (paramNode3 != null) {
/* 1658 */         return paramNode3;
/*      */       }
/*      */ 
/* 1661 */       return new Node(129, paramInt);
/*      */     }
/*      */ 
/* 1664 */     Node localNode1 = new Node(129, paramInt);
/* 1665 */     Node localNode2 = Node.newTarget();
/* 1666 */     Jump localJump = new Jump(7, paramNode1);
/* 1667 */     localJump.target = localNode2;
/*      */ 
/* 1669 */     localNode1.addChildToBack(localJump);
/* 1670 */     localNode1.addChildrenToBack(paramNode2);
/*      */ 
/* 1672 */     if (paramNode3 != null) {
/* 1673 */       Node localNode3 = Node.newTarget();
/* 1674 */       localNode1.addChildToBack(makeJump(5, localNode3));
/* 1675 */       localNode1.addChildToBack(localNode2);
/* 1676 */       localNode1.addChildrenToBack(paramNode3);
/* 1677 */       localNode1.addChildToBack(localNode3);
/*      */     } else {
/* 1679 */       localNode1.addChildToBack(localNode2);
/*      */     }
/*      */ 
/* 1682 */     return localNode1;
/*      */   }
/*      */ 
/*      */   private Node createCondExpr(Node paramNode1, Node paramNode2, Node paramNode3) {
/* 1686 */     int i = isAlwaysDefinedBoolean(paramNode1);
/* 1687 */     if (i == 1)
/* 1688 */       return paramNode2;
/* 1689 */     if (i == -1) {
/* 1690 */       return paramNode3;
/*      */     }
/* 1692 */     return new Node(102, paramNode1, paramNode2, paramNode3);
/*      */   }
/*      */ 
/*      */   private Node createUnary(int paramInt, Node paramNode)
/*      */   {
/* 1697 */     int i = paramNode.getType();
/*      */     int j;
/* 1698 */     switch (paramInt)
/*      */     {
/*      */     case 31:
/*      */       Node localNode2;
/*      */       Node localNode3;
/*      */       Node localNode1;
/* 1701 */       if (i == 39)
/*      */       {
/* 1704 */         paramNode.setType(49);
/* 1705 */         localNode2 = paramNode;
/* 1706 */         localNode3 = Node.newString(paramNode.getString());
/* 1707 */         localNode1 = new Node(paramInt, localNode2, localNode3);
/* 1708 */       } else if ((i == 33) || (i == 36))
/*      */       {
/* 1711 */         localNode2 = paramNode.getFirstChild();
/* 1712 */         localNode3 = paramNode.getLastChild();
/* 1713 */         paramNode.removeChild(localNode2);
/* 1714 */         paramNode.removeChild(localNode3);
/* 1715 */         localNode1 = new Node(paramInt, localNode2, localNode3);
/* 1716 */       } else if (i == 67) {
/* 1717 */         localNode2 = paramNode.getFirstChild();
/* 1718 */         paramNode.removeChild(localNode2);
/* 1719 */         localNode1 = new Node(69, localNode2);
/* 1720 */       } else if (i == 38) {
/* 1721 */         localNode1 = new Node(paramInt, new Node(45), paramNode);
/*      */       } else {
/* 1723 */         localNode1 = new Node(45);
/*      */       }
/* 1725 */       return localNode1;
/*      */     case 32:
/* 1728 */       if (i == 39) {
/* 1729 */         paramNode.setType(137);
/* 1730 */         return paramNode;
/*      */       }
/*      */       break;
/*      */     case 27:
/* 1734 */       if (i == 40) {
/* 1735 */         j = ScriptRuntime.toInt32(paramNode.getDouble());
/* 1736 */         paramNode.setDouble(j ^ 0xFFFFFFFF);
/* 1737 */         return paramNode;
/*      */       }
/*      */       break;
/*      */     case 29:
/* 1741 */       if (i == 40) {
/* 1742 */         paramNode.setDouble(-paramNode.getDouble());
/* 1743 */         return paramNode;
/*      */       }
/*      */       break;
/*      */     case 26:
/* 1747 */       j = isAlwaysDefinedBoolean(paramNode);
/* 1748 */       if (j != 0)
/*      */       {
/*      */         int k;
/* 1750 */         if (j == 1)
/* 1751 */           k = 44;
/*      */         else {
/* 1753 */           k = 45;
/*      */         }
/* 1755 */         if ((i == 45) || (i == 44)) {
/* 1756 */           paramNode.setType(k);
/* 1757 */           return paramNode;
/*      */         }
/* 1759 */         return new Node(k);
/*      */       }break;
/*      */     case 28:
/*      */     case 30:
/*      */     }
/* 1764 */     return new Node(paramInt, paramNode);
/*      */   }
/*      */ 
/*      */   private Node createCallOrNew(int paramInt, Node paramNode) {
/* 1768 */     int i = 0;
/* 1769 */     if (paramNode.getType() == 39) {
/* 1770 */       localObject = paramNode.getString();
/* 1771 */       if (((String)localObject).equals("eval"))
/* 1772 */         i = 1;
/* 1773 */       else if (((String)localObject).equals("With"))
/* 1774 */         i = 2;
/*      */     }
/* 1776 */     else if (paramNode.getType() == 33) {
/* 1777 */       localObject = paramNode.getLastChild().getString();
/* 1778 */       if (((String)localObject).equals("eval")) {
/* 1779 */         i = 1;
/*      */       }
/*      */     }
/* 1782 */     Object localObject = new Node(paramInt, paramNode);
/* 1783 */     if (i != 0)
/*      */     {
/* 1785 */       setRequiresActivation();
/* 1786 */       ((Node)localObject).putIntProp(10, i);
/*      */     }
/* 1788 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Node createIncDec(int paramInt, boolean paramBoolean, Node paramNode)
/*      */   {
/* 1793 */     paramNode = makeReference(paramNode);
/* 1794 */     int i = paramNode.getType();
/*      */ 
/* 1796 */     switch (i) {
/*      */     case 33:
/*      */     case 36:
/*      */     case 39:
/*      */     case 67:
/* 1801 */       Node localNode = new Node(paramInt, paramNode);
/* 1802 */       int j = 0;
/* 1803 */       if (paramInt == 107) {
/* 1804 */         j |= 1;
/*      */       }
/* 1806 */       if (paramBoolean) {
/* 1807 */         j |= 2;
/*      */       }
/* 1809 */       localNode.putIntProp(13, j);
/* 1810 */       return localNode;
/*      */     }
/*      */ 
/* 1813 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private Node createPropertyGet(Node paramNode, String paramString1, String paramString2, int paramInt)
/*      */   {
/* 1819 */     if ((paramString1 == null) && (paramInt == 0)) {
/* 1820 */       if (paramNode == null) {
/* 1821 */         return createName(paramString2);
/*      */       }
/* 1823 */       checkActivationName(paramString2, 33);
/* 1824 */       if (ScriptRuntime.isSpecialProperty(paramString2)) {
/* 1825 */         localNode = new Node(71, paramNode);
/* 1826 */         localNode.putProp(17, paramString2);
/* 1827 */         return new Node(67, localNode);
/*      */       }
/* 1829 */       return new Node(33, paramNode, Node.newString(paramString2));
/*      */     }
/* 1831 */     Node localNode = Node.newString(paramString2);
/* 1832 */     paramInt |= 1;
/* 1833 */     return createMemberRefGet(paramNode, paramString1, localNode, paramInt);
/*      */   }
/*      */ 
/*      */   private Node createElementGet(Node paramNode1, String paramString, Node paramNode2, int paramInt)
/*      */   {
/* 1847 */     if ((paramString == null) && (paramInt == 0))
/*      */     {
/* 1850 */       if (paramNode1 == null) throw Kit.codeBug();
/* 1851 */       return new Node(36, paramNode1, paramNode2);
/*      */     }
/* 1853 */     return createMemberRefGet(paramNode1, paramString, paramNode2, paramInt);
/*      */   }
/*      */ 
/*      */   private Node createMemberRefGet(Node paramNode1, String paramString, Node paramNode2, int paramInt)
/*      */   {
/* 1859 */     Node localNode1 = null;
/* 1860 */     if (paramString != null)
/*      */     {
/* 1862 */       if (paramString.equals("*"))
/* 1863 */         localNode1 = new Node(42);
/*      */       else
/* 1865 */         localNode1 = createName(paramString);
/*      */     }
/*      */     Node localNode2;
/* 1869 */     if (paramNode1 == null) {
/* 1870 */       if (paramString == null)
/* 1871 */         localNode2 = new Node(79, paramNode2);
/*      */       else {
/* 1873 */         localNode2 = new Node(80, localNode1, paramNode2);
/*      */       }
/*      */     }
/* 1876 */     else if (paramString == null)
/* 1877 */       localNode2 = new Node(77, paramNode1, paramNode2);
/*      */     else {
/* 1879 */       localNode2 = new Node(78, paramNode1, localNode1, paramNode2);
/*      */     }
/*      */ 
/* 1882 */     if (paramInt != 0) {
/* 1883 */       localNode2.putIntProp(16, paramInt);
/*      */     }
/* 1885 */     return new Node(67, localNode2);
/*      */   }
/*      */ 
/*      */   private Node createBinary(int paramInt, Node paramNode1, Node paramNode2)
/*      */   {
/*      */     double d;
/*      */     int i;
/* 1889 */     switch (paramInt)
/*      */     {
/*      */     case 21:
/*      */       String str1;
/*      */       String str2;
/* 1893 */       if (paramNode1.type == 41)
/*      */       {
/* 1895 */         if (paramNode2.type == 41) {
/* 1896 */           str1 = paramNode2.getString(); } else {
/* 1897 */           if (paramNode2.type != 40) break;
/* 1898 */           str1 = ScriptRuntime.numberToString(paramNode2.getDouble(), 10);
/*      */         }
/*      */ 
/* 1902 */         str2 = paramNode1.getString();
/* 1903 */         paramNode1.setString(str2.concat(str1));
/* 1904 */         return paramNode1;
/* 1905 */       } else if (paramNode1.type == 40) {
/* 1906 */         if (paramNode2.type == 40) {
/* 1907 */           paramNode1.setDouble(paramNode1.getDouble() + paramNode2.getDouble());
/* 1908 */           return paramNode1;
/* 1909 */         }if (paramNode2.type == 41)
/*      */         {
/* 1911 */           str1 = ScriptRuntime.numberToString(paramNode1.getDouble(), 10);
/* 1912 */           str2 = paramNode2.getString();
/* 1913 */           paramNode2.setString(str1.concat(str2));
/* 1914 */           return paramNode2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 22:
/* 1924 */       if (paramNode1.type == 40) {
/* 1925 */         d = paramNode1.getDouble();
/* 1926 */         if (paramNode2.type == 40)
/*      */         {
/* 1928 */           paramNode1.setDouble(d - paramNode2.getDouble());
/* 1929 */           return paramNode1;
/* 1930 */         }if (d == 0.0D)
/*      */         {
/* 1932 */           return new Node(29, paramNode2);
/*      */         }
/* 1934 */       } else if ((paramNode2.type == 40) && 
/* 1935 */         (paramNode2.getDouble() == 0.0D))
/*      */       {
/* 1938 */         return new Node(28, paramNode1);
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 23:
/* 1945 */       if (paramNode1.type == 40) {
/* 1946 */         d = paramNode1.getDouble();
/* 1947 */         if (paramNode2.type == 40)
/*      */         {
/* 1949 */           paramNode1.setDouble(d * paramNode2.getDouble());
/* 1950 */           return paramNode1;
/* 1951 */         }if (d == 1.0D)
/*      */         {
/* 1953 */           return new Node(28, paramNode2);
/*      */         }
/* 1955 */       } else if ((paramNode2.type == 40) && 
/* 1956 */         (paramNode2.getDouble() == 1.0D))
/*      */       {
/* 1959 */         return new Node(28, paramNode1);
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 24:
/* 1967 */       if (paramNode2.type == 40) {
/* 1968 */         d = paramNode2.getDouble();
/* 1969 */         if (paramNode1.type == 40)
/*      */         {
/* 1971 */           paramNode1.setDouble(paramNode1.getDouble() / d);
/* 1972 */           return paramNode1;
/* 1973 */         }if (d == 1.0D)
/*      */         {
/* 1976 */           return new Node(28, paramNode1);
/*      */         }
/*      */       }
/* 1978 */       break;
/*      */     case 105:
/* 1986 */       i = isAlwaysDefinedBoolean(paramNode1);
/* 1987 */       if (i == -1)
/*      */       {
/* 1989 */         return paramNode1;
/* 1990 */       }if (i == 1)
/*      */       {
/* 1992 */         return paramNode2;
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 104:
/* 2002 */       i = isAlwaysDefinedBoolean(paramNode1);
/* 2003 */       if (i == 1)
/*      */       {
/* 2005 */         return paramNode1;
/* 2006 */       }if (i == -1)
/*      */       {
/* 2008 */         return paramNode2;
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/* 2014 */     return new Node(paramInt, paramNode1, paramNode2);
/*      */   }
/*      */ 
/*      */   private Node createAssignment(int paramInt, Node paramNode1, Node paramNode2)
/*      */   {
/* 2019 */     Node localNode1 = makeReference(paramNode1);
/* 2020 */     if (localNode1 == null) {
/* 2021 */       if ((paramNode1.getType() == 65) || (paramNode1.getType() == 66))
/*      */       {
/* 2024 */         if (paramInt != 90) {
/* 2025 */           reportError("msg.bad.destruct.op");
/* 2026 */           return paramNode2;
/*      */         }
/* 2028 */         return createDestructuringAssignment(-1, paramNode1, paramNode2);
/*      */       }
/* 2030 */       reportError("msg.bad.assign.left");
/* 2031 */       return paramNode2;
/*      */     }
/* 2033 */     paramNode1 = localNode1;
/*      */     int i;
/* 2036 */     switch (paramInt) {
/*      */     case 90:
/* 2038 */       return simpleAssignment(paramNode1, paramNode2);
/*      */     case 91:
/* 2039 */       i = 9; break;
/*      */     case 92:
/* 2040 */       i = 10; break;
/*      */     case 93:
/* 2041 */       i = 11; break;
/*      */     case 94:
/* 2042 */       i = 18; break;
/*      */     case 95:
/* 2043 */       i = 19; break;
/*      */     case 96:
/* 2044 */       i = 20; break;
/*      */     case 97:
/* 2045 */       i = 21; break;
/*      */     case 98:
/* 2046 */       i = 22; break;
/*      */     case 99:
/* 2047 */       i = 23; break;
/*      */     case 100:
/* 2048 */       i = 24; break;
/*      */     case 101:
/* 2049 */       i = 25; break;
/*      */     default:
/* 2050 */       throw Kit.codeBug();
/*      */     }
/*      */ 
/* 2053 */     int j = paramNode1.getType();
/*      */     Node localNode2;
/*      */     Node localNode3;
/* 2054 */     switch (j) {
/*      */     case 39:
/* 2056 */       localNode2 = new Node(i, paramNode1, paramNode2);
/* 2057 */       localNode3 = Node.newString(49, paramNode1.getString());
/* 2058 */       return new Node(8, localNode3, localNode2);
/*      */     case 33:
/*      */     case 36:
/* 2062 */       localNode2 = paramNode1.getFirstChild();
/* 2063 */       localNode3 = paramNode1.getLastChild();
/*      */ 
/* 2065 */       int k = j == 33 ? 139 : 140;
/*      */ 
/* 2069 */       Node localNode4 = new Node(138);
/* 2070 */       Node localNode5 = new Node(i, localNode4, paramNode2);
/* 2071 */       return new Node(k, localNode2, localNode3, localNode5);
/*      */     case 67:
/* 2074 */       localNode1 = paramNode1.getFirstChild();
/* 2075 */       checkMutableReference(localNode1);
/* 2076 */       localNode2 = new Node(138);
/* 2077 */       localNode3 = new Node(i, localNode2, paramNode2);
/* 2078 */       return new Node(142, localNode1, localNode3);
/*      */     }
/*      */ 
/* 2082 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   private Node createUseLocal(Node paramNode) {
/* 2086 */     if (141 != paramNode.getType()) throw Kit.codeBug();
/* 2087 */     Node localNode = new Node(54);
/* 2088 */     localNode.putProp(3, paramNode);
/* 2089 */     return localNode;
/*      */   }
/*      */ 
/*      */   private Jump makeJump(int paramInt, Node paramNode) {
/* 2093 */     Jump localJump = new Jump(paramInt);
/* 2094 */     localJump.target = paramNode;
/* 2095 */     return localJump;
/*      */   }
/*      */ 
/*      */   private Node makeReference(Node paramNode) {
/* 2099 */     int i = paramNode.getType();
/* 2100 */     switch (i) {
/*      */     case 33:
/*      */     case 36:
/*      */     case 39:
/*      */     case 67:
/* 2105 */       return paramNode;
/*      */     case 38:
/* 2107 */       paramNode.setType(70);
/* 2108 */       return new Node(67, paramNode);
/*      */     }
/*      */ 
/* 2111 */     return null;
/*      */   }
/*      */ 
/*      */   private static int isAlwaysDefinedBoolean(Node paramNode)
/*      */   {
/* 2116 */     switch (paramNode.getType()) {
/*      */     case 42:
/*      */     case 44:
/* 2119 */       return -1;
/*      */     case 45:
/* 2121 */       return 1;
/*      */     case 40:
/* 2123 */       double d = paramNode.getDouble();
/* 2124 */       if ((d == d) && (d != 0.0D)) {
/* 2125 */         return 1;
/*      */       }
/* 2127 */       return -1;
/*      */     case 41:
/*      */     case 43:
/*      */     }
/* 2131 */     return 0;
/*      */   }
/*      */ 
/*      */   boolean isDestructuring(Node paramNode)
/*      */   {
/* 2136 */     return ((paramNode instanceof DestructuringForm)) && (((DestructuringForm)paramNode).isDestructuring());
/*      */   }
/*      */ 
/*      */   Node decompileFunctionHeader(FunctionNode paramFunctionNode)
/*      */   {
/* 2141 */     Node localNode = null;
/* 2142 */     if (paramFunctionNode.getFunctionName() != null)
/* 2143 */       this.decompiler.addName(paramFunctionNode.getName());
/* 2144 */     else if (paramFunctionNode.getMemberExprNode() != null) {
/* 2145 */       localNode = transform(paramFunctionNode.getMemberExprNode());
/*      */     }
/* 2147 */     this.decompiler.addToken(87);
/* 2148 */     List localList = paramFunctionNode.getParams();
/* 2149 */     for (int i = 0; i < localList.size(); i++) {
/* 2150 */       decompile((AstNode)localList.get(i));
/* 2151 */       if (i < localList.size() - 1) {
/* 2152 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/* 2155 */     this.decompiler.addToken(88);
/* 2156 */     if (!paramFunctionNode.isExpressionClosure()) {
/* 2157 */       this.decompiler.addEOL(85);
/*      */     }
/* 2159 */     return localNode;
/*      */   }
/*      */ 
/*      */   void decompile(AstNode paramAstNode) {
/* 2163 */     switch (paramAstNode.getType()) {
/*      */     case 65:
/* 2165 */       decompileArrayLiteral((ArrayLiteral)paramAstNode);
/* 2166 */       break;
/*      */     case 66:
/* 2168 */       decompileObjectLiteral((ObjectLiteral)paramAstNode);
/* 2169 */       break;
/*      */     case 41:
/* 2171 */       this.decompiler.addString(((StringLiteral)paramAstNode).getValue());
/* 2172 */       break;
/*      */     case 39:
/* 2174 */       this.decompiler.addName(((Name)paramAstNode).getIdentifier());
/* 2175 */       break;
/*      */     case 40:
/* 2177 */       this.decompiler.addNumber(((NumberLiteral)paramAstNode).getNumber());
/* 2178 */       break;
/*      */     case 33:
/* 2180 */       decompilePropertyGet((PropertyGet)paramAstNode);
/* 2181 */       break;
/*      */     case 128:
/* 2183 */       break;
/*      */     case 36:
/* 2185 */       decompileElementGet((ElementGet)paramAstNode);
/* 2186 */       break;
/*      */     default:
/* 2188 */       Kit.codeBug("unexpected token: " + Token.typeToName(paramAstNode.getType()));
/*      */     }
/*      */   }
/*      */ 
/*      */   void decompileArrayLiteral(ArrayLiteral paramArrayLiteral)
/*      */   {
/* 2195 */     this.decompiler.addToken(83);
/* 2196 */     List localList = paramArrayLiteral.getElements();
/* 2197 */     int i = localList.size();
/* 2198 */     for (int j = 0; j < i; j++) {
/* 2199 */       AstNode localAstNode = (AstNode)localList.get(j);
/* 2200 */       decompile(localAstNode);
/* 2201 */       if (j < i - 1) {
/* 2202 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/* 2205 */     this.decompiler.addToken(84);
/*      */   }
/*      */ 
/*      */   void decompileObjectLiteral(ObjectLiteral paramObjectLiteral)
/*      */   {
/* 2210 */     this.decompiler.addToken(85);
/* 2211 */     List localList = paramObjectLiteral.getElements();
/* 2212 */     int i = localList.size();
/* 2213 */     for (int j = 0; j < i; j++) {
/* 2214 */       ObjectProperty localObjectProperty = (ObjectProperty)localList.get(j);
/* 2215 */       boolean bool = Boolean.TRUE.equals(localObjectProperty.getProp(26));
/*      */ 
/* 2217 */       decompile(localObjectProperty.getLeft());
/* 2218 */       if (!bool) {
/* 2219 */         this.decompiler.addToken(103);
/* 2220 */         decompile(localObjectProperty.getRight());
/*      */       }
/* 2222 */       if (j < i - 1) {
/* 2223 */         this.decompiler.addToken(89);
/*      */       }
/*      */     }
/* 2226 */     this.decompiler.addToken(86);
/*      */   }
/*      */ 
/*      */   void decompilePropertyGet(PropertyGet paramPropertyGet)
/*      */   {
/* 2231 */     decompile(paramPropertyGet.getTarget());
/* 2232 */     this.decompiler.addToken(108);
/* 2233 */     decompile(paramPropertyGet.getProperty());
/*      */   }
/*      */ 
/*      */   void decompileElementGet(ElementGet paramElementGet)
/*      */   {
/* 2238 */     decompile(paramElementGet.getTarget());
/* 2239 */     this.decompiler.addToken(83);
/* 2240 */     decompile(paramElementGet.getElement());
/* 2241 */     this.decompiler.addToken(84);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.IRFactory
 * JD-Core Version:    0.6.2
 */