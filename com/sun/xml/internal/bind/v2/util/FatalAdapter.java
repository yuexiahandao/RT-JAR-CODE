/*    */ package com.sun.xml.internal.bind.v2.util;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ public class FatalAdapter
/*    */   implements ErrorHandler
/*    */ {
/*    */   private final ErrorHandler core;
/*    */ 
/*    */   public FatalAdapter(ErrorHandler handler)
/*    */   {
/* 41 */     this.core = handler;
/*    */   }
/*    */ 
/*    */   public void warning(SAXParseException exception) throws SAXException {
/* 45 */     this.core.warning(exception);
/*    */   }
/*    */ 
/*    */   public void error(SAXParseException exception) throws SAXException {
/* 49 */     this.core.fatalError(exception);
/*    */   }
/*    */ 
/*    */   public void fatalError(SAXParseException exception) throws SAXException {
/* 53 */     this.core.fatalError(exception);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.FatalAdapter
 * JD-Core Version:    0.6.2
 */