/*      */ package sun.misc;
/*      */ 
/*      */ public class FpUtils
/*      */ {
/*  129 */   static double twoToTheDoubleScaleUp = powerOfTwoD(512);
/*  130 */   static double twoToTheDoubleScaleDown = powerOfTwoD(-512);
/*      */ 
/*      */   public static int getExponent(double paramDouble)
/*      */   {
/*  147 */     return (int)(((Double.doubleToRawLongBits(paramDouble) & 0x0) >> 52) - 1023L);
/*      */   }
/*      */ 
/*      */   public static int getExponent(float paramFloat)
/*      */   {
/*  160 */     return ((Float.floatToRawIntBits(paramFloat) & 0x7F800000) >> 23) - 127;
/*      */   }
/*      */ 
/*      */   static double powerOfTwoD(int paramInt)
/*      */   {
/*  168 */     assert ((paramInt >= -1022) && (paramInt <= 1023));
/*  169 */     return Double.longBitsToDouble(paramInt + 1023L << 52 & 0x0);
/*      */   }
/*      */ 
/*      */   static float powerOfTwoF(int paramInt)
/*      */   {
/*  178 */     assert ((paramInt >= -126) && (paramInt <= 127));
/*  179 */     return Float.intBitsToFloat(paramInt + 127 << 23 & 0x7F800000);
/*      */   }
/*      */ 
/*      */   public static double rawCopySign(double paramDouble1, double paramDouble2)
/*      */   {
/*  200 */     return Double.longBitsToDouble(Double.doubleToRawLongBits(paramDouble2) & 0x0 | Double.doubleToRawLongBits(paramDouble1) & 0xFFFFFFFF);
/*      */   }
/*      */ 
/*      */   public static float rawCopySign(float paramFloat1, float paramFloat2)
/*      */   {
/*  223 */     return Float.intBitsToFloat(Float.floatToRawIntBits(paramFloat2) & 0x80000000 | Float.floatToRawIntBits(paramFloat1) & 0x7FFFFFFF);
/*      */   }
/*      */ 
/*      */   public static boolean isFinite(double paramDouble)
/*      */   {
/*  242 */     return Math.abs(paramDouble) <= 1.7976931348623157E+308D;
/*      */   }
/*      */ 
/*      */   public static boolean isFinite(float paramFloat)
/*      */   {
/*  255 */     return Math.abs(paramFloat) <= 3.4028235E+38F;
/*      */   }
/*      */ 
/*      */   public static boolean isInfinite(double paramDouble)
/*      */   {
/*  271 */     return Double.isInfinite(paramDouble);
/*      */   }
/*      */ 
/*      */   public static boolean isInfinite(float paramFloat)
/*      */   {
/*  287 */     return Float.isInfinite(paramFloat);
/*      */   }
/*      */ 
/*      */   public static boolean isNaN(double paramDouble)
/*      */   {
/*  303 */     return Double.isNaN(paramDouble);
/*      */   }
/*      */ 
/*      */   public static boolean isNaN(float paramFloat)
/*      */   {
/*  319 */     return Float.isNaN(paramFloat);
/*      */   }
/*      */ 
/*      */   public static boolean isUnordered(double paramDouble1, double paramDouble2)
/*      */   {
/*  335 */     return (isNaN(paramDouble1)) || (isNaN(paramDouble2));
/*      */   }
/*      */ 
/*      */   public static boolean isUnordered(float paramFloat1, float paramFloat2)
/*      */   {
/*  351 */     return (isNaN(paramFloat1)) || (isNaN(paramFloat2));
/*      */   }
/*      */ 
/*      */   public static int ilogb(double paramDouble)
/*      */   {
/*  373 */     int i = getExponent(paramDouble);
/*      */ 
/*  375 */     switch (i) {
/*      */     case 1024:
/*  377 */       if (isNaN(paramDouble)) {
/*  378 */         return 1073741824;
/*      */       }
/*  380 */       return 268435456;
/*      */     case -1023:
/*  383 */       if (paramDouble == 0.0D) {
/*  384 */         return -268435456;
/*      */       }
/*      */ 
/*  387 */       long l = Double.doubleToRawLongBits(paramDouble);
/*      */ 
/*  398 */       l &= 4503599627370495L;
/*  399 */       assert (l != 0L);
/*      */ 
/*  405 */       while (l < 4503599627370496L)
/*      */       {
/*  407 */         l *= 2L;
/*  408 */         i--;
/*      */       }
/*  410 */       i++;
/*  411 */       assert ((i >= -1074) && (i < -1022));
/*      */ 
/*  414 */       return i;
/*      */     }
/*      */ 
/*  418 */     assert ((i >= -1022) && (i <= 1023));
/*      */ 
/*  420 */     return i;
/*      */   }
/*      */ 
/*      */   public static int ilogb(float paramFloat)
/*      */   {
/*  443 */     int i = getExponent(paramFloat);
/*      */ 
/*  445 */     switch (i) {
/*      */     case 128:
/*  447 */       if (isNaN(paramFloat)) {
/*  448 */         return 1073741824;
/*      */       }
/*  450 */       return 268435456;
/*      */     case -127:
/*  453 */       if (paramFloat == 0.0F) {
/*  454 */         return -268435456;
/*      */       }
/*      */ 
/*  457 */       int j = Float.floatToRawIntBits(paramFloat);
/*      */ 
/*  468 */       j &= 8388607;
/*  469 */       assert (j != 0);
/*      */ 
/*  475 */       while (j < 8388608)
/*      */       {
/*  477 */         j *= 2;
/*  478 */         i--;
/*      */       }
/*  480 */       i++;
/*  481 */       assert ((i >= -149) && (i < -126));
/*      */ 
/*  484 */       return i;
/*      */     }
/*      */ 
/*  488 */     assert ((i >= -126) && (i <= 127));
/*      */ 
/*  490 */     return i;
/*      */   }
/*      */ 
/*      */   public static double scalb(double paramDouble, int paramInt)
/*      */   {
/*  607 */     int i = 0;
/*  608 */     int j = 0;
/*  609 */     double d = (0.0D / 0.0D);
/*      */ 
/*  613 */     if (paramInt < 0) {
/*  614 */       paramInt = Math.max(paramInt, -2099);
/*  615 */       j = -512;
/*  616 */       d = twoToTheDoubleScaleDown;
/*      */     }
/*      */     else {
/*  619 */       paramInt = Math.min(paramInt, 2099);
/*  620 */       j = 512;
/*  621 */       d = twoToTheDoubleScaleUp;
/*      */     }
/*      */ 
/*  626 */     int k = paramInt >> 8 >>> 23;
/*  627 */     i = (paramInt + k & 0x1FF) - k;
/*      */ 
/*  629 */     paramDouble *= powerOfTwoD(i);
/*  630 */     paramInt -= i;
/*      */ 
/*  632 */     while (paramInt != 0) {
/*  633 */       paramDouble *= d;
/*  634 */       paramInt -= j;
/*      */     }
/*  636 */     return paramDouble;
/*      */   }
/*      */ 
/*      */   public static float scalb(float paramFloat, int paramInt)
/*      */   {
/*  680 */     paramInt = Math.max(Math.min(paramInt, 278), -278);
/*      */ 
/*  691 */     return (float)(paramFloat * powerOfTwoD(paramInt));
/*      */   }
/*      */ 
/*      */   public static double nextAfter(double paramDouble1, double paramDouble2)
/*      */   {
/*  747 */     if ((isNaN(paramDouble1)) || (isNaN(paramDouble2)))
/*      */     {
/*  749 */       return paramDouble1 + paramDouble2;
/*  750 */     }if (paramDouble1 == paramDouble2) {
/*  751 */       return paramDouble2;
/*      */     }
/*      */ 
/*  755 */     long l = Double.doubleToRawLongBits(paramDouble1 + 0.0D);
/*      */ 
/*  769 */     if (paramDouble2 > paramDouble1) {
/*  770 */       l += (l >= 0L ? 1L : -1L);
/*      */     } else {
/*  772 */       assert (paramDouble2 < paramDouble1);
/*  773 */       if (l > 0L) {
/*  774 */         l -= 1L;
/*      */       }
/*  776 */       else if (l < 0L) {
/*  777 */         l += 1L;
/*      */       }
/*      */       else
/*      */       {
/*  787 */         l = -9223372036854775807L;
/*      */       }
/*      */     }
/*  790 */     return Double.longBitsToDouble(l);
/*      */   }
/*      */ 
/*      */   public static float nextAfter(float paramFloat, double paramDouble)
/*      */   {
/*  847 */     if ((isNaN(paramFloat)) || (isNaN(paramDouble)))
/*      */     {
/*  849 */       return paramFloat + (float)paramDouble;
/*  850 */     }if (paramFloat == paramDouble) {
/*  851 */       return (float)paramDouble;
/*      */     }
/*      */ 
/*  855 */     int i = Float.floatToRawIntBits(paramFloat + 0.0F);
/*      */ 
/*  869 */     if (paramDouble > paramFloat) {
/*  870 */       i += (i >= 0 ? 1 : -1);
/*      */     } else {
/*  872 */       assert (paramDouble < paramFloat);
/*  873 */       if (i > 0) {
/*  874 */         i--;
/*      */       }
/*  876 */       else if (i < 0) {
/*  877 */         i++;
/*      */       }
/*      */       else
/*      */       {
/*  887 */         i = -2147483647;
/*      */       }
/*      */     }
/*  890 */     return Float.intBitsToFloat(i);
/*      */   }
/*      */ 
/*      */   public static double nextUp(double paramDouble)
/*      */   {
/*  920 */     if ((isNaN(paramDouble)) || (paramDouble == (1.0D / 0.0D))) {
/*  921 */       return paramDouble;
/*      */     }
/*  923 */     paramDouble += 0.0D;
/*  924 */     return Double.longBitsToDouble(Double.doubleToRawLongBits(paramDouble) + (paramDouble >= 0.0D ? 1L : -1L));
/*      */   }
/*      */ 
/*      */   public static float nextUp(float paramFloat)
/*      */   {
/*  955 */     if ((isNaN(paramFloat)) || (paramFloat == (1.0F / 1.0F))) {
/*  956 */       return paramFloat;
/*      */     }
/*  958 */     paramFloat += 0.0F;
/*  959 */     return Float.intBitsToFloat(Float.floatToRawIntBits(paramFloat) + (paramFloat >= 0.0F ? 1 : -1));
/*      */   }
/*      */ 
/*      */   public static double nextDown(double paramDouble)
/*      */   {
/*  990 */     if ((isNaN(paramDouble)) || (paramDouble == (-1.0D / 0.0D))) {
/*  991 */       return paramDouble;
/*      */     }
/*  993 */     if (paramDouble == 0.0D) {
/*  994 */       return -4.940656458412465E-324D;
/*      */     }
/*  996 */     return Double.longBitsToDouble(Double.doubleToRawLongBits(paramDouble) + (paramDouble > 0.0D ? -1L : 1L));
/*      */   }
/*      */ 
/*      */   public static double nextDown(float paramFloat)
/*      */   {
/* 1027 */     if ((isNaN(paramFloat)) || (paramFloat == (1.0F / -1.0F))) {
/* 1028 */       return paramFloat;
/*      */     }
/* 1030 */     if (paramFloat == 0.0F) {
/* 1031 */       return -1.401298464324817E-045D;
/*      */     }
/* 1033 */     return Float.intBitsToFloat(Float.floatToRawIntBits(paramFloat) + (paramFloat > 0.0F ? -1 : 1));
/*      */   }
/*      */ 
/*      */   public static double copySign(double paramDouble1, double paramDouble2)
/*      */   {
/* 1052 */     return rawCopySign(paramDouble1, isNaN(paramDouble2) ? 1.0D : paramDouble2);
/*      */   }
/*      */ 
/*      */   public static float copySign(float paramFloat1, float paramFloat2)
/*      */   {
/* 1068 */     return rawCopySign(paramFloat1, isNaN(paramFloat2) ? 1.0F : paramFloat2);
/*      */   }
/*      */ 
/*      */   public static double ulp(double paramDouble)
/*      */   {
/* 1095 */     int i = getExponent(paramDouble);
/*      */ 
/* 1097 */     switch (i) {
/*      */     case 1024:
/* 1099 */       return Math.abs(paramDouble);
/*      */     case -1023:
/* 1102 */       return 4.9E-324D;
/*      */     }
/*      */ 
/* 1105 */     assert ((i <= 1023) && (i >= -1022));
/*      */ 
/* 1108 */     i -= 52;
/* 1109 */     if (i >= -1022) {
/* 1110 */       return powerOfTwoD(i);
/*      */     }
/*      */ 
/* 1116 */     return Double.longBitsToDouble(1L << i - -1074);
/*      */   }
/*      */ 
/*      */   public static float ulp(float paramFloat)
/*      */   {
/* 1146 */     int i = getExponent(paramFloat);
/*      */ 
/* 1148 */     switch (i) {
/*      */     case 128:
/* 1150 */       return Math.abs(paramFloat);
/*      */     case -127:
/* 1153 */       return 1.4E-45F;
/*      */     }
/*      */ 
/* 1156 */     assert ((i <= 127) && (i >= -126));
/*      */ 
/* 1159 */     i -= 23;
/* 1160 */     if (i >= -126) {
/* 1161 */       return powerOfTwoF(i);
/*      */     }
/*      */ 
/* 1167 */     return Float.intBitsToFloat(1 << i - -149);
/*      */   }
/*      */ 
/*      */   public static double signum(double paramDouble)
/*      */   {
/* 1191 */     return (paramDouble == 0.0D) || (isNaN(paramDouble)) ? paramDouble : copySign(1.0D, paramDouble);
/*      */   }
/*      */ 
/*      */   public static float signum(float paramFloat)
/*      */   {
/* 1212 */     return (paramFloat == 0.0F) || (isNaN(paramFloat)) ? paramFloat : copySign(1.0F, paramFloat);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FpUtils
 * JD-Core Version:    0.6.2
 */