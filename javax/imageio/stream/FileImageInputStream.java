/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import com.sun.imageio.stream.CloseableDisposerRecord;
/*     */ import com.sun.imageio.stream.StreamFinalizer;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import sun.java2d.Disposer;
/*     */ 
/*     */ public class FileImageInputStream extends ImageInputStreamImpl
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private final Object disposerReferent;
/*     */   private final CloseableDisposerRecord disposerRecord;
/*     */ 
/*     */   public FileImageInputStream(File paramFile)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/*  73 */     this(paramFile == null ? null : new RandomAccessFile(paramFile, "r"));
/*     */   }
/*     */ 
/*     */   public FileImageInputStream(RandomAccessFile paramRandomAccessFile)
/*     */   {
/*  90 */     if (paramRandomAccessFile == null) {
/*  91 */       throw new IllegalArgumentException("raf == null!");
/*     */     }
/*  93 */     this.raf = paramRandomAccessFile;
/*     */ 
/*  95 */     this.disposerRecord = new CloseableDisposerRecord(paramRandomAccessFile);
/*  96 */     if (getClass() == FileImageInputStream.class) {
/*  97 */       this.disposerReferent = new Object();
/*  98 */       Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*     */     } else {
/* 100 */       this.disposerReferent = new StreamFinalizer(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/* 105 */     checkClosed();
/* 106 */     this.bitOffset = 0;
/* 107 */     int i = this.raf.read();
/* 108 */     if (i != -1) {
/* 109 */       this.streamPos += 1L;
/*     */     }
/* 111 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 115 */     checkClosed();
/* 116 */     this.bitOffset = 0;
/* 117 */     int i = this.raf.read(paramArrayOfByte, paramInt1, paramInt2);
/* 118 */     if (i != -1) {
/* 119 */       this.streamPos += i;
/*     */     }
/* 121 */     return i;
/*     */   }
/*     */ 
/*     */   public long length()
/*     */   {
/*     */     try
/*     */     {
/* 133 */       checkClosed();
/* 134 */       return this.raf.length(); } catch (IOException localIOException) {
/*     */     }
/* 136 */     return -1L;
/*     */   }
/*     */ 
/*     */   public void seek(long paramLong) throws IOException
/*     */   {
/* 141 */     checkClosed();
/* 142 */     if (paramLong < this.flushedPos) {
/* 143 */       throw new IndexOutOfBoundsException("pos < flushedPos!");
/*     */     }
/* 145 */     this.bitOffset = 0;
/* 146 */     this.raf.seek(paramLong);
/* 147 */     this.streamPos = this.raf.getFilePointer();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 151 */     super.close();
/* 152 */     this.disposerRecord.dispose();
/* 153 */     this.raf = null;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.FileImageInputStream
 * JD-Core Version:    0.6.2
 */