/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class FunctionOneArg extends Function
/*     */   implements ExpressionOwner
/*     */ {
/*     */   static final long serialVersionUID = -5180174180765609758L;
/*     */   Expression m_arg0;
/*     */ 
/*     */   public Expression getArg0()
/*     */   {
/*  50 */     return this.m_arg0;
/*     */   }
/*     */ 
/*     */   public void setArg(Expression arg, int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  66 */     if (0 == argNum)
/*     */     {
/*  68 */       this.m_arg0 = arg;
/*  69 */       arg.exprSetParent(this);
/*     */     }
/*     */     else {
/*  72 */       reportWrongNumberArgs();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  85 */     if (argNum != 1)
/*  86 */       reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/*  96 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("one", null));
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/* 107 */     return this.m_arg0.canTraverseOutsideSubtree();
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 122 */     if (null != this.m_arg0)
/* 123 */       this.m_arg0.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public void callArgVisitors(XPathVisitor visitor)
/*     */   {
/* 131 */     if (null != this.m_arg0)
/* 132 */       this.m_arg0.callVisitors(this, visitor);
/*     */   }
/*     */ 
/*     */   public Expression getExpression()
/*     */   {
/* 141 */     return this.m_arg0;
/*     */   }
/*     */ 
/*     */   public void setExpression(Expression exp)
/*     */   {
/* 149 */     exp.exprSetParent(this);
/* 150 */     this.m_arg0 = exp;
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 158 */     if (!super.deepEquals(expr)) {
/* 159 */       return false;
/*     */     }
/* 161 */     if (null != this.m_arg0)
/*     */     {
/* 163 */       if (null == ((FunctionOneArg)expr).m_arg0) {
/* 164 */         return false;
/*     */       }
/* 166 */       if (!this.m_arg0.deepEquals(((FunctionOneArg)expr).m_arg0))
/* 167 */         return false;
/*     */     }
/* 169 */     else if (null != ((FunctionOneArg)expr).m_arg0) {
/* 170 */       return false;
/*     */     }
/* 172 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FunctionOneArg
 * JD-Core Version:    0.6.2
 */