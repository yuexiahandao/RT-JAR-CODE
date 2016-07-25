/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ 
/*    */ public class FuncNamespace extends FunctionDef1Arg
/*    */ {
/*    */   static final long serialVersionUID = -4695674566722321237L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 49 */     int context = getArg0AsNode(xctxt);
/*    */     String s;
/* 52 */     if (context != -1)
/*    */     {
/* 54 */       DTM dtm = xctxt.getDTM(context);
/* 55 */       int t = dtm.getNodeType(context);
/*    */       String s;
/* 56 */       if (t == 1)
/*    */       {
/* 58 */         s = dtm.getNamespaceURI(context);
/*    */       }
/* 60 */       else if (t == 2)
/*    */       {
/* 66 */         String s = dtm.getNodeName(context);
/* 67 */         if ((s.startsWith("xmlns:")) || (s.equals("xmlns"))) {
/* 68 */           return XString.EMPTYSTRING;
/*    */         }
/* 70 */         s = dtm.getNamespaceURI(context);
/*    */       }
/*    */       else {
/* 73 */         return XString.EMPTYSTRING;
/*    */       }
/*    */     } else {
/* 76 */       return XString.EMPTYSTRING;
/*    */     }
/*    */     String s;
/* 78 */     return null == s ? XString.EMPTYSTRING : new XString(s);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncNamespace
 * JD-Core Version:    0.6.2
 */