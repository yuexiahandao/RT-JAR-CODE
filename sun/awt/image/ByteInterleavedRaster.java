/*      */ package sun.awt.image;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.PixelInterleavedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RasterFormatException;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.SinglePixelPackedSampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ 
/*      */ public class ByteInterleavedRaster extends ByteComponentRaster
/*      */ {
/*      */   boolean inOrder;
/*      */   int dbOffset;
/*      */   int dbOffsetPacked;
/*   65 */   boolean packed = false;
/*      */   int[] bitMasks;
/*      */   int[] bitOffsets;
/*      */   private int maxX;
/*      */   private int maxY;
/*      */ 
/*      */   public ByteInterleavedRaster(SampleModel paramSampleModel, Point paramPoint)
/*      */   {
/*   89 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*      */   }
/*      */ 
/*      */   public ByteInterleavedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*      */   {
/*  113 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*      */   }
/*      */ 
/*      */   private boolean isInterleaved(ComponentSampleModel paramComponentSampleModel)
/*      */   {
/*  134 */     int i = this.sampleModel.getNumBands();
/*  135 */     if (i == 1) {
/*  136 */       return true;
/*      */     }
/*      */ 
/*  140 */     int[] arrayOfInt1 = paramComponentSampleModel.getBankIndices();
/*  141 */     for (int j = 0; j < i; j++) {
/*  142 */       if (arrayOfInt1[j] != 0) {
/*  143 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  148 */     int[] arrayOfInt2 = paramComponentSampleModel.getBandOffsets();
/*  149 */     int k = arrayOfInt2[0];
/*  150 */     int m = k;
/*  151 */     for (int n = 1; n < i; n++) {
/*  152 */       int i1 = arrayOfInt2[n];
/*  153 */       if (i1 < k) {
/*  154 */         k = i1;
/*      */       }
/*  156 */       if (i1 > m) {
/*  157 */         m = i1;
/*      */       }
/*      */     }
/*  160 */     if (m - k >= paramComponentSampleModel.getPixelStride()) {
/*  161 */       return false;
/*      */     }
/*      */ 
/*  164 */     return true;
/*      */   }
/*      */ 
/*      */   public ByteInterleavedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, ByteInterleavedRaster paramByteInterleavedRaster)
/*      */   {
/*  191 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramByteInterleavedRaster);
/*  192 */     this.maxX = (this.minX + this.width);
/*  193 */     this.maxY = (this.minY + this.height);
/*      */ 
/*  195 */     if (!(paramDataBuffer instanceof DataBufferByte)) {
/*  196 */       throw new RasterFormatException("ByteInterleavedRasters must have byte DataBuffers");
/*      */     }
/*      */ 
/*  200 */     DataBufferByte localDataBufferByte = (DataBufferByte)paramDataBuffer;
/*  201 */     this.data = stealData(localDataBufferByte, 0);
/*      */ 
/*  203 */     int i = paramRectangle.x - paramPoint.x;
/*  204 */     int j = paramRectangle.y - paramPoint.y;
/*      */     Object localObject;
/*  205 */     if (((paramSampleModel instanceof PixelInterleavedSampleModel)) || (((paramSampleModel instanceof ComponentSampleModel)) && (isInterleaved((ComponentSampleModel)paramSampleModel))))
/*      */     {
/*  208 */       localObject = (ComponentSampleModel)paramSampleModel;
/*  209 */       this.scanlineStride = ((ComponentSampleModel)localObject).getScanlineStride();
/*  210 */       this.pixelStride = ((ComponentSampleModel)localObject).getPixelStride();
/*  211 */       this.dataOffsets = ((ComponentSampleModel)localObject).getBandOffsets();
/*  212 */       for (int m = 0; m < getNumDataElements(); m++)
/*  213 */         this.dataOffsets[m] += i * this.pixelStride + j * this.scanlineStride;
/*      */     }
/*  215 */     else if ((paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/*  216 */       localObject = (SinglePixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  218 */       this.packed = true;
/*  219 */       this.bitMasks = ((SinglePixelPackedSampleModel)localObject).getBitMasks();
/*  220 */       this.bitOffsets = ((SinglePixelPackedSampleModel)localObject).getBitOffsets();
/*  221 */       this.scanlineStride = ((SinglePixelPackedSampleModel)localObject).getScanlineStride();
/*  222 */       this.pixelStride = 1;
/*  223 */       this.dataOffsets = new int[1];
/*  224 */       this.dataOffsets[0] = localDataBufferByte.getOffset();
/*  225 */       this.dataOffsets[0] += i * this.pixelStride + j * this.scanlineStride;
/*      */     } else {
/*  227 */       throw new RasterFormatException("ByteInterleavedRasters must have PixelInterleavedSampleModel, SinglePixelPackedSampleModel or interleaved ComponentSampleModel.  Sample model is " + paramSampleModel);
/*      */     }
/*      */ 
/*  232 */     this.bandOffset = this.dataOffsets[0];
/*      */ 
/*  234 */     this.dbOffsetPacked = (paramDataBuffer.getOffset() - this.sampleModelTranslateY * this.scanlineStride - this.sampleModelTranslateX * this.pixelStride);
/*      */ 
/*  237 */     this.dbOffset = (this.dbOffsetPacked - (i * this.pixelStride + j * this.scanlineStride));
/*      */ 
/*  242 */     this.inOrder = false;
/*  243 */     if (this.numDataElements == this.pixelStride) {
/*  244 */       this.inOrder = true;
/*  245 */       for (int k = 1; k < this.numDataElements; k++) {
/*  246 */         if (this.dataOffsets[k] - this.dataOffsets[0] != k) {
/*  247 */           this.inOrder = false;
/*  248 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  253 */     verify();
/*      */   }
/*      */ 
/*      */   public int[] getDataOffsets()
/*      */   {
/*  262 */     return (int[])this.dataOffsets.clone();
/*      */   }
/*      */ 
/*      */   public int getDataOffset(int paramInt)
/*      */   {
/*  272 */     return this.dataOffsets[paramInt];
/*      */   }
/*      */ 
/*      */   public int getScanlineStride()
/*      */   {
/*  281 */     return this.scanlineStride;
/*      */   }
/*      */ 
/*      */   public int getPixelStride()
/*      */   {
/*  289 */     return this.pixelStride;
/*      */   }
/*      */ 
/*      */   public byte[] getDataStorage()
/*      */   {
/*  296 */     return this.data;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/*  316 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  318 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     byte[] arrayOfByte;
/*  322 */     if (paramObject == null)
/*  323 */       arrayOfByte = new byte[this.numDataElements];
/*      */     else {
/*  325 */       arrayOfByte = (byte[])paramObject;
/*      */     }
/*  327 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*      */ 
/*  330 */     for (int j = 0; j < this.numDataElements; j++) {
/*  331 */       arrayOfByte[j] = this.data[(this.dataOffsets[j] + i)];
/*      */     }
/*      */ 
/*  334 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*      */   {
/*  364 */     return getByteData(paramInt1, paramInt2, paramInt3, paramInt4, (byte[])paramObject);
/*      */   }
/*      */ 
/*      */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*      */   {
/*  389 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  391 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  394 */     if (paramArrayOfByte == null) {
/*  395 */       paramArrayOfByte = new byte[paramInt3 * paramInt4];
/*      */     }
/*  397 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*      */ 
/*  400 */     int k = 0;
/*      */     int n;
/*  404 */     if (this.pixelStride == 1) {
/*  405 */       if (this.scanlineStride == paramInt3)
/*  406 */         System.arraycopy(this.data, i, paramArrayOfByte, 0, paramInt3 * paramInt4);
/*      */       else {
/*  408 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  409 */           System.arraycopy(this.data, i, paramArrayOfByte, k, paramInt3);
/*  410 */           k += paramInt3;
/*      */ 
/*  408 */           n++;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  414 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  415 */         int j = i;
/*  416 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/*  417 */           paramArrayOfByte[(k++)] = this.data[j];
/*      */ 
/*  416 */           m++;
/*      */         }
/*  414 */         n++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  422 */     return paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*      */   {
/*  447 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  449 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  452 */     if (paramArrayOfByte == null) {
/*  453 */       paramArrayOfByte = new byte[this.numDataElements * paramInt3 * paramInt4];
/*      */     }
/*  455 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*      */ 
/*  458 */     int k = 0;
/*      */     int i1;
/*      */     int n;
/*  462 */     if (this.inOrder) {
/*  463 */       i += this.dataOffsets[0];
/*  464 */       i1 = paramInt3 * this.pixelStride;
/*  465 */       if (this.scanlineStride == i1)
/*  466 */         System.arraycopy(this.data, i, paramArrayOfByte, k, i1 * paramInt4);
/*      */       else
/*  468 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  469 */           System.arraycopy(this.data, i, paramArrayOfByte, k, i1);
/*  470 */           k += i1;
/*      */ 
/*  468 */           n++;
/*      */         }
/*      */     }
/*      */     else
/*      */     {
/*      */       int j;
/*      */       int m;
/*  473 */       if (this.numDataElements == 1) {
/*  474 */         i += this.dataOffsets[0];
/*  475 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  476 */           j = i;
/*  477 */           for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  478 */             paramArrayOfByte[(k++)] = this.data[j];
/*      */ 
/*  477 */             m++;
/*      */           }
/*  475 */           n++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  481 */       if (this.numDataElements == 2) {
/*  482 */         i += this.dataOffsets[0];
/*  483 */         i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  484 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  485 */           j = i;
/*  486 */           for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  487 */             paramArrayOfByte[(k++)] = this.data[j];
/*  488 */             paramArrayOfByte[(k++)] = this.data[(j + i1)];
/*      */ 
/*  486 */             m++;
/*      */           }
/*  484 */           n++;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i2;
/*  491 */         if (this.numDataElements == 3) {
/*  492 */           i += this.dataOffsets[0];
/*  493 */           i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  494 */           i2 = this.dataOffsets[2] - this.dataOffsets[0];
/*  495 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  496 */             j = i;
/*  497 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  498 */               paramArrayOfByte[(k++)] = this.data[j];
/*  499 */               paramArrayOfByte[(k++)] = this.data[(j + i1)];
/*  500 */               paramArrayOfByte[(k++)] = this.data[(j + i2)];
/*      */ 
/*  497 */               m++;
/*      */             }
/*  495 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*  503 */         else if (this.numDataElements == 4) {
/*  504 */           i += this.dataOffsets[0];
/*  505 */           i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  506 */           i2 = this.dataOffsets[2] - this.dataOffsets[0];
/*  507 */           int i3 = this.dataOffsets[3] - this.dataOffsets[0];
/*  508 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  509 */             j = i;
/*  510 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  511 */               paramArrayOfByte[(k++)] = this.data[j];
/*  512 */               paramArrayOfByte[(k++)] = this.data[(j + i1)];
/*  513 */               paramArrayOfByte[(k++)] = this.data[(j + i2)];
/*  514 */               paramArrayOfByte[(k++)] = this.data[(j + i3)];
/*      */ 
/*  510 */               m++;
/*      */             }
/*  508 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  518 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  519 */             j = i;
/*  520 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  521 */               for (i1 = 0; i1 < this.numDataElements; i1++)
/*  522 */                 paramArrayOfByte[(k++)] = this.data[(this.dataOffsets[i1] + j)];
/*  520 */               m++;
/*      */             }
/*  518 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  528 */     return paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/*  544 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  546 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  549 */     byte[] arrayOfByte = (byte[])paramObject;
/*  550 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*      */ 
/*  553 */     for (int j = 0; j < this.numDataElements; j++) {
/*  554 */       this.data[(this.dataOffsets[j] + i)] = arrayOfByte[j];
/*      */     }
/*      */ 
/*  557 */     markDirty();
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*      */   {
/*  569 */     int i = paramRaster.getMinX();
/*  570 */     int j = paramRaster.getMinY();
/*  571 */     int k = paramInt1 + i;
/*  572 */     int m = paramInt2 + j;
/*  573 */     int n = paramRaster.getWidth();
/*  574 */     int i1 = paramRaster.getHeight();
/*  575 */     if ((k < this.minX) || (m < this.minY) || (k + n > this.maxX) || (m + i1 > this.maxY))
/*      */     {
/*  577 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  581 */     setDataElements(k, m, i, j, n, i1, paramRaster);
/*      */   }
/*      */ 
/*      */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Raster paramRaster)
/*      */   {
/*  606 */     if ((paramInt5 <= 0) || (paramInt6 <= 0)) {
/*  607 */       return;
/*      */     }
/*      */ 
/*  612 */     int i = paramRaster.getMinX();
/*  613 */     int j = paramRaster.getMinY();
/*  614 */     Object localObject = null;
/*      */ 
/*  616 */     if ((paramRaster instanceof ByteInterleavedRaster)) {
/*  617 */       ByteInterleavedRaster localByteInterleavedRaster = (ByteInterleavedRaster)paramRaster;
/*  618 */       byte[] arrayOfByte = localByteInterleavedRaster.getDataStorage();
/*      */ 
/*  620 */       if ((this.inOrder) && (localByteInterleavedRaster.inOrder) && (this.pixelStride == localByteInterleavedRaster.pixelStride)) {
/*  621 */         int m = localByteInterleavedRaster.getDataOffset(0);
/*  622 */         int n = localByteInterleavedRaster.getScanlineStride();
/*  623 */         int i1 = localByteInterleavedRaster.getPixelStride();
/*      */ 
/*  625 */         int i2 = m + (paramInt4 - j) * n + (paramInt3 - i) * i1;
/*      */ 
/*  628 */         int i3 = this.dataOffsets[0] + (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*      */ 
/*  632 */         int i4 = paramInt5 * this.pixelStride;
/*  633 */         for (int i5 = 0; i5 < paramInt6; i5++) {
/*  634 */           System.arraycopy(arrayOfByte, i2, this.data, i3, i4);
/*      */ 
/*  636 */           i2 += n;
/*  637 */           i3 += this.scanlineStride;
/*      */         }
/*  639 */         markDirty();
/*  640 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  644 */     for (int k = 0; k < paramInt6; k++)
/*      */     {
/*  646 */       localObject = paramRaster.getDataElements(i, j + k, paramInt5, 1, localObject);
/*      */ 
/*  648 */       setDataElements(paramInt1, paramInt2 + k, paramInt5, 1, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*      */   {
/*  675 */     putByteData(paramInt1, paramInt2, paramInt3, paramInt4, (byte[])paramObject);
/*      */   }
/*      */ 
/*      */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*      */   {
/*  699 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  701 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  704 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride + this.dataOffsets[paramInt5];
/*      */ 
/*  707 */     int k = 0;
/*      */     int n;
/*  711 */     if (this.pixelStride == 1) {
/*  712 */       if (this.scanlineStride == paramInt3) {
/*  713 */         System.arraycopy(paramArrayOfByte, 0, this.data, i, paramInt3 * paramInt4);
/*      */       }
/*      */       else {
/*  716 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  717 */           System.arraycopy(paramArrayOfByte, k, this.data, i, paramInt3);
/*  718 */           k += paramInt3;
/*      */ 
/*  716 */           n++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  723 */       for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  724 */         int j = i;
/*  725 */         for (int m = 0; m < paramInt3; j += this.pixelStride) {
/*  726 */           this.data[j] = paramArrayOfByte[(k++)];
/*      */ 
/*  725 */           m++;
/*      */         }
/*  723 */         n++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  731 */     markDirty();
/*      */   }
/*      */ 
/*      */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*      */   {
/*  752 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  754 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  757 */     int i = (paramInt2 - this.minY) * this.scanlineStride + (paramInt1 - this.minX) * this.pixelStride;
/*      */ 
/*  761 */     int k = 0;
/*      */     int i1;
/*      */     int n;
/*  765 */     if (this.inOrder) {
/*  766 */       i += this.dataOffsets[0];
/*  767 */       i1 = paramInt3 * this.pixelStride;
/*  768 */       if (i1 == this.scanlineStride)
/*  769 */         System.arraycopy(paramArrayOfByte, 0, this.data, i, i1 * paramInt4);
/*      */       else
/*  771 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  772 */           System.arraycopy(paramArrayOfByte, k, this.data, i, i1);
/*  773 */           k += i1;
/*      */ 
/*  771 */           n++;
/*      */         }
/*      */     }
/*      */     else
/*      */     {
/*      */       int j;
/*      */       int m;
/*  776 */       if (this.numDataElements == 1) {
/*  777 */         i += this.dataOffsets[0];
/*  778 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  779 */           j = i;
/*  780 */           for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  781 */             this.data[j] = paramArrayOfByte[(k++)];
/*      */ 
/*  780 */             m++;
/*      */           }
/*  778 */           n++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  784 */       if (this.numDataElements == 2) {
/*  785 */         i += this.dataOffsets[0];
/*  786 */         i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  787 */         for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  788 */           j = i;
/*  789 */           for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  790 */             this.data[j] = paramArrayOfByte[(k++)];
/*  791 */             this.data[(j + i1)] = paramArrayOfByte[(k++)];
/*      */ 
/*  789 */             m++;
/*      */           }
/*  787 */           n++;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i2;
/*  794 */         if (this.numDataElements == 3) {
/*  795 */           i += this.dataOffsets[0];
/*  796 */           i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  797 */           i2 = this.dataOffsets[2] - this.dataOffsets[0];
/*  798 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  799 */             j = i;
/*  800 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  801 */               this.data[j] = paramArrayOfByte[(k++)];
/*  802 */               this.data[(j + i1)] = paramArrayOfByte[(k++)];
/*  803 */               this.data[(j + i2)] = paramArrayOfByte[(k++)];
/*      */ 
/*  800 */               m++;
/*      */             }
/*  798 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*  806 */         else if (this.numDataElements == 4) {
/*  807 */           i += this.dataOffsets[0];
/*  808 */           i1 = this.dataOffsets[1] - this.dataOffsets[0];
/*  809 */           i2 = this.dataOffsets[2] - this.dataOffsets[0];
/*  810 */           int i3 = this.dataOffsets[3] - this.dataOffsets[0];
/*  811 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  812 */             j = i;
/*  813 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  814 */               this.data[j] = paramArrayOfByte[(k++)];
/*  815 */               this.data[(j + i1)] = paramArrayOfByte[(k++)];
/*  816 */               this.data[(j + i2)] = paramArrayOfByte[(k++)];
/*  817 */               this.data[(j + i3)] = paramArrayOfByte[(k++)];
/*      */ 
/*  813 */               m++;
/*      */             }
/*  811 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  821 */           for (n = 0; n < paramInt4; i += this.scanlineStride) {
/*  822 */             j = i;
/*  823 */             for (m = 0; m < paramInt3; j += this.pixelStride) {
/*  824 */               for (i1 = 0; i1 < this.numDataElements; i1++)
/*  825 */                 this.data[(this.dataOffsets[i1] + j)] = paramArrayOfByte[(k++)];
/*  823 */               m++;
/*      */             }
/*  821 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  831 */     markDirty();
/*      */   }
/*      */ 
/*      */   public int getSample(int paramInt1, int paramInt2, int paramInt3) {
/*  835 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  837 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  840 */     if (this.packed) {
/*  841 */       i = paramInt2 * this.scanlineStride + paramInt1 + this.dbOffsetPacked;
/*  842 */       int j = this.data[i];
/*  843 */       return (j & this.bitMasks[paramInt3]) >>> this.bitOffsets[paramInt3];
/*      */     }
/*  845 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.dbOffset;
/*  846 */     return this.data[(i + this.dataOffsets[paramInt3])] & 0xFF;
/*      */   }
/*      */ 
/*      */   public void setSample(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  851 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  853 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     int i;
/*  856 */     if (this.packed) {
/*  857 */       i = paramInt2 * this.scanlineStride + paramInt1 + this.dbOffsetPacked;
/*  858 */       int j = this.bitMasks[paramInt3];
/*      */ 
/*  860 */       int k = this.data[i];
/*  861 */       k = (byte)(k & (j ^ 0xFFFFFFFF));
/*  862 */       k = (byte)(k | paramInt4 << this.bitOffsets[paramInt3] & j);
/*  863 */       this.data[i] = k;
/*      */     } else {
/*  865 */       i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride + this.dbOffset;
/*  866 */       this.data[(i + this.dataOffsets[paramInt3])] = ((byte)paramInt4);
/*      */     }
/*      */ 
/*  869 */     markDirty();
/*      */   }
/*      */ 
/*      */   public int[] getSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt)
/*      */   {
/*  874 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  876 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     int[] arrayOfInt;
/*  880 */     if (paramArrayOfInt != null)
/*  881 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  883 */       arrayOfInt = new int[paramInt3 * paramInt4];
/*      */     }
/*      */ 
/*  886 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*  887 */     int j = 0;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*  889 */     if (this.packed) {
/*  890 */       i += this.dbOffsetPacked;
/*  891 */       k = this.bitMasks[paramInt5];
/*  892 */       m = this.bitOffsets[paramInt5];
/*      */ 
/*  894 */       for (n = 0; n < paramInt4; n++) {
/*  895 */         int i1 = i;
/*  896 */         for (int i2 = 0; i2 < paramInt3; i2++) {
/*  897 */           int i3 = this.data[(i1++)];
/*  898 */           arrayOfInt[(j++)] = ((i3 & k) >>> m);
/*      */         }
/*  900 */         i += this.scanlineStride;
/*      */       }
/*      */     } else {
/*  903 */       i += this.dbOffset + this.dataOffsets[paramInt5];
/*  904 */       for (k = 0; k < paramInt4; k++) {
/*  905 */         m = i;
/*  906 */         for (n = 0; n < paramInt3; n++) {
/*  907 */           arrayOfInt[(j++)] = (this.data[m] & 0xFF);
/*  908 */           m += this.pixelStride;
/*      */         }
/*  910 */         i += this.scanlineStride;
/*      */       }
/*      */     }
/*      */ 
/*  914 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setSamples(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt) {
/*  918 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  920 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  923 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*  924 */     int j = 0;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*  926 */     if (this.packed) {
/*  927 */       i += this.dbOffsetPacked;
/*  928 */       k = this.bitMasks[paramInt5];
/*      */ 
/*  930 */       for (m = 0; m < paramInt4; m++) {
/*  931 */         n = i;
/*  932 */         for (int i1 = 0; i1 < paramInt3; i1++) {
/*  933 */           int i2 = this.data[n];
/*  934 */           i2 = (byte)(i2 & (k ^ 0xFFFFFFFF));
/*  935 */           int i3 = paramArrayOfInt[(j++)];
/*  936 */           i2 = (byte)(i2 | i3 << this.bitOffsets[paramInt5] & k);
/*  937 */           this.data[(n++)] = i2;
/*      */         }
/*  939 */         i += this.scanlineStride;
/*      */       }
/*      */     } else {
/*  942 */       i += this.dbOffset + this.dataOffsets[paramInt5];
/*  943 */       for (k = 0; k < paramInt4; k++) {
/*  944 */         m = i;
/*  945 */         for (n = 0; n < paramInt3; n++) {
/*  946 */           this.data[m] = ((byte)paramArrayOfInt[(j++)]);
/*  947 */           m += this.pixelStride;
/*      */         }
/*  949 */         i += this.scanlineStride;
/*      */       }
/*      */     }
/*      */ 
/*  953 */     markDirty();
/*      */   }
/*      */ 
/*      */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
/*  957 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  959 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     int[] arrayOfInt;
/*  963 */     if (paramArrayOfInt != null)
/*  964 */       arrayOfInt = paramArrayOfInt;
/*      */     else {
/*  966 */       arrayOfInt = new int[paramInt3 * paramInt4 * this.numBands];
/*      */     }
/*      */ 
/*  969 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/*  970 */     int j = 0;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/*  972 */     if (this.packed) {
/*  973 */       i += this.dbOffsetPacked;
/*  974 */       for (k = 0; k < paramInt4; k++) {
/*  975 */         for (m = 0; m < paramInt3; m++) {
/*  976 */           n = this.data[(i + m)];
/*  977 */           for (i1 = 0; i1 < this.numBands; i1++) {
/*  978 */             arrayOfInt[(j++)] = ((n & this.bitMasks[i1]) >>> this.bitOffsets[i1]);
/*      */           }
/*      */         }
/*      */ 
/*  982 */         i += this.scanlineStride;
/*      */       }
/*      */     } else {
/*  985 */       i += this.dbOffset;
/*  986 */       k = this.dataOffsets[0];
/*      */ 
/*  988 */       if (this.numBands == 1) {
/*  989 */         for (m = 0; m < paramInt4; m++) {
/*  990 */           n = i + k;
/*  991 */           for (i1 = 0; i1 < paramInt3; i1++) {
/*  992 */             arrayOfInt[(j++)] = (this.data[n] & 0xFF);
/*  993 */             n += this.pixelStride;
/*      */           }
/*  995 */           i += this.scanlineStride;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i2;
/*  997 */         if (this.numBands == 2) {
/*  998 */           m = this.dataOffsets[1] - k;
/*  999 */           for (n = 0; n < paramInt4; n++) {
/* 1000 */             i1 = i + k;
/* 1001 */             for (i2 = 0; i2 < paramInt3; i2++) {
/* 1002 */               arrayOfInt[(j++)] = (this.data[i1] & 0xFF);
/* 1003 */               arrayOfInt[(j++)] = (this.data[(i1 + m)] & 0xFF);
/* 1004 */               i1 += this.pixelStride;
/*      */             }
/* 1006 */             i += this.scanlineStride;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*      */           int i3;
/* 1008 */           if (this.numBands == 3) {
/* 1009 */             m = this.dataOffsets[1] - k;
/* 1010 */             n = this.dataOffsets[2] - k;
/* 1011 */             for (i1 = 0; i1 < paramInt4; i1++) {
/* 1012 */               i2 = i + k;
/* 1013 */               for (i3 = 0; i3 < paramInt3; i3++) {
/* 1014 */                 arrayOfInt[(j++)] = (this.data[i2] & 0xFF);
/* 1015 */                 arrayOfInt[(j++)] = (this.data[(i2 + m)] & 0xFF);
/* 1016 */                 arrayOfInt[(j++)] = (this.data[(i2 + n)] & 0xFF);
/* 1017 */                 i2 += this.pixelStride;
/*      */               }
/* 1019 */               i += this.scanlineStride;
/*      */             }
/* 1021 */           } else if (this.numBands == 4) {
/* 1022 */             m = this.dataOffsets[1] - k;
/* 1023 */             n = this.dataOffsets[2] - k;
/* 1024 */             i1 = this.dataOffsets[3] - k;
/* 1025 */             for (i2 = 0; i2 < paramInt4; i2++) {
/* 1026 */               i3 = i + k;
/* 1027 */               for (int i4 = 0; i4 < paramInt3; i4++) {
/* 1028 */                 arrayOfInt[(j++)] = (this.data[i3] & 0xFF);
/* 1029 */                 arrayOfInt[(j++)] = (this.data[(i3 + m)] & 0xFF);
/* 1030 */                 arrayOfInt[(j++)] = (this.data[(i3 + n)] & 0xFF);
/* 1031 */                 arrayOfInt[(j++)] = (this.data[(i3 + i1)] & 0xFF);
/* 1032 */                 i3 += this.pixelStride;
/*      */               }
/* 1034 */               i += this.scanlineStride;
/*      */             }
/*      */           } else {
/* 1037 */             for (m = 0; m < paramInt4; m++) {
/* 1038 */               n = i;
/* 1039 */               for (i1 = 0; i1 < paramInt3; i1++) {
/* 1040 */                 for (i2 = 0; i2 < this.numBands; i2++) {
/* 1041 */                   arrayOfInt[(j++)] = (this.data[(n + this.dataOffsets[i2])] & 0xFF);
/*      */                 }
/*      */ 
/* 1044 */                 n += this.pixelStride;
/*      */               }
/* 1046 */               i += this.scanlineStride;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1051 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt) {
/* 1055 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/* 1057 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1060 */     int i = paramInt2 * this.scanlineStride + paramInt1 * this.pixelStride;
/* 1061 */     int j = 0;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/*      */     int i2;
/* 1063 */     if (this.packed) {
/* 1064 */       i += this.dbOffsetPacked;
/* 1065 */       for (k = 0; k < paramInt4; k++) {
/* 1066 */         for (m = 0; m < paramInt3; m++) {
/* 1067 */           n = 0;
/* 1068 */           for (i1 = 0; i1 < this.numBands; i1++) {
/* 1069 */             i2 = paramArrayOfInt[(j++)];
/* 1070 */             n |= i2 << this.bitOffsets[i1] & this.bitMasks[i1];
/*      */           }
/*      */ 
/* 1073 */           this.data[(i + m)] = ((byte)n);
/*      */         }
/* 1075 */         i += this.scanlineStride;
/*      */       }
/*      */     } else {
/* 1078 */       i += this.dbOffset;
/* 1079 */       k = this.dataOffsets[0];
/*      */ 
/* 1081 */       if (this.numBands == 1) {
/* 1082 */         for (m = 0; m < paramInt4; m++) {
/* 1083 */           n = i + k;
/* 1084 */           for (i1 = 0; i1 < paramInt3; i1++) {
/* 1085 */             this.data[n] = ((byte)paramArrayOfInt[(j++)]);
/* 1086 */             n += this.pixelStride;
/*      */           }
/* 1088 */           i += this.scanlineStride;
/*      */         }
/* 1090 */       } else if (this.numBands == 2) {
/* 1091 */         m = this.dataOffsets[1] - k;
/* 1092 */         for (n = 0; n < paramInt4; n++) {
/* 1093 */           i1 = i + k;
/* 1094 */           for (i2 = 0; i2 < paramInt3; i2++) {
/* 1095 */             this.data[i1] = ((byte)paramArrayOfInt[(j++)]);
/* 1096 */             this.data[(i1 + m)] = ((byte)paramArrayOfInt[(j++)]);
/* 1097 */             i1 += this.pixelStride;
/*      */           }
/* 1099 */           i += this.scanlineStride;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int i3;
/* 1101 */         if (this.numBands == 3) {
/* 1102 */           m = this.dataOffsets[1] - k;
/* 1103 */           n = this.dataOffsets[2] - k;
/* 1104 */           for (i1 = 0; i1 < paramInt4; i1++) {
/* 1105 */             i2 = i + k;
/* 1106 */             for (i3 = 0; i3 < paramInt3; i3++) {
/* 1107 */               this.data[i2] = ((byte)paramArrayOfInt[(j++)]);
/* 1108 */               this.data[(i2 + m)] = ((byte)paramArrayOfInt[(j++)]);
/* 1109 */               this.data[(i2 + n)] = ((byte)paramArrayOfInt[(j++)]);
/* 1110 */               i2 += this.pixelStride;
/*      */             }
/* 1112 */             i += this.scanlineStride;
/*      */           }
/* 1114 */         } else if (this.numBands == 4) {
/* 1115 */           m = this.dataOffsets[1] - k;
/* 1116 */           n = this.dataOffsets[2] - k;
/* 1117 */           i1 = this.dataOffsets[3] - k;
/* 1118 */           for (i2 = 0; i2 < paramInt4; i2++) {
/* 1119 */             i3 = i + k;
/* 1120 */             for (int i4 = 0; i4 < paramInt3; i4++) {
/* 1121 */               this.data[i3] = ((byte)paramArrayOfInt[(j++)]);
/* 1122 */               this.data[(i3 + m)] = ((byte)paramArrayOfInt[(j++)]);
/* 1123 */               this.data[(i3 + n)] = ((byte)paramArrayOfInt[(j++)]);
/* 1124 */               this.data[(i3 + i1)] = ((byte)paramArrayOfInt[(j++)]);
/* 1125 */               i3 += this.pixelStride;
/*      */             }
/* 1127 */             i += this.scanlineStride;
/*      */           }
/*      */         } else {
/* 1130 */           for (m = 0; m < paramInt4; m++) {
/* 1131 */             n = i;
/* 1132 */             for (i1 = 0; i1 < paramInt3; i1++) {
/* 1133 */               for (i2 = 0; i2 < this.numBands; i2++) {
/* 1134 */                 this.data[(n + this.dataOffsets[i2])] = ((byte)paramArrayOfInt[(j++)]);
/*      */               }
/*      */ 
/* 1137 */               n += this.pixelStride;
/*      */             }
/* 1139 */             i += this.scanlineStride;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1144 */     markDirty();
/*      */   }
/*      */ 
/*      */   public void setRect(int paramInt1, int paramInt2, Raster paramRaster) {
/* 1148 */     if (!(paramRaster instanceof ByteInterleavedRaster)) {
/* 1149 */       super.setRect(paramInt1, paramInt2, paramRaster);
/* 1150 */       return;
/*      */     }
/*      */ 
/* 1153 */     int i = paramRaster.getWidth();
/* 1154 */     int j = paramRaster.getHeight();
/* 1155 */     int k = paramRaster.getMinX();
/* 1156 */     int m = paramRaster.getMinY();
/* 1157 */     int n = paramInt1 + k;
/* 1158 */     int i1 = paramInt2 + m;
/*      */     int i2;
/* 1161 */     if (n < this.minX) {
/* 1162 */       i2 = this.minX - n;
/* 1163 */       i -= i2;
/* 1164 */       k += i2;
/* 1165 */       n = this.minX;
/*      */     }
/* 1167 */     if (i1 < this.minY) {
/* 1168 */       i2 = this.minY - i1;
/* 1169 */       j -= i2;
/* 1170 */       m += i2;
/* 1171 */       i1 = this.minY;
/*      */     }
/* 1173 */     if (n + i > this.maxX) {
/* 1174 */       i = this.maxX - n;
/*      */     }
/* 1176 */     if (i1 + j > this.maxY) {
/* 1177 */       j = this.maxY - i1;
/*      */     }
/*      */ 
/* 1180 */     setDataElements(n, i1, k, m, i, j, paramRaster);
/*      */   }
/*      */ 
/*      */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 1208 */     WritableRaster localWritableRaster = createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*      */ 
/* 1212 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 1238 */     if (paramInt1 < this.minX) {
/* 1239 */       throw new RasterFormatException("x lies outside the raster");
/*      */     }
/* 1241 */     if (paramInt2 < this.minY) {
/* 1242 */       throw new RasterFormatException("y lies outside the raster");
/*      */     }
/* 1244 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 1245 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*      */     }
/* 1247 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 1248 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*      */     SampleModel localSampleModel;
/* 1253 */     if (paramArrayOfInt != null)
/* 1254 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*      */     else {
/* 1256 */       localSampleModel = this.sampleModel;
/*      */     }
/* 1258 */     int i = paramInt5 - paramInt1;
/* 1259 */     int j = paramInt6 - paramInt2;
/*      */ 
/* 1261 */     return new ByteInterleavedRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/* 1274 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 1275 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*      */     }
/*      */ 
/* 1279 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*      */ 
/* 1281 */     return new ByteInterleavedRaster(localSampleModel, new Point(0, 0));
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster()
/*      */   {
/* 1292 */     return createCompatibleWritableRaster(this.width, this.height);
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1296 */     return new String("ByteInterleavedRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements + " dataOff[0] = " + this.dataOffsets[0]);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ByteInterleavedRaster
 * JD-Core Version:    0.6.2
 */