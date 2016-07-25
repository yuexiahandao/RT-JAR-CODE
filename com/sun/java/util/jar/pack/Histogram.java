/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import [I;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ final class Histogram
/*     */ {
/*     */   protected final int[][] matrix;
/*     */   protected final int totalWeight;
/*     */   protected final int[] values;
/*     */   protected final int[] counts;
/*     */   private static final long LOW32 = 4294967295L;
/* 163 */   private static double log2 = Math.log(2.0D);
/*     */ 
/* 189 */   private final BitMetric bitMetric = new BitMetric() {
/*     */     public double getBitLength(int paramAnonymousInt) {
/* 191 */       return Histogram.this.getBitLength(paramAnonymousInt);
/*     */     }
/* 189 */   };
/*     */ 
/*     */   public Histogram(int[] paramArrayOfInt)
/*     */   {
/*  55 */     long[] arrayOfLong = computeHistogram2Col(maybeSort(paramArrayOfInt));
/*  56 */     int[][] arrayOfInt = makeTable(arrayOfLong);
/*  57 */     this.values = arrayOfInt[0];
/*  58 */     this.counts = arrayOfInt[1];
/*  59 */     this.matrix = makeMatrix(arrayOfLong);
/*  60 */     this.totalWeight = paramArrayOfInt.length;
/*  61 */     assert (assertWellFormed(paramArrayOfInt));
/*     */   }
/*     */ 
/*     */   public Histogram(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*  65 */     this(sortedSlice(paramArrayOfInt, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public Histogram(int[][] paramArrayOfInt)
/*     */   {
/*  72 */     paramArrayOfInt = normalizeMatrix(paramArrayOfInt);
/*  73 */     this.matrix = paramArrayOfInt;
/*  74 */     int i = 0;
/*  75 */     int j = 0;
/*  76 */     for (int k = 0; k < paramArrayOfInt.length; k++) {
/*  77 */       m = paramArrayOfInt[k].length - 1;
/*  78 */       i += m;
/*  79 */       j += paramArrayOfInt[k][0] * m;
/*     */     }
/*  81 */     this.totalWeight = j;
/*  82 */     long[] arrayOfLong = new long[i];
/*  83 */     int m = 0;
/*  84 */     for (int n = 0; n < paramArrayOfInt.length; n++) {
/*  85 */       for (int i1 = 1; i1 < paramArrayOfInt[n].length; i1++)
/*     */       {
/*  87 */         arrayOfLong[(m++)] = (paramArrayOfInt[n][i1] << 32 | 0xFFFFFFFF & paramArrayOfInt[n][0]);
/*     */       }
/*     */     }
/*     */ 
/*  91 */     assert (m == arrayOfLong.length);
/*  92 */     Arrays.sort(arrayOfLong);
/*  93 */     int[][] arrayOfInt = makeTable(arrayOfLong);
/*  94 */     this.values = arrayOfInt[1];
/*  95 */     this.counts = arrayOfInt[0];
/*  96 */     assert (assertWellFormed(null));
/*     */   }
/*     */ 
/*     */   public int[][] getMatrix()
/*     */   {
/* 119 */     return this.matrix;
/*     */   }
/*     */   public int getRowCount() {
/* 122 */     return this.matrix.length;
/*     */   }
/*     */   public int getRowFrequency(int paramInt) {
/* 125 */     return this.matrix[paramInt][0];
/*     */   }
/*     */   public int getRowLength(int paramInt) {
/* 128 */     return this.matrix[paramInt].length - 1;
/*     */   }
/*     */   public int getRowValue(int paramInt1, int paramInt2) {
/* 131 */     return this.matrix[paramInt1][(paramInt2 + 1)];
/*     */   }
/*     */ 
/*     */   public int getRowWeight(int paramInt) {
/* 135 */     return getRowFrequency(paramInt) * getRowLength(paramInt);
/*     */   }
/*     */ 
/*     */   public int getTotalWeight()
/*     */   {
/* 140 */     return this.totalWeight;
/*     */   }
/*     */ 
/*     */   public int getTotalLength()
/*     */   {
/* 145 */     return this.values.length;
/*     */   }
/*     */ 
/*     */   public int[] getAllValues()
/*     */   {
/* 152 */     return this.values;
/*     */   }
/*     */ 
/*     */   public int[] getAllFrequencies()
/*     */   {
/* 160 */     return this.counts;
/*     */   }
/*     */ 
/*     */   public int getFrequency(int paramInt)
/*     */   {
/* 167 */     int i = Arrays.binarySearch(this.values, paramInt);
/* 168 */     if (i < 0) return 0;
/* 169 */     assert (this.values[i] == paramInt);
/* 170 */     return this.counts[i];
/*     */   }
/*     */ 
/*     */   public double getBitLength(int paramInt)
/*     */   {
/* 175 */     double d = getFrequency(paramInt) / getTotalWeight();
/* 176 */     return -Math.log(d) / log2;
/*     */   }
/*     */ 
/*     */   public double getRowBitLength(int paramInt)
/*     */   {
/* 181 */     double d = getRowFrequency(paramInt) / getTotalWeight();
/* 182 */     return -Math.log(d) / log2;
/*     */   }
/*     */ 
/*     */   public BitMetric getBitMetric()
/*     */   {
/* 195 */     return this.bitMetric;
/*     */   }
/*     */ 
/*     */   public double getBitLength()
/*     */   {
/* 201 */     double d = 0.0D;
/* 202 */     for (int i = 0; i < this.matrix.length; i++) {
/* 203 */       d += getRowBitLength(i) * getRowWeight(i);
/*     */     }
/* 205 */     assert (0.1D > Math.abs(d - getBitLength(this.bitMetric)));
/* 206 */     return d;
/*     */   }
/*     */ 
/*     */   public double getBitLength(BitMetric paramBitMetric)
/*     */   {
/* 212 */     double d = 0.0D;
/* 213 */     for (int i = 0; i < this.matrix.length; i++) {
/* 214 */       for (int j = 1; j < this.matrix[i].length; j++) {
/* 215 */         d += this.matrix[i][0] * paramBitMetric.getBitLength(this.matrix[i][j]);
/*     */       }
/*     */     }
/* 218 */     return d;
/*     */   }
/*     */ 
/*     */   private static double round(double paramDouble1, double paramDouble2)
/*     */   {
/* 223 */     return Math.round(paramDouble1 * paramDouble2) / paramDouble2;
/*     */   }
/*     */ 
/*     */   public int[][] normalizeMatrix(int[][] paramArrayOfInt)
/*     */   {
/* 231 */     long[] arrayOfLong = new long[paramArrayOfInt.length];
/* 232 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/* 233 */       if (paramArrayOfInt[i].length > 1) {
/* 234 */         j = paramArrayOfInt[i][0];
/* 235 */         if (j > 0)
/* 236 */           arrayOfLong[i] = (j << 32 | i);
/*     */       }
/* 238 */     Arrays.sort(arrayOfLong);
/* 239 */     int[][] arrayOfInt = new int[paramArrayOfInt.length][];
/* 240 */     int j = -1;
/* 241 */     int k = 0;
/* 242 */     int m = 0;
/* 243 */     for (int n = 0; ; n++)
/*     */     {
/*     */       int[] arrayOfInt1;
/* 245 */       if (n < paramArrayOfInt.length) {
/* 246 */         long l = arrayOfLong[(arrayOfLong.length - n - 1)];
/* 247 */         if (l == 0L) continue;
/* 248 */         arrayOfInt1 = paramArrayOfInt[((int)l)];
/* 249 */         assert (l >>> 32 == arrayOfInt1[0]);
/*     */       } else {
/* 251 */         arrayOfInt1 = new int[] { -1 };
/*     */       }
/* 253 */       if ((arrayOfInt1[0] != j) && (m > k))
/*     */       {
/* 255 */         int i1 = 0;
/* 256 */         for (int i2 = k; i2 < m; i2++) {
/* 257 */           [I local[I1 = arrayOfInt[i2];
/* 258 */           assert (local[I1[0] == j);
/* 259 */           i1 += local[I1.length - 1;
/*     */         }
/* 261 */         Object localObject = new int[1 + i1];
/* 262 */         localObject[0] = j;
/* 263 */         int i3 = 1;
/* 264 */         for (int i4 = k; i4 < m; i4++) {
/* 265 */           [I local[I2 = arrayOfInt[i4];
/* 266 */           assert (local[I2[0] == j);
/* 267 */           System.arraycopy(local[I2, 1, localObject, i3, local[I2.length - 1);
/* 268 */           i3 += local[I2.length - 1;
/*     */         }
/* 270 */         if (!isSorted((int[])localObject, 1, true)) {
/* 271 */           Arrays.sort((int[])localObject, 1, localObject.length);
/* 272 */           i4 = 2;
/*     */ 
/* 274 */           for (int i5 = 2; i5 < localObject.length; i5++) {
/* 275 */             if (localObject[i5] != localObject[(i5 - 1)])
/* 276 */               localObject[(i4++)] = localObject[i5];
/*     */           }
/* 278 */           if (i4 < localObject.length)
/*     */           {
/* 280 */             int[] arrayOfInt2 = new int[i4];
/* 281 */             System.arraycopy(localObject, 0, arrayOfInt2, 0, i4);
/* 282 */             localObject = arrayOfInt2;
/*     */           }
/*     */         }
/* 285 */         arrayOfInt[(k++)] = localObject;
/* 286 */         m = k;
/*     */       }
/* 288 */       if (n == paramArrayOfInt.length)
/*     */         break;
/* 290 */       j = arrayOfInt1[0];
/* 291 */       arrayOfInt[(m++)] = arrayOfInt1;
/*     */     }
/* 293 */     assert (k == m);
/*     */ 
/* 295 */     paramArrayOfInt = arrayOfInt;
/* 296 */     if (k < paramArrayOfInt.length) {
/* 297 */       arrayOfInt = new int[k][];
/* 298 */       System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, k);
/* 299 */       paramArrayOfInt = arrayOfInt;
/*     */     }
/* 301 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   public String[] getRowTitles(String paramString)
/*     */   {
/* 306 */     int i = getTotalLength();
/* 307 */     int j = getTotalWeight();
/* 308 */     String[] arrayOfString = new String[this.matrix.length];
/* 309 */     int k = 0;
/* 310 */     int m = 0;
/* 311 */     for (int n = 0; n < this.matrix.length; n++) {
/* 312 */       int i1 = getRowFrequency(n);
/* 313 */       int i2 = getRowLength(n);
/* 314 */       int i3 = getRowWeight(n);
/* 315 */       k += i3;
/* 316 */       m += i2;
/* 317 */       long l1 = (k * 100L + j / 2) / j;
/* 318 */       long l2 = (m * 100L + i / 2) / i;
/* 319 */       double d = getRowBitLength(n);
/* 320 */       assert (0.1D > Math.abs(d - getBitLength(this.matrix[n][1])));
/* 321 */       arrayOfString[n] = (paramString + "[" + n + "]" + " len=" + round(d, 10.0D) + " (" + i1 + "*[" + i2 + "])" + " (" + k + ":" + l1 + "%)" + " [" + m + ":" + l2 + "%]");
/*     */     }
/*     */ 
/* 327 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */   {
/* 334 */     print("hist", paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void print(String paramString, PrintStream paramPrintStream)
/*     */   {
/* 341 */     print(paramString, getRowTitles(paramString), paramPrintStream);
/*     */   }
/*     */ 
/*     */   public void print(String paramString, String[] paramArrayOfString, PrintStream paramPrintStream)
/*     */   {
/* 348 */     int i = getTotalLength();
/* 349 */     int j = getTotalWeight();
/* 350 */     double d1 = getBitLength();
/* 351 */     double d2 = d1 / j;
/* 352 */     double d3 = j / i;
/* 353 */     String str = paramString + " len=" + round(d1, 10.0D) + " avgLen=" + round(d2, 10.0D) + " weight(" + j + ")" + " unique[" + i + "]" + " avgWeight(" + round(d3, 100.0D) + ")";
/*     */ 
/* 359 */     if (paramArrayOfString == null) {
/* 360 */       paramPrintStream.println(str);
/*     */     } else {
/* 362 */       paramPrintStream.println(str + " {");
/* 363 */       StringBuffer localStringBuffer = new StringBuffer();
/* 364 */       for (int k = 0; k < this.matrix.length; k++) {
/* 365 */         localStringBuffer.setLength(0);
/* 366 */         localStringBuffer.append("  ").append(paramArrayOfString[k]).append(" {");
/* 367 */         for (int m = 1; m < this.matrix[k].length; m++) {
/* 368 */           localStringBuffer.append(" ").append(this.matrix[k][m]);
/*     */         }
/* 370 */         localStringBuffer.append(" }");
/* 371 */         paramPrintStream.println(localStringBuffer);
/*     */       }
/* 373 */       paramPrintStream.println("}");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int[][] makeMatrix(long[] paramArrayOfLong)
/*     */   {
/* 391 */     Arrays.sort(paramArrayOfLong);
/* 392 */     int[] arrayOfInt1 = new int[paramArrayOfLong.length];
/* 393 */     for (int i = 0; i < arrayOfInt1.length; i++) {
/* 394 */       arrayOfInt1[i] = ((int)(paramArrayOfLong[i] >>> 32));
/*     */     }
/* 396 */     long[] arrayOfLong = computeHistogram2Col(arrayOfInt1);
/* 397 */     int[][] arrayOfInt = new int[arrayOfLong.length][];
/* 398 */     int j = 0;
/* 399 */     int k = 0;
/*     */ 
/* 401 */     int m = arrayOfInt.length;
/*     */     while (true) { m--; if (m < 0) break;
/* 402 */       long l1 = arrayOfLong[(k++)];
/* 403 */       int n = (int)l1;
/* 404 */       int i1 = (int)(l1 >>> 32);
/* 405 */       int[] arrayOfInt2 = new int[1 + i1];
/* 406 */       arrayOfInt2[0] = n;
/* 407 */       for (int i2 = 0; i2 < i1; i2++) {
/* 408 */         long l2 = paramArrayOfLong[(j++)];
/* 409 */         assert (l2 >>> 32 == n);
/* 410 */         arrayOfInt2[(1 + i2)] = ((int)l2);
/*     */       }
/* 412 */       arrayOfInt[m] = arrayOfInt2;
/*     */     }
/* 414 */     assert (j == paramArrayOfLong.length);
/* 415 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static int[][] makeTable(long[] paramArrayOfLong)
/*     */   {
/* 420 */     int[][] arrayOfInt = new int[2][paramArrayOfLong.length];
/*     */ 
/* 423 */     for (int i = 0; i < paramArrayOfLong.length; i++) {
/* 424 */       arrayOfInt[0][i] = ((int)paramArrayOfLong[i]);
/* 425 */       arrayOfInt[1][i] = ((int)(paramArrayOfLong[i] >>> 32));
/*     */     }
/* 427 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static long[] computeHistogram2Col(int[] paramArrayOfInt)
/*     */   {
/* 447 */     switch (paramArrayOfInt.length) {
/*     */     case 0:
/* 449 */       return new long[0];
/*     */     case 1:
/* 451 */       return new long[] { 0x0 | 0xFFFFFFFF & paramArrayOfInt[0] };
/*     */     }
/* 453 */     long[] arrayOfLong = null;
/* 454 */     for (int i = 1; ; i = 0) {
/* 455 */       int j = -1;
/* 456 */       int k = paramArrayOfInt[0] ^ 0xFFFFFFFF;
/* 457 */       int m = 0;
/* 458 */       for (int n = 0; n <= paramArrayOfInt.length; n++)
/*     */       {
/*     */         int i1;
/* 460 */         if (n < paramArrayOfInt.length)
/* 461 */           i1 = paramArrayOfInt[n];
/*     */         else
/* 463 */           i1 = k ^ 0xFFFFFFFF;
/* 464 */         if (i1 == k) {
/* 465 */           m++;
/*     */         }
/*     */         else {
/* 468 */           if ((i == 0) && (m != 0))
/*     */           {
/* 470 */             arrayOfLong[j] = (m << 32 | 0xFFFFFFFF & k);
/*     */           }
/*     */ 
/* 473 */           k = i1;
/* 474 */           m = 1;
/* 475 */           j++;
/*     */         }
/*     */       }
/* 478 */       if (i == 0)
/*     */         break;
/* 480 */       arrayOfLong = new long[j];
/*     */     }
/*     */ 
/* 485 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   private static int[][] regroupHistogram(int[][] paramArrayOfInt, int[] paramArrayOfInt1)
/*     */   {
/* 497 */     long l1 = 0L;
/* 498 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 499 */       l1 += paramArrayOfInt[i].length - 1;
/*     */     }
/* 501 */     long l2 = 0L;
/* 502 */     for (int j = 0; j < paramArrayOfInt1.length; j++) {
/* 503 */       l2 += paramArrayOfInt1[j];
/*     */     }
/* 505 */     if (l2 > l1) {
/* 506 */       j = paramArrayOfInt1.length;
/* 507 */       long l4 = l1;
/* 508 */       for (n = 0; n < paramArrayOfInt1.length; n++) {
/* 509 */         if (l4 < paramArrayOfInt1[n]) {
/* 510 */           int[] arrayOfInt2 = new int[n + 1];
/* 511 */           System.arraycopy(paramArrayOfInt1, 0, arrayOfInt2, 0, n + 1);
/* 512 */           paramArrayOfInt1 = arrayOfInt2;
/* 513 */           paramArrayOfInt1[n] = ((int)l4);
/* 514 */           l4 = 0L;
/* 515 */           break;
/*     */         }
/* 517 */         l4 -= paramArrayOfInt1[n];
/*     */       }
/*     */     } else {
/* 520 */       long l3 = l1 - l2;
/* 521 */       int[] arrayOfInt1 = new int[paramArrayOfInt1.length + 1];
/* 522 */       System.arraycopy(paramArrayOfInt1, 0, arrayOfInt1, 0, paramArrayOfInt1.length);
/* 523 */       arrayOfInt1[paramArrayOfInt1.length] = ((int)l3);
/* 524 */       paramArrayOfInt1 = arrayOfInt1;
/*     */     }
/* 526 */     int[][] arrayOfInt = new int[paramArrayOfInt1.length][];
/*     */ 
/* 528 */     int k = 0;
/* 529 */     int m = 1;
/* 530 */     int n = paramArrayOfInt[k].length;
/* 531 */     for (int i1 = 0; i1 < paramArrayOfInt1.length; i1++) {
/* 532 */       int i2 = paramArrayOfInt1[i1];
/* 533 */       int[] arrayOfInt3 = new int[1 + i2];
/* 534 */       long l5 = 0L;
/* 535 */       arrayOfInt[i1] = arrayOfInt3;
/* 536 */       int i3 = 1;
/* 537 */       while (i3 < arrayOfInt3.length) {
/* 538 */         int i4 = arrayOfInt3.length - i3;
/* 539 */         while (m == n) {
/* 540 */           m = 1;
/* 541 */           n = paramArrayOfInt[(++k)].length;
/*     */         }
/* 543 */         if (i4 > n - m) i4 = n - m;
/* 544 */         l5 += paramArrayOfInt[k][0] * i4;
/* 545 */         System.arraycopy(paramArrayOfInt[k], n - i4, arrayOfInt3, i3, i4);
/* 546 */         n -= i4;
/* 547 */         i3 += i4;
/*     */       }
/* 549 */       Arrays.sort(arrayOfInt3, 1, arrayOfInt3.length);
/*     */ 
/* 551 */       arrayOfInt3[0] = ((int)((l5 + i2 / 2) / i2));
/*     */     }
/* 553 */     assert (m == n);
/* 554 */     assert (k == paramArrayOfInt.length - 1);
/* 555 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static Histogram makeByteHistogram(InputStream paramInputStream) throws IOException
/*     */   {
/* 560 */     byte[] arrayOfByte = new byte[4096];
/* 561 */     int[] arrayOfInt = new int[256];
/*     */     int i;
/* 562 */     while ((i = paramInputStream.read(arrayOfByte)) > 0) {
/* 563 */       for (j = 0; j < i; j++) {
/* 564 */         arrayOfInt[(arrayOfByte[j] & 0xFF)] += 1;
/*     */       }
/*     */     }
/*     */ 
/* 568 */     int[][] arrayOfInt1 = new int[256][2];
/* 569 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 570 */       arrayOfInt1[j][0] = arrayOfInt[j];
/* 571 */       arrayOfInt1[j][1] = j;
/*     */     }
/* 573 */     return new Histogram(arrayOfInt1);
/*     */   }
/*     */ 
/*     */   private static int[] sortedSlice(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 579 */     if ((paramInt1 == 0) && (paramInt2 == paramArrayOfInt.length) && (isSorted(paramArrayOfInt, 0, false)))
/*     */     {
/* 581 */       return paramArrayOfInt;
/*     */     }
/* 583 */     int[] arrayOfInt = new int[paramInt2 - paramInt1];
/* 584 */     System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, 0, arrayOfInt.length);
/* 585 */     Arrays.sort(arrayOfInt);
/* 586 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static boolean isSorted(int[] paramArrayOfInt, int paramInt, boolean paramBoolean)
/*     */   {
/* 593 */     for (int i = paramInt + 1; i < paramArrayOfInt.length; i++) {
/* 594 */       if (paramBoolean ? paramArrayOfInt[(i - 1)] >= paramArrayOfInt[i] : paramArrayOfInt[(i - 1)] > paramArrayOfInt[i])
/*     */       {
/* 596 */         return false;
/*     */       }
/*     */     }
/* 599 */     return true;
/*     */   }
/*     */ 
/*     */   private static int[] maybeSort(int[] paramArrayOfInt)
/*     */   {
/* 605 */     if (!isSorted(paramArrayOfInt, 0, false)) {
/* 606 */       paramArrayOfInt = (int[])paramArrayOfInt.clone();
/* 607 */       Arrays.sort(paramArrayOfInt);
/*     */     }
/* 609 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   private boolean assertWellFormed(int[] paramArrayOfInt)
/*     */   {
/* 660 */     return true;
/*     */   }
/*     */ 
/*     */   public static abstract interface BitMetric
/*     */   {
/*     */     public abstract double getBitLength(int paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Histogram
 * JD-Core Version:    0.6.2
 */