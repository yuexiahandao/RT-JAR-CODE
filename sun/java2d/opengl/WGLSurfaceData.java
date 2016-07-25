/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.ColorModel;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public abstract class WGLSurfaceData extends OGLSurfaceData
/*     */ {
/*     */   protected WComponentPeer peer;
/*     */   private WGLGraphicsConfig graphicsConfig;
/*     */ 
/*     */   private native void initOps(long paramLong1, WComponentPeer paramWComponentPeer, long paramLong2);
/*     */ 
/*     */   protected native boolean initPbuffer(long paramLong1, long paramLong2, boolean paramBoolean, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected WGLSurfaceData(WComponentPeer paramWComponentPeer, WGLGraphicsConfig paramWGLGraphicsConfig, ColorModel paramColorModel, int paramInt)
/*     */   {
/*  53 */     super(paramWGLGraphicsConfig, paramColorModel, paramInt);
/*  54 */     this.peer = paramWComponentPeer;
/*  55 */     this.graphicsConfig = paramWGLGraphicsConfig;
/*     */ 
/*  57 */     long l1 = paramWGLGraphicsConfig.getNativeConfigInfo();
/*  58 */     long l2 = paramWComponentPeer != null ? paramWComponentPeer.getHWnd() : 0L;
/*     */ 
/*  60 */     initOps(l1, paramWComponentPeer, l2);
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getDeviceConfiguration() {
/*  64 */     return this.graphicsConfig;
/*     */   }
/*     */ 
/*     */   public static WGLWindowSurfaceData createData(WComponentPeer paramWComponentPeer)
/*     */   {
/*  75 */     if ((!paramWComponentPeer.isAccelCapable()) || (!SunToolkit.isContainingTopLevelOpaque((Component)paramWComponentPeer.getTarget())))
/*     */     {
/*  78 */       return null;
/*     */     }
/*  80 */     WGLGraphicsConfig localWGLGraphicsConfig = getGC(paramWComponentPeer);
/*  81 */     return new WGLWindowSurfaceData(paramWComponentPeer, localWGLGraphicsConfig);
/*     */   }
/*     */ 
/*     */   public static WGLOffScreenSurfaceData createData(WComponentPeer paramWComponentPeer, Image paramImage, int paramInt)
/*     */   {
/*  95 */     if ((!paramWComponentPeer.isAccelCapable()) || (!SunToolkit.isContainingTopLevelOpaque((Component)paramWComponentPeer.getTarget())))
/*     */     {
/*  98 */       return null;
/*     */     }
/* 100 */     WGLGraphicsConfig localWGLGraphicsConfig = getGC(paramWComponentPeer);
/* 101 */     Rectangle localRectangle = paramWComponentPeer.getBounds();
/* 102 */     if (paramInt == 4) {
/* 103 */       return new WGLOffScreenSurfaceData(paramWComponentPeer, localWGLGraphicsConfig, localRectangle.width, localRectangle.height, paramImage, paramWComponentPeer.getColorModel(), paramInt);
/*     */     }
/*     */ 
/* 107 */     return new WGLVSyncOffScreenSurfaceData(paramWComponentPeer, localWGLGraphicsConfig, localRectangle.width, localRectangle.height, paramImage, paramWComponentPeer.getColorModel(), paramInt);
/*     */   }
/*     */ 
/*     */   public static WGLOffScreenSurfaceData createData(WGLGraphicsConfig paramWGLGraphicsConfig, int paramInt1, int paramInt2, ColorModel paramColorModel, Image paramImage, int paramInt3)
/*     */   {
/* 122 */     return new WGLOffScreenSurfaceData(null, paramWGLGraphicsConfig, paramInt1, paramInt2, paramImage, paramColorModel, paramInt3);
/*     */   }
/*     */ 
/*     */   public static WGLGraphicsConfig getGC(WComponentPeer paramWComponentPeer)
/*     */   {
/* 127 */     if (paramWComponentPeer != null) {
/* 128 */       return (WGLGraphicsConfig)paramWComponentPeer.getGraphicsConfiguration();
/*     */     }
/*     */ 
/* 132 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/* 134 */     GraphicsDevice localGraphicsDevice = localGraphicsEnvironment.getDefaultScreenDevice();
/* 135 */     return (WGLGraphicsConfig)localGraphicsDevice.getDefaultConfiguration();
/*     */   }
/*     */ 
/*     */   public static native boolean updateWindowAccelImpl(long paramLong, WComponentPeer paramWComponentPeer, int paramInt1, int paramInt2);
/*     */ 
/*     */   public static class WGLOffScreenSurfaceData extends WGLSurfaceData
/*     */   {
/*     */     private Image offscreenImage;
/*     */     private int width;
/*     */     private int height;
/*     */ 
/*     */     public WGLOffScreenSurfaceData(WComponentPeer paramWComponentPeer, WGLGraphicsConfig paramWGLGraphicsConfig, int paramInt1, int paramInt2, Image paramImage, ColorModel paramColorModel, int paramInt3)
/*     */     {
/* 212 */       super(paramWGLGraphicsConfig, paramColorModel, paramInt3);
/*     */ 
/* 214 */       this.width = paramInt1;
/* 215 */       this.height = paramInt2;
/* 216 */       this.offscreenImage = paramImage;
/*     */ 
/* 218 */       initSurface(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public SurfaceData getReplacement() {
/* 222 */       return restoreContents(this.offscreenImage);
/*     */     }
/*     */ 
/*     */     public Rectangle getBounds() {
/* 226 */       if (this.type == 4) {
/* 227 */         Rectangle localRectangle = this.peer.getBounds();
/* 228 */         localRectangle.x = (localRectangle.y = 0);
/* 229 */         return localRectangle;
/*     */       }
/* 231 */       return new Rectangle(this.width, this.height);
/*     */     }
/*     */ 
/*     */     public Object getDestination()
/*     */     {
/* 239 */       return this.offscreenImage;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class WGLVSyncOffScreenSurfaceData extends WGLSurfaceData.WGLOffScreenSurfaceData
/*     */   {
/*     */     private WGLSurfaceData.WGLOffScreenSurfaceData flipSurface;
/*     */ 
/*     */     public WGLVSyncOffScreenSurfaceData(WComponentPeer paramWComponentPeer, WGLGraphicsConfig paramWGLGraphicsConfig, int paramInt1, int paramInt2, Image paramImage, ColorModel paramColorModel, int paramInt3)
/*     */     {
/* 185 */       super(paramWGLGraphicsConfig, paramInt1, paramInt2, paramImage, paramColorModel, paramInt3);
/* 186 */       this.flipSurface = WGLSurfaceData.createData(paramWComponentPeer, paramImage, 4);
/*     */     }
/*     */ 
/*     */     public SurfaceData getFlipSurface() {
/* 190 */       return this.flipSurface;
/*     */     }
/*     */ 
/*     */     public void flush()
/*     */     {
/* 195 */       this.flipSurface.flush();
/* 196 */       super.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class WGLWindowSurfaceData extends WGLSurfaceData
/*     */   {
/*     */     public WGLWindowSurfaceData(WComponentPeer paramWComponentPeer, WGLGraphicsConfig paramWGLGraphicsConfig)
/*     */     {
/* 144 */       super(paramWGLGraphicsConfig, paramWComponentPeer.getColorModel(), 1);
/*     */     }
/*     */ 
/*     */     public SurfaceData getReplacement() {
/* 148 */       return this.peer.getSurfaceData();
/*     */     }
/*     */ 
/*     */     public Rectangle getBounds() {
/* 152 */       Rectangle localRectangle = this.peer.getBounds();
/* 153 */       localRectangle.x = (localRectangle.y = 0);
/* 154 */       return localRectangle;
/*     */     }
/*     */ 
/*     */     public Object getDestination()
/*     */     {
/* 161 */       return this.peer.getTarget();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.WGLSurfaceData
 * JD-Core Version:    0.6.2
 */