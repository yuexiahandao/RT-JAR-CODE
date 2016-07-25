/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public class GradientPaint
/*     */   implements Paint
/*     */ {
/*     */   Point2D.Float p1;
/*     */   Point2D.Float p2;
/*     */   Color color1;
/*     */   Color color2;
/*     */   boolean cyclic;
/*     */ 
/*     */   public GradientPaint(float paramFloat1, float paramFloat2, Color paramColor1, float paramFloat3, float paramFloat4, Color paramColor2)
/*     */   {
/*  89 */     if ((paramColor1 == null) || (paramColor2 == null)) {
/*  90 */       throw new NullPointerException("Colors cannot be null");
/*     */     }
/*     */ 
/*  93 */     this.p1 = new Point2D.Float(paramFloat1, paramFloat2);
/*  94 */     this.p2 = new Point2D.Float(paramFloat3, paramFloat4);
/*  95 */     this.color1 = paramColor1;
/*  96 */     this.color2 = paramColor2;
/*     */   }
/*     */ 
/*     */   public GradientPaint(Point2D paramPoint2D1, Color paramColor1, Point2D paramPoint2D2, Color paramColor2)
/*     */   {
/* 114 */     if ((paramColor1 == null) || (paramColor2 == null) || (paramPoint2D1 == null) || (paramPoint2D2 == null))
/*     */     {
/* 116 */       throw new NullPointerException("Colors and points should be non-null");
/*     */     }
/*     */ 
/* 119 */     this.p1 = new Point2D.Float((float)paramPoint2D1.getX(), (float)paramPoint2D1.getY());
/* 120 */     this.p2 = new Point2D.Float((float)paramPoint2D2.getX(), (float)paramPoint2D2.getY());
/* 121 */     this.color1 = paramColor1;
/* 122 */     this.color2 = paramColor2;
/*     */   }
/*     */ 
/*     */   public GradientPaint(float paramFloat1, float paramFloat2, Color paramColor1, float paramFloat3, float paramFloat4, Color paramColor2, boolean paramBoolean)
/*     */   {
/* 150 */     this(paramFloat1, paramFloat2, paramColor1, paramFloat3, paramFloat4, paramColor2);
/* 151 */     this.cyclic = paramBoolean;
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"point1", "color1", "point2", "color2", "cyclic"})
/*     */   public GradientPaint(Point2D paramPoint2D1, Color paramColor1, Point2D paramPoint2D2, Color paramColor2, boolean paramBoolean)
/*     */   {
/* 176 */     this(paramPoint2D1, paramColor1, paramPoint2D2, paramColor2);
/* 177 */     this.cyclic = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Point2D getPoint1()
/*     */   {
/* 187 */     return new Point2D.Float(this.p1.x, this.p1.y);
/*     */   }
/*     */ 
/*     */   public Color getColor1()
/*     */   {
/* 196 */     return this.color1;
/*     */   }
/*     */ 
/*     */   public Point2D getPoint2()
/*     */   {
/* 206 */     return new Point2D.Float(this.p2.x, this.p2.y);
/*     */   }
/*     */ 
/*     */   public Color getColor2()
/*     */   {
/* 215 */     return this.color2;
/*     */   }
/*     */ 
/*     */   public boolean isCyclic()
/*     */   {
/* 225 */     return this.cyclic;
/*     */   }
/*     */ 
/*     */   public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
/*     */   {
/* 262 */     return new GradientPaintContext(paramColorModel, this.p1, this.p2, paramAffineTransform, this.color1, this.color2, this.cyclic);
/*     */   }
/*     */ 
/*     */   public int getTransparency()
/*     */   {
/* 273 */     int i = this.color1.getAlpha();
/* 274 */     int j = this.color2.getAlpha();
/* 275 */     return (i & j) == 255 ? 1 : 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GradientPaint
 * JD-Core Version:    0.6.2
 */