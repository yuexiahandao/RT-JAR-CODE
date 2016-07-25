/*     */ package java.io;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ public class BufferedInputStream extends FilterInputStream
/*     */ {
/*  53 */   private static int defaultBufferSize = 8192;
/*     */   protected volatile byte[] buf;
/*  69 */   private static final AtomicReferenceFieldUpdater<BufferedInputStream, byte[]> bufUpdater = AtomicReferenceFieldUpdater.newUpdater(BufferedInputStream.class, [B.class, "buf");
/*     */   protected int count;
/*     */   protected int pos;
/* 128 */   protected int markpos = -1;
/*     */   protected int marklimit;
/*     */ 
/*     */   private InputStream getInIfOpen()
/*     */     throws IOException
/*     */   {
/* 149 */     InputStream localInputStream = this.in;
/* 150 */     if (localInputStream == null)
/* 151 */       throw new IOException("Stream closed");
/* 152 */     return localInputStream;
/*     */   }
/*     */ 
/*     */   private byte[] getBufIfOpen()
/*     */     throws IOException
/*     */   {
/* 160 */     byte[] arrayOfByte = this.buf;
/* 161 */     if (arrayOfByte == null)
/* 162 */       throw new IOException("Stream closed");
/* 163 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public BufferedInputStream(InputStream paramInputStream)
/*     */   {
/* 175 */     this(paramInputStream, defaultBufferSize);
/*     */   }
/*     */ 
/*     */   public BufferedInputStream(InputStream paramInputStream, int paramInt)
/*     */   {
/* 191 */     super(paramInputStream);
/* 192 */     if (paramInt <= 0) {
/* 193 */       throw new IllegalArgumentException("Buffer size <= 0");
/*     */     }
/* 195 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   private void fill()
/*     */     throws IOException
/*     */   {
/* 206 */     Object localObject = getBufIfOpen();
/* 207 */     if (this.markpos < 0)
/* 208 */       this.pos = 0;
/* 209 */     else if (this.pos >= localObject.length)
/* 210 */       if (this.markpos > 0) {
/* 211 */         i = this.pos - this.markpos;
/* 212 */         System.arraycopy(localObject, this.markpos, localObject, 0, i);
/* 213 */         this.pos = i;
/* 214 */         this.markpos = 0;
/* 215 */       } else if (localObject.length >= this.marklimit) {
/* 216 */         this.markpos = -1;
/* 217 */         this.pos = 0;
/*     */       } else {
/* 219 */         i = this.pos * 2;
/* 220 */         if (i > this.marklimit)
/* 221 */           i = this.marklimit;
/* 222 */         byte[] arrayOfByte = new byte[i];
/* 223 */         System.arraycopy(localObject, 0, arrayOfByte, 0, this.pos);
/* 224 */         if (!bufUpdater.compareAndSet(this, localObject, arrayOfByte))
/*     */         {
/* 230 */           throw new IOException("Stream closed");
/*     */         }
/* 232 */         localObject = arrayOfByte;
/*     */       }
/* 234 */     this.count = this.pos;
/* 235 */     int i = getInIfOpen().read((byte[])localObject, this.pos, localObject.length - this.pos);
/* 236 */     if (i > 0)
/* 237 */       this.count = (i + this.pos);
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */     throws IOException
/*     */   {
/* 253 */     if (this.pos >= this.count) {
/* 254 */       fill();
/* 255 */       if (this.pos >= this.count)
/* 256 */         return -1;
/*     */     }
/* 258 */     return getBufIfOpen()[(this.pos++)] & 0xFF;
/*     */   }
/*     */ 
/*     */   private int read1(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 266 */     int i = this.count - this.pos;
/* 267 */     if (i <= 0)
/*     */     {
/* 272 */       if ((paramInt2 >= getBufIfOpen().length) && (this.markpos < 0)) {
/* 273 */         return getInIfOpen().read(paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/* 275 */       fill();
/* 276 */       i = this.count - this.pos;
/* 277 */       if (i <= 0) return -1;
/*     */     }
/* 279 */     int j = i < paramInt2 ? i : paramInt2;
/* 280 */     System.arraycopy(getBufIfOpen(), this.pos, paramArrayOfByte, paramInt1, j);
/* 281 */     this.pos += j;
/* 282 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 325 */     getBufIfOpen();
/* 326 */     if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramArrayOfByte.length - (paramInt1 + paramInt2)) < 0)
/* 327 */       throw new IndexOutOfBoundsException();
/* 328 */     if (paramInt2 == 0) {
/* 329 */       return 0;
/*     */     }
/*     */ 
/* 332 */     int i = 0;
/*     */     while (true) {
/* 334 */       int j = read1(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
/* 335 */       if (j <= 0)
/* 336 */         return i == 0 ? j : i;
/* 337 */       i += j;
/* 338 */       if (i >= paramInt2) {
/* 339 */         return i;
/*     */       }
/* 341 */       InputStream localInputStream = this.in;
/* 342 */       if ((localInputStream != null) && (localInputStream.available() <= 0))
/* 343 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 357 */     getBufIfOpen();
/* 358 */     if (paramLong <= 0L) {
/* 359 */       return 0L;
/*     */     }
/* 361 */     long l1 = this.count - this.pos;
/*     */ 
/* 363 */     if (l1 <= 0L)
/*     */     {
/* 365 */       if (this.markpos < 0) {
/* 366 */         return getInIfOpen().skip(paramLong);
/*     */       }
/*     */ 
/* 369 */       fill();
/* 370 */       l1 = this.count - this.pos;
/* 371 */       if (l1 <= 0L) {
/* 372 */         return 0L;
/*     */       }
/*     */     }
/* 375 */     long l2 = l1 < paramLong ? l1 : paramLong;
/* 376 */     this.pos = ((int)(this.pos + l2));
/* 377 */     return l2;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */     throws IOException
/*     */   {
/* 398 */     int i = this.count - this.pos;
/* 399 */     int j = getInIfOpen().available();
/* 400 */     return i > 2147483647 - j ? 2147483647 : i + j;
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int paramInt)
/*     */   {
/* 414 */     this.marklimit = paramInt;
/* 415 */     this.markpos = this.pos;
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 435 */     getBufIfOpen();
/* 436 */     if (this.markpos < 0)
/* 437 */       throw new IOException("Resetting to invalid mark");
/* 438 */     this.pos = this.markpos;
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 453 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     byte[] arrayOfByte;
/* 467 */     while ((arrayOfByte = this.buf) != null)
/* 468 */       if (bufUpdater.compareAndSet(this, arrayOfByte, null)) {
/* 469 */         InputStream localInputStream = this.in;
/* 470 */         this.in = null;
/* 471 */         if (localInputStream != null)
/* 472 */           localInputStream.close();
/* 473 */         return;
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.BufferedInputStream
 * JD-Core Version:    0.6.2
 */