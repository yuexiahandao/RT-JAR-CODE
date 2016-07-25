/*    */ package sun.rmi.transport.proxy;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ class HttpOutputStream extends ByteArrayOutputStream
/*    */ {
/*    */   protected OutputStream out;
/* 41 */   boolean responseSent = false;
/*    */ 
/* 79 */   private static byte[] emptyData = { 0 };
/*    */ 
/*    */   public HttpOutputStream(OutputStream paramOutputStream)
/*    */   {
/* 49 */     this.out = paramOutputStream;
/*    */   }
/*    */ 
/*    */   public synchronized void close()
/*    */     throws IOException
/*    */   {
/* 56 */     if (!this.responseSent)
/*    */     {
/* 62 */       if (size() == 0) {
/* 63 */         write(emptyData);
/*    */       }
/* 65 */       DataOutputStream localDataOutputStream = new DataOutputStream(this.out);
/* 66 */       localDataOutputStream.writeBytes("Content-type: application/octet-stream\r\n");
/* 67 */       localDataOutputStream.writeBytes("Content-length: " + size() + "\r\n");
/* 68 */       localDataOutputStream.writeBytes("\r\n");
/* 69 */       writeTo(localDataOutputStream);
/* 70 */       localDataOutputStream.flush();
/*    */ 
/* 73 */       reset();
/* 74 */       this.responseSent = true;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.HttpOutputStream
 * JD-Core Version:    0.6.2
 */