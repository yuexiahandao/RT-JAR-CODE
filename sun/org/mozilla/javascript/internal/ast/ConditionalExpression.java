/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ConditionalExpression extends AstNode
/*     */ {
/*     */   private AstNode testExpression;
/*     */   private AstNode trueExpression;
/*     */   private AstNode falseExpression;
/*  62 */   private int questionMarkPosition = -1;
/*  63 */   private int colonPosition = -1;
/*     */ 
/*     */   public ConditionalExpression() {
/*  66 */     this.type = 102;
/*     */   }
/*     */ 
/*     */   public ConditionalExpression(int paramInt)
/*     */   {
/*  73 */     super(paramInt);
/*     */ 
/*  66 */     this.type = 102;
/*     */   }
/*     */ 
/*     */   public ConditionalExpression(int paramInt1, int paramInt2)
/*     */   {
/*  77 */     super(paramInt1, paramInt2);
/*     */ 
/*  66 */     this.type = 102;
/*     */   }
/*     */ 
/*     */   public AstNode getTestExpression()
/*     */   {
/*  84 */     return this.testExpression;
/*     */   }
/*     */ 
/*     */   public void setTestExpression(AstNode paramAstNode)
/*     */   {
/*  93 */     assertNotNull(paramAstNode);
/*  94 */     this.testExpression = paramAstNode;
/*  95 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getTrueExpression()
/*     */   {
/* 102 */     return this.trueExpression;
/*     */   }
/*     */ 
/*     */   public void setTrueExpression(AstNode paramAstNode)
/*     */   {
/* 112 */     assertNotNull(paramAstNode);
/* 113 */     this.trueExpression = paramAstNode;
/* 114 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getFalseExpression()
/*     */   {
/* 121 */     return this.falseExpression;
/*     */   }
/*     */ 
/*     */   public void setFalseExpression(AstNode paramAstNode)
/*     */   {
/* 132 */     assertNotNull(paramAstNode);
/* 133 */     this.falseExpression = paramAstNode;
/* 134 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getQuestionMarkPosition()
/*     */   {
/* 141 */     return this.questionMarkPosition;
/*     */   }
/*     */ 
/*     */   public void setQuestionMarkPosition(int paramInt)
/*     */   {
/* 149 */     this.questionMarkPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public int getColonPosition()
/*     */   {
/* 156 */     return this.colonPosition;
/*     */   }
/*     */ 
/*     */   public void setColonPosition(int paramInt)
/*     */   {
/* 164 */     this.colonPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean hasSideEffects()
/*     */   {
/* 169 */     if ((this.testExpression == null) || (this.trueExpression == null) || (this.falseExpression == null))
/*     */     {
/* 171 */       codeBug();
/* 172 */     }return (this.trueExpression.hasSideEffects()) && (this.falseExpression.hasSideEffects());
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 178 */     StringBuilder localStringBuilder = new StringBuilder();
/* 179 */     localStringBuilder.append(makeIndent(paramInt));
/* 180 */     localStringBuilder.append(this.testExpression.toSource(paramInt));
/* 181 */     localStringBuilder.append(" ? ");
/* 182 */     localStringBuilder.append(this.trueExpression.toSource(0));
/* 183 */     localStringBuilder.append(" : ");
/* 184 */     localStringBuilder.append(this.falseExpression.toSource(0));
/* 185 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 194 */     if (paramNodeVisitor.visit(this)) {
/* 195 */       this.testExpression.visit(paramNodeVisitor);
/* 196 */       this.trueExpression.visit(paramNodeVisitor);
/* 197 */       this.falseExpression.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ConditionalExpression
 * JD-Core Version:    0.6.2
 */