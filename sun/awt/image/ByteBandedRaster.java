/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BandedSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RasterFormatException;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ByteBandedRaster extends SunWritableRaster
/*     */ {
/*     */   int[] dataOffsets;
/*     */   int scanlineStride;
/*     */   byte[][] data;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */ 
/*     */   public ByteBandedRaster(SampleModel paramSampleModel, Point paramPoint)
/*     */   {
/*  79 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ByteBandedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*     */   {
/* 102 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ByteBandedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ByteBandedRaster paramByteBandedRaster)
/*     */   {
/* 133 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramByteBandedRaster);
/* 134 */     this.maxX = (this.minX + this.width);
/* 135 */     this.maxY = (this.minY + this.height);
/*     */ 
/* 137 */     if (!(paramDataBuffer instanceof DataBufferByte)) {
/* 138 */       throw new RasterFormatException("ByteBandedRaster must havebyte DataBuffers");
/*     */     }
/*     */ 
/* 141 */     DataBufferByte localDataBufferByte = (DataBufferByte)paramDataBuffer;
/*     */ 
/* 143 */     if ((paramSampleModel instanceof BandedSampleModel)) {
/* 144 */       BandedSampleModel localBandedSampleModel = (BandedSampleModel)paramSampleModel;
/* 145 */       this.scanlineStride = localBandedSampleModel.getScanlineStride();
/* 146 */       int[] arrayOfInt1 = localBandedSampleModel.getBankIndices();
/* 147 */       int[] arrayOfInt2 = localBandedSampleModel.getBandOffsets();
/* 148 */       int[] arrayOfInt3 = localDataBufferByte.getOffsets();
/* 149 */       this.dataOffsets = new int[arrayOfInt1.length];
/* 150 */       this.data = new byte[arrayOfInt1.length][];
/* 151 */       int i = paramRectangle.x - paramPoint.x;
/* 152 */       int j = paramRectangle.y - paramPoint.y;
/* 153 */       for (int k = 0; k < arrayOfInt1.length; k++) {
/* 154 */         this.data[k] = stealData(localDataBufferByte, arrayOfInt1[k]);
/* 155 */         this.dataOffsets[k] = (arrayOfInt3[arrayOfInt1[k]] + i + j * this.scanlineStride + arrayOfInt2[k]);
/*     */       }
/*     */     }
/*     */     else {
/* 159 */       throw new RasterFormatException("ByteBandedRasters must haveBandedSampleModels");
/*     */     }
/*     */ 
/* 162 */     verify();
/*     */   }
/*     */ 
/*     */   public int[] getDataOffsets()
/*     */   {
/* 172 */     return (int[])this.dataOffsets.clone();
/*     */   }
/*     */ 
/*     */   public int getDataOffset(int paramInt)
/*     */   {
/* 182 */     return this.dataOffsets[paramInt];
/*     */   }
/*     */ 
/*     */   public int getScanlineStride()
/*     */   {
/* 191 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelStride()
/*     */   {
/* 199 */     return 1;
/*     */   }
/*     */ 
/*     */   public byte[][] getDataStorage()
/*     */   {
/* 206 */     return this.data;
/*     */   }
/*     */ 
/*     */   public byte[] getDataStorage(int paramInt)
/*     */   {
/* 213 */     return this.data[paramInt];
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 233 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 235 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     byte[] arrayOfByte;
/* 239 */     if (paramObject == null)
/* 240 */       arrayOfByte = new byte[this.numDataElements];
/*     */     else {
/* 242 */       arrayOfByte = (byte[])paramObject;
/*     */     }
/* 244 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 246 */     for (int j = 0; j < this.numDataElements; j++) {
/* 247 */       arrayOfByte[j] = this.data[j][(this.dataOffsets[j] + i)];
/*     */     }
/*     */ 
/* 250 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 280 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 282 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     byte[] arrayOfByte1;
/* 286 */     if (paramObject == null)
/* 287 */       arrayOfByte1 = new byte[this.numDataElements * paramInt3 * paramInt4];
/*     */     else {
/* 289 */       arrayOfByte1 = (byte[])paramObject;
/*     */     }
/* 291 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 293 */     for (int j = 0; j < this.numDataElements; j++) {
/* 294 */       int k = j;
/* 295 */       byte[] arrayOfByte2 = this.data[j];
/* 296 */       int m = this.dataOffsets[j];
/*     */ 
/* 298 */       int n = i;
/* 299 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 300 */         int i2 = m + n;
/* 301 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 302 */           arrayOfByte1[k] = arrayOfByte2[(i2++)];
/* 303 */           k += this.numDataElements;
/*     */         }
/* 299 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 308 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*     */   {
/* 333 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 335 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 338 */     if (paramArrayOfByte == null) {
/* 339 */       paramArrayOfByte = new byte[this.scanlineStride * paramInt4];
/*     */     }
/* 341 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) + this.dataOffsets[paramInt5];
/*     */ 
/* 343 */     if (this.scanlineStride == paramInt3) {
/* 344 */       System.arraycopy(this.data[paramInt5], i, paramArrayOfByte, 0, paramInt3 * paramInt4);
/*     */     } else {
/* 346 */       int j = 0;
/* 347 */       for (int k = 0; k < paramInt4; i += this.scanlineStride) {
/* 348 */         System.arraycopy(this.data[paramInt5], i, paramArrayOfByte, j, paramInt3);
/* 349 */         j += paramInt3;
/*     */ 
/* 347 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 353 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 378 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 380 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 383 */     if (paramArrayOfByte == null) {
/* 384 */       paramArrayOfByte = new byte[this.numDataElements * this.scanlineStride * paramInt4];
/*     */     }
/* 386 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 388 */     for (int j = 0; j < this.numDataElements; j++) {
/* 389 */       int k = j;
/* 390 */       byte[] arrayOfByte = this.data[j];
/* 391 */       int m = this.dataOffsets[j];
/*     */ 
/* 394 */       int n = i;
/* 395 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 396 */         int i2 = m + n;
/* 397 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 398 */           paramArrayOfByte[k] = arrayOfByte[(i2++)];
/* 399 */           k += this.numDataElements;
/*     */         }
/* 395 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 404 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 420 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 422 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 425 */     byte[] arrayOfByte = (byte[])paramObject;
/* 426 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/* 427 */     for (int j = 0; j < this.numDataElements; j++) {
/* 428 */       this.data[j][(this.dataOffsets[j] + i)] = arrayOfByte[j];
/*     */     }
/* 430 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*     */   {
/* 442 */     int i = paramRaster.getMinX() + paramInt1;
/* 443 */     int j = paramRaster.getMinY() + paramInt2;
/* 444 */     int k = paramRaster.getWidth();
/* 445 */     int m = paramRaster.getHeight();
/* 446 */     if ((i < this.minX) || (j < this.minY) || (i + k > this.maxX) || (j + m > this.maxY))
/*     */     {
/* 448 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 452 */     setDataElements(i, j, k, m, paramRaster);
/*     */   }
/*     */ 
/*     */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Raster paramRaster)
/*     */   {
/* 471 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 472 */       return;
/*     */     }
/*     */ 
/* 475 */     int i = paramRaster.getMinX();
/* 476 */     int j = paramRaster.getMinY();
/* 477 */     Object localObject = null;
/*     */ 
/* 483 */     for (int k = 0; k < paramInt4; k++)
/*     */     {
/* 485 */       localObject = paramRaster.getDataElements(i, j + k, paramInt3, 1, localObject);
/*     */ 
/* 487 */       setDataElements(paramInt1, paramInt2 + k, paramInt3, 1, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 514 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 516 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 519 */     byte[] arrayOfByte1 = (byte[])paramObject;
/* 520 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 522 */     for (int j = 0; j < this.numDataElements; j++) {
/* 523 */       int k = j;
/* 524 */       byte[] arrayOfByte2 = this.data[j];
/* 525 */       int m = this.dataOffsets[j];
/*     */ 
/* 527 */       int n = i;
/* 528 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 529 */         int i2 = m + n;
/* 530 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 531 */           arrayOfByte2[(i2++)] = arrayOfByte1[k];
/* 532 */           k += this.numDataElements;
/*     */         }
/* 528 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 537 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*     */   {
/* 561 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 563 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 566 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) + this.dataOffsets[paramInt5];
/*     */ 
/* 568 */     int j = 0;
/*     */ 
/* 572 */     if (this.scanlineStride == paramInt3)
/* 573 */       System.arraycopy(paramArrayOfByte, 0, this.data[paramInt5], i, paramInt3 * paramInt4);
/*     */     else {
/* 575 */       for (int k = 0; k < paramInt4; i += this.scanlineStride) {
/* 576 */         System.arraycopy(paramArrayOfByte, j, this.data[paramInt5], i, paramInt3);
/* 577 */         j += paramInt3;
/*     */ 
/* 575 */         k++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 581 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 602 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 604 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 607 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 609 */     for (int j = 0; j < this.numDataElements; j++) {
/* 610 */       int k = j;
/* 611 */       byte[] arrayOfByte = this.data[j];
/* 612 */       int m = this.dataOffsets[j];
/*     */ 
/* 614 */       int n = i;
/* 615 */       for (int i1 = 0; i1 < paramInt4; n += this.scanlineStride) {
/* 616 */         int i2 = m + n;
/* 617 */         for (int i3 = 0; i3 < paramInt3; i3++) {
/* 618 */           arrayOfByte[(i2++)] = paramArrayOfByte[k];
/* 619 */           k += this.numDataElements;
/*     */         }
/* 615 */         i1++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 624 */     markDirty();
/*     */   }
/*     */ 
/*     */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 651 */     if (paramInt1 < this.minX) {
/* 652 */       throw new RasterFormatException("x lies outside raster");
/*     */     }
/* 654 */     if (paramInt2 < this.minY) {
/* 655 */       throw new RasterFormatException("y lies outside raster");
/*     */     }
/* 657 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.width + this.minX)) {
/* 658 */       throw new RasterFormatException("(x + width) is outside raster");
/*     */     }
/* 660 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.height + this.minY))
/* 661 */       throw new RasterFormatException("(y + height) is outside raster");
/*     */     SampleModel localSampleModel;
/* 666 */     if (paramArrayOfInt != null)
/* 667 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*     */     else {
/* 669 */       localSampleModel = this.sampleModel;
/*     */     }
/* 671 */     int i = paramInt5 - paramInt1;
/* 672 */     int j = paramInt6 - paramInt2;
/*     */ 
/* 674 */     return new ByteBandedRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*     */   }
/*     */ 
/*     */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 705 */     return createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*     */   {
/* 713 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 714 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*     */     }
/*     */ 
/* 718 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*     */ 
/* 720 */     return new ByteBandedRaster(localSampleModel, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster()
/*     */   {
/* 730 */     return createCompatibleWritableRaster(this.width, this.height);
/*     */   }
/*     */ 
/*     */   private void verify()
/*     */   {
/* 744 */     if ((this.width <= 0) || (this.height <= 0) || (this.height > 2147483647 / this.width))
/*     */     {
/* 747 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/*     */ 
/* 750 */     if ((this.scanlineStride < 0) || (this.scanlineStride > 2147483647 / this.height))
/*     */     {
/* 754 */       throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */     }
/*     */ 
/* 758 */     if ((this.minX - this.sampleModelTranslateX < 0L) || (this.minY - this.sampleModelTranslateY < 0L))
/*     */     {
/* 761 */       throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
/*     */     }
/*     */ 
/* 767 */     if ((this.height > 1) || (this.minY - this.sampleModelTranslateY > 0))
/*     */     {
/* 769 */       for (i = 0; i < this.data.length; i++) {
/* 770 */         if (this.scanlineStride > this.data[i].length) {
/* 771 */           throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 778 */     for (int i = 0; i < this.dataOffsets.length; i++) {
/* 779 */       if (this.dataOffsets[i] < 0) {
/* 780 */         throw new RasterFormatException("Data offsets for band " + i + "(" + this.dataOffsets[i] + ") must be >= 0");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 786 */     i = (this.height - 1) * this.scanlineStride;
/*     */ 
/* 788 */     if (this.width - 1 > 2147483647 - i) {
/* 789 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/* 791 */     int j = i + (this.width - 1);
/*     */ 
/* 793 */     int k = 0;
/*     */ 
/* 796 */     for (int n = 0; n < this.numDataElements; n++) {
/* 797 */       if (this.dataOffsets[n] > 2147483647 - j) {
/* 798 */         throw new RasterFormatException("Invalid raster dimension");
/*     */       }
/* 800 */       int m = j + this.dataOffsets[n];
/* 801 */       if (m > k) {
/* 802 */         k = m;
/*     */       }
/*     */     }
/*     */ 
/* 806 */     if (this.data.length == 1) {
/* 807 */       if (this.data[0].length <= k * this.numDataElements) {
/* 808 */         throw new RasterFormatException("Data array too small (it is " + this.data[0].length + " and should be > " + k * this.numDataElements + " )");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 816 */       for (n = 0; n < this.numDataElements; n++)
/* 817 */         if (this.data[n].length <= k)
/* 818 */           throw new RasterFormatException("Data array too small (it is " + this.data[n].length + " and should be > " + k + " )");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 828 */     return new String("ByteBandedRaster: width = " + this.width + " height = " + this.height + " #bands " + this.numDataElements + " minX = " + this.minX + " minY = " + this.minY);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ByteBandedRaster
 * JD-Core Version:    0.6.2
 */