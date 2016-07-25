/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class CatchClause extends AstNode
/*     */ {
/*     */   private Name varName;
/*     */   private AstNode catchCondition;
/*     */   private Block body;
/*  55 */   private int ifPosition = -1;
/*  56 */   private int lp = -1;
/*  57 */   private int rp = -1;
/*     */ 
/*     */   public CatchClause() {
/*  60 */     this.type = 124;
/*     */   }
/*     */ 
/*     */   public CatchClause(int paramInt)
/*     */   {
/*  67 */     super(paramInt);
/*     */ 
/*  60 */     this.type = 124;
/*     */   }
/*     */ 
/*     */   public CatchClause(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     super(paramInt1, paramInt2);
/*     */ 
/*  60 */     this.type = 124;
/*     */   }
/*     */ 
/*     */   public Name getVarName()
/*     */   {
/*  79 */     return this.varName;
/*     */   }
/*     */ 
/*     */   public void setVarName(Name paramName)
/*     */   {
/*  88 */     assertNotNull(paramName);
/*  89 */     this.varName = paramName;
/*  90 */     paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getCatchCondition()
/*     */   {
/*  98 */     return this.catchCondition;
/*     */   }
/*     */ 
/*     */   public void setCatchCondition(AstNode paramAstNode)
/*     */   {
/* 106 */     this.catchCondition = paramAstNode;
/* 107 */     if (paramAstNode != null)
/* 108 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public Block getBody()
/*     */   {
/* 115 */     return this.body;
/*     */   }
/*     */ 
/*     */   public void setBody(Block paramBlock)
/*     */   {
/* 123 */     assertNotNull(paramBlock);
/* 124 */     this.body = paramBlock;
/* 125 */     paramBlock.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 132 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 139 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 146 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 153 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 160 */     this.lp = paramInt1;
/* 161 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getIfPosition()
/*     */   {
/* 169 */     return this.ifPosition;
/*     */   }
/*     */ 
/*     */   public void setIfPosition(int paramInt)
/*     */   {
/* 177 */     this.ifPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 182 */     StringBuilder localStringBuilder = new StringBuilder();
/* 183 */     localStringBuilder.append(makeIndent(paramInt));
/* 184 */     localStringBuilder.append("catch (");
/* 185 */     localStringBuilder.append(this.varName.toSource(0));
/* 186 */     if (this.catchCondition != null) {
/* 187 */       localStringBuilder.append(" if ");
/* 188 */       localStringBuilder.append(this.catchCondition.toSource(0));
/*     */     }
/* 190 */     localStringBuilder.append(") ");
/* 191 */     localStringBuilder.append(this.body.toSource(0));
/* 192 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 201 */     if (paramNodeVisitor.visit(this)) {
/* 202 */       this.varName.visit(paramNodeVisitor);
/* 203 */       if (this.catchCondition != null) {
/* 204 */         this.catchCondition.visit(paramNodeVisitor);
/*     */       }
/* 206 */       this.body.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.CatchClause
 * JD-Core Version:    0.6.2
 */