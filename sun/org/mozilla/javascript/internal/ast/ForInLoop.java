/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ForInLoop extends Loop
/*     */ {
/*     */   protected AstNode iterator;
/*     */   protected AstNode iteratedObject;
/*  53 */   protected int inPosition = -1;
/*  54 */   protected int eachPosition = -1;
/*     */   protected boolean isForEach;
/*     */ 
/*     */   public ForInLoop()
/*     */   {
/*  58 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public ForInLoop(int paramInt)
/*     */   {
/*  65 */     super(paramInt);
/*     */ 
/*  58 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public ForInLoop(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     super(paramInt1, paramInt2);
/*     */ 
/*  58 */     this.type = 119;
/*     */   }
/*     */ 
/*     */   public AstNode getIterator()
/*     */   {
/*  76 */     return this.iterator;
/*     */   }
/*     */ 
/*     */   public void setIterator(AstNode paramAstNode)
/*     */   {
/*  85 */     assertNotNull(paramAstNode);
/*  86 */     this.iterator = paramAstNode;
/*  87 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getIteratedObject()
/*     */   {
/*  94 */     return this.iteratedObject;
/*     */   }
/*     */ 
/*     */   public void setIteratedObject(AstNode paramAstNode)
/*     */   {
/* 102 */     assertNotNull(paramAstNode);
/* 103 */     this.iteratedObject = paramAstNode;
/* 104 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isForEach()
/*     */   {
/* 111 */     return this.isForEach;
/*     */   }
/*     */ 
/*     */   public void setIsForEach(boolean paramBoolean)
/*     */   {
/* 118 */     this.isForEach = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getInPosition()
/*     */   {
/* 125 */     return this.inPosition;
/*     */   }
/*     */ 
/*     */   public void setInPosition(int paramInt)
/*     */   {
/* 134 */     this.inPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public int getEachPosition()
/*     */   {
/* 141 */     return this.eachPosition;
/*     */   }
/*     */ 
/*     */   public void setEachPosition(int paramInt)
/*     */   {
/* 150 */     this.eachPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 155 */     StringBuilder localStringBuilder = new StringBuilder();
/* 156 */     localStringBuilder.append(makeIndent(paramInt));
/* 157 */     localStringBuilder.append("for ");
/* 158 */     if (isForEach()) {
/* 159 */       localStringBuilder.append("each ");
/*     */     }
/* 161 */     localStringBuilder.append("(");
/* 162 */     localStringBuilder.append(this.iterator.toSource(0));
/* 163 */     localStringBuilder.append(" in ");
/* 164 */     localStringBuilder.append(this.iteratedObject.toSource(0));
/* 165 */     localStringBuilder.append(") ");
/* 166 */     if ((this.body instanceof Block))
/* 167 */       localStringBuilder.append(this.body.toSource(paramInt).trim()).append("\n");
/*     */     else {
/* 169 */       localStringBuilder.append("\n").append(this.body.toSource(paramInt + 1));
/*     */     }
/* 171 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 179 */     if (paramNodeVisitor.visit(this)) {
/* 180 */       this.iterator.visit(paramNodeVisitor);
/* 181 */       this.iteratedObject.visit(paramNodeVisitor);
/* 182 */       this.body.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ForInLoop
 * JD-Core Version:    0.6.2
 */