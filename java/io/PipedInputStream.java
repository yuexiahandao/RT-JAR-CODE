/*     */ package java.io;
/*     */ 
/*     */ public class PipedInputStream extends InputStream
/*     */ {
/*  51 */   boolean closedByWriter = false;
/*  52 */   volatile boolean closedByReader = false;
/*  53 */   boolean connected = false;
/*     */   Thread readSide;
/*     */   Thread writeSide;
/*     */   private static final int DEFAULT_PIPE_SIZE = 1024;
/*     */   protected static final int PIPE_SIZE = 1024;
/*     */   protected byte[] buffer;
/*  86 */   protected int in = -1;
/*     */ 
/*  93 */   protected int out = 0;
/*     */ 
/*     */   public PipedInputStream(PipedOutputStream paramPipedOutputStream)
/*     */     throws IOException
/*     */   {
/* 106 */     this(paramPipedOutputStream, 1024);
/*     */   }
/*     */ 
/*     */   public PipedInputStream(PipedOutputStream paramPipedOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 125 */     initPipe(paramInt);
/* 126 */     connect(paramPipedOutputStream);
/*     */   }
/*     */ 
/*     */   public PipedInputStream()
/*     */   {
/* 138 */     initPipe(1024);
/*     */   }
/*     */ 
/*     */   public PipedInputStream(int paramInt)
/*     */   {
/* 154 */     initPipe(paramInt);
/*     */   }
/*     */ 
/*     */   private void initPipe(int paramInt) {
/* 158 */     if (paramInt <= 0) {
/* 159 */       throw new IllegalArgumentException("Pipe Size <= 0");
/*     */     }
/* 161 */     this.buffer = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   public void connect(PipedOutputStream paramPipedOutputStream)
/*     */     throws IOException
/*     */   {
/* 189 */     paramPipedOutputStream.connect(this);
/*     */   }
/*     */ 
/*     */   protected synchronized void receive(int paramInt)
/*     */     throws IOException
/*     */   {
/* 202 */     checkStateForReceive();
/* 203 */     this.writeSide = Thread.currentThread();
/* 204 */     if (this.in == this.out)
/* 205 */       awaitSpace();
/* 206 */     if (this.in < 0) {
/* 207 */       this.in = 0;
/* 208 */       this.out = 0;
/*     */     }
/* 210 */     this.buffer[(this.in++)] = ((byte)(paramInt & 0xFF));
/* 211 */     if (this.in >= this.buffer.length)
/* 212 */       this.in = 0;
/*     */   }
/*     */ 
/*     */   synchronized void receive(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 227 */     checkStateForReceive();
/* 228 */     this.writeSide = Thread.currentThread();
/* 229 */     int i = paramInt2;
/* 230 */     while (i > 0) {
/* 231 */       if (this.in == this.out)
/* 232 */         awaitSpace();
/* 233 */       int j = 0;
/* 234 */       if (this.out < this.in)
/* 235 */         j = this.buffer.length - this.in;
/* 236 */       else if (this.in < this.out) {
/* 237 */         if (this.in == -1) {
/* 238 */           this.in = (this.out = 0);
/* 239 */           j = this.buffer.length - this.in;
/*     */         } else {
/* 241 */           j = this.out - this.in;
/*     */         }
/*     */       }
/* 244 */       if (j > i)
/* 245 */         j = i;
/* 246 */       assert (j > 0);
/* 247 */       System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, this.in, j);
/* 248 */       i -= j;
/* 249 */       paramInt1 += j;
/* 250 */       this.in += j;
/* 251 */       if (this.in >= this.buffer.length)
/* 252 */         this.in = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkStateForReceive() throws IOException
/*     */   {
/* 258 */     if (!this.connected)
/* 259 */       throw new IOException("Pipe not connected");
/* 260 */     if ((this.closedByWriter) || (this.closedByReader))
/* 261 */       throw new IOException("Pipe closed");
/* 262 */     if ((this.readSide != null) && (!this.readSide.isAlive()))
/* 263 */       throw new IOException("Read end dead");
/*     */   }
/*     */ 
/*     */   private void awaitSpace() throws IOException
/*     */   {
/* 268 */     while (this.in == this.out) {
/* 269 */       checkStateForReceive();
/*     */ 
/* 272 */       notifyAll();
/*     */       try {
/* 274 */         wait(1000L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 276 */         throw new InterruptedIOException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void receivedLast()
/*     */   {
/* 286 */     this.closedByWriter = true;
/* 287 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */     throws IOException
/*     */   {
/* 305 */     if (!this.connected)
/* 306 */       throw new IOException("Pipe not connected");
/* 307 */     if (this.closedByReader)
/* 308 */       throw new IOException("Pipe closed");
/* 309 */     if ((this.writeSide != null) && (!this.writeSide.isAlive()) && (!this.closedByWriter) && (this.in < 0))
/*     */     {
/* 311 */       throw new IOException("Write end dead");
/*     */     }
/*     */ 
/* 314 */     this.readSide = Thread.currentThread();
/* 315 */     int i = 2;
/* 316 */     while (this.in < 0) {
/* 317 */       if (this.closedByWriter)
/*     */       {
/* 319 */         return -1;
/*     */       }
/* 321 */       if ((this.writeSide != null) && (!this.writeSide.isAlive())) { i--; if (i < 0) {
/* 322 */           throw new IOException("Pipe broken");
/*     */         }
/*     */       }
/* 325 */       notifyAll();
/*     */       try {
/* 327 */         wait(1000L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 329 */         throw new InterruptedIOException();
/*     */       }
/*     */     }
/* 332 */     int j = this.buffer[(this.out++)] & 0xFF;
/* 333 */     if (this.out >= this.buffer.length) {
/* 334 */       this.out = 0;
/*     */     }
/* 336 */     if (this.in == this.out)
/*     */     {
/* 338 */       this.in = -1;
/*     */     }
/*     */ 
/* 341 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 369 */     if (paramArrayOfByte == null)
/* 370 */       throw new NullPointerException();
/* 371 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 372 */       throw new IndexOutOfBoundsException();
/* 373 */     if (paramInt2 == 0) {
/* 374 */       return 0;
/*     */     }
/*     */ 
/* 378 */     int i = read();
/* 379 */     if (i < 0) {
/* 380 */       return -1;
/*     */     }
/* 382 */     paramArrayOfByte[paramInt1] = ((byte)i);
/* 383 */     int j = 1;
/* 384 */     while ((this.in >= 0) && (paramInt2 > 1))
/*     */     {
/*     */       int k;
/* 388 */       if (this.in > this.out)
/* 389 */         k = Math.min(this.buffer.length - this.out, this.in - this.out);
/*     */       else {
/* 391 */         k = this.buffer.length - this.out;
/*     */       }
/*     */ 
/* 395 */       if (k > paramInt2 - 1) {
/* 396 */         k = paramInt2 - 1;
/*     */       }
/* 398 */       System.arraycopy(this.buffer, this.out, paramArrayOfByte, paramInt1 + j, k);
/* 399 */       this.out += k;
/* 400 */       j += k;
/* 401 */       paramInt2 -= k;
/*     */ 
/* 403 */       if (this.out >= this.buffer.length) {
/* 404 */         this.out = 0;
/*     */       }
/* 406 */       if (this.in == this.out)
/*     */       {
/* 408 */         this.in = -1;
/*     */       }
/*     */     }
/* 411 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized int available()
/*     */     throws IOException
/*     */   {
/* 428 */     if (this.in < 0)
/* 429 */       return 0;
/* 430 */     if (this.in == this.out)
/* 431 */       return this.buffer.length;
/* 432 */     if (this.in > this.out) {
/* 433 */       return this.in - this.out;
/*     */     }
/* 435 */     return this.in + this.buffer.length - this.out;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 445 */     this.closedByReader = true;
/* 446 */     synchronized (this) {
/* 447 */       this.in = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PipedInputStream
 * JD-Core Version:    0.6.2
 */