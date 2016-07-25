/*    */ package sun.net.www.http;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class HttpCaptureInputStream extends FilterInputStream
/*    */ {
/* 36 */   private HttpCapture capture = null;
/*    */ 
/*    */   public HttpCaptureInputStream(InputStream paramInputStream, HttpCapture paramHttpCapture) {
/* 39 */     super(paramInputStream);
/* 40 */     this.capture = paramHttpCapture;
/*    */   }
/*    */ 
/*    */   public int read() throws IOException
/*    */   {
/* 45 */     int i = super.read();
/* 46 */     this.capture.received(i);
/* 47 */     return i;
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/*    */     try {
/* 53 */       this.capture.flush();
/*    */     } catch (IOException localIOException) {
/*    */     }
/* 56 */     super.close();
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 61 */     int i = super.read(paramArrayOfByte);
/* 62 */     for (int j = 0; j < i; j++) {
/* 63 */       this.capture.received(paramArrayOfByte[j]);
/*    */     }
/* 65 */     return i;
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 70 */     int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/* 71 */     for (int j = 0; j < i; j++) {
/* 72 */       this.capture.received(paramArrayOfByte[(paramInt1 + j)]);
/*    */     }
/* 74 */     return i;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.HttpCaptureInputStream
 * JD-Core Version:    0.6.2
 */