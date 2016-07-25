/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ElementGet extends AstNode
/*     */ {
/*     */   private AstNode target;
/*     */   private AstNode element;
/*  58 */   private int lb = -1;
/*  59 */   private int rb = -1;
/*     */ 
/*     */   public ElementGet() {
/*  62 */     this.type = 36;
/*     */   }
/*     */ 
/*     */   public ElementGet(int paramInt)
/*     */   {
/*  69 */     super(paramInt);
/*     */ 
/*  62 */     this.type = 36;
/*     */   }
/*     */ 
/*     */   public ElementGet(int paramInt1, int paramInt2)
/*     */   {
/*  73 */     super(paramInt1, paramInt2);
/*     */ 
/*  62 */     this.type = 36; } 
/*  62 */   public ElementGet(AstNode paramAstNode1, AstNode paramAstNode2) { this.type = 36;
/*     */ 
/*  77 */     setTarget(paramAstNode1);
/*  78 */     setElement(paramAstNode2);
/*     */   }
/*     */ 
/*     */   public AstNode getTarget()
/*     */   {
/*  85 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setTarget(AstNode paramAstNode)
/*     */   {
/*  95 */     assertNotNull(paramAstNode);
/*  96 */     this.target = paramAstNode;
/*  97 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getElement()
/*     */   {
/* 104 */     return this.element;
/*     */   }
/*     */ 
/*     */   public void setElement(AstNode paramAstNode)
/*     */   {
/* 112 */     assertNotNull(paramAstNode);
/* 113 */     this.element = paramAstNode;
/* 114 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLb()
/*     */   {
/* 121 */     return this.lb;
/*     */   }
/*     */ 
/*     */   public void setLb(int paramInt)
/*     */   {
/* 128 */     this.lb = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRb()
/*     */   {
/* 135 */     return this.rb;
/*     */   }
/*     */ 
/*     */   public void setRb(int paramInt)
/*     */   {
/* 142 */     this.rb = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2) {
/* 146 */     this.lb = paramInt1;
/* 147 */     this.rb = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 152 */     StringBuilder localStringBuilder = new StringBuilder();
/* 153 */     localStringBuilder.append(makeIndent(paramInt));
/* 154 */     localStringBuilder.append(this.target.toSource(0));
/* 155 */     localStringBuilder.append("[");
/* 156 */     localStringBuilder.append(this.element.toSource(0));
/* 157 */     localStringBuilder.append("]");
/* 158 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 166 */     if (paramNodeVisitor.visit(this)) {
/* 167 */       this.target.visit(paramNodeVisitor);
/* 168 */       this.element.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ElementGet
 * JD-Core Version:    0.6.2
 */