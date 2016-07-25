/*    */ package com.sun.istack.internal;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class SAXParseException2 extends SAXParseException
/*    */ {
/*    */   public SAXParseException2(String message, Locator locator)
/*    */   {
/* 39 */     super(message, locator);
/*    */   }
/*    */ 
/*    */   public SAXParseException2(String message, Locator locator, Exception e) {
/* 43 */     super(message, locator, e);
/*    */   }
/*    */ 
/*    */   public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
/* 47 */     super(message, publicId, systemId, lineNumber, columnNumber);
/*    */   }
/*    */ 
/*    */   public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e) {
/* 51 */     super(message, publicId, systemId, lineNumber, columnNumber, e);
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 55 */     return getException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.SAXParseException2
 * JD-Core Version:    0.6.2
 */