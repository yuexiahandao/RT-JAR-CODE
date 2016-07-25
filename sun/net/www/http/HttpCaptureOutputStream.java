/*    */ package sun.net.www.http;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class HttpCaptureOutputStream extends FilterOutputStream
/*    */ {
/* 36 */   private HttpCapture capture = null;
/*    */ 
/*    */   public HttpCaptureOutputStream(OutputStream paramOutputStream, HttpCapture paramHttpCapture) {
/* 39 */     super(paramOutputStream);
/* 40 */     this.capture = paramHttpCapture;
/*    */   }
/*    */ 
/*    */   public void write(int paramInt) throws IOException
/*    */   {
/* 45 */     this.capture.sent(paramInt);
/* 46 */     this.out.write(paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte) throws IOException
/*    */   {
/* 51 */     for (int k : paramArrayOfByte) {
/* 52 */       this.capture.sent(k);
/*    */     }
/* 54 */     this.out.write(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 59 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 60 */       this.capture.sent(paramArrayOfByte[i]);
/*    */     }
/* 62 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public void flush() throws IOException
/*    */   {
/*    */     try {
/* 68 */       this.capture.flush();
/*    */     } catch (IOException localIOException) {
/*    */     }
/* 71 */     super.flush();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.HttpCaptureOutputStream
 * JD-Core Version:    0.6.2
 */