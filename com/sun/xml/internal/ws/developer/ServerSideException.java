/*    */ package com.sun.xml.internal.ws.developer;
/*    */ 
/*    */ public class ServerSideException extends Exception
/*    */ {
/*    */   private final String className;
/*    */ 
/*    */   public ServerSideException(String className, String message)
/*    */   {
/* 43 */     super(message);
/* 44 */     this.className = className;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 48 */     String s = this.className;
/* 49 */     String message = getLocalizedMessage();
/* 50 */     return message != null ? s + ": " + message : s;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.ServerSideException
 * JD-Core Version:    0.6.2
 */