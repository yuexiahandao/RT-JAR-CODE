/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class TransformBlit extends GraphicsPrimitive
/*     */ {
/*  49 */   public static final String methodSignature = "TransformBlit(...)".toString();
/*     */ 
/*  52 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  54 */   private static RenderCache blitcache = new RenderCache(10);
/*     */ 
/*     */   public static TransformBlit locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  60 */     return (TransformBlit)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public static TransformBlit getFromCache(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  69 */     Object localObject = blitcache.get(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  70 */     if (localObject != null) {
/*  71 */       return (TransformBlit)localObject;
/*     */     }
/*  73 */     TransformBlit localTransformBlit = locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  74 */     if (localTransformBlit != null)
/*     */     {
/*  82 */       blitcache.put(paramSurfaceType1, paramCompositeType, paramSurfaceType2, localTransformBlit);
/*     */     }
/*  84 */     return localTransformBlit;
/*     */   }
/*     */ 
/*     */   protected TransformBlit(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  91 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public TransformBlit(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  99 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void Transform(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap() {
/* 129 */     return new TraceTransformBlit(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 111 */     GraphicsPrimitiveMgr.registerGeneral(new TransformBlit(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class TraceTransformBlit extends TransformBlit
/*     */   {
/*     */     TransformBlit target;
/*     */ 
/*     */     public TraceTransformBlit(TransformBlit paramTransformBlit)
/*     */     {
/* 136 */       super(paramTransformBlit.getCompositeType(), paramTransformBlit.getDestType());
/*     */ 
/* 139 */       this.target = paramTransformBlit;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     public void Transform(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 152 */       tracePrimitive(this.target);
/* 153 */       this.target.Transform(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.TransformBlit
 * JD-Core Version:    0.6.2
 */