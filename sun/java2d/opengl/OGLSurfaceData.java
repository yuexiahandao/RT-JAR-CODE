/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.security.AccessController;
/*     */ import sun.awt.image.PixelConverter.ArgbPre;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.SurfaceDataProxy;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.GraphicsPrimitive;
/*     */ import sun.java2d.loops.MaskFill;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.pipe.ParallelogramPipe;
/*     */ import sun.java2d.pipe.PixelToParallelogramConverter;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.TextPipe;
/*     */ import sun.java2d.pipe.hw.AccelSurface;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class OGLSurfaceData extends SurfaceData
/*     */   implements AccelSurface
/*     */ {
/*     */   public static final int PBUFFER = 2;
/*     */   public static final int FBOBJECT = 5;
/*     */   public static final int PF_INT_ARGB = 0;
/*     */   public static final int PF_INT_ARGB_PRE = 1;
/*     */   public static final int PF_INT_RGB = 2;
/*     */   public static final int PF_INT_RGBX = 3;
/*     */   public static final int PF_INT_BGR = 4;
/*     */   public static final int PF_INT_BGRX = 5;
/*     */   public static final int PF_USHORT_565_RGB = 6;
/*     */   public static final int PF_USHORT_555_RGB = 7;
/*     */   public static final int PF_USHORT_555_RGBX = 8;
/*     */   public static final int PF_BYTE_GRAY = 9;
/*     */   public static final int PF_USHORT_GRAY = 10;
/*     */   public static final int PF_3BYTE_BGR = 11;
/*     */   private static final String DESC_OPENGL_SURFACE = "OpenGL Surface";
/*     */   private static final String DESC_OPENGL_SURFACE_RTT = "OpenGL Surface (render-to-texture)";
/*     */   private static final String DESC_OPENGL_TEXTURE = "OpenGL Texture";
/* 133 */   static final SurfaceType OpenGLSurface = SurfaceType.Any.deriveSubType("OpenGL Surface", PixelConverter.ArgbPre.instance);
/*     */ 
/* 136 */   static final SurfaceType OpenGLSurfaceRTT = OpenGLSurface.deriveSubType("OpenGL Surface (render-to-texture)");
/*     */ 
/* 138 */   static final SurfaceType OpenGLTexture = SurfaceType.Any.deriveSubType("OpenGL Texture");
/*     */   private static boolean isFBObjectEnabled;
/*     */   private static boolean isLCDShaderEnabled;
/*     */   private static boolean isBIOpShaderEnabled;
/*     */   private static boolean isGradShaderEnabled;
/*     */   private OGLGraphicsConfig graphicsConfig;
/*     */   protected int type;
/*     */   private int nativeWidth;
/*     */   private int nativeHeight;
/*     */   protected static OGLRenderer oglRenderPipe;
/*     */   protected static PixelToParallelogramConverter oglTxRenderPipe;
/*     */   protected static ParallelogramPipe oglAAPgramPipe;
/*     */   protected static OGLTextRenderer oglTextPipe;
/*     */   protected static OGLDrawImage oglImagePipe;
/*     */ 
/*     */   protected native boolean initTexture(long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected native boolean initFBObject(long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected native boolean initFlipBackbuffer(long paramLong);
/*     */ 
/*     */   protected abstract boolean initPbuffer(long paramLong1, long paramLong2, boolean paramBoolean, int paramInt1, int paramInt2);
/*     */ 
/*     */   private native int getTextureTarget(long paramLong);
/*     */ 
/*     */   private native int getTextureID(long paramLong);
/*     */ 
/*     */   protected OGLSurfaceData(OGLGraphicsConfig paramOGLGraphicsConfig, ColorModel paramColorModel, int paramInt)
/*     */   {
/* 231 */     super(getCustomSurfaceType(paramInt), paramColorModel);
/* 232 */     this.graphicsConfig = paramOGLGraphicsConfig;
/* 233 */     this.type = paramInt;
/* 234 */     setBlitProxyKey(paramOGLGraphicsConfig.getProxyKey());
/*     */   }
/*     */ 
/*     */   public SurfaceDataProxy makeProxyFor(SurfaceData paramSurfaceData)
/*     */   {
/* 239 */     return OGLSurfaceDataProxy.createProxy(paramSurfaceData, this.graphicsConfig);
/*     */   }
/*     */ 
/*     */   private static SurfaceType getCustomSurfaceType(int paramInt)
/*     */   {
/* 247 */     switch (paramInt) {
/*     */     case 3:
/* 249 */       return OpenGLTexture;
/*     */     case 5:
/* 251 */       return OpenGLSurfaceRTT;
/*     */     case 2:
/*     */     case 4:
/* 254 */     }return OpenGLSurface;
/*     */   }
/*     */ 
/*     */   private void initSurfaceNow(int paramInt1, int paramInt2)
/*     */   {
/* 264 */     boolean bool1 = getTransparency() == 1;
/* 265 */     boolean bool2 = false;
/*     */ 
/* 267 */     switch (this.type) {
/*     */     case 2:
/* 269 */       bool2 = initPbuffer(getNativeOps(), this.graphicsConfig.getNativeConfigInfo(), bool1, paramInt1, paramInt2);
/*     */ 
/* 273 */       break;
/*     */     case 3:
/* 276 */       bool2 = initTexture(getNativeOps(), bool1, isTexNonPow2Available(), isTexRectAvailable(), paramInt1, paramInt2);
/*     */ 
/* 280 */       break;
/*     */     case 5:
/* 283 */       bool2 = initFBObject(getNativeOps(), bool1, isTexNonPow2Available(), isTexRectAvailable(), paramInt1, paramInt2);
/*     */ 
/* 287 */       break;
/*     */     case 4:
/* 290 */       bool2 = initFlipBackbuffer(getNativeOps());
/* 291 */       break;
/*     */     }
/*     */ 
/* 297 */     if (!bool2)
/* 298 */       throw new OutOfMemoryError("can't create offscreen surface");
/*     */   }
/*     */ 
/*     */   protected void initSurface(final int paramInt1, final int paramInt2)
/*     */   {
/* 308 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 309 */     localOGLRenderQueue.lock();
/*     */     try {
/* 311 */       switch (this.type)
/*     */       {
/*     */       case 2:
/*     */       case 3:
/*     */       case 5:
/* 317 */         OGLContext.setScratchSurface(this.graphicsConfig);
/* 318 */         break;
/*     */       case 4:
/*     */       }
/*     */ 
/* 322 */       localOGLRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 324 */           OGLSurfaceData.this.initSurfaceNow(paramInt1, paramInt2);
/*     */         } } );
/*     */     }
/*     */     finally {
/* 328 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final OGLContext getContext()
/*     */   {
/* 337 */     return this.graphicsConfig.getContext();
/*     */   }
/*     */ 
/*     */   final OGLGraphicsConfig getOGLGraphicsConfig()
/*     */   {
/* 344 */     return this.graphicsConfig;
/*     */   }
/*     */ 
/*     */   public final int getType()
/*     */   {
/* 351 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final int getTextureTarget()
/*     */   {
/* 360 */     return getTextureTarget(getNativeOps());
/*     */   }
/*     */ 
/*     */   public final int getTextureID()
/*     */   {
/* 369 */     return getTextureID(getNativeOps());
/*     */   }
/*     */ 
/*     */   public long getNativeResource(int paramInt)
/*     */   {
/* 390 */     if (paramInt == 3) {
/* 391 */       return getTextureID();
/*     */     }
/* 393 */     return 0L;
/*     */   }
/*     */ 
/*     */   public Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 397 */     throw new InternalError("not implemented yet");
/*     */   }
/*     */ 
/*     */   public boolean canRenderLCDText(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 412 */     return (this.graphicsConfig.isCapPresent(131072)) && (paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.paintState <= 0) && (paramSunGraphics2D.surfaceData.getTransparency() == 1);
/*     */   }
/*     */ 
/*     */   public void validatePipe(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 421 */     int i = 0;
/*     */     Object localObject;
/* 430 */     if (((paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.paintState <= 1)) || ((paramSunGraphics2D.compositeState == 1) && (paramSunGraphics2D.paintState <= 1) && (((AlphaComposite)paramSunGraphics2D.composite).getRule() == 3)) || ((paramSunGraphics2D.compositeState == 2) && (paramSunGraphics2D.paintState <= 1)))
/*     */     {
/* 444 */       localObject = oglTextPipe;
/*     */     }
/*     */     else
/*     */     {
/* 448 */       super.validatePipe(paramSunGraphics2D);
/* 449 */       localObject = paramSunGraphics2D.textpipe;
/* 450 */       i = 1;
/*     */     }
/*     */ 
/* 453 */     PixelToParallelogramConverter localPixelToParallelogramConverter1 = null;
/* 454 */     OGLRenderer localOGLRenderer = null;
/*     */ 
/* 456 */     if (paramSunGraphics2D.antialiasHint != 2) {
/* 457 */       if (paramSunGraphics2D.paintState <= 1) {
/* 458 */         if (paramSunGraphics2D.compositeState <= 2) {
/* 459 */           localPixelToParallelogramConverter1 = oglTxRenderPipe;
/* 460 */           localOGLRenderer = oglRenderPipe;
/*     */         }
/* 462 */       } else if ((paramSunGraphics2D.compositeState <= 1) && 
/* 463 */         (OGLPaints.isValid(paramSunGraphics2D))) {
/* 464 */         localPixelToParallelogramConverter1 = oglTxRenderPipe;
/* 465 */         localOGLRenderer = oglRenderPipe;
/*     */       }
/*     */ 
/*     */     }
/* 470 */     else if (paramSunGraphics2D.paintState <= 1) {
/* 471 */       if ((this.graphicsConfig.isCapPresent(256)) && ((paramSunGraphics2D.imageComp == CompositeType.SrcOverNoEa) || (paramSunGraphics2D.imageComp == CompositeType.SrcOver)))
/*     */       {
/* 475 */         if (i == 0) {
/* 476 */           super.validatePipe(paramSunGraphics2D);
/* 477 */           i = 1;
/*     */         }
/* 479 */         PixelToParallelogramConverter localPixelToParallelogramConverter2 = new PixelToParallelogramConverter(paramSunGraphics2D.shapepipe, oglAAPgramPipe, 0.125D, 0.499D, false);
/*     */ 
/* 484 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter2;
/* 485 */         paramSunGraphics2D.fillpipe = localPixelToParallelogramConverter2;
/* 486 */         paramSunGraphics2D.shapepipe = localPixelToParallelogramConverter2;
/* 487 */       } else if (paramSunGraphics2D.compositeState == 2)
/*     */       {
/* 489 */         localPixelToParallelogramConverter1 = oglTxRenderPipe;
/* 490 */         localOGLRenderer = oglRenderPipe;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 496 */     if (localPixelToParallelogramConverter1 != null) {
/* 497 */       if (paramSunGraphics2D.transformState >= 3) {
/* 498 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter1;
/* 499 */         paramSunGraphics2D.fillpipe = localPixelToParallelogramConverter1;
/* 500 */       } else if (paramSunGraphics2D.strokeState != 0) {
/* 501 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter1;
/* 502 */         paramSunGraphics2D.fillpipe = localOGLRenderer;
/*     */       } else {
/* 504 */         paramSunGraphics2D.drawpipe = localOGLRenderer;
/* 505 */         paramSunGraphics2D.fillpipe = localOGLRenderer;
/*     */       }
/*     */ 
/* 511 */       paramSunGraphics2D.shapepipe = localPixelToParallelogramConverter1;
/*     */     }
/* 513 */     else if (i == 0) {
/* 514 */       super.validatePipe(paramSunGraphics2D);
/*     */     }
/*     */ 
/* 519 */     paramSunGraphics2D.textpipe = ((TextPipe)localObject);
/*     */ 
/* 522 */     paramSunGraphics2D.imagepipe = oglImagePipe;
/*     */   }
/*     */ 
/*     */   protected MaskFill getMaskFill(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 527 */     if (paramSunGraphics2D.paintState > 1)
/*     */     {
/* 539 */       if ((!OGLPaints.isValid(paramSunGraphics2D)) || (!this.graphicsConfig.isCapPresent(16)))
/*     */       {
/* 542 */         return null;
/*     */       }
/*     */     }
/* 545 */     return super.getMaskFill(paramSunGraphics2D);
/*     */   }
/*     */ 
/*     */   public boolean copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 551 */     if ((paramSunGraphics2D.transformState < 3) && (paramSunGraphics2D.compositeState < 2))
/*     */     {
/* 554 */       paramInt1 += paramSunGraphics2D.transX;
/* 555 */       paramInt2 += paramSunGraphics2D.transY;
/*     */ 
/* 557 */       oglRenderPipe.copyArea(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 559 */       return true;
/*     */     }
/* 561 */     return false;
/*     */   }
/*     */ 
/*     */   public void flush() {
/* 565 */     invalidate();
/* 566 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 567 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 571 */       OGLContext.setScratchSurface(this.graphicsConfig);
/*     */ 
/* 573 */       RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/* 574 */       localOGLRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 575 */       localRenderBuffer.putInt(72);
/* 576 */       localRenderBuffer.putLong(getNativeOps());
/*     */ 
/* 579 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 581 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void dispose(long paramLong1, long paramLong2)
/*     */   {
/* 594 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 595 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 599 */       OGLContext.setScratchSurface(paramLong2);
/*     */ 
/* 601 */       RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/* 602 */       localOGLRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 603 */       localRenderBuffer.putInt(73);
/* 604 */       localRenderBuffer.putLong(paramLong1);
/*     */ 
/* 607 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 609 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void swapBuffers(long paramLong) {
/* 614 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 615 */     localOGLRenderQueue.lock();
/*     */     try {
/* 617 */       RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/* 618 */       localOGLRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 619 */       localRenderBuffer.putInt(80);
/* 620 */       localRenderBuffer.putLong(paramLong);
/* 621 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 623 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isTexNonPow2Available()
/*     */   {
/* 632 */     return this.graphicsConfig.isCapPresent(32);
/*     */   }
/*     */ 
/*     */   boolean isTexRectAvailable()
/*     */   {
/* 641 */     return this.graphicsConfig.isCapPresent(1048576);
/*     */   }
/*     */ 
/*     */   public Rectangle getNativeBounds() {
/* 645 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 646 */     localOGLRenderQueue.lock();
/*     */     try {
/* 648 */       return new Rectangle(this.nativeWidth, this.nativeHeight);
/*     */     } finally {
/* 650 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isOnScreen()
/*     */   {
/* 661 */     return getType() == 1;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 182 */     if (!GraphicsEnvironment.isHeadless())
/*     */     {
/* 184 */       String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.fbobject"));
/*     */ 
/* 187 */       isFBObjectEnabled = !"false".equals(str1);
/*     */ 
/* 190 */       String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.lcdshader"));
/*     */ 
/* 193 */       isLCDShaderEnabled = !"false".equals(str2);
/*     */ 
/* 196 */       String str3 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.biopshader"));
/*     */ 
/* 199 */       isBIOpShaderEnabled = !"false".equals(str3);
/*     */ 
/* 202 */       String str4 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.gradshader"));
/*     */ 
/* 205 */       isGradShaderEnabled = !"false".equals(str4);
/*     */ 
/* 207 */       OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 208 */       oglImagePipe = new OGLDrawImage();
/* 209 */       oglTextPipe = new OGLTextRenderer(localOGLRenderQueue);
/* 210 */       oglRenderPipe = new OGLRenderer(localOGLRenderQueue);
/* 211 */       if (GraphicsPrimitive.tracingEnabled()) {
/* 212 */         oglTextPipe = oglTextPipe.traceWrap();
/*     */       }
/*     */ 
/* 216 */       oglAAPgramPipe = oglRenderPipe.getAAParallelogramPipe();
/* 217 */       oglTxRenderPipe = new PixelToParallelogramConverter(oglRenderPipe, oglRenderPipe, 1.0D, 0.25D, true);
/*     */ 
/* 222 */       OGLBlitLoops.register();
/* 223 */       OGLMaskFill.register();
/* 224 */       OGLMaskBlit.register();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLSurfaceData
 * JD-Core Version:    0.6.2
 */