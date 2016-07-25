/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ class UndefLengthOutputStream extends FilterOutputStream
/*    */ {
/* 42 */   private boolean closed = false;
/*    */   ExchangeImpl t;
/*    */ 
/*    */   UndefLengthOutputStream(ExchangeImpl paramExchangeImpl, OutputStream paramOutputStream)
/*    */   {
/* 46 */     super(paramOutputStream);
/* 47 */     this.t = paramExchangeImpl;
/*    */   }
/*    */ 
/*    */   public void write(int paramInt) throws IOException {
/* 51 */     if (this.closed) {
/* 52 */       throw new IOException("stream closed");
/*    */     }
/* 54 */     this.out.write(paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 58 */     if (this.closed) {
/* 59 */       throw new IOException("stream closed");
/*    */     }
/* 61 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 65 */     if (this.closed) {
/* 66 */       return;
/*    */     }
/* 68 */     this.closed = true;
/* 69 */     flush();
/* 70 */     LeftOverInputStream localLeftOverInputStream = this.t.getOriginalInputStream();
/* 71 */     if (!localLeftOverInputStream.isClosed())
/*    */       try {
/* 73 */         localLeftOverInputStream.close();
/*    */       } catch (IOException localIOException) {
/*    */       }
/* 76 */     WriteFinishedEvent localWriteFinishedEvent = new WriteFinishedEvent(this.t);
/* 77 */     this.t.getHttpContext().getServerImpl().addEvent(localWriteFinishedEvent);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.UndefLengthOutputStream
 * JD-Core Version:    0.6.2
 */