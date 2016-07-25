/*    */ package com.sun.corba.se.impl.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class ORBProperties
/*    */ {
/*    */   public static final String ORB_CLASS = "org.omg.CORBA.ORBClass=com.sun.corba.se.impl.orb.ORBImpl";
/*    */   public static final String ORB_SINGLETON_CLASS = "org.omg.CORBA.ORBSingletonClass=com.sun.corba.se.impl.orb.ORBSingleton";
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/*    */     try
/*    */     {
/* 49 */       String str = System.getProperty("java.home");
/* 50 */       File localFile = new File(str + File.separator + "lib" + File.separator + "orb.properties");
/*    */ 
/* 54 */       if (localFile.exists()) {
/* 55 */         return;
/*    */       }
/*    */ 
/* 58 */       FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/* 59 */       PrintWriter localPrintWriter = new PrintWriter(localFileOutputStream);
/*    */       try
/*    */       {
/* 62 */         localPrintWriter.println("org.omg.CORBA.ORBClass=com.sun.corba.se.impl.orb.ORBImpl");
/* 63 */         localPrintWriter.println("org.omg.CORBA.ORBSingletonClass=com.sun.corba.se.impl.orb.ORBSingleton");
/*    */       } finally {
/* 65 */         localPrintWriter.close();
/* 66 */         localFileOutputStream.close();
/*    */       }
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.ORBProperties
 * JD-Core Version:    0.6.2
 */