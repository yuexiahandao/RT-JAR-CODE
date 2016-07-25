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
/*     */ public class FileImageOutputStream extends ImageOutputStreamImpl
/*     */ {
/*     */   private RandomAccessFile raf;
/*     */   private final Object disposerReferent;
/*     */   private final CloseableDisposerRecord disposerRecord;
/*     */ 
/*     */   public FileImageOutputStream(File paramFile)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/*  69 */     this(paramFile == null ? null : new RandomAccessFile(paramFile, "rw"));
/*     */   }
/*     */ 
/*     */   public FileImageOutputStream(RandomAccessFile paramRandomAccessFile)
/*     */   {
/*  82 */     if (paramRandomAccessFile == null) {
/*  83 */       throw new IllegalArgumentException("raf == null!");
/*     */     }
/*  85 */     this.raf = paramRandomAccessFile;
/*     */ 
/*  87 */     this.disposerRecord = new CloseableDisposerRecord(paramRandomAccessFile);
/*  88 */     if (getClass() == FileImageOutputStream.class) {
/*  89 */       this.disposerReferent = new Object();
/*  90 */       Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*     */     } else {
/*  92 */       this.disposerReferent = new StreamFinalizer(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read() throws IOException {
/*  97 */     checkClosed();
/*  98 */     this.bitOffset = 0;
/*  99 */     int i = this.raf.read();
/* 100 */     if (i != -1) {
/* 101 */       this.streamPos += 1L;
/*     */     }
/* 103 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 107 */     checkClosed();
/* 108 */     this.bitOffset = 0;
/* 109 */     int i = this.raf.read(paramArrayOfByte, paramInt1, paramInt2);
/* 110 */     if (i != -1) {
/* 111 */       this.streamPos += i;
/*     */     }
/* 113 */     return i;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt) throws IOException {
/* 117 */     flushBits();
/* 118 */     this.raf.write(paramInt);
/* 119 */     this.streamPos += 1L;
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 123 */     flushBits();
/* 124 */     this.raf.write(paramArrayOfByte, paramInt1, paramInt2);
/* 125 */     this.streamPos += paramInt2;
/*     */   }
/*     */ 
/*     */   public long length() {
/*     */     try {
/* 130 */       checkClosed();
/* 131 */       return this.raf.length(); } catch (IOException localIOException) {
/*     */     }
/* 133 */     return -1L;
/*     */   }
/*     */ 
/*     */   public void seek(long paramLong)
/*     */     throws IOException
/*     */   {
/* 149 */     checkClosed();
/* 150 */     if (paramLong < this.flushedPos) {
/* 151 */       throw new IndexOutOfBoundsException("pos < flushedPos!");
/*     */     }
/* 153 */     this.bitOffset = 0;
/* 154 */     this.raf.seek(paramLong);
/* 155 */     this.streamPos = this.raf.getFilePointer();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 159 */     super.close();
/* 160 */     this.disposerRecord.dispose();
/* 161 */     this.raf = null;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.FileImageOutputStream
 * JD-Core Version:    0.6.2
 */