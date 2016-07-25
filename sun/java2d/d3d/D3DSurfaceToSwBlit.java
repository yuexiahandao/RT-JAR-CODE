/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ 
/*     */ class D3DSurfaceToSwBlit extends Blit
/*     */ {
/*     */   private int typeval;
/*     */ 
/*     */   D3DSurfaceToSwBlit(SurfaceType paramSurfaceType, int paramInt)
/*     */   {
/* 502 */     super(D3DSurfaceData.D3DSurface, CompositeType.SrcNoEa, paramSurfaceType);
/*     */ 
/* 505 */     this.typeval = paramInt;
/*     */   }
/*     */ 
/*     */   public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 513 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 514 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 519 */       localD3DRenderQueue.addReference(paramSurfaceData2);
/*     */ 
/* 521 */       RenderBuffer localRenderBuffer = localD3DRenderQueue.getBuffer();
/* 522 */       D3DContext.setScratchSurface(((D3DSurfaceData)paramSurfaceData1).getContext());
/*     */ 
/* 524 */       localD3DRenderQueue.ensureCapacityAndAlignment(48, 32);
/* 525 */       localRenderBuffer.putInt(34);
/* 526 */       localRenderBuffer.putInt(paramInt1).putInt(paramInt2);
/* 527 */       localRenderBuffer.putInt(paramInt3).putInt(paramInt4);
/* 528 */       localRenderBuffer.putInt(paramInt5).putInt(paramInt6);
/* 529 */       localRenderBuffer.putInt(this.typeval);
/* 530 */       localRenderBuffer.putLong(paramSurfaceData1.getNativeOps());
/* 531 */       localRenderBuffer.putLong(paramSurfaceData2.getNativeOps());
/*     */ 
/* 534 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 536 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DSurfaceToSwBlit
 * JD-Core Version:    0.6.2
 */