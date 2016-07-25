/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.windows.WComponentPeer;
/*     */ import sun.java2d.InvalidPipeException;
/*     */ import sun.java2d.ScreenUpdateManager;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.windows.GDIWindowSurfaceData;
/*     */ import sun.java2d.windows.WindowsFlags;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ 
/*     */ public class D3DScreenUpdateManager extends ScreenUpdateManager
/*     */   implements Runnable
/*     */ {
/*     */   private static final int MIN_WIN_SIZE = 150;
/*     */   private volatile boolean done;
/*     */   private volatile Thread screenUpdater;
/*     */   private boolean needsUpdateNow;
/*  78 */   private Object runLock = new Object();
/*     */   private ArrayList<D3DSurfaceData.D3DWindowSurfaceData> d3dwSurfaces;
/*     */   private HashMap<D3DSurfaceData.D3DWindowSurfaceData, GDIWindowSurfaceData> gdiSurfaces;
/*     */ 
/*     */   public D3DScreenUpdateManager()
/*     */   {
/*  94 */     this.done = false;
/*  95 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/*  99 */         ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/* 100 */         Thread localThread = new Thread(localThreadGroup, new Runnable()
/*     */         {
/*     */           public void run() {
/* 103 */             D3DScreenUpdateManager.this.done = true;
/* 104 */             D3DScreenUpdateManager.this.wakeUpUpdateThread();
/*     */           }
/*     */         });
/* 107 */         localThread.setContextClassLoader(null);
/*     */         try {
/* 109 */           Runtime.getRuntime().addShutdownHook(localThread);
/*     */         } catch (Exception localException) {
/* 111 */           D3DScreenUpdateManager.this.done = true;
/*     */         }
/* 113 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public SurfaceData createScreenSurface(Win32GraphicsConfig paramWin32GraphicsConfig, WComponentPeer paramWComponentPeer, int paramInt, boolean paramBoolean)
/*     */   {
/* 157 */     if ((this.done) || (!(paramWin32GraphicsConfig instanceof D3DGraphicsConfig))) {
/* 158 */       return super.createScreenSurface(paramWin32GraphicsConfig, paramWComponentPeer, paramInt, paramBoolean);
/*     */     }
/*     */ 
/* 161 */     Object localObject = null;
/*     */ 
/* 163 */     if (canUseD3DOnScreen(paramWComponentPeer, paramWin32GraphicsConfig, paramInt))
/*     */     {
/*     */       try
/*     */       {
/* 169 */         localObject = D3DSurfaceData.createData(paramWComponentPeer);
/*     */       } catch (InvalidPipeException localInvalidPipeException) {
/* 171 */         localObject = null;
/*     */       }
/*     */     }
/* 174 */     if (localObject == null) {
/* 175 */       localObject = GDIWindowSurfaceData.createData(paramWComponentPeer);
/*     */     }
/*     */ 
/* 182 */     if (paramBoolean)
/*     */     {
/* 189 */       repaintPeerTarget(paramWComponentPeer);
/*     */     }
/*     */ 
/* 192 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static boolean canUseD3DOnScreen(WComponentPeer paramWComponentPeer, Win32GraphicsConfig paramWin32GraphicsConfig, int paramInt)
/*     */   {
/* 216 */     if (!(paramWin32GraphicsConfig instanceof D3DGraphicsConfig)) {
/* 217 */       return false;
/*     */     }
/* 219 */     D3DGraphicsConfig localD3DGraphicsConfig = (D3DGraphicsConfig)paramWin32GraphicsConfig;
/* 220 */     D3DGraphicsDevice localD3DGraphicsDevice = localD3DGraphicsConfig.getD3DDevice();
/* 221 */     String str = paramWComponentPeer.getClass().getName();
/* 222 */     Rectangle localRectangle = paramWComponentPeer.getBounds();
/* 223 */     Component localComponent = (Component)paramWComponentPeer.getTarget();
/* 224 */     Window localWindow = localD3DGraphicsDevice.getFullScreenWindow();
/*     */ 
/* 226 */     return (WindowsFlags.isD3DOnScreenEnabled()) && (localD3DGraphicsDevice.isD3DEnabledOnDevice()) && (paramWComponentPeer.isAccelCapable()) && ((localRectangle.width > 150) || (localRectangle.height > 150)) && (paramInt == 0) && ((localWindow == null) || ((localWindow == localComponent) && (!hasHWChildren(localComponent)))) && ((str.equals("sun.awt.windows.WCanvasPeer")) || (str.equals("sun.awt.windows.WDialogPeer")) || (str.equals("sun.awt.windows.WPanelPeer")) || (str.equals("sun.awt.windows.WWindowPeer")) || (str.equals("sun.awt.windows.WFramePeer")) || (str.equals("sun.awt.windows.WEmbeddedFramePeer")));
/*     */   }
/*     */ 
/*     */   public Graphics2D createGraphics(SurfaceData paramSurfaceData, WComponentPeer paramWComponentPeer, Color paramColor1, Color paramColor2, Font paramFont)
/*     */   {
/* 263 */     if ((!this.done) && ((paramSurfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData))) {
/* 264 */       D3DSurfaceData.D3DWindowSurfaceData localD3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData)paramSurfaceData;
/* 265 */       if ((!localD3DWindowSurfaceData.isSurfaceLost()) || (validate(localD3DWindowSurfaceData))) {
/* 266 */         trackScreenSurface(localD3DWindowSurfaceData);
/* 267 */         return new SunGraphics2D(paramSurfaceData, paramColor1, paramColor2, paramFont);
/*     */       }
/*     */ 
/* 273 */       paramSurfaceData = getGdiSurface(localD3DWindowSurfaceData);
/*     */     }
/* 275 */     return super.createGraphics(paramSurfaceData, paramWComponentPeer, paramColor1, paramColor2, paramFont);
/*     */   }
/*     */ 
/*     */   private void repaintPeerTarget(WComponentPeer paramWComponentPeer)
/*     */   {
/* 283 */     Component localComponent = (Component)paramWComponentPeer.getTarget();
/* 284 */     Rectangle localRectangle = AWTAccessor.getComponentAccessor().getBounds(localComponent);
/*     */ 
/* 288 */     paramWComponentPeer.handlePaint(0, 0, localRectangle.width, localRectangle.height);
/*     */   }
/*     */ 
/*     */   private void trackScreenSurface(SurfaceData paramSurfaceData)
/*     */   {
/* 297 */     if ((!this.done) && ((paramSurfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData))) {
/* 298 */       synchronized (this) {
/* 299 */         if (this.d3dwSurfaces == null) {
/* 300 */           this.d3dwSurfaces = new ArrayList();
/*     */         }
/* 302 */         D3DSurfaceData.D3DWindowSurfaceData localD3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData)paramSurfaceData;
/* 303 */         if (!this.d3dwSurfaces.contains(localD3DWindowSurfaceData)) {
/* 304 */           this.d3dwSurfaces.add(localD3DWindowSurfaceData);
/*     */         }
/*     */       }
/* 307 */       startUpdateThread();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void dropScreenSurface(SurfaceData paramSurfaceData)
/*     */   {
/* 313 */     if ((this.d3dwSurfaces != null) && ((paramSurfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData))) {
/* 314 */       D3DSurfaceData.D3DWindowSurfaceData localD3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData)paramSurfaceData;
/* 315 */       removeGdiSurface(localD3DWindowSurfaceData);
/* 316 */       this.d3dwSurfaces.remove(localD3DWindowSurfaceData);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SurfaceData getReplacementScreenSurface(WComponentPeer paramWComponentPeer, SurfaceData paramSurfaceData)
/*     */   {
/* 324 */     SurfaceData localSurfaceData = super.getReplacementScreenSurface(paramWComponentPeer, paramSurfaceData);
/*     */ 
/* 328 */     trackScreenSurface(localSurfaceData);
/* 329 */     return localSurfaceData;
/*     */   }
/*     */ 
/*     */   private void removeGdiSurface(D3DSurfaceData.D3DWindowSurfaceData paramD3DWindowSurfaceData)
/*     */   {
/* 339 */     if (this.gdiSurfaces != null) {
/* 340 */       GDIWindowSurfaceData localGDIWindowSurfaceData = (GDIWindowSurfaceData)this.gdiSurfaces.get(paramD3DWindowSurfaceData);
/* 341 */       if (localGDIWindowSurfaceData != null) {
/* 342 */         localGDIWindowSurfaceData.invalidate();
/* 343 */         this.gdiSurfaces.remove(paramD3DWindowSurfaceData);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void startUpdateThread()
/*     */   {
/* 353 */     if (this.screenUpdater == null) {
/* 354 */       this.screenUpdater = ((Thread)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Thread run()
/*     */         {
/* 358 */           ThreadGroup localThreadGroup = ThreadGroupUtils.getRootThreadGroup();
/* 359 */           Thread localThread = new Thread(localThreadGroup, D3DScreenUpdateManager.this, "D3D Screen Updater");
/*     */ 
/* 363 */           localThread.setPriority(7);
/* 364 */           localThread.setDaemon(true);
/* 365 */           return localThread;
/*     */         }
/*     */       }));
/* 368 */       this.screenUpdater.start();
/*     */     } else {
/* 370 */       wakeUpUpdateThread();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void wakeUpUpdateThread()
/*     */   {
/* 386 */     synchronized (this.runLock) {
/* 387 */       this.runLock.notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void runUpdateNow()
/*     */   {
/* 401 */     synchronized (this)
/*     */     {
/* 404 */       if ((this.done) || (this.screenUpdater == null) || (this.d3dwSurfaces == null) || (this.d3dwSurfaces.size() == 0))
/*     */       {
/* 407 */         return;
/*     */       }
/*     */     }
/* 410 */     synchronized (this.runLock) {
/* 411 */       this.needsUpdateNow = true;
/* 412 */       this.runLock.notifyAll();
/* 413 */       while (this.needsUpdateNow)
/*     */         try {
/* 415 */           this.runLock.wait();
/*     */         } catch (InterruptedException localInterruptedException) {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run() {
/* 422 */     while (!this.done) {
/* 423 */       synchronized (this.runLock)
/*     */       {
/* 428 */         long l = this.d3dwSurfaces.size() > 0 ? 100L : 0L;
/*     */ 
/* 431 */         if (!this.needsUpdateNow) try {
/* 432 */             this.runLock.wait(l);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/*     */ 
/*     */       }
/*     */ 
/* 440 */       ??? = new D3DSurfaceData.D3DWindowSurfaceData[0];
/* 441 */       synchronized (this) {
/* 442 */         ??? = (D3DSurfaceData.D3DWindowSurfaceData[])this.d3dwSurfaces.toArray((Object[])???);
/*     */       }
/* 444 */       for (D3DSurfaceData localD3DSurfaceData : ???)
/*     */       {
/* 447 */         if ((localD3DSurfaceData.isValid()) && ((localD3DSurfaceData.isDirty()) || (localD3DSurfaceData.isSurfaceLost()))) {
/* 448 */           if (!localD3DSurfaceData.isSurfaceLost())
/*     */           {
/* 452 */             D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 453 */             localD3DRenderQueue.lock();
/*     */             try {
/* 455 */               Rectangle localRectangle = localD3DSurfaceData.getBounds();
/* 456 */               D3DSurfaceData.swapBuffers(localD3DSurfaceData, 0, 0, localRectangle.width, localRectangle.height);
/*     */ 
/* 458 */               localD3DSurfaceData.markClean();
/*     */             } finally {
/* 460 */               localD3DRenderQueue.unlock();
/*     */             }
/* 462 */           } else if (!validate(localD3DSurfaceData))
/*     */           {
/* 467 */             localD3DSurfaceData.getPeer().replaceSurfaceDataLater();
/*     */           }
/*     */         }
/*     */       }
/* 471 */       synchronized (this.runLock) {
/* 472 */         this.needsUpdateNow = false;
/* 473 */         this.runLock.notifyAll();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean validate(D3DSurfaceData.D3DWindowSurfaceData paramD3DWindowSurfaceData)
/*     */   {
/* 485 */     if (paramD3DWindowSurfaceData.isSurfaceLost()) {
/*     */       try {
/* 487 */         paramD3DWindowSurfaceData.restoreSurface();
/*     */ 
/* 490 */         Color localColor = paramD3DWindowSurfaceData.getPeer().getBackgroundNoSync();
/* 491 */         SunGraphics2D localSunGraphics2D = new SunGraphics2D(paramD3DWindowSurfaceData, localColor, localColor, null);
/* 492 */         localSunGraphics2D.fillRect(0, 0, paramD3DWindowSurfaceData.getBounds().width, paramD3DWindowSurfaceData.getBounds().height);
/* 493 */         localSunGraphics2D.dispose();
/*     */ 
/* 498 */         paramD3DWindowSurfaceData.markClean();
/*     */ 
/* 501 */         repaintPeerTarget(paramD3DWindowSurfaceData.getPeer());
/*     */       } catch (InvalidPipeException localInvalidPipeException) {
/* 503 */         return false;
/*     */       }
/*     */     }
/* 506 */     return true;
/*     */   }
/*     */ 
/*     */   private synchronized SurfaceData getGdiSurface(D3DSurfaceData.D3DWindowSurfaceData paramD3DWindowSurfaceData)
/*     */   {
/* 517 */     if (this.gdiSurfaces == null) {
/* 518 */       this.gdiSurfaces = new HashMap();
/*     */     }
/*     */ 
/* 521 */     GDIWindowSurfaceData localGDIWindowSurfaceData = (GDIWindowSurfaceData)this.gdiSurfaces.get(paramD3DWindowSurfaceData);
/* 522 */     if (localGDIWindowSurfaceData == null) {
/* 523 */       localGDIWindowSurfaceData = GDIWindowSurfaceData.createData(paramD3DWindowSurfaceData.getPeer());
/* 524 */       this.gdiSurfaces.put(paramD3DWindowSurfaceData, localGDIWindowSurfaceData);
/*     */     }
/* 526 */     return localGDIWindowSurfaceData;
/*     */   }
/*     */ 
/*     */   private static boolean hasHWChildren(Component paramComponent)
/*     */   {
/* 536 */     if ((paramComponent instanceof Container)) {
/* 537 */       for (Component localComponent : ((Container)paramComponent).getComponents()) {
/* 538 */         if (((localComponent.getPeer() instanceof WComponentPeer)) || (hasHWChildren(localComponent))) {
/* 539 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 543 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DScreenUpdateManager
 * JD-Core Version:    0.6.2
 */