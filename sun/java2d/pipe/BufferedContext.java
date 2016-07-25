/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Paint;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.loops.XORComposite;
/*     */ import sun.java2d.pipe.hw.AccelSurface;
/*     */ 
/*     */ public abstract class BufferedContext
/*     */ {
/*     */   public static final int NO_CONTEXT_FLAGS = 0;
/*     */   public static final int SRC_IS_OPAQUE = 1;
/*     */   public static final int USE_MASK = 2;
/*     */   protected RenderQueue rq;
/*     */   protected RenderBuffer buf;
/*     */   protected static BufferedContext currentContext;
/*     */   private AccelSurface validatedSrcData;
/*     */   private AccelSurface validatedDstData;
/*     */   private Region validatedClip;
/*     */   private Composite validatedComp;
/*     */   private Paint validatedPaint;
/*     */   private boolean isValidatedPaintJustAColor;
/*     */   private int validatedRGB;
/*     */   private int validatedFlags;
/*     */   private boolean xformInUse;
/*     */   private AffineTransform transform;
/*     */ 
/*     */   protected BufferedContext(RenderQueue paramRenderQueue)
/*     */   {
/* 101 */     this.rq = paramRenderQueue;
/* 102 */     this.buf = paramRenderQueue.getBuffer();
/*     */   }
/*     */ 
/*     */   public static void validateContext(AccelSurface paramAccelSurface1, AccelSurface paramAccelSurface2, Region paramRegion, Composite paramComposite, AffineTransform paramAffineTransform, Paint paramPaint, SunGraphics2D paramSunGraphics2D, int paramInt)
/*     */   {
/* 128 */     BufferedContext localBufferedContext = paramAccelSurface2.getContext();
/* 129 */     localBufferedContext.validate(paramAccelSurface1, paramAccelSurface2, paramRegion, paramComposite, paramAffineTransform, paramPaint, paramSunGraphics2D, paramInt);
/*     */   }
/*     */ 
/*     */   public static void validateContext(AccelSurface paramAccelSurface)
/*     */   {
/* 148 */     validateContext(paramAccelSurface, paramAccelSurface, null, null, null, null, null, 0);
/*     */   }
/*     */ 
/*     */   public void validate(AccelSurface paramAccelSurface1, AccelSurface paramAccelSurface2, Region paramRegion, Composite paramComposite, AffineTransform paramAffineTransform, Paint paramPaint, SunGraphics2D paramSunGraphics2D, int paramInt)
/*     */   {
/* 178 */     int i = 0;
/* 179 */     int j = 0;
/*     */ 
/* 181 */     if ((!paramAccelSurface2.isValid()) || (paramAccelSurface2.isSurfaceLost()) || (paramAccelSurface1.isSurfaceLost()))
/*     */     {
/* 184 */       invalidateContext();
/* 185 */       throw new InvalidPipeException("bounds changed or surface lost");
/*     */     }
/*     */ 
/* 188 */     if ((paramPaint instanceof Color))
/*     */     {
/* 190 */       k = ((Color)paramPaint).getRGB();
/* 191 */       if (this.isValidatedPaintJustAColor) {
/* 192 */         if (k != this.validatedRGB) {
/* 193 */           this.validatedRGB = k;
/* 194 */           j = 1;
/*     */         }
/*     */       } else {
/* 197 */         this.validatedRGB = k;
/* 198 */         j = 1;
/* 199 */         this.isValidatedPaintJustAColor = true;
/*     */       }
/* 201 */     } else if (this.validatedPaint != paramPaint) {
/* 202 */       j = 1;
/*     */ 
/* 205 */       this.isValidatedPaintJustAColor = false;
/*     */     }
/*     */ 
/* 208 */     if ((currentContext != this) || (paramAccelSurface1 != this.validatedSrcData) || (paramAccelSurface2 != this.validatedDstData))
/*     */     {
/* 212 */       if (paramAccelSurface2 != this.validatedDstData)
/*     */       {
/* 215 */         i = 1;
/*     */       }
/*     */ 
/* 218 */       if (paramPaint == null)
/*     */       {
/* 222 */         j = 1;
/*     */       }
/*     */ 
/* 226 */       setSurfaces(paramAccelSurface1, paramAccelSurface2);
/*     */ 
/* 228 */       currentContext = this;
/* 229 */       this.validatedSrcData = paramAccelSurface1;
/* 230 */       this.validatedDstData = paramAccelSurface2;
/*     */     }
/*     */ 
/* 234 */     if ((paramRegion != this.validatedClip) || (i != 0)) {
/* 235 */       if (paramRegion != null) {
/* 236 */         if ((i != 0) || (this.validatedClip == null) || (!this.validatedClip.isRectangular()) || (!paramRegion.isRectangular()) || (paramRegion.getLoX() != this.validatedClip.getLoX()) || (paramRegion.getLoY() != this.validatedClip.getLoY()) || (paramRegion.getHiX() != this.validatedClip.getHiX()) || (paramRegion.getHiY() != this.validatedClip.getHiY()))
/*     */         {
/* 244 */           setClip(paramRegion);
/*     */         }
/*     */       }
/* 247 */       else resetClip();
/*     */ 
/* 249 */       this.validatedClip = paramRegion;
/*     */     }
/*     */ 
/* 255 */     if ((paramComposite != this.validatedComp) || (paramInt != this.validatedFlags)) {
/* 256 */       if (paramComposite != null)
/* 257 */         setComposite(paramComposite, paramInt);
/*     */       else {
/* 259 */         resetComposite();
/*     */       }
/*     */ 
/* 263 */       j = 1;
/* 264 */       this.validatedComp = paramComposite;
/* 265 */       this.validatedFlags = paramInt;
/*     */     }
/*     */ 
/* 269 */     int k = 0;
/* 270 */     if (paramAffineTransform == null) {
/* 271 */       if (this.xformInUse) {
/* 272 */         resetTransform();
/* 273 */         this.xformInUse = false;
/* 274 */         k = 1;
/* 275 */       } else if ((paramSunGraphics2D != null) && (!paramSunGraphics2D.transform.equals(this.transform))) {
/* 276 */         k = 1;
/*     */       }
/* 278 */       if ((paramSunGraphics2D != null) && (k != 0))
/* 279 */         this.transform = new AffineTransform(paramSunGraphics2D.transform);
/*     */     }
/*     */     else {
/* 282 */       setTransform(paramAffineTransform);
/* 283 */       this.xformInUse = true;
/* 284 */       k = 1;
/*     */     }
/*     */ 
/* 287 */     if ((!this.isValidatedPaintJustAColor) && (k != 0)) {
/* 288 */       j = 1;
/*     */     }
/*     */ 
/* 292 */     if (j != 0) {
/* 293 */       if (paramPaint != null)
/* 294 */         BufferedPaints.setPaint(this.rq, paramSunGraphics2D, paramPaint, paramInt);
/*     */       else {
/* 296 */         BufferedPaints.resetPaint(this.rq);
/*     */       }
/* 298 */       this.validatedPaint = paramPaint;
/*     */     }
/*     */ 
/* 303 */     paramAccelSurface2.markDirty();
/*     */   }
/*     */ 
/*     */   public void invalidateSurfaces()
/*     */   {
/* 317 */     this.validatedSrcData = null;
/* 318 */     this.validatedDstData = null;
/*     */   }
/*     */ 
/*     */   private void setSurfaces(AccelSurface paramAccelSurface1, AccelSurface paramAccelSurface2)
/*     */   {
/* 325 */     this.rq.ensureCapacityAndAlignment(20, 4);
/* 326 */     this.buf.putInt(70);
/* 327 */     this.buf.putLong(paramAccelSurface1.getNativeOps());
/* 328 */     this.buf.putLong(paramAccelSurface2.getNativeOps());
/*     */   }
/*     */ 
/*     */   private void resetClip()
/*     */   {
/* 333 */     this.rq.ensureCapacity(4);
/* 334 */     this.buf.putInt(55);
/*     */   }
/*     */ 
/*     */   private void setClip(Region paramRegion)
/*     */   {
/* 339 */     if (paramRegion.isRectangular()) {
/* 340 */       this.rq.ensureCapacity(20);
/* 341 */       this.buf.putInt(51);
/* 342 */       this.buf.putInt(paramRegion.getLoX()).putInt(paramRegion.getLoY());
/* 343 */       this.buf.putInt(paramRegion.getHiX()).putInt(paramRegion.getHiY());
/*     */     } else {
/* 345 */       this.rq.ensureCapacity(28);
/* 346 */       this.buf.putInt(52);
/* 347 */       this.buf.putInt(53);
/*     */ 
/* 349 */       int i = this.buf.position();
/* 350 */       this.buf.putInt(0);
/* 351 */       int j = 0;
/* 352 */       int k = this.buf.remaining() / 16;
/* 353 */       int[] arrayOfInt = new int[4];
/* 354 */       SpanIterator localSpanIterator = paramRegion.getSpanIterator();
/* 355 */       while (localSpanIterator.nextSpan(arrayOfInt)) {
/* 356 */         if (k == 0) {
/* 357 */           this.buf.putInt(i, j);
/* 358 */           this.rq.flushNow();
/* 359 */           this.buf.putInt(53);
/* 360 */           i = this.buf.position();
/* 361 */           this.buf.putInt(0);
/* 362 */           j = 0;
/* 363 */           k = this.buf.remaining() / 16;
/*     */         }
/* 365 */         this.buf.putInt(arrayOfInt[0]);
/* 366 */         this.buf.putInt(arrayOfInt[1]);
/* 367 */         this.buf.putInt(arrayOfInt[2]);
/* 368 */         this.buf.putInt(arrayOfInt[3]);
/* 369 */         j++;
/* 370 */         k--;
/*     */       }
/* 372 */       this.buf.putInt(i, j);
/* 373 */       this.rq.ensureCapacity(4);
/* 374 */       this.buf.putInt(54);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resetComposite()
/*     */   {
/* 380 */     this.rq.ensureCapacity(4);
/* 381 */     this.buf.putInt(58);
/*     */   }
/*     */ 
/*     */   private void setComposite(Composite paramComposite, int paramInt)
/*     */   {
/* 386 */     if ((paramComposite instanceof AlphaComposite)) {
/* 387 */       AlphaComposite localAlphaComposite = (AlphaComposite)paramComposite;
/* 388 */       this.rq.ensureCapacity(16);
/* 389 */       this.buf.putInt(56);
/* 390 */       this.buf.putInt(localAlphaComposite.getRule());
/* 391 */       this.buf.putFloat(localAlphaComposite.getAlpha());
/* 392 */       this.buf.putInt(paramInt);
/* 393 */     } else if ((paramComposite instanceof XORComposite)) {
/* 394 */       int i = ((XORComposite)paramComposite).getXorPixel();
/* 395 */       this.rq.ensureCapacity(8);
/* 396 */       this.buf.putInt(57);
/* 397 */       this.buf.putInt(i);
/*     */     } else {
/* 399 */       throw new InternalError("not yet implemented");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void resetTransform()
/*     */   {
/* 405 */     this.rq.ensureCapacity(4);
/* 406 */     this.buf.putInt(60);
/*     */   }
/*     */ 
/*     */   private void setTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 411 */     this.rq.ensureCapacityAndAlignment(52, 4);
/* 412 */     this.buf.putInt(59);
/* 413 */     this.buf.putDouble(paramAffineTransform.getScaleX());
/* 414 */     this.buf.putDouble(paramAffineTransform.getShearY());
/* 415 */     this.buf.putDouble(paramAffineTransform.getShearX());
/* 416 */     this.buf.putDouble(paramAffineTransform.getScaleY());
/* 417 */     this.buf.putDouble(paramAffineTransform.getTranslateX());
/* 418 */     this.buf.putDouble(paramAffineTransform.getTranslateY());
/*     */   }
/*     */ 
/*     */   public void invalidateContext()
/*     */   {
/* 430 */     resetTransform();
/* 431 */     resetComposite();
/* 432 */     resetClip();
/* 433 */     BufferedPaints.resetPaint(this.rq);
/* 434 */     invalidateSurfaces();
/* 435 */     this.validatedComp = null;
/* 436 */     this.validatedClip = null;
/* 437 */     this.validatedPaint = null;
/* 438 */     this.isValidatedPaintJustAColor = false;
/* 439 */     this.xformInUse = false;
/*     */   }
/*     */ 
/*     */   public abstract RenderQueue getRenderQueue();
/*     */ 
/*     */   public abstract void saveState();
/*     */ 
/*     */   public abstract void restoreState();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.BufferedContext
 * JD-Core Version:    0.6.2
 */