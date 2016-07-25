/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LabeledStatement extends AstNode
/*     */ {
/*  55 */   private List<Label> labels = new ArrayList();
/*     */   private AstNode statement;
/*     */ 
/*     */   public LabeledStatement()
/*     */   {
/*  59 */     this.type = 133;
/*     */   }
/*     */ 
/*     */   public LabeledStatement(int paramInt)
/*     */   {
/*  66 */     super(paramInt);
/*     */ 
/*  59 */     this.type = 133;
/*     */   }
/*     */ 
/*     */   public LabeledStatement(int paramInt1, int paramInt2)
/*     */   {
/*  70 */     super(paramInt1, paramInt2);
/*     */ 
/*  59 */     this.type = 133;
/*     */   }
/*     */ 
/*     */   public List<Label> getLabels()
/*     */   {
/*  77 */     return this.labels;
/*     */   }
/*     */ 
/*     */   public void setLabels(List<Label> paramList)
/*     */   {
/*  86 */     assertNotNull(paramList);
/*  87 */     if (this.labels != null)
/*  88 */       this.labels.clear();
/*  89 */     for (Label localLabel : paramList)
/*  90 */       addLabel(localLabel);
/*     */   }
/*     */ 
/*     */   public void addLabel(Label paramLabel)
/*     */   {
/*  99 */     assertNotNull(paramLabel);
/* 100 */     this.labels.add(paramLabel);
/* 101 */     paramLabel.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getStatement()
/*     */   {
/* 108 */     return this.statement;
/*     */   }
/*     */ 
/*     */   public Label getLabelByName(String paramString)
/*     */   {
/* 117 */     for (Label localLabel : this.labels) {
/* 118 */       if (paramString.equals(localLabel.getName())) {
/* 119 */         return localLabel;
/*     */       }
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   public void setStatement(AstNode paramAstNode)
/*     */   {
/* 130 */     assertNotNull(paramAstNode);
/* 131 */     this.statement = paramAstNode;
/* 132 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public Label getFirstLabel() {
/* 136 */     return (Label)this.labels.get(0);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 141 */     StringBuilder localStringBuilder = new StringBuilder();
/* 142 */     for (Label localLabel : this.labels) {
/* 143 */       localStringBuilder.append(localLabel.toSource(paramInt));
/*     */     }
/* 145 */     localStringBuilder.append(this.statement.toSource(paramInt + 1));
/* 146 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 155 */     if (paramNodeVisitor.visit(this)) {
/* 156 */       for (AstNode localAstNode : this.labels) {
/* 157 */         localAstNode.visit(paramNodeVisitor);
/*     */       }
/* 159 */       this.statement.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.LabeledStatement
 * JD-Core Version:    0.6.2
 */