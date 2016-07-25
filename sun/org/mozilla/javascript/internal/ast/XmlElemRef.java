/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class XmlElemRef extends XmlRef
/*     */ {
/*     */   private AstNode indexExpr;
/*  69 */   private int lb = -1;
/*  70 */   private int rb = -1;
/*     */ 
/*     */   public XmlElemRef() {
/*  73 */     this.type = 77;
/*     */   }
/*     */ 
/*     */   public XmlElemRef(int paramInt)
/*     */   {
/*  80 */     super(paramInt);
/*     */ 
/*  73 */     this.type = 77;
/*     */   }
/*     */ 
/*     */   public XmlElemRef(int paramInt1, int paramInt2)
/*     */   {
/*  84 */     super(paramInt1, paramInt2);
/*     */ 
/*  73 */     this.type = 77;
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  92 */     return this.indexExpr;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/* 100 */     assertNotNull(paramAstNode);
/* 101 */     this.indexExpr = paramAstNode;
/* 102 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLb()
/*     */   {
/* 109 */     return this.lb;
/*     */   }
/*     */ 
/*     */   public void setLb(int paramInt)
/*     */   {
/* 116 */     this.lb = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRb()
/*     */   {
/* 123 */     return this.rb;
/*     */   }
/*     */ 
/*     */   public void setRb(int paramInt)
/*     */   {
/* 130 */     this.rb = paramInt;
/*     */   }
/*     */ 
/*     */   public void setBrackets(int paramInt1, int paramInt2)
/*     */   {
/* 137 */     this.lb = paramInt1;
/* 138 */     this.rb = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 143 */     StringBuilder localStringBuilder = new StringBuilder();
/* 144 */     localStringBuilder.append(makeIndent(paramInt));
/* 145 */     if (isAttributeAccess()) {
/* 146 */       localStringBuilder.append("@");
/*     */     }
/* 148 */     if (this.namespace != null) {
/* 149 */       localStringBuilder.append(this.namespace.toSource(0));
/* 150 */       localStringBuilder.append("::");
/*     */     }
/* 152 */     localStringBuilder.append("[");
/* 153 */     localStringBuilder.append(this.indexExpr.toSource(0));
/* 154 */     localStringBuilder.append("]");
/* 155 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 164 */     if (paramNodeVisitor.visit(this)) {
/* 165 */       if (this.namespace != null) {
/* 166 */         this.namespace.visit(paramNodeVisitor);
/*     */       }
/* 168 */       this.indexExpr.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlElemRef
 * JD-Core Version:    0.6.2
 */