/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncLocalPart extends FunctionDef1Arg
/*    */ {
/*    */   static final long serialVersionUID = 7591798770325814746L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     int context = getArg0AsNode(xctxt);
/* 50 */     if (-1 == context)
/* 51 */       return XString.EMPTYSTRING;
/* 52 */     DTM dtm = xctxt.getDTM(context);
/* 53 */     String s = context != -1 ? dtm.getLocalName(context) : "";
/* 54 */     if ((s.startsWith("#")) || (s.equals("xmlns"))) {
/* 55 */       return XString.EMPTYSTRING;
/*    */     }
/* 57 */     return new XString(s);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncLocalPart
 * JD-Core Version:    0.6.2
 */