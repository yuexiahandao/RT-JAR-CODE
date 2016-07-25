/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ class DToA
/*      */ {
/*      */   private static final int DTOBASESTR_BUFFER_SIZE = 1078;
/*      */   static final int DTOSTR_STANDARD = 0;
/*      */   static final int DTOSTR_STANDARD_EXPONENTIAL = 1;
/*      */   static final int DTOSTR_FIXED = 2;
/*      */   static final int DTOSTR_EXPONENTIAL = 3;
/*      */   static final int DTOSTR_PRECISION = 4;
/*      */   private static final int Frac_mask = 1048575;
/*      */   private static final int Exp_shift = 20;
/*      */   private static final int Exp_msk1 = 1048576;
/*      */   private static final long Frac_maskL = 4503599627370495L;
/*      */   private static final int Exp_shiftL = 52;
/*      */   private static final long Exp_msk1L = 4503599627370496L;
/*      */   private static final int Bias = 1023;
/*      */   private static final int P = 53;
/*      */   private static final int Exp_shift1 = 20;
/*      */   private static final int Exp_mask = 2146435072;
/*      */   private static final int Exp_mask_shifted = 2047;
/*      */   private static final int Bndry_mask = 1048575;
/*      */   private static final int Log2P = 1;
/*      */   private static final int Sign_bit = -2147483648;
/*      */   private static final int Exp_11 = 1072693248;
/*      */   private static final int Ten_pmax = 22;
/*      */   private static final int Quick_max = 14;
/*      */   private static final int Bletch = 16;
/*      */   private static final int Frac_mask1 = 1048575;
/*      */   private static final int Int_max = 14;
/*      */   private static final int n_bigtens = 5;
/*  112 */   private static final double[] tens = { 1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 10000000.0D, 100000000.0D, 1000000000.0D, 10000000000.0D, 100000000000.0D, 1000000000000.0D, 10000000000000.0D, 100000000000000.0D, 1000000000000000.0D, 10000000000000000.0D, 1.0E+017D, 1.0E+018D, 1.0E+019D, 1.0E+020D, 1.0E+021D, 1.0E+022D };
/*      */ 
/*  118 */   private static final double[] bigtens = { 10000000000000000.0D, 1.E+032D, 1.0E+064D, 1.E+128D, 1.0E+256D };
/*      */ 
/* 1167 */   private static final int[] dtoaModes = { 0, 0, 3, 2, 2 };
/*      */ 
/*      */   private static char BASEDIGIT(int paramInt)
/*      */   {
/*   74 */     return (char)(paramInt >= 10 ? 87 + paramInt : 48 + paramInt);
/*      */   }
/*      */ 
/*      */   private static int lo0bits(int paramInt)
/*      */   {
/*  123 */     int j = paramInt;
/*      */ 
/*  125 */     if ((j & 0x7) != 0) {
/*  126 */       if ((j & 0x1) != 0)
/*  127 */         return 0;
/*  128 */       if ((j & 0x2) != 0) {
/*  129 */         return 1;
/*      */       }
/*  131 */       return 2;
/*      */     }
/*  133 */     int i = 0;
/*  134 */     if ((j & 0xFFFF) == 0) {
/*  135 */       i = 16;
/*  136 */       j >>>= 16;
/*      */     }
/*  138 */     if ((j & 0xFF) == 0) {
/*  139 */       i += 8;
/*  140 */       j >>>= 8;
/*      */     }
/*  142 */     if ((j & 0xF) == 0) {
/*  143 */       i += 4;
/*  144 */       j >>>= 4;
/*      */     }
/*  146 */     if ((j & 0x3) == 0) {
/*  147 */       i += 2;
/*  148 */       j >>>= 2;
/*      */     }
/*  150 */     if ((j & 0x1) == 0) {
/*  151 */       i++;
/*  152 */       j >>>= 1;
/*  153 */       if ((j & 0x1) == 0)
/*  154 */         return 32;
/*      */     }
/*  156 */     return i;
/*      */   }
/*      */ 
/*      */   private static int hi0bits(int paramInt)
/*      */   {
/*  162 */     int i = 0;
/*      */ 
/*  164 */     if ((paramInt & 0xFFFF0000) == 0) {
/*  165 */       i = 16;
/*  166 */       paramInt <<= 16;
/*      */     }
/*  168 */     if ((paramInt & 0xFF000000) == 0) {
/*  169 */       i += 8;
/*  170 */       paramInt <<= 8;
/*      */     }
/*  172 */     if ((paramInt & 0xF0000000) == 0) {
/*  173 */       i += 4;
/*  174 */       paramInt <<= 4;
/*      */     }
/*  176 */     if ((paramInt & 0xC0000000) == 0) {
/*  177 */       i += 2;
/*  178 */       paramInt <<= 2;
/*      */     }
/*  180 */     if ((paramInt & 0x80000000) == 0) {
/*  181 */       i++;
/*  182 */       if ((paramInt & 0x40000000) == 0)
/*  183 */         return 32;
/*      */     }
/*  185 */     return i;
/*      */   }
/*      */ 
/*      */   private static void stuffBits(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  190 */     paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 24));
/*  191 */     paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >> 16));
/*  192 */     paramArrayOfByte[(paramInt1 + 2)] = ((byte)(paramInt2 >> 8));
/*  193 */     paramArrayOfByte[(paramInt1 + 3)] = ((byte)paramInt2);
/*      */   }
/*      */ 
/*      */   private static BigInteger d2b(double paramDouble, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  203 */     long l = Double.doubleToLongBits(paramDouble);
/*  204 */     int i1 = (int)(l >>> 32);
/*  205 */     int i2 = (int)l;
/*      */ 
/*  207 */     int m = i1 & 0xFFFFF;
/*  208 */     i1 &= 2147483647;
/*      */     int n;
/*  210 */     if ((n = i1 >>> 20) != 0)
/*  211 */       m |= 1048576;
/*      */     int k;
/*      */     byte[] arrayOfByte;
/*      */     int j;
/*      */     int i;
/*  213 */     if ((k = i2) != 0) {
/*  214 */       arrayOfByte = new byte[8];
/*  215 */       j = lo0bits(k);
/*  216 */       k >>>= j;
/*  217 */       if (j != 0) {
/*  218 */         stuffBits(arrayOfByte, 4, k | m << 32 - j);
/*  219 */         m >>= j;
/*      */       }
/*      */       else {
/*  222 */         stuffBits(arrayOfByte, 4, k);
/*  223 */       }stuffBits(arrayOfByte, 0, m);
/*  224 */       i = m != 0 ? 2 : 1;
/*      */     }
/*      */     else
/*      */     {
/*  228 */       arrayOfByte = new byte[4];
/*  229 */       j = lo0bits(m);
/*  230 */       m >>>= j;
/*  231 */       stuffBits(arrayOfByte, 0, m);
/*  232 */       j += 32;
/*  233 */       i = 1;
/*      */     }
/*  235 */     if (n != 0) {
/*  236 */       paramArrayOfInt1[0] = (n - 1023 - 52 + j);
/*  237 */       paramArrayOfInt2[0] = (53 - j);
/*      */     }
/*      */     else {
/*  240 */       paramArrayOfInt1[0] = (n - 1023 - 52 + 1 + j);
/*  241 */       paramArrayOfInt2[0] = (32 * i - hi0bits(m));
/*      */     }
/*  243 */     return new BigInteger(arrayOfByte);
/*      */   }
/*      */ 
/*      */   static String JS_dtobasestr(int paramInt, double paramDouble)
/*      */   {
/*  248 */     if ((2 > paramInt) || (paramInt > 36)) {
/*  249 */       throw new IllegalArgumentException("Bad base: " + paramInt);
/*      */     }
/*      */ 
/*  252 */     if (Double.isNaN(paramDouble))
/*  253 */       return "NaN";
/*  254 */     if (Double.isInfinite(paramDouble))
/*  255 */       return paramDouble > 0.0D ? "Infinity" : "-Infinity";
/*  256 */     if (paramDouble == 0.0D)
/*      */     {
/*  258 */       return "0";
/*      */     }
/*      */     int i;
/*  262 */     if (paramDouble >= 0.0D) {
/*  263 */       i = 0;
/*      */     } else {
/*  265 */       i = 1;
/*  266 */       paramDouble = -paramDouble;
/*      */     }
/*      */ 
/*  272 */     double d1 = Math.floor(paramDouble);
/*  273 */     long l1 = ()d1;
/*      */     String str;
/*      */     int k;
/*  274 */     if (l1 == d1)
/*      */     {
/*  276 */       str = Long.toString(i != 0 ? -l1 : l1, paramInt);
/*      */     }
/*      */     else {
/*  279 */       long l2 = Double.doubleToLongBits(d1);
/*  280 */       k = (int)(l2 >> 52) & 0x7FF;
/*      */       long l3;
/*  282 */       if (k == 0)
/*  283 */         l3 = (l2 & 0xFFFFFFFF) << 1;
/*      */       else {
/*  285 */         l3 = l2 & 0xFFFFFFFF | 0x0;
/*      */       }
/*  287 */       if (i != 0) {
/*  288 */         l3 = -l3;
/*      */       }
/*  290 */       k -= 1075;
/*  291 */       localBigInteger1 = BigInteger.valueOf(l3);
/*  292 */       if (k > 0)
/*  293 */         localBigInteger1 = localBigInteger1.shiftLeft(k);
/*  294 */       else if (k < 0) {
/*  295 */         localBigInteger1 = localBigInteger1.shiftRight(-k);
/*      */       }
/*  297 */       str = localBigInteger1.toString(paramInt);
/*      */     }
/*      */ 
/*  300 */     if (paramDouble == d1)
/*      */     {
/*  302 */       return str;
/*      */     }
/*      */ 
/*  312 */     char[] arrayOfChar = new char[1078];
/*  313 */     int j = 0;
/*  314 */     double d2 = paramDouble - d1;
/*      */ 
/*  316 */     long l4 = Double.doubleToLongBits(paramDouble);
/*  317 */     int m = (int)(l4 >> 32);
/*  318 */     int n = (int)l4;
/*      */ 
/*  320 */     int[] arrayOfInt1 = new int[1];
/*  321 */     int[] arrayOfInt2 = new int[1];
/*      */ 
/*  323 */     BigInteger localBigInteger1 = d2b(d2, arrayOfInt1, arrayOfInt2);
/*      */ 
/*  327 */     int i1 = -(m >>> 20 & 0x7FF);
/*  328 */     if (i1 == 0)
/*  329 */       i1 = -1;
/*  330 */     i1 += 1076;
/*      */ 
/*  333 */     BigInteger localBigInteger2 = BigInteger.valueOf(1L);
/*  334 */     BigInteger localBigInteger3 = localBigInteger2;
/*  335 */     if ((n == 0) && ((m & 0xFFFFF) == 0) && ((m & 0x7FE00000) != 0))
/*      */     {
/*  339 */       i1++;
/*  340 */       localBigInteger3 = BigInteger.valueOf(2L);
/*      */     }
/*      */ 
/*  343 */     localBigInteger1 = localBigInteger1.shiftLeft(arrayOfInt1[0] + i1);
/*  344 */     BigInteger localBigInteger4 = BigInteger.valueOf(1L);
/*  345 */     localBigInteger4 = localBigInteger4.shiftLeft(i1);
/*      */ 
/*  351 */     BigInteger localBigInteger5 = BigInteger.valueOf(paramInt);
/*      */ 
/*  353 */     int i2 = 0;
/*      */     do {
/*  355 */       localBigInteger1 = localBigInteger1.multiply(localBigInteger5);
/*  356 */       localObject = localBigInteger1.divideAndRemainder(localBigInteger4);
/*  357 */       localBigInteger1 = localObject[1];
/*  358 */       k = (char)localObject[0].intValue();
/*  359 */       if (localBigInteger2 == localBigInteger3) {
/*  360 */         localBigInteger2 = localBigInteger3 = localBigInteger2.multiply(localBigInteger5);
/*      */       } else {
/*  362 */         localBigInteger2 = localBigInteger2.multiply(localBigInteger5);
/*  363 */         localBigInteger3 = localBigInteger3.multiply(localBigInteger5);
/*      */       }
/*      */ 
/*  367 */       int i3 = localBigInteger1.compareTo(localBigInteger2);
/*      */ 
/*  369 */       BigInteger localBigInteger6 = localBigInteger4.subtract(localBigInteger3);
/*  370 */       int i4 = localBigInteger6.signum() <= 0 ? 1 : localBigInteger1.compareTo(localBigInteger6);
/*      */ 
/*  372 */       if ((i4 == 0) && ((n & 0x1) == 0)) {
/*  373 */         if (i3 > 0)
/*  374 */           k++;
/*  375 */         i2 = 1;
/*      */       }
/*  377 */       else if ((i3 < 0) || ((i3 == 0) && ((n & 0x1) == 0))) {
/*  378 */         if (i4 > 0)
/*      */         {
/*  381 */           localBigInteger1 = localBigInteger1.shiftLeft(1);
/*  382 */           i4 = localBigInteger1.compareTo(localBigInteger4);
/*  383 */           if (i4 > 0)
/*      */           {
/*  385 */             k++;
/*      */           }
/*      */         }
/*  387 */         i2 = 1;
/*  388 */       } else if (i4 > 0) {
/*  389 */         k++;
/*  390 */         i2 = 1;
/*      */       }
/*      */ 
/*  393 */       arrayOfChar[(j++)] = BASEDIGIT(k);
/*  394 */     }while (i2 == 0);
/*      */ 
/*  396 */     Object localObject = new StringBuffer(str.length() + 1 + j);
/*  397 */     ((StringBuffer)localObject).append(str);
/*  398 */     ((StringBuffer)localObject).append('.');
/*  399 */     ((StringBuffer)localObject).append(arrayOfChar, 0, j);
/*  400 */     return ((StringBuffer)localObject).toString();
/*      */   }
/*      */ 
/*      */   static int word0(double paramDouble)
/*      */   {
/*  441 */     long l = Double.doubleToLongBits(paramDouble);
/*  442 */     return (int)(l >> 32);
/*      */   }
/*      */ 
/*      */   static double setWord0(double paramDouble, int paramInt)
/*      */   {
/*  447 */     long l = Double.doubleToLongBits(paramDouble);
/*  448 */     l = paramInt << 32 | l & 0xFFFFFFFF;
/*  449 */     return Double.longBitsToDouble(l);
/*      */   }
/*      */ 
/*      */   static int word1(double paramDouble)
/*      */   {
/*  454 */     long l = Double.doubleToLongBits(paramDouble);
/*  455 */     return (int)l;
/*      */   }
/*      */ 
/*      */   static BigInteger pow5mult(BigInteger paramBigInteger, int paramInt)
/*      */   {
/*  462 */     return paramBigInteger.multiply(BigInteger.valueOf(5L).pow(paramInt));
/*      */   }
/*      */ 
/*      */   static boolean roundOff(StringBuffer paramStringBuffer)
/*      */   {
/*  467 */     int i = paramStringBuffer.length();
/*  468 */     while (i != 0) {
/*  469 */       i--;
/*  470 */       int j = paramStringBuffer.charAt(i);
/*  471 */       if (j != 57) {
/*  472 */         paramStringBuffer.setCharAt(i, (char)(j + 1));
/*  473 */         paramStringBuffer.setLength(i + 1);
/*  474 */         return false;
/*      */       }
/*      */     }
/*  477 */     paramStringBuffer.setLength(0);
/*  478 */     return true;
/*      */   }
/*      */ 
/*      */   static int JS_dtoa(double paramDouble, int paramInt1, boolean paramBoolean, int paramInt2, boolean[] paramArrayOfBoolean, StringBuffer paramStringBuffer)
/*      */   {
/*  532 */     int[] arrayOfInt1 = new int[1];
/*  533 */     int[] arrayOfInt2 = new int[1];
/*      */ 
/*  537 */     if ((word0(paramDouble) & 0x80000000) != 0)
/*      */     {
/*  539 */       paramArrayOfBoolean[0] = true;
/*      */ 
/*  541 */       paramDouble = setWord0(paramDouble, word0(paramDouble) & 0x7FFFFFFF);
/*      */     }
/*      */     else {
/*  544 */       paramArrayOfBoolean[0] = false;
/*      */     }
/*  546 */     if ((word0(paramDouble) & 0x7FF00000) == 2146435072)
/*      */     {
/*  548 */       paramStringBuffer.append((word1(paramDouble) == 0) && ((word0(paramDouble) & 0xFFFFF) == 0) ? "Infinity" : "NaN");
/*  549 */       return 9999;
/*      */     }
/*  551 */     if (paramDouble == 0.0D)
/*      */     {
/*  553 */       paramStringBuffer.setLength(0);
/*  554 */       paramStringBuffer.append('0');
/*  555 */       return 1;
/*      */     }
/*      */ 
/*  558 */     Object localObject1 = d2b(paramDouble, arrayOfInt1, arrayOfInt2);
/*      */     double d1;
/*      */     int i12;
/*  559 */     if ((k = word0(paramDouble) >>> 20 & 0x7FF) != 0) {
/*  560 */       d1 = setWord0(paramDouble, word0(paramDouble) & 0xFFFFF | 0x3FF00000);
/*      */ 
/*  582 */       k -= 1023;
/*  583 */       i12 = 0;
/*      */     }
/*      */     else
/*      */     {
/*  587 */       k = arrayOfInt2[0] + arrayOfInt1[0] + 1074;
/*  588 */       long l2 = k > 32 ? word0(paramDouble) << 64 - k | word1(paramDouble) >>> k - 32 : word1(paramDouble) << 32 - k;
/*      */ 
/*  591 */       d1 = setWord0(l2, word0(l2) - 32505856);
/*  592 */       k -= 1075;
/*  593 */       i12 = 1;
/*      */     }
/*      */ 
/*  596 */     double d2 = (d1 - 1.5D) * 0.289529654602168D + 0.1760912590558D + k * 0.301029995663981D;
/*  597 */     int i5 = (int)d2;
/*  598 */     if ((d2 < 0.0D) && (d2 != i5))
/*  599 */       i5--;
/*  600 */     int i13 = 1;
/*  601 */     if ((i5 >= 0) && (i5 <= 22)) {
/*  602 */       if (paramDouble < tens[i5])
/*  603 */         i5--;
/*  604 */       i13 = 0;
/*      */     }
/*      */ 
/*  608 */     int i3 = arrayOfInt2[0] - k - 1;
/*      */     int i;
/*      */     int i9;
/*  610 */     if (i3 >= 0) {
/*  611 */       i = 0;
/*  612 */       i9 = i3;
/*      */     }
/*      */     else {
/*  615 */       i = -i3;
/*  616 */       i9 = 0;
/*      */     }
/*      */     int j;
/*      */     int i10;
/*  618 */     if (i5 >= 0) {
/*  619 */       j = 0;
/*  620 */       i10 = i5;
/*  621 */       i9 += i5;
/*      */     }
/*      */     else {
/*  624 */       i -= i5;
/*  625 */       j = -i5;
/*  626 */       i10 = 0;
/*      */     }
/*      */ 
/*  630 */     if ((paramInt1 < 0) || (paramInt1 > 9))
/*  631 */       paramInt1 = 0;
/*  632 */     int i14 = 1;
/*  633 */     if (paramInt1 > 5) {
/*  634 */       paramInt1 -= 4;
/*  635 */       i14 = 0;
/*      */     }
/*  637 */     int i15 = 1;
/*      */     int i2;
/*  638 */     int n = i2 = 0;
/*  639 */     switch (paramInt1) {
/*      */     case 0:
/*      */     case 1:
/*  642 */       n = i2 = -1;
/*  643 */       k = 18;
/*  644 */       paramInt2 = 0;
/*  645 */       break;
/*      */     case 2:
/*  647 */       i15 = 0;
/*      */     case 4:
/*  650 */       if (paramInt2 <= 0)
/*  651 */         paramInt2 = 1;
/*  652 */       n = i2 = k = paramInt2;
/*  653 */       break;
/*      */     case 3:
/*  655 */       i15 = 0;
/*      */     case 5:
/*  658 */       k = paramInt2 + i5 + 1;
/*  659 */       n = k;
/*  660 */       i2 = k - 1;
/*  661 */       if (k <= 0) {
/*  662 */         k = 1;
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  668 */     int i16 = 0;
/*      */     int i4;
/*      */     long l1;
/*      */     int i17;
/*  669 */     if ((n >= 0) && (n <= 14) && (i14 != 0))
/*      */     {
/*  673 */       k = 0;
/*  674 */       d1 = paramDouble;
/*  675 */       int i6 = i5;
/*  676 */       int i1 = n;
/*  677 */       int m = 2;
/*      */ 
/*  679 */       if (i5 > 0) {
/*  680 */         d2 = tens[(i5 & 0xF)];
/*  681 */         i3 = i5 >> 4;
/*  682 */         if ((i3 & 0x10) != 0)
/*      */         {
/*  684 */           i3 &= 15;
/*  685 */           paramDouble /= bigtens[4];
/*  686 */           m++;
/*      */         }
/*  688 */         for (; i3 != 0; k++) {
/*  689 */           if ((i3 & 0x1) != 0) {
/*  690 */             m++;
/*  691 */             d2 *= bigtens[k];
/*      */           }
/*  688 */           i3 >>= 1;
/*      */         }
/*      */ 
/*  693 */         paramDouble /= d2;
/*      */       }
/*  695 */       else if ((i4 = -i5) != 0) {
/*  696 */         paramDouble *= tens[(i4 & 0xF)];
/*  697 */         for (i3 = i4 >> 4; i3 != 0; k++) {
/*  698 */           if ((i3 & 0x1) != 0) {
/*  699 */             m++;
/*  700 */             paramDouble *= bigtens[k];
/*      */           }
/*  697 */           i3 >>= 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  704 */       if ((i13 != 0) && (paramDouble < 1.0D) && (n > 0)) {
/*  705 */         if (i2 <= 0) {
/*  706 */           i16 = 1;
/*      */         } else {
/*  708 */           n = i2;
/*  709 */           i5--;
/*  710 */           paramDouble *= 10.0D;
/*  711 */           m++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  717 */       double d3 = m * paramDouble + 7.0D;
/*  718 */       d3 = setWord0(d3, word0(d3) - 54525952);
/*  719 */       if (n == 0) {
/*  720 */         localBigInteger3 = localObject3 = null;
/*  721 */         paramDouble -= 5.0D;
/*  722 */         if (paramDouble > d3) {
/*  723 */           paramStringBuffer.append('1');
/*  724 */           i5++;
/*  725 */           return i5 + 1;
/*      */         }
/*  727 */         if (paramDouble < -d3) {
/*  728 */           paramStringBuffer.setLength(0);
/*  729 */           paramStringBuffer.append('0');
/*  730 */           return 1;
/*      */         }
/*  732 */         i16 = 1;
/*      */       }
/*  734 */       if (i16 == 0) {
/*  735 */         i16 = 1;
/*  736 */         if (i15 != 0)
/*      */         {
/*  740 */           d3 = 0.5D / tens[(n - 1)] - d3;
/*  741 */           k = 0;
/*      */           while (true) { l1 = ()paramDouble;
/*  743 */             paramDouble -= l1;
/*  744 */             paramStringBuffer.append((char)(int)(48L + l1));
/*  745 */             if (paramDouble < d3) {
/*  746 */               return i5 + 1;
/*      */             }
/*  748 */             if (1.0D - paramDouble < d3)
/*      */             {
/*      */               do
/*      */               {
/*  752 */                 i17 = paramStringBuffer.charAt(paramStringBuffer.length() - 1);
/*  753 */                 paramStringBuffer.setLength(paramStringBuffer.length() - 1);
/*  754 */                 if (i17 != 57) break; 
/*  755 */               }while (paramStringBuffer.length() != 0);
/*  756 */               i5++;
/*  757 */               i17 = 48;
/*      */ 
/*  761 */               paramStringBuffer.append((char)(i17 + 1));
/*  762 */               return i5 + 1;
/*      */             }
/*  764 */             k++; if (k >= n)
/*      */               break;
/*  766 */             d3 *= 10.0D;
/*  767 */             paramDouble *= 10.0D;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  772 */         d3 *= tens[(n - 1)];
/*  773 */         for (k = 1; ; paramDouble *= 10.0D) {
/*  774 */           l1 = ()paramDouble;
/*  775 */           paramDouble -= l1;
/*  776 */           paramStringBuffer.append((char)(int)(48L + l1));
/*  777 */           if (k == n) {
/*  778 */             if (paramDouble > 0.5D + d3)
/*      */             {
/*      */               do
/*      */               {
/*  782 */                 i17 = paramStringBuffer.charAt(paramStringBuffer.length() - 1);
/*  783 */                 paramStringBuffer.setLength(paramStringBuffer.length() - 1);
/*  784 */                 if (i17 != 57) break; 
/*  785 */               }while (paramStringBuffer.length() != 0);
/*  786 */               i5++;
/*  787 */               i17 = 48;
/*      */ 
/*  791 */               paramStringBuffer.append((char)(i17 + 1));
/*  792 */               return i5 + 1;
/*      */             }
/*      */ 
/*  795 */             if (paramDouble >= 0.5D - d3) break;
/*  796 */             stripTrailingZeroes(paramStringBuffer);
/*      */ 
/*  799 */             return i5 + 1;
/*      */           }
/*  773 */           k++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  806 */       if (i16 != 0) {
/*  807 */         paramStringBuffer.setLength(0);
/*  808 */         paramDouble = d1;
/*  809 */         i5 = i6;
/*  810 */         n = i1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  816 */     if ((arrayOfInt1[0] >= 0) && (i5 <= 14))
/*      */     {
/*  818 */       d2 = tens[i5];
/*  819 */       if ((paramInt2 < 0) && (n <= 0)) {
/*  820 */         localBigInteger3 = localObject3 = null;
/*  821 */         if ((n < 0) || (paramDouble < 5.0D * d2) || ((!paramBoolean) && (paramDouble == 5.0D * d2))) {
/*  822 */           paramStringBuffer.setLength(0);
/*  823 */           paramStringBuffer.append('0');
/*  824 */           return 1;
/*      */         }
/*  826 */         paramStringBuffer.append('1');
/*  827 */         i5++;
/*  828 */         return i5 + 1;
/*      */       }
/*  830 */       for (k = 1; ; k++) {
/*  831 */         l1 = ()(paramDouble / d2);
/*  832 */         paramDouble -= l1 * d2;
/*  833 */         paramStringBuffer.append((char)(int)(48L + l1));
/*  834 */         if (k == n) {
/*  835 */           paramDouble += paramDouble;
/*  836 */           if ((paramDouble > d2) || ((paramDouble == d2) && (((l1 & 1L) != 0L) || (paramBoolean))))
/*      */           {
/*      */             do
/*      */             {
/*  847 */               i17 = paramStringBuffer.charAt(paramStringBuffer.length() - 1);
/*  848 */               paramStringBuffer.setLength(paramStringBuffer.length() - 1);
/*  849 */               if (i17 != 57) break; 
/*  850 */             }while (paramStringBuffer.length() != 0);
/*  851 */             i5++;
/*  852 */             i17 = 48;
/*      */ 
/*  856 */             paramStringBuffer.append((char)(i17 + 1));
/*      */           }
/*      */         }
/*      */         else {
/*  860 */           paramDouble *= 10.0D;
/*  861 */           if (paramDouble == 0.0D) break;
/*      */         }
/*      */       }
/*  864 */       return i5 + 1;
/*      */     }
/*      */ 
/*  867 */     int i7 = i;
/*  868 */     int i8 = j;
/*      */     Object localObject2;
/*  869 */     Object localObject3 = localObject2 = null;
/*  870 */     if (i15 != 0) {
/*  871 */       if (paramInt1 < 2) {
/*  872 */         k = i12 != 0 ? arrayOfInt1[0] + 1075 : 54 - arrayOfInt2[0];
/*      */       }
/*      */       else
/*      */       {
/*  877 */         i3 = n - 1;
/*  878 */         if (i8 >= i3) {
/*  879 */           i8 -= i3;
/*      */         } else {
/*  881 */           i10 += i3 -= i8;
/*  882 */           j += i3;
/*  883 */           i8 = 0;
/*      */         }
/*  885 */         if ((k = n) < 0) {
/*  886 */           i7 -= k;
/*  887 */           k = 0;
/*      */         }
/*      */       }
/*      */ 
/*  891 */       i += k;
/*  892 */       i9 += k;
/*  893 */       localObject3 = BigInteger.valueOf(1L);
/*      */     }
/*      */ 
/*  899 */     if ((i7 > 0) && (i9 > 0)) {
/*  900 */       k = i7 < i9 ? i7 : i9;
/*  901 */       i -= k;
/*  902 */       i7 -= k;
/*  903 */       i9 -= k;
/*      */     }
/*      */ 
/*  907 */     if (j > 0) {
/*  908 */       if (i15 != 0) {
/*  909 */         if (i8 > 0) {
/*  910 */           localObject3 = pow5mult((BigInteger)localObject3, i8);
/*  911 */           BigInteger localBigInteger1 = ((BigInteger)localObject3).multiply((BigInteger)localObject1);
/*  912 */           localObject1 = localBigInteger1;
/*      */         }
/*  914 */         if ((i3 = j - i8) != 0)
/*  915 */           localObject1 = pow5mult((BigInteger)localObject1, i3);
/*      */       }
/*      */       else {
/*  918 */         localObject1 = pow5mult((BigInteger)localObject1, j);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  923 */     BigInteger localBigInteger3 = BigInteger.valueOf(1L);
/*  924 */     if (i10 > 0) {
/*  925 */       localBigInteger3 = pow5mult(localBigInteger3, i10);
/*      */     }
/*      */ 
/*  930 */     int i11 = 0;
/*  931 */     if ((paramInt1 < 2) && 
/*  932 */       (word1(paramDouble) == 0) && ((word0(paramDouble) & 0xFFFFF) == 0) && ((word0(paramDouble) & 0x7FE00000) != 0))
/*      */     {
/*  937 */       i++;
/*  938 */       i9++;
/*  939 */       i11 = 1;
/*      */     }
/*      */ 
/*  950 */     byte[] arrayOfByte = localBigInteger3.toByteArray();
/*  951 */     int i18 = 0;
/*  952 */     for (int i19 = 0; i19 < 4; i19++) {
/*  953 */       i18 <<= 8;
/*  954 */       if (i19 < arrayOfByte.length)
/*  955 */         i18 |= arrayOfByte[i19] & 0xFF;
/*      */     }
/*  957 */     if ((k = (i10 != 0 ? 32 - hi0bits(i18) : 1) + i9 & 0x1F) != 0) {
/*  958 */       k = 32 - k;
/*      */     }
/*  960 */     if (k > 4) {
/*  961 */       k -= 4;
/*  962 */       i += k;
/*  963 */       i7 += k;
/*  964 */       i9 += k;
/*      */     }
/*  966 */     else if (k < 4) {
/*  967 */       k += 28;
/*  968 */       i += k;
/*  969 */       i7 += k;
/*  970 */       i9 += k;
/*      */     }
/*      */ 
/*  973 */     if (i > 0)
/*  974 */       localObject1 = ((BigInteger)localObject1).shiftLeft(i);
/*  975 */     if (i9 > 0) {
/*  976 */       localBigInteger3 = localBigInteger3.shiftLeft(i9);
/*      */     }
/*      */ 
/*  979 */     if ((i13 != 0) && 
/*  980 */       (((BigInteger)localObject1).compareTo(localBigInteger3) < 0)) {
/*  981 */       i5--;
/*  982 */       localObject1 = ((BigInteger)localObject1).multiply(BigInteger.valueOf(10L));
/*  983 */       if (i15 != 0)
/*  984 */         localObject3 = ((BigInteger)localObject3).multiply(BigInteger.valueOf(10L));
/*  985 */       n = i2;
/*      */     }
/*      */ 
/*  990 */     if ((n <= 0) && (paramInt1 > 2))
/*      */     {
/*  993 */       if ((n < 0) || ((k = ((BigInteger)localObject1).compareTo(localBigInteger3 = localBigInteger3.multiply(BigInteger.valueOf(5L)))) < 0) || ((k == 0) && (!paramBoolean)))
/*      */       {
/* 1001 */         paramStringBuffer.setLength(0);
/* 1002 */         paramStringBuffer.append('0');
/* 1003 */         return 1;
/*      */       }
/*      */ 
/* 1007 */       paramStringBuffer.append('1');
/* 1008 */       i5++;
/* 1009 */       return i5 + 1;
/*      */     }
/*      */     BigInteger[] arrayOfBigInteger;
/*      */     char c;
/* 1011 */     if (i15 != 0) {
/* 1012 */       if (i7 > 0) {
/* 1013 */         localObject3 = ((BigInteger)localObject3).shiftLeft(i7);
/*      */       }
/*      */ 
/* 1019 */       localObject2 = localObject3;
/* 1020 */       if (i11 != 0) {
/* 1021 */         localObject3 = localObject2;
/* 1022 */         localObject3 = ((BigInteger)localObject3).shiftLeft(1);
/*      */       }
/*      */ 
/* 1027 */       for (k = 1; ; k++) {
/* 1028 */         arrayOfBigInteger = ((BigInteger)localObject1).divideAndRemainder(localBigInteger3);
/* 1029 */         localObject1 = arrayOfBigInteger[1];
/* 1030 */         c = (char)(arrayOfBigInteger[0].intValue() + 48);
/*      */ 
/* 1034 */         i3 = ((BigInteger)localObject1).compareTo((BigInteger)localObject2);
/*      */ 
/* 1036 */         BigInteger localBigInteger2 = localBigInteger3.subtract((BigInteger)localObject3);
/* 1037 */         i4 = localBigInteger2.signum() <= 0 ? 1 : ((BigInteger)localObject1).compareTo(localBigInteger2);
/*      */ 
/* 1039 */         if ((i4 == 0) && (paramInt1 == 0) && ((word1(paramDouble) & 0x1) == 0)) {
/* 1040 */           if (c == '9') {
/* 1041 */             paramStringBuffer.append('9');
/* 1042 */             if (roundOff(paramStringBuffer)) {
/* 1043 */               i5++;
/* 1044 */               paramStringBuffer.append('1');
/*      */             }
/* 1046 */             return i5 + 1;
/*      */           }
/*      */ 
/* 1049 */           if (i3 > 0)
/* 1050 */             c = (char)(c + '\001');
/* 1051 */           paramStringBuffer.append(c);
/* 1052 */           return i5 + 1;
/*      */         }
/* 1054 */         if ((i3 < 0) || ((i3 == 0) && (paramInt1 == 0) && ((word1(paramDouble) & 0x1) == 0)))
/*      */         {
/* 1059 */           if (i4 > 0)
/*      */           {
/* 1062 */             localObject1 = ((BigInteger)localObject1).shiftLeft(1);
/* 1063 */             i4 = ((BigInteger)localObject1).compareTo(localBigInteger3);
/* 1064 */             if ((i4 > 0) || ((i4 == 0) && (((c & 0x1) == '\001') || (paramBoolean)))) { c = (char)(c + '\001'); if (c == '9')
/*      */               {
/* 1066 */                 paramStringBuffer.append('9');
/* 1067 */                 if (roundOff(paramStringBuffer)) {
/* 1068 */                   i5++;
/* 1069 */                   paramStringBuffer.append('1');
/*      */                 }
/* 1071 */                 return i5 + 1;
/*      */               }
/*      */             }
/*      */           }
/* 1075 */           paramStringBuffer.append(c);
/* 1076 */           return i5 + 1;
/*      */         }
/* 1078 */         if (i4 > 0) {
/* 1079 */           if (c == '9')
/*      */           {
/* 1083 */             paramStringBuffer.append('9');
/* 1084 */             if (roundOff(paramStringBuffer)) {
/* 1085 */               i5++;
/* 1086 */               paramStringBuffer.append('1');
/*      */             }
/* 1088 */             return i5 + 1;
/*      */           }
/* 1090 */           paramStringBuffer.append((char)(c + '\001'));
/* 1091 */           return i5 + 1;
/*      */         }
/* 1093 */         paramStringBuffer.append(c);
/* 1094 */         if (k == n)
/*      */           break;
/* 1096 */         localObject1 = ((BigInteger)localObject1).multiply(BigInteger.valueOf(10L));
/* 1097 */         if (localObject2 == localObject3) {
/* 1098 */           localObject2 = localObject3 = ((BigInteger)localObject3).multiply(BigInteger.valueOf(10L));
/*      */         } else {
/* 1100 */           localObject2 = ((BigInteger)localObject2).multiply(BigInteger.valueOf(10L));
/* 1101 */           localObject3 = ((BigInteger)localObject3).multiply(BigInteger.valueOf(10L));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1106 */     for (int k = 1; ; k++)
/*      */     {
/* 1108 */       arrayOfBigInteger = ((BigInteger)localObject1).divideAndRemainder(localBigInteger3);
/* 1109 */       localObject1 = arrayOfBigInteger[1];
/* 1110 */       c = (char)(arrayOfBigInteger[0].intValue() + 48);
/* 1111 */       paramStringBuffer.append(c);
/* 1112 */       if (k >= n)
/*      */         break;
/* 1114 */       localObject1 = ((BigInteger)localObject1).multiply(BigInteger.valueOf(10L));
/*      */     }
/*      */ 
/* 1119 */     localObject1 = ((BigInteger)localObject1).shiftLeft(1);
/* 1120 */     i3 = ((BigInteger)localObject1).compareTo(localBigInteger3);
/* 1121 */     if ((i3 > 0) || ((i3 == 0) && (((c & 0x1) == '\001') || (paramBoolean))))
/*      */     {
/* 1130 */       if (roundOff(paramStringBuffer)) {
/* 1131 */         i5++;
/* 1132 */         paramStringBuffer.append('1');
/* 1133 */         return i5 + 1;
/*      */       }
/*      */     }
/*      */     else {
/* 1137 */       stripTrailingZeroes(paramStringBuffer);
/*      */     }
/*      */ 
/* 1151 */     return i5 + 1;
/*      */   }
/*      */ 
/*      */   private static void stripTrailingZeroes(StringBuffer paramStringBuffer)
/*      */   {
/* 1159 */     int i = paramStringBuffer.length();
/* 1160 */     while ((i-- > 0) && (paramStringBuffer.charAt(i) == '0'));
/* 1163 */     paramStringBuffer.setLength(i + 1);
/*      */   }
/*      */ 
/*      */   static void JS_dtostr(StringBuffer paramStringBuffer, int paramInt1, int paramInt2, double paramDouble)
/*      */   {
/* 1178 */     boolean[] arrayOfBoolean = new boolean[1];
/*      */ 
/* 1184 */     if ((paramInt1 == 2) && ((paramDouble >= 1.0E+021D) || (paramDouble <= -1.0E+021D))) {
/* 1185 */       paramInt1 = 0;
/*      */     }
/* 1187 */     int i = JS_dtoa(paramDouble, dtoaModes[paramInt1], paramInt1 >= 2, paramInt2, arrayOfBoolean, paramStringBuffer);
/* 1188 */     int j = paramStringBuffer.length();
/*      */ 
/* 1191 */     if (i != 9999) {
/* 1192 */       int k = 0;
/* 1193 */       int m = 0;
/*      */ 
/* 1196 */       switch (paramInt1) {
/*      */       case 0:
/* 1198 */         if ((i < -5) || (i > 21))
/* 1199 */           k = 1;
/*      */         else
/* 1201 */           m = i;
/* 1202 */         break;
/*      */       case 2:
/* 1205 */         if (paramInt2 >= 0)
/* 1206 */           m = i + paramInt2;
/*      */         else
/* 1208 */           m = i;
/* 1209 */         break;
/*      */       case 3:
/* 1213 */         m = paramInt2;
/*      */       case 1:
/* 1216 */         k = 1;
/* 1217 */         break;
/*      */       case 4:
/* 1221 */         m = paramInt2;
/* 1222 */         if ((i < -5) || (i > paramInt2)) {
/* 1223 */           k = 1;
/*      */         }
/*      */         break;
/*      */       }
/*      */ 
/* 1228 */       if (j < m) {
/* 1229 */         int n = m;
/* 1230 */         j = m;
/*      */         do
/* 1232 */           paramStringBuffer.append('0');
/* 1233 */         while (paramStringBuffer.length() != n);
/*      */       }
/*      */ 
/* 1236 */       if (k != 0)
/*      */       {
/* 1238 */         if (j != 1) {
/* 1239 */           paramStringBuffer.insert(1, '.');
/*      */         }
/* 1241 */         paramStringBuffer.append('e');
/* 1242 */         if (i - 1 >= 0)
/* 1243 */           paramStringBuffer.append('+');
/* 1244 */         paramStringBuffer.append(i - 1);
/*      */       }
/* 1246 */       else if (i != j)
/*      */       {
/* 1249 */         if (i > 0)
/*      */         {
/* 1251 */           paramStringBuffer.insert(i, '.');
/*      */         }
/*      */         else {
/* 1254 */           for (int i1 = 0; i1 < 1 - i; i1++)
/* 1255 */             paramStringBuffer.insert(0, '0');
/* 1256 */           paramStringBuffer.insert(1, '.');
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1262 */     if ((arrayOfBoolean[0] != 0) && ((word0(paramDouble) != -2147483648) || (word1(paramDouble) != 0)) && (((word0(paramDouble) & 0x7FF00000) != 2146435072) || ((word1(paramDouble) == 0) && ((word0(paramDouble) & 0xFFFFF) == 0))))
/*      */     {
/* 1266 */       paramStringBuffer.insert(0, '-');
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.DToA
 * JD-Core Version:    0.6.2
 */