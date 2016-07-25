/*     */ package java.io;
/*     */ 
/*     */ public class BufferedReader extends Reader
/*     */ {
/*     */   private Reader in;
/*     */   private char[] cb;
/*     */   private int nChars;
/*     */   private int nextChar;
/*     */   private static final int INVALIDATED = -2;
/*     */   private static final int UNMARKED = -1;
/*  72 */   private int markedChar = -1;
/*  73 */   private int readAheadLimit = 0;
/*     */ 
/*  76 */   private boolean skipLF = false;
/*     */ 
/*  79 */   private boolean markedSkipLF = false;
/*     */ 
/*  81 */   private static int defaultCharBufferSize = 8192;
/*  82 */   private static int defaultExpectedLineLength = 80;
/*     */ 
/*     */   public BufferedReader(Reader paramReader, int paramInt)
/*     */   {
/*  94 */     super(paramReader);
/*  95 */     if (paramInt <= 0)
/*  96 */       throw new IllegalArgumentException("Buffer size <= 0");
/*  97 */     this.in = paramReader;
/*  98 */     this.cb = new char[paramInt];
/*  99 */     this.nextChar = (this.nChars = 0);
/*     */   }
/*     */ 
/*     */   public BufferedReader(Reader paramReader)
/*     */   {
/* 109 */     this(paramReader, defaultCharBufferSize);
/*     */   }
/*     */ 
/*     */   private void ensureOpen() throws IOException
/*     */   {
/* 114 */     if (this.in == null)
/* 115 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   private void fill()
/*     */     throws IOException
/*     */   {
/*     */     int i;
/*     */     int j;
/* 123 */     if (this.markedChar <= -1)
/*     */     {
/* 125 */       i = 0;
/*     */     }
/*     */     else {
/* 128 */       j = this.nextChar - this.markedChar;
/* 129 */       if (j >= this.readAheadLimit)
/*     */       {
/* 131 */         this.markedChar = -2;
/* 132 */         this.readAheadLimit = 0;
/* 133 */         i = 0;
/*     */       } else {
/* 135 */         if (this.readAheadLimit <= this.cb.length)
/*     */         {
/* 137 */           System.arraycopy(this.cb, this.markedChar, this.cb, 0, j);
/* 138 */           this.markedChar = 0;
/* 139 */           i = j;
/*     */         }
/*     */         else {
/* 142 */           char[] arrayOfChar = new char[this.readAheadLimit];
/* 143 */           System.arraycopy(this.cb, this.markedChar, arrayOfChar, 0, j);
/* 144 */           this.cb = arrayOfChar;
/* 145 */           this.markedChar = 0;
/* 146 */           i = j;
/*     */         }
/* 148 */         this.nextChar = (this.nChars = j);
/*     */       }
/*     */     }
/*     */ 
/*     */     do
/*     */     {
/* 154 */       j = this.in.read(this.cb, i, this.cb.length - i);
/* 155 */     }while (j == 0);
/* 156 */     if (j > 0) {
/* 157 */       this.nChars = (i + j);
/* 158 */       this.nextChar = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 171 */     synchronized (this.lock) {
/* 172 */       ensureOpen();
/*     */       while (true) {
/* 174 */         if (this.nextChar >= this.nChars) {
/* 175 */           fill();
/* 176 */           if (this.nextChar >= this.nChars)
/* 177 */             return -1;
/*     */         }
/* 179 */         if (!this.skipLF) break;
/* 180 */         this.skipLF = false;
/* 181 */         if (this.cb[this.nextChar] != '\n') break;
/* 182 */         this.nextChar += 1;
/*     */       }
/*     */ 
/* 186 */       return this.cb[(this.nextChar++)];
/*     */     }
/*     */   }
/*     */ 
/*     */   private int read1(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 196 */     if (this.nextChar >= this.nChars)
/*     */     {
/* 202 */       if ((paramInt2 >= this.cb.length) && (this.markedChar <= -1) && (!this.skipLF)) {
/* 203 */         return this.in.read(paramArrayOfChar, paramInt1, paramInt2);
/*     */       }
/* 205 */       fill();
/*     */     }
/* 207 */     if (this.nextChar >= this.nChars) return -1;
/* 208 */     if (this.skipLF) {
/* 209 */       this.skipLF = false;
/* 210 */       if (this.cb[this.nextChar] == '\n') {
/* 211 */         this.nextChar += 1;
/* 212 */         if (this.nextChar >= this.nChars)
/* 213 */           fill();
/* 214 */         if (this.nextChar >= this.nChars)
/* 215 */           return -1;
/*     */       }
/*     */     }
/* 218 */     int i = Math.min(paramInt2, this.nChars - this.nextChar);
/* 219 */     System.arraycopy(this.cb, this.nextChar, paramArrayOfChar, paramInt1, i);
/* 220 */     this.nextChar += i;
/* 221 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 270 */     synchronized (this.lock) {
/* 271 */       ensureOpen();
/* 272 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0))
/*     */       {
/* 274 */         throw new IndexOutOfBoundsException();
/* 275 */       }if (paramInt2 == 0) {
/* 276 */         return 0;
/*     */       }
/*     */ 
/* 279 */       int i = read1(paramArrayOfChar, paramInt1, paramInt2);
/* 280 */       if (i <= 0) return i;
/* 281 */       while ((i < paramInt2) && (this.in.ready())) {
/* 282 */         int j = read1(paramArrayOfChar, paramInt1 + i, paramInt2 - i);
/* 283 */         if (j <= 0) break;
/* 284 */         i += j;
/*     */       }
/* 286 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   String readLine(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 306 */     StringBuffer localStringBuffer = null;
/*     */ 
/* 309 */     synchronized (this.lock) {
/* 310 */       ensureOpen();
/* 311 */       int j = (paramBoolean) || (this.skipLF) ? 1 : 0;
/*     */ 
/* 316 */       if (this.nextChar >= this.nChars)
/* 317 */         fill();
/* 318 */       if (this.nextChar >= this.nChars) {
/* 319 */         if ((localStringBuffer != null) && (localStringBuffer.length() > 0)) {
/* 320 */           return localStringBuffer.toString();
/*     */         }
/* 322 */         return null;
/*     */       }
/* 324 */       int k = 0;
/* 325 */       int m = 0;
/*     */ 
/* 329 */       if ((j != 0) && (this.cb[this.nextChar] == '\n'))
/* 330 */         this.nextChar += 1;
/* 331 */       this.skipLF = false;
/* 332 */       j = 0;
/*     */ 
/* 335 */       for (int n = this.nextChar; n < this.nChars; n++) {
/* 336 */         m = this.cb[n];
/* 337 */         if ((m == 10) || (m == 13)) {
/* 338 */           k = 1;
/* 339 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 343 */       int i = this.nextChar;
/* 344 */       this.nextChar = n;
/*     */ 
/* 346 */       if (k != 0)
/*     */       {
/*     */         String str;
/* 348 */         if (localStringBuffer == null) {
/* 349 */           str = new String(this.cb, i, n - i);
/*     */         } else {
/* 351 */           localStringBuffer.append(this.cb, i, n - i);
/* 352 */           str = localStringBuffer.toString();
/*     */         }
/* 354 */         this.nextChar += 1;
/* 355 */         if (m == 13) {
/* 356 */           this.skipLF = true;
/*     */         }
/* 358 */         return str;
/*     */       }
/*     */ 
/* 361 */       if (localStringBuffer == null)
/* 362 */         localStringBuffer = new StringBuffer(defaultExpectedLineLength);
/* 363 */       localStringBuffer.append(this.cb, i, n - i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String readLine()
/*     */     throws IOException
/*     */   {
/* 382 */     return readLine(false);
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 396 */     if (paramLong < 0L) {
/* 397 */       throw new IllegalArgumentException("skip value is negative");
/*     */     }
/* 399 */     synchronized (this.lock) {
/* 400 */       ensureOpen();
/* 401 */       long l1 = paramLong;
/* 402 */       while (l1 > 0L) {
/* 403 */         if (this.nextChar >= this.nChars)
/* 404 */           fill();
/* 405 */         if (this.nextChar >= this.nChars)
/*     */           break;
/* 407 */         if (this.skipLF) {
/* 408 */           this.skipLF = false;
/* 409 */           if (this.cb[this.nextChar] == '\n') {
/* 410 */             this.nextChar += 1;
/*     */           }
/*     */         }
/* 413 */         long l2 = this.nChars - this.nextChar;
/* 414 */         if (l1 <= l2) {
/* 415 */           this.nextChar = ((int)(this.nextChar + l1));
/* 416 */           l1 = 0L;
/* 417 */           break;
/*     */         }
/*     */ 
/* 420 */         l1 -= l2;
/* 421 */         this.nextChar = this.nChars;
/*     */       }
/*     */ 
/* 424 */       return paramLong - l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 436 */     synchronized (this.lock) {
/* 437 */       ensureOpen();
/*     */ 
/* 443 */       if (this.skipLF)
/*     */       {
/* 447 */         if ((this.nextChar >= this.nChars) && (this.in.ready())) {
/* 448 */           fill();
/*     */         }
/* 450 */         if (this.nextChar < this.nChars) {
/* 451 */           if (this.cb[this.nextChar] == '\n')
/* 452 */             this.nextChar += 1;
/* 453 */           this.skipLF = false;
/*     */         }
/*     */       }
/* 456 */       return (this.nextChar < this.nChars) || (this.in.ready());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 464 */     return true;
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 484 */     if (paramInt < 0) {
/* 485 */       throw new IllegalArgumentException("Read-ahead limit < 0");
/*     */     }
/* 487 */     synchronized (this.lock) {
/* 488 */       ensureOpen();
/* 489 */       this.readAheadLimit = paramInt;
/* 490 */       this.markedChar = this.nextChar;
/* 491 */       this.markedSkipLF = this.skipLF;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 502 */     synchronized (this.lock) {
/* 503 */       ensureOpen();
/* 504 */       if (this.markedChar < 0) {
/* 505 */         throw new IOException(this.markedChar == -2 ? "Mark invalid" : "Stream not marked");
/*     */       }
/*     */ 
/* 508 */       this.nextChar = this.markedChar;
/* 509 */       this.skipLF = this.markedSkipLF;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 514 */     synchronized (this.lock) {
/* 515 */       if (this.in == null)
/* 516 */         return;
/* 517 */       this.in.close();
/* 518 */       this.in = null;
/* 519 */       this.cb = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.BufferedReader
 * JD-Core Version:    0.6.2
 */