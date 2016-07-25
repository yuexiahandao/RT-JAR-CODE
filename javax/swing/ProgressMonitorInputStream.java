/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ 
/*     */ public class ProgressMonitorInputStream extends FilterInputStream
/*     */ {
/*     */   private ProgressMonitor monitor;
/*  67 */   private int nread = 0;
/*  68 */   private int size = 0;
/*     */ 
/*     */   public ProgressMonitorInputStream(Component paramComponent, Object paramObject, InputStream paramInputStream)
/*     */   {
/*  83 */     super(paramInputStream);
/*     */     try {
/*  85 */       this.size = paramInputStream.available();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  88 */       this.size = 0;
/*     */     }
/*  90 */     this.monitor = new ProgressMonitor(paramComponent, paramObject, null, 0, this.size);
/*     */   }
/*     */ 
/*     */   public ProgressMonitor getProgressMonitor()
/*     */   {
/* 101 */     return this.monitor;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 110 */     int i = this.in.read();
/* 111 */     if (i >= 0) this.monitor.setProgress(++this.nread);
/* 112 */     if (this.monitor.isCanceled()) {
/* 113 */       InterruptedIOException localInterruptedIOException = new InterruptedIOException("progress");
/*     */ 
/* 115 */       localInterruptedIOException.bytesTransferred = this.nread;
/* 116 */       throw localInterruptedIOException;
/*     */     }
/* 118 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 127 */     int i = this.in.read(paramArrayOfByte);
/* 128 */     if (i > 0) this.monitor.setProgress(this.nread += i);
/* 129 */     if (this.monitor.isCanceled()) {
/* 130 */       InterruptedIOException localInterruptedIOException = new InterruptedIOException("progress");
/*     */ 
/* 132 */       localInterruptedIOException.bytesTransferred = this.nread;
/* 133 */       throw localInterruptedIOException;
/*     */     }
/* 135 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 146 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 147 */     if (i > 0) this.monitor.setProgress(this.nread += i);
/* 148 */     if (this.monitor.isCanceled()) {
/* 149 */       InterruptedIOException localInterruptedIOException = new InterruptedIOException("progress");
/*     */ 
/* 151 */       localInterruptedIOException.bytesTransferred = this.nread;
/* 152 */       throw localInterruptedIOException;
/*     */     }
/* 154 */     return i;
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 163 */     long l = this.in.skip(paramLong);
/* 164 */     if (l > 0L) this.monitor.setProgress(this.nread = (int)(this.nread + l));
/* 165 */     return l;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 174 */     this.in.close();
/* 175 */     this.monitor.close();
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 184 */     this.in.reset();
/* 185 */     this.nread = (this.size - this.in.available());
/* 186 */     this.monitor.setProgress(this.nread);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ProgressMonitorInputStream
 * JD-Core Version:    0.6.2
 */