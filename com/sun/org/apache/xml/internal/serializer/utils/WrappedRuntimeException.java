/*    */ package com.sun.org.apache.xml.internal.serializer.utils;
/*    */ 
/*    */ public final class WrappedRuntimeException extends RuntimeException
/*    */ {
/*    */   static final long serialVersionUID = 7140414456714658073L;
/*    */   private Exception m_exception;
/*    */ 
/*    */   public WrappedRuntimeException(Exception e)
/*    */   {
/* 54 */     super(e.getMessage());
/*    */ 
/* 56 */     this.m_exception = e;
/*    */   }
/*    */ 
/*    */   public WrappedRuntimeException(String msg, Exception e)
/*    */   {
/* 69 */     super(msg);
/*    */ 
/* 71 */     this.m_exception = e;
/*    */   }
/*    */ 
/*    */   public Exception getException()
/*    */   {
/* 81 */     return this.m_exception;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException
 * JD-Core Version:    0.6.2
 */