/*    */ package com.sun.xml.internal.ws.api.server;
/*    */ 
/*    */ public abstract class Container
/*    */ {
/* 92 */   public static final Container NONE = new Container() {
/*    */     public <T> T getSPI(Class<T> spiType) {
/* 94 */       return null;
/*    */     }
/* 92 */   };
/*    */ 
/*    */   public abstract <T> T getSPI(Class<T> paramClass);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.Container
 * JD-Core Version:    0.6.2
 */