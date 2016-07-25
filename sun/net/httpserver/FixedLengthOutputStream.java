/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class FixedLengthOutputStream extends FilterOutputStream
/*     */ {
/*     */   private long remaining;
/*  44 */   private boolean eof = false;
/*  45 */   private boolean closed = false;
/*     */   ExchangeImpl t;
/*     */ 
/*     */   FixedLengthOutputStream(ExchangeImpl paramExchangeImpl, OutputStream paramOutputStream, long paramLong)
/*     */   {
/*  49 */     super(paramOutputStream);
/*  50 */     this.t = paramExchangeImpl;
/*  51 */     this.remaining = paramLong;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/*  55 */     if (this.closed) {
/*  56 */       throw new IOException("stream closed");
/*     */     }
/*  58 */     this.eof = (this.remaining == 0L);
/*  59 */     if (this.eof) {
/*  60 */       throw new StreamClosedException();
/*     */     }
/*  62 */     this.out.write(paramInt);
/*  63 */     this.remaining -= 1L;
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  67 */     if (this.closed) {
/*  68 */       throw new IOException("stream closed");
/*     */     }
/*  70 */     this.eof = (this.remaining == 0L);
/*  71 */     if (this.eof) {
/*  72 */       throw new StreamClosedException();
/*     */     }
/*  74 */     if (paramInt2 > this.remaining)
/*     */     {
/*  76 */       throw new IOException("too many bytes to write to stream");
/*     */     }
/*  78 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*  79 */     this.remaining -= paramInt2;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*  83 */     if (this.closed) {
/*  84 */       return;
/*     */     }
/*  86 */     this.closed = true;
/*  87 */     if (this.remaining > 0L) {
/*  88 */       this.t.close();
/*  89 */       throw new IOException("insufficient bytes written to stream");
/*     */     }
/*  91 */     flush();
/*  92 */     this.eof = true;
/*  93 */     LeftOverInputStream localLeftOverInputStream = this.t.getOriginalInputStream();
/*  94 */     if (!localLeftOverInputStream.isClosed())
/*     */       try {
/*  96 */         localLeftOverInputStream.close();
/*     */       } catch (IOException localIOException) {
/*     */       }
/*  99 */     WriteFinishedEvent localWriteFinishedEvent = new WriteFinishedEvent(this.t);
/* 100 */     this.t.getHttpContext().getServerImpl().addEvent(localWriteFinishedEvent);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.FixedLengthOutputStream
 * JD-Core Version:    0.6.2
 */