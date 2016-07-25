/*    */ package com.sun.istack.internal;
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
/*    */   private final int len;
/*    */ 
/*    */   public ByteArrayDataSource(byte[] buf, String contentType)
/*    */   {
/* 45 */     this(buf, buf.length, contentType);
/*    */   }
/*    */   public ByteArrayDataSource(byte[] buf, int length, String contentType) {
/* 48 */     this.buf = buf;
/* 49 */     this.len = length;
/* 50 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */   public String getContentType() {
/* 54 */     if (this.contentType == null)
/* 55 */       return "application/octet-stream";
/* 56 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public InputStream getInputStream() {
/* 60 */     return new ByteArrayInputStream(this.buf, 0, this.len);
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   public OutputStream getOutputStream() {
/* 68 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.ByteArrayDataSource
 * JD-Core Version:    0.6.2
 */