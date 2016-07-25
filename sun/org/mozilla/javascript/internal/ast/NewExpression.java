/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class NewExpression extends FunctionCall
/*     */ {
/*     */   private ObjectLiteral initializer;
/*     */ 
/*     */   public NewExpression()
/*     */   {
/*  59 */     this.type = 30;
/*     */   }
/*     */ 
/*     */   public NewExpression(int paramInt)
/*     */   {
/*  66 */     super(paramInt);
/*     */ 
/*  59 */     this.type = 30;
/*     */   }
/*     */ 
/*     */   public NewExpression(int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(paramInt1, paramInt2);
/*     */ 
/*  59 */     this.type = 30;
/*     */   }
/*     */ 
/*     */   public ObjectLiteral getInitializer()
/*     */   {
/*  79 */     return this.initializer;
/*     */   }
/*     */ 
/*     */   public void setInitializer(ObjectLiteral paramObjectLiteral)
/*     */   {
/*  92 */     this.initializer = paramObjectLiteral;
/*  93 */     if (paramObjectLiteral != null)
/*  94 */       paramObjectLiteral.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  99 */     StringBuilder localStringBuilder = new StringBuilder();
/* 100 */     localStringBuilder.append(makeIndent(paramInt));
/* 101 */     localStringBuilder.append("new ");
/* 102 */     localStringBuilder.append(this.target.toSource(0));
/* 103 */     localStringBuilder.append("(");
/* 104 */     if (this.arguments != null) {
/* 105 */       printList(this.arguments, localStringBuilder);
/*     */     }
/* 107 */     localStringBuilder.append(")");
/* 108 */     if (this.initializer != null) {
/* 109 */       localStringBuilder.append(" ");
/* 110 */       localStringBuilder.append(this.initializer.toSource(0));
/*     */     }
/* 112 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 121 */     if (paramNodeVisitor.visit(this)) {
/* 122 */       this.target.visit(paramNodeVisitor);
/* 123 */       for (AstNode localAstNode : getArguments()) {
/* 124 */         localAstNode.visit(paramNodeVisitor);
/*     */       }
/* 126 */       if (this.initializer != null)
/* 127 */         this.initializer.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.NewExpression
 * JD-Core Version:    0.6.2
 */