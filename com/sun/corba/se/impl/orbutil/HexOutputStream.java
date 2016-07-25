/*    */ package com.sun.corba.se.impl.orbutil;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.StringWriter;
/*    */ 
/*    */ public class HexOutputStream extends OutputStream
/*    */ {
/* 41 */   private static final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*    */   private StringWriter writer;
/*    */ 
/*    */   public HexOutputStream(StringWriter paramStringWriter)
/*    */   {
/* 54 */     this.writer = paramStringWriter;
/*    */   }
/*    */ 
/*    */   public synchronized void write(int paramInt)
/*    */     throws IOException
/*    */   {
/* 65 */     this.writer.write(hex[(paramInt >> 4 & 0xF)]);
/* 66 */     this.writer.write(hex[(paramInt >> 0 & 0xF)]);
/*    */   }
/*    */ 
/*    */   public synchronized void write(byte[] paramArrayOfByte) throws IOException {
/* 70 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 76 */     for (int i = 0; i < paramInt2; i++)
/* 77 */       write(paramArrayOfByte[(paramInt1 + i)]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.HexOutputStream
 * JD-Core Version:    0.6.2
 */