/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class FillParallelogram extends GraphicsPrimitive
/*     */ {
/*  43 */   public static final String methodSignature = "FillParallelogram(...)".toString();
/*     */ 
/*  46 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static FillParallelogram locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  52 */     return (FillParallelogram)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected FillParallelogram(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  61 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public FillParallelogram(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  70 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void FillParallelogram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  87 */     throw new InternalError("FillParallelogram not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  92 */     return new TraceFillParallelogram(this);
/*     */   }
/*     */ 
/*     */   private static class TraceFillParallelogram extends FillParallelogram {
/*     */     FillParallelogram target;
/*     */ 
/*     */     public TraceFillParallelogram(FillParallelogram paramFillParallelogram) {
/*  99 */       super(paramFillParallelogram.getCompositeType(), paramFillParallelogram.getDestType());
/*     */ 
/* 102 */       this.target = paramFillParallelogram;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */     public void FillParallelogram(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
/*     */     {
/* 114 */       tracePrimitive(this.target);
/* 115 */       this.target.FillParallelogram(paramSunGraphics2D, paramSurfaceData, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.FillParallelogram
 * JD-Core Version:    0.6.2
 */