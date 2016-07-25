/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import sun.awt.geom.AreaOp;
/*     */ import sun.awt.geom.AreaOp.AddOp;
/*     */ import sun.awt.geom.AreaOp.EOWindOp;
/*     */ import sun.awt.geom.AreaOp.IntOp;
/*     */ import sun.awt.geom.AreaOp.NZWindOp;
/*     */ import sun.awt.geom.AreaOp.SubOp;
/*     */ import sun.awt.geom.AreaOp.XorOp;
/*     */ import sun.awt.geom.Crossings;
/*     */ import sun.awt.geom.Curve;
/*     */ 
/*     */ public class Area
/*     */   implements Shape, Cloneable
/*     */ {
/* 100 */   private static Vector EmptyCurves = new Vector();
/*     */   private Vector curves;
/*     */   private Rectangle2D cachedBounds;
/*     */ 
/*     */   public Area()
/*     */   {
/* 109 */     this.curves = EmptyCurves;
/*     */   }
/*     */ 
/*     */   public Area(Shape paramShape)
/*     */   {
/* 123 */     if ((paramShape instanceof Area))
/* 124 */       this.curves = ((Area)paramShape).curves;
/*     */     else
/* 126 */       this.curves = pathToCurves(paramShape.getPathIterator(null));
/*     */   }
/*     */ 
/*     */   private static Vector pathToCurves(PathIterator paramPathIterator)
/*     */   {
/* 131 */     Vector localVector = new Vector();
/* 132 */     int i = paramPathIterator.getWindingRule();
/*     */ 
/* 147 */     double[] arrayOfDouble = new double[23];
/* 148 */     double d1 = 0.0D; double d2 = 0.0D;
/* 149 */     double d3 = 0.0D; double d4 = 0.0D;
/*     */ 
/* 151 */     while (!paramPathIterator.isDone())
/*     */     {
/*     */       double d5;
/*     */       double d6;
/* 152 */       switch (paramPathIterator.currentSegment(arrayOfDouble)) {
/*     */       case 0:
/* 154 */         Curve.insertLine(localVector, d3, d4, d1, d2);
/* 155 */         d3 = d1 = arrayOfDouble[0];
/* 156 */         d4 = d2 = arrayOfDouble[1];
/* 157 */         Curve.insertMove(localVector, d1, d2);
/* 158 */         break;
/*     */       case 1:
/* 160 */         d5 = arrayOfDouble[0];
/* 161 */         d6 = arrayOfDouble[1];
/* 162 */         Curve.insertLine(localVector, d3, d4, d5, d6);
/* 163 */         d3 = d5;
/* 164 */         d4 = d6;
/* 165 */         break;
/*     */       case 2:
/* 167 */         d5 = arrayOfDouble[2];
/* 168 */         d6 = arrayOfDouble[3];
/* 169 */         Curve.insertQuad(localVector, d3, d4, arrayOfDouble);
/* 170 */         d3 = d5;
/* 171 */         d4 = d6;
/* 172 */         break;
/*     */       case 3:
/* 174 */         d5 = arrayOfDouble[4];
/* 175 */         d6 = arrayOfDouble[5];
/* 176 */         Curve.insertCubic(localVector, d3, d4, arrayOfDouble);
/* 177 */         d3 = d5;
/* 178 */         d4 = d6;
/* 179 */         break;
/*     */       case 4:
/* 181 */         Curve.insertLine(localVector, d3, d4, d1, d2);
/* 182 */         d3 = d1;
/* 183 */         d4 = d2;
/*     */       }
/*     */ 
/* 186 */       paramPathIterator.next();
/*     */     }
/* 188 */     Curve.insertLine(localVector, d3, d4, d1, d2);
/*     */     Object localObject;
/* 190 */     if (i == 0)
/* 191 */       localObject = new AreaOp.EOWindOp();
/*     */     else {
/* 193 */       localObject = new AreaOp.NZWindOp();
/*     */     }
/* 195 */     return ((AreaOp)localObject).calculate(localVector, EmptyCurves);
/*     */   }
/*     */ 
/*     */   public void add(Area paramArea)
/*     */   {
/* 227 */     this.curves = new AreaOp.AddOp().calculate(this.curves, paramArea.curves);
/* 228 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public void subtract(Area paramArea)
/*     */   {
/* 260 */     this.curves = new AreaOp.SubOp().calculate(this.curves, paramArea.curves);
/* 261 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public void intersect(Area paramArea)
/*     */   {
/* 293 */     this.curves = new AreaOp.IntOp().calculate(this.curves, paramArea.curves);
/* 294 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public void exclusiveOr(Area paramArea)
/*     */   {
/* 327 */     this.curves = new AreaOp.XorOp().calculate(this.curves, paramArea.curves);
/* 328 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 337 */     this.curves = new Vector();
/* 338 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 348 */     return this.curves.size() == 0;
/*     */   }
/*     */ 
/*     */   public boolean isPolygonal()
/*     */   {
/* 360 */     Enumeration localEnumeration = this.curves.elements();
/* 361 */     while (localEnumeration.hasMoreElements()) {
/* 362 */       if (((Curve)localEnumeration.nextElement()).getOrder() > 1) {
/* 363 */         return false;
/*     */       }
/*     */     }
/* 366 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isRectangular()
/*     */   {
/* 377 */     int i = this.curves.size();
/* 378 */     if (i == 0) {
/* 379 */       return true;
/*     */     }
/* 381 */     if (i > 3) {
/* 382 */       return false;
/*     */     }
/* 384 */     Curve localCurve1 = (Curve)this.curves.get(1);
/* 385 */     Curve localCurve2 = (Curve)this.curves.get(2);
/* 386 */     if ((localCurve1.getOrder() != 1) || (localCurve2.getOrder() != 1)) {
/* 387 */       return false;
/*     */     }
/* 389 */     if ((localCurve1.getXTop() != localCurve1.getXBot()) || (localCurve2.getXTop() != localCurve2.getXBot())) {
/* 390 */       return false;
/*     */     }
/* 392 */     if ((localCurve1.getYTop() != localCurve2.getYTop()) || (localCurve1.getYBot() != localCurve2.getYBot()))
/*     */     {
/* 394 */       return false;
/*     */     }
/* 396 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isSingular()
/*     */   {
/* 411 */     if (this.curves.size() < 3) {
/* 412 */       return true;
/*     */     }
/* 414 */     Enumeration localEnumeration = this.curves.elements();
/* 415 */     localEnumeration.nextElement();
/* 416 */     while (localEnumeration.hasMoreElements()) {
/* 417 */       if (((Curve)localEnumeration.nextElement()).getOrder() == 0) {
/* 418 */         return false;
/*     */       }
/*     */     }
/* 421 */     return true;
/*     */   }
/*     */ 
/*     */   private void invalidateBounds()
/*     */   {
/* 426 */     this.cachedBounds = null;
/*     */   }
/*     */   private Rectangle2D getCachedBounds() {
/* 429 */     if (this.cachedBounds != null) {
/* 430 */       return this.cachedBounds;
/*     */     }
/* 432 */     Rectangle2D.Double localDouble = new Rectangle2D.Double();
/* 433 */     if (this.curves.size() > 0) {
/* 434 */       Curve localCurve = (Curve)this.curves.get(0);
/*     */ 
/* 436 */       localDouble.setRect(localCurve.getX0(), localCurve.getY0(), 0.0D, 0.0D);
/* 437 */       for (int i = 1; i < this.curves.size(); i++) {
/* 438 */         ((Curve)this.curves.get(i)).enlarge(localDouble);
/*     */       }
/*     */     }
/* 441 */     return this.cachedBounds = localDouble;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 458 */     return getCachedBounds().getBounds2D();
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 478 */     return getCachedBounds().getBounds();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 487 */     return new Area(this);
/*     */   }
/*     */ 
/*     */   public boolean equals(Area paramArea)
/*     */   {
/* 504 */     if (paramArea == this) {
/* 505 */       return true;
/*     */     }
/* 507 */     if (paramArea == null) {
/* 508 */       return false;
/*     */     }
/* 510 */     Vector localVector = new AreaOp.XorOp().calculate(this.curves, paramArea.curves);
/* 511 */     return localVector.isEmpty();
/*     */   }
/*     */ 
/*     */   public void transform(AffineTransform paramAffineTransform)
/*     */   {
/* 523 */     if (paramAffineTransform == null) {
/* 524 */       throw new NullPointerException("transform must not be null");
/*     */     }
/*     */ 
/* 528 */     this.curves = pathToCurves(getPathIterator(paramAffineTransform));
/* 529 */     invalidateBounds();
/*     */   }
/*     */ 
/*     */   public Area createTransformedArea(AffineTransform paramAffineTransform)
/*     */   {
/* 545 */     Area localArea = new Area(this);
/* 546 */     localArea.transform(paramAffineTransform);
/* 547 */     return localArea;
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2)
/*     */   {
/* 555 */     if (!getCachedBounds().contains(paramDouble1, paramDouble2)) {
/* 556 */       return false;
/*     */     }
/* 558 */     Enumeration localEnumeration = this.curves.elements();
/* 559 */     int i = 0;
/* 560 */     while (localEnumeration.hasMoreElements()) {
/* 561 */       Curve localCurve = (Curve)localEnumeration.nextElement();
/* 562 */       i += localCurve.crossingsFor(paramDouble1, paramDouble2);
/*     */     }
/* 564 */     return (i & 0x1) == 1;
/*     */   }
/*     */ 
/*     */   public boolean contains(Point2D paramPoint2D)
/*     */   {
/* 572 */     return contains(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 580 */     if ((paramDouble3 < 0.0D) || (paramDouble4 < 0.0D)) {
/* 581 */       return false;
/*     */     }
/* 583 */     if (!getCachedBounds().contains(paramDouble1, paramDouble2, paramDouble3, paramDouble4)) {
/* 584 */       return false;
/*     */     }
/* 586 */     Crossings localCrossings = Crossings.findCrossings(this.curves, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 587 */     return (localCrossings != null) && (localCrossings.covers(paramDouble2, paramDouble2 + paramDouble4));
/*     */   }
/*     */ 
/*     */   public boolean contains(Rectangle2D paramRectangle2D)
/*     */   {
/* 595 */     return contains(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 603 */     if ((paramDouble3 < 0.0D) || (paramDouble4 < 0.0D)) {
/* 604 */       return false;
/*     */     }
/* 606 */     if (!getCachedBounds().intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4)) {
/* 607 */       return false;
/*     */     }
/* 609 */     Crossings localCrossings = Crossings.findCrossings(this.curves, paramDouble1, paramDouble2, paramDouble1 + paramDouble3, paramDouble2 + paramDouble4);
/* 610 */     return (localCrossings == null) || (!localCrossings.isEmpty());
/*     */   }
/*     */ 
/*     */   public boolean intersects(Rectangle2D paramRectangle2D)
/*     */   {
/* 618 */     return intersects(paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform)
/*     */   {
/* 633 */     return new AreaIterator(this.curves, paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble)
/*     */   {
/* 655 */     return new FlatteningPathIterator(getPathIterator(paramAffineTransform), paramDouble);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Area
 * JD-Core Version:    0.6.2
 */