/*     */ package sun.java2d.cmm.kcms;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ComponentSampleModel;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DataBufferUShort;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.awt.image.ByteComponentRaster;
/*     */ import sun.awt.image.IntegerComponentRaster;
/*     */ import sun.awt.image.ShortComponentRaster;
/*     */ 
/*     */ class CMMImageLayout
/*     */ {
/*     */   private static final int typeBase = 256;
/*     */   public static final int typeComponentUByte = 256;
/*     */   public static final int typeComponentUShort12 = 257;
/*     */   public static final int typeComponentUShort = 258;
/*     */   public static final int typePixelUByte = 259;
/*     */   public static final int typePixelUShort12 = 260;
/*     */   public static final int typePixelUShort = 261;
/*     */   public static final int typeShort555 = 262;
/*     */   public static final int typeShort565 = 263;
/*     */   public static final int typeInt101010 = 264;
/*     */   public static final int typeIntRGBPacked = 265;
/*     */   public int Type;
/*     */   public int NumCols;
/*     */   public int NumRows;
/*     */   public int OffsetColumn;
/*     */   public int OffsetRow;
/*     */   public int NumChannels;
/*     */   public final boolean hasAlpha;
/*     */   public Object[] chanData;
/*     */   public int[] DataOffsets;
/*     */   public int[] sampleInfo;
/*     */   private int[] dataArrayLength;
/*     */   private static final int MAX_NumChannels = 9;
/*     */ 
/*     */   public CMMImageLayout(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 114 */     this.Type = 256;
/*     */ 
/* 116 */     this.chanData = new Object[paramInt2];
/* 117 */     this.DataOffsets = new int[paramInt2];
/* 118 */     this.dataArrayLength = new int[paramInt2];
/*     */ 
/* 120 */     this.NumCols = paramInt1;
/* 121 */     this.NumRows = 1;
/* 122 */     this.OffsetColumn = paramInt2;
/* 123 */     this.OffsetRow = (this.NumCols * this.OffsetColumn);
/* 124 */     this.NumChannels = paramInt2;
/* 125 */     for (int i = 0; i < paramInt2; i++) {
/* 126 */       this.chanData[i] = paramArrayOfByte;
/* 127 */       this.DataOffsets[i] = i;
/* 128 */       this.dataArrayLength[i] = paramArrayOfByte.length;
/*     */     }
/* 130 */     this.hasAlpha = false;
/* 131 */     verify();
/*     */   }
/*     */ 
/*     */   public CMMImageLayout(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 142 */     this.Type = 258;
/*     */ 
/* 144 */     this.chanData = new Object[paramInt2];
/* 145 */     this.DataOffsets = new int[paramInt2];
/* 146 */     this.dataArrayLength = new int[paramInt2];
/*     */ 
/* 148 */     this.NumCols = paramInt1;
/* 149 */     this.NumRows = 1;
/*     */ 
/* 151 */     this.OffsetColumn = safeMult(2, paramInt2);
/* 152 */     this.OffsetRow = (this.NumCols * this.OffsetColumn);
/* 153 */     this.NumChannels = paramInt2;
/* 154 */     for (int i = 0; i < paramInt2; i++) {
/* 155 */       this.chanData[i] = paramArrayOfShort;
/* 156 */       this.DataOffsets[i] = (i * 2);
/* 157 */       this.dataArrayLength[i] = (2 * paramArrayOfShort.length);
/*     */     }
/* 159 */     this.hasAlpha = false;
/* 160 */     verify();
/*     */   }
/*     */ 
/*     */   public CMMImageLayout(BufferedImage paramBufferedImage)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 175 */     this.Type = paramBufferedImage.getType();
/* 176 */     this.NumCols = paramBufferedImage.getWidth();
/* 177 */     this.NumRows = paramBufferedImage.getHeight();
/*     */ 
/* 179 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/*     */     int i;
/*     */     Object localObject1;
/*     */     int k;
/*     */     int j;
/*     */     Object localObject2;
/*     */     int m;
/* 183 */     switch (this.Type)
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 4:
/* 188 */       this.NumChannels = 3;
/*     */ 
/* 190 */       this.hasAlpha = (this.Type == 2);
/* 191 */       i = this.hasAlpha ? 4 : 3;
/*     */ 
/* 193 */       this.chanData = new Object[i];
/* 194 */       this.DataOffsets = new int[i];
/* 195 */       this.dataArrayLength = new int[i];
/* 196 */       this.sampleInfo = new int[i];
/*     */ 
/* 200 */       this.OffsetColumn = 4;
/*     */ 
/* 202 */       if ((localWritableRaster instanceof IntegerComponentRaster)) {
/* 203 */         localObject1 = (IntegerComponentRaster)localWritableRaster;
/*     */ 
/* 205 */         k = safeMult(4, ((IntegerComponentRaster)localObject1).getPixelStride());
/* 206 */         if (k != this.OffsetColumn) {
/* 207 */           throw new ImageLayoutException("Incompatible raster type");
/*     */         }
/*     */ 
/* 210 */         this.OffsetRow = safeMult(4, ((IntegerComponentRaster)localObject1).getScanlineStride());
/*     */ 
/* 213 */         j = safeMult(4, ((IntegerComponentRaster)localObject1).getDataOffset(0));
/*     */ 
/* 215 */         localObject2 = ((IntegerComponentRaster)localObject1).getDataStorage();
/*     */ 
/* 217 */         for (m = 0; m < 3; m++) {
/* 218 */           this.chanData[m] = localObject2;
/* 219 */           this.DataOffsets[m] = j;
/* 220 */           this.dataArrayLength[m] = (4 * localObject2.length);
/* 221 */           if (this.Type == 4)
/* 222 */             this.sampleInfo[m] = (3 - m);
/*     */           else {
/* 224 */             this.sampleInfo[m] = (m + 1);
/*     */           }
/*     */         }
/* 227 */         if (this.hasAlpha) {
/* 228 */           this.chanData[3] = localObject2;
/* 229 */           this.DataOffsets[3] = j;
/* 230 */           this.dataArrayLength[3] = (4 * localObject2.length);
/* 231 */           this.sampleInfo[3] = 0;
/*     */         }
/*     */       } else {
/* 234 */         throw new ImageLayoutException("Incompatible raster type");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/*     */     case 6:
/* 241 */       this.NumChannels = 3;
/*     */ 
/* 243 */       this.hasAlpha = (this.Type == 6);
/* 244 */       if (this.hasAlpha) {
/* 245 */         this.OffsetColumn = 4;
/* 246 */         i = 4;
/*     */       } else {
/* 248 */         this.OffsetColumn = 3;
/* 249 */         i = 3;
/*     */       }
/* 251 */       this.chanData = new Object[i];
/* 252 */       this.DataOffsets = new int[i];
/* 253 */       this.dataArrayLength = new int[i];
/*     */ 
/* 255 */       if ((localWritableRaster instanceof ByteComponentRaster)) {
/* 256 */         localObject1 = (ByteComponentRaster)localWritableRaster;
/* 257 */         k = ((ByteComponentRaster)localObject1).getPixelStride();
/* 258 */         if (k != this.OffsetColumn) {
/* 259 */           throw new ImageLayoutException("Incompatible raster type");
/*     */         }
/*     */ 
/* 262 */         this.OffsetRow = ((ByteComponentRaster)localObject1).getScanlineStride();
/* 263 */         j = ((ByteComponentRaster)localObject1).getDataOffset(0);
/* 264 */         localObject2 = ((ByteComponentRaster)localObject1).getDataStorage();
/* 265 */         for (m = 0; m < i; m++) {
/* 266 */           this.chanData[m] = localObject2;
/* 267 */           this.DataOffsets[m] = (j - m);
/* 268 */           this.dataArrayLength[m] = localObject2.length;
/*     */         }
/*     */       } else {
/* 271 */         throw new ImageLayoutException("Incompatible raster type");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 10:
/* 278 */       this.Type = 256;
/*     */ 
/* 280 */       this.NumChannels = 1;
/* 281 */       this.hasAlpha = false;
/* 282 */       this.chanData = new Object[1];
/* 283 */       this.DataOffsets = new int[1];
/* 284 */       this.dataArrayLength = new int[1];
/*     */ 
/* 286 */       this.OffsetColumn = 1;
/*     */ 
/* 288 */       if ((localWritableRaster instanceof ByteComponentRaster)) {
/* 289 */         localObject1 = (ByteComponentRaster)localWritableRaster;
/* 290 */         k = ((ByteComponentRaster)localObject1).getPixelStride();
/* 291 */         if (k != this.OffsetColumn) {
/* 292 */           throw new ImageLayoutException("Incompatible raster type");
/*     */         }
/*     */ 
/* 295 */         this.OffsetRow = ((ByteComponentRaster)localObject1).getScanlineStride();
/* 296 */         localObject2 = ((ByteComponentRaster)localObject1).getDataStorage();
/* 297 */         this.chanData[0] = localObject2;
/* 298 */         this.dataArrayLength[0] = localObject2.length;
/* 299 */         this.DataOffsets[0] = ((ByteComponentRaster)localObject1).getDataOffset(0);
/*     */       } else {
/* 301 */         throw new ImageLayoutException("Incompatible raster type");
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 11:
/* 307 */       this.Type = 258;
/*     */ 
/* 309 */       this.NumChannels = 1;
/* 310 */       this.hasAlpha = false;
/* 311 */       this.chanData = new Object[1];
/* 312 */       this.DataOffsets = new int[1];
/* 313 */       this.dataArrayLength = new int[1];
/*     */ 
/* 315 */       this.OffsetColumn = 2;
/*     */ 
/* 317 */       if ((localWritableRaster instanceof ShortComponentRaster)) {
/* 318 */         localObject1 = (ShortComponentRaster)localWritableRaster;
/* 319 */         k = safeMult(2, ((ShortComponentRaster)localObject1).getPixelStride());
/* 320 */         if (k != this.OffsetColumn) {
/* 321 */           throw new ImageLayoutException("Incompatible raster type");
/*     */         }
/*     */ 
/* 324 */         this.OffsetRow = safeMult(2, ((ShortComponentRaster)localObject1).getScanlineStride());
/*     */ 
/* 326 */         this.DataOffsets[0] = safeMult(2, ((ShortComponentRaster)localObject1).getDataOffset(0));
/*     */ 
/* 328 */         localObject2 = ((ShortComponentRaster)localObject1).getDataStorage();
/* 329 */         this.chanData[0] = localObject2;
/* 330 */         this.dataArrayLength[0] = (2 * localObject2.length);
/*     */       } else {
/* 332 */         throw new ImageLayoutException("Incompatible raster type"); } break;
/*     */     case 3:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     default:
/* 338 */       throw new IllegalArgumentException("CMMImageLayout - bad image type passed to constructor");
/*     */     }
/*     */ 
/* 341 */     verify();
/*     */   }
/*     */ 
/*     */   public CMMImageLayout(BufferedImage paramBufferedImage, SinglePixelPackedSampleModel paramSinglePixelPackedSampleModel, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 354 */     this.Type = 265;
/*     */ 
/* 356 */     this.NumChannels = 3;
/* 357 */     this.NumCols = paramBufferedImage.getWidth();
/* 358 */     this.NumRows = paramBufferedImage.getHeight();
/*     */ 
/* 360 */     this.hasAlpha = (paramInt4 >= 0);
/* 361 */     int i = this.hasAlpha ? 4 : 3;
/*     */ 
/* 363 */     this.chanData = new Object[i];
/* 364 */     this.DataOffsets = new int[i];
/* 365 */     this.dataArrayLength = new int[i];
/* 366 */     this.sampleInfo = new int[i];
/*     */ 
/* 370 */     this.OffsetColumn = 4;
/*     */ 
/* 372 */     int j = paramSinglePixelPackedSampleModel.getScanlineStride();
/*     */ 
/* 374 */     this.OffsetRow = safeMult(4, j);
/*     */ 
/* 376 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 377 */     DataBufferInt localDataBufferInt = (DataBufferInt)localWritableRaster.getDataBuffer();
/*     */ 
/* 380 */     int k = localWritableRaster.getSampleModelTranslateX();
/* 381 */     int m = localWritableRaster.getSampleModelTranslateY();
/*     */ 
/* 383 */     int n = safeMult(m, j);
/*     */ 
/* 385 */     int i1 = safeMult(4, k);
/*     */ 
/* 387 */     i1 = safeAdd(i1, n);
/*     */ 
/* 389 */     int i2 = safeAdd(localDataBufferInt.getOffset(), -i1);
/*     */ 
/* 391 */     int[] arrayOfInt = localDataBufferInt.getData();
/*     */ 
/* 393 */     for (int i3 = 0; i3 < i; i3++) {
/* 394 */       this.chanData[i3] = arrayOfInt;
/* 395 */       this.DataOffsets[i3] = i2;
/* 396 */       this.dataArrayLength[i3] = (arrayOfInt.length * 4);
/*     */     }
/* 398 */     this.sampleInfo[0] = paramInt1;
/* 399 */     this.sampleInfo[1] = paramInt2;
/* 400 */     this.sampleInfo[2] = paramInt3;
/* 401 */     if (this.hasAlpha) {
/* 402 */       this.sampleInfo[3] = paramInt4;
/*     */     }
/* 404 */     verify();
/*     */   }
/*     */ 
/*     */   public CMMImageLayout(BufferedImage paramBufferedImage, ComponentSampleModel paramComponentSampleModel)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 416 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 417 */     int i = localColorModel.getNumColorComponents();
/*     */ 
/* 419 */     if ((i < 0) || (i > 9)) {
/* 420 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/*     */ 
/* 423 */     this.hasAlpha = localColorModel.hasAlpha();
/* 424 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 425 */     int[] arrayOfInt1 = paramComponentSampleModel.getBankIndices();
/* 426 */     int[] arrayOfInt2 = paramComponentSampleModel.getBandOffsets();
/* 427 */     this.NumChannels = i;
/* 428 */     this.NumCols = paramBufferedImage.getWidth();
/* 429 */     this.NumRows = paramBufferedImage.getHeight();
/*     */ 
/* 431 */     if (this.hasAlpha) {
/* 432 */       i++;
/*     */     }
/* 434 */     this.chanData = new Object[i];
/* 435 */     this.DataOffsets = new int[i];
/* 436 */     this.dataArrayLength = new int[i];
/*     */ 
/* 439 */     int j = localWritableRaster.getSampleModelTranslateY();
/* 440 */     int k = localWritableRaster.getSampleModelTranslateX();
/* 441 */     int m = paramComponentSampleModel.getScanlineStride();
/* 442 */     int n = paramComponentSampleModel.getPixelStride();
/*     */ 
/* 444 */     int i1 = safeMult(m, j);
/*     */ 
/* 446 */     int i2 = safeMult(n, k);
/*     */ 
/* 448 */     i2 = safeAdd(i2, i1);
/*     */     Object localObject1;
/*     */     int[] arrayOfInt3;
/*     */     int i3;
/*     */     Object localObject2;
/*     */     int i4;
/* 450 */     switch (paramComponentSampleModel.getDataType())
/*     */     {
/*     */     case 0:
/* 453 */       this.Type = 256;
/* 454 */       this.OffsetColumn = n;
/* 455 */       this.OffsetRow = m;
/* 456 */       localObject1 = (DataBufferByte)localWritableRaster.getDataBuffer();
/* 457 */       arrayOfInt3 = ((DataBufferByte)localObject1).getOffsets();
/*     */ 
/* 459 */       for (i3 = 0; i3 < i; i3++) {
/* 460 */         localObject2 = ((DataBufferByte)localObject1).getData(arrayOfInt1[i3]);
/* 461 */         this.chanData[i3] = localObject2;
/* 462 */         this.dataArrayLength[i3] = localObject2.length;
/*     */ 
/* 464 */         i4 = safeAdd(arrayOfInt3[arrayOfInt1[i3]], -i2);
/*     */ 
/* 466 */         i4 = safeAdd(i4, arrayOfInt2[i3]);
/*     */ 
/* 468 */         this.DataOffsets[i3] = i4;
/*     */       }
/*     */ 
/* 471 */       break;
/*     */     case 1:
/* 475 */       this.Type = 258;
/*     */ 
/* 477 */       this.OffsetColumn = safeMult(2, n);
/*     */ 
/* 479 */       this.OffsetRow = safeMult(2, m);
/*     */ 
/* 481 */       localObject1 = (DataBufferUShort)localWritableRaster.getDataBuffer();
/*     */ 
/* 483 */       arrayOfInt3 = ((DataBufferUShort)localObject1).getOffsets();
/*     */ 
/* 485 */       for (i3 = 0; i3 < i; i3++) {
/* 486 */         localObject2 = ((DataBufferUShort)localObject1).getData(arrayOfInt1[i3]);
/* 487 */         this.chanData[i3] = localObject2;
/* 488 */         this.dataArrayLength[i3] = (localObject2.length * 2);
/*     */ 
/* 490 */         i4 = safeAdd(arrayOfInt3[arrayOfInt1[i3]], -i2);
/*     */ 
/* 492 */         i4 = safeAdd(i4, arrayOfInt2[i3]);
/*     */ 
/* 494 */         this.DataOffsets[i3] = safeMult(2, i4);
/*     */       }
/*     */ 
/* 497 */       break;
/*     */     default:
/* 501 */       throw new IllegalArgumentException("CMMImageLayout - bad image type passed to constructor");
/*     */     }
/*     */ 
/* 504 */     verify();
/*     */   }
/*     */ 
/*     */   public CMMImageLayout(Raster paramRaster, ComponentSampleModel paramComponentSampleModel)
/*     */     throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 515 */     int i = paramRaster.getNumBands();
/*     */ 
/* 517 */     if ((i < 0) || (i > 9)) {
/* 518 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/*     */ 
/* 521 */     int[] arrayOfInt1 = paramComponentSampleModel.getBankIndices();
/* 522 */     int[] arrayOfInt2 = paramComponentSampleModel.getBandOffsets();
/* 523 */     this.NumChannels = i;
/* 524 */     this.NumCols = paramRaster.getWidth();
/* 525 */     this.NumRows = paramRaster.getHeight();
/*     */ 
/* 527 */     this.hasAlpha = false;
/*     */ 
/* 529 */     this.chanData = new Object[i];
/* 530 */     this.DataOffsets = new int[i];
/* 531 */     this.dataArrayLength = new int[i];
/*     */ 
/* 533 */     int j = paramComponentSampleModel.getScanlineStride();
/* 534 */     int k = paramComponentSampleModel.getPixelStride();
/*     */ 
/* 537 */     int m = paramRaster.getMinX();
/* 538 */     int n = paramRaster.getMinY();
/*     */ 
/* 540 */     int i1 = paramRaster.getSampleModelTranslateX();
/* 541 */     int i2 = paramRaster.getSampleModelTranslateY();
/*     */ 
/* 543 */     int i3 = safeAdd(n, -i2);
/* 544 */     i3 = safeMult(i3, j);
/*     */ 
/* 546 */     int i4 = safeAdd(m, -i1);
/* 547 */     i4 = safeMult(i4, k);
/*     */ 
/* 549 */     i4 = safeAdd(i4, i3);
/*     */     Object localObject1;
/*     */     int[] arrayOfInt3;
/*     */     int i5;
/*     */     Object localObject2;
/*     */     int i6;
/* 551 */     switch (paramComponentSampleModel.getDataType())
/*     */     {
/*     */     case 0:
/* 554 */       this.Type = 256;
/* 555 */       this.OffsetColumn = k;
/* 556 */       this.OffsetRow = j;
/*     */ 
/* 558 */       localObject1 = (DataBufferByte)paramRaster.getDataBuffer();
/* 559 */       arrayOfInt3 = ((DataBufferByte)localObject1).getOffsets();
/* 560 */       for (i5 = 0; i5 < i; i5++) {
/* 561 */         localObject2 = ((DataBufferByte)localObject1).getData(arrayOfInt1[i5]);
/* 562 */         this.chanData[i5] = localObject2;
/* 563 */         this.dataArrayLength[i5] = localObject2.length;
/*     */ 
/* 565 */         i6 = safeAdd(arrayOfInt3[arrayOfInt1[i5]], i4);
/*     */ 
/* 567 */         this.DataOffsets[i5] = safeAdd(i6, arrayOfInt2[i5]);
/*     */       }
/*     */ 
/* 571 */       break;
/*     */     case 1:
/* 575 */       this.Type = 258;
/* 576 */       this.OffsetColumn = safeMult(2, k);
/*     */ 
/* 578 */       this.OffsetRow = safeMult(2, j);
/*     */ 
/* 580 */       localObject1 = (DataBufferUShort)paramRaster.getDataBuffer();
/*     */ 
/* 582 */       arrayOfInt3 = ((DataBufferUShort)localObject1).getOffsets();
/* 583 */       for (i5 = 0; i5 < i; i5++) {
/* 584 */         localObject2 = ((DataBufferUShort)localObject1).getData(arrayOfInt1[i5]);
/* 585 */         this.chanData[i5] = localObject2;
/* 586 */         this.dataArrayLength[i5] = (localObject2.length * 2);
/*     */ 
/* 589 */         i6 = safeAdd(arrayOfInt3[arrayOfInt1[i5]], i4);
/*     */ 
/* 591 */         i6 = safeAdd(i6, arrayOfInt2[i5]);
/*     */ 
/* 593 */         this.DataOffsets[i5] = safeMult(2, i6);
/*     */       }
/*     */ 
/* 597 */       break;
/*     */     default:
/* 601 */       throw new IllegalArgumentException("CMMImageLayout - bad image type passed to constructor");
/*     */     }
/*     */ 
/* 604 */     verify();
/*     */   }
/*     */ 
/*     */   private final void verify() throws CMMImageLayout.ImageLayoutException {
/* 608 */     int i = safeMult(this.OffsetRow, this.NumRows - 1);
/*     */ 
/* 610 */     int j = safeMult(this.OffsetColumn, this.NumCols - 1);
/*     */ 
/* 612 */     i = safeAdd(i, j);
/*     */ 
/* 614 */     int k = this.NumChannels;
/*     */ 
/* 616 */     if (this.hasAlpha) {
/* 617 */       k++;
/*     */     }
/*     */ 
/* 620 */     for (int m = 0; m < k; m++) {
/* 621 */       int n = this.DataOffsets[m];
/*     */ 
/* 623 */       if ((n < 0) || (n >= this.dataArrayLength[m])) {
/* 624 */         throw new ImageLayoutException("Invalid image layout");
/*     */       }
/* 626 */       n = safeAdd(n, i);
/*     */ 
/* 628 */       if ((n < 0) || (n >= this.dataArrayLength[m]))
/* 629 */         throw new ImageLayoutException("Invalid image layout");
/*     */     }
/*     */   }
/*     */ 
/*     */   static int safeAdd(int paramInt1, int paramInt2) throws CMMImageLayout.ImageLayoutException
/*     */   {
/* 635 */     long l = paramInt1;
/* 636 */     l += paramInt2;
/* 637 */     if ((l < -2147483648L) || (l > 2147483647L)) {
/* 638 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/* 640 */     return (int)l;
/*     */   }
/*     */ 
/*     */   static int safeMult(int paramInt1, int paramInt2) throws CMMImageLayout.ImageLayoutException {
/* 644 */     long l = paramInt1;
/* 645 */     l *= paramInt2;
/* 646 */     if ((l < -2147483648L) || (l > 2147483647L)) {
/* 647 */       throw new ImageLayoutException("Invalid image layout");
/*     */     }
/* 649 */     return (int)l;
/*     */   }
/*     */ 
/*     */   public static class ImageLayoutException extends Exception
/*     */   {
/*     */     public ImageLayoutException(String paramString) {
/* 655 */       super();
/*     */     }
/*     */ 
/*     */     public ImageLayoutException(String paramString, Throwable paramThrowable) {
/* 659 */       super(paramThrowable);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.kcms.CMMImageLayout
 * JD-Core Version:    0.6.2
 */