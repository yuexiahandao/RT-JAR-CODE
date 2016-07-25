/*     */ package java.lang;
/*     */ 
/*     */ import sun.misc.FloatingDecimal;
/*     */ import sun.misc.FpUtils;
/*     */ 
/*     */ public final class Double extends Number
/*     */   implements Comparable<Double>
/*     */ {
/*     */   public static final double POSITIVE_INFINITY = (1.0D / 0.0D);
/*     */   public static final double NEGATIVE_INFINITY = (-1.0D / 0.0D);
/*     */   public static final double NaN = (0.0D / 0.0D);
/*     */   public static final double MAX_VALUE = 1.7976931348623157E+308D;
/*     */   public static final double MIN_NORMAL = 2.225073858507201E-308D;
/*     */   public static final double MIN_VALUE = 4.9E-324D;
/*     */   public static final int MAX_EXPONENT = 1023;
/*     */   public static final int MIN_EXPONENT = -1022;
/*     */   public static final int SIZE = 64;
/* 131 */   public static final Class<Double> TYPE = Class.getPrimitiveClass("double");
/*     */   private final double value;
/*     */   private static final long serialVersionUID = -9172774392245257468L;
/*     */ 
/*     */   public static String toString(double paramDouble)
/*     */   {
/* 196 */     return new FloatingDecimal(paramDouble).toJavaFormatString();
/*     */   }
/*     */ 
/*     */   public static String toHexString(double paramDouble)
/*     */   {
/* 279 */     if (!FpUtils.isFinite(paramDouble))
/*     */     {
/* 281 */       return toString(paramDouble);
/*     */     }
/*     */ 
/* 284 */     StringBuffer localStringBuffer = new StringBuffer(24);
/*     */ 
/* 286 */     if (FpUtils.rawCopySign(1.0D, paramDouble) == -1.0D) {
/* 287 */       localStringBuffer.append("-");
/*     */     }
/* 289 */     localStringBuffer.append("0x");
/*     */ 
/* 291 */     paramDouble = Math.abs(paramDouble);
/*     */ 
/* 293 */     if (paramDouble == 0.0D) {
/* 294 */       localStringBuffer.append("0.0p0");
/*     */     }
/*     */     else {
/* 297 */       int i = paramDouble < 2.225073858507201E-308D ? 1 : 0;
/*     */ 
/* 302 */       long l = doubleToLongBits(paramDouble) & 0xFFFFFFFF | 0x0;
/*     */ 
/* 308 */       localStringBuffer.append(i != 0 ? "0." : "1.");
/*     */ 
/* 314 */       String str = Long.toHexString(l).substring(3, 16);
/* 315 */       localStringBuffer.append(str.equals("0000000000000") ? "0" : str.replaceFirst("0{1,12}$", ""));
/*     */ 
/* 323 */       localStringBuffer.append("p" + (i != 0 ? -1022 : FpUtils.getExponent(paramDouble)));
/*     */     }
/*     */ 
/* 327 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static Double valueOf(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 504 */     return new Double(FloatingDecimal.readJavaFormatString(paramString).doubleValue());
/*     */   }
/*     */ 
/*     */   public static Double valueOf(double paramDouble)
/*     */   {
/* 521 */     return new Double(paramDouble);
/*     */   }
/*     */ 
/*     */   public static double parseDouble(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 540 */     return FloatingDecimal.readJavaFormatString(paramString).doubleValue();
/*     */   }
/*     */ 
/*     */   public static boolean isNaN(double paramDouble)
/*     */   {
/* 552 */     return paramDouble != paramDouble;
/*     */   }
/*     */ 
/*     */   public static boolean isInfinite(double paramDouble)
/*     */   {
/* 564 */     return (paramDouble == (1.0D / 0.0D)) || (paramDouble == (-1.0D / 0.0D));
/*     */   }
/*     */ 
/*     */   public Double(double paramDouble)
/*     */   {
/* 581 */     this.value = paramDouble;
/*     */   }
/*     */ 
/*     */   public Double(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 597 */     this(valueOf(paramString).doubleValue());
/*     */   }
/*     */ 
/*     */   public boolean isNaN()
/*     */   {
/* 608 */     return isNaN(this.value);
/*     */   }
/*     */ 
/*     */   public boolean isInfinite()
/*     */   {
/* 620 */     return isInfinite(this.value);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 633 */     return toString(this.value);
/*     */   }
/*     */ 
/*     */   public byte byteValue()
/*     */   {
/* 645 */     return (byte)(int)this.value;
/*     */   }
/*     */ 
/*     */   public short shortValue()
/*     */   {
/* 657 */     return (short)(int)this.value;
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 668 */     return (int)this.value;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 679 */     return ()this.value;
/*     */   }
/*     */ 
/*     */   public float floatValue()
/*     */   {
/* 691 */     return (float)this.value;
/*     */   }
/*     */ 
/*     */   public double doubleValue()
/*     */   {
/* 701 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 726 */     long l = doubleToLongBits(this.value);
/* 727 */     return (int)(l ^ l >>> 32);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 770 */     return ((paramObject instanceof Double)) && (doubleToLongBits(((Double)paramObject).value) == doubleToLongBits(this.value));
/*     */   }
/*     */ 
/*     */   public static long doubleToLongBits(double paramDouble)
/*     */   {
/* 808 */     long l = doubleToRawLongBits(paramDouble);
/*     */ 
/* 811 */     if (((l & 0x0) == 9218868437227405312L) && ((l & 0xFFFFFFFF) != 0L))
/*     */     {
/* 814 */       l = 9221120237041090560L;
/* 815 */     }return l;
/*     */   }
/*     */ 
/*     */   public static native long doubleToRawLongBits(double paramDouble);
/*     */ 
/*     */   public static native double longBitsToDouble(long paramLong);
/*     */ 
/*     */   public int compareTo(Double paramDouble)
/*     */   {
/* 950 */     return compare(this.value, paramDouble.value);
/*     */   }
/*     */ 
/*     */   public static int compare(double paramDouble1, double paramDouble2)
/*     */   {
/* 972 */     if (paramDouble1 < paramDouble2)
/* 973 */       return -1;
/* 974 */     if (paramDouble1 > paramDouble2) {
/* 975 */       return 1;
/*     */     }
/*     */ 
/* 978 */     long l1 = doubleToLongBits(paramDouble1);
/* 979 */     long l2 = doubleToLongBits(paramDouble2);
/*     */ 
/* 981 */     return l1 < l2 ? -1 : l1 == l2 ? 0 : 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Double
 * JD-Core Version:    0.6.2
 */