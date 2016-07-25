/*     */ package java.awt.image;
/*     */ 
/*     */ public class MultiPixelPackedSampleModel extends SampleModel
/*     */ {
/*     */   int pixelBitStride;
/*     */   int bitMask;
/*     */   int pixelsPerDataElement;
/*     */   int dataElementSize;
/*     */   int dataBitOffset;
/*     */   int scanlineStride;
/*     */ 
/*     */   public MultiPixelPackedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 107 */     this(paramInt1, paramInt2, paramInt3, paramInt4, (paramInt2 * paramInt4 + DataBuffer.getDataTypeSize(paramInt1) - 1) / DataBuffer.getDataTypeSize(paramInt1), 0);
/*     */ 
/* 112 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 3))
/*     */     {
/* 115 */       throw new IllegalArgumentException("Unsupported data type " + paramInt1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MultiPixelPackedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 147 */     super(paramInt1, paramInt2, paramInt3, 1);
/* 148 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 3))
/*     */     {
/* 151 */       throw new IllegalArgumentException("Unsupported data type " + paramInt1);
/*     */     }
/*     */ 
/* 154 */     this.dataType = paramInt1;
/* 155 */     this.pixelBitStride = paramInt4;
/* 156 */     this.scanlineStride = paramInt5;
/* 157 */     this.dataBitOffset = paramInt6;
/* 158 */     this.dataElementSize = DataBuffer.getDataTypeSize(paramInt1);
/* 159 */     this.pixelsPerDataElement = (this.dataElementSize / paramInt4);
/* 160 */     if (this.pixelsPerDataElement * paramInt4 != this.dataElementSize) {
/* 161 */       throw new RasterFormatException("MultiPixelPackedSampleModel does not allow pixels to span data element boundaries");
/*     */     }
/*     */ 
/* 165 */     this.bitMask = ((1 << paramInt4) - 1);
/*     */   }
/*     */ 
/*     */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = new MultiPixelPackedSampleModel(this.dataType, paramInt1, paramInt2, this.pixelBitStride);
/*     */ 
/* 186 */     return localMultiPixelPackedSampleModel;
/*     */   }
/*     */ 
/*     */   public DataBuffer createDataBuffer()
/*     */   {
/* 199 */     Object localObject = null;
/*     */ 
/* 201 */     int i = this.scanlineStride * this.height;
/* 202 */     switch (this.dataType) {
/*     */     case 0:
/* 204 */       localObject = new DataBufferByte(i + (this.dataBitOffset + 7) / 8);
/* 205 */       break;
/*     */     case 1:
/* 207 */       localObject = new DataBufferUShort(i + (this.dataBitOffset + 15) / 16);
/* 208 */       break;
/*     */     case 3:
/* 210 */       localObject = new DataBufferInt(i + (this.dataBitOffset + 31) / 32);
/*     */     case 2:
/*     */     }
/* 213 */     return localObject;
/*     */   }
/*     */ 
/*     */   public int getNumDataElements()
/*     */   {
/* 224 */     return 1;
/*     */   }
/*     */ 
/*     */   public int[] getSampleSize()
/*     */   {
/* 232 */     int[] arrayOfInt = { this.pixelBitStride };
/* 233 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int getSampleSize(int paramInt)
/*     */   {
/* 242 */     return this.pixelBitStride;
/*     */   }
/*     */ 
/*     */   public int getOffset(int paramInt1, int paramInt2)
/*     */   {
/* 252 */     int i = paramInt2 * this.scanlineStride;
/* 253 */     i += (paramInt1 * this.pixelBitStride + this.dataBitOffset) / this.dataElementSize;
/* 254 */     return i;
/*     */   }
/*     */ 
/*     */   public int getBitOffset(int paramInt)
/*     */   {
/* 265 */     return (paramInt * this.pixelBitStride + this.dataBitOffset) % this.dataElementSize;
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 274 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelBitStride()
/*     */   {
/* 284 */     return this.pixelBitStride;
/*     */   }
/*     */ 
/*     */   public int getDataBitOffset()
/*     */   {
/* 293 */     return this.dataBitOffset;
/*     */   }
/*     */ 
/*     */   public int getTransferType()
/*     */   {
/* 306 */     if (this.pixelBitStride > 16)
/* 307 */       return 3;
/* 308 */     if (this.pixelBitStride > 8) {
/* 309 */       return 1;
/*     */     }
/* 311 */     return 0;
/*     */   }
/*     */ 
/*     */   public SampleModel createSubsetSampleModel(int[] paramArrayOfInt)
/*     */   {
/* 330 */     if ((paramArrayOfInt != null) && 
/* 331 */       (paramArrayOfInt.length != 1)) {
/* 332 */       throw new RasterFormatException("MultiPixelPackedSampleModel has only one band.");
/*     */     }
/*     */ 
/* 335 */     SampleModel localSampleModel = createCompatibleSampleModel(this.width, this.height);
/* 336 */     return localSampleModel;
/*     */   }
/*     */ 
/*     */   public int getSample(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*     */   {
/* 357 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height) || (paramInt3 != 0))
/*     */     {
/* 359 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 362 */     int i = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 363 */     int j = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + i / this.dataElementSize);
/* 364 */     int k = this.dataElementSize - (i & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 366 */     return j >> k & this.bitMask;
/*     */   }
/*     */ 
/*     */   public void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DataBuffer paramDataBuffer)
/*     */   {
/* 387 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height) || (paramInt3 != 0))
/*     */     {
/* 389 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 392 */     int i = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 393 */     int j = paramInt2 * this.scanlineStride + i / this.dataElementSize;
/* 394 */     int k = this.dataElementSize - (i & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 396 */     int m = paramDataBuffer.getElem(j);
/* 397 */     m &= (this.bitMask << k ^ 0xFFFFFFFF);
/* 398 */     m |= (paramInt4 & this.bitMask) << k;
/* 399 */     paramDataBuffer.setElem(j, m);
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*     */   {
/* 453 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 454 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 458 */     int i = getTransferType();
/* 459 */     int j = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 460 */     int k = this.dataElementSize - (j & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 462 */     int m = 0;
/*     */ 
/* 464 */     switch (i)
/*     */     {
/*     */     case 0:
/*     */       byte[] arrayOfByte;
/* 470 */       if (paramObject == null)
/* 471 */         arrayOfByte = new byte[1];
/*     */       else {
/* 473 */         arrayOfByte = (byte[])paramObject;
/*     */       }
/* 475 */       m = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + j / this.dataElementSize);
/*     */ 
/* 477 */       arrayOfByte[0] = ((byte)(m >> k & this.bitMask));
/*     */ 
/* 479 */       paramObject = arrayOfByte;
/* 480 */       break;
/*     */     case 1:
/*     */       short[] arrayOfShort;
/* 486 */       if (paramObject == null)
/* 487 */         arrayOfShort = new short[1];
/*     */       else {
/* 489 */         arrayOfShort = (short[])paramObject;
/*     */       }
/* 491 */       m = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + j / this.dataElementSize);
/*     */ 
/* 493 */       arrayOfShort[0] = ((short)(m >> k & this.bitMask));
/*     */ 
/* 495 */       paramObject = arrayOfShort;
/* 496 */       break;
/*     */     case 3:
/*     */       int[] arrayOfInt;
/* 502 */       if (paramObject == null)
/* 503 */         arrayOfInt = new int[1];
/*     */       else {
/* 505 */         arrayOfInt = (int[])paramObject;
/*     */       }
/* 507 */       m = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + j / this.dataElementSize);
/*     */ 
/* 509 */       arrayOfInt[0] = (m >> k & this.bitMask);
/*     */ 
/* 511 */       paramObject = arrayOfInt;
/*     */     case 2:
/*     */     }
/*     */ 
/* 515 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public int[] getPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 534 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height))
/* 535 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     int[] arrayOfInt;
/* 539 */     if (paramArrayOfInt != null)
/* 540 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 542 */       arrayOfInt = new int[this.numBands];
/*     */     }
/* 544 */     int i = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 545 */     int j = paramDataBuffer.getElem(paramInt2 * this.scanlineStride + i / this.dataElementSize);
/* 546 */     int k = this.dataElementSize - (i & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 548 */     arrayOfInt[0] = (j >> k & this.bitMask);
/* 549 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*     */   {
/* 593 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 594 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 598 */     int i = getTransferType();
/* 599 */     int j = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 600 */     int k = paramInt2 * this.scanlineStride + j / this.dataElementSize;
/* 601 */     int m = this.dataElementSize - (j & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 603 */     int n = paramDataBuffer.getElem(k);
/* 604 */     n &= (this.bitMask << m ^ 0xFFFFFFFF);
/*     */ 
/* 606 */     switch (i)
/*     */     {
/*     */     case 0:
/* 610 */       byte[] arrayOfByte = (byte[])paramObject;
/* 611 */       n |= (arrayOfByte[0] & 0xFF & this.bitMask) << m;
/* 612 */       paramDataBuffer.setElem(k, n);
/* 613 */       break;
/*     */     case 1:
/* 617 */       short[] arrayOfShort = (short[])paramObject;
/* 618 */       n |= (arrayOfShort[0] & 0xFFFF & this.bitMask) << m;
/* 619 */       paramDataBuffer.setElem(k, n);
/* 620 */       break;
/*     */     case 3:
/* 624 */       int[] arrayOfInt = (int[])paramObject;
/* 625 */       n |= (arrayOfInt[0] & this.bitMask) << m;
/* 626 */       paramDataBuffer.setElem(k, n);
/*     */     case 2:
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 643 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 644 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 647 */     int i = this.dataBitOffset + paramInt1 * this.pixelBitStride;
/* 648 */     int j = paramInt2 * this.scanlineStride + i / this.dataElementSize;
/* 649 */     int k = this.dataElementSize - (i & this.dataElementSize - 1) - this.pixelBitStride;
/*     */ 
/* 651 */     int m = paramDataBuffer.getElem(j);
/* 652 */     m &= (this.bitMask << k ^ 0xFFFFFFFF);
/* 653 */     m |= (paramArrayOfInt[0] & this.bitMask) << k;
/* 654 */     paramDataBuffer.setElem(j, m);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 658 */     if ((paramObject == null) || (!(paramObject instanceof MultiPixelPackedSampleModel))) {
/* 659 */       return false;
/*     */     }
/*     */ 
/* 662 */     MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)paramObject;
/* 663 */     return (this.width == localMultiPixelPackedSampleModel.width) && (this.height == localMultiPixelPackedSampleModel.height) && (this.numBands == localMultiPixelPackedSampleModel.numBands) && (this.dataType == localMultiPixelPackedSampleModel.dataType) && (this.pixelBitStride == localMultiPixelPackedSampleModel.pixelBitStride) && (this.bitMask == localMultiPixelPackedSampleModel.bitMask) && (this.pixelsPerDataElement == localMultiPixelPackedSampleModel.pixelsPerDataElement) && (this.dataElementSize == localMultiPixelPackedSampleModel.dataElementSize) && (this.dataBitOffset == localMultiPixelPackedSampleModel.dataBitOffset) && (this.scanlineStride == localMultiPixelPackedSampleModel.scanlineStride);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 677 */     int i = 0;
/* 678 */     i = this.width;
/* 679 */     i <<= 8;
/* 680 */     i ^= this.height;
/* 681 */     i <<= 8;
/* 682 */     i ^= this.numBands;
/* 683 */     i <<= 8;
/* 684 */     i ^= this.dataType;
/* 685 */     i <<= 8;
/* 686 */     i ^= this.pixelBitStride;
/* 687 */     i <<= 8;
/* 688 */     i ^= this.bitMask;
/* 689 */     i <<= 8;
/* 690 */     i ^= this.pixelsPerDataElement;
/* 691 */     i <<= 8;
/* 692 */     i ^= this.dataElementSize;
/* 693 */     i <<= 8;
/* 694 */     i ^= this.dataBitOffset;
/* 695 */     i <<= 8;
/* 696 */     i ^= this.scanlineStride;
/* 697 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.MultiPixelPackedSampleModel
 * JD-Core Version:    0.6.2
 */