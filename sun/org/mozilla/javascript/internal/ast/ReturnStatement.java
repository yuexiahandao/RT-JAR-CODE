/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ReturnStatement extends AstNode
/*     */ {
/*     */   private AstNode returnValue;
/*     */ 
/*     */   public ReturnStatement()
/*     */   {
/*  54 */     this.type = 4;
/*     */   }
/*     */ 
/*     */   public ReturnStatement(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */ 
/*  54 */     this.type = 4;
/*     */   }
/*     */ 
/*     */   public ReturnStatement(int paramInt1, int paramInt2)
/*     */   {
/*  65 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 4;
/*     */   }
/*     */ 
/*     */   public ReturnStatement(int paramInt1, int paramInt2, AstNode paramAstNode)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 4;
/*     */ 
/*  70 */     setReturnValue(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getReturnValue()
/*     */   {
/*  77 */     return this.returnValue;
/*     */   }
/*     */ 
/*     */   public void setReturnValue(AstNode paramAstNode)
/*     */   {
/*  85 */     this.returnValue = paramAstNode;
/*  86 */     if (paramAstNode != null)
/*  87 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  92 */     StringBuilder localStringBuilder = new StringBuilder();
/*  93 */     localStringBuilder.append(makeIndent(paramInt));
/*  94 */     localStringBuilder.append("return");
/*  95 */     if (this.returnValue != null) {
/*  96 */       localStringBuilder.append(" ");
/*  97 */       localStringBuilder.append(this.returnValue.toSource(0));
/*     */     }
/*  99 */     localStringBuilder.append(";\n");
/* 100 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 108 */     if ((paramNodeVisitor.visit(this)) && (this.returnValue != null))
/* 109 */       this.returnValue.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ReturnStatement
 * JD-Core Version:    0.6.2
 */