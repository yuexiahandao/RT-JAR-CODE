/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ class D3DGeneralBlit extends Blit
/*     */ {
/*     */   private Blit performop;
/*     */   private WeakReference srcTmp;
/*     */ 
/*     */   D3DGeneralBlit(SurfaceType paramSurfaceType, CompositeType paramCompositeType, Blit paramBlit)
/*     */   {
/* 725 */     super(SurfaceType.Any, paramCompositeType, paramSurfaceType);
/* 726 */     this.performop = paramBlit;
/*     */   }
/*     */ 
/*     */   public synchronized void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 734 */     Blit localBlit = Blit.getFromCache(paramSurfaceData1.getSurfaceType(), CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
/*     */ 
/* 738 */     SurfaceData localSurfaceData = null;
/* 739 */     if (this.srcTmp != null)
/*     */     {
/* 741 */       localSurfaceData = (SurfaceData)this.srcTmp.get();
/*     */     }
/*     */ 
/* 745 */     paramSurfaceData1 = convertFrom(localBlit, paramSurfaceData1, paramInt1, paramInt2, paramInt5, paramInt6, localSurfaceData, 3);
/*     */ 
/* 749 */     this.performop.Blit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, 0, 0, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 752 */     if (paramSurfaceData1 != localSurfaceData)
/*     */     {
/* 754 */       this.srcTmp = new WeakReference(paramSurfaceData1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DGeneralBlit
 * JD-Core Version:    0.6.2
 */