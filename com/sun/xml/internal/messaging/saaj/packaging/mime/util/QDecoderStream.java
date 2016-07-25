/*    */ package com.sun.xml.internal.messaging.saaj.packaging.mime.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class QDecoderStream extends QPDecoderStream
/*    */ {
/*    */   public QDecoderStream(InputStream in)
/*    */   {
/* 51 */     super(in);
/*    */   }
/*    */ 
/*    */   public int read()
/*    */     throws IOException
/*    */   {
/* 67 */     int c = this.in.read();
/*    */ 
/* 69 */     if (c == 95)
/* 70 */       return 32;
/* 71 */     if (c == 61)
/*    */     {
/* 73 */       this.ba[0] = ((byte)this.in.read());
/* 74 */       this.ba[1] = ((byte)this.in.read());
/*    */       try
/*    */       {
/* 77 */         return ASCIIUtility.parseInt(this.ba, 0, 2, 16);
/*    */       } catch (NumberFormatException nex) {
/* 79 */         throw new IOException("Error in QP stream " + nex.getMessage());
/*    */       }
/*    */     }
/* 82 */     return c;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.util.QDecoderStream
 * JD-Core Version:    0.6.2
 */