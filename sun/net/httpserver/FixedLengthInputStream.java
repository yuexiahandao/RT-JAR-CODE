/*    */ package sun.net.httpserver;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ class FixedLengthInputStream extends LeftOverInputStream
/*    */ {
/*    */   private long remaining;
/*    */ 
/*    */   FixedLengthInputStream(ExchangeImpl paramExchangeImpl, InputStream paramInputStream, long paramLong)
/*    */   {
/* 43 */     super(paramExchangeImpl, paramInputStream);
/* 44 */     this.remaining = paramLong;
/*    */   }
/*    */ 
/*    */   protected int readImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*    */   {
/* 49 */     this.eof = (this.remaining == 0L);
/* 50 */     if (this.eof) {
/* 51 */       return -1;
/*    */     }
/* 53 */     if (paramInt2 > this.remaining) {
/* 54 */       paramInt2 = (int)this.remaining;
/*    */     }
/* 56 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 57 */     if (i > -1) {
/* 58 */       this.remaining -= i;
/* 59 */       if (this.remaining == 0L) {
/* 60 */         this.t.getServerImpl().requestCompleted(this.t.getConnection());
/*    */       }
/*    */     }
/* 63 */     return i;
/*    */   }
/*    */ 
/*    */   public int available() throws IOException {
/* 67 */     if (this.eof) {
/* 68 */       return 0;
/*    */     }
/* 70 */     int i = this.in.available();
/* 71 */     return i < this.remaining ? i : (int)this.remaining;
/*    */   }
/*    */   public boolean markSupported() {
/* 74 */     return false;
/*    */   }
/*    */   public void mark(int paramInt) {
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException {
/* 80 */     throw new IOException("mark/reset not supported");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.FixedLengthInputStream
 * JD-Core Version:    0.6.2
 */