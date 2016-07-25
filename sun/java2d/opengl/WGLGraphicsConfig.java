/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.BufferCapabilities;
/*     */ import java.awt.BufferCapabilities.FlipContents;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.VolatileImage;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.Win32GraphicsDevice;
/*     */ import sun.awt.image.SunVolatileImage;
/*     */ import sun.awt.image.SurfaceManager;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.Surface;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.hw.AccelDeviceEventListener;
/*     */ import sun.java2d.pipe.hw.AccelDeviceEventNotifier;
/*     */ import sun.java2d.pipe.hw.AccelSurface;
/*     */ import sun.java2d.pipe.hw.AccelTypedVolatileImage;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ import sun.java2d.windows.GDIWindowSurfaceData;
/*     */ 
/*     */ public class WGLGraphicsConfig extends Win32GraphicsConfig
/*     */   implements OGLGraphicsConfig
/*     */ {
/*  81 */   protected static boolean wglAvailable = initWGL();
/*     */ 
/*  67 */   private static ImageCapabilities imageCaps = new WGLImageCaps(null);
/*     */   private BufferCapabilities bufferCaps;
/*     */   private long pConfigInfo;
/*     */   private ContextCapabilities oglCaps;
/*     */   private OGLContext context;
/*  73 */   private Object disposerReferent = new Object();
/*     */ 
/*     */   public static native int getDefaultPixFmt(int paramInt);
/*     */ 
/*     */   private static native boolean initWGL();
/*     */ 
/*     */   private static native long getWGLConfigInfo(int paramInt1, int paramInt2);
/*     */ 
/*     */   private static native int getOGLCapabilities(long paramLong);
/*     */ 
/*     */   protected WGLGraphicsConfig(Win32GraphicsDevice paramWin32GraphicsDevice, int paramInt, long paramLong, ContextCapabilities paramContextCapabilities)
/*     */   {
/*  87 */     super(paramWin32GraphicsDevice, paramInt);
/*  88 */     this.pConfigInfo = paramLong;
/*  89 */     this.oglCaps = paramContextCapabilities;
/*  90 */     this.context = new OGLContext(OGLRenderQueue.getInstance(), this);
/*     */ 
/*  94 */     Disposer.addRecord(this.disposerReferent, new WGLGCDisposerRecord(this.pConfigInfo, paramWin32GraphicsDevice.getScreen()));
/*     */   }
/*     */ 
/*     */   public Object getProxyKey()
/*     */   {
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   public SurfaceData createManagedSurface(int paramInt1, int paramInt2, int paramInt3) {
/* 104 */     return WGLSurfaceData.createData(this, paramInt1, paramInt2, getColorModel(paramInt3), null, 3);
/*     */   }
/*     */ 
/*     */   public static WGLGraphicsConfig getConfig(Win32GraphicsDevice paramWin32GraphicsDevice, int paramInt)
/*     */   {
/* 113 */     if (!wglAvailable) {
/* 114 */       return null;
/*     */     }
/*     */ 
/* 117 */     long l = 0L;
/* 118 */     String[] arrayOfString = new String[1];
/* 119 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 120 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 125 */       OGLContext.invalidateCurrentContext();
/* 126 */       WGLGetConfigInfo localWGLGetConfigInfo = new WGLGetConfigInfo(paramWin32GraphicsDevice.getScreen(), paramInt, null);
/*     */ 
/* 128 */       localOGLRenderQueue.flushAndInvokeNow(localWGLGetConfigInfo);
/* 129 */       l = localWGLGetConfigInfo.getConfigInfo();
/* 130 */       if (l != 0L) {
/* 131 */         OGLContext.setScratchSurface(l);
/* 132 */         localOGLRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 134 */             this.val$ids[0] = OGLContext.getOGLIdString();
/*     */           } } );
/*     */       }
/*     */     }
/*     */     finally {
/* 139 */       localOGLRenderQueue.unlock();
/*     */     }
/* 141 */     if (l == 0L) {
/* 142 */       return null;
/*     */     }
/*     */ 
/* 145 */     int i = getOGLCapabilities(l);
/* 146 */     OGLContext.OGLContextCaps localOGLContextCaps = new OGLContext.OGLContextCaps(i, arrayOfString[0]);
/*     */ 
/* 148 */     return new WGLGraphicsConfig(paramWin32GraphicsDevice, paramInt, l, localOGLContextCaps);
/*     */   }
/*     */ 
/*     */   public static boolean isWGLAvailable()
/*     */   {
/* 172 */     return wglAvailable;
/*     */   }
/*     */ 
/*     */   public final boolean isCapPresent(int paramInt)
/*     */   {
/* 181 */     return (this.oglCaps.getCaps() & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   public final long getNativeConfigInfo()
/*     */   {
/* 186 */     return this.pConfigInfo;
/*     */   }
/*     */ 
/*     */   public final OGLContext getContext()
/*     */   {
/* 196 */     return this.context;
/*     */   }
/*     */ 
/*     */   public synchronized void displayChanged()
/*     */   {
/* 231 */     super.displayChanged();
/*     */ 
/* 235 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 236 */     localOGLRenderQueue.lock();
/*     */     try {
/* 238 */       OGLContext.invalidateCurrentContext();
/*     */     } finally {
/* 240 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel(int paramInt)
/*     */   {
/* 246 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/* 250 */       return new DirectColorModel(24, 16711680, 65280, 255);
/*     */     case 2:
/* 252 */       return new DirectColorModel(25, 16711680, 65280, 255, 16777216);
/*     */     case 3:
/* 254 */       ColorSpace localColorSpace = ColorSpace.getInstance(1000);
/* 255 */       return new DirectColorModel(localColorSpace, 32, 16711680, 65280, 255, -16777216, true, 3);
/*     */     }
/*     */ 
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 265 */     return "WGLGraphicsConfig[dev=" + this.screen + ",pixfmt=" + this.visual + "]";
/*     */   }
/*     */ 
/*     */   public SurfaceData createSurfaceData(WComponentPeer paramWComponentPeer, int paramInt)
/*     */   {
/* 284 */     Object localObject = WGLSurfaceData.createData(paramWComponentPeer);
/* 285 */     if (localObject == null) {
/* 286 */       localObject = GDIWindowSurfaceData.createData(paramWComponentPeer);
/*     */     }
/* 288 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void assertOperationSupported(Component paramComponent, int paramInt, BufferCapabilities paramBufferCapabilities)
/*     */     throws AWTException
/*     */   {
/* 306 */     if (paramInt > 2) {
/* 307 */       throw new AWTException("Only double or single buffering is supported");
/*     */     }
/*     */ 
/* 310 */     BufferCapabilities localBufferCapabilities = getBufferCapabilities();
/* 311 */     if (!localBufferCapabilities.isPageFlipping()) {
/* 312 */       throw new AWTException("Page flipping is not supported");
/*     */     }
/* 314 */     if (paramBufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.PRIOR)
/* 315 */       throw new AWTException("FlipContents.PRIOR is not supported");
/*     */   }
/*     */ 
/*     */   public VolatileImage createBackBuffer(WComponentPeer paramWComponentPeer)
/*     */   {
/* 325 */     Component localComponent = (Component)paramWComponentPeer.getTarget();
/* 326 */     return new SunVolatileImage(localComponent, localComponent.getWidth(), localComponent.getHeight(), Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   public void flip(WComponentPeer paramWComponentPeer, Component paramComponent, VolatileImage paramVolatileImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents)
/*     */   {
/*     */     Object localObject1;
/* 340 */     if (paramFlipContents == BufferCapabilities.FlipContents.COPIED) {
/* 341 */       localObject1 = SurfaceManager.getManager(paramVolatileImage);
/* 342 */       SurfaceData localSurfaceData1 = ((SurfaceManager)localObject1).getPrimarySurfaceData();
/*     */       Object localObject2;
/* 344 */       if ((localSurfaceData1 instanceof WGLSurfaceData.WGLVSyncOffScreenSurfaceData)) {
/* 345 */         localObject2 = (WGLSurfaceData.WGLVSyncOffScreenSurfaceData)localSurfaceData1;
/*     */ 
/* 347 */         SurfaceData localSurfaceData2 = ((WGLSurfaceData.WGLVSyncOffScreenSurfaceData)localObject2).getFlipSurface();
/* 348 */         SunGraphics2D localSunGraphics2D = new SunGraphics2D(localSurfaceData2, Color.black, Color.white, null);
/*     */         try
/*     */         {
/* 351 */           localSunGraphics2D.drawImage(paramVolatileImage, 0, 0, null);
/*     */         } finally {
/* 353 */           localSunGraphics2D.dispose();
/*     */         }
/*     */       } else {
/* 356 */         localObject2 = paramWComponentPeer.getGraphics();
/*     */         try {
/* 358 */           ((Graphics)localObject2).drawImage(paramVolatileImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*     */         }
/*     */         finally
/*     */         {
/* 363 */           ((Graphics)localObject2).dispose();
/*     */         }
/* 365 */         return;
/*     */       }
/* 367 */     } else if (paramFlipContents == BufferCapabilities.FlipContents.PRIOR)
/*     */     {
/* 369 */       return;
/*     */     }
/*     */ 
/* 372 */     OGLSurfaceData.swapBuffers(paramWComponentPeer.getData());
/*     */ 
/* 374 */     if (paramFlipContents == BufferCapabilities.FlipContents.BACKGROUND) {
/* 375 */       localObject1 = paramVolatileImage.getGraphics();
/*     */       try {
/* 377 */         ((Graphics)localObject1).setColor(paramComponent.getBackground());
/* 378 */         ((Graphics)localObject1).fillRect(0, 0, paramVolatileImage.getWidth(), paramVolatileImage.getHeight());
/*     */       }
/*     */       finally
/*     */       {
/* 382 */         ((Graphics)localObject1).dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public BufferCapabilities getBufferCapabilities()
/*     */   {
/* 396 */     if (this.bufferCaps == null) {
/* 397 */       boolean bool = isCapPresent(65536);
/* 398 */       this.bufferCaps = new WGLBufferCaps(bool);
/*     */     }
/* 400 */     return this.bufferCaps;
/*     */   }
/*     */ 
/*     */   public ImageCapabilities getImageCapabilities()
/*     */   {
/* 414 */     return imageCaps;
/*     */   }
/*     */ 
/*     */   public VolatileImage createCompatibleVolatileImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 427 */     if ((paramInt4 == 4) || (paramInt4 == 1) || (paramInt4 == 0) || (paramInt3 == 2))
/*     */     {
/* 430 */       return null;
/*     */     }
/*     */ 
/* 433 */     if (paramInt4 == 5) {
/* 434 */       if (!isCapPresent(12))
/* 435 */         return null;
/*     */     }
/* 437 */     else if (paramInt4 == 2) {
/* 438 */       int i = paramInt3 == 1 ? 1 : 0;
/* 439 */       if ((i == 0) && (!isCapPresent(2))) {
/* 440 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 444 */     AccelTypedVolatileImage localAccelTypedVolatileImage = new AccelTypedVolatileImage(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 446 */     Surface localSurface = localAccelTypedVolatileImage.getDestSurface();
/* 447 */     if ((!(localSurface instanceof AccelSurface)) || (((AccelSurface)localSurface).getType() != paramInt4))
/*     */     {
/* 450 */       localAccelTypedVolatileImage.flush();
/* 451 */       localAccelTypedVolatileImage = null;
/*     */     }
/*     */ 
/* 454 */     return localAccelTypedVolatileImage;
/*     */   }
/*     */ 
/*     */   public ContextCapabilities getContextCapabilities()
/*     */   {
/* 464 */     return this.oglCaps;
/*     */   }
/*     */ 
/*     */   public void addDeviceEventListener(AccelDeviceEventListener paramAccelDeviceEventListener)
/*     */   {
/* 469 */     AccelDeviceEventNotifier.addListener(paramAccelDeviceEventListener, this.screen.getScreen());
/*     */   }
/*     */ 
/*     */   public void removeDeviceEventListener(AccelDeviceEventListener paramAccelDeviceEventListener)
/*     */   {
/* 474 */     AccelDeviceEventNotifier.removeListener(paramAccelDeviceEventListener);
/*     */   }
/*     */ 
/*     */   private static class WGLBufferCaps extends BufferCapabilities
/*     */   {
/*     */     public WGLBufferCaps(boolean paramBoolean)
/*     */     {
/* 389 */       super(WGLGraphicsConfig.imageCaps, paramBoolean ? BufferCapabilities.FlipContents.UNDEFINED : null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WGLGCDisposerRecord
/*     */     implements DisposerRecord
/*     */   {
/*     */     private long pCfgInfo;
/*     */     private int screen;
/*     */ 
/*     */     public WGLGCDisposerRecord(long paramLong, int paramInt)
/*     */     {
/* 203 */       this.pCfgInfo = paramLong;
/*     */     }
/*     */     public void dispose() {
/* 206 */       OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 207 */       localOGLRenderQueue.lock();
/*     */       try {
/* 209 */         localOGLRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 211 */             AccelDeviceEventNotifier.eventOccured(WGLGraphicsConfig.WGLGCDisposerRecord.this.screen, 0);
/*     */ 
/* 214 */             AccelDeviceEventNotifier.eventOccured(WGLGraphicsConfig.WGLGCDisposerRecord.this.screen, 1);
/*     */           }
/*     */         });
/*     */       }
/*     */       finally
/*     */       {
/* 220 */         localOGLRenderQueue.unlock();
/*     */       }
/* 222 */       if (this.pCfgInfo != 0L) {
/* 223 */         OGLRenderQueue.disposeGraphicsConfig(this.pCfgInfo);
/* 224 */         this.pCfgInfo = 0L;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WGLGetConfigInfo
/*     */     implements Runnable
/*     */   {
/*     */     private int screen;
/*     */     private int pixfmt;
/*     */     private long cfginfo;
/*     */ 
/*     */     private WGLGetConfigInfo(int paramInt1, int paramInt2)
/*     */     {
/* 160 */       this.screen = paramInt1;
/* 161 */       this.pixfmt = paramInt2;
/*     */     }
/*     */     public void run() {
/* 164 */       this.cfginfo = WGLGraphicsConfig.getWGLConfigInfo(this.screen, this.pixfmt);
/*     */     }
/*     */     public long getConfigInfo() {
/* 167 */       return this.cfginfo;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WGLImageCaps extends ImageCapabilities
/*     */   {
/*     */     private WGLImageCaps()
/*     */     {
/* 405 */       super();
/*     */     }
/*     */     public boolean isTrueVolatile() {
/* 408 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.WGLGraphicsConfig
 * JD-Core Version:    0.6.2
 */