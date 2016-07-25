/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ParenthesizedExpression extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */ 
/*     */   public ParenthesizedExpression()
/*     */   {
/*  52 */     this.type = 87;
/*     */   }
/*     */ 
/*     */   public ParenthesizedExpression(int paramInt)
/*     */   {
/*  59 */     super(paramInt);
/*     */ 
/*  52 */     this.type = 87;
/*     */   }
/*     */ 
/*     */   public ParenthesizedExpression(int paramInt1, int paramInt2)
/*     */   {
/*  63 */     super(paramInt1, paramInt2);
/*     */ 
/*  52 */     this.type = 87;
/*     */   }
/*     */ 
/*     */   public ParenthesizedExpression(AstNode paramAstNode)
/*     */   {
/*  67 */     this(paramAstNode != null ? paramAstNode.getPosition() : 0, paramAstNode != null ? paramAstNode.getLength() : 1, paramAstNode);
/*     */   }
/*     */ 
/*     */   public ParenthesizedExpression(int paramInt1, int paramInt2, AstNode paramAstNode)
/*     */   {
/*  73 */     super(paramInt1, paramInt2);
/*     */ 
/*  52 */     this.type = 87;
/*     */ 
/*  74 */     setExpression(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  81 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/*  91 */     assertNotNull(paramAstNode);
/*  92 */     this.expression = paramAstNode;
/*  93 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  98 */     return makeIndent(paramInt) + "(" + this.expression.toSource(0) + ")";
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 106 */     if (paramNodeVisitor.visit(this))
/* 107 */       this.expression.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ParenthesizedExpression
 * JD-Core Version:    0.6.2
 */