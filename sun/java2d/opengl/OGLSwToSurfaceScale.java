/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.ScaledBlit;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class OGLSwToSurfaceScale extends ScaledBlit
/*     */ {
/*     */   private int typeval;
/*     */ 
/*     */   OGLSwToSurfaceScale(SurfaceType paramSurfaceType, int paramInt)
/*     */   {
/* 581 */     super(paramSurfaceType, CompositeType.AnyAlpha, OGLSurfaceData.OpenGLSurface);
/*     */ 
/* 584 */     this.typeval = paramInt;
/*     */   }
/*     */ 
/*     */   public void Scale(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 594 */     OGLBlitLoops.Blit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, null, 1, paramInt1, paramInt2, paramInt3, paramInt4, paramDouble1, paramDouble2, paramDouble3, paramDouble4, this.typeval, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLSwToSurfaceScale
 * JD-Core Version:    0.6.2
 */