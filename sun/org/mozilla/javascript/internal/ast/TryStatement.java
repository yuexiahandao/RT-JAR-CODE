/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TryStatement extends AstNode
/*     */ {
/*  61 */   private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList());
/*     */   private AstNode tryBlock;
/*     */   private List<CatchClause> catchClauses;
/*     */   private AstNode finallyBlock;
/*  67 */   private int finallyPosition = -1;
/*     */ 
/*     */   public TryStatement() {
/*  70 */     this.type = 81;
/*     */   }
/*     */ 
/*     */   public TryStatement(int paramInt)
/*     */   {
/*  77 */     super(paramInt);
/*     */ 
/*  70 */     this.type = 81;
/*     */   }
/*     */ 
/*     */   public TryStatement(int paramInt1, int paramInt2)
/*     */   {
/*  81 */     super(paramInt1, paramInt2);
/*     */ 
/*  70 */     this.type = 81;
/*     */   }
/*     */ 
/*     */   public AstNode getTryBlock()
/*     */   {
/*  85 */     return this.tryBlock;
/*     */   }
/*     */ 
/*     */   public void setTryBlock(AstNode paramAstNode)
/*     */   {
/*  93 */     assertNotNull(paramAstNode);
/*  94 */     this.tryBlock = paramAstNode;
/*  95 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public List<CatchClause> getCatchClauses()
/*     */   {
/* 103 */     return this.catchClauses != null ? this.catchClauses : NO_CATCHES;
/*     */   }
/*     */ 
/*     */   public void setCatchClauses(List<CatchClause> paramList)
/*     */   {
/* 112 */     if (paramList == null) {
/* 113 */       this.catchClauses = null;
/*     */     } else {
/* 115 */       if (this.catchClauses != null)
/* 116 */         this.catchClauses.clear();
/* 117 */       for (CatchClause localCatchClause : paramList)
/* 118 */         addCatchClause(localCatchClause);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addCatchClause(CatchClause paramCatchClause)
/*     */   {
/* 129 */     assertNotNull(paramCatchClause);
/* 130 */     if (this.catchClauses == null) {
/* 131 */       this.catchClauses = new ArrayList();
/*     */     }
/* 133 */     this.catchClauses.add(paramCatchClause);
/* 134 */     paramCatchClause.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getFinallyBlock()
/*     */   {
/* 141 */     return this.finallyBlock;
/*     */   }
/*     */ 
/*     */   public void setFinallyBlock(AstNode paramAstNode)
/*     */   {
/* 149 */     this.finallyBlock = paramAstNode;
/* 150 */     if (paramAstNode != null)
/* 151 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getFinallyPosition()
/*     */   {
/* 158 */     return this.finallyPosition;
/*     */   }
/*     */ 
/*     */   public void setFinallyPosition(int paramInt)
/*     */   {
/* 165 */     this.finallyPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 170 */     StringBuilder localStringBuilder = new StringBuilder(250);
/* 171 */     localStringBuilder.append(makeIndent(paramInt));
/* 172 */     localStringBuilder.append("try ");
/* 173 */     localStringBuilder.append(this.tryBlock.toSource(paramInt).trim());
/* 174 */     for (CatchClause localCatchClause : getCatchClauses()) {
/* 175 */       localStringBuilder.append(localCatchClause.toSource(paramInt));
/*     */     }
/* 177 */     if (this.finallyBlock != null) {
/* 178 */       localStringBuilder.append(" finally ");
/* 179 */       localStringBuilder.append(this.finallyBlock.toSource(paramInt));
/*     */     }
/* 181 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 190 */     if (paramNodeVisitor.visit(this)) {
/* 191 */       this.tryBlock.visit(paramNodeVisitor);
/* 192 */       for (CatchClause localCatchClause : getCatchClauses()) {
/* 193 */         localCatchClause.visit(paramNodeVisitor);
/*     */       }
/* 195 */       if (this.finallyBlock != null)
/* 196 */         this.finallyBlock.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.TryStatement
 * JD-Core Version:    0.6.2
 */