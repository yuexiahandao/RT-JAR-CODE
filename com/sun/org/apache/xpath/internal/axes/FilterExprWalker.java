/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.operations.Variable;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FilterExprWalker extends AxesWalker
/*     */ {
/*     */   static final long serialVersionUID = 5457182471424488375L;
/*     */   private Expression m_expr;
/*     */   private transient XNodeSet m_exprObj;
/* 227 */   private boolean m_mustHardReset = false;
/* 228 */   private boolean m_canDetachNodeset = true;
/*     */ 
/*     */   public FilterExprWalker(WalkingIterator locPathIterator)
/*     */   {
/*  52 */     super(locPathIterator, 20);
/*     */   }
/*     */ 
/*     */   public void init(Compiler compiler, int opPos, int stepType)
/*     */     throws TransformerException
/*     */   {
/*  68 */     super.init(compiler, opPos, stepType);
/*     */ 
/*  71 */     switch (stepType)
/*     */     {
/*     */     case 24:
/*     */     case 25:
/*  75 */       this.m_mustHardReset = true;
/*     */     case 22:
/*     */     case 23:
/*  78 */       this.m_expr = compiler.compile(opPos);
/*  79 */       this.m_expr.exprSetParent(this);
/*     */ 
/*  81 */       if ((this.m_expr instanceof Variable))
/*     */       {
/*  84 */         this.m_canDetachNodeset = false; } break;
/*     */     default:
/*  88 */       this.m_expr = compiler.compile(opPos + 2);
/*  89 */       this.m_expr.exprSetParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 114 */     super.detach();
/* 115 */     if (this.m_canDetachNodeset)
/*     */     {
/* 117 */       this.m_exprObj.detach();
/*     */     }
/* 119 */     this.m_exprObj = null;
/*     */   }
/*     */ 
/*     */   public void setRoot(int root)
/*     */   {
/* 131 */     super.setRoot(root);
/*     */ 
/* 133 */     this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(root, this.m_lpi.getXPathContext(), this.m_lpi.getPrefixResolver(), this.m_lpi.getIsTopLevel(), this.m_lpi.m_stackFrame, this.m_expr);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 149 */     FilterExprWalker clone = (FilterExprWalker)super.clone();
/*     */ 
/* 152 */     if (null != this.m_exprObj) {
/* 153 */       clone.m_exprObj = ((XNodeSet)this.m_exprObj.clone());
/*     */     }
/* 155 */     return clone;
/*     */   }
/*     */ 
/*     */   public short acceptNode(int n)
/*     */   {
/*     */     try
/*     */     {
/* 170 */       if (getPredicateCount() > 0)
/*     */       {
/* 172 */         countProximityPosition(0);
/*     */ 
/* 174 */         if (!executePredicates(n, this.m_lpi.getXPathContext())) {
/* 175 */           return 3;
/*     */         }
/*     */       }
/* 178 */       return 1;
/*     */     }
/*     */     catch (TransformerException se)
/*     */     {
/* 182 */       throw new RuntimeException(se.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNextNode()
/*     */   {
/* 198 */     if (null != this.m_exprObj)
/*     */     {
/* 200 */       int next = this.m_exprObj.nextNode();
/* 201 */       return next;
/*     */     }
/*     */ 
/* 204 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getLastPos(XPathContext xctxt)
/*     */   {
/* 217 */     return this.m_exprObj.getLength();
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 242 */     super.fixupVariables(vars, globalsSize);
/* 243 */     this.m_expr.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public Expression getInnerExpression()
/*     */   {
/* 251 */     return this.m_expr;
/*     */   }
/*     */ 
/*     */   public void setInnerExpression(Expression expr)
/*     */   {
/* 259 */     expr.exprSetParent(this);
/* 260 */     this.m_expr = expr;
/*     */   }
/*     */ 
/*     */   public int getAnalysisBits()
/*     */   {
/* 270 */     if ((null != this.m_expr) && ((this.m_expr instanceof PathComponent)))
/*     */     {
/* 272 */       return ((PathComponent)this.m_expr).getAnalysisBits();
/*     */     }
/* 274 */     return 67108864;
/*     */   }
/*     */ 
/*     */   public boolean isDocOrdered()
/*     */   {
/* 286 */     return this.m_exprObj.isDocOrdered();
/*     */   }
/*     */ 
/*     */   public int getAxis()
/*     */   {
/* 297 */     return this.m_exprObj.getAxis();
/*     */   }
/*     */ 
/*     */   public void callPredicateVisitors(XPathVisitor visitor)
/*     */   {
/* 329 */     this.m_expr.callVisitors(new filterExprOwner(), visitor);
/*     */ 
/* 331 */     super.callPredicateVisitors(visitor);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 340 */     if (!super.deepEquals(expr)) {
/* 341 */       return false;
/*     */     }
/* 343 */     FilterExprWalker walker = (FilterExprWalker)expr;
/* 344 */     if (!this.m_expr.deepEquals(walker.m_expr)) {
/* 345 */       return false;
/*     */     }
/* 347 */     return true;
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
/* 307 */       return FilterExprWalker.this.m_expr;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 315 */       exp.exprSetParent(FilterExprWalker.this);
/* 316 */       FilterExprWalker.this.m_expr = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.FilterExprWalker
 * JD-Core Version:    0.6.2
 */