/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class IgnoreAllErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 38 */   static Logger log = Logger.getLogger(IgnoreAllErrorHandler.class.getName());
/*    */ 
/* 43 */   static final boolean warnOnExceptions = System.getProperty("com.sun.org.apache.xml.internal.security.test.warn.on.exceptions", "false").equals("true");
/*    */ 
/* 47 */   static final boolean throwExceptions = System.getProperty("com.sun.org.apache.xml.internal.security.test.throw.exceptions", "false").equals("true");
/*    */ 
/*    */   public void warning(SAXParseException paramSAXParseException)
/*    */     throws SAXException
/*    */   {
/* 53 */     if (warnOnExceptions) {
/* 54 */       log.log(Level.WARNING, "", paramSAXParseException);
/*    */     }
/* 56 */     if (throwExceptions)
/* 57 */       throw paramSAXParseException;
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException paramSAXParseException)
/*    */     throws SAXException
/*    */   {
/* 64 */     if (warnOnExceptions) {
/* 65 */       log.log(Level.SEVERE, "", paramSAXParseException);
/*    */     }
/* 67 */     if (throwExceptions)
/* 68 */       throw paramSAXParseException;
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException paramSAXParseException)
/*    */     throws SAXException
/*    */   {
/* 76 */     if (warnOnExceptions) {
/* 77 */       log.log(Level.WARNING, "", paramSAXParseException);
/*    */     }
/* 79 */     if (throwExceptions)
/* 80 */       throw paramSAXParseException;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler
 * JD-Core Version:    0.6.2
 */