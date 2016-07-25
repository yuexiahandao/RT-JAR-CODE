/*       */ package java.awt;
/*       */ 
/*       */ import java.applet.Applet;
/*       */ import java.awt.dnd.DropTarget;
/*       */ import java.awt.event.ComponentEvent;
/*       */ import java.awt.event.ComponentListener;
/*       */ import java.awt.event.FocusEvent;
/*       */ import java.awt.event.FocusListener;
/*       */ import java.awt.event.HierarchyBoundsListener;
/*       */ import java.awt.event.HierarchyEvent;
/*       */ import java.awt.event.HierarchyListener;
/*       */ import java.awt.event.InputEvent;
/*       */ import java.awt.event.InputMethodEvent;
/*       */ import java.awt.event.InputMethodListener;
/*       */ import java.awt.event.KeyEvent;
/*       */ import java.awt.event.KeyListener;
/*       */ import java.awt.event.MouseEvent;
/*       */ import java.awt.event.MouseListener;
/*       */ import java.awt.event.MouseMotionListener;
/*       */ import java.awt.event.MouseWheelEvent;
/*       */ import java.awt.event.MouseWheelListener;
/*       */ import java.awt.event.PaintEvent;
/*       */ import java.awt.event.WindowEvent;
/*       */ import java.awt.im.InputMethodRequests;
/*       */ import java.awt.image.BufferStrategy;
/*       */ import java.awt.image.ColorModel;
/*       */ import java.awt.image.ImageObserver;
/*       */ import java.awt.image.ImageProducer;
/*       */ import java.awt.image.VolatileImage;
/*       */ import java.awt.peer.ComponentPeer;
/*       */ import java.awt.peer.ContainerPeer;
/*       */ import java.awt.peer.LightweightPeer;
/*       */ import java.awt.peer.MouseInfoPeer;
/*       */ import java.beans.PropertyChangeListener;
/*       */ import java.beans.PropertyChangeSupport;
/*       */ import java.beans.Transient;
/*       */ import java.io.IOException;
/*       */ import java.io.ObjectInputStream;
/*       */ import java.io.ObjectOutputStream;
/*       */ import java.io.OptionalDataException;
/*       */ import java.io.PrintStream;
/*       */ import java.io.PrintWriter;
/*       */ import java.io.Serializable;
/*       */ import java.lang.reflect.InvocationTargetException;
/*       */ import java.lang.reflect.Method;
/*       */ import java.security.AccessControlContext;
/*       */ import java.security.AccessController;
/*       */ import java.security.PrivilegedAction;
/*       */ import java.util.Collections;
/*       */ import java.util.EventListener;
/*       */ import java.util.HashSet;
/*       */ import java.util.Iterator;
/*       */ import java.util.Locale;
/*       */ import java.util.Map;
/*       */ import java.util.Set;
/*       */ import java.util.Vector;
/*       */ import java.util.WeakHashMap;
/*       */ import javax.accessibility.Accessible;
/*       */ import javax.accessibility.AccessibleComponent;
/*       */ import javax.accessibility.AccessibleContext;
/*       */ import javax.accessibility.AccessibleRole;
/*       */ import javax.accessibility.AccessibleSelection;
/*       */ import javax.accessibility.AccessibleState;
/*       */ import javax.accessibility.AccessibleStateSet;
/*       */ import javax.swing.JComponent;
/*       */ import sun.awt.AWTAccessor;
/*       */ import sun.awt.AWTAccessor.ComponentAccessor;
/*       */ import sun.awt.AppContext;
/*       */ import sun.awt.CausedFocusEvent.Cause;
/*       */ import sun.awt.ConstrainableGraphics;
/*       */ import sun.awt.EmbeddedFrame;
/*       */ import sun.awt.EventQueueItem;
/*       */ import sun.awt.RequestFocusController;
/*       */ import sun.awt.SubRegionShowable;
/*       */ import sun.awt.SunToolkit;
/*       */ import sun.awt.WindowClosingListener;
/*       */ import sun.awt.dnd.SunDropTargetEvent;
/*       */ import sun.awt.im.CompositionArea;
/*       */ import sun.awt.image.VSyncedBSManager;
/*       */ import sun.font.FontDesignMetrics;
/*       */ import sun.font.FontManager;
/*       */ import sun.font.FontManagerFactory;
/*       */ import sun.font.SunFontManager;
/*       */ import sun.java2d.SunGraphics2D;
/*       */ import sun.java2d.SunGraphicsEnvironment;
/*       */ import sun.java2d.pipe.Region;
/*       */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
/*       */ import sun.java2d.pipe.hw.ExtendedBufferCapabilities.VSyncType;
/*       */ import sun.security.action.GetPropertyAction;
/*       */ import sun.util.logging.PlatformLogger;
/*       */ 
/*       */ public abstract class Component
/*       */   implements ImageObserver, MenuContainer, Serializable
/*       */ {
/*       */   private static final PlatformLogger log;
/*       */   private static final PlatformLogger eventLog;
/*       */   private static final PlatformLogger focusLog;
/*       */   private static final PlatformLogger mixingLog;
/*       */   transient ComponentPeer peer;
/*       */   transient Container parent;
/*       */   transient AppContext appContext;
/*       */   int x;
/*       */   int y;
/*       */   int width;
/*       */   int height;
/*       */   Color foreground;
/*       */   Color background;
/*       */   Font font;
/*       */   Font peerFont;
/*       */   Cursor cursor;
/*       */   Locale locale;
/*   317 */   private transient GraphicsConfiguration graphicsConfig = null;
/*       */ 
/*   327 */   transient BufferStrategy bufferStrategy = null;
/*       */ 
/*   337 */   boolean ignoreRepaint = false;
/*       */ 
/*   347 */   boolean visible = true;
/*       */ 
/*   357 */   boolean enabled = true;
/*       */ 
/*   369 */   private volatile boolean valid = false;
/*       */   DropTarget dropTarget;
/*       */   Vector popups;
/*       */   private String name;
/*   407 */   private boolean nameExplicitlySet = false;
/*       */ 
/*   417 */   private boolean focusable = true;
/*       */   private static final int FOCUS_TRAVERSABLE_UNKNOWN = 0;
/*       */   private static final int FOCUS_TRAVERSABLE_DEFAULT = 1;
/*       */   private static final int FOCUS_TRAVERSABLE_SET = 2;
/*   429 */   private int isFocusTraversableOverridden = 0;
/*       */   Set[] focusTraversalKeys;
/*       */   private static final String[] focusTraversalKeyPropertyNames;
/*   465 */   private boolean focusTraversalKeysEnabled = true;
/*       */   static final Object LOCK;
/*   478 */   private volatile transient AccessControlContext acc = AccessController.getContext();
/*       */   Dimension minSize;
/*       */   boolean minSizeSet;
/*       */   Dimension prefSize;
/*       */   boolean prefSizeSet;
/*       */   Dimension maxSize;
/*       */   boolean maxSizeSet;
/*   524 */   transient ComponentOrientation componentOrientation = ComponentOrientation.UNKNOWN;
/*       */ 
/*   538 */   boolean newEventsOnly = false;
/*       */   transient ComponentListener componentListener;
/*       */   transient FocusListener focusListener;
/*       */   transient HierarchyListener hierarchyListener;
/*       */   transient HierarchyBoundsListener hierarchyBoundsListener;
/*       */   transient KeyListener keyListener;
/*       */   transient MouseListener mouseListener;
/*       */   transient MouseMotionListener mouseMotionListener;
/*       */   transient MouseWheelListener mouseWheelListener;
/*       */   transient InputMethodListener inputMethodListener;
/*   549 */   transient RuntimeException windowClosingException = null;
/*       */   static final String actionListenerK = "actionL";
/*       */   static final String adjustmentListenerK = "adjustmentL";
/*       */   static final String componentListenerK = "componentL";
/*       */   static final String containerListenerK = "containerL";
/*       */   static final String focusListenerK = "focusL";
/*       */   static final String itemListenerK = "itemL";
/*       */   static final String keyListenerK = "keyL";
/*       */   static final String mouseListenerK = "mouseL";
/*       */   static final String mouseMotionListenerK = "mouseMotionL";
/*       */   static final String mouseWheelListenerK = "mouseWheelL";
/*       */   static final String textListenerK = "textL";
/*       */   static final String ownedWindowK = "ownedL";
/*       */   static final String windowListenerK = "windowL";
/*       */   static final String inputMethodListenerK = "inputMethodL";
/*       */   static final String hierarchyListenerK = "hierarchyL";
/*       */   static final String hierarchyBoundsListenerK = "hierarchyBoundsL";
/*       */   static final String windowStateListenerK = "windowStateL";
/*       */   static final String windowFocusListenerK = "windowFocusL";
/*   585 */   long eventMask = 4096L;
/*       */   static boolean isInc;
/*       */   static int incRate;
/*       */   public static final float TOP_ALIGNMENT = 0.0F;
/*       */   public static final float CENTER_ALIGNMENT = 0.5F;
/*       */   public static final float BOTTOM_ALIGNMENT = 1.0F;
/*       */   public static final float LEFT_ALIGNMENT = 0.0F;
/*       */   public static final float RIGHT_ALIGNMENT = 1.0F;
/*       */   private static final long serialVersionUID = -7644114512714619750L;
/*       */   private PropertyChangeSupport changeSupport;
/*   677 */   private transient Object objectLock = new Object();
/*       */ 
/*   692 */   boolean isPacked = false;
/*       */ 
/*   700 */   private int boundsOp = 3;
/*       */ 
/*   797 */   private transient Region compoundShape = null;
/*       */ 
/*   806 */   private transient Region mixingCutoutRegion = null;
/*       */ 
/*   812 */   private transient boolean isAddNotifyComplete = false;
/*       */   transient boolean backgroundEraseDisabled;
/*       */   transient EventQueueItem[] eventCache;
/*  6121 */   private transient boolean coalescingEnabled = checkCoalescing();
/*       */ 
/*  6128 */   private static final Map<Class<?>, Boolean> coalesceMap = new WeakHashMap();
/*       */ 
/*  6167 */   private static final Class[] coalesceEventsParams = { AWTEvent.class, AWTEvent.class };
/*       */ 
/*  7751 */   private static RequestFocusController requestFocusController = new DummyRequestFocusController(null);
/*       */ 
/*  8013 */   private boolean autoFocusTransferOnDisposal = true;
/*       */ 
/*  8553 */   private int componentSerializedDataVersion = 4;
/*       */ 
/*  8976 */   AccessibleContext accessibleContext = null;
/*       */ 
/*       */   Object getObjectLock()
/*       */   {
/*   679 */     return this.objectLock;
/*       */   }
/*       */ 
/*       */   final AccessControlContext getAccessControlContext()
/*       */   {
/*   686 */     if (this.acc == null) {
/*   687 */       throw new SecurityException("Component is missing AccessControlContext");
/*       */     }
/*   689 */     return this.acc;
/*       */   }
/*       */ 
/*       */   int getBoundsOp()
/*       */   {
/*   819 */     assert (Thread.holdsLock(getTreeLock()));
/*   820 */     return this.boundsOp;
/*       */   }
/*       */ 
/*       */   void setBoundsOp(int paramInt) {
/*   824 */     assert (Thread.holdsLock(getTreeLock()));
/*   825 */     if (paramInt == 5) {
/*   826 */       this.boundsOp = 3;
/*       */     }
/*   828 */     else if (this.boundsOp == 3)
/*   829 */       this.boundsOp = paramInt;
/*       */   }
/*       */ 
/*       */   protected Component()
/*       */   {
/*   990 */     this.appContext = AppContext.getAppContext();
/*       */   }
/*       */ 
/*       */   void initializeFocusTraversalKeys() {
/*   994 */     this.focusTraversalKeys = new Set[3];
/*       */   }
/*       */ 
/*       */   String constructComponentName()
/*       */   {
/*  1002 */     return null;
/*       */   }
/*       */ 
/*       */   public String getName()
/*       */   {
/*  1014 */     if ((this.name == null) && (!this.nameExplicitlySet)) {
/*  1015 */       synchronized (getObjectLock()) {
/*  1016 */         if ((this.name == null) && (!this.nameExplicitlySet))
/*  1017 */           this.name = constructComponentName();
/*       */       }
/*       */     }
/*  1020 */     return this.name;
/*       */   }
/*       */ 
/*       */   public void setName(String paramString)
/*       */   {
/*       */     String str;
/*  1032 */     synchronized (getObjectLock()) {
/*  1033 */       str = this.name;
/*  1034 */       this.name = paramString;
/*  1035 */       this.nameExplicitlySet = true;
/*       */     }
/*  1037 */     firePropertyChange("name", str, paramString);
/*       */   }
/*       */ 
/*       */   public Container getParent()
/*       */   {
/*  1046 */     return getParent_NoClientCode();
/*       */   }
/*       */ 
/*       */   final Container getParent_NoClientCode()
/*       */   {
/*  1054 */     return this.parent;
/*       */   }
/*       */ 
/*       */   Container getContainer()
/*       */   {
/*  1061 */     return getParent();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public ComponentPeer getPeer()
/*       */   {
/*  1071 */     return this.peer;
/*       */   }
/*       */ 
/*       */   public synchronized void setDropTarget(DropTarget paramDropTarget)
/*       */   {
/*  1084 */     if ((paramDropTarget == this.dropTarget) || ((this.dropTarget != null) && (this.dropTarget.equals(paramDropTarget))))
/*       */       return;
/*       */     DropTarget localDropTarget1;
/*  1089 */     if ((localDropTarget1 = this.dropTarget) != null) {
/*  1090 */       if (this.peer != null) this.dropTarget.removeNotify(this.peer);
/*       */ 
/*  1092 */       DropTarget localDropTarget2 = this.dropTarget;
/*       */ 
/*  1094 */       this.dropTarget = null;
/*       */       try
/*       */       {
/*  1097 */         localDropTarget2.setComponent(null);
/*       */       }
/*       */       catch (IllegalArgumentException localIllegalArgumentException2)
/*       */       {
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  1105 */     if ((this.dropTarget = paramDropTarget) != null)
/*       */       try {
/*  1107 */         this.dropTarget.setComponent(this);
/*  1108 */         if (this.peer != null) this.dropTarget.addNotify(this.peer); 
/*       */       }
/*  1110 */       catch (IllegalArgumentException localIllegalArgumentException1) { if (localDropTarget1 != null)
/*       */           try {
/*  1112 */             localDropTarget1.setComponent(this);
/*  1113 */             if (this.peer != null) this.dropTarget.addNotify(this.peer);
/*       */           }
/*       */           catch (IllegalArgumentException localIllegalArgumentException3)
/*       */           {
/*       */           }
/*       */       }
/*       */   }
/*       */ 
/*       */   public synchronized DropTarget getDropTarget()
/*       */   {
/*  1127 */     return this.dropTarget;
/*       */   }
/*       */ 
/*       */   public GraphicsConfiguration getGraphicsConfiguration()
/*       */   {
/*  1145 */     synchronized (getTreeLock()) {
/*  1146 */       return getGraphicsConfiguration_NoClientCode();
/*       */     }
/*       */   }
/*       */ 
/*       */   final GraphicsConfiguration getGraphicsConfiguration_NoClientCode() {
/*  1151 */     return this.graphicsConfig;
/*       */   }
/*       */ 
/*       */   void setGraphicsConfiguration(GraphicsConfiguration paramGraphicsConfiguration) {
/*  1155 */     synchronized (getTreeLock()) {
/*  1156 */       if (updateGraphicsData(paramGraphicsConfiguration)) {
/*  1157 */         removeNotify();
/*  1158 */         addNotify();
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   boolean updateGraphicsData(GraphicsConfiguration paramGraphicsConfiguration) {
/*  1164 */     checkTreeLock();
/*       */ 
/*  1166 */     this.graphicsConfig = paramGraphicsConfiguration;
/*       */ 
/*  1168 */     ComponentPeer localComponentPeer = getPeer();
/*  1169 */     if (localComponentPeer != null) {
/*  1170 */       return localComponentPeer.updateGraphicsData(paramGraphicsConfiguration);
/*       */     }
/*  1172 */     return false;
/*       */   }
/*       */ 
/*       */   void checkGD(String paramString)
/*       */   {
/*  1180 */     if ((this.graphicsConfig != null) && 
/*  1181 */       (!this.graphicsConfig.getDevice().getIDstring().equals(paramString)))
/*  1182 */       throw new IllegalArgumentException("adding a container to a container on a different GraphicsDevice");
/*       */   }
/*       */ 
/*       */   public final Object getTreeLock()
/*       */   {
/*  1195 */     return LOCK;
/*       */   }
/*       */ 
/*       */   final void checkTreeLock() {
/*  1199 */     if (!Thread.holdsLock(getTreeLock()))
/*  1200 */       throw new IllegalStateException("This function should be called while holding treeLock");
/*       */   }
/*       */ 
/*       */   public Toolkit getToolkit()
/*       */   {
/*  1213 */     return getToolkitImpl();
/*       */   }
/*       */ 
/*       */   final Toolkit getToolkitImpl()
/*       */   {
/*  1221 */     ComponentPeer localComponentPeer = this.peer;
/*  1222 */     if ((localComponentPeer != null) && (!(localComponentPeer instanceof LightweightPeer))) {
/*  1223 */       return localComponentPeer.getToolkit();
/*       */     }
/*  1225 */     Container localContainer = this.parent;
/*  1226 */     if (localContainer != null) {
/*  1227 */       return localContainer.getToolkitImpl();
/*       */     }
/*  1229 */     return Toolkit.getDefaultToolkit();
/*       */   }
/*       */ 
/*       */   public boolean isValid()
/*       */   {
/*  1246 */     return (this.peer != null) && (this.valid);
/*       */   }
/*       */ 
/*       */   public boolean isDisplayable()
/*       */   {
/*  1274 */     return getPeer() != null;
/*       */   }
/*       */ 
/*       */   @Transient
/*       */   public boolean isVisible()
/*       */   {
/*  1289 */     return isVisible_NoClientCode();
/*       */   }
/*       */   final boolean isVisible_NoClientCode() {
/*  1292 */     return this.visible;
/*       */   }
/*       */ 
/*       */   boolean isRecursivelyVisible()
/*       */   {
/*  1302 */     return (this.visible) && ((this.parent == null) || (this.parent.isRecursivelyVisible()));
/*       */   }
/*       */ 
/*       */   Point pointRelativeToComponent(Point paramPoint)
/*       */   {
/*  1310 */     Point localPoint = getLocationOnScreen();
/*  1311 */     return new Point(paramPoint.x - localPoint.x, paramPoint.y - localPoint.y);
/*       */   }
/*       */ 
/*       */   Component findUnderMouseInWindow(PointerInfo paramPointerInfo)
/*       */   {
/*  1325 */     if (!isShowing()) {
/*  1326 */       return null;
/*       */     }
/*  1328 */     Window localWindow = getContainingWindow();
/*  1329 */     if (!Toolkit.getDefaultToolkit().getMouseInfoPeer().isWindowUnderMouse(localWindow)) {
/*  1330 */       return null;
/*       */     }
/*       */ 
/*  1333 */     Point localPoint = localWindow.pointRelativeToComponent(paramPointerInfo.getLocation());
/*  1334 */     Component localComponent = localWindow.findComponentAt(localPoint.x, localPoint.y, true);
/*       */ 
/*  1337 */     return localComponent;
/*       */   }
/*       */ 
/*       */   public Point getMousePosition()
/*       */     throws HeadlessException
/*       */   {
/*  1368 */     if (GraphicsEnvironment.isHeadless()) {
/*  1369 */       throw new HeadlessException();
/*       */     }
/*       */ 
/*  1372 */     PointerInfo localPointerInfo = (PointerInfo)AccessController.doPrivileged(new PrivilegedAction()
/*       */     {
/*       */       public Object run() {
/*  1375 */         return MouseInfo.getPointerInfo();
/*       */       }
/*       */     });
/*  1380 */     synchronized (getTreeLock()) {
/*  1381 */       Component localComponent = findUnderMouseInWindow(localPointerInfo);
/*  1382 */       if (!isSameOrAncestorOf(localComponent, true)) {
/*  1383 */         return null;
/*       */       }
/*  1385 */       return pointRelativeToComponent(localPointerInfo.getLocation());
/*       */     }
/*       */   }
/*       */ 
/*       */   boolean isSameOrAncestorOf(Component paramComponent, boolean paramBoolean)
/*       */   {
/*  1393 */     return paramComponent == this;
/*       */   }
/*       */ 
/*       */   public boolean isShowing()
/*       */   {
/*  1415 */     if ((this.visible) && (this.peer != null)) {
/*  1416 */       Container localContainer = this.parent;
/*  1417 */       return (localContainer == null) || (localContainer.isShowing());
/*       */     }
/*  1419 */     return false;
/*       */   }
/*       */ 
/*       */   public boolean isEnabled()
/*       */   {
/*  1433 */     return isEnabledImpl();
/*       */   }
/*       */ 
/*       */   final boolean isEnabledImpl()
/*       */   {
/*  1441 */     return this.enabled;
/*       */   }
/*       */ 
/*       */   public void setEnabled(boolean paramBoolean)
/*       */   {
/*  1462 */     enable(paramBoolean);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void enable()
/*       */   {
/*  1471 */     if (!this.enabled) {
/*  1472 */       synchronized (getTreeLock()) {
/*  1473 */         this.enabled = true;
/*  1474 */         ComponentPeer localComponentPeer = this.peer;
/*  1475 */         if (localComponentPeer != null) {
/*  1476 */           localComponentPeer.setEnabled(true);
/*  1477 */           if (this.visible) {
/*  1478 */             updateCursorImmediately();
/*       */           }
/*       */         }
/*       */       }
/*  1482 */       if (this.accessibleContext != null)
/*  1483 */         this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.ENABLED);
/*       */     }
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void enable(boolean paramBoolean)
/*       */   {
/*  1496 */     if (paramBoolean)
/*  1497 */       enable();
/*       */     else
/*  1499 */       disable();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void disable()
/*       */   {
/*  1509 */     if (this.enabled) {
/*  1510 */       KeyboardFocusManager.clearMostRecentFocusOwner(this);
/*  1511 */       synchronized (getTreeLock()) {
/*  1512 */         this.enabled = false;
/*       */ 
/*  1514 */         if (((isFocusOwner()) || ((containsFocus()) && (!isLightweight()))) && (KeyboardFocusManager.isAutoFocusTransferEnabled()))
/*       */         {
/*  1521 */           transferFocus(false);
/*       */         }
/*  1523 */         ComponentPeer localComponentPeer = this.peer;
/*  1524 */         if (localComponentPeer != null) {
/*  1525 */           localComponentPeer.setEnabled(false);
/*  1526 */           if (this.visible) {
/*  1527 */             updateCursorImmediately();
/*       */           }
/*       */         }
/*       */       }
/*  1531 */       if (this.accessibleContext != null)
/*  1532 */         this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.ENABLED);
/*       */     }
/*       */   }
/*       */ 
/*       */   public boolean isDoubleBuffered()
/*       */   {
/*  1548 */     return false;
/*       */   }
/*       */ 
/*       */   public void enableInputMethods(boolean paramBoolean)
/*       */   {
/*       */     java.awt.im.InputContext localInputContext;
/*  1564 */     if (paramBoolean) {
/*  1565 */       if ((this.eventMask & 0x1000) != 0L) {
/*  1566 */         return;
/*       */       }
/*       */ 
/*  1571 */       if (isFocusOwner()) {
/*  1572 */         localInputContext = getInputContext();
/*  1573 */         if (localInputContext != null) {
/*  1574 */           FocusEvent localFocusEvent = new FocusEvent(this, 1004);
/*       */ 
/*  1576 */           localInputContext.dispatchEvent(localFocusEvent);
/*       */         }
/*       */       }
/*       */ 
/*  1580 */       this.eventMask |= 4096L;
/*       */     } else {
/*  1582 */       if ((this.eventMask & 0x1000) != 0L) {
/*  1583 */         localInputContext = getInputContext();
/*  1584 */         if (localInputContext != null) {
/*  1585 */           localInputContext.endComposition();
/*  1586 */           localInputContext.removeNotify(this);
/*       */         }
/*       */       }
/*  1589 */       this.eventMask &= -4097L;
/*       */     }
/*       */   }
/*       */ 
/*       */   public void setVisible(boolean paramBoolean)
/*       */   {
/*  1607 */     show(paramBoolean);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void show()
/*       */   {
/*  1616 */     if (!this.visible) {
/*  1617 */       synchronized (getTreeLock()) {
/*  1618 */         this.visible = true;
/*  1619 */         mixOnShowing();
/*  1620 */         ComponentPeer localComponentPeer = this.peer;
/*  1621 */         if (localComponentPeer != null) {
/*  1622 */           localComponentPeer.setVisible(true);
/*  1623 */           createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
/*       */ 
/*  1627 */           if ((localComponentPeer instanceof LightweightPeer)) {
/*  1628 */             repaint();
/*       */           }
/*  1630 */           updateCursorImmediately();
/*       */         }
/*       */ 
/*  1633 */         if ((this.componentListener != null) || ((this.eventMask & 1L) != 0L) || (Toolkit.enabledOnToolkit(1L)))
/*       */         {
/*  1636 */           ComponentEvent localComponentEvent = new ComponentEvent(this, 102);
/*       */ 
/*  1638 */           Toolkit.getEventQueue().postEvent(localComponentEvent);
/*       */         }
/*       */       }
/*  1641 */       ??? = this.parent;
/*  1642 */       if (??? != null)
/*  1643 */         ((Container)???).invalidate();
/*       */     }
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void show(boolean paramBoolean)
/*       */   {
/*  1654 */     if (paramBoolean)
/*  1655 */       show();
/*       */     else
/*  1657 */       hide();
/*       */   }
/*       */ 
/*       */   boolean containsFocus()
/*       */   {
/*  1662 */     return isFocusOwner();
/*       */   }
/*       */ 
/*       */   void clearMostRecentFocusOwnerOnHide() {
/*  1666 */     KeyboardFocusManager.clearMostRecentFocusOwner(this);
/*       */   }
/*       */ 
/*       */   void clearCurrentFocusCycleRootOnHide()
/*       */   {
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void hide()
/*       */   {
/*  1679 */     this.isPacked = false;
/*       */ 
/*  1681 */     if (this.visible) {
/*  1682 */       clearCurrentFocusCycleRootOnHide();
/*  1683 */       clearMostRecentFocusOwnerOnHide();
/*  1684 */       synchronized (getTreeLock()) {
/*  1685 */         this.visible = false;
/*  1686 */         mixOnHiding(isLightweight());
/*  1687 */         if ((containsFocus()) && (KeyboardFocusManager.isAutoFocusTransferEnabled())) {
/*  1688 */           transferFocus(true);
/*       */         }
/*  1690 */         ComponentPeer localComponentPeer = this.peer;
/*  1691 */         if (localComponentPeer != null) {
/*  1692 */           localComponentPeer.setVisible(false);
/*  1693 */           createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
/*       */ 
/*  1697 */           if ((localComponentPeer instanceof LightweightPeer)) {
/*  1698 */             repaint();
/*       */           }
/*  1700 */           updateCursorImmediately();
/*       */         }
/*  1702 */         if ((this.componentListener != null) || ((this.eventMask & 1L) != 0L) || (Toolkit.enabledOnToolkit(1L)))
/*       */         {
/*  1705 */           ComponentEvent localComponentEvent = new ComponentEvent(this, 103);
/*       */ 
/*  1707 */           Toolkit.getEventQueue().postEvent(localComponentEvent);
/*       */         }
/*       */       }
/*  1710 */       ??? = this.parent;
/*  1711 */       if (??? != null)
/*  1712 */         ((Container)???).invalidate();
/*       */     }
/*       */   }
/*       */ 
/*       */   @Transient
/*       */   public Color getForeground()
/*       */   {
/*  1729 */     Color localColor = this.foreground;
/*  1730 */     if (localColor != null) {
/*  1731 */       return localColor;
/*       */     }
/*  1733 */     Container localContainer = this.parent;
/*  1734 */     return localContainer != null ? localContainer.getForeground() : null;
/*       */   }
/*       */ 
/*       */   public void setForeground(Color paramColor)
/*       */   {
/*  1747 */     Color localColor = this.foreground;
/*  1748 */     ComponentPeer localComponentPeer = this.peer;
/*  1749 */     this.foreground = paramColor;
/*  1750 */     if (localComponentPeer != null) {
/*  1751 */       paramColor = getForeground();
/*  1752 */       if (paramColor != null) {
/*  1753 */         localComponentPeer.setForeground(paramColor);
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  1758 */     firePropertyChange("foreground", localColor, paramColor);
/*       */   }
/*       */ 
/*       */   public boolean isForegroundSet()
/*       */   {
/*  1771 */     return this.foreground != null;
/*       */   }
/*       */ 
/*       */   @Transient
/*       */   public Color getBackground()
/*       */   {
/*  1784 */     Color localColor = this.background;
/*  1785 */     if (localColor != null) {
/*  1786 */       return localColor;
/*       */     }
/*  1788 */     Container localContainer = this.parent;
/*  1789 */     return localContainer != null ? localContainer.getBackground() : null;
/*       */   }
/*       */ 
/*       */   public void setBackground(Color paramColor)
/*       */   {
/*  1808 */     Color localColor = this.background;
/*  1809 */     ComponentPeer localComponentPeer = this.peer;
/*  1810 */     this.background = paramColor;
/*  1811 */     if (localComponentPeer != null) {
/*  1812 */       paramColor = getBackground();
/*  1813 */       if (paramColor != null) {
/*  1814 */         localComponentPeer.setBackground(paramColor);
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  1819 */     firePropertyChange("background", localColor, paramColor);
/*       */   }
/*       */ 
/*       */   public boolean isBackgroundSet()
/*       */   {
/*  1832 */     return this.background != null;
/*       */   }
/*       */ 
/*       */   @Transient
/*       */   public Font getFont()
/*       */   {
/*  1844 */     return getFont_NoClientCode();
/*       */   }
/*       */ 
/*       */   final Font getFont_NoClientCode()
/*       */   {
/*  1852 */     Font localFont = this.font;
/*  1853 */     if (localFont != null) {
/*  1854 */       return localFont;
/*       */     }
/*  1856 */     Container localContainer = this.parent;
/*  1857 */     return localContainer != null ? localContainer.getFont_NoClientCode() : null;
/*       */   }
/*       */ 
/*       */   public void setFont(Font paramFont)
/*       */   {
/*       */     Font localFont1;
/*       */     Font localFont2;
/*  1877 */     synchronized (getTreeLock()) {
/*  1878 */       synchronized (this) {
/*  1879 */         localFont1 = this.font;
/*  1880 */         localFont2 = this.font = paramFont;
/*       */       }
/*  1882 */       ??? = this.peer;
/*  1883 */       if (??? != null) {
/*  1884 */         paramFont = getFont();
/*  1885 */         if (paramFont != null) {
/*  1886 */           ((ComponentPeer)???).setFont(paramFont);
/*  1887 */           this.peerFont = paramFont;
/*       */         }
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  1893 */     firePropertyChange("font", localFont1, localFont2);
/*       */ 
/*  1898 */     if ((paramFont != localFont1) && ((localFont1 == null) || (!localFont1.equals(paramFont))))
/*       */     {
/*  1900 */       invalidateIfValid();
/*       */     }
/*       */   }
/*       */ 
/*       */   public boolean isFontSet()
/*       */   {
/*  1914 */     return this.font != null;
/*       */   }
/*       */ 
/*       */   public Locale getLocale()
/*       */   {
/*  1929 */     Locale localLocale = this.locale;
/*  1930 */     if (localLocale != null) {
/*  1931 */       return localLocale;
/*       */     }
/*  1933 */     Container localContainer = this.parent;
/*       */ 
/*  1935 */     if (localContainer == null) {
/*  1936 */       throw new IllegalComponentStateException("This component must have a parent in order to determine its locale");
/*       */     }
/*  1938 */     return localContainer.getLocale();
/*       */   }
/*       */ 
/*       */   public void setLocale(Locale paramLocale)
/*       */   {
/*  1954 */     Locale localLocale = this.locale;
/*  1955 */     this.locale = paramLocale;
/*       */ 
/*  1959 */     firePropertyChange("locale", localLocale, paramLocale);
/*       */ 
/*  1962 */     invalidateIfValid();
/*       */   }
/*       */ 
/*       */   public ColorModel getColorModel()
/*       */   {
/*  1975 */     ComponentPeer localComponentPeer = this.peer;
/*  1976 */     if ((localComponentPeer != null) && (!(localComponentPeer instanceof LightweightPeer)))
/*  1977 */       return localComponentPeer.getColorModel();
/*  1978 */     if (GraphicsEnvironment.isHeadless()) {
/*  1979 */       return ColorModel.getRGBdefault();
/*       */     }
/*  1981 */     return getToolkit().getColorModel();
/*       */   }
/*       */ 
/*       */   public Point getLocation()
/*       */   {
/*  2005 */     return location();
/*       */   }
/*       */ 
/*       */   public Point getLocationOnScreen()
/*       */   {
/*  2021 */     synchronized (getTreeLock()) {
/*  2022 */       return getLocationOnScreen_NoTreeLock();
/*       */     }
/*       */   }
/*       */ 
/*       */   final Point getLocationOnScreen_NoTreeLock()
/*       */   {
/*  2032 */     if ((this.peer != null) && (isShowing())) {
/*  2033 */       if ((this.peer instanceof LightweightPeer))
/*       */       {
/*  2036 */         localObject1 = getNativeContainer();
/*  2037 */         Point localPoint = ((Container)localObject1).peer.getLocationOnScreen();
/*  2038 */         for (Object localObject2 = this; localObject2 != localObject1; localObject2 = ((Component)localObject2).getParent()) {
/*  2039 */           localPoint.x += ((Component)localObject2).x;
/*  2040 */           localPoint.y += ((Component)localObject2).y;
/*       */         }
/*  2042 */         return localPoint;
/*       */       }
/*  2044 */       Object localObject1 = this.peer.getLocationOnScreen();
/*  2045 */       return localObject1;
/*       */     }
/*       */ 
/*  2048 */     throw new IllegalComponentStateException("component must be showing on the screen to determine its location");
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Point location()
/*       */   {
/*  2059 */     return location_NoClientCode();
/*       */   }
/*       */ 
/*       */   private Point location_NoClientCode() {
/*  2063 */     return new Point(this.x, this.y);
/*       */   }
/*       */ 
/*       */   public void setLocation(int paramInt1, int paramInt2)
/*       */   {
/*  2084 */     move(paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void move(int paramInt1, int paramInt2)
/*       */   {
/*  2093 */     synchronized (getTreeLock()) {
/*  2094 */       setBoundsOp(1);
/*  2095 */       setBounds(paramInt1, paramInt2, this.width, this.height);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void setLocation(Point paramPoint)
/*       */   {
/*  2116 */     setLocation(paramPoint.x, paramPoint.y);
/*       */   }
/*       */ 
/*       */   public Dimension getSize()
/*       */   {
/*  2132 */     return size();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Dimension size()
/*       */   {
/*  2141 */     return new Dimension(this.width, this.height);
/*       */   }
/*       */ 
/*       */   public void setSize(int paramInt1, int paramInt2)
/*       */   {
/*  2159 */     resize(paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void resize(int paramInt1, int paramInt2)
/*       */   {
/*  2168 */     synchronized (getTreeLock()) {
/*  2169 */       setBoundsOp(2);
/*  2170 */       setBounds(this.x, this.y, paramInt1, paramInt2);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void setSize(Dimension paramDimension)
/*       */   {
/*  2190 */     resize(paramDimension);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void resize(Dimension paramDimension)
/*       */   {
/*  2199 */     setSize(paramDimension.width, paramDimension.height);
/*       */   }
/*       */ 
/*       */   public Rectangle getBounds()
/*       */   {
/*  2213 */     return bounds();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Rectangle bounds()
/*       */   {
/*  2222 */     return new Rectangle(this.x, this.y, this.width, this.height);
/*       */   }
/*       */ 
/*       */   public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */   {
/*  2247 */     reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */   {
/*  2256 */     synchronized (getTreeLock()) {
/*       */       try {
/*  2258 */         setBoundsOp(3);
/*  2259 */         boolean bool1 = (this.width != paramInt3) || (this.height != paramInt4);
/*  2260 */         boolean bool2 = (this.x != paramInt1) || (this.y != paramInt2);
/*  2261 */         if ((!bool1) && (!bool2))
/*       */         {
/*  2306 */           setBoundsOp(5);
/*       */         }
/*       */         else
/*       */         {
/*  2264 */           int i = this.x;
/*  2265 */           int j = this.y;
/*  2266 */           int k = this.width;
/*  2267 */           int m = this.height;
/*  2268 */           this.x = paramInt1;
/*  2269 */           this.y = paramInt2;
/*  2270 */           this.width = paramInt3;
/*  2271 */           this.height = paramInt4;
/*       */ 
/*  2273 */           if (bool1) {
/*  2274 */             this.isPacked = false;
/*       */           }
/*       */ 
/*  2277 */           int n = 1;
/*  2278 */           mixOnReshaping();
/*  2279 */           if (this.peer != null)
/*       */           {
/*  2281 */             if (!(this.peer instanceof LightweightPeer)) {
/*  2282 */               reshapeNativePeer(paramInt1, paramInt2, paramInt3, paramInt4, getBoundsOp());
/*       */ 
/*  2284 */               bool1 = (k != this.width) || (m != this.height);
/*  2285 */               bool2 = (i != this.x) || (j != this.y);
/*       */ 
/*  2290 */               if ((this instanceof Window)) {
/*  2291 */                 n = 0;
/*       */               }
/*       */             }
/*  2294 */             if (bool1) {
/*  2295 */               invalidate();
/*       */             }
/*  2297 */             if (this.parent != null) {
/*  2298 */               this.parent.invalidateIfValid();
/*       */             }
/*       */           }
/*  2301 */           if (n != 0) {
/*  2302 */             notifyNewBounds(bool1, bool2);
/*       */           }
/*  2304 */           repaintParentIfNeeded(i, j, k, m);
/*       */         }
/*       */       } finally { setBoundsOp(5); }
/*       */ 
/*       */     }
/*       */   }
/*       */ 
/*       */   private void repaintParentIfNeeded(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */   {
/*  2314 */     if ((this.parent != null) && ((this.peer instanceof LightweightPeer)) && (isShowing()))
/*       */     {
/*  2316 */       this.parent.repaint(paramInt1, paramInt2, paramInt3, paramInt4);
/*       */ 
/*  2318 */       repaint();
/*       */     }
/*       */   }
/*       */ 
/*       */   private void reshapeNativePeer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*       */   {
/*  2325 */     int i = paramInt1;
/*  2326 */     int j = paramInt2;
/*  2327 */     for (Container localContainer = this.parent; 
/*  2328 */       (localContainer != null) && ((localContainer.peer instanceof LightweightPeer)); 
/*  2329 */       localContainer = localContainer.parent)
/*       */     {
/*  2331 */       i += localContainer.x;
/*  2332 */       j += localContainer.y;
/*       */     }
/*  2334 */     this.peer.setBounds(i, j, paramInt3, paramInt4, paramInt5);
/*       */   }
/*       */ 
/*       */   private void notifyNewBounds(boolean paramBoolean1, boolean paramBoolean2)
/*       */   {
/*  2339 */     if ((this.componentListener != null) || ((this.eventMask & 1L) != 0L) || (Toolkit.enabledOnToolkit(1L)))
/*       */     {
/*       */       ComponentEvent localComponentEvent;
/*  2343 */       if (paramBoolean1) {
/*  2344 */         localComponentEvent = new ComponentEvent(this, 101);
/*       */ 
/*  2346 */         Toolkit.getEventQueue().postEvent(localComponentEvent);
/*       */       }
/*  2348 */       if (paramBoolean2) {
/*  2349 */         localComponentEvent = new ComponentEvent(this, 100);
/*       */ 
/*  2351 */         Toolkit.getEventQueue().postEvent(localComponentEvent);
/*       */       }
/*       */     }
/*  2354 */     else if (((this instanceof Container)) && (((Container)this).countComponents() > 0)) {
/*  2355 */       boolean bool = Toolkit.enabledOnToolkit(65536L);
/*       */ 
/*  2357 */       if (paramBoolean1)
/*       */       {
/*  2359 */         ((Container)this).createChildHierarchyEvents(1402, 0L, bool);
/*       */       }
/*       */ 
/*  2362 */       if (paramBoolean2)
/*  2363 */         ((Container)this).createChildHierarchyEvents(1401, 0L, bool);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void setBounds(Rectangle paramRectangle)
/*       */   {
/*  2391 */     setBounds(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*       */   }
/*       */ 
/*       */   public int getX()
/*       */   {
/*  2406 */     return this.x;
/*       */   }
/*       */ 
/*       */   public int getY()
/*       */   {
/*  2421 */     return this.y;
/*       */   }
/*       */ 
/*       */   public int getWidth()
/*       */   {
/*  2436 */     return this.width;
/*       */   }
/*       */ 
/*       */   public int getHeight()
/*       */   {
/*  2451 */     return this.height;
/*       */   }
/*       */ 
/*       */   public Rectangle getBounds(Rectangle paramRectangle)
/*       */   {
/*  2466 */     if (paramRectangle == null) {
/*  2467 */       return new Rectangle(getX(), getY(), getWidth(), getHeight());
/*       */     }
/*       */ 
/*  2470 */     paramRectangle.setBounds(getX(), getY(), getWidth(), getHeight());
/*  2471 */     return paramRectangle;
/*       */   }
/*       */ 
/*       */   public Dimension getSize(Dimension paramDimension)
/*       */   {
/*  2486 */     if (paramDimension == null) {
/*  2487 */       return new Dimension(getWidth(), getHeight());
/*       */     }
/*       */ 
/*  2490 */     paramDimension.setSize(getWidth(), getHeight());
/*  2491 */     return paramDimension;
/*       */   }
/*       */ 
/*       */   public Point getLocation(Point paramPoint)
/*       */   {
/*  2507 */     if (paramPoint == null) {
/*  2508 */       return new Point(getX(), getY());
/*       */     }
/*       */ 
/*  2511 */     paramPoint.setLocation(getX(), getY());
/*  2512 */     return paramPoint;
/*       */   }
/*       */ 
/*       */   public boolean isOpaque()
/*       */   {
/*  2534 */     if (getPeer() == null) {
/*  2535 */       return false;
/*       */     }
/*       */ 
/*  2538 */     return !isLightweight();
/*       */   }
/*       */ 
/*       */   public boolean isLightweight()
/*       */   {
/*  2560 */     return getPeer() instanceof LightweightPeer;
/*       */   }
/*       */ 
/*       */   public void setPreferredSize(Dimension paramDimension)
/*       */   {
/*       */     Dimension localDimension;
/*  2580 */     if (this.prefSizeSet) {
/*  2581 */       localDimension = this.prefSize;
/*       */     }
/*       */     else {
/*  2584 */       localDimension = null;
/*       */     }
/*  2586 */     this.prefSize = paramDimension;
/*  2587 */     this.prefSizeSet = (paramDimension != null);
/*  2588 */     firePropertyChange("preferredSize", localDimension, paramDimension);
/*       */   }
/*       */ 
/*       */   public boolean isPreferredSizeSet()
/*       */   {
/*  2601 */     return this.prefSizeSet;
/*       */   }
/*       */ 
/*       */   public Dimension getPreferredSize()
/*       */   {
/*  2612 */     return preferredSize();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Dimension preferredSize()
/*       */   {
/*  2625 */     Dimension localDimension = this.prefSize;
/*  2626 */     if ((localDimension == null) || ((!isPreferredSizeSet()) && (!isValid()))) {
/*  2627 */       synchronized (getTreeLock()) {
/*  2628 */         this.prefSize = (this.peer != null ? this.peer.getPreferredSize() : getMinimumSize());
/*       */ 
/*  2631 */         localDimension = this.prefSize;
/*       */       }
/*       */     }
/*  2634 */     return new Dimension(localDimension);
/*       */   }
/*       */ 
/*       */   public void setMinimumSize(Dimension paramDimension)
/*       */   {
/*       */     Dimension localDimension;
/*  2653 */     if (this.minSizeSet) {
/*  2654 */       localDimension = this.minSize;
/*       */     }
/*       */     else {
/*  2657 */       localDimension = null;
/*       */     }
/*  2659 */     this.minSize = paramDimension;
/*  2660 */     this.minSizeSet = (paramDimension != null);
/*  2661 */     firePropertyChange("minimumSize", localDimension, paramDimension);
/*       */   }
/*       */ 
/*       */   public boolean isMinimumSizeSet()
/*       */   {
/*  2673 */     return this.minSizeSet;
/*       */   }
/*       */ 
/*       */   public Dimension getMinimumSize()
/*       */   {
/*  2683 */     return minimumSize();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Dimension minimumSize()
/*       */   {
/*  2695 */     Dimension localDimension = this.minSize;
/*  2696 */     if ((localDimension == null) || ((!isMinimumSizeSet()) && (!isValid()))) {
/*  2697 */       synchronized (getTreeLock()) {
/*  2698 */         this.minSize = (this.peer != null ? this.peer.getMinimumSize() : size());
/*       */ 
/*  2701 */         localDimension = this.minSize;
/*       */       }
/*       */     }
/*  2704 */     return new Dimension(localDimension);
/*       */   }
/*       */ 
/*       */   public void setMaximumSize(Dimension paramDimension)
/*       */   {
/*       */     Dimension localDimension;
/*  2724 */     if (this.maxSizeSet) {
/*  2725 */       localDimension = this.maxSize;
/*       */     }
/*       */     else {
/*  2728 */       localDimension = null;
/*       */     }
/*  2730 */     this.maxSize = paramDimension;
/*  2731 */     this.maxSizeSet = (paramDimension != null);
/*  2732 */     firePropertyChange("maximumSize", localDimension, paramDimension);
/*       */   }
/*       */ 
/*       */   public boolean isMaximumSizeSet()
/*       */   {
/*  2744 */     return this.maxSizeSet;
/*       */   }
/*       */ 
/*       */   public Dimension getMaximumSize()
/*       */   {
/*  2755 */     if (isMaximumSizeSet()) {
/*  2756 */       return new Dimension(this.maxSize);
/*       */     }
/*  2758 */     return new Dimension(32767, 32767);
/*       */   }
/*       */ 
/*       */   public float getAlignmentX()
/*       */   {
/*  2769 */     return 0.5F;
/*       */   }
/*       */ 
/*       */   public float getAlignmentY()
/*       */   {
/*  2780 */     return 0.5F;
/*       */   }
/*       */ 
/*       */   public int getBaseline(int paramInt1, int paramInt2)
/*       */   {
/*  2808 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  2809 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*       */     }
/*       */ 
/*  2812 */     return -1;
/*       */   }
/*       */ 
/*       */   public BaselineResizeBehavior getBaselineResizeBehavior()
/*       */   {
/*  2837 */     return BaselineResizeBehavior.OTHER;
/*       */   }
/*       */ 
/*       */   public void doLayout()
/*       */   {
/*  2848 */     layout();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void layout()
/*       */   {
/*       */   }
/*       */ 
/*       */   public void validate()
/*       */   {
/*  2872 */     synchronized (getTreeLock()) {
/*  2873 */       ComponentPeer localComponentPeer = this.peer;
/*  2874 */       boolean bool = isValid();
/*  2875 */       if ((!bool) && (localComponentPeer != null)) {
/*  2876 */         Font localFont1 = getFont();
/*  2877 */         Font localFont2 = this.peerFont;
/*  2878 */         if ((localFont1 != localFont2) && ((localFont2 == null) || (!localFont2.equals(localFont1))))
/*       */         {
/*  2880 */           localComponentPeer.setFont(localFont1);
/*  2881 */           this.peerFont = localFont1;
/*       */         }
/*  2883 */         localComponentPeer.layout();
/*       */       }
/*  2885 */       this.valid = true;
/*  2886 */       if (!bool)
/*  2887 */         mixOnValidating();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void invalidate()
/*       */   {
/*  2915 */     synchronized (getTreeLock())
/*       */     {
/*  2920 */       this.valid = false;
/*  2921 */       if (!isPreferredSizeSet()) {
/*  2922 */         this.prefSize = null;
/*       */       }
/*  2924 */       if (!isMinimumSizeSet()) {
/*  2925 */         this.minSize = null;
/*       */       }
/*  2927 */       if (!isMaximumSizeSet()) {
/*  2928 */         this.maxSize = null;
/*       */       }
/*  2930 */       invalidateParent();
/*       */     }
/*       */   }
/*       */ 
/*       */   void invalidateParent()
/*       */   {
/*  2940 */     if (this.parent != null)
/*  2941 */       this.parent.invalidateIfValid();
/*       */   }
/*       */ 
/*       */   final void invalidateIfValid()
/*       */   {
/*  2948 */     if (isValid())
/*  2949 */       invalidate();
/*       */   }
/*       */ 
/*       */   public void revalidate()
/*       */   {
/*  2970 */     revalidateSynchronously();
/*       */   }
/*       */ 
/*       */   final void revalidateSynchronously()
/*       */   {
/*  2977 */     synchronized (getTreeLock()) {
/*  2978 */       invalidate();
/*       */ 
/*  2980 */       Container localContainer = getContainer();
/*  2981 */       if (localContainer == null)
/*       */       {
/*  2983 */         validate();
/*       */       } else {
/*  2985 */         while ((!localContainer.isValidateRoot()) && 
/*  2986 */           (localContainer.getContainer() != null))
/*       */         {
/*  2992 */           localContainer = localContainer.getContainer();
/*       */         }
/*       */ 
/*  2995 */         localContainer.validate();
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public Graphics getGraphics()
/*       */   {
/*  3010 */     if ((this.peer instanceof LightweightPeer))
/*       */     {
/*  3014 */       if (this.parent == null) return null;
/*  3015 */       localObject = this.parent.getGraphics();
/*  3016 */       if (localObject == null) return null;
/*  3017 */       if ((localObject instanceof ConstrainableGraphics)) {
/*  3018 */         ((ConstrainableGraphics)localObject).constrain(this.x, this.y, this.width, this.height);
/*       */       } else {
/*  3020 */         ((Graphics)localObject).translate(this.x, this.y);
/*  3021 */         ((Graphics)localObject).setClip(0, 0, this.width, this.height);
/*       */       }
/*  3023 */       ((Graphics)localObject).setFont(getFont());
/*  3024 */       return localObject;
/*       */     }
/*  3026 */     Object localObject = this.peer;
/*  3027 */     return localObject != null ? ((ComponentPeer)localObject).getGraphics() : null;
/*       */   }
/*       */ 
/*       */   final Graphics getGraphics_NoClientCode()
/*       */   {
/*  3032 */     ComponentPeer localComponentPeer = this.peer;
/*  3033 */     if ((localComponentPeer instanceof LightweightPeer))
/*       */     {
/*  3037 */       Container localContainer = this.parent;
/*  3038 */       if (localContainer == null) return null;
/*  3039 */       Graphics localGraphics = localContainer.getGraphics_NoClientCode();
/*  3040 */       if (localGraphics == null) return null;
/*  3041 */       if ((localGraphics instanceof ConstrainableGraphics)) {
/*  3042 */         ((ConstrainableGraphics)localGraphics).constrain(this.x, this.y, this.width, this.height);
/*       */       } else {
/*  3044 */         localGraphics.translate(this.x, this.y);
/*  3045 */         localGraphics.setClip(0, 0, this.width, this.height);
/*       */       }
/*  3047 */       localGraphics.setFont(getFont_NoClientCode());
/*  3048 */       return localGraphics;
/*       */     }
/*  3050 */     return localComponentPeer != null ? localComponentPeer.getGraphics() : null;
/*       */   }
/*       */ 
/*       */   public FontMetrics getFontMetrics(Font paramFont)
/*       */   {
/*  3076 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*  3077 */     if (((localFontManager instanceof SunFontManager)) && (((SunFontManager)localFontManager).usePlatformFontMetrics()))
/*       */     {
/*  3080 */       if ((this.peer != null) && (!(this.peer instanceof LightweightPeer)))
/*       */       {
/*  3082 */         return this.peer.getFontMetrics(paramFont);
/*       */       }
/*       */     }
/*  3085 */     return FontDesignMetrics.getMetrics(paramFont);
/*       */   }
/*       */ 
/*       */   public void setCursor(Cursor paramCursor)
/*       */   {
/*  3114 */     this.cursor = paramCursor;
/*  3115 */     updateCursorImmediately();
/*       */   }
/*       */ 
/*       */   final void updateCursorImmediately()
/*       */   {
/*  3123 */     if ((this.peer instanceof LightweightPeer)) {
/*  3124 */       Container localContainer = getNativeContainer();
/*       */ 
/*  3126 */       if (localContainer == null) return;
/*       */ 
/*  3128 */       ComponentPeer localComponentPeer = localContainer.getPeer();
/*       */ 
/*  3130 */       if (localComponentPeer != null)
/*  3131 */         localComponentPeer.updateCursorImmediately();
/*       */     }
/*  3133 */     else if (this.peer != null) {
/*  3134 */       this.peer.updateCursorImmediately();
/*       */     }
/*       */   }
/*       */ 
/*       */   public Cursor getCursor()
/*       */   {
/*  3147 */     return getCursor_NoClientCode();
/*       */   }
/*       */ 
/*       */   final Cursor getCursor_NoClientCode() {
/*  3151 */     Cursor localCursor = this.cursor;
/*  3152 */     if (localCursor != null) {
/*  3153 */       return localCursor;
/*       */     }
/*  3155 */     Container localContainer = this.parent;
/*  3156 */     if (localContainer != null) {
/*  3157 */       return localContainer.getCursor_NoClientCode();
/*       */     }
/*  3159 */     return Cursor.getPredefinedCursor(0);
/*       */   }
/*       */ 
/*       */   public boolean isCursorSet()
/*       */   {
/*  3173 */     return this.cursor != null;
/*       */   }
/*       */ 
/*       */   public void paint(Graphics paramGraphics)
/*       */   {
/*       */   }
/*       */ 
/*       */   public void update(Graphics paramGraphics)
/*       */   {
/*  3237 */     paint(paramGraphics);
/*       */   }
/*       */ 
/*       */   public void paintAll(Graphics paramGraphics)
/*       */   {
/*  3253 */     if (isShowing())
/*  3254 */       GraphicsCallback.PeerPaintCallback.getInstance().runOneComponent(this, new Rectangle(0, 0, this.width, this.height), paramGraphics, paramGraphics.getClip(), 3);
/*       */   }
/*       */ 
/*       */   void lightweightPaint(Graphics paramGraphics)
/*       */   {
/*  3269 */     paint(paramGraphics);
/*       */   }
/*       */ 
/*       */   void paintHeavyweightComponents(Graphics paramGraphics)
/*       */   {
/*       */   }
/*       */ 
/*       */   public void repaint()
/*       */   {
/*  3297 */     repaint(0L, 0, 0, this.width, this.height);
/*       */   }
/*       */ 
/*       */   public void repaint(long paramLong)
/*       */   {
/*  3316 */     repaint(paramLong, 0, 0, this.width, this.height);
/*       */   }
/*       */ 
/*       */   public void repaint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */   {
/*  3340 */     repaint(0L, paramInt1, paramInt2, paramInt3, paramInt4);
/*       */   }
/*       */ 
/*       */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */   {
/*  3366 */     if ((this.peer instanceof LightweightPeer))
/*       */     {
/*  3371 */       if (this.parent != null) {
/*  3372 */         if (paramInt1 < 0) {
/*  3373 */           paramInt3 += paramInt1;
/*  3374 */           paramInt1 = 0;
/*       */         }
/*  3376 */         if (paramInt2 < 0) {
/*  3377 */           paramInt4 += paramInt2;
/*  3378 */           paramInt2 = 0;
/*       */         }
/*       */ 
/*  3381 */         int i = paramInt3 > this.width ? this.width : paramInt3;
/*  3382 */         int j = paramInt4 > this.height ? this.height : paramInt4;
/*       */ 
/*  3384 */         if ((i <= 0) || (j <= 0)) {
/*  3385 */           return;
/*       */         }
/*       */ 
/*  3388 */         int k = this.x + paramInt1;
/*  3389 */         int m = this.y + paramInt2;
/*  3390 */         this.parent.repaint(paramLong, k, m, i, j);
/*       */       }
/*       */     }
/*  3393 */     else if ((isVisible()) && (this.peer != null) && (paramInt3 > 0) && (paramInt4 > 0))
/*       */     {
/*  3395 */       PaintEvent localPaintEvent = new PaintEvent(this, 801, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*       */ 
/*  3397 */       SunToolkit.postEvent(SunToolkit.targetToAppContext(this), localPaintEvent);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void print(Graphics paramGraphics)
/*       */   {
/*  3419 */     paint(paramGraphics);
/*       */   }
/*       */ 
/*       */   public void printAll(Graphics paramGraphics)
/*       */   {
/*  3434 */     if (isShowing())
/*  3435 */       GraphicsCallback.PeerPrintCallback.getInstance().runOneComponent(this, new Rectangle(0, 0, this.width, this.height), paramGraphics, paramGraphics.getClip(), 3);
/*       */   }
/*       */ 
/*       */   void lightweightPrint(Graphics paramGraphics)
/*       */   {
/*  3450 */     print(paramGraphics);
/*       */   }
/*       */ 
/*       */   void printHeavyweightComponents(Graphics paramGraphics)
/*       */   {
/*       */   }
/*       */ 
/*       */   private Insets getInsets_NoClientCode()
/*       */   {
/*  3460 */     ComponentPeer localComponentPeer = this.peer;
/*  3461 */     if ((localComponentPeer instanceof ContainerPeer)) {
/*  3462 */       return (Insets)((ContainerPeer)localComponentPeer).getInsets().clone();
/*       */     }
/*  3464 */     return new Insets(0, 0, 0, 0);
/*       */   }
/*       */ 
/*       */   public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*       */   {
/*  3515 */     int i = -1;
/*  3516 */     if ((paramInt1 & 0x30) != 0) {
/*  3517 */       i = 0;
/*  3518 */     } else if (((paramInt1 & 0x8) != 0) && 
/*  3519 */       (isInc)) {
/*  3520 */       i = incRate;
/*  3521 */       if (i < 0) {
/*  3522 */         i = 0;
/*       */       }
/*       */     }
/*       */ 
/*  3526 */     if (i >= 0) {
/*  3527 */       repaint(i, 0, 0, this.width, this.height);
/*       */     }
/*  3529 */     return (paramInt1 & 0xA0) == 0;
/*       */   }
/*       */ 
/*       */   public Image createImage(ImageProducer paramImageProducer)
/*       */   {
/*  3539 */     ComponentPeer localComponentPeer = this.peer;
/*  3540 */     if ((localComponentPeer != null) && (!(localComponentPeer instanceof LightweightPeer))) {
/*  3541 */       return localComponentPeer.createImage(paramImageProducer);
/*       */     }
/*  3543 */     return getToolkit().createImage(paramImageProducer);
/*       */   }
/*       */ 
/*       */   public Image createImage(int paramInt1, int paramInt2)
/*       */   {
/*  3561 */     ComponentPeer localComponentPeer = this.peer;
/*  3562 */     if ((localComponentPeer instanceof LightweightPeer)) {
/*  3563 */       if (this.parent != null) return this.parent.createImage(paramInt1, paramInt2);
/*  3564 */       return null;
/*       */     }
/*  3566 */     return localComponentPeer != null ? localComponentPeer.createImage(paramInt1, paramInt2) : null;
/*       */   }
/*       */ 
/*       */   public VolatileImage createVolatileImage(int paramInt1, int paramInt2)
/*       */   {
/*  3586 */     ComponentPeer localComponentPeer = this.peer;
/*  3587 */     if ((localComponentPeer instanceof LightweightPeer)) {
/*  3588 */       if (this.parent != null) {
/*  3589 */         return this.parent.createVolatileImage(paramInt1, paramInt2);
/*       */       }
/*  3591 */       return null;
/*       */     }
/*  3593 */     return localComponentPeer != null ? localComponentPeer.createVolatileImage(paramInt1, paramInt2) : null;
/*       */   }
/*       */ 
/*       */   public VolatileImage createVolatileImage(int paramInt1, int paramInt2, ImageCapabilities paramImageCapabilities)
/*       */     throws AWTException
/*       */   {
/*  3616 */     return createVolatileImage(paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   public boolean prepareImage(Image paramImage, ImageObserver paramImageObserver)
/*       */   {
/*  3632 */     return prepareImage(paramImage, -1, -1, paramImageObserver);
/*       */   }
/*       */ 
/*       */   public boolean prepareImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*       */   {
/*  3655 */     ComponentPeer localComponentPeer = this.peer;
/*  3656 */     if ((localComponentPeer instanceof LightweightPeer)) {
/*  3657 */       return this.parent != null ? this.parent.prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver) : getToolkit().prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*       */     }
/*       */ 
/*  3661 */     return localComponentPeer != null ? localComponentPeer.prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver) : getToolkit().prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*       */   }
/*       */ 
/*       */   public int checkImage(Image paramImage, ImageObserver paramImageObserver)
/*       */   {
/*  3690 */     return checkImage(paramImage, -1, -1, paramImageObserver);
/*       */   }
/*       */ 
/*       */   public int checkImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*       */   {
/*  3727 */     ComponentPeer localComponentPeer = this.peer;
/*  3728 */     if ((localComponentPeer instanceof LightweightPeer)) {
/*  3729 */       return this.parent != null ? this.parent.checkImage(paramImage, paramInt1, paramInt2, paramImageObserver) : getToolkit().checkImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*       */     }
/*       */ 
/*  3733 */     return localComponentPeer != null ? localComponentPeer.checkImage(paramImage, paramInt1, paramInt2, paramImageObserver) : getToolkit().checkImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*       */   }
/*       */ 
/*       */   void createBufferStrategy(int paramInt)
/*       */   {
/*  3761 */     if (paramInt > 1)
/*       */     {
/*  3763 */       localBufferCapabilities = new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.UNDEFINED);
/*       */       try
/*       */       {
/*  3767 */         createBufferStrategy(paramInt, localBufferCapabilities);
/*  3768 */         return;
/*       */       }
/*       */       catch (AWTException localAWTException1)
/*       */       {
/*       */       }
/*       */     }
/*  3774 */     BufferCapabilities localBufferCapabilities = new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), null);
/*       */     try
/*       */     {
/*  3778 */       createBufferStrategy(paramInt, localBufferCapabilities);
/*  3779 */       return;
/*       */     }
/*       */     catch (AWTException localAWTException2)
/*       */     {
/*  3784 */       localBufferCapabilities = new BufferCapabilities(new ImageCapabilities(false), new ImageCapabilities(false), null);
/*       */       try
/*       */       {
/*  3788 */         createBufferStrategy(paramInt, localBufferCapabilities);
/*  3789 */         return;
/*       */       }
/*       */       catch (AWTException localAWTException3)
/*       */       {
/*       */       }
/*       */     }
/*  3795 */     throw new InternalError("Could not create a buffer strategy");
/*       */   }
/*       */ 
/*       */   void createBufferStrategy(int paramInt, BufferCapabilities paramBufferCapabilities)
/*       */     throws AWTException
/*       */   {
/*  3823 */     if (paramInt < 1) {
/*  3824 */       throw new IllegalArgumentException("Number of buffers must be at least 1");
/*       */     }
/*       */ 
/*  3827 */     if (paramBufferCapabilities == null) {
/*  3828 */       throw new IllegalArgumentException("No capabilities specified");
/*       */     }
/*       */ 
/*  3831 */     if (this.bufferStrategy != null) {
/*  3832 */       this.bufferStrategy.dispose();
/*       */     }
/*  3834 */     if (paramInt == 1) {
/*  3835 */       this.bufferStrategy = new SingleBufferStrategy(paramBufferCapabilities);
/*       */     } else {
/*  3837 */       SunGraphicsEnvironment localSunGraphicsEnvironment = (SunGraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment();
/*       */ 
/*  3839 */       if ((!paramBufferCapabilities.isPageFlipping()) && (localSunGraphicsEnvironment.isFlipStrategyPreferred(this.peer))) {
/*  3840 */         paramBufferCapabilities = new ProxyCapabilities(paramBufferCapabilities, null);
/*       */       }
/*       */ 
/*  3843 */       if (paramBufferCapabilities.isPageFlipping())
/*  3844 */         this.bufferStrategy = new FlipSubRegionBufferStrategy(paramInt, paramBufferCapabilities);
/*       */       else
/*  3846 */         this.bufferStrategy = new BltSubRegionBufferStrategy(paramInt, paramBufferCapabilities);
/*       */     }
/*       */   }
/*       */ 
/*       */   BufferStrategy getBufferStrategy()
/*       */   {
/*  3877 */     return this.bufferStrategy;
/*       */   }
/*       */ 
/*       */   Image getBackBuffer()
/*       */   {
/*  3886 */     if (this.bufferStrategy != null)
/*       */     {
/*       */       Object localObject;
/*  3887 */       if ((this.bufferStrategy instanceof BltBufferStrategy)) {
/*  3888 */         localObject = (BltBufferStrategy)this.bufferStrategy;
/*  3889 */         return ((BltBufferStrategy)localObject).getBackBuffer();
/*  3890 */       }if ((this.bufferStrategy instanceof FlipBufferStrategy)) {
/*  3891 */         localObject = (FlipBufferStrategy)this.bufferStrategy;
/*  3892 */         return ((FlipBufferStrategy)localObject).getBackBuffer();
/*       */       }
/*       */     }
/*  3895 */     return null;
/*       */   }
/*       */ 
/*       */   public void setIgnoreRepaint(boolean paramBoolean)
/*       */   {
/*  4588 */     this.ignoreRepaint = paramBoolean;
/*       */   }
/*       */ 
/*       */   public boolean getIgnoreRepaint()
/*       */   {
/*  4599 */     return this.ignoreRepaint;
/*       */   }
/*       */ 
/*       */   public boolean contains(int paramInt1, int paramInt2)
/*       */   {
/*  4612 */     return inside(paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean inside(int paramInt1, int paramInt2)
/*       */   {
/*  4621 */     return (paramInt1 >= 0) && (paramInt1 < this.width) && (paramInt2 >= 0) && (paramInt2 < this.height);
/*       */   }
/*       */ 
/*       */   public boolean contains(Point paramPoint)
/*       */   {
/*  4634 */     return contains(paramPoint.x, paramPoint.y);
/*       */   }
/*       */ 
/*       */   public Component getComponentAt(int paramInt1, int paramInt2)
/*       */   {
/*  4659 */     return locate(paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public Component locate(int paramInt1, int paramInt2)
/*       */   {
/*  4668 */     return contains(paramInt1, paramInt2) ? this : null;
/*       */   }
/*       */ 
/*       */   public Component getComponentAt(Point paramPoint)
/*       */   {
/*  4679 */     return getComponentAt(paramPoint.x, paramPoint.y);
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void deliverEvent(Event paramEvent)
/*       */   {
/*  4688 */     postEvent(paramEvent);
/*       */   }
/*       */ 
/*       */   public final void dispatchEvent(AWTEvent paramAWTEvent)
/*       */   {
/*  4698 */     dispatchEventImpl(paramAWTEvent);
/*       */   }
/*       */ 
/*       */   void dispatchEventImpl(AWTEvent paramAWTEvent) {
/*  4702 */     int i = paramAWTEvent.getID();
/*       */ 
/*  4705 */     AppContext localAppContext = this.appContext;
/*  4706 */     if ((localAppContext != null) && (!localAppContext.equals(AppContext.getAppContext())) && 
/*  4707 */       (eventLog.isLoggable(500))) {
/*  4708 */       eventLog.fine("Event " + paramAWTEvent + " is being dispatched on the wrong AppContext");
/*       */     }
/*       */ 
/*  4712 */     if (eventLog.isLoggable(300)) {
/*  4713 */       eventLog.finest("{0}", new Object[] { paramAWTEvent });
/*       */     }
/*       */ 
/*  4719 */     EventQueue.setCurrentEventAndMostRecentTime(paramAWTEvent);
/*       */ 
/*  4726 */     if ((paramAWTEvent instanceof SunDropTargetEvent)) {
/*  4727 */       ((SunDropTargetEvent)paramAWTEvent).dispatch();
/*  4728 */       return;
/*       */     }
/*       */ 
/*  4731 */     if (!paramAWTEvent.focusManagerIsDispatching)
/*       */     {
/*  4734 */       if (paramAWTEvent.isPosted) {
/*  4735 */         paramAWTEvent = KeyboardFocusManager.retargetFocusEvent(paramAWTEvent);
/*  4736 */         paramAWTEvent.isPosted = true;
/*       */       }
/*       */ 
/*  4742 */       if (KeyboardFocusManager.getCurrentKeyboardFocusManager().dispatchEvent(paramAWTEvent))
/*       */       {
/*  4745 */         return;
/*       */       }
/*       */     }
/*  4748 */     if (((paramAWTEvent instanceof FocusEvent)) && (focusLog.isLoggable(300))) {
/*  4749 */       focusLog.finest("" + paramAWTEvent);
/*       */     }
/*       */ 
/*  4756 */     if ((i == 507) && (!eventTypeEnabled(i)) && (this.peer != null) && (!this.peer.handlesWheelScrolling()) && (dispatchMouseWheelToAncestor((MouseWheelEvent)paramAWTEvent)))
/*       */     {
/*  4761 */       return;
/*       */     }
/*       */ 
/*  4767 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  4768 */     localToolkit.notifyAWTEventListeners(paramAWTEvent);
/*       */ 
/*  4775 */     if ((!paramAWTEvent.isConsumed()) && 
/*  4776 */       ((paramAWTEvent instanceof KeyEvent))) {
/*  4777 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().processKeyEvent(this, (KeyEvent)paramAWTEvent);
/*       */ 
/*  4779 */       if (paramAWTEvent.isConsumed())
/*       */         return;
/*       */     }
/*       */     Object localObject;
/*  4788 */     if (areInputMethodsEnabled())
/*       */     {
/*  4793 */       if ((((paramAWTEvent instanceof InputMethodEvent)) && (!(this instanceof CompositionArea))) || ((paramAWTEvent instanceof InputEvent)) || ((paramAWTEvent instanceof FocusEvent)))
/*       */       {
/*  4800 */         localObject = getInputContext();
/*       */ 
/*  4803 */         if (localObject != null) {
/*  4804 */           ((java.awt.im.InputContext)localObject).dispatchEvent(paramAWTEvent);
/*  4805 */           if (paramAWTEvent.isConsumed()) {
/*  4806 */             if (((paramAWTEvent instanceof FocusEvent)) && (focusLog.isLoggable(300))) {
/*  4807 */               focusLog.finest("3579: Skipping " + paramAWTEvent);
/*       */             }
/*  4809 */             return;
/*       */           }
/*       */ 
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*       */     }
/*  4817 */     else if (i == 1004) {
/*  4818 */       localObject = getInputContext();
/*  4819 */       if ((localObject != null) && ((localObject instanceof sun.awt.im.InputContext))) {
/*  4820 */         ((sun.awt.im.InputContext)localObject).disableNativeIM();
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  4829 */     switch (i)
/*       */     {
/*       */     case 401:
/*       */     case 402:
/*  4837 */       localObject = (Container)((this instanceof Container) ? this : this.parent);
/*  4838 */       if (localObject != null) {
/*  4839 */         ((Container)localObject).preProcessKeyEvent((KeyEvent)paramAWTEvent);
/*  4840 */         if (paramAWTEvent.isConsumed()) {
/*  4841 */           if (focusLog.isLoggable(300)) {
/*  4842 */             focusLog.finest("Pre-process consumed event");
/*       */           }
/*       */           return;
/*       */         }
/*       */       }
/*       */ 
/*       */       break;
/*       */     case 201:
/*  4850 */       if ((localToolkit instanceof WindowClosingListener)) {
/*  4851 */         this.windowClosingException = ((WindowClosingListener)localToolkit).windowClosingNotify((WindowEvent)paramAWTEvent);
/*       */ 
/*  4853 */         if (checkWindowClosingException())
/*       */         {
/*       */           return;
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*       */       break;
/*       */     }
/*       */ 
/*  4866 */     if (this.newEventsOnly)
/*       */     {
/*  4871 */       if (eventEnabled(paramAWTEvent))
/*  4872 */         processEvent(paramAWTEvent);
/*       */     }
/*  4874 */     else if (i == 507)
/*       */     {
/*  4878 */       autoProcessMouseWheel((MouseWheelEvent)paramAWTEvent);
/*  4879 */     } else if ((!(paramAWTEvent instanceof MouseEvent)) || (postsOldMouseEvents()))
/*       */     {
/*  4883 */       localObject = paramAWTEvent.convertToOld();
/*  4884 */       if (localObject != null) {
/*  4885 */         int j = ((Event)localObject).key;
/*  4886 */         int k = ((Event)localObject).modifiers;
/*       */ 
/*  4888 */         postEvent((Event)localObject);
/*  4889 */         if (((Event)localObject).isConsumed()) {
/*  4890 */           paramAWTEvent.consume();
/*       */         }
/*       */ 
/*  4895 */         switch (((Event)localObject).id) {
/*       */         case 401:
/*       */         case 402:
/*       */         case 403:
/*       */         case 404:
/*  4900 */           if (((Event)localObject).key != j) {
/*  4901 */             ((KeyEvent)paramAWTEvent).setKeyChar(((Event)localObject).getKeyEventChar());
/*       */           }
/*  4903 */           if (((Event)localObject).modifiers != k)
/*  4904 */             ((KeyEvent)paramAWTEvent).setModifiers(((Event)localObject).modifiers); break;
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  4917 */     if ((i == 201) && (!paramAWTEvent.isConsumed()) && 
/*  4918 */       ((localToolkit instanceof WindowClosingListener))) {
/*  4919 */       this.windowClosingException = ((WindowClosingListener)localToolkit).windowClosingDelivered((WindowEvent)paramAWTEvent);
/*       */ 
/*  4922 */       if (checkWindowClosingException()) {
/*  4923 */         return;
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  4934 */     if (!(paramAWTEvent instanceof KeyEvent)) {
/*  4935 */       localObject = this.peer;
/*  4936 */       if (((paramAWTEvent instanceof FocusEvent)) && ((localObject == null) || ((localObject instanceof LightweightPeer))))
/*       */       {
/*  4939 */         Component localComponent = (Component)paramAWTEvent.getSource();
/*  4940 */         if (localComponent != null) {
/*  4941 */           Container localContainer = localComponent.getNativeContainer();
/*  4942 */           if (localContainer != null) {
/*  4943 */             localObject = localContainer.getPeer();
/*       */           }
/*       */         }
/*       */       }
/*  4947 */       if (localObject != null)
/*  4948 */         ((ComponentPeer)localObject).handleEvent(paramAWTEvent);
/*       */     }
/*       */   }
/*       */ 
/*       */   void autoProcessMouseWheel(MouseWheelEvent paramMouseWheelEvent)
/*       */   {
/*       */   }
/*       */ 
/*       */   boolean dispatchMouseWheelToAncestor(MouseWheelEvent paramMouseWheelEvent)
/*       */   {
/*  4968 */     int i = paramMouseWheelEvent.getX() + getX();
/*  4969 */     int j = paramMouseWheelEvent.getY() + getY();
/*       */ 
/*  4974 */     if (eventLog.isLoggable(300)) {
/*  4975 */       eventLog.finest("dispatchMouseWheelToAncestor");
/*  4976 */       eventLog.finest("orig event src is of " + paramMouseWheelEvent.getSource().getClass());
/*       */     }
/*       */ 
/*  4982 */     synchronized (getTreeLock()) {
/*  4983 */       Container localContainer = getParent();
/*  4984 */       while ((localContainer != null) && (!localContainer.eventEnabled(paramMouseWheelEvent)))
/*       */       {
/*  4986 */         i += localContainer.getX();
/*  4987 */         j += localContainer.getY();
/*       */ 
/*  4989 */         if ((localContainer instanceof Window)) break;
/*  4990 */         localContainer = localContainer.getParent();
/*       */       }
/*       */ 
/*  4997 */       if (eventLog.isLoggable(300)) {
/*  4998 */         eventLog.finest("new event src is " + localContainer.getClass());
/*       */       }
/*       */ 
/*  5001 */       if ((localContainer != null) && (localContainer.eventEnabled(paramMouseWheelEvent)))
/*       */       {
/*  5005 */         MouseWheelEvent localMouseWheelEvent = new MouseWheelEvent(localContainer, paramMouseWheelEvent.getID(), paramMouseWheelEvent.getWhen(), paramMouseWheelEvent.getModifiers(), i, j, paramMouseWheelEvent.getXOnScreen(), paramMouseWheelEvent.getYOnScreen(), paramMouseWheelEvent.getClickCount(), paramMouseWheelEvent.isPopupTrigger(), paramMouseWheelEvent.getScrollType(), paramMouseWheelEvent.getScrollAmount(), paramMouseWheelEvent.getWheelRotation(), paramMouseWheelEvent.getPreciseWheelRotation());
/*       */ 
/*  5019 */         paramMouseWheelEvent.copyPrivateDataInto(localMouseWheelEvent);
/*       */ 
/*  5025 */         localContainer.dispatchEventToSelf(localMouseWheelEvent);
/*  5026 */         if (localMouseWheelEvent.isConsumed()) {
/*  5027 */           paramMouseWheelEvent.consume();
/*       */         }
/*  5029 */         return true;
/*       */       }
/*       */     }
/*  5032 */     return false;
/*       */   }
/*       */ 
/*       */   boolean checkWindowClosingException() {
/*  5036 */     if (this.windowClosingException != null) {
/*  5037 */       if ((this instanceof Dialog)) {
/*  5038 */         ((Dialog)this).interruptBlocking();
/*       */       } else {
/*  5040 */         this.windowClosingException.fillInStackTrace();
/*  5041 */         this.windowClosingException.printStackTrace();
/*  5042 */         this.windowClosingException = null;
/*       */       }
/*  5044 */       return true;
/*       */     }
/*  5046 */     return false;
/*       */   }
/*       */ 
/*       */   boolean areInputMethodsEnabled()
/*       */   {
/*  5053 */     return ((this.eventMask & 0x1000) != 0L) && (((this.eventMask & 0x8) != 0L) || (this.keyListener != null));
/*       */   }
/*       */ 
/*       */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*       */   {
/*  5059 */     return eventTypeEnabled(paramAWTEvent.id);
/*       */   }
/*       */ 
/*       */   boolean eventTypeEnabled(int paramInt) {
/*  5063 */     switch (paramInt) {
/*       */     case 100:
/*       */     case 101:
/*       */     case 102:
/*       */     case 103:
/*  5068 */       if (((this.eventMask & 1L) != 0L) || (this.componentListener != null))
/*       */       {
/*  5070 */         return true;
/*       */       }
/*       */       break;
/*       */     case 1004:
/*       */     case 1005:
/*  5075 */       if (((this.eventMask & 0x4) != 0L) || (this.focusListener != null))
/*       */       {
/*  5077 */         return true;
/*       */       }
/*       */       break;
/*       */     case 400:
/*       */     case 401:
/*       */     case 402:
/*  5083 */       if (((this.eventMask & 0x8) != 0L) || (this.keyListener != null))
/*       */       {
/*  5085 */         return true;
/*       */       }
/*       */       break;
/*       */     case 500:
/*       */     case 501:
/*       */     case 502:
/*       */     case 504:
/*       */     case 505:
/*  5093 */       if (((this.eventMask & 0x10) != 0L) || (this.mouseListener != null))
/*       */       {
/*  5095 */         return true;
/*       */       }
/*       */       break;
/*       */     case 503:
/*       */     case 506:
/*  5100 */       if (((this.eventMask & 0x20) != 0L) || (this.mouseMotionListener != null))
/*       */       {
/*  5102 */         return true;
/*       */       }
/*       */       break;
/*       */     case 507:
/*  5106 */       if (((this.eventMask & 0x20000) != 0L) || (this.mouseWheelListener != null))
/*       */       {
/*  5108 */         return true;
/*       */       }
/*       */       break;
/*       */     case 1100:
/*       */     case 1101:
/*  5113 */       if (((this.eventMask & 0x800) != 0L) || (this.inputMethodListener != null))
/*       */       {
/*  5115 */         return true;
/*       */       }
/*       */       break;
/*       */     case 1400:
/*  5119 */       if (((this.eventMask & 0x8000) != 0L) || (this.hierarchyListener != null))
/*       */       {
/*  5121 */         return true;
/*       */       }
/*       */       break;
/*       */     case 1401:
/*       */     case 1402:
/*  5126 */       if (((this.eventMask & 0x10000) != 0L) || (this.hierarchyBoundsListener != null))
/*       */       {
/*  5128 */         return true;
/*       */       }
/*       */       break;
/*       */     case 1001:
/*  5132 */       if ((this.eventMask & 0x80) != 0L) {
/*  5133 */         return true;
/*       */       }
/*       */       break;
/*       */     case 900:
/*  5137 */       if ((this.eventMask & 0x400) != 0L) {
/*  5138 */         return true;
/*       */       }
/*       */       break;
/*       */     case 701:
/*  5142 */       if ((this.eventMask & 0x200) != 0L) {
/*  5143 */         return true;
/*       */       }
/*       */       break;
/*       */     case 601:
/*  5147 */       if ((this.eventMask & 0x100) != 0L) {
/*  5148 */         return true;
/*       */       }
/*       */ 
/*       */       break;
/*       */     }
/*       */ 
/*  5157 */     if (paramInt > 1999) {
/*  5158 */       return true;
/*       */     }
/*  5160 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean postEvent(Event paramEvent)
/*       */   {
/*  5169 */     ComponentPeer localComponentPeer = this.peer;
/*       */ 
/*  5171 */     if (handleEvent(paramEvent)) {
/*  5172 */       paramEvent.consume();
/*  5173 */       return true;
/*       */     }
/*       */ 
/*  5176 */     Container localContainer = this.parent;
/*  5177 */     int i = paramEvent.x;
/*  5178 */     int j = paramEvent.y;
/*  5179 */     if (localContainer != null) {
/*  5180 */       paramEvent.translate(this.x, this.y);
/*  5181 */       if (localContainer.postEvent(paramEvent)) {
/*  5182 */         paramEvent.consume();
/*  5183 */         return true;
/*       */       }
/*       */ 
/*  5186 */       paramEvent.x = i;
/*  5187 */       paramEvent.y = j;
/*       */     }
/*  5189 */     return false;
/*       */   }
/*       */ 
/*       */   public synchronized void addComponentListener(ComponentListener paramComponentListener)
/*       */   {
/*  5210 */     if (paramComponentListener == null) {
/*  5211 */       return;
/*       */     }
/*  5213 */     this.componentListener = AWTEventMulticaster.add(this.componentListener, paramComponentListener);
/*  5214 */     this.newEventsOnly = true;
/*       */   }
/*       */ 
/*       */   public synchronized void removeComponentListener(ComponentListener paramComponentListener)
/*       */   {
/*  5234 */     if (paramComponentListener == null) {
/*  5235 */       return;
/*       */     }
/*  5237 */     this.componentListener = AWTEventMulticaster.remove(this.componentListener, paramComponentListener);
/*       */   }
/*       */ 
/*       */   public synchronized ComponentListener[] getComponentListeners()
/*       */   {
/*  5253 */     return (ComponentListener[])getListeners(ComponentListener.class);
/*       */   }
/*       */ 
/*       */   public synchronized void addFocusListener(FocusListener paramFocusListener)
/*       */   {
/*  5272 */     if (paramFocusListener == null) {
/*  5273 */       return;
/*       */     }
/*  5275 */     this.focusListener = AWTEventMulticaster.add(this.focusListener, paramFocusListener);
/*  5276 */     this.newEventsOnly = true;
/*       */ 
/*  5280 */     if ((this.peer instanceof LightweightPeer))
/*  5281 */       this.parent.proxyEnableEvents(4L);
/*       */   }
/*       */ 
/*       */   public synchronized void removeFocusListener(FocusListener paramFocusListener)
/*       */   {
/*  5303 */     if (paramFocusListener == null) {
/*  5304 */       return;
/*       */     }
/*  5306 */     this.focusListener = AWTEventMulticaster.remove(this.focusListener, paramFocusListener);
/*       */   }
/*       */ 
/*       */   public synchronized FocusListener[] getFocusListeners()
/*       */   {
/*  5322 */     return (FocusListener[])getListeners(FocusListener.class);
/*       */   }
/*       */ 
/*       */   public void addHierarchyListener(HierarchyListener paramHierarchyListener)
/*       */   {
/*  5342 */     if (paramHierarchyListener == null)
/*       */       return;
/*       */     int i;
/*  5346 */     synchronized (this) {
/*  5347 */       i = (this.hierarchyListener == null) && ((this.eventMask & 0x8000) == 0L) ? 1 : 0;
/*       */ 
/*  5350 */       this.hierarchyListener = AWTEventMulticaster.add(this.hierarchyListener, paramHierarchyListener);
/*  5351 */       i = (i != 0) && (this.hierarchyListener != null) ? 1 : 0;
/*  5352 */       this.newEventsOnly = true;
/*       */     }
/*  5354 */     if (i != 0)
/*  5355 */       synchronized (getTreeLock()) {
/*  5356 */         adjustListeningChildrenOnParent(32768L, 1);
/*       */       }
/*       */   }
/*       */ 
/*       */   public void removeHierarchyListener(HierarchyListener paramHierarchyListener)
/*       */   {
/*  5380 */     if (paramHierarchyListener == null)
/*       */       return;
/*       */     int i;
/*  5384 */     synchronized (this) {
/*  5385 */       i = (this.hierarchyListener != null) && ((this.eventMask & 0x8000) == 0L) ? 1 : 0;
/*       */ 
/*  5388 */       this.hierarchyListener = AWTEventMulticaster.remove(this.hierarchyListener, paramHierarchyListener);
/*       */ 
/*  5390 */       i = (i != 0) && (this.hierarchyListener == null) ? 1 : 0;
/*       */     }
/*  5392 */     if (i != 0)
/*  5393 */       synchronized (getTreeLock()) {
/*  5394 */         adjustListeningChildrenOnParent(32768L, -1);
/*       */       }
/*       */   }
/*       */ 
/*       */   public synchronized HierarchyListener[] getHierarchyListeners()
/*       */   {
/*  5413 */     return (HierarchyListener[])getListeners(HierarchyListener.class);
/*       */   }
/*       */ 
/*       */   public void addHierarchyBoundsListener(HierarchyBoundsListener paramHierarchyBoundsListener)
/*       */   {
/*  5433 */     if (paramHierarchyBoundsListener == null)
/*       */       return;
/*       */     int i;
/*  5437 */     synchronized (this) {
/*  5438 */       i = (this.hierarchyBoundsListener == null) && ((this.eventMask & 0x10000) == 0L) ? 1 : 0;
/*       */ 
/*  5441 */       this.hierarchyBoundsListener = AWTEventMulticaster.add(this.hierarchyBoundsListener, paramHierarchyBoundsListener);
/*       */ 
/*  5443 */       i = (i != 0) && (this.hierarchyBoundsListener != null) ? 1 : 0;
/*       */ 
/*  5445 */       this.newEventsOnly = true;
/*       */     }
/*  5447 */     if (i != 0)
/*  5448 */       synchronized (getTreeLock()) {
/*  5449 */         adjustListeningChildrenOnParent(65536L, 1);
/*       */       }
/*       */   }
/*       */ 
/*       */   public void removeHierarchyBoundsListener(HierarchyBoundsListener paramHierarchyBoundsListener)
/*       */   {
/*  5473 */     if (paramHierarchyBoundsListener == null)
/*       */       return;
/*       */     int i;
/*  5477 */     synchronized (this) {
/*  5478 */       i = (this.hierarchyBoundsListener != null) && ((this.eventMask & 0x10000) == 0L) ? 1 : 0;
/*       */ 
/*  5481 */       this.hierarchyBoundsListener = AWTEventMulticaster.remove(this.hierarchyBoundsListener, paramHierarchyBoundsListener);
/*       */ 
/*  5483 */       i = (i != 0) && (this.hierarchyBoundsListener == null) ? 1 : 0;
/*       */     }
/*       */ 
/*  5486 */     if (i != 0)
/*  5487 */       synchronized (getTreeLock()) {
/*  5488 */         adjustListeningChildrenOnParent(65536L, -1);
/*       */       }
/*       */   }
/*       */ 
/*       */   int numListening(long paramLong)
/*       */   {
/*  5497 */     if ((eventLog.isLoggable(500)) && 
/*  5498 */       (paramLong != 32768L) && (paramLong != 65536L))
/*       */     {
/*  5501 */       eventLog.fine("Assertion failed");
/*       */     }
/*       */ 
/*  5504 */     if (((paramLong == 32768L) && ((this.hierarchyListener != null) || ((this.eventMask & 0x8000) != 0L))) || ((paramLong == 65536L) && ((this.hierarchyBoundsListener != null) || ((this.eventMask & 0x10000) != 0L))))
/*       */     {
/*  5510 */       return 1;
/*       */     }
/*  5512 */     return 0;
/*       */   }
/*       */ 
/*       */   int countHierarchyMembers()
/*       */   {
/*  5518 */     return 1;
/*       */   }
/*       */ 
/*       */   int createHierarchyEvents(int paramInt, Component paramComponent, Container paramContainer, long paramLong, boolean paramBoolean)
/*       */   {
/*       */     HierarchyEvent localHierarchyEvent;
/*  5524 */     switch (paramInt) {
/*       */     case 1400:
/*  5526 */       if ((this.hierarchyListener != null) || ((this.eventMask & 0x8000) != 0L) || (paramBoolean))
/*       */       {
/*  5529 */         localHierarchyEvent = new HierarchyEvent(this, paramInt, paramComponent, paramContainer, paramLong);
/*       */ 
/*  5532 */         dispatchEvent(localHierarchyEvent);
/*  5533 */         return 1;
/*       */       }
/*       */       break;
/*       */     case 1401:
/*       */     case 1402:
/*  5538 */       if ((eventLog.isLoggable(500)) && 
/*  5539 */         (paramLong != 0L)) {
/*  5540 */         eventLog.fine("Assertion (changeFlags == 0) failed");
/*       */       }
/*       */ 
/*  5543 */       if ((this.hierarchyBoundsListener != null) || ((this.eventMask & 0x10000) != 0L) || (paramBoolean))
/*       */       {
/*  5546 */         localHierarchyEvent = new HierarchyEvent(this, paramInt, paramComponent, paramContainer);
/*       */ 
/*  5548 */         dispatchEvent(localHierarchyEvent);
/*  5549 */         return 1;
/*       */       }
/*       */ 
/*       */       break;
/*       */     default:
/*  5554 */       if (eventLog.isLoggable(500)) {
/*  5555 */         eventLog.fine("This code must never be reached");
/*       */       }
/*       */       break;
/*       */     }
/*  5559 */     return 0;
/*       */   }
/*       */ 
/*       */   public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners()
/*       */   {
/*  5575 */     return (HierarchyBoundsListener[])getListeners(HierarchyBoundsListener.class);
/*       */   }
/*       */ 
/*       */   void adjustListeningChildrenOnParent(long paramLong, int paramInt)
/*       */   {
/*  5585 */     if (this.parent != null)
/*  5586 */       this.parent.adjustListeningChildren(paramLong, paramInt);
/*       */   }
/*       */ 
/*       */   public synchronized void addKeyListener(KeyListener paramKeyListener)
/*       */   {
/*  5605 */     if (paramKeyListener == null) {
/*  5606 */       return;
/*       */     }
/*  5608 */     this.keyListener = AWTEventMulticaster.add(this.keyListener, paramKeyListener);
/*  5609 */     this.newEventsOnly = true;
/*       */ 
/*  5613 */     if ((this.peer instanceof LightweightPeer))
/*  5614 */       this.parent.proxyEnableEvents(8L);
/*       */   }
/*       */ 
/*       */   public synchronized void removeKeyListener(KeyListener paramKeyListener)
/*       */   {
/*  5636 */     if (paramKeyListener == null) {
/*  5637 */       return;
/*       */     }
/*  5639 */     this.keyListener = AWTEventMulticaster.remove(this.keyListener, paramKeyListener);
/*       */   }
/*       */ 
/*       */   public synchronized KeyListener[] getKeyListeners()
/*       */   {
/*  5655 */     return (KeyListener[])getListeners(KeyListener.class);
/*       */   }
/*       */ 
/*       */   public synchronized void addMouseListener(MouseListener paramMouseListener)
/*       */   {
/*  5674 */     if (paramMouseListener == null) {
/*  5675 */       return;
/*       */     }
/*  5677 */     this.mouseListener = AWTEventMulticaster.add(this.mouseListener, paramMouseListener);
/*  5678 */     this.newEventsOnly = true;
/*       */ 
/*  5682 */     if ((this.peer instanceof LightweightPeer))
/*  5683 */       this.parent.proxyEnableEvents(16L);
/*       */   }
/*       */ 
/*       */   public synchronized void removeMouseListener(MouseListener paramMouseListener)
/*       */   {
/*  5705 */     if (paramMouseListener == null) {
/*  5706 */       return;
/*       */     }
/*  5708 */     this.mouseListener = AWTEventMulticaster.remove(this.mouseListener, paramMouseListener);
/*       */   }
/*       */ 
/*       */   public synchronized MouseListener[] getMouseListeners()
/*       */   {
/*  5724 */     return (MouseListener[])getListeners(MouseListener.class);
/*       */   }
/*       */ 
/*       */   public synchronized void addMouseMotionListener(MouseMotionListener paramMouseMotionListener)
/*       */   {
/*  5743 */     if (paramMouseMotionListener == null) {
/*  5744 */       return;
/*       */     }
/*  5746 */     this.mouseMotionListener = AWTEventMulticaster.add(this.mouseMotionListener, paramMouseMotionListener);
/*  5747 */     this.newEventsOnly = true;
/*       */ 
/*  5751 */     if ((this.peer instanceof LightweightPeer))
/*  5752 */       this.parent.proxyEnableEvents(32L);
/*       */   }
/*       */ 
/*       */   public synchronized void removeMouseMotionListener(MouseMotionListener paramMouseMotionListener)
/*       */   {
/*  5774 */     if (paramMouseMotionListener == null) {
/*  5775 */       return;
/*       */     }
/*  5777 */     this.mouseMotionListener = AWTEventMulticaster.remove(this.mouseMotionListener, paramMouseMotionListener);
/*       */   }
/*       */ 
/*       */   public synchronized MouseMotionListener[] getMouseMotionListeners()
/*       */   {
/*  5793 */     return (MouseMotionListener[])getListeners(MouseMotionListener.class);
/*       */   }
/*       */ 
/*       */   public synchronized void addMouseWheelListener(MouseWheelListener paramMouseWheelListener)
/*       */   {
/*  5817 */     if (paramMouseWheelListener == null) {
/*  5818 */       return;
/*       */     }
/*  5820 */     this.mouseWheelListener = AWTEventMulticaster.add(this.mouseWheelListener, paramMouseWheelListener);
/*  5821 */     this.newEventsOnly = true;
/*       */ 
/*  5825 */     if ((this.peer instanceof LightweightPeer))
/*  5826 */       this.parent.proxyEnableEvents(131072L);
/*       */   }
/*       */ 
/*       */   public synchronized void removeMouseWheelListener(MouseWheelListener paramMouseWheelListener)
/*       */   {
/*  5847 */     if (paramMouseWheelListener == null) {
/*  5848 */       return;
/*       */     }
/*  5850 */     this.mouseWheelListener = AWTEventMulticaster.remove(this.mouseWheelListener, paramMouseWheelListener);
/*       */   }
/*       */ 
/*       */   public synchronized MouseWheelListener[] getMouseWheelListeners()
/*       */   {
/*  5866 */     return (MouseWheelListener[])getListeners(MouseWheelListener.class);
/*       */   }
/*       */ 
/*       */   public synchronized void addInputMethodListener(InputMethodListener paramInputMethodListener)
/*       */   {
/*  5889 */     if (paramInputMethodListener == null) {
/*  5890 */       return;
/*       */     }
/*  5892 */     this.inputMethodListener = AWTEventMulticaster.add(this.inputMethodListener, paramInputMethodListener);
/*  5893 */     this.newEventsOnly = true;
/*       */   }
/*       */ 
/*       */   public synchronized void removeInputMethodListener(InputMethodListener paramInputMethodListener)
/*       */   {
/*  5914 */     if (paramInputMethodListener == null) {
/*  5915 */       return;
/*       */     }
/*  5917 */     this.inputMethodListener = AWTEventMulticaster.remove(this.inputMethodListener, paramInputMethodListener);
/*       */   }
/*       */ 
/*       */   public synchronized InputMethodListener[] getInputMethodListeners()
/*       */   {
/*  5933 */     return (InputMethodListener[])getListeners(InputMethodListener.class);
/*       */   }
/*       */ 
/*       */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*       */   {
/*  5979 */     Object localObject = null;
/*  5980 */     if (paramClass == ComponentListener.class)
/*  5981 */       localObject = this.componentListener;
/*  5982 */     else if (paramClass == FocusListener.class)
/*  5983 */       localObject = this.focusListener;
/*  5984 */     else if (paramClass == HierarchyListener.class)
/*  5985 */       localObject = this.hierarchyListener;
/*  5986 */     else if (paramClass == HierarchyBoundsListener.class)
/*  5987 */       localObject = this.hierarchyBoundsListener;
/*  5988 */     else if (paramClass == KeyListener.class)
/*  5989 */       localObject = this.keyListener;
/*  5990 */     else if (paramClass == MouseListener.class)
/*  5991 */       localObject = this.mouseListener;
/*  5992 */     else if (paramClass == MouseMotionListener.class)
/*  5993 */       localObject = this.mouseMotionListener;
/*  5994 */     else if (paramClass == MouseWheelListener.class)
/*  5995 */       localObject = this.mouseWheelListener;
/*  5996 */     else if (paramClass == InputMethodListener.class)
/*  5997 */       localObject = this.inputMethodListener;
/*  5998 */     else if (paramClass == PropertyChangeListener.class) {
/*  5999 */       return (EventListener[])getPropertyChangeListeners();
/*       */     }
/*  6001 */     return AWTEventMulticaster.getListeners((EventListener)localObject, paramClass);
/*       */   }
/*       */ 
/*       */   public InputMethodRequests getInputMethodRequests()
/*       */   {
/*  6017 */     return null;
/*       */   }
/*       */ 
/*       */   public java.awt.im.InputContext getInputContext()
/*       */   {
/*  6032 */     Container localContainer = this.parent;
/*  6033 */     if (localContainer == null) {
/*  6034 */       return null;
/*       */     }
/*  6036 */     return localContainer.getInputContext();
/*       */   }
/*       */ 
/*       */   protected final void enableEvents(long paramLong)
/*       */   {
/*  6058 */     long l = 0L;
/*  6059 */     synchronized (this) {
/*  6060 */       if (((paramLong & 0x8000) != 0L) && (this.hierarchyListener == null) && ((this.eventMask & 0x8000) == 0L))
/*       */       {
/*  6063 */         l |= 32768L;
/*       */       }
/*  6065 */       if (((paramLong & 0x10000) != 0L) && (this.hierarchyBoundsListener == null) && ((this.eventMask & 0x10000) == 0L))
/*       */       {
/*  6068 */         l |= 65536L;
/*       */       }
/*  6070 */       this.eventMask |= paramLong;
/*  6071 */       this.newEventsOnly = true;
/*       */     }
/*       */ 
/*  6076 */     if ((this.peer instanceof LightweightPeer)) {
/*  6077 */       this.parent.proxyEnableEvents(this.eventMask);
/*       */     }
/*  6079 */     if (l != 0L)
/*  6080 */       synchronized (getTreeLock()) {
/*  6081 */         adjustListeningChildrenOnParent(l, 1);
/*       */       }
/*       */   }
/*       */ 
/*       */   protected final void disableEvents(long paramLong)
/*       */   {
/*  6094 */     long l = 0L;
/*  6095 */     synchronized (this) {
/*  6096 */       if (((paramLong & 0x8000) != 0L) && (this.hierarchyListener == null) && ((this.eventMask & 0x8000) != 0L))
/*       */       {
/*  6099 */         l |= 32768L;
/*       */       }
/*  6101 */       if (((paramLong & 0x10000) != 0L) && (this.hierarchyBoundsListener == null) && ((this.eventMask & 0x10000) != 0L))
/*       */       {
/*  6104 */         l |= 65536L;
/*       */       }
/*  6106 */       this.eventMask &= (paramLong ^ 0xFFFFFFFF);
/*       */     }
/*  6108 */     if (l != 0L)
/*  6109 */       synchronized (getTreeLock()) {
/*  6110 */         adjustListeningChildrenOnParent(l, -1);
/*       */       }
/*       */   }
/*       */ 
/*       */   private boolean checkCoalescing()
/*       */   {
/*  6140 */     if (getClass().getClassLoader() == null) {
/*  6141 */       return false;
/*       */     }
/*  6143 */     final Class localClass = getClass();
/*  6144 */     synchronized (coalesceMap)
/*       */     {
/*  6146 */       Boolean localBoolean1 = (Boolean)coalesceMap.get(localClass);
/*  6147 */       if (localBoolean1 != null) {
/*  6148 */         return localBoolean1.booleanValue();
/*       */       }
/*       */ 
/*  6152 */       Boolean localBoolean2 = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*       */       {
/*       */         public Boolean run() {
/*  6155 */           return Boolean.valueOf(Component.isCoalesceEventsOverriden(localClass));
/*       */         }
/*       */       });
/*  6159 */       coalesceMap.put(localClass, localBoolean2);
/*  6160 */       return localBoolean2.booleanValue();
/*       */     }
/*       */   }
/*       */ 
/*       */   private static boolean isCoalesceEventsOverriden(Class<?> paramClass)
/*       */   {
/*  6177 */     assert (Thread.holdsLock(coalesceMap));
/*       */ 
/*  6180 */     Class localClass = paramClass.getSuperclass();
/*  6181 */     if (localClass == null)
/*       */     {
/*  6184 */       return false;
/*       */     }
/*  6186 */     if (localClass.getClassLoader() != null) {
/*  6187 */       Boolean localBoolean = (Boolean)coalesceMap.get(localClass);
/*  6188 */       if (localBoolean == null)
/*       */       {
/*  6190 */         if (isCoalesceEventsOverriden(localClass)) {
/*  6191 */           coalesceMap.put(localClass, Boolean.valueOf(true));
/*  6192 */           return true;
/*       */         }
/*  6194 */       } else if (localBoolean.booleanValue()) {
/*  6195 */         return true;
/*       */       }
/*       */     }
/*       */ 
/*       */     try
/*       */     {
/*  6201 */       paramClass.getDeclaredMethod("coalesceEvents", coalesceEventsParams);
/*       */ 
/*  6204 */       return true;
/*       */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*       */     }
/*  6207 */     return false;
/*       */   }
/*       */ 
/*       */   final boolean isCoalescingEnabled()
/*       */   {
/*  6215 */     return this.coalescingEnabled;
/*       */   }
/*       */ 
/*       */   protected AWTEvent coalesceEvents(AWTEvent paramAWTEvent1, AWTEvent paramAWTEvent2)
/*       */   {
/*  6247 */     return null;
/*       */   }
/*       */ 
/*       */   protected void processEvent(AWTEvent paramAWTEvent)
/*       */   {
/*  6271 */     if ((paramAWTEvent instanceof FocusEvent)) {
/*  6272 */       processFocusEvent((FocusEvent)paramAWTEvent);
/*       */     }
/*  6274 */     else if ((paramAWTEvent instanceof MouseEvent)) {
/*  6275 */       switch (paramAWTEvent.getID()) {
/*       */       case 500:
/*       */       case 501:
/*       */       case 502:
/*       */       case 504:
/*       */       case 505:
/*  6281 */         processMouseEvent((MouseEvent)paramAWTEvent);
/*  6282 */         break;
/*       */       case 503:
/*       */       case 506:
/*  6285 */         processMouseMotionEvent((MouseEvent)paramAWTEvent);
/*  6286 */         break;
/*       */       case 507:
/*  6288 */         processMouseWheelEvent((MouseWheelEvent)paramAWTEvent);
/*       */       }
/*       */ 
/*       */     }
/*  6292 */     else if ((paramAWTEvent instanceof KeyEvent)) {
/*  6293 */       processKeyEvent((KeyEvent)paramAWTEvent);
/*       */     }
/*  6295 */     else if ((paramAWTEvent instanceof ComponentEvent))
/*  6296 */       processComponentEvent((ComponentEvent)paramAWTEvent);
/*  6297 */     else if ((paramAWTEvent instanceof InputMethodEvent))
/*  6298 */       processInputMethodEvent((InputMethodEvent)paramAWTEvent);
/*  6299 */     else if ((paramAWTEvent instanceof HierarchyEvent))
/*  6300 */       switch (paramAWTEvent.getID()) {
/*       */       case 1400:
/*  6302 */         processHierarchyEvent((HierarchyEvent)paramAWTEvent);
/*  6303 */         break;
/*       */       case 1401:
/*       */       case 1402:
/*  6306 */         processHierarchyBoundsEvent((HierarchyEvent)paramAWTEvent);
/*       */       }
/*       */   }
/*       */ 
/*       */   protected void processComponentEvent(ComponentEvent paramComponentEvent)
/*       */   {
/*  6337 */     ComponentListener localComponentListener = this.componentListener;
/*  6338 */     if (localComponentListener != null) {
/*  6339 */       int i = paramComponentEvent.getID();
/*  6340 */       switch (i) {
/*       */       case 101:
/*  6342 */         localComponentListener.componentResized(paramComponentEvent);
/*  6343 */         break;
/*       */       case 100:
/*  6345 */         localComponentListener.componentMoved(paramComponentEvent);
/*  6346 */         break;
/*       */       case 102:
/*  6348 */         localComponentListener.componentShown(paramComponentEvent);
/*  6349 */         break;
/*       */       case 103:
/*  6351 */         localComponentListener.componentHidden(paramComponentEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processFocusEvent(FocusEvent paramFocusEvent)
/*       */   {
/*  6400 */     FocusListener localFocusListener = this.focusListener;
/*  6401 */     if (localFocusListener != null) {
/*  6402 */       int i = paramFocusEvent.getID();
/*  6403 */       switch (i) {
/*       */       case 1004:
/*  6405 */         localFocusListener.focusGained(paramFocusEvent);
/*  6406 */         break;
/*       */       case 1005:
/*  6408 */         localFocusListener.focusLost(paramFocusEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processKeyEvent(KeyEvent paramKeyEvent)
/*       */   {
/*  6466 */     KeyListener localKeyListener = this.keyListener;
/*  6467 */     if (localKeyListener != null) {
/*  6468 */       int i = paramKeyEvent.getID();
/*  6469 */       switch (i) {
/*       */       case 400:
/*  6471 */         localKeyListener.keyTyped(paramKeyEvent);
/*  6472 */         break;
/*       */       case 401:
/*  6474 */         localKeyListener.keyPressed(paramKeyEvent);
/*  6475 */         break;
/*       */       case 402:
/*  6477 */         localKeyListener.keyReleased(paramKeyEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processMouseEvent(MouseEvent paramMouseEvent)
/*       */   {
/*  6508 */     MouseListener localMouseListener = this.mouseListener;
/*  6509 */     if (localMouseListener != null) {
/*  6510 */       int i = paramMouseEvent.getID();
/*  6511 */       switch (i) {
/*       */       case 501:
/*  6513 */         localMouseListener.mousePressed(paramMouseEvent);
/*  6514 */         break;
/*       */       case 502:
/*  6516 */         localMouseListener.mouseReleased(paramMouseEvent);
/*  6517 */         break;
/*       */       case 500:
/*  6519 */         localMouseListener.mouseClicked(paramMouseEvent);
/*  6520 */         break;
/*       */       case 505:
/*  6522 */         localMouseListener.mouseExited(paramMouseEvent);
/*  6523 */         break;
/*       */       case 504:
/*  6525 */         localMouseListener.mouseEntered(paramMouseEvent);
/*       */       case 503:
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processMouseMotionEvent(MouseEvent paramMouseEvent)
/*       */   {
/*  6556 */     MouseMotionListener localMouseMotionListener = this.mouseMotionListener;
/*  6557 */     if (localMouseMotionListener != null) {
/*  6558 */       int i = paramMouseEvent.getID();
/*  6559 */       switch (i) {
/*       */       case 503:
/*  6561 */         localMouseMotionListener.mouseMoved(paramMouseEvent);
/*  6562 */         break;
/*       */       case 506:
/*  6564 */         localMouseMotionListener.mouseDragged(paramMouseEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processMouseWheelEvent(MouseWheelEvent paramMouseWheelEvent)
/*       */   {
/*  6599 */     MouseWheelListener localMouseWheelListener = this.mouseWheelListener;
/*  6600 */     if (localMouseWheelListener != null) {
/*  6601 */       int i = paramMouseWheelEvent.getID();
/*  6602 */       switch (i) {
/*       */       case 507:
/*  6604 */         localMouseWheelListener.mouseWheelMoved(paramMouseWheelEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   boolean postsOldMouseEvents()
/*       */   {
/*  6611 */     return false;
/*       */   }
/*       */ 
/*       */   protected void processInputMethodEvent(InputMethodEvent paramInputMethodEvent)
/*       */   {
/*  6639 */     InputMethodListener localInputMethodListener = this.inputMethodListener;
/*  6640 */     if (localInputMethodListener != null) {
/*  6641 */       int i = paramInputMethodEvent.getID();
/*  6642 */       switch (i) {
/*       */       case 1100:
/*  6644 */         localInputMethodListener.inputMethodTextChanged(paramInputMethodEvent);
/*  6645 */         break;
/*       */       case 1101:
/*  6647 */         localInputMethodListener.caretPositionChanged(paramInputMethodEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processHierarchyEvent(HierarchyEvent paramHierarchyEvent)
/*       */   {
/*  6678 */     HierarchyListener localHierarchyListener = this.hierarchyListener;
/*  6679 */     if (localHierarchyListener != null) {
/*  6680 */       int i = paramHierarchyEvent.getID();
/*  6681 */       switch (i) {
/*       */       case 1400:
/*  6683 */         localHierarchyListener.hierarchyChanged(paramHierarchyEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void processHierarchyBoundsEvent(HierarchyEvent paramHierarchyEvent)
/*       */   {
/*  6714 */     HierarchyBoundsListener localHierarchyBoundsListener = this.hierarchyBoundsListener;
/*  6715 */     if (localHierarchyBoundsListener != null) {
/*  6716 */       int i = paramHierarchyEvent.getID();
/*  6717 */       switch (i) {
/*       */       case 1401:
/*  6719 */         localHierarchyBoundsListener.ancestorMoved(paramHierarchyEvent);
/*  6720 */         break;
/*       */       case 1402:
/*  6722 */         localHierarchyBoundsListener.ancestorResized(paramHierarchyEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean handleEvent(Event paramEvent)
/*       */   {
/*  6734 */     switch (paramEvent.id) {
/*       */     case 504:
/*  6736 */       return mouseEnter(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 505:
/*  6739 */       return mouseExit(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 503:
/*  6742 */       return mouseMove(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 501:
/*  6745 */       return mouseDown(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 506:
/*  6748 */       return mouseDrag(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 502:
/*  6751 */       return mouseUp(paramEvent, paramEvent.x, paramEvent.y);
/*       */     case 401:
/*       */     case 403:
/*  6755 */       return keyDown(paramEvent, paramEvent.key);
/*       */     case 402:
/*       */     case 404:
/*  6759 */       return keyUp(paramEvent, paramEvent.key);
/*       */     case 1001:
/*  6762 */       return action(paramEvent, paramEvent.arg);
/*       */     case 1004:
/*  6764 */       return gotFocus(paramEvent, paramEvent.arg);
/*       */     case 1005:
/*  6766 */       return lostFocus(paramEvent, paramEvent.arg);
/*       */     }
/*  6768 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6777 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseDrag(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6786 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseUp(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6795 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseMove(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6804 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseEnter(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6813 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean mouseExit(Event paramEvent, int paramInt1, int paramInt2)
/*       */   {
/*  6822 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean keyDown(Event paramEvent, int paramInt)
/*       */   {
/*  6831 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean keyUp(Event paramEvent, int paramInt)
/*       */   {
/*  6840 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean action(Event paramEvent, Object paramObject)
/*       */   {
/*  6850 */     return false;
/*       */   }
/*       */ 
/*       */   public void addNotify()
/*       */   {
/*  6868 */     synchronized (getTreeLock()) {
/*  6869 */       Object localObject1 = this.peer;
/*  6870 */       if ((localObject1 == null) || ((localObject1 instanceof LightweightPeer))) {
/*  6871 */         if (localObject1 == null)
/*       */         {
/*  6874 */           this.peer = (localObject1 = getToolkit().createComponent(this));
/*       */         }
/*       */ 
/*  6881 */         if (this.parent != null) {
/*  6882 */           long l = 0L;
/*  6883 */           if ((this.mouseListener != null) || ((this.eventMask & 0x10) != 0L)) {
/*  6884 */             l |= 16L;
/*       */           }
/*  6886 */           if ((this.mouseMotionListener != null) || ((this.eventMask & 0x20) != 0L))
/*       */           {
/*  6888 */             l |= 32L;
/*       */           }
/*  6890 */           if ((this.mouseWheelListener != null) || ((this.eventMask & 0x20000) != 0L))
/*       */           {
/*  6892 */             l |= 131072L;
/*       */           }
/*  6894 */           if ((this.focusListener != null) || ((this.eventMask & 0x4) != 0L)) {
/*  6895 */             l |= 4L;
/*       */           }
/*  6897 */           if ((this.keyListener != null) || ((this.eventMask & 0x8) != 0L)) {
/*  6898 */             l |= 8L;
/*       */           }
/*  6900 */           if (l != 0L) {
/*  6901 */             this.parent.proxyEnableEvents(l);
/*       */           }
/*       */         }
/*       */       }
/*       */       else
/*       */       {
/*  6907 */         Container localContainer = getContainer();
/*  6908 */         if ((localContainer != null) && (localContainer.isLightweight())) {
/*  6909 */           relocateComponent();
/*  6910 */           if (!localContainer.isRecursivelyVisibleUpToHeavyweightContainer())
/*       */           {
/*  6912 */             ((ComponentPeer)localObject1).setVisible(false);
/*       */           }
/*       */         }
/*       */       }
/*  6916 */       invalidate();
/*       */ 
/*  6918 */       int i = this.popups != null ? this.popups.size() : 0;
/*  6919 */       for (int j = 0; j < i; j++) {
/*  6920 */         PopupMenu localPopupMenu = (PopupMenu)this.popups.elementAt(j);
/*  6921 */         localPopupMenu.addNotify();
/*       */       }
/*       */ 
/*  6924 */       if (this.dropTarget != null) this.dropTarget.addNotify((ComponentPeer)localObject1);
/*       */ 
/*  6926 */       this.peerFont = getFont();
/*       */ 
/*  6928 */       if ((getContainer() != null) && (!this.isAddNotifyComplete)) {
/*  6929 */         getContainer().increaseComponentCount(this);
/*       */       }
/*       */ 
/*  6934 */       updateZOrder();
/*       */ 
/*  6936 */       if (!this.isAddNotifyComplete) {
/*  6937 */         mixOnShowing();
/*       */       }
/*       */ 
/*  6940 */       this.isAddNotifyComplete = true;
/*       */ 
/*  6942 */       if ((this.hierarchyListener != null) || ((this.eventMask & 0x8000) != 0L) || (Toolkit.enabledOnToolkit(32768L)))
/*       */       {
/*  6945 */         HierarchyEvent localHierarchyEvent = new HierarchyEvent(this, 1400, this, this.parent, 0x2 | (isRecursivelyVisible() ? 4 : 0));
/*       */ 
/*  6952 */         dispatchEvent(localHierarchyEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public void removeNotify()
/*       */   {
/*  6971 */     KeyboardFocusManager.clearMostRecentFocusOwner(this);
/*  6972 */     if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == this)
/*       */     {
/*  6975 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalPermanentFocusOwner(null);
/*       */     }
/*       */ 
/*  6979 */     synchronized (getTreeLock()) {
/*  6980 */       if ((isFocusOwner()) && (KeyboardFocusManager.isAutoFocusTransferEnabledFor(this))) {
/*  6981 */         transferFocus(true);
/*       */       }
/*       */ 
/*  6984 */       if ((getContainer() != null) && (this.isAddNotifyComplete)) {
/*  6985 */         getContainer().decreaseComponentCount(this);
/*       */       }
/*       */ 
/*  6988 */       int i = this.popups != null ? this.popups.size() : 0;
/*  6989 */       for (int j = 0; j < i; j++) {
/*  6990 */         PopupMenu localPopupMenu = (PopupMenu)this.popups.elementAt(j);
/*  6991 */         localPopupMenu.removeNotify();
/*       */       }
/*       */ 
/*  6996 */       if ((this.eventMask & 0x1000) != 0L) {
/*  6997 */         localObject1 = getInputContext();
/*  6998 */         if (localObject1 != null) {
/*  6999 */           ((java.awt.im.InputContext)localObject1).removeNotify(this);
/*       */         }
/*       */       }
/*       */ 
/*  7003 */       Object localObject1 = this.peer;
/*  7004 */       if (localObject1 != null) {
/*  7005 */         boolean bool = isLightweight();
/*       */ 
/*  7007 */         if ((this.bufferStrategy instanceof FlipBufferStrategy)) {
/*  7008 */           ((FlipBufferStrategy)this.bufferStrategy).destroyBuffers();
/*       */         }
/*       */ 
/*  7011 */         if (this.dropTarget != null) this.dropTarget.removeNotify(this.peer);
/*       */ 
/*  7014 */         if (this.visible) {
/*  7015 */           ((ComponentPeer)localObject1).setVisible(false);
/*       */         }
/*       */ 
/*  7018 */         this.peer = null;
/*  7019 */         this.peerFont = null;
/*       */ 
/*  7021 */         Toolkit.getEventQueue().removeSourceEvents(this, false);
/*  7022 */         KeyboardFocusManager.getCurrentKeyboardFocusManager().discardKeyEvents(this);
/*       */ 
/*  7025 */         ((ComponentPeer)localObject1).dispose();
/*       */ 
/*  7027 */         mixOnHiding(bool);
/*       */ 
/*  7029 */         this.isAddNotifyComplete = false;
/*       */ 
/*  7032 */         this.compoundShape = null;
/*       */       }
/*       */ 
/*  7035 */       if ((this.hierarchyListener != null) || ((this.eventMask & 0x8000) != 0L) || (Toolkit.enabledOnToolkit(32768L)))
/*       */       {
/*  7038 */         HierarchyEvent localHierarchyEvent = new HierarchyEvent(this, 1400, this, this.parent, 0x2 | (isRecursivelyVisible() ? 4 : 0));
/*       */ 
/*  7045 */         dispatchEvent(localHierarchyEvent);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean gotFocus(Event paramEvent, Object paramObject)
/*       */   {
/*  7056 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean lostFocus(Event paramEvent, Object paramObject)
/*       */   {
/*  7065 */     return false;
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public boolean isFocusTraversable()
/*       */   {
/*  7080 */     if (this.isFocusTraversableOverridden == 0) {
/*  7081 */       this.isFocusTraversableOverridden = 1;
/*       */     }
/*  7083 */     return this.focusable;
/*       */   }
/*       */ 
/*       */   public boolean isFocusable()
/*       */   {
/*  7095 */     return isFocusTraversable();
/*       */   }
/*       */ 
/*       */   public void setFocusable(boolean paramBoolean)
/*       */   {
/*       */     boolean bool;
/*  7110 */     synchronized (this) {
/*  7111 */       bool = this.focusable;
/*  7112 */       this.focusable = paramBoolean;
/*       */     }
/*  7114 */     this.isFocusTraversableOverridden = 2;
/*       */ 
/*  7116 */     firePropertyChange("focusable", bool, paramBoolean);
/*  7117 */     if ((bool) && (!paramBoolean)) {
/*  7118 */       if ((isFocusOwner()) && (KeyboardFocusManager.isAutoFocusTransferEnabled())) {
/*  7119 */         transferFocus(true);
/*       */       }
/*  7121 */       KeyboardFocusManager.clearMostRecentFocusOwner(this);
/*       */     }
/*       */   }
/*       */ 
/*       */   final boolean isFocusTraversableOverridden() {
/*  7126 */     return this.isFocusTraversableOverridden != 1;
/*       */   }
/*       */ 
/*       */   public void setFocusTraversalKeys(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*       */   {
/*  7202 */     if ((paramInt < 0) || (paramInt >= 3)) {
/*  7203 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*       */     }
/*       */ 
/*  7206 */     setFocusTraversalKeys_NoIDCheck(paramInt, paramSet);
/*       */   }
/*       */ 
/*       */   public Set<AWTKeyStroke> getFocusTraversalKeys(int paramInt)
/*       */   {
/*  7236 */     if ((paramInt < 0) || (paramInt >= 3)) {
/*  7237 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*       */     }
/*       */ 
/*  7240 */     return getFocusTraversalKeys_NoIDCheck(paramInt);
/*       */   }
/*       */ 
/*       */   final void setFocusTraversalKeys_NoIDCheck(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*       */   {
/*       */     Set localSet;
/*  7251 */     synchronized (this) {
/*  7252 */       if (this.focusTraversalKeys == null)
/*  7253 */         initializeFocusTraversalKeys();
/*       */       Iterator localIterator;
/*  7256 */       if (paramSet != null) {
/*  7257 */         for (localIterator = paramSet.iterator(); localIterator.hasNext(); ) {
/*  7258 */           Object localObject1 = localIterator.next();
/*       */ 
/*  7260 */           if (localObject1 == null) {
/*  7261 */             throw new IllegalArgumentException("cannot set null focus traversal key");
/*       */           }
/*       */ 
/*  7266 */           if (!(localObject1 instanceof AWTKeyStroke)) {
/*  7267 */             throw new IllegalArgumentException("object is expected to be AWTKeyStroke");
/*       */           }
/*  7269 */           AWTKeyStroke localAWTKeyStroke = (AWTKeyStroke)localObject1;
/*       */ 
/*  7271 */           if (localAWTKeyStroke.getKeyChar() != 65535) {
/*  7272 */             throw new IllegalArgumentException("focus traversal keys cannot map to KEY_TYPED events");
/*       */           }
/*       */ 
/*  7275 */           for (int i = 0; i < this.focusTraversalKeys.length; i++) {
/*  7276 */             if (i != paramInt)
/*       */             {
/*  7280 */               if (getFocusTraversalKeys_NoIDCheck(i).contains(localAWTKeyStroke))
/*       */               {
/*  7282 */                 throw new IllegalArgumentException("focus traversal keys must be unique for a Component");
/*       */               }
/*       */             }
/*       */           }
/*       */         }
/*       */       }
/*  7288 */       localSet = this.focusTraversalKeys[paramInt];
/*  7289 */       this.focusTraversalKeys[paramInt] = (paramSet != null ? Collections.unmodifiableSet(new HashSet(paramSet)) : null);
/*       */     }
/*       */ 
/*  7294 */     firePropertyChange(focusTraversalKeyPropertyNames[paramInt], localSet, paramSet);
/*       */   }
/*       */ 
/*       */   final Set getFocusTraversalKeys_NoIDCheck(int paramInt)
/*       */   {
/*  7299 */     Set localSet = this.focusTraversalKeys != null ? this.focusTraversalKeys[paramInt] : null;
/*       */ 
/*  7303 */     if (localSet != null) {
/*  7304 */       return localSet;
/*       */     }
/*  7306 */     Container localContainer = this.parent;
/*  7307 */     if (localContainer != null) {
/*  7308 */       return localContainer.getFocusTraversalKeys(paramInt);
/*       */     }
/*  7310 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(paramInt);
/*       */   }
/*       */ 
/*       */   public boolean areFocusTraversalKeysSet(int paramInt)
/*       */   {
/*  7335 */     if ((paramInt < 0) || (paramInt >= 3)) {
/*  7336 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*       */     }
/*       */ 
/*  7339 */     return (this.focusTraversalKeys != null) && (this.focusTraversalKeys[paramInt] != null);
/*       */   }
/*       */ 
/*       */   public void setFocusTraversalKeysEnabled(boolean paramBoolean)
/*       */   {
/*       */     boolean bool;
/*  7361 */     synchronized (this) {
/*  7362 */       bool = this.focusTraversalKeysEnabled;
/*  7363 */       this.focusTraversalKeysEnabled = paramBoolean;
/*       */     }
/*  7365 */     firePropertyChange("focusTraversalKeysEnabled", bool, paramBoolean);
/*       */   }
/*       */ 
/*       */   public boolean getFocusTraversalKeysEnabled()
/*       */   {
/*  7384 */     return this.focusTraversalKeysEnabled;
/*       */   }
/*       */ 
/*       */   public void requestFocus()
/*       */   {
/*  7422 */     requestFocusHelper(false, true);
/*       */   }
/*       */ 
/*       */   boolean requestFocus(CausedFocusEvent.Cause paramCause) {
/*  7426 */     return requestFocusHelper(false, true, paramCause);
/*       */   }
/*       */ 
/*       */   protected boolean requestFocus(boolean paramBoolean)
/*       */   {
/*  7489 */     return requestFocusHelper(paramBoolean, true);
/*       */   }
/*       */ 
/*       */   boolean requestFocus(boolean paramBoolean, CausedFocusEvent.Cause paramCause) {
/*  7493 */     return requestFocusHelper(paramBoolean, true, paramCause);
/*       */   }
/*       */ 
/*       */   public boolean requestFocusInWindow()
/*       */   {
/*  7540 */     return requestFocusHelper(false, false);
/*       */   }
/*       */ 
/*       */   boolean requestFocusInWindow(CausedFocusEvent.Cause paramCause) {
/*  7544 */     return requestFocusHelper(false, false, paramCause);
/*       */   }
/*       */ 
/*       */   protected boolean requestFocusInWindow(boolean paramBoolean)
/*       */   {
/*  7605 */     return requestFocusHelper(paramBoolean, false);
/*       */   }
/*       */ 
/*       */   boolean requestFocusInWindow(boolean paramBoolean, CausedFocusEvent.Cause paramCause) {
/*  7609 */     return requestFocusHelper(paramBoolean, false, paramCause);
/*       */   }
/*       */ 
/*       */   final boolean requestFocusHelper(boolean paramBoolean1, boolean paramBoolean2)
/*       */   {
/*  7614 */     return requestFocusHelper(paramBoolean1, paramBoolean2, CausedFocusEvent.Cause.UNKNOWN);
/*       */   }
/*       */ 
/*       */   final boolean requestFocusHelper(boolean paramBoolean1, boolean paramBoolean2, CausedFocusEvent.Cause paramCause)
/*       */   {
/*  7621 */     if (!isRequestFocusAccepted(paramBoolean1, paramBoolean2, paramCause)) {
/*  7622 */       if (focusLog.isLoggable(300)) {
/*  7623 */         focusLog.finest("requestFocus is not accepted");
/*       */       }
/*  7625 */       return false;
/*       */     }
/*       */ 
/*  7629 */     KeyboardFocusManager.setMostRecentFocusOwner(this);
/*       */ 
/*  7631 */     Object localObject = this;
/*  7632 */     while ((localObject != null) && (!(localObject instanceof Window))) {
/*  7633 */       if (!((Component)localObject).isVisible()) {
/*  7634 */         if (focusLog.isLoggable(300)) {
/*  7635 */           focusLog.finest("component is recurively invisible");
/*       */         }
/*  7637 */         return false;
/*       */       }
/*  7639 */       localObject = ((Component)localObject).parent;
/*       */     }
/*       */ 
/*  7642 */     ComponentPeer localComponentPeer = this.peer;
/*  7643 */     Component localComponent = (localComponentPeer instanceof LightweightPeer) ? getNativeContainer() : this;
/*       */ 
/*  7645 */     if ((localComponent == null) || (!localComponent.isVisible())) {
/*  7646 */       if (focusLog.isLoggable(300)) {
/*  7647 */         focusLog.finest("Component is not a part of visible hierarchy");
/*       */       }
/*  7649 */       return false;
/*       */     }
/*  7651 */     localComponentPeer = localComponent.peer;
/*  7652 */     if (localComponentPeer == null) {
/*  7653 */       if (focusLog.isLoggable(300)) {
/*  7654 */         focusLog.finest("Peer is null");
/*       */       }
/*  7656 */       return false;
/*       */     }
/*       */ 
/*  7660 */     long l = EventQueue.getMostRecentEventTime();
/*  7661 */     boolean bool = localComponentPeer.requestFocus(this, paramBoolean1, paramBoolean2, l, paramCause);
/*       */ 
/*  7663 */     if (!bool) {
/*  7664 */       KeyboardFocusManager.getCurrentKeyboardFocusManager(this.appContext).dequeueKeyEvents(l, this);
/*       */ 
/*  7666 */       if (focusLog.isLoggable(300)) {
/*  7667 */         focusLog.finest("Peer request failed");
/*       */       }
/*       */     }
/*  7670 */     else if (focusLog.isLoggable(300)) {
/*  7671 */       focusLog.finest("Pass for " + this);
/*       */     }
/*       */ 
/*  7674 */     return bool;
/*       */   }
/*       */ 
/*       */   private boolean isRequestFocusAccepted(boolean paramBoolean1, boolean paramBoolean2, CausedFocusEvent.Cause paramCause)
/*       */   {
/*  7681 */     if ((!isFocusable()) || (!isVisible())) {
/*  7682 */       if (focusLog.isLoggable(300)) {
/*  7683 */         focusLog.finest("Not focusable or not visible");
/*       */       }
/*  7685 */       return false;
/*       */     }
/*       */ 
/*  7688 */     ComponentPeer localComponentPeer = this.peer;
/*  7689 */     if (localComponentPeer == null) {
/*  7690 */       if (focusLog.isLoggable(300)) {
/*  7691 */         focusLog.finest("peer is null");
/*       */       }
/*  7693 */       return false;
/*       */     }
/*       */ 
/*  7696 */     Window localWindow = getContainingWindow();
/*  7697 */     if ((localWindow == null) || (!localWindow.isFocusableWindow())) {
/*  7698 */       if (focusLog.isLoggable(300)) {
/*  7699 */         focusLog.finest("Component doesn't have toplevel");
/*       */       }
/*  7701 */       return false;
/*       */     }
/*       */ 
/*  7706 */     Component localComponent = KeyboardFocusManager.getMostRecentFocusOwner(localWindow);
/*  7707 */     if (localComponent == null)
/*       */     {
/*  7710 */       localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*  7711 */       if ((localComponent != null) && (localComponent.getContainingWindow() != localWindow)) {
/*  7712 */         localComponent = null;
/*       */       }
/*       */     }
/*       */ 
/*  7716 */     if ((localComponent == this) || (localComponent == null))
/*       */     {
/*  7720 */       if (focusLog.isLoggable(300)) {
/*  7721 */         focusLog.finest("focus owner is null or this");
/*       */       }
/*  7723 */       return true;
/*       */     }
/*       */ 
/*  7726 */     if (CausedFocusEvent.Cause.ACTIVATION == paramCause)
/*       */     {
/*  7733 */       if (focusLog.isLoggable(300)) {
/*  7734 */         focusLog.finest("cause is activation");
/*       */       }
/*  7736 */       return true;
/*       */     }
/*       */ 
/*  7739 */     boolean bool = requestFocusController.acceptRequestFocus(localComponent, this, paramBoolean1, paramBoolean2, paramCause);
/*       */ 
/*  7744 */     if (focusLog.isLoggable(300)) {
/*  7745 */       focusLog.finest("RequestFocusController returns {0}", new Object[] { Boolean.valueOf(bool) });
/*       */     }
/*       */ 
/*  7748 */     return bool;
/*       */   }
/*       */ 
/*       */   static synchronized void setRequestFocusController(RequestFocusController paramRequestFocusController)
/*       */   {
/*  7766 */     if (paramRequestFocusController == null)
/*  7767 */       requestFocusController = new DummyRequestFocusController(null);
/*       */     else
/*  7769 */       requestFocusController = paramRequestFocusController;
/*       */   }
/*       */ 
/*       */   public Container getFocusCycleRootAncestor()
/*       */   {
/*  7788 */     Container localContainer = this.parent;
/*  7789 */     while ((localContainer != null) && (!localContainer.isFocusCycleRoot())) {
/*  7790 */       localContainer = localContainer.parent;
/*       */     }
/*  7792 */     return localContainer;
/*       */   }
/*       */ 
/*       */   public boolean isFocusCycleRoot(Container paramContainer)
/*       */   {
/*  7808 */     Container localContainer = getFocusCycleRootAncestor();
/*  7809 */     return localContainer == paramContainer;
/*       */   }
/*       */ 
/*       */   Container getTraversalRoot() {
/*  7813 */     return getFocusCycleRootAncestor();
/*       */   }
/*       */ 
/*       */   public void transferFocus()
/*       */   {
/*  7823 */     nextFocus();
/*       */   }
/*       */ 
/*       */   @Deprecated
/*       */   public void nextFocus()
/*       */   {
/*  7832 */     transferFocus(false);
/*       */   }
/*       */ 
/*       */   boolean transferFocus(boolean paramBoolean) {
/*  7836 */     if (focusLog.isLoggable(400)) {
/*  7837 */       focusLog.finer("clearOnFailure = " + paramBoolean);
/*       */     }
/*  7839 */     Component localComponent = getNextFocusCandidate();
/*  7840 */     boolean bool = false;
/*  7841 */     if ((localComponent != null) && (!localComponent.isFocusOwner()) && (localComponent != this)) {
/*  7842 */       bool = localComponent.requestFocusInWindow(CausedFocusEvent.Cause.TRAVERSAL_FORWARD);
/*       */     }
/*  7844 */     if ((paramBoolean) && (!bool)) {
/*  7845 */       if (focusLog.isLoggable(400)) {
/*  7846 */         focusLog.finer("clear global focus owner");
/*       */       }
/*  7848 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
/*       */     }
/*  7850 */     if (focusLog.isLoggable(400)) {
/*  7851 */       focusLog.finer("returning result: " + bool);
/*       */     }
/*  7853 */     return bool;
/*       */   }
/*       */ 
/*       */   final Component getNextFocusCandidate() {
/*  7857 */     Container localContainer = getTraversalRoot();
/*  7858 */     Object localObject1 = this;
/*  7859 */     while ((localContainer != null) && ((!localContainer.isShowing()) || (!localContainer.canBeFocusOwner())))
/*       */     {
/*  7862 */       localObject1 = localContainer;
/*  7863 */       localContainer = ((Component)localObject1).getFocusCycleRootAncestor();
/*       */     }
/*  7865 */     if (focusLog.isLoggable(400)) {
/*  7866 */       focusLog.finer("comp = " + localObject1 + ", root = " + localContainer);
/*       */     }
/*  7868 */     Object localObject2 = null;
/*  7869 */     if (localContainer != null) {
/*  7870 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/*  7871 */       Object localObject3 = localFocusTraversalPolicy.getComponentAfter(localContainer, (Component)localObject1);
/*  7872 */       if (focusLog.isLoggable(400)) {
/*  7873 */         focusLog.finer("component after is " + localObject3);
/*       */       }
/*  7875 */       if (localObject3 == null) {
/*  7876 */         localObject3 = localFocusTraversalPolicy.getDefaultComponent(localContainer);
/*  7877 */         if (focusLog.isLoggable(400)) {
/*  7878 */           focusLog.finer("default component is " + localObject3);
/*       */         }
/*       */       }
/*  7881 */       if (localObject3 == null) {
/*  7882 */         Applet localApplet = EmbeddedFrame.getAppletIfAncestorOf(this);
/*  7883 */         if (localApplet != null) {
/*  7884 */           localObject3 = localApplet;
/*       */         }
/*       */       }
/*  7887 */       localObject2 = localObject3;
/*       */     }
/*  7889 */     if (focusLog.isLoggable(400)) {
/*  7890 */       focusLog.finer("Focus transfer candidate: " + localObject2);
/*       */     }
/*  7892 */     return localObject2;
/*       */   }
/*       */ 
/*       */   public void transferFocusBackward()
/*       */   {
/*  7902 */     transferFocusBackward(false);
/*       */   }
/*       */ 
/*       */   boolean transferFocusBackward(boolean paramBoolean) {
/*  7906 */     Container localContainer = getTraversalRoot();
/*  7907 */     Object localObject = this;
/*  7908 */     while ((localContainer != null) && ((!localContainer.isShowing()) || (!localContainer.canBeFocusOwner())))
/*       */     {
/*  7911 */       localObject = localContainer;
/*  7912 */       localContainer = ((Component)localObject).getFocusCycleRootAncestor();
/*       */     }
/*  7914 */     boolean bool = false;
/*  7915 */     if (localContainer != null) {
/*  7916 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/*  7917 */       Component localComponent = localFocusTraversalPolicy.getComponentBefore(localContainer, (Component)localObject);
/*  7918 */       if (localComponent == null) {
/*  7919 */         localComponent = localFocusTraversalPolicy.getDefaultComponent(localContainer);
/*       */       }
/*  7921 */       if (localComponent != null) {
/*  7922 */         bool = localComponent.requestFocusInWindow(CausedFocusEvent.Cause.TRAVERSAL_BACKWARD);
/*       */       }
/*       */     }
/*  7925 */     if ((paramBoolean) && (!bool)) {
/*  7926 */       if (focusLog.isLoggable(400)) {
/*  7927 */         focusLog.finer("clear global focus owner");
/*       */       }
/*  7929 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
/*       */     }
/*  7931 */     if (focusLog.isLoggable(400)) {
/*  7932 */       focusLog.finer("returning result: " + bool);
/*       */     }
/*  7934 */     return bool;
/*       */   }
/*       */ 
/*       */   public void transferFocusUpCycle()
/*       */   {
/*  7952 */     Container localContainer = getFocusCycleRootAncestor();
/*  7953 */     while ((localContainer != null) && ((!localContainer.isShowing()) || (!localContainer.isFocusable()) || (!localContainer.isEnabled())))
/*       */     {
/*  7956 */       localContainer = localContainer.getFocusCycleRootAncestor();
/*       */     }
/*       */     Object localObject;
/*  7959 */     if (localContainer != null) {
/*  7960 */       localObject = localContainer.getFocusCycleRootAncestor();
/*       */ 
/*  7962 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRoot(localObject != null ? localObject : localContainer);
/*       */ 
/*  7967 */       localContainer.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_UP);
/*       */     } else {
/*  7969 */       localObject = getContainingWindow();
/*       */ 
/*  7971 */       if (localObject != null) {
/*  7972 */         Component localComponent = ((Window)localObject).getFocusTraversalPolicy().getDefaultComponent((Container)localObject);
/*       */ 
/*  7974 */         if (localComponent != null) {
/*  7975 */           KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRoot((Container)localObject);
/*       */ 
/*  7977 */           localComponent.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_UP);
/*       */         }
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public boolean hasFocus()
/*       */   {
/*  7993 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this;
/*       */   }
/*       */ 
/*       */   public boolean isFocusOwner()
/*       */   {
/*  8006 */     return hasFocus();
/*       */   }
/*       */ 
/*       */   void setAutoFocusTransferOnDisposal(boolean paramBoolean)
/*       */   {
/*  8016 */     this.autoFocusTransferOnDisposal = paramBoolean;
/*       */   }
/*       */ 
/*       */   boolean isAutoFocusTransferOnDisposal() {
/*  8020 */     return this.autoFocusTransferOnDisposal;
/*       */   }
/*       */ 
/*       */   public void add(PopupMenu paramPopupMenu)
/*       */   {
/*  8031 */     synchronized (getTreeLock()) {
/*  8032 */       if (paramPopupMenu.parent != null) {
/*  8033 */         paramPopupMenu.parent.remove(paramPopupMenu);
/*       */       }
/*  8035 */       if (this.popups == null) {
/*  8036 */         this.popups = new Vector();
/*       */       }
/*  8038 */       this.popups.addElement(paramPopupMenu);
/*  8039 */       paramPopupMenu.parent = this;
/*       */ 
/*  8041 */       if ((this.peer != null) && 
/*  8042 */         (paramPopupMenu.peer == null))
/*  8043 */         paramPopupMenu.addNotify();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void remove(MenuComponent paramMenuComponent)
/*       */   {
/*  8056 */     synchronized (getTreeLock()) {
/*  8057 */       if (this.popups == null) {
/*  8058 */         return;
/*       */       }
/*  8060 */       int i = this.popups.indexOf(paramMenuComponent);
/*  8061 */       if (i >= 0) {
/*  8062 */         PopupMenu localPopupMenu = (PopupMenu)paramMenuComponent;
/*  8063 */         if (localPopupMenu.peer != null) {
/*  8064 */           localPopupMenu.removeNotify();
/*       */         }
/*  8066 */         localPopupMenu.parent = null;
/*  8067 */         this.popups.removeElementAt(i);
/*  8068 */         if (this.popups.size() == 0)
/*  8069 */           this.popups = null;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   protected String paramString()
/*       */   {
/*  8086 */     String str1 = getName();
/*  8087 */     String str2 = (str1 != null ? str1 : "") + "," + this.x + "," + this.y + "," + this.width + "x" + this.height;
/*  8088 */     if (!isValid()) {
/*  8089 */       str2 = str2 + ",invalid";
/*       */     }
/*  8091 */     if (!this.visible) {
/*  8092 */       str2 = str2 + ",hidden";
/*       */     }
/*  8094 */     if (!this.enabled) {
/*  8095 */       str2 = str2 + ",disabled";
/*       */     }
/*  8097 */     return str2;
/*       */   }
/*       */ 
/*       */   public String toString()
/*       */   {
/*  8106 */     return getClass().getName() + "[" + paramString() + "]";
/*       */   }
/*       */ 
/*       */   public void list()
/*       */   {
/*  8116 */     list(System.out, 0);
/*       */   }
/*       */ 
/*       */   public void list(PrintStream paramPrintStream)
/*       */   {
/*  8127 */     list(paramPrintStream, 0);
/*       */   }
/*       */ 
/*       */   public void list(PrintStream paramPrintStream, int paramInt)
/*       */   {
/*  8140 */     for (int i = 0; i < paramInt; i++) {
/*  8141 */       paramPrintStream.print(" ");
/*       */     }
/*  8143 */     paramPrintStream.println(this);
/*       */   }
/*       */ 
/*       */   public void list(PrintWriter paramPrintWriter)
/*       */   {
/*  8153 */     list(paramPrintWriter, 0);
/*       */   }
/*       */ 
/*       */   public void list(PrintWriter paramPrintWriter, int paramInt)
/*       */   {
/*  8166 */     for (int i = 0; i < paramInt; i++) {
/*  8167 */       paramPrintWriter.print(" ");
/*       */     }
/*  8169 */     paramPrintWriter.println(this);
/*       */   }
/*       */ 
/*       */   Container getNativeContainer()
/*       */   {
/*  8177 */     Container localContainer = this.parent;
/*  8178 */     while ((localContainer != null) && ((localContainer.peer instanceof LightweightPeer))) {
/*  8179 */       localContainer = localContainer.getParent_NoClientCode();
/*       */     }
/*  8181 */     return localContainer;
/*       */   }
/*       */ 
/*       */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*       */   {
/*  8220 */     synchronized (getObjectLock()) {
/*  8221 */       if (paramPropertyChangeListener == null) {
/*  8222 */         return;
/*       */       }
/*  8224 */       if (this.changeSupport == null) {
/*  8225 */         this.changeSupport = new PropertyChangeSupport(this);
/*       */       }
/*  8227 */       this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*       */   {
/*  8246 */     synchronized (getObjectLock()) {
/*  8247 */       if ((paramPropertyChangeListener == null) || (this.changeSupport == null)) {
/*  8248 */         return;
/*       */       }
/*  8250 */       this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*       */     }
/*       */   }
/*       */ 
/*       */   public PropertyChangeListener[] getPropertyChangeListeners()
/*       */   {
/*  8269 */     synchronized (getObjectLock()) {
/*  8270 */       if (this.changeSupport == null) {
/*  8271 */         return new PropertyChangeListener[0];
/*       */       }
/*  8273 */       return this.changeSupport.getPropertyChangeListeners();
/*       */     }
/*       */   }
/*       */ 
/*       */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*       */   {
/*  8311 */     synchronized (getObjectLock()) {
/*  8312 */       if (paramPropertyChangeListener == null) {
/*  8313 */         return;
/*       */       }
/*  8315 */       if (this.changeSupport == null) {
/*  8316 */         this.changeSupport = new PropertyChangeSupport(this);
/*       */       }
/*  8318 */       this.changeSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*       */     }
/*       */   }
/*       */ 
/*       */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*       */   {
/*  8341 */     synchronized (getObjectLock()) {
/*  8342 */       if ((paramPropertyChangeListener == null) || (this.changeSupport == null)) {
/*  8343 */         return;
/*       */       }
/*  8345 */       this.changeSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*       */     }
/*       */   }
/*       */ 
/*       */   public PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*       */   {
/*  8365 */     synchronized (getObjectLock()) {
/*  8366 */       if (this.changeSupport == null) {
/*  8367 */         return new PropertyChangeListener[0];
/*       */       }
/*  8369 */       return this.changeSupport.getPropertyChangeListeners(paramString);
/*       */     }
/*       */   }
/*       */ 
/*       */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*       */   {
/*       */     PropertyChangeSupport localPropertyChangeSupport;
/*  8386 */     synchronized (getObjectLock()) {
/*  8387 */       localPropertyChangeSupport = this.changeSupport;
/*       */     }
/*  8389 */     if ((localPropertyChangeSupport == null) || ((paramObject1 != null) && (paramObject2 != null) && (paramObject1.equals(paramObject2))))
/*       */     {
/*  8391 */       return;
/*       */     }
/*  8393 */     localPropertyChangeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*       */   }
/*       */ 
/*       */   protected void firePropertyChange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*       */   {
/*  8409 */     PropertyChangeSupport localPropertyChangeSupport = this.changeSupport;
/*  8410 */     if ((localPropertyChangeSupport == null) || (paramBoolean1 == paramBoolean2)) {
/*  8411 */       return;
/*       */     }
/*  8413 */     localPropertyChangeSupport.firePropertyChange(paramString, paramBoolean1, paramBoolean2);
/*       */   }
/*       */ 
/*       */   protected void firePropertyChange(String paramString, int paramInt1, int paramInt2)
/*       */   {
/*  8429 */     PropertyChangeSupport localPropertyChangeSupport = this.changeSupport;
/*  8430 */     if ((localPropertyChangeSupport == null) || (paramInt1 == paramInt2)) {
/*  8431 */       return;
/*       */     }
/*  8433 */     localPropertyChangeSupport.firePropertyChange(paramString, paramInt1, paramInt2);
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, byte paramByte1, byte paramByte2)
/*       */   {
/*  8448 */     if ((this.changeSupport == null) || (paramByte1 == paramByte2)) {
/*  8449 */       return;
/*       */     }
/*  8451 */     firePropertyChange(paramString, Byte.valueOf(paramByte1), Byte.valueOf(paramByte2));
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, char paramChar1, char paramChar2)
/*       */   {
/*  8466 */     if ((this.changeSupport == null) || (paramChar1 == paramChar2)) {
/*  8467 */       return;
/*       */     }
/*  8469 */     firePropertyChange(paramString, new Character(paramChar1), new Character(paramChar2));
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, short paramShort1, short paramShort2)
/*       */   {
/*  8484 */     if ((this.changeSupport == null) || (paramShort1 == paramShort2)) {
/*  8485 */       return;
/*       */     }
/*  8487 */     firePropertyChange(paramString, Short.valueOf(paramShort1), Short.valueOf(paramShort2));
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, long paramLong1, long paramLong2)
/*       */   {
/*  8503 */     if ((this.changeSupport == null) || (paramLong1 == paramLong2)) {
/*  8504 */       return;
/*       */     }
/*  8506 */     firePropertyChange(paramString, Long.valueOf(paramLong1), Long.valueOf(paramLong2));
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, float paramFloat1, float paramFloat2)
/*       */   {
/*  8521 */     if ((this.changeSupport == null) || (paramFloat1 == paramFloat2)) {
/*  8522 */       return;
/*       */     }
/*  8524 */     firePropertyChange(paramString, Float.valueOf(paramFloat1), Float.valueOf(paramFloat2));
/*       */   }
/*       */ 
/*       */   public void firePropertyChange(String paramString, double paramDouble1, double paramDouble2)
/*       */   {
/*  8539 */     if ((this.changeSupport == null) || (paramDouble1 == paramDouble2)) {
/*  8540 */       return;
/*       */     }
/*  8542 */     firePropertyChange(paramString, Double.valueOf(paramDouble1), Double.valueOf(paramDouble2));
/*       */   }
/*       */ 
/*       */   private void doSwingSerialization()
/*       */   {
/*  8560 */     Package localPackage = Package.getPackage("javax.swing");
/*       */ 
/*  8567 */     for (Class localClass1 = getClass(); localClass1 != null; 
/*  8568 */       localClass1 = localClass1.getSuperclass())
/*  8569 */       if ((localClass1.getPackage() == localPackage) && (localClass1.getClassLoader() == null))
/*       */       {
/*  8571 */         final Class localClass2 = localClass1;
/*       */ 
/*  8573 */         Method[] arrayOfMethod = (Method[])AccessController.doPrivileged(new PrivilegedAction()
/*       */         {
/*       */           public Object run() {
/*  8576 */             return localClass2.getDeclaredMethods();
/*       */           }
/*       */         });
/*  8579 */         for (int i = arrayOfMethod.length - 1; i >= 0; 
/*  8580 */           i--) {
/*  8581 */           final Method localMethod = arrayOfMethod[i];
/*  8582 */           if (localMethod.getName().equals("compWriteObjectNotify"))
/*       */           {
/*  8585 */             AccessController.doPrivileged(new PrivilegedAction() {
/*       */               public Object run() {
/*  8587 */                 localMethod.setAccessible(true);
/*  8588 */                 return null;
/*       */               }
/*       */             });
/*       */             try
/*       */             {
/*  8593 */               localMethod.invoke(this, (Object[])null);
/*       */             } catch (IllegalAccessException localIllegalAccessException) {
/*       */             }
/*       */             catch (InvocationTargetException localInvocationTargetException) {
/*       */             }
/*  8598 */             return;
/*       */           }
/*       */         }
/*       */       }
/*       */   }
/*       */ 
/*       */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*       */     throws IOException
/*       */   {
/*  8652 */     doSwingSerialization();
/*       */ 
/*  8654 */     paramObjectOutputStream.defaultWriteObject();
/*       */ 
/*  8656 */     AWTEventMulticaster.save(paramObjectOutputStream, "componentL", this.componentListener);
/*  8657 */     AWTEventMulticaster.save(paramObjectOutputStream, "focusL", this.focusListener);
/*  8658 */     AWTEventMulticaster.save(paramObjectOutputStream, "keyL", this.keyListener);
/*  8659 */     AWTEventMulticaster.save(paramObjectOutputStream, "mouseL", this.mouseListener);
/*  8660 */     AWTEventMulticaster.save(paramObjectOutputStream, "mouseMotionL", this.mouseMotionListener);
/*  8661 */     AWTEventMulticaster.save(paramObjectOutputStream, "inputMethodL", this.inputMethodListener);
/*       */ 
/*  8663 */     paramObjectOutputStream.writeObject(null);
/*  8664 */     paramObjectOutputStream.writeObject(this.componentOrientation);
/*       */ 
/*  8666 */     AWTEventMulticaster.save(paramObjectOutputStream, "hierarchyL", this.hierarchyListener);
/*  8667 */     AWTEventMulticaster.save(paramObjectOutputStream, "hierarchyBoundsL", this.hierarchyBoundsListener);
/*       */ 
/*  8669 */     paramObjectOutputStream.writeObject(null);
/*       */ 
/*  8671 */     AWTEventMulticaster.save(paramObjectOutputStream, "mouseWheelL", this.mouseWheelListener);
/*  8672 */     paramObjectOutputStream.writeObject(null);
/*       */   }
/*       */ 
/*       */   private void readObject(ObjectInputStream paramObjectInputStream)
/*       */     throws ClassNotFoundException, IOException
/*       */   {
/*  8688 */     this.objectLock = new Object();
/*       */ 
/*  8690 */     this.acc = AccessController.getContext();
/*       */ 
/*  8692 */     paramObjectInputStream.defaultReadObject();
/*       */ 
/*  8694 */     this.appContext = AppContext.getAppContext();
/*  8695 */     this.coalescingEnabled = checkCoalescing();
/*  8696 */     if (this.componentSerializedDataVersion < 4)
/*       */     {
/*  8701 */       this.focusable = true;
/*  8702 */       this.isFocusTraversableOverridden = 0;
/*  8703 */       initializeFocusTraversalKeys();
/*  8704 */       this.focusTraversalKeysEnabled = true;
/*       */     }
/*       */     Object localObject1;
/*  8708 */     while (null != (localObject1 = paramObjectInputStream.readObject())) {
/*  8709 */       localObject2 = ((String)localObject1).intern();
/*       */ 
/*  8711 */       if ("componentL" == localObject2) {
/*  8712 */         addComponentListener((ComponentListener)paramObjectInputStream.readObject());
/*       */       }
/*  8714 */       else if ("focusL" == localObject2) {
/*  8715 */         addFocusListener((FocusListener)paramObjectInputStream.readObject());
/*       */       }
/*  8717 */       else if ("keyL" == localObject2) {
/*  8718 */         addKeyListener((KeyListener)paramObjectInputStream.readObject());
/*       */       }
/*  8720 */       else if ("mouseL" == localObject2) {
/*  8721 */         addMouseListener((MouseListener)paramObjectInputStream.readObject());
/*       */       }
/*  8723 */       else if ("mouseMotionL" == localObject2) {
/*  8724 */         addMouseMotionListener((MouseMotionListener)paramObjectInputStream.readObject());
/*       */       }
/*  8726 */       else if ("inputMethodL" == localObject2) {
/*  8727 */         addInputMethodListener((InputMethodListener)paramObjectInputStream.readObject());
/*       */       }
/*       */       else {
/*  8730 */         paramObjectInputStream.readObject();
/*       */       }
/*       */ 
/*       */     }
/*       */ 
/*  8735 */     Object localObject2 = null;
/*       */     try
/*       */     {
/*  8738 */       localObject2 = paramObjectInputStream.readObject();
/*       */     }
/*       */     catch (OptionalDataException localOptionalDataException1)
/*       */     {
/*  8747 */       if (!localOptionalDataException1.eof) {
/*  8748 */         throw localOptionalDataException1;
/*       */       }
/*       */     }
/*       */ 
/*  8752 */     if (localObject2 != null)
/*  8753 */       this.componentOrientation = ((ComponentOrientation)localObject2);
/*       */     else {
/*  8755 */       this.componentOrientation = ComponentOrientation.UNKNOWN;
/*       */     }
/*       */     try
/*       */     {
/*  8759 */       while (null != (localObject1 = paramObjectInputStream.readObject())) {
/*  8760 */         String str1 = ((String)localObject1).intern();
/*       */ 
/*  8762 */         if ("hierarchyL" == str1) {
/*  8763 */           addHierarchyListener((HierarchyListener)paramObjectInputStream.readObject());
/*       */         }
/*  8765 */         else if ("hierarchyBoundsL" == str1) {
/*  8766 */           addHierarchyBoundsListener((HierarchyBoundsListener)paramObjectInputStream.readObject());
/*       */         }
/*       */         else
/*       */         {
/*  8771 */           paramObjectInputStream.readObject();
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*       */     }
/*       */     catch (OptionalDataException localOptionalDataException2)
/*       */     {
/*  8782 */       if (!localOptionalDataException2.eof) {
/*  8783 */         throw localOptionalDataException2;
/*       */       }
/*       */     }
/*       */     try
/*       */     {
/*  8788 */       while (null != (localObject1 = paramObjectInputStream.readObject())) {
/*  8789 */         String str2 = ((String)localObject1).intern();
/*       */ 
/*  8791 */         if ("mouseWheelL" == str2) {
/*  8792 */           addMouseWheelListener((MouseWheelListener)paramObjectInputStream.readObject());
/*       */         }
/*       */         else
/*       */         {
/*  8796 */           paramObjectInputStream.readObject();
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*       */     }
/*       */     catch (OptionalDataException localOptionalDataException3)
/*       */     {
/*  8807 */       if (!localOptionalDataException3.eof) {
/*  8808 */         throw localOptionalDataException3;
/*       */       }
/*       */     }
/*       */ 
/*  8812 */     if (this.popups != null) {
/*  8813 */       int i = this.popups.size();
/*  8814 */       for (int j = 0; j < i; j++) {
/*  8815 */         PopupMenu localPopupMenu = (PopupMenu)this.popups.elementAt(j);
/*  8816 */         localPopupMenu.parent = this;
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public void setComponentOrientation(ComponentOrientation paramComponentOrientation)
/*       */   {
/*  8851 */     ComponentOrientation localComponentOrientation = this.componentOrientation;
/*  8852 */     this.componentOrientation = paramComponentOrientation;
/*       */ 
/*  8856 */     firePropertyChange("componentOrientation", localComponentOrientation, paramComponentOrientation);
/*       */ 
/*  8859 */     invalidateIfValid();
/*       */   }
/*       */ 
/*       */   public ComponentOrientation getComponentOrientation()
/*       */   {
/*  8874 */     return this.componentOrientation;
/*       */   }
/*       */ 
/*       */   public void applyComponentOrientation(ComponentOrientation paramComponentOrientation)
/*       */   {
/*  8894 */     if (paramComponentOrientation == null) {
/*  8895 */       throw new NullPointerException();
/*       */     }
/*  8897 */     setComponentOrientation(paramComponentOrientation);
/*       */   }
/*       */ 
/*       */   final boolean canBeFocusOwner()
/*       */   {
/*  8902 */     if ((isEnabled()) && (isDisplayable()) && (isVisible()) && (isFocusable())) {
/*  8903 */       return true;
/*       */     }
/*  8905 */     return false;
/*       */   }
/*       */ 
/*       */   final boolean canBeFocusOwnerRecursively()
/*       */   {
/*  8919 */     if (!canBeFocusOwner()) {
/*  8920 */       return false;
/*       */     }
/*       */ 
/*  8924 */     synchronized (getTreeLock()) {
/*  8925 */       if (this.parent != null) {
/*  8926 */         return this.parent.canContainFocusOwner(this);
/*       */       }
/*       */     }
/*  8929 */     return true;
/*       */   }
/*       */ 
/*       */   final void relocateComponent()
/*       */   {
/*  8936 */     synchronized (getTreeLock()) {
/*  8937 */       if (this.peer == null) {
/*  8938 */         return;
/*       */       }
/*  8940 */       int i = this.x;
/*  8941 */       int j = this.y;
/*  8942 */       for (Container localContainer = getContainer(); 
/*  8943 */         (localContainer != null) && (localContainer.isLightweight()); 
/*  8944 */         localContainer = localContainer.getContainer())
/*       */       {
/*  8946 */         i += localContainer.x;
/*  8947 */         j += localContainer.y;
/*       */       }
/*  8949 */       this.peer.setBounds(i, j, this.width, this.height, 1);
/*       */     }
/*       */   }
/*       */ 
/*       */   Window getContainingWindow()
/*       */   {
/*  8960 */     return SunToolkit.getContainingWindow(this);
/*       */   }
/*       */ 
/*       */   private static native void initIDs();
/*       */ 
/*       */   public AccessibleContext getAccessibleContext()
/*       */   {
/*  8992 */     return this.accessibleContext;
/*       */   }
/*       */ 
/*       */   int getAccessibleIndexInParent()
/*       */   {
/*  9585 */     synchronized (getTreeLock()) {
/*  9586 */       int i = -1;
/*  9587 */       Container localContainer = getParent();
/*  9588 */       if ((localContainer != null) && ((localContainer instanceof Accessible))) {
/*  9589 */         Component[] arrayOfComponent = localContainer.getComponents();
/*  9590 */         for (int j = 0; j < arrayOfComponent.length; j++) {
/*  9591 */           if ((arrayOfComponent[j] instanceof Accessible)) {
/*  9592 */             i++;
/*       */           }
/*  9594 */           if (equals(arrayOfComponent[j])) {
/*  9595 */             return i;
/*       */           }
/*       */         }
/*       */       }
/*  9599 */       return -1;
/*       */     }
/*       */   }
/*       */ 
/*       */   AccessibleStateSet getAccessibleStateSet()
/*       */   {
/*  9611 */     synchronized (getTreeLock()) {
/*  9612 */       AccessibleStateSet localAccessibleStateSet = new AccessibleStateSet();
/*  9613 */       if (isEnabled()) {
/*  9614 */         localAccessibleStateSet.add(AccessibleState.ENABLED);
/*       */       }
/*  9616 */       if (isFocusTraversable()) {
/*  9617 */         localAccessibleStateSet.add(AccessibleState.FOCUSABLE);
/*       */       }
/*  9619 */       if (isVisible()) {
/*  9620 */         localAccessibleStateSet.add(AccessibleState.VISIBLE);
/*       */       }
/*  9622 */       if (isShowing()) {
/*  9623 */         localAccessibleStateSet.add(AccessibleState.SHOWING);
/*       */       }
/*  9625 */       if (isFocusOwner()) {
/*  9626 */         localAccessibleStateSet.add(AccessibleState.FOCUSED);
/*       */       }
/*  9628 */       if ((this instanceof Accessible)) {
/*  9629 */         AccessibleContext localAccessibleContext1 = ((Accessible)this).getAccessibleContext();
/*  9630 */         if (localAccessibleContext1 != null) {
/*  9631 */           Accessible localAccessible = localAccessibleContext1.getAccessibleParent();
/*  9632 */           if (localAccessible != null) {
/*  9633 */             AccessibleContext localAccessibleContext2 = localAccessible.getAccessibleContext();
/*  9634 */             if (localAccessibleContext2 != null) {
/*  9635 */               AccessibleSelection localAccessibleSelection = localAccessibleContext2.getAccessibleSelection();
/*  9636 */               if (localAccessibleSelection != null) {
/*  9637 */                 localAccessibleStateSet.add(AccessibleState.SELECTABLE);
/*  9638 */                 int i = localAccessibleContext1.getAccessibleIndexInParent();
/*  9639 */                 if ((i >= 0) && 
/*  9640 */                   (localAccessibleSelection.isAccessibleChildSelected(i))) {
/*  9641 */                   localAccessibleStateSet.add(AccessibleState.SELECTED);
/*       */                 }
/*       */               }
/*       */             }
/*       */           }
/*       */         }
/*       */       }
/*       */ 
/*  9649 */       if ((isInstanceOf(this, "javax.swing.JComponent")) && 
/*  9650 */         (((JComponent)this).isOpaque())) {
/*  9651 */         localAccessibleStateSet.add(AccessibleState.OPAQUE);
/*       */       }
/*       */ 
/*  9654 */       return localAccessibleStateSet;
/*       */     }
/*       */   }
/*       */ 
/*       */   static boolean isInstanceOf(Object paramObject, String paramString)
/*       */   {
/*  9666 */     if (paramObject == null) return false;
/*  9667 */     if (paramString == null) return false;
/*       */ 
/*  9669 */     Class localClass = paramObject.getClass();
/*  9670 */     while (localClass != null) {
/*  9671 */       if (localClass.getName().equals(paramString)) {
/*  9672 */         return true;
/*       */       }
/*  9674 */       localClass = localClass.getSuperclass();
/*       */     }
/*  9676 */     return false;
/*       */   }
/*       */ 
/*       */   final boolean areBoundsValid()
/*       */   {
/*  9691 */     Container localContainer = getContainer();
/*  9692 */     return (localContainer == null) || (localContainer.isValid()) || (localContainer.getLayout() == null);
/*       */   }
/*       */ 
/*       */   void applyCompoundShape(Region paramRegion)
/*       */   {
/*  9700 */     checkTreeLock();
/*       */ 
/*  9702 */     if (!areBoundsValid()) {
/*  9703 */       if (mixingLog.isLoggable(500)) {
/*  9704 */         mixingLog.fine("this = " + this + "; areBoundsValid = " + areBoundsValid());
/*       */       }
/*  9706 */       return;
/*       */     }
/*       */ 
/*  9709 */     if (!isLightweight()) {
/*  9710 */       ComponentPeer localComponentPeer = getPeer();
/*  9711 */       if (localComponentPeer != null)
/*       */       {
/*  9717 */         if (paramRegion.isEmpty()) {
/*  9718 */           paramRegion = Region.EMPTY_REGION;
/*       */         }
/*       */ 
/*  9727 */         if (paramRegion.equals(getNormalShape())) {
/*  9728 */           if (this.compoundShape == null) {
/*  9729 */             return;
/*       */           }
/*  9731 */           this.compoundShape = null;
/*  9732 */           localComponentPeer.applyShape(null);
/*       */         } else {
/*  9734 */           if (paramRegion.equals(getAppliedShape())) {
/*  9735 */             return;
/*       */           }
/*  9737 */           this.compoundShape = paramRegion;
/*  9738 */           Point localPoint = getLocationOnWindow();
/*  9739 */           if (mixingLog.isLoggable(400)) {
/*  9740 */             mixingLog.fine("this = " + this + "; compAbsolute=" + localPoint + "; shape=" + paramRegion);
/*       */           }
/*       */ 
/*  9743 */           localComponentPeer.applyShape(paramRegion.getTranslatedRegion(-localPoint.x, -localPoint.y));
/*       */         }
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   private Region getAppliedShape()
/*       */   {
/*  9755 */     checkTreeLock();
/*       */ 
/*  9757 */     return (this.compoundShape == null) || (isLightweight()) ? getNormalShape() : this.compoundShape;
/*       */   }
/*       */ 
/*       */   Point getLocationOnWindow() {
/*  9761 */     checkTreeLock();
/*  9762 */     Point localPoint = getLocation();
/*       */ 
/*  9764 */     for (Container localContainer = getContainer(); 
/*  9765 */       (localContainer != null) && (!(localContainer instanceof Window)); 
/*  9766 */       localContainer = localContainer.getContainer())
/*       */     {
/*  9768 */       localPoint.x += localContainer.getX();
/*  9769 */       localPoint.y += localContainer.getY();
/*       */     }
/*       */ 
/*  9772 */     return localPoint;
/*       */   }
/*       */ 
/*       */   final Region getNormalShape()
/*       */   {
/*  9779 */     checkTreeLock();
/*       */ 
/*  9781 */     Point localPoint = getLocationOnWindow();
/*  9782 */     return Region.getInstanceXYWH(localPoint.x, localPoint.y, getWidth(), getHeight());
/*       */   }
/*       */ 
/*       */   Region getOpaqueShape()
/*       */   {
/*  9804 */     checkTreeLock();
/*  9805 */     if (this.mixingCutoutRegion != null) {
/*  9806 */       return this.mixingCutoutRegion;
/*       */     }
/*  9808 */     return getNormalShape();
/*       */   }
/*       */ 
/*       */   final int getSiblingIndexAbove()
/*       */   {
/*  9813 */     checkTreeLock();
/*  9814 */     Container localContainer = getContainer();
/*  9815 */     if (localContainer == null) {
/*  9816 */       return -1;
/*       */     }
/*       */ 
/*  9819 */     int i = localContainer.getComponentZOrder(this) - 1;
/*       */ 
/*  9821 */     return i < 0 ? -1 : i;
/*       */   }
/*       */ 
/*       */   final ComponentPeer getHWPeerAboveMe() {
/*  9825 */     checkTreeLock();
/*       */ 
/*  9827 */     Container localContainer = getContainer();
/*  9828 */     int i = getSiblingIndexAbove();
/*       */ 
/*  9830 */     while (localContainer != null) {
/*  9831 */       for (int j = i; j > -1; j--) {
/*  9832 */         Component localComponent = localContainer.getComponent(j);
/*  9833 */         if ((localComponent != null) && (localComponent.isDisplayable()) && (!localComponent.isLightweight())) {
/*  9834 */           return localComponent.getPeer();
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*  9841 */       if (!localContainer.isLightweight())
/*       */       {
/*       */         break;
/*       */       }
/*  9845 */       i = localContainer.getSiblingIndexAbove();
/*  9846 */       localContainer = localContainer.getContainer();
/*       */     }
/*       */ 
/*  9849 */     return null;
/*       */   }
/*       */ 
/*       */   final int getSiblingIndexBelow() {
/*  9853 */     checkTreeLock();
/*  9854 */     Container localContainer = getContainer();
/*  9855 */     if (localContainer == null) {
/*  9856 */       return -1;
/*       */     }
/*       */ 
/*  9859 */     int i = localContainer.getComponentZOrder(this) + 1;
/*       */ 
/*  9861 */     return i >= localContainer.getComponentCount() ? -1 : i;
/*       */   }
/*       */ 
/*       */   final boolean isNonOpaqueForMixing() {
/*  9865 */     return (this.mixingCutoutRegion != null) && (this.mixingCutoutRegion.isEmpty());
/*       */   }
/*       */ 
/*       */   private Region calculateCurrentShape()
/*       */   {
/*  9870 */     checkTreeLock();
/*  9871 */     Region localRegion = getNormalShape();
/*       */ 
/*  9873 */     if (mixingLog.isLoggable(500)) {
/*  9874 */       mixingLog.fine("this = " + this + "; normalShape=" + localRegion);
/*       */     }
/*       */ 
/*  9877 */     if (getContainer() != null) {
/*  9878 */       Object localObject = this;
/*  9879 */       Container localContainer = ((Component)localObject).getContainer();
/*       */ 
/*  9881 */       while (localContainer != null) {
/*  9882 */         for (int i = ((Component)localObject).getSiblingIndexAbove(); i != -1; i--)
/*       */         {
/*  9890 */           Component localComponent = localContainer.getComponent(i);
/*  9891 */           if ((localComponent.isLightweight()) && (localComponent.isShowing())) {
/*  9892 */             localRegion = localRegion.getDifference(localComponent.getOpaqueShape());
/*       */           }
/*       */         }
/*       */ 
/*  9896 */         if (!localContainer.isLightweight()) break;
/*  9897 */         localRegion = localRegion.getIntersection(localContainer.getNormalShape());
/*       */ 
/*  9902 */         localObject = localContainer;
/*  9903 */         localContainer = localContainer.getContainer();
/*       */       }
/*       */     }
/*       */ 
/*  9907 */     if (mixingLog.isLoggable(500)) {
/*  9908 */       mixingLog.fine("currentShape=" + localRegion);
/*       */     }
/*       */ 
/*  9911 */     return localRegion;
/*       */   }
/*       */ 
/*       */   void applyCurrentShape() {
/*  9915 */     checkTreeLock();
/*  9916 */     if (!areBoundsValid()) {
/*  9917 */       if (mixingLog.isLoggable(500)) {
/*  9918 */         mixingLog.fine("this = " + this + "; areBoundsValid = " + areBoundsValid());
/*       */       }
/*  9920 */       return;
/*       */     }
/*  9922 */     if (mixingLog.isLoggable(500)) {
/*  9923 */       mixingLog.fine("this = " + this);
/*       */     }
/*  9925 */     applyCompoundShape(calculateCurrentShape());
/*       */   }
/*       */ 
/*       */   final void subtractAndApplyShape(Region paramRegion) {
/*  9929 */     checkTreeLock();
/*       */ 
/*  9931 */     if (mixingLog.isLoggable(500)) {
/*  9932 */       mixingLog.fine("this = " + this + "; s=" + paramRegion);
/*       */     }
/*       */ 
/*  9935 */     applyCompoundShape(getAppliedShape().getDifference(paramRegion));
/*       */   }
/*       */ 
/*       */   private final void applyCurrentShapeBelowMe() {
/*  9939 */     checkTreeLock();
/*  9940 */     Object localObject = getContainer();
/*  9941 */     if ((localObject != null) && (((Container)localObject).isShowing()))
/*       */     {
/*  9943 */       ((Container)localObject).recursiveApplyCurrentShape(getSiblingIndexBelow());
/*       */ 
/*  9946 */       Container localContainer = ((Container)localObject).getContainer();
/*  9947 */       while ((!((Container)localObject).isOpaque()) && (localContainer != null)) {
/*  9948 */         localContainer.recursiveApplyCurrentShape(((Container)localObject).getSiblingIndexBelow());
/*       */ 
/*  9950 */         localObject = localContainer;
/*  9951 */         localContainer = ((Container)localObject).getContainer();
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   final void subtractAndApplyShapeBelowMe() {
/*  9957 */     checkTreeLock();
/*  9958 */     Object localObject = getContainer();
/*  9959 */     if ((localObject != null) && (isShowing())) {
/*  9960 */       Region localRegion = getOpaqueShape();
/*       */ 
/*  9963 */       ((Container)localObject).recursiveSubtractAndApplyShape(localRegion, getSiblingIndexBelow());
/*       */ 
/*  9966 */       Container localContainer = ((Container)localObject).getContainer();
/*  9967 */       while ((!((Container)localObject).isOpaque()) && (localContainer != null)) {
/*  9968 */         localContainer.recursiveSubtractAndApplyShape(localRegion, ((Container)localObject).getSiblingIndexBelow());
/*       */ 
/*  9970 */         localObject = localContainer;
/*  9971 */         localContainer = ((Container)localObject).getContainer();
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   void mixOnShowing() {
/*  9977 */     synchronized (getTreeLock()) {
/*  9978 */       if (mixingLog.isLoggable(500)) {
/*  9979 */         mixingLog.fine("this = " + this);
/*       */       }
/*  9981 */       if (!isMixingNeeded()) {
/*  9982 */         return;
/*       */       }
/*  9984 */       if (isLightweight())
/*  9985 */         subtractAndApplyShapeBelowMe();
/*       */       else
/*  9987 */         applyCurrentShape();
/*       */     }
/*       */   }
/*       */ 
/*       */   void mixOnHiding(boolean paramBoolean)
/*       */   {
/*  9995 */     synchronized (getTreeLock()) {
/*  9996 */       if (mixingLog.isLoggable(500)) {
/*  9997 */         mixingLog.fine("this = " + this + "; isLightweight = " + paramBoolean);
/*       */       }
/*  9999 */       if (!isMixingNeeded()) {
/* 10000 */         return;
/*       */       }
/* 10002 */       if (paramBoolean)
/* 10003 */         applyCurrentShapeBelowMe();
/*       */     }
/*       */   }
/*       */ 
/*       */   void mixOnReshaping()
/*       */   {
/* 10009 */     synchronized (getTreeLock()) {
/* 10010 */       if (mixingLog.isLoggable(500)) {
/* 10011 */         mixingLog.fine("this = " + this);
/*       */       }
/* 10013 */       if (!isMixingNeeded()) {
/* 10014 */         return;
/*       */       }
/* 10016 */       if (isLightweight())
/* 10017 */         applyCurrentShapeBelowMe();
/*       */       else
/* 10019 */         applyCurrentShape();
/*       */     }
/*       */   }
/*       */ 
/*       */   void mixOnZOrderChanging(int paramInt1, int paramInt2)
/*       */   {
/* 10025 */     synchronized (getTreeLock()) {
/* 10026 */       int i = paramInt2 < paramInt1 ? 1 : 0;
/* 10027 */       Container localContainer = getContainer();
/*       */ 
/* 10029 */       if (mixingLog.isLoggable(500)) {
/* 10030 */         mixingLog.fine("this = " + this + "; oldZorder=" + paramInt1 + "; newZorder=" + paramInt2 + "; parent=" + localContainer);
/*       */       }
/*       */ 
/* 10033 */       if (!isMixingNeeded()) {
/* 10034 */         return;
/*       */       }
/* 10036 */       if (isLightweight()) {
/* 10037 */         if (i != 0) {
/* 10038 */           if ((localContainer != null) && (isShowing())) {
/* 10039 */             localContainer.recursiveSubtractAndApplyShape(getOpaqueShape(), getSiblingIndexBelow(), paramInt1);
/*       */           }
/*       */         }
/* 10042 */         else if (localContainer != null) {
/* 10043 */           localContainer.recursiveApplyCurrentShape(paramInt1, paramInt2);
/*       */         }
/*       */ 
/*       */       }
/* 10047 */       else if (i != 0) {
/* 10048 */         applyCurrentShape();
/*       */       }
/* 10050 */       else if (localContainer != null) {
/* 10051 */         Region localRegion = getAppliedShape();
/*       */ 
/* 10053 */         for (int j = paramInt1; j < paramInt2; j++) {
/* 10054 */           Component localComponent = localContainer.getComponent(j);
/* 10055 */           if ((localComponent.isLightweight()) && (localComponent.isShowing())) {
/* 10056 */             localRegion = localRegion.getDifference(localComponent.getOpaqueShape());
/*       */           }
/*       */         }
/* 10059 */         applyCompoundShape(localRegion);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   void mixOnValidating()
/*       */   {
/*       */   }
/*       */ 
/*       */   final boolean isMixingNeeded()
/*       */   {
/* 10072 */     if (SunToolkit.getSunAwtDisableMixing()) {
/* 10073 */       if (mixingLog.isLoggable(300)) {
/* 10074 */         mixingLog.finest("this = " + this + "; Mixing disabled via sun.awt.disableMixing");
/*       */       }
/* 10076 */       return false;
/*       */     }
/* 10078 */     if (!areBoundsValid()) {
/* 10079 */       if (mixingLog.isLoggable(500)) {
/* 10080 */         mixingLog.fine("this = " + this + "; areBoundsValid = " + areBoundsValid());
/*       */       }
/* 10082 */       return false;
/*       */     }
/* 10084 */     Window localWindow = getContainingWindow();
/* 10085 */     if (localWindow != null) {
/* 10086 */       if ((!localWindow.hasHeavyweightDescendants()) || (!localWindow.hasLightweightDescendants()) || (localWindow.isDisposing())) {
/* 10087 */         if (mixingLog.isLoggable(500)) {
/* 10088 */           mixingLog.fine("containing window = " + localWindow + "; has h/w descendants = " + localWindow.hasHeavyweightDescendants() + "; has l/w descendants = " + localWindow.hasLightweightDescendants() + "; disposing = " + localWindow.isDisposing());
/*       */         }
/*       */ 
/* 10093 */         return false;
/*       */       }
/*       */     } else {
/* 10096 */       if (mixingLog.isLoggable(500)) {
/* 10097 */         mixingLog.fine("this = " + this + "; containing window is null");
/*       */       }
/* 10099 */       return false;
/*       */     }
/* 10101 */     return true;
/*       */   }
/*       */ 
/*       */   void updateZOrder()
/*       */   {
/* 10109 */     this.peer.setZOrder(getHWPeerAboveMe());
/*       */   }
/*       */ 
/*       */   static
/*       */   {
/*   192 */     log = PlatformLogger.getLogger("java.awt.Component");
/*   193 */     eventLog = PlatformLogger.getLogger("java.awt.event.Component");
/*   194 */     focusLog = PlatformLogger.getLogger("java.awt.focus.Component");
/*   195 */     mixingLog = PlatformLogger.getLogger("java.awt.mixing.Component");
/*       */ 
/*   446 */     focusTraversalKeyPropertyNames = new String[] { "forwardFocusTraversalKeys", "backwardFocusTraversalKeys", "upCycleFocusTraversalKeys", "downCycleFocusTraversalKeys" };
/*       */ 
/*   472 */     LOCK = new AWTTreeLock();
/*       */ 
/*   595 */     Toolkit.loadLibraries();
/*       */ 
/*   597 */     if (!GraphicsEnvironment.isHeadless()) {
/*   598 */       initIDs();
/*       */     }
/*       */ 
/*   601 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("awt.image.incrementaldraw"));
/*       */ 
/*   603 */     isInc = (str == null) || (str.equals("true"));
/*       */ 
/*   605 */     str = (String)AccessController.doPrivileged(new GetPropertyAction("awt.image.redrawrate"));
/*       */ 
/*   607 */     incRate = str != null ? Integer.parseInt(str) : 100;
/*       */ 
/*   841 */     AWTAccessor.setComponentAccessor(new AWTAccessor.ComponentAccessor() {
/*       */       public void setBackgroundEraseDisabled(Component paramAnonymousComponent, boolean paramAnonymousBoolean) {
/*   843 */         paramAnonymousComponent.backgroundEraseDisabled = paramAnonymousBoolean;
/*       */       }
/*       */       public boolean getBackgroundEraseDisabled(Component paramAnonymousComponent) {
/*   846 */         return paramAnonymousComponent.backgroundEraseDisabled;
/*       */       }
/*       */       public Rectangle getBounds(Component paramAnonymousComponent) {
/*   849 */         return new Rectangle(paramAnonymousComponent.x, paramAnonymousComponent.y, paramAnonymousComponent.width, paramAnonymousComponent.height);
/*       */       }
/*       */       public void setMixingCutoutShape(Component paramAnonymousComponent, Shape paramAnonymousShape) {
/*   852 */         Region localRegion = paramAnonymousShape == null ? null : Region.getInstance(paramAnonymousShape, null);
/*       */ 
/*   855 */         synchronized (paramAnonymousComponent.getTreeLock()) {
/*   856 */           int i = 0;
/*   857 */           int j = 0;
/*       */ 
/*   859 */           if (!paramAnonymousComponent.isNonOpaqueForMixing()) {
/*   860 */             j = 1;
/*       */           }
/*       */ 
/*   863 */           paramAnonymousComponent.mixingCutoutRegion = localRegion;
/*       */ 
/*   865 */           if (!paramAnonymousComponent.isNonOpaqueForMixing()) {
/*   866 */             i = 1;
/*       */           }
/*       */ 
/*   869 */           if (paramAnonymousComponent.isMixingNeeded()) {
/*   870 */             if (j != 0) {
/*   871 */               paramAnonymousComponent.mixOnHiding(paramAnonymousComponent.isLightweight());
/*       */             }
/*   873 */             if (i != 0)
/*   874 */               paramAnonymousComponent.mixOnShowing();
/*       */           }
/*       */         }
/*       */       }
/*       */ 
/*       */       public void setGraphicsConfiguration(Component paramAnonymousComponent, GraphicsConfiguration paramAnonymousGraphicsConfiguration)
/*       */       {
/*   883 */         paramAnonymousComponent.setGraphicsConfiguration(paramAnonymousGraphicsConfiguration);
/*       */       }
/*       */       public boolean requestFocus(Component paramAnonymousComponent, CausedFocusEvent.Cause paramAnonymousCause) {
/*   886 */         return paramAnonymousComponent.requestFocus(paramAnonymousCause);
/*       */       }
/*       */       public boolean canBeFocusOwner(Component paramAnonymousComponent) {
/*   889 */         return paramAnonymousComponent.canBeFocusOwner();
/*       */       }
/*       */ 
/*       */       public boolean isVisible(Component paramAnonymousComponent) {
/*   893 */         return paramAnonymousComponent.isVisible_NoClientCode();
/*       */       }
/*       */ 
/*       */       public void setRequestFocusController(RequestFocusController paramAnonymousRequestFocusController)
/*       */       {
/*   898 */         Component.setRequestFocusController(paramAnonymousRequestFocusController);
/*       */       }
/*       */       public AppContext getAppContext(Component paramAnonymousComponent) {
/*   901 */         return paramAnonymousComponent.appContext;
/*       */       }
/*       */       public void setAppContext(Component paramAnonymousComponent, AppContext paramAnonymousAppContext) {
/*   904 */         paramAnonymousComponent.appContext = paramAnonymousAppContext;
/*       */       }
/*       */       public Container getParent(Component paramAnonymousComponent) {
/*   907 */         return paramAnonymousComponent.getParent_NoClientCode();
/*       */       }
/*       */       public void setParent(Component paramAnonymousComponent, Container paramAnonymousContainer) {
/*   910 */         paramAnonymousComponent.parent = paramAnonymousContainer;
/*       */       }
/*       */       public void setSize(Component paramAnonymousComponent, int paramAnonymousInt1, int paramAnonymousInt2) {
/*   913 */         paramAnonymousComponent.width = paramAnonymousInt1;
/*   914 */         paramAnonymousComponent.height = paramAnonymousInt2;
/*       */       }
/*       */       public Point getLocation(Component paramAnonymousComponent) {
/*   917 */         return paramAnonymousComponent.location_NoClientCode();
/*       */       }
/*       */       public void setLocation(Component paramAnonymousComponent, int paramAnonymousInt1, int paramAnonymousInt2) {
/*   920 */         paramAnonymousComponent.x = paramAnonymousInt1;
/*   921 */         paramAnonymousComponent.y = paramAnonymousInt2;
/*       */       }
/*       */       public boolean isEnabled(Component paramAnonymousComponent) {
/*   924 */         return paramAnonymousComponent.isEnabledImpl();
/*       */       }
/*       */       public boolean isDisplayable(Component paramAnonymousComponent) {
/*   927 */         return paramAnonymousComponent.peer != null;
/*       */       }
/*       */       public Cursor getCursor(Component paramAnonymousComponent) {
/*   930 */         return paramAnonymousComponent.getCursor_NoClientCode();
/*       */       }
/*       */       public ComponentPeer getPeer(Component paramAnonymousComponent) {
/*   933 */         return paramAnonymousComponent.peer;
/*       */       }
/*       */       public void setPeer(Component paramAnonymousComponent, ComponentPeer paramAnonymousComponentPeer) {
/*   936 */         paramAnonymousComponent.peer = paramAnonymousComponentPeer;
/*       */       }
/*       */       public boolean isLightweight(Component paramAnonymousComponent) {
/*   939 */         return paramAnonymousComponent.peer instanceof LightweightPeer;
/*       */       }
/*       */       public boolean getIgnoreRepaint(Component paramAnonymousComponent) {
/*   942 */         return paramAnonymousComponent.ignoreRepaint;
/*       */       }
/*       */       public int getWidth(Component paramAnonymousComponent) {
/*   945 */         return paramAnonymousComponent.width;
/*       */       }
/*       */       public int getHeight(Component paramAnonymousComponent) {
/*   948 */         return paramAnonymousComponent.height;
/*       */       }
/*       */       public int getX(Component paramAnonymousComponent) {
/*   951 */         return paramAnonymousComponent.x;
/*       */       }
/*       */       public int getY(Component paramAnonymousComponent) {
/*   954 */         return paramAnonymousComponent.y;
/*       */       }
/*       */       public Color getForeground(Component paramAnonymousComponent) {
/*   957 */         return paramAnonymousComponent.foreground;
/*       */       }
/*       */       public Color getBackground(Component paramAnonymousComponent) {
/*   960 */         return paramAnonymousComponent.background;
/*       */       }
/*       */       public void setBackground(Component paramAnonymousComponent, Color paramAnonymousColor) {
/*   963 */         paramAnonymousComponent.background = paramAnonymousColor;
/*       */       }
/*       */       public Font getFont(Component paramAnonymousComponent) {
/*   966 */         return paramAnonymousComponent.getFont_NoClientCode();
/*       */       }
/*       */       public void processEvent(Component paramAnonymousComponent, AWTEvent paramAnonymousAWTEvent) {
/*   969 */         paramAnonymousComponent.processEvent(paramAnonymousAWTEvent);
/*       */       }
/*       */ 
/*       */       public AccessControlContext getAccessControlContext(Component paramAnonymousComponent) {
/*   973 */         return paramAnonymousComponent.getAccessControlContext();
/*       */       }
/*       */ 
/*       */       public void revalidateSynchronously(Component paramAnonymousComponent) {
/*   977 */         paramAnonymousComponent.revalidateSynchronously();
/*       */       }
/*       */     });
/*       */   }
/*       */ 
/*       */   static class AWTTreeLock
/*       */   {
/*       */   }
/*       */ 
/*       */   protected abstract class AccessibleAWTComponent extends AccessibleContext
/*       */     implements Serializable, AccessibleComponent
/*       */   {
/*       */     private static final long serialVersionUID = 642321655757800191L;
/*  9016 */     protected ComponentListener accessibleAWTComponentHandler = null;
/*  9017 */     protected FocusListener accessibleAWTFocusHandler = null;
/*       */ 
/*       */     protected AccessibleAWTComponent()
/*       */     {
/*       */     }
/*       */ 
/*       */     public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*       */     {
/*  9078 */       if (this.accessibleAWTComponentHandler == null) {
/*  9079 */         this.accessibleAWTComponentHandler = new AccessibleAWTComponentHandler();
/*  9080 */         Component.this.addComponentListener(this.accessibleAWTComponentHandler);
/*       */       }
/*  9082 */       if (this.accessibleAWTFocusHandler == null) {
/*  9083 */         this.accessibleAWTFocusHandler = new AccessibleAWTFocusHandler();
/*  9084 */         Component.this.addFocusListener(this.accessibleAWTFocusHandler);
/*       */       }
/*  9086 */       super.addPropertyChangeListener(paramPropertyChangeListener);
/*       */     }
/*       */ 
/*       */     public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*       */     {
/*  9097 */       if (this.accessibleAWTComponentHandler != null) {
/*  9098 */         Component.this.removeComponentListener(this.accessibleAWTComponentHandler);
/*  9099 */         this.accessibleAWTComponentHandler = null;
/*       */       }
/*  9101 */       if (this.accessibleAWTFocusHandler != null) {
/*  9102 */         Component.this.removeFocusListener(this.accessibleAWTFocusHandler);
/*  9103 */         this.accessibleAWTFocusHandler = null;
/*       */       }
/*  9105 */       super.removePropertyChangeListener(paramPropertyChangeListener);
/*       */     }
/*       */ 
/*       */     public String getAccessibleName()
/*       */     {
/*  9126 */       return this.accessibleName;
/*       */     }
/*       */ 
/*       */     public String getAccessibleDescription()
/*       */     {
/*  9145 */       return this.accessibleDescription;
/*       */     }
/*       */ 
/*       */     public AccessibleRole getAccessibleRole()
/*       */     {
/*  9156 */       return AccessibleRole.AWT_COMPONENT;
/*       */     }
/*       */ 
/*       */     public AccessibleStateSet getAccessibleStateSet()
/*       */     {
/*  9167 */       return Component.this.getAccessibleStateSet();
/*       */     }
/*       */ 
/*       */     public Accessible getAccessibleParent()
/*       */     {
/*  9180 */       if (this.accessibleParent != null) {
/*  9181 */         return this.accessibleParent;
/*       */       }
/*  9183 */       Container localContainer = Component.this.getParent();
/*  9184 */       if ((localContainer instanceof Accessible)) {
/*  9185 */         return (Accessible)localContainer;
/*       */       }
/*       */ 
/*  9188 */       return null;
/*       */     }
/*       */ 
/*       */     public int getAccessibleIndexInParent()
/*       */     {
/*  9199 */       return Component.this.getAccessibleIndexInParent();
/*       */     }
/*       */ 
/*       */     public int getAccessibleChildrenCount()
/*       */     {
/*  9210 */       return 0;
/*       */     }
/*       */ 
/*       */     public Accessible getAccessibleChild(int paramInt)
/*       */     {
/*  9220 */       return null;
/*       */     }
/*       */ 
/*       */     public Locale getLocale()
/*       */     {
/*  9229 */       return Component.this.getLocale();
/*       */     }
/*       */ 
/*       */     public AccessibleComponent getAccessibleComponent()
/*       */     {
/*  9240 */       return this;
/*       */     }
/*       */ 
/*       */     public Color getBackground()
/*       */     {
/*  9253 */       return Component.this.getBackground();
/*       */     }
/*       */ 
/*       */     public void setBackground(Color paramColor)
/*       */     {
/*  9264 */       Component.this.setBackground(paramColor);
/*       */     }
/*       */ 
/*       */     public Color getForeground()
/*       */     {
/*  9274 */       return Component.this.getForeground();
/*       */     }
/*       */ 
/*       */     public void setForeground(Color paramColor)
/*       */     {
/*  9283 */       Component.this.setForeground(paramColor);
/*       */     }
/*       */ 
/*       */     public Cursor getCursor()
/*       */     {
/*  9293 */       return Component.this.getCursor();
/*       */     }
/*       */ 
/*       */     public void setCursor(Cursor paramCursor)
/*       */     {
/*  9305 */       Component.this.setCursor(paramCursor);
/*       */     }
/*       */ 
/*       */     public Font getFont()
/*       */     {
/*  9315 */       return Component.this.getFont();
/*       */     }
/*       */ 
/*       */     public void setFont(Font paramFont)
/*       */     {
/*  9324 */       Component.this.setFont(paramFont);
/*       */     }
/*       */ 
/*       */     public FontMetrics getFontMetrics(Font paramFont)
/*       */     {
/*  9336 */       if (paramFont == null) {
/*  9337 */         return null;
/*       */       }
/*  9339 */       return Component.this.getFontMetrics(paramFont);
/*       */     }
/*       */ 
/*       */     public boolean isEnabled()
/*       */     {
/*  9349 */       return Component.this.isEnabled();
/*       */     }
/*       */ 
/*       */     public void setEnabled(boolean paramBoolean)
/*       */     {
/*  9358 */       boolean bool = Component.this.isEnabled();
/*  9359 */       Component.this.setEnabled(paramBoolean);
/*  9360 */       if ((paramBoolean != bool) && 
/*  9361 */         (Component.this.accessibleContext != null))
/*  9362 */         if (paramBoolean) {
/*  9363 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.ENABLED);
/*       */         }
/*       */         else
/*       */         {
/*  9367 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.ENABLED, null);
/*       */         }
/*       */     }
/*       */ 
/*       */     public boolean isVisible()
/*       */     {
/*  9385 */       return Component.this.isVisible();
/*       */     }
/*       */ 
/*       */     public void setVisible(boolean paramBoolean)
/*       */     {
/*  9394 */       boolean bool = Component.this.isVisible();
/*  9395 */       Component.this.setVisible(paramBoolean);
/*  9396 */       if ((paramBoolean != bool) && 
/*  9397 */         (Component.this.accessibleContext != null))
/*  9398 */         if (paramBoolean) {
/*  9399 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.VISIBLE);
/*       */         }
/*       */         else
/*       */         {
/*  9403 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.VISIBLE, null);
/*       */         }
/*       */     }
/*       */ 
/*       */     public boolean isShowing()
/*       */     {
/*  9421 */       return Component.this.isShowing();
/*       */     }
/*       */ 
/*       */     public boolean contains(Point paramPoint)
/*       */     {
/*  9434 */       return Component.this.contains(paramPoint);
/*       */     }
/*       */ 
/*       */     public Point getLocationOnScreen()
/*       */     {
/*  9444 */       synchronized (Component.this.getTreeLock()) {
/*  9445 */         if (Component.this.isShowing()) {
/*  9446 */           return Component.this.getLocationOnScreen();
/*       */         }
/*  9448 */         return null;
/*       */       }
/*       */     }
/*       */ 
/*       */     public Point getLocation()
/*       */     {
/*  9463 */       return Component.this.getLocation();
/*       */     }
/*       */ 
/*       */     public void setLocation(Point paramPoint)
/*       */     {
/*  9471 */       Component.this.setLocation(paramPoint);
/*       */     }
/*       */ 
/*       */     public Rectangle getBounds()
/*       */     {
/*  9483 */       return Component.this.getBounds();
/*       */     }
/*       */ 
/*       */     public void setBounds(Rectangle paramRectangle)
/*       */     {
/*  9495 */       Component.this.setBounds(paramRectangle);
/*       */     }
/*       */ 
/*       */     public Dimension getSize()
/*       */     {
/*  9510 */       return Component.this.getSize();
/*       */     }
/*       */ 
/*       */     public void setSize(Dimension paramDimension)
/*       */     {
/*  9519 */       Component.this.setSize(paramDimension);
/*       */     }
/*       */ 
/*       */     public Accessible getAccessibleAt(Point paramPoint)
/*       */     {
/*  9535 */       return null;
/*       */     }
/*       */ 
/*       */     public boolean isFocusTraversable()
/*       */     {
/*  9544 */       return Component.this.isFocusTraversable();
/*       */     }
/*       */ 
/*       */     public void requestFocus()
/*       */     {
/*  9551 */       Component.this.requestFocus();
/*       */     }
/*       */ 
/*       */     public void addFocusListener(FocusListener paramFocusListener)
/*       */     {
/*  9561 */       Component.this.addFocusListener(paramFocusListener);
/*       */     }
/*       */ 
/*       */     public void removeFocusListener(FocusListener paramFocusListener)
/*       */     {
/*  9571 */       Component.this.removeFocusListener(paramFocusListener);
/*       */     }
/*       */ 
/*       */     protected class AccessibleAWTComponentHandler
/*       */       implements ComponentListener
/*       */     {
/*       */       protected AccessibleAWTComponentHandler()
/*       */       {
/*       */       }
/*       */ 
/*       */       public void componentHidden(ComponentEvent paramComponentEvent)
/*       */       {
/*  9026 */         if (Component.this.accessibleContext != null)
/*  9027 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.VISIBLE, null);
/*       */       }
/*       */ 
/*       */       public void componentShown(ComponentEvent paramComponentEvent)
/*       */       {
/*  9034 */         if (Component.this.accessibleContext != null)
/*  9035 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.VISIBLE);
/*       */       }
/*       */ 
/*       */       public void componentMoved(ComponentEvent paramComponentEvent)
/*       */       {
/*       */       }
/*       */ 
/*       */       public void componentResized(ComponentEvent paramComponentEvent)
/*       */       {
/*       */       }
/*       */     }
/*       */ 
/*       */     protected class AccessibleAWTFocusHandler
/*       */       implements FocusListener
/*       */     {
/*       */       protected AccessibleAWTFocusHandler()
/*       */       {
/*       */       }
/*       */ 
/*       */       public void focusGained(FocusEvent paramFocusEvent)
/*       */       {
/*  9056 */         if (Component.this.accessibleContext != null)
/*  9057 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.FOCUSED);
/*       */       }
/*       */ 
/*       */       public void focusLost(FocusEvent paramFocusEvent)
/*       */       {
/*  9063 */         if (Component.this.accessibleContext != null)
/*  9064 */           Component.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.FOCUSED, null);
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   public static enum BaselineResizeBehavior
/*       */   {
/*   729 */     CONSTANT_ASCENT, 
/*       */ 
/*   740 */     CONSTANT_DESCENT, 
/*       */ 
/*   779 */     CENTER_OFFSET, 
/*       */ 
/*   787 */     OTHER;
/*       */   }
/*       */ 
/*       */   protected class BltBufferStrategy extends BufferStrategy
/*       */   {
/*       */     protected BufferCapabilities caps;
/*       */     protected VolatileImage[] backBuffers;
/*       */     protected boolean validatedContents;
/*       */     protected int width;
/*       */     protected int height;
/*       */     private Insets insets;
/*       */ 
/*       */     protected BltBufferStrategy(int paramBufferCapabilities, BufferCapabilities arg3)
/*       */     {
/*       */       Object localObject;
/*  4266 */       this.caps = localObject;
/*  4267 */       createBackBuffers(paramBufferCapabilities - 1);
/*       */     }
/*       */ 
/*       */     public void dispose()
/*       */     {
/*  4275 */       if (this.backBuffers != null) {
/*  4276 */         for (int i = this.backBuffers.length - 1; i >= 0; 
/*  4277 */           i--) {
/*  4278 */           if (this.backBuffers[i] != null) {
/*  4279 */             this.backBuffers[i].flush();
/*  4280 */             this.backBuffers[i] = null;
/*       */           }
/*       */         }
/*       */       }
/*  4284 */       if (Component.this.bufferStrategy == this)
/*  4285 */         Component.this.bufferStrategy = null;
/*       */     }
/*       */ 
/*       */     protected void createBackBuffers(int paramInt)
/*       */     {
/*  4293 */       if (paramInt == 0) {
/*  4294 */         this.backBuffers = null;
/*       */       }
/*       */       else {
/*  4297 */         this.width = Component.this.getWidth();
/*  4298 */         this.height = Component.this.getHeight();
/*  4299 */         this.insets = Component.this.getInsets_NoClientCode();
/*  4300 */         int i = this.width - this.insets.left - this.insets.right;
/*  4301 */         int j = this.height - this.insets.top - this.insets.bottom;
/*       */ 
/*  4306 */         i = Math.max(1, i);
/*  4307 */         j = Math.max(1, j);
/*  4308 */         if (this.backBuffers == null) {
/*  4309 */           this.backBuffers = new VolatileImage[paramInt];
/*       */         }
/*       */         else {
/*  4312 */           for (k = 0; k < paramInt; k++) {
/*  4313 */             if (this.backBuffers[k] != null) {
/*  4314 */               this.backBuffers[k].flush();
/*  4315 */               this.backBuffers[k] = null;
/*       */             }
/*       */           }
/*       */ 
/*       */         }
/*       */ 
/*  4321 */         for (int k = 0; k < paramInt; k++)
/*  4322 */           this.backBuffers[k] = Component.this.createVolatileImage(i, j);
/*       */       }
/*       */     }
/*       */ 
/*       */     public BufferCapabilities getCapabilities()
/*       */     {
/*  4331 */       return this.caps;
/*       */     }
/*       */ 
/*       */     public Graphics getDrawGraphics()
/*       */     {
/*  4338 */       revalidate();
/*  4339 */       Image localImage = getBackBuffer();
/*  4340 */       if (localImage == null) {
/*  4341 */         return Component.this.getGraphics();
/*       */       }
/*  4343 */       SunGraphics2D localSunGraphics2D = (SunGraphics2D)localImage.getGraphics();
/*  4344 */       localSunGraphics2D.constrain(-this.insets.left, -this.insets.top, localImage.getWidth(null) + this.insets.left, localImage.getHeight(null) + this.insets.top);
/*       */ 
/*  4347 */       return localSunGraphics2D;
/*       */     }
/*       */ 
/*       */     Image getBackBuffer()
/*       */     {
/*  4355 */       if (this.backBuffers != null) {
/*  4356 */         return this.backBuffers[(this.backBuffers.length - 1)];
/*       */       }
/*  4358 */       return null;
/*       */     }
/*       */ 
/*       */     public void show()
/*       */     {
/*  4366 */       showSubRegion(this.insets.left, this.insets.top, this.width - this.insets.right, this.height - this.insets.bottom);
/*       */     }
/*       */ 
/*       */     void showSubRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */     {
/*  4381 */       if (this.backBuffers == null) {
/*  4382 */         return;
/*       */       }
/*       */ 
/*  4385 */       paramInt1 -= this.insets.left;
/*  4386 */       paramInt3 -= this.insets.left;
/*  4387 */       paramInt2 -= this.insets.top;
/*  4388 */       paramInt4 -= this.insets.top;
/*  4389 */       Graphics localGraphics = Component.this.getGraphics_NoClientCode();
/*  4390 */       if (localGraphics == null)
/*       */       {
/*  4392 */         return;
/*       */       }
/*       */ 
/*       */       try
/*       */       {
/*  4397 */         localGraphics.translate(this.insets.left, this.insets.top);
/*  4398 */         for (int i = 0; i < this.backBuffers.length; i++) {
/*  4399 */           localGraphics.drawImage(this.backBuffers[i], paramInt1, paramInt2, paramInt3, paramInt4, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*       */ 
/*  4403 */           localGraphics.dispose();
/*  4404 */           localGraphics = null;
/*  4405 */           localGraphics = this.backBuffers[i].getGraphics();
/*       */         }
/*       */       } finally {
/*  4408 */         if (localGraphics != null)
/*  4409 */           localGraphics.dispose();
/*       */       }
/*       */     }
/*       */ 
/*       */     protected void revalidate()
/*       */     {
/*  4418 */       revalidate(true);
/*       */     }
/*       */ 
/*       */     void revalidate(boolean paramBoolean) {
/*  4422 */       this.validatedContents = false;
/*       */ 
/*  4424 */       if (this.backBuffers == null) {
/*  4425 */         return;
/*       */       }
/*       */ 
/*  4428 */       if (paramBoolean) {
/*  4429 */         localObject = Component.this.getInsets_NoClientCode();
/*  4430 */         if ((Component.this.getWidth() != this.width) || (Component.this.getHeight() != this.height) || (!((Insets)localObject).equals(this.insets)))
/*       */         {
/*  4433 */           createBackBuffers(this.backBuffers.length);
/*  4434 */           this.validatedContents = true;
/*       */         }
/*       */ 
/*       */       }
/*       */ 
/*  4439 */       Object localObject = Component.this.getGraphicsConfiguration_NoClientCode();
/*  4440 */       int i = this.backBuffers[(this.backBuffers.length - 1)].validate((GraphicsConfiguration)localObject);
/*       */ 
/*  4442 */       if (i == 2) {
/*  4443 */         if (paramBoolean) {
/*  4444 */           createBackBuffers(this.backBuffers.length);
/*       */ 
/*  4446 */           this.backBuffers[(this.backBuffers.length - 1)].validate((GraphicsConfiguration)localObject);
/*       */         }
/*       */ 
/*  4452 */         this.validatedContents = true;
/*  4453 */       } else if (i == 1) {
/*  4454 */         this.validatedContents = true;
/*       */       }
/*       */     }
/*       */ 
/*       */     public boolean contentsLost()
/*       */     {
/*  4463 */       if (this.backBuffers == null) {
/*  4464 */         return false;
/*       */       }
/*  4466 */       return this.backBuffers[(this.backBuffers.length - 1)].contentsLost();
/*       */     }
/*       */ 
/*       */     public boolean contentsRestored()
/*       */     {
/*  4475 */       return this.validatedContents;
/*       */     }
/*       */   }
/*       */ 
/*       */   private class BltSubRegionBufferStrategy extends Component.BltBufferStrategy
/*       */     implements SubRegionShowable
/*       */   {
/*       */     protected BltSubRegionBufferStrategy(int paramBufferCapabilities, BufferCapabilities arg3)
/*       */     {
/*  4520 */       super(paramBufferCapabilities, localBufferCapabilities);
/*       */     }
/*       */ 
/*       */     public void show(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  4524 */       showSubRegion(paramInt1, paramInt2, paramInt3, paramInt4);
/*       */     }
/*       */ 
/*       */     public boolean showIfNotLost(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */     {
/*  4529 */       if (!contentsLost()) {
/*  4530 */         showSubRegion(paramInt1, paramInt2, paramInt3, paramInt4);
/*  4531 */         return !contentsLost();
/*       */       }
/*  4533 */       return false;
/*       */     }
/*       */   }
/*       */ 
/*       */   private static class DummyRequestFocusController
/*       */     implements RequestFocusController
/*       */   {
/*       */     public boolean acceptRequestFocus(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, CausedFocusEvent.Cause paramCause)
/*       */     {
/*  7760 */       return true;
/*       */     }
/*       */   }
/*       */ 
/*       */   protected class FlipBufferStrategy extends BufferStrategy
/*       */   {
/*       */     protected int numBuffers;
/*       */     protected BufferCapabilities caps;
/*       */     protected Image drawBuffer;
/*       */     protected VolatileImage drawVBuffer;
/*       */     protected boolean validatedContents;
/*       */     int width;
/*       */     int height;
/*       */ 
/*       */     protected FlipBufferStrategy(int paramBufferCapabilities, BufferCapabilities arg3)
/*       */       throws AWTException
/*       */     {
/*  3959 */       if ((!(Component.this instanceof Window)) && (!(Component.this instanceof Canvas)))
/*       */       {
/*  3962 */         throw new ClassCastException("Component must be a Canvas or Window");
/*       */       }
/*       */ 
/*  3965 */       this.numBuffers = paramBufferCapabilities;
/*       */       BufferCapabilities localBufferCapabilities;
/*  3966 */       this.caps = localBufferCapabilities;
/*  3967 */       createBuffers(paramBufferCapabilities, localBufferCapabilities);
/*       */     }
/*       */ 
/*       */     protected void createBuffers(int paramInt, BufferCapabilities paramBufferCapabilities)
/*       */       throws AWTException
/*       */     {
/*  3989 */       if (paramInt < 2) {
/*  3990 */         throw new IllegalArgumentException("Number of buffers cannot be less than two");
/*       */       }
/*  3992 */       if (Component.this.peer == null) {
/*  3993 */         throw new IllegalStateException("Component must have a valid peer");
/*       */       }
/*  3995 */       if ((paramBufferCapabilities == null) || (!paramBufferCapabilities.isPageFlipping())) {
/*  3996 */         throw new IllegalArgumentException("Page flipping capabilities must be specified");
/*       */       }
/*       */ 
/*  4001 */       this.width = Component.this.getWidth();
/*  4002 */       this.height = Component.this.getHeight();
/*       */ 
/*  4004 */       if (this.drawBuffer != null)
/*       */       {
/*  4006 */         this.drawBuffer = null;
/*  4007 */         this.drawVBuffer = null;
/*  4008 */         destroyBuffers();
/*       */       }
/*       */ 
/*  4012 */       if ((paramBufferCapabilities instanceof ExtendedBufferCapabilities)) {
/*  4013 */         ExtendedBufferCapabilities localExtendedBufferCapabilities = (ExtendedBufferCapabilities)paramBufferCapabilities;
/*       */ 
/*  4015 */         if (localExtendedBufferCapabilities.getVSync() == ExtendedBufferCapabilities.VSyncType.VSYNC_ON)
/*       */         {
/*  4021 */           if (!VSyncedBSManager.vsyncAllowed(this)) {
/*  4022 */             paramBufferCapabilities = localExtendedBufferCapabilities.derive(ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT);
/*       */           }
/*       */         }
/*       */       }
/*       */ 
/*  4027 */       Component.this.peer.createBuffers(paramInt, paramBufferCapabilities);
/*  4028 */       updateInternalBuffers();
/*       */     }
/*       */ 
/*       */     private void updateInternalBuffers()
/*       */     {
/*  4037 */       this.drawBuffer = getBackBuffer();
/*  4038 */       if ((this.drawBuffer instanceof VolatileImage))
/*  4039 */         this.drawVBuffer = ((VolatileImage)this.drawBuffer);
/*       */       else
/*  4041 */         this.drawVBuffer = null;
/*       */     }
/*       */ 
/*       */     protected Image getBackBuffer()
/*       */     {
/*  4051 */       if (Component.this.peer != null) {
/*  4052 */         return Component.this.peer.getBackBuffer();
/*       */       }
/*  4054 */       throw new IllegalStateException("Component must have a valid peer");
/*       */     }
/*       */ 
/*       */     protected void flip(BufferCapabilities.FlipContents paramFlipContents)
/*       */     {
/*  4071 */       if (Component.this.peer != null) {
/*  4072 */         Image localImage = getBackBuffer();
/*  4073 */         if (localImage != null) {
/*  4074 */           Component.this.peer.flip(0, 0, localImage.getWidth(null), localImage.getHeight(null), paramFlipContents);
/*       */         }
/*       */       }
/*       */       else
/*       */       {
/*  4079 */         throw new IllegalStateException("Component must have a valid peer");
/*       */       }
/*       */     }
/*       */ 
/*       */     void flipSubRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BufferCapabilities.FlipContents paramFlipContents)
/*       */     {
/*  4087 */       if (Component.this.peer != null)
/*  4088 */         Component.this.peer.flip(paramInt1, paramInt2, paramInt3, paramInt4, paramFlipContents);
/*       */       else
/*  4090 */         throw new IllegalStateException("Component must have a valid peer");
/*       */     }
/*       */ 
/*       */     protected void destroyBuffers()
/*       */     {
/*  4099 */       VSyncedBSManager.releaseVsync(this);
/*  4100 */       if (Component.this.peer != null)
/*  4101 */         Component.this.peer.destroyBuffers();
/*       */       else
/*  4103 */         throw new IllegalStateException("Component must have a valid peer");
/*       */     }
/*       */ 
/*       */     public BufferCapabilities getCapabilities()
/*       */     {
/*  4112 */       if ((this.caps instanceof Component.ProxyCapabilities)) {
/*  4113 */         return Component.ProxyCapabilities.access$300((Component.ProxyCapabilities)this.caps);
/*       */       }
/*  4115 */       return this.caps;
/*       */     }
/*       */ 
/*       */     public Graphics getDrawGraphics()
/*       */     {
/*  4126 */       revalidate();
/*  4127 */       return this.drawBuffer.getGraphics();
/*       */     }
/*       */ 
/*       */     protected void revalidate()
/*       */     {
/*  4134 */       revalidate(true);
/*       */     }
/*       */ 
/*       */     void revalidate(boolean paramBoolean) {
/*  4138 */       this.validatedContents = false;
/*       */ 
/*  4140 */       if ((paramBoolean) && ((Component.this.getWidth() != this.width) || (Component.this.getHeight() != this.height)))
/*       */       {
/*       */         try {
/*  4143 */           createBuffers(this.numBuffers, this.caps);
/*       */         }
/*       */         catch (AWTException localAWTException1) {
/*       */         }
/*  4147 */         this.validatedContents = true;
/*       */       }
/*       */ 
/*  4152 */       updateInternalBuffers();
/*       */ 
/*  4155 */       if (this.drawVBuffer != null) {
/*  4156 */         GraphicsConfiguration localGraphicsConfiguration = Component.this.getGraphicsConfiguration_NoClientCode();
/*       */ 
/*  4158 */         int i = this.drawVBuffer.validate(localGraphicsConfiguration);
/*  4159 */         if (i == 2) {
/*       */           try {
/*  4161 */             createBuffers(this.numBuffers, this.caps);
/*       */           }
/*       */           catch (AWTException localAWTException2) {
/*       */           }
/*  4165 */           if (this.drawVBuffer != null)
/*       */           {
/*  4167 */             this.drawVBuffer.validate(localGraphicsConfiguration);
/*       */           }
/*  4169 */           this.validatedContents = true;
/*  4170 */         } else if (i == 1) {
/*  4171 */           this.validatedContents = true;
/*       */         }
/*       */       }
/*       */     }
/*       */ 
/*       */     public boolean contentsLost()
/*       */     {
/*  4181 */       if (this.drawVBuffer == null) {
/*  4182 */         return false;
/*       */       }
/*  4184 */       return this.drawVBuffer.contentsLost();
/*       */     }
/*       */ 
/*       */     public boolean contentsRestored()
/*       */     {
/*  4192 */       return this.validatedContents;
/*       */     }
/*       */ 
/*       */     public void show()
/*       */     {
/*  4200 */       flip(this.caps.getFlipContents());
/*       */     }
/*       */ 
/*       */     void showSubRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */     {
/*  4208 */       flipSubRegion(paramInt1, paramInt2, paramInt3, paramInt4, this.caps.getFlipContents());
/*       */     }
/*       */ 
/*       */     public void dispose()
/*       */     {
/*  4216 */       if (Component.this.bufferStrategy == this) {
/*  4217 */         Component.this.bufferStrategy = null;
/*  4218 */         if (Component.this.peer != null)
/*  4219 */           destroyBuffers();
/*       */       }
/*       */     }
/*       */   }
/*       */ 
/*       */   private class FlipSubRegionBufferStrategy extends Component.FlipBufferStrategy
/*       */     implements SubRegionShowable
/*       */   {
/*       */     protected FlipSubRegionBufferStrategy(int paramBufferCapabilities, BufferCapabilities arg3)
/*       */       throws AWTException
/*       */     {
/*  4490 */       super(paramBufferCapabilities, localBufferCapabilities);
/*       */     }
/*       */ 
/*       */     public void show(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  4494 */       showSubRegion(paramInt1, paramInt2, paramInt3, paramInt4);
/*       */     }
/*       */ 
/*       */     public boolean showIfNotLost(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*       */     {
/*  4499 */       if (!contentsLost()) {
/*  4500 */         showSubRegion(paramInt1, paramInt2, paramInt3, paramInt4);
/*  4501 */         return !contentsLost();
/*       */       }
/*  4503 */       return false;
/*       */     }
/*       */   }
/*       */ 
/*       */   private class ProxyCapabilities extends ExtendedBufferCapabilities
/*       */   {
/*       */     private BufferCapabilities orig;
/*       */ 
/*       */     private ProxyCapabilities(BufferCapabilities arg2)
/*       */     {
/*  3860 */       super(localObject.getBackBufferCapabilities(), localObject.getFlipContents() == BufferCapabilities.FlipContents.BACKGROUND ? BufferCapabilities.FlipContents.BACKGROUND : BufferCapabilities.FlipContents.COPIED);
/*       */ 
/*  3866 */       this.orig = localObject;
/*       */     }
/*       */   }
/*       */ 
/*       */   private class SingleBufferStrategy extends BufferStrategy
/*       */   {
/*       */     private BufferCapabilities caps;
/*       */ 
/*       */     public SingleBufferStrategy(BufferCapabilities arg2)
/*       */     {
/*       */       Object localObject;
/*  4551 */       this.caps = localObject;
/*       */     }
/*       */     public BufferCapabilities getCapabilities() {
/*  4554 */       return this.caps;
/*       */     }
/*       */     public Graphics getDrawGraphics() {
/*  4557 */       return Component.this.getGraphics();
/*       */     }
/*       */     public boolean contentsLost() {
/*  4560 */       return false;
/*       */     }
/*       */     public boolean contentsRestored() {
/*  4563 */       return false;
/*       */     }
/*       */ 
/*       */     public void show()
/*       */     {
/*       */     }
/*       */   }
/*       */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Component
 * JD-Core Version:    0.6.2
 */