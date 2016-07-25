/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ZipOutputStream extends DeflaterOutputStream
/*     */   implements ZipConstants
/*     */ {
/*     */   private XEntry current;
/*  56 */   private Vector<XEntry> xentries = new Vector();
/*  57 */   private HashSet<String> names = new HashSet();
/*  58 */   private CRC32 crc = new CRC32();
/*  59 */   private long written = 0L;
/*  60 */   private long locoff = 0L;
/*     */   private byte[] comment;
/*  62 */   private int method = 8;
/*     */   private boolean finished;
/*  65 */   private boolean closed = false;
/*     */   private final ZipCoder zc;
/*     */   public static final int STORED = 0;
/*     */   public static final int DEFLATED = 8;
/*     */ 
/*     */   private static int version(ZipEntry paramZipEntry)
/*     */     throws ZipException
/*     */   {
/*  70 */     switch (paramZipEntry.method) { case 8:
/*  71 */       return 20;
/*     */     case 0:
/*  72 */       return 10; }
/*  73 */     throw new ZipException("unsupported compression method");
/*     */   }
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  81 */     if (this.closed)
/*  82 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public ZipOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 104 */     this(paramOutputStream, StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */   public ZipOutputStream(OutputStream paramOutputStream, Charset paramCharset)
/*     */   {
/* 118 */     super(paramOutputStream, new Deflater(-1, true));
/* 119 */     if (paramCharset == null)
/* 120 */       throw new NullPointerException("charset is null");
/* 121 */     this.zc = ZipCoder.get(paramCharset);
/* 122 */     this.usesDefaultDeflater = true;
/*     */   }
/*     */ 
/*     */   public void setComment(String paramString)
/*     */   {
/* 132 */     if (paramString != null) {
/* 133 */       this.comment = this.zc.getBytes(paramString);
/* 134 */       if (this.comment.length > 65535)
/* 135 */         throw new IllegalArgumentException("ZIP file comment too long.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMethod(int paramInt)
/*     */   {
/* 148 */     if ((paramInt != 8) && (paramInt != 0)) {
/* 149 */       throw new IllegalArgumentException("invalid compression method");
/*     */     }
/* 151 */     this.method = paramInt;
/*     */   }
/*     */ 
/*     */   public void setLevel(int paramInt)
/*     */   {
/* 161 */     this.def.setLevel(paramInt);
/*     */   }
/*     */ 
/*     */   public void putNextEntry(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 175 */     ensureOpen();
/* 176 */     if (this.current != null) {
/* 177 */       closeEntry();
/*     */     }
/* 179 */     if (paramZipEntry.time == -1L) {
/* 180 */       paramZipEntry.setTime(System.currentTimeMillis());
/*     */     }
/* 182 */     if (paramZipEntry.method == -1) {
/* 183 */       paramZipEntry.method = this.method;
/*     */     }
/*     */ 
/* 186 */     paramZipEntry.flag = 0;
/* 187 */     switch (paramZipEntry.method)
/*     */     {
/*     */     case 8:
/* 191 */       if ((paramZipEntry.size == -1L) || (paramZipEntry.csize == -1L) || (paramZipEntry.crc == -1L))
/* 192 */         paramZipEntry.flag = 8; break;
/*     */     case 0:
/* 198 */       if (paramZipEntry.size == -1L)
/* 199 */         paramZipEntry.size = paramZipEntry.csize;
/* 200 */       else if (paramZipEntry.csize == -1L)
/* 201 */         paramZipEntry.csize = paramZipEntry.size;
/* 202 */       else if (paramZipEntry.size != paramZipEntry.csize) {
/* 203 */         throw new ZipException("STORED entry where compressed != uncompressed size");
/*     */       }
/*     */ 
/* 206 */       if ((paramZipEntry.size == -1L) || (paramZipEntry.crc == -1L)) {
/* 207 */         throw new ZipException("STORED entry missing size, compressed size, or crc-32");
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 212 */       throw new ZipException("unsupported compression method");
/*     */     }
/* 214 */     if (!this.names.add(paramZipEntry.name)) {
/* 215 */       throw new ZipException("duplicate entry: " + paramZipEntry.name);
/*     */     }
/* 217 */     if (this.zc.isUTF8())
/* 218 */       paramZipEntry.flag |= 2048;
/* 219 */     this.current = new XEntry(paramZipEntry, this.written);
/* 220 */     this.xentries.add(this.current);
/* 221 */     writeLOC(this.current);
/*     */   }
/*     */ 
/*     */   public void closeEntry()
/*     */     throws IOException
/*     */   {
/* 231 */     ensureOpen();
/* 232 */     if (this.current != null) {
/* 233 */       ZipEntry localZipEntry = this.current.entry;
/* 234 */       switch (localZipEntry.method) {
/*     */       case 8:
/* 236 */         this.def.finish();
/* 237 */         while (!this.def.finished()) {
/* 238 */           deflate();
/*     */         }
/* 240 */         if ((localZipEntry.flag & 0x8) == 0)
/*     */         {
/* 242 */           if (localZipEntry.size != this.def.getBytesRead()) {
/* 243 */             throw new ZipException("invalid entry size (expected " + localZipEntry.size + " but got " + this.def.getBytesRead() + " bytes)");
/*     */           }
/*     */ 
/* 247 */           if (localZipEntry.csize != this.def.getBytesWritten()) {
/* 248 */             throw new ZipException("invalid entry compressed size (expected " + localZipEntry.csize + " but got " + this.def.getBytesWritten() + " bytes)");
/*     */           }
/*     */ 
/* 252 */           if (localZipEntry.crc != this.crc.getValue()) {
/* 253 */             throw new ZipException("invalid entry CRC-32 (expected 0x" + Long.toHexString(localZipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 259 */           localZipEntry.size = this.def.getBytesRead();
/* 260 */           localZipEntry.csize = this.def.getBytesWritten();
/* 261 */           localZipEntry.crc = this.crc.getValue();
/* 262 */           writeEXT(localZipEntry);
/*     */         }
/* 264 */         this.def.reset();
/* 265 */         this.written += localZipEntry.csize;
/* 266 */         break;
/*     */       case 0:
/* 269 */         if (localZipEntry.size != this.written - this.locoff) {
/* 270 */           throw new ZipException("invalid entry size (expected " + localZipEntry.size + " but got " + (this.written - this.locoff) + " bytes)");
/*     */         }
/*     */ 
/* 274 */         if (localZipEntry.crc != this.crc.getValue()) {
/* 275 */           throw new ZipException("invalid entry crc-32 (expected 0x" + Long.toHexString(localZipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
/*     */         }
/*     */ 
/*     */         break;
/*     */       default:
/* 282 */         throw new ZipException("invalid compression method");
/*     */       }
/* 284 */       this.crc.reset();
/* 285 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 301 */     ensureOpen();
/* 302 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2))
/* 303 */       throw new IndexOutOfBoundsException();
/* 304 */     if (paramInt2 == 0) {
/* 305 */       return;
/*     */     }
/*     */ 
/* 308 */     if (this.current == null) {
/* 309 */       throw new ZipException("no current ZIP entry");
/*     */     }
/* 311 */     ZipEntry localZipEntry = this.current.entry;
/* 312 */     switch (localZipEntry.method) {
/*     */     case 8:
/* 314 */       super.write(paramArrayOfByte, paramInt1, paramInt2);
/* 315 */       break;
/*     */     case 0:
/* 317 */       this.written += paramInt2;
/* 318 */       if (this.written - this.locoff > localZipEntry.size) {
/* 319 */         throw new ZipException("attempt to write past end of STORED entry");
/*     */       }
/*     */ 
/* 322 */       this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 323 */       break;
/*     */     default:
/* 325 */       throw new ZipException("invalid compression method");
/*     */     }
/* 327 */     this.crc.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */     throws IOException
/*     */   {
/* 338 */     ensureOpen();
/* 339 */     if (this.finished) {
/* 340 */       return;
/*     */     }
/* 342 */     if (this.current != null) {
/* 343 */       closeEntry();
/*     */     }
/*     */ 
/* 346 */     long l = this.written;
/* 347 */     for (XEntry localXEntry : this.xentries)
/* 348 */       writeCEN(localXEntry);
/* 349 */     writeEND(l, this.written - l);
/* 350 */     this.finished = true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 359 */     if (!this.closed) {
/* 360 */       super.close();
/* 361 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeLOC(XEntry paramXEntry)
/*     */     throws IOException
/*     */   {
/* 369 */     ZipEntry localZipEntry = paramXEntry.entry;
/* 370 */     int i = localZipEntry.flag;
/* 371 */     int j = localZipEntry.extra != null ? localZipEntry.extra.length : 0;
/* 372 */     int k = 0;
/*     */ 
/* 374 */     writeInt(67324752L);
/*     */ 
/* 376 */     if ((i & 0x8) == 8) {
/* 377 */       writeShort(version(localZipEntry));
/* 378 */       writeShort(i);
/* 379 */       writeShort(localZipEntry.method);
/* 380 */       writeInt(localZipEntry.time);
/*     */ 
/* 384 */       writeInt(0L);
/* 385 */       writeInt(0L);
/* 386 */       writeInt(0L);
/*     */     } else {
/* 388 */       if ((localZipEntry.csize >= 4294967295L) || (localZipEntry.size >= 4294967295L)) {
/* 389 */         k = 1;
/* 390 */         writeShort(45);
/*     */       } else {
/* 392 */         writeShort(version(localZipEntry));
/*     */       }
/* 394 */       writeShort(i);
/* 395 */       writeShort(localZipEntry.method);
/* 396 */       writeInt(localZipEntry.time);
/* 397 */       writeInt(localZipEntry.crc);
/* 398 */       if (k != 0) {
/* 399 */         writeInt(4294967295L);
/* 400 */         writeInt(4294967295L);
/* 401 */         j += 20;
/*     */       } else {
/* 403 */         writeInt(localZipEntry.csize);
/* 404 */         writeInt(localZipEntry.size);
/*     */       }
/*     */     }
/* 407 */     byte[] arrayOfByte = this.zc.getBytes(localZipEntry.name);
/* 408 */     writeShort(arrayOfByte.length);
/* 409 */     writeShort(j);
/* 410 */     writeBytes(arrayOfByte, 0, arrayOfByte.length);
/* 411 */     if (k != 0) {
/* 412 */       writeShort(1);
/* 413 */       writeShort(16);
/* 414 */       writeLong(localZipEntry.size);
/* 415 */       writeLong(localZipEntry.csize);
/*     */     }
/* 417 */     if (localZipEntry.extra != null) {
/* 418 */       writeBytes(localZipEntry.extra, 0, localZipEntry.extra.length);
/*     */     }
/* 420 */     this.locoff = this.written;
/*     */   }
/*     */ 
/*     */   private void writeEXT(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 427 */     writeInt(134695760L);
/* 428 */     writeInt(paramZipEntry.crc);
/* 429 */     if ((paramZipEntry.csize >= 4294967295L) || (paramZipEntry.size >= 4294967295L)) {
/* 430 */       writeLong(paramZipEntry.csize);
/* 431 */       writeLong(paramZipEntry.size);
/*     */     } else {
/* 433 */       writeInt(paramZipEntry.csize);
/* 434 */       writeInt(paramZipEntry.size);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeCEN(XEntry paramXEntry)
/*     */     throws IOException
/*     */   {
/* 443 */     ZipEntry localZipEntry = paramXEntry.entry;
/* 444 */     int i = localZipEntry.flag;
/* 445 */     int j = version(localZipEntry);
/*     */ 
/* 447 */     long l1 = localZipEntry.csize;
/* 448 */     long l2 = localZipEntry.size;
/* 449 */     long l3 = paramXEntry.offset;
/* 450 */     int k = 0;
/* 451 */     int m = 0;
/* 452 */     if (localZipEntry.csize >= 4294967295L) {
/* 453 */       l1 = 4294967295L;
/* 454 */       k += 8;
/* 455 */       m = 1;
/*     */     }
/* 457 */     if (localZipEntry.size >= 4294967295L) {
/* 458 */       l2 = 4294967295L;
/* 459 */       k += 8;
/* 460 */       m = 1;
/*     */     }
/* 462 */     if (paramXEntry.offset >= 4294967295L) {
/* 463 */       l3 = 4294967295L;
/* 464 */       k += 8;
/* 465 */       m = 1;
/*     */     }
/* 467 */     writeInt(33639248L);
/* 468 */     if (m != 0) {
/* 469 */       writeShort(45);
/* 470 */       writeShort(45);
/*     */     } else {
/* 472 */       writeShort(j);
/* 473 */       writeShort(j);
/*     */     }
/* 475 */     writeShort(i);
/* 476 */     writeShort(localZipEntry.method);
/* 477 */     writeInt(localZipEntry.time);
/* 478 */     writeInt(localZipEntry.crc);
/* 479 */     writeInt(l1);
/* 480 */     writeInt(l2);
/* 481 */     byte[] arrayOfByte1 = this.zc.getBytes(localZipEntry.name);
/* 482 */     writeShort(arrayOfByte1.length);
/* 483 */     if (m != 0)
/*     */     {
/* 485 */       writeShort(k + 4 + (localZipEntry.extra != null ? localZipEntry.extra.length : 0));
/*     */     }
/* 487 */     else writeShort(localZipEntry.extra != null ? localZipEntry.extra.length : 0);
/*     */     byte[] arrayOfByte2;
/* 490 */     if (localZipEntry.comment != null) {
/* 491 */       arrayOfByte2 = this.zc.getBytes(localZipEntry.comment);
/* 492 */       writeShort(Math.min(arrayOfByte2.length, 65535));
/*     */     } else {
/* 494 */       arrayOfByte2 = null;
/* 495 */       writeShort(0);
/*     */     }
/* 497 */     writeShort(0);
/* 498 */     writeShort(0);
/* 499 */     writeInt(0L);
/* 500 */     writeInt(l3);
/* 501 */     writeBytes(arrayOfByte1, 0, arrayOfByte1.length);
/* 502 */     if (m != 0) {
/* 503 */       writeShort(1);
/* 504 */       writeShort(k);
/* 505 */       if (l2 == 4294967295L)
/* 506 */         writeLong(localZipEntry.size);
/* 507 */       if (l1 == 4294967295L)
/* 508 */         writeLong(localZipEntry.csize);
/* 509 */       if (l3 == 4294967295L)
/* 510 */         writeLong(paramXEntry.offset);
/*     */     }
/* 512 */     if (localZipEntry.extra != null) {
/* 513 */       writeBytes(localZipEntry.extra, 0, localZipEntry.extra.length);
/*     */     }
/* 515 */     if (arrayOfByte2 != null)
/* 516 */       writeBytes(arrayOfByte2, 0, Math.min(arrayOfByte2.length, 65535));
/*     */   }
/*     */ 
/*     */   private void writeEND(long paramLong1, long paramLong2)
/*     */     throws IOException
/*     */   {
/* 524 */     int i = 0;
/* 525 */     long l1 = paramLong2;
/* 526 */     long l2 = paramLong1;
/* 527 */     if (l1 >= 4294967295L) {
/* 528 */       l1 = 4294967295L;
/* 529 */       i = 1;
/*     */     }
/* 531 */     if (l2 >= 4294967295L) {
/* 532 */       l2 = 4294967295L;
/* 533 */       i = 1;
/*     */     }
/* 535 */     int j = this.xentries.size();
/* 536 */     if (j >= 65535) {
/* 537 */       j = 65535;
/* 538 */       i = 1;
/*     */     }
/* 540 */     if (i != 0) {
/* 541 */       long l3 = this.written;
/*     */ 
/* 543 */       writeInt(101075792L);
/* 544 */       writeLong(44L);
/* 545 */       writeShort(45);
/* 546 */       writeShort(45);
/* 547 */       writeInt(0L);
/* 548 */       writeInt(0L);
/* 549 */       writeLong(this.xentries.size());
/* 550 */       writeLong(this.xentries.size());
/* 551 */       writeLong(paramLong2);
/* 552 */       writeLong(paramLong1);
/*     */ 
/* 555 */       writeInt(117853008L);
/* 556 */       writeInt(0L);
/* 557 */       writeLong(l3);
/* 558 */       writeInt(1L);
/*     */     }
/* 560 */     writeInt(101010256L);
/* 561 */     writeShort(0);
/* 562 */     writeShort(0);
/* 563 */     writeShort(j);
/* 564 */     writeShort(j);
/* 565 */     writeInt(l1);
/* 566 */     writeInt(l2);
/* 567 */     if (this.comment != null) {
/* 568 */       writeShort(this.comment.length);
/* 569 */       writeBytes(this.comment, 0, this.comment.length);
/*     */     } else {
/* 571 */       writeShort(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeShort(int paramInt)
/*     */     throws IOException
/*     */   {
/* 579 */     OutputStream localOutputStream = this.out;
/* 580 */     localOutputStream.write(paramInt >>> 0 & 0xFF);
/* 581 */     localOutputStream.write(paramInt >>> 8 & 0xFF);
/* 582 */     this.written += 2L;
/*     */   }
/*     */ 
/*     */   private void writeInt(long paramLong)
/*     */     throws IOException
/*     */   {
/* 589 */     OutputStream localOutputStream = this.out;
/* 590 */     localOutputStream.write((int)(paramLong >>> 0 & 0xFF));
/* 591 */     localOutputStream.write((int)(paramLong >>> 8 & 0xFF));
/* 592 */     localOutputStream.write((int)(paramLong >>> 16 & 0xFF));
/* 593 */     localOutputStream.write((int)(paramLong >>> 24 & 0xFF));
/* 594 */     this.written += 4L;
/*     */   }
/*     */ 
/*     */   private void writeLong(long paramLong)
/*     */     throws IOException
/*     */   {
/* 601 */     OutputStream localOutputStream = this.out;
/* 602 */     localOutputStream.write((int)(paramLong >>> 0 & 0xFF));
/* 603 */     localOutputStream.write((int)(paramLong >>> 8 & 0xFF));
/* 604 */     localOutputStream.write((int)(paramLong >>> 16 & 0xFF));
/* 605 */     localOutputStream.write((int)(paramLong >>> 24 & 0xFF));
/* 606 */     localOutputStream.write((int)(paramLong >>> 32 & 0xFF));
/* 607 */     localOutputStream.write((int)(paramLong >>> 40 & 0xFF));
/* 608 */     localOutputStream.write((int)(paramLong >>> 48 & 0xFF));
/* 609 */     localOutputStream.write((int)(paramLong >>> 56 & 0xFF));
/* 610 */     this.written += 8L;
/*     */   }
/*     */ 
/*     */   private void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 617 */     this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 618 */     this.written += paramInt2;
/*     */   }
/*     */ 
/*     */   private static class XEntry
/*     */   {
/*     */     public final ZipEntry entry;
/*     */     public final long offset;
/*     */ 
/*     */     public XEntry(ZipEntry paramZipEntry, long paramLong)
/*     */     {
/*  50 */       this.entry = paramZipEntry;
/*  51 */       this.offset = paramLong;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZipOutputStream
 * JD-Core Version:    0.6.2
 */