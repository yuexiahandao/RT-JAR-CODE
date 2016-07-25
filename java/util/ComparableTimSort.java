/*     */ package java.util;
/*     */ 
/*     */ class ComparableTimSort
/*     */ {
/*     */   private static final int MIN_MERGE = 32;
/*     */   private final Object[] a;
/*     */   private static final int MIN_GALLOP = 7;
/*  77 */   private int minGallop = 7;
/*     */   private static final int INITIAL_TMP_STORAGE_LENGTH = 256;
/*     */   private Object[] tmp;
/* 103 */   private int stackSize = 0;
/*     */   private final int[] runBase;
/*     */   private final int[] runLen;
/*     */ 
/*     */   private ComparableTimSort(Object[] paramArrayOfObject)
/*     */   {
/* 113 */     this.a = paramArrayOfObject;
/*     */ 
/* 116 */     int i = paramArrayOfObject.length;
/*     */ 
/* 118 */     Object[] arrayOfObject = new Object[i < 512 ? i >>> 1 : 256];
/*     */ 
/* 120 */     this.tmp = arrayOfObject;
/*     */ 
/* 132 */     int j = i < 119151 ? 24 : i < 1542 ? 10 : i < 120 ? 5 : 40;
/*     */ 
/* 135 */     this.runBase = new int[j];
/* 136 */     this.runLen = new int[j];
/*     */   }
/*     */ 
/*     */   static void sort(Object[] paramArrayOfObject)
/*     */   {
/* 146 */     sort(paramArrayOfObject, 0, paramArrayOfObject.length);
/*     */   }
/*     */ 
/*     */   static void sort(Object[] paramArrayOfObject, int paramInt1, int paramInt2) {
/* 150 */     rangeCheck(paramArrayOfObject.length, paramInt1, paramInt2);
/* 151 */     int i = paramInt2 - paramInt1;
/* 152 */     if (i < 2) {
/* 153 */       return;
/*     */     }
/*     */ 
/* 156 */     if (i < 32) {
/* 157 */       int j = countRunAndMakeAscending(paramArrayOfObject, paramInt1, paramInt2);
/* 158 */       binarySort(paramArrayOfObject, paramInt1, paramInt2, paramInt1 + j);
/* 159 */       return;
/*     */     }
/*     */ 
/* 167 */     ComparableTimSort localComparableTimSort = new ComparableTimSort(paramArrayOfObject);
/* 168 */     int k = minRunLength(i);
/*     */     do
/*     */     {
/* 171 */       int m = countRunAndMakeAscending(paramArrayOfObject, paramInt1, paramInt2);
/*     */ 
/* 174 */       if (m < k) {
/* 175 */         int n = i <= k ? i : k;
/* 176 */         binarySort(paramArrayOfObject, paramInt1, paramInt1 + n, paramInt1 + m);
/* 177 */         m = n;
/*     */       }
/*     */ 
/* 181 */       localComparableTimSort.pushRun(paramInt1, m);
/* 182 */       localComparableTimSort.mergeCollapse();
/*     */ 
/* 185 */       paramInt1 += m;
/* 186 */       i -= m;
/* 187 */     }while (i != 0);
/*     */ 
/* 190 */     assert (paramInt1 == paramInt2);
/* 191 */     localComparableTimSort.mergeForceCollapse();
/* 192 */     assert (localComparableTimSort.stackSize == 1);
/*     */   }
/*     */ 
/*     */   private static void binarySort(Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 214 */     assert ((paramInt1 <= paramInt3) && (paramInt3 <= paramInt2));
/* 215 */     if (paramInt3 == paramInt1)
/* 216 */       paramInt3++;
/* 217 */     for (; paramInt3 < paramInt2; paramInt3++)
/*     */     {
/* 219 */       Comparable localComparable = (Comparable)paramArrayOfObject[paramInt3];
/*     */ 
/* 222 */       int i = paramInt1;
/* 223 */       int j = paramInt3;
/* 224 */       assert (i <= j);
/*     */ 
/* 230 */       while (i < j) {
/* 231 */         k = i + j >>> 1;
/* 232 */         if (localComparable.compareTo(paramArrayOfObject[k]) < 0)
/* 233 */           j = k;
/*     */         else
/* 235 */           i = k + 1;
/*     */       }
/* 237 */       assert (i == j);
/*     */ 
/* 246 */       int k = paramInt3 - i;
/*     */ 
/* 248 */       switch (k) { case 2:
/* 249 */         paramArrayOfObject[(i + 2)] = paramArrayOfObject[(i + 1)];
/*     */       case 1:
/* 250 */         paramArrayOfObject[(i + 1)] = paramArrayOfObject[i];
/* 251 */         break;
/*     */       default:
/* 252 */         System.arraycopy(paramArrayOfObject, i, paramArrayOfObject, i + 1, k);
/*     */       }
/* 254 */       paramArrayOfObject[i] = localComparable;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int countRunAndMakeAscending(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*     */   {
/* 284 */     assert (paramInt1 < paramInt2);
/* 285 */     int i = paramInt1 + 1;
/* 286 */     if (i == paramInt2) {
/* 287 */       return 1;
/*     */     }
/*     */ 
/* 290 */     if (((Comparable)paramArrayOfObject[(i++)]).compareTo(paramArrayOfObject[paramInt1]) < 0) {
/* 291 */       while ((i < paramInt2) && (((Comparable)paramArrayOfObject[i]).compareTo(paramArrayOfObject[(i - 1)]) < 0))
/* 292 */         i++;
/* 293 */       reverseRange(paramArrayOfObject, paramInt1, i);
/*     */     } else {
/* 295 */       while ((i < paramInt2) && (((Comparable)paramArrayOfObject[i]).compareTo(paramArrayOfObject[(i - 1)]) >= 0)) {
/* 296 */         i++;
/*     */       }
/*     */     }
/* 299 */     return i - paramInt1;
/*     */   }
/*     */ 
/*     */   private static void reverseRange(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*     */   {
/* 310 */     paramInt2--;
/* 311 */     while (paramInt1 < paramInt2) {
/* 312 */       Object localObject = paramArrayOfObject[paramInt1];
/* 313 */       paramArrayOfObject[(paramInt1++)] = paramArrayOfObject[paramInt2];
/* 314 */       paramArrayOfObject[(paramInt2--)] = localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int minRunLength(int paramInt)
/*     */   {
/* 336 */     assert (paramInt >= 0);
/* 337 */     int i = 0;
/* 338 */     while (paramInt >= 32) {
/* 339 */       i |= paramInt & 0x1;
/* 340 */       paramInt >>= 1;
/*     */     }
/* 342 */     return paramInt + i;
/*     */   }
/*     */ 
/*     */   private void pushRun(int paramInt1, int paramInt2)
/*     */   {
/* 352 */     this.runBase[this.stackSize] = paramInt1;
/* 353 */     this.runLen[this.stackSize] = paramInt2;
/* 354 */     this.stackSize += 1;
/*     */   }
/*     */ 
/*     */   private void mergeCollapse()
/*     */   {
/* 369 */     while (this.stackSize > 1) {
/* 370 */       int i = this.stackSize - 2;
/* 371 */       if ((i > 0) && (this.runLen[(i - 1)] <= this.runLen[i] + this.runLen[(i + 1)])) {
/* 372 */         if (this.runLen[(i - 1)] < this.runLen[(i + 1)])
/* 373 */           i--;
/* 374 */         mergeAt(i); } else {
/* 375 */         if (this.runLen[i] > this.runLen[(i + 1)]) break;
/* 376 */         mergeAt(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeForceCollapse()
/*     */   {
/* 388 */     while (this.stackSize > 1) {
/* 389 */       int i = this.stackSize - 2;
/* 390 */       if ((i > 0) && (this.runLen[(i - 1)] < this.runLen[(i + 1)]))
/* 391 */         i--;
/* 392 */       mergeAt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeAt(int paramInt)
/*     */   {
/* 405 */     assert (this.stackSize >= 2);
/* 406 */     assert (paramInt >= 0);
/* 407 */     assert ((paramInt == this.stackSize - 2) || (paramInt == this.stackSize - 3));
/*     */ 
/* 409 */     int i = this.runBase[paramInt];
/* 410 */     int j = this.runLen[paramInt];
/* 411 */     int k = this.runBase[(paramInt + 1)];
/* 412 */     int m = this.runLen[(paramInt + 1)];
/* 413 */     assert ((j > 0) && (m > 0));
/* 414 */     assert (i + j == k);
/*     */ 
/* 421 */     this.runLen[paramInt] = (j + m);
/* 422 */     if (paramInt == this.stackSize - 3) {
/* 423 */       this.runBase[(paramInt + 1)] = this.runBase[(paramInt + 2)];
/* 424 */       this.runLen[(paramInt + 1)] = this.runLen[(paramInt + 2)];
/*     */     }
/* 426 */     this.stackSize -= 1;
/*     */ 
/* 432 */     int n = gallopRight((Comparable)this.a[k], this.a, i, j, 0);
/* 433 */     assert (n >= 0);
/* 434 */     i += n;
/* 435 */     j -= n;
/* 436 */     if (j == 0) {
/* 437 */       return;
/*     */     }
/*     */ 
/* 443 */     m = gallopLeft((Comparable)this.a[(i + j - 1)], this.a, k, m, m - 1);
/*     */ 
/* 445 */     assert (m >= 0);
/* 446 */     if (m == 0) {
/* 447 */       return;
/*     */     }
/*     */ 
/* 450 */     if (j <= m)
/* 451 */       mergeLo(i, j, k, m);
/*     */     else
/* 453 */       mergeHi(i, j, k, m);
/*     */   }
/*     */ 
/*     */   private static int gallopLeft(Comparable<Object> paramComparable, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 475 */     assert ((paramInt2 > 0) && (paramInt3 >= 0) && (paramInt3 < paramInt2));
/*     */ 
/* 477 */     int i = 0;
/* 478 */     int j = 1;
/*     */     int k;
/* 479 */     if (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3)]) > 0)
/*     */     {
/* 481 */       k = paramInt2 - paramInt3;
/* 482 */       while ((j < k) && (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3 + j)]) > 0)) {
/* 483 */         i = j;
/* 484 */         j = (j << 1) + 1;
/* 485 */         if (j <= 0)
/* 486 */           j = k;
/*     */       }
/* 488 */       if (j > k) {
/* 489 */         j = k;
/*     */       }
/*     */ 
/* 492 */       i += paramInt3;
/* 493 */       j += paramInt3;
/*     */     }
/*     */     else {
/* 496 */       k = paramInt3 + 1;
/* 497 */       while ((j < k) && (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3 - j)]) <= 0)) {
/* 498 */         i = j;
/* 499 */         j = (j << 1) + 1;
/* 500 */         if (j <= 0)
/* 501 */           j = k;
/*     */       }
/* 503 */       if (j > k) {
/* 504 */         j = k;
/*     */       }
/*     */ 
/* 507 */       int m = i;
/* 508 */       i = paramInt3 - j;
/* 509 */       j = paramInt3 - m;
/*     */     }
/* 511 */     assert ((-1 <= i) && (i < j) && (j <= paramInt2));
/*     */ 
/* 518 */     i++;
/* 519 */     while (i < j) {
/* 520 */       k = i + (j - i >>> 1);
/*     */ 
/* 522 */       if (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + k)]) > 0)
/* 523 */         i = k + 1;
/*     */       else
/* 525 */         j = k;
/*     */     }
/* 527 */     assert (i == j);
/* 528 */     return j;
/*     */   }
/*     */ 
/*     */   private static int gallopRight(Comparable<Object> paramComparable, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 545 */     assert ((paramInt2 > 0) && (paramInt3 >= 0) && (paramInt3 < paramInt2));
/*     */ 
/* 547 */     int i = 1;
/* 548 */     int j = 0;
/*     */     int k;
/* 549 */     if (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3)]) < 0)
/*     */     {
/* 551 */       k = paramInt3 + 1;
/* 552 */       while ((i < k) && (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3 - i)]) < 0)) {
/* 553 */         j = i;
/* 554 */         i = (i << 1) + 1;
/* 555 */         if (i <= 0)
/* 556 */           i = k;
/*     */       }
/* 558 */       if (i > k) {
/* 559 */         i = k;
/*     */       }
/*     */ 
/* 562 */       int m = j;
/* 563 */       j = paramInt3 - i;
/* 564 */       i = paramInt3 - m;
/*     */     }
/*     */     else {
/* 567 */       k = paramInt2 - paramInt3;
/* 568 */       while ((i < k) && (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + paramInt3 + i)]) >= 0)) {
/* 569 */         j = i;
/* 570 */         i = (i << 1) + 1;
/* 571 */         if (i <= 0)
/* 572 */           i = k;
/*     */       }
/* 574 */       if (i > k) {
/* 575 */         i = k;
/*     */       }
/*     */ 
/* 578 */       j += paramInt3;
/* 579 */       i += paramInt3;
/*     */     }
/* 581 */     assert ((-1 <= j) && (j < i) && (i <= paramInt2));
/*     */ 
/* 588 */     j++;
/* 589 */     while (j < i) {
/* 590 */       k = j + (i - j >>> 1);
/*     */ 
/* 592 */       if (paramComparable.compareTo(paramArrayOfObject[(paramInt1 + k)]) < 0)
/* 593 */         i = k;
/*     */       else
/* 595 */         j = k + 1;
/*     */     }
/* 597 */     assert (j == i);
/* 598 */     return i;
/*     */   }
/*     */ 
/*     */   private void mergeLo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 619 */     assert ((paramInt2 > 0) && (paramInt4 > 0) && (paramInt1 + paramInt2 == paramInt3));
/*     */ 
/* 622 */     Object[] arrayOfObject1 = this.a;
/* 623 */     Object[] arrayOfObject2 = ensureCapacity(paramInt2);
/* 624 */     System.arraycopy(arrayOfObject1, paramInt1, arrayOfObject2, 0, paramInt2);
/*     */ 
/* 626 */     int i = 0;
/* 627 */     int j = paramInt3;
/* 628 */     int k = paramInt1;
/*     */ 
/* 631 */     arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 632 */     paramInt4--; if (paramInt4 == 0) {
/* 633 */       System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, paramInt2);
/* 634 */       return;
/*     */     }
/* 636 */     if (paramInt2 == 1) {
/* 637 */       System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, paramInt4);
/* 638 */       arrayOfObject1[(k + paramInt4)] = arrayOfObject2[i];
/* 639 */       return;
/*     */     }
/*     */ 
/* 642 */     int m = this.minGallop;
/*     */     while (true)
/*     */     {
/* 645 */       int n = 0;
/* 646 */       int i1 = 0;
/*     */       do
/*     */       {
/* 653 */         assert ((paramInt2 > 1) && (paramInt4 > 0));
/* 654 */         if (((Comparable)arrayOfObject1[j]).compareTo(arrayOfObject2[i]) < 0) {
/* 655 */           arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 656 */           i1++;
/* 657 */           n = 0;
/* 658 */           paramInt4--; if (paramInt4 == 0)
/* 659 */             break;
/*     */         } else {
/* 661 */           arrayOfObject1[(k++)] = arrayOfObject2[(i++)];
/* 662 */           n++;
/* 663 */           i1 = 0;
/* 664 */           paramInt2--; if (paramInt2 == 1) break;
/*     */         }
/*     */       }
/* 667 */       while ((n | i1) < m);
/*     */       do
/*     */       {
/* 675 */         assert ((paramInt2 > 1) && (paramInt4 > 0));
/* 676 */         n = gallopRight((Comparable)arrayOfObject1[j], arrayOfObject2, i, paramInt2, 0);
/* 677 */         if (n != 0) {
/* 678 */           System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, n);
/* 679 */           k += n;
/* 680 */           i += n;
/* 681 */           paramInt2 -= n;
/* 682 */           if (paramInt2 <= 1)
/*     */             break;
/*     */         }
/* 685 */         arrayOfObject1[(k++)] = arrayOfObject1[(j++)];
/* 686 */         paramInt4--; if (paramInt4 == 0) {
/*     */           break;
/*     */         }
/* 689 */         i1 = gallopLeft((Comparable)arrayOfObject2[i], arrayOfObject1, j, paramInt4, 0);
/* 690 */         if (i1 != 0) {
/* 691 */           System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, i1);
/* 692 */           k += i1;
/* 693 */           j += i1;
/* 694 */           paramInt4 -= i1;
/* 695 */           if (paramInt4 == 0)
/*     */             break;
/*     */         }
/* 698 */         arrayOfObject1[(k++)] = arrayOfObject2[(i++)];
/* 699 */         paramInt2--; if (paramInt2 == 1)
/*     */           break;
/* 701 */         m--;
/* 702 */       }while (((n >= 7 ? 1 : 0) | (i1 >= 7 ? 1 : 0)) != 0);
/* 703 */       if (m < 0)
/* 704 */         m = 0;
/* 705 */       m += 2;
/*     */     }
/* 707 */     this.minGallop = (m < 1 ? 1 : m);
/*     */ 
/* 709 */     if (paramInt2 == 1) {
/* 710 */       assert (paramInt4 > 0);
/* 711 */       System.arraycopy(arrayOfObject1, j, arrayOfObject1, k, paramInt4);
/* 712 */       arrayOfObject1[(k + paramInt4)] = arrayOfObject2[i]; } else {
/* 713 */       if (paramInt2 == 0) {
/* 714 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/* 717 */       assert (paramInt4 == 0);
/* 718 */       assert (paramInt2 > 1);
/* 719 */       System.arraycopy(arrayOfObject2, i, arrayOfObject1, k, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mergeHi(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 736 */     assert ((paramInt2 > 0) && (paramInt4 > 0) && (paramInt1 + paramInt2 == paramInt3));
/*     */ 
/* 739 */     Object[] arrayOfObject1 = this.a;
/* 740 */     Object[] arrayOfObject2 = ensureCapacity(paramInt4);
/* 741 */     System.arraycopy(arrayOfObject1, paramInt3, arrayOfObject2, 0, paramInt4);
/*     */ 
/* 743 */     int i = paramInt1 + paramInt2 - 1;
/* 744 */     int j = paramInt4 - 1;
/* 745 */     int k = paramInt3 + paramInt4 - 1;
/*     */ 
/* 748 */     arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 749 */     paramInt2--; if (paramInt2 == 0) {
/* 750 */       System.arraycopy(arrayOfObject2, 0, arrayOfObject1, k - (paramInt4 - 1), paramInt4);
/* 751 */       return;
/*     */     }
/* 753 */     if (paramInt4 == 1) {
/* 754 */       k -= paramInt2;
/* 755 */       i -= paramInt2;
/* 756 */       System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, paramInt2);
/* 757 */       arrayOfObject1[k] = arrayOfObject2[j];
/* 758 */       return;
/*     */     }
/*     */ 
/* 761 */     int m = this.minGallop;
/*     */     while (true)
/*     */     {
/* 764 */       int n = 0;
/* 765 */       int i1 = 0;
/*     */       do
/*     */       {
/* 772 */         assert ((paramInt2 > 0) && (paramInt4 > 1));
/* 773 */         if (((Comparable)arrayOfObject2[j]).compareTo(arrayOfObject1[i]) < 0) {
/* 774 */           arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 775 */           n++;
/* 776 */           i1 = 0;
/* 777 */           paramInt2--; if (paramInt2 == 0)
/* 778 */             break;
/*     */         } else {
/* 780 */           arrayOfObject1[(k--)] = arrayOfObject2[(j--)];
/* 781 */           i1++;
/* 782 */           n = 0;
/* 783 */           paramInt4--; if (paramInt4 == 1) break;
/*     */         }
/*     */       }
/* 786 */       while ((n | i1) < m);
/*     */       do
/*     */       {
/* 794 */         assert ((paramInt2 > 0) && (paramInt4 > 1));
/* 795 */         n = paramInt2 - gallopRight((Comparable)arrayOfObject2[j], arrayOfObject1, paramInt1, paramInt2, paramInt2 - 1);
/* 796 */         if (n != 0) {
/* 797 */           k -= n;
/* 798 */           i -= n;
/* 799 */           paramInt2 -= n;
/* 800 */           System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, n);
/* 801 */           if (paramInt2 == 0)
/*     */             break;
/*     */         }
/* 804 */         arrayOfObject1[(k--)] = arrayOfObject2[(j--)];
/* 805 */         paramInt4--; if (paramInt4 == 1) {
/*     */           break;
/*     */         }
/* 808 */         i1 = paramInt4 - gallopLeft((Comparable)arrayOfObject1[i], arrayOfObject2, 0, paramInt4, paramInt4 - 1);
/* 809 */         if (i1 != 0) {
/* 810 */           k -= i1;
/* 811 */           j -= i1;
/* 812 */           paramInt4 -= i1;
/* 813 */           System.arraycopy(arrayOfObject2, j + 1, arrayOfObject1, k + 1, i1);
/* 814 */           if (paramInt4 <= 1)
/*     */             break;
/*     */         }
/* 817 */         arrayOfObject1[(k--)] = arrayOfObject1[(i--)];
/* 818 */         paramInt2--; if (paramInt2 == 0)
/*     */           break;
/* 820 */         m--;
/* 821 */       }while (((n >= 7 ? 1 : 0) | (i1 >= 7 ? 1 : 0)) != 0);
/* 822 */       if (m < 0)
/* 823 */         m = 0;
/* 824 */       m += 2;
/*     */     }
/* 826 */     this.minGallop = (m < 1 ? 1 : m);
/*     */ 
/* 828 */     if (paramInt4 == 1) {
/* 829 */       assert (paramInt2 > 0);
/* 830 */       k -= paramInt2;
/* 831 */       i -= paramInt2;
/* 832 */       System.arraycopy(arrayOfObject1, i + 1, arrayOfObject1, k + 1, paramInt2);
/* 833 */       arrayOfObject1[k] = arrayOfObject2[j]; } else {
/* 834 */       if (paramInt4 == 0) {
/* 835 */         throw new IllegalArgumentException("Comparison method violates its general contract!");
/*     */       }
/*     */ 
/* 838 */       assert (paramInt2 == 0);
/* 839 */       assert (paramInt4 > 0);
/* 840 */       System.arraycopy(arrayOfObject2, 0, arrayOfObject1, k - (paramInt4 - 1), paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object[] ensureCapacity(int paramInt)
/*     */   {
/* 853 */     if (this.tmp.length < paramInt)
/*     */     {
/* 855 */       int i = paramInt;
/* 856 */       i |= i >> 1;
/* 857 */       i |= i >> 2;
/* 858 */       i |= i >> 4;
/* 859 */       i |= i >> 8;
/* 860 */       i |= i >> 16;
/* 861 */       i++;
/*     */ 
/* 863 */       if (i < 0)
/* 864 */         i = paramInt;
/*     */       else {
/* 866 */         i = Math.min(i, this.a.length >>> 1);
/*     */       }
/*     */ 
/* 869 */       Object[] arrayOfObject = new Object[i];
/* 870 */       this.tmp = arrayOfObject;
/*     */     }
/* 872 */     return this.tmp;
/*     */   }
/*     */ 
/*     */   private static void rangeCheck(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 887 */     if (paramInt2 > paramInt3) {
/* 888 */       throw new IllegalArgumentException("fromIndex(" + paramInt2 + ") > toIndex(" + paramInt3 + ")");
/*     */     }
/* 890 */     if (paramInt2 < 0)
/* 891 */       throw new ArrayIndexOutOfBoundsException(paramInt2);
/* 892 */     if (paramInt3 > paramInt1)
/* 893 */       throw new ArrayIndexOutOfBoundsException(paramInt3);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ComparableTimSort
 * JD-Core Version:    0.6.2
 */