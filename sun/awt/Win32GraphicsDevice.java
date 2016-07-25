/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.AWTPermission;
/*     */ import java.awt.DisplayMode;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.peer.WindowPeer;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import sun.awt.windows.WWindowPeer;
/*     */ import sun.java2d.opengl.WGLGraphicsConfig;
/*     */ import sun.java2d.windows.WindowsFlags;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class Win32GraphicsDevice extends GraphicsDevice
/*     */   implements DisplayChangedListener
/*     */ {
/*     */   int screen;
/*     */   ColorModel dynamicColorModel;
/*     */   ColorModel colorModel;
/*     */   protected GraphicsConfiguration[] configs;
/*     */   protected GraphicsConfiguration defaultConfig;
/*     */   private final String idString;
/*     */   protected String descString;
/*     */   private boolean valid;
/*  72 */   private SunDisplayChanger topLevels = new SunDisplayChanger();
/*     */   protected static boolean pfDisabled;
/*     */   private static AWTPermission fullScreenExclusivePermission;
/*     */   private DisplayMode defaultDisplayMode;
/*     */   private WindowListener fsWindowListener;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   native void initDevice(int paramInt);
/*     */ 
/*     */   public Win32GraphicsDevice(int paramInt)
/*     */   {
/* 102 */     this.screen = paramInt;
/*     */ 
/* 106 */     this.idString = ("\\Display" + this.screen);
/*     */ 
/* 108 */     this.descString = ("Win32GraphicsDevice[screen=" + this.screen);
/* 109 */     this.valid = true;
/*     */ 
/* 111 */     initDevice(paramInt);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 121 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getScreen()
/*     */   {
/* 128 */     return this.screen;
/*     */   }
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 136 */     return this.valid;
/*     */   }
/*     */ 
/*     */   protected void invalidate(int paramInt)
/*     */   {
/* 145 */     this.valid = false;
/* 146 */     this.screen = paramInt;
/*     */   }
/*     */ 
/*     */   public String getIDstring()
/*     */   {
/* 154 */     return this.idString;
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration[] getConfigurations()
/*     */   {
/* 163 */     if (this.configs == null) {
/* 164 */       if ((WindowsFlags.isOGLEnabled()) && (isDefaultDevice())) {
/* 165 */         this.defaultConfig = getDefaultConfiguration();
/* 166 */         if (this.defaultConfig != null) {
/* 167 */           this.configs = new GraphicsConfiguration[1];
/* 168 */           this.configs[0] = this.defaultConfig;
/* 169 */           return (GraphicsConfiguration[])this.configs.clone();
/*     */         }
/*     */       }
/*     */ 
/* 173 */       int i = getMaxConfigs(this.screen);
/* 174 */       int j = getDefaultPixID(this.screen);
/* 175 */       Vector localVector = new Vector(i);
/* 176 */       if (j == 0)
/*     */       {
/* 178 */         this.defaultConfig = Win32GraphicsConfig.getConfig(this, j);
/*     */ 
/* 180 */         localVector.addElement(this.defaultConfig);
/*     */       }
/*     */       else {
/* 183 */         for (int k = 1; k <= i; k++) {
/* 184 */           if (isPixFmtSupported(k, this.screen)) {
/* 185 */             if (k == j) {
/* 186 */               this.defaultConfig = Win32GraphicsConfig.getConfig(this, k);
/*     */ 
/* 188 */               localVector.addElement(this.defaultConfig);
/*     */             }
/*     */             else {
/* 191 */               localVector.addElement(Win32GraphicsConfig.getConfig(this, k));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 197 */       this.configs = new GraphicsConfiguration[localVector.size()];
/* 198 */       localVector.copyInto(this.configs);
/*     */     }
/* 200 */     return (GraphicsConfiguration[])this.configs.clone();
/*     */   }
/*     */ 
/*     */   protected int getMaxConfigs(int paramInt)
/*     */   {
/* 210 */     if (pfDisabled) {
/* 211 */       return 1;
/*     */     }
/* 213 */     return getMaxConfigsImpl(paramInt);
/*     */   }
/*     */ 
/*     */   private native int getMaxConfigsImpl(int paramInt);
/*     */ 
/*     */   protected native boolean isPixFmtSupported(int paramInt1, int paramInt2);
/*     */ 
/*     */   protected int getDefaultPixID(int paramInt)
/*     */   {
/* 237 */     if (pfDisabled) {
/* 238 */       return 0;
/*     */     }
/* 240 */     return getDefaultPixIDImpl(paramInt);
/*     */   }
/*     */ 
/*     */   private native int getDefaultPixIDImpl(int paramInt);
/*     */ 
/*     */   public GraphicsConfiguration getDefaultConfiguration()
/*     */   {
/* 255 */     if (this.defaultConfig == null)
/*     */     {
/* 260 */       if ((WindowsFlags.isOGLEnabled()) && (isDefaultDevice())) {
/* 261 */         int i = WGLGraphicsConfig.getDefaultPixFmt(this.screen);
/* 262 */         this.defaultConfig = WGLGraphicsConfig.getConfig(this, i);
/* 263 */         if (WindowsFlags.isOGLVerbose()) {
/* 264 */           if (this.defaultConfig != null)
/* 265 */             System.out.print("OpenGL pipeline enabled");
/*     */           else {
/* 267 */             System.out.print("Could not enable OpenGL pipeline");
/*     */           }
/* 269 */           System.out.println(" for default config on screen " + this.screen);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 286 */       if (this.defaultConfig == null) {
/* 287 */         this.defaultConfig = Win32GraphicsConfig.getConfig(this, 0);
/*     */       }
/*     */     }
/* 290 */     return this.defaultConfig;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 294 */     return this.descString + ", removed]";
/*     */   }
/*     */ 
/*     */   private boolean isDefaultDevice()
/*     */   {
/* 302 */     return this == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
/*     */   }
/*     */ 
/*     */   private static boolean isFSExclusiveModeAllowed()
/*     */   {
/* 308 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 309 */     if (localSecurityManager != null) {
/* 310 */       if (fullScreenExclusivePermission == null) {
/* 311 */         fullScreenExclusivePermission = new AWTPermission("fullScreenExclusive");
/*     */       }
/*     */       try
/*     */       {
/* 315 */         localSecurityManager.checkPermission(fullScreenExclusivePermission);
/*     */       } catch (SecurityException localSecurityException) {
/* 317 */         return false;
/*     */       }
/*     */     }
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isFullScreenSupported()
/*     */   {
/* 328 */     return isFSExclusiveModeAllowed();
/*     */   }
/*     */ 
/*     */   public synchronized void setFullScreenWindow(Window paramWindow)
/*     */   {
/* 333 */     Window localWindow = getFullScreenWindow();
/* 334 */     if (paramWindow == localWindow) {
/* 335 */       return;
/*     */     }
/* 337 */     if (!isFullScreenSupported()) {
/* 338 */       super.setFullScreenWindow(paramWindow);
/*     */       return;
/*     */     }
/*     */     WWindowPeer localWWindowPeer;
/* 343 */     if (localWindow != null)
/*     */     {
/* 345 */       if (this.defaultDisplayMode != null) {
/* 346 */         setDisplayMode(this.defaultDisplayMode);
/*     */ 
/* 353 */         this.defaultDisplayMode = null;
/*     */       }
/* 355 */       localWWindowPeer = (WWindowPeer)localWindow.getPeer();
/* 356 */       if (localWWindowPeer != null) {
/* 357 */         localWWindowPeer.setFullScreenExclusiveModeState(false);
/*     */ 
/* 361 */         synchronized (localWWindowPeer) {
/* 362 */           exitFullScreenExclusive(this.screen, localWWindowPeer);
/*     */         }
/*     */       }
/* 365 */       removeFSWindowListener(localWindow);
/*     */     }
/* 367 */     super.setFullScreenWindow(paramWindow);
/* 368 */     if (paramWindow != null)
/*     */     {
/* 371 */       this.defaultDisplayMode = getDisplayMode();
/* 372 */       addFSWindowListener(paramWindow);
/*     */ 
/* 374 */       localWWindowPeer = (WWindowPeer)paramWindow.getPeer();
/* 375 */       if (localWWindowPeer != null) {
/* 376 */         synchronized (localWWindowPeer) {
/* 377 */           enterFullScreenExclusive(this.screen, localWWindowPeer);
/*     */         }
/*     */ 
/* 383 */         localWWindowPeer.setFullScreenExclusiveModeState(true);
/*     */       }
/*     */ 
/* 387 */       localWWindowPeer.updateGC();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected native void enterFullScreenExclusive(int paramInt, WindowPeer paramWindowPeer);
/*     */ 
/*     */   protected native void exitFullScreenExclusive(int paramInt, WindowPeer paramWindowPeer);
/*     */ 
/*     */   public boolean isDisplayChangeSupported()
/*     */   {
/* 403 */     return (isFullScreenSupported()) && (getFullScreenWindow() != null);
/*     */   }
/*     */ 
/*     */   public synchronized void setDisplayMode(DisplayMode paramDisplayMode)
/*     */   {
/* 408 */     if (!isDisplayChangeSupported()) {
/* 409 */       super.setDisplayMode(paramDisplayMode);
/* 410 */       return;
/*     */     }
/* 412 */     if ((paramDisplayMode == null) || ((paramDisplayMode = getMatchingDisplayMode(paramDisplayMode)) == null)) {
/* 413 */       throw new IllegalArgumentException("Invalid display mode");
/*     */     }
/* 415 */     if (getDisplayMode().equals(paramDisplayMode)) {
/* 416 */       return;
/*     */     }
/* 418 */     Window localWindow = getFullScreenWindow();
/* 419 */     if (localWindow != null) {
/* 420 */       WWindowPeer localWWindowPeer = (WWindowPeer)localWindow.getPeer();
/* 421 */       configDisplayMode(this.screen, localWWindowPeer, paramDisplayMode.getWidth(), paramDisplayMode.getHeight(), paramDisplayMode.getBitDepth(), paramDisplayMode.getRefreshRate());
/*     */ 
/* 425 */       Rectangle localRectangle = getDefaultConfiguration().getBounds();
/* 426 */       localWindow.setBounds(localRectangle.x, localRectangle.y, paramDisplayMode.getWidth(), paramDisplayMode.getHeight());
/*     */     }
/*     */     else
/*     */     {
/* 431 */       throw new IllegalStateException("Must be in fullscreen mode in order to set display mode");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected native DisplayMode getCurrentDisplayMode(int paramInt);
/*     */ 
/*     */   protected native void configDisplayMode(int paramInt1, WindowPeer paramWindowPeer, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */ 
/*     */   protected native void enumDisplayModes(int paramInt, ArrayList paramArrayList);
/*     */ 
/*     */   public synchronized DisplayMode getDisplayMode()
/*     */   {
/* 444 */     DisplayMode localDisplayMode = getCurrentDisplayMode(this.screen);
/* 445 */     return localDisplayMode;
/*     */   }
/*     */ 
/*     */   public synchronized DisplayMode[] getDisplayModes()
/*     */   {
/* 450 */     ArrayList localArrayList = new ArrayList();
/* 451 */     enumDisplayModes(this.screen, localArrayList);
/* 452 */     int i = localArrayList.size();
/* 453 */     DisplayMode[] arrayOfDisplayMode = new DisplayMode[i];
/* 454 */     for (int j = 0; j < i; j++) {
/* 455 */       arrayOfDisplayMode[j] = ((DisplayMode)localArrayList.get(j));
/*     */     }
/* 457 */     return arrayOfDisplayMode;
/*     */   }
/*     */ 
/*     */   protected synchronized DisplayMode getMatchingDisplayMode(DisplayMode paramDisplayMode) {
/* 461 */     if (!isDisplayChangeSupported()) {
/* 462 */       return null;
/*     */     }
/* 464 */     DisplayMode[] arrayOfDisplayMode1 = getDisplayModes();
/* 465 */     for (DisplayMode localDisplayMode : arrayOfDisplayMode1) {
/* 466 */       if ((paramDisplayMode.equals(localDisplayMode)) || ((paramDisplayMode.getRefreshRate() == 0) && (paramDisplayMode.getWidth() == localDisplayMode.getWidth()) && (paramDisplayMode.getHeight() == localDisplayMode.getHeight()) && (paramDisplayMode.getBitDepth() == localDisplayMode.getBitDepth())))
/*     */       {
/* 472 */         return localDisplayMode;
/*     */       }
/*     */     }
/* 475 */     return null;
/*     */   }
/*     */ 
/*     */   public void displayChanged()
/*     */   {
/* 484 */     this.dynamicColorModel = null;
/* 485 */     this.defaultConfig = null;
/* 486 */     this.configs = null;
/*     */ 
/* 488 */     this.topLevels.notifyListeners();
/*     */   }
/*     */ 
/*     */   public void paletteChanged()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addDisplayChangedListener(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/* 504 */     this.topLevels.add(paramDisplayChangedListener);
/*     */   }
/*     */ 
/*     */   public void removeDisplayChangedListener(DisplayChangedListener paramDisplayChangedListener)
/*     */   {
/* 511 */     this.topLevels.remove(paramDisplayChangedListener);
/*     */   }
/*     */ 
/*     */   private native ColorModel makeColorModel(int paramInt, boolean paramBoolean);
/*     */ 
/*     */   public ColorModel getDynamicColorModel()
/*     */   {
/* 525 */     if (this.dynamicColorModel == null) {
/* 526 */       this.dynamicColorModel = makeColorModel(this.screen, true);
/*     */     }
/* 528 */     return this.dynamicColorModel;
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/* 535 */     if (this.colorModel == null) {
/* 536 */       this.colorModel = makeColorModel(this.screen, false);
/*     */     }
/* 538 */     return this.colorModel;
/*     */   }
/*     */ 
/*     */   protected void addFSWindowListener(final Window paramWindow)
/*     */   {
/* 617 */     this.fsWindowListener = new Win32FSWindowAdapter(this);
/*     */ 
/* 621 */     EventQueue.invokeLater(new Runnable() {
/*     */       public void run() {
/* 623 */         paramWindow.addWindowListener(Win32GraphicsDevice.this.fsWindowListener);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected void removeFSWindowListener(Window paramWindow)
/*     */   {
/* 634 */     paramWindow.removeWindowListener(this.fsWindowListener);
/* 635 */     this.fsWindowListener = null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  91 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.awt.nopixfmt"));
/*     */ 
/*  93 */     pfDisabled = str != null;
/*  94 */     initIDs();
/*     */   }
/*     */ 
/*     */   private static class Win32FSWindowAdapter extends WindowAdapter
/*     */   {
/*     */     private Win32GraphicsDevice device;
/*     */     private DisplayMode dm;
/*     */ 
/*     */     Win32FSWindowAdapter(Win32GraphicsDevice paramWin32GraphicsDevice)
/*     */     {
/* 553 */       this.device = paramWin32GraphicsDevice;
/*     */     }
/*     */ 
/*     */     private void setFSWindowsState(Window paramWindow, int paramInt) {
/* 557 */       GraphicsDevice[] arrayOfGraphicsDevice1 = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
/*     */       GraphicsDevice localGraphicsDevice;
/* 562 */       if (paramWindow != null) {
/* 563 */         for (localGraphicsDevice : arrayOfGraphicsDevice1) {
/* 564 */           if (paramWindow == localGraphicsDevice.getFullScreenWindow()) {
/* 565 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 570 */       for (localGraphicsDevice : arrayOfGraphicsDevice1) {
/* 571 */         Window localWindow = localGraphicsDevice.getFullScreenWindow();
/* 572 */         if ((localWindow instanceof Frame))
/* 573 */           ((Frame)localWindow).setExtendedState(paramInt);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void windowDeactivated(WindowEvent paramWindowEvent)
/*     */     {
/* 580 */       setFSWindowsState(paramWindowEvent.getOppositeWindow(), 1);
/*     */     }
/*     */ 
/*     */     public void windowActivated(WindowEvent paramWindowEvent)
/*     */     {
/* 585 */       setFSWindowsState(paramWindowEvent.getOppositeWindow(), 0);
/*     */     }
/*     */ 
/*     */     public void windowIconified(WindowEvent paramWindowEvent)
/*     */     {
/* 591 */       DisplayMode localDisplayMode = this.device.defaultDisplayMode;
/* 592 */       if (localDisplayMode != null) {
/* 593 */         this.dm = this.device.getDisplayMode();
/* 594 */         this.device.setDisplayMode(localDisplayMode);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void windowDeiconified(WindowEvent paramWindowEvent)
/*     */     {
/* 601 */       if (this.dm != null) {
/* 602 */         this.device.setDisplayMode(this.dm);
/* 603 */         this.dm = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.Win32GraphicsDevice
 * JD-Core Version:    0.6.2
 */