/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Paint;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.GraphicsPrimitive;
/*     */ import sun.java2d.pipe.BufferedRenderPipe;
/*     */ import sun.java2d.pipe.ParallelogramPipe;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ class D3DRenderer extends BufferedRenderPipe
/*     */ {
/*     */   D3DRenderer(RenderQueue paramRenderQueue)
/*     */   {
/*  43 */     super(paramRenderQueue);
/*     */   }
/*     */ 
/*     */   protected void validateContext(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  48 */     int i = paramSunGraphics2D.paint.getTransparency() == 1 ? 1 : 0;
/*     */     D3DSurfaceData localD3DSurfaceData;
/*     */     try
/*     */     {
/*  53 */       localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/*     */     } catch (ClassCastException localClassCastException) {
/*  55 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*  57 */     D3DContext.validateContext(localD3DSurfaceData, localD3DSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, paramSunGraphics2D.paint, paramSunGraphics2D, i);
/*     */   }
/*     */ 
/*     */   protected void validateContextAA(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  64 */     int i = 0;
/*     */     D3DSurfaceData localD3DSurfaceData;
/*     */     try
/*     */     {
/*  67 */       localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/*     */     } catch (ClassCastException localClassCastException) {
/*  69 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*  71 */     D3DContext.validateContext(localD3DSurfaceData, localD3DSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, paramSunGraphics2D.paint, paramSunGraphics2D, i);
/*     */   }
/*     */ 
/*     */   void copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  79 */     this.rq.lock();
/*     */     try {
/*  81 */       int i = paramSunGraphics2D.surfaceData.getTransparency() == 1 ? 1 : 0;
/*     */       D3DSurfaceData localD3DSurfaceData;
/*     */       try
/*     */       {
/*  86 */         localD3DSurfaceData = (D3DSurfaceData)paramSunGraphics2D.surfaceData;
/*     */       } catch (ClassCastException localClassCastException) {
/*  88 */         throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */       }
/*  90 */       D3DContext.validateContext(localD3DSurfaceData, localD3DSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, null, null, i);
/*     */ 
/*  94 */       this.rq.ensureCapacity(28);
/*  95 */       this.buf.putInt(30);
/*  96 */       this.buf.putInt(paramInt1).putInt(paramInt2).putInt(paramInt3).putInt(paramInt4);
/*  97 */       this.buf.putInt(paramInt5).putInt(paramInt6);
/*     */     } finally {
/*  99 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected native void drawPoly(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
/*     */ 
/*     */   D3DRenderer traceWrap()
/*     */   {
/* 108 */     return new Tracer(this);
/*     */   }
/*     */   private class Tracer extends D3DRenderer {
/*     */     private D3DRenderer d3dr;
/*     */ 
/*     */     Tracer(D3DRenderer arg2) {
/* 114 */       super();
/* 115 */       this.d3dr = localD3DRenderer;
/*     */     }
/*     */     public ParallelogramPipe getAAParallelogramPipe() {
/* 118 */       final ParallelogramPipe localParallelogramPipe = this.d3dr.getAAParallelogramPipe();
/* 119 */       return new ParallelogramPipe()
/*     */       {
/*     */         public void fillParallelogram(SunGraphics2D paramAnonymousSunGraphics2D, double paramAnonymousDouble1, double paramAnonymousDouble2, double paramAnonymousDouble3, double paramAnonymousDouble4, double paramAnonymousDouble5, double paramAnonymousDouble6, double paramAnonymousDouble7, double paramAnonymousDouble8, double paramAnonymousDouble9, double paramAnonymousDouble10)
/*     */         {
/* 127 */           GraphicsPrimitive.tracePrimitive("D3DFillAAParallelogram");
/* 128 */           localParallelogramPipe.fillParallelogram(paramAnonymousSunGraphics2D, paramAnonymousDouble1, paramAnonymousDouble2, paramAnonymousDouble3, paramAnonymousDouble4, paramAnonymousDouble5, paramAnonymousDouble6, paramAnonymousDouble7, paramAnonymousDouble8, paramAnonymousDouble9, paramAnonymousDouble10);
/*     */         }
/*     */ 
/*     */         public void drawParallelogram(SunGraphics2D paramAnonymousSunGraphics2D, double paramAnonymousDouble1, double paramAnonymousDouble2, double paramAnonymousDouble3, double paramAnonymousDouble4, double paramAnonymousDouble5, double paramAnonymousDouble6, double paramAnonymousDouble7, double paramAnonymousDouble8, double paramAnonymousDouble9, double paramAnonymousDouble10, double paramAnonymousDouble11, double paramAnonymousDouble12)
/*     */         {
/* 140 */           GraphicsPrimitive.tracePrimitive("D3DDrawAAParallelogram");
/* 141 */           localParallelogramPipe.drawParallelogram(paramAnonymousSunGraphics2D, paramAnonymousDouble1, paramAnonymousDouble2, paramAnonymousDouble3, paramAnonymousDouble4, paramAnonymousDouble5, paramAnonymousDouble6, paramAnonymousDouble7, paramAnonymousDouble8, paramAnonymousDouble9, paramAnonymousDouble10, paramAnonymousDouble11, paramAnonymousDouble12);
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     protected void validateContext(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 150 */       this.d3dr.validateContext(paramSunGraphics2D);
/*     */     }
/*     */ 
/*     */     public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 155 */       GraphicsPrimitive.tracePrimitive("D3DDrawLine");
/* 156 */       this.d3dr.drawLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 159 */       GraphicsPrimitive.tracePrimitive("D3DDrawRect");
/* 160 */       this.d3dr.drawRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     protected void drawPoly(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt, boolean paramBoolean)
/*     */     {
/* 166 */       GraphicsPrimitive.tracePrimitive("D3DDrawPoly");
/* 167 */       this.d3dr.drawPoly(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt, paramBoolean);
/*     */     }
/*     */     public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 170 */       GraphicsPrimitive.tracePrimitive("D3DFillRect");
/* 171 */       this.d3dr.fillRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     protected void drawPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */     {
/* 176 */       GraphicsPrimitive.tracePrimitive("D3DDrawPath");
/* 177 */       this.d3dr.drawPath(paramSunGraphics2D, paramFloat, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void fillPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */     {
/* 182 */       GraphicsPrimitive.tracePrimitive("D3DFillPath");
/* 183 */       this.d3dr.fillPath(paramSunGraphics2D, paramFloat, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void fillSpans(SunGraphics2D paramSunGraphics2D, SpanIterator paramSpanIterator, int paramInt1, int paramInt2)
/*     */     {
/* 188 */       GraphicsPrimitive.tracePrimitive("D3DFillSpans");
/* 189 */       this.d3dr.fillSpans(paramSunGraphics2D, paramSpanIterator, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*     */     {
/* 198 */       GraphicsPrimitive.tracePrimitive("D3DFillParallelogram");
/* 199 */       this.d3dr.fillParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10);
/*     */     }
/*     */ 
/*     */     public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*     */     {
/* 211 */       GraphicsPrimitive.tracePrimitive("D3DDrawParallelogram");
/* 212 */       this.d3dr.drawParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12);
/*     */     }
/*     */ 
/*     */     public void copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 219 */       GraphicsPrimitive.tracePrimitive("D3DCopyArea");
/* 220 */       this.d3dr.copyArea(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DRenderer
 * JD-Core Version:    0.6.2
 */