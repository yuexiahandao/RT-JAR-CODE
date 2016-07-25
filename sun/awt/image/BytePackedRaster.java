/*      */ package sun.awt.image;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RasterFormatException;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ 
/*      */ public class BytePackedRaster extends SunWritableRaster
/*      */ {
/*      */   int dataBitOffset;
/*      */   int scanlineStride;
/*      */   int pixelBitStride;
/*      */   int bitMask;
/*      */   byte[] data;
/*      */   int shiftOffset;
/*      */   int type;
/*      */   private int maxX;
/*      */   private int maxY;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public BytePackedRaster(SampleModel paramSampleModel, Point paramPoint)
/*      */   {
/*   94 */     this(paramSampleModel, paramSampleModel.createDataBuffer(), new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*      */   }
/*      */ 
/*      */   public BytePackedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Point paramPoint)
/*      */   {
/*  117 */     this(paramSampleModel, paramDataBuffer, new Rectangle(paramPoint.x, paramPoint.y, paramSampleModel.getWidth(), paramSampleModel.getHeight()), paramPoint, null);
/*      */   }
/*      */ 
/*      */   public BytePackedRaster(SampleModel paramSampleModel, DataBuffer paramDataBuffer, Rectangle paramRectangle, Point paramPoint, BytePackedRaster paramBytePackedRaster)
/*      */   {
/*  153 */     super(paramSampleModel, paramDataBuffer, paramRectangle, paramPoint, paramBytePackedRaster);
/*  154 */     this.maxX = (this.minX + this.width);
/*  155 */     this.maxY = (this.minY + this.height);
/*      */ 
/*  157 */     if (!(paramDataBuffer instanceof DataBufferByte)) {
/*  158 */       throw new RasterFormatException("BytePackedRasters must havebyte DataBuffers");
/*      */     }
/*      */ 
/*  161 */     DataBufferByte localDataBufferByte = (DataBufferByte)paramDataBuffer;
/*  162 */     this.data = stealData(localDataBufferByte, 0);
/*  163 */     if (localDataBufferByte.getNumBanks() != 1) {
/*  164 */       throw new RasterFormatException("DataBuffer for BytePackedRasters must only have 1 bank.");
/*      */     }
/*      */ 
/*  168 */     int i = localDataBufferByte.getOffset();
/*      */ 
/*  170 */     if ((paramSampleModel instanceof MultiPixelPackedSampleModel)) {
/*  171 */       MultiPixelPackedSampleModel localMultiPixelPackedSampleModel = (MultiPixelPackedSampleModel)paramSampleModel;
/*      */ 
/*  173 */       this.type = 11;
/*  174 */       this.pixelBitStride = localMultiPixelPackedSampleModel.getPixelBitStride();
/*  175 */       if ((this.pixelBitStride != 1) && (this.pixelBitStride != 2) && (this.pixelBitStride != 4))
/*      */       {
/*  178 */         throw new RasterFormatException("BytePackedRasters must have a bit depth of 1, 2, or 4");
/*      */       }
/*      */ 
/*  181 */       this.scanlineStride = localMultiPixelPackedSampleModel.getScanlineStride();
/*  182 */       this.dataBitOffset = (localMultiPixelPackedSampleModel.getDataBitOffset() + i * 8);
/*  183 */       int j = paramRectangle.x - paramPoint.x;
/*  184 */       int k = paramRectangle.y - paramPoint.y;
/*  185 */       this.dataBitOffset += j * this.pixelBitStride + k * this.scanlineStride * 8;
/*  186 */       this.bitMask = ((1 << this.pixelBitStride) - 1);
/*  187 */       this.shiftOffset = (8 - this.pixelBitStride);
/*      */     } else {
/*  189 */       throw new RasterFormatException("BytePackedRasters must haveMultiPixelPackedSampleModel");
/*      */     }
/*      */ 
/*  192 */     verify(false);
/*      */   }
/*      */ 
/*      */   public int getDataBitOffset()
/*      */   {
/*  201 */     return this.dataBitOffset;
/*      */   }
/*      */ 
/*      */   public int getScanlineStride()
/*      */   {
/*  210 */     return this.scanlineStride;
/*      */   }
/*      */ 
/*      */   public int getPixelBitStride()
/*      */   {
/*  218 */     return this.pixelBitStride;
/*      */   }
/*      */ 
/*      */   public byte[] getDataStorage()
/*      */   {
/*  225 */     return this.data;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/*  245 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  247 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     byte[] arrayOfByte;
/*  251 */     if (paramObject == null)
/*  252 */       arrayOfByte = new byte[this.numDataElements];
/*      */     else {
/*  254 */       arrayOfByte = (byte[])paramObject;
/*      */     }
/*  256 */     int i = this.dataBitOffset + (paramInt1 - this.minX) * this.pixelBitStride;
/*      */ 
/*  258 */     int j = this.data[((paramInt2 - this.minY) * this.scanlineStride + (i >> 3))] & 0xFF;
/*  259 */     int k = this.shiftOffset - (i & 0x7);
/*  260 */     arrayOfByte[0] = ((byte)(j >> k & this.bitMask));
/*  261 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public Object getDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*      */   {
/*  290 */     return getByteData(paramInt1, paramInt2, paramInt3, paramInt4, (byte[])paramObject);
/*      */   }
/*      */ 
/*      */   public Object getPixelData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*      */   {
/*  319 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  321 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */     byte[] arrayOfByte1;
/*  325 */     if (paramObject == null)
/*  326 */       arrayOfByte1 = new byte[this.numDataElements * paramInt3 * paramInt4];
/*      */     else {
/*  328 */       arrayOfByte1 = (byte[])paramObject;
/*      */     }
/*  330 */     int i = this.pixelBitStride;
/*  331 */     int j = this.dataBitOffset + (paramInt1 - this.minX) * i;
/*  332 */     int k = (paramInt2 - this.minY) * this.scanlineStride;
/*  333 */     int m = 0;
/*  334 */     byte[] arrayOfByte2 = this.data;
/*      */ 
/*  336 */     for (int n = 0; n < paramInt4; n++) {
/*  337 */       int i1 = j;
/*  338 */       for (int i2 = 0; i2 < paramInt3; i2++) {
/*  339 */         int i3 = this.shiftOffset - (i1 & 0x7);
/*  340 */         arrayOfByte1[(m++)] = ((byte)(this.bitMask & arrayOfByte2[(k + (i1 >> 3))] >> i3));
/*      */ 
/*  342 */         i1 += i;
/*      */       }
/*  344 */       k += this.scanlineStride;
/*      */     }
/*  346 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*      */   {
/*  370 */     return getByteData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public byte[] getByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*      */   {
/*  393 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  395 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  398 */     if (paramArrayOfByte == null) {
/*  399 */       paramArrayOfByte = new byte[paramInt3 * paramInt4];
/*      */     }
/*  401 */     int i = this.pixelBitStride;
/*  402 */     int j = this.dataBitOffset + (paramInt1 - this.minX) * i;
/*  403 */     int k = (paramInt2 - this.minY) * this.scanlineStride;
/*  404 */     int m = 0;
/*  405 */     byte[] arrayOfByte = this.data;
/*      */ 
/*  407 */     for (int n = 0; n < paramInt4; n++) {
/*  408 */       int i1 = j;
/*      */ 
/*  412 */       int i3 = 0;
/*  413 */       while ((i3 < paramInt3) && ((i1 & 0x7) != 0)) {
/*  414 */         i4 = this.shiftOffset - (i1 & 0x7);
/*  415 */         paramArrayOfByte[(m++)] = ((byte)(this.bitMask & arrayOfByte[(k + (i1 >> 3))] >> i4));
/*      */ 
/*  417 */         i1 += i;
/*  418 */         i3++;
/*      */       }
/*      */ 
/*  422 */       int i4 = k + (i1 >> 3);
/*  423 */       switch (i) { case 1:
/*      */       case 2:
/*      */       case 4:
/*  425 */         while (i3 < paramInt3 - 7) {
/*  426 */           int i2 = arrayOfByte[(i4++)];
/*  427 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 7 & 0x1));
/*  428 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 6 & 0x1));
/*  429 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 5 & 0x1));
/*  430 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0x1));
/*  431 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 3 & 0x1));
/*  432 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 2 & 0x1));
/*  433 */           paramArrayOfByte[(m++)] = ((byte)(i2 >> 1 & 0x1));
/*  434 */           paramArrayOfByte[(m++)] = ((byte)(i2 & 0x1));
/*  435 */           i1 += 8;
/*      */ 
/*  425 */           i3 += 8; continue;
/*      */ 
/*  440 */           while (i3 < paramInt3 - 7) {
/*  441 */             i2 = arrayOfByte[(i4++)];
/*  442 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 6 & 0x3));
/*  443 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0x3));
/*  444 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 2 & 0x3));
/*  445 */             paramArrayOfByte[(m++)] = ((byte)(i2 & 0x3));
/*      */ 
/*  447 */             i2 = arrayOfByte[(i4++)];
/*  448 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 6 & 0x3));
/*  449 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0x3));
/*  450 */             paramArrayOfByte[(m++)] = ((byte)(i2 >> 2 & 0x3));
/*  451 */             paramArrayOfByte[(m++)] = ((byte)(i2 & 0x3));
/*      */ 
/*  453 */             i1 += 16;
/*      */ 
/*  440 */             i3 += 8; continue;
/*      */ 
/*  458 */             for (; i3 < paramInt3 - 7; i3 += 8) {
/*  459 */               i2 = arrayOfByte[(i4++)];
/*  460 */               paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0xF));
/*  461 */               paramArrayOfByte[(m++)] = ((byte)(i2 & 0xF));
/*      */ 
/*  463 */               i2 = arrayOfByte[(i4++)];
/*  464 */               paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0xF));
/*  465 */               paramArrayOfByte[(m++)] = ((byte)(i2 & 0xF));
/*      */ 
/*  467 */               i2 = arrayOfByte[(i4++)];
/*  468 */               paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0xF));
/*  469 */               paramArrayOfByte[(m++)] = ((byte)(i2 & 0xF));
/*      */ 
/*  471 */               i2 = arrayOfByte[(i4++)];
/*  472 */               paramArrayOfByte[(m++)] = ((byte)(i2 >> 4 & 0xF));
/*  473 */               paramArrayOfByte[(m++)] = ((byte)(i2 & 0xF));
/*      */ 
/*  475 */               i1 += 32;
/*      */             }
/*      */           }
/*      */         }
/*      */       case 3:
/*      */       }
/*  481 */       for (; i3 < paramInt3; i3++) {
/*  482 */         int i5 = this.shiftOffset - (i1 & 0x7);
/*  483 */         paramArrayOfByte[(m++)] = ((byte)(this.bitMask & arrayOfByte[(k + (i1 >> 3))] >> i5));
/*      */ 
/*  485 */         i1 += i;
/*      */       }
/*      */ 
/*  488 */       k += this.scanlineStride;
/*      */     }
/*      */ 
/*  491 */     return paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, Object paramObject)
/*      */   {
/*  507 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 >= this.maxX) || (paramInt2 >= this.maxY))
/*      */     {
/*  509 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  512 */     byte[] arrayOfByte = (byte[])paramObject;
/*  513 */     int i = this.dataBitOffset + (paramInt1 - this.minX) * this.pixelBitStride;
/*  514 */     int j = (paramInt2 - this.minY) * this.scanlineStride + (i >> 3);
/*  515 */     int k = this.shiftOffset - (i & 0x7);
/*      */ 
/*  517 */     int m = this.data[j];
/*  518 */     m = (byte)(m & (this.bitMask << k ^ 0xFFFFFFFF));
/*  519 */     m = (byte)(m | (arrayOfByte[0] & this.bitMask) << k);
/*  520 */     this.data[j] = m;
/*      */ 
/*  522 */     markDirty();
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, Raster paramRaster)
/*      */   {
/*  535 */     if ((!(paramRaster instanceof BytePackedRaster)) || (((BytePackedRaster)paramRaster).pixelBitStride != this.pixelBitStride))
/*      */     {
/*  537 */       super.setDataElements(paramInt1, paramInt2, paramRaster);
/*  538 */       return;
/*      */     }
/*      */ 
/*  541 */     int i = paramRaster.getMinX();
/*  542 */     int j = paramRaster.getMinY();
/*  543 */     int k = i + paramInt1;
/*  544 */     int m = j + paramInt2;
/*  545 */     int n = paramRaster.getWidth();
/*  546 */     int i1 = paramRaster.getHeight();
/*  547 */     if ((k < this.minX) || (m < this.minY) || (k + n > this.maxX) || (m + i1 > this.maxY))
/*      */     {
/*  549 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  552 */     setDataElements(k, m, i, j, n, i1, (BytePackedRaster)paramRaster);
/*      */   }
/*      */ 
/*      */   private void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BytePackedRaster paramBytePackedRaster)
/*      */   {
/*  579 */     if ((paramInt5 <= 0) || (paramInt6 <= 0)) {
/*  580 */       return;
/*      */     }
/*      */ 
/*  583 */     byte[] arrayOfByte1 = paramBytePackedRaster.data;
/*  584 */     byte[] arrayOfByte2 = this.data;
/*      */ 
/*  586 */     int i = paramBytePackedRaster.scanlineStride;
/*  587 */     int j = this.scanlineStride;
/*  588 */     int k = paramBytePackedRaster.dataBitOffset + 8 * (paramInt4 - paramBytePackedRaster.minY) * i + (paramInt3 - paramBytePackedRaster.minX) * paramBytePackedRaster.pixelBitStride;
/*      */ 
/*  591 */     int m = this.dataBitOffset + 8 * (paramInt2 - this.minY) * j + (paramInt1 - this.minX) * this.pixelBitStride;
/*      */ 
/*  594 */     int n = paramInt5 * this.pixelBitStride;
/*      */     int i1;
/*      */     int i2;
/*      */     int i3;
/*      */     int i4;
/*      */     int i5;
/*      */     int i6;
/*      */     int i7;
/*  600 */     if ((k & 0x7) == (m & 0x7))
/*      */     {
/*  602 */       i1 = m & 0x7;
/*  603 */       if (i1 != 0) {
/*  604 */         i2 = 8 - i1;
/*      */ 
/*  606 */         i3 = k >> 3;
/*  607 */         i4 = m >> 3;
/*  608 */         i5 = 255 >> i1;
/*  609 */         if (n < i2)
/*      */         {
/*  617 */           i5 &= 255 << i2 - n;
/*  618 */           i2 = n;
/*      */         }
/*  620 */         for (i6 = 0; i6 < paramInt6; i6++) {
/*  621 */           i7 = arrayOfByte2[i4];
/*  622 */           i7 &= (i5 ^ 0xFFFFFFFF);
/*  623 */           i7 |= arrayOfByte1[i3] & i5;
/*  624 */           arrayOfByte2[i4] = ((byte)i7);
/*  625 */           i3 += i;
/*  626 */           i4 += j;
/*      */         }
/*  628 */         k += i2;
/*  629 */         m += i2;
/*  630 */         n -= i2;
/*      */       }
/*  632 */       if (n >= 8)
/*      */       {
/*  634 */         i2 = k >> 3;
/*  635 */         i3 = m >> 3;
/*  636 */         i4 = n >> 3;
/*  637 */         if ((i4 == i) && (i == j)) {
/*  638 */           System.arraycopy(arrayOfByte1, i2, arrayOfByte2, i3, i * paramInt6);
/*      */         }
/*      */         else
/*      */         {
/*  642 */           for (i5 = 0; i5 < paramInt6; i5++) {
/*  643 */             System.arraycopy(arrayOfByte1, i2, arrayOfByte2, i3, i4);
/*      */ 
/*  646 */             i2 += i;
/*  647 */             i3 += j;
/*      */           }
/*      */         }
/*      */ 
/*  651 */         i5 = i4 * 8;
/*  652 */         k += i5;
/*  653 */         m += i5;
/*  654 */         n -= i5;
/*      */       }
/*  656 */       if (n > 0)
/*      */       {
/*  658 */         i2 = k >> 3;
/*  659 */         i3 = m >> 3;
/*  660 */         i4 = 65280 >> n & 0xFF;
/*  661 */         for (i5 = 0; i5 < paramInt6; i5++) {
/*  662 */           i6 = arrayOfByte2[i3];
/*  663 */           i6 &= (i4 ^ 0xFFFFFFFF);
/*  664 */           i6 |= arrayOfByte1[i2] & i4;
/*  665 */           arrayOfByte2[i3] = ((byte)i6);
/*  666 */           i2 += i;
/*  667 */           i3 += j;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  677 */       i1 = m & 0x7;
/*      */       int i8;
/*      */       int i9;
/*      */       int i10;
/*      */       int i11;
/*      */       int i12;
/*  678 */       if ((i1 != 0) || (n < 8)) {
/*  679 */         i2 = 8 - i1;
/*  680 */         i3 = k >> 3;
/*  681 */         i4 = m >> 3;
/*      */ 
/*  683 */         i5 = k & 0x7;
/*  684 */         i6 = 8 - i5;
/*  685 */         i7 = 255 >> i1;
/*  686 */         if (n < i2)
/*      */         {
/*  688 */           i7 &= 255 << i2 - n;
/*  689 */           i2 = n;
/*      */         }
/*  691 */         i8 = arrayOfByte1.length - 1;
/*  692 */         for (i9 = 0; i9 < paramInt6; i9++)
/*      */         {
/*  696 */           i10 = arrayOfByte1[i3];
/*  697 */           i11 = 0;
/*  698 */           if (i3 < i8) {
/*  699 */             i11 = arrayOfByte1[(i3 + 1)];
/*      */           }
/*      */ 
/*  703 */           i12 = arrayOfByte2[i4];
/*  704 */           i12 &= (i7 ^ 0xFFFFFFFF);
/*  705 */           i12 |= (i10 << i5 | (i11 & 0xFF) >> i6) >> i1 & i7;
/*      */ 
/*  708 */           arrayOfByte2[i4] = ((byte)i12);
/*  709 */           i3 += i;
/*  710 */           i4 += j;
/*      */         }
/*      */ 
/*  713 */         k += i2;
/*  714 */         m += i2;
/*  715 */         n -= i2;
/*      */       }
/*      */ 
/*  723 */       if (n >= 8) {
/*  724 */         i2 = k >> 3;
/*  725 */         i3 = m >> 3;
/*  726 */         i4 = n >> 3;
/*  727 */         i5 = k & 0x7;
/*  728 */         i6 = 8 - i5;
/*      */ 
/*  730 */         for (i7 = 0; i7 < paramInt6; i7++) {
/*  731 */           i8 = i2 + i7 * i;
/*  732 */           i9 = i3 + i7 * j;
/*      */ 
/*  734 */           i10 = arrayOfByte1[i8];
/*      */ 
/*  736 */           for (i11 = 0; i11 < i4; i11++) {
/*  737 */             i12 = arrayOfByte1[(i8 + 1)];
/*  738 */             int i13 = i10 << i5 | (i12 & 0xFF) >> i6;
/*      */ 
/*  740 */             arrayOfByte2[i9] = ((byte)i13);
/*  741 */             i10 = i12;
/*      */ 
/*  743 */             i8++;
/*  744 */             i9++;
/*      */           }
/*      */         }
/*      */ 
/*  748 */         i7 = i4 * 8;
/*  749 */         k += i7;
/*  750 */         m += i7;
/*  751 */         n -= i7;
/*      */       }
/*      */ 
/*  755 */       if (n > 0) {
/*  756 */         i2 = k >> 3;
/*  757 */         i3 = m >> 3;
/*  758 */         i4 = 65280 >> n & 0xFF;
/*  759 */         i5 = k & 0x7;
/*  760 */         i6 = 8 - i5;
/*      */ 
/*  762 */         i7 = arrayOfByte1.length - 1;
/*  763 */         for (i8 = 0; i8 < paramInt6; i8++) {
/*  764 */           i9 = arrayOfByte1[i2];
/*  765 */           i10 = 0;
/*  766 */           if (i2 < i7) {
/*  767 */             i10 = arrayOfByte1[(i2 + 1)];
/*      */           }
/*      */ 
/*  771 */           i11 = arrayOfByte2[i3];
/*  772 */           i11 &= (i4 ^ 0xFFFFFFFF);
/*  773 */           i11 |= (i9 << i5 | (i10 & 0xFF) >> i6) & i4;
/*      */ 
/*  775 */           arrayOfByte2[i3] = ((byte)i11);
/*      */ 
/*  777 */           i2 += i;
/*  778 */           i3 += j;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  783 */     markDirty();
/*      */   }
/*      */ 
/*      */   public void setRect(int paramInt1, int paramInt2, Raster paramRaster)
/*      */   {
/*  804 */     if ((!(paramRaster instanceof BytePackedRaster)) || (((BytePackedRaster)paramRaster).pixelBitStride != this.pixelBitStride))
/*      */     {
/*  806 */       super.setRect(paramInt1, paramInt2, paramRaster);
/*  807 */       return;
/*      */     }
/*      */ 
/*  810 */     int i = paramRaster.getWidth();
/*  811 */     int j = paramRaster.getHeight();
/*  812 */     int k = paramRaster.getMinX();
/*  813 */     int m = paramRaster.getMinY();
/*  814 */     int n = paramInt1 + k;
/*  815 */     int i1 = paramInt2 + m;
/*      */     int i2;
/*  818 */     if (n < this.minX) {
/*  819 */       i2 = this.minX - n;
/*  820 */       i -= i2;
/*  821 */       k += i2;
/*  822 */       n = this.minX;
/*      */     }
/*  824 */     if (i1 < this.minY) {
/*  825 */       i2 = this.minY - i1;
/*  826 */       j -= i2;
/*  827 */       m += i2;
/*  828 */       i1 = this.minY;
/*      */     }
/*  830 */     if (n + i > this.maxX) {
/*  831 */       i = this.maxX - n;
/*      */     }
/*  833 */     if (i1 + j > this.maxY) {
/*  834 */       j = this.maxY - i1;
/*      */     }
/*      */ 
/*  837 */     setDataElements(n, i1, k, m, i, j, (BytePackedRaster)paramRaster);
/*      */   }
/*      */ 
/*      */   public void setDataElements(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject)
/*      */   {
/*  866 */     putByteData(paramInt1, paramInt2, paramInt3, paramInt4, (byte[])paramObject);
/*      */   }
/*      */ 
/*      */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*      */   {
/*  889 */     putByteData(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   public void putByteData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*      */   {
/*  910 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/*  912 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/*  915 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/*  916 */       return;
/*      */     }
/*      */ 
/*  919 */     int i = this.pixelBitStride;
/*  920 */     int j = this.dataBitOffset + (paramInt1 - this.minX) * i;
/*  921 */     int k = (paramInt2 - this.minY) * this.scanlineStride;
/*  922 */     int m = 0;
/*  923 */     byte[] arrayOfByte = this.data;
/*  924 */     for (int n = 0; n < paramInt4; n++) {
/*  925 */       int i1 = j;
/*      */ 
/*  929 */       int i3 = 0;
/*      */       int i2;
/*  930 */       while ((i3 < paramInt3) && ((i1 & 0x7) != 0)) {
/*  931 */         i4 = this.shiftOffset - (i1 & 0x7);
/*  932 */         i2 = arrayOfByte[(k + (i1 >> 3))];
/*  933 */         i2 &= (this.bitMask << i4 ^ 0xFFFFFFFF);
/*  934 */         i2 |= (paramArrayOfByte[(m++)] & this.bitMask) << i4;
/*  935 */         arrayOfByte[(k + (i1 >> 3))] = ((byte)i2);
/*      */ 
/*  937 */         i1 += i;
/*  938 */         i3++;
/*      */       }
/*      */ 
/*  942 */       int i4 = k + (i1 >> 3);
/*  943 */       switch (i) { case 1:
/*      */       case 2:
/*      */       case 4:
/*  945 */         while (i3 < paramInt3 - 7) {
/*  946 */           i2 = (paramArrayOfByte[(m++)] & 0x1) << 7;
/*  947 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 6;
/*  948 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 5;
/*  949 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 4;
/*  950 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 3;
/*  951 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 2;
/*  952 */           i2 |= (paramArrayOfByte[(m++)] & 0x1) << 1;
/*  953 */           i2 |= paramArrayOfByte[(m++)] & 0x1;
/*      */ 
/*  955 */           arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  957 */           i1 += 8;
/*      */ 
/*  945 */           i3 += 8; continue;
/*      */ 
/*  962 */           while (i3 < paramInt3 - 7) {
/*  963 */             i2 = (paramArrayOfByte[(m++)] & 0x3) << 6;
/*  964 */             i2 |= (paramArrayOfByte[(m++)] & 0x3) << 4;
/*  965 */             i2 |= (paramArrayOfByte[(m++)] & 0x3) << 2;
/*  966 */             i2 |= paramArrayOfByte[(m++)] & 0x3;
/*  967 */             arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  969 */             i2 = (paramArrayOfByte[(m++)] & 0x3) << 6;
/*  970 */             i2 |= (paramArrayOfByte[(m++)] & 0x3) << 4;
/*  971 */             i2 |= (paramArrayOfByte[(m++)] & 0x3) << 2;
/*  972 */             i2 |= paramArrayOfByte[(m++)] & 0x3;
/*  973 */             arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  975 */             i1 += 16;
/*      */ 
/*  962 */             i3 += 8; continue;
/*      */ 
/*  980 */             for (; i3 < paramInt3 - 7; i3 += 8) {
/*  981 */               i2 = (paramArrayOfByte[(m++)] & 0xF) << 4;
/*  982 */               i2 |= paramArrayOfByte[(m++)] & 0xF;
/*  983 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  985 */               i2 = (paramArrayOfByte[(m++)] & 0xF) << 4;
/*  986 */               i2 |= paramArrayOfByte[(m++)] & 0xF;
/*  987 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  989 */               i2 = (paramArrayOfByte[(m++)] & 0xF) << 4;
/*  990 */               i2 |= paramArrayOfByte[(m++)] & 0xF;
/*  991 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  993 */               i2 = (paramArrayOfByte[(m++)] & 0xF) << 4;
/*  994 */               i2 |= paramArrayOfByte[(m++)] & 0xF;
/*  995 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/*  997 */               i1 += 32;
/*      */             }
/*      */           }
/*      */         }
/*      */       case 3:
/*      */       }
/* 1003 */       for (; i3 < paramInt3; i3++) {
/* 1004 */         int i5 = this.shiftOffset - (i1 & 0x7);
/*      */ 
/* 1006 */         i2 = arrayOfByte[(k + (i1 >> 3))];
/* 1007 */         i2 &= (this.bitMask << i5 ^ 0xFFFFFFFF);
/* 1008 */         i2 |= (paramArrayOfByte[(m++)] & this.bitMask) << i5;
/* 1009 */         arrayOfByte[(k + (i1 >> 3))] = ((byte)i2);
/*      */ 
/* 1011 */         i1 += i;
/*      */       }
/*      */ 
/* 1014 */       k += this.scanlineStride;
/*      */     }
/*      */ 
/* 1017 */     markDirty();
/*      */   }
/*      */ 
/*      */   public int[] getPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*      */   {
/* 1032 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/* 1034 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1037 */     if (paramArrayOfInt == null) {
/* 1038 */       paramArrayOfInt = new int[paramInt3 * paramInt4];
/*      */     }
/* 1040 */     int i = this.pixelBitStride;
/* 1041 */     int j = this.dataBitOffset + (paramInt1 - this.minX) * i;
/* 1042 */     int k = (paramInt2 - this.minY) * this.scanlineStride;
/* 1043 */     int m = 0;
/* 1044 */     byte[] arrayOfByte = this.data;
/*      */ 
/* 1046 */     for (int n = 0; n < paramInt4; n++) {
/* 1047 */       int i1 = j;
/*      */ 
/* 1051 */       int i3 = 0;
/* 1052 */       while ((i3 < paramInt3) && ((i1 & 0x7) != 0)) {
/* 1053 */         i4 = this.shiftOffset - (i1 & 0x7);
/* 1054 */         paramArrayOfInt[(m++)] = (this.bitMask & arrayOfByte[(k + (i1 >> 3))] >> i4);
/*      */ 
/* 1056 */         i1 += i;
/* 1057 */         i3++;
/*      */       }
/*      */ 
/* 1061 */       int i4 = k + (i1 >> 3);
/* 1062 */       switch (i) { case 1:
/*      */       case 2:
/*      */       case 4:
/* 1064 */         while (i3 < paramInt3 - 7) {
/* 1065 */           int i2 = arrayOfByte[(i4++)];
/* 1066 */           paramArrayOfInt[(m++)] = (i2 >> 7 & 0x1);
/* 1067 */           paramArrayOfInt[(m++)] = (i2 >> 6 & 0x1);
/* 1068 */           paramArrayOfInt[(m++)] = (i2 >> 5 & 0x1);
/* 1069 */           paramArrayOfInt[(m++)] = (i2 >> 4 & 0x1);
/* 1070 */           paramArrayOfInt[(m++)] = (i2 >> 3 & 0x1);
/* 1071 */           paramArrayOfInt[(m++)] = (i2 >> 2 & 0x1);
/* 1072 */           paramArrayOfInt[(m++)] = (i2 >> 1 & 0x1);
/* 1073 */           paramArrayOfInt[(m++)] = (i2 & 0x1);
/* 1074 */           i1 += 8;
/*      */ 
/* 1064 */           i3 += 8; continue;
/*      */ 
/* 1079 */           while (i3 < paramInt3 - 7) {
/* 1080 */             i2 = arrayOfByte[(i4++)];
/* 1081 */             paramArrayOfInt[(m++)] = (i2 >> 6 & 0x3);
/* 1082 */             paramArrayOfInt[(m++)] = (i2 >> 4 & 0x3);
/* 1083 */             paramArrayOfInt[(m++)] = (i2 >> 2 & 0x3);
/* 1084 */             paramArrayOfInt[(m++)] = (i2 & 0x3);
/*      */ 
/* 1086 */             i2 = arrayOfByte[(i4++)];
/* 1087 */             paramArrayOfInt[(m++)] = (i2 >> 6 & 0x3);
/* 1088 */             paramArrayOfInt[(m++)] = (i2 >> 4 & 0x3);
/* 1089 */             paramArrayOfInt[(m++)] = (i2 >> 2 & 0x3);
/* 1090 */             paramArrayOfInt[(m++)] = (i2 & 0x3);
/*      */ 
/* 1092 */             i1 += 16;
/*      */ 
/* 1079 */             i3 += 8; continue;
/*      */ 
/* 1097 */             for (; i3 < paramInt3 - 7; i3 += 8) {
/* 1098 */               i2 = arrayOfByte[(i4++)];
/* 1099 */               paramArrayOfInt[(m++)] = (i2 >> 4 & 0xF);
/* 1100 */               paramArrayOfInt[(m++)] = (i2 & 0xF);
/*      */ 
/* 1102 */               i2 = arrayOfByte[(i4++)];
/* 1103 */               paramArrayOfInt[(m++)] = (i2 >> 4 & 0xF);
/* 1104 */               paramArrayOfInt[(m++)] = (i2 & 0xF);
/*      */ 
/* 1106 */               i2 = arrayOfByte[(i4++)];
/* 1107 */               paramArrayOfInt[(m++)] = (i2 >> 4 & 0xF);
/* 1108 */               paramArrayOfInt[(m++)] = (i2 & 0xF);
/*      */ 
/* 1110 */               i2 = arrayOfByte[(i4++)];
/* 1111 */               paramArrayOfInt[(m++)] = (i2 >> 4 & 0xF);
/* 1112 */               paramArrayOfInt[(m++)] = (i2 & 0xF);
/*      */ 
/* 1114 */               i1 += 32;
/*      */             }
/*      */           }
/*      */         }
/*      */       case 3:
/*      */       }
/* 1120 */       for (; i3 < paramInt3; i3++) {
/* 1121 */         int i5 = this.shiftOffset - (i1 & 0x7);
/* 1122 */         paramArrayOfInt[(m++)] = (this.bitMask & arrayOfByte[(k + (i1 >> 3))] >> i5);
/*      */ 
/* 1124 */         i1 += i;
/*      */       }
/*      */ 
/* 1127 */       k += this.scanlineStride;
/*      */     }
/*      */ 
/* 1130 */     return paramArrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*      */   {
/* 1145 */     if ((paramInt1 < this.minX) || (paramInt2 < this.minY) || (paramInt1 + paramInt3 > this.maxX) || (paramInt2 + paramInt4 > this.maxY))
/*      */     {
/* 1147 */       throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
/*      */     }
/*      */ 
/* 1150 */     int i = this.pixelBitStride;
/* 1151 */     int j = this.dataBitOffset + (paramInt1 - this.minX) * i;
/* 1152 */     int k = (paramInt2 - this.minY) * this.scanlineStride;
/* 1153 */     int m = 0;
/* 1154 */     byte[] arrayOfByte = this.data;
/* 1155 */     for (int n = 0; n < paramInt4; n++) {
/* 1156 */       int i1 = j;
/*      */ 
/* 1160 */       int i3 = 0;
/*      */       int i2;
/* 1161 */       while ((i3 < paramInt3) && ((i1 & 0x7) != 0)) {
/* 1162 */         i4 = this.shiftOffset - (i1 & 0x7);
/* 1163 */         i2 = arrayOfByte[(k + (i1 >> 3))];
/* 1164 */         i2 &= (this.bitMask << i4 ^ 0xFFFFFFFF);
/* 1165 */         i2 |= (paramArrayOfInt[(m++)] & this.bitMask) << i4;
/* 1166 */         arrayOfByte[(k + (i1 >> 3))] = ((byte)i2);
/*      */ 
/* 1168 */         i1 += i;
/* 1169 */         i3++;
/*      */       }
/*      */ 
/* 1173 */       int i4 = k + (i1 >> 3);
/* 1174 */       switch (i) { case 1:
/*      */       case 2:
/*      */       case 4:
/* 1176 */         while (i3 < paramInt3 - 7) {
/* 1177 */           i2 = (paramArrayOfInt[(m++)] & 0x1) << 7;
/* 1178 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 6;
/* 1179 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 5;
/* 1180 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 4;
/* 1181 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 3;
/* 1182 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 2;
/* 1183 */           i2 |= (paramArrayOfInt[(m++)] & 0x1) << 1;
/* 1184 */           i2 |= paramArrayOfInt[(m++)] & 0x1;
/* 1185 */           arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1187 */           i1 += 8;
/*      */ 
/* 1176 */           i3 += 8; continue;
/*      */ 
/* 1192 */           while (i3 < paramInt3 - 7) {
/* 1193 */             i2 = (paramArrayOfInt[(m++)] & 0x3) << 6;
/* 1194 */             i2 |= (paramArrayOfInt[(m++)] & 0x3) << 4;
/* 1195 */             i2 |= (paramArrayOfInt[(m++)] & 0x3) << 2;
/* 1196 */             i2 |= paramArrayOfInt[(m++)] & 0x3;
/* 1197 */             arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1199 */             i2 = (paramArrayOfInt[(m++)] & 0x3) << 6;
/* 1200 */             i2 |= (paramArrayOfInt[(m++)] & 0x3) << 4;
/* 1201 */             i2 |= (paramArrayOfInt[(m++)] & 0x3) << 2;
/* 1202 */             i2 |= paramArrayOfInt[(m++)] & 0x3;
/* 1203 */             arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1205 */             i1 += 16;
/*      */ 
/* 1192 */             i3 += 8; continue;
/*      */ 
/* 1210 */             for (; i3 < paramInt3 - 7; i3 += 8) {
/* 1211 */               i2 = (paramArrayOfInt[(m++)] & 0xF) << 4;
/* 1212 */               i2 |= paramArrayOfInt[(m++)] & 0xF;
/* 1213 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1215 */               i2 = (paramArrayOfInt[(m++)] & 0xF) << 4;
/* 1216 */               i2 |= paramArrayOfInt[(m++)] & 0xF;
/* 1217 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1219 */               i2 = (paramArrayOfInt[(m++)] & 0xF) << 4;
/* 1220 */               i2 |= paramArrayOfInt[(m++)] & 0xF;
/* 1221 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1223 */               i2 = (paramArrayOfInt[(m++)] & 0xF) << 4;
/* 1224 */               i2 |= paramArrayOfInt[(m++)] & 0xF;
/* 1225 */               arrayOfByte[(i4++)] = ((byte)i2);
/*      */ 
/* 1227 */               i1 += 32;
/*      */             }
/*      */           }
/*      */         }
/*      */       case 3:
/*      */       }
/* 1233 */       for (; i3 < paramInt3; i3++) {
/* 1234 */         int i5 = this.shiftOffset - (i1 & 0x7);
/*      */ 
/* 1236 */         i2 = arrayOfByte[(k + (i1 >> 3))];
/* 1237 */         i2 &= (this.bitMask << i5 ^ 0xFFFFFFFF);
/* 1238 */         i2 |= (paramArrayOfInt[(m++)] & this.bitMask) << i5;
/* 1239 */         arrayOfByte[(k + (i1 >> 3))] = ((byte)i2);
/*      */ 
/* 1241 */         i1 += i;
/*      */       }
/*      */ 
/* 1244 */       k += this.scanlineStride;
/*      */     }
/*      */ 
/* 1247 */     markDirty();
/*      */   }
/*      */ 
/*      */   public Raster createChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 1270 */     WritableRaster localWritableRaster = createWritableChild(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfInt);
/*      */ 
/* 1274 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   public WritableRaster createWritableChild(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfInt)
/*      */   {
/* 1299 */     if (paramInt1 < this.minX) {
/* 1300 */       throw new RasterFormatException("x lies outside the raster");
/*      */     }
/* 1302 */     if (paramInt2 < this.minY) {
/* 1303 */       throw new RasterFormatException("y lies outside the raster");
/*      */     }
/* 1305 */     if ((paramInt1 + paramInt3 < paramInt1) || (paramInt1 + paramInt3 > this.minX + this.width)) {
/* 1306 */       throw new RasterFormatException("(x + width) is outside of Raster");
/*      */     }
/* 1308 */     if ((paramInt2 + paramInt4 < paramInt2) || (paramInt2 + paramInt4 > this.minY + this.height))
/* 1309 */       throw new RasterFormatException("(y + height) is outside of Raster");
/*      */     SampleModel localSampleModel;
/* 1314 */     if (paramArrayOfInt != null) {
/* 1315 */       localSampleModel = this.sampleModel.createSubsetSampleModel(paramArrayOfInt);
/*      */     }
/*      */     else {
/* 1318 */       localSampleModel = this.sampleModel;
/*      */     }
/*      */ 
/* 1321 */     int i = paramInt5 - paramInt1;
/* 1322 */     int j = paramInt6 - paramInt2;
/*      */ 
/* 1324 */     return new BytePackedRaster(localSampleModel, this.dataBuffer, new Rectangle(paramInt5, paramInt6, paramInt3, paramInt4), new Point(this.sampleModelTranslateX + i, this.sampleModelTranslateY + j), this);
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster(int paramInt1, int paramInt2)
/*      */   {
/* 1337 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/* 1338 */       throw new RasterFormatException("negative " + (paramInt1 <= 0 ? "width" : "height"));
/*      */     }
/*      */ 
/* 1342 */     SampleModel localSampleModel = this.sampleModel.createCompatibleSampleModel(paramInt1, paramInt2);
/*      */ 
/* 1344 */     return new BytePackedRaster(localSampleModel, new Point(0, 0));
/*      */   }
/*      */ 
/*      */   public WritableRaster createCompatibleWritableRaster()
/*      */   {
/* 1352 */     return createCompatibleWritableRaster(this.width, this.height);
/*      */   }
/*      */ 
/*      */   private void verify(boolean paramBoolean)
/*      */   {
/* 1367 */     if (this.dataBitOffset < 0) {
/* 1368 */       throw new RasterFormatException("Data offsets must be >= 0");
/*      */     }
/*      */ 
/* 1374 */     if ((this.width <= 0) || (this.height <= 0) || (this.height > 2147483647 / this.width))
/*      */     {
/* 1377 */       throw new RasterFormatException("Invalid raster dimension");
/*      */     }
/*      */ 
/* 1385 */     if (this.width - 1 > 2147483647 / this.pixelBitStride) {
/* 1386 */       throw new RasterFormatException("Invalid raster dimension");
/*      */     }
/*      */ 
/* 1389 */     if ((this.minX - this.sampleModelTranslateX < 0L) || (this.minY - this.sampleModelTranslateY < 0L))
/*      */     {
/* 1392 */       throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
/*      */     }
/*      */ 
/* 1397 */     if ((this.scanlineStride < 0) || (this.scanlineStride > 2147483647 / this.height))
/*      */     {
/* 1400 */       throw new RasterFormatException("Invalid scanline stride");
/*      */     }
/*      */ 
/* 1403 */     if ((this.height > 1) || (this.minY - this.sampleModelTranslateY > 0))
/*      */     {
/* 1405 */       if (this.scanlineStride > this.data.length) {
/* 1406 */         throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1411 */     long l = this.dataBitOffset + (this.height - 1) * this.scanlineStride * 8L + (this.width - 1) * this.pixelBitStride + this.pixelBitStride - 1L;
/*      */ 
/* 1415 */     if ((l < 0L) || (l / 8L >= this.data.length)) {
/* 1416 */       throw new RasterFormatException("raster dimensions overflow array bounds");
/*      */     }
/*      */ 
/* 1419 */     if ((paramBoolean) && 
/* 1420 */       (this.height > 1)) {
/* 1421 */       l = this.width * this.pixelBitStride - 1;
/* 1422 */       if (l / 8L >= this.scanlineStride)
/* 1423 */         throw new RasterFormatException("data for adjacent scanlines overlaps");
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1431 */     return new String("BytePackedRaster: width = " + this.width + " height = " + this.height + " #channels " + this.numBands + " xOff = " + this.sampleModelTranslateX + " yOff = " + this.sampleModelTranslateY);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   79 */     NativeLibLoader.loadLibraries();
/*   80 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.BytePackedRaster
 * JD-Core Version:    0.6.2
 */