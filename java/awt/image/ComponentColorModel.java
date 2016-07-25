/*      */ package java.awt.image;
/*      */ 
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class ComponentColorModel extends ColorModel
/*      */ {
/*      */   private boolean signed;
/*      */   private boolean is_sRGB_stdScale;
/*      */   private boolean is_LinearRGB_stdScale;
/*      */   private boolean is_LinearGray_stdScale;
/*      */   private boolean is_ICCGray_stdScale;
/*      */   private byte[] tosRGB8LUT;
/*      */   private byte[] fromsRGB8LUT8;
/*      */   private short[] fromsRGB8LUT16;
/*      */   private byte[] fromLinearGray16ToOtherGray8LUT;
/*      */   private short[] fromLinearGray16ToOtherGray16LUT;
/*      */   private boolean needScaleInit;
/*      */   private boolean noUnnorm;
/*      */   private boolean nonStdScale;
/*      */   private float[] min;
/*      */   private float[] diffMinMax;
/*      */   private float[] compOffset;
/*      */   private float[] compScale;
/*      */ 
/*      */   public ComponentColorModel(ColorSpace paramColorSpace, int[] paramArrayOfInt, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*      */   {
/*  273 */     super(bitsHelper(paramInt2, paramColorSpace, paramBoolean1), bitsArrayHelper(paramArrayOfInt, paramInt2, paramColorSpace, paramBoolean1), paramColorSpace, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*      */ 
/*  277 */     switch (paramInt2) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 3:
/*  281 */       this.signed = false;
/*  282 */       this.needScaleInit = true;
/*  283 */       break;
/*      */     case 2:
/*  285 */       this.signed = true;
/*  286 */       this.needScaleInit = true;
/*  287 */       break;
/*      */     case 4:
/*      */     case 5:
/*  290 */       this.signed = true;
/*  291 */       this.needScaleInit = false;
/*  292 */       this.noUnnorm = true;
/*  293 */       this.nonStdScale = false;
/*  294 */       break;
/*      */     default:
/*  296 */       throw new IllegalArgumentException("This constructor is not compatible with transferType " + paramInt2);
/*      */     }
/*      */ 
/*  299 */     setupLUTs();
/*      */   }
/*      */ 
/*      */   public ComponentColorModel(ColorSpace paramColorSpace, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2)
/*      */   {
/*  351 */     this(paramColorSpace, null, paramBoolean1, paramBoolean2, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private static int bitsHelper(int paramInt, ColorSpace paramColorSpace, boolean paramBoolean)
/*      */   {
/*  358 */     int i = DataBuffer.getDataTypeSize(paramInt);
/*  359 */     int j = paramColorSpace.getNumComponents();
/*  360 */     if (paramBoolean) {
/*  361 */       j++;
/*      */     }
/*  363 */     return i * j;
/*      */   }
/*      */ 
/*      */   private static int[] bitsArrayHelper(int[] paramArrayOfInt, int paramInt, ColorSpace paramColorSpace, boolean paramBoolean)
/*      */   {
/*  370 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 3:
/*  374 */       if (paramArrayOfInt != null) {
/*  375 */         return paramArrayOfInt;
/*      */       }
/*      */       break;
/*      */     case 2:
/*      */     }
/*      */ 
/*  381 */     int i = DataBuffer.getDataTypeSize(paramInt);
/*  382 */     int j = paramColorSpace.getNumComponents();
/*  383 */     if (paramBoolean) {
/*  384 */       j++;
/*      */     }
/*  386 */     int[] arrayOfInt = new int[j];
/*  387 */     for (int k = 0; k < j; k++) {
/*  388 */       arrayOfInt[k] = i;
/*      */     }
/*  390 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private void setupLUTs()
/*      */   {
/*  411 */     if (this.is_sRGB) {
/*  412 */       this.is_sRGB_stdScale = true;
/*  413 */       this.nonStdScale = false;
/*  414 */     } else if (ColorModel.isLinearRGBspace(this.colorSpace))
/*      */     {
/*  418 */       this.is_LinearRGB_stdScale = true;
/*  419 */       this.nonStdScale = false;
/*  420 */       if (this.transferType == 0) {
/*  421 */         this.tosRGB8LUT = ColorModel.getLinearRGB8TosRGB8LUT();
/*  422 */         this.fromsRGB8LUT8 = ColorModel.getsRGB8ToLinearRGB8LUT();
/*      */       } else {
/*  424 */         this.tosRGB8LUT = ColorModel.getLinearRGB16TosRGB8LUT();
/*  425 */         this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
/*      */       }
/*  427 */     } else if ((this.colorSpaceType == 6) && ((this.colorSpace instanceof ICC_ColorSpace)) && (this.colorSpace.getMinValue(0) == 0.0F) && (this.colorSpace.getMaxValue(0) == 1.0F))
/*      */     {
/*  434 */       ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)this.colorSpace;
/*  435 */       this.is_ICCGray_stdScale = true;
/*  436 */       this.nonStdScale = false;
/*  437 */       this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
/*  438 */       if (ColorModel.isLinearGRAYspace(localICC_ColorSpace)) {
/*  439 */         this.is_LinearGray_stdScale = true;
/*  440 */         if (this.transferType == 0)
/*  441 */           this.tosRGB8LUT = ColorModel.getGray8TosRGB8LUT(localICC_ColorSpace);
/*      */         else {
/*  443 */           this.tosRGB8LUT = ColorModel.getGray16TosRGB8LUT(localICC_ColorSpace);
/*      */         }
/*      */       }
/*  446 */       else if (this.transferType == 0) {
/*  447 */         this.tosRGB8LUT = ColorModel.getGray8TosRGB8LUT(localICC_ColorSpace);
/*  448 */         this.fromLinearGray16ToOtherGray8LUT = ColorModel.getLinearGray16ToOtherGray8LUT(localICC_ColorSpace);
/*      */       }
/*      */       else {
/*  451 */         this.tosRGB8LUT = ColorModel.getGray16TosRGB8LUT(localICC_ColorSpace);
/*  452 */         this.fromLinearGray16ToOtherGray16LUT = ColorModel.getLinearGray16ToOtherGray16LUT(localICC_ColorSpace);
/*      */       }
/*      */ 
/*      */     }
/*  456 */     else if (this.needScaleInit)
/*      */     {
/*  461 */       this.nonStdScale = false;
/*  462 */       for (int i = 0; i < this.numColorComponents; i++) {
/*  463 */         if ((this.colorSpace.getMinValue(i) != 0.0F) || (this.colorSpace.getMaxValue(i) != 1.0F))
/*      */         {
/*  465 */           this.nonStdScale = true;
/*  466 */           break;
/*      */         }
/*      */       }
/*  469 */       if (this.nonStdScale) {
/*  470 */         this.min = new float[this.numColorComponents];
/*  471 */         this.diffMinMax = new float[this.numColorComponents];
/*  472 */         for (i = 0; i < this.numColorComponents; i++) {
/*  473 */           this.min[i] = this.colorSpace.getMinValue(i);
/*  474 */           this.diffMinMax[i] = (this.colorSpace.getMaxValue(i) - this.min[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initScale()
/*      */   {
/*  500 */     this.needScaleInit = false;
/*  501 */     if ((this.nonStdScale) || (this.signed))
/*      */     {
/*  510 */       this.noUnnorm = true;
/*      */     }
/*  512 */     else this.noUnnorm = false;
/*      */     Object localObject;
/*      */     int j;
/*      */     float[] arrayOfFloat1;
/*      */     float[] arrayOfFloat2;
/*  515 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/*  518 */       localObject = new byte[this.numComponents];
/*  519 */       for (j = 0; j < this.numColorComponents; j++) {
/*  520 */         localObject[j] = 0;
/*      */       }
/*  522 */       if (this.supportsAlpha) {
/*  523 */         localObject[this.numColorComponents] = ((byte)((1 << this.nBits[this.numColorComponents]) - 1));
/*      */       }
/*      */ 
/*  526 */       arrayOfFloat1 = getNormalizedComponents(localObject, null, 0);
/*  527 */       for (j = 0; j < this.numColorComponents; j++) {
/*  528 */         localObject[j] = ((byte)((1 << this.nBits[j]) - 1));
/*      */       }
/*  530 */       arrayOfFloat2 = getNormalizedComponents(localObject, null, 0);
/*      */ 
/*  532 */       break;
/*      */     case 1:
/*  535 */       localObject = new short[this.numComponents];
/*  536 */       for (j = 0; j < this.numColorComponents; j++) {
/*  537 */         localObject[j] = 0;
/*      */       }
/*  539 */       if (this.supportsAlpha) {
/*  540 */         localObject[this.numColorComponents] = ((short)((1 << this.nBits[this.numColorComponents]) - 1));
/*      */       }
/*      */ 
/*  543 */       arrayOfFloat1 = getNormalizedComponents(localObject, null, 0);
/*  544 */       for (j = 0; j < this.numColorComponents; j++) {
/*  545 */         localObject[j] = ((short)((1 << this.nBits[j]) - 1));
/*      */       }
/*  547 */       arrayOfFloat2 = getNormalizedComponents(localObject, null, 0);
/*      */ 
/*  549 */       break;
/*      */     case 3:
/*  552 */       localObject = new int[this.numComponents];
/*  553 */       for (j = 0; j < this.numColorComponents; j++) {
/*  554 */         localObject[j] = 0;
/*      */       }
/*  556 */       if (this.supportsAlpha) {
/*  557 */         localObject[this.numColorComponents] = ((1 << this.nBits[this.numColorComponents]) - 1);
/*      */       }
/*      */ 
/*  560 */       arrayOfFloat1 = getNormalizedComponents(localObject, null, 0);
/*  561 */       for (j = 0; j < this.numColorComponents; j++) {
/*  562 */         localObject[j] = ((1 << this.nBits[j]) - 1);
/*      */       }
/*  564 */       arrayOfFloat2 = getNormalizedComponents(localObject, null, 0);
/*      */ 
/*  566 */       break;
/*      */     case 2:
/*  569 */       localObject = new short[this.numComponents];
/*  570 */       for (j = 0; j < this.numColorComponents; j++) {
/*  571 */         localObject[j] = 0;
/*      */       }
/*  573 */       if (this.supportsAlpha) {
/*  574 */         localObject[this.numColorComponents] = 32767;
/*      */       }
/*  576 */       arrayOfFloat1 = getNormalizedComponents(localObject, null, 0);
/*  577 */       for (j = 0; j < this.numColorComponents; j++) {
/*  578 */         localObject[j] = 32767;
/*      */       }
/*  580 */       arrayOfFloat2 = getNormalizedComponents(localObject, null, 0);
/*      */ 
/*  582 */       break;
/*      */     default:
/*  584 */       arrayOfFloat1 = arrayOfFloat2 = null;
/*      */     }
/*      */ 
/*  587 */     this.nonStdScale = false;
/*  588 */     for (int i = 0; i < this.numColorComponents; i++) {
/*  589 */       if ((arrayOfFloat1[i] != 0.0F) || (arrayOfFloat2[i] != 1.0F)) {
/*  590 */         this.nonStdScale = true;
/*  591 */         break;
/*      */       }
/*      */     }
/*  594 */     if (this.nonStdScale) {
/*  595 */       this.noUnnorm = true;
/*  596 */       this.is_sRGB_stdScale = false;
/*  597 */       this.is_LinearRGB_stdScale = false;
/*  598 */       this.is_LinearGray_stdScale = false;
/*  599 */       this.is_ICCGray_stdScale = false;
/*  600 */       this.compOffset = new float[this.numColorComponents];
/*  601 */       this.compScale = new float[this.numColorComponents];
/*  602 */       for (i = 0; i < this.numColorComponents; i++) {
/*  603 */         this.compOffset[i] = arrayOfFloat1[i];
/*  604 */         this.compScale[i] = (1.0F / (arrayOfFloat2[i] - arrayOfFloat1[i]));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getRGBComponent(int paramInt1, int paramInt2) {
/*  610 */     if (this.numComponents > 1) {
/*  611 */       throw new IllegalArgumentException("More than one component per pixel");
/*      */     }
/*      */ 
/*  614 */     if (this.signed) {
/*  615 */       throw new IllegalArgumentException("Component value is signed");
/*      */     }
/*      */ 
/*  618 */     if (this.needScaleInit) {
/*  619 */       initScale();
/*      */     }
/*      */ 
/*  624 */     Object localObject1 = null;
/*  625 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/*  628 */       localObject2 = new byte[] { (byte)paramInt1 };
/*  629 */       localObject1 = localObject2;
/*      */ 
/*  631 */       break;
/*      */     case 1:
/*  634 */       localObject2 = new short[] { (short)paramInt1 };
/*  635 */       localObject1 = localObject2;
/*      */ 
/*  637 */       break;
/*      */     case 3:
/*  640 */       localObject2 = new int[] { paramInt1 };
/*  641 */       localObject1 = localObject2;
/*      */     case 2:
/*      */     }
/*      */ 
/*  645 */     Object localObject2 = getNormalizedComponents(localObject1, null, 0);
/*  646 */     float[] arrayOfFloat = this.colorSpace.toRGB((float[])localObject2);
/*      */ 
/*  648 */     return (int)(arrayOfFloat[paramInt2] * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public int getRed(int paramInt)
/*      */   {
/*  670 */     return getRGBComponent(paramInt, 0);
/*      */   }
/*      */ 
/*      */   public int getGreen(int paramInt)
/*      */   {
/*  692 */     return getRGBComponent(paramInt, 1);
/*      */   }
/*      */ 
/*      */   public int getBlue(int paramInt)
/*      */   {
/*  714 */     return getRGBComponent(paramInt, 2);
/*      */   }
/*      */ 
/*      */   public int getAlpha(int paramInt)
/*      */   {
/*  731 */     if (!this.supportsAlpha) {
/*  732 */       return 255;
/*      */     }
/*  734 */     if (this.numComponents > 1) {
/*  735 */       throw new IllegalArgumentException("More than one component per pixel");
/*      */     }
/*      */ 
/*  738 */     if (this.signed) {
/*  739 */       throw new IllegalArgumentException("Component value is signed");
/*      */     }
/*      */ 
/*  743 */     return (int)(paramInt / ((1 << this.nBits[0]) - 1) * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public int getRGB(int paramInt)
/*      */   {
/*  763 */     if (this.numComponents > 1) {
/*  764 */       throw new IllegalArgumentException("More than one component per pixel");
/*      */     }
/*      */ 
/*  767 */     if (this.signed) {
/*  768 */       throw new IllegalArgumentException("Component value is signed");
/*      */     }
/*      */ 
/*  772 */     return getAlpha(paramInt) << 24 | getRed(paramInt) << 16 | getGreen(paramInt) << 8 | getBlue(paramInt) << 0;
/*      */   }
/*      */ 
/*      */   private int extractComponent(Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/*  794 */     int i = (this.supportsAlpha) && (this.isAlphaPremultiplied) ? 1 : 0;
/*  795 */     int j = 0;
/*      */ 
/*  797 */     int m = (1 << this.nBits[paramInt1]) - 1;
/*      */     Object localObject;
/*      */     float f2;
/*      */     int k;
/*  799 */     switch (this.transferType)
/*      */     {
/*      */     case 2:
/*  803 */       localObject = (short[])paramObject;
/*  804 */       f2 = (1 << paramInt2) - 1;
/*  805 */       if (i != 0) {
/*  806 */         int n = localObject[this.numColorComponents];
/*  807 */         if (n != 0) {
/*  808 */           return (int)(localObject[paramInt1] / n * f2 + 0.5F);
/*      */         }
/*      */ 
/*  811 */         return 0;
/*      */       }
/*      */ 
/*  814 */       return (int)(localObject[paramInt1] / 32767.0F * f2 + 0.5F);
/*      */     case 4:
/*  818 */       localObject = (float[])paramObject;
/*  819 */       f2 = (1 << paramInt2) - 1;
/*  820 */       if (i != 0) {
/*  821 */         float f4 = localObject[this.numColorComponents];
/*  822 */         if (f4 != 0.0F) {
/*  823 */           return (int)(localObject[paramInt1] / f4 * f2 + 0.5F);
/*      */         }
/*  825 */         return 0;
/*      */       }
/*      */ 
/*  828 */       return (int)(localObject[paramInt1] * f2 + 0.5F);
/*      */     case 5:
/*  832 */       localObject = (double[])paramObject;
/*  833 */       double d1 = (1 << paramInt2) - 1;
/*  834 */       if (i != 0) {
/*  835 */         double d2 = localObject[this.numColorComponents];
/*  836 */         if (d2 != 0.0D) {
/*  837 */           return (int)(localObject[paramInt1] / d2 * d1 + 0.5D);
/*      */         }
/*  839 */         return 0;
/*      */       }
/*      */ 
/*  842 */       return (int)(localObject[paramInt1] * d1 + 0.5D);
/*      */     case 0:
/*  846 */       localObject = (byte[])paramObject;
/*  847 */       k = localObject[paramInt1] & m;
/*  848 */       paramInt2 = 8;
/*  849 */       if (i != 0)
/*  850 */         j = localObject[this.numColorComponents] & m; break;
/*      */     case 1:
/*  854 */       short[] arrayOfShort = (short[])paramObject;
/*  855 */       k = arrayOfShort[paramInt1] & m;
/*  856 */       if (i != 0)
/*  857 */         j = arrayOfShort[this.numColorComponents] & m; break;
/*      */     case 3:
/*  861 */       int[] arrayOfInt = (int[])paramObject;
/*  862 */       k = arrayOfInt[paramInt1];
/*  863 */       if (i != 0)
/*  864 */         j = arrayOfInt[this.numColorComponents]; break;
/*      */     default:
/*  868 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */     float f1;
/*      */     float f3;
/*  872 */     if (i != 0) {
/*  873 */       if (j != 0) {
/*  874 */         f1 = (1 << paramInt2) - 1;
/*  875 */         f3 = k / m;
/*  876 */         float f5 = ((1 << this.nBits[this.numColorComponents]) - 1) / j;
/*      */ 
/*  878 */         return (int)(f3 * f5 * f1 + 0.5F);
/*      */       }
/*  880 */       return 0;
/*      */     }
/*      */ 
/*  883 */     if (this.nBits[paramInt1] != paramInt2) {
/*  884 */       f1 = (1 << paramInt2) - 1;
/*  885 */       f3 = k / m;
/*  886 */       return (int)(f3 * f1 + 0.5F);
/*      */     }
/*  888 */     return k;
/*      */   }
/*      */ 
/*      */   private int getRGBComponent(Object paramObject, int paramInt)
/*      */   {
/*  893 */     if (this.needScaleInit) {
/*  894 */       initScale();
/*      */     }
/*  896 */     if (this.is_sRGB_stdScale)
/*  897 */       return extractComponent(paramObject, paramInt, 8);
/*      */     int i;
/*  898 */     if (this.is_LinearRGB_stdScale) {
/*  899 */       i = extractComponent(paramObject, paramInt, 16);
/*  900 */       return this.tosRGB8LUT[i] & 0xFF;
/*  901 */     }if (this.is_ICCGray_stdScale) {
/*  902 */       i = extractComponent(paramObject, 0, 16);
/*  903 */       return this.tosRGB8LUT[i] & 0xFF;
/*      */     }
/*      */ 
/*  907 */     float[] arrayOfFloat1 = getNormalizedComponents(paramObject, null, 0);
/*      */ 
/*  909 */     float[] arrayOfFloat2 = this.colorSpace.toRGB(arrayOfFloat1);
/*  910 */     return (int)(arrayOfFloat2[paramInt] * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public int getRed(Object paramObject)
/*      */   {
/*  944 */     return getRGBComponent(paramObject, 0);
/*      */   }
/*      */ 
/*      */   public int getGreen(Object paramObject)
/*      */   {
/*  979 */     return getRGBComponent(paramObject, 1);
/*      */   }
/*      */ 
/*      */   public int getBlue(Object paramObject)
/*      */   {
/* 1014 */     return getRGBComponent(paramObject, 2);
/*      */   }
/*      */ 
/*      */   public int getAlpha(Object paramObject)
/*      */   {
/* 1045 */     if (!this.supportsAlpha) {
/* 1046 */       return 255;
/*      */     }
/*      */ 
/* 1049 */     int i = 0;
/* 1050 */     int j = this.numColorComponents;
/* 1051 */     int k = (1 << this.nBits[j]) - 1;
/*      */ 
/* 1053 */     switch (this.transferType) {
/*      */     case 2:
/* 1055 */       short[] arrayOfShort1 = (short[])paramObject;
/* 1056 */       i = (int)(arrayOfShort1[j] / 32767.0F * 255.0F + 0.5F);
/* 1057 */       return i;
/*      */     case 4:
/* 1059 */       float[] arrayOfFloat = (float[])paramObject;
/* 1060 */       i = (int)(arrayOfFloat[j] * 255.0F + 0.5F);
/* 1061 */       return i;
/*      */     case 5:
/* 1063 */       double[] arrayOfDouble = (double[])paramObject;
/* 1064 */       i = (int)(arrayOfDouble[j] * 255.0D + 0.5D);
/* 1065 */       return i;
/*      */     case 0:
/* 1067 */       byte[] arrayOfByte = (byte[])paramObject;
/* 1068 */       i = arrayOfByte[j] & k;
/* 1069 */       break;
/*      */     case 1:
/* 1071 */       short[] arrayOfShort2 = (short[])paramObject;
/* 1072 */       i = arrayOfShort2[j] & k;
/* 1073 */       break;
/*      */     case 3:
/* 1075 */       int[] arrayOfInt = (int[])paramObject;
/* 1076 */       i = arrayOfInt[j];
/* 1077 */       break;
/*      */     default:
/* 1079 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/* 1084 */     if (this.nBits[j] == 8) {
/* 1085 */       return i;
/*      */     }
/* 1087 */     return (int)(i / ((1 << this.nBits[j]) - 1) * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public int getRGB(Object paramObject)
/*      */   {
/* 1126 */     if (this.needScaleInit) {
/* 1127 */       initScale();
/*      */     }
/* 1129 */     if ((this.is_sRGB_stdScale) || (this.is_LinearRGB_stdScale)) {
/* 1130 */       return getAlpha(paramObject) << 24 | getRed(paramObject) << 16 | getGreen(paramObject) << 8 | getBlue(paramObject);
/*      */     }
/*      */ 
/* 1134 */     if (this.colorSpaceType == 6) {
/* 1135 */       int i = getRed(paramObject);
/*      */ 
/* 1137 */       return getAlpha(paramObject) << 24 | i << 16 | i << 8 | i;
/*      */     }
/*      */ 
/* 1142 */     float[] arrayOfFloat1 = getNormalizedComponents(paramObject, null, 0);
/*      */ 
/* 1144 */     float[] arrayOfFloat2 = this.colorSpace.toRGB(arrayOfFloat1);
/* 1145 */     return getAlpha(paramObject) << 24 | (int)(arrayOfFloat2[0] * 255.0F + 0.5F) << 16 | (int)(arrayOfFloat2[1] * 255.0F + 0.5F) << 8 | (int)(arrayOfFloat2[2] * 255.0F + 0.5F) << 0;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt, Object paramObject)
/*      */   {
/* 1189 */     int i = paramInt >> 16 & 0xFF;
/* 1190 */     int j = paramInt >> 8 & 0xFF;
/* 1191 */     int k = paramInt & 0xFF;
/*      */ 
/* 1193 */     if (this.needScaleInit)
/* 1194 */       initScale();
/*      */     Object localObject1;
/*      */     int m;
/*      */     int i7;
/* 1196 */     if (this.signed)
/*      */     {
/*      */       float f1;
/*      */       float[] arrayOfFloat2;
/* 1199 */       switch (this.transferType)
/*      */       {
/*      */       case 2:
/* 1203 */         if (paramObject == null)
/* 1204 */           localObject1 = new short[this.numComponents];
/*      */         else {
/* 1206 */           localObject1 = (short[])paramObject;
/*      */         }
/*      */ 
/* 1209 */         if ((this.is_sRGB_stdScale) || (this.is_LinearRGB_stdScale)) {
/* 1210 */           f1 = 128.49803F;
/* 1211 */           if (this.is_LinearRGB_stdScale) {
/* 1212 */             i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1213 */             j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1214 */             k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1215 */             f1 = 0.4999924F;
/*      */           }
/* 1217 */           if (this.supportsAlpha) {
/* 1218 */             m = paramInt >> 24 & 0xFF;
/* 1219 */             localObject1[3] = ((short)(int)(m * 128.49803F + 0.5F));
/*      */ 
/* 1221 */             if (this.isAlphaPremultiplied) {
/* 1222 */               f1 = m * f1 * 0.003921569F;
/*      */             }
/*      */           }
/* 1225 */           localObject1[0] = ((short)(int)(i * f1 + 0.5F));
/* 1226 */           localObject1[1] = ((short)(int)(j * f1 + 0.5F));
/* 1227 */           localObject1[2] = ((short)(int)(k * f1 + 0.5F));
/* 1228 */         } else if (this.is_LinearGray_stdScale) {
/* 1229 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1230 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1231 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1232 */           float f4 = (0.2125F * i + 0.7154F * j + 0.0721F * k) / 65535.0F;
/*      */ 
/* 1235 */           f1 = 32767.0F;
/* 1236 */           if (this.supportsAlpha) {
/* 1237 */             m = paramInt >> 24 & 0xFF;
/* 1238 */             localObject1[1] = ((short)(int)(m * 128.49803F + 0.5F));
/*      */ 
/* 1240 */             if (this.isAlphaPremultiplied) {
/* 1241 */               f1 = m * f1 * 0.003921569F;
/*      */             }
/*      */           }
/* 1244 */           localObject1[0] = ((short)(int)(f4 * f1 + 0.5F));
/* 1245 */         } else if (this.is_ICCGray_stdScale) {
/* 1246 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1247 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1248 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1249 */           int i4 = (int)(0.2125F * i + 0.7154F * j + 0.0721F * k + 0.5F);
/*      */ 
/* 1252 */           i4 = this.fromLinearGray16ToOtherGray16LUT[i4] & 0xFFFF;
/* 1253 */           f1 = 0.4999924F;
/* 1254 */           if (this.supportsAlpha) {
/* 1255 */             m = paramInt >> 24 & 0xFF;
/* 1256 */             localObject1[1] = ((short)(int)(m * 128.49803F + 0.5F));
/*      */ 
/* 1258 */             if (this.isAlphaPremultiplied) {
/* 1259 */               f1 = m * f1 * 0.003921569F;
/*      */             }
/*      */           }
/* 1262 */           localObject1[0] = ((short)(int)(i4 * f1 + 0.5F));
/*      */         } else {
/* 1264 */           f1 = 0.003921569F;
/* 1265 */           float[] arrayOfFloat1 = new float[3];
/* 1266 */           arrayOfFloat1[0] = (i * f1);
/* 1267 */           arrayOfFloat1[1] = (j * f1);
/* 1268 */           arrayOfFloat1[2] = (k * f1);
/* 1269 */           arrayOfFloat1 = this.colorSpace.fromRGB(arrayOfFloat1);
/* 1270 */           if (this.nonStdScale) {
/* 1271 */             for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1272 */               arrayOfFloat1[i7] = ((arrayOfFloat1[i7] - this.compOffset[i7]) * this.compScale[i7]);
/*      */ 
/* 1276 */               if (arrayOfFloat1[i7] < 0.0F) {
/* 1277 */                 arrayOfFloat1[i7] = 0.0F;
/*      */               }
/* 1279 */               if (arrayOfFloat1[i7] > 1.0F) {
/* 1280 */                 arrayOfFloat1[i7] = 1.0F;
/*      */               }
/*      */             }
/*      */           }
/* 1284 */           f1 = 32767.0F;
/* 1285 */           if (this.supportsAlpha) {
/* 1286 */             m = paramInt >> 24 & 0xFF;
/* 1287 */             localObject1[this.numColorComponents] = ((short)(int)(m * 128.49803F + 0.5F));
/*      */ 
/* 1289 */             if (this.isAlphaPremultiplied) {
/* 1290 */               f1 *= m * 0.003921569F;
/*      */             }
/*      */           }
/* 1293 */           for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1294 */             localObject1[i7] = ((short)(int)(arrayOfFloat1[i7] * f1 + 0.5F));
/*      */           }
/*      */         }
/* 1297 */         return localObject1;
/*      */       case 4:
/* 1302 */         if (paramObject == null)
/* 1303 */           localObject1 = new float[this.numComponents];
/*      */         else {
/* 1305 */           localObject1 = (float[])paramObject;
/*      */         }
/*      */ 
/* 1308 */         if ((this.is_sRGB_stdScale) || (this.is_LinearRGB_stdScale)) {
/* 1309 */           if (this.is_LinearRGB_stdScale) {
/* 1310 */             i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1311 */             j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1312 */             k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1313 */             f1 = 1.525902E-005F;
/*      */           } else {
/* 1315 */             f1 = 0.003921569F;
/*      */           }
/* 1317 */           if (this.supportsAlpha) {
/* 1318 */             m = paramInt >> 24 & 0xFF;
/* 1319 */             localObject1[3] = (m * 0.003921569F);
/* 1320 */             if (this.isAlphaPremultiplied) {
/* 1321 */               f1 *= localObject1[3];
/*      */             }
/*      */           }
/* 1324 */           localObject1[0] = (i * f1);
/* 1325 */           localObject1[1] = (j * f1);
/* 1326 */           localObject1[2] = (k * f1);
/* 1327 */         } else if (this.is_LinearGray_stdScale) {
/* 1328 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1329 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1330 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1331 */           localObject1[0] = ((0.2125F * i + 0.7154F * j + 0.0721F * k) / 65535.0F);
/*      */ 
/* 1334 */           if (this.supportsAlpha) {
/* 1335 */             m = paramInt >> 24 & 0xFF;
/* 1336 */             localObject1[1] = (m * 0.003921569F);
/* 1337 */             if (this.isAlphaPremultiplied)
/* 1338 */               localObject1[0] *= localObject1[1];
/*      */           }
/*      */         }
/* 1341 */         else if (this.is_ICCGray_stdScale) {
/* 1342 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1343 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1344 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1345 */           int i5 = (int)(0.2125F * i + 0.7154F * j + 0.0721F * k + 0.5F);
/*      */ 
/* 1348 */           localObject1[0] = ((this.fromLinearGray16ToOtherGray16LUT[i5] & 0xFFFF) / 65535.0F);
/*      */ 
/* 1350 */           if (this.supportsAlpha) {
/* 1351 */             m = paramInt >> 24 & 0xFF;
/* 1352 */             localObject1[1] = (m * 0.003921569F);
/* 1353 */             if (this.isAlphaPremultiplied)
/* 1354 */               localObject1[0] *= localObject1[1];
/*      */           }
/*      */         }
/*      */         else {
/* 1358 */           arrayOfFloat2 = new float[3];
/* 1359 */           f1 = 0.003921569F;
/* 1360 */           arrayOfFloat2[0] = (i * f1);
/* 1361 */           arrayOfFloat2[1] = (j * f1);
/* 1362 */           arrayOfFloat2[2] = (k * f1);
/* 1363 */           arrayOfFloat2 = this.colorSpace.fromRGB(arrayOfFloat2);
/* 1364 */           if (this.supportsAlpha) {
/* 1365 */             m = paramInt >> 24 & 0xFF;
/* 1366 */             localObject1[this.numColorComponents] = (m * f1);
/* 1367 */             if (this.isAlphaPremultiplied) {
/* 1368 */               f1 *= m;
/* 1369 */               for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1370 */                 arrayOfFloat2[i7] *= f1;
/*      */               }
/*      */             }
/*      */           }
/* 1374 */           for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1375 */             localObject1[i7] = arrayOfFloat2[i7];
/*      */           }
/*      */         }
/* 1378 */         return localObject1;
/*      */       case 5:
/* 1383 */         if (paramObject == null)
/* 1384 */           localObject1 = new double[this.numComponents];
/*      */         else {
/* 1386 */           localObject1 = (double[])paramObject;
/*      */         }
/* 1388 */         if ((this.is_sRGB_stdScale) || (this.is_LinearRGB_stdScale))
/*      */         {
/*      */           double d;
/* 1390 */           if (this.is_LinearRGB_stdScale) {
/* 1391 */             i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1392 */             j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1393 */             k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1394 */             d = 1.525902189669642E-005D;
/*      */           } else {
/* 1396 */             d = 0.00392156862745098D;
/*      */           }
/* 1398 */           if (this.supportsAlpha) {
/* 1399 */             m = paramInt >> 24 & 0xFF;
/* 1400 */             localObject1[3] = (m * 0.00392156862745098D);
/* 1401 */             if (this.isAlphaPremultiplied) {
/* 1402 */               d *= localObject1[3];
/*      */             }
/*      */           }
/* 1405 */           localObject1[0] = (i * d);
/* 1406 */           localObject1[1] = (j * d);
/* 1407 */           localObject1[2] = (k * d);
/* 1408 */         } else if (this.is_LinearGray_stdScale) {
/* 1409 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1410 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1411 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1412 */           localObject1[0] = ((0.2125D * i + 0.7154D * j + 0.0721D * k) / 65535.0D);
/*      */ 
/* 1415 */           if (this.supportsAlpha) {
/* 1416 */             m = paramInt >> 24 & 0xFF;
/* 1417 */             localObject1[1] = (m * 0.00392156862745098D);
/* 1418 */             if (this.isAlphaPremultiplied)
/* 1419 */               localObject1[0] *= localObject1[1];
/*      */           }
/*      */         }
/* 1422 */         else if (this.is_ICCGray_stdScale) {
/* 1423 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1424 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1425 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1426 */           int n = (int)(0.2125F * i + 0.7154F * j + 0.0721F * k + 0.5F);
/*      */ 
/* 1429 */           localObject1[0] = ((this.fromLinearGray16ToOtherGray16LUT[n] & 0xFFFF) / 65535.0D);
/*      */ 
/* 1431 */           if (this.supportsAlpha) {
/* 1432 */             m = paramInt >> 24 & 0xFF;
/* 1433 */             localObject1[1] = (m * 0.00392156862745098D);
/* 1434 */             if (this.isAlphaPremultiplied)
/* 1435 */               localObject1[0] *= localObject1[1];
/*      */           }
/*      */         }
/*      */         else {
/* 1439 */           float f2 = 0.003921569F;
/* 1440 */           arrayOfFloat2 = new float[3];
/* 1441 */           arrayOfFloat2[0] = (i * f2);
/* 1442 */           arrayOfFloat2[1] = (j * f2);
/* 1443 */           arrayOfFloat2[2] = (k * f2);
/* 1444 */           arrayOfFloat2 = this.colorSpace.fromRGB(arrayOfFloat2);
/* 1445 */           if (this.supportsAlpha) {
/* 1446 */             m = paramInt >> 24 & 0xFF;
/* 1447 */             localObject1[this.numColorComponents] = (m * 0.00392156862745098D);
/* 1448 */             if (this.isAlphaPremultiplied) {
/* 1449 */               f2 *= m;
/* 1450 */               for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1451 */                 arrayOfFloat2[i7] *= f2;
/*      */               }
/*      */             }
/*      */           }
/* 1455 */           for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1456 */             localObject1[i7] = arrayOfFloat2[i7];
/*      */           }
/*      */         }
/* 1459 */         return localObject1;
/*      */       case 3:
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1468 */     if ((this.transferType == 3) && (paramObject != null))
/*      */     {
/* 1470 */       localObject1 = (int[])paramObject;
/*      */     }
/* 1472 */     else localObject1 = new int[this.numComponents];
/*      */     float f5;
/*      */     Object localObject2;
/* 1475 */     if ((this.is_sRGB_stdScale) || (this.is_LinearRGB_stdScale))
/*      */     {
/*      */       int i1;
/* 1478 */       if (this.is_LinearRGB_stdScale) {
/* 1479 */         if (this.transferType == 0) {
/* 1480 */           i = this.fromsRGB8LUT8[i] & 0xFF;
/* 1481 */           j = this.fromsRGB8LUT8[j] & 0xFF;
/* 1482 */           k = this.fromsRGB8LUT8[k] & 0xFF;
/* 1483 */           i1 = 8;
/* 1484 */           f5 = 0.003921569F;
/*      */         } else {
/* 1486 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1487 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1488 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1489 */           i1 = 16;
/* 1490 */           f5 = 1.525902E-005F;
/*      */         }
/*      */       } else {
/* 1493 */         i1 = 8;
/* 1494 */         f5 = 0.003921569F;
/*      */       }
/* 1496 */       if (this.supportsAlpha) {
/* 1497 */         m = paramInt >> 24 & 0xFF;
/* 1498 */         if (this.nBits[3] == 8) {
/* 1499 */           localObject1[3] = m;
/*      */         }
/*      */         else {
/* 1502 */           localObject1[3] = ((int)(m * 0.003921569F * ((1 << this.nBits[3]) - 1) + 0.5F));
/*      */         }
/*      */ 
/* 1505 */         if (this.isAlphaPremultiplied) {
/* 1506 */           f5 *= m * 0.003921569F;
/* 1507 */           i1 = -1;
/*      */         }
/*      */       }
/* 1510 */       if (this.nBits[0] == i1) {
/* 1511 */         localObject1[0] = i;
/*      */       }
/*      */       else {
/* 1514 */         localObject1[0] = ((int)(i * f5 * ((1 << this.nBits[0]) - 1) + 0.5F));
/*      */       }
/* 1516 */       if (this.nBits[1] == i1) {
/* 1517 */         localObject1[1] = j;
/*      */       }
/*      */       else {
/* 1520 */         localObject1[1] = ((int)(j * f5 * ((1 << this.nBits[1]) - 1) + 0.5F));
/*      */       }
/* 1522 */       if (this.nBits[2] == i1) {
/* 1523 */         localObject1[2] = k;
/*      */       }
/*      */       else
/* 1526 */         localObject1[2] = ((int)(k * f5 * ((1 << this.nBits[2]) - 1) + 0.5F));
/*      */     }
/* 1528 */     else if (this.is_LinearGray_stdScale) {
/* 1529 */       i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1530 */       j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1531 */       k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1532 */       float f3 = (0.2125F * i + 0.7154F * j + 0.0721F * k) / 65535.0F;
/*      */ 
/* 1535 */       if (this.supportsAlpha) {
/* 1536 */         m = paramInt >> 24 & 0xFF;
/* 1537 */         if (this.nBits[1] == 8)
/* 1538 */           localObject1[1] = m;
/*      */         else {
/* 1540 */           localObject1[1] = ((int)(m * 0.003921569F * ((1 << this.nBits[1]) - 1) + 0.5F));
/*      */         }
/*      */ 
/* 1543 */         if (this.isAlphaPremultiplied) {
/* 1544 */           f3 *= m * 0.003921569F;
/*      */         }
/*      */       }
/* 1547 */       localObject1[0] = ((int)(f3 * ((1 << this.nBits[0]) - 1) + 0.5F));
/* 1548 */     } else if (this.is_ICCGray_stdScale) {
/* 1549 */       i = this.fromsRGB8LUT16[i] & 0xFFFF;
/* 1550 */       j = this.fromsRGB8LUT16[j] & 0xFFFF;
/* 1551 */       k = this.fromsRGB8LUT16[k] & 0xFFFF;
/* 1552 */       int i2 = (int)(0.2125F * i + 0.7154F * j + 0.0721F * k + 0.5F);
/*      */ 
/* 1555 */       f5 = (this.fromLinearGray16ToOtherGray16LUT[i2] & 0xFFFF) / 65535.0F;
/*      */ 
/* 1557 */       if (this.supportsAlpha) {
/* 1558 */         m = paramInt >> 24 & 0xFF;
/* 1559 */         if (this.nBits[1] == 8)
/* 1560 */           localObject1[1] = m;
/*      */         else {
/* 1562 */           localObject1[1] = ((int)(m * 0.003921569F * ((1 << this.nBits[1]) - 1) + 0.5F));
/*      */         }
/*      */ 
/* 1565 */         if (this.isAlphaPremultiplied) {
/* 1566 */           f5 *= m * 0.003921569F;
/*      */         }
/*      */       }
/* 1569 */       localObject1[0] = ((int)(f5 * ((1 << this.nBits[0]) - 1) + 0.5F));
/*      */     }
/*      */     else {
/* 1572 */       localObject2 = new float[3];
/* 1573 */       f5 = 0.003921569F;
/* 1574 */       localObject2[0] = (i * f5);
/* 1575 */       localObject2[1] = (j * f5);
/* 1576 */       localObject2[2] = (k * f5);
/* 1577 */       localObject2 = this.colorSpace.fromRGB((float[])localObject2);
/* 1578 */       if (this.nonStdScale) {
/* 1579 */         for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1580 */           localObject2[i7] = ((localObject2[i7] - this.compOffset[i7]) * this.compScale[i7]);
/*      */ 
/* 1584 */           if (localObject2[i7] < 0.0F) {
/* 1585 */             localObject2[i7] = 0.0F;
/*      */           }
/* 1587 */           if (localObject2[i7] > 1.0F) {
/* 1588 */             localObject2[i7] = 1.0F;
/*      */           }
/*      */         }
/*      */       }
/* 1592 */       if (this.supportsAlpha) {
/* 1593 */         m = paramInt >> 24 & 0xFF;
/* 1594 */         if (this.nBits[this.numColorComponents] == 8) {
/* 1595 */           localObject1[this.numColorComponents] = m;
/*      */         }
/*      */         else {
/* 1598 */           localObject1[this.numColorComponents] = ((int)(m * f5 * ((1 << this.nBits[this.numColorComponents]) - 1) + 0.5F));
/*      */         }
/*      */ 
/* 1602 */         if (this.isAlphaPremultiplied) {
/* 1603 */           f5 *= m;
/* 1604 */           for (i7 = 0; i7 < this.numColorComponents; i7++) {
/* 1605 */             localObject2[i7] *= f5;
/*      */           }
/*      */         }
/*      */       }
/* 1609 */       for (i7 = 0; i7 < this.numColorComponents; i7++)
/* 1610 */         localObject1[i7] = ((int)(localObject2[i7] * ((1 << this.nBits[i7]) - 1) + 0.5F));
/*      */     }
/*      */     int i6;
/* 1614 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/* 1617 */       if (paramObject == null)
/* 1618 */         localObject2 = new byte[this.numComponents];
/*      */       else {
/* 1620 */         localObject2 = (byte[])paramObject;
/*      */       }
/* 1622 */       for (i6 = 0; i6 < this.numComponents; i6++) {
/* 1623 */         localObject2[i6] = ((byte)(0xFF & localObject1[i6]));
/*      */       }
/* 1625 */       return localObject2;
/*      */     case 1:
/* 1629 */       if (paramObject == null)
/* 1630 */         localObject2 = new short[this.numComponents];
/*      */       else {
/* 1632 */         localObject2 = (short[])paramObject;
/*      */       }
/* 1634 */       for (i6 = 0; i6 < this.numComponents; i6++) {
/* 1635 */         localObject2[i6] = ((short)(localObject1[i6] & 0xFFFF));
/*      */       }
/* 1637 */       return localObject2;
/*      */     case 3:
/* 1640 */       if (this.maxBits > 23)
/*      */       {
/* 1645 */         for (int i3 = 0; i3 < this.numComponents; i3++) {
/* 1646 */           if (localObject1[i3] > (1 << this.nBits[i3]) - 1) {
/* 1647 */             localObject1[i3] = ((1 << this.nBits[i3]) - 1);
/*      */           }
/*      */         }
/*      */       }
/* 1651 */       return localObject1;
/*      */     case 2:
/* 1653 */     }throw new IllegalArgumentException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public int[] getComponents(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/* 1682 */     if (this.numComponents > 1) {
/* 1683 */       throw new IllegalArgumentException("More than one component per pixel");
/*      */     }
/*      */ 
/* 1686 */     if (this.needScaleInit) {
/* 1687 */       initScale();
/*      */     }
/* 1689 */     if (this.noUnnorm) {
/* 1690 */       throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */     }
/*      */ 
/* 1694 */     if (paramArrayOfInt == null) {
/* 1695 */       paramArrayOfInt = new int[paramInt2 + 1];
/*      */     }
/*      */ 
/* 1698 */     paramArrayOfInt[(paramInt2 + 0)] = (paramInt1 & (1 << this.nBits[0]) - 1);
/* 1699 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public int[] getComponents(Object paramObject, int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1743 */     if (this.needScaleInit) {
/* 1744 */       initScale();
/*      */     }
/* 1746 */     if (this.noUnnorm)
/* 1747 */       throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */     int[] arrayOfInt;
/* 1751 */     if ((paramObject instanceof int[])) {
/* 1752 */       arrayOfInt = (int[])paramObject;
/*      */     } else {
/* 1754 */       arrayOfInt = DataBuffer.toIntArray(paramObject);
/* 1755 */       if (arrayOfInt == null) {
/* 1756 */         throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */       }
/*      */     }
/*      */ 
/* 1760 */     if (arrayOfInt.length < this.numComponents) {
/* 1761 */       throw new IllegalArgumentException("Length of pixel array < number of components in model");
/*      */     }
/*      */ 
/* 1764 */     if (paramArrayOfInt == null) {
/* 1765 */       paramArrayOfInt = new int[paramInt + this.numComponents];
/*      */     }
/* 1767 */     else if (paramArrayOfInt.length - paramInt < this.numComponents) {
/* 1768 */       throw new IllegalArgumentException("Length of components array < number of components in model");
/*      */     }
/*      */ 
/* 1771 */     System.arraycopy(arrayOfInt, 0, paramArrayOfInt, paramInt, this.numComponents);
/*      */ 
/* 1773 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public int[] getUnnormalizedComponents(float[] paramArrayOfFloat, int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/* 1819 */     if (this.needScaleInit) {
/* 1820 */       initScale();
/*      */     }
/* 1822 */     if (this.noUnnorm) {
/* 1823 */       throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */     }
/*      */ 
/* 1827 */     return super.getUnnormalizedComponents(paramArrayOfFloat, paramInt1, paramArrayOfInt, paramInt2);
/*      */   }
/*      */ 
/*      */   public float[] getNormalizedComponents(int[] paramArrayOfInt, int paramInt1, float[] paramArrayOfFloat, int paramInt2)
/*      */   {
/* 1868 */     if (this.needScaleInit) {
/* 1869 */       initScale();
/*      */     }
/* 1871 */     if (this.noUnnorm) {
/* 1872 */       throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */     }
/*      */ 
/* 1876 */     return super.getNormalizedComponents(paramArrayOfInt, paramInt1, paramArrayOfFloat, paramInt2);
/*      */   }
/*      */ 
/*      */   public int getDataElement(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1895 */     if (this.needScaleInit) {
/* 1896 */       initScale();
/*      */     }
/* 1898 */     if (this.numComponents == 1) {
/* 1899 */       if (this.noUnnorm) {
/* 1900 */         throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */       }
/*      */ 
/* 1904 */       return paramArrayOfInt[(paramInt + 0)];
/*      */     }
/* 1906 */     throw new IllegalArgumentException("This model returns " + this.numComponents + " elements in the pixel array.");
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int[] paramArrayOfInt, int paramInt, Object paramObject)
/*      */   {
/* 1953 */     if (this.needScaleInit) {
/* 1954 */       initScale();
/*      */     }
/* 1956 */     if (this.noUnnorm) {
/* 1957 */       throw new IllegalArgumentException("This ColorModel does not support the unnormalized form");
/*      */     }
/*      */ 
/* 1961 */     if (paramArrayOfInt.length - paramInt < this.numComponents)
/* 1962 */       throw new IllegalArgumentException("Component array too small (should be " + this.numComponents);
/*      */     Object localObject;
/*      */     int i;
/* 1965 */     switch (this.transferType)
/*      */     {
/*      */     case 3:
/* 1969 */       if (paramObject == null) {
/* 1970 */         localObject = new int[this.numComponents];
/*      */       }
/*      */       else {
/* 1973 */         localObject = (int[])paramObject;
/*      */       }
/* 1975 */       System.arraycopy(paramArrayOfInt, paramInt, localObject, 0, this.numComponents);
/*      */ 
/* 1977 */       return localObject;
/*      */     case 0:
/* 1983 */       if (paramObject == null) {
/* 1984 */         localObject = new byte[this.numComponents];
/*      */       }
/*      */       else {
/* 1987 */         localObject = (byte[])paramObject;
/*      */       }
/* 1989 */       for (i = 0; i < this.numComponents; i++) {
/* 1990 */         localObject[i] = ((byte)(paramArrayOfInt[(paramInt + i)] & 0xFF));
/*      */       }
/* 1992 */       return localObject;
/*      */     case 1:
/* 1998 */       if (paramObject == null) {
/* 1999 */         localObject = new short[this.numComponents];
/*      */       }
/*      */       else {
/* 2002 */         localObject = (short[])paramObject;
/*      */       }
/* 2004 */       for (i = 0; i < this.numComponents; i++) {
/* 2005 */         localObject[i] = ((short)(paramArrayOfInt[(paramInt + i)] & 0xFFFF));
/*      */       }
/* 2007 */       return localObject;
/*      */     case 2:
/*      */     }
/*      */ 
/* 2011 */     throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public int getDataElement(float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 2043 */     if (this.numComponents > 1) {
/* 2044 */       throw new IllegalArgumentException("More than one component per pixel");
/*      */     }
/*      */ 
/* 2047 */     if (this.signed) {
/* 2048 */       throw new IllegalArgumentException("Component value is signed");
/*      */     }
/*      */ 
/* 2051 */     if (this.needScaleInit) {
/* 2052 */       initScale();
/*      */     }
/* 2054 */     Object localObject1 = getDataElements(paramArrayOfFloat, paramInt, null);
/*      */     Object localObject2;
/* 2055 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/* 2058 */       localObject2 = (byte[])localObject1;
/* 2059 */       return localObject2[0] & 0xFF;
/*      */     case 1:
/* 2063 */       localObject2 = (short[])localObject1;
/* 2064 */       return localObject2[0] & 0xFFFF;
/*      */     case 3:
/* 2068 */       localObject2 = (int[])localObject1;
/* 2069 */       return localObject2[0];
/*      */     case 2:
/*      */     }
/* 2072 */     throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public Object getDataElements(float[] paramArrayOfFloat, int paramInt, Object paramObject)
/*      */   {
/* 2113 */     int i = (this.supportsAlpha) && (this.isAlphaPremultiplied) ? 1 : 0;
/*      */ 
/* 2115 */     if (this.needScaleInit)
/* 2116 */       initScale();
/*      */     float[] arrayOfFloat1;
/* 2118 */     if (this.nonStdScale) {
/* 2119 */       arrayOfFloat1 = new float[this.numComponents];
/* 2120 */       int j = 0; for (int k = paramInt; j < this.numColorComponents; 
/* 2121 */         k++) {
/* 2122 */         arrayOfFloat1[j] = ((paramArrayOfFloat[k] - this.compOffset[j]) * this.compScale[j]);
/*      */ 
/* 2126 */         if (arrayOfFloat1[j] < 0.0F) {
/* 2127 */           arrayOfFloat1[j] = 0.0F;
/*      */         }
/* 2129 */         if (arrayOfFloat1[j] > 1.0F)
/* 2130 */           arrayOfFloat1[j] = 1.0F;
/* 2121 */         j++;
/*      */       }
/*      */ 
/* 2133 */       if (this.supportsAlpha) {
/* 2134 */         arrayOfFloat1[this.numColorComponents] = paramArrayOfFloat[(this.numColorComponents + paramInt)];
/*      */       }
/*      */ 
/* 2137 */       paramInt = 0;
/*      */     } else {
/* 2139 */       arrayOfFloat1 = paramArrayOfFloat;
/*      */     }
/*      */     int i2;
/*      */     int i4;
/*      */     int i6;
/*      */     int i8;
/*      */     int i10;
/* 2141 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/*      */       byte[] arrayOfByte;
/* 2144 */       if (paramObject == null)
/* 2145 */         arrayOfByte = new byte[this.numComponents];
/*      */       else
/* 2147 */         arrayOfByte = (byte[])paramObject;
/*      */       int n;
/* 2149 */       if (i != 0) {
/* 2150 */         float f1 = arrayOfFloat1[(this.numColorComponents + paramInt)];
/*      */ 
/* 2152 */         n = 0; for (i2 = paramInt; n < this.numColorComponents; 
/* 2153 */           i2++) {
/* 2154 */           arrayOfByte[n] = ((byte)(int)(arrayOfFloat1[i2] * f1 * ((1 << this.nBits[n]) - 1) + 0.5F));
/*      */ 
/* 2153 */           n++;
/*      */         }
/*      */ 
/* 2157 */         arrayOfByte[this.numColorComponents] = ((byte)(int)(f1 * ((1 << this.nBits[this.numColorComponents]) - 1) + 0.5F));
/*      */       }
/*      */       else
/*      */       {
/* 2162 */         int m = 0; for (n = paramInt; m < this.numComponents; 
/* 2163 */           n++) {
/* 2164 */           arrayOfByte[m] = ((byte)(int)(arrayOfFloat1[n] * ((1 << this.nBits[m]) - 1) + 0.5F));
/*      */ 
/* 2163 */           m++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2168 */       return arrayOfByte;
/*      */     case 1:
/*      */       short[] arrayOfShort1;
/* 2171 */       if (paramObject == null)
/* 2172 */         arrayOfShort1 = new short[this.numComponents];
/*      */       else {
/* 2174 */         arrayOfShort1 = (short[])paramObject;
/*      */       }
/* 2176 */       if (i != 0) {
/* 2177 */         float f2 = arrayOfFloat1[(this.numColorComponents + paramInt)];
/*      */ 
/* 2179 */         i2 = 0; for (i4 = paramInt; i2 < this.numColorComponents; 
/* 2180 */           i4++) {
/* 2181 */           arrayOfShort1[i2] = ((short)(int)(arrayOfFloat1[i4] * f2 * ((1 << this.nBits[i2]) - 1) + 0.5F));
/*      */ 
/* 2180 */           i2++;
/*      */         }
/*      */ 
/* 2185 */         arrayOfShort1[this.numColorComponents] = ((short)(int)(f2 * ((1 << this.nBits[this.numColorComponents]) - 1) + 0.5F));
/*      */       }
/*      */       else
/*      */       {
/* 2190 */         int i1 = 0; for (i2 = paramInt; i1 < this.numComponents; 
/* 2191 */           i2++) {
/* 2192 */           arrayOfShort1[i1] = ((short)(int)(arrayOfFloat1[i2] * ((1 << this.nBits[i1]) - 1) + 0.5F));
/*      */ 
/* 2191 */           i1++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2197 */       return arrayOfShort1;
/*      */     case 3:
/*      */       int[] arrayOfInt;
/* 2200 */       if (paramObject == null)
/* 2201 */         arrayOfInt = new int[this.numComponents];
/*      */       else {
/* 2203 */         arrayOfInt = (int[])paramObject;
/*      */       }
/* 2205 */       if (i != 0) {
/* 2206 */         float f3 = arrayOfFloat1[(this.numColorComponents + paramInt)];
/*      */ 
/* 2208 */         i4 = 0; for (i6 = paramInt; i4 < this.numColorComponents; 
/* 2209 */           i6++) {
/* 2210 */           arrayOfInt[i4] = ((int)(arrayOfFloat1[i6] * f3 * ((1 << this.nBits[i4]) - 1) + 0.5F));
/*      */ 
/* 2209 */           i4++;
/*      */         }
/*      */ 
/* 2213 */         arrayOfInt[this.numColorComponents] = ((int)(f3 * ((1 << this.nBits[this.numColorComponents]) - 1) + 0.5F));
/*      */       }
/*      */       else
/*      */       {
/* 2218 */         int i3 = 0; for (i4 = paramInt; i3 < this.numComponents; 
/* 2219 */           i4++) {
/* 2220 */           arrayOfInt[i3] = ((int)(arrayOfFloat1[i4] * ((1 << this.nBits[i3]) - 1) + 0.5F));
/*      */ 
/* 2219 */           i3++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2224 */       return arrayOfInt;
/*      */     case 2:
/*      */       short[] arrayOfShort2;
/* 2227 */       if (paramObject == null)
/* 2228 */         arrayOfShort2 = new short[this.numComponents];
/*      */       else {
/* 2230 */         arrayOfShort2 = (short[])paramObject;
/*      */       }
/* 2232 */       if (i != 0) {
/* 2233 */         float f4 = arrayOfFloat1[(this.numColorComponents + paramInt)];
/*      */ 
/* 2235 */         i6 = 0; for (i8 = paramInt; i6 < this.numColorComponents; 
/* 2236 */           i8++) {
/* 2237 */           arrayOfShort2[i6] = ((short)(int)(arrayOfFloat1[i8] * f4 * 32767.0F + 0.5F));
/*      */ 
/* 2236 */           i6++;
/*      */         }
/*      */ 
/* 2240 */         arrayOfShort2[this.numColorComponents] = ((short)(int)(f4 * 32767.0F + 0.5F));
/*      */       } else {
/* 2242 */         int i5 = 0; for (i6 = paramInt; i5 < this.numComponents; 
/* 2243 */           i6++) {
/* 2244 */           arrayOfShort2[i5] = ((short)(int)(arrayOfFloat1[i6] * 32767.0F + 0.5F));
/*      */ 
/* 2243 */           i5++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2248 */       return arrayOfShort2;
/*      */     case 4:
/*      */       float[] arrayOfFloat2;
/* 2251 */       if (paramObject == null)
/* 2252 */         arrayOfFloat2 = new float[this.numComponents];
/*      */       else {
/* 2254 */         arrayOfFloat2 = (float[])paramObject;
/*      */       }
/* 2256 */       if (i != 0) {
/* 2257 */         float f5 = paramArrayOfFloat[(this.numColorComponents + paramInt)];
/* 2258 */         i8 = 0; for (i10 = paramInt; i8 < this.numColorComponents; 
/* 2259 */           i10++) {
/* 2260 */           paramArrayOfFloat[i10] *= f5;
/*      */ 
/* 2259 */           i8++;
/*      */         }
/*      */ 
/* 2262 */         arrayOfFloat2[this.numColorComponents] = f5;
/*      */       } else {
/* 2264 */         int i7 = 0; for (i8 = paramInt; i7 < this.numComponents; 
/* 2265 */           i8++) {
/* 2266 */           arrayOfFloat2[i7] = paramArrayOfFloat[i8];
/*      */ 
/* 2265 */           i7++;
/*      */         }
/*      */       }
/*      */ 
/* 2269 */       return arrayOfFloat2;
/*      */     case 5:
/*      */       double[] arrayOfDouble;
/* 2272 */       if (paramObject == null)
/* 2273 */         arrayOfDouble = new double[this.numComponents];
/*      */       else {
/* 2275 */         arrayOfDouble = (double[])paramObject;
/*      */       }
/* 2277 */       if (i != 0) {
/* 2278 */         double d = paramArrayOfFloat[(this.numColorComponents + paramInt)];
/*      */ 
/* 2280 */         int i11 = 0; for (int i12 = paramInt; i11 < this.numColorComponents; 
/* 2281 */           i12++) {
/* 2282 */           arrayOfDouble[i11] = (paramArrayOfFloat[i12] * d);
/*      */ 
/* 2281 */           i11++;
/*      */         }
/*      */ 
/* 2284 */         arrayOfDouble[this.numColorComponents] = d;
/*      */       } else {
/* 2286 */         int i9 = 0; for (i10 = paramInt; i9 < this.numComponents; 
/* 2287 */           i10++) {
/* 2288 */           arrayOfDouble[i9] = paramArrayOfFloat[i10];
/*      */ 
/* 2287 */           i9++;
/*      */         }
/*      */       }
/*      */ 
/* 2291 */       return arrayOfDouble;
/*      */     }
/* 2293 */     throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public float[] getNormalizedComponents(Object paramObject, float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 2346 */     if (paramArrayOfFloat == null)
/* 2347 */       paramArrayOfFloat = new float[this.numComponents + paramInt];
/*      */     int k;
/*      */     int n;
/*      */     int i1;
/*      */     int i2;
/*      */     int i3;
/* 2349 */     switch (this.transferType) {
/*      */     case 0:
/* 2351 */       byte[] arrayOfByte = (byte[])paramObject;
/* 2352 */       int j = 0; for (k = paramInt; j < this.numComponents; k++) {
/* 2353 */         paramArrayOfFloat[k] = ((arrayOfByte[j] & 0xFF) / ((1 << this.nBits[j]) - 1));
/*      */ 
/* 2352 */         j++;
/*      */       }
/*      */ 
/* 2356 */       break;
/*      */     case 1:
/* 2358 */       short[] arrayOfShort1 = (short[])paramObject;
/* 2359 */       k = 0; for (n = paramInt; k < this.numComponents; n++) {
/* 2360 */         paramArrayOfFloat[n] = ((arrayOfShort1[k] & 0xFFFF) / ((1 << this.nBits[k]) - 1));
/*      */ 
/* 2359 */         k++;
/*      */       }
/*      */ 
/* 2363 */       break;
/*      */     case 3:
/* 2365 */       int[] arrayOfInt = (int[])paramObject;
/* 2366 */       n = 0; for (i1 = paramInt; n < this.numComponents; i1++) {
/* 2367 */         paramArrayOfFloat[i1] = (arrayOfInt[n] / ((1 << this.nBits[n]) - 1));
/*      */ 
/* 2366 */         n++;
/*      */       }
/*      */ 
/* 2370 */       break;
/*      */     case 2:
/* 2372 */       short[] arrayOfShort2 = (short[])paramObject;
/* 2373 */       i1 = 0; for (i2 = paramInt; i1 < this.numComponents; i2++) {
/* 2374 */         paramArrayOfFloat[i2] = (arrayOfShort2[i1] / 32767.0F);
/*      */ 
/* 2373 */         i1++;
/*      */       }
/*      */ 
/* 2376 */       break;
/*      */     case 4:
/* 2378 */       float[] arrayOfFloat = (float[])paramObject;
/* 2379 */       i2 = 0; for (i3 = paramInt; i2 < this.numComponents; i3++) {
/* 2380 */         paramArrayOfFloat[i3] = arrayOfFloat[i2];
/*      */ 
/* 2379 */         i2++;
/*      */       }
/*      */ 
/* 2382 */       break;
/*      */     case 5:
/* 2384 */       double[] arrayOfDouble = (double[])paramObject;
/* 2385 */       i3 = 0; for (int i4 = paramInt; i3 < this.numComponents; i4++) {
/* 2386 */         paramArrayOfFloat[i4] = ((float)arrayOfDouble[i3]);
/*      */ 
/* 2385 */         i3++;
/*      */       }
/*      */ 
/* 2388 */       break;
/*      */     default:
/* 2390 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/* 2395 */     if ((this.supportsAlpha) && (this.isAlphaPremultiplied)) {
/* 2396 */       float f1 = paramArrayOfFloat[(this.numColorComponents + paramInt)];
/* 2397 */       if (f1 != 0.0F) {
/* 2398 */         float f2 = 1.0F / f1;
/* 2399 */         for (int m = paramInt; m < this.numColorComponents + paramInt; 
/* 2400 */           m++) {
/* 2401 */           paramArrayOfFloat[m] *= f2;
/*      */         }
/*      */       }
/*      */     }
/* 2405 */     if (this.min != null)
/*      */     {
/* 2419 */       for (int i = 0; i < this.numColorComponents; i++) {
/* 2420 */         paramArrayOfFloat[(i + paramInt)] = (this.min[i] + this.diffMinMax[i] * paramArrayOfFloat[(i + paramInt)]);
/*      */       }
/*      */     }
/*      */ 
/* 2424 */     return paramArrayOfFloat;
/*      */   }
/*      */ 
/*      */   public ColorModel coerceData(WritableRaster paramWritableRaster, boolean paramBoolean)
/*      */   {
/* 2452 */     if ((!this.supportsAlpha) || (this.isAlphaPremultiplied == paramBoolean))
/*      */     {
/* 2456 */       return this;
/*      */     }
/*      */ 
/* 2459 */     int i = paramWritableRaster.getWidth();
/* 2460 */     int j = paramWritableRaster.getHeight();
/* 2461 */     int k = paramWritableRaster.getNumBands() - 1;
/*      */ 
/* 2463 */     int m = paramWritableRaster.getMinX();
/* 2464 */     int n = paramWritableRaster.getMinY();
/*      */     Object localObject1;
/*      */     int i4;
/*      */     int i1;
/*      */     float f1;
/*      */     int i7;
/*      */     int i3;
/* 2466 */     if (paramBoolean)
/*      */     {
/*      */       Object localObject2;
/*      */       float f3;
/*      */       int i5;
/* 2467 */       switch (this.transferType) {
/*      */       case 0:
/* 2469 */         localObject1 = null;
/* 2470 */         localObject2 = null;
/* 2471 */         f3 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2472 */         for (i4 = 0; i4 < j; n++) {
/* 2473 */           i1 = m;
/* 2474 */           for (i5 = 0; i5 < i; i1++) {
/* 2475 */             localObject1 = (byte[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2477 */             f1 = (localObject1[k] & 0xFF) * f3;
/* 2478 */             if (f1 != 0.0F) {
/* 2479 */               for (i7 = 0; i7 < k; i7++) {
/* 2480 */                 localObject1[i7] = ((byte)(int)((localObject1[i7] & 0xFF) * f1 + 0.5F));
/*      */               }
/*      */ 
/* 2483 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2485 */               if (localObject2 == null) {
/* 2486 */                 localObject2 = new byte[this.numComponents];
/* 2487 */                 Arrays.fill((byte[])localObject2, (byte)0);
/*      */               }
/* 2489 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2474 */             i5++;
/*      */           }
/* 2472 */           i4++;
/*      */         }
/*      */ 
/* 2494 */         break;
/*      */       case 1:
/* 2496 */         localObject1 = null;
/* 2497 */         localObject2 = null;
/* 2498 */         f3 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2499 */         for (i4 = 0; i4 < j; n++) {
/* 2500 */           i1 = m;
/* 2501 */           for (i5 = 0; i5 < i; i1++) {
/* 2502 */             localObject1 = (short[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2504 */             f1 = (localObject1[k] & 0xFFFF) * f3;
/* 2505 */             if (f1 != 0.0F) {
/* 2506 */               for (i7 = 0; i7 < k; i7++) {
/* 2507 */                 localObject1[i7] = ((short)(int)((localObject1[i7] & 0xFFFF) * f1 + 0.5F));
/*      */               }
/*      */ 
/* 2511 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2513 */               if (localObject2 == null) {
/* 2514 */                 localObject2 = new short[this.numComponents];
/* 2515 */                 Arrays.fill((short[])localObject2, (short)0);
/*      */               }
/* 2517 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2501 */             i5++;
/*      */           }
/* 2499 */           i4++;
/*      */         }
/*      */ 
/* 2522 */         break;
/*      */       case 3:
/* 2524 */         localObject1 = null;
/* 2525 */         localObject2 = null;
/* 2526 */         f3 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2527 */         for (i4 = 0; i4 < j; n++) {
/* 2528 */           i1 = m;
/* 2529 */           for (i5 = 0; i5 < i; i1++) {
/* 2530 */             localObject1 = (int[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2532 */             f1 = localObject1[k] * f3;
/* 2533 */             if (f1 != 0.0F) {
/* 2534 */               for (i7 = 0; i7 < k; i7++) {
/* 2535 */                 localObject1[i7] = ((int)(localObject1[i7] * f1 + 0.5F));
/*      */               }
/*      */ 
/* 2538 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2540 */               if (localObject2 == null) {
/* 2541 */                 localObject2 = new int[this.numComponents];
/* 2542 */                 Arrays.fill((int[])localObject2, 0);
/*      */               }
/* 2544 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2529 */             i5++;
/*      */           }
/* 2527 */           i4++;
/*      */         }
/*      */ 
/* 2549 */         break;
/*      */       case 2:
/* 2551 */         localObject1 = null;
/* 2552 */         localObject2 = null;
/* 2553 */         f3 = 3.051851E-005F;
/* 2554 */         for (i4 = 0; i4 < j; n++) {
/* 2555 */           i1 = m;
/* 2556 */           for (i5 = 0; i5 < i; i1++) {
/* 2557 */             localObject1 = (short[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2559 */             f1 = localObject1[k] * f3;
/* 2560 */             if (f1 != 0.0F) {
/* 2561 */               for (i7 = 0; i7 < k; i7++) {
/* 2562 */                 localObject1[i7] = ((short)(int)(localObject1[i7] * f1 + 0.5F));
/*      */               }
/*      */ 
/* 2565 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2567 */               if (localObject2 == null) {
/* 2568 */                 localObject2 = new short[this.numComponents];
/* 2569 */                 Arrays.fill((short[])localObject2, (short)0);
/*      */               }
/* 2571 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2556 */             i5++;
/*      */           }
/* 2554 */           i4++;
/*      */         }
/*      */ 
/* 2576 */         break;
/*      */       case 4:
/* 2578 */         localObject1 = null;
/* 2579 */         localObject2 = null;
/* 2580 */         for (i3 = 0; i3 < j; n++) {
/* 2581 */           i1 = m;
/* 2582 */           for (i4 = 0; i4 < i; i1++) {
/* 2583 */             localObject1 = (float[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2585 */             f1 = localObject1[k];
/* 2586 */             if (f1 != 0.0F) {
/* 2587 */               for (i5 = 0; i5 < k; i5++) {
/* 2588 */                 localObject1[i5] *= f1;
/*      */               }
/* 2590 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2592 */               if (localObject2 == null) {
/* 2593 */                 localObject2 = new float[this.numComponents];
/* 2594 */                 Arrays.fill((float[])localObject2, 0.0F);
/*      */               }
/* 2596 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2582 */             i4++;
/*      */           }
/* 2580 */           i3++;
/*      */         }
/*      */ 
/* 2601 */         break;
/*      */       case 5:
/* 2603 */         localObject1 = null;
/* 2604 */         localObject2 = null;
/* 2605 */         for (i3 = 0; i3 < j; n++) {
/* 2606 */           i1 = m;
/* 2607 */           for (i4 = 0; i4 < i; i1++) {
/* 2608 */             localObject1 = (double[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2610 */             double d2 = localObject1[k];
/* 2611 */             if (d2 != 0.0D) {
/* 2612 */               for (int i8 = 0; i8 < k; i8++) {
/* 2613 */                 localObject1[i8] *= d2;
/*      */               }
/* 2615 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             } else {
/* 2617 */               if (localObject2 == null) {
/* 2618 */                 localObject2 = new double[this.numComponents];
/* 2619 */                 Arrays.fill((double[])localObject2, 0.0D);
/*      */               }
/* 2621 */               paramWritableRaster.setDataElements(i1, n, localObject2);
/*      */             }
/* 2607 */             i4++;
/*      */           }
/* 2605 */           i3++;
/*      */         }
/*      */ 
/* 2626 */         break;
/*      */       default:
/* 2628 */         throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       float f2;
/*      */       float f5;
/*      */       int i2;
/* 2634 */       switch (this.transferType) {
/*      */       case 0:
/* 2636 */         localObject1 = null;
/* 2637 */         f2 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2638 */         for (i3 = 0; i3 < j; n++) {
/* 2639 */           i1 = m;
/* 2640 */           for (i4 = 0; i4 < i; i1++) {
/* 2641 */             localObject1 = (byte[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2643 */             f1 = (localObject1[k] & 0xFF) * f2;
/* 2644 */             if (f1 != 0.0F) {
/* 2645 */               f5 = 1.0F / f1;
/* 2646 */               for (i7 = 0; i7 < k; i7++) {
/* 2647 */                 localObject1[i7] = ((byte)(int)((localObject1[i7] & 0xFF) * f5 + 0.5F));
/*      */               }
/*      */ 
/* 2650 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2640 */             i4++;
/*      */           }
/* 2638 */           i3++;
/*      */         }
/*      */ 
/* 2655 */         break;
/*      */       case 1:
/* 2657 */         localObject1 = null;
/* 2658 */         f2 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2659 */         for (i3 = 0; i3 < j; n++) {
/* 2660 */           i1 = m;
/* 2661 */           for (i4 = 0; i4 < i; i1++) {
/* 2662 */             localObject1 = (short[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2664 */             f1 = (localObject1[k] & 0xFFFF) * f2;
/* 2665 */             if (f1 != 0.0F) {
/* 2666 */               f5 = 1.0F / f1;
/* 2667 */               for (i7 = 0; i7 < k; i7++) {
/* 2668 */                 localObject1[i7] = ((short)(int)((localObject1[i7] & 0xFFFF) * f5 + 0.5F));
/*      */               }
/*      */ 
/* 2671 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2661 */             i4++;
/*      */           }
/* 2659 */           i3++;
/*      */         }
/*      */ 
/* 2676 */         break;
/*      */       case 3:
/* 2678 */         localObject1 = null;
/* 2679 */         f2 = 1.0F / ((1 << this.nBits[k]) - 1);
/* 2680 */         for (i3 = 0; i3 < j; n++) {
/* 2681 */           i1 = m;
/* 2682 */           for (i4 = 0; i4 < i; i1++) {
/* 2683 */             localObject1 = (int[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2685 */             f1 = localObject1[k] * f2;
/* 2686 */             if (f1 != 0.0F) {
/* 2687 */               f5 = 1.0F / f1;
/* 2688 */               for (i7 = 0; i7 < k; i7++) {
/* 2689 */                 localObject1[i7] = ((int)(localObject1[i7] * f5 + 0.5F));
/*      */               }
/*      */ 
/* 2692 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2682 */             i4++;
/*      */           }
/* 2680 */           i3++;
/*      */         }
/*      */ 
/* 2697 */         break;
/*      */       case 2:
/* 2699 */         localObject1 = null;
/* 2700 */         f2 = 3.051851E-005F;
/* 2701 */         for (i3 = 0; i3 < j; n++) {
/* 2702 */           i1 = m;
/* 2703 */           for (i4 = 0; i4 < i; i1++) {
/* 2704 */             localObject1 = (short[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2706 */             f1 = localObject1[k] * f2;
/* 2707 */             if (f1 != 0.0F) {
/* 2708 */               f5 = 1.0F / f1;
/* 2709 */               for (i7 = 0; i7 < k; i7++) {
/* 2710 */                 localObject1[i7] = ((short)(int)(localObject1[i7] * f5 + 0.5F));
/*      */               }
/*      */ 
/* 2713 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2703 */             i4++;
/*      */           }
/* 2701 */           i3++;
/*      */         }
/*      */ 
/* 2718 */         break;
/*      */       case 4:
/* 2720 */         localObject1 = null;
/* 2721 */         for (i2 = 0; i2 < j; n++) {
/* 2722 */           i1 = m;
/* 2723 */           for (i3 = 0; i3 < i; i1++) {
/* 2724 */             localObject1 = (float[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2726 */             f1 = localObject1[k];
/* 2727 */             if (f1 != 0.0F) {
/* 2728 */               float f4 = 1.0F / f1;
/* 2729 */               for (int i6 = 0; i6 < k; i6++) {
/* 2730 */                 localObject1[i6] *= f4;
/*      */               }
/* 2732 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2723 */             i3++;
/*      */           }
/* 2721 */           i2++;
/*      */         }
/*      */ 
/* 2737 */         break;
/*      */       case 5:
/* 2739 */         localObject1 = null;
/* 2740 */         for (i2 = 0; i2 < j; n++) {
/* 2741 */           i1 = m;
/* 2742 */           for (i3 = 0; i3 < i; i1++) {
/* 2743 */             localObject1 = (double[])paramWritableRaster.getDataElements(i1, n, localObject1);
/*      */ 
/* 2745 */             double d1 = localObject1[k];
/* 2746 */             if (d1 != 0.0D) {
/* 2747 */               double d3 = 1.0D / d1;
/* 2748 */               for (int i9 = 0; i9 < k; i9++) {
/* 2749 */                 localObject1[i9] *= d3;
/*      */               }
/* 2751 */               paramWritableRaster.setDataElements(i1, n, localObject1);
/*      */             }
/* 2742 */             i3++;
/*      */           }
/* 2740 */           i2++;
/*      */         }
/*      */ 
/* 2756 */         break;
/*      */       default:
/* 2758 */         throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2764 */     if (!this.signed) {
/* 2765 */       return new ComponentColorModel(this.colorSpace, this.nBits, this.supportsAlpha, paramBoolean, this.transparency, this.transferType);
/*      */     }
/*      */ 
/* 2769 */     return new ComponentColorModel(this.colorSpace, this.supportsAlpha, paramBoolean, this.transparency, this.transferType);
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleRaster(Raster paramRaster)
/*      */   {
/* 2787 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/*      */ 
/* 2789 */     if ((localSampleModel instanceof ComponentSampleModel)) {
/* 2790 */       if (localSampleModel.getNumBands() != getNumComponents()) {
/* 2791 */         return false;
/*      */       }
/* 2793 */       for (int i = 0; i < this.nBits.length; i++) {
/* 2794 */         if (localSampleModel.getSampleSize(i) < this.nBits[i]) {
/* 2795 */           return false;
/*      */         }
/*      */       }
/* 2798 */       return paramRaster.getTransferType() == this.transferType;
/*      */     }
/*      */ 
/* 2801 */     return false;
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/* 2819 */     int i = paramInt1 * paramInt2 * this.numComponents;
/* 2820 */     WritableRaster localWritableRaster = null;
/*      */ 
/* 2822 */     switch (this.transferType) {
/*      */     case 0:
/*      */     case 1:
/* 2825 */       localWritableRaster = Raster.createInterleavedRaster(this.transferType, paramInt1, paramInt2, this.numComponents, null);
/*      */ 
/* 2828 */       break;
/*      */     default:
/* 2830 */       SampleModel localSampleModel = createCompatibleSampleModel(paramInt1, paramInt2);
/* 2831 */       DataBuffer localDataBuffer = localSampleModel.createDataBuffer();
/* 2832 */       localWritableRaster = Raster.createWritableRaster(localSampleModel, localDataBuffer, null);
/*      */     }
/*      */ 
/* 2835 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*      */   {
/* 2851 */     int[] arrayOfInt = new int[this.numComponents];
/* 2852 */     for (int i = 0; i < this.numComponents; i++) {
/* 2853 */       arrayOfInt[i] = i;
/*      */     }
/* 2855 */     switch (this.transferType) {
/*      */     case 0:
/*      */     case 1:
/* 2858 */       return new PixelInterleavedSampleModel(this.transferType, paramInt1, paramInt2, this.numComponents, paramInt1 * this.numComponents, arrayOfInt);
/*      */     }
/*      */ 
/* 2863 */     return new ComponentSampleModel(this.transferType, paramInt1, paramInt2, this.numComponents, paramInt1 * this.numComponents, arrayOfInt);
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleSampleModel(SampleModel paramSampleModel)
/*      */   {
/* 2883 */     if (!(paramSampleModel instanceof ComponentSampleModel)) {
/* 2884 */       return false;
/*      */     }
/*      */ 
/* 2888 */     if (this.numComponents != paramSampleModel.getNumBands()) {
/* 2889 */       return false;
/*      */     }
/*      */ 
/* 2892 */     if (paramSampleModel.getTransferType() != this.transferType) {
/* 2893 */       return false;
/*      */     }
/*      */ 
/* 2896 */     return true;
/*      */   }
/*      */ 
/*      */   public WritableRaster getAlphaRaster(WritableRaster paramWritableRaster)
/*      */   {
/* 2916 */     if (!hasAlpha()) {
/* 2917 */       return null;
/*      */     }
/*      */ 
/* 2920 */     int i = paramWritableRaster.getMinX();
/* 2921 */     int j = paramWritableRaster.getMinY();
/* 2922 */     int[] arrayOfInt = new int[1];
/* 2923 */     arrayOfInt[0] = (paramWritableRaster.getNumBands() - 1);
/* 2924 */     return paramWritableRaster.createWritableChild(i, j, paramWritableRaster.getWidth(), paramWritableRaster.getHeight(), i, j, arrayOfInt);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 2937 */     if (!super.equals(paramObject)) {
/* 2938 */       return false;
/*      */     }
/*      */ 
/* 2941 */     if (paramObject.getClass() != getClass()) {
/* 2942 */       return false;
/*      */     }
/*      */ 
/* 2945 */     return true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ComponentColorModel
 * JD-Core Version:    0.6.2
 */