/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*    */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncSum extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = -2719049259574677519L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 51 */     DTMIterator nodes = this.m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
/* 52 */     double sum = 0.0D;
/*    */     int pos;
/* 55 */     while (-1 != (pos = nodes.nextNode()))
/*    */     {
/* 57 */       DTM dtm = nodes.getDTM(pos);
/* 58 */       XMLString s = dtm.getStringValue(pos);
/*    */ 
/* 60 */       if (null != s)
/* 61 */         sum += s.toDouble();
/*    */     }
/* 63 */     nodes.detach();
/*    */ 
/* 65 */     return new XNumber(sum);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncSum
 * JD-Core Version:    0.6.2
 */