/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class FunctionMultiArgs extends Function3Args
/*     */ {
/*     */   static final long serialVersionUID = 7117257746138417181L;
/*     */   Expression[] m_args;
/*     */ 
/*     */   public Expression[] getArgs()
/*     */   {
/*  51 */     return this.m_args;
/*     */   }
/*     */ 
/*     */   public void setArg(Expression arg, int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  68 */     if (argNum < 3) {
/*  69 */       super.setArg(arg, argNum);
/*     */     }
/*     */     else {
/*  72 */       if (null == this.m_args)
/*     */       {
/*  74 */         this.m_args = new Expression[1];
/*  75 */         this.m_args[0] = arg;
/*     */       }
/*     */       else
/*     */       {
/*  81 */         Expression[] args = new Expression[this.m_args.length + 1];
/*     */ 
/*  83 */         System.arraycopy(this.m_args, 0, args, 0, this.m_args.length);
/*     */ 
/*  85 */         args[this.m_args.length] = arg;
/*  86 */         this.m_args = args;
/*     */       }
/*  88 */       arg.exprSetParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/* 104 */     super.fixupVariables(vars, globalsSize);
/* 105 */     if (null != this.m_args)
/*     */     {
/* 107 */       for (int i = 0; i < this.m_args.length; i++)
/*     */       {
/* 109 */         this.m_args[i].fixupVariables(vars, globalsSize);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/* 132 */     String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[] { "Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called." });
/*     */ 
/* 136 */     throw new RuntimeException(fMsg);
/*     */   }
/*     */ 
/*     */   public boolean canTraverseOutsideSubtree()
/*     */   {
/* 148 */     if (super.canTraverseOutsideSubtree()) {
/* 149 */       return true;
/*     */     }
/*     */ 
/* 152 */     int n = this.m_args.length;
/*     */ 
/* 154 */     for (int i = 0; i < n; i++)
/*     */     {
/* 156 */       if (this.m_args[i].canTraverseOutsideSubtree()) {
/* 157 */         return true;
/*     */       }
/*     */     }
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */   public void callArgVisitors(XPathVisitor visitor)
/*     */   {
/* 198 */     super.callArgVisitors(visitor);
/* 199 */     if (null != this.m_args)
/*     */     {
/* 201 */       int n = this.m_args.length;
/* 202 */       for (int i = 0; i < n; i++)
/*     */       {
/* 204 */         this.m_args[i].callVisitors(new ArgMultiOwner(i), visitor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 214 */     if (!super.deepEquals(expr)) {
/* 215 */       return false;
/*     */     }
/* 217 */     FunctionMultiArgs fma = (FunctionMultiArgs)expr;
/* 218 */     if (null != this.m_args)
/*     */     {
/* 220 */       int n = this.m_args.length;
/* 221 */       if ((null == fma) || (fma.m_args.length != n)) {
/* 222 */         return false;
/*     */       }
/* 224 */       for (int i = 0; i < n; i++)
/*     */       {
/* 226 */         if (!this.m_args[i].deepEquals(fma.m_args[i])) {
/* 227 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 231 */     else if (null != fma.m_args)
/*     */     {
/* 233 */       return false;
/*     */     }
/*     */ 
/* 236 */     return true;
/*     */   }
/*     */ 
/*     */   class ArgMultiOwner
/*     */     implements ExpressionOwner
/*     */   {
/*     */     int m_argIndex;
/*     */ 
/*     */     ArgMultiOwner(int index)
/*     */     {
/* 170 */       this.m_argIndex = index;
/*     */     }
/*     */ 
/*     */     public Expression getExpression()
/*     */     {
/* 178 */       return FunctionMultiArgs.this.m_args[this.m_argIndex];
/*     */     }
/*     */ 
/*     */     public void setExpression(Expression exp)
/*     */     {
/* 187 */       exp.exprSetParent(FunctionMultiArgs.this);
/* 188 */       FunctionMultiArgs.this.m_args[this.m_argIndex] = exp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FunctionMultiArgs
 * JD-Core Version:    0.6.2
 */