/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferUShort;
/*     */ import java.awt.image.PixelInterleavedSampleModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RasterFormatException;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ShortInterleavedRaster extends ShortComponentRaster
/*     */ {
/*     */   private int maxX;
/*     */   private int maxY;
/*     */ 
/*     */   public ShortInterleavedRaster(SampleModel paramSampleModel, Point paramPoint)
/*     */   {
/*  73 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortInterleavedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*     */   {
/*  97 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ShortInterleavedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ShortInterleavedRaster paramShortInterleavedRaster)
/*     */   {
/* 131 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramShortInterleavedRaster);
/* 132 */     this.maxX = (this.minX + this.width);
/* 133 */     this.maxY = (this.minY + this.height);
/*     */ 
/* 135 */     if (!(paramDataBuffer instanceof DataBufferUShort)) {
/* 136 */       throw new RasterFormatException("ShortInterleavedRasters must have ushort DataBuffers");
/*     */     }
/*     */ 
/* 140 */     DataBufferUShort localDataBufferUShort = (DataBufferUShort)paramDataBuffer;
/* 141 */     this.data = stealData(localDataBufferUShort, 0);
/*     */     Object localObject;
/*     */     int i;
/*     */     int j;
/* 144 */     if (((paramSampleModel instanceof PixelInterleavedSampleModel)) || (((paramSampleModel instanceof ComponentSampleModel)) && (paramSampleModel.getNumBands() == 1)))
/*     */     {
/* 147 */       localObject = (ComponentSampleModel)paramSampleModel;
/*     */ 
/* 149 */       this.scanlineStride = ((ComponentSampleModel)localObject).getScanlineStride();
/* 150 */       this.pixelStride = ((ComponentSampleModel)localObject).getPixelStride();
/* 151 */       this.dataOffsets = ((ComponentSampleModel)localObject).getBandOffsets();
/* 152 */       i = paramRectangle.x - paramPoint.x;
/* 153 */       j = paramRectangle.y - paramPoint.y;
/* 154 */       for (int k = 0; k < getNumDataElements(); k++)
/* 155 */         this.dataOffsets[k] += i * this.pixelStride + j * this.scanlineStride;
/*     */     }
/* 157 */     else if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 158 */       localObject = (SinglePixelPackedSampleModel)paramSampleModel;
/*     */ 
/* 160 */       this.scanlineStride = ((SinglePixelPackedSampleModel)localObject).getScanlineStride();
/* 161 */       this.pixelStride = 1;
/* 162 */       this.dataOffsets = new int[1];
/* 163 */       this.dataOffsets[0] = localDataBufferUShort.getOffset();
/* 164 */       i = paramRectangle.x - paramPoint.x;
/* 165 */       j = paramRectangle.y - paramPoint.y;
/* 166 */       this.dataOffsets[0] += i + j * this.scanlineStride;
/*     */     } else {
/* 168 */       throw new RasterFormatException("ShortInterleavedRasters must have PixelInterleavedSampleModel, SinglePixelPackedSampleModel or 1 band ComponentSampleModel.  Sample model is " + paramSampleModel);
/*     */     }
/*     */ 
/* 173 */     this.bandOffset = this.dataOffsets[0];
/* 174 */     verify();
/*     */   }
/*     */ 
/*     */   public int[] getDataOffsets()
/*     */   {
/* 183 */     return (int[])this.dataOffsets.clone();
/*     */   }
/*     */ 
/*     */   public int getDataOffset(int paramInt)
/*     */   {
/* 193 */     return this.dataOffsets[paramInt];
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 201 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelStride()
/*     */   {
/* 209 */     return this.pixelStride;
/*     */   }
/*     */ 
/*     */   public short[] getDataStorage()
/*     */   {
/* 216 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 236 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 238 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort;
/* 242 */     if (paramObject == null)
/* 243 */       arrayOfShort = new short[this.numDataElements];
/*     */     else {
/* 245 */       arrayOfShort = (short[])paramObject;
/*     */     }
/* 247 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 250 */     for (int j = 0; j < this.numDataElements; j++) {
/* 251 */       arrayOfShort[j] = this.data[(this.dataOffsets[j] + i)];
/*     */     }
/*     */ 
/* 254 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 284 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 286 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     short[] arrayOfShort;
/* 290 */     if (paramObject == null)
/* 291 */       arrayOfShort = new short[paramInt3 * paramInt4 * this.numDataElements];
/*     */     else {
/* 293 */       arrayOfShort = (short[])paramObject;
/*     */     }
/* 295 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 299 */     int k = 0;
/*     */ 
/* 303 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 304 */       int j = i;
/* 305 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 306 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 307 */           arrayOfShort[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 305 */         m++;
/*     */       }
/* 303 */       n++;
/*     */     }
/*     */ 
/* 312 */     return arrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 337 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 339 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 342 */     if (paramArrayOfShort == null) {
/* 343 */       paramArrayOfShort = new short[this.numDataElements * paramInt3 * paramInt4];
/*     */     }
/* 345 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 348 */     int k = 0;
/*     */     int n;
/* 352 */     if (this.pixelStride == 1) {
/* 353 */       if (this.scanlineStride == paramInt3) {
/* 354 */         System.arraycopy(this.data, i, paramArrayOfShort, 0, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 357 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 358 */           System.arraycopy(this.data, i, paramArrayOfShort, k, paramInt3);
/* 359 */           k += paramInt3;
/*     */ 
/* 357 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 364 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 365 */         int j = i;
/* 366 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 367 */           paramArrayOfShort[(k++)] = this.data[j];
/*     */ 
/* 366 */           m++;
/*     */         }
/* 364 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 372 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public short[] getShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 397 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 399 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 402 */     if (paramArrayOfShort == null) {
/* 403 */       paramArrayOfShort = new short[this.numDataElements * paramInt3 * paramInt4];
/*     */     }
/* 405 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 408 */     int k = 0;
/*     */ 
/* 412 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 413 */       int j = i;
/* 414 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 415 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 416 */           paramArrayOfShort[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 414 */         m++;
/*     */       }
/* 412 */       n++;
/*     */     }
/*     */ 
/* 421 */     return paramArrayOfShort;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 437 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 439 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 442 */     short[] arrayOfShort = (short[])paramObject;
/* 443 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 445 */     for (int j = 0; j < this.numDataElements; j++) {
/* 446 */       this.data[(this.dataOffsets[j] + i)] = arrayOfShort[j];
/*     */     }
/* 448 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*     */   {
/* 460 */     int i = paramInt1 + paramRaster.getMinX();
/* 461 */     int j = paramInt2 + paramRaster.getMinY();
/* 462 */     int k = paramRaster.getWidth();
/* 463 */     int m = paramRaster.getHeight();
/* 464 */     if ((i < this.minX) || (j < this.minY) || (i + k > this.maxX) || (j + m > this.maxY))
/*     */     {
/* 466 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 470 */     setDataElements(i, j, k, m, paramRaster);
/*     */   }
/*     */ 
/*     */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Raster paramRaster)
/*     */   {
/* 489 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 490 */       return;
/*     */     }
/*     */ 
/* 495 */     int i = paramRaster.getMinX();
/* 496 */     int j = paramRaster.getMinY();
/* 497 */     Object localObject = null;
/*     */ 
/* 503 */     for (int k = 0; k < paramInt4; k++)
/*     */     {
/* 505 */       localObject = paramRaster.getDataElements(i, j + k, paramInt3, 1, localObject);
/*     */ 
/* 507 */       setDataElements(paramInt1, paramInt2 + k, paramInt3, 1, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 534 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 536 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 539 */     short[] arrayOfShort = (short[])paramObject;
/* 540 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 543 */     int k = 0;
/*     */ 
/* 547 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 548 */       int j = i;
/* 549 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 550 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 551 */           this.data[(this.dataOffsets[i1] + j)] = arrayOfShort[(k++)];
/* 549 */         m++;
/*     */       }
/* 547 */       n++;
/*     */     }
/*     */ 
/* 556 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, short[] paramArrayOfShort)
/*     */   {
/* 580 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 582 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 585 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 588 */     int k = 0;
/*     */     int n;
/* 592 */     if (this.pixelStride == 1) {
/* 593 */       if (this.scanlineStride == paramInt3) {
/* 594 */         System.arraycopy(paramArrayOfShort, 0, this.data, i, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 597 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 598 */           System.arraycopy(paramArrayOfShort, k, this.data, i, paramInt3);
/* 599 */           k += paramInt3;
/*     */ 
/* 597 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 604 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 605 */         int j = i;
/* 606 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 607 */           this.data[j] = paramArrayOfShort[(k++)];
/*     */ 
/* 606 */           m++;
/*     */         }
/* 604 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 612 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putShortData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfShort)
/*     */   {
/* 633 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 635 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 638 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 641 */     int k = 0;
/*     */ 
/* 645 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 646 */       int j = i;
/* 647 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 648 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 649 */           this.data[(this.dataOffsets[i1] + j)] = paramArrayOfShort[(k++)];
/* 647 */         m++;
/*     */       }
/* 645 */       n++;
/*     */     }
/*     */ 
/* 654 */     markDirty();
/*     */   }
/*     */ 
/*     */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 679 */     WritableRaster localWritableRaster = createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*     */ 
/* 683 */     return localWritableRaster;
/*     */   }
/*     */ 
/*     */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 709 */     if (paramInt1 < this.minX) {
/* 710 */       throw new RasterFormatException("x lies outside the raster");
/*     */     }
/* 712 */     if (paramInt2 < this.minY) {
/* 713 */       throw new RasterFormatException("y lies outside the raster");
/*     */     }
/* 715 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 716 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*     */     }
/* 718 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 719 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*     */     SampleModel localSampleModel;
/* 724 */     if (paramArrayOfInt != null)
/* 725 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*     */     else {
/* 727 */       localSampleModel = this.sampleModel;
/*     */     }
/* 729 */     int i = paramInt5 - paramInt1;
/* 730 */     int j = paramInt6 - paramInt2;
/*     */ 
/* 732 */     return new ShortInterleavedRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*     */   {
/* 745 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 746 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*     */     }
/*     */ 
/* 750 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*     */ 
/* 752 */     return new ShortInterleavedRaster(localSampleModel, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster()
/*     */   {
/* 762 */     return createCompatibleWritableRaster(this.width, this.height);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 766 */     return new String("ShortInterleavedRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ShortInterleavedRaster
 * JD-Core Version:    0.6.2
 */