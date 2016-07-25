/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Neg extends UnaryOperation
/*    */ {
/*    */   static final long serialVersionUID = -6280607702375702291L;
/*    */ 
/*    */   public XObject operate(XObject right)
/*    */     throws TransformerException
/*    */   {
/* 48 */     return new XNumber(-right.num());
/*    */   }
/*    */ 
/*    */   public double num(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 64 */     return -this.m_right.num(xctxt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Neg
 * JD-Core Version:    0.6.2
 */