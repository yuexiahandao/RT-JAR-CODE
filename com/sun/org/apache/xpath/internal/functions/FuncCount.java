/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncCount extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = -7116225100474153751L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 60 */     DTMIterator nl = this.m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
/* 61 */     int i = nl.getLength();
/* 62 */     nl.detach();
/*    */ 
/* 64 */     return new XNumber(i);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncCount
 * JD-Core Version:    0.6.2
 */