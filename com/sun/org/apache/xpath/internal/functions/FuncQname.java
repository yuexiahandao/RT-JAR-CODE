/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncQname extends FunctionDef1Arg
/*    */ {
/*    */   static final long serialVersionUID = -1532307875532617380L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     int context = getArg0AsNode(xctxt);
/*    */     XObject val;
/*    */     XObject val;
/* 52 */     if (-1 != context)
/*    */     {
/* 54 */       DTM dtm = xctxt.getDTM(context);
/* 55 */       String qname = dtm.getNodeNameX(context);
/* 56 */       val = null == qname ? XString.EMPTYSTRING : new XString(qname);
/*    */     }
/*    */     else
/*    */     {
/* 60 */       val = XString.EMPTYSTRING;
/*    */     }
/*    */ 
/* 63 */     return val;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncQname
 * JD-Core Version:    0.6.2
 */