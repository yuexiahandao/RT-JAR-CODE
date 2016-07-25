/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferUShort;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RasterFormatException;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ShortComponentRaster extends SunWritableRaster
/*     */ {
/*     */   protected int bandOffset;
/*     */   protected int[] dataOffsets;
/*     */   protected int scanlineStride;
/*     */   protected int pixelStride;
/*     */   protected short[] data;
/*     */   int type;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public ShortComponentRaster(SampleModel paramSampleModel, Point paramPoint)
/*     */   {
/*  96 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortComponentRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*     */   {
/* 120 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortComponentRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ShortComponentRaster paramShortComponentRaster)
/*     */   {
/* 154 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramShortComponentRaster);
/* 155 */     this.maxX = (this.minX + this.width);
/* 156 */     this.maxY = (this.minY + this.height);
/*     */ 
/* 158 */     if (!(paramDataBuffer instanceof DataBufferUShort)) {
/* 159 */       throw new RasterFormatException("ShortComponentRasters must have short DataBuffers");
/*     */     }
/*     */ 
/* 163 */     DataBufferUShort localDataBufferUShort = (DataBufferUShort)paramDataBuffer;
/* 164 */     this.data = stealData(localDataBufferUShort, 0);
/* 165 */     if (localDataBufferUShort.getNumBanks() != 1) {
/* 166 */       throw new RasterFormatException("DataBuffer for ShortComponentRasters must only have 1 bank.");
/*     */     }
/*     */ 
/* 170 */     int i = localDataBufferUShort.getOffset();
/*     */     Object localObject;
/*     */     int j;
/*     */     int k;
/* 172 */     if ((paramSampleModel instanceof ComponentSampleModel)) {
/* 173 */       localObject = (ComponentSampleModel)paramSampleModel;
/* 174 */       this.type = 2;
/* 175 */       this.scanlineStride = ((ComponentSampleModel)localObject).getScanlineStride();
/* 176 */       this.pixelStride = ((ComponentSampleModel)localObject).getPixelStride();
/* 177 */       this.dataOffsets = ((ComponentSampleModel)localObject).getBandOffsets();
/* 178 */       j = paramRectangle.x - paramPoint.x;
/* 179 */       k = paramRectangle.y - paramPoint.y;
/* 180 */       for (int m = 0; m < getNumDataElements(); m++) {
/* 181 */         this.dataOffsets[m] += i + j * this.pixelStride + k * this.scanlineStride;
/*     */       }
/*     */     }
/* 184 */     else if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 185 */       localObject = (SinglePixelPackedSampleModel)paramSampleModel;
/*     */ 
/* 187 */       this.type = 8;
/* 188 */       this.scanlineStride = ((SinglePixelPackedSampleModel)localObject).getScanlineStride();
/* 189 */       this.pixelStride = 1;
/* 190 */       this.dataOffsets = new int[1];
/* 191 */       this.dataOffsets[0] = i;
/* 192 */       j = paramRectangle.x - paramPoint.x;
/* 193 */       k = paramRectangle.y - paramPoint.y;
/* 194 */       this.dataOffsets[0] += j + k * this.scanlineStride;
/*     */     } else {
/* 196 */       throw new RasterFormatException("ShortComponentRasters must haveComponentSampleModel or SinglePixelPackedSampleModel");
/*     */     }
/*     */ 
/* 199 */     this.bandOffset = this.dataOffsets[0];
/*     */ 
/* 201 */     verify();
/*     */   }
/*     */ 
/*     */   public int[] getDataOffsets()
/*     */   {
/* 210 */     return (int[])this.dataOffsets.clone();
/*     */   }
/*     */ 
/*     */   public int getDataOffset(int paramInt)
/*     */   {
/* 220 */     return this.dataOffsets[paramInt];
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 228 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelStride()
/*     */   {
/* 236 */     return this.pixelStride;
/*     */   }
/*     */ 
/*     */   public short[] getDataStorage()
/*     */   {
/* 243 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 263 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 265 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort;
/* 269 */     if (paramObject == null)
/* 270 */       arrayOfShort = new short[this.numDataElements];
/*     */     else {
/* 272 */       arrayOfShort = (short[])paramObject;
/*     */     }
/* 274 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 277 */     for (int j = 0; j < this.numDataElements; j++) {
/* 278 */       arrayOfShort[j] = this.data[(this.dataOffsets[j] + i)];
/*     */     }
/*     */ 
/* 281 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 311 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 313 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort;
/* 317 */     if (paramObject == null)
/* 318 */       arrayOfShort = new short[paramInt3 * paramInt4 * this.numDataElements];
/*     */     else {
/* 320 */       arrayOfShort = (short[])paramObject;
/*     */     }
/* 322 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 326 */     int k = 0;
/*     */ 
/* 330 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 331 */       int j = i;
/* 332 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 333 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 334 */           arrayOfShort[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 332 */         m++;
/*     */       }
/* 330 */       n++;
/*     */     }
/*     */ 
/* 339 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 364 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 366 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 369 */     if (paramArrayOfShort == null) {
/* 370 */       paramArrayOfShort = new short[this.numDataElements * paramInt3 * paramInt4];
/*     */     }
/* 372 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 375 */     int k = 0;
/*     */     int n;
/* 379 */     if (this.pixelStride == 1) {
/* 380 */       if (this.scanlineStride == paramInt3) {
/* 381 */         System.arraycopy(this.data, i, paramArrayOfShort, 0, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 384 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 385 */           System.arraycopy(this.data, i, paramArrayOfShort, k, paramInt3);
/* 386 */           k += paramInt3;
/*     */ 
/* 384 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 391 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 392 */         int j = i;
/* 393 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 394 */           paramArrayOfShort[(k++)] = this.data[j];
/*     */ 
/* 393 */           m++;
/*     */         }
/* 391 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 399 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 424 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 426 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 429 */     if (paramArrayOfShort == null) {
/* 430 */       paramArrayOfShort = new short[this.numDataElements * paramInt3 * paramInt4];
/*     */     }
/* 432 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 435 */     int k = 0;
/*     */ 
/* 439 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 440 */       int j = i;
/* 441 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 442 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 443 */           paramArrayOfShort[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 441 */         m++;
/*     */       }
/* 439 */       n++;
/*     */     }
/*     */ 
/* 448 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 464 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 466 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 469 */     short[] arrayOfShort = (short[])paramObject;
/* 470 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 472 */     for (int j = 0; j < this.numDataElements; j++) {
/* 473 */       this.data[(this.dataOffsets[j] + i)] = arrayOfShort[j];
/*     */     }
/*     */ 
/* 476 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*     */   {
/* 488 */     int i = paramInt1 + paramRaster.getMinX();
/* 489 */     int j = paramInt2 + paramRaster.getMinY();
/* 490 */     int k = paramRaster.getWidth();
/* 491 */     int m = paramRaster.getHeight();
/* 492 */     if ((i < this.minX) || (j < this.minY) || (i + k > this.maxX) || (j + m > this.maxY))
/*     */     {
/* 494 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 498 */     setDataElements(i, j, k, m, paramRaster);
/*     */   }
/*     */ 
/*     */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Raster paramRaster)
/*     */   {
/* 517 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 518 */       return;
/*     */     }
/*     */ 
/* 523 */     int i = paramRaster.getMinX();
/* 524 */     int j = paramRaster.getMinY();
/* 525 */     Object localObject = null;
/*     */ 
/* 531 */     for (int k = 0; k < paramInt4; k++)
/*     */     {
/* 533 */       localObject = paramRaster.getDataElements(i, j + k, paramInt3, 1, localObject);
/*     */ 
/* 535 */       setDataElements(paramInt1, paramInt2 + k, paramInt3, 1, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 562 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 564 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 567 */     short[] arrayOfShort = (short[])paramObject;
/* 568 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 571 */     int k = 0;
/*     */ 
/* 575 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 576 */       int j = i;
/* 577 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 578 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 579 */           this.data[(this.dataOffsets[i1] + j)] = arrayOfShort[(k++)];
/* 577 */         m++;
/*     */       }
/* 575 */       n++;
/*     */     }
/*     */ 
/* 584 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 608 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 610 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 613 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 616 */     int k = 0;
/*     */     int n;
/* 620 */     if (this.pixelStride == 1) {
/* 621 */       if (this.scanlineStride == paramInt3) {
/* 622 */         System.arraycopy(paramArrayOfShort, 0, this.data, i, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 625 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 626 */           System.arraycopy(paramArrayOfShort, k, this.data, i, paramInt3);
/* 627 */           k += paramInt3;
/*     */ 
/* 625 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 632 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 633 */         int j = i;
/* 634 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 635 */           this.data[j] = paramArrayOfShort[(k++)];
/*     */ 
/* 634 */           m++;
/*     */         }
/* 632 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 640 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 661 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 663 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 666 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 669 */     int k = 0;
/*     */ 
/* 673 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 674 */       int j = i;
/* 675 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 676 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 677 */           this.data[(this.dataOffsets[i1] + j)] = paramArrayOfShort[(k++)];
/* 675 */         m++;
/*     */       }
/* 673 */       n++;
/*     */     }
/*     */ 
/* 682 */     markDirty();
/*     */   }
/*     */ 
/*     */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 707 */     WritableRaster localWritableRaster = createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*     */ 
/* 711 */     return localWritableRaster;
/*     */   }
/*     */ 
/*     */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 737 */     if (paramInt1 < this.minX) {
/* 738 */       throw new RasterFormatException("x lies outside the raster");
/*     */     }
/* 740 */     if (paramInt2 < this.minY) {
/* 741 */       throw new RasterFormatException("y lies outside the raster");
/*     */     }
/* 743 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 744 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*     */     }
/* 746 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 747 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*     */     SampleModel localSampleModel;
/* 752 */     if (paramArrayOfInt != null)
/* 753 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*     */     else {
/* 755 */       localSampleModel = this.sampleModel;
/*     */     }
/* 757 */     int i = paramInt5 - paramInt1;
/* 758 */     int j = paramInt6 - paramInt2;
/*     */ 
/* 760 */     return new ShortComponentRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*     */   {
/* 773 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 774 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*     */     }
/*     */ 
/* 778 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*     */ 
/* 780 */     return new ShortComponentRaster(localSampleModel, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster()
/*     */   {
/* 790 */     return createCompatibleWritableRaster(this.width, this.height);
/*     */   }
/*     */ 
/*     */   protected final void verify()
/*     */   {
/* 808 */     if ((this.width <= 0) || (this.height <= 0) || (this.height > 2147483647 / this.width))
/*     */     {
/* 811 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/*     */ 
/* 814 */     for (int i = 0; i < this.dataOffsets.length; i++) {
/* 815 */       if (this.dataOffsets[i] < 0) {
/* 816 */         throw new RasterFormatException("Data offsets for band " + i + "(" + this.dataOffsets[i] + ") must be >= 0");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 822 */     if ((this.minX - this.sampleModelTranslateX < 0L) || (this.minY - this.sampleModelTranslateY < 0L))
/*     */     {
/* 825 */       throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
/*     */     }
/*     */ 
/* 831 */     if ((this.scanlineStride < 0) || (this.scanlineStride > 2147483647 / this.height))
/*     */     {
/* 835 */       throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */     }
/*     */ 
/* 839 */     if ((this.height > 1) || (this.minY - this.sampleModelTranslateY > 0))
/*     */     {
/* 841 */       if (this.scanlineStride > this.data.length) {
/* 842 */         throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 847 */     i = (this.height - 1) * this.scanlineStride;
/*     */ 
/* 849 */     if ((this.pixelStride < 0) || (this.pixelStride > 2147483647 / this.width) || (this.pixelStride > this.data.length))
/*     */     {
/* 854 */       throw new RasterFormatException("Incorrect pixel stride: " + this.pixelStride);
/*     */     }
/*     */ 
/* 857 */     int j = (this.width - 1) * this.pixelStride;
/*     */ 
/* 859 */     if (j > 2147483647 - i)
/*     */     {
/* 861 */       throw new RasterFormatException("Incorrect raster attributes");
/*     */     }
/* 863 */     j += i;
/*     */ 
/* 866 */     int m = 0;
/* 867 */     for (int n = 0; n < this.numDataElements; n++) {
/* 868 */       if (this.dataOffsets[n] > 2147483647 - j) {
/* 869 */         throw new RasterFormatException("Incorrect band offset: " + this.dataOffsets[n]);
/*     */       }
/*     */ 
/* 873 */       int k = j + this.dataOffsets[n];
/*     */ 
/* 875 */       if (k > m) {
/* 876 */         m = k;
/*     */       }
/*     */     }
/* 879 */     if (this.data.length <= m)
/* 880 */       throw new RasterFormatException("Data array too small (should be > " + m + " )");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 886 */     return new String("ShortComponentRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  82 */     NativeLibLoader.loadLibraries();
/*  83 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ShortComponentRaster
 * JD-Core Version:    0.6.2
 */