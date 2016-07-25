/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class LetNode extends Scope
/*     */ {
/*     */   private VariableDeclaration variables;
/*     */   private AstNode body;
/*  61 */   private int lp = -1;
/*  62 */   private int rp = -1;
/*     */ 
/*     */   public LetNode() {
/*  65 */     this.type = 158;
/*     */   }
/*     */ 
/*     */   public LetNode(int paramInt)
/*     */   {
/*  72 */     super(paramInt);
/*     */ 
/*  65 */     this.type = 158;
/*     */   }
/*     */ 
/*     */   public LetNode(int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramInt1, paramInt2);
/*     */ 
/*  65 */     this.type = 158;
/*     */   }
/*     */ 
/*     */   public VariableDeclaration getVariables()
/*     */   {
/*  83 */     return this.variables;
/*     */   }
/*     */ 
/*     */   public void setVariables(VariableDeclaration paramVariableDeclaration)
/*     */   {
/*  91 */     assertNotNull(paramVariableDeclaration);
/*  92 */     this.variables = paramVariableDeclaration;
/*  93 */     paramVariableDeclaration.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getBody()
/*     */   {
/* 105 */     return this.body;
/*     */   }
/*     */ 
/*     */   public void setBody(AstNode paramAstNode)
/*     */   {
/* 115 */     this.body = paramAstNode;
/* 116 */     if (paramAstNode != null)
/* 117 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 124 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 131 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 138 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 145 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 152 */     this.lp = paramInt1;
/* 153 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 158 */     String str = makeIndent(paramInt);
/* 159 */     StringBuilder localStringBuilder = new StringBuilder();
/* 160 */     localStringBuilder.append(str);
/* 161 */     localStringBuilder.append("let (");
/* 162 */     printList(this.variables.getVariables(), localStringBuilder);
/* 163 */     localStringBuilder.append(") ");
/* 164 */     if (this.body != null) {
/* 165 */       localStringBuilder.append(this.body.toSource(paramInt));
/*     */     }
/* 167 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 176 */     if (paramNodeVisitor.visit(this)) {
/* 177 */       this.variables.visit(paramNodeVisitor);
/* 178 */       if (this.body != null)
/* 179 */         this.body.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.LetNode
 * JD-Core Version:    0.6.2
 */