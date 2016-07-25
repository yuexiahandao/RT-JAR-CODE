/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DrawLine extends GraphicsPrimitive
/*     */ {
/*  47 */   public static final String methodSignature = "DrawLine(...)".toString();
/*     */ 
/*  49 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawLine locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  55 */     return (DrawLine)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawLine(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  63 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawLine(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  71 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawLine(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  85 */     throw new InternalError("DrawLine not implemented for " + paramSurfaceType1 + " with " + paramCompositeType);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/*  90 */     return new TraceDrawLine(this);
/*     */   }
/*     */ 
/*     */   private static class TraceDrawLine extends DrawLine {
/*     */     DrawLine target;
/*     */ 
/*     */     public TraceDrawLine(DrawLine paramDrawLine) {
/*  97 */       super(paramDrawLine.getCompositeType(), paramDrawLine.getDestType());
/*     */ 
/* 100 */       this.target = paramDrawLine;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 104 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawLine(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 110 */       tracePrimitive(this.target);
/* 111 */       this.target.DrawLine(paramSunGraphics2D, paramSurfaceData, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawLine
 * JD-Core Version:    0.6.2
 */