/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.misc.JavaUtilZipFileAccess;
/*     */ import sun.misc.PerfCounter;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.misc.VM;
/*     */ 
/*     */ public class ZipFile
/*     */   implements ZipConstants, Closeable
/*     */ {
/*     */   private long jzfile;
/*     */   private final String name;
/*     */   private final int total;
/*     */   private final boolean locsig;
/*  61 */   private volatile boolean closeRequested = false;
/*     */   private static final int STORED = 0;
/*     */   private static final int DEFLATED = 8;
/*     */   public static final int OPEN_READ = 1;
/*     */   public static final int OPEN_DELETE = 4;
/*     */   private static final boolean usemmap;
/*     */   private ZipCoder zc;
/* 324 */   private final Map<InputStream, Inflater> streams = new WeakHashMap();
/*     */ 
/* 466 */   private Deque<Inflater> inflaterCache = new ArrayDeque();
/*     */   private static final int JZENTRY_NAME = 0;
/*     */   private static final int JZENTRY_EXTRA = 1;
/*     */   private static final int JZENTRY_COMMENT = 2;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public ZipFile(String paramString)
/*     */     throws IOException
/*     */   {
/* 116 */     this(new File(paramString), 1);
/*     */   }
/*     */ 
/*     */   public ZipFile(File paramFile, int paramInt)
/*     */     throws IOException
/*     */   {
/* 145 */     this(paramFile, paramInt, StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */   public ZipFile(File paramFile)
/*     */     throws ZipException, IOException
/*     */   {
/* 159 */     this(paramFile, 1);
/*     */   }
/*     */ 
/*     */   public ZipFile(File paramFile, int paramInt, Charset paramCharset)
/*     */     throws IOException
/*     */   {
/* 198 */     if (((paramInt & 0x1) == 0) || ((paramInt & 0xFFFFFFFA) != 0))
/*     */     {
/* 200 */       throw new IllegalArgumentException("Illegal mode: 0x" + Integer.toHexString(paramInt));
/*     */     }
/*     */ 
/* 203 */     String str = paramFile.getPath();
/* 204 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 205 */     if (localSecurityManager != null) {
/* 206 */       localSecurityManager.checkRead(str);
/* 207 */       if ((paramInt & 0x4) != 0) {
/* 208 */         localSecurityManager.checkDelete(str);
/*     */       }
/*     */     }
/* 211 */     if (paramCharset == null)
/* 212 */       throw new NullPointerException("charset is null");
/* 213 */     this.zc = ZipCoder.get(paramCharset);
/* 214 */     long l = System.nanoTime();
/* 215 */     this.jzfile = open(str, paramInt, paramFile.lastModified(), usemmap);
/* 216 */     PerfCounter.getZipFileOpenTime().addElapsedTimeFrom(l);
/* 217 */     PerfCounter.getZipFileCount().increment();
/* 218 */     this.name = str;
/* 219 */     this.total = getTotal(this.jzfile);
/* 220 */     this.locsig = startsWithLOC(this.jzfile);
/*     */   }
/*     */ 
/*     */   public ZipFile(String paramString, Charset paramCharset)
/*     */     throws IOException
/*     */   {
/* 249 */     this(new File(paramString), 1, paramCharset);
/*     */   }
/*     */ 
/*     */   public ZipFile(File paramFile, Charset paramCharset)
/*     */     throws IOException
/*     */   {
/* 269 */     this(paramFile, 1, paramCharset);
/*     */   }
/*     */ 
/*     */   public String getComment()
/*     */   {
/* 282 */     synchronized (this) {
/* 283 */       ensureOpen();
/* 284 */       byte[] arrayOfByte = getCommentBytes(this.jzfile);
/* 285 */       if (arrayOfByte == null)
/* 286 */         return null;
/* 287 */       return this.zc.toString(arrayOfByte, arrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ZipEntry getEntry(String paramString)
/*     */   {
/* 300 */     if (paramString == null) {
/* 301 */       throw new NullPointerException("name");
/*     */     }
/* 303 */     long l = 0L;
/* 304 */     synchronized (this) {
/* 305 */       ensureOpen();
/* 306 */       l = getEntry(this.jzfile, this.zc.getBytes(paramString), true);
/* 307 */       if (l != 0L) {
/* 308 */         ZipEntry localZipEntry = getZipEntry(paramString, l);
/* 309 */         freeEntry(this.jzfile, l);
/* 310 */         return localZipEntry;
/*     */       }
/*     */     }
/* 313 */     return null;
/*     */   }
/*     */ 
/*     */   private static native long getEntry(long paramLong, byte[] paramArrayOfByte, boolean paramBoolean);
/*     */ 
/*     */   private static native void freeEntry(long paramLong1, long paramLong2);
/*     */ 
/*     */   public InputStream getInputStream(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 341 */     if (paramZipEntry == null) {
/* 342 */       throw new NullPointerException("entry");
/*     */     }
/* 344 */     long l1 = 0L;
/* 345 */     ZipFileInputStream localZipFileInputStream = null;
/* 346 */     synchronized (this) {
/* 347 */       ensureOpen();
/* 348 */       if ((!this.zc.isUTF8()) && ((paramZipEntry.flag & 0x800) != 0))
/* 349 */         l1 = getEntry(this.jzfile, this.zc.getBytesUTF8(paramZipEntry.name), false);
/*     */       else {
/* 351 */         l1 = getEntry(this.jzfile, this.zc.getBytes(paramZipEntry.name), false);
/*     */       }
/* 353 */       if (l1 == 0L) {
/* 354 */         return null;
/*     */       }
/* 356 */       localZipFileInputStream = new ZipFileInputStream(l1);
/*     */ 
/* 358 */       switch (getEntryMethod(l1)) {
/*     */       case 0:
/* 360 */         synchronized (this.streams) {
/* 361 */           this.streams.put(localZipFileInputStream, null);
/*     */         }
/* 363 */         return localZipFileInputStream;
/*     */       case 8:
/* 366 */         long l2 = getEntrySize(l1) + 2L;
/* 367 */         if (l2 > 65536L) l2 = 8192L;
/* 368 */         if (l2 <= 0L) l2 = 4096L;
/* 369 */         Inflater localInflater = getInflater();
/* 370 */         ZipFileInflaterInputStream localZipFileInflaterInputStream = new ZipFileInflaterInputStream(localZipFileInputStream, localInflater, (int)l2);
/*     */ 
/* 372 */         synchronized (this.streams) {
/* 373 */           this.streams.put(localZipFileInflaterInputStream, localInflater);
/*     */         }
/* 375 */         return localZipFileInflaterInputStream;
/*     */       }
/* 377 */       throw new ZipException("invalid compression method");
/*     */     }
/*     */   }
/*     */ 
/*     */   private Inflater getInflater()
/*     */   {
/* 443 */     synchronized (this.inflaterCache)
/*     */     {
/*     */       Inflater localInflater;
/* 444 */       while (null != (localInflater = (Inflater)this.inflaterCache.poll())) {
/* 445 */         if (false == localInflater.ended()) {
/* 446 */           return localInflater;
/*     */         }
/*     */       }
/*     */     }
/* 450 */     return new Inflater(true);
/*     */   }
/*     */ 
/*     */   private void releaseInflater(Inflater paramInflater)
/*     */   {
/* 457 */     if (false == paramInflater.ended()) {
/* 458 */       paramInflater.reset();
/* 459 */       synchronized (this.inflaterCache) {
/* 460 */         this.inflaterCache.add(paramInflater);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 473 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Enumeration<? extends ZipEntry> entries()
/*     */   {
/* 482 */     ensureOpen();
/* 483 */     return new Enumeration() {
/* 484 */       private int i = 0;
/*     */ 
/* 486 */       public boolean hasMoreElements() { synchronized (ZipFile.this) {
/* 487 */           ZipFile.this.ensureOpen();
/* 488 */           return this.i < ZipFile.this.total;
/*     */         } }
/*     */ 
/*     */       public ZipEntry nextElement() throws NoSuchElementException {
/* 492 */         synchronized (ZipFile.this) {
/* 493 */           ZipFile.this.ensureOpen();
/* 494 */           if (this.i >= ZipFile.this.total) {
/* 495 */             throw new NoSuchElementException();
/*     */           }
/* 497 */           long l = ZipFile.getNextEntry(ZipFile.this.jzfile, this.i++);
/* 498 */           if (l == 0L)
/*     */           {
/* 500 */             if (ZipFile.this.closeRequested)
/* 501 */               localObject1 = "ZipFile concurrently closed";
/*     */             else {
/* 503 */               localObject1 = ZipFile.getZipMessage(ZipFile.this.jzfile);
/*     */             }
/* 505 */             throw new ZipError("jzentry == 0,\n jzfile = " + ZipFile.this.jzfile + ",\n total = " + ZipFile.this.total + ",\n name = " + ZipFile.this.name + ",\n i = " + this.i + ",\n message = " + (String)localObject1);
/*     */           }
/*     */ 
/* 513 */           Object localObject1 = ZipFile.this.getZipEntry(null, l);
/* 514 */           ZipFile.freeEntry(ZipFile.this.jzfile, l);
/* 515 */           return localObject1;
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private ZipEntry getZipEntry(String paramString, long paramLong) {
/* 522 */     ZipEntry localZipEntry = new ZipEntry();
/* 523 */     localZipEntry.flag = getEntryFlag(paramLong);
/* 524 */     if (paramString != null) {
/* 525 */       localZipEntry.name = paramString;
/*     */     } else {
/* 527 */       arrayOfByte = getEntryBytes(paramLong, 0);
/* 528 */       if ((!this.zc.isUTF8()) && ((localZipEntry.flag & 0x800) != 0))
/* 529 */         localZipEntry.name = this.zc.toStringUTF8(arrayOfByte, arrayOfByte.length);
/*     */       else {
/* 531 */         localZipEntry.name = this.zc.toString(arrayOfByte, arrayOfByte.length);
/*     */       }
/*     */     }
/* 534 */     localZipEntry.time = getEntryTime(paramLong);
/* 535 */     localZipEntry.crc = getEntryCrc(paramLong);
/* 536 */     localZipEntry.size = getEntrySize(paramLong);
/* 537 */     localZipEntry.csize = getEntryCSize(paramLong);
/* 538 */     localZipEntry.method = getEntryMethod(paramLong);
/* 539 */     localZipEntry.extra = getEntryBytes(paramLong, 1);
/* 540 */     byte[] arrayOfByte = getEntryBytes(paramLong, 2);
/* 541 */     if (arrayOfByte == null) {
/* 542 */       localZipEntry.comment = null;
/*     */     }
/* 544 */     else if ((!this.zc.isUTF8()) && ((localZipEntry.flag & 0x800) != 0))
/* 545 */       localZipEntry.comment = this.zc.toStringUTF8(arrayOfByte, arrayOfByte.length);
/*     */     else {
/* 547 */       localZipEntry.comment = this.zc.toString(arrayOfByte, arrayOfByte.length);
/*     */     }
/*     */ 
/* 550 */     return localZipEntry;
/*     */   }
/*     */ 
/*     */   private static native long getNextEntry(long paramLong, int paramInt);
/*     */ 
/*     */   public int size()
/*     */   {
/* 561 */     ensureOpen();
/* 562 */     return this.total;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 574 */     if (this.closeRequested)
/* 575 */       return;
/* 576 */     this.closeRequested = true;
/*     */ 
/* 578 */     synchronized (this)
/*     */     {
/* 580 */       synchronized (this.streams) {
/* 581 */         if (false == this.streams.isEmpty()) {
/* 582 */           HashMap localHashMap = new HashMap(this.streams);
/* 583 */           this.streams.clear();
/* 584 */           for (Map.Entry localEntry : localHashMap.entrySet()) {
/* 585 */             ((InputStream)localEntry.getKey()).close();
/* 586 */             Inflater localInflater = (Inflater)localEntry.getValue();
/* 587 */             if (localInflater != null) {
/* 588 */               localInflater.end();
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 596 */       synchronized (this.inflaterCache) {
/* 597 */         while (null != ( = (Inflater)this.inflaterCache.poll())) {
/* 598 */           ((Inflater)???).end();
/*     */         }
/*     */       }
/*     */ 
/* 602 */       if (this.jzfile != 0L)
/*     */       {
/* 604 */         long l = this.jzfile;
/* 605 */         this.jzfile = 0L;
/*     */ 
/* 607 */         close(l);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws IOException
/*     */   {
/* 627 */     close();
/*     */   }
/*     */ 
/*     */   private static native void close(long paramLong);
/*     */ 
/*     */   private void ensureOpen() {
/* 633 */     if (this.closeRequested) {
/* 634 */       throw new IllegalStateException("zip file closed");
/*     */     }
/*     */ 
/* 637 */     if (this.jzfile == 0L)
/* 638 */       throw new IllegalStateException("The object is not initialized.");
/*     */   }
/*     */ 
/*     */   private void ensureOpenOrZipException() throws IOException
/*     */   {
/* 643 */     if (this.closeRequested)
/* 644 */       throw new ZipException("ZipFile closed");
/*     */   }
/*     */ 
/*     */   private boolean startsWithLocHeader()
/*     */   {
/* 757 */     return this.locsig;
/*     */   }
/*     */ 
/*     */   private static native long open(String paramString, int paramInt, long paramLong, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int getTotal(long paramLong);
/*     */ 
/*     */   private static native boolean startsWithLOC(long paramLong);
/*     */ 
/*     */   private static native int read(long paramLong1, long paramLong2, long paramLong3, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   private static native long getEntryTime(long paramLong);
/*     */ 
/*     */   private static native long getEntryCrc(long paramLong);
/*     */ 
/*     */   private static native long getEntryCSize(long paramLong);
/*     */ 
/*     */   private static native long getEntrySize(long paramLong);
/*     */ 
/*     */   private static native int getEntryMethod(long paramLong);
/*     */ 
/*     */   private static native int getEntryFlag(long paramLong);
/*     */ 
/*     */   private static native byte[] getCommentBytes(long paramLong);
/*     */ 
/*     */   private static native byte[] getEntryBytes(long paramLong, int paramInt);
/*     */ 
/*     */   private static native String getZipMessage(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/*  82 */     initIDs();
/*     */ 
/*  92 */     String str = VM.getSavedProperty("sun.zip.disableMemoryMapping");
/*  93 */     usemmap = (str == null) || ((str.length() != 0) && (!str.equalsIgnoreCase("true")));
/*     */ 
/* 743 */     SharedSecrets.setJavaUtilZipFileAccess(new JavaUtilZipFileAccess()
/*     */     {
/*     */       public boolean startsWithLocHeader(ZipFile paramAnonymousZipFile) {
/* 746 */         return paramAnonymousZipFile.startsWithLocHeader();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private class ZipFileInflaterInputStream extends InflaterInputStream
/*     */   {
/* 383 */     private volatile boolean closeRequested = false;
/* 384 */     private boolean eof = false;
/*     */     private final ZipFile.ZipFileInputStream zfin;
/*     */ 
/*     */     ZipFileInflaterInputStream(ZipFile.ZipFileInputStream paramInflater, Inflater paramInt, int arg4)
/*     */     {
/* 389 */       super(paramInt, i);
/* 390 */       this.zfin = paramInflater;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 394 */       if (this.closeRequested)
/* 395 */         return;
/* 396 */       this.closeRequested = true;
/*     */ 
/* 398 */       super.close();
/*     */       Inflater localInflater;
/* 400 */       synchronized (ZipFile.this.streams) {
/* 401 */         localInflater = (Inflater)ZipFile.this.streams.remove(this);
/*     */       }
/* 403 */       if (localInflater != null)
/* 404 */         ZipFile.this.releaseInflater(localInflater);
/*     */     }
/*     */ 
/*     */     protected void fill()
/*     */       throws IOException
/*     */     {
/* 412 */       if (this.eof) {
/* 413 */         throw new EOFException("Unexpected end of ZLIB input stream");
/*     */       }
/* 415 */       this.len = this.in.read(this.buf, 0, this.buf.length);
/* 416 */       if (this.len == -1) {
/* 417 */         this.buf[0] = 0;
/* 418 */         this.len = 1;
/* 419 */         this.eof = true;
/*     */       }
/* 421 */       this.inf.setInput(this.buf, 0, this.len);
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 425 */       if (this.closeRequested)
/* 426 */         return 0;
/* 427 */       long l = this.zfin.size() - this.inf.getBytesWritten();
/* 428 */       return l > 2147483647L ? 2147483647 : (int)l;
/*     */     }
/*     */ 
/*     */     protected void finalize() throws Throwable
/*     */     {
/* 433 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ZipFileInputStream extends InputStream
/*     */   {
/* 653 */     private volatile boolean closeRequested = false;
/*     */     protected long jzentry;
/* 660 */     private long pos = 0L;
/*     */     protected long rem;
/*     */     protected long size;
/*     */ 
/*     */     ZipFileInputStream(long arg2)
/*     */     {
/*     */       Object localObject;
/* 661 */       this.rem = ZipFile.getEntryCSize(localObject);
/* 662 */       this.size = ZipFile.getEntrySize(localObject);
/* 663 */       this.jzentry = localObject;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 667 */       if (this.rem == 0L) {
/* 668 */         return -1;
/*     */       }
/* 670 */       if (paramInt2 <= 0) {
/* 671 */         return 0;
/*     */       }
/* 673 */       if (paramInt2 > this.rem) {
/* 674 */         paramInt2 = (int)this.rem;
/*     */       }
/* 676 */       synchronized (ZipFile.this) {
/* 677 */         ZipFile.this.ensureOpenOrZipException();
/*     */ 
/* 679 */         paramInt2 = ZipFile.read(ZipFile.this.jzfile, this.jzentry, this.pos, paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/*     */ 
/* 682 */       if (paramInt2 > 0) {
/* 683 */         this.pos += paramInt2;
/* 684 */         this.rem -= paramInt2;
/*     */       }
/* 686 */       if (this.rem == 0L) {
/* 687 */         close();
/*     */       }
/* 689 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 693 */       byte[] arrayOfByte = new byte[1];
/* 694 */       if (read(arrayOfByte, 0, 1) == 1) {
/* 695 */         return arrayOfByte[0] & 0xFF;
/*     */       }
/* 697 */       return -1;
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong)
/*     */     {
/* 702 */       if (paramLong > this.rem)
/* 703 */         paramLong = this.rem;
/* 704 */       this.pos += paramLong;
/* 705 */       this.rem -= paramLong;
/* 706 */       if (this.rem == 0L) {
/* 707 */         close();
/*     */       }
/* 709 */       return paramLong;
/*     */     }
/*     */ 
/*     */     public int available() {
/* 713 */       return this.rem > 2147483647L ? 2147483647 : (int)this.rem;
/*     */     }
/*     */ 
/*     */     public long size() {
/* 717 */       return this.size;
/*     */     }
/*     */ 
/*     */     public void close() {
/* 721 */       if (this.closeRequested)
/* 722 */         return;
/* 723 */       this.closeRequested = true;
/*     */ 
/* 725 */       this.rem = 0L;
/* 726 */       synchronized (ZipFile.this) {
/* 727 */         if ((this.jzentry != 0L) && (ZipFile.this.jzfile != 0L)) {
/* 728 */           ZipFile.freeEntry(ZipFile.this.jzfile, this.jzentry);
/* 729 */           this.jzentry = 0L;
/*     */         }
/*     */       }
/* 732 */       synchronized (ZipFile.this.streams) {
/* 733 */         ZipFile.this.streams.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void finalize() {
/* 738 */       close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZipFile
 * JD-Core Version:    0.6.2
 */