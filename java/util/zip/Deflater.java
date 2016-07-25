/*     */ package java.util.zip;
/*     */ 
/*     */ public class Deflater
/*     */ {
/*     */   private final ZStreamRef zsRef;
/*  77 */   private byte[] buf = new byte[0];
/*     */   private int off;
/*     */   private int len;
/*     */   private int level;
/*     */   private int strategy;
/*     */   private boolean setParams;
/*     */   private boolean finish;
/*     */   private boolean finished;
/*     */   private long bytesRead;
/*     */   private long bytesWritten;
/*     */   public static final int DEFLATED = 8;
/*     */   public static final int NO_COMPRESSION = 0;
/*     */   public static final int BEST_SPEED = 1;
/*     */   public static final int BEST_COMPRESSION = 9;
/*     */   public static final int DEFAULT_COMPRESSION = -1;
/*     */   public static final int FILTERED = 1;
/*     */   public static final int HUFFMAN_ONLY = 2;
/*     */   public static final int DEFAULT_STRATEGY = 0;
/*     */   public static final int NO_FLUSH = 0;
/*     */   public static final int SYNC_FLUSH = 2;
/*     */   public static final int FULL_FLUSH = 3;
/*     */ 
/*     */   public Deflater(int paramInt, boolean paramBoolean)
/*     */   {
/* 169 */     this.level = paramInt;
/* 170 */     this.strategy = 0;
/* 171 */     this.zsRef = new ZStreamRef(init(paramInt, 0, paramBoolean));
/*     */   }
/*     */ 
/*     */   public Deflater(int paramInt)
/*     */   {
/* 180 */     this(paramInt, false);
/*     */   }
/*     */ 
/*     */   public Deflater()
/*     */   {
/* 188 */     this(-1, false);
/*     */   }
/*     */ 
/*     */   public void setInput(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 200 */     if (paramArrayOfByte == null) {
/* 201 */       throw new NullPointerException();
/*     */     }
/* 203 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 204 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 206 */     synchronized (this.zsRef) {
/* 207 */       this.buf = paramArrayOfByte;
/* 208 */       this.off = paramInt1;
/* 209 */       this.len = paramInt2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInput(byte[] paramArrayOfByte)
/*     */   {
/* 220 */     setInput(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void setDictionary(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 236 */     if (paramArrayOfByte == null) {
/* 237 */       throw new NullPointerException();
/*     */     }
/* 239 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 240 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 242 */     synchronized (this.zsRef) {
/* 243 */       ensureOpen();
/* 244 */       setDictionary(this.zsRef.address(), paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDictionary(byte[] paramArrayOfByte)
/*     */   {
/* 259 */     setDictionary(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void setStrategy(int paramInt)
/*     */   {
/* 269 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/* 273 */       break;
/*     */     default:
/* 275 */       throw new IllegalArgumentException();
/*     */     }
/* 277 */     synchronized (this.zsRef) {
/* 278 */       if (this.strategy != paramInt) {
/* 279 */         this.strategy = paramInt;
/* 280 */         this.setParams = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLevel(int paramInt)
/*     */   {
/* 291 */     if (((paramInt < 0) || (paramInt > 9)) && (paramInt != -1)) {
/* 292 */       throw new IllegalArgumentException("invalid compression level");
/*     */     }
/* 294 */     synchronized (this.zsRef) {
/* 295 */       if (this.level != paramInt) {
/* 296 */         this.level = paramInt;
/* 297 */         this.setParams = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean needsInput()
/*     */   {
/* 309 */     return this.len <= 0;
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */   {
/* 317 */     synchronized (this.zsRef) {
/* 318 */       this.finish = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean finished()
/*     */   {
/* 329 */     synchronized (this.zsRef) {
/* 330 */       return this.finished;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int deflate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 352 */     return deflate(paramArrayOfByte, paramInt1, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public int deflate(byte[] paramArrayOfByte)
/*     */   {
/* 371 */     return deflate(paramArrayOfByte, 0, paramArrayOfByte.length, 0);
/*     */   }
/*     */ 
/*     */   public int deflate(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 419 */     if (paramArrayOfByte == null) {
/* 420 */       throw new NullPointerException();
/*     */     }
/* 422 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 423 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/* 425 */     synchronized (this.zsRef) {
/* 426 */       ensureOpen();
/* 427 */       if ((paramInt3 == 0) || (paramInt3 == 2) || (paramInt3 == 3))
/*     */       {
/* 429 */         int i = this.len;
/* 430 */         int j = deflateBytes(this.zsRef.address(), paramArrayOfByte, paramInt1, paramInt2, paramInt3);
/* 431 */         this.bytesWritten += j;
/* 432 */         this.bytesRead += i - this.len;
/* 433 */         return j;
/*     */       }
/* 435 */       throw new IllegalArgumentException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getAdler()
/*     */   {
/* 444 */     synchronized (this.zsRef) {
/* 445 */       ensureOpen();
/* 446 */       return getAdler(this.zsRef.address());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTotalIn()
/*     */   {
/* 460 */     return (int)getBytesRead();
/*     */   }
/*     */ 
/*     */   public long getBytesRead()
/*     */   {
/* 470 */     synchronized (this.zsRef) {
/* 471 */       ensureOpen();
/* 472 */       return this.bytesRead;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getTotalOut()
/*     */   {
/* 486 */     return (int)getBytesWritten();
/*     */   }
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/* 496 */     synchronized (this.zsRef) {
/* 497 */       ensureOpen();
/* 498 */       return this.bytesWritten;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 507 */     synchronized (this.zsRef) {
/* 508 */       ensureOpen();
/* 509 */       reset(this.zsRef.address());
/* 510 */       this.finish = false;
/* 511 */       this.finished = false;
/* 512 */       this.off = (this.len = 0);
/* 513 */       this.bytesRead = (this.bytesWritten = 0L);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void end()
/*     */   {
/* 525 */     synchronized (this.zsRef) {
/* 526 */       long l = this.zsRef.address();
/* 527 */       this.zsRef.clear();
/* 528 */       if (l != 0L) {
/* 529 */         end(l);
/* 530 */         this.buf = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/* 539 */     end();
/*     */   }
/*     */ 
/*     */   private void ensureOpen() {
/* 543 */     assert (Thread.holdsLock(this.zsRef));
/* 544 */     if (this.zsRef.address() == 0L)
/* 545 */       throw new NullPointerException("Deflater has been closed");
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native long init(int paramInt1, int paramInt2, boolean paramBoolean);
/*     */ 
/*     */   private static native void setDictionary(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   private native int deflateBytes(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   private static native int getAdler(long paramLong);
/*     */ 
/*     */   private static native void reset(long paramLong);
/*     */ 
/*     */   private static native void end(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/* 157 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.Deflater
 * JD-Core Version:    0.6.2
 */