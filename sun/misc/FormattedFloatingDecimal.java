/*      */ package sun.misc;
/*      */ 
/*      */ public class FormattedFloatingDecimal
/*      */ {
/*      */   boolean isExceptional;
/*      */   boolean isNegative;
/*      */   int decExponent;
/*      */   int decExponentRounded;
/*      */   char[] digits;
/*      */   int nDigits;
/*      */   int bigIntExp;
/*      */   int bigIntNBits;
/*   42 */   boolean mustSetRoundDir = false;
/*   43 */   boolean fromHex = false;
/*   44 */   int roundDir = 0;
/*      */   int precision;
/*      */   private Form form;
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
/* 1144 */   private static ThreadLocal perThreadBuffer = new ThreadLocal() {
/*      */     protected synchronized Object initialValue() {
/* 1146 */       return new char[26];
/*      */     }
/* 1144 */   };
/*      */ 
/* 1662 */   private static final double[] small10pow = { 1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 10000000.0D, 100000000.0D, 1000000000.0D, 10000000000.0D, 100000000000.0D, 1000000000000.0D, 10000000000000.0D, 100000000000000.0D, 1000000000000000.0D, 10000000000000000.0D, 1.0E+017D, 1.0E+018D, 1.0E+019D, 1.0E+020D, 1.0E+021D, 1.0E+022D };
/*      */ 
/* 1671 */   private static final float[] singleSmall10pow = { 1.0F, 10.0F, 100.0F, 1000.0F, 10000.0F, 100000.0F, 1000000.0F, 10000000.0F, 1.0E+008F, 1.0E+009F, 1.0E+010F };
/*      */ 
/* 1677 */   private static final double[] big10pow = { 10000000000000000.0D, 1.E+032D, 1.0E+064D, 1.E+128D, 1.0E+256D };
/*      */ 
/* 1679 */   private static final double[] tiny10pow = { 1.0E-016D, 1.E-032D, 1.0E-064D, 1.E-128D, 1.0E-256D };
/*      */ 
/* 1682 */   private static final int maxSmallTen = small10pow.length - 1;
/* 1683 */   private static final int singleMaxSmallTen = singleSmall10pow.length - 1;
/*      */ 
/* 1685 */   private static final int[] small5pow = { 1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125 };
/*      */ 
/* 1703 */   private static final long[] long5pow = { 1L, 5L, 25L, 125L, 625L, 3125L, 15625L, 78125L, 390625L, 1953125L, 9765625L, 48828125L, 244140625L, 1220703125L, 6103515625L, 30517578125L, 152587890625L, 762939453125L, 3814697265625L, 19073486328125L, 95367431640625L, 476837158203125L, 2384185791015625L, 11920928955078125L, 59604644775390625L, 298023223876953125L, 1490116119384765625L };
/*      */ 
/* 1734 */   private static final int[] n5bits = { 0, 3, 5, 7, 10, 12, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35, 38, 40, 42, 45, 47, 49, 52, 54, 56, 59, 61 };
/*      */ 
/* 1764 */   private static final char[] infinity = { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };
/* 1765 */   private static final char[] notANumber = { 'N', 'a', 'N' };
/* 1766 */   private static final char[] zero = { '0', '0', '0', '0', '0', '0', '0', '0' };
/*      */ 
/*      */   private FormattedFloatingDecimal(boolean paramBoolean1, int paramInt1, char[] paramArrayOfChar, int paramInt2, boolean paramBoolean2, int paramInt3, Form paramForm)
/*      */   {
/*   53 */     this.isNegative = paramBoolean1;
/*   54 */     this.isExceptional = paramBoolean2;
/*   55 */     this.decExponent = paramInt1;
/*   56 */     this.digits = paramArrayOfChar;
/*   57 */     this.nDigits = paramInt2;
/*   58 */     this.precision = paramInt3;
/*   59 */     this.form = paramForm;
/*      */   }
/*      */ 
/*      */   private static int countBits(long paramLong)
/*      */   {
/*  109 */     if (paramLong == 0L) return 0;
/*      */ 
/*  111 */     while ((paramLong & 0x0) == 0L) {
/*  112 */       paramLong <<= 8;
/*      */     }
/*  114 */     while (paramLong > 0L) {
/*  115 */       paramLong <<= 1;
/*      */     }
/*      */ 
/*  118 */     int i = 0;
/*  119 */     while ((paramLong & 0xFFFFFFFF) != 0L) {
/*  120 */       paramLong <<= 8;
/*  121 */       i += 8;
/*      */     }
/*  123 */     while (paramLong != 0L) {
/*  124 */       paramLong <<= 1;
/*  125 */       i++;
/*      */     }
/*  127 */     return i;
/*      */   }
/*      */ 
/*      */   private static synchronized FDBigInt big5pow(int paramInt)
/*      */   {
/*  137 */     assert (paramInt >= 0) : paramInt;
/*  138 */     if (b5p == null) {
/*  139 */       b5p = new FDBigInt[paramInt + 1];
/*  140 */     } else if (b5p.length <= paramInt) {
/*  141 */       FDBigInt[] arrayOfFDBigInt = new FDBigInt[paramInt + 1];
/*  142 */       System.arraycopy(b5p, 0, arrayOfFDBigInt, 0, b5p.length);
/*  143 */       b5p = arrayOfFDBigInt;
/*      */     }
/*  145 */     if (b5p[paramInt] != null)
/*  146 */       return b5p[paramInt];
/*  147 */     if (paramInt < small5pow.length)
/*  148 */       return b5p[paramInt] =  = new FDBigInt(small5pow[paramInt]);
/*  149 */     if (paramInt < long5pow.length) {
/*  150 */       return b5p[paramInt] =  = new FDBigInt(long5pow[paramInt]);
/*      */     }
/*      */ 
/*  159 */     int i = paramInt >> 1;
/*  160 */     int j = paramInt - i;
/*  161 */     FDBigInt localFDBigInt1 = b5p[i];
/*  162 */     if (localFDBigInt1 == null)
/*  163 */       localFDBigInt1 = big5pow(i);
/*  164 */     if (j < small5pow.length) {
/*  165 */       return b5p[paramInt] =  = localFDBigInt1.mult(small5pow[j]);
/*      */     }
/*  167 */     FDBigInt localFDBigInt2 = b5p[j];
/*  168 */     if (localFDBigInt2 == null)
/*  169 */       localFDBigInt2 = big5pow(j);
/*  170 */     return b5p[paramInt] =  = localFDBigInt1.mult(localFDBigInt2);
/*      */   }
/*      */ 
/*      */   private static FDBigInt multPow52(FDBigInt paramFDBigInt, int paramInt1, int paramInt2)
/*      */   {
/*  180 */     if (paramInt1 != 0) {
/*  181 */       if (paramInt1 < small5pow.length)
/*  182 */         paramFDBigInt = paramFDBigInt.mult(small5pow[paramInt1]);
/*      */       else {
/*  184 */         paramFDBigInt = paramFDBigInt.mult(big5pow(paramInt1));
/*      */       }
/*      */     }
/*  187 */     if (paramInt2 != 0) {
/*  188 */       paramFDBigInt.lshiftMe(paramInt2);
/*      */     }
/*  190 */     return paramFDBigInt;
/*      */   }
/*      */ 
/*      */   private static FDBigInt constructPow52(int paramInt1, int paramInt2)
/*      */   {
/*  198 */     FDBigInt localFDBigInt = new FDBigInt(big5pow(paramInt1));
/*  199 */     if (paramInt2 != 0) {
/*  200 */       localFDBigInt.lshiftMe(paramInt2);
/*      */     }
/*  202 */     return localFDBigInt;
/*      */   }
/*      */ 
/*      */   private FDBigInt doubleToBigInt(double paramDouble)
/*      */   {
/*  217 */     long l = Double.doubleToLongBits(paramDouble) & 0xFFFFFFFF;
/*  218 */     int i = (int)(l >>> 52);
/*  219 */     l &= 4503599627370495L;
/*  220 */     if (i > 0) {
/*  221 */       l |= 4503599627370496L;
/*      */     } else {
/*  223 */       assert (l != 0L) : l;
/*  224 */       i++;
/*  225 */       while ((l & 0x0) == 0L) {
/*  226 */         l <<= 1;
/*  227 */         i--;
/*      */       }
/*      */     }
/*  230 */     i -= 1023;
/*  231 */     int j = countBits(l);
/*      */ 
/*  236 */     int k = 53 - j;
/*  237 */     l >>>= k;
/*      */ 
/*  239 */     this.bigIntExp = (i + 1 - j);
/*  240 */     this.bigIntNBits = j;
/*  241 */     return new FDBigInt(l);
/*      */   }
/*      */ 
/*      */   private static double ulp(double paramDouble, boolean paramBoolean)
/*      */   {
/*  251 */     long l = Double.doubleToLongBits(paramDouble) & 0xFFFFFFFF;
/*  252 */     int i = (int)(l >>> 52);
/*      */ 
/*  254 */     if ((paramBoolean) && (i >= 52) && ((l & 0xFFFFFFFF) == 0L))
/*      */     {
/*  257 */       i--;
/*      */     }
/*      */     double d;
/*  259 */     if (i > 52)
/*  260 */       d = Double.longBitsToDouble(i - 52 << 52);
/*  261 */     else if (i == 0)
/*  262 */       d = 4.9E-324D;
/*      */     else {
/*  264 */       d = Double.longBitsToDouble(1L << i - 1);
/*      */     }
/*  266 */     if (paramBoolean) d = -d;
/*      */ 
/*  268 */     return d;
/*      */   }
/*      */ 
/*      */   float stickyRound(double paramDouble)
/*      */   {
/*  282 */     long l1 = Double.doubleToLongBits(paramDouble);
/*  283 */     long l2 = l1 & 0x0;
/*  284 */     if ((l2 == 0L) || (l2 == 9218868437227405312L))
/*      */     {
/*  287 */       return (float)paramDouble;
/*      */     }
/*  289 */     l1 += this.roundDir;
/*  290 */     return (float)Double.longBitsToDouble(l1);
/*      */   }
/*      */ 
/*      */   private void developLongDigits(int paramInt, long paramLong1, long paramLong2)
/*      */   {
/*  319 */     for (int m = 0; paramLong2 >= 10L; m++)
/*  320 */       paramLong2 /= 10L;
/*  321 */     if (m != 0) {
/*  322 */       long l1 = long5pow[m] << m;
/*  323 */       long l2 = paramLong1 % l1;
/*  324 */       paramLong1 /= l1;
/*  325 */       paramInt += m;
/*  326 */       if (l2 >= l1 >> 1)
/*      */       {
/*  328 */         paramLong1 += 1L;
/*      */       }
/*      */     }
/*      */     int i;
/*      */     char[] arrayOfChar1;
/*      */     int j;
/*      */     int k;
/*  331 */     if (paramLong1 <= 2147483647L) {
/*  332 */       assert (paramLong1 > 0L) : paramLong1;
/*      */ 
/*  335 */       int n = (int)paramLong1;
/*  336 */       i = 10;
/*  337 */       arrayOfChar1 = (char[])perThreadBuffer.get();
/*  338 */       j = i - 1;
/*  339 */       k = n % 10;
/*  340 */       n /= 10;
/*  341 */       while (k == 0) {
/*  342 */         paramInt++;
/*  343 */         k = n % 10;
/*  344 */         n /= 10;
/*      */       }
/*  346 */       while (n != 0) {
/*  347 */         arrayOfChar1[(j--)] = ((char)(k + 48));
/*  348 */         paramInt++;
/*  349 */         k = n % 10;
/*  350 */         n /= 10;
/*      */       }
/*  352 */       arrayOfChar1[j] = ((char)(k + 48));
/*      */     }
/*      */     else
/*      */     {
/*  356 */       i = 20;
/*  357 */       arrayOfChar1 = (char[])perThreadBuffer.get();
/*  358 */       j = i - 1;
/*  359 */       k = (int)(paramLong1 % 10L);
/*  360 */       paramLong1 /= 10L;
/*  361 */       while (k == 0) {
/*  362 */         paramInt++;
/*  363 */         k = (int)(paramLong1 % 10L);
/*  364 */         paramLong1 /= 10L;
/*      */       }
/*  366 */       while (paramLong1 != 0L) {
/*  367 */         arrayOfChar1[(j--)] = ((char)(k + 48));
/*  368 */         paramInt++;
/*  369 */         k = (int)(paramLong1 % 10L);
/*  370 */         paramLong1 /= 10L;
/*      */       }
/*  372 */       arrayOfChar1[j] = ((char)(k + 48));
/*      */     }
/*      */ 
/*  375 */     i -= j;
/*  376 */     char[] arrayOfChar2 = new char[i];
/*  377 */     System.arraycopy(arrayOfChar1, j, arrayOfChar2, 0, i);
/*  378 */     this.digits = arrayOfChar2;
/*  379 */     this.decExponent = (paramInt + 1);
/*  380 */     this.nDigits = i;
/*      */   }
/*      */ 
/*      */   private void roundup()
/*      */   {
/*      */     int i;
/*  393 */     int j = this.digits[(i = this.nDigits - 1)];
/*  394 */     if (j == 57) {
/*  395 */       while ((j == 57) && (i > 0)) {
/*  396 */         this.digits[i] = '0';
/*  397 */         j = this.digits[(--i)];
/*      */       }
/*  399 */       if (j == 57)
/*      */       {
/*  401 */         this.decExponent += 1;
/*  402 */         this.digits[0] = '1';
/*  403 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  407 */     this.digits[i] = ((char)(j + 1));
/*      */   }
/*      */ 
/*      */   private int checkExponent(int paramInt)
/*      */   {
/*  412 */     if ((paramInt >= this.nDigits) || (paramInt < 0)) {
/*  413 */       return this.decExponent;
/*      */     }
/*  415 */     for (int i = 0; i < paramInt; i++)
/*  416 */       if (this.digits[i] != '9')
/*      */       {
/*  418 */         return this.decExponent;
/*      */       }
/*  419 */     return this.decExponent + (this.digits[paramInt] >= '5' ? 1 : 0);
/*      */   }
/*      */ 
/*      */   private char[] applyPrecision(int paramInt)
/*      */   {
/*  425 */     char[] arrayOfChar = new char[this.nDigits];
/*  426 */     for (int i = 0; i < arrayOfChar.length; i++) arrayOfChar[i] = '0';
/*      */ 
/*  428 */     if ((paramInt >= this.nDigits) || (paramInt < 0))
/*      */     {
/*  430 */       System.arraycopy(this.digits, 0, arrayOfChar, 0, this.nDigits);
/*  431 */       return arrayOfChar;
/*      */     }
/*  433 */     if (paramInt == 0)
/*      */     {
/*  436 */       if (this.digits[0] >= '5') {
/*  437 */         arrayOfChar[0] = '1';
/*      */       }
/*  439 */       return arrayOfChar;
/*      */     }
/*      */ 
/*  442 */     i = paramInt;
/*  443 */     int j = this.digits[i];
/*  444 */     if ((j >= 53) && (i > 0)) {
/*  445 */       j = this.digits[(--i)];
/*  446 */       if (j == 57) {
/*  447 */         while ((j == 57) && (i > 0)) {
/*  448 */           j = this.digits[(--i)];
/*      */         }
/*  450 */         if (j == 57)
/*      */         {
/*  452 */           arrayOfChar[0] = '1';
/*  453 */           return arrayOfChar;
/*      */         }
/*      */       }
/*  456 */       arrayOfChar[i] = ((char)(j + 1));
/*      */     }while (true) {
/*  458 */       i--; if (i < 0) break;
/*  459 */       arrayOfChar[i] = this.digits[i];
/*      */     }
/*  461 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   public FormattedFloatingDecimal(double paramDouble)
/*      */   {
/*  469 */     this(paramDouble, 2147483647, Form.COMPATIBLE);
/*      */   }
/*      */ 
/*      */   public FormattedFloatingDecimal(double paramDouble, int paramInt, Form paramForm)
/*      */   {
/*  474 */     long l1 = Double.doubleToLongBits(paramDouble);
/*      */ 
/*  479 */     this.precision = paramInt;
/*  480 */     this.form = paramForm;
/*      */ 
/*  483 */     if ((l1 & 0x0) != 0L) {
/*  484 */       this.isNegative = true;
/*  485 */       l1 ^= -9223372036854775808L;
/*      */     } else {
/*  487 */       this.isNegative = false;
/*      */     }
/*      */ 
/*  491 */     int i = (int)((l1 & 0x0) >> 52);
/*  492 */     long l2 = l1 & 0xFFFFFFFF;
/*  493 */     if (i == 2047) {
/*  494 */       this.isExceptional = true;
/*  495 */       if (l2 == 0L) {
/*  496 */         this.digits = infinity;
/*      */       } else {
/*  498 */         this.digits = notANumber;
/*  499 */         this.isNegative = false;
/*      */       }
/*  501 */       this.nDigits = this.digits.length;
/*  502 */       return;
/*      */     }
/*  504 */     this.isExceptional = false;
/*      */     int j;
/*  509 */     if (i == 0) {
/*  510 */       if (l2 == 0L)
/*      */       {
/*  512 */         this.decExponent = 0;
/*  513 */         this.digits = zero;
/*  514 */         this.nDigits = 1;
/*  515 */         return;
/*      */       }
/*  517 */       while ((l2 & 0x0) == 0L) {
/*  518 */         l2 <<= 1;
/*  519 */         i--;
/*      */       }
/*  521 */       j = 52 + i + 1;
/*  522 */       i++;
/*      */     } else {
/*  524 */       l2 |= 4503599627370496L;
/*  525 */       j = 53;
/*      */     }
/*  527 */     i -= 1023;
/*      */ 
/*  529 */     dtoa(i, l2, j);
/*      */   }
/*      */ 
/*      */   public FormattedFloatingDecimal(float paramFloat)
/*      */   {
/*  537 */     this(paramFloat, 2147483647, Form.COMPATIBLE);
/*      */   }
/*      */ 
/*      */   public FormattedFloatingDecimal(float paramFloat, int paramInt, Form paramForm) {
/*  541 */     int i = Float.floatToIntBits(paramFloat);
/*      */ 
/*  546 */     this.precision = paramInt;
/*  547 */     this.form = paramForm;
/*      */ 
/*  550 */     if ((i & 0x80000000) != 0) {
/*  551 */       this.isNegative = true;
/*  552 */       i ^= -2147483648;
/*      */     } else {
/*  554 */       this.isNegative = false;
/*      */     }
/*      */ 
/*  558 */     int k = (i & 0x7F800000) >> 23;
/*  559 */     int j = i & 0x7FFFFF;
/*  560 */     if (k == 255) {
/*  561 */       this.isExceptional = true;
/*  562 */       if (j == 0L) {
/*  563 */         this.digits = infinity;
/*      */       } else {
/*  565 */         this.digits = notANumber;
/*  566 */         this.isNegative = false;
/*      */       }
/*  568 */       this.nDigits = this.digits.length;
/*  569 */       return;
/*      */     }
/*  571 */     this.isExceptional = false;
/*      */     int m;
/*  576 */     if (k == 0) {
/*  577 */       if (j == 0)
/*      */       {
/*  579 */         this.decExponent = 0;
/*  580 */         this.digits = zero;
/*  581 */         this.nDigits = 1;
/*  582 */         return;
/*      */       }
/*  584 */       while ((j & 0x800000) == 0) {
/*  585 */         j <<= 1;
/*  586 */         k--;
/*      */       }
/*  588 */       m = 23 + k + 1;
/*  589 */       k++;
/*      */     } else {
/*  591 */       j |= 8388608;
/*  592 */       m = 24;
/*      */     }
/*  594 */     k -= 127;
/*      */ 
/*  596 */     dtoa(k, j << 29, m);
/*      */   }
/*      */ 
/*      */   private void dtoa(int paramInt1, long paramLong, int paramInt2)
/*      */   {
/*  609 */     int i = countBits(paramLong);
/*  610 */     int j = Math.max(0, i - paramInt1 - 1);
/*  611 */     if ((paramInt1 <= 62) && (paramInt1 >= -21))
/*      */     {
/*  615 */       if ((j < long5pow.length) && (i + n5bits[j] < 64))
/*      */       {
/*  632 */         if (j == 0)
/*      */         {
/*      */           long l1;
/*  633 */           if (paramInt1 > paramInt2)
/*  634 */             l1 = 1L << paramInt1 - paramInt2 - 1;
/*      */           else {
/*  636 */             l1 = 0L;
/*      */           }
/*  638 */           if (paramInt1 >= 52)
/*  639 */             paramLong <<= paramInt1 - 52;
/*      */           else {
/*  641 */             paramLong >>>= 52 - paramInt1;
/*      */           }
/*  643 */           developLongDigits(0, paramLong, l1);
/*  644 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  695 */     double d = Double.longBitsToDouble(0x0 | paramLong & 0xFFFFFFFF);
/*      */ 
/*  697 */     int k = (int)Math.floor((d - 1.5D) * 0.289529654D + 0.176091259D + paramInt1 * 0.301029995663981D);
/*      */ 
/*  706 */     int n = Math.max(0, -k);
/*  707 */     int m = n + j + paramInt1;
/*      */ 
/*  709 */     int i2 = Math.max(0, k);
/*  710 */     int i1 = i2 + j;
/*      */ 
/*  712 */     int i4 = n;
/*  713 */     int i3 = m - paramInt2;
/*      */ 
/*  723 */     paramLong >>>= 53 - i;
/*  724 */     m -= i - 1;
/*  725 */     int i7 = Math.min(m, i1);
/*  726 */     m -= i7;
/*  727 */     i1 -= i7;
/*  728 */     i3 -= i7;
/*      */ 
/*  736 */     if (i == 1) {
/*  737 */       i3--;
/*      */     }
/*  739 */     if (i3 < 0)
/*      */     {
/*  743 */       m -= i3;
/*  744 */       i1 -= i3;
/*  745 */       i3 = 0;
/*      */     }
/*      */ 
/*  755 */     char[] arrayOfChar = this.digits = new char[18];
/*  756 */     int i8 = 0;
/*      */ 
/*  776 */     int i5 = i + m + (n < n5bits.length ? n5bits[n] : n * 3);
/*  777 */     int i6 = i1 + 1 + (i2 + 1 < n5bits.length ? n5bits[(i2 + 1)] : (i2 + 1) * 3);
/*      */     int i13;
/*      */     int i11;
/*      */     int i9;
/*      */     int i10;
/*      */     long l2;
/*  778 */     if ((i5 < 64) && (i6 < 64)) {
/*  779 */       if ((i5 < 32) && (i6 < 32))
/*      */       {
/*  781 */         int i12 = (int)paramLong * small5pow[n] << m;
/*  782 */         i13 = small5pow[i2] << i1;
/*  783 */         int i14 = small5pow[i4] << i3;
/*  784 */         int i15 = i13 * 10;
/*      */ 
/*  790 */         i8 = 0;
/*  791 */         i11 = i12 / i13;
/*  792 */         i12 = 10 * (i12 % i13);
/*  793 */         i14 *= 10;
/*  794 */         i9 = i12 < i14 ? 1 : 0;
/*  795 */         i10 = i12 + i14 > i15 ? 1 : 0;
/*  796 */         assert (i11 < 10) : i11;
/*  797 */         if ((i11 == 0) && (i10 == 0))
/*      */         {
/*  799 */           k--;
/*      */         }
/*  801 */         else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  809 */         if ((this.form != Form.COMPATIBLE) || (-3 >= k) || (k >= 8)) {
/*  810 */           i10 = i9 = 0;
/*      */         }
/*  812 */         while ((i9 == 0) && (i10 == 0)) {
/*  813 */           i11 = i12 / i13;
/*  814 */           i12 = 10 * (i12 % i13);
/*  815 */           i14 *= 10;
/*  816 */           assert (i11 < 10) : i11;
/*  817 */           if (i14 > 0L) {
/*  818 */             i9 = i12 < i14 ? 1 : 0;
/*  819 */             i10 = i12 + i14 > i15 ? 1 : 0;
/*      */           }
/*      */           else
/*      */           {
/*  826 */             i9 = 1;
/*  827 */             i10 = 1;
/*      */           }
/*  829 */           arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */         }
/*  831 */         l2 = (i12 << 1) - i15;
/*      */       }
/*      */       else {
/*  834 */         long l3 = paramLong * long5pow[n] << m;
/*  835 */         long l4 = long5pow[i2] << i1;
/*  836 */         long l5 = long5pow[i4] << i3;
/*  837 */         long l6 = l4 * 10L;
/*      */ 
/*  843 */         i8 = 0;
/*  844 */         i11 = (int)(l3 / l4);
/*  845 */         l3 = 10L * (l3 % l4);
/*  846 */         l5 *= 10L;
/*  847 */         i9 = l3 < l5 ? 1 : 0;
/*  848 */         i10 = l3 + l5 > l6 ? 1 : 0;
/*  849 */         assert (i11 < 10) : i11;
/*  850 */         if ((i11 == 0) && (i10 == 0))
/*      */         {
/*  852 */           k--;
/*      */         }
/*  854 */         else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  862 */         if ((this.form != Form.COMPATIBLE) || (-3 >= k) || (k >= 8)) {
/*  863 */           i10 = i9 = 0;
/*      */         }
/*  865 */         while ((i9 == 0) && (i10 == 0)) {
/*  866 */           i11 = (int)(l3 / l4);
/*  867 */           l3 = 10L * (l3 % l4);
/*  868 */           l5 *= 10L;
/*  869 */           assert (i11 < 10) : i11;
/*  870 */           if (l5 > 0L) {
/*  871 */             i9 = l3 < l5 ? 1 : 0;
/*  872 */             i10 = l3 + l5 > l6 ? 1 : 0;
/*      */           }
/*      */           else
/*      */           {
/*  879 */             i9 = 1;
/*  880 */             i10 = 1;
/*      */           }
/*  882 */           arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */         }
/*  884 */         l2 = (l3 << 1) - l6;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  894 */       FDBigInt localFDBigInt2 = multPow52(new FDBigInt(paramLong), n, m);
/*  895 */       FDBigInt localFDBigInt1 = constructPow52(i2, i1);
/*  896 */       FDBigInt localFDBigInt3 = constructPow52(i4, i3);
/*      */ 
/*  900 */       localFDBigInt2.lshiftMe(i13 = localFDBigInt1.normalizeMe());
/*  901 */       localFDBigInt3.lshiftMe(i13);
/*  902 */       FDBigInt localFDBigInt4 = localFDBigInt1.mult(10);
/*      */ 
/*  908 */       i8 = 0;
/*  909 */       i11 = localFDBigInt2.quoRemIteration(localFDBigInt1);
/*  910 */       localFDBigInt3 = localFDBigInt3.mult(10);
/*  911 */       i9 = localFDBigInt2.cmp(localFDBigInt3) < 0 ? 1 : 0;
/*  912 */       i10 = localFDBigInt2.add(localFDBigInt3).cmp(localFDBigInt4) > 0 ? 1 : 0;
/*  913 */       assert (i11 < 10) : i11;
/*  914 */       if ((i11 == 0) && (i10 == 0))
/*      */       {
/*  916 */         k--;
/*      */       }
/*  918 */       else arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */ 
/*  926 */       if ((this.form != Form.COMPATIBLE) || (-3 >= k) || (k >= 8)) {
/*  927 */         i10 = i9 = 0;
/*      */       }
/*  929 */       while ((i9 == 0) && (i10 == 0)) {
/*  930 */         i11 = localFDBigInt2.quoRemIteration(localFDBigInt1);
/*  931 */         localFDBigInt3 = localFDBigInt3.mult(10);
/*  932 */         assert (i11 < 10) : i11;
/*  933 */         i9 = localFDBigInt2.cmp(localFDBigInt3) < 0 ? 1 : 0;
/*  934 */         i10 = localFDBigInt2.add(localFDBigInt3).cmp(localFDBigInt4) > 0 ? 1 : 0;
/*  935 */         arrayOfChar[(i8++)] = ((char)(48 + i11));
/*      */       }
/*  937 */       if ((i10 != 0) && (i9 != 0)) {
/*  938 */         localFDBigInt2.lshiftMe(1);
/*  939 */         l2 = localFDBigInt2.cmp(localFDBigInt4);
/*      */       } else {
/*  941 */         l2 = 0L;
/*      */       }
/*      */     }
/*  943 */     this.decExponent = (k + 1);
/*  944 */     this.digits = arrayOfChar;
/*  945 */     this.nDigits = i8;
/*      */ 
/*  949 */     if (i10 != 0)
/*  950 */       if (i9 != 0) {
/*  951 */         if (l2 == 0L)
/*      */         {
/*  954 */           if ((arrayOfChar[(this.nDigits - 1)] & 0x1) != 0) roundup(); 
/*      */         }
/*  955 */         else if (l2 > 0L)
/*  956 */           roundup();
/*      */       }
/*      */       else
/*  959 */         roundup();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  967 */     StringBuffer localStringBuffer = new StringBuffer(this.nDigits + 8);
/*  968 */     if (this.isNegative) localStringBuffer.append('-');
/*  969 */     if (this.isExceptional) {
/*  970 */       localStringBuffer.append(this.digits, 0, this.nDigits);
/*      */     } else {
/*  972 */       localStringBuffer.append("0.");
/*  973 */       localStringBuffer.append(this.digits, 0, this.nDigits);
/*  974 */       localStringBuffer.append('e');
/*  975 */       localStringBuffer.append(this.decExponent);
/*      */     }
/*  977 */     return new String(localStringBuffer);
/*      */   }
/*      */ 
/*      */   public int getExponent()
/*      */   {
/*  982 */     return this.decExponent - 1;
/*      */   }
/*      */ 
/*      */   public int getExponentRounded()
/*      */   {
/*  987 */     return this.decExponentRounded - 1;
/*      */   }
/*      */ 
/*      */   public int getChars(char[] paramArrayOfChar) {
/*  991 */     assert (this.nDigits <= 19) : this.nDigits;
/*  992 */     int i = 0;
/*  993 */     if (this.isNegative) { paramArrayOfChar[0] = '-'; i = 1; }
/*  994 */     if (this.isExceptional) {
/*  995 */       System.arraycopy(this.digits, 0, paramArrayOfChar, i, this.nDigits);
/*  996 */       i += this.nDigits;
/*      */     } else {
/*  998 */       char[] arrayOfChar = this.digits;
/*  999 */       int j = this.decExponent;
/* 1000 */       switch (2.$SwitchMap$sun$misc$FormattedFloatingDecimal$Form[this.form.ordinal()]) {
/*      */       case 1:
/* 1002 */         break;
/*      */       case 2:
/* 1004 */         j = checkExponent(this.decExponent + this.precision);
/* 1005 */         arrayOfChar = applyPrecision(this.decExponent + this.precision);
/* 1006 */         break;
/*      */       case 3:
/* 1008 */         j = checkExponent(this.precision + 1);
/* 1009 */         arrayOfChar = applyPrecision(this.precision + 1);
/* 1010 */         break;
/*      */       case 4:
/* 1012 */         j = checkExponent(this.precision);
/* 1013 */         arrayOfChar = applyPrecision(this.precision);
/*      */ 
/* 1016 */         if ((j - 1 < -4) || (j - 1 >= this.precision)) {
/* 1017 */           this.form = Form.SCIENTIFIC;
/* 1018 */           this.precision -= 1;
/*      */         } else {
/* 1020 */           this.form = Form.DECIMAL_FLOAT;
/* 1021 */           this.precision -= j;
/*      */         }
/* 1023 */         break;
/*      */       default:
/* 1025 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/* 1027 */       this.decExponentRounded = j;
/*      */       int k;
/*      */       int m;
/* 1029 */       if ((j > 0) && (((this.form == Form.COMPATIBLE) && (j < 8)) || (this.form == Form.DECIMAL_FLOAT)))
/*      */       {
/* 1034 */         k = Math.min(this.nDigits, j);
/* 1035 */         System.arraycopy(arrayOfChar, 0, paramArrayOfChar, i, k);
/* 1036 */         i += k;
/* 1037 */         if (k < j) {
/* 1038 */           k = j - k;
/* 1039 */           for (m = 0; m < k; m++) {
/* 1040 */             paramArrayOfChar[(i++)] = '0';
/*      */           }
/*      */ 
/* 1044 */           if (this.form == Form.COMPATIBLE) {
/* 1045 */             paramArrayOfChar[(i++)] = '.';
/* 1046 */             paramArrayOfChar[(i++)] = '0';
/*      */           }
/*      */ 
/*      */         }
/* 1052 */         else if (this.form == Form.COMPATIBLE) {
/* 1053 */           paramArrayOfChar[(i++)] = '.';
/* 1054 */           if (k < this.nDigits) {
/* 1055 */             m = Math.min(this.nDigits - k, this.precision);
/* 1056 */             System.arraycopy(arrayOfChar, k, paramArrayOfChar, i, m);
/* 1057 */             i += m;
/*      */           } else {
/* 1059 */             paramArrayOfChar[(i++)] = '0';
/*      */           }
/*      */         } else {
/* 1062 */           m = Math.min(this.nDigits - k, this.precision);
/* 1063 */           if (m > 0) {
/* 1064 */             paramArrayOfChar[(i++)] = '.';
/* 1065 */             System.arraycopy(arrayOfChar, k, paramArrayOfChar, i, m);
/* 1066 */             i += m;
/*      */           }
/*      */         }
/*      */       }
/* 1070 */       else if ((j <= 0) && (((this.form == Form.COMPATIBLE) && (j > -3)) || (this.form == Form.DECIMAL_FLOAT)))
/*      */       {
/* 1075 */         paramArrayOfChar[(i++)] = '0';
/* 1076 */         if (j != 0)
/*      */         {
/* 1078 */           k = Math.min(-j, this.precision);
/* 1079 */           if (k > 0) {
/* 1080 */             paramArrayOfChar[(i++)] = '.';
/* 1081 */             for (m = 0; m < k; m++)
/* 1082 */               paramArrayOfChar[(i++)] = '0';
/*      */           }
/*      */         }
/* 1085 */         k = Math.min(arrayOfChar.length, this.precision + j);
/* 1086 */         if (k > 0) {
/* 1087 */           if (i == 1) {
/* 1088 */             paramArrayOfChar[(i++)] = '.';
/*      */           }
/* 1090 */           System.arraycopy(arrayOfChar, 0, paramArrayOfChar, i, k);
/* 1091 */           i += k;
/*      */         }
/*      */       } else {
/* 1094 */         paramArrayOfChar[(i++)] = arrayOfChar[0];
/* 1095 */         if (this.form == Form.COMPATIBLE) {
/* 1096 */           paramArrayOfChar[(i++)] = '.';
/* 1097 */           if (this.nDigits > 1) {
/* 1098 */             System.arraycopy(arrayOfChar, 1, paramArrayOfChar, i, this.nDigits - 1);
/* 1099 */             i += this.nDigits - 1;
/*      */           } else {
/* 1101 */             paramArrayOfChar[(i++)] = '0';
/*      */           }
/* 1103 */           paramArrayOfChar[(i++)] = 'E';
/*      */         } else {
/* 1105 */           if (this.nDigits > 1) {
/* 1106 */             k = Math.min(this.nDigits - 1, this.precision);
/* 1107 */             if (k > 0) {
/* 1108 */               paramArrayOfChar[(i++)] = '.';
/* 1109 */               System.arraycopy(arrayOfChar, 1, paramArrayOfChar, i, k);
/* 1110 */               i += k;
/*      */             }
/*      */           }
/* 1113 */           paramArrayOfChar[(i++)] = 'e';
/*      */         }
/*      */ 
/* 1116 */         if (j <= 0) {
/* 1117 */           paramArrayOfChar[(i++)] = '-';
/* 1118 */           k = -j + 1;
/*      */         } else {
/* 1120 */           if (this.form != Form.COMPATIBLE)
/* 1121 */             paramArrayOfChar[(i++)] = '+';
/* 1122 */           k = j - 1;
/*      */         }
/*      */ 
/* 1125 */         if (k <= 9) {
/* 1126 */           if (this.form != Form.COMPATIBLE)
/* 1127 */             paramArrayOfChar[(i++)] = '0';
/* 1128 */           paramArrayOfChar[(i++)] = ((char)(k + 48));
/* 1129 */         } else if (k <= 99) {
/* 1130 */           paramArrayOfChar[(i++)] = ((char)(k / 10 + 48));
/* 1131 */           paramArrayOfChar[(i++)] = ((char)(k % 10 + 48));
/*      */         } else {
/* 1133 */           paramArrayOfChar[(i++)] = ((char)(k / 100 + 48));
/* 1134 */           k %= 100;
/* 1135 */           paramArrayOfChar[(i++)] = ((char)(k / 10 + 48));
/* 1136 */           paramArrayOfChar[(i++)] = ((char)(k % 10 + 48));
/*      */         }
/*      */       }
/*      */     }
/* 1140 */     return i;
/*      */   }
/*      */ 
/*      */   public strictfp double doubleValue()
/*      */   {
/* 1160 */     int i = Math.min(this.nDigits, 16);
/*      */ 
/* 1166 */     if ((this.digits == infinity) || (this.digits == notANumber)) {
/* 1167 */       if (this.digits == notANumber) {
/* 1168 */         return (0.0D / 0.0D);
/*      */       }
/* 1170 */       return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */     }
/*      */ 
/* 1173 */     if (this.mustSetRoundDir) {
/* 1174 */       this.roundDir = 0;
/*      */     }
/*      */ 
/* 1180 */     int j = this.digits[0] - '0';
/* 1181 */     int k = Math.min(i, 9);
/* 1182 */     for (int m = 1; m < k; m++) {
/* 1183 */       j = j * 10 + this.digits[m] - 48;
/*      */     }
/* 1185 */     long l = j;
/* 1186 */     for (m = k; m < i; m++) {
/* 1187 */       l = l * 10L + (this.digits[m] - '0');
/*      */     }
/* 1189 */     double d1 = l;
/* 1190 */     m = this.decExponent - i;
/*      */     int n;
/* 1197 */     if (this.nDigits <= 15)
/*      */     {
/* 1208 */       if ((m == 0) || (d1 == 0.0D))
/* 1209 */         return this.isNegative ? -d1 : d1;
/*      */       double d2;
/*      */       double d3;
/* 1210 */       if (m >= 0) {
/* 1211 */         if (m <= maxSmallTen)
/*      */         {
/* 1216 */           d2 = d1 * small10pow[m];
/* 1217 */           if (this.mustSetRoundDir) {
/* 1218 */             d3 = d2 / small10pow[m];
/* 1219 */             this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */           }
/*      */ 
/* 1223 */           return this.isNegative ? -d2 : d2;
/*      */         }
/* 1225 */         n = 15 - i;
/* 1226 */         if (m <= maxSmallTen + n)
/*      */         {
/* 1233 */           d1 *= small10pow[n];
/* 1234 */           d2 = d1 * small10pow[(m - n)];
/*      */ 
/* 1236 */           if (this.mustSetRoundDir) {
/* 1237 */             d3 = d2 / small10pow[(m - n)];
/* 1238 */             this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */           }
/*      */ 
/* 1242 */           return this.isNegative ? -d2 : d2;
/*      */         }
/*      */ 
/*      */       }
/* 1248 */       else if (m >= -maxSmallTen)
/*      */       {
/* 1252 */         d2 = d1 / small10pow[(-m)];
/* 1253 */         d3 = d2 * small10pow[(-m)];
/* 1254 */         if (this.mustSetRoundDir) {
/* 1255 */           this.roundDir = (d3 < d1 ? 1 : d3 == d1 ? 0 : -1);
/*      */         }
/*      */ 
/* 1259 */         return this.isNegative ? -d2 : d2;
/*      */       }
/*      */     }
/*      */     double d4;
/* 1275 */     if (m > 0) {
/* 1276 */       if (this.decExponent > 309)
/*      */       {
/* 1281 */         return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */       }
/* 1283 */       if ((m & 0xF) != 0) {
/* 1284 */         d1 *= small10pow[(m & 0xF)];
/*      */       }
/* 1286 */       if (m >>= 4 != 0)
/*      */       {
/* 1288 */         for (n = 0; m > 1; m >>= 1) {
/* 1289 */           if ((m & 0x1) != 0)
/* 1290 */             d1 *= big10pow[n];
/* 1288 */           n++;
/*      */         }
/*      */ 
/* 1298 */         d4 = d1 * big10pow[n];
/* 1299 */         if (Double.isInfinite(d4))
/*      */         {
/* 1312 */           d4 = d1 / 2.0D;
/* 1313 */           d4 *= big10pow[n];
/* 1314 */           if (Double.isInfinite(d4)) {
/* 1315 */             return this.isNegative ? (-1.0D / 0.0D) : (1.0D / 0.0D);
/*      */           }
/* 1317 */           d4 = 1.7976931348623157E+308D;
/*      */         }
/* 1319 */         d1 = d4;
/*      */       }
/* 1321 */     } else if (m < 0) {
/* 1322 */       m = -m;
/* 1323 */       if (this.decExponent < -325)
/*      */       {
/* 1328 */         return this.isNegative ? -0.0D : 0.0D;
/*      */       }
/* 1330 */       if ((m & 0xF) != 0) {
/* 1331 */         d1 /= small10pow[(m & 0xF)];
/*      */       }
/* 1333 */       if (m >>= 4 != 0)
/*      */       {
/* 1335 */         for (n = 0; m > 1; m >>= 1) {
/* 1336 */           if ((m & 0x1) != 0)
/* 1337 */             d1 *= tiny10pow[n];
/* 1335 */           n++;
/*      */         }
/*      */ 
/* 1345 */         d4 = d1 * tiny10pow[n];
/* 1346 */         if (d4 == 0.0D)
/*      */         {
/* 1359 */           d4 = d1 * 2.0D;
/* 1360 */           d4 *= tiny10pow[n];
/* 1361 */           if (d4 == 0.0D) {
/* 1362 */             return this.isNegative ? -0.0D : 0.0D;
/*      */           }
/* 1364 */           d4 = 4.9E-324D;
/*      */         }
/* 1366 */         d1 = d4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1377 */     FDBigInt localFDBigInt1 = new FDBigInt(l, this.digits, i, this.nDigits);
/* 1378 */     m = this.decExponent - this.nDigits;
/*      */     while (true)
/*      */     {
/* 1385 */       FDBigInt localFDBigInt2 = doubleToBigInt(d1);
/*      */       int i2;
/*      */       int i1;
/*      */       int i4;
/*      */       int i3;
/* 1399 */       if (m >= 0) {
/* 1400 */         i1 = i2 = 0;
/* 1401 */         i3 = i4 = m;
/*      */       } else {
/* 1403 */         i1 = i2 = -m;
/* 1404 */         i3 = i4 = 0;
/*      */       }
/* 1406 */       if (this.bigIntExp >= 0)
/* 1407 */         i1 += this.bigIntExp;
/*      */       else {
/* 1409 */         i3 -= this.bigIntExp;
/*      */       }
/* 1411 */       int i5 = i1;
/*      */       int i6;
/* 1415 */       if (this.bigIntExp + this.bigIntNBits <= -1022)
/*      */       {
/* 1419 */         i6 = this.bigIntExp + 1023 + 52;
/*      */       }
/* 1421 */       else i6 = 54 - this.bigIntNBits;
/*      */ 
/* 1423 */       i1 += i6;
/* 1424 */       i3 += i6;
/*      */ 
/* 1427 */       int i7 = Math.min(i1, Math.min(i3, i5));
/* 1428 */       i1 -= i7;
/* 1429 */       i3 -= i7;
/* 1430 */       i5 -= i7;
/*      */ 
/* 1432 */       localFDBigInt2 = multPow52(localFDBigInt2, i2, i1);
/* 1433 */       FDBigInt localFDBigInt3 = multPow52(new FDBigInt(localFDBigInt1), i4, i3);
/*      */       int i8;
/*      */       boolean bool;
/*      */       FDBigInt localFDBigInt4;
/* 1451 */       if ((i8 = localFDBigInt2.cmp(localFDBigInt3)) > 0) {
/* 1452 */         bool = true;
/* 1453 */         localFDBigInt4 = localFDBigInt2.sub(localFDBigInt3);
/* 1454 */         if ((this.bigIntNBits == 1) && (this.bigIntExp > -1023))
/*      */         {
/* 1459 */           i5--;
/* 1460 */           if (i5 < 0)
/*      */           {
/* 1463 */             i5 = 0;
/* 1464 */             localFDBigInt4.lshiftMe(1);
/*      */           }
/*      */         }
/*      */       } else { if (i8 >= 0) break;
/* 1468 */         bool = false;
/* 1469 */         localFDBigInt4 = localFDBigInt3.sub(localFDBigInt2);
/*      */       }
/*      */ 
/* 1475 */       FDBigInt localFDBigInt5 = constructPow52(i2, i5);
/* 1476 */       if ((i8 = localFDBigInt4.cmp(localFDBigInt5)) < 0)
/*      */       {
/* 1479 */         if (this.mustSetRoundDir) {
/* 1480 */           this.roundDir = (bool ? -1 : 1);
/*      */         }
/*      */       }
/* 1483 */       else if (i8 == 0)
/*      */       {
/* 1486 */         d1 += 0.5D * ulp(d1, bool);
/*      */ 
/* 1488 */         if (this.mustSetRoundDir) {
/* 1489 */           this.roundDir = (bool ? -1 : 1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1497 */         d1 += ulp(d1, bool);
/* 1498 */         if (d1 != 0.0D) if (d1 == (1.0D / 0.0D)) {
/* 1499 */             break;
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/* 1504 */     return this.isNegative ? -d1 : d1;
/*      */   }
/*      */ 
/*      */   public strictfp float floatValue()
/*      */   {
/* 1519 */     int i = Math.min(this.nDigits, 8);
/*      */ 
/* 1524 */     if ((this.digits == infinity) || (this.digits == notANumber)) {
/* 1525 */       if (this.digits == notANumber) {
/* 1526 */         return (0.0F / 0.0F);
/*      */       }
/* 1528 */       return this.isNegative ? (1.0F / -1.0F) : (1.0F / 1.0F);
/*      */     }
/*      */ 
/* 1534 */     int j = this.digits[0] - '0';
/* 1535 */     for (int k = 1; k < i; k++) {
/* 1536 */       j = j * 10 + this.digits[k] - 48;
/*      */     }
/* 1538 */     float f = j;
/* 1539 */     k = this.decExponent - i;
/*      */ 
/* 1546 */     if (this.nDigits <= 7)
/*      */     {
/* 1557 */       if ((k == 0) || (f == 0.0F))
/* 1558 */         return this.isNegative ? -f : f;
/* 1559 */       if (k >= 0) {
/* 1560 */         if (k <= singleMaxSmallTen)
/*      */         {
/* 1565 */           f *= singleSmall10pow[k];
/* 1566 */           return this.isNegative ? -f : f;
/*      */         }
/* 1568 */         int m = 7 - i;
/* 1569 */         if (k <= singleMaxSmallTen + m)
/*      */         {
/* 1576 */           f *= singleSmall10pow[m];
/* 1577 */           f *= singleSmall10pow[(k - m)];
/* 1578 */           return this.isNegative ? -f : f;
/*      */         }
/*      */ 
/*      */       }
/* 1584 */       else if (k >= -singleMaxSmallTen)
/*      */       {
/* 1588 */         f /= singleSmall10pow[(-k)];
/* 1589 */         return this.isNegative ? -f : f;
/*      */       }
/*      */ 
/*      */     }
/* 1595 */     else if ((this.decExponent >= this.nDigits) && (this.nDigits + this.decExponent <= 15))
/*      */     {
/* 1605 */       long l = j;
/* 1606 */       for (int n = i; n < this.nDigits; n++) {
/* 1607 */         l = l * 10L + (this.digits[n] - '0');
/*      */       }
/* 1609 */       double d2 = l;
/* 1610 */       k = this.decExponent - this.nDigits;
/* 1611 */       d2 *= small10pow[k];
/* 1612 */       f = (float)d2;
/* 1613 */       return this.isNegative ? -f : f;
/*      */     }
/*      */ 
/* 1625 */     if (this.decExponent > 39)
/*      */     {
/* 1630 */       return this.isNegative ? (1.0F / -1.0F) : (1.0F / 1.0F);
/* 1631 */     }if (this.decExponent < -46)
/*      */     {
/* 1636 */       return this.isNegative ? -0.0F : 0.0F;
/*      */     }
/*      */ 
/* 1651 */     this.mustSetRoundDir = (!this.fromHex);
/* 1652 */     double d1 = doubleValue();
/* 1653 */     return stickyRound(d1);
/*      */   }
/*      */ 
/*      */   public static enum Form
/*      */   {
/*   47 */     SCIENTIFIC, COMPATIBLE, DECIMAL_FLOAT, GENERAL;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FormattedFloatingDecimal
 * JD-Core Version:    0.6.2
 */