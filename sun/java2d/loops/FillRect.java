/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class FillRect extends GraphicsPrimitive
/*     */ {
/*  47 */   public static final String methodSignature = "FillRect(...)".toString();
/*     */ 
/*  49 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static FillRect locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  55 */     return (FillRect)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected FillRect(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  64 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public FillRect(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  72 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void FillRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  89 */     return new General(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 111 */     return new TraceFillRect(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  82 */     GraphicsPrimitiveMgr.registerGeneral(new FillRect(null, null, null));
/*     */   }
/*     */ 
/*     */   public static class General extends FillRect
/*     */   {
/*     */     public MaskFill fillop;
/*     */ 
/*     */     public General(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/*  99 */       super(paramCompositeType, paramSurfaceType2);
/* 100 */       this.fillop = MaskFill.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void FillRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 106 */       this.fillop.MaskFill(paramSunGraphics2D, paramSurfaceData, paramSunGraphics2D.composite, paramInt1, paramInt2, paramInt3, paramInt4, null, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceFillRect extends FillRect
/*     */   {
/*     */     FillRect target;
/*     */ 
/*     */     public TraceFillRect(FillRect paramFillRect)
/*     */     {
/* 118 */       super(paramFillRect.getCompositeType(), paramFillRect.getDestType());
/*     */ 
/* 121 */       this.target = paramFillRect;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 125 */       return this;
/*     */     }
/*     */ 
/*     */     public void FillRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 131 */       tracePrimitive(this.target);
/* 132 */       this.target.FillRect(paramSunGraphics2D, paramSurfaceData, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.FillRect
 * JD-Core Version:    0.6.2
 */