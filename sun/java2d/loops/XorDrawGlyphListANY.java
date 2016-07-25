/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ class XorDrawGlyphListANY extends DrawGlyphList
/*     */ {
/*     */   XorDrawGlyphListANY()
/*     */   {
/* 970 */     super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void DrawGlyphList(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList)
/*     */   {
/* 978 */     PixelWriter localPixelWriter = GeneralRenderer.createXorPixelWriter(paramSunGraphics2D, paramSurfaceData);
/* 979 */     GeneralRenderer.doDrawGlyphList(paramSurfaceData, localPixelWriter, paramGlyphList, paramSunGraphics2D.getCompClip());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorDrawGlyphListANY
 * JD-Core Version:    0.6.2
 */