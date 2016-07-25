/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ public class FillSpans extends GraphicsPrimitive
/*     */ {
/*  48 */   public static final String methodSignature = "FillSpans(...)".toString();
/*     */ 
/*  50 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static FillSpans locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  56 */     return (FillSpans)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected FillSpans(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  65 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public FillSpans(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  73 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   private native void FillSpans(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt, long paramLong, SpanIterator paramSpanIterator);
/*     */ 
/*     */   public void FillSpans(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, SpanIterator paramSpanIterator)
/*     */   {
/*  85 */     FillSpans(paramSunGraphics2D, paramSurfaceData, paramSunGraphics2D.pixel, paramSpanIterator.getNativeIterator(), paramSpanIterator);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  93 */     throw new InternalError("FillSpans not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  98 */     return new TraceFillSpans(this);
/*     */   }
/*     */ 
/*     */   private static class TraceFillSpans extends FillSpans {
/*     */     FillSpans target;
/*     */ 
/*     */     public TraceFillSpans(FillSpans paramFillSpans) {
/* 105 */       super(paramFillSpans.getCompositeType(), paramFillSpans.getDestType());
/*     */ 
/* 108 */       this.target = paramFillSpans;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 112 */       return this;
/*     */     }
/*     */ 
/*     */     public void FillSpans(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, SpanIterator paramSpanIterator)
/*     */     {
/* 118 */       tracePrimitive(this.target);
/* 119 */       this.target.FillSpans(paramSunGraphics2D, paramSurfaceData, paramSpanIterator);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.FillSpans
 * JD-Core Version:    0.6.2
 */