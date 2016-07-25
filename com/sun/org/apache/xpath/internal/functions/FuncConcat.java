/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncConcat extends FunctionMultiArgs
/*    */ {
/*    */   static final long serialVersionUID = 1737228885202314413L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     StringBuffer sb = new StringBuffer();
/*    */ 
/* 52 */     sb.append(this.m_arg0.execute(xctxt).str());
/* 53 */     sb.append(this.m_arg1.execute(xctxt).str());
/*    */ 
/* 55 */     if (null != this.m_arg2) {
/* 56 */       sb.append(this.m_arg2.execute(xctxt).str());
/*    */     }
/* 58 */     if (null != this.m_args)
/*    */     {
/* 60 */       for (int i = 0; i < this.m_args.length; i++)
/*    */       {
/* 62 */         sb.append(this.m_args[i].execute(xctxt).str());
/*    */       }
/*    */     }
/*    */ 
/* 66 */     return new XString(sb.toString());
/*    */   }
/*    */ 
/*    */   public void checkNumberArgs(int argNum)
/*    */     throws WrongNumberArgsException
/*    */   {
/* 79 */     if (argNum < 2)
/* 80 */       reportWrongNumberArgs();
/*    */   }
/*    */ 
/*    */   protected void reportWrongNumberArgs()
/*    */     throws WrongNumberArgsException
/*    */   {
/* 90 */     throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("gtone", null));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncConcat
 * JD-Core Version:    0.6.2
 */