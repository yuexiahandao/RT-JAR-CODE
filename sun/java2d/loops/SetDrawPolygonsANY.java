/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class SetDrawPolygonsANY extends DrawPolygons
/*     */ {
/*     */   SetDrawPolygonsANY()
/*     */   {
/* 763 */     super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void DrawPolygons(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 774 */     PixelWriter localPixelWriter = GeneralRenderer.createSolidPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 776 */     int i = 0;
/* 777 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 778 */     for (int j = 0; j < paramInt1; j++) {
/* 779 */       int k = paramArrayOfInt3[j];
/* 780 */       GeneralRenderer.doDrawPoly(paramSurfaceData, localPixelWriter, paramArrayOfInt1, paramArrayOfInt2, i, k, localRegion, paramInt2, paramInt3, paramBoolean);
/*     */ 
/* 783 */       i += k;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.SetDrawPolygonsANY
 * JD-Core Version:    0.6.2
 */