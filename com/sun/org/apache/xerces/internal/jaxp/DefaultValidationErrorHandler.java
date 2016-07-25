/*    */ package com.sun.org.apache.xerces.internal.jaxp;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Locale;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ class DefaultValidationErrorHandler extends DefaultHandler
/*    */ {
/* 33 */   private static int ERROR_COUNT_LIMIT = 10;
/* 34 */   private int errorCount = 0;
/* 35 */   private Locale locale = Locale.getDefault();
/*    */ 
/*    */   public DefaultValidationErrorHandler(Locale locale) {
/* 38 */     this.locale = locale;
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException e) throws SAXException
/*    */   {
/* 43 */     if (this.errorCount >= ERROR_COUNT_LIMIT)
/*    */     {
/* 45 */       return;
/* 46 */     }if (this.errorCount == 0)
/*    */     {
/* 48 */       System.err.println(SAXMessageFormatter.formatMessage(this.locale, "errorHandlerNotSet", new Object[] { Integer.valueOf(this.errorCount) }));
/*    */     }
/*    */ 
/* 52 */     String systemId = e.getSystemId();
/* 53 */     if (systemId == null) {
/* 54 */       systemId = "null";
/*    */     }
/* 56 */     String message = "Error: URI=" + systemId + " Line=" + e.getLineNumber() + ": " + e.getMessage();
/*    */ 
/* 59 */     System.err.println(message);
/* 60 */     this.errorCount += 1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.DefaultValidationErrorHandler
 * JD-Core Version:    0.6.2
 */