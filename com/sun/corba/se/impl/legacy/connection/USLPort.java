/*    */ package com.sun.corba.se.impl.legacy.connection;
/*    */ 
/*    */ public class USLPort
/*    */ {
/*    */   private String type;
/*    */   private int port;
/*    */ 
/*    */   public USLPort(String paramString, int paramInt)
/*    */   {
/* 35 */     this.type = paramString;
/* 36 */     this.port = paramInt;
/*    */   }
/*    */   public String getType() {
/* 39 */     return this.type; } 
/* 40 */   public int getPort() { return this.port; } 
/* 41 */   public String toString() { return this.type + ":" + this.port; }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.USLPort
 * JD-Core Version:    0.6.2
 */