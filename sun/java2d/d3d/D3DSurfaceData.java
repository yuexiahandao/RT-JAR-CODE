/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BufferCapabilities;
/*     */ import java.awt.BufferCapabilities.FlipContents;
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.image.DataBufferNative;
/*     */ import sun.awt.image.PixelConverter.ArgbPre;
/*     */ import sun.awt.image.SunVolatileImage;
/*     */ import sun.awt.image.SurfaceManager;
/*     */ import sun.awt.image.WritableRasterNative;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.ScreenUpdateManager;
/*     */ import sun.java2d.StateTracker;
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
/*     */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
/*     */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities.VSyncType;
/*     */ 
/*     */ public class D3DSurfaceData extends SurfaceData
/*     */   implements AccelSurface
/*     */ {
/*     */   public static final int D3D_DEVICE_RESOURCE = 100;
/*     */   public static final int ST_INT_ARGB = 0;
/*     */   public static final int ST_INT_ARGB_PRE = 1;
/*     */   public static final int ST_INT_ARGB_BM = 2;
/*     */   public static final int ST_INT_RGB = 3;
/*     */   public static final int ST_INT_BGR = 4;
/*     */   public static final int ST_USHORT_565_RGB = 5;
/*     */   public static final int ST_USHORT_555_RGB = 6;
/*     */   public static final int ST_BYTE_INDEXED = 7;
/*     */   public static final int ST_BYTE_INDEXED_BM = 8;
/*     */   public static final int ST_3BYTE_BGR = 9;
/*     */   public static final int SWAP_DISCARD = 1;
/*     */   public static final int SWAP_FLIP = 2;
/*     */   public static final int SWAP_COPY = 3;
/*     */   private static final String DESC_D3D_SURFACE = "D3D Surface";
/*     */   private static final String DESC_D3D_SURFACE_RTT = "D3D Surface (render-to-texture)";
/*     */   private static final String DESC_D3D_TEXTURE = "D3D Texture";
/* 155 */   static final SurfaceType D3DSurface = SurfaceType.Any.deriveSubType("D3D Surface", PixelConverter.ArgbPre.instance);
/*     */ 
/* 158 */   static final SurfaceType D3DSurfaceRTT = D3DSurface.deriveSubType("D3D Surface (render-to-texture)");
/*     */ 
/* 160 */   static final SurfaceType D3DTexture = SurfaceType.Any.deriveSubType("D3D Texture");
/*     */   private int type;
/*     */   private int width;
/*     */   private int height;
/*     */   private int nativeWidth;
/*     */   private int nativeHeight;
/*     */   protected WComponentPeer peer;
/*     */   private Image offscreenImage;
/*     */   protected D3DGraphicsDevice graphicsDevice;
/*     */   private int swapEffect;
/*     */   private ExtendedBufferCapabilities.VSyncType syncType;
/*     */   private int backBuffersNum;
/*     */   private WritableRasterNative wrn;
/*     */   protected static D3DRenderer d3dRenderPipe;
/*     */   protected static PixelToParallelogramConverter d3dTxRenderPipe;
/*     */   protected static ParallelogramPipe d3dAAPgramPipe;
/*     */   protected static D3DTextRenderer d3dTextPipe;
/*     */   protected static D3DDrawImage d3dImagePipe;
/*     */ 
/*     */   private native boolean initTexture(long paramLong, boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   private native boolean initFlipBackbuffer(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   private native boolean initRTSurface(long paramLong, boolean paramBoolean);
/*     */ 
/*     */   private native void initOps(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   protected D3DSurfaceData(WComponentPeer paramWComponentPeer, D3DGraphicsConfig paramD3DGraphicsConfig, int paramInt1, int paramInt2, Image paramImage, ColorModel paramColorModel, int paramInt3, int paramInt4, ExtendedBufferCapabilities.VSyncType paramVSyncType, int paramInt5)
/*     */   {
/* 219 */     super(getCustomSurfaceType(paramInt5), paramColorModel);
/* 220 */     this.graphicsDevice = paramD3DGraphicsConfig.getD3DDevice();
/* 221 */     this.peer = paramWComponentPeer;
/* 222 */     this.type = paramInt5;
/* 223 */     this.width = paramInt1;
/* 224 */     this.height = paramInt2;
/* 225 */     this.offscreenImage = paramImage;
/* 226 */     this.backBuffersNum = paramInt3;
/* 227 */     this.swapEffect = paramInt4;
/* 228 */     this.syncType = paramVSyncType;
/*     */ 
/* 230 */     initOps(this.graphicsDevice.getScreen(), paramInt1, paramInt2);
/* 231 */     if (paramInt5 == 1)
/*     */     {
/* 236 */       setSurfaceLost(true);
/*     */     }
/* 238 */     else initSurface();
/*     */ 
/* 240 */     setBlitProxyKey(paramD3DGraphicsConfig.getProxyKey());
/*     */   }
/*     */ 
/*     */   public SurfaceDataProxy makeProxyFor(SurfaceData paramSurfaceData)
/*     */   {
/* 245 */     return D3DSurfaceDataProxy.createProxy(paramSurfaceData, (D3DGraphicsConfig)this.graphicsDevice.getDefaultConfiguration());
/*     */   }
/*     */ 
/*     */   public static D3DSurfaceData createData(WComponentPeer paramWComponentPeer, Image paramImage)
/*     */   {
/* 255 */     D3DGraphicsConfig localD3DGraphicsConfig = getGC(paramWComponentPeer);
/* 256 */     if ((localD3DGraphicsConfig == null) || (!paramWComponentPeer.isAccelCapable())) {
/* 257 */       return null;
/*     */     }
/* 259 */     BufferCapabilities localBufferCapabilities = paramWComponentPeer.getBackBufferCaps();
/* 260 */     ExtendedBufferCapabilities.VSyncType localVSyncType = ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT;
/* 261 */     if ((localBufferCapabilities instanceof ExtendedBufferCapabilities)) {
/* 262 */       localVSyncType = ((ExtendedBufferCapabilities)localBufferCapabilities).getVSync();
/*     */     }
/* 264 */     Rectangle localRectangle = paramWComponentPeer.getBounds();
/* 265 */     BufferCapabilities.FlipContents localFlipContents = localBufferCapabilities.getFlipContents();
/*     */     int i;
/* 267 */     if (localFlipContents == BufferCapabilities.FlipContents.COPIED)
/* 268 */       i = 3;
/* 269 */     else if (localFlipContents == BufferCapabilities.FlipContents.PRIOR)
/* 270 */       i = 2;
/*     */     else {
/* 272 */       i = 1;
/*     */     }
/* 274 */     return new D3DSurfaceData(paramWComponentPeer, localD3DGraphicsConfig, localRectangle.width, localRectangle.height, paramImage, paramWComponentPeer.getColorModel(), paramWComponentPeer.getBackBuffersNum(), i, localVSyncType, 4);
/*     */   }
/*     */ 
/*     */   public static D3DSurfaceData createData(WComponentPeer paramWComponentPeer)
/*     */   {
/* 295 */     D3DGraphicsConfig localD3DGraphicsConfig = getGC(paramWComponentPeer);
/* 296 */     if ((localD3DGraphicsConfig == null) || (!paramWComponentPeer.isAccelCapable())) {
/* 297 */       return null;
/*     */     }
/* 299 */     return new D3DWindowSurfaceData(paramWComponentPeer, localD3DGraphicsConfig);
/*     */   }
/*     */ 
/*     */   public static D3DSurfaceData createData(D3DGraphicsConfig paramD3DGraphicsConfig, int paramInt1, int paramInt2, ColorModel paramColorModel, Image paramImage, int paramInt3)
/*     */   {
/* 311 */     if (paramInt3 == 5) {
/* 312 */       int i = paramColorModel.getTransparency() == 1 ? 1 : 0;
/* 313 */       int j = i != 0 ? 8 : 4;
/* 314 */       if (!paramD3DGraphicsConfig.getD3DDevice().isCapPresent(j)) {
/* 315 */         paramInt3 = 2;
/*     */       }
/*     */     }
/* 318 */     D3DSurfaceData localD3DSurfaceData = null;
/*     */     try {
/* 320 */       localD3DSurfaceData = new D3DSurfaceData(null, paramD3DGraphicsConfig, paramInt1, paramInt2, paramImage, paramColorModel, 0, 1, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, paramInt3);
/*     */     }
/*     */     catch (InvalidPipeException localInvalidPipeException)
/*     */     {
/* 326 */       if (paramInt3 == 5)
/*     */       {
/* 330 */         if (((SunVolatileImage)paramImage).getForcedAccelSurfaceType() != 5)
/*     */         {
/* 333 */           paramInt3 = 2;
/* 334 */           localD3DSurfaceData = new D3DSurfaceData(null, paramD3DGraphicsConfig, paramInt1, paramInt2, paramImage, paramColorModel, 0, 1, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, paramInt3);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 340 */     return localD3DSurfaceData;
/*     */   }
/*     */ 
/*     */   private static SurfaceType getCustomSurfaceType(int paramInt)
/*     */   {
/* 348 */     switch (paramInt) {
/*     */     case 3:
/* 350 */       return D3DTexture;
/*     */     case 5:
/* 352 */       return D3DSurfaceRTT;
/*     */     }
/* 354 */     return D3DSurface;
/*     */   }
/*     */ 
/*     */   private boolean initSurfaceNow()
/*     */   {
/* 359 */     boolean bool = getTransparency() == 1;
/* 360 */     switch (this.type) {
/*     */     case 2:
/* 362 */       return initRTSurface(getNativeOps(), bool);
/*     */     case 3:
/* 364 */       return initTexture(getNativeOps(), false, bool);
/*     */     case 5:
/* 366 */       return initTexture(getNativeOps(), true, bool);
/*     */     case 1:
/*     */     case 4:
/* 372 */       return initFlipBackbuffer(getNativeOps(), this.peer.getData(), this.backBuffersNum, this.swapEffect, this.syncType.id());
/*     */     }
/*     */ 
/* 376 */     return false;
/*     */   }
/*     */ 
/*     */   protected void initSurface()
/*     */   {
/* 387 */     synchronized (this) {
/* 388 */       this.wrn = null;
/*     */     }
/*     */ 
/* 394 */     ??? = new Object()
/*     */     {
/* 392 */       boolean success = false;
/*     */     };
/* 395 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 396 */     localD3DRenderQueue.lock();
/*     */     try {
/* 398 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 400 */           this.val$status.success = D3DSurfaceData.this.initSurfaceNow();
/*     */         }
/*     */       });
/* 403 */       if (!((1Status)???).success)
/* 404 */         throw new InvalidPipeException("Error creating D3DSurface");
/*     */     }
/*     */     finally {
/* 407 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final D3DContext getContext()
/*     */   {
/* 416 */     return this.graphicsDevice.getContext();
/*     */   }
/*     */ 
/*     */   public final int getType()
/*     */   {
/* 423 */     return this.type;
/*     */   }
/*     */ 
/*     */   private static native int dbGetPixelNative(long paramLong, int paramInt1, int paramInt2);
/*     */ 
/*     */   private static native void dbSetPixelNative(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public synchronized Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 483 */     if (this.wrn == null) {
/* 484 */       DirectColorModel localDirectColorModel = (DirectColorModel)getColorModel();
/*     */ 
/* 486 */       int i = 0;
/* 487 */       int j = this.width;
/*     */ 
/* 489 */       if (localDirectColorModel.getPixelSize() > 16) {
/* 490 */         i = 3;
/*     */       }
/*     */       else {
/* 493 */         i = 1;
/*     */       }
/*     */ 
/* 498 */       SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = new SinglePixelPackedSampleModel(i, this.width, this.height, j, localDirectColorModel.getMasks());
/*     */ 
/* 500 */       D3DDataBufferNative localD3DDataBufferNative = new D3DDataBufferNative(this, i, this.width, this.height);
/*     */ 
/* 502 */       this.wrn = WritableRasterNative.createNativeRaster(localSinglePixelPackedSampleModel, localD3DDataBufferNative);
/*     */     }
/*     */ 
/* 505 */     return this.wrn;
/*     */   }
/*     */ 
/*     */   public boolean canRenderLCDText(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 516 */     return (this.graphicsDevice.isCapPresent(65536)) && (paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.paintState <= 0) && (paramSunGraphics2D.surfaceData.getTransparency() == 1);
/*     */   }
/*     */ 
/*     */   void disableAccelerationForSurface()
/*     */   {
/* 529 */     if (this.offscreenImage != null) {
/* 530 */       SurfaceManager localSurfaceManager = SurfaceManager.getManager(this.offscreenImage);
/* 531 */       if ((localSurfaceManager instanceof D3DVolatileSurfaceManager)) {
/* 532 */         setSurfaceLost(true);
/* 533 */         ((D3DVolatileSurfaceManager)localSurfaceManager).setAccelerationEnabled(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void validatePipe(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 540 */     int i = 0;
/*     */ 
/* 545 */     if (paramSunGraphics2D.compositeState >= 2) {
/* 546 */       super.validatePipe(paramSunGraphics2D);
/* 547 */       paramSunGraphics2D.imagepipe = d3dImagePipe;
/* 548 */       disableAccelerationForSurface();
/*     */       return;
/*     */     }
/*     */     Object localObject;
/* 559 */     if (((paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.paintState <= 1)) || ((paramSunGraphics2D.compositeState == 1) && (paramSunGraphics2D.paintState <= 1) && (((AlphaComposite)paramSunGraphics2D.composite).getRule() == 3)) || ((paramSunGraphics2D.compositeState == 2) && (paramSunGraphics2D.paintState <= 1)))
/*     */     {
/* 573 */       localObject = d3dTextPipe;
/*     */     }
/*     */     else
/*     */     {
/* 577 */       super.validatePipe(paramSunGraphics2D);
/* 578 */       localObject = paramSunGraphics2D.textpipe;
/* 579 */       i = 1;
/*     */     }
/*     */ 
/* 582 */     PixelToParallelogramConverter localPixelToParallelogramConverter1 = null;
/* 583 */     D3DRenderer localD3DRenderer = null;
/*     */ 
/* 585 */     if (paramSunGraphics2D.antialiasHint != 2) {
/* 586 */       if (paramSunGraphics2D.paintState <= 1) {
/* 587 */         if (paramSunGraphics2D.compositeState <= 2) {
/* 588 */           localPixelToParallelogramConverter1 = d3dTxRenderPipe;
/* 589 */           localD3DRenderer = d3dRenderPipe;
/*     */         }
/* 591 */       } else if ((paramSunGraphics2D.compositeState <= 1) && 
/* 592 */         (D3DPaints.isValid(paramSunGraphics2D))) {
/* 593 */         localPixelToParallelogramConverter1 = d3dTxRenderPipe;
/* 594 */         localD3DRenderer = d3dRenderPipe;
/*     */       }
/*     */ 
/*     */     }
/* 599 */     else if (paramSunGraphics2D.paintState <= 1) {
/* 600 */       if ((this.graphicsDevice.isCapPresent(524288)) && ((paramSunGraphics2D.imageComp == CompositeType.SrcOverNoEa) || (paramSunGraphics2D.imageComp == CompositeType.SrcOver)))
/*     */       {
/* 604 */         if (i == 0) {
/* 605 */           super.validatePipe(paramSunGraphics2D);
/* 606 */           i = 1;
/*     */         }
/* 608 */         PixelToParallelogramConverter localPixelToParallelogramConverter2 = new PixelToParallelogramConverter(paramSunGraphics2D.shapepipe, d3dAAPgramPipe, 0.125D, 0.499D, false);
/*     */ 
/* 613 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter2;
/* 614 */         paramSunGraphics2D.fillpipe = localPixelToParallelogramConverter2;
/* 615 */         paramSunGraphics2D.shapepipe = localPixelToParallelogramConverter2;
/* 616 */       } else if (paramSunGraphics2D.compositeState == 2)
/*     */       {
/* 618 */         localPixelToParallelogramConverter1 = d3dTxRenderPipe;
/* 619 */         localD3DRenderer = d3dRenderPipe;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 625 */     if (localPixelToParallelogramConverter1 != null) {
/* 626 */       if (paramSunGraphics2D.transformState >= 3) {
/* 627 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter1;
/* 628 */         paramSunGraphics2D.fillpipe = localPixelToParallelogramConverter1;
/* 629 */       } else if (paramSunGraphics2D.strokeState != 0) {
/* 630 */         paramSunGraphics2D.drawpipe = localPixelToParallelogramConverter1;
/* 631 */         paramSunGraphics2D.fillpipe = localD3DRenderer;
/*     */       } else {
/* 633 */         paramSunGraphics2D.drawpipe = localD3DRenderer;
/* 634 */         paramSunGraphics2D.fillpipe = localD3DRenderer;
/*     */       }
/*     */ 
/* 640 */       paramSunGraphics2D.shapepipe = localPixelToParallelogramConverter1;
/*     */     }
/* 642 */     else if (i == 0) {
/* 643 */       super.validatePipe(paramSunGraphics2D);
/*     */     }
/*     */ 
/* 648 */     paramSunGraphics2D.textpipe = ((TextPipe)localObject);
/*     */ 
/* 651 */     paramSunGraphics2D.imagepipe = d3dImagePipe;
/*     */   }
/*     */ 
/*     */   protected MaskFill getMaskFill(SunGraphics2D paramSunGraphics2D)
/*     */   {
/* 656 */     if (paramSunGraphics2D.paintState > 1)
/*     */     {
/* 668 */       if ((!D3DPaints.isValid(paramSunGraphics2D)) || (!this.graphicsDevice.isCapPresent(16)))
/*     */       {
/* 671 */         return null;
/*     */       }
/*     */     }
/* 674 */     return super.getMaskFill(paramSunGraphics2D);
/*     */   }
/*     */ 
/*     */   public boolean copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 681 */     if ((paramSunGraphics2D.transformState < 3) && (paramSunGraphics2D.compositeState < 2))
/*     */     {
/* 684 */       paramInt1 += paramSunGraphics2D.transX;
/* 685 */       paramInt2 += paramSunGraphics2D.transY;
/*     */ 
/* 687 */       d3dRenderPipe.copyArea(paramSunGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 689 */       return true;
/*     */     }
/* 691 */     return false;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 696 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 697 */     localD3DRenderQueue.lock();
/*     */     try {
/* 699 */       RenderBuffer localRenderBuffer = localD3DRenderQueue.getBuffer();
/* 700 */       localD3DRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 701 */       localRenderBuffer.putInt(72);
/* 702 */       localRenderBuffer.putLong(getNativeOps());
/*     */ 
/* 705 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 707 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void dispose(long paramLong)
/*     */   {
/* 718 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 719 */     localD3DRenderQueue.lock();
/*     */     try {
/* 721 */       RenderBuffer localRenderBuffer = localD3DRenderQueue.getBuffer();
/* 722 */       localD3DRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 723 */       localRenderBuffer.putInt(73);
/* 724 */       localRenderBuffer.putLong(paramLong);
/*     */ 
/* 727 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 729 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void swapBuffers(D3DSurfaceData paramD3DSurfaceData, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
/*     */   {
/* 737 */     long l = paramD3DSurfaceData.getNativeOps();
/* 738 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/*     */     Object localObject1;
/* 741 */     if (D3DRenderQueue.isRenderQueueThread()) {
/* 742 */       if (!localD3DRenderQueue.tryLock())
/*     */       {
/* 745 */         localObject1 = (Component)paramD3DSurfaceData.getPeer().getTarget();
/* 746 */         SunToolkit.executeOnEventHandlerThread(localObject1, new Runnable() {
/*     */           public void run() {
/* 748 */             this.val$target.repaint(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     else
/* 754 */       localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 757 */       localObject1 = localD3DRenderQueue.getBuffer();
/* 758 */       localD3DRenderQueue.ensureCapacityAndAlignment(28, 4);
/* 759 */       ((RenderBuffer)localObject1).putInt(80);
/* 760 */       ((RenderBuffer)localObject1).putLong(l);
/* 761 */       ((RenderBuffer)localObject1).putInt(paramInt1);
/* 762 */       ((RenderBuffer)localObject1).putInt(paramInt2);
/* 763 */       ((RenderBuffer)localObject1).putInt(paramInt3);
/* 764 */       ((RenderBuffer)localObject1).putInt(paramInt4);
/* 765 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 767 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getDestination()
/*     */   {
/* 775 */     return this.offscreenImage;
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds() {
/* 779 */     if ((this.type == 4) || (this.type == 1)) {
/* 780 */       Rectangle localRectangle = this.peer.getBounds();
/* 781 */       localRectangle.x = (localRectangle.y = 0);
/* 782 */       return localRectangle;
/*     */     }
/* 784 */     return new Rectangle(this.width, this.height);
/*     */   }
/*     */ 
/*     */   public Rectangle getNativeBounds()
/*     */   {
/* 789 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/*     */ 
/* 793 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 796 */       return new Rectangle(this.nativeWidth, this.nativeHeight);
/*     */     } finally {
/* 798 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getDeviceConfiguration()
/*     */   {
/* 804 */     return this.graphicsDevice.getDefaultConfiguration();
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacement() {
/* 808 */     return restoreContents(this.offscreenImage);
/*     */   }
/*     */ 
/*     */   private static D3DGraphicsConfig getGC(WComponentPeer paramWComponentPeer)
/*     */   {
/*     */     GraphicsConfiguration localGraphicsConfiguration;
/* 813 */     if (paramWComponentPeer != null) {
/* 814 */       localGraphicsConfiguration = paramWComponentPeer.getGraphicsConfiguration();
/*     */     } else {
/* 816 */       GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/* 818 */       GraphicsDevice localGraphicsDevice = localGraphicsEnvironment.getDefaultScreenDevice();
/* 819 */       localGraphicsConfiguration = localGraphicsDevice.getDefaultConfiguration();
/*     */     }
/* 821 */     return (localGraphicsConfiguration instanceof D3DGraphicsConfig) ? (D3DGraphicsConfig)localGraphicsConfiguration : null;
/*     */   }
/*     */ 
/*     */   void restoreSurface()
/*     */   {
/* 828 */     initSurface();
/*     */   }
/*     */ 
/*     */   WComponentPeer getPeer() {
/* 832 */     return this.peer;
/*     */   }
/*     */ 
/*     */   public void setSurfaceLost(boolean paramBoolean)
/*     */   {
/* 844 */     super.setSurfaceLost(paramBoolean);
/* 845 */     if ((paramBoolean) && (this.offscreenImage != null)) {
/* 846 */       SurfaceManager localSurfaceManager = SurfaceManager.getManager(this.offscreenImage);
/* 847 */       localSurfaceManager.acceleratedSurfaceLost();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native long getNativeResourceNative(long paramLong, int paramInt);
/*     */ 
/*     */   public long getNativeResource(int paramInt)
/*     */   {
/* 877 */     return getNativeResourceNative(getNativeOps(), paramInt);
/*     */   }
/*     */ 
/*     */   public static native boolean updateWindowAccelImpl(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
/*     */ 
/*     */   static
/*     */   {
/* 193 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 194 */     d3dImagePipe = new D3DDrawImage();
/* 195 */     d3dTextPipe = new D3DTextRenderer(localD3DRenderQueue);
/* 196 */     d3dRenderPipe = new D3DRenderer(localD3DRenderQueue);
/* 197 */     if (GraphicsPrimitive.tracingEnabled()) {
/* 198 */       d3dTextPipe = d3dTextPipe.traceWrap();
/* 199 */       d3dRenderPipe = d3dRenderPipe.traceWrap();
/*     */     }
/*     */ 
/* 203 */     d3dAAPgramPipe = d3dRenderPipe.getAAParallelogramPipe();
/* 204 */     d3dTxRenderPipe = new PixelToParallelogramConverter(d3dRenderPipe, d3dRenderPipe, 1.0D, 0.25D, true);
/*     */ 
/* 208 */     D3DBlitLoops.register();
/* 209 */     D3DMaskFill.register();
/* 210 */     D3DMaskBlit.register();
/*     */   }
/*     */ 
/*     */   static class D3DDataBufferNative extends DataBufferNative
/*     */   {
/*     */     int pixel;
/*     */ 
/*     */     protected D3DDataBufferNative(SurfaceData paramSurfaceData, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 434 */       super(paramInt1, paramInt2, paramInt3);
/*     */     }
/*     */ 
/*     */     protected int getElem(final int paramInt1, final int paramInt2, final SurfaceData paramSurfaceData)
/*     */     {
/* 440 */       if (paramSurfaceData.isSurfaceLost()) {
/* 441 */         return 0;
/* 445 */       }
/*     */ D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 446 */       localD3DRenderQueue.lock();
/*     */       int i;
/*     */       try { localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 450 */             D3DSurfaceData.D3DDataBufferNative.this.pixel = D3DSurfaceData.dbGetPixelNative(paramSurfaceData.getNativeOps(), paramInt1, paramInt2);
/*     */           }
/*     */         });
/*     */       } finally {
/* 454 */         i = this.pixel;
/* 455 */         localD3DRenderQueue.unlock();
/*     */       }
/* 457 */       return i;
/*     */     }
/*     */ 
/*     */     protected void setElem(final int paramInt1, final int paramInt2, final int paramInt3, final SurfaceData paramSurfaceData)
/*     */     {
/* 463 */       if (paramSurfaceData.isSurfaceLost()) {
/* 464 */         return;
/*     */       }
/*     */ 
/* 467 */       D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 468 */       localD3DRenderQueue.lock();
/*     */       try {
/* 470 */         localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 472 */             D3DSurfaceData.dbSetPixelNative(paramSurfaceData.getNativeOps(), paramInt1, paramInt2, paramInt3);
/*     */           }
/*     */         });
/* 475 */         paramSurfaceData.markDirty();
/*     */       } finally {
/* 477 */         localD3DRenderQueue.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class D3DWindowSurfaceData extends D3DSurfaceData
/*     */   {
/*     */     StateTracker dirtyTracker;
/*     */ 
/*     */     public D3DWindowSurfaceData(WComponentPeer paramWComponentPeer, D3DGraphicsConfig paramD3DGraphicsConfig)
/*     */     {
/* 893 */       super(paramD3DGraphicsConfig, paramWComponentPeer.getBounds().width, paramWComponentPeer.getBounds().height, null, paramWComponentPeer.getColorModel(), 1, 3, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, 1);
/*     */ 
/* 897 */       this.dirtyTracker = getStateTracker();
/*     */     }
/*     */ 
/*     */     public SurfaceData getReplacement()
/*     */     {
/* 910 */       ScreenUpdateManager localScreenUpdateManager = ScreenUpdateManager.getInstance();
/* 911 */       return localScreenUpdateManager.getReplacementScreenSurface(this.peer, this);
/*     */     }
/*     */ 
/*     */     public Object getDestination()
/*     */     {
/* 919 */       return this.peer.getTarget();
/*     */     }
/*     */ 
/*     */     void disableAccelerationForSurface()
/*     */     {
/* 929 */       setSurfaceLost(true);
/* 930 */       invalidate();
/* 931 */       flush();
/* 932 */       this.peer.disableAcceleration();
/* 933 */       ScreenUpdateManager.getInstance().dropScreenSurface(this);
/*     */     }
/*     */ 
/*     */     void restoreSurface()
/*     */     {
/* 938 */       if (!this.peer.isAccelCapable()) {
/* 939 */         throw new InvalidPipeException("Onscreen acceleration disabled for this surface");
/*     */       }
/*     */ 
/* 942 */       Window localWindow = this.graphicsDevice.getFullScreenWindow();
/* 943 */       if ((localWindow != null) && (localWindow != this.peer.getTarget())) {
/* 944 */         throw new InvalidPipeException("Can't restore onscreen surface when in full-screen mode");
/*     */       }
/*     */ 
/* 947 */       super.restoreSurface();
/*     */ 
/* 950 */       setSurfaceLost(false);
/*     */ 
/* 961 */       D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 962 */       localD3DRenderQueue.lock();
/*     */       try {
/* 964 */         getContext().invalidateContext();
/*     */       } finally {
/* 966 */         localD3DRenderQueue.unlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isDirty() {
/* 971 */       return !this.dirtyTracker.isCurrent();
/*     */     }
/*     */ 
/*     */     public void markClean() {
/* 975 */       this.dirtyTracker = getStateTracker();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DSurfaceData
 * JD-Core Version:    0.6.2
 */