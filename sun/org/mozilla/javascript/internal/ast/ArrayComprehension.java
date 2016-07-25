/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ArrayComprehension extends Scope
/*     */ {
/*     */   private AstNode result;
/*  53 */   private List<ArrayComprehensionLoop> loops = new ArrayList();
/*     */   private AstNode filter;
/*  56 */   private int ifPosition = -1;
/*  57 */   private int lp = -1;
/*  58 */   private int rp = -1;
/*     */ 
/*     */   public ArrayComprehension() {
/*  61 */     this.type = 157;
/*     */   }
/*     */ 
/*     */   public ArrayComprehension(int paramInt)
/*     */   {
/*  68 */     super(paramInt);
/*     */ 
/*  61 */     this.type = 157;
/*     */   }
/*     */ 
/*     */   public ArrayComprehension(int paramInt1, int paramInt2)
/*     */   {
/*  72 */     super(paramInt1, paramInt2);
/*     */ 
/*  61 */     this.type = 157;
/*     */   }
/*     */ 
/*     */   public AstNode getResult()
/*     */   {
/*  79 */     return this.result;
/*     */   }
/*     */ 
/*     */   public void setResult(AstNode paramAstNode)
/*     */   {
/*  87 */     assertNotNull(paramAstNode);
/*  88 */     this.result = paramAstNode;
/*  89 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public List<ArrayComprehensionLoop> getLoops()
/*     */   {
/*  96 */     return this.loops;
/*     */   }
/*     */ 
/*     */   public void setLoops(List<ArrayComprehensionLoop> paramList)
/*     */   {
/* 104 */     assertNotNull(paramList);
/* 105 */     this.loops.clear();
/* 106 */     for (ArrayComprehensionLoop localArrayComprehensionLoop : paramList)
/* 107 */       addLoop(localArrayComprehensionLoop);
/*     */   }
/*     */ 
/*     */   public void addLoop(ArrayComprehensionLoop paramArrayComprehensionLoop)
/*     */   {
/* 116 */     assertNotNull(paramArrayComprehensionLoop);
/* 117 */     this.loops.add(paramArrayComprehensionLoop);
/* 118 */     paramArrayComprehensionLoop.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getFilter()
/*     */   {
/* 125 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public void setFilter(AstNode paramAstNode)
/*     */   {
/* 133 */     this.filter = paramAstNode;
/* 134 */     if (paramAstNode != null)
/* 135 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public int getIfPosition()
/*     */   {
/* 142 */     return this.ifPosition;
/*     */   }
/*     */ 
/*     */   public void setIfPosition(int paramInt)
/*     */   {
/* 149 */     this.ifPosition = paramInt;
/*     */   }
/*     */ 
/*     */   public int getFilterLp()
/*     */   {
/* 156 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setFilterLp(int paramInt)
/*     */   {
/* 163 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getFilterRp()
/*     */   {
/* 170 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setFilterRp(int paramInt)
/*     */   {
/* 177 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 182 */     StringBuilder localStringBuilder = new StringBuilder(250);
/* 183 */     localStringBuilder.append("[");
/* 184 */     localStringBuilder.append(this.result.toSource(0));
/* 185 */     for (ArrayComprehensionLoop localArrayComprehensionLoop : this.loops) {
/* 186 */       localStringBuilder.append(localArrayComprehensionLoop.toSource(0));
/*     */     }
/* 188 */     if (this.filter != null) {
/* 189 */       localStringBuilder.append(" if (");
/* 190 */       localStringBuilder.append(this.filter.toSource(0));
/* 191 */       localStringBuilder.append(")");
/*     */     }
/* 193 */     localStringBuilder.append("]");
/* 194 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 203 */     if (!paramNodeVisitor.visit(this)) {
/* 204 */       return;
/*     */     }
/* 206 */     this.result.visit(paramNodeVisitor);
/* 207 */     for (ArrayComprehensionLoop localArrayComprehensionLoop : this.loops) {
/* 208 */       localArrayComprehensionLoop.visit(paramNodeVisitor);
/*     */     }
/* 210 */     if (this.filter != null)
/* 211 */       this.filter.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ArrayComprehension
 * JD-Core Version:    0.6.2
 */