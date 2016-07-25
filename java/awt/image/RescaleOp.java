/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import sun.awt.image.ImagingLib;
/*     */ 
/*     */ public class RescaleOp
/*     */   implements BufferedImageOp, RasterOp
/*     */ {
/*     */   float[] scaleFactors;
/*     */   float[] offsets;
/*  86 */   int length = 0;
/*     */   RenderingHints hints;
/*     */   private int srcNbits;
/*     */   private int dstNbits;
/*     */ 
/*     */   public RescaleOp(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, RenderingHints paramRenderingHints)
/*     */   {
/* 105 */     this.length = paramArrayOfFloat1.length;
/* 106 */     if (this.length > paramArrayOfFloat2.length) this.length = paramArrayOfFloat2.length;
/*     */ 
/* 108 */     this.scaleFactors = new float[this.length];
/* 109 */     this.offsets = new float[this.length];
/* 110 */     for (int i = 0; i < this.length; i++) {
/* 111 */       this.scaleFactors[i] = paramArrayOfFloat1[i];
/* 112 */       this.offsets[i] = paramArrayOfFloat2[i];
/*     */     }
/* 114 */     this.hints = paramRenderingHints;
/*     */   }
/*     */ 
/*     */   public RescaleOp(float paramFloat1, float paramFloat2, RenderingHints paramRenderingHints)
/*     */   {
/* 129 */     this.length = 1;
/* 130 */     this.scaleFactors = new float[1];
/* 131 */     this.offsets = new float[1];
/* 132 */     this.scaleFactors[0] = paramFloat1;
/* 133 */     this.offsets[0] = paramFloat2;
/* 134 */     this.hints = paramRenderingHints;
/*     */   }
/*     */ 
/*     */   public final float[] getScaleFactors(float[] paramArrayOfFloat)
/*     */   {
/* 146 */     if (paramArrayOfFloat == null) {
/* 147 */       return (float[])this.scaleFactors.clone();
/*     */     }
/* 149 */     System.arraycopy(this.scaleFactors, 0, paramArrayOfFloat, 0, Math.min(this.scaleFactors.length, paramArrayOfFloat.length));
/*     */ 
/* 152 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public final float[] getOffsets(float[] paramArrayOfFloat)
/*     */   {
/* 164 */     if (paramArrayOfFloat == null) {
/* 165 */       return (float[])this.offsets.clone();
/*     */     }
/*     */ 
/* 168 */     System.arraycopy(this.offsets, 0, paramArrayOfFloat, 0, Math.min(this.offsets.length, paramArrayOfFloat.length));
/*     */ 
/* 170 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public final int getNumFactors()
/*     */   {
/* 180 */     return this.length;
/*     */   }
/*     */ 
/*     */   private ByteLookupTable createByteLut(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt1, int paramInt2)
/*     */   {
/* 196 */     byte[][] arrayOfByte = new byte[paramArrayOfFloat1.length][paramInt2];
/*     */ 
/* 198 */     for (int i = 0; i < paramArrayOfFloat1.length; i++) {
/* 199 */       float f1 = paramArrayOfFloat1[i];
/* 200 */       float f2 = paramArrayOfFloat2[i];
/* 201 */       byte[] arrayOfByte1 = arrayOfByte[i];
/* 202 */       for (int j = 0; j < paramInt2; j++) {
/* 203 */         int k = (int)(j * f1 + f2);
/* 204 */         if ((k & 0xFFFFFF00) != 0) {
/* 205 */           if (k < 0)
/* 206 */             k = 0;
/*     */           else {
/* 208 */             k = 255;
/*     */           }
/*     */         }
/* 211 */         arrayOfByte1[j] = ((byte)k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     return new ByteLookupTable(0, arrayOfByte);
/*     */   }
/*     */ 
/*     */   private ShortLookupTable createShortLut(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt1, int paramInt2)
/*     */   {
/* 231 */     short[][] arrayOfShort = new short[paramArrayOfFloat1.length][paramInt2];
/*     */ 
/* 233 */     for (int i = 0; i < paramArrayOfFloat1.length; i++) {
/* 234 */       float f1 = paramArrayOfFloat1[i];
/* 235 */       float f2 = paramArrayOfFloat2[i];
/* 236 */       short[] arrayOfShort1 = arrayOfShort[i];
/* 237 */       for (int j = 0; j < paramInt2; j++) {
/* 238 */         int k = (int)(j * f1 + f2);
/* 239 */         if ((k & 0xFFFF0000) != 0) {
/* 240 */           if (k < 0)
/* 241 */             k = 0;
/*     */           else {
/* 243 */             k = 65535;
/*     */           }
/*     */         }
/* 246 */         arrayOfShort1[j] = ((short)k);
/*     */       }
/*     */     }
/*     */ 
/* 250 */     return new ShortLookupTable(0, arrayOfShort);
/*     */   }
/*     */ 
/*     */   private boolean canUseLookup(Raster paramRaster1, Raster paramRaster2)
/*     */   {
/* 266 */     int i = paramRaster1.getDataBuffer().getDataType();
/* 267 */     if ((i != 0) && (i != 1))
/*     */     {
/* 269 */       return false;
/*     */     }
/*     */ 
/* 275 */     SampleModel localSampleModel1 = paramRaster2.getSampleModel();
/* 276 */     this.dstNbits = localSampleModel1.getSampleSize(0);
/*     */ 
/* 278 */     if ((this.dstNbits != 8) && (this.dstNbits != 16)) {
/* 279 */       return false;
/*     */     }
/* 281 */     for (int j = 1; j < paramRaster1.getNumBands(); j++) {
/* 282 */       k = localSampleModel1.getSampleSize(j);
/* 283 */       if (k != this.dstNbits) {
/* 284 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 291 */     SampleModel localSampleModel2 = paramRaster1.getSampleModel();
/* 292 */     this.srcNbits = localSampleModel2.getSampleSize(0);
/* 293 */     if (this.srcNbits > 16) {
/* 294 */       return false;
/*     */     }
/* 296 */     for (int k = 1; k < paramRaster1.getNumBands(); k++) {
/* 297 */       int m = localSampleModel2.getSampleSize(k);
/* 298 */       if (m != this.srcNbits) {
/* 299 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 303 */     return true;
/*     */   }
/*     */ 
/*     */   public final BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
/*     */   {
/* 327 */     ColorModel localColorModel1 = paramBufferedImage1.getColorModel();
/*     */ 
/* 329 */     int i = localColorModel1.getNumColorComponents();
/*     */ 
/* 332 */     if ((localColorModel1 instanceof IndexColorModel)) {
/* 333 */       throw new IllegalArgumentException("Rescaling cannot be performed on an indexed image");
/*     */     }
/*     */ 
/* 337 */     if ((this.length != 1) && (this.length != i) && (this.length != localColorModel1.getNumComponents()))
/*     */     {
/* 340 */       throw new IllegalArgumentException("Number of scaling constants does not equal the number of of color or color/alpha  components");
/*     */     }
/*     */ 
/* 346 */     int j = 0;
/*     */ 
/* 349 */     if ((this.length > i) && (localColorModel1.hasAlpha())) {
/* 350 */       this.length = (i + 1);
/*     */     }
/*     */ 
/* 353 */     int k = paramBufferedImage1.getWidth();
/* 354 */     int m = paramBufferedImage1.getHeight();
/*     */     ColorModel localColorModel2;
/* 356 */     if (paramBufferedImage2 == null) {
/* 357 */       paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/* 358 */       localColorModel2 = localColorModel1;
/*     */     }
/*     */     else {
/* 361 */       if (k != paramBufferedImage2.getWidth()) {
/* 362 */         throw new IllegalArgumentException("Src width (" + k + ") not equal to dst width (" + paramBufferedImage2.getWidth() + ")");
/*     */       }
/*     */ 
/* 367 */       if (m != paramBufferedImage2.getHeight()) {
/* 368 */         throw new IllegalArgumentException("Src height (" + m + ") not equal to dst height (" + paramBufferedImage2.getHeight() + ")");
/*     */       }
/*     */ 
/* 374 */       localColorModel2 = paramBufferedImage2.getColorModel();
/* 375 */       if (localColorModel1.getColorSpace().getType() != localColorModel2.getColorSpace().getType())
/*     */       {
/* 377 */         j = 1;
/* 378 */         paramBufferedImage2 = createCompatibleDestImage(paramBufferedImage1, null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 383 */     BufferedImage localBufferedImage = paramBufferedImage2;
/*     */     Object localObject;
/* 388 */     if (ImagingLib.filter(this, paramBufferedImage1, paramBufferedImage2) == null)
/*     */     {
/* 392 */       localObject = paramBufferedImage1.getRaster();
/* 393 */       WritableRaster localWritableRaster = paramBufferedImage2.getRaster();
/*     */       int n;
/*     */       int i1;
/* 395 */       if ((localColorModel1.hasAlpha()) && (
/* 396 */         (i - 1 == this.length) || (this.length == 1))) {
/* 397 */         n = ((WritableRaster)localObject).getMinX();
/* 398 */         i1 = ((WritableRaster)localObject).getMinY();
/* 399 */         int[] arrayOfInt1 = new int[i - 1];
/* 400 */         for (int i3 = 0; i3 < i - 1; i3++) {
/* 401 */           arrayOfInt1[i3] = i3;
/*     */         }
/* 403 */         localObject = ((WritableRaster)localObject).createWritableChild(n, i1, ((WritableRaster)localObject).getWidth(), ((WritableRaster)localObject).getHeight(), n, i1, arrayOfInt1);
/*     */       }
/*     */ 
/* 411 */       if (localColorModel2.hasAlpha()) {
/* 412 */         n = localWritableRaster.getNumBands();
/* 413 */         if ((n - 1 == this.length) || (this.length == 1)) {
/* 414 */           i1 = localWritableRaster.getMinX();
/* 415 */           int i2 = localWritableRaster.getMinY();
/* 416 */           int[] arrayOfInt2 = new int[i - 1];
/* 417 */           for (int i4 = 0; i4 < i - 1; i4++) {
/* 418 */             arrayOfInt2[i4] = i4;
/*     */           }
/* 420 */           localWritableRaster = localWritableRaster.createWritableChild(i1, i2, localWritableRaster.getWidth(), localWritableRaster.getHeight(), i1, i2, arrayOfInt2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 432 */       filter((Raster)localObject, localWritableRaster);
/*     */     }
/*     */ 
/* 436 */     if (j != 0)
/*     */     {
/* 438 */       localObject = new ColorConvertOp(this.hints);
/* 439 */       ((ColorConvertOp)localObject).filter(paramBufferedImage2, localBufferedImage);
/*     */     }
/*     */ 
/* 442 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public final WritableRaster filter(Raster paramRaster, WritableRaster paramWritableRaster)
/*     */   {
/* 464 */     int i = paramRaster.getNumBands();
/* 465 */     int j = paramRaster.getWidth();
/* 466 */     int k = paramRaster.getHeight();
/* 467 */     int[] arrayOfInt1 = null;
/* 468 */     int m = 0;
/* 469 */     int n = 0;
/*     */ 
/* 472 */     if (paramWritableRaster == null) {
/* 473 */       paramWritableRaster = createCompatibleDestRaster(paramRaster);
/*     */     } else {
/* 475 */       if ((k != paramWritableRaster.getHeight()) || (j != paramWritableRaster.getWidth())) {
/* 476 */         throw new IllegalArgumentException("Width or height of Rasters do not match");
/*     */       }
/*     */ 
/* 480 */       if (i != paramWritableRaster.getNumBands())
/*     */       {
/* 482 */         throw new IllegalArgumentException("Number of bands in src " + i + " does not equal number of bands in dest " + paramWritableRaster.getNumBands());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 489 */     if ((this.length != 1) && (this.length != paramRaster.getNumBands())) {
/* 490 */       throw new IllegalArgumentException("Number of scaling constants does not equal the number of of bands in the src raster");
/*     */     }
/*     */ 
/* 499 */     if (ImagingLib.filter(this, paramRaster, paramWritableRaster) != null)
/* 500 */       return paramWritableRaster;
/*     */     int i1;
/*     */     int i2;
/* 507 */     if (canUseLookup(paramRaster, paramWritableRaster)) {
/* 508 */       i1 = 1 << this.srcNbits;
/* 509 */       i2 = 1 << this.dstNbits;
/*     */       Object localObject;
/*     */       LookupOp localLookupOp;
/* 511 */       if (i2 == 256) {
/* 512 */         localObject = createByteLut(this.scaleFactors, this.offsets, i, i1);
/*     */ 
/* 514 */         localLookupOp = new LookupOp((LookupTable)localObject, this.hints);
/* 515 */         localLookupOp.filter(paramRaster, paramWritableRaster);
/*     */       } else {
/* 517 */         localObject = createShortLut(this.scaleFactors, this.offsets, i, i1);
/*     */ 
/* 519 */         localLookupOp = new LookupOp((LookupTable)localObject, this.hints);
/* 520 */         localLookupOp.filter(paramRaster, paramWritableRaster);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 526 */       if (this.length > 1) {
/* 527 */         m = 1;
/*     */       }
/*     */ 
/* 530 */       i1 = paramRaster.getMinX();
/* 531 */       i2 = paramRaster.getMinY();
/* 532 */       int i3 = paramWritableRaster.getMinX();
/* 533 */       int i4 = paramWritableRaster.getMinY();
/*     */ 
/* 543 */       int[] arrayOfInt2 = new int[i];
/* 544 */       int[] arrayOfInt3 = new int[i];
/* 545 */       SampleModel localSampleModel = paramWritableRaster.getSampleModel();
/* 546 */       for (int i8 = 0; i8 < i; i8++) {
/* 547 */         int i7 = localSampleModel.getSampleSize(i8);
/* 548 */         arrayOfInt2[i8] = ((1 << i7) - 1);
/* 549 */         arrayOfInt2[i8] ^= -1;
/*     */       }
/*     */ 
/* 553 */       for (int i9 = 0; i9 < k; i4++) {
/* 554 */         int i6 = i3;
/* 555 */         int i5 = i1;
/* 556 */         for (int i10 = 0; i10 < j; i6++)
/*     */         {
/* 558 */           arrayOfInt1 = paramRaster.getPixel(i5, i2, arrayOfInt1);
/* 559 */           n = 0;
/* 560 */           for (int i11 = 0; i11 < i; n += m) {
/* 561 */             i8 = (int)(arrayOfInt1[i11] * this.scaleFactors[n] + this.offsets[n]);
/*     */ 
/* 564 */             if ((i8 & arrayOfInt3[i11]) != 0) {
/* 565 */               if (i8 < 0)
/* 566 */                 i8 = 0;
/*     */               else {
/* 568 */                 i8 = arrayOfInt2[i11];
/*     */               }
/*     */             }
/* 571 */             arrayOfInt1[i11] = i8;
/*     */ 
/* 560 */             i11++;
/*     */           }
/*     */ 
/* 576 */           paramWritableRaster.setPixel(i6, i4, arrayOfInt1);
/*     */ 
/* 556 */           i10++; i5++;
/*     */         }
/* 553 */         i9++; i2++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 580 */     return paramWritableRaster;
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(BufferedImage paramBufferedImage)
/*     */   {
/* 589 */     return getBounds2D(paramBufferedImage.getRaster());
/*     */   }
/*     */ 
/*     */   public final Rectangle2D getBounds2D(Raster paramRaster)
/*     */   {
/* 600 */     return paramRaster.getBounds();
/*     */   }
/*     */ 
/*     */   public BufferedImage createCompatibleDestImage(BufferedImage paramBufferedImage, ColorModel paramColorModel)
/*     */   {
/*     */     BufferedImage localBufferedImage;
/* 614 */     if (paramColorModel == null) {
/* 615 */       ColorModel localColorModel = paramBufferedImage.getColorModel();
/* 616 */       localBufferedImage = new BufferedImage(localColorModel, paramBufferedImage.getRaster().createCompatibleWritableRaster(), localColorModel.isAlphaPremultiplied(), null);
/*     */     }
/*     */     else
/*     */     {
/* 622 */       int i = paramBufferedImage.getWidth();
/* 623 */       int j = paramBufferedImage.getHeight();
/* 624 */       localBufferedImage = new BufferedImage(paramColorModel, paramColorModel.createCompatibleWritableRaster(i, j), paramColorModel.isAlphaPremultiplied(), null);
/*     */     }
/*     */ 
/* 629 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public WritableRaster createCompatibleDestRaster(Raster paramRaster)
/*     */   {
/* 639 */     return paramRaster.createCompatibleWritableRaster(paramRaster.getWidth(), paramRaster.getHeight());
/*     */   }
/*     */ 
/*     */   public final Point2D getPoint2D(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 652 */     if (paramPoint2D2 == null) {
/* 653 */       paramPoint2D2 = new Point2D.Float();
/*     */     }
/* 655 */     paramPoint2D2.setLocation(paramPoint2D1.getX(), paramPoint2D1.getY());
/* 656 */     return paramPoint2D2;
/*     */   }
/*     */ 
/*     */   public final RenderingHints getRenderingHints()
/*     */   {
/* 664 */     return this.hints;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.RescaleOp
 * JD-Core Version:    0.6.2
 */