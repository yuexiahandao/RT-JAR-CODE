/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ public class ZipInputStream extends InflaterInputStream
/*     */   implements ZipConstants
/*     */ {
/*     */   private ZipEntry entry;
/*     */   private int flag;
/*  47 */   private CRC32 crc = new CRC32();
/*     */   private long remaining;
/*  49 */   private byte[] tmpbuf = new byte[512];
/*     */   private static final int STORED = 0;
/*     */   private static final int DEFLATED = 8;
/*  54 */   private boolean closed = false;
/*     */ 
/*  57 */   private boolean entryEOF = false;
/*     */   private ZipCoder zc;
/* 270 */   private byte[] b = new byte[256];
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  65 */     if (this.closed)
/*  66 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public ZipInputStream(InputStream paramInputStream)
/*     */   {
/*  79 */     this(paramInputStream, StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */   public ZipInputStream(InputStream paramInputStream, Charset paramCharset)
/*     */   {
/*  97 */     super(new PushbackInputStream(paramInputStream, 512), new Inflater(true), 512);
/*  98 */     this.usesDefaultInflater = true;
/*  99 */     if (paramInputStream == null) {
/* 100 */       throw new NullPointerException("in is null");
/*     */     }
/* 102 */     if (paramCharset == null)
/* 103 */       throw new NullPointerException("charset is null");
/* 104 */     this.zc = ZipCoder.get(paramCharset);
/*     */   }
/*     */ 
/*     */   public ZipEntry getNextEntry()
/*     */     throws IOException
/*     */   {
/* 115 */     ensureOpen();
/* 116 */     if (this.entry != null) {
/* 117 */       closeEntry();
/*     */     }
/* 119 */     this.crc.reset();
/* 120 */     this.inf.reset();
/* 121 */     if ((this.entry = readLOC()) == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     if (this.entry.method == 0) {
/* 125 */       this.remaining = this.entry.size;
/*     */     }
/* 127 */     this.entryEOF = false;
/* 128 */     return this.entry;
/*     */   }
/*     */ 
/*     */   public void closeEntry()
/*     */     throws IOException
/*     */   {
/* 138 */     ensureOpen();
/* 139 */     while (read(this.tmpbuf, 0, this.tmpbuf.length) != -1);
/* 140 */     this.entryEOF = true;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 155 */     ensureOpen();
/* 156 */     if (this.entryEOF) {
/* 157 */       return 0;
/*     */     }
/* 159 */     return 1;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 181 */     ensureOpen();
/* 182 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2))
/* 183 */       throw new IndexOutOfBoundsException();
/* 184 */     if (paramInt2 == 0) {
/* 185 */       return 0;
/*     */     }
/*     */ 
/* 188 */     if (this.entry == null) {
/* 189 */       return -1;
/*     */     }
/* 191 */     switch (this.entry.method) {
/*     */     case 8:
/* 193 */       paramInt2 = super.read(paramArrayOfByte, paramInt1, paramInt2);
/* 194 */       if (paramInt2 == -1) {
/* 195 */         readEnd(this.entry);
/* 196 */         this.entryEOF = true;
/* 197 */         this.entry = null;
/*     */       } else {
/* 199 */         this.crc.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */       }
/* 201 */       return paramInt2;
/*     */     case 0:
/* 203 */       if (this.remaining <= 0L) {
/* 204 */         this.entryEOF = true;
/* 205 */         this.entry = null;
/* 206 */         return -1;
/*     */       }
/* 208 */       if (paramInt2 > this.remaining) {
/* 209 */         paramInt2 = (int)this.remaining;
/*     */       }
/* 211 */       paramInt2 = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 212 */       if (paramInt2 == -1) {
/* 213 */         throw new ZipException("unexpected EOF");
/*     */       }
/* 215 */       this.crc.update(paramArrayOfByte, paramInt1, paramInt2);
/* 216 */       this.remaining -= paramInt2;
/* 217 */       if ((this.remaining == 0L) && (this.entry.crc != this.crc.getValue())) {
/* 218 */         throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(this.entry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
/*     */       }
/*     */ 
/* 222 */       return paramInt2;
/*     */     }
/* 224 */     throw new ZipException("invalid compression method");
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/* 237 */     if (paramLong < 0L) {
/* 238 */       throw new IllegalArgumentException("negative skip length");
/*     */     }
/* 240 */     ensureOpen();
/* 241 */     int i = (int)Math.min(paramLong, 2147483647L);
/* 242 */     int j = 0;
/* 243 */     while (j < i) {
/* 244 */       int k = i - j;
/* 245 */       if (k > this.tmpbuf.length) {
/* 246 */         k = this.tmpbuf.length;
/*     */       }
/* 248 */       k = read(this.tmpbuf, 0, k);
/* 249 */       if (k == -1) {
/* 250 */         this.entryEOF = true;
/* 251 */         break;
/*     */       }
/* 253 */       j += k;
/*     */     }
/* 255 */     return j;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 264 */     if (!this.closed) {
/* 265 */       super.close();
/* 266 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private ZipEntry readLOC()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 277 */       readFully(this.tmpbuf, 0, 30);
/*     */     } catch (EOFException localEOFException) {
/* 279 */       return null;
/*     */     }
/* 281 */     if (get32(this.tmpbuf, 0) != 67324752L) {
/* 282 */       return null;
/*     */     }
/*     */ 
/* 285 */     this.flag = get16(this.tmpbuf, 6);
/*     */ 
/* 287 */     int i = get16(this.tmpbuf, 26);
/* 288 */     int j = this.b.length;
/* 289 */     if (i > j) {
/*     */       do
/* 291 */         j *= 2;
/* 292 */       while (i > j);
/* 293 */       this.b = new byte[j];
/*     */     }
/* 295 */     readFully(this.b, 0, i);
/*     */ 
/* 297 */     ZipEntry localZipEntry = createZipEntry((this.flag & 0x800) != 0 ? this.zc.toStringUTF8(this.b, i) : this.zc.toString(this.b, i));
/*     */ 
/* 301 */     if ((this.flag & 0x1) == 1) {
/* 302 */       throw new ZipException("encrypted ZIP entry not supported");
/*     */     }
/* 304 */     localZipEntry.method = get16(this.tmpbuf, 8);
/* 305 */     localZipEntry.time = get32(this.tmpbuf, 10);
/* 306 */     if ((this.flag & 0x8) == 8)
/*     */     {
/* 308 */       if (localZipEntry.method != 8)
/* 309 */         throw new ZipException("only DEFLATED entries can have EXT descriptor");
/*     */     }
/*     */     else
/*     */     {
/* 313 */       localZipEntry.crc = get32(this.tmpbuf, 14);
/* 314 */       localZipEntry.csize = get32(this.tmpbuf, 18);
/* 315 */       localZipEntry.size = get32(this.tmpbuf, 22);
/*     */     }
/* 317 */     i = get16(this.tmpbuf, 28);
/* 318 */     if (i > 0) {
/* 319 */       byte[] arrayOfByte = new byte[i];
/* 320 */       readFully(arrayOfByte, 0, i);
/* 321 */       localZipEntry.setExtra(arrayOfByte);
/*     */ 
/* 323 */       if ((localZipEntry.csize == 4294967295L) || (localZipEntry.size == 4294967295L)) {
/* 324 */         int k = 0;
/* 325 */         while (k + 4 < i) {
/* 326 */           int m = get16(arrayOfByte, k + 2);
/* 327 */           if (get16(arrayOfByte, k) == 1) {
/* 328 */             k += 4;
/*     */ 
/* 331 */             if ((m < 16) || (k + m > i))
/*     */             {
/* 336 */               return localZipEntry;
/*     */             }
/* 338 */             localZipEntry.size = get64(arrayOfByte, k);
/* 339 */             localZipEntry.csize = get64(arrayOfByte, k + 8);
/* 340 */             break;
/*     */           }
/* 342 */           k += m + 4;
/*     */         }
/*     */       }
/*     */     }
/* 346 */     return localZipEntry;
/*     */   }
/*     */ 
/*     */   protected ZipEntry createZipEntry(String paramString)
/*     */   {
/* 357 */     return new ZipEntry(paramString);
/*     */   }
/*     */ 
/*     */   private void readEnd(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 364 */     int i = this.inf.getRemaining();
/* 365 */     if (i > 0) {
/* 366 */       ((PushbackInputStream)this.in).unread(this.buf, this.len - i, i);
/*     */     }
/* 368 */     if ((this.flag & 0x8) == 8)
/*     */     {
/*     */       long l;
/* 370 */       if ((this.inf.getBytesWritten() > 4294967295L) || (this.inf.getBytesRead() > 4294967295L))
/*     */       {
/* 373 */         readFully(this.tmpbuf, 0, 24);
/* 374 */         l = get32(this.tmpbuf, 0);
/* 375 */         if (l != 134695760L) {
/* 376 */           paramZipEntry.crc = l;
/* 377 */           paramZipEntry.csize = get64(this.tmpbuf, 4);
/* 378 */           paramZipEntry.size = get64(this.tmpbuf, 12);
/* 379 */           ((PushbackInputStream)this.in).unread(this.tmpbuf, 19, 4);
/*     */         }
/*     */         else {
/* 382 */           paramZipEntry.crc = get32(this.tmpbuf, 4);
/* 383 */           paramZipEntry.csize = get64(this.tmpbuf, 8);
/* 384 */           paramZipEntry.size = get64(this.tmpbuf, 16);
/*     */         }
/*     */       } else {
/* 387 */         readFully(this.tmpbuf, 0, 16);
/* 388 */         l = get32(this.tmpbuf, 0);
/* 389 */         if (l != 134695760L) {
/* 390 */           paramZipEntry.crc = l;
/* 391 */           paramZipEntry.csize = get32(this.tmpbuf, 4);
/* 392 */           paramZipEntry.size = get32(this.tmpbuf, 8);
/* 393 */           ((PushbackInputStream)this.in).unread(this.tmpbuf, 11, 4);
/*     */         }
/*     */         else {
/* 396 */           paramZipEntry.crc = get32(this.tmpbuf, 4);
/* 397 */           paramZipEntry.csize = get32(this.tmpbuf, 8);
/* 398 */           paramZipEntry.size = get32(this.tmpbuf, 12);
/*     */         }
/*     */       }
/*     */     }
/* 402 */     if (paramZipEntry.size != this.inf.getBytesWritten()) {
/* 403 */       throw new ZipException("invalid entry size (expected " + paramZipEntry.size + " but got " + this.inf.getBytesWritten() + " bytes)");
/*     */     }
/*     */ 
/* 407 */     if (paramZipEntry.csize != this.inf.getBytesRead()) {
/* 408 */       throw new ZipException("invalid entry compressed size (expected " + paramZipEntry.csize + " but got " + this.inf.getBytesRead() + " bytes)");
/*     */     }
/*     */ 
/* 412 */     if (paramZipEntry.crc != this.crc.getValue())
/* 413 */       throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(paramZipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
/*     */   }
/*     */ 
/*     */   private void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 423 */     while (paramInt2 > 0) {
/* 424 */       int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 425 */       if (i == -1) {
/* 426 */         throw new EOFException();
/*     */       }
/* 428 */       paramInt1 += i;
/* 429 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final int get16(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 438 */     return paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8;
/*     */   }
/*     */ 
/*     */   private static final long get32(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 446 */     return (get16(paramArrayOfByte, paramInt) | get16(paramArrayOfByte, paramInt + 2) << 16) & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   private static final long get64(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 454 */     return get32(paramArrayOfByte, paramInt) | get32(paramArrayOfByte, paramInt + 4) << 32;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZipInputStream
 * JD-Core Version:    0.6.2
 */