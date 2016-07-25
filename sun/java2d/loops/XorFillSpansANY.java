/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ class XorFillSpansANY extends FillSpans
/*     */ {
/*     */   XorFillSpansANY()
/*     */   {
/* 864 */     super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void FillSpans(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, SpanIterator paramSpanIterator)
/*     */   {
/* 872 */     PixelWriter localPixelWriter = GeneralRenderer.createXorPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 874 */     int[] arrayOfInt = new int[4];
/* 875 */     while (paramSpanIterator.nextSpan(arrayOfInt))
/* 876 */       GeneralRenderer.doSetRect(paramSurfaceData, localPixelWriter, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorFillSpansANY
 * JD-Core Version:    0.6.2
 */