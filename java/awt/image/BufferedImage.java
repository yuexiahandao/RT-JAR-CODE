/*      */ package java.awt.image;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.Image;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Transparency;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import sun.awt.image.ByteComponentRaster;
/*      */ import sun.awt.image.BytePackedRaster;
/*      */ import sun.awt.image.IntegerComponentRaster;
/*      */ import sun.awt.image.OffScreenImageSource;
/*      */ import sun.awt.image.ShortComponentRaster;
/*      */ 
/*      */ public class BufferedImage extends Image
/*      */   implements WritableRenderedImage, Transparency
/*      */ {
/*   75 */   int imageType = 0;
/*      */   ColorModel colorModel;
/*      */   WritableRaster raster;
/*      */   OffScreenImageSource osis;
/*      */   Hashtable properties;
/*      */   boolean isAlphaPremultiplied;
/*      */   public static final int TYPE_CUSTOM = 0;
/*      */   public static final int TYPE_INT_RGB = 1;
/*      */   public static final int TYPE_INT_ARGB = 2;
/*      */   public static final int TYPE_INT_ARGB_PRE = 3;
/*      */   public static final int TYPE_INT_BGR = 4;
/*      */   public static final int TYPE_3BYTE_BGR = 5;
/*      */   public static final int TYPE_4BYTE_ABGR = 6;
/*      */   public static final int TYPE_4BYTE_ABGR_PRE = 7;
/*      */   public static final int TYPE_USHORT_565_RGB = 8;
/*      */   public static final int TYPE_USHORT_555_RGB = 9;
/*      */   public static final int TYPE_BYTE_GRAY = 10;
/*      */   public static final int TYPE_USHORT_GRAY = 11;
/*      */   public static final int TYPE_BYTE_BINARY = 12;
/*      */   public static final int TYPE_BYTE_INDEXED = 13;
/*      */   private static final int DCM_RED_MASK = 16711680;
/*      */   private static final int DCM_GREEN_MASK = 65280;
/*      */   private static final int DCM_BLUE_MASK = 255;
/*      */   private static final int DCM_ALPHA_MASK = -16777216;
/*      */   private static final int DCM_565_RED_MASK = 63488;
/*      */   private static final int DCM_565_GRN_MASK = 2016;
/*      */   private static final int DCM_565_BLU_MASK = 31;
/*      */   private static final int DCM_555_RED_MASK = 31744;
/*      */   private static final int DCM_555_GRN_MASK = 992;
/*      */   private static final int DCM_555_BLU_MASK = 31;
/*      */   private static final int DCM_BGR_RED_MASK = 255;
/*      */   private static final int DCM_BGR_GRN_MASK = 65280;
/*      */   private static final int DCM_BGR_BLU_MASK = 16711680;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public BufferedImage(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*      */     Object localObject;
/*      */     int[] arrayOfInt1;
/*      */     int[] arrayOfInt2;
/*  322 */     switch (paramInt3)
/*      */     {
/*      */     case 1:
/*  325 */       this.colorModel = new DirectColorModel(24, 16711680, 65280, 255, 0);
/*      */ 
/*  331 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  334 */       break;
/*      */     case 2:
/*  338 */       this.colorModel = ColorModel.getRGBdefault();
/*      */ 
/*  340 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  343 */       break;
/*      */     case 3:
/*  347 */       this.colorModel = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, 65280, 255, -16777216, true, 3);
/*      */ 
/*  359 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  362 */       break;
/*      */     case 4:
/*  366 */       this.colorModel = new DirectColorModel(24, 255, 65280, 16711680);
/*      */ 
/*  371 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  374 */       break;
/*      */     case 5:
/*  378 */       localObject = ColorSpace.getInstance(1000);
/*  379 */       arrayOfInt1 = new int[] { 8, 8, 8 };
/*  380 */       arrayOfInt2 = new int[] { 2, 1, 0 };
/*  381 */       this.colorModel = new ComponentColorModel((ColorSpace)localObject, arrayOfInt1, false, false, 1, 0);
/*      */ 
/*  384 */       this.raster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, paramInt1 * 3, 3, arrayOfInt2, null);
/*      */ 
/*  389 */       break;
/*      */     case 6:
/*  393 */       localObject = ColorSpace.getInstance(1000);
/*  394 */       arrayOfInt1 = new int[] { 8, 8, 8, 8 };
/*  395 */       arrayOfInt2 = new int[] { 3, 2, 1, 0 };
/*  396 */       this.colorModel = new ComponentColorModel((ColorSpace)localObject, arrayOfInt1, true, false, 3, 0);
/*      */ 
/*  399 */       this.raster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, paramInt1 * 4, 4, arrayOfInt2, null);
/*      */ 
/*  404 */       break;
/*      */     case 7:
/*  408 */       localObject = ColorSpace.getInstance(1000);
/*  409 */       arrayOfInt1 = new int[] { 8, 8, 8, 8 };
/*  410 */       arrayOfInt2 = new int[] { 3, 2, 1, 0 };
/*  411 */       this.colorModel = new ComponentColorModel((ColorSpace)localObject, arrayOfInt1, true, true, 3, 0);
/*      */ 
/*  414 */       this.raster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, paramInt1 * 4, 4, arrayOfInt2, null);
/*      */ 
/*  419 */       break;
/*      */     case 10:
/*  423 */       localObject = ColorSpace.getInstance(1003);
/*  424 */       arrayOfInt1 = new int[] { 8 };
/*  425 */       this.colorModel = new ComponentColorModel((ColorSpace)localObject, arrayOfInt1, false, true, 1, 0);
/*      */ 
/*  428 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  431 */       break;
/*      */     case 11:
/*  435 */       localObject = ColorSpace.getInstance(1003);
/*  436 */       arrayOfInt1 = new int[] { 16 };
/*  437 */       this.colorModel = new ComponentColorModel((ColorSpace)localObject, arrayOfInt1, false, true, 1, 1);
/*      */ 
/*  440 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  443 */       break;
/*      */     case 12:
/*  447 */       localObject = new byte[] { 0, -1 };
/*      */ 
/*  449 */       this.colorModel = new IndexColorModel(1, 2, (byte[])localObject, (byte[])localObject, (byte[])localObject);
/*  450 */       this.raster = Raster.createPackedRaster(0, paramInt1, paramInt2, 1, 1, null);
/*      */ 
/*  453 */       break;
/*      */     case 13:
/*  458 */       localObject = new int[256];
/*  459 */       int i = 0;
/*  460 */       for (int j = 0; j < 256; j += 51) {
/*  461 */         for (k = 0; k < 256; k += 51) {
/*  462 */           for (int m = 0; m < 256; m += 51) {
/*  463 */             localObject[(i++)] = (j << 16 | k << 8 | m);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  468 */       j = 256 / (256 - i);
/*      */ 
/*  471 */       int k = j * 3;
/*  472 */       for (; i < 256; i++) {
/*  473 */         localObject[i] = (k << 16 | k << 8 | k);
/*  474 */         k += j;
/*      */       }
/*      */ 
/*  477 */       this.colorModel = new IndexColorModel(8, 256, (int[])localObject, 0, false, -1, 0);
/*      */ 
/*  479 */       this.raster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, 1, null);
/*      */ 
/*  482 */       break;
/*      */     case 8:
/*  486 */       this.colorModel = new DirectColorModel(16, 63488, 2016, 31);
/*      */ 
/*  491 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  494 */       break;
/*      */     case 9:
/*  498 */       this.colorModel = new DirectColorModel(15, 31744, 992, 31);
/*      */ 
/*  503 */       this.raster = this.colorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*      */ 
/*  506 */       break;
/*      */     default:
/*  509 */       throw new IllegalArgumentException("Unknown image type " + paramInt3);
/*      */     }
/*      */ 
/*  513 */     this.imageType = paramInt3;
/*      */   }
/*      */ 
/*      */   public BufferedImage(int paramInt1, int paramInt2, int paramInt3, IndexColorModel paramIndexColorModel)
/*      */   {
/*  543 */     if ((paramIndexColorModel.hasAlpha()) && (paramIndexColorModel.isAlphaPremultiplied())) {
/*  544 */       throw new IllegalArgumentException("This image types do not have premultiplied alpha.");
/*      */     }
/*      */ 
/*  548 */     switch (paramInt3)
/*      */     {
/*      */     case 12:
/*  551 */       int j = paramIndexColorModel.getMapSize();
/*      */       int i;
/*  552 */       if (j <= 2)
/*  553 */         i = 1;
/*  554 */       else if (j <= 4)
/*  555 */         i = 2;
/*  556 */       else if (j <= 16)
/*  557 */         i = 4;
/*      */       else {
/*  559 */         throw new IllegalArgumentException("Color map for TYPE_BYTE_BINARY must have no more than 16 entries");
/*      */       }
/*      */ 
/*  563 */       this.raster = Raster.createPackedRaster(0, paramInt1, paramInt2, 1, i, null);
/*      */ 
/*  565 */       break;
/*      */     case 13:
/*  568 */       this.raster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, 1, null);
/*      */ 
/*  570 */       break;
/*      */     default:
/*  572 */       throw new IllegalArgumentException("Invalid image type (" + paramInt3 + ").  Image type must" + " be either TYPE_BYTE_BINARY or " + " TYPE_BYTE_INDEXED");
/*      */     }
/*      */ 
/*  578 */     if (!paramIndexColorModel.isCompatibleRaster(this.raster)) {
/*  579 */       throw new IllegalArgumentException("Incompatible image type and IndexColorModel");
/*      */     }
/*      */ 
/*  582 */     this.colorModel = paramIndexColorModel;
/*  583 */     this.imageType = paramInt3;
/*      */   }
/*      */ 
/*      */   public BufferedImage(ColorModel paramColorModel, WritableRaster paramWritableRaster, boolean paramBoolean, Hashtable<?, ?> paramHashtable)
/*      */   {
/*  629 */     if (!paramColorModel.isCompatibleRaster(paramWritableRaster)) {
/*  630 */       throw new IllegalArgumentException("Raster " + paramWritableRaster + " is incompatible with ColorModel " + paramColorModel);
/*      */     }
/*      */ 
/*  636 */     if ((paramWritableRaster.minX != 0) || (paramWritableRaster.minY != 0)) {
/*  637 */       throw new IllegalArgumentException("Raster " + paramWritableRaster + " has minX or minY not equal to zero: " + paramWritableRaster.minX + " " + paramWritableRaster.minY);
/*      */     }
/*      */ 
/*  643 */     this.colorModel = paramColorModel;
/*  644 */     this.raster = paramWritableRaster;
/*  645 */     this.properties = paramHashtable;
/*  646 */     int i = paramWritableRaster.getNumBands();
/*  647 */     boolean bool1 = paramColorModel.isAlphaPremultiplied();
/*  648 */     boolean bool2 = isStandard(paramColorModel, paramWritableRaster);
/*      */ 
/*  653 */     coerceData(paramBoolean);
/*      */ 
/*  655 */     SampleModel localSampleModel = paramWritableRaster.getSampleModel();
/*  656 */     ColorSpace localColorSpace = paramColorModel.getColorSpace();
/*  657 */     int j = localColorSpace.getType();
/*  658 */     if (j != 5) {
/*  659 */       if ((j == 6) && (bool2) && ((paramColorModel instanceof ComponentColorModel)))
/*      */       {
/*  663 */         if (((localSampleModel instanceof ComponentSampleModel)) && (((ComponentSampleModel)localSampleModel).getPixelStride() != i))
/*      */         {
/*  665 */           this.imageType = 0;
/*  666 */         } else if (((paramWritableRaster instanceof ByteComponentRaster)) && (paramWritableRaster.getNumBands() == 1) && (paramColorModel.getComponentSize(0) == 8) && (((ByteComponentRaster)paramWritableRaster).getPixelStride() == 1))
/*      */         {
/*  670 */           this.imageType = 10;
/*  671 */         } else if (((paramWritableRaster instanceof ShortComponentRaster)) && (paramWritableRaster.getNumBands() == 1) && (paramColorModel.getComponentSize(0) == 16) && (((ShortComponentRaster)paramWritableRaster).getPixelStride() == 1))
/*      */         {
/*  675 */           this.imageType = 11;
/*      */         }
/*      */       }
/*  678 */       else this.imageType = 0;
/*      */       return;
/*      */     }
/*      */     Object localObject1;
/*      */     int k;
/*      */     Object localObject2;
/*      */     int i1;
/*  683 */     if (((paramWritableRaster instanceof IntegerComponentRaster)) && ((i == 3) || (i == 4)))
/*      */     {
/*  685 */       localObject1 = (IntegerComponentRaster)paramWritableRaster;
/*      */ 
/*  689 */       k = paramColorModel.getPixelSize();
/*  690 */       if ((((IntegerComponentRaster)localObject1).getPixelStride() == 1) && (bool2) && ((paramColorModel instanceof DirectColorModel)) && ((k == 32) || (k == 24)))
/*      */       {
/*  696 */         localObject2 = (DirectColorModel)paramColorModel;
/*  697 */         int m = ((DirectColorModel)localObject2).getRedMask();
/*  698 */         int n = ((DirectColorModel)localObject2).getGreenMask();
/*  699 */         i1 = ((DirectColorModel)localObject2).getBlueMask();
/*  700 */         if ((m == 16711680) && (n == 65280) && (i1 == 255))
/*      */         {
/*  703 */           if (((DirectColorModel)localObject2).getAlphaMask() == -16777216) {
/*  704 */             this.imageType = (bool1 ? 3 : 2);
/*      */           }
/*  710 */           else if (!((DirectColorModel)localObject2).hasAlpha()) {
/*  711 */             this.imageType = 1;
/*      */           }
/*      */ 
/*      */         }
/*  715 */         else if ((m == 255) && (n == 65280) && (i1 == 16711680))
/*      */         {
/*  717 */           if (!((DirectColorModel)localObject2).hasAlpha()) {
/*  718 */             this.imageType = 4;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  723 */     else if (((paramColorModel instanceof IndexColorModel)) && (i == 1) && (bool2) && ((!paramColorModel.hasAlpha()) || (!bool1)))
/*      */     {
/*  727 */       localObject1 = (IndexColorModel)paramColorModel;
/*  728 */       k = ((IndexColorModel)localObject1).getPixelSize();
/*      */ 
/*  730 */       if ((paramWritableRaster instanceof BytePackedRaster)) {
/*  731 */         this.imageType = 12;
/*      */       }
/*  733 */       else if ((paramWritableRaster instanceof ByteComponentRaster)) {
/*  734 */         localObject2 = (ByteComponentRaster)paramWritableRaster;
/*  735 */         if ((((ByteComponentRaster)localObject2).getPixelStride() == 1) && (k <= 8)) {
/*  736 */           this.imageType = 13;
/*      */         }
/*      */       }
/*      */     }
/*  740 */     else if (((paramWritableRaster instanceof ShortComponentRaster)) && ((paramColorModel instanceof DirectColorModel)) && (bool2) && (i == 3) && (!paramColorModel.hasAlpha()))
/*      */     {
/*  746 */       localObject1 = (DirectColorModel)paramColorModel;
/*  747 */       if (((DirectColorModel)localObject1).getRedMask() == 63488) {
/*  748 */         if ((((DirectColorModel)localObject1).getGreenMask() == 2016) && (((DirectColorModel)localObject1).getBlueMask() == 31))
/*      */         {
/*  750 */           this.imageType = 8;
/*      */         }
/*      */       }
/*  753 */       else if ((((DirectColorModel)localObject1).getRedMask() == 31744) && 
/*  754 */         (((DirectColorModel)localObject1).getGreenMask() == 992) && (((DirectColorModel)localObject1).getBlueMask() == 31))
/*      */       {
/*  756 */         this.imageType = 9;
/*      */       }
/*      */ 
/*      */     }
/*  760 */     else if (((paramWritableRaster instanceof ByteComponentRaster)) && ((paramColorModel instanceof ComponentColorModel)) && (bool2) && ((paramWritableRaster.getSampleModel() instanceof PixelInterleavedSampleModel)) && ((i == 3) || (i == 4)))
/*      */     {
/*  766 */       localObject1 = (ComponentColorModel)paramColorModel;
/*  767 */       PixelInterleavedSampleModel localPixelInterleavedSampleModel = (PixelInterleavedSampleModel)paramWritableRaster.getSampleModel();
/*      */ 
/*  769 */       localObject2 = (ByteComponentRaster)paramWritableRaster;
/*  770 */       int[] arrayOfInt1 = localPixelInterleavedSampleModel.getBandOffsets();
/*  771 */       if (((ComponentColorModel)localObject1).getNumComponents() != i) {
/*  772 */         throw new RasterFormatException("Number of components in ColorModel (" + ((ComponentColorModel)localObject1).getNumComponents() + ") does not match # in " + " Raster (" + i + ")");
/*      */       }
/*      */ 
/*  778 */       int[] arrayOfInt2 = ((ComponentColorModel)localObject1).getComponentSize();
/*  779 */       i1 = 1;
/*  780 */       for (int i2 = 0; i2 < i; i2++) {
/*  781 */         if (arrayOfInt2[i2] != 8) {
/*  782 */           i1 = 0;
/*  783 */           break;
/*      */         }
/*      */       }
/*  786 */       if ((i1 != 0) && (((ByteComponentRaster)localObject2).getPixelStride() == i) && (arrayOfInt1[0] == i - 1) && (arrayOfInt1[1] == i - 2) && (arrayOfInt1[2] == i - 3))
/*      */       {
/*  792 */         if ((i == 3) && (!((ComponentColorModel)localObject1).hasAlpha())) {
/*  793 */           this.imageType = 5;
/*      */         }
/*  795 */         else if ((arrayOfInt1[3] == 0) && (((ComponentColorModel)localObject1).hasAlpha()))
/*  796 */           this.imageType = (bool1 ? 7 : 6);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isStandard(ColorModel paramColorModel, WritableRaster paramWritableRaster)
/*      */   {
/*  805 */     Class localClass1 = paramColorModel.getClass();
/*  806 */     final Class localClass2 = paramWritableRaster.getClass();
/*  807 */     final Class localClass3 = paramWritableRaster.getSampleModel().getClass();
/*      */ 
/*  809 */     PrivilegedAction local1 = new PrivilegedAction()
/*      */     {
/*      */       public Boolean run()
/*      */       {
/*  815 */         ClassLoader localClassLoader = System.class.getClassLoader();
/*      */ 
/*  817 */         return Boolean.valueOf((this.val$cmClass.getClassLoader() == localClassLoader) && (localClass3.getClassLoader() == localClassLoader) && (localClass2.getClassLoader() == localClassLoader));
/*      */       }
/*      */     };
/*  822 */     return ((Boolean)AccessController.doPrivileged(local1)).booleanValue();
/*      */   }
/*      */ 
/*      */   public int getType()
/*      */   {
/*  845 */     return this.imageType;
/*      */   }
/*      */ 
/*      */   public ColorModel getColorModel()
/*      */   {
/*  854 */     return this.colorModel;
/*      */   }
/*      */ 
/*      */   public WritableRaster getRaster()
/*      */   {
/*  863 */     return this.raster;
/*      */   }
/*      */ 
/*      */   public WritableRaster getAlphaRaster()
/*      */   {
/*  889 */     return this.colorModel.getAlphaRaster(this.raster);
/*      */   }
/*      */ 
/*      */   public int getRGB(int paramInt1, int paramInt2)
/*      */   {
/*  918 */     return this.colorModel.getRGB(this.raster.getDataElements(paramInt1, paramInt2, null));
/*      */   }
/*      */ 
/*      */   public int[] getRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*      */   {
/*  955 */     int i = paramInt5;
/*      */ 
/*  958 */     int k = this.raster.getNumBands();
/*  959 */     int m = this.raster.getDataBuffer().getDataType();
/*      */     Object localObject;
/*  960 */     switch (m) {
/*      */     case 0:
/*  962 */       localObject = new byte[k];
/*  963 */       break;
/*      */     case 1:
/*  965 */       localObject = new short[k];
/*  966 */       break;
/*      */     case 3:
/*  968 */       localObject = new int[k];
/*  969 */       break;
/*      */     case 4:
/*  971 */       localObject = new float[k];
/*  972 */       break;
/*      */     case 5:
/*  974 */       localObject = new double[k];
/*  975 */       break;
/*      */     case 2:
/*      */     default:
/*  977 */       throw new IllegalArgumentException("Unknown data buffer type: " + m);
/*      */     }
/*      */ 
/*  981 */     if (paramArrayOfInt == null) {
/*  982 */       paramArrayOfInt = new int[paramInt5 + paramInt4 * paramInt6];
/*      */     }
/*      */ 
/*  985 */     for (int n = paramInt2; n < paramInt2 + paramInt4; i += paramInt6) {
/*  986 */       int j = i;
/*  987 */       for (int i1 = paramInt1; i1 < paramInt1 + paramInt3; i1++)
/*  988 */         paramArrayOfInt[(j++)] = this.colorModel.getRGB(this.raster.getDataElements(i1, n, localObject));
/*  985 */       n++;
/*      */     }
/*      */ 
/*  994 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public synchronized void setRGB(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1018 */     this.raster.setDataElements(paramInt1, paramInt2, this.colorModel.getDataElements(paramInt3, null));
/*      */   }
/*      */ 
/*      */   public void setRGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*      */   {
/* 1053 */     int i = paramInt5;
/*      */ 
/* 1055 */     Object localObject = null;
/*      */ 
/* 1057 */     for (int k = paramInt2; k < paramInt2 + paramInt4; i += paramInt6) {
/* 1058 */       int j = i;
/* 1059 */       for (int m = paramInt1; m < paramInt1 + paramInt3; m++) {
/* 1060 */         localObject = this.colorModel.getDataElements(paramArrayOfInt[(j++)], localObject);
/* 1061 */         this.raster.setDataElements(m, k, localObject);
/*      */       }
/* 1057 */       k++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getWidth()
/*      */   {
/* 1072 */     return this.raster.getWidth();
/*      */   }
/*      */ 
/*      */   public int getHeight()
/*      */   {
/* 1080 */     return this.raster.getHeight();
/*      */   }
/*      */ 
/*      */   public int getWidth(ImageObserver paramImageObserver)
/*      */   {
/* 1089 */     return this.raster.getWidth();
/*      */   }
/*      */ 
/*      */   public int getHeight(ImageObserver paramImageObserver)
/*      */   {
/* 1098 */     return this.raster.getHeight();
/*      */   }
/*      */ 
/*      */   public ImageProducer getSource()
/*      */   {
/* 1108 */     if (this.osis == null) {
/* 1109 */       if (this.properties == null) {
/* 1110 */         this.properties = new Hashtable();
/*      */       }
/* 1112 */       this.osis = new OffScreenImageSource(this, this.properties);
/*      */     }
/* 1114 */     return this.osis;
/*      */   }
/*      */ 
/*      */   public Object getProperty(String paramString, ImageObserver paramImageObserver)
/*      */   {
/* 1139 */     return getProperty(paramString);
/*      */   }
/*      */ 
/*      */   public Object getProperty(String paramString)
/*      */   {
/* 1150 */     if (paramString == null) {
/* 1151 */       throw new NullPointerException("null property name is not allowed");
/*      */     }
/* 1153 */     if (this.properties == null) {
/* 1154 */       return Image.UndefinedProperty;
/*      */     }
/* 1156 */     Object localObject = this.properties.get(paramString);
/* 1157 */     if (localObject == null) {
/* 1158 */       localObject = Image.UndefinedProperty;
/*      */     }
/* 1160 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/* 1172 */     return createGraphics();
/*      */   }
/*      */ 
/*      */   public Graphics2D createGraphics()
/*      */   {
/* 1182 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/* 1184 */     return localGraphicsEnvironment.createGraphics(this);
/*      */   }
/*      */ 
/*      */   public BufferedImage getSubimage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1203 */     return new BufferedImage(this.colorModel, this.raster.createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, 0, 0, null), this.colorModel.isAlphaPremultiplied(), this.properties);
/*      */   }
/*      */ 
/*      */   public boolean isAlphaPremultiplied()
/*      */   {
/* 1217 */     return this.colorModel.isAlphaPremultiplied();
/*      */   }
/*      */ 
/*      */   public void coerceData(boolean paramBoolean)
/*      */   {
/* 1229 */     if ((this.colorModel.hasAlpha()) && (this.colorModel.isAlphaPremultiplied() != paramBoolean))
/*      */     {
/* 1232 */       this.colorModel = this.colorModel.coerceData(this.raster, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1243 */     return "BufferedImage@" + Integer.toHexString(hashCode()) + ": type = " + this.imageType + " " + this.colorModel + " " + this.raster;
/*      */   }
/*      */ 
/*      */   public Vector<RenderedImage> getSources()
/*      */   {
/* 1264 */     return null;
/*      */   }
/*      */ 
/*      */   public String[] getPropertyNames()
/*      */   {
/* 1276 */     return null;
/*      */   }
/*      */ 
/*      */   public int getMinX()
/*      */   {
/* 1286 */     return this.raster.getMinX();
/*      */   }
/*      */ 
/*      */   public int getMinY()
/*      */   {
/* 1296 */     return this.raster.getMinY();
/*      */   }
/*      */ 
/*      */   public SampleModel getSampleModel()
/*      */   {
/* 1306 */     return this.raster.getSampleModel();
/*      */   }
/*      */ 
/*      */   public int getNumXTiles()
/*      */   {
/* 1315 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getNumYTiles()
/*      */   {
/* 1324 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getMinTileX()
/*      */   {
/* 1333 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getMinTileY()
/*      */   {
/* 1342 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getTileWidth()
/*      */   {
/* 1350 */     return this.raster.getWidth();
/*      */   }
/*      */ 
/*      */   public int getTileHeight()
/*      */   {
/* 1358 */     return this.raster.getHeight();
/*      */   }
/*      */ 
/*      */   public int getTileGridXOffset()
/*      */   {
/* 1368 */     return this.raster.getSampleModelTranslateX();
/*      */   }
/*      */ 
/*      */   public int getTileGridYOffset()
/*      */   {
/* 1378 */     return this.raster.getSampleModelTranslateY();
/*      */   }
/*      */ 
/*      */   public Raster getTile(int paramInt1, int paramInt2)
/*      */   {
/* 1396 */     if ((paramInt1 == 0) && (paramInt2 == 0)) {
/* 1397 */       return this.raster;
/*      */     }
/* 1399 */     throw new ArrayIndexOutOfBoundsException("BufferedImages only have one tile with index 0,0");
/*      */   }
/*      */ 
/*      */   public Raster getData()
/*      */   {
/* 1415 */     int i = this.raster.getWidth();
/* 1416 */     int j = this.raster.getHeight();
/* 1417 */     int k = this.raster.getMinX();
/* 1418 */     int m = this.raster.getMinY();
/* 1419 */     WritableRaster localWritableRaster = Raster.createWritableRaster(this.raster.getSampleModel(), new Point(this.raster.getSampleModelTranslateX(), this.raster.getSampleModelTranslateY()));
/*      */ 
/* 1424 */     Object localObject = null;
/*      */ 
/* 1426 */     for (int n = m; n < m + j; n++) {
/* 1427 */       localObject = this.raster.getDataElements(k, n, i, 1, localObject);
/* 1428 */       localWritableRaster.setDataElements(k, n, i, 1, localObject);
/*      */     }
/* 1430 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public Raster getData(Rectangle paramRectangle)
/*      */   {
/* 1445 */     SampleModel localSampleModel1 = this.raster.getSampleModel();
/* 1446 */     SampleModel localSampleModel2 = localSampleModel1.createCompatibleSampleModel(paramRectangle.width, paramRectangle.height);
/*      */ 
/* 1448 */     WritableRaster localWritableRaster = Raster.createWritableRaster(localSampleModel2, paramRectangle.getLocation());
/*      */ 
/* 1450 */     int i = paramRectangle.width;
/* 1451 */     int j = paramRectangle.height;
/* 1452 */     int k = paramRectangle.x;
/* 1453 */     int m = paramRectangle.y;
/*      */ 
/* 1455 */     Object localObject = null;
/*      */ 
/* 1457 */     for (int n = m; n < m + j; n++) {
/* 1458 */       localObject = this.raster.getDataElements(k, n, i, 1, localObject);
/* 1459 */       localWritableRaster.setDataElements(k, n, i, 1, localObject);
/*      */     }
/* 1461 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public WritableRaster copyData(WritableRaster paramWritableRaster)
/*      */   {
/* 1480 */     if (paramWritableRaster == null) {
/* 1481 */       return (WritableRaster)getData();
/*      */     }
/* 1483 */     int i = paramWritableRaster.getWidth();
/* 1484 */     int j = paramWritableRaster.getHeight();
/* 1485 */     int k = paramWritableRaster.getMinX();
/* 1486 */     int m = paramWritableRaster.getMinY();
/*      */ 
/* 1488 */     Object localObject = null;
/*      */ 
/* 1490 */     for (int n = m; n < m + j; n++) {
/* 1491 */       localObject = this.raster.getDataElements(k, n, i, 1, localObject);
/* 1492 */       paramWritableRaster.setDataElements(k, n, i, 1, localObject);
/*      */     }
/*      */ 
/* 1495 */     return paramWritableRaster;
/*      */   }
/*      */ 
/*      */   public void setData(Raster paramRaster)
/*      */   {
/* 1509 */     int i = paramRaster.getWidth();
/* 1510 */     int j = paramRaster.getHeight();
/* 1511 */     int k = paramRaster.getMinX();
/* 1512 */     int m = paramRaster.getMinY();
/*      */ 
/* 1514 */     int[] arrayOfInt = null;
/*      */ 
/* 1517 */     Rectangle localRectangle1 = new Rectangle(k, m, i, j);
/* 1518 */     Rectangle localRectangle2 = new Rectangle(0, 0, this.raster.width, this.raster.height);
/* 1519 */     Rectangle localRectangle3 = localRectangle1.intersection(localRectangle2);
/* 1520 */     if (localRectangle3.isEmpty()) {
/* 1521 */       return;
/*      */     }
/* 1523 */     i = localRectangle3.width;
/* 1524 */     j = localRectangle3.height;
/* 1525 */     k = localRectangle3.x;
/* 1526 */     m = localRectangle3.y;
/*      */ 
/* 1530 */     for (int n = m; n < m + j; n++) {
/* 1531 */       arrayOfInt = paramRaster.getPixels(k, n, i, 1, arrayOfInt);
/* 1532 */       this.raster.setPixels(k, n, i, 1, arrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addTileObserver(TileObserver paramTileObserver)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void removeTileObserver(TileObserver paramTileObserver)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isTileWritable(int paramInt1, int paramInt2)
/*      */   {
/* 1566 */     if ((paramInt1 == 0) && (paramInt2 == 0)) {
/* 1567 */       return true;
/*      */     }
/* 1569 */     throw new IllegalArgumentException("Only 1 tile in image");
/*      */   }
/*      */ 
/*      */   public Point[] getWritableTileIndices()
/*      */   {
/* 1581 */     Point[] arrayOfPoint = new Point[1];
/* 1582 */     arrayOfPoint[0] = new Point(0, 0);
/*      */ 
/* 1584 */     return arrayOfPoint;
/*      */   }
/*      */ 
/*      */   public boolean hasTileWriters()
/*      */   {
/* 1597 */     return true;
/*      */   }
/*      */ 
/*      */   public WritableRaster getWritableTile(int paramInt1, int paramInt2)
/*      */   {
/* 1610 */     return this.raster;
/*      */   }
/*      */ 
/*      */   public void releaseWritableTile(int paramInt1, int paramInt2)
/*      */   {
/*      */   }
/*      */ 
/*      */   public int getTransparency()
/*      */   {
/* 1637 */     return this.colorModel.getTransparency();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  293 */     ColorModel.loadLibraries();
/*  294 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.BufferedImage
 * JD-Core Version:    0.6.2
 */