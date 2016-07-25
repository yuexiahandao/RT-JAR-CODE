/*    */ package com.sun.xml.internal.ws.message;
/*    */ 
/*    */ public abstract class Util
/*    */ {
/*    */   public static boolean parseBool(String value)
/*    */   {
/* 41 */     if (value.length() == 0) {
/* 42 */       return false;
/*    */     }
/* 44 */     char ch = value.charAt(0);
/* 45 */     return (ch == 't') || (ch == '1');
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.Util
 * JD-Core Version:    0.6.2
 */