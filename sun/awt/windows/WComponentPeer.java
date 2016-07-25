/*      */ package sun.awt.windows;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.AWTException;
/*      */ import java.awt.BufferCapabilities;
/*      */ import java.awt.BufferCapabilities.FlipContents;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.SystemColor;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.dnd.DropTarget;
/*      */ import java.awt.dnd.peer.DropTargetPeer;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.PaintEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.ImageProducer;
/*      */ import java.awt.image.VolatileImage;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.peer.ContainerPeer;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.GlobalCursorManager;
/*      */ import sun.awt.PaintEventDispatcher;
/*      */ import sun.awt.RepaintArea;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.Win32GraphicsConfig;
/*      */ import sun.awt.Win32GraphicsEnvironment;
/*      */ import sun.awt.event.IgnorePaintEvent;
/*      */ import sun.awt.image.SunVolatileImage;
/*      */ import sun.awt.image.ToolkitImage;
/*      */ import sun.java2d.InvalidPipeException;
/*      */ import sun.java2d.ScreenUpdateManager;
/*      */ import sun.java2d.SurfaceData;
/*      */ import sun.java2d.d3d.D3DSurfaceData.D3DWindowSurfaceData;
/*      */ import sun.java2d.opengl.OGLSurfaceData;
/*      */ import sun.java2d.pipe.Region;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class WComponentPeer extends WObjectPeer
/*      */   implements ComponentPeer, DropTargetPeer
/*      */ {
/*      */   protected volatile long hwnd;
/*   72 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WComponentPeer");
/*   73 */   private static final PlatformLogger shapeLog = PlatformLogger.getLogger("sun.awt.windows.shape.WComponentPeer");
/*   74 */   private static final PlatformLogger focusLog = PlatformLogger.getLogger("sun.awt.windows.focus.WComponentPeer");
/*      */   SurfaceData surfaceData;
/*      */   private RepaintArea paintArea;
/*      */   protected Win32GraphicsConfig winGraphicsConfig;
/*   83 */   boolean isLayouting = false;
/*   84 */   boolean paintPending = false;
/*   85 */   int oldWidth = -1;
/*   86 */   int oldHeight = -1;
/*   87 */   private int numBackBuffers = 0;
/*   88 */   private VolatileImage backBuffer = null;
/*   89 */   private BufferCapabilities backBufferCaps = null;
/*      */   private Color foreground;
/*      */   private Color background;
/*      */   private Font font;
/*      */   int nDropTargets;
/*      */   long nativeDropTargetContext;
/*  142 */   public int serialNum = 0;
/*      */   private static final double BANDING_DIVISOR = 4.0D;
/*  555 */   static final Font defaultFont = new Font("Dialog", 0, 12);
/*      */   private int updateX1;
/*      */   private int updateY1;
/*      */   private int updateX2;
/*      */   private int updateY2;
/* 1015 */   private volatile boolean isAccelCapable = true;
/*      */ 
/*      */   public native boolean isObscured();
/*      */ 
/*      */   public boolean canDetermineObscurity()
/*      */   {
/*   98 */     return true;
/*      */   }
/*      */ 
/*      */   public synchronized native void pShow();
/*      */ 
/*      */   public synchronized native void hide();
/*      */ 
/*      */   public synchronized native void enable();
/*      */ 
/*      */   public synchronized native void disable();
/*      */ 
/*      */   public long getHWnd()
/*      */   {
/*  111 */     return this.hwnd;
/*      */   }
/*      */ 
/*      */   public native Point getLocationOnScreen();
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/*  119 */     if (paramBoolean)
/*  120 */       show();
/*      */     else
/*  122 */       hide();
/*      */   }
/*      */ 
/*      */   public void show()
/*      */   {
/*  127 */     Dimension localDimension = ((Component)this.target).getSize();
/*  128 */     this.oldHeight = localDimension.height;
/*  129 */     this.oldWidth = localDimension.width;
/*  130 */     pShow();
/*      */   }
/*      */ 
/*      */   public void setEnabled(boolean paramBoolean)
/*      */   {
/*  135 */     if (paramBoolean)
/*  136 */       enable();
/*      */     else
/*  138 */       disable();
/*      */   }
/*      */ 
/*      */   private native void reshapeNoCheck(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  151 */     this.paintPending = ((paramInt3 != this.oldWidth) || (paramInt4 != this.oldHeight));
/*      */ 
/*  153 */     if ((paramInt5 & 0x4000) != 0)
/*  154 */       reshapeNoCheck(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     else {
/*  156 */       reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*  158 */     if ((paramInt3 != this.oldWidth) || (paramInt4 != this.oldHeight))
/*      */     {
/*      */       try
/*      */       {
/*  162 */         replaceSurfaceData();
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException) {
/*      */       }
/*  166 */       this.oldWidth = paramInt3;
/*  167 */       this.oldHeight = paramInt4;
/*      */     }
/*      */ 
/*  170 */     this.serialNum += 1;
/*      */   }
/*      */ 
/*      */   void dynamicallyLayoutContainer()
/*      */   {
/*  180 */     if (log.isLoggable(500)) {
/*  181 */       localContainer = WToolkit.getNativeContainer((Component)this.target);
/*  182 */       if (localContainer != null) {
/*  183 */         log.fine("Assertion (parent == null) failed");
/*      */       }
/*      */     }
/*  186 */     final Container localContainer = (Container)this.target;
/*      */ 
/*  188 */     WToolkit.executeOnEventHandlerThread(localContainer, new Runnable()
/*      */     {
/*      */       public void run() {
/*  191 */         localContainer.invalidate();
/*  192 */         localContainer.validate();
/*      */ 
/*  194 */         if (((WComponentPeer.this.surfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData)) || ((WComponentPeer.this.surfaceData instanceof OGLSurfaceData)))
/*      */         {
/*      */           try
/*      */           {
/*  202 */             WComponentPeer.this.replaceSurfaceData();
/*      */           }
/*      */           catch (InvalidPipeException localInvalidPipeException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   void paintDamagedAreaImmediately()
/*      */   {
/*  222 */     updateWindow();
/*      */ 
/*  225 */     WToolkit.getWToolkit(); WToolkit.flushPendingEvents();
/*      */ 
/*  227 */     this.paintArea.paint(this.target, shouldClearRectBeforePaint());
/*      */   }
/*      */ 
/*      */   synchronized native void updateWindow();
/*      */ 
/*      */   public void paint(Graphics paramGraphics) {
/*  233 */     ((Component)this.target).paint(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */   }
/*      */ 
/*      */   private native int[] createPrintedPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*      */ 
/*      */   public void print(Graphics paramGraphics)
/*      */   {
/*  245 */     Component localComponent = (Component)this.target;
/*      */ 
/*  249 */     int i = localComponent.getWidth();
/*  250 */     int j = localComponent.getHeight();
/*      */ 
/*  252 */     int k = (int)(j / 4.0D);
/*  253 */     if (k == 0) {
/*  254 */       k = j;
/*      */     }
/*      */ 
/*  257 */     for (int m = 0; m < j; m += k) {
/*  258 */       int n = m + k - 1;
/*  259 */       if (n >= j) {
/*  260 */         n = j - 1;
/*      */       }
/*  262 */       int i1 = n - m + 1;
/*      */ 
/*  264 */       Color localColor = localComponent.getBackground();
/*  265 */       int[] arrayOfInt = createPrintedPixels(0, m, i, i1, localColor == null ? 255 : localColor.getAlpha());
/*      */ 
/*  267 */       if (arrayOfInt != null) {
/*  268 */         BufferedImage localBufferedImage = new BufferedImage(i, i1, 2);
/*      */ 
/*  270 */         localBufferedImage.setRGB(0, 0, i, i1, arrayOfInt, 0, i);
/*  271 */         paramGraphics.drawImage(localBufferedImage, 0, m, null);
/*  272 */         localBufferedImage.flush();
/*      */       }
/*      */     }
/*      */ 
/*  276 */     localComponent.print(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void coalescePaintEvent(PaintEvent paramPaintEvent) {
/*  280 */     Rectangle localRectangle = paramPaintEvent.getUpdateRect();
/*  281 */     if (!(paramPaintEvent instanceof IgnorePaintEvent)) {
/*  282 */       this.paintArea.add(localRectangle, paramPaintEvent.getID());
/*      */     }
/*      */ 
/*  285 */     if (log.isLoggable(300))
/*  286 */       switch (paramPaintEvent.getID()) {
/*      */       case 801:
/*  288 */         log.finest("coalescePaintEvent: UPDATE: add: x = " + localRectangle.x + ", y = " + localRectangle.y + ", width = " + localRectangle.width + ", height = " + localRectangle.height);
/*      */ 
/*  290 */         return;
/*      */       case 800:
/*  292 */         log.finest("coalescePaintEvent: PAINT: add: x = " + localRectangle.x + ", y = " + localRectangle.y + ", width = " + localRectangle.width + ", height = " + localRectangle.height);
/*      */ 
/*  294 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   public synchronized native void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public boolean handleJavaKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*  304 */     return false;
/*      */   }
/*      */   public void handleJavaMouseEvent(MouseEvent paramMouseEvent) {
/*  307 */     switch (paramMouseEvent.getID())
/*      */     {
/*      */     case 501:
/*  310 */       if ((this.target == paramMouseEvent.getSource()) && (!((Component)this.target).isFocusOwner()) && (WKeyboardFocusManagerPeer.shouldFocusOnClick((Component)this.target)))
/*      */       {
/*  314 */         WKeyboardFocusManagerPeer.requestFocusFor((Component)this.target, CausedFocusEvent.Cause.MOUSE_EVENT);
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   native void nativeHandleEvent(AWTEvent paramAWTEvent);
/*      */ 
/*      */   public void handleEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  324 */     int i = paramAWTEvent.getID();
/*      */ 
/*  326 */     if (((paramAWTEvent instanceof InputEvent)) && (!((InputEvent)paramAWTEvent).isConsumed()) && (((Component)this.target).isEnabled()))
/*      */     {
/*  329 */       if (((paramAWTEvent instanceof MouseEvent)) && (!(paramAWTEvent instanceof MouseWheelEvent)))
/*  330 */         handleJavaMouseEvent((MouseEvent)paramAWTEvent);
/*  331 */       else if (((paramAWTEvent instanceof KeyEvent)) && 
/*  332 */         (handleJavaKeyEvent((KeyEvent)paramAWTEvent))) {
/*  333 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  338 */     switch (i)
/*      */     {
/*      */     case 800:
/*  341 */       this.paintPending = false;
/*      */     case 801:
/*  346 */       if ((!this.isLayouting) && (!this.paintPending)) {
/*  347 */         this.paintArea.paint(this.target, shouldClearRectBeforePaint());
/*      */       }
/*  349 */       return;
/*      */     case 1004:
/*      */     case 1005:
/*  352 */       handleJavaFocusEvent((FocusEvent)paramAWTEvent);
/*      */     }
/*      */ 
/*  358 */     nativeHandleEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   void handleJavaFocusEvent(FocusEvent paramFocusEvent) {
/*  362 */     if (focusLog.isLoggable(400)) focusLog.finer(paramFocusEvent.toString());
/*  363 */     setFocus(paramFocusEvent.getID() == 1004);
/*      */   }
/*      */ 
/*      */   native void setFocus(boolean paramBoolean);
/*      */ 
/*      */   public Dimension getMinimumSize() {
/*  369 */     return ((Component)this.target).getSize();
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize() {
/*  373 */     return getMinimumSize();
/*      */   }
/*      */ 
/*      */   public void layout() {
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds() {
/*  380 */     return ((Component)this.target).getBounds();
/*      */   }
/*      */ 
/*      */   public boolean isFocusable() {
/*  384 */     return false;
/*      */   }
/*      */ 
/*      */   public GraphicsConfiguration getGraphicsConfiguration()
/*      */   {
/*  392 */     if (this.winGraphicsConfig != null) {
/*  393 */       return this.winGraphicsConfig;
/*      */     }
/*      */ 
/*  398 */     return ((Component)this.target).getGraphicsConfiguration();
/*      */   }
/*      */ 
/*      */   public SurfaceData getSurfaceData()
/*      */   {
/*  403 */     return this.surfaceData;
/*      */   }
/*      */ 
/*      */   public void replaceSurfaceData()
/*      */   {
/*  416 */     replaceSurfaceData(this.numBackBuffers, this.backBufferCaps);
/*      */   }
/*      */ 
/*      */   public void createScreenSurface(boolean paramBoolean)
/*      */   {
/*  421 */     Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*  422 */     if (localWin32GraphicsConfig == null) {
/*  423 */       this.surfaceData = null;
/*  424 */       return;
/*      */     }
/*      */ 
/*  427 */     ScreenUpdateManager localScreenUpdateManager = ScreenUpdateManager.getInstance();
/*  428 */     this.surfaceData = localScreenUpdateManager.createScreenSurface(localWin32GraphicsConfig, this, this.numBackBuffers, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void replaceSurfaceData(int paramInt, BufferCapabilities paramBufferCapabilities)
/*      */   {
/*  441 */     SurfaceData localSurfaceData = null;
/*  442 */     VolatileImage localVolatileImage = null;
/*  443 */     synchronized (((Component)this.target).getTreeLock()) {
/*  444 */       synchronized (this) {
/*  445 */         if (this.pData == 0L) {
/*  446 */           return;
/*      */         }
/*  448 */         this.numBackBuffers = paramInt;
/*  449 */         ScreenUpdateManager localScreenUpdateManager = ScreenUpdateManager.getInstance();
/*  450 */         localSurfaceData = this.surfaceData;
/*  451 */         localScreenUpdateManager.dropScreenSurface(localSurfaceData);
/*  452 */         createScreenSurface(true);
/*  453 */         if (localSurfaceData != null) {
/*  454 */           localSurfaceData.invalidate();
/*      */         }
/*      */ 
/*  457 */         localVolatileImage = this.backBuffer;
/*  458 */         if (this.numBackBuffers > 0)
/*      */         {
/*  460 */           this.backBufferCaps = paramBufferCapabilities;
/*  461 */           Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*      */ 
/*  463 */           this.backBuffer = localWin32GraphicsConfig.createBackBuffer(this);
/*  464 */         } else if (this.backBuffer != null) {
/*  465 */           this.backBufferCaps = null;
/*  466 */           this.backBuffer = null;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  472 */     if (localSurfaceData != null) {
/*  473 */       localSurfaceData.flush();
/*      */ 
/*  475 */       localSurfaceData = null;
/*      */     }
/*  477 */     if (localVolatileImage != null) {
/*  478 */       localVolatileImage.flush();
/*      */ 
/*  480 */       localSurfaceData = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void replaceSurfaceDataLater() {
/*  485 */     Runnable local2 = new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*  490 */         if (!WComponentPeer.this.isDisposed())
/*      */           try {
/*  492 */             WComponentPeer.this.replaceSurfaceData();
/*      */           }
/*      */           catch (InvalidPipeException localInvalidPipeException)
/*      */           {
/*      */           }
/*      */       }
/*      */     };
/*  499 */     Component localComponent = (Component)this.target;
/*      */ 
/*  501 */     if (!PaintEventDispatcher.getPaintEventDispatcher().queueSurfaceDataReplacing(localComponent, local2))
/*  502 */       postEvent(new InvocationEvent(localComponent, local2));
/*      */   }
/*      */ 
/*      */   public boolean updateGraphicsData(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  507 */     this.winGraphicsConfig = ((Win32GraphicsConfig)paramGraphicsConfiguration);
/*      */     try {
/*  509 */       replaceSurfaceData();
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException) {
/*      */     }
/*  513 */     return false;
/*      */   }
/*      */ 
/*      */   public ColorModel getColorModel()
/*      */   {
/*  518 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
/*  519 */     if (localGraphicsConfiguration != null) {
/*  520 */       return localGraphicsConfiguration.getColorModel();
/*      */     }
/*      */ 
/*  523 */     return null;
/*      */   }
/*      */ 
/*      */   public ColorModel getDeviceColorModel()
/*      */   {
/*  529 */     Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*      */ 
/*  531 */     if (localWin32GraphicsConfig != null) {
/*  532 */       return localWin32GraphicsConfig.getDeviceColorModel();
/*      */     }
/*      */ 
/*  535 */     return null;
/*      */   }
/*      */ 
/*      */   public ColorModel getColorModel(int paramInt)
/*      */   {
/*  542 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
/*  543 */     if (localGraphicsConfiguration != null) {
/*  544 */       return localGraphicsConfiguration.getColorModel(paramInt);
/*      */     }
/*      */ 
/*  547 */     return null;
/*      */   }
/*      */ 
/*      */   public Toolkit getToolkit() {
/*  551 */     return Toolkit.getDefaultToolkit();
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/*  558 */     if (isDisposed()) {
/*  559 */       return null;
/*      */     }
/*      */ 
/*  562 */     Component localComponent = (Component)getTarget();
/*  563 */     Window localWindow = SunToolkit.getContainingWindow(localComponent);
/*      */     Object localObject4;
/*  564 */     if (localWindow != null) {
/*  565 */       localObject1 = ((WWindowPeer)localWindow.getPeer()).getTranslucentGraphics();
/*      */ 
/*  568 */       if (localObject1 != null)
/*      */       {
/*  573 */         int i = 0; int j = 0;
/*  574 */         for (localObject4 = localComponent; localObject4 != localWindow; localObject4 = ((Component)localObject4).getParent()) {
/*  575 */           i += ((Component)localObject4).getX();
/*  576 */           j += ((Component)localObject4).getY();
/*      */         }
/*      */ 
/*  579 */         ((Graphics)localObject1).translate(i, j);
/*  580 */         ((Graphics)localObject1).clipRect(0, 0, localComponent.getWidth(), localComponent.getHeight());
/*      */ 
/*  582 */         return localObject1;
/*      */       }
/*      */     }
/*      */ 
/*  586 */     Object localObject1 = this.surfaceData;
/*  587 */     if (localObject1 != null)
/*      */     {
/*  589 */       Object localObject2 = this.background;
/*  590 */       if (localObject2 == null) {
/*  591 */         localObject2 = SystemColor.window;
/*      */       }
/*  593 */       Object localObject3 = this.foreground;
/*  594 */       if (localObject3 == null) {
/*  595 */         localObject3 = SystemColor.windowText;
/*      */       }
/*  597 */       localObject4 = this.font;
/*  598 */       if (localObject4 == null) {
/*  599 */         localObject4 = defaultFont;
/*      */       }
/*  601 */       ScreenUpdateManager localScreenUpdateManager = ScreenUpdateManager.getInstance();
/*      */ 
/*  603 */       return localScreenUpdateManager.createGraphics((SurfaceData)localObject1, this, (Color)localObject3, (Color)localObject2, (Font)localObject4);
/*      */     }
/*      */ 
/*  606 */     return null;
/*      */   }
/*      */   public FontMetrics getFontMetrics(Font paramFont) {
/*  609 */     return WFontMetrics.getFontMetrics(paramFont);
/*      */   }
/*      */   private synchronized native void _dispose();
/*      */ 
/*      */   protected void disposeImpl() {
/*  614 */     SurfaceData localSurfaceData = this.surfaceData;
/*  615 */     this.surfaceData = null;
/*  616 */     ScreenUpdateManager.getInstance().dropScreenSurface(localSurfaceData);
/*  617 */     localSurfaceData.invalidate();
/*      */ 
/*  619 */     WToolkit.targetDisposedPeer(this.target, this);
/*  620 */     _dispose();
/*      */   }
/*      */ 
/*      */   public void disposeLater() {
/*  624 */     postEvent(new InvocationEvent(this.target, new Runnable() {
/*      */       public void run() {
/*  626 */         WComponentPeer.this.dispose();
/*      */       }
/*      */     }));
/*      */   }
/*      */ 
/*      */   public synchronized void setForeground(Color paramColor) {
/*  632 */     this.foreground = paramColor;
/*  633 */     _setForeground(paramColor.getRGB());
/*      */   }
/*      */ 
/*      */   public synchronized void setBackground(Color paramColor) {
/*  637 */     this.background = paramColor;
/*  638 */     _setBackground(paramColor.getRGB());
/*      */   }
/*      */ 
/*      */   public Color getBackgroundNoSync()
/*      */   {
/*  648 */     return this.background;
/*      */   }
/*      */   public native void _setForeground(int paramInt);
/*      */ 
/*      */   public native void _setBackground(int paramInt);
/*      */ 
/*      */   public synchronized void setFont(Font paramFont) {
/*  655 */     this.font = paramFont;
/*  656 */     _setFont(paramFont);
/*      */   }
/*      */   public synchronized native void _setFont(Font paramFont);
/*      */ 
/*  660 */   public final void updateCursorImmediately() { WGlobalCursorManager.getCursorManager().updateCursorImmediately(); }
/*      */ 
/*      */ 
/*      */   public boolean requestFocus(Component paramComponent, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause)
/*      */   {
/*  668 */     if (WKeyboardFocusManagerPeer.processSynchronousLightweightTransfer((Component)this.target, paramComponent, paramBoolean1, paramBoolean2, paramLong))
/*      */     {
/*  672 */       return true;
/*      */     }
/*      */ 
/*  675 */     int i = WKeyboardFocusManagerPeer.shouldNativelyFocusHeavyweight((Component)this.target, paramComponent, paramBoolean1, paramBoolean2, paramLong, paramCause);
/*      */ 
/*  680 */     switch (i) {
/*      */     case 0:
/*  682 */       return false;
/*      */     case 2:
/*  684 */       if (focusLog.isLoggable(400)) {
/*  685 */         focusLog.finer("Proceeding with request to " + paramComponent + " in " + this.target);
/*      */       }
/*  687 */       Window localWindow = SunToolkit.getContainingWindow((Component)this.target);
/*  688 */       if (localWindow == null) {
/*  689 */         return rejectFocusRequestHelper("WARNING: Parent window is null");
/*      */       }
/*  691 */       WWindowPeer localWWindowPeer = (WWindowPeer)localWindow.getPeer();
/*  692 */       if (localWWindowPeer == null) {
/*  693 */         return rejectFocusRequestHelper("WARNING: Parent window's peer is null");
/*      */       }
/*  695 */       boolean bool = localWWindowPeer.requestWindowFocus(paramCause);
/*      */ 
/*  697 */       if (focusLog.isLoggable(400)) focusLog.finer("Requested window focus: " + bool);
/*      */ 
/*  700 */       if ((!bool) || (!localWindow.isFocused())) {
/*  701 */         return rejectFocusRequestHelper("Waiting for asynchronous processing of the request");
/*      */       }
/*  703 */       return WKeyboardFocusManagerPeer.deliverFocus(paramComponent, (Component)this.target, paramBoolean1, paramBoolean2, paramLong, paramCause);
/*      */     case 1:
/*  711 */       return true;
/*      */     }
/*  713 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean rejectFocusRequestHelper(String paramString) {
/*  717 */     if (focusLog.isLoggable(400)) focusLog.finer(paramString);
/*  718 */     WKeyboardFocusManagerPeer.removeLastFocusRequest((Component)this.target);
/*  719 */     return false;
/*      */   }
/*      */ 
/*      */   public Image createImage(ImageProducer paramImageProducer) {
/*  723 */     return new ToolkitImage(paramImageProducer);
/*      */   }
/*      */ 
/*      */   public Image createImage(int paramInt1, int paramInt2) {
/*  727 */     Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*      */ 
/*  729 */     return localWin32GraphicsConfig.createAcceleratedImage((Component)this.target, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public VolatileImage createVolatileImage(int paramInt1, int paramInt2) {
/*  733 */     return new SunVolatileImage((Component)this.target, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver) {
/*  737 */     return getToolkit().prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver) {
/*  741 */     return getToolkit().checkImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  747 */     return getClass().getName() + "[" + this.target + "]";
/*      */   }
/*      */ 
/*      */   WComponentPeer(Component paramComponent)
/*      */   {
/*  755 */     this.target = paramComponent;
/*  756 */     this.paintArea = new RepaintArea();
/*  757 */     Container localContainer = WToolkit.getNativeContainer(paramComponent);
/*  758 */     WComponentPeer localWComponentPeer = (WComponentPeer)WToolkit.targetToPeer(localContainer);
/*  759 */     create(localWComponentPeer);
/*      */ 
/*  761 */     checkCreation();
/*      */ 
/*  763 */     createScreenSurface(false);
/*  764 */     initialize();
/*  765 */     start();
/*      */   }
/*      */ 
/*      */   abstract void create(WComponentPeer paramWComponentPeer);
/*      */ 
/*      */   protected void checkCreation() {
/*  771 */     if ((this.hwnd == 0L) || (this.pData == 0L))
/*      */     {
/*  773 */       if (this.createError != null)
/*      */       {
/*  775 */         throw this.createError;
/*      */       }
/*      */ 
/*  779 */       throw new InternalError("couldn't create component peer");
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized native void start();
/*      */ 
/*      */   void initialize()
/*      */   {
/*  787 */     if (((Component)this.target).isVisible()) {
/*  788 */       show();
/*      */     }
/*  790 */     Color localColor = ((Component)this.target).getForeground();
/*  791 */     if (localColor != null) {
/*  792 */       setForeground(localColor);
/*      */     }
/*      */ 
/*  795 */     Font localFont = ((Component)this.target).getFont();
/*  796 */     if (localFont != null) {
/*  797 */       setFont(localFont);
/*      */     }
/*  799 */     if (!((Component)this.target).isEnabled()) {
/*  800 */       disable();
/*      */     }
/*  802 */     Rectangle localRectangle = ((Component)this.target).getBounds();
/*  803 */     setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, 3);
/*      */   }
/*      */ 
/*      */   void handleRepaint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */   }
/*      */ 
/*      */   void handleExpose(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  820 */     postPaintIfNecessary(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void handlePaint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  831 */     postPaintIfNecessary(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   private void postPaintIfNecessary(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  835 */     if (!AWTAccessor.getComponentAccessor().getIgnoreRepaint((Component)this.target)) {
/*  836 */       PaintEvent localPaintEvent = PaintEventDispatcher.getPaintEventDispatcher().createPaintEvent((Component)this.target, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/*  838 */       if (localPaintEvent != null)
/*  839 */         postEvent(localPaintEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   void postEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  848 */     preprocessPostEvent(paramAWTEvent);
/*  849 */     WToolkit.postEvent(WToolkit.targetToAppContext(this.target), paramAWTEvent);
/*      */   }
/*      */ 
/*      */   void preprocessPostEvent(AWTEvent paramAWTEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void beginLayout() {
/*  857 */     this.isLayouting = true;
/*      */   }
/*      */ 
/*      */   public void endLayout() {
/*  861 */     if ((!this.paintArea.isEmpty()) && (!this.paintPending) && (!((Component)this.target).getIgnoreRepaint()))
/*      */     {
/*  864 */       postEvent(new PaintEvent((Component)this.target, 800, new Rectangle()));
/*      */     }
/*      */ 
/*  867 */     this.isLayouting = false;
/*      */   }
/*      */ 
/*      */   public native void beginValidate();
/*      */ 
/*      */   public native void endValidate();
/*      */ 
/*      */   public Dimension minimumSize()
/*      */   {
/*  877 */     return getMinimumSize();
/*      */   }
/*      */ 
/*      */   public Dimension preferredSize()
/*      */   {
/*  884 */     return getPreferredSize();
/*      */   }
/*      */ 
/*      */   public synchronized void addDropTarget(DropTarget paramDropTarget)
/*      */   {
/*  892 */     if (this.nDropTargets == 0) {
/*  893 */       this.nativeDropTargetContext = addNativeDropTarget();
/*      */     }
/*  895 */     this.nDropTargets += 1;
/*      */   }
/*      */ 
/*      */   public synchronized void removeDropTarget(DropTarget paramDropTarget)
/*      */   {
/*  903 */     this.nDropTargets -= 1;
/*  904 */     if (this.nDropTargets == 0) {
/*  905 */       removeNativeDropTarget();
/*  906 */       this.nativeDropTargetContext = 0L;
/*      */     }
/*      */   }
/*      */ 
/*      */   native long addNativeDropTarget();
/*      */ 
/*      */   native void removeNativeDropTarget();
/*      */ 
/*      */   native boolean nativeHandlesWheelScrolling();
/*      */ 
/*      */   public boolean handlesWheelScrolling()
/*      */   {
/*  926 */     return nativeHandlesWheelScrolling();
/*      */   }
/*      */ 
/*      */   public boolean isPaintPending()
/*      */   {
/*  932 */     return (this.paintPending) && (this.isLayouting);
/*      */   }
/*      */ 
/*      */   public void createBuffers(int paramInt, BufferCapabilities paramBufferCapabilities)
/*      */     throws AWTException
/*      */   {
/*  945 */     Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*      */ 
/*  947 */     localWin32GraphicsConfig.assertOperationSupported((Component)this.target, paramInt, paramBufferCapabilities);
/*      */     try
/*      */     {
/*  951 */       replaceSurfaceData(paramInt - 1, paramBufferCapabilities);
/*      */     } catch (InvalidPipeException localInvalidPipeException) {
/*  953 */       throw new AWTException(localInvalidPipeException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroyBuffers()
/*      */   {
/*  959 */     replaceSurfaceData(0, null);
/*      */   }
/*      */ 
/*      */   public void flip(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents)
/*      */   {
/*  966 */     VolatileImage localVolatileImage = this.backBuffer;
/*  967 */     if (localVolatileImage == null) {
/*  968 */       throw new IllegalStateException("Buffers have not been created");
/*      */     }
/*  970 */     Win32GraphicsConfig localWin32GraphicsConfig = (Win32GraphicsConfig)getGraphicsConfiguration();
/*      */ 
/*  972 */     localWin32GraphicsConfig.flip(this, (Component)this.target, localVolatileImage, paramInt1, paramInt2, paramInt3, paramInt4, paramFlipContents);
/*      */   }
/*      */ 
/*      */   public synchronized Image getBackBuffer()
/*      */   {
/*  977 */     VolatileImage localVolatileImage = this.backBuffer;
/*  978 */     if (localVolatileImage == null) {
/*  979 */       throw new IllegalStateException("Buffers have not been created");
/*      */     }
/*  981 */     return localVolatileImage;
/*      */   }
/*      */   public BufferCapabilities getBackBufferCaps() {
/*  984 */     return this.backBufferCaps;
/*      */   }
/*      */   public int getBackBuffersNum() {
/*  987 */     return this.numBackBuffers;
/*      */   }
/*      */ 
/*      */   public boolean shouldClearRectBeforePaint()
/*      */   {
/*  993 */     return true;
/*      */   }
/*      */ 
/*      */   native void pSetParent(ComponentPeer paramComponentPeer);
/*      */ 
/*      */   public void reparent(ContainerPeer paramContainerPeer)
/*      */   {
/* 1002 */     pSetParent(paramContainerPeer);
/*      */   }
/*      */ 
/*      */   public boolean isReparentSupported()
/*      */   {
/* 1009 */     return true;
/*      */   }
/*      */ 
/*      */   public void setBoundsOperation(int paramInt)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isAccelCapable()
/*      */   {
/* 1037 */     if ((!this.isAccelCapable) || (!isContainingTopLevelAccelCapable((Component)this.target)))
/*      */     {
/* 1040 */       return false;
/*      */     }
/*      */ 
/* 1043 */     boolean bool = SunToolkit.isContainingTopLevelTranslucent((Component)this.target);
/*      */ 
/* 1047 */     return (!bool) || (Win32GraphicsEnvironment.isVistaOS());
/*      */   }
/*      */ 
/*      */   public void disableAcceleration()
/*      */   {
/* 1054 */     this.isAccelCapable = false;
/*      */   }
/*      */ 
/*      */   native void setRectangularShape(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Region paramRegion);
/*      */ 
/*      */   private static final boolean isContainingTopLevelAccelCapable(Component paramComponent)
/*      */   {
/* 1067 */     while ((paramComponent != null) && (!(paramComponent instanceof WEmbeddedFrame))) {
/* 1068 */       paramComponent = paramComponent.getParent();
/*      */     }
/* 1070 */     if (paramComponent == null) {
/* 1071 */       return true;
/*      */     }
/* 1073 */     return ((WEmbeddedFramePeer)paramComponent.getPeer()).isAccelCapable();
/*      */   }
/*      */ 
/*      */   public void applyShape(Region paramRegion)
/*      */   {
/* 1081 */     if (shapeLog.isLoggable(400)) {
/* 1082 */       shapeLog.finer("*** INFO: Setting shape: PEER: " + this + "; TARGET: " + this.target + "; SHAPE: " + paramRegion);
/*      */     }
/*      */ 
/* 1088 */     if (paramRegion != null) {
/* 1089 */       setRectangularShape(paramRegion.getLoX(), paramRegion.getLoY(), paramRegion.getHiX(), paramRegion.getHiY(), paramRegion.isRectangular() ? null : paramRegion);
/*      */     }
/*      */     else
/* 1092 */       setRectangularShape(0, 0, 0, 0, null);
/*      */   }
/*      */ 
/*      */   public void setZOrder(ComponentPeer paramComponentPeer)
/*      */   {
/* 1101 */     long l = paramComponentPeer != null ? ((WComponentPeer)paramComponentPeer).getHWnd() : 0L;
/*      */ 
/* 1103 */     setZOrder(l);
/*      */   }
/*      */ 
/*      */   private native void setZOrder(long paramLong);
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WComponentPeer
 * JD-Core Version:    0.6.2
 */