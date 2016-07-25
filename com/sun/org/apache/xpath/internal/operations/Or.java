/*    */ package com.sun.org.apache.xpath.internal.operations;
/*    */ 
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class Or extends Operation
/*    */ {
/*    */   static final long serialVersionUID = -644107191353853079L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 50 */     XObject expr1 = this.m_left.execute(xctxt);
/*    */ 
/* 52 */     if (!expr1.bool())
/*    */     {
/* 54 */       XObject expr2 = this.m_right.execute(xctxt);
/*    */ 
/* 56 */       return expr2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
/*    */     }
/*    */ 
/* 59 */     return XBoolean.S_TRUE;
/*    */   }
/*    */ 
/*    */   public boolean bool(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 74 */     return (this.m_left.bool(xctxt)) || (this.m_right.bool(xctxt));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.operations.Or
 * JD-Core Version:    0.6.2
 */