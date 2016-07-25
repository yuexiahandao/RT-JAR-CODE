/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Rectangle2D extends RectangularShape
/*     */ {
/*     */   public static final int OUT_LEFT = 1;
/*     */   public static final int OUT_TOP = 2;
/*     */   public static final int OUT_RIGHT = 4;
/*     */   public static final int OUT_BOTTOM = 8;
/*     */ 
/*     */   public abstract void setRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*     */ 
/*     */   public void setRect(Rectangle2D paramRectangle2D)
/*     */   {
/* 535 */     setRect(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public boolean intersectsLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*     */     int j;
/* 557 */     if ((j = outcode(paramDouble3, paramDouble4)) == 0)
/* 558 */       return true;
/*     */     int i;
/* 560 */     while ((i = outcode(paramDouble1, paramDouble2)) != 0) {
/* 561 */       if ((i & j) != 0)
/* 562 */         return false;
/*     */       double d;
/* 564 */       if ((i & 0x5) != 0) {
/* 565 */         d = getX();
/* 566 */         if ((i & 0x4) != 0) {
/* 567 */           d += getWidth();
/*     */         }
/* 569 */         paramDouble2 += (d - paramDouble1) * (paramDouble4 - paramDouble2) / (paramDouble3 - paramDouble1);
/* 570 */         paramDouble1 = d;
/*     */       } else {
/* 572 */         d = getY();
/* 573 */         if ((i & 0x8) != 0) {
/* 574 */           d += getHeight();
/*     */         }
/* 576 */         paramDouble1 += (d - paramDouble2) * (paramDouble3 - paramDouble1) / (paramDouble4 - paramDouble2);
/* 577 */         paramDouble2 = d;
/*     */       }
/*     */     }
/* 580 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean intersectsLine(Line2D paramLine2D)
/*     */   {
/* 594 */     return intersectsLine(paramLine2D.getX1(), paramLine2D.getY1(), paramLine2D.getX2(), paramLine2D.getY2());
/*     */   }
/*     */ 
/*     */   public abstract int outcode(double paramDouble1, double paramDouble2);
/*     */ 
/*     */   public int outcode(Point2D paramPoint2D)
/*     */   {
/* 631 */     return outcode(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public void setFrame(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 647 */     setRect(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 655 */     return (Rectangle2D)clone();
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2)
/*     */   {
/* 663 */     double d1 = getX();
/* 664 */     double d2 = getY();
/* 665 */     return (paramDouble1 >= d1) && (paramDouble2 >= d2) && (paramDouble1 < d1 + getWidth()) && (paramDouble2 < d2 + getHeight());
/*     */   }
/*     */ 
/*     */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 676 */     if ((isEmpty()) || (paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 677 */       return false;
/*     */     }
/* 679 */     double d1 = getX();
/* 680 */     double d2 = getY();
/* 681 */     return (paramDouble1 + paramDouble3 > d1) && (paramDouble2 + paramDouble4 > d2) && (paramDouble1 < d1 + getWidth()) && (paramDouble2 < d2 + getHeight());
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 692 */     if ((isEmpty()) || (paramDouble3 <= 0.0D) || (paramDouble4 <= 0.0D)) {
/* 693 */       return false;
/*     */     }
/* 695 */     double d1 = getX();
/* 696 */     double d2 = getY();
/* 697 */     return (paramDouble1 >= d1) && (paramDouble2 >= d2) && (paramDouble1 + paramDouble3 <= d1 + getWidth()) && (paramDouble2 + paramDouble4 <= d2 + getHeight());
/*     */   }
/*     */ 
/*     */   public abstract Rectangle2D createIntersection(Rectangle2D paramRectangle2D);
/*     */ 
/*     */   public static void intersect(Rectangle2D paramRectangle2D1, Rectangle2D paramRectangle2D2, Rectangle2D paramRectangle2D3)
/*     */   {
/* 735 */     double d1 = Math.max(paramRectangle2D1.getMinX(), paramRectangle2D2.getMinX());
/* 736 */     double d2 = Math.max(paramRectangle2D1.getMinY(), paramRectangle2D2.getMinY());
/* 737 */     double d3 = Math.min(paramRectangle2D1.getMaxX(), paramRectangle2D2.getMaxX());
/* 738 */     double d4 = Math.min(paramRectangle2D1.getMaxY(), paramRectangle2D2.getMaxY());
/* 739 */     paramRectangle2D3.setFrame(d1, d2, d3 - d1, d4 - d2);
/*     */   }
/*     */ 
/*     */   public abstract Rectangle2D createUnion(Rectangle2D paramRectangle2D);
/*     */ 
/*     */   public static void union(Rectangle2D paramRectangle2D1, Rectangle2D paramRectangle2D2, Rectangle2D paramRectangle2D3)
/*     */   {
/* 774 */     double d1 = Math.min(paramRectangle2D1.getMinX(), paramRectangle2D2.getMinX());
/* 775 */     double d2 = Math.min(paramRectangle2D1.getMinY(), paramRectangle2D2.getMinY());
/* 776 */     double d3 = Math.max(paramRectangle2D1.getMaxX(), paramRectangle2D2.getMaxX());
/* 777 */     double d4 = Math.max(paramRectangle2D1.getMaxY(), paramRectangle2D2.getMaxY());
/* 778 */     paramRectangle2D3.setFrameFromDiagonal(d1, d2, d3, d4);
/*     */   }
/*     */ 
/*     */   public void add(double paramDouble1, double paramDouble2)
/*     */   {
/* 801 */     double d1 = Math.min(getMinX(), paramDouble1);
/* 802 */     double d2 = Math.max(getMaxX(), paramDouble1);
/* 803 */     double d3 = Math.min(getMinY(), paramDouble2);
/* 804 */     double d4 = Math.max(getMaxY(), paramDouble2);
/* 805 */     setRect(d1, d3, d2 - d1, d4 - d3);
/*     */   }
/*     */ 
/*     */   public void add(Point2D paramPoint2D)
/*     */   {
/* 827 */     add(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public void add(Rectangle2D paramRectangle2D)
/*     */   {
/* 839 */     double d1 = Math.min(getMinX(), paramRectangle2D.getMinX());
/* 840 */     double d2 = Math.max(getMaxX(), paramRectangle2D.getMaxX());
/* 841 */     double d3 = Math.min(getMinY(), paramRectangle2D.getMinY());
/* 842 */     double d4 = Math.max(getMaxY(), paramRectangle2D.getMaxY());
/* 843 */     setRect(d1, d3, d2 - d1, d4 - d3);
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*     */   {
/* 863 */     return new RectIterator(this, paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*     */   {
/* 888 */     return new RectIterator(this, paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 897 */     long l = Double.doubleToLongBits(getX());
/* 898 */     l += Double.doubleToLongBits(getY()) * 37L;
/* 899 */     l += Double.doubleToLongBits(getWidth()) * 43L;
/* 900 */     l += Double.doubleToLongBits(getHeight()) * 47L;
/* 901 */     return (int)l ^ (int)(l >> 32);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 918 */     if (paramObject == this) {
/* 919 */       return true;
/*     */     }
/* 921 */     if ((paramObject instanceof Rectangle2D)) {
/* 922 */       Rectangle2D localRectangle2D = (Rectangle2D)paramObject;
/* 923 */       return (getX() == localRectangle2D.getX()) && (getY() == localRectangle2D.getY()) && (getWidth() == localRectangle2D.getWidth()) && (getHeight() == localRectangle2D.getHeight());
/*     */     }
/*     */ 
/* 928 */     return false;
/*     */   }
/*     */ 
/*     */   public static class Double extends Rectangle2D
/*     */     implements Serializable
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double width;
/*     */     public double height;
/*     */     private static final long serialVersionUID = 7771313791441850493L;
/*     */ 
/*     */     public Double()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Double(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 362 */       setRect(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/* 370 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 378 */       return this.y;
/*     */     }
/*     */ 
/*     */     public double getWidth()
/*     */     {
/* 386 */       return this.width;
/*     */     }
/*     */ 
/*     */     public double getHeight()
/*     */     {
/* 394 */       return this.height;
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 402 */       return (this.width <= 0.0D) || (this.height <= 0.0D);
/*     */     }
/*     */ 
/*     */     public void setRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 410 */       this.x = paramDouble1;
/* 411 */       this.y = paramDouble2;
/* 412 */       this.width = paramDouble3;
/* 413 */       this.height = paramDouble4;
/*     */     }
/*     */ 
/*     */     public void setRect(Rectangle2D paramRectangle2D)
/*     */     {
/* 421 */       this.x = paramRectangle2D.getX();
/* 422 */       this.y = paramRectangle2D.getY();
/* 423 */       this.width = paramRectangle2D.getWidth();
/* 424 */       this.height = paramRectangle2D.getHeight();
/*     */     }
/*     */ 
/*     */     public int outcode(double paramDouble1, double paramDouble2)
/*     */     {
/* 432 */       int i = 0;
/* 433 */       if (this.width <= 0.0D)
/* 434 */         i |= 5;
/* 435 */       else if (paramDouble1 < this.x)
/* 436 */         i |= 1;
/* 437 */       else if (paramDouble1 > this.x + this.width) {
/* 438 */         i |= 4;
/*     */       }
/* 440 */       if (this.height <= 0.0D)
/* 441 */         i |= 10;
/* 442 */       else if (paramDouble2 < this.y)
/* 443 */         i |= 2;
/* 444 */       else if (paramDouble2 > this.y + this.height) {
/* 445 */         i |= 8;
/*     */       }
/* 447 */       return i;
/*     */     }
/*     */ 
/*     */     public Rectangle2D getBounds2D()
/*     */     {
/* 455 */       return new Double(this.x, this.y, this.width, this.height);
/*     */     }
/*     */ 
/*     */     public Rectangle2D createIntersection(Rectangle2D paramRectangle2D)
/*     */     {
/* 463 */       Double localDouble = new Double();
/* 464 */       Rectangle2D.intersect(this, paramRectangle2D, localDouble);
/* 465 */       return localDouble;
/*     */     }
/*     */ 
/*     */     public Rectangle2D createUnion(Rectangle2D paramRectangle2D)
/*     */     {
/* 473 */       Double localDouble = new Double();
/* 474 */       Rectangle2D.union(this, paramRectangle2D, localDouble);
/* 475 */       return localDouble;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 486 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",w=" + this.width + ",h=" + this.height + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Float extends Rectangle2D
/*     */     implements Serializable
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     public float width;
/*     */     public float height;
/*     */     private static final long serialVersionUID = 3798716824173675777L;
/*     */ 
/*     */     public Float()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Float(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 129 */       setRect(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/* 137 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 145 */       return this.y;
/*     */     }
/*     */ 
/*     */     public double getWidth()
/*     */     {
/* 153 */       return this.width;
/*     */     }
/*     */ 
/*     */     public double getHeight()
/*     */     {
/* 161 */       return this.height;
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 169 */       return (this.width <= 0.0F) || (this.height <= 0.0F);
/*     */     }
/*     */ 
/*     */     public void setRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 185 */       this.x = paramFloat1;
/* 186 */       this.y = paramFloat2;
/* 187 */       this.width = paramFloat3;
/* 188 */       this.height = paramFloat4;
/*     */     }
/*     */ 
/*     */     public void setRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */     {
/* 196 */       this.x = ((float)paramDouble1);
/* 197 */       this.y = ((float)paramDouble2);
/* 198 */       this.width = ((float)paramDouble3);
/* 199 */       this.height = ((float)paramDouble4);
/*     */     }
/*     */ 
/*     */     public void setRect(Rectangle2D paramRectangle2D)
/*     */     {
/* 207 */       this.x = ((float)paramRectangle2D.getX());
/* 208 */       this.y = ((float)paramRectangle2D.getY());
/* 209 */       this.width = ((float)paramRectangle2D.getWidth());
/* 210 */       this.height = ((float)paramRectangle2D.getHeight());
/*     */     }
/*     */ 
/*     */     public int outcode(double paramDouble1, double paramDouble2)
/*     */     {
/* 228 */       int i = 0;
/* 229 */       if (this.width <= 0.0F)
/* 230 */         i |= 5;
/* 231 */       else if (paramDouble1 < this.x)
/* 232 */         i |= 1;
/* 233 */       else if (paramDouble1 > this.x + this.width) {
/* 234 */         i |= 4;
/*     */       }
/* 236 */       if (this.height <= 0.0F)
/* 237 */         i |= 10;
/* 238 */       else if (paramDouble2 < this.y)
/* 239 */         i |= 2;
/* 240 */       else if (paramDouble2 > this.y + this.height) {
/* 241 */         i |= 8;
/*     */       }
/* 243 */       return i;
/*     */     }
/*     */ 
/*     */     public Rectangle2D getBounds2D()
/*     */     {
/* 251 */       return new Float(this.x, this.y, this.width, this.height);
/*     */     }
/*     */ 
/*     */     public Rectangle2D createIntersection(Rectangle2D paramRectangle2D)
/*     */     {
/*     */       Object localObject;
/* 260 */       if ((paramRectangle2D instanceof Float))
/* 261 */         localObject = new Float();
/*     */       else {
/* 263 */         localObject = new Rectangle2D.Double();
/*     */       }
/* 265 */       Rectangle2D.intersect(this, paramRectangle2D, (Rectangle2D)localObject);
/* 266 */       return localObject;
/*     */     }
/*     */ 
/*     */     public Rectangle2D createUnion(Rectangle2D paramRectangle2D)
/*     */     {
/*     */       Object localObject;
/* 275 */       if ((paramRectangle2D instanceof Float))
/* 276 */         localObject = new Float();
/*     */       else {
/* 278 */         localObject = new Rectangle2D.Double();
/*     */       }
/* 280 */       Rectangle2D.union(this, paramRectangle2D, (Rectangle2D)localObject);
/* 281 */       return localObject;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 292 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",w=" + this.width + ",h=" + this.height + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Rectangle2D
 * JD-Core Version:    0.6.2
 */