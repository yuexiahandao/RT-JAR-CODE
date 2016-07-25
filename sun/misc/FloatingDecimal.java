/*      */ package sun.misc;
/*      */ 
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ public class FloatingDecimal
/*      */ {
/*      */   boolean isExceptional;
/*      */   boolean isNegative;
/*      */   int decExponent;
/*      */   char[] digits;
/*      */   int nDigits;
/*      */   int bigIntExp;
/*      */   int bigIntNBits;
/*   41 */   boolean mustSetRoundDir = false;
/*   42 */   boolean fromHex = false;
/*   43 */   int roundDir = 0;
/*      */   static final long signMask = -9223372036854775808L;
/*      */   static final long expMask = 9218868437227405312L;
/*      */   static final long fractMask = 4503599627370495L;
/*      */   static final int expShift = 52;
/*      */   static final int expBias = 1023;
/*      */   static final long fractHOB = 4503599627370496L;
/*      */   static final long expOne = 4607182418800017408L;
/*      */   static final int maxSmallBinExp = 62;
/*      */   static final int minSmallBinExp = -21;
/*      */   static final int maxDecimalDigits = 15;
/*      */   static final int maxDecimalExponent = 308;
/*      */   static final int minDecimalExponent = -324;
/*      */   static final int bigDecimalExponent = 324;
/*      */   static final int MAX_NDIGITS = 1100;
/*      */   static final long highbyte = -72057594037927936L;
/*      */   static final long highbit = -9223372036854775808L;
/*      */   static final long lowbytes = 72057594037927935L;
/*      */   static final int singleSignMask = -2147483648;
/*      */   static final int singleExpMask = 2139095040;
/*      */   static final int singleFractMask = 8388607;
/*      */   static final int singleExpShift = 23;
/*      */   static final int singleFractHOB = 8388608;
/*      */   static final int singleExpBias = 127;
/*      */   static final int singleMaxDecimalDigits = 7;
/*      */   static final int singleMaxDecimalExponent = 38;
/*      */   static final int singleMinDecimalExponent = -45;
/*      */   static final int intDecimalDigits = 9;
/*      */   private static FDBigInt[] b5p;
/*  991 */   private static ThreadLocal perThreadBuffer = new ThreadLocal() {
/*      */     protected synchronized Object initialValue() {
/*  993 */       return new char[26];
/*      */     }
/*  991 */   };
/*      */ 
/* 1769 */   private static final double[] small10pow = { 1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 10000000.0D, 100000000.0D, 1000000000.0D, 10000000000.0D, 100000000000.0D, 1000000000000.0D, 10000000000000.0D, 100000000000000.0D, 1000000000000000.0D, 10000000000000000.0D, 1.0E+017D, 1.0E+018D, 1.0E+019D, 1.0E+020D, 1.0E+021D, 1.0E+022D };
/*      */ 
/* 1778 */   private static final float[] singleSmall10pow = { 1.0F, 10.0F, 100.0F, 1000.0F, 10000.0F, 100000.0F, 1000000.0F, 10000000.0F, 1.0E+008F, 1.0E+009F, 1.0E+010F };
/*      */ 
/* 1784 */   private static final double[] big10pow = { 10000000000000000.0D, 1.E+032D, 1.0E+064D, 1.E+128D, 1.0E+256D };
/*      */ 
/* 1786 */   private static final double[] tiny10pow = { 1.0E-016D, 1.E-032D, 1.0E-064D, 1.E-128D, 1.0E-256D };
/*      */ 
/* 1789 */   private static final int maxSmallTen = small10pow.length - 1;
/* 1790 */   private static final int singleMaxSmallTen = singleSmall10pow.length - 1;
/*      */ 
/* 1792 */   private static final int[] small5pow = { 1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125 };
/*      */ 
/* 1810 */   private static final long[] long5pow = { 1L, 5L, 25L, 125L, 625L, 3125L, 15625L, 78125L, 390625L, 1953125L, 9765625L, 48828125L, 244140625L, 1220703125L, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L };
/*      */ 
/* 1841 */   private static final int[] n5bits = { 0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61 };
/*      */ 
/* 1871 */   private static final char[] infinity = { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };
/* 1872 */   private static final char[] notANumber = { 'N', 'a', 'N' };
/* 1873 */   private static final char[] zero = { '0', '0', '0', '0', '0', '0', '0', '0' };
/*      */ 
/* 1880 */   private static Pattern hexFloatPattern = null;
/*      */ 
/*      */   private FloatingDecimal(boolean paramBoolean1, int paramInt1, char[] paramArrayOfChar, int paramInt2, boolean paramBoolean2)
/*      */   {
/*   47 */     this.isNegative = paramBoolean1;
/*   48 */     this.isExceptional = paramBoolean2;
/*   49 */     this.decExponent = paramInt1;
/*   50 */     this.digits = paramArrayOfChar;
/*   51 */     this.nDigits = paramInt2;
/*      */   }
/*      */ 
/*      */   private static int countBits(long paramLong)
/*      */   {
/*  110 */     if (paramLong == 0L) return 0;
/*      */ 
/*  112 */     while ((paramLong & 0x0) == 0L) {
/*  113 */       paramLong <<= 8;
/*      */     }
/*  115 */     while (paramLong > 0L) {
/*  116 */       paramLong <<= 1;
/*      */     }
/*      */ 
/*  119 */     int i = 0;
/*  120 */     while ((paramLong & 0xFFFFFFFF) != 0L) {
/*  121 */       paramLong <<= 8;
/*  122 */       i += 8;
/*      */     }
/*  124 */     while (paramLong != 0L) {
/*  125 */       paramLong <<= 1;
/*  126 */       i++;
/*      */     }
/*  128 */     return i;
/*      */   }
/*      */ 
/*      */   private static synchronized FDBigInt big5pow(int paramInt)
/*      */   {
/*  138 */     assert (paramInt >= 0) : paramInt;
/*  139 */     if (b5p == null) {
/*  140 */       b5p = new FDBigInt[paramInt + 1];
/*  141 */     } else if (b5p.length <= paramInt) {
/*  142 */       FDBigInt[] arrayOfFDBigInt = new FDBigInt[paramInt + 1];
/*  143 */       System.arraycopy(b5p, 0, arrayOfFDBigInt, 0, b5p.length);
/*  144 */       b5p = arrayOfFDBigInt;
/*      */     }
/*  146 */     if (b5p[paramInt] != null)
/*  147 */       return b5p[paramInt];
/*  148 */     if (paramInt < small5pow.length)
/*  149 */       return b5p[paramInt] =  = new FDBigInt(small5pow[paramInt]);
/*  150 */     if (paramInt < long5pow.length) {
/*  151 */       return b5p[paramInt] =  = new FDBigInt(long5pow[paramInt]);
/*      */     }
/*      */ 
/*  160 */     int i = paramInt >> 1;
/*  161 */     int j = paramInt - i;
/*  162 */     FDBigInt localFDBigInt1 = b5p[i];
/*  163 */     if (localFDBigInt1 == null)
/*  164 */       localFDBigInt1 = big5pow(i);
/*  165 */     if (j < small5pow.length) {
/*  166 */       return b5p[paramInt] =  = localFDBigInt1.mult(small5pow[j]);
/*      */     }
/*  168 */     FDBigInt localFDBigInt2 = b5p[j];
/*  169 */     if (localFDBigInt2 == null)
/*  170 */       localFDBigInt2 = big5pow(j);
/*  171 */     return b5p[paramInt] =  = localFDBigInt1.mult(localFDBigInt2);
/*      */   }
/*      */ 
/*      */   private static FDBigInt multPow52(FDBigInt paramFDBigInt, int paramInt1, int paramInt2)
/*      */   {
/*  181 */     if (paramInt1 != 0) {
/*  182 */       if (paramInt1 < small5pow.length)
/*  183 */         paramFDBigInt = paramFDBigInt.mult(small5pow[paramInt1]);
/*      */       else {
/*  185 */         paramFDBigInt = paramFDBigInt.mult(big5pow(paramInt1));
/*      */       }
/*      */     }
/*  188 */     if (paramInt2 != 0) {
/*  189 */       paramFDBigInt.lshiftMe(paramInt2);
/*      */     }
/*  191 */     return paramFDBigInt;
/*      */   }
/*      */ 
/*      */   private static FDBigInt constructPow52(int paramInt1, int paramInt2)
/*      */   {
/*  199 */     FDBigInt localFDBigInt = new FDBigInt(big5pow(paramInt1));
/*  200 */     if (paramInt2 != 0) {
/*  201 */       localFDBigInt.lshiftMe(paramInt2);
/*      */     }
/*  203 */     return localFDBigInt;
/*      */   }
/*      */ 
/*      */   private FDBigInt doubleToBigInt(double paramDouble)
/*      */   {
/*  218 */     long l = Double.doubleToLongBits(paramDouble) & 0xFFFFFFFF;
/*  219 */     int i = (int)(l >>> 52);
/*  220 */     l &= 4503599627370495L;
/*  221 */     if (i > 0) {
/*  222 */       l |= 4503599627370496L;
/*      */     } else {
/*  224 */       assert (l != 0L) : l;
/*  225 */       i++;
/*  226 */       while ((l & 0x0) == 0L) {
/*  227 */         l <<= 1;
/*  228 */         i--;
/*      */       }
/*      */     }
/*  231 */     i -= 1023;
/*  232 */     int j = countBits(l);
/*      */ 
/*  237 */     int k = 53 - j;
/*  238 */     l >>>= k;
/*      */ 
/*  240 */     this.bigIntExp = (i + 1 - j);
/*  241 */     this.bigIntNBits = j;
/*  242 */     return new FDBigInt(l);
/*      */   }
/*      */ 
/*      */   private static double ulp(double paramDouble, boolean paramBoolean)
/*      */   {
/*  252 */     long l = Double.doubleToLongBits(paramDouble) & 0xFFFFFFFF;
/*  253 */     int i = (int)(l >>> 52);
/*      */ 
/*  255 */     if ((paramBoolean) && (i >= 52) && ((l & 0xFFFFFFFF) == 0L))
/*      */     {
/*  258 */       i--;
/*      */     }
/*      */     double d;
/*  260 */     if (i > 52)
/*  261 */       d = Double.longBitsToDouble(i - 52 << 52);
/*  262 */     else if (i == 0)
/*  263 */       d = 4.9E-324D;
/*      */     else {
/*  265 */       d = Double.longBitsToDouble(1L << i - 1);
/*      */     }
/*  267 */     if (paramBoolean) d = -d;
/*      */ 
/*  269 */     return d;
/*      */   }
/*      */ 
/*      */   float stickyRound(double paramDouble)
/*      */   {
/*  283 */     long l1 = Double.doubleToLongBits(paramDouble);
/*  284 */     long l2 = l1 & 0x0;
/*  285 */     if ((l2 == 0L) || (l2 == 9218868437227405312L))
/*      */     {
/*  288 */       return (float)paramDouble;
/*      */     }
/*  290 */     l1 += this.roundDir;
/*  291 */     return (float)Double.longBitsToDouble(l1);
/*      */   }
/*      */ 
/*      */   private void developLongDigits(int paramInt, long paramLong1, long paramLong2)
/*      */   {
/*  320 */     for (int m = 0; paramLong2 >= 10L; m++)
/*  321 */       paramLong2 /= 10L;
/*  322 */     if (m != 0) {
/*  323 */       long l1 = long5pow[m] << m;
/*  324 */       long l2 = paramLong1 % l1;
/*  325 */       paramLong1 /= l1;
/*  326 */       paramInt += m;
/*  327 */       if (l2 >= l1 >> 1)
/*      */       {
/*  329 */         paramLong1 += 1L;
/*      */       }
/*      */     }
/*      */     int i;
/*      */     char[] arrayOfChar1;
/*      */     int j;
/*      */     int k;
/*  332 */     if (paramLong1 <= 2147483647L) {
/*  333 */       assert (paramLong1 > 0L) : paramLong1;
/*      */ 
/*  336 */       int n = (int)paramLong1;
/*  337 */       i = 10;
/*  338 */       arrayOfChar1 = (char[])perThreadBuffer.get();
/*  339 */       j = i - 1;
/*  340 */       k = n % 10;
/*  341 */       n /= 10;
/*  342 */       while (k == 0) {
/*  343 */         paramInt++;
/*  344 */         k = n % 10;
/*  345 */         n /= 10;
/*      */       }
/*  347 */       while (n != 0) {
/*  348 */         arrayOfChar1[(j--)] = ((char)(k + 48));
/*  349 */         paramInt++;
/*  350 */         k = n % 10;
/*  351 */         n /= 10;
/*      */       }
/*  353 */       arrayOfChar1[j] = ((char)(k + 48));
/*      */     }
/*      */     else
/*      */     {
/*  357 */       i = 20;
/*  358 */       arrayOfChar1 = (char[])perThreadBuffer.get();
/*  359 */       j = i - 1;
/*  360 */       k = (int)(paramLong1 % 10L);
/*  361 */       paramLong1 /= 10L;
/*  362 */       while (k == 0) {
/*  363 */         paramInt++;
/*  364 */         k = (int)(paramLong1 % 10L);
/*  365 */         paramLong1 /= 10L;
/*      */       }
/*  367 */       while (paramLong1 != 0L) {
/*  368 */         arrayOfChar1[(j--)] = ((char)(k + 48));
/*  369 */         paramInt++;
/*  370 */         k = (int)(paramLong1 % 10L);
/*  371 */         paramLong1 /= 10L;
/*      */       }
/*  373 */       arrayOfChar1[j] = ((char)(k + 48));
/*      */     }
/*      */ 
/*  376 */     i -= j;
/*  377 */     char[] arrayOfChar2 = new char[i];
/*  378 */     System.arraycopy(arrayOfChar1, j, arrayOfChar2, 0, i);
/*  379 */     this.digits = arrayOfChar2;
/*  380 */     this.decExponent = (paramInt + 1);
/*  381 */     this.nDigits = i;
/*      */   }
/*      */ 
/*      */   private void roundup()
/*      */   {
/*      */     int i;
/*  394 */     int j = this.digits[(i = this.nDigits - 1)];
/*  395 */     if (j == 57) {
/*  396 */       while ((j == 57) && (i > 0)) {
/*  397 */         this.digits[i] = '0';
/*  398 */         j = this.digits[(--i)];
/*      */       }
/*  400 */       if (j == 57)
/*      */       {
/*  402 */         this.decExponent += 1;
/*  403 */         this.digits[0] = '1';
/*  404 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  408 */     this.digits[i] = ((char)(j + 1));
/*      */   }
/*      */ 
/*      */   public FloatingDecimal(double paramDouble)
/*      */   {
/*  416 */     long l1 = Double.doubleToLongBits(paramDouble);
/*      */ 
/*  422 */     if ((l1 & 0x0) != 0L) {
/*  423 */       this.isNegative = true;
/*  424 */       l1 ^= -9223372036854775808L;
/*      */     } else {
/*  426 */       this.isNegative = false;
/*      */     }
/*      */ 
/*  430 */     int i = (int)((l1 & 0x0) >> 52);
/*  431 */     long l2 = l1 & 0xFFFFFFFF;
/*  432 */     if (i == 2047) {
/*  433 */       this.isExceptional = true;
/*  434 */       if (l2 == 0L) {
/*  435 */         this.digits = infinity;
/*      */       } else {
/*  437 */         this.digits = notANumber;
/*  438 */         this.isNegative = false;
/*      */       }
/*  440 */       this.nDigits = this.digits.length;
/*  441 */       return;
/*      */     }
/*  443 */     this.isExceptional = false;
/*      */     int j;
/*  448 */     if (i == 0) {
/*  449 */       if (l2 == 0L)
/*      */       {
/*  451 */         this.decExponent = 0;
/*  452 */         this.digits = zero;
/*  453 */         this.nDigits = 1;
/*  454 */         return;
/*      */       }
/*  456 */       while ((l2 & 0x0) == 0L) {
/*  457 */         l2 <<= 1;
/*  458 */         i--;
/*      */       }
/*  460 */       j = 52 + i + 1;
/*  461 */       i++;
/*      */     } else {
/*  463 */       l2 |= 4503599627370496L;
/*  464 */       j = 53;
/*      */     }
/*  466 */     i -= 1023;
/*      */ 
/*  468 */     dtoa(i, l2, j);
/*      */   }
/*      */ 
/*      */   public FloatingDecimal(float paramFloat)
/*      */   {
/*  476 */     int i = Float.floatToIntBits(paramFloat);
/*      */ 
/*  482 */     if ((i & 0x80000000) != 0) {
/*  483 */       this.isNegative = true;
/*  484 */       i ^= -2147483648;
/*      */     } else {
/*  486 */       this.isNegative = false;
/*      */     }
/*      */ 
/*  490 */     int k = (i & 0x7F800000) >> 23;
/*  491 */     int j = i & 0x7FFFFF;
/*  492 */     if (k == 255) {
/*  493 */       this.isExceptional = true;
/*  494 */       if (j == 0L) {
/*  495 */         this.digits = infinity;
/*      */       } else {
/*  497 */         this.digits = notANumber;
/*  498 */         this.isNegative = false;
/*      */       }
/*  500 */       this.nDigits = this.digits.length;
/*  501 */       return;
/*      */     }
/*  503 */     this.isExceptional = false;
/*      */     int m;
/*  508 */     if (k == 0) {
/*  509 */       if (j == 0)
/*      */       {
/*  511 */         this.decExponent = 0;
/*  512 */         this.digits = zero;
/*  513 */         this.nDigits = 1;
/*  514 */         return;
/*      */       }
/*  516 */       while ((j & 0x800000) == 0) {
/*  517 */         j <<= 1;
/*  518 */         k--;
/*      */       }
/*  520 */       m = 23 + k + 1;
/*  521 */       k++;
/*      */     } else {
/*  523 */       j |= 8388608;
/*  524 */       m = 24;
/*      */     }
/*  526 */     k -= 127;
/*      */ 
/*  528 */     dtoa(k, j << 29, m);
/*      */   }
/*      */ 
/*      */   private void dtoa(int paramInt1, long paramLong, int paramInt2)
/*      */   {
/*  541 */     int i = countBits(paramLong);
/*  542 */     int j = Math.max(0, i - paramInt1 - 1);
/*  543 */     if ((paramInt1 <= 62) && (paramInt1 >= -21))
/*      */     {
/*  547 */       if ((j < long5pow.length) && (i + n5bits[j] < 64))
/*      */       {
/*  564 */         if (j == 0)
/*      */         {
/*      */           long l1;
/*  565 */           if (paramInt1 > paramInt2)
/*  566 */             l1 = 1L << paramInt1 - paramInt2 - 1;
/*      */           else {
/*  568 */             l1 = 0L;
/*      */           }
/*  570 */           if (paramInt1 >= 52)
/*  571 */             paramLong <<= paramInt1 - 52;
/*      */           else {
/*  573 */             paramLong >>>= 52 - paramInt1;
/*      */           }
/*  575 */           developLongDigits(0, paramLong, l1);
/*  576 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  627 */     double d = Double.longBitsToDouble(0x0 | paramLong & 0xFFFFFFFF);
/*      */ 
/*  629 */     int k = (int)Math.floor((d - 1.5D) * 0.289529654D + 0.176091259D + paramInt1 * 0.301029995663981D);
/*      */ 
/*  638 */     int n = Math.max(0, -k);
/*  639 */     int m = n + j + paramInt1;
/*      */ 
/*  641 */     int i2 = Math.max(0, k);
/*  642 */     int i1 = i2 + j;
/*      */ 
/*  644 */     int i4 = n;
/*  645 */     int i3 = m - paramInt2;
/*      */ 
/*  655 */     paramLong >>>= 53 - i;
/*  656 */     m -= i - 1;
/*  657 */     int i7 = Math.min(m, i1);
/*  658 */     m -= i7;
/*  659 */     i1 -= i7;
/*  660 */     i3 -= i7;
/*      */ 
/*  668 */     if (i == 1) {
/*  669 */       i3--;
/*      */     }
/*  671 */     if (i3 < 0)
/*      */     {
/*  675 */       m -= i3;
/*  676 */       i1 -= i3;
/*  677 */       i3 = 0;
/*      */     }
/*      */ 
/*  687 */     char[] arrayOfChar = this.digits = new char[18];
/*  688 */     int i8 = 0;
/*      */ 
/*  708 */     int i5 = i + m + (n < n5bits.length ? n5bits[n] : n * 3);
/*  709 */     int i6 = i1 + 1 + (i2 + 1 < n5bits.length ? n5bits[(i2 + 1)] : (i2 + 1) * 3);
/*      */     int i13;
/*      */     int i11;
/*      */     int i9;
/*      */     int i10;
/*      */     long l2;
/*  710 */     if ((i5 < 64) && (i6 < 64)) {
/*  711 */       if ((i5 < 32) && (i6 < 32))
/*      */       {
/*  713 */         int i12 = (int)paramLong * small5pow[n] << m;
/*  714 */         i13 = small5pow[i2] << i1;
/*  715 */         int i14 = small5pow[i4] << i3;
/*  716 */         int i15 = i13 * 10;
/*      */ 
/*  722 */         i8 = 0;
/*  723 */         i11 = i12 / i13;
/*  724 */         i12 = 10 * (i12 % i13);
/*  725 */         i14 *= 10;
/*  726 */         i9 = i12 < i14 ? 1 : 0;
/*  727 */         i10 = i12 + i14 > i15 ? 1 : 0;
/*  728 */         assert (i11 < 10) : i11;
/*  729 */         if ((i11 == 0) && (i10 == 0))
/*      */         {
/*  731 */           k--;
/*      */         }
/*  733 */         else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  741 */         if ((k < -3) || (k >= 8)) {
/*  742 */           i10 = i9 = 0;
/*      */         }
/*  744 */         while ((i9 == 0) && (i10 == 0)) {
/*  745 */           i11 = i12 / i13;
/*  746 */           i12 = 10 * (i12 % i13);
/*  747 */           i14 *= 10;
/*  748 */           assert (i11 < 10) : i11;
/*  749 */           if (i14 > 0L) {
/*  750 */             i9 = i12 < i14 ? 1 : 0;
/*  751 */             i10 = i12 + i14 > i15 ? 1 : 0;
/*      */           }
/*      */           else
/*      */           {
/*  758 */             i9 = 1;
/*  759 */             i10 = 1;
/*      */           }
/*  761 */           arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */         }
/*  763 */         l2 = (i12 << 1) - i15;
/*      */       }
/*      */       else {
/*  766 */         long l3 = paramLong * long5pow[n] << m;
/*  767 */         long l4 = long5pow[i2] << i1;
/*  768 */         long l5 = long5pow[i4] << i3;
/*  769 */         long l6 = l4 * 10L;
/*      */ 
/*  775 */         i8 = 0;
/*  776 */         i11 = (int)(l3 / l4);
/*  777 */         l3 = 10L * (l3 % l4);
/*  778 */         l5 *= 10L;
/*  779 */         i9 = l3 < l5 ? 1 : 0;
/*  780 */         i10 = l3 + l5 > l6 ? 1 : 0;
/*  781 */         assert (i11 < 10) : i11;
/*  782 */         if ((i11 == 0) && (i10 == 0))
/*      */         {
/*  784 */           k--;
/*      */         }
/*  786 */         else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  794 */         if ((k < -3) || (k >= 8)) {
/*  795 */           i10 = i9 = 0;
/*      */         }
/*  797 */         while ((i9 == 0) && (i10 == 0)) {
/*  798 */           i11 = (int)(l3 / l4);
/*  799 */           l3 = 10L * (l3 % l4);
/*  800 */           l5 *= 10L;
/*  801 */           assert (i11 < 10) : i11;
/*  802 */           if (l5 > 0L) {
/*  803 */             i9 = l3 < l5 ? 1 : 0;
/*  804 */             i10 = l3 + l5 > l6 ? 1 : 0;
/*      */           }
/*      */           else
/*      */           {
/*  811 */             i9 = 1;
/*  812 */             i10 = 1;
/*      */           }
/*  814 */           arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */         }
/*  816 */         l2 = (l3 << 1) - l6;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  826 */       FDBigInt localFDBigInt2 = multPow52(new FDBigInt(paramLong), n, m);
/*  827 */       FDBigInt localFDBigInt1 = constructPow52(i2, i1);
/*  828 */       FDBigInt localFDBigInt3 = constructPow52(i4, i3);
/*      */ 
/*  832 */       localFDBigInt2.lshiftMe(i13 = localFDBigInt1.normalizeMe());
/*  833 */       localFDBigInt3.lshiftMe(i13);
/*  834 */       FDBigInt localFDBigInt4 = localFDBigInt1.mult(10);
/*      */ 
/*  840 */       i8 = 0;
/*  841 */       i11 = localFDBigInt2.quoRemIteration(localFDBigInt1);
/*  842 */       localFDBigInt3 = localFDBigInt3.mult(10);
/*  843 */       i9 = localFDBigInt2.cmp(localFDBigInt3) < 0 ? 1 : 0;
/*  844 */       i10 = localFDBigInt2.add(localFDBigInt3).cmp(localFDBigInt4) > 0 ? 1 : 0;
/*  845 */       assert (i11 < 10) : i11;
/*  846 */       if ((i11 == 0) && (i10 == 0))
/*      */       {
/*  848 */         k--;
/*      */       }
/*  850 */       else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  858 */       if ((k < -3) || (k >= 8)) {
/*  859 */         i10 = i9 = 0;
/*      */       }
/*  861 */       while ((i9 == 0) && (i10 == 0)) {
/*  862 */         i11 = localFDBigInt2.quoRemIteration(localFDBigInt1);
/*  863 */         localFDBigInt3 = localFDBigInt3.mult(10);
/*  864 */         assert (i11 < 10) : i11;
/*  865 */         i9 = localFDBigInt2.cmp(localFDBigInt3) < 0 ? 1 : 0;
/*  866 */         i10 = localFDBigInt2.add(localFDBigInt3).cmp(localFDBigInt4) > 0 ? 1 : 0;
/*  867 */         arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */       }
/*  869 */       if ((i10 != 0) && (i9 != 0)) {
/*  870 */         localFDBigInt2.lshiftMe(1);
/*  871 */         l2 = localFDBigInt2.cmp(localFDBigInt4);
/*      */       } else {
/*  873 */         l2 = 0L;
/*      */       }
/*      */     }
/*  875 */     this.decExponent = (k + 1);
/*  876 */     this.digits = arrayOfChar;
/*  877 */     this.nDigits = i8;
/*      */ 
/*  881 */     if (i10 != 0)
/*  882 */       if (i9 != 0) {
/*  883 */         if (l2 == 0L)
/*      */         {
/*  886 */           if ((arrayOfChar[(this.nDigits - 1)] & 0x1) != 0) roundup(); 
/*      */         }
/*  887 */         else if (l2 > 0L)
/*  888 */           roundup();
/*      */       }
/*      */       else
/*  891 */         roundup();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  899 */     StringBuffer localStringBuffer = new StringBuffer(this.nDigits + 8);
/*  900 */     if (this.isNegative) localStringBuffer.append('-');
/*  901 */     if (this.isExceptional) {
/*  902 */       localStringBuffer.append(this.digits, 0, this.nDigits);
/*      */     } else {
/*  904 */       localStringBuffer.append("0.");
/*  905 */       localStringBuffer.append(this.digits, 0, this.nDigits);
/*  906 */       localStringBuffer.append('e');
/*  907 */       localStringBuffer.append(this.decExponent);
/*      */     }
/*  909 */     return new String(localStringBuffer);
/*      */   }
/*      */ 
/*      */   public String toJavaFormatString() {
/*  913 */     char[] arrayOfChar = (char[])perThreadBuffer.get();
/*  914 */     int i = getChars(arrayOfChar);
/*  915 */     return new String(arrayOfChar, 0, i);
/*      */   }
/*      */ 
/*      */   private int getChars(char[] paramArrayOfChar) {
/*  919 */     assert (this.nDigits <= 19) : this.nDigits;
/*  920 */     int i = 0;
/*  921 */     if (this.isNegative) { paramArrayOfChar[0] = '-'; i = 1; }
/*  922 */     if (this.isExceptional) {
/*  923 */       System.arraycopy(this.digits, 0, paramArrayOfChar, i, this.nDigits);
/*  924 */       i += this.nDigits;
/*      */     }
/*      */     else
/*      */     {
/*      */       int j;
/*  926 */       if ((this.decExponent > 0) && (this.decExponent < 8))
/*      */       {
/*  928 */         j = Math.min(this.nDigits, this.decExponent);
/*  929 */         System.arraycopy(this.digits, 0, paramArrayOfChar, i, j);
/*  930 */         i += j;
/*  931 */         if (j < this.decExponent) {
/*  932 */           j = this.decExponent - j;
/*  933 */           System.arraycopy(zero, 0, paramArrayOfChar, i, j);
/*  934 */           i += j;
/*  935 */           paramArrayOfChar[(i++)] = '.';
/*  936 */           paramArrayOfChar[(i++)] = '0';
/*      */         } else {
/*  938 */           paramArrayOfChar[(i++)] = '.';
/*  939 */           if (j < this.nDigits) {
/*  940 */             int k = this.nDigits - j;
/*  941 */             System.arraycopy(this.digits, j, paramArrayOfChar, i, k);
/*  942 */             i += k;
/*      */           } else {
/*  944 */             paramArrayOfChar[(i++)] = '0';
/*      */           }
/*      */         }
/*  947 */       } else if ((this.decExponent <= 0) && (this.decExponent > -3)) {
/*  948 */         paramArrayOfChar[(i++)] = '0';
/*  949 */         paramArrayOfChar[(i++)] = '.';
/*  950 */         if (this.decExponent != 0) {
/*  951 */           System.arraycopy(zero, 0, paramArrayOfChar, i, -this.decExponent);
/*  952 */           i -= this.decExponent;
/*      */         }
/*  954 */         System.arraycopy(this.digits, 0, paramArrayOfChar, i, this.nDigits);
/*  955 */         i += this.nDigits;
/*      */       } else {
/*  957 */         paramArrayOfChar[(i++)] = this.digits[0];
/*  958 */         paramArrayOfChar[(i++)] = '.';
/*  959 */         if (this.nDigits > 1) {
/*  960 */           System.arraycopy(this.digits, 1, paramArrayOfChar, i, this.nDigits - 1);
/*  961 */           i += this.nDigits - 1;
/*      */         } else {
/*  963 */           paramArrayOfChar[(i++)] = '0';
/*      */         }
/*  965 */         paramArrayOfChar[(i++)] = 'E';
/*      */ 
/*  967 */         if (this.decExponent <= 0) {
/*  968 */           paramArrayOfChar[(i++)] = '-';
/*  969 */           j = -this.decExponent + 1;
/*      */         } else {
/*  971 */           j = this.decExponent - 1;
/*      */         }
/*      */ 
/*  974 */         if (j <= 9) {
/*  975 */           paramArrayOfChar[(i++)] = ((char)(j + 48));
/*  976 */         } else if (j <= 99) {
/*  977 */           paramArrayOfChar[(i++)] = ((char)(j / 10 + 48));
/*  978 */           paramArrayOfChar[(i++)] = ((char)(j % 10 + 48));
/*      */         } else {
/*  980 */           paramArrayOfChar[(i++)] = ((char)(j / 100 + 48));
/*  981 */           j %= 100;
/*  982 */           paramArrayOfChar[(i++)] = ((char)(j / 10 + 48));
/*  983 */           paramArrayOfChar[(i++)] = ((char)(j % 10 + 48));
/*      */         }
/*      */       }
/*      */     }
/*  987 */     return i;
/*      */   }
/*      */ 
/*      */   public void appendTo(Appendable paramAppendable)
/*      */   {
/*  998 */     char[] arrayOfChar = (char[])perThreadBuffer.get();
/*  999 */     int i = getChars(arrayOfChar);
/* 1000 */     if ((paramAppendable instanceof StringBuilder))
/* 1001 */       ((StringBuilder)paramAppendable).append(arrayOfChar, 0, i);
/* 1002 */     else if ((paramAppendable instanceof StringBuffer)) {
/* 1003 */       ((StringBuffer)paramAppendable).append(arrayOfChar, 0, i);
/*      */     }
/* 1005 */     else if (!$assertionsDisabled) throw new AssertionError(); 
/*      */   }
/*      */ 
/*      */   public static FloatingDecimal readJavaFormatString(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/* 1010 */     boolean bool = false;
/* 1011 */     int i = 0;
/*      */     try
/*      */     {
/* 1017 */       paramString = paramString.trim();
/*      */ 
/* 1019 */       int m = paramString.length();
/* 1020 */       if (m == 0) throw new NumberFormatException("empty String");
/* 1021 */       int n = 0;
/* 1022 */       switch (k = paramString.charAt(n)) {
/*      */       case '-':
/* 1024 */         bool = true;
/*      */       case '+':
/* 1027 */         n++;
/* 1028 */         i = 1;
/*      */       }
/*      */ 
/* 1032 */       int k = paramString.charAt(n);
/*      */       int i1;
/*      */       int i3;
/* 1033 */       if ((k == 78) || (k == 73)) {
/* 1034 */         i1 = 0;
/* 1035 */         char[] arrayOfChar2 = null;
/*      */ 
/* 1037 */         if (k == 78) {
/* 1038 */           arrayOfChar2 = notANumber;
/* 1039 */           i1 = 1;
/*      */         } else {
/* 1041 */           arrayOfChar2 = infinity;
/*      */         }
/*      */ 
/* 1045 */         i3 = 0;
/* 1046 */         while ((n < m) && (i3 < arrayOfChar2.length)) {
/* 1047 */           if (paramString.charAt(n) == arrayOfChar2[i3]) {
/* 1048 */             n++; i3++;
/*      */           }
/*      */           else {
/* 1051 */             break label824;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1058 */         if ((i3 == arrayOfChar2.length) && (n == m)) {
/* 1059 */           return i1 != 0 ? new FloatingDecimal((0.0D / 0.0D)) : new FloatingDecimal(bool ? (-1.0D / 0.0D) : (1.0D / 0.0D));
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1068 */         if ((k == 48) && 
/* 1069 */           (m > n + 1)) {
/* 1070 */           i1 = paramString.charAt(n + 1);
/* 1071 */           if ((i1 == 120) || (i1 == 88)) {
/* 1072 */             return parseHexString(paramString);
/*      */           }
/*      */         }
/*      */ 
/* 1076 */         char[] arrayOfChar1 = new char[m];
/* 1077 */         int i2 = 0;
/* 1078 */         i3 = 0;
/* 1079 */         int i4 = 0;
/* 1080 */         int i5 = 0;
/* 1081 */         int i6 = 0;
/*      */ 
/* 1083 */         while (n < m) {
/* 1084 */           switch (k = paramString.charAt(n)) {
/*      */           case '0':
/* 1086 */             if (i2 > 0)
/* 1087 */               i6++;
/*      */             else {
/* 1089 */               i5++;
/*      */             }
/* 1091 */             break;
/*      */           case '1':
/*      */           case '2':
/*      */           case '3':
/*      */           case '4':
/*      */           case '5':
/*      */           case '6':
/*      */           case '7':
/*      */           case '8':
/*      */           case '9':
/* 1101 */             while (i6 > 0) {
/* 1102 */               arrayOfChar1[(i2++)] = '0';
/* 1103 */               i6--;
/*      */             }
/* 1105 */             arrayOfChar1[(i2++)] = k;
/* 1106 */             break;
/*      */           case '.':
/* 1108 */             if (i3 != 0)
/*      */             {
/* 1110 */               throw new NumberFormatException("multiple points");
/*      */             }
/* 1112 */             i4 = n;
/* 1113 */             if (i != 0) {
/* 1114 */               i4--;
/*      */             }
/* 1116 */             i3 = 1;
/* 1117 */             break;
/*      */           case '/':
/*      */           default:
/* 1119 */             break;
/*      */           }
/* 1121 */           n++;
/*      */         }
/*      */ 
/* 1142 */         if (i2 == 0) {
/* 1143 */           arrayOfChar1 = zero;
/* 1144 */           i2 = 1;
/* 1145 */           if (i5 == 0);
/*      */         }
/*      */         else
/*      */         {
/*      */           int j;
/* 1158 */           if (i3 != 0)
/* 1159 */             j = i4 - i5;
/*      */           else {
/* 1161 */             j = i2 + i6;
/*      */           }
/*      */ 
/* 1167 */           if ((n < m) && (((k = paramString.charAt(n)) == 'e') || (k == 69))) {
/* 1168 */             int i7 = 1;
/* 1169 */             int i8 = 0;
/* 1170 */             int i9 = 214748364;
/* 1171 */             int i10 = 0;
/* 1172 */             switch (paramString.charAt(++n)) {
/*      */             case '-':
/* 1174 */               i7 = -1;
/*      */             case '+':
/* 1177 */               n++;
/*      */             }
/* 1179 */             int i11 = n;
/*      */ 
/* 1181 */             while (n < m) {
/* 1182 */               if (i8 >= i9)
/*      */               {
/* 1185 */                 i10 = 1;
/*      */               }
/* 1187 */               switch (k = paramString.charAt(n++)) {
/*      */               case '0':
/*      */               case '1':
/*      */               case '2':
/*      */               case '3':
/*      */               case '4':
/*      */               case '5':
/*      */               case '6':
/*      */               case '7':
/*      */               case '8':
/*      */               case '9':
/* 1198 */                 i8 = i8 * 10 + (k - 48);
/* 1199 */                 break;
/*      */               default:
/* 1201 */                 n--;
/*      */               }
/*      */             }
/*      */ 
/* 1205 */             int i12 = 324 + i2 + i6;
/* 1206 */             if ((i10 != 0) || (i8 > i12))
/*      */             {
/* 1219 */               j = i7 * i12;
/*      */             }
/*      */             else
/*      */             {
/* 1223 */               j += i7 * i8;
/*      */             }
/*      */ 
/* 1232 */             if (n == i11);
/*      */           }
/* 1239 */           else if ((n >= m) || ((n != m - 1) || ((paramString.charAt(n) == 'f') || (paramString.charAt(n) == 'F') || (paramString.charAt(n) == 'd') || (paramString.charAt(n) == 'D'))))
/*      */           {
/* 1248 */             return new FloatingDecimal(bool, j, arrayOfChar1, i2, false); }  } 
/*      */       } } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {  }
/*      */ 
/* 1250 */     label824: throw new NumberFormatException("For input string: \"" + paramString + "\"");
/*      */   }
/*      */ 
/*      */   public strictfp double doubleValue()
/*      */   {
/* 1263 */     int i = Math.min(this.nDigits, 16);
/*      */ 
/* 1269 */     if ((this.digits == infinity) || (this.digits == notANumber)) {
/* 1270 */       if (this.digits == notANumber) {
/* 1271 */         return (0.0D / 0.0D);
/*      */       }
/* 1273 */       return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */     }
/*      */ 
/* 1276 */     if (this.mustSetRoundDir) {
/* 1277 */       this.roundDir = 0;
/*      */     }
/*      */ 
/* 1283 */     int j = this.digits[0] - '0';
/* 1284 */     int k = Math.min(i, 9);
/* 1285 */     for (int m = 1; m < k; m++) {
/* 1286 */       j = j * 10 + this.digits[m] - 48;
/*      */     }
/* 1288 */     long l = j;
/* 1289 */     for (m = k; m < i; m++) {
/* 1290 */       l = l * 10L + (this.digits[m] - '0');
/*      */     }
/* 1292 */     double d1 = l;
/* 1293 */     m = this.decExponent - i;
/*      */     int n;
/* 1300 */     if (this.nDigits <= 15)
/*      */     {
/* 1311 */       if ((m == 0) || (d1 == 0.0D))
/* 1312 */         return this.isNegative ? -d1 : d1;
/*      */       double d2;
/*      */       double d3;
/* 1313 */       if (m >= 0) {
/* 1314 */         if (m <= maxSmallTen)
/*      */         {
/* 1319 */           d2 = d1 * small10pow[m];
/* 1320 */           if (this.mustSetRoundDir) {
/* 1321 */             d3 = d2 / small10pow[m];
/* 1322 */             this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */           }
/*      */ 
/* 1326 */           return this.isNegative ? -d2 : d2;
/*      */         }
/* 1328 */         n = 15 - i;
/* 1329 */         if (m <= maxSmallTen + n)
/*      */         {
/* 1336 */           d1 *= small10pow[n];
/* 1337 */           d2 = d1 * small10pow[(m - n)];
/*      */ 
/* 1339 */           if (this.mustSetRoundDir) {
/* 1340 */             d3 = d2 / small10pow[(m - n)];
/* 1341 */             this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */           }
/*      */ 
/* 1345 */           return this.isNegative ? -d2 : d2;
/*      */         }
/*      */ 
/*      */       }
/* 1351 */       else if (m >= -maxSmallTen)
/*      */       {
/* 1355 */         d2 = d1 / small10pow[(-m)];
/* 1356 */         d3 = d2 * small10pow[(-m)];
/* 1357 */         if (this.mustSetRoundDir) {
/* 1358 */           this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */         }
/*      */ 
/* 1362 */         return this.isNegative ? -d2 : d2;
/*      */       }
/*      */     }
/*      */     double d4;
/* 1378 */     if (m > 0) {
/* 1379 */       if (this.decExponent > 309)
/*      */       {
/* 1384 */         return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */       }
/* 1386 */       if ((m & 0xF) != 0) {
/* 1387 */         d1 *= small10pow[(m & 0xF)];
/*      */       }
/* 1389 */       if (m >>= 4 != 0)
/*      */       {
/* 1391 */         for (n = 0; m > 1; m >>= 1) {
/* 1392 */           if ((m & 0x1) != 0)
/* 1393 */             d1 *= big10pow[n];
/* 1391 */           n++;
/*      */         }
/*      */ 
/* 1401 */         d4 = d1 * big10pow[n];
/* 1402 */         if (Double.isInfinite(d4))
/*      */         {
/* 1415 */           d4 = d1 / 2.0D;
/* 1416 */           d4 *= big10pow[n];
/* 1417 */           if (Double.isInfinite(d4)) {
/* 1418 */             return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */           }
/* 1420 */           d4 = 1.7976931348623157E+308D;
/*      */         }
/* 1422 */         d1 = d4;
/*      */       }
/* 1424 */     } else if (m < 0) {
/* 1425 */       m = -m;
/* 1426 */       if (this.decExponent < -325)
/*      */       {
/* 1431 */         return this.isNegative ? -0.0D : 0.0D;
/*      */       }
/* 1433 */       if ((m & 0xF) != 0) {
/* 1434 */         d1 /= small10pow[(m & 0xF)];
/*      */       }
/* 1436 */       if (m >>= 4 != 0)
/*      */       {
/* 1438 */         for (n = 0; m > 1; m >>= 1) {
/* 1439 */           if ((m & 0x1) != 0)
/* 1440 */             d1 *= tiny10pow[n];
/* 1438 */           n++;
/*      */         }
/*      */ 
/* 1448 */         d4 = d1 * tiny10pow[n];
/* 1449 */         if (d4 == 0.0D)
/*      */         {
/* 1462 */           d4 = d1 * 2.0D;
/* 1463 */           d4 *= tiny10pow[n];
/* 1464 */           if (d4 == 0.0D) {
/* 1465 */             return this.isNegative ? -0.0D : 0.0D;
/*      */           }
/* 1467 */           d4 = 4.9E-324D;
/*      */         }
/* 1469 */         d1 = d4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1480 */     if (this.nDigits > 1100) {
/* 1481 */       this.nDigits = 1101;
/* 1482 */       this.digits[1100] = '1';
/*      */     }
/* 1484 */     FDBigInt localFDBigInt1 = new FDBigInt(l, this.digits, i, this.nDigits);
/* 1485 */     m = this.decExponent - this.nDigits;
/*      */     while (true)
/*      */     {
/* 1492 */       FDBigInt localFDBigInt2 = doubleToBigInt(d1);
/*      */       int i2;
/*      */       int i1;
/*      */       int i4;
/*      */       int i3;
/* 1506 */       if (m >= 0) {
/* 1507 */         i1 = i2 = 0;
/* 1508 */         i3 = i4 = m;
/*      */       } else {
/* 1510 */         i1 = i2 = -m;
/* 1511 */         i3 = i4 = 0;
/*      */       }
/* 1513 */       if (this.bigIntExp >= 0)
/* 1514 */         i1 += this.bigIntExp;
/*      */       else {
/* 1516 */         i3 -= this.bigIntExp;
/*      */       }
/* 1518 */       int i5 = i1;
/*      */       int i6;
/* 1522 */       if (this.bigIntExp + this.bigIntNBits <= -1022)
/*      */       {
/* 1526 */         i6 = this.bigIntExp + 1023 + 52;
/*      */       }
/* 1528 */       else i6 = 54 - this.bigIntNBits;
/*      */ 
/* 1530 */       i1 += i6;
/* 1531 */       i3 += i6;
/*      */ 
/* 1534 */       int i7 = Math.min(i1, Math.min(i3, i5));
/* 1535 */       i1 -= i7;
/* 1536 */       i3 -= i7;
/* 1537 */       i5 -= i7;
/*      */ 
/* 1539 */       localFDBigInt2 = multPow52(localFDBigInt2, i2, i1);
/* 1540 */       FDBigInt localFDBigInt3 = multPow52(new FDBigInt(localFDBigInt1), i4, i3);
/*      */       int i8;
/*      */       boolean bool;
/*      */       FDBigInt localFDBigInt4;
/* 1558 */       if ((i8 = localFDBigInt2.cmp(localFDBigInt3)) > 0) {
/* 1559 */         bool = true;
/* 1560 */         localFDBigInt4 = localFDBigInt2.sub(localFDBigInt3);
/* 1561 */         if ((this.bigIntNBits == 1) && (this.bigIntExp > -1022))
/*      */         {
/* 1566 */           i5--;
/* 1567 */           if (i5 < 0)
/*      */           {
/* 1570 */             i5 = 0;
/* 1571 */             localFDBigInt4.lshiftMe(1);
/*      */           }
/*      */         }
/*      */       } else { if (i8 >= 0) break;
/* 1575 */         bool = false;
/* 1576 */         localFDBigInt4 = localFDBigInt3.sub(localFDBigInt2);
/*      */       }
/*      */ 
/* 1582 */       FDBigInt localFDBigInt5 = constructPow52(i2, i5);
/* 1583 */       if ((i8 = localFDBigInt4.cmp(localFDBigInt5)) < 0)
/*      */       {
/* 1586 */         if (this.mustSetRoundDir) {
/* 1587 */           this.roundDir = (bool ? -1 : 1);
/*      */         }
/*      */       }
/* 1590 */       else if (i8 == 0)
/*      */       {
/* 1593 */         d1 += 0.5D * ulp(d1, bool);
/*      */ 
/* 1595 */         if (this.mustSetRoundDir) {
/* 1596 */           this.roundDir = (bool ? -1 : 1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1604 */         d1 += ulp(d1, bool);
/* 1605 */         if (d1 != 0.0D) if (d1 == (1.0D / 0.0D)) {
/* 1606 */             break;
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/* 1611 */     return this.isNegative ? -d1 : d1;
/*      */   }
/*      */ 
/*      */   public strictfp float floatValue()
/*      */   {
/* 1626 */     int i = Math.min(this.nDigits, 8);
/*      */ 
/* 1631 */     if ((this.digits == infinity) || (this.digits == notANumber)) {
/* 1632 */       if (this.digits == notANumber) {
/* 1633 */         return (0.0F / 0.0F);
/*      */       }
/* 1635 */       return this.isNegative ? (1.0F / -1.0F) : (1.0F / 1.0F);
/*      */     }
/*      */ 
/* 1641 */     int j = this.digits[0] - '0';
/* 1642 */     for (int k = 1; k < i; k++) {
/* 1643 */       j = j * 10 + this.digits[k] - 48;
/*      */     }
/* 1645 */     float f = j;
/* 1646 */     k = this.decExponent - i;
/*      */ 
/* 1653 */     if (this.nDigits <= 7)
/*      */     {
/* 1664 */       if ((k == 0) || (f == 0.0F))
/* 1665 */         return this.isNegative ? -f : f;
/* 1666 */       if (k >= 0) {
/* 1667 */         if (k <= singleMaxSmallTen)
/*      */         {
/* 1672 */           f *= singleSmall10pow[k];
/* 1673 */           return this.isNegative ? -f : f;
/*      */         }
/* 1675 */         int m = 7 - i;
/* 1676 */         if (k <= singleMaxSmallTen + m)
/*      */         {
/* 1683 */           f *= singleSmall10pow[m];
/* 1684 */           f *= singleSmall10pow[(k - m)];
/* 1685 */           return this.isNegative ? -f : f;
/*      */         }
/*      */ 
/*      */       }
/* 1691 */       else if (k >= -singleMaxSmallTen)
/*      */       {
/* 1695 */         f /= singleSmall10pow[(-k)];
/* 1696 */         return this.isNegative ? -f : f;
/*      */       }
/*      */ 
/*      */     }
/* 1702 */     else if ((this.decExponent >= this.nDigits) && (this.nDigits + this.decExponent <= 15))
/*      */     {
/* 1712 */       long l = j;
/* 1713 */       for (int n = i; n < this.nDigits; n++) {
/* 1714 */         l = l * 10L + (this.digits[n] - '0');
/*      */       }
/* 1716 */       double d2 = l;
/* 1717 */       k = this.decExponent - this.nDigits;
/* 1718 */       d2 *= small10pow[k];
/* 1719 */       f = (float)d2;
/* 1720 */       return this.isNegative ? -f : f;
/*      */     }
/*      */ 
/* 1732 */     if (this.decExponent > 39)
/*      */     {
/* 1737 */       return this.isNegative ? (1.0F / -1.0F) : (1.0F / 1.0F);
/* 1738 */     }if (this.decExponent < -46)
/*      */     {
/* 1743 */       return this.isNegative ? -0.0F : 0.0F;
/*      */     }
/*      */ 
/* 1758 */     this.mustSetRoundDir = (!this.fromHex);
/* 1759 */     double d1 = doubleValue();
/* 1760 */     return stickyRound(d1);
/*      */   }
/*      */ 
/*      */   private static synchronized Pattern getHexFloatPattern()
/*      */   {
/* 1882 */     if (hexFloatPattern == null) {
/* 1883 */       hexFloatPattern = Pattern.compile("([-+])?0[xX](((\\p{XDigit}+)\\.?)|((\\p{XDigit}*)\\.(\\p{XDigit}+)))[pP]([-+])?(\\p{Digit}+)[fFdD]?");
/*      */     }
/*      */ 
/* 1888 */     return hexFloatPattern;
/*      */   }
/*      */ 
/*      */   static FloatingDecimal parseHexString(String paramString)
/*      */   {
/* 1899 */     Matcher localMatcher = getHexFloatPattern().matcher(paramString);
/* 1900 */     boolean bool = localMatcher.matches();
/*      */ 
/* 1902 */     if (!bool)
/*      */     {
/* 1904 */       throw new NumberFormatException("For input string: \"" + paramString + "\"");
/*      */     }
/*      */ 
/* 1931 */     String str1 = localMatcher.group(1);
/* 1932 */     double d = (str1 == null) || (str1.equals("+")) ? 1.0D : -1.0D;
/*      */ 
/* 1964 */     String str2 = null;
/* 1965 */     int i = 0;
/* 1966 */     int j = 0;
/*      */ 
/* 1968 */     int k = 0;
/*      */ 
/* 1971 */     int m = 0;
/*      */     String str4;
/* 1985 */     if ((str4 = localMatcher.group(4)) != null)
/*      */     {
/* 1987 */       str2 = stripLeadingZeros(str4);
/* 1988 */       k = str2.length();
/*      */     }
/*      */     else
/*      */     {
/* 1993 */       String str5 = stripLeadingZeros(localMatcher.group(6));
/* 1994 */       k = str5.length();
/*      */ 
/* 1997 */       String str6 = localMatcher.group(7);
/* 1998 */       m = str6.length();
/*      */ 
/* 2001 */       str2 = (str5 == null ? "" : str5) + str6;
/*      */     }
/*      */ 
/* 2007 */     str2 = stripLeadingZeros(str2);
/* 2008 */     i = str2.length();
/*      */ 
/* 2013 */     if (k >= 1)
/* 2014 */       j = 4 * (k - 1);
/*      */     else {
/* 2016 */       j = -4 * (m - i + 1);
/*      */     }
/*      */ 
/* 2022 */     if (i == 0) {
/* 2023 */       return new FloatingDecimal(d * 0.0D);
/*      */     }
/*      */ 
/* 2035 */     String str3 = localMatcher.group(8);
/* 2036 */     m = (str3 == null) || (str3.equals("+")) ? 1 : 0;
/*      */     long l1;
/*      */     try
/*      */     {
/* 2039 */       l1 = Integer.parseInt(localMatcher.group(9));
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/* 2055 */       return new FloatingDecimal(d * (m != 0 ? (1.0D / 0.0D) : 0.0D));
/*      */     }
/*      */ 
/* 2059 */     long l2 = (m != 0 ? 1L : -1L) * l1;
/*      */ 
/* 2064 */     long l3 = l2 + j;
/*      */ 
/* 2070 */     int n = 0;
/* 2071 */     int i1 = 0;
/* 2072 */     int i2 = 0;
/* 2073 */     int i3 = 0;
/* 2074 */     long l4 = 0L;
/*      */ 
/* 2081 */     long l5 = getHexDigit(str2, 0);
/*      */ 
/* 2092 */     if (l5 == 1L) {
/* 2093 */       l4 |= l5 << 52;
/* 2094 */       i3 = 48;
/*      */     }
/* 2096 */     else if (l5 <= 3L) {
/* 2097 */       l4 |= l5 << 51;
/* 2098 */       i3 = 47;
/* 2099 */       l3 += 1L;
/*      */     }
/* 2101 */     else if (l5 <= 7L) {
/* 2102 */       l4 |= l5 << 50;
/* 2103 */       i3 = 46;
/* 2104 */       l3 += 2L;
/*      */     }
/* 2106 */     else if (l5 <= 15L) {
/* 2107 */       l4 |= l5 << 49;
/* 2108 */       i3 = 45;
/* 2109 */       l3 += 3L;
/*      */     } else {
/* 2111 */       throw new AssertionError("Result from digit conversion too large!");
/*      */     }
/*      */ 
/* 2133 */     int i4 = 0;
/*      */     long l6;
/* 2134 */     for (i4 = 1; 
/* 2135 */       (i4 < i) && (i3 >= 0); 
/* 2136 */       i4++) {
/* 2137 */       l6 = getHexDigit(str2, i4);
/* 2138 */       l4 |= l6 << i3;
/* 2139 */       i3 -= 4;
/*      */     }
/*      */ 
/* 2147 */     if (i4 < i) {
/* 2148 */       l6 = getHexDigit(str2, i4);
/*      */ 
/* 2152 */       switch (i3)
/*      */       {
/*      */       case -1:
/* 2156 */         l4 |= (l6 & 0xE) >> 1;
/* 2157 */         n = (l6 & 1L) != 0L ? 1 : 0;
/* 2158 */         break;
/*      */       case -2:
/* 2163 */         l4 |= (l6 & 0xC) >> 2;
/* 2164 */         n = (l6 & 0x2) != 0L ? 1 : 0;
/* 2165 */         i1 = (l6 & 1L) != 0L ? 1 : 0;
/* 2166 */         break;
/*      */       case -3:
/* 2170 */         l4 |= (l6 & 0x8) >> 3;
/*      */ 
/* 2172 */         n = (l6 & 0x4) != 0L ? 1 : 0;
/* 2173 */         i1 = (l6 & 0x3) != 0L ? 1 : 0;
/* 2174 */         break;
/*      */       case -4:
/* 2179 */         n = (l6 & 0x8) != 0L ? 1 : 0;
/*      */ 
/* 2181 */         i1 = (l6 & 0x7) != 0L ? 1 : 0;
/* 2182 */         break;
/*      */       default:
/* 2185 */         throw new AssertionError("Unexpected shift distance remainder.");
/*      */       }
/*      */ 
/* 2194 */       i4++;
/* 2195 */       while ((i4 < i) && (i1 == 0)) {
/* 2196 */         l6 = getHexDigit(str2, i4);
/* 2197 */         i1 = (i1 != 0) || (l6 != 0L) ? 1 : 0;
/* 2198 */         i4++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2208 */     if (l3 > 1023L)
/*      */     {
/* 2210 */       return new FloatingDecimal(d * (1.0D / 0.0D));
/*      */     }
/* 2212 */     if ((l3 <= 1023L) && (l3 >= -1022L))
/*      */     {
/* 2226 */       l4 = l3 + 1023L << 52 & 0x0 | 0xFFFFFFFF & l4;
/*      */     }
/*      */     else
/*      */     {
/* 2235 */       if (l3 < -1075L)
/*      */       {
/* 2239 */         return new FloatingDecimal(d * 0.0D);
/*      */       }
/*      */ 
/* 2247 */       i1 = (i1 != 0) || (n != 0) ? 1 : 0;
/* 2248 */       n = 0;
/*      */ 
/* 2256 */       i5 = 53 - ((int)l3 - -1074 + 1);
/*      */ 
/* 2258 */       assert ((i5 >= 1) && (i5 <= 53));
/*      */ 
/* 2262 */       n = (l4 & 1L << i5 - 1) != 0L ? 1 : 0;
/* 2263 */       if (i5 > 1)
/*      */       {
/* 2266 */         long l7 = -1L << i5 - 1 ^ 0xFFFFFFFF;
/* 2267 */         i1 = (i1 != 0) || ((l4 & l7) != 0L) ? 1 : 0;
/*      */       }
/*      */ 
/* 2271 */       l4 >>= i5;
/*      */ 
/* 2273 */       l4 = 0L | 0xFFFFFFFF & l4;
/*      */     }
/*      */ 
/* 2305 */     int i5 = 0;
/* 2306 */     int i6 = (l4 & 1L) == 0L ? 1 : 0;
/* 2307 */     if (((i6 != 0) && (n != 0) && (i1 != 0)) || ((i6 == 0) && (n != 0)))
/*      */     {
/* 2309 */       i5 = 1;
/* 2310 */       l4 += 1L;
/*      */     }
/*      */ 
/* 2313 */     FloatingDecimal localFloatingDecimal = new FloatingDecimal(FpUtils.rawCopySign(Double.longBitsToDouble(l4), d));
/*      */ 
/* 2340 */     if ((l3 >= -150L) && (l3 <= 127L))
/*      */     {
/* 2359 */       if ((l4 & 0xFFFFFFF) == 0L)
/*      */       {
/* 2368 */         if ((n != 0) || (i1 != 0)) {
/* 2369 */           if (i6 != 0)
/*      */           {
/* 2378 */             if ((n ^ i1) != 0) {
/* 2379 */               localFloatingDecimal.roundDir = 1;
/*      */             }
/*      */ 
/*      */           }
/* 2390 */           else if (n != 0) {
/* 2391 */             localFloatingDecimal.roundDir = -1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2397 */     localFloatingDecimal.fromHex = true;
/* 2398 */     return localFloatingDecimal;
/*      */   }
/*      */ 
/*      */   static String stripLeadingZeros(String paramString)
/*      */   {
/* 2407 */     return paramString.replaceFirst("^0+", "");
/*      */   }
/*      */ 
/*      */   static int getHexDigit(String paramString, int paramInt)
/*      */   {
/* 2415 */     int i = Character.digit(paramString.charAt(paramInt), 16);
/* 2416 */     if ((i <= -1) || (i >= 16)) {
/* 2417 */       throw new AssertionError("Unexpected failure of digit conversion of " + paramString.charAt(paramInt));
/*      */     }
/*      */ 
/* 2420 */     return i;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FloatingDecimal
 * JD-Core Version:    0.6.2
 */