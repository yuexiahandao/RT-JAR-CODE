/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.peer.KeyboardFocusManagerPeer;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.beans.VetoableChangeListener;
/*      */ import java.beans.VetoableChangeSupport;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.KeyboardFocusManagerAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.CausedFocusEvent;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.KeyboardFocusManagerPeerProvider;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public abstract class KeyboardFocusManager
/*      */   implements KeyEventDispatcher, KeyEventPostProcessor
/*      */ {
/*      */   private static final PlatformLogger focusLog;
/*      */   transient KeyboardFocusManagerPeer peer;
/*  164 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.KeyboardFocusManager");
/*      */   public static final int FORWARD_TRAVERSAL_KEYS = 0;
/*      */   public static final int BACKWARD_TRAVERSAL_KEYS = 1;
/*      */   public static final int UP_CYCLE_TRAVERSAL_KEYS = 2;
/*      */   public static final int DOWN_CYCLE_TRAVERSAL_KEYS = 3;
/*      */   static final int TRAVERSAL_KEY_LENGTH = 4;
/*      */   private static Component focusOwner;
/*      */   private static Component permanentFocusOwner;
/*      */   private static Window focusedWindow;
/*      */   private static Window activeWindow;
/*  314 */   private FocusTraversalPolicy defaultPolicy = new DefaultFocusTraversalPolicy();
/*      */ 
/*  320 */   private static final String[] defaultFocusTraversalKeyPropertyNames = { "forwardDefaultFocusTraversalKeys", "backwardDefaultFocusTraversalKeys", "upCycleDefaultFocusTraversalKeys", "downCycleDefaultFocusTraversalKeys" };
/*      */ 
/*  330 */   private static final AWTKeyStroke[][] defaultFocusTraversalKeyStrokes = { { AWTKeyStroke.getAWTKeyStroke(9, 0, false), AWTKeyStroke.getAWTKeyStroke(9, 130, false) }, { AWTKeyStroke.getAWTKeyStroke(9, 65, false), AWTKeyStroke.getAWTKeyStroke(9, 195, false) }, new AWTKeyStroke[0], new AWTKeyStroke[0] };
/*      */ 
/*  351 */   private Set[] defaultFocusTraversalKeys = new Set[4];
/*      */   private static Container currentFocusCycleRoot;
/*      */   private VetoableChangeSupport vetoableSupport;
/*      */   private PropertyChangeSupport changeSupport;
/*      */   private LinkedList keyEventDispatchers;
/*      */   private LinkedList keyEventPostProcessors;
/*  393 */   private static Map mostRecentFocusOwners = new WeakHashMap();
/*      */   private static final String notPrivileged = "this KeyboardFocusManager is not installed in the current thread's context";
/*      */   private static AWTPermission replaceKeyboardFocusManagerPermission;
/*  409 */   transient SequencedEvent currentSequencedEvent = null;
/*      */ 
/* 2203 */   private static LinkedList<HeavyweightFocusRequest> heavyweightRequests = new LinkedList();
/*      */   private static LinkedList<LightweightFocusRequest> currentLightweightRequests;
/*      */   private static boolean clearingCurrentLightweightRequests;
/* 2207 */   private static boolean allowSyncFocusRequests = true;
/* 2208 */   private static Component newFocusOwner = null;
/*      */   private static volatile boolean disableRestoreFocus;
/*      */   static final int SNFH_FAILURE = 0;
/*      */   static final int SNFH_SUCCESS_HANDLED = 1;
/*      */   static final int SNFH_SUCCESS_PROCEED = 2;
/*      */   static Field proxyActive;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public static KeyboardFocusManager getCurrentKeyboardFocusManager()
/*      */   {
/*  216 */     return getCurrentKeyboardFocusManager(AppContext.getAppContext());
/*      */   }
/*      */ 
/*      */   static synchronized KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext paramAppContext)
/*      */   {
/*  222 */     Object localObject = (KeyboardFocusManager)paramAppContext.get(KeyboardFocusManager.class);
/*      */ 
/*  224 */     if (localObject == null) {
/*  225 */       localObject = new DefaultKeyboardFocusManager();
/*  226 */       paramAppContext.put(KeyboardFocusManager.class, localObject);
/*      */     }
/*  228 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static void setCurrentKeyboardFocusManager(KeyboardFocusManager paramKeyboardFocusManager)
/*      */     throws SecurityException
/*      */   {
/*  251 */     checkReplaceKFMPermission();
/*      */ 
/*  253 */     KeyboardFocusManager localKeyboardFocusManager = null;
/*      */ 
/*  255 */     synchronized (KeyboardFocusManager.class) {
/*  256 */       AppContext localAppContext = AppContext.getAppContext();
/*      */ 
/*  258 */       if (paramKeyboardFocusManager != null) {
/*  259 */         localKeyboardFocusManager = getCurrentKeyboardFocusManager(localAppContext);
/*      */ 
/*  261 */         localAppContext.put(KeyboardFocusManager.class, paramKeyboardFocusManager);
/*      */       } else {
/*  263 */         localKeyboardFocusManager = getCurrentKeyboardFocusManager(localAppContext);
/*  264 */         localAppContext.remove(KeyboardFocusManager.class);
/*      */       }
/*      */     }
/*      */ 
/*  268 */     if (localKeyboardFocusManager != null) {
/*  269 */       localKeyboardFocusManager.firePropertyChange("managingFocus", Boolean.TRUE, Boolean.FALSE);
/*      */     }
/*      */ 
/*  273 */     if (paramKeyboardFocusManager != null)
/*  274 */       paramKeyboardFocusManager.firePropertyChange("managingFocus", Boolean.FALSE, Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   final void setCurrentSequencedEvent(SequencedEvent paramSequencedEvent)
/*      */   {
/*  412 */     synchronized (SequencedEvent.class) {
/*  413 */       assert ((paramSequencedEvent == null) || (this.currentSequencedEvent == null));
/*  414 */       this.currentSequencedEvent = paramSequencedEvent;
/*      */     }
/*      */   }
/*      */ 
/*      */   final SequencedEvent getCurrentSequencedEvent() {
/*  419 */     synchronized (SequencedEvent.class) {
/*  420 */       return this.currentSequencedEvent;
/*      */     }
/*      */   }
/*      */ 
/*      */   static Set initFocusTraversalKeysSet(String paramString, Set paramSet) {
/*  425 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/*  426 */     while (localStringTokenizer.hasMoreTokens()) {
/*  427 */       paramSet.add(AWTKeyStroke.getAWTKeyStroke(localStringTokenizer.nextToken()));
/*      */     }
/*  429 */     return paramSet.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(paramSet);
/*      */   }
/*      */ 
/*      */   public KeyboardFocusManager()
/*      */   {
/*  438 */     for (int i = 0; i < 4; i++) {
/*  439 */       HashSet localHashSet = new HashSet();
/*  440 */       for (int j = 0; j < defaultFocusTraversalKeyStrokes[i].length; j++) {
/*  441 */         localHashSet.add(defaultFocusTraversalKeyStrokes[i][j]);
/*      */       }
/*  443 */       this.defaultFocusTraversalKeys[i] = (localHashSet.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(localHashSet));
/*      */     }
/*      */ 
/*  447 */     initPeer();
/*      */   }
/*      */ 
/*      */   private void initPeer() {
/*  451 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  452 */     KeyboardFocusManagerPeerProvider localKeyboardFocusManagerPeerProvider = (KeyboardFocusManagerPeerProvider)localToolkit;
/*  453 */     this.peer = localKeyboardFocusManagerPeerProvider.getKeyboardFocusManagerPeer();
/*      */   }
/*      */ 
/*      */   public Component getFocusOwner()
/*      */   {
/*  471 */     synchronized (KeyboardFocusManager.class) {
/*  472 */       if (focusOwner == null) {
/*  473 */         return null;
/*      */       }
/*      */ 
/*  476 */       return focusOwner.appContext == AppContext.getAppContext() ? focusOwner : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Component getGlobalFocusOwner()
/*      */     throws SecurityException
/*      */   {
/*  502 */     synchronized (KeyboardFocusManager.class) {
/*  503 */       checkKFMSecurity();
/*  504 */       return focusOwner;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setGlobalFocusOwner(Component paramComponent)
/*      */   {
/*  533 */     Component localComponent = null;
/*  534 */     int i = 0;
/*      */ 
/*  536 */     if ((paramComponent == null) || (paramComponent.isFocusable())) {
/*  537 */       synchronized (KeyboardFocusManager.class) {
/*  538 */         checkKFMSecurity();
/*      */ 
/*  540 */         localComponent = getFocusOwner();
/*      */         try
/*      */         {
/*  543 */           fireVetoableChange("focusOwner", localComponent, paramComponent);
/*      */         }
/*      */         catch (PropertyVetoException localPropertyVetoException)
/*      */         {
/*  547 */           return;
/*      */         }
/*      */ 
/*  550 */         focusOwner = paramComponent;
/*      */ 
/*  552 */         if ((paramComponent != null) && ((getCurrentFocusCycleRoot() == null) || (!paramComponent.isFocusCycleRoot(getCurrentFocusCycleRoot()))))
/*      */         {
/*  556 */           Container localContainer = paramComponent.getFocusCycleRootAncestor();
/*      */ 
/*  558 */           if ((localContainer == null) && ((paramComponent instanceof Window)))
/*      */           {
/*  560 */             localContainer = (Container)paramComponent;
/*      */           }
/*  562 */           if (localContainer != null) {
/*  563 */             setGlobalCurrentFocusCycleRoot(localContainer);
/*      */           }
/*      */         }
/*      */ 
/*  567 */         i = 1;
/*      */       }
/*      */     }
/*      */ 
/*  571 */     if (i != 0)
/*  572 */       firePropertyChange("focusOwner", localComponent, paramComponent);
/*      */   }
/*      */ 
/*      */   public void clearGlobalFocusOwner()
/*      */   {
/*  589 */     synchronized (KeyboardFocusManager.class) {
/*  590 */       checkKFMSecurity();
/*      */     }
/*  592 */     if (!GraphicsEnvironment.isHeadless())
/*      */     {
/*  595 */       Toolkit.getDefaultToolkit();
/*      */ 
/*  597 */       _clearGlobalFocusOwner();
/*      */     }
/*      */   }
/*      */ 
/*  601 */   private void _clearGlobalFocusOwner() { Window localWindow = markClearGlobalFocusOwner();
/*  602 */     this.peer.clearGlobalFocusOwner(localWindow); }
/*      */ 
/*      */   Component getNativeFocusOwner()
/*      */   {
/*  606 */     return this.peer.getCurrentFocusOwner();
/*      */   }
/*      */ 
/*      */   void setNativeFocusOwner(Component paramComponent) {
/*  610 */     if (focusLog.isLoggable(300)) {
/*  611 */       focusLog.finest("Calling peer {0} setCurrentFocusOwner for {1}", new Object[] { String.valueOf(this.peer), String.valueOf(paramComponent) });
/*      */     }
/*      */ 
/*  614 */     this.peer.setCurrentFocusOwner(paramComponent);
/*      */   }
/*      */ 
/*      */   Window getNativeFocusedWindow() {
/*  618 */     return this.peer.getCurrentFocusedWindow();
/*      */   }
/*      */ 
/*      */   public Component getPermanentFocusOwner()
/*      */   {
/*  636 */     synchronized (KeyboardFocusManager.class) {
/*  637 */       if (permanentFocusOwner == null) {
/*  638 */         return null;
/*      */       }
/*      */ 
/*  641 */       return permanentFocusOwner.appContext == AppContext.getAppContext() ? permanentFocusOwner : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Component getGlobalPermanentFocusOwner()
/*      */     throws SecurityException
/*      */   {
/*  670 */     synchronized (KeyboardFocusManager.class) {
/*  671 */       checkKFMSecurity();
/*  672 */       return permanentFocusOwner;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setGlobalPermanentFocusOwner(Component paramComponent)
/*      */   {
/*  702 */     Component localComponent = null;
/*  703 */     int i = 0;
/*      */ 
/*  705 */     if ((paramComponent == null) || (paramComponent.isFocusable())) {
/*  706 */       synchronized (KeyboardFocusManager.class) {
/*  707 */         checkKFMSecurity();
/*      */ 
/*  709 */         localComponent = getPermanentFocusOwner();
/*      */         try
/*      */         {
/*  712 */           fireVetoableChange("permanentFocusOwner", localComponent, paramComponent);
/*      */         }
/*      */         catch (PropertyVetoException localPropertyVetoException)
/*      */         {
/*  717 */           return;
/*      */         }
/*      */ 
/*  720 */         permanentFocusOwner = paramComponent;
/*      */ 
/*  722 */         setMostRecentFocusOwner(paramComponent);
/*      */ 
/*  725 */         i = 1;
/*      */       }
/*      */     }
/*      */ 
/*  729 */     if (i != 0)
/*  730 */       firePropertyChange("permanentFocusOwner", localComponent, paramComponent);
/*      */   }
/*      */ 
/*      */   public Window getFocusedWindow()
/*      */   {
/*  746 */     synchronized (KeyboardFocusManager.class) {
/*  747 */       if (focusedWindow == null) {
/*  748 */         return null;
/*      */       }
/*      */ 
/*  751 */       return focusedWindow.appContext == AppContext.getAppContext() ? focusedWindow : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Window getGlobalFocusedWindow()
/*      */     throws SecurityException
/*      */   {
/*  773 */     synchronized (KeyboardFocusManager.class) {
/*  774 */       checkKFMSecurity();
/*  775 */       return focusedWindow;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setGlobalFocusedWindow(Window paramWindow)
/*      */   {
/*  801 */     Window localWindow = null;
/*  802 */     int i = 0;
/*      */ 
/*  804 */     if ((paramWindow == null) || (paramWindow.isFocusableWindow())) {
/*  805 */       synchronized (KeyboardFocusManager.class) {
/*  806 */         checkKFMSecurity();
/*      */ 
/*  808 */         localWindow = getFocusedWindow();
/*      */         try
/*      */         {
/*  811 */           fireVetoableChange("focusedWindow", localWindow, paramWindow);
/*      */         }
/*      */         catch (PropertyVetoException localPropertyVetoException)
/*      */         {
/*  815 */           return;
/*      */         }
/*      */ 
/*  818 */         focusedWindow = paramWindow;
/*  819 */         i = 1;
/*      */       }
/*      */     }
/*      */ 
/*  823 */     if (i != 0)
/*  824 */       firePropertyChange("focusedWindow", localWindow, paramWindow);
/*      */   }
/*      */ 
/*      */   public Window getActiveWindow()
/*      */   {
/*  843 */     synchronized (KeyboardFocusManager.class) {
/*  844 */       if (activeWindow == null) {
/*  845 */         return null;
/*      */       }
/*      */ 
/*  848 */       return activeWindow.appContext == AppContext.getAppContext() ? activeWindow : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Window getGlobalActiveWindow()
/*      */     throws SecurityException
/*      */   {
/*  873 */     synchronized (KeyboardFocusManager.class) {
/*  874 */       checkKFMSecurity();
/*  875 */       return activeWindow;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setGlobalActiveWindow(Window paramWindow)
/*      */   {
/*      */     Window localWindow;
/*  903 */     synchronized (KeyboardFocusManager.class) {
/*  904 */       checkKFMSecurity();
/*      */ 
/*  906 */       localWindow = getActiveWindow();
/*  907 */       if (focusLog.isLoggable(400)) {
/*  908 */         focusLog.finer("Setting global active window to " + paramWindow + ", old active " + localWindow);
/*      */       }
/*      */       try
/*      */       {
/*  912 */         fireVetoableChange("activeWindow", localWindow, paramWindow);
/*      */       }
/*      */       catch (PropertyVetoException localPropertyVetoException)
/*      */       {
/*  916 */         return;
/*      */       }
/*      */ 
/*  919 */       activeWindow = paramWindow;
/*      */     }
/*      */ 
/*  922 */     firePropertyChange("activeWindow", localWindow, paramWindow);
/*      */   }
/*      */ 
/*      */   public synchronized FocusTraversalPolicy getDefaultFocusTraversalPolicy()
/*      */   {
/*  936 */     return this.defaultPolicy;
/*      */   }
/*      */ 
/*      */   public void setDefaultFocusTraversalPolicy(FocusTraversalPolicy paramFocusTraversalPolicy)
/*      */   {
/*  957 */     if (paramFocusTraversalPolicy == null)
/*  958 */       throw new IllegalArgumentException("default focus traversal policy cannot be null");
/*      */     FocusTraversalPolicy localFocusTraversalPolicy;
/*  963 */     synchronized (this) {
/*  964 */       localFocusTraversalPolicy = this.defaultPolicy;
/*  965 */       this.defaultPolicy = paramFocusTraversalPolicy;
/*      */     }
/*      */ 
/*  968 */     firePropertyChange("defaultFocusTraversalPolicy", localFocusTraversalPolicy, paramFocusTraversalPolicy);
/*      */   }
/*      */ 
/*      */   public void setDefaultFocusTraversalKeys(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*      */   {
/* 1063 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 1064 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/* 1066 */     if (paramSet == null)
/* 1067 */       throw new IllegalArgumentException("cannot set null Set of default focus traversal keys");
/*      */     Set localSet;
/* 1072 */     synchronized (this) {
/* 1073 */       for (Iterator localIterator = paramSet.iterator(); localIterator.hasNext(); ) {
/* 1074 */         Object localObject1 = localIterator.next();
/*      */ 
/* 1076 */         if (localObject1 == null) {
/* 1077 */           throw new IllegalArgumentException("cannot set null focus traversal key");
/*      */         }
/*      */ 
/* 1082 */         if (!(localObject1 instanceof AWTKeyStroke)) {
/* 1083 */           throw new IllegalArgumentException("object is expected to be AWTKeyStroke");
/*      */         }
/* 1085 */         AWTKeyStroke localAWTKeyStroke = (AWTKeyStroke)localObject1;
/*      */ 
/* 1087 */         if (localAWTKeyStroke.getKeyChar() != 65535) {
/* 1088 */           throw new IllegalArgumentException("focus traversal keys cannot map to KEY_TYPED events");
/*      */         }
/*      */ 
/* 1093 */         for (int i = 0; i < 4; i++) {
/* 1094 */           if (i != paramInt)
/*      */           {
/* 1098 */             if (this.defaultFocusTraversalKeys[i].contains(localAWTKeyStroke)) {
/* 1099 */               throw new IllegalArgumentException("focus traversal keys must be unique for a Component");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1104 */       localSet = this.defaultFocusTraversalKeys[paramInt];
/* 1105 */       this.defaultFocusTraversalKeys[paramInt] = Collections.unmodifiableSet(new HashSet(paramSet));
/*      */     }
/*      */ 
/* 1109 */     firePropertyChange(defaultFocusTraversalKeyPropertyNames[paramInt], localSet, paramSet);
/*      */   }
/*      */ 
/*      */   public Set<AWTKeyStroke> getDefaultFocusTraversalKeys(int paramInt)
/*      */   {
/* 1140 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 1141 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/*      */ 
/* 1145 */     return this.defaultFocusTraversalKeys[paramInt];
/*      */   }
/*      */ 
/*      */   public Container getCurrentFocusCycleRoot()
/*      */   {
/* 1165 */     synchronized (KeyboardFocusManager.class) {
/* 1166 */       if (currentFocusCycleRoot == null) {
/* 1167 */         return null;
/*      */       }
/*      */ 
/* 1170 */       return currentFocusCycleRoot.appContext == AppContext.getAppContext() ? currentFocusCycleRoot : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Container getGlobalCurrentFocusCycleRoot()
/*      */     throws SecurityException
/*      */   {
/* 1199 */     synchronized (KeyboardFocusManager.class) {
/* 1200 */       checkKFMSecurity();
/* 1201 */       return currentFocusCycleRoot;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setGlobalCurrentFocusCycleRoot(Container paramContainer)
/*      */   {
/*      */     Container localContainer;
/* 1224 */     synchronized (KeyboardFocusManager.class) {
/* 1225 */       checkKFMSecurity();
/*      */ 
/* 1227 */       localContainer = getCurrentFocusCycleRoot();
/* 1228 */       currentFocusCycleRoot = paramContainer;
/*      */     }
/*      */ 
/* 1231 */     firePropertyChange("currentFocusCycleRoot", localContainer, paramContainer);
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1267 */     if (paramPropertyChangeListener != null)
/* 1268 */       synchronized (this) {
/* 1269 */         if (this.changeSupport == null) {
/* 1270 */           this.changeSupport = new PropertyChangeSupport(this);
/*      */         }
/* 1272 */         this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1290 */     if (paramPropertyChangeListener != null)
/* 1291 */       synchronized (this) {
/* 1292 */         if (this.changeSupport != null)
/* 1293 */           this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*      */   {
/* 1314 */     if (this.changeSupport == null) {
/* 1315 */       this.changeSupport = new PropertyChangeSupport(this);
/*      */     }
/* 1317 */     return this.changeSupport.getPropertyChangeListeners();
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1354 */     if (paramPropertyChangeListener != null)
/* 1355 */       synchronized (this) {
/* 1356 */         if (this.changeSupport == null) {
/* 1357 */           this.changeSupport = new PropertyChangeSupport(this);
/*      */         }
/* 1359 */         this.changeSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1380 */     if (paramPropertyChangeListener != null)
/* 1381 */       synchronized (this) {
/* 1382 */         if (this.changeSupport != null)
/* 1383 */           this.changeSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public synchronized PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*      */   {
/* 1403 */     if (this.changeSupport == null) {
/* 1404 */       this.changeSupport = new PropertyChangeSupport(this);
/*      */     }
/* 1406 */     return this.changeSupport.getPropertyChangeListeners(paramString);
/*      */   }
/*      */ 
/*      */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/* 1421 */     if (paramObject1 == paramObject2) {
/* 1422 */       return;
/*      */     }
/* 1424 */     PropertyChangeSupport localPropertyChangeSupport = this.changeSupport;
/* 1425 */     if (localPropertyChangeSupport != null)
/* 1426 */       localPropertyChangeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public void addVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 1448 */     if (paramVetoableChangeListener != null)
/* 1449 */       synchronized (this) {
/* 1450 */         if (this.vetoableSupport == null) {
/* 1451 */           this.vetoableSupport = new VetoableChangeSupport(this);
/*      */         }
/*      */ 
/* 1454 */         this.vetoableSupport.addVetoableChangeListener(paramVetoableChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removeVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 1472 */     if (paramVetoableChangeListener != null)
/* 1473 */       synchronized (this) {
/* 1474 */         if (this.vetoableSupport != null)
/* 1475 */           this.vetoableSupport.removeVetoableChangeListener(paramVetoableChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public synchronized VetoableChangeListener[] getVetoableChangeListeners()
/*      */   {
/* 1496 */     if (this.vetoableSupport == null) {
/* 1497 */       this.vetoableSupport = new VetoableChangeSupport(this);
/*      */     }
/* 1499 */     return this.vetoableSupport.getVetoableChangeListeners();
/*      */   }
/*      */ 
/*      */   public void addVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 1522 */     if (paramVetoableChangeListener != null)
/* 1523 */       synchronized (this) {
/* 1524 */         if (this.vetoableSupport == null) {
/* 1525 */           this.vetoableSupport = new VetoableChangeSupport(this);
/*      */         }
/*      */ 
/* 1528 */         this.vetoableSupport.addVetoableChangeListener(paramString, paramVetoableChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removeVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 1549 */     if (paramVetoableChangeListener != null)
/* 1550 */       synchronized (this) {
/* 1551 */         if (this.vetoableSupport != null)
/* 1552 */           this.vetoableSupport.removeVetoableChangeListener(paramString, paramVetoableChangeListener);
/*      */       }
/*      */   }
/*      */ 
/*      */   public synchronized VetoableChangeListener[] getVetoableChangeListeners(String paramString)
/*      */   {
/* 1573 */     if (this.vetoableSupport == null) {
/* 1574 */       this.vetoableSupport = new VetoableChangeSupport(this);
/*      */     }
/* 1576 */     return this.vetoableSupport.getVetoableChangeListeners(paramString);
/*      */   }
/*      */ 
/*      */   protected void fireVetoableChange(String paramString, Object paramObject1, Object paramObject2)
/*      */     throws PropertyVetoException
/*      */   {
/* 1599 */     if (paramObject1 == paramObject2) {
/* 1600 */       return;
/*      */     }
/* 1602 */     VetoableChangeSupport localVetoableChangeSupport = this.vetoableSupport;
/*      */ 
/* 1604 */     if (localVetoableChangeSupport != null)
/* 1605 */       localVetoableChangeSupport.fireVetoableChange(paramString, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public void addKeyEventDispatcher(KeyEventDispatcher paramKeyEventDispatcher)
/*      */   {
/* 1633 */     if (paramKeyEventDispatcher != null)
/* 1634 */       synchronized (this) {
/* 1635 */         if (this.keyEventDispatchers == null) {
/* 1636 */           this.keyEventDispatchers = new LinkedList();
/*      */         }
/* 1638 */         this.keyEventDispatchers.add(paramKeyEventDispatcher);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removeKeyEventDispatcher(KeyEventDispatcher paramKeyEventDispatcher)
/*      */   {
/* 1664 */     if (paramKeyEventDispatcher != null)
/* 1665 */       synchronized (this) {
/* 1666 */         if (this.keyEventDispatchers != null)
/* 1667 */           this.keyEventDispatchers.remove(paramKeyEventDispatcher);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected synchronized List<KeyEventDispatcher> getKeyEventDispatchers()
/*      */   {
/* 1689 */     return this.keyEventDispatchers != null ? (List)this.keyEventDispatchers.clone() : null;
/*      */   }
/*      */ 
/*      */   public void addKeyEventPostProcessor(KeyEventPostProcessor paramKeyEventPostProcessor)
/*      */   {
/* 1721 */     if (paramKeyEventPostProcessor != null)
/* 1722 */       synchronized (this) {
/* 1723 */         if (this.keyEventPostProcessors == null) {
/* 1724 */           this.keyEventPostProcessors = new LinkedList();
/*      */         }
/* 1726 */         this.keyEventPostProcessors.add(paramKeyEventPostProcessor);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removeKeyEventPostProcessor(KeyEventPostProcessor paramKeyEventPostProcessor)
/*      */   {
/* 1754 */     if (paramKeyEventPostProcessor != null)
/* 1755 */       synchronized (this) {
/* 1756 */         if (this.keyEventPostProcessors != null)
/* 1757 */           this.keyEventPostProcessors.remove(paramKeyEventPostProcessor);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected List<KeyEventPostProcessor> getKeyEventPostProcessors()
/*      */   {
/* 1780 */     return this.keyEventPostProcessors != null ? (List)this.keyEventPostProcessors.clone() : null;
/*      */   }
/*      */ 
/*      */   static void setMostRecentFocusOwner(Component paramComponent)
/*      */   {
/* 1788 */     Object localObject = paramComponent;
/* 1789 */     while ((localObject != null) && (!(localObject instanceof Window))) {
/* 1790 */       localObject = ((Component)localObject).parent;
/*      */     }
/* 1792 */     if (localObject != null)
/* 1793 */       setMostRecentFocusOwner((Window)localObject, paramComponent);
/*      */   }
/*      */ 
/*      */   static synchronized void setMostRecentFocusOwner(Window paramWindow, Component paramComponent)
/*      */   {
/* 1802 */     WeakReference localWeakReference = null;
/* 1803 */     if (paramComponent != null) {
/* 1804 */       localWeakReference = new WeakReference(paramComponent);
/*      */     }
/* 1806 */     mostRecentFocusOwners.put(paramWindow, localWeakReference);
/*      */   }
/*      */ 
/*      */   static void clearMostRecentFocusOwner(Component paramComponent)
/*      */   {
/* 1811 */     if (paramComponent == null)
/*      */       return;
/*      */     Container localContainer;
/* 1815 */     synchronized (paramComponent.getTreeLock()) {
/* 1816 */       localContainer = paramComponent.getParent();
/* 1817 */       while ((localContainer != null) && (!(localContainer instanceof Window))) {
/* 1818 */         localContainer = localContainer.getParent();
/*      */       }
/*      */     }
/*      */ 
/* 1822 */     synchronized (KeyboardFocusManager.class) {
/* 1823 */       if ((localContainer != null) && (getMostRecentFocusOwner((Window)localContainer) == paramComponent))
/*      */       {
/* 1826 */         setMostRecentFocusOwner((Window)localContainer, null);
/*      */       }
/*      */ 
/* 1829 */       if (localContainer != null) {
/* 1830 */         Window localWindow = (Window)localContainer;
/* 1831 */         if (localWindow.getTemporaryLostComponent() == paramComponent)
/* 1832 */           localWindow.setTemporaryLostComponent(null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static synchronized Component getMostRecentFocusOwner(Window paramWindow)
/*      */   {
/* 1843 */     WeakReference localWeakReference = (WeakReference)mostRecentFocusOwners.get(paramWindow);
/*      */ 
/* 1845 */     return localWeakReference == null ? null : (Component)localWeakReference.get();
/*      */   }
/*      */ 
/*      */   public abstract boolean dispatchEvent(AWTEvent paramAWTEvent);
/*      */ 
/*      */   public final void redispatchEvent(Component paramComponent, AWTEvent paramAWTEvent)
/*      */   {
/* 1886 */     paramAWTEvent.focusManagerIsDispatching = true;
/* 1887 */     paramComponent.dispatchEvent(paramAWTEvent);
/* 1888 */     paramAWTEvent.focusManagerIsDispatching = false;
/*      */   }
/*      */ 
/*      */   public abstract boolean dispatchKeyEvent(KeyEvent paramKeyEvent);
/*      */ 
/*      */   public abstract boolean postProcessKeyEvent(KeyEvent paramKeyEvent);
/*      */ 
/*      */   public abstract void processKeyEvent(Component paramComponent, KeyEvent paramKeyEvent);
/*      */ 
/*      */   protected abstract void enqueueKeyEvents(long paramLong, Component paramComponent);
/*      */ 
/*      */   protected abstract void dequeueKeyEvents(long paramLong, Component paramComponent);
/*      */ 
/*      */   protected abstract void discardKeyEvents(Component paramComponent);
/*      */ 
/*      */   public abstract void focusNextComponent(Component paramComponent);
/*      */ 
/*      */   public abstract void focusPreviousComponent(Component paramComponent);
/*      */ 
/*      */   public abstract void upFocusCycle(Component paramComponent);
/*      */ 
/*      */   public abstract void downFocusCycle(Container paramContainer);
/*      */ 
/*      */   public final void focusNextComponent()
/*      */   {
/* 2042 */     Component localComponent = getFocusOwner();
/* 2043 */     if (localComponent != null)
/* 2044 */       focusNextComponent(localComponent);
/*      */   }
/*      */ 
/*      */   public final void focusPreviousComponent()
/*      */   {
/* 2052 */     Component localComponent = getFocusOwner();
/* 2053 */     if (localComponent != null)
/* 2054 */       focusPreviousComponent(localComponent);
/*      */   }
/*      */ 
/*      */   public final void upFocusCycle()
/*      */   {
/* 2068 */     Component localComponent = getFocusOwner();
/* 2069 */     if (localComponent != null)
/* 2070 */       upFocusCycle(localComponent);
/*      */   }
/*      */ 
/*      */   public final void downFocusCycle()
/*      */   {
/* 2084 */     Component localComponent = getFocusOwner();
/* 2085 */     if ((localComponent instanceof Container))
/* 2086 */       downFocusCycle((Container)localComponent);
/*      */   }
/*      */ 
/*      */   void dumpRequests()
/*      */   {
/* 2094 */     System.err.println(">>> Requests dump, time: " + System.currentTimeMillis());
/* 2095 */     synchronized (heavyweightRequests) {
/* 2096 */       for (HeavyweightFocusRequest localHeavyweightFocusRequest : heavyweightRequests) {
/* 2097 */         System.err.println(">>> Req: " + localHeavyweightFocusRequest);
/*      */       }
/*      */     }
/* 2100 */     System.err.println("");
/*      */   }
/*      */ 
/*      */   static boolean processSynchronousLightweightTransfer(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
/*      */   {
/* 2219 */     Window localWindow = SunToolkit.getContainingWindow(paramComponent1);
/* 2220 */     if ((localWindow == null) || (!localWindow.syncLWRequests)) {
/* 2221 */       return false;
/*      */     }
/* 2223 */     if (paramComponent2 == null)
/*      */     {
/* 2227 */       paramComponent2 = paramComponent1;
/*      */     }
/*      */ 
/* 2230 */     KeyboardFocusManager localKeyboardFocusManager = getCurrentKeyboardFocusManager(SunToolkit.targetToAppContext(paramComponent2));
/*      */ 
/* 2232 */     FocusEvent localFocusEvent1 = null;
/* 2233 */     FocusEvent localFocusEvent2 = null;
/* 2234 */     Component localComponent = localKeyboardFocusManager.getGlobalFocusOwner();
/*      */ 
/* 2236 */     synchronized (heavyweightRequests) {
/* 2237 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getLastHWRequest();
/* 2238 */       if ((localHeavyweightFocusRequest == null) && (paramComponent1 == localKeyboardFocusManager.getNativeFocusOwner()) && (allowSyncFocusRequests))
/*      */       {
/* 2243 */         if (paramComponent2 == localComponent)
/*      */         {
/* 2245 */           return true;
/*      */         }
/*      */ 
/* 2252 */         localKeyboardFocusManager.enqueueKeyEvents(paramLong, paramComponent2);
/*      */ 
/* 2254 */         localHeavyweightFocusRequest = new HeavyweightFocusRequest(paramComponent1, paramComponent2, paramBoolean1, CausedFocusEvent.Cause.UNKNOWN);
/*      */ 
/* 2257 */         heavyweightRequests.add(localHeavyweightFocusRequest);
/*      */ 
/* 2259 */         if (localComponent != null) {
/* 2260 */           localFocusEvent1 = new FocusEvent(localComponent, 1005, paramBoolean1, paramComponent2);
/*      */         }
/*      */ 
/* 2265 */         localFocusEvent2 = new FocusEvent(paramComponent2, 1004, paramBoolean1, localComponent);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2270 */     boolean bool1 = false;
/* 2271 */     boolean bool2 = clearingCurrentLightweightRequests;
/*      */ 
/* 2273 */     Throwable localThrowable = null;
/*      */     try {
/* 2275 */       clearingCurrentLightweightRequests = false;
/* 2276 */       synchronized (Component.LOCK)
/*      */       {
/* 2278 */         if ((localFocusEvent1 != null) && (localComponent != null)) {
/* 2279 */           localFocusEvent1.isPosted = true;
/* 2280 */           localThrowable = dispatchAndCatchException(localThrowable, localComponent, localFocusEvent1);
/* 2281 */           bool1 = true;
/*      */         }
/*      */ 
/* 2284 */         if ((localFocusEvent2 != null) && (paramComponent2 != null)) {
/* 2285 */           localFocusEvent2.isPosted = true;
/* 2286 */           localThrowable = dispatchAndCatchException(localThrowable, paramComponent2, localFocusEvent2);
/* 2287 */           bool1 = true;
/*      */         }
/*      */       }
/*      */     } finally {
/* 2291 */       clearingCurrentLightweightRequests = bool2;
/*      */     }
/* 2293 */     if ((localThrowable instanceof RuntimeException))
/* 2294 */       throw ((RuntimeException)localThrowable);
/* 2295 */     if ((localThrowable instanceof Error)) {
/* 2296 */       throw ((Error)localThrowable);
/*      */     }
/* 2298 */     return bool1;
/*      */   }
/*      */ 
/*      */   static int shouldNativelyFocusHeavyweight(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause)
/*      */   {
/* 2323 */     if (log.isLoggable(500)) {
/* 2324 */       if (paramComponent1 == null) {
/* 2325 */         log.fine("Assertion (heavyweight != null) failed");
/*      */       }
/* 2327 */       if (paramLong == 0L) {
/* 2328 */         log.fine("Assertion (time != 0) failed");
/*      */       }
/*      */     }
/*      */ 
/* 2332 */     if (paramComponent2 == null)
/*      */     {
/* 2336 */       paramComponent2 = paramComponent1;
/*      */     }
/*      */ 
/* 2339 */     KeyboardFocusManager localKeyboardFocusManager1 = getCurrentKeyboardFocusManager(SunToolkit.targetToAppContext(paramComponent2));
/*      */ 
/* 2341 */     KeyboardFocusManager localKeyboardFocusManager2 = getCurrentKeyboardFocusManager();
/* 2342 */     Component localComponent1 = localKeyboardFocusManager2.getGlobalFocusOwner();
/* 2343 */     Component localComponent2 = localKeyboardFocusManager2.getNativeFocusOwner();
/* 2344 */     Window localWindow = localKeyboardFocusManager2.getNativeFocusedWindow();
/* 2345 */     if (focusLog.isLoggable(400)) {
/* 2346 */       focusLog.finer("SNFH for {0} in {1}", new Object[] { String.valueOf(paramComponent2), String.valueOf(paramComponent1) });
/*      */     }
/*      */ 
/* 2349 */     if (focusLog.isLoggable(300)) {
/* 2350 */       focusLog.finest("0. Current focus owner {0}", new Object[] { String.valueOf(localComponent1) });
/*      */ 
/* 2352 */       focusLog.finest("0. Native focus owner {0}", new Object[] { String.valueOf(localComponent2) });
/*      */ 
/* 2354 */       focusLog.finest("0. Native focused window {0}", new Object[] { String.valueOf(localWindow) });
/*      */     }
/*      */ 
/* 2357 */     synchronized (heavyweightRequests) {
/* 2358 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getLastHWRequest();
/* 2359 */       if (focusLog.isLoggable(300)) {
/* 2360 */         focusLog.finest("Request {0}", new Object[] { String.valueOf(localHeavyweightFocusRequest) });
/*      */       }
/* 2362 */       if ((localHeavyweightFocusRequest == null) && (paramComponent1 == localComponent2) && (paramComponent1.getContainingWindow() == localWindow))
/*      */       {
/* 2366 */         if (paramComponent2 == localComponent1)
/*      */         {
/* 2368 */           if (focusLog.isLoggable(300)) {
/* 2369 */             focusLog.finest("1. SNFH_FAILURE for {0}", new Object[] { String.valueOf(paramComponent2) });
/*      */           }
/* 2371 */           return 0;
/*      */         }
/*      */ 
/* 2378 */         localKeyboardFocusManager1.enqueueKeyEvents(paramLong, paramComponent2);
/*      */ 
/* 2380 */         localHeavyweightFocusRequest = new HeavyweightFocusRequest(paramComponent1, paramComponent2, paramBoolean1, paramCause);
/*      */ 
/* 2383 */         heavyweightRequests.add(localHeavyweightFocusRequest);
/*      */ 
/* 2385 */         if (localComponent1 != null) {
/* 2386 */           localCausedFocusEvent = new CausedFocusEvent(localComponent1, 1005, paramBoolean1, paramComponent2, paramCause);
/*      */ 
/* 2392 */           SunToolkit.postEvent(localComponent1.appContext, localCausedFocusEvent);
/*      */         }
/*      */ 
/* 2395 */         CausedFocusEvent localCausedFocusEvent = new CausedFocusEvent(paramComponent2, 1004, paramBoolean1, localComponent1, paramCause);
/*      */ 
/* 2400 */         SunToolkit.postEvent(paramComponent2.appContext, localCausedFocusEvent);
/*      */ 
/* 2402 */         if (focusLog.isLoggable(300))
/* 2403 */           focusLog.finest("2. SNFH_HANDLED for {0}", new Object[] { String.valueOf(paramComponent2) });
/* 2404 */         return 1;
/* 2405 */       }if ((localHeavyweightFocusRequest != null) && (localHeavyweightFocusRequest.heavyweight == paramComponent1))
/*      */       {
/* 2411 */         if (localHeavyweightFocusRequest.addLightweightRequest(paramComponent2, paramBoolean1, paramCause))
/*      */         {
/* 2413 */           localKeyboardFocusManager1.enqueueKeyEvents(paramLong, paramComponent2);
/*      */         }
/*      */ 
/* 2416 */         if (focusLog.isLoggable(300)) {
/* 2417 */           focusLog.finest("3. SNFH_HANDLED for lightweight" + paramComponent2 + " in " + paramComponent1);
/*      */         }
/* 2419 */         return 1;
/*      */       }
/* 2421 */       if (!paramBoolean2)
/*      */       {
/* 2427 */         if (localHeavyweightFocusRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER)
/*      */         {
/* 2430 */           int i = heavyweightRequests.size();
/* 2431 */           localHeavyweightFocusRequest = i >= 2 ? (HeavyweightFocusRequest)heavyweightRequests.get(i - 2) : null;
/*      */         }
/*      */ 
/* 2435 */         if (focusedWindowChanged(paramComponent1, localHeavyweightFocusRequest != null ? localHeavyweightFocusRequest.heavyweight : localWindow))
/*      */         {
/* 2439 */           if (focusLog.isLoggable(300))
/* 2440 */             focusLog.finest("4. SNFH_FAILURE for " + paramComponent2);
/* 2441 */           return 0;
/*      */         }
/*      */       }
/*      */ 
/* 2445 */       localKeyboardFocusManager1.enqueueKeyEvents(paramLong, paramComponent2);
/* 2446 */       heavyweightRequests.add(new HeavyweightFocusRequest(paramComponent1, paramComponent2, paramBoolean1, paramCause));
/*      */ 
/* 2449 */       if (focusLog.isLoggable(300))
/* 2450 */         focusLog.finest("5. SNFH_PROCEED for " + paramComponent2);
/* 2451 */       return 2;
/*      */     }
/*      */   }
/*      */ 
/*      */   static Window markClearGlobalFocusOwner()
/*      */   {
/* 2466 */     Window localWindow = getCurrentKeyboardFocusManager().getNativeFocusedWindow();
/*      */ 
/* 2469 */     synchronized (heavyweightRequests) {
/* 2470 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getLastHWRequest();
/* 2471 */       if (localHeavyweightFocusRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER)
/*      */       {
/* 2475 */         return null;
/*      */       }
/*      */ 
/* 2478 */       heavyweightRequests.add(HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER);
/*      */ 
/* 2481 */       Object localObject1 = localHeavyweightFocusRequest != null ? SunToolkit.getContainingWindow(localHeavyweightFocusRequest.heavyweight) : localWindow;
/*      */ 
/* 2484 */       while ((localObject1 != null) && (!(localObject1 instanceof Frame)) && (!(localObject1 instanceof Dialog)))
/*      */       {
/* 2488 */         localObject1 = ((Component)localObject1).getParent_NoClientCode();
/*      */       }
/*      */ 
/* 2491 */       return (Window)localObject1;
/*      */     }
/*      */   }
/*      */ 
/* 2495 */   Component getCurrentWaitingRequest(Component paramComponent) { synchronized (heavyweightRequests) {
/* 2496 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getFirstHWRequest();
/* 2497 */       if ((localHeavyweightFocusRequest != null) && 
/* 2498 */         (localHeavyweightFocusRequest.heavyweight == paramComponent)) {
/* 2499 */         LightweightFocusRequest localLightweightFocusRequest = (LightweightFocusRequest)localHeavyweightFocusRequest.lightweightRequests.getFirst();
/*      */ 
/* 2501 */         if (localLightweightFocusRequest != null) {
/* 2502 */           return localLightweightFocusRequest.component;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2507 */     return null; }
/*      */ 
/*      */   static boolean isAutoFocusTransferEnabled()
/*      */   {
/* 2511 */     synchronized (heavyweightRequests) {
/* 2512 */       return (heavyweightRequests.size() == 0) && (!disableRestoreFocus) && (null == currentLightweightRequests);
/*      */     }
/*      */   }
/*      */ 
/*      */   static boolean isAutoFocusTransferEnabledFor(Component paramComponent)
/*      */   {
/* 2519 */     return (isAutoFocusTransferEnabled()) && (paramComponent.isAutoFocusTransferOnDisposal());
/*      */   }
/*      */ 
/*      */   private static Throwable dispatchAndCatchException(Throwable paramThrowable, Component paramComponent, FocusEvent paramFocusEvent)
/*      */   {
/* 2529 */     Object localObject = null;
/*      */     try {
/* 2531 */       paramComponent.dispatchEvent(paramFocusEvent);
/*      */     } catch (RuntimeException localRuntimeException) {
/* 2533 */       localObject = localRuntimeException;
/*      */     } catch (Error localError) {
/* 2535 */       localObject = localError;
/*      */     }
/* 2537 */     if (localObject != null) {
/* 2538 */       if (paramThrowable != null) {
/* 2539 */         handleException(paramThrowable);
/*      */       }
/* 2541 */       return localObject;
/*      */     }
/* 2543 */     return paramThrowable;
/*      */   }
/*      */ 
/*      */   private static void handleException(Throwable paramThrowable) {
/* 2547 */     paramThrowable.printStackTrace();
/*      */   }
/*      */ 
/*      */   static void processCurrentLightweightRequests() {
/* 2551 */     KeyboardFocusManager localKeyboardFocusManager = getCurrentKeyboardFocusManager();
/* 2552 */     LinkedList localLinkedList = null;
/*      */ 
/* 2554 */     Component localComponent1 = localKeyboardFocusManager.getGlobalFocusOwner();
/* 2555 */     if ((localComponent1 != null) && (localComponent1.appContext != AppContext.getAppContext()))
/*      */     {
/* 2561 */       return;
/*      */     }
/*      */ 
/* 2564 */     synchronized (heavyweightRequests) {
/* 2565 */       if (currentLightweightRequests != null) {
/* 2566 */         clearingCurrentLightweightRequests = true;
/* 2567 */         disableRestoreFocus = true;
/* 2568 */         localLinkedList = currentLightweightRequests;
/* 2569 */         allowSyncFocusRequests = localLinkedList.size() < 2;
/* 2570 */         currentLightweightRequests = null;
/*      */       }
/*      */       else {
/* 2573 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 2577 */     ??? = null;
/*      */     try {
/* 2579 */       if (localLinkedList != null) {
/* 2580 */         localComponent2 = null;
/* 2581 */         localComponent3 = null;
/*      */ 
/* 2583 */         for (localIterator = localLinkedList.iterator(); localIterator.hasNext(); )
/*      */         {
/* 2585 */           localComponent3 = localKeyboardFocusManager.getGlobalFocusOwner();
/* 2586 */           LightweightFocusRequest localLightweightFocusRequest = (LightweightFocusRequest)localIterator.next();
/*      */ 
/* 2598 */           if (!localIterator.hasNext()) {
/* 2599 */             disableRestoreFocus = false;
/*      */           }
/*      */ 
/* 2602 */           CausedFocusEvent localCausedFocusEvent1 = null;
/*      */ 
/* 2608 */           if (localComponent3 != null) {
/* 2609 */             localCausedFocusEvent1 = new CausedFocusEvent(localComponent3, 1005, localLightweightFocusRequest.temporary, localLightweightFocusRequest.component, localLightweightFocusRequest.cause);
/*      */           }
/*      */ 
/* 2614 */           CausedFocusEvent localCausedFocusEvent2 = new CausedFocusEvent(localLightweightFocusRequest.component, 1004, localLightweightFocusRequest.temporary, localComponent3 == null ? localComponent2 : localComponent3, localLightweightFocusRequest.cause);
/*      */ 
/* 2621 */           if (localComponent3 != null) {
/* 2622 */             localCausedFocusEvent1.isPosted = true;
/* 2623 */             ??? = dispatchAndCatchException((Throwable)???, localComponent3, localCausedFocusEvent1);
/*      */           }
/*      */ 
/* 2626 */           localCausedFocusEvent2.isPosted = true;
/* 2627 */           ??? = dispatchAndCatchException((Throwable)???, localLightweightFocusRequest.component, localCausedFocusEvent2);
/*      */ 
/* 2629 */           if (localKeyboardFocusManager.getGlobalFocusOwner() == localLightweightFocusRequest.component)
/* 2630 */             localComponent2 = localLightweightFocusRequest.component;
/*      */         }
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*      */       Component localComponent2;
/*      */       Component localComponent3;
/*      */       Iterator localIterator;
/* 2635 */       clearingCurrentLightweightRequests = false;
/* 2636 */       disableRestoreFocus = false;
/* 2637 */       localLinkedList = null;
/* 2638 */       allowSyncFocusRequests = true;
/*      */     }
/* 2640 */     if ((??? instanceof RuntimeException))
/* 2641 */       throw ((RuntimeException)???);
/* 2642 */     if ((??? instanceof Error))
/* 2643 */       throw ((Error)???);
/*      */   }
/*      */ 
/*      */   static FocusEvent retargetUnexpectedFocusEvent(FocusEvent paramFocusEvent)
/*      */   {
/* 2648 */     synchronized (heavyweightRequests)
/*      */     {
/* 2653 */       if (removeFirstRequest()) {
/* 2654 */         return (FocusEvent)retargetFocusEvent(paramFocusEvent);
/*      */       }
/*      */ 
/* 2657 */       Component localComponent1 = paramFocusEvent.getComponent();
/* 2658 */       Component localComponent2 = paramFocusEvent.getOppositeComponent();
/* 2659 */       boolean bool = false;
/* 2660 */       if ((paramFocusEvent.getID() == 1005) && ((localComponent2 == null) || (isTemporary(localComponent2, localComponent1))))
/*      */       {
/* 2663 */         bool = true;
/*      */       }
/* 2665 */       return new CausedFocusEvent(localComponent1, paramFocusEvent.getID(), bool, localComponent2, CausedFocusEvent.Cause.NATIVE_SYSTEM);
/*      */     }
/*      */   }
/*      */ 
/*      */   static FocusEvent retargetFocusGained(FocusEvent paramFocusEvent)
/*      */   {
/* 2671 */     assert (paramFocusEvent.getID() == 1004);
/*      */ 
/* 2673 */     Component localComponent1 = getCurrentKeyboardFocusManager().getGlobalFocusOwner();
/*      */ 
/* 2675 */     Component localComponent2 = paramFocusEvent.getComponent();
/* 2676 */     Component localComponent3 = paramFocusEvent.getOppositeComponent();
/* 2677 */     Component localComponent4 = getHeavyweight(localComponent2);
/*      */ 
/* 2679 */     synchronized (heavyweightRequests) {
/* 2680 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getFirstHWRequest();
/*      */ 
/* 2682 */       if (localHeavyweightFocusRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER)
/*      */       {
/* 2684 */         return retargetUnexpectedFocusEvent(paramFocusEvent);
/*      */       }
/*      */ 
/* 2687 */       if ((localComponent2 != null) && (localComponent4 == null) && (localHeavyweightFocusRequest != null))
/*      */       {
/* 2691 */         if (localComponent2 == localHeavyweightFocusRequest.getFirstLightweightRequest().component)
/*      */         {
/* 2693 */           localComponent2 = localHeavyweightFocusRequest.heavyweight;
/* 2694 */           localComponent4 = localComponent2;
/*      */         }
/*      */       }
/* 2697 */       if ((localHeavyweightFocusRequest != null) && (localComponent4 == localHeavyweightFocusRequest.heavyweight))
/*      */       {
/* 2703 */         heavyweightRequests.removeFirst();
/*      */ 
/* 2705 */         LightweightFocusRequest localLightweightFocusRequest = (LightweightFocusRequest)localHeavyweightFocusRequest.lightweightRequests.removeFirst();
/*      */ 
/* 2708 */         Component localComponent5 = localLightweightFocusRequest.component;
/* 2709 */         if (localComponent1 != null)
/*      */         {
/* 2721 */           newFocusOwner = localComponent5;
/*      */         }
/*      */ 
/* 2724 */         boolean bool = (localComponent3 == null) || (isTemporary(localComponent5, localComponent3)) ? false : localLightweightFocusRequest.temporary;
/*      */ 
/* 2729 */         if (localHeavyweightFocusRequest.lightweightRequests.size() > 0) {
/* 2730 */           currentLightweightRequests = localHeavyweightFocusRequest.lightweightRequests;
/*      */ 
/* 2732 */           EventQueue.invokeLater(new Runnable() {
/*      */             public void run() {
/* 2734 */               KeyboardFocusManager.processCurrentLightweightRequests();
/*      */             }
/*      */ 
/*      */           });
/*      */         }
/*      */ 
/* 2741 */         return new CausedFocusEvent(localComponent5, 1004, bool, localComponent3, localLightweightFocusRequest.cause);
/*      */       }
/*      */ 
/* 2746 */       if ((localComponent1 != null) && (localComponent1.getContainingWindow() == localComponent2) && ((localHeavyweightFocusRequest == null) || (localComponent2 != localHeavyweightFocusRequest.heavyweight)))
/*      */       {
/* 2754 */         return new CausedFocusEvent(localComponent1, 1004, false, null, CausedFocusEvent.Cause.ACTIVATION);
/*      */       }
/*      */ 
/* 2758 */       return retargetUnexpectedFocusEvent(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static FocusEvent retargetFocusLost(FocusEvent paramFocusEvent) {
/* 2763 */     assert (paramFocusEvent.getID() == 1005);
/*      */ 
/* 2765 */     Component localComponent1 = getCurrentKeyboardFocusManager().getGlobalFocusOwner();
/*      */ 
/* 2767 */     Component localComponent2 = paramFocusEvent.getOppositeComponent();
/* 2768 */     Component localComponent3 = getHeavyweight(localComponent2);
/*      */ 
/* 2770 */     synchronized (heavyweightRequests) {
/* 2771 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getFirstHWRequest();
/*      */ 
/* 2773 */       if (localHeavyweightFocusRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER)
/*      */       {
/* 2775 */         if (localComponent1 != null)
/*      */         {
/* 2777 */           heavyweightRequests.removeFirst();
/* 2778 */           return new CausedFocusEvent(localComponent1, 1005, false, null, CausedFocusEvent.Cause.CLEAR_GLOBAL_FOCUS_OWNER);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2785 */         if (localComponent2 == null)
/*      */         {
/* 2788 */           if (localComponent1 != null) {
/* 2789 */             return new CausedFocusEvent(localComponent1, 1005, true, null, CausedFocusEvent.Cause.ACTIVATION);
/*      */           }
/*      */ 
/* 2793 */           return paramFocusEvent;
/*      */         }
/* 2795 */         if ((localHeavyweightFocusRequest != null) && ((localComponent3 == localHeavyweightFocusRequest.heavyweight) || ((localComponent3 == null) && (localComponent2 == localHeavyweightFocusRequest.getFirstLightweightRequest().component))))
/*      */         {
/* 2800 */           if (localComponent1 == null) {
/* 2801 */             return paramFocusEvent;
/*      */           }
/*      */ 
/* 2810 */           LightweightFocusRequest localLightweightFocusRequest = (LightweightFocusRequest)localHeavyweightFocusRequest.lightweightRequests.getFirst();
/*      */ 
/* 2813 */           boolean bool = isTemporary(localComponent2, localComponent1) ? true : localLightweightFocusRequest.temporary;
/*      */ 
/* 2817 */           return new CausedFocusEvent(localComponent1, 1005, bool, localLightweightFocusRequest.component, localLightweightFocusRequest.cause);
/*      */         }
/* 2819 */         if (focusedWindowChanged(localComponent2, localComponent1))
/*      */         {
/* 2822 */           if ((!paramFocusEvent.isTemporary()) && (localComponent1 != null))
/*      */           {
/* 2824 */             paramFocusEvent = new CausedFocusEvent(localComponent1, 1005, true, localComponent2, CausedFocusEvent.Cause.ACTIVATION);
/*      */           }
/*      */ 
/* 2827 */           return paramFocusEvent;
/*      */         }
/*      */       }
/* 2830 */       return retargetUnexpectedFocusEvent(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static AWTEvent retargetFocusEvent(AWTEvent paramAWTEvent) {
/* 2835 */     if (clearingCurrentLightweightRequests) {
/* 2836 */       return paramAWTEvent;
/*      */     }
/*      */ 
/* 2839 */     KeyboardFocusManager localKeyboardFocusManager = getCurrentKeyboardFocusManager();
/* 2840 */     if (focusLog.isLoggable(400)) {
/* 2841 */       if (((paramAWTEvent instanceof FocusEvent)) || ((paramAWTEvent instanceof WindowEvent))) {
/* 2842 */         focusLog.finer(">>> {0}", new Object[] { String.valueOf(paramAWTEvent) });
/*      */       }
/* 2844 */       if ((focusLog.isLoggable(400)) && ((paramAWTEvent instanceof KeyEvent))) {
/* 2845 */         focusLog.finer("    focus owner is {0}", new Object[] { String.valueOf(localKeyboardFocusManager.getGlobalFocusOwner()) });
/*      */ 
/* 2847 */         focusLog.finer(">>> {0}", new Object[] { String.valueOf(paramAWTEvent) });
/*      */       }
/*      */     }
/*      */ 
/* 2851 */     synchronized (heavyweightRequests)
/*      */     {
/* 2862 */       if ((newFocusOwner != null) && (paramAWTEvent.getID() == 1005))
/*      */       {
/* 2865 */         FocusEvent localFocusEvent = (FocusEvent)paramAWTEvent;
/*      */ 
/* 2867 */         if ((localKeyboardFocusManager.getGlobalFocusOwner() == localFocusEvent.getComponent()) && (localFocusEvent.getOppositeComponent() == newFocusOwner))
/*      */         {
/* 2870 */           newFocusOwner = null;
/* 2871 */           return paramAWTEvent;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2876 */     processCurrentLightweightRequests();
/*      */ 
/* 2878 */     switch (paramAWTEvent.getID()) {
/*      */     case 1004:
/* 2880 */       paramAWTEvent = retargetFocusGained((FocusEvent)paramAWTEvent);
/* 2881 */       break;
/*      */     case 1005:
/* 2884 */       paramAWTEvent = retargetFocusLost((FocusEvent)paramAWTEvent);
/* 2885 */       break;
/*      */     }
/*      */ 
/* 2890 */     return paramAWTEvent;
/*      */   }
/*      */ 
/*      */   void clearMarkers()
/*      */   {
/*      */   }
/*      */ 
/*      */   static boolean removeFirstRequest()
/*      */   {
/* 2903 */     KeyboardFocusManager localKeyboardFocusManager = getCurrentKeyboardFocusManager();
/*      */ 
/* 2906 */     synchronized (heavyweightRequests) {
/* 2907 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getFirstHWRequest();
/*      */ 
/* 2909 */       if (localHeavyweightFocusRequest != null) {
/* 2910 */         heavyweightRequests.removeFirst();
/* 2911 */         if (localHeavyweightFocusRequest.lightweightRequests != null) {
/* 2912 */           Iterator localIterator = localHeavyweightFocusRequest.lightweightRequests.iterator();
/*      */ 
/* 2914 */           while (localIterator.hasNext())
/*      */           {
/* 2916 */             localKeyboardFocusManager.dequeueKeyEvents(-1L, ((LightweightFocusRequest)localIterator.next()).component);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2924 */       if (heavyweightRequests.size() == 0) {
/* 2925 */         localKeyboardFocusManager.clearMarkers();
/*      */       }
/* 2927 */       return heavyweightRequests.size() > 0;
/*      */     }
/*      */   }
/*      */ 
/* 2931 */   static void removeLastFocusRequest(Component paramComponent) { if ((log.isLoggable(500)) && 
/* 2932 */       (paramComponent == null)) {
/* 2933 */       log.fine("Assertion (heavyweight != null) failed");
/*      */     }
/*      */ 
/* 2937 */     KeyboardFocusManager localKeyboardFocusManager = getCurrentKeyboardFocusManager();
/*      */ 
/* 2939 */     synchronized (heavyweightRequests) {
/* 2940 */       HeavyweightFocusRequest localHeavyweightFocusRequest = getLastHWRequest();
/* 2941 */       if ((localHeavyweightFocusRequest != null) && (localHeavyweightFocusRequest.heavyweight == paramComponent))
/*      */       {
/* 2943 */         heavyweightRequests.removeLast();
/*      */       }
/*      */ 
/* 2947 */       if (heavyweightRequests.size() == 0)
/* 2948 */         localKeyboardFocusManager.clearMarkers();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean focusedWindowChanged(Component paramComponent1, Component paramComponent2)
/*      */   {
/* 2954 */     Window localWindow1 = SunToolkit.getContainingWindow(paramComponent1);
/* 2955 */     Window localWindow2 = SunToolkit.getContainingWindow(paramComponent2);
/* 2956 */     if ((localWindow1 == null) && (localWindow2 == null)) {
/* 2957 */       return true;
/*      */     }
/* 2959 */     if (localWindow1 == null) {
/* 2960 */       return true;
/*      */     }
/* 2962 */     if (localWindow2 == null) {
/* 2963 */       return true;
/*      */     }
/* 2965 */     return localWindow1 != localWindow2;
/*      */   }
/*      */ 
/*      */   private static boolean isTemporary(Component paramComponent1, Component paramComponent2) {
/* 2969 */     Window localWindow1 = SunToolkit.getContainingWindow(paramComponent1);
/* 2970 */     Window localWindow2 = SunToolkit.getContainingWindow(paramComponent2);
/* 2971 */     if ((localWindow1 == null) && (localWindow2 == null)) {
/* 2972 */       return false;
/*      */     }
/* 2974 */     if (localWindow1 == null) {
/* 2975 */       return true;
/*      */     }
/* 2977 */     if (localWindow2 == null) {
/* 2978 */       return false;
/*      */     }
/* 2980 */     return localWindow1 != localWindow2;
/*      */   }
/*      */ 
/*      */   static Component getHeavyweight(Component paramComponent) {
/* 2984 */     if ((paramComponent == null) || (paramComponent.getPeer() == null))
/* 2985 */       return null;
/* 2986 */     if ((paramComponent.getPeer() instanceof LightweightPeer)) {
/* 2987 */       return paramComponent.getNativeContainer();
/*      */     }
/* 2989 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   private static boolean isProxyActiveImpl(KeyEvent paramKeyEvent)
/*      */   {
/* 2996 */     if (proxyActive == null) {
/* 2997 */       proxyActive = (Field)AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Object run() {
/* 2999 */           Field localField = null;
/*      */           try {
/* 3001 */             localField = KeyEvent.class.getDeclaredField("isProxyActive");
/* 3002 */             if (localField != null)
/* 3003 */               localField.setAccessible(true);
/*      */           }
/*      */           catch (NoSuchFieldException localNoSuchFieldException) {
/* 3006 */             if (!$assertionsDisabled) throw new AssertionError();
/*      */           }
/* 3008 */           return localField;
/*      */         }
/*      */       });
/*      */     }
/*      */     try
/*      */     {
/* 3014 */       return proxyActive.getBoolean(paramKeyEvent);
/*      */     } catch (IllegalAccessException localIllegalAccessException) {
/* 3016 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/* 3018 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean isProxyActive(KeyEvent paramKeyEvent)
/*      */   {
/* 3023 */     if (!GraphicsEnvironment.isHeadless()) {
/* 3024 */       return isProxyActiveImpl(paramKeyEvent);
/*      */     }
/* 3026 */     return false;
/*      */   }
/*      */ 
/*      */   private static HeavyweightFocusRequest getLastHWRequest()
/*      */   {
/* 3031 */     synchronized (heavyweightRequests) {
/* 3032 */       return heavyweightRequests.size() > 0 ? (HeavyweightFocusRequest)heavyweightRequests.getLast() : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static HeavyweightFocusRequest getFirstHWRequest()
/*      */   {
/* 3039 */     synchronized (heavyweightRequests) {
/* 3040 */       return heavyweightRequests.size() > 0 ? (HeavyweightFocusRequest)heavyweightRequests.getFirst() : null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkReplaceKFMPermission()
/*      */     throws SecurityException
/*      */   {
/* 3049 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 3050 */     if (localSecurityManager != null) {
/* 3051 */       if (replaceKeyboardFocusManagerPermission == null) {
/* 3052 */         replaceKeyboardFocusManagerPermission = new AWTPermission("replaceKeyboardFocusManager");
/*      */       }
/*      */ 
/* 3055 */       localSecurityManager.checkPermission(replaceKeyboardFocusManagerPermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkKFMSecurity()
/*      */   {
/* 3074 */     if (this != getCurrentKeyboardFocusManager())
/* 3075 */       checkReplaceKFMPermission();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  112 */     focusLog = PlatformLogger.getLogger("java.awt.focus.KeyboardFocusManager");
/*      */ 
/*  116 */     Toolkit.loadLibraries();
/*  117 */     if (!GraphicsEnvironment.isHeadless()) {
/*  118 */       initIDs();
/*      */     }
/*  120 */     AWTAccessor.setKeyboardFocusManagerAccessor(new AWTAccessor.KeyboardFocusManagerAccessor()
/*      */     {
/*      */       public int shouldNativelyFocusHeavyweight(Component paramAnonymousComponent1, Component paramAnonymousComponent2, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, long paramAnonymousLong, CausedFocusEvent.Cause paramAnonymousCause)
/*      */       {
/*  129 */         return KeyboardFocusManager.shouldNativelyFocusHeavyweight(paramAnonymousComponent1, paramAnonymousComponent2, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousLong, paramAnonymousCause);
/*      */       }
/*      */ 
/*      */       public boolean processSynchronousLightweightTransfer(Component paramAnonymousComponent1, Component paramAnonymousComponent2, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, long paramAnonymousLong)
/*      */       {
/*  138 */         return KeyboardFocusManager.processSynchronousLightweightTransfer(paramAnonymousComponent1, paramAnonymousComponent2, paramAnonymousBoolean1, paramAnonymousBoolean2, paramAnonymousLong);
/*      */       }
/*      */ 
/*      */       public void removeLastFocusRequest(Component paramAnonymousComponent) {
/*  142 */         KeyboardFocusManager.removeLastFocusRequest(paramAnonymousComponent);
/*      */       }
/*      */       public void setMostRecentFocusOwner(Window paramAnonymousWindow, Component paramAnonymousComponent) {
/*  145 */         KeyboardFocusManager.setMostRecentFocusOwner(paramAnonymousWindow, paramAnonymousComponent);
/*      */       }
/*      */       public KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext paramAnonymousAppContext) {
/*  148 */         return KeyboardFocusManager.getCurrentKeyboardFocusManager(paramAnonymousAppContext);
/*      */       }
/*      */       public Container getCurrentFocusCycleRoot() {
/*  151 */         return KeyboardFocusManager.currentFocusCycleRoot;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static final class HeavyweightFocusRequest
/*      */   {
/*      */     final Component heavyweight;
/*      */     final LinkedList<KeyboardFocusManager.LightweightFocusRequest> lightweightRequests;
/* 2123 */     static final HeavyweightFocusRequest CLEAR_GLOBAL_FOCUS_OWNER = new HeavyweightFocusRequest();
/*      */ 
/*      */     private HeavyweightFocusRequest()
/*      */     {
/* 2127 */       this.heavyweight = null;
/* 2128 */       this.lightweightRequests = null;
/*      */     }
/*      */ 
/*      */     HeavyweightFocusRequest(Component paramComponent1, Component paramComponent2, boolean paramBoolean, CausedFocusEvent.Cause paramCause)
/*      */     {
/* 2133 */       if ((KeyboardFocusManager.log.isLoggable(500)) && 
/* 2134 */         (paramComponent1 == null)) {
/* 2135 */         KeyboardFocusManager.log.fine("Assertion (heavyweight != null) failed");
/*      */       }
/*      */ 
/* 2139 */       this.heavyweight = paramComponent1;
/* 2140 */       this.lightweightRequests = new LinkedList();
/* 2141 */       addLightweightRequest(paramComponent2, paramBoolean, paramCause);
/*      */     }
/*      */ 
/*      */     boolean addLightweightRequest(Component paramComponent, boolean paramBoolean, CausedFocusEvent.Cause paramCause) {
/* 2145 */       if (KeyboardFocusManager.log.isLoggable(500)) {
/* 2146 */         if (this == CLEAR_GLOBAL_FOCUS_OWNER) {
/* 2147 */           KeyboardFocusManager.log.fine("Assertion (this != HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) failed");
/*      */         }
/* 2149 */         if (paramComponent == null) {
/* 2150 */           KeyboardFocusManager.log.fine("Assertion (descendant != null) failed");
/*      */         }
/*      */       }
/*      */ 
/* 2154 */       Object localObject = this.lightweightRequests.size() > 0 ? ((KeyboardFocusManager.LightweightFocusRequest)this.lightweightRequests.getLast()).component : null;
/*      */ 
/* 2158 */       if (paramComponent != localObject)
/*      */       {
/* 2160 */         this.lightweightRequests.add(new KeyboardFocusManager.LightweightFocusRequest(paramComponent, paramBoolean, paramCause));
/*      */ 
/* 2162 */         return true;
/*      */       }
/* 2164 */       return false;
/*      */     }
/*      */ 
/*      */     KeyboardFocusManager.LightweightFocusRequest getFirstLightweightRequest()
/*      */     {
/* 2169 */       if (this == CLEAR_GLOBAL_FOCUS_OWNER) {
/* 2170 */         return null;
/*      */       }
/* 2172 */       return (KeyboardFocusManager.LightweightFocusRequest)this.lightweightRequests.getFirst();
/*      */     }
/*      */     public String toString() {
/* 2175 */       int i = 1;
/* 2176 */       String str = "HeavyweightFocusRequest[heavweight=" + this.heavyweight + ",lightweightRequests=";
/*      */ 
/* 2178 */       if (this.lightweightRequests == null) {
/* 2179 */         str = str + null;
/*      */       } else {
/* 2181 */         str = str + "[";
/*      */ 
/* 2183 */         for (KeyboardFocusManager.LightweightFocusRequest localLightweightFocusRequest : this.lightweightRequests) {
/* 2184 */           if (i != 0)
/* 2185 */             i = 0;
/*      */           else {
/* 2187 */             str = str + ",";
/*      */           }
/* 2189 */           str = str + localLightweightFocusRequest;
/*      */         }
/* 2191 */         str = str + "]";
/*      */       }
/* 2193 */       str = str + "]";
/* 2194 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class LightweightFocusRequest
/*      */   {
/*      */     final Component component;
/*      */     final boolean temporary;
/*      */     final CausedFocusEvent.Cause cause;
/*      */ 
/*      */     LightweightFocusRequest(Component paramComponent, boolean paramBoolean, CausedFocusEvent.Cause paramCause)
/*      */     {
/* 2109 */       this.component = paramComponent;
/* 2110 */       this.temporary = paramBoolean;
/* 2111 */       this.cause = paramCause;
/*      */     }
/*      */     public String toString() {
/* 2114 */       return "LightweightFocusRequest[component=" + this.component + ",temporary=" + this.temporary + ", cause=" + this.cause + "]";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.KeyboardFocusManager
 * JD-Core Version:    0.6.2
 */