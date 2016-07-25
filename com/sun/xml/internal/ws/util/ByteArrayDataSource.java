/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.activation.DataSource;
/*    */ 
/*    */ public final class ByteArrayDataSource
/*    */   implements DataSource
/*    */ {
/*    */   private final String contentType;
/*    */   private final byte[] buf;
/*    */   private final int start;
/*    */   private final int len;
/*    */ 
/*    */   public ByteArrayDataSource(byte[] buf, String contentType)
/*    */   {
/* 47 */     this(buf, 0, buf.length, contentType);
/*    */   }
/*    */   public ByteArrayDataSource(byte[] buf, int length, String contentType) {
/* 50 */     this(buf, 0, length, contentType);
/*    */   }
/*    */   public ByteArrayDataSource(byte[] buf, int start, int length, String contentType) {
/* 53 */     this.buf = buf;
/* 54 */     this.start = start;
/* 55 */     this.len = length;
/* 56 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */   public String getContentType() {
/* 60 */     if (this.contentType == null)
/* 61 */       return "application/octet-stream";
/* 62 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public InputStream getInputStream() {
/* 66 */     return new ByteArrayInputStream(this.buf, this.start, this.len);
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 70 */     return null;
/*    */   }
/*    */ 
/*    */   public OutputStream getOutputStream() {
/* 74 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ByteArrayDataSource
 * JD-Core Version:    0.6.2
 */