/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.ColorModel;
/*     */ 
/*     */ final class LinearGradientPaintContext extends MultipleGradientPaintContext
/*     */ {
/*     */   private float dgdX;
/*     */   private float dgdY;
/*     */   private float gc;
/*     */ 
/*     */   LinearGradientPaintContext(LinearGradientPaint paramLinearGradientPaint, ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints, Point2D paramPoint2D1, Point2D paramPoint2D2, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod, MultipleGradientPaint.ColorSpaceType paramColorSpaceType)
/*     */   {
/*  90 */     super(paramLinearGradientPaint, paramColorModel, paramRectangle, paramRectangle2D, paramAffineTransform, paramRenderingHints, paramArrayOfFloat, paramArrayOfColor, paramCycleMethod, paramColorSpaceType);
/*     */ 
/* 103 */     float f1 = (float)paramPoint2D1.getX();
/* 104 */     float f2 = (float)paramPoint2D1.getY();
/* 105 */     float f3 = (float)paramPoint2D2.getX();
/* 106 */     float f4 = (float)paramPoint2D2.getY();
/*     */ 
/* 108 */     float f5 = f3 - f1;
/* 109 */     float f6 = f4 - f2;
/* 110 */     float f7 = f5 * f5 + f6 * f6;
/*     */ 
/* 113 */     float f8 = f5 / f7;
/* 114 */     float f9 = f6 / f7;
/*     */ 
/* 117 */     this.dgdX = (this.a00 * f8 + this.a10 * f9);
/*     */ 
/* 119 */     this.dgdY = (this.a01 * f8 + this.a11 * f9);
/*     */ 
/* 122 */     this.gc = ((this.a02 - f1) * f8 + (this.a12 - f2) * f9);
/*     */   }
/*     */ 
/*     */   protected void fillRaster(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 137 */     float f1 = 0.0F;
/*     */ 
/* 140 */     int i = paramInt1 + paramInt5;
/*     */ 
/* 143 */     float f2 = this.dgdX * paramInt3 + this.gc;
/*     */ 
/* 145 */     for (int j = 0; j < paramInt6; j++)
/*     */     {
/* 148 */       f1 = f2 + this.dgdY * (paramInt4 + j);
/*     */ 
/* 150 */       while (paramInt1 < i)
/*     */       {
/* 152 */         paramArrayOfInt[(paramInt1++)] = indexIntoGradientsArrays(f1);
/*     */ 
/* 155 */         f1 += this.dgdX;
/*     */       }
/*     */ 
/* 159 */       paramInt1 += paramInt2;
/*     */ 
/* 162 */       i = paramInt1 + paramInt5;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.LinearGradientPaintContext
 * JD-Core Version:    0.6.2
 */