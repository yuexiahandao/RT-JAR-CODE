/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.AWTEventMulticaster;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Window;
/*     */ import java.awt.Window.Type;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.peer.WindowPeer;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.AWTAccessor.WindowAccessor;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.CausedFocusEvent.Cause;
/*     */ import sun.awt.DisplayChangedListener;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.Win32GraphicsConfig;
/*     */ import sun.awt.Win32GraphicsDevice;
/*     */ import sun.awt.Win32GraphicsEnvironment;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class WWindowPeer extends WPanelPeer
/*     */   implements WindowPeer, DisplayChangedListener
/*     */ {
/*  46 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WWindowPeer");
/*  47 */   private static final PlatformLogger screenLog = PlatformLogger.getLogger("sun.awt.windows.screen.WWindowPeer");
/*     */ 
/*  51 */   private WWindowPeer modalBlocker = null;
/*     */   private boolean isOpaque;
/*     */   private TranslucentWindowPainter painter;
/*  62 */   private static final StringBuffer ACTIVE_WINDOWS_KEY = new StringBuffer("active_windows_list");
/*     */ 
/*  69 */   private static PropertyChangeListener activeWindowListener = new ActiveWindowListener(null);
/*     */ 
/*  75 */   private static final PropertyChangeListener guiDisposedListener = new GuiDisposedListener(null);
/*     */   private WindowListener windowListener;
/* 205 */   private volatile Window.Type windowType = Window.Type.NORMAL;
/*     */ 
/* 549 */   private volatile int sysX = 0;
/* 550 */   private volatile int sysY = 0;
/* 551 */   private volatile int sysW = 0;
/* 552 */   private volatile int sysH = 0;
/*     */ 
/* 606 */   private float opacity = 1.0F;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   protected void disposeImpl()
/*     */   {
/*  95 */     AppContext localAppContext = SunToolkit.targetToAppContext(this.target);
/*  96 */     synchronized (localAppContext) {
/*  97 */       List localList = (List)localAppContext.get(ACTIVE_WINDOWS_KEY);
/*  98 */       if (localList != null) {
/*  99 */         localList.remove(this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 104 */     ??? = getGraphicsConfiguration();
/* 105 */     ((Win32GraphicsDevice)((GraphicsConfiguration)???).getDevice()).removeDisplayChangedListener(this);
/*     */ 
/* 107 */     synchronized (getStateLock()) {
/* 108 */       TranslucentWindowPainter localTranslucentWindowPainter = this.painter;
/* 109 */       if (localTranslucentWindowPainter != null) {
/* 110 */         localTranslucentWindowPainter.flush();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 116 */     super.disposeImpl();
/*     */   }
/*     */ 
/*     */   public void toFront()
/*     */   {
/* 122 */     updateFocusableWindowState();
/* 123 */     _toFront(); } 
/*     */   native void _toFront();
/*     */ 
/*     */   public native void toBack();
/*     */ 
/*     */   public native void setAlwaysOnTopNative(boolean paramBoolean);
/*     */ 
/* 130 */   public void setAlwaysOnTop(boolean paramBoolean) { if (((paramBoolean) && (((Window)this.target).isVisible())) || (!paramBoolean))
/* 131 */       setAlwaysOnTopNative(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void updateAlwaysOnTopState()
/*     */   {
/* 136 */     setAlwaysOnTop(((Window)this.target).isAlwaysOnTop());
/*     */   }
/*     */ 
/*     */   public void updateFocusableWindowState() {
/* 140 */     setFocusableWindow(((Window)this.target).isFocusableWindow());
/*     */   }
/*     */ 
/*     */   native void setFocusableWindow(boolean paramBoolean);
/*     */ 
/*     */   public void setTitle(String paramString)
/*     */   {
/* 148 */     if (paramString == null) {
/* 149 */       paramString = "";
/*     */     }
/* 151 */     _setTitle(paramString);
/*     */   }
/*     */   native void _setTitle(String paramString);
/*     */ 
/*     */   public void setResizable(boolean paramBoolean) {
/* 156 */     _setResizable(paramBoolean);
/*     */   }
/*     */ 
/*     */   public native void _setResizable(boolean paramBoolean);
/*     */ 
/*     */   WWindowPeer(Window paramWindow)
/*     */   {
/* 163 */     super(paramWindow);
/*     */   }
/*     */ 
/*     */   void initialize() {
/* 167 */     super.initialize();
/*     */ 
/* 169 */     updateInsets(this.insets_);
/*     */ 
/* 171 */     Font localFont = ((Window)this.target).getFont();
/* 172 */     if (localFont == null) {
/* 173 */       localFont = defaultFont;
/* 174 */       ((Window)this.target).setFont(localFont);
/* 175 */       setFont(localFont);
/*     */     }
/*     */ 
/* 178 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
/* 179 */     ((Win32GraphicsDevice)localGraphicsConfiguration.getDevice()).addDisplayChangedListener(this);
/*     */ 
/* 181 */     initActiveWindowsTracking((Window)this.target);
/*     */ 
/* 183 */     updateIconImages();
/*     */ 
/* 185 */     Shape localShape = ((Window)this.target).getShape();
/* 186 */     if (localShape != null) {
/* 187 */       applyShape(Region.getInstance(localShape, null));
/*     */     }
/*     */ 
/* 190 */     float f = ((Window)this.target).getOpacity();
/* 191 */     if (f < 1.0F) {
/* 192 */       setOpacity(f);
/*     */     }
/*     */ 
/* 195 */     synchronized (getStateLock())
/*     */     {
/* 198 */       this.isOpaque = true;
/* 199 */       setOpaque(((Window)this.target).isOpaque());
/*     */     }
/*     */   }
/*     */ 
/*     */   native void createAwtWindow(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void preCreate(WComponentPeer paramWComponentPeer)
/*     */   {
/* 210 */     this.windowType = ((Window)this.target).getType();
/*     */   }
/*     */ 
/*     */   void create(WComponentPeer paramWComponentPeer) {
/* 214 */     preCreate(paramWComponentPeer);
/* 215 */     createAwtWindow(paramWComponentPeer);
/*     */   }
/*     */ 
/*     */   protected void realShow()
/*     */   {
/* 220 */     super.show();
/*     */   }
/*     */ 
/*     */   public void show() {
/* 224 */     updateFocusableWindowState();
/*     */ 
/* 226 */     boolean bool = ((Window)this.target).isAlwaysOnTop();
/*     */ 
/* 235 */     updateGC();
/*     */ 
/* 237 */     realShow();
/* 238 */     updateMinimumSize();
/*     */ 
/* 240 */     if ((((Window)this.target).isAlwaysOnTopSupported()) && (bool)) {
/* 241 */       setAlwaysOnTop(bool);
/*     */     }
/*     */ 
/* 244 */     synchronized (getStateLock()) {
/* 245 */       if (!this.isOpaque)
/* 246 */         updateWindow(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void updateInsets(Insets paramInsets);
/*     */ 
/*     */   static native int getSysMinWidth();
/*     */ 
/*     */   static native int getSysMinHeight();
/*     */ 
/*     */   static native int getSysIconWidth();
/*     */ 
/*     */   static native int getSysIconHeight();
/*     */ 
/*     */   static native int getSysSmIconWidth();
/*     */ 
/*     */   static native int getSysSmIconHeight();
/*     */ 
/*     */   native void setIconImagesData(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   synchronized native void reshapeFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public boolean requestWindowFocus(CausedFocusEvent.Cause paramCause)
/*     */   {
/* 274 */     if (!focusAllowedFor()) {
/* 275 */       return false;
/*     */     }
/* 277 */     return requestWindowFocus(paramCause == CausedFocusEvent.Cause.MOUSE_EVENT);
/*     */   }
/*     */   public native boolean requestWindowFocus(boolean paramBoolean);
/*     */ 
/*     */   public boolean focusAllowedFor() {
/* 282 */     Window localWindow = (Window)this.target;
/* 283 */     if ((!localWindow.isVisible()) || (!localWindow.isEnabled()) || (!localWindow.isFocusableWindow()))
/*     */     {
/* 287 */       return false;
/*     */     }
/* 289 */     if (isModalBlocked()) {
/* 290 */       return false;
/*     */     }
/* 292 */     return true;
/*     */   }
/*     */ 
/*     */   public void hide() {
/* 296 */     WindowListener localWindowListener = this.windowListener;
/* 297 */     if (localWindowListener != null)
/*     */     {
/* 300 */       localWindowListener.windowClosing(new WindowEvent((Window)this.target, 201));
/*     */     }
/* 302 */     super.hide();
/*     */   }
/*     */ 
/*     */   void preprocessPostEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 307 */     if ((paramAWTEvent instanceof WindowEvent)) {
/* 308 */       WindowListener localWindowListener = this.windowListener;
/* 309 */       if (localWindowListener != null)
/* 310 */         switch (paramAWTEvent.getID()) {
/*     */         case 201:
/* 312 */           localWindowListener.windowClosing((WindowEvent)paramAWTEvent);
/* 313 */           break;
/*     */         case 203:
/* 315 */           localWindowListener.windowIconified((WindowEvent)paramAWTEvent);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void addWindowListener(WindowListener paramWindowListener)
/*     */   {
/* 323 */     this.windowListener = AWTEventMulticaster.add(this.windowListener, paramWindowListener);
/*     */   }
/*     */ 
/*     */   synchronized void removeWindowListener(WindowListener paramWindowListener) {
/* 327 */     this.windowListener = AWTEventMulticaster.remove(this.windowListener, paramWindowListener);
/*     */   }
/*     */ 
/*     */   public void updateMinimumSize() {
/* 331 */     Dimension localDimension = null;
/* 332 */     if (((Component)this.target).isMinimumSizeSet()) {
/* 333 */       localDimension = ((Component)this.target).getMinimumSize();
/*     */     }
/* 335 */     if (localDimension != null) {
/* 336 */       int i = getSysMinWidth();
/* 337 */       int j = getSysMinHeight();
/* 338 */       int k = localDimension.width >= i ? localDimension.width : i;
/* 339 */       int m = localDimension.height >= j ? localDimension.height : j;
/* 340 */       setMinSize(k, m);
/*     */     } else {
/* 342 */       setMinSize(0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateIconImages() {
/* 347 */     List localList = ((Window)this.target).getIconImages();
/* 348 */     if ((localList == null) || (localList.size() == 0)) {
/* 349 */       setIconImagesData(null, 0, 0, null, 0, 0);
/*     */     } else {
/* 351 */       int i = getSysIconWidth();
/* 352 */       int j = getSysIconHeight();
/* 353 */       int k = getSysSmIconWidth();
/* 354 */       int m = getSysSmIconHeight();
/* 355 */       DataBufferInt localDataBufferInt1 = SunToolkit.getScaledIconData(localList, i, j);
/*     */ 
/* 357 */       DataBufferInt localDataBufferInt2 = SunToolkit.getScaledIconData(localList, k, m);
/*     */ 
/* 359 */       if ((localDataBufferInt1 != null) && (localDataBufferInt2 != null)) {
/* 360 */         setIconImagesData(localDataBufferInt1.getData(), i, j, localDataBufferInt2.getData(), k, m);
/*     */       }
/*     */       else
/* 363 */         setIconImagesData(null, 0, 0, null, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void setMinSize(int paramInt1, int paramInt2);
/*     */ 
/*     */   public boolean isModalBlocked()
/*     */   {
/* 380 */     return this.modalBlocker != null;
/*     */   }
/*     */ 
/*     */   public void setModalBlocked(Dialog paramDialog, boolean paramBoolean) {
/* 384 */     synchronized (((Component)getTarget()).getTreeLock())
/*     */     {
/* 387 */       WWindowPeer localWWindowPeer = (WWindowPeer)paramDialog.getPeer();
/* 388 */       if (paramBoolean)
/*     */       {
/* 390 */         this.modalBlocker = localWWindowPeer;
/*     */ 
/* 394 */         if ((localWWindowPeer instanceof WFileDialogPeer))
/* 395 */           ((WFileDialogPeer)localWWindowPeer).blockWindow(this);
/* 396 */         else if ((localWWindowPeer instanceof WPrintDialogPeer))
/* 397 */           ((WPrintDialogPeer)localWWindowPeer).blockWindow(this);
/*     */         else
/* 399 */           modalDisable(paramDialog, localWWindowPeer.getHWnd());
/*     */       }
/*     */       else {
/* 402 */         this.modalBlocker = null;
/* 403 */         if ((localWWindowPeer instanceof WFileDialogPeer))
/* 404 */           ((WFileDialogPeer)localWWindowPeer).unblockWindow(this);
/* 405 */         else if ((localWWindowPeer instanceof WPrintDialogPeer))
/* 406 */           ((WPrintDialogPeer)localWWindowPeer).unblockWindow(this);
/*     */         else
/* 408 */           modalEnable(paramDialog);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   native void modalDisable(Dialog paramDialog, long paramLong);
/*     */ 
/*     */   native void modalEnable(Dialog paramDialog);
/*     */ 
/*     */   public static long[] getActiveWindowHandles()
/*     */   {
/* 423 */     AppContext localAppContext = AppContext.getAppContext();
/* 424 */     if (localAppContext == null) return null;
/* 425 */     synchronized (localAppContext) {
/* 426 */       List localList = (List)localAppContext.get(ACTIVE_WINDOWS_KEY);
/* 427 */       if (localList == null) {
/* 428 */         return null;
/*     */       }
/* 430 */       long[] arrayOfLong = new long[localList.size()];
/* 431 */       for (int i = 0; i < localList.size(); i++) {
/* 432 */         arrayOfLong[i] = ((WWindowPeer)localList.get(i)).getHWnd();
/*     */       }
/* 434 */       return arrayOfLong;
/*     */     }
/*     */   }
/*     */ 
/*     */   void draggedToNewScreen()
/*     */   {
/* 446 */     SunToolkit.executeOnEventHandlerThread((Component)this.target, new Runnable()
/*     */     {
/*     */       public void run() {
/* 449 */         WWindowPeer.this.displayChanged();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void updateGC() {
/* 455 */     int i = getScreenImOn();
/* 456 */     if (screenLog.isLoggable(400)) {
/* 457 */       log.finer("Screen number: " + i);
/*     */     }
/*     */ 
/* 461 */     Win32GraphicsDevice localWin32GraphicsDevice1 = (Win32GraphicsDevice)this.winGraphicsConfig.getDevice();
/*     */ 
/* 465 */     GraphicsDevice[] arrayOfGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
/*     */     Win32GraphicsDevice localWin32GraphicsDevice2;
/* 470 */     if (i >= arrayOfGraphicsDevice.length) {
/* 471 */       localWin32GraphicsDevice2 = (Win32GraphicsDevice)GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
/*     */     }
/*     */     else {
/* 474 */       localWin32GraphicsDevice2 = (Win32GraphicsDevice)arrayOfGraphicsDevice[i];
/*     */     }
/*     */ 
/* 479 */     this.winGraphicsConfig = ((Win32GraphicsConfig)localWin32GraphicsDevice2.getDefaultConfiguration());
/*     */ 
/* 481 */     if ((screenLog.isLoggable(500)) && 
/* 482 */       (this.winGraphicsConfig == null)) {
/* 483 */       screenLog.fine("Assertion (winGraphicsConfig != null) failed");
/*     */     }
/*     */ 
/* 488 */     if (localWin32GraphicsDevice1 != localWin32GraphicsDevice2) {
/* 489 */       localWin32GraphicsDevice1.removeDisplayChangedListener(this);
/* 490 */       localWin32GraphicsDevice2.addDisplayChangedListener(this);
/*     */     }
/*     */ 
/* 493 */     AWTAccessor.getComponentAccessor().setGraphicsConfiguration((Component)this.target, this.winGraphicsConfig);
/*     */   }
/*     */ 
/*     */   public void displayChanged()
/*     */   {
/* 509 */     updateGC();
/*     */   }
/*     */ 
/*     */   public void paletteChanged()
/*     */   {
/*     */   }
/*     */ 
/*     */   private native int getScreenImOn();
/*     */ 
/*     */   public final native void setFullScreenExclusiveModeState(boolean paramBoolean);
/*     */ 
/*     */   public void grab()
/*     */   {
/* 529 */     nativeGrab();
/*     */   }
/*     */ 
/*     */   public void ungrab() {
/* 533 */     nativeUngrab();
/*     */   }
/*     */   private native void nativeGrab();
/*     */ 
/*     */   private native void nativeUngrab();
/*     */ 
/* 539 */   private final boolean hasWarningWindow() { return ((Window)this.target).getWarningString() != null; }
/*     */ 
/*     */   boolean isTargetUndecorated()
/*     */   {
/* 543 */     return true;
/*     */   }
/*     */ 
/*     */   public native void repositionSecurityWarning();
/*     */ 
/*     */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 558 */     this.sysX = paramInt1;
/* 559 */     this.sysY = paramInt2;
/* 560 */     this.sysW = paramInt3;
/* 561 */     this.sysH = paramInt4;
/*     */ 
/* 563 */     super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*     */   }
/*     */ 
/*     */   public void print(Graphics paramGraphics)
/*     */   {
/* 570 */     Shape localShape = AWTAccessor.getWindowAccessor().getShape((Window)this.target);
/* 571 */     if (localShape != null) {
/* 572 */       paramGraphics.setClip(localShape);
/*     */     }
/* 574 */     super.print(paramGraphics);
/*     */   }
/*     */ 
/*     */   private void replaceSurfaceDataRecursively(Component paramComponent) {
/* 578 */     if ((paramComponent instanceof Container)) {
/* 579 */       for (Component localComponent : ((Container)paramComponent).getComponents()) {
/* 580 */         replaceSurfaceDataRecursively(localComponent);
/*     */       }
/*     */     }
/* 583 */     ??? = paramComponent.getPeer();
/* 584 */     if ((??? instanceof WComponentPeer))
/* 585 */       ((WComponentPeer)???).replaceSurfaceDataLater();
/*     */   }
/*     */ 
/*     */   public final Graphics getTranslucentGraphics()
/*     */   {
/* 590 */     synchronized (getStateLock()) {
/* 591 */       return this.isOpaque ? null : this.painter.getBackBuffer(false).getGraphics();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setBackground(Color paramColor)
/*     */   {
/* 597 */     super.setBackground(paramColor);
/* 598 */     synchronized (getStateLock()) {
/* 599 */       if ((!this.isOpaque) && (((Window)this.target).isVisible()))
/* 600 */         updateWindow(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void setOpacity(int paramInt);
/*     */ 
/*     */   public void setOpacity(float paramFloat)
/*     */   {
/* 609 */     if (!((SunToolkit)((Window)this.target).getToolkit()).isWindowOpacitySupported())
/*     */     {
/* 612 */       return;
/*     */     }
/*     */ 
/* 615 */     if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
/* 616 */       throw new IllegalArgumentException("The value of opacity should be in the range [0.0f .. 1.0f].");
/*     */     }
/*     */ 
/* 620 */     if (((this.opacity == 1.0F) && (paramFloat < 1.0F)) || ((this.opacity < 1.0F) && (paramFloat == 1.0F) && (!Win32GraphicsEnvironment.isVistaOS())))
/*     */     {
/* 626 */       replaceSurfaceDataRecursively((Component)getTarget());
/*     */     }
/*     */ 
/* 629 */     this.opacity = paramFloat;
/*     */ 
/* 632 */     int i = (int)(paramFloat * 255.0F);
/* 633 */     if (i < 0) {
/* 634 */       i = 0;
/*     */     }
/* 636 */     if (i > 255) {
/* 637 */       i = 255;
/*     */     }
/*     */ 
/* 640 */     setOpacity(i);
/*     */ 
/* 642 */     synchronized (getStateLock()) {
/* 643 */       if ((!this.isOpaque) && (((Window)this.target).isVisible()))
/* 644 */         updateWindow(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void setOpaqueImpl(boolean paramBoolean);
/*     */ 
/*     */   public void setOpaque(boolean paramBoolean)
/*     */   {
/* 652 */     synchronized (getStateLock()) {
/* 653 */       if (this.isOpaque == paramBoolean) {
/* 654 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 658 */     ??? = (Window)getTarget();
/*     */ 
/* 660 */     if (!paramBoolean) {
/* 661 */       SunToolkit localSunToolkit = (SunToolkit)((Window)???).getToolkit();
/* 662 */       if ((!localSunToolkit.isWindowTranslucencySupported()) || (!localSunToolkit.isTranslucencyCapable(((Window)???).getGraphicsConfiguration())))
/*     */       {
/* 665 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 669 */     boolean bool = Win32GraphicsEnvironment.isVistaOS();
/*     */ 
/* 671 */     if ((this.isOpaque != paramBoolean) && (!bool))
/*     */     {
/* 674 */       replaceSurfaceDataRecursively((Component)???);
/*     */     }
/*     */ 
/* 677 */     synchronized (getStateLock()) {
/* 678 */       this.isOpaque = paramBoolean;
/* 679 */       setOpaqueImpl(paramBoolean);
/* 680 */       if (paramBoolean) {
/* 681 */         TranslucentWindowPainter localTranslucentWindowPainter = this.painter;
/* 682 */         if (localTranslucentWindowPainter != null) {
/* 683 */           localTranslucentWindowPainter.flush();
/* 684 */           this.painter = null;
/*     */         }
/*     */       } else {
/* 687 */         this.painter = TranslucentWindowPainter.createInstance(this);
/*     */       }
/*     */     }
/*     */ 
/* 691 */     if (bool)
/*     */     {
/* 697 */       ??? = ((Window)???).getShape();
/* 698 */       if (??? != null) {
/* 699 */         ((Window)???).setShape((Shape)???);
/*     */       }
/*     */     }
/*     */ 
/* 703 */     if (((Window)???).isVisible())
/* 704 */       updateWindow(true);
/*     */   }
/*     */ 
/*     */   public native void updateWindowImpl(int[] paramArrayOfInt, int paramInt1, int paramInt2);
/*     */ 
/*     */   public void updateWindow()
/*     */   {
/* 711 */     updateWindow(false);
/*     */   }
/*     */ 
/*     */   private void updateWindow(boolean paramBoolean) {
/* 715 */     Window localWindow = (Window)this.target;
/* 716 */     synchronized (getStateLock()) {
/* 717 */       if ((this.isOpaque) || (!localWindow.isVisible()) || (localWindow.getWidth() <= 0) || (localWindow.getHeight() <= 0))
/*     */       {
/* 720 */         return;
/*     */       }
/* 722 */       TranslucentWindowPainter localTranslucentWindowPainter = this.painter;
/* 723 */       if (localTranslucentWindowPainter != null)
/* 724 */         localTranslucentWindowPainter.updateWindow(paramBoolean);
/* 725 */       else if (log.isLoggable(400))
/* 726 */         log.finer("Translucent window painter is null in updateWindow");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void initActiveWindowsTracking(Window paramWindow)
/*     */   {
/* 737 */     AppContext localAppContext = AppContext.getAppContext();
/* 738 */     synchronized (localAppContext) {
/* 739 */       Object localObject1 = (List)localAppContext.get(ACTIVE_WINDOWS_KEY);
/* 740 */       if (localObject1 == null) {
/* 741 */         localObject1 = new LinkedList();
/* 742 */         localAppContext.put(ACTIVE_WINDOWS_KEY, localObject1);
/* 743 */         localAppContext.addPropertyChangeListener("guidisposed", guiDisposedListener);
/*     */ 
/* 745 */         KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 746 */         localKeyboardFocusManager.addPropertyChangeListener("activeWindow", activeWindowListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  89 */     initIDs();
/*     */   }
/*     */ 
/*     */   private static class ActiveWindowListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 782 */       Window localWindow = (Window)paramPropertyChangeEvent.getNewValue();
/* 783 */       if (localWindow == null) {
/* 784 */         return;
/*     */       }
/* 786 */       AppContext localAppContext = SunToolkit.targetToAppContext(localWindow);
/* 787 */       synchronized (localAppContext) {
/* 788 */         WWindowPeer localWWindowPeer = (WWindowPeer)localWindow.getPeer();
/*     */ 
/* 790 */         List localList = (List)localAppContext.get(WWindowPeer.ACTIVE_WINDOWS_KEY);
/* 791 */         if (localList != null) {
/* 792 */           localList.remove(localWWindowPeer);
/* 793 */           localList.add(localWWindowPeer);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class GuiDisposedListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 758 */       boolean bool = ((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue();
/* 759 */       if ((bool != true) && 
/* 760 */         (WWindowPeer.log.isLoggable(500))) {
/* 761 */         WWindowPeer.log.fine(" Assertion (newValue != true) failed for AppContext.GUI_DISPOSED ");
/*     */       }
/*     */ 
/* 764 */       AppContext localAppContext = AppContext.getAppContext();
/* 765 */       synchronized (localAppContext) {
/* 766 */         localAppContext.remove(WWindowPeer.ACTIVE_WINDOWS_KEY);
/* 767 */         localAppContext.removePropertyChangeListener("guidisposed", this);
/*     */ 
/* 769 */         KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 770 */         localKeyboardFocusManager.removePropertyChangeListener("activeWindow", WWindowPeer.activeWindowListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WWindowPeer
 * JD-Core Version:    0.6.2
 */