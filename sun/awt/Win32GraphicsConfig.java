/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.BufferCapabilities;
/*     */ import java.awt.BufferCapabilities.FlipContents;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.awt.image.OffScreenImage;
/*     */ import sun.awt.image.SunVolatileImage;
/*     */ import sun.awt.image.SurfaceManager.ProxiedGraphicsConfig;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.loops.CompositeType;
/*     */ import sun.java2d.loops.RenderLoops;
/*     */ import sun.java2d.loops.SurfaceType;
/*     */ import sun.java2d.windows.GDIWindowSurfaceData;
/*     */ 
/*     */ public class Win32GraphicsConfig extends GraphicsConfiguration
/*     */   implements DisplayChangedListener, SurfaceManager.ProxiedGraphicsConfig
/*     */ {
/*     */   protected Win32GraphicsDevice screen;
/*     */   protected int visual;
/*     */   protected RenderLoops solidloops;
/* 128 */   private SurfaceType sTypeOrig = null;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public static Win32GraphicsConfig getConfig(Win32GraphicsDevice paramWin32GraphicsDevice, int paramInt)
/*     */   {
/*  92 */     return new Win32GraphicsConfig(paramWin32GraphicsDevice, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Win32GraphicsConfig(GraphicsDevice paramGraphicsDevice, int paramInt)
/*     */   {
/* 101 */     this.screen = ((Win32GraphicsDevice)paramGraphicsDevice);
/* 102 */     this.visual = paramInt;
/* 103 */     ((Win32GraphicsDevice)paramGraphicsDevice).addDisplayChangedListener(this);
/*     */   }
/*     */ 
/*     */   public GraphicsDevice getDevice()
/*     */   {
/* 110 */     return this.screen;
/*     */   }
/*     */ 
/*     */   public int getVisual()
/*     */   {
/* 117 */     return this.visual;
/*     */   }
/*     */ 
/*     */   public Object getProxyKey() {
/* 121 */     return this.screen;
/*     */   }
/*     */ 
/*     */   public synchronized RenderLoops getSolidLoops(SurfaceType paramSurfaceType)
/*     */   {
/* 130 */     if ((this.solidloops == null) || (this.sTypeOrig != paramSurfaceType)) {
/* 131 */       this.solidloops = SurfaceData.makeRenderLoops(SurfaceType.OpaqueColor, CompositeType.SrcNoEa, paramSurfaceType);
/*     */ 
/* 134 */       this.sTypeOrig = paramSurfaceType;
/*     */     }
/* 136 */     return this.solidloops;
/*     */   }
/*     */ 
/*     */   public synchronized ColorModel getColorModel()
/*     */   {
/* 143 */     return this.screen.getColorModel();
/*     */   }
/*     */ 
/*     */   public ColorModel getDeviceColorModel()
/*     */   {
/* 155 */     return this.screen.getDynamicColorModel();
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel(int paramInt)
/*     */   {
/* 163 */     switch (paramInt) {
/*     */     case 1:
/* 165 */       return getColorModel();
/*     */     case 2:
/* 167 */       return new DirectColorModel(25, 16711680, 65280, 255, 16777216);
/*     */     case 3:
/* 169 */       return ColorModel.getRGBdefault();
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public AffineTransform getDefaultTransform()
/*     */   {
/* 185 */     return new AffineTransform();
/*     */   }
/*     */ 
/*     */   public AffineTransform getNormalizingTransform()
/*     */   {
/* 208 */     Win32GraphicsEnvironment localWin32GraphicsEnvironment = (Win32GraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/* 210 */     double d1 = localWin32GraphicsEnvironment.getXResolution() / 72.0D;
/* 211 */     double d2 = localWin32GraphicsEnvironment.getYResolution() / 72.0D;
/* 212 */     return new AffineTransform(d1, 0.0D, 0.0D, d2, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 216 */     return super.toString() + "[dev=" + this.screen + ",pixfmt=" + this.visual + "]";
/*     */   }
/*     */ 
/*     */   private native Rectangle getBounds(int paramInt);
/*     */ 
/*     */   public Rectangle getBounds() {
/* 222 */     return getBounds(this.screen.getScreen());
/*     */   }
/*     */ 
/*     */   public synchronized void displayChanged() {
/* 226 */     this.solidloops = null;
/*     */   }
/*     */ 
/*     */   public void paletteChanged()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SurfaceData createSurfaceData(WComponentPeer paramWComponentPeer, int paramInt)
/*     */   {
/* 246 */     return GDIWindowSurfaceData.createData(paramWComponentPeer);
/*     */   }
/*     */ 
/*     */   public Image createAcceleratedImage(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 256 */     ColorModel localColorModel = getColorModel(1);
/* 257 */     WritableRaster localWritableRaster = localColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
/*     */ 
/* 259 */     return new OffScreenImage(paramComponent, localColorModel, localWritableRaster, localColorModel.isAlphaPremultiplied());
/*     */   }
/*     */ 
/*     */   public void assertOperationSupported(Component paramComponent, int paramInt, BufferCapabilities paramBufferCapabilities)
/*     */     throws AWTException
/*     */   {
/* 278 */     throw new AWTException("The operation requested is not supported");
/*     */   }
/*     */ 
/*     */   public VolatileImage createBackBuffer(WComponentPeer paramWComponentPeer)
/*     */   {
/* 290 */     Component localComponent = (Component)paramWComponentPeer.getTarget();
/* 291 */     return new SunVolatileImage(localComponent, localComponent.getWidth(), localComponent.getHeight(), Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   public void flip(WComponentPeer paramWComponentPeer, Component paramComponent, VolatileImage paramVolatileImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents)
/*     */   {
/*     */     Graphics localGraphics;
/* 310 */     if ((paramFlipContents == BufferCapabilities.FlipContents.COPIED) || (paramFlipContents == BufferCapabilities.FlipContents.UNDEFINED))
/*     */     {
/* 312 */       localGraphics = paramWComponentPeer.getGraphics();
/*     */       try {
/* 314 */         localGraphics.drawImage(paramVolatileImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*     */       }
/*     */       finally
/*     */       {
/* 319 */         localGraphics.dispose();
/*     */       }
/* 321 */     } else if (paramFlipContents == BufferCapabilities.FlipContents.BACKGROUND) {
/* 322 */       localGraphics = paramVolatileImage.getGraphics();
/*     */       try {
/* 324 */         localGraphics.setColor(paramComponent.getBackground());
/* 325 */         localGraphics.fillRect(0, 0, paramVolatileImage.getWidth(), paramVolatileImage.getHeight());
/*     */       }
/*     */       finally
/*     */       {
/* 329 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isTranslucencyCapable()
/*     */   {
/* 338 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  77 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.Win32GraphicsConfig
 * JD-Core Version:    0.6.2
 */