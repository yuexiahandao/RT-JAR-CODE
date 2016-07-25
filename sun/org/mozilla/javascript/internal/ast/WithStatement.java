/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class WithStatement extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */   private AstNode statement;
/*  53 */   private int lp = -1;
/*  54 */   private int rp = -1;
/*     */ 
/*     */   public WithStatement() {
/*  57 */     this.type = 123;
/*     */   }
/*     */ 
/*     */   public WithStatement(int paramInt)
/*     */   {
/*  64 */     super(paramInt);
/*     */ 
/*  57 */     this.type = 123;
/*     */   }
/*     */ 
/*     */   public WithStatement(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     super(paramInt1, paramInt2);
/*     */ 
/*  57 */     this.type = 123;
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  75 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/*  83 */     assertNotNull(paramAstNode);
/*  84 */     this.expression = paramAstNode;
/*  85 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getStatement()
/*     */   {
/*  92 */     return this.statement;
/*     */   }
/*     */ 
/*     */   public void setStatement(AstNode paramAstNode)
/*     */   {
/* 100 */     assertNotNull(paramAstNode);
/* 101 */     this.statement = paramAstNode;
/* 102 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 109 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 116 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 123 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 130 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 137 */     this.lp = paramInt1;
/* 138 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 143 */     StringBuilder localStringBuilder = new StringBuilder();
/* 144 */     localStringBuilder.append(makeIndent(paramInt));
/* 145 */     localStringBuilder.append("with (");
/* 146 */     localStringBuilder.append(this.expression.toSource(0));
/* 147 */     localStringBuilder.append(") ");
/* 148 */     localStringBuilder.append(this.statement.toSource(paramInt + 1));
/* 149 */     if (!(this.statement instanceof Block)) {
/* 150 */       localStringBuilder.append(";\n");
/*     */     }
/* 152 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 160 */     if (paramNodeVisitor.visit(this)) {
/* 161 */       this.expression.visit(paramNodeVisitor);
/* 162 */       this.statement.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.WithStatement
 * JD-Core Version:    0.6.2
 */