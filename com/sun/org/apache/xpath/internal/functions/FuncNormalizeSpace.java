/*    */ package com.sun.org.apache.xpath.internal.functions;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*    */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*    */ import com.sun.org.apache.xpath.internal.XPathContext;
/*    */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*    */ import com.sun.org.apache.xpath.internal.objects.XString;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class FuncNormalizeSpace extends FunctionDef1Arg
/*    */ {
/*    */   static final long serialVersionUID = -3377956872032190880L;
/*    */ 
/*    */   public XObject execute(XPathContext xctxt)
/*    */     throws TransformerException
/*    */   {
/* 50 */     XMLString s1 = getArg0AsString(xctxt);
/*    */ 
/* 52 */     return (XString)s1.fixWhiteSpace(true, true, false);
/*    */   }
/*    */ 
/*    */   public void executeCharsToContentHandler(XPathContext xctxt, ContentHandler handler)
/*    */     throws TransformerException, SAXException
/*    */   {
/* 72 */     if (Arg0IsNodesetExpr())
/*    */     {
/* 74 */       int node = getArg0AsNode(xctxt);
/* 75 */       if (-1 != node)
/*    */       {
/* 77 */         DTM dtm = xctxt.getDTM(node);
/* 78 */         dtm.dispatchCharactersEvents(node, handler, true);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 83 */       XObject obj = execute(xctxt);
/* 84 */       obj.dispatchCharactersEvents(handler);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.functions.FuncNormalizeSpace
 * JD-Core Version:    0.6.2
 */