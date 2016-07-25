/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class VariableInitializer extends AstNode
/*     */ {
/*     */   private AstNode target;
/*     */   private AstNode initializer;
/*     */ 
/*     */   public void setNodeType(int paramInt)
/*     */   {
/*  66 */     if ((paramInt != 122) && (paramInt != 154) && (paramInt != 153))
/*     */     {
/*  69 */       throw new IllegalArgumentException("invalid node type");
/*  70 */     }setType(paramInt);
/*     */   }
/*     */ 
/*     */   public VariableInitializer()
/*     */   {
/*  57 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public VariableInitializer(int paramInt)
/*     */   {
/*  77 */     super(paramInt);
/*     */ 
/*  57 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public VariableInitializer(int paramInt1, int paramInt2)
/*     */   {
/*  81 */     super(paramInt1, paramInt2);
/*     */ 
/*  57 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public boolean isDestructuring()
/*     */   {
/*  92 */     return !(this.target instanceof Name);
/*     */   }
/*     */ 
/*     */   public AstNode getTarget()
/*     */   {
/*  99 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setTarget(AstNode paramAstNode)
/*     */   {
/* 110 */     if (paramAstNode == null)
/* 111 */       throw new IllegalArgumentException("invalid target arg");
/* 112 */     this.target = paramAstNode;
/* 113 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getInitializer()
/*     */   {
/* 120 */     return this.initializer;
/*     */   }
/*     */ 
/*     */   public void setInitializer(AstNode paramAstNode)
/*     */   {
/* 128 */     this.initializer = paramAstNode;
/* 129 */     if (paramAstNode != null)
/* 130 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 135 */     StringBuilder localStringBuilder = new StringBuilder();
/* 136 */     localStringBuilder.append(makeIndent(paramInt));
/* 137 */     localStringBuilder.append(this.target.toSource(0));
/* 138 */     if (this.initializer != null) {
/* 139 */       localStringBuilder.append(" = ");
/* 140 */       localStringBuilder.append(this.initializer.toSource(0));
/*     */     }
/* 142 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 151 */     if (paramNodeVisitor.visit(this)) {
/* 152 */       this.target.visit(paramNodeVisitor);
/* 153 */       if (this.initializer != null)
/* 154 */         this.initializer.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.VariableInitializer
 * JD-Core Version:    0.6.2
 */