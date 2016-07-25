/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ public abstract class GlyphVector
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int FLAG_HAS_TRANSFORMS = 1;
/*     */   public static final int FLAG_HAS_POSITION_ADJUSTMENTS = 2;
/*     */   public static final int FLAG_RUN_RTL = 4;
/*     */   public static final int FLAG_COMPLEX_GLYPHS = 8;
/*     */   public static final int FLAG_MASK = 15;
/*     */ 
/*     */   public abstract Font getFont();
/*     */ 
/*     */   public abstract FontRenderContext getFontRenderContext();
/*     */ 
/*     */   public abstract void performDefaultLayout();
/*     */ 
/*     */   public abstract int getNumGlyphs();
/*     */ 
/*     */   public abstract int getGlyphCode(int paramInt);
/*     */ 
/*     */   public abstract int[] getGlyphCodes(int paramInt1, int paramInt2, int[] paramArrayOfInt);
/*     */ 
/*     */   public int getGlyphCharIndex(int paramInt)
/*     */   {
/* 203 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int[] getGlyphCharIndices(int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*     */   {
/* 224 */     if (paramArrayOfInt == null) {
/* 225 */       paramArrayOfInt = new int[paramInt2];
/*     */     }
/* 227 */     int i = 0; for (int j = paramInt1; i < paramInt2; j++) {
/* 228 */       paramArrayOfInt[i] = getGlyphCharIndex(j);
/*     */ 
/* 227 */       i++;
/*     */     }
/*     */ 
/* 230 */     return paramArrayOfInt;
/*     */   }
/*     */ 
/*     */   public abstract Rectangle2D getLogicalBounds();
/*     */ 
/*     */   public abstract Rectangle2D getVisualBounds();
/*     */ 
/*     */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*     */   {
/* 272 */     Rectangle2D localRectangle2D = getVisualBounds();
/* 273 */     int i = (int)Math.floor(localRectangle2D.getX() + paramFloat1);
/* 274 */     int j = (int)Math.floor(localRectangle2D.getY() + paramFloat2);
/* 275 */     int k = (int)Math.ceil(localRectangle2D.getMaxX() + paramFloat1);
/* 276 */     int m = (int)Math.ceil(localRectangle2D.getMaxY() + paramFloat2);
/* 277 */     return new Rectangle(i, j, k - i, m - j);
/*     */   }
/*     */ 
/*     */   public abstract Shape getOutline();
/*     */ 
/*     */   public abstract Shape getOutline(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract Shape getGlyphOutline(int paramInt);
/*     */ 
/*     */   public Shape getGlyphOutline(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 336 */     Shape localShape = getGlyphOutline(paramInt);
/* 337 */     AffineTransform localAffineTransform = AffineTransform.getTranslateInstance(paramFloat1, paramFloat2);
/* 338 */     return localAffineTransform.createTransformedShape(localShape);
/*     */   }
/*     */ 
/*     */   public abstract Point2D getGlyphPosition(int paramInt);
/*     */ 
/*     */   public abstract void setGlyphPosition(int paramInt, Point2D paramPoint2D);
/*     */ 
/*     */   public abstract AffineTransform getGlyphTransform(int paramInt);
/*     */ 
/*     */   public abstract void setGlyphTransform(int paramInt, AffineTransform paramAffineTransform);
/*     */ 
/*     */   public int getLayoutFlags()
/*     */   {
/* 425 */     return 0;
/*     */   }
/*     */ 
/*     */   public abstract float[] getGlyphPositions(int paramInt1, int paramInt2, float[] paramArrayOfFloat);
/*     */ 
/*     */   public abstract Shape getGlyphLogicalBounds(int paramInt);
/*     */ 
/*     */   public abstract Shape getGlyphVisualBounds(int paramInt);
/*     */ 
/*     */   public Rectangle getGlyphPixelBounds(int paramInt, FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2)
/*     */   {
/* 560 */     Rectangle2D localRectangle2D = getGlyphVisualBounds(paramInt).getBounds2D();
/* 561 */     int i = (int)Math.floor(localRectangle2D.getX() + paramFloat1);
/* 562 */     int j = (int)Math.floor(localRectangle2D.getY() + paramFloat2);
/* 563 */     int k = (int)Math.ceil(localRectangle2D.getMaxX() + paramFloat1);
/* 564 */     int m = (int)Math.ceil(localRectangle2D.getMaxY() + paramFloat2);
/* 565 */     return new Rectangle(i, j, k - i, m - j);
/*     */   }
/*     */ 
/*     */   public abstract GlyphMetrics getGlyphMetrics(int paramInt);
/*     */ 
/*     */   public abstract GlyphJustificationInfo getGlyphJustificationInfo(int paramInt);
/*     */ 
/*     */   public abstract boolean equals(GlyphVector paramGlyphVector);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.GlyphVector
 * JD-Core Version:    0.6.2
 */