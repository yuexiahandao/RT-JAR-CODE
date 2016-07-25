/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class XorFillRectANY extends FillRect
/*     */ {
/*     */   XorFillRectANY()
/*     */   {
/* 826 */     super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void FillRect(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 834 */     PixelWriter localPixelWriter = GeneralRenderer.createXorPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 836 */     Region localRegion = paramSunGraphics2D.getCompClip().getBoundsIntersectionXYWH(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 838 */     GeneralRenderer.doSetRect(paramSurfaceData, localPixelWriter, localRegion.getLoX(), localRegion.getLoY(), localRegion.getHiX(), localRegion.getHiY());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorFillRectANY
 * JD-Core Version:    0.6.2
 */