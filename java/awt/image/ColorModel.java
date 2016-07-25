/*      */ package java.awt.image;
/*      */ 
/*      */ import java.awt.Transparency;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.security.AccessController;
/*      */ import java.util.Collections;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import sun.java2d.cmm.CMSManager;
/*      */ import sun.java2d.cmm.ColorTransform;
/*      */ import sun.java2d.cmm.PCMM;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ 
/*      */ public abstract class ColorModel
/*      */   implements Transparency
/*      */ {
/*      */   private long pData;
/*      */   protected int pixel_bits;
/*      */   int[] nBits;
/*  164 */   int transparency = 3;
/*  165 */   boolean supportsAlpha = true;
/*  166 */   boolean isAlphaPremultiplied = false;
/*  167 */   int numComponents = -1;
/*  168 */   int numColorComponents = -1;
/*  169 */   ColorSpace colorSpace = ColorSpace.getInstance(1000);
/*  170 */   int colorSpaceType = 5;
/*      */   int maxBits;
/*  172 */   boolean is_sRGB = true;
/*      */   protected int transferType;
/*  203 */   private static boolean loaded = false;
/*      */   private static ColorModel RGBdefault;
/* 1689 */   static byte[] l8Tos8 = null;
/* 1690 */   static byte[] s8Tol8 = null;
/* 1691 */   static byte[] l16Tos8 = null;
/* 1692 */   static short[] s8Tol16 = null;
/*      */ 
/* 1695 */   static Map g8Tos8Map = null;
/* 1696 */   static Map lg16Toog8Map = null;
/* 1697 */   static Map g16Tos8Map = null;
/* 1698 */   static Map lg16Toog16Map = null;
/*      */ 
/*      */   static void loadLibraries()
/*      */   {
/*  205 */     if (!loaded) {
/*  206 */       AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*      */ 
/*  208 */       loaded = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public static ColorModel getRGBdefault()
/*      */   {
/*  236 */     if (RGBdefault == null) {
/*  237 */       RGBdefault = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
/*      */     }
/*      */ 
/*  244 */     return RGBdefault;
/*      */   }
/*      */ 
/*      */   public ColorModel(int paramInt)
/*      */   {
/*  268 */     this.pixel_bits = paramInt;
/*  269 */     if (paramInt < 1) {
/*  270 */       throw new IllegalArgumentException("Number of bits must be > 0");
/*      */     }
/*  272 */     this.numComponents = 4;
/*  273 */     this.numColorComponents = 3;
/*  274 */     this.maxBits = paramInt;
/*      */ 
/*  276 */     this.transferType = getDefaultTransferType(paramInt);
/*      */   }
/*      */ 
/*      */   protected ColorModel(int paramInt1, int[] paramArrayOfInt, ColorSpace paramColorSpace, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
/*      */   {
/*  330 */     this.colorSpace = paramColorSpace;
/*  331 */     this.colorSpaceType = paramColorSpace.getType();
/*  332 */     this.numColorComponents = paramColorSpace.getNumComponents();
/*  333 */     this.numComponents = (this.numColorComponents + (paramBoolean1 ? 1 : 0));
/*  334 */     this.supportsAlpha = paramBoolean1;
/*  335 */     if (paramArrayOfInt.length < this.numComponents) {
/*  336 */       throw new IllegalArgumentException("Number of color/alpha components should be " + this.numComponents + " but length of bits array is " + paramArrayOfInt.length);
/*      */     }
/*      */ 
/*  344 */     if ((paramInt2 < 1) || (paramInt2 > 3))
/*      */     {
/*  347 */       throw new IllegalArgumentException("Unknown transparency: " + paramInt2);
/*      */     }
/*      */ 
/*  351 */     if (!this.supportsAlpha) {
/*  352 */       this.isAlphaPremultiplied = false;
/*  353 */       this.transparency = 1;
/*      */     }
/*      */     else {
/*  356 */       this.isAlphaPremultiplied = paramBoolean2;
/*  357 */       this.transparency = paramInt2;
/*      */     }
/*      */ 
/*  360 */     this.nBits = ((int[])paramArrayOfInt.clone());
/*  361 */     this.pixel_bits = paramInt1;
/*  362 */     if (paramInt1 <= 0) {
/*  363 */       throw new IllegalArgumentException("Number of pixel bits must be > 0");
/*      */     }
/*      */ 
/*  367 */     this.maxBits = 0;
/*  368 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/*      */     {
/*  370 */       if (paramArrayOfInt[i] < 0) {
/*  371 */         throw new IllegalArgumentException("Number of bits must be >= 0");
/*      */       }
/*      */ 
/*  374 */       if (this.maxBits < paramArrayOfInt[i]) {
/*  375 */         this.maxBits = paramArrayOfInt[i];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  380 */     if (this.maxBits == 0) {
/*  381 */       throw new IllegalArgumentException("There must be at least one component with > 0 pixel bits.");
/*      */     }
/*      */ 
/*  387 */     if (paramColorSpace != ColorSpace.getInstance(1000)) {
/*  388 */       this.is_sRGB = false;
/*      */     }
/*      */ 
/*  392 */     this.transferType = paramInt3;
/*      */   }
/*      */ 
/*      */   public final boolean hasAlpha()
/*      */   {
/*  402 */     return this.supportsAlpha;
/*      */   }
/*      */ 
/*      */   public final boolean isAlphaPremultiplied()
/*      */   {
/*  418 */     return this.isAlphaPremultiplied;
/*      */   }
/*      */ 
/*      */   public final int getTransferType()
/*      */   {
/*  429 */     return this.transferType;
/*      */   }
/*      */ 
/*      */   public int getPixelSize()
/*      */   {
/*  438 */     return this.pixel_bits;
/*      */   }
/*      */ 
/*      */   public int getComponentSize(int paramInt)
/*      */   {
/*  461 */     if (this.nBits == null) {
/*  462 */       throw new NullPointerException("Number of bits array is null.");
/*      */     }
/*      */ 
/*  465 */     return this.nBits[paramInt];
/*      */   }
/*      */ 
/*      */   public int[] getComponentSize()
/*      */   {
/*  476 */     if (this.nBits != null) {
/*  477 */       return (int[])this.nBits.clone();
/*      */     }
/*      */ 
/*  480 */     return null;
/*      */   }
/*      */ 
/*      */   public int getTransparency()
/*      */   {
/*  492 */     return this.transparency;
/*      */   }
/*      */ 
/*      */   public int getNumComponents()
/*      */   {
/*  502 */     return this.numComponents;
/*      */   }
/*      */ 
/*      */   public int getNumColorComponents()
/*      */   {
/*  515 */     return this.numColorComponents;
/*      */   }
/*      */ 
/*      */   public abstract int getRed(int paramInt);
/*      */ 
/*      */   public abstract int getGreen(int paramInt);
/*      */ 
/*      */   public abstract int getBlue(int paramInt);
/*      */ 
/*      */   public abstract int getAlpha(int paramInt);
/*      */ 
/*      */   public int getRGB(int paramInt)
/*      */   {
/*  590 */     return getAlpha(paramInt) << 24 | getRed(paramInt) << 16 | getGreen(paramInt) << 8 | getBlue(paramInt) << 0;
/*      */   }
/*      */ 
/*      */   public int getRed(Object paramObject)
/*      */   {
/*  633 */     int i = 0; int j = 0;
/*  634 */     switch (this.transferType) {
/*      */     case 0:
/*  636 */       byte[] arrayOfByte = (byte[])paramObject;
/*  637 */       i = arrayOfByte[0] & 0xFF;
/*  638 */       j = arrayOfByte.length;
/*  639 */       break;
/*      */     case 1:
/*  641 */       short[] arrayOfShort = (short[])paramObject;
/*  642 */       i = arrayOfShort[0] & 0xFFFF;
/*  643 */       j = arrayOfShort.length;
/*  644 */       break;
/*      */     case 3:
/*  646 */       int[] arrayOfInt = (int[])paramObject;
/*  647 */       i = arrayOfInt[0];
/*  648 */       j = arrayOfInt.length;
/*  649 */       break;
/*      */     case 2:
/*      */     default:
/*  651 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  654 */     if (j == 1) {
/*  655 */       return getRed(i);
/*      */     }
/*      */ 
/*  658 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public int getGreen(Object paramObject)
/*      */   {
/*  700 */     int i = 0; int j = 0;
/*  701 */     switch (this.transferType) {
/*      */     case 0:
/*  703 */       byte[] arrayOfByte = (byte[])paramObject;
/*  704 */       i = arrayOfByte[0] & 0xFF;
/*  705 */       j = arrayOfByte.length;
/*  706 */       break;
/*      */     case 1:
/*  708 */       short[] arrayOfShort = (short[])paramObject;
/*  709 */       i = arrayOfShort[0] & 0xFFFF;
/*  710 */       j = arrayOfShort.length;
/*  711 */       break;
/*      */     case 3:
/*  713 */       int[] arrayOfInt = (int[])paramObject;
/*  714 */       i = arrayOfInt[0];
/*  715 */       j = arrayOfInt.length;
/*  716 */       break;
/*      */     case 2:
/*      */     default:
/*  718 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  721 */     if (j == 1) {
/*  722 */       return getGreen(i);
/*      */     }
/*      */ 
/*  725 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public int getBlue(Object paramObject)
/*      */   {
/*  767 */     int i = 0; int j = 0;
/*  768 */     switch (this.transferType) {
/*      */     case 0:
/*  770 */       byte[] arrayOfByte = (byte[])paramObject;
/*  771 */       i = arrayOfByte[0] & 0xFF;
/*  772 */       j = arrayOfByte.length;
/*  773 */       break;
/*      */     case 1:
/*  775 */       short[] arrayOfShort = (short[])paramObject;
/*  776 */       i = arrayOfShort[0] & 0xFFFF;
/*  777 */       j = arrayOfShort.length;
/*  778 */       break;
/*      */     case 3:
/*  780 */       int[] arrayOfInt = (int[])paramObject;
/*  781 */       i = arrayOfInt[0];
/*  782 */       j = arrayOfInt.length;
/*  783 */       break;
/*      */     case 2:
/*      */     default:
/*  785 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  788 */     if (j == 1) {
/*  789 */       return getBlue(i);
/*      */     }
/*      */ 
/*  792 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public int getAlpha(Object paramObject)
/*      */   {
/*  830 */     int i = 0; int j = 0;
/*  831 */     switch (this.transferType) {
/*      */     case 0:
/*  833 */       byte[] arrayOfByte = (byte[])paramObject;
/*  834 */       i = arrayOfByte[0] & 0xFF;
/*  835 */       j = arrayOfByte.length;
/*  836 */       break;
/*      */     case 1:
/*  838 */       short[] arrayOfShort = (short[])paramObject;
/*  839 */       i = arrayOfShort[0] & 0xFFFF;
/*  840 */       j = arrayOfShort.length;
/*  841 */       break;
/*      */     case 3:
/*  843 */       int[] arrayOfInt = (int[])paramObject;
/*  844 */       i = arrayOfInt[0];
/*  845 */       j = arrayOfInt.length;
/*  846 */       break;
/*      */     case 2:
/*      */     default:
/*  848 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/*  851 */     if (j == 1) {
/*  852 */       return getAlpha(i);
/*      */     }
/*      */ 
/*  855 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public int getRGB(Object paramObject)
/*      */   {
/*  878 */     return getAlpha(paramObject) << 24 | getRed(paramObject) << 16 | getGreen(paramObject) << 8 | getBlue(paramObject) << 0;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt, Object paramObject)
/*      */   {
/*  922 */     throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */   }
/*      */ 
/*      */   public int[] getComponents(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/*  959 */     throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */   }
/*      */ 
/*      */   public int[] getComponents(Object paramObject, int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1001 */     throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */   }
/*      */ 
/*      */   public int[] getUnnormalizedComponents(float[] paramArrayOfFloat, int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/* 1056 */     if (this.colorSpace == null) {
/* 1057 */       throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */     }
/*      */ 
/* 1061 */     if (this.nBits == null) {
/* 1062 */       throw new UnsupportedOperationException("This method is not supported.  Unable to determine #bits per component.");
/*      */     }
/*      */ 
/* 1066 */     if (paramArrayOfFloat.length - paramInt1 < this.numComponents) {
/* 1067 */       throw new IllegalArgumentException("Incorrect number of components.  Expecting " + this.numComponents);
/*      */     }
/*      */ 
/* 1073 */     if (paramArrayOfInt == null) {
/* 1074 */       paramArrayOfInt = new int[paramInt2 + this.numComponents];
/*      */     }
/*      */ 
/* 1077 */     if ((this.supportsAlpha) && (this.isAlphaPremultiplied)) {
/* 1078 */       float f = paramArrayOfFloat[(paramInt1 + this.numColorComponents)];
/* 1079 */       for (int j = 0; j < this.numColorComponents; j++) {
/* 1080 */         paramArrayOfInt[(paramInt2 + j)] = ((int)(paramArrayOfFloat[(paramInt1 + j)] * ((1 << this.nBits[j]) - 1) * f + 0.5F));
/*      */       }
/*      */ 
/* 1084 */       paramArrayOfInt[(paramInt2 + this.numColorComponents)] = ((int)(f * ((1 << this.nBits[this.numColorComponents]) - 1) + 0.5F));
/*      */     }
/*      */     else
/*      */     {
/* 1088 */       for (int i = 0; i < this.numComponents; i++) {
/* 1089 */         paramArrayOfInt[(paramInt2 + i)] = ((int)(paramArrayOfFloat[(paramInt1 + i)] * ((1 << this.nBits[i]) - 1) + 0.5F));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1094 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public float[] getNormalizedComponents(int[] paramArrayOfInt, int paramInt1, float[] paramArrayOfFloat, int paramInt2)
/*      */   {
/* 1152 */     if (this.colorSpace == null) {
/* 1153 */       throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */     }
/*      */ 
/* 1156 */     if (this.nBits == null) {
/* 1157 */       throw new UnsupportedOperationException("This method is not supported.  Unable to determine #bits per component.");
/*      */     }
/*      */ 
/* 1162 */     if (paramArrayOfInt.length - paramInt1 < this.numComponents) {
/* 1163 */       throw new IllegalArgumentException("Incorrect number of components.  Expecting " + this.numComponents);
/*      */     }
/*      */ 
/* 1169 */     if (paramArrayOfFloat == null) {
/* 1170 */       paramArrayOfFloat = new float[this.numComponents + paramInt2];
/*      */     }
/*      */ 
/* 1173 */     if ((this.supportsAlpha) && (this.isAlphaPremultiplied))
/*      */     {
/* 1175 */       float f = paramArrayOfInt[(paramInt1 + this.numColorComponents)];
/* 1176 */       f /= ((1 << this.nBits[this.numColorComponents]) - 1);
/*      */       int j;
/* 1177 */       if (f != 0.0F) {
/* 1178 */         for (j = 0; j < this.numColorComponents; j++) {
/* 1179 */           paramArrayOfFloat[(paramInt2 + j)] = (paramArrayOfInt[(paramInt1 + j)] / (f * ((1 << this.nBits[j]) - 1)));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1184 */         for (j = 0; j < this.numColorComponents; j++) {
/* 1185 */           paramArrayOfFloat[(paramInt2 + j)] = 0.0F;
/*      */         }
/*      */       }
/* 1188 */       paramArrayOfFloat[(paramInt2 + this.numColorComponents)] = f;
/*      */     }
/*      */     else {
/* 1191 */       for (int i = 0; i < this.numComponents; i++) {
/* 1192 */         paramArrayOfFloat[(paramInt2 + i)] = (paramArrayOfInt[(paramInt1 + i)] / ((1 << this.nBits[i]) - 1));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1197 */     return paramArrayOfFloat;
/*      */   }
/*      */ 
/*      */   public int getDataElement(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1236 */     throw new UnsupportedOperationException("This method is not supported by this color model.");
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int[] paramArrayOfInt, int paramInt, Object paramObject)
/*      */   {
/* 1287 */     throw new UnsupportedOperationException("This method has not been implemented for this color model.");
/*      */   }
/*      */ 
/*      */   public int getDataElement(float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 1324 */     int[] arrayOfInt = getUnnormalizedComponents(paramArrayOfFloat, paramInt, null, 0);
/*      */ 
/* 1326 */     return getDataElement(arrayOfInt, 0);
/*      */   }
/*      */ 
/*      */   public Object getDataElements(float[] paramArrayOfFloat, int paramInt, Object paramObject)
/*      */   {
/* 1372 */     int[] arrayOfInt = getUnnormalizedComponents(paramArrayOfFloat, paramInt, null, 0);
/*      */ 
/* 1374 */     return getDataElements(arrayOfInt, 0, paramObject);
/*      */   }
/*      */ 
/*      */   public float[] getNormalizedComponents(Object paramObject, float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 1433 */     int[] arrayOfInt = getComponents(paramObject, null, 0);
/* 1434 */     return getNormalizedComponents(arrayOfInt, 0, paramArrayOfFloat, paramInt);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1448 */     if (!(paramObject instanceof ColorModel)) {
/* 1449 */       return false;
/*      */     }
/* 1451 */     ColorModel localColorModel = (ColorModel)paramObject;
/*      */ 
/* 1453 */     if (this == localColorModel) {
/* 1454 */       return true;
/*      */     }
/* 1456 */     if ((this.supportsAlpha != localColorModel.hasAlpha()) || (this.isAlphaPremultiplied != localColorModel.isAlphaPremultiplied()) || (this.pixel_bits != localColorModel.getPixelSize()) || (this.transparency != localColorModel.getTransparency()) || (this.numComponents != localColorModel.getNumComponents()))
/*      */     {
/* 1462 */       return false;
/*      */     }
/*      */ 
/* 1465 */     int[] arrayOfInt = localColorModel.getComponentSize();
/*      */ 
/* 1467 */     if ((this.nBits != null) && (arrayOfInt != null)) {
/* 1468 */       for (int i = 0; i < this.numComponents; i++) {
/* 1469 */         if (this.nBits[i] != arrayOfInt[i])
/* 1470 */           return false;
/*      */       }
/*      */     }
/*      */     else {
/* 1474 */       return (this.nBits == null) && (arrayOfInt == null);
/*      */     }
/*      */ 
/* 1477 */     return true;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1487 */     int i = 0;
/*      */ 
/* 1489 */     i = (this.supportsAlpha ? 2 : 3) + (this.isAlphaPremultiplied ? 4 : 5) + this.pixel_bits * 6 + this.transparency * 7 + this.numComponents * 8;
/*      */ 
/* 1495 */     if (this.nBits != null) {
/* 1496 */       for (int j = 0; j < this.numComponents; j++) {
/* 1497 */         i += this.nBits[j] * (j + 9);
/*      */       }
/*      */     }
/*      */ 
/* 1501 */     return i;
/*      */   }
/*      */ 
/*      */   public final ColorSpace getColorSpace()
/*      */   {
/* 1511 */     return this.colorSpace;
/*      */   }
/*      */ 
/*      */   public ColorModel coerceData(WritableRaster paramWritableRaster, boolean paramBoolean)
/*      */   {
/* 1537 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleRaster(Raster paramRaster)
/*      */   {
/* 1557 */     throw new UnsupportedOperationException("This method has not been implemented for this ColorModel.");
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/* 1579 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*      */   {
/* 1600 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleSampleModel(SampleModel paramSampleModel)
/*      */   {
/* 1619 */     throw new UnsupportedOperationException("This method is not supported by this color model");
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public WritableRaster getAlphaRaster(WritableRaster paramWritableRaster)
/*      */   {
/* 1658 */     return null;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1668 */     return new String("ColorModel: #pixelBits = " + this.pixel_bits + " numComponents = " + this.numComponents + " color space = " + this.colorSpace + " transparency = " + this.transparency + " has alpha = " + this.supportsAlpha + " isAlphaPre = " + this.isAlphaPremultiplied);
/*      */   }
/*      */ 
/*      */   static int getDefaultTransferType(int paramInt)
/*      */   {
/* 1678 */     if (paramInt <= 8)
/* 1679 */       return 0;
/* 1680 */     if (paramInt <= 16)
/* 1681 */       return 1;
/* 1682 */     if (paramInt <= 32) {
/* 1683 */       return 3;
/*      */     }
/* 1685 */     return 32;
/*      */   }
/*      */ 
/*      */   static boolean isLinearRGBspace(ColorSpace paramColorSpace)
/*      */   {
/* 1703 */     return paramColorSpace == CMSManager.LINEAR_RGBspace;
/*      */   }
/*      */ 
/*      */   static boolean isLinearGRAYspace(ColorSpace paramColorSpace)
/*      */   {
/* 1709 */     return paramColorSpace == CMSManager.GRAYspace;
/*      */   }
/*      */ 
/*      */   static byte[] getLinearRGB8TosRGB8LUT() {
/* 1713 */     if (l8Tos8 == null) {
/* 1714 */       l8Tos8 = new byte[256];
/*      */ 
/* 1721 */       for (int i = 0; i <= 255; i++) {
/* 1722 */         float f1 = i / 255.0F;
/*      */         float f2;
/* 1723 */         if (f1 <= 0.0031308F)
/* 1724 */           f2 = f1 * 12.92F;
/*      */         else {
/* 1726 */           f2 = 1.055F * (float)Math.pow(f1, 0.4166666666666667D) - 0.055F;
/*      */         }
/*      */ 
/* 1729 */         l8Tos8[i] = ((byte)Math.round(f2 * 255.0F));
/*      */       }
/*      */     }
/* 1732 */     return l8Tos8;
/*      */   }
/*      */ 
/*      */   static byte[] getsRGB8ToLinearRGB8LUT() {
/* 1736 */     if (s8Tol8 == null) {
/* 1737 */       s8Tol8 = new byte[256];
/*      */ 
/* 1740 */       for (int i = 0; i <= 255; i++) {
/* 1741 */         float f1 = i / 255.0F;
/*      */         float f2;
/* 1742 */         if (f1 <= 0.04045F)
/* 1743 */           f2 = f1 / 12.92F;
/*      */         else {
/* 1745 */           f2 = (float)Math.pow((f1 + 0.055F) / 1.055F, 2.4D);
/*      */         }
/* 1747 */         s8Tol8[i] = ((byte)Math.round(f2 * 255.0F));
/*      */       }
/*      */     }
/* 1750 */     return s8Tol8;
/*      */   }
/*      */ 
/*      */   static byte[] getLinearRGB16TosRGB8LUT() {
/* 1754 */     if (l16Tos8 == null) {
/* 1755 */       l16Tos8 = new byte[65536];
/*      */ 
/* 1758 */       for (int i = 0; i <= 65535; i++) {
/* 1759 */         float f1 = i / 65535.0F;
/*      */         float f2;
/* 1760 */         if (f1 <= 0.0031308F)
/* 1761 */           f2 = f1 * 12.92F;
/*      */         else {
/* 1763 */           f2 = 1.055F * (float)Math.pow(f1, 0.4166666666666667D) - 0.055F;
/*      */         }
/*      */ 
/* 1766 */         l16Tos8[i] = ((byte)Math.round(f2 * 255.0F));
/*      */       }
/*      */     }
/* 1769 */     return l16Tos8;
/*      */   }
/*      */ 
/*      */   static short[] getsRGB8ToLinearRGB16LUT() {
/* 1773 */     if (s8Tol16 == null) {
/* 1774 */       s8Tol16 = new short[256];
/*      */ 
/* 1777 */       for (int i = 0; i <= 255; i++) {
/* 1778 */         float f1 = i / 255.0F;
/*      */         float f2;
/* 1779 */         if (f1 <= 0.04045F)
/* 1780 */           f2 = f1 / 12.92F;
/*      */         else {
/* 1782 */           f2 = (float)Math.pow((f1 + 0.055F) / 1.055F, 2.4D);
/*      */         }
/* 1784 */         s8Tol16[i] = ((short)Math.round(f2 * 65535.0F));
/*      */       }
/*      */     }
/* 1787 */     return s8Tol16;
/*      */   }
/*      */ 
/*      */   static byte[] getGray8TosRGB8LUT(ICC_ColorSpace paramICC_ColorSpace)
/*      */   {
/* 1798 */     if (isLinearGRAYspace(paramICC_ColorSpace)) {
/* 1799 */       return getLinearRGB8TosRGB8LUT();
/*      */     }
/* 1801 */     if (g8Tos8Map != null) {
/* 1802 */       arrayOfByte1 = (byte[])g8Tos8Map.get(paramICC_ColorSpace);
/* 1803 */       if (arrayOfByte1 != null) {
/* 1804 */         return arrayOfByte1;
/*      */       }
/*      */     }
/* 1807 */     byte[] arrayOfByte1 = new byte[256];
/* 1808 */     for (int i = 0; i <= 255; i++) {
/* 1809 */       arrayOfByte1[i] = ((byte)i);
/*      */     }
/* 1811 */     ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 1812 */     PCMM localPCMM = CMSManager.getModule();
/* 1813 */     ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1000);
/*      */ 
/* 1815 */     arrayOfColorTransform[0] = localPCMM.createTransform(paramICC_ColorSpace.getProfile(), -1, 1);
/*      */ 
/* 1817 */     arrayOfColorTransform[1] = localPCMM.createTransform(localICC_ColorSpace.getProfile(), -1, 2);
/*      */ 
/* 1819 */     ColorTransform localColorTransform = localPCMM.createTransform(arrayOfColorTransform);
/* 1820 */     byte[] arrayOfByte2 = localColorTransform.colorConvert(arrayOfByte1, null);
/* 1821 */     int j = 0; for (int k = 2; j <= 255; k += 3)
/*      */     {
/* 1827 */       arrayOfByte1[j] = arrayOfByte2[k];
/*      */ 
/* 1821 */       j++;
/*      */     }
/*      */ 
/* 1829 */     if (g8Tos8Map == null) {
/* 1830 */       g8Tos8Map = Collections.synchronizedMap(new WeakHashMap(2));
/*      */     }
/* 1832 */     g8Tos8Map.put(paramICC_ColorSpace, arrayOfByte1);
/* 1833 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   static byte[] getLinearGray16ToOtherGray8LUT(ICC_ColorSpace paramICC_ColorSpace)
/*      */   {
/* 1842 */     if (lg16Toog8Map != null) {
/* 1843 */       localObject = (byte[])lg16Toog8Map.get(paramICC_ColorSpace);
/* 1844 */       if (localObject != null) {
/* 1845 */         return localObject;
/*      */       }
/*      */     }
/* 1848 */     Object localObject = new short[65536];
/* 1849 */     for (int i = 0; i <= 65535; i++) {
/* 1850 */       localObject[i] = ((short)i);
/*      */     }
/* 1852 */     ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 1853 */     PCMM localPCMM = CMSManager.getModule();
/* 1854 */     ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1003);
/*      */ 
/* 1856 */     arrayOfColorTransform[0] = localPCMM.createTransform(localICC_ColorSpace.getProfile(), -1, 1);
/*      */ 
/* 1858 */     arrayOfColorTransform[1] = localPCMM.createTransform(paramICC_ColorSpace.getProfile(), -1, 2);
/*      */ 
/* 1860 */     ColorTransform localColorTransform = localPCMM.createTransform(arrayOfColorTransform);
/* 1861 */     localObject = localColorTransform.colorConvert((short[])localObject, null);
/* 1862 */     byte[] arrayOfByte = new byte[65536];
/* 1863 */     for (int j = 0; j <= 65535; j++)
/*      */     {
/* 1865 */       arrayOfByte[j] = ((byte)(int)((localObject[j] & 0xFFFF) * 0.003891051F + 0.5F));
/*      */     }
/*      */ 
/* 1868 */     if (lg16Toog8Map == null) {
/* 1869 */       lg16Toog8Map = Collections.synchronizedMap(new WeakHashMap(2));
/*      */     }
/* 1871 */     lg16Toog8Map.put(paramICC_ColorSpace, arrayOfByte);
/* 1872 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static byte[] getGray16TosRGB8LUT(ICC_ColorSpace paramICC_ColorSpace)
/*      */   {
/* 1883 */     if (isLinearGRAYspace(paramICC_ColorSpace)) {
/* 1884 */       return getLinearRGB16TosRGB8LUT();
/*      */     }
/* 1886 */     if (g16Tos8Map != null) {
/* 1887 */       localObject = (byte[])g16Tos8Map.get(paramICC_ColorSpace);
/* 1888 */       if (localObject != null) {
/* 1889 */         return localObject;
/*      */       }
/*      */     }
/* 1892 */     Object localObject = new short[65536];
/* 1893 */     for (int i = 0; i <= 65535; i++) {
/* 1894 */       localObject[i] = ((short)i);
/*      */     }
/* 1896 */     ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 1897 */     PCMM localPCMM = CMSManager.getModule();
/* 1898 */     ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1000);
/*      */ 
/* 1900 */     arrayOfColorTransform[0] = localPCMM.createTransform(paramICC_ColorSpace.getProfile(), -1, 1);
/*      */ 
/* 1902 */     arrayOfColorTransform[1] = localPCMM.createTransform(localICC_ColorSpace.getProfile(), -1, 2);
/*      */ 
/* 1904 */     ColorTransform localColorTransform = localPCMM.createTransform(arrayOfColorTransform);
/* 1905 */     localObject = localColorTransform.colorConvert((short[])localObject, null);
/* 1906 */     byte[] arrayOfByte = new byte[65536];
/* 1907 */     int j = 0; for (int k = 2; j <= 65535; k += 3)
/*      */     {
/* 1915 */       arrayOfByte[j] = ((byte)(int)((localObject[k] & 0xFFFF) * 0.003891051F + 0.5F));
/*      */ 
/* 1907 */       j++;
/*      */     }
/*      */ 
/* 1918 */     if (g16Tos8Map == null) {
/* 1919 */       g16Tos8Map = Collections.synchronizedMap(new WeakHashMap(2));
/*      */     }
/* 1921 */     g16Tos8Map.put(paramICC_ColorSpace, arrayOfByte);
/* 1922 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static short[] getLinearGray16ToOtherGray16LUT(ICC_ColorSpace paramICC_ColorSpace)
/*      */   {
/* 1931 */     if (lg16Toog16Map != null) {
/* 1932 */       arrayOfShort1 = (short[])lg16Toog16Map.get(paramICC_ColorSpace);
/* 1933 */       if (arrayOfShort1 != null) {
/* 1934 */         return arrayOfShort1;
/*      */       }
/*      */     }
/* 1937 */     short[] arrayOfShort1 = new short[65536];
/* 1938 */     for (int i = 0; i <= 65535; i++) {
/* 1939 */       arrayOfShort1[i] = ((short)i);
/*      */     }
/* 1941 */     ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 1942 */     PCMM localPCMM = CMSManager.getModule();
/* 1943 */     ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1003);
/*      */ 
/* 1945 */     arrayOfColorTransform[0] = localPCMM.createTransform(localICC_ColorSpace.getProfile(), -1, 1);
/*      */ 
/* 1947 */     arrayOfColorTransform[1] = localPCMM.createTransform(paramICC_ColorSpace.getProfile(), -1, 2);
/*      */ 
/* 1949 */     ColorTransform localColorTransform = localPCMM.createTransform(arrayOfColorTransform);
/*      */ 
/* 1951 */     short[] arrayOfShort2 = localColorTransform.colorConvert(arrayOfShort1, null);
/* 1952 */     if (lg16Toog16Map == null) {
/* 1953 */       lg16Toog16Map = Collections.synchronizedMap(new WeakHashMap(2));
/*      */     }
/* 1955 */     lg16Toog16Map.put(paramICC_ColorSpace, arrayOfShort2);
/* 1956 */     return arrayOfShort2;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  214 */     loadLibraries();
/*  215 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ColorModel
 * JD-Core Version:    0.6.2
 */