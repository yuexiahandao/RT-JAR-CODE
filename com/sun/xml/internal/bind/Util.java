/*    */ package com.sun.xml.internal.bind;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public abstract class Util
/*    */ {
/*    */   public static Logger getClassLogger()
/*    */   {
/*    */     try
/*    */     {
/* 44 */       StackTraceElement[] trace = new Exception().getStackTrace();
/* 45 */       return Logger.getLogger(trace[1].getClassName()); } catch (SecurityException _) {
/*    */     }
/* 47 */     return Logger.getLogger("com.sun.xml.internal.bind");
/*    */   }
/*    */ 
/*    */   public static String getSystemProperty(String name)
/*    */   {
/*    */     try
/*    */     {
/* 56 */       return System.getProperty(name); } catch (SecurityException e) {
/*    */     }
/* 58 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.Util
 * JD-Core Version:    0.6.2
 */