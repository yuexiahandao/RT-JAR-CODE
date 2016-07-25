/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.ColorModel;
/*     */ 
/*     */ final class RadialGradientPaintContext extends MultipleGradientPaintContext
/*     */ {
/*  46 */   private boolean isSimpleFocus = false;
/*     */ 
/*  49 */   private boolean isNonCyclic = false;
/*     */   private float radius;
/*     */   private float centerX;
/*     */   private float centerY;
/*     */   private float focusX;
/*     */   private float focusY;
/*     */   private float radiusSq;
/*     */   private float constA;
/*     */   private float constB;
/*     */   private float gDeltaDelta;
/*     */   private float trivial;
/*     */   private static final float SCALEBACK = 0.99F;
/*     */   private static final int SQRT_LUT_SIZE = 2048;
/* 304 */   private static float[] sqrtLut = new float[2049];
/*     */ 
/*     */   RadialGradientPaintContext(RadialGradientPaint paramRadialGradientPaint, ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod, MultipleGradientPaint.ColorSpaceType paramColorSpaceType)
/*     */   {
/* 123 */     super(paramRadialGradientPaint, paramColorModel, paramRectangle, paramRectangle2D, paramAffineTransform, paramRenderingHints, paramArrayOfFloat, paramArrayOfColor, paramCycleMethod, paramColorSpaceType);
/*     */ 
/* 127 */     this.centerX = paramFloat1;
/* 128 */     this.centerY = paramFloat2;
/* 129 */     this.focusX = paramFloat4;
/* 130 */     this.focusY = paramFloat5;
/* 131 */     this.radius = paramFloat3;
/*     */ 
/* 133 */     this.isSimpleFocus = ((this.focusX == this.centerX) && (this.focusY == this.centerY));
/* 134 */     this.isNonCyclic = (paramCycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE);
/*     */ 
/* 137 */     this.radiusSq = (this.radius * this.radius);
/*     */ 
/* 139 */     float f1 = this.focusX - this.centerX;
/* 140 */     float f2 = this.focusY - this.centerY;
/*     */ 
/* 142 */     double d = f1 * f1 + f2 * f2;
/*     */ 
/* 145 */     if (d > this.radiusSq * 0.99F)
/*     */     {
/* 147 */       float f3 = (float)Math.sqrt(this.radiusSq * 0.99F / d);
/* 148 */       f1 *= f3;
/* 149 */       f2 *= f3;
/* 150 */       this.focusX = (this.centerX + f1);
/* 151 */       this.focusY = (this.centerY + f2);
/*     */     }
/*     */ 
/* 156 */     this.trivial = ((float)Math.sqrt(this.radiusSq - f1 * f1));
/*     */ 
/* 159 */     this.constA = (this.a02 - this.centerX);
/* 160 */     this.constB = (this.a12 - this.centerY);
/*     */ 
/* 163 */     this.gDeltaDelta = (2.0F * (this.a00 * this.a00 + this.a10 * this.a10) / this.radiusSq);
/*     */   }
/*     */ 
/*     */   protected void fillRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 176 */     if ((this.isSimpleFocus) && (this.isNonCyclic) && (this.isSimpleLookup))
/* 177 */       simpleNonCyclicFillRaster(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     else
/* 179 */       cyclicCircularGradientFillRaster(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   private void simpleNonCyclicFillRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 226 */     float f1 = this.a00 * paramInt3 + this.a01 * paramInt4 + this.constA;
/* 227 */     float f2 = this.a10 * paramInt3 + this.a11 * paramInt4 + this.constB;
/*     */ 
/* 230 */     float f3 = this.gDeltaDelta;
/*     */ 
/* 233 */     paramInt2 += paramInt5;
/*     */ 
/* 236 */     int i = this.gradient[this.fastGradientArraySize];
/*     */ 
/* 238 */     for (int j = 0; j < paramInt6; j++)
/*     */     {
/* 240 */       float f4 = (f1 * f1 + f2 * f2) / this.radiusSq;
/* 241 */       float f5 = 2.0F * (this.a00 * f1 + this.a10 * f2) / this.radiusSq + f3 / 2.0F;
/*     */ 
/* 259 */       int k = 0;
/*     */ 
/* 261 */       while ((k < paramInt5) && (f4 >= 1.0F)) {
/* 262 */         paramArrayOfInt[(paramInt1 + k)] = i;
/* 263 */         f4 += f5;
/* 264 */         f5 += f3;
/* 265 */         k++;
/*     */       }
/*     */ 
/* 268 */       while ((k < paramInt5) && (f4 < 1.0F))
/*     */       {
/*     */         int m;
/* 271 */         if (f4 <= 0.0F) {
/* 272 */           m = 0;
/*     */         } else {
/* 274 */           float f6 = f4 * 2048.0F;
/* 275 */           int n = (int)f6;
/* 276 */           float f7 = sqrtLut[n];
/* 277 */           float f8 = sqrtLut[(n + 1)] - f7;
/* 278 */           f6 = f7 + (f6 - n) * f8;
/* 279 */           m = (int)(f6 * this.fastGradientArraySize);
/*     */         }
/*     */ 
/* 283 */         paramArrayOfInt[(paramInt1 + k)] = this.gradient[m];
/*     */ 
/* 286 */         f4 += f5;
/* 287 */         f5 += f3;
/* 288 */         k++;
/*     */       }
/*     */ 
/* 291 */       while (k < paramInt5) {
/* 292 */         paramArrayOfInt[(paramInt1 + k)] = i;
/* 293 */         k++;
/*     */       }
/*     */ 
/* 296 */       paramInt1 += paramInt2;
/* 297 */       f1 += this.a01;
/* 298 */       f2 += this.a11;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void cyclicCircularGradientFillRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 336 */     double d1 = -this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY;
/*     */ 
/* 349 */     float f1 = this.a00 * paramInt3 + this.a01 * paramInt4 + this.a02;
/* 350 */     float f2 = this.a10 * paramInt3 + this.a11 * paramInt4 + this.a12;
/*     */ 
/* 353 */     float f3 = 2.0F * this.centerY;
/* 354 */     float f4 = -2.0F * this.centerX;
/*     */ 
/* 372 */     int i = paramInt1;
/*     */ 
/* 375 */     int j = paramInt5 + paramInt2;
/*     */ 
/* 378 */     for (int k = 0; k < paramInt6; k++)
/*     */     {
/* 381 */       float f11 = this.a01 * k + f1;
/* 382 */       float f12 = this.a11 * k + f2;
/*     */ 
/* 385 */       for (int m = 0; m < paramInt5; m++)
/*     */       {
/*     */         double d7;
/*     */         double d8;
/* 387 */         if (f11 == this.focusX)
/*     */         {
/* 389 */           d7 = this.focusX;
/* 390 */           d8 = this.centerY;
/* 391 */           d8 += (f12 > this.focusY ? this.trivial : -this.trivial);
/*     */         }
/*     */         else {
/* 394 */           double d5 = (f12 - this.focusY) / (f11 - this.focusX);
/* 395 */           double d6 = f12 - d5 * f11;
/*     */ 
/* 399 */           double d2 = d5 * d5 + 1.0D;
/* 400 */           double d3 = f4 + -2.0D * d5 * (this.centerY - d6);
/* 401 */           double d4 = d1 + d6 * (d6 - f3);
/*     */ 
/* 403 */           float f6 = (float)Math.sqrt(d3 * d3 - 4.0D * d2 * d4);
/* 404 */           d7 = -d3;
/*     */ 
/* 408 */           d7 += (f11 < this.focusX ? -f6 : f6);
/* 409 */           d7 /= 2.0D * d2;
/* 410 */           d8 = d5 * d7 + d6;
/*     */         }
/*     */ 
/* 418 */         float f9 = f11 - this.focusX;
/* 419 */         f9 *= f9;
/*     */ 
/* 421 */         float f10 = f12 - this.focusY;
/* 422 */         f10 *= f10;
/*     */ 
/* 424 */         float f7 = f9 + f10;
/*     */ 
/* 426 */         f9 = (float)d7 - this.focusX;
/* 427 */         f9 *= f9;
/*     */ 
/* 429 */         f10 = (float)d8 - this.focusY;
/* 430 */         f10 *= f10;
/*     */ 
/* 432 */         float f8 = f9 + f10;
/*     */ 
/* 436 */         float f5 = (float)Math.sqrt(f7 / f8);
/*     */ 
/* 439 */         paramArrayOfInt[(i + m)] = indexIntoGradientsArrays(f5);
/*     */ 
/* 442 */         f11 += this.a00;
/* 443 */         f12 += this.a10;
/*     */       }
/*     */ 
/* 446 */       i += j;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 306 */     for (int i = 0; i < sqrtLut.length; i++)
/* 307 */       sqrtLut[i] = ((float)Math.sqrt(i / 2048.0F));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.RadialGradientPaintContext
 * JD-Core Version:    0.6.2
 */