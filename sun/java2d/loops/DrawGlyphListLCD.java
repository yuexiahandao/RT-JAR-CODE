/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class DrawGlyphListLCD extends GraphicsPrimitive
/*     */ {
/*  43 */   public static final String methodSignature = "DrawGlyphListLCD(...)".toString();
/*     */ 
/*  45 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawGlyphListLCD locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  51 */     return (DrawGlyphListLCD)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawGlyphListLCD(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  60 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawGlyphListLCD(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  68 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawGlyphListLCD(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap() {
/*  93 */     return new TraceDrawGlyphListLCD(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  76 */     GraphicsPrimitiveMgr.registerGeneral(new DrawGlyphListLCD(null, null, null));
/*     */   }
/*     */ 
/*     */   private static class TraceDrawGlyphListLCD extends DrawGlyphListLCD
/*     */   {
/*     */     DrawGlyphListLCD target;
/*     */ 
/*     */     public TraceDrawGlyphListLCD(DrawGlyphListLCD paramDrawGlyphListLCD)
/*     */     {
/* 100 */       super(paramDrawGlyphListLCD.getCompositeType(), paramDrawGlyphListLCD.getDestType());
/*     */ 
/* 103 */       this.target = paramDrawGlyphListLCD;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 107 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawGlyphListLCD(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList)
/*     */     {
/* 113 */       tracePrimitive(this.target);
/* 114 */       this.target.DrawGlyphListLCD(paramSunGraphics2D, paramSurfaceData, paramGlyphList);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawGlyphListLCD
 * JD-Core Version:    0.6.2
 */