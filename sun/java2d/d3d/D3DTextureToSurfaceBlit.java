/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class D3DTextureToSurfaceBlit extends Blit
/*     */ {
/*     */   D3DTextureToSurfaceBlit()
/*     */   {
/* 643 */     super(D3DSurfaceData.D3DTexture, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
/*     */   }
/*     */ 
/*     */   public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 652 */     D3DBlitLoops.IsoBlit(paramSurfaceData1, paramSurfaceData2, null, null, paramComposite, paramRegion, null, 1, paramInt1, paramInt2, paramInt1 + paramInt5, paramInt2 + paramInt6, paramInt3, paramInt4, paramInt3 + paramInt5, paramInt4 + paramInt6, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DTextureToSurfaceBlit
 * JD-Core Version:    0.6.2
 */