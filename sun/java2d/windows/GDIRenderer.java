/*     */ package sun.java2d.windows;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.loops.GraphicsPrimitive;
/*     */ import sun.java2d.pipe.LoopPipe;
/*     */ import sun.java2d.pipe.PixelDrawPipe;
/*     */ import sun.java2d.pipe.PixelFillPipe;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.ShapeDrawPipe;
/*     */ import sun.java2d.pipe.ShapeSpanIterator;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ public class GDIRenderer
/*     */   implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe
/*     */ {
/*     */   native void doDrawLine(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  56 */     int i = paramSunGraphics2D.transX;
/*  57 */     int j = paramSunGraphics2D.transY;
/*     */     try {
/*  59 */       doDrawLine((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + i, paramInt2 + j, paramInt3 + i, paramInt4 + j);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*  63 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doDrawRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     try
/*     */     {
/*  75 */       doDrawRect((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*  79 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doDrawRoundRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public void drawRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*     */     try
/*     */     {
/*  93 */       doDrawRoundRect((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*  98 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doDrawOval(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   public void drawOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     try
/*     */     {
/* 110 */       doDrawOval((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 114 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doDrawArc(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public void drawArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*     */     try
/*     */     {
/* 128 */       doDrawArc((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 133 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doDrawPoly(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, boolean paramBoolean);
/*     */ 
/*     */   public void drawPolyline(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       doDrawPoly((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramSunGraphics2D.transX, paramSunGraphics2D.transY, paramArrayOfInt1, paramArrayOfInt2, paramInt, false);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 152 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       doDrawPoly((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramSunGraphics2D.transX, paramSunGraphics2D.transY, paramArrayOfInt1, paramArrayOfInt2, paramInt, true);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 165 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doFillRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     try
/*     */     {
/* 177 */       doFillRect((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 181 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doFillRoundRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public void fillRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*     */     try
/*     */     {
/* 195 */       doFillRoundRect((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 200 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doFillOval(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   public void fillOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     try
/*     */     {
/* 212 */       doFillOval((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 216 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doFillArc(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */ 
/*     */   public void fillArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*     */     try
/*     */     {
/* 230 */       doFillArc((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramInt1 + paramSunGraphics2D.transX, paramInt2 + paramSunGraphics2D.transY, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 235 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doFillPoly(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4);
/*     */ 
/*     */   public void fillPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 250 */       doFillPoly((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, paramSunGraphics2D.transX, paramSunGraphics2D.transY, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 254 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void doShape(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, Path2D.Float paramFloat, boolean paramBoolean);
/*     */ 
/*     */   void doShape(SunGraphics2D paramSunGraphics2D, Shape paramShape, boolean paramBoolean)
/*     */   {
/*     */     Path2D.Float localFloat;
/*     */     int i;
/*     */     int j;
/* 267 */     if (paramSunGraphics2D.transformState <= 1) {
/* 268 */       if ((paramShape instanceof Path2D.Float))
/* 269 */         localFloat = (Path2D.Float)paramShape;
/*     */       else {
/* 271 */         localFloat = new Path2D.Float(paramShape);
/*     */       }
/* 273 */       i = paramSunGraphics2D.transX;
/* 274 */       j = paramSunGraphics2D.transY;
/*     */     } else {
/* 276 */       localFloat = new Path2D.Float(paramShape, paramSunGraphics2D.transform);
/* 277 */       i = 0;
/* 278 */       j = 0;
/*     */     }
/*     */     try {
/* 281 */       doShape((GDIWindowSurfaceData)paramSunGraphics2D.surfaceData, paramSunGraphics2D.getCompClip(), paramSunGraphics2D.composite, paramSunGraphics2D.eargb, i, j, localFloat, paramBoolean);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/* 285 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void doFillSpans(SunGraphics2D paramSunGraphics2D, SpanIterator paramSpanIterator)
/*     */   {
/* 294 */     int[] arrayOfInt = new int[4];
/*     */     GDIWindowSurfaceData localGDIWindowSurfaceData;
/*     */     try
/*     */     {
/* 297 */       localGDIWindowSurfaceData = (GDIWindowSurfaceData)paramSunGraphics2D.surfaceData;
/*     */     } catch (ClassCastException localClassCastException) {
/* 299 */       throw new InvalidPipeException("wrong surface data type: " + paramSunGraphics2D.surfaceData);
/*     */     }
/* 301 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 302 */     Composite localComposite = paramSunGraphics2D.composite;
/* 303 */     int i = paramSunGraphics2D.eargb;
/* 304 */     while (paramSpanIterator.nextSpan(arrayOfInt))
/* 305 */       doFillRect(localGDIWindowSurfaceData, localRegion, localComposite, i, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */   }
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/* 311 */     if (paramSunGraphics2D.strokeState == 0) {
/* 312 */       doShape(paramSunGraphics2D, paramShape, false);
/* 313 */     } else if (paramSunGraphics2D.strokeState < 3) {
/* 314 */       ShapeSpanIterator localShapeSpanIterator = LoopPipe.getStrokeSpans(paramSunGraphics2D, paramShape);
/*     */       try {
/* 316 */         doFillSpans(paramSunGraphics2D, localShapeSpanIterator);
/*     */       } finally {
/* 318 */         localShapeSpanIterator.dispose();
/*     */       }
/*     */     } else {
/* 321 */       doShape(paramSunGraphics2D, paramSunGraphics2D.stroke.createStrokedShape(paramShape), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape) {
/* 326 */     doShape(paramSunGraphics2D, paramShape, true);
/*     */   }
/*     */ 
/*     */   public native void devCopyArea(GDIWindowSurfaceData paramGDIWindowSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*     */ 
/*     */   public GDIRenderer traceWrap()
/*     */   {
/* 335 */     return new Tracer();
/*     */   }
/*     */ 
/*     */   public static class Tracer extends GDIRenderer
/*     */   {
/*     */     void doDrawLine(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 343 */       GraphicsPrimitive.tracePrimitive("GDIDrawLine");
/* 344 */       super.doDrawLine(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */     }
/*     */ 
/*     */     void doDrawRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 350 */       GraphicsPrimitive.tracePrimitive("GDIDrawRect");
/* 351 */       super.doDrawRect(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */     }
/*     */ 
/*     */     void doDrawRoundRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 358 */       GraphicsPrimitive.tracePrimitive("GDIDrawRoundRect");
/* 359 */       super.doDrawRoundRect(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */ 
/*     */     void doDrawOval(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 366 */       GraphicsPrimitive.tracePrimitive("GDIDrawOval");
/* 367 */       super.doDrawOval(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */     }
/*     */ 
/*     */     void doDrawArc(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 374 */       GraphicsPrimitive.tracePrimitive("GDIDrawArc");
/* 375 */       super.doDrawArc(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */ 
/*     */     void doDrawPoly(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, boolean paramBoolean)
/*     */     {
/* 384 */       GraphicsPrimitive.tracePrimitive("GDIDrawPoly");
/* 385 */       super.doDrawPoly(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramArrayOfInt1, paramArrayOfInt2, paramInt4, paramBoolean);
/*     */     }
/*     */ 
/*     */     void doFillRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 392 */       GraphicsPrimitive.tracePrimitive("GDIFillRect");
/* 393 */       super.doFillRect(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */     }
/*     */ 
/*     */     void doFillRoundRect(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 400 */       GraphicsPrimitive.tracePrimitive("GDIFillRoundRect");
/* 401 */       super.doFillRoundRect(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */ 
/*     */     void doFillOval(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */     {
/* 408 */       GraphicsPrimitive.tracePrimitive("GDIFillOval");
/* 409 */       super.doFillOval(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */     }
/*     */ 
/*     */     void doFillArc(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */     {
/* 416 */       GraphicsPrimitive.tracePrimitive("GDIFillArc");
/* 417 */       super.doFillArc(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7);
/*     */     }
/*     */ 
/*     */     void doFillPoly(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4)
/*     */     {
/* 426 */       GraphicsPrimitive.tracePrimitive("GDIFillPoly");
/* 427 */       super.doFillPoly(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramArrayOfInt1, paramArrayOfInt2, paramInt4);
/*     */     }
/*     */ 
/*     */     void doShape(GDIWindowSurfaceData paramGDIWindowSurfaceData, Region paramRegion, Composite paramComposite, int paramInt1, int paramInt2, int paramInt3, Path2D.Float paramFloat, boolean paramBoolean)
/*     */     {
/* 435 */       GraphicsPrimitive.tracePrimitive(paramBoolean ? "GDIFillShape" : "GDIDrawShape");
/*     */ 
/* 438 */       super.doShape(paramGDIWindowSurfaceData, paramRegion, paramComposite, paramInt1, paramInt2, paramInt3, paramFloat, paramBoolean);
/*     */     }
/*     */ 
/*     */     public void devCopyArea(GDIWindowSurfaceData paramGDIWindowSurfaceData, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */     {
/* 446 */       GraphicsPrimitive.tracePrimitive("GDICopyArea");
/* 447 */       super.devCopyArea(paramGDIWindowSurfaceData, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.windows.GDIRenderer
 * JD-Core Version:    0.6.2
 */