/*    */ package com.sun.org.apache.xerces.internal.impl;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Version
/*    */ {
/*    */ 
/*    */   /** @deprecated */
/* 77 */   public static final String fVersion = getVersion();
/*    */   private static final String fImmutableVersion = "Xerces-J 2.7.1";
/*    */ 
/*    */   public static String getVersion()
/*    */   {
/* 87 */     return "Xerces-J 2.7.1";
/*    */   }
/*    */ 
/*    */   public static void main(String[] argv)
/*    */   {
/* 99 */     System.out.println(fVersion);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.Version
 * JD-Core Version:    0.6.2
 */