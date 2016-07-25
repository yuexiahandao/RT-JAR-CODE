/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class String extends UnaryOperation
/*    */ {
/*    */   static final long serialVersionUID = 2973374377453022888L;
/*    */ 
/*    */   public XObject operate(XObject right)
/*    */     throws TransformerException
/*    */   {
/* 47 */     return (XString)right.xstr();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.String
 * JD-Core Version:    0.6.2
 */