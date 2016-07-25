/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Polygon;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Arc2D.Float;
/*     */ import java.awt.geom.Ellipse2D.Float;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.awt.geom.RoundRectangle2D.Float;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.loops.ProcessPath;
/*     */ import sun.java2d.loops.ProcessPath.DrawHandler;
/*     */ 
/*     */ public abstract class BufferedRenderPipe
/*     */   implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe, ParallelogramPipe
/*     */ {
/*  58 */   ParallelogramPipe aapgrampipe = new AAParallelogramPipe(null);
/*     */   static final int BYTES_PER_POLY_POINT = 8;
/*     */   static final int BYTES_PER_SCANLINE = 12;
/*     */   static final int BYTES_PER_SPAN = 16;
/*     */   protected RenderQueue rq;
/*     */   protected RenderBuffer buf;
/*     */   private BufferedDrawHandler drawHandler;
/*     */ 
/*     */   public BufferedRenderPipe(RenderQueue paramRenderQueue)
/*     */   {
/*  69 */     this.rq = paramRenderQueue;
/*  70 */     this.buf = paramRenderQueue.getBuffer();
/*  71 */     this.drawHandler = new BufferedDrawHandler();
/*     */   }
/*     */ 
/*     */   public ParallelogramPipe getAAParallelogramPipe() {
/*  75 */     return this.aapgrampipe;
/*     */   }
/*     */ 
/*     */   protected abstract void validateContext(SunGraphics2D paramSunGraphics2D);
/*     */ 
/*     */   protected abstract void validateContextAA(SunGraphics2D paramSunGraphics2D);
/*     */ 
/*     */   public void drawLine(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  89 */     int i = paramSunGraphics2D.transX;
/*  90 */     int j = paramSunGraphics2D.transY;
/*  91 */     this.rq.lock();
/*     */     try {
/*  93 */       validateContext(paramSunGraphics2D);
/*  94 */       this.rq.ensureCapacity(20);
/*  95 */       this.buf.putInt(10);
/*  96 */       this.buf.putInt(paramInt1 + i);
/*  97 */       this.buf.putInt(paramInt2 + j);
/*  98 */       this.buf.putInt(paramInt3 + i);
/*  99 */       this.buf.putInt(paramInt4 + j);
/*     */     } finally {
/* 101 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 108 */     this.rq.lock();
/*     */     try {
/* 110 */       validateContext(paramSunGraphics2D);
/* 111 */       this.rq.ensureCapacity(20);
/* 112 */       this.buf.putInt(11);
/* 113 */       this.buf.putInt(paramInt1 + paramSunGraphics2D.transX);
/* 114 */       this.buf.putInt(paramInt2 + paramSunGraphics2D.transY);
/* 115 */       this.buf.putInt(paramInt3);
/* 116 */       this.buf.putInt(paramInt4);
/*     */     } finally {
/* 118 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fillRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 125 */     this.rq.lock();
/*     */     try {
/* 127 */       validateContext(paramSunGraphics2D);
/* 128 */       this.rq.ensureCapacity(20);
/* 129 */       this.buf.putInt(20);
/* 130 */       this.buf.putInt(paramInt1 + paramSunGraphics2D.transX);
/* 131 */       this.buf.putInt(paramInt2 + paramSunGraphics2D.transY);
/* 132 */       this.buf.putInt(paramInt3);
/* 133 */       this.buf.putInt(paramInt4);
/*     */     } finally {
/* 135 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 143 */     draw(paramSunGraphics2D, new RoundRectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
/*     */   }
/*     */ 
/*     */   public void fillRoundRect(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 151 */     fill(paramSunGraphics2D, new RoundRectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
/*     */   }
/*     */ 
/*     */   public void drawOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 158 */     draw(paramSunGraphics2D, new Ellipse2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*     */   }
/*     */ 
/*     */   public void fillOval(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 164 */     fill(paramSunGraphics2D, new Ellipse2D.Float(paramInt1, paramInt2, paramInt3, paramInt4));
/*     */   }
/*     */ 
/*     */   public void drawArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 171 */     draw(paramSunGraphics2D, new Arc2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 0));
/*     */   }
/*     */ 
/*     */   public void fillArc(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 180 */     fill(paramSunGraphics2D, new Arc2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, 2));
/*     */   }
/*     */ 
/*     */   protected void drawPoly(final SunGraphics2D paramSunGraphics2D, final int[] paramArrayOfInt1, final int[] paramArrayOfInt2, final int paramInt, final boolean paramBoolean)
/*     */   {
/* 189 */     if ((paramArrayOfInt1 == null) || (paramArrayOfInt2 == null)) {
/* 190 */       throw new NullPointerException("coordinate array");
/*     */     }
/* 192 */     if ((paramArrayOfInt1.length < paramInt) || (paramArrayOfInt2.length < paramInt)) {
/* 193 */       throw new ArrayIndexOutOfBoundsException("coordinate array");
/*     */     }
/*     */ 
/* 196 */     if (paramInt < 2)
/*     */     {
/* 198 */       return;
/* 199 */     }if ((paramInt == 2) && (!paramBoolean))
/*     */     {
/* 201 */       drawLine(paramSunGraphics2D, paramArrayOfInt1[0], paramArrayOfInt2[0], paramArrayOfInt1[1], paramArrayOfInt2[1]);
/* 202 */       return;
/*     */     }
/*     */ 
/* 205 */     this.rq.lock();
/*     */     try {
/* 207 */       validateContext(paramSunGraphics2D);
/*     */ 
/* 209 */       int i = paramInt * 8;
/* 210 */       int j = 20 + i;
/*     */ 
/* 212 */       if (j <= this.buf.capacity()) {
/* 213 */         if (j > this.buf.remaining())
/*     */         {
/* 215 */           this.rq.flushNow();
/*     */         }
/* 217 */         this.buf.putInt(12);
/*     */ 
/* 219 */         this.buf.putInt(paramInt);
/* 220 */         this.buf.putInt(paramBoolean ? 1 : 0);
/* 221 */         this.buf.putInt(paramSunGraphics2D.transX);
/* 222 */         this.buf.putInt(paramSunGraphics2D.transY);
/*     */ 
/* 224 */         this.buf.put(paramArrayOfInt1, 0, paramInt);
/* 225 */         this.buf.put(paramArrayOfInt2, 0, paramInt);
/*     */       }
/*     */       else
/*     */       {
/* 229 */         this.rq.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 231 */             BufferedRenderPipe.this.drawPoly(paramArrayOfInt1, paramArrayOfInt2, paramInt, paramBoolean, paramSunGraphics2D.transX, paramSunGraphics2D.transY);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 238 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void drawPoly(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3);
/*     */ 
/*     */   public void drawPolyline(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 250 */     drawPoly(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt, false);
/*     */   }
/*     */ 
/*     */   public void drawPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 257 */     drawPoly(paramSunGraphics2D, paramArrayOfInt1, paramArrayOfInt2, paramInt, true);
/*     */   }
/*     */ 
/*     */   public void fillPolygon(SunGraphics2D paramSunGraphics2D, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*     */   {
/* 264 */     fill(paramSunGraphics2D, new Polygon(paramArrayOfInt1, paramArrayOfInt2, paramInt));
/*     */   }
/*     */ 
/*     */   protected void drawPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */   {
/* 363 */     this.rq.lock();
/*     */     try {
/* 365 */       validateContext(paramSunGraphics2D);
/* 366 */       this.drawHandler.validate(paramSunGraphics2D);
/* 367 */       ProcessPath.drawPath(this.drawHandler, paramFloat, paramInt1, paramInt2);
/*     */     } finally {
/* 369 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void fillPath(SunGraphics2D paramSunGraphics2D, Path2D.Float paramFloat, int paramInt1, int paramInt2)
/*     */   {
/* 376 */     this.rq.lock();
/*     */     try {
/* 378 */       validateContext(paramSunGraphics2D);
/* 379 */       this.drawHandler.validate(paramSunGraphics2D);
/* 380 */       this.drawHandler.startFillPath();
/* 381 */       ProcessPath.fillPath(this.drawHandler, paramFloat, paramInt1, paramInt2);
/* 382 */       this.drawHandler.endFillPath();
/*     */     } finally {
/* 384 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private native int fillSpans(RenderQueue paramRenderQueue, long paramLong1, int paramInt1, int paramInt2, SpanIterator paramSpanIterator, long paramLong2, int paramInt3, int paramInt4);
/*     */ 
/*     */   protected void fillSpans(SunGraphics2D paramSunGraphics2D, SpanIterator paramSpanIterator, int paramInt1, int paramInt2)
/*     */   {
/* 396 */     this.rq.lock();
/*     */     try {
/* 398 */       validateContext(paramSunGraphics2D);
/* 399 */       this.rq.ensureCapacity(24);
/* 400 */       int i = fillSpans(this.rq, this.buf.getAddress(), this.buf.position(), this.buf.capacity(), paramSpanIterator, paramSpanIterator.getNativeIterator(), paramInt1, paramInt2);
/*     */ 
/* 404 */       this.buf.position(i);
/*     */     } finally {
/* 406 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*     */   {
/* 417 */     this.rq.lock();
/*     */     try {
/* 419 */       validateContext(paramSunGraphics2D);
/* 420 */       this.rq.ensureCapacity(28);
/* 421 */       this.buf.putInt(22);
/* 422 */       this.buf.putFloat((float)paramDouble5);
/* 423 */       this.buf.putFloat((float)paramDouble6);
/* 424 */       this.buf.putFloat((float)paramDouble7);
/* 425 */       this.buf.putFloat((float)paramDouble8);
/* 426 */       this.buf.putFloat((float)paramDouble9);
/* 427 */       this.buf.putFloat((float)paramDouble10);
/*     */     } finally {
/* 429 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*     */   {
/* 441 */     this.rq.lock();
/*     */     try {
/* 443 */       validateContext(paramSunGraphics2D);
/* 444 */       this.rq.ensureCapacity(36);
/* 445 */       this.buf.putInt(15);
/* 446 */       this.buf.putFloat((float)paramDouble5);
/* 447 */       this.buf.putFloat((float)paramDouble6);
/* 448 */       this.buf.putFloat((float)paramDouble7);
/* 449 */       this.buf.putFloat((float)paramDouble8);
/* 450 */       this.buf.putFloat((float)paramDouble9);
/* 451 */       this.buf.putFloat((float)paramDouble10);
/* 452 */       this.buf.putFloat((float)paramDouble11);
/* 453 */       this.buf.putFloat((float)paramDouble12);
/*     */     } finally {
/* 455 */       this.rq.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/*     */     Object localObject1;
/* 511 */     if (paramSunGraphics2D.strokeState == 0) {
/* 512 */       if (((paramShape instanceof Polygon)) && 
/* 513 */         (paramSunGraphics2D.transformState < 3)) {
/* 514 */         localObject1 = (Polygon)paramShape;
/* 515 */         drawPolygon(paramSunGraphics2D, ((Polygon)localObject1).xpoints, ((Polygon)localObject1).ypoints, ((Polygon)localObject1).npoints);
/*     */         return;
/*     */       }
/*     */       int i;
/*     */       int j;
/* 521 */       if (paramSunGraphics2D.transformState <= 1) {
/* 522 */         if ((paramShape instanceof Path2D.Float))
/* 523 */           localObject1 = (Path2D.Float)paramShape;
/*     */         else {
/* 525 */           localObject1 = new Path2D.Float(paramShape);
/*     */         }
/* 527 */         i = paramSunGraphics2D.transX;
/* 528 */         j = paramSunGraphics2D.transY;
/*     */       } else {
/* 530 */         localObject1 = new Path2D.Float(paramShape, paramSunGraphics2D.transform);
/* 531 */         i = 0;
/* 532 */         j = 0;
/*     */       }
/* 534 */       drawPath(paramSunGraphics2D, (Path2D.Float)localObject1, i, j);
/* 535 */     } else if (paramSunGraphics2D.strokeState < 3) {
/* 536 */       localObject1 = LoopPipe.getStrokeSpans(paramSunGraphics2D, paramShape);
/*     */       try {
/* 538 */         fillSpans(paramSunGraphics2D, (SpanIterator)localObject1, 0, 0);
/*     */       } finally {
/* 540 */         ((ShapeSpanIterator)localObject1).dispose();
/*     */       }
/*     */     } else {
/* 543 */       fill(paramSunGraphics2D, paramSunGraphics2D.stroke.createStrokedShape(paramShape));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/*     */     Object localObject1;
/*     */     int i;
/*     */     int j;
/* 550 */     if (paramSunGraphics2D.strokeState == 0)
/*     */     {
/* 554 */       if (paramSunGraphics2D.transformState <= 1) {
/* 555 */         if ((paramShape instanceof Path2D.Float))
/* 556 */           localObject1 = (Path2D.Float)paramShape;
/*     */         else {
/* 558 */           localObject1 = new Path2D.Float(paramShape);
/*     */         }
/* 560 */         i = paramSunGraphics2D.transX;
/* 561 */         j = paramSunGraphics2D.transY;
/*     */       } else {
/* 563 */         localObject1 = new Path2D.Float(paramShape, paramSunGraphics2D.transform);
/* 564 */         i = 0;
/* 565 */         j = 0;
/*     */       }
/* 567 */       fillPath(paramSunGraphics2D, (Path2D.Float)localObject1, i, j);
/* 568 */       return;
/*     */     }
/*     */ 
/* 572 */     if (paramSunGraphics2D.transformState <= 1)
/*     */     {
/* 577 */       localObject1 = null;
/* 578 */       i = paramSunGraphics2D.transX;
/* 579 */       j = paramSunGraphics2D.transY;
/*     */     }
/*     */     else {
/* 582 */       localObject1 = paramSunGraphics2D.transform;
/* 583 */       i = j = 0;
/*     */     }
/*     */ 
/* 586 */     ShapeSpanIterator localShapeSpanIterator = LoopPipe.getFillSSI(paramSunGraphics2D);
/*     */     try
/*     */     {
/* 590 */       Region localRegion = paramSunGraphics2D.getCompClip();
/* 591 */       localShapeSpanIterator.setOutputAreaXYXY(localRegion.getLoX() - i, localRegion.getLoY() - j, localRegion.getHiX() - i, localRegion.getHiY() - j);
/*     */ 
/* 595 */       localShapeSpanIterator.appendPath(paramShape.getPathIterator((AffineTransform)localObject1));
/* 596 */       fillSpans(paramSunGraphics2D, localShapeSpanIterator, i, j);
/*     */     } finally {
/* 598 */       localShapeSpanIterator.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class AAParallelogramPipe
/*     */     implements ParallelogramPipe
/*     */   {
/*     */     private AAParallelogramPipe()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*     */     {
/* 467 */       BufferedRenderPipe.this.rq.lock();
/*     */       try {
/* 469 */         BufferedRenderPipe.this.validateContextAA(paramSunGraphics2D);
/* 470 */         BufferedRenderPipe.this.rq.ensureCapacity(28);
/* 471 */         BufferedRenderPipe.this.buf.putInt(23);
/* 472 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble5);
/* 473 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble6);
/* 474 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble7);
/* 475 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble8);
/* 476 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble9);
/* 477 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble10);
/*     */       } finally {
/* 479 */         BufferedRenderPipe.this.rq.unlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*     */     {
/* 491 */       BufferedRenderPipe.this.rq.lock();
/*     */       try {
/* 493 */         BufferedRenderPipe.this.validateContextAA(paramSunGraphics2D);
/* 494 */         BufferedRenderPipe.this.rq.ensureCapacity(36);
/* 495 */         BufferedRenderPipe.this.buf.putInt(16);
/* 496 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble5);
/* 497 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble6);
/* 498 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble7);
/* 499 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble8);
/* 500 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble9);
/* 501 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble10);
/* 502 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble11);
/* 503 */         BufferedRenderPipe.this.buf.putFloat((float)paramDouble12);
/*     */       } finally {
/* 505 */         BufferedRenderPipe.this.rq.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class BufferedDrawHandler extends ProcessPath.DrawHandler
/*     */   {
/*     */     private int scanlineCount;
/*     */     private int scanlineCountIndex;
/*     */     private int remainingScanlines;
/*     */ 
/*     */     BufferedDrawHandler()
/*     */     {
/* 273 */       super(0, 0, 0);
/*     */     }
/*     */ 
/*     */     void validate(SunGraphics2D paramSunGraphics2D)
/*     */     {
/* 281 */       Region localRegion = paramSunGraphics2D.getCompClip();
/* 282 */       setBounds(localRegion.getLoX(), localRegion.getLoY(), localRegion.getHiX(), localRegion.getHiY(), paramSunGraphics2D.strokeHint);
/*     */     }
/*     */ 
/*     */     public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 293 */       BufferedRenderPipe.this.rq.ensureCapacity(20);
/* 294 */       BufferedRenderPipe.this.buf.putInt(10);
/* 295 */       BufferedRenderPipe.this.buf.putInt(paramInt1);
/* 296 */       BufferedRenderPipe.this.buf.putInt(paramInt2);
/* 297 */       BufferedRenderPipe.this.buf.putInt(paramInt3);
/* 298 */       BufferedRenderPipe.this.buf.putInt(paramInt4);
/*     */     }
/*     */ 
/*     */     public void drawPixel(int paramInt1, int paramInt2)
/*     */     {
/* 303 */       BufferedRenderPipe.this.rq.ensureCapacity(12);
/* 304 */       BufferedRenderPipe.this.buf.putInt(13);
/* 305 */       BufferedRenderPipe.this.buf.putInt(paramInt1);
/* 306 */       BufferedRenderPipe.this.buf.putInt(paramInt2);
/*     */     }
/*     */ 
/*     */     private void resetFillPath()
/*     */     {
/* 318 */       BufferedRenderPipe.this.buf.putInt(14);
/* 319 */       this.scanlineCountIndex = BufferedRenderPipe.this.buf.position();
/* 320 */       BufferedRenderPipe.this.buf.putInt(0);
/* 321 */       this.scanlineCount = 0;
/* 322 */       this.remainingScanlines = (BufferedRenderPipe.this.buf.remaining() / 12);
/*     */     }
/*     */ 
/*     */     private void updateScanlineCount() {
/* 326 */       BufferedRenderPipe.this.buf.putInt(this.scanlineCountIndex, this.scanlineCount);
/*     */     }
/*     */ 
/*     */     public void startFillPath()
/*     */     {
/* 334 */       BufferedRenderPipe.this.rq.ensureCapacity(20);
/* 335 */       resetFillPath();
/*     */     }
/*     */ 
/*     */     public void drawScanline(int paramInt1, int paramInt2, int paramInt3) {
/* 339 */       if (this.remainingScanlines == 0) {
/* 340 */         updateScanlineCount();
/* 341 */         BufferedRenderPipe.this.rq.flushNow();
/* 342 */         resetFillPath();
/*     */       }
/* 344 */       BufferedRenderPipe.this.buf.putInt(paramInt1);
/* 345 */       BufferedRenderPipe.this.buf.putInt(paramInt2);
/* 346 */       BufferedRenderPipe.this.buf.putInt(paramInt3);
/* 347 */       this.scanlineCount += 1;
/* 348 */       this.remainingScanlines -= 1;
/*     */     }
/*     */ 
/*     */     public void endFillPath()
/*     */     {
/* 356 */       updateScanlineCount();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedRenderPipe
 * JD-Core Version:    0.6.2
 */