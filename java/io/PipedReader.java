/*     */ package java.io;
/*     */ 
/*     */ public class PipedReader extends Reader
/*     */ {
/*  37 */   boolean closedByWriter = false;
/*  38 */   boolean closedByReader = false;
/*  39 */   boolean connected = false;
/*     */   Thread readSide;
/*     */   Thread writeSide;
/*     */   private static final int DEFAULT_PIPE_SIZE = 1024;
/*     */   char[] buffer;
/*  64 */   int in = -1;
/*     */ 
/*  70 */   int out = 0;
/*     */ 
/*     */   public PipedReader(PipedWriter paramPipedWriter)
/*     */     throws IOException
/*     */   {
/*  82 */     this(paramPipedWriter, 1024);
/*     */   }
/*     */ 
/*     */   public PipedReader(PipedWriter paramPipedWriter, int paramInt)
/*     */     throws IOException
/*     */   {
/*  98 */     initPipe(paramInt);
/*  99 */     connect(paramPipedWriter);
/*     */   }
/*     */ 
/*     */   public PipedReader()
/*     */   {
/* 111 */     initPipe(1024);
/*     */   }
/*     */ 
/*     */   public PipedReader(int paramInt)
/*     */   {
/* 127 */     initPipe(paramInt);
/*     */   }
/*     */ 
/*     */   private void initPipe(int paramInt) {
/* 131 */     if (paramInt <= 0) {
/* 132 */       throw new IllegalArgumentException("Pipe size <= 0");
/*     */     }
/* 134 */     this.buffer = new char[paramInt];
/*     */   }
/*     */ 
/*     */   public void connect(PipedWriter paramPipedWriter)
/*     */     throws IOException
/*     */   {
/* 162 */     paramPipedWriter.connect(this);
/*     */   }
/*     */ 
/*     */   synchronized void receive(int paramInt)
/*     */     throws IOException
/*     */   {
/* 170 */     if (!this.connected)
/* 171 */       throw new IOException("Pipe not connected");
/* 172 */     if ((this.closedByWriter) || (this.closedByReader))
/* 173 */       throw new IOException("Pipe closed");
/* 174 */     if ((this.readSide != null) && (!this.readSide.isAlive())) {
/* 175 */       throw new IOException("Read end dead");
/*     */     }
/*     */ 
/* 178 */     this.writeSide = Thread.currentThread();
/* 179 */     while (this.in == this.out) {
/* 180 */       if ((this.readSide != null) && (!this.readSide.isAlive())) {
/* 181 */         throw new IOException("Pipe broken");
/*     */       }
/*     */ 
/* 184 */       notifyAll();
/*     */       try {
/* 186 */         wait(1000L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 188 */         throw new InterruptedIOException();
/*     */       }
/*     */     }
/* 191 */     if (this.in < 0) {
/* 192 */       this.in = 0;
/* 193 */       this.out = 0;
/*     */     }
/* 195 */     this.buffer[(this.in++)] = ((char)paramInt);
/* 196 */     if (this.in >= this.buffer.length)
/* 197 */       this.in = 0;
/*     */   }
/*     */ 
/*     */   synchronized void receive(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 206 */       paramInt2--; if (paramInt2 < 0) break;
/* 207 */       receive(paramArrayOfChar[(paramInt1++)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void receivedLast()
/*     */   {
/* 216 */     this.closedByWriter = true;
/* 217 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized int read()
/*     */     throws IOException
/*     */   {
/* 235 */     if (!this.connected)
/* 236 */       throw new IOException("Pipe not connected");
/* 237 */     if (this.closedByReader)
/* 238 */       throw new IOException("Pipe closed");
/* 239 */     if ((this.writeSide != null) && (!this.writeSide.isAlive()) && (!this.closedByWriter) && (this.in < 0))
/*     */     {
/* 241 */       throw new IOException("Write end dead");
/*     */     }
/*     */ 
/* 244 */     this.readSide = Thread.currentThread();
/* 245 */     int i = 2;
/* 246 */     while (this.in < 0) {
/* 247 */       if (this.closedByWriter)
/*     */       {
/* 249 */         return -1;
/*     */       }
/* 251 */       if ((this.writeSide != null) && (!this.writeSide.isAlive())) { i--; if (i < 0) {
/* 252 */           throw new IOException("Pipe broken");
/*     */         }
/*     */       }
/* 255 */       notifyAll();
/*     */       try {
/* 257 */         wait(1000L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 259 */         throw new InterruptedIOException();
/*     */       }
/*     */     }
/* 262 */     int j = this.buffer[(this.out++)];
/* 263 */     if (this.out >= this.buffer.length) {
/* 264 */       this.out = 0;
/*     */     }
/* 266 */     if (this.in == this.out)
/*     */     {
/* 268 */       this.in = -1;
/*     */     }
/* 270 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 292 */     if (!this.connected)
/* 293 */       throw new IOException("Pipe not connected");
/* 294 */     if (this.closedByReader)
/* 295 */       throw new IOException("Pipe closed");
/* 296 */     if ((this.writeSide != null) && (!this.writeSide.isAlive()) && (!this.closedByWriter) && (this.in < 0))
/*     */     {
/* 298 */       throw new IOException("Write end dead");
/*     */     }
/*     */ 
/* 301 */     if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */     {
/* 303 */       throw new IndexOutOfBoundsException();
/* 304 */     }if (paramInt2 == 0) {
/* 305 */       return 0;
/*     */     }
/*     */ 
/* 309 */     int i = read();
/* 310 */     if (i < 0) {
/* 311 */       return -1;
/*     */     }
/* 313 */     paramArrayOfChar[paramInt1] = ((char)i);
/* 314 */     int j = 1;
/* 315 */     while (this.in >= 0) { paramInt2--; if (paramInt2 <= 0) break;
/* 316 */       paramArrayOfChar[(paramInt1 + j)] = this.buffer[(this.out++)];
/* 317 */       j++;
/* 318 */       if (this.out >= this.buffer.length) {
/* 319 */         this.out = 0;
/*     */       }
/* 321 */       if (this.in == this.out)
/*     */       {
/* 323 */         this.in = -1;
/*     */       }
/*     */     }
/* 326 */     return j;
/*     */   }
/*     */ 
/*     */   public synchronized boolean ready()
/*     */     throws IOException
/*     */   {
/* 338 */     if (!this.connected)
/* 339 */       throw new IOException("Pipe not connected");
/* 340 */     if (this.closedByReader)
/* 341 */       throw new IOException("Pipe closed");
/* 342 */     if ((this.writeSide != null) && (!this.writeSide.isAlive()) && (!this.closedByWriter) && (this.in < 0))
/*     */     {
/* 344 */       throw new IOException("Write end dead");
/*     */     }
/* 346 */     if (this.in < 0) {
/* 347 */       return false;
/*     */     }
/* 349 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 360 */     this.in = -1;
/* 361 */     this.closedByReader = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PipedReader
 * JD-Core Version:    0.6.2
 */