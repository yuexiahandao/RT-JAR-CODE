/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ public class WrappedRuntimeException extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = 7140414456714658073L;
/*    */   private Exception m_exception;
/*    */ 
/*    */   public WrappedRuntimeException(Exception e)
/*    */   {
/* 47 */     super(e.getMessage());
/*    */ 
/* 49 */     this.m_exception = e;
/*    */   }
/*    */ 
/*    */   public WrappedRuntimeException(String msg, Exception e)
/*    */   {
/* 62 */     super(msg);
/*    */ 
/* 64 */     this.m_exception = e;
/*    */   }
/*    */ 
/*    */   public Exception getException()
/*    */   {
/* 74 */     return this.m_exception;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.WrappedRuntimeException
 * JD-Core Version:    0.6.2
 */