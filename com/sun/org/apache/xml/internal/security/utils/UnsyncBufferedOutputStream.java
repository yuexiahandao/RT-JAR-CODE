/*    */ package com.sun.org.apache.xml.internal.security.utils;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class UnsyncBufferedOutputStream extends OutputStream
/*    */ {
/*    */   final OutputStream out;
/*    */   final byte[] buf;
/*    */   static final int size = 8192;
/* 36 */   private static ThreadLocal bufCahce = new ThreadLocal() {
/*    */     protected synchronized Object initialValue() {
/* 38 */       return new byte[8192];
/*    */     }
/* 36 */   };
/*    */ 
/* 41 */   int pointer = 0;
/*    */ 
/*    */   public UnsyncBufferedOutputStream(OutputStream paramOutputStream)
/*    */   {
/* 47 */     this.buf = ((byte[])bufCahce.get());
/* 48 */     this.out = paramOutputStream;
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 53 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 58 */     int i = this.pointer + paramInt2;
/* 59 */     if (i > 8192) {
/* 60 */       flushBuffer();
/* 61 */       if (paramInt2 > 8192) {
/* 62 */         this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 63 */         return;
/*    */       }
/* 65 */       i = paramInt2;
/*    */     }
/* 67 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pointer, paramInt2);
/* 68 */     this.pointer = i;
/*    */   }
/*    */ 
/*    */   private final void flushBuffer() throws IOException {
/* 72 */     if (this.pointer > 0)
/* 73 */       this.out.write(this.buf, 0, this.pointer);
/* 74 */     this.pointer = 0;
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */     throws IOException
/*    */   {
/* 80 */     if (this.pointer >= 8192) {
/* 81 */       flushBuffer();
/*    */     }
/* 83 */     this.buf[(this.pointer++)] = ((byte)paramInt);
/*    */   }
/*    */ 
/*    */   public void flush()
/*    */     throws IOException
/*    */   {
/* 89 */     flushBuffer();
/* 90 */     this.out.flush();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 95 */     flush();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream
 * JD-Core Version:    0.6.2
 */