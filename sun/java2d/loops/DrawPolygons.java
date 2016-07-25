/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DrawPolygons extends GraphicsPrimitive
/*     */ {
/*  44 */   public static final String methodSignature = "DrawPolygons(...)".toString();
/*     */ 
/*  46 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawPolygons locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  52 */     return (DrawPolygons)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawPolygons(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  61 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawPolygons(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  69 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawPolygons(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  86 */     throw new InternalError("DrawPolygons not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  91 */     return new TraceDrawPolygons(this);
/*     */   }
/*     */ 
/*     */   private static class TraceDrawPolygons extends DrawPolygons {
/*     */     DrawPolygons target;
/*     */ 
/*     */     public TraceDrawPolygons(DrawPolygons paramDrawPolygons) {
/*  98 */       super(paramDrawPolygons.getCompositeType(), paramDrawPolygons.getDestType());
/*     */ 
/* 101 */       this.target = paramDrawPolygons;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 105 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawPolygons(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */     {
/* 114 */       tracePrimitive(this.target);
/* 115 */       this.target.DrawPolygons(paramSunGraphics2D, paramSurfaceData, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramInt1, paramInt2, paramInt3, paramBoolean);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawPolygons
 * JD-Core Version:    0.6.2
 */