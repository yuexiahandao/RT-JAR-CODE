/*      */ package java.lang;
/*      */ 
/*      */ import sun.misc.VM;
/*      */ 
/*      */ public final class Integer extends Number
/*      */   implements Comparable<Integer>
/*      */ {
/*      */   public static final int MIN_VALUE = -2147483648;
/*      */   public static final int MAX_VALUE = 2147483647;
/*   71 */   public static final Class<Integer> TYPE = Class.getPrimitiveClass("int");
/*      */ 
/*   76 */   static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
/*      */ 
/*  272 */   static final char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9' };
/*      */ 
/*  285 */   static final char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*      */ 
/*  379 */   static final int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 2147483647 };
/*      */   private final int value;
/*      */   public static final int SIZE = 32;
/*      */   private static final long serialVersionUID = 1360826667806852920L;
/*      */ 
/*      */   public static String toString(int paramInt1, int paramInt2)
/*      */   {
/*  131 */     if ((paramInt2 < 2) || (paramInt2 > 36)) {
/*  132 */       paramInt2 = 10;
/*      */     }
/*      */ 
/*  135 */     if (paramInt2 == 10) {
/*  136 */       return toString(paramInt1);
/*      */     }
/*      */ 
/*  139 */     char[] arrayOfChar = new char[33];
/*  140 */     int i = paramInt1 < 0 ? 1 : 0;
/*  141 */     int j = 32;
/*      */ 
/*  143 */     if (i == 0) {
/*  144 */       paramInt1 = -paramInt1;
/*      */     }
/*      */ 
/*  147 */     while (paramInt1 <= -paramInt2) {
/*  148 */       arrayOfChar[(j--)] = digits[(-(paramInt1 % paramInt2))];
/*  149 */       paramInt1 /= paramInt2;
/*      */     }
/*  151 */     arrayOfChar[j] = digits[(-paramInt1)];
/*      */ 
/*  153 */     if (i != 0) {
/*  154 */       arrayOfChar[(--j)] = '-';
/*      */     }
/*      */ 
/*  157 */     return new String(arrayOfChar, j, 33 - j);
/*      */   }
/*      */ 
/*      */   public static String toHexString(int paramInt)
/*      */   {
/*  195 */     return toUnsignedString(paramInt, 4);
/*      */   }
/*      */ 
/*      */   public static String toOctalString(int paramInt)
/*      */   {
/*  227 */     return toUnsignedString(paramInt, 3);
/*      */   }
/*      */ 
/*      */   public static String toBinaryString(int paramInt)
/*      */   {
/*  252 */     return toUnsignedString(paramInt, 1);
/*      */   }
/*      */ 
/*      */   private static String toUnsignedString(int paramInt1, int paramInt2)
/*      */   {
/*  259 */     char[] arrayOfChar = new char[32];
/*  260 */     int i = 32;
/*  261 */     int j = 1 << paramInt2;
/*  262 */     int k = j - 1;
/*      */     do {
/*  264 */       arrayOfChar[(--i)] = digits[(paramInt1 & k)];
/*  265 */       paramInt1 >>>= paramInt2;
/*  266 */     }while (paramInt1 != 0);
/*      */ 
/*  268 */     return new String(arrayOfChar, i, 32 - i);
/*      */   }
/*      */ 
/*      */   public static String toString(int paramInt)
/*      */   {
/*  328 */     if (paramInt == -2147483648)
/*  329 */       return "-2147483648";
/*  330 */     int i = paramInt < 0 ? stringSize(-paramInt) + 1 : stringSize(paramInt);
/*  331 */     char[] arrayOfChar = new char[i];
/*  332 */     getChars(paramInt, i, arrayOfChar);
/*  333 */     return new String(arrayOfChar, true);
/*      */   }
/*      */ 
/*      */   static void getChars(int paramInt1, int paramInt2, char[] paramArrayOfChar)
/*      */   {
/*  347 */     int k = paramInt2;
/*  348 */     int m = 0;
/*      */ 
/*  350 */     if (paramInt1 < 0) {
/*  351 */       m = 45;
/*  352 */       paramInt1 = -paramInt1;
/*      */     }
/*      */     int i;
/*      */     int j;
/*  356 */     while (paramInt1 >= 65536) {
/*  357 */       i = paramInt1 / 100;
/*      */ 
/*  359 */       j = paramInt1 - ((i << 6) + (i << 5) + (i << 2));
/*  360 */       paramInt1 = i;
/*  361 */       paramArrayOfChar[(--k)] = DigitOnes[j];
/*  362 */       paramArrayOfChar[(--k)] = DigitTens[j];
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  368 */       i = paramInt1 * 52429 >>> 19;
/*  369 */       j = paramInt1 - ((i << 3) + (i << 1));
/*  370 */       paramArrayOfChar[(--k)] = digits[j];
/*  371 */       paramInt1 = i;
/*  372 */       if (paramInt1 == 0) break;
/*      */     }
/*  374 */     if (m != 0)
/*  375 */       paramArrayOfChar[(--k)] = m;
/*      */   }
/*      */ 
/*      */   static int stringSize(int paramInt)
/*      */   {
/*  384 */     for (int i = 0; ; i++)
/*  385 */       if (paramInt <= sizeTable[i])
/*  386 */         return i + 1;
/*      */   }
/*      */ 
/*      */   public static int parseInt(String paramString, int paramInt)
/*      */     throws NumberFormatException
/*      */   {
/*  453 */     if (paramString == null) {
/*  454 */       throw new NumberFormatException("null");
/*      */     }
/*      */ 
/*  457 */     if (paramInt < 2) {
/*  458 */       throw new NumberFormatException("radix " + paramInt + " less than Character.MIN_RADIX");
/*      */     }
/*      */ 
/*  462 */     if (paramInt > 36) {
/*  463 */       throw new NumberFormatException("radix " + paramInt + " greater than Character.MAX_RADIX");
/*      */     }
/*      */ 
/*  467 */     int i = 0;
/*  468 */     int j = 0;
/*  469 */     int k = 0; int m = paramString.length();
/*  470 */     int n = -2147483647;
/*      */ 
/*  474 */     if (m > 0) {
/*  475 */       int i3 = paramString.charAt(0);
/*  476 */       if (i3 < 48) {
/*  477 */         if (i3 == 45) {
/*  478 */           j = 1;
/*  479 */           n = -2147483648;
/*  480 */         } else if (i3 != 43) {
/*  481 */           throw NumberFormatException.forInputString(paramString);
/*      */         }
/*  483 */         if (m == 1)
/*  484 */           throw NumberFormatException.forInputString(paramString);
/*  485 */         k++;
/*      */       }
/*  487 */       int i1 = n / paramInt;
/*  488 */       while (k < m)
/*      */       {
/*  490 */         int i2 = Character.digit(paramString.charAt(k++), paramInt);
/*  491 */         if (i2 < 0) {
/*  492 */           throw NumberFormatException.forInputString(paramString);
/*      */         }
/*  494 */         if (i < i1) {
/*  495 */           throw NumberFormatException.forInputString(paramString);
/*      */         }
/*  497 */         i *= paramInt;
/*  498 */         if (i < n + i2) {
/*  499 */           throw NumberFormatException.forInputString(paramString);
/*      */         }
/*  501 */         i -= i2;
/*      */       }
/*      */     } else {
/*  504 */       throw NumberFormatException.forInputString(paramString);
/*      */     }
/*  506 */     return j != 0 ? i : -i;
/*      */   }
/*      */ 
/*      */   public static int parseInt(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/*  527 */     return parseInt(paramString, 10);
/*      */   }
/*      */ 
/*      */   public static Integer valueOf(String paramString, int paramInt)
/*      */     throws NumberFormatException
/*      */   {
/*  556 */     return valueOf(parseInt(paramString, paramInt));
/*      */   }
/*      */ 
/*      */   public static Integer valueOf(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/*  582 */     return valueOf(parseInt(paramString, 10));
/*      */   }
/*      */ 
/*      */   public static Integer valueOf(int paramInt)
/*      */   {
/*  639 */     assert (IntegerCache.high >= 127);
/*  640 */     if ((paramInt >= -128) && (paramInt <= IntegerCache.high))
/*  641 */       return IntegerCache.cache[(paramInt + 128)];
/*  642 */     return new Integer(paramInt);
/*      */   }
/*      */ 
/*      */   public Integer(int paramInt)
/*      */   {
/*  660 */     this.value = paramInt;
/*      */   }
/*      */ 
/*      */   public Integer(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/*  677 */     this.value = parseInt(paramString, 10);
/*      */   }
/*      */ 
/*      */   public byte byteValue()
/*      */   {
/*  685 */     return (byte)this.value;
/*      */   }
/*      */ 
/*      */   public short shortValue()
/*      */   {
/*  693 */     return (short)this.value;
/*      */   }
/*      */ 
/*      */   public int intValue()
/*      */   {
/*  701 */     return this.value;
/*      */   }
/*      */ 
/*      */   public long longValue()
/*      */   {
/*  709 */     return this.value;
/*      */   }
/*      */ 
/*      */   public float floatValue()
/*      */   {
/*  717 */     return this.value;
/*      */   }
/*      */ 
/*      */   public double doubleValue()
/*      */   {
/*  725 */     return this.value;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  739 */     return toString(this.value);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  750 */     return this.value;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  764 */     if ((paramObject instanceof Integer)) {
/*  765 */       return this.value == ((Integer)paramObject).intValue();
/*      */     }
/*  767 */     return false;
/*      */   }
/*      */ 
/*      */   public static Integer getInteger(String paramString)
/*      */   {
/*  799 */     return getInteger(paramString, null);
/*      */   }
/*      */ 
/*      */   public static Integer getInteger(String paramString, int paramInt)
/*      */   {
/*  844 */     Integer localInteger = getInteger(paramString, null);
/*  845 */     return localInteger == null ? valueOf(paramInt) : localInteger;
/*      */   }
/*      */ 
/*      */   public static Integer getInteger(String paramString, Integer paramInteger)
/*      */   {
/*  885 */     String str = null;
/*      */     try {
/*  887 */       str = System.getProperty(paramString);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     } catch (NullPointerException localNullPointerException) {
/*      */     }
/*  891 */     if (str != null)
/*      */       try {
/*  893 */         return decode(str);
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException) {
/*      */       }
/*  897 */     return paramInteger;
/*      */   }
/*      */ 
/*      */   public static Integer decode(String paramString)
/*      */     throws NumberFormatException
/*      */   {
/*  943 */     int i = 10;
/*  944 */     int j = 0;
/*  945 */     int k = 0;
/*      */ 
/*  948 */     if (paramString.length() == 0)
/*  949 */       throw new NumberFormatException("Zero length string");
/*  950 */     int m = paramString.charAt(0);
/*      */ 
/*  952 */     if (m == 45) {
/*  953 */       k = 1;
/*  954 */       j++;
/*  955 */     } else if (m == 43) {
/*  956 */       j++;
/*      */     }
/*      */ 
/*  959 */     if ((paramString.startsWith("0x", j)) || (paramString.startsWith("0X", j))) {
/*  960 */       j += 2;
/*  961 */       i = 16;
/*      */     }
/*  963 */     else if (paramString.startsWith("#", j)) {
/*  964 */       j++;
/*  965 */       i = 16;
/*      */     }
/*  967 */     else if ((paramString.startsWith("0", j)) && (paramString.length() > 1 + j)) {
/*  968 */       j++;
/*  969 */       i = 8;
/*      */     }
/*      */ 
/*  972 */     if ((paramString.startsWith("-", j)) || (paramString.startsWith("+", j)))
/*  973 */       throw new NumberFormatException("Sign character in wrong position");
/*      */     Integer localInteger;
/*      */     try {
/*  976 */       localInteger = valueOf(paramString.substring(j), i);
/*  977 */       localInteger = k != 0 ? valueOf(-localInteger.intValue()) : localInteger;
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*  982 */       String str = k != 0 ? "-" + paramString.substring(j) : paramString.substring(j);
/*      */ 
/*  984 */       localInteger = valueOf(str, i);
/*      */     }
/*  986 */     return localInteger;
/*      */   }
/*      */ 
/*      */   public int compareTo(Integer paramInteger)
/*      */   {
/* 1003 */     return compare(this.value, paramInteger.value);
/*      */   }
/*      */ 
/*      */   public static int compare(int paramInt1, int paramInt2)
/*      */   {
/* 1021 */     return paramInt1 == paramInt2 ? 0 : paramInt1 < paramInt2 ? -1 : 1;
/*      */   }
/*      */ 
/*      */   public static int highestOneBit(int paramInt)
/*      */   {
/* 1049 */     paramInt |= paramInt >> 1;
/* 1050 */     paramInt |= paramInt >> 2;
/* 1051 */     paramInt |= paramInt >> 4;
/* 1052 */     paramInt |= paramInt >> 8;
/* 1053 */     paramInt |= paramInt >> 16;
/* 1054 */     return paramInt - (paramInt >>> 1);
/*      */   }
/*      */ 
/*      */   public static int lowestOneBit(int paramInt)
/*      */   {
/* 1071 */     return paramInt & -paramInt;
/*      */   }
/*      */ 
/*      */   public static int numberOfLeadingZeros(int paramInt)
/*      */   {
/* 1096 */     if (paramInt == 0)
/* 1097 */       return 32;
/* 1098 */     int i = 1;
/* 1099 */     if (paramInt >>> 16 == 0) { i += 16; paramInt <<= 16; }
/* 1100 */     if (paramInt >>> 24 == 0) { i += 8; paramInt <<= 8; }
/* 1101 */     if (paramInt >>> 28 == 0) { i += 4; paramInt <<= 4; }
/* 1102 */     if (paramInt >>> 30 == 0) { i += 2; paramInt <<= 2; }
/* 1103 */     i -= (paramInt >>> 31);
/* 1104 */     return i;
/*      */   }
/*      */ 
/*      */   public static int numberOfTrailingZeros(int paramInt)
/*      */   {
/* 1123 */     if (paramInt == 0) return 32;
/* 1124 */     int j = 31;
/* 1125 */     int i = paramInt << 16; if (i != 0) { j -= 16; paramInt = i; }
/* 1126 */     i = paramInt << 8; if (i != 0) { j -= 8; paramInt = i; }
/* 1127 */     i = paramInt << 4; if (i != 0) { j -= 4; paramInt = i; }
/* 1128 */     i = paramInt << 2; if (i != 0) { j -= 2; paramInt = i; }
/* 1129 */     return j - (paramInt << 1 >>> 31);
/*      */   }
/*      */ 
/*      */   public static int bitCount(int paramInt)
/*      */   {
/* 1143 */     paramInt -= (paramInt >>> 1 & 0x55555555);
/* 1144 */     paramInt = (paramInt & 0x33333333) + (paramInt >>> 2 & 0x33333333);
/* 1145 */     paramInt = paramInt + (paramInt >>> 4) & 0xF0F0F0F;
/* 1146 */     paramInt += (paramInt >>> 8);
/* 1147 */     paramInt += (paramInt >>> 16);
/* 1148 */     return paramInt & 0x3F;
/*      */   }
/*      */ 
/*      */   public static int rotateLeft(int paramInt1, int paramInt2)
/*      */   {
/* 1170 */     return paramInt1 << paramInt2 | paramInt1 >>> -paramInt2;
/*      */   }
/*      */ 
/*      */   public static int rotateRight(int paramInt1, int paramInt2)
/*      */   {
/* 1192 */     return paramInt1 >>> paramInt2 | paramInt1 << -paramInt2;
/*      */   }
/*      */ 
/*      */   public static int reverse(int paramInt)
/*      */   {
/* 1206 */     paramInt = (paramInt & 0x55555555) << 1 | paramInt >>> 1 & 0x55555555;
/* 1207 */     paramInt = (paramInt & 0x33333333) << 2 | paramInt >>> 2 & 0x33333333;
/* 1208 */     paramInt = (paramInt & 0xF0F0F0F) << 4 | paramInt >>> 4 & 0xF0F0F0F;
/* 1209 */     paramInt = paramInt << 24 | (paramInt & 0xFF00) << 8 | paramInt >>> 8 & 0xFF00 | paramInt >>> 24;
/*      */ 
/* 1211 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static int signum(int paramInt)
/*      */   {
/* 1224 */     return paramInt >> 31 | -paramInt >>> 31;
/*      */   }
/*      */ 
/*      */   public static int reverseBytes(int paramInt)
/*      */   {
/* 1236 */     return paramInt >>> 24 | paramInt >> 8 & 0xFF00 | paramInt << 8 & 0xFF0000 | paramInt << 24;
/*      */   }
/*      */ 
/*      */   private static class IntegerCache
/*      */   {
/*      */     static final int low = -128;
/*      */     static final int high;
/*      */     static final Integer[] cache;
/*      */ 
/*      */     static
/*      */     {
/*  603 */       int i = 127;
/*  604 */       String str = VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
/*      */ 
/*  606 */       if (str != null) {
/*  607 */         j = Integer.parseInt(str);
/*  608 */         j = Math.max(j, 127);
/*      */ 
/*  610 */         i = Math.min(j, 2147483518);
/*      */       }
/*  612 */       high = i;
/*      */ 
/*  614 */       cache = new Integer[high - -128 + 1];
/*  615 */       int j = -128;
/*  616 */       for (int k = 0; k < cache.length; k++)
/*  617 */         cache[k] = new Integer(j++);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Integer
 * JD-Core Version:    0.6.2
 */