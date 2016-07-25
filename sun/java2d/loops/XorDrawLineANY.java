/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ class XorDrawLineANY extends DrawLine
/*     */ {
/*     */   XorDrawLineANY()
/*     */   {
/* 884 */     super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void DrawLine(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 892 */     PixelWriter localPixelWriter = GeneralRenderer.createXorPixelWriter(paramSunGraphics2D, paramSurfaceData);
/*     */ 
/* 894 */     if (paramInt2 >= paramInt4) {
/* 895 */       GeneralRenderer.doDrawLine(paramSurfaceData, localPixelWriter, null, paramSunGraphics2D.getCompClip(), paramInt3, paramInt4, paramInt1, paramInt2);
/*     */     }
/*     */     else
/*     */     {
/* 899 */       GeneralRenderer.doDrawLine(paramSurfaceData, localPixelWriter, null, paramSunGraphics2D.getCompClip(), paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorDrawLineANY
 * JD-Core Version:    0.6.2
 */