/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class FillPath extends GraphicsPrimitive
/*     */ {
/*  41 */   public static final String methodSignature = "FillPath(...)".toString();
/*     */ 
/*  44 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static FillPath locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  50 */     return (FillPath)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected FillPath(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  59 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public FillPath(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  68 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void FillPath(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, Path2D.Float paramFloat);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  84 */     throw new InternalError("FillPath not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  89 */     return new TraceFillPath(this);
/*     */   }
/*     */ 
/*     */   private static class TraceFillPath extends FillPath {
/*     */     FillPath target;
/*     */ 
/*     */     public TraceFillPath(FillPath paramFillPath) {
/*  96 */       super(paramFillPath.getCompositeType(), paramFillPath.getDestType());
/*     */ 
/*  99 */       this.target = paramFillPath;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 103 */       return this;
/*     */     }
/*     */ 
/*     */     public void FillPath(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, Path2D.Float paramFloat)
/*     */     {
/* 110 */       tracePrimitive(this.target);
/* 111 */       this.target.FillPath(paramSunGraphics2D, paramSurfaceData, paramInt1, paramInt2, paramFloat);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.FillPath
 * JD-Core Version:    0.6.2
 */