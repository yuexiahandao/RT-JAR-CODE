/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.Blit;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.MaskBlit;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ 
/*     */ public abstract class BufferedMaskBlit extends MaskBlit
/*     */ {
/*     */   private static final int ST_INT_ARGB = 0;
/*     */   private static final int ST_INT_ARGB_PRE = 1;
/*     */   private static final int ST_INT_RGB = 2;
/*     */   private static final int ST_INT_BGR = 3;
/*     */   private final RenderQueue rq;
/*     */   private final int srcTypeVal;
/*     */   private Blit blitop;
/*     */ 
/*     */   protected BufferedMaskBlit(RenderQueue paramRenderQueue, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  75 */     super(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  76 */     this.rq = paramRenderQueue;
/*  77 */     if (paramSurfaceType1 == SurfaceType.IntArgb)
/*  78 */       this.srcTypeVal = 0;
/*  79 */     else if (paramSurfaceType1 == SurfaceType.IntArgbPre)
/*  80 */       this.srcTypeVal = 1;
/*  81 */     else if (paramSurfaceType1 == SurfaceType.IntRgb)
/*  82 */       this.srcTypeVal = 2;
/*  83 */     else if (paramSurfaceType1 == SurfaceType.IntBgr)
/*  84 */       this.srcTypeVal = 3;
/*     */     else
/*  86 */       throw new InternalError("unrecognized source surface type");
/*     */   }
/*     */ 
/*     */   public void MaskBlit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte, int paramInt7, int paramInt8)
/*     */   {
/*  98 */     if ((paramInt5 <= 0) || (paramInt6 <= 0)) {
/*  99 */       return;
/*     */     }
/*     */ 
/* 102 */     if (paramArrayOfByte == null)
/*     */     {
/* 104 */       if (this.blitop == null) {
/* 105 */         this.blitop = Blit.getFromCache(paramSurfaceData1.getSurfaceType(), CompositeType.AnyAlpha, getDestType());
/*     */       }
/*     */ 
/* 109 */       this.blitop.Blit(paramSurfaceData1, paramSurfaceData2, paramComposite, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 113 */       return;
/*     */     }
/*     */ 
/* 116 */     AlphaComposite localAlphaComposite = (AlphaComposite)paramComposite;
/* 117 */     if (localAlphaComposite.getRule() != 3) {
/* 118 */       paramComposite = AlphaComposite.SrcOver;
/*     */     }
/*     */ 
/* 121 */     this.rq.lock();
/*     */     try {
/* 123 */       validateContext(paramSurfaceData2, paramComposite, paramRegion);
/*     */ 
/* 125 */       RenderBuffer localRenderBuffer = this.rq.getBuffer();
/* 126 */       int i = 20 + paramInt5 * paramInt6 * 4;
/*     */ 
/* 134 */       this.rq.ensureCapacity(i);
/*     */ 
/* 137 */       int j = enqueueTile(localRenderBuffer.getAddress(), localRenderBuffer.position(), paramSurfaceData1, paramSurfaceData1.getNativeOps(), this.srcTypeVal, paramArrayOfByte, paramArrayOfByte.length, paramInt7, paramInt8, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 143 */       localRenderBuffer.position(j);
/*     */     } finally {
/* 145 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private native int enqueueTile(long paramLong1, int paramInt1, SurfaceData paramSurfaceData, long paramLong2, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11);
/*     */ 
/*     */   protected abstract void validateContext(SurfaceData paramSurfaceData, Composite paramComposite, Region paramRegion);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedMaskBlit
 * JD-Core Version:    0.6.2
 */