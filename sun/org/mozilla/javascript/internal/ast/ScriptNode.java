/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ 
/*     */ public class ScriptNode extends Scope
/*     */ {
/*  57 */   private int encodedSourceStart = -1;
/*  58 */   private int encodedSourceEnd = -1;
/*     */   private String sourceName;
/*     */   private String encodedSource;
/*  61 */   private int endLineno = -1;
/*     */   private List<FunctionNode> functions;
/*     */   private List<RegExpLiteral> regexps;
/*  65 */   private List<FunctionNode> EMPTY_LIST = Collections.emptyList();
/*     */ 
/*  67 */   private List<Symbol> symbols = new ArrayList(4);
/*  68 */   private int paramCount = 0;
/*     */   private String[] variableNames;
/*     */   private boolean[] isConsts;
/*     */   private Object compilerData;
/*  73 */   private int tempNumber = 0;
/*     */ 
/*     */   public ScriptNode()
/*     */   {
/*  77 */     this.top = this;
/*  78 */     this.type = 136;
/*     */   }
/*     */ 
/*     */   public ScriptNode(int paramInt)
/*     */   {
/*  85 */     super(paramInt);
/*     */ 
/*  77 */     this.top = this;
/*  78 */     this.type = 136;
/*     */   }
/*     */ 
/*     */   public String getSourceName()
/*     */   {
/*  93 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */   public void setSourceName(String paramString)
/*     */   {
/* 101 */     this.sourceName = paramString;
/*     */   }
/*     */ 
/*     */   public int getEncodedSourceStart()
/*     */   {
/* 109 */     return this.encodedSourceStart;
/*     */   }
/*     */ 
/*     */   public void setEncodedSourceStart(int paramInt)
/*     */   {
/* 117 */     this.encodedSourceStart = paramInt;
/*     */   }
/*     */ 
/*     */   public int getEncodedSourceEnd()
/*     */   {
/* 125 */     return this.encodedSourceEnd;
/*     */   }
/*     */ 
/*     */   public void setEncodedSourceEnd(int paramInt)
/*     */   {
/* 133 */     this.encodedSourceEnd = paramInt;
/*     */   }
/*     */ 
/*     */   public void setEncodedSourceBounds(int paramInt1, int paramInt2)
/*     */   {
/* 141 */     this.encodedSourceStart = paramInt1;
/* 142 */     this.encodedSourceEnd = paramInt2;
/*     */   }
/*     */ 
/*     */   public void setEncodedSource(String paramString)
/*     */   {
/* 150 */     this.encodedSource = paramString;
/*     */   }
/*     */ 
/*     */   public String getEncodedSource()
/*     */   {
/* 169 */     return this.encodedSource;
/*     */   }
/*     */ 
/*     */   public int getBaseLineno() {
/* 173 */     return this.lineno;
/*     */   }
/*     */ 
/*     */   public void setBaseLineno(int paramInt)
/*     */   {
/* 182 */     if ((paramInt < 0) || (this.lineno >= 0)) codeBug();
/* 183 */     this.lineno = paramInt;
/*     */   }
/*     */ 
/*     */   public int getEndLineno() {
/* 187 */     return this.endLineno;
/*     */   }
/*     */ 
/*     */   public void setEndLineno(int paramInt)
/*     */   {
/* 192 */     if ((paramInt < 0) || (this.endLineno >= 0)) codeBug();
/* 193 */     this.endLineno = paramInt;
/*     */   }
/*     */ 
/*     */   public int getFunctionCount() {
/* 197 */     return this.functions == null ? 0 : this.functions.size();
/*     */   }
/*     */ 
/*     */   public FunctionNode getFunctionNode(int paramInt) {
/* 201 */     return (FunctionNode)this.functions.get(paramInt);
/*     */   }
/*     */ 
/*     */   public List<FunctionNode> getFunctions() {
/* 205 */     return this.functions == null ? this.EMPTY_LIST : this.functions;
/*     */   }
/*     */ 
/*     */   public int addFunction(FunctionNode paramFunctionNode)
/*     */   {
/* 214 */     if (paramFunctionNode == null) codeBug();
/* 215 */     if (this.functions == null)
/* 216 */       this.functions = new ArrayList();
/* 217 */     this.functions.add(paramFunctionNode);
/* 218 */     return this.functions.size() - 1;
/*     */   }
/*     */ 
/*     */   public int getRegexpCount() {
/* 222 */     return this.regexps == null ? 0 : this.regexps.size();
/*     */   }
/*     */ 
/*     */   public String getRegexpString(int paramInt) {
/* 226 */     return ((RegExpLiteral)this.regexps.get(paramInt)).getValue();
/*     */   }
/*     */ 
/*     */   public String getRegexpFlags(int paramInt) {
/* 230 */     return ((RegExpLiteral)this.regexps.get(paramInt)).getFlags();
/*     */   }
/*     */ 
/*     */   public void addRegExp(RegExpLiteral paramRegExpLiteral)
/*     */   {
/* 237 */     if (paramRegExpLiteral == null) codeBug();
/* 238 */     if (this.regexps == null)
/* 239 */       this.regexps = new ArrayList();
/* 240 */     this.regexps.add(paramRegExpLiteral);
/* 241 */     paramRegExpLiteral.putIntProp(4, this.regexps.size() - 1);
/*     */   }
/*     */ 
/*     */   public int getIndexForNameNode(Node paramNode) {
/* 245 */     if (this.variableNames == null) codeBug();
/* 246 */     Scope localScope = paramNode.getScope();
/* 247 */     Symbol localSymbol = localScope == null ? null : localScope.getSymbol(((Name)paramNode).getIdentifier());
/*     */ 
/* 250 */     return localSymbol == null ? -1 : localSymbol.getIndex();
/*     */   }
/*     */ 
/*     */   public String getParamOrVarName(int paramInt) {
/* 254 */     if (this.variableNames == null) codeBug();
/* 255 */     return this.variableNames[paramInt];
/*     */   }
/*     */ 
/*     */   public int getParamCount() {
/* 259 */     return this.paramCount;
/*     */   }
/*     */ 
/*     */   public int getParamAndVarCount() {
/* 263 */     if (this.variableNames == null) codeBug();
/* 264 */     return this.symbols.size();
/*     */   }
/*     */ 
/*     */   public String[] getParamAndVarNames() {
/* 268 */     if (this.variableNames == null) codeBug();
/* 269 */     return this.variableNames;
/*     */   }
/*     */ 
/*     */   public boolean[] getParamAndVarConst() {
/* 273 */     if (this.variableNames == null) codeBug();
/* 274 */     return this.isConsts;
/*     */   }
/*     */ 
/*     */   void addSymbol(Symbol paramSymbol) {
/* 278 */     if (this.variableNames != null) codeBug();
/* 279 */     if (paramSymbol.getDeclType() == 87) {
/* 280 */       this.paramCount += 1;
/*     */     }
/* 282 */     this.symbols.add(paramSymbol);
/*     */   }
/*     */ 
/*     */   public List<Symbol> getSymbols() {
/* 286 */     return this.symbols;
/*     */   }
/*     */ 
/*     */   public void setSymbols(List<Symbol> paramList) {
/* 290 */     this.symbols = paramList;
/*     */   }
/*     */ 
/*     */   public void flattenSymbolTable(boolean paramBoolean)
/*     */   {
/* 302 */     if (!paramBoolean) {
/* 303 */       ArrayList localArrayList = new ArrayList();
/* 304 */       if (this.symbolTable != null)
/*     */       {
/* 308 */         for (int j = 0; j < this.symbols.size(); j++) {
/* 309 */           Symbol localSymbol2 = (Symbol)this.symbols.get(j);
/* 310 */           if (localSymbol2.getContainingTable() == this) {
/* 311 */             localArrayList.add(localSymbol2);
/*     */           }
/*     */         }
/*     */       }
/* 315 */       this.symbols = localArrayList;
/*     */     }
/* 317 */     this.variableNames = new String[this.symbols.size()];
/* 318 */     this.isConsts = new boolean[this.symbols.size()];
/* 319 */     for (int i = 0; i < this.symbols.size(); i++) {
/* 320 */       Symbol localSymbol1 = (Symbol)this.symbols.get(i);
/* 321 */       this.variableNames[i] = localSymbol1.getName();
/* 322 */       this.isConsts[i] = (localSymbol1.getDeclType() == 154 ? 1 : false);
/* 323 */       localSymbol1.setIndex(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getCompilerData() {
/* 328 */     return this.compilerData;
/*     */   }
/*     */ 
/*     */   public void setCompilerData(Object paramObject) {
/* 332 */     assertNotNull(paramObject);
/*     */ 
/* 334 */     if (this.compilerData != null)
/* 335 */       throw new IllegalStateException();
/* 336 */     this.compilerData = paramObject;
/*     */   }
/*     */ 
/*     */   public String getNextTempName() {
/* 340 */     return "$" + this.tempNumber++;
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 345 */     if (paramNodeVisitor.visit(this))
/* 346 */       for (Node localNode : this)
/* 347 */         ((AstNode)localNode).visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ScriptNode
 * JD-Core Version:    0.6.2
 */