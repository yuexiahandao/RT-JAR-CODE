/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ class SetDrawLineANY extends DrawLine
/*     */ {
/*     */   SetDrawLineANY()
/*     */   {
/* 739 */     super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void DrawLine(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 747 */     PixelWriter localPixelWriter = GeneralRenderer.createSolidPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 749 */     if (paramInt2 >= paramInt4) {
/* 750 */       GeneralRenderer.doDrawLine(paramSurfaceData, localPixelWriter, null, paramSunGraphics2D.getCompClip(), paramInt3, paramInt4, paramInt1, paramInt2);
/*     */     }
/*     */     else
/*     */     {
/* 754 */       GeneralRenderer.doDrawLine(paramSurfaceData, localPixelWriter, null, paramSunGraphics2D.getCompClip(), paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.SetDrawLineANY
 * JD-Core Version:    0.6.2
 */