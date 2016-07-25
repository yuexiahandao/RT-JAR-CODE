/*    */ package com.sun.xml.internal.ws.api.message;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;
/*    */ 
/*    */ public abstract class ExceptionHasMessage extends JAXWSExceptionBase
/*    */ {
/*    */   public ExceptionHasMessage(String key, Object[] args)
/*    */   {
/* 44 */     super(key, args);
/*    */   }
/*    */ 
/*    */   public abstract Message getFaultMessage();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.ExceptionHasMessage
 * JD-Core Version:    0.6.2
 */