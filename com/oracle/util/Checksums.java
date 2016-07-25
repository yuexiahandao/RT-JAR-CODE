/*    */ package com.oracle.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.zip.Adler32;
/*    */ import sun.misc.JavaUtilZipAccess;
/*    */ import sun.misc.SharedSecrets;
/*    */ 
/*    */ public class Checksums
/*    */ {
/* 60 */   private static JavaUtilZipAccess juza = SharedSecrets.getJavaUtilZipAccess();
/*    */ 
/*    */   public static void update(Adler32 paramAdler32, ByteBuffer paramByteBuffer)
/*    */   {
/* 55 */     juza.update(paramAdler32, paramByteBuffer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.oracle.util.Checksums
 * JD-Core Version:    0.6.2
 */