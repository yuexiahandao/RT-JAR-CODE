/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public abstract class BufferedTextPipe extends GlyphListPipe
/*     */ {
/*     */   private static final int BYTES_PER_GLYPH_IMAGE = 8;
/*     */   private static final int BYTES_PER_GLYPH_POSITION = 8;
/*     */   private static final int OFFSET_CONTRAST = 8;
/*     */   private static final int OFFSET_RGBORDER = 2;
/*     */   private static final int OFFSET_SUBPIXPOS = 1;
/*     */   private static final int OFFSET_POSITIONS = 0;
/*     */   protected final RenderQueue rq;
/*     */ 
/*     */   private static int createPackedParams(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList)
/*     */   {
/*  58 */     return (paramGlyphList.usePositions() ? 1 : 0) << 0 | (paramGlyphList.isSubPixPos() ? 1 : 0) << 1 | (paramGlyphList.isRGBOrder() ? 1 : 0) << 2 | (paramSunGraphics2D.lcdTextContrast & 0xFF) << 8;
/*     */   }
/*     */ 
/*     */   protected BufferedTextPipe(RenderQueue paramRenderQueue)
/*     */   {
/*  68 */     this.rq = paramRenderQueue;
/*     */   }
/*     */ 
/*     */   protected void drawGlyphList(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList)
/*     */   {
/*  78 */     Object localObject1 = paramSunGraphics2D.composite;
/*  79 */     if (localObject1 == AlphaComposite.Src)
/*     */     {
/*  88 */       localObject1 = AlphaComposite.SrcOver;
/*     */     }
/*     */ 
/*  91 */     this.rq.lock();
/*     */     try {
/*  93 */       validateContext(paramSunGraphics2D, (Composite)localObject1);
/*  94 */       enqueueGlyphList(paramSunGraphics2D, paramGlyphList);
/*     */     } finally {
/*  96 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enqueueGlyphList(final SunGraphics2D paramSunGraphics2D, final GlyphList paramGlyphList)
/*     */   {
/* 104 */     RenderBuffer localRenderBuffer = this.rq.getBuffer();
/* 105 */     final int i = paramGlyphList.getNumGlyphs();
/* 106 */     int j = i * 8;
/* 107 */     int k = paramGlyphList.usePositions() ? i * 8 : 0;
/*     */ 
/* 109 */     int m = 24 + j + k;
/*     */ 
/* 111 */     final long[] arrayOfLong = paramGlyphList.getImages();
/* 112 */     final float f1 = paramGlyphList.getX() + 0.5F;
/* 113 */     final float f2 = paramGlyphList.getY() + 0.5F;
/*     */ 
/* 117 */     this.rq.addReference(paramGlyphList.getStrike());
/*     */ 
/* 119 */     if (m <= localRenderBuffer.capacity()) {
/* 120 */       if (m > localRenderBuffer.remaining())
/*     */       {
/* 122 */         this.rq.flushNow();
/*     */       }
/* 124 */       this.rq.ensureAlignment(20);
/* 125 */       localRenderBuffer.putInt(40);
/*     */ 
/* 127 */       localRenderBuffer.putInt(i);
/* 128 */       localRenderBuffer.putInt(createPackedParams(paramSunGraphics2D, paramGlyphList));
/* 129 */       localRenderBuffer.putFloat(f1);
/* 130 */       localRenderBuffer.putFloat(f2);
/*     */ 
/* 132 */       localRenderBuffer.put(arrayOfLong, 0, i);
/* 133 */       if (paramGlyphList.usePositions()) {
/* 134 */         float[] arrayOfFloat = paramGlyphList.getPositions();
/* 135 */         localRenderBuffer.put(arrayOfFloat, 0, 2 * i);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 140 */       this.rq.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 142 */           BufferedTextPipe.this.drawGlyphList(i, paramGlyphList.usePositions(), paramGlyphList.isSubPixPos(), paramGlyphList.isRGBOrder(), paramSunGraphics2D.lcdTextContrast, f1, f2, arrayOfLong, paramGlyphList.getPositions());
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void drawGlyphList(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2, float paramFloat1, float paramFloat2, long[] paramArrayOfLong, float[] paramArrayOfFloat);
/*     */ 
/*     */   protected abstract void validateContext(SunGraphics2D paramSunGraphics2D, Composite paramComposite);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedTextPipe
 * JD-Core Version:    0.6.2
 */