/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ abstract class LeftOverInputStream extends FilterInputStream
/*     */ {
/*     */   ExchangeImpl t;
/*     */   ServerImpl server;
/*  46 */   protected boolean closed = false;
/*  47 */   protected boolean eof = false;
/*  48 */   byte[] one = new byte[1];
/*     */ 
/*     */   public LeftOverInputStream(ExchangeImpl paramExchangeImpl, InputStream paramInputStream) {
/*  51 */     super(paramInputStream);
/*  52 */     this.t = paramExchangeImpl;
/*  53 */     this.server = paramExchangeImpl.getServerImpl();
/*     */   }
/*     */ 
/*     */   public boolean isDataBuffered()
/*     */     throws IOException
/*     */   {
/*  59 */     assert (this.eof);
/*  60 */     return super.available() > 0;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*  64 */     if (this.closed) {
/*  65 */       return;
/*     */     }
/*  67 */     this.closed = true;
/*  68 */     if (!this.eof)
/*  69 */       this.eof = drain(ServerConfig.getDrainAmount());
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/*  74 */     return this.closed;
/*     */   }
/*     */ 
/*     */   public boolean isEOF() {
/*  78 */     return this.eof;
/*     */   }
/*     */ 
/*     */   protected abstract int readImpl(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   public synchronized int read() throws IOException {
/*  84 */     if (this.closed) {
/*  85 */       throw new IOException("Stream is closed");
/*     */     }
/*  87 */     int i = readImpl(this.one, 0, 1);
/*  88 */     if ((i == -1) || (i == 0)) {
/*  89 */       return i;
/*     */     }
/*  91 */     return this.one[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  96 */     if (this.closed) {
/*  97 */       throw new IOException("Stream is closed");
/*     */     }
/*  99 */     return readImpl(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public boolean drain(long paramLong)
/*     */     throws IOException
/*     */   {
/* 109 */     int i = 2048;
/* 110 */     byte[] arrayOfByte = new byte[i];
/* 111 */     while (paramLong > 0L) {
/* 112 */       long l = readImpl(arrayOfByte, 0, i);
/* 113 */       if (l == -1L) {
/* 114 */         this.eof = true;
/* 115 */         return true;
/*     */       }
/* 117 */       paramLong -= l;
/*     */     }
/*     */ 
/* 120 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.LeftOverInputStream
 * JD-Core Version:    0.6.2
 */