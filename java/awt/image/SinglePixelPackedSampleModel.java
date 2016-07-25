/*     */ package java.awt.image;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class SinglePixelPackedSampleModel extends SampleModel
/*     */ {
/*     */   private int[] bitMasks;
/*     */   private int[] bitOffsets;
/*     */   private int[] bitSizes;
/*     */   private int maxBitSize;
/*     */   private int scanlineStride;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public SinglePixelPackedSampleModel(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*     */   {
/* 110 */     this(paramInt1, paramInt2, paramInt3, paramInt2, paramArrayOfInt);
/* 111 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 3))
/*     */     {
/* 114 */       throw new IllegalArgumentException("Unsupported data type " + paramInt1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SinglePixelPackedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*     */   {
/* 144 */     super(paramInt1, paramInt2, paramInt3, paramArrayOfInt.length);
/* 145 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 3))
/*     */     {
/* 148 */       throw new IllegalArgumentException("Unsupported data type " + paramInt1);
/*     */     }
/*     */ 
/* 151 */     this.dataType = paramInt1;
/* 152 */     this.bitMasks = ((int[])paramArrayOfInt.clone());
/* 153 */     this.scanlineStride = paramInt4;
/*     */ 
/* 155 */     this.bitOffsets = new int[this.numBands];
/* 156 */     this.bitSizes = new int[this.numBands];
/*     */ 
/* 158 */     int i = (int)((1L << DataBuffer.getDataTypeSize(paramInt1)) - 1L);
/*     */ 
/* 160 */     this.maxBitSize = 0;
/* 161 */     for (int j = 0; j < this.numBands; j++) {
/* 162 */       int k = 0; int m = 0;
/* 163 */       this.bitMasks[j] &= i;
/* 164 */       int n = this.bitMasks[j];
/* 165 */       if (n != 0) {
/* 166 */         while ((n & 0x1) == 0) {
/* 167 */           n >>>= 1;
/* 168 */           k++;
/*     */         }
/* 170 */         while ((n & 0x1) == 1) {
/* 171 */           n >>>= 1;
/* 172 */           m++;
/*     */         }
/* 174 */         if (n != 0) {
/* 175 */           throw new IllegalArgumentException("Mask " + paramArrayOfInt[j] + " must be contiguous");
/*     */         }
/*     */       }
/*     */ 
/* 179 */       this.bitOffsets[j] = k;
/* 180 */       this.bitSizes[j] = m;
/* 181 */       if (m > this.maxBitSize)
/* 182 */         this.maxBitSize = m;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumDataElements()
/*     */   {
/* 193 */     return 1;
/*     */   }
/*     */ 
/*     */   private long getBufferSize()
/*     */   {
/* 202 */     long l = this.scanlineStride * (this.height - 1) + this.width;
/* 203 */     return l;
/*     */   }
/*     */ 
/*     */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*     */   {
/* 219 */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = new SinglePixelPackedSampleModel(this.dataType, paramInt1, paramInt2, this.bitMasks);
/*     */ 
/* 221 */     return localSinglePixelPackedSampleModel;
/*     */   }
/*     */ 
/*     */   public DataBuffer createDataBuffer()
/*     */   {
/* 231 */     Object localObject = null;
/*     */ 
/* 233 */     int i = (int)getBufferSize();
/* 234 */     switch (this.dataType) {
/*     */     case 0:
/* 236 */       localObject = new DataBufferByte(i);
/* 237 */       break;
/*     */     case 1:
/* 239 */       localObject = new DataBufferUShort(i);
/* 240 */       break;
/*     */     case 3:
/* 242 */       localObject = new DataBufferInt(i);
/*     */     case 2:
/*     */     }
/* 245 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int[] getSampleSize()
/*     */   {
/* 250 */     return (int[])this.bitSizes.clone();
/*     */   }
/*     */ 
/*     */   public int getSampleSize(int paramInt)
/*     */   {
/* 255 */     return this.bitSizes[paramInt];
/*     */   }
/*     */ 
/*     */   public int getOffset(int paramInt1, int paramInt2)
/*     */   {
/* 270 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 271 */     return i;
/*     */   }
/*     */ 
/*     */   public int[] getBitOffsets()
/*     */   {
/* 279 */     return (int[])this.bitOffsets.clone();
/*     */   }
/*     */ 
/*     */   public int[] getBitMasks()
/*     */   {
/* 286 */     return (int[])this.bitMasks.clone();
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 294 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public SampleModel createSubsetSampleModel(int[] paramArrayOfInt)
/*     */   {
/* 310 */     if (paramArrayOfInt.length > this.numBands) {
/* 311 */       throw new RasterFormatException("There are only " + this.numBands + " bands");
/*     */     }
/*     */ 
/* 314 */     int[] arrayOfInt = new int[paramArrayOfInt.length];
/* 315 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 316 */       arrayOfInt[i] = this.bitMasks[paramArrayOfInt[i]];
/*     */     }
/* 318 */     return new SinglePixelPackedSampleModel(this.dataType, this.width, this.height, this.scanlineStride, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*     */   {
/* 363 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 364 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 368 */     int i = getTransferType();
/*     */ 
/* 370 */     switch (i)
/*     */     {
/*     */     case 0:
/*     */       byte[] arrayOfByte;
/* 376 */       if (paramObject == null)
/* 377 */         arrayOfByte = new byte[1];
/*     */       else {
/* 379 */         arrayOfByte = (byte[])paramObject;
/*     */       }
/* 381 */       arrayOfByte[0] = ((byte)paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1));
/*     */ 
/* 383 */       paramObject = arrayOfByte;
/* 384 */       break;
/*     */     case 1:
/*     */       short[] arrayOfShort;
/* 390 */       if (paramObject == null)
/* 391 */         arrayOfShort = new short[1];
/*     */       else {
/* 393 */         arrayOfShort = (short[])paramObject;
/*     */       }
/* 395 */       arrayOfShort[0] = ((short)paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1));
/*     */ 
/* 397 */       paramObject = arrayOfShort;
/* 398 */       break;
/*     */     case 3:
/*     */       int[] arrayOfInt;
/* 404 */       if (paramObject == null)
/* 405 */         arrayOfInt = new int[1];
/*     */       else {
/* 407 */         arrayOfInt = (int[])paramObject;
/*     */       }
/* 409 */       arrayOfInt[0] = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1);
/*     */ 
/* 411 */       paramObject = arrayOfInt;
/*     */     case 2:
/*     */     }
/*     */ 
/* 415 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public int[] getPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 430 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height))
/* 431 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     int[] arrayOfInt;
/* 435 */     if (paramArrayOfInt == null)
/* 436 */       arrayOfInt = new int[this.numBands];
/*     */     else {
/* 438 */       arrayOfInt = paramArrayOfInt;
/*     */     }
/*     */ 
/* 441 */     int i = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1);
/* 442 */     for (int j = 0; j < this.numBands; j++) {
/* 443 */       arrayOfInt[j] = ((i & this.bitMasks[j]) >>> this.bitOffsets[j]);
/*     */     }
/* 445 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 464 */     int i = paramInt1 + paramInt3;
/* 465 */     int j = paramInt2 + paramInt4;
/*     */ 
/* 467 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (j < 0) || (j > this.height))
/*     */     {
/* 470 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     int[] arrayOfInt;
/* 474 */     if (paramArrayOfInt != null)
/* 475 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 477 */       arrayOfInt = new int[paramInt3 * paramInt4 * this.numBands];
/*     */     }
/* 479 */     int k = paramInt2 * this.scanlineStride + paramInt1;
/* 480 */     int m = 0;
/*     */ 
/* 482 */     for (int n = 0; n < paramInt4; n++) {
/* 483 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/* 484 */         int i2 = paramDataBuffer.getElem(k + i1);
/* 485 */         for (int i3 = 0; i3 < this.numBands; i3++) {
/* 486 */           arrayOfInt[(m++)] = ((i2 & this.bitMasks[i3]) >>> this.bitOffsets[i3]);
/*     */         }
/*     */       }
/*     */ 
/* 490 */       k += this.scanlineStride;
/*     */     }
/* 492 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int getSample(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*     */   {
/* 510 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 511 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 514 */     int i = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1);
/* 515 */     return (i & this.bitMasks[paramInt3]) >>> this.bitOffsets[paramInt3];
/*     */   }
/*     */ 
/*     */   public int[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 537 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height))
/* 538 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     int[] arrayOfInt;
/* 542 */     if (paramArrayOfInt != null)
/* 543 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 545 */       arrayOfInt = new int[paramInt3 * paramInt4];
/*     */     }
/* 547 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 548 */     int j = 0;
/*     */ 
/* 550 */     for (int k = 0; k < paramInt4; k++) {
/* 551 */       for (int m = 0; m < paramInt3; m++) {
/* 552 */         int n = paramDataBuffer.getElem(i + m);
/* 553 */         arrayOfInt[(j++)] = ((n & this.bitMasks[paramInt5]) >>> this.bitOffsets[paramInt5]);
/*     */       }
/*     */ 
/* 556 */       i += this.scanlineStride;
/*     */     }
/* 558 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*     */   {
/* 597 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 598 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 602 */     int i = getTransferType();
/*     */ 
/* 604 */     switch (i)
/*     */     {
/*     */     case 0:
/* 608 */       byte[] arrayOfByte = (byte[])paramObject;
/* 609 */       paramDataBuffer.setElem(paramInt2 * this.scanlineStride + paramInt1, arrayOfByte[0] & 0xFF);
/* 610 */       break;
/*     */     case 1:
/* 614 */       short[] arrayOfShort = (short[])paramObject;
/* 615 */       paramDataBuffer.setElem(paramInt2 * this.scanlineStride + paramInt1, arrayOfShort[0] & 0xFFFF);
/* 616 */       break;
/*     */     case 3:
/* 620 */       int[] arrayOfInt = (int[])paramObject;
/* 621 */       paramDataBuffer.setElem(paramInt2 * this.scanlineStride + paramInt1, arrayOfInt[0]);
/*     */     case 2:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 639 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 640 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 643 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 644 */     int j = paramDataBuffer.getElem(i);
/* 645 */     for (int k = 0; k < this.numBands; k++) {
/* 646 */       j &= (this.bitMasks[k] ^ 0xFFFFFFFF);
/* 647 */       j |= paramArrayOfInt[k] << this.bitOffsets[k] & this.bitMasks[k];
/*     */     }
/* 649 */     paramDataBuffer.setElem(i, j);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 667 */     int i = paramInt1 + paramInt3;
/* 668 */     int j = paramInt2 + paramInt4;
/*     */ 
/* 670 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (j < 0) || (j > this.height))
/*     */     {
/* 673 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 677 */     int k = paramInt2 * this.scanlineStride + paramInt1;
/* 678 */     int m = 0;
/*     */ 
/* 680 */     for (int n = 0; n < paramInt4; n++) {
/* 681 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/* 682 */         int i2 = paramDataBuffer.getElem(k + i1);
/* 683 */         for (int i3 = 0; i3 < this.numBands; i3++) {
/* 684 */           i2 &= (this.bitMasks[i3] ^ 0xFFFFFFFF);
/* 685 */           int i4 = paramArrayOfInt[(m++)];
/* 686 */           i2 |= i4 << this.bitOffsets[i3] & this.bitMasks[i3];
/*     */         }
/*     */ 
/* 689 */         paramDataBuffer.setElem(k + i1, i2);
/*     */       }
/* 691 */       k += this.scanlineStride;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DataBuffer paramDataBuffer)
/*     */   {
/* 710 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 711 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 714 */     int i = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + paramInt1);
/* 715 */     i &= (this.bitMasks[paramInt3] ^ 0xFFFFFFFF);
/* 716 */     i |= paramInt4 << this.bitOffsets[paramInt3] & this.bitMasks[paramInt3];
/* 717 */     paramDataBuffer.setElem(paramInt2 * this.scanlineStride + paramInt1, i);
/*     */   }
/*     */ 
/*     */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 737 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height)) {
/* 738 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 741 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 742 */     int j = 0;
/*     */ 
/* 744 */     for (int k = 0; k < paramInt4; k++) {
/* 745 */       for (int m = 0; m < paramInt3; m++) {
/* 746 */         int n = paramDataBuffer.getElem(i + m);
/* 747 */         n &= (this.bitMasks[paramInt5] ^ 0xFFFFFFFF);
/* 748 */         int i1 = paramArrayOfInt[(j++)];
/* 749 */         n |= i1 << this.bitOffsets[paramInt5] & this.bitMasks[paramInt5];
/* 750 */         paramDataBuffer.setElem(i + m, n);
/*     */       }
/* 752 */       i += this.scanlineStride;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 757 */     if ((paramObject == null) || (!(paramObject instanceof SinglePixelPackedSampleModel))) {
/* 758 */       return false;
/*     */     }
/*     */ 
/* 761 */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)paramObject;
/* 762 */     return (this.width == localSinglePixelPackedSampleModel.width) && (this.height == localSinglePixelPackedSampleModel.height) && (this.numBands == localSinglePixelPackedSampleModel.numBands) && (this.dataType == localSinglePixelPackedSampleModel.dataType) && (Arrays.equals(this.bitMasks, localSinglePixelPackedSampleModel.bitMasks)) && (Arrays.equals(this.bitOffsets, localSinglePixelPackedSampleModel.bitOffsets)) && (Arrays.equals(this.bitSizes, localSinglePixelPackedSampleModel.bitSizes)) && (this.maxBitSize == localSinglePixelPackedSampleModel.maxBitSize) && (this.scanlineStride == localSinglePixelPackedSampleModel.scanlineStride);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 775 */     int i = 0;
/* 776 */     i = this.width;
/* 777 */     i <<= 8;
/* 778 */     i ^= this.height;
/* 779 */     i <<= 8;
/* 780 */     i ^= this.numBands;
/* 781 */     i <<= 8;
/* 782 */     i ^= this.dataType;
/* 783 */     i <<= 8;
/* 784 */     for (int j = 0; j < this.bitMasks.length; j++) {
/* 785 */       i ^= this.bitMasks[j];
/* 786 */       i <<= 8;
/*     */     }
/* 788 */     for (j = 0; j < this.bitOffsets.length; j++) {
/* 789 */       i ^= this.bitOffsets[j];
/* 790 */       i <<= 8;
/*     */     }
/* 792 */     for (j = 0; j < this.bitSizes.length; j++) {
/* 793 */       i ^= this.bitSizes[j];
/* 794 */       i <<= 8;
/*     */     }
/* 796 */     i ^= this.maxBitSize;
/* 797 */     i <<= 8;
/* 798 */     i ^= this.scanlineStride;
/* 799 */     return i;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     ColorModel.loadLibraries();
/*  88 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.SinglePixelPackedSampleModel
 * JD-Core Version:    0.6.2
 */