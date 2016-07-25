/*      */ package sun.awt.windows;
/*      */ 
/*      */ import java.awt.AWTException;
/*      */ import java.awt.Button;
/*      */ import java.awt.Canvas;
/*      */ import java.awt.Checkbox;
/*      */ import java.awt.CheckboxMenuItem;
/*      */ import java.awt.Choice;
/*      */ import java.awt.Component;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Desktop;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dialog.ModalExclusionType;
/*      */ import java.awt.Dialog.ModalityType;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.JobAttributes;
/*      */ import java.awt.Label;
/*      */ import java.awt.List;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.MenuItem;
/*      */ import java.awt.PageAttributes;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Point;
/*      */ import java.awt.PopupMenu;
/*      */ import java.awt.PrintJob;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Robot;
/*      */ import java.awt.ScrollPane;
/*      */ import java.awt.Scrollbar;
/*      */ import java.awt.SystemTray;
/*      */ import java.awt.TextArea;
/*      */ import java.awt.TextField;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.TrayIcon;
/*      */ import java.awt.Window;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.dnd.DragGestureEvent;
/*      */ import java.awt.dnd.DragGestureListener;
/*      */ import java.awt.dnd.DragGestureRecognizer;
/*      */ import java.awt.dnd.DragSource;
/*      */ import java.awt.dnd.InvalidDnDOperationException;
/*      */ import java.awt.dnd.MouseDragGestureRecognizer;
/*      */ import java.awt.dnd.peer.DragSourceContextPeer;
/*      */ import java.awt.im.InputMethodHighlight;
/*      */ import java.awt.im.spi.InputMethodDescriptor;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.peer.ButtonPeer;
/*      */ import java.awt.peer.CanvasPeer;
/*      */ import java.awt.peer.CheckboxMenuItemPeer;
/*      */ import java.awt.peer.CheckboxPeer;
/*      */ import java.awt.peer.ChoicePeer;
/*      */ import java.awt.peer.DesktopPeer;
/*      */ import java.awt.peer.DialogPeer;
/*      */ import java.awt.peer.FileDialogPeer;
/*      */ import java.awt.peer.FontPeer;
/*      */ import java.awt.peer.FramePeer;
/*      */ import java.awt.peer.KeyboardFocusManagerPeer;
/*      */ import java.awt.peer.LabelPeer;
/*      */ import java.awt.peer.ListPeer;
/*      */ import java.awt.peer.MenuBarPeer;
/*      */ import java.awt.peer.MenuItemPeer;
/*      */ import java.awt.peer.MenuPeer;
/*      */ import java.awt.peer.PanelPeer;
/*      */ import java.awt.peer.PopupMenuPeer;
/*      */ import java.awt.peer.RobotPeer;
/*      */ import java.awt.peer.ScrollPanePeer;
/*      */ import java.awt.peer.ScrollbarPeer;
/*      */ import java.awt.peer.SystemTrayPeer;
/*      */ import java.awt.peer.TextAreaPeer;
/*      */ import java.awt.peer.TextFieldPeer;
/*      */ import java.awt.peer.TrayIconPeer;
/*      */ import java.awt.peer.WindowPeer;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import sun.awt.AWTAutoShutdown;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.Win32GraphicsDevice;
/*      */ import sun.awt.Win32GraphicsEnvironment;
/*      */ import sun.font.FontManager;
/*      */ import sun.font.FontManagerFactory;
/*      */ import sun.font.SunFontManager;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ import sun.java2d.d3d.D3DRenderQueue;
/*      */ import sun.java2d.opengl.OGLRenderQueue;
/*      */ import sun.misc.PerformanceLogger;
/*      */ import sun.misc.ThreadGroupUtils;
/*      */ import sun.print.PrintJob2D;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class WToolkit extends SunToolkit
/*      */   implements Runnable
/*      */ {
/*   72 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WToolkit");
/*      */   public static final String XPSTYLE_THEME_ACTIVE = "win.xpstyle.themeActive";
/*      */   static GraphicsConfiguration config;
/*      */   WClipboard clipboard;
/*      */   private Hashtable cacheFontPeer;
/*      */   private WDesktopProperties wprops;
/*   89 */   protected boolean dynamicLayoutSetting = false;
/*      */ 
/*   93 */   private static boolean areExtraMouseButtonsEnabled = true;
/*      */ 
/*   99 */   private static boolean loaded = false;
/*      */   public static final String DATA_TRANSFERER_CLASS_NAME = "sun.awt.windows.WDataTransferer";
/*  220 */   private final Object anchor = new Object();
/*      */ 
/*  325 */   private boolean inited = false;
/*      */   static ColorModel screenmodel;
/*      */   private static final String prefix = "DnD.Cursor.";
/*      */   private static final String postfix = ".32x32";
/*      */   private static final String awtPrefix = "awt.";
/*      */   private static final String dndPrefix = "DnD.";
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public static void loadLibraries()
/*      */   {
/*  101 */     if (!loaded) {
/*  102 */       AccessController.doPrivileged(new LoadLibraryAction("awt"));
/*      */ 
/*  104 */       loaded = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native String getWindowsVersion();
/*      */ 
/*      */   private static native void disableCustomPalette();
/*      */ 
/*      */   public static void resetGC()
/*      */   {
/*  139 */     if (GraphicsEnvironment.isHeadless())
/*  140 */       config = null;
/*      */     else
/*  142 */       config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */   }
/*      */ 
/*      */   public static native boolean embeddedInit();
/*      */ 
/*      */   public static native boolean embeddedDispose();
/*      */ 
/*      */   public native void embeddedEventLoopIdleProcessing();
/*      */ 
/*      */   private static native void postDispose();
/*      */ 
/*      */   private static native boolean startToolkitThread(Runnable paramRunnable, ThreadGroup paramThreadGroup);
/*      */ 
/*      */   public WToolkit()
/*      */   {
/*  228 */     if (PerformanceLogger.loggingEnabled()) {
/*  229 */       PerformanceLogger.setTime("WToolkit construction");
/*      */     }
/*      */ 
/*  232 */     Disposer.addRecord(this.anchor, new ToolkitDisposer());
/*      */ 
/*  239 */     AWTAutoShutdown.notifyToolkitThreadBusy();
/*      */ 
/*  242 */     ThreadGroup localThreadGroup = (ThreadGroup)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ThreadGroup run() {
/*  245 */         return ThreadGroupUtils.getRootThreadGroup();
/*      */       }
/*      */     });
/*  248 */     if (!startToolkitThread(this, localThreadGroup)) {
/*  249 */       Thread localThread = new Thread(localThreadGroup, this, "AWT-Windows");
/*  250 */       localThread.setDaemon(true);
/*  251 */       localThread.start();
/*      */     }
/*      */     try
/*      */     {
/*  255 */       synchronized (this) {
/*  256 */         while (!this.inited) {
/*  257 */           wait();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InterruptedException localInterruptedException)
/*      */     {
/*      */     }
/*  264 */     SunToolkit.setDataTransfererClassName("sun.awt.windows.WDataTransferer");
/*      */ 
/*  268 */     setDynamicLayout(true);
/*      */ 
/*  270 */     areExtraMouseButtonsEnabled = Boolean.parseBoolean(System.getProperty("sun.awt.enableExtraMouseButtons", "true"));
/*      */ 
/*  272 */     System.setProperty("sun.awt.enableExtraMouseButtons", "" + areExtraMouseButtonsEnabled);
/*  273 */     setExtraMouseButtonsEnabledNative(areExtraMouseButtonsEnabled);
/*      */   }
/*      */ 
/*      */   private final void registerShutdownHook() {
/*  277 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*  279 */         Thread localThread = new Thread(ThreadGroupUtils.getRootThreadGroup(), new Runnable() {
/*      */           public void run() {
/*  281 */             WToolkit.this.shutdown();
/*      */           }
/*      */         });
/*  284 */         localThread.setContextClassLoader(null);
/*  285 */         Runtime.getRuntime().addShutdownHook(localThread);
/*  286 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public void run() {
/*  292 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run() {
/*  295 */         Thread.currentThread().setContextClassLoader(null);
/*  296 */         return null;
/*      */       }
/*      */     });
/*  299 */     Thread.currentThread().setPriority(6);
/*  300 */     boolean bool = init();
/*      */ 
/*  302 */     if (bool) {
/*  303 */       registerShutdownHook();
/*      */     }
/*      */ 
/*  306 */     synchronized (this) {
/*  307 */       this.inited = true;
/*  308 */       notifyAll();
/*      */     }
/*      */ 
/*  311 */     if (bool)
/*  312 */       eventLoop();
/*      */   }
/*      */ 
/*      */   private native boolean init();
/*      */ 
/*      */   private native void eventLoop();
/*      */ 
/*      */   private native void shutdown();
/*      */ 
/*      */   public static native void startSecondaryEventLoop();
/*      */ 
/*      */   public static native void quitSecondaryEventLoop();
/*      */ 
/*      */   public ButtonPeer createButton(Button paramButton)
/*      */   {
/*  350 */     WButtonPeer localWButtonPeer = new WButtonPeer(paramButton);
/*  351 */     targetCreatedPeer(paramButton, localWButtonPeer);
/*  352 */     return localWButtonPeer;
/*      */   }
/*      */ 
/*      */   public TextFieldPeer createTextField(TextField paramTextField) {
/*  356 */     WTextFieldPeer localWTextFieldPeer = new WTextFieldPeer(paramTextField);
/*  357 */     targetCreatedPeer(paramTextField, localWTextFieldPeer);
/*  358 */     return localWTextFieldPeer;
/*      */   }
/*      */ 
/*      */   public LabelPeer createLabel(Label paramLabel) {
/*  362 */     WLabelPeer localWLabelPeer = new WLabelPeer(paramLabel);
/*  363 */     targetCreatedPeer(paramLabel, localWLabelPeer);
/*  364 */     return localWLabelPeer;
/*      */   }
/*      */ 
/*      */   public ListPeer createList(List paramList) {
/*  368 */     WListPeer localWListPeer = new WListPeer(paramList);
/*  369 */     targetCreatedPeer(paramList, localWListPeer);
/*  370 */     return localWListPeer;
/*      */   }
/*      */ 
/*      */   public CheckboxPeer createCheckbox(Checkbox paramCheckbox) {
/*  374 */     WCheckboxPeer localWCheckboxPeer = new WCheckboxPeer(paramCheckbox);
/*  375 */     targetCreatedPeer(paramCheckbox, localWCheckboxPeer);
/*  376 */     return localWCheckboxPeer;
/*      */   }
/*      */ 
/*      */   public ScrollbarPeer createScrollbar(Scrollbar paramScrollbar) {
/*  380 */     WScrollbarPeer localWScrollbarPeer = new WScrollbarPeer(paramScrollbar);
/*  381 */     targetCreatedPeer(paramScrollbar, localWScrollbarPeer);
/*  382 */     return localWScrollbarPeer;
/*      */   }
/*      */ 
/*      */   public ScrollPanePeer createScrollPane(ScrollPane paramScrollPane) {
/*  386 */     WScrollPanePeer localWScrollPanePeer = new WScrollPanePeer(paramScrollPane);
/*  387 */     targetCreatedPeer(paramScrollPane, localWScrollPanePeer);
/*  388 */     return localWScrollPanePeer;
/*      */   }
/*      */ 
/*      */   public TextAreaPeer createTextArea(TextArea paramTextArea) {
/*  392 */     WTextAreaPeer localWTextAreaPeer = new WTextAreaPeer(paramTextArea);
/*  393 */     targetCreatedPeer(paramTextArea, localWTextAreaPeer);
/*  394 */     return localWTextAreaPeer;
/*      */   }
/*      */ 
/*      */   public ChoicePeer createChoice(Choice paramChoice) {
/*  398 */     WChoicePeer localWChoicePeer = new WChoicePeer(paramChoice);
/*  399 */     targetCreatedPeer(paramChoice, localWChoicePeer);
/*  400 */     return localWChoicePeer;
/*      */   }
/*      */ 
/*      */   public FramePeer createFrame(Frame paramFrame) {
/*  404 */     WFramePeer localWFramePeer = new WFramePeer(paramFrame);
/*  405 */     targetCreatedPeer(paramFrame, localWFramePeer);
/*  406 */     return localWFramePeer;
/*      */   }
/*      */ 
/*      */   public CanvasPeer createCanvas(Canvas paramCanvas) {
/*  410 */     WCanvasPeer localWCanvasPeer = new WCanvasPeer(paramCanvas);
/*  411 */     targetCreatedPeer(paramCanvas, localWCanvasPeer);
/*  412 */     return localWCanvasPeer;
/*      */   }
/*      */ 
/*      */   public void disableBackgroundErase(Canvas paramCanvas) {
/*  416 */     WCanvasPeer localWCanvasPeer = (WCanvasPeer)paramCanvas.getPeer();
/*  417 */     if (localWCanvasPeer == null) {
/*  418 */       throw new IllegalStateException("Canvas must have a valid peer");
/*      */     }
/*  420 */     localWCanvasPeer.disableBackgroundErase();
/*      */   }
/*      */ 
/*      */   public PanelPeer createPanel(Panel paramPanel) {
/*  424 */     WPanelPeer localWPanelPeer = new WPanelPeer(paramPanel);
/*  425 */     targetCreatedPeer(paramPanel, localWPanelPeer);
/*  426 */     return localWPanelPeer;
/*      */   }
/*      */ 
/*      */   public WindowPeer createWindow(Window paramWindow) {
/*  430 */     WWindowPeer localWWindowPeer = new WWindowPeer(paramWindow);
/*  431 */     targetCreatedPeer(paramWindow, localWWindowPeer);
/*  432 */     return localWWindowPeer;
/*      */   }
/*      */ 
/*      */   public DialogPeer createDialog(Dialog paramDialog) {
/*  436 */     WDialogPeer localWDialogPeer = new WDialogPeer(paramDialog);
/*  437 */     targetCreatedPeer(paramDialog, localWDialogPeer);
/*  438 */     return localWDialogPeer;
/*      */   }
/*      */ 
/*      */   public FileDialogPeer createFileDialog(FileDialog paramFileDialog) {
/*  442 */     WFileDialogPeer localWFileDialogPeer = new WFileDialogPeer(paramFileDialog);
/*  443 */     targetCreatedPeer(paramFileDialog, localWFileDialogPeer);
/*  444 */     return localWFileDialogPeer;
/*      */   }
/*      */ 
/*      */   public MenuBarPeer createMenuBar(MenuBar paramMenuBar) {
/*  448 */     WMenuBarPeer localWMenuBarPeer = new WMenuBarPeer(paramMenuBar);
/*  449 */     targetCreatedPeer(paramMenuBar, localWMenuBarPeer);
/*  450 */     return localWMenuBarPeer;
/*      */   }
/*      */ 
/*      */   public MenuPeer createMenu(Menu paramMenu) {
/*  454 */     WMenuPeer localWMenuPeer = new WMenuPeer(paramMenu);
/*  455 */     targetCreatedPeer(paramMenu, localWMenuPeer);
/*  456 */     return localWMenuPeer;
/*      */   }
/*      */ 
/*      */   public PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu) {
/*  460 */     WPopupMenuPeer localWPopupMenuPeer = new WPopupMenuPeer(paramPopupMenu);
/*  461 */     targetCreatedPeer(paramPopupMenu, localWPopupMenuPeer);
/*  462 */     return localWPopupMenuPeer;
/*      */   }
/*      */ 
/*      */   public MenuItemPeer createMenuItem(MenuItem paramMenuItem) {
/*  466 */     WMenuItemPeer localWMenuItemPeer = new WMenuItemPeer(paramMenuItem);
/*  467 */     targetCreatedPeer(paramMenuItem, localWMenuItemPeer);
/*  468 */     return localWMenuItemPeer;
/*      */   }
/*      */ 
/*      */   public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem) {
/*  472 */     WCheckboxMenuItemPeer localWCheckboxMenuItemPeer = new WCheckboxMenuItemPeer(paramCheckboxMenuItem);
/*  473 */     targetCreatedPeer(paramCheckboxMenuItem, localWCheckboxMenuItemPeer);
/*  474 */     return localWCheckboxMenuItemPeer;
/*      */   }
/*      */ 
/*      */   public RobotPeer createRobot(Robot paramRobot, GraphicsDevice paramGraphicsDevice)
/*      */   {
/*  481 */     return new WRobotPeer(paramGraphicsDevice);
/*      */   }
/*      */ 
/*      */   public WEmbeddedFramePeer createEmbeddedFrame(WEmbeddedFrame paramWEmbeddedFrame) {
/*  485 */     WEmbeddedFramePeer localWEmbeddedFramePeer = new WEmbeddedFramePeer(paramWEmbeddedFrame);
/*  486 */     targetCreatedPeer(paramWEmbeddedFrame, localWEmbeddedFramePeer);
/*  487 */     return localWEmbeddedFramePeer;
/*      */   }
/*      */ 
/*      */   WPrintDialogPeer createWPrintDialog(WPrintDialog paramWPrintDialog) {
/*  491 */     WPrintDialogPeer localWPrintDialogPeer = new WPrintDialogPeer(paramWPrintDialog);
/*  492 */     targetCreatedPeer(paramWPrintDialog, localWPrintDialogPeer);
/*  493 */     return localWPrintDialogPeer;
/*      */   }
/*      */ 
/*      */   WPageDialogPeer createWPageDialog(WPageDialog paramWPageDialog) {
/*  497 */     WPageDialogPeer localWPageDialogPeer = new WPageDialogPeer(paramWPageDialog);
/*  498 */     targetCreatedPeer(paramWPageDialog, localWPageDialogPeer);
/*  499 */     return localWPageDialogPeer;
/*      */   }
/*      */ 
/*      */   public TrayIconPeer createTrayIcon(TrayIcon paramTrayIcon) {
/*  503 */     WTrayIconPeer localWTrayIconPeer = new WTrayIconPeer(paramTrayIcon);
/*  504 */     targetCreatedPeer(paramTrayIcon, localWTrayIconPeer);
/*  505 */     return localWTrayIconPeer;
/*      */   }
/*      */ 
/*      */   public SystemTrayPeer createSystemTray(SystemTray paramSystemTray) {
/*  509 */     return new WSystemTrayPeer(paramSystemTray);
/*      */   }
/*      */ 
/*      */   public boolean isTraySupported() {
/*  513 */     return true;
/*      */   }
/*      */ 
/*      */   public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer()
/*      */     throws HeadlessException
/*      */   {
/*  519 */     return WKeyboardFocusManagerPeer.getInstance();
/*      */   }
/*      */ 
/*      */   protected native void setDynamicLayoutNative(boolean paramBoolean);
/*      */ 
/*      */   public void setDynamicLayout(boolean paramBoolean) {
/*  525 */     if (paramBoolean == this.dynamicLayoutSetting) {
/*  526 */       return;
/*      */     }
/*      */ 
/*  529 */     this.dynamicLayoutSetting = paramBoolean;
/*  530 */     setDynamicLayoutNative(paramBoolean);
/*      */   }
/*      */ 
/*      */   protected boolean isDynamicLayoutSet() {
/*  534 */     return this.dynamicLayoutSetting;
/*      */   }
/*      */ 
/*      */   protected native boolean isDynamicLayoutSupportedNative();
/*      */ 
/*      */   public boolean isDynamicLayoutActive()
/*      */   {
/*  544 */     return (isDynamicLayoutSet()) && (isDynamicLayoutSupported());
/*      */   }
/*      */ 
/*      */   public boolean isFrameStateSupported(int paramInt)
/*      */   {
/*  551 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 6:
/*  555 */       return true;
/*      */     }
/*  557 */     return false;
/*      */   }
/*      */ 
/*      */   static native ColorModel makeColorModel();
/*      */ 
/*      */   static ColorModel getStaticColorModel()
/*      */   {
/*  565 */     if (GraphicsEnvironment.isHeadless()) {
/*  566 */       throw new IllegalArgumentException();
/*      */     }
/*  568 */     if (config == null) {
/*  569 */       resetGC();
/*      */     }
/*  571 */     return config.getColorModel();
/*      */   }
/*      */ 
/*      */   public ColorModel getColorModel() {
/*  575 */     return getStaticColorModel();
/*      */   }
/*      */ 
/*      */   public Insets getScreenInsets(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  580 */     return getScreenInsets(((Win32GraphicsDevice)paramGraphicsConfiguration.getDevice()).getScreen());
/*      */   }
/*      */ 
/*      */   public int getScreenResolution() {
/*  584 */     Win32GraphicsEnvironment localWin32GraphicsEnvironment = (Win32GraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/*  586 */     return localWin32GraphicsEnvironment.getXResolution();
/*      */   }
/*      */ 
/*      */   protected native int getScreenWidth();
/*      */ 
/*      */   protected native int getScreenHeight();
/*      */ 
/*      */   protected native Insets getScreenInsets(int paramInt);
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont) {
/*  596 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*  597 */     if (((localFontManager instanceof SunFontManager)) && (((SunFontManager)localFontManager).usePlatformFontMetrics()))
/*      */     {
/*  599 */       return WFontMetrics.getFontMetrics(paramFont);
/*      */     }
/*  601 */     return super.getFontMetrics(paramFont);
/*      */   }
/*      */ 
/*      */   public FontPeer getFontPeer(String paramString, int paramInt) {
/*  605 */     Object localObject = null;
/*  606 */     String str = paramString.toLowerCase();
/*  607 */     if (null != this.cacheFontPeer) {
/*  608 */       localObject = (FontPeer)this.cacheFontPeer.get(str + paramInt);
/*  609 */       if (null != localObject) {
/*  610 */         return localObject;
/*      */       }
/*      */     }
/*  613 */     localObject = new WFontPeer(paramString, paramInt);
/*  614 */     if (localObject != null) {
/*  615 */       if (null == this.cacheFontPeer) {
/*  616 */         this.cacheFontPeer = new Hashtable(5, 0.9F);
/*      */       }
/*  618 */       if (null != this.cacheFontPeer) {
/*  619 */         this.cacheFontPeer.put(str + paramInt, localObject);
/*      */       }
/*      */     }
/*  622 */     return localObject;
/*      */   }
/*      */ 
/*      */   private native void nativeSync();
/*      */ 
/*      */   public void sync()
/*      */   {
/*  629 */     nativeSync();
/*      */ 
/*  631 */     OGLRenderQueue.sync();
/*      */ 
/*  633 */     D3DRenderQueue.sync();
/*      */   }
/*      */ 
/*      */   public PrintJob getPrintJob(Frame paramFrame, String paramString, Properties paramProperties)
/*      */   {
/*  638 */     return getPrintJob(paramFrame, paramString, null, null);
/*      */   }
/*      */ 
/*      */   public PrintJob getPrintJob(Frame paramFrame, String paramString, JobAttributes paramJobAttributes, PageAttributes paramPageAttributes)
/*      */   {
/*  645 */     if (paramFrame == null) {
/*  646 */       throw new NullPointerException("frame must not be null");
/*      */     }
/*      */ 
/*  649 */     PrintJob2D localPrintJob2D = new PrintJob2D(paramFrame, paramString, paramJobAttributes, paramPageAttributes);
/*      */ 
/*  652 */     if (!localPrintJob2D.printDialog()) {
/*  653 */       localPrintJob2D = null;
/*      */     }
/*      */ 
/*  656 */     return localPrintJob2D;
/*      */   }
/*      */ 
/*      */   public native void beep();
/*      */ 
/*      */   public boolean getLockingKeyState(int paramInt) {
/*  662 */     if ((paramInt != 20) && (paramInt != 144) && (paramInt != 145) && (paramInt != 262))
/*      */     {
/*  664 */       throw new IllegalArgumentException("invalid key for Toolkit.getLockingKeyState");
/*      */     }
/*  666 */     return getLockingKeyStateNative(paramInt);
/*      */   }
/*      */ 
/*      */   public native boolean getLockingKeyStateNative(int paramInt);
/*      */ 
/*      */   public void setLockingKeyState(int paramInt, boolean paramBoolean) {
/*  672 */     if ((paramInt != 20) && (paramInt != 144) && (paramInt != 145) && (paramInt != 262))
/*      */     {
/*  674 */       throw new IllegalArgumentException("invalid key for Toolkit.setLockingKeyState");
/*      */     }
/*  676 */     setLockingKeyStateNative(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   public native void setLockingKeyStateNative(int paramInt, boolean paramBoolean);
/*      */ 
/*      */   public Clipboard getSystemClipboard() {
/*  682 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  683 */     if (localSecurityManager != null) {
/*  684 */       localSecurityManager.checkSystemClipboardAccess();
/*      */     }
/*  686 */     synchronized (this) {
/*  687 */       if (this.clipboard == null) {
/*  688 */         this.clipboard = new WClipboard();
/*      */       }
/*      */     }
/*  691 */     return this.clipboard;
/*      */   }
/*      */ 
/*      */   protected native void loadSystemColors(int[] paramArrayOfInt);
/*      */ 
/*      */   public static final Object targetToPeer(Object paramObject) {
/*  697 */     return SunToolkit.targetToPeer(paramObject);
/*      */   }
/*      */ 
/*      */   public static final void targetDisposedPeer(Object paramObject1, Object paramObject2) {
/*  701 */     SunToolkit.targetDisposedPeer(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public InputMethodDescriptor getInputMethodAdapterDescriptor()
/*      */   {
/*  708 */     return new WInputMethodDescriptor();
/*      */   }
/*      */ 
/*      */   public Map mapInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight)
/*      */   {
/*  715 */     return WInputMethod.mapInputMethodHighlight(paramInputMethodHighlight);
/*      */   }
/*      */ 
/*      */   public boolean enableInputMethodsForTextComponent()
/*      */   {
/*  723 */     return true;
/*      */   }
/*      */ 
/*      */   public Locale getDefaultKeyboardLocale()
/*      */   {
/*  730 */     Locale localLocale = WInputMethod.getNativeLocale();
/*      */ 
/*  732 */     if (localLocale == null) {
/*  733 */       return super.getDefaultKeyboardLocale();
/*      */     }
/*  735 */     return localLocale;
/*      */   }
/*      */ 
/*      */   public Cursor createCustomCursor(Image paramImage, Point paramPoint, String paramString)
/*      */     throws IndexOutOfBoundsException
/*      */   {
/*  744 */     return new WCustomCursor(paramImage, paramPoint, paramString);
/*      */   }
/*      */ 
/*      */   public Dimension getBestCursorSize(int paramInt1, int paramInt2)
/*      */   {
/*  751 */     return new Dimension(WCustomCursor.getCursorWidth(), WCustomCursor.getCursorHeight());
/*      */   }
/*      */ 
/*      */   public native int getMaximumCursorColors();
/*      */ 
/*      */   static void paletteChanged()
/*      */   {
/*  758 */     ((Win32GraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment()).paletteChanged();
/*      */   }
/*      */ 
/*      */   public static void displayChanged()
/*      */   {
/*  769 */     EventQueue.invokeLater(new Runnable() {
/*      */       public void run() {
/*  771 */         ((Win32GraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment()).displayChanged();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*      */     throws InvalidDnDOperationException
/*      */   {
/*  783 */     return WDragSourceContextPeer.createDragSourceContextPeer(paramDragGestureEvent);
/*      */   }
/*      */ 
/*      */   public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> paramClass, DragSource paramDragSource, Component paramComponent, int paramInt, DragGestureListener paramDragGestureListener)
/*      */   {
/*  791 */     if (MouseDragGestureRecognizer.class.equals(paramClass)) {
/*  792 */       return new WMouseDragGestureRecognizer(paramDragSource, paramComponent, paramInt, paramDragGestureListener);
/*      */     }
/*  794 */     return null;
/*      */   }
/*      */ 
/*      */   protected Object lazilyLoadDesktopProperty(String paramString)
/*      */   {
/*  807 */     if (paramString.startsWith("DnD.Cursor.")) {
/*  808 */       String str = paramString.substring("DnD.Cursor.".length(), paramString.length()) + ".32x32";
/*      */       try
/*      */       {
/*  811 */         return Cursor.getSystemCustomCursor(str);
/*      */       } catch (AWTException localAWTException) {
/*  813 */         throw new RuntimeException("cannot load system cursor: " + str, localAWTException);
/*      */       }
/*      */     }
/*      */ 
/*  817 */     if (paramString.equals("awt.dynamicLayoutSupported")) {
/*  818 */       return Boolean.valueOf(isDynamicLayoutSupported());
/*      */     }
/*      */ 
/*  821 */     if ((WDesktopProperties.isWindowsProperty(paramString)) || (paramString.startsWith("awt.")) || (paramString.startsWith("DnD.")))
/*      */     {
/*  824 */       synchronized (this) {
/*  825 */         lazilyInitWProps();
/*  826 */         return this.desktopProperties.get(paramString);
/*      */       }
/*      */     }
/*      */ 
/*  830 */     return super.lazilyLoadDesktopProperty(paramString);
/*      */   }
/*      */ 
/*      */   private synchronized void lazilyInitWProps() {
/*  834 */     if (this.wprops == null) {
/*  835 */       this.wprops = new WDesktopProperties(this);
/*  836 */       updateProperties(this.wprops.getProperties());
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized boolean isDynamicLayoutSupported()
/*      */   {
/*  845 */     boolean bool = isDynamicLayoutSupportedNative();
/*  846 */     lazilyInitWProps();
/*  847 */     Boolean localBoolean = (Boolean)this.desktopProperties.get("awt.dynamicLayoutSupported");
/*      */ 
/*  849 */     if (log.isLoggable(400)) {
/*  850 */       log.finer("In WTK.isDynamicLayoutSupported()   nativeDynamic == " + bool + "   wprops.dynamic == " + localBoolean);
/*      */     }
/*      */ 
/*  855 */     if ((localBoolean == null) || (bool != localBoolean.booleanValue()))
/*      */     {
/*  858 */       windowsSettingChange();
/*  859 */       return bool;
/*      */     }
/*      */ 
/*  862 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private void windowsSettingChange()
/*      */   {
/*  874 */     final Map localMap = getWProps();
/*  875 */     if (localMap == null)
/*      */     {
/*  877 */       return;
/*      */     }
/*      */ 
/*  880 */     updateXPStyleEnabled(localMap.get("win.xpstyle.themeActive"));
/*      */ 
/*  882 */     if (AppContext.getAppContext() == null)
/*      */     {
/*  885 */       updateProperties(localMap);
/*      */     }
/*      */     else
/*      */     {
/*  890 */       EventQueue.invokeLater(new Runnable()
/*      */       {
/*      */         public void run() {
/*  893 */           WToolkit.this.updateProperties(localMap);
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void updateProperties(Map<String, Object> paramMap) {
/*  900 */     if (null == paramMap) {
/*  901 */       return;
/*      */     }
/*      */ 
/*  904 */     updateXPStyleEnabled(paramMap.get("win.xpstyle.themeActive"));
/*      */ 
/*  906 */     for (String str : paramMap.keySet()) {
/*  907 */       Object localObject = paramMap.get(str);
/*  908 */       if (log.isLoggable(400)) {
/*  909 */         log.finer("changed " + str + " to " + localObject);
/*      */       }
/*  911 */       setDesktopProperty(str, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener) {
/*  916 */     if (paramString == null)
/*      */     {
/*  918 */       return;
/*      */     }
/*  920 */     if ((WDesktopProperties.isWindowsProperty(paramString)) || (paramString.startsWith("awt.")) || (paramString.startsWith("DnD.")))
/*      */     {
/*  926 */       lazilyInitWProps();
/*      */     }
/*  928 */     super.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   protected synchronized void initializeDesktopProperties()
/*      */   {
/*  936 */     this.desktopProperties.put("DnD.Autoscroll.initialDelay", Integer.valueOf(50));
/*      */ 
/*  938 */     this.desktopProperties.put("DnD.Autoscroll.interval", Integer.valueOf(50));
/*      */ 
/*  940 */     this.desktopProperties.put("DnD.isDragImageSupported", Boolean.TRUE);
/*      */ 
/*  942 */     this.desktopProperties.put("Shell.shellFolderManager", "sun.awt.shell.Win32ShellFolderManager2");
/*      */   }
/*      */ 
/*      */   protected synchronized RenderingHints getDesktopAAHints()
/*      */   {
/*  951 */     if (this.wprops == null) {
/*  952 */       return null;
/*      */     }
/*  954 */     return this.wprops.getDesktopAAHints();
/*      */   }
/*      */ 
/*      */   public boolean isModalityTypeSupported(Dialog.ModalityType paramModalityType)
/*      */   {
/*  959 */     return (paramModalityType == null) || (paramModalityType == Dialog.ModalityType.MODELESS) || (paramModalityType == Dialog.ModalityType.DOCUMENT_MODAL) || (paramModalityType == Dialog.ModalityType.APPLICATION_MODAL) || (paramModalityType == Dialog.ModalityType.TOOLKIT_MODAL);
/*      */   }
/*      */ 
/*      */   public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType paramModalExclusionType)
/*      */   {
/*  967 */     return (paramModalExclusionType == null) || (paramModalExclusionType == Dialog.ModalExclusionType.NO_EXCLUDE) || (paramModalExclusionType == Dialog.ModalExclusionType.APPLICATION_EXCLUDE) || (paramModalExclusionType == Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
/*      */   }
/*      */ 
/*      */   public static WToolkit getWToolkit()
/*      */   {
/*  974 */     WToolkit localWToolkit = (WToolkit)Toolkit.getDefaultToolkit();
/*  975 */     return localWToolkit;
/*      */   }
/*      */ 
/*      */   public boolean useBufferPerWindow()
/*      */   {
/* 1002 */     return !Win32GraphicsEnvironment.isDWMCompositionEnabled();
/*      */   }
/*      */ 
/*      */   public void grab(Window paramWindow) {
/* 1006 */     if (paramWindow.getPeer() != null)
/* 1007 */       ((WWindowPeer)paramWindow.getPeer()).grab();
/*      */   }
/*      */ 
/*      */   public void ungrab(Window paramWindow)
/*      */   {
/* 1012 */     if (paramWindow.getPeer() != null)
/* 1013 */       ((WWindowPeer)paramWindow.getPeer()).ungrab();
/*      */   }
/*      */ 
/*      */   public native boolean syncNativeQueue(long paramLong);
/*      */ 
/*      */   public boolean isDesktopSupported() {
/* 1019 */     return true;
/*      */   }
/*      */ 
/*      */   public DesktopPeer createDesktopPeer(Desktop paramDesktop) {
/* 1023 */     return new WDesktopPeer();
/*      */   }
/*      */ 
/*      */   public static native void setExtraMouseButtonsEnabledNative(boolean paramBoolean);
/*      */ 
/*      */   public boolean areExtraMouseButtonsEnabled() throws HeadlessException {
/* 1029 */     return areExtraMouseButtonsEnabled;
/*      */   }
/*      */ 
/*      */   private synchronized native int getNumberOfButtonsImpl();
/*      */ 
/*      */   private synchronized Map<String, Object> getWProps() {
/* 1035 */     return this.wprops != null ? this.wprops.getProperties() : null;
/*      */   }
/*      */ 
/*      */   private void updateXPStyleEnabled(Object paramObject) {
/* 1039 */     ThemeReader.xpStyleEnabled = Boolean.TRUE.equals(paramObject);
/*      */   }
/*      */ 
/*      */   public int getNumberOfButtons()
/*      */   {
/* 1044 */     if (numberOfButtons == 0) {
/* 1045 */       numberOfButtons = getNumberOfButtonsImpl();
/*      */     }
/* 1047 */     return numberOfButtons > 20 ? 20 : numberOfButtons;
/*      */   }
/*      */ 
/*      */   public boolean isWindowOpacitySupported()
/*      */   {
/* 1053 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isWindowShapingSupported()
/*      */   {
/* 1058 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isWindowTranslucencySupported()
/*      */   {
/* 1064 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isTranslucencyCapable(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/* 1070 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean needUpdateWindow()
/*      */   {
/* 1077 */     return true;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  111 */     loadLibraries();
/*  112 */     initIDs();
/*      */ 
/*  115 */     if (log.isLoggable(500)) {
/*  116 */       log.fine("Win version: " + getWindowsVersion());
/*      */     }
/*      */ 
/*  119 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  123 */         String str = System.getProperty("browser");
/*  124 */         if ((str != null) && (str.equals("sun.plugin"))) {
/*  125 */           WToolkit.access$000();
/*      */         }
/*  127 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   static class ToolkitDisposer
/*      */     implements DisposerRecord
/*      */   {
/*      */     public void dispose()
/*      */     {
/*  216 */       WToolkit.access$100();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WToolkit
 * JD-Core Version:    0.6.2
 */