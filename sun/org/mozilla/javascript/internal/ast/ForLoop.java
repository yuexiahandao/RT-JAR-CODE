/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ForLoop extends Loop
/*     */ {
/*     */   private AstNode initializer;
/*     */   private AstNode condition;
/*     */   private AstNode increment;
/*     */ 
/*     */   public ForLoop()
/*     */   {
/*  57 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public ForLoop(int paramInt)
/*     */   {
/*  64 */     super(paramInt);
/*     */ 
/*  57 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public ForLoop(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     super(paramInt1, paramInt2);
/*     */ 
/*  57 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public AstNode getInitializer()
/*     */   {
/*  78 */     return this.initializer;
/*     */   }
/*     */ 
/*     */   public void setInitializer(AstNode paramAstNode)
/*     */   {
/*  90 */     assertNotNull(paramAstNode);
/*  91 */     this.initializer = paramAstNode;
/*  92 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getCondition()
/*     */   {
/*  99 */     return this.condition;
/*     */   }
/*     */ 
/*     */   public void setCondition(AstNode paramAstNode)
/*     */   {
/* 109 */     assertNotNull(paramAstNode);
/* 110 */     this.condition = paramAstNode;
/* 111 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getIncrement()
/*     */   {
/* 118 */     return this.increment;
/*     */   }
/*     */ 
/*     */   public void setIncrement(AstNode paramAstNode)
/*     */   {
/* 129 */     assertNotNull(paramAstNode);
/* 130 */     this.increment = paramAstNode;
/* 131 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 136 */     StringBuilder localStringBuilder = new StringBuilder();
/* 137 */     localStringBuilder.append(makeIndent(paramInt));
/* 138 */     localStringBuilder.append("for (");
/* 139 */     localStringBuilder.append(this.initializer.toSource(0));
/* 140 */     localStringBuilder.append("; ");
/* 141 */     localStringBuilder.append(this.condition.toSource(0));
/* 142 */     localStringBuilder.append("; ");
/* 143 */     localStringBuilder.append(this.increment.toSource(0));
/* 144 */     localStringBuilder.append(") ");
/* 145 */     if ((this.body instanceof Block))
/* 146 */       localStringBuilder.append(this.body.toSource(paramInt).trim()).append("\n");
/*     */     else {
/* 148 */       localStringBuilder.append("\n").append(this.body.toSource(paramInt + 1));
/*     */     }
/* 150 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 159 */     if (paramNodeVisitor.visit(this)) {
/* 160 */       this.initializer.visit(paramNodeVisitor);
/* 161 */       this.condition.visit(paramNodeVisitor);
/* 162 */       this.increment.visit(paramNodeVisitor);
/* 163 */       this.body.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ForLoop
 * JD-Core Version:    0.6.2
 */