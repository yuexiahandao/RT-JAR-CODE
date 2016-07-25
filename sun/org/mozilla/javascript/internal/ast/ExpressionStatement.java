/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ExpressionStatement extends AstNode
/*     */ {
/*     */   private AstNode expr;
/*     */ 
/*     */   public void setHasResult()
/*     */   {
/*  61 */     this.type = 134;
/*     */   }
/*     */ 
/*     */   public ExpressionStatement()
/*     */   {
/*  53 */     this.type = 133;
/*     */   }
/*     */ 
/*     */   public ExpressionStatement(AstNode paramAstNode, boolean paramBoolean)
/*     */   {
/*  78 */     this(paramAstNode);
/*  79 */     if (paramBoolean) setHasResult();
/*     */   }
/*     */ 
/*     */   public ExpressionStatement(AstNode paramAstNode)
/*     */   {
/*  91 */     this(paramAstNode.getPosition(), paramAstNode.getLength(), paramAstNode);
/*     */   }
/*     */ 
/*     */   public ExpressionStatement(int paramInt1, int paramInt2) {
/*  95 */     super(paramInt1, paramInt2);
/*     */ 
/*  53 */     this.type = 133;
/*     */   }
/*     */ 
/*     */   public ExpressionStatement(int paramInt1, int paramInt2, AstNode paramAstNode)
/*     */   {
/* 106 */     super(paramInt1, paramInt2);
/*     */ 
/*  53 */     this.type = 133;
/*     */ 
/* 107 */     setExpression(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/* 114 */     return this.expr;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/* 122 */     assertNotNull(paramAstNode);
/* 123 */     this.expr = paramAstNode;
/* 124 */     paramAstNode.setParent(this);
/* 125 */     setLineno(paramAstNode.getLineno());
/*     */   }
/*     */ 
/*     */   public boolean hasSideEffects()
/*     */   {
/* 135 */     return (this.type == 134) || (this.expr.hasSideEffects());
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 140 */     StringBuilder localStringBuilder = new StringBuilder();
/* 141 */     localStringBuilder.append(this.expr.toSource(paramInt));
/* 142 */     localStringBuilder.append(";\n");
/* 143 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 151 */     if (paramNodeVisitor.visit(this))
/* 152 */       this.expr.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ExpressionStatement
 * JD-Core Version:    0.6.2
 */