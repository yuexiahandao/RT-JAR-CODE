/*      */ package java.awt.image;
/*      */ 
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Arrays;
/*      */ import sun.awt.image.BufImgSurfaceData.ICMColorData;
/*      */ 
/*      */ public class IndexColorModel extends ColorModel
/*      */ {
/*      */   private int[] rgb;
/*      */   private int map_size;
/*      */   private int pixel_mask;
/*  128 */   private int transparent_index = -1;
/*      */   private boolean allgrayopaque;
/*      */   private BigInteger validBits;
/*  132 */   private BufImgSurfaceData.ICMColorData colorData = null;
/*      */ 
/*  134 */   private static int[] opaqueBits = { 8, 8, 8 };
/*  135 */   private static int[] alphaBits = { 8, 8, 8, 8 };
/*      */   private static final int CACHESIZE = 40;
/*  841 */   private int[] lookupcache = new int[40];
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*      */   {
/*  167 */     super(paramInt1, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(paramInt1));
/*      */ 
/*  171 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  172 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  175 */     setRGBs(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, null);
/*  176 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt3)
/*      */   {
/*  207 */     super(paramInt1, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(paramInt1));
/*      */ 
/*  211 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  212 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  215 */     setRGBs(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, null);
/*  216 */     setTransparentPixel(paramInt3);
/*  217 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4)
/*      */   {
/*  246 */     super(paramInt1, alphaBits, ColorSpace.getInstance(1000), true, false, 3, ColorModel.getDefaultTransferType(paramInt1));
/*      */ 
/*  250 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  251 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  254 */     setRGBs(paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4);
/*  255 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, boolean paramBoolean)
/*      */   {
/*  286 */     this(paramInt1, paramInt2, paramArrayOfByte, paramInt3, paramBoolean, -1);
/*  287 */     if ((paramInt1 < 1) || (paramInt1 > 16))
/*  288 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, boolean paramBoolean, int paramInt4)
/*      */   {
/*  324 */     super(paramInt1, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, ColorModel.getDefaultTransferType(paramInt1));
/*      */ 
/*  329 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  330 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  333 */     if (paramInt2 < 1) {
/*  334 */       throw new IllegalArgumentException("Map size (" + paramInt2 + ") must be >= 1");
/*      */     }
/*      */ 
/*  337 */     this.map_size = paramInt2;
/*  338 */     this.rgb = new int[calcRealMapSize(paramInt1, paramInt2)];
/*  339 */     int i = paramInt3;
/*  340 */     int j = 255;
/*  341 */     boolean bool = true;
/*  342 */     int k = 1;
/*  343 */     for (int m = 0; m < paramInt2; m++) {
/*  344 */       int n = paramArrayOfByte[(i++)] & 0xFF;
/*  345 */       int i1 = paramArrayOfByte[(i++)] & 0xFF;
/*  346 */       int i2 = paramArrayOfByte[(i++)] & 0xFF;
/*  347 */       bool = (bool) && (n == i1) && (i1 == i2);
/*  348 */       if (paramBoolean) {
/*  349 */         j = paramArrayOfByte[(i++)] & 0xFF;
/*  350 */         if (j != 255) {
/*  351 */           if (j == 0) {
/*  352 */             if (k == 1) {
/*  353 */               k = 2;
/*      */             }
/*  355 */             if (this.transparent_index < 0)
/*  356 */               this.transparent_index = m;
/*      */           }
/*      */           else {
/*  359 */             k = 3;
/*      */           }
/*  361 */           bool = false;
/*      */         }
/*      */       }
/*  364 */       this.rgb[m] = (j << 24 | n << 16 | i1 << 8 | i2);
/*      */     }
/*  366 */     this.allgrayopaque = bool;
/*  367 */     setTransparency(k);
/*  368 */     setTransparentPixel(paramInt4);
/*  369 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5)
/*      */   {
/*  409 */     super(paramInt1, opaqueBits, ColorSpace.getInstance(1000), false, false, 1, paramInt5);
/*      */ 
/*  414 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  415 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  418 */     if (paramInt2 < 1) {
/*  419 */       throw new IllegalArgumentException("Map size (" + paramInt2 + ") must be >= 1");
/*      */     }
/*      */ 
/*  422 */     if ((paramInt5 != 0) && (paramInt5 != 1))
/*      */     {
/*  424 */       throw new IllegalArgumentException("transferType must be eitherDataBuffer.TYPE_BYTE or DataBuffer.TYPE_USHORT");
/*      */     }
/*      */ 
/*  428 */     setRGBs(paramInt2, paramArrayOfInt, paramInt3, paramBoolean);
/*  429 */     setTransparentPixel(paramInt4);
/*  430 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   public IndexColorModel(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, BigInteger paramBigInteger)
/*      */   {
/*  474 */     super(paramInt1, alphaBits, ColorSpace.getInstance(1000), true, false, 3, paramInt4);
/*      */ 
/*  479 */     if ((paramInt1 < 1) || (paramInt1 > 16)) {
/*  480 */       throw new IllegalArgumentException("Number of bits must be between 1 and 16.");
/*      */     }
/*      */ 
/*  483 */     if (paramInt2 < 1) {
/*  484 */       throw new IllegalArgumentException("Map size (" + paramInt2 + ") must be >= 1");
/*      */     }
/*      */ 
/*  487 */     if ((paramInt4 != 0) && (paramInt4 != 1))
/*      */     {
/*  489 */       throw new IllegalArgumentException("transferType must be eitherDataBuffer.TYPE_BYTE or DataBuffer.TYPE_USHORT");
/*      */     }
/*      */ 
/*  493 */     if (paramBigInteger != null)
/*      */     {
/*  495 */       for (int i = 0; i < paramInt2; i++) {
/*  496 */         if (!paramBigInteger.testBit(i)) {
/*  497 */           this.validBits = paramBigInteger;
/*  498 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  503 */     setRGBs(paramInt2, paramArrayOfInt, paramInt3, true);
/*  504 */     calculatePixelMask();
/*      */   }
/*      */ 
/*      */   private void setRGBs(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4) {
/*  508 */     if (paramInt < 1) {
/*  509 */       throw new IllegalArgumentException("Map size (" + paramInt + ") must be >= 1");
/*      */     }
/*      */ 
/*  512 */     this.map_size = paramInt;
/*  513 */     this.rgb = new int[calcRealMapSize(this.pixel_bits, paramInt)];
/*  514 */     int i = 255;
/*  515 */     int j = 1;
/*  516 */     boolean bool = true;
/*  517 */     for (int k = 0; k < paramInt; k++) {
/*  518 */       int m = paramArrayOfByte1[k] & 0xFF;
/*  519 */       int n = paramArrayOfByte2[k] & 0xFF;
/*  520 */       int i1 = paramArrayOfByte3[k] & 0xFF;
/*  521 */       bool = (bool) && (m == n) && (n == i1);
/*  522 */       if (paramArrayOfByte4 != null) {
/*  523 */         i = paramArrayOfByte4[k] & 0xFF;
/*  524 */         if (i != 255) {
/*  525 */           if (i == 0) {
/*  526 */             if (j == 1) {
/*  527 */               j = 2;
/*      */             }
/*  529 */             if (this.transparent_index < 0)
/*  530 */               this.transparent_index = k;
/*      */           }
/*      */           else {
/*  533 */             j = 3;
/*      */           }
/*  535 */           bool = false;
/*      */         }
/*      */       }
/*  538 */       this.rgb[k] = (i << 24 | m << 16 | n << 8 | i1);
/*      */     }
/*  540 */     this.allgrayopaque = bool;
/*  541 */     setTransparency(j);
/*      */   }
/*      */ 
/*      */   private void setRGBs(int paramInt1, int[] paramArrayOfInt, int paramInt2, boolean paramBoolean) {
/*  545 */     this.map_size = paramInt1;
/*  546 */     this.rgb = new int[calcRealMapSize(this.pixel_bits, paramInt1)];
/*  547 */     int i = paramInt2;
/*  548 */     int j = 1;
/*  549 */     boolean bool = true;
/*  550 */     BigInteger localBigInteger = this.validBits;
/*  551 */     for (int k = 0; k < paramInt1; i++) {
/*  552 */       if ((localBigInteger == null) || (localBigInteger.testBit(k)))
/*      */       {
/*  555 */         int m = paramArrayOfInt[i];
/*  556 */         int n = m >> 16 & 0xFF;
/*  557 */         int i1 = m >> 8 & 0xFF;
/*  558 */         int i2 = m & 0xFF;
/*  559 */         bool = (bool) && (n == i1) && (i1 == i2);
/*  560 */         if (paramBoolean) {
/*  561 */           int i3 = m >>> 24;
/*  562 */           if (i3 != 255) {
/*  563 */             if (i3 == 0) {
/*  564 */               if (j == 1) {
/*  565 */                 j = 2;
/*      */               }
/*  567 */               if (this.transparent_index < 0)
/*  568 */                 this.transparent_index = k;
/*      */             }
/*      */             else {
/*  571 */               j = 3;
/*      */             }
/*  573 */             bool = false;
/*      */           }
/*      */         } else {
/*  576 */           m |= -16777216;
/*      */         }
/*  578 */         this.rgb[k] = m;
/*      */       }
/*  551 */       k++;
/*      */     }
/*      */ 
/*  580 */     this.allgrayopaque = bool;
/*  581 */     setTransparency(j);
/*      */   }
/*      */ 
/*      */   private int calcRealMapSize(int paramInt1, int paramInt2) {
/*  585 */     int i = Math.max(1 << paramInt1, paramInt2);
/*  586 */     return Math.max(i, 256);
/*      */   }
/*      */ 
/*      */   private BigInteger getAllValid() {
/*  590 */     int i = (this.map_size + 7) / 8;
/*  591 */     byte[] arrayOfByte = new byte[i];
/*  592 */     Arrays.fill(arrayOfByte, (byte)-1);
/*  593 */     arrayOfByte[0] = ((byte)(255 >>> i * 8 - this.map_size));
/*      */ 
/*  595 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   public int getTransparency()
/*      */   {
/*  607 */     return this.transparency;
/*      */   }
/*      */ 
/*      */   public int[] getComponentSize()
/*      */   {
/*  618 */     if (this.nBits == null) {
/*  619 */       if (this.supportsAlpha) {
/*  620 */         this.nBits = new int[4];
/*  621 */         this.nBits[3] = 8;
/*      */       }
/*      */       else {
/*  624 */         this.nBits = new int[3];
/*      */       }
/*      */       int tmp58_57 = (this.nBits[2] = 8); this.nBits[1] = tmp58_57; this.nBits[0] = tmp58_57;
/*      */     }
/*  628 */     return (int[])this.nBits.clone();
/*      */   }
/*      */ 
/*      */   public final int getMapSize()
/*      */   {
/*  637 */     return this.map_size;
/*      */   }
/*      */ 
/*      */   public final int getTransparentPixel()
/*      */   {
/*  653 */     return this.transparent_index;
/*      */   }
/*      */ 
/*      */   public final void getReds(byte[] paramArrayOfByte)
/*      */   {
/*  664 */     for (int i = 0; i < this.map_size; i++)
/*  665 */       paramArrayOfByte[i] = ((byte)(this.rgb[i] >> 16));
/*      */   }
/*      */ 
/*      */   public final void getGreens(byte[] paramArrayOfByte)
/*      */   {
/*  677 */     for (int i = 0; i < this.map_size; i++)
/*  678 */       paramArrayOfByte[i] = ((byte)(this.rgb[i] >> 8));
/*      */   }
/*      */ 
/*      */   public final void getBlues(byte[] paramArrayOfByte)
/*      */   {
/*  690 */     for (int i = 0; i < this.map_size; i++)
/*  691 */       paramArrayOfByte[i] = ((byte)this.rgb[i]);
/*      */   }
/*      */ 
/*      */   public final void getAlphas(byte[] paramArrayOfByte)
/*      */   {
/*  703 */     for (int i = 0; i < this.map_size; i++)
/*  704 */       paramArrayOfByte[i] = ((byte)(this.rgb[i] >> 24));
/*      */   }
/*      */ 
/*      */   public final void getRGBs(int[] paramArrayOfInt)
/*      */   {
/*  720 */     System.arraycopy(this.rgb, 0, paramArrayOfInt, 0, this.map_size);
/*      */   }
/*      */ 
/*      */   private void setTransparentPixel(int paramInt) {
/*  724 */     if ((paramInt >= 0) && (paramInt < this.map_size)) {
/*  725 */       this.rgb[paramInt] &= 16777215;
/*  726 */       this.transparent_index = paramInt;
/*  727 */       this.allgrayopaque = false;
/*  728 */       if (this.transparency == 1)
/*  729 */         setTransparency(2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setTransparency(int paramInt)
/*      */   {
/*  735 */     if (this.transparency != paramInt) {
/*  736 */       this.transparency = paramInt;
/*  737 */       if (paramInt == 1) {
/*  738 */         this.supportsAlpha = false;
/*  739 */         this.numComponents = 3;
/*  740 */         this.nBits = opaqueBits;
/*      */       } else {
/*  742 */         this.supportsAlpha = true;
/*  743 */         this.numComponents = 4;
/*  744 */         this.nBits = alphaBits;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void calculatePixelMask()
/*      */   {
/*  758 */     int i = this.pixel_bits;
/*  759 */     if (i == 3)
/*  760 */       i = 4;
/*  761 */     else if ((i > 4) && (i < 8)) {
/*  762 */       i = 8;
/*      */     }
/*  764 */     this.pixel_mask = ((1 << i) - 1);
/*      */   }
/*      */ 
/*      */   public final int getRed(int paramInt)
/*      */   {
/*  779 */     return this.rgb[(paramInt & this.pixel_mask)] >> 16 & 0xFF;
/*      */   }
/*      */ 
/*      */   public final int getGreen(int paramInt)
/*      */   {
/*  794 */     return this.rgb[(paramInt & this.pixel_mask)] >> 8 & 0xFF;
/*      */   }
/*      */ 
/*      */   public final int getBlue(int paramInt)
/*      */   {
/*  809 */     return this.rgb[(paramInt & this.pixel_mask)] & 0xFF;
/*      */   }
/*      */ 
/*      */   public final int getAlpha(int paramInt)
/*      */   {
/*  822 */     return this.rgb[(paramInt & this.pixel_mask)] >> 24 & 0xFF;
/*      */   }
/*      */ 
/*      */   public final int getRGB(int paramInt)
/*      */   {
/*  837 */     return this.rgb[(paramInt & this.pixel_mask)];
/*      */   }
/*      */ 
/*      */   public synchronized Object getDataElements(int paramInt, Object paramObject)
/*      */   {
/*  878 */     int i = paramInt >> 16 & 0xFF;
/*  879 */     int j = paramInt >> 8 & 0xFF;
/*  880 */     int k = paramInt & 0xFF;
/*  881 */     int m = paramInt >>> 24;
/*  882 */     int n = 0;
/*      */ 
/*  889 */     for (int i1 = 38; (i1 >= 0) && 
/*  890 */       ((n = this.lookupcache[i1]) != 0); i1 -= 2)
/*      */     {
/*  893 */       if (paramInt == this.lookupcache[(i1 + 1)])
/*  894 */         return installpixel(paramObject, n ^ 0xFFFFFFFF);
/*      */     }
/*      */     int i3;
/*      */     int i4;
/*  898 */     if (this.allgrayopaque)
/*      */     {
/*  909 */       i1 = 256;
/*      */ 
/*  911 */       i3 = (i * 77 + j * 150 + k * 29 + 128) / 256;
/*      */ 
/*  913 */       for (i4 = 0; i4 < this.map_size; i4++)
/*  914 */         if (this.rgb[i4] != 0)
/*      */         {
/*  920 */           int i2 = (this.rgb[i4] & 0xFF) - i3;
/*  921 */           if (i2 < 0) i2 = -i2;
/*  922 */           if (i2 < i1) {
/*  923 */             n = i4;
/*  924 */             if (i2 == 0) {
/*      */               break;
/*      */             }
/*  927 */             i1 = i2;
/*      */           }
/*      */         }
/*      */     }
/*      */     else
/*      */     {
/*      */       int[] arrayOfInt;
/*      */       int i5;
/*      */       int i6;
/*  930 */       if (this.transparency == 1)
/*      */       {
/*  944 */         i1 = 2147483647;
/*  945 */         arrayOfInt = this.rgb;
/*      */ 
/*  947 */         for (i4 = 0; i4 < this.map_size; i4++) {
/*  948 */           i3 = arrayOfInt[i4];
/*  949 */           if ((i3 == paramInt) && (i3 != 0)) {
/*  950 */             n = i4;
/*  951 */             i1 = 0;
/*  952 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  956 */         if (i1 != 0)
/*  957 */           for (i4 = 0; i4 < this.map_size; i4++) {
/*  958 */             i3 = arrayOfInt[i4];
/*  959 */             if (i3 != 0)
/*      */             {
/*  963 */               i5 = (i3 >> 16 & 0xFF) - i;
/*  964 */               i6 = i5 * i5;
/*  965 */               if (i6 < i1) {
/*  966 */                 i5 = (i3 >> 8 & 0xFF) - j;
/*  967 */                 i6 += i5 * i5;
/*  968 */                 if (i6 < i1) {
/*  969 */                   i5 = (i3 & 0xFF) - k;
/*  970 */                   i6 += i5 * i5;
/*  971 */                   if (i6 < i1) {
/*  972 */                     n = i4;
/*  973 */                     i1 = i6;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  979 */       } else if ((m == 0) && (this.transparent_index >= 0))
/*      */       {
/*  983 */         n = this.transparent_index;
/*      */       }
/*      */       else
/*      */       {
/*  991 */         i1 = 2147483647;
/*  992 */         arrayOfInt = this.rgb;
/*  993 */         for (i3 = 0; i3 < this.map_size; i3++) {
/*  994 */           i4 = arrayOfInt[i3];
/*  995 */           if (i4 == paramInt) {
/*  996 */             if ((this.validBits == null) || (this.validBits.testBit(i3)))
/*      */             {
/*  999 */               n = i3;
/* 1000 */               break;
/*      */             }
/*      */           } else {
/* 1003 */             i5 = (i4 >> 16 & 0xFF) - i;
/* 1004 */             i6 = i5 * i5;
/* 1005 */             if (i6 < i1) {
/* 1006 */               i5 = (i4 >> 8 & 0xFF) - j;
/* 1007 */               i6 += i5 * i5;
/* 1008 */               if (i6 < i1) {
/* 1009 */                 i5 = (i4 & 0xFF) - k;
/* 1010 */                 i6 += i5 * i5;
/* 1011 */                 if (i6 < i1) {
/* 1012 */                   i5 = (i4 >>> 24) - m;
/* 1013 */                   i6 += i5 * i5;
/* 1014 */                   if ((i6 < i1) && ((this.validBits == null) || (this.validBits.testBit(i3))))
/*      */                   {
/* 1017 */                     n = i3;
/* 1018 */                     i1 = i6;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1025 */     System.arraycopy(this.lookupcache, 2, this.lookupcache, 0, 38);
/* 1026 */     this.lookupcache[39] = paramInt;
/* 1027 */     this.lookupcache[38] = (n ^ 0xFFFFFFFF);
/* 1028 */     return installpixel(paramObject, n);
/*      */   }
/*      */ 
/*      */   private Object installpixel(Object paramObject, int paramInt) {
/* 1032 */     switch (this.transferType)
/*      */     {
/*      */     case 3:
/*      */       int[] arrayOfInt;
/* 1035 */       if (paramObject == null)
/* 1036 */         paramObject = arrayOfInt = new int[1];
/*      */       else {
/* 1038 */         arrayOfInt = (int[])paramObject;
/*      */       }
/* 1040 */       arrayOfInt[0] = paramInt;
/* 1041 */       break;
/*      */     case 0:
/*      */       byte[] arrayOfByte;
/* 1044 */       if (paramObject == null)
/* 1045 */         paramObject = arrayOfByte = new byte[1];
/*      */       else {
/* 1047 */         arrayOfByte = (byte[])paramObject;
/*      */       }
/* 1049 */       arrayOfByte[0] = ((byte)paramInt);
/* 1050 */       break;
/*      */     case 1:
/*      */       short[] arrayOfShort;
/* 1053 */       if (paramObject == null)
/* 1054 */         paramObject = arrayOfShort = new short[1];
/*      */       else {
/* 1056 */         arrayOfShort = (short[])paramObject;
/*      */       }
/* 1058 */       arrayOfShort[0] = ((short)paramInt);
/* 1059 */       break;
/*      */     case 2:
/*      */     default:
/* 1061 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/* 1064 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public int[] getComponents(int paramInt1, int[] paramArrayOfInt, int paramInt2)
/*      */   {
/* 1093 */     if (paramArrayOfInt == null) {
/* 1094 */       paramArrayOfInt = new int[paramInt2 + this.numComponents];
/*      */     }
/*      */ 
/* 1098 */     paramArrayOfInt[(paramInt2 + 0)] = getRed(paramInt1);
/* 1099 */     paramArrayOfInt[(paramInt2 + 1)] = getGreen(paramInt1);
/* 1100 */     paramArrayOfInt[(paramInt2 + 2)] = getBlue(paramInt1);
/* 1101 */     if ((this.supportsAlpha) && (paramArrayOfInt.length - paramInt2 > 3)) {
/* 1102 */       paramArrayOfInt[(paramInt2 + 3)] = getAlpha(paramInt1);
/*      */     }
/*      */ 
/* 1105 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public int[] getComponents(Object paramObject, int[] paramArrayOfInt, int paramInt)
/*      */   {
/*      */     int i;
/* 1160 */     switch (this.transferType) {
/*      */     case 0:
/* 1162 */       byte[] arrayOfByte = (byte[])paramObject;
/* 1163 */       i = arrayOfByte[0] & 0xFF;
/* 1164 */       break;
/*      */     case 1:
/* 1166 */       short[] arrayOfShort = (short[])paramObject;
/* 1167 */       i = arrayOfShort[0] & 0xFFFF;
/* 1168 */       break;
/*      */     case 3:
/* 1170 */       int[] arrayOfInt = (int[])paramObject;
/* 1171 */       i = arrayOfInt[0];
/* 1172 */       break;
/*      */     case 2:
/*      */     default:
/* 1174 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/* 1177 */     return getComponents(i, paramArrayOfInt, paramInt);
/*      */   }
/*      */ 
/*      */   public int getDataElement(int[] paramArrayOfInt, int paramInt)
/*      */   {
/* 1205 */     int i = paramArrayOfInt[(paramInt + 0)] << 16 | paramArrayOfInt[(paramInt + 1)] << 8 | paramArrayOfInt[(paramInt + 2)];
/*      */ 
/* 1207 */     if (this.supportsAlpha) {
/* 1208 */       i |= paramArrayOfInt[(paramInt + 3)] << 24;
/*      */     }
/*      */     else {
/* 1211 */       i |= -16777216;
/*      */     }
/* 1213 */     Object localObject = getDataElements(i, null);
/*      */     int j;
/* 1215 */     switch (this.transferType) {
/*      */     case 0:
/* 1217 */       byte[] arrayOfByte = (byte[])localObject;
/* 1218 */       j = arrayOfByte[0] & 0xFF;
/* 1219 */       break;
/*      */     case 1:
/* 1221 */       short[] arrayOfShort = (short[])localObject;
/* 1222 */       j = arrayOfShort[0];
/* 1223 */       break;
/*      */     case 3:
/* 1225 */       int[] arrayOfInt = (int[])localObject;
/* 1226 */       j = arrayOfInt[0];
/* 1227 */       break;
/*      */     case 2:
/*      */     default:
/* 1229 */       throw new UnsupportedOperationException("This method has not been implemented for transferType " + this.transferType);
/*      */     }
/*      */ 
/* 1232 */     return j;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int[] paramArrayOfInt, int paramInt, Object paramObject)
/*      */   {
/* 1278 */     int i = paramArrayOfInt[(paramInt + 0)] << 16 | paramArrayOfInt[(paramInt + 1)] << 8 | paramArrayOfInt[(paramInt + 2)];
/*      */ 
/* 1280 */     if (this.supportsAlpha) {
/* 1281 */       i |= paramArrayOfInt[(paramInt + 3)] << 24;
/*      */     }
/*      */     else {
/* 1284 */       i &= -16777216;
/*      */     }
/* 1286 */     return getDataElements(i, paramObject);
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/*      */     WritableRaster localWritableRaster;
/* 1311 */     if ((this.pixel_bits == 1) || (this.pixel_bits == 2) || (this.pixel_bits == 4))
/*      */     {
/* 1313 */       localWritableRaster = Raster.createPackedRaster(0, paramInt1, paramInt2, 1, this.pixel_bits, null);
/*      */     }
/* 1316 */     else if (this.pixel_bits <= 8) {
/* 1317 */       localWritableRaster = Raster.createInterleavedRaster(0, paramInt1, paramInt2, 1, null);
/*      */     }
/* 1320 */     else if (this.pixel_bits <= 16) {
/* 1321 */       localWritableRaster = Raster.createInterleavedRaster(1, paramInt1, paramInt2, 1, null);
/*      */     }
/*      */     else
/*      */     {
/* 1325 */       throw new UnsupportedOperationException("This method is not supported  for pixel bits > 16.");
/*      */     }
/*      */ 
/* 1329 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleRaster(Raster paramRaster)
/*      */   {
/* 1343 */     int i = paramRaster.getSampleModel().getSampleSize(0);
/* 1344 */     return (paramRaster.getTransferType() == this.transferType) && (paramRaster.getNumBands() == 1) && (1 << i >= this.map_size);
/*      */   }
/*      */ 
/*      */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*      */   {
/* 1361 */     int[] arrayOfInt = new int[1];
/* 1362 */     arrayOfInt[0] = 0;
/* 1363 */     if ((this.pixel_bits == 1) || (this.pixel_bits == 2) || (this.pixel_bits == 4)) {
/* 1364 */       return new MultiPixelPackedSampleModel(this.transferType, paramInt1, paramInt2, this.pixel_bits);
/*      */     }
/*      */ 
/* 1368 */     return new ComponentSampleModel(this.transferType, paramInt1, paramInt2, 1, paramInt1, arrayOfInt);
/*      */   }
/*      */ 
/*      */   public boolean isCompatibleSampleModel(SampleModel paramSampleModel)
/*      */   {
/* 1386 */     if ((!(paramSampleModel instanceof ComponentSampleModel)) && (!(paramSampleModel instanceof MultiPixelPackedSampleModel)))
/*      */     {
/* 1388 */       return false;
/*      */     }
/*      */ 
/* 1392 */     if (paramSampleModel.getTransferType() != this.transferType) {
/* 1393 */       return false;
/*      */     }
/*      */ 
/* 1396 */     if (paramSampleModel.getNumBands() != 1) {
/* 1397 */       return false;
/*      */     }
/*      */ 
/* 1400 */     return true;
/*      */   }
/*      */ 
/*      */   public BufferedImage convertToIntDiscrete(Raster paramRaster, boolean paramBoolean)
/*      */   {
/* 1428 */     if (!isCompatibleRaster(paramRaster))
/* 1429 */       throw new IllegalArgumentException("This raster is not compatiblewith this IndexColorModel.");
/*      */     Object localObject1;
/* 1432 */     if ((paramBoolean) || (this.transparency == 3)) {
/* 1433 */       localObject1 = ColorModel.getRGBdefault();
/*      */     }
/* 1435 */     else if (this.transparency == 2) {
/* 1436 */       localObject1 = new DirectColorModel(25, 16711680, 65280, 255, 16777216);
/*      */     }
/*      */     else
/*      */     {
/* 1440 */       localObject1 = new DirectColorModel(24, 16711680, 65280, 255);
/*      */     }
/*      */ 
/* 1443 */     int i = paramRaster.getWidth();
/* 1444 */     int j = paramRaster.getHeight();
/* 1445 */     WritableRaster localWritableRaster = ((ColorModel)localObject1).createCompatibleWritableRaster(i, j);
/*      */ 
/* 1447 */     Object localObject2 = null;
/* 1448 */     int[] arrayOfInt = null;
/*      */ 
/* 1450 */     int k = paramRaster.getMinX();
/* 1451 */     int m = paramRaster.getMinY();
/*      */ 
/* 1453 */     for (int n = 0; n < j; m++) {
/* 1454 */       localObject2 = paramRaster.getDataElements(k, m, i, 1, localObject2);
/* 1455 */       if ((localObject2 instanceof int[]))
/* 1456 */         arrayOfInt = (int[])localObject2;
/*      */       else {
/* 1458 */         arrayOfInt = DataBuffer.toIntArray(localObject2);
/*      */       }
/* 1460 */       for (int i1 = 0; i1 < i; i1++) {
/* 1461 */         arrayOfInt[i1] = this.rgb[(arrayOfInt[i1] & this.pixel_mask)];
/*      */       }
/* 1463 */       localWritableRaster.setDataElements(0, n, i, 1, arrayOfInt);
/*      */ 
/* 1453 */       n++;
/*      */     }
/*      */ 
/* 1466 */     return new BufferedImage((ColorModel)localObject1, localWritableRaster, false, null);
/*      */   }
/*      */ 
/*      */   public boolean isValid(int paramInt)
/*      */   {
/* 1477 */     return (paramInt >= 0) && (paramInt < this.map_size) && ((this.validBits == null) || (this.validBits.testBit(paramInt)));
/*      */   }
/*      */ 
/*      */   public boolean isValid()
/*      */   {
/* 1488 */     return this.validBits == null;
/*      */   }
/*      */ 
/*      */   public BigInteger getValidPixels()
/*      */   {
/* 1502 */     if (this.validBits == null) {
/* 1503 */       return getAllValid();
/*      */     }
/*      */ 
/* 1506 */     return this.validBits;
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1525 */     return new String("IndexColorModel: #pixelBits = " + this.pixel_bits + " numComponents = " + this.numComponents + " color space = " + this.colorSpace + " transparency = " + this.transparency + " transIndex   = " + this.transparent_index + " has alpha = " + this.supportsAlpha + " isAlphaPre = " + this.isAlphaPremultiplied);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  139 */     ColorModel.loadLibraries();
/*  140 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.IndexColorModel
 * JD-Core Version:    0.6.2
 */