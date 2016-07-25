/*      */ package java.awt.image;
/*      */ 
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class DirectColorModel extends PackedColorModel
/*      */ {
/*      */   private int red_mask;
/*      */   private int green_mask;
/*      */   private int blue_mask;
/*      */   private int alpha_mask;
/*      */   private int red_offset;
/*      */   private int green_offset;
/*      */   private int blue_offset;
/*      */   private int alpha_offset;
/*      */   private int red_scale;
/*      */   private int green_scale;
/*      */   private int blue_scale;
/*      */   private int alpha_scale;
/*      */   private boolean is_LinearRGB;
/*      */   private int lRGBprecision;
/*      */   private byte[] tosRGB8LUT;
/*      */   private byte[] fromsRGB8LUT8;
/*      */   private short[] fromsRGB8LUT16;
/*      */ 
/*      */   public DirectColorModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  152 */     this(paramInt1, paramInt2, paramInt3, paramInt4, 0);
/*      */   }
/*      */ 
/*      */   public DirectColorModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  182 */     super(ColorSpace.getInstance(1000), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, false, paramInt5 == 0 ? 1 : 3, ColorModel.getDefaultTransferType(paramInt1));
/*      */ 
/*  186 */     setFields();
/*      */   }
/*      */ 
/*      */   public DirectColorModel(ColorSpace paramColorSpace, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6)
/*      */   {
/*  234 */     super(paramColorSpace, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean, paramInt5 == 0 ? 1 : 3, paramInt6);
/*      */ 
/*  238 */     if (ColorModel.isLinearRGBspace(this.colorSpace)) {
/*  239 */       this.is_LinearRGB = true;
/*  240 */       if (this.maxBits <= 8) {
/*  241 */         this.lRGBprecision = 8;
/*  242 */         this.tosRGB8LUT = ColorModel.getLinearRGB8TosRGB8LUT();
/*  243 */         this.fromsRGB8LUT8 = ColorModel.getsRGB8ToLinearRGB8LUT();
/*      */       } else {
/*  245 */         this.lRGBprecision = 16;
/*  246 */         this.tosRGB8LUT = ColorModel.getLinearRGB16TosRGB8LUT();
/*  247 */         this.fromsRGB8LUT16 = ColorModel.getsRGB8ToLinearRGB16LUT();
/*      */       }
/*  249 */     } else if (!this.is_sRGB) {
/*  250 */       for (int i = 0; i < 3; i++)
/*      */       {
/*  253 */         if ((paramColorSpace.getMinValue(i) != 0.0F) || (paramColorSpace.getMaxValue(i) != 1.0F))
/*      */         {
/*  255 */           throw new IllegalArgumentException("Illegal min/max RGB component value");
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  260 */     setFields();
/*      */   }
/*      */ 
/*      */   public final int getRedMask()
/*      */   {
/*  270 */     return this.maskArray[0];
/*      */   }
/*      */ 
/*      */   public final int getGreenMask()
/*      */   {
/*  280 */     return this.maskArray[1];
/*      */   }
/*      */ 
/*      */   public final int getBlueMask()
/*      */   {
/*  290 */     return this.maskArray[2];
/*      */   }
/*      */ 
/*      */   public final int getAlphaMask()
/*      */   {
/*  300 */     if (this.supportsAlpha) {
/*  301 */       return this.maskArray[3];
/*      */     }
/*  303 */     return 0;
/*      */   }
/*      */ 
/*      */   private float[] getDefaultRGBComponents(int paramInt)
/*      */   {
/*  314 */     int[] arrayOfInt = getComponents(paramInt, null, 0);
/*  315 */     float[] arrayOfFloat = getNormalizedComponents(arrayOfInt, 0, null, 0);
/*      */ 
/*  317 */     return this.colorSpace.toRGB(arrayOfFloat);
/*      */   }
/*      */ 
/*      */   private int getsRGBComponentFromsRGB(int paramInt1, int paramInt2)
/*      */   {
/*  322 */     int i = (paramInt1 & this.maskArray[paramInt2]) >>> this.maskOffsets[paramInt2];
/*  323 */     if (this.isAlphaPremultiplied) {
/*  324 */       int j = (paramInt1 & this.maskArray[3]) >>> this.maskOffsets[3];
/*  325 */       i = j == 0 ? 0 : (int)(i * this.scaleFactors[paramInt2] * 255.0F / (j * this.scaleFactors[3]) + 0.5F);
/*      */     }
/*  328 */     else if (this.scaleFactors[paramInt2] != 1.0F) {
/*  329 */       i = (int)(i * this.scaleFactors[paramInt2] + 0.5F);
/*      */     }
/*  331 */     return i;
/*      */   }
/*      */ 
/*      */   private int getsRGBComponentFromLinearRGB(int paramInt1, int paramInt2)
/*      */   {
/*  336 */     int i = (paramInt1 & this.maskArray[paramInt2]) >>> this.maskOffsets[paramInt2];
/*  337 */     if (this.isAlphaPremultiplied) {
/*  338 */       float f = (1 << this.lRGBprecision) - 1;
/*  339 */       int j = (paramInt1 & this.maskArray[3]) >>> this.maskOffsets[3];
/*  340 */       i = j == 0 ? 0 : (int)(i * this.scaleFactors[paramInt2] * f / (j * this.scaleFactors[3]) + 0.5F);
/*      */     }
/*  343 */     else if (this.nBits[paramInt2] != this.lRGBprecision) {
/*  344 */       if (this.lRGBprecision == 16)
/*  345 */         i = (int)(i * this.scaleFactors[paramInt2] * 257.0F + 0.5F);
/*      */       else {
/*  347 */         i = (int)(i * this.scaleFactors[paramInt2] + 0.5F);
/*      */       }
/*      */     }
/*      */ 
/*  351 */     return this.tosRGB8LUT[i] & 0xFF;
/*      */   }
/*      */ 
/*      */   public final int getRed(int paramInt)
/*      */   {
/*  369 */     if (this.is_sRGB)
/*  370 */       return getsRGBComponentFromsRGB(paramInt, 0);
/*  371 */     if (this.is_LinearRGB) {
/*  372 */       return getsRGBComponentFromLinearRGB(paramInt, 0);
/*      */     }
/*  374 */     float[] arrayOfFloat = getDefaultRGBComponents(paramInt);
/*  375 */     return (int)(arrayOfFloat[0] * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public final int getGreen(int paramInt)
/*      */   {
/*  392 */     if (this.is_sRGB)
/*  393 */       return getsRGBComponentFromsRGB(paramInt, 1);
/*  394 */     if (this.is_LinearRGB) {
/*  395 */       return getsRGBComponentFromLinearRGB(paramInt, 1);
/*      */     }
/*  397 */     float[] arrayOfFloat = getDefaultRGBComponents(paramInt);
/*  398 */     return (int)(arrayOfFloat[1] * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public final int getBlue(int paramInt)
/*      */   {
/*  415 */     if (this.is_sRGB)
/*  416 */       return getsRGBComponentFromsRGB(paramInt, 2);
/*  417 */     if (this.is_LinearRGB) {
/*  418 */       return getsRGBComponentFromLinearRGB(paramInt, 2);
/*      */     }
/*  420 */     float[] arrayOfFloat = getDefaultRGBComponents(paramInt);
/*  421 */     return (int)(arrayOfFloat[2] * 255.0F + 0.5F);
/*      */   }
/*      */ 
/*      */   public final int getAlpha(int paramInt)
/*      */   {
/*  432 */     if (!this.supportsAlpha) return 255;
/*  433 */     int i = (paramInt & this.maskArray[3]) >>> this.maskOffsets[3];
/*  434 */     if (this.scaleFactors[3] != 1.0F) {
/*  435 */       i = (int)(i * this.scaleFactors[3] + 0.5F);
/*      */     }
/*  437 */     return i;
/*      */   }
/*      */ 
/*      */   public final int getRGB(int paramInt)
/*      */   {
/*  454 */     if ((this.is_sRGB) || (this.is_LinearRGB)) {
/*  455 */       return getAlpha(paramInt) << 24 | getRed(paramInt) << 16 | getGreen(paramInt) << 8 | getBlue(paramInt) << 0;
/*      */     }
/*      */ 
/*  460 */     float[] arrayOfFloat = getDefaultRGBComponents(paramInt);
/*  461 */     return getAlpha(paramInt) << 24 | (int)(arrayOfFloat[0] * 255.0F + 0.5F) << 16 | (int)(arrayOfFloat[1] * 255.0F + 0.5F) << 8 | (int)(arrayOfFloat[2] * 255.0F + 0.5F) << 0;
/*      */   }
/*      */ 
/*      */   public int getRed(Object paramObject)
/*      */   {
/*  499 */     int i = 0;
/*  500 */     switch (this.transferType) {
/*      */     case 0:
/*  502 */       byte[] arrayOfByte = (byte[])paramObject;
/*  503 */       i = arrayOfByte[0] & 0xFF;
/*  504 */       break;
/*      */     case 1:
/*  506 */       short[] arrayOfShort = (short[])paramObject;
/*  507 */       i = arrayOfShort[0] & 0xFFFF;
/*  508 */       break;
/*      */     case 3:
/*  510 */       int[] arrayOfInt = (int[])paramObject;
/*  511 */       i = arrayOfInt[0];
/*  512 */       break;
/*      */     case 2:
/*      */     default:
/*  514 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  517 */     return getRed(i);
/*      */   }
/*      */ 
/*      */   public int getGreen(Object paramObject)
/*      */   {
/*  552 */     int i = 0;
/*  553 */     switch (this.transferType) {
/*      */     case 0:
/*  555 */       byte[] arrayOfByte = (byte[])paramObject;
/*  556 */       i = arrayOfByte[0] & 0xFF;
/*  557 */       break;
/*      */     case 1:
/*  559 */       short[] arrayOfShort = (short[])paramObject;
/*  560 */       i = arrayOfShort[0] & 0xFFFF;
/*  561 */       break;
/*      */     case 3:
/*  563 */       int[] arrayOfInt = (int[])paramObject;
/*  564 */       i = arrayOfInt[0];
/*  565 */       break;
/*      */     case 2:
/*      */     default:
/*  567 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  570 */     return getGreen(i);
/*      */   }
/*      */ 
/*      */   public int getBlue(Object paramObject)
/*      */   {
/*  605 */     int i = 0;
/*  606 */     switch (this.transferType) {
/*      */     case 0:
/*  608 */       byte[] arrayOfByte = (byte[])paramObject;
/*  609 */       i = arrayOfByte[0] & 0xFF;
/*  610 */       break;
/*      */     case 1:
/*  612 */       short[] arrayOfShort = (short[])paramObject;
/*  613 */       i = arrayOfShort[0] & 0xFFFF;
/*  614 */       break;
/*      */     case 3:
/*  616 */       int[] arrayOfInt = (int[])paramObject;
/*  617 */       i = arrayOfInt[0];
/*  618 */       break;
/*      */     case 2:
/*      */     default:
/*  620 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  623 */     return getBlue(i);
/*      */   }
/*      */ 
/*      */   public int getAlpha(Object paramObject)
/*      */   {
/*  655 */     int i = 0;
/*  656 */     switch (this.transferType) {
/*      */     case 0:
/*  658 */       byte[] arrayOfByte = (byte[])paramObject;
/*  659 */       i = arrayOfByte[0] & 0xFF;
/*  660 */       break;
/*      */     case 1:
/*  662 */       short[] arrayOfShort = (short[])paramObject;
/*  663 */       i = arrayOfShort[0] & 0xFFFF;
/*  664 */       break;
/*      */     case 3:
/*  666 */       int[] arrayOfInt = (int[])paramObject;
/*  667 */       i = arrayOfInt[0];
/*  668 */       break;
/*      */     case 2:
/*      */     default:
/*  670 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  673 */     return getAlpha(i);
/*      */   }
/*      */ 
/*      */   public int getRGB(Object paramObject)
/*      */   {
/*  703 */     int i = 0;
/*  704 */     switch (this.transferType) {
/*      */     case 0:
/*  706 */       byte[] arrayOfByte = (byte[])paramObject;
/*  707 */       i = arrayOfByte[0] & 0xFF;
/*  708 */       break;
/*      */     case 1:
/*  710 */       short[] arrayOfShort = (short[])paramObject;
/*  711 */       i = arrayOfShort[0] & 0xFFFF;
/*  712 */       break;
/*      */     case 3:
/*  714 */       int[] arrayOfInt = (int[])paramObject;
/*  715 */       i = arrayOfInt[0];
/*  716 */       break;
/*      */     case 2:
/*      */     default:
/*  718 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  721 */     return getRGB(i);
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt, Object paramObject)
/*      */   {
/*  761 */     int[] arrayOfInt = null;
/*  762 */     if ((this.transferType == 3) && (paramObject != null))
/*      */     {
/*  764 */       arrayOfInt = (int[])paramObject;
/*  765 */       arrayOfInt[0] = 0;
/*      */     } else {
/*  767 */       arrayOfInt = new int[1];
/*      */     }
/*      */ 
/*  770 */     ColorModel localColorModel = ColorModel.getRGBdefault();
/*  771 */     if ((this == localColorModel) || (equals(localColorModel))) {
/*  772 */       arrayOfInt[0] = paramInt;
/*  773 */       return arrayOfInt;
/*      */     }
/*      */ 
/*  777 */     int i = paramInt >> 16 & 0xFF;
/*  778 */     int j = paramInt >> 8 & 0xFF;
/*  779 */     int k = paramInt & 0xFF;
/*      */     float f;
/*      */     int m;
/*      */     Object localObject;
/*  780 */     if ((this.is_sRGB) || (this.is_LinearRGB))
/*      */     {
/*      */       int n;
/*  783 */       if (this.is_LinearRGB) {
/*  784 */         if (this.lRGBprecision == 8) {
/*  785 */           i = this.fromsRGB8LUT8[i] & 0xFF;
/*  786 */           j = this.fromsRGB8LUT8[j] & 0xFF;
/*  787 */           k = this.fromsRGB8LUT8[k] & 0xFF;
/*  788 */           n = 8;
/*  789 */           f = 0.003921569F;
/*      */         } else {
/*  791 */           i = this.fromsRGB8LUT16[i] & 0xFFFF;
/*  792 */           j = this.fromsRGB8LUT16[j] & 0xFFFF;
/*  793 */           k = this.fromsRGB8LUT16[k] & 0xFFFF;
/*  794 */           n = 16;
/*  795 */           f = 1.525902E-005F;
/*      */         }
/*      */       } else {
/*  798 */         n = 8;
/*  799 */         f = 0.003921569F;
/*      */       }
/*  801 */       if (this.supportsAlpha) {
/*  802 */         m = paramInt >> 24 & 0xFF;
/*  803 */         if (this.isAlphaPremultiplied) {
/*  804 */           f *= m * 0.003921569F;
/*  805 */           n = -1;
/*      */         }
/*  807 */         if (this.nBits[3] != 8) {
/*  808 */           m = (int)(m * 0.003921569F * ((1 << this.nBits[3]) - 1) + 0.5F);
/*      */ 
/*  810 */           if (m > (1 << this.nBits[3]) - 1)
/*      */           {
/*  812 */             m = (1 << this.nBits[3]) - 1;
/*      */           }
/*      */         }
/*  815 */         arrayOfInt[0] = (m << this.maskOffsets[3]);
/*      */       }
/*  817 */       if (this.nBits[0] != n) {
/*  818 */         i = (int)(i * f * ((1 << this.nBits[0]) - 1) + 0.5F);
/*      */       }
/*  820 */       if (this.nBits[1] != n) {
/*  821 */         j = (int)(j * f * ((1 << this.nBits[1]) - 1) + 0.5F);
/*      */       }
/*  823 */       if (this.nBits[2] != n)
/*  824 */         k = (int)(k * f * ((1 << this.nBits[2]) - 1) + 0.5F);
/*      */     }
/*      */     else
/*      */     {
/*  828 */       localObject = new float[3];
/*  829 */       f = 0.003921569F;
/*  830 */       localObject[0] = (i * f);
/*  831 */       localObject[1] = (j * f);
/*  832 */       localObject[2] = (k * f);
/*  833 */       localObject = this.colorSpace.fromRGB((float[])localObject);
/*  834 */       if (this.supportsAlpha) {
/*  835 */         m = paramInt >> 24 & 0xFF;
/*  836 */         if (this.isAlphaPremultiplied) {
/*  837 */           f *= m;
/*  838 */           for (int i1 = 0; i1 < 3; i1++) {
/*  839 */             localObject[i1] *= f;
/*      */           }
/*      */         }
/*  842 */         if (this.nBits[3] != 8) {
/*  843 */           m = (int)(m * 0.003921569F * ((1 << this.nBits[3]) - 1) + 0.5F);
/*      */ 
/*  845 */           if (m > (1 << this.nBits[3]) - 1)
/*      */           {
/*  847 */             m = (1 << this.nBits[3]) - 1;
/*      */           }
/*      */         }
/*  850 */         arrayOfInt[0] = (m << this.maskOffsets[3]);
/*      */       }
/*  852 */       i = (int)(localObject[0] * ((1 << this.nBits[0]) - 1) + 0.5F);
/*  853 */       j = (int)(localObject[1] * ((1 << this.nBits[1]) - 1) + 0.5F);
/*  854 */       k = (int)(localObject[2] * ((1 << this.nBits[2]) - 1) + 0.5F);
/*      */     }
/*      */ 
/*  857 */     if (this.maxBits > 23)
/*      */     {
/*  862 */       if (i > (1 << this.nBits[0]) - 1) {
/*  863 */         i = (1 << this.nBits[0]) - 1;
/*      */       }
/*  865 */       if (j > (1 << this.nBits[1]) - 1) {
/*  866 */         j = (1 << this.nBits[1]) - 1;
/*      */       }
/*  868 */       if (k > (1 << this.nBits[2]) - 1) {
/*  869 */         k = (1 << this.nBits[2]) - 1;
/*      */       }
/*      */     }
/*      */ 
/*  873 */     arrayOfInt[0] |= i << this.maskOffsets[0] | j << this.maskOffsets[1] | k << this.maskOffsets[2];
/*      */ 
/*  877 */     switch (this.transferType)
/*      */     {
/*      */     case 0:
/*  880 */       if (paramObject == null)
/*  881 */         localObject = new byte[1];
/*      */       else {
/*  883 */         localObject = (byte[])paramObject;
/*      */       }
/*  885 */       localObject[0] = ((byte)(0xFF & arrayOfInt[0]));
/*  886 */       return localObject;
/*      */     case 1:
/*  890 */       if (paramObject == null)
/*  891 */         localObject = new short[1];
/*      */       else {
/*  893 */         localObject = (short[])paramObject;
/*      */       }
/*  895 */       localObject[0] = ((short)(arrayOfInt[0] & 0xFFFF));
/*  896 */       return localObject;
/*      */     case 3:
/*  899 */       return arrayOfInt;
/*      */     case 2:
/*  901 */     }throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public final int[] getComponents(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/*  927 */     if (paramArrayOfInt == null) {
/*  928 */       paramArrayOfInt = new int[paramInt2 + this.numComponents];
/*      */     }
/*      */ 
/*  931 */     for (int i = 0; i < this.numComponents; i++) {
/*  932 */       paramArrayOfInt[(paramInt2 + i)] = ((paramInt1 & this.maskArray[i]) >>> this.maskOffsets[i]);
/*      */     }
/*      */ 
/*  935 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public final int[] getComponents(Object paramObject, int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  979 */     int i = 0;
/*  980 */     switch (this.transferType) {
/*      */     case 0:
/*  982 */       byte[] arrayOfByte = (byte[])paramObject;
/*  983 */       i = arrayOfByte[0] & 0xFF;
/*  984 */       break;
/*      */     case 1:
/*  986 */       short[] arrayOfShort = (short[])paramObject;
/*  987 */       i = arrayOfShort[0] & 0xFFFF;
/*  988 */       break;
/*      */     case 3:
/*  990 */       int[] arrayOfInt = (int[])paramObject;
/*  991 */       i = arrayOfInt[0];
/*  992 */       break;
/*      */     case 2:
/*      */     default:
/*  994 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  997 */     return getComponents(i, paramArrayOfInt, paramInt);
/*      */   }
/*      */ 
/*      */   public final WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/* 1015 */     if ((paramInt1 <= 0) || (paramInt2 <= 0))
/* 1016 */       throw new IllegalArgumentException("Width (" + paramInt1 + ") and height (" + paramInt2 + ") cannot be <= 0");
/*      */     int[] arrayOfInt;
/* 1020 */     if (this.supportsAlpha) {
/* 1021 */       arrayOfInt = new int[4];
/* 1022 */       arrayOfInt[3] = this.alpha_mask;
/*      */     }
/*      */     else {
/* 1025 */       arrayOfInt = new int[3];
/*      */     }
/* 1027 */     arrayOfInt[0] = this.red_mask;
/* 1028 */     arrayOfInt[1] = this.green_mask;
/* 1029 */     arrayOfInt[2] = this.blue_mask;
/*      */ 
/* 1031 */     if (this.pixel_bits > 16) {
/* 1032 */       return Raster.createPackedRaster(3, paramInt1, paramInt2, arrayOfInt, null);
/*      */     }
/*      */ 
/* 1035 */     if (this.pixel_bits > 8) {
/* 1036 */       return Raster.createPackedRaster(1, paramInt1, paramInt2, arrayOfInt, null);
/*      */     }
/*      */ 
/* 1040 */     return Raster.createPackedRaster(0, paramInt1, paramInt2, arrayOfInt, null);
/*      */   }
/*      */ 
/*      */   public int getDataElement(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1064 */     int i = 0;
/* 1065 */     for (int j = 0; j < this.numComponents; j++) {
/* 1066 */       i |= paramArrayOfInt[(paramInt + j)] << this.maskOffsets[j] & this.maskArray[j];
/*      */     }
/* 1068 */     return i;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int[] paramArrayOfInt, int paramInt, Object paramObject)
/*      */   {
/* 1114 */     int i = 0;
/* 1115 */     for (int j = 0; j < this.numComponents; j++)
/* 1116 */       i |= paramArrayOfInt[(paramInt + j)] << this.maskOffsets[j] & this.maskArray[j];
/*      */     Object localObject;
/* 1118 */     switch (this.transferType) {
/*      */     case 0:
/* 1120 */       if ((paramObject instanceof byte[])) {
/* 1121 */         localObject = (byte[])paramObject;
/* 1122 */         localObject[0] = ((byte)(i & 0xFF));
/* 1123 */         return localObject;
/*      */       }
/* 1125 */       localObject = new byte[] { (byte)(i & 0xFF) };
/* 1126 */       return localObject;
/*      */     case 1:
/* 1129 */       if ((paramObject instanceof short[])) {
/* 1130 */         localObject = (short[])paramObject;
/* 1131 */         localObject[0] = ((short)(i & 0xFFFF));
/* 1132 */         return localObject;
/*      */       }
/* 1134 */       localObject = new short[] { (short)(i & 0xFFFF) };
/* 1135 */       return localObject;
/*      */     case 3:
/* 1138 */       if ((paramObject instanceof int[])) {
/* 1139 */         localObject = (int[])paramObject;
/* 1140 */         localObject[0] = i;
/* 1141 */         return localObject;
/*      */       }
/* 1143 */       localObject = new int[] { i };
/* 1144 */       return localObject;
/*      */     case 2:
/*      */     }
/* 1147 */     throw new ClassCastException("This method has not been implemented for transferType " + this.transferType);
/*      */   }
/*      */ 
/*      */   public final ColorModel coerceData(WritableRaster paramWritableRaster, boolean paramBoolean)
/*      */   {
/* 1179 */     if ((!this.supportsAlpha) || (isAlphaPremultiplied() == paramBoolean))
/*      */     {
/* 1181 */       return this;
/*      */     }
/*      */ 
/* 1184 */     int i = paramWritableRaster.getWidth();
/* 1185 */     int j = paramWritableRaster.getHeight();
/* 1186 */     int k = this.numColorComponents;
/*      */ 
/* 1188 */     float f2 = 1.0F / ((1 << this.nBits[k]) - 1);
/*      */ 
/* 1190 */     int m = paramWritableRaster.getMinX();
/* 1191 */     int n = paramWritableRaster.getMinY();
/*      */ 
/* 1193 */     int[] arrayOfInt1 = null;
/* 1194 */     int[] arrayOfInt2 = null;
/*      */     int i2;
/*      */     int i1;
/*      */     int i3;
/*      */     float f1;
/* 1196 */     if (paramBoolean)
/*      */     {
/*      */       int i4;
/* 1199 */       switch (this.transferType) {
/*      */       case 0:
/* 1201 */         for (i2 = 0; i2 < j; n++) {
/* 1202 */           i1 = m;
/* 1203 */           for (i3 = 0; i3 < i; i1++) {
/* 1204 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1205 */             f1 = arrayOfInt1[k] * f2;
/* 1206 */             if (f1 != 0.0F) {
/* 1207 */               for (i4 = 0; i4 < k; i4++) {
/* 1208 */                 arrayOfInt1[i4] = ((int)(arrayOfInt1[i4] * f1 + 0.5F));
/*      */               }
/*      */ 
/* 1211 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             } else {
/* 1213 */               if (arrayOfInt2 == null) {
/* 1214 */                 arrayOfInt2 = new int[this.numComponents];
/* 1215 */                 Arrays.fill(arrayOfInt2, 0);
/*      */               }
/* 1217 */               paramWritableRaster.setPixel(i1, n, arrayOfInt2);
/*      */             }
/* 1203 */             i3++;
/*      */           }
/* 1201 */           i2++;
/*      */         }
/*      */ 
/* 1222 */         break;
/*      */       case 1:
/* 1224 */         for (i2 = 0; i2 < j; n++) {
/* 1225 */           i1 = m;
/* 1226 */           for (i3 = 0; i3 < i; i1++) {
/* 1227 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1228 */             f1 = arrayOfInt1[k] * f2;
/* 1229 */             if (f1 != 0.0F) {
/* 1230 */               for (i4 = 0; i4 < k; i4++) {
/* 1231 */                 arrayOfInt1[i4] = ((int)(arrayOfInt1[i4] * f1 + 0.5F));
/*      */               }
/*      */ 
/* 1234 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             } else {
/* 1236 */               if (arrayOfInt2 == null) {
/* 1237 */                 arrayOfInt2 = new int[this.numComponents];
/* 1238 */                 Arrays.fill(arrayOfInt2, 0);
/*      */               }
/* 1240 */               paramWritableRaster.setPixel(i1, n, arrayOfInt2);
/*      */             }
/* 1226 */             i3++;
/*      */           }
/* 1224 */           i2++;
/*      */         }
/*      */ 
/* 1245 */         break;
/*      */       case 3:
/* 1247 */         for (i2 = 0; i2 < j; n++) {
/* 1248 */           i1 = m;
/* 1249 */           for (i3 = 0; i3 < i; i1++) {
/* 1250 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1251 */             f1 = arrayOfInt1[k] * f2;
/* 1252 */             if (f1 != 0.0F) {
/* 1253 */               for (i4 = 0; i4 < k; i4++) {
/* 1254 */                 arrayOfInt1[i4] = ((int)(arrayOfInt1[i4] * f1 + 0.5F));
/*      */               }
/*      */ 
/* 1257 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             } else {
/* 1259 */               if (arrayOfInt2 == null) {
/* 1260 */                 arrayOfInt2 = new int[this.numComponents];
/* 1261 */                 Arrays.fill(arrayOfInt2, 0);
/*      */               }
/* 1263 */               paramWritableRaster.setPixel(i1, n, arrayOfInt2);
/*      */             }
/* 1249 */             i3++;
/*      */           }
/* 1247 */           i2++;
/*      */         }
/*      */ 
/* 1268 */         break;
/*      */       case 2:
/*      */       default:
/* 1270 */         throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       float f3;
/*      */       int i5;
/* 1276 */       switch (this.transferType) {
/*      */       case 0:
/* 1278 */         for (i2 = 0; i2 < j; n++) {
/* 1279 */           i1 = m;
/* 1280 */           for (i3 = 0; i3 < i; i1++) {
/* 1281 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1282 */             f1 = arrayOfInt1[k] * f2;
/* 1283 */             if (f1 != 0.0F) {
/* 1284 */               f3 = 1.0F / f1;
/* 1285 */               for (i5 = 0; i5 < k; i5++) {
/* 1286 */                 arrayOfInt1[i5] = ((int)(arrayOfInt1[i5] * f3 + 0.5F));
/*      */               }
/*      */ 
/* 1289 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             }
/* 1280 */             i3++;
/*      */           }
/* 1278 */           i2++;
/*      */         }
/*      */ 
/* 1294 */         break;
/*      */       case 1:
/* 1296 */         for (i2 = 0; i2 < j; n++) {
/* 1297 */           i1 = m;
/* 1298 */           for (i3 = 0; i3 < i; i1++) {
/* 1299 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1300 */             f1 = arrayOfInt1[k] * f2;
/* 1301 */             if (f1 != 0.0F) {
/* 1302 */               f3 = 1.0F / f1;
/* 1303 */               for (i5 = 0; i5 < k; i5++) {
/* 1304 */                 arrayOfInt1[i5] = ((int)(arrayOfInt1[i5] * f3 + 0.5F));
/*      */               }
/*      */ 
/* 1307 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             }
/* 1298 */             i3++;
/*      */           }
/* 1296 */           i2++;
/*      */         }
/*      */ 
/* 1312 */         break;
/*      */       case 3:
/* 1314 */         for (i2 = 0; i2 < j; n++) {
/* 1315 */           i1 = m;
/* 1316 */           for (i3 = 0; i3 < i; i1++) {
/* 1317 */             arrayOfInt1 = paramWritableRaster.getPixel(i1, n, arrayOfInt1);
/* 1318 */             f1 = arrayOfInt1[k] * f2;
/* 1319 */             if (f1 != 0.0F) {
/* 1320 */               f3 = 1.0F / f1;
/* 1321 */               for (i5 = 0; i5 < k; i5++) {
/* 1322 */                 arrayOfInt1[i5] = ((int)(arrayOfInt1[i5] * f3 + 0.5F));
/*      */               }
/*      */ 
/* 1325 */               paramWritableRaster.setPixel(i1, n, arrayOfInt1);
/*      */             }
/* 1316 */             i3++;
/*      */           }
/* 1314 */           i2++;
/*      */         }
/*      */ 
/* 1330 */         break;
/*      */       case 2:
/*      */       default:
/* 1332 */         throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1338 */     return new DirectColorModel(this.colorSpace, this.pixel_bits, this.maskArray[0], this.maskArray[1], this.maskArray[2], this.maskArray[3], paramBoolean, this.transferType);
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleRaster(Raster paramRaster)
/*      */   {
/* 1354 */     SampleModel localSampleModel = paramRaster.getSampleModel();
/*      */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel;
/* 1356 */     if ((localSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 1357 */       localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)localSampleModel;
/*      */     }
/*      */     else {
/* 1360 */       return false;
/*      */     }
/* 1362 */     if (localSinglePixelPackedSampleModel.getNumBands() != getNumComponents()) {
/* 1363 */       return false;
/*      */     }
/*      */ 
/* 1366 */     int[] arrayOfInt = localSinglePixelPackedSampleModel.getBitMasks();
/* 1367 */     for (int i = 0; i < this.numComponents; i++) {
/* 1368 */       if (arrayOfInt[i] != this.maskArray[i]) {
/* 1369 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1373 */     return paramRaster.getTransferType() == this.transferType;
/*      */   }
/*      */ 
/*      */   private void setFields()
/*      */   {
/* 1379 */     this.red_mask = this.maskArray[0];
/* 1380 */     this.red_offset = this.maskOffsets[0];
/* 1381 */     this.green_mask = this.maskArray[1];
/* 1382 */     this.green_offset = this.maskOffsets[1];
/* 1383 */     this.blue_mask = this.maskArray[2];
/* 1384 */     this.blue_offset = this.maskOffsets[2];
/* 1385 */     if (this.nBits[0] < 8) {
/* 1386 */       this.red_scale = ((1 << this.nBits[0]) - 1);
/*      */     }
/* 1388 */     if (this.nBits[1] < 8) {
/* 1389 */       this.green_scale = ((1 << this.nBits[1]) - 1);
/*      */     }
/* 1391 */     if (this.nBits[2] < 8) {
/* 1392 */       this.blue_scale = ((1 << this.nBits[2]) - 1);
/*      */     }
/* 1394 */     if (this.supportsAlpha) {
/* 1395 */       this.alpha_mask = this.maskArray[3];
/* 1396 */       this.alpha_offset = this.maskOffsets[3];
/* 1397 */       if (this.nBits[3] < 8)
/* 1398 */         this.alpha_scale = ((1 << this.nBits[3]) - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1410 */     return new String("DirectColorModel: rmask=" + Integer.toHexString(this.red_mask) + " gmask=" + Integer.toHexString(this.green_mask) + " bmask=" + Integer.toHexString(this.blue_mask) + " amask=" + Integer.toHexString(this.alpha_mask));
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DirectColorModel
 * JD-Core Version:    0.6.2
 */