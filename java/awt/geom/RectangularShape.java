/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.beans.Transient;
/*     */ 
/*     */ public abstract class RectangularShape
/*     */   implements Shape, Cloneable
/*     */ {
/*     */   public abstract double getX();
/*     */ 
/*     */   public abstract double getY();
/*     */ 
/*     */   public abstract double getWidth();
/*     */ 
/*     */   public abstract double getHeight();
/*     */ 
/*     */   public double getMinX()
/*     */   {
/* 102 */     return getX();
/*     */   }
/*     */ 
/*     */   public double getMinY()
/*     */   {
/* 114 */     return getY();
/*     */   }
/*     */ 
/*     */   public double getMaxX()
/*     */   {
/* 126 */     return getX() + getWidth();
/*     */   }
/*     */ 
/*     */   public double getMaxY()
/*     */   {
/* 138 */     return getY() + getHeight();
/*     */   }
/*     */ 
/*     */   public double getCenterX()
/*     */   {
/* 150 */     return getX() + getWidth() / 2.0D;
/*     */   }
/*     */ 
/*     */   public double getCenterY()
/*     */   {
/* 162 */     return getY() + getHeight() / 2.0D;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public Rectangle2D getFrame()
/*     */   {
/* 177 */     return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
/*     */   }
/*     */ 
/*     */   public abstract boolean isEmpty();
/*     */ 
/*     */   public abstract void setFrame(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*     */ 
/*     */   public void setFrame(Point2D paramPoint2D, Dimension2D paramDimension2D)
/*     */   {
/* 217 */     setFrame(paramPoint2D.getX(), paramPoint2D.getY(), paramDimension2D.getWidth(), paramDimension2D.getHeight());
/*     */   }
/*     */ 
/*     */   public void setFrame(Rectangle2D paramRectangle2D)
/*     */   {
/* 230 */     setFrame(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public void setFrameFromDiagonal(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*     */     double d;
/* 247 */     if (paramDouble3 < paramDouble1) {
/* 248 */       d = paramDouble1;
/* 249 */       paramDouble1 = paramDouble3;
/* 250 */       paramDouble3 = d;
/*     */     }
/* 252 */     if (paramDouble4 < paramDouble2) {
/* 253 */       d = paramDouble2;
/* 254 */       paramDouble2 = paramDouble4;
/* 255 */       paramDouble4 = d;
/*     */     }
/* 257 */     setFrame(paramDouble1, paramDouble2, paramDouble3 - paramDouble1, paramDouble4 - paramDouble2);
/*     */   }
/*     */ 
/*     */   public void setFrameFromDiagonal(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 271 */     setFrameFromDiagonal(paramPoint2D1.getX(), paramPoint2D1.getY(), paramPoint2D2.getX(), paramPoint2D2.getY());
/*     */   }
/*     */ 
/*     */   public void setFrameFromCenter(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 288 */     double d1 = Math.abs(paramDouble3 - paramDouble1);
/* 289 */     double d2 = Math.abs(paramDouble4 - paramDouble2);
/* 290 */     setFrame(paramDouble1 - d1, paramDouble2 - d2, d1 * 2.0D, d2 * 2.0D);
/*     */   }
/*     */ 
/*     */   public void setFrameFromCenter(Point2D paramPoint2D1, Point2D paramPoint2D2)
/*     */   {
/* 303 */     setFrameFromCenter(paramPoint2D1.getX(), paramPoint2D1.getY(), paramPoint2D2.getX(), paramPoint2D2.getY());
/*     */   }
/*     */ 
/*     */   public boolean contains(Point2D paramPoint2D)
/*     */   {
/* 312 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public boolean intersects(Rectangle2D paramRectangle2D)
/*     */   {
/* 320 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public boolean contains(Rectangle2D paramRectangle2D)
/*     */   {
/* 328 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 336 */     double d1 = getWidth();
/* 337 */     double d2 = getHeight();
/* 338 */     if ((d1 < 0.0D) || (d2 < 0.0D)) {
/* 339 */       return new Rectangle();
/*     */     }
/* 341 */     double d3 = getX();
/* 342 */     double d4 = getY();
/* 343 */     double d5 = Math.floor(d3);
/* 344 */     double d6 = Math.floor(d4);
/* 345 */     double d7 = Math.ceil(d3 + d1);
/* 346 */     double d8 = Math.ceil(d4 + d2);
/* 347 */     return new Rectangle((int)d5, (int)d6, (int)(d7 - d5), (int)(d8 - d6));
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*     */   {
/* 378 */     return new FlatteningPathIterator(getPathIterator(paramAffineTransform), paramDouble);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 391 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 394 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.RectangularShape
 * JD-Core Version:    0.6.2
 */