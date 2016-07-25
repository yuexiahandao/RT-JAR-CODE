/*    */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class WrappedSAXException extends RuntimeException
/*    */ {
/*    */   public final SAXException exception;
/*    */ 
/*    */   WrappedSAXException(SAXException e)
/*    */   {
/* 85 */     this.exception = e;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.WrappedSAXException
 * JD-Core Version:    0.6.2
 */