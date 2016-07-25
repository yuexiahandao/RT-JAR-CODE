/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Div extends Operation
/*    */ {
/*    */   static final long serialVersionUID = 6220756595959798135L;
/*    */ 
/*    */   public XObject operate(XObject left, XObject right)
/*    */     throws TransformerException
/*    */   {
/* 50 */     return new XNumber(left.num() / right.num());
/*    */   }
/*    */ 
/*    */   public double num(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 66 */     return this.m_left.num(xctxt) / this.m_right.num(xctxt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Div
 * JD-Core Version:    0.6.2
 */