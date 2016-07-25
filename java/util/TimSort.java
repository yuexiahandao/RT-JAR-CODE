/*     */ package java.util;
/*     */ 
/*     */ class TimSort<T>
/*     */ {
/*     */   private static final int MIN_MERGE = 32;
/*     */   private final T[] a;
/*     */   private final Comparator<? super T> c;
/*     */   private static final int MIN_GALLOP = 7;
/* 102 */   private int minGallop = 7;
/*     */   private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
/*     */   private T[] tmp;
/* 128 */   private int stackSize = 0;
/*     */   private final int[] runBase;
/*     */   private final int[] runLen;
/*     */ 
/*     */   private TimSort(T[] paramArrayOfT, Comparator<? super T> paramComparator)
/*     */   {
/* 139 */     this.a = paramArrayOfT;
/* 140 */     this.c = paramComparator;
/*     */ 
/* 143 */     int i = paramArrayOfT.length;
/*     */ 
/* 145 */     Object[] arrayOfObject = (Object[])new Object[i < 512 ? i >>> 1 : 256];
/*     */ 
/* 147 */     this.tmp = arrayOfObject;
/*     */ 
/* 159 */     int j = i < 119151 ? 24 : i < 1542 ? 10 : i < 120 ? 5 : 40;
/*     */ 
/* 162 */     this.runBase = new int[j];
/* 163 */     this.runLen = new int[j];
/*     */   }
/*     */ 
/*     */   static <T> void sort(T[] paramArrayOfT, Comparator<? super T> paramComparator)
/*     */   {
/* 173 */     sort(paramArrayOfT, 0, paramArrayOfT.length, paramComparator);
/*     */   }
/*     */ 
/*     */   static <T> void sort(T[] paramArrayOfT, int paramInt1, int paramInt2, Comparator<? super T> paramComparator) {
/* 177 */     if (paramComparator == null) {
/* 178 */       Arrays.sort(paramArrayOfT, paramInt1, paramInt2);
/* 179 */       return;
/*     */     }
/*     */ 
/* 182 */     rangeCheck(paramArrayOfT.length, paramInt1, paramInt2);
/* 183 */     int i = paramInt2 - paramInt1;
/* 184 */     if (i < 2) {
/* 185 */       return;
/*     */     }
/*     */ 
/* 188 */     if (i < 32) {
/* 189 */       int j = countRunAndMakeAscending(paramArrayOfT, paramInt1, paramInt2, paramComparator);
/* 190 */       binarySort(paramArrayOfT, paramInt1, paramInt2, paramInt1 + j, paramComparator);
/* 191 */       return;
/*     */     }
/*     */ 
/* 199 */     TimSort localTimSort = new TimSort(paramArrayOfT, paramComparator);
/* 200 */     int k = minRunLength(i);
/*     */     do
/*     */     {
/* 203 */       int m = countRunAndMakeAscending(paramArrayOfT, paramInt1, paramInt2, paramComparator);
/*     */ 
/* 206 */       if (m < k) {
/* 207 */         int n = i <= k ? i : k;
/* 208 */         binarySort(paramArrayOfT, paramInt1, paramInt1 + n, paramInt1 + m, paramComparator);
/* 209 */         m = n;
/*     */       }
/*     */ 
/* 213 */       localTimSort.pushRun(paramInt1, m);
/* 214 */       localTimSort.mergeCollapse();
/*     */ 
/* 217 */       paramInt1 += m;
/* 218 */       i -= m;
/* 219 */     }while (i != 0);
/*     */ 
/* 222 */     assert (paramInt1 == paramInt2);
/* 223 */     localTimSort.mergeForceCollapse();
/* 224 */     assert (localTimSort.stackSize == 1);
/*     */   }
/*     */ 
/*     */   private static <T> void binarySort(T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3, Comparator<? super T> paramComparator)
/*     */   {
/* 248 */     assert ((paramInt1 <= paramInt3) && (paramInt3 <= paramInt2));
/* 249 */     if (paramInt3 == paramInt1)
/* 250 */       paramInt3++;
/* 251 */     for (; paramInt3 < paramInt2; paramInt3++) {
/* 252 */       T ? = paramArrayOfT[paramInt3];
/*     */ 
/* 255 */       int i = paramInt1;
/* 256 */       int j = paramInt3;
/* 257 */       assert (i <= j);
/*     */ 
/* 263 */       while (i < j) {
/* 264 */         k = i + j >>> 1;
/* 265 */         if (paramComparator.compare(?, paramArrayOfT[k]) < 0)
/* 266 */           j = k;
/*     */         else
/* 268 */           i = k + 1;
/*     */       }
/* 270 */       assert (i == j);
/*     */ 
/* 279 */       int k = paramInt3 - i;
/*     */ 
/* 281 */       switch (k) { case 2:
/* 282 */         paramArrayOfT[(i + 2)] = paramArrayOfT[(i + 1)];
/*     */       case 1:
/* 283 */         paramArrayOfT[(i + 1)] = paramArrayOfT[i];
/* 284 */         break;
/*     */       default:
/* 285 */         System.arraycopy(paramArrayOfT, i, paramArrayOfT, i + 1, k);
/*     */       }
/* 287 */       paramArrayOfT[i] = ?;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static <T> int countRunAndMakeAscending(T[] paramArrayOfT, int paramInt1, int paramInt2, Comparator<? super T> paramComparator)
/*     */   {
/* 318 */     assert (paramInt1 < paramInt2);
/* 319 */     int i = paramInt1 + 1;
/* 320 */     if (i == paramInt2) {
/* 321 */       return 1;
/*     */     }
/*     */ 
/* 324 */     if (paramComparator.compare(paramArrayOfT[(i++)], paramArrayOfT[paramInt1]) < 0) {
/* 325 */       while ((i < paramInt2) && (paramComparator.compare(paramArrayOfT[i], paramArrayOfT[(i - 1)]) < 0))
/* 326 */         i++;
/* 327 */       reverseRange(paramArrayOfT, paramInt1, i);
/*     */     } else {
/* 329 */       while ((i < paramInt2) && (paramComparator.compare(paramArrayOfT[i], paramArrayOfT[(i - 1)]) >= 0)) {
/* 330 */         i++;
/*     */       }
/*     */     }
/* 333 */     return i - paramInt1;
/*     */   }
/*     */ 
/*     */   private static void reverseRange(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*     */   {
/* 344 */     paramInt2--;
/* 345 */     while (paramInt1 < paramInt2) {
/* 346 */       Object localObject = paramArrayOfObject[paramInt1];
/* 347 */       paramArrayOfObject[(paramInt1++)] = paramArrayOfObject[paramInt2];
/* 348 */       paramArrayOfObject[(paramInt2--)] = localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int minRunLength(int paramInt)
/*     */   {
/* 370 */     assert (paramInt >= 0);
/* 371 */     int i = 0;
/* 372 */     while (paramInt >= 32) {
/* 373 */       i |= paramInt & 0x1;
/* 374 */       paramInt >>= 1;
/*     */     }
/* 376 */     return paramInt + i;
/*     */   }
/*     */ 
/*     */   private void pushRun(int paramInt1, int paramInt2)
/*     */   {
/* 386 */     this.runBase[this.stackSize] = paramInt1;
/* 387 */     this.runLen[this.stackSize] = paramInt2;
/* 388 */     this.stackSize += 1;
/*     */   }
/*     */ 
/*     */   private void mergeCollapse()
/*     */   {
/* 403 */     while (this.stackSize > 1) {
/* 404 */       int i = this.stackSize - 2;
/* 405 */       if ((i > 0) && (this.runLen[(i - 1)] <= this.runLen[i] + this.runLen[(i + 1)])) {
/* 406 */         if (this.runLen[(i - 1)] < this.runLen[(i + 1)])
/* 407 */           i--;
/* 408 */         mergeAt(i); } else {
/* 409 */         if (this.runLen[i] > this.runLen[(i + 1)]) break;
/* 410 */         mergeAt(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeForceCollapse()
/*     */   {
/* 422 */     while (this.stackSize > 1) {
/* 423 */       int i = this.stackSize - 2;
/* 424 */       if ((i > 0) && (this.runLen[(i - 1)] < this.runLen[(i + 1)]))
/* 425 */         i--;
/* 426 */       mergeAt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeAt(int paramInt)
/*     */   {
/* 438 */     assert (this.stackSize >= 2);
/* 439 */     assert (paramInt >= 0);
/* 440 */     assert ((paramInt == this.stackSize - 2) || (paramInt == this.stackSize - 3));
/*     */ 
/* 442 */     int i = this.runBase[paramInt];
/* 443 */     int j = this.runLen[paramInt];
/* 444 */     int k = this.runBase[(paramInt + 1)];
/* 445 */     int m = this.runLen[(paramInt + 1)];
/* 446 */     assert ((j > 0) && (m > 0));
/* 447 */     assert (i + j == k);
/*     */ 
/* 454 */     this.runLen[paramInt] = (j + m);
/* 455 */     if (paramInt == this.stackSize - 3) {
/* 456 */       this.runBase[(paramInt + 1)] = this.runBase[(paramInt + 2)];
/* 457 */       this.runLen[(paramInt + 1)] = this.runLen[(paramInt + 2)];
/*     */     }
/* 459 */     this.stackSize -= 1;
/*     */ 
/* 465 */     int n = gallopRight(this.a[k], this.a, i, j, 0, this.c);
/* 466 */     assert (n >= 0);
/* 467 */     i += n;
/* 468 */     j -= n;
/* 469 */     if (j == 0) {
/* 470 */       return;
/*     */     }
/*     */ 
/* 476 */     m = gallopLeft(this.a[(i + j - 1)], this.a, k, m, m - 1, this.c);
/* 477 */     assert (m >= 0);
/* 478 */     if (m == 0) {
/* 479 */       return;
/*     */     }
/*     */ 
/* 482 */     if (j <= m)
/* 483 */       mergeLo(i, j, k, m);
/*     */     else
/* 485 */       mergeHi(i, j, k, m);
/*     */   }
/*     */ 
/*     */   private static <T> int gallopLeft(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3, Comparator<? super T> paramComparator)
/*     */   {
/* 508 */     assert ((paramInt2 > 0) && (paramInt3 >= 0) && (paramInt3 < paramInt2));
/* 509 */     int i = 0;
/* 510 */     int j = 1;
/*     */     int k;
/* 511 */     if (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3)]) > 0)
/*     */     {
/* 513 */       k = paramInt2 - paramInt3;
/* 514 */       while ((j < k) && (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3 + j)]) > 0)) {
/* 515 */         i = j;
/* 516 */         j = (j << 1) + 1;
/* 517 */         if (j <= 0)
/* 518 */           j = k;
/*     */       }
/* 520 */       if (j > k) {
/* 521 */         j = k;
/*     */       }
/*     */ 
/* 524 */       i += paramInt3;
/* 525 */       j += paramInt3;
/*     */     }
/*     */     else {
/* 528 */       k = paramInt3 + 1;
/* 529 */       while ((j < k) && (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3 - j)]) <= 0)) {
/* 530 */         i = j;
/* 531 */         j = (j << 1) + 1;
/* 532 */         if (j <= 0)
/* 533 */           j = k;
/*     */       }
/* 535 */       if (j > k) {
/* 536 */         j = k;
/*     */       }
/*     */ 
/* 539 */       int m = i;
/* 540 */       i = paramInt3 - j;
/* 541 */       j = paramInt3 - m;
/*     */     }
/* 543 */     assert ((-1 <= i) && (i < j) && (j <= paramInt2));
/*     */ 
/* 550 */     i++;
/* 551 */     while (i < j) {
/* 552 */       k = i + (j - i >>> 1);
/*     */ 
/* 554 */       if (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + k)]) > 0)
/* 555 */         i = k + 1;
/*     */       else
/* 557 */         j = k;
/*     */     }
/* 559 */     assert (i == j);
/* 560 */     return j;
/*     */   }
/*     */ 
/*     */   private static <T> int gallopRight(T paramT, T[] paramArrayOfT, int paramInt1, int paramInt2, int paramInt3, Comparator<? super T> paramComparator)
/*     */   {
/* 578 */     assert ((paramInt2 > 0) && (paramInt3 >= 0) && (paramInt3 < paramInt2));
/*     */ 
/* 580 */     int i = 1;
/* 581 */     int j = 0;
/*     */     int k;
/* 582 */     if (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3)]) < 0)
/*     */     {
/* 584 */       k = paramInt3 + 1;
/* 585 */       while ((i < k) && (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3 - i)]) < 0)) {
/* 586 */         j = i;
/* 587 */         i = (i << 1) + 1;
/* 588 */         if (i <= 0)
/* 589 */           i = k;
/*     */       }
/* 591 */       if (i > k) {
/* 592 */         i = k;
/*     */       }
/*     */ 
/* 595 */       int m = j;
/* 596 */       j = paramInt3 - i;
/* 597 */       i = paramInt3 - m;
/*     */     }
/*     */     else {
/* 600 */       k = paramInt2 - paramInt3;
/* 601 */       while ((i < k) && (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + paramInt3 + i)]) >= 0)) {
/* 602 */         j = i;
/* 603 */         i = (i << 1) + 1;
/* 604 */         if (i <= 0)
/* 605 */           i = k;
/*     */       }
/* 607 */       if (i > k) {
/* 608 */         i = k;
/*     */       }
/*     */ 
/* 611 */       j += paramInt3;
/* 612 */       i += paramInt3;
/*     */     }
/* 614 */     assert ((-1 <= j) && (j < i) && (i <= paramInt2));
/*     */ 
/* 621 */     j++;
/* 622 */     while (j < i) {
/* 623 */       k = j + (i - j >>> 1);
/*     */ 
/* 625 */       if (paramComparator.compare(paramT, paramArrayOfT[(paramInt1 + k)]) < 0)
/* 626 */         i = k;
/*     */       else
/* 628 */         j = k + 1;
/*     */     }
/* 630 */     assert (j == i);
/* 631 */     return i;
/*     */   }
/*     */ 
/*     */   private void mergeLo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 651 */     assert ((paramInt2 > 0) && (paramInt4 > 0) && (paramInt1 + paramInt2 == paramInt3));
/*     */ 
/* 654 */     Object[] arrayOfObject1 = this.a;
/* 655 */     Object[] arrayOfObject2 = ensureCapacity(paramInt2);
/* 656 */     System.arraycopy(arrayOfObject1, paramInt1, arrayOfObject2, 0, paramInt2);
/*     */ 
/* 658 */     int i = 0;
/* 659 */     int j = paramInt3;
/* 660 */     int k = paramInt1;
/*     */ 
/* 663 */     arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 664 */     paramInt4--; if (paramInt4 == 0) {
/* 665 */       System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, paramInt2);
/* 666 */       return;
/*     */     }
/* 668 */     if (paramInt2 == 1) {
/* 669 */       System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, paramInt4);
/* 670 */       arrayOfObject1[(k + paramInt4)] = arrayOfObject2[i];
/* 671 */       return;
/*     */     }
/*     */ 
/* 674 */     Comparator localComparator = this.c;
/* 675 */     int m = this.minGallop;
/*     */     while (true)
/*     */     {
/* 678 */       int n = 0;
/* 679 */       int i1 = 0;
/*     */       do
/*     */       {
/* 686 */         assert ((paramInt2 > 1) && (paramInt4 > 0));
/* 687 */         if (localComparator.compare(arrayOfObject1[j], arrayOfObject2[i]) < 0) {
/* 688 */           arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 689 */           i1++;
/* 690 */           n = 0;
/* 691 */           paramInt4--; if (paramInt4 == 0)
/* 692 */             break;
/*     */         } else {
/* 694 */           arrayOfObject1[(k++)] = arrayOfObject2[(i++)];
/* 695 */           n++;
/* 696 */           i1 = 0;
/* 697 */           paramInt2--; if (paramInt2 == 1) break;
/*     */         }
/*     */       }
/* 700 */       while ((n | i1) < m);
/*     */       do
/*     */       {
/* 708 */         assert ((paramInt2 > 1) && (paramInt4 > 0));
/* 709 */         n = gallopRight(arrayOfObject1[j], arrayOfObject2, i, paramInt2, 0, localComparator);
/* 710 */         if (n != 0) {
/* 711 */           System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, n);
/* 712 */           k += n;
/* 713 */           i += n;
/* 714 */           paramInt2 -= n;
/* 715 */           if (paramInt2 <= 1)
/*     */             break;
/*     */         }
/* 718 */         arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 719 */         paramInt4--; if (paramInt4 == 0) {
/*     */           break;
/*     */         }
/* 722 */         i1 = gallopLeft(arrayOfObject2[i], arrayOfObject1, j, paramInt4, 0, localComparator);
/* 723 */         if (i1 != 0) {
/* 724 */           System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, i1);
/* 725 */           k += i1;
/* 726 */           j += i1;
/* 727 */           paramInt4 -= i1;
/* 728 */           if (paramInt4 == 0)
/*     */             break;
/*     */         }
/* 731 */         arrayOfObject1[(k++)] = arrayOfObject2[(i++)];
/* 732 */         paramInt2--; if (paramInt2 == 1)
/*     */           break;
/* 734 */         m--;
/* 735 */       }while (((n >= 7 ? 1 : 0) | (i1 >= 7 ? 1 : 0)) != 0);
/* 736 */       if (m < 0)
/* 737 */         m = 0;
/* 738 */       m += 2;
/*     */     }
/* 740 */     this.minGallop = (m < 1 ? 1 : m);
/*     */ 
/* 742 */     if (paramInt2 == 1) {
/* 743 */       assert (paramInt4 > 0);
/* 744 */       System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, paramInt4);
/* 745 */       arrayOfObject1[(k + paramInt4)] = arrayOfObject2[i]; } else {
/* 746 */       if (paramInt2 == 0) {
/* 747 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/* 750 */       assert (paramInt4 == 0);
/* 751 */       assert (paramInt2 > 1);
/* 752 */       System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeHi(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 768 */     assert ((paramInt2 > 0) && (paramInt4 > 0) && (paramInt1 + paramInt2 == paramInt3));
/*     */ 
/* 771 */     Object[] arrayOfObject1 = this.a;
/* 772 */     Object[] arrayOfObject2 = ensureCapacity(paramInt4);
/* 773 */     System.arraycopy(arrayOfObject1, paramInt3, arrayOfObject2, 0, paramInt4);
/*     */ 
/* 775 */     int i = paramInt1 + paramInt2 - 1;
/* 776 */     int j = paramInt4 - 1;
/* 777 */     int k = paramInt3 + paramInt4 - 1;
/*     */ 
/* 780 */     arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 781 */     paramInt2--; if (paramInt2 == 0) {
/* 782 */       System.arraycopy(arrayOfObject2, 0, arrayOfObject1, k - (paramInt4 - 1), paramInt4);
/* 783 */       return;
/*     */     }
/* 785 */     if (paramInt4 == 1) {
/* 786 */       k -= paramInt2;
/* 787 */       i -= paramInt2;
/* 788 */       System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, paramInt2);
/* 789 */       arrayOfObject1[k] = arrayOfObject2[j];
/* 790 */       return;
/*     */     }
/*     */ 
/* 793 */     Comparator localComparator = this.c;
/* 794 */     int m = this.minGallop;
/*     */     while (true)
/*     */     {
/* 797 */       int n = 0;
/* 798 */       int i1 = 0;
/*     */       do
/*     */       {
/* 805 */         assert ((paramInt2 > 0) && (paramInt4 > 1));
/* 806 */         if (localComparator.compare(arrayOfObject2[j], arrayOfObject1[i]) < 0) {
/* 807 */           arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 808 */           n++;
/* 809 */           i1 = 0;
/* 810 */           paramInt2--; if (paramInt2 == 0)
/* 811 */             break;
/*     */         } else {
/* 813 */           arrayOfObject1[(k--)] = arrayOfObject2[(j--)];
/* 814 */           i1++;
/* 815 */           n = 0;
/* 816 */           paramInt4--; if (paramInt4 == 1) break;
/*     */         }
/*     */       }
/* 819 */       while ((n | i1) < m);
/*     */       do
/*     */       {
/* 827 */         assert ((paramInt2 > 0) && (paramInt4 > 1));
/* 828 */         n = paramInt2 - gallopRight(arrayOfObject2[j], arrayOfObject1, paramInt1, paramInt2, paramInt2 - 1, localComparator);
/* 829 */         if (n != 0) {
/* 830 */           k -= n;
/* 831 */           i -= n;
/* 832 */           paramInt2 -= n;
/* 833 */           System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, n);
/* 834 */           if (paramInt2 == 0)
/*     */             break;
/*     */         }
/* 837 */         arrayOfObject1[(k--)] = arrayOfObject2[(j--)];
/* 838 */         paramInt4--; if (paramInt4 == 1) {
/*     */           break;
/*     */         }
/* 841 */         i1 = paramInt4 - gallopLeft(arrayOfObject1[i], arrayOfObject2, 0, paramInt4, paramInt4 - 1, localComparator);
/* 842 */         if (i1 != 0) {
/* 843 */           k -= i1;
/* 844 */           j -= i1;
/* 845 */           paramInt4 -= i1;
/* 846 */           System.arraycopy(arrayOfObject2, j + 1, arrayOfObject1, k + 1, i1);
/* 847 */           if (paramInt4 <= 1)
/*     */             break;
/*     */         }
/* 850 */         arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 851 */         paramInt2--; if (paramInt2 == 0)
/*     */           break;
/* 853 */         m--;
/* 854 */       }while (((n >= 7 ? 1 : 0) | (i1 >= 7 ? 1 : 0)) != 0);
/* 855 */       if (m < 0)
/* 856 */         m = 0;
/* 857 */       m += 2;
/*     */     }
/* 859 */     this.minGallop = (m < 1 ? 1 : m);
/*     */ 
/* 861 */     if (paramInt4 == 1) {
/* 862 */       assert (paramInt2 > 0);
/* 863 */       k -= paramInt2;
/* 864 */       i -= paramInt2;
/* 865 */       System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, paramInt2);
/* 866 */       arrayOfObject1[k] = arrayOfObject2[j]; } else {
/* 867 */       if (paramInt4 == 0) {
/* 868 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/* 871 */       assert (paramInt2 == 0);
/* 872 */       assert (paramInt4 > 0);
/* 873 */       System.arraycopy(arrayOfObject2, 0, arrayOfObject1, k - (paramInt4 - 1), paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   private T[] ensureCapacity(int paramInt)
/*     */   {
/* 886 */     if (this.tmp.length < paramInt)
/*     */     {
/* 888 */       int i = paramInt;
/* 889 */       i |= i >> 1;
/* 890 */       i |= i >> 2;
/* 891 */       i |= i >> 4;
/* 892 */       i |= i >> 8;
/* 893 */       i |= i >> 16;
/* 894 */       i++;
/*     */ 
/* 896 */       if (i < 0)
/* 897 */         i = paramInt;
/*     */       else {
/* 899 */         i = Math.min(i, this.a.length >>> 1);
/*     */       }
/*     */ 
/* 902 */       Object[] arrayOfObject = (Object[])new Object[i];
/* 903 */       this.tmp = arrayOfObject;
/*     */     }
/* 905 */     return this.tmp;
/*     */   }
/*     */ 
/*     */   private static void rangeCheck(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 920 */     if (paramInt2 > paramInt3) {
/* 921 */       throw new IllegalArgumentException("fromIndex(" + paramInt2 + ") > toIndex(" + paramInt3 + ")");
/*     */     }
/* 923 */     if (paramInt2 < 0)
/* 924 */       throw new ArrayIndexOutOfBoundsException(paramInt2);
/* 925 */     if (paramInt3 > paramInt1)
/* 926 */       throw new ArrayIndexOutOfBoundsException(paramInt3);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.TimSort
 * JD-Core Version:    0.6.2
 */