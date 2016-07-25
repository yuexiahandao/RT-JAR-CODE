/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Window;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.security.AccessController;
/*     */ import sun.awt.image.BufImgSurfaceData;
/*     */ import sun.java2d.DestSurfaceProvider;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.Surface;
/*     */ import sun.java2d.d3d.D3DSurfaceData;
/*     */ import sun.java2d.opengl.WGLSurfaceData;
/*     */ import sun.java2d.pipe.BufferedContext;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.java2d.pipe.hw.AccelGraphicsConfig;
/*     */ import sun.java2d.pipe.hw.AccelSurface;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class TranslucentWindowPainter
/*     */ {
/*     */   protected Window window;
/*     */   protected WWindowPeer peer;
/*  67 */   private static final boolean forceOpt = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.twp.forceopt", "false"))).booleanValue();
/*     */ 
/*  70 */   private static final boolean forceSW = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.twp.forcesw", "false"))).booleanValue();
/*     */ 
/*     */   public static TranslucentWindowPainter createInstance(WWindowPeer paramWWindowPeer)
/*     */   {
/*  78 */     GraphicsConfiguration localGraphicsConfiguration = paramWWindowPeer.getGraphicsConfiguration();
/*  79 */     if ((!forceSW) && ((localGraphicsConfiguration instanceof AccelGraphicsConfig))) {
/*  80 */       String str = localGraphicsConfiguration.getClass().getSimpleName();
/*  81 */       AccelGraphicsConfig localAccelGraphicsConfig = (AccelGraphicsConfig)localGraphicsConfiguration;
/*     */ 
/*  84 */       if (((localAccelGraphicsConfig.getContextCapabilities().getCaps() & 0x100) != 0) || (forceOpt))
/*     */       {
/*  89 */         if (str.startsWith("D3D"))
/*  90 */           return new VIOptD3DWindowPainter(paramWWindowPeer);
/*  91 */         if ((forceOpt) && (str.startsWith("WGL")))
/*     */         {
/*  95 */           return new VIOptWGLWindowPainter(paramWWindowPeer);
/*     */         }
/*     */       }
/*     */     }
/*  99 */     return new BIWindowPainter(paramWWindowPeer);
/*     */   }
/*     */ 
/*     */   protected TranslucentWindowPainter(WWindowPeer paramWWindowPeer) {
/* 103 */     this.peer = paramWWindowPeer;
/* 104 */     this.window = ((Window)paramWWindowPeer.getTarget());
/*     */   }
/*     */ 
/*     */   protected abstract Image getBackBuffer(boolean paramBoolean);
/*     */ 
/*     */   protected abstract boolean update(Image paramImage);
/*     */ 
/*     */   public abstract void flush();
/*     */ 
/*     */   public void updateWindow(boolean paramBoolean)
/*     */   {
/* 133 */     boolean bool = false;
/* 134 */     Image localImage = getBackBuffer(paramBoolean);
/* 135 */     while (!bool) {
/* 136 */       if (paramBoolean) {
/* 137 */         Graphics2D localGraphics2D = (Graphics2D)localImage.getGraphics();
/*     */         try {
/* 139 */           this.window.paintAll(localGraphics2D);
/*     */         } finally {
/* 141 */           localGraphics2D.dispose();
/*     */         }
/*     */       }
/*     */ 
/* 145 */       bool = update(localImage);
/* 146 */       if (!bool) {
/* 147 */         paramBoolean = true;
/* 148 */         localImage = getBackBuffer(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final Image clearImage(Image paramImage) {
/* 154 */     Graphics2D localGraphics2D = (Graphics2D)paramImage.getGraphics();
/* 155 */     int i = paramImage.getWidth(null);
/* 156 */     int j = paramImage.getHeight(null);
/*     */ 
/* 158 */     localGraphics2D.setComposite(AlphaComposite.Src);
/* 159 */     localGraphics2D.setColor(new Color(0, 0, 0, 0));
/* 160 */     localGraphics2D.fillRect(0, 0, i, j);
/*     */ 
/* 162 */     return paramImage;
/*     */   }
/*     */ 
/*     */   private static class BIWindowPainter extends TranslucentWindowPainter
/*     */   {
/*     */     private BufferedImage backBuffer;
/*     */ 
/*     */     protected BIWindowPainter(WWindowPeer paramWWindowPeer)
/*     */     {
/* 177 */       super();
/*     */     }
/*     */ 
/*     */     protected Image getBackBuffer(boolean paramBoolean)
/*     */     {
/* 182 */       int i = this.window.getWidth();
/* 183 */       int j = this.window.getHeight();
/* 184 */       if ((this.backBuffer == null) || (this.backBuffer.getWidth() != i) || (this.backBuffer.getHeight() != j))
/*     */       {
/* 188 */         flush();
/* 189 */         this.backBuffer = new BufferedImage(i, j, 3);
/*     */       }
/* 191 */       return paramBoolean ? (BufferedImage)TranslucentWindowPainter.clearImage(this.backBuffer) : this.backBuffer;
/*     */     }
/*     */ 
/*     */     protected boolean update(Image paramImage)
/*     */     {
/* 196 */       VolatileImage localVolatileImage = null;
/*     */ 
/* 198 */       if ((paramImage instanceof BufferedImage)) {
/* 199 */         localObject = (BufferedImage)paramImage;
/* 200 */         int[] arrayOfInt1 = ((DataBufferInt)((BufferedImage)localObject).getRaster().getDataBuffer()).getData();
/*     */ 
/* 202 */         this.peer.updateWindowImpl(arrayOfInt1, ((BufferedImage)localObject).getWidth(), ((BufferedImage)localObject).getHeight());
/* 203 */         return true;
/* 204 */       }if ((paramImage instanceof VolatileImage)) {
/* 205 */         localVolatileImage = (VolatileImage)paramImage;
/* 206 */         if ((paramImage instanceof DestSurfaceProvider)) {
/* 207 */           localObject = ((DestSurfaceProvider)paramImage).getDestSurface();
/* 208 */           if ((localObject instanceof BufImgSurfaceData))
/*     */           {
/* 212 */             int i = localVolatileImage.getWidth();
/* 213 */             int j = localVolatileImage.getHeight();
/* 214 */             BufImgSurfaceData localBufImgSurfaceData = (BufImgSurfaceData)localObject;
/* 215 */             int[] arrayOfInt3 = ((DataBufferInt)localBufImgSurfaceData.getRaster(0, 0, i, j).getDataBuffer()).getData();
/*     */ 
/* 217 */             this.peer.updateWindowImpl(arrayOfInt3, i, j);
/* 218 */             return true;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 224 */       Object localObject = (BufferedImage)TranslucentWindowPainter.clearImage(this.backBuffer);
/*     */ 
/* 226 */       int[] arrayOfInt2 = ((DataBufferInt)((BufferedImage)localObject).getRaster().getDataBuffer()).getData();
/*     */ 
/* 228 */       this.peer.updateWindowImpl(arrayOfInt2, ((BufferedImage)localObject).getWidth(), ((BufferedImage)localObject).getHeight());
/*     */ 
/* 230 */       return !localVolatileImage.contentsLost();
/*     */     }
/*     */ 
/*     */     public void flush() {
/* 234 */       if (this.backBuffer != null) {
/* 235 */         this.backBuffer.flush();
/* 236 */         this.backBuffer = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class VIOptD3DWindowPainter extends TranslucentWindowPainter.VIOptWindowPainter
/*     */   {
/*     */     protected VIOptD3DWindowPainter(WWindowPeer paramWWindowPeer)
/*     */     {
/* 335 */       super();
/*     */     }
/*     */ 
/*     */     protected boolean updateWindowAccel(long paramLong, int paramInt1, int paramInt2)
/*     */     {
/* 342 */       return D3DSurfaceData.updateWindowAccelImpl(paramLong, this.peer.getData(), paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class VIOptWGLWindowPainter extends TranslucentWindowPainter.VIOptWindowPainter
/*     */   {
/*     */     protected VIOptWGLWindowPainter(WWindowPeer paramWWindowPeer)
/*     */     {
/* 350 */       super();
/*     */     }
/*     */ 
/*     */     protected boolean updateWindowAccel(long paramLong, int paramInt1, int paramInt2)
/*     */     {
/* 357 */       return WGLSurfaceData.updateWindowAccelImpl(paramLong, this.peer, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class VIOptWindowPainter extends TranslucentWindowPainter.VIWindowPainter
/*     */   {
/*     */     protected VIOptWindowPainter(WWindowPeer paramWWindowPeer)
/*     */     {
/* 296 */       super();
/*     */     }
/*     */ 
/*     */     protected abstract boolean updateWindowAccel(long paramLong, int paramInt1, int paramInt2);
/*     */ 
/*     */     protected boolean update(Image paramImage)
/*     */     {
/* 303 */       if ((paramImage instanceof DestSurfaceProvider)) {
/* 304 */         Surface localSurface = ((DestSurfaceProvider)paramImage).getDestSurface();
/* 305 */         if ((localSurface instanceof AccelSurface)) {
/* 306 */           final int i = paramImage.getWidth(null);
/* 307 */           final int j = paramImage.getHeight(null);
/* 308 */           final boolean[] arrayOfBoolean = { false };
/* 309 */           final AccelSurface localAccelSurface = (AccelSurface)localSurface;
/* 310 */           RenderQueue localRenderQueue = localAccelSurface.getContext().getRenderQueue();
/* 311 */           localRenderQueue.lock();
/*     */           try {
/* 313 */             localAccelSurface.getContext(); BufferedContext.validateContext(localAccelSurface);
/* 314 */             localRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */               public void run() {
/* 316 */                 long l = localAccelSurface.getNativeOps();
/* 317 */                 arrayOfBoolean[0] = TranslucentWindowPainter.VIOptWindowPainter.this.updateWindowAccel(l, i, j);
/*     */               } } );
/*     */           }
/*     */           catch (InvalidPipeException localInvalidPipeException) {
/*     */           }
/*     */           finally {
/* 323 */             localRenderQueue.unlock();
/*     */           }
/* 325 */           return arrayOfBoolean[0];
/*     */         }
/*     */       }
/* 328 */       return super.update(paramImage);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class VIWindowPainter extends TranslucentWindowPainter.BIWindowPainter
/*     */   {
/*     */     private VolatileImage viBB;
/*     */ 
/*     */     protected VIWindowPainter(WWindowPeer paramWWindowPeer)
/*     */     {
/* 250 */       super();
/*     */     }
/*     */ 
/*     */     protected Image getBackBuffer(boolean paramBoolean)
/*     */     {
/* 255 */       int i = this.window.getWidth();
/* 256 */       int j = this.window.getHeight();
/* 257 */       GraphicsConfiguration localGraphicsConfiguration = this.peer.getGraphicsConfiguration();
/*     */ 
/* 259 */       if ((this.viBB == null) || (this.viBB.getWidth() != i) || (this.viBB.getHeight() != j) || (this.viBB.validate(localGraphicsConfiguration) == 2))
/*     */       {
/* 262 */         flush();
/*     */ 
/* 264 */         if ((localGraphicsConfiguration instanceof AccelGraphicsConfig)) {
/* 265 */           AccelGraphicsConfig localAccelGraphicsConfig = (AccelGraphicsConfig)localGraphicsConfiguration;
/* 266 */           this.viBB = localAccelGraphicsConfig.createCompatibleVolatileImage(i, j, 3, 2);
/*     */         }
/*     */ 
/* 270 */         if (this.viBB == null) {
/* 271 */           this.viBB = localGraphicsConfiguration.createCompatibleVolatileImage(i, j, 3);
/*     */         }
/* 273 */         this.viBB.validate(localGraphicsConfiguration);
/*     */       }
/*     */ 
/* 276 */       return paramBoolean ? TranslucentWindowPainter.clearImage(this.viBB) : this.viBB;
/*     */     }
/*     */ 
/*     */     public void flush()
/*     */     {
/* 281 */       if (this.viBB != null) {
/* 282 */         this.viBB.flush();
/* 283 */         this.viBB = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.TranslucentWindowPainter
 * JD-Core Version:    0.6.2
 */