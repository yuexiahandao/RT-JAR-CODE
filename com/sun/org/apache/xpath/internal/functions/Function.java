/*     */ package com.sun.org.apache.xpath.internal.functions;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.compiler.Compiler;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public abstract class Function extends Expression
/*     */ {
/*     */   static final long serialVersionUID = 6927661240854599768L;
/*     */ 
/*     */   public void setArg(Expression arg, int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  61 */     reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   public void checkNumberArgs(int argNum)
/*     */     throws WrongNumberArgsException
/*     */   {
/*  76 */     if (argNum != 0)
/*  77 */       reportWrongNumberArgs();
/*     */   }
/*     */ 
/*     */   protected void reportWrongNumberArgs()
/*     */     throws WrongNumberArgsException
/*     */   {
/*  88 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("zero", null));
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 103 */     System.out.println("Error! Function.execute should not be called!");
/*     */ 
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   public void callArgVisitors(XPathVisitor visitor)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 121 */     if (visitor.visitFunction(owner, this))
/*     */     {
/* 123 */       callArgVisitors(visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 132 */     if (!isSameClass(expr)) {
/* 133 */       return false;
/*     */     }
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   public void postCompileStep(Compiler compiler)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.Function
 * JD-Core Version:    0.6.2
 */