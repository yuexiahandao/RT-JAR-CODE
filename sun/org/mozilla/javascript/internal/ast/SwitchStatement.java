/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SwitchStatement extends Jump
/*     */ {
/*  66 */   private static final List<SwitchCase> NO_CASES = Collections.unmodifiableList(new ArrayList());
/*     */   private AstNode expression;
/*     */   private List<SwitchCase> cases;
/*  71 */   private int lp = -1;
/*  72 */   private int rp = -1;
/*     */ 
/*     */   public SwitchStatement() {
/*  75 */     this.type = 114; } 
/*  75 */   public SwitchStatement(int paramInt) { this.type = 114;
/*     */ 
/*  83 */     this.position = paramInt;
/*     */   }
/*     */ 
/*     */   public SwitchStatement(int paramInt1, int paramInt2)
/*     */   {
/*  75 */     this.type = 114;
/*     */ 
/*  87 */     this.position = paramInt1;
/*  88 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  95 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/* 104 */     assertNotNull(paramAstNode);
/* 105 */     this.expression = paramAstNode;
/* 106 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public List<SwitchCase> getCases()
/*     */   {
/* 114 */     return this.cases != null ? this.cases : NO_CASES;
/*     */   }
/*     */ 
/*     */   public void setCases(List<SwitchCase> paramList)
/*     */   {
/* 123 */     if (paramList == null) {
/* 124 */       this.cases = null;
/*     */     } else {
/* 126 */       if (this.cases != null)
/* 127 */         this.cases.clear();
/* 128 */       for (SwitchCase localSwitchCase : paramList)
/* 129 */         addCase(localSwitchCase);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addCase(SwitchCase paramSwitchCase)
/*     */   {
/* 138 */     assertNotNull(paramSwitchCase);
/* 139 */     if (this.cases == null) {
/* 140 */       this.cases = new ArrayList();
/*     */     }
/* 142 */     this.cases.add(paramSwitchCase);
/* 143 */     paramSwitchCase.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 150 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 157 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 164 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 171 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 178 */     this.lp = paramInt1;
/* 179 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 184 */     String str = makeIndent(paramInt);
/* 185 */     StringBuilder localStringBuilder = new StringBuilder();
/* 186 */     localStringBuilder.append(str);
/* 187 */     localStringBuilder.append("switch (");
/* 188 */     localStringBuilder.append(this.expression.toSource(0));
/* 189 */     localStringBuilder.append(") {\n");
/* 190 */     for (SwitchCase localSwitchCase : this.cases) {
/* 191 */       localStringBuilder.append(localSwitchCase.toSource(paramInt + 1));
/*     */     }
/* 193 */     localStringBuilder.append(str);
/* 194 */     localStringBuilder.append("}\n");
/* 195 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 204 */     if (paramNodeVisitor.visit(this)) {
/* 205 */       this.expression.visit(paramNodeVisitor);
/* 206 */       for (SwitchCase localSwitchCase : getCases())
/* 207 */         localSwitchCase.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.SwitchStatement
 * JD-Core Version:    0.6.2
 */