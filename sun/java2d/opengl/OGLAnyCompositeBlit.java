/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class OGLAnyCompositeBlit extends Blit
/*     */ {
/*     */   private WeakReference<SurfaceData> dstTmp;
/*     */ 
/*     */   public OGLAnyCompositeBlit(SurfaceType paramSurfaceType)
/*     */   {
/* 774 */     super(SurfaceType.Any, CompositeType.Any, paramSurfaceType);
/*     */   }
/*     */ 
/*     */   public synchronized void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 781 */     Blit localBlit1 = Blit.getFromCache(paramSurfaceData2.getSurfaceType(), CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
/*     */ 
/* 785 */     SurfaceData localSurfaceData1 = null;
/*     */ 
/* 787 */     if (this.dstTmp != null)
/*     */     {
/* 789 */       localSurfaceData1 = (SurfaceData)this.dstTmp.get();
/*     */     }
/*     */ 
/* 793 */     SurfaceData localSurfaceData2 = convertFrom(localBlit1, paramSurfaceData2, paramInt3, paramInt4, paramInt5, paramInt6, localSurfaceData1, 3);
/*     */ 
/* 796 */     Blit localBlit2 = Blit.getFromCache(paramSurfaceData1.getSurfaceType(), CompositeType.Any, localSurfaceData2.getSurfaceType());
/*     */ 
/* 799 */     localBlit2.Blit(paramSurfaceData1, localSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, 0, 0, paramInt5, paramInt6);
/*     */ 
/* 802 */     if (localSurfaceData2 != localSurfaceData1)
/*     */     {
/* 804 */       this.dstTmp = new WeakReference(localSurfaceData2);
/*     */     }
/*     */ 
/* 808 */     localBlit1 = Blit.getFromCache(localSurfaceData2.getSurfaceType(), CompositeType.SrcNoEa, paramSurfaceData2.getSurfaceType());
/*     */ 
/* 811 */     localBlit1.Blit(localSurfaceData2, paramSurfaceData2, AlphaComposite.Src, paramRegion, 0, 0, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLAnyCompositeBlit
 * JD-Core Version:    0.6.2
 */