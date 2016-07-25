/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.VariableStack;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FilterExprIteratorSimple extends LocPathIterator
/*     */ {
/*     */   static final long serialVersionUID = -6978977187025375579L;
/*     */   private Expression m_expr;
/*     */   private transient XNodeSet m_exprObj;
/*  49 */   private boolean m_mustHardReset = false;
/*  50 */   private boolean m_canDetachNodeset = true;
/*     */ 
/*     */   public FilterExprIteratorSimple()
/*     */   {
/*  58 */     super(null);
/*     */   }
/*     */ 
/*     */   public FilterExprIteratorSimple(Expression expr)
/*     */   {
/*  67 */     super(null);
/*  68 */     this.m_expr = expr;
/*     */   }
/*     */ 
/*     */   public void setRoot(int context, Object environment)
/*     */   {
/*  80 */     super.setRoot(context, environment);
/*  81 */     this.m_exprObj = executeFilterExpr(context, this.m_execContext, getPrefixResolver(), getIsTopLevel(), this.m_stackFrame, this.m_expr);
/*     */   }
/*     */ 
/*     */   public static XNodeSet executeFilterExpr(int context, XPathContext xctxt, PrefixResolver prefixResolver, boolean isTopLevel, int stackFrame, Expression expr)
/*     */     throws WrappedRuntimeException
/*     */   {
/*  96 */     PrefixResolver savedResolver = xctxt.getNamespaceContext();
/*  97 */     XNodeSet result = null;
/*     */     try
/*     */     {
/* 101 */       xctxt.pushCurrentNode(context);
/* 102 */       xctxt.setNamespaceContext(prefixResolver);
/*     */ 
/* 109 */       if (isTopLevel)
/*     */       {
/* 112 */         VariableStack vars = xctxt.getVarStack();
/*     */ 
/* 115 */         int savedStart = vars.getStackFrame();
/* 116 */         vars.setStackFrame(stackFrame);
/*     */ 
/* 118 */         result = (XNodeSet)expr.execute(xctxt);
/* 119 */         result.setShouldCacheNodes(true);
/*     */ 
/* 122 */         vars.setStackFrame(savedStart);
/*     */       }
/*     */       else {
/* 125 */         result = (XNodeSet)expr.execute(xctxt);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (TransformerException se)
/*     */     {
/* 132 */       throw new WrappedRuntimeException(se);
/*     */     }
/*     */     finally
/*     */     {
/* 136 */       xctxt.popCurrentNode();
/* 137 */       xctxt.setNamespaceContext(savedResolver);
/*     */     }
/* 139 */     return result;
/*     */   }
/*     */ 
/*     */   public int nextNode()
/*     */   {
/* 152 */     if (this.m_foundLast)
/* 153 */       return -1;
/*     */     int next;
/* 157 */     if (null != this.m_exprObj)
/*     */     {
/*     */       int next;
/* 159 */       this.m_lastFetched = (next = this.m_exprObj.nextNode());
/*     */     }
/*     */     else {
/* 162 */       this.m_lastFetched = (next = -1);
/*     */     }
/*     */ 
/* 165 */     if (-1 != next)
/*     */     {
/* 167 */       this.m_pos += 1;
/* 168 */       return next;
/*     */     }
/*     */ 
/* 172 */     this.m_foundLast = true;
/*     */ 
/* 174 */     return -1;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 185 */     if (this.m_allowDetach)
/*     */     {
/* 187 */       super.detach();
/* 188 */       this.m_exprObj.detach();
/* 189 */       this.m_exprObj = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 205 */     super.fixupVariables(vars, globalsSize);
/* 206 */     this.m_expr.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public Expression getInnerExpression()
/*     */   {
/* 214 */     return this.m_expr;
/*     */   }
/*     */ 
/*     */   public void setInnerExpression(Expression expr)
/*     */   {
/* 222 */     expr.exprSetParent(this);
/* 223 */     this.m_expr = expr;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 232 */     if ((null != this.m_expr) && ((this.m_expr instanceof PathComponent)))
/*     */     {
/* 234 */       return ((PathComponent)this.m_expr).getAnalysisBits();
/*     */     }
/* 236 */     return 67108864;
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 248 */     return this.m_exprObj.isDocOrdered();
/*     */   }
/*     */ 
/*     */   public void callPredicateVisitors(XPathVisitor visitor)
/*     */   {
/* 281 */     this.m_expr.callVisitors(new filterExprOwner(), visitor);
/*     */ 
/* 283 */     super.callPredicateVisitors(visitor);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 291 */     if (!super.deepEquals(expr)) {
/* 292 */       return false;
/*     */     }
/* 294 */     FilterExprIteratorSimple fet = (FilterExprIteratorSimple)expr;
/* 295 */     if (!this.m_expr.deepEquals(fet.m_expr)) {
/* 296 */       return false;
/*     */     }
/* 298 */     return true;
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 309 */     if (null != this.m_exprObj) {
/* 310 */       return this.m_exprObj.getAxis();
/*     */     }
/* 312 */     return 20;
/*     */   }
/*     */ 
/*     */   class filterExprOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     filterExprOwner()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 258 */       return FilterExprIteratorSimple.this.m_expr;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 266 */       exp.exprSetParent(FilterExprIteratorSimple.this);
/* 267 */       FilterExprIteratorSimple.this.m_expr = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.FilterExprIteratorSimple
 * JD-Core Version:    0.6.2
 */