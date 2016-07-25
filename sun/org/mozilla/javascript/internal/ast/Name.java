/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class Name extends AstNode
/*     */ {
/*     */   private String identifier;
/*     */   private Scope scope;
/*     */ 
/*     */   public Name()
/*     */   {
/*  58 */     this.type = 39;
/*     */   }
/*     */ 
/*     */   public Name(int paramInt)
/*     */   {
/*  65 */     super(paramInt);
/*     */ 
/*  58 */     this.type = 39;
/*     */   }
/*     */ 
/*     */   public Name(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*     */ 
/*  58 */     this.type = 39;
/*     */   }
/*     */ 
/*     */   public Name(int paramInt1, int paramInt2, String paramString)
/*     */   {
/*  79 */     super(paramInt1, paramInt2);
/*     */ 
/*  58 */     this.type = 39;
/*     */ 
/*  80 */     setIdentifier(paramString);
/*     */   }
/*     */ 
/*     */   public Name(int paramInt, String paramString) {
/*  84 */     super(paramInt);
/*     */ 
/*  58 */     this.type = 39;
/*     */ 
/*  85 */     setIdentifier(paramString);
/*  86 */     setLength(paramString.length());
/*     */   }
/*     */ 
/*     */   public String getIdentifier()
/*     */   {
/*  93 */     return this.identifier;
/*     */   }
/*     */ 
/*     */   public void setIdentifier(String paramString)
/*     */   {
/* 101 */     assertNotNull(paramString);
/* 102 */     this.identifier = paramString;
/* 103 */     setLength(paramString.length());
/*     */   }
/*     */ 
/*     */   public void setScope(Scope paramScope)
/*     */   {
/* 117 */     this.scope = paramScope;
/*     */   }
/*     */ 
/*     */   public Scope getScope()
/*     */   {
/* 128 */     return this.scope;
/*     */   }
/*     */ 
/*     */   public Scope getDefiningScope()
/*     */   {
/* 137 */     Scope localScope = getEnclosingScope();
/* 138 */     String str = getIdentifier();
/* 139 */     return localScope == null ? null : localScope.getDefiningScope(str);
/*     */   }
/*     */ 
/*     */   public boolean isLocalName()
/*     */   {
/* 156 */     Scope localScope = getDefiningScope();
/* 157 */     return (localScope != null) && (localScope.getParentScope() != null);
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 167 */     return this.identifier == null ? 0 : this.identifier.length();
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 172 */     return makeIndent(paramInt) + (this.identifier == null ? "<null>" : this.identifier);
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 180 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Name
 * JD-Core Version:    0.6.2
 */