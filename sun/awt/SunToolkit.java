/*      */ package sun.awt;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.AWTException;
/*      */ import java.awt.Button;
/*      */ import java.awt.Canvas;
/*      */ import java.awt.Checkbox;
/*      */ import java.awt.CheckboxMenuItem;
/*      */ import java.awt.Choice;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.DefaultKeyboardFocusManager;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dialog.ModalExclusionType;
/*      */ import java.awt.Dialog.ModalityType;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Image;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Label;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.MenuComponent;
/*      */ import java.awt.MenuItem;
/*      */ import java.awt.Panel;
/*      */ import java.awt.PopupMenu;
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
/*      */ import java.awt.dnd.DragGestureEvent;
/*      */ import java.awt.dnd.InvalidDnDOperationException;
/*      */ import java.awt.dnd.peer.DragSourceContextPeer;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferInt;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.ImageProducer;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.awt.peer.ButtonPeer;
/*      */ import java.awt.peer.CanvasPeer;
/*      */ import java.awt.peer.CheckboxMenuItemPeer;
/*      */ import java.awt.peer.CheckboxPeer;
/*      */ import java.awt.peer.ChoicePeer;
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
/*      */ import java.awt.peer.MouseInfoPeer;
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
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.awt.im.InputContext;
/*      */ import sun.awt.im.SimpleInputMethodWindow;
/*      */ import sun.awt.image.ByteArrayImageSource;
/*      */ import sun.awt.image.FileImageSource;
/*      */ import sun.awt.image.ImageRepresentation;
/*      */ import sun.awt.image.ToolkitImage;
/*      */ import sun.awt.image.URLImageSource;
/*      */ import sun.font.FontDesignMetrics;
/*      */ import sun.misc.SoftCache;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.util.SecurityConstants.AWT;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class SunToolkit extends Toolkit
/*      */   implements WindowClosingSupport, WindowClosingListener, ComponentFactory, InputMethodSupport, KeyboardFocusManagerPeerProvider
/*      */ {
/*      */   public static final int GRAB_EVENT_MASK = -2147483648;
/*      */   private static final String POST_EVENT_QUEUE_KEY = "PostEventQueue";
/*   87 */   protected static int numberOfButtons = 0;
/*      */   public static final int MAX_BUTTONS_SUPPORTED = 20;
/*  241 */   private static final ReentrantLock AWT_LOCK = new ReentrantLock();
/*  242 */   private static final Condition AWT_LOCK_COND = AWT_LOCK.newCondition();
/*      */ 
/*  337 */   private static final Map appContextMap = Collections.synchronizedMap(new WeakHashMap());
/*      */ 
/*  512 */   protected static final Lock flushLock = new ReentrantLock();
/*  513 */   private static boolean isFlushingPendingEvents = false;
/*      */ 
/*  725 */   static final SoftCache imgCache = new SoftCache();
/*      */ 
/* 1094 */   private static Locale startupLocale = null;
/*      */ 
/* 1135 */   private static String dataTransfererClassName = null;
/*      */ 
/* 1149 */   private transient WindowClosingListener windowClosingListener = null;
/*      */ 
/* 1184 */   private static DefaultMouseInfoPeer mPeer = null;
/*      */ 
/* 1226 */   private static Dialog.ModalExclusionType DEFAULT_MODAL_EXCLUSION_TYPE = null;
/*      */ 
/* 1338 */   private ModalityListenerList modalityListeners = new ModalityListenerList();
/*      */   public static final int DEFAULT_WAIT_TIME = 10000;
/*      */   private static final int MAX_ITERS = 20;
/*      */   private static final int MIN_ITERS = 0;
/*      */   private static final int MINIMAL_EDELAY = 0;
/* 1550 */   private boolean eventDispatched = false;
/* 1551 */   private boolean queueEmpty = false;
/* 1552 */   private final Object waitLock = "Wait Lock";
/*      */   private static boolean checkedSystemAAFontSettings;
/*      */   private static boolean useSystemAAFontSettings;
/* 1660 */   private static boolean lastExtraCondition = true;
/*      */   private static RenderingHints desktopFontHints;
/*      */   public static final String DESKTOPFONTHINTS = "awt.font.desktophints";
/* 1838 */   private static Boolean sunAwtDisableMixing = null;
/*      */ 
/* 1861 */   private static final Object DEACTIVATION_TIMES_MAP_KEY = new Object();
/*      */ 
/*      */   private static void initEQ(AppContext paramAppContext)
/*      */   {
/*  112 */     String str = System.getProperty("AWT.EventQueueClass", "java.awt.EventQueue");
/*      */     EventQueue localEventQueue;
/*      */     try
/*      */     {
/*  116 */       localEventQueue = (EventQueue)Class.forName(str).newInstance();
/*      */     } catch (Exception localException) {
/*  118 */       localException.printStackTrace();
/*  119 */       System.err.println("Failed loading " + str + ": " + localException);
/*  120 */       localEventQueue = new EventQueue();
/*      */     }
/*  122 */     paramAppContext.put(AppContext.EVENT_QUEUE_KEY, localEventQueue);
/*      */ 
/*  124 */     PostEventQueue localPostEventQueue = new PostEventQueue(localEventQueue);
/*  125 */     paramAppContext.put("PostEventQueue", localPostEventQueue);
/*      */   }
/*      */ 
/*      */   public boolean useBufferPerWindow()
/*      */   {
/*  132 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract WindowPeer createWindow(Window paramWindow)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract FramePeer createFrame(Frame paramFrame)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract DialogPeer createDialog(Dialog paramDialog)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract ButtonPeer createButton(Button paramButton)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract TextFieldPeer createTextField(TextField paramTextField)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract ChoicePeer createChoice(Choice paramChoice)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract LabelPeer createLabel(Label paramLabel)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract ListPeer createList(java.awt.List paramList)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract CheckboxPeer createCheckbox(Checkbox paramCheckbox)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract ScrollbarPeer createScrollbar(Scrollbar paramScrollbar)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract ScrollPanePeer createScrollPane(ScrollPane paramScrollPane)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract TextAreaPeer createTextArea(TextArea paramTextArea)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract FileDialogPeer createFileDialog(FileDialog paramFileDialog)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract MenuBarPeer createMenuBar(MenuBar paramMenuBar)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract MenuPeer createMenu(Menu paramMenu)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract MenuItemPeer createMenuItem(MenuItem paramMenuItem)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem)
/*      */     throws HeadlessException;
/*      */ 
/*      */   public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
/*      */     throws InvalidDnDOperationException;
/*      */ 
/*      */   public abstract TrayIconPeer createTrayIcon(TrayIcon paramTrayIcon)
/*      */     throws HeadlessException, AWTException;
/*      */ 
/*      */   public abstract SystemTrayPeer createSystemTray(SystemTray paramSystemTray);
/*      */ 
/*      */   public abstract boolean isTraySupported();
/*      */ 
/*      */   public abstract FontPeer getFontPeer(String paramString, int paramInt);
/*      */ 
/*      */   public abstract RobotPeer createRobot(Robot paramRobot, GraphicsDevice paramGraphicsDevice)
/*      */     throws AWTException;
/*      */ 
/*      */   public abstract KeyboardFocusManagerPeer getKeyboardFocusManagerPeer()
/*      */     throws HeadlessException;
/*      */ 
/*      */   public static final void awtLock()
/*      */   {
/*  245 */     AWT_LOCK.lock();
/*      */   }
/*      */ 
/*      */   public static final boolean awtTryLock() {
/*  249 */     return AWT_LOCK.tryLock();
/*      */   }
/*      */ 
/*      */   public static final void awtUnlock() {
/*  253 */     AWT_LOCK.unlock();
/*      */   }
/*      */ 
/*      */   public static final void awtLockWait()
/*      */     throws InterruptedException
/*      */   {
/*  259 */     AWT_LOCK_COND.await();
/*      */   }
/*      */ 
/*      */   public static final void awtLockWait(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  265 */     AWT_LOCK_COND.await(paramLong, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */   public static final void awtLockNotify() {
/*  269 */     AWT_LOCK_COND.signal();
/*      */   }
/*      */ 
/*      */   public static final void awtLockNotifyAll() {
/*  273 */     AWT_LOCK_COND.signalAll();
/*      */   }
/*      */ 
/*      */   public static final boolean isAWTLockHeldByCurrentThread() {
/*  277 */     return AWT_LOCK.isHeldByCurrentThread();
/*      */   }
/*      */ 
/*      */   public static AppContext createNewAppContext()
/*      */   {
/*  286 */     ThreadGroup localThreadGroup = Thread.currentThread().getThreadGroup();
/*  287 */     return createNewAppContext(localThreadGroup);
/*      */   }
/*      */ 
/*      */   static final AppContext createNewAppContext(ThreadGroup paramThreadGroup)
/*      */   {
/*  294 */     AppContext localAppContext = new AppContext(paramThreadGroup);
/*  295 */     initEQ(localAppContext);
/*      */ 
/*  297 */     return localAppContext;
/*      */   }
/*      */ 
/*      */   static void wakeupEventQueue(EventQueue paramEventQueue, boolean paramBoolean) {
/*  301 */     AWTAccessor.getEventQueueAccessor().wakeup(paramEventQueue, paramBoolean);
/*      */   }
/*      */ 
/*      */   protected static Object targetToPeer(Object paramObject)
/*      */   {
/*  313 */     if ((paramObject != null) && (!GraphicsEnvironment.isHeadless())) {
/*  314 */       return AWTAutoShutdown.getInstance().getPeer(paramObject);
/*      */     }
/*  316 */     return null;
/*      */   }
/*      */ 
/*      */   protected static void targetCreatedPeer(Object paramObject1, Object paramObject2) {
/*  320 */     if ((paramObject1 != null) && (paramObject2 != null) && (!GraphicsEnvironment.isHeadless()))
/*      */     {
/*  323 */       AWTAutoShutdown.getInstance().registerPeer(paramObject1, paramObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static void targetDisposedPeer(Object paramObject1, Object paramObject2) {
/*  328 */     if ((paramObject1 != null) && (paramObject2 != null) && (!GraphicsEnvironment.isHeadless()))
/*      */     {
/*  331 */       AWTAutoShutdown.getInstance().unregisterPeer(paramObject1, paramObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean setAppContext(Object paramObject, AppContext paramAppContext)
/*      */   {
/*  346 */     if ((paramObject instanceof Component)) {
/*  347 */       AWTAccessor.getComponentAccessor().setAppContext((Component)paramObject, paramAppContext);
/*      */     }
/*  349 */     else if ((paramObject instanceof MenuComponent)) {
/*  350 */       AWTAccessor.getMenuComponentAccessor().setAppContext((MenuComponent)paramObject, paramAppContext);
/*      */     }
/*      */     else {
/*  353 */       return false;
/*      */     }
/*  355 */     return true;
/*      */   }
/*      */ 
/*      */   private static AppContext getAppContext(Object paramObject)
/*      */   {
/*  363 */     if ((paramObject instanceof Component)) {
/*  364 */       return AWTAccessor.getComponentAccessor().getAppContext((Component)paramObject);
/*      */     }
/*  366 */     if ((paramObject instanceof MenuComponent)) {
/*  367 */       return AWTAccessor.getMenuComponentAccessor().getAppContext((MenuComponent)paramObject);
/*      */     }
/*      */ 
/*  370 */     return null;
/*      */   }
/*      */ 
/*      */   public static AppContext targetToAppContext(Object paramObject)
/*      */   {
/*  381 */     if (paramObject == null) {
/*  382 */       return null;
/*      */     }
/*  384 */     AppContext localAppContext = getAppContext(paramObject);
/*  385 */     if (localAppContext == null)
/*      */     {
/*  388 */       localAppContext = (AppContext)appContextMap.get(paramObject);
/*      */     }
/*  390 */     return localAppContext;
/*      */   }
/*      */ 
/*      */   public static void setLWRequestStatus(Window paramWindow, boolean paramBoolean)
/*      */   {
/*  419 */     AWTAccessor.getWindowAccessor().setLWRequestStatus(paramWindow, paramBoolean);
/*      */   }
/*      */ 
/*      */   public static void checkAndSetPolicy(Container paramContainer) {
/*  423 */     FocusTraversalPolicy localFocusTraversalPolicy = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy();
/*      */ 
/*  427 */     paramContainer.setFocusTraversalPolicy(localFocusTraversalPolicy);
/*      */   }
/*      */ 
/*      */   private static FocusTraversalPolicy createLayoutPolicy() {
/*  431 */     FocusTraversalPolicy localFocusTraversalPolicy = null;
/*      */     try {
/*  433 */       Class localClass = Class.forName("javax.swing.LayoutFocusTraversalPolicy");
/*      */ 
/*  435 */       localFocusTraversalPolicy = (FocusTraversalPolicy)localClass.newInstance();
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/*  438 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*      */     }
/*      */     catch (InstantiationException localInstantiationException)
/*      */     {
/*  441 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*      */     }
/*      */     catch (IllegalAccessException localIllegalAccessException)
/*      */     {
/*  444 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/*      */ 
/*  447 */     return localFocusTraversalPolicy;
/*      */   }
/*      */ 
/*      */   public static void insertTargetMapping(Object paramObject, AppContext paramAppContext)
/*      */   {
/*  455 */     if (!setAppContext(paramObject, paramAppContext))
/*      */     {
/*  458 */       appContextMap.put(paramObject, paramAppContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void postEvent(AppContext paramAppContext, AWTEvent paramAWTEvent)
/*      */   {
/*  470 */     if (paramAWTEvent == null) {
/*  471 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  474 */     AWTAccessor.SequencedEventAccessor localSequencedEventAccessor = AWTAccessor.getSequencedEventAccessor();
/*  475 */     if ((localSequencedEventAccessor != null) && (localSequencedEventAccessor.isSequencedEvent(paramAWTEvent))) {
/*  476 */       localObject = localSequencedEventAccessor.getNested(paramAWTEvent);
/*  477 */       if ((((AWTEvent)localObject).getID() == 208) && ((localObject instanceof TimedWindowEvent)))
/*      */       {
/*  480 */         TimedWindowEvent localTimedWindowEvent = (TimedWindowEvent)localObject;
/*  481 */         ((SunToolkit)Toolkit.getDefaultToolkit()).setWindowDeactivationTime((Window)localTimedWindowEvent.getSource(), localTimedWindowEvent.getWhen());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  491 */     setSystemGenerated(paramAWTEvent);
/*  492 */     Object localObject = (PostEventQueue)paramAppContext.get("PostEventQueue");
/*      */ 
/*  494 */     if (localObject != null)
/*  495 */       ((PostEventQueue)localObject).postEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   public static void postPriorityEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  503 */     PeerEvent localPeerEvent = new PeerEvent(Toolkit.getDefaultToolkit(), new Runnable() {
/*      */       public void run() {
/*  505 */         AWTAccessor.getAWTEventAccessor().setPosted(this.val$e);
/*  506 */         ((Component)this.val$e.getSource()).dispatchEvent(this.val$e);
/*      */       }
/*      */     }
/*      */     , 2L);
/*      */ 
/*  509 */     postEvent(targetToAppContext(paramAWTEvent.getSource()), localPeerEvent);
/*      */   }
/*      */ 
/*      */   public static void flushPendingEvents()
/*      */   {
/*  520 */     AppContext localAppContext = AppContext.getAppContext();
/*  521 */     flushPendingEvents(localAppContext);
/*      */   }
/*      */ 
/*      */   public static void flushPendingEvents(AppContext paramAppContext) {
/*  525 */     flushLock.lock();
/*      */     try
/*      */     {
/*  528 */       if (!isFlushingPendingEvents) {
/*  529 */         isFlushingPendingEvents = true;
/*      */         try {
/*  531 */           PostEventQueue localPostEventQueue = (PostEventQueue)paramAppContext.get("PostEventQueue");
/*      */ 
/*  533 */           if (localPostEventQueue != null)
/*  534 */             localPostEventQueue.flush();
/*      */         }
/*      */         finally
/*      */         {
/*  538 */           isFlushingPendingEvents = false;
/*      */         }
/*      */       }
/*      */     } finally {
/*  542 */       flushLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean isPostEventQueueEmpty() {
/*  547 */     AppContext localAppContext = AppContext.getAppContext();
/*  548 */     PostEventQueue localPostEventQueue = (PostEventQueue)localAppContext.get("PostEventQueue");
/*      */ 
/*  550 */     if (localPostEventQueue != null) {
/*  551 */       return localPostEventQueue.noEvents();
/*      */     }
/*  553 */     return true;
/*      */   }
/*      */ 
/*      */   public static void executeOnEventHandlerThread(Object paramObject, Runnable paramRunnable)
/*      */   {
/*  564 */     executeOnEventHandlerThread(new PeerEvent(paramObject, paramRunnable, 1L));
/*      */   }
/*      */ 
/*      */   public static void executeOnEventHandlerThread(Object paramObject, Runnable paramRunnable, long paramLong)
/*      */   {
/*  574 */     executeOnEventHandlerThread(new PeerEvent(paramObject, paramRunnable, 1L) {
/*      */       public long getWhen() {
/*  576 */         return this.val$when;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static void executeOnEventHandlerThread(PeerEvent paramPeerEvent)
/*      */   {
/*  587 */     postEvent(targetToAppContext(paramPeerEvent.getSource()), paramPeerEvent);
/*      */   }
/*      */ 
/*      */   public static void invokeLaterOnAppContext(AppContext paramAppContext, Runnable paramRunnable)
/*      */   {
/*  601 */     postEvent(paramAppContext, new PeerEvent(Toolkit.getDefaultToolkit(), paramRunnable, 1L));
/*      */   }
/*      */ 
/*      */   public static void executeOnEDTAndWait(Object paramObject, Runnable paramRunnable)
/*      */     throws InterruptedException, InvocationTargetException
/*      */   {
/*  614 */     if (EventQueue.isDispatchThread()) {
/*  615 */       throw new Error("Cannot call executeOnEDTAndWait from any event dispatcher thread");
/*      */     }
/*      */ 
/*  619 */     Object local1AWTInvocationLock = new Object()
/*      */     {
/*      */     };
/*  621 */     PeerEvent localPeerEvent = new PeerEvent(paramObject, paramRunnable, local1AWTInvocationLock, true, 1L);
/*      */ 
/*  623 */     synchronized (local1AWTInvocationLock) {
/*  624 */       executeOnEventHandlerThread(localPeerEvent);
/*  625 */       while (!localPeerEvent.isDispatched()) {
/*  626 */         local1AWTInvocationLock.wait();
/*      */       }
/*      */     }
/*      */ 
/*  630 */     ??? = localPeerEvent.getThrowable();
/*  631 */     if (??? != null)
/*  632 */       throw new InvocationTargetException((Throwable)???);
/*      */   }
/*      */ 
/*      */   public static boolean isDispatchThreadForAppContext(Object paramObject)
/*      */   {
/*  643 */     AppContext localAppContext = targetToAppContext(paramObject);
/*  644 */     EventQueue localEventQueue = (EventQueue)localAppContext.get(AppContext.EVENT_QUEUE_KEY);
/*      */ 
/*  646 */     AWTAccessor.EventQueueAccessor localEventQueueAccessor = AWTAccessor.getEventQueueAccessor();
/*  647 */     return localEventQueueAccessor.isDispatchThreadImpl(localEventQueue);
/*      */   }
/*      */ 
/*      */   public Dimension getScreenSize() {
/*  651 */     return new Dimension(getScreenWidth(), getScreenHeight());
/*      */   }
/*      */   protected abstract int getScreenWidth();
/*      */ 
/*      */   protected abstract int getScreenHeight();
/*      */ 
/*  657 */   public FontMetrics getFontMetrics(Font paramFont) { return FontDesignMetrics.getMetrics(paramFont); }
/*      */ 
/*      */   public String[] getFontList()
/*      */   {
/*  661 */     String[] arrayOfString = { "Dialog", "SansSerif", "Serif", "Monospaced", "DialogInput" };
/*      */ 
/*  669 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public PanelPeer createPanel(Panel paramPanel) {
/*  673 */     return (PanelPeer)createComponent(paramPanel);
/*      */   }
/*      */ 
/*      */   public CanvasPeer createCanvas(Canvas paramCanvas) {
/*  677 */     return (CanvasPeer)createComponent(paramCanvas);
/*      */   }
/*      */ 
/*      */   public void disableBackgroundErase(Canvas paramCanvas)
/*      */   {
/*  688 */     disableBackgroundEraseImpl(paramCanvas);
/*      */   }
/*      */ 
/*      */   public void disableBackgroundErase(Component paramComponent)
/*      */   {
/*  701 */     disableBackgroundEraseImpl(paramComponent);
/*      */   }
/*      */ 
/*      */   private void disableBackgroundEraseImpl(Component paramComponent) {
/*  705 */     AWTAccessor.getComponentAccessor().setBackgroundEraseDisabled(paramComponent, true);
/*      */   }
/*      */ 
/*      */   public static boolean getSunAwtNoerasebackground()
/*      */   {
/*  713 */     return ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.awt.noerasebackground"))).booleanValue();
/*      */   }
/*      */ 
/*      */   public static boolean getSunAwtErasebackgroundonresize()
/*      */   {
/*  721 */     return ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.awt.erasebackgroundonresize"))).booleanValue();
/*      */   }
/*      */ 
/*      */   static Image getImageFromHash(Toolkit paramToolkit, URL paramURL)
/*      */   {
/*  728 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  729 */     if (localSecurityManager != null) {
/*      */       try {
/*  731 */         Permission localPermission = paramURL.openConnection().getPermission();
/*      */ 
/*  733 */         if (localPermission != null)
/*      */           try {
/*  735 */             localSecurityManager.checkPermission(localPermission);
/*      */           }
/*      */           catch (SecurityException localSecurityException)
/*      */           {
/*  739 */             if (((localPermission instanceof FilePermission)) && (localPermission.getActions().indexOf("read") != -1))
/*      */             {
/*  741 */               localSecurityManager.checkRead(localPermission.getName());
/*  742 */             } else if (((localPermission instanceof SocketPermission)) && (localPermission.getActions().indexOf("connect") != -1))
/*      */             {
/*  745 */               localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */             }
/*  747 */             else throw localSecurityException;
/*      */           }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  752 */         localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */       }
/*      */     }
/*  755 */     synchronized (imgCache) {
/*  756 */       Image localImage = (Image)imgCache.get(paramURL);
/*  757 */       if (localImage == null)
/*      */         try {
/*  759 */           localImage = paramToolkit.createImage(new URLImageSource(paramURL));
/*  760 */           imgCache.put(paramURL, localImage);
/*      */         }
/*      */         catch (Exception localException) {
/*      */         }
/*  764 */       return localImage;
/*      */     }
/*      */   }
/*      */ 
/*      */   static Image getImageFromHash(Toolkit paramToolkit, String paramString)
/*      */   {
/*  770 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  771 */     if (localSecurityManager != null) {
/*  772 */       localSecurityManager.checkRead(paramString);
/*      */     }
/*  774 */     synchronized (imgCache) {
/*  775 */       Image localImage = (Image)imgCache.get(paramString);
/*  776 */       if (localImage == null)
/*      */         try {
/*  778 */           localImage = paramToolkit.createImage(new FileImageSource(paramString));
/*  779 */           imgCache.put(paramString, localImage);
/*      */         }
/*      */         catch (Exception localException) {
/*      */         }
/*  783 */       return localImage;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Image getImage(String paramString) {
/*  788 */     return getImageFromHash(this, paramString);
/*      */   }
/*      */ 
/*      */   public Image getImage(URL paramURL) {
/*  792 */     return getImageFromHash(this, paramURL);
/*      */   }
/*      */ 
/*      */   public Image createImage(String paramString) {
/*  796 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  797 */     if (localSecurityManager != null) {
/*  798 */       localSecurityManager.checkRead(paramString);
/*      */     }
/*  800 */     return createImage(new FileImageSource(paramString));
/*      */   }
/*      */ 
/*      */   public Image createImage(URL paramURL) {
/*  804 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  805 */     if (localSecurityManager != null) {
/*      */       try {
/*  807 */         Permission localPermission = paramURL.openConnection().getPermission();
/*      */ 
/*  809 */         if (localPermission != null)
/*      */           try {
/*  811 */             localSecurityManager.checkPermission(localPermission);
/*      */           }
/*      */           catch (SecurityException localSecurityException)
/*      */           {
/*  815 */             if (((localPermission instanceof FilePermission)) && (localPermission.getActions().indexOf("read") != -1))
/*      */             {
/*  817 */               localSecurityManager.checkRead(localPermission.getName());
/*  818 */             } else if (((localPermission instanceof SocketPermission)) && (localPermission.getActions().indexOf("connect") != -1))
/*      */             {
/*  821 */               localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */             }
/*  823 */             else throw localSecurityException;
/*      */           }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  828 */         localSecurityManager.checkConnect(paramURL.getHost(), paramURL.getPort());
/*      */       }
/*      */     }
/*  831 */     return createImage(new URLImageSource(paramURL));
/*      */   }
/*      */ 
/*      */   public Image createImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  835 */     return createImage(new ByteArrayImageSource(paramArrayOfByte, paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public Image createImage(ImageProducer paramImageProducer) {
/*  839 */     return new ToolkitImage(paramImageProducer);
/*      */   }
/*      */ 
/*      */   public int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver) {
/*  843 */     if (!(paramImage instanceof ToolkitImage)) {
/*  844 */       return 32;
/*      */     }
/*      */ 
/*  847 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/*      */     int i;
/*  849 */     if ((paramInt1 == 0) || (paramInt2 == 0))
/*  850 */       i = 32;
/*      */     else {
/*  852 */       i = localToolkitImage.getImageRep().check(paramImageObserver);
/*      */     }
/*  854 */     return localToolkitImage.check(paramImageObserver) | i;
/*      */   }
/*      */ 
/*      */   public boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver) {
/*  858 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/*  859 */       return true;
/*      */     }
/*      */ 
/*  863 */     if (!(paramImage instanceof ToolkitImage)) {
/*  864 */       return true;
/*      */     }
/*      */ 
/*  867 */     ToolkitImage localToolkitImage = (ToolkitImage)paramImage;
/*  868 */     if (localToolkitImage.hasError()) {
/*  869 */       if (paramImageObserver != null) {
/*  870 */         paramImageObserver.imageUpdate(paramImage, 192, -1, -1, -1, -1);
/*      */       }
/*      */ 
/*  873 */       return false;
/*      */     }
/*  875 */     ImageRepresentation localImageRepresentation = localToolkitImage.getImageRep();
/*  876 */     return localImageRepresentation.prepare(paramImageObserver);
/*      */   }
/*      */ 
/*      */   public static BufferedImage getScaledIconImage(java.util.List<Image> paramList, int paramInt1, int paramInt2)
/*      */   {
/*  884 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/*  885 */       return null;
/*      */     }
/*  887 */     Object localObject1 = null;
/*  888 */     int i = 0;
/*  889 */     int j = 0;
/*  890 */     double d1 = 3.0D;
/*  891 */     double d2 = 0.0D;
/*  892 */     for (Object localObject2 = paramList.iterator(); ((Iterator)localObject2).hasNext(); )
/*      */     {
/*  898 */       localObject3 = (Image)((Iterator)localObject2).next();
/*  899 */       if (localObject3 != null)
/*      */       {
/*  902 */         if ((localObject3 instanceof ToolkitImage)) {
/*  903 */           ImageRepresentation localImageRepresentation = ((ToolkitImage)localObject3).getImageRep();
/*  904 */           localImageRepresentation.reconstruct(32);
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  909 */           k = ((Image)localObject3).getWidth(null);
/*  910 */           m = ((Image)localObject3).getHeight(null); } catch (Exception localException) {
/*      */         }
/*  912 */         continue;
/*      */ 
/*  914 */         if ((k > 0) && (m > 0))
/*      */         {
/*  916 */           double d3 = Math.min(paramInt1 / k, paramInt2 / m);
/*      */ 
/*  920 */           int n = 0;
/*  921 */           int i1 = 0;
/*  922 */           double d4 = 1.0D;
/*  923 */           if (d3 >= 2.0D)
/*      */           {
/*  926 */             d3 = Math.floor(d3);
/*  927 */             n = k * (int)d3;
/*  928 */             i1 = m * (int)d3;
/*  929 */             d4 = 1.0D - 0.5D / d3;
/*  930 */           } else if (d3 >= 1.0D)
/*      */           {
/*  932 */             d3 = 1.0D;
/*  933 */             n = k;
/*  934 */             i1 = m;
/*  935 */             d4 = 0.0D;
/*  936 */           } else if (d3 >= 0.75D)
/*      */           {
/*  938 */             d3 = 0.75D;
/*  939 */             n = k * 3 / 4;
/*  940 */             i1 = m * 3 / 4;
/*  941 */             d4 = 0.3D;
/*  942 */           } else if (d3 >= 0.6666D)
/*      */           {
/*  944 */             d3 = 0.6666D;
/*  945 */             n = k * 2 / 3;
/*  946 */             i1 = m * 2 / 3;
/*  947 */             d4 = 0.33D;
/*      */           }
/*      */           else
/*      */           {
/*  952 */             d5 = Math.ceil(1.0D / d3);
/*  953 */             d3 = 1.0D / d5;
/*  954 */             n = (int)Math.round(k / d5);
/*  955 */             i1 = (int)Math.round(m / d5);
/*  956 */             d4 = 1.0D - 1.0D / d5;
/*      */           }
/*  958 */           double d5 = (paramInt1 - n) / paramInt1 + (paramInt2 - i1) / paramInt2 + d4;
/*      */ 
/*  961 */           if (d5 < d1) {
/*  962 */             d1 = d5;
/*  963 */             d2 = d3;
/*  964 */             localObject1 = localObject3;
/*  965 */             i = n;
/*  966 */             j = i1;
/*      */           }
/*  968 */           if (d5 == 0.0D)
/*      */             break;
/*      */         }
/*      */       }
/*      */     }
/*      */     int k;
/*      */     int m;
/*  971 */     if (localObject1 == null)
/*      */     {
/*  973 */       return null;
/*      */     }
/*  975 */     localObject2 = new BufferedImage(paramInt1, paramInt2, 2);
/*      */ 
/*  977 */     Object localObject3 = ((BufferedImage)localObject2).createGraphics();
/*  978 */     ((Graphics2D)localObject3).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*      */     try
/*      */     {
/*  981 */       k = (paramInt1 - i) / 2;
/*  982 */       m = (paramInt2 - j) / 2;
/*  983 */       ((Graphics2D)localObject3).drawImage(localObject1, k, m, i, j, null);
/*      */     } finally {
/*  985 */       ((Graphics2D)localObject3).dispose();
/*      */     }
/*  987 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static DataBufferInt getScaledIconData(java.util.List<Image> paramList, int paramInt1, int paramInt2) {
/*  991 */     BufferedImage localBufferedImage = getScaledIconImage(paramList, paramInt1, paramInt2);
/*  992 */     if (localBufferedImage == null) {
/*  993 */       return null;
/*      */     }
/*  995 */     WritableRaster localWritableRaster = localBufferedImage.getRaster();
/*  996 */     DataBuffer localDataBuffer = localWritableRaster.getDataBuffer();
/*  997 */     return (DataBufferInt)localDataBuffer;
/*      */   }
/*      */ 
/*      */   protected EventQueue getSystemEventQueueImpl() {
/* 1001 */     return getSystemEventQueueImplPP();
/*      */   }
/*      */ 
/*      */   static EventQueue getSystemEventQueueImplPP()
/*      */   {
/* 1006 */     return getSystemEventQueueImplPP(AppContext.getAppContext());
/*      */   }
/*      */ 
/*      */   public static EventQueue getSystemEventQueueImplPP(AppContext paramAppContext) {
/* 1010 */     EventQueue localEventQueue = (EventQueue)paramAppContext.get(AppContext.EVENT_QUEUE_KEY);
/*      */ 
/* 1012 */     return localEventQueue;
/*      */   }
/*      */ 
/*      */   public static Container getNativeContainer(Component paramComponent)
/*      */   {
/* 1020 */     return Toolkit.getNativeContainer(paramComponent);
/*      */   }
/*      */ 
/*      */   public static Component getHeavyweightComponent(Component paramComponent)
/*      */   {
/* 1029 */     while ((paramComponent != null) && (AWTAccessor.getComponentAccessor().isLightweight(paramComponent))) {
/* 1030 */       paramComponent = AWTAccessor.getComponentAccessor().getParent(paramComponent);
/*      */     }
/* 1032 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public int getFocusAcceleratorKeyMask()
/*      */   {
/* 1039 */     return 8;
/*      */   }
/*      */ 
/*      */   public boolean isPrintableCharacterModifiersMask(int paramInt)
/*      */   {
/* 1049 */     return (paramInt & 0x8) == (paramInt & 0x2);
/*      */   }
/*      */ 
/*      */   public boolean canPopupOverlapTaskBar()
/*      */   {
/* 1058 */     boolean bool = true;
/*      */     try {
/* 1060 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1061 */       if (localSecurityManager != null) {
/* 1062 */         localSecurityManager.checkPermission(SecurityConstants.AWT.SET_WINDOW_ALWAYS_ON_TOP_PERMISSION);
/*      */       }
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/* 1067 */       bool = false;
/*      */     }
/* 1069 */     return bool;
/*      */   }
/*      */ 
/*      */   public Window createInputMethodWindow(String paramString, InputContext paramInputContext)
/*      */   {
/* 1083 */     return new SimpleInputMethodWindow(paramString, paramInputContext);
/*      */   }
/*      */ 
/*      */   public boolean enableInputMethodsForTextComponent()
/*      */   {
/* 1091 */     return false;
/*      */   }
/*      */ 
/*      */   public static Locale getStartupLocale()
/*      */   {
/* 1100 */     if (startupLocale == null)
/*      */     {
/* 1102 */       String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("user.language", "en"));
/*      */ 
/* 1105 */       String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("user.region"));
/*      */       String str3;
/*      */       String str4;
/* 1107 */       if (str2 != null)
/*      */       {
/* 1109 */         int i = str2.indexOf('_');
/* 1110 */         if (i >= 0) {
/* 1111 */           str3 = str2.substring(0, i);
/* 1112 */           str4 = str2.substring(i + 1);
/*      */         } else {
/* 1114 */           str3 = str2;
/* 1115 */           str4 = "";
/*      */         }
/*      */       } else {
/* 1118 */         str3 = (String)AccessController.doPrivileged(new GetPropertyAction("user.country", ""));
/*      */ 
/* 1120 */         str4 = (String)AccessController.doPrivileged(new GetPropertyAction("user.variant", ""));
/*      */       }
/*      */ 
/* 1123 */       startupLocale = new Locale(str1, str3, str4);
/*      */     }
/* 1125 */     return startupLocale;
/*      */   }
/*      */ 
/*      */   public Locale getDefaultKeyboardLocale()
/*      */   {
/* 1132 */     return getStartupLocale();
/*      */   }
/*      */ 
/*      */   protected static void setDataTransfererClassName(String paramString)
/*      */   {
/* 1138 */     dataTransfererClassName = paramString;
/*      */   }
/*      */ 
/*      */   public static String getDataTransfererClassName() {
/* 1142 */     if (dataTransfererClassName == null) {
/* 1143 */       Toolkit.getDefaultToolkit();
/*      */     }
/* 1145 */     return dataTransfererClassName;
/*      */   }
/*      */ 
/*      */   public WindowClosingListener getWindowClosingListener()
/*      */   {
/* 1154 */     return this.windowClosingListener;
/*      */   }
/*      */ 
/*      */   public void setWindowClosingListener(WindowClosingListener paramWindowClosingListener)
/*      */   {
/* 1160 */     this.windowClosingListener = paramWindowClosingListener;
/*      */   }
/*      */ 
/*      */   public RuntimeException windowClosingNotify(WindowEvent paramWindowEvent)
/*      */   {
/* 1167 */     if (this.windowClosingListener != null) {
/* 1168 */       return this.windowClosingListener.windowClosingNotify(paramWindowEvent);
/*      */     }
/* 1170 */     return null;
/*      */   }
/*      */ 
/*      */   public RuntimeException windowClosingDelivered(WindowEvent paramWindowEvent)
/*      */   {
/* 1177 */     if (this.windowClosingListener != null) {
/* 1178 */       return this.windowClosingListener.windowClosingDelivered(paramWindowEvent);
/*      */     }
/* 1180 */     return null;
/*      */   }
/*      */ 
/*      */   protected synchronized MouseInfoPeer getMouseInfoPeer()
/*      */   {
/* 1187 */     if (mPeer == null) {
/* 1188 */       mPeer = new DefaultMouseInfoPeer();
/*      */     }
/* 1190 */     return mPeer;
/*      */   }
/*      */ 
/*      */   public static boolean needsXEmbed()
/*      */   {
/* 1200 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.awt.noxembed", "false"));
/*      */ 
/* 1202 */     if ("true".equals(str)) {
/* 1203 */       return false;
/*      */     }
/*      */ 
/* 1206 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1207 */     if ((localToolkit instanceof SunToolkit))
/*      */     {
/* 1210 */       return ((SunToolkit)localToolkit).needsXEmbedImpl();
/*      */     }
/*      */ 
/* 1213 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean needsXEmbedImpl()
/*      */   {
/* 1223 */     return false;
/*      */   }
/*      */ 
/*      */   protected final boolean isXEmbedServerRequested()
/*      */   {
/* 1234 */     return ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.awt.xembedserver"))).booleanValue();
/*      */   }
/*      */ 
/*      */   public static boolean isModalExcludedSupported()
/*      */   {
/* 1251 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1252 */     return localToolkit.isModalExclusionTypeSupported(DEFAULT_MODAL_EXCLUSION_TYPE);
/*      */   }
/*      */ 
/*      */   protected boolean isModalExcludedSupportedImpl()
/*      */   {
/* 1264 */     return false;
/*      */   }
/*      */ 
/*      */   public static void setModalExcluded(Window paramWindow)
/*      */   {
/* 1285 */     if (DEFAULT_MODAL_EXCLUSION_TYPE == null) {
/* 1286 */       DEFAULT_MODAL_EXCLUSION_TYPE = Dialog.ModalExclusionType.APPLICATION_EXCLUDE;
/*      */     }
/* 1288 */     paramWindow.setModalExclusionType(DEFAULT_MODAL_EXCLUSION_TYPE);
/*      */   }
/*      */ 
/*      */   public static boolean isModalExcluded(Window paramWindow)
/*      */   {
/* 1309 */     if (DEFAULT_MODAL_EXCLUSION_TYPE == null) {
/* 1310 */       DEFAULT_MODAL_EXCLUSION_TYPE = Dialog.ModalExclusionType.APPLICATION_EXCLUDE;
/*      */     }
/* 1312 */     return paramWindow.getModalExclusionType().compareTo(DEFAULT_MODAL_EXCLUSION_TYPE) >= 0;
/*      */   }
/*      */ 
/*      */   public boolean isModalityTypeSupported(Dialog.ModalityType paramModalityType)
/*      */   {
/* 1319 */     return (paramModalityType == Dialog.ModalityType.MODELESS) || (paramModalityType == Dialog.ModalityType.APPLICATION_MODAL);
/*      */   }
/*      */ 
/*      */   public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType paramModalExclusionType)
/*      */   {
/* 1327 */     return paramModalExclusionType == Dialog.ModalExclusionType.NO_EXCLUDE;
/*      */   }
/*      */ 
/*      */   public void addModalityListener(ModalityListener paramModalityListener)
/*      */   {
/* 1341 */     this.modalityListeners.add(paramModalityListener);
/*      */   }
/*      */ 
/*      */   public void removeModalityListener(ModalityListener paramModalityListener) {
/* 1345 */     this.modalityListeners.remove(paramModalityListener);
/*      */   }
/*      */ 
/*      */   public void notifyModalityPushed(Dialog paramDialog) {
/* 1349 */     notifyModalityChange(1300, paramDialog);
/*      */   }
/*      */ 
/*      */   public void notifyModalityPopped(Dialog paramDialog) {
/* 1353 */     notifyModalityChange(1301, paramDialog);
/*      */   }
/*      */ 
/*      */   final void notifyModalityChange(int paramInt, Dialog paramDialog) {
/* 1357 */     ModalityEvent localModalityEvent = new ModalityEvent(paramDialog, this.modalityListeners, paramInt);
/* 1358 */     localModalityEvent.dispatch();
/*      */   }
/*      */ 
/*      */   public static boolean isLightweightOrUnknown(Component paramComponent)
/*      */   {
/* 1393 */     if ((paramComponent.isLightweight()) || (!(getDefaultToolkit() instanceof SunToolkit)))
/*      */     {
/* 1396 */       return true;
/*      */     }
/* 1398 */     return (!(paramComponent instanceof Button)) && (!(paramComponent instanceof Canvas)) && (!(paramComponent instanceof Checkbox)) && (!(paramComponent instanceof Choice)) && (!(paramComponent instanceof Label)) && (!(paramComponent instanceof java.awt.List)) && (!(paramComponent instanceof Panel)) && (!(paramComponent instanceof Scrollbar)) && (!(paramComponent instanceof ScrollPane)) && (!(paramComponent instanceof TextArea)) && (!(paramComponent instanceof TextField)) && (!(paramComponent instanceof Window));
/*      */   }
/*      */ 
/*      */   public void realSync()
/*      */     throws SunToolkit.OperationTimedOut, SunToolkit.InfiniteLoop
/*      */   {
/* 1439 */     realSync(10000L);
/*      */   }
/*      */ 
/*      */   public void realSync(long paramLong)
/*      */     throws SunToolkit.OperationTimedOut, SunToolkit.InfiniteLoop
/*      */   {
/* 1489 */     if (EventQueue.isDispatchThread()) {
/* 1490 */       throw new IllegalThreadException("The SunToolkit.realSync() method cannot be used on the event dispatch thread (EDT).");
/*      */     }
/* 1492 */     int i = 0;
/*      */     do
/*      */     {
/* 1495 */       sync();
/*      */ 
/* 1502 */       int j = 0;
/* 1503 */       while (j < 0) {
/* 1504 */         syncNativeQueue(paramLong);
/* 1505 */         j++;
/*      */       }
/* 1507 */       while ((syncNativeQueue(paramLong)) && (j < 20)) {
/* 1508 */         j++;
/*      */       }
/* 1510 */       if (j >= 20) {
/* 1511 */         throw new InfiniteLoop();
/*      */       }
/*      */ 
/* 1521 */       j = 0;
/* 1522 */       while (j < 0) {
/* 1523 */         waitForIdle(paramLong);
/* 1524 */         j++;
/*      */       }
/* 1526 */       while ((waitForIdle(paramLong)) && (j < 20)) {
/* 1527 */         j++;
/*      */       }
/* 1529 */       if (j >= 20) {
/* 1530 */         throw new InfiniteLoop();
/*      */       }
/*      */ 
/* 1533 */       i++;
/*      */     }
/*      */ 
/* 1537 */     while (((syncNativeQueue(paramLong)) || (waitForIdle(paramLong))) && (i < 20));
/*      */   }
/*      */ 
/*      */   protected abstract boolean syncNativeQueue(long paramLong);
/*      */ 
/*      */   private boolean isEQEmpty()
/*      */   {
/* 1555 */     EventQueue localEventQueue = getSystemEventQueueImpl();
/* 1556 */     return AWTAccessor.getEventQueueAccessor().noEvents(localEventQueue);
/*      */   }
/*      */ 
/*      */   protected final boolean waitForIdle(long paramLong)
/*      */   {
/* 1567 */     flushPendingEvents();
/* 1568 */     boolean bool = isEQEmpty();
/* 1569 */     this.queueEmpty = false;
/* 1570 */     this.eventDispatched = false;
/* 1571 */     synchronized (this.waitLock) {
/* 1572 */       postEvent(AppContext.getAppContext(), new PeerEvent(getSystemEventQueueImpl(), null, 4L)
/*      */       {
/*      */         public void dispatch()
/*      */         {
/* 1580 */           int i = 0;
/* 1581 */           while (i < 0) {
/* 1582 */             SunToolkit.this.syncNativeQueue(this.val$timeout);
/* 1583 */             i++;
/*      */           }
/* 1585 */           while ((SunToolkit.this.syncNativeQueue(this.val$timeout)) && (i < 20)) {
/* 1586 */             i++;
/*      */           }
/* 1588 */           SunToolkit.flushPendingEvents();
/*      */ 
/* 1590 */           synchronized (SunToolkit.this.waitLock) {
/* 1591 */             SunToolkit.this.queueEmpty = SunToolkit.this.isEQEmpty();
/* 1592 */             SunToolkit.this.eventDispatched = true;
/* 1593 */             SunToolkit.this.waitLock.notifyAll();
/*      */           }
/*      */         }
/*      */       });
/*      */       try {
/* 1598 */         while (!this.eventDispatched)
/* 1599 */           this.waitLock.wait();
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {
/* 1602 */         return false;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1607 */       Thread.sleep(0L);
/*      */     } catch (InterruptedException ) {
/* 1609 */       throw new RuntimeException("Interrupted");
/*      */     }
/*      */ 
/* 1612 */     flushPendingEvents();
/*      */ 
/* 1615 */     synchronized (this.waitLock) {
/* 1616 */       return (!this.queueEmpty) || (!isEQEmpty()) || (!bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract void grab(Window paramWindow);
/*      */ 
/*      */   public abstract void ungrab(Window paramWindow);
/*      */ 
/*      */   public static native void closeSplashScreen();
/*      */ 
/*      */   private void fireDesktopFontPropertyChanges()
/*      */   {
/* 1654 */     setDesktopProperty("awt.font.desktophints", getDesktopFontHints());
/*      */   }
/*      */ 
/*      */   public static void setAAFontSettingsCondition(boolean paramBoolean)
/*      */   {
/* 1688 */     if (paramBoolean != lastExtraCondition) {
/* 1689 */       lastExtraCondition = paramBoolean;
/* 1690 */       if (checkedSystemAAFontSettings)
/*      */       {
/* 1696 */         checkedSystemAAFontSettings = false;
/* 1697 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1698 */         if ((localToolkit instanceof SunToolkit))
/* 1699 */           ((SunToolkit)localToolkit).fireDesktopFontPropertyChanges();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static RenderingHints getDesktopAAHintsByName(String paramString)
/*      */   {
/* 1710 */     Object localObject = null;
/* 1711 */     paramString = paramString.toLowerCase(Locale.ENGLISH);
/* 1712 */     if (paramString.equals("on"))
/* 1713 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
/* 1714 */     else if (paramString.equals("gasp"))
/* 1715 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
/* 1716 */     else if ((paramString.equals("lcd")) || (paramString.equals("lcd_hrgb")))
/* 1717 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
/* 1718 */     else if (paramString.equals("lcd_hbgr"))
/* 1719 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
/* 1720 */     else if (paramString.equals("lcd_vrgb"))
/* 1721 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
/* 1722 */     else if (paramString.equals("lcd_vbgr")) {
/* 1723 */       localObject = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
/*      */     }
/* 1725 */     if (localObject != null) {
/* 1726 */       RenderingHints localRenderingHints = new RenderingHints(null);
/* 1727 */       localRenderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, localObject);
/* 1728 */       return localRenderingHints;
/*      */     }
/* 1730 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean useSystemAAFontSettings()
/*      */   {
/* 1743 */     if (!checkedSystemAAFontSettings) {
/* 1744 */       useSystemAAFontSettings = true;
/* 1745 */       String str = null;
/* 1746 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1747 */       if ((localToolkit instanceof SunToolkit)) {
/* 1748 */         str = (String)AccessController.doPrivileged(new GetPropertyAction("awt.useSystemAAFontSettings"));
/*      */       }
/*      */ 
/* 1752 */       if (str != null) {
/* 1753 */         useSystemAAFontSettings = Boolean.valueOf(str).booleanValue();
/*      */ 
/* 1758 */         if (!useSystemAAFontSettings) {
/* 1759 */           desktopFontHints = getDesktopAAHintsByName(str);
/*      */         }
/*      */       }
/*      */ 
/* 1763 */       if (useSystemAAFontSettings) {
/* 1764 */         useSystemAAFontSettings = lastExtraCondition;
/*      */       }
/* 1766 */       checkedSystemAAFontSettings = true;
/*      */     }
/* 1768 */     return useSystemAAFontSettings;
/*      */   }
/*      */ 
/*      */   protected RenderingHints getDesktopAAHints()
/*      */   {
/* 1776 */     return null;
/*      */   }
/*      */ 
/*      */   public static RenderingHints getDesktopFontHints()
/*      */   {
/* 1786 */     if (useSystemAAFontSettings()) {
/* 1787 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1788 */       if ((localToolkit instanceof SunToolkit)) {
/* 1789 */         RenderingHints localRenderingHints = ((SunToolkit)localToolkit).getDesktopAAHints();
/* 1790 */         return (RenderingHints)localRenderingHints;
/*      */       }
/* 1792 */       return null;
/*      */     }
/* 1794 */     if (desktopFontHints != null)
/*      */     {
/* 1798 */       return (RenderingHints)desktopFontHints.clone();
/*      */     }
/* 1800 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract boolean isDesktopSupported();
/*      */ 
/*      */   public static synchronized void consumeNextKeyTyped(KeyEvent paramKeyEvent)
/*      */   {
/*      */     try
/*      */     {
/* 1813 */       AWTAccessor.getDefaultKeyboardFocusManagerAccessor().consumeNextKeyTyped((DefaultKeyboardFocusManager)KeyboardFocusManager.getCurrentKeyboardFocusManager(), paramKeyEvent);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/* 1818 */       localClassCastException.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static void dumpPeers(PlatformLogger paramPlatformLogger) {
/* 1823 */     AWTAutoShutdown.getInstance().dumpPeers(paramPlatformLogger);
/*      */   }
/*      */ 
/*      */   public static Window getContainingWindow(Component paramComponent)
/*      */   {
/* 1832 */     while ((paramComponent != null) && (!(paramComponent instanceof Window))) {
/* 1833 */       paramComponent = paramComponent.getParent();
/*      */     }
/* 1835 */     return (Window)paramComponent;
/*      */   }
/*      */ 
/*      */   public static synchronized boolean getSunAwtDisableMixing()
/*      */   {
/* 1845 */     if (sunAwtDisableMixing == null) {
/* 1846 */       sunAwtDisableMixing = (Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.awt.disableMixing"));
/*      */     }
/*      */ 
/* 1849 */     return sunAwtDisableMixing.booleanValue();
/*      */   }
/*      */ 
/*      */   public boolean isNativeGTKAvailable()
/*      */   {
/* 1858 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized void setWindowDeactivationTime(Window paramWindow, long paramLong)
/*      */   {
/* 1864 */     AppContext localAppContext = getAppContext(paramWindow);
/* 1865 */     WeakHashMap localWeakHashMap = (WeakHashMap)localAppContext.get(DEACTIVATION_TIMES_MAP_KEY);
/* 1866 */     if (localWeakHashMap == null) {
/* 1867 */       localWeakHashMap = new WeakHashMap();
/* 1868 */       localAppContext.put(DEACTIVATION_TIMES_MAP_KEY, localWeakHashMap);
/*      */     }
/* 1870 */     localWeakHashMap.put(paramWindow, Long.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public synchronized long getWindowDeactivationTime(Window paramWindow) {
/* 1874 */     AppContext localAppContext = getAppContext(paramWindow);
/* 1875 */     WeakHashMap localWeakHashMap = (WeakHashMap)localAppContext.get(DEACTIVATION_TIMES_MAP_KEY);
/* 1876 */     if (localWeakHashMap == null) {
/* 1877 */       return -1L;
/*      */     }
/* 1879 */     Long localLong = (Long)localWeakHashMap.get(paramWindow);
/* 1880 */     return localLong == null ? -1L : localLong.longValue();
/*      */   }
/*      */ 
/*      */   public boolean isWindowOpacitySupported()
/*      */   {
/* 1885 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isWindowShapingSupported()
/*      */   {
/* 1890 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isWindowTranslucencySupported()
/*      */   {
/* 1895 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isTranslucencyCapable(GraphicsConfiguration paramGraphicsConfiguration) {
/* 1899 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isSwingBackbufferTranslucencySupported()
/*      */   {
/* 1906 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean isContainingTopLevelOpaque(Component paramComponent)
/*      */   {
/* 1921 */     Window localWindow = getContainingWindow(paramComponent);
/* 1922 */     return (localWindow != null) && (localWindow.isOpaque());
/*      */   }
/*      */ 
/*      */   public static boolean isContainingTopLevelTranslucent(Component paramComponent)
/*      */   {
/* 1937 */     Window localWindow = getContainingWindow(paramComponent);
/* 1938 */     return (localWindow != null) && (localWindow.getOpacity() < 1.0F);
/*      */   }
/*      */ 
/*      */   public boolean needUpdateWindow()
/*      */   {
/* 1949 */     return false;
/*      */   }
/*      */ 
/*      */   public int getNumberOfButtons()
/*      */   {
/* 1956 */     return 3;
/*      */   }
/*      */ 
/*      */   public static boolean isInstanceOf(Object paramObject, String paramString)
/*      */   {
/* 1974 */     if (paramObject == null) return false;
/* 1975 */     if (paramString == null) return false;
/*      */ 
/* 1977 */     return isInstanceOf(paramObject.getClass(), paramString);
/*      */   }
/*      */ 
/*      */   private static boolean isInstanceOf(Class paramClass, String paramString) {
/* 1981 */     if (paramClass == null) return false;
/*      */ 
/* 1983 */     if (paramClass.getName().equals(paramString)) {
/* 1984 */       return true;
/*      */     }
/*      */ 
/* 1987 */     for (Class localClass : paramClass.getInterfaces()) {
/* 1988 */       if (localClass.getName().equals(paramString)) {
/* 1989 */         return true;
/*      */       }
/*      */     }
/* 1992 */     return isInstanceOf(paramClass.getSuperclass(), paramString);
/*      */   }
/*      */ 
/*      */   public static void setSystemGenerated(AWTEvent paramAWTEvent)
/*      */   {
/* 2006 */     AWTAccessor.getAWTEventAccessor().setSystemGenerated(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   public static boolean isSystemGenerated(AWTEvent paramAWTEvent) {
/* 2010 */     return AWTAccessor.getAWTEventAccessor().isSystemGenerated(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   66 */     if (((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.awt.nativedebug"))).booleanValue())
/*   67 */       DebugSettings.init();
/*      */   }
/*      */ 
/*      */   public static class IllegalThreadException extends RuntimeException
/*      */   {
/*      */     public IllegalThreadException(String paramString)
/*      */     {
/* 1424 */       super();
/*      */     }
/*      */ 
/*      */     public IllegalThreadException()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InfiniteLoop extends RuntimeException
/*      */   {
/*      */   }
/*      */ 
/*      */   static class ModalityListenerList
/*      */     implements ModalityListener
/*      */   {
/* 1363 */     Vector<ModalityListener> listeners = new Vector();
/*      */ 
/*      */     void add(ModalityListener paramModalityListener) {
/* 1366 */       this.listeners.addElement(paramModalityListener);
/*      */     }
/*      */ 
/*      */     void remove(ModalityListener paramModalityListener) {
/* 1370 */       this.listeners.removeElement(paramModalityListener);
/*      */     }
/*      */ 
/*      */     public void modalityPushed(ModalityEvent paramModalityEvent) {
/* 1374 */       Iterator localIterator = this.listeners.iterator();
/* 1375 */       while (localIterator.hasNext())
/* 1376 */         ((ModalityListener)localIterator.next()).modalityPushed(paramModalityEvent);
/*      */     }
/*      */ 
/*      */     public void modalityPopped(ModalityEvent paramModalityEvent)
/*      */     {
/* 1381 */       Iterator localIterator = this.listeners.iterator();
/* 1382 */       while (localIterator.hasNext())
/* 1383 */         ((ModalityListener)localIterator.next()).modalityPopped(paramModalityEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class OperationTimedOut extends RuntimeException
/*      */   {
/*      */     public OperationTimedOut(String paramString)
/*      */     {
/* 1414 */       super();
/*      */     }
/*      */ 
/*      */     public OperationTimedOut()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.SunToolkit
 * JD-Core Version:    0.6.2
 */