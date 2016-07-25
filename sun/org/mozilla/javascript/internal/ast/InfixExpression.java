/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Token;
/*     */ 
/*     */ public class InfixExpression extends AstNode
/*     */ {
/*     */   protected AstNode left;
/*     */   protected AstNode right;
/*  51 */   protected int operatorPosition = -1;
/*     */ 
/*     */   public InfixExpression() {
/*     */   }
/*     */ 
/*     */   public InfixExpression(int paramInt) {
/*  57 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public InfixExpression(int paramInt1, int paramInt2) {
/*  61 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public InfixExpression(int paramInt1, int paramInt2, AstNode paramAstNode1, AstNode paramAstNode2)
/*     */   {
/*  67 */     super(paramInt1, paramInt2);
/*  68 */     setLeft(paramAstNode1);
/*  69 */     setRight(paramAstNode2);
/*     */   }
/*     */ 
/*     */   public InfixExpression(AstNode paramAstNode1, AstNode paramAstNode2)
/*     */   {
/*  77 */     setLeftAndRight(paramAstNode1, paramAstNode2);
/*     */   }
/*     */ 
/*     */   public InfixExpression(int paramInt1, AstNode paramAstNode1, AstNode paramAstNode2, int paramInt2)
/*     */   {
/*  86 */     setType(paramInt1);
/*  87 */     setOperatorPosition(paramInt2 - paramAstNode1.getPosition());
/*  88 */     setLeftAndRight(paramAstNode1, paramAstNode2);
/*     */   }
/*     */ 
/*     */   public void setLeftAndRight(AstNode paramAstNode1, AstNode paramAstNode2) {
/*  92 */     assertNotNull(paramAstNode1);
/*  93 */     assertNotNull(paramAstNode2);
/*     */ 
/*  95 */     int i = paramAstNode1.getPosition();
/*  96 */     int j = paramAstNode2.getPosition() + paramAstNode2.getLength();
/*  97 */     setBounds(i, j);
/*     */ 
/*  99 */     setLeft(paramAstNode1);
/* 100 */     setRight(paramAstNode2);
/*     */   }
/*     */ 
/*     */   public int getOperator()
/*     */   {
/* 107 */     return getType();
/*     */   }
/*     */ 
/*     */   public void setOperator(int paramInt)
/*     */   {
/* 117 */     if (!Token.isValidToken(paramInt))
/* 118 */       throw new IllegalArgumentException("Invalid token: " + paramInt);
/* 119 */     setType(paramInt);
/*     */   }
/*     */ 
/*     */   public AstNode getLeft()
/*     */   {
/* 126 */     return this.left;
/*     */   }
/*     */ 
/*     */   public void setLeft(AstNode paramAstNode)
/*     */   {
/* 136 */     assertNotNull(paramAstNode);
/* 137 */     this.left = paramAstNode;
/* 138 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getRight()
/*     */   {
/* 148 */     return this.right;
/*     */   }
/*     */ 
/*     */   public void setRight(AstNode paramAstNode)
/*     */   {
/* 157 */     assertNotNull(paramAstNode);
/* 158 */     this.right = paramAstNode;
/* 159 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getOperatorPosition()
/*     */   {
/* 166 */     return this.operatorPosition;
/*     */   }
/*     */ 
/*     */   public void setOperatorPosition(int paramInt)
/*     */   {
/* 174 */     this.operatorPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean hasSideEffects()
/*     */   {
/* 180 */     switch (getType()) {
/*     */     case 89:
/* 182 */       return (this.right != null) && (this.right.hasSideEffects());
/*     */     case 104:
/*     */     case 105:
/* 185 */       return ((this.left != null) && (this.left.hasSideEffects())) || ((this.right != null) && (this.right.hasSideEffects()));
/*     */     }
/*     */ 
/* 188 */     return super.hasSideEffects();
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 194 */     StringBuilder localStringBuilder = new StringBuilder();
/* 195 */     localStringBuilder.append(makeIndent(paramInt));
/* 196 */     localStringBuilder.append(this.left.toSource());
/* 197 */     localStringBuilder.append(" ");
/* 198 */     localStringBuilder.append(operatorToString(getType()));
/* 199 */     localStringBuilder.append(" ");
/* 200 */     localStringBuilder.append(this.right.toSource());
/* 201 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 209 */     if (paramNodeVisitor.visit(this)) {
/* 210 */       this.left.visit(paramNodeVisitor);
/* 211 */       this.right.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.InfixExpression
 * JD-Core Version:    0.6.2
 */