/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class PropertyGet extends InfixExpression
/*     */ {
/*     */   public PropertyGet()
/*     */   {
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public PropertyGet(int paramInt)
/*     */   {
/*  56 */     super(paramInt);
/*     */ 
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public PropertyGet(int paramInt1, int paramInt2)
/*     */   {
/*  60 */     super(paramInt1, paramInt2);
/*     */ 
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public PropertyGet(int paramInt1, int paramInt2, AstNode paramAstNode, Name paramName)
/*     */   {
/*  64 */     super(paramInt1, paramInt2, paramAstNode, paramName);
/*     */ 
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public PropertyGet(AstNode paramAstNode, Name paramName)
/*     */   {
/*  72 */     super(paramAstNode, paramName);
/*     */ 
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public PropertyGet(AstNode paramAstNode, Name paramName, int paramInt)
/*     */   {
/*  76 */     super(33, paramAstNode, paramName, paramInt);
/*     */ 
/*  49 */     this.type = 33;
/*     */   }
/*     */ 
/*     */   public AstNode getTarget()
/*     */   {
/*  84 */     return getLeft();
/*     */   }
/*     */ 
/*     */   public void setTarget(AstNode paramAstNode)
/*     */   {
/*  94 */     setLeft(paramAstNode);
/*     */   }
/*     */ 
/*     */   public Name getProperty()
/*     */   {
/* 101 */     return (Name)getRight();
/*     */   }
/*     */ 
/*     */   public void setProperty(Name paramName)
/*     */   {
/* 109 */     setRight(paramName);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 114 */     StringBuilder localStringBuilder = new StringBuilder();
/* 115 */     localStringBuilder.append(makeIndent(paramInt));
/* 116 */     localStringBuilder.append(getLeft().toSource(0));
/* 117 */     localStringBuilder.append(".");
/* 118 */     localStringBuilder.append(getRight().toSource(0));
/* 119 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 127 */     if (paramNodeVisitor.visit(this)) {
/* 128 */       getTarget().visit(paramNodeVisitor);
/* 129 */       getProperty().visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.PropertyGet
 * JD-Core Version:    0.6.2
 */