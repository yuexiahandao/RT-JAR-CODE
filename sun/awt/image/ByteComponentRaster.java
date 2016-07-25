/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RasterFormatException;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ 
/*     */ public class ByteComponentRaster extends SunWritableRaster
/*     */ {
/*     */   protected int bandOffset;
/*     */   protected int[] dataOffsets;
/*     */   protected int scanlineStride;
/*     */   protected int pixelStride;
/*     */   protected byte[] data;
/*     */   int type;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public ByteComponentRaster(SampleModel paramSampleModel, Point paramPoint)
/*     */   {
/*  96 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ByteComponentRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*     */   {
/* 120 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*     */   }
/*     */ 
/*     */   public ByteComponentRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ByteComponentRaster paramByteComponentRaster)
/*     */   {
/* 154 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramByteComponentRaster);
/* 155 */     this.maxX = (this.minX + this.width);
/* 156 */     this.maxY = (this.minY + this.height);
/*     */ 
/* 158 */     if (!(paramDataBuffer instanceof DataBufferByte)) {
/* 159 */       throw new RasterFormatException("ByteComponentRasters must have byte DataBuffers");
/*     */     }
/*     */ 
/* 163 */     DataBufferByte localDataBufferByte = (DataBufferByte)paramDataBuffer;
/* 164 */     this.data = stealData(localDataBufferByte, 0);
/* 165 */     if (localDataBufferByte.getNumBanks() != 1) {
/* 166 */       throw new RasterFormatException("DataBuffer for ByteComponentRasters must only have 1 bank.");
/*     */     }
/*     */ 
/* 170 */     int i = localDataBufferByte.getOffset();
/*     */     Object localObject;
/*     */     int j;
/*     */     int k;
/* 172 */     if ((paramSampleModel instanceof ComponentSampleModel)) {
/* 173 */       localObject = (ComponentSampleModel)paramSampleModel;
/* 174 */       this.type = 1;
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
/* 187 */       this.type = 7;
/* 188 */       this.scanlineStride = ((SinglePixelPackedSampleModel)localObject).getScanlineStride();
/* 189 */       this.pixelStride = 1;
/* 190 */       this.dataOffsets = new int[1];
/* 191 */       this.dataOffsets[0] = i;
/* 192 */       j = paramRectangle.x - paramPoint.x;
/* 193 */       k = paramRectangle.y - paramPoint.y;
/* 194 */       this.dataOffsets[0] += j * this.pixelStride + k * this.scanlineStride;
/*     */     } else {
/* 196 */       throw new RasterFormatException("IntegerComponentRasters must have ComponentSampleModel or SinglePixelPackedSampleModel");
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
/* 229 */     return this.scanlineStride;
/*     */   }
/*     */ 
/*     */   public int getPixelStride()
/*     */   {
/* 237 */     return this.pixelStride;
/*     */   }
/*     */ 
/*     */   public byte[] getDataStorage()
/*     */   {
/* 244 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 264 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 266 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     byte[] arrayOfByte;
/* 270 */     if (paramObject == null)
/* 271 */       arrayOfByte = new byte[this.numDataElements];
/*     */     else {
/* 273 */       arrayOfByte = (byte[])paramObject;
/*     */     }
/* 275 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 278 */     for (int j = 0; j < this.numDataElements; j++) {
/* 279 */       arrayOfByte[j] = this.data[(this.dataOffsets[j] + i)];
/*     */     }
/*     */ 
/* 282 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 312 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 314 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */     byte[] arrayOfByte;
/* 318 */     if (paramObject == null)
/* 319 */       arrayOfByte = new byte[paramInt3 * paramInt4 * this.numDataElements];
/*     */     else {
/* 321 */       arrayOfByte = (byte[])paramObject;
/*     */     }
/*     */ 
/* 324 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 327 */     int k = 0;
/*     */ 
/* 331 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 332 */       int j = i;
/* 333 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 334 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 335 */           arrayOfByte[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 333 */         m++;
/*     */       }
/* 331 */       n++;
/*     */     }
/*     */ 
/* 340 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*     */   {
/* 365 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 367 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 370 */     if (paramArrayOfByte == null) {
/* 371 */       paramArrayOfByte = new byte[this.scanlineStride * paramInt4];
/*     */     }
/* 373 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 376 */     int k = 0;
/*     */     int n;
/* 380 */     if (this.pixelStride == 1) {
/* 381 */       if (this.scanlineStride == paramInt3) {
/* 382 */         System.arraycopy(this.data, i, paramArrayOfByte, 0, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 385 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 386 */           System.arraycopy(this.data, i, paramArrayOfByte, k, paramInt3);
/* 387 */           k += paramInt3;
/*     */ 
/* 385 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 392 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 393 */         int j = i;
/* 394 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 395 */           paramArrayOfByte[(k++)] = this.data[j];
/*     */ 
/* 394 */           m++;
/*     */         }
/* 392 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 400 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 425 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 427 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 430 */     if (paramArrayOfByte == null) {
/* 431 */       paramArrayOfByte = new byte[this.numDataElements * this.scanlineStride * paramInt4];
/*     */     }
/* 433 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 436 */     int k = 0;
/*     */ 
/* 441 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 442 */       int j = i;
/* 443 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 444 */         for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 445 */           paramArrayOfByte[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/* 443 */         m++;
/*     */       }
/* 441 */       n++;
/*     */     }
/*     */ 
/* 450 */     return paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 466 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*     */     {
/* 468 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 471 */     byte[] arrayOfByte = (byte[])paramObject;
/* 472 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 475 */     for (int j = 0; j < this.numDataElements; j++) {
/* 476 */       this.data[(this.dataOffsets[j] + i)] = arrayOfByte[j];
/*     */     }
/*     */ 
/* 479 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*     */   {
/* 491 */     int i = paramRaster.getMinX() + paramInt1;
/* 492 */     int j = paramRaster.getMinY() + paramInt2;
/* 493 */     int k = paramRaster.getWidth();
/* 494 */     int m = paramRaster.getHeight();
/* 495 */     if ((i < this.minX) || (j < this.minY) || (i + k > this.maxX) || (j + m > this.maxY))
/*     */     {
/* 497 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 501 */     setDataElements(i, j, k, m, paramRaster);
/*     */   }
/*     */ 
/*     */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Raster paramRaster)
/*     */   {
/* 520 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 521 */       return;
/*     */     }
/*     */ 
/* 524 */     int i = paramRaster.getMinX();
/* 525 */     int j = paramRaster.getMinY();
/* 526 */     Object localObject = null;
/*     */ 
/* 528 */     if ((paramRaster instanceof ByteComponentRaster)) {
/* 529 */       ByteComponentRaster localByteComponentRaster = (ByteComponentRaster)paramRaster;
/* 530 */       byte[] arrayOfByte = localByteComponentRaster.getDataStorage();
/*     */ 
/* 532 */       if (this.numDataElements == 1) {
/* 533 */         int m = localByteComponentRaster.getDataOffset(0);
/* 534 */         int n = localByteComponentRaster.getScanlineStride();
/*     */ 
/* 536 */         int i1 = m;
/* 537 */         int i2 = this.dataOffsets[0] + (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX);
/*     */ 
/* 541 */         if (this.pixelStride == localByteComponentRaster.getPixelStride()) {
/* 542 */           paramInt3 *= this.pixelStride;
/* 543 */           for (int i3 = 0; i3 < paramInt4; i3++) {
/* 544 */             System.arraycopy(arrayOfByte, i1, this.data, i2, paramInt3);
/*     */ 
/* 546 */             i1 += n;
/* 547 */             i2 += this.scanlineStride;
/*     */           }
/* 549 */           markDirty();
/* 550 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 555 */     for (int k = 0; k < paramInt4; k++)
/*     */     {
/* 557 */       localObject = paramRaster.getDataElements(i, j + k, paramInt3, 1, localObject);
/*     */ 
/* 559 */       setDataElements(paramInt1, paramInt2 + k, paramInt3, 1, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*     */   {
/* 586 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 588 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 591 */     byte[] arrayOfByte = (byte[])paramObject;
/* 592 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 595 */     int k = 0;
/*     */     int i1;
/*     */     int j;
/* 599 */     if (this.numDataElements == 1) {
/* 600 */       i1 = 0;
/* 601 */       int i2 = i + this.dataOffsets[0];
/* 602 */       for (n = 0; n < paramInt4; n++) {
/* 603 */         j = i;
/* 604 */         System.arraycopy(arrayOfByte, i1, this.data, i2, paramInt3);
/*     */ 
/* 607 */         i1 += paramInt3;
/* 608 */         i2 += this.scanlineStride;
/*     */       }
/* 610 */       markDirty();
/* 611 */       return;
/*     */     }
/*     */ 
/* 614 */     for (int n = 0; n < paramInt4; i += this.scanlineStride) {
/* 615 */       j = i;
/* 616 */       for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 617 */         for (i1 = 0; i1 < this.numDataElements; i1++)
/* 618 */           this.data[(this.dataOffsets[i1] + j)] = arrayOfByte[(k++)];
/* 616 */         m++;
/*     */       }
/* 614 */       n++;
/*     */     }
/*     */ 
/* 623 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*     */   {
/* 647 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 649 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 652 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*     */ 
/* 655 */     int k = 0;
/*     */     int n;
/* 659 */     if (this.pixelStride == 1) {
/* 660 */       if (this.scanlineStride == paramInt3) {
/* 661 */         System.arraycopy(paramArrayOfByte, 0, this.data, i, paramInt3 * paramInt4);
/*     */       }
/*     */       else {
/* 664 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 665 */           System.arraycopy(paramArrayOfByte, k, this.data, i, paramInt3);
/* 666 */           k += paramInt3;
/*     */ 
/* 664 */           n++;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 671 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 672 */         int j = i;
/* 673 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/* 674 */           this.data[j] = paramArrayOfByte[(k++)];
/*     */ 
/* 673 */           m++;
/*     */         }
/* 671 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 679 */     markDirty();
/*     */   }
/*     */ 
/*     */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 700 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*     */     {
/* 702 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*     */     }
/*     */ 
/* 705 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*     */ 
/* 709 */     int k = 0;
/*     */     int n;
/*     */     int j;
/*     */     int m;
/* 713 */     if (this.numDataElements == 1) {
/* 714 */       i += this.dataOffsets[0];
/* 715 */       if (this.pixelStride == 1) {
/* 716 */         if (this.scanlineStride == paramInt3) {
/* 717 */           System.arraycopy(paramArrayOfByte, 0, this.data, i, paramInt3 * paramInt4);
/*     */         }
/*     */         else {
/* 720 */           for (n = 0; n < paramInt4; n++) {
/* 721 */             System.arraycopy(paramArrayOfByte, k, this.data, i, paramInt3);
/* 722 */             k += paramInt3;
/* 723 */             i += this.scanlineStride;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 728 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 729 */           j = i;
/* 730 */           for (m = 0; m < paramInt3; j += this.pixelStride) {
/* 731 */             this.data[j] = paramArrayOfByte[(k++)];
/*     */ 
/* 730 */             m++;
/*     */           }
/* 728 */           n++;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 737 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/* 738 */         j = i;
/* 739 */         for (m = 0; m < paramInt3; j += this.pixelStride) {
/* 740 */           for (int i1 = 0; i1 < this.numDataElements; i1++)
/* 741 */             this.data[(this.dataOffsets[i1] + j)] = paramArrayOfByte[(k++)];
/* 739 */           m++;
/*     */         }
/* 737 */         n++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 747 */     markDirty();
/*     */   }
/*     */ 
/*     */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 772 */     WritableRaster localWritableRaster = createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*     */ 
/* 776 */     return localWritableRaster;
/*     */   }
/*     */ 
/*     */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*     */   {
/* 802 */     if (paramInt1 < this.minX) {
/* 803 */       throw new RasterFormatException("x lies outside the raster");
/*     */     }
/* 805 */     if (paramInt2 < this.minY) {
/* 806 */       throw new RasterFormatException("y lies outside the raster");
/*     */     }
/* 808 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 809 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*     */     }
/* 811 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 812 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*     */     SampleModel localSampleModel;
/* 817 */     if (paramArrayOfInt != null)
/* 818 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*     */     else {
/* 820 */       localSampleModel = this.sampleModel;
/*     */     }
/* 822 */     int i = paramInt5 - paramInt1;
/* 823 */     int j = paramInt6 - paramInt2;
/*     */ 
/* 825 */     return new ByteComponentRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*     */   {
/* 838 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 839 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*     */     }
/*     */ 
/* 843 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*     */ 
/* 845 */     return new ByteComponentRaster(localSampleModel, new Point(0, 0));
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleWritableRaster()
/*     */   {
/* 856 */     return createCompatibleWritableRaster(this.width, this.height);
/*     */   }
/*     */ 
/*     */   protected final void verify()
/*     */   {
/* 874 */     if ((this.width <= 0) || (this.height <= 0) || (this.height > 2147483647 / this.width))
/*     */     {
/* 877 */       throw new RasterFormatException("Invalid raster dimension");
/*     */     }
/*     */ 
/* 880 */     for (int i = 0; i < this.dataOffsets.length; i++) {
/* 881 */       if (this.dataOffsets[i] < 0) {
/* 882 */         throw new RasterFormatException("Data offsets for band " + i + "(" + this.dataOffsets[i] + ") must be >= 0");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 888 */     if ((this.minX - this.sampleModelTranslateX < 0L) || (this.minY - this.sampleModelTranslateY < 0L))
/*     */     {
/* 891 */       throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
/*     */     }
/*     */ 
/* 897 */     if ((this.scanlineStride < 0) || (this.scanlineStride > 2147483647 / this.height))
/*     */     {
/* 901 */       throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */     }
/*     */ 
/* 905 */     if ((this.height > 1) || (this.minY - this.sampleModelTranslateY > 0))
/*     */     {
/* 907 */       if (this.scanlineStride > this.data.length) {
/* 908 */         throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 913 */     i = (this.height - 1) * this.scanlineStride;
/*     */ 
/* 915 */     if ((this.pixelStride < 0) || (this.pixelStride > 2147483647 / this.width) || (this.pixelStride > this.data.length))
/*     */     {
/* 920 */       throw new RasterFormatException("Incorrect pixel stride: " + this.pixelStride);
/*     */     }
/*     */ 
/* 923 */     int j = (this.width - 1) * this.pixelStride;
/*     */ 
/* 925 */     if (j > 2147483647 - i)
/*     */     {
/* 927 */       throw new RasterFormatException("Incorrect raster attributes");
/*     */     }
/* 929 */     j += i;
/*     */ 
/* 932 */     int m = 0;
/* 933 */     for (int n = 0; n < this.numDataElements; n++) {
/* 934 */       if (this.dataOffsets[n] > 2147483647 - j) {
/* 935 */         throw new RasterFormatException("Incorrect band offset: " + this.dataOffsets[n]);
/*     */       }
/*     */ 
/* 940 */       int k = j + this.dataOffsets[n];
/*     */ 
/* 942 */       if (k > m) {
/* 943 */         m = k;
/*     */       }
/*     */     }
/* 946 */     if (this.data.length <= m)
/* 947 */       throw new RasterFormatException("Data array too small (should be > " + m + " )");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 953 */     return new String("ByteComponentRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements + " dataOff[0] = " + this.dataOffsets[0]);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  82 */     NativeLibLoader.loadLibraries();
/*  83 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ByteComponentRaster
 * JD-Core Version:    0.6.2
 */