/*      */ package java.awt.image;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class ComponentSampleModel extends SampleModel
/*      */ {
/*      */   protected int[] bandOffsets;
/*      */   protected int[] bankIndices;
/*   88 */   protected int numBands = 1;
/*      */ 
/*   94 */   protected int numBanks = 1;
/*      */   protected int scanlineStride;
/*      */   protected int pixelStride;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public ComponentSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt)
/*      */   {
/*  146 */     super(paramInt1, paramInt2, paramInt3, paramArrayOfInt.length);
/*  147 */     this.dataType = paramInt1;
/*  148 */     this.pixelStride = paramInt4;
/*  149 */     this.scanlineStride = paramInt5;
/*  150 */     this.bandOffsets = ((int[])paramArrayOfInt.clone());
/*  151 */     this.numBands = this.bandOffsets.length;
/*  152 */     if (paramInt4 < 0) {
/*  153 */       throw new IllegalArgumentException("Pixel stride must be >= 0");
/*      */     }
/*      */ 
/*  156 */     if (paramInt5 < 0) {
/*  157 */       throw new IllegalArgumentException("Scanline stride must be >= 0");
/*      */     }
/*  159 */     if (this.numBands < 1) {
/*  160 */       throw new IllegalArgumentException("Must have at least one band.");
/*      */     }
/*  162 */     if ((paramInt1 < 0) || (paramInt1 > 5))
/*      */     {
/*  164 */       throw new IllegalArgumentException("Unsupported dataType.");
/*      */     }
/*  166 */     this.bankIndices = new int[this.numBands];
/*  167 */     for (int i = 0; i < this.numBands; i++)
/*  168 */       this.bankIndices[i] = 0;
/*      */   }
/*      */ 
/*      */   public ComponentSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  209 */     super(paramInt1, paramInt2, paramInt3, paramArrayOfInt2.length);
/*  210 */     this.dataType = paramInt1;
/*  211 */     this.pixelStride = paramInt4;
/*  212 */     this.scanlineStride = paramInt5;
/*  213 */     this.bandOffsets = ((int[])paramArrayOfInt2.clone());
/*  214 */     this.bankIndices = ((int[])paramArrayOfInt1.clone());
/*  215 */     if (paramInt4 < 0) {
/*  216 */       throw new IllegalArgumentException("Pixel stride must be >= 0");
/*      */     }
/*      */ 
/*  219 */     if (paramInt5 < 0) {
/*  220 */       throw new IllegalArgumentException("Scanline stride must be >= 0");
/*      */     }
/*  222 */     if ((paramInt1 < 0) || (paramInt1 > 5))
/*      */     {
/*  224 */       throw new IllegalArgumentException("Unsupported dataType.");
/*      */     }
/*  226 */     int i = this.bankIndices[0];
/*  227 */     if (i < 0) {
/*  228 */       throw new IllegalArgumentException("Index of bank 0 is less than 0 (" + i + ")");
/*      */     }
/*      */ 
/*  231 */     for (int j = 1; j < this.bankIndices.length; j++) {
/*  232 */       if (this.bankIndices[j] > i) {
/*  233 */         i = this.bankIndices[j];
/*      */       }
/*  235 */       else if (this.bankIndices[j] < 0) {
/*  236 */         throw new IllegalArgumentException("Index of bank " + j + " is less than 0 (" + i + ")");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  241 */     this.numBanks = (i + 1);
/*  242 */     this.numBands = this.bandOffsets.length;
/*  243 */     if (this.bandOffsets.length != this.bankIndices.length)
/*  244 */       throw new IllegalArgumentException("Length of bandOffsets must equal length of bankIndices.");
/*      */   }
/*      */ 
/*      */   private long getBufferSize()
/*      */   {
/*  254 */     int i = this.bandOffsets[0];
/*  255 */     for (int j = 1; j < this.bandOffsets.length; j++) {
/*  256 */       i = Math.max(i, this.bandOffsets[j]);
/*      */     }
/*  258 */     long l = 0L;
/*  259 */     if (i >= 0)
/*  260 */       l += i + 1;
/*  261 */     if (this.pixelStride > 0)
/*  262 */       l += this.pixelStride * (this.width - 1);
/*  263 */     if (this.scanlineStride > 0)
/*  264 */       l += this.scanlineStride * (this.height - 1);
/*  265 */     return l;
/*      */   }
/*      */ 
/*      */   int[] orderBands(int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  272 */     int[] arrayOfInt1 = new int[paramArrayOfInt.length];
/*  273 */     int[] arrayOfInt2 = new int[paramArrayOfInt.length];
/*      */ 
/*  275 */     for (int i = 0; i < arrayOfInt1.length; i++) arrayOfInt1[i] = i;
/*      */ 
/*  277 */     for (i = 0; i < arrayOfInt2.length; i++) {
/*  278 */       int j = i;
/*  279 */       for (int k = i + 1; k < arrayOfInt2.length; k++) {
/*  280 */         if (paramArrayOfInt[arrayOfInt1[j]] > paramArrayOfInt[arrayOfInt1[k]]) {
/*  281 */           j = k;
/*      */         }
/*      */       }
/*  284 */       arrayOfInt2[arrayOfInt1[j]] = (i * paramInt);
/*  285 */       arrayOfInt1[j] = arrayOfInt1[i];
/*      */     }
/*  287 */     return arrayOfInt2;
/*      */   }
/*      */ 
/*      */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*      */   {
/*  302 */     Object localObject = null;
/*      */ 
/*  304 */     int i = this.bandOffsets[0];
/*  305 */     int j = this.bandOffsets[0];
/*  306 */     for (int k = 1; k < this.bandOffsets.length; k++) {
/*  307 */       i = Math.min(i, this.bandOffsets[k]);
/*  308 */       j = Math.max(j, this.bandOffsets[k]);
/*      */     }
/*  310 */     j -= i;
/*      */ 
/*  312 */     k = this.bandOffsets.length;
/*      */ 
/*  314 */     int m = Math.abs(this.pixelStride);
/*  315 */     int n = Math.abs(this.scanlineStride);
/*  316 */     int i1 = Math.abs(j);
/*      */     int[] arrayOfInt;
/*  318 */     if (m > n) {
/*  319 */       if (m > i1) {
/*  320 */         if (n > i1) {
/*  321 */           arrayOfInt = new int[this.bandOffsets.length];
/*  322 */           for (i2 = 0; i2 < k; i2++)
/*  323 */             arrayOfInt[i2] = (this.bandOffsets[i2] - i);
/*  324 */           n = i1 + 1;
/*  325 */           m = n * paramInt2;
/*      */         } else {
/*  327 */           arrayOfInt = orderBands(this.bandOffsets, n * paramInt2);
/*  328 */           m = k * n * paramInt2;
/*      */         }
/*      */       } else {
/*  331 */         m = n * paramInt2;
/*  332 */         arrayOfInt = orderBands(this.bandOffsets, m * paramInt1);
/*      */       }
/*      */     }
/*  335 */     else if (m > i1) {
/*  336 */       arrayOfInt = new int[this.bandOffsets.length];
/*  337 */       for (i2 = 0; i2 < k; i2++)
/*  338 */         arrayOfInt[i2] = (this.bandOffsets[i2] - i);
/*  339 */       m = i1 + 1;
/*  340 */       n = m * paramInt1;
/*      */     }
/*  342 */     else if (n > i1) {
/*  343 */       arrayOfInt = orderBands(this.bandOffsets, m * paramInt1);
/*  344 */       n = k * m * paramInt1;
/*      */     } else {
/*  346 */       n = m * paramInt1;
/*  347 */       arrayOfInt = orderBands(this.bandOffsets, n * paramInt2);
/*      */     }
/*      */ 
/*  353 */     int i2 = 0;
/*  354 */     if (this.scanlineStride < 0) {
/*  355 */       i2 += n * paramInt2;
/*  356 */       n *= -1;
/*      */     }
/*  358 */     if (this.pixelStride < 0) {
/*  359 */       i2 += m * paramInt1;
/*  360 */       m *= -1;
/*      */     }
/*      */ 
/*  363 */     for (int i3 = 0; i3 < k; i3++)
/*  364 */       arrayOfInt[i3] += i2;
/*  365 */     return new ComponentSampleModel(this.dataType, paramInt1, paramInt2, m, n, this.bankIndices, arrayOfInt);
/*      */   }
/*      */ 
/*      */   public SampleModel createSubsetSampleModel(int[] paramArrayOfInt)
/*      */   {
/*  382 */     if (paramArrayOfInt.length > this.bankIndices.length) {
/*  383 */       throw new RasterFormatException("There are only " + this.bankIndices.length + " bands");
/*      */     }
/*      */ 
/*  386 */     int[] arrayOfInt1 = new int[paramArrayOfInt.length];
/*  387 */     int[] arrayOfInt2 = new int[paramArrayOfInt.length];
/*      */ 
/*  389 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/*  390 */       arrayOfInt1[i] = this.bankIndices[paramArrayOfInt[i]];
/*  391 */       arrayOfInt2[i] = this.bandOffsets[paramArrayOfInt[i]];
/*      */     }
/*      */ 
/*  394 */     return new ComponentSampleModel(this.dataType, this.width, this.height, this.pixelStride, this.scanlineStride, arrayOfInt1, arrayOfInt2);
/*      */   }
/*      */ 
/*      */   public DataBuffer createDataBuffer()
/*      */   {
/*  410 */     Object localObject = null;
/*      */ 
/*  412 */     int i = (int)getBufferSize();
/*  413 */     switch (this.dataType) {
/*      */     case 0:
/*  415 */       localObject = new DataBufferByte(i, this.numBanks);
/*  416 */       break;
/*      */     case 1:
/*  418 */       localObject = new DataBufferUShort(i, this.numBanks);
/*  419 */       break;
/*      */     case 2:
/*  421 */       localObject = new DataBufferShort(i, this.numBanks);
/*  422 */       break;
/*      */     case 3:
/*  424 */       localObject = new DataBufferInt(i, this.numBanks);
/*  425 */       break;
/*      */     case 4:
/*  427 */       localObject = new DataBufferFloat(i, this.numBanks);
/*  428 */       break;
/*      */     case 5:
/*  430 */       localObject = new DataBufferDouble(i, this.numBanks);
/*      */     }
/*      */ 
/*  434 */     return localObject;
/*      */   }
/*      */ 
/*      */   public int getOffset(int paramInt1, int paramInt2)
/*      */   {
/*  451 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[0];
/*  452 */     return i;
/*      */   }
/*      */ 
/*      */   public int getOffset(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  468 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3];
/*  469 */     return i;
/*      */   }
/*      */ 
/*      */   public final int[] getSampleSize()
/*      */   {
/*  478 */     int[] arrayOfInt = new int[this.numBands];
/*  479 */     int i = getSampleSize(0);
/*      */ 
/*  481 */     for (int j = 0; j < this.numBands; j++) {
/*  482 */       arrayOfInt[j] = i;
/*      */     }
/*  484 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public final int getSampleSize(int paramInt)
/*      */   {
/*  492 */     return DataBuffer.getDataTypeSize(this.dataType);
/*      */   }
/*      */ 
/*      */   public final int[] getBankIndices()
/*      */   {
/*  499 */     return (int[])this.bankIndices.clone();
/*      */   }
/*      */ 
/*      */   public final int[] getBandOffsets()
/*      */   {
/*  506 */     return (int[])this.bandOffsets.clone();
/*      */   }
/*      */ 
/*      */   public final int getScanlineStride()
/*      */   {
/*  513 */     return this.scanlineStride;
/*      */   }
/*      */ 
/*      */   public final int getPixelStride()
/*      */   {
/*  520 */     return this.pixelStride;
/*      */   }
/*      */ 
/*      */   public final int getNumDataElements()
/*      */   {
/*  538 */     return getNumBands();
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*      */   {
/*  590 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/*  591 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  595 */     int i = getTransferType();
/*  596 */     int j = getNumDataElements();
/*  597 */     int k = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*      */ 
/*  599 */     switch (i)
/*      */     {
/*      */     case 0:
/*      */       byte[] arrayOfByte;
/*  605 */       if (paramObject == null)
/*  606 */         arrayOfByte = new byte[j];
/*      */       else {
/*  608 */         arrayOfByte = (byte[])paramObject;
/*      */       }
/*  610 */       for (int m = 0; m < j; m++) {
/*  611 */         arrayOfByte[m] = ((byte)paramDataBuffer.getElem(this.bankIndices[m], k + this.bandOffsets[m]));
/*      */       }
/*      */ 
/*  615 */       paramObject = arrayOfByte;
/*  616 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */       short[] arrayOfShort;
/*  623 */       if (paramObject == null)
/*  624 */         arrayOfShort = new short[j];
/*      */       else {
/*  626 */         arrayOfShort = (short[])paramObject;
/*      */       }
/*  628 */       for (int n = 0; n < j; n++) {
/*  629 */         arrayOfShort[n] = ((short)paramDataBuffer.getElem(this.bankIndices[n], k + this.bandOffsets[n]));
/*      */       }
/*      */ 
/*  633 */       paramObject = arrayOfShort;
/*  634 */       break;
/*      */     case 3:
/*      */       int[] arrayOfInt;
/*  640 */       if (paramObject == null)
/*  641 */         arrayOfInt = new int[j];
/*      */       else {
/*  643 */         arrayOfInt = (int[])paramObject;
/*      */       }
/*  645 */       for (int i1 = 0; i1 < j; i1++) {
/*  646 */         arrayOfInt[i1] = paramDataBuffer.getElem(this.bankIndices[i1], k + this.bandOffsets[i1]);
/*      */       }
/*      */ 
/*  650 */       paramObject = arrayOfInt;
/*  651 */       break;
/*      */     case 4:
/*      */       float[] arrayOfFloat;
/*  657 */       if (paramObject == null)
/*  658 */         arrayOfFloat = new float[j];
/*      */       else {
/*  660 */         arrayOfFloat = (float[])paramObject;
/*      */       }
/*  662 */       for (int i2 = 0; i2 < j; i2++) {
/*  663 */         arrayOfFloat[i2] = paramDataBuffer.getElemFloat(this.bankIndices[i2], k + this.bandOffsets[i2]);
/*      */       }
/*      */ 
/*  667 */       paramObject = arrayOfFloat;
/*  668 */       break;
/*      */     case 5:
/*      */       double[] arrayOfDouble;
/*  674 */       if (paramObject == null)
/*  675 */         arrayOfDouble = new double[j];
/*      */       else {
/*  677 */         arrayOfDouble = (double[])paramObject;
/*      */       }
/*  679 */       for (int i3 = 0; i3 < j; i3++) {
/*  680 */         arrayOfDouble[i3] = paramDataBuffer.getElemDouble(this.bankIndices[i3], k + this.bandOffsets[i3]);
/*      */       }
/*      */ 
/*  684 */       paramObject = arrayOfDouble;
/*      */     }
/*      */ 
/*  688 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public int[] getPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*  708 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height))
/*  709 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     int[] arrayOfInt;
/*  713 */     if (paramArrayOfInt != null)
/*  714 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  716 */       arrayOfInt = new int[this.numBands];
/*      */     }
/*  718 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*  719 */     for (int j = 0; j < this.numBands; j++) {
/*  720 */       arrayOfInt[j] = paramDataBuffer.getElem(this.bankIndices[j], i + this.bandOffsets[j]);
/*      */     }
/*      */ 
/*  723 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*  742 */     int i = paramInt1 + paramInt3;
/*  743 */     int j = paramInt2 + paramInt4;
/*      */ 
/*  745 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt2 > this.height) || (j < 0) || (j > this.height))
/*      */     {
/*  748 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     int[] arrayOfInt;
/*  752 */     if (paramArrayOfInt != null)
/*  753 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  755 */       arrayOfInt = new int[paramInt3 * paramInt4 * this.numBands];
/*      */     }
/*  757 */     int k = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*  758 */     int m = 0;
/*      */ 
/*  760 */     for (int n = 0; n < paramInt4; n++) {
/*  761 */       int i1 = k;
/*  762 */       for (int i2 = 0; i2 < paramInt3; i2++) {
/*  763 */         for (int i3 = 0; i3 < this.numBands; i3++) {
/*  764 */           arrayOfInt[(m++)] = paramDataBuffer.getElem(this.bankIndices[i3], i1 + this.bandOffsets[i3]);
/*      */         }
/*      */ 
/*  767 */         i1 += this.pixelStride;
/*      */       }
/*  769 */       k += this.scanlineStride;
/*      */     }
/*  771 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public int getSample(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*      */   {
/*  788 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/*  789 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  792 */     int i = paramDataBuffer.getElem(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3]);
/*      */ 
/*  795 */     return i;
/*      */   }
/*      */ 
/*      */   public float getSampleFloat(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*      */   {
/*  812 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/*  813 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  817 */     float f = paramDataBuffer.getElemFloat(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3]);
/*      */ 
/*  820 */     return f;
/*      */   }
/*      */ 
/*      */   public double getSampleDouble(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*      */   {
/*  837 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/*  838 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  842 */     double d = paramDataBuffer.getElemDouble(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3]);
/*      */ 
/*  845 */     return d;
/*      */   }
/*      */ 
/*      */   public int[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/*  867 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height))
/*  868 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     int[] arrayOfInt;
/*  872 */     if (paramArrayOfInt != null)
/*  873 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  875 */       arrayOfInt = new int[paramInt3 * paramInt4];
/*      */     }
/*  877 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt5];
/*  878 */     int j = 0;
/*      */ 
/*  880 */     for (int k = 0; k < paramInt4; k++) {
/*  881 */       int m = i;
/*  882 */       for (int n = 0; n < paramInt3; n++) {
/*  883 */         arrayOfInt[(j++)] = paramDataBuffer.getElem(this.bankIndices[paramInt5], m);
/*      */ 
/*  885 */         m += this.pixelStride;
/*      */       }
/*  887 */       i += this.scanlineStride;
/*      */     }
/*  889 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*      */   {
/*  930 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/*  931 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  935 */     int i = getTransferType();
/*  936 */     int j = getNumDataElements();
/*  937 */     int k = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*      */ 
/*  939 */     switch (i)
/*      */     {
/*      */     case 0:
/*  943 */       byte[] arrayOfByte = (byte[])paramObject;
/*      */ 
/*  945 */       for (int m = 0; m < j; m++) {
/*  946 */         paramDataBuffer.setElem(this.bankIndices[m], k + this.bandOffsets[m], arrayOfByte[m] & 0xFF);
/*      */       }
/*      */ 
/*  949 */       break;
/*      */     case 1:
/*      */     case 2:
/*  954 */       short[] arrayOfShort = (short[])paramObject;
/*      */ 
/*  956 */       for (int n = 0; n < j; n++) {
/*  957 */         paramDataBuffer.setElem(this.bankIndices[n], k + this.bandOffsets[n], arrayOfShort[n] & 0xFFFF);
/*      */       }
/*      */ 
/*  960 */       break;
/*      */     case 3:
/*  964 */       int[] arrayOfInt = (int[])paramObject;
/*      */ 
/*  966 */       for (int i1 = 0; i1 < j; i1++) {
/*  967 */         paramDataBuffer.setElem(this.bankIndices[i1], k + this.bandOffsets[i1], arrayOfInt[i1]);
/*      */       }
/*      */ 
/*  970 */       break;
/*      */     case 4:
/*  974 */       float[] arrayOfFloat = (float[])paramObject;
/*      */ 
/*  976 */       for (int i2 = 0; i2 < j; i2++) {
/*  977 */         paramDataBuffer.setElemFloat(this.bankIndices[i2], k + this.bandOffsets[i2], arrayOfFloat[i2]);
/*      */       }
/*      */ 
/*  980 */       break;
/*      */     case 5:
/*  984 */       double[] arrayOfDouble = (double[])paramObject;
/*      */ 
/*  986 */       for (int i3 = 0; i3 < j; i3++)
/*  987 */         paramDataBuffer.setElemDouble(this.bankIndices[i3], k + this.bandOffsets[i3], arrayOfDouble[i3]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1007 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 1008 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1011 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/* 1012 */     for (int j = 0; j < this.numBands; j++)
/* 1013 */       paramDataBuffer.setElem(this.bankIndices[j], i + this.bandOffsets[j], paramArrayOfInt[j]);
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1033 */     int i = paramInt1 + paramInt3;
/* 1034 */     int j = paramInt2 + paramInt4;
/*      */ 
/* 1036 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (j < 0) || (j > this.height))
/*      */     {
/* 1039 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1043 */     int k = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/* 1044 */     int m = 0;
/*      */ 
/* 1046 */     for (int n = 0; n < paramInt4; n++) {
/* 1047 */       int i1 = k;
/* 1048 */       for (int i2 = 0; i2 < paramInt3; i2++) {
/* 1049 */         for (int i3 = 0; i3 < this.numBands; i3++) {
/* 1050 */           paramDataBuffer.setElem(this.bankIndices[i3], i1 + this.bandOffsets[i3], paramArrayOfInt[(m++)]);
/*      */         }
/*      */ 
/* 1053 */         i1 += this.pixelStride;
/*      */       }
/* 1055 */       k += this.scanlineStride;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DataBuffer paramDataBuffer)
/*      */   {
/* 1074 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 1075 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1078 */     paramDataBuffer.setElem(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3], paramInt4);
/*      */   }
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, float paramFloat, DataBuffer paramDataBuffer)
/*      */   {
/* 1098 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 1099 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1102 */     paramDataBuffer.setElemFloat(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3], paramFloat);
/*      */   }
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, double paramDouble, DataBuffer paramDataBuffer)
/*      */   {
/* 1123 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 1124 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1127 */     paramDataBuffer.setElemDouble(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt3], paramDouble);
/*      */   }
/*      */ 
/*      */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*      */   {
/* 1149 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height)) {
/* 1150 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1153 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.bandOffsets[paramInt5];
/* 1154 */     int j = 0;
/*      */ 
/* 1156 */     for (int k = 0; k < paramInt4; k++) {
/* 1157 */       int m = i;
/* 1158 */       for (int n = 0; n < paramInt3; n++) {
/* 1159 */         paramDataBuffer.setElem(this.bankIndices[paramInt5], m, paramArrayOfInt[(j++)]);
/* 1160 */         m += this.pixelStride;
/*      */       }
/* 1162 */       i += this.scanlineStride;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject) {
/* 1167 */     if ((paramObject == null) || (!(paramObject instanceof ComponentSampleModel))) {
/* 1168 */       return false;
/*      */     }
/*      */ 
/* 1171 */     ComponentSampleModel localComponentSampleModel = (ComponentSampleModel)paramObject;
/* 1172 */     return (this.width == localComponentSampleModel.width) && (this.height == localComponentSampleModel.height) && (this.numBands == localComponentSampleModel.numBands) && (this.dataType == localComponentSampleModel.dataType) && (Arrays.equals(this.bandOffsets, localComponentSampleModel.bandOffsets)) && (Arrays.equals(this.bankIndices, localComponentSampleModel.bankIndices)) && (this.numBands == localComponentSampleModel.numBands) && (this.numBanks == localComponentSampleModel.numBanks) && (this.scanlineStride == localComponentSampleModel.scanlineStride) && (this.pixelStride == localComponentSampleModel.pixelStride);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1186 */     int i = 0;
/* 1187 */     i = this.width;
/* 1188 */     i <<= 8;
/* 1189 */     i ^= this.height;
/* 1190 */     i <<= 8;
/* 1191 */     i ^= this.numBands;
/* 1192 */     i <<= 8;
/* 1193 */     i ^= this.dataType;
/* 1194 */     i <<= 8;
/* 1195 */     for (int j = 0; j < this.bandOffsets.length; j++) {
/* 1196 */       i ^= this.bandOffsets[j];
/* 1197 */       i <<= 8;
/*      */     }
/* 1199 */     for (j = 0; j < this.bankIndices.length; j++) {
/* 1200 */       i ^= this.bankIndices[j];
/* 1201 */       i <<= 8;
/*      */     }
/* 1203 */     i ^= this.numBands;
/* 1204 */     i <<= 8;
/* 1205 */     i ^= this.numBanks;
/* 1206 */     i <<= 8;
/* 1207 */     i ^= this.scanlineStride;
/* 1208 */     i <<= 8;
/* 1209 */     i ^= this.pixelStride;
/* 1210 */     return i;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  109 */     ColorModel.loadLibraries();
/*  110 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ComponentSampleModel
 * JD-Core Version:    0.6.2
 */