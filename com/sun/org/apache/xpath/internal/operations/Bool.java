/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Bool extends UnaryOperation
/*    */ {
/*    */   static final long serialVersionUID = 44705375321914635L;
/*    */ 
/*    */   public XObject operate(XObject right)
/*    */     throws TransformerException
/*    */   {
/* 49 */     if (1 == right.getType()) {
/* 50 */       return right;
/*    */     }
/* 52 */     return right.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */   }
/*    */ 
/*    */   public boolean bool(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 67 */     return this.m_right.bool(xctxt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Bool
 * JD-Core Version:    0.6.2
 */