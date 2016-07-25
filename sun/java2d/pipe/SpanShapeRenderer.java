/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.FillRect;
/*     */ import sun.java2d.loops.RenderLoops;
/*     */ 
/*     */ public abstract class SpanShapeRenderer
/*     */   implements ShapeDrawPipe
/*     */ {
/*  45 */   static final RenderingEngine RenderEngine = RenderingEngine.getInstance();
/*     */   public static final int NON_RECTILINEAR_TRANSFORM_MASK = 48;
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/*  87 */     if ((paramSunGraphics2D.stroke instanceof BasicStroke)) {
/*  88 */       ShapeSpanIterator localShapeSpanIterator = LoopPipe.getStrokeSpans(paramSunGraphics2D, paramShape);
/*     */       try {
/*  90 */         renderSpans(paramSunGraphics2D, paramSunGraphics2D.getCompClip(), paramShape, localShapeSpanIterator);
/*     */       } finally {
/*  92 */         localShapeSpanIterator.dispose();
/*     */       }
/*     */     } else {
/*  95 */       fill(paramSunGraphics2D, paramSunGraphics2D.stroke.createStrokedShape(paramShape));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/* 104 */     if (((paramShape instanceof Rectangle2D)) && ((paramSunGraphics2D.transform.getType() & 0x30) == 0))
/*     */     {
/* 107 */       renderRect(paramSunGraphics2D, (Rectangle2D)paramShape);
/* 108 */       return;
/*     */     }
/*     */ 
/* 111 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 112 */     ShapeSpanIterator localShapeSpanIterator = LoopPipe.getFillSSI(paramSunGraphics2D);
/*     */     try {
/* 114 */       localShapeSpanIterator.setOutputArea(localRegion);
/* 115 */       localShapeSpanIterator.appendPath(paramShape.getPathIterator(paramSunGraphics2D.transform));
/* 116 */       renderSpans(paramSunGraphics2D, localRegion, paramShape, localShapeSpanIterator);
/*     */     } finally {
/* 118 */       localShapeSpanIterator.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt);
/*     */ 
/*     */   public abstract void renderBox(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public abstract void endSequence(Object paramObject);
/*     */ 
/*     */   public void renderRect(SunGraphics2D paramSunGraphics2D, Rectangle2D paramRectangle2D)
/*     */   {
/* 130 */     double[] arrayOfDouble = { paramRectangle2D.getX(), paramRectangle2D.getY(), paramRectangle2D.getWidth(), paramRectangle2D.getHeight() };
/*     */ 
/* 133 */     arrayOfDouble[2] += arrayOfDouble[0];
/* 134 */     arrayOfDouble[3] += arrayOfDouble[1];
/* 135 */     if ((arrayOfDouble[2] <= arrayOfDouble[0]) || (arrayOfDouble[3] <= arrayOfDouble[1])) {
/* 136 */       return;
/*     */     }
/* 138 */     paramSunGraphics2D.transform.transform(arrayOfDouble, 0, arrayOfDouble, 0, 2);
/*     */     double d;
/* 139 */     if (arrayOfDouble[2] < arrayOfDouble[0]) {
/* 140 */       d = arrayOfDouble[2];
/* 141 */       arrayOfDouble[2] = arrayOfDouble[0];
/* 142 */       arrayOfDouble[0] = d;
/*     */     }
/* 144 */     if (arrayOfDouble[3] < arrayOfDouble[1]) {
/* 145 */       d = arrayOfDouble[3];
/* 146 */       arrayOfDouble[3] = arrayOfDouble[1];
/* 147 */       arrayOfDouble[1] = d;
/*     */     }
/* 149 */     int[] arrayOfInt = { (int)arrayOfDouble[0], (int)arrayOfDouble[1], (int)arrayOfDouble[2], (int)arrayOfDouble[3] };
/*     */ 
/* 155 */     Rectangle localRectangle = new Rectangle(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */ 
/* 158 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 159 */     localRegion.clipBoxToBounds(arrayOfInt);
/* 160 */     if ((arrayOfInt[0] >= arrayOfInt[2]) || (arrayOfInt[1] >= arrayOfInt[3])) {
/* 161 */       return;
/*     */     }
/* 163 */     Object localObject = startSequence(paramSunGraphics2D, paramRectangle2D, localRectangle, arrayOfInt);
/* 164 */     if (localRegion.isRectangular()) {
/* 165 */       renderBox(localObject, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */     }
/*     */     else
/*     */     {
/* 169 */       SpanIterator localSpanIterator = localRegion.getSpanIterator(arrayOfInt);
/* 170 */       while (localSpanIterator.nextSpan(arrayOfInt)) {
/* 171 */         renderBox(localObject, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 176 */     endSequence(localObject);
/*     */   }
/*     */ 
/*     */   public void renderSpans(SunGraphics2D paramSunGraphics2D, Region paramRegion, Shape paramShape, ShapeSpanIterator paramShapeSpanIterator)
/*     */   {
/* 182 */     Object localObject1 = null;
/* 183 */     int[] arrayOfInt = new int[4];
/*     */     try {
/* 185 */       paramShapeSpanIterator.getPathBox(arrayOfInt);
/* 186 */       Rectangle localRectangle = new Rectangle(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */ 
/* 189 */       paramRegion.clipBoxToBounds(arrayOfInt);
/* 190 */       if ((arrayOfInt[0] >= arrayOfInt[2]) || (arrayOfInt[1] >= arrayOfInt[3])) {
/*     */         return;
/*     */       }
/* 193 */       paramShapeSpanIterator.intersectClipBox(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/* 194 */       localObject1 = startSequence(paramSunGraphics2D, paramShape, localRectangle, arrayOfInt);
/*     */ 
/* 196 */       spanClipLoop(localObject1, paramShapeSpanIterator, paramRegion, arrayOfInt);
/*     */     }
/*     */     finally {
/* 199 */       if (localObject1 != null)
/* 200 */         endSequence(localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void spanClipLoop(Object paramObject, SpanIterator paramSpanIterator, Region paramRegion, int[] paramArrayOfInt)
/*     */   {
/* 207 */     if (!paramRegion.isRectangular()) {
/* 208 */       paramSpanIterator = paramRegion.filter(paramSpanIterator);
/*     */     }
/* 210 */     while (paramSpanIterator.nextSpan(paramArrayOfInt)) {
/* 211 */       int i = paramArrayOfInt[0];
/* 212 */       int j = paramArrayOfInt[1];
/* 213 */       renderBox(paramObject, i, j, paramArrayOfInt[2] - i, paramArrayOfInt[3] - j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Composite extends SpanShapeRenderer
/*     */   {
/*     */     CompositePipe comppipe;
/*     */ 
/*     */     public Composite(CompositePipe paramCompositePipe)
/*     */     {
/*  51 */       this.comppipe = paramCompositePipe;
/*     */     }
/*     */ 
/*     */     public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*     */     {
/*  56 */       return this.comppipe.startSequence(paramSunGraphics2D, paramShape, paramRectangle, paramArrayOfInt);
/*     */     }
/*     */ 
/*     */     public void renderBox(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  60 */       this.comppipe.renderPathTile(paramObject, null, 0, paramInt3, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public void endSequence(Object paramObject) {
/*  64 */       this.comppipe.endSequence(paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Simple extends SpanShapeRenderer
/*     */     implements LoopBasedPipe
/*     */   {
/*     */     public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*     */     {
/*  73 */       return paramSunGraphics2D;
/*     */     }
/*     */ 
/*     */     public void renderBox(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  77 */       SunGraphics2D localSunGraphics2D = (SunGraphics2D)paramObject;
/*  78 */       SurfaceData localSurfaceData = localSunGraphics2D.getSurfaceData();
/*  79 */       localSunGraphics2D.loops.fillRectLoop.FillRect(localSunGraphics2D, localSurfaceData, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public void endSequence(Object paramObject)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.SpanShapeRenderer
 * JD-Core Version:    0.6.2
 */