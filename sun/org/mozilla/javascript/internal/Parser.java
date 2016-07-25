/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayComprehension;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ArrayLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.Assignment;
/*      */ import sun.org.mozilla.javascript.internal.ast.AstNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.AstRoot;
/*      */ import sun.org.mozilla.javascript.internal.ast.Block;
/*      */ import sun.org.mozilla.javascript.internal.ast.BreakStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.CatchClause;
/*      */ import sun.org.mozilla.javascript.internal.ast.Comment;
/*      */ import sun.org.mozilla.javascript.internal.ast.ConditionalExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.ContinueStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.DestructuringForm;
/*      */ import sun.org.mozilla.javascript.internal.ast.DoLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ElementGet;
/*      */ import sun.org.mozilla.javascript.internal.ast.EmptyExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.ErrorNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.ExpressionStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.ForInLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.ForLoop;
/*      */ import sun.org.mozilla.javascript.internal.ast.FunctionCall;
/*      */ import sun.org.mozilla.javascript.internal.ast.FunctionNode;
/*      */ import sun.org.mozilla.javascript.internal.ast.IdeErrorReporter;
/*      */ import sun.org.mozilla.javascript.internal.ast.IfStatement;
/*      */ import sun.org.mozilla.javascript.internal.ast.InfixExpression;
/*      */ import sun.org.mozilla.javascript.internal.ast.Jump;
/*      */ import sun.org.mozilla.javascript.internal.ast.KeywordLiteral;
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
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlMemberGet;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlPropRef;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlRef;
/*      */ import sun.org.mozilla.javascript.internal.ast.XmlString;
/*      */ import sun.org.mozilla.javascript.internal.ast.Yield;
/*      */ 
/*      */ public class Parser
/*      */ {
/*      */   public static final int ARGC_LIMIT = 65536;
/*      */   static final int CLEAR_TI_MASK = 65535;
/*      */   static final int TI_AFTER_EOL = 65536;
/*      */   static final int TI_CHECK_LABEL = 131072;
/*      */   CompilerEnvirons compilerEnv;
/*      */   private ErrorReporter errorReporter;
/*      */   private IdeErrorReporter errorCollector;
/*      */   private String sourceURI;
/*      */   private char[] sourceChars;
/*      */   boolean calledByCompileFunction;
/*      */   private boolean parseFinished;
/*      */   private TokenStream ts;
/*  109 */   private int currentFlaggedToken = 0;
/*      */   private int currentToken;
/*      */   private int syntaxErrorCount;
/*      */   private List<Comment> scannedComments;
/*      */   private String currentJsDocComment;
/*      */   protected int nestingOfFunction;
/*      */   private LabeledStatement currentLabel;
/*      */   private boolean inDestructuringAssignment;
/*      */   protected boolean inUseStrictDirective;
/*      */   ScriptNode currentScriptOrFn;
/*      */   Scope currentScope;
/*      */   int nestingOfWith;
/*      */   private int endFlags;
/*      */   private boolean inForInit;
/*      */   private Map<String, LabeledStatement> labelSet;
/*      */   private List<Loop> loopSet;
/*      */   private List<Jump> loopAndSwitchSet;
/*      */   private int prevNameTokenStart;
/*  137 */   private String prevNameTokenString = "";
/*      */   private int prevNameTokenLineno;
/*      */ 
/*      */   public Parser()
/*      */   {
/*  147 */     this(new CompilerEnvirons());
/*      */   }
/*      */ 
/*      */   public Parser(CompilerEnvirons paramCompilerEnvirons) {
/*  151 */     this(paramCompilerEnvirons, paramCompilerEnvirons.getErrorReporter());
/*      */   }
/*      */ 
/*      */   public Parser(CompilerEnvirons paramCompilerEnvirons, ErrorReporter paramErrorReporter) {
/*  155 */     this.compilerEnv = paramCompilerEnvirons;
/*  156 */     this.errorReporter = paramErrorReporter;
/*  157 */     if ((paramErrorReporter instanceof IdeErrorReporter))
/*  158 */       this.errorCollector = ((IdeErrorReporter)paramErrorReporter);
/*      */   }
/*      */ 
/*      */   void addStrictWarning(String paramString1, String paramString2)
/*      */   {
/*  164 */     int i = -1; int j = -1;
/*  165 */     if (this.ts != null) {
/*  166 */       i = this.ts.tokenBeg;
/*  167 */       j = this.ts.tokenEnd - this.ts.tokenBeg;
/*      */     }
/*  169 */     addStrictWarning(paramString1, paramString2, i, j);
/*      */   }
/*      */ 
/*      */   void addStrictWarning(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */   {
/*  174 */     if (this.compilerEnv.isStrictMode())
/*  175 */       addWarning(paramString1, paramString2, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   void addWarning(String paramString1, String paramString2) {
/*  179 */     int i = -1; int j = -1;
/*  180 */     if (this.ts != null) {
/*  181 */       i = this.ts.tokenBeg;
/*  182 */       j = this.ts.tokenEnd - this.ts.tokenBeg;
/*      */     }
/*  184 */     addWarning(paramString1, paramString2, i, j);
/*      */   }
/*      */ 
/*      */   void addWarning(String paramString, int paramInt1, int paramInt2) {
/*  188 */     addWarning(paramString, null, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   void addWarning(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */   {
/*  194 */     String str = lookupMessage(paramString1, paramString2);
/*  195 */     if (this.compilerEnv.reportWarningAsError())
/*  196 */       addError(paramString1, paramString2, paramInt1, paramInt2);
/*  197 */     else if (this.errorCollector != null)
/*  198 */       this.errorCollector.warning(str, this.sourceURI, paramInt1, paramInt2);
/*      */     else
/*  200 */       this.errorReporter.warning(str, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
/*      */   }
/*      */ 
/*      */   void addError(String paramString)
/*      */   {
/*  206 */     addError(paramString, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */   void addError(String paramString, int paramInt1, int paramInt2) {
/*  210 */     addError(paramString, null, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   void addError(String paramString1, String paramString2) {
/*  214 */     addError(paramString1, paramString2, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */   void addError(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */   {
/*  220 */     this.syntaxErrorCount += 1;
/*  221 */     String str1 = lookupMessage(paramString1, paramString2);
/*  222 */     if (this.errorCollector != null) {
/*  223 */       this.errorCollector.error(str1, this.sourceURI, paramInt1, paramInt2);
/*      */     } else {
/*  225 */       int i = 1; int j = 1;
/*  226 */       String str2 = "";
/*  227 */       if (this.ts != null) {
/*  228 */         i = this.ts.getLineno();
/*  229 */         str2 = this.ts.getLine();
/*  230 */         j = this.ts.getOffset();
/*      */       }
/*  232 */       this.errorReporter.error(str1, this.sourceURI, i, str2, j);
/*      */     }
/*      */   }
/*      */ 
/*      */   String lookupMessage(String paramString) {
/*  237 */     return lookupMessage(paramString, null);
/*      */   }
/*      */ 
/*      */   String lookupMessage(String paramString1, String paramString2) {
/*  241 */     return paramString2 == null ? ScriptRuntime.getMessage0(paramString1) : ScriptRuntime.getMessage1(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   void reportError(String paramString)
/*      */   {
/*  247 */     reportError(paramString, null);
/*      */   }
/*      */ 
/*      */   void reportError(String paramString1, String paramString2) {
/*  251 */     if (this.ts == null)
/*  252 */       reportError(paramString1, paramString2, 1, 1);
/*      */     else
/*  254 */       reportError(paramString1, paramString2, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */   void reportError(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  261 */     reportError(paramString, null, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   void reportError(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */   {
/*  267 */     addError(paramString1, paramInt1, paramInt2);
/*      */ 
/*  269 */     if (!this.compilerEnv.recoverFromErrors())
/*  270 */       throw new ParserException(null);
/*      */   }
/*      */ 
/*      */   private int getNodeEnd(AstNode paramAstNode)
/*      */   {
/*  278 */     return paramAstNode.getPosition() + paramAstNode.getLength();
/*      */   }
/*      */ 
/*      */   private void recordComment(int paramInt) {
/*  282 */     if (this.scannedComments == null) {
/*  283 */       this.scannedComments = new ArrayList();
/*      */     }
/*  285 */     String str = this.ts.getAndResetCurrentComment();
/*  286 */     if ((this.ts.commentType == Token.CommentType.JSDOC) && (this.compilerEnv.isRecordingLocalJsDocComments()))
/*      */     {
/*  288 */       this.currentJsDocComment = str;
/*      */     }
/*  290 */     Comment localComment = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, str);
/*      */ 
/*  294 */     localComment.setLineno(paramInt);
/*  295 */     this.scannedComments.add(localComment);
/*      */   }
/*      */ 
/*      */   private String getAndResetJsDoc() {
/*  299 */     String str = this.currentJsDocComment;
/*  300 */     this.currentJsDocComment = null;
/*  301 */     return str;
/*      */   }
/*      */ 
/*      */   private int peekToken()
/*      */     throws IOException
/*      */   {
/*  324 */     if (this.currentFlaggedToken != 0) {
/*  325 */       return this.currentToken;
/*      */     }
/*      */ 
/*  328 */     int i = this.ts.getLineno();
/*  329 */     int j = this.ts.getToken();
/*  330 */     int k = 0;
/*      */ 
/*  333 */     while ((j == 1) || (j == 161)) {
/*  334 */       if (j == 1) {
/*  335 */         i++;
/*  336 */         k = 1;
/*      */       } else {
/*  338 */         k = 0;
/*  339 */         if (this.compilerEnv.isRecordingComments()) {
/*  340 */           recordComment(i);
/*      */         }
/*      */       }
/*  343 */       j = this.ts.getToken();
/*      */     }
/*      */ 
/*  346 */     this.currentToken = j;
/*  347 */     this.currentFlaggedToken = (j | (k != 0 ? 65536 : 0));
/*  348 */     return this.currentToken;
/*      */   }
/*      */ 
/*      */   private int peekFlaggedToken()
/*      */     throws IOException
/*      */   {
/*  354 */     peekToken();
/*  355 */     return this.currentFlaggedToken;
/*      */   }
/*      */ 
/*      */   private void consumeToken() {
/*  359 */     this.currentFlaggedToken = 0;
/*      */   }
/*      */ 
/*      */   private int nextToken()
/*      */     throws IOException
/*      */   {
/*  365 */     int i = peekToken();
/*  366 */     consumeToken();
/*  367 */     return i;
/*      */   }
/*      */ 
/*      */   private int nextFlaggedToken()
/*      */     throws IOException
/*      */   {
/*  373 */     peekToken();
/*  374 */     int i = this.currentFlaggedToken;
/*  375 */     consumeToken();
/*  376 */     return i;
/*      */   }
/*      */ 
/*      */   private boolean matchToken(int paramInt)
/*      */     throws IOException
/*      */   {
/*  382 */     if (peekToken() != paramInt) {
/*  383 */       return false;
/*      */     }
/*  385 */     consumeToken();
/*  386 */     return true;
/*      */   }
/*      */ 
/*      */   private int peekTokenOrEOL()
/*      */     throws IOException
/*      */   {
/*  397 */     int i = peekToken();
/*      */ 
/*  399 */     if ((this.currentFlaggedToken & 0x10000) != 0) {
/*  400 */       i = 1;
/*      */     }
/*  402 */     return i;
/*      */   }
/*      */ 
/*      */   private boolean mustMatchToken(int paramInt, String paramString)
/*      */     throws IOException
/*      */   {
/*  408 */     return mustMatchToken(paramInt, paramString, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */   private boolean mustMatchToken(int paramInt1, String paramString, int paramInt2, int paramInt3)
/*      */     throws IOException
/*      */   {
/*  415 */     if (matchToken(paramInt1)) {
/*  416 */       return true;
/*      */     }
/*  418 */     reportError(paramString, paramInt2, paramInt3);
/*  419 */     return false;
/*      */   }
/*      */ 
/*      */   private void mustHaveXML() {
/*  423 */     if (!this.compilerEnv.isXmlAvailable())
/*  424 */       reportError("msg.XML.not.available");
/*      */   }
/*      */ 
/*      */   public boolean eof()
/*      */   {
/*  429 */     return this.ts.eof();
/*      */   }
/*      */ 
/*      */   boolean insideFunction() {
/*  433 */     return this.nestingOfFunction != 0;
/*      */   }
/*      */ 
/*      */   void pushScope(Scope paramScope) {
/*  437 */     Scope localScope = paramScope.getParentScope();
/*      */ 
/*  440 */     if (localScope != null) {
/*  441 */       if (localScope != this.currentScope)
/*  442 */         codeBug();
/*      */     }
/*  444 */     else this.currentScope.addChildScope(paramScope);
/*      */ 
/*  446 */     this.currentScope = paramScope;
/*      */   }
/*      */ 
/*      */   void popScope() {
/*  450 */     this.currentScope = this.currentScope.getParentScope();
/*      */   }
/*      */ 
/*      */   private void enterLoop(Loop paramLoop) {
/*  454 */     if (this.loopSet == null)
/*  455 */       this.loopSet = new ArrayList();
/*  456 */     this.loopSet.add(paramLoop);
/*  457 */     if (this.loopAndSwitchSet == null)
/*  458 */       this.loopAndSwitchSet = new ArrayList();
/*  459 */     this.loopAndSwitchSet.add(paramLoop);
/*  460 */     pushScope(paramLoop);
/*  461 */     if (this.currentLabel != null) {
/*  462 */       this.currentLabel.setStatement(paramLoop);
/*  463 */       this.currentLabel.getFirstLabel().setLoop(paramLoop);
/*      */ 
/*  468 */       paramLoop.setRelative(-this.currentLabel.getPosition());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void exitLoop() {
/*  473 */     Loop localLoop = (Loop)this.loopSet.remove(this.loopSet.size() - 1);
/*  474 */     this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
/*  475 */     if (localLoop.getParent() != null) {
/*  476 */       localLoop.setRelative(localLoop.getParent().getPosition());
/*      */     }
/*  478 */     popScope();
/*      */   }
/*      */ 
/*      */   private void enterSwitch(SwitchStatement paramSwitchStatement) {
/*  482 */     if (this.loopAndSwitchSet == null)
/*  483 */       this.loopAndSwitchSet = new ArrayList();
/*  484 */     this.loopAndSwitchSet.add(paramSwitchStatement);
/*      */   }
/*      */ 
/*      */   private void exitSwitch() {
/*  488 */     this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
/*      */   }
/*      */ 
/*      */   public AstRoot parse(String paramString1, String paramString2, int paramInt)
/*      */   {
/*  501 */     if (this.parseFinished) throw new IllegalStateException("parser reused");
/*  502 */     this.sourceURI = paramString2;
/*  503 */     if (this.compilerEnv.isIdeMode()) {
/*  504 */       this.sourceChars = paramString1.toCharArray();
/*      */     }
/*  506 */     this.ts = new TokenStream(this, null, paramString1, paramInt);
/*      */     try {
/*  508 */       return parse();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  511 */       throw new IllegalStateException();
/*      */     } finally {
/*  513 */       this.parseFinished = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public AstRoot parse(Reader paramReader, String paramString, int paramInt)
/*      */     throws IOException
/*      */   {
/*  525 */     if (this.parseFinished) throw new IllegalStateException("parser reused");
/*  526 */     if (this.compilerEnv.isIdeMode())
/*  527 */       return parse(readFully(paramReader), paramString, paramInt);
/*      */     try
/*      */     {
/*  530 */       this.sourceURI = paramString;
/*  531 */       this.ts = new TokenStream(this, paramReader, null, paramInt);
/*  532 */       return parse();
/*      */     } finally {
/*  534 */       this.parseFinished = true; }  } 
/*  540 */   private AstRoot parse() throws IOException { int i = 0;
/*  541 */     AstRoot localAstRoot = new AstRoot(i);
/*  542 */     this.currentScope = (this.currentScriptOrFn = localAstRoot);
/*      */ 
/*  544 */     int j = this.ts.lineno;
/*  545 */     int k = i;
/*      */ 
/*  547 */     int m = 1;
/*  548 */     boolean bool = this.inUseStrictDirective;
/*      */ 
/*  550 */     this.inUseStrictDirective = false;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     try { while (true) { int n = peekToken();
/*  555 */         if (n <= 0)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  560 */         if (n == 109) {
/*  561 */           consumeToken();
/*      */           try {
/*  563 */             localObject1 = function(this.calledByCompileFunction ? 2 : 1);
/*      */           }
/*      */           catch (ParserException localParserException)
/*      */           {
/*  567 */             break;
/*      */           }
/*      */         } else {
/*  570 */           localObject1 = statement();
/*  571 */           if (m != 0) {
/*  572 */             localObject2 = getDirective((AstNode)localObject1);
/*  573 */             if (localObject2 == null) {
/*  574 */               m = 0;
/*  575 */             } else if (((String)localObject2).equals("use strict")) {
/*  576 */               this.inUseStrictDirective = true;
/*  577 */               localAstRoot.setInStrictMode(true);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  582 */         k = getNodeEnd((AstNode)localObject1);
/*  583 */         localAstRoot.addChildToBack((Node)localObject1);
/*  584 */         ((AstNode)localObject1).setParent(localAstRoot); }
/*      */     } catch (StackOverflowError localStackOverflowError)
/*      */     {
/*  587 */       localObject1 = lookupMessage("msg.too.deep.parser.recursion");
/*  588 */       if (!this.compilerEnv.isIdeMode())
/*  589 */         throw Context.reportRuntimeError((String)localObject1, this.sourceURI, this.ts.lineno, null, 0);
/*      */     }
/*      */     finally {
/*  592 */       this.inUseStrictDirective = bool;
/*      */     }
/*      */ 
/*  595 */     if (this.syntaxErrorCount != 0) {
/*  596 */       String str = String.valueOf(this.syntaxErrorCount);
/*  597 */       str = lookupMessage("msg.got.syntax.errors", str);
/*  598 */       if (!this.compilerEnv.isIdeMode()) {
/*  599 */         throw this.errorReporter.runtimeError(str, this.sourceURI, j, null, 0);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  604 */     if (this.scannedComments != null)
/*      */     {
/*  607 */       int i1 = this.scannedComments.size() - 1;
/*  608 */       k = Math.max(k, getNodeEnd((AstNode)this.scannedComments.get(i1)));
/*  609 */       for (localObject1 = this.scannedComments.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Comment)((Iterator)localObject1).next();
/*  610 */         localAstRoot.addComment((Comment)localObject2);
/*      */       }
/*      */     }
/*      */ 
/*  614 */     localAstRoot.setLength(k - i);
/*  615 */     localAstRoot.setSourceName(this.sourceURI);
/*  616 */     localAstRoot.setBaseLineno(j);
/*  617 */     localAstRoot.setEndLineno(this.ts.lineno);
/*  618 */     return localAstRoot;
/*      */   }
/*      */ 
/*      */   private AstNode parseFunctionBody()
/*      */     throws IOException
/*      */   {
/*  624 */     if (!matchToken(85)) {
/*  625 */       if (this.compilerEnv.getLanguageVersion() < 180) {
/*  626 */         reportError("msg.no.brace.body");
/*      */       }
/*  628 */       return parseFunctionBodyExpr();
/*      */     }
/*  630 */     this.nestingOfFunction += 1;
/*  631 */     int i = this.ts.tokenBeg;
/*  632 */     Block localBlock = new Block(i);
/*      */ 
/*  634 */     int j = 1;
/*  635 */     boolean bool = this.inUseStrictDirective;
/*      */ 
/*  638 */     localBlock.setLineno(this.ts.lineno);
/*      */     try
/*      */     {
/*      */       while (true) {
/*  642 */         int m = peekToken();
/*      */         Object localObject1;
/*  643 */         switch (m) {
/*      */         case -1:
/*      */         case 0:
/*      */         case 86:
/*  647 */           break;
/*      */         case 109:
/*  650 */           consumeToken();
/*  651 */           localObject1 = function(1);
/*  652 */           break;
/*      */         default:
/*  654 */           localObject1 = statement();
/*  655 */           if (j != 0) {
/*  656 */             String str = getDirective((AstNode)localObject1);
/*  657 */             if (str == null)
/*  658 */               j = 0;
/*  659 */             else if (str.equals("use strict")) {
/*  660 */               this.inUseStrictDirective = true;
/*      */             }
/*      */           }
/*      */           break;
/*      */         }
/*  665 */         localBlock.addStatement((AstNode)localObject1);
/*      */       }
/*      */     } catch (ParserException localParserException) {
/*      */     }
/*      */     finally {
/*  670 */       this.nestingOfFunction -= 1;
/*  671 */       this.inUseStrictDirective = bool;
/*      */     }
/*      */ 
/*  674 */     int k = this.ts.tokenEnd;
/*  675 */     getAndResetJsDoc();
/*  676 */     if (mustMatchToken(86, "msg.no.brace.after.body"))
/*  677 */       k = this.ts.tokenEnd;
/*  678 */     localBlock.setLength(k - i);
/*  679 */     return localBlock;
/*      */   }
/*      */ 
/*      */   private String getDirective(AstNode paramAstNode) {
/*  683 */     if ((paramAstNode instanceof ExpressionStatement)) {
/*  684 */       AstNode localAstNode = ((ExpressionStatement)paramAstNode).getExpression();
/*  685 */       if ((localAstNode instanceof StringLiteral)) {
/*  686 */         return ((StringLiteral)localAstNode).getValue();
/*      */       }
/*      */     }
/*  689 */     return null;
/*      */   }
/*      */ 
/*      */   private void parseFunctionParams(FunctionNode paramFunctionNode)
/*      */     throws IOException
/*      */   {
/*  695 */     if (matchToken(88)) {
/*  696 */       paramFunctionNode.setRp(this.ts.tokenBeg - paramFunctionNode.getPosition());
/*  697 */       return; } 
/*      */ HashMap localHashMap = null;
/*  702 */     HashSet localHashSet = new HashSet();
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     do { int i = peekToken();
/*  705 */       if ((i == 83) || (i == 85)) {
/*  706 */         localObject1 = destructuringPrimaryExpr();
/*  707 */         markDestructuring((AstNode)localObject1);
/*  708 */         paramFunctionNode.addParam((AstNode)localObject1);
/*      */ 
/*  712 */         if (localHashMap == null) {
/*  713 */           localHashMap = new HashMap();
/*      */         }
/*  715 */         localObject2 = this.currentScriptOrFn.getNextTempName();
/*  716 */         defineSymbol(87, (String)localObject2, false);
/*  717 */         localHashMap.put(localObject2, localObject1);
/*      */       }
/*  719 */       else if (mustMatchToken(39, "msg.no.parm")) {
/*  720 */         paramFunctionNode.addParam(createNameNode());
/*  721 */         localObject1 = this.ts.getString();
/*  722 */         defineSymbol(87, (String)localObject1);
/*  723 */         if (this.inUseStrictDirective) {
/*  724 */           if (("eval".equals(localObject1)) || ("arguments".equals(localObject1)))
/*      */           {
/*  727 */             reportError("msg.bad.id.strict", (String)localObject1);
/*      */           }
/*  729 */           if (localHashSet.contains(localObject1))
/*  730 */             addError("msg.dup.param.strict", (String)localObject1);
/*  731 */           localHashSet.add(localObject1);
/*      */         }
/*      */       } else {
/*  734 */         paramFunctionNode.addParam(makeErrorNode());
/*      */       }
/*      */     }
/*  737 */     while (matchToken(89));
/*      */ 
/*  739 */     if (localHashMap != null) {
/*  740 */       Node localNode1 = new Node(89);
/*      */ 
/*  742 */       for (localObject1 = localHashMap.entrySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map.Entry)((Iterator)localObject1).next();
/*  743 */         Node localNode2 = createDestructuringAssignment(122, (Node)((Map.Entry)localObject2).getValue(), createName((String)((Map.Entry)localObject2).getKey()));
/*      */ 
/*  745 */         localNode1.addChildToBack(localNode2);
/*      */       }
/*      */ 
/*  748 */       paramFunctionNode.putProp(23, localNode1);
/*      */     }
/*      */ 
/*  751 */     if (mustMatchToken(88, "msg.no.paren.after.parms"))
/*  752 */       paramFunctionNode.setRp(this.ts.tokenBeg - paramFunctionNode.getPosition());
/*      */   }
/*      */ 
/*      */   private AstNode parseFunctionBodyExpr()
/*      */     throws IOException
/*      */   {
/*  760 */     this.nestingOfFunction += 1;
/*  761 */     int i = this.ts.getLineno();
/*  762 */     ReturnStatement localReturnStatement = new ReturnStatement(i);
/*  763 */     localReturnStatement.putProp(25, Boolean.TRUE);
/*      */     try {
/*  765 */       localReturnStatement.setReturnValue(assignExpr());
/*      */     } finally {
/*  767 */       this.nestingOfFunction -= 1;
/*      */     }
/*  769 */     return localReturnStatement;
/*      */   }
/*      */ 
/*      */   private FunctionNode function(int paramInt)
/*      */     throws IOException
/*      */   {
/*  775 */     int i = paramInt;
/*  776 */     int j = this.ts.lineno;
/*  777 */     int k = this.ts.tokenBeg;
/*  778 */     Name localName = null;
/*  779 */     AstNode localAstNode = null;
/*      */ 
/*  781 */     if (matchToken(39)) {
/*  782 */       localName = createNameNode(true, 39);
/*      */       Object localObject1;
/*  783 */       if (this.inUseStrictDirective) {
/*  784 */         localObject1 = localName.getIdentifier();
/*  785 */         if (("eval".equals(localObject1)) || ("arguments".equals(localObject1))) {
/*  786 */           reportError("msg.bad.id.strict", (String)localObject1);
/*      */         }
/*      */       }
/*  789 */       if (!matchToken(87)) {
/*  790 */         if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
/*  791 */           localObject1 = localName;
/*  792 */           localName = null;
/*  793 */           localAstNode = memberExprTail(false, (AstNode)localObject1);
/*      */         }
/*  795 */         mustMatchToken(87, "msg.no.paren.parms");
/*      */       }
/*  797 */     } else if (!matchToken(87))
/*      */     {
/*  800 */       if (this.compilerEnv.isAllowMemberExprAsFunctionName())
/*      */       {
/*  804 */         localAstNode = memberExpr(false);
/*      */       }
/*  806 */       mustMatchToken(87, "msg.no.paren.parms");
/*      */     }
/*  808 */     int m = this.currentToken == 87 ? this.ts.tokenBeg : -1;
/*      */ 
/*  810 */     if (localAstNode != null) {
/*  811 */       i = 2;
/*      */     }
/*      */ 
/*  814 */     if ((i != 2) && (localName != null) && (localName.length() > 0))
/*      */     {
/*  817 */       defineSymbol(109, localName.getIdentifier());
/*      */     }
/*      */ 
/*  820 */     FunctionNode localFunctionNode = new FunctionNode(k, localName);
/*  821 */     localFunctionNode.setFunctionType(paramInt);
/*  822 */     if (m != -1) {
/*  823 */       localFunctionNode.setLp(m - k);
/*      */     }
/*  825 */     if ((insideFunction()) || (this.nestingOfWith > 0))
/*      */     {
/*  831 */       localFunctionNode.setIgnoreDynamicScope();
/*      */     }
/*      */ 
/*  834 */     localFunctionNode.setJsDoc(getAndResetJsDoc());
/*      */ 
/*  836 */     PerFunctionVariables localPerFunctionVariables = new PerFunctionVariables(localFunctionNode);
/*      */     try {
/*  838 */       parseFunctionParams(localFunctionNode);
/*  839 */       localFunctionNode.setBody(parseFunctionBody());
/*  840 */       localFunctionNode.setEncodedSourceBounds(k, this.ts.tokenEnd);
/*  841 */       localFunctionNode.setLength(this.ts.tokenEnd - k);
/*      */ 
/*  843 */       if ((this.compilerEnv.isStrictMode()) && (!localFunctionNode.getBody().hasConsistentReturnUsage()))
/*      */       {
/*  845 */         String str = (localName != null) && (localName.length() > 0) ? "msg.no.return.value" : "msg.anon.no.return.value";
/*      */ 
/*  848 */         addStrictWarning(str, localName == null ? "" : localName.getIdentifier());
/*      */       }
/*      */     } finally {
/*  851 */       localPerFunctionVariables.restore();
/*      */     }
/*      */ 
/*  854 */     if (localAstNode != null)
/*      */     {
/*  856 */       Kit.codeBug();
/*  857 */       localFunctionNode.setMemberExprNode(localAstNode);
/*      */     }
/*      */ 
/*  869 */     localFunctionNode.setSourceName(this.sourceURI);
/*  870 */     localFunctionNode.setBaseLineno(j);
/*  871 */     localFunctionNode.setEndLineno(this.ts.lineno);
/*      */ 
/*  877 */     if (this.compilerEnv.isIdeMode()) {
/*  878 */       localFunctionNode.setParentScope(this.currentScope);
/*      */     }
/*  880 */     return localFunctionNode;
/*      */   }
/*      */ 
/*      */   private AstNode statements(AstNode paramAstNode)
/*      */     throws IOException
/*      */   {
/*  892 */     if ((this.currentToken != 85) && (!this.compilerEnv.isIdeMode()))
/*  893 */       codeBug();
/*  894 */     int i = this.ts.tokenBeg;
/*  895 */     Block localBlock = paramAstNode != null ? paramAstNode : new Block(i);
/*  896 */     localBlock.setLineno(this.ts.lineno);
/*      */     int j;
/*  899 */     while (((j = peekToken()) > 0) && (j != 86)) {
/*  900 */       localBlock.addChild(statement());
/*      */     }
/*  902 */     localBlock.setLength(this.ts.tokenBeg - i);
/*  903 */     return localBlock;
/*      */   }
/*      */ 
/*      */   private AstNode statements() throws IOException {
/*  907 */     return statements(null);
/*      */   }
/*      */ 
/*      */   private ConditionData condition()
/*      */     throws IOException
/*      */   {
/*  920 */     ConditionData localConditionData = new ConditionData(null);
/*      */ 
/*  922 */     if (mustMatchToken(87, "msg.no.paren.cond")) {
/*  923 */       localConditionData.lp = this.ts.tokenBeg;
/*      */     }
/*  925 */     localConditionData.condition = expr();
/*      */ 
/*  927 */     if (mustMatchToken(88, "msg.no.paren.after.cond")) {
/*  928 */       localConditionData.rp = this.ts.tokenBeg;
/*      */     }
/*      */ 
/*  932 */     if ((localConditionData.condition instanceof Assignment)) {
/*  933 */       addStrictWarning("msg.equal.as.assign", "", localConditionData.condition.getPosition(), localConditionData.condition.getLength());
/*      */     }
/*      */ 
/*  937 */     return localConditionData;
/*      */   }
/*      */ 
/*      */   private AstNode statement()
/*      */     throws IOException
/*      */   {
/*  943 */     int i = this.ts.tokenBeg;
/*      */     try {
/*  945 */       AstNode localAstNode = statementHelper();
/*  946 */       if (localAstNode != null) {
/*  947 */         if ((this.compilerEnv.isStrictMode()) && (!localAstNode.hasSideEffects())) {
/*  948 */           int k = localAstNode.getPosition();
/*  949 */           k = Math.max(k, lineBeginningFor(k));
/*  950 */           addStrictWarning((localAstNode instanceof EmptyExpression) ? "msg.extra.trailing.semi" : "msg.no.side.effects", "", k, nodeEnd(localAstNode) - k);
/*      */         }
/*      */ 
/*  955 */         return localAstNode;
/*      */       }
/*      */     }
/*      */     catch (ParserException localParserException)
/*      */     {
/*      */     }
/*      */     while (true)
/*      */     {
/*  963 */       int j = peekTokenOrEOL();
/*  964 */       consumeToken();
/*  965 */       switch (j) {
/*      */       case -1:
/*      */       case 0:
/*      */       case 1:
/*      */       case 82:
/*  970 */         break label142;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  976 */     label142: return new EmptyExpression(i, this.ts.tokenBeg - i);
/*      */   }
/*      */ 
/*      */   private AstNode statementHelper()
/*      */     throws IOException
/*      */   {
/*  983 */     if ((this.currentLabel != null) && (this.currentLabel.getStatement() != null)) {
/*  984 */       this.currentLabel = null;
/*      */     }
/*  986 */     Object localObject = null;
/*  987 */     int i = peekToken(); int j = this.ts.tokenBeg;
/*      */     int k;
/*  989 */     switch (i) {
/*      */     case 112:
/*  991 */       return ifStatement();
/*      */     case 114:
/*  994 */       return switchStatement();
/*      */     case 117:
/*  997 */       return whileLoop();
/*      */     case 118:
/* 1000 */       return doLoop();
/*      */     case 119:
/* 1003 */       return forLoop();
/*      */     case 81:
/* 1006 */       return tryStatement();
/*      */     case 50:
/* 1009 */       localObject = throwStatement();
/* 1010 */       break;
/*      */     case 120:
/* 1013 */       localObject = breakStatement();
/* 1014 */       break;
/*      */     case 121:
/* 1017 */       localObject = continueStatement();
/* 1018 */       break;
/*      */     case 123:
/* 1021 */       if (this.inUseStrictDirective) {
/* 1022 */         reportError("msg.no.with.strict");
/*      */       }
/* 1024 */       return withStatement();
/*      */     case 122:
/*      */     case 154:
/* 1028 */       consumeToken();
/* 1029 */       k = this.ts.lineno;
/* 1030 */       localObject = variables(this.currentToken, this.ts.tokenBeg);
/* 1031 */       ((AstNode)localObject).setLineno(k);
/* 1032 */       break;
/*      */     case 153:
/* 1035 */       localObject = letStatement();
/* 1036 */       if ((!(localObject instanceof VariableDeclaration)) || (peekToken() != 82))
/*      */       {
/* 1039 */         return localObject;
/*      */       }break;
/*      */     case 4:
/*      */     case 72:
/* 1043 */       localObject = returnOrYield(i, false);
/* 1044 */       break;
/*      */     case 160:
/* 1047 */       consumeToken();
/* 1048 */       localObject = new KeywordLiteral(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, i);
/*      */ 
/* 1050 */       ((AstNode)localObject).setLineno(this.ts.lineno);
/* 1051 */       break;
/*      */     case 85:
/* 1054 */       return block();
/*      */     case -1:
/* 1057 */       consumeToken();
/* 1058 */       return makeErrorNode();
/*      */     case 82:
/* 1061 */       consumeToken();
/* 1062 */       j = this.ts.tokenBeg;
/* 1063 */       localObject = new EmptyExpression(j, this.ts.tokenEnd - j);
/* 1064 */       ((AstNode)localObject).setLineno(this.ts.lineno);
/* 1065 */       return localObject;
/*      */     case 109:
/* 1068 */       consumeToken();
/* 1069 */       return function(3);
/*      */     case 116:
/* 1072 */       localObject = defaultXmlNamespace();
/* 1073 */       break;
/*      */     case 39:
/* 1076 */       localObject = nameOrLabel();
/* 1077 */       if (!(localObject instanceof ExpressionStatement))
/*      */       {
/* 1079 */         return localObject;
/*      */       }break;
/*      */     default:
/* 1082 */       k = this.ts.lineno;
/* 1083 */       localObject = new ExpressionStatement(expr(), !insideFunction());
/* 1084 */       ((AstNode)localObject).setLineno(k);
/*      */     }
/*      */ 
/* 1088 */     autoInsertSemicolon((AstNode)localObject);
/* 1089 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void autoInsertSemicolon(AstNode paramAstNode) throws IOException {
/* 1093 */     int i = peekFlaggedToken();
/* 1094 */     int j = paramAstNode.getPosition();
/* 1095 */     switch (i & 0xFFFF)
/*      */     {
/*      */     case 82:
/* 1098 */       consumeToken();
/*      */ 
/* 1100 */       paramAstNode.setLength(this.ts.tokenEnd - j);
/* 1101 */       break;
/*      */     case -1:
/*      */     case 0:
/*      */     case 86:
/* 1106 */       warnMissingSemi(j, nodeEnd(paramAstNode));
/* 1107 */       break;
/*      */     default:
/* 1109 */       if ((i & 0x10000) == 0)
/*      */       {
/* 1111 */         reportError("msg.no.semi.stmt");
/*      */       }
/* 1113 */       else warnMissingSemi(j, nodeEnd(paramAstNode));
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private IfStatement ifStatement()
/*      */     throws IOException
/*      */   {
/* 1122 */     if (this.currentToken != 112) codeBug();
/* 1123 */     consumeToken();
/* 1124 */     int i = this.ts.tokenBeg; int j = this.ts.lineno; int k = -1;
/* 1125 */     ConditionData localConditionData = condition();
/* 1126 */     AstNode localAstNode1 = statement(); AstNode localAstNode2 = null;
/* 1127 */     if (matchToken(113)) {
/* 1128 */       k = this.ts.tokenBeg - i;
/* 1129 */       localAstNode2 = statement();
/*      */     }
/* 1131 */     int m = getNodeEnd(localAstNode2 != null ? localAstNode2 : localAstNode1);
/* 1132 */     IfStatement localIfStatement = new IfStatement(i, m - i);
/* 1133 */     localIfStatement.setCondition(localConditionData.condition);
/* 1134 */     localIfStatement.setParens(localConditionData.lp - i, localConditionData.rp - i);
/* 1135 */     localIfStatement.setThenPart(localAstNode1);
/* 1136 */     localIfStatement.setElsePart(localAstNode2);
/* 1137 */     localIfStatement.setElsePosition(k);
/* 1138 */     localIfStatement.setLineno(j);
/* 1139 */     return localIfStatement;
/*      */   }
/*      */ 
/*      */   private SwitchStatement switchStatement()
/*      */     throws IOException
/*      */   {
/* 1145 */     if (this.currentToken != 114) codeBug();
/* 1146 */     consumeToken();
/* 1147 */     int i = this.ts.tokenBeg;
/*      */ 
/* 1149 */     SwitchStatement localSwitchStatement = new SwitchStatement(i);
/* 1150 */     if (mustMatchToken(87, "msg.no.paren.switch"))
/* 1151 */       localSwitchStatement.setLp(this.ts.tokenBeg - i);
/* 1152 */     localSwitchStatement.setLineno(this.ts.lineno);
/*      */ 
/* 1154 */     AstNode localAstNode1 = expr();
/* 1155 */     localSwitchStatement.setExpression(localAstNode1);
/* 1156 */     enterSwitch(localSwitchStatement);
/*      */     try
/*      */     {
/* 1159 */       if (mustMatchToken(88, "msg.no.paren.after.switch")) {
/* 1160 */         localSwitchStatement.setRp(this.ts.tokenBeg - i);
/*      */       }
/* 1162 */       mustMatchToken(85, "msg.no.brace.switch");
/*      */ 
/* 1164 */       int j = 0;
/*      */       while (true)
/*      */       {
/* 1167 */         int k = nextToken();
/* 1168 */         int m = this.ts.tokenBeg;
/* 1169 */         int n = this.ts.lineno;
/* 1170 */         AstNode localAstNode2 = null;
/* 1171 */         switch (k) {
/*      */         case 86:
/* 1173 */           localSwitchStatement.setLength(this.ts.tokenEnd - i);
/* 1174 */           break;
/*      */         case 115:
/* 1177 */           localAstNode2 = expr();
/* 1178 */           mustMatchToken(103, "msg.no.colon.case");
/* 1179 */           break;
/*      */         case 116:
/* 1182 */           if (j != 0) {
/* 1183 */             reportError("msg.double.switch.default");
/*      */           }
/* 1185 */           j = 1;
/* 1186 */           localAstNode2 = null;
/* 1187 */           mustMatchToken(103, "msg.no.colon.case");
/* 1188 */           break;
/*      */         default:
/* 1191 */           reportError("msg.bad.switch");
/* 1192 */           break;
/*      */         }
/*      */ 
/* 1195 */         SwitchCase localSwitchCase = new SwitchCase(m);
/* 1196 */         localSwitchCase.setExpression(localAstNode2);
/* 1197 */         localSwitchCase.setLength(this.ts.tokenEnd - i);
/* 1198 */         localSwitchCase.setLineno(n);
/*      */ 
/* 1203 */         while (((k = peekToken()) != 86) && (k != 115) && (k != 116) && (k != 0))
/*      */         {
/* 1205 */           localSwitchCase.addStatement(statement());
/*      */         }
/* 1207 */         localSwitchStatement.addCase(localSwitchCase);
/*      */       }
/*      */     } finally {
/* 1210 */       exitSwitch();
/*      */     }
/* 1212 */     return localSwitchStatement;
/*      */   }
/*      */ 
/*      */   private WhileLoop whileLoop()
/*      */     throws IOException
/*      */   {
/* 1218 */     if (this.currentToken != 117) codeBug();
/* 1219 */     consumeToken();
/* 1220 */     int i = this.ts.tokenBeg;
/* 1221 */     WhileLoop localWhileLoop = new WhileLoop(i);
/* 1222 */     localWhileLoop.setLineno(this.ts.lineno);
/* 1223 */     enterLoop(localWhileLoop);
/*      */     try {
/* 1225 */       ConditionData localConditionData = condition();
/* 1226 */       localWhileLoop.setCondition(localConditionData.condition);
/* 1227 */       localWhileLoop.setParens(localConditionData.lp - i, localConditionData.rp - i);
/* 1228 */       AstNode localAstNode = statement();
/* 1229 */       localWhileLoop.setLength(getNodeEnd(localAstNode) - i);
/* 1230 */       localWhileLoop.setBody(localAstNode);
/*      */     } finally {
/* 1232 */       exitLoop();
/*      */     }
/* 1234 */     return localWhileLoop;
/*      */   }
/*      */ 
/*      */   private DoLoop doLoop()
/*      */     throws IOException
/*      */   {
/* 1240 */     if (this.currentToken != 118) codeBug(); consumeToken();
/* 1242 */     int i = this.ts.tokenBeg;
/* 1243 */     DoLoop localDoLoop = new DoLoop(i);
/* 1244 */     localDoLoop.setLineno(this.ts.lineno);
/* 1245 */     enterLoop(localDoLoop);
/*      */     int j;
/*      */     try { AstNode localAstNode = statement();
/* 1248 */       mustMatchToken(117, "msg.no.while.do");
/* 1249 */       localDoLoop.setWhilePosition(this.ts.tokenBeg - i);
/* 1250 */       ConditionData localConditionData = condition();
/* 1251 */       localDoLoop.setCondition(localConditionData.condition);
/* 1252 */       localDoLoop.setParens(localConditionData.lp - i, localConditionData.rp - i);
/* 1253 */       j = getNodeEnd(localAstNode);
/* 1254 */       localDoLoop.setBody(localAstNode);
/*      */     } finally {
/* 1256 */       exitLoop();
/*      */     }
/*      */ 
/* 1261 */     if (matchToken(82)) {
/* 1262 */       j = this.ts.tokenEnd;
/*      */     }
/* 1264 */     localDoLoop.setLength(j - i);
/* 1265 */     return localDoLoop;
/*      */   }
/*      */ 
/*      */   private Loop forLoop()
/*      */     throws IOException
/*      */   {
/* 1271 */     if (this.currentToken != 119) codeBug();
/* 1272 */     consumeToken();
/* 1273 */     int i = this.ts.tokenBeg; int j = this.ts.lineno;
/* 1274 */     boolean bool = false; int k = 0;
/* 1275 */     int m = -1; int n = -1; int i1 = -1; int i2 = -1;
/* 1276 */     AstNode localAstNode = null;
/* 1277 */     Object localObject1 = null;
/* 1278 */     Object localObject2 = null;
/* 1279 */     Object localObject3 = null;
/*      */ 
/* 1281 */     Scope localScope = new Scope();
/* 1282 */     pushScope(localScope);
/*      */     try
/*      */     {
/* 1285 */       if (matchToken(39)) {
/* 1286 */         if ("each".equals(this.ts.getString())) {
/* 1287 */           bool = true;
/* 1288 */           m = this.ts.tokenBeg - i;
/*      */         } else {
/* 1290 */           reportError("msg.no.paren.for");
/*      */         }
/*      */       }
/*      */ 
/* 1294 */       if (mustMatchToken(87, "msg.no.paren.for"))
/* 1295 */         i1 = this.ts.tokenBeg - i;
/* 1296 */       int i3 = peekToken();
/*      */ 
/* 1298 */       localAstNode = forLoopInit(i3);
/*      */ 
/* 1300 */       if (matchToken(52)) {
/* 1301 */         k = 1;
/* 1302 */         n = this.ts.tokenBeg - i;
/* 1303 */         localObject1 = expr();
/*      */       } else {
/* 1305 */         mustMatchToken(82, "msg.no.semi.for");
/* 1306 */         if (peekToken() == 82)
/*      */         {
/* 1308 */           localObject1 = new EmptyExpression(this.ts.tokenBeg, 1);
/* 1309 */           ((AstNode)localObject1).setLineno(this.ts.lineno);
/*      */         } else {
/* 1311 */           localObject1 = expr();
/*      */         }
/*      */ 
/* 1314 */         mustMatchToken(82, "msg.no.semi.for.cond");
/* 1315 */         int i4 = this.ts.tokenEnd;
/* 1316 */         if (peekToken() == 88) {
/* 1317 */           localObject2 = new EmptyExpression(i4, 1);
/* 1318 */           ((AstNode)localObject2).setLineno(this.ts.lineno);
/*      */         } else {
/* 1320 */           localObject2 = expr();
/*      */         }
/*      */       }
/*      */ 
/* 1324 */       if (mustMatchToken(88, "msg.no.paren.for.ctrl"))
/* 1325 */         i2 = this.ts.tokenBeg - i;
/*      */       Object localObject4;
/* 1327 */       if (k != 0) {
/* 1328 */         localObject4 = new ForInLoop(i);
/* 1329 */         if ((localAstNode instanceof VariableDeclaration))
/*      */         {
/* 1331 */           if (((VariableDeclaration)localAstNode).getVariables().size() > 1) {
/* 1332 */             reportError("msg.mult.index");
/*      */           }
/*      */         }
/* 1335 */         ((ForInLoop)localObject4).setIterator(localAstNode);
/* 1336 */         ((ForInLoop)localObject4).setIteratedObject((AstNode)localObject1);
/* 1337 */         ((ForInLoop)localObject4).setInPosition(n);
/* 1338 */         ((ForInLoop)localObject4).setIsForEach(bool);
/* 1339 */         ((ForInLoop)localObject4).setEachPosition(m);
/* 1340 */         localObject3 = localObject4;
/*      */       } else {
/* 1342 */         localObject4 = new ForLoop(i);
/* 1343 */         ((ForLoop)localObject4).setInitializer(localAstNode);
/* 1344 */         ((ForLoop)localObject4).setCondition((AstNode)localObject1);
/* 1345 */         ((ForLoop)localObject4).setIncrement((AstNode)localObject2);
/* 1346 */         localObject3 = localObject4;
/*      */       }
/*      */ 
/* 1350 */       this.currentScope.replaceWith(localObject3);
/* 1351 */       popScope();
/*      */ 
/* 1356 */       enterLoop(localObject3);
/*      */       try {
/* 1358 */         localObject4 = statement();
/* 1359 */         localObject3.setLength(getNodeEnd((AstNode)localObject4) - i);
/* 1360 */         localObject3.setBody((AstNode)localObject4);
/*      */       } finally {
/* 1362 */         exitLoop();
/*      */       }
/*      */ 
/* 1366 */       if (this.currentScope == localScope)
/* 1367 */         popScope();
/*      */     }
/*      */     finally
/*      */     {
/* 1366 */       if (this.currentScope == localScope) {
/* 1367 */         popScope();
/*      */       }
/*      */     }
/* 1370 */     localObject3.setParens(i1, i2);
/* 1371 */     localObject3.setLineno(j);
/* 1372 */     return localObject3;
/*      */   }
/*      */ 
/*      */   private AstNode forLoopInit(int paramInt) throws IOException {
/*      */     try {
/* 1377 */       this.inForInit = true;
/* 1378 */       Object localObject1 = null;
/* 1379 */       if (paramInt == 82) {
/* 1380 */         localObject1 = new EmptyExpression(this.ts.tokenBeg, 1);
/* 1381 */         ((AstNode)localObject1).setLineno(this.ts.lineno);
/* 1382 */       } else if ((paramInt == 122) || (paramInt == 153)) {
/* 1383 */         consumeToken();
/* 1384 */         localObject1 = variables(paramInt, this.ts.tokenBeg);
/*      */       } else {
/* 1386 */         localObject1 = expr();
/* 1387 */         markDestructuring((AstNode)localObject1);
/*      */       }
/* 1389 */       return localObject1;
/*      */     } finally {
/* 1391 */       this.inForInit = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private TryStatement tryStatement()
/*      */     throws IOException
/*      */   {
/* 1398 */     if (this.currentToken != 81) codeBug();
/* 1399 */     consumeToken();
/*      */ 
/* 1402 */     String str1 = getAndResetJsDoc();
/*      */ 
/* 1404 */     int i = this.ts.tokenBeg; int j = this.ts.lineno; int k = -1;
/* 1405 */     if (peekToken() != 85) {
/* 1406 */       reportError("msg.no.brace.try");
/*      */     }
/* 1408 */     AstNode localAstNode1 = statement();
/* 1409 */     int m = getNodeEnd(localAstNode1);
/*      */ 
/* 1411 */     ArrayList localArrayList = null;
/*      */ 
/* 1413 */     int n = 0;
/* 1414 */     int i1 = peekToken();
/* 1415 */     if (i1 == 124)
/* 1416 */       while (matchToken(124)) {
/* 1417 */         int i2 = this.ts.lineno;
/* 1418 */         if (n != 0) {
/* 1419 */           reportError("msg.catch.unreachable");
/*      */         }
/* 1421 */         int i3 = this.ts.tokenBeg; int i4 = -1; int i5 = -1; int i6 = -1;
/* 1422 */         if (mustMatchToken(87, "msg.no.paren.catch")) {
/* 1423 */           i4 = this.ts.tokenBeg;
/*      */         }
/* 1425 */         mustMatchToken(39, "msg.bad.catchcond");
/* 1426 */         Name localName = createNameNode();
/* 1427 */         String str2 = localName.getIdentifier();
/* 1428 */         if ((this.inUseStrictDirective) && (
/* 1429 */           ("eval".equals(str2)) || ("arguments".equals(str2))))
/*      */         {
/* 1432 */           reportError("msg.bad.id.strict", str2);
/*      */         }
/*      */ 
/* 1436 */         AstNode localAstNode3 = null;
/* 1437 */         if (matchToken(112)) {
/* 1438 */           i6 = this.ts.tokenBeg;
/* 1439 */           localAstNode3 = expr();
/*      */         } else {
/* 1441 */           n = 1;
/*      */         }
/*      */ 
/* 1444 */         if (mustMatchToken(88, "msg.bad.catchcond"))
/* 1445 */           i5 = this.ts.tokenBeg;
/* 1446 */         mustMatchToken(85, "msg.no.brace.catchblock");
/*      */ 
/* 1448 */         Block localBlock = (Block)statements();
/* 1449 */         m = getNodeEnd(localBlock);
/* 1450 */         CatchClause localCatchClause = new CatchClause(i3);
/* 1451 */         localCatchClause.setVarName(localName);
/* 1452 */         localCatchClause.setCatchCondition(localAstNode3);
/* 1453 */         localCatchClause.setBody(localBlock);
/* 1454 */         if (i6 != -1) {
/* 1455 */           localCatchClause.setIfPosition(i6 - i3);
/*      */         }
/* 1457 */         localCatchClause.setParens(i4, i5);
/* 1458 */         localCatchClause.setLineno(i2);
/*      */ 
/* 1460 */         if (mustMatchToken(86, "msg.no.brace.after.body"))
/* 1461 */           m = this.ts.tokenEnd;
/* 1462 */         localCatchClause.setLength(m - i3);
/* 1463 */         if (localArrayList == null)
/* 1464 */           localArrayList = new ArrayList();
/* 1465 */         localArrayList.add(localCatchClause);
/*      */       }
/* 1467 */     if (i1 != 125) {
/* 1468 */       mustMatchToken(125, "msg.try.no.catchfinally");
/*      */     }
/*      */ 
/* 1471 */     AstNode localAstNode2 = null;
/* 1472 */     if (matchToken(125)) {
/* 1473 */       k = this.ts.tokenBeg;
/* 1474 */       localAstNode2 = statement();
/* 1475 */       m = getNodeEnd(localAstNode2);
/*      */     }
/*      */ 
/* 1478 */     TryStatement localTryStatement = new TryStatement(i, m - i);
/* 1479 */     localTryStatement.setTryBlock(localAstNode1);
/* 1480 */     localTryStatement.setCatchClauses(localArrayList);
/* 1481 */     localTryStatement.setFinallyBlock(localAstNode2);
/* 1482 */     if (k != -1) {
/* 1483 */       localTryStatement.setFinallyPosition(k - i);
/*      */     }
/* 1485 */     localTryStatement.setLineno(j);
/*      */ 
/* 1487 */     if (str1 != null) {
/* 1488 */       localTryStatement.setJsDoc(str1);
/*      */     }
/*      */ 
/* 1491 */     return localTryStatement;
/*      */   }
/*      */ 
/*      */   private ThrowStatement throwStatement()
/*      */     throws IOException
/*      */   {
/* 1497 */     if (this.currentToken != 50) codeBug();
/* 1498 */     consumeToken();
/* 1499 */     int i = this.ts.tokenBeg; int j = this.ts.lineno;
/* 1500 */     if (peekTokenOrEOL() == 1)
/*      */     {
/* 1503 */       reportError("msg.bad.throw.eol");
/*      */     }
/* 1505 */     AstNode localAstNode = expr();
/* 1506 */     ThrowStatement localThrowStatement = new ThrowStatement(i, getNodeEnd(localAstNode), localAstNode);
/* 1507 */     localThrowStatement.setLineno(j);
/* 1508 */     return localThrowStatement;
/*      */   }
/*      */ 
/*      */   private LabeledStatement matchJumpLabelName()
/*      */     throws IOException
/*      */   {
/* 1520 */     LabeledStatement localLabeledStatement = null;
/*      */ 
/* 1522 */     if (peekTokenOrEOL() == 39) {
/* 1523 */       consumeToken();
/* 1524 */       if (this.labelSet != null) {
/* 1525 */         localLabeledStatement = (LabeledStatement)this.labelSet.get(this.ts.getString());
/*      */       }
/* 1527 */       if (localLabeledStatement == null) {
/* 1528 */         reportError("msg.undef.label");
/*      */       }
/*      */     }
/*      */ 
/* 1532 */     return localLabeledStatement;
/*      */   }
/*      */ 
/*      */   private BreakStatement breakStatement()
/*      */     throws IOException
/*      */   {
/* 1538 */     if (this.currentToken != 120) codeBug();
/* 1539 */     consumeToken();
/* 1540 */     int i = this.ts.lineno; int j = this.ts.tokenBeg; int k = this.ts.tokenEnd;
/* 1541 */     Name localName = null;
/* 1542 */     if (peekTokenOrEOL() == 39) {
/* 1543 */       localName = createNameNode();
/* 1544 */       k = getNodeEnd(localName);
/*      */     }
/*      */ 
/* 1548 */     LabeledStatement localLabeledStatement = matchJumpLabelName();
/*      */ 
/* 1550 */     Object localObject = localLabeledStatement == null ? null : localLabeledStatement.getFirstLabel();
/*      */ 
/* 1552 */     if ((localObject == null) && (localName == null)) {
/* 1553 */       if ((this.loopAndSwitchSet == null) || (this.loopAndSwitchSet.size() == 0)) {
/* 1554 */         if (localName == null)
/* 1555 */           reportError("msg.bad.break", j, k - j);
/*      */       }
/*      */       else {
/* 1558 */         localObject = (Jump)this.loopAndSwitchSet.get(this.loopAndSwitchSet.size() - 1);
/*      */       }
/*      */     }
/*      */ 
/* 1562 */     BreakStatement localBreakStatement = new BreakStatement(j, k - j);
/* 1563 */     localBreakStatement.setBreakLabel(localName);
/*      */ 
/* 1565 */     if (localObject != null)
/* 1566 */       localBreakStatement.setBreakTarget((Jump)localObject);
/* 1567 */     localBreakStatement.setLineno(i);
/* 1568 */     return localBreakStatement;
/*      */   }
/*      */ 
/*      */   private ContinueStatement continueStatement()
/*      */     throws IOException
/*      */   {
/* 1574 */     if (this.currentToken != 121) codeBug();
/* 1575 */     consumeToken();
/* 1576 */     int i = this.ts.lineno; int j = this.ts.tokenBeg; int k = this.ts.tokenEnd;
/* 1577 */     Name localName = null;
/* 1578 */     if (peekTokenOrEOL() == 39) {
/* 1579 */       localName = createNameNode();
/* 1580 */       k = getNodeEnd(localName);
/*      */     }
/*      */ 
/* 1584 */     LabeledStatement localLabeledStatement = matchJumpLabelName();
/* 1585 */     Loop localLoop = null;
/* 1586 */     if ((localLabeledStatement == null) && (localName == null)) {
/* 1587 */       if ((this.loopSet == null) || (this.loopSet.size() == 0))
/* 1588 */         reportError("msg.continue.outside");
/*      */       else
/* 1590 */         localLoop = (Loop)this.loopSet.get(this.loopSet.size() - 1);
/*      */     }
/*      */     else {
/* 1593 */       if ((localLabeledStatement == null) || (!(localLabeledStatement.getStatement() instanceof Loop))) {
/* 1594 */         reportError("msg.continue.nonloop", j, k - j);
/*      */       }
/* 1596 */       localLoop = localLabeledStatement == null ? null : (Loop)localLabeledStatement.getStatement();
/*      */     }
/*      */ 
/* 1599 */     ContinueStatement localContinueStatement = new ContinueStatement(j, k - j);
/* 1600 */     if (localLoop != null)
/* 1601 */       localContinueStatement.setTarget(localLoop);
/* 1602 */     localContinueStatement.setLabel(localName);
/* 1603 */     localContinueStatement.setLineno(i);
/* 1604 */     return localContinueStatement;
/*      */   }
/*      */ 
/*      */   private WithStatement withStatement()
/*      */     throws IOException
/*      */   {
/* 1610 */     if (this.currentToken != 123) codeBug();
/* 1611 */     consumeToken();
/* 1612 */     int i = this.ts.lineno; int j = this.ts.tokenBeg; int k = -1; int m = -1;
/* 1613 */     if (mustMatchToken(87, "msg.no.paren.with")) {
/* 1614 */       k = this.ts.tokenBeg;
/*      */     }
/* 1616 */     AstNode localAstNode1 = expr();
/*      */ 
/* 1618 */     if (mustMatchToken(88, "msg.no.paren.after.with")) {
/* 1619 */       m = this.ts.tokenBeg;
/* 1621 */     }this.nestingOfWith += 1;
/*      */     AstNode localAstNode2;
/*      */     try {
/* 1624 */       localAstNode2 = statement();
/*      */     } finally {
/* 1626 */       this.nestingOfWith -= 1;
/*      */     }
/*      */ 
/* 1629 */     WithStatement localWithStatement = new WithStatement(j, getNodeEnd(localAstNode2) - j);
/* 1630 */     localWithStatement.setJsDoc(getAndResetJsDoc());
/* 1631 */     localWithStatement.setExpression(localAstNode1);
/* 1632 */     localWithStatement.setStatement(localAstNode2);
/* 1633 */     localWithStatement.setParens(k, m);
/* 1634 */     localWithStatement.setLineno(i);
/* 1635 */     return localWithStatement;
/*      */   }
/*      */ 
/*      */   private AstNode letStatement()
/*      */     throws IOException
/*      */   {
/* 1641 */     if (this.currentToken != 153) codeBug();
/* 1642 */     consumeToken();
/* 1643 */     int i = this.ts.lineno; int j = this.ts.tokenBeg;
/*      */     Object localObject;
/* 1645 */     if (peekToken() == 87)
/* 1646 */       localObject = let(true, j);
/*      */     else {
/* 1648 */       localObject = variables(153, j);
/*      */     }
/* 1650 */     ((AstNode)localObject).setLineno(i);
/* 1651 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static final boolean nowAllSet(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1663 */     return ((paramInt1 & paramInt3) != paramInt3) && ((paramInt2 & paramInt3) == paramInt3);
/*      */   }
/*      */ 
/*      */   private AstNode returnOrYield(int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1669 */     if (!insideFunction()) {
/* 1670 */       reportError(paramInt == 4 ? "msg.bad.return" : "msg.bad.yield");
/*      */     }
/*      */ 
/* 1673 */     consumeToken();
/* 1674 */     int i = this.ts.lineno; int j = this.ts.tokenBeg; int k = this.ts.tokenEnd;
/*      */ 
/* 1676 */     AstNode localAstNode = null;
/*      */ 
/* 1678 */     switch (peekTokenOrEOL()) { case -1:
/*      */     case 0:
/*      */     case 1:
/*      */     case 72:
/*      */     case 82:
/*      */     case 84:
/*      */     case 86:
/*      */     case 88:
/* 1681 */       break;
/*      */     default:
/* 1683 */       localAstNode = expr();
/* 1684 */       k = getNodeEnd(localAstNode);
/*      */     }
/*      */ 
/* 1687 */     int m = this.endFlags;
/*      */     Object localObject;
/* 1690 */     if (paramInt == 4) {
/* 1691 */       this.endFlags |= (localAstNode == null ? 2 : 4);
/* 1692 */       localObject = new ReturnStatement(j, k - j, localAstNode);
/*      */ 
/* 1695 */       if (nowAllSet(m, this.endFlags, 6))
/*      */       {
/* 1697 */         addStrictWarning("msg.return.inconsistent", "", j, k - j);
/*      */       }
/*      */     } else { if (!insideFunction())
/* 1700 */         reportError("msg.bad.yield");
/* 1701 */       this.endFlags |= 8;
/* 1702 */       localObject = new Yield(j, k - j, localAstNode);
/* 1703 */       setRequiresActivation();
/* 1704 */       setIsGenerator();
/* 1705 */       if (!paramBoolean) {
/* 1706 */         localObject = new ExpressionStatement((AstNode)localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1711 */     if ((insideFunction()) && (nowAllSet(m, this.endFlags, 12)))
/*      */     {
/* 1714 */       Name localName = ((FunctionNode)this.currentScriptOrFn).getFunctionName();
/* 1715 */       if ((localName == null) || (localName.length() == 0))
/* 1716 */         addError("msg.anon.generator.returns", "");
/*      */       else {
/* 1718 */         addError("msg.generator.returns", localName.getIdentifier());
/*      */       }
/*      */     }
/* 1721 */     ((AstNode)localObject).setLineno(i);
/* 1722 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode block()
/*      */     throws IOException
/*      */   {
/* 1728 */     if (this.currentToken != 85) codeBug();
/* 1729 */     consumeToken();
/* 1730 */     int i = this.ts.tokenBeg;
/* 1731 */     Scope localScope1 = new Scope(i);
/* 1732 */     localScope1.setLineno(this.ts.lineno);
/* 1733 */     pushScope(localScope1);
/*      */     try {
/* 1735 */       statements(localScope1);
/* 1736 */       mustMatchToken(86, "msg.no.brace.block");
/* 1737 */       localScope1.setLength(this.ts.tokenEnd - i);
/* 1738 */       return localScope1;
/*      */     } finally {
/* 1740 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private AstNode defaultXmlNamespace()
/*      */     throws IOException
/*      */   {
/* 1747 */     if (this.currentToken != 116) codeBug();
/* 1748 */     consumeToken();
/* 1749 */     mustHaveXML();
/* 1750 */     setRequiresActivation();
/* 1751 */     int i = this.ts.lineno; int j = this.ts.tokenBeg;
/*      */ 
/* 1753 */     if ((!matchToken(39)) || (!"xml".equals(this.ts.getString()))) {
/* 1754 */       reportError("msg.bad.namespace");
/*      */     }
/* 1756 */     if ((!matchToken(39)) || (!"namespace".equals(this.ts.getString()))) {
/* 1757 */       reportError("msg.bad.namespace");
/*      */     }
/* 1759 */     if (!matchToken(90)) {
/* 1760 */       reportError("msg.bad.namespace");
/*      */     }
/*      */ 
/* 1763 */     AstNode localAstNode = expr();
/* 1764 */     UnaryExpression localUnaryExpression = new UnaryExpression(j, getNodeEnd(localAstNode) - j);
/* 1765 */     localUnaryExpression.setOperator(74);
/* 1766 */     localUnaryExpression.setOperand(localAstNode);
/* 1767 */     localUnaryExpression.setLineno(i);
/*      */ 
/* 1769 */     ExpressionStatement localExpressionStatement = new ExpressionStatement(localUnaryExpression, true);
/* 1770 */     return localExpressionStatement;
/*      */   }
/*      */ 
/*      */   private void recordLabel(Label paramLabel, LabeledStatement paramLabeledStatement)
/*      */     throws IOException
/*      */   {
/* 1777 */     if (peekToken() != 103) codeBug();
/* 1778 */     consumeToken();
/* 1779 */     String str = paramLabel.getName();
/* 1780 */     if (this.labelSet == null) {
/* 1781 */       this.labelSet = new HashMap();
/*      */     } else {
/* 1783 */       LabeledStatement localLabeledStatement = (LabeledStatement)this.labelSet.get(str);
/* 1784 */       if (localLabeledStatement != null) {
/* 1785 */         if (this.compilerEnv.isIdeMode()) {
/* 1786 */           Label localLabel = localLabeledStatement.getLabelByName(str);
/* 1787 */           reportError("msg.dup.label", localLabel.getAbsolutePosition(), localLabel.getLength());
/*      */         }
/*      */ 
/* 1790 */         reportError("msg.dup.label", paramLabel.getPosition(), paramLabel.getLength());
/*      */       }
/*      */     }
/*      */ 
/* 1794 */     paramLabeledStatement.addLabel(paramLabel);
/* 1795 */     this.labelSet.put(str, paramLabeledStatement);
/*      */   }
/*      */ 
/*      */   private AstNode nameOrLabel()
/*      */     throws IOException
/*      */   {
/* 1807 */     if (this.currentToken != 39) throw codeBug();
/* 1808 */     int i = this.ts.tokenBeg;
/*      */ 
/* 1811 */     this.currentFlaggedToken |= 131072;
/* 1812 */     AstNode localAstNode = expr();
/*      */ 
/* 1814 */     if (localAstNode.getType() != 130) {
/* 1815 */       localObject1 = new ExpressionStatement(localAstNode, !insideFunction());
/* 1816 */       ((AstNode)localObject1).lineno = localAstNode.lineno;
/* 1817 */       return localObject1;
/*      */     }
/*      */ 
/* 1820 */     Object localObject1 = new LabeledStatement(i);
/* 1821 */     recordLabel((Label)localAstNode, (LabeledStatement)localObject1);
/* 1822 */     ((LabeledStatement)localObject1).setLineno(this.ts.lineno);
/*      */ 
/* 1824 */     Object localObject2 = null;
/* 1825 */     while (peekToken() == 39) {
/* 1826 */       this.currentFlaggedToken |= 131072;
/* 1827 */       localAstNode = expr();
/* 1828 */       if (localAstNode.getType() != 130) {
/* 1829 */         localObject2 = new ExpressionStatement(localAstNode, !insideFunction());
/* 1830 */         autoInsertSemicolon((AstNode)localObject2);
/* 1831 */         break;
/*      */       }
/* 1833 */       recordLabel((Label)localAstNode, (LabeledStatement)localObject1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1838 */       this.currentLabel = ((LabeledStatement)localObject1);
/* 1839 */       if (localObject2 == null)
/* 1840 */         localObject2 = statementHelper();
/*      */     }
/*      */     finally
/*      */     {
/*      */       Iterator localIterator1;
/*      */       Label localLabel1;
/* 1843 */       this.currentLabel = null;
/*      */ 
/* 1845 */       for (Label localLabel2 : ((LabeledStatement)localObject1).getLabels()) {
/* 1846 */         this.labelSet.remove(localLabel2.getName());
/*      */       }
/*      */     }
/*      */ 
/* 1850 */     ((LabeledStatement)localObject1).setLength(getNodeEnd((AstNode)localObject2) - i);
/* 1851 */     ((LabeledStatement)localObject1).setStatement((AstNode)localObject2);
/* 1852 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private VariableDeclaration variables(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1869 */     VariableDeclaration localVariableDeclaration = new VariableDeclaration(paramInt2);
/* 1870 */     localVariableDeclaration.setType(paramInt1);
/* 1871 */     localVariableDeclaration.setLineno(this.ts.lineno);
/* 1872 */     String str1 = getAndResetJsDoc();
/* 1873 */     if (str1 != null) {
/* 1874 */       localVariableDeclaration.setJsDoc(str1);
/*      */     }
/*      */ 
/*      */     int i;
/*      */     while (true)
/*      */     {
/* 1880 */       AstNode localAstNode1 = null;
/* 1881 */       Name localName = null;
/* 1882 */       int j = peekToken(); int k = this.ts.tokenBeg;
/* 1883 */       i = this.ts.tokenEnd;
/*      */ 
/* 1885 */       if ((j == 83) || (j == 85))
/*      */       {
/* 1887 */         localAstNode1 = destructuringPrimaryExpr();
/* 1888 */         i = getNodeEnd(localAstNode1);
/* 1889 */         if (!(localAstNode1 instanceof DestructuringForm))
/* 1890 */           reportError("msg.bad.assign.left", k, i - k);
/* 1891 */         markDestructuring(localAstNode1);
/*      */       }
/*      */       else {
/* 1894 */         mustMatchToken(39, "msg.bad.var");
/* 1895 */         localName = createNameNode();
/* 1896 */         localName.setLineno(this.ts.getLineno());
/* 1897 */         if (this.inUseStrictDirective) {
/* 1898 */           String str2 = this.ts.getString();
/* 1899 */           if (("eval".equals(str2)) || ("arguments".equals(this.ts.getString())))
/*      */           {
/* 1901 */             reportError("msg.bad.id.strict", str2);
/*      */           }
/*      */         }
/* 1904 */         defineSymbol(paramInt1, this.ts.getString(), this.inForInit);
/*      */       }
/*      */ 
/* 1907 */       int m = this.ts.lineno;
/*      */ 
/* 1909 */       String str3 = getAndResetJsDoc();
/*      */ 
/* 1911 */       AstNode localAstNode2 = null;
/* 1912 */       if (matchToken(90)) {
/* 1913 */         localAstNode2 = assignExpr();
/* 1914 */         i = getNodeEnd(localAstNode2);
/*      */       }
/*      */ 
/* 1917 */       VariableInitializer localVariableInitializer = new VariableInitializer(k, i - k);
/* 1918 */       if (localAstNode1 != null) {
/* 1919 */         if ((localAstNode2 == null) && (!this.inForInit)) {
/* 1920 */           reportError("msg.destruct.assign.no.init");
/*      */         }
/* 1922 */         localVariableInitializer.setTarget(localAstNode1);
/*      */       } else {
/* 1924 */         localVariableInitializer.setTarget(localName);
/*      */       }
/* 1926 */       localVariableInitializer.setInitializer(localAstNode2);
/* 1927 */       localVariableInitializer.setType(paramInt1);
/* 1928 */       localVariableInitializer.setJsDoc(str3);
/* 1929 */       localVariableInitializer.setLineno(m);
/* 1930 */       localVariableDeclaration.addVariable(localVariableInitializer);
/*      */ 
/* 1932 */       if (!matchToken(89))
/*      */         break;
/*      */     }
/* 1935 */     localVariableDeclaration.setLength(i - paramInt2);
/* 1936 */     return localVariableDeclaration;
/*      */   }
/*      */ 
/*      */   private AstNode let(boolean paramBoolean, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1943 */     LetNode localLetNode = new LetNode(paramInt);
/* 1944 */     localLetNode.setLineno(this.ts.lineno);
/* 1945 */     if (mustMatchToken(87, "msg.no.paren.after.let"))
/* 1946 */       localLetNode.setLp(this.ts.tokenBeg - paramInt);
/* 1947 */     pushScope(localLetNode);
/*      */     try {
/* 1949 */       VariableDeclaration localVariableDeclaration = variables(153, this.ts.tokenBeg);
/* 1950 */       localLetNode.setVariables(localVariableDeclaration);
/* 1951 */       if (mustMatchToken(88, "msg.no.paren.let"))
/* 1952 */         localLetNode.setRp(this.ts.tokenBeg - paramInt);
/*      */       Object localObject1;
/* 1954 */       if ((paramBoolean) && (peekToken() == 85))
/*      */       {
/* 1956 */         consumeToken();
/* 1957 */         int i = this.ts.tokenBeg;
/* 1958 */         localObject1 = statements();
/* 1959 */         mustMatchToken(86, "msg.no.curly.let");
/* 1960 */         ((AstNode)localObject1).setLength(this.ts.tokenEnd - i);
/* 1961 */         localLetNode.setLength(this.ts.tokenEnd - paramInt);
/* 1962 */         localLetNode.setBody((AstNode)localObject1);
/* 1963 */         localLetNode.setType(153);
/*      */       }
/*      */       else {
/* 1966 */         AstNode localAstNode = expr();
/* 1967 */         localLetNode.setLength(getNodeEnd(localAstNode) - paramInt);
/* 1968 */         localLetNode.setBody(localAstNode);
/* 1969 */         if (paramBoolean)
/*      */         {
/* 1971 */           localObject1 = new ExpressionStatement(localLetNode, !insideFunction());
/*      */ 
/* 1973 */           ((ExpressionStatement)localObject1).setLineno(localLetNode.getLineno());
/* 1974 */           return localObject1;
/*      */         }
/*      */       }
/*      */     } finally {
/* 1978 */       popScope();
/*      */     }
/* 1980 */     return localLetNode;
/*      */   }
/*      */ 
/*      */   void defineSymbol(int paramInt, String paramString) {
/* 1984 */     defineSymbol(paramInt, paramString, false);
/*      */   }
/*      */ 
/*      */   void defineSymbol(int paramInt, String paramString, boolean paramBoolean) {
/* 1988 */     if (paramString == null) {
/* 1989 */       if (this.compilerEnv.isIdeMode()) {
/* 1990 */         return;
/*      */       }
/* 1992 */       codeBug();
/*      */     }
/*      */ 
/* 1995 */     Scope localScope = this.currentScope.getDefiningScope(paramString);
/* 1996 */     Object localObject = localScope != null ? localScope.getSymbol(paramString) : null;
/*      */ 
/* 1999 */     int i = localObject != null ? localObject.getDeclType() : -1;
/* 2000 */     if ((localObject != null) && ((i == 154) || (paramInt == 154) || ((localScope == this.currentScope) && (i == 153))))
/*      */     {
/* 2005 */       addError(i == 109 ? "msg.fn.redecl" : i == 122 ? "msg.var.redecl" : i == 153 ? "msg.let.redecl" : i == 154 ? "msg.const.redecl" : "msg.parm.redecl", paramString);
/*      */ 
/* 2010 */       return;
/*      */     }
/* 2012 */     switch (paramInt) {
/*      */     case 153:
/* 2014 */       if ((!paramBoolean) && ((this.currentScope.getType() == 112) || ((this.currentScope instanceof Loop))))
/*      */       {
/* 2017 */         addError("msg.let.decl.not.in.block");
/* 2018 */         return;
/*      */       }
/* 2020 */       this.currentScope.putSymbol(new Symbol(paramInt, paramString));
/* 2021 */       return;
/*      */     case 109:
/*      */     case 122:
/*      */     case 154:
/* 2026 */       if (localObject != null) {
/* 2027 */         if (i == 122)
/* 2028 */           addStrictWarning("msg.var.redecl", paramString);
/* 2029 */         else if (i == 87)
/* 2030 */           addStrictWarning("msg.var.hides.arg", paramString);
/*      */       }
/*      */       else {
/* 2033 */         this.currentScriptOrFn.putSymbol(new Symbol(paramInt, paramString));
/*      */       }
/* 2035 */       return;
/*      */     case 87:
/* 2038 */       if (localObject != null)
/*      */       {
/* 2041 */         addWarning("msg.dup.parms", paramString);
/*      */       }
/* 2043 */       this.currentScriptOrFn.putSymbol(new Symbol(paramInt, paramString));
/* 2044 */       return;
/*      */     }
/*      */ 
/* 2047 */     throw codeBug();
/*      */   }
/*      */ 
/*      */   private AstNode expr()
/*      */     throws IOException
/*      */   {
/* 2054 */     Object localObject = assignExpr();
/* 2055 */     int i = ((AstNode)localObject).getPosition();
/* 2056 */     while (matchToken(89)) {
/* 2057 */       int j = this.ts.lineno;
/* 2058 */       int k = this.ts.tokenBeg;
/* 2059 */       if ((this.compilerEnv.isStrictMode()) && (!((AstNode)localObject).hasSideEffects())) {
/* 2060 */         addStrictWarning("msg.no.side.effects", "", i, nodeEnd((AstNode)localObject) - i);
/*      */       }
/* 2062 */       if (peekToken() == 72)
/* 2063 */         reportError("msg.yield.parenthesized");
/* 2064 */       localObject = new InfixExpression(89, (AstNode)localObject, assignExpr(), k);
/* 2065 */       ((AstNode)localObject).setLineno(j);
/*      */     }
/* 2067 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode assignExpr()
/*      */     throws IOException
/*      */   {
/* 2073 */     int i = peekToken();
/* 2074 */     if (i == 72) {
/* 2075 */       return returnOrYield(i, true);
/*      */     }
/* 2077 */     Object localObject = condExpr();
/* 2078 */     i = peekToken();
/* 2079 */     if ((90 <= i) && (i <= 101)) {
/* 2080 */       consumeToken();
/*      */ 
/* 2083 */       String str = getAndResetJsDoc();
/*      */ 
/* 2085 */       markDestructuring((AstNode)localObject);
/* 2086 */       int j = this.ts.tokenBeg;
/* 2087 */       int k = this.ts.getLineno();
/*      */ 
/* 2089 */       localObject = new Assignment(i, (AstNode)localObject, assignExpr(), j);
/*      */ 
/* 2091 */       ((AstNode)localObject).setLineno(k);
/* 2092 */       if (str != null)
/* 2093 */         ((AstNode)localObject).setJsDoc(str);
/*      */     }
/* 2095 */     else if ((i == 82) && (((AstNode)localObject).getType() == 33))
/*      */     {
/* 2098 */       if (this.currentJsDocComment != null) {
/* 2099 */         ((AstNode)localObject).setJsDoc(getAndResetJsDoc());
/*      */       }
/*      */     }
/* 2102 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode condExpr()
/*      */     throws IOException
/*      */   {
/* 2108 */     Object localObject = orExpr();
/* 2109 */     if (matchToken(102)) {
/* 2110 */       int i = this.ts.lineno;
/* 2111 */       int j = this.ts.tokenBeg; int k = -1;
/* 2112 */       AstNode localAstNode1 = assignExpr();
/* 2113 */       if (mustMatchToken(103, "msg.no.colon.cond"))
/* 2114 */         k = this.ts.tokenBeg;
/* 2115 */       AstNode localAstNode2 = assignExpr();
/* 2116 */       int m = ((AstNode)localObject).getPosition(); int n = getNodeEnd(localAstNode2) - m;
/* 2117 */       ConditionalExpression localConditionalExpression = new ConditionalExpression(m, n);
/* 2118 */       localConditionalExpression.setLineno(i);
/* 2119 */       localConditionalExpression.setTestExpression((AstNode)localObject);
/* 2120 */       localConditionalExpression.setTrueExpression(localAstNode1);
/* 2121 */       localConditionalExpression.setFalseExpression(localAstNode2);
/* 2122 */       localConditionalExpression.setQuestionMarkPosition(j - m);
/* 2123 */       localConditionalExpression.setColonPosition(k - m);
/* 2124 */       localObject = localConditionalExpression;
/*      */     }
/* 2126 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode orExpr()
/*      */     throws IOException
/*      */   {
/* 2132 */     Object localObject = andExpr();
/* 2133 */     if (matchToken(104)) {
/* 2134 */       LinkedList localLinkedList1 = new LinkedList();
/* 2135 */       LinkedList localLinkedList2 = new LinkedList();
/* 2136 */       LinkedList localLinkedList3 = new LinkedList();
/* 2137 */       localLinkedList1.addLast(localObject);
/* 2138 */       localLinkedList2.addLast(Integer.valueOf(this.ts.tokenBeg));
/* 2139 */       localLinkedList3.addLast(Integer.valueOf(this.ts.lineno));
/*      */       do {
/* 2141 */         localLinkedList1.addLast(andExpr());
/* 2142 */         localLinkedList2.addLast(Integer.valueOf(this.ts.tokenBeg));
/* 2143 */         localLinkedList3.addLast(Integer.valueOf(this.ts.lineno));
/* 2144 */       }while (matchToken(104));
/* 2145 */       LinkedList localLinkedList4 = new LinkedList();
/* 2146 */       LinkedList localLinkedList5 = new LinkedList();
/* 2147 */       LinkedList localLinkedList6 = new LinkedList();
/* 2148 */       while (localLinkedList1.size() > 1) {
/*      */         do {
/* 2150 */           AstNode localAstNode1 = (AstNode)localLinkedList1.pollLast();
/* 2151 */           int i = ((Integer)localLinkedList2.pollLast()).intValue();
/* 2152 */           int j = ((Integer)localLinkedList3.pollLast()).intValue();
/* 2153 */           AstNode localAstNode2 = (AstNode)localLinkedList1.pollLast();
/* 2154 */           int k = ((Integer)localLinkedList2.pollLast()).intValue();
/* 2155 */           int m = ((Integer)localLinkedList3.pollLast()).intValue();
/* 2156 */           localObject = new InfixExpression(104, localAstNode2, localAstNode1, k);
/* 2157 */           ((AstNode)localObject).setLineno(m);
/* 2158 */           localLinkedList4.addFirst(localObject);
/* 2159 */           localLinkedList5.addFirst(Integer.valueOf(k));
/* 2160 */           localLinkedList6.addFirst(Integer.valueOf(m));
/* 2161 */         }while (localLinkedList1.size() > 1);
/* 2162 */         localLinkedList1.addAll(localLinkedList4);
/* 2163 */         localLinkedList4.clear();
/* 2164 */         localLinkedList2.addAll(localLinkedList5);
/* 2165 */         localLinkedList5.clear();
/* 2166 */         localLinkedList3.addAll(localLinkedList6);
/* 2167 */         localLinkedList6.clear();
/*      */       }
/* 2169 */       localObject = (AstNode)localLinkedList1.pollFirst();
/*      */     }
/* 2171 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode andExpr()
/*      */     throws IOException
/*      */   {
/* 2177 */     Object localObject = bitOrExpr();
/* 2178 */     if (matchToken(105)) {
/* 2179 */       int i = this.ts.tokenBeg;
/* 2180 */       int j = this.ts.lineno;
/* 2181 */       localObject = new InfixExpression(105, (AstNode)localObject, andExpr(), i);
/* 2182 */       ((AstNode)localObject).setLineno(j);
/*      */     }
/* 2184 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode bitOrExpr()
/*      */     throws IOException
/*      */   {
/* 2190 */     Object localObject = bitXorExpr();
/* 2191 */     while (matchToken(9)) {
/* 2192 */       int i = this.ts.tokenBeg;
/* 2193 */       int j = this.ts.lineno;
/* 2194 */       localObject = new InfixExpression(9, (AstNode)localObject, bitXorExpr(), i);
/* 2195 */       ((AstNode)localObject).setLineno(j);
/*      */     }
/* 2197 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode bitXorExpr()
/*      */     throws IOException
/*      */   {
/* 2203 */     Object localObject = bitAndExpr();
/* 2204 */     while (matchToken(10)) {
/* 2205 */       int i = this.ts.tokenBeg;
/* 2206 */       int j = this.ts.lineno;
/* 2207 */       localObject = new InfixExpression(10, (AstNode)localObject, bitAndExpr(), i);
/* 2208 */       ((AstNode)localObject).setLineno(j);
/*      */     }
/* 2210 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode bitAndExpr()
/*      */     throws IOException
/*      */   {
/* 2216 */     Object localObject = eqExpr();
/* 2217 */     while (matchToken(11)) {
/* 2218 */       int i = this.ts.tokenBeg;
/* 2219 */       int j = this.ts.lineno;
/* 2220 */       localObject = new InfixExpression(11, (AstNode)localObject, eqExpr(), i);
/* 2221 */       ((AstNode)localObject).setLineno(j);
/*      */     }
/* 2223 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode eqExpr()
/*      */     throws IOException
/*      */   {
/* 2229 */     Object localObject = relExpr();
/*      */     while (true) {
/* 2231 */       int i = peekToken(); int j = this.ts.tokenBeg;
/* 2232 */       int k = this.ts.lineno;
/* 2233 */       switch (i) {
/*      */       case 12:
/*      */       case 13:
/*      */       case 46:
/*      */       case 47:
/* 2238 */         consumeToken();
/* 2239 */         int m = i;
/* 2240 */         if (this.compilerEnv.getLanguageVersion() == 120)
/*      */         {
/* 2242 */           if (i == 12)
/* 2243 */             m = 46;
/* 2244 */           else if (i == 13)
/* 2245 */             m = 47;
/*      */         }
/* 2247 */         localObject = new InfixExpression(m, (AstNode)localObject, relExpr(), j);
/* 2248 */         ((AstNode)localObject).setLineno(k);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2253 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode relExpr()
/*      */     throws IOException
/*      */   {
/* 2259 */     Object localObject = shiftExpr();
/*      */     while (true) {
/* 2261 */       int i = peekToken(); int j = this.ts.tokenBeg;
/* 2262 */       int k = this.ts.lineno;
/* 2263 */       switch (i) {
/*      */       case 52:
/* 2265 */         if (this.inForInit) {
/*      */           break;
/*      */         }
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 53:
/* 2273 */         consumeToken();
/* 2274 */         localObject = new InfixExpression(i, (AstNode)localObject, shiftExpr(), j);
/* 2275 */         ((AstNode)localObject).setLineno(k);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2280 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode shiftExpr()
/*      */     throws IOException
/*      */   {
/* 2286 */     Object localObject = addExpr();
/*      */     while (true) {
/* 2288 */       int i = peekToken(); int j = this.ts.tokenBeg;
/* 2289 */       int k = this.ts.lineno;
/* 2290 */       switch (i) {
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/* 2294 */         consumeToken();
/* 2295 */         localObject = new InfixExpression(i, (AstNode)localObject, addExpr(), j);
/* 2296 */         ((AstNode)localObject).setLineno(k);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2301 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode addExpr()
/*      */     throws IOException
/*      */   {
/* 2307 */     Object localObject = mulExpr();
/*      */     while (true) {
/* 2309 */       int i = peekToken(); int j = this.ts.tokenBeg;
/* 2310 */       if ((i != 21) && (i != 22)) break;
/* 2311 */       consumeToken();
/* 2312 */       int k = this.ts.lineno;
/* 2313 */       localObject = new InfixExpression(i, (AstNode)localObject, mulExpr(), j);
/* 2314 */       ((AstNode)localObject).setLineno(k);
/*      */     }
/*      */ 
/* 2319 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode mulExpr()
/*      */     throws IOException
/*      */   {
/* 2325 */     Object localObject = unaryExpr();
/*      */     while (true) {
/* 2327 */       int i = peekToken(); int j = this.ts.tokenBeg;
/* 2328 */       switch (i) {
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/* 2332 */         consumeToken();
/* 2333 */         int k = this.ts.lineno;
/* 2334 */         localObject = new InfixExpression(i, (AstNode)localObject, unaryExpr(), j);
/* 2335 */         ((AstNode)localObject).setLineno(k);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2340 */     return localObject;
/*      */   }
/*      */ 
/*      */   private AstNode unaryExpr()
/*      */     throws IOException
/*      */   {
/* 2347 */     int i = peekToken();
/* 2348 */     int j = this.ts.lineno;
/*      */     UnaryExpression localUnaryExpression1;
/* 2350 */     switch (i) {
/*      */     case 26:
/*      */     case 27:
/*      */     case 32:
/*      */     case 126:
/* 2355 */       consumeToken();
/* 2356 */       localUnaryExpression1 = new UnaryExpression(i, this.ts.tokenBeg, unaryExpr());
/* 2357 */       localUnaryExpression1.setLineno(j);
/* 2358 */       return localUnaryExpression1;
/*      */     case 21:
/* 2361 */       consumeToken();
/*      */ 
/* 2363 */       localUnaryExpression1 = new UnaryExpression(28, this.ts.tokenBeg, unaryExpr());
/* 2364 */       localUnaryExpression1.setLineno(j);
/* 2365 */       return localUnaryExpression1;
/*      */     case 22:
/* 2368 */       consumeToken();
/*      */ 
/* 2370 */       localUnaryExpression1 = new UnaryExpression(29, this.ts.tokenBeg, unaryExpr());
/* 2371 */       localUnaryExpression1.setLineno(j);
/* 2372 */       return localUnaryExpression1;
/*      */     case 106:
/*      */     case 107:
/* 2376 */       consumeToken();
/* 2377 */       UnaryExpression localUnaryExpression2 = new UnaryExpression(i, this.ts.tokenBeg, memberExpr(true));
/*      */ 
/* 2379 */       localUnaryExpression2.setLineno(j);
/* 2380 */       checkBadIncDec(localUnaryExpression2);
/* 2381 */       return localUnaryExpression2;
/*      */     case 31:
/* 2384 */       consumeToken();
/* 2385 */       localUnaryExpression1 = new UnaryExpression(i, this.ts.tokenBeg, unaryExpr());
/* 2386 */       localUnaryExpression1.setLineno(j);
/* 2387 */       return localUnaryExpression1;
/*      */     case -1:
/* 2390 */       consumeToken();
/* 2391 */       return makeErrorNode();
/*      */     case 14:
/* 2395 */       if (this.compilerEnv.isXmlAvailable()) {
/* 2396 */         consumeToken();
/* 2397 */         return memberExprTail(true, xmlInitializer());
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 2402 */     AstNode localAstNode = memberExpr(true);
/*      */ 
/* 2404 */     i = peekTokenOrEOL();
/* 2405 */     if ((i != 106) && (i != 107)) {
/* 2406 */       return localAstNode;
/*      */     }
/* 2408 */     consumeToken();
/* 2409 */     UnaryExpression localUnaryExpression3 = new UnaryExpression(i, this.ts.tokenBeg, localAstNode, true);
/*      */ 
/* 2411 */     localUnaryExpression3.setLineno(j);
/* 2412 */     checkBadIncDec(localUnaryExpression3);
/* 2413 */     return localUnaryExpression3;
/*      */   }
/*      */ 
/*      */   private AstNode xmlInitializer()
/*      */     throws IOException
/*      */   {
/* 2420 */     if (this.currentToken != 14) codeBug();
/* 2421 */     int i = this.ts.tokenBeg; int j = this.ts.getFirstXMLToken();
/* 2422 */     if ((j != 145) && (j != 148)) {
/* 2423 */       reportError("msg.syntax");
/* 2424 */       return makeErrorNode();
/*      */     }
/*      */ 
/* 2427 */     XmlLiteral localXmlLiteral = new XmlLiteral(i);
/* 2428 */     localXmlLiteral.setLineno(this.ts.lineno);
/*      */ 
/* 2430 */     for (; ; j = this.ts.getNextXMLToken())
/* 2431 */       switch (j) {
/*      */       case 145:
/* 2433 */         localXmlLiteral.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
/* 2434 */         mustMatchToken(85, "msg.syntax");
/* 2435 */         int k = this.ts.tokenBeg;
/* 2436 */         AstNode localAstNode = peekToken() == 86 ? new EmptyExpression(k, this.ts.tokenEnd - k) : expr();
/*      */ 
/* 2439 */         mustMatchToken(86, "msg.syntax");
/* 2440 */         XmlExpression localXmlExpression = new XmlExpression(k, localAstNode);
/* 2441 */         localXmlExpression.setIsXmlAttribute(this.ts.isXMLAttribute());
/* 2442 */         localXmlExpression.setLength(this.ts.tokenEnd - k);
/* 2443 */         localXmlLiteral.addFragment(localXmlExpression);
/* 2444 */         break;
/*      */       case 148:
/* 2447 */         localXmlLiteral.addFragment(new XmlString(this.ts.tokenBeg, this.ts.getString()));
/* 2448 */         return localXmlLiteral;
/*      */       default:
/* 2451 */         reportError("msg.syntax");
/* 2452 */         return makeErrorNode();
/*      */       }
/*      */   }
/*      */ 
/*      */   private List<AstNode> argumentList()
/*      */     throws IOException
/*      */   {
/* 2460 */     if (matchToken(88)) {
/* 2461 */       return null;
/*      */     }
/* 2463 */     ArrayList localArrayList = new ArrayList();
/* 2464 */     boolean bool = this.inForInit;
/* 2465 */     this.inForInit = false;
/*      */     try {
/*      */       do {
/* 2468 */         if (peekToken() == 72)
/* 2469 */           reportError("msg.yield.parenthesized");
/* 2470 */         localArrayList.add(assignExpr());
/* 2471 */       }while (matchToken(89));
/*      */     } finally {
/* 2473 */       this.inForInit = bool;
/*      */     }
/*      */ 
/* 2476 */     mustMatchToken(88, "msg.no.paren.arg");
/* 2477 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private AstNode memberExpr(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 2488 */     int i = peekToken(); int j = this.ts.lineno;
/*      */     Object localObject1;
/* 2491 */     if (i != 30) {
/* 2492 */       localObject1 = primaryExpr();
/*      */     } else {
/* 2494 */       consumeToken();
/* 2495 */       int k = this.ts.tokenBeg;
/* 2496 */       NewExpression localNewExpression = new NewExpression(k);
/*      */ 
/* 2498 */       AstNode localAstNode2 = memberExpr(false);
/* 2499 */       int m = getNodeEnd(localAstNode2);
/* 2500 */       localNewExpression.setTarget(localAstNode2);
/*      */ 
/* 2502 */       int n = -1;
/*      */       Object localObject2;
/* 2503 */       if (matchToken(87)) {
/* 2504 */         n = this.ts.tokenBeg;
/* 2505 */         localObject2 = argumentList();
/* 2506 */         if ((localObject2 != null) && (((List)localObject2).size() > 65536))
/* 2507 */           reportError("msg.too.many.constructor.args");
/* 2508 */         int i1 = this.ts.tokenBeg;
/* 2509 */         m = this.ts.tokenEnd;
/* 2510 */         if (localObject2 != null)
/* 2511 */           localNewExpression.setArguments((List)localObject2);
/* 2512 */         localNewExpression.setParens(n - k, i1 - k);
/*      */       }
/*      */ 
/* 2519 */       if (matchToken(85)) {
/* 2520 */         localObject2 = objectLiteral();
/* 2521 */         m = getNodeEnd((AstNode)localObject2);
/* 2522 */         localNewExpression.setInitializer((ObjectLiteral)localObject2);
/*      */       }
/* 2524 */       localNewExpression.setLength(m - k);
/* 2525 */       localObject1 = localNewExpression;
/*      */     }
/* 2527 */     ((AstNode)localObject1).setLineno(j);
/* 2528 */     AstNode localAstNode1 = memberExprTail(paramBoolean, (AstNode)localObject1);
/* 2529 */     return localAstNode1;
/*      */   }
/*      */ 
/*      */   private AstNode memberExprTail(boolean paramBoolean, AstNode paramAstNode)
/*      */     throws IOException
/*      */   {
/* 2543 */     if (paramAstNode == null) codeBug();
/* 2544 */     int i = paramAstNode.getPosition();
/*      */     while (true)
/*      */     {
/* 2548 */       int k = peekToken();
/*      */       int j;
/*      */       int i1;
/* 2549 */       switch (k) {
/*      */       case 108:
/*      */       case 143:
/* 2552 */         j = this.ts.lineno;
/* 2553 */         paramAstNode = propertyAccess(k, paramAstNode);
/* 2554 */         paramAstNode.setLineno(j);
/* 2555 */         break;
/*      */       case 146:
/* 2558 */         consumeToken();
/* 2559 */         int m = this.ts.tokenBeg; int n = -1;
/* 2560 */         j = this.ts.lineno;
/* 2561 */         mustHaveXML();
/* 2562 */         setRequiresActivation();
/* 2563 */         AstNode localAstNode1 = expr();
/* 2564 */         i1 = getNodeEnd(localAstNode1);
/* 2565 */         if (mustMatchToken(88, "msg.no.paren")) {
/* 2566 */           n = this.ts.tokenBeg;
/* 2567 */           i1 = this.ts.tokenEnd;
/*      */         }
/* 2569 */         XmlDotQuery localXmlDotQuery = new XmlDotQuery(i, i1 - i);
/* 2570 */         localXmlDotQuery.setLeft(paramAstNode);
/* 2571 */         localXmlDotQuery.setRight(localAstNode1);
/* 2572 */         localXmlDotQuery.setOperatorPosition(m);
/* 2573 */         localXmlDotQuery.setRp(n - i);
/* 2574 */         localXmlDotQuery.setLineno(j);
/* 2575 */         paramAstNode = localXmlDotQuery;
/* 2576 */         break;
/*      */       case 83:
/* 2579 */         consumeToken();
/* 2580 */         int i2 = this.ts.tokenBeg; int i3 = -1;
/* 2581 */         j = this.ts.lineno;
/* 2582 */         AstNode localAstNode2 = expr();
/* 2583 */         i1 = getNodeEnd(localAstNode2);
/* 2584 */         if (mustMatchToken(84, "msg.no.bracket.index")) {
/* 2585 */           i3 = this.ts.tokenBeg;
/* 2586 */           i1 = this.ts.tokenEnd;
/*      */         }
/* 2588 */         ElementGet localElementGet = new ElementGet(i, i1 - i);
/* 2589 */         localElementGet.setTarget(paramAstNode);
/* 2590 */         localElementGet.setElement(localAstNode2);
/* 2591 */         localElementGet.setParens(i2, i3);
/* 2592 */         localElementGet.setLineno(j);
/* 2593 */         paramAstNode = localElementGet;
/* 2594 */         break;
/*      */       case 87:
/* 2597 */         if (!paramBoolean) {
/*      */           break label488;
/*      */         }
/* 2600 */         j = this.ts.lineno;
/* 2601 */         consumeToken();
/* 2602 */         checkCallRequiresActivation(paramAstNode);
/* 2603 */         FunctionCall localFunctionCall = new FunctionCall(i);
/* 2604 */         localFunctionCall.setTarget(paramAstNode);
/*      */ 
/* 2607 */         localFunctionCall.setLineno(j);
/* 2608 */         localFunctionCall.setLp(this.ts.tokenBeg - i);
/* 2609 */         List localList = argumentList();
/* 2610 */         if ((localList != null) && (localList.size() > 65536))
/* 2611 */           reportError("msg.too.many.function.args");
/* 2612 */         localFunctionCall.setArguments(localList);
/* 2613 */         localFunctionCall.setRp(this.ts.tokenBeg - i);
/* 2614 */         localFunctionCall.setLength(this.ts.tokenEnd - i);
/* 2615 */         paramAstNode = localFunctionCall;
/* 2616 */         break;
/*      */       default:
/* 2619 */         break label488;
/*      */       }
/*      */     }
/* 2622 */     label488: return paramAstNode;
/*      */   }
/*      */ 
/*      */   private AstNode propertyAccess(int paramInt, AstNode paramAstNode)
/*      */     throws IOException
/*      */   {
/* 2633 */     if (paramAstNode == null) codeBug();
/* 2634 */     int i = 0; int j = this.ts.lineno; int k = this.ts.tokenBeg;
/* 2635 */     consumeToken();
/*      */ 
/* 2637 */     if (paramInt == 143) {
/* 2638 */       mustHaveXML();
/* 2639 */       i = 4;
/*      */     }
/*      */ 
/* 2642 */     if (!this.compilerEnv.isXmlAvailable()) {
/* 2643 */       mustMatchToken(39, "msg.no.name.after.dot");
/* 2644 */       localObject = createNameNode(true, 33);
/* 2645 */       PropertyGet localPropertyGet1 = new PropertyGet(paramAstNode, (Name)localObject, k);
/* 2646 */       localPropertyGet1.setLineno(j);
/* 2647 */       return localPropertyGet1;
/*      */     }
/*      */ 
/* 2650 */     Object localObject = null;
/*      */ 
/* 2652 */     switch (nextToken())
/*      */     {
/*      */     case 50:
/* 2655 */       saveNameTokenData(this.ts.tokenBeg, "throw", this.ts.lineno);
/* 2656 */       localObject = propertyName(-1, "throw", i);
/* 2657 */       break;
/*      */     case 39:
/* 2661 */       localObject = propertyName(-1, this.ts.getString(), i);
/* 2662 */       break;
/*      */     case 23:
/* 2666 */       saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2667 */       localObject = propertyName(-1, "*", i);
/* 2668 */       break;
/*      */     case 147:
/* 2673 */       localObject = attributeAccess();
/* 2674 */       break;
/*      */     default:
/* 2677 */       reportError("msg.no.name.after.dot");
/* 2678 */       return makeErrorNode();
/*      */     }
/*      */ 
/* 2681 */     boolean bool = localObject instanceof XmlRef;
/* 2682 */     PropertyGet localPropertyGet2 = bool ? new XmlMemberGet() : new PropertyGet();
/* 2683 */     if ((bool) && (paramInt == 108))
/* 2684 */       localPropertyGet2.setType(108);
/* 2685 */     int m = paramAstNode.getPosition();
/* 2686 */     localPropertyGet2.setPosition(m);
/* 2687 */     localPropertyGet2.setLength(getNodeEnd((AstNode)localObject) - m);
/* 2688 */     localPropertyGet2.setOperatorPosition(k - m);
/* 2689 */     localPropertyGet2.setLineno(j);
/* 2690 */     localPropertyGet2.setLeft(paramAstNode);
/* 2691 */     localPropertyGet2.setRight((AstNode)localObject);
/* 2692 */     return localPropertyGet2;
/*      */   }
/*      */ 
/*      */   private AstNode attributeAccess()
/*      */     throws IOException
/*      */   {
/* 2705 */     int i = nextToken(); int j = this.ts.tokenBeg;
/*      */ 
/* 2707 */     switch (i)
/*      */     {
/*      */     case 39:
/* 2710 */       return propertyName(j, this.ts.getString(), 0);
/*      */     case 23:
/* 2714 */       saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2715 */       return propertyName(j, "*", 0);
/*      */     case 83:
/* 2719 */       return xmlElemRef(j, null, -1);
/*      */     }
/*      */ 
/* 2722 */     reportError("msg.no.name.after.xmlAttr");
/* 2723 */     return makeErrorNode();
/*      */   }
/*      */ 
/*      */   private AstNode propertyName(int paramInt1, String paramString, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 2745 */     int i = paramInt1 != -1 ? paramInt1 : this.ts.tokenBeg; int j = this.ts.lineno;
/* 2746 */     int k = -1;
/* 2747 */     Name localName1 = createNameNode(true, this.currentToken);
/* 2748 */     Name localName2 = null;
/*      */ 
/* 2750 */     if (matchToken(144)) {
/* 2751 */       localName2 = localName1;
/* 2752 */       k = this.ts.tokenBeg;
/*      */ 
/* 2754 */       switch (nextToken())
/*      */       {
/*      */       case 39:
/* 2757 */         localName1 = createNameNode();
/* 2758 */         break;
/*      */       case 23:
/* 2762 */         saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2763 */         localName1 = createNameNode(false, -1);
/* 2764 */         break;
/*      */       case 83:
/* 2768 */         return xmlElemRef(paramInt1, localName2, k);
/*      */       default:
/* 2771 */         reportError("msg.no.name.after.coloncolon");
/* 2772 */         return makeErrorNode();
/*      */       }
/*      */     }
/*      */ 
/* 2776 */     if ((localName2 == null) && (paramInt2 == 0) && (paramInt1 == -1)) {
/* 2777 */       return localName1;
/*      */     }
/*      */ 
/* 2780 */     XmlPropRef localXmlPropRef = new XmlPropRef(i, getNodeEnd(localName1) - i);
/* 2781 */     localXmlPropRef.setAtPos(paramInt1);
/* 2782 */     localXmlPropRef.setNamespace(localName2);
/* 2783 */     localXmlPropRef.setColonPos(k);
/* 2784 */     localXmlPropRef.setPropName(localName1);
/* 2785 */     localXmlPropRef.setLineno(j);
/* 2786 */     return localXmlPropRef;
/*      */   }
/*      */ 
/*      */   private XmlElemRef xmlElemRef(int paramInt1, Name paramName, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 2796 */     int i = this.ts.tokenBeg; int j = -1; int k = paramInt1 != -1 ? paramInt1 : i;
/* 2797 */     AstNode localAstNode = expr();
/* 2798 */     int m = getNodeEnd(localAstNode);
/* 2799 */     if (mustMatchToken(84, "msg.no.bracket.index")) {
/* 2800 */       j = this.ts.tokenBeg;
/* 2801 */       m = this.ts.tokenEnd;
/*      */     }
/* 2803 */     XmlElemRef localXmlElemRef = new XmlElemRef(k, m - k);
/* 2804 */     localXmlElemRef.setNamespace(paramName);
/* 2805 */     localXmlElemRef.setColonPos(paramInt2);
/* 2806 */     localXmlElemRef.setAtPos(paramInt1);
/* 2807 */     localXmlElemRef.setExpression(localAstNode);
/* 2808 */     localXmlElemRef.setBrackets(i, j);
/* 2809 */     return localXmlElemRef;
/*      */   }
/*      */ 
/*      */   private AstNode destructuringPrimaryExpr() throws IOException, Parser.ParserException
/*      */   {
/*      */     try
/*      */     {
/* 2816 */       this.inDestructuringAssignment = true;
/* 2817 */       return primaryExpr();
/*      */     } finally {
/* 2819 */       this.inDestructuringAssignment = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private AstNode primaryExpr()
/*      */     throws IOException
/*      */   {
/* 2826 */     int i = nextFlaggedToken();
/* 2827 */     int j = i & 0xFFFF;
/*      */     int k;
/*      */     int m;
/* 2829 */     switch (j) {
/*      */     case 109:
/* 2831 */       return function(2);
/*      */     case 83:
/* 2834 */       return arrayLiteral();
/*      */     case 85:
/* 2837 */       return objectLiteral();
/*      */     case 153:
/* 2840 */       return let(false, this.ts.tokenBeg);
/*      */     case 87:
/* 2843 */       return parenExpr();
/*      */     case 147:
/* 2846 */       mustHaveXML();
/* 2847 */       return attributeAccess();
/*      */     case 39:
/* 2850 */       return name(i, j);
/*      */     case 40:
/* 2853 */       String str = this.ts.getString();
/* 2854 */       if ((this.inUseStrictDirective) && (this.ts.isNumberOctal())) {
/* 2855 */         reportError("msg.no.octal.strict");
/*      */       }
/* 2857 */       return new NumberLiteral(this.ts.tokenBeg, str, this.ts.getNumber());
/*      */     case 41:
/* 2863 */       return createStringLiteral();
/*      */     case 24:
/*      */     case 100:
/* 2868 */       this.ts.readRegExp(j);
/* 2869 */       k = this.ts.tokenBeg; m = this.ts.tokenEnd;
/* 2870 */       RegExpLiteral localRegExpLiteral = new RegExpLiteral(k, m - k);
/* 2871 */       localRegExpLiteral.setValue(this.ts.getString());
/* 2872 */       localRegExpLiteral.setFlags(this.ts.readAndClearRegExpFlags());
/* 2873 */       return localRegExpLiteral;
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/* 2879 */       k = this.ts.tokenBeg; m = this.ts.tokenEnd;
/* 2880 */       return new KeywordLiteral(k, m - k, j);
/*      */     case 127:
/* 2883 */       reportError("msg.reserved.id");
/* 2884 */       break;
/*      */     case -1:
/* 2888 */       break;
/*      */     case 0:
/* 2891 */       reportError("msg.unexpected.eof");
/* 2892 */       break;
/*      */     default:
/* 2895 */       reportError("msg.syntax");
/*      */     }
/*      */ 
/* 2899 */     return makeErrorNode();
/*      */   }
/*      */ 
/*      */   private AstNode parenExpr() throws IOException {
/* 2903 */     boolean bool = this.inForInit;
/* 2904 */     this.inForInit = false;
/*      */     try {
/* 2906 */       String str = getAndResetJsDoc();
/* 2907 */       int i = this.ts.lineno;
/* 2908 */       AstNode localAstNode = expr();
/* 2909 */       ParenthesizedExpression localParenthesizedExpression1 = new ParenthesizedExpression(localAstNode);
/* 2910 */       if (str == null) {
/* 2911 */         str = getAndResetJsDoc();
/*      */       }
/* 2913 */       if (str != null) {
/* 2914 */         localParenthesizedExpression1.setJsDoc(str);
/*      */       }
/* 2916 */       mustMatchToken(88, "msg.no.paren");
/* 2917 */       localParenthesizedExpression1.setLength(this.ts.tokenEnd - localParenthesizedExpression1.getPosition());
/* 2918 */       localParenthesizedExpression1.setLineno(i);
/* 2919 */       return localParenthesizedExpression1;
/*      */     } finally {
/* 2921 */       this.inForInit = bool;
/*      */     }
/*      */   }
/*      */ 
/*      */   private AstNode name(int paramInt1, int paramInt2) throws IOException {
/* 2926 */     String str = this.ts.getString();
/* 2927 */     int i = this.ts.tokenBeg; int j = this.ts.lineno;
/* 2928 */     if ((0 != (paramInt1 & 0x20000)) && (peekToken() == 103))
/*      */     {
/* 2931 */       Label localLabel = new Label(i, this.ts.tokenEnd - i);
/* 2932 */       localLabel.setName(str);
/* 2933 */       localLabel.setLineno(this.ts.lineno);
/* 2934 */       return localLabel;
/*      */     }
/*      */ 
/* 2939 */     saveNameTokenData(i, str, j);
/*      */ 
/* 2941 */     if (this.compilerEnv.isXmlAvailable()) {
/* 2942 */       return propertyName(-1, str, 0);
/*      */     }
/* 2944 */     return createNameNode(true, 39);
/*      */   }
/*      */ 
/*      */   private AstNode arrayLiteral()
/*      */     throws IOException
/*      */   {
/* 2954 */     if (this.currentToken != 83) codeBug();
/* 2955 */     int i = this.ts.tokenBeg; int j = this.ts.tokenEnd;
/* 2956 */     ArrayList localArrayList = new ArrayList();
/* 2957 */     ArrayLiteral localArrayLiteral = new ArrayLiteral(i);
/* 2958 */     int k = 1;
/* 2959 */     int m = -1;
/* 2960 */     int n = 0;
/*      */     while (true) {
/* 2962 */       int i1 = peekToken();
/* 2963 */       if (i1 == 89) {
/* 2964 */         consumeToken();
/* 2965 */         m = this.ts.tokenEnd;
/* 2966 */         if (k == 0) {
/* 2967 */           k = 1;
/*      */         } else {
/* 2969 */           localArrayList.add(new EmptyExpression(this.ts.tokenBeg, 1));
/* 2970 */           n++;
/*      */         }
/*      */       } else { if (i1 == 84) {
/* 2973 */           consumeToken();
/*      */ 
/* 2979 */           j = this.ts.tokenEnd;
/* 2980 */           localArrayLiteral.setDestructuringLength(localArrayList.size() + (k != 0 ? 1 : 0));
/*      */ 
/* 2982 */           localArrayLiteral.setSkipCount(n);
/* 2983 */           if (m == -1) break;
/* 2984 */           warnTrailingComma("msg.array.trailing.comma", i, localArrayList, m); break;
/*      */         }
/*      */ 
/* 2987 */         if ((i1 == 119) && (k == 0) && (localArrayList.size() == 1))
/*      */         {
/* 2989 */           return arrayComprehension((AstNode)localArrayList.get(0), i);
/* 2990 */         }if (i1 == 0) {
/* 2991 */           reportError("msg.no.bracket.arg");
/*      */         } else {
/* 2993 */           if (k == 0) {
/* 2994 */             reportError("msg.no.bracket.arg");
/*      */           }
/* 2996 */           localArrayList.add(assignExpr());
/* 2997 */           k = 0;
/* 2998 */           m = -1;
/*      */         } }
/*      */     }
/* 3001 */     for (AstNode localAstNode : localArrayList) {
/* 3002 */       localArrayLiteral.addElement(localAstNode);
/*      */     }
/* 3004 */     localArrayLiteral.setLength(j - i);
/* 3005 */     return localArrayLiteral;
/*      */   }
/*      */ 
/*      */   private AstNode arrayComprehension(AstNode paramAstNode, int paramInt)
/*      */     throws IOException
/*      */   {
/* 3017 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 3019 */     while (peekToken() == 119) {
/* 3020 */       localArrayList.add(arrayComprehensionLoop());
/*      */     }
/* 3022 */     int i = -1;
/* 3023 */     ConditionData localConditionData = null;
/* 3024 */     if (peekToken() == 112) {
/* 3025 */       consumeToken();
/* 3026 */       i = this.ts.tokenBeg - paramInt;
/* 3027 */       localConditionData = condition();
/*      */     }
/* 3029 */     mustMatchToken(84, "msg.no.bracket.arg");
/* 3030 */     ArrayComprehension localArrayComprehension = new ArrayComprehension(paramInt, this.ts.tokenEnd - paramInt);
/* 3031 */     localArrayComprehension.setResult(paramAstNode);
/* 3032 */     localArrayComprehension.setLoops(localArrayList);
/* 3033 */     if (localConditionData != null) {
/* 3034 */       localArrayComprehension.setIfPosition(i);
/* 3035 */       localArrayComprehension.setFilter(localConditionData.condition);
/* 3036 */       localArrayComprehension.setFilterLp(localConditionData.lp - paramInt);
/* 3037 */       localArrayComprehension.setFilterRp(localConditionData.rp - paramInt);
/*      */     }
/* 3039 */     return localArrayComprehension;
/*      */   }
/*      */ 
/*      */   private ArrayComprehensionLoop arrayComprehensionLoop()
/*      */     throws IOException
/*      */   {
/* 3045 */     if (nextToken() != 119) codeBug();
/* 3046 */     int i = this.ts.tokenBeg;
/* 3047 */     int j = -1; int k = -1; int m = -1; int n = -1;
/* 3048 */     ArrayComprehensionLoop localArrayComprehensionLoop1 = new ArrayComprehensionLoop(i);
/*      */ 
/* 3050 */     pushScope(localArrayComprehensionLoop1);
/*      */     try {
/* 3052 */       if (matchToken(39)) {
/* 3053 */         if (this.ts.getString().equals("each"))
/* 3054 */           j = this.ts.tokenBeg - i;
/*      */         else {
/* 3056 */           reportError("msg.no.paren.for");
/*      */         }
/*      */       }
/* 3059 */       if (mustMatchToken(87, "msg.no.paren.for")) {
/* 3060 */         k = this.ts.tokenBeg - i;
/*      */       }
/*      */ 
/* 3063 */       Object localObject1 = null;
/* 3064 */       switch (peekToken())
/*      */       {
/*      */       case 83:
/*      */       case 85:
/* 3068 */         localObject1 = destructuringPrimaryExpr();
/* 3069 */         markDestructuring((AstNode)localObject1);
/* 3070 */         break;
/*      */       case 39:
/* 3072 */         consumeToken();
/* 3073 */         localObject1 = createNameNode();
/* 3074 */         break;
/*      */       default:
/* 3076 */         reportError("msg.bad.var");
/*      */       }
/*      */ 
/* 3081 */       if (((AstNode)localObject1).getType() == 39) {
/* 3082 */         defineSymbol(153, this.ts.getString(), true);
/*      */       }
/*      */ 
/* 3085 */       if (mustMatchToken(52, "msg.in.after.for.name"))
/* 3086 */         n = this.ts.tokenBeg - i;
/* 3087 */       AstNode localAstNode = expr();
/* 3088 */       if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
/* 3089 */         m = this.ts.tokenBeg - i;
/*      */       }
/* 3091 */       localArrayComprehensionLoop1.setLength(this.ts.tokenEnd - i);
/* 3092 */       localArrayComprehensionLoop1.setIterator((AstNode)localObject1);
/* 3093 */       localArrayComprehensionLoop1.setIteratedObject(localAstNode);
/* 3094 */       localArrayComprehensionLoop1.setInPosition(n);
/* 3095 */       localArrayComprehensionLoop1.setEachPosition(j);
/* 3096 */       localArrayComprehensionLoop1.setIsForEach(j != -1);
/* 3097 */       localArrayComprehensionLoop1.setParens(k, m);
/* 3098 */       return localArrayComprehensionLoop1;
/*      */     } finally {
/* 3100 */       popScope();
/*      */     }
/*      */   }
/*      */ 
/*      */   private ObjectLiteral objectLiteral()
/*      */     throws IOException
/*      */   {
/* 3107 */     int i = this.ts.tokenBeg; int j = this.ts.lineno;
/* 3108 */     int k = -1;
/* 3109 */     ArrayList localArrayList = new ArrayList();
/* 3110 */     HashSet localHashSet = new HashSet();
/*      */     while (true)
/*      */     {
/* 3114 */       localObject1 = null;
/* 3115 */       int m = peekToken();
/* 3116 */       String str = getAndResetJsDoc();
/*      */       Object localObject2;
/* 3117 */       switch (m) {
/*      */       case 39:
/*      */       case 41:
/* 3120 */         k = -1;
/* 3121 */         saveNameTokenData(this.ts.tokenBeg, this.ts.getString(), this.ts.lineno);
/* 3122 */         consumeToken();
/* 3123 */         StringLiteral localStringLiteral = null;
/* 3124 */         if (m == 41) {
/* 3125 */           localStringLiteral = createStringLiteral();
/*      */         }
/* 3127 */         Name localName = createNameNode();
/* 3128 */         localObject1 = this.ts.getString();
/* 3129 */         int n = this.ts.tokenBeg;
/*      */ 
/* 3131 */         if ((m == 39) && (peekToken() == 39) && (("get".equals(localObject1)) || ("set".equals(localObject1))))
/*      */         {
/* 3135 */           consumeToken();
/* 3136 */           localName = createNameNode();
/* 3137 */           localName.setJsDoc(str);
/* 3138 */           localObject2 = getterSetterProperty(n, localName, "get".equals(localObject1));
/*      */ 
/* 3140 */           localArrayList.add(localObject2);
/* 3141 */           localObject1 = ((ObjectProperty)localObject2).getLeft().getString();
/*      */         } else {
/* 3143 */           localObject2 = localStringLiteral != null ? localStringLiteral : localName;
/* 3144 */           ((AstNode)localObject2).setJsDoc(str);
/* 3145 */           localArrayList.add(plainProperty((AstNode)localObject2, m));
/*      */         }
/* 3147 */         break;
/*      */       case 40:
/* 3150 */         consumeToken();
/* 3151 */         k = -1;
/* 3152 */         localObject2 = new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber());
/*      */ 
/* 3155 */         ((AstNode)localObject2).setJsDoc(str);
/* 3156 */         localObject1 = this.ts.getString();
/* 3157 */         localArrayList.add(plainProperty((AstNode)localObject2, m));
/* 3158 */         break;
/*      */       case 86:
/* 3161 */         if ((k == -1) || (!this.compilerEnv.getWarnTrailingComma())) break label476;
/* 3162 */         warnTrailingComma("msg.extra.trailing.comma", i, localArrayList, k); break;
/*      */       default:
/* 3167 */         reportError("msg.bad.prop");
/*      */       }
/*      */ 
/* 3171 */       if (this.inUseStrictDirective) {
/* 3172 */         if (localHashSet.contains(localObject1)) {
/* 3173 */           addError("msg.dup.obj.lit.prop.strict", (String)localObject1);
/*      */         }
/* 3175 */         localHashSet.add(localObject1);
/*      */       }
/*      */ 
/* 3179 */       getAndResetJsDoc();
/* 3180 */       str = null;
/*      */ 
/* 3182 */       if (!matchToken(89)) break;
/* 3183 */       k = this.ts.tokenEnd;
/*      */     }
/*      */ 
/* 3189 */     label476: mustMatchToken(86, "msg.no.brace.prop");
/* 3190 */     Object localObject1 = new ObjectLiteral(i, this.ts.tokenEnd - i);
/* 3191 */     ((ObjectLiteral)localObject1).setElements(localArrayList);
/* 3192 */     ((ObjectLiteral)localObject1).setLineno(j);
/* 3193 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private ObjectProperty plainProperty(AstNode paramAstNode, int paramInt)
/*      */     throws IOException
/*      */   {
/* 3201 */     int i = peekToken();
/* 3202 */     if (((i == 89) || (i == 86)) && (paramInt == 39) && (this.compilerEnv.getLanguageVersion() >= 180))
/*      */     {
/* 3204 */       if (!this.inDestructuringAssignment) {
/* 3205 */         reportError("msg.bad.object.init");
/*      */       }
/* 3207 */       localObject = new Name(paramAstNode.getPosition(), paramAstNode.getString());
/* 3208 */       ObjectProperty localObjectProperty = new ObjectProperty();
/* 3209 */       localObjectProperty.putProp(26, Boolean.TRUE);
/* 3210 */       localObjectProperty.setLeftAndRight(paramAstNode, (AstNode)localObject);
/* 3211 */       return localObjectProperty;
/*      */     }
/* 3213 */     mustMatchToken(103, "msg.no.colon.prop");
/* 3214 */     Object localObject = new ObjectProperty();
/* 3215 */     ((ObjectProperty)localObject).setOperatorPosition(this.ts.tokenBeg);
/* 3216 */     ((ObjectProperty)localObject).setLeftAndRight(paramAstNode, assignExpr());
/* 3217 */     return localObject;
/*      */   }
/*      */ 
/*      */   private ObjectProperty getterSetterProperty(int paramInt, AstNode paramAstNode, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 3224 */     FunctionNode localFunctionNode = function(2);
/*      */ 
/* 3226 */     Name localName = localFunctionNode.getFunctionName();
/* 3227 */     if ((localName != null) && (localName.length() != 0)) {
/* 3228 */       reportError("msg.bad.prop");
/*      */     }
/* 3230 */     ObjectProperty localObjectProperty = new ObjectProperty(paramInt);
/* 3231 */     if (paramBoolean)
/* 3232 */       localObjectProperty.setIsGetter();
/*      */     else {
/* 3234 */       localObjectProperty.setIsSetter();
/*      */     }
/* 3236 */     int i = getNodeEnd(localFunctionNode);
/* 3237 */     localObjectProperty.setLeft(paramAstNode);
/* 3238 */     localObjectProperty.setRight(localFunctionNode);
/* 3239 */     localObjectProperty.setLength(i - paramInt);
/* 3240 */     return localObjectProperty;
/*      */   }
/*      */ 
/*      */   private Name createNameNode() {
/* 3244 */     return createNameNode(false, 39);
/*      */   }
/*      */ 
/*      */   private Name createNameNode(boolean paramBoolean, int paramInt)
/*      */   {
/* 3255 */     int i = this.ts.tokenBeg;
/* 3256 */     String str = this.ts.getString();
/* 3257 */     int j = this.ts.lineno;
/* 3258 */     if (!"".equals(this.prevNameTokenString)) {
/* 3259 */       i = this.prevNameTokenStart;
/* 3260 */       str = this.prevNameTokenString;
/* 3261 */       j = this.prevNameTokenLineno;
/* 3262 */       this.prevNameTokenStart = 0;
/* 3263 */       this.prevNameTokenString = "";
/* 3264 */       this.prevNameTokenLineno = 0;
/*      */     }
/* 3266 */     if (str == null) {
/* 3267 */       if (this.compilerEnv.isIdeMode())
/* 3268 */         str = "";
/*      */       else {
/* 3270 */         codeBug();
/*      */       }
/*      */     }
/* 3273 */     Name localName = new Name(i, str);
/* 3274 */     localName.setLineno(j);
/* 3275 */     if (paramBoolean) {
/* 3276 */       checkActivationName(str, paramInt);
/*      */     }
/* 3278 */     return localName;
/*      */   }
/*      */ 
/*      */   private StringLiteral createStringLiteral() {
/* 3282 */     int i = this.ts.tokenBeg; int j = this.ts.tokenEnd;
/* 3283 */     StringLiteral localStringLiteral = new StringLiteral(i, j - i);
/* 3284 */     localStringLiteral.setLineno(this.ts.lineno);
/* 3285 */     localStringLiteral.setValue(this.ts.getString());
/* 3286 */     localStringLiteral.setQuoteCharacter(this.ts.getQuoteChar());
/* 3287 */     return localStringLiteral;
/*      */   }
/*      */ 
/*      */   protected void checkActivationName(String paramString, int paramInt) {
/* 3291 */     if (!insideFunction()) {
/* 3292 */       return;
/*      */     }
/* 3294 */     int i = 0;
/* 3295 */     if (("arguments".equals(paramString)) || ((this.compilerEnv.getActivationNames() != null) && (this.compilerEnv.getActivationNames().contains(paramString))))
/*      */     {
/* 3299 */       i = 1;
/* 3300 */     } else if (("length".equals(paramString)) && 
/* 3301 */       (paramInt == 33) && (this.compilerEnv.getLanguageVersion() == 120))
/*      */     {
/* 3305 */       i = 1;
/*      */     }
/*      */ 
/* 3308 */     if (i != 0)
/* 3309 */       setRequiresActivation();
/*      */   }
/*      */ 
/*      */   protected void setRequiresActivation()
/*      */   {
/* 3314 */     if (insideFunction())
/* 3315 */       ((FunctionNode)this.currentScriptOrFn).setRequiresActivation();
/*      */   }
/*      */ 
/*      */   private void checkCallRequiresActivation(AstNode paramAstNode)
/*      */   {
/* 3320 */     if (((paramAstNode.getType() == 39) && ("eval".equals(((Name)paramAstNode).getIdentifier()))) || ((paramAstNode.getType() == 33) && ("eval".equals(((PropertyGet)paramAstNode).getProperty().getIdentifier()))))
/*      */     {
/* 3324 */       setRequiresActivation();
/*      */     }
/*      */   }
/*      */ 
/* 3328 */   protected void setIsGenerator() { if (insideFunction())
/* 3329 */       ((FunctionNode)this.currentScriptOrFn).setIsGenerator();
/*      */   }
/*      */ 
/*      */   private void checkBadIncDec(UnaryExpression paramUnaryExpression)
/*      */   {
/* 3334 */     AstNode localAstNode = removeParens(paramUnaryExpression.getOperand());
/* 3335 */     int i = localAstNode.getType();
/* 3336 */     if ((i != 39) && (i != 33) && (i != 36) && (i != 67) && (i != 38))
/*      */     {
/* 3341 */       reportError(paramUnaryExpression.getType() == 106 ? "msg.bad.incr" : "msg.bad.decr");
/*      */     }
/*      */   }
/*      */ 
/*      */   private ErrorNode makeErrorNode()
/*      */   {
/* 3347 */     ErrorNode localErrorNode = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/* 3348 */     localErrorNode.setLineno(this.ts.lineno);
/* 3349 */     return localErrorNode;
/*      */   }
/*      */ 
/*      */   private int nodeEnd(AstNode paramAstNode)
/*      */   {
/* 3354 */     return paramAstNode.getPosition() + paramAstNode.getLength();
/*      */   }
/*      */ 
/*      */   private void saveNameTokenData(int paramInt1, String paramString, int paramInt2) {
/* 3358 */     this.prevNameTokenStart = paramInt1;
/* 3359 */     this.prevNameTokenString = paramString;
/* 3360 */     this.prevNameTokenLineno = paramInt2;
/*      */   }
/*      */ 
/*      */   private int lineBeginningFor(int paramInt)
/*      */   {
/* 3377 */     if (this.sourceChars == null) {
/* 3378 */       return -1;
/*      */     }
/* 3380 */     if (paramInt <= 0) {
/* 3381 */       return 0;
/*      */     }
/* 3383 */     char[] arrayOfChar = this.sourceChars;
/* 3384 */     if (paramInt >= arrayOfChar.length)
/* 3385 */       paramInt = arrayOfChar.length - 1;
/*      */     while (true) {
/* 3387 */       paramInt--; if (paramInt < 0) break;
/* 3388 */       int i = arrayOfChar[paramInt];
/* 3389 */       if ((i == 10) || (i == 13)) {
/* 3390 */         return paramInt + 1;
/*      */       }
/*      */     }
/* 3393 */     return 0;
/*      */   }
/*      */ 
/*      */   private void warnMissingSemi(int paramInt1, int paramInt2)
/*      */   {
/* 3400 */     if (this.compilerEnv.isStrictMode()) {
/* 3401 */       int i = Math.max(paramInt1, lineBeginningFor(paramInt2));
/* 3402 */       if (paramInt2 == -1)
/* 3403 */         paramInt2 = this.ts.cursor;
/* 3404 */       addStrictWarning("msg.missing.semi", "", i, paramInt2 - i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void warnTrailingComma(String paramString, int paramInt1, List<?> paramList, int paramInt2)
/*      */   {
/* 3411 */     if (this.compilerEnv.getWarnTrailingComma())
/*      */     {
/* 3413 */       if (!paramList.isEmpty()) {
/* 3414 */         paramInt1 = ((AstNode)paramList.get(0)).getPosition();
/*      */       }
/* 3416 */       paramInt1 = Math.max(paramInt1, lineBeginningFor(paramInt2));
/* 3417 */       addWarning("msg.extra.trailing.comma", paramInt1, paramInt2 - paramInt1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String readFully(Reader paramReader) throws IOException
/*      */   {
/* 3423 */     BufferedReader localBufferedReader = new BufferedReader(paramReader);
/*      */     try {
/* 3425 */       char[] arrayOfChar = new char[1024];
/* 3426 */       StringBuilder localStringBuilder = new StringBuilder(1024);
/*      */       int i;
/* 3428 */       while ((i = localBufferedReader.read(arrayOfChar, 0, 1024)) != -1) {
/* 3429 */         localStringBuilder.append(arrayOfChar, 0, i);
/*      */       }
/* 3431 */       return localStringBuilder.toString();
/*      */     } finally {
/* 3433 */       localBufferedReader.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   Node createDestructuringAssignment(int paramInt, Node paramNode1, Node paramNode2)
/*      */   {
/* 3501 */     String str = this.currentScriptOrFn.getNextTempName();
/* 3502 */     Node localNode1 = destructuringAssignmentHelper(paramInt, paramNode1, paramNode2, str);
/*      */ 
/* 3504 */     Node localNode2 = localNode1.getLastChild();
/* 3505 */     localNode2.addChildToBack(createName(str));
/* 3506 */     return localNode1;
/*      */   }
/*      */ 
/*      */   Node destructuringAssignmentHelper(int paramInt, Node paramNode1, Node paramNode2, String paramString)
/*      */   {
/* 3512 */     Scope localScope = createScopeNode(158, paramNode1.getLineno());
/* 3513 */     localScope.addChildToFront(new Node(153, createName(39, paramString, paramNode2)));
/*      */     try
/*      */     {
/* 3516 */       pushScope(localScope);
/* 3517 */       defineSymbol(153, paramString, true);
/*      */     } finally {
/* 3519 */       popScope();
/*      */     }
/* 3521 */     Node localNode = new Node(89);
/* 3522 */     localScope.addChildToBack(localNode);
/* 3523 */     ArrayList localArrayList = new ArrayList();
/* 3524 */     boolean bool = true;
/* 3525 */     switch (paramNode1.getType()) {
/*      */     case 65:
/* 3527 */       bool = destructuringArray((ArrayLiteral)paramNode1, paramInt, paramString, localNode, localArrayList);
/*      */ 
/* 3530 */       break;
/*      */     case 66:
/* 3532 */       bool = destructuringObject((ObjectLiteral)paramNode1, paramInt, paramString, localNode, localArrayList);
/*      */ 
/* 3535 */       break;
/*      */     case 33:
/*      */     case 36:
/* 3538 */       localNode.addChildToBack(simpleAssignment(paramNode1, createName(paramString)));
/* 3539 */       break;
/*      */     default:
/* 3541 */       reportError("msg.bad.assign.left");
/*      */     }
/* 3543 */     if (bool)
/*      */     {
/* 3545 */       localNode.addChildToBack(createNumber(0.0D));
/*      */     }
/* 3547 */     localScope.putProp(22, localArrayList);
/* 3548 */     return localScope;
/*      */   }
/*      */ 
/*      */   boolean destructuringArray(ArrayLiteral paramArrayLiteral, int paramInt, String paramString, Node paramNode, List<String> paramList)
/*      */   {
/* 3557 */     boolean bool = true;
/* 3558 */     int i = paramInt == 154 ? 155 : 8;
/*      */ 
/* 3560 */     int j = 0;
/* 3561 */     for (AstNode localAstNode : paramArrayLiteral.getElements())
/* 3562 */       if (localAstNode.getType() == 128) {
/* 3563 */         j++;
/*      */       }
/*      */       else {
/* 3566 */         Node localNode = new Node(36, createName(paramString), createNumber(j));
/*      */ 
/* 3569 */         if (localAstNode.getType() == 39) {
/* 3570 */           String str = localAstNode.getString();
/* 3571 */           paramNode.addChildToBack(new Node(i, createName(49, str, null), localNode));
/*      */ 
/* 3575 */           if (paramInt != -1) {
/* 3576 */             defineSymbol(paramInt, str, true);
/* 3577 */             paramList.add(str);
/*      */           }
/*      */         } else {
/* 3580 */           paramNode.addChildToBack(destructuringAssignmentHelper(paramInt, localAstNode, localNode, this.currentScriptOrFn.getNextTempName()));
/*      */         }
/*      */ 
/* 3586 */         j++;
/* 3587 */         bool = false;
/*      */       }
/* 3589 */     return bool;
/*      */   }
/*      */ 
/*      */   boolean destructuringObject(ObjectLiteral paramObjectLiteral, int paramInt, String paramString, Node paramNode, List<String> paramList)
/*      */   {
/* 3598 */     boolean bool = true;
/* 3599 */     int i = paramInt == 154 ? 155 : 8;
/*      */ 
/* 3602 */     for (ObjectProperty localObjectProperty : paramObjectLiteral.getElements()) {
/* 3603 */       int j = 0;
/*      */ 
/* 3607 */       if (this.ts != null) {
/* 3608 */         j = this.ts.lineno;
/*      */       }
/* 3610 */       AstNode localAstNode = localObjectProperty.getLeft();
/* 3611 */       Node localNode = null;
/* 3612 */       if ((localAstNode instanceof Name)) {
/* 3613 */         localObject = Node.newString(((Name)localAstNode).getIdentifier());
/* 3614 */         localNode = new Node(33, createName(paramString), (Node)localObject);
/* 3615 */       } else if ((localAstNode instanceof StringLiteral)) {
/* 3616 */         localObject = Node.newString(((StringLiteral)localAstNode).getValue());
/* 3617 */         localNode = new Node(33, createName(paramString), (Node)localObject);
/* 3618 */       } else if ((localAstNode instanceof NumberLiteral)) {
/* 3619 */         localObject = createNumber((int)((NumberLiteral)localAstNode).getNumber());
/* 3620 */         localNode = new Node(36, createName(paramString), (Node)localObject);
/*      */       } else {
/* 3622 */         throw codeBug();
/*      */       }
/* 3624 */       localNode.setLineno(j);
/* 3625 */       Object localObject = localObjectProperty.getRight();
/* 3626 */       if (((AstNode)localObject).getType() == 39) {
/* 3627 */         String str = ((Name)localObject).getIdentifier();
/* 3628 */         paramNode.addChildToBack(new Node(i, createName(49, str, null), localNode));
/*      */ 
/* 3632 */         if (paramInt != -1) {
/* 3633 */           defineSymbol(paramInt, str, true);
/* 3634 */           paramList.add(str);
/*      */         }
/*      */       } else {
/* 3637 */         paramNode.addChildToBack(destructuringAssignmentHelper(paramInt, (Node)localObject, localNode, this.currentScriptOrFn.getNextTempName()));
/*      */       }
/*      */ 
/* 3642 */       bool = false;
/*      */     }
/* 3644 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Node createName(String paramString) {
/* 3648 */     checkActivationName(paramString, 39);
/* 3649 */     return Node.newString(39, paramString);
/*      */   }
/*      */ 
/*      */   protected Node createName(int paramInt, String paramString, Node paramNode) {
/* 3653 */     Node localNode = createName(paramString);
/* 3654 */     localNode.setType(paramInt);
/* 3655 */     if (paramNode != null)
/* 3656 */       localNode.addChildToBack(paramNode);
/* 3657 */     return localNode;
/*      */   }
/*      */ 
/*      */   protected Node createNumber(double paramDouble) {
/* 3661 */     return Node.newNumber(paramDouble);
/*      */   }
/*      */ 
/*      */   protected Scope createScopeNode(int paramInt1, int paramInt2)
/*      */   {
/* 3673 */     Scope localScope = new Scope();
/* 3674 */     localScope.setType(paramInt1);
/* 3675 */     localScope.setLineno(paramInt2);
/* 3676 */     return localScope;
/*      */   }
/*      */ 
/*      */   protected Node simpleAssignment(Node paramNode1, Node paramNode2)
/*      */   {
/* 3702 */     int i = paramNode1.getType();
/*      */     Object localObject1;
/* 3703 */     switch (i) {
/*      */     case 39:
/* 3705 */       if ((this.inUseStrictDirective) && ("eval".equals(((Name)paramNode1).getIdentifier())))
/*      */       {
/* 3708 */         reportError("msg.bad.id.strict", ((Name)paramNode1).getIdentifier());
/*      */       }
/*      */ 
/* 3711 */       paramNode1.setType(49);
/* 3712 */       return new Node(8, paramNode1, paramNode2);
/*      */     case 33:
/*      */     case 36:
/*      */       Object localObject2;
/* 3721 */       if ((paramNode1 instanceof PropertyGet)) {
/* 3722 */         localObject1 = ((PropertyGet)paramNode1).getTarget();
/* 3723 */         localObject2 = ((PropertyGet)paramNode1).getProperty();
/* 3724 */       } else if ((paramNode1 instanceof ElementGet)) {
/* 3725 */         localObject1 = ((ElementGet)paramNode1).getTarget();
/* 3726 */         localObject2 = ((ElementGet)paramNode1).getElement();
/*      */       }
/*      */       else {
/* 3729 */         localObject1 = paramNode1.getFirstChild();
/* 3730 */         localObject2 = paramNode1.getLastChild();
/*      */       }
/*      */       int j;
/* 3733 */       if (i == 33) {
/* 3734 */         j = 35;
/*      */ 
/* 3740 */         ((Node)localObject2).setType(41);
/*      */       } else {
/* 3742 */         j = 37;
/*      */       }
/* 3744 */       return new Node(j, (Node)localObject1, (Node)localObject2, paramNode2);
/*      */     case 67:
/* 3747 */       localObject1 = paramNode1.getFirstChild();
/* 3748 */       checkMutableReference((Node)localObject1);
/* 3749 */       return new Node(68, (Node)localObject1, paramNode2);
/*      */     }
/*      */ 
/* 3753 */     throw codeBug();
/*      */   }
/*      */ 
/*      */   protected void checkMutableReference(Node paramNode) {
/* 3757 */     int i = paramNode.getIntProp(16, 0);
/* 3758 */     if ((i & 0x4) != 0)
/* 3759 */       reportError("msg.bad.assign.left");
/*      */   }
/*      */ 
/*      */   protected AstNode removeParens(AstNode paramAstNode)
/*      */   {
/* 3765 */     while ((paramAstNode instanceof ParenthesizedExpression)) {
/* 3766 */       paramAstNode = ((ParenthesizedExpression)paramAstNode).getExpression();
/*      */     }
/* 3768 */     return paramAstNode;
/*      */   }
/*      */ 
/*      */   void markDestructuring(AstNode paramAstNode) {
/* 3772 */     if ((paramAstNode instanceof DestructuringForm))
/* 3773 */       ((DestructuringForm)paramAstNode).setIsDestructuring(true);
/* 3774 */     else if ((paramAstNode instanceof ParenthesizedExpression))
/* 3775 */       markDestructuring(((ParenthesizedExpression)paramAstNode).getExpression());
/*      */   }
/*      */ 
/*      */   private RuntimeException codeBug()
/*      */     throws RuntimeException
/*      */   {
/* 3783 */     throw Kit.codeBug("ts.cursor=" + this.ts.cursor + ", ts.tokenBeg=" + this.ts.tokenBeg + ", currentToken=" + this.currentToken);
/*      */   }
/*      */ 
/*      */   private static class ConditionData
/*      */   {
/*      */     AstNode condition;
/*  912 */     int lp = -1;
/*  913 */     int rp = -1;
/*      */   }
/*      */ 
/*      */   private static class ParserException extends RuntimeException
/*      */   {
/*      */     static final long serialVersionUID = 5882582646773765630L;
/*      */   }
/*      */ 
/*      */   protected class PerFunctionVariables
/*      */   {
/* 3450 */     private ScriptNode savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;
/*      */     private Scope savedCurrentScope;
/*      */     private int savedNestingOfWith;
/*      */     private int savedEndFlags;
/*      */     private boolean savedInForInit;
/*      */     private Map<String, LabeledStatement> savedLabelSet;
/*      */     private List<Loop> savedLoopSet;
/*      */     private List<Jump> savedLoopAndSwitchSet;
/*      */ 
/*      */     PerFunctionVariables(FunctionNode arg2)
/*      */     {
/*      */       Object localObject;
/* 3451 */       Parser.this.currentScriptOrFn = localObject;
/*      */ 
/* 3453 */       this.savedCurrentScope = Parser.this.currentScope;
/* 3454 */       Parser.this.currentScope = localObject;
/*      */ 
/* 3456 */       this.savedNestingOfWith = Parser.this.nestingOfWith;
/* 3457 */       Parser.this.nestingOfWith = 0;
/*      */ 
/* 3459 */       this.savedLabelSet = Parser.this.labelSet;
/* 3460 */       Parser.this.labelSet = null;
/*      */ 
/* 3462 */       this.savedLoopSet = Parser.this.loopSet;
/* 3463 */       Parser.this.loopSet = null;
/*      */ 
/* 3465 */       this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
/* 3466 */       Parser.this.loopAndSwitchSet = null;
/*      */ 
/* 3468 */       this.savedEndFlags = Parser.this.endFlags;
/* 3469 */       Parser.this.endFlags = 0;
/*      */ 
/* 3471 */       this.savedInForInit = Parser.this.inForInit;
/* 3472 */       Parser.this.inForInit = false;
/*      */     }
/*      */ 
/*      */     void restore() {
/* 3476 */       Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
/* 3477 */       Parser.this.currentScope = this.savedCurrentScope;
/* 3478 */       Parser.this.nestingOfWith = this.savedNestingOfWith;
/* 3479 */       Parser.this.labelSet = this.savedLabelSet;
/* 3480 */       Parser.this.loopSet = this.savedLoopSet;
/* 3481 */       Parser.this.loopAndSwitchSet = this.savedLoopAndSwitchSet;
/* 3482 */       Parser.this.endFlags = this.savedEndFlags;
/* 3483 */       Parser.this.inForInit = this.savedInForInit;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Parser
 * JD-Core Version:    0.6.2
 */