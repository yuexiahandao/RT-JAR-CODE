/*      */
package java.lang;

/*      */
/*      */ public final class Long extends Number
/*      */ implements Comparable<Long>
/*      */ {
    /*      */   public static final long MIN_VALUE = -9223372036854775808L;
    /*      */   public static final long MAX_VALUE = 9223372036854775807L;
    /*   69 */   public static final Class<Long> TYPE = Class.getPrimitiveClass("long");
    /*      */   private final long value;
    /*      */   public static final int SIZE = 64;
    /*      */   private static final long serialVersionUID = 4290774380558885855L;

    /*      */
/*      */
    public static String toString(long paramLong, int paramInt)
/*      */ {
/*  116 */
        if ((paramInt < 2) || (paramInt > 36))
/*  117 */ paramInt = 10;
/*  118 */
        if (paramInt == 10)
/*  119 */ return toString(paramLong);
/*  120 */
        char[] arrayOfChar = new char[65];
/*  121 */
        int i = 64;
/*  122 */
        int j = paramLong < 0L ? 1 : 0;
/*      */ 
/*  124 */
        if (j == 0) {
/*  125 */
            paramLong = -paramLong;
/*      */
        }
/*      */ 
/*  128 */
        while (paramLong <= -paramInt) {
/*  129 */
            arrayOfChar[(i--)] = Integer.digits[((int) -(paramLong % paramInt))];
/*  130 */
            paramLong /= paramInt;
/*      */
        }
/*  132 */
        arrayOfChar[i] = Integer.digits[((int) -paramLong)];
/*      */ 
/*  134 */
        if (j != 0) {
/*  135 */
            arrayOfChar[(--i)] = '-';
/*      */
        }
/*      */ 
/*  138 */
        return new String(arrayOfChar, i, 65 - i);
/*      */
    }

    /*      */
/*      */
    public static String toHexString(long paramLong)
/*      */ {
/*  177 */
        return toUnsignedString(paramLong, 4);
/*      */
    }

    /*      */
/*      */
    public static String toOctalString(long paramLong)
/*      */ {
/*  210 */
        return toUnsignedString(paramLong, 3);
/*      */
    }

    /*      */
/*      */
    public static String toBinaryString(long paramLong)
/*      */ {
/*  235 */
        return toUnsignedString(paramLong, 1);
/*      */
    }

    /*      */
/*      */
    private static String toUnsignedString(long paramLong, int paramInt)
/*      */ {
/*  242 */
        char[] arrayOfChar = new char[64];
/*  243 */
        int i = 64;
/*  244 */
        int j = 1 << paramInt;
/*  245 */
        long l = j - 1;
/*      */
        do {
/*  247 */
            arrayOfChar[(--i)] = Integer.digits[((int) (paramLong & l))];
/*  248 */
            paramLong >>>= paramInt;
/*  249 */
        } while (paramLong != 0L);
/*  250 */
        return new String(arrayOfChar, i, 64 - i);
/*      */
    }

    /*      */
/*      */
    public static String toString(long paramLong)
/*      */ {
/*  264 */
        if (paramLong == -9223372036854775808L)
/*  265 */ return "-9223372036854775808";
/*  266 */
        int i = paramLong < 0L ? stringSize(-paramLong) + 1 : stringSize(paramLong);
/*  267 */
        char[] arrayOfChar = new char[i];
/*  268 */
        getChars(paramLong, i, arrayOfChar);
/*  269 */
        return new String(arrayOfChar, true);
/*      */
    }

    /*      */
/*      */
    static void getChars(long paramLong, int paramInt, char[] paramArrayOfChar)
/*      */ {
/*  284 */
        int j = paramInt;
/*  285 */
        int k = 0;
/*      */ 
/*  287 */
        if (paramLong < 0L) {
/*  288 */
            k = 45;
/*  289 */
            paramLong = -paramLong;
/*      */
        }
/*      */
        int i;
/*  293 */
        while (paramLong > 2147483647L) {
/*  294 */
            long l = paramLong / 100L;
/*      */ 
/*  296 */
            i = (int) (paramLong - ((l << 6) + (l << 5) + (l << 2)));
/*  297 */
            paramLong = l;
/*  298 */
            paramArrayOfChar[(--j)] = Integer.DigitOnes[i];
/*  299 */
            paramArrayOfChar[(--j)] = Integer.DigitTens[i];
/*      */
        }
/*      */ 
/*  304 */
        int n = (int) paramLong;
/*      */
        int m;
/*  305 */
        while (n >= 65536) {
/*  306 */
            m = n / 100;
/*      */ 
/*  308 */
            i = n - ((m << 6) + (m << 5) + (m << 2));
/*  309 */
            n = m;
/*  310 */
            paramArrayOfChar[(--j)] = Integer.DigitOnes[i];
/*  311 */
            paramArrayOfChar[(--j)] = Integer.DigitTens[i];
/*      */
        }
/*      */ 
/*      */
        while (true)
/*      */ {
/*  317 */
            m = n * 52429 >>> 19;
/*  318 */
            i = n - ((m << 3) + (m << 1));
/*  319 */
            paramArrayOfChar[(--j)] = Integer.digits[i];
/*  320 */
            n = m;
/*  321 */
            if (n == 0) break;
/*      */
        }
/*  323 */
        if (k != 0)
/*  324 */ paramArrayOfChar[(--j)] = k;
/*      */
    }

    /*      */
/*      */
    static int stringSize(long paramLong)
/*      */ {
/*  330 */
        long l = 10L;
/*  331 */
        for (int i = 1; i < 19; i++) {
/*  332 */
            if (paramLong < l)
/*  333 */ return i;
/*  334 */
            l = 10L * l;
/*      */
        }
/*  336 */
        return 19;
/*      */
    }

    /*      */
/*      */
    public static long parseLong(String paramString, int paramInt)
/*      */     throws NumberFormatException
/*      */ {
/*  403 */
        if (paramString == null) {
/*  404 */
            throw new NumberFormatException("null");
/*      */
        }
/*      */ 
/*  407 */
        if (paramInt < 2) {
/*  408 */
            throw new NumberFormatException("radix " + paramInt + " less than Character.MIN_RADIX");
/*      */
        }
/*      */ 
/*  411 */
        if (paramInt > 36) {
/*  412 */
            throw new NumberFormatException("radix " + paramInt + " greater than Character.MAX_RADIX");
/*      */
        }
/*      */ 
/*  416 */
        long l1 = 0L;
/*  417 */
        int i = 0;
/*  418 */
        int j = 0;
        int k = paramString.length();
/*  419 */
        long l2 = -9223372036854775807L;
/*      */ 
/*  423 */
        if (k > 0) {
/*  424 */
            int n = paramString.charAt(0);
/*  425 */
            if (n < 48) {
/*  426 */
                if (n == 45) {
/*  427 */
                    i = 1;
/*  428 */
                    l2 = -9223372036854775808L;
/*  429 */
                } else if (n != 43) {
/*  430 */
                    throw NumberFormatException.forInputString(paramString);
/*      */
                }
/*  432 */
                if (k == 1)
/*  433 */ throw NumberFormatException.forInputString(paramString);
/*  434 */
                j++;
/*      */
            }
/*  436 */
            long l3 = l2 / paramInt;
/*  437 */
            while (j < k)
/*      */ {
/*  439 */
                int m = Character.digit(paramString.charAt(j++), paramInt);
/*  440 */
                if (m < 0) {
/*  441 */
                    throw NumberFormatException.forInputString(paramString);
/*      */
                }
/*  443 */
                if (l1 < l3) {
/*  444 */
                    throw NumberFormatException.forInputString(paramString);
/*      */
                }
/*  446 */
                l1 *= paramInt;
/*  447 */
                if (l1 < l2 + m) {
/*  448 */
                    throw NumberFormatException.forInputString(paramString);
/*      */
                }
/*  450 */
                l1 -= m;
/*      */
            }
/*      */
        } else {
/*  453 */
            throw NumberFormatException.forInputString(paramString);
/*      */
        }
/*  455 */
        return i != 0 ? l1 : -l1;
/*      */
    }

    /*      */
/*      */
    public static long parseLong(String paramString)
/*      */     throws NumberFormatException
/*      */ {
/*  483 */
        return parseLong(paramString, 10);
/*      */
    }

    /*      */
/*      */
    public static Long valueOf(String paramString, int paramInt)
/*      */     throws NumberFormatException
/*      */ {
/*  513 */
        return valueOf(parseLong(paramString, paramInt));
/*      */
    }

    /*      */
/*      */
    public static Long valueOf(String paramString)
/*      */     throws NumberFormatException
/*      */ {
/*  540 */
        return valueOf(parseLong(paramString, 10));
/*      */
    }

    /*      */
/*      */
    public static Long valueOf(long paramLong)
/*      */ {
/*  574 */
        if ((paramLong >= -128L) && (paramLong <= 127L)) {
/*  575 */
            return LongCache.cache[((int) paramLong + 128)];
/*      */
        }
/*  577 */
        return new Long(paramLong);
/*      */
    }

    /*      */
/*      */
    public static Long decode(String paramString)
/*      */     throws NumberFormatException
/*      */ {
/*  624 */
        int i = 10;
/*  625 */
        int j = 0;
/*  626 */
        int k = 0;
/*      */ 
/*  629 */
        if (paramString.length() == 0)
/*  630 */ throw new NumberFormatException("Zero length string");
/*  631 */
        int m = paramString.charAt(0);
/*      */ 
/*  633 */
        if (m == 45) {
/*  634 */
            k = 1;
/*  635 */
            j++;
/*  636 */
        } else if (m == 43) {
/*  637 */
            j++;
/*      */
        }
/*      */ 
/*  640 */
        if ((paramString.startsWith("0x", j)) || (paramString.startsWith("0X", j))) {
/*  641 */
            j += 2;
/*  642 */
            i = 16;
/*      */
        }
/*  644 */
        else if (paramString.startsWith("#", j)) {
/*  645 */
            j++;
/*  646 */
            i = 16;
/*      */
        }
/*  648 */
        else if ((paramString.startsWith("0", j)) && (paramString.length() > 1 + j)) {
/*  649 */
            j++;
/*  650 */
            i = 8;
/*      */
        }
/*      */ 
/*  653 */
        if ((paramString.startsWith("-", j)) || (paramString.startsWith("+", j)))
/*  654 */ throw new NumberFormatException("Sign character in wrong position");
/*      */
        Long localLong;
/*      */
        try {
/*  657 */
            localLong = valueOf(paramString.substring(j), i);
/*  658 */
            localLong = k != 0 ? valueOf(-localLong.longValue()) : localLong;
/*      */
        }
/*      */ catch (NumberFormatException localNumberFormatException)
/*      */ {
/*  663 */
            String str = k != 0 ? "-" + paramString.substring(j) : paramString.substring(j);
/*      */ 
/*  665 */
            localLong = valueOf(str, i);
/*      */
        }
/*  667 */
        return localLong;
/*      */
    }

    /*      */
/*      */
    public Long(long paramLong)
/*      */ {
/*  685 */
        this.value = paramLong;
/*      */
    }

    /*      */
/*      */
    public Long(String paramString)
/*      */     throws NumberFormatException
/*      */ {
/*  702 */
        this.value = parseLong(paramString, 10);
/*      */
    }

    /*      */
/*      */
    public byte byteValue()
/*      */ {
/*  710 */
        return (byte) (int) this.value;
/*      */
    }

    /*      */
/*      */
    public short shortValue()
/*      */ {
/*  718 */
        return (short) (int) this.value;
/*      */
    }

    /*      */
/*      */
    public int intValue()
/*      */ {
/*  726 */
        return (int) this.value;
/*      */
    }

    /*      */
/*      */
    public long longValue()
/*      */ {
/*  734 */
        return this.value;
/*      */
    }

    /*      */
/*      */
    public float floatValue()
/*      */ {
/*  742 */
        return (float) this.value;
/*      */
    }

    /*      */
/*      */
    public double doubleValue()
/*      */ {
/*  750 */
        return this.value;
/*      */
    }

    /*      */
/*      */
    public String toString()
/*      */ {
/*  764 */
        return toString(this.value);
/*      */
    }

    /*      */
/*      */
    public int hashCode()
/*      */ {
/*  780 */
        return (int) (this.value ^ this.value >>> 32);
/*      */
    }

    /*      */
/*      */
    public boolean equals(Object paramObject)
/*      */ {
/*  794 */
        if ((paramObject instanceof Long)) {
/*  795 */
            return this.value == ((Long) paramObject).longValue();
/*      */
        }
/*  797 */
        return false;
/*      */
    }

    /*      */
/*      */
    public static Long getLong(String paramString)
/*      */ {
/*  831 */
        return getLong(paramString, null);
/*      */
    }

    /*      */
/*      */
    public static Long getLong(String paramString, long paramLong)
/*      */ {
/*  876 */
        Long localLong = getLong(paramString, null);
/*  877 */
        return localLong == null ? valueOf(paramLong) : localLong;
/*      */
    }

    /*      */
/*      */
    public static Long getLong(String paramString, Long paramLong)
/*      */ {
/*  924 */
        String str = null;
/*      */
        try {
/*  926 */
            str = System.getProperty(paramString);
/*      */
        } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */
        } catch (NullPointerException localNullPointerException) {
/*      */
        }
/*  930 */
        if (str != null)
/*      */ try {
/*  932 */
            return decode(str);
/*      */
        }
/*      */ catch (NumberFormatException localNumberFormatException) {
/*      */
        }
/*  936 */
        return paramLong;
/*      */
    }

    /*      */
/*      */
    public int compareTo(Long paramLong)
/*      */ {
/*  953 */
        return compare(this.value, paramLong.value);
/*      */
    }

    /*      */
/*      */
    public static int compare(long paramLong1, long paramLong2)
/*      */ {
/*  971 */
        return paramLong1 == paramLong2 ? 0 : paramLong1 < paramLong2 ? -1 : 1;
/*      */
    }

    /*      */
/*      */
    public static long highestOneBit(long paramLong)
/*      */ {
/*  999 */
        paramLong |= paramLong >> 1;
/* 1000 */
        paramLong |= paramLong >> 2;
/* 1001 */
        paramLong |= paramLong >> 4;
/* 1002 */
        paramLong |= paramLong >> 8;
/* 1003 */
        paramLong |= paramLong >> 16;
/* 1004 */
        paramLong |= paramLong >> 32;
/* 1005 */
        return paramLong - (paramLong >>> 1);
/*      */
    }

    /*      */
/*      */
    public static long lowestOneBit(long paramLong)
/*      */ {
/* 1022 */
        return paramLong & -paramLong;
/*      */
    }

    /*      */
/*      */
    public static int numberOfLeadingZeros(long paramLong)
/*      */ {
/* 1047 */
        if (paramLong == 0L)
/* 1048 */ return 64;
/* 1049 */
        int i = 1;
/* 1050 */
        int j = (int) (paramLong >>> 32);
/* 1051 */
        if (j == 0) {
            i += 32;
            j = (int) paramLong;
        }
/* 1052 */
        if (j >>> 16 == 0) {
            i += 16;
            j <<= 16;
        }
/* 1053 */
        if (j >>> 24 == 0) {
            i += 8;
            j <<= 8;
        }
/* 1054 */
        if (j >>> 28 == 0) {
            i += 4;
            j <<= 4;
        }
/* 1055 */
        if (j >>> 30 == 0) {
            i += 2;
            j <<= 2;
        }
/* 1056 */
        i -= (j >>> 31);
/* 1057 */
        return i;
/*      */
    }

    /*      */
/*      */
    public static int numberOfTrailingZeros(long paramLong)
/*      */ {
/* 1076 */
        if (paramLong == 0L) return 64;
/* 1077 */
        int k = 63;
/* 1078 */
        int j = (int) paramLong;
/*      */
        int i;
/* 1078 */
        if (j != 0) {
            k -= 32;
            i = j;
        } else {
            i = (int) (paramLong >>> 32);
        }
/* 1079 */
        j = i << 16;
        if (j != 0) {
            k -= 16;
            i = j;
        }
/* 1080 */
        j = i << 8;
        if (j != 0) {
            k -= 8;
            i = j;
        }
/* 1081 */
        j = i << 4;
        if (j != 0) {
            k -= 4;
            i = j;
        }
/* 1082 */
        j = i << 2;
        if (j != 0) {
            k -= 2;
            i = j;
        }
/* 1083 */
        return k - (i << 1 >>> 31);
/*      */
    }

    /*      */
/*      */
    public static int bitCount(long paramLong)
/*      */ {
/* 1097 */
        paramLong -= (paramLong >>> 1 & 0x55555555);
/* 1098 */
        paramLong = (paramLong & 0x33333333) + (paramLong >>> 2 & 0x33333333);
/* 1099 */
        paramLong = paramLong + (paramLong >>> 4) & 0xF0F0F0F;
/* 1100 */
        paramLong += (paramLong >>> 8);
/* 1101 */
        paramLong += (paramLong >>> 16);
/* 1102 */
        paramLong += (paramLong >>> 32);
/* 1103 */
        return (int) paramLong & 0x7F;
/*      */
    }

    /*      */
/*      */
    public static long rotateLeft(long paramLong, int paramInt)
/*      */ {
/* 1125 */
        return paramLong << paramInt | paramLong >>> -paramInt;
/*      */
    }

    /*      */
/*      */
    public static long rotateRight(long paramLong, int paramInt)
/*      */ {
/* 1147 */
        return paramLong >>> paramInt | paramLong << -paramInt;
/*      */
    }

    /*      */
/*      */
    public static long reverse(long paramLong)
/*      */ {
/* 1161 */
        paramLong = (paramLong & 0x55555555) << 1 | paramLong >>> 1 & 0x55555555;
/* 1162 */
        paramLong = (paramLong & 0x33333333) << 2 | paramLong >>> 2 & 0x33333333;
/* 1163 */
        paramLong = (paramLong & 0xF0F0F0F) << 4 | paramLong >>> 4 & 0xF0F0F0F;
/* 1164 */
        paramLong = (paramLong & 0xFF00FF) << 8 | paramLong >>> 8 & 0xFF00FF;
/* 1165 */
        paramLong = paramLong << 48 | (paramLong & 0xFFFF0000) << 16 | paramLong >>> 16 & 0xFFFF0000 | paramLong >>> 48;
/*      */ 
/* 1167 */
        return paramLong;
/*      */
    }

    /*      */
/*      */
    public static int signum(long paramLong)
/*      */ {
/* 1180 */
        return (int) (paramLong >> 63 | -paramLong >>> 63);
/*      */
    }

    /*      */
/*      */
    public static long reverseBytes(long paramLong)
/*      */ {
/* 1192 */
        paramLong = (paramLong & 0xFF00FF) << 8 | paramLong >>> 8 & 0xFF00FF;
/* 1193 */
        return paramLong << 48 | (paramLong & 0xFFFF0000) << 16 | paramLong >>> 16 & 0xFFFF0000 | paramLong >>> 48;
/*      */
    }

    /*      */
/*      */   private static class LongCache
/*      */ {
        /*  546 */     static final Long[] cache = new Long[256];

        /*      */
/*      */     static {
/*  549 */
            for (int i = 0; i < cache.length; i++)
/*  550 */
                cache[i] = new Long(i - 128);
/*      */
        }
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Long
 * JD-Core Version:    0.6.2
 */