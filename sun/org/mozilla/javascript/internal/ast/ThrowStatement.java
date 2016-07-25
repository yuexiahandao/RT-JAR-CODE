/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ThrowStatement extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */ 
/*     */   public ThrowStatement()
/*     */   {
/*  54 */     this.type = 50;
/*     */   }
/*     */ 
/*     */   public ThrowStatement(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */ 
/*  54 */     this.type = 50;
/*     */   }
/*     */ 
/*     */   public ThrowStatement(int paramInt1, int paramInt2)
/*     */   {
/*  65 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 50; } 
/*  54 */   public ThrowStatement(AstNode paramAstNode) { this.type = 50;
/*     */ 
/*  69 */     setExpression(paramAstNode); }
/*     */ 
/*     */   public ThrowStatement(int paramInt, AstNode paramAstNode)
/*     */   {
/*  73 */     super(paramInt, paramAstNode.getLength());
/*     */ 
/*  54 */     this.type = 50;
/*     */ 
/*  74 */     setExpression(paramAstNode);
/*     */   }
/*     */ 
/*     */   public ThrowStatement(int paramInt1, int paramInt2, AstNode paramAstNode) {
/*  78 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 50;
/*     */ 
/*  79 */     setExpression(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  86 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/*  95 */     assertNotNull(paramAstNode);
/*  96 */     this.expression = paramAstNode;
/*  97 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 102 */     StringBuilder localStringBuilder = new StringBuilder();
/* 103 */     localStringBuilder.append(makeIndent(paramInt));
/* 104 */     localStringBuilder.append("throw");
/* 105 */     localStringBuilder.append(" ");
/* 106 */     localStringBuilder.append(this.expression.toSource(0));
/* 107 */     localStringBuilder.append(";\n");
/* 108 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 116 */     if (paramNodeVisitor.visit(this))
/* 117 */       this.expression.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ThrowStatement
 * JD-Core Version:    0.6.2
 */