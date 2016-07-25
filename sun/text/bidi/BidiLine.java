/*     */ package sun.text.bidi;
/*     */ 
/*     */ import java.text.Bidi;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class BidiLine
/*     */ {
/*     */   static void setTrailingWSStart(BidiBase paramBidiBase)
/*     */   {
/* 114 */     byte[] arrayOfByte1 = paramBidiBase.dirProps;
/* 115 */     byte[] arrayOfByte2 = paramBidiBase.levels;
/* 116 */     int i = paramBidiBase.length;
/* 117 */     int j = paramBidiBase.paraLevel;
/*     */ 
/* 125 */     if (BidiBase.NoContextRTL(arrayOfByte1[(i - 1)]) == 7) {
/* 126 */       paramBidiBase.trailingWSStart = i;
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     while ((i > 0) && ((BidiBase.DirPropFlagNC(arrayOfByte1[(i - 1)]) & BidiBase.MASK_WS) != 0))
/*     */     {
/* 132 */       i--;
/*     */     }
/*     */ 
/* 136 */     while ((i > 0) && (arrayOfByte2[(i - 1)] == j)) {
/* 137 */       i--;
/*     */     }
/*     */ 
/* 140 */     paramBidiBase.trailingWSStart = i;
/*     */   }
/*     */ 
/*     */   public static Bidi setLine(Bidi paramBidi1, BidiBase paramBidiBase1, Bidi paramBidi2, BidiBase paramBidiBase2, int paramInt1, int paramInt2)
/*     */   {
/* 148 */     BidiBase localBidiBase = paramBidiBase2;
/*     */ 
/* 156 */     int i = localBidiBase.length = localBidiBase.originalLength = localBidiBase.resultLength = paramInt2 - paramInt1;
/*     */ 
/* 159 */     localBidiBase.text = new char[i];
/* 160 */     System.arraycopy(paramBidiBase1.text, paramInt1, localBidiBase.text, 0, i);
/* 161 */     localBidiBase.paraLevel = paramBidiBase1.GetParaLevelAt(paramInt1);
/* 162 */     localBidiBase.paraCount = paramBidiBase1.paraCount;
/* 163 */     localBidiBase.runs = new BidiRun[0];
/* 164 */     if (paramBidiBase1.controlCount > 0)
/*     */     {
/* 166 */       for (int j = paramInt1; j < paramInt2; j++) {
/* 167 */         if (BidiBase.IsBidiControlChar(paramBidiBase1.text[j])) {
/* 168 */           localBidiBase.controlCount += 1;
/*     */         }
/*     */       }
/* 171 */       localBidiBase.resultLength -= localBidiBase.controlCount;
/*     */     }
/*     */ 
/* 174 */     localBidiBase.getDirPropsMemory(i);
/* 175 */     localBidiBase.dirProps = localBidiBase.dirPropsMemory;
/* 176 */     System.arraycopy(paramBidiBase1.dirProps, paramInt1, localBidiBase.dirProps, 0, i);
/*     */ 
/* 179 */     localBidiBase.getLevelsMemory(i);
/* 180 */     localBidiBase.levels = localBidiBase.levelsMemory;
/* 181 */     System.arraycopy(paramBidiBase1.levels, paramInt1, localBidiBase.levels, 0, i);
/*     */ 
/* 183 */     localBidiBase.runCount = -1;
/*     */ 
/* 185 */     if (paramBidiBase1.direction != 2)
/*     */     {
/* 187 */       localBidiBase.direction = paramBidiBase1.direction;
/*     */ 
/* 194 */       if (paramBidiBase1.trailingWSStart <= paramInt1)
/* 195 */         localBidiBase.trailingWSStart = 0;
/* 196 */       else if (paramBidiBase1.trailingWSStart < paramInt2)
/* 197 */         paramBidiBase1.trailingWSStart -= paramInt1;
/*     */       else
/* 199 */         localBidiBase.trailingWSStart = i;
/*     */     }
/*     */     else {
/* 202 */       byte[] arrayOfByte = localBidiBase.levels;
/*     */ 
/* 206 */       setTrailingWSStart(localBidiBase);
/* 207 */       int m = localBidiBase.trailingWSStart;
/*     */ 
/* 210 */       if (m == 0)
/*     */       {
/* 212 */         localBidiBase.direction = ((byte)(localBidiBase.paraLevel & 0x1));
/*     */       }
/*     */       else {
/* 215 */         byte b = (byte)(arrayOfByte[0] & 0x1);
/*     */ 
/* 219 */         if ((m < i) && ((localBidiBase.paraLevel & 0x1) != b))
/*     */         {
/* 223 */           localBidiBase.direction = 2;
/*     */         }
/*     */         else
/*     */         {
/* 227 */           for (int k = 1; ; k++) {
/* 228 */             if (k == m)
/*     */             {
/* 230 */               localBidiBase.direction = b;
/* 231 */               break;
/* 232 */             }if ((arrayOfByte[k] & 0x1) != b) {
/* 233 */               localBidiBase.direction = 2;
/* 234 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 240 */       switch (localBidiBase.direction)
/*     */       {
/*     */       case 0:
/* 243 */         localBidiBase.paraLevel = ((byte)(localBidiBase.paraLevel + 1 & 0xFFFFFFFE));
/*     */ 
/* 248 */         localBidiBase.trailingWSStart = 0;
/* 249 */         break;
/*     */       case 1:
/*     */         BidiBase tmp471_469 = localBidiBase; tmp471_469.paraLevel = ((byte)(tmp471_469.paraLevel | 0x1));
/*     */ 
/* 256 */         localBidiBase.trailingWSStart = 0;
/* 257 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 263 */     paramBidiBase2.paraBidi = paramBidiBase1;
/* 264 */     return paramBidi2;
/*     */   }
/*     */ 
/*     */   static byte getLevelAt(BidiBase paramBidiBase, int paramInt)
/*     */   {
/* 270 */     if ((paramBidiBase.direction != 2) || (paramInt >= paramBidiBase.trailingWSStart)) {
/* 271 */       return paramBidiBase.GetParaLevelAt(paramInt);
/*     */     }
/* 273 */     return paramBidiBase.levels[paramInt];
/*     */   }
/*     */ 
/*     */   static byte[] getLevels(BidiBase paramBidiBase)
/*     */   {
/* 279 */     int i = paramBidiBase.trailingWSStart;
/* 280 */     int j = paramBidiBase.length;
/*     */ 
/* 282 */     if (i != j)
/*     */     {
/* 293 */       Arrays.fill(paramBidiBase.levels, i, j, paramBidiBase.paraLevel);
/*     */ 
/* 296 */       paramBidiBase.trailingWSStart = j;
/*     */     }
/* 298 */     if (j < paramBidiBase.levels.length) {
/* 299 */       byte[] arrayOfByte = new byte[j];
/* 300 */       System.arraycopy(paramBidiBase.levels, 0, arrayOfByte, 0, j);
/* 301 */       return arrayOfByte;
/*     */     }
/* 303 */     return paramBidiBase.levels;
/*     */   }
/*     */ 
/*     */   static BidiRun getLogicalRun(BidiBase paramBidiBase, int paramInt)
/*     */   {
/* 311 */     BidiRun localBidiRun1 = new BidiRun();
/* 312 */     getRuns(paramBidiBase);
/* 313 */     int i = paramBidiBase.runCount;
/* 314 */     int j = 0; int k = 0;
/* 315 */     BidiRun localBidiRun2 = paramBidiBase.runs[0];
/*     */ 
/* 317 */     for (int m = 0; m < i; m++) {
/* 318 */       localBidiRun2 = paramBidiBase.runs[m];
/* 319 */       k = localBidiRun2.start + localBidiRun2.limit - j;
/* 320 */       if ((paramInt >= localBidiRun2.start) && (paramInt < k))
/*     */       {
/*     */         break;
/*     */       }
/* 324 */       j = localBidiRun2.limit;
/*     */     }
/* 326 */     localBidiRun1.start = localBidiRun2.start;
/* 327 */     localBidiRun1.limit = k;
/* 328 */     localBidiRun1.level = localBidiRun2.level;
/* 329 */     return localBidiRun1;
/*     */   }
/*     */ 
/*     */   private static void getSingleRun(BidiBase paramBidiBase, byte paramByte)
/*     */   {
/* 335 */     paramBidiBase.runs = paramBidiBase.simpleRuns;
/* 336 */     paramBidiBase.runCount = 1;
/*     */ 
/* 339 */     paramBidiBase.runs[0] = new BidiRun(0, paramBidiBase.length, paramByte);
/*     */   }
/*     */ 
/*     */   private static void reorderLine(BidiBase paramBidiBase, byte paramByte1, byte paramByte2)
/*     */   {
/* 378 */     if (paramByte2 <= (paramByte1 | 0x1)) {
/* 379 */       return;
/*     */     }
/*     */ 
/* 392 */     paramByte1 = (byte)(paramByte1 + 1);
/*     */ 
/* 394 */     BidiRun[] arrayOfBidiRun = paramBidiBase.runs;
/* 395 */     byte[] arrayOfByte = paramBidiBase.levels;
/* 396 */     int m = paramBidiBase.runCount;
/*     */ 
/* 399 */     if (paramBidiBase.trailingWSStart < paramBidiBase.length) {
/* 400 */       m--;
/*     */     }
/*     */ 
/* 403 */     paramByte2 = (byte)(paramByte2 - 1);
/*     */     int i;
/*     */     BidiRun localBidiRun;
/* 403 */     if (paramByte2 >= paramByte1) {
/* 404 */       i = 0;
/*     */       while (true)
/*     */       {
/* 410 */         if ((i < m) && (arrayOfByte[arrayOfBidiRun[i].start] < paramByte2)) {
/* 411 */           i++;
/*     */         } else {
/* 413 */           if (i >= m)
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 418 */           int k = i;
/*     */           do k++; while ((k < m) && (arrayOfByte[arrayOfBidiRun[k].start] >= paramByte2));
/*     */ 
/* 422 */           int j = k - 1;
/* 423 */           while (i < j) {
/* 424 */             localBidiRun = arrayOfBidiRun[i];
/* 425 */             arrayOfBidiRun[i] = arrayOfBidiRun[j];
/* 426 */             arrayOfBidiRun[j] = localBidiRun;
/* 427 */             i++;
/* 428 */             j--;
/*     */           }
/*     */ 
/* 431 */           if (k == m) {
/*     */             break;
/*     */           }
/* 434 */           i = k + 1;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 440 */     if ((paramByte1 & 0x1) == 0) {
/* 441 */       i = 0;
/*     */ 
/* 444 */       if (paramBidiBase.trailingWSStart == paramBidiBase.length) {
/* 445 */         m--;
/*     */       }
/*     */ 
/* 449 */       while (i < m) {
/* 450 */         localBidiRun = arrayOfBidiRun[i];
/* 451 */         arrayOfBidiRun[i] = arrayOfBidiRun[m];
/* 452 */         arrayOfBidiRun[m] = localBidiRun;
/* 453 */         i++;
/* 454 */         m--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static int getRunFromLogicalIndex(BidiBase paramBidiBase, int paramInt)
/*     */   {
/* 462 */     BidiRun[] arrayOfBidiRun = paramBidiBase.runs;
/* 463 */     int i = paramBidiBase.runCount; int j = 0;
/*     */ 
/* 465 */     for (int k = 0; k < i; k++) {
/* 466 */       int m = arrayOfBidiRun[k].limit - j;
/* 467 */       int n = arrayOfBidiRun[k].start;
/* 468 */       if ((paramInt >= n) && (paramInt < n + m)) {
/* 469 */         return k;
/*     */       }
/* 471 */       j += m;
/*     */     }
/*     */ 
/* 474 */     throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
/*     */   }
/*     */ 
/*     */   static void getRuns(BidiBase paramBidiBase)
/*     */   {
/* 493 */     if (paramBidiBase.runCount >= 0)
/*     */       return;
/*     */     int k;
/* 496 */     if (paramBidiBase.direction != 2)
/*     */     {
/* 499 */       getSingleRun(paramBidiBase, paramBidiBase.paraLevel);
/*     */     }
/*     */     else {
/* 502 */       int i = paramBidiBase.length;
/* 503 */       byte[] arrayOfByte = paramBidiBase.levels;
/*     */ 
/* 505 */       byte b1 = 126;
/*     */ 
/* 517 */       k = paramBidiBase.trailingWSStart;
/*     */ 
/* 519 */       int i1 = 0;
/* 520 */       for (int n = 0; n < k; n++)
/*     */       {
/* 522 */         if (arrayOfByte[n] != b1) {
/* 523 */           i1++;
/* 524 */           b1 = arrayOfByte[n];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 532 */       if ((i1 == 1) && (k == i))
/*     */       {
/* 534 */         getSingleRun(paramBidiBase, arrayOfByte[0]);
/*     */       }
/*     */       else
/*     */       {
/* 539 */         byte b2 = 62;
/* 540 */         byte b3 = 0;
/*     */ 
/* 543 */         if (k < i) {
/* 544 */           i1++;
/*     */         }
/*     */ 
/* 548 */         paramBidiBase.getRunsMemory(i1);
/* 549 */         BidiRun[] arrayOfBidiRun = paramBidiBase.runsMemory;
/*     */ 
/* 557 */         int i2 = 0;
/*     */ 
/* 560 */         n = 0;
/*     */         do
/*     */         {
/* 563 */           int i3 = n;
/* 564 */           b1 = arrayOfByte[n];
/* 565 */           if (b1 < b2) {
/* 566 */             b2 = b1;
/*     */           }
/* 568 */           if (b1 > b3) {
/* 569 */             b3 = b1;
/*     */           }
/*     */           do
/*     */           {
/* 573 */             n++; } while ((n < k) && (arrayOfByte[n] == b1));
/*     */ 
/* 576 */           arrayOfBidiRun[i2] = new BidiRun(i3, n - i3, b1);
/* 577 */           i2++;
/* 578 */         }while (n < k);
/*     */ 
/* 580 */         if (k < i)
/*     */         {
/* 582 */           arrayOfBidiRun[i2] = new BidiRun(k, i - k, paramBidiBase.paraLevel);
/*     */ 
/* 585 */           if (paramBidiBase.paraLevel < b2) {
/* 586 */             b2 = paramBidiBase.paraLevel;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 591 */         paramBidiBase.runs = arrayOfBidiRun;
/* 592 */         paramBidiBase.runCount = i1;
/*     */ 
/* 594 */         reorderLine(paramBidiBase, b2, b3);
/*     */ 
/* 598 */         k = 0;
/* 599 */         for (n = 0; n < i1; n++) {
/* 600 */           arrayOfBidiRun[n].level = arrayOfByte[arrayOfBidiRun[n].start];
/* 601 */           k = arrayOfBidiRun[n].limit += k;
/*     */         }
/*     */ 
/* 608 */         if (i2 < i1) {
/* 609 */           int i4 = (paramBidiBase.paraLevel & 0x1) != 0 ? 0 : i2;
/* 610 */           arrayOfBidiRun[i4].level = paramBidiBase.paraLevel;
/*     */         }
/*     */       }
/*     */     }
/*     */     int m;
/* 616 */     if (paramBidiBase.insertPoints.size > 0)
/*     */     {
/* 619 */       for (m = 0; m < paramBidiBase.insertPoints.size; m++) {
/* 620 */         BidiBase.Point localPoint = paramBidiBase.insertPoints.points[m];
/* 621 */         k = getRunFromLogicalIndex(paramBidiBase, localPoint.pos);
/* 622 */         paramBidiBase.runs[k].insertRemove |= localPoint.flag;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 627 */     if (paramBidiBase.controlCount > 0)
/*     */     {
/* 630 */       for (k = 0; k < paramBidiBase.length; k++) {
/* 631 */         m = paramBidiBase.text[k];
/* 632 */         if (BidiBase.IsBidiControlChar(m)) {
/* 633 */           int j = getRunFromLogicalIndex(paramBidiBase, k);
/* 634 */           paramBidiBase.runs[j].insertRemove -= 1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static int[] prepareReorder(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */   {
/* 645 */     if ((paramArrayOfByte1 == null) || (paramArrayOfByte1.length <= 0)) {
/* 646 */       return null;
/*     */     }
/*     */ 
/* 650 */     int k = 62;
/* 651 */     int m = 0;
/* 652 */     for (int i = paramArrayOfByte1.length; i > 0; ) {
/* 653 */       int j = paramArrayOfByte1[(--i)];
/* 654 */       if (j > 62) {
/* 655 */         return null;
/*     */       }
/* 657 */       if (j < k) {
/* 658 */         k = j;
/*     */       }
/* 660 */       if (j > m) {
/* 661 */         m = j;
/*     */       }
/*     */     }
/* 664 */     paramArrayOfByte2[0] = k;
/* 665 */     paramArrayOfByte3[0] = m;
/*     */ 
/* 668 */     int[] arrayOfInt = new int[paramArrayOfByte1.length];
/* 669 */     for (i = paramArrayOfByte1.length; i > 0; ) {
/* 670 */       i--;
/* 671 */       arrayOfInt[i] = i;
/*     */     }
/*     */ 
/* 674 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   static int[] reorderVisual(byte[] paramArrayOfByte)
/*     */   {
/* 679 */     byte[] arrayOfByte1 = new byte[1];
/* 680 */     byte[] arrayOfByte2 = new byte[1];
/*     */ 
/* 684 */     int[] arrayOfInt = prepareReorder(paramArrayOfByte, arrayOfByte1, arrayOfByte2);
/* 685 */     if (arrayOfInt == null) {
/* 686 */       return null;
/*     */     }
/*     */ 
/* 689 */     int n = arrayOfByte1[0];
/* 690 */     int i1 = arrayOfByte2[0];
/*     */ 
/* 693 */     if ((n == i1) && ((n & 0x1) == 0)) {
/* 694 */       return arrayOfInt;
/*     */     }
/*     */ 
/* 698 */     n = (byte)(n | 0x1);
/*     */     do
/*     */     {
/* 702 */       int i = 0;
/*     */       while (true)
/*     */       {
/* 708 */         if ((i < paramArrayOfByte.length) && (paramArrayOfByte[i] < i1)) {
/* 709 */           i++;
/*     */         } else {
/* 711 */           if (i >= paramArrayOfByte.length)
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 716 */           int k = i;
/*     */           do k++; while ((k < paramArrayOfByte.length) && (paramArrayOfByte[k] >= i1));
/*     */ 
/* 724 */           int j = k - 1;
/* 725 */           while (i < j) {
/* 726 */             int m = arrayOfInt[i];
/* 727 */             arrayOfInt[i] = arrayOfInt[j];
/* 728 */             arrayOfInt[j] = m;
/*     */ 
/* 730 */             i++;
/* 731 */             j--;
/*     */           }
/*     */ 
/* 734 */           if (k == paramArrayOfByte.length) {
/*     */             break;
/*     */           }
/* 737 */           i = k + 1;
/*     */         }
/*     */       }
/* 740 */       i1 = (byte)(i1 - 1); } while (i1 >= n);
/*     */ 
/* 742 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   static int[] getVisualMap(BidiBase paramBidiBase)
/*     */   {
/* 748 */     BidiRun[] arrayOfBidiRun = paramBidiBase.runs;
/*     */ 
/* 750 */     int m = paramBidiBase.length > paramBidiBase.resultLength ? paramBidiBase.length : paramBidiBase.resultLength;
/*     */ 
/* 752 */     int[] arrayOfInt1 = new int[m];
/*     */ 
/* 754 */     int j = 0;
/* 755 */     int n = 0;
/*     */     int i;
/*     */     int k;
/* 756 */     for (int i1 = 0; i1 < paramBidiBase.runCount; i1++) {
/* 757 */       i = arrayOfBidiRun[i1].start;
/* 758 */       k = arrayOfBidiRun[i1].limit;
/* 759 */       if (arrayOfBidiRun[i1].isEvenRun()) {
/*     */         do {
/* 761 */           arrayOfInt1[(n++)] = (i++);
/* 762 */           j++; } while (j < k);
/*     */       } else {
/* 764 */         i += k - j;
/*     */         do {
/* 766 */           arrayOfInt1[(n++)] = (--i);
/* 767 */           j++; } while (j < k);
/*     */       }
/*     */     }
/*     */     int i2;
/*     */     int i4;
/*     */     int i3;
/*     */     int i6;
/*     */     int i5;
/* 772 */     if (paramBidiBase.insertPoints.size > 0) {
/* 773 */       i1 = 0; i2 = paramBidiBase.runCount;
/*     */ 
/* 775 */       arrayOfBidiRun = paramBidiBase.runs;
/*     */ 
/* 777 */       for (i4 = 0; i4 < i2; i4++) {
/* 778 */         i3 = arrayOfBidiRun[i4].insertRemove;
/* 779 */         if ((i3 & 0x5) > 0) {
/* 780 */           i1++;
/*     */         }
/* 782 */         if ((i3 & 0xA) > 0) {
/* 783 */           i1++;
/*     */         }
/*     */       }
/*     */ 
/* 787 */       i6 = paramBidiBase.resultLength;
/* 788 */       for (i4 = i2 - 1; (i4 >= 0) && (i1 > 0); i4--) {
/* 789 */         i3 = arrayOfBidiRun[i4].insertRemove;
/* 790 */         if ((i3 & 0xA) > 0) {
/* 791 */           arrayOfInt1[(--i6)] = -1;
/* 792 */           i1--;
/*     */         }
/* 794 */         j = i4 > 0 ? arrayOfBidiRun[(i4 - 1)].limit : 0;
/* 795 */         for (i5 = arrayOfBidiRun[i4].limit - 1; (i5 >= j) && (i1 > 0); i5--) {
/* 796 */           arrayOfInt1[(--i6)] = arrayOfInt1[i5];
/*     */         }
/* 798 */         if ((i3 & 0x5) > 0) {
/* 799 */           arrayOfInt1[(--i6)] = -1;
/* 800 */           i1--;
/*     */         }
/*     */       }
/*     */     }
/* 804 */     else if (paramBidiBase.controlCount > 0) {
/* 805 */       i1 = paramBidiBase.runCount;
/*     */ 
/* 809 */       arrayOfBidiRun = paramBidiBase.runs;
/* 810 */       j = 0;
/*     */ 
/* 812 */       int i7 = 0;
/* 813 */       for (i5 = 0; i5 < i1; j += i4) {
/* 814 */         i4 = arrayOfBidiRun[i5].limit - j;
/* 815 */         i3 = arrayOfBidiRun[i5].insertRemove;
/*     */ 
/* 817 */         if ((i3 == 0) && (i7 == j)) {
/* 818 */           i7 += i4;
/*     */         }
/*     */         else
/*     */         {
/* 822 */           if (i3 == 0) {
/* 823 */             k = arrayOfBidiRun[i5].limit;
/* 824 */             for (i6 = j; i6 < k; i6++) {
/* 825 */               arrayOfInt1[(i7++)] = arrayOfInt1[i6];
/*     */             }
/*     */           }
/*     */ 
/* 829 */           i = arrayOfBidiRun[i5].start;
/* 830 */           boolean bool = arrayOfBidiRun[i5].isEvenRun();
/* 831 */           i2 = i + i4 - 1;
/* 832 */           for (i6 = 0; i6 < i4; i6++) {
/* 833 */             int i8 = bool ? i + i6 : i2 - i6;
/* 834 */             int i9 = paramBidiBase.text[i8];
/* 835 */             if (!BidiBase.IsBidiControlChar(i9))
/* 836 */               arrayOfInt1[(i7++)] = i8;
/*     */           }
/*     */         }
/* 813 */         i5++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 841 */     if (m == paramBidiBase.resultLength) {
/* 842 */       return arrayOfInt1;
/*     */     }
/* 844 */     int[] arrayOfInt2 = new int[paramBidiBase.resultLength];
/* 845 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, paramBidiBase.resultLength);
/* 846 */     return arrayOfInt2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.bidi.BidiLine
 * JD-Core Version:    0.6.2
 */