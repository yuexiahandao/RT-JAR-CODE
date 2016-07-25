/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class TeeInputStream extends InputStream
/*    */ {
/*    */   protected InputStream source;
/*    */   protected OutputStream copySink;
/*    */ 
/*    */   public TeeInputStream(InputStream source, OutputStream sink)
/*    */   {
/* 45 */     this.copySink = sink;
/* 46 */     this.source = source;
/*    */   }
/*    */ 
/*    */   public int read() throws IOException {
/* 50 */     int result = this.source.read();
/* 51 */     this.copySink.write(result);
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   public int available() throws IOException {
/* 56 */     return this.source.available();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 60 */     this.source.close();
/*    */   }
/*    */ 
/*    */   public synchronized void mark(int readlimit) {
/* 64 */     this.source.mark(readlimit);
/*    */   }
/*    */ 
/*    */   public boolean markSupported() {
/* 68 */     return this.source.markSupported();
/*    */   }
/*    */ 
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 72 */     int result = this.source.read(b, off, len);
/* 73 */     this.copySink.write(b, off, len);
/* 74 */     return result;
/*    */   }
/*    */ 
/*    */   public int read(byte[] b) throws IOException {
/* 78 */     int result = this.source.read(b);
/* 79 */     this.copySink.write(b);
/* 80 */     return result;
/*    */   }
/*    */ 
/*    */   public synchronized void reset() throws IOException {
/* 84 */     this.source.reset();
/*    */   }
/*    */ 
/*    */   public long skip(long n) throws IOException {
/* 88 */     return this.source.skip(n);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.TeeInputStream
 * JD-Core Version:    0.6.2
 */