/*    */ package com.sun.activation.registries;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class LogSupport
/*    */ {
/* 35 */   private static boolean debug = false;
/*    */ 
/* 45 */   private static Logger logger = Logger.getLogger("javax.activation");
/*    */ 
/* 37 */   private static final Level level = Level.FINE;
/*    */ 
/*    */   public static void log(String msg)
/*    */   {
/* 56 */     if (debug)
/* 57 */       System.out.println(msg);
/* 58 */     logger.log(level, msg);
/*    */   }
/*    */ 
/*    */   public static void log(String msg, Throwable t) {
/* 62 */     if (debug)
/* 63 */       System.out.println(msg + "; Exception: " + t);
/* 64 */     logger.log(level, msg, t);
/*    */   }
/*    */ 
/*    */   public static boolean isLoggable() {
/* 68 */     return (debug) || (logger.isLoggable(level));
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 41 */       debug = Boolean.getBoolean("javax.activation.debug");
/*    */     }
/*    */     catch (Throwable t)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.activation.registries.LogSupport
 * JD-Core Version:    0.6.2
 */