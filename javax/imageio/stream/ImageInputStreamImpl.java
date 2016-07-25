/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Stack;
/*     */ import javax.imageio.IIOException;
/*     */ 
/*     */ public abstract class ImageInputStreamImpl
/*     */   implements ImageInputStream
/*     */ {
/*  46 */   private Stack markByteStack = new Stack();
/*     */ 
/*  48 */   private Stack markBitStack = new Stack();
/*     */ 
/*  50 */   private boolean isClosed = false;
/*     */   private static final int BYTE_BUF_LENGTH = 8192;
/*  62 */   byte[] byteBuf = new byte[8192];
/*     */ 
/*  72 */   protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
/*     */   protected long streamPos;
/*     */   protected int bitOffset;
/*  93 */   protected long flushedPos = 0L;
/*     */ 
/*     */   protected final void checkClosed()
/*     */     throws IOException
/*     */   {
/* 109 */     if (this.isClosed)
/* 110 */       throw new IOException("closed");
/*     */   }
/*     */ 
/*     */   public void setByteOrder(ByteOrder paramByteOrder)
/*     */   {
/* 115 */     this.byteOrder = paramByteOrder;
/*     */   }
/*     */ 
/*     */   public ByteOrder getByteOrder() {
/* 119 */     return this.byteOrder;
/*     */   }
/*     */ 
/*     */   public abstract int read()
/*     */     throws IOException;
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 155 */     return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public void readBytes(IIOByteBuffer paramIIOByteBuffer, int paramInt)
/*     */     throws IOException
/*     */   {
/* 188 */     if (paramInt < 0) {
/* 189 */       throw new IndexOutOfBoundsException("len < 0!");
/*     */     }
/* 191 */     if (paramIIOByteBuffer == null) {
/* 192 */       throw new NullPointerException("buf == null!");
/*     */     }
/*     */ 
/* 195 */     byte[] arrayOfByte = new byte[paramInt];
/* 196 */     paramInt = read(arrayOfByte, 0, paramInt);
/*     */ 
/* 198 */     paramIIOByteBuffer.setData(arrayOfByte);
/* 199 */     paramIIOByteBuffer.setOffset(0);
/* 200 */     paramIIOByteBuffer.setLength(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean readBoolean() throws IOException {
/* 204 */     int i = read();
/* 205 */     if (i < 0) {
/* 206 */       throw new EOFException();
/*     */     }
/* 208 */     return i != 0;
/*     */   }
/*     */ 
/*     */   public byte readByte() throws IOException {
/* 212 */     int i = read();
/* 213 */     if (i < 0) {
/* 214 */       throw new EOFException();
/*     */     }
/* 216 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public int readUnsignedByte() throws IOException {
/* 220 */     int i = read();
/* 221 */     if (i < 0) {
/* 222 */       throw new EOFException();
/*     */     }
/* 224 */     return i;
/*     */   }
/*     */ 
/*     */   public short readShort() throws IOException {
/* 228 */     if (read(this.byteBuf, 0, 2) < 0) {
/* 229 */       throw new EOFException();
/*     */     }
/*     */ 
/* 232 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/* 233 */       return (short)((this.byteBuf[0] & 0xFF) << 8 | (this.byteBuf[1] & 0xFF) << 0);
/*     */     }
/*     */ 
/* 236 */     return (short)((this.byteBuf[1] & 0xFF) << 8 | (this.byteBuf[0] & 0xFF) << 0);
/*     */   }
/*     */ 
/*     */   public int readUnsignedShort()
/*     */     throws IOException
/*     */   {
/* 242 */     return readShort() & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public char readChar() throws IOException {
/* 246 */     return (char)readShort();
/*     */   }
/*     */ 
/*     */   public int readInt() throws IOException {
/* 250 */     if (read(this.byteBuf, 0, 4) < 0) {
/* 251 */       throw new EOFException();
/*     */     }
/*     */ 
/* 254 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/* 255 */       return (this.byteBuf[0] & 0xFF) << 24 | (this.byteBuf[1] & 0xFF) << 16 | (this.byteBuf[2] & 0xFF) << 8 | (this.byteBuf[3] & 0xFF) << 0;
/*     */     }
/*     */ 
/* 259 */     return (this.byteBuf[3] & 0xFF) << 24 | (this.byteBuf[2] & 0xFF) << 16 | (this.byteBuf[1] & 0xFF) << 8 | (this.byteBuf[0] & 0xFF) << 0;
/*     */   }
/*     */ 
/*     */   public long readUnsignedInt()
/*     */     throws IOException
/*     */   {
/* 266 */     return readInt() & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   public long readLong()
/*     */     throws IOException
/*     */   {
/* 273 */     int i = readInt();
/* 274 */     int j = readInt();
/*     */ 
/* 276 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/* 277 */       return (i << 32) + (j & 0xFFFFFFFF);
/*     */     }
/* 279 */     return (j << 32) + (i & 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public float readFloat() throws IOException
/*     */   {
/* 284 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   public double readDouble() throws IOException {
/* 288 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   public String readLine() throws IOException {
/* 292 */     StringBuffer localStringBuffer = new StringBuffer();
/* 293 */     int i = -1;
/* 294 */     int j = 0;
/*     */ 
/* 296 */     while (j == 0) {
/* 297 */       switch (i = read()) {
/*     */       case -1:
/*     */       case 10:
/* 300 */         j = 1;
/* 301 */         break;
/*     */       case 13:
/* 303 */         j = 1;
/* 304 */         long l = getStreamPosition();
/* 305 */         if (read() != 10)
/* 306 */           seek(l); break;
/*     */       default:
/* 310 */         localStringBuffer.append((char)i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 315 */     if ((i == -1) && (localStringBuffer.length() == 0)) {
/* 316 */       return null;
/*     */     }
/* 318 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String readUTF() throws IOException {
/* 322 */     this.bitOffset = 0;
/*     */ 
/* 327 */     ByteOrder localByteOrder = getByteOrder();
/* 328 */     setByteOrder(ByteOrder.BIG_ENDIAN);
/*     */     String str;
/*     */     try
/*     */     {
/* 332 */       str = DataInputStream.readUTF(this);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 335 */       setByteOrder(localByteOrder);
/* 336 */       throw localIOException;
/*     */     }
/*     */ 
/* 339 */     setByteOrder(localByteOrder);
/* 340 */     return str;
/*     */   }
/*     */ 
/*     */   public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 345 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
/* 346 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
/*     */     }
/*     */ 
/* 350 */     while (paramInt2 > 0) {
/* 351 */       int i = read(paramArrayOfByte, paramInt1, paramInt2);
/* 352 */       if (i == -1) {
/* 353 */         throw new EOFException();
/*     */       }
/* 355 */       paramInt1 += i;
/* 356 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(byte[] paramArrayOfByte) throws IOException {
/* 361 */     readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void readFully(short[] paramArrayOfShort, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 366 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfShort.length) || (paramInt1 + paramInt2 < 0)) {
/* 367 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > s.length!");
/*     */     }
/*     */ 
/* 371 */     while (paramInt2 > 0) {
/* 372 */       int i = Math.min(paramInt2, this.byteBuf.length / 2);
/* 373 */       readFully(this.byteBuf, 0, i * 2);
/* 374 */       toShorts(this.byteBuf, paramArrayOfShort, paramInt1, i);
/* 375 */       paramInt1 += i;
/* 376 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 382 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0)) {
/* 383 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > c.length!");
/*     */     }
/*     */ 
/* 387 */     while (paramInt2 > 0) {
/* 388 */       int i = Math.min(paramInt2, this.byteBuf.length / 2);
/* 389 */       readFully(this.byteBuf, 0, i * 2);
/* 390 */       toChars(this.byteBuf, paramArrayOfChar, paramInt1, i);
/* 391 */       paramInt1 += i;
/* 392 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 398 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfInt.length) || (paramInt1 + paramInt2 < 0)) {
/* 399 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > i.length!");
/*     */     }
/*     */ 
/* 403 */     while (paramInt2 > 0) {
/* 404 */       int i = Math.min(paramInt2, this.byteBuf.length / 4);
/* 405 */       readFully(this.byteBuf, 0, i * 4);
/* 406 */       toInts(this.byteBuf, paramArrayOfInt, paramInt1, i);
/* 407 */       paramInt1 += i;
/* 408 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(long[] paramArrayOfLong, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 414 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfLong.length) || (paramInt1 + paramInt2 < 0)) {
/* 415 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > l.length!");
/*     */     }
/*     */ 
/* 419 */     while (paramInt2 > 0) {
/* 420 */       int i = Math.min(paramInt2, this.byteBuf.length / 8);
/* 421 */       readFully(this.byteBuf, 0, i * 8);
/* 422 */       toLongs(this.byteBuf, paramArrayOfLong, paramInt1, i);
/* 423 */       paramInt1 += i;
/* 424 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 430 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfFloat.length) || (paramInt1 + paramInt2 < 0)) {
/* 431 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > f.length!");
/*     */     }
/*     */ 
/* 435 */     while (paramInt2 > 0) {
/* 436 */       int i = Math.min(paramInt2, this.byteBuf.length / 4);
/* 437 */       readFully(this.byteBuf, 0, i * 4);
/* 438 */       toFloats(this.byteBuf, paramArrayOfFloat, paramInt1, i);
/* 439 */       paramInt1 += i;
/* 440 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFully(double[] paramArrayOfDouble, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 446 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfDouble.length) || (paramInt1 + paramInt2 < 0)) {
/* 447 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > d.length!");
/*     */     }
/*     */ 
/* 451 */     while (paramInt2 > 0) {
/* 452 */       int i = Math.min(paramInt2, this.byteBuf.length / 8);
/* 453 */       readFully(this.byteBuf, 0, i * 8);
/* 454 */       toDoubles(this.byteBuf, paramArrayOfDouble, paramInt1, i);
/* 455 */       paramInt1 += i;
/* 456 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void toShorts(byte[] paramArrayOfByte, short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 461 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 462 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 463 */       for (j = 0; j < paramInt2; j++) {
/* 464 */         k = paramArrayOfByte[i];
/* 465 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 466 */         paramArrayOfShort[(paramInt1 + j)] = ((short)(k << 8 | m));
/* 467 */         i += 2;
/*     */       }
/*     */     else
/* 470 */       for (j = 0; j < paramInt2; j++) {
/* 471 */         k = paramArrayOfByte[(i + 1)];
/* 472 */         m = paramArrayOfByte[i] & 0xFF;
/* 473 */         paramArrayOfShort[(paramInt1 + j)] = ((short)(k << 8 | m));
/* 474 */         i += 2;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void toChars(byte[] paramArrayOfByte, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 480 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 481 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 482 */       for (j = 0; j < paramInt2; j++) {
/* 483 */         k = paramArrayOfByte[i];
/* 484 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 485 */         paramArrayOfChar[(paramInt1 + j)] = ((char)(k << 8 | m));
/* 486 */         i += 2;
/*     */       }
/*     */     else
/* 489 */       for (j = 0; j < paramInt2; j++) {
/* 490 */         k = paramArrayOfByte[(i + 1)];
/* 491 */         m = paramArrayOfByte[i] & 0xFF;
/* 492 */         paramArrayOfChar[(paramInt1 + j)] = ((char)(k << 8 | m));
/* 493 */         i += 2;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void toInts(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 499 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/* 500 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 501 */       for (j = 0; j < paramInt2; j++) {
/* 502 */         k = paramArrayOfByte[i];
/* 503 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 504 */         n = paramArrayOfByte[(i + 2)] & 0xFF;
/* 505 */         i1 = paramArrayOfByte[(i + 3)] & 0xFF;
/* 506 */         paramArrayOfInt[(paramInt1 + j)] = (k << 24 | m << 16 | n << 8 | i1);
/* 507 */         i += 4;
/*     */       }
/*     */     else
/* 510 */       for (j = 0; j < paramInt2; j++) {
/* 511 */         k = paramArrayOfByte[(i + 3)];
/* 512 */         m = paramArrayOfByte[(i + 2)] & 0xFF;
/* 513 */         n = paramArrayOfByte[(i + 1)] & 0xFF;
/* 514 */         i1 = paramArrayOfByte[i] & 0xFF;
/* 515 */         paramArrayOfInt[(paramInt1 + j)] = (k << 24 | m << 16 | n << 8 | i1);
/* 516 */         i += 4;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void toLongs(byte[] paramArrayOfByte, long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 522 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/*     */     int i4;
/*     */     int i5;
/*     */     int i6;
/*     */     int i7;
/* 523 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 524 */       for (j = 0; j < paramInt2; j++) {
/* 525 */         k = paramArrayOfByte[i];
/* 526 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 527 */         n = paramArrayOfByte[(i + 2)] & 0xFF;
/* 528 */         i1 = paramArrayOfByte[(i + 3)] & 0xFF;
/* 529 */         i2 = paramArrayOfByte[(i + 4)];
/* 530 */         i3 = paramArrayOfByte[(i + 5)] & 0xFF;
/* 531 */         i4 = paramArrayOfByte[(i + 6)] & 0xFF;
/* 532 */         i5 = paramArrayOfByte[(i + 7)] & 0xFF;
/*     */ 
/* 534 */         i6 = k << 24 | m << 16 | n << 8 | i1;
/* 535 */         i7 = i2 << 24 | i3 << 16 | i4 << 8 | i5;
/*     */ 
/* 537 */         paramArrayOfLong[(paramInt1 + j)] = (i6 << 32 | i7 & 0xFFFFFFFF);
/* 538 */         i += 8;
/*     */       }
/*     */     else
/* 541 */       for (j = 0; j < paramInt2; j++) {
/* 542 */         k = paramArrayOfByte[(i + 7)];
/* 543 */         m = paramArrayOfByte[(i + 6)] & 0xFF;
/* 544 */         n = paramArrayOfByte[(i + 5)] & 0xFF;
/* 545 */         i1 = paramArrayOfByte[(i + 4)] & 0xFF;
/* 546 */         i2 = paramArrayOfByte[(i + 3)];
/* 547 */         i3 = paramArrayOfByte[(i + 2)] & 0xFF;
/* 548 */         i4 = paramArrayOfByte[(i + 1)] & 0xFF;
/* 549 */         i5 = paramArrayOfByte[i] & 0xFF;
/*     */ 
/* 551 */         i6 = k << 24 | m << 16 | n << 8 | i1;
/* 552 */         i7 = i2 << 24 | i3 << 16 | i4 << 8 | i5;
/*     */ 
/* 554 */         paramArrayOfLong[(paramInt1 + j)] = (i6 << 32 | i7 & 0xFFFFFFFF);
/* 555 */         i += 8;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void toFloats(byte[] paramArrayOfByte, float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 561 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/*     */     int i2;
/* 562 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 563 */       for (j = 0; j < paramInt2; j++) {
/* 564 */         k = paramArrayOfByte[i];
/* 565 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 566 */         n = paramArrayOfByte[(i + 2)] & 0xFF;
/* 567 */         i1 = paramArrayOfByte[(i + 3)] & 0xFF;
/* 568 */         i2 = k << 24 | m << 16 | n << 8 | i1;
/* 569 */         paramArrayOfFloat[(paramInt1 + j)] = Float.intBitsToFloat(i2);
/* 570 */         i += 4;
/*     */       }
/*     */     else
/* 573 */       for (j = 0; j < paramInt2; j++) {
/* 574 */         k = paramArrayOfByte[(i + 3)];
/* 575 */         m = paramArrayOfByte[(i + 2)] & 0xFF;
/* 576 */         n = paramArrayOfByte[(i + 1)] & 0xFF;
/* 577 */         i1 = paramArrayOfByte[(i + 0)] & 0xFF;
/* 578 */         i2 = k << 24 | m << 16 | n << 8 | i1;
/* 579 */         paramArrayOfFloat[(paramInt1 + j)] = Float.intBitsToFloat(i2);
/* 580 */         i += 4;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void toDoubles(byte[] paramArrayOfByte, double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 586 */     int i = 0;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/*     */     int i4;
/*     */     int i5;
/*     */     int i6;
/*     */     int i7;
/*     */     long l;
/* 587 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 588 */       for (j = 0; j < paramInt2; j++) {
/* 589 */         k = paramArrayOfByte[i];
/* 590 */         m = paramArrayOfByte[(i + 1)] & 0xFF;
/* 591 */         n = paramArrayOfByte[(i + 2)] & 0xFF;
/* 592 */         i1 = paramArrayOfByte[(i + 3)] & 0xFF;
/* 593 */         i2 = paramArrayOfByte[(i + 4)];
/* 594 */         i3 = paramArrayOfByte[(i + 5)] & 0xFF;
/* 595 */         i4 = paramArrayOfByte[(i + 6)] & 0xFF;
/* 596 */         i5 = paramArrayOfByte[(i + 7)] & 0xFF;
/*     */ 
/* 598 */         i6 = k << 24 | m << 16 | n << 8 | i1;
/* 599 */         i7 = i2 << 24 | i3 << 16 | i4 << 8 | i5;
/* 600 */         l = i6 << 32 | i7 & 0xFFFFFFFF;
/*     */ 
/* 602 */         paramArrayOfDouble[(paramInt1 + j)] = Double.longBitsToDouble(l);
/* 603 */         i += 8;
/*     */       }
/*     */     else
/* 606 */       for (j = 0; j < paramInt2; j++) {
/* 607 */         k = paramArrayOfByte[(i + 7)];
/* 608 */         m = paramArrayOfByte[(i + 6)] & 0xFF;
/* 609 */         n = paramArrayOfByte[(i + 5)] & 0xFF;
/* 610 */         i1 = paramArrayOfByte[(i + 4)] & 0xFF;
/* 611 */         i2 = paramArrayOfByte[(i + 3)];
/* 612 */         i3 = paramArrayOfByte[(i + 2)] & 0xFF;
/* 613 */         i4 = paramArrayOfByte[(i + 1)] & 0xFF;
/* 614 */         i5 = paramArrayOfByte[i] & 0xFF;
/*     */ 
/* 616 */         i6 = k << 24 | m << 16 | n << 8 | i1;
/* 617 */         i7 = i2 << 24 | i3 << 16 | i4 << 8 | i5;
/* 618 */         l = i6 << 32 | i7 & 0xFFFFFFFF;
/*     */ 
/* 620 */         paramArrayOfDouble[(paramInt1 + j)] = Double.longBitsToDouble(l);
/* 621 */         i += 8;
/*     */       }
/*     */   }
/*     */ 
/*     */   public long getStreamPosition() throws IOException
/*     */   {
/* 627 */     checkClosed();
/* 628 */     return this.streamPos;
/*     */   }
/*     */ 
/*     */   public int getBitOffset() throws IOException {
/* 632 */     checkClosed();
/* 633 */     return this.bitOffset;
/*     */   }
/*     */ 
/*     */   public void setBitOffset(int paramInt) throws IOException {
/* 637 */     checkClosed();
/* 638 */     if ((paramInt < 0) || (paramInt > 7)) {
/* 639 */       throw new IllegalArgumentException("bitOffset must be betwwen 0 and 7!");
/*     */     }
/* 641 */     this.bitOffset = paramInt;
/*     */   }
/*     */ 
/*     */   public int readBit() throws IOException {
/* 645 */     checkClosed();
/*     */ 
/* 648 */     int i = this.bitOffset + 1 & 0x7;
/*     */ 
/* 650 */     int j = read();
/* 651 */     if (j == -1) {
/* 652 */       throw new EOFException();
/*     */     }
/*     */ 
/* 655 */     if (i != 0)
/*     */     {
/* 657 */       seek(getStreamPosition() - 1L);
/*     */ 
/* 659 */       j >>= 8 - i;
/*     */     }
/* 661 */     this.bitOffset = i;
/*     */ 
/* 663 */     return j & 0x1;
/*     */   }
/*     */ 
/*     */   public long readBits(int paramInt) throws IOException {
/* 667 */     checkClosed();
/*     */ 
/* 669 */     if ((paramInt < 0) || (paramInt > 64)) {
/* 670 */       throw new IllegalArgumentException();
/*     */     }
/* 672 */     if (paramInt == 0) {
/* 673 */       return 0L;
/*     */     }
/*     */ 
/* 677 */     int i = paramInt + this.bitOffset;
/*     */ 
/* 680 */     int j = this.bitOffset + paramInt & 0x7;
/*     */ 
/* 683 */     long l = 0L;
/* 684 */     while (i > 0) {
/* 685 */       int k = read();
/* 686 */       if (k == -1) {
/* 687 */         throw new EOFException();
/*     */       }
/*     */ 
/* 690 */       l <<= 8;
/* 691 */       l |= k;
/* 692 */       i -= 8;
/*     */     }
/*     */ 
/* 696 */     if (j != 0) {
/* 697 */       seek(getStreamPosition() - 1L);
/*     */     }
/* 699 */     this.bitOffset = j;
/*     */ 
/* 702 */     l >>>= -i;
/*     */ 
/* 705 */     l &= -1L >>> 64 - paramInt;
/*     */ 
/* 707 */     return l;
/*     */   }
/*     */ 
/*     */   public long length()
/*     */   {
/* 718 */     return -1L;
/*     */   }
/*     */ 
/*     */   public int skipBytes(int paramInt)
/*     */     throws IOException
/*     */   {
/* 737 */     long l = getStreamPosition();
/* 738 */     seek(l + paramInt);
/* 739 */     return (int)(getStreamPosition() - l);
/*     */   }
/*     */ 
/*     */   public long skipBytes(long paramLong)
/*     */     throws IOException
/*     */   {
/* 758 */     long l = getStreamPosition();
/* 759 */     seek(l + paramLong);
/* 760 */     return getStreamPosition() - l;
/*     */   }
/*     */ 
/*     */   public void seek(long paramLong) throws IOException {
/* 764 */     checkClosed();
/*     */ 
/* 767 */     if (paramLong < this.flushedPos) {
/* 768 */       throw new IndexOutOfBoundsException("pos < flushedPos!");
/*     */     }
/*     */ 
/* 771 */     this.streamPos = paramLong;
/* 772 */     this.bitOffset = 0;
/*     */   }
/*     */ 
/*     */   public void mark()
/*     */   {
/*     */     try
/*     */     {
/* 781 */       this.markByteStack.push(Long.valueOf(getStreamPosition()));
/* 782 */       this.markBitStack.push(Integer.valueOf(getBitOffset()));
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 797 */     if (this.markByteStack.empty()) {
/* 798 */       return;
/*     */     }
/*     */ 
/* 801 */     long l = ((Long)this.markByteStack.pop()).longValue();
/* 802 */     if (l < this.flushedPos) {
/* 803 */       throw new IIOException("Previous marked position has been discarded!");
/*     */     }
/*     */ 
/* 806 */     seek(l);
/*     */ 
/* 808 */     int i = ((Integer)this.markBitStack.pop()).intValue();
/* 809 */     setBitOffset(i);
/*     */   }
/*     */ 
/*     */   public void flushBefore(long paramLong) throws IOException {
/* 813 */     checkClosed();
/* 814 */     if (paramLong < this.flushedPos) {
/* 815 */       throw new IndexOutOfBoundsException("pos < flushedPos!");
/*     */     }
/* 817 */     if (paramLong > getStreamPosition()) {
/* 818 */       throw new IndexOutOfBoundsException("pos > getStreamPosition()!");
/*     */     }
/*     */ 
/* 821 */     this.flushedPos = paramLong;
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException {
/* 825 */     flushBefore(getStreamPosition());
/*     */   }
/*     */ 
/*     */   public long getFlushedPosition() {
/* 829 */     return this.flushedPos;
/*     */   }
/*     */ 
/*     */   public boolean isCached()
/*     */   {
/* 837 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCachedMemory()
/*     */   {
/* 845 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCachedFile()
/*     */   {
/* 853 */     return false;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 857 */     checkClosed();
/*     */ 
/* 859 */     this.isClosed = true;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 872 */     if (!this.isClosed)
/*     */       try {
/* 874 */         close();
/*     */       }
/*     */       catch (IOException localIOException) {
/*     */       }
/* 878 */     super.finalize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.ImageInputStreamImpl
 * JD-Core Version:    0.6.2
 */