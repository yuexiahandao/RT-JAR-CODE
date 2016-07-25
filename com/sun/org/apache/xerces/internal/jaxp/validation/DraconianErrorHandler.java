/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ final class DraconianErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 37 */   private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();
/*    */ 
/*    */   public static DraconianErrorHandler getInstance()
/*    */   {
/* 44 */     return ERROR_HANDLER_INSTANCE;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException e)
/*    */     throws SAXException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException e) throws SAXException
/*    */   {
/* 54 */     throw e;
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException e) throws SAXException
/*    */   {
/* 59 */     throw e;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.DraconianErrorHandler
 * JD-Core Version:    0.6.2
 */