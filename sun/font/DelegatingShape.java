/*    */ package sun.font;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.Shape;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.PathIterator;
/*    */ import java.awt.geom.Point2D;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ 
/*    */ public final class DelegatingShape
/*    */   implements Shape
/*    */ {
/*    */   Shape delegate;
/*    */ 
/*    */   public DelegatingShape(Shape paramShape)
/*    */   {
/* 44 */     this.delegate = paramShape;
/*    */   }
/*    */ 
/*    */   public Rectangle getBounds() {
/* 48 */     return this.delegate.getBounds();
/*    */   }
/*    */ 
/*    */   public Rectangle2D getBounds2D() {
/* 52 */     return this.delegate.getBounds2D();
/*    */   }
/*    */ 
/*    */   public boolean contains(double paramDouble1, double paramDouble2) {
/* 56 */     return this.delegate.contains(paramDouble1, paramDouble2);
/*    */   }
/*    */ 
/*    */   public boolean contains(Point2D paramPoint2D) {
/* 60 */     return this.delegate.contains(paramPoint2D);
/*    */   }
/*    */ 
/*    */   public boolean intersects(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 64 */     return this.delegate.intersects(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*    */   }
/*    */ 
/*    */   public boolean intersects(Rectangle2D paramRectangle2D) {
/* 68 */     return this.delegate.intersects(paramRectangle2D);
/*    */   }
/*    */ 
/*    */   public boolean contains(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 72 */     return this.delegate.contains(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*    */   }
/*    */ 
/*    */   public boolean contains(Rectangle2D paramRectangle2D) {
/* 76 */     return this.delegate.contains(paramRectangle2D);
/*    */   }
/*    */ 
/*    */   public PathIterator getPathIterator(AffineTransform paramAffineTransform) {
/* 80 */     return this.delegate.getPathIterator(paramAffineTransform);
/*    */   }
/*    */ 
/*    */   public PathIterator getPathIterator(AffineTransform paramAffineTransform, double paramDouble) {
/* 84 */     return this.delegate.getPathIterator(paramAffineTransform, paramDouble);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.DelegatingShape
 * JD-Core Version:    0.6.2
 */