/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ 
/*     */ class OGLSurfaceToSwBlit extends Blit
/*     */ {
/*     */   private int typeval;
/*     */ 
/*     */   OGLSurfaceToSwBlit(SurfaceType paramSurfaceType, int paramInt)
/*     */   {
/* 513 */     super(OGLSurfaceData.OpenGLSurface, CompositeType.SrcNoEa, paramSurfaceType);
/*     */ 
/* 516 */     this.typeval = paramInt;
/*     */   }
/*     */ 
/*     */   public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 524 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 525 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 530 */       localOGLRenderQueue.addReference(paramSurfaceData2);
/*     */ 
/* 532 */       RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/* 533 */       OGLContext.validateContext((OGLSurfaceData)paramSurfaceData1);
/*     */ 
/* 535 */       localOGLRenderQueue.ensureCapacityAndAlignment(48, 32);
/* 536 */       localRenderBuffer.putInt(34);
/* 537 */       localRenderBuffer.putInt(paramInt1).putInt(paramInt2);
/* 538 */       localRenderBuffer.putInt(paramInt3).putInt(paramInt4);
/* 539 */       localRenderBuffer.putInt(paramInt5).putInt(paramInt6);
/* 540 */       localRenderBuffer.putInt(this.typeval);
/* 541 */       localRenderBuffer.putLong(paramSurfaceData1.getNativeOps());
/* 542 */       localRenderBuffer.putLong(paramSurfaceData2.getNativeOps());
/*     */ 
/* 545 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 547 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLSurfaceToSwBlit
 * JD-Core Version:    0.6.2
 */