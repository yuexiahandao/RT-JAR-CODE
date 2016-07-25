/*     */ package javax.imageio.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UTFDataFormatException;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ public abstract class ImageOutputStreamImpl extends ImageInputStreamImpl
/*     */   implements ImageOutputStream
/*     */ {
/*     */   public abstract void write(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  51 */     write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public abstract void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   public void writeBoolean(boolean paramBoolean) throws IOException {
/*  57 */     write(paramBoolean ? 1 : 0);
/*     */   }
/*     */ 
/*     */   public void writeByte(int paramInt) throws IOException {
/*  61 */     write(paramInt);
/*     */   }
/*     */ 
/*     */   public void writeShort(int paramInt) throws IOException {
/*  65 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/*  66 */       this.byteBuf[0] = ((byte)(paramInt >>> 8));
/*  67 */       this.byteBuf[1] = ((byte)(paramInt >>> 0));
/*     */     } else {
/*  69 */       this.byteBuf[0] = ((byte)(paramInt >>> 0));
/*  70 */       this.byteBuf[1] = ((byte)(paramInt >>> 8));
/*     */     }
/*  72 */     write(this.byteBuf, 0, 2);
/*     */   }
/*     */ 
/*     */   public void writeChar(int paramInt) throws IOException {
/*  76 */     writeShort(paramInt);
/*     */   }
/*     */ 
/*     */   public void writeInt(int paramInt) throws IOException {
/*  80 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/*  81 */       this.byteBuf[0] = ((byte)(paramInt >>> 24));
/*  82 */       this.byteBuf[1] = ((byte)(paramInt >>> 16));
/*  83 */       this.byteBuf[2] = ((byte)(paramInt >>> 8));
/*  84 */       this.byteBuf[3] = ((byte)(paramInt >>> 0));
/*     */     } else {
/*  86 */       this.byteBuf[0] = ((byte)(paramInt >>> 0));
/*  87 */       this.byteBuf[1] = ((byte)(paramInt >>> 8));
/*  88 */       this.byteBuf[2] = ((byte)(paramInt >>> 16));
/*  89 */       this.byteBuf[3] = ((byte)(paramInt >>> 24));
/*     */     }
/*  91 */     write(this.byteBuf, 0, 4);
/*     */   }
/*     */ 
/*     */   public void writeLong(long paramLong) throws IOException {
/*  95 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
/*  96 */       this.byteBuf[0] = ((byte)(int)(paramLong >>> 56));
/*  97 */       this.byteBuf[1] = ((byte)(int)(paramLong >>> 48));
/*  98 */       this.byteBuf[2] = ((byte)(int)(paramLong >>> 40));
/*  99 */       this.byteBuf[3] = ((byte)(int)(paramLong >>> 32));
/* 100 */       this.byteBuf[4] = ((byte)(int)(paramLong >>> 24));
/* 101 */       this.byteBuf[5] = ((byte)(int)(paramLong >>> 16));
/* 102 */       this.byteBuf[6] = ((byte)(int)(paramLong >>> 8));
/* 103 */       this.byteBuf[7] = ((byte)(int)(paramLong >>> 0));
/*     */     } else {
/* 105 */       this.byteBuf[0] = ((byte)(int)(paramLong >>> 0));
/* 106 */       this.byteBuf[1] = ((byte)(int)(paramLong >>> 8));
/* 107 */       this.byteBuf[2] = ((byte)(int)(paramLong >>> 16));
/* 108 */       this.byteBuf[3] = ((byte)(int)(paramLong >>> 24));
/* 109 */       this.byteBuf[4] = ((byte)(int)(paramLong >>> 32));
/* 110 */       this.byteBuf[5] = ((byte)(int)(paramLong >>> 40));
/* 111 */       this.byteBuf[6] = ((byte)(int)(paramLong >>> 48));
/* 112 */       this.byteBuf[7] = ((byte)(int)(paramLong >>> 56));
/*     */     }
/*     */ 
/* 118 */     write(this.byteBuf, 0, 4);
/* 119 */     write(this.byteBuf, 4, 4);
/*     */   }
/*     */ 
/*     */   public void writeFloat(float paramFloat) throws IOException {
/* 123 */     writeInt(Float.floatToIntBits(paramFloat));
/*     */   }
/*     */ 
/*     */   public void writeDouble(double paramDouble) throws IOException {
/* 127 */     writeLong(Double.doubleToLongBits(paramDouble));
/*     */   }
/*     */ 
/*     */   public void writeBytes(String paramString) throws IOException {
/* 131 */     int i = paramString.length();
/* 132 */     for (int j = 0; j < i; j++)
/* 133 */       write((byte)paramString.charAt(j));
/*     */   }
/*     */ 
/*     */   public void writeChars(String paramString) throws IOException
/*     */   {
/* 138 */     int i = paramString.length();
/*     */ 
/* 140 */     byte[] arrayOfByte = new byte[i * 2];
/* 141 */     int j = 0;
/*     */     int k;
/*     */     int m;
/* 142 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 143 */       for (k = 0; k < i; k++) {
/* 144 */         m = paramString.charAt(k);
/* 145 */         arrayOfByte[(j++)] = ((byte)(m >>> 8));
/* 146 */         arrayOfByte[(j++)] = ((byte)(m >>> 0));
/*     */       }
/*     */     else {
/* 149 */       for (k = 0; k < i; k++) {
/* 150 */         m = paramString.charAt(k);
/* 151 */         arrayOfByte[(j++)] = ((byte)(m >>> 0));
/* 152 */         arrayOfByte[(j++)] = ((byte)(m >>> 8));
/*     */       }
/*     */     }
/*     */ 
/* 156 */     write(arrayOfByte, 0, i * 2);
/*     */   }
/*     */ 
/*     */   public void writeUTF(String paramString) throws IOException {
/* 160 */     int i = paramString.length();
/* 161 */     int j = 0;
/* 162 */     char[] arrayOfChar = new char[i];
/* 163 */     int m = 0;
/*     */ 
/* 165 */     paramString.getChars(0, i, arrayOfChar, 0);
/*     */     int k;
/* 167 */     for (int n = 0; n < i; n++) {
/* 168 */       k = arrayOfChar[n];
/* 169 */       if ((k >= 1) && (k <= 127))
/* 170 */         j++;
/* 171 */       else if (k > 2047)
/* 172 */         j += 3;
/*     */       else {
/* 174 */         j += 2;
/*     */       }
/*     */     }
/*     */ 
/* 178 */     if (j > 65535) {
/* 179 */       throw new UTFDataFormatException("utflen > 65536!");
/*     */     }
/*     */ 
/* 182 */     byte[] arrayOfByte = new byte[j + 2];
/* 183 */     arrayOfByte[(m++)] = ((byte)(j >>> 8 & 0xFF));
/* 184 */     arrayOfByte[(m++)] = ((byte)(j >>> 0 & 0xFF));
/* 185 */     for (int i1 = 0; i1 < i; i1++) {
/* 186 */       k = arrayOfChar[i1];
/* 187 */       if ((k >= 1) && (k <= 127)) {
/* 188 */         arrayOfByte[(m++)] = ((byte)k);
/* 189 */       } else if (k > 2047) {
/* 190 */         arrayOfByte[(m++)] = ((byte)(0xE0 | k >> 12 & 0xF));
/* 191 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 6 & 0x3F));
/* 192 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 0 & 0x3F));
/*     */       } else {
/* 194 */         arrayOfByte[(m++)] = ((byte)(0xC0 | k >> 6 & 0x1F));
/* 195 */         arrayOfByte[(m++)] = ((byte)(0x80 | k >> 0 & 0x3F));
/*     */       }
/*     */     }
/* 198 */     write(arrayOfByte, 0, j + 2);
/*     */   }
/*     */ 
/*     */   public void writeShorts(short[] paramArrayOfShort, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 203 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfShort.length) || (paramInt1 + paramInt2 < 0)) {
/* 204 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > s.length!");
/*     */     }
/*     */ 
/* 208 */     byte[] arrayOfByte = new byte[paramInt2 * 2];
/* 209 */     int i = 0;
/*     */     int j;
/*     */     int k;
/* 210 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 211 */       for (j = 0; j < paramInt2; j++) {
/* 212 */         k = paramArrayOfShort[(paramInt1 + j)];
/* 213 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 214 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/*     */       }
/*     */     else {
/* 217 */       for (j = 0; j < paramInt2; j++) {
/* 218 */         k = paramArrayOfShort[(paramInt1 + j)];
/* 219 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/* 220 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/*     */       }
/*     */     }
/*     */ 
/* 224 */     write(arrayOfByte, 0, paramInt2 * 2);
/*     */   }
/*     */ 
/*     */   public void writeChars(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 229 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 + paramInt2 < 0)) {
/* 230 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > c.length!");
/*     */     }
/*     */ 
/* 234 */     byte[] arrayOfByte = new byte[paramInt2 * 2];
/* 235 */     int i = 0;
/*     */     int j;
/*     */     int k;
/* 236 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 237 */       for (j = 0; j < paramInt2; j++) {
/* 238 */         k = paramArrayOfChar[(paramInt1 + j)];
/* 239 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 240 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/*     */       }
/*     */     else {
/* 243 */       for (j = 0; j < paramInt2; j++) {
/* 244 */         k = paramArrayOfChar[(paramInt1 + j)];
/* 245 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/* 246 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/*     */       }
/*     */     }
/*     */ 
/* 250 */     write(arrayOfByte, 0, paramInt2 * 2);
/*     */   }
/*     */ 
/*     */   public void writeInts(int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 255 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfInt.length) || (paramInt1 + paramInt2 < 0)) {
/* 256 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > i.length!");
/*     */     }
/*     */ 
/* 260 */     byte[] arrayOfByte = new byte[paramInt2 * 4];
/* 261 */     int i = 0;
/*     */     int j;
/*     */     int k;
/* 262 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 263 */       for (j = 0; j < paramInt2; j++) {
/* 264 */         k = paramArrayOfInt[(paramInt1 + j)];
/* 265 */         arrayOfByte[(i++)] = ((byte)(k >>> 24));
/* 266 */         arrayOfByte[(i++)] = ((byte)(k >>> 16));
/* 267 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 268 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/*     */       }
/*     */     else {
/* 271 */       for (j = 0; j < paramInt2; j++) {
/* 272 */         k = paramArrayOfInt[(paramInt1 + j)];
/* 273 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/* 274 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 275 */         arrayOfByte[(i++)] = ((byte)(k >>> 16));
/* 276 */         arrayOfByte[(i++)] = ((byte)(k >>> 24));
/*     */       }
/*     */     }
/*     */ 
/* 280 */     write(arrayOfByte, 0, paramInt2 * 4);
/*     */   }
/*     */ 
/*     */   public void writeLongs(long[] paramArrayOfLong, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 285 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfLong.length) || (paramInt1 + paramInt2 < 0)) {
/* 286 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > l.length!");
/*     */     }
/*     */ 
/* 290 */     byte[] arrayOfByte = new byte[paramInt2 * 8];
/* 291 */     int i = 0;
/*     */     int j;
/*     */     long l;
/* 292 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 293 */       for (j = 0; j < paramInt2; j++) {
/* 294 */         l = paramArrayOfLong[(paramInt1 + j)];
/* 295 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 56));
/* 296 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 48));
/* 297 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 40));
/* 298 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 32));
/* 299 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 24));
/* 300 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 16));
/* 301 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 8));
/* 302 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 0));
/*     */       }
/*     */     else {
/* 305 */       for (j = 0; j < paramInt2; j++) {
/* 306 */         l = paramArrayOfLong[(paramInt1 + j)];
/* 307 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 0));
/* 308 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 8));
/* 309 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 16));
/* 310 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 24));
/* 311 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 32));
/* 312 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 40));
/* 313 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 48));
/* 314 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 56));
/*     */       }
/*     */     }
/*     */ 
/* 318 */     write(arrayOfByte, 0, paramInt2 * 8);
/*     */   }
/*     */ 
/*     */   public void writeFloats(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 323 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfFloat.length) || (paramInt1 + paramInt2 < 0)) {
/* 324 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > f.length!");
/*     */     }
/*     */ 
/* 328 */     byte[] arrayOfByte = new byte[paramInt2 * 4];
/* 329 */     int i = 0;
/*     */     int j;
/*     */     int k;
/* 330 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 331 */       for (j = 0; j < paramInt2; j++) {
/* 332 */         k = Float.floatToIntBits(paramArrayOfFloat[(paramInt1 + j)]);
/* 333 */         arrayOfByte[(i++)] = ((byte)(k >>> 24));
/* 334 */         arrayOfByte[(i++)] = ((byte)(k >>> 16));
/* 335 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 336 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/*     */       }
/*     */     else {
/* 339 */       for (j = 0; j < paramInt2; j++) {
/* 340 */         k = Float.floatToIntBits(paramArrayOfFloat[(paramInt1 + j)]);
/* 341 */         arrayOfByte[(i++)] = ((byte)(k >>> 0));
/* 342 */         arrayOfByte[(i++)] = ((byte)(k >>> 8));
/* 343 */         arrayOfByte[(i++)] = ((byte)(k >>> 16));
/* 344 */         arrayOfByte[(i++)] = ((byte)(k >>> 24));
/*     */       }
/*     */     }
/*     */ 
/* 348 */     write(arrayOfByte, 0, paramInt2 * 4);
/*     */   }
/*     */ 
/*     */   public void writeDoubles(double[] paramArrayOfDouble, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 353 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfDouble.length) || (paramInt1 + paramInt2 < 0)) {
/* 354 */       throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > d.length!");
/*     */     }
/*     */ 
/* 358 */     byte[] arrayOfByte = new byte[paramInt2 * 8];
/* 359 */     int i = 0;
/*     */     int j;
/*     */     long l;
/* 360 */     if (this.byteOrder == ByteOrder.BIG_ENDIAN)
/* 361 */       for (j = 0; j < paramInt2; j++) {
/* 362 */         l = Double.doubleToLongBits(paramArrayOfDouble[(paramInt1 + j)]);
/* 363 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 56));
/* 364 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 48));
/* 365 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 40));
/* 366 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 32));
/* 367 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 24));
/* 368 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 16));
/* 369 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 8));
/* 370 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 0));
/*     */       }
/*     */     else {
/* 373 */       for (j = 0; j < paramInt2; j++) {
/* 374 */         l = Double.doubleToLongBits(paramArrayOfDouble[(paramInt1 + j)]);
/* 375 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 0));
/* 376 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 8));
/* 377 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 16));
/* 378 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 24));
/* 379 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 32));
/* 380 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 40));
/* 381 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 48));
/* 382 */         arrayOfByte[(i++)] = ((byte)(int)(l >>> 56));
/*     */       }
/*     */     }
/*     */ 
/* 386 */     write(arrayOfByte, 0, paramInt2 * 8);
/*     */   }
/*     */ 
/*     */   public void writeBit(int paramInt) throws IOException {
/* 390 */     writeBits(1L & paramInt, 1);
/*     */   }
/*     */ 
/*     */   public void writeBits(long paramLong, int paramInt) throws IOException {
/* 394 */     checkClosed();
/*     */ 
/* 396 */     if ((paramInt < 0) || (paramInt > 64)) {
/* 397 */       throw new IllegalArgumentException("Bad value for numBits!");
/*     */     }
/* 399 */     if (paramInt == 0)
/*     */       return;
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 407 */     if ((getStreamPosition() > 0L) || (this.bitOffset > 0)) {
/* 408 */       i = this.bitOffset;
/* 409 */       j = read();
/* 410 */       if (j != -1)
/* 411 */         seek(getStreamPosition() - 1L);
/*     */       else {
/* 413 */         j = 0;
/*     */       }
/*     */ 
/* 416 */       if (paramInt + i < 8)
/*     */       {
/* 418 */         k = 8 - (i + paramInt);
/* 419 */         m = -1 >>> 32 - paramInt;
/* 420 */         j &= (m << k ^ 0xFFFFFFFF);
/* 421 */         j = (int)(j | (paramLong & m) << k);
/* 422 */         write(j);
/* 423 */         seek(getStreamPosition() - 1L);
/* 424 */         this.bitOffset = (i + paramInt);
/* 425 */         paramInt = 0;
/*     */       }
/*     */       else {
/* 428 */         k = 8 - i;
/* 429 */         m = -1 >>> 32 - k;
/* 430 */         j &= (m ^ 0xFFFFFFFF);
/* 431 */         j = (int)(j | paramLong >> paramInt - k & m);
/*     */ 
/* 434 */         write(j);
/* 435 */         paramInt -= k;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 440 */     if (paramInt > 7) {
/* 441 */       i = paramInt % 8;
/* 442 */       for (j = paramInt / 8; j > 0; j--) {
/* 443 */         k = (j - 1) * 8 + i;
/* 444 */         m = (int)(k == 0 ? paramLong & 0xFF : paramLong >> k & 0xFF);
/*     */ 
/* 447 */         write(m);
/*     */       }
/* 449 */       paramInt = i;
/*     */     }
/*     */ 
/* 455 */     if (paramInt != 0)
/*     */     {
/* 458 */       i = 0;
/* 459 */       i = read();
/* 460 */       if (i != -1) {
/* 461 */         seek(getStreamPosition() - 1L);
/*     */       }
/*     */       else
/*     */       {
/* 466 */         i = 0;
/*     */       }
/*     */ 
/* 469 */       j = 8 - paramInt;
/* 470 */       k = -1 >>> 32 - paramInt;
/* 471 */       i &= (k << j ^ 0xFFFFFFFF);
/* 472 */       i = (int)(i | (paramLong & k) << j);
/*     */ 
/* 474 */       write(i);
/* 475 */       seek(getStreamPosition() - 1L);
/* 476 */       this.bitOffset = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void flushBits()
/*     */     throws IOException
/*     */   {
/* 490 */     checkClosed();
/* 491 */     if (this.bitOffset != 0) {
/* 492 */       int i = this.bitOffset;
/* 493 */       int j = read();
/* 494 */       if (j < 0)
/*     */       {
/* 499 */         j = 0;
/* 500 */         this.bitOffset = 0;
/*     */       }
/*     */       else {
/* 503 */         seek(getStreamPosition() - 1L);
/* 504 */         j &= -1 << 8 - i;
/*     */       }
/* 506 */       write(j);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.ImageOutputStreamImpl
 * JD-Core Version:    0.6.2
 */