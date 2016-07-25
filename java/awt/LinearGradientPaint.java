/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public final class LinearGradientPaint extends MultipleGradientPaint
/*     */ {
/*     */   private final Point2D start;
/*     */   private final Point2D end;
/*     */ 
/*     */   public LinearGradientPaint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat, Color[] paramArrayOfColor)
/*     */   {
/* 140 */     this(new Point2D.Float(paramFloat1, paramFloat2), new Point2D.Float(paramFloat3, paramFloat4), paramArrayOfFloat, paramArrayOfColor, MultipleGradientPaint.CycleMethod.NO_CYCLE);
/*     */   }
/*     */ 
/*     */   public LinearGradientPaint(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod)
/*     */   {
/* 181 */     this(new Point2D.Float(paramFloat1, paramFloat2), new Point2D.Float(paramFloat3, paramFloat4), paramArrayOfFloat, paramArrayOfColor, paramCycleMethod);
/*     */   }
/*     */ 
/*     */   public LinearGradientPaint(Point2D paramPoint2D1, Point2D paramPoint2D2, float[] paramArrayOfFloat, Color[] paramArrayOfColor)
/*     */   {
/* 212 */     this(paramPoint2D1, paramPoint2D2, paramArrayOfFloat, paramArrayOfColor, MultipleGradientPaint.CycleMethod.NO_CYCLE);
/*     */   }
/*     */ 
/*     */   public LinearGradientPaint(Point2D paramPoint2D1, Point2D paramPoint2D2, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod)
/*     */   {
/* 245 */     this(paramPoint2D1, paramPoint2D2, paramArrayOfFloat, paramArrayOfColor, paramCycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"startPoint", "endPoint", "fractions", "colors", "cycleMethod", "colorSpace", "transform"})
/*     */   public LinearGradientPaint(Point2D paramPoint2D1, Point2D paramPoint2D2, float[] paramArrayOfFloat, Color[] paramArrayOfColor, MultipleGradientPaint.CycleMethod paramCycleMethod, MultipleGradientPaint.ColorSpaceType paramColorSpaceType, AffineTransform paramAffineTransform)
/*     */   {
/* 287 */     super(paramArrayOfFloat, paramArrayOfColor, paramCycleMethod, paramColorSpaceType, paramAffineTransform);
/*     */ 
/* 290 */     if ((paramPoint2D1 == null) || (paramPoint2D2 == null)) {
/* 291 */       throw new NullPointerException("Start and end points must benon-null");
/*     */     }
/*     */ 
/* 295 */     if (paramPoint2D1.equals(paramPoint2D2)) {
/* 296 */       throw new IllegalArgumentException("Start point cannot equalendpoint");
/*     */     }
/*     */ 
/* 301 */     this.start = new Point2D.Double(paramPoint2D1.getX(), paramPoint2D1.getY());
/* 302 */     this.end = new Point2D.Double(paramPoint2D2.getX(), paramPoint2D2.getY());
/*     */   }
/*     */ 
/*     */   public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
/*     */   {
/* 340 */     paramAffineTransform = new AffineTransform(paramAffineTransform);
/*     */ 
/* 342 */     paramAffineTransform.concatenate(this.gradientTransform);
/*     */ 
/* 344 */     if ((this.fractions.length == 2) && (this.cycleMethod != MultipleGradientPaint.CycleMethod.REPEAT) && (this.colorSpace == MultipleGradientPaint.ColorSpaceType.SRGB))
/*     */     {
/* 350 */       boolean bool = this.cycleMethod != MultipleGradientPaint.CycleMethod.NO_CYCLE;
/* 351 */       return new GradientPaintContext(paramColorModel, this.start, this.end, paramAffineTransform, this.colors[0], this.colors[1], bool);
/*     */     }
/*     */ 
/* 356 */     return new LinearGradientPaintContext(this, paramColorModel, paramRectangle, paramRectangle2D, paramAffineTransform, paramRenderingHints, this.start, this.end, this.fractions, this.colors, this.cycleMethod, this.colorSpace);
/*     */   }
/*     */ 
/*     */   public Point2D getStartPoint()
/*     */   {
/* 372 */     return new Point2D.Double(this.start.getX(), this.start.getY());
/*     */   }
/*     */ 
/*     */   public Point2D getEndPoint()
/*     */   {
/* 382 */     return new Point2D.Double(this.end.getX(), this.end.getY());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.LinearGradientPaint
 * JD-Core Version:    0.6.2
 */