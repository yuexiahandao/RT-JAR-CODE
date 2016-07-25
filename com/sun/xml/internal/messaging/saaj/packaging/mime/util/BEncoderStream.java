/*    */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class BEncoderStream extends BASE64EncoderStream
/*    */ {
/*    */   public BEncoderStream(OutputStream out)
/*    */   {
/* 51 */     super(out, 2147483647);
/*    */   }
/*    */ 
/*    */   public static int encodedLength(byte[] b)
/*    */   {
/* 60 */     return (b.length + 2) / 3 * 4;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream
 * JD-Core Version:    0.6.2
 */