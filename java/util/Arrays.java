/*      */ package java.util;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.security.AccessController;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ 
/*      */ public class Arrays
/*      */ {
/*      */   private static final int INSERTIONSORT_THRESHOLD = 7;
/*      */ 
/*      */   public static void sort(int[] paramArrayOfInt)
/*      */   {
/*   76 */     DualPivotQuicksort.sort(paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   public static void sort(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  100 */     rangeCheck(paramArrayOfInt.length, paramInt1, paramInt2);
/*  101 */     DualPivotQuicksort.sort(paramArrayOfInt, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(long[] paramArrayOfLong)
/*      */   {
/*  116 */     DualPivotQuicksort.sort(paramArrayOfLong);
/*      */   }
/*      */ 
/*      */   public static void sort(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*  140 */     rangeCheck(paramArrayOfLong.length, paramInt1, paramInt2);
/*  141 */     DualPivotQuicksort.sort(paramArrayOfLong, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(short[] paramArrayOfShort)
/*      */   {
/*  156 */     DualPivotQuicksort.sort(paramArrayOfShort);
/*      */   }
/*      */ 
/*      */   public static void sort(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  180 */     rangeCheck(paramArrayOfShort.length, paramInt1, paramInt2);
/*  181 */     DualPivotQuicksort.sort(paramArrayOfShort, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(char[] paramArrayOfChar)
/*      */   {
/*  196 */     DualPivotQuicksort.sort(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public static void sort(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  220 */     rangeCheck(paramArrayOfChar.length, paramInt1, paramInt2);
/*  221 */     DualPivotQuicksort.sort(paramArrayOfChar, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(byte[] paramArrayOfByte)
/*      */   {
/*  236 */     DualPivotQuicksort.sort(paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public static void sort(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  260 */     rangeCheck(paramArrayOfByte.length, paramInt1, paramInt2);
/*  261 */     DualPivotQuicksort.sort(paramArrayOfByte, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(float[] paramArrayOfFloat)
/*      */   {
/*  284 */     DualPivotQuicksort.sort(paramArrayOfFloat);
/*      */   }
/*      */ 
/*      */   public static void sort(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/*  316 */     rangeCheck(paramArrayOfFloat.length, paramInt1, paramInt2);
/*  317 */     DualPivotQuicksort.sort(paramArrayOfFloat, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(double[] paramArrayOfDouble)
/*      */   {
/*  340 */     DualPivotQuicksort.sort(paramArrayOfDouble);
/*      */   }
/*      */ 
/*      */   public static void sort(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/*  372 */     rangeCheck(paramArrayOfDouble.length, paramInt1, paramInt2);
/*  373 */     DualPivotQuicksort.sort(paramArrayOfDouble, paramInt1, paramInt2 - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(Object[] paramArrayOfObject)
/*      */   {
/*  469 */     if (LegacyMergeSort.userRequested)
/*  470 */       legacyMergeSort(paramArrayOfObject);
/*      */     else
/*  472 */       ComparableTimSort.sort(paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   private static void legacyMergeSort(Object[] paramArrayOfObject)
/*      */   {
/*  477 */     Object[] arrayOfObject = (Object[])paramArrayOfObject.clone();
/*  478 */     mergeSort(arrayOfObject, paramArrayOfObject, 0, paramArrayOfObject.length, 0);
/*      */   }
/*      */ 
/*      */   public static void sort(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*      */   {
/*  534 */     if (LegacyMergeSort.userRequested)
/*  535 */       legacyMergeSort(paramArrayOfObject, paramInt1, paramInt2);
/*      */     else
/*  537 */       ComparableTimSort.sort(paramArrayOfObject, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private static void legacyMergeSort(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*      */   {
/*  543 */     rangeCheck(paramArrayOfObject.length, paramInt1, paramInt2);
/*  544 */     Object[] arrayOfObject = copyOfRange(paramArrayOfObject, paramInt1, paramInt2);
/*  545 */     mergeSort(arrayOfObject, paramArrayOfObject, paramInt1, paramInt2, -paramInt1);
/*      */   }
/*      */ 
/*      */   private static void mergeSort(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  568 */     int i = paramInt2 - paramInt1;
/*      */ 
/*  571 */     if (i < 7) {
/*  572 */       for (j = paramInt1; j < paramInt2; j++)
/*  573 */         for (k = j; (k > paramInt1) && (((Comparable)paramArrayOfObject2[(k - 1)]).compareTo(paramArrayOfObject2[k]) > 0); 
/*  574 */           k--)
/*  575 */           swap(paramArrayOfObject2, k, k - 1);
/*  576 */       return;
/*      */     }
/*      */ 
/*  580 */     int j = paramInt1;
/*  581 */     int k = paramInt2;
/*  582 */     paramInt1 += paramInt3;
/*  583 */     paramInt2 += paramInt3;
/*  584 */     int m = paramInt1 + paramInt2 >>> 1;
/*  585 */     mergeSort(paramArrayOfObject2, paramArrayOfObject1, paramInt1, m, -paramInt3);
/*  586 */     mergeSort(paramArrayOfObject2, paramArrayOfObject1, m, paramInt2, -paramInt3);
/*      */ 
/*  590 */     if (((Comparable)paramArrayOfObject1[(m - 1)]).compareTo(paramArrayOfObject1[m]) <= 0) {
/*  591 */       System.arraycopy(paramArrayOfObject1, paramInt1, paramArrayOfObject2, j, i);
/*  592 */       return;
/*      */     }
/*      */ 
/*  596 */     int n = j; int i1 = paramInt1; for (int i2 = m; n < k; n++)
/*  597 */       if ((i2 >= paramInt2) || ((i1 < m) && (((Comparable)paramArrayOfObject1[i1]).compareTo(paramArrayOfObject1[i2]) <= 0)))
/*  598 */         paramArrayOfObject2[n] = paramArrayOfObject1[(i1++)];
/*      */       else
/*  600 */         paramArrayOfObject2[n] = paramArrayOfObject1[(i2++)];
/*      */   }
/*      */ 
/*      */   private static void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*      */   {
/*  608 */     Object localObject = paramArrayOfObject[paramInt1];
/*  609 */     paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
/*  610 */     paramArrayOfObject[paramInt2] = localObject;
/*      */   }
/*      */ 
/*      */   public static <T> void sort(T[] paramArrayOfT, Comparator<? super T> paramComparator)
/*      */   {
/*  656 */     if (LegacyMergeSort.userRequested)
/*  657 */       legacyMergeSort(paramArrayOfT, paramComparator);
/*      */     else
/*  659 */       TimSort.sort(paramArrayOfT, paramComparator);
/*      */   }
/*      */ 
/*      */   private static <T> void legacyMergeSort(T[] paramArrayOfT, Comparator<? super T> paramComparator)
/*      */   {
/*  664 */     Object[] arrayOfObject = (Object[])paramArrayOfT.clone();
/*  665 */     if (paramComparator == null)
/*  666 */       mergeSort(arrayOfObject, paramArrayOfT, 0, paramArrayOfT.length, 0);
/*      */     else
/*  668 */       mergeSort(arrayOfObject, paramArrayOfT, 0, paramArrayOfT.length, 0, paramComparator);
/*      */   }
/*      */ 
/*      */   public static <T> void sort(T[] paramArrayOfT, int paramInt1, int paramInt2, Comparator<? super T> paramComparator)
/*      */   {
/*  724 */     if (LegacyMergeSort.userRequested)
/*  725 */       legacyMergeSort(paramArrayOfT, paramInt1, paramInt2, paramComparator);
/*      */     else
/*  727 */       TimSort.sort(paramArrayOfT, paramInt1, paramInt2, paramComparator);
/*      */   }
/*      */ 
/*      */   private static <T> void legacyMergeSort(T[] paramArrayOfT, int paramInt1, int paramInt2, Comparator<? super T> paramComparator)
/*      */   {
/*  733 */     rangeCheck(paramArrayOfT.length, paramInt1, paramInt2);
/*  734 */     Object[] arrayOfObject = copyOfRange(paramArrayOfT, paramInt1, paramInt2);
/*  735 */     if (paramComparator == null)
/*  736 */       mergeSort(arrayOfObject, paramArrayOfT, paramInt1, paramInt2, -paramInt1);
/*      */     else
/*  738 */       mergeSort(arrayOfObject, paramArrayOfT, paramInt1, paramInt2, -paramInt1, paramComparator);
/*      */   }
/*      */ 
/*      */   private static void mergeSort(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int paramInt1, int paramInt2, int paramInt3, Comparator paramComparator)
/*      */   {
/*  753 */     int i = paramInt2 - paramInt1;
/*      */ 
/*  756 */     if (i < 7) {
/*  757 */       for (j = paramInt1; j < paramInt2; j++)
/*  758 */         for (k = j; (k > paramInt1) && (paramComparator.compare(paramArrayOfObject2[(k - 1)], paramArrayOfObject2[k]) > 0); k--)
/*  759 */           swap(paramArrayOfObject2, k, k - 1);
/*  760 */       return;
/*      */     }
/*      */ 
/*  764 */     int j = paramInt1;
/*  765 */     int k = paramInt2;
/*  766 */     paramInt1 += paramInt3;
/*  767 */     paramInt2 += paramInt3;
/*  768 */     int m = paramInt1 + paramInt2 >>> 1;
/*  769 */     mergeSort(paramArrayOfObject2, paramArrayOfObject1, paramInt1, m, -paramInt3, paramComparator);
/*  770 */     mergeSort(paramArrayOfObject2, paramArrayOfObject1, m, paramInt2, -paramInt3, paramComparator);
/*      */ 
/*  774 */     if (paramComparator.compare(paramArrayOfObject1[(m - 1)], paramArrayOfObject1[m]) <= 0) {
/*  775 */       System.arraycopy(paramArrayOfObject1, paramInt1, paramArrayOfObject2, j, i);
/*  776 */       return;
/*      */     }
/*      */ 
/*  780 */     int n = j; int i1 = paramInt1; for (int i2 = m; n < k; n++)
/*  781 */       if ((i2 >= paramInt2) || ((i1 < m) && (paramComparator.compare(paramArrayOfObject1[i1], paramArrayOfObject1[i2]) <= 0)))
/*  782 */         paramArrayOfObject2[n] = paramArrayOfObject1[(i1++)];
/*      */       else
/*  784 */         paramArrayOfObject2[n] = paramArrayOfObject1[(i2++)];
/*      */   }
/*      */ 
/*      */   private static void rangeCheck(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  793 */     if (paramInt2 > paramInt3) {
/*  794 */       throw new IllegalArgumentException("fromIndex(" + paramInt2 + ") > toIndex(" + paramInt3 + ")");
/*      */     }
/*      */ 
/*  797 */     if (paramInt2 < 0) {
/*  798 */       throw new ArrayIndexOutOfBoundsException(paramInt2);
/*      */     }
/*  800 */     if (paramInt3 > paramInt1)
/*  801 */       throw new ArrayIndexOutOfBoundsException(paramInt3);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(long[] paramArrayOfLong, long paramLong)
/*      */   {
/*  827 */     return binarySearch0(paramArrayOfLong, 0, paramArrayOfLong.length, paramLong);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(long[] paramArrayOfLong, int paramInt1, int paramInt2, long paramLong)
/*      */   {
/*  864 */     rangeCheck(paramArrayOfLong.length, paramInt1, paramInt2);
/*  865 */     return binarySearch0(paramArrayOfLong, paramInt1, paramInt2, paramLong);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(long[] paramArrayOfLong, int paramInt1, int paramInt2, long paramLong)
/*      */   {
/*  871 */     int i = paramInt1;
/*  872 */     int j = paramInt2 - 1;
/*      */ 
/*  874 */     while (i <= j) {
/*  875 */       int k = i + j >>> 1;
/*  876 */       long l = paramArrayOfLong[k];
/*      */ 
/*  878 */       if (l < paramLong)
/*  879 */         i = k + 1;
/*  880 */       else if (l > paramLong)
/*  881 */         j = k - 1;
/*      */       else
/*  883 */         return k;
/*      */     }
/*  885 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  908 */     return binarySearch0(paramArrayOfInt, 0, paramArrayOfInt.length, paramInt);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  945 */     rangeCheck(paramArrayOfInt.length, paramInt1, paramInt2);
/*  946 */     return binarySearch0(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  952 */     int i = paramInt1;
/*  953 */     int j = paramInt2 - 1;
/*      */ 
/*  955 */     while (i <= j) {
/*  956 */       int k = i + j >>> 1;
/*  957 */       int m = paramArrayOfInt[k];
/*      */ 
/*  959 */       if (m < paramInt3)
/*  960 */         i = k + 1;
/*  961 */       else if (m > paramInt3)
/*  962 */         j = k - 1;
/*      */       else
/*  964 */         return k;
/*      */     }
/*  966 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(short[] paramArrayOfShort, short paramShort)
/*      */   {
/*  989 */     return binarySearch0(paramArrayOfShort, 0, paramArrayOfShort.length, paramShort);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(short[] paramArrayOfShort, int paramInt1, int paramInt2, short paramShort)
/*      */   {
/* 1026 */     rangeCheck(paramArrayOfShort.length, paramInt1, paramInt2);
/* 1027 */     return binarySearch0(paramArrayOfShort, paramInt1, paramInt2, paramShort);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(short[] paramArrayOfShort, int paramInt1, int paramInt2, short paramShort)
/*      */   {
/* 1033 */     int i = paramInt1;
/* 1034 */     int j = paramInt2 - 1;
/*      */ 
/* 1036 */     while (i <= j) {
/* 1037 */       int k = i + j >>> 1;
/* 1038 */       short s = paramArrayOfShort[k];
/*      */ 
/* 1040 */       if (s < paramShort)
/* 1041 */         i = k + 1;
/* 1042 */       else if (s > paramShort)
/* 1043 */         j = k - 1;
/*      */       else
/* 1045 */         return k;
/*      */     }
/* 1047 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(char[] paramArrayOfChar, char paramChar)
/*      */   {
/* 1070 */     return binarySearch0(paramArrayOfChar, 0, paramArrayOfChar.length, paramChar);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(char[] paramArrayOfChar, int paramInt1, int paramInt2, char paramChar)
/*      */   {
/* 1107 */     rangeCheck(paramArrayOfChar.length, paramInt1, paramInt2);
/* 1108 */     return binarySearch0(paramArrayOfChar, paramInt1, paramInt2, paramChar);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(char[] paramArrayOfChar, int paramInt1, int paramInt2, char paramChar)
/*      */   {
/* 1114 */     int i = paramInt1;
/* 1115 */     int j = paramInt2 - 1;
/*      */ 
/* 1117 */     while (i <= j) {
/* 1118 */       int k = i + j >>> 1;
/* 1119 */       char c = paramArrayOfChar[k];
/*      */ 
/* 1121 */       if (c < paramChar)
/* 1122 */         i = k + 1;
/* 1123 */       else if (c > paramChar)
/* 1124 */         j = k - 1;
/*      */       else
/* 1126 */         return k;
/*      */     }
/* 1128 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(byte[] paramArrayOfByte, byte paramByte)
/*      */   {
/* 1151 */     return binarySearch0(paramArrayOfByte, 0, paramArrayOfByte.length, paramByte);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(byte[] paramArrayOfByte, int paramInt1, int paramInt2, byte paramByte)
/*      */   {
/* 1188 */     rangeCheck(paramArrayOfByte.length, paramInt1, paramInt2);
/* 1189 */     return binarySearch0(paramArrayOfByte, paramInt1, paramInt2, paramByte);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(byte[] paramArrayOfByte, int paramInt1, int paramInt2, byte paramByte)
/*      */   {
/* 1195 */     int i = paramInt1;
/* 1196 */     int j = paramInt2 - 1;
/*      */ 
/* 1198 */     while (i <= j) {
/* 1199 */       int k = i + j >>> 1;
/* 1200 */       byte b = paramArrayOfByte[k];
/*      */ 
/* 1202 */       if (b < paramByte)
/* 1203 */         i = k + 1;
/* 1204 */       else if (b > paramByte)
/* 1205 */         j = k - 1;
/*      */       else
/* 1207 */         return k;
/*      */     }
/* 1209 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(double[] paramArrayOfDouble, double paramDouble)
/*      */   {
/* 1233 */     return binarySearch0(paramArrayOfDouble, 0, paramArrayOfDouble.length, paramDouble);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(double[] paramArrayOfDouble, int paramInt1, int paramInt2, double paramDouble)
/*      */   {
/* 1271 */     rangeCheck(paramArrayOfDouble.length, paramInt1, paramInt2);
/* 1272 */     return binarySearch0(paramArrayOfDouble, paramInt1, paramInt2, paramDouble);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(double[] paramArrayOfDouble, int paramInt1, int paramInt2, double paramDouble)
/*      */   {
/* 1278 */     int i = paramInt1;
/* 1279 */     int j = paramInt2 - 1;
/*      */ 
/* 1281 */     while (i <= j) {
/* 1282 */       int k = i + j >>> 1;
/* 1283 */       double d = paramArrayOfDouble[k];
/*      */ 
/* 1285 */       if (d < paramDouble) {
/* 1286 */         i = k + 1;
/* 1287 */       } else if (d > paramDouble) {
/* 1288 */         j = k - 1;
/*      */       } else {
/* 1290 */         long l1 = Double.doubleToLongBits(d);
/* 1291 */         long l2 = Double.doubleToLongBits(paramDouble);
/* 1292 */         if (l1 == l2)
/* 1293 */           return k;
/* 1294 */         if (l1 < l2)
/* 1295 */           i = k + 1;
/*      */         else
/* 1297 */           j = k - 1;
/*      */       }
/*      */     }
/* 1300 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(float[] paramArrayOfFloat, float paramFloat)
/*      */   {
/* 1324 */     return binarySearch0(paramArrayOfFloat, 0, paramArrayOfFloat.length, paramFloat);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(float[] paramArrayOfFloat, int paramInt1, int paramInt2, float paramFloat)
/*      */   {
/* 1362 */     rangeCheck(paramArrayOfFloat.length, paramInt1, paramInt2);
/* 1363 */     return binarySearch0(paramArrayOfFloat, paramInt1, paramInt2, paramFloat);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(float[] paramArrayOfFloat, int paramInt1, int paramInt2, float paramFloat)
/*      */   {
/* 1369 */     int i = paramInt1;
/* 1370 */     int j = paramInt2 - 1;
/*      */ 
/* 1372 */     while (i <= j) {
/* 1373 */       int k = i + j >>> 1;
/* 1374 */       float f = paramArrayOfFloat[k];
/*      */ 
/* 1376 */       if (f < paramFloat) {
/* 1377 */         i = k + 1;
/* 1378 */       } else if (f > paramFloat) {
/* 1379 */         j = k - 1;
/*      */       } else {
/* 1381 */         int m = Float.floatToIntBits(f);
/* 1382 */         int n = Float.floatToIntBits(paramFloat);
/* 1383 */         if (m == n)
/* 1384 */           return k;
/* 1385 */         if (m < n)
/* 1386 */           i = k + 1;
/*      */         else
/* 1388 */           j = k - 1;
/*      */       }
/*      */     }
/* 1391 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(Object[] paramArrayOfObject, Object paramObject)
/*      */   {
/* 1423 */     return binarySearch0(paramArrayOfObject, 0, paramArrayOfObject.length, paramObject);
/*      */   }
/*      */ 
/*      */   public static int binarySearch(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/* 1468 */     rangeCheck(paramArrayOfObject.length, paramInt1, paramInt2);
/* 1469 */     return binarySearch0(paramArrayOfObject, paramInt1, paramInt2, paramObject);
/*      */   }
/*      */ 
/*      */   private static int binarySearch0(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/* 1475 */     int i = paramInt1;
/* 1476 */     int j = paramInt2 - 1;
/*      */ 
/* 1478 */     while (i <= j) {
/* 1479 */       int k = i + j >>> 1;
/* 1480 */       Comparable localComparable = (Comparable)paramArrayOfObject[k];
/* 1481 */       int m = localComparable.compareTo(paramObject);
/*      */ 
/* 1483 */       if (m < 0)
/* 1484 */         i = k + 1;
/* 1485 */       else if (m > 0)
/* 1486 */         j = k - 1;
/*      */       else
/* 1488 */         return k;
/*      */     }
/* 1490 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static <T> int binarySearch(T[] paramArrayOfT, T paramT, Comparator<? super T> paramComparator)
/*      */   {
/* 1523 */     return binarySearch0(paramArrayOfT, 0, paramArrayOfT.length, paramT, paramComparator);
/*      */   }
/*      */ 
/*      */   public static <T> int binarySearch(T[] paramArrayOfT, int paramInt1, int paramInt2, T paramT, Comparator<? super T> paramComparator)
/*      */   {
/* 1569 */     rangeCheck(paramArrayOfT.length, paramInt1, paramInt2);
/* 1570 */     return binarySearch0(paramArrayOfT, paramInt1, paramInt2, paramT, paramComparator);
/*      */   }
/*      */ 
/*      */   private static <T> int binarySearch0(T[] paramArrayOfT, int paramInt1, int paramInt2, T paramT, Comparator<? super T> paramComparator)
/*      */   {
/* 1576 */     if (paramComparator == null) {
/* 1577 */       return binarySearch0(paramArrayOfT, paramInt1, paramInt2, paramT);
/*      */     }
/* 1579 */     int i = paramInt1;
/* 1580 */     int j = paramInt2 - 1;
/*      */ 
/* 1582 */     while (i <= j) {
/* 1583 */       int k = i + j >>> 1;
/* 1584 */       T ? = paramArrayOfT[k];
/* 1585 */       int m = paramComparator.compare(?, paramT);
/* 1586 */       if (m < 0)
/* 1587 */         i = k + 1;
/* 1588 */       else if (m > 0)
/* 1589 */         j = k - 1;
/*      */       else
/* 1591 */         return k;
/*      */     }
/* 1593 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static boolean equals(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
/*      */   {
/* 1611 */     if (paramArrayOfLong1 == paramArrayOfLong2)
/* 1612 */       return true;
/* 1613 */     if ((paramArrayOfLong1 == null) || (paramArrayOfLong2 == null)) {
/* 1614 */       return false;
/*      */     }
/* 1616 */     int i = paramArrayOfLong1.length;
/* 1617 */     if (paramArrayOfLong2.length != i) {
/* 1618 */       return false;
/*      */     }
/* 1620 */     for (int j = 0; j < i; j++) {
/* 1621 */       if (paramArrayOfLong1[j] != paramArrayOfLong2[j])
/* 1622 */         return false;
/*      */     }
/* 1624 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/* 1640 */     if (paramArrayOfInt1 == paramArrayOfInt2)
/* 1641 */       return true;
/* 1642 */     if ((paramArrayOfInt1 == null) || (paramArrayOfInt2 == null)) {
/* 1643 */       return false;
/*      */     }
/* 1645 */     int i = paramArrayOfInt1.length;
/* 1646 */     if (paramArrayOfInt2.length != i) {
/* 1647 */       return false;
/*      */     }
/* 1649 */     for (int j = 0; j < i; j++) {
/* 1650 */       if (paramArrayOfInt1[j] != paramArrayOfInt2[j])
/* 1651 */         return false;
/*      */     }
/* 1653 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*      */   {
/* 1669 */     if (paramArrayOfShort1 == paramArrayOfShort2)
/* 1670 */       return true;
/* 1671 */     if ((paramArrayOfShort1 == null) || (paramArrayOfShort2 == null)) {
/* 1672 */       return false;
/*      */     }
/* 1674 */     int i = paramArrayOfShort1.length;
/* 1675 */     if (paramArrayOfShort2.length != i) {
/* 1676 */       return false;
/*      */     }
/* 1678 */     for (int j = 0; j < i; j++) {
/* 1679 */       if (paramArrayOfShort1[j] != paramArrayOfShort2[j])
/* 1680 */         return false;
/*      */     }
/* 1682 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(char[] paramArrayOfChar1, char[] paramArrayOfChar2)
/*      */   {
/* 1698 */     if (paramArrayOfChar1 == paramArrayOfChar2)
/* 1699 */       return true;
/* 1700 */     if ((paramArrayOfChar1 == null) || (paramArrayOfChar2 == null)) {
/* 1701 */       return false;
/*      */     }
/* 1703 */     int i = paramArrayOfChar1.length;
/* 1704 */     if (paramArrayOfChar2.length != i) {
/* 1705 */       return false;
/*      */     }
/* 1707 */     for (int j = 0; j < i; j++) {
/* 1708 */       if (paramArrayOfChar1[j] != paramArrayOfChar2[j])
/* 1709 */         return false;
/*      */     }
/* 1711 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */   {
/* 1727 */     if (paramArrayOfByte1 == paramArrayOfByte2)
/* 1728 */       return true;
/* 1729 */     if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null)) {
/* 1730 */       return false;
/*      */     }
/* 1732 */     int i = paramArrayOfByte1.length;
/* 1733 */     if (paramArrayOfByte2.length != i) {
/* 1734 */       return false;
/*      */     }
/* 1736 */     for (int j = 0; j < i; j++) {
/* 1737 */       if (paramArrayOfByte1[j] != paramArrayOfByte2[j])
/* 1738 */         return false;
/*      */     }
/* 1740 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
/*      */   {
/* 1756 */     if (paramArrayOfBoolean1 == paramArrayOfBoolean2)
/* 1757 */       return true;
/* 1758 */     if ((paramArrayOfBoolean1 == null) || (paramArrayOfBoolean2 == null)) {
/* 1759 */       return false;
/*      */     }
/* 1761 */     int i = paramArrayOfBoolean1.length;
/* 1762 */     if (paramArrayOfBoolean2.length != i) {
/* 1763 */       return false;
/*      */     }
/* 1765 */     for (int j = 0; j < i; j++) {
/* 1766 */       if (paramArrayOfBoolean1[j] != paramArrayOfBoolean2[j])
/* 1767 */         return false;
/*      */     }
/* 1769 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
/*      */   {
/* 1791 */     if (paramArrayOfDouble1 == paramArrayOfDouble2)
/* 1792 */       return true;
/* 1793 */     if ((paramArrayOfDouble1 == null) || (paramArrayOfDouble2 == null)) {
/* 1794 */       return false;
/*      */     }
/* 1796 */     int i = paramArrayOfDouble1.length;
/* 1797 */     if (paramArrayOfDouble2.length != i) {
/* 1798 */       return false;
/*      */     }
/* 1800 */     for (int j = 0; j < i; j++) {
/* 1801 */       if (Double.doubleToLongBits(paramArrayOfDouble1[j]) != Double.doubleToLongBits(paramArrayOfDouble2[j]))
/* 1802 */         return false;
/*      */     }
/* 1804 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*      */   {
/* 1826 */     if (paramArrayOfFloat1 == paramArrayOfFloat2)
/* 1827 */       return true;
/* 1828 */     if ((paramArrayOfFloat1 == null) || (paramArrayOfFloat2 == null)) {
/* 1829 */       return false;
/*      */     }
/* 1831 */     int i = paramArrayOfFloat1.length;
/* 1832 */     if (paramArrayOfFloat2.length != i) {
/* 1833 */       return false;
/*      */     }
/* 1835 */     for (int j = 0; j < i; j++) {
/* 1836 */       if (Float.floatToIntBits(paramArrayOfFloat1[j]) != Float.floatToIntBits(paramArrayOfFloat2[j]))
/* 1837 */         return false;
/*      */     }
/* 1839 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean equals(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
/*      */   {
/* 1857 */     if (paramArrayOfObject1 == paramArrayOfObject2)
/* 1858 */       return true;
/* 1859 */     if ((paramArrayOfObject1 == null) || (paramArrayOfObject2 == null)) {
/* 1860 */       return false;
/*      */     }
/* 1862 */     int i = paramArrayOfObject1.length;
/* 1863 */     if (paramArrayOfObject2.length != i) {
/* 1864 */       return false;
/*      */     }
/* 1866 */     for (int j = 0; j < i; j++) {
/* 1867 */       Object localObject1 = paramArrayOfObject1[j];
/* 1868 */       Object localObject2 = paramArrayOfObject2[j];
/* 1869 */       if (localObject1 == null ? localObject2 != null : !localObject1.equals(localObject2)) {
/* 1870 */         return false;
/*      */       }
/*      */     }
/* 1873 */     return true;
/*      */   }
/*      */ 
/*      */   public static void fill(long[] paramArrayOfLong, long paramLong)
/*      */   {
/* 1886 */     int i = 0; for (int j = paramArrayOfLong.length; i < j; i++)
/* 1887 */       paramArrayOfLong[i] = paramLong;
/*      */   }
/*      */ 
/*      */   public static void fill(long[] paramArrayOfLong, int paramInt1, int paramInt2, long paramLong)
/*      */   {
/* 1908 */     rangeCheck(paramArrayOfLong.length, paramInt1, paramInt2);
/* 1909 */     for (int i = paramInt1; i < paramInt2; i++)
/* 1910 */       paramArrayOfLong[i] = paramLong;
/*      */   }
/*      */ 
/*      */   public static void fill(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1921 */     int i = 0; for (int j = paramArrayOfInt.length; i < j; i++)
/* 1922 */       paramArrayOfInt[i] = paramInt;
/*      */   }
/*      */ 
/*      */   public static void fill(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1943 */     rangeCheck(paramArrayOfInt.length, paramInt1, paramInt2);
/* 1944 */     for (int i = paramInt1; i < paramInt2; i++)
/* 1945 */       paramArrayOfInt[i] = paramInt3;
/*      */   }
/*      */ 
/*      */   public static void fill(short[] paramArrayOfShort, short paramShort)
/*      */   {
/* 1956 */     int i = 0; for (int j = paramArrayOfShort.length; i < j; i++)
/* 1957 */       paramArrayOfShort[i] = paramShort;
/*      */   }
/*      */ 
/*      */   public static void fill(short[] paramArrayOfShort, int paramInt1, int paramInt2, short paramShort)
/*      */   {
/* 1978 */     rangeCheck(paramArrayOfShort.length, paramInt1, paramInt2);
/* 1979 */     for (int i = paramInt1; i < paramInt2; i++)
/* 1980 */       paramArrayOfShort[i] = paramShort;
/*      */   }
/*      */ 
/*      */   public static void fill(char[] paramArrayOfChar, char paramChar)
/*      */   {
/* 1991 */     int i = 0; for (int j = paramArrayOfChar.length; i < j; i++)
/* 1992 */       paramArrayOfChar[i] = paramChar;
/*      */   }
/*      */ 
/*      */   public static void fill(char[] paramArrayOfChar, int paramInt1, int paramInt2, char paramChar)
/*      */   {
/* 2013 */     rangeCheck(paramArrayOfChar.length, paramInt1, paramInt2);
/* 2014 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2015 */       paramArrayOfChar[i] = paramChar;
/*      */   }
/*      */ 
/*      */   public static void fill(byte[] paramArrayOfByte, byte paramByte)
/*      */   {
/* 2026 */     int i = 0; for (int j = paramArrayOfByte.length; i < j; i++)
/* 2027 */       paramArrayOfByte[i] = paramByte;
/*      */   }
/*      */ 
/*      */   public static void fill(byte[] paramArrayOfByte, int paramInt1, int paramInt2, byte paramByte)
/*      */   {
/* 2048 */     rangeCheck(paramArrayOfByte.length, paramInt1, paramInt2);
/* 2049 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2050 */       paramArrayOfByte[i] = paramByte;
/*      */   }
/*      */ 
/*      */   public static void fill(boolean[] paramArrayOfBoolean, boolean paramBoolean)
/*      */   {
/* 2061 */     int i = 0; for (int j = paramArrayOfBoolean.length; i < j; i++)
/* 2062 */       paramArrayOfBoolean[i] = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static void fill(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2084 */     rangeCheck(paramArrayOfBoolean.length, paramInt1, paramInt2);
/* 2085 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2086 */       paramArrayOfBoolean[i] = paramBoolean;
/*      */   }
/*      */ 
/*      */   public static void fill(double[] paramArrayOfDouble, double paramDouble)
/*      */   {
/* 2097 */     int i = 0; for (int j = paramArrayOfDouble.length; i < j; i++)
/* 2098 */       paramArrayOfDouble[i] = paramDouble;
/*      */   }
/*      */ 
/*      */   public static void fill(double[] paramArrayOfDouble, int paramInt1, int paramInt2, double paramDouble)
/*      */   {
/* 2119 */     rangeCheck(paramArrayOfDouble.length, paramInt1, paramInt2);
/* 2120 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2121 */       paramArrayOfDouble[i] = paramDouble;
/*      */   }
/*      */ 
/*      */   public static void fill(float[] paramArrayOfFloat, float paramFloat)
/*      */   {
/* 2132 */     int i = 0; for (int j = paramArrayOfFloat.length; i < j; i++)
/* 2133 */       paramArrayOfFloat[i] = paramFloat;
/*      */   }
/*      */ 
/*      */   public static void fill(float[] paramArrayOfFloat, int paramInt1, int paramInt2, float paramFloat)
/*      */   {
/* 2154 */     rangeCheck(paramArrayOfFloat.length, paramInt1, paramInt2);
/* 2155 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2156 */       paramArrayOfFloat[i] = paramFloat;
/*      */   }
/*      */ 
/*      */   public static void fill(Object[] paramArrayOfObject, Object paramObject)
/*      */   {
/* 2169 */     int i = 0; for (int j = paramArrayOfObject.length; i < j; i++)
/* 2170 */       paramArrayOfObject[i] = paramObject;
/*      */   }
/*      */ 
/*      */   public static void fill(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/* 2193 */     rangeCheck(paramArrayOfObject.length, paramInt1, paramInt2);
/* 2194 */     for (int i = paramInt1; i < paramInt2; i++)
/* 2195 */       paramArrayOfObject[i] = paramObject;
/*      */   }
/*      */ 
/*      */   public static <T> T[] copyOf(T[] paramArrayOfT, int paramInt)
/*      */   {
/* 2219 */     return (Object[])copyOf(paramArrayOfT, paramInt, paramArrayOfT.getClass());
/*      */   }
/*      */ 
/*      */   public static <T, U> T[] copyOf(U[] paramArrayOfU, int paramInt, Class<? extends T[]> paramClass)
/*      */   {
/* 2245 */     Object[] arrayOfObject = paramClass == [Ljava.lang.Object.class ? (Object[])new Object[paramInt] : (Object[])Array.newInstance(paramClass.getComponentType(), paramInt);
/*      */ 
/* 2248 */     System.arraycopy(paramArrayOfU, 0, arrayOfObject, 0, Math.min(paramArrayOfU.length, paramInt));
/*      */ 
/* 2250 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public static byte[] copyOf(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 2271 */     byte[] arrayOfByte = new byte[paramInt];
/* 2272 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, Math.min(paramArrayOfByte.length, paramInt));
/*      */ 
/* 2274 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public static short[] copyOf(short[] paramArrayOfShort, int paramInt)
/*      */   {
/* 2295 */     short[] arrayOfShort = new short[paramInt];
/* 2296 */     System.arraycopy(paramArrayOfShort, 0, arrayOfShort, 0, Math.min(paramArrayOfShort.length, paramInt));
/*      */ 
/* 2298 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   public static int[] copyOf(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 2319 */     int[] arrayOfInt = new int[paramInt];
/* 2320 */     System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, Math.min(paramArrayOfInt.length, paramInt));
/*      */ 
/* 2322 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public static long[] copyOf(long[] paramArrayOfLong, int paramInt)
/*      */   {
/* 2343 */     long[] arrayOfLong = new long[paramInt];
/* 2344 */     System.arraycopy(paramArrayOfLong, 0, arrayOfLong, 0, Math.min(paramArrayOfLong.length, paramInt));
/*      */ 
/* 2346 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   public static char[] copyOf(char[] paramArrayOfChar, int paramInt)
/*      */   {
/* 2367 */     char[] arrayOfChar = new char[paramInt];
/* 2368 */     System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, Math.min(paramArrayOfChar.length, paramInt));
/*      */ 
/* 2370 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   public static float[] copyOf(float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 2391 */     float[] arrayOfFloat = new float[paramInt];
/* 2392 */     System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, Math.min(paramArrayOfFloat.length, paramInt));
/*      */ 
/* 2394 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public static double[] copyOf(double[] paramArrayOfDouble, int paramInt)
/*      */   {
/* 2415 */     double[] arrayOfDouble = new double[paramInt];
/* 2416 */     System.arraycopy(paramArrayOfDouble, 0, arrayOfDouble, 0, Math.min(paramArrayOfDouble.length, paramInt));
/*      */ 
/* 2418 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public static boolean[] copyOf(boolean[] paramArrayOfBoolean, int paramInt)
/*      */   {
/* 2439 */     boolean[] arrayOfBoolean = new boolean[paramInt];
/* 2440 */     System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, Math.min(paramArrayOfBoolean.length, paramInt));
/*      */ 
/* 2442 */     return arrayOfBoolean;
/*      */   }
/*      */ 
/*      */   public static <T> T[] copyOfRange(T[] paramArrayOfT, int paramInt1, int paramInt2)
/*      */   {
/* 2474 */     return copyOfRange(paramArrayOfT, paramInt1, paramInt2, paramArrayOfT.getClass());
/*      */   }
/*      */ 
/*      */   public static <T, U> T[] copyOfRange(U[] paramArrayOfU, int paramInt1, int paramInt2, Class<? extends T[]> paramClass)
/*      */   {
/* 2509 */     int i = paramInt2 - paramInt1;
/* 2510 */     if (i < 0)
/* 2511 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2512 */     Object[] arrayOfObject = paramClass == [Ljava.lang.Object.class ? (Object[])new Object[i] : (Object[])Array.newInstance(paramClass.getComponentType(), i);
/*      */ 
/* 2515 */     System.arraycopy(paramArrayOfU, paramInt1, arrayOfObject, 0, Math.min(paramArrayOfU.length - paramInt1, i));
/*      */ 
/* 2517 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public static byte[] copyOfRange(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/* 2547 */     int i = paramInt2 - paramInt1;
/* 2548 */     if (i < 0)
/* 2549 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2550 */     byte[] arrayOfByte = new byte[i];
/* 2551 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, Math.min(paramArrayOfByte.length - paramInt1, i));
/*      */ 
/* 2553 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public static short[] copyOfRange(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/* 2583 */     int i = paramInt2 - paramInt1;
/* 2584 */     if (i < 0)
/* 2585 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2586 */     short[] arrayOfShort = new short[i];
/* 2587 */     System.arraycopy(paramArrayOfShort, paramInt1, arrayOfShort, 0, Math.min(paramArrayOfShort.length - paramInt1, i));
/*      */ 
/* 2589 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   public static int[] copyOfRange(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 2619 */     int i = paramInt2 - paramInt1;
/* 2620 */     if (i < 0)
/* 2621 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2622 */     int[] arrayOfInt = new int[i];
/* 2623 */     System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, 0, Math.min(paramArrayOfInt.length - paramInt1, i));
/*      */ 
/* 2625 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public static long[] copyOfRange(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/* 2655 */     int i = paramInt2 - paramInt1;
/* 2656 */     if (i < 0)
/* 2657 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2658 */     long[] arrayOfLong = new long[i];
/* 2659 */     System.arraycopy(paramArrayOfLong, paramInt1, arrayOfLong, 0, Math.min(paramArrayOfLong.length - paramInt1, i));
/*      */ 
/* 2661 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   public static char[] copyOfRange(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2691 */     int i = paramInt2 - paramInt1;
/* 2692 */     if (i < 0)
/* 2693 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2694 */     char[] arrayOfChar = new char[i];
/* 2695 */     System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar, 0, Math.min(paramArrayOfChar.length - paramInt1, i));
/*      */ 
/* 2697 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   public static float[] copyOfRange(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/* 2727 */     int i = paramInt2 - paramInt1;
/* 2728 */     if (i < 0)
/* 2729 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2730 */     float[] arrayOfFloat = new float[i];
/* 2731 */     System.arraycopy(paramArrayOfFloat, paramInt1, arrayOfFloat, 0, Math.min(paramArrayOfFloat.length - paramInt1, i));
/*      */ 
/* 2733 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public static double[] copyOfRange(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/* 2763 */     int i = paramInt2 - paramInt1;
/* 2764 */     if (i < 0)
/* 2765 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2766 */     double[] arrayOfDouble = new double[i];
/* 2767 */     System.arraycopy(paramArrayOfDouble, paramInt1, arrayOfDouble, 0, Math.min(paramArrayOfDouble.length - paramInt1, i));
/*      */ 
/* 2769 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public static boolean[] copyOfRange(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*      */   {
/* 2799 */     int i = paramInt2 - paramInt1;
/* 2800 */     if (i < 0)
/* 2801 */       throw new IllegalArgumentException(paramInt1 + " > " + paramInt2);
/* 2802 */     boolean[] arrayOfBoolean = new boolean[i];
/* 2803 */     System.arraycopy(paramArrayOfBoolean, paramInt1, arrayOfBoolean, 0, Math.min(paramArrayOfBoolean.length - paramInt1, i));
/*      */ 
/* 2805 */     return arrayOfBoolean;
/*      */   }
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <T> List<T> asList(T[] paramArrayOfT)
/*      */   {
/* 2828 */     return new ArrayList(paramArrayOfT);
/*      */   }
/*      */ 
/*      */   public static int hashCode(long[] paramArrayOfLong)
/*      */   {
/* 2910 */     if (paramArrayOfLong == null) {
/* 2911 */       return 0;
/*      */     }
/* 2913 */     int i = 1;
/* 2914 */     for (long l : paramArrayOfLong) {
/* 2915 */       int m = (int)(l ^ l >>> 32);
/* 2916 */       i = 31 * i + m;
/*      */     }
/*      */ 
/* 2919 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(int[] paramArrayOfInt)
/*      */   {
/* 2939 */     if (paramArrayOfInt == null) {
/* 2940 */       return 0;
/*      */     }
/* 2942 */     int i = 1;
/* 2943 */     for (int m : paramArrayOfInt) {
/* 2944 */       i = 31 * i + m;
/*      */     }
/* 2946 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(short[] paramArrayOfShort)
/*      */   {
/* 2966 */     if (paramArrayOfShort == null) {
/* 2967 */       return 0;
/*      */     }
/* 2969 */     int i = 1;
/* 2970 */     for (int m : paramArrayOfShort) {
/* 2971 */       i = 31 * i + m;
/*      */     }
/* 2973 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(char[] paramArrayOfChar)
/*      */   {
/* 2993 */     if (paramArrayOfChar == null) {
/* 2994 */       return 0;
/*      */     }
/* 2996 */     int i = 1;
/* 2997 */     for (int m : paramArrayOfChar) {
/* 2998 */       i = 31 * i + m;
/*      */     }
/* 3000 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(byte[] paramArrayOfByte)
/*      */   {
/* 3020 */     if (paramArrayOfByte == null) {
/* 3021 */       return 0;
/*      */     }
/* 3023 */     int i = 1;
/* 3024 */     for (int m : paramArrayOfByte) {
/* 3025 */       i = 31 * i + m;
/*      */     }
/* 3027 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(boolean[] paramArrayOfBoolean)
/*      */   {
/* 3047 */     if (paramArrayOfBoolean == null) {
/* 3048 */       return 0;
/*      */     }
/* 3050 */     int i = 1;
/* 3051 */     for (int m : paramArrayOfBoolean) {
/* 3052 */       i = 31 * i + (m != 0 ? 1231 : 1237);
/*      */     }
/* 3054 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(float[] paramArrayOfFloat)
/*      */   {
/* 3074 */     if (paramArrayOfFloat == null) {
/* 3075 */       return 0;
/*      */     }
/* 3077 */     int i = 1;
/* 3078 */     for (float f : paramArrayOfFloat) {
/* 3079 */       i = 31 * i + Float.floatToIntBits(f);
/*      */     }
/* 3081 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(double[] paramArrayOfDouble)
/*      */   {
/* 3101 */     if (paramArrayOfDouble == null) {
/* 3102 */       return 0;
/*      */     }
/* 3104 */     int i = 1;
/* 3105 */     for (double d : paramArrayOfDouble) {
/* 3106 */       long l = Double.doubleToLongBits(d);
/* 3107 */       i = 31 * i + (int)(l ^ l >>> 32);
/*      */     }
/* 3109 */     return i;
/*      */   }
/*      */ 
/*      */   public static int hashCode(Object[] paramArrayOfObject)
/*      */   {
/* 3134 */     if (paramArrayOfObject == null) {
/* 3135 */       return 0;
/*      */     }
/* 3137 */     int i = 1;
/*      */ 
/* 3139 */     for (Object localObject : paramArrayOfObject) {
/* 3140 */       i = 31 * i + (localObject == null ? 0 : localObject.hashCode());
/*      */     }
/* 3142 */     return i;
/*      */   }
/*      */ 
/*      */   public static int deepHashCode(Object[] paramArrayOfObject)
/*      */   {
/* 3175 */     if (paramArrayOfObject == null) {
/* 3176 */       return 0;
/*      */     }
/* 3178 */     int i = 1;
/*      */ 
/* 3180 */     for (Object localObject : paramArrayOfObject) {
/* 3181 */       int m = 0;
/* 3182 */       if ((localObject instanceof Object[]))
/* 3183 */         m = deepHashCode((Object[])localObject);
/* 3184 */       else if ((localObject instanceof byte[]))
/* 3185 */         m = hashCode((byte[])localObject);
/* 3186 */       else if ((localObject instanceof short[]))
/* 3187 */         m = hashCode((short[])localObject);
/* 3188 */       else if ((localObject instanceof int[]))
/* 3189 */         m = hashCode((int[])localObject);
/* 3190 */       else if ((localObject instanceof long[]))
/* 3191 */         m = hashCode((long[])localObject);
/* 3192 */       else if ((localObject instanceof char[]))
/* 3193 */         m = hashCode((char[])localObject);
/* 3194 */       else if ((localObject instanceof float[]))
/* 3195 */         m = hashCode((float[])localObject);
/* 3196 */       else if ((localObject instanceof double[]))
/* 3197 */         m = hashCode((double[])localObject);
/* 3198 */       else if ((localObject instanceof boolean[]))
/* 3199 */         m = hashCode((boolean[])localObject);
/* 3200 */       else if (localObject != null) {
/* 3201 */         m = localObject.hashCode();
/*      */       }
/* 3203 */       i = 31 * i + m;
/*      */     }
/*      */ 
/* 3206 */     return i;
/*      */   }
/*      */ 
/*      */   public static boolean deepEquals(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
/*      */   {
/* 3245 */     if (paramArrayOfObject1 == paramArrayOfObject2)
/* 3246 */       return true;
/* 3247 */     if ((paramArrayOfObject1 == null) || (paramArrayOfObject2 == null))
/* 3248 */       return false;
/* 3249 */     int i = paramArrayOfObject1.length;
/* 3250 */     if (paramArrayOfObject2.length != i) {
/* 3251 */       return false;
/*      */     }
/* 3253 */     for (int j = 0; j < i; j++) {
/* 3254 */       Object localObject1 = paramArrayOfObject1[j];
/* 3255 */       Object localObject2 = paramArrayOfObject2[j];
/*      */ 
/* 3257 */       if (localObject1 != localObject2)
/*      */       {
/* 3259 */         if (localObject1 == null) {
/* 3260 */           return false;
/*      */         }
/*      */ 
/* 3263 */         boolean bool = deepEquals0(localObject1, localObject2);
/*      */ 
/* 3265 */         if (!bool)
/* 3266 */           return false; 
/*      */       }
/*      */     }
/* 3268 */     return true;
/*      */   }
/*      */ 
/*      */   static boolean deepEquals0(Object paramObject1, Object paramObject2) {
/* 3272 */     assert (paramObject1 != null);
/*      */     boolean bool;
/* 3274 */     if (((paramObject1 instanceof Object[])) && ((paramObject2 instanceof Object[])))
/* 3275 */       bool = deepEquals((Object[])paramObject1, (Object[])paramObject2);
/* 3276 */     else if (((paramObject1 instanceof byte[])) && ((paramObject2 instanceof byte[])))
/* 3277 */       bool = equals((byte[])paramObject1, (byte[])paramObject2);
/* 3278 */     else if (((paramObject1 instanceof short[])) && ((paramObject2 instanceof short[])))
/* 3279 */       bool = equals((short[])paramObject1, (short[])paramObject2);
/* 3280 */     else if (((paramObject1 instanceof int[])) && ((paramObject2 instanceof int[])))
/* 3281 */       bool = equals((int[])paramObject1, (int[])paramObject2);
/* 3282 */     else if (((paramObject1 instanceof long[])) && ((paramObject2 instanceof long[])))
/* 3283 */       bool = equals((long[])paramObject1, (long[])paramObject2);
/* 3284 */     else if (((paramObject1 instanceof char[])) && ((paramObject2 instanceof char[])))
/* 3285 */       bool = equals((char[])paramObject1, (char[])paramObject2);
/* 3286 */     else if (((paramObject1 instanceof float[])) && ((paramObject2 instanceof float[])))
/* 3287 */       bool = equals((float[])paramObject1, (float[])paramObject2);
/* 3288 */     else if (((paramObject1 instanceof double[])) && ((paramObject2 instanceof double[])))
/* 3289 */       bool = equals((double[])paramObject1, (double[])paramObject2);
/* 3290 */     else if (((paramObject1 instanceof boolean[])) && ((paramObject2 instanceof boolean[])))
/* 3291 */       bool = equals((boolean[])paramObject1, (boolean[])paramObject2);
/*      */     else
/* 3293 */       bool = paramObject1.equals(paramObject2);
/* 3294 */     return bool;
/*      */   }
/*      */ 
/*      */   public static String toString(long[] paramArrayOfLong)
/*      */   {
/* 3311 */     if (paramArrayOfLong == null)
/* 3312 */       return "null";
/* 3313 */     int i = paramArrayOfLong.length - 1;
/* 3314 */     if (i == -1) {
/* 3315 */       return "[]";
/*      */     }
/* 3317 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3318 */     localStringBuilder.append('[');
/* 3319 */     for (int j = 0; ; j++) {
/* 3320 */       localStringBuilder.append(paramArrayOfLong[j]);
/* 3321 */       if (j == i)
/* 3322 */         return ']';
/* 3323 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(int[] paramArrayOfInt)
/*      */   {
/* 3341 */     if (paramArrayOfInt == null)
/* 3342 */       return "null";
/* 3343 */     int i = paramArrayOfInt.length - 1;
/* 3344 */     if (i == -1) {
/* 3345 */       return "[]";
/*      */     }
/* 3347 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3348 */     localStringBuilder.append('[');
/* 3349 */     for (int j = 0; ; j++) {
/* 3350 */       localStringBuilder.append(paramArrayOfInt[j]);
/* 3351 */       if (j == i)
/* 3352 */         return ']';
/* 3353 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(short[] paramArrayOfShort)
/*      */   {
/* 3371 */     if (paramArrayOfShort == null)
/* 3372 */       return "null";
/* 3373 */     int i = paramArrayOfShort.length - 1;
/* 3374 */     if (i == -1) {
/* 3375 */       return "[]";
/*      */     }
/* 3377 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3378 */     localStringBuilder.append('[');
/* 3379 */     for (int j = 0; ; j++) {
/* 3380 */       localStringBuilder.append(paramArrayOfShort[j]);
/* 3381 */       if (j == i)
/* 3382 */         return ']';
/* 3383 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(char[] paramArrayOfChar)
/*      */   {
/* 3401 */     if (paramArrayOfChar == null)
/* 3402 */       return "null";
/* 3403 */     int i = paramArrayOfChar.length - 1;
/* 3404 */     if (i == -1) {
/* 3405 */       return "[]";
/*      */     }
/* 3407 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3408 */     localStringBuilder.append('[');
/* 3409 */     for (int j = 0; ; j++) {
/* 3410 */       localStringBuilder.append(paramArrayOfChar[j]);
/* 3411 */       if (j == i)
/* 3412 */         return ']';
/* 3413 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(byte[] paramArrayOfByte)
/*      */   {
/* 3431 */     if (paramArrayOfByte == null)
/* 3432 */       return "null";
/* 3433 */     int i = paramArrayOfByte.length - 1;
/* 3434 */     if (i == -1) {
/* 3435 */       return "[]";
/*      */     }
/* 3437 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3438 */     localStringBuilder.append('[');
/* 3439 */     for (int j = 0; ; j++) {
/* 3440 */       localStringBuilder.append(paramArrayOfByte[j]);
/* 3441 */       if (j == i)
/* 3442 */         return ']';
/* 3443 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(boolean[] paramArrayOfBoolean)
/*      */   {
/* 3461 */     if (paramArrayOfBoolean == null)
/* 3462 */       return "null";
/* 3463 */     int i = paramArrayOfBoolean.length - 1;
/* 3464 */     if (i == -1) {
/* 3465 */       return "[]";
/*      */     }
/* 3467 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3468 */     localStringBuilder.append('[');
/* 3469 */     for (int j = 0; ; j++) {
/* 3470 */       localStringBuilder.append(paramArrayOfBoolean[j]);
/* 3471 */       if (j == i)
/* 3472 */         return ']';
/* 3473 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(float[] paramArrayOfFloat)
/*      */   {
/* 3491 */     if (paramArrayOfFloat == null) {
/* 3492 */       return "null";
/*      */     }
/* 3494 */     int i = paramArrayOfFloat.length - 1;
/* 3495 */     if (i == -1) {
/* 3496 */       return "[]";
/*      */     }
/* 3498 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3499 */     localStringBuilder.append('[');
/* 3500 */     for (int j = 0; ; j++) {
/* 3501 */       localStringBuilder.append(paramArrayOfFloat[j]);
/* 3502 */       if (j == i)
/* 3503 */         return ']';
/* 3504 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(double[] paramArrayOfDouble)
/*      */   {
/* 3522 */     if (paramArrayOfDouble == null)
/* 3523 */       return "null";
/* 3524 */     int i = paramArrayOfDouble.length - 1;
/* 3525 */     if (i == -1) {
/* 3526 */       return "[]";
/*      */     }
/* 3528 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3529 */     localStringBuilder.append('[');
/* 3530 */     for (int j = 0; ; j++) {
/* 3531 */       localStringBuilder.append(paramArrayOfDouble[j]);
/* 3532 */       if (j == i)
/* 3533 */         return ']';
/* 3534 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String toString(Object[] paramArrayOfObject)
/*      */   {
/* 3555 */     if (paramArrayOfObject == null) {
/* 3556 */       return "null";
/*      */     }
/* 3558 */     int i = paramArrayOfObject.length - 1;
/* 3559 */     if (i == -1) {
/* 3560 */       return "[]";
/*      */     }
/* 3562 */     StringBuilder localStringBuilder = new StringBuilder();
/* 3563 */     localStringBuilder.append('[');
/* 3564 */     for (int j = 0; ; j++) {
/* 3565 */       localStringBuilder.append(String.valueOf(paramArrayOfObject[j]));
/* 3566 */       if (j == i)
/* 3567 */         return ']';
/* 3568 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String deepToString(Object[] paramArrayOfObject)
/*      */   {
/* 3606 */     if (paramArrayOfObject == null) {
/* 3607 */       return "null";
/*      */     }
/* 3609 */     int i = 20 * paramArrayOfObject.length;
/* 3610 */     if ((paramArrayOfObject.length != 0) && (i <= 0))
/* 3611 */       i = 2147483647;
/* 3612 */     StringBuilder localStringBuilder = new StringBuilder(i);
/* 3613 */     deepToString(paramArrayOfObject, localStringBuilder, new HashSet());
/* 3614 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static void deepToString(Object[] paramArrayOfObject, StringBuilder paramStringBuilder, Set<Object[]> paramSet)
/*      */   {
/* 3619 */     if (paramArrayOfObject == null) {
/* 3620 */       paramStringBuilder.append("null");
/* 3621 */       return;
/*      */     }
/* 3623 */     int i = paramArrayOfObject.length - 1;
/* 3624 */     if (i == -1) {
/* 3625 */       paramStringBuilder.append("[]");
/* 3626 */       return;
/*      */     }
/*      */ 
/* 3629 */     paramSet.add(paramArrayOfObject);
/* 3630 */     paramStringBuilder.append('[');
/* 3631 */     for (int j = 0; ; j++)
/*      */     {
/* 3633 */       Object localObject = paramArrayOfObject[j];
/* 3634 */       if (localObject == null) {
/* 3635 */         paramStringBuilder.append("null");
/*      */       } else {
/* 3637 */         Class localClass = localObject.getClass();
/*      */ 
/* 3639 */         if (localClass.isArray()) {
/* 3640 */           if (localClass == [B.class)
/* 3641 */             paramStringBuilder.append(toString((byte[])localObject));
/* 3642 */           else if (localClass == [S.class)
/* 3643 */             paramStringBuilder.append(toString((short[])localObject));
/* 3644 */           else if (localClass == [I.class)
/* 3645 */             paramStringBuilder.append(toString((int[])localObject));
/* 3646 */           else if (localClass == [J.class)
/* 3647 */             paramStringBuilder.append(toString((long[])localObject));
/* 3648 */           else if (localClass == [C.class)
/* 3649 */             paramStringBuilder.append(toString((char[])localObject));
/* 3650 */           else if (localClass == [F.class)
/* 3651 */             paramStringBuilder.append(toString((float[])localObject));
/* 3652 */           else if (localClass == [D.class)
/* 3653 */             paramStringBuilder.append(toString((double[])localObject));
/* 3654 */           else if (localClass == [Z.class) {
/* 3655 */             paramStringBuilder.append(toString((boolean[])localObject));
/*      */           }
/* 3657 */           else if (paramSet.contains(localObject))
/* 3658 */             paramStringBuilder.append("[...]");
/*      */           else
/* 3660 */             deepToString((Object[])localObject, paramStringBuilder, paramSet);
/*      */         }
/*      */         else {
/* 3663 */           paramStringBuilder.append(localObject.toString());
/*      */         }
/*      */       }
/* 3666 */       if (j == i)
/*      */         break;
/* 3668 */       paramStringBuilder.append(", ");
/*      */     }
/* 3670 */     paramStringBuilder.append(']');
/* 3671 */     paramSet.remove(paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   private static class ArrayList<E> extends AbstractList<E>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -2764017481108945198L;
/*      */     private final E[] a;
/*      */ 
/*      */     ArrayList(E[] paramArrayOfE)
/*      */     {
/* 2841 */       if (paramArrayOfE == null)
/* 2842 */         throw new NullPointerException();
/* 2843 */       this.a = paramArrayOfE;
/*      */     }
/*      */ 
/*      */     public int size() {
/* 2847 */       return this.a.length;
/*      */     }
/*      */ 
/*      */     public Object[] toArray() {
/* 2851 */       return (Object[])this.a.clone();
/*      */     }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 2855 */       int i = size();
/* 2856 */       if (paramArrayOfT.length < i) {
/* 2857 */         return Arrays.copyOf(this.a, i, paramArrayOfT.getClass());
/*      */       }
/* 2859 */       System.arraycopy(this.a, 0, paramArrayOfT, 0, i);
/* 2860 */       if (paramArrayOfT.length > i)
/* 2861 */         paramArrayOfT[i] = null;
/* 2862 */       return paramArrayOfT;
/*      */     }
/*      */ 
/*      */     public E get(int paramInt) {
/* 2866 */       return this.a[paramInt];
/*      */     }
/*      */ 
/*      */     public E set(int paramInt, E paramE) {
/* 2870 */       Object localObject = this.a[paramInt];
/* 2871 */       this.a[paramInt] = paramE;
/* 2872 */       return localObject;
/*      */     }
/*      */ 
/*      */     public int indexOf(Object paramObject)
/*      */     {
/*      */       int i;
/* 2876 */       if (paramObject == null)
/* 2877 */         for (i = 0; i < this.a.length; i++)
/* 2878 */           if (this.a[i] == null)
/* 2879 */             return i;
/*      */       else {
/* 2881 */         for (i = 0; i < this.a.length; i++)
/* 2882 */           if (paramObject.equals(this.a[i]))
/* 2883 */             return i;
/*      */       }
/* 2885 */       return -1;
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 2889 */       return indexOf(paramObject) != -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class LegacyMergeSort
/*      */   {
/*  387 */     private static final boolean userRequested = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("java.util.Arrays.useLegacyMergeSort"))).booleanValue();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Arrays
 * JD-Core Version:    0.6.2
 */