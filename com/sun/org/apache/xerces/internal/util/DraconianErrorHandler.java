/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class DraconianErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 77 */   public static final ErrorHandler theInstance = new DraconianErrorHandler();
/*    */ 
/*    */   public void error(SAXParseException e)
/*    */     throws SAXException
/*    */   {
/* 82 */     throw e;
/*    */   }
/*    */   public void fatalError(SAXParseException e) throws SAXException {
/* 85 */     throw e;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException e)
/*    */     throws SAXException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DraconianErrorHandler
 * JD-Core Version:    0.6.2
 */