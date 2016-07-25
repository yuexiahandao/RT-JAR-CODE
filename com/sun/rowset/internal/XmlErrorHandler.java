/*    */ package com.sun.rowset.internal;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ public class XmlErrorHandler extends DefaultHandler
/*    */ {
/* 44 */   public int errorCounter = 0;
/*    */ 
/*    */   public void error(SAXParseException paramSAXParseException) throws SAXException {
/* 47 */     this.errorCounter += 1;
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException paramSAXParseException) throws SAXException
/*    */   {
/* 52 */     this.errorCounter += 1;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException paramSAXParseException)
/*    */     throws SAXException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.XmlErrorHandler
 * JD-Core Version:    0.6.2
 */