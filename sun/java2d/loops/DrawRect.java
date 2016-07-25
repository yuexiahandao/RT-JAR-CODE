/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DrawRect extends GraphicsPrimitive
/*     */ {
/*  47 */   public static final String methodSignature = "DrawRect(...)".toString();
/*     */ 
/*  49 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawRect locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  55 */     return (DrawRect)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawRect(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  64 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawRect(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  72 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  86 */     throw new InternalError("DrawRect not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  91 */     return new TraceDrawRect(this);
/*     */   }
/*     */ 
/*     */   private static class TraceDrawRect extends DrawRect {
/*     */     DrawRect target;
/*     */ 
/*     */     public TraceDrawRect(DrawRect paramDrawRect) {
/*  98 */       super(paramDrawRect.getCompositeType(), paramDrawRect.getDestType());
/*     */ 
/* 101 */       this.target = paramDrawRect;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 105 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 111 */       tracePrimitive(this.target);
/* 112 */       this.target.DrawRect(paramSunGraphics2D, paramSurfaceData, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawRect
 * JD-Core Version:    0.6.2
 */