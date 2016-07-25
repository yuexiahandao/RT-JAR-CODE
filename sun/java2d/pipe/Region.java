/*      */ package sun.java2d.pipe;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.RectangularShape;
/*      */ 
/*      */ public class Region
/*      */ {
/*      */   static final int INIT_SIZE = 50;
/*      */   static final int GROW_SIZE = 50;
/*   83 */   public static final Region EMPTY_REGION = new ImmutableRegion(0, 0, 0, 0);
/*   84 */   public static final Region WHOLE_REGION = new ImmutableRegion(-2147483648, -2147483648, 2147483647, 2147483647);
/*      */   int lox;
/*      */   int loy;
/*      */   int hix;
/*      */   int hiy;
/*      */   int endIndex;
/*      */   int[] bands;
/*      */   static final int INCLUDE_A = 1;
/*      */   static final int INCLUDE_B = 2;
/*      */   static final int INCLUDE_COMMON = 4;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public static int dimAdd(int paramInt1, int paramInt2)
/*      */   {
/*  112 */     if (paramInt2 <= 0) return paramInt1;
/*  113 */     if (paramInt2 += paramInt1 < paramInt1) return 2147483647;
/*  114 */     return paramInt2;
/*      */   }
/*      */ 
/*      */   public static int clipAdd(int paramInt1, int paramInt2)
/*      */   {
/*  127 */     int i = paramInt1 + paramInt2;
/*  128 */     if ((i > paramInt1 ? 1 : 0) != (paramInt2 > 0 ? 1 : 0)) {
/*  129 */       i = paramInt2 < 0 ? -2147483648 : 2147483647;
/*      */     }
/*  131 */     return i;
/*      */   }
/*      */ 
/*      */   public static int clipScale(int paramInt, double paramDouble)
/*      */   {
/*  143 */     if (paramDouble == 1.0D) {
/*  144 */       return paramInt;
/*      */     }
/*  146 */     double d = paramInt * paramDouble;
/*  147 */     if (d < -2147483648.0D) {
/*  148 */       return -2147483648;
/*      */     }
/*  150 */     if (d > 2147483647.0D) {
/*  151 */       return 2147483647;
/*      */     }
/*  153 */     return (int)Math.round(d);
/*      */   }
/*      */ 
/*      */   protected Region(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  157 */     this.lox = paramInt1;
/*  158 */     this.loy = paramInt2;
/*  159 */     this.hix = paramInt3;
/*  160 */     this.hiy = paramInt4;
/*      */   }
/*      */ 
/*      */   public static Region getInstance(Shape paramShape, AffineTransform paramAffineTransform)
/*      */   {
/*  176 */     return getInstance(WHOLE_REGION, false, paramShape, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public static Region getInstance(Region paramRegion, Shape paramShape, AffineTransform paramAffineTransform)
/*      */   {
/*  204 */     return getInstance(paramRegion, false, paramShape, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public static Region getInstance(Region paramRegion, boolean paramBoolean, Shape paramShape, AffineTransform paramAffineTransform)
/*      */   {
/*  238 */     if (((paramShape instanceof RectangularShape)) && (((RectangularShape)paramShape).isEmpty()))
/*      */     {
/*  241 */       return EMPTY_REGION;
/*      */     }
/*      */ 
/*  244 */     int[] arrayOfInt = new int[4];
/*  245 */     ShapeSpanIterator localShapeSpanIterator = new ShapeSpanIterator(paramBoolean);
/*      */     try {
/*  247 */       localShapeSpanIterator.setOutputArea(paramRegion);
/*  248 */       localShapeSpanIterator.appendPath(paramShape.getPathIterator(paramAffineTransform));
/*  249 */       localShapeSpanIterator.getPathBox(arrayOfInt);
/*  250 */       Region localRegion1 = getInstance(arrayOfInt);
/*  251 */       localRegion1.appendSpans(localShapeSpanIterator);
/*  252 */       return localRegion1;
/*      */     } finally {
/*  254 */       localShapeSpanIterator.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Region getInstance(Rectangle paramRectangle)
/*      */   {
/*  266 */     return getInstanceXYWH(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public static Region getInstanceXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  277 */     return getInstanceXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public static Region getInstance(int[] paramArrayOfInt)
/*      */   {
/*  288 */     return new Region(paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2], paramArrayOfInt[3]);
/*      */   }
/*      */ 
/*      */   public static Region getInstanceXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  299 */     return new Region(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void setOutputArea(Rectangle paramRectangle)
/*      */   {
/*  310 */     setOutputAreaXYWH(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public void setOutputAreaXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  323 */     setOutputAreaXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public void setOutputArea(int[] paramArrayOfInt)
/*      */   {
/*  334 */     this.lox = paramArrayOfInt[0];
/*  335 */     this.loy = paramArrayOfInt[1];
/*  336 */     this.hix = paramArrayOfInt[2];
/*  337 */     this.hiy = paramArrayOfInt[3];
/*      */   }
/*      */ 
/*      */   public void setOutputAreaXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  349 */     this.lox = paramInt1;
/*  350 */     this.loy = paramInt2;
/*  351 */     this.hix = paramInt3;
/*  352 */     this.hiy = paramInt4;
/*      */   }
/*      */ 
/*      */   public void appendSpans(SpanIterator paramSpanIterator)
/*      */   {
/*  363 */     int[] arrayOfInt = new int[6];
/*      */ 
/*  365 */     while (paramSpanIterator.nextSpan(arrayOfInt)) {
/*  366 */       appendSpan(arrayOfInt);
/*      */     }
/*      */ 
/*  369 */     endRow(arrayOfInt);
/*  370 */     calcBBox();
/*      */   }
/*      */ 
/*      */   public Region getScaledRegion(double paramDouble1, double paramDouble2)
/*      */   {
/*  378 */     if ((paramDouble1 == 0.0D) || (paramDouble2 == 0.0D) || (this == EMPTY_REGION)) {
/*  379 */       return EMPTY_REGION;
/*      */     }
/*  381 */     if (((paramDouble1 == 1.0D) && (paramDouble2 == 1.0D)) || (this == WHOLE_REGION)) {
/*  382 */       return this;
/*      */     }
/*      */ 
/*  385 */     int i = clipScale(this.lox, paramDouble1);
/*  386 */     int j = clipScale(this.loy, paramDouble2);
/*  387 */     int k = clipScale(this.hix, paramDouble1);
/*  388 */     int m = clipScale(this.hiy, paramDouble2);
/*  389 */     Region localRegion = new Region(i, j, k, m);
/*  390 */     int[] arrayOfInt1 = this.bands;
/*  391 */     if (arrayOfInt1 != null) {
/*  392 */       int n = this.endIndex;
/*  393 */       int[] arrayOfInt2 = new int[n];
/*  394 */       int i1 = 0;
/*  395 */       int i2 = 0;
/*      */ 
/*  397 */       while (i1 < n)
/*      */       {
/*      */         int tmp156_153 = clipScale(arrayOfInt1[(i1++)], paramDouble2); int i4 = tmp156_153; arrayOfInt2[(i2++)] = tmp156_153;
/*      */         int tmp179_176 = clipScale(arrayOfInt1[(i1++)], paramDouble2); int i5 = tmp179_176; arrayOfInt2[(i2++)] = tmp179_176;
/*      */         int tmp198_197 = arrayOfInt1[(i1++)]; int i3 = tmp198_197; arrayOfInt2[(i2++)] = tmp198_197;
/*  402 */         int i6 = i2;
/*  403 */         if (i4 < i5) while (true) {
/*  404 */             i3--; if (i3 < 0) break;
/*  405 */             int i7 = clipScale(arrayOfInt1[(i1++)], paramDouble1);
/*  406 */             int i8 = clipScale(arrayOfInt1[(i1++)], paramDouble1);
/*  407 */             if (i7 < i8) {
/*  408 */               arrayOfInt2[(i2++)] = i7;
/*  409 */               arrayOfInt2[(i2++)] = i8;
/*      */             }
/*      */           }
/*      */ 
/*  413 */         i1 += i3 * 2;
/*      */ 
/*  416 */         if (i2 > i6)
/*  417 */           arrayOfInt2[(i6 - 1)] = ((i2 - i6) / 2);
/*      */         else {
/*  419 */           i2 = i6 - 3;
/*      */         }
/*      */       }
/*  422 */       if (i2 <= 5) {
/*  423 */         if (i2 < 5)
/*      */         {
/*  425 */           localRegion.lox = (localRegion.loy = localRegion.hix = localRegion.hiy = 0);
/*      */         }
/*      */         else {
/*  428 */           localRegion.loy = arrayOfInt2[0];
/*  429 */           localRegion.hiy = arrayOfInt2[1];
/*  430 */           localRegion.lox = arrayOfInt2[3];
/*  431 */           localRegion.hix = arrayOfInt2[4];
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  438 */         localRegion.endIndex = i2;
/*  439 */         localRegion.bands = arrayOfInt2;
/*      */       }
/*      */     }
/*  442 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getTranslatedRegion(int paramInt1, int paramInt2)
/*      */   {
/*  452 */     if ((paramInt1 | paramInt2) == 0) {
/*  453 */       return this;
/*      */     }
/*  455 */     int i = this.lox + paramInt1;
/*  456 */     int j = this.loy + paramInt2;
/*  457 */     int k = this.hix + paramInt1;
/*  458 */     int m = this.hiy + paramInt2;
/*  459 */     if ((i > this.lox ? 1 : 0) == (paramInt1 > 0 ? 1 : 0)) if ((j > this.loy ? 1 : 0) == (paramInt2 > 0 ? 1 : 0)) if ((k > this.hix ? 1 : 0) == (paramInt1 > 0 ? 1 : 0)) if ((m > this.hiy ? 1 : 0) == (paramInt2 > 0 ? 1 : 0))
/*      */           {
/*      */             break label149;
/*      */           }
/*      */ 
/*  464 */     return getSafeTranslatedRegion(paramInt1, paramInt2);
/*      */ 
/*  466 */     label149: Region localRegion = new Region(i, j, k, m);
/*  467 */     int[] arrayOfInt1 = this.bands;
/*  468 */     if (arrayOfInt1 != null) {
/*  469 */       int n = this.endIndex;
/*  470 */       localRegion.endIndex = n;
/*  471 */       int[] arrayOfInt2 = new int[n];
/*  472 */       localRegion.bands = arrayOfInt2;
/*  473 */       int i1 = 0;
/*      */ 
/*  475 */       while (i1 < n) {
/*  476 */         arrayOfInt1[i1] += paramInt2; i1++;
/*  477 */         arrayOfInt1[i1] += paramInt2; i1++;
/*      */         int tmp251_250 = arrayOfInt1[i1]; int i2 = tmp251_250; arrayOfInt2[i1] = tmp251_250; i1++;
/*      */         while (true) { i2--; if (i2 < 0) break;
/*  480 */           arrayOfInt1[i1] += paramInt1; i1++;
/*  481 */           arrayOfInt1[i1] += paramInt1; i1++;
/*      */         }
/*      */       }
/*      */     }
/*  485 */     return localRegion;
/*      */   }
/*      */ 
/*      */   private Region getSafeTranslatedRegion(int paramInt1, int paramInt2) {
/*  489 */     int i = clipAdd(this.lox, paramInt1);
/*  490 */     int j = clipAdd(this.loy, paramInt2);
/*  491 */     int k = clipAdd(this.hix, paramInt1);
/*  492 */     int m = clipAdd(this.hiy, paramInt2);
/*  493 */     Region localRegion = new Region(i, j, k, m);
/*  494 */     int[] arrayOfInt1 = this.bands;
/*  495 */     if (arrayOfInt1 != null) {
/*  496 */       int n = this.endIndex;
/*  497 */       int[] arrayOfInt2 = new int[n];
/*  498 */       int i1 = 0;
/*  499 */       int i2 = 0;
/*      */ 
/*  501 */       while (i1 < n)
/*      */       {
/*      */         int tmp110_107 = clipAdd(arrayOfInt1[(i1++)], paramInt2); int i4 = tmp110_107; arrayOfInt2[(i2++)] = tmp110_107;
/*      */         int tmp133_130 = clipAdd(arrayOfInt1[(i1++)], paramInt2); int i5 = tmp133_130; arrayOfInt2[(i2++)] = tmp133_130;
/*      */         int tmp152_151 = arrayOfInt1[(i1++)]; int i3 = tmp152_151; arrayOfInt2[(i2++)] = tmp152_151;
/*  506 */         int i6 = i2;
/*  507 */         if (i4 < i5) while (true) {
/*  508 */             i3--; if (i3 < 0) break;
/*  509 */             int i7 = clipAdd(arrayOfInt1[(i1++)], paramInt1);
/*  510 */             int i8 = clipAdd(arrayOfInt1[(i1++)], paramInt1);
/*  511 */             if (i7 < i8) {
/*  512 */               arrayOfInt2[(i2++)] = i7;
/*  513 */               arrayOfInt2[(i2++)] = i8;
/*      */             }
/*      */           }
/*      */ 
/*  517 */         i1 += i3 * 2;
/*      */ 
/*  520 */         if (i2 > i6)
/*  521 */           arrayOfInt2[(i6 - 1)] = ((i2 - i6) / 2);
/*      */         else {
/*  523 */           i2 = i6 - 3;
/*      */         }
/*      */       }
/*  526 */       if (i2 <= 5) {
/*  527 */         if (i2 < 5)
/*      */         {
/*  529 */           localRegion.lox = (localRegion.loy = localRegion.hix = localRegion.hiy = 0);
/*      */         }
/*      */         else {
/*  532 */           localRegion.loy = arrayOfInt2[0];
/*  533 */           localRegion.hiy = arrayOfInt2[1];
/*  534 */           localRegion.lox = arrayOfInt2[3];
/*  535 */           localRegion.hix = arrayOfInt2[4];
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  542 */         localRegion.endIndex = i2;
/*  543 */         localRegion.bands = arrayOfInt2;
/*      */       }
/*      */     }
/*  546 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getIntersection(Rectangle paramRectangle)
/*      */   {
/*  555 */     return getIntersectionXYWH(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public Region getIntersectionXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  564 */     return getIntersectionXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public Region getIntersectionXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  573 */     if (isInsideXYXY(paramInt1, paramInt2, paramInt3, paramInt4)) {
/*  574 */       return this;
/*      */     }
/*  576 */     Region localRegion = new Region(paramInt1 < this.lox ? this.lox : paramInt1, paramInt2 < this.loy ? this.loy : paramInt2, paramInt3 > this.hix ? this.hix : paramInt3, paramInt4 > this.hiy ? this.hiy : paramInt4);
/*      */ 
/*  580 */     if (this.bands != null) {
/*  581 */       localRegion.appendSpans(getSpanIterator());
/*      */     }
/*  583 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getIntersection(Region paramRegion)
/*      */   {
/*  599 */     if (isInsideQuickCheck(paramRegion)) {
/*  600 */       return this;
/*      */     }
/*  602 */     if (paramRegion.isInsideQuickCheck(this)) {
/*  603 */       return paramRegion;
/*      */     }
/*  605 */     Region localRegion = new Region(paramRegion.lox < this.lox ? this.lox : paramRegion.lox, paramRegion.loy < this.loy ? this.loy : paramRegion.loy, paramRegion.hix > this.hix ? this.hix : paramRegion.hix, paramRegion.hiy > this.hiy ? this.hiy : paramRegion.hiy);
/*      */ 
/*  609 */     if (!localRegion.isEmpty()) {
/*  610 */       localRegion.filterSpans(this, paramRegion, 4);
/*      */     }
/*  612 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getUnion(Region paramRegion)
/*      */   {
/*  628 */     if ((paramRegion.isEmpty()) || (paramRegion.isInsideQuickCheck(this))) {
/*  629 */       return this;
/*      */     }
/*  631 */     if ((isEmpty()) || (isInsideQuickCheck(paramRegion))) {
/*  632 */       return paramRegion;
/*      */     }
/*  634 */     Region localRegion = new Region(paramRegion.lox > this.lox ? this.lox : paramRegion.lox, paramRegion.loy > this.loy ? this.loy : paramRegion.loy, paramRegion.hix < this.hix ? this.hix : paramRegion.hix, paramRegion.hiy < this.hiy ? this.hiy : paramRegion.hiy);
/*      */ 
/*  638 */     localRegion.filterSpans(this, paramRegion, 7);
/*  639 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getDifference(Region paramRegion)
/*      */   {
/*  655 */     if (!paramRegion.intersectsQuickCheck(this)) {
/*  656 */       return this;
/*      */     }
/*  658 */     if (isInsideQuickCheck(paramRegion)) {
/*  659 */       return EMPTY_REGION;
/*      */     }
/*  661 */     Region localRegion = new Region(this.lox, this.loy, this.hix, this.hiy);
/*  662 */     localRegion.filterSpans(this, paramRegion, 1);
/*  663 */     return localRegion;
/*      */   }
/*      */ 
/*      */   public Region getExclusiveOr(Region paramRegion)
/*      */   {
/*  679 */     if (paramRegion.isEmpty()) {
/*  680 */       return this;
/*      */     }
/*  682 */     if (isEmpty()) {
/*  683 */       return paramRegion;
/*      */     }
/*  685 */     Region localRegion = new Region(paramRegion.lox > this.lox ? this.lox : paramRegion.lox, paramRegion.loy > this.loy ? this.loy : paramRegion.loy, paramRegion.hix < this.hix ? this.hix : paramRegion.hix, paramRegion.hiy < this.hiy ? this.hiy : paramRegion.hiy);
/*      */ 
/*  689 */     localRegion.filterSpans(this, paramRegion, 3);
/*  690 */     return localRegion;
/*      */   }
/*      */ 
/*      */   private void filterSpans(Region paramRegion1, Region paramRegion2, int paramInt)
/*      */   {
/*  698 */     int[] arrayOfInt1 = paramRegion1.bands;
/*  699 */     int[] arrayOfInt2 = paramRegion2.bands;
/*  700 */     if (arrayOfInt1 == null) {
/*  701 */       arrayOfInt1 = new int[] { paramRegion1.loy, paramRegion1.hiy, 1, paramRegion1.lox, paramRegion1.hix };
/*      */     }
/*  703 */     if (arrayOfInt2 == null) {
/*  704 */       arrayOfInt2 = new int[] { paramRegion2.loy, paramRegion2.hiy, 1, paramRegion2.lox, paramRegion2.hix };
/*      */     }
/*  706 */     int[] arrayOfInt3 = new int[6];
/*  707 */     int i = 0;
/*  708 */     int j = arrayOfInt1[(i++)];
/*  709 */     int k = arrayOfInt1[(i++)];
/*  710 */     int m = arrayOfInt1[(i++)];
/*  711 */     m = i + 2 * m;
/*  712 */     int n = 0;
/*  713 */     int i1 = arrayOfInt2[(n++)];
/*  714 */     int i2 = arrayOfInt2[(n++)];
/*  715 */     int i3 = arrayOfInt2[(n++)];
/*  716 */     i3 = n + 2 * i3;
/*  717 */     int i4 = this.loy;
/*  718 */     while (i4 < this.hiy)
/*  719 */       if (i4 >= k) {
/*  720 */         if (m < paramRegion1.endIndex) {
/*  721 */           i = m;
/*  722 */           j = arrayOfInt1[(i++)];
/*  723 */           k = arrayOfInt1[(i++)];
/*  724 */           m = arrayOfInt1[(i++)];
/*  725 */           m = i + 2 * m;
/*      */         } else {
/*  727 */           if ((paramInt & 0x2) == 0) break;
/*  728 */           j = k = this.hiy;
/*      */         }
/*      */ 
/*      */       }
/*  732 */       else if (i4 >= i2) {
/*  733 */         if (i3 < paramRegion2.endIndex) {
/*  734 */           n = i3;
/*  735 */           i1 = arrayOfInt2[(n++)];
/*  736 */           i2 = arrayOfInt2[(n++)];
/*  737 */           i3 = arrayOfInt2[(n++)];
/*  738 */           i3 = n + 2 * i3;
/*      */         } else {
/*  740 */           if ((paramInt & 0x1) == 0) break;
/*  741 */           i1 = i2 = this.hiy;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i5;
/*      */         int i6;
/*  746 */         if (i4 < i1) {
/*  747 */           if (i4 < j) {
/*  748 */             i4 = Math.min(j, i1);
/*      */           }
/*      */           else
/*      */           {
/*  752 */             i5 = Math.min(k, i1);
/*  753 */             if ((paramInt & 0x1) != 0) {
/*  754 */               arrayOfInt3[1] = i4;
/*  755 */               arrayOfInt3[3] = i5;
/*  756 */               i6 = i;
/*  757 */               while (i6 < m) {
/*  758 */                 arrayOfInt3[0] = arrayOfInt1[(i6++)];
/*  759 */                 arrayOfInt3[2] = arrayOfInt1[(i6++)];
/*  760 */                 appendSpan(arrayOfInt3);
/*      */               }
/*      */             }
/*      */           } } else { if (i4 < j)
/*      */           {
/*  765 */             i5 = Math.min(i2, j);
/*  766 */             if ((paramInt & 0x2) != 0) {
/*  767 */               arrayOfInt3[1] = i4;
/*  768 */               arrayOfInt3[3] = i5;
/*  769 */               i6 = n;
/*  770 */               while (i6 < i3) {
/*  771 */                 arrayOfInt3[0] = arrayOfInt2[(i6++)];
/*  772 */                 arrayOfInt3[2] = arrayOfInt2[(i6++)];
/*  773 */                 appendSpan(arrayOfInt3);
/*      */               }
/*      */             }
/*      */           }
/*      */           else {
/*  778 */             i5 = Math.min(k, i2);
/*  779 */             arrayOfInt3[1] = i4;
/*  780 */             arrayOfInt3[3] = i5;
/*  781 */             i6 = i;
/*  782 */             int i7 = n;
/*  783 */             int i8 = arrayOfInt1[(i6++)];
/*  784 */             int i9 = arrayOfInt1[(i6++)];
/*  785 */             int i10 = arrayOfInt2[(i7++)];
/*  786 */             int i11 = arrayOfInt2[(i7++)];
/*  787 */             int i12 = Math.min(i8, i10);
/*  788 */             if (i12 < this.lox) i12 = this.lox;
/*  789 */             while (i12 < this.hix)
/*  790 */               if (i12 >= i9) {
/*  791 */                 if (i6 < m) {
/*  792 */                   i8 = arrayOfInt1[(i6++)];
/*  793 */                   i9 = arrayOfInt1[(i6++)];
/*      */                 } else {
/*  795 */                   if ((paramInt & 0x2) == 0) break;
/*  796 */                   i8 = i9 = this.hix;
/*      */                 }
/*      */ 
/*      */               }
/*  800 */               else if (i12 >= i11) {
/*  801 */                 if (i7 < i3) {
/*  802 */                   i10 = arrayOfInt2[(i7++)];
/*  803 */                   i11 = arrayOfInt2[(i7++)];
/*      */                 } else {
/*  805 */                   if ((paramInt & 0x1) == 0) break;
/*  806 */                   i10 = i11 = this.hix;
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/*      */                 int i13;
/*      */                 int i14;
/*  812 */                 if (i12 < i10) {
/*  813 */                   if (i12 < i8) {
/*  814 */                     i13 = Math.min(i8, i10);
/*  815 */                     i14 = 0;
/*      */                   } else {
/*  817 */                     i13 = Math.min(i9, i10);
/*  818 */                     i14 = (paramInt & 0x1) != 0 ? 1 : 0;
/*      */                   }
/*  820 */                 } else if (i12 < i8) {
/*  821 */                   i13 = Math.min(i8, i11);
/*  822 */                   i14 = (paramInt & 0x2) != 0 ? 1 : 0;
/*      */                 } else {
/*  824 */                   i13 = Math.min(i9, i11);
/*  825 */                   i14 = (paramInt & 0x4) != 0 ? 1 : 0;
/*      */                 }
/*  827 */                 if (i14 != 0) {
/*  828 */                   arrayOfInt3[0] = i12;
/*  829 */                   arrayOfInt3[2] = i13;
/*  830 */                   appendSpan(arrayOfInt3);
/*      */                 }
/*  832 */                 i12 = i13;
/*      */               }
/*      */           }
/*  835 */           i4 = i5; }
/*      */       }
/*  837 */     endRow(arrayOfInt3);
/*  838 */     calcBBox();
/*      */   }
/*      */ 
/*      */   public Region getBoundsIntersection(Rectangle paramRectangle)
/*      */   {
/*  850 */     return getBoundsIntersectionXYWH(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public Region getBoundsIntersectionXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  862 */     return getBoundsIntersectionXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public Region getBoundsIntersectionXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  876 */     if ((this.bands == null) && (this.lox >= paramInt1) && (this.loy >= paramInt2) && (this.hix <= paramInt3) && (this.hiy <= paramInt4))
/*      */     {
/*  880 */       return this;
/*      */     }
/*  882 */     return new Region(paramInt1 < this.lox ? this.lox : paramInt1, paramInt2 < this.loy ? this.loy : paramInt2, paramInt3 > this.hix ? this.hix : paramInt3, paramInt4 > this.hiy ? this.hiy : paramInt4);
/*      */   }
/*      */ 
/*      */   public Region getBoundsIntersection(Region paramRegion)
/*      */   {
/*  897 */     if (encompasses(paramRegion)) {
/*  898 */       return paramRegion;
/*      */     }
/*  900 */     if (paramRegion.encompasses(this)) {
/*  901 */       return this;
/*      */     }
/*  903 */     return new Region(paramRegion.lox < this.lox ? this.lox : paramRegion.lox, paramRegion.loy < this.loy ? this.loy : paramRegion.loy, paramRegion.hix > this.hix ? this.hix : paramRegion.hix, paramRegion.hiy > this.hiy ? this.hiy : paramRegion.hiy);
/*      */   }
/*      */ 
/*      */   private void appendSpan(int[] paramArrayOfInt)
/*      */   {
/*  919 */     int i;
/*  919 */     if ((i = paramArrayOfInt[0]) < this.lox) i = this.lox;
/*  920 */     int j;
/*  920 */     if ((j = paramArrayOfInt[1]) < this.loy) j = this.loy;
/*  921 */     int k;
/*  921 */     if ((k = paramArrayOfInt[2]) > this.hix) k = this.hix;
/*  922 */     int m;
/*  922 */     if ((m = paramArrayOfInt[3]) > this.hiy) m = this.hiy;
/*  923 */     if ((k <= i) || (m <= j)) {
/*  924 */       return;
/*      */     }
/*      */ 
/*  927 */     int n = paramArrayOfInt[4];
/*  928 */     if ((this.endIndex == 0) || (j >= this.bands[(n + 1)])) {
/*  929 */       if (this.bands == null) {
/*  930 */         this.bands = new int[50];
/*      */       } else {
/*  932 */         needSpace(5);
/*  933 */         endRow(paramArrayOfInt);
/*  934 */         n = paramArrayOfInt[4];
/*      */       }
/*  936 */       this.bands[(this.endIndex++)] = j;
/*  937 */       this.bands[(this.endIndex++)] = m;
/*  938 */       this.bands[(this.endIndex++)] = 0;
/*  939 */     } else if ((j == this.bands[n]) && (m == this.bands[(n + 1)]) && (i >= this.bands[(this.endIndex - 1)]))
/*      */     {
/*  942 */       if (i == this.bands[(this.endIndex - 1)]) {
/*  943 */         this.bands[(this.endIndex - 1)] = k;
/*  944 */         return;
/*      */       }
/*  946 */       needSpace(2);
/*      */     } else {
/*  948 */       throw new InternalError("bad span");
/*      */     }
/*  950 */     this.bands[(this.endIndex++)] = i;
/*  951 */     this.bands[(this.endIndex++)] = k;
/*  952 */     this.bands[(n + 2)] += 1;
/*      */   }
/*      */ 
/*      */   private void needSpace(int paramInt) {
/*  956 */     if (this.endIndex + paramInt >= this.bands.length) {
/*  957 */       int[] arrayOfInt = new int[this.bands.length + 50];
/*  958 */       System.arraycopy(this.bands, 0, arrayOfInt, 0, this.endIndex);
/*  959 */       this.bands = arrayOfInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void endRow(int[] paramArrayOfInt) {
/*  964 */     int i = paramArrayOfInt[4];
/*  965 */     int j = paramArrayOfInt[5];
/*  966 */     if (i > j) {
/*  967 */       int[] arrayOfInt = this.bands;
/*  968 */       if ((arrayOfInt[(j + 1)] == arrayOfInt[i]) && (arrayOfInt[(j + 2)] == arrayOfInt[(i + 2)]))
/*      */       {
/*  971 */         int k = arrayOfInt[(i + 2)] * 2;
/*  972 */         i += 3;
/*  973 */         j += 3;
/*  974 */         while ((k > 0) && 
/*  975 */           (arrayOfInt[(i++)] == arrayOfInt[(j++)]))
/*      */         {
/*  978 */           k--;
/*      */         }
/*  980 */         if (k == 0)
/*      */         {
/*  982 */           arrayOfInt[(paramArrayOfInt[5] + 1)] = arrayOfInt[(j + 1)];
/*  983 */           this.endIndex = j;
/*  984 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  988 */     paramArrayOfInt[5] = paramArrayOfInt[4];
/*  989 */     paramArrayOfInt[4] = this.endIndex;
/*      */   }
/*      */ 
/*      */   private void calcBBox() {
/*  993 */     int[] arrayOfInt = this.bands;
/*  994 */     if (this.endIndex <= 5) {
/*  995 */       if (this.endIndex == 0) {
/*  996 */         this.lox = (this.loy = this.hix = this.hiy = 0);
/*      */       } else {
/*  998 */         this.loy = arrayOfInt[0];
/*  999 */         this.hiy = arrayOfInt[1];
/* 1000 */         this.lox = arrayOfInt[3];
/* 1001 */         this.hix = arrayOfInt[4];
/* 1002 */         this.endIndex = 0;
/*      */       }
/* 1004 */       this.bands = null;
/* 1005 */       return;
/*      */     }
/* 1007 */     int i = this.hix;
/* 1008 */     int j = this.lox;
/* 1009 */     int k = 0;
/*      */ 
/* 1011 */     int m = 0;
/* 1012 */     while (m < this.endIndex) {
/* 1013 */       k = m;
/* 1014 */       int n = arrayOfInt[(m + 2)];
/* 1015 */       m += 3;
/* 1016 */       if (i > arrayOfInt[m]) {
/* 1017 */         i = arrayOfInt[m];
/*      */       }
/* 1019 */       m += n * 2;
/* 1020 */       if (j < arrayOfInt[(m - 1)]) {
/* 1021 */         j = arrayOfInt[(m - 1)];
/*      */       }
/*      */     }
/*      */ 
/* 1025 */     this.lox = i;
/* 1026 */     this.loy = arrayOfInt[0];
/* 1027 */     this.hix = j;
/* 1028 */     this.hiy = arrayOfInt[(k + 1)];
/*      */   }
/*      */ 
/*      */   public final int getLoX()
/*      */   {
/* 1035 */     return this.lox;
/*      */   }
/*      */ 
/*      */   public final int getLoY()
/*      */   {
/* 1042 */     return this.loy;
/*      */   }
/*      */ 
/*      */   public final int getHiX()
/*      */   {
/* 1049 */     return this.hix;
/*      */   }
/*      */ 
/*      */   public final int getHiY()
/*      */   {
/* 1056 */     return this.hiy;
/*      */   }
/*      */ 
/*      */   public final int getWidth()
/*      */   {
/* 1063 */     if (this.hix < this.lox) return 0;
/*      */     int i;
/* 1065 */     if ((i = this.hix - this.lox) < 0) {
/* 1066 */       i = 2147483647;
/*      */     }
/* 1068 */     return i;
/*      */   }
/*      */ 
/*      */   public final int getHeight()
/*      */   {
/* 1075 */     if (this.hiy < this.loy) return 0;
/*      */     int i;
/* 1077 */     if ((i = this.hiy - this.loy) < 0) {
/* 1078 */       i = 2147483647;
/*      */     }
/* 1080 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1087 */     return (this.hix <= this.lox) || (this.hiy <= this.loy);
/*      */   }
/*      */ 
/*      */   public boolean isRectangular()
/*      */   {
/* 1095 */     return this.bands == null;
/*      */   }
/*      */ 
/*      */   public boolean contains(int paramInt1, int paramInt2)
/*      */   {
/* 1102 */     if ((paramInt1 < this.lox) || (paramInt1 >= this.hix) || (paramInt2 < this.loy) || (paramInt2 >= this.hiy)) return false;
/* 1103 */     if (this.bands == null) return true;
/* 1104 */     int i = 0;
/* 1105 */     while (i < this.endIndex) {
/* 1106 */       if (paramInt2 < this.bands[(i++)])
/* 1107 */         return false;
/*      */       int j;
/* 1109 */       if (paramInt2 >= this.bands[(i++)]) {
/* 1110 */         j = this.bands[(i++)];
/* 1111 */         i += j * 2;
/*      */       } else {
/* 1113 */         j = this.bands[(i++)];
/* 1114 */         j = i + j * 2;
/* 1115 */         while (i < j) {
/* 1116 */           if (paramInt1 < this.bands[(i++)]) return false;
/* 1117 */           if (paramInt1 < this.bands[(i++)]) return true;
/*      */         }
/* 1119 */         return false;
/*      */       }
/*      */     }
/* 1122 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isInsideXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1131 */     return isInsideXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public boolean isInsideXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1139 */     return (this.lox >= paramInt1) && (this.loy >= paramInt2) && (this.hix <= paramInt3) && (this.hiy <= paramInt4);
/*      */   }
/*      */ 
/*      */   public boolean isInsideQuickCheck(Region paramRegion)
/*      */   {
/* 1152 */     return (paramRegion.bands == null) && (paramRegion.lox <= this.lox) && (paramRegion.loy <= this.loy) && (paramRegion.hix >= this.hix) && (paramRegion.hiy >= this.hiy);
/*      */   }
/*      */ 
/*      */   public boolean intersectsQuickCheckXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1168 */     return (paramInt3 > this.lox) && (paramInt1 < this.hix) && (paramInt4 > this.loy) && (paramInt2 < this.hiy);
/*      */   }
/*      */ 
/*      */   public boolean intersectsQuickCheck(Region paramRegion)
/*      */   {
/* 1181 */     return (paramRegion.hix > this.lox) && (paramRegion.lox < this.hix) && (paramRegion.hiy > this.loy) && (paramRegion.loy < this.hiy);
/*      */   }
/*      */ 
/*      */   public boolean encompasses(Region paramRegion)
/*      */   {
/* 1193 */     return (this.bands == null) && (this.lox <= paramRegion.lox) && (this.loy <= paramRegion.loy) && (this.hix >= paramRegion.hix) && (this.hiy >= paramRegion.hiy);
/*      */   }
/*      */ 
/*      */   public boolean encompassesXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1206 */     return encompassesXYXY(paramInt1, paramInt2, dimAdd(paramInt1, paramInt3), dimAdd(paramInt2, paramInt4));
/*      */   }
/*      */ 
/*      */   public boolean encompassesXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1217 */     return (this.bands == null) && (this.lox <= paramInt1) && (this.loy <= paramInt2) && (this.hix >= paramInt3) && (this.hiy >= paramInt4);
/*      */   }
/*      */ 
/*      */   public void getBounds(int[] paramArrayOfInt)
/*      */   {
/* 1226 */     paramArrayOfInt[0] = this.lox;
/* 1227 */     paramArrayOfInt[1] = this.loy;
/* 1228 */     paramArrayOfInt[2] = this.hix;
/* 1229 */     paramArrayOfInt[3] = this.hiy;
/*      */   }
/*      */ 
/*      */   public void clipBoxToBounds(int[] paramArrayOfInt)
/*      */   {
/* 1236 */     if (paramArrayOfInt[0] < this.lox) paramArrayOfInt[0] = this.lox;
/* 1237 */     if (paramArrayOfInt[1] < this.loy) paramArrayOfInt[1] = this.loy;
/* 1238 */     if (paramArrayOfInt[2] > this.hix) paramArrayOfInt[2] = this.hix;
/* 1239 */     if (paramArrayOfInt[3] > this.hiy) paramArrayOfInt[3] = this.hiy;
/*      */   }
/*      */ 
/*      */   public RegionIterator getIterator()
/*      */   {
/* 1246 */     return new RegionIterator(this);
/*      */   }
/*      */ 
/*      */   public SpanIterator getSpanIterator()
/*      */   {
/* 1253 */     return new RegionSpanIterator(this);
/*      */   }
/*      */ 
/*      */   public SpanIterator getSpanIterator(int[] paramArrayOfInt)
/*      */   {
/* 1261 */     SpanIterator localSpanIterator = getSpanIterator();
/* 1262 */     localSpanIterator.intersectClipBox(paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2], paramArrayOfInt[3]);
/* 1263 */     return localSpanIterator;
/*      */   }
/*      */ 
/*      */   public SpanIterator filter(SpanIterator paramSpanIterator)
/*      */   {
/* 1271 */     if (this.bands == null)
/* 1272 */       paramSpanIterator.intersectClipBox(this.lox, this.loy, this.hix, this.hiy);
/*      */     else {
/* 1274 */       paramSpanIterator = new RegionClipSpanIterator(this, paramSpanIterator);
/*      */     }
/* 1276 */     return paramSpanIterator;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1280 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1281 */     localStringBuffer.append("Region[[");
/* 1282 */     localStringBuffer.append(this.lox);
/* 1283 */     localStringBuffer.append(", ");
/* 1284 */     localStringBuffer.append(this.loy);
/* 1285 */     localStringBuffer.append(" => ");
/* 1286 */     localStringBuffer.append(this.hix);
/* 1287 */     localStringBuffer.append(", ");
/* 1288 */     localStringBuffer.append(this.hiy);
/* 1289 */     localStringBuffer.append("]");
/* 1290 */     if (this.bands != null) {
/* 1291 */       int i = 0;
/* 1292 */       while (i < this.endIndex) {
/* 1293 */         localStringBuffer.append("y{");
/* 1294 */         localStringBuffer.append(this.bands[(i++)]);
/* 1295 */         localStringBuffer.append(",");
/* 1296 */         localStringBuffer.append(this.bands[(i++)]);
/* 1297 */         localStringBuffer.append("}[");
/* 1298 */         int j = this.bands[(i++)];
/* 1299 */         j = i + j * 2;
/* 1300 */         while (i < j) {
/* 1301 */           localStringBuffer.append("x(");
/* 1302 */           localStringBuffer.append(this.bands[(i++)]);
/* 1303 */           localStringBuffer.append(", ");
/* 1304 */           localStringBuffer.append(this.bands[(i++)]);
/* 1305 */           localStringBuffer.append(")");
/*      */         }
/* 1307 */         localStringBuffer.append("]");
/*      */       }
/*      */     }
/* 1310 */     localStringBuffer.append("]");
/* 1311 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public int hashCode() {
/* 1315 */     return isEmpty() ? 0 : this.lox * 3 + this.loy * 5 + this.hix * 7 + this.hiy * 9;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/* 1319 */     if (!(paramObject instanceof Region)) {
/* 1320 */       return false;
/*      */     }
/* 1322 */     Region localRegion = (Region)paramObject;
/* 1323 */     if (isEmpty())
/* 1324 */       return localRegion.isEmpty();
/* 1325 */     if (localRegion.isEmpty()) {
/* 1326 */       return false;
/*      */     }
/* 1328 */     if ((localRegion.lox != this.lox) || (localRegion.loy != this.loy) || (localRegion.hix != this.hix) || (localRegion.hiy != this.hiy))
/*      */     {
/* 1331 */       return false;
/*      */     }
/* 1333 */     if (this.bands == null)
/* 1334 */       return localRegion.bands == null;
/* 1335 */     if (localRegion.bands == null) {
/* 1336 */       return false;
/*      */     }
/* 1338 */     if (this.endIndex != localRegion.endIndex) {
/* 1339 */       return false;
/*      */     }
/* 1341 */     int[] arrayOfInt1 = this.bands;
/* 1342 */     int[] arrayOfInt2 = localRegion.bands;
/* 1343 */     for (int i = 0; i < this.endIndex; i++) {
/* 1344 */       if (arrayOfInt1[i] != arrayOfInt2[i]) {
/* 1345 */         return false;
/*      */       }
/*      */     }
/* 1348 */     return true;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  101 */     initIDs();
/*      */   }
/*      */ 
/*      */   private static final class ImmutableRegion extends Region
/*      */   {
/*      */     protected ImmutableRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*   72 */       super(paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void appendSpans(SpanIterator paramSpanIterator)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setOutputArea(Rectangle paramRectangle)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setOutputAreaXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setOutputArea(int[] paramArrayOfInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setOutputAreaXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.Region
 * JD-Core Version:    0.6.2
 */