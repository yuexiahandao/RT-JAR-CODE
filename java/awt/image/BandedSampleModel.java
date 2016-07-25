/*     */ package java.awt.image;
/*     */ 
/*     */ public final class BandedSampleModel extends ComponentSampleModel
/*     */ {
/*     */   public BandedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  85 */     super(paramInt1, paramInt2, paramInt3, 1, paramInt2, createIndicesArray(paramInt4), createOffsetArray(paramInt4));
/*     */   }
/*     */ 
/*     */   public BandedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 112 */     super(paramInt1, paramInt2, paramInt3, 1, paramInt4, paramArrayOfInt1, paramArrayOfInt2);
/*     */   }
/*     */ 
/*     */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*     */   {
/*     */     int[] arrayOfInt;
/* 136 */     if (this.numBanks == 1) {
/* 137 */       arrayOfInt = orderBands(this.bandOffsets, paramInt1 * paramInt2);
/*     */     }
/*     */     else {
/* 140 */       arrayOfInt = new int[this.bandOffsets.length];
/*     */     }
/*     */ 
/* 143 */     BandedSampleModel localBandedSampleModel = new BandedSampleModel(this.dataType, paramInt1, paramInt2, paramInt1, this.bankIndices, arrayOfInt);
/*     */ 
/* 145 */     return localBandedSampleModel;
/*     */   }
/*     */ 
/*     */   public SampleModel createSubsetSampleModel(int[] paramArrayOfInt)
/*     */   {
/* 161 */     if (paramArrayOfInt.length > this.bankIndices.length) {
/* 162 */       throw new RasterFormatException("There are only " + this.bankIndices.length + " bands");
/*     */     }
/*     */ 
/* 165 */     int[] arrayOfInt1 = new int[paramArrayOfInt.length];
/* 166 */     int[] arrayOfInt2 = new int[paramArrayOfInt.length];
/*     */ 
/* 168 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 169 */       arrayOfInt1[i] = this.bankIndices[paramArrayOfInt[i]];
/* 170 */       arrayOfInt2[i] = this.bandOffsets[paramArrayOfInt[i]];
/*     */     }
/*     */ 
/* 173 */     return new BandedSampleModel(this.dataType, this.width, this.height, this.scanlineStride, arrayOfInt1, arrayOfInt2);
/*     */   }
/*     */ 
/*     */   public DataBuffer createDataBuffer()
/*     */   {
/* 186 */     Object localObject = null;
/*     */ 
/* 188 */     int i = this.scanlineStride * this.height;
/* 189 */     switch (this.dataType) {
/*     */     case 0:
/* 191 */       localObject = new DataBufferByte(i, this.numBanks);
/* 192 */       break;
/*     */     case 1:
/* 194 */       localObject = new DataBufferUShort(i, this.numBanks);
/* 195 */       break;
/*     */     case 2:
/* 197 */       localObject = new DataBufferShort(i, this.numBanks);
/* 198 */       break;
/*     */     case 3:
/* 200 */       localObject = new DataBufferInt(i, this.numBanks);
/* 201 */       break;
/*     */     case 4:
/* 203 */       localObject = new DataBufferFloat(i, this.numBanks);
/* 204 */       break;
/*     */     case 5:
/* 206 */       localObject = new DataBufferDouble(i, this.numBanks);
/* 207 */       break;
/*     */     default:
/* 209 */       throw new IllegalArgumentException("dataType is not one of the supported types.");
/*     */     }
/*     */ 
/* 213 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject, DataBuffer paramDataBuffer)
/*     */   {
/* 257 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 258 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 261 */     int i = getTransferType();
/* 262 */     int j = getNumDataElements();
/* 263 */     int k = paramInt2 * this.scanlineStride + paramInt1;
/*     */ 
/* 265 */     switch (i)
/*     */     {
/*     */     case 0:
/*     */       byte[] arrayOfByte;
/* 271 */       if (paramObject == null)
/* 272 */         arrayOfByte = new byte[j];
/*     */       else {
/* 274 */         arrayOfByte = (byte[])paramObject;
/*     */       }
/*     */ 
/* 277 */       for (int m = 0; m < j; m++) {
/* 278 */         arrayOfByte[m] = ((byte)paramDataBuffer.getElem(this.bankIndices[m], k + this.bandOffsets[m]));
/*     */       }
/*     */ 
/* 282 */       paramObject = arrayOfByte;
/* 283 */       break;
/*     */     case 1:
/*     */     case 2:
/*     */       short[] arrayOfShort;
/* 290 */       if (paramObject == null)
/* 291 */         arrayOfShort = new short[j];
/*     */       else {
/* 293 */         arrayOfShort = (short[])paramObject;
/*     */       }
/*     */ 
/* 296 */       for (int n = 0; n < j; n++) {
/* 297 */         arrayOfShort[n] = ((short)paramDataBuffer.getElem(this.bankIndices[n], k + this.bandOffsets[n]));
/*     */       }
/*     */ 
/* 301 */       paramObject = arrayOfShort;
/* 302 */       break;
/*     */     case 3:
/*     */       int[] arrayOfInt;
/* 308 */       if (paramObject == null)
/* 309 */         arrayOfInt = new int[j];
/*     */       else {
/* 311 */         arrayOfInt = (int[])paramObject;
/*     */       }
/*     */ 
/* 314 */       for (int i1 = 0; i1 < j; i1++) {
/* 315 */         arrayOfInt[i1] = paramDataBuffer.getElem(this.bankIndices[i1], k + this.bandOffsets[i1]);
/*     */       }
/*     */ 
/* 319 */       paramObject = arrayOfInt;
/* 320 */       break;
/*     */     case 4:
/*     */       float[] arrayOfFloat;
/* 326 */       if (paramObject == null)
/* 327 */         arrayOfFloat = new float[j];
/*     */       else {
/* 329 */         arrayOfFloat = (float[])paramObject;
/*     */       }
/*     */ 
/* 332 */       for (int i2 = 0; i2 < j; i2++) {
/* 333 */         arrayOfFloat[i2] = paramDataBuffer.getElemFloat(this.bankIndices[i2], k + this.bandOffsets[i2]);
/*     */       }
/*     */ 
/* 337 */       paramObject = arrayOfFloat;
/* 338 */       break;
/*     */     case 5:
/*     */       double[] arrayOfDouble;
/* 344 */       if (paramObject == null)
/* 345 */         arrayOfDouble = new double[j];
/*     */       else {
/* 347 */         arrayOfDouble = (double[])paramObject;
/*     */       }
/*     */ 
/* 350 */       for (int i3 = 0; i3 < j; i3++) {
/* 351 */         arrayOfDouble[i3] = paramDataBuffer.getElemDouble(this.bankIndices[i3], k + this.bandOffsets[i3]);
/*     */       }
/*     */ 
/* 355 */       paramObject = arrayOfDouble;
/*     */     }
/*     */ 
/* 359 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public int[] getPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 374 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height))
/* 375 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     int[] arrayOfInt;
/* 381 */     if (paramArrayOfInt != null)
/* 382 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 384 */       arrayOfInt = new int[this.numBands];
/*     */     }
/*     */ 
/* 387 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 388 */     for (int j = 0; j < this.numBands; j++) {
/* 389 */       arrayOfInt[j] = paramDataBuffer.getElem(this.bankIndices[j], i + this.bandOffsets[j]);
/*     */     }
/*     */ 
/* 392 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 411 */     int i = paramInt1 + paramInt3;
/* 412 */     int j = paramInt2 + paramInt4;
/*     */ 
/* 414 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (j < 0) || (j > this.height))
/*     */     {
/* 417 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     int[] arrayOfInt;
/* 422 */     if (paramArrayOfInt != null)
/* 423 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 425 */       arrayOfInt = new int[paramInt3 * paramInt4 * this.numBands];
/*     */     }
/*     */ 
/* 428 */     for (int k = 0; k < this.numBands; k++) {
/* 429 */       int m = paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[k];
/* 430 */       int n = k;
/* 431 */       int i1 = this.bankIndices[k];
/*     */ 
/* 433 */       for (int i2 = 0; i2 < paramInt4; i2++) {
/* 434 */         int i3 = m;
/* 435 */         for (int i4 = 0; i4 < paramInt3; i4++) {
/* 436 */           arrayOfInt[n] = paramDataBuffer.getElem(i1, i3++);
/* 437 */           n += this.numBands;
/*     */         }
/* 439 */         m += this.scanlineStride;
/*     */       }
/*     */     }
/* 442 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int getSample(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*     */   {
/* 459 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 460 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 463 */     int i = paramDataBuffer.getElem(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3]);
/*     */ 
/* 466 */     return i;
/*     */   }
/*     */ 
/*     */   public float getSampleFloat(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*     */   {
/* 483 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 484 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 488 */     float f = paramDataBuffer.getElemFloat(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3]);
/*     */ 
/* 490 */     return f;
/*     */   }
/*     */ 
/*     */   public double getSampleDouble(int paramInt1, int paramInt2, int paramInt3, DataBuffer paramDataBuffer)
/*     */   {
/* 507 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 508 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 512 */     double d = paramDataBuffer.getElemDouble(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3]);
/*     */ 
/* 514 */     return d;
/*     */   }
/*     */ 
/*     */   public int[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 536 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height))
/* 537 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     int[] arrayOfInt;
/* 541 */     if (paramArrayOfInt != null)
/* 542 */       arrayOfInt = paramArrayOfInt;
/*     */     else {
/* 544 */       arrayOfInt = new int[paramInt3 * paramInt4];
/*     */     }
/*     */ 
/* 547 */     int i = paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt5];
/* 548 */     int j = 0;
/* 549 */     int k = this.bankIndices[paramInt5];
/*     */ 
/* 551 */     for (int m = 0; m < paramInt4; m++) {
/* 552 */       int n = i;
/* 553 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/* 554 */         arrayOfInt[(j++)] = paramDataBuffer.getElem(k, n++);
/*     */       }
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
/* 601 */     int i = getTransferType();
/* 602 */     int j = getNumDataElements();
/* 603 */     int k = paramInt2 * this.scanlineStride + paramInt1;
/*     */ 
/* 605 */     switch (i)
/*     */     {
/*     */     case 0:
/* 609 */       byte[] arrayOfByte = (byte[])paramObject;
/*     */ 
/* 611 */       for (int m = 0; m < j; m++) {
/* 612 */         paramDataBuffer.setElem(this.bankIndices[m], k + this.bandOffsets[m], arrayOfByte[m] & 0xFF);
/*     */       }
/*     */ 
/* 615 */       break;
/*     */     case 1:
/*     */     case 2:
/* 620 */       short[] arrayOfShort = (short[])paramObject;
/*     */ 
/* 622 */       for (int n = 0; n < j; n++) {
/* 623 */         paramDataBuffer.setElem(this.bankIndices[n], k + this.bandOffsets[n], arrayOfShort[n] & 0xFFFF);
/*     */       }
/*     */ 
/* 626 */       break;
/*     */     case 3:
/* 630 */       int[] arrayOfInt = (int[])paramObject;
/*     */ 
/* 632 */       for (int i1 = 0; i1 < j; i1++) {
/* 633 */         paramDataBuffer.setElem(this.bankIndices[i1], k + this.bandOffsets[i1], arrayOfInt[i1]);
/*     */       }
/*     */ 
/* 636 */       break;
/*     */     case 4:
/* 640 */       float[] arrayOfFloat = (float[])paramObject;
/*     */ 
/* 642 */       for (int i2 = 0; i2 < j; i2++) {
/* 643 */         paramDataBuffer.setElemFloat(this.bankIndices[i2], k + this.bandOffsets[i2], arrayOfFloat[i2]);
/*     */       }
/*     */ 
/* 646 */       break;
/*     */     case 5:
/* 650 */       double[] arrayOfDouble = (double[])paramObject;
/*     */ 
/* 652 */       for (int i3 = 0; i3 < j; i3++)
/* 653 */         paramDataBuffer.setElemDouble(this.bankIndices[i3], k + this.bandOffsets[i3], arrayOfDouble[i3]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixel(int paramInt1, int paramInt2, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 672 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 673 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 676 */     int i = paramInt2 * this.scanlineStride + paramInt1;
/* 677 */     for (int j = 0; j < this.numBands; j++)
/* 678 */       paramDataBuffer.setElem(this.bankIndices[j], i + this.bandOffsets[j], paramArrayOfInt[j]);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 698 */     int i = paramInt1 + paramInt3;
/* 699 */     int j = paramInt2 + paramInt4;
/*     */ 
/* 701 */     if ((paramInt1 < 0) || (paramInt1 >= this.width) || (paramInt3 > this.width) || (i < 0) || (i > this.width) || (paramInt2 < 0) || (paramInt2 >= this.height) || (paramInt4 > this.height) || (j < 0) || (j > this.height))
/*     */     {
/* 704 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 708 */     for (int k = 0; k < this.numBands; k++) {
/* 709 */       int m = paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[k];
/* 710 */       int n = k;
/* 711 */       int i1 = this.bankIndices[k];
/*     */ 
/* 713 */       for (int i2 = 0; i2 < paramInt4; i2++) {
/* 714 */         int i3 = m;
/* 715 */         for (int i4 = 0; i4 < paramInt3; i4++) {
/* 716 */           paramDataBuffer.setElem(i1, i3++, paramArrayOfInt[n]);
/* 717 */           n += this.numBands;
/*     */         }
/* 719 */         m += this.scanlineStride;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, DataBuffer paramDataBuffer)
/*     */   {
/* 739 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 740 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 743 */     paramDataBuffer.setElem(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3], paramInt4);
/*     */   }
/*     */ 
/*     */   public void setSample(int paramInt1, int paramInt2, int paramInt3, float paramFloat, DataBuffer paramDataBuffer)
/*     */   {
/* 763 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 764 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 767 */     paramDataBuffer.setElemFloat(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3], paramFloat);
/*     */   }
/*     */ 
/*     */   public void setSample(int paramInt1, int paramInt2, int paramInt3, double paramDouble, DataBuffer paramDataBuffer)
/*     */   {
/* 787 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.width) || (paramInt2 >= this.height)) {
/* 788 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 791 */     paramDataBuffer.setElemDouble(this.bankIndices[paramInt3], paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt3], paramDouble);
/*     */   }
/*     */ 
/*     */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt, DataBuffer paramDataBuffer)
/*     */   {
/* 812 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt3 > this.width) || (paramInt2 + paramInt4 > this.height)) {
/* 813 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 816 */     int i = paramInt2 * this.scanlineStride + paramInt1 + this.bandOffsets[paramInt5];
/* 817 */     int j = 0;
/* 818 */     int k = this.bankIndices[paramInt5];
/*     */ 
/* 820 */     for (int m = 0; m < paramInt4; m++) {
/* 821 */       int n = i;
/* 822 */       for (int i1 = 0; i1 < paramInt3; i1++) {
/* 823 */         paramDataBuffer.setElem(k, n++, paramArrayOfInt[(j++)]);
/*     */       }
/* 825 */       i += this.scanlineStride;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int[] createOffsetArray(int paramInt) {
/* 830 */     int[] arrayOfInt = new int[paramInt];
/* 831 */     for (int i = 0; i < paramInt; i++) {
/* 832 */       arrayOfInt[i] = 0;
/*     */     }
/* 834 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static int[] createIndicesArray(int paramInt) {
/* 838 */     int[] arrayOfInt = new int[paramInt];
/* 839 */     for (int i = 0; i < paramInt; i++) {
/* 840 */       arrayOfInt[i] = i;
/*     */     }
/* 842 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 847 */     return super.hashCode() ^ 0x2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.BandedSampleModel
 * JD-Core Version:    0.6.2
 */