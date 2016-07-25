/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class FunctionCall extends AstNode
/*     */ {
/*  52 */   protected static final List<AstNode> NO_ARGS = Collections.unmodifiableList(new ArrayList());
/*     */   protected AstNode target;
/*     */   protected List<AstNode> arguments;
/*  57 */   protected int lp = -1;
/*  58 */   protected int rp = -1;
/*     */ 
/*     */   public FunctionCall() {
/*  61 */     this.type = 38;
/*     */   }
/*     */ 
/*     */   public FunctionCall(int paramInt)
/*     */   {
/*  68 */     super(paramInt);
/*     */ 
/*  61 */     this.type = 38;
/*     */   }
/*     */ 
/*     */   public FunctionCall(int paramInt1, int paramInt2)
/*     */   {
/*  72 */     super(paramInt1, paramInt2);
/*     */ 
/*  61 */     this.type = 38;
/*     */   }
/*     */ 
/*     */   public AstNode getTarget()
/*     */   {
/*  79 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setTarget(AstNode paramAstNode)
/*     */   {
/*  89 */     assertNotNull(paramAstNode);
/*  90 */     this.target = paramAstNode;
/*  91 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public List<AstNode> getArguments()
/*     */   {
/* 100 */     return this.arguments != null ? this.arguments : NO_ARGS;
/*     */   }
/*     */ 
/*     */   public void setArguments(List<AstNode> paramList)
/*     */   {
/* 109 */     if (paramList == null) {
/* 110 */       this.arguments = null;
/*     */     } else {
/* 112 */       if (this.arguments != null)
/* 113 */         this.arguments.clear();
/* 114 */       for (AstNode localAstNode : paramList)
/* 115 */         addArgument(localAstNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addArgument(AstNode paramAstNode)
/*     */   {
/* 126 */     assertNotNull(paramAstNode);
/* 127 */     if (this.arguments == null) {
/* 128 */       this.arguments = new ArrayList();
/*     */     }
/* 130 */     this.arguments.add(paramAstNode);
/* 131 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 138 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 146 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 153 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 160 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 167 */     this.lp = paramInt1;
/* 168 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 173 */     StringBuilder localStringBuilder = new StringBuilder();
/* 174 */     localStringBuilder.append(makeIndent(paramInt));
/* 175 */     localStringBuilder.append(this.target.toSource(0));
/* 176 */     localStringBuilder.append("(");
/* 177 */     if (this.arguments != null) {
/* 178 */       printList(this.arguments, localStringBuilder);
/*     */     }
/* 180 */     localStringBuilder.append(")");
/* 181 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 189 */     if (paramNodeVisitor.visit(this)) {
/* 190 */       this.target.visit(paramNodeVisitor);
/* 191 */       for (AstNode localAstNode : getArguments())
/* 192 */         localAstNode.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.FunctionCall
 * JD-Core Version:    0.6.2
 */