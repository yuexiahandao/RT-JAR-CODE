/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DrawPath extends GraphicsPrimitive
/*     */ {
/*  41 */   public static final String methodSignature = "DrawPath(...)".toString();
/*     */ 
/*  44 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawPath locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  50 */     return (DrawPath)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawPath(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  59 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawPath(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  68 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawPath(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, Path2D.Float paramFloat);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  84 */     throw new InternalError("DrawPath not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  89 */     return new TraceDrawPath(this);
/*     */   }
/*     */ 
/*     */   private static class TraceDrawPath extends DrawPath {
/*     */     DrawPath target;
/*     */ 
/*     */     public TraceDrawPath(DrawPath paramDrawPath) {
/*  96 */       super(paramDrawPath.getCompositeType(), paramDrawPath.getDestType());
/*     */ 
/*  99 */       this.target = paramDrawPath;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 103 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawPath(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, Path2D.Float paramFloat)
/*     */     {
/* 110 */       tracePrimitive(this.target);
/* 111 */       this.target.DrawPath(paramSunGraphics2D, paramSurfaceData, paramInt1, paramInt2, paramFloat);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawPath
 * JD-Core Version:    0.6.2
 */