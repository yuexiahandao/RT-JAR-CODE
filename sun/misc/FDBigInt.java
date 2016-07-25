/*      */ package sun.misc;
/*      */ 
/*      */ class FDBigInt
/*      */ {
/*      */   int nWords;
/*      */   int[] data;
/*      */ 
/*      */   public FDBigInt(int paramInt)
/*      */   {
/* 2436 */     this.nWords = 1;
/* 2437 */     this.data = new int[1];
/* 2438 */     this.data[0] = paramInt;
/*      */   }
/*      */ 
/*      */   public FDBigInt(long paramLong) {
/* 2442 */     this.data = new int[2];
/* 2443 */     this.data[0] = ((int)paramLong);
/* 2444 */     this.data[1] = ((int)(paramLong >>> 32));
/* 2445 */     this.nWords = (this.data[1] == 0 ? 1 : 2);
/*      */   }
/*      */ 
/*      */   public FDBigInt(FDBigInt paramFDBigInt) {
/* 2449 */     this.data = new int[this.nWords = paramFDBigInt.nWords];
/* 2450 */     System.arraycopy(paramFDBigInt.data, 0, this.data, 0, this.nWords);
/*      */   }
/*      */ 
/*      */   private FDBigInt(int[] paramArrayOfInt, int paramInt) {
/* 2454 */     this.data = paramArrayOfInt;
/* 2455 */     this.nWords = paramInt;
/*      */   }
/*      */ 
/*      */   public FDBigInt(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 2459 */     int i = (paramInt2 + 8) / 9;
/* 2460 */     if (i < 2) i = 2;
/* 2461 */     this.data = new int[i];
/* 2462 */     this.data[0] = ((int)paramLong);
/* 2463 */     this.data[1] = ((int)(paramLong >>> 32));
/* 2464 */     this.nWords = (this.data[1] == 0 ? 1 : 2);
/* 2465 */     int j = paramInt1;
/* 2466 */     int k = paramInt2 - 5;
/*      */ 
/* 2468 */     while (j < k) {
/* 2469 */       n = j + 5;
/* 2470 */       m = paramArrayOfChar[(j++)] - '0';
/* 2471 */       while (j < n) {
/* 2472 */         m = 10 * m + paramArrayOfChar[(j++)] - 48;
/*      */       }
/* 2474 */       multaddMe(100000, m);
/*      */     }
/* 2476 */     int n = 1;
/* 2477 */     int m = 0;
/* 2478 */     while (j < paramInt2) {
/* 2479 */       m = 10 * m + paramArrayOfChar[(j++)] - 48;
/* 2480 */       n *= 10;
/*      */     }
/* 2482 */     if (n != 1)
/* 2483 */       multaddMe(n, m);
/*      */   }
/*      */ 
/*      */   public void lshiftMe(int paramInt)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2493 */     if (paramInt <= 0) {
/* 2494 */       if (paramInt == 0) {
/* 2495 */         return;
/*      */       }
/* 2497 */       throw new IllegalArgumentException("negative shift count");
/*      */     }
/* 2499 */     int i = paramInt >> 5;
/* 2500 */     int j = paramInt & 0x1F;
/* 2501 */     int k = 32 - j;
/* 2502 */     int[] arrayOfInt1 = this.data;
/* 2503 */     int[] arrayOfInt2 = this.data;
/* 2504 */     if (this.nWords + i + 1 > arrayOfInt1.length)
/*      */     {
/* 2506 */       arrayOfInt1 = new int[this.nWords + i + 1];
/*      */     }
/* 2508 */     int m = this.nWords + i;
/* 2509 */     int n = this.nWords - 1;
/* 2510 */     if (j == 0)
/*      */     {
/* 2512 */       System.arraycopy(arrayOfInt2, 0, arrayOfInt1, i, this.nWords);
/* 2513 */       m = i - 1;
/*      */     } else {
/* 2515 */       arrayOfInt1[(m--)] = (arrayOfInt2[n] >>> k);
/* 2516 */       while (n >= 1) {
/* 2517 */         arrayOfInt1[(m--)] = (arrayOfInt2[n] << j | arrayOfInt2[(--n)] >>> k);
/*      */       }
/* 2519 */       arrayOfInt1[(m--)] = (arrayOfInt2[n] << j);
/*      */     }
/* 2521 */     while (m >= 0) {
/* 2522 */       arrayOfInt1[(m--)] = 0;
/*      */     }
/* 2524 */     this.data = arrayOfInt1;
/* 2525 */     this.nWords += i + 1;
/*      */ 
/* 2528 */     while ((this.nWords > 1) && (this.data[(this.nWords - 1)] == 0))
/* 2529 */       this.nWords -= 1;
/*      */   }
/*      */ 
/*      */   public int normalizeMe()
/*      */     throws IllegalArgumentException
/*      */   {
/* 2546 */     int j = 0;
/* 2547 */     int k = 0;
/* 2548 */     int m = 0;
/* 2549 */     for (int i = this.nWords - 1; (i >= 0) && ((m = this.data[i]) == 0); i--) {
/* 2550 */       j++;
/*      */     }
/* 2552 */     if (i < 0)
/*      */     {
/* 2554 */       throw new IllegalArgumentException("zero value");
/*      */     }
/*      */ 
/* 2562 */     this.nWords -= j;
/*      */ 
/* 2568 */     if ((m & 0xF0000000) != 0)
/*      */     {
/* 2571 */       for (k = 32; (m & 0xF0000000) != 0; k--)
/* 2572 */         m >>>= 1;
/*      */     }
/* 2574 */     while (m <= 1048575)
/*      */     {
/* 2576 */       m <<= 8;
/* 2577 */       k += 8;
/*      */     }
/* 2579 */     while (m <= 134217727) {
/* 2580 */       m <<= 1;
/* 2581 */       k++;
/*      */     }
/*      */ 
/* 2584 */     if (k != 0)
/* 2585 */       lshiftMe(k);
/* 2586 */     return k;
/*      */   }
/*      */ 
/*      */   public FDBigInt mult(int paramInt)
/*      */   {
/* 2595 */     long l1 = paramInt;
/*      */ 
/* 2600 */     int[] arrayOfInt = new int[l1 * (this.data[(this.nWords - 1)] & 0xFFFFFFFF) > 268435455L ? this.nWords + 1 : this.nWords];
/* 2601 */     long l2 = 0L;
/* 2602 */     for (int i = 0; i < this.nWords; i++) {
/* 2603 */       l2 += l1 * (this.data[i] & 0xFFFFFFFF);
/* 2604 */       arrayOfInt[i] = ((int)l2);
/* 2605 */       l2 >>>= 32;
/*      */     }
/* 2607 */     if (l2 == 0L) {
/* 2608 */       return new FDBigInt(arrayOfInt, this.nWords);
/*      */     }
/* 2610 */     arrayOfInt[this.nWords] = ((int)l2);
/* 2611 */     return new FDBigInt(arrayOfInt, this.nWords + 1);
/*      */   }
/*      */ 
/*      */   public void multaddMe(int paramInt1, int paramInt2)
/*      */   {
/* 2622 */     long l1 = paramInt1;
/*      */ 
/* 2626 */     long l2 = l1 * (this.data[0] & 0xFFFFFFFF) + (paramInt2 & 0xFFFFFFFF);
/* 2627 */     this.data[0] = ((int)l2);
/* 2628 */     l2 >>>= 32;
/* 2629 */     for (int i = 1; i < this.nWords; i++) {
/* 2630 */       l2 += l1 * (this.data[i] & 0xFFFFFFFF);
/* 2631 */       this.data[i] = ((int)l2);
/* 2632 */       l2 >>>= 32;
/*      */     }
/* 2634 */     if (l2 != 0L) {
/* 2635 */       this.data[this.nWords] = ((int)l2);
/* 2636 */       this.nWords += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public FDBigInt mult(FDBigInt paramFDBigInt)
/*      */   {
/* 2647 */     int[] arrayOfInt = new int[this.nWords + paramFDBigInt.nWords];
/*      */ 
/* 2651 */     for (int i = 0; i < this.nWords; i++) {
/* 2652 */       long l1 = this.data[i] & 0xFFFFFFFF;
/* 2653 */       long l2 = 0L;
/*      */ 
/* 2655 */       for (int j = 0; j < paramFDBigInt.nWords; j++) {
/* 2656 */         l2 += (arrayOfInt[(i + j)] & 0xFFFFFFFF) + l1 * (paramFDBigInt.data[j] & 0xFFFFFFFF);
/* 2657 */         arrayOfInt[(i + j)] = ((int)l2);
/* 2658 */         l2 >>>= 32;
/*      */       }
/* 2660 */       arrayOfInt[(i + j)] = ((int)l2);
/*      */     }
/*      */ 
/* 2663 */     for (i = arrayOfInt.length - 1; (i > 0) && 
/* 2664 */       (arrayOfInt[i] == 0); i--);
/* 2666 */     return new FDBigInt(arrayOfInt, i + 1);
/*      */   }
/*      */ 
/*      */   public FDBigInt add(FDBigInt paramFDBigInt)
/*      */   {
/* 2677 */     long l = 0L;
/*      */     int[] arrayOfInt1;
/*      */     int j;
/*      */     int[] arrayOfInt2;
/*      */     int k;
/* 2680 */     if (this.nWords >= paramFDBigInt.nWords) {
/* 2681 */       arrayOfInt1 = this.data;
/* 2682 */       j = this.nWords;
/* 2683 */       arrayOfInt2 = paramFDBigInt.data;
/* 2684 */       k = paramFDBigInt.nWords;
/*      */     } else {
/* 2686 */       arrayOfInt1 = paramFDBigInt.data;
/* 2687 */       j = paramFDBigInt.nWords;
/* 2688 */       arrayOfInt2 = this.data;
/* 2689 */       k = this.nWords;
/*      */     }
/* 2691 */     int[] arrayOfInt3 = new int[j];
/* 2692 */     for (int i = 0; i < j; i++) {
/* 2693 */       l += (arrayOfInt1[i] & 0xFFFFFFFF);
/* 2694 */       if (i < k) {
/* 2695 */         l += (arrayOfInt2[i] & 0xFFFFFFFF);
/*      */       }
/* 2697 */       arrayOfInt3[i] = ((int)l);
/* 2698 */       l >>= 32;
/*      */     }
/* 2700 */     if (l != 0L)
/*      */     {
/* 2702 */       int[] arrayOfInt4 = new int[arrayOfInt3.length + 1];
/* 2703 */       System.arraycopy(arrayOfInt3, 0, arrayOfInt4, 0, arrayOfInt3.length);
/* 2704 */       arrayOfInt4[(i++)] = ((int)l);
/* 2705 */       return new FDBigInt(arrayOfInt4, i);
/*      */     }
/* 2707 */     return new FDBigInt(arrayOfInt3, i);
/*      */   }
/*      */ 
/*      */   public FDBigInt sub(FDBigInt paramFDBigInt)
/*      */   {
/* 2716 */     int[] arrayOfInt = new int[this.nWords];
/*      */ 
/* 2718 */     int j = this.nWords;
/* 2719 */     int k = paramFDBigInt.nWords;
/* 2720 */     int m = 0;
/* 2721 */     long l = 0L;
/* 2722 */     for (int i = 0; i < j; i++) {
/* 2723 */       l += (this.data[i] & 0xFFFFFFFF);
/* 2724 */       if (i < k) {
/* 2725 */         l -= (paramFDBigInt.data[i] & 0xFFFFFFFF);
/*      */       }
/* 2727 */       if ((arrayOfInt[i] = (int)l) == 0)
/* 2728 */         m++;
/*      */       else
/* 2730 */         m = 0;
/* 2731 */       l >>= 32;
/*      */     }
/* 2733 */     assert (l == 0L) : l;
/* 2734 */     assert (dataInRangeIsZero(i, k, paramFDBigInt));
/* 2735 */     return new FDBigInt(arrayOfInt, j - m);
/*      */   }
/*      */ 
/*      */   private static boolean dataInRangeIsZero(int paramInt1, int paramInt2, FDBigInt paramFDBigInt) {
/* 2739 */     while (paramInt1 < paramInt2)
/* 2740 */       if (paramFDBigInt.data[(paramInt1++)] != 0)
/* 2741 */         return false;
/* 2742 */     return true;
/*      */   }
/*      */ 
/*      */   public int cmp(FDBigInt paramFDBigInt)
/*      */   {
/*      */     int i;
/* 2754 */     if (this.nWords > paramFDBigInt.nWords)
/*      */     {
/* 2757 */       j = paramFDBigInt.nWords - 1;
/* 2758 */       for (i = this.nWords - 1; i > j; i--)
/* 2759 */         if (this.data[i] != 0) return 1; 
/*      */     }
/* 2760 */     else if (this.nWords < paramFDBigInt.nWords)
/*      */     {
/* 2763 */       j = this.nWords - 1;
/* 2764 */       for (i = paramFDBigInt.nWords - 1; i > j; i--)
/* 2765 */         if (paramFDBigInt.data[i] != 0) return -1; 
/*      */     }
/* 2767 */     else { i = this.nWords - 1; }
/*      */ 
/* 2769 */     while ((i > 0) && 
/* 2770 */       (this.data[i] == paramFDBigInt.data[i])) {
/* 2769 */       i--;
/*      */     }
/*      */ 
/* 2774 */     int j = this.data[i];
/* 2775 */     int k = paramFDBigInt.data[i];
/* 2776 */     if (j < 0)
/*      */     {
/* 2778 */       if (k < 0) {
/* 2779 */         return j - k;
/*      */       }
/* 2781 */       return 1;
/*      */     }
/*      */ 
/* 2785 */     if (k < 0)
/*      */     {
/* 2787 */       return -1;
/*      */     }
/* 2789 */     return j - k;
/*      */   }
/*      */ 
/*      */   public int quoRemIteration(FDBigInt paramFDBigInt)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2810 */     if (this.nWords != paramFDBigInt.nWords) {
/* 2811 */       throw new IllegalArgumentException("disparate values");
/*      */     }
/*      */ 
/* 2816 */     int i = this.nWords - 1;
/* 2817 */     long l1 = (this.data[i] & 0xFFFFFFFF) / paramFDBigInt.data[i];
/* 2818 */     long l2 = 0L;
/* 2819 */     for (int j = 0; j <= i; j++) {
/* 2820 */       l2 += (this.data[j] & 0xFFFFFFFF) - l1 * (paramFDBigInt.data[j] & 0xFFFFFFFF);
/* 2821 */       this.data[j] = ((int)l2);
/* 2822 */       l2 >>= 32;
/*      */     }
/* 2824 */     if (l2 != 0L)
/*      */     {
/* 2828 */       l3 = 0L;
/* 2829 */       while (l3 == 0L) {
/* 2830 */         l3 = 0L;
/* 2831 */         for (k = 0; k <= i; k++) {
/* 2832 */           l3 += (this.data[k] & 0xFFFFFFFF) + (paramFDBigInt.data[k] & 0xFFFFFFFF);
/* 2833 */           this.data[k] = ((int)l3);
/* 2834 */           l3 >>= 32;
/*      */         }
/*      */ 
/* 2845 */         assert ((l3 == 0L) || (l3 == 1L)) : l3;
/* 2846 */         l1 -= 1L;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2852 */     long l3 = 0L;
/* 2853 */     for (int k = 0; k <= i; k++) {
/* 2854 */       l3 += 10L * (this.data[k] & 0xFFFFFFFF);
/* 2855 */       this.data[k] = ((int)l3);
/* 2856 */       l3 >>= 32;
/*      */     }
/* 2858 */     assert (l3 == 0L) : l3;
/* 2859 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   public long longValue()
/*      */   {
/* 2865 */     assert (this.nWords > 0) : this.nWords;
/*      */ 
/* 2867 */     if (this.nWords == 1) {
/* 2868 */       return this.data[0] & 0xFFFFFFFF;
/*      */     }
/* 2870 */     assert (dataInRangeIsZero(2, this.nWords, this));
/* 2871 */     assert (this.data[1] >= 0);
/* 2872 */     return this.data[1] << 32 | this.data[0] & 0xFFFFFFFF;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2877 */     StringBuffer localStringBuffer = new StringBuffer(30);
/* 2878 */     localStringBuffer.append('[');
/* 2879 */     int i = Math.min(this.nWords - 1, this.data.length - 1);
/* 2880 */     if (this.nWords > this.data.length) {
/* 2881 */       localStringBuffer.append("(" + this.data.length + "<" + this.nWords + "!)");
/*      */     }
/* 2883 */     for (; i > 0; i--) {
/* 2884 */       localStringBuffer.append(Integer.toHexString(this.data[i]));
/* 2885 */       localStringBuffer.append(' ');
/*      */     }
/* 2887 */     localStringBuffer.append(Integer.toHexString(this.data[0]));
/* 2888 */     localStringBuffer.append(']');
/* 2889 */     return new String(localStringBuffer);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FDBigInt
 * JD-Core Version:    0.6.2
 */