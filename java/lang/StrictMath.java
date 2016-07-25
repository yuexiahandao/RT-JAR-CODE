/*      */ package java.lang;
/*      */ 
/*      */ import java.util.Random;
/*      */ import sun.misc.FpUtils;
/*      */ 
/*      */ public final class StrictMath
/*      */ {
/*      */   public static final double E = 2.718281828459045D;
/*      */   public static final double PI = 3.141592653589793D;
/*      */   private static Random randomNumberGenerator;
/*  799 */   private static long negativeZeroFloatBits = Float.floatToRawIntBits(-0.0F);
/*  800 */   private static long negativeZeroDoubleBits = Double.doubleToRawLongBits(-0.0D);
/*      */ 
/*      */   public static native double sin(double paramDouble);
/*      */ 
/*      */   public static native double cos(double paramDouble);
/*      */ 
/*      */   public static native double tan(double paramDouble);
/*      */ 
/*      */   public static native double asin(double paramDouble);
/*      */ 
/*      */   public static native double acos(double paramDouble);
/*      */ 
/*      */   public static native double atan(double paramDouble);
/*      */ 
/*      */   public static strictfp double toRadians(double paramDouble)
/*      */   {
/*  165 */     return paramDouble / 180.0D * 3.141592653589793D;
/*      */   }
/*      */ 
/*      */   public static strictfp double toDegrees(double paramDouble)
/*      */   {
/*  180 */     return paramDouble * 180.0D / 3.141592653589793D;
/*      */   }
/*      */ 
/*      */   public static native double exp(double paramDouble);
/*      */ 
/*      */   public static native double log(double paramDouble);
/*      */ 
/*      */   public static native double log10(double paramDouble);
/*      */ 
/*      */   public static native double sqrt(double paramDouble);
/*      */ 
/*      */   public static native double cbrt(double paramDouble);
/*      */ 
/*      */   public static native double IEEEremainder(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public static double ceil(double paramDouble)
/*      */   {
/*  321 */     return floorOrCeil(paramDouble, -0.0D, 1.0D, 1.0D);
/*      */   }
/*      */ 
/*      */   public static double floor(double paramDouble)
/*      */   {
/*  340 */     return floorOrCeil(paramDouble, -1.0D, 0.0D, -1.0D);
/*      */   }
/*      */ 
/*      */   private static double floorOrCeil(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/*  355 */     int i = Math.getExponent(paramDouble1);
/*      */ 
/*  357 */     if (i < 0)
/*      */     {
/*  363 */       return paramDouble1 < 0.0D ? paramDouble2 : paramDouble1 == 0.0D ? paramDouble1 : paramDouble3;
/*      */     }
/*  365 */     if (i >= 52)
/*      */     {
/*  369 */       return paramDouble1;
/*      */     }
/*      */ 
/*  373 */     assert ((i >= 0) && (i <= 51));
/*      */ 
/*  375 */     long l1 = Double.doubleToRawLongBits(paramDouble1);
/*  376 */     long l2 = 4503599627370495L >> i;
/*      */ 
/*  378 */     if ((l2 & l1) == 0L) {
/*  379 */       return paramDouble1;
/*      */     }
/*  381 */     double d = Double.longBitsToDouble(l1 & (l2 ^ 0xFFFFFFFF));
/*  382 */     if (paramDouble4 * paramDouble1 > 0.0D)
/*  383 */       d += paramDouble4;
/*  384 */     return d;
/*      */   }
/*      */ 
/*      */   public static double rint(double paramDouble)
/*      */   {
/*  430 */     double d1 = 4503599627370496.0D;
/*  431 */     double d2 = FpUtils.rawCopySign(1.0D, paramDouble);
/*  432 */     paramDouble = Math.abs(paramDouble);
/*      */ 
/*  434 */     if (paramDouble < d1) {
/*  435 */       paramDouble = d1 + paramDouble - d1;
/*      */     }
/*      */ 
/*  438 */     return d2 * paramDouble;
/*      */   }
/*      */ 
/*      */   public static native double atan2(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public static native double pow(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public static int round(float paramFloat)
/*      */   {
/*  635 */     return Math.round(paramFloat);
/*      */   }
/*      */ 
/*      */   public static long round(double paramDouble)
/*      */   {
/*  659 */     return Math.round(paramDouble);
/*      */   }
/*      */ 
/*      */   private static synchronized Random initRNG()
/*      */   {
/*  665 */     Random localRandom = randomNumberGenerator;
/*  666 */     return localRandom == null ? (StrictMath.randomNumberGenerator = new Random()) : localRandom;
/*      */   }
/*      */ 
/*      */   public static double random()
/*      */   {
/*  693 */     Random localRandom = randomNumberGenerator;
/*  694 */     if (localRandom == null) localRandom = initRNG();
/*  695 */     return localRandom.nextDouble();
/*      */   }
/*      */ 
/*      */   public static int abs(int paramInt)
/*      */   {
/*  712 */     return paramInt < 0 ? -paramInt : paramInt;
/*      */   }
/*      */ 
/*      */   public static long abs(long paramLong)
/*      */   {
/*  729 */     return paramLong < 0L ? -paramLong : paramLong;
/*      */   }
/*      */ 
/*      */   public static float abs(float paramFloat)
/*      */   {
/*  748 */     return paramFloat <= 0.0F ? 0.0F - paramFloat : paramFloat;
/*      */   }
/*      */ 
/*      */   public static double abs(double paramDouble)
/*      */   {
/*  767 */     return paramDouble <= 0.0D ? 0.0D - paramDouble : paramDouble;
/*      */   }
/*      */ 
/*      */   public static int max(int paramInt1, int paramInt2)
/*      */   {
/*  781 */     return paramInt1 >= paramInt2 ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */   public static long max(long paramLong1, long paramLong2)
/*      */   {
/*  795 */     return paramLong1 >= paramLong2 ? paramLong1 : paramLong2;
/*      */   }
/*      */ 
/*      */   public static float max(float paramFloat1, float paramFloat2)
/*      */   {
/*  817 */     if (paramFloat1 != paramFloat1)
/*  818 */       return paramFloat1;
/*  819 */     if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F) && (Float.floatToRawIntBits(paramFloat1) == negativeZeroFloatBits))
/*      */     {
/*  823 */       return paramFloat2;
/*      */     }
/*  825 */     return paramFloat1 >= paramFloat2 ? paramFloat1 : paramFloat2;
/*      */   }
/*      */ 
/*      */   public static double max(double paramDouble1, double paramDouble2)
/*      */   {
/*  843 */     if (paramDouble1 != paramDouble1)
/*  844 */       return paramDouble1;
/*  845 */     if ((paramDouble1 == 0.0D) && (paramDouble2 == 0.0D) && (Double.doubleToRawLongBits(paramDouble1) == negativeZeroDoubleBits))
/*      */     {
/*  849 */       return paramDouble2;
/*      */     }
/*  851 */     return paramDouble1 >= paramDouble2 ? paramDouble1 : paramDouble2;
/*      */   }
/*      */ 
/*      */   public static int min(int paramInt1, int paramInt2)
/*      */   {
/*  865 */     return paramInt1 <= paramInt2 ? paramInt1 : paramInt2;
/*      */   }
/*      */ 
/*      */   public static long min(long paramLong1, long paramLong2)
/*      */   {
/*  879 */     return paramLong1 <= paramLong2 ? paramLong1 : paramLong2;
/*      */   }
/*      */ 
/*      */   public static float min(float paramFloat1, float paramFloat2)
/*      */   {
/*  897 */     if (paramFloat1 != paramFloat1)
/*  898 */       return paramFloat1;
/*  899 */     if ((paramFloat1 == 0.0F) && (paramFloat2 == 0.0F) && (Float.floatToRawIntBits(paramFloat2) == negativeZeroFloatBits))
/*      */     {
/*  903 */       return paramFloat2;
/*      */     }
/*  905 */     return paramFloat1 <= paramFloat2 ? paramFloat1 : paramFloat2;
/*      */   }
/*      */ 
/*      */   public static double min(double paramDouble1, double paramDouble2)
/*      */   {
/*  923 */     if (paramDouble1 != paramDouble1)
/*  924 */       return paramDouble1;
/*  925 */     if ((paramDouble1 == 0.0D) && (paramDouble2 == 0.0D) && (Double.doubleToRawLongBits(paramDouble2) == negativeZeroDoubleBits))
/*      */     {
/*  929 */       return paramDouble2;
/*      */     }
/*  931 */     return paramDouble1 <= paramDouble2 ? paramDouble1 : paramDouble2;
/*      */   }
/*      */ 
/*      */   public static double ulp(double paramDouble)
/*      */   {
/*  958 */     return FpUtils.ulp(paramDouble);
/*      */   }
/*      */ 
/*      */   public static float ulp(float paramFloat)
/*      */   {
/*  985 */     return FpUtils.ulp(paramFloat);
/*      */   }
/*      */ 
/*      */   public static double signum(double paramDouble)
/*      */   {
/* 1006 */     return FpUtils.signum(paramDouble);
/*      */   }
/*      */ 
/*      */   public static float signum(float paramFloat)
/*      */   {
/* 1027 */     return FpUtils.signum(paramFloat);
/*      */   }
/*      */ 
/*      */   public static native double sinh(double paramDouble);
/*      */ 
/*      */   public static native double cosh(double paramDouble);
/*      */ 
/*      */   public static native double tanh(double paramDouble);
/*      */ 
/*      */   public static native double hypot(double paramDouble1, double paramDouble2);
/*      */ 
/*      */   public static native double expm1(double paramDouble);
/*      */ 
/*      */   public static native double log1p(double paramDouble);
/*      */ 
/*      */   public static double copySign(double paramDouble1, double paramDouble2)
/*      */   {
/* 1205 */     return FpUtils.copySign(paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   public static float copySign(float paramFloat1, float paramFloat2)
/*      */   {
/* 1221 */     return FpUtils.copySign(paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public static int getExponent(float paramFloat)
/*      */   {
/* 1237 */     return FpUtils.getExponent(paramFloat);
/*      */   }
/*      */ 
/*      */   public static int getExponent(double paramDouble)
/*      */   {
/* 1254 */     return FpUtils.getExponent(paramDouble);
/*      */   }
/*      */ 
/*      */   public static double nextAfter(double paramDouble1, double paramDouble2)
/*      */   {
/* 1297 */     return FpUtils.nextAfter(paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   public static float nextAfter(float paramFloat, double paramDouble)
/*      */   {
/* 1339 */     return FpUtils.nextAfter(paramFloat, paramDouble);
/*      */   }
/*      */ 
/*      */   public static double nextUp(double paramDouble)
/*      */   {
/* 1368 */     return FpUtils.nextUp(paramDouble);
/*      */   }
/*      */ 
/*      */   public static float nextUp(float paramFloat)
/*      */   {
/* 1397 */     return FpUtils.nextUp(paramFloat);
/*      */   }
/*      */ 
/*      */   public static double scalb(double paramDouble, int paramInt)
/*      */   {
/* 1432 */     return FpUtils.scalb(paramDouble, paramInt);
/*      */   }
/*      */ 
/*      */   public static float scalb(float paramFloat, int paramInt)
/*      */   {
/* 1466 */     return FpUtils.scalb(paramFloat, paramInt);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.StrictMath
 * JD-Core Version:    0.6.2
 */