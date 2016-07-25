/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ public enum ContentNegotiation
/*    */ {
/* 49 */   none, 
/* 50 */   pessimistic, 
/* 51 */   optimistic;
/*    */ 
/*    */   public static final String PROPERTY = "com.sun.xml.internal.ws.client.ContentNegotiation";
/*    */ 
/*    */   public static ContentNegotiation obtainFromSystemProperty()
/*    */   {
/*    */     try
/*    */     {
/* 67 */       String value = System.getProperty("com.sun.xml.internal.ws.client.ContentNegotiation");
/*    */ 
/* 69 */       if (value == null) return none;
/*    */ 
/* 71 */       return valueOf(value);
/*    */     }
/*    */     catch (Exception e) {
/*    */     }
/* 75 */     return none;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.ContentNegotiation
 * JD-Core Version:    0.6.2
 */