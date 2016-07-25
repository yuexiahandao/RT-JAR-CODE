/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class ChunkedOutputStream extends FilterOutputStream
/*     */ {
/*  52 */   private boolean closed = false;
/*     */   static final int CHUNK_SIZE = 4096;
/*     */   static final int OFFSET = 6;
/*  57 */   private int pos = 6;
/*  58 */   private int count = 0;
/*  59 */   private byte[] buf = new byte[4104];
/*     */   ExchangeImpl t;
/*     */ 
/*     */   ChunkedOutputStream(ExchangeImpl paramExchangeImpl, OutputStream paramOutputStream)
/*     */   {
/*  63 */     super(paramOutputStream);
/*  64 */     this.t = paramExchangeImpl;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/*  68 */     if (this.closed) {
/*  69 */       throw new StreamClosedException();
/*     */     }
/*  71 */     this.buf[(this.pos++)] = ((byte)paramInt);
/*  72 */     this.count += 1;
/*  73 */     if (this.count == 4096) {
/*  74 */       writeChunk();
/*     */     }
/*  76 */     assert (this.count < 4096);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  80 */     if (this.closed) {
/*  81 */       throw new StreamClosedException();
/*     */     }
/*  83 */     int i = 4096 - this.count;
/*  84 */     if (paramInt2 > i) {
/*  85 */       System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pos, i);
/*  86 */       this.count = 4096;
/*  87 */       writeChunk();
/*  88 */       paramInt2 -= i;
/*  89 */       paramInt1 += i;
/*  90 */       while (paramInt2 >= 4096) {
/*  91 */         System.arraycopy(paramArrayOfByte, paramInt1, this.buf, 6, 4096);
/*  92 */         paramInt2 -= 4096;
/*  93 */         paramInt1 += 4096;
/*  94 */         this.count = 4096;
/*  95 */         writeChunk();
/*     */       }
/*     */     }
/*  98 */     if (paramInt2 > 0) {
/*  99 */       System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pos, paramInt2);
/* 100 */       this.count += paramInt2;
/* 101 */       this.pos += paramInt2;
/*     */     }
/* 103 */     if (this.count == 4096)
/* 104 */       writeChunk();
/*     */   }
/*     */ 
/*     */   private void writeChunk()
/*     */     throws IOException
/*     */   {
/* 114 */     char[] arrayOfChar = Integer.toHexString(this.count).toCharArray();
/* 115 */     int i = arrayOfChar.length;
/* 116 */     int j = 4 - i;
/*     */ 
/* 118 */     for (int k = 0; k < i; k++) {
/* 119 */       this.buf[(j + k)] = ((byte)arrayOfChar[k]);
/*     */     }
/* 121 */     this.buf[(j + k++)] = 13;
/* 122 */     this.buf[(j + k++)] = 10;
/* 123 */     this.buf[(j + k++ + this.count)] = 13;
/* 124 */     this.buf[(j + k++ + this.count)] = 10;
/* 125 */     this.out.write(this.buf, j, k + this.count);
/* 126 */     this.count = 0;
/* 127 */     this.pos = 6;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 131 */     if (this.closed) {
/* 132 */       return;
/*     */     }
/* 134 */     flush();
/*     */     try
/*     */     {
/* 137 */       writeChunk();
/* 138 */       this.out.flush();
/* 139 */       LeftOverInputStream localLeftOverInputStream = this.t.getOriginalInputStream();
/* 140 */       if (!localLeftOverInputStream.isClosed())
/* 141 */         localLeftOverInputStream.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */     finally {
/* 147 */       this.closed = true;
/*     */     }
/*     */ 
/* 150 */     WriteFinishedEvent localWriteFinishedEvent = new WriteFinishedEvent(this.t);
/* 151 */     this.t.getHttpContext().getServerImpl().addEvent(localWriteFinishedEvent);
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 155 */     if (this.closed) {
/* 156 */       throw new StreamClosedException();
/*     */     }
/* 158 */     if (this.count > 0) {
/* 159 */       writeChunk();
/*     */     }
/* 161 */     this.out.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ChunkedOutputStream
 * JD-Core Version:    0.6.2
 */