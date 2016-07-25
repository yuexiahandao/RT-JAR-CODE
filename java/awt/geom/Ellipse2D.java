/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Ellipse2D extends RectangularShape
/*     */ {
/*     */   public boolean contains(double paramDouble1, double paramDouble2)
/*     */   {
/* 334 */     double d1 = getWidth();
/* 335 */     if (d1 <= 0.0D) {
/* 336 */       return false;
/*     */     }
/* 338 */     double d2 = (paramDouble1 - getX()) / d1 - 0.5D;
/* 339 */     double d3 = getHeight();
/* 340 */     if (d3 <= 0.0D) {
/* 341 */       return false;
/*     */     }
/* 343 */     double d4 = (paramDouble2 - getY()) / d3 - 0.5D;
/* 344 */     return d2 * d2 + d4 * d4 < 0.25D;
/*     */   }
/*     */ 
/*     */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 352 */     if ((paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 353 */       return false;
/*     */     }
/*     */ 
/* 357 */     double d1 = getWidth();
/* 358 */     if (d1 <= 0.0D) {
/* 359 */       return false;
/*     */     }
/* 361 */     double d2 = (paramDouble1 - getX()) / d1 - 0.5D;
/* 362 */     double d3 = d2 + paramDouble3 / d1;
/* 363 */     double d4 = getHeight();
/* 364 */     if (d4 <= 0.0D) {
/* 365 */       return false;
/*     */     }
/* 367 */     double d5 = (paramDouble2 - getY()) / d4 - 0.5D;
/* 368 */     double d6 = d5 + paramDouble4 / d4;
/*     */     double d7;
/* 373 */     if (d2 > 0.0D)
/*     */     {
/* 375 */       d7 = d2;
/* 376 */     } else if (d3 < 0.0D)
/*     */     {
/* 378 */       d7 = d3;
/*     */     }
/* 380 */     else d7 = 0.0D;
/*     */     double d8;
/* 382 */     if (d5 > 0.0D)
/*     */     {
/* 384 */       d8 = d5;
/* 385 */     } else if (d6 < 0.0D)
/*     */     {
/* 387 */       d8 = d6;
/*     */     }
/* 389 */     else d8 = 0.0D;
/*     */ 
/* 391 */     return d7 * d7 + d8 * d8 < 0.25D;
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 399 */     return (contains(paramDouble1, paramDouble2)) && (contains(paramDouble1 + paramDouble3, paramDouble2)) && (contains(paramDouble1, paramDouble2 + paramDouble4)) && (contains(paramDouble1 + paramDouble3, paramDouble2 + paramDouble4));
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*     */   {
/* 422 */     return new EllipseIterator(this, paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 431 */     long l = Double.doubleToLongBits(getX());
/* 432 */     l += Double.doubleToLongBits(getY()) * 37L;
/* 433 */     l += Double.doubleToLongBits(getWidth()) * 43L;
/* 434 */     l += Double.doubleToLongBits(getHeight()) * 47L;
/* 435 */     return (int)l ^ (int)(l >> 32);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 452 */     if (paramObject == this) {
/* 453 */       return true;
/*     */     }
/* 455 */     if ((paramObject instanceof Ellipse2D)) {
/* 456 */       Ellipse2D localEllipse2D = (Ellipse2D)paramObject;
/* 457 */       return (getX() == localEllipse2D.getX()) && (getY() == localEllipse2D.getY()) && (getWidth() == localEllipse2D.getWidth()) && (getHeight() == localEllipse2D.getHeight());
/*     */     }
/*     */ 
/* 462 */     return false;
/*     */   }
/*     */ 
/*     */   public static class Double extends Ellipse2D
/*     */     implements Serializable
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double width;
/*     */     public double height;
/*     */     private static final long serialVersionUID = 5555464816372320683L;
/*     */ 
/*     */     public Double()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Double(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 245 */       setFrame(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/* 253 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 261 */       return this.y;
/*     */     }
/*     */ 
/*     */     public double getWidth()
/*     */     {
/* 269 */       return this.width;
/*     */     }
/*     */ 
/*     */     public double getHeight()
/*     */     {
/* 277 */       return this.height;
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 285 */       return (this.width <= 0.0D) || (this.height <= 0.0D);
/*     */     }
/*     */ 
/*     */     public void setFrame(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 293 */       this.x = paramDouble1;
/* 294 */       this.y = paramDouble2;
/* 295 */       this.width = paramDouble3;
/* 296 */       this.height = paramDouble4;
/*     */     }
/*     */ 
/*     */     public Rectangle2D getBounds2D()
/*     */     {
/* 304 */       return new Rectangle2D.Double(this.x, this.y, this.width, this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Float extends Ellipse2D
/*     */     implements Serializable
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     public float width;
/*     */     public float height;
/*     */     private static final long serialVersionUID = -6633761252372475977L;
/*     */ 
/*     */     public Float()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Float(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 101 */       setFrame(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/* 109 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 117 */       return this.y;
/*     */     }
/*     */ 
/*     */     public double getWidth()
/*     */     {
/* 125 */       return this.width;
/*     */     }
/*     */ 
/*     */     public double getHeight()
/*     */     {
/* 133 */       return this.height;
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 141 */       return (this.width <= 0.0D) || (this.height <= 0.0D);
/*     */     }
/*     */ 
/*     */     public void setFrame(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 157 */       this.x = paramFloat1;
/* 158 */       this.y = paramFloat2;
/* 159 */       this.width = paramFloat3;
/* 160 */       this.height = paramFloat4;
/*     */     }
/*     */ 
/*     */     public void setFrame(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 168 */       this.x = ((float)paramDouble1);
/* 169 */       this.y = ((float)paramDouble2);
/* 170 */       this.width = ((float)paramDouble3);
/* 171 */       this.height = ((float)paramDouble4);
/*     */     }
/*     */ 
/*     */     public Rectangle2D getBounds2D()
/*     */     {
/* 179 */       return new Rectangle2D.Float(this.x, this.y, this.width, this.height);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Ellipse2D
 * JD-Core Version:    0.6.2
 */