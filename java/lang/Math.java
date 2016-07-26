/*      */
package java.lang;
/*      */ 
/*      */

import java.util.Random;
/*      */ import sun.misc.FpUtils;

/*      */
/*      */ public final class Math
/*      */ {
    /*      */   public static final double E = 2.718281828459045D;
    /*      */   public static final double PI = 3.141592653589793D;
    /*      */   private static Random randomNumberGenerator;
    /*  819 */   private static long negativeZeroFloatBits = Float.floatToIntBits(-0.0F);
    /*  820 */   private static long negativeZeroDoubleBits = Double.doubleToLongBits(-0.0D);

    /*      */
/*      */
    public static double sin(double paramDouble)
/*      */ {
/*  121 */
        return StrictMath.sin(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double cos(double paramDouble)
/*      */ {
/*  136 */
        return StrictMath.cos(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double tan(double paramDouble)
/*      */ {
/*  153 */
        return StrictMath.tan(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double asin(double paramDouble)
/*      */ {
/*  171 */
        return StrictMath.asin(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double acos(double paramDouble)
/*      */ {
/*  187 */
        return StrictMath.acos(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double atan(double paramDouble)
/*      */ {
/*  204 */
        return StrictMath.atan(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double toRadians(double paramDouble)
/*      */ {
/*  218 */
        return paramDouble / 180.0D * 3.141592653589793D;
/*      */
    }

    /*      */
/*      */
    public static double toDegrees(double paramDouble)
/*      */ {
/*  234 */
        return paramDouble * 180.0D / 3.141592653589793D;
/*      */
    }

    /*      */
/*      */
    public static double exp(double paramDouble)
/*      */ {
/*  254 */
        return StrictMath.exp(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double log(double paramDouble)
/*      */ {
/*  275 */
        return StrictMath.log(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double log10(double paramDouble)
/*      */ {
/*  300 */
        return StrictMath.log10(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double sqrt(double paramDouble)
/*      */ {
/*  321 */
        return StrictMath.sqrt(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double cbrt(double paramDouble)
/*      */ {
/*  356 */
        return StrictMath.cbrt(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double IEEEremainder(double paramDouble1, double paramDouble2)
/*      */ {
/*  382 */
        return StrictMath.IEEEremainder(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static double ceil(double paramDouble)
/*      */ {
/*  405 */
        return StrictMath.ceil(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double floor(double paramDouble)
/*      */ {
/*  424 */
        return StrictMath.floor(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double rint(double paramDouble)
/*      */ {
/*  443 */
        return StrictMath.rint(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double atan2(double paramDouble1, double paramDouble2)
/*      */ {
/*  499 */
        return StrictMath.atan2(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static double pow(double paramDouble1, double paramDouble2)
/*      */ {
/*  626 */
        return StrictMath.pow(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static int round(float paramFloat)
/*      */ {
/*  650 */
        if (paramFloat != 0.5F) {
/*  651 */
            return (int) floor(paramFloat + 0.5F);
/*      */
        }
/*  653 */
        return 0;
/*      */
    }

    /*      */
/*      */
    public static long round(double paramDouble)
/*      */ {
/*  677 */
        if (paramDouble != 0.4999999999999999D) {
/*  678 */
            return () floor(paramDouble + 0.5D);
/*      */
        }
/*  680 */
        return 0L;
/*      */
    }

    /*      */
/*      */
    private static synchronized Random initRNG()
/*      */ {
/*  686 */
        Random localRandom = randomNumberGenerator;
/*  687 */
        return localRandom == null ? (Math.randomNumberGenerator = new Random()) : localRandom;
/*      */
    }

    /*      */
/*      */
    public static double random()
/*      */ {
/*  714 */
        Random localRandom = randomNumberGenerator;
/*  715 */
        if (localRandom == null) localRandom = initRNG();
/*  716 */
        return localRandom.nextDouble();
/*      */
    }

    /*      */
/*      */
    public static int abs(int paramInt)
/*      */ {
/*  733 */
        return paramInt < 0 ? -paramInt : paramInt;
/*      */
    }

    /*      */
/*      */
    public static long abs(long paramLong)
/*      */ {
/*  750 */
        return paramLong < 0L ? -paramLong : paramLong;
/*      */
    }

    /*      */
/*      */
    public static float abs(float paramFloat)
/*      */ {
/*  769 */
        return paramFloat <= 0.0F ? 0.0F - paramFloat : paramFloat;
/*      */
    }

    /*      */
/*      */
    public static double abs(double paramDouble)
/*      */ {
/*  788 */
        return paramDouble <= 0.0D ? 0.0D - paramDouble : paramDouble;
/*      */
    }

    /*      */
/*      */
    public static int max(int paramInt1, int paramInt2)
/*      */ {
/*  802 */
        return paramInt1 >= paramInt2 ? paramInt1 : paramInt2;
/*      */
    }

    /*      */
/*      */
    public static long max(long paramLong1, long paramLong2)
/*      */ {
/*  816 */
        return paramLong1 >= paramLong2 ? paramLong1 : paramLong2;
/*      */
    }

    /*      */
/*      */
    public static float max(float paramFloat1, float paramFloat2)
/*      */ {
/*  837 */
        if (paramFloat1 != paramFloat1) return paramFloat1;
/*  838 */
        if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F) && (Float.floatToIntBits(paramFloat1) == negativeZeroFloatBits))
/*      */ {
/*  840 */
            return paramFloat2;
/*      */
        }
/*  842 */
        return paramFloat1 >= paramFloat2 ? paramFloat1 : paramFloat2;
/*      */
    }

    /*      */
/*      */
    public static double max(double paramDouble1, double paramDouble2)
/*      */ {
/*  860 */
        if (paramDouble1 != paramDouble1) return paramDouble1;
/*  861 */
        if ((paramDouble1 == 0.0D) && (paramDouble2 == 0.0D) && (Double.doubleToLongBits(paramDouble1) == negativeZeroDoubleBits))
/*      */ {
/*  863 */
            return paramDouble2;
/*      */
        }
/*  865 */
        return paramDouble1 >= paramDouble2 ? paramDouble1 : paramDouble2;
/*      */
    }

    /*      */
/*      */
    public static int min(int paramInt1, int paramInt2)
/*      */ {
/*  879 */
        return paramInt1 <= paramInt2 ? paramInt1 : paramInt2;
/*      */
    }

    /*      */
/*      */
    public static long min(long paramLong1, long paramLong2)
/*      */ {
/*  893 */
        return paramLong1 <= paramLong2 ? paramLong1 : paramLong2;
/*      */
    }

    /*      */
/*      */
    public static float min(float paramFloat1, float paramFloat2)
/*      */ {
/*  911 */
        if (paramFloat1 != paramFloat1) return paramFloat1;
/*  912 */
        if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F) && (Float.floatToIntBits(paramFloat2) == negativeZeroFloatBits))
/*      */ {
/*  914 */
            return paramFloat2;
/*      */
        }
/*  916 */
        return paramFloat1 <= paramFloat2 ? paramFloat1 : paramFloat2;
/*      */
    }

    /*      */
/*      */
    public static double min(double paramDouble1, double paramDouble2)
/*      */ {
/*  934 */
        if (paramDouble1 != paramDouble1) return paramDouble1;
/*  935 */
        if ((paramDouble1 == 0.0D) && (paramDouble2 == 0.0D) && (Double.doubleToLongBits(paramDouble2) == negativeZeroDoubleBits))
/*      */ {
/*  937 */
            return paramDouble2;
/*      */
        }
/*  939 */
        return paramDouble1 <= paramDouble2 ? paramDouble1 : paramDouble2;
/*      */
    }

    /*      */
/*      */
    public static double ulp(double paramDouble)
/*      */ {
/*  966 */
        return FpUtils.ulp(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static float ulp(float paramFloat)
/*      */ {
/*  993 */
        return FpUtils.ulp(paramFloat);
/*      */
    }

    /*      */
/*      */
    public static double signum(double paramDouble)
/*      */ {
/* 1014 */
        return FpUtils.signum(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static float signum(float paramFloat)
/*      */ {
/* 1035 */
        return FpUtils.signum(paramFloat);
/*      */
    }

    /*      */
/*      */
    public static double sinh(double paramDouble)
/*      */ {
/* 1064 */
        return StrictMath.sinh(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double cosh(double paramDouble)
/*      */ {
/* 1092 */
        return StrictMath.cosh(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double tanh(double paramDouble)
/*      */ {
/* 1132 */
        return StrictMath.tanh(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double hypot(double paramDouble1, double paramDouble2)
/*      */ {
/* 1161 */
        return StrictMath.hypot(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static double expm1(double paramDouble)
/*      */ {
/* 1199 */
        return StrictMath.expm1(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double log1p(double paramDouble)
/*      */ {
/* 1236 */
        return StrictMath.log1p(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double copySign(double paramDouble1, double paramDouble2)
/*      */ {
/* 1255 */
        return FpUtils.rawCopySign(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static float copySign(float paramFloat1, float paramFloat2)
/*      */ {
/* 1274 */
        return FpUtils.rawCopySign(paramFloat1, paramFloat2);
/*      */
    }

    /*      */
/*      */
    public static int getExponent(float paramFloat)
/*      */ {
/* 1292 */
        return FpUtils.getExponent(paramFloat);
/*      */
    }

    /*      */
/*      */
    public static int getExponent(double paramDouble)
/*      */ {
/* 1310 */
        return FpUtils.getExponent(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double nextAfter(double paramDouble1, double paramDouble2)
/*      */ {
/* 1354 */
        return FpUtils.nextAfter(paramDouble1, paramDouble2);
/*      */
    }

    /*      */
/*      */
    public static float nextAfter(float paramFloat, double paramDouble)
/*      */ {
/* 1397 */
        return FpUtils.nextAfter(paramFloat, paramDouble);
/*      */
    }

    /*      */
/*      */
    public static double nextUp(double paramDouble)
/*      */ {
/* 1426 */
        return FpUtils.nextUp(paramDouble);
/*      */
    }

    /*      */
/*      */
    public static float nextUp(float paramFloat)
/*      */ {
/* 1455 */
        return FpUtils.nextUp(paramFloat);
/*      */
    }

    /*      */
/*      */
    public static double scalb(double paramDouble, int paramInt)
/*      */ {
/* 1490 */
        return FpUtils.scalb(paramDouble, paramInt);
/*      */
    }

    /*      */
/*      */
    public static float scalb(float paramFloat, int paramInt)
/*      */ {
/* 1524 */
        return FpUtils.scalb(paramFloat, paramInt);
/*      */
    }
/*      */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Math
 * JD-Core Version:    0.6.2
 */