/*     */ package com.sun.org.apache.xpath.internal.patterns;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FunctionPattern extends StepPattern
/*     */ {
/*     */   static final long serialVersionUID = -5426793413091209944L;
/*     */   Expression m_functionExpr;
/*     */ 
/*     */   public FunctionPattern(Expression expr, int axis, int predaxis)
/*     */   {
/*  51 */     super(0, null, null, axis, predaxis);
/*     */ 
/*  53 */     this.m_functionExpr = expr;
/*     */   }
/*     */ 
/*     */   public final void calcScore()
/*     */   {
/*  62 */     this.m_score = SCORE_OTHER;
/*     */ 
/*  64 */     if (null == this.m_targetString)
/*  65 */       calcTargetString();
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  86 */     super.fixupVariables(vars, globalsSize);
/*  87 */     this.m_functionExpr.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int context)
/*     */     throws TransformerException
/*     */   {
/* 108 */     DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
/* 109 */     XNumber score = SCORE_NONE;
/*     */ 
/* 111 */     if (null != nl)
/*     */     {
/*     */       int n;
/* 115 */       while (-1 != (n = nl.nextNode()))
/*     */       {
/* 117 */         score = n == context ? SCORE_OTHER : SCORE_NONE;
/*     */ 
/* 119 */         if (score == SCORE_OTHER)
/*     */         {
/* 121 */           context = n;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 129 */     nl.detach();
/*     */ 
/* 131 */     return score;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int context, DTM dtm, int expType)
/*     */     throws TransformerException
/*     */   {
/* 152 */     DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
/* 153 */     XNumber score = SCORE_NONE;
/*     */ 
/* 155 */     if (null != nl)
/*     */     {
/*     */       int n;
/* 159 */       while (-1 != (n = nl.nextNode()))
/*     */       {
/* 161 */         score = n == context ? SCORE_OTHER : SCORE_NONE;
/*     */ 
/* 163 */         if (score == SCORE_OTHER)
/*     */         {
/* 165 */           context = n;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 171 */       nl.detach();
/*     */     }
/*     */ 
/* 174 */     return score;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 194 */     int context = xctxt.getCurrentNode();
/* 195 */     DTMIterator nl = this.m_functionExpr.asIterator(xctxt, context);
/* 196 */     XNumber score = SCORE_NONE;
/*     */ 
/* 198 */     if (null != nl)
/*     */     {
/*     */       int n;
/* 202 */       while (-1 != (n = nl.nextNode()))
/*     */       {
/* 204 */         score = n == context ? SCORE_OTHER : SCORE_NONE;
/*     */ 
/* 206 */         if (score == SCORE_OTHER)
/*     */         {
/* 208 */           context = n;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 214 */       nl.detach();
/*     */     }
/*     */ 
/* 217 */     return score;
/*     */   }
/*     */ 
/*     */   protected void callSubtreeVisitors(XPathVisitor visitor)
/*     */   {
/* 246 */     this.m_functionExpr.callVisitors(new FunctionOwner(), visitor);
/* 247 */     super.callSubtreeVisitors(visitor);
/*     */   }
/*     */ 
/*     */   class FunctionOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     FunctionOwner()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 227 */       return FunctionPattern.this.m_functionExpr;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 236 */       exp.exprSetParent(FunctionPattern.this);
/* 237 */       FunctionPattern.this.m_functionExpr = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.patterns.FunctionPattern
 * JD-Core Version:    0.6.2
 */