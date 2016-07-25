/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.awt.Dialog;
/*     */ import java.awt.DisplayMode;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.awt.peer.WindowPeer;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import sun.awt.Win32GraphicsDevice;
/*     */ import sun.awt.windows.WWindowPeer;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ import sun.java2d.windows.WindowsFlags;
/*     */ import sun.misc.PerfCounter;
/*     */ 
/*     */ public class D3DGraphicsDevice extends Win32GraphicsDevice
/*     */ {
/*     */   private D3DContext context;
/*     */   private static boolean d3dAvailable;
/*     */   private ContextCapabilities d3dCaps;
/*     */   private boolean fsStatus;
/* 171 */   private Rectangle ownerOrigBounds = null;
/*     */   private boolean ownerWasVisible;
/*     */   private Window realFSWindow;
/*     */   private WindowListener fsWindowListener;
/*     */   private boolean fsWindowWasAlwaysOnTop;
/*     */ 
/*     */   private static native boolean initD3D();
/*     */ 
/*     */   public static D3DGraphicsDevice createDevice(int paramInt)
/*     */   {
/*  83 */     if (!d3dAvailable) {
/*  84 */       return null;
/*     */     }
/*     */ 
/*  87 */     ContextCapabilities localContextCapabilities = getDeviceCaps(paramInt);
/*     */ 
/*  89 */     if ((localContextCapabilities.getCaps() & 0x40000) == 0) {
/*  90 */       if (WindowsFlags.isD3DVerbose()) {
/*  91 */         System.out.println("Could not enable Direct3D pipeline on screen " + paramInt);
/*     */       }
/*     */ 
/*  94 */       return null;
/*     */     }
/*  96 */     if (WindowsFlags.isD3DVerbose()) {
/*  97 */       System.out.println("Direct3D pipeline enabled on screen " + paramInt);
/*     */     }
/*     */ 
/* 100 */     D3DGraphicsDevice localD3DGraphicsDevice = new D3DGraphicsDevice(paramInt, localContextCapabilities);
/* 101 */     return localD3DGraphicsDevice;
/*     */   }
/*     */   private static native int getDeviceCapsNative(int paramInt);
/*     */ 
/*     */   private static native String getDeviceIdNative(int paramInt);
/*     */ 
/* 107 */   private static ContextCapabilities getDeviceCaps(final int paramInt) { D3DContext.D3DContextCaps localD3DContextCaps = null;
/* 108 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 109 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 115 */       Object local1Result = new Object()
/*     */       {
/*     */         int caps;
/*     */         String id;
/*     */       };
/* 116 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 118 */           this.val$res.caps = D3DGraphicsDevice.getDeviceCapsNative(paramInt);
/* 119 */           this.val$res.id = D3DGraphicsDevice.getDeviceIdNative(paramInt);
/*     */         }
/*     */       });
/* 122 */       localD3DContextCaps = new D3DContext.D3DContextCaps(local1Result.caps, local1Result.id);
/*     */     } finally {
/* 124 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */ 
/* 127 */     return localD3DContextCaps != null ? localD3DContextCaps : new D3DContext.D3DContextCaps(0, null); }
/*     */ 
/*     */   public final boolean isCapPresent(int paramInt)
/*     */   {
/* 131 */     return (this.d3dCaps.getCaps() & paramInt) != 0;
/*     */   }
/*     */ 
/*     */   private D3DGraphicsDevice(int paramInt, ContextCapabilities paramContextCapabilities) {
/* 135 */     super(paramInt);
/* 136 */     this.descString = ("D3DGraphicsDevice[screen=" + paramInt);
/* 137 */     this.d3dCaps = paramContextCapabilities;
/* 138 */     this.context = new D3DContext(D3DRenderQueue.getInstance(), this);
/*     */   }
/*     */ 
/*     */   public boolean isD3DEnabledOnDevice() {
/* 142 */     return (isValid()) && (isCapPresent(262144));
/*     */   }
/*     */ 
/*     */   public static boolean isD3DAvailable()
/*     */   {
/* 150 */     return d3dAvailable;
/*     */   }
/*     */ 
/*     */   private Frame getToplevelOwner(Window paramWindow)
/*     */   {
/* 159 */     Window localWindow = paramWindow;
/* 160 */     while (localWindow != null) {
/* 161 */       localWindow = localWindow.getOwner();
/* 162 */       if ((localWindow instanceof Frame)) {
/* 163 */         return (Frame)localWindow;
/*     */       }
/*     */     }
/*     */ 
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   private static native boolean enterFullScreenExclusiveNative(int paramInt, long paramLong);
/*     */ 
/*     */   protected void enterFullScreenExclusive(final int paramInt, WindowPeer paramWindowPeer)
/*     */   {
/* 182 */     final WWindowPeer localWWindowPeer = (WWindowPeer)this.realFSWindow.getPeer();
/*     */ 
/* 184 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 185 */     localD3DRenderQueue.lock();
/*     */     try {
/* 187 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 189 */           long l = localWWindowPeer.getHWnd();
/* 190 */           if (l == 0L)
/*     */           {
/* 192 */             D3DGraphicsDevice.this.fsStatus = false;
/* 193 */             return;
/*     */           }
/* 195 */           D3DGraphicsDevice.this.fsStatus = D3DGraphicsDevice.enterFullScreenExclusiveNative(paramInt, l);
/*     */         } } );
/*     */     }
/*     */     finally {
/* 199 */       localD3DRenderQueue.unlock();
/*     */     }
/* 201 */     if (!this.fsStatus)
/* 202 */       super.enterFullScreenExclusive(paramInt, paramWindowPeer);
/*     */   }
/*     */ 
/*     */   private static native boolean exitFullScreenExclusiveNative(int paramInt);
/*     */ 
/*     */   protected void exitFullScreenExclusive(final int paramInt, WindowPeer paramWindowPeer)
/*     */   {
/* 209 */     if (this.fsStatus) {
/* 210 */       D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 211 */       localD3DRenderQueue.lock();
/*     */       try {
/* 213 */         localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */           public void run() {
/* 215 */             D3DGraphicsDevice.exitFullScreenExclusiveNative(paramInt);
/*     */           } } );
/*     */       }
/*     */       finally {
/* 219 */         localD3DRenderQueue.unlock();
/*     */       }
/*     */     } else {
/* 222 */       super.exitFullScreenExclusive(paramInt, paramWindowPeer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addFSWindowListener(Window paramWindow)
/*     */   {
/* 251 */     if ((!(paramWindow instanceof Frame)) && (!(paramWindow instanceof Dialog)) && ((this.realFSWindow = getToplevelOwner(paramWindow)) != null))
/*     */     {
/* 254 */       this.ownerOrigBounds = this.realFSWindow.getBounds();
/* 255 */       WWindowPeer localWWindowPeer = (WWindowPeer)this.realFSWindow.getPeer();
/*     */ 
/* 257 */       this.ownerWasVisible = this.realFSWindow.isVisible();
/* 258 */       Rectangle localRectangle = paramWindow.getBounds();
/*     */ 
/* 261 */       localWWindowPeer.reshape(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/* 262 */       localWWindowPeer.setVisible(true);
/*     */     } else {
/* 264 */       this.realFSWindow = paramWindow;
/*     */     }
/*     */ 
/* 267 */     this.fsWindowWasAlwaysOnTop = this.realFSWindow.isAlwaysOnTop();
/* 268 */     ((WWindowPeer)this.realFSWindow.getPeer()).setAlwaysOnTop(true);
/*     */ 
/* 270 */     this.fsWindowListener = new D3DFSWindowAdapter(null);
/* 271 */     this.realFSWindow.addWindowListener(this.fsWindowListener);
/*     */   }
/*     */ 
/*     */   protected void removeFSWindowListener(Window paramWindow)
/*     */   {
/* 276 */     this.realFSWindow.removeWindowListener(this.fsWindowListener);
/* 277 */     this.fsWindowListener = null;
/*     */ 
/* 289 */     WWindowPeer localWWindowPeer = (WWindowPeer)this.realFSWindow.getPeer();
/* 290 */     if (localWWindowPeer != null) {
/* 291 */       if (this.ownerOrigBounds != null)
/*     */       {
/* 294 */         if (this.ownerOrigBounds.width == 0) this.ownerOrigBounds.width = 1;
/* 295 */         if (this.ownerOrigBounds.height == 0) this.ownerOrigBounds.height = 1;
/* 296 */         localWWindowPeer.reshape(this.ownerOrigBounds.x, this.ownerOrigBounds.y, this.ownerOrigBounds.width, this.ownerOrigBounds.height);
/*     */ 
/* 298 */         if (!this.ownerWasVisible) {
/* 299 */           localWWindowPeer.setVisible(false);
/*     */         }
/* 301 */         this.ownerOrigBounds = null;
/*     */       }
/* 303 */       if (!this.fsWindowWasAlwaysOnTop) {
/* 304 */         localWWindowPeer.setAlwaysOnTop(false);
/*     */       }
/*     */     }
/*     */ 
/* 308 */     this.realFSWindow = null;
/*     */   }
/*     */ 
/*     */   private static native DisplayMode getCurrentDisplayModeNative(int paramInt);
/*     */ 
/*     */   protected DisplayMode getCurrentDisplayMode(final int paramInt) {
/* 314 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 315 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 320 */       final Object local2Result = new Object()
/*     */       {
/* 318 */         DisplayMode dm = null;
/*     */       };
/* 321 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 323 */           local2Result.dm = D3DGraphicsDevice.getCurrentDisplayModeNative(paramInt);
/*     */         }
/*     */       });
/*     */       DisplayMode localDisplayMode;
/* 326 */       if (local2Result.dm == null) {
/* 327 */         return super.getCurrentDisplayMode(paramInt);
/*     */       }
/* 329 */       return local2Result.dm;
/*     */     } finally {
/* 331 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void configDisplayModeNative(int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   protected void configDisplayMode(final int paramInt1, WindowPeer paramWindowPeer, final int paramInt2, final int paramInt3, final int paramInt4, final int paramInt5)
/*     */   {
/* 344 */     if (!this.fsStatus) {
/* 345 */       super.configDisplayMode(paramInt1, paramWindowPeer, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */ 
/* 347 */       return;
/*     */     }
/*     */ 
/* 350 */     final WWindowPeer localWWindowPeer = (WWindowPeer)this.realFSWindow.getPeer();
/*     */ 
/* 357 */     if (getFullScreenWindow() != this.realFSWindow) {
/* 358 */       localObject1 = getDefaultConfiguration().getBounds();
/* 359 */       localWWindowPeer.reshape(((Rectangle)localObject1).x, ((Rectangle)localObject1).y, paramInt2, paramInt3);
/*     */     }
/*     */ 
/* 362 */     Object localObject1 = D3DRenderQueue.getInstance();
/* 363 */     ((D3DRenderQueue)localObject1).lock();
/*     */     try {
/* 365 */       ((D3DRenderQueue)localObject1).flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 367 */           long l = localWWindowPeer.getHWnd();
/* 368 */           if (l == 0L)
/*     */           {
/* 370 */             return;
/*     */           }
/*     */ 
/* 374 */           D3DGraphicsDevice.configDisplayModeNative(paramInt1, l, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */         }
/*     */       });
/*     */     }
/*     */     finally {
/* 379 */       ((D3DRenderQueue)localObject1).unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void enumDisplayModesNative(int paramInt, ArrayList paramArrayList);
/*     */ 
/*     */   protected void enumDisplayModes(final int paramInt, final ArrayList paramArrayList)
/*     */   {
/* 387 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 388 */     localD3DRenderQueue.lock();
/*     */     try {
/* 390 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 392 */           D3DGraphicsDevice.enumDisplayModesNative(paramInt, paramArrayList);
/*     */         }
/*     */       });
/* 395 */       if (paramArrayList.size() == 0)
/* 396 */         paramArrayList.add(getCurrentDisplayModeNative(paramInt));
/*     */     }
/*     */     finally {
/* 399 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native long getAvailableAcceleratedMemoryNative(int paramInt);
/*     */ 
/*     */   public int getAvailableAcceleratedMemory() {
/* 406 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 407 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 412 */       final Object local3Result = new Object()
/*     */       {
/* 410 */         long mem = 0L;
/*     */       };
/* 413 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 415 */           local3Result.mem = D3DGraphicsDevice.getAvailableAcceleratedMemoryNative(D3DGraphicsDevice.this.getScreen());
/*     */         }
/*     */       });
/* 418 */       return (int)local3Result.mem;
/*     */     } finally {
/* 420 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration[] getConfigurations()
/*     */   {
/* 426 */     if ((this.configs == null) && 
/* 427 */       (isD3DEnabledOnDevice())) {
/* 428 */       this.defaultConfig = getDefaultConfiguration();
/* 429 */       if (this.defaultConfig != null) {
/* 430 */         this.configs = new GraphicsConfiguration[1];
/* 431 */         this.configs[0] = this.defaultConfig;
/* 432 */         return (GraphicsConfiguration[])this.configs.clone();
/*     */       }
/*     */     }
/*     */ 
/* 436 */     return super.getConfigurations();
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getDefaultConfiguration()
/*     */   {
/* 441 */     if (this.defaultConfig == null) {
/* 442 */       if (isD3DEnabledOnDevice())
/* 443 */         this.defaultConfig = new D3DGraphicsConfig(this);
/*     */       else {
/* 445 */         this.defaultConfig = super.getDefaultConfiguration();
/*     */       }
/*     */     }
/* 448 */     return this.defaultConfig;
/*     */   }
/*     */ 
/*     */   private static native boolean isD3DAvailableOnDeviceNative(int paramInt);
/*     */ 
/*     */   public static boolean isD3DAvailableOnDevice(final int paramInt) {
/* 454 */     if (!d3dAvailable) {
/* 455 */       return false;
/*     */     }
/*     */ 
/* 460 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/* 461 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 466 */       Object local4Result = new Object()
/*     */       {
/* 464 */         boolean avail = false;
/*     */       };
/* 467 */       localD3DRenderQueue.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/* 469 */           this.val$res.avail = D3DGraphicsDevice.isD3DAvailableOnDeviceNative(paramInt);
/*     */         }
/*     */       });
/* 472 */       return local4Result.avail;
/*     */     } finally {
/* 474 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   D3DContext getContext() {
/* 479 */     return this.context;
/*     */   }
/*     */ 
/*     */   ContextCapabilities getContextCapabilities() {
/* 483 */     return this.d3dCaps;
/*     */   }
/*     */ 
/*     */   public void displayChanged()
/*     */   {
/* 488 */     super.displayChanged();
/*     */ 
/* 491 */     if (d3dAvailable)
/* 492 */       this.d3dCaps = getDeviceCaps(getScreen());
/*     */   }
/*     */ 
/*     */   protected void invalidate(int paramInt)
/*     */   {
/* 498 */     super.invalidate(paramInt);
/*     */ 
/* 501 */     this.d3dCaps = new D3DContext.D3DContextCaps(0, null);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  65 */     Toolkit.getDefaultToolkit();
/*  66 */     d3dAvailable = initD3D();
/*  67 */     if (d3dAvailable)
/*     */     {
/*  69 */       pfDisabled = true;
/*  70 */       PerfCounter.getD3DAvailable().set(1L);
/*     */     } else {
/*  72 */       PerfCounter.getD3DAvailable().set(0L);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class D3DFSWindowAdapter extends WindowAdapter
/*     */   {
/*     */     public void windowDeactivated(WindowEvent paramWindowEvent)
/*     */     {
/* 239 */       D3DRenderQueue.getInstance(); D3DRenderQueue.restoreDevices();
/*     */     }
/*     */ 
/*     */     public void windowActivated(WindowEvent paramWindowEvent) {
/* 243 */       D3DRenderQueue.getInstance(); D3DRenderQueue.restoreDevices();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DGraphicsDevice
 * JD-Core Version:    0.6.2
 */