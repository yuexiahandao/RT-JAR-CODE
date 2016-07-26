/*     */
package java.lang;

/*     */
/*     */ public final class Short extends Number
/*     */ implements Comparable<Short>
/*     */ {
    /*     */   public static final short MIN_VALUE = -32768;
    /*     */   public static final short MAX_VALUE = 32767;
    /*  61 */   public static final Class<Short> TYPE = Class.getPrimitiveClass("short");
    /*     */   private final short value;
    /*     */   public static final int SIZE = 16;
    /*     */   private static final long serialVersionUID = 7515723908773894738L;

    /*     */
/*     */
    public static String toString(short paramShort)
/*     */ {
/*  72 */
        return Integer.toString(paramShort, 10);
/*     */
    }

    /*     */
/*     */
    public static short parseShort(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */ {
/* 117 */
        int i = Integer.parseInt(paramString, paramInt);
/* 118 */
        if ((i < -32768) || (i > 32767)) {
/* 119 */
            throw new NumberFormatException("Value out of range. Value:\"" + paramString + "\" Radix:" + paramInt);
/*     */
        }
/* 121 */
        return (short) i;
/*     */
    }

    /*     */
/*     */
    public static short parseShort(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 143 */
        return parseShort(paramString, 10);
/*     */
    }

    /*     */
/*     */
    public static Short valueOf(String paramString, int paramInt)
/*     */     throws NumberFormatException
/*     */ {
/* 173 */
        return valueOf(parseShort(paramString, paramInt));
/*     */
    }

    /*     */
/*     */
    public static Short valueOf(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 199 */
        return valueOf(paramString, 10);
/*     */
    }

    /*     */
/*     */
    public static Short valueOf(short paramShort)
/*     */ {
/* 231 */
        int i = paramShort;
/* 232 */
        if ((i >= -128) && (i <= 127)) {
/* 233 */
            return ShortCache.cache[(i + 128)];
/*     */
        }
/* 235 */
        return new Short(paramShort);
/*     */
    }

    /*     */
/*     */
    public static Short decode(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 281 */
        int i = Integer.decode(paramString).intValue();
/* 282 */
        if ((i < -32768) || (i > 32767)) {
/* 283 */
            throw new NumberFormatException("Value " + i + " out of range from input " + paramString);
/*     */
        }
/* 285 */
        return valueOf((short) i);
/*     */
    }

    /*     */
/*     */
    public Short(short paramShort)
/*     */ {
/* 303 */
        this.value = paramShort;
/*     */
    }

    /*     */
/*     */
    public Short(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 320 */
        this.value = parseShort(paramString, 10);
/*     */
    }

    /*     */
/*     */
    public byte byteValue()
/*     */ {
/* 328 */
        return (byte) this.value;
/*     */
    }

    /*     */
/*     */
    public short shortValue()
/*     */ {
/* 336 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public int intValue()
/*     */ {
/* 344 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public long longValue()
/*     */ {
/* 352 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public float floatValue()
/*     */ {
/* 360 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public double doubleValue()
/*     */ {
/* 368 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 382 */
        return Integer.toString(this.value);
/*     */
    }

    /*     */
/*     */
    public int hashCode()
/*     */ {
/* 392 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public boolean equals(Object paramObject)
/*     */ {
/* 406 */
        if ((paramObject instanceof Short)) {
/* 407 */
            return this.value == ((Short) paramObject).shortValue();
/*     */
        }
/* 409 */
        return false;
/*     */
    }

    /*     */
/*     */
    public int compareTo(Short paramShort)
/*     */ {
/* 426 */
        return compare(this.value, paramShort.value);
/*     */
    }

    /*     */
/*     */
    public static int compare(short paramShort1, short paramShort2)
/*     */ {
/* 444 */
        return paramShort1 - paramShort2;
/*     */
    }

    /*     */
/*     */
    public static short reverseBytes(short paramShort)
/*     */ {
/* 463 */
        return (short) ((paramShort & 0xFF00) >> 8 | paramShort << 8);
/*     */
    }

    /*     */
/*     */   private static class ShortCache
/*     */ {
        /* 205 */     static final Short[] cache = new Short[256];

        /*     */
/*     */     static {
/* 208 */
            for (int i = 0; i < cache.length; i++)
/* 209 */
                cache[i] = new Short((short) (i - 128));
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Short
 * JD-Core Version:    0.6.2
 */