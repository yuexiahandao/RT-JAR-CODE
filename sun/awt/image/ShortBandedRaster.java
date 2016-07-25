/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BandedSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferUShort;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RasterFormatException;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ShortBandedRaster extends SunWritableRaster
/*     */ {
/*     */   int[] dataOffsets;
/*     */   int scanlineStride;
/*     */   short[][] data;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */ 
/*     */   public ShortBandedRaster(SampleModel paramSampleModel, Point paramPoint)
/*     */   {
/*  77 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortBandedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*     */   {
/* 100 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortBandedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ShortBandedRaster paramShortBandedRaster)
/*     */   {
/* 131 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramShortBandedRaster);
/* 132 */     this.maxX = (this.minX + this.width);
/* 133 */     this.maxY = (this.minY + this.height);
/* 134 */     if (!(paramDataBuffer instanceof DataBufferUShort)) {
/* 135 */       throw new RasterFormatException("ShortBandedRaster must have ushort DataBuffers");
/*     */     }
/*     */ 
/* 138 */     DataBufferUShort localDataBufferUShort = (DataBufferUShort)paramDataBuffer;
/*     */ 
/* 140 */     if ((paramSampleModel instanceof BandedSampleModel)) {
/* 141 */       BandedSampleModel localBandedSampleModel = (BandedSampleModel)paramSampleModel;
/* 142 */       this.scanlineStride = localBandedSampleModel.getScanlineStride();
/* 143 */       int[] arrayOfInt1 = localBandedSampleModel.getBankIndices();
/* 144 */       int[] arrayOfInt2 = localBandedSampleModel.getBandOffsets();
/* 145 */       int[] arrayOfInt3 = localDataBufferUShort.getOffsets();
/* 146 */       this.dataOffsets = new int[arrayOfInt1.length];
/* 147 */       this.data = new short[arrayOfInt1.length][];
/* 148 */       int i = paramRectangle.x - paramPoint.x;
/* 149 */       int j = paramRectangle.y - paramPoint.y;
/* 150 */       for (int k = 0; k < arrayOfInt1.length; k++) {
/* 151 */         this.data[k] = stealData(localDataBufferUShort, arrayOfInt1[k]);
/* 152 */         this.dataOffsets[k] = (arrayOfInt3[arrayOfInt1[k]] + i + j * this.scanlineStride + arrayOfInt2[k]);
/*     */       }
/*     */     }
/*     */     else {
/* 156 */       throw new RasterFormatException("ShortBandedRasters must have BandedSampleModels");
/*     */     }
/*     */ 
/* 159 */     verify();
/*     */   }
/*     */ 
/*     */   public int[] getDataOffsets()
/*     */   {
/* 168 */     return (int[])this.dataOffsets.clone();
/*     */   }
/*     */ 
/*     */   public int getDataOffset(int paramInt)
/*     */   {
/* 178 */     return this.dataOffsets[paramInt];
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 187 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelStride()
/*     */   {
/* 195 */     return 1;
/*     */   }
/*     */ 
/*     */   public short[][] getDataStorage()
/*     */   {
/* 202 */     return this.data;
/*     */   }
/*     */ 
/*     */   public short[] getDataStorage(int paramInt)
/*     */   {
/* 209 */     return this.data[paramInt];
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 229 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 231 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort;
/* 235 */     if (paramObject == null)
/* 236 */       arrayOfShort = new short[this.numDataElements];
/*     */     else {
/* 238 */       arrayOfShort = (short[])paramObject;
/*     */     }
/*     */ 
/* 241 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 243 */     for (int j = 0; j < this.numDataElements; j++) {
/* 244 */       arrayOfShort[j] = this.data[j][(this.dataOffsets[j] + i)];
/*     */     }
/*     */ 
/* 247 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 275 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 277 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort1;
/* 281 */     if (paramObject == null)
/* 282 */       arrayOfShort1 = new short[this.numDataElements * paramInt3 * paramInt4];
/*     */     else {
/* 284 */       arrayOfShort1 = (short[])paramObject;
/*     */     }
/* 286 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 288 */     for (int j = 0; j < this.numDataElements; j++) {
/* 289 */       int k = j;
/* 290 */       short[] arrayOfShort2 = this.data[j];
/* 291 */       int m = this.dataOffsets[j];
/*     */ 
/* 293 */       int n = i;
/* 294 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 295 */         int i2 = m + n;
/* 296 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 297 */           arrayOfShort1[k] = arrayOfShort2[(i2++)];
/* 298 */           k += this.numDataElements;
/*     */         }
/* 294 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 303 */     return arrayOfShort1;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 328 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 330 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 333 */     if (paramArrayOfShort == null) {
/* 334 */       paramArrayOfShort = new short[this.scanlineStride * paramInt4];
/*     */     }
/* 336 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) + this.dataOffsets[paramInt5];
/*     */ 
/* 338 */     if (this.scanlineStride == paramInt3) {
/* 339 */       System.arraycopy(this.data[paramInt5], i, paramArrayOfShort, 0, paramInt3 * paramInt4);
/*     */     } else {
/* 341 */       int j = 0;
/* 342 */       for (int k = 0; k < paramInt4; i += this.scanlineStride) {
/* 343 */         System.arraycopy(this.data[paramInt5], i, paramArrayOfShort, j, paramInt3);
/* 344 */         j += paramInt3;
/*     */ 
/* 342 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 348 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 374 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 376 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 379 */     if (paramArrayOfShort == null) {
/* 380 */       paramArrayOfShort = new short[this.numDataElements * this.scanlineStride * paramInt4];
/*     */     }
/* 382 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 384 */     for (int j = 0; j < this.numDataElements; j++) {
/* 385 */       int k = j;
/* 386 */       short[] arrayOfShort = this.data[j];
/* 387 */       int m = this.dataOffsets[j];
/*     */ 
/* 389 */       int n = i;
/* 390 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 391 */         int i2 = m + n;
/* 392 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 393 */           paramArrayOfShort[k] = arrayOfShort[(i2++)];
/* 394 */           k += this.numDataElements;
/*     */         }
/* 390 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 399 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 415 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 417 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 420 */     short[] arrayOfShort = (short[])paramObject;
/* 421 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/* 422 */     for (int j = 0; j < this.numDataElements; j++) {
/* 423 */       this.data[j][(this.dataOffsets[j] + i)] = arrayOfShort[j];
/*     */     }
/*     */ 
/* 426 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*     */   {
/* 438 */     int i = paramInt1 + paramRaster.getMinX();
/* 439 */     int j = paramInt2 + paramRaster.getMinY();
/* 440 */     int k = paramRaster.getWidth();
/* 441 */     int m = paramRaster.getHeight();
/* 442 */     if ((i < this.minX) || (j < this.minY) || (i + k > this.maxX) || (j + m > this.maxY))
/*     */     {
/* 444 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 448 */     setDataElements(i, j, k, m, paramRaster);
/*     */   }
/*     */ 
/*     */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Raster paramRaster)
/*     */   {
/* 467 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 468 */       return;
/*     */     }
/*     */ 
/* 473 */     int i = paramRaster.getMinX();
/* 474 */     int j = paramRaster.getMinY();
/* 475 */     Object localObject = null;
/*     */ 
/* 481 */     for (int k = 0; k < paramInt4; k++)
/*     */     {
/* 483 */       localObject = paramRaster.getDataElements(i, j + k, paramInt3, 1, localObject);
/*     */ 
/* 485 */       setDataElements(paramInt1, paramInt2 + k, paramInt3, 1, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 512 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 514 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 517 */     short[] arrayOfShort1 = (short[])paramObject;
/* 518 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 520 */     for (int j = 0; j < this.numDataElements; j++) {
/* 521 */       int k = j;
/* 522 */       short[] arrayOfShort2 = this.data[j];
/* 523 */       int m = this.dataOffsets[j];
/*     */ 
/* 525 */       int n = i;
/* 526 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 527 */         int i2 = m + n;
/* 528 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 529 */           arrayOfShort2[(i2++)] = arrayOfShort1[k];
/* 530 */           k += this.numDataElements;
/*     */         }
/* 526 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 535 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 559 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 561 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 564 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) + this.dataOffsets[paramInt5];
/*     */ 
/* 566 */     int j = 0;
/*     */ 
/* 570 */     if (this.scanlineStride == paramInt3)
/* 571 */       System.arraycopy(paramArrayOfShort, 0, this.data[paramInt5], i, paramInt3 * paramInt4);
/*     */     else {
/* 573 */       for (int k = 0; k < paramInt4; i += this.scanlineStride) {
/* 574 */         System.arraycopy(paramArrayOfShort, j, this.data[paramInt5], i, paramInt3);
/* 575 */         j += paramInt3;
/*     */ 
/* 573 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 579 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 600 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 602 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 605 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 607 */     for (int j = 0; j < this.numDataElements; j++) {
/* 608 */       int k = j;
/* 609 */       short[] arrayOfShort = this.data[j];
/* 610 */       int m = this.dataOffsets[j];
/*     */ 
/* 612 */       int n = i;
/* 613 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 614 */         int i2 = m + n;
/* 615 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 616 */           arrayOfShort[(i2++)] = paramArrayOfShort[k];
/* 617 */           k += this.numDataElements;
/*     */         }
/* 613 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 622 */     markDirty();
/*     */   }
/*     */ 
/*     */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 649 */     if (paramInt1 < this.minX) {
/* 650 */       throw new RasterFormatException("x lies outside raster");
/*     */     }
/* 652 */     if (paramInt2 < this.minY) {
/* 653 */       throw new RasterFormatException("y lies outside raster");
/*     */     }
/* 655 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 656 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*     */     }
/* 658 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 659 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*     */     SampleModel localSampleModel;
/* 664 */     if (paramArrayOfInt != null)
/* 665 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*     */     else {
/* 667 */       localSampleModel = this.sampleModel;
/*     */     }
/* 669 */     int i = paramInt5 - paramInt1;
/* 670 */     int j = paramInt6 - paramInt2;
/*     */ 
/* 672 */     return new ShortBandedRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*     */   }
/*     */ 
/*     */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 704 */     return createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*     */   {
/* 712 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 713 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*     */     }
/*     */ 
/* 717 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*     */ 
/* 719 */     return new ShortBandedRaster(localSampleModel, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster()
/*     */   {
/* 729 */     return createCompatibleWritableRaster(this.width, this.height);
/*     */   }
/*     */ 
/*     */   private void verify()
/*     */   {
/* 743 */     if ((this.width <= 0) || (this.height <= 0) || (this.height > 2147483647 / this.width))
/*     */     {
/* 746 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/*     */ 
/* 749 */     if ((this.scanlineStride < 0) || (this.scanlineStride > 2147483647 / this.height))
/*     */     {
/* 753 */       throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */     }
/*     */ 
/* 757 */     if ((this.minX - this.sampleModelTranslateX < 0L) || (this.minY - this.sampleModelTranslateY < 0L))
/*     */     {
/* 760 */       throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
/*     */     }
/*     */ 
/* 765 */     if ((this.height > 1) || (this.minY - this.sampleModelTranslateY > 0))
/*     */     {
/* 767 */       for (i = 0; i < this.data.length; i++) {
/* 768 */         if (this.scanlineStride > this.data[i].length) {
/* 769 */           throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 776 */     for (int i = 0; i < this.dataOffsets.length; i++) {
/* 777 */       if (this.dataOffsets[i] < 0) {
/* 778 */         throw new RasterFormatException("Data offsets for band " + i + "(" + this.dataOffsets[i] + ") must be >= 0");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 784 */     i = (this.height - 1) * this.scanlineStride;
/* 785 */     if (this.width - 1 > 2147483647 - i) {
/* 786 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/* 788 */     int j = i + (this.width - 1);
/*     */ 
/* 790 */     int k = 0;
/*     */ 
/* 793 */     for (int n = 0; n < this.numDataElements; n++) {
/* 794 */       if (this.dataOffsets[n] > 2147483647 - j) {
/* 795 */         throw new RasterFormatException("Invalid raster dimension");
/*     */       }
/* 797 */       int m = j + this.dataOffsets[n];
/* 798 */       if (m > k) {
/* 799 */         k = m;
/*     */       }
/*     */     }
/* 802 */     for (n = 0; n < this.numDataElements; n++)
/* 803 */       if (this.data[n].length <= k)
/* 804 */         throw new RasterFormatException("Data array too small (should be > " + k + " )");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 811 */     return new String("ShortBandedRaster: width = " + this.width + " height = " + this.height + " #numBands " + this.numBands + " #dataElements " + this.numDataElements);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ShortBandedRaster
 * JD-Core Version:    0.6.2
 */