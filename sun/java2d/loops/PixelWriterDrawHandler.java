/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class PixelWriterDrawHandler extends ProcessPath.DrawHandler
/*     */ {
/*     */   PixelWriter pw;
/*     */   SurfaceData sData;
/*     */   Region clip;
/*     */ 
/*     */   public PixelWriterDrawHandler(SurfaceData paramSurfaceData, PixelWriter paramPixelWriter, Region paramRegion, int paramInt)
/*     */   {
/* 677 */     super(paramRegion.getLoX(), paramRegion.getLoY(), paramRegion.getHiX(), paramRegion.getHiY(), paramInt);
/*     */ 
/* 680 */     this.sData = paramSurfaceData;
/* 681 */     this.pw = paramPixelWriter;
/* 682 */     this.clip = paramRegion;
/*     */   }
/*     */ 
/*     */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 686 */     GeneralRenderer.doDrawLine(this.sData, this.pw, null, this.clip, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawPixel(int paramInt1, int paramInt2)
/*     */   {
/* 691 */     GeneralRenderer.doSetRect(this.sData, this.pw, paramInt1, paramInt2, paramInt1 + 1, paramInt2 + 1);
/*     */   }
/*     */ 
/*     */   public void drawScanline(int paramInt1, int paramInt2, int paramInt3) {
/* 695 */     GeneralRenderer.doSetRect(this.sData, this.pw, paramInt1, paramInt3, paramInt2 + 1, paramInt3 + 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.PixelWriterDrawHandler
 * JD-Core Version:    0.6.2
 */