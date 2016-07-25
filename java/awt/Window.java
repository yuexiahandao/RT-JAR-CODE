/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.awt.event.WindowStateListener;
/*      */ import java.awt.geom.Path2D.Float;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Double;
/*      */ import java.awt.im.InputContext;
/*      */ import java.awt.image.BufferStrategy;
/*      */ import java.awt.peer.WindowPeer;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OptionalDataException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.EventListener;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.RootPaneContainer;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.WindowAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.util.IdentityArrayList;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ import sun.java2d.pipe.Region;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.security.util.SecurityConstants.AWT;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class Window extends Container
/*      */   implements Accessible
/*      */ {
/*      */   String warningString;
/*      */   transient List<Image> icons;
/*      */   private transient Component temporaryLostComponent;
/*  226 */   static boolean systemSyncLWRequests = false;
/*  227 */   boolean syncLWRequests = false;
/*  228 */   transient boolean beforeFirstShow = true;
/*  229 */   private transient boolean disposing = false;
/*      */   static final int OPENED = 1;
/*      */   int state;
/*      */   private boolean alwaysOnTop;
/*  258 */   private static final IdentityArrayList<Window> allWindows = new IdentityArrayList();
/*      */ 
/*  266 */   transient Vector<WeakReference<Window>> ownedWindowList = new Vector();
/*      */   private transient WeakReference<Window> weakThis;
/*      */   transient boolean showWithParent;
/*      */   transient Dialog modalBlocker;
/*      */   Dialog.ModalExclusionType modalExclusionType;
/*      */   transient WindowListener windowListener;
/*      */   transient WindowStateListener windowStateListener;
/*      */   transient WindowFocusListener windowFocusListener;
/*      */   transient InputContext inputContext;
/*  302 */   private transient Object inputContextLock = new Object();
/*      */   private FocusManager focusMgr;
/*  320 */   private boolean focusableWindowState = true;
/*      */ 
/*  332 */   private volatile boolean autoRequestFocus = true;
/*      */ 
/*  341 */   transient boolean isInShow = false;
/*      */ 
/*  351 */   private float opacity = 1.0F;
/*      */ 
/*  362 */   private Shape shape = null;
/*      */   private static final String base = "win";
/*  365 */   private static int nameCounter = 0;
/*      */   private static final long serialVersionUID = 4497834738069338734L;
/*  372 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Window");
/*      */   private static final boolean locationByPlatformProp;
/*  376 */   transient boolean isTrayIconWindow = false;
/*      */ 
/*  382 */   private volatile transient int securityWarningWidth = 0;
/*  383 */   private volatile transient int securityWarningHeight = 0;
/*      */ 
/*  390 */   private transient double securityWarningPointX = 2.0D;
/*  391 */   private transient double securityWarningPointY = 0.0D;
/*  392 */   private transient float securityWarningAlignmentX = 1.0F;
/*  393 */   private transient float securityWarningAlignmentY = 0.0F;
/*      */ 
/*  441 */   transient Object anchor = new Object();
/*      */   private static final AtomicBoolean beforeFirstWindowShown;
/* 2822 */   private Type type = Type.NORMAL;
/*      */ 
/* 2868 */   private int windowSerializedDataVersion = 2;
/*      */ 
/* 3366 */   private boolean locationByPlatform = locationByPlatformProp;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   Window(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  438 */     init(paramGraphicsConfiguration);
/*      */   }
/*      */ 
/*      */   private GraphicsConfiguration initGC(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  464 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/*  466 */     if (paramGraphicsConfiguration == null) {
/*  467 */       paramGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */     }
/*      */ 
/*  470 */     setGraphicsConfiguration(paramGraphicsConfiguration);
/*      */ 
/*  472 */     return paramGraphicsConfiguration;
/*      */   }
/*      */ 
/*      */   private void init(GraphicsConfiguration paramGraphicsConfiguration) {
/*  476 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/*  478 */     this.syncLWRequests = systemSyncLWRequests;
/*      */ 
/*  480 */     this.weakThis = new WeakReference(this);
/*  481 */     addToWindowList();
/*      */ 
/*  483 */     setWarningString();
/*  484 */     this.cursor = Cursor.getPredefinedCursor(0);
/*  485 */     this.visible = false;
/*      */ 
/*  487 */     paramGraphicsConfiguration = initGC(paramGraphicsConfiguration);
/*      */ 
/*  489 */     if (paramGraphicsConfiguration.getDevice().getType() != 0)
/*      */     {
/*  491 */       throw new IllegalArgumentException("not a screen device");
/*      */     }
/*  493 */     setLayout(new BorderLayout());
/*      */ 
/*  497 */     Rectangle localRectangle = paramGraphicsConfiguration.getBounds();
/*  498 */     Insets localInsets = getToolkit().getScreenInsets(paramGraphicsConfiguration);
/*  499 */     int i = getX() + localRectangle.x + localInsets.left;
/*  500 */     int j = getY() + localRectangle.y + localInsets.top;
/*  501 */     if ((i != this.x) || (j != this.y)) {
/*  502 */       setLocation(i, j);
/*      */ 
/*  504 */       setLocationByPlatform(locationByPlatformProp);
/*      */     }
/*      */ 
/*  507 */     this.modalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
/*      */ 
/*  509 */     SunToolkit.checkAndSetPolicy(this);
/*      */   }
/*      */ 
/*      */   Window()
/*      */     throws HeadlessException
/*      */   {
/*  535 */     GraphicsEnvironment.checkHeadless();
/*  536 */     init((GraphicsConfiguration)null);
/*      */   }
/*      */ 
/*      */   public Window(Frame paramFrame)
/*      */   {
/*  562 */     this(paramFrame == null ? (GraphicsConfiguration)null : paramFrame.getGraphicsConfiguration());
/*      */ 
/*  564 */     ownedInit(paramFrame);
/*      */   }
/*      */ 
/*      */   public Window(Window paramWindow)
/*      */   {
/*  594 */     this(paramWindow == null ? (GraphicsConfiguration)null : paramWindow.getGraphicsConfiguration());
/*      */ 
/*  596 */     ownedInit(paramWindow);
/*      */   }
/*      */ 
/*      */   public Window(Window paramWindow, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  630 */     this(paramGraphicsConfiguration);
/*  631 */     ownedInit(paramWindow);
/*      */   }
/*      */ 
/*      */   private void ownedInit(Window paramWindow) {
/*  635 */     this.parent = paramWindow;
/*  636 */     if (paramWindow != null) {
/*  637 */       paramWindow.addOwnedWindow(this.weakThis);
/*      */     }
/*      */ 
/*  642 */     Disposer.addRecord(this.anchor, new WindowDisposerRecord(this.appContext, this));
/*      */   }
/*      */ 
/*      */   String constructComponentName()
/*      */   {
/*  650 */     synchronized (Window.class) {
/*  651 */       return "win" + nameCounter++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<Image> getIconImages()
/*      */   {
/*  668 */     List localList = this.icons;
/*  669 */     if ((localList == null) || (localList.size() == 0)) {
/*  670 */       return new ArrayList();
/*      */     }
/*  672 */     return new ArrayList(localList);
/*      */   }
/*      */ 
/*      */   public synchronized void setIconImages(List<? extends Image> paramList)
/*      */   {
/*  704 */     this.icons = (paramList == null ? new ArrayList() : new ArrayList(paramList));
/*      */ 
/*  706 */     WindowPeer localWindowPeer = (WindowPeer)this.peer;
/*  707 */     if (localWindowPeer != null) {
/*  708 */       localWindowPeer.updateIconImages();
/*      */     }
/*      */ 
/*  711 */     firePropertyChange("iconImage", null, null);
/*      */   }
/*      */ 
/*      */   public void setIconImage(Image paramImage)
/*      */   {
/*  742 */     ArrayList localArrayList = new ArrayList();
/*  743 */     if (paramImage != null) {
/*  744 */       localArrayList.add(paramImage);
/*      */     }
/*  746 */     setIconImages(localArrayList);
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  759 */     synchronized (getTreeLock()) {
/*  760 */       Container localContainer = this.parent;
/*  761 */       if ((localContainer != null) && (localContainer.getPeer() == null)) {
/*  762 */         localContainer.addNotify();
/*      */       }
/*  764 */       if (this.peer == null) {
/*  765 */         this.peer = getToolkit().createWindow(this);
/*      */       }
/*  767 */       synchronized (allWindows) {
/*  768 */         allWindows.add(this);
/*      */       }
/*  770 */       super.addNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  778 */     synchronized (getTreeLock()) {
/*  779 */       synchronized (allWindows) {
/*  780 */         allWindows.remove(this);
/*      */       }
/*  782 */       super.removeNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void pack()
/*      */   {
/*  802 */     Container localContainer = this.parent;
/*  803 */     if ((localContainer != null) && (localContainer.getPeer() == null)) {
/*  804 */       localContainer.addNotify();
/*      */     }
/*  806 */     if (this.peer == null) {
/*  807 */       addNotify();
/*      */     }
/*  809 */     Dimension localDimension = getPreferredSize();
/*  810 */     if (this.peer != null) {
/*  811 */       setClientSize(localDimension.width, localDimension.height);
/*      */     }
/*      */ 
/*  814 */     if (this.beforeFirstShow) {
/*  815 */       this.isPacked = true;
/*      */     }
/*      */ 
/*  818 */     validateUnconditionally();
/*      */   }
/*      */ 
/*      */   public void setMinimumSize(Dimension paramDimension)
/*      */   {
/*  851 */     synchronized (getTreeLock()) {
/*  852 */       super.setMinimumSize(paramDimension);
/*  853 */       Dimension localDimension = getSize();
/*  854 */       if ((isMinimumSizeSet()) && (
/*  855 */         (localDimension.width < paramDimension.width) || (localDimension.height < paramDimension.height))) {
/*  856 */         int i = Math.max(this.width, paramDimension.width);
/*  857 */         int j = Math.max(this.height, paramDimension.height);
/*  858 */         setSize(i, j);
/*      */       }
/*      */ 
/*  861 */       if (this.peer != null)
/*  862 */         ((WindowPeer)this.peer).updateMinimumSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSize(Dimension paramDimension)
/*      */   {
/*  886 */     super.setSize(paramDimension);
/*      */   }
/*      */ 
/*      */   public void setSize(int paramInt1, int paramInt2)
/*      */   {
/*  908 */     super.setSize(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setLocation(int paramInt1, int paramInt2)
/*      */   {
/*  921 */     super.setLocation(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setLocation(Point paramPoint)
/*      */   {
/*  934 */     super.setLocation(paramPoint);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  943 */     if (isMinimumSizeSet()) {
/*  944 */       Dimension localDimension = getMinimumSize();
/*  945 */       if (paramInt3 < localDimension.width) {
/*  946 */         paramInt3 = localDimension.width;
/*      */       }
/*  948 */       if (paramInt4 < localDimension.height) {
/*  949 */         paramInt4 = localDimension.height;
/*      */       }
/*      */     }
/*  952 */     super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   void setClientSize(int paramInt1, int paramInt2) {
/*  956 */     synchronized (getTreeLock()) {
/*  957 */       setBoundsOp(4);
/*  958 */       setBounds(this.x, this.y, paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   final void closeSplashScreen()
/*      */   {
/*  966 */     if (this.isTrayIconWindow) {
/*  967 */       return;
/*      */     }
/*  969 */     if (beforeFirstWindowShown.getAndSet(false))
/*      */     {
/*  972 */       SunToolkit.closeSplashScreen();
/*  973 */       SplashScreen.markClosed();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/* 1014 */     super.setVisible(paramBoolean);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void show()
/*      */   {
/* 1030 */     if (this.peer == null) {
/* 1031 */       addNotify();
/*      */     }
/* 1033 */     validateUnconditionally();
/*      */ 
/* 1035 */     this.isInShow = true;
/* 1036 */     if (this.visible) {
/* 1037 */       toFront();
/*      */     } else {
/* 1039 */       this.beforeFirstShow = false;
/* 1040 */       closeSplashScreen();
/* 1041 */       Dialog.checkShouldBeBlocked(this);
/* 1042 */       super.show();
/* 1043 */       this.locationByPlatform = false;
/* 1044 */       for (int i = 0; i < this.ownedWindowList.size(); i++) {
/* 1045 */         Window localWindow = (Window)((WeakReference)this.ownedWindowList.elementAt(i)).get();
/* 1046 */         if ((localWindow != null) && (localWindow.showWithParent)) {
/* 1047 */           localWindow.show();
/* 1048 */           localWindow.showWithParent = false;
/*      */         }
/*      */       }
/* 1051 */       if (!isModalBlocked()) {
/* 1052 */         updateChildrenBlocking();
/*      */       }
/*      */       else
/*      */       {
/* 1056 */         this.modalBlocker.toFront_NoClientCode();
/*      */       }
/* 1058 */       if (((this instanceof Frame)) || ((this instanceof Dialog))) {
/* 1059 */         updateChildFocusableWindowState(this);
/*      */       }
/*      */     }
/* 1062 */     this.isInShow = false;
/*      */ 
/* 1065 */     if ((this.state & 0x1) == 0) {
/* 1066 */       postWindowEvent(200);
/* 1067 */       this.state |= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static void updateChildFocusableWindowState(Window paramWindow) {
/* 1072 */     if ((paramWindow.getPeer() != null) && (paramWindow.isShowing())) {
/* 1073 */       ((WindowPeer)paramWindow.getPeer()).updateFocusableWindowState();
/*      */     }
/* 1075 */     for (int i = 0; i < paramWindow.ownedWindowList.size(); i++) {
/* 1076 */       Window localWindow = (Window)((WeakReference)paramWindow.ownedWindowList.elementAt(i)).get();
/* 1077 */       if (localWindow != null)
/* 1078 */         updateChildFocusableWindowState(localWindow);
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void postWindowEvent(int paramInt)
/*      */   {
/* 1084 */     if ((this.windowListener != null) || ((this.eventMask & 0x40) != 0L) || (Toolkit.enabledOnToolkit(64L)))
/*      */     {
/* 1087 */       WindowEvent localWindowEvent = new WindowEvent(this, paramInt);
/* 1088 */       Toolkit.getEventQueue().postEvent(localWindowEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void hide()
/*      */   {
/* 1104 */     synchronized (this.ownedWindowList) {
/* 1105 */       for (int i = 0; i < this.ownedWindowList.size(); i++) {
/* 1106 */         Window localWindow = (Window)((WeakReference)this.ownedWindowList.elementAt(i)).get();
/* 1107 */         if ((localWindow != null) && (localWindow.visible)) {
/* 1108 */           localWindow.hide();
/* 1109 */           localWindow.showWithParent = true;
/*      */         }
/*      */       }
/*      */     }
/* 1113 */     if (isModalBlocked()) {
/* 1114 */       this.modalBlocker.unblockWindow(this);
/*      */     }
/* 1116 */     super.hide();
/*      */   }
/*      */ 
/*      */   final void clearMostRecentFocusOwnerOnHide()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1147 */     doDispose();
/*      */   }
/*      */ 
/*      */   void disposeImpl()
/*      */   {
/* 1157 */     dispose();
/* 1158 */     if (getPeer() != null)
/* 1159 */       doDispose();
/*      */   }
/*      */ 
/*      */   void doDispose()
/*      */   {
/* 1203 */     Runnable local1DisposeAction = new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/* 1166 */         Window.this.disposing = true;
/*      */         try
/*      */         {
/* 1171 */           GraphicsDevice localGraphicsDevice = Window.this.getGraphicsConfiguration().getDevice();
/* 1172 */           if (localGraphicsDevice.getFullScreenWindow() == Window.this)
/* 1173 */             localGraphicsDevice.setFullScreenWindow(null);
/*      */           Object[] arrayOfObject;
/* 1177 */           synchronized (Window.this.ownedWindowList) {
/* 1178 */             arrayOfObject = new Object[Window.this.ownedWindowList.size()];
/* 1179 */             Window.this.ownedWindowList.copyInto(arrayOfObject);
/*      */           }
/* 1181 */           for (??? = 0; ??? < arrayOfObject.length; ???++) {
/* 1182 */             Window localWindow = (Window)((WeakReference)arrayOfObject[???]).get();
/*      */ 
/* 1184 */             if (localWindow != null) {
/* 1185 */               localWindow.disposeImpl();
/*      */             }
/*      */           }
/* 1188 */           Window.this.hide();
/* 1189 */           Window.this.beforeFirstShow = true;
/* 1190 */           Window.this.removeNotify();
/* 1191 */           synchronized (Window.this.inputContextLock) {
/* 1192 */             if (Window.this.inputContext != null) {
/* 1193 */               Window.this.inputContext.dispose();
/* 1194 */               Window.this.inputContext = null;
/*      */             }
/*      */           }
/* 1197 */           Window.this.clearCurrentFocusCycleRootOnHide();
/*      */         } finally {
/* 1199 */           Window.this.disposing = false;
/*      */         }
/*      */       }
/*      */     };
/* 1204 */     if (EventQueue.isDispatchThread())
/* 1205 */       local1DisposeAction.run();
/*      */     else {
/*      */       try
/*      */       {
/* 1209 */         EventQueue.invokeAndWait(this, local1DisposeAction);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {
/* 1212 */         System.err.println("Disposal was interrupted:");
/* 1213 */         localInterruptedException.printStackTrace();
/*      */       }
/*      */       catch (InvocationTargetException localInvocationTargetException) {
/* 1216 */         System.err.println("Exception during disposal:");
/* 1217 */         localInvocationTargetException.printStackTrace();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1223 */     postWindowEvent(202);
/*      */   }
/*      */ 
/*      */   void adjustListeningChildrenOnParent(long paramLong, int paramInt)
/*      */   {
/*      */   }
/*      */ 
/*      */   void adjustDecendantsOnParent(int paramInt)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void toFront()
/*      */   {
/* 1284 */     toFront_NoClientCode();
/*      */   }
/*      */ 
/*      */   final void toFront_NoClientCode()
/*      */   {
/* 1290 */     if (this.visible) {
/* 1291 */       WindowPeer localWindowPeer = (WindowPeer)this.peer;
/* 1292 */       if (localWindowPeer != null) {
/* 1293 */         localWindowPeer.toFront();
/*      */       }
/* 1295 */       if (isModalBlocked())
/* 1296 */         this.modalBlocker.toFront_NoClientCode();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void toBack()
/*      */   {
/* 1327 */     toBack_NoClientCode();
/*      */   }
/*      */ 
/*      */   final void toBack_NoClientCode()
/*      */   {
/* 1333 */     if (isAlwaysOnTop())
/*      */       try {
/* 1335 */         setAlwaysOnTop(false);
/*      */       }
/*      */       catch (SecurityException localSecurityException) {
/*      */       }
/* 1339 */     if (this.visible) {
/* 1340 */       WindowPeer localWindowPeer = (WindowPeer)this.peer;
/* 1341 */       if (localWindowPeer != null)
/* 1342 */         localWindowPeer.toBack();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Toolkit getToolkit()
/*      */   {
/* 1355 */     return Toolkit.getDefaultToolkit();
/*      */   }
/*      */ 
/*      */   public final String getWarningString()
/*      */   {
/* 1376 */     return this.warningString;
/*      */   }
/*      */ 
/*      */   private void setWarningString() {
/* 1380 */     this.warningString = null;
/* 1381 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1382 */     if ((localSecurityManager != null) && 
/* 1383 */       (!localSecurityManager.checkTopLevelWindow(this)))
/*      */     {
/* 1387 */       this.warningString = ((String)AccessController.doPrivileged(new GetPropertyAction("awt.appletWarning", "Java Applet Window")));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/* 1404 */     if (this.locale == null) {
/* 1405 */       return Locale.getDefault();
/*      */     }
/* 1407 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public InputContext getInputContext()
/*      */   {
/* 1417 */     synchronized (this.inputContextLock) {
/* 1418 */       if (this.inputContext == null) {
/* 1419 */         this.inputContext = InputContext.getInstance();
/*      */       }
/*      */     }
/* 1422 */     return this.inputContext;
/*      */   }
/*      */ 
/*      */   public void setCursor(Cursor paramCursor)
/*      */   {
/* 1440 */     if (paramCursor == null) {
/* 1441 */       paramCursor = Cursor.getPredefinedCursor(0);
/*      */     }
/* 1443 */     super.setCursor(paramCursor);
/*      */   }
/*      */ 
/*      */   public Window getOwner()
/*      */   {
/* 1451 */     return getOwner_NoClientCode();
/*      */   }
/*      */   final Window getOwner_NoClientCode() {
/* 1454 */     return (Window)this.parent;
/*      */   }
/*      */ 
/*      */   public Window[] getOwnedWindows()
/*      */   {
/* 1463 */     return getOwnedWindows_NoClientCode();
/*      */   }
/*      */ 
/*      */   final Window[] getOwnedWindows_NoClientCode()
/*      */   {
/*      */     Window[] arrayOfWindow1;
/* 1468 */     synchronized (this.ownedWindowList)
/*      */     {
/* 1474 */       int i = this.ownedWindowList.size();
/* 1475 */       int j = 0;
/* 1476 */       Window[] arrayOfWindow2 = new Window[i];
/*      */ 
/* 1478 */       for (int k = 0; k < i; k++) {
/* 1479 */         arrayOfWindow2[j] = ((Window)((WeakReference)this.ownedWindowList.elementAt(k)).get());
/*      */ 
/* 1481 */         if (arrayOfWindow2[j] != null) {
/* 1482 */           j++;
/*      */         }
/*      */       }
/*      */ 
/* 1486 */       if (i != j)
/* 1487 */         arrayOfWindow1 = (Window[])Arrays.copyOf(arrayOfWindow2, j);
/*      */       else {
/* 1489 */         arrayOfWindow1 = arrayOfWindow2;
/*      */       }
/*      */     }
/*      */ 
/* 1493 */     return arrayOfWindow1;
/*      */   }
/*      */ 
/*      */   boolean isModalBlocked() {
/* 1497 */     return this.modalBlocker != null;
/*      */   }
/*      */ 
/*      */   void setModalBlocked(Dialog paramDialog, boolean paramBoolean1, boolean paramBoolean2) {
/* 1501 */     this.modalBlocker = (paramBoolean1 ? paramDialog : null);
/* 1502 */     if (paramBoolean2) {
/* 1503 */       WindowPeer localWindowPeer = (WindowPeer)this.peer;
/* 1504 */       if (localWindowPeer != null)
/* 1505 */         localWindowPeer.setModalBlocked(paramDialog, paramBoolean1);
/*      */     }
/*      */   }
/*      */ 
/*      */   Dialog getModalBlocker()
/*      */   {
/* 1511 */     return this.modalBlocker;
/*      */   }
/*      */ 
/*      */   static IdentityArrayList<Window> getAllWindows()
/*      */   {
/* 1522 */     synchronized (allWindows) {
/* 1523 */       IdentityArrayList localIdentityArrayList = new IdentityArrayList();
/* 1524 */       localIdentityArrayList.addAll(allWindows);
/* 1525 */       return localIdentityArrayList;
/*      */     }
/*      */   }
/*      */ 
/*      */   static IdentityArrayList<Window> getAllUnblockedWindows() {
/* 1530 */     synchronized (allWindows) {
/* 1531 */       IdentityArrayList localIdentityArrayList = new IdentityArrayList();
/* 1532 */       for (int i = 0; i < allWindows.size(); i++) {
/* 1533 */         Window localWindow = (Window)allWindows.get(i);
/* 1534 */         if (!localWindow.isModalBlocked()) {
/* 1535 */           localIdentityArrayList.add(localWindow);
/*      */         }
/*      */       }
/* 1538 */       return localIdentityArrayList;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Window[] getWindows(AppContext paramAppContext) {
/* 1543 */     synchronized (Window.class)
/*      */     {
/* 1545 */       Vector localVector = (Vector)paramAppContext.get(Window.class);
/*      */       Window[] arrayOfWindow1;
/* 1547 */       if (localVector != null) {
/* 1548 */         int i = localVector.size();
/* 1549 */         int j = 0;
/* 1550 */         Window[] arrayOfWindow2 = new Window[i];
/* 1551 */         for (int k = 0; k < i; k++) {
/* 1552 */           Window localWindow = (Window)((WeakReference)localVector.get(k)).get();
/* 1553 */           if (localWindow != null) {
/* 1554 */             arrayOfWindow2[(j++)] = localWindow;
/*      */           }
/*      */         }
/* 1557 */         if (i != j)
/* 1558 */           arrayOfWindow1 = (Window[])Arrays.copyOf(arrayOfWindow2, j);
/*      */         else
/* 1560 */           arrayOfWindow1 = arrayOfWindow2;
/*      */       }
/*      */       else {
/* 1563 */         arrayOfWindow1 = new Window[0];
/*      */       }
/* 1565 */       return arrayOfWindow1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Window[] getWindows()
/*      */   {
/* 1587 */     return getWindows(AppContext.getAppContext());
/*      */   }
/*      */ 
/*      */   public static Window[] getOwnerlessWindows()
/*      */   {
/* 1609 */     Window[] arrayOfWindow1 = getWindows();
/*      */ 
/* 1611 */     int i = 0;
/* 1612 */     for (Window localWindow1 : arrayOfWindow1) {
/* 1613 */       if (localWindow1.getOwner() == null) {
/* 1614 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1618 */     ??? = new Window[i];
/* 1619 */     ??? = 0;
/* 1620 */     for (Window localWindow2 : arrayOfWindow1) {
/* 1621 */       if (localWindow2.getOwner() == null) {
/* 1622 */         ???[(???++)] = localWindow2;
/*      */       }
/*      */     }
/*      */ 
/* 1626 */     return ???;
/*      */   }
/*      */ 
/*      */   Window getDocumentRoot() {
/* 1630 */     synchronized (getTreeLock()) {
/* 1631 */       Window localWindow = this;
/* 1632 */       while (localWindow.getOwner() != null) {
/* 1633 */         localWindow = localWindow.getOwner();
/*      */       }
/* 1635 */       return localWindow;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setModalExclusionType(Dialog.ModalExclusionType paramModalExclusionType)
/*      */   {
/* 1663 */     if (paramModalExclusionType == null) {
/* 1664 */       paramModalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
/*      */     }
/* 1666 */     if (!Toolkit.getDefaultToolkit().isModalExclusionTypeSupported(paramModalExclusionType)) {
/* 1667 */       paramModalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
/*      */     }
/* 1669 */     if (this.modalExclusionType == paramModalExclusionType) {
/* 1670 */       return;
/*      */     }
/* 1672 */     if (paramModalExclusionType == Dialog.ModalExclusionType.TOOLKIT_EXCLUDE) {
/* 1673 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1674 */       if (localSecurityManager != null) {
/* 1675 */         localSecurityManager.checkPermission(SecurityConstants.AWT.TOOLKIT_MODALITY_PERMISSION);
/*      */       }
/*      */     }
/* 1678 */     this.modalExclusionType = paramModalExclusionType;
/*      */   }
/*      */ 
/*      */   public Dialog.ModalExclusionType getModalExclusionType()
/*      */   {
/* 1703 */     return this.modalExclusionType;
/*      */   }
/*      */ 
/*      */   boolean isModalExcluded(Dialog.ModalExclusionType paramModalExclusionType) {
/* 1707 */     if ((this.modalExclusionType != null) && (this.modalExclusionType.compareTo(paramModalExclusionType) >= 0))
/*      */     {
/* 1710 */       return true;
/*      */     }
/* 1712 */     Window localWindow = getOwner_NoClientCode();
/* 1713 */     return (localWindow != null) && (localWindow.isModalExcluded(paramModalExclusionType));
/*      */   }
/*      */ 
/*      */   void updateChildrenBlocking() {
/* 1717 */     Vector localVector = new Vector();
/* 1718 */     Window[] arrayOfWindow = getOwnedWindows();
/* 1719 */     for (int i = 0; i < arrayOfWindow.length; i++) {
/* 1720 */       localVector.add(arrayOfWindow[i]);
/*      */     }
/* 1722 */     i = 0;
/* 1723 */     while (i < localVector.size()) {
/* 1724 */       Window localWindow = (Window)localVector.get(i);
/* 1725 */       if (localWindow.isVisible()) {
/* 1726 */         if (localWindow.isModalBlocked()) {
/* 1727 */           localObject = localWindow.getModalBlocker();
/* 1728 */           ((Dialog)localObject).unblockWindow(localWindow);
/*      */         }
/* 1730 */         Dialog.checkShouldBeBlocked(localWindow);
/* 1731 */         Object localObject = localWindow.getOwnedWindows();
/* 1732 */         for (int j = 0; j < localObject.length; j++) {
/* 1733 */           localVector.add(localObject[j]);
/*      */         }
/*      */       }
/* 1736 */       i++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void addWindowListener(WindowListener paramWindowListener)
/*      */   {
/* 1752 */     if (paramWindowListener == null) {
/* 1753 */       return;
/*      */     }
/* 1755 */     this.newEventsOnly = true;
/* 1756 */     this.windowListener = AWTEventMulticaster.add(this.windowListener, paramWindowListener);
/*      */   }
/*      */ 
/*      */   public synchronized void addWindowStateListener(WindowStateListener paramWindowStateListener)
/*      */   {
/* 1772 */     if (paramWindowStateListener == null) {
/* 1773 */       return;
/*      */     }
/* 1775 */     this.windowStateListener = AWTEventMulticaster.add(this.windowStateListener, paramWindowStateListener);
/* 1776 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void addWindowFocusListener(WindowFocusListener paramWindowFocusListener)
/*      */   {
/* 1792 */     if (paramWindowFocusListener == null) {
/* 1793 */       return;
/*      */     }
/* 1795 */     this.windowFocusListener = AWTEventMulticaster.add(this.windowFocusListener, paramWindowFocusListener);
/* 1796 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeWindowListener(WindowListener paramWindowListener)
/*      */   {
/* 1811 */     if (paramWindowListener == null) {
/* 1812 */       return;
/*      */     }
/* 1814 */     this.windowListener = AWTEventMulticaster.remove(this.windowListener, paramWindowListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removeWindowStateListener(WindowStateListener paramWindowStateListener)
/*      */   {
/* 1831 */     if (paramWindowStateListener == null) {
/* 1832 */       return;
/*      */     }
/* 1834 */     this.windowStateListener = AWTEventMulticaster.remove(this.windowStateListener, paramWindowStateListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removeWindowFocusListener(WindowFocusListener paramWindowFocusListener)
/*      */   {
/* 1850 */     if (paramWindowFocusListener == null) {
/* 1851 */       return;
/*      */     }
/* 1853 */     this.windowFocusListener = AWTEventMulticaster.remove(this.windowFocusListener, paramWindowFocusListener);
/*      */   }
/*      */ 
/*      */   public synchronized WindowListener[] getWindowListeners()
/*      */   {
/* 1869 */     return (WindowListener[])getListeners(WindowListener.class);
/*      */   }
/*      */ 
/*      */   public synchronized WindowFocusListener[] getWindowFocusListeners()
/*      */   {
/* 1885 */     return (WindowFocusListener[])getListeners(WindowFocusListener.class);
/*      */   }
/*      */ 
/*      */   public synchronized WindowStateListener[] getWindowStateListeners()
/*      */   {
/* 1901 */     return (WindowStateListener[])getListeners(WindowStateListener.class);
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/* 1941 */     Object localObject = null;
/* 1942 */     if (paramClass == WindowFocusListener.class)
/* 1943 */       localObject = this.windowFocusListener;
/* 1944 */     else if (paramClass == WindowStateListener.class)
/* 1945 */       localObject = this.windowStateListener;
/* 1946 */     else if (paramClass == WindowListener.class)
/* 1947 */       localObject = this.windowListener;
/*      */     else {
/* 1949 */       return super.getListeners(paramClass);
/*      */     }
/* 1951 */     return AWTEventMulticaster.getListeners((EventListener)localObject, paramClass);
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/* 1956 */     switch (paramAWTEvent.id) {
/*      */     case 200:
/*      */     case 201:
/*      */     case 202:
/*      */     case 203:
/*      */     case 204:
/*      */     case 205:
/*      */     case 206:
/* 1964 */       if (((this.eventMask & 0x40) != 0L) || (this.windowListener != null))
/*      */       {
/* 1966 */         return true;
/*      */       }
/* 1968 */       return false;
/*      */     case 207:
/*      */     case 208:
/* 1971 */       if (((this.eventMask & 0x80000) != 0L) || (this.windowFocusListener != null))
/*      */       {
/* 1973 */         return true;
/*      */       }
/* 1975 */       return false;
/*      */     case 209:
/* 1977 */       if (((this.eventMask & 0x40000) != 0L) || (this.windowStateListener != null))
/*      */       {
/* 1979 */         return true;
/*      */       }
/* 1981 */       return false;
/*      */     }
/*      */ 
/* 1985 */     return super.eventEnabled(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 2000 */     if ((paramAWTEvent instanceof WindowEvent)) {
/* 2001 */       switch (paramAWTEvent.getID()) {
/*      */       case 200:
/*      */       case 201:
/*      */       case 202:
/*      */       case 203:
/*      */       case 204:
/*      */       case 205:
/*      */       case 206:
/* 2009 */         processWindowEvent((WindowEvent)paramAWTEvent);
/* 2010 */         break;
/*      */       case 207:
/*      */       case 208:
/* 2013 */         processWindowFocusEvent((WindowEvent)paramAWTEvent);
/* 2014 */         break;
/*      */       case 209:
/* 2016 */         processWindowStateEvent((WindowEvent)paramAWTEvent);
/*      */       }
/*      */ 
/* 2020 */       return;
/*      */     }
/* 2022 */     super.processEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processWindowEvent(WindowEvent paramWindowEvent)
/*      */   {
/* 2044 */     WindowListener localWindowListener = this.windowListener;
/* 2045 */     if (localWindowListener != null)
/* 2046 */       switch (paramWindowEvent.getID()) {
/*      */       case 200:
/* 2048 */         localWindowListener.windowOpened(paramWindowEvent);
/* 2049 */         break;
/*      */       case 201:
/* 2051 */         localWindowListener.windowClosing(paramWindowEvent);
/* 2052 */         break;
/*      */       case 202:
/* 2054 */         localWindowListener.windowClosed(paramWindowEvent);
/* 2055 */         break;
/*      */       case 203:
/* 2057 */         localWindowListener.windowIconified(paramWindowEvent);
/* 2058 */         break;
/*      */       case 204:
/* 2060 */         localWindowListener.windowDeiconified(paramWindowEvent);
/* 2061 */         break;
/*      */       case 205:
/* 2063 */         localWindowListener.windowActivated(paramWindowEvent);
/* 2064 */         break;
/*      */       case 206:
/* 2066 */         localWindowListener.windowDeactivated(paramWindowEvent);
/* 2067 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void processWindowFocusEvent(WindowEvent paramWindowEvent)
/*      */   {
/* 2094 */     WindowFocusListener localWindowFocusListener = this.windowFocusListener;
/* 2095 */     if (localWindowFocusListener != null)
/* 2096 */       switch (paramWindowEvent.getID()) {
/*      */       case 207:
/* 2098 */         localWindowFocusListener.windowGainedFocus(paramWindowEvent);
/* 2099 */         break;
/*      */       case 208:
/* 2101 */         localWindowFocusListener.windowLostFocus(paramWindowEvent);
/* 2102 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void processWindowStateEvent(WindowEvent paramWindowEvent)
/*      */   {
/* 2130 */     WindowStateListener localWindowStateListener = this.windowStateListener;
/* 2131 */     if (localWindowStateListener != null)
/* 2132 */       switch (paramWindowEvent.getID()) {
/*      */       case 209:
/* 2134 */         localWindowStateListener.windowStateChanged(paramWindowEvent);
/* 2135 */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   void preProcessKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 2150 */     if ((paramKeyEvent.isActionKey()) && (paramKeyEvent.getKeyCode() == 112) && (paramKeyEvent.isControlDown()) && (paramKeyEvent.isShiftDown()) && (paramKeyEvent.getID() == 401))
/*      */     {
/* 2153 */       list(System.out, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   void postProcessKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final void setAlwaysOnTop(boolean paramBoolean)
/*      */     throws SecurityException
/*      */   {
/* 2222 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 2223 */     if (localSecurityManager != null)
/* 2224 */       localSecurityManager.checkPermission(SecurityConstants.AWT.SET_WINDOW_ALWAYS_ON_TOP_PERMISSION);
/*      */     boolean bool;
/* 2228 */     synchronized (this) {
/* 2229 */       bool = this.alwaysOnTop;
/* 2230 */       this.alwaysOnTop = paramBoolean;
/*      */     }
/* 2232 */     if (bool != paramBoolean) {
/* 2233 */       if (isAlwaysOnTopSupported()) {
/* 2234 */         ??? = (WindowPeer)this.peer;
/* 2235 */         synchronized (getTreeLock()) {
/* 2236 */           if (??? != null) {
/* 2237 */             ((WindowPeer)???).updateAlwaysOnTopState();
/*      */           }
/*      */         }
/*      */       }
/* 2241 */       firePropertyChange("alwaysOnTop", bool, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isAlwaysOnTopSupported()
/*      */   {
/* 2259 */     return Toolkit.getDefaultToolkit().isAlwaysOnTopSupported();
/*      */   }
/*      */ 
/*      */   public final boolean isAlwaysOnTop()
/*      */   {
/* 2271 */     return this.alwaysOnTop;
/*      */   }
/*      */ 
/*      */   public Component getFocusOwner()
/*      */   {
/* 2285 */     return isFocused() ? KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() : null;
/*      */   }
/*      */ 
/*      */   public Component getMostRecentFocusOwner()
/*      */   {
/* 2309 */     if (isFocused()) {
/* 2310 */       return getFocusOwner();
/*      */     }
/* 2312 */     Component localComponent = KeyboardFocusManager.getMostRecentFocusOwner(this);
/*      */ 
/* 2314 */     if (localComponent != null) {
/* 2315 */       return localComponent;
/*      */     }
/* 2317 */     return isFocusableWindow() ? getFocusTraversalPolicy().getInitialComponent(this) : null;
/*      */   }
/*      */ 
/*      */   public boolean isActive()
/*      */   {
/* 2336 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() == this;
/*      */   }
/*      */ 
/*      */   public boolean isFocused()
/*      */   {
/* 2354 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getGlobalFocusedWindow() == this;
/*      */   }
/*      */ 
/*      */   public Set<AWTKeyStroke> getFocusTraversalKeys(int paramInt)
/*      */   {
/* 2386 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 2387 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/*      */ 
/* 2391 */     Set<AWTKeyStroke> localSet = this.focusTraversalKeys != null ? this.focusTraversalKeys[paramInt] : null;
/*      */ 
/* 2395 */     if (localSet != null) {
/* 2396 */       return localSet;
/*      */     }
/* 2398 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(paramInt);
/*      */   }
/*      */ 
/*      */   public final void setFocusCycleRoot(boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final boolean isFocusCycleRoot()
/*      */   {
/* 2427 */     return true;
/*      */   }
/*      */ 
/*      */   public final Container getFocusCycleRootAncestor()
/*      */   {
/* 2439 */     return null;
/*      */   }
/*      */ 
/*      */   public final boolean isFocusableWindow()
/*      */   {
/* 2465 */     if (!getFocusableWindowState()) {
/* 2466 */       return false;
/*      */     }
/*      */ 
/* 2470 */     if (((this instanceof Frame)) || ((this instanceof Dialog))) {
/* 2471 */       return true;
/*      */     }
/*      */ 
/* 2476 */     if (getFocusTraversalPolicy().getDefaultComponent(this) == null) {
/* 2477 */       return false;
/*      */     }
/*      */ 
/* 2482 */     for (Window localWindow = getOwner(); localWindow != null; 
/* 2483 */       localWindow = localWindow.getOwner())
/*      */     {
/* 2485 */       if (((localWindow instanceof Frame)) || ((localWindow instanceof Dialog))) {
/* 2486 */         return localWindow.isShowing();
/*      */       }
/*      */     }
/*      */ 
/* 2490 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getFocusableWindowState()
/*      */   {
/* 2514 */     return this.focusableWindowState;
/*      */   }
/*      */ 
/*      */   public void setFocusableWindowState(boolean paramBoolean)
/*      */   {
/*      */     boolean bool;
/* 2549 */     synchronized (this) {
/* 2550 */       bool = this.focusableWindowState;
/* 2551 */       this.focusableWindowState = paramBoolean;
/*      */     }
/* 2553 */     ??? = (WindowPeer)this.peer;
/* 2554 */     if (??? != null) {
/* 2555 */       ((WindowPeer)???).updateFocusableWindowState();
/*      */     }
/* 2557 */     firePropertyChange("focusableWindowState", bool, paramBoolean);
/*      */ 
/* 2559 */     if ((bool) && (!paramBoolean) && (isFocused())) {
/* 2560 */       for (Window localWindow = getOwner(); 
/* 2561 */         localWindow != null; 
/* 2562 */         localWindow = localWindow.getOwner())
/*      */       {
/* 2564 */         Component localComponent = KeyboardFocusManager.getMostRecentFocusOwner(localWindow);
/*      */ 
/* 2566 */         if ((localComponent != null) && (localComponent.requestFocus(false, CausedFocusEvent.Cause.ACTIVATION))) {
/* 2567 */           return;
/*      */         }
/*      */       }
/* 2570 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAutoRequestFocus(boolean paramBoolean)
/*      */   {
/* 2597 */     this.autoRequestFocus = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isAutoRequestFocus()
/*      */   {
/* 2612 */     return this.autoRequestFocus;
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 2651 */     super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 2692 */     super.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/* 2707 */     return true;
/*      */   }
/*      */ 
/*      */   void dispatchEventImpl(AWTEvent paramAWTEvent)
/*      */   {
/* 2715 */     if (paramAWTEvent.getID() == 101) {
/* 2716 */       invalidate();
/* 2717 */       validate();
/*      */     }
/* 2719 */     super.dispatchEventImpl(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean postEvent(Event paramEvent)
/*      */   {
/* 2728 */     if (handleEvent(paramEvent)) {
/* 2729 */       paramEvent.consume();
/* 2730 */       return true;
/*      */     }
/* 2732 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isShowing()
/*      */   {
/* 2740 */     return this.visible;
/*      */   }
/*      */ 
/*      */   boolean isDisposing() {
/* 2744 */     return this.disposing;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void applyResourceBundle(ResourceBundle paramResourceBundle)
/*      */   {
/* 2753 */     applyComponentOrientation(ComponentOrientation.getOrientation(paramResourceBundle));
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void applyResourceBundle(String paramString)
/*      */   {
/* 2762 */     applyResourceBundle(ResourceBundle.getBundle(paramString));
/*      */   }
/*      */ 
/*      */   void addOwnedWindow(WeakReference paramWeakReference)
/*      */   {
/* 2769 */     if (paramWeakReference != null)
/* 2770 */       synchronized (this.ownedWindowList)
/*      */       {
/* 2773 */         if (!this.ownedWindowList.contains(paramWeakReference))
/* 2774 */           this.ownedWindowList.addElement(paramWeakReference);
/*      */       }
/*      */   }
/*      */ 
/*      */   void removeOwnedWindow(WeakReference paramWeakReference)
/*      */   {
/* 2781 */     if (paramWeakReference != null)
/*      */     {
/* 2784 */       this.ownedWindowList.removeElement(paramWeakReference);
/*      */     }
/*      */   }
/*      */ 
/*      */   void connectOwnedWindow(Window paramWindow) {
/* 2789 */     paramWindow.parent = this;
/* 2790 */     addOwnedWindow(paramWindow.weakThis);
/*      */   }
/*      */ 
/*      */   private void addToWindowList() {
/* 2794 */     synchronized (Window.class) {
/* 2795 */       Vector localVector = (Vector)this.appContext.get(Window.class);
/* 2796 */       if (localVector == null) {
/* 2797 */         localVector = new Vector();
/* 2798 */         this.appContext.put(Window.class, localVector);
/*      */       }
/* 2800 */       localVector.add(this.weakThis);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void removeFromWindowList(AppContext paramAppContext, WeakReference paramWeakReference) {
/* 2805 */     synchronized (Window.class) {
/* 2806 */       Vector localVector = (Vector)paramAppContext.get(Window.class);
/* 2807 */       if (localVector != null)
/* 2808 */         localVector.remove(paramWeakReference);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeFromWindowList()
/*      */   {
/* 2814 */     removeFromWindowList(this.appContext, this.weakThis);
/*      */   }
/*      */ 
/*      */   public void setType(Type paramType)
/*      */   {
/* 2837 */     if (paramType == null) {
/* 2838 */       throw new IllegalArgumentException("type should not be null.");
/*      */     }
/* 2840 */     synchronized (getTreeLock()) {
/* 2841 */       if (isDisplayable()) {
/* 2842 */         throw new IllegalComponentStateException("The window is displayable.");
/*      */       }
/*      */ 
/* 2845 */       synchronized (getObjectLock()) {
/* 2846 */         this.type = paramType;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Type getType()
/*      */   {
/* 2858 */     synchronized (getObjectLock()) {
/* 2859 */       return this.type;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2896 */     synchronized (this)
/*      */     {
/* 2899 */       this.focusMgr = new FocusManager();
/* 2900 */       this.focusMgr.focusRoot = this;
/* 2901 */       this.focusMgr.focusOwner = getMostRecentFocusOwner();
/*      */ 
/* 2903 */       paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 2906 */       this.focusMgr = null;
/*      */ 
/* 2908 */       AWTEventMulticaster.save(paramObjectOutputStream, "windowL", this.windowListener);
/* 2909 */       AWTEventMulticaster.save(paramObjectOutputStream, "windowFocusL", this.windowFocusListener);
/* 2910 */       AWTEventMulticaster.save(paramObjectOutputStream, "windowStateL", this.windowStateListener);
/*      */     }
/*      */ 
/* 2913 */     paramObjectOutputStream.writeObject(null);
/*      */ 
/* 2915 */     synchronized (this.ownedWindowList) {
/* 2916 */       for (int i = 0; i < this.ownedWindowList.size(); i++) {
/* 2917 */         Window localWindow = (Window)((WeakReference)this.ownedWindowList.elementAt(i)).get();
/* 2918 */         if (localWindow != null) {
/* 2919 */           paramObjectOutputStream.writeObject("ownedL");
/* 2920 */           paramObjectOutputStream.writeObject(localWindow);
/*      */         }
/*      */       }
/*      */     }
/* 2924 */     paramObjectOutputStream.writeObject(null);
/*      */ 
/* 2927 */     if (this.icons != null) {
/* 2928 */       for (??? = this.icons.iterator(); ((Iterator)???).hasNext(); ) { Image localImage = (Image)((Iterator)???).next();
/* 2929 */         if ((localImage instanceof Serializable)) {
/* 2930 */           paramObjectOutputStream.writeObject(localImage);
/*      */         }
/*      */       }
/*      */     }
/* 2934 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void initDeserializedWindow()
/*      */   {
/* 2942 */     setWarningString();
/* 2943 */     this.inputContextLock = new Object();
/*      */ 
/* 2946 */     this.visible = false;
/*      */ 
/* 2948 */     this.weakThis = new WeakReference(this);
/*      */ 
/* 2950 */     this.anchor = new Object();
/* 2951 */     Disposer.addRecord(this.anchor, new WindowDisposerRecord(this.appContext, this));
/*      */ 
/* 2953 */     addToWindowList();
/* 2954 */     initGC(null);
/*      */   }
/*      */ 
/*      */   private void deserializeResources(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/* 2959 */     this.ownedWindowList = new Vector();
/*      */ 
/* 2961 */     if (this.windowSerializedDataVersion < 2)
/*      */     {
/* 2965 */       if ((this.focusMgr != null) && 
/* 2966 */         (this.focusMgr.focusOwner != null)) {
/* 2967 */         KeyboardFocusManager.setMostRecentFocusOwner(this, this.focusMgr.focusOwner);
/*      */       }
/*      */ 
/* 2975 */       this.focusableWindowState = true;
/*      */     }
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2981 */     while (null != (localObject1 = paramObjectInputStream.readObject())) {
/* 2982 */       localObject2 = ((String)localObject1).intern();
/*      */ 
/* 2984 */       if ("windowL" == localObject2)
/* 2985 */         addWindowListener((WindowListener)paramObjectInputStream.readObject());
/* 2986 */       else if ("windowFocusL" == localObject2)
/* 2987 */         addWindowFocusListener((WindowFocusListener)paramObjectInputStream.readObject());
/* 2988 */       else if ("windowStateL" == localObject2)
/* 2989 */         addWindowStateListener((WindowStateListener)paramObjectInputStream.readObject());
/*      */       else
/* 2991 */         paramObjectInputStream.readObject();
/*      */     }
/*      */     try
/*      */     {
/* 2995 */       while (null != (localObject1 = paramObjectInputStream.readObject())) {
/* 2996 */         localObject2 = ((String)localObject1).intern();
/*      */ 
/* 2998 */         if ("ownedL" == localObject2) {
/* 2999 */           connectOwnedWindow((Window)paramObjectInputStream.readObject());
/*      */         }
/*      */         else {
/* 3002 */           paramObjectInputStream.readObject();
/*      */         }
/*      */       }
/*      */ 
/* 3006 */       localObject2 = paramObjectInputStream.readObject();
/*      */ 
/* 3008 */       this.icons = new ArrayList();
/*      */ 
/* 3010 */       while (localObject2 != null) {
/* 3011 */         if ((localObject2 instanceof Image)) {
/* 3012 */           this.icons.add((Image)localObject2);
/*      */         }
/* 3014 */         localObject2 = paramObjectInputStream.readObject();
/*      */       }
/*      */     }
/*      */     catch (OptionalDataException localOptionalDataException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/* 3041 */     GraphicsEnvironment.checkHeadless();
/* 3042 */     initDeserializedWindow();
/* 3043 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*      */ 
/* 3045 */     this.syncLWRequests = localGetField.get("syncLWRequests", systemSyncLWRequests);
/* 3046 */     this.state = localGetField.get("state", 0);
/* 3047 */     this.focusableWindowState = localGetField.get("focusableWindowState", true);
/* 3048 */     this.windowSerializedDataVersion = localGetField.get("windowSerializedDataVersion", 1);
/* 3049 */     this.locationByPlatform = localGetField.get("locationByPlatform", locationByPlatformProp);
/*      */ 
/* 3051 */     this.focusMgr = ((FocusManager)localGetField.get("focusMgr", null));
/* 3052 */     Dialog.ModalExclusionType localModalExclusionType = (Dialog.ModalExclusionType)localGetField.get("modalExclusionType", Dialog.ModalExclusionType.NO_EXCLUDE);
/*      */ 
/* 3054 */     setModalExclusionType(localModalExclusionType);
/* 3055 */     boolean bool = localGetField.get("alwaysOnTop", false);
/* 3056 */     if (bool) {
/* 3057 */       setAlwaysOnTop(bool);
/*      */     }
/* 3059 */     this.shape = ((Shape)localGetField.get("shape", null));
/* 3060 */     this.opacity = Float.valueOf(localGetField.get("opacity", 1.0F)).floatValue();
/*      */ 
/* 3062 */     this.securityWarningWidth = 0;
/* 3063 */     this.securityWarningHeight = 0;
/* 3064 */     this.securityWarningPointX = 2.0D;
/* 3065 */     this.securityWarningPointY = 0.0D;
/* 3066 */     this.securityWarningAlignmentX = 1.0F;
/* 3067 */     this.securityWarningAlignmentY = 0.0F;
/*      */ 
/* 3069 */     deserializeResources(paramObjectInputStream);
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 3088 */     if (this.accessibleContext == null) {
/* 3089 */       this.accessibleContext = new AccessibleAWTWindow();
/*      */     }
/* 3091 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   void setGraphicsConfiguration(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/* 3137 */     if (paramGraphicsConfiguration == null) {
/* 3138 */       paramGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */     }
/*      */ 
/* 3143 */     synchronized (getTreeLock()) {
/* 3144 */       super.setGraphicsConfiguration(paramGraphicsConfiguration);
/* 3145 */       if (log.isLoggable(400))
/* 3146 */         log.finer("+ Window.setGraphicsConfiguration(): new GC is \n+ " + getGraphicsConfiguration_NoClientCode() + "\n+ this is " + this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLocationRelativeTo(Component paramComponent)
/*      */   {
/* 3209 */     int i = 0; int j = 0;
/*      */ 
/* 3211 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration_NoClientCode();
/* 3212 */     Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/*      */ 
/* 3214 */     Dimension localDimension = getSize();
/*      */ 
/* 3217 */     Window localWindow = SunToolkit.getContainingWindow(paramComponent);
/*      */     Object localObject;
/*      */     Point localPoint;
/* 3218 */     if ((paramComponent == null) || (localWindow == null)) {
/* 3219 */       localObject = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 3220 */       localGraphicsConfiguration = ((GraphicsEnvironment)localObject).getDefaultScreenDevice().getDefaultConfiguration();
/* 3221 */       localRectangle = localGraphicsConfiguration.getBounds();
/* 3222 */       localPoint = ((GraphicsEnvironment)localObject).getCenterPoint();
/* 3223 */       i = localPoint.x - localDimension.width / 2;
/* 3224 */       j = localPoint.y - localDimension.height / 2;
/* 3225 */     } else if (!paramComponent.isShowing()) {
/* 3226 */       localGraphicsConfiguration = localWindow.getGraphicsConfiguration();
/* 3227 */       localRectangle = localGraphicsConfiguration.getBounds();
/* 3228 */       i = localRectangle.x + (localRectangle.width - localDimension.width) / 2;
/* 3229 */       j = localRectangle.y + (localRectangle.height - localDimension.height) / 2;
/*      */     } else {
/* 3231 */       localGraphicsConfiguration = localWindow.getGraphicsConfiguration();
/* 3232 */       localRectangle = localGraphicsConfiguration.getBounds();
/* 3233 */       localObject = paramComponent.getSize();
/* 3234 */       localPoint = paramComponent.getLocationOnScreen();
/* 3235 */       i = localPoint.x + (((Dimension)localObject).width - localDimension.width) / 2;
/* 3236 */       j = localPoint.y + (((Dimension)localObject).height - localDimension.height) / 2;
/*      */ 
/* 3239 */       if (j + localDimension.height > localRectangle.y + localRectangle.height) {
/* 3240 */         j = localRectangle.y + localRectangle.height - localDimension.height;
/* 3241 */         if (localPoint.x - localRectangle.x + ((Dimension)localObject).width / 2 < localRectangle.width / 2)
/* 3242 */           i = localPoint.x + ((Dimension)localObject).width;
/*      */         else {
/* 3244 */           i = localPoint.x - localDimension.width;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3251 */     if (j + localDimension.height > localRectangle.y + localRectangle.height) {
/* 3252 */       j = localRectangle.y + localRectangle.height - localDimension.height;
/*      */     }
/*      */ 
/* 3255 */     if (j < localRectangle.y) {
/* 3256 */       j = localRectangle.y;
/*      */     }
/*      */ 
/* 3259 */     if (i + localDimension.width > localRectangle.x + localRectangle.width) {
/* 3260 */       i = localRectangle.x + localRectangle.width - localDimension.width;
/*      */     }
/*      */ 
/* 3263 */     if (i < localRectangle.x) {
/* 3264 */       i = localRectangle.x;
/*      */     }
/*      */ 
/* 3267 */     setLocation(i, j);
/*      */   }
/*      */ 
/*      */   void deliverMouseWheelToAncestor(MouseWheelEvent paramMouseWheelEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   boolean dispatchMouseWheelToAncestor(MouseWheelEvent paramMouseWheelEvent)
/*      */   {
/* 3279 */     return false;
/*      */   }
/*      */ 
/*      */   public void createBufferStrategy(int paramInt)
/*      */   {
/* 3301 */     super.createBufferStrategy(paramInt);
/*      */   }
/*      */ 
/*      */   public void createBufferStrategy(int paramInt, BufferCapabilities paramBufferCapabilities)
/*      */     throws AWTException
/*      */   {
/* 3326 */     super.createBufferStrategy(paramInt, paramBufferCapabilities);
/*      */   }
/*      */ 
/*      */   public BufferStrategy getBufferStrategy()
/*      */   {
/* 3339 */     return super.getBufferStrategy();
/*      */   }
/*      */ 
/*      */   Component getTemporaryLostComponent() {
/* 3343 */     return this.temporaryLostComponent;
/*      */   }
/*      */   Component setTemporaryLostComponent(Component paramComponent) {
/* 3346 */     Component localComponent = this.temporaryLostComponent;
/*      */ 
/* 3349 */     if ((paramComponent == null) || (paramComponent.canBeFocusOwner()))
/* 3350 */       this.temporaryLostComponent = paramComponent;
/*      */     else {
/* 3352 */       this.temporaryLostComponent = null;
/*      */     }
/* 3354 */     return localComponent;
/*      */   }
/*      */ 
/*      */   boolean canContainFocusOwner(Component paramComponent)
/*      */   {
/* 3363 */     return (super.canContainFocusOwner(paramComponent)) && (isFocusableWindow());
/*      */   }
/*      */ 
/*      */   public void setLocationByPlatform(boolean paramBoolean)
/*      */   {
/* 3417 */     synchronized (getTreeLock()) {
/* 3418 */       if ((paramBoolean) && (isShowing())) {
/* 3419 */         throw new IllegalComponentStateException("The window is showing on screen.");
/*      */       }
/* 3421 */       this.locationByPlatform = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isLocationByPlatform()
/*      */   {
/* 3437 */     synchronized (getTreeLock()) {
/* 3438 */       return this.locationByPlatform;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 3466 */     synchronized (getTreeLock()) {
/* 3467 */       if ((getBoundsOp() == 1) || (getBoundsOp() == 3))
/*      */       {
/* 3470 */         this.locationByPlatform = false;
/*      */       }
/* 3472 */       super.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBounds(Rectangle paramRectangle)
/*      */   {
/* 3500 */     setBounds(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   boolean isRecursivelyVisible()
/*      */   {
/* 3511 */     return this.visible;
/*      */   }
/*      */ 
/*      */   public float getOpacity()
/*      */   {
/* 3528 */     synchronized (getTreeLock()) {
/* 3529 */       return this.opacity;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOpacity(float paramFloat)
/*      */   {
/* 3583 */     synchronized (getTreeLock()) {
/* 3584 */       if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
/* 3585 */         throw new IllegalArgumentException("The value of opacity should be in the range [0.0f .. 1.0f].");
/*      */       }
/*      */ 
/* 3588 */       if (paramFloat < 1.0F) {
/* 3589 */         localObject1 = getGraphicsConfiguration();
/* 3590 */         GraphicsDevice localGraphicsDevice = ((GraphicsConfiguration)localObject1).getDevice();
/* 3591 */         if (((GraphicsConfiguration)localObject1).getDevice().getFullScreenWindow() == this) {
/* 3592 */           throw new IllegalComponentStateException("Setting opacity for full-screen window is not supported.");
/*      */         }
/*      */ 
/* 3595 */         if (!localGraphicsDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT))
/*      */         {
/* 3598 */           throw new UnsupportedOperationException("TRANSLUCENT translucency is not supported.");
/*      */         }
/*      */       }
/*      */ 
/* 3602 */       this.opacity = paramFloat;
/* 3603 */       Object localObject1 = (WindowPeer)getPeer();
/* 3604 */       if (localObject1 != null)
/* 3605 */         ((WindowPeer)localObject1).setOpacity(paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Shape getShape()
/*      */   {
/* 3626 */     synchronized (getTreeLock()) {
/* 3627 */       return this.shape == null ? null : new Path2D.Float(this.shape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setShape(Shape paramShape)
/*      */   {
/* 3681 */     synchronized (getTreeLock()) {
/* 3682 */       if (paramShape != null) {
/* 3683 */         localObject1 = getGraphicsConfiguration();
/* 3684 */         GraphicsDevice localGraphicsDevice = ((GraphicsConfiguration)localObject1).getDevice();
/* 3685 */         if (((GraphicsConfiguration)localObject1).getDevice().getFullScreenWindow() == this) {
/* 3686 */           throw new IllegalComponentStateException("Setting shape for full-screen window is not supported.");
/*      */         }
/*      */ 
/* 3689 */         if (!localGraphicsDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT))
/*      */         {
/* 3692 */           throw new UnsupportedOperationException("PERPIXEL_TRANSPARENT translucency is not supported.");
/*      */         }
/*      */       }
/*      */ 
/* 3696 */       this.shape = (paramShape == null ? null : new Path2D.Float(paramShape));
/* 3697 */       Object localObject1 = (WindowPeer)getPeer();
/* 3698 */       if (localObject1 != null)
/* 3699 */         ((WindowPeer)localObject1).applyShape(paramShape == null ? null : Region.getInstance(paramShape, null));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Color getBackground()
/*      */   {
/* 3718 */     return super.getBackground();
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/* 3798 */     Color localColor = getBackground();
/* 3799 */     super.setBackground(paramColor);
/* 3800 */     if ((localColor != null) && (localColor.equals(paramColor))) {
/* 3801 */       return;
/*      */     }
/* 3803 */     int i = localColor != null ? localColor.getAlpha() : 255;
/* 3804 */     int j = paramColor != null ? paramColor.getAlpha() : 255;
/* 3805 */     if ((i == 255) && (j < 255)) {
/* 3806 */       localObject = getGraphicsConfiguration();
/* 3807 */       GraphicsDevice localGraphicsDevice = ((GraphicsConfiguration)localObject).getDevice();
/* 3808 */       if (((GraphicsConfiguration)localObject).getDevice().getFullScreenWindow() == this) {
/* 3809 */         throw new IllegalComponentStateException("Making full-screen window non opaque is not supported.");
/*      */       }
/*      */ 
/* 3812 */       if (!((GraphicsConfiguration)localObject).isTranslucencyCapable()) {
/* 3813 */         GraphicsConfiguration localGraphicsConfiguration = localGraphicsDevice.getTranslucencyCapableGC();
/* 3814 */         if (localGraphicsConfiguration == null) {
/* 3815 */           throw new UnsupportedOperationException("PERPIXEL_TRANSLUCENT translucency is not supported");
/*      */         }
/*      */ 
/* 3818 */         setGraphicsConfiguration(localGraphicsConfiguration);
/*      */       }
/* 3820 */       setLayersOpaque(this, false);
/* 3821 */     } else if ((i < 255) && (j == 255)) {
/* 3822 */       setLayersOpaque(this, true);
/*      */     }
/* 3824 */     Object localObject = (WindowPeer)getPeer();
/* 3825 */     if (localObject != null)
/* 3826 */       ((WindowPeer)localObject).setOpaque(j == 255);
/*      */   }
/*      */ 
/*      */   public boolean isOpaque()
/*      */   {
/* 3845 */     Color localColor = getBackground();
/* 3846 */     return localColor.getAlpha() == 255;
/*      */   }
/*      */ 
/*      */   private void updateWindow() {
/* 3850 */     synchronized (getTreeLock()) {
/* 3851 */       WindowPeer localWindowPeer = (WindowPeer)getPeer();
/* 3852 */       if (localWindowPeer != null)
/* 3853 */         localWindowPeer.updateWindow();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/* 3865 */     if (!isOpaque()) {
/* 3866 */       Graphics localGraphics = paramGraphics.create();
/*      */       try {
/* 3868 */         if ((localGraphics instanceof Graphics2D)) {
/* 3869 */           localGraphics.setColor(getBackground());
/* 3870 */           ((Graphics2D)localGraphics).setComposite(AlphaComposite.getInstance(2));
/* 3871 */           localGraphics.fillRect(0, 0, getWidth(), getHeight());
/*      */         }
/*      */       } finally {
/* 3874 */         localGraphics.dispose();
/*      */       }
/*      */     }
/* 3877 */     super.paint(paramGraphics);
/*      */   }
/*      */ 
/*      */   private static void setLayersOpaque(Component paramComponent, boolean paramBoolean)
/*      */   {
/* 3883 */     if (SunToolkit.isInstanceOf(paramComponent, "javax.swing.RootPaneContainer")) {
/* 3884 */       RootPaneContainer localRootPaneContainer = (RootPaneContainer)paramComponent;
/* 3885 */       JRootPane localJRootPane = localRootPaneContainer.getRootPane();
/* 3886 */       JLayeredPane localJLayeredPane = localJRootPane.getLayeredPane();
/* 3887 */       Container localContainer = localJRootPane.getContentPane();
/* 3888 */       Object localObject = (localContainer instanceof JComponent) ? (JComponent)localContainer : null;
/*      */ 
/* 3890 */       localJLayeredPane.setOpaque(paramBoolean);
/* 3891 */       localJRootPane.setOpaque(paramBoolean);
/* 3892 */       if (localObject != null) {
/* 3893 */         localObject.setOpaque(paramBoolean);
/*      */ 
/* 3897 */         int i = localObject.getComponentCount();
/* 3898 */         if (i > 0) {
/* 3899 */           Component localComponent = localObject.getComponent(0);
/*      */ 
/* 3902 */           if ((localComponent instanceof RootPaneContainer))
/* 3903 */             setLayersOpaque(localComponent, paramBoolean);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final Container getContainer()
/*      */   {
/* 3916 */     return null;
/*      */   }
/*      */ 
/*      */   final void applyCompoundShape(Region paramRegion)
/*      */   {
/*      */   }
/*      */ 
/*      */   final void applyCurrentShape()
/*      */   {
/*      */   }
/*      */ 
/*      */   final void mixOnReshaping()
/*      */   {
/*      */   }
/*      */ 
/*      */   final Point getLocationOnWindow()
/*      */   {
/* 3943 */     return new Point(0, 0);
/*      */   }
/*      */ 
/*      */   private static double limit(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 3952 */     paramDouble1 = Math.max(paramDouble1, paramDouble2);
/* 3953 */     paramDouble1 = Math.min(paramDouble1, paramDouble3);
/* 3954 */     return paramDouble1;
/*      */   }
/*      */ 
/*      */   private Point2D calculateSecurityWarningPosition(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*      */   {
/* 3972 */     double d1 = paramDouble1 + paramDouble3 * this.securityWarningAlignmentX + this.securityWarningPointX;
/* 3973 */     double d2 = paramDouble2 + paramDouble4 * this.securityWarningAlignmentY + this.securityWarningPointY;
/*      */ 
/* 3976 */     d1 = limit(d1, paramDouble1 - this.securityWarningWidth - 2.0D, paramDouble1 + paramDouble3 + 2.0D);
/*      */ 
/* 3979 */     d2 = limit(d2, paramDouble2 - this.securityWarningHeight - 2.0D, paramDouble2 + paramDouble4 + 2.0D);
/*      */ 
/* 3984 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration_NoClientCode();
/*      */ 
/* 3986 */     Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/* 3987 */     Insets localInsets = Toolkit.getDefaultToolkit().getScreenInsets(localGraphicsConfiguration);
/*      */ 
/* 3990 */     d1 = limit(d1, localRectangle.x + localInsets.left, localRectangle.x + localRectangle.width - localInsets.right - this.securityWarningWidth);
/*      */ 
/* 3994 */     d2 = limit(d2, localRectangle.y + localInsets.top, localRectangle.y + localRectangle.height - localInsets.bottom - this.securityWarningHeight);
/*      */ 
/* 3999 */     return new Point2D.Double(d1, d2);
/*      */   }
/*      */ 
/*      */   void updateZOrder()
/*      */   {
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  397 */     Toolkit.loadLibraries();
/*  398 */     if (!GraphicsEnvironment.isHeadless()) {
/*  399 */       initIDs();
/*      */     }
/*      */ 
/*  402 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.awt.syncLWRequests"));
/*      */ 
/*  404 */     systemSyncLWRequests = (str != null) && (str.equals("true"));
/*  405 */     str = (String)AccessController.doPrivileged(new GetPropertyAction("java.awt.Window.locationByPlatform"));
/*      */ 
/*  407 */     locationByPlatformProp = (str != null) && (str.equals("true"));
/*      */ 
/*  963 */     beforeFirstWindowShown = new AtomicBoolean(true);
/*      */ 
/* 4003 */     AWTAccessor.setWindowAccessor(new AWTAccessor.WindowAccessor() {
/*      */       public float getOpacity(Window paramAnonymousWindow) {
/* 4005 */         return paramAnonymousWindow.opacity;
/*      */       }
/*      */       public void setOpacity(Window paramAnonymousWindow, float paramAnonymousFloat) {
/* 4008 */         paramAnonymousWindow.setOpacity(paramAnonymousFloat);
/*      */       }
/*      */       public Shape getShape(Window paramAnonymousWindow) {
/* 4011 */         return paramAnonymousWindow.getShape();
/*      */       }
/*      */       public void setShape(Window paramAnonymousWindow, Shape paramAnonymousShape) {
/* 4014 */         paramAnonymousWindow.setShape(paramAnonymousShape);
/*      */       }
/*      */       public void setOpaque(Window paramAnonymousWindow, boolean paramAnonymousBoolean) {
/* 4017 */         Color localColor = paramAnonymousWindow.getBackground();
/* 4018 */         if (localColor == null) {
/* 4019 */           localColor = new Color(0, 0, 0, 0);
/*      */         }
/* 4021 */         paramAnonymousWindow.setBackground(new Color(localColor.getRed(), localColor.getGreen(), localColor.getBlue(), paramAnonymousBoolean ? 255 : 0));
/*      */       }
/*      */ 
/*      */       public void updateWindow(Window paramAnonymousWindow) {
/* 4025 */         paramAnonymousWindow.updateWindow();
/*      */       }
/*      */ 
/*      */       public Dimension getSecurityWarningSize(Window paramAnonymousWindow) {
/* 4029 */         return new Dimension(paramAnonymousWindow.securityWarningWidth, paramAnonymousWindow.securityWarningHeight);
/*      */       }
/*      */ 
/*      */       public void setSecurityWarningSize(Window paramAnonymousWindow, int paramAnonymousInt1, int paramAnonymousInt2)
/*      */       {
/* 4035 */         paramAnonymousWindow.securityWarningWidth = paramAnonymousInt1;
/* 4036 */         paramAnonymousWindow.securityWarningHeight = paramAnonymousInt2;
/*      */       }
/*      */ 
/*      */       public void setSecurityWarningPosition(Window paramAnonymousWindow, Point2D paramAnonymousPoint2D, float paramAnonymousFloat1, float paramAnonymousFloat2)
/*      */       {
/* 4042 */         paramAnonymousWindow.securityWarningPointX = paramAnonymousPoint2D.getX();
/* 4043 */         paramAnonymousWindow.securityWarningPointY = paramAnonymousPoint2D.getY();
/* 4044 */         paramAnonymousWindow.securityWarningAlignmentX = paramAnonymousFloat1;
/* 4045 */         paramAnonymousWindow.securityWarningAlignmentY = paramAnonymousFloat2;
/*      */ 
/* 4047 */         synchronized (paramAnonymousWindow.getTreeLock()) {
/* 4048 */           WindowPeer localWindowPeer = (WindowPeer)paramAnonymousWindow.getPeer();
/* 4049 */           if (localWindowPeer != null)
/* 4050 */             localWindowPeer.repositionSecurityWarning();
/*      */         }
/*      */       }
/*      */ 
/*      */       public Point2D calculateSecurityWarningPosition(Window paramAnonymousWindow, double paramAnonymousDouble1, double paramAnonymousDouble2, double paramAnonymousDouble3, double paramAnonymousDouble4)
/*      */       {
/* 4058 */         return paramAnonymousWindow.calculateSecurityWarningPosition(paramAnonymousDouble1, paramAnonymousDouble2, paramAnonymousDouble3, paramAnonymousDouble4);
/*      */       }
/*      */ 
/*      */       public void setLWRequestStatus(Window paramAnonymousWindow, boolean paramAnonymousBoolean) {
/* 4062 */         paramAnonymousWindow.syncLWRequests = paramAnonymousBoolean;
/*      */       }
/*      */ 
/*      */       public boolean isAutoRequestFocus(Window paramAnonymousWindow) {
/* 4066 */         return paramAnonymousWindow.autoRequestFocus;
/*      */       }
/*      */ 
/*      */       public boolean isTrayIconWindow(Window paramAnonymousWindow) {
/* 4070 */         return paramAnonymousWindow.isTrayIconWindow;
/*      */       }
/*      */ 
/*      */       public void setTrayIconWindow(Window paramAnonymousWindow, boolean paramAnonymousBoolean) {
/* 4074 */         paramAnonymousWindow.isTrayIconWindow = paramAnonymousBoolean;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTWindow extends Container.AccessibleAWTContainer
/*      */   {
/*      */     private static final long serialVersionUID = 4215068635060671780L;
/*      */ 
/*      */     protected AccessibleAWTWindow()
/*      */     {
/* 3100 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 3115 */       return AccessibleRole.WINDOW;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 3126 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 3127 */       if (Window.this.getFocusOwner() != null) {
/* 3128 */         localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */       }
/* 3130 */       return localAccessibleStateSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum Type
/*      */   {
/*  172 */     NORMAL, 
/*      */ 
/*  182 */     UTILITY, 
/*      */ 
/*  192 */     POPUP;
/*      */   }
/*      */ 
/*      */   static class WindowDisposerRecord
/*      */     implements DisposerRecord
/*      */   {
/*      */     final WeakReference<Window> owner;
/*      */     final WeakReference weakThis;
/*      */     final WeakReference<AppContext> context;
/*      */ 
/*      */     WindowDisposerRecord(AppContext paramAppContext, Window paramWindow)
/*      */     {
/*  447 */       this.owner = new WeakReference(paramWindow.getOwner());
/*  448 */       this.weakThis = paramWindow.weakThis;
/*  449 */       this.context = new WeakReference(paramAppContext);
/*      */     }
/*      */     public void dispose() {
/*  452 */       Window localWindow = (Window)this.owner.get();
/*  453 */       if (localWindow != null) {
/*  454 */         localWindow.removeOwnedWindow(this.weakThis);
/*      */       }
/*  456 */       AppContext localAppContext = (AppContext)this.context.get();
/*  457 */       if (null != localAppContext)
/*  458 */         Window.removeFromWindowList(localAppContext, this.weakThis);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Window
 * JD-Core Version:    0.6.2
 */