/*     */ package java.io;
/*     */ 
/*     */ public class BufferedOutputStream extends FilterOutputStream
/*     */ {
/*     */   protected byte[] buf;
/*     */   protected int count;
/*     */ 
/*     */   public BufferedOutputStream(OutputStream paramOutputStream)
/*     */   {
/*  59 */     this(paramOutputStream, 8192);
/*     */   }
/*     */ 
/*     */   public BufferedOutputStream(OutputStream paramOutputStream, int paramInt)
/*     */   {
/*  72 */     super(paramOutputStream);
/*  73 */     if (paramInt <= 0) {
/*  74 */       throw new IllegalArgumentException("Buffer size <= 0");
/*     */     }
/*  76 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   private void flushBuffer() throws IOException
/*     */   {
/*  81 */     if (this.count > 0) {
/*  82 */       this.out.write(this.buf, 0, this.count);
/*  83 */       this.count = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  94 */     if (this.count >= this.buf.length) {
/*  95 */       flushBuffer();
/*     */     }
/*  97 */     this.buf[(this.count++)] = ((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 117 */     if (paramInt2 >= this.buf.length)
/*     */     {
/* 121 */       flushBuffer();
/* 122 */       this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 123 */       return;
/*     */     }
/* 125 */     if (paramInt2 > this.buf.length - this.count) {
/* 126 */       flushBuffer();
/*     */     }
/* 128 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.count, paramInt2);
/* 129 */     this.count += paramInt2;
/*     */   }
/*     */ 
/*     */   public synchronized void flush()
/*     */     throws IOException
/*     */   {
/* 140 */     flushBuffer();
/* 141 */     this.out.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.BufferedOutputStream
 * JD-Core Version:    0.6.2
 */