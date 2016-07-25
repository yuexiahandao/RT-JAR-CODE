/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.MaskFill;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ 
/*     */ public abstract class BufferedMaskFill extends MaskFill
/*     */ {
/*     */   protected final RenderQueue rq;
/*     */ 
/*     */   protected BufferedMaskFill(RenderQueue paramRenderQueue, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  68 */     super(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  69 */     this.rq = paramRenderQueue;
/*     */   }
/*     */ 
/*     */   public void MaskFill(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, Composite paramComposite, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final byte[] paramArrayOfByte, final int paramInt5, final int paramInt6)
/*     */   {
/*  79 */     AlphaComposite localAlphaComposite = (AlphaComposite)paramComposite;
/*  80 */     if (localAlphaComposite.getRule() != 3) {
/*  81 */       paramComposite = AlphaComposite.SrcOver;
/*     */     }
/*     */ 
/*  84 */     this.rq.lock();
/*     */     try {
/*  86 */       validateContext(paramSunGraphics2D, paramComposite, 2);
/*     */       int i;
/*  91 */       if (paramArrayOfByte != null)
/*     */       {
/*  94 */         i = paramArrayOfByte.length + 3 & 0xFFFFFFFC;
/*     */       }
/*     */       else {
/*  97 */         i = 0;
/*     */       }
/*  99 */       int j = 32 + i;
/*     */ 
/* 101 */       RenderBuffer localRenderBuffer = this.rq.getBuffer();
/* 102 */       if (j <= localRenderBuffer.capacity()) {
/* 103 */         if (j > localRenderBuffer.remaining())
/*     */         {
/* 105 */           this.rq.flushNow();
/*     */         }
/*     */ 
/* 108 */         localRenderBuffer.putInt(32);
/*     */ 
/* 110 */         localRenderBuffer.putInt(paramInt1).putInt(paramInt2).putInt(paramInt3).putInt(paramInt4);
/* 111 */         localRenderBuffer.putInt(paramInt5);
/* 112 */         localRenderBuffer.putInt(paramInt6);
/* 113 */         localRenderBuffer.putInt(i);
/* 114 */         if (paramArrayOfByte != null)
/*     */         {
/* 116 */           int k = i - paramArrayOfByte.length;
/* 117 */           localRenderBuffer.put(paramArrayOfByte);
/* 118 */           if (k != 0) {
/* 119 */             localRenderBuffer.position(localRenderBuffer.position() + k);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 125 */         this.rq.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 127 */             BufferedMaskFill.this.maskFill(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramArrayOfByte.length, paramArrayOfByte);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     finally {
/* 133 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void maskFill(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, byte[] paramArrayOfByte);
/*     */ 
/*     */   protected abstract void validateContext(SunGraphics2D paramSunGraphics2D, Composite paramComposite, int paramInt);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedMaskFill
 * JD-Core Version:    0.6.2
 */