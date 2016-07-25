/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class DoLoop extends Loop
/*     */ {
/*     */   private AstNode condition;
/*  52 */   private int whilePosition = -1;
/*     */ 
/*     */   public DoLoop() {
/*  55 */     this.type = 118;
/*     */   }
/*     */ 
/*     */   public DoLoop(int paramInt)
/*     */   {
/*  62 */     super(paramInt);
/*     */ 
/*  55 */     this.type = 118;
/*     */   }
/*     */ 
/*     */   public DoLoop(int paramInt1, int paramInt2)
/*     */   {
/*  66 */     super(paramInt1, paramInt2);
/*     */ 
/*  55 */     this.type = 118;
/*     */   }
/*     */ 
/*     */   public AstNode getCondition()
/*     */   {
/*  73 */     return this.condition;
/*     */   }
/*     */ 
/*     */   public void setCondition(AstNode paramAstNode)
/*     */   {
/*  81 */     assertNotNull(paramAstNode);
/*  82 */     this.condition = paramAstNode;
/*  83 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getWhilePosition()
/*     */   {
/*  90 */     return this.whilePosition;
/*     */   }
/*     */ 
/*     */   public void setWhilePosition(int paramInt)
/*     */   {
/*  97 */     this.whilePosition = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 102 */     StringBuilder localStringBuilder = new StringBuilder();
/* 103 */     localStringBuilder.append("do ");
/* 104 */     localStringBuilder.append(this.body.toSource(paramInt).trim());
/* 105 */     localStringBuilder.append(" while (");
/* 106 */     localStringBuilder.append(this.condition.toSource(0));
/* 107 */     localStringBuilder.append(");\n");
/* 108 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 116 */     if (paramNodeVisitor.visit(this)) {
/* 117 */       this.body.visit(paramNodeVisitor);
/* 118 */       this.condition.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.DoLoop
 * JD-Core Version:    0.6.2
 */