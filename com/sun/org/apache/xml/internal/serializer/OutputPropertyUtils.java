/*    */ package com.sun.org.apache.xml.internal.serializer;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ public final class OutputPropertyUtils
/*    */ {
/*    */   public static boolean getBooleanProperty(String key, Properties props)
/*    */   {
/* 54 */     String s = props.getProperty(key);
/*    */ 
/* 56 */     if ((null == s) || (!s.equals("yes"))) {
/* 57 */       return false;
/*    */     }
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   public static int getIntProperty(String key, Properties props)
/*    */   {
/* 77 */     String s = props.getProperty(key);
/*    */ 
/* 79 */     if (null == s) {
/* 80 */       return 0;
/*    */     }
/* 82 */     return Integer.parseInt(s);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.OutputPropertyUtils
 * JD-Core Version:    0.6.2
 */