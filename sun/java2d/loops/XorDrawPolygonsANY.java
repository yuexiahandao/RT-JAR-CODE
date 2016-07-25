/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class XorDrawPolygonsANY extends DrawPolygons
/*     */ {
/*     */   XorDrawPolygonsANY()
/*     */   {
/* 908 */     super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void DrawPolygons(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 919 */     PixelWriter localPixelWriter = GeneralRenderer.createXorPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 921 */     int i = 0;
/* 922 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 923 */     for (int j = 0; j < paramInt1; j++) {
/* 924 */       int k = paramArrayOfInt3[j];
/* 925 */       GeneralRenderer.doDrawPoly(paramSurfaceData, localPixelWriter, paramArrayOfInt1, paramArrayOfInt2, i, k, localRegion, paramInt2, paramInt3, paramBoolean);
/*     */ 
/* 928 */       i += k;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorDrawPolygonsANY
 * JD-Core Version:    0.6.2
 */