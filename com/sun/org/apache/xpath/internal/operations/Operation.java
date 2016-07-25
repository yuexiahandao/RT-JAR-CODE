/*     */ package com.sun.org.apache.xpath.internal.operations;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class Operation extends Expression
/*     */   implements ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = -3037139537171050430L;
/*     */   protected Expression m_left;
/*     */   protected Expression m_right;
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  58 */     this.m_left.fixupVariables(vars, globalsSize);
/*  59 */     this.m_right.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/*  72 */     if ((null != this.m_left) && (this.m_left.canTraverseOutsideSubtree())) {
/*  73 */       return true;
/*     */     }
/*  75 */     if ((null != this.m_right) && (this.m_right.canTraverseOutsideSubtree())) {
/*  76 */       return true;
/*     */     }
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   public void setLeftRight(Expression l, Expression r)
/*     */   {
/*  90 */     this.m_left = l;
/*  91 */     this.m_right = r;
/*  92 */     l.exprSetParent(this);
/*  93 */     r.exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 111 */     XObject left = this.m_left.execute(xctxt, true);
/* 112 */     XObject right = this.m_right.execute(xctxt, true);
/*     */ 
/* 114 */     XObject result = operate(left, right);
/* 115 */     left.detach();
/* 116 */     right.detach();
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   public XObject operate(XObject left, XObject right)
/*     */     throws TransformerException
/*     */   {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public Expression getLeftOperand()
/*     */   {
/* 140 */     return this.m_left;
/*     */   }
/*     */ 
/*     */   public Expression getRightOperand()
/*     */   {
/* 146 */     return this.m_right;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 174 */     if (visitor.visitBinaryOperation(owner, this))
/*     */     {
/* 176 */       this.m_left.callVisitors(new LeftExprOwner(), visitor);
/* 177 */       this.m_right.callVisitors(this, visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 186 */     return this.m_right;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 194 */     exp.exprSetParent(this);
/* 195 */     this.m_right = exp;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 203 */     if (!isSameClass(expr)) {
/* 204 */       return false;
/*     */     }
/* 206 */     if (!this.m_left.deepEquals(((Operation)expr).m_left)) {
/* 207 */       return false;
/*     */     }
/* 209 */     if (!this.m_right.deepEquals(((Operation)expr).m_right)) {
/* 210 */       return false;
/*     */     }
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   class LeftExprOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     LeftExprOwner()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 156 */       return Operation.this.m_left;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 164 */       exp.exprSetParent(Operation.this);
/* 165 */       Operation.this.m_left = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Operation
 * JD-Core Version:    0.6.2
 */