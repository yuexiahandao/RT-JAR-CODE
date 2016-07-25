/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Function2Args extends FunctionOneArg
/*     */ {
/*     */   static final long serialVersionUID = 5574294996842710641L;
/*     */   Expression m_arg1;
/*     */ 
/*     */   public Expression getArg1()
/*     */   {
/*  50 */     return this.m_arg1;
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*  65 */     super.fixupVariables(vars, globalsSize);
/*  66 */     if (null != this.m_arg1)
/*  67 */       this.m_arg1.fixupVariables(vars, globalsSize);
/*     */   }
/*     */ 
/*     */   public void setArg(Expression arg, int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  85 */     if (argNum == 0) {
/*  86 */       super.setArg(arg, argNum);
/*  87 */     } else if (1 == argNum)
/*     */     {
/*  89 */       this.m_arg1 = arg;
/*  90 */       arg.exprSetParent(this);
/*     */     }
/*     */     else {
/*  93 */       reportWrongNumberArgs();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/* 106 */     if (argNum != 2)
/* 107 */       reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/* 117 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("two", null));
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/* 128 */     return super.canTraverseOutsideSubtree() ? true : this.m_arg1.canTraverseOutsideSubtree();
/*     */   }
/*     */ 
/*     */   public void callArgVisitors(XPathVisitor visitor)
/*     */   {
/* 159 */     super.callArgVisitors(visitor);
/* 160 */     if (null != this.m_arg1)
/* 161 */       this.m_arg1.callVisitors(new Arg1Owner(), visitor);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 169 */     if (!super.deepEquals(expr)) {
/* 170 */       return false;
/*     */     }
/* 172 */     if (null != this.m_arg1)
/*     */     {
/* 174 */       if (null == ((Function2Args)expr).m_arg1) {
/* 175 */         return false;
/*     */       }
/* 177 */       if (!this.m_arg1.deepEquals(((Function2Args)expr).m_arg1))
/* 178 */         return false;
/*     */     }
/* 180 */     else if (null != ((Function2Args)expr).m_arg1) {
/* 181 */       return false;
/*     */     }
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   class Arg1Owner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     Arg1Owner()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 139 */       return Function2Args.this.m_arg1;
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 148 */       exp.exprSetParent(Function2Args.this);
/* 149 */       Function2Args.this.m_arg1 = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.Function2Args
 * JD-Core Version:    0.6.2
 */