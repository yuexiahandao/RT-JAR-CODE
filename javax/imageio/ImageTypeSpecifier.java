/*      */ package javax.imageio;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.image.BandedSampleModel;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentColorModel;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.PixelInterleavedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class ImageTypeSpecifier
/*      */ {
/*      */   protected ColorModel colorModel;
/*      */   protected SampleModel sampleModel;
/*   75 */   private static ImageTypeSpecifier[] BISpecifier = new ImageTypeSpecifier[14];
/*      */ 
/*   73 */   private static ColorSpace sRGB = ColorSpace.getInstance(1000);
/*      */ 
/*      */   private ImageTypeSpecifier()
/*      */   {
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier(ColorModel paramColorModel, SampleModel paramSampleModel)
/*      */   {
/*   99 */     if (paramColorModel == null) {
/*  100 */       throw new IllegalArgumentException("colorModel == null!");
/*      */     }
/*  102 */     if (paramSampleModel == null) {
/*  103 */       throw new IllegalArgumentException("sampleModel == null!");
/*      */     }
/*  105 */     if (!paramColorModel.isCompatibleSampleModel(paramSampleModel)) {
/*  106 */       throw new IllegalArgumentException("sampleModel is incompatible with colorModel!");
/*      */     }
/*      */ 
/*  109 */     this.colorModel = paramColorModel;
/*  110 */     this.sampleModel = paramSampleModel;
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier(RenderedImage paramRenderedImage)
/*      */   {
/*  127 */     if (paramRenderedImage == null) {
/*  128 */       throw new IllegalArgumentException("image == null!");
/*      */     }
/*  130 */     this.colorModel = paramRenderedImage.getColorModel();
/*  131 */     this.sampleModel = paramRenderedImage.getSampleModel();
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createPacked(ColorSpace paramColorSpace, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
/*      */   {
/*  231 */     return new Packed(paramColorSpace, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean);
/*      */   }
/*      */ 
/*      */   static ColorModel createComponentCM(ColorSpace paramColorSpace, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  245 */     int i = paramBoolean1 ? 3 : 1;
/*      */ 
/*  248 */     int[] arrayOfInt = new int[paramInt1];
/*  249 */     int j = DataBuffer.getDataTypeSize(paramInt2);
/*      */ 
/*  251 */     for (int k = 0; k < paramInt1; k++) {
/*  252 */       arrayOfInt[k] = j;
/*      */     }
/*      */ 
/*  255 */     return new ComponentColorModel(paramColorSpace, arrayOfInt, paramBoolean1, paramBoolean2, i, paramInt2);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createInterleaved(ColorSpace paramColorSpace, int[] paramArrayOfInt, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  399 */     return new Interleaved(paramColorSpace, paramArrayOfInt, paramInt, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createBanded(ColorSpace paramColorSpace, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  558 */     return new Banded(paramColorSpace, paramArrayOfInt1, paramArrayOfInt2, paramInt, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createGrayscale(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  685 */     return new Grayscale(paramInt1, paramInt2, paramBoolean, false, false);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createGrayscale(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  722 */     return new Grayscale(paramInt1, paramInt2, paramBoolean1, true, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createIndexed(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt1, int paramInt2)
/*      */   {
/*  859 */     return new Indexed(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createFromBufferedImageType(int paramInt)
/*      */   {
/*  899 */     if ((paramInt >= 1) && (paramInt <= 13))
/*      */     {
/*  901 */       return getSpecifier(paramInt);
/*  902 */     }if (paramInt == 0) {
/*  903 */       throw new IllegalArgumentException("Cannot create from TYPE_CUSTOM!");
/*      */     }
/*  905 */     throw new IllegalArgumentException("Invalid BufferedImage type!");
/*      */   }
/*      */ 
/*      */   public static ImageTypeSpecifier createFromRenderedImage(RenderedImage paramRenderedImage)
/*      */   {
/*  924 */     if (paramRenderedImage == null) {
/*  925 */       throw new IllegalArgumentException("image == null!");
/*      */     }
/*      */ 
/*  928 */     if ((paramRenderedImage instanceof BufferedImage)) {
/*  929 */       int i = ((BufferedImage)paramRenderedImage).getType();
/*  930 */       if (i != 0) {
/*  931 */         return getSpecifier(i);
/*      */       }
/*      */     }
/*      */ 
/*  935 */     return new ImageTypeSpecifier(paramRenderedImage);
/*      */   }
/*      */ 
/*      */   public int getBufferedImageType()
/*      */   {
/*  962 */     BufferedImage localBufferedImage = createBufferedImage(1, 1);
/*  963 */     return localBufferedImage.getType();
/*      */   }
/*      */ 
/*      */   public int getNumComponents()
/*      */   {
/*  974 */     return this.colorModel.getNumComponents();
/*      */   }
/*      */ 
/*      */   public int getNumBands()
/*      */   {
/*  985 */     return this.sampleModel.getNumBands();
/*      */   }
/*      */ 
/*      */   public int getBitsPerBand(int paramInt)
/*      */   {
/* 1000 */     if (((paramInt < 0 ? 1 : 0) | (paramInt >= getNumBands() ? 1 : 0)) != 0) {
/* 1001 */       throw new IllegalArgumentException("band out of range!");
/*      */     }
/* 1003 */     return this.sampleModel.getSampleSize(paramInt);
/*      */   }
/*      */ 
/*      */   public SampleModel getSampleModel()
/*      */   {
/* 1014 */     return this.sampleModel;
/*      */   }
/*      */ 
/*      */   public SampleModel getSampleModel(int paramInt1, int paramInt2)
/*      */   {
/* 1035 */     if (paramInt1 * paramInt2 > 2147483647L) {
/* 1036 */       throw new IllegalArgumentException("width*height > Integer.MAX_VALUE!");
/*      */     }
/*      */ 
/* 1039 */     return this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public ColorModel getColorModel()
/*      */   {
/* 1048 */     return this.colorModel;
/*      */   }
/*      */ 
/*      */   public BufferedImage createBufferedImage(int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/* 1072 */       SampleModel localSampleModel = getSampleModel(paramInt1, paramInt2);
/* 1073 */       WritableRaster localWritableRaster = Raster.createWritableRaster(localSampleModel, new Point(0, 0));
/*      */ 
/* 1076 */       return new BufferedImage(this.colorModel, localWritableRaster, this.colorModel.isAlphaPremultiplied(), new Hashtable());
/*      */     }
/*      */     catch (NegativeArraySizeException localNegativeArraySizeException)
/*      */     {
/*      */     }
/* 1081 */     throw new IllegalArgumentException("Array size > Integer.MAX_VALUE!");
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1098 */     if ((paramObject == null) || (!(paramObject instanceof ImageTypeSpecifier))) {
/* 1099 */       return false;
/*      */     }
/*      */ 
/* 1102 */     ImageTypeSpecifier localImageTypeSpecifier = (ImageTypeSpecifier)paramObject;
/* 1103 */     return (this.colorModel.equals(localImageTypeSpecifier.colorModel)) && (this.sampleModel.equals(localImageTypeSpecifier.sampleModel));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1113 */     return 9 * this.colorModel.hashCode() + 14 * this.sampleModel.hashCode();
/*      */   }
/*      */ 
/*      */   private static ImageTypeSpecifier getSpecifier(int paramInt) {
/* 1117 */     if (BISpecifier[paramInt] == null) {
/* 1118 */       BISpecifier[paramInt] = createSpecifier(paramInt);
/*      */     }
/* 1120 */     return BISpecifier[paramInt];
/*      */   }
/*      */ 
/*      */   private static ImageTypeSpecifier createSpecifier(int paramInt) {
/* 1124 */     switch (paramInt) {
/*      */     case 1:
/* 1126 */       return createPacked(sRGB, 16711680, 65280, 255, 0, 3, false);
/*      */     case 2:
/* 1135 */       return createPacked(sRGB, 16711680, 65280, 255, -16777216, 3, false);
/*      */     case 3:
/* 1144 */       return createPacked(sRGB, 16711680, 65280, 255, -16777216, 3, true);
/*      */     case 4:
/* 1153 */       return createPacked(sRGB, 255, 65280, 16711680, 0, 3, false);
/*      */     case 5:
/* 1162 */       return createInterleaved(sRGB, new int[] { 2, 1, 0 }, 0, false, false);
/*      */     case 6:
/* 1169 */       return createInterleaved(sRGB, new int[] { 3, 2, 1, 0 }, 0, true, false);
/*      */     case 7:
/* 1176 */       return createInterleaved(sRGB, new int[] { 3, 2, 1, 0 }, 0, true, true);
/*      */     case 8:
/* 1183 */       return createPacked(sRGB, 63488, 2016, 31, 0, 1, false);
/*      */     case 9:
/* 1192 */       return createPacked(sRGB, 31744, 992, 31, 0, 1, false);
/*      */     case 10:
/* 1201 */       return createGrayscale(8, 0, false);
/*      */     case 11:
/* 1206 */       return createGrayscale(16, 1, false);
/*      */     case 12:
/* 1211 */       return createGrayscale(1, 0, false);
/*      */     case 13:
/* 1218 */       BufferedImage localBufferedImage = new BufferedImage(1, 1, 13);
/*      */ 
/* 1220 */       IndexColorModel localIndexColorModel = (IndexColorModel)localBufferedImage.getColorModel();
/* 1221 */       int i = localIndexColorModel.getMapSize();
/* 1222 */       byte[] arrayOfByte1 = new byte[i];
/* 1223 */       byte[] arrayOfByte2 = new byte[i];
/* 1224 */       byte[] arrayOfByte3 = new byte[i];
/* 1225 */       byte[] arrayOfByte4 = new byte[i];
/*      */ 
/* 1227 */       localIndexColorModel.getReds(arrayOfByte1);
/* 1228 */       localIndexColorModel.getGreens(arrayOfByte2);
/* 1229 */       localIndexColorModel.getBlues(arrayOfByte3);
/* 1230 */       localIndexColorModel.getAlphas(arrayOfByte4);
/*      */ 
/* 1232 */       return createIndexed(arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4, 8, 0);
/*      */     }
/*      */ 
/* 1237 */     throw new IllegalArgumentException("Invalid BufferedImage type!");
/*      */   }
/*      */ 
/*      */   static class Banded extends ImageTypeSpecifier
/*      */   {
/*      */     ColorSpace colorSpace;
/*      */     int[] bankIndices;
/*      */     int[] bandOffsets;
/*      */     int dataType;
/*      */     boolean hasAlpha;
/*      */     boolean isAlphaPremultiplied;
/*      */ 
/*      */     public Banded(ColorSpace paramColorSpace, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  421 */       super();
/*  422 */       if (paramColorSpace == null) {
/*  423 */         throw new IllegalArgumentException("colorSpace == null!");
/*      */       }
/*  425 */       if (paramArrayOfInt1 == null) {
/*  426 */         throw new IllegalArgumentException("bankIndices == null!");
/*      */       }
/*  428 */       if (paramArrayOfInt2 == null) {
/*  429 */         throw new IllegalArgumentException("bandOffsets == null!");
/*      */       }
/*  431 */       if (paramArrayOfInt1.length != paramArrayOfInt2.length) {
/*  432 */         throw new IllegalArgumentException("bankIndices.length != bandOffsets.length!");
/*      */       }
/*      */ 
/*  435 */       if ((paramInt != 0) && (paramInt != 2) && (paramInt != 1) && (paramInt != 3) && (paramInt != 4) && (paramInt != 5))
/*      */       {
/*  441 */         throw new IllegalArgumentException("Bad value for dataType!");
/*      */       }
/*      */ 
/*  444 */       int i = paramColorSpace.getNumComponents() + (paramBoolean1 ? 1 : 0);
/*      */ 
/*  446 */       if (paramArrayOfInt2.length != i) {
/*  447 */         throw new IllegalArgumentException("bandOffsets.length is wrong!");
/*      */       }
/*      */ 
/*  451 */       this.colorSpace = paramColorSpace;
/*  452 */       this.bankIndices = ((int[])paramArrayOfInt1.clone());
/*  453 */       this.bandOffsets = ((int[])paramArrayOfInt2.clone());
/*  454 */       this.dataType = paramInt;
/*  455 */       this.hasAlpha = paramBoolean1;
/*  456 */       this.isAlphaPremultiplied = paramBoolean2;
/*      */ 
/*  458 */       this.colorModel = ImageTypeSpecifier.createComponentCM(paramColorSpace, paramArrayOfInt1.length, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  465 */       int j = 1;
/*  466 */       int k = 1;
/*  467 */       this.sampleModel = new BandedSampleModel(paramInt, j, k, j, paramArrayOfInt1, paramArrayOfInt2);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  475 */       if ((paramObject == null) || (!(paramObject instanceof Banded)))
/*      */       {
/*  477 */         return false;
/*      */       }
/*      */ 
/*  480 */       Banded localBanded = (Banded)paramObject;
/*      */ 
/*  483 */       if ((!this.colorSpace.equals(localBanded.colorSpace)) || (this.dataType != localBanded.dataType) || (this.hasAlpha != localBanded.hasAlpha) || (this.isAlphaPremultiplied != localBanded.isAlphaPremultiplied) || (this.bankIndices.length != localBanded.bankIndices.length) || (this.bandOffsets.length != localBanded.bandOffsets.length))
/*      */       {
/*  489 */         return false;
/*      */       }
/*      */ 
/*  492 */       for (int i = 0; i < this.bankIndices.length; i++) {
/*  493 */         if (this.bankIndices[i] != localBanded.bankIndices[i]) {
/*  494 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  498 */       for (i = 0; i < this.bandOffsets.length; i++) {
/*  499 */         if (this.bandOffsets[i] != localBanded.bandOffsets[i]) {
/*  500 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  504 */       return true;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  508 */       return super.hashCode() + 3 * this.bandOffsets.length + 7 * this.bankIndices.length + 21 * this.dataType + (this.hasAlpha ? 19 : 29);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Grayscale extends ImageTypeSpecifier
/*      */   {
/*      */     int bits;
/*      */     int dataType;
/*      */     boolean isSigned;
/*      */     boolean hasAlpha;
/*      */     boolean isAlphaPremultiplied;
/*      */ 
/*      */     public Grayscale(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */     {
/*  580 */       super();
/*  581 */       if ((paramInt1 != 1) && (paramInt1 != 2) && (paramInt1 != 4) && (paramInt1 != 8) && (paramInt1 != 16))
/*      */       {
/*  584 */         throw new IllegalArgumentException("Bad value for bits!");
/*      */       }
/*  586 */       if ((paramInt2 != 0) && (paramInt2 != 2) && (paramInt2 != 1))
/*      */       {
/*  590 */         throw new IllegalArgumentException("Bad value for dataType!");
/*      */       }
/*      */ 
/*  593 */       if ((paramInt1 > 8) && (paramInt2 == 0)) {
/*  594 */         throw new IllegalArgumentException("Too many bits for dataType!");
/*      */       }
/*      */ 
/*  598 */       this.bits = paramInt1;
/*  599 */       this.dataType = paramInt2;
/*  600 */       this.isSigned = paramBoolean1;
/*  601 */       this.hasAlpha = paramBoolean2;
/*  602 */       this.isAlphaPremultiplied = paramBoolean3;
/*      */ 
/*  604 */       ColorSpace localColorSpace = ColorSpace.getInstance(1003);
/*      */       int i;
/*  606 */       if (((paramInt1 == 8) && (paramInt2 == 0)) || ((paramInt1 == 16) && ((paramInt2 == 2) || (paramInt2 == 1))))
/*      */       {
/*  612 */         i = paramBoolean2 ? 2 : 1;
/*  613 */         int j = paramBoolean2 ? 3 : 1;
/*      */ 
/*  617 */         int[] arrayOfInt1 = new int[i];
/*  618 */         arrayOfInt1[0] = paramInt1;
/*  619 */         if (i == 2) {
/*  620 */           arrayOfInt1[1] = paramInt1;
/*      */         }
/*  622 */         this.colorModel = new ComponentColorModel(localColorSpace, arrayOfInt1, paramBoolean2, paramBoolean3, j, paramInt2);
/*      */ 
/*  630 */         int[] arrayOfInt2 = new int[i];
/*  631 */         arrayOfInt2[0] = 0;
/*  632 */         if (i == 2) {
/*  633 */           arrayOfInt2[1] = 1;
/*      */         }
/*      */ 
/*  636 */         int m = 1;
/*  637 */         int n = 1;
/*  638 */         this.sampleModel = new PixelInterleavedSampleModel(paramInt2, m, n, i, m * i, arrayOfInt2);
/*      */       }
/*      */       else
/*      */       {
/*  644 */         i = 1 << paramInt1;
/*  645 */         byte[] arrayOfByte = new byte[i];
/*  646 */         for (int k = 0; k < i; k++) {
/*  647 */           arrayOfByte[k] = ((byte)(k * 255 / (i - 1)));
/*      */         }
/*  649 */         this.colorModel = new IndexColorModel(paramInt1, i, arrayOfByte, arrayOfByte, arrayOfByte);
/*      */ 
/*  652 */         this.sampleModel = new MultiPixelPackedSampleModel(paramInt2, 1, 1, paramInt1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Indexed extends ImageTypeSpecifier
/*      */   {
/*      */     byte[] redLUT;
/*      */     byte[] greenLUT;
/*      */     byte[] blueLUT;
/*  735 */     byte[] alphaLUT = null;
/*      */     int bits;
/*      */     int dataType;
/*      */ 
/*      */     public Indexed(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, int paramInt1, int paramInt2)
/*      */     {
/*  744 */       super();
/*  745 */       if ((paramArrayOfByte1 == null) || (paramArrayOfByte2 == null) || (paramArrayOfByte3 == null)) {
/*  746 */         throw new IllegalArgumentException("LUT is null!");
/*      */       }
/*  748 */       if ((paramInt1 != 1) && (paramInt1 != 2) && (paramInt1 != 4) && (paramInt1 != 8) && (paramInt1 != 16))
/*      */       {
/*  750 */         throw new IllegalArgumentException("Bad value for bits!");
/*      */       }
/*  752 */       if ((paramInt2 != 0) && (paramInt2 != 2) && (paramInt2 != 1) && (paramInt2 != 3))
/*      */       {
/*  756 */         throw new IllegalArgumentException("Bad value for dataType!");
/*      */       }
/*      */ 
/*  759 */       if (((paramInt1 > 8) && (paramInt2 == 0)) || ((paramInt1 > 16) && (paramInt2 != 3)))
/*      */       {
/*  761 */         throw new IllegalArgumentException("Too many bits for dataType!");
/*      */       }
/*      */ 
/*  765 */       int i = 1 << paramInt1;
/*  766 */       if ((paramArrayOfByte1.length != i) || (paramArrayOfByte2.length != i) || (paramArrayOfByte3.length != i) || ((paramArrayOfByte4 != null) && (paramArrayOfByte4.length != i)))
/*      */       {
/*  770 */         throw new IllegalArgumentException("LUT has improper length!");
/*      */       }
/*  772 */       this.redLUT = ((byte[])paramArrayOfByte1.clone());
/*  773 */       this.greenLUT = ((byte[])paramArrayOfByte2.clone());
/*  774 */       this.blueLUT = ((byte[])paramArrayOfByte3.clone());
/*  775 */       if (paramArrayOfByte4 != null) {
/*  776 */         this.alphaLUT = ((byte[])paramArrayOfByte4.clone());
/*      */       }
/*  778 */       this.bits = paramInt1;
/*  779 */       this.dataType = paramInt2;
/*      */ 
/*  781 */       if (paramArrayOfByte4 == null) {
/*  782 */         this.colorModel = new IndexColorModel(paramInt1, paramArrayOfByte1.length, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
/*      */       }
/*      */       else
/*      */       {
/*  788 */         this.colorModel = new IndexColorModel(paramInt1, paramArrayOfByte1.length, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4);
/*      */       }
/*      */ 
/*  796 */       if (((paramInt1 == 8) && (paramInt2 == 0)) || ((paramInt1 == 16) && ((paramInt2 == 2) || (paramInt2 == 1))))
/*      */       {
/*  800 */         int[] arrayOfInt = { 0 };
/*  801 */         this.sampleModel = new PixelInterleavedSampleModel(paramInt2, 1, 1, 1, 1, arrayOfInt);
/*      */       }
/*      */       else
/*      */       {
/*  806 */         this.sampleModel = new MultiPixelPackedSampleModel(paramInt2, 1, 1, paramInt1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Interleaved extends ImageTypeSpecifier
/*      */   {
/*      */     ColorSpace colorSpace;
/*      */     int[] bandOffsets;
/*      */     int dataType;
/*      */     boolean hasAlpha;
/*      */     boolean isAlphaPremultiplied;
/*      */ 
/*      */     public Interleaved(ColorSpace paramColorSpace, int[] paramArrayOfInt, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  276 */       super();
/*  277 */       if (paramColorSpace == null) {
/*  278 */         throw new IllegalArgumentException("colorSpace == null!");
/*      */       }
/*  280 */       if (paramArrayOfInt == null) {
/*  281 */         throw new IllegalArgumentException("bandOffsets == null!");
/*      */       }
/*  283 */       int i = paramColorSpace.getNumComponents() + (paramBoolean1 ? 1 : 0);
/*      */ 
/*  285 */       if (paramArrayOfInt.length != i) {
/*  286 */         throw new IllegalArgumentException("bandOffsets.length is wrong!");
/*      */       }
/*      */ 
/*  289 */       if ((paramInt != 0) && (paramInt != 2) && (paramInt != 1) && (paramInt != 3) && (paramInt != 4) && (paramInt != 5))
/*      */       {
/*  295 */         throw new IllegalArgumentException("Bad value for dataType!");
/*      */       }
/*      */ 
/*  298 */       this.colorSpace = paramColorSpace;
/*  299 */       this.bandOffsets = ((int[])paramArrayOfInt.clone());
/*  300 */       this.dataType = paramInt;
/*  301 */       this.hasAlpha = paramBoolean1;
/*  302 */       this.isAlphaPremultiplied = paramBoolean2;
/*      */ 
/*  304 */       this.colorModel = ImageTypeSpecifier.createComponentCM(paramColorSpace, paramArrayOfInt.length, paramInt, paramBoolean1, paramBoolean2);
/*      */ 
/*  311 */       int j = paramArrayOfInt[0];
/*  312 */       int k = j;
/*  313 */       for (int m = 0; m < paramArrayOfInt.length; m++) {
/*  314 */         n = paramArrayOfInt[m];
/*  315 */         j = Math.min(n, j);
/*  316 */         k = Math.max(n, k);
/*      */       }
/*  318 */       m = k - j + 1;
/*      */ 
/*  320 */       int n = 1;
/*  321 */       int i1 = 1;
/*  322 */       this.sampleModel = new PixelInterleavedSampleModel(paramInt, n, i1, m, n * m, paramArrayOfInt);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  331 */       if ((paramObject == null) || (!(paramObject instanceof Interleaved)))
/*      */       {
/*  333 */         return false;
/*      */       }
/*      */ 
/*  336 */       Interleaved localInterleaved = (Interleaved)paramObject;
/*      */ 
/*  339 */       if ((!this.colorSpace.equals(localInterleaved.colorSpace)) || (this.dataType != localInterleaved.dataType) || (this.hasAlpha != localInterleaved.hasAlpha) || (this.isAlphaPremultiplied != localInterleaved.isAlphaPremultiplied) || (this.bandOffsets.length != localInterleaved.bandOffsets.length))
/*      */       {
/*  344 */         return false;
/*      */       }
/*      */ 
/*  347 */       for (int i = 0; i < this.bandOffsets.length; i++) {
/*  348 */         if (this.bandOffsets[i] != localInterleaved.bandOffsets[i]) {
/*  349 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*  353 */       return true;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  357 */       return super.hashCode() + 4 * this.bandOffsets.length + 25 * this.dataType + (this.hasAlpha ? 17 : 18);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Packed extends ImageTypeSpecifier
/*      */   {
/*      */     ColorSpace colorSpace;
/*      */     int redMask;
/*      */     int greenMask;
/*      */     int blueMask;
/*      */     int alphaMask;
/*      */     int transferType;
/*      */     boolean isAlphaPremultiplied;
/*      */ 
/*      */     public Packed(ColorSpace paramColorSpace, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
/*      */     {
/*  151 */       super();
/*  152 */       if (paramColorSpace == null) {
/*  153 */         throw new IllegalArgumentException("colorSpace == null!");
/*      */       }
/*  155 */       if (paramColorSpace.getType() != 5) {
/*  156 */         throw new IllegalArgumentException("colorSpace is not of type TYPE_RGB!");
/*      */       }
/*      */ 
/*  159 */       if ((paramInt5 != 0) && (paramInt5 != 1) && (paramInt5 != 3))
/*      */       {
/*  162 */         throw new IllegalArgumentException("Bad value for transferType!");
/*      */       }
/*      */ 
/*  165 */       if ((paramInt1 == 0) && (paramInt2 == 0) && (paramInt3 == 0) && (paramInt4 == 0))
/*      */       {
/*  167 */         throw new IllegalArgumentException("No mask has at least 1 bit set!");
/*      */       }
/*      */ 
/*  170 */       this.colorSpace = paramColorSpace;
/*  171 */       this.redMask = paramInt1;
/*  172 */       this.greenMask = paramInt2;
/*  173 */       this.blueMask = paramInt3;
/*  174 */       this.alphaMask = paramInt4;
/*  175 */       this.transferType = paramInt5;
/*  176 */       this.isAlphaPremultiplied = paramBoolean;
/*      */ 
/*  178 */       int i = 32;
/*  179 */       this.colorModel = new DirectColorModel(paramColorSpace, i, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean, paramInt5);
/*      */ 
/*  185 */       this.sampleModel = this.colorModel.createCompatibleSampleModel(1, 1);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageTypeSpecifier
 * JD-Core Version:    0.6.2
 */