/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import com.sun.imageio.stream.StreamCloser;
/*     */ import com.sun.imageio.stream.StreamCloser.CloseAction;
/*     */ import com.sun.imageio.stream.StreamFinalizer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ 
/*     */ public class FileCacheImageInputStream extends ImageInputStreamImpl
/*     */ {
/*     */   private InputStream stream;
/*     */   private File cacheFile;
/*     */   private RandomAccessFile cache;
/*     */   private static final int BUFFER_LENGTH = 1024;
/*  54 */   private byte[] buf = new byte[1024];
/*     */ 
/*  56 */   private long length = 0L;
/*     */ 
/*  58 */   private boolean foundEOF = false;
/*     */   private final Object disposerReferent;
/*     */   private final DisposerRecord disposerRecord;
/*     */   private final StreamCloser.CloseAction closeAction;
/*     */ 
/*     */   public FileCacheImageInputStream(InputStream paramInputStream, File paramFile)
/*     */     throws IOException
/*     */   {
/*  94 */     if (paramInputStream == null) {
/*  95 */       throw new IllegalArgumentException("stream == null!");
/*     */     }
/*  97 */     if ((paramFile != null) && (!paramFile.isDirectory())) {
/*  98 */       throw new IllegalArgumentException("Not a directory!");
/*     */     }
/* 100 */     this.stream = paramInputStream;
/* 101 */     if (paramFile == null)
/* 102 */       this.cacheFile = Files.createTempFile("imageio", ".tmp", new FileAttribute[0]).toFile();
/*     */     else {
/* 104 */       this.cacheFile = Files.createTempFile(paramFile.toPath(), "imageio", ".tmp", new FileAttribute[0]).toFile();
/*     */     }
/* 106 */     this.cache = new RandomAccessFile(this.cacheFile, "rw");
/*     */ 
/* 108 */     this.closeAction = StreamCloser.createCloseAction(this);
/* 109 */     StreamCloser.addToQueue(this.closeAction);
/*     */ 
/* 111 */     this.disposerRecord = new StreamDisposerRecord(this.cacheFile, this.cache);
/* 112 */     if (getClass() == FileCacheImageInputStream.class) {
/* 113 */       this.disposerReferent = new Object();
/* 114 */       Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*     */     } else {
/* 116 */       this.disposerReferent = new StreamFinalizer(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   private long readUntil(long paramLong)
/*     */     throws IOException
/*     */   {
/* 128 */     if (paramLong < this.length) {
/* 129 */       return paramLong;
/*     */     }
/*     */ 
/* 132 */     if (this.foundEOF) {
/* 133 */       return this.length;
/*     */     }
/*     */ 
/* 136 */     long l = paramLong - this.length;
/* 137 */     this.cache.seek(this.length);
/* 138 */     while (l > 0L)
/*     */     {
/* 141 */       int i = this.stream.read(this.buf, 0, (int)Math.min(l, 1024L));
/*     */ 
/* 143 */       if (i == -1) {
/* 144 */         this.foundEOF = true;
/* 145 */         return this.length;
/*     */       }
/*     */ 
/* 148 */       this.cache.write(this.buf, 0, i);
/* 149 */       l -= i;
/* 150 */       this.length += i;
/*     */     }
/*     */ 
/* 153 */     return paramLong;
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 157 */     checkClosed();
/* 158 */     this.bitOffset = 0;
/* 159 */     long l1 = this.streamPos + 1L;
/* 160 */     long l2 = readUntil(l1);
/* 161 */     if (l2 >= l1) {
/* 162 */       this.cache.seek(this.streamPos++);
/* 163 */       return this.cache.read();
/*     */     }
/* 165 */     return -1;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 170 */     checkClosed();
/*     */ 
/* 172 */     if (paramArrayOfByte == null) {
/* 173 */       throw new NullPointerException("b == null!");
/*     */     }
/*     */ 
/* 176 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
/* 177 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
/*     */     }
/*     */ 
/* 181 */     this.bitOffset = 0;
/*     */ 
/* 183 */     if (paramInt2 == 0) {
/* 184 */       return 0;
/*     */     }
/*     */ 
/* 187 */     long l = readUntil(this.streamPos + paramInt2);
/*     */ 
/* 190 */     paramInt2 = (int)Math.min(paramInt2, l - this.streamPos);
/* 191 */     if (paramInt2 > 0) {
/* 192 */       this.cache.seek(this.streamPos);
/* 193 */       this.cache.readFully(paramArrayOfByte, paramInt1, paramInt2);
/* 194 */       this.streamPos += paramInt2;
/* 195 */       return paramInt2;
/*     */     }
/* 197 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isCached()
/*     */   {
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedFile()
/*     */   {
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCachedMemory()
/*     */   {
/* 239 */     return false;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 250 */     super.close();
/* 251 */     this.disposerRecord.dispose();
/* 252 */     this.stream = null;
/* 253 */     this.cache = null;
/* 254 */     this.cacheFile = null;
/* 255 */     StreamCloser.removeFromQueue(this.closeAction);
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
/*     */     private File cacheFile;
/*     */     private RandomAccessFile cache;
/*     */ 
/*     */     public StreamDisposerRecord(File paramFile, RandomAccessFile paramRandomAccessFile)
/*     */     {
/* 272 */       this.cacheFile = paramFile;
/* 273 */       this.cache = paramRandomAccessFile;
/*     */     }
/*     */ 
/*     */     public synchronized void dispose() {
/* 277 */       if (this.cache != null) {
/*     */         try {
/* 279 */           this.cache.close();
/*     */         } catch (IOException localIOException) {
/*     */         } finally {
/* 282 */           this.cache = null;
/*     */         }
/*     */       }
/* 285 */       if (this.cacheFile != null) {
/* 286 */         this.cacheFile.delete();
/* 287 */         this.cacheFile = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.FileCacheImageInputStream
 * JD-Core Version:    0.6.2
 */