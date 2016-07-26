/*     */
package java.lang;
/*     */ 
/*     */

import sun.misc.FloatingDecimal;
/*     */ import sun.misc.FpUtils;

/*     */
/*     */ public final class Float extends Number
/*     */ implements Comparable<Float>
/*     */ {
    /*     */   public static final float POSITIVE_INFINITY = (1.0F / 1.0F);
    /*     */   public static final float NEGATIVE_INFINITY = (1.0F / -1.0F);
    /*     */   public static final float NaN = (0.0F / 0.0F);
    /*     */   public static final float MAX_VALUE = 3.4028235E+38F;
    /*     */   public static final float MIN_NORMAL = 1.175494E-038F;
    /*     */   public static final float MIN_VALUE = 1.4E-45F;
    /*     */   public static final int MAX_EXPONENT = 127;
    /*     */   public static final int MIN_EXPONENT = -126;
    /*     */   public static final int SIZE = 32;
    /* 130 */   public static final Class<Float> TYPE = Class.getPrimitiveClass("float");
    /*     */   private final float value;
    /*     */   private static final long serialVersionUID = -2671257302660747028L;

    /*     */
/*     */
    public static String toString(float paramFloat)
/*     */ {
/* 199 */
        return new FloatingDecimal(paramFloat).toJavaFormatString();
/*     */
    }

    /*     */
/*     */
    public static String toHexString(float paramFloat)
/*     */ {
/* 277 */
        if ((Math.abs(paramFloat) < 1.175494E-038F) && (paramFloat != 0.0F))
/*     */ {
/* 282 */
            String str = Double.toHexString(FpUtils.scalb(paramFloat, -896));
/*     */ 
/* 286 */
            return str.replaceFirst("p-1022$", "p-126");
/*     */
        }
/*     */ 
/* 289 */
        return Double.toHexString(paramFloat);
/*     */
    }

    /*     */
/*     */
    public static Float valueOf(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 417 */
        return new Float(FloatingDecimal.readJavaFormatString(paramString).floatValue());
/*     */
    }

    /*     */
/*     */
    public static Float valueOf(float paramFloat)
/*     */ {
/* 434 */
        return new Float(paramFloat);
/*     */
    }

    /*     */
/*     */
    public static float parseFloat(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 452 */
        return FloatingDecimal.readJavaFormatString(paramString).floatValue();
/*     */
    }

    /*     */
/*     */
    public static boolean isNaN(float paramFloat)
/*     */ {
/* 464 */
        return paramFloat != paramFloat;
/*     */
    }

    /*     */
/*     */
    public static boolean isInfinite(float paramFloat)
/*     */ {
/* 476 */
        return (paramFloat == (1.0F / 1.0F)) || (paramFloat == (1.0F / -1.0F));
/*     */
    }

    /*     */
/*     */
    public Float(float paramFloat)
/*     */ {
/* 493 */
        this.value = paramFloat;
/*     */
    }

    /*     */
/*     */
    public Float(double paramDouble)
/*     */ {
/* 503 */
        this.value = ((float) paramDouble);
/*     */
    }

    /*     */
/*     */
    public Float(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 519 */
        this(valueOf(paramString).floatValue());
/*     */
    }

    /*     */
/*     */
    public boolean isNaN()
/*     */ {
/* 530 */
        return isNaN(this.value);
/*     */
    }

    /*     */
/*     */
    public boolean isInfinite()
/*     */ {
/* 542 */
        return isInfinite(this.value);
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 555 */
        return toString(this.value);
/*     */
    }

    /*     */
/*     */
    public byte byteValue()
/*     */ {
/* 566 */
        return (byte) (int) this.value;
/*     */
    }

    /*     */
/*     */
    public short shortValue()
/*     */ {
/* 578 */
        return (short) (int) this.value;
/*     */
    }

    /*     */
/*     */
    public int intValue()
/*     */ {
/* 589 */
        return (int) this.value;
/*     */
    }

    /*     */
/*     */
    public long longValue()
/*     */ {
/* 600 */
        return () this.value;
/*     */
    }

    /*     */
/*     */
    public float floatValue()
/*     */ {
/* 609 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public double doubleValue()
/*     */ {
/* 620 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public int hashCode()
/*     */ {
/* 633 */
        return floatToIntBits(this.value);
/*     */
    }

    /*     */
/*     */
    public boolean equals(Object paramObject)
/*     */ {
/* 677 */
        return ((paramObject instanceof Float)) && (floatToIntBits(((Float) paramObject).value) == floatToIntBits(this.value));
/*     */
    }

    /*     */
/*     */
    public static int floatToIntBits(float paramFloat)
/*     */ {
/* 713 */
        int i = floatToRawIntBits(paramFloat);
/*     */ 
/* 716 */
        if (((i & 0x7F800000) == 2139095040) && ((i & 0x7FFFFF) != 0))
/*     */ {
/* 719 */
            i = 2143289344;
/* 720 */
        }
        return i;
/*     */
    }

    /*     */
/*     */
    public static native int floatToRawIntBits(float paramFloat);

    /*     */
/*     */
    public static native float intBitsToFloat(int paramInt);

    /*     */
/*     */
    public int compareTo(Float paramFloat)
/*     */ {
/* 854 */
        return compare(this.value, paramFloat.value);
/*     */
    }

    /*     */
/*     */
    public static int compare(float paramFloat1, float paramFloat2)
/*     */ {
/* 876 */
        if (paramFloat1 < paramFloat2)
/* 877 */ return -1;
/* 878 */
        if (paramFloat1 > paramFloat2) {
/* 879 */
            return 1;
/*     */
        }
/*     */ 
/* 882 */
        int i = floatToIntBits(paramFloat1);
/* 883 */
        int j = floatToIntBits(paramFloat2);
/*     */ 
/* 885 */
        return i < j ? -1 : i == j ? 0 : 1;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Float
 * JD-Core Version:    0.6.2
 */