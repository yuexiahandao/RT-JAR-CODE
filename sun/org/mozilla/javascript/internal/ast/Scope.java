/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ 
/*     */ public class Scope extends Jump
/*     */ {
/*     */   protected Map<String, Symbol> symbolTable;
/*     */   protected Scope parentScope;
/*     */   protected ScriptNode top;
/*     */   private List<Scope> childScopes;
/*     */ 
/*     */   public Scope()
/*     */   {
/*  67 */     this.type = 129; } 
/*  67 */   public Scope(int paramInt) { this.type = 129;
/*     */ 
/*  74 */     this.position = paramInt; }
/*     */ 
/*     */   public Scope(int paramInt1, int paramInt2)
/*     */   {
/*  78 */     this(paramInt1);
/*  79 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public Scope getParentScope() {
/*  83 */     return this.parentScope;
/*     */   }
/*     */ 
/*     */   public void setParentScope(Scope paramScope)
/*     */   {
/*  90 */     this.parentScope = paramScope;
/*  91 */     this.top = (paramScope == null ? (ScriptNode)this : paramScope.top);
/*     */   }
/*     */ 
/*     */   public void clearParentScope()
/*     */   {
/*  98 */     this.parentScope = null;
/*     */   }
/*     */ 
/*     */   public List<Scope> getChildScopes()
/*     */   {
/* 106 */     return this.childScopes;
/*     */   }
/*     */ 
/*     */   public void addChildScope(Scope paramScope)
/*     */   {
/* 116 */     if (this.childScopes == null) {
/* 117 */       this.childScopes = new ArrayList();
/*     */     }
/* 119 */     this.childScopes.add(paramScope);
/* 120 */     paramScope.setParentScope(this);
/*     */   }
/*     */ 
/*     */   public void replaceWith(Scope paramScope)
/*     */   {
/* 133 */     if (this.childScopes != null) {
/* 134 */       for (Scope localScope : this.childScopes) {
/* 135 */         paramScope.addChildScope(localScope);
/*     */       }
/* 137 */       this.childScopes.clear();
/* 138 */       this.childScopes = null;
/*     */     }
/* 140 */     if ((this.symbolTable != null) && (!this.symbolTable.isEmpty()))
/* 141 */       joinScopes(this, paramScope);
/*     */   }
/*     */ 
/*     */   public ScriptNode getTop()
/*     */   {
/* 149 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void setTop(ScriptNode paramScriptNode)
/*     */   {
/* 156 */     this.top = paramScriptNode;
/*     */   }
/*     */ 
/*     */   public static Scope splitScope(Scope paramScope)
/*     */   {
/* 166 */     Scope localScope = new Scope(paramScope.getType());
/* 167 */     localScope.symbolTable = paramScope.symbolTable;
/* 168 */     paramScope.symbolTable = null;
/* 169 */     localScope.parent = paramScope.parent;
/* 170 */     localScope.setParentScope(paramScope.getParentScope());
/* 171 */     localScope.setParentScope(localScope);
/* 172 */     paramScope.parent = localScope;
/* 173 */     localScope.top = paramScope.top;
/* 174 */     return localScope;
/*     */   }
/*     */ 
/*     */   public static void joinScopes(Scope paramScope1, Scope paramScope2)
/*     */   {
/* 181 */     Map localMap1 = paramScope1.ensureSymbolTable();
/* 182 */     Map localMap2 = paramScope2.ensureSymbolTable();
/* 183 */     if (!Collections.disjoint(localMap1.keySet(), localMap2.keySet())) {
/* 184 */       codeBug();
/*     */     }
/* 186 */     for (Map.Entry localEntry : localMap1.entrySet()) {
/* 187 */       Symbol localSymbol = (Symbol)localEntry.getValue();
/* 188 */       localSymbol.setContainingTable(paramScope2);
/* 189 */       localMap2.put(localEntry.getKey(), localSymbol);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Scope getDefiningScope(String paramString)
/*     */   {
/* 200 */     for (Scope localScope = this; localScope != null; localScope = localScope.parentScope) {
/* 201 */       Map localMap = localScope.getSymbolTable();
/* 202 */       if ((localMap != null) && (localMap.containsKey(paramString))) {
/* 203 */         return localScope;
/*     */       }
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public Symbol getSymbol(String paramString)
/*     */   {
/* 215 */     return this.symbolTable == null ? null : (Symbol)this.symbolTable.get(paramString);
/*     */   }
/*     */ 
/*     */   public void putSymbol(Symbol paramSymbol)
/*     */   {
/* 222 */     if (paramSymbol.getName() == null)
/* 223 */       throw new IllegalArgumentException("null symbol name");
/* 224 */     ensureSymbolTable();
/* 225 */     this.symbolTable.put(paramSymbol.getName(), paramSymbol);
/* 226 */     paramSymbol.setContainingTable(this);
/* 227 */     this.top.addSymbol(paramSymbol);
/*     */   }
/*     */ 
/*     */   public Map<String, Symbol> getSymbolTable()
/*     */   {
/* 235 */     return this.symbolTable;
/*     */   }
/*     */ 
/*     */   public void setSymbolTable(Map<String, Symbol> paramMap)
/*     */   {
/* 242 */     this.symbolTable = paramMap;
/*     */   }
/*     */ 
/*     */   private Map<String, Symbol> ensureSymbolTable() {
/* 246 */     if (this.symbolTable == null) {
/* 247 */       this.symbolTable = new LinkedHashMap(5);
/*     */     }
/* 249 */     return this.symbolTable;
/*     */   }
/*     */ 
/*     */   public List<AstNode> getStatements()
/*     */   {
/* 260 */     ArrayList localArrayList = new ArrayList();
/* 261 */     Node localNode = getFirstChild();
/* 262 */     while (localNode != null) {
/* 263 */       localArrayList.add((AstNode)localNode);
/* 264 */       localNode = localNode.getNext();
/*     */     }
/* 266 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 271 */     StringBuilder localStringBuilder = new StringBuilder();
/* 272 */     localStringBuilder.append(makeIndent(paramInt));
/* 273 */     localStringBuilder.append("{\n");
/* 274 */     for (Node localNode : this) {
/* 275 */       localStringBuilder.append(((AstNode)localNode).toSource(paramInt + 1));
/*     */     }
/* 277 */     localStringBuilder.append(makeIndent(paramInt));
/* 278 */     localStringBuilder.append("}\n");
/* 279 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 284 */     if (paramNodeVisitor.visit(this))
/* 285 */       for (Node localNode : this)
/* 286 */         ((AstNode)localNode).visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Scope
 * JD-Core Version:    0.6.2
 */