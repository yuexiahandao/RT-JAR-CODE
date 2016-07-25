/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SwitchCase extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */   private List<AstNode> statements;
/*     */ 
/*     */   public SwitchCase()
/*     */   {
/*  68 */     this.type = 115;
/*     */   }
/*     */ 
/*     */   public SwitchCase(int paramInt)
/*     */   {
/*  75 */     super(paramInt);
/*     */ 
/*  68 */     this.type = 115;
/*     */   }
/*     */ 
/*     */   public SwitchCase(int paramInt1, int paramInt2)
/*     */   {
/*  79 */     super(paramInt1, paramInt2);
/*     */ 
/*  68 */     this.type = 115;
/*     */   }
/*     */ 
/*     */   public AstNode getExpression()
/*     */   {
/*  86 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public void setExpression(AstNode paramAstNode)
/*     */   {
/*  97 */     this.expression = paramAstNode;
/*  98 */     if (paramAstNode != null)
/*  99 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isDefault()
/*     */   {
/* 107 */     return this.expression == null;
/*     */   }
/*     */ 
/*     */   public List<AstNode> getStatements()
/*     */   {
/* 114 */     return this.statements;
/*     */   }
/*     */ 
/*     */   public void setStatements(List<AstNode> paramList)
/*     */   {
/* 122 */     if (this.statements != null) {
/* 123 */       this.statements.clear();
/*     */     }
/* 125 */     for (AstNode localAstNode : paramList)
/* 126 */       addStatement(localAstNode);
/*     */   }
/*     */ 
/*     */   public void addStatement(AstNode paramAstNode)
/*     */   {
/* 140 */     assertNotNull(paramAstNode);
/* 141 */     if (this.statements == null) {
/* 142 */       this.statements = new ArrayList();
/*     */     }
/* 144 */     int i = paramAstNode.getPosition() + paramAstNode.getLength();
/* 145 */     setLength(i - getPosition());
/* 146 */     this.statements.add(paramAstNode);
/* 147 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 152 */     StringBuilder localStringBuilder = new StringBuilder();
/* 153 */     localStringBuilder.append(makeIndent(paramInt));
/* 154 */     if (this.expression == null) {
/* 155 */       localStringBuilder.append("default:\n");
/*     */     } else {
/* 157 */       localStringBuilder.append("case ");
/* 158 */       localStringBuilder.append(this.expression.toSource(0));
/* 159 */       localStringBuilder.append(":\n");
/*     */     }
/* 161 */     if (this.statements != null) {
/* 162 */       for (AstNode localAstNode : this.statements) {
/* 163 */         localStringBuilder.append(localAstNode.toSource(paramInt + 1));
/*     */       }
/*     */     }
/* 166 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 175 */     if (paramNodeVisitor.visit(this)) {
/* 176 */       if (this.expression != null) {
/* 177 */         this.expression.visit(paramNodeVisitor);
/*     */       }
/* 179 */       if (this.statements != null)
/* 180 */         for (AstNode localAstNode : this.statements)
/* 181 */           localAstNode.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.SwitchCase
 * JD-Core Version:    0.6.2
 */