/*    */ package com.sun.xml.internal.ws.api.model;
/*    */ 
/*    */ public enum ExceptionType
/*    */ {
/* 43 */   WSDLException(0), UserDefined(1);
/*    */ 
/*    */   private final int exceptionType;
/*    */ 
/* 46 */   private ExceptionType(int exceptionType) { this.exceptionType = exceptionType; }
/*    */ 
/*    */   public int value()
/*    */   {
/* 50 */     return this.exceptionType;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.ExceptionType
 * JD-Core Version:    0.6.2
 */