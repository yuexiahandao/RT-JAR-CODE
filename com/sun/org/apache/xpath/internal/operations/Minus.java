/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Minus extends Operation
/*    */ {
/*    */   static final long serialVersionUID = -5297672838170871043L;
/*    */ 
/*    */   public XObject operate(XObject left, XObject right)
/*    */     throws TransformerException
/*    */   {
/* 51 */     return new XNumber(left.num() - right.num());
/*    */   }
/*    */ 
/*    */   public double num(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 67 */     return this.m_left.num(xctxt) - this.m_right.num(xctxt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Minus
 * JD-Core Version:    0.6.2
 */