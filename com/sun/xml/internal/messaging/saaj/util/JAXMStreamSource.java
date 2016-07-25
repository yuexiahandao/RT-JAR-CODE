/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ 
/*    */ public class JAXMStreamSource extends StreamSource
/*    */ {
/*    */   InputStream in;
/*    */   Reader reader;
/* 42 */   private static final boolean lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");
/*    */ 
/*    */   public JAXMStreamSource(InputStream is) throws IOException {
/* 45 */     if (lazyContentLength) {
/* 46 */       this.in = is;
/* 47 */     } else if ((is instanceof ByteInputStream)) {
/* 48 */       this.in = ((ByteInputStream)is);
/*    */     } else {
/* 50 */       ByteOutputStream bout = new ByteOutputStream();
/* 51 */       bout.write(is);
/* 52 */       this.in = bout.newInputStream();
/*    */     }
/*    */   }
/*    */ 
/*    */   public JAXMStreamSource(Reader rdr) throws IOException
/*    */   {
/* 58 */     if (lazyContentLength) {
/* 59 */       this.reader = rdr;
/* 60 */       return;
/*    */     }
/* 62 */     CharWriter cout = new CharWriter();
/* 63 */     char[] temp = new char[1024];
/*    */     int len;
/* 66 */     while (-1 != (len = rdr.read(temp))) {
/* 67 */       cout.write(temp, 0, len);
/*    */     }
/* 69 */     this.reader = new CharReader(cout.getChars(), cout.getCount());
/*    */   }
/*    */ 
/*    */   public InputStream getInputStream() {
/* 73 */     return this.in;
/*    */   }
/*    */ 
/*    */   public Reader getReader() {
/* 77 */     return this.reader;
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException {
/* 81 */     if (this.in != null)
/* 82 */       this.in.reset();
/* 83 */     if (this.reader != null)
/* 84 */       this.reader.reset();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.JAXMStreamSource
 * JD-Core Version:    0.6.2
 */