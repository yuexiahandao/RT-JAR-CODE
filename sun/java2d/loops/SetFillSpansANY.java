/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ class SetFillSpansANY extends FillSpans
/*     */ {
/*     */   SetFillSpansANY()
/*     */   {
/* 719 */     super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void FillSpans(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, SpanIterator paramSpanIterator)
/*     */   {
/* 727 */     PixelWriter localPixelWriter = GeneralRenderer.createSolidPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 729 */     int[] arrayOfInt = new int[4];
/* 730 */     while (paramSpanIterator.nextSpan(arrayOfInt))
/* 731 */       GeneralRenderer.doSetRect(paramSurfaceData, localPixelWriter, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.SetFillSpansANY
 * JD-Core Version:    0.6.2
 */