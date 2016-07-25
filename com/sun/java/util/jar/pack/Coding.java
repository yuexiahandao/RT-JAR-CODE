/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ class Coding
/*     */   implements Comparable, CodingMethod, Histogram.BitMetric
/*     */ {
/*     */   public static final int B_MAX = 5;
/*     */   public static final int H_MAX = 256;
/*     */   public static final int S_MAX = 2;
/*     */   private final int B;
/*     */   private final int H;
/*     */   private final int L;
/*     */   private final int S;
/*     */   private final int del;
/*     */   private final int min;
/*     */   private final int max;
/*     */   private final int umin;
/*     */   private final int umax;
/*     */   private final int[] byteMin;
/*     */   private final int[] byteMax;
/*     */   private static Map<Coding, Coding> codeMap;
/*     */   private static final byte[] byteBitWidths;
/* 883 */   static boolean verboseStringForDebug = false;
/*     */ 
/*     */   private static int saturate32(long paramLong)
/*     */   {
/* 158 */     if (paramLong > 2147483647L) return 2147483647;
/* 159 */     if (paramLong < -2147483648L) return -2147483648;
/* 160 */     return (int)paramLong;
/*     */   }
/*     */   private static long codeRangeLong(int paramInt1, int paramInt2) {
/* 163 */     return codeRangeLong(paramInt1, paramInt2, paramInt1);
/*     */   }
/*     */ 
/*     */   private static long codeRangeLong(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 169 */     assert ((paramInt3 >= 0) && (paramInt3 <= paramInt1));
/* 170 */     assert ((paramInt1 >= 1) && (paramInt1 <= 5));
/* 171 */     assert ((paramInt2 >= 1) && (paramInt2 <= 256));
/* 172 */     if (paramInt3 == 0) return 0L;
/* 173 */     if (paramInt1 == 1) return paramInt2;
/* 174 */     int i = 256 - paramInt2;
/* 175 */     long l1 = 0L;
/* 176 */     long l2 = 1L;
/* 177 */     for (int j = 1; j <= paramInt3; j++) {
/* 178 */       l1 += l2;
/* 179 */       l2 *= paramInt2;
/*     */     }
/* 181 */     l1 *= i;
/* 182 */     if (paramInt3 == paramInt1)
/* 183 */       l1 += l2;
/* 184 */     return l1;
/*     */   }
/*     */ 
/*     */   public static int codeMax(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 189 */     long l1 = codeRangeLong(paramInt1, paramInt2, paramInt4);
/* 190 */     if (l1 == 0L)
/* 191 */       return -1;
/* 192 */     if ((paramInt3 == 0) || (l1 >= 4294967296L))
/* 193 */       return saturate32(l1 - 1L);
/* 194 */     long l2 = l1 - 1L;
/* 195 */     while (isNegativeCode(l2, paramInt3)) {
/* 196 */       l2 -= 1L;
/*     */     }
/* 198 */     if (l2 < 0L) return -1;
/* 199 */     int i = decodeSign32(l2, paramInt3);
/*     */ 
/* 201 */     if (i < 0)
/* 202 */       return 2147483647;
/* 203 */     return i;
/*     */   }
/*     */ 
/*     */   public static int codeMin(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 211 */     long l1 = codeRangeLong(paramInt1, paramInt2, paramInt4);
/* 212 */     if ((l1 >= 4294967296L) && (paramInt4 == paramInt1))
/*     */     {
/* 214 */       return -2147483648;
/*     */     }
/* 216 */     if (paramInt3 == 0) {
/* 217 */       return 0;
/*     */     }
/* 219 */     long l2 = l1 - 1L;
/* 220 */     while (!isNegativeCode(l2, paramInt3)) {
/* 221 */       l2 -= 1L;
/*     */     }
/* 223 */     if (l2 < 0L) return 0;
/* 224 */     return decodeSign32(l2, paramInt3);
/*     */   }
/*     */ 
/*     */   private static long toUnsigned32(int paramInt)
/*     */   {
/* 232 */     return paramInt << 32 >>> 32;
/*     */   }
/*     */ 
/*     */   private static boolean isNegativeCode(long paramLong, int paramInt)
/*     */   {
/* 237 */     assert (paramInt > 0);
/* 238 */     assert (paramLong >= -1L);
/* 239 */     int i = (1 << paramInt) - 1;
/* 240 */     return ((int)paramLong + 1 & i) == 0;
/*     */   }
/*     */   private static boolean hasNegativeCode(int paramInt1, int paramInt2) {
/* 243 */     assert (paramInt2 > 0);
/*     */ 
/* 250 */     return (0 > paramInt1) && (paramInt1 >= (-1 >>> paramInt2 ^ 0xFFFFFFFF));
/*     */   }
/*     */ 
/*     */   private static int decodeSign32(long paramLong, int paramInt) {
/* 254 */     assert (paramLong == toUnsigned32((int)paramLong)) : Long.toHexString(paramLong);
/* 255 */     if (paramInt == 0)
/* 256 */       return (int)paramLong;
/*     */     int i;
/* 259 */     if (isNegativeCode(paramLong, paramInt))
/*     */     {
/* 261 */       i = (int)paramLong >>> paramInt ^ 0xFFFFFFFF;
/*     */     }
/*     */     else {
/* 264 */       i = (int)paramLong - ((int)paramLong >>> paramInt);
/*     */     }
/*     */ 
/* 267 */     assert ((paramInt != 1) || (i == ((int)paramLong >>> 1 ^ -((int)paramLong & 0x1))));
/* 268 */     return i;
/*     */   }
/*     */   private static long encodeSign32(int paramInt1, int paramInt2) {
/* 271 */     if (paramInt2 == 0) {
/* 272 */       return toUnsigned32(paramInt1);
/*     */     }
/* 274 */     int i = (1 << paramInt2) - 1;
/*     */ 
/* 276 */     if (!hasNegativeCode(paramInt1, paramInt2))
/*     */     {
/* 278 */       l = paramInt1 + toUnsigned32(paramInt1) / i;
/*     */     }
/*     */     else {
/* 281 */       l = (-paramInt1 << paramInt2) - 1;
/*     */     }
/* 283 */     long l = toUnsigned32((int)l);
/*     */ 
/* 285 */     assert (paramInt1 == decodeSign32(l, paramInt2)) : (Long.toHexString(l) + " -> " + Integer.toHexString(paramInt1) + " != " + Integer.toHexString(decodeSign32(l, paramInt2)));
/*     */ 
/* 288 */     return l;
/*     */   }
/*     */ 
/*     */   public static void writeInt(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 293 */     long l1 = encodeSign32(paramInt1, paramInt4);
/* 294 */     assert (l1 == toUnsigned32((int)l1));
/*     */ 
/* 296 */     assert (l1 < codeRangeLong(paramInt2, paramInt3)) : Long.toHexString(l1);
/* 297 */     int i = 256 - paramInt3;
/* 298 */     long l2 = l1;
/* 299 */     int j = paramArrayOfInt[0];
/* 300 */     for (int k = 0; (k < paramInt2 - 1) && 
/* 301 */       (l2 >= i); k++)
/*     */     {
/* 303 */       l2 -= i;
/* 304 */       int m = (int)(i + l2 % paramInt3);
/* 305 */       l2 /= paramInt3;
/* 306 */       paramArrayOfByte[(j++)] = ((byte)m);
/*     */     }
/* 308 */     paramArrayOfByte[(j++)] = ((byte)(int)l2);
/*     */ 
/* 310 */     paramArrayOfInt[0] = j;
/*     */   }
/*     */ 
/*     */   public static int readInt(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 316 */     int i = 256 - paramInt2;
/* 317 */     long l1 = 0L;
/* 318 */     long l2 = 1L;
/* 319 */     int j = paramArrayOfInt[0];
/* 320 */     for (int k = 0; k < paramInt1; k++) {
/* 321 */       int m = paramArrayOfByte[(j++)] & 0xFF;
/* 322 */       l1 += m * l2;
/* 323 */       l2 *= paramInt2;
/* 324 */       if (m < i) {
/*     */         break;
/*     */       }
/*     */     }
/* 328 */     paramArrayOfInt[0] = j;
/* 329 */     return decodeSign32(l1, paramInt3);
/*     */   }
/*     */ 
/*     */   public static int readIntFrom(InputStream paramInputStream, int paramInt1, int paramInt2, int paramInt3) throws IOException
/*     */   {
/* 334 */     int i = 256 - paramInt2;
/* 335 */     long l1 = 0L;
/* 336 */     long l2 = 1L;
/* 337 */     for (int j = 0; j < paramInt1; j++) {
/* 338 */       int k = paramInputStream.read();
/* 339 */       if (k < 0) throw new RuntimeException("unexpected EOF");
/* 340 */       l1 += k * l2;
/* 341 */       l2 *= paramInt2;
/* 342 */       if (k < i) break;
/*     */     }
/* 344 */     assert ((l1 >= 0L) && (l1 < codeRangeLong(paramInt1, paramInt2)));
/* 345 */     return decodeSign32(l1, paramInt3);
/*     */   }
/*     */ 
/*     */   private Coding(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 367 */     this(paramInt1, paramInt2, paramInt3, 0);
/*     */   }
/*     */   private Coding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 370 */     this.B = paramInt1;
/* 371 */     this.H = paramInt2;
/* 372 */     this.L = (256 - paramInt2);
/* 373 */     this.S = paramInt3;
/* 374 */     this.del = paramInt4;
/* 375 */     this.min = codeMin(paramInt1, paramInt2, paramInt3, paramInt1);
/* 376 */     this.max = codeMax(paramInt1, paramInt2, paramInt3, paramInt1);
/* 377 */     this.umin = codeMin(paramInt1, paramInt2, 0, paramInt1);
/* 378 */     this.umax = codeMax(paramInt1, paramInt2, 0, paramInt1);
/* 379 */     this.byteMin = new int[paramInt1];
/* 380 */     this.byteMax = new int[paramInt1];
/*     */ 
/* 382 */     for (int i = 1; i <= paramInt1; i++) {
/* 383 */       this.byteMin[(i - 1)] = codeMin(paramInt1, paramInt2, paramInt3, i);
/* 384 */       this.byteMax[(i - 1)] = codeMax(paramInt1, paramInt2, paramInt3, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 389 */     if (!(paramObject instanceof Coding)) return false;
/* 390 */     Coding localCoding = (Coding)paramObject;
/* 391 */     if (this.B != localCoding.B) return false;
/* 392 */     if (this.H != localCoding.H) return false;
/* 393 */     if (this.S != localCoding.S) return false;
/* 394 */     if (this.del != localCoding.del) return false;
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 399 */     return (this.del << 14) + (this.S << 11) + (this.B << 8) + (this.H << 0);
/*     */   }
/*     */ 
/*     */   private static synchronized Coding of(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 405 */     if (codeMap == null) codeMap = new HashMap();
/* 406 */     Coding localCoding1 = new Coding(paramInt1, paramInt2, paramInt3, paramInt4);
/* 407 */     Coding localCoding2 = (Coding)codeMap.get(localCoding1);
/* 408 */     if (localCoding2 == null) codeMap.put(localCoding1, localCoding2 = localCoding1);
/* 409 */     return localCoding2;
/*     */   }
/*     */ 
/*     */   public static Coding of(int paramInt1, int paramInt2) {
/* 413 */     return of(paramInt1, paramInt2, 0, 0);
/*     */   }
/*     */ 
/*     */   public static Coding of(int paramInt1, int paramInt2, int paramInt3) {
/* 417 */     return of(paramInt1, paramInt2, paramInt3, 0);
/*     */   }
/*     */ 
/*     */   public boolean canRepresentValue(int paramInt) {
/* 421 */     if (isSubrange()) {
/* 422 */       return canRepresentUnsigned(paramInt);
/*     */     }
/* 424 */     return canRepresentSigned(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean canRepresentSigned(int paramInt)
/*     */   {
/* 433 */     return (paramInt >= this.min) && (paramInt <= this.max);
/*     */   }
/*     */ 
/*     */   public boolean canRepresentUnsigned(int paramInt)
/*     */   {
/* 442 */     return (paramInt >= this.umin) && (paramInt <= this.umax);
/*     */   }
/*     */ 
/*     */   public int readFrom(byte[] paramArrayOfByte, int[] paramArrayOfInt)
/*     */   {
/* 447 */     return readInt(paramArrayOfByte, paramArrayOfInt, this.B, this.H, this.S);
/*     */   }
/*     */   public void writeTo(byte[] paramArrayOfByte, int[] paramArrayOfInt, int paramInt) {
/* 450 */     writeInt(paramArrayOfByte, paramArrayOfInt, paramInt, this.B, this.H, this.S);
/*     */   }
/*     */ 
/*     */   public int readFrom(InputStream paramInputStream) throws IOException
/*     */   {
/* 455 */     return readIntFrom(paramInputStream, this.B, this.H, this.S);
/*     */   }
/*     */   public void writeTo(OutputStream paramOutputStream, int paramInt) throws IOException {
/* 458 */     byte[] arrayOfByte = new byte[this.B];
/* 459 */     int[] arrayOfInt = new int[1];
/* 460 */     writeInt(arrayOfByte, arrayOfInt, paramInt, this.B, this.H, this.S);
/* 461 */     paramOutputStream.write(arrayOfByte, 0, arrayOfInt[0]);
/*     */   }
/*     */ 
/*     */   public void readArrayFrom(InputStream paramInputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 467 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 468 */       paramArrayOfInt[i] = readFrom(paramInputStream);
/*     */     }
/* 470 */     for (i = 0; i < this.del; i++) {
/* 471 */       long l = 0L;
/* 472 */       for (int j = paramInt1; j < paramInt2; j++) {
/* 473 */         l += paramArrayOfInt[j];
/*     */ 
/* 475 */         if (isSubrange()) {
/* 476 */           l = reduceToUnsignedRange(l);
/*     */         }
/* 478 */         paramArrayOfInt[j] = ((int)l);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 483 */   public void writeArrayTo(OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException { if (paramInt2 <= paramInt1) return;
/* 484 */     for (int i = 0; i < this.del; i++)
/*     */     {
/*     */       int[] arrayOfInt1;
/* 486 */       if (!isSubrange())
/* 487 */         arrayOfInt1 = makeDeltas(paramArrayOfInt, paramInt1, paramInt2, 0, 0);
/*     */       else
/* 489 */         arrayOfInt1 = makeDeltas(paramArrayOfInt, paramInt1, paramInt2, this.min, this.max);
/* 490 */       paramArrayOfInt = arrayOfInt1;
/* 491 */       paramInt1 = 0;
/* 492 */       paramInt2 = arrayOfInt1.length;
/*     */     }
/*     */ 
/* 497 */     byte[] arrayOfByte = new byte[256];
/* 498 */     int j = arrayOfByte.length - this.B;
/* 499 */     int[] arrayOfInt2 = { 0 };
/* 500 */     for (int k = paramInt1; k < paramInt2; ) {
/* 501 */       while (arrayOfInt2[0] <= j) {
/* 502 */         writeTo(arrayOfByte, arrayOfInt2, paramArrayOfInt[(k++)]);
/* 503 */         if (k >= paramInt2) break;
/*     */       }
/* 505 */       paramOutputStream.write(arrayOfByte, 0, arrayOfInt2[0]);
/* 506 */       arrayOfInt2[0] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isSubrange()
/*     */   {
/* 514 */     return (this.max < 2147483647) && (this.max - this.min + 1L <= 2147483647L);
/*     */   }
/*     */ 
/*     */   boolean isFullRange()
/*     */   {
/* 523 */     return (this.max == 2147483647) && (this.min == -2147483648);
/*     */   }
/*     */ 
/*     */   int getRange()
/*     */   {
/* 528 */     assert (isSubrange());
/* 529 */     return this.max - this.min + 1;
/*     */   }
/*     */   Coding setB(int paramInt) {
/* 532 */     return of(paramInt, this.H, this.S, this.del); } 
/* 533 */   Coding setH(int paramInt) { return of(this.B, paramInt, this.S, this.del); } 
/* 534 */   Coding setS(int paramInt) { return of(this.B, this.H, paramInt, this.del); } 
/* 535 */   Coding setL(int paramInt) { return setH(256 - paramInt); } 
/* 536 */   Coding setD(int paramInt) { return of(this.B, this.H, this.S, paramInt); } 
/* 537 */   Coding getDeltaCoding() { return setD(this.del + 1); }
/*     */ 
/*     */   Coding getValueCoding()
/*     */   {
/* 541 */     if (isDelta()) {
/* 542 */       return of(this.B, this.H, 0, this.del - 1);
/*     */     }
/* 544 */     return this;
/*     */   }
/*     */ 
/*     */   int reduceToUnsignedRange(long paramLong)
/*     */   {
/* 551 */     if ((paramLong == (int)paramLong) && (canRepresentUnsigned((int)paramLong)))
/*     */     {
/* 553 */       return (int)paramLong;
/* 554 */     }int i = getRange();
/* 555 */     assert (i > 0);
/* 556 */     paramLong %= i;
/* 557 */     if (paramLong < 0L) paramLong += i;
/* 558 */     assert (canRepresentUnsigned((int)paramLong));
/* 559 */     return (int)paramLong;
/*     */   }
/*     */ 
/*     */   int reduceToSignedRange(int paramInt) {
/* 563 */     if (canRepresentSigned(paramInt))
/*     */     {
/* 565 */       return paramInt;
/* 566 */     }return reduceToSignedRange(paramInt, this.min, this.max);
/*     */   }
/*     */   static int reduceToSignedRange(int paramInt1, int paramInt2, int paramInt3) {
/* 569 */     int i = paramInt3 - paramInt2 + 1;
/* 570 */     assert (i > 0);
/* 571 */     int j = paramInt1;
/* 572 */     paramInt1 -= paramInt2;
/* 573 */     if ((paramInt1 < 0) && (j >= 0))
/*     */     {
/* 575 */       paramInt1 -= i;
/* 576 */       assert (paramInt1 >= 0);
/*     */     }
/* 578 */     paramInt1 %= i;
/* 579 */     if (paramInt1 < 0) paramInt1 += i;
/* 580 */     paramInt1 += paramInt2;
/* 581 */     assert ((paramInt2 <= paramInt1) && (paramInt1 <= paramInt3));
/* 582 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   boolean isSigned()
/*     */   {
/* 589 */     return this.min < 0;
/*     */   }
/*     */ 
/*     */   boolean isDelta() {
/* 593 */     return this.del != 0;
/*     */   }
/*     */   public int B() {
/* 596 */     return this.B; } 
/* 597 */   public int H() { return this.H; } 
/* 598 */   public int L() { return this.L; } 
/* 599 */   public int S() { return this.S; } 
/* 600 */   public int del() { return this.del; } 
/* 601 */   public int min() { return this.min; } 
/* 602 */   public int max() { return this.max; } 
/* 603 */   public int umin() { return this.umin; } 
/* 604 */   public int umax() { return this.umax; } 
/* 605 */   public int byteMin(int paramInt) { return this.byteMin[(paramInt - 1)]; } 
/* 606 */   public int byteMax(int paramInt) { return this.byteMax[(paramInt - 1)]; }
/*     */ 
/*     */   public int compareTo(Object paramObject) {
/* 609 */     Coding localCoding = (Coding)paramObject;
/* 610 */     int i = this.del - localCoding.del;
/* 611 */     if (i == 0)
/* 612 */       i = this.B - localCoding.B;
/* 613 */     if (i == 0)
/* 614 */       i = this.H - localCoding.H;
/* 615 */     if (i == 0)
/* 616 */       i = this.S - localCoding.S;
/* 617 */     return i;
/*     */   }
/*     */ 
/*     */   public int distanceFrom(Coding paramCoding)
/*     */   {
/* 622 */     int i = this.del - paramCoding.del;
/* 623 */     if (i < 0) i = -i;
/* 624 */     int j = this.S - paramCoding.S;
/* 625 */     if (j < 0) j = -j;
/* 626 */     int k = this.B - paramCoding.B;
/* 627 */     if (k < 0) k = -k;
/*     */     int m;
/* 629 */     if (this.H == paramCoding.H) {
/* 630 */       m = 0;
/*     */     }
/*     */     else {
/* 633 */       n = getHL();
/* 634 */       int i1 = paramCoding.getHL();
/*     */ 
/* 636 */       n *= n;
/* 637 */       i1 *= i1;
/* 638 */       if (n > i1)
/* 639 */         m = ceil_lg2(1 + (n - 1) / i1);
/*     */       else
/* 641 */         m = ceil_lg2(1 + (i1 - 1) / n);
/*     */     }
/* 643 */     int n = 5 * (i + j + k) + m;
/* 644 */     assert ((n != 0) || (compareTo(paramCoding) == 0));
/* 645 */     return n;
/*     */   }
/*     */ 
/*     */   private int getHL() {
/* 649 */     if (this.H <= 128) return this.H;
/* 650 */     if (this.L >= 1) return 16384 / this.L;
/* 651 */     return 32768;
/*     */   }
/*     */ 
/*     */   static int ceil_lg2(int paramInt)
/*     */   {
/* 656 */     assert (paramInt - 1 >= 0);
/* 657 */     paramInt--;
/* 658 */     int i = 0;
/* 659 */     while (paramInt != 0) {
/* 660 */       i++;
/* 661 */       paramInt >>= 1;
/*     */     }
/* 663 */     return i;
/*     */   }
/*     */ 
/*     */   static int bitWidth(int paramInt)
/*     */   {
/* 680 */     if (paramInt < 0) paramInt ^= -1;
/* 681 */     int i = 0;
/* 682 */     int j = paramInt;
/* 683 */     if (j < byteBitWidths.length) {
/* 684 */       return byteBitWidths[j];
/*     */     }
/* 686 */     int k = j >>> 16;
/* 687 */     if (k != 0) {
/* 688 */       j = k;
/* 689 */       i += 16;
/*     */     }
/* 691 */     k = j >>> 8;
/* 692 */     if (k != 0) {
/* 693 */       j = k;
/* 694 */       i += 8;
/*     */     }
/* 696 */     i += byteBitWidths[j];
/*     */ 
/* 698 */     return i;
/*     */   }
/*     */ 
/*     */   static int[] makeDeltas(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 710 */     assert (paramInt4 >= paramInt3);
/* 711 */     int i = paramInt2 - paramInt1;
/* 712 */     int[] arrayOfInt = new int[i];
/* 713 */     int j = 0;
/*     */     int k;
/*     */     int m;
/* 714 */     if (paramInt3 == paramInt4)
/* 715 */       for (k = 0; k < i; k++) {
/* 716 */         m = paramArrayOfInt[(paramInt1 + k)];
/* 717 */         arrayOfInt[k] = (m - j);
/* 718 */         j = m;
/*     */       }
/*     */     else {
/* 721 */       for (k = 0; k < i; k++) {
/* 722 */         m = paramArrayOfInt[(paramInt1 + k)];
/* 723 */         assert ((m >= 0) && (m + paramInt3 <= paramInt4));
/* 724 */         int n = m - j;
/* 725 */         assert (n == m - j);
/* 726 */         j = m;
/*     */ 
/* 728 */         n = reduceToSignedRange(n, paramInt3, paramInt4);
/* 729 */         arrayOfInt[k] = n;
/*     */       }
/*     */     }
/* 732 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   boolean canRepresent(int paramInt1, int paramInt2) {
/* 736 */     assert (paramInt1 <= paramInt2);
/* 737 */     if (this.del > 0) {
/* 738 */       if (isSubrange())
/*     */       {
/* 740 */         return (canRepresentUnsigned(paramInt2)) && (canRepresentUnsigned(paramInt1));
/*     */       }
/*     */ 
/* 744 */       return isFullRange();
/*     */     }
/*     */ 
/* 749 */     return (canRepresentSigned(paramInt2)) && (canRepresentSigned(paramInt1));
/*     */   }
/*     */ 
/*     */   boolean canRepresent(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 754 */     int i = paramInt2 - paramInt1;
/* 755 */     if (i == 0) return true;
/* 756 */     if (isFullRange()) return true;
/*     */ 
/* 758 */     int j = paramArrayOfInt[paramInt1];
/* 759 */     int k = j;
/* 760 */     for (int m = 1; m < i; m++) {
/* 761 */       int n = paramArrayOfInt[(paramInt1 + m)];
/* 762 */       if (j < n) j = n;
/* 763 */       if (k > n) k = n;
/*     */     }
/* 765 */     return canRepresent(k, j);
/*     */   }
/*     */ 
/*     */   public double getBitLength(int paramInt) {
/* 769 */     return getLength(paramInt) * 8.0D;
/*     */   }
/*     */ 
/*     */   public int getLength(int paramInt)
/*     */   {
/* 778 */     if ((isDelta()) && (isSubrange())) {
/* 779 */       if (!canRepresentUnsigned(paramInt))
/* 780 */         return 2147483647;
/* 781 */       paramInt = reduceToSignedRange(paramInt);
/*     */     }
/*     */     int i;
/* 783 */     if (paramInt >= 0) {
/* 784 */       for (i = 0; i < this.B; i++)
/* 785 */         if (paramInt <= this.byteMax[i]) return i + 1;
/*     */     }
/*     */     else {
/* 788 */       for (i = 0; i < this.B; i++) {
/* 789 */         if (paramInt >= this.byteMin[i]) return i + 1;
/*     */       }
/*     */     }
/* 792 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public int getLength(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 796 */     int[] arrayOfInt1 = paramInt2 - paramInt1;
/* 797 */     if (this.B == 1) return arrayOfInt1;
/* 798 */     if (this.L == 0) return arrayOfInt1 * this.B;
/* 799 */     if (isDelta())
/*     */     {
/* 801 */       if (!isSubrange())
/* 802 */         arrayOfInt2 = makeDeltas(paramArrayOfInt, paramInt1, paramInt2, 0, 0);
/*     */       else {
/* 804 */         arrayOfInt2 = makeDeltas(paramArrayOfInt, paramInt1, paramInt2, this.min, this.max);
/*     */       }
/* 806 */       paramArrayOfInt = arrayOfInt2;
/* 807 */       paramInt1 = 0;
/*     */     }
/* 809 */     int[] arrayOfInt2 = arrayOfInt1;
/*     */     int i;
/* 811 */     for (int j = 1; j <= this.B; j++)
/*     */     {
/* 813 */       int k = this.byteMax[(j - 1)];
/* 814 */       int m = this.byteMin[(j - 1)];
/* 815 */       int[] arrayOfInt3 = 0;
/* 816 */       for (int n = 0; n < arrayOfInt1; n++) {
/* 817 */         int i1 = paramArrayOfInt[(paramInt1 + n)];
/* 818 */         if (i1 >= 0) {
/* 819 */           if (i1 > k) arrayOfInt3++;
/*     */         }
/* 821 */         else if (i1 < m) arrayOfInt3++;
/*     */       }
/*     */ 
/* 824 */       if (arrayOfInt3 == 0) break;
/* 825 */       if (j == this.B) return 2147483647;
/* 826 */       arrayOfInt2 += arrayOfInt3;
/*     */     }
/* 828 */     return i;
/*     */   }
/*     */ 
/*     */   public byte[] getMetaCoding(Coding paramCoding) {
/* 832 */     if (paramCoding == this) return new byte[] { 0 };
/* 833 */     int i = BandStructure.indexOf(this);
/* 834 */     if (i > 0)
/* 835 */       return new byte[] { (byte)i };
/* 836 */     return new byte[] { 116, (byte)(this.del + 2 * this.S + 8 * (this.B - 1)), (byte)(this.H - 1) };
/*     */   }
/*     */ 
/*     */   public static int parseMetaCoding(byte[] paramArrayOfByte, int paramInt, Coding paramCoding, CodingMethod[] paramArrayOfCodingMethod)
/*     */   {
/* 843 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 844 */     if ((1 <= i) && (i <= 115)) {
/* 845 */       Coding localCoding = BandStructure.codingForIndex(i);
/* 846 */       assert (localCoding != null);
/* 847 */       paramArrayOfCodingMethod[0] = localCoding;
/* 848 */       return paramInt;
/*     */     }
/* 850 */     if (i == 116) {
/* 851 */       int j = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 852 */       int k = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 853 */       int m = j % 2;
/* 854 */       int n = j / 2 % 4;
/* 855 */       int i1 = j / 8 + 1;
/* 856 */       int i2 = k + 1;
/* 857 */       if ((1 > i1) || (i1 > 5) || (0 > n) || (n > 2) || (1 > i2) || (i2 > 256) || (0 > m) || (m > 1) || ((i1 == 1) && (i2 != 256)) || ((i1 == 5) && (i2 == 256)))
/*     */       {
/* 863 */         throw new RuntimeException("Bad arb. coding: (" + i1 + "," + i2 + "," + n + "," + m);
/*     */       }
/* 865 */       paramArrayOfCodingMethod[0] = of(i1, i2, n, m);
/* 866 */       return paramInt;
/*     */     }
/* 868 */     return paramInt - 1;
/*     */   }
/*     */ 
/*     */   public String keyString()
/*     */   {
/* 873 */     return "(" + this.B + "," + this.H + "," + this.S + "," + this.del + ")";
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 877 */     String str = "Coding" + keyString();
/*     */ 
/* 880 */     return str;
/*     */   }
/*     */ 
/*     */   String stringForDebug()
/*     */   {
/* 885 */     String str1 = "" + this.min;
/* 886 */     String str2 = "" + this.max;
/* 887 */     String str3 = keyString() + " L=" + this.L + " r=[" + str1 + "," + str2 + "]";
/* 888 */     if (isSubrange())
/* 889 */       str3 = str3 + " subrange";
/* 890 */     else if (!isFullRange())
/* 891 */       str3 = str3 + " MIDRANGE";
/* 892 */     if (verboseStringForDebug) {
/* 893 */       str3 = str3 + " {";
/* 894 */       int i = 0;
/* 895 */       for (int j = 1; j <= this.B; j++) {
/* 896 */         int k = saturate32(this.byteMax[(j - 1)] - this.byteMin[(j - 1)] + 1L);
/* 897 */         assert (k == saturate32(codeRangeLong(this.B, this.H, j)));
/* 898 */         k -= i;
/* 899 */         i = k;
/* 900 */         String str4 = "" + k;
/* 901 */         str3 = str3 + " #" + j + "=" + str4;
/*     */       }
/* 903 */       str3 = str3 + " }";
/*     */     }
/* 905 */     return str3;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 666 */     byteBitWidths = new byte[256];
/*     */ 
/* 668 */     for (int i = 0; i < byteBitWidths.length; i++) {
/* 669 */       byteBitWidths[i] = ((byte)ceil_lg2(i + 1));
/*     */     }
/* 671 */     for (i = 10; i >= 0; i = (i << 1) - (i >> 3))
/* 672 */       assert (bitWidth(i) == ceil_lg2(i + 1));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Coding
 * JD-Core Version:    0.6.2
 */