/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.BufferCapabilities;
/*     */ import java.awt.BufferCapabilities.FlipContents;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.VolatileImage;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.image.SunVolatileImage;
/*     */ import sun.awt.image.SurfaceManager;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.Surface;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.hw.AccelDeviceEventListener;
/*     */ import sun.java2d.pipe.hw.AccelDeviceEventNotifier;
/*     */ import sun.java2d.pipe.hw.AccelGraphicsConfig;
/*     */ import sun.java2d.pipe.hw.AccelSurface;
/*     */ import sun.java2d.pipe.hw.AccelTypedVolatileImage;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ 
/*     */ public class D3DGraphicsConfig extends Win32GraphicsConfig
/*     */   implements AccelGraphicsConfig
/*     */ {
/*  59 */   private static ImageCapabilities imageCaps = new D3DImageCaps(null);
/*     */   private BufferCapabilities bufferCaps;
/*     */   private D3DGraphicsDevice device;
/*     */ 
/*     */   protected D3DGraphicsConfig(D3DGraphicsDevice paramD3DGraphicsDevice)
/*     */   {
/*  65 */     super(paramD3DGraphicsDevice, 0);
/*  66 */     this.device = paramD3DGraphicsDevice;
/*     */   }
/*     */ 
/*     */   public SurfaceData createManagedSurface(int paramInt1, int paramInt2, int paramInt3) {
/*  70 */     return D3DSurfaceData.createData(this, paramInt1, paramInt2, getColorModel(paramInt3), null, 3);
/*     */   }
/*     */ 
/*     */   public synchronized void displayChanged()
/*     */   {
/*  78 */     super.displayChanged();
/*     */ 
/*  82 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/*  83 */     localD3DRenderQueue.lock();
/*     */     try {
/*  85 */       D3DContext.invalidateCurrentContext();
/*     */     } finally {
/*  87 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel(int paramInt)
/*     */   {
/*  93 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/*  97 */       return new DirectColorModel(24, 16711680, 65280, 255);
/*     */     case 2:
/*  99 */       return new DirectColorModel(25, 16711680, 65280, 255, 16777216);
/*     */     case 3:
/* 101 */       ColorSpace localColorSpace = ColorSpace.getInstance(1000);
/* 102 */       return new DirectColorModel(localColorSpace, 32, 16711680, 65280, 255, -16777216, true, 3);
/*     */     }
/*     */ 
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return "D3DGraphicsConfig[dev=" + this.screen + ",pixfmt=" + this.visual + "]";
/*     */   }
/*     */ 
/*     */   public SurfaceData createSurfaceData(WComponentPeer paramWComponentPeer, int paramInt)
/*     */   {
/* 132 */     return super.createSurfaceData(paramWComponentPeer, paramInt);
/*     */   }
/*     */ 
/*     */   public void assertOperationSupported(Component paramComponent, int paramInt, BufferCapabilities paramBufferCapabilities)
/*     */     throws AWTException
/*     */   {
/* 150 */     if ((paramInt < 2) || (paramInt > 4)) {
/* 151 */       throw new AWTException("Only 2-4 buffers supported");
/*     */     }
/* 153 */     if ((paramBufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.COPIED) && (paramInt != 2))
/*     */     {
/* 156 */       throw new AWTException("FlipContents.COPIED is onlysupported for 2 buffers");
/*     */     }
/*     */   }
/*     */ 
/*     */   public VolatileImage createBackBuffer(WComponentPeer paramWComponentPeer)
/*     */   {
/* 167 */     Component localComponent = (Component)paramWComponentPeer.getTarget();
/*     */ 
/* 170 */     int i = Math.max(1, localComponent.getWidth());
/* 171 */     int j = Math.max(1, localComponent.getHeight());
/* 172 */     return new SunVolatileImage(localComponent, i, j, Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   public void flip(WComponentPeer paramWComponentPeer, Component paramComponent, VolatileImage paramVolatileImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents)
/*     */   {
/* 186 */     SurfaceManager localSurfaceManager = SurfaceManager.getManager(paramVolatileImage);
/*     */ 
/* 188 */     SurfaceData localSurfaceData = localSurfaceManager.getPrimarySurfaceData();
/*     */     Object localObject1;
/* 189 */     if ((localSurfaceData instanceof D3DSurfaceData)) {
/* 190 */       localObject1 = (D3DSurfaceData)localSurfaceData;
/* 191 */       D3DSurfaceData.swapBuffers((D3DSurfaceData)localObject1, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     else {
/* 194 */       localObject1 = paramWComponentPeer.getGraphics();
/*     */       try {
/* 196 */         ((Graphics)localObject1).drawImage(paramVolatileImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*     */       }
/*     */       finally
/*     */       {
/* 201 */         ((Graphics)localObject1).dispose();
/*     */       }
/*     */     }
/*     */ 
/* 205 */     if (paramFlipContents == BufferCapabilities.FlipContents.BACKGROUND) {
/* 206 */       localObject1 = paramVolatileImage.getGraphics();
/*     */       try {
/* 208 */         ((Graphics)localObject1).setColor(paramComponent.getBackground());
/* 209 */         ((Graphics)localObject1).fillRect(0, 0, paramVolatileImage.getWidth(), paramVolatileImage.getHeight());
/*     */       }
/*     */       finally
/*     */       {
/* 213 */         ((Graphics)localObject1).dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public BufferCapabilities getBufferCapabilities()
/*     */   {
/* 233 */     if (this.bufferCaps == null) {
/* 234 */       this.bufferCaps = new D3DBufferCaps();
/*     */     }
/* 236 */     return this.bufferCaps;
/*     */   }
/*     */ 
/*     */   public ImageCapabilities getImageCapabilities()
/*     */   {
/* 251 */     return imageCaps;
/*     */   }
/*     */ 
/*     */   D3DGraphicsDevice getD3DDevice() {
/* 255 */     return this.device;
/*     */   }
/*     */ 
/*     */   public D3DContext getContext()
/*     */   {
/* 265 */     return this.device.getContext();
/*     */   }
/*     */ 
/*     */   public VolatileImage createCompatibleVolatileImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 278 */     if ((paramInt4 == 4) || (paramInt4 == 1) || (paramInt4 == 0) || (paramInt3 == 2))
/*     */     {
/* 281 */       return null;
/*     */     }
/* 283 */     int i = paramInt3 == 1 ? 1 : 0;
/* 284 */     if (paramInt4 == 5) {
/* 285 */       int j = i != 0 ? 8 : 4;
/* 286 */       if (!this.device.isCapPresent(j))
/* 287 */         return null;
/*     */     }
/* 289 */     else if ((paramInt4 == 2) && 
/* 290 */       (i == 0) && (!this.device.isCapPresent(2))) {
/* 291 */       return null;
/*     */     }
/*     */ 
/* 295 */     AccelTypedVolatileImage localAccelTypedVolatileImage = new AccelTypedVolatileImage(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 297 */     Surface localSurface = localAccelTypedVolatileImage.getDestSurface();
/* 298 */     if ((!(localSurface instanceof AccelSurface)) || (((AccelSurface)localSurface).getType() != paramInt4))
/*     */     {
/* 301 */       localAccelTypedVolatileImage.flush();
/* 302 */       localAccelTypedVolatileImage = null;
/*     */     }
/*     */ 
/* 305 */     return localAccelTypedVolatileImage;
/*     */   }
/*     */ 
/*     */   public ContextCapabilities getContextCapabilities()
/*     */   {
/* 315 */     return this.device.getContextCapabilities();
/*     */   }
/*     */ 
/*     */   public void addDeviceEventListener(AccelDeviceEventListener paramAccelDeviceEventListener)
/*     */   {
/* 320 */     AccelDeviceEventNotifier.addListener(paramAccelDeviceEventListener, this.device.getScreen());
/*     */   }
/*     */ 
/*     */   public void removeDeviceEventListener(AccelDeviceEventListener paramAccelDeviceEventListener)
/*     */   {
/* 325 */     AccelDeviceEventNotifier.removeListener(paramAccelDeviceEventListener);
/*     */   }
/*     */ 
/*     */   private static class D3DBufferCaps extends BufferCapabilities
/*     */   {
/*     */     public D3DBufferCaps()
/*     */     {
/* 222 */       super(D3DGraphicsConfig.imageCaps, BufferCapabilities.FlipContents.UNDEFINED);
/*     */     }
/*     */ 
/*     */     public boolean isMultiBufferAvailable() {
/* 226 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class D3DImageCaps extends ImageCapabilities
/*     */   {
/*     */     private D3DImageCaps()
/*     */     {
/* 241 */       super();
/*     */     }
/*     */ 
/*     */     public boolean isTrueVolatile() {
/* 245 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DGraphicsConfig
 * JD-Core Version:    0.6.2
 */