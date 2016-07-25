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
/*     */ public abstract class UnaryOperation extends Expression
/*     */   implements ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = 6536083808424286166L;
/*     */   protected Expression m_right;
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  54 */     this.m_right.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/*  66 */     if ((null != this.m_right) && (this.m_right.canTraverseOutsideSubtree())) {
/*  67 */       return true;
/*     */     }
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */   public void setRight(Expression r)
/*     */   {
/*  81 */     this.m_right = r;
/*  82 */     r.exprSetParent(this);
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  99 */     return operate(this.m_right.execute(xctxt));
/*     */   }
/*     */ 
/*     */   public abstract XObject operate(XObject paramXObject)
/*     */     throws TransformerException;
/*     */ 
/*     */   public Expression getOperand()
/*     */   {
/* 118 */     return this.m_right;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 126 */     if (visitor.visitUnaryOperation(owner, this))
/*     */     {
/* 128 */       this.m_right.callVisitors(this, visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 138 */     return this.m_right;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 146 */     exp.exprSetParent(this);
/* 147 */     this.m_right = exp;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 155 */     if (!isSameClass(expr)) {
/* 156 */       return false;
/*     */     }
/* 158 */     if (!this.m_right.deepEquals(((UnaryOperation)expr).m_right)) {
/* 159 */       return false;
/*     */     }
/* 161 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.UnaryOperation
 * JD-Core Version:    0.6.2
 */