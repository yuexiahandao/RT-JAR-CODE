/*     */ package java.io;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Formatter;
/*     */ import sun.misc.JavaIOAccess;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.nio.cs.StreamDecoder;
/*     */ import sun.nio.cs.StreamEncoder;
/*     */ 
/*     */ public final class Console
/*     */   implements Flushable
/*     */ {
/*     */   private Object readLock;
/*     */   private Object writeLock;
/*     */   private Reader reader;
/*     */   private Writer out;
/*     */   private PrintWriter pw;
/*     */   private Formatter formatter;
/*     */   private Charset cs;
/*     */   private char[] rcb;
/*     */   private static boolean echoOff;
/*     */   private static Console cons;
/*     */ 
/*     */   public PrintWriter writer()
/*     */   {
/* 101 */     return this.pw;
/*     */   }
/*     */ 
/*     */   public Reader reader()
/*     */   {
/* 134 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public Console format(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 168 */     this.formatter.format(paramString, paramArrayOfObject).flush();
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   public Console printf(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 207 */     return format(paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public String readLine(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 242 */     String str = null;
/* 243 */     synchronized (this.writeLock) {
/* 244 */       synchronized (this.readLock) {
/* 245 */         if (paramString.length() != 0)
/* 246 */           this.pw.format(paramString, paramArrayOfObject);
/*     */         try {
/* 248 */           char[] arrayOfChar = readline(false);
/* 249 */           if (arrayOfChar != null)
/* 250 */             str = new String(arrayOfChar);
/*     */         } catch (IOException localIOException) {
/* 252 */           throw new IOError(localIOException);
/*     */         }
/*     */       }
/*     */     }
/* 256 */     return str;
/*     */   }
/*     */ 
/*     */   public String readLine()
/*     */   {
/* 270 */     return readLine("", new Object[0]);
/*     */   }
/*     */ 
/*     */   public char[] readPassword(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 306 */     char[] arrayOfChar = null;
/* 307 */     synchronized (this.writeLock) {
/* 308 */       synchronized (this.readLock) {
/*     */         try {
/* 310 */           echoOff = echo(false);
/*     */         } catch (IOException localIOException1) {
/* 312 */           throw new IOError(localIOException1);
/*     */         }
/* 314 */         IOError localIOError = null;
/*     */         try {
/* 316 */           if (paramString.length() != 0)
/* 317 */             this.pw.format(paramString, paramArrayOfObject);
/* 318 */           arrayOfChar = readline(true);
/*     */         } catch (IOException localIOException3) {
/* 320 */           localIOError = new IOError(localIOException3);
/*     */         } finally {
/*     */           try {
/* 323 */             echoOff = echo(true);
/*     */           } catch (IOException localIOException5) {
/* 325 */             if (localIOError == null)
/* 326 */               localIOError = new IOError(localIOException5);
/*     */             else
/* 328 */               localIOError.addSuppressed(localIOException5);
/*     */           }
/* 330 */           if (localIOError != null)
/* 331 */             throw localIOError;
/*     */         }
/* 333 */         this.pw.println();
/*     */       }
/*     */     }
/* 336 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   public char[] readPassword()
/*     */   {
/* 350 */     return readPassword("", new Object[0]);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 358 */     this.pw.flush();
/*     */   }
/*     */ 
/*     */   private static native String encoding();
/*     */ 
/*     */   private static native boolean echo(boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   private char[] readline(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 374 */     int i = this.reader.read(this.rcb, 0, this.rcb.length);
/* 375 */     if (i < 0)
/* 376 */       return null;
/* 377 */     if (this.rcb[(i - 1)] == '\r') {
/* 378 */       i--;
/* 379 */     } else if (this.rcb[(i - 1)] == '\n') {
/* 380 */       i--;
/* 381 */       if ((i > 0) && (this.rcb[(i - 1)] == '\r'))
/* 382 */         i--;
/*     */     }
/* 384 */     char[] arrayOfChar = new char[i];
/* 385 */     if (i > 0) {
/* 386 */       System.arraycopy(this.rcb, 0, arrayOfChar, 0, i);
/* 387 */       if (paramBoolean) {
/* 388 */         Arrays.fill(this.rcb, 0, i, ' ');
/*     */       }
/*     */     }
/* 391 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   private char[] grow() {
/* 395 */     assert (Thread.holdsLock(this.readLock));
/* 396 */     char[] arrayOfChar = new char[this.rcb.length * 2];
/* 397 */     System.arraycopy(this.rcb, 0, arrayOfChar, 0, this.rcb.length);
/* 398 */     this.rcb = arrayOfChar;
/* 399 */     return this.rcb;
/*     */   }
/*     */ 
/*     */   private static native boolean istty();
/*     */ 
/*     */   private Console()
/*     */   {
/* 556 */     this.readLock = new Object();
/* 557 */     this.writeLock = new Object();
/* 558 */     String str = encoding();
/* 559 */     if (str != null)
/*     */       try {
/* 561 */         this.cs = Charset.forName(str);
/*     */       } catch (Exception localException) {
/*     */       }
/* 564 */     if (this.cs == null)
/* 565 */       this.cs = Charset.defaultCharset();
/* 566 */     this.out = StreamEncoder.forOutputStreamWriter(new FileOutputStream(FileDescriptor.out), this.writeLock, this.cs);
/*     */ 
/* 570 */     this.pw = new PrintWriter(this.out, true)
/*     */     {
/*     */       public void close()
/*     */       {
/*     */       }
/*     */     };
/* 571 */     this.formatter = new Formatter(this.out);
/* 572 */     this.reader = new LineReader(StreamDecoder.forInputStreamReader(new FileInputStream(FileDescriptor.in), this.readLock, this.cs));
/*     */ 
/* 576 */     this.rcb = new char[1024];
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 519 */       SharedSecrets.getJavaLangAccess().registerShutdownHook(0, false, new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/* 525 */             if (Console.echoOff)
/* 526 */               Console.echo(true);
/*     */           }
/*     */           catch (IOException localIOException)
/*     */           {
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException) {
/*     */     }
/* 536 */     SharedSecrets.setJavaIOAccess(new JavaIOAccess() {
/*     */       public Console console() {
/* 538 */         if (Console.access$500()) {
/* 539 */           if (Console.cons == null)
/* 540 */             Console.access$602(new Console(null));
/* 541 */           return Console.cons;
/*     */         }
/* 543 */         return null;
/*     */       }
/*     */ 
/*     */       public Charset charset()
/*     */       {
/* 549 */         return Console.cons.cs;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   class LineReader extends Reader
/*     */   {
/*     */     private Reader in;
/*     */     private char[] cb;
/*     */     private int nChars;
/*     */     private int nextChar;
/*     */     boolean leftoverLF;
/*     */ 
/*     */     LineReader(Reader arg2)
/*     */     {
/*     */       Object localObject;
/* 408 */       this.in = localObject;
/* 409 */       this.cb = new char[1024];
/* 410 */       this.nextChar = (this.nChars = 0);
/* 411 */       this.leftoverLF = false;
/*     */     }
/*     */     public void close() {
/*     */     }
/*     */     public boolean ready() throws IOException {
/* 416 */       return this.in.ready();
/*     */     }
/*     */ 
/*     */     public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 422 */       int i = paramInt1;
/* 423 */       int j = paramInt1 + paramInt2;
/* 424 */       if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (j < 0) || (j > paramArrayOfChar.length))
/*     */       {
/* 426 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 428 */       synchronized (Console.this.readLock) {
/* 429 */         int k = 0;
/* 430 */         int m = 0;
/*     */         do {
/* 432 */           if (this.nextChar >= this.nChars) {
/* 433 */             int n = 0;
/*     */             do
/* 435 */               n = this.in.read(this.cb, 0, this.cb.length);
/* 436 */             while (n == 0);
/* 437 */             if (n > 0) {
/* 438 */               this.nChars = n;
/* 439 */               this.nextChar = 0;
/* 440 */               if ((n < this.cb.length) && (this.cb[(n - 1)] != '\n') && (this.cb[(n - 1)] != '\r'))
/*     */               {
/* 447 */                 k = 1;
/*     */               }
/*     */             } else {
/* 450 */               if (i - paramInt1 == 0)
/* 451 */                 return -1;
/* 452 */               return i - paramInt1;
/*     */             }
/*     */           }
/* 455 */           if ((this.leftoverLF) && (paramArrayOfChar == Console.this.rcb) && (this.cb[this.nextChar] == '\n'))
/*     */           {
/* 460 */             this.nextChar += 1;
/*     */           }
/* 462 */           this.leftoverLF = false;
/* 463 */           while (this.nextChar < this.nChars) {
/* 464 */             m = paramArrayOfChar[(i++)] = this.cb[this.nextChar];
/* 465 */             this.cb[(this.nextChar++)] = '\000';
/* 466 */             if (m == 10)
/* 467 */               return i - paramInt1;
/* 468 */             if (m == 13) {
/* 469 */               if (i == j)
/*     */               {
/* 474 */                 if (paramArrayOfChar == Console.this.rcb) {
/* 475 */                   paramArrayOfChar = Console.this.grow();
/* 476 */                   j = paramArrayOfChar.length;
/*     */                 } else {
/* 478 */                   this.leftoverLF = true;
/* 479 */                   return i - paramInt1;
/*     */                 }
/*     */               }
/* 482 */               if ((this.nextChar == this.nChars) && (this.in.ready()))
/*     */               {
/* 490 */                 this.nChars = this.in.read(this.cb, 0, this.cb.length);
/* 491 */                 this.nextChar = 0;
/*     */               }
/* 493 */               if ((this.nextChar < this.nChars) && (this.cb[this.nextChar] == '\n')) {
/* 494 */                 paramArrayOfChar[(i++)] = '\n';
/* 495 */                 this.nextChar += 1;
/*     */               }
/* 497 */               return i - paramInt1;
/* 498 */             }if (i == j)
/* 499 */               if (paramArrayOfChar == Console.this.rcb) {
/* 500 */                 paramArrayOfChar = Console.this.grow();
/* 501 */                 j = paramArrayOfChar.length;
/*     */               } else {
/* 503 */                 return i - paramInt1;
/*     */               }
/*     */           }
/*     */         }
/* 507 */         while (k == 0);
/* 508 */         return i - paramInt1;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Console
 * JD-Core Version:    0.6.2
 */