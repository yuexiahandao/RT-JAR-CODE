/*     */ package sun.java2d.opengl;
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
/*     */ class OGLRenderer extends BufferedRenderPipe
/*     */ {
/*     */   OGLRenderer(RenderQueue paramRenderQueue)
/*     */   {
/*  42 */     super(paramRenderQueue);
/*     */   }
/*     */ 
/*     */   protected void validateContext(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  47 */     int i = paramSunGraphics2D.paint.getTransparency() == 1 ? 1 : 0;
/*     */     OGLSurfaceData localOGLSurfaceData;
/*     */     try
/*     */     {
/*  52 */       localOGLSurfaceData = (OGLSurfaceData)paramSunGraphics2D.surfaceData;
/*     */     } catch (ClassCastException localClassCastException) {
/*  54 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*  56 */     OGLContext.validateContext(localOGLSurfaceData, localOGLSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, paramSunGraphics2D.paint, paramSunGraphics2D, i);
/*     */   }
/*     */ 
/*     */   protected void validateContextAA(SunGraphics2D paramSunGraphics2D)
/*     */   {
/*  63 */     int i = 0;
/*     */     OGLSurfaceData localOGLSurfaceData;
/*     */     try
/*     */     {
/*  66 */       localOGLSurfaceData = (OGLSurfaceData)paramSunGraphics2D.surfaceData;
/*     */     } catch (ClassCastException localClassCastException) {
/*  68 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*  70 */     OGLContext.validateContext(localOGLSurfaceData, localOGLSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, paramSunGraphics2D.paint, paramSunGraphics2D, i);
/*     */   }
/*     */ 
/*     */   void copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  78 */     this.rq.lock();
/*     */     try {
/*  80 */       int i = paramSunGraphics2D.surfaceData.getTransparency() == 1 ? 1 : 0;
/*     */       OGLSurfaceData localOGLSurfaceData;
/*     */       try
/*     */       {
/*  85 */         localOGLSurfaceData = (OGLSurfaceData)paramSunGraphics2D.surfaceData;
/*     */       } catch (ClassCastException localClassCastException) {
/*  87 */         throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */       }
/*  89 */       OGLContext.validateContext(localOGLSurfaceData, localOGLSurfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, null, null, null, i);
/*     */ 
/*  93 */       this.rq.ensureCapacity(28);
/*  94 */       this.buf.putInt(30);
/*  95 */       this.buf.putInt(paramInt1).putInt(paramInt2).putInt(paramInt3).putInt(paramInt4);
/*  96 */       this.buf.putInt(paramInt5).putInt(paramInt6);
/*     */     } finally {
/*  98 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected native void drawPoly(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
/*     */ 
/*     */   OGLRenderer traceWrap()
/*     */   {
/* 108 */     return new Tracer(this);
/*     */   }
/*     */   private class Tracer extends OGLRenderer {
/*     */     private OGLRenderer oglr;
/*     */ 
/*     */     Tracer(OGLRenderer arg2) {
/* 114 */       super();
/* 115 */       this.oglr = localOGLRenderer;
/*     */     }
/*     */     public ParallelogramPipe getAAParallelogramPipe() {
/* 118 */       final ParallelogramPipe localParallelogramPipe = this.oglr.getAAParallelogramPipe();
/* 119 */       return new ParallelogramPipe()
/*     */       {
/*     */         public void fillParallelogram(SunGraphics2D paramAnonymousSunGraphics2D, double paramAnonymousDouble1, double paramAnonymousDouble2, double paramAnonymousDouble3, double paramAnonymousDouble4, double paramAnonymousDouble5, double paramAnonymousDouble6, double paramAnonymousDouble7, double paramAnonymousDouble8, double paramAnonymousDouble9, double paramAnonymousDouble10)
/*     */         {
/* 127 */           GraphicsPrimitive.tracePrimitive("OGLFillAAParallelogram");
/* 128 */           localParallelogramPipe.fillParallelogram(paramAnonymousSunGraphics2D, paramAnonymousDouble1, paramAnonymousDouble2, paramAnonymousDouble3, paramAnonymousDouble4, paramAnonymousDouble5, paramAnonymousDouble6, paramAnonymousDouble7, paramAnonymousDouble8, paramAnonymousDouble9, paramAnonymousDouble10);
/*     */         }
/*     */ 
/*     */         public void drawParallelogram(SunGraphics2D paramAnonymousSunGraphics2D, double paramAnonymousDouble1, double paramAnonymousDouble2, double paramAnonymousDouble3, double paramAnonymousDouble4, double paramAnonymousDouble5, double paramAnonymousDouble6, double paramAnonymousDouble7, double paramAnonymousDouble8, double paramAnonymousDouble9, double paramAnonymousDouble10, double paramAnonymousDouble11, double paramAnonymousDouble12)
/*     */         {
/* 140 */           GraphicsPrimitive.tracePrimitive("OGLDrawAAParallelogram");
/* 141 */           localParallelogramPipe.drawParallelogram(paramAnonymousSunGraphics2D, paramAnonymousDouble1, paramAnonymousDouble2, paramAnonymousDouble3, paramAnonymousDouble4, paramAnonymousDouble5, paramAnonymousDouble6, paramAnonymousDouble7, paramAnonymousDouble8, paramAnonymousDouble9, paramAnonymousDouble10, paramAnonymousDouble11, paramAnonymousDouble12);
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     protected void validateContext(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 149 */       this.oglr.validateContext(paramSunGraphics2D);
/*     */     }
/*     */ 
/*     */     public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 154 */       GraphicsPrimitive.tracePrimitive("OGLDrawLine");
/* 155 */       this.oglr.drawLine(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 158 */       GraphicsPrimitive.tracePrimitive("OGLDrawRect");
/* 159 */       this.oglr.drawRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     protected void drawPoly(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt, boolean paramBoolean)
/*     */     {
/* 165 */       GraphicsPrimitive.tracePrimitive("OGLDrawPoly");
/* 166 */       this.oglr.drawPoly(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt, paramBoolean);
/*     */     }
/*     */     public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 169 */       GraphicsPrimitive.tracePrimitive("OGLFillRect");
/* 170 */       this.oglr.fillRect(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     protected void drawPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */     {
/* 175 */       GraphicsPrimitive.tracePrimitive("OGLDrawPath");
/* 176 */       this.oglr.drawPath(paramSunGraphics2D, paramFloat, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void fillPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */     {
/* 181 */       GraphicsPrimitive.tracePrimitive("OGLFillPath");
/* 182 */       this.oglr.fillPath(paramSunGraphics2D, paramFloat, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void fillSpans(SunGraphics2D paramSunGraphics2D, SpanIterator paramSpanIterator, int paramInt1, int paramInt2)
/*     */     {
/* 187 */       GraphicsPrimitive.tracePrimitive("OGLFillSpans");
/* 188 */       this.oglr.fillSpans(paramSunGraphics2D, paramSpanIterator, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*     */     {
/* 197 */       GraphicsPrimitive.tracePrimitive("OGLFillParallelogram");
/* 198 */       this.oglr.fillParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10);
/*     */     }
/*     */ 
/*     */     public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*     */     {
/* 210 */       GraphicsPrimitive.tracePrimitive("OGLDrawParallelogram");
/* 211 */       this.oglr.drawParallelogram(paramSunGraphics2D, paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12);
/*     */     }
/*     */ 
/*     */     public void copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 218 */       GraphicsPrimitive.tracePrimitive("OGLCopyArea");
/* 219 */       this.oglr.copyArea(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLRenderer
 * JD-Core Version:    0.6.2
 */