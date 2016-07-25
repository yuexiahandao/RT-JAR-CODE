/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Token;
/*     */ 
/*     */ public class UnaryExpression extends AstNode
/*     */ {
/*     */   private AstNode operand;
/*     */   private boolean isPostfix;
/*     */ 
/*     */   public UnaryExpression()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UnaryExpression(int paramInt)
/*     */   {
/*  64 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public UnaryExpression(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public UnaryExpression(int paramInt1, int paramInt2, AstNode paramAstNode)
/*     */   {
/*  79 */     this(paramInt1, paramInt2, paramAstNode, false);
/*     */   }
/*     */ 
/*     */   public UnaryExpression(int paramInt1, int paramInt2, AstNode paramAstNode, boolean paramBoolean)
/*     */   {
/*  94 */     assertNotNull(paramAstNode);
/*  95 */     int i = paramBoolean ? paramAstNode.getPosition() : paramInt2;
/*     */ 
/*  97 */     int j = paramBoolean ? paramInt2 + 2 : paramAstNode.getPosition() + paramAstNode.getLength();
/*     */ 
/* 100 */     setBounds(i, j);
/* 101 */     setOperator(paramInt1);
/* 102 */     setOperand(paramAstNode);
/* 103 */     this.isPostfix = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getOperator()
/*     */   {
/* 110 */     return this.type;
/*     */   }
/*     */ 
/*     */   public void setOperator(int paramInt)
/*     */   {
/* 120 */     if (!Token.isValidToken(paramInt))
/* 121 */       throw new IllegalArgumentException("Invalid token: " + paramInt);
/* 122 */     setType(paramInt);
/*     */   }
/*     */ 
/*     */   public AstNode getOperand() {
/* 126 */     return this.operand;
/*     */   }
/*     */ 
/*     */   public void setOperand(AstNode paramAstNode)
/*     */   {
/* 134 */     assertNotNull(paramAstNode);
/* 135 */     this.operand = paramAstNode;
/* 136 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isPostfix()
/*     */   {
/* 143 */     return this.isPostfix;
/*     */   }
/*     */ 
/*     */   public boolean isPrefix()
/*     */   {
/* 150 */     return !this.isPostfix;
/*     */   }
/*     */ 
/*     */   public void setIsPostfix(boolean paramBoolean)
/*     */   {
/* 157 */     this.isPostfix = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 162 */     StringBuilder localStringBuilder = new StringBuilder();
/* 163 */     localStringBuilder.append(makeIndent(paramInt));
/* 164 */     if (!this.isPostfix) {
/* 165 */       localStringBuilder.append(operatorToString(getType()));
/* 166 */       if ((getType() == 32) || (getType() == 31))
/*     */       {
/* 168 */         localStringBuilder.append(" ");
/*     */       }
/*     */     }
/* 171 */     localStringBuilder.append(this.operand.toSource());
/* 172 */     if (this.isPostfix) {
/* 173 */       localStringBuilder.append(operatorToString(getType()));
/*     */     }
/* 175 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 183 */     if (paramNodeVisitor.visit(this))
/* 184 */       this.operand.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.UnaryExpression
 * JD-Core Version:    0.6.2
 */