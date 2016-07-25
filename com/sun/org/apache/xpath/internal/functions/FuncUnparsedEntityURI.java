/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.Expression;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncUnparsedEntityURI extends FunctionOneArg
/*    */ {
/*    */   static final long serialVersionUID = 845309759097448178L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 48 */     String name = this.m_arg0.execute(xctxt).str();
/* 49 */     int context = xctxt.getCurrentNode();
/* 50 */     DTM dtm = xctxt.getDTM(context);
/* 51 */     int doc = dtm.getDocument();
/*    */ 
/* 53 */     String uri = dtm.getUnparsedEntityURI(name);
/*    */ 
/* 55 */     return new XString(uri);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncUnparsedEntityURI
 * JD-Core Version:    0.6.2
 */