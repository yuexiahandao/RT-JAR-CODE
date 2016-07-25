/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class MemoryCacheImageOutputStream extends ImageOutputStreamImpl
/*     */ {
/*     */   private OutputStream stream;
/*  46 */   private MemoryCache cache = new MemoryCache();
/*     */ 
/*     */   public MemoryCacheImageOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  58 */     if (paramOutputStream == null) {
/*  59 */       throw new IllegalArgumentException("stream == null!");
/*     */     }
/*  61 */     this.stream = paramOutputStream;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/*  65 */     checkClosed();
/*     */ 
/*  67 */     this.bitOffset = 0;
/*     */ 
/*  69 */     int i = this.cache.read(this.streamPos);
/*  70 */     if (i != -1) {
/*  71 */       this.streamPos += 1L;
/*     */     }
/*  73 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  77 */     checkClosed();
/*     */ 
/*  79 */     if (paramArrayOfByte == null) {
/*  80 */       throw new NullPointerException("b == null!");
/*     */     }
/*     */ 
/*  83 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
/*  84 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
/*     */     }
/*     */ 
/*  88 */     this.bitOffset = 0;
/*     */ 
/*  90 */     if (paramInt2 == 0) {
/*  91 */       return 0;
/*     */     }
/*     */ 
/*  96 */     long l = this.cache.getLength() - this.streamPos;
/*  97 */     if (l <= 0L) {
/*  98 */       return -1;
/*     */     }
/*     */ 
/* 104 */     paramInt2 = (int)Math.min(l, paramInt2);
/* 105 */     this.cache.read(paramArrayOfByte, paramInt1, paramInt2, this.streamPos);
/* 106 */     this.streamPos += paramInt2;
/* 107 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/* 111 */     flushBits();
/* 112 */     this.cache.write(paramInt, this.streamPos);
/* 113 */     this.streamPos += 1L;
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 117 */     flushBits();
/* 118 */     this.cache.write(paramArrayOfByte, paramInt1, paramInt2, this.streamPos);
/* 119 */     this.streamPos += paramInt2;
/*     */   }
/*     */ 
/*     */   public long length() {
/*     */     try {
/* 124 */       checkClosed();
/* 125 */       return this.cache.getLength(); } catch (IOException localIOException) {
/*     */     }
/* 127 */     return -1L;
/*     */   }
/*     */ 
/*     */   public boolean isCached()
/*     */   {
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedFile()
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCachedMemory()
/*     */   {
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 178 */     long l = this.cache.getLength();
/* 179 */     seek(l);
/* 180 */     flushBefore(l);
/* 181 */     super.close();
/* 182 */     this.cache.reset();
/* 183 */     this.cache = null;
/* 184 */     this.stream = null;
/*     */   }
/*     */ 
/*     */   public void flushBefore(long paramLong) throws IOException {
/* 188 */     long l1 = this.flushedPos;
/* 189 */     super.flushBefore(paramLong);
/*     */ 
/* 191 */     long l2 = this.flushedPos - l1;
/* 192 */     this.cache.writeToStream(this.stream, l1, l2);
/* 193 */     this.cache.disposeBefore(this.flushedPos);
/* 194 */     this.stream.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.MemoryCacheImageOutputStream
 * JD-Core Version:    0.6.2
 */