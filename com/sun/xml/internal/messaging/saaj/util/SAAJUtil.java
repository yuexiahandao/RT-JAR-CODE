/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.security.AccessControlException;
/*    */ 
/*    */ public final class SAAJUtil
/*    */ {
/*    */   public static boolean getSystemBoolean(String arg)
/*    */   {
/*    */     try
/*    */     {
/* 38 */       return Boolean.getBoolean(arg); } catch (AccessControlException ex) {
/*    */     }
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   public static String getSystemProperty(String arg)
/*    */   {
/*    */     try {
/* 46 */       return System.getProperty(arg); } catch (SecurityException ex) {
/*    */     }
/* 48 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.SAAJUtil
 * JD-Core Version:    0.6.2
 */