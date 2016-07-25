/*      */ package java.math;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class BigDecimal extends Number
/*      */   implements Comparable<BigDecimal>
/*      */ {
/*      */   private volatile BigInteger intVal;
/*      */   private int scale;
/*      */   private transient int precision;
/*      */   private transient String stringCache;
/*      */   static final long INFLATED = -9223372036854775808L;
/*      */   private transient long intCompact;
/*      */   private static final int MAX_COMPACT_DIGITS = 18;
/*      */   private static final int MAX_BIGINT_BITS = 62;
/*      */   private static final long serialVersionUID = 6108874887143696463L;
/*  276 */   private static final ThreadLocal<StringBuilderHelper> threadLocalStringBuilderHelper = new ThreadLocal()
/*      */   {
/*      */     protected BigDecimal.StringBuilderHelper initialValue() {
/*  279 */       return new BigDecimal.StringBuilderHelper();
/*      */     }
/*  276 */   };
/*      */ 
/*  284 */   private static final BigDecimal[] zeroThroughTen = { new BigDecimal(BigInteger.ZERO, 0L, 0, 1), new BigDecimal(BigInteger.ONE, 1L, 0, 1), new BigDecimal(BigInteger.valueOf(2L), 2L, 0, 1), new BigDecimal(BigInteger.valueOf(3L), 3L, 0, 1), new BigDecimal(BigInteger.valueOf(4L), 4L, 0, 1), new BigDecimal(BigInteger.valueOf(5L), 5L, 0, 1), new BigDecimal(BigInteger.valueOf(6L), 6L, 0, 1), new BigDecimal(BigInteger.valueOf(7L), 7L, 0, 1), new BigDecimal(BigInteger.valueOf(8L), 8L, 0, 1), new BigDecimal(BigInteger.valueOf(9L), 9L, 0, 1), new BigDecimal(BigInteger.TEN, 10L, 0, 2) };
/*      */ 
/*  299 */   private static final BigDecimal[] ZERO_SCALED_BY = { zeroThroughTen[0], new BigDecimal(BigInteger.ZERO, 0L, 1, 1), new BigDecimal(BigInteger.ZERO, 0L, 2, 1), new BigDecimal(BigInteger.ZERO, 0L, 3, 1), new BigDecimal(BigInteger.ZERO, 0L, 4, 1), new BigDecimal(BigInteger.ZERO, 0L, 5, 1), new BigDecimal(BigInteger.ZERO, 0L, 6, 1), new BigDecimal(BigInteger.ZERO, 0L, 7, 1), new BigDecimal(BigInteger.ZERO, 0L, 8, 1), new BigDecimal(BigInteger.ZERO, 0L, 9, 1), new BigDecimal(BigInteger.ZERO, 0L, 10, 1), new BigDecimal(BigInteger.ZERO, 0L, 11, 1), new BigDecimal(BigInteger.ZERO, 0L, 12, 1), new BigDecimal(BigInteger.ZERO, 0L, 13, 1), new BigDecimal(BigInteger.ZERO, 0L, 14, 1), new BigDecimal(BigInteger.ZERO, 0L, 15, 1) };
/*      */   private static final long HALF_LONG_MAX_VALUE = 4611686018427387903L;
/*      */   private static final long HALF_LONG_MIN_VALUE = -4611686018427387904L;
/*  328 */   public static final BigDecimal ZERO = zeroThroughTen[0];
/*      */ 
/*  336 */   public static final BigDecimal ONE = zeroThroughTen[1];
/*      */ 
/*  344 */   public static final BigDecimal TEN = zeroThroughTen[10];
/*      */   public static final int ROUND_UP = 0;
/*      */   public static final int ROUND_DOWN = 1;
/*      */   public static final int ROUND_CEILING = 2;
/*      */   public static final int ROUND_FLOOR = 3;
/*      */   public static final int ROUND_HALF_UP = 4;
/*      */   public static final int ROUND_HALF_DOWN = 5;
/*      */   public static final int ROUND_HALF_EVEN = 6;
/*      */   public static final int ROUND_UNNECESSARY = 7;
/* 3414 */   private static final long[] LONG_TEN_POWERS_TABLE = { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
/*      */ 
/* 3436 */   private static volatile BigInteger[] BIG_TEN_POWERS_TABLE = { BigInteger.ONE, BigInteger.valueOf(10L), BigInteger.valueOf(100L), BigInteger.valueOf(1000L), BigInteger.valueOf(10000L), BigInteger.valueOf(100000L), BigInteger.valueOf(1000000L), BigInteger.valueOf(10000000L), BigInteger.valueOf(100000000L), BigInteger.valueOf(1000000000L), BigInteger.valueOf(10000000000L), BigInteger.valueOf(100000000000L), BigInteger.valueOf(1000000000000L), BigInteger.valueOf(10000000000000L), BigInteger.valueOf(100000000000000L), BigInteger.valueOf(1000000000000000L), BigInteger.valueOf(10000000000000000L), BigInteger.valueOf(100000000000000000L), BigInteger.valueOf(1000000000000000000L) };
/*      */ 
/* 3453 */   private static final int BIG_TEN_POWERS_TABLE_INITLEN = BIG_TEN_POWERS_TABLE.length;
/*      */ 
/* 3455 */   private static final int BIG_TEN_POWERS_TABLE_MAX = 16 * BIG_TEN_POWERS_TABLE_INITLEN;
/*      */ 
/* 3458 */   private static final long[] THRESHOLDS_TABLE = { 9223372036854775807L, 922337203685477580L, 92233720368547758L, 9223372036854775L, 922337203685477L, 92233720368547L, 9223372036854L, 922337203685L, 92233720368L, 9223372036L, 922337203L, 92233720L, 9223372L, 922337L, 92233L, 9223L, 922L, 92L, 9L };
/*      */ 
/*      */   BigDecimal(BigInteger paramBigInteger, long paramLong, int paramInt1, int paramInt2)
/*      */   {
/*  355 */     this.scale = paramInt1;
/*  356 */     this.precision = paramInt2;
/*  357 */     this.intCompact = paramLong;
/*  358 */     this.intVal = paramBigInteger;
/*      */   }
/*      */ 
/*      */   public BigDecimal(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  382 */     if ((paramInt1 + paramInt2 > paramArrayOfChar.length) || (paramInt1 < 0)) {
/*  383 */       throw new NumberFormatException();
/*      */     }
/*      */ 
/*  390 */     int i = 0;
/*  391 */     int j = 0;
/*  392 */     long l1 = 0L;
/*  393 */     BigInteger localBigInteger = null;
/*      */     try
/*      */     {
/*  399 */       int k = 0;
/*  400 */       if (paramArrayOfChar[paramInt1] == '-') {
/*  401 */         k = 1;
/*  402 */         paramInt1++;
/*  403 */         paramInt2--;
/*  404 */       } else if (paramArrayOfChar[paramInt1] == '+') {
/*  405 */         paramInt1++;
/*  406 */         paramInt2--;
/*      */       }
/*      */ 
/*  410 */       int m = 0;
/*  411 */       int n = paramInt1;
/*  412 */       long l2 = 0L;
/*      */ 
/*  415 */       int i1 = paramInt2 <= 18 ? 1 : 0;
/*      */ 
/*  418 */       char[] arrayOfChar1 = i1 != 0 ? null : new char[paramInt2];
/*  419 */       int i2 = 0;
/*      */ 
/*  421 */       for (; paramInt2 > 0; paramInt2--) {
/*  422 */         char c = paramArrayOfChar[paramInt1];
/*      */         int i3;
/*  424 */         if (((c >= '0') && (c <= '9')) || (Character.isDigit(c)))
/*      */         {
/*  427 */           if (i1 != 0) {
/*  428 */             i3 = Character.digit(c, 10);
/*  429 */             if (i3 == 0) {
/*  430 */               if (i == 0) {
/*  431 */                 i = 1;
/*  432 */               } else if (l1 != 0L) {
/*  433 */                 l1 *= 10L;
/*  434 */                 i++;
/*      */               }
/*      */             } else {
/*  437 */               if ((i != 1) || (l1 != 0L))
/*  438 */                 i++;
/*  439 */               l1 = l1 * 10L + i3;
/*      */             }
/*      */           }
/*  442 */           else if ((c == '0') || (Character.digit(c, 10) == 0)) {
/*  443 */             if (i == 0) {
/*  444 */               arrayOfChar1[i2] = c;
/*  445 */               i = 1;
/*  446 */             } else if (i2 != 0) {
/*  447 */               arrayOfChar1[(i2++)] = c;
/*  448 */               i++;
/*      */             }
/*      */           } else {
/*  451 */             if ((i != 1) || (i2 != 0))
/*  452 */               i++;
/*  453 */             arrayOfChar1[(i2++)] = c;
/*      */           }
/*      */ 
/*  456 */           if (m != 0) {
/*  457 */             j++;
/*      */           }
/*      */ 
/*      */         }
/*  461 */         else if (c == '.')
/*      */         {
/*  463 */           if (m != 0)
/*  464 */             throw new NumberFormatException();
/*  465 */           m = 1;
/*      */         }
/*      */         else
/*      */         {
/*  469 */           if ((c != 'e') && (c != 'E'))
/*  470 */             throw new NumberFormatException();
/*  471 */           paramInt1++;
/*  472 */           c = paramArrayOfChar[paramInt1];
/*  473 */           paramInt2--;
/*  474 */           i3 = c == '-' ? 1 : 0;
/*      */ 
/*  476 */           if ((i3 != 0) || (c == '+')) {
/*  477 */             paramInt1++;
/*  478 */             c = paramArrayOfChar[paramInt1];
/*  479 */             paramInt2--;
/*      */           }
/*  481 */           if (paramInt2 <= 0) {
/*  482 */             throw new NumberFormatException();
/*      */           }
/*  484 */           while ((paramInt2 > 10) && (Character.digit(c, 10) == 0)) {
/*  485 */             paramInt1++;
/*  486 */             c = paramArrayOfChar[paramInt1];
/*  487 */             paramInt2--;
/*      */           }
/*  489 */           if (paramInt2 > 10) {
/*  490 */             throw new NumberFormatException();
/*      */           }
/*  492 */           for (; ; paramInt2--)
/*      */           {
/*      */             int i4;
/*  494 */             if ((c >= '0') && (c <= '9')) {
/*  495 */               i4 = c - '0';
/*      */             } else {
/*  497 */               i4 = Character.digit(c, 10);
/*  498 */               if (i4 < 0)
/*  499 */                 throw new NumberFormatException();
/*      */             }
/*  501 */             l2 = l2 * 10L + i4;
/*  502 */             if (paramInt2 == 1)
/*      */               break;
/*  504 */             paramInt1++;
/*  505 */             c = paramArrayOfChar[paramInt1];
/*      */           }
/*  507 */           if (i3 != 0) {
/*  508 */             l2 = -l2;
/*      */           }
/*  510 */           if ((int)l2 == l2) break;
/*  511 */           throw new NumberFormatException();
/*      */         }
/*  421 */         paramInt1++;
/*      */       }
/*      */ 
/*  515 */       if (i == 0) {
/*  516 */         throw new NumberFormatException();
/*      */       }
/*      */ 
/*  519 */       if (l2 != 0L)
/*      */       {
/*  521 */         long l3 = j - l2;
/*  522 */         if ((l3 > 2147483647L) || (l3 < -2147483648L))
/*      */         {
/*  524 */           throw new NumberFormatException("Scale out of range.");
/*  525 */         }j = (int)l3;
/*      */       }
/*      */ 
/*  529 */       if (i1 != 0) {
/*  530 */         l1 = k != 0 ? -l1 : l1;
/*      */       }
/*      */       else
/*      */       {
/*      */         char[] arrayOfChar2;
/*  533 */         if (k == 0) {
/*  534 */           arrayOfChar2 = arrayOfChar1.length != i ? Arrays.copyOf(arrayOfChar1, i) : arrayOfChar1;
/*      */         }
/*      */         else {
/*  537 */           arrayOfChar2 = new char[i + 1];
/*  538 */           arrayOfChar2[0] = '-';
/*  539 */           System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 1, i);
/*      */         }
/*  541 */         localBigInteger = new BigInteger(arrayOfChar2);
/*  542 */         l1 = compactValFor(localBigInteger);
/*      */       }
/*      */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  545 */       throw new NumberFormatException();
/*      */     } catch (NegativeArraySizeException localNegativeArraySizeException) {
/*  547 */       throw new NumberFormatException();
/*      */     }
/*  549 */     this.scale = j;
/*  550 */     this.precision = i;
/*  551 */     this.intCompact = l1;
/*  552 */     this.intVal = (l1 != -9223372036854775808L ? null : localBigInteger);
/*      */   }
/*      */ 
/*      */   public BigDecimal(char[] paramArrayOfChar, int paramInt1, int paramInt2, MathContext paramMathContext)
/*      */   {
/*  579 */     this(paramArrayOfChar, paramInt1, paramInt2);
/*  580 */     if (paramMathContext.precision > 0)
/*  581 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(char[] paramArrayOfChar)
/*      */   {
/*  601 */     this(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public BigDecimal(char[] paramArrayOfChar, MathContext paramMathContext)
/*      */   {
/*  625 */     this(paramArrayOfChar, 0, paramArrayOfChar.length, paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(String paramString)
/*      */   {
/*  739 */     this(paramString.toCharArray(), 0, paramString.length());
/*      */   }
/*      */ 
/*      */   public BigDecimal(String paramString, MathContext paramMathContext)
/*      */   {
/*  757 */     this(paramString.toCharArray(), 0, paramString.length());
/*  758 */     if (paramMathContext.precision > 0)
/*  759 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(double paramDouble)
/*      */   {
/*  807 */     if ((Double.isInfinite(paramDouble)) || (Double.isNaN(paramDouble))) {
/*  808 */       throw new NumberFormatException("Infinite or NaN");
/*      */     }
/*      */ 
/*  812 */     long l1 = Double.doubleToLongBits(paramDouble);
/*  813 */     int i = l1 >> 63 == 0L ? 1 : -1;
/*  814 */     int j = (int)(l1 >> 52 & 0x7FF);
/*  815 */     long l2 = j == 0 ? (l1 & 0xFFFFFFFF) << 1 : l1 & 0xFFFFFFFF | 0x0;
/*      */ 
/*  817 */     j -= 1075;
/*      */ 
/*  824 */     if (l2 == 0L) {
/*  825 */       this.intVal = BigInteger.ZERO;
/*  826 */       this.intCompact = 0L;
/*  827 */       this.precision = 1;
/*  828 */       return;
/*      */     }
/*      */ 
/*  832 */     while ((l2 & 1L) == 0L) {
/*  833 */       l2 >>= 1;
/*  834 */       j++;
/*      */     }
/*      */ 
/*  838 */     long l3 = i * l2;
/*      */     BigInteger localBigInteger;
/*  840 */     if (j < 0) {
/*  841 */       localBigInteger = BigInteger.valueOf(5L).pow(-j).multiply(l3);
/*  842 */       this.scale = (-j);
/*  843 */     } else if (j > 0) {
/*  844 */       localBigInteger = BigInteger.valueOf(2L).pow(j).multiply(l3);
/*      */     } else {
/*  846 */       localBigInteger = BigInteger.valueOf(l3);
/*      */     }
/*  848 */     this.intCompact = compactValFor(localBigInteger);
/*  849 */     this.intVal = (this.intCompact != -9223372036854775808L ? null : localBigInteger);
/*      */   }
/*      */ 
/*      */   public BigDecimal(double paramDouble, MathContext paramMathContext)
/*      */   {
/*  871 */     this(paramDouble);
/*  872 */     if (paramMathContext.precision > 0)
/*  873 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(BigInteger paramBigInteger)
/*      */   {
/*  884 */     this.intCompact = compactValFor(paramBigInteger);
/*  885 */     this.intVal = (this.intCompact != -9223372036854775808L ? null : paramBigInteger);
/*      */   }
/*      */ 
/*      */   public BigDecimal(BigInteger paramBigInteger, MathContext paramMathContext)
/*      */   {
/*  901 */     this(paramBigInteger);
/*  902 */     if (paramMathContext.precision > 0)
/*  903 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(BigInteger paramBigInteger, int paramInt)
/*      */   {
/*  917 */     this(paramBigInteger);
/*  918 */     this.scale = paramInt;
/*      */   }
/*      */ 
/*      */   public BigDecimal(BigInteger paramBigInteger, int paramInt, MathContext paramMathContext)
/*      */   {
/*  937 */     this(paramBigInteger);
/*  938 */     this.scale = paramInt;
/*  939 */     if (paramMathContext.precision > 0)
/*  940 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(int paramInt)
/*      */   {
/*  952 */     this.intCompact = paramInt;
/*      */   }
/*      */ 
/*      */   public BigDecimal(int paramInt, MathContext paramMathContext)
/*      */   {
/*  967 */     this.intCompact = paramInt;
/*  968 */     if (paramMathContext.precision > 0)
/*  969 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal(long paramLong)
/*      */   {
/*  980 */     this.intCompact = paramLong;
/*  981 */     this.intVal = (paramLong == -9223372036854775808L ? BigInteger.valueOf(paramLong) : null);
/*      */   }
/*      */ 
/*      */   public BigDecimal(long paramLong, MathContext paramMathContext)
/*      */   {
/*  996 */     this(paramLong);
/*  997 */     if (paramMathContext.precision > 0)
/*  998 */       roundThis(paramMathContext);
/*      */   }
/*      */ 
/*      */   public static BigDecimal valueOf(long paramLong, int paramInt)
/*      */   {
/* 1016 */     if (paramInt == 0)
/* 1017 */       return valueOf(paramLong);
/* 1018 */     if (paramLong == 0L) {
/* 1019 */       if ((paramInt > 0) && (paramInt < ZERO_SCALED_BY.length)) {
/* 1020 */         return ZERO_SCALED_BY[paramInt];
/*      */       }
/* 1022 */       return new BigDecimal(BigInteger.ZERO, 0L, paramInt, 1);
/*      */     }
/* 1024 */     return new BigDecimal(paramLong == -9223372036854775808L ? BigInteger.valueOf(paramLong) : null, paramLong, paramInt, 0);
/*      */   }
/*      */ 
/*      */   public static BigDecimal valueOf(long paramLong)
/*      */   {
/* 1040 */     if ((paramLong >= 0L) && (paramLong < zeroThroughTen.length))
/* 1041 */       return zeroThroughTen[((int)paramLong)];
/* 1042 */     if (paramLong != -9223372036854775808L)
/* 1043 */       return new BigDecimal(null, paramLong, 0, 0);
/* 1044 */     return new BigDecimal(BigInteger.valueOf(paramLong), paramLong, 0, 0);
/*      */   }
/*      */ 
/*      */   public static BigDecimal valueOf(double paramDouble)
/*      */   {
/* 1069 */     return new BigDecimal(Double.toString(paramDouble));
/*      */   }
/*      */ 
/*      */   public BigDecimal add(BigDecimal paramBigDecimal)
/*      */   {
/* 1082 */     long l1 = this.intCompact;
/* 1083 */     long l2 = paramBigDecimal.intCompact;
/* 1084 */     BigInteger localBigInteger1 = l1 != -9223372036854775808L ? null : this.intVal;
/* 1085 */     BigInteger localBigInteger2 = l2 != -9223372036854775808L ? null : paramBigDecimal.intVal;
/* 1086 */     int i = this.scale;
/*      */ 
/* 1088 */     long l3 = i - paramBigDecimal.scale;
/* 1089 */     if (l3 != 0L)
/*      */     {
/*      */       int j;
/* 1090 */       if (l3 < 0L) {
/* 1091 */         j = checkScale(-l3);
/* 1092 */         i = paramBigDecimal.scale;
/* 1093 */         if ((l1 == -9223372036854775808L) || ((l1 = longMultiplyPowerTen(l1, j)) == -9223372036854775808L))
/*      */         {
/* 1095 */           localBigInteger1 = bigMultiplyPowerTen(j);
/*      */         }
/*      */       } else { j = paramBigDecimal.checkScale(l3);
/* 1098 */         if ((l2 == -9223372036854775808L) || ((l2 = longMultiplyPowerTen(l2, j)) == -9223372036854775808L))
/*      */         {
/* 1100 */           localBigInteger2 = paramBigDecimal.bigMultiplyPowerTen(j);
/*      */         } }
/*      */     }
/* 1103 */     if ((l1 != -9223372036854775808L) && (l2 != -9223372036854775808L)) {
/* 1104 */       long l4 = l1 + l2;
/*      */ 
/* 1107 */       if (((l4 ^ l1) & (l4 ^ l2)) >= 0L)
/* 1108 */         return valueOf(l4, i);
/*      */     }
/* 1110 */     if (localBigInteger1 == null)
/* 1111 */       localBigInteger1 = BigInteger.valueOf(l1);
/* 1112 */     if (localBigInteger2 == null)
/* 1113 */       localBigInteger2 = BigInteger.valueOf(l2);
/* 1114 */     BigInteger localBigInteger3 = localBigInteger1.add(localBigInteger2);
/* 1115 */     return localBigInteger1.signum == localBigInteger2.signum ? new BigDecimal(localBigInteger3, -9223372036854775808L, i, 0) : new BigDecimal(localBigInteger3, i);
/*      */   }
/*      */ 
/*      */   public BigDecimal add(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1135 */     if (paramMathContext.precision == 0)
/* 1136 */       return add(paramBigDecimal);
/* 1137 */     BigDecimal localBigDecimal1 = this;
/*      */ 
/* 1140 */     inflate();
/* 1141 */     paramBigDecimal.inflate();
/*      */ 
/* 1146 */     int i = localBigDecimal1.signum() == 0 ? 1 : 0;
/* 1147 */     int j = paramBigDecimal.signum() == 0 ? 1 : 0;
/*      */ 
/* 1149 */     if ((i != 0) || (j != 0)) {
/* 1150 */       int k = Math.max(localBigDecimal1.scale(), paramBigDecimal.scale());
/*      */ 
/* 1154 */       if ((i != 0) && (j != 0)) {
/* 1155 */         return new BigDecimal(BigInteger.ZERO, 0L, k, 0);
/*      */       }
/* 1157 */       BigDecimal localBigDecimal2 = i != 0 ? doRound(paramBigDecimal, paramMathContext) : doRound(localBigDecimal1, paramMathContext);
/*      */ 
/* 1159 */       if (localBigDecimal2.scale() == k)
/* 1160 */         return localBigDecimal2;
/* 1161 */       if (localBigDecimal2.scale() > k) {
/* 1162 */         BigDecimal localBigDecimal3 = new BigDecimal(localBigDecimal2.intVal, localBigDecimal2.intCompact, localBigDecimal2.scale, 0);
/*      */ 
/* 1165 */         localBigDecimal3.stripZerosToMatchScale(k);
/* 1166 */         return localBigDecimal3;
/*      */       }
/* 1168 */       int m = paramMathContext.precision - localBigDecimal2.precision();
/* 1169 */       int n = k - localBigDecimal2.scale();
/*      */ 
/* 1171 */       if (m >= n) {
/* 1172 */         return localBigDecimal2.setScale(k);
/*      */       }
/* 1174 */       return localBigDecimal2.setScale(localBigDecimal2.scale() + m);
/*      */     }
/*      */ 
/* 1179 */     long l = localBigDecimal1.scale - paramBigDecimal.scale;
/* 1180 */     if (l != 0L) {
/* 1181 */       localObject = preAlign(localBigDecimal1, paramBigDecimal, l, paramMathContext);
/* 1182 */       matchScale((BigDecimal[])localObject);
/* 1183 */       localBigDecimal1 = localObject[0];
/* 1184 */       paramBigDecimal = localObject[1];
/*      */     }
/*      */ 
/* 1187 */     Object localObject = new BigDecimal(localBigDecimal1.inflate().add(paramBigDecimal.inflate()), localBigDecimal1.scale);
/*      */ 
/* 1189 */     return doRound((BigDecimal)localObject, paramMathContext);
/*      */   }
/*      */ 
/*      */   private BigDecimal[] preAlign(BigDecimal paramBigDecimal1, BigDecimal paramBigDecimal2, long paramLong, MathContext paramMathContext)
/*      */   {
/* 1216 */     assert (paramLong != 0L);
/*      */     BigDecimal localBigDecimal1;
/*      */     BigDecimal localBigDecimal2;
/* 1220 */     if (paramLong < 0L) {
/* 1221 */       localBigDecimal1 = paramBigDecimal1;
/* 1222 */       localBigDecimal2 = paramBigDecimal2;
/*      */     } else {
/* 1224 */       localBigDecimal1 = paramBigDecimal2;
/* 1225 */       localBigDecimal2 = paramBigDecimal1;
/*      */     }
/*      */ 
/* 1234 */     long l1 = localBigDecimal1.scale - localBigDecimal1.precision() + paramMathContext.precision;
/*      */ 
/* 1245 */     long l2 = localBigDecimal2.scale - localBigDecimal2.precision() + 1L;
/* 1246 */     if ((l2 > localBigDecimal1.scale + 2) && (l2 > l1 + 2L))
/*      */     {
/* 1248 */       localBigDecimal2 = valueOf(localBigDecimal2.signum(), checkScale(Math.max(localBigDecimal1.scale, l1) + 3L));
/*      */     }
/*      */ 
/* 1254 */     BigDecimal[] arrayOfBigDecimal = { localBigDecimal1, localBigDecimal2 };
/* 1255 */     return arrayOfBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal subtract(BigDecimal paramBigDecimal)
/*      */   {
/* 1267 */     return add(paramBigDecimal.negate());
/*      */   }
/*      */ 
/*      */   public BigDecimal subtract(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1285 */     BigDecimal localBigDecimal = paramBigDecimal.negate();
/* 1286 */     if (paramMathContext.precision == 0) {
/* 1287 */       return add(localBigDecimal);
/*      */     }
/* 1289 */     return add(localBigDecimal, paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal multiply(BigDecimal paramBigDecimal)
/*      */   {
/* 1301 */     long l1 = this.intCompact;
/* 1302 */     long l2 = paramBigDecimal.intCompact;
/* 1303 */     int i = checkScale(this.scale + paramBigDecimal.scale);
/*      */ 
/* 1307 */     if ((l1 != -9223372036854775808L) && (l2 != -9223372036854775808L))
/*      */     {
/* 1317 */       long l3 = l1 * l2;
/* 1318 */       long l4 = precision() + paramBigDecimal.precision();
/* 1319 */       if ((l4 < 19L) || ((l4 < 21L) && ((l2 == 0L) || (l3 / l2 == l1))))
/* 1320 */         return valueOf(l3, i);
/* 1321 */       return new BigDecimal(BigInteger.valueOf(l1).multiply(l2), -9223372036854775808L, i, 0);
/*      */     }
/*      */     BigInteger localBigInteger;
/* 1325 */     if ((l1 == -9223372036854775808L) && (l2 == -9223372036854775808L))
/* 1326 */       localBigInteger = this.intVal.multiply(paramBigDecimal.intVal);
/* 1327 */     else if (l1 != -9223372036854775808L)
/* 1328 */       localBigInteger = paramBigDecimal.intVal.multiply(l1);
/*      */     else
/* 1330 */       localBigInteger = this.intVal.multiply(l2);
/* 1331 */     return new BigDecimal(localBigInteger, -9223372036854775808L, i, 0);
/*      */   }
/*      */ 
/*      */   public BigDecimal multiply(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1346 */     if (paramMathContext.precision == 0)
/* 1347 */       return multiply(paramBigDecimal);
/* 1348 */     return doRound(multiply(paramBigDecimal), paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal, int paramInt1, int paramInt2)
/*      */   {
/* 1385 */     if ((paramInt2 < 0) || (paramInt2 > 7)) {
/* 1386 */       throw new IllegalArgumentException("Invalid rounding mode");
/*      */     }
/*      */ 
/* 1392 */     BigDecimal localBigDecimal = this;
/* 1393 */     if (checkScale(paramInt1 + paramBigDecimal.scale) > this.scale)
/* 1394 */       localBigDecimal = setScale(paramInt1 + paramBigDecimal.scale, 7);
/*      */     else {
/* 1396 */       paramBigDecimal = paramBigDecimal.setScale(checkScale(this.scale - paramInt1), 7);
/*      */     }
/* 1398 */     return divideAndRound(localBigDecimal.intCompact, localBigDecimal.intVal, paramBigDecimal.intCompact, paramBigDecimal.intVal, paramInt1, paramInt2, paramInt1);
/*      */   }
/*      */ 
/*      */   private static BigDecimal divideAndRound(long paramLong1, BigInteger paramBigInteger1, long paramLong2, BigInteger paramBigInteger2, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1418 */     long l1 = 0L; long l2 = 0L;
/* 1419 */     MutableBigInteger localMutableBigInteger1 = null;
/* 1420 */     MutableBigInteger localMutableBigInteger2 = null;
/* 1421 */     MutableBigInteger localMutableBigInteger3 = null;
/* 1422 */     int j = (paramLong1 != -9223372036854775808L) && (paramLong2 != -9223372036854775808L) ? 1 : 0;
/*      */     boolean bool1;
/*      */     int i;
/* 1423 */     if (j != 0) {
/* 1424 */       l1 = paramLong1 / paramLong2;
/* 1425 */       if ((paramInt2 == 1) && (paramInt1 == paramInt3))
/* 1426 */         return new BigDecimal(null, l1, paramInt1, 0);
/* 1427 */       l2 = paramLong1 % paramLong2;
/* 1428 */       bool1 = l2 == 0L;
/* 1429 */       i = (paramLong1 < 0L ? 1 : 0) == (paramLong2 < 0L ? 1 : 0) ? 1 : -1;
/*      */     } else {
/* 1431 */       if (paramBigInteger1 == null) {
/* 1432 */         paramBigInteger1 = BigInteger.valueOf(paramLong1);
/*      */       }
/* 1434 */       MutableBigInteger localMutableBigInteger4 = new MutableBigInteger(paramBigInteger1.mag);
/* 1435 */       localMutableBigInteger1 = new MutableBigInteger();
/* 1436 */       if (paramLong2 != -9223372036854775808L) {
/* 1437 */         l2 = localMutableBigInteger4.divide(paramLong2, localMutableBigInteger1);
/* 1438 */         bool1 = l2 == 0L;
/* 1439 */         i = paramLong2 < 0L ? -paramBigInteger1.signum : paramBigInteger1.signum;
/*      */       } else {
/* 1441 */         localMutableBigInteger3 = new MutableBigInteger(paramBigInteger2.mag);
/* 1442 */         localMutableBigInteger2 = localMutableBigInteger4.divide(localMutableBigInteger3, localMutableBigInteger1);
/* 1443 */         bool1 = localMutableBigInteger2.isZero();
/* 1444 */         i = paramBigInteger1.signum != paramBigInteger2.signum ? -1 : 1;
/*      */       }
/*      */     }
/* 1447 */     boolean bool2 = false;
/* 1448 */     if (!bool1)
/*      */     {
/* 1451 */       if (paramInt2 == 7)
/* 1452 */         throw new ArithmeticException("Rounding necessary");
/* 1453 */       if (paramInt2 == 0) {
/* 1454 */         bool2 = true;
/* 1455 */       } else if (paramInt2 == 1) {
/* 1456 */         bool2 = false;
/* 1457 */       } else if (paramInt2 == 2) {
/* 1458 */         bool2 = i > 0;
/* 1459 */       } else if (paramInt2 == 3) {
/* 1460 */         bool2 = i < 0;
/*      */       }
/*      */       else
/*      */       {
/*      */         int k;
/* 1462 */         if ((j != 0) || (paramLong2 != -9223372036854775808L)) {
/* 1463 */           if ((l2 <= -4611686018427387904L) || (l2 > 4611686018427387903L))
/* 1464 */             k = 1;
/*      */           else
/* 1466 */             k = longCompareMagnitude(2L * l2, paramLong2);
/*      */         }
/*      */         else {
/* 1469 */           k = localMutableBigInteger2.compareHalf(localMutableBigInteger3);
/*      */         }
/* 1471 */         if (k < 0)
/* 1472 */           bool2 = false;
/* 1473 */         else if (k > 0)
/* 1474 */           bool2 = true;
/* 1475 */         else if (paramInt2 == 4)
/* 1476 */           bool2 = true;
/* 1477 */         else if (paramInt2 == 5)
/* 1478 */           bool2 = false;
/*      */         else
/* 1480 */           bool2 = j != 0 ? false : (l1 & 1L) != 0L ? true : localMutableBigInteger1.isOdd();
/*      */       }
/*      */     }
/*      */     BigDecimal localBigDecimal;
/* 1484 */     if (j != 0) {
/* 1485 */       localBigDecimal = new BigDecimal(null, bool2 ? l1 + i : l1, paramInt1, 0);
/*      */     } else {
/* 1487 */       if (bool2)
/* 1488 */         localMutableBigInteger1.add(MutableBigInteger.ONE);
/* 1489 */       localBigDecimal = localMutableBigInteger1.toBigDecimal(i, paramInt1);
/*      */     }
/* 1491 */     if ((bool1) && (paramInt3 != paramInt1))
/* 1492 */       localBigDecimal.stripZerosToMatchScale(paramInt3);
/* 1493 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal, int paramInt, RoundingMode paramRoundingMode)
/*      */   {
/* 1513 */     return divide(paramBigDecimal, paramInt, paramRoundingMode.oldMode);
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal, int paramInt)
/*      */   {
/* 1544 */     return divide(paramBigDecimal, this.scale, paramInt);
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal, RoundingMode paramRoundingMode)
/*      */   {
/* 1563 */     return divide(paramBigDecimal, this.scale, paramRoundingMode.oldMode);
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal)
/*      */   {
/* 1584 */     if (paramBigDecimal.signum() == 0) {
/* 1585 */       if (signum() == 0)
/* 1586 */         throw new ArithmeticException("Division undefined");
/* 1587 */       throw new ArithmeticException("Division by zero");
/*      */     }
/*      */ 
/* 1591 */     int i = saturateLong(this.scale - paramBigDecimal.scale);
/* 1592 */     if (signum() == 0) {
/* 1593 */       return (i >= 0) && (i < ZERO_SCALED_BY.length) ? ZERO_SCALED_BY[i] : valueOf(0L, i);
/*      */     }
/*      */ 
/* 1598 */     inflate();
/* 1599 */     paramBigDecimal.inflate();
/*      */ 
/* 1608 */     MathContext localMathContext = new MathContext((int)Math.min(precision() + ()Math.ceil(10.0D * paramBigDecimal.precision() / 3.0D), 2147483647L), RoundingMode.UNNECESSARY);
/*      */     BigDecimal localBigDecimal;
/*      */     try
/*      */     {
/* 1614 */       localBigDecimal = divide(paramBigDecimal, localMathContext);
/*      */     } catch (ArithmeticException localArithmeticException) {
/* 1616 */       throw new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.");
/*      */     }
/*      */ 
/* 1620 */     int j = localBigDecimal.scale();
/*      */ 
/* 1627 */     if (i > j) {
/* 1628 */       return localBigDecimal.setScale(i, 7);
/*      */     }
/* 1630 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal divide(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1648 */     int i = paramMathContext.precision;
/* 1649 */     if (i == 0) {
/* 1650 */       return divide(paramBigDecimal);
/*      */     }
/* 1652 */     BigDecimal localBigDecimal1 = this;
/* 1653 */     long l = localBigDecimal1.scale - paramBigDecimal.scale;
/*      */ 
/* 1666 */     if (paramBigDecimal.signum() == 0) {
/* 1667 */       if (localBigDecimal1.signum() == 0)
/* 1668 */         throw new ArithmeticException("Division undefined");
/* 1669 */       throw new ArithmeticException("Division by zero");
/*      */     }
/* 1671 */     if (localBigDecimal1.signum() == 0) {
/* 1672 */       return new BigDecimal(BigInteger.ZERO, 0L, saturateLong(l), 1);
/*      */     }
/*      */ 
/* 1676 */     int j = localBigDecimal1.precision();
/* 1677 */     int k = paramBigDecimal.precision();
/* 1678 */     localBigDecimal1 = new BigDecimal(localBigDecimal1.intVal, localBigDecimal1.intCompact, j, j);
/*      */ 
/* 1680 */     paramBigDecimal = new BigDecimal(paramBigDecimal.intVal, paramBigDecimal.intCompact, k, k);
/*      */ 
/* 1682 */     if (localBigDecimal1.compareMagnitude(paramBigDecimal) > 0) {
/* 1683 */       k = --paramBigDecimal.scale;
/*      */     }
/*      */ 
/* 1689 */     int m = checkScale(l + k - j + i);
/* 1690 */     if (checkScale(i + k) > j)
/* 1691 */       localBigDecimal1 = localBigDecimal1.setScale(i + k, 7);
/*      */     else {
/* 1693 */       paramBigDecimal = paramBigDecimal.setScale(checkScale(j - i), 7);
/*      */     }
/* 1695 */     BigDecimal localBigDecimal2 = divideAndRound(localBigDecimal1.intCompact, localBigDecimal1.intVal, paramBigDecimal.intCompact, paramBigDecimal.intVal, m, paramMathContext.roundingMode.oldMode, checkScale(l));
/*      */ 
/* 1700 */     localBigDecimal2 = doRound(localBigDecimal2, paramMathContext);
/*      */ 
/* 1702 */     return localBigDecimal2;
/*      */   }
/*      */ 
/*      */   public BigDecimal divideToIntegralValue(BigDecimal paramBigDecimal)
/*      */   {
/* 1718 */     int i = saturateLong(this.scale - paramBigDecimal.scale);
/* 1719 */     if (compareMagnitude(paramBigDecimal) < 0)
/*      */     {
/* 1721 */       return valueOf(0L, i);
/*      */     }
/*      */ 
/* 1724 */     if ((signum() == 0) && (paramBigDecimal.signum() != 0)) {
/* 1725 */       return setScale(i, 7);
/*      */     }
/*      */ 
/* 1730 */     int j = (int)Math.min(precision() + ()Math.ceil(10.0D * paramBigDecimal.precision() / 3.0D) + Math.abs(scale() - paramBigDecimal.scale()) + 2L, 2147483647L);
/*      */ 
/* 1734 */     BigDecimal localBigDecimal = divide(paramBigDecimal, new MathContext(j, RoundingMode.DOWN));
/*      */ 
/* 1736 */     if (localBigDecimal.scale > 0) {
/* 1737 */       localBigDecimal = localBigDecimal.setScale(0, RoundingMode.DOWN);
/* 1738 */       localBigDecimal.stripZerosToMatchScale(i);
/*      */     }
/*      */ 
/* 1741 */     if (localBigDecimal.scale < i)
/*      */     {
/* 1743 */       localBigDecimal = localBigDecimal.setScale(i, 7);
/*      */     }
/* 1745 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal divideToIntegralValue(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1769 */     if ((paramMathContext.precision == 0) || (compareMagnitude(paramBigDecimal) < 0))
/*      */     {
/* 1771 */       return divideToIntegralValue(paramBigDecimal);
/*      */     }
/*      */ 
/* 1774 */     int i = saturateLong(this.scale - paramBigDecimal.scale);
/*      */ 
/* 1783 */     BigDecimal localBigDecimal1 = divide(paramBigDecimal, new MathContext(paramMathContext.precision, RoundingMode.DOWN));
/*      */ 
/* 1786 */     if (localBigDecimal1.scale() < 0)
/*      */     {
/* 1792 */       BigDecimal localBigDecimal2 = localBigDecimal1.multiply(paramBigDecimal);
/*      */ 
/* 1795 */       if (subtract(localBigDecimal2).compareMagnitude(paramBigDecimal) >= 0)
/* 1796 */         throw new ArithmeticException("Division impossible");
/*      */     }
/* 1798 */     else if (localBigDecimal1.scale() > 0)
/*      */     {
/* 1804 */       localBigDecimal1 = localBigDecimal1.setScale(0, RoundingMode.DOWN);
/*      */     }
/*      */     int j;
/* 1809 */     if ((i > localBigDecimal1.scale()) && ((j = paramMathContext.precision - localBigDecimal1.precision()) > 0))
/*      */     {
/* 1811 */       return localBigDecimal1.setScale(localBigDecimal1.scale() + Math.min(j, i - localBigDecimal1.scale));
/*      */     }
/*      */ 
/* 1814 */     localBigDecimal1.stripZerosToMatchScale(i);
/* 1815 */     return localBigDecimal1;
/*      */   }
/*      */ 
/*      */   public BigDecimal remainder(BigDecimal paramBigDecimal)
/*      */   {
/* 1833 */     BigDecimal[] arrayOfBigDecimal = divideAndRemainder(paramBigDecimal);
/* 1834 */     return arrayOfBigDecimal[1];
/*      */   }
/*      */ 
/*      */   public BigDecimal remainder(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1863 */     BigDecimal[] arrayOfBigDecimal = divideAndRemainder(paramBigDecimal, paramMathContext);
/* 1864 */     return arrayOfBigDecimal[1];
/*      */   }
/*      */ 
/*      */   public BigDecimal[] divideAndRemainder(BigDecimal paramBigDecimal)
/*      */   {
/* 1889 */     BigDecimal[] arrayOfBigDecimal = new BigDecimal[2];
/*      */ 
/* 1891 */     arrayOfBigDecimal[0] = divideToIntegralValue(paramBigDecimal);
/* 1892 */     arrayOfBigDecimal[1] = subtract(arrayOfBigDecimal[0].multiply(paramBigDecimal));
/* 1893 */     return arrayOfBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal[] divideAndRemainder(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 1923 */     if (paramMathContext.precision == 0) {
/* 1924 */       return divideAndRemainder(paramBigDecimal);
/*      */     }
/* 1926 */     BigDecimal[] arrayOfBigDecimal = new BigDecimal[2];
/* 1927 */     BigDecimal localBigDecimal = this;
/*      */ 
/* 1929 */     arrayOfBigDecimal[0] = localBigDecimal.divideToIntegralValue(paramBigDecimal, paramMathContext);
/* 1930 */     arrayOfBigDecimal[1] = localBigDecimal.subtract(arrayOfBigDecimal[0].multiply(paramBigDecimal));
/* 1931 */     return arrayOfBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal pow(int paramInt)
/*      */   {
/* 1952 */     if ((paramInt < 0) || (paramInt > 999999999)) {
/* 1953 */       throw new ArithmeticException("Invalid operation");
/*      */     }
/*      */ 
/* 1956 */     int i = checkScale(this.scale * paramInt);
/* 1957 */     inflate();
/* 1958 */     return new BigDecimal(this.intVal.pow(paramInt), i);
/*      */   }
/*      */ 
/*      */   public BigDecimal pow(int paramInt, MathContext paramMathContext)
/*      */   {
/* 2013 */     if (paramMathContext.precision == 0)
/* 2014 */       return pow(paramInt);
/* 2015 */     if ((paramInt < -999999999) || (paramInt > 999999999))
/* 2016 */       throw new ArithmeticException("Invalid operation");
/* 2017 */     if (paramInt == 0)
/* 2018 */       return ONE;
/* 2019 */     inflate();
/* 2020 */     BigDecimal localBigDecimal1 = this;
/* 2021 */     MathContext localMathContext = paramMathContext;
/* 2022 */     int i = Math.abs(paramInt);
/* 2023 */     if (paramMathContext.precision > 0)
/*      */     {
/* 2025 */       int j = longDigitLength(i);
/* 2026 */       if (j > paramMathContext.precision)
/* 2027 */         throw new ArithmeticException("Invalid operation");
/* 2028 */       localMathContext = new MathContext(paramMathContext.precision + j + 1, paramMathContext.roundingMode);
/*      */     }
/*      */ 
/* 2032 */     BigDecimal localBigDecimal2 = ONE;
/* 2033 */     int k = 0;
/* 2034 */     for (int m = 1; ; m++) {
/* 2035 */       i += i;
/* 2036 */       if (i < 0) {
/* 2037 */         k = 1;
/* 2038 */         localBigDecimal2 = localBigDecimal2.multiply(localBigDecimal1, localMathContext);
/*      */       }
/* 2040 */       if (m == 31)
/*      */         break;
/* 2042 */       if (k != 0) {
/* 2043 */         localBigDecimal2 = localBigDecimal2.multiply(localBigDecimal2, localMathContext);
/*      */       }
/*      */     }
/*      */ 
/* 2047 */     if (paramInt < 0) {
/* 2048 */       localBigDecimal2 = ONE.divide(localBigDecimal2, localMathContext);
/*      */     }
/* 2050 */     return doRound(localBigDecimal2, paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal abs()
/*      */   {
/* 2061 */     return signum() < 0 ? negate() : this;
/*      */   }
/*      */ 
/*      */   public BigDecimal abs(MathContext paramMathContext)
/*      */   {
/* 2076 */     return signum() < 0 ? negate(paramMathContext) : plus(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal negate()
/*      */   {
/*      */     BigDecimal localBigDecimal;
/* 2087 */     if (this.intCompact != -9223372036854775808L) {
/* 2088 */       localBigDecimal = valueOf(-this.intCompact, this.scale);
/*      */     } else {
/* 2090 */       localBigDecimal = new BigDecimal(this.intVal.negate(), this.scale);
/* 2091 */       localBigDecimal.precision = this.precision;
/*      */     }
/* 2093 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal negate(MathContext paramMathContext)
/*      */   {
/* 2107 */     return negate().plus(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal plus()
/*      */   {
/* 2123 */     return this;
/*      */   }
/*      */ 
/*      */   public BigDecimal plus(MathContext paramMathContext)
/*      */   {
/* 2142 */     if (paramMathContext.precision == 0)
/* 2143 */       return this;
/* 2144 */     return doRound(this, paramMathContext);
/*      */   }
/*      */ 
/*      */   public int signum()
/*      */   {
/* 2154 */     return this.intCompact != -9223372036854775808L ? Long.signum(this.intCompact) : this.intVal.signum();
/*      */   }
/*      */ 
/*      */   public int scale()
/*      */   {
/* 2170 */     return this.scale;
/*      */   }
/*      */ 
/*      */   public int precision()
/*      */   {
/* 2183 */     int i = this.precision;
/* 2184 */     if (i == 0) {
/* 2185 */       long l = this.intCompact;
/* 2186 */       if (l != -9223372036854775808L)
/* 2187 */         i = longDigitLength(l);
/*      */       else
/* 2189 */         i = bigDigitLength(inflate());
/* 2190 */       this.precision = i;
/*      */     }
/* 2192 */     return i;
/*      */   }
/*      */ 
/*      */   public BigInteger unscaledValue()
/*      */   {
/* 2205 */     return inflate();
/*      */   }
/*      */ 
/*      */   public BigDecimal round(MathContext paramMathContext)
/*      */   {
/* 2302 */     return plus(paramMathContext);
/*      */   }
/*      */ 
/*      */   public BigDecimal setScale(int paramInt, RoundingMode paramRoundingMode)
/*      */   {
/* 2335 */     return setScale(paramInt, paramRoundingMode.oldMode);
/*      */   }
/*      */ 
/*      */   public BigDecimal setScale(int paramInt1, int paramInt2)
/*      */   {
/* 2379 */     if ((paramInt2 < 0) || (paramInt2 > 7)) {
/* 2380 */       throw new IllegalArgumentException("Invalid rounding mode");
/*      */     }
/* 2382 */     int i = this.scale;
/* 2383 */     if (paramInt1 == i)
/* 2384 */       return this;
/* 2385 */     if (signum() == 0) {
/* 2386 */       return valueOf(0L, paramInt1);
/*      */     }
/* 2388 */     long l = this.intCompact;
/* 2389 */     if (paramInt1 > i) {
/* 2390 */       j = checkScale(paramInt1 - i);
/* 2391 */       BigInteger localBigInteger = null;
/* 2392 */       if ((l == -9223372036854775808L) || ((l = longMultiplyPowerTen(l, j)) == -9223372036854775808L))
/*      */       {
/* 2394 */         localBigInteger = bigMultiplyPowerTen(j);
/* 2395 */       }return new BigDecimal(localBigInteger, l, paramInt1, this.precision > 0 ? this.precision + j : 0);
/*      */     }
/*      */ 
/* 2400 */     int j = checkScale(i - paramInt1);
/* 2401 */     if (j < LONG_TEN_POWERS_TABLE.length) {
/* 2402 */       return divideAndRound(l, this.intVal, LONG_TEN_POWERS_TABLE[j], null, paramInt1, paramInt2, paramInt1);
/*      */     }
/*      */ 
/* 2406 */     return divideAndRound(l, this.intVal, -9223372036854775808L, bigTenToThe(j), paramInt1, paramInt2, paramInt1);
/*      */   }
/*      */ 
/*      */   public BigDecimal setScale(int paramInt)
/*      */   {
/* 2449 */     return setScale(paramInt, 7);
/*      */   }
/*      */ 
/*      */   public BigDecimal movePointLeft(int paramInt)
/*      */   {
/* 2471 */     int i = checkScale(this.scale + paramInt);
/* 2472 */     BigDecimal localBigDecimal = new BigDecimal(this.intVal, this.intCompact, i, 0);
/* 2473 */     return localBigDecimal.scale < 0 ? localBigDecimal.setScale(0, 7) : localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal movePointRight(int paramInt)
/*      */   {
/* 2493 */     int i = checkScale(this.scale - paramInt);
/* 2494 */     BigDecimal localBigDecimal = new BigDecimal(this.intVal, this.intCompact, i, 0);
/* 2495 */     return localBigDecimal.scale < 0 ? localBigDecimal.setScale(0, 7) : localBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal scaleByPowerOfTen(int paramInt)
/*      */   {
/* 2509 */     return new BigDecimal(this.intVal, this.intCompact, checkScale(this.scale - paramInt), this.precision);
/*      */   }
/*      */ 
/*      */   public BigDecimal stripTrailingZeros()
/*      */   {
/* 2527 */     inflate();
/* 2528 */     BigDecimal localBigDecimal = new BigDecimal(this.intVal, this.scale);
/* 2529 */     localBigDecimal.stripZerosToMatchScale(-9223372036854775808L);
/* 2530 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   public int compareTo(BigDecimal paramBigDecimal)
/*      */   {
/* 2554 */     if (this.scale == paramBigDecimal.scale) {
/* 2555 */       long l1 = this.intCompact;
/* 2556 */       long l2 = paramBigDecimal.intCompact;
/* 2557 */       if ((l1 != -9223372036854775808L) && (l2 != -9223372036854775808L))
/* 2558 */         return l1 != l2 ? -1 : l1 > l2 ? 1 : 0;
/*      */     }
/* 2560 */     int i = signum();
/* 2561 */     int j = paramBigDecimal.signum();
/* 2562 */     if (i != j)
/* 2563 */       return i > j ? 1 : -1;
/* 2564 */     if (i == 0)
/* 2565 */       return 0;
/* 2566 */     int k = compareMagnitude(paramBigDecimal);
/* 2567 */     return i > 0 ? k : -k;
/*      */   }
/*      */ 
/*      */   private int compareMagnitude(BigDecimal paramBigDecimal)
/*      */   {
/* 2575 */     long l1 = paramBigDecimal.intCompact;
/* 2576 */     long l2 = this.intCompact;
/* 2577 */     if (l2 == 0L)
/* 2578 */       return l1 == 0L ? 0 : -1;
/* 2579 */     if (l1 == 0L) {
/* 2580 */       return 1;
/*      */     }
/* 2582 */     int i = this.scale - paramBigDecimal.scale;
/* 2583 */     if (i != 0)
/*      */     {
/* 2585 */       int j = precision() - this.scale;
/* 2586 */       int k = paramBigDecimal.precision() - paramBigDecimal.scale;
/* 2587 */       if (j < k)
/* 2588 */         return -1;
/* 2589 */       if (j > k)
/* 2590 */         return 1;
/* 2591 */       BigInteger localBigInteger = null;
/* 2592 */       if (i < 0) {
/* 2593 */         if (((l2 == -9223372036854775808L) || ((l2 = longMultiplyPowerTen(l2, -i)) == -9223372036854775808L)) && (l1 == -9223372036854775808L))
/*      */         {
/* 2596 */           localBigInteger = bigMultiplyPowerTen(-i);
/* 2597 */           return localBigInteger.compareMagnitude(paramBigDecimal.intVal);
/*      */         }
/*      */       }
/* 2600 */       else if (((l1 == -9223372036854775808L) || ((l1 = longMultiplyPowerTen(l1, i)) == -9223372036854775808L)) && (l2 == -9223372036854775808L))
/*      */       {
/* 2603 */         localBigInteger = paramBigDecimal.bigMultiplyPowerTen(i);
/* 2604 */         return this.intVal.compareMagnitude(localBigInteger);
/*      */       }
/*      */     }
/*      */ 
/* 2608 */     if (l2 != -9223372036854775808L)
/* 2609 */       return l1 != -9223372036854775808L ? longCompareMagnitude(l2, l1) : -1;
/* 2610 */     if (l1 != -9223372036854775808L) {
/* 2611 */       return 1;
/*      */     }
/* 2613 */     return this.intVal.compareMagnitude(paramBigDecimal.intVal);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2634 */     if (!(paramObject instanceof BigDecimal))
/* 2635 */       return false;
/* 2636 */     BigDecimal localBigDecimal = (BigDecimal)paramObject;
/* 2637 */     if (paramObject == this)
/* 2638 */       return true;
/* 2639 */     if (this.scale != localBigDecimal.scale)
/* 2640 */       return false;
/* 2641 */     long l1 = this.intCompact;
/* 2642 */     long l2 = localBigDecimal.intCompact;
/* 2643 */     if (l1 != -9223372036854775808L) {
/* 2644 */       if (l2 == -9223372036854775808L)
/* 2645 */         l2 = compactValFor(localBigDecimal.intVal);
/* 2646 */       return l2 == l1;
/* 2647 */     }if (l2 != -9223372036854775808L) {
/* 2648 */       return l2 == compactValFor(this.intVal);
/*      */     }
/* 2650 */     return inflate().equals(localBigDecimal.inflate());
/*      */   }
/*      */ 
/*      */   public BigDecimal min(BigDecimal paramBigDecimal)
/*      */   {
/* 2665 */     return compareTo(paramBigDecimal) <= 0 ? this : paramBigDecimal;
/*      */   }
/*      */ 
/*      */   public BigDecimal max(BigDecimal paramBigDecimal)
/*      */   {
/* 2679 */     return compareTo(paramBigDecimal) >= 0 ? this : paramBigDecimal;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 2695 */     if (this.intCompact != -9223372036854775808L) {
/* 2696 */       long l = this.intCompact < 0L ? -this.intCompact : this.intCompact;
/* 2697 */       int i = (int)((int)(l >>> 32) * 31 + (l & 0xFFFFFFFF));
/*      */ 
/* 2699 */       return 31 * (this.intCompact < 0L ? -i : i) + this.scale;
/*      */     }
/* 2701 */     return 31 * this.intVal.hashCode() + this.scale;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2807 */     String str = this.stringCache;
/* 2808 */     if (str == null)
/* 2809 */       this.stringCache = (str = layoutChars(true));
/* 2810 */     return str;
/*      */   }
/*      */ 
/*      */   public String toEngineeringString()
/*      */   {
/* 2838 */     return layoutChars(false);
/*      */   }
/*      */ 
/*      */   public String toPlainString()
/*      */   {
/* 2875 */     BigDecimal localBigDecimal = this;
/* 2876 */     if (localBigDecimal.scale < 0)
/* 2877 */       localBigDecimal = localBigDecimal.setScale(0);
/* 2878 */     localBigDecimal.inflate();
/* 2879 */     if (localBigDecimal.scale == 0)
/* 2880 */       return localBigDecimal.intVal.toString();
/* 2881 */     return localBigDecimal.getValueString(localBigDecimal.signum(), localBigDecimal.intVal.abs().toString(), localBigDecimal.scale);
/*      */   }
/*      */ 
/*      */   private String getValueString(int paramInt1, String paramString, int paramInt2)
/*      */   {
/* 2888 */     int i = paramString.length() - paramInt2;
/* 2889 */     if (i == 0)
/* 2890 */       return (paramInt1 < 0 ? "-0." : "0.") + paramString;
/*      */     StringBuilder localStringBuilder;
/* 2891 */     if (i > 0) {
/* 2892 */       localStringBuilder = new StringBuilder(paramString);
/* 2893 */       localStringBuilder.insert(i, '.');
/* 2894 */       if (paramInt1 < 0)
/* 2895 */         localStringBuilder.insert(0, '-');
/*      */     } else {
/* 2897 */       localStringBuilder = new StringBuilder(3 - i + paramString.length());
/* 2898 */       localStringBuilder.append(paramInt1 < 0 ? "-0." : "0.");
/* 2899 */       for (int j = 0; j < -i; j++)
/* 2900 */         localStringBuilder.append('0');
/* 2901 */       localStringBuilder.append(paramString);
/*      */     }
/* 2903 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public BigInteger toBigInteger()
/*      */   {
/* 2925 */     return setScale(0, 1).inflate();
/*      */   }
/*      */ 
/*      */   public BigInteger toBigIntegerExact()
/*      */   {
/* 2940 */     return setScale(0, 7).inflate();
/*      */   }
/*      */ 
/*      */   public long longValue()
/*      */   {
/* 2960 */     return (this.intCompact != -9223372036854775808L) && (this.scale == 0) ? this.intCompact : toBigInteger().longValue();
/*      */   }
/*      */ 
/*      */   public long longValueExact()
/*      */   {
/* 2978 */     if ((this.intCompact != -9223372036854775808L) && (this.scale == 0)) {
/* 2979 */       return this.intCompact;
/*      */     }
/* 2981 */     if (precision() - this.scale > 19) {
/* 2982 */       throw new ArithmeticException("Overflow");
/*      */     }
/*      */ 
/* 2985 */     if (signum() == 0)
/* 2986 */       return 0L;
/* 2987 */     if (precision() - this.scale <= 0) {
/* 2988 */       throw new ArithmeticException("Rounding necessary");
/*      */     }
/* 2990 */     BigDecimal localBigDecimal = setScale(0, 7);
/* 2991 */     if (localBigDecimal.precision() >= 19)
/* 2992 */       LongOverflow.check(localBigDecimal);
/* 2993 */     return localBigDecimal.inflate().longValue();
/*      */   }
/*      */ 
/*      */   public int intValue()
/*      */   {
/* 3028 */     return (this.intCompact != -9223372036854775808L) && (this.scale == 0) ? (int)this.intCompact : toBigInteger().intValue();
/*      */   }
/*      */ 
/*      */   public int intValueExact()
/*      */   {
/* 3047 */     long l = longValueExact();
/* 3048 */     if ((int)l != l)
/* 3049 */       throw new ArithmeticException("Overflow");
/* 3050 */     return (int)l;
/*      */   }
/*      */ 
/*      */   public short shortValueExact()
/*      */   {
/* 3067 */     long l = longValueExact();
/* 3068 */     if ((short)(int)l != l)
/* 3069 */       throw new ArithmeticException("Overflow");
/* 3070 */     return (short)(int)l;
/*      */   }
/*      */ 
/*      */   public byte byteValueExact()
/*      */   {
/* 3087 */     long l = longValueExact();
/* 3088 */     if ((byte)(int)l != l)
/* 3089 */       throw new ArithmeticException("Overflow");
/* 3090 */     return (byte)(int)l;
/*      */   }
/*      */ 
/*      */   public float floatValue()
/*      */   {
/* 3110 */     if ((this.scale == 0) && (this.intCompact != -9223372036854775808L)) {
/* 3111 */       return (float)this.intCompact;
/*      */     }
/* 3113 */     return Float.parseFloat(toString());
/*      */   }
/*      */ 
/*      */   public double doubleValue()
/*      */   {
/* 3133 */     if ((this.scale == 0) && (this.intCompact != -9223372036854775808L)) {
/* 3134 */       return this.intCompact;
/*      */     }
/* 3136 */     return Double.parseDouble(toString());
/*      */   }
/*      */ 
/*      */   public BigDecimal ulp()
/*      */   {
/* 3154 */     return valueOf(1L, scale());
/*      */   }
/*      */ 
/*      */   private String layoutChars(boolean paramBoolean)
/*      */   {
/* 3267 */     if (this.scale == 0) {
/* 3268 */       return this.intCompact != -9223372036854775808L ? Long.toString(this.intCompact) : this.intVal.toString();
/*      */     }
/*      */ 
/* 3272 */     StringBuilderHelper localStringBuilderHelper = (StringBuilderHelper)threadLocalStringBuilderHelper.get();
/*      */     int i;
/*      */     char[] arrayOfChar;
/* 3276 */     if (this.intCompact != -9223372036854775808L) {
/* 3277 */       i = localStringBuilderHelper.putIntCompact(Math.abs(this.intCompact));
/* 3278 */       arrayOfChar = localStringBuilderHelper.getCompactCharArray();
/*      */     } else {
/* 3280 */       i = 0;
/* 3281 */       arrayOfChar = this.intVal.abs().toString().toCharArray();
/*      */     }
/*      */ 
/* 3288 */     StringBuilder localStringBuilder = localStringBuilderHelper.getStringBuilder();
/* 3289 */     if (signum() < 0)
/* 3290 */       localStringBuilder.append('-');
/* 3291 */     int j = arrayOfChar.length - i;
/* 3292 */     long l = -this.scale + (j - 1);
/*      */     int k;
/* 3293 */     if ((this.scale >= 0) && (l >= -6L)) {
/* 3294 */       k = this.scale - j;
/* 3295 */       if (k >= 0) {
/* 3296 */         localStringBuilder.append('0');
/* 3297 */         localStringBuilder.append('.');
/* 3298 */         for (; k > 0; k--) {
/* 3299 */           localStringBuilder.append('0');
/*      */         }
/* 3301 */         localStringBuilder.append(arrayOfChar, i, j);
/*      */       } else {
/* 3303 */         localStringBuilder.append(arrayOfChar, i, -k);
/* 3304 */         localStringBuilder.append('.');
/* 3305 */         localStringBuilder.append(arrayOfChar, -k + i, this.scale);
/*      */       }
/*      */     } else {
/* 3308 */       if (paramBoolean) {
/* 3309 */         localStringBuilder.append(arrayOfChar[i]);
/* 3310 */         if (j > 1) {
/* 3311 */           localStringBuilder.append('.');
/* 3312 */           localStringBuilder.append(arrayOfChar, i + 1, j - 1);
/*      */         }
/*      */       } else {
/* 3315 */         k = (int)(l % 3L);
/* 3316 */         if (k < 0)
/* 3317 */           k += 3;
/* 3318 */         l -= k;
/* 3319 */         k++;
/* 3320 */         if (signum() == 0) {
/* 3321 */           switch (k) {
/*      */           case 1:
/* 3323 */             localStringBuilder.append('0');
/* 3324 */             break;
/*      */           case 2:
/* 3326 */             localStringBuilder.append("0.00");
/* 3327 */             l += 3L;
/* 3328 */             break;
/*      */           case 3:
/* 3330 */             localStringBuilder.append("0.0");
/* 3331 */             l += 3L;
/* 3332 */             break;
/*      */           default:
/* 3334 */             throw new AssertionError("Unexpected sig value " + k);
/*      */           }
/* 3336 */         } else if (k >= j) {
/* 3337 */           localStringBuilder.append(arrayOfChar, i, j);
/*      */ 
/* 3339 */           for (int m = k - j; m > 0; m--)
/* 3340 */             localStringBuilder.append('0');
/*      */         } else {
/* 3342 */           localStringBuilder.append(arrayOfChar, i, k);
/* 3343 */           localStringBuilder.append('.');
/* 3344 */           localStringBuilder.append(arrayOfChar, i + k, j - k);
/*      */         }
/*      */       }
/* 3347 */       if (l != 0L) {
/* 3348 */         localStringBuilder.append('E');
/* 3349 */         if (l > 0L)
/* 3350 */           localStringBuilder.append('+');
/* 3351 */         localStringBuilder.append(l);
/*      */       }
/*      */     }
/* 3354 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static BigInteger bigTenToThe(int paramInt)
/*      */   {
/* 3364 */     if (paramInt < 0) {
/* 3365 */       return BigInteger.ZERO;
/*      */     }
/* 3367 */     if (paramInt < BIG_TEN_POWERS_TABLE_MAX) {
/* 3368 */       localObject = BIG_TEN_POWERS_TABLE;
/* 3369 */       if (paramInt < localObject.length) {
/* 3370 */         return localObject[paramInt];
/*      */       }
/* 3372 */       return expandBigIntegerTenPowers(paramInt);
/*      */     }
/*      */ 
/* 3376 */     Object localObject = new char[paramInt + 1];
/* 3377 */     localObject[0] = 49;
/* 3378 */     for (int i = 1; i <= paramInt; i++)
/* 3379 */       localObject[i] = 48;
/* 3380 */     return new BigInteger((char[])localObject);
/*      */   }
/*      */ 
/*      */   private static BigInteger expandBigIntegerTenPowers(int paramInt)
/*      */   {
/* 3392 */     synchronized (BigDecimal.class) {
/* 3393 */       BigInteger[] arrayOfBigInteger = BIG_TEN_POWERS_TABLE;
/* 3394 */       int i = arrayOfBigInteger.length;
/*      */ 
/* 3397 */       if (i <= paramInt) {
/* 3398 */         int j = i << 1;
/* 3399 */         while (j <= paramInt)
/* 3400 */           j <<= 1;
/* 3401 */         arrayOfBigInteger = (BigInteger[])Arrays.copyOf(arrayOfBigInteger, j);
/* 3402 */         for (int k = i; k < j; k++) {
/* 3403 */           arrayOfBigInteger[k] = arrayOfBigInteger[(k - 1)].multiply(BigInteger.TEN);
/*      */         }
/*      */ 
/* 3408 */         BIG_TEN_POWERS_TABLE = arrayOfBigInteger;
/*      */       }
/* 3410 */       return arrayOfBigInteger[paramInt];
/*      */     }
/*      */   }
/*      */ 
/*      */   private static long longMultiplyPowerTen(long paramLong, int paramInt)
/*      */   {
/* 3485 */     if ((paramLong == 0L) || (paramInt <= 0))
/* 3486 */       return paramLong;
/* 3487 */     long[] arrayOfLong1 = LONG_TEN_POWERS_TABLE;
/* 3488 */     long[] arrayOfLong2 = THRESHOLDS_TABLE;
/* 3489 */     if ((paramInt < arrayOfLong1.length) && (paramInt < arrayOfLong2.length)) {
/* 3490 */       long l = arrayOfLong1[paramInt];
/* 3491 */       if (paramLong == 1L)
/* 3492 */         return l;
/* 3493 */       if (Math.abs(paramLong) <= arrayOfLong2[paramInt])
/* 3494 */         return paramLong * l;
/*      */     }
/* 3496 */     return -9223372036854775808L;
/*      */   }
/*      */ 
/*      */   private BigInteger bigMultiplyPowerTen(int paramInt)
/*      */   {
/* 3504 */     if (paramInt <= 0) {
/* 3505 */       return inflate();
/*      */     }
/* 3507 */     if (this.intCompact != -9223372036854775808L) {
/* 3508 */       return bigTenToThe(paramInt).multiply(this.intCompact);
/*      */     }
/* 3510 */     return this.intVal.multiply(bigTenToThe(paramInt));
/*      */   }
/*      */ 
/*      */   private BigInteger inflate()
/*      */   {
/* 3518 */     if (this.intVal == null)
/* 3519 */       this.intVal = BigInteger.valueOf(this.intCompact);
/* 3520 */     return this.intVal;
/*      */   }
/*      */ 
/*      */   private static void matchScale(BigDecimal[] paramArrayOfBigDecimal)
/*      */   {
/* 3537 */     if (paramArrayOfBigDecimal[0].scale == paramArrayOfBigDecimal[1].scale)
/* 3538 */       return;
/* 3539 */     if (paramArrayOfBigDecimal[0].scale < paramArrayOfBigDecimal[1].scale)
/* 3540 */       paramArrayOfBigDecimal[0] = paramArrayOfBigDecimal[0].setScale(paramArrayOfBigDecimal[1].scale, 7);
/* 3541 */     else if (paramArrayOfBigDecimal[1].scale < paramArrayOfBigDecimal[0].scale)
/* 3542 */       paramArrayOfBigDecimal[1] = paramArrayOfBigDecimal[1].setScale(paramArrayOfBigDecimal[0].scale, 7);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 3555 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 3557 */     if (this.intVal == null) {
/* 3558 */       String str = "BigDecimal: null intVal in stream";
/* 3559 */       throw new StreamCorruptedException(str);
/*      */     }
/*      */ 
/* 3562 */     this.intCompact = compactValFor(this.intVal);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 3573 */     inflate();
/*      */ 
/* 3576 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private static int longDigitLength(long paramLong)
/*      */   {
/* 3601 */     assert (paramLong != -9223372036854775808L);
/* 3602 */     if (paramLong < 0L)
/* 3603 */       paramLong = -paramLong;
/* 3604 */     if (paramLong < 10L)
/* 3605 */       return 1;
/* 3606 */     int i = 64;
/* 3607 */     int j = (int)(paramLong >>> 32);
/* 3608 */     if (j == 0) { i -= 32; j = (int)paramLong; }
/* 3609 */     if (j >>> 16 == 0) { i -= 16; j <<= 16; }
/* 3610 */     if (j >>> 24 == 0) { i -= 8; j <<= 8; }
/* 3611 */     if (j >>> 28 == 0) { i -= 4; j <<= 4; }
/* 3612 */     if (j >>> 30 == 0) { i -= 2; j <<= 2; }
/* 3613 */     int k = ((j >>> 31) + i) * 1233 >>> 12;
/* 3614 */     long[] arrayOfLong = LONG_TEN_POWERS_TABLE;
/*      */ 
/* 3616 */     return (k >= arrayOfLong.length) || (paramLong < arrayOfLong[k]) ? k : k + 1;
/*      */   }
/*      */ 
/*      */   private static int bigDigitLength(BigInteger paramBigInteger)
/*      */   {
/* 3632 */     if (paramBigInteger.signum == 0)
/* 3633 */       return 1;
/* 3634 */     int i = (int)((paramBigInteger.bitLength() + 1L) * 646456993L >>> 31);
/* 3635 */     return paramBigInteger.compareMagnitude(bigTenToThe(i)) < 0 ? i : i + 1;
/*      */   }
/*      */ 
/*      */   private BigDecimal stripZerosToMatchScale(long paramLong)
/*      */   {
/* 3654 */     inflate();
/*      */ 
/* 3656 */     while ((this.intVal.compareMagnitude(BigInteger.TEN) >= 0) && (this.scale > paramLong))
/*      */     {
/* 3658 */       if (this.intVal.testBit(0))
/*      */         break;
/* 3660 */       BigInteger[] arrayOfBigInteger = this.intVal.divideAndRemainder(BigInteger.TEN);
/* 3661 */       if (arrayOfBigInteger[1].signum() != 0)
/*      */         break;
/* 3663 */       this.intVal = arrayOfBigInteger[0];
/* 3664 */       this.scale = checkScale(this.scale - 1L);
/* 3665 */       if (this.precision > 0)
/* 3666 */         this.precision -= 1;
/*      */     }
/* 3668 */     if (this.intVal != null)
/* 3669 */       this.intCompact = compactValFor(this.intVal);
/* 3670 */     return this;
/*      */   }
/*      */ 
/*      */   private int checkScale(long paramLong)
/*      */   {
/* 3685 */     int i = (int)paramLong;
/* 3686 */     if (i != paramLong) {
/* 3687 */       i = paramLong > 2147483647L ? 2147483647 : -2147483648;
/*      */       BigInteger localBigInteger;
/* 3689 */       if ((this.intCompact != 0L) && (((localBigInteger = this.intVal) == null) || (localBigInteger.signum() != 0)))
/*      */       {
/* 3691 */         throw new ArithmeticException(i > 0 ? "Underflow" : "Overflow");
/*      */       }
/*      */     }
/* 3693 */     return i;
/*      */   }
/*      */ 
/*      */   private BigDecimal roundOp(MathContext paramMathContext)
/*      */   {
/* 3706 */     BigDecimal localBigDecimal = doRound(this, paramMathContext);
/* 3707 */     return localBigDecimal;
/*      */   }
/*      */ 
/*      */   private void roundThis(MathContext paramMathContext)
/*      */   {
/* 3722 */     BigDecimal localBigDecimal = doRound(this, paramMathContext);
/* 3723 */     if (localBigDecimal == this)
/* 3724 */       return;
/* 3725 */     this.intVal = localBigDecimal.intVal;
/* 3726 */     this.intCompact = localBigDecimal.intCompact;
/* 3727 */     this.scale = localBigDecimal.scale;
/* 3728 */     this.precision = localBigDecimal.precision;
/*      */   }
/*      */ 
/*      */   private static BigDecimal doRound(BigDecimal paramBigDecimal, MathContext paramMathContext)
/*      */   {
/* 3745 */     int i = paramMathContext.precision;
/*      */     int j;
/* 3748 */     while ((j = paramBigDecimal.precision() - i) > 0) {
/* 3749 */       int k = paramBigDecimal.checkScale(paramBigDecimal.scale - j);
/* 3750 */       int m = paramMathContext.roundingMode.oldMode;
/* 3751 */       if (j < LONG_TEN_POWERS_TABLE.length) {
/* 3752 */         paramBigDecimal = divideAndRound(paramBigDecimal.intCompact, paramBigDecimal.intVal, LONG_TEN_POWERS_TABLE[j], null, k, m, k);
/*      */       }
/*      */       else
/*      */       {
/* 3756 */         paramBigDecimal = divideAndRound(paramBigDecimal.intCompact, paramBigDecimal.intVal, -9223372036854775808L, bigTenToThe(j), k, m, k);
/*      */       }
/*      */     }
/*      */ 
/* 3760 */     return paramBigDecimal;
/*      */   }
/*      */ 
/*      */   private static long compactValFor(BigInteger paramBigInteger)
/*      */   {
/* 3769 */     int[] arrayOfInt = paramBigInteger.mag;
/* 3770 */     int i = arrayOfInt.length;
/* 3771 */     if (i == 0)
/* 3772 */       return 0L;
/* 3773 */     int j = arrayOfInt[0];
/* 3774 */     if ((i > 2) || ((i == 2) && (j < 0))) {
/* 3775 */       return -9223372036854775808L;
/*      */     }
/* 3777 */     long l = i == 2 ? (arrayOfInt[1] & 0xFFFFFFFF) + (j << 32) : j & 0xFFFFFFFF;
/*      */ 
/* 3780 */     return paramBigInteger.signum < 0 ? -l : l;
/*      */   }
/*      */ 
/*      */   private static int longCompareMagnitude(long paramLong1, long paramLong2) {
/* 3784 */     if (paramLong1 < 0L)
/* 3785 */       paramLong1 = -paramLong1;
/* 3786 */     if (paramLong2 < 0L)
/* 3787 */       paramLong2 = -paramLong2;
/* 3788 */     return paramLong1 == paramLong2 ? 0 : paramLong1 < paramLong2 ? -1 : 1;
/*      */   }
/*      */ 
/*      */   private static int saturateLong(long paramLong) {
/* 3792 */     int i = (int)paramLong;
/* 3793 */     return paramLong < 0L ? -2147483648 : paramLong == i ? i : 2147483647;
/*      */   }
/*      */ 
/*      */   private static void print(String paramString, BigDecimal paramBigDecimal)
/*      */   {
/* 3800 */     System.err.format("%s:\tintCompact %d\tintVal %d\tscale %d\tprecision %d%n", new Object[] { paramString, Long.valueOf(paramBigDecimal.intCompact), paramBigDecimal.intVal, Integer.valueOf(paramBigDecimal.scale), Integer.valueOf(paramBigDecimal.precision) });
/*      */   }
/*      */ 
/*      */   private BigDecimal audit()
/*      */   {
/* 3828 */     if (this.intCompact == -9223372036854775808L) {
/* 3829 */       if (this.intVal == null) {
/* 3830 */         print("audit", this);
/* 3831 */         throw new AssertionError("null intVal");
/*      */       }
/*      */ 
/* 3834 */       if ((this.precision > 0) && (this.precision != bigDigitLength(this.intVal))) {
/* 3835 */         print("audit", this);
/* 3836 */         throw new AssertionError("precision mismatch");
/*      */       }
/*      */     } else {
/* 3839 */       if (this.intVal != null) {
/* 3840 */         long l = this.intVal.longValue();
/* 3841 */         if (l != this.intCompact) {
/* 3842 */           print("audit", this);
/* 3843 */           throw new AssertionError("Inconsistent state, intCompact=" + this.intCompact + "\t intVal=" + l);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3848 */       if ((this.precision > 0) && (this.precision != longDigitLength(this.intCompact))) {
/* 3849 */         print("audit", this);
/* 3850 */         throw new AssertionError("precision mismatch");
/*      */       }
/*      */     }
/* 3853 */     return this;
/*      */   }
/*      */ 
/*      */   private static class LongOverflow
/*      */   {
/* 2998 */     private static final BigInteger LONGMIN = BigInteger.valueOf(-9223372036854775808L);
/*      */ 
/* 3001 */     private static final BigInteger LONGMAX = BigInteger.valueOf(9223372036854775807L);
/*      */ 
/*      */     public static void check(BigDecimal paramBigDecimal) {
/* 3004 */       paramBigDecimal.inflate();
/* 3005 */       if ((paramBigDecimal.intVal.compareTo(LONGMIN) < 0) || (paramBigDecimal.intVal.compareTo(LONGMAX) > 0))
/*      */       {
/* 3007 */         throw new ArithmeticException("Overflow");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StringBuilderHelper
/*      */   {
/*      */     final StringBuilder sb;
/*      */     final char[] cmpCharArray;
/* 3230 */     static final char[] DIGIT_TENS = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9' };
/*      */ 
/* 3243 */     static final char[] DIGIT_ONES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*      */ 
/*      */     StringBuilderHelper()
/*      */     {
/* 3170 */       this.sb = new StringBuilder();
/*      */ 
/* 3172 */       this.cmpCharArray = new char[19];
/*      */     }
/*      */ 
/*      */     StringBuilder getStringBuilder()
/*      */     {
/* 3177 */       this.sb.setLength(0);
/* 3178 */       return this.sb;
/*      */     }
/*      */ 
/*      */     char[] getCompactCharArray() {
/* 3182 */       return this.cmpCharArray;
/*      */     }
/*      */ 
/*      */     int putIntCompact(long paramLong)
/*      */     {
/* 3195 */       assert (paramLong >= 0L);
/*      */ 
/* 3201 */       int j = this.cmpCharArray.length;
/*      */       int i;
/* 3204 */       while (paramLong > 2147483647L) {
/* 3205 */         long l = paramLong / 100L;
/* 3206 */         i = (int)(paramLong - l * 100L);
/* 3207 */         paramLong = l;
/* 3208 */         this.cmpCharArray[(--j)] = DIGIT_ONES[i];
/* 3209 */         this.cmpCharArray[(--j)] = DIGIT_TENS[i];
/*      */       }
/*      */ 
/* 3214 */       int m = (int)paramLong;
/* 3215 */       while (m >= 100) {
/* 3216 */         int k = m / 100;
/* 3217 */         i = m - k * 100;
/* 3218 */         m = k;
/* 3219 */         this.cmpCharArray[(--j)] = DIGIT_ONES[i];
/* 3220 */         this.cmpCharArray[(--j)] = DIGIT_TENS[i];
/*      */       }
/*      */ 
/* 3223 */       this.cmpCharArray[(--j)] = DIGIT_ONES[m];
/* 3224 */       if (m >= 10) {
/* 3225 */         this.cmpCharArray[(--j)] = DIGIT_TENS[m];
/*      */       }
/* 3227 */       return j;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.math.BigDecimal
 * JD-Core Version:    0.6.2
 */