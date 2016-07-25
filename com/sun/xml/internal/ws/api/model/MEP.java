/*    */ package com.sun.xml.internal.ws.api.model;
/*    */ 
/*    */ public enum MEP
/*    */ {
/* 34 */   REQUEST_RESPONSE(false), 
/* 35 */   ONE_WAY(false), 
/* 36 */   ASYNC_POLL(true), 
/* 37 */   ASYNC_CALLBACK(true);
/*    */ 
/*    */   public final boolean isAsync;
/*    */ 
/*    */   private MEP(boolean async)
/*    */   {
/* 45 */     this.isAsync = async;
/*    */   }
/*    */ 
/*    */   public final boolean isOneWay() {
/* 49 */     return this == ONE_WAY;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.MEP
 * JD-Core Version:    0.6.2
 */