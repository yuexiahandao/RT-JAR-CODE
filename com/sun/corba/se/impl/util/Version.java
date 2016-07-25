/*    */ package com.sun.corba.se.impl.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Version
/*    */ {
/*    */   public static final String PROJECT_NAME = "RMI-IIOP";
/*    */   public static final String VERSION = "1.0";
/*    */   public static final String BUILD = "0.0";
/*    */   public static final String BUILD_TIME = "unknown";
/*    */   public static final String FULL = "RMI-IIOP 1.0 (unknown)";
/*    */ 
/*    */   public static String asString()
/*    */   {
/* 45 */     return "RMI-IIOP 1.0 (unknown)";
/*    */   }
/*    */ 
/*    */   public static void main(String[] paramArrayOfString) {
/* 49 */     System.out.println("RMI-IIOP 1.0 (unknown)");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.Version
 * JD-Core Version:    0.6.2
 */