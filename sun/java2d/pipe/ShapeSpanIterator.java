/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.PathIterator;
/*     */ import sun.awt.geom.PathConsumer2D;
/*     */ 
/*     */ public final class ShapeSpanIterator
/*     */   implements SpanIterator, PathConsumer2D
/*     */ {
/*     */   long pData;
/*     */ 
/*     */   public static native void initIDs();
/*     */ 
/*     */   public ShapeSpanIterator(boolean paramBoolean)
/*     */   {
/*  77 */     setNormalize(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void appendPath(PathIterator paramPathIterator)
/*     */   {
/*  85 */     float[] arrayOfFloat = new float[6];
/*     */ 
/*  87 */     setRule(paramPathIterator.getWindingRule());
/*  88 */     while (!paramPathIterator.isDone()) {
/*  89 */       addSegment(paramPathIterator.currentSegment(arrayOfFloat), arrayOfFloat);
/*  90 */       paramPathIterator.next();
/*     */     }
/*  92 */     pathDone();
/*     */   }
/*     */ 
/*     */   public native void appendPoly(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   private native void setNormalize(boolean paramBoolean);
/*     */ 
/*     */   public void setOutputAreaXYWH(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 112 */     setOutputAreaXYXY(paramInt1, paramInt2, Region.dimAdd(paramInt1, paramInt3), Region.dimAdd(paramInt2, paramInt4));
/*     */   }
/*     */ 
/*     */   public native void setOutputAreaXYXY(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public void setOutputArea(Rectangle paramRectangle)
/*     */   {
/* 126 */     setOutputAreaXYWH(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   public void setOutputArea(Region paramRegion)
/*     */   {
/* 134 */     setOutputAreaXYXY(paramRegion.lox, paramRegion.loy, paramRegion.hix, paramRegion.hiy);
/*     */   }
/*     */ 
/*     */   public native void setRule(int paramInt);
/*     */ 
/*     */   public native void addSegment(int paramInt, float[] paramArrayOfFloat);
/*     */ 
/*     */   public native void getPathBox(int[] paramArrayOfInt);
/*     */ 
/*     */   public native void intersectClipBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public native boolean nextSpan(int[] paramArrayOfInt);
/*     */ 
/*     */   public native void skipDownTo(int paramInt);
/*     */ 
/*     */   public native long getNativeIterator();
/*     */ 
/*     */   public native void dispose();
/*     */ 
/*     */   public native void moveTo(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public native void lineTo(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public native void quadTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*     */ 
/*     */   public native void curveTo(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);
/*     */ 
/*     */   public native void closePath();
/*     */ 
/*     */   public native void pathDone();
/*     */ 
/*     */   public native long getNativeConsumer();
/*     */ 
/*     */   static
/*     */   {
/*  71 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.ShapeSpanIterator
 * JD-Core Version:    0.6.2
 */