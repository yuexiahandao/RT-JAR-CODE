/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import com.sun.imageio.stream.StreamFinalizer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ 
/*     */ public class MemoryCacheImageInputStream extends ImageInputStreamImpl
/*     */ {
/*     */   private InputStream stream;
/*  50 */   private MemoryCache cache = new MemoryCache();
/*     */   private final Object disposerReferent;
/*     */   private final DisposerRecord disposerRecord;
/*     */ 
/*     */   public MemoryCacheImageInputStream(InputStream paramInputStream)
/*     */   {
/*  68 */     if (paramInputStream == null) {
/*  69 */       throw new IllegalArgumentException("stream == null!");
/*     */     }
/*  71 */     this.stream = paramInputStream;
/*     */ 
/*  73 */     this.disposerRecord = new StreamDisposerRecord(this.cache);
/*  74 */     if (getClass() == MemoryCacheImageInputStream.class) {
/*  75 */       this.disposerReferent = new Object();
/*  76 */       Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*     */     } else {
/*  78 */       this.disposerReferent = new StreamFinalizer(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/*  83 */     checkClosed();
/*  84 */     this.bitOffset = 0;
/*  85 */     long l = this.cache.loadFromStream(this.stream, this.streamPos + 1L);
/*  86 */     if (l >= this.streamPos + 1L) {
/*  87 */       return this.cache.read(this.streamPos++);
/*     */     }
/*  89 */     return -1;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  94 */     checkClosed();
/*     */ 
/*  96 */     if (paramArrayOfByte == null) {
/*  97 */       throw new NullPointerException("b == null!");
/*     */     }
/*  99 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
/* 100 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
/*     */     }
/*     */ 
/* 104 */     this.bitOffset = 0;
/*     */ 
/* 106 */     if (paramInt2 == 0) {
/* 107 */       return 0;
/*     */     }
/*     */ 
/* 110 */     long l = this.cache.loadFromStream(this.stream, this.streamPos + paramInt2);
/*     */ 
/* 112 */     paramInt2 = (int)(l - this.streamPos);
/*     */ 
/* 114 */     if (paramInt2 > 0) {
/* 115 */       this.cache.read(paramArrayOfByte, paramInt1, paramInt2, this.streamPos);
/* 116 */       this.streamPos += paramInt2;
/* 117 */       return paramInt2;
/*     */     }
/* 119 */     return -1;
/*     */   }
/*     */ 
/*     */   public void flushBefore(long paramLong) throws IOException
/*     */   {
/* 124 */     super.flushBefore(paramLong);
/* 125 */     this.cache.disposeBefore(paramLong);
/*     */   }
/*     */ 
/*     */   public boolean isCached()
/*     */   {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedFile()
/*     */   {
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCachedMemory()
/*     */   {
/* 165 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 173 */     super.close();
/* 174 */     this.disposerRecord.dispose();
/* 175 */     this.stream = null;
/* 176 */     this.cache = null;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class StreamDisposerRecord
/*     */     implements DisposerRecord
/*     */   {
/*     */     private MemoryCache cache;
/*     */ 
/*     */     public StreamDisposerRecord(MemoryCache paramMemoryCache)
/*     */     {
/* 192 */       this.cache = paramMemoryCache;
/*     */     }
/*     */ 
/*     */     public synchronized void dispose() {
/* 196 */       if (this.cache != null) {
/* 197 */         this.cache.reset();
/* 198 */         this.cache = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.MemoryCacheImageInputStream
 * JD-Core Version:    0.6.2
 */