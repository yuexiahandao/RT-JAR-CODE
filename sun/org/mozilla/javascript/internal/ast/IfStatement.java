/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class IfStatement extends AstNode
/*     */ {
/*     */   private AstNode condition;
/*     */   private AstNode thenPart;
/*  54 */   private int elsePosition = -1;
/*     */   private AstNode elsePart;
/*  56 */   private int lp = -1;
/*  57 */   private int rp = -1;
/*     */ 
/*     */   public IfStatement() {
/*  60 */     this.type = 112;
/*     */   }
/*     */ 
/*     */   public IfStatement(int paramInt)
/*     */   {
/*  67 */     super(paramInt);
/*     */ 
/*  60 */     this.type = 112;
/*     */   }
/*     */ 
/*     */   public IfStatement(int paramInt1, int paramInt2)
/*     */   {
/*  71 */     super(paramInt1, paramInt2);
/*     */ 
/*  60 */     this.type = 112;
/*     */   }
/*     */ 
/*     */   public AstNode getCondition()
/*     */   {
/*  78 */     return this.condition;
/*     */   }
/*     */ 
/*     */   public void setCondition(AstNode paramAstNode)
/*     */   {
/*  86 */     assertNotNull(paramAstNode);
/*  87 */     this.condition = paramAstNode;
/*  88 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getThenPart()
/*     */   {
/*  95 */     return this.thenPart;
/*     */   }
/*     */ 
/*     */   public void setThenPart(AstNode paramAstNode)
/*     */   {
/* 103 */     assertNotNull(paramAstNode);
/* 104 */     this.thenPart = paramAstNode;
/* 105 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getElsePart()
/*     */   {
/* 112 */     return this.elsePart;
/*     */   }
/*     */ 
/*     */   public void setElsePart(AstNode paramAstNode)
/*     */   {
/* 121 */     this.elsePart = paramAstNode;
/* 122 */     if (paramAstNode != null)
/* 123 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getElsePosition()
/*     */   {
/* 130 */     return this.elsePosition;
/*     */   }
/*     */ 
/*     */   public void setElsePosition(int paramInt)
/*     */   {
/* 137 */     this.elsePosition = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 144 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 151 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 158 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 165 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 172 */     this.lp = paramInt1;
/* 173 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 178 */     String str = makeIndent(paramInt);
/* 179 */     StringBuilder localStringBuilder = new StringBuilder(32);
/* 180 */     localStringBuilder.append(str);
/* 181 */     localStringBuilder.append("if (");
/* 182 */     localStringBuilder.append(this.condition.toSource(0));
/* 183 */     localStringBuilder.append(") ");
/* 184 */     if (!(this.thenPart instanceof Block)) {
/* 185 */       localStringBuilder.append("\n").append(makeIndent(paramInt));
/*     */     }
/* 187 */     localStringBuilder.append(this.thenPart.toSource(paramInt).trim());
/* 188 */     if ((this.elsePart instanceof IfStatement)) {
/* 189 */       localStringBuilder.append(" else ");
/* 190 */       localStringBuilder.append(this.elsePart.toSource(paramInt).trim());
/* 191 */     } else if (this.elsePart != null) {
/* 192 */       localStringBuilder.append(" else ");
/* 193 */       localStringBuilder.append(this.elsePart.toSource(paramInt).trim());
/*     */     }
/* 195 */     localStringBuilder.append("\n");
/* 196 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 205 */     if (paramNodeVisitor.visit(this)) {
/* 206 */       this.condition.visit(paramNodeVisitor);
/* 207 */       this.thenPart.visit(paramNodeVisitor);
/* 208 */       if (this.elsePart != null)
/* 209 */         this.elsePart.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.IfStatement
 * JD-Core Version:    0.6.2
 */