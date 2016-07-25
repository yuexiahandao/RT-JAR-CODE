/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import com.sun.imageio.stream.StreamCloser;
/*     */ import com.sun.imageio.stream.StreamCloser.CloseAction;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ 
/*     */ public class FileCacheImageOutputStream extends ImageOutputStreamImpl
/*     */ {
/*     */   private OutputStream stream;
/*     */   private File cacheFile;
/*     */   private RandomAccessFile cache;
/*  50 */   private long maxStreamPos = 0L;
/*     */   private final StreamCloser.CloseAction closeAction;
/*     */ 
/*     */   public FileCacheImageOutputStream(OutputStream paramOutputStream, File paramFile)
/*     */     throws IOException
/*     */   {
/*  80 */     if (paramOutputStream == null) {
/*  81 */       throw new IllegalArgumentException("stream == null!");
/*     */     }
/*  83 */     if ((paramFile != null) && (!paramFile.isDirectory())) {
/*  84 */       throw new IllegalArgumentException("Not a directory!");
/*     */     }
/*  86 */     this.stream = paramOutputStream;
/*  87 */     if (paramFile == null)
/*  88 */       this.cacheFile = Files.createTempFile("imageio", ".tmp", new FileAttribute[0]).toFile();
/*     */     else {
/*  90 */       this.cacheFile = Files.createTempFile(paramFile.toPath(), "imageio", ".tmp", new FileAttribute[0]).toFile();
/*     */     }
/*  92 */     this.cache = new RandomAccessFile(this.cacheFile, "rw");
/*     */ 
/*  94 */     this.closeAction = StreamCloser.createCloseAction(this);
/*  95 */     StreamCloser.addToQueue(this.closeAction);
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/*  99 */     checkClosed();
/* 100 */     this.bitOffset = 0;
/* 101 */     int i = this.cache.read();
/* 102 */     if (i != -1) {
/* 103 */       this.streamPos += 1L;
/*     */     }
/* 105 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 109 */     checkClosed();
/*     */ 
/* 111 */     if (paramArrayOfByte == null) {
/* 112 */       throw new NullPointerException("b == null!");
/*     */     }
/* 114 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
/* 115 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
/*     */     }
/*     */ 
/* 119 */     this.bitOffset = 0;
/*     */ 
/* 121 */     if (paramInt2 == 0) {
/* 122 */       return 0;
/*     */     }
/*     */ 
/* 125 */     int i = this.cache.read(paramArrayOfByte, paramInt1, paramInt2);
/* 126 */     if (i != -1) {
/* 127 */       this.streamPos += i;
/*     */     }
/* 129 */     return i;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/* 133 */     flushBits();
/* 134 */     this.cache.write(paramInt);
/* 135 */     this.streamPos += 1L;
/* 136 */     this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 140 */     flushBits();
/* 141 */     this.cache.write(paramArrayOfByte, paramInt1, paramInt2);
/* 142 */     this.streamPos += paramInt2;
/* 143 */     this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
/*     */   }
/*     */ 
/*     */   public long length() {
/*     */     try {
/* 148 */       checkClosed();
/* 149 */       return this.cache.length(); } catch (IOException localIOException) {
/*     */     }
/* 151 */     return -1L;
/*     */   }
/*     */ 
/*     */   public void seek(long paramLong)
/*     */     throws IOException
/*     */   {
/* 167 */     checkClosed();
/*     */ 
/* 169 */     if (paramLong < this.flushedPos) {
/* 170 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 173 */     this.cache.seek(paramLong);
/* 174 */     this.streamPos = this.cache.getFilePointer();
/* 175 */     this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
/* 176 */     this.bitOffset = 0;
/*     */   }
/*     */ 
/*     */   public boolean isCached()
/*     */   {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedFile()
/*     */   {
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedMemory()
/*     */   {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 229 */     this.maxStreamPos = this.cache.length();
/*     */ 
/* 231 */     seek(this.maxStreamPos);
/* 232 */     flushBefore(this.maxStreamPos);
/* 233 */     super.close();
/* 234 */     this.cache.close();
/* 235 */     this.cache = null;
/* 236 */     this.cacheFile.delete();
/* 237 */     this.cacheFile = null;
/* 238 */     this.stream.flush();
/* 239 */     this.stream = null;
/* 240 */     StreamCloser.removeFromQueue(this.closeAction);
/*     */   }
/*     */ 
/*     */   public void flushBefore(long paramLong) throws IOException {
/* 244 */     long l1 = this.flushedPos;
/* 245 */     super.flushBefore(paramLong);
/*     */ 
/* 247 */     long l2 = this.flushedPos - l1;
/* 248 */     if (l2 > 0L) {
/* 249 */       int i = 512;
/* 250 */       byte[] arrayOfByte = new byte[i];
/* 251 */       this.cache.seek(l1);
/* 252 */       while (l2 > 0L) {
/* 253 */         int j = (int)Math.min(l2, i);
/* 254 */         this.cache.readFully(arrayOfByte, 0, j);
/* 255 */         this.stream.write(arrayOfByte, 0, j);
/* 256 */         l2 -= j;
/*     */       }
/* 258 */       this.stream.flush();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.FileCacheImageOutputStream
 * JD-Core Version:    0.6.2
 */