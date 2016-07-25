/*    */ package com.sun.istack.internal;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class SAXException2 extends SAXException
/*    */ {
/*    */   public SAXException2(String message)
/*    */   {
/* 38 */     super(message);
/*    */   }
/*    */ 
/*    */   public SAXException2(Exception e) {
/* 42 */     super(e);
/*    */   }
/*    */ 
/*    */   public SAXException2(String message, Exception e) {
/* 46 */     super(message, e);
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 50 */     return getException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.SAXException2
 * JD-Core Version:    0.6.2
 */