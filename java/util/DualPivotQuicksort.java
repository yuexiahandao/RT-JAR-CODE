/*      */ package java.util;
/*      */ 
/*      */ final class DualPivotQuicksort
/*      */ {
/*      */   private static final int MAX_RUN_COUNT = 67;
/*      */   private static final int MAX_RUN_LENGTH = 33;
/*      */   private static final int QUICKSORT_THRESHOLD = 286;
/*      */   private static final int INSERTION_SORT_THRESHOLD = 47;
/*      */   private static final int COUNTING_SORT_THRESHOLD_FOR_BYTE = 29;
/*      */   private static final int COUNTING_SORT_THRESHOLD_FOR_SHORT_OR_CHAR = 3200;
/*      */   private static final int NUM_SHORT_VALUES = 65536;
/*      */   private static final int NUM_CHAR_VALUES = 65536;
/*      */   private static final int NUM_BYTE_VALUES = 256;
/*      */ 
/*      */   public static void sort(int[] paramArrayOfInt)
/*      */   {
/*   97 */     sort(paramArrayOfInt, 0, paramArrayOfInt.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  109 */     if (paramInt2 - paramInt1 < 286) {
/*  110 */       sort(paramArrayOfInt, paramInt1, paramInt2, true);
/*  111 */       return;
/*      */     }
/*      */ 
/*  118 */     int[] arrayOfInt1 = new int[68];
/*  119 */     int i = 0; arrayOfInt1[0] = paramInt1;
/*      */     int n;
/*  122 */     for (int j = paramInt1; j < paramInt2; arrayOfInt1[i] = j) {
/*  123 */       if (paramArrayOfInt[j] < paramArrayOfInt[(j + 1)]) while (true) {
/*  124 */           j++; if ((j > paramInt2) || (paramArrayOfInt[(j - 1)] > paramArrayOfInt[j])) break;
/*      */         } if (paramArrayOfInt[j] > paramArrayOfInt[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfInt[(j - 1)] >= paramArrayOfInt[j]));
/*  127 */         k = arrayOfInt1[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/*  128 */           n = paramArrayOfInt[k]; paramArrayOfInt[k] = paramArrayOfInt[m]; paramArrayOfInt[m] = n; }
/*      */       }
/*      */       else {
/*  131 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfInt[(j - 1)] != paramArrayOfInt[j])) break;
/*  132 */           k--; } while (k != 0);
/*  133 */         sort(paramArrayOfInt, paramInt1, paramInt2, true);
/*  134 */         return;
/*      */       }
/*      */ 
/*  143 */       i++; if (i == 67) {
/*  144 */         sort(paramArrayOfInt, paramInt1, paramInt2, true);
/*  145 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  150 */     if (arrayOfInt1[i] == paramInt2++)
/*  151 */       arrayOfInt1[(++i)] = paramInt2;
/*  152 */     else if (i == 1) {
/*  153 */       return;
/*      */     }
/*      */ 
/*  160 */     int k = 0;
/*  161 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/*  163 */     if (k == 0) {
/*  164 */       localObject = paramArrayOfInt; paramArrayOfInt = new int[localObject.length];
/*  165 */       for (m = paramInt1 - 1; ; paramArrayOfInt[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/*  167 */       localObject = new int[paramArrayOfInt.length];
/*      */     }
/*      */ 
/*  171 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/*  172 */       for (n = (m = 0) + 2; n <= i; n += 2) {
/*  173 */         i1 = arrayOfInt1[n]; int i2 = arrayOfInt1[(n - 1)];
/*  174 */         int i3 = arrayOfInt1[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/*  175 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfInt[i4] <= paramArrayOfInt[i5])))
/*  176 */             localObject[i3] = paramArrayOfInt[(i4++)];
/*      */           else {
/*  178 */             localObject[i3] = paramArrayOfInt[(i5++)];
/*      */           }
/*      */         }
/*  181 */         arrayOfInt1[(++m)] = i1;
/*      */       }
/*  183 */       if ((i & 0x1) != 0) {
/*  184 */         n = paramInt2; i1 = arrayOfInt1[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/*  185 */           localObject[n] = paramArrayOfInt[n];
/*      */         }
/*  187 */         arrayOfInt1[(++m)] = paramInt2;
/*      */       }
/*  189 */       int[] arrayOfInt2 = paramArrayOfInt; paramArrayOfInt = (int[])localObject; localObject = arrayOfInt2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(int[] paramArrayOfInt, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  202 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/*  205 */     if (i < 47) {
/*  206 */       if (paramBoolean)
/*      */       {
/*  212 */         j = paramInt1; for (k = j; j < paramInt2; k = j) {
/*  213 */           m = paramArrayOfInt[(j + 1)];
/*  214 */           while (m < paramArrayOfInt[k]) {
/*  215 */             paramArrayOfInt[(k + 1)] = paramArrayOfInt[k];
/*  216 */             if (k-- == paramInt1) {
/*  217 */               break;
/*      */             }
/*      */           }
/*  220 */           paramArrayOfInt[(k + 1)] = m;
/*      */ 
/*  212 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/*  227 */           if (paramInt1 >= paramInt2)
/*  228 */             return;
/*      */         }
/*  230 */         while (paramArrayOfInt[(++paramInt1)] >= paramArrayOfInt[(paramInt1 - 1)]);
/*      */ 
/*  240 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/*  241 */           k = paramArrayOfInt[j]; m = paramArrayOfInt[paramInt1];
/*      */ 
/*  243 */           if (k < m) {
/*  244 */             m = k; k = paramArrayOfInt[paramInt1];
/*      */           }
/*  246 */           while (k < paramArrayOfInt[(--j)]) {
/*  247 */             paramArrayOfInt[(j + 2)] = paramArrayOfInt[j];
/*      */           }
/*  249 */           paramArrayOfInt[(++j + 1)] = k;
/*      */ 
/*  251 */           while (m < paramArrayOfInt[(--j)]) {
/*  252 */             paramArrayOfInt[(j + 1)] = paramArrayOfInt[j];
/*      */           }
/*  254 */           paramArrayOfInt[(j + 1)] = m;
/*      */ 
/*  240 */           paramInt1++;
/*      */         }
/*      */ 
/*  256 */         j = paramArrayOfInt[paramInt2];
/*      */ 
/*  258 */         while (j < paramArrayOfInt[(--paramInt2)]) {
/*  259 */           paramArrayOfInt[(paramInt2 + 1)] = paramArrayOfInt[paramInt2];
/*      */         }
/*  261 */         paramArrayOfInt[(paramInt2 + 1)] = j;
/*      */       }
/*  263 */       return;
/*      */     }
/*      */ 
/*  267 */     int j = (i >> 3) + (i >> 6) + 1;
/*      */ 
/*  276 */     int k = paramInt1 + paramInt2 >>> 1;
/*  277 */     int m = k - j;
/*  278 */     int n = m - j;
/*  279 */     int i1 = k + j;
/*  280 */     int i2 = i1 + j;
/*      */ 
/*  283 */     if (paramArrayOfInt[m] < paramArrayOfInt[n]) { i3 = paramArrayOfInt[m]; paramArrayOfInt[m] = paramArrayOfInt[n]; paramArrayOfInt[n] = i3;
/*      */     }
/*  285 */     if (paramArrayOfInt[k] < paramArrayOfInt[m]) { i3 = paramArrayOfInt[k]; paramArrayOfInt[k] = paramArrayOfInt[m]; paramArrayOfInt[m] = i3;
/*  286 */       if (i3 < paramArrayOfInt[n]) { paramArrayOfInt[m] = paramArrayOfInt[n]; paramArrayOfInt[n] = i3; }
/*      */     }
/*  288 */     if (paramArrayOfInt[i1] < paramArrayOfInt[k]) { i3 = paramArrayOfInt[i1]; paramArrayOfInt[i1] = paramArrayOfInt[k]; paramArrayOfInt[k] = i3;
/*  289 */       if (i3 < paramArrayOfInt[m]) { paramArrayOfInt[k] = paramArrayOfInt[m]; paramArrayOfInt[m] = i3;
/*  290 */         if (i3 < paramArrayOfInt[n]) { paramArrayOfInt[m] = paramArrayOfInt[n]; paramArrayOfInt[n] = i3; }
/*      */       }
/*      */     }
/*  293 */     if (paramArrayOfInt[i2] < paramArrayOfInt[i1]) { i3 = paramArrayOfInt[i2]; paramArrayOfInt[i2] = paramArrayOfInt[i1]; paramArrayOfInt[i1] = i3;
/*  294 */       if (i3 < paramArrayOfInt[k]) { paramArrayOfInt[i1] = paramArrayOfInt[k]; paramArrayOfInt[k] = i3;
/*  295 */         if (i3 < paramArrayOfInt[m]) { paramArrayOfInt[k] = paramArrayOfInt[m]; paramArrayOfInt[m] = i3;
/*  296 */           if (i3 < paramArrayOfInt[n]) { paramArrayOfInt[m] = paramArrayOfInt[n]; paramArrayOfInt[n] = i3;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  302 */     int i3 = paramInt1;
/*  303 */     int i4 = paramInt2;
/*      */     int i5;
/*      */     int i6;
/*      */     int i7;
/*  305 */     if ((paramArrayOfInt[n] != paramArrayOfInt[m]) && (paramArrayOfInt[m] != paramArrayOfInt[k]) && (paramArrayOfInt[k] != paramArrayOfInt[i1]) && (paramArrayOfInt[i1] != paramArrayOfInt[i2]))
/*      */     {
/*  311 */       i5 = paramArrayOfInt[m];
/*  312 */       i6 = paramArrayOfInt[i1];
/*      */ 
/*  320 */       paramArrayOfInt[m] = paramArrayOfInt[paramInt1];
/*  321 */       paramArrayOfInt[i1] = paramArrayOfInt[paramInt2];
/*      */ 
/*  326 */       while (paramArrayOfInt[(++i3)] < i5);
/*  327 */       while (paramArrayOfInt[(--i4)] > i6);
/*  349 */       i7 = i3 - 1;
/*      */       int i8;
/*      */       while (true)
/*      */       {
/*  349 */         i7++; if (i7 > i4) break;
/*  350 */         i8 = paramArrayOfInt[i7];
/*  351 */         if (i8 < i5) {
/*  352 */           paramArrayOfInt[i7] = paramArrayOfInt[i3];
/*      */ 
/*  357 */           paramArrayOfInt[i3] = i8;
/*  358 */           i3++;
/*  359 */         } else if (i8 > i6) {
/*  360 */           while (paramArrayOfInt[i4] > i6) {
/*  361 */             if (i4-- == i7) {
/*  362 */               break label808;
/*      */             }
/*      */           }
/*  365 */           if (paramArrayOfInt[i4] < i5) {
/*  366 */             paramArrayOfInt[i7] = paramArrayOfInt[i3];
/*  367 */             paramArrayOfInt[i3] = paramArrayOfInt[i4];
/*  368 */             i3++;
/*      */           } else {
/*  370 */             paramArrayOfInt[i7] = paramArrayOfInt[i4];
/*      */           }
/*      */ 
/*  376 */           paramArrayOfInt[i4] = i8;
/*  377 */           i4--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  382 */       label808: paramArrayOfInt[paramInt1] = paramArrayOfInt[(i3 - 1)]; paramArrayOfInt[(i3 - 1)] = i5;
/*  383 */       paramArrayOfInt[paramInt2] = paramArrayOfInt[(i4 + 1)]; paramArrayOfInt[(i4 + 1)] = i6;
/*      */ 
/*  386 */       sort(paramArrayOfInt, paramInt1, i3 - 2, paramBoolean);
/*  387 */       sort(paramArrayOfInt, i4 + 2, paramInt2, false);
/*      */ 
/*  393 */       if ((i3 < n) && (i2 < i4))
/*      */       {
/*  397 */         while (paramArrayOfInt[i3] == i5) {
/*  398 */           i3++;
/*      */         }
/*      */ 
/*  401 */         while (paramArrayOfInt[i4] == i6) {
/*  402 */           i4--;
/*      */         }
/*      */ 
/*  425 */         i7 = i3 - 1;
/*      */         while (true) { i7++; if (i7 > i4) break;
/*  426 */           i8 = paramArrayOfInt[i7];
/*  427 */           if (i8 == i5) {
/*  428 */             paramArrayOfInt[i7] = paramArrayOfInt[i3];
/*  429 */             paramArrayOfInt[i3] = i8;
/*  430 */             i3++;
/*  431 */           } else if (i8 == i6) {
/*  432 */             while (paramArrayOfInt[i4] == i6) {
/*  433 */               if (i4-- == i7) {
/*  434 */                 break label1033;
/*      */               }
/*      */             }
/*  437 */             if (paramArrayOfInt[i4] == i5) {
/*  438 */               paramArrayOfInt[i7] = paramArrayOfInt[i3];
/*      */ 
/*  447 */               paramArrayOfInt[i3] = i5;
/*  448 */               i3++;
/*      */             } else {
/*  450 */               paramArrayOfInt[i7] = paramArrayOfInt[i4];
/*      */             }
/*  452 */             paramArrayOfInt[i4] = i8;
/*  453 */             i4--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  459 */       label1033: sort(paramArrayOfInt, i3, i4, false);
/*      */     }
/*      */     else
/*      */     {
/*  466 */       i5 = paramArrayOfInt[k];
/*      */ 
/*  488 */       for (i6 = i3; i6 <= i4; i6++) {
/*  489 */         if (paramArrayOfInt[i6] != i5)
/*      */         {
/*  492 */           i7 = paramArrayOfInt[i6];
/*  493 */           if (i7 < i5) {
/*  494 */             paramArrayOfInt[i6] = paramArrayOfInt[i3];
/*  495 */             paramArrayOfInt[i3] = i7;
/*  496 */             i3++;
/*      */           } else {
/*  498 */             while (paramArrayOfInt[i4] > i5) {
/*  499 */               i4--;
/*      */             }
/*  501 */             if (paramArrayOfInt[i4] < i5) {
/*  502 */               paramArrayOfInt[i6] = paramArrayOfInt[i3];
/*  503 */               paramArrayOfInt[i3] = paramArrayOfInt[i4];
/*  504 */               i3++;
/*      */             }
/*      */             else
/*      */             {
/*  514 */               paramArrayOfInt[i6] = i5;
/*      */             }
/*  516 */             paramArrayOfInt[i4] = i7;
/*  517 */             i4--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  526 */       sort(paramArrayOfInt, paramInt1, i3 - 1, paramBoolean);
/*  527 */       sort(paramArrayOfInt, i4 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(long[] paramArrayOfLong)
/*      */   {
/*  537 */     sort(paramArrayOfLong, 0, paramArrayOfLong.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*  549 */     if (paramInt2 - paramInt1 < 286) {
/*  550 */       sort(paramArrayOfLong, paramInt1, paramInt2, true);
/*  551 */       return;
/*      */     }
/*      */ 
/*  558 */     int[] arrayOfInt = new int[68];
/*  559 */     int i = 0; arrayOfInt[0] = paramInt1;
/*      */ 
/*  562 */     for (int j = paramInt1; j < paramInt2; arrayOfInt[i] = j) {
/*  563 */       if (paramArrayOfLong[j] < paramArrayOfLong[(j + 1)]) while (true) {
/*  564 */           j++; if ((j > paramInt2) || (paramArrayOfLong[(j - 1)] > paramArrayOfLong[j])) break;
/*      */         } if (paramArrayOfLong[j] > paramArrayOfLong[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfLong[(j - 1)] >= paramArrayOfLong[j]));
/*  567 */         k = arrayOfInt[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/*  568 */           long l = paramArrayOfLong[k]; paramArrayOfLong[k] = paramArrayOfLong[m]; paramArrayOfLong[m] = l; }
/*      */       }
/*      */       else {
/*  571 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfLong[(j - 1)] != paramArrayOfLong[j])) break;
/*  572 */           k--; } while (k != 0);
/*  573 */         sort(paramArrayOfLong, paramInt1, paramInt2, true);
/*  574 */         return;
/*      */       }
/*      */ 
/*  583 */       i++; if (i == 67) {
/*  584 */         sort(paramArrayOfLong, paramInt1, paramInt2, true);
/*  585 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  590 */     if (arrayOfInt[i] == paramInt2++)
/*  591 */       arrayOfInt[(++i)] = paramInt2;
/*  592 */     else if (i == 1) {
/*  593 */       return;
/*      */     }
/*      */ 
/*  600 */     int k = 0;
/*  601 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/*  603 */     if (k == 0) {
/*  604 */       localObject = paramArrayOfLong; paramArrayOfLong = new long[localObject.length];
/*  605 */       for (m = paramInt1 - 1; ; paramArrayOfLong[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/*  607 */       localObject = new long[paramArrayOfLong.length];
/*      */     }
/*      */ 
/*  611 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/*  612 */       for (int n = (m = 0) + 2; n <= i; n += 2) {
/*  613 */         i1 = arrayOfInt[n]; int i2 = arrayOfInt[(n - 1)];
/*  614 */         int i3 = arrayOfInt[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/*  615 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfLong[i4] <= paramArrayOfLong[i5])))
/*  616 */             localObject[i3] = paramArrayOfLong[(i4++)];
/*      */           else {
/*  618 */             localObject[i3] = paramArrayOfLong[(i5++)];
/*      */           }
/*      */         }
/*  621 */         arrayOfInt[(++m)] = i1;
/*      */       }
/*  623 */       if ((i & 0x1) != 0) {
/*  624 */         n = paramInt2; i1 = arrayOfInt[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/*  625 */           localObject[n] = paramArrayOfLong[n];
/*      */         }
/*  627 */         arrayOfInt[(++m)] = paramInt2;
/*      */       }
/*  629 */       long[] arrayOfLong = paramArrayOfLong; paramArrayOfLong = (long[])localObject; localObject = arrayOfLong;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(long[] paramArrayOfLong, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  642 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/*  645 */     if (i < 47)
/*      */     {
/*      */       int j;
/*  646 */       if (paramBoolean)
/*      */       {
/*  652 */         j = paramInt1; for (int m = j; j < paramInt2; m = j) {
/*  653 */           long l3 = paramArrayOfLong[(j + 1)];
/*  654 */           while (l3 < paramArrayOfLong[m]) {
/*  655 */             paramArrayOfLong[(m + 1)] = paramArrayOfLong[m];
/*  656 */             if (m-- == paramInt1) {
/*  657 */               break;
/*      */             }
/*      */           }
/*  660 */           paramArrayOfLong[(m + 1)] = l3;
/*      */ 
/*  652 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/*  667 */           if (paramInt1 >= paramInt2)
/*  668 */             return;
/*      */         }
/*  670 */         while (paramArrayOfLong[(++paramInt1)] >= paramArrayOfLong[(paramInt1 - 1)]);
/*      */ 
/*  680 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/*  681 */           long l2 = paramArrayOfLong[j]; long l4 = paramArrayOfLong[paramInt1];
/*      */ 
/*  683 */           if (l2 < l4) {
/*  684 */             l4 = l2; l2 = paramArrayOfLong[paramInt1];
/*      */           }
/*  686 */           while (l2 < paramArrayOfLong[(--j)]) {
/*  687 */             paramArrayOfLong[(j + 2)] = paramArrayOfLong[j];
/*      */           }
/*  689 */           paramArrayOfLong[(++j + 1)] = l2;
/*      */ 
/*  691 */           while (l4 < paramArrayOfLong[(--j)]) {
/*  692 */             paramArrayOfLong[(j + 1)] = paramArrayOfLong[j];
/*      */           }
/*  694 */           paramArrayOfLong[(j + 1)] = l4;
/*      */ 
/*  680 */           paramInt1++;
/*      */         }
/*      */ 
/*  696 */         long l1 = paramArrayOfLong[paramInt2];
/*      */ 
/*  698 */         while (l1 < paramArrayOfLong[(--paramInt2)]) {
/*  699 */           paramArrayOfLong[(paramInt2 + 1)] = paramArrayOfLong[paramInt2];
/*      */         }
/*  701 */         paramArrayOfLong[(paramInt2 + 1)] = l1;
/*      */       }
/*  703 */       return;
/*      */     }
/*      */ 
/*  707 */     int k = (i >> 3) + (i >> 6) + 1;
/*      */ 
/*  716 */     int n = paramInt1 + paramInt2 >>> 1;
/*  717 */     int i1 = n - k;
/*  718 */     int i2 = i1 - k;
/*  719 */     int i3 = n + k;
/*  720 */     int i4 = i3 + k;
/*      */     long l5;
/*  723 */     if (paramArrayOfLong[i1] < paramArrayOfLong[i2]) { l5 = paramArrayOfLong[i1]; paramArrayOfLong[i1] = paramArrayOfLong[i2]; paramArrayOfLong[i2] = l5;
/*      */     }
/*  725 */     if (paramArrayOfLong[n] < paramArrayOfLong[i1]) { l5 = paramArrayOfLong[n]; paramArrayOfLong[n] = paramArrayOfLong[i1]; paramArrayOfLong[i1] = l5;
/*  726 */       if (l5 < paramArrayOfLong[i2]) { paramArrayOfLong[i1] = paramArrayOfLong[i2]; paramArrayOfLong[i2] = l5; }
/*      */     }
/*  728 */     if (paramArrayOfLong[i3] < paramArrayOfLong[n]) { l5 = paramArrayOfLong[i3]; paramArrayOfLong[i3] = paramArrayOfLong[n]; paramArrayOfLong[n] = l5;
/*  729 */       if (l5 < paramArrayOfLong[i1]) { paramArrayOfLong[n] = paramArrayOfLong[i1]; paramArrayOfLong[i1] = l5;
/*  730 */         if (l5 < paramArrayOfLong[i2]) { paramArrayOfLong[i1] = paramArrayOfLong[i2]; paramArrayOfLong[i2] = l5; }
/*      */       }
/*      */     }
/*  733 */     if (paramArrayOfLong[i4] < paramArrayOfLong[i3]) { l5 = paramArrayOfLong[i4]; paramArrayOfLong[i4] = paramArrayOfLong[i3]; paramArrayOfLong[i3] = l5;
/*  734 */       if (l5 < paramArrayOfLong[n]) { paramArrayOfLong[i3] = paramArrayOfLong[n]; paramArrayOfLong[n] = l5;
/*  735 */         if (l5 < paramArrayOfLong[i1]) { paramArrayOfLong[n] = paramArrayOfLong[i1]; paramArrayOfLong[i1] = l5;
/*  736 */           if (l5 < paramArrayOfLong[i2]) { paramArrayOfLong[i1] = paramArrayOfLong[i2]; paramArrayOfLong[i2] = l5;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  742 */     long l6 = paramInt1;
/*  743 */     long l7 = paramInt2;
/*      */     long l8;
/*      */     long l9;
/*  745 */     if ((paramArrayOfLong[i2] != paramArrayOfLong[i1]) && (paramArrayOfLong[i1] != paramArrayOfLong[n]) && (paramArrayOfLong[n] != paramArrayOfLong[i3]) && (paramArrayOfLong[i3] != paramArrayOfLong[i4]))
/*      */     {
/*  751 */       l8 = paramArrayOfLong[i1];
/*  752 */       l9 = paramArrayOfLong[i3];
/*      */ 
/*  760 */       paramArrayOfLong[i1] = paramArrayOfLong[paramInt1];
/*  761 */       paramArrayOfLong[i3] = paramArrayOfLong[paramInt2];
/*      */ 
/*  766 */       while (paramArrayOfLong[(++l6)] < l8);
/*  767 */       while (paramArrayOfLong[(--l7)] > l9);
/*  789 */       long l11 = l6 - 1;
/*      */       long l13;
/*      */       while (true)
/*      */       {
/*  789 */         l11++; if (l11 > l7) break;
/*  790 */         l13 = paramArrayOfLong[l11];
/*  791 */         if (l13 < l8) {
/*  792 */           paramArrayOfLong[l11] = paramArrayOfLong[l6];
/*      */ 
/*  797 */           paramArrayOfLong[l6] = l13;
/*  798 */           l6++;
/*  799 */         } else if (l13 > l9) {
/*  800 */           while (paramArrayOfLong[l7] > l9) {
/*  801 */             if (l7-- == l11) {
/*  802 */               break label834;
/*      */             }
/*      */           }
/*  805 */           if (paramArrayOfLong[l7] < l8) {
/*  806 */             paramArrayOfLong[l11] = paramArrayOfLong[l6];
/*  807 */             paramArrayOfLong[l6] = paramArrayOfLong[l7];
/*  808 */             l6++;
/*      */           } else {
/*  810 */             paramArrayOfLong[l11] = paramArrayOfLong[l7];
/*      */           }
/*      */ 
/*  816 */           paramArrayOfLong[l7] = l13;
/*  817 */           l7--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  822 */       label834: paramArrayOfLong[paramInt1] = paramArrayOfLong[(l6 - 1)]; paramArrayOfLong[(l6 - 1)] = l8;
/*  823 */       paramArrayOfLong[paramInt2] = paramArrayOfLong[(l7 + 1)]; paramArrayOfLong[(l7 + 1)] = l9;
/*      */ 
/*  826 */       sort(paramArrayOfLong, paramInt1, l6 - 2, paramBoolean);
/*  827 */       sort(paramArrayOfLong, l7 + 2, paramInt2, false);
/*      */ 
/*  833 */       if ((l6 < i2) && (i4 < l7))
/*      */       {
/*  837 */         while (paramArrayOfLong[l6] == l8) {
/*  838 */           l6++;
/*      */         }
/*      */ 
/*  841 */         while (paramArrayOfLong[l7] == l9) {
/*  842 */           l7--;
/*      */         }
/*      */ 
/*  865 */         long l12 = l6 - 1;
/*      */         while (true) { l12++; if (l12 > l7) break;
/*  866 */           l13 = paramArrayOfLong[l12];
/*  867 */           if (l13 == l8) {
/*  868 */             paramArrayOfLong[l12] = paramArrayOfLong[l6];
/*  869 */             paramArrayOfLong[l6] = l13;
/*  870 */             l6++;
/*  871 */           } else if (l13 == l9) {
/*  872 */             while (paramArrayOfLong[l7] == l9) {
/*  873 */               if (l7-- == l12) {
/*  874 */                 break label1065;
/*      */               }
/*      */             }
/*  877 */             if (paramArrayOfLong[l7] == l8) {
/*  878 */               paramArrayOfLong[l12] = paramArrayOfLong[l6];
/*      */ 
/*  887 */               paramArrayOfLong[l6] = l8;
/*  888 */               l6++;
/*      */             } else {
/*  890 */               paramArrayOfLong[l12] = paramArrayOfLong[l7];
/*      */             }
/*  892 */             paramArrayOfLong[l7] = l13;
/*  893 */             l7--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  899 */       label1065: sort(paramArrayOfLong, l6, l7, false);
/*      */     }
/*      */     else
/*      */     {
/*  906 */       l8 = paramArrayOfLong[n];
/*      */ 
/*  928 */       for (l9 = l6; l9 <= l7; l9++) {
/*  929 */         if (paramArrayOfLong[l9] != l8)
/*      */         {
/*  932 */           long l10 = paramArrayOfLong[l9];
/*  933 */           if (l10 < l8) {
/*  934 */             paramArrayOfLong[l9] = paramArrayOfLong[l6];
/*  935 */             paramArrayOfLong[l6] = l10;
/*  936 */             l6++;
/*      */           } else {
/*  938 */             while (paramArrayOfLong[l7] > l8) {
/*  939 */               l7--;
/*      */             }
/*  941 */             if (paramArrayOfLong[l7] < l8) {
/*  942 */               paramArrayOfLong[l9] = paramArrayOfLong[l6];
/*  943 */               paramArrayOfLong[l6] = paramArrayOfLong[l7];
/*  944 */               l6++;
/*      */             }
/*      */             else
/*      */             {
/*  954 */               paramArrayOfLong[l9] = l8;
/*      */             }
/*  956 */             paramArrayOfLong[l7] = l10;
/*  957 */             l7--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  966 */       sort(paramArrayOfLong, paramInt1, l6 - 1, paramBoolean);
/*  967 */       sort(paramArrayOfLong, l7 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(short[] paramArrayOfShort)
/*      */   {
/*  977 */     sort(paramArrayOfShort, 0, paramArrayOfShort.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  989 */     if (paramInt2 - paramInt1 > 3200) {
/*  990 */       int[] arrayOfInt = new int[65536];
/*      */ 
/*  992 */       int i = paramInt1 - 1;
/*      */       while (true) { i++; if (i > paramInt2) break;
/*  993 */         arrayOfInt[(paramArrayOfShort[i] - -32768)] += 1;
/*      */       }
/*  995 */       i = 65536;
/*      */       int m;
/*  995 */       for (int j = paramInt2 + 1; j > paramInt1; 
/* 1002 */         m > 0)
/*      */       {
/*  996 */         while (arrayOfInt[(--i)] == 0);
/*  997 */         int k = (short)(i + -32768);
/*  998 */         m = arrayOfInt[i];
/*      */ 
/* 1001 */         paramArrayOfShort[(--j)] = k;
/* 1002 */         m--;
/*      */       }
/*      */     } else {
/* 1005 */       doSort(paramArrayOfShort, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void doSort(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/* 1021 */     if (paramInt2 - paramInt1 < 286) {
/* 1022 */       sort(paramArrayOfShort, paramInt1, paramInt2, true);
/* 1023 */       return;
/*      */     }
/*      */ 
/* 1030 */     int[] arrayOfInt = new int[68];
/* 1031 */     int i = 0; arrayOfInt[0] = paramInt1;
/*      */     int n;
/* 1034 */     for (int j = paramInt1; j < paramInt2; arrayOfInt[i] = j) {
/* 1035 */       if (paramArrayOfShort[j] < paramArrayOfShort[(j + 1)]) while (true) {
/* 1036 */           j++; if ((j > paramInt2) || (paramArrayOfShort[(j - 1)] > paramArrayOfShort[j])) break;
/*      */         } if (paramArrayOfShort[j] > paramArrayOfShort[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfShort[(j - 1)] >= paramArrayOfShort[j]));
/* 1039 */         k = arrayOfInt[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/* 1040 */           n = paramArrayOfShort[k]; paramArrayOfShort[k] = paramArrayOfShort[m]; paramArrayOfShort[m] = n; }
/*      */       }
/*      */       else {
/* 1043 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfShort[(j - 1)] != paramArrayOfShort[j])) break;
/* 1044 */           k--; } while (k != 0);
/* 1045 */         sort(paramArrayOfShort, paramInt1, paramInt2, true);
/* 1046 */         return;
/*      */       }
/*      */ 
/* 1055 */       i++; if (i == 67) {
/* 1056 */         sort(paramArrayOfShort, paramInt1, paramInt2, true);
/* 1057 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1062 */     if (arrayOfInt[i] == paramInt2++)
/* 1063 */       arrayOfInt[(++i)] = paramInt2;
/* 1064 */     else if (i == 1) {
/* 1065 */       return;
/*      */     }
/*      */ 
/* 1072 */     int k = 0;
/* 1073 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/* 1075 */     if (k == 0) {
/* 1076 */       localObject = paramArrayOfShort; paramArrayOfShort = new short[localObject.length];
/* 1077 */       for (m = paramInt1 - 1; ; paramArrayOfShort[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/* 1079 */       localObject = new short[paramArrayOfShort.length];
/*      */     }
/*      */ 
/* 1083 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/* 1084 */       for (n = (m = 0) + 2; n <= i; n += 2) {
/* 1085 */         i1 = arrayOfInt[n]; int i2 = arrayOfInt[(n - 1)];
/* 1086 */         int i3 = arrayOfInt[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/* 1087 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfShort[i4] <= paramArrayOfShort[i5])))
/* 1088 */             localObject[i3] = paramArrayOfShort[(i4++)];
/*      */           else {
/* 1090 */             localObject[i3] = paramArrayOfShort[(i5++)];
/*      */           }
/*      */         }
/* 1093 */         arrayOfInt[(++m)] = i1;
/*      */       }
/* 1095 */       if ((i & 0x1) != 0) {
/* 1096 */         n = paramInt2; i1 = arrayOfInt[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/* 1097 */           localObject[n] = paramArrayOfShort[n];
/*      */         }
/* 1099 */         arrayOfInt[(++m)] = paramInt2;
/*      */       }
/* 1101 */       short[] arrayOfShort = paramArrayOfShort; paramArrayOfShort = (short[])localObject; localObject = arrayOfShort;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(short[] paramArrayOfShort, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1114 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/* 1117 */     if (i < 47) {
/* 1118 */       if (paramBoolean)
/*      */       {
/* 1124 */         j = paramInt1; for (k = j; j < paramInt2; k = j) {
/* 1125 */           m = paramArrayOfShort[(j + 1)];
/* 1126 */           while (m < paramArrayOfShort[k]) {
/* 1127 */             paramArrayOfShort[(k + 1)] = paramArrayOfShort[k];
/* 1128 */             if (k-- == paramInt1) {
/* 1129 */               break;
/*      */             }
/*      */           }
/* 1132 */           paramArrayOfShort[(k + 1)] = m;
/*      */ 
/* 1124 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/* 1139 */           if (paramInt1 >= paramInt2)
/* 1140 */             return;
/*      */         }
/* 1142 */         while (paramArrayOfShort[(++paramInt1)] >= paramArrayOfShort[(paramInt1 - 1)]);
/*      */ 
/* 1152 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/* 1153 */           k = paramArrayOfShort[j]; m = paramArrayOfShort[paramInt1];
/*      */ 
/* 1155 */           if (k < m) {
/* 1156 */             m = k; k = paramArrayOfShort[paramInt1];
/*      */           }
/* 1158 */           while (k < paramArrayOfShort[(--j)]) {
/* 1159 */             paramArrayOfShort[(j + 2)] = paramArrayOfShort[j];
/*      */           }
/* 1161 */           paramArrayOfShort[(++j + 1)] = k;
/*      */ 
/* 1163 */           while (m < paramArrayOfShort[(--j)]) {
/* 1164 */             paramArrayOfShort[(j + 1)] = paramArrayOfShort[j];
/*      */           }
/* 1166 */           paramArrayOfShort[(j + 1)] = m;
/*      */ 
/* 1152 */           paramInt1++;
/*      */         }
/*      */ 
/* 1168 */         j = paramArrayOfShort[paramInt2];
/*      */ 
/* 1170 */         while (j < paramArrayOfShort[(--paramInt2)]) {
/* 1171 */           paramArrayOfShort[(paramInt2 + 1)] = paramArrayOfShort[paramInt2];
/*      */         }
/* 1173 */         paramArrayOfShort[(paramInt2 + 1)] = j;
/*      */       }
/* 1175 */       return;
/*      */     }
/*      */ 
/* 1179 */     int j = (i >> 3) + (i >> 6) + 1;
/*      */ 
/* 1188 */     int k = paramInt1 + paramInt2 >>> 1;
/* 1189 */     int m = k - j;
/* 1190 */     int n = m - j;
/* 1191 */     int i1 = k + j;
/* 1192 */     int i2 = i1 + j;
/*      */ 
/* 1195 */     if (paramArrayOfShort[m] < paramArrayOfShort[n]) { i3 = paramArrayOfShort[m]; paramArrayOfShort[m] = paramArrayOfShort[n]; paramArrayOfShort[n] = i3;
/*      */     }
/* 1197 */     if (paramArrayOfShort[k] < paramArrayOfShort[m]) { i3 = paramArrayOfShort[k]; paramArrayOfShort[k] = paramArrayOfShort[m]; paramArrayOfShort[m] = i3;
/* 1198 */       if (i3 < paramArrayOfShort[n]) { paramArrayOfShort[m] = paramArrayOfShort[n]; paramArrayOfShort[n] = i3; }
/*      */     }
/* 1200 */     if (paramArrayOfShort[i1] < paramArrayOfShort[k]) { i3 = paramArrayOfShort[i1]; paramArrayOfShort[i1] = paramArrayOfShort[k]; paramArrayOfShort[k] = i3;
/* 1201 */       if (i3 < paramArrayOfShort[m]) { paramArrayOfShort[k] = paramArrayOfShort[m]; paramArrayOfShort[m] = i3;
/* 1202 */         if (i3 < paramArrayOfShort[n]) { paramArrayOfShort[m] = paramArrayOfShort[n]; paramArrayOfShort[n] = i3; }
/*      */       }
/*      */     }
/* 1205 */     if (paramArrayOfShort[i2] < paramArrayOfShort[i1]) { i3 = paramArrayOfShort[i2]; paramArrayOfShort[i2] = paramArrayOfShort[i1]; paramArrayOfShort[i1] = i3;
/* 1206 */       if (i3 < paramArrayOfShort[k]) { paramArrayOfShort[i1] = paramArrayOfShort[k]; paramArrayOfShort[k] = i3;
/* 1207 */         if (i3 < paramArrayOfShort[m]) { paramArrayOfShort[k] = paramArrayOfShort[m]; paramArrayOfShort[m] = i3;
/* 1208 */           if (i3 < paramArrayOfShort[n]) { paramArrayOfShort[m] = paramArrayOfShort[n]; paramArrayOfShort[n] = i3;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1214 */     int i3 = paramInt1;
/* 1215 */     int i4 = paramInt2;
/*      */     int i5;
/*      */     int i6;
/*      */     int i7;
/* 1217 */     if ((paramArrayOfShort[n] != paramArrayOfShort[m]) && (paramArrayOfShort[m] != paramArrayOfShort[k]) && (paramArrayOfShort[k] != paramArrayOfShort[i1]) && (paramArrayOfShort[i1] != paramArrayOfShort[i2]))
/*      */     {
/* 1223 */       i5 = paramArrayOfShort[m];
/* 1224 */       i6 = paramArrayOfShort[i1];
/*      */ 
/* 1232 */       paramArrayOfShort[m] = paramArrayOfShort[paramInt1];
/* 1233 */       paramArrayOfShort[i1] = paramArrayOfShort[paramInt2];
/*      */ 
/* 1238 */       while (paramArrayOfShort[(++i3)] < i5);
/* 1239 */       while (paramArrayOfShort[(--i4)] > i6);
/* 1261 */       i7 = i3 - 1;
/*      */       int i8;
/*      */       while (true)
/*      */       {
/* 1261 */         i7++; if (i7 > i4) break;
/* 1262 */         i8 = paramArrayOfShort[i7];
/* 1263 */         if (i8 < i5) {
/* 1264 */           paramArrayOfShort[i7] = paramArrayOfShort[i3];
/*      */ 
/* 1269 */           paramArrayOfShort[i3] = i8;
/* 1270 */           i3++;
/* 1271 */         } else if (i8 > i6) {
/* 1272 */           while (paramArrayOfShort[i4] > i6) {
/* 1273 */             if (i4-- == i7) {
/* 1274 */               break label808;
/*      */             }
/*      */           }
/* 1277 */           if (paramArrayOfShort[i4] < i5) {
/* 1278 */             paramArrayOfShort[i7] = paramArrayOfShort[i3];
/* 1279 */             paramArrayOfShort[i3] = paramArrayOfShort[i4];
/* 1280 */             i3++;
/*      */           } else {
/* 1282 */             paramArrayOfShort[i7] = paramArrayOfShort[i4];
/*      */           }
/*      */ 
/* 1288 */           paramArrayOfShort[i4] = i8;
/* 1289 */           i4--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1294 */       label808: paramArrayOfShort[paramInt1] = paramArrayOfShort[(i3 - 1)]; paramArrayOfShort[(i3 - 1)] = i5;
/* 1295 */       paramArrayOfShort[paramInt2] = paramArrayOfShort[(i4 + 1)]; paramArrayOfShort[(i4 + 1)] = i6;
/*      */ 
/* 1298 */       sort(paramArrayOfShort, paramInt1, i3 - 2, paramBoolean);
/* 1299 */       sort(paramArrayOfShort, i4 + 2, paramInt2, false);
/*      */ 
/* 1305 */       if ((i3 < n) && (i2 < i4))
/*      */       {
/* 1309 */         while (paramArrayOfShort[i3] == i5) {
/* 1310 */           i3++;
/*      */         }
/*      */ 
/* 1313 */         while (paramArrayOfShort[i4] == i6) {
/* 1314 */           i4--;
/*      */         }
/*      */ 
/* 1337 */         i7 = i3 - 1;
/*      */         while (true) { i7++; if (i7 > i4) break;
/* 1338 */           i8 = paramArrayOfShort[i7];
/* 1339 */           if (i8 == i5) {
/* 1340 */             paramArrayOfShort[i7] = paramArrayOfShort[i3];
/* 1341 */             paramArrayOfShort[i3] = i8;
/* 1342 */             i3++;
/* 1343 */           } else if (i8 == i6) {
/* 1344 */             while (paramArrayOfShort[i4] == i6) {
/* 1345 */               if (i4-- == i7) {
/* 1346 */                 break label1033;
/*      */               }
/*      */             }
/* 1349 */             if (paramArrayOfShort[i4] == i5) {
/* 1350 */               paramArrayOfShort[i7] = paramArrayOfShort[i3];
/*      */ 
/* 1359 */               paramArrayOfShort[i3] = i5;
/* 1360 */               i3++;
/*      */             } else {
/* 1362 */               paramArrayOfShort[i7] = paramArrayOfShort[i4];
/*      */             }
/* 1364 */             paramArrayOfShort[i4] = i8;
/* 1365 */             i4--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1371 */       label1033: sort(paramArrayOfShort, i3, i4, false);
/*      */     }
/*      */     else
/*      */     {
/* 1378 */       i5 = paramArrayOfShort[k];
/*      */ 
/* 1400 */       for (i6 = i3; i6 <= i4; i6++) {
/* 1401 */         if (paramArrayOfShort[i6] != i5)
/*      */         {
/* 1404 */           i7 = paramArrayOfShort[i6];
/* 1405 */           if (i7 < i5) {
/* 1406 */             paramArrayOfShort[i6] = paramArrayOfShort[i3];
/* 1407 */             paramArrayOfShort[i3] = i7;
/* 1408 */             i3++;
/*      */           } else {
/* 1410 */             while (paramArrayOfShort[i4] > i5) {
/* 1411 */               i4--;
/*      */             }
/* 1413 */             if (paramArrayOfShort[i4] < i5) {
/* 1414 */               paramArrayOfShort[i6] = paramArrayOfShort[i3];
/* 1415 */               paramArrayOfShort[i3] = paramArrayOfShort[i4];
/* 1416 */               i3++;
/*      */             }
/*      */             else
/*      */             {
/* 1426 */               paramArrayOfShort[i6] = i5;
/*      */             }
/* 1428 */             paramArrayOfShort[i4] = i7;
/* 1429 */             i4--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1438 */       sort(paramArrayOfShort, paramInt1, i3 - 1, paramBoolean);
/* 1439 */       sort(paramArrayOfShort, i4 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(char[] paramArrayOfChar)
/*      */   {
/* 1449 */     sort(paramArrayOfChar, 0, paramArrayOfChar.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 1461 */     if (paramInt2 - paramInt1 > 3200) {
/* 1462 */       int[] arrayOfInt = new int[65536];
/*      */ 
/* 1464 */       int i = paramInt1 - 1;
/*      */       while (true) { i++; if (i > paramInt2) break;
/* 1465 */         arrayOfInt[paramArrayOfChar[i]] += 1;
/*      */       }
/* 1467 */       i = 65536;
/*      */       int m;
/* 1467 */       for (int j = paramInt2 + 1; j > paramInt1; 
/* 1474 */         m > 0)
/*      */       {
/* 1468 */         while (arrayOfInt[(--i)] == 0);
/* 1469 */         int k = (char)i;
/* 1470 */         m = arrayOfInt[i];
/*      */ 
/* 1473 */         paramArrayOfChar[(--j)] = k;
/* 1474 */         m--;
/*      */       }
/*      */     } else {
/* 1477 */       doSort(paramArrayOfChar, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void doSort(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 1493 */     if (paramInt2 - paramInt1 < 286) {
/* 1494 */       sort(paramArrayOfChar, paramInt1, paramInt2, true);
/* 1495 */       return;
/*      */     }
/*      */ 
/* 1502 */     int[] arrayOfInt = new int[68];
/* 1503 */     int i = 0; arrayOfInt[0] = paramInt1;
/*      */     int n;
/* 1506 */     for (int j = paramInt1; j < paramInt2; arrayOfInt[i] = j) {
/* 1507 */       if (paramArrayOfChar[j] < paramArrayOfChar[(j + 1)]) while (true) {
/* 1508 */           j++; if ((j > paramInt2) || (paramArrayOfChar[(j - 1)] > paramArrayOfChar[j])) break;
/*      */         } if (paramArrayOfChar[j] > paramArrayOfChar[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfChar[(j - 1)] >= paramArrayOfChar[j]));
/* 1511 */         k = arrayOfInt[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/* 1512 */           n = paramArrayOfChar[k]; paramArrayOfChar[k] = paramArrayOfChar[m]; paramArrayOfChar[m] = n; }
/*      */       }
/*      */       else {
/* 1515 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfChar[(j - 1)] != paramArrayOfChar[j])) break;
/* 1516 */           k--; } while (k != 0);
/* 1517 */         sort(paramArrayOfChar, paramInt1, paramInt2, true);
/* 1518 */         return;
/*      */       }
/*      */ 
/* 1527 */       i++; if (i == 67) {
/* 1528 */         sort(paramArrayOfChar, paramInt1, paramInt2, true);
/* 1529 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1534 */     if (arrayOfInt[i] == paramInt2++)
/* 1535 */       arrayOfInt[(++i)] = paramInt2;
/* 1536 */     else if (i == 1) {
/* 1537 */       return;
/*      */     }
/*      */ 
/* 1544 */     int k = 0;
/* 1545 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/* 1547 */     if (k == 0) {
/* 1548 */       localObject = paramArrayOfChar; paramArrayOfChar = new char[localObject.length];
/* 1549 */       for (m = paramInt1 - 1; ; paramArrayOfChar[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/* 1551 */       localObject = new char[paramArrayOfChar.length];
/*      */     }
/*      */ 
/* 1555 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/* 1556 */       for (n = (m = 0) + 2; n <= i; n += 2) {
/* 1557 */         i1 = arrayOfInt[n]; int i2 = arrayOfInt[(n - 1)];
/* 1558 */         int i3 = arrayOfInt[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/* 1559 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfChar[i4] <= paramArrayOfChar[i5])))
/* 1560 */             localObject[i3] = paramArrayOfChar[(i4++)];
/*      */           else {
/* 1562 */             localObject[i3] = paramArrayOfChar[(i5++)];
/*      */           }
/*      */         }
/* 1565 */         arrayOfInt[(++m)] = i1;
/*      */       }
/* 1567 */       if ((i & 0x1) != 0) {
/* 1568 */         n = paramInt2; i1 = arrayOfInt[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/* 1569 */           localObject[n] = paramArrayOfChar[n];
/*      */         }
/* 1571 */         arrayOfInt[(++m)] = paramInt2;
/*      */       }
/* 1573 */       char[] arrayOfChar = paramArrayOfChar; paramArrayOfChar = (char[])localObject; localObject = arrayOfChar;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1586 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/* 1589 */     if (i < 47) {
/* 1590 */       if (paramBoolean)
/*      */       {
/* 1596 */         j = paramInt1; for (k = j; j < paramInt2; k = j) {
/* 1597 */           m = paramArrayOfChar[(j + 1)];
/* 1598 */           while (m < paramArrayOfChar[k]) {
/* 1599 */             paramArrayOfChar[(k + 1)] = paramArrayOfChar[k];
/* 1600 */             if (k-- == paramInt1) {
/* 1601 */               break;
/*      */             }
/*      */           }
/* 1604 */           paramArrayOfChar[(k + 1)] = m;
/*      */ 
/* 1596 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/* 1611 */           if (paramInt1 >= paramInt2)
/* 1612 */             return;
/*      */         }
/* 1614 */         while (paramArrayOfChar[(++paramInt1)] >= paramArrayOfChar[(paramInt1 - 1)]);
/*      */ 
/* 1624 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/* 1625 */           k = paramArrayOfChar[j]; m = paramArrayOfChar[paramInt1];
/*      */ 
/* 1627 */           if (k < m) {
/* 1628 */             m = k; k = paramArrayOfChar[paramInt1];
/*      */           }
/* 1630 */           while (k < paramArrayOfChar[(--j)]) {
/* 1631 */             paramArrayOfChar[(j + 2)] = paramArrayOfChar[j];
/*      */           }
/* 1633 */           paramArrayOfChar[(++j + 1)] = k;
/*      */ 
/* 1635 */           while (m < paramArrayOfChar[(--j)]) {
/* 1636 */             paramArrayOfChar[(j + 1)] = paramArrayOfChar[j];
/*      */           }
/* 1638 */           paramArrayOfChar[(j + 1)] = m;
/*      */ 
/* 1624 */           paramInt1++;
/*      */         }
/*      */ 
/* 1640 */         j = paramArrayOfChar[paramInt2];
/*      */ 
/* 1642 */         while (j < paramArrayOfChar[(--paramInt2)]) {
/* 1643 */           paramArrayOfChar[(paramInt2 + 1)] = paramArrayOfChar[paramInt2];
/*      */         }
/* 1645 */         paramArrayOfChar[(paramInt2 + 1)] = j;
/*      */       }
/* 1647 */       return;
/*      */     }
/*      */ 
/* 1651 */     int j = (i >> 3) + (i >> 6) + 1;
/*      */ 
/* 1660 */     int k = paramInt1 + paramInt2 >>> 1;
/* 1661 */     int m = k - j;
/* 1662 */     int n = m - j;
/* 1663 */     int i1 = k + j;
/* 1664 */     int i2 = i1 + j;
/*      */ 
/* 1667 */     if (paramArrayOfChar[m] < paramArrayOfChar[n]) { i3 = paramArrayOfChar[m]; paramArrayOfChar[m] = paramArrayOfChar[n]; paramArrayOfChar[n] = i3;
/*      */     }
/* 1669 */     if (paramArrayOfChar[k] < paramArrayOfChar[m]) { i3 = paramArrayOfChar[k]; paramArrayOfChar[k] = paramArrayOfChar[m]; paramArrayOfChar[m] = i3;
/* 1670 */       if (i3 < paramArrayOfChar[n]) { paramArrayOfChar[m] = paramArrayOfChar[n]; paramArrayOfChar[n] = i3; }
/*      */     }
/* 1672 */     if (paramArrayOfChar[i1] < paramArrayOfChar[k]) { i3 = paramArrayOfChar[i1]; paramArrayOfChar[i1] = paramArrayOfChar[k]; paramArrayOfChar[k] = i3;
/* 1673 */       if (i3 < paramArrayOfChar[m]) { paramArrayOfChar[k] = paramArrayOfChar[m]; paramArrayOfChar[m] = i3;
/* 1674 */         if (i3 < paramArrayOfChar[n]) { paramArrayOfChar[m] = paramArrayOfChar[n]; paramArrayOfChar[n] = i3; }
/*      */       }
/*      */     }
/* 1677 */     if (paramArrayOfChar[i2] < paramArrayOfChar[i1]) { i3 = paramArrayOfChar[i2]; paramArrayOfChar[i2] = paramArrayOfChar[i1]; paramArrayOfChar[i1] = i3;
/* 1678 */       if (i3 < paramArrayOfChar[k]) { paramArrayOfChar[i1] = paramArrayOfChar[k]; paramArrayOfChar[k] = i3;
/* 1679 */         if (i3 < paramArrayOfChar[m]) { paramArrayOfChar[k] = paramArrayOfChar[m]; paramArrayOfChar[m] = i3;
/* 1680 */           if (i3 < paramArrayOfChar[n]) { paramArrayOfChar[m] = paramArrayOfChar[n]; paramArrayOfChar[n] = i3;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1686 */     int i3 = paramInt1;
/* 1687 */     int i4 = paramInt2;
/*      */     int i5;
/*      */     int i6;
/*      */     int i7;
/* 1689 */     if ((paramArrayOfChar[n] != paramArrayOfChar[m]) && (paramArrayOfChar[m] != paramArrayOfChar[k]) && (paramArrayOfChar[k] != paramArrayOfChar[i1]) && (paramArrayOfChar[i1] != paramArrayOfChar[i2]))
/*      */     {
/* 1695 */       i5 = paramArrayOfChar[m];
/* 1696 */       i6 = paramArrayOfChar[i1];
/*      */ 
/* 1704 */       paramArrayOfChar[m] = paramArrayOfChar[paramInt1];
/* 1705 */       paramArrayOfChar[i1] = paramArrayOfChar[paramInt2];
/*      */ 
/* 1710 */       while (paramArrayOfChar[(++i3)] < i5);
/* 1711 */       while (paramArrayOfChar[(--i4)] > i6);
/* 1733 */       i7 = i3 - 1;
/*      */       int i8;
/*      */       while (true)
/*      */       {
/* 1733 */         i7++; if (i7 > i4) break;
/* 1734 */         i8 = paramArrayOfChar[i7];
/* 1735 */         if (i8 < i5) {
/* 1736 */           paramArrayOfChar[i7] = paramArrayOfChar[i3];
/*      */ 
/* 1741 */           paramArrayOfChar[i3] = i8;
/* 1742 */           i3++;
/* 1743 */         } else if (i8 > i6) {
/* 1744 */           while (paramArrayOfChar[i4] > i6) {
/* 1745 */             if (i4-- == i7) {
/* 1746 */               break label808;
/*      */             }
/*      */           }
/* 1749 */           if (paramArrayOfChar[i4] < i5) {
/* 1750 */             paramArrayOfChar[i7] = paramArrayOfChar[i3];
/* 1751 */             paramArrayOfChar[i3] = paramArrayOfChar[i4];
/* 1752 */             i3++;
/*      */           } else {
/* 1754 */             paramArrayOfChar[i7] = paramArrayOfChar[i4];
/*      */           }
/*      */ 
/* 1760 */           paramArrayOfChar[i4] = i8;
/* 1761 */           i4--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1766 */       label808: paramArrayOfChar[paramInt1] = paramArrayOfChar[(i3 - 1)]; paramArrayOfChar[(i3 - 1)] = i5;
/* 1767 */       paramArrayOfChar[paramInt2] = paramArrayOfChar[(i4 + 1)]; paramArrayOfChar[(i4 + 1)] = i6;
/*      */ 
/* 1770 */       sort(paramArrayOfChar, paramInt1, i3 - 2, paramBoolean);
/* 1771 */       sort(paramArrayOfChar, i4 + 2, paramInt2, false);
/*      */ 
/* 1777 */       if ((i3 < n) && (i2 < i4))
/*      */       {
/* 1781 */         while (paramArrayOfChar[i3] == i5) {
/* 1782 */           i3++;
/*      */         }
/*      */ 
/* 1785 */         while (paramArrayOfChar[i4] == i6) {
/* 1786 */           i4--;
/*      */         }
/*      */ 
/* 1809 */         i7 = i3 - 1;
/*      */         while (true) { i7++; if (i7 > i4) break;
/* 1810 */           i8 = paramArrayOfChar[i7];
/* 1811 */           if (i8 == i5) {
/* 1812 */             paramArrayOfChar[i7] = paramArrayOfChar[i3];
/* 1813 */             paramArrayOfChar[i3] = i8;
/* 1814 */             i3++;
/* 1815 */           } else if (i8 == i6) {
/* 1816 */             while (paramArrayOfChar[i4] == i6) {
/* 1817 */               if (i4-- == i7) {
/* 1818 */                 break label1033;
/*      */               }
/*      */             }
/* 1821 */             if (paramArrayOfChar[i4] == i5) {
/* 1822 */               paramArrayOfChar[i7] = paramArrayOfChar[i3];
/*      */ 
/* 1831 */               paramArrayOfChar[i3] = i5;
/* 1832 */               i3++;
/*      */             } else {
/* 1834 */               paramArrayOfChar[i7] = paramArrayOfChar[i4];
/*      */             }
/* 1836 */             paramArrayOfChar[i4] = i8;
/* 1837 */             i4--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1843 */       label1033: sort(paramArrayOfChar, i3, i4, false);
/*      */     }
/*      */     else
/*      */     {
/* 1850 */       i5 = paramArrayOfChar[k];
/*      */ 
/* 1872 */       for (i6 = i3; i6 <= i4; i6++) {
/* 1873 */         if (paramArrayOfChar[i6] != i5)
/*      */         {
/* 1876 */           i7 = paramArrayOfChar[i6];
/* 1877 */           if (i7 < i5) {
/* 1878 */             paramArrayOfChar[i6] = paramArrayOfChar[i3];
/* 1879 */             paramArrayOfChar[i3] = i7;
/* 1880 */             i3++;
/*      */           } else {
/* 1882 */             while (paramArrayOfChar[i4] > i5) {
/* 1883 */               i4--;
/*      */             }
/* 1885 */             if (paramArrayOfChar[i4] < i5) {
/* 1886 */               paramArrayOfChar[i6] = paramArrayOfChar[i3];
/* 1887 */               paramArrayOfChar[i3] = paramArrayOfChar[i4];
/* 1888 */               i3++;
/*      */             }
/*      */             else
/*      */             {
/* 1898 */               paramArrayOfChar[i6] = i5;
/*      */             }
/* 1900 */             paramArrayOfChar[i4] = i7;
/* 1901 */             i4--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1910 */       sort(paramArrayOfChar, paramInt1, i3 - 1, paramBoolean);
/* 1911 */       sort(paramArrayOfChar, i4 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(byte[] paramArrayOfByte)
/*      */   {
/* 1924 */     sort(paramArrayOfByte, 0, paramArrayOfByte.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*      */     int j;
/*      */     int k;
/* 1936 */     if (paramInt2 - paramInt1 > 29) {
/* 1937 */       int[] arrayOfInt = new int[256];
/*      */ 
/* 1939 */       j = paramInt1 - 1;
/*      */       while (true) { j++; if (j > paramInt2) break;
/* 1940 */         arrayOfInt[(paramArrayOfByte[j] - -128)] += 1;
/*      */       }
/* 1942 */       j = 256;
/*      */       int n;
/* 1942 */       for (k = paramInt2 + 1; k > paramInt1; 
/* 1949 */         n > 0)
/*      */       {
/* 1943 */         while (arrayOfInt[(--j)] == 0);
/* 1944 */         int m = (byte)(j + -128);
/* 1945 */         n = arrayOfInt[j];
/*      */ 
/* 1948 */         paramArrayOfByte[(--k)] = m;
/* 1949 */         n--;
/*      */       }
/*      */     } else {
/* 1952 */       int i = paramInt1; for (j = i; i < paramInt2; j = i) {
/* 1953 */         k = paramArrayOfByte[(i + 1)];
/* 1954 */         while (k < paramArrayOfByte[j]) {
/* 1955 */           paramArrayOfByte[(j + 1)] = paramArrayOfByte[j];
/* 1956 */           if (j-- == paramInt1) {
/* 1957 */             break;
/*      */           }
/*      */         }
/* 1960 */         paramArrayOfByte[(j + 1)] = k;
/*      */ 
/* 1952 */         i++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(float[] paramArrayOfFloat)
/*      */   {
/* 1971 */     sort(paramArrayOfFloat, 0, paramArrayOfFloat.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/* 1985 */     while ((paramInt1 <= paramInt2) && (Float.isNaN(paramArrayOfFloat[paramInt2]))) {
/* 1986 */       paramInt2--;
/*      */     }
/* 1988 */     int i = paramInt2;
/*      */     while (true) { i--; if (i < paramInt1) break;
/* 1989 */       float f1 = paramArrayOfFloat[i];
/* 1990 */       if (f1 != f1) {
/* 1991 */         paramArrayOfFloat[i] = paramArrayOfFloat[paramInt2];
/* 1992 */         paramArrayOfFloat[paramInt2] = f1;
/* 1993 */         paramInt2--;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2000 */     doSort(paramArrayOfFloat, paramInt1, paramInt2);
/*      */ 
/* 2005 */     i = paramInt2;
/*      */ 
/* 2010 */     while (paramInt1 < i) {
/* 2011 */       j = paramInt1 + i >>> 1;
/* 2012 */       float f2 = paramArrayOfFloat[j];
/*      */ 
/* 2014 */       if (f2 < 0.0F)
/* 2015 */         paramInt1 = j + 1;
/*      */       else {
/* 2017 */         i = j;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2024 */     while ((paramInt1 <= paramInt2) && (Float.floatToRawIntBits(paramArrayOfFloat[paramInt1]) < 0)) {
/* 2025 */       paramInt1++;
/*      */     }
/*      */ 
/* 2049 */     int j = paramInt1; int k = paramInt1 - 1;
/*      */     while (true) { j++; if (j > paramInt2) break;
/* 2050 */       float f3 = paramArrayOfFloat[j];
/* 2051 */       if (f3 != 0.0F) {
/*      */         break;
/*      */       }
/* 2054 */       if (Float.floatToRawIntBits(f3) < 0) {
/* 2055 */         paramArrayOfFloat[j] = 0.0F;
/* 2056 */         paramArrayOfFloat[(++k)] = -0.0F;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void doSort(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/* 2070 */     if (paramInt2 - paramInt1 < 286) {
/* 2071 */       sort(paramArrayOfFloat, paramInt1, paramInt2, true);
/* 2072 */       return;
/*      */     }
/*      */ 
/* 2079 */     int[] arrayOfInt = new int[68];
/* 2080 */     int i = 0; arrayOfInt[0] = paramInt1;
/*      */ 
/* 2083 */     for (int j = paramInt1; j < paramInt2; arrayOfInt[i] = j) {
/* 2084 */       if (paramArrayOfFloat[j] < paramArrayOfFloat[(j + 1)]) while (true) {
/* 2085 */           j++; if ((j > paramInt2) || (paramArrayOfFloat[(j - 1)] > paramArrayOfFloat[j])) break;
/*      */         } if (paramArrayOfFloat[j] > paramArrayOfFloat[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfFloat[(j - 1)] >= paramArrayOfFloat[j]));
/* 2088 */         k = arrayOfInt[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/* 2089 */           float f = paramArrayOfFloat[k]; paramArrayOfFloat[k] = paramArrayOfFloat[m]; paramArrayOfFloat[m] = f; }
/*      */       }
/*      */       else {
/* 2092 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfFloat[(j - 1)] != paramArrayOfFloat[j])) break;
/* 2093 */           k--; } while (k != 0);
/* 2094 */         sort(paramArrayOfFloat, paramInt1, paramInt2, true);
/* 2095 */         return;
/*      */       }
/*      */ 
/* 2104 */       i++; if (i == 67) {
/* 2105 */         sort(paramArrayOfFloat, paramInt1, paramInt2, true);
/* 2106 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2111 */     if (arrayOfInt[i] == paramInt2++)
/* 2112 */       arrayOfInt[(++i)] = paramInt2;
/* 2113 */     else if (i == 1) {
/* 2114 */       return;
/*      */     }
/*      */ 
/* 2121 */     int k = 0;
/* 2122 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/* 2124 */     if (k == 0) {
/* 2125 */       localObject = paramArrayOfFloat; paramArrayOfFloat = new float[localObject.length];
/* 2126 */       for (m = paramInt1 - 1; ; paramArrayOfFloat[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/* 2128 */       localObject = new float[paramArrayOfFloat.length];
/*      */     }
/*      */ 
/* 2132 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/* 2133 */       for (int n = (m = 0) + 2; n <= i; n += 2) {
/* 2134 */         i1 = arrayOfInt[n]; int i2 = arrayOfInt[(n - 1)];
/* 2135 */         int i3 = arrayOfInt[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/* 2136 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfFloat[i4] <= paramArrayOfFloat[i5])))
/* 2137 */             localObject[i3] = paramArrayOfFloat[(i4++)];
/*      */           else {
/* 2139 */             localObject[i3] = paramArrayOfFloat[(i5++)];
/*      */           }
/*      */         }
/* 2142 */         arrayOfInt[(++m)] = i1;
/*      */       }
/* 2144 */       if ((i & 0x1) != 0) {
/* 2145 */         n = paramInt2; i1 = arrayOfInt[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/* 2146 */           localObject[n] = paramArrayOfFloat[n];
/*      */         }
/* 2148 */         arrayOfInt[(++m)] = paramInt2;
/*      */       }
/* 2150 */       float[] arrayOfFloat = paramArrayOfFloat; paramArrayOfFloat = (float[])localObject; localObject = arrayOfFloat;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(float[] paramArrayOfFloat, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2163 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/* 2166 */     if (i < 47)
/*      */     {
/*      */       int j;
/*      */       float f3;
/* 2167 */       if (paramBoolean)
/*      */       {
/* 2173 */         j = paramInt1; for (int m = j; j < paramInt2; m = j) {
/* 2174 */           f3 = paramArrayOfFloat[(j + 1)];
/* 2175 */           while (f3 < paramArrayOfFloat[m]) {
/* 2176 */             paramArrayOfFloat[(m + 1)] = paramArrayOfFloat[m];
/* 2177 */             if (m-- == paramInt1) {
/* 2178 */               break;
/*      */             }
/*      */           }
/* 2181 */           paramArrayOfFloat[(m + 1)] = f3;
/*      */ 
/* 2173 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/* 2188 */           if (paramInt1 >= paramInt2)
/* 2189 */             return;
/*      */         }
/* 2191 */         while (paramArrayOfFloat[(++paramInt1)] >= paramArrayOfFloat[(paramInt1 - 1)]);
/*      */ 
/* 2201 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/* 2202 */           float f2 = paramArrayOfFloat[j]; f3 = paramArrayOfFloat[paramInt1];
/*      */ 
/* 2204 */           if (f2 < f3) {
/* 2205 */             f3 = f2; f2 = paramArrayOfFloat[paramInt1];
/*      */           }
/* 2207 */           while (f2 < paramArrayOfFloat[(--j)]) {
/* 2208 */             paramArrayOfFloat[(j + 2)] = paramArrayOfFloat[j];
/*      */           }
/* 2210 */           paramArrayOfFloat[(++j + 1)] = f2;
/*      */ 
/* 2212 */           while (f3 < paramArrayOfFloat[(--j)]) {
/* 2213 */             paramArrayOfFloat[(j + 1)] = paramArrayOfFloat[j];
/*      */           }
/* 2215 */           paramArrayOfFloat[(j + 1)] = f3;
/*      */ 
/* 2201 */           paramInt1++;
/*      */         }
/*      */ 
/* 2217 */         float f1 = paramArrayOfFloat[paramInt2];
/*      */ 
/* 2219 */         while (f1 < paramArrayOfFloat[(--paramInt2)]) {
/* 2220 */           paramArrayOfFloat[(paramInt2 + 1)] = paramArrayOfFloat[paramInt2];
/*      */         }
/* 2222 */         paramArrayOfFloat[(paramInt2 + 1)] = f1;
/*      */       }
/* 2224 */       return;
/*      */     }
/*      */ 
/* 2228 */     int k = (i >> 3) + (i >> 6) + 1;
/*      */ 
/* 2237 */     int n = paramInt1 + paramInt2 >>> 1;
/* 2238 */     int i1 = n - k;
/* 2239 */     int i2 = i1 - k;
/* 2240 */     int i3 = n + k;
/* 2241 */     int i4 = i3 + k;
/*      */     float f4;
/* 2244 */     if (paramArrayOfFloat[i1] < paramArrayOfFloat[i2]) { f4 = paramArrayOfFloat[i1]; paramArrayOfFloat[i1] = paramArrayOfFloat[i2]; paramArrayOfFloat[i2] = f4;
/*      */     }
/* 2246 */     if (paramArrayOfFloat[n] < paramArrayOfFloat[i1]) { f4 = paramArrayOfFloat[n]; paramArrayOfFloat[n] = paramArrayOfFloat[i1]; paramArrayOfFloat[i1] = f4;
/* 2247 */       if (f4 < paramArrayOfFloat[i2]) { paramArrayOfFloat[i1] = paramArrayOfFloat[i2]; paramArrayOfFloat[i2] = f4; }
/*      */     }
/* 2249 */     if (paramArrayOfFloat[i3] < paramArrayOfFloat[n]) { f4 = paramArrayOfFloat[i3]; paramArrayOfFloat[i3] = paramArrayOfFloat[n]; paramArrayOfFloat[n] = f4;
/* 2250 */       if (f4 < paramArrayOfFloat[i1]) { paramArrayOfFloat[n] = paramArrayOfFloat[i1]; paramArrayOfFloat[i1] = f4;
/* 2251 */         if (f4 < paramArrayOfFloat[i2]) { paramArrayOfFloat[i1] = paramArrayOfFloat[i2]; paramArrayOfFloat[i2] = f4; }
/*      */       }
/*      */     }
/* 2254 */     if (paramArrayOfFloat[i4] < paramArrayOfFloat[i3]) { f4 = paramArrayOfFloat[i4]; paramArrayOfFloat[i4] = paramArrayOfFloat[i3]; paramArrayOfFloat[i3] = f4;
/* 2255 */       if (f4 < paramArrayOfFloat[n]) { paramArrayOfFloat[i3] = paramArrayOfFloat[n]; paramArrayOfFloat[n] = f4;
/* 2256 */         if (f4 < paramArrayOfFloat[i1]) { paramArrayOfFloat[n] = paramArrayOfFloat[i1]; paramArrayOfFloat[i1] = f4;
/* 2257 */           if (f4 < paramArrayOfFloat[i2]) { paramArrayOfFloat[i1] = paramArrayOfFloat[i2]; paramArrayOfFloat[i2] = f4;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2263 */     float f5 = paramInt1;
/* 2264 */     float f6 = paramInt2;
/*      */     float f7;
/*      */     float f8;
/*      */     label834: float f10;
/* 2266 */     if ((paramArrayOfFloat[i2] != paramArrayOfFloat[i1]) && (paramArrayOfFloat[i1] != paramArrayOfFloat[n]) && (paramArrayOfFloat[n] != paramArrayOfFloat[i3]) && (paramArrayOfFloat[i3] != paramArrayOfFloat[i4]))
/*      */     {
/* 2272 */       f7 = paramArrayOfFloat[i1];
/* 2273 */       f8 = paramArrayOfFloat[i3];
/*      */ 
/* 2281 */       paramArrayOfFloat[i1] = paramArrayOfFloat[paramInt1];
/* 2282 */       paramArrayOfFloat[i3] = paramArrayOfFloat[paramInt2];
/*      */ 
/* 2287 */       while (paramArrayOfFloat[(++f5)] < f7);
/* 2288 */       while (paramArrayOfFloat[(--f6)] > f8);
/* 2310 */       float f9 = f5 - 1;
/*      */       float f11;
/*      */       while (true)
/*      */       {
/* 2310 */         f9++; if (f9 > f6) break;
/* 2311 */         f11 = paramArrayOfFloat[f9];
/* 2312 */         if (f11 < f7) {
/* 2313 */           paramArrayOfFloat[f9] = paramArrayOfFloat[f5];
/*      */ 
/* 2318 */           paramArrayOfFloat[f5] = f11;
/* 2319 */           f5++;
/* 2320 */         } else if (f11 > f8) {
/* 2321 */           while (paramArrayOfFloat[f6] > f8) {
/* 2322 */             if (f6-- == f9) {
/* 2323 */               break label834;
/*      */             }
/*      */           }
/* 2326 */           if (paramArrayOfFloat[f6] < f7) {
/* 2327 */             paramArrayOfFloat[f9] = paramArrayOfFloat[f5];
/* 2328 */             paramArrayOfFloat[f5] = paramArrayOfFloat[f6];
/* 2329 */             f5++;
/*      */           } else {
/* 2331 */             paramArrayOfFloat[f9] = paramArrayOfFloat[f6];
/*      */           }
/*      */ 
/* 2337 */           paramArrayOfFloat[f6] = f11;
/* 2338 */           f6--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2343 */       paramArrayOfFloat[paramInt1] = paramArrayOfFloat[(f5 - 1)]; paramArrayOfFloat[(f5 - 1)] = f7;
/* 2344 */       paramArrayOfFloat[paramInt2] = paramArrayOfFloat[(f6 + 1)]; paramArrayOfFloat[(f6 + 1)] = f8;
/*      */ 
/* 2347 */       sort(paramArrayOfFloat, paramInt1, f5 - 2, paramBoolean);
/* 2348 */       sort(paramArrayOfFloat, f6 + 2, paramInt2, false);
/*      */ 
/* 2354 */       if ((f5 < i2) && (i4 < f6))
/*      */       {
/* 2358 */         while (paramArrayOfFloat[f5] == f7) {
/* 2359 */           f5++;
/*      */         }
/*      */ 
/* 2362 */         while (paramArrayOfFloat[f6] == f8) {
/* 2363 */           f6--;
/*      */         }
/*      */ 
/* 2386 */         f10 = f5 - 1;
/*      */         while (true) { f10++; if (f10 > f6) break;
/* 2387 */           f11 = paramArrayOfFloat[f10];
/* 2388 */           if (f11 == f7) {
/* 2389 */             paramArrayOfFloat[f10] = paramArrayOfFloat[f5];
/* 2390 */             paramArrayOfFloat[f5] = f11;
/* 2391 */             f5++;
/* 2392 */           } else if (f11 == f8) {
/* 2393 */             while (paramArrayOfFloat[f6] == f8) {
/* 2394 */               if (f6-- == f10) {
/* 2395 */                 break label1067;
/*      */               }
/*      */             }
/* 2398 */             if (paramArrayOfFloat[f6] == f7) {
/* 2399 */               paramArrayOfFloat[f10] = paramArrayOfFloat[f5];
/*      */ 
/* 2408 */               paramArrayOfFloat[f5] = paramArrayOfFloat[f6];
/* 2409 */               f5++;
/*      */             } else {
/* 2411 */               paramArrayOfFloat[f10] = paramArrayOfFloat[f6];
/*      */             }
/* 2413 */             paramArrayOfFloat[f6] = f11;
/* 2414 */             f6--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2420 */       label1067: sort(paramArrayOfFloat, f5, f6, false);
/*      */     }
/*      */     else
/*      */     {
/* 2427 */       f7 = paramArrayOfFloat[n];
/*      */ 
/* 2449 */       for (f8 = f5; f8 <= f6; f8++) {
/* 2450 */         if (paramArrayOfFloat[f8] != f7)
/*      */         {
/* 2453 */           f10 = paramArrayOfFloat[f8];
/* 2454 */           if (f10 < f7) {
/* 2455 */             paramArrayOfFloat[f8] = paramArrayOfFloat[f5];
/* 2456 */             paramArrayOfFloat[f5] = f10;
/* 2457 */             f5++;
/*      */           } else {
/* 2459 */             while (paramArrayOfFloat[f6] > f7) {
/* 2460 */               f6--;
/*      */             }
/* 2462 */             if (paramArrayOfFloat[f6] < f7) {
/* 2463 */               paramArrayOfFloat[f8] = paramArrayOfFloat[f5];
/* 2464 */               paramArrayOfFloat[f5] = paramArrayOfFloat[f6];
/* 2465 */               f5++;
/*      */             }
/*      */             else
/*      */             {
/* 2475 */               paramArrayOfFloat[f8] = paramArrayOfFloat[f6];
/*      */             }
/* 2477 */             paramArrayOfFloat[f6] = f10;
/* 2478 */             f6--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2487 */       sort(paramArrayOfFloat, paramInt1, f5 - 1, paramBoolean);
/* 2488 */       sort(paramArrayOfFloat, f6 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void sort(double[] paramArrayOfDouble)
/*      */   {
/* 2498 */     sort(paramArrayOfDouble, 0, paramArrayOfDouble.length - 1);
/*      */   }
/*      */ 
/*      */   public static void sort(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/* 2512 */     while ((paramInt1 <= paramInt2) && (Double.isNaN(paramArrayOfDouble[paramInt2]))) {
/* 2513 */       paramInt2--;
/*      */     }
/* 2515 */     int i = paramInt2;
/*      */     while (true) { i--; if (i < paramInt1) break;
/* 2516 */       double d1 = paramArrayOfDouble[i];
/* 2517 */       if (d1 != d1) {
/* 2518 */         paramArrayOfDouble[i] = paramArrayOfDouble[paramInt2];
/* 2519 */         paramArrayOfDouble[paramInt2] = d1;
/* 2520 */         paramInt2--;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2527 */     doSort(paramArrayOfDouble, paramInt1, paramInt2);
/*      */ 
/* 2532 */     i = paramInt2;
/*      */ 
/* 2537 */     while (paramInt1 < i) {
/* 2538 */       j = paramInt1 + i >>> 1;
/* 2539 */       double d2 = paramArrayOfDouble[j];
/*      */ 
/* 2541 */       if (d2 < 0.0D)
/* 2542 */         paramInt1 = j + 1;
/*      */       else {
/* 2544 */         i = j;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2551 */     while ((paramInt1 <= paramInt2) && (Double.doubleToRawLongBits(paramArrayOfDouble[paramInt1]) < 0L)) {
/* 2552 */       paramInt1++;
/*      */     }
/*      */ 
/* 2576 */     int j = paramInt1; int k = paramInt1 - 1;
/*      */     while (true) { j++; if (j > paramInt2) break;
/* 2577 */       double d3 = paramArrayOfDouble[j];
/* 2578 */       if (d3 != 0.0D) {
/*      */         break;
/*      */       }
/* 2581 */       if (Double.doubleToRawLongBits(d3) < 0L) {
/* 2582 */         paramArrayOfDouble[j] = 0.0D;
/* 2583 */         paramArrayOfDouble[(++k)] = -0.0D;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void doSort(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/* 2597 */     if (paramInt2 - paramInt1 < 286) {
/* 2598 */       sort(paramArrayOfDouble, paramInt1, paramInt2, true);
/* 2599 */       return;
/*      */     }
/*      */ 
/* 2606 */     int[] arrayOfInt = new int[68];
/* 2607 */     int i = 0; arrayOfInt[0] = paramInt1;
/*      */ 
/* 2610 */     for (int j = paramInt1; j < paramInt2; arrayOfInt[i] = j) {
/* 2611 */       if (paramArrayOfDouble[j] < paramArrayOfDouble[(j + 1)]) while (true) {
/* 2612 */           j++; if ((j > paramInt2) || (paramArrayOfDouble[(j - 1)] > paramArrayOfDouble[j])) break;
/*      */         } if (paramArrayOfDouble[j] > paramArrayOfDouble[(j + 1)]) {
/*      */         do j++; while ((j <= paramInt2) && (paramArrayOfDouble[(j - 1)] >= paramArrayOfDouble[j]));
/* 2615 */         k = arrayOfInt[i] - 1; m = j;
/*      */         while (true) { k++; if (k >= --m) break;
/* 2616 */           double d = paramArrayOfDouble[k]; paramArrayOfDouble[k] = paramArrayOfDouble[m]; paramArrayOfDouble[m] = d; }
/*      */       }
/*      */       else {
/* 2619 */         k = 33;
/*      */         do { j++; if ((j > paramInt2) || (paramArrayOfDouble[(j - 1)] != paramArrayOfDouble[j])) break;
/* 2620 */           k--; } while (k != 0);
/* 2621 */         sort(paramArrayOfDouble, paramInt1, paramInt2, true);
/* 2622 */         return;
/*      */       }
/*      */ 
/* 2631 */       i++; if (i == 67) {
/* 2632 */         sort(paramArrayOfDouble, paramInt1, paramInt2, true);
/* 2633 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2638 */     if (arrayOfInt[i] == paramInt2++)
/* 2639 */       arrayOfInt[(++i)] = paramInt2;
/* 2640 */     else if (i == 1) {
/* 2641 */       return;
/*      */     }
/*      */ 
/* 2648 */     int k = 0;
/* 2649 */     for (int m = 1; m <<= 1 < i; k = (byte)(k ^ 0x1));
/*      */     Object localObject;
/* 2651 */     if (k == 0) {
/* 2652 */       localObject = paramArrayOfDouble; paramArrayOfDouble = new double[localObject.length];
/* 2653 */       for (m = paramInt1 - 1; ; paramArrayOfDouble[m] = localObject[m]) { m++; if (m >= paramInt2) break; }
/*      */     } else {
/* 2655 */       localObject = new double[paramArrayOfDouble.length];
/*      */     }
/*      */ 
/* 2659 */     for (; i > 1; i = m)
/*      */     {
/*      */       int i1;
/* 2660 */       for (int n = (m = 0) + 2; n <= i; n += 2) {
/* 2661 */         i1 = arrayOfInt[n]; int i2 = arrayOfInt[(n - 1)];
/* 2662 */         int i3 = arrayOfInt[(n - 2)]; int i4 = i3; for (int i5 = i2; i3 < i1; i3++) {
/* 2663 */           if ((i5 >= i1) || ((i4 < i2) && (paramArrayOfDouble[i4] <= paramArrayOfDouble[i5])))
/* 2664 */             localObject[i3] = paramArrayOfDouble[(i4++)];
/*      */           else {
/* 2666 */             localObject[i3] = paramArrayOfDouble[(i5++)];
/*      */           }
/*      */         }
/* 2669 */         arrayOfInt[(++m)] = i1;
/*      */       }
/* 2671 */       if ((i & 0x1) != 0) {
/* 2672 */         n = paramInt2; i1 = arrayOfInt[(i - 1)];
/*      */         while (true) { n--; if (n < i1) break;
/* 2673 */           localObject[n] = paramArrayOfDouble[n];
/*      */         }
/* 2675 */         arrayOfInt[(++m)] = paramInt2;
/*      */       }
/* 2677 */       double[] arrayOfDouble = paramArrayOfDouble; paramArrayOfDouble = (double[])localObject; localObject = arrayOfDouble;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void sort(double[] paramArrayOfDouble, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2690 */     int i = paramInt2 - paramInt1 + 1;
/*      */ 
/* 2693 */     if (i < 47)
/*      */     {
/*      */       int j;
/* 2694 */       if (paramBoolean)
/*      */       {
/* 2700 */         j = paramInt1; for (int m = j; j < paramInt2; m = j) {
/* 2701 */           double d3 = paramArrayOfDouble[(j + 1)];
/* 2702 */           while (d3 < paramArrayOfDouble[m]) {
/* 2703 */             paramArrayOfDouble[(m + 1)] = paramArrayOfDouble[m];
/* 2704 */             if (m-- == paramInt1) {
/* 2705 */               break;
/*      */             }
/*      */           }
/* 2708 */           paramArrayOfDouble[(m + 1)] = d3;
/*      */ 
/* 2700 */           j++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */         do
/*      */         {
/* 2715 */           if (paramInt1 >= paramInt2)
/* 2716 */             return;
/*      */         }
/* 2718 */         while (paramArrayOfDouble[(++paramInt1)] >= paramArrayOfDouble[(paramInt1 - 1)]);
/*      */ 
/* 2728 */         for (j = paramInt1; ; j = paramInt1) { paramInt1++; if (paramInt1 > paramInt2) break;
/* 2729 */           double d2 = paramArrayOfDouble[j]; double d4 = paramArrayOfDouble[paramInt1];
/*      */ 
/* 2731 */           if (d2 < d4) {
/* 2732 */             d4 = d2; d2 = paramArrayOfDouble[paramInt1];
/*      */           }
/* 2734 */           while (d2 < paramArrayOfDouble[(--j)]) {
/* 2735 */             paramArrayOfDouble[(j + 2)] = paramArrayOfDouble[j];
/*      */           }
/* 2737 */           paramArrayOfDouble[(++j + 1)] = d2;
/*      */ 
/* 2739 */           while (d4 < paramArrayOfDouble[(--j)]) {
/* 2740 */             paramArrayOfDouble[(j + 1)] = paramArrayOfDouble[j];
/*      */           }
/* 2742 */           paramArrayOfDouble[(j + 1)] = d4;
/*      */ 
/* 2728 */           paramInt1++;
/*      */         }
/*      */ 
/* 2744 */         double d1 = paramArrayOfDouble[paramInt2];
/*      */ 
/* 2746 */         while (d1 < paramArrayOfDouble[(--paramInt2)]) {
/* 2747 */           paramArrayOfDouble[(paramInt2 + 1)] = paramArrayOfDouble[paramInt2];
/*      */         }
/* 2749 */         paramArrayOfDouble[(paramInt2 + 1)] = d1;
/*      */       }
/* 2751 */       return;
/*      */     }
/*      */ 
/* 2755 */     int k = (i >> 3) + (i >> 6) + 1;
/*      */ 
/* 2764 */     int n = paramInt1 + paramInt2 >>> 1;
/* 2765 */     int i1 = n - k;
/* 2766 */     int i2 = i1 - k;
/* 2767 */     int i3 = n + k;
/* 2768 */     int i4 = i3 + k;
/*      */     double d5;
/* 2771 */     if (paramArrayOfDouble[i1] < paramArrayOfDouble[i2]) { d5 = paramArrayOfDouble[i1]; paramArrayOfDouble[i1] = paramArrayOfDouble[i2]; paramArrayOfDouble[i2] = d5;
/*      */     }
/* 2773 */     if (paramArrayOfDouble[n] < paramArrayOfDouble[i1]) { d5 = paramArrayOfDouble[n]; paramArrayOfDouble[n] = paramArrayOfDouble[i1]; paramArrayOfDouble[i1] = d5;
/* 2774 */       if (d5 < paramArrayOfDouble[i2]) { paramArrayOfDouble[i1] = paramArrayOfDouble[i2]; paramArrayOfDouble[i2] = d5; }
/*      */     }
/* 2776 */     if (paramArrayOfDouble[i3] < paramArrayOfDouble[n]) { d5 = paramArrayOfDouble[i3]; paramArrayOfDouble[i3] = paramArrayOfDouble[n]; paramArrayOfDouble[n] = d5;
/* 2777 */       if (d5 < paramArrayOfDouble[i1]) { paramArrayOfDouble[n] = paramArrayOfDouble[i1]; paramArrayOfDouble[i1] = d5;
/* 2778 */         if (d5 < paramArrayOfDouble[i2]) { paramArrayOfDouble[i1] = paramArrayOfDouble[i2]; paramArrayOfDouble[i2] = d5; }
/*      */       }
/*      */     }
/* 2781 */     if (paramArrayOfDouble[i4] < paramArrayOfDouble[i3]) { d5 = paramArrayOfDouble[i4]; paramArrayOfDouble[i4] = paramArrayOfDouble[i3]; paramArrayOfDouble[i3] = d5;
/* 2782 */       if (d5 < paramArrayOfDouble[n]) { paramArrayOfDouble[i3] = paramArrayOfDouble[n]; paramArrayOfDouble[n] = d5;
/* 2783 */         if (d5 < paramArrayOfDouble[i1]) { paramArrayOfDouble[n] = paramArrayOfDouble[i1]; paramArrayOfDouble[i1] = d5;
/* 2784 */           if (d5 < paramArrayOfDouble[i2]) { paramArrayOfDouble[i1] = paramArrayOfDouble[i2]; paramArrayOfDouble[i2] = d5;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2790 */     double d6 = paramInt1;
/* 2791 */     double d7 = paramInt2;
/*      */     double d8;
/*      */     double d9;
/* 2793 */     if ((paramArrayOfDouble[i2] != paramArrayOfDouble[i1]) && (paramArrayOfDouble[i1] != paramArrayOfDouble[n]) && (paramArrayOfDouble[n] != paramArrayOfDouble[i3]) && (paramArrayOfDouble[i3] != paramArrayOfDouble[i4]))
/*      */     {
/* 2799 */       d8 = paramArrayOfDouble[i1];
/* 2800 */       d9 = paramArrayOfDouble[i3];
/*      */ 
/* 2808 */       paramArrayOfDouble[i1] = paramArrayOfDouble[paramInt1];
/* 2809 */       paramArrayOfDouble[i3] = paramArrayOfDouble[paramInt2];
/*      */ 
/* 2814 */       while (paramArrayOfDouble[(++d6)] < d8);
/* 2815 */       while (paramArrayOfDouble[(--d7)] > d9);
/* 2837 */       double d11 = d6 - 1;
/*      */       double d13;
/*      */       while (true)
/*      */       {
/* 2837 */         d11++; if (d11 > d7) break;
/* 2838 */         d13 = paramArrayOfDouble[d11];
/* 2839 */         if (d13 < d8) {
/* 2840 */           paramArrayOfDouble[d11] = paramArrayOfDouble[d6];
/*      */ 
/* 2845 */           paramArrayOfDouble[d6] = d13;
/* 2846 */           d6++;
/* 2847 */         } else if (d13 > d9) {
/* 2848 */           while (paramArrayOfDouble[d7] > d9) {
/* 2849 */             if (d7-- == d11) {
/* 2850 */               break label834;
/*      */             }
/*      */           }
/* 2853 */           if (paramArrayOfDouble[d7] < d8) {
/* 2854 */             paramArrayOfDouble[d11] = paramArrayOfDouble[d6];
/* 2855 */             paramArrayOfDouble[d6] = paramArrayOfDouble[d7];
/* 2856 */             d6++;
/*      */           } else {
/* 2858 */             paramArrayOfDouble[d11] = paramArrayOfDouble[d7];
/*      */           }
/*      */ 
/* 2864 */           paramArrayOfDouble[d7] = d13;
/* 2865 */           d7--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2870 */       label834: paramArrayOfDouble[paramInt1] = paramArrayOfDouble[(d6 - 1)]; paramArrayOfDouble[(d6 - 1)] = d8;
/* 2871 */       paramArrayOfDouble[paramInt2] = paramArrayOfDouble[(d7 + 1)]; paramArrayOfDouble[(d7 + 1)] = d9;
/*      */ 
/* 2874 */       sort(paramArrayOfDouble, paramInt1, d6 - 2, paramBoolean);
/* 2875 */       sort(paramArrayOfDouble, d7 + 2, paramInt2, false);
/*      */ 
/* 2881 */       if ((d6 < i2) && (i4 < d7))
/*      */       {
/* 2885 */         while (paramArrayOfDouble[d6] == d8) {
/* 2886 */           d6++;
/*      */         }
/*      */ 
/* 2889 */         while (paramArrayOfDouble[d7] == d9) {
/* 2890 */           d7--;
/*      */         }
/*      */ 
/* 2913 */         double d12 = d6 - 1;
/*      */         while (true) { d12++; if (d12 > d7) break;
/* 2914 */           d13 = paramArrayOfDouble[d12];
/* 2915 */           if (d13 == d8) {
/* 2916 */             paramArrayOfDouble[d12] = paramArrayOfDouble[d6];
/* 2917 */             paramArrayOfDouble[d6] = d13;
/* 2918 */             d6++;
/* 2919 */           } else if (d13 == d9) {
/* 2920 */             while (paramArrayOfDouble[d7] == d9) {
/* 2921 */               if (d7-- == d12) {
/* 2922 */                 break label1067;
/*      */               }
/*      */             }
/* 2925 */             if (paramArrayOfDouble[d7] == d8) {
/* 2926 */               paramArrayOfDouble[d12] = paramArrayOfDouble[d6];
/*      */ 
/* 2935 */               paramArrayOfDouble[d6] = paramArrayOfDouble[d7];
/* 2936 */               d6++;
/*      */             } else {
/* 2938 */               paramArrayOfDouble[d12] = paramArrayOfDouble[d7];
/*      */             }
/* 2940 */             paramArrayOfDouble[d7] = d13;
/* 2941 */             d7--;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2947 */       label1067: sort(paramArrayOfDouble, d6, d7, false);
/*      */     }
/*      */     else
/*      */     {
/* 2954 */       d8 = paramArrayOfDouble[n];
/*      */ 
/* 2976 */       for (d9 = d6; d9 <= d7; d9++) {
/* 2977 */         if (paramArrayOfDouble[d9] != d8)
/*      */         {
/* 2980 */           double d10 = paramArrayOfDouble[d9];
/* 2981 */           if (d10 < d8) {
/* 2982 */             paramArrayOfDouble[d9] = paramArrayOfDouble[d6];
/* 2983 */             paramArrayOfDouble[d6] = d10;
/* 2984 */             d6++;
/*      */           } else {
/* 2986 */             while (paramArrayOfDouble[d7] > d8) {
/* 2987 */               d7--;
/*      */             }
/* 2989 */             if (paramArrayOfDouble[d7] < d8) {
/* 2990 */               paramArrayOfDouble[d9] = paramArrayOfDouble[d6];
/* 2991 */               paramArrayOfDouble[d6] = paramArrayOfDouble[d7];
/* 2992 */               d6++;
/*      */             }
/*      */             else
/*      */             {
/* 3002 */               paramArrayOfDouble[d9] = paramArrayOfDouble[d7];
/*      */             }
/* 3004 */             paramArrayOfDouble[d7] = d10;
/* 3005 */             d7--;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3014 */       sort(paramArrayOfDouble, paramInt1, d6 - 1, paramBoolean);
/* 3015 */       sort(paramArrayOfDouble, d7 + 1, paramInt2, false);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.DualPivotQuicksort
 * JD-Core Version:    0.6.2
 */