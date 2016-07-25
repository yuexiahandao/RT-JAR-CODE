/*    */ package com.sun.org.apache.xerces.internal.xni;
/*    */ 
/*    */ public class XNIException extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = 9019819772686063775L;
/*    */   private Exception fException;
/*    */ 
/*    */   public XNIException(String message)
/*    */   {
/* 60 */     super(message);
/*    */   }
/*    */ 
/*    */   public XNIException(Exception exception)
/*    */   {
/* 69 */     super(exception.getMessage());
/* 70 */     this.fException = exception;
/*    */   }
/*    */ 
/*    */   public XNIException(String message, Exception exception)
/*    */   {
/* 80 */     super(message);
/* 81 */     this.fException = exception;
/*    */   }
/*    */ 
/*    */   public Exception getException()
/*    */   {
/* 90 */     return this.fException;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 94 */     return this.fException;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.XNIException
 * JD-Core Version:    0.6.2
 */