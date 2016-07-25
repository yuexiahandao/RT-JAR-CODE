/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ abstract class MultipleGradientPaintContext
/*     */   implements PaintContext
/*     */ {
/*     */   protected ColorModel model;
/*  62 */   private static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, 65280, 255);
/*     */   protected static ColorModel cachedModel;
/*     */   protected static WeakReference<Raster> cached;
/*     */   protected Raster saved;
/*     */   protected MultipleGradientPaint.CycleMethod cycleMethod;
/*     */   protected MultipleGradientPaint.ColorSpaceType colorSpace;
/*     */   protected float a00;
/*     */   protected float a01;
/*     */   protected float a10;
/*     */   protected float a11;
/*     */   protected float a02;
/*     */   protected float a12;
/*     */   protected boolean isSimpleLookup;
/*     */   protected int fastGradientArraySize;
/*     */   protected int[] gradient;
/*     */   private int[][] gradients;
/*     */   private float[] normalizedIntervals;
/*     */   private float[] fractions;
/*     */   private int transparencyTest;
/* 121 */   private static final int[] SRGBtoLinearRGB = new int[256];
/* 122 */   private static final int[] LinearRGBtoSRGB = new int[256];
/*     */   protected static final int GRADIENT_SIZE = 256;
/*     */   protected static final int GRADIENT_SIZE_INDEX = 255;
/*     */   private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;
/*     */ 
/*     */   protected MultipleGradientPaintContext(MultipleGradientPaint paramMultipleGradientPaint, ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod, MultipleGradientPaint.ColorSpaceType paramColorSpaceType)
/*     */   {
/* 161 */     if (paramRectangle == null) {
/* 162 */       throw new NullPointerException("Device bounds cannot be null");
/*     */     }
/*     */ 
/* 165 */     if (paramRectangle2D == null) {
/* 166 */       throw new NullPointerException("User bounds cannot be null");
/*     */     }
/*     */ 
/* 169 */     if (paramAffineTransform == null) {
/* 170 */       throw new NullPointerException("Transform cannot be null");
/*     */     }
/*     */ 
/* 173 */     if (paramRenderingHints == null) {
/* 174 */       throw new NullPointerException("RenderingHints cannot be null");
/*     */     }
/*     */ 
/*     */     AffineTransform localAffineTransform;
/*     */     try
/*     */     {
/* 183 */       paramAffineTransform.invert();
/* 184 */       localAffineTransform = paramAffineTransform;
/*     */     }
/*     */     catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*     */     {
/* 188 */       localAffineTransform = new AffineTransform();
/*     */     }
/* 190 */     double[] arrayOfDouble = new double[6];
/* 191 */     localAffineTransform.getMatrix(arrayOfDouble);
/* 192 */     this.a00 = ((float)arrayOfDouble[0]);
/* 193 */     this.a10 = ((float)arrayOfDouble[1]);
/* 194 */     this.a01 = ((float)arrayOfDouble[2]);
/* 195 */     this.a11 = ((float)arrayOfDouble[3]);
/* 196 */     this.a02 = ((float)arrayOfDouble[4]);
/* 197 */     this.a12 = ((float)arrayOfDouble[5]);
/*     */ 
/* 200 */     this.cycleMethod = paramCycleMethod;
/* 201 */     this.colorSpace = paramColorSpaceType;
/*     */ 
/* 204 */     this.fractions = paramArrayOfFloat;
/*     */ 
/* 209 */     Object localObject = paramMultipleGradientPaint.gradient != null ? (int[])paramMultipleGradientPaint.gradient.get() : null;
/*     */ 
/* 211 */     int[][] arrayOfInt = paramMultipleGradientPaint.gradients != null ? (int[][])paramMultipleGradientPaint.gradients.get() : (int[][])null;
/*     */ 
/* 214 */     if ((localObject == null) && (arrayOfInt == null))
/*     */     {
/* 216 */       calculateLookupData(paramArrayOfColor);
/*     */ 
/* 220 */       paramMultipleGradientPaint.model = this.model;
/* 221 */       paramMultipleGradientPaint.normalizedIntervals = this.normalizedIntervals;
/* 222 */       paramMultipleGradientPaint.isSimpleLookup = this.isSimpleLookup;
/* 223 */       if (this.isSimpleLookup)
/*     */       {
/* 225 */         paramMultipleGradientPaint.fastGradientArraySize = this.fastGradientArraySize;
/* 226 */         paramMultipleGradientPaint.gradient = new SoftReference(this.gradient);
/*     */       }
/*     */       else {
/* 229 */         paramMultipleGradientPaint.gradients = new SoftReference(this.gradients);
/*     */       }
/*     */     }
/*     */     else {
/* 233 */       this.model = paramMultipleGradientPaint.model;
/* 234 */       this.normalizedIntervals = paramMultipleGradientPaint.normalizedIntervals;
/* 235 */       this.isSimpleLookup = paramMultipleGradientPaint.isSimpleLookup;
/* 236 */       this.gradient = localObject;
/* 237 */       this.fastGradientArraySize = paramMultipleGradientPaint.fastGradientArraySize;
/* 238 */       this.gradients = arrayOfInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void calculateLookupData(Color[] paramArrayOfColor)
/*     */   {
/*     */     Color[] arrayOfColor;
/* 249 */     if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB)
/*     */     {
/* 251 */       arrayOfColor = new Color[paramArrayOfColor.length];
/*     */ 
/* 253 */       for (i = 0; i < paramArrayOfColor.length; i++) {
/* 254 */         j = paramArrayOfColor[i].getRGB();
/* 255 */         k = j >>> 24;
/* 256 */         int m = SRGBtoLinearRGB[(j >> 16 & 0xFF)];
/* 257 */         int n = SRGBtoLinearRGB[(j >> 8 & 0xFF)];
/* 258 */         int i1 = SRGBtoLinearRGB[(j & 0xFF)];
/* 259 */         arrayOfColor[i] = new Color(m, n, i1, k);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 264 */       arrayOfColor = paramArrayOfColor;
/*     */     }
/*     */ 
/* 268 */     this.normalizedIntervals = new float[this.fractions.length - 1];
/*     */ 
/* 271 */     for (int i = 0; i < this.normalizedIntervals.length; i++)
/*     */     {
/* 273 */       this.normalizedIntervals[i] = (this.fractions[(i + 1)] - this.fractions[i]);
/*     */     }
/*     */ 
/* 277 */     this.transparencyTest = -16777216;
/*     */ 
/* 280 */     this.gradients = new int[this.normalizedIntervals.length][];
/*     */ 
/* 283 */     float f = 1.0F;
/* 284 */     for (int j = 0; j < this.normalizedIntervals.length; j++) {
/* 285 */       f = f > this.normalizedIntervals[j] ? this.normalizedIntervals[j] : f;
/*     */     }
/*     */ 
/* 294 */     j = 0;
/* 295 */     for (int k = 0; k < this.normalizedIntervals.length; k++) {
/* 296 */       j = (int)(j + this.normalizedIntervals[k] / f * 256.0F);
/*     */     }
/*     */ 
/* 299 */     if (j > 5000)
/*     */     {
/* 301 */       calculateMultipleArrayGradient(arrayOfColor);
/*     */     }
/*     */     else {
/* 304 */       calculateSingleArrayGradient(arrayOfColor, f);
/*     */     }
/*     */ 
/* 308 */     if (this.transparencyTest >>> 24 == 255)
/* 309 */       this.model = xrgbmodel;
/*     */     else
/* 311 */       this.model = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   private void calculateSingleArrayGradient(Color[] paramArrayOfColor, float paramFloat)
/*     */   {
/* 340 */     this.isSimpleLookup = true;
/*     */ 
/* 346 */     int k = 1;
/*     */ 
/* 349 */     for (int m = 0; m < this.gradients.length; m++)
/*     */     {
/* 352 */       n = (int)(this.normalizedIntervals[m] / paramFloat * 255.0F);
/* 353 */       k += n;
/* 354 */       this.gradients[m] = new int[n];
/*     */ 
/* 357 */       int i = paramArrayOfColor[m].getRGB();
/* 358 */       int j = paramArrayOfColor[(m + 1)].getRGB();
/*     */ 
/* 361 */       interpolate(i, j, this.gradients[m]);
/*     */ 
/* 365 */       this.transparencyTest &= i;
/* 366 */       this.transparencyTest &= j;
/*     */     }
/*     */ 
/* 370 */     this.gradient = new int[k];
/* 371 */     m = 0;
/* 372 */     for (int n = 0; n < this.gradients.length; n++) {
/* 373 */       System.arraycopy(this.gradients[n], 0, this.gradient, m, this.gradients[n].length);
/*     */ 
/* 375 */       m += this.gradients[n].length;
/*     */     }
/* 377 */     this.gradient[(this.gradient.length - 1)] = paramArrayOfColor[(paramArrayOfColor.length - 1)].getRGB();
/*     */ 
/* 381 */     if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
/* 382 */       for (n = 0; n < this.gradient.length; n++) {
/* 383 */         this.gradient[n] = convertEntireColorLinearRGBtoSRGB(this.gradient[n]);
/*     */       }
/*     */     }
/*     */ 
/* 387 */     this.fastGradientArraySize = (this.gradient.length - 1);
/*     */   }
/*     */ 
/*     */   private void calculateMultipleArrayGradient(Color[] paramArrayOfColor)
/*     */   {
/* 410 */     this.isSimpleLookup = false;
/*     */ 
/* 416 */     for (int k = 0; k < this.gradients.length; k++)
/*     */     {
/* 419 */       this.gradients[k] = new int[256];
/*     */ 
/* 422 */       int i = paramArrayOfColor[k].getRGB();
/* 423 */       int j = paramArrayOfColor[(k + 1)].getRGB();
/*     */ 
/* 426 */       interpolate(i, j, this.gradients[k]);
/*     */ 
/* 430 */       this.transparencyTest &= i;
/* 431 */       this.transparencyTest &= j;
/*     */     }
/*     */ 
/* 436 */     if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB)
/* 437 */       for (k = 0; k < this.gradients.length; k++)
/* 438 */         for (int m = 0; m < this.gradients[k].length; m++)
/* 439 */           this.gradients[k][m] = convertEntireColorLinearRGBtoSRGB(this.gradients[k][m]);
/*     */   }
/*     */ 
/*     */   private void interpolate(int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*     */   {
/* 459 */     float f = 1.0F / paramArrayOfInt.length;
/*     */ 
/* 462 */     int i = paramInt1 >> 24 & 0xFF;
/* 463 */     int j = paramInt1 >> 16 & 0xFF;
/* 464 */     int k = paramInt1 >> 8 & 0xFF;
/* 465 */     int m = paramInt1 & 0xFF;
/*     */ 
/* 468 */     int n = (paramInt2 >> 24 & 0xFF) - i;
/* 469 */     int i1 = (paramInt2 >> 16 & 0xFF) - j;
/* 470 */     int i2 = (paramInt2 >> 8 & 0xFF) - k;
/* 471 */     int i3 = (paramInt2 & 0xFF) - m;
/*     */ 
/* 476 */     for (int i4 = 0; i4 < paramArrayOfInt.length; i4++)
/* 477 */       paramArrayOfInt[i4] = ((int)(i + i4 * n * f + 0.5D) << 24 | (int)(j + i4 * i1 * f + 0.5D) << 16 | (int)(k + i4 * i2 * f + 0.5D) << 8 | (int)(m + i4 * i3 * f + 0.5D));
/*     */   }
/*     */ 
/*     */   private int convertEntireColorLinearRGBtoSRGB(int paramInt)
/*     */   {
/* 495 */     int i = paramInt >> 24 & 0xFF;
/* 496 */     int j = paramInt >> 16 & 0xFF;
/* 497 */     int k = paramInt >> 8 & 0xFF;
/* 498 */     int m = paramInt & 0xFF;
/*     */ 
/* 501 */     j = LinearRGBtoSRGB[j];
/* 502 */     k = LinearRGBtoSRGB[k];
/* 503 */     m = LinearRGBtoSRGB[m];
/*     */ 
/* 506 */     return i << 24 | j << 16 | k << 8 | m;
/*     */   }
/*     */ 
/*     */   protected final int indexIntoGradientsArrays(float paramFloat)
/*     */   {
/* 524 */     if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
/* 525 */       if (paramFloat > 1.0F)
/*     */       {
/* 527 */         paramFloat = 1.0F;
/* 528 */       } else if (paramFloat < 0.0F)
/*     */       {
/* 530 */         paramFloat = 0.0F;
/*     */       }
/* 532 */     } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT)
/*     */     {
/* 535 */       paramFloat -= (int)paramFloat;
/*     */ 
/* 538 */       if (paramFloat < 0.0F)
/*     */       {
/* 540 */         paramFloat += 1.0F;
/*     */       }
/*     */     } else {
/* 543 */       if (paramFloat < 0.0F)
/*     */       {
/* 545 */         paramFloat = -paramFloat;
/*     */       }
/*     */ 
/* 549 */       i = (int)paramFloat;
/*     */ 
/* 552 */       paramFloat -= i;
/*     */ 
/* 554 */       if ((i & 0x1) == 1)
/*     */       {
/* 556 */         paramFloat = 1.0F - paramFloat;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 562 */     if (this.isSimpleLookup)
/*     */     {
/* 564 */       return this.gradient[((int)(paramFloat * this.fastGradientArraySize))];
/*     */     }
/*     */ 
/* 569 */     for (int i = 0; i < this.gradients.length; i++) {
/* 570 */       if (paramFloat < this.fractions[(i + 1)])
/*     */       {
/* 572 */         float f = paramFloat - this.fractions[i];
/*     */ 
/* 575 */         int j = (int)(f / this.normalizedIntervals[i] * 255.0F);
/*     */ 
/* 578 */         return this.gradients[i][j];
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 583 */     return this.gradients[(this.gradients.length - 1)]['ÿ'];
/*     */   }
/*     */ 
/*     */   private static int convertSRGBtoLinearRGB(int paramInt)
/*     */   {
/* 593 */     float f1 = paramInt / 255.0F;
/*     */     float f2;
/* 594 */     if (f1 <= 0.04045F)
/* 595 */       f2 = f1 / 12.92F;
/*     */     else {
/* 597 */       f2 = (float)Math.pow((f1 + 0.055D) / 1.055D, 2.4D);
/*     */     }
/*     */ 
/* 600 */     return Math.round(f2 * 255.0F);
/*     */   }
/*     */ 
/*     */   private static int convertLinearRGBtoSRGB(int paramInt)
/*     */   {
/* 610 */     float f1 = paramInt / 255.0F;
/*     */     float f2;
/* 611 */     if (f1 <= 0.0031308D)
/* 612 */       f2 = f1 * 12.92F;
/*     */     else {
/* 614 */       f2 = 1.055F * (float)Math.pow(f1, 0.4166666666666667D) - 0.055F;
/*     */     }
/*     */ 
/* 618 */     return Math.round(f2 * 255.0F);
/*     */   }
/*     */ 
/*     */   public final Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 627 */     Raster localRaster = this.saved;
/* 628 */     if ((localRaster == null) || (localRaster.getWidth() < paramInt3) || (localRaster.getHeight() < paramInt4))
/*     */     {
/* 631 */       localRaster = getCachedRaster(this.model, paramInt3, paramInt4);
/* 632 */       this.saved = localRaster;
/*     */     }
/*     */ 
/* 642 */     DataBufferInt localDataBufferInt = (DataBufferInt)localRaster.getDataBuffer();
/* 643 */     int[] arrayOfInt = localDataBufferInt.getData(0);
/* 644 */     int i = localDataBufferInt.getOffset();
/* 645 */     int j = ((SinglePixelPackedSampleModel)localRaster.getSampleModel()).getScanlineStride();
/*     */ 
/* 647 */     int k = j - paramInt3;
/*     */ 
/* 649 */     fillRaster(arrayOfInt, i, k, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 651 */     return localRaster;
/*     */   }
/*     */ 
/*     */   protected abstract void fillRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */ 
/*     */   private static synchronized Raster getCachedRaster(ColorModel paramColorModel, int paramInt1, int paramInt2)
/*     */   {
/* 666 */     if ((paramColorModel == cachedModel) && 
/* 667 */       (cached != null)) {
/* 668 */       Raster localRaster = (Raster)cached.get();
/* 669 */       if ((localRaster != null) && (localRaster.getWidth() >= paramInt1) && (localRaster.getHeight() >= paramInt2))
/*     */       {
/* 673 */         cached = null;
/* 674 */         return localRaster;
/*     */       }
/*     */     }
/*     */ 
/* 678 */     return paramColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private static synchronized void putCachedRaster(ColorModel paramColorModel, Raster paramRaster)
/*     */   {
/* 689 */     if (cached != null) {
/* 690 */       Raster localRaster = (Raster)cached.get();
/* 691 */       if (localRaster != null) {
/* 692 */         int i = localRaster.getWidth();
/* 693 */         int j = localRaster.getHeight();
/* 694 */         int k = paramRaster.getWidth();
/* 695 */         int m = paramRaster.getHeight();
/* 696 */         if ((i >= k) && (j >= m)) {
/* 697 */           return;
/*     */         }
/* 699 */         if (i * j >= k * m) {
/* 700 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 704 */     cachedModel = paramColorModel;
/* 705 */     cached = new WeakReference(paramRaster);
/*     */   }
/*     */ 
/*     */   public final void dispose()
/*     */   {
/* 712 */     if (this.saved != null) {
/* 713 */       putCachedRaster(this.model, this.saved);
/* 714 */       this.saved = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final ColorModel getColorModel()
/*     */   {
/* 722 */     return this.model;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 126 */     for (int i = 0; i < 256; i++) {
/* 127 */       SRGBtoLinearRGB[i] = convertSRGBtoLinearRGB(i);
/* 128 */       LinearRGBtoSRGB[i] = convertLinearRGBtoSRGB(i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MultipleGradientPaintContext
 * JD-Core Version:    0.6.2
 */