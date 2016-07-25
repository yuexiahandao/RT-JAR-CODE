/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.awt.image.ByteInterleavedRaster;
/*     */ import sun.awt.image.IntegerInterleavedRaster;
/*     */ import sun.awt.image.SunWritableRaster;
/*     */ 
/*     */ abstract class TexturePaintContext
/*     */   implements PaintContext
/*     */ {
/*  42 */   public static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, 65280, 255);
/*     */ 
/*  44 */   public static ColorModel argbmodel = ColorModel.getRGBdefault();
/*     */   ColorModel colorModel;
/*     */   int bWidth;
/*     */   int bHeight;
/*     */   int maxWidth;
/*     */   WritableRaster outRas;
/*     */   double xOrg;
/*     */   double yOrg;
/*     */   double incXAcross;
/*     */   double incYAcross;
/*     */   double incXDown;
/*     */   double incYDown;
/*     */   int colincx;
/*     */   int colincy;
/*     */   int colincxerr;
/*     */   int colincyerr;
/*     */   int rowincx;
/*     */   int rowincy;
/*     */   int rowincxerr;
/*     */   int rowincyerr;
/*     */   private static WeakReference xrgbRasRef;
/*     */   private static WeakReference argbRasRef;
/*     */   private static WeakReference byteRasRef;
/*     */ 
/*     */   public static PaintContext getContext(BufferedImage paramBufferedImage, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints, Rectangle paramRectangle)
/*     */   {
/*  73 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/*  74 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*  75 */     int i = paramRectangle.width;
/*  76 */     Object localObject1 = paramRenderingHints.get(RenderingHints.KEY_INTERPOLATION);
/*  77 */     boolean bool = paramRenderingHints.get(RenderingHints.KEY_RENDERING) == RenderingHints.VALUE_RENDER_QUALITY;
/*     */     Object localObject2;
/*  81 */     if (((localWritableRaster instanceof IntegerInterleavedRaster)) && ((!bool) || (isFilterableDCM(localColorModel))))
/*     */     {
/*  84 */       localObject2 = (IntegerInterleavedRaster)localWritableRaster;
/*  85 */       if ((((IntegerInterleavedRaster)localObject2).getNumDataElements() == 1) && (((IntegerInterleavedRaster)localObject2).getPixelStride() == 1))
/*  86 */         return new Int((IntegerInterleavedRaster)localObject2, localColorModel, paramAffineTransform, i, bool);
/*     */     }
/*  88 */     else if ((localWritableRaster instanceof ByteInterleavedRaster)) {
/*  89 */       localObject2 = (ByteInterleavedRaster)localWritableRaster;
/*  90 */       if ((((ByteInterleavedRaster)localObject2).getNumDataElements() == 1) && (((ByteInterleavedRaster)localObject2).getPixelStride() == 1)) {
/*  91 */         if (bool) {
/*  92 */           if (isFilterableICM(localColorModel))
/*  93 */             return new ByteFilter((ByteInterleavedRaster)localObject2, localColorModel, paramAffineTransform, i);
/*     */         }
/*     */         else {
/*  96 */           return new Byte((ByteInterleavedRaster)localObject2, localColorModel, paramAffineTransform, i);
/*     */         }
/*     */       }
/*     */     }
/* 100 */     return new Any(localWritableRaster, localColorModel, paramAffineTransform, i, bool);
/*     */   }
/*     */ 
/*     */   public static boolean isFilterableICM(ColorModel paramColorModel) {
/* 104 */     if ((paramColorModel instanceof IndexColorModel)) {
/* 105 */       IndexColorModel localIndexColorModel = (IndexColorModel)paramColorModel;
/* 106 */       if (localIndexColorModel.getMapSize() <= 256) {
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isFilterableDCM(ColorModel paramColorModel) {
/* 114 */     if ((paramColorModel instanceof DirectColorModel)) {
/* 115 */       DirectColorModel localDirectColorModel = (DirectColorModel)paramColorModel;
/* 116 */       return (isMaskOK(localDirectColorModel.getAlphaMask(), true)) && (isMaskOK(localDirectColorModel.getRedMask(), false)) && (isMaskOK(localDirectColorModel.getGreenMask(), false)) && (isMaskOK(localDirectColorModel.getBlueMask(), false));
/*     */     }
/*     */ 
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isMaskOK(int paramInt, boolean paramBoolean) {
/* 125 */     if ((paramBoolean) && (paramInt == 0)) {
/* 126 */       return true;
/*     */     }
/* 128 */     return (paramInt == 255) || (paramInt == 65280) || (paramInt == 16711680) || (paramInt == -16777216);
/*     */   }
/*     */ 
/*     */   public static ColorModel getInternedColorModel(ColorModel paramColorModel)
/*     */   {
/* 135 */     if ((xrgbmodel == paramColorModel) || (xrgbmodel.equals(paramColorModel))) {
/* 136 */       return xrgbmodel;
/*     */     }
/* 138 */     if ((argbmodel == paramColorModel) || (argbmodel.equals(paramColorModel))) {
/* 139 */       return argbmodel;
/*     */     }
/* 141 */     return paramColorModel;
/*     */   }
/*     */ 
/*     */   TexturePaintContext(ColorModel paramColorModel, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 146 */     this.colorModel = getInternedColorModel(paramColorModel);
/* 147 */     this.bWidth = paramInt1;
/* 148 */     this.bHeight = paramInt2;
/* 149 */     this.maxWidth = paramInt3;
/*     */     try
/*     */     {
/* 152 */       paramAffineTransform = paramAffineTransform.createInverse();
/*     */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 154 */       paramAffineTransform.setToScale(0.0D, 0.0D);
/*     */     }
/* 156 */     this.incXAcross = mod(paramAffineTransform.getScaleX(), paramInt1);
/* 157 */     this.incYAcross = mod(paramAffineTransform.getShearY(), paramInt2);
/* 158 */     this.incXDown = mod(paramAffineTransform.getShearX(), paramInt1);
/* 159 */     this.incYDown = mod(paramAffineTransform.getScaleY(), paramInt2);
/* 160 */     this.xOrg = paramAffineTransform.getTranslateX();
/* 161 */     this.yOrg = paramAffineTransform.getTranslateY();
/* 162 */     this.colincx = ((int)this.incXAcross);
/* 163 */     this.colincy = ((int)this.incYAcross);
/* 164 */     this.colincxerr = fractAsInt(this.incXAcross);
/* 165 */     this.colincyerr = fractAsInt(this.incYAcross);
/* 166 */     this.rowincx = ((int)this.incXDown);
/* 167 */     this.rowincy = ((int)this.incYDown);
/* 168 */     this.rowincxerr = fractAsInt(this.incXDown);
/* 169 */     this.rowincyerr = fractAsInt(this.incYDown);
/*     */   }
/*     */ 
/*     */   static int fractAsInt(double paramDouble)
/*     */   {
/* 174 */     return (int)(paramDouble % 1.0D * 2147483647.0D);
/*     */   }
/*     */ 
/*     */   static double mod(double paramDouble1, double paramDouble2) {
/* 178 */     paramDouble1 %= paramDouble2;
/* 179 */     if (paramDouble1 < 0.0D) {
/* 180 */       paramDouble1 += paramDouble2;
/* 181 */       if (paramDouble1 >= paramDouble2)
/*     */       {
/* 188 */         paramDouble1 = 0.0D;
/*     */       }
/*     */     }
/* 191 */     return paramDouble1;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 198 */     dropRaster(this.colorModel, this.outRas);
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/* 205 */     return this.colorModel;
/*     */   }
/*     */ 
/*     */   public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 215 */     if ((this.outRas == null) || (this.outRas.getWidth() < paramInt3) || (this.outRas.getHeight() < paramInt4))
/*     */     {
/* 220 */       this.outRas = makeRaster(paramInt4 == 1 ? Math.max(paramInt3, this.maxWidth) : paramInt3, paramInt4);
/*     */     }
/* 222 */     double d1 = mod(this.xOrg + paramInt1 * this.incXAcross + paramInt2 * this.incXDown, this.bWidth);
/* 223 */     double d2 = mod(this.yOrg + paramInt1 * this.incYAcross + paramInt2 * this.incYDown, this.bHeight);
/*     */ 
/* 225 */     setRaster((int)d1, (int)d2, fractAsInt(d1), fractAsInt(d2), paramInt3, paramInt4, this.bWidth, this.bHeight, this.colincx, this.colincxerr, this.colincy, this.colincyerr, this.rowincx, this.rowincxerr, this.rowincy, this.rowincyerr);
/*     */ 
/* 232 */     SunWritableRaster.markDirty(this.outRas);
/*     */ 
/* 234 */     return this.outRas;
/*     */   }
/*     */ 
/*     */   static synchronized WritableRaster makeRaster(ColorModel paramColorModel, Raster paramRaster, int paramInt1, int paramInt2)
/*     */   {
/*     */     WritableRaster localWritableRaster;
/* 244 */     if (xrgbmodel == paramColorModel) {
/* 245 */       if (xrgbRasRef != null) {
/* 246 */         localWritableRaster = (WritableRaster)xrgbRasRef.get();
/* 247 */         if ((localWritableRaster != null) && (localWritableRaster.getWidth() >= paramInt1) && (localWritableRaster.getHeight() >= paramInt2)) {
/* 248 */           xrgbRasRef = null;
/* 249 */           return localWritableRaster;
/*     */         }
/*     */       }
/*     */ 
/* 253 */       if ((paramInt1 <= 32) && (paramInt2 <= 32))
/* 254 */         paramInt1 = paramInt2 = 32;
/*     */     }
/* 256 */     else if (argbmodel == paramColorModel) {
/* 257 */       if (argbRasRef != null) {
/* 258 */         localWritableRaster = (WritableRaster)argbRasRef.get();
/* 259 */         if ((localWritableRaster != null) && (localWritableRaster.getWidth() >= paramInt1) && (localWritableRaster.getHeight() >= paramInt2)) {
/* 260 */           argbRasRef = null;
/* 261 */           return localWritableRaster;
/*     */         }
/*     */       }
/*     */ 
/* 265 */       if ((paramInt1 <= 32) && (paramInt2 <= 32)) {
/* 266 */         paramInt1 = paramInt2 = 32;
/*     */       }
/*     */     }
/* 269 */     if (paramRaster != null) {
/* 270 */       return paramRaster.createCompatibleWritableRaster(paramInt1, paramInt2);
/*     */     }
/* 272 */     return paramColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static synchronized void dropRaster(ColorModel paramColorModel, Raster paramRaster)
/*     */   {
/* 277 */     if (paramRaster == null) {
/* 278 */       return;
/*     */     }
/* 280 */     if (xrgbmodel == paramColorModel)
/* 281 */       xrgbRasRef = new WeakReference(paramRaster);
/* 282 */     else if (argbmodel == paramColorModel)
/* 283 */       argbRasRef = new WeakReference(paramRaster);
/*     */   }
/*     */ 
/*     */   static synchronized WritableRaster makeByteRaster(Raster paramRaster, int paramInt1, int paramInt2)
/*     */   {
/* 292 */     if (byteRasRef != null) {
/* 293 */       WritableRaster localWritableRaster = (WritableRaster)byteRasRef.get();
/* 294 */       if ((localWritableRaster != null) && (localWritableRaster.getWidth() >= paramInt1) && (localWritableRaster.getHeight() >= paramInt2)) {
/* 295 */         byteRasRef = null;
/* 296 */         return localWritableRaster;
/*     */       }
/*     */     }
/*     */ 
/* 300 */     if ((paramInt1 <= 32) && (paramInt2 <= 32)) {
/* 301 */       paramInt1 = paramInt2 = 32;
/*     */     }
/* 303 */     return paramRaster.createCompatibleWritableRaster(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static synchronized void dropByteRaster(Raster paramRaster) {
/* 307 */     if (paramRaster == null) {
/* 308 */       return;
/*     */     }
/* 310 */     byteRasRef = new WeakReference(paramRaster);
/*     */   }
/*     */ 
/*     */   public abstract WritableRaster makeRaster(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void setRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16);
/*     */ 
/*     */   public static int blend(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 341 */     paramInt1 >>>= 19;
/* 342 */     paramInt2 >>>= 19;
/*     */     int m;
/*     */     int k;
/*     */     int j;
/* 344 */     int i = j = k = m = 0;
/* 345 */     for (int n = 0; n < 4; n++) {
/* 346 */       int i1 = paramArrayOfInt[n];
/*     */ 
/* 350 */       paramInt1 = 4096 - paramInt1;
/* 351 */       if ((n & 0x1) == 0) {
/* 352 */         paramInt2 = 4096 - paramInt2;
/*     */       }
/*     */ 
/* 356 */       int i2 = paramInt1 * paramInt2;
/* 357 */       if (i2 != 0)
/*     */       {
/* 361 */         i += (i1 >>> 24) * i2;
/* 362 */         j += (i1 >>> 16 & 0xFF) * i2;
/* 363 */         k += (i1 >>> 8 & 0xFF) * i2;
/* 364 */         m += (i1 & 0xFF) * i2;
/*     */       }
/*     */     }
/* 367 */     return i + 8388608 >>> 24 << 24 | j + 8388608 >>> 24 << 16 | k + 8388608 >>> 24 << 8 | m + 8388608 >>> 24;
/*     */   }
/*     */ 
/*     */   static class Any extends TexturePaintContext
/*     */   {
/*     */     WritableRaster srcRas;
/*     */     boolean filter;
/*     */ 
/*     */     public Any(WritableRaster paramWritableRaster, ColorModel paramColorModel, AffineTransform paramAffineTransform, int paramInt, boolean paramBoolean)
/*     */     {
/* 745 */       super(paramAffineTransform, paramWritableRaster.getWidth(), paramWritableRaster.getHeight(), paramInt);
/* 746 */       this.srcRas = paramWritableRaster;
/* 747 */       this.filter = paramBoolean;
/*     */     }
/*     */ 
/*     */     public WritableRaster makeRaster(int paramInt1, int paramInt2) {
/* 751 */       return makeRaster(this.colorModel, this.srcRas, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void setRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
/*     */     {
/* 760 */       Object localObject = null;
/* 761 */       int i = paramInt1;
/* 762 */       int j = paramInt2;
/* 763 */       int k = paramInt3;
/* 764 */       int m = paramInt4;
/* 765 */       WritableRaster localWritableRaster1 = this.srcRas;
/* 766 */       WritableRaster localWritableRaster2 = this.outRas;
/* 767 */       int[] arrayOfInt = this.filter ? new int[4] : null;
/* 768 */       for (int n = 0; n < paramInt6; n++) {
/* 769 */         paramInt1 = i;
/* 770 */         paramInt2 = j;
/* 771 */         paramInt3 = k;
/* 772 */         paramInt4 = m;
/* 773 */         for (int i1 = 0; i1 < paramInt5; i1++) {
/* 774 */           localObject = localWritableRaster1.getDataElements(paramInt1, paramInt2, localObject);
/* 775 */           if (this.filter)
/*     */           {
/*     */             int i2;
/* 777 */             if ((i2 = paramInt1 + 1) >= paramInt7)
/* 778 */               i2 = 0;
/*     */             int i3;
/* 780 */             if ((i3 = paramInt2 + 1) >= paramInt8) {
/* 781 */               i3 = 0;
/*     */             }
/* 783 */             arrayOfInt[0] = this.colorModel.getRGB(localObject);
/* 784 */             localObject = localWritableRaster1.getDataElements(i2, paramInt2, localObject);
/* 785 */             arrayOfInt[1] = this.colorModel.getRGB(localObject);
/* 786 */             localObject = localWritableRaster1.getDataElements(paramInt1, i3, localObject);
/* 787 */             arrayOfInt[2] = this.colorModel.getRGB(localObject);
/* 788 */             localObject = localWritableRaster1.getDataElements(i2, i3, localObject);
/* 789 */             arrayOfInt[3] = this.colorModel.getRGB(localObject);
/* 790 */             int i4 = TexturePaintContext.blend(arrayOfInt, paramInt3, paramInt4);
/*     */ 
/* 792 */             localObject = this.colorModel.getDataElements(i4, localObject);
/*     */           }
/* 794 */           localWritableRaster2.setDataElements(i1, n, localObject);
/* 795 */           if (paramInt3 += paramInt10 < 0) {
/* 796 */             paramInt3 &= 2147483647;
/* 797 */             paramInt1++;
/*     */           }
/* 799 */           if (paramInt1 += paramInt9 >= paramInt7) {
/* 800 */             paramInt1 -= paramInt7;
/*     */           }
/* 802 */           if (paramInt4 += paramInt12 < 0) {
/* 803 */             paramInt4 &= 2147483647;
/* 804 */             paramInt2++;
/*     */           }
/* 806 */           if (paramInt2 += paramInt11 >= paramInt8) {
/* 807 */             paramInt2 -= paramInt8;
/*     */           }
/*     */         }
/* 810 */         if (k += paramInt14 < 0) {
/* 811 */           k &= 2147483647;
/* 812 */           i++;
/*     */         }
/* 814 */         if (i += paramInt13 >= paramInt7) {
/* 815 */           i -= paramInt7;
/*     */         }
/* 817 */         if (m += paramInt16 < 0) {
/* 818 */           m &= 2147483647;
/* 819 */           j++;
/*     */         }
/* 821 */         if (j += paramInt15 >= paramInt8)
/* 822 */           j -= paramInt8;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Byte extends TexturePaintContext
/*     */   {
/*     */     ByteInterleavedRaster srcRas;
/*     */     byte[] inData;
/*     */     int inOff;
/*     */     int inSpan;
/*     */     byte[] outData;
/*     */     int outOff;
/*     */     int outSpan;
/*     */ 
/*     */     public Byte(ByteInterleavedRaster paramByteInterleavedRaster, ColorModel paramColorModel, AffineTransform paramAffineTransform, int paramInt)
/*     */     {
/* 521 */       super(paramAffineTransform, paramByteInterleavedRaster.getWidth(), paramByteInterleavedRaster.getHeight(), paramInt);
/* 522 */       this.srcRas = paramByteInterleavedRaster;
/* 523 */       this.inData = paramByteInterleavedRaster.getDataStorage();
/* 524 */       this.inSpan = paramByteInterleavedRaster.getScanlineStride();
/* 525 */       this.inOff = paramByteInterleavedRaster.getDataOffset(0);
/*     */     }
/*     */ 
/*     */     public WritableRaster makeRaster(int paramInt1, int paramInt2) {
/* 529 */       WritableRaster localWritableRaster = makeByteRaster(this.srcRas, paramInt1, paramInt2);
/* 530 */       ByteInterleavedRaster localByteInterleavedRaster = (ByteInterleavedRaster)localWritableRaster;
/* 531 */       this.outData = localByteInterleavedRaster.getDataStorage();
/* 532 */       this.outSpan = localByteInterleavedRaster.getScanlineStride();
/* 533 */       this.outOff = localByteInterleavedRaster.getDataOffset(0);
/* 534 */       return localWritableRaster;
/*     */     }
/*     */ 
/*     */     public void dispose() {
/* 538 */       dropByteRaster(this.outRas);
/*     */     }
/*     */ 
/*     */     public void setRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
/*     */     {
/* 547 */       byte[] arrayOfByte1 = this.inData;
/* 548 */       byte[] arrayOfByte2 = this.outData;
/* 549 */       int i = this.outOff;
/* 550 */       int j = this.inSpan;
/* 551 */       int k = this.inOff;
/* 552 */       int m = this.outSpan;
/* 553 */       int n = (paramInt9 == 1) && (paramInt10 == 0) && (paramInt11 == 0) && (paramInt12 == 0) ? 1 : 0;
/*     */ 
/* 555 */       int i1 = paramInt1;
/* 556 */       int i2 = paramInt2;
/* 557 */       int i3 = paramInt3;
/* 558 */       int i4 = paramInt4;
/* 559 */       if (n != 0) {
/* 560 */         m -= paramInt5;
/*     */       }
/* 562 */       for (int i5 = 0; i5 < paramInt6; i5++)
/*     */       {
/*     */         int i6;
/* 563 */         if (n != 0) {
/* 564 */           i6 = k + i2 * j + paramInt7;
/* 565 */           paramInt1 = paramInt7 - i1;
/* 566 */           i += paramInt5;
/*     */           int i7;
/* 567 */           if (paramInt7 >= 32) {
/* 568 */             i7 = paramInt5;
/* 569 */             while (i7 > 0) {
/* 570 */               int i8 = i7 < paramInt1 ? i7 : paramInt1;
/* 571 */               System.arraycopy(arrayOfByte1, i6 - paramInt1, arrayOfByte2, i - i7, i8);
/*     */ 
/* 574 */               i7 -= i8;
/* 575 */               if (paramInt1 -= i8 == 0)
/* 576 */                 paramInt1 = paramInt7;
/*     */             }
/*     */           }
/*     */           else {
/* 580 */             for (i7 = paramInt5; i7 > 0; i7--) {
/* 581 */               arrayOfByte2[(i - i7)] = arrayOfByte1[(i6 - paramInt1)];
/* 582 */               paramInt1--; if (paramInt1 == 0)
/* 583 */                 paramInt1 = paramInt7;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 588 */           paramInt1 = i1;
/* 589 */           paramInt2 = i2;
/* 590 */           paramInt3 = i3;
/* 591 */           paramInt4 = i4;
/* 592 */           for (i6 = 0; i6 < paramInt5; i6++) {
/* 593 */             arrayOfByte2[(i + i6)] = arrayOfByte1[(k + paramInt2 * j + paramInt1)];
/* 594 */             if (paramInt3 += paramInt10 < 0) {
/* 595 */               paramInt3 &= 2147483647;
/* 596 */               paramInt1++;
/*     */             }
/* 598 */             if (paramInt1 += paramInt9 >= paramInt7) {
/* 599 */               paramInt1 -= paramInt7;
/*     */             }
/* 601 */             if (paramInt4 += paramInt12 < 0) {
/* 602 */               paramInt4 &= 2147483647;
/* 603 */               paramInt2++;
/*     */             }
/* 605 */             if (paramInt2 += paramInt11 >= paramInt8) {
/* 606 */               paramInt2 -= paramInt8;
/*     */             }
/*     */           }
/*     */         }
/* 610 */         if (i3 += paramInt14 < 0) {
/* 611 */           i3 &= 2147483647;
/* 612 */           i1++;
/*     */         }
/* 614 */         if (i1 += paramInt13 >= paramInt7) {
/* 615 */           i1 -= paramInt7;
/*     */         }
/* 617 */         if (i4 += paramInt16 < 0) {
/* 618 */           i4 &= 2147483647;
/* 619 */           i2++;
/*     */         }
/* 621 */         if (i2 += paramInt15 >= paramInt8) {
/* 622 */           i2 -= paramInt8;
/*     */         }
/* 624 */         i += m; }  } 
/*     */   }
/*     */   static class ByteFilter extends TexturePaintContext { ByteInterleavedRaster srcRas;
/*     */     int[] inPalette;
/*     */     byte[] inData;
/*     */     int inOff;
/*     */     int inSpan;
/*     */     int[] outData;
/*     */     int outOff;
/*     */     int outSpan;
/*     */ 
/* 642 */     public ByteFilter(ByteInterleavedRaster paramByteInterleavedRaster, ColorModel paramColorModel, AffineTransform paramAffineTransform, int paramInt) { super(paramAffineTransform, paramByteInterleavedRaster.getWidth(), paramByteInterleavedRaster.getHeight(), paramInt);
/*     */ 
/* 645 */       this.inPalette = new int[256];
/* 646 */       ((IndexColorModel)paramColorModel).getRGBs(this.inPalette);
/* 647 */       this.srcRas = paramByteInterleavedRaster;
/* 648 */       this.inData = paramByteInterleavedRaster.getDataStorage();
/* 649 */       this.inSpan = paramByteInterleavedRaster.getScanlineStride();
/* 650 */       this.inOff = paramByteInterleavedRaster.getDataOffset(0);
/*     */     }
/*     */ 
/*     */     public WritableRaster makeRaster(int paramInt1, int paramInt2)
/*     */     {
/* 656 */       WritableRaster localWritableRaster = makeRaster(this.colorModel, null, paramInt1, paramInt2);
/* 657 */       IntegerInterleavedRaster localIntegerInterleavedRaster = (IntegerInterleavedRaster)localWritableRaster;
/* 658 */       this.outData = localIntegerInterleavedRaster.getDataStorage();
/* 659 */       this.outSpan = localIntegerInterleavedRaster.getScanlineStride();
/* 660 */       this.outOff = localIntegerInterleavedRaster.getDataOffset(0);
/* 661 */       return localWritableRaster;
/*     */     }
/*     */ 
/*     */     public void setRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
/*     */     {
/* 670 */       byte[] arrayOfByte = this.inData;
/* 671 */       int[] arrayOfInt1 = this.outData;
/* 672 */       int i = this.outOff;
/* 673 */       int j = this.inSpan;
/* 674 */       int k = this.inOff;
/* 675 */       int m = this.outSpan;
/* 676 */       int n = paramInt1;
/* 677 */       int i1 = paramInt2;
/* 678 */       int i2 = paramInt3;
/* 679 */       int i3 = paramInt4;
/* 680 */       int[] arrayOfInt2 = new int[4];
/* 681 */       for (int i4 = 0; i4 < paramInt6; i4++) {
/* 682 */         paramInt1 = n;
/* 683 */         paramInt2 = i1;
/* 684 */         paramInt3 = i2;
/* 685 */         paramInt4 = i3;
/* 686 */         for (int i5 = 0; i5 < paramInt5; i5++)
/*     */         {
/*     */           int i6;
/* 688 */           if ((i6 = paramInt1 + 1) >= paramInt7)
/* 689 */             i6 = 0;
/*     */           int i7;
/* 691 */           if ((i7 = paramInt2 + 1) >= paramInt8) {
/* 692 */             i7 = 0;
/*     */           }
/* 694 */           arrayOfInt2[0] = this.inPalette[(0xFF & arrayOfByte[(k + paramInt1 + j * paramInt2)])];
/*     */ 
/* 696 */           arrayOfInt2[1] = this.inPalette[(0xFF & arrayOfByte[(k + i6 + j * paramInt2)])];
/*     */ 
/* 698 */           arrayOfInt2[2] = this.inPalette[(0xFF & arrayOfByte[(k + paramInt1 + j * i7)])];
/*     */ 
/* 700 */           arrayOfInt2[3] = this.inPalette[(0xFF & arrayOfByte[(k + i6 + j * i7)])];
/*     */ 
/* 702 */           arrayOfInt1[(i + i5)] = TexturePaintContext.blend(arrayOfInt2, paramInt3, paramInt4);
/*     */ 
/* 704 */           if (paramInt3 += paramInt10 < 0) {
/* 705 */             paramInt3 &= 2147483647;
/* 706 */             paramInt1++;
/*     */           }
/* 708 */           if (paramInt1 += paramInt9 >= paramInt7) {
/* 709 */             paramInt1 -= paramInt7;
/*     */           }
/* 711 */           if (paramInt4 += paramInt12 < 0) {
/* 712 */             paramInt4 &= 2147483647;
/* 713 */             paramInt2++;
/*     */           }
/* 715 */           if (paramInt2 += paramInt11 >= paramInt8) {
/* 716 */             paramInt2 -= paramInt8;
/*     */           }
/*     */         }
/* 719 */         if (i2 += paramInt14 < 0) {
/* 720 */           i2 &= 2147483647;
/* 721 */           n++;
/*     */         }
/* 723 */         if (n += paramInt13 >= paramInt7) {
/* 724 */           n -= paramInt7;
/*     */         }
/* 726 */         if (i3 += paramInt16 < 0) {
/* 727 */           i3 &= 2147483647;
/* 728 */           i1++;
/*     */         }
/* 730 */         if (i1 += paramInt15 >= paramInt8) {
/* 731 */           i1 -= paramInt8;
/*     */         }
/* 733 */         i += m;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Int extends TexturePaintContext
/*     */   {
/*     */     IntegerInterleavedRaster srcRas;
/*     */     int[] inData;
/*     */     int inOff;
/*     */     int inSpan;
/*     */     int[] outData;
/*     */     int outOff;
/*     */     int outSpan;
/*     */     boolean filter;
/*     */ 
/*     */     public Int(IntegerInterleavedRaster paramIntegerInterleavedRaster, ColorModel paramColorModel, AffineTransform paramAffineTransform, int paramInt, boolean paramBoolean)
/*     */     {
/* 386 */       super(paramAffineTransform, paramIntegerInterleavedRaster.getWidth(), paramIntegerInterleavedRaster.getHeight(), paramInt);
/* 387 */       this.srcRas = paramIntegerInterleavedRaster;
/* 388 */       this.inData = paramIntegerInterleavedRaster.getDataStorage();
/* 389 */       this.inSpan = paramIntegerInterleavedRaster.getScanlineStride();
/* 390 */       this.inOff = paramIntegerInterleavedRaster.getDataOffset(0);
/* 391 */       this.filter = paramBoolean;
/*     */     }
/*     */ 
/*     */     public WritableRaster makeRaster(int paramInt1, int paramInt2) {
/* 395 */       WritableRaster localWritableRaster = makeRaster(this.colorModel, this.srcRas, paramInt1, paramInt2);
/* 396 */       IntegerInterleavedRaster localIntegerInterleavedRaster = (IntegerInterleavedRaster)localWritableRaster;
/* 397 */       this.outData = localIntegerInterleavedRaster.getDataStorage();
/* 398 */       this.outSpan = localIntegerInterleavedRaster.getScanlineStride();
/* 399 */       this.outOff = localIntegerInterleavedRaster.getDataOffset(0);
/* 400 */       return localWritableRaster;
/*     */     }
/*     */ 
/*     */     public void setRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16)
/*     */     {
/* 409 */       int[] arrayOfInt1 = this.inData;
/* 410 */       int[] arrayOfInt2 = this.outData;
/* 411 */       int i = this.outOff;
/* 412 */       int j = this.inSpan;
/* 413 */       int k = this.inOff;
/* 414 */       int m = this.outSpan;
/* 415 */       boolean bool = this.filter;
/* 416 */       int n = (paramInt9 == 1) && (paramInt10 == 0) && (paramInt11 == 0) && (paramInt12 == 0) && (!bool) ? 1 : 0;
/*     */ 
/* 418 */       int i1 = paramInt1;
/* 419 */       int i2 = paramInt2;
/* 420 */       int i3 = paramInt3;
/* 421 */       int i4 = paramInt4;
/* 422 */       if (n != 0) {
/* 423 */         m -= paramInt5;
/*     */       }
/* 425 */       int[] arrayOfInt3 = bool ? new int[4] : null;
/* 426 */       for (int i5 = 0; i5 < paramInt6; i5++)
/*     */       {
/*     */         int i6;
/*     */         int i7;
/*     */         int i8;
/* 427 */         if (n != 0) {
/* 428 */           i6 = k + i2 * j + paramInt7;
/* 429 */           paramInt1 = paramInt7 - i1;
/* 430 */           i += paramInt5;
/* 431 */           if (paramInt7 >= 32) {
/* 432 */             i7 = paramInt5;
/* 433 */             while (i7 > 0) {
/* 434 */               i8 = i7 < paramInt1 ? i7 : paramInt1;
/* 435 */               System.arraycopy(arrayOfInt1, i6 - paramInt1, arrayOfInt2, i - i7, i8);
/*     */ 
/* 438 */               i7 -= i8;
/* 439 */               if (paramInt1 -= i8 == 0)
/* 440 */                 paramInt1 = paramInt7;
/*     */             }
/*     */           }
/*     */           else {
/* 444 */             for (i7 = paramInt5; i7 > 0; i7--) {
/* 445 */               arrayOfInt2[(i - i7)] = arrayOfInt1[(i6 - paramInt1)];
/* 446 */               paramInt1--; if (paramInt1 == 0)
/* 447 */                 paramInt1 = paramInt7;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 452 */           paramInt1 = i1;
/* 453 */           paramInt2 = i2;
/* 454 */           paramInt3 = i3;
/* 455 */           paramInt4 = i4;
/* 456 */           for (i6 = 0; i6 < paramInt5; i6++) {
/* 457 */             if (bool)
/*     */             {
/* 459 */               if ((i7 = paramInt1 + 1) >= paramInt7) {
/* 460 */                 i7 = 0;
/*     */               }
/* 462 */               if ((i8 = paramInt2 + 1) >= paramInt8) {
/* 463 */                 i8 = 0;
/*     */               }
/* 465 */               arrayOfInt3[0] = arrayOfInt1[(k + paramInt2 * j + paramInt1)];
/* 466 */               arrayOfInt3[1] = arrayOfInt1[(k + paramInt2 * j + i7)];
/* 467 */               arrayOfInt3[2] = arrayOfInt1[(k + i8 * j + paramInt1)];
/* 468 */               arrayOfInt3[3] = arrayOfInt1[(k + i8 * j + i7)];
/* 469 */               arrayOfInt2[(i + i6)] = TexturePaintContext.blend(arrayOfInt3, paramInt3, paramInt4);
/*     */             }
/*     */             else {
/* 472 */               arrayOfInt2[(i + i6)] = arrayOfInt1[(k + paramInt2 * j + paramInt1)];
/*     */             }
/* 474 */             if (paramInt3 += paramInt10 < 0) {
/* 475 */               paramInt3 &= 2147483647;
/* 476 */               paramInt1++;
/*     */             }
/* 478 */             if (paramInt1 += paramInt9 >= paramInt7) {
/* 479 */               paramInt1 -= paramInt7;
/*     */             }
/* 481 */             if (paramInt4 += paramInt12 < 0) {
/* 482 */               paramInt4 &= 2147483647;
/* 483 */               paramInt2++;
/*     */             }
/* 485 */             if (paramInt2 += paramInt11 >= paramInt8) {
/* 486 */               paramInt2 -= paramInt8;
/*     */             }
/*     */           }
/*     */         }
/* 490 */         if (i3 += paramInt14 < 0) {
/* 491 */           i3 &= 2147483647;
/* 492 */           i1++;
/*     */         }
/* 494 */         if (i1 += paramInt13 >= paramInt7) {
/* 495 */           i1 -= paramInt7;
/*     */         }
/* 497 */         if (i4 += paramInt16 < 0) {
/* 498 */           i4 &= 2147483647;
/* 499 */           i2++;
/*     */         }
/* 501 */         if (i2 += paramInt15 >= paramInt8) {
/* 502 */           i2 -= paramInt8;
/*     */         }
/* 504 */         i += m;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.TexturePaintContext
 * JD-Core Version:    0.6.2
 */