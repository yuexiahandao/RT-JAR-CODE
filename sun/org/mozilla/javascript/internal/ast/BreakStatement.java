/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class BreakStatement extends Jump
/*     */ {
/*     */   private Name breakLabel;
/*     */   private AstNode target;
/*     */ 
/*     */   public BreakStatement()
/*     */   {
/*  55 */     this.type = 120; } 
/*  55 */   public BreakStatement(int paramInt) { this.type = 120;
/*     */ 
/*  63 */     this.position = paramInt;
/*     */   }
/*     */ 
/*     */   public BreakStatement(int paramInt1, int paramInt2)
/*     */   {
/*  55 */     this.type = 120;
/*     */ 
/*  67 */     this.position = paramInt1;
/*  68 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public Name getBreakLabel()
/*     */   {
/*  77 */     return this.breakLabel;
/*     */   }
/*     */ 
/*     */   public void setBreakLabel(Name paramName)
/*     */   {
/*  87 */     this.breakLabel = paramName;
/*  88 */     if (paramName != null)
/*  89 */       paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getBreakTarget()
/*     */   {
/*  98 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setBreakTarget(Jump paramJump)
/*     */   {
/* 107 */     assertNotNull(paramJump);
/* 108 */     this.target = paramJump;
/* 109 */     setJumpStatement(paramJump);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 114 */     StringBuilder localStringBuilder = new StringBuilder();
/* 115 */     localStringBuilder.append(makeIndent(paramInt));
/* 116 */     localStringBuilder.append("break");
/* 117 */     if (this.breakLabel != null) {
/* 118 */       localStringBuilder.append(" ");
/* 119 */       localStringBuilder.append(this.breakLabel.toSource(0));
/*     */     }
/* 121 */     localStringBuilder.append(";\n");
/* 122 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 130 */     if ((paramNodeVisitor.visit(this)) && (this.breakLabel != null))
/* 131 */       this.breakLabel.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.BreakStatement
 * JD-Core Version:    0.6.2
 */