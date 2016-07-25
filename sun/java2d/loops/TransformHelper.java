/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class TransformHelper extends GraphicsPrimitive
/*     */ {
/*  48 */   public static final String methodSignature = "TransformHelper(...)".toString();
/*     */ 
/*  51 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*  53 */   private static RenderCache helpercache = new RenderCache(10);
/*     */ 
/*     */   public static TransformHelper locate(SurfaceType paramSurfaceType) {
/*  56 */     return (TransformHelper)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType, CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
/*     */   }
/*     */ 
/*     */   public static synchronized TransformHelper getFromCache(SurfaceType paramSurfaceType)
/*     */   {
/*  64 */     Object localObject = helpercache.get(paramSurfaceType, null, null);
/*  65 */     if (localObject != null) {
/*  66 */       return (TransformHelper)localObject;
/*     */     }
/*  68 */     TransformHelper localTransformHelper = locate(paramSurfaceType);
/*  69 */     if (localTransformHelper != null)
/*     */     {
/*  75 */       helpercache.put(paramSurfaceType, null, null, localTransformHelper);
/*     */     }
/*  77 */     return localTransformHelper;
/*     */   }
/*     */ 
/*     */   protected TransformHelper(SurfaceType paramSurfaceType) {
/*  81 */     super(methodSignature, primTypeID, paramSurfaceType, CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
/*     */   }
/*     */ 
/*     */   public TransformHelper(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  90 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void Transform(MaskBlit paramMaskBlit, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int[] paramArrayOfInt, int paramInt10, int paramInt11);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap() {
/* 110 */     return new TraceTransformHelper(this);
/*     */   }
/*     */ 
/*     */   private static class TraceTransformHelper extends TransformHelper {
/*     */     TransformHelper target;
/*     */ 
/*     */     public TraceTransformHelper(TransformHelper paramTransformHelper) {
/* 117 */       super();
/* 118 */       this.target = paramTransformHelper;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 122 */       return this;
/*     */     }
/*     */ 
/*     */     public void Transform(MaskBlit paramMaskBlit, SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, AffineTransform paramAffineTransform, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int[] paramArrayOfInt, int paramInt10, int paramInt11)
/*     */     {
/* 133 */       tracePrimitive(this.target);
/* 134 */       this.target.Transform(paramMaskBlit, paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramAffineTransform, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramArrayOfInt, paramInt10, paramInt11);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.TransformHelper
 * JD-Core Version:    0.6.2
 */