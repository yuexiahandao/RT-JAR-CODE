/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import sun.awt.geom.Crossings;
/*     */ import sun.awt.geom.Crossings.EvenOdd;
/*     */ 
/*     */ public class Polygon
/*     */   implements Shape, Serializable
/*     */ {
/*     */   public int npoints;
/*     */   public int[] xpoints;
/*     */   public int[] ypoints;
/*     */   protected Rectangle bounds;
/*     */   private static final long serialVersionUID = -6460061437900069969L;
/*     */   private static final int MIN_LENGTH = 4;
/*     */ 
/*     */   public Polygon()
/*     */   {
/* 127 */     this.xpoints = new int[4];
/* 128 */     this.ypoints = new int[4];
/*     */   }
/*     */ 
/*     */   public Polygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 150 */     if ((paramInt > paramArrayOfInt1.length) || (paramInt > paramArrayOfInt2.length)) {
/* 151 */       throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
/*     */     }
/*     */ 
/* 156 */     if (paramInt < 0) {
/* 157 */       throw new NegativeArraySizeException("npoints < 0");
/*     */     }
/*     */ 
/* 161 */     this.npoints = paramInt;
/* 162 */     this.xpoints = Arrays.copyOf(paramArrayOfInt1, paramInt);
/* 163 */     this.ypoints = Arrays.copyOf(paramArrayOfInt2, paramInt);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 184 */     this.npoints = 0;
/* 185 */     this.bounds = null;
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 201 */     this.bounds = null;
/*     */   }
/*     */ 
/*     */   public void translate(int paramInt1, int paramInt2)
/*     */   {
/* 213 */     for (int i = 0; i < this.npoints; i++) {
/* 214 */       this.xpoints[i] += paramInt1;
/* 215 */       this.ypoints[i] += paramInt2;
/*     */     }
/* 217 */     if (this.bounds != null)
/* 218 */       this.bounds.translate(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   void calculateBounds(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 230 */     int i = 2147483647;
/* 231 */     int j = 2147483647;
/* 232 */     int k = -2147483648;
/* 233 */     int m = -2147483648;
/*     */ 
/* 235 */     for (int n = 0; n < paramInt; n++) {
/* 236 */       int i1 = paramArrayOfInt1[n];
/* 237 */       i = Math.min(i, i1);
/* 238 */       k = Math.max(k, i1);
/* 239 */       int i2 = paramArrayOfInt2[n];
/* 240 */       j = Math.min(j, i2);
/* 241 */       m = Math.max(m, i2);
/*     */     }
/* 243 */     this.bounds = new Rectangle(i, j, k - i, m - j);
/*     */   }
/*     */ 
/*     */   void updateBounds(int paramInt1, int paramInt2)
/*     */   {
/* 253 */     if (paramInt1 < this.bounds.x) {
/* 254 */       this.bounds.width += this.bounds.x - paramInt1;
/* 255 */       this.bounds.x = paramInt1;
/*     */     }
/*     */     else {
/* 258 */       this.bounds.width = Math.max(this.bounds.width, paramInt1 - this.bounds.x);
/*     */     }
/*     */ 
/* 262 */     if (paramInt2 < this.bounds.y) {
/* 263 */       this.bounds.height += this.bounds.y - paramInt2;
/* 264 */       this.bounds.y = paramInt2;
/*     */     }
/*     */     else {
/* 267 */       this.bounds.height = Math.max(this.bounds.height, paramInt2 - this.bounds.y);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPoint(int paramInt1, int paramInt2)
/*     */   {
/* 286 */     if ((this.npoints >= this.xpoints.length) || (this.npoints >= this.ypoints.length)) {
/* 287 */       int i = this.npoints * 2;
/*     */ 
/* 290 */       if (i < 4)
/* 291 */         i = 4;
/* 292 */       else if ((i & i - 1) != 0) {
/* 293 */         i = Integer.highestOneBit(i);
/*     */       }
/*     */ 
/* 296 */       this.xpoints = Arrays.copyOf(this.xpoints, i);
/* 297 */       this.ypoints = Arrays.copyOf(this.ypoints, i);
/*     */     }
/* 299 */     this.xpoints[this.npoints] = paramInt1;
/* 300 */     this.ypoints[this.npoints] = paramInt2;
/* 301 */     this.npoints += 1;
/* 302 */     if (this.bounds != null)
/* 303 */       updateBounds(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 317 */     return getBoundingBox();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Rectangle getBoundingBox()
/*     */   {
/* 329 */     if (this.npoints == 0) {
/* 330 */       return new Rectangle();
/*     */     }
/* 332 */     if (this.bounds == null) {
/* 333 */       calculateBounds(this.xpoints, this.ypoints, this.npoints);
/*     */     }
/* 335 */     return this.bounds.getBounds();
/*     */   }
/*     */ 
/*     */   public boolean contains(Point paramPoint)
/*     */   {
/* 348 */     return contains(paramPoint.x, paramPoint.y);
/*     */   }
/*     */ 
/*     */   public boolean contains(int paramInt1, int paramInt2)
/*     */   {
/* 364 */     return contains(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public boolean inside(int paramInt1, int paramInt2)
/*     */   {
/* 382 */     return contains(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 390 */     return getBounds();
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2)
/*     */   {
/* 398 */     if ((this.npoints <= 2) || (!getBoundingBox().contains(paramDouble1, paramDouble2))) {
/* 399 */       return false;
/*     */     }
/* 401 */     int i = 0;
/*     */ 
/* 403 */     int j = this.xpoints[(this.npoints - 1)];
/* 404 */     int k = this.ypoints[(this.npoints - 1)];
/*     */ 
/* 408 */     for (int i1 = 0; i1 < this.npoints; i1++) {
/* 409 */       int m = this.xpoints[i1];
/* 410 */       int n = this.ypoints[i1];
/*     */ 
/* 412 */       if (n != k)
/*     */       {
/*     */         int i2;
/* 417 */         if (m < j) {
/* 418 */           if (paramDouble1 >= j) {
/*     */             break label260;
/*     */           }
/* 421 */           i2 = m;
/*     */         } else {
/* 423 */           if (paramDouble1 >= m) {
/*     */             break label260;
/*     */           }
/* 426 */           i2 = j;
/*     */         }
/*     */         double d1;
/*     */         double d2;
/* 430 */         if (n < k) {
/* 431 */           if ((paramDouble2 < n) || (paramDouble2 >= k)) {
/*     */             break label260;
/*     */           }
/* 434 */           if (paramDouble1 < i2) {
/* 435 */             i++;
/* 436 */             break label260;
/*     */           }
/* 438 */           d1 = paramDouble1 - m;
/* 439 */           d2 = paramDouble2 - n;
/*     */         } else {
/* 441 */           if ((paramDouble2 < k) || (paramDouble2 >= n)) {
/*     */             break label260;
/*     */           }
/* 444 */           if (paramDouble1 < i2) {
/* 445 */             i++;
/* 446 */             break label260;
/*     */           }
/* 448 */           d1 = paramDouble1 - j;
/* 449 */           d2 = paramDouble2 - k;
/*     */         }
/*     */ 
/* 452 */         if (d1 < d2 / (k - n) * (j - m))
/* 453 */           i++;
/*     */       }
/* 408 */       label260: j = m; k = n;
/*     */     }
/*     */ 
/* 457 */     return (i & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   private Crossings getCrossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 463 */     Crossings.EvenOdd localEvenOdd = new Crossings.EvenOdd(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/* 464 */     int i = this.xpoints[(this.npoints - 1)];
/* 465 */     int j = this.ypoints[(this.npoints - 1)];
/*     */ 
/* 469 */     for (int n = 0; n < this.npoints; n++) {
/* 470 */       int k = this.xpoints[n];
/* 471 */       int m = this.ypoints[n];
/* 472 */       if (localEvenOdd.accumulateLine(i, j, k, m)) {
/* 473 */         return null;
/*     */       }
/* 475 */       i = k;
/* 476 */       j = m;
/*     */     }
/*     */ 
/* 479 */     return localEvenOdd;
/*     */   }
/*     */ 
/*     */   public boolean contains(Point2D paramPoint2D)
/*     */   {
/* 487 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 495 */     if ((this.npoints <= 0) || (!getBoundingBox().intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4))) {
/* 496 */       return false;
/*     */     }
/*     */ 
/* 499 */     Crossings localCrossings = getCrossings(paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 500 */     return (localCrossings == null) || (!localCrossings.isEmpty());
/*     */   }
/*     */ 
/*     */   public boolean intersects(Rectangle2D paramRectangle2D)
/*     */   {
/* 508 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 516 */     if ((this.npoints <= 0) || (!getBoundingBox().intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4))) {
/* 517 */       return false;
/*     */     }
/*     */ 
/* 520 */     Crossings localCrossings = getCrossings(paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 521 */     return (localCrossings != null) && (localCrossings.covers(paramDouble2, paramDouble2 + paramDouble4));
/*     */   }
/*     */ 
/*     */   public boolean contains(Rectangle2D paramRectangle2D)
/*     */   {
/* 529 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*     */   {
/* 546 */     return new PolygonPathIterator(this, paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*     */   {
/* 571 */     return getPathIterator(paramAffineTransform);
/*     */   }
/*     */   class PolygonPathIterator implements PathIterator {
/*     */     Polygon poly;
/*     */     AffineTransform transform;
/*     */     int index;
/*     */ 
/* 580 */     public PolygonPathIterator(Polygon paramAffineTransform, AffineTransform arg3) { this.poly = paramAffineTransform;
/*     */       Object localObject;
/* 581 */       this.transform = localObject;
/* 582 */       if (paramAffineTransform.npoints == 0)
/*     */       {
/* 584 */         this.index = 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getWindingRule()
/*     */     {
/* 595 */       return 0;
/*     */     }
/*     */ 
/*     */     public boolean isDone()
/*     */     {
/* 604 */       return this.index > this.poly.npoints;
/*     */     }
/*     */ 
/*     */     public void next()
/*     */     {
/* 613 */       this.index += 1;
/*     */     }
/*     */ 
/*     */     public int currentSegment(float[] paramArrayOfFloat)
/*     */     {
/* 635 */       if (this.index >= this.poly.npoints) {
/* 636 */         return 4;
/*     */       }
/* 638 */       paramArrayOfFloat[0] = this.poly.xpoints[this.index];
/* 639 */       paramArrayOfFloat[1] = this.poly.ypoints[this.index];
/* 640 */       if (this.transform != null) {
/* 641 */         this.transform.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */       }
/* 643 */       return this.index == 0 ? 0 : 1;
/*     */     }
/*     */ 
/*     */     public int currentSegment(double[] paramArrayOfDouble)
/*     */     {
/* 666 */       if (this.index >= this.poly.npoints) {
/* 667 */         return 4;
/*     */       }
/* 669 */       paramArrayOfDouble[0] = this.poly.xpoints[this.index];
/* 670 */       paramArrayOfDouble[1] = this.poly.ypoints[this.index];
/* 671 */       if (this.transform != null) {
/* 672 */         this.transform.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */       }
/* 674 */       return this.index == 0 ? 0 : 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Polygon
 * JD-Core Version:    0.6.2
 */