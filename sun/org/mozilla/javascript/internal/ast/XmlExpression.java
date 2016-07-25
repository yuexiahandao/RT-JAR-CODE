/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class XmlExpression extends XmlFragment
/*     */ {
/*     */   private AstNode expression;
/*     */   private boolean isXmlAttribute;
/*     */ 
/*     */   public XmlExpression()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XmlExpression(int paramInt)
/*     */   {
/*  57 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public XmlExpression(int paramInt1, int paramInt2) {
/*  61 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public XmlExpression(int paramInt, AstNode paramAstNode) {
/*  65 */     super(paramInt);
/*  66 */     setExpression(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  73 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/*  81 */     assertNotNull(paramAstNode);
/*  82 */     this.expression = paramAstNode;
/*  83 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isXmlAttribute()
/*     */   {
/*  90 */     return this.isXmlAttribute;
/*     */   }
/*     */ 
/*     */   public void setIsXmlAttribute(boolean paramBoolean)
/*     */   {
/*  97 */     this.isXmlAttribute = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 102 */     return makeIndent(paramInt) + "{" + this.expression.toSource(paramInt) + "}";
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 110 */     if (paramNodeVisitor.visit(this))
/* 111 */       this.expression.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlExpression
 * JD-Core Version:    0.6.2
 */