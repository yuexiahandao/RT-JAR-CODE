/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class WhileLoop extends Loop
/*     */ {
/*     */   private AstNode condition;
/*     */ 
/*     */   public WhileLoop()
/*     */   {
/*  54 */     this.type = 117;
/*     */   }
/*     */ 
/*     */   public WhileLoop(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */ 
/*  54 */     this.type = 117;
/*     */   }
/*     */ 
/*     */   public WhileLoop(int paramInt1, int paramInt2)
/*     */   {
/*  65 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 117;
/*     */   }
/*     */ 
/*     */   public AstNode getCondition()
/*     */   {
/*  72 */     return this.condition;
/*     */   }
/*     */ 
/*     */   public void setCondition(AstNode paramAstNode)
/*     */   {
/*  80 */     assertNotNull(paramAstNode);
/*  81 */     this.condition = paramAstNode;
/*  82 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  87 */     StringBuilder localStringBuilder = new StringBuilder();
/*  88 */     localStringBuilder.append(makeIndent(paramInt));
/*  89 */     localStringBuilder.append("while (");
/*  90 */     localStringBuilder.append(this.condition.toSource(0));
/*  91 */     localStringBuilder.append(") ");
/*  92 */     if ((this.body instanceof Block)) {
/*  93 */       localStringBuilder.append(this.body.toSource(paramInt).trim());
/*  94 */       localStringBuilder.append("\n");
/*     */     } else {
/*  96 */       localStringBuilder.append("\n").append(this.body.toSource(paramInt + 1));
/*     */     }
/*  98 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 106 */     if (paramNodeVisitor.visit(this)) {
/* 107 */       this.condition.visit(paramNodeVisitor);
/* 108 */       this.body.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.WhileLoop
 * JD-Core Version:    0.6.2
 */