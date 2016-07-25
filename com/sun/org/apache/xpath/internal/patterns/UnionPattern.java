/*     */ package com.sun.org.apache.xpath.internal.patterns;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class UnionPattern extends Expression
/*     */ {
/*     */   static final long serialVersionUID = -6670449967116905820L;
/*     */   private StepPattern[] m_patterns;
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  49 */     for (int i = 0; i < this.m_patterns.length; i++)
/*     */     {
/*  51 */       this.m_patterns[i].fixupVariables(vars, globalsSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/*  64 */     if (null != this.m_patterns)
/*     */     {
/*  66 */       int n = this.m_patterns.length;
/*  67 */       for (int i = 0; i < n; i++)
/*     */       {
/*  69 */         if (this.m_patterns[i].canTraverseOutsideSubtree())
/*  70 */           return true;
/*     */       }
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   public void setPatterns(StepPattern[] patterns)
/*     */   {
/*  84 */     this.m_patterns = patterns;
/*  85 */     if (null != patterns)
/*     */     {
/*  87 */       for (int i = 0; i < patterns.length; i++)
/*     */       {
/*  89 */         patterns[i].exprSetParent(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public StepPattern[] getPatterns()
/*     */   {
/* 103 */     return this.m_patterns;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 122 */     XObject bestScore = null;
/* 123 */     int n = this.m_patterns.length;
/*     */ 
/* 125 */     for (int i = 0; i < n; i++)
/*     */     {
/* 127 */       XObject score = this.m_patterns[i].execute(xctxt);
/*     */ 
/* 129 */       if (score != NodeTest.SCORE_NONE)
/*     */       {
/* 131 */         if (null == bestScore)
/* 132 */           bestScore = score;
/* 133 */         else if (score.num() > bestScore.num()) {
/* 134 */           bestScore = score;
/*     */         }
/*     */       }
/*     */     }
/* 138 */     if (null == bestScore)
/*     */     {
/* 140 */       bestScore = NodeTest.SCORE_NONE;
/*     */     }
/*     */ 
/* 143 */     return bestScore;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 179 */     visitor.visitUnionPattern(owner, this);
/* 180 */     if (null != this.m_patterns)
/*     */     {
/* 182 */       int n = this.m_patterns.length;
/* 183 */       for (int i = 0; i < n; i++)
/*     */       {
/* 185 */         this.m_patterns[i].callVisitors(new UnionPathPartOwner(i), visitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 195 */     if (!isSameClass(expr)) {
/* 196 */       return false;
/*     */     }
/* 198 */     UnionPattern up = (UnionPattern)expr;
/*     */ 
/* 200 */     if (null != this.m_patterns)
/*     */     {
/* 202 */       int n = this.m_patterns.length;
/* 203 */       if ((null == up.m_patterns) || (up.m_patterns.length != n)) {
/* 204 */         return false;
/*     */       }
/* 206 */       for (int i = 0; i < n; i++)
/*     */       {
/* 208 */         if (!this.m_patterns[i].deepEquals(up.m_patterns[i]))
/* 209 */           return false;
/*     */       }
/*     */     }
/* 212 */     else if (up.m_patterns != null) {
/* 213 */       return false;
/*     */     }
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   class UnionPathPartOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     int m_index;
/*     */ 
/*     */     UnionPathPartOwner(int index)
/*     */     {
/* 152 */       this.m_index = index;
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 160 */       return UnionPattern.this.m_patterns[this.m_index];
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 169 */       exp.exprSetParent(UnionPattern.this);
/* 170 */       UnionPattern.this.m_patterns[this.m_index] = ((StepPattern)exp);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.patterns.UnionPattern
 * JD-Core Version:    0.6.2
 */