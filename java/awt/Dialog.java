/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.peer.DialogPeer;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessControlException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.awt.util.IdentityArrayList;
/*      */ import sun.awt.util.IdentityLinkedList;
/*      */ import sun.security.util.SecurityConstants.AWT;
/*      */ 
/*      */ public class Dialog extends Window
/*      */ {
/*  115 */   boolean resizable = true;
/*      */ 
/*  130 */   boolean undecorated = false;
/*      */ 
/*  132 */   private transient boolean initialized = false;
/*      */ 
/*  197 */   public static final ModalityType DEFAULT_MODALITY_TYPE = ModalityType.APPLICATION_MODAL;
/*      */   boolean modal;
/*      */   ModalityType modalityType;
/*  269 */   static transient IdentityArrayList<Dialog> modalDialogs = new IdentityArrayList();
/*      */ 
/*  271 */   transient IdentityArrayList<Window> blockedWindows = new IdentityArrayList();
/*      */   String title;
/*      */   private transient ModalEventFilter modalFilter;
/*      */   private volatile transient SecondaryLoop secondaryLoop;
/*  295 */   volatile transient boolean isInHide = false;
/*      */ 
/*  306 */   volatile transient boolean isInDispose = false;
/*      */   private static final String base = "dialog";
/*  309 */   private static int nameCounter = 0;
/*      */   private static final long serialVersionUID = 5920926903803293709L;
/*      */ 
/*      */   public Dialog(Frame paramFrame)
/*      */   {
/*  332 */     this(paramFrame, "", false);
/*      */   }
/*      */ 
/*      */   public Dialog(Frame paramFrame, boolean paramBoolean)
/*      */   {
/*  358 */     this(paramFrame, "", paramBoolean);
/*      */   }
/*      */ 
/*      */   public Dialog(Frame paramFrame, String paramString)
/*      */   {
/*  379 */     this(paramFrame, paramString, false);
/*      */   }
/*      */ 
/*      */   public Dialog(Frame paramFrame, String paramString, boolean paramBoolean)
/*      */   {
/*  409 */     this(paramFrame, paramString, paramBoolean ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public Dialog(Frame paramFrame, String paramString, boolean paramBoolean, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  443 */     this(paramFrame, paramString, paramBoolean ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, paramGraphicsConfiguration);
/*      */   }
/*      */ 
/*      */   public Dialog(Dialog paramDialog)
/*      */   {
/*  460 */     this(paramDialog, "", false);
/*      */   }
/*      */ 
/*      */   public Dialog(Dialog paramDialog, String paramString)
/*      */   {
/*  480 */     this(paramDialog, paramString, false);
/*      */   }
/*      */ 
/*      */   public Dialog(Dialog paramDialog, String paramString, boolean paramBoolean)
/*      */   {
/*  510 */     this(paramDialog, paramString, paramBoolean ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public Dialog(Dialog paramDialog, String paramString, boolean paramBoolean, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  547 */     this(paramDialog, paramString, paramBoolean ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, paramGraphicsConfiguration);
/*      */   }
/*      */ 
/*      */   public Dialog(Window paramWindow)
/*      */   {
/*  571 */     this(paramWindow, "", ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public Dialog(Window paramWindow, String paramString)
/*      */   {
/*  597 */     this(paramWindow, paramString, ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public Dialog(Window paramWindow, ModalityType paramModalityType)
/*      */   {
/*  630 */     this(paramWindow, "", paramModalityType);
/*      */   }
/*      */ 
/*      */   public Dialog(Window paramWindow, String paramString, ModalityType paramModalityType)
/*      */   {
/*  665 */     super(paramWindow);
/*      */ 
/*  667 */     if ((paramWindow != null) && (!(paramWindow instanceof Frame)) && (!(paramWindow instanceof Dialog)))
/*      */     {
/*  671 */       throw new IllegalArgumentException("Wrong parent window");
/*      */     }
/*      */ 
/*  674 */     this.title = paramString;
/*  675 */     setModalityType(paramModalityType);
/*  676 */     SunToolkit.checkAndSetPolicy(this);
/*  677 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public Dialog(Window paramWindow, String paramString, ModalityType paramModalityType, GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/*  717 */     super(paramWindow, paramGraphicsConfiguration);
/*      */ 
/*  719 */     if ((paramWindow != null) && (!(paramWindow instanceof Frame)) && (!(paramWindow instanceof Dialog)))
/*      */     {
/*  723 */       throw new IllegalArgumentException("wrong owner window");
/*      */     }
/*      */ 
/*  726 */     this.title = paramString;
/*  727 */     setModalityType(paramModalityType);
/*  728 */     SunToolkit.checkAndSetPolicy(this);
/*  729 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   String constructComponentName()
/*      */   {
/*  737 */     synchronized (Dialog.class) {
/*  738 */       return "dialog" + nameCounter++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  752 */     synchronized (getTreeLock()) {
/*  753 */       if ((this.parent != null) && (this.parent.getPeer() == null)) {
/*  754 */         this.parent.addNotify();
/*      */       }
/*      */ 
/*  757 */       if (this.peer == null) {
/*  758 */         this.peer = getToolkit().createDialog(this);
/*      */       }
/*  760 */       super.addNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isModal()
/*      */   {
/*  780 */     return isModal_NoClientCode();
/*      */   }
/*      */   final boolean isModal_NoClientCode() {
/*  783 */     return this.modalityType != ModalityType.MODELESS;
/*      */   }
/*      */ 
/*      */   public void setModal(boolean paramBoolean)
/*      */   {
/*  810 */     this.modal = paramBoolean;
/*  811 */     setModalityType(paramBoolean ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public ModalityType getModalityType()
/*      */   {
/*  824 */     return this.modalityType;
/*      */   }
/*      */ 
/*      */   public void setModalityType(ModalityType paramModalityType)
/*      */   {
/*  850 */     if (paramModalityType == null) {
/*  851 */       paramModalityType = ModalityType.MODELESS;
/*      */     }
/*  853 */     if (!Toolkit.getDefaultToolkit().isModalityTypeSupported(paramModalityType)) {
/*  854 */       paramModalityType = ModalityType.MODELESS;
/*      */     }
/*  856 */     if (this.modalityType == paramModalityType) {
/*  857 */       return;
/*      */     }
/*      */ 
/*  860 */     checkModalityPermission(paramModalityType);
/*      */ 
/*  862 */     this.modalityType = paramModalityType;
/*  863 */     this.modal = (this.modalityType != ModalityType.MODELESS);
/*      */   }
/*      */ 
/*      */   public String getTitle()
/*      */   {
/*  874 */     return this.title;
/*      */   }
/*      */ 
/*      */   public void setTitle(String paramString)
/*      */   {
/*  884 */     String str = this.title;
/*      */ 
/*  886 */     synchronized (this) {
/*  887 */       this.title = paramString;
/*  888 */       DialogPeer localDialogPeer = (DialogPeer)this.peer;
/*  889 */       if (localDialogPeer != null) {
/*  890 */         localDialogPeer.setTitle(paramString);
/*      */       }
/*      */     }
/*  893 */     firePropertyChange("title", str, paramString);
/*      */   }
/*      */ 
/*      */   private boolean conditionalShow(Component paramComponent, AtomicLong paramAtomicLong)
/*      */   {
/*  902 */     closeSplashScreen();
/*      */     boolean bool;
/*  904 */     synchronized (getTreeLock()) {
/*  905 */       if (this.peer == null) {
/*  906 */         addNotify();
/*      */       }
/*  908 */       validateUnconditionally();
/*  909 */       if (this.visible) {
/*  910 */         toFront();
/*  911 */         bool = false;
/*      */       } else {
/*  913 */         this.visible = (bool = 1);
/*      */ 
/*  918 */         if (!isModal()) {
/*  919 */           checkShouldBeBlocked(this);
/*      */         } else {
/*  921 */           modalDialogs.add(this);
/*  922 */           modalShow();
/*      */         }
/*      */ 
/*  925 */         if ((paramComponent != null) && (paramAtomicLong != null) && (isFocusable()) && (isEnabled()) && (!isModalBlocked()))
/*      */         {
/*  929 */           paramAtomicLong.set(Toolkit.getEventQueue().getMostRecentEventTimeEx());
/*  930 */           KeyboardFocusManager.getCurrentKeyboardFocusManager().enqueueKeyEvents(paramAtomicLong.get(), paramComponent);
/*      */         }
/*      */ 
/*  936 */         mixOnShowing();
/*      */ 
/*  938 */         this.peer.setVisible(true);
/*  939 */         if (isModalBlocked()) {
/*  940 */           this.modalBlocker.toFront();
/*      */         }
/*      */ 
/*  943 */         setLocationByPlatform(false);
/*  944 */         for (int i = 0; i < this.ownedWindowList.size(); i++) {
/*  945 */           Window localWindow = (Window)((WeakReference)this.ownedWindowList.elementAt(i)).get();
/*  946 */           if ((localWindow != null) && (localWindow.showWithParent)) {
/*  947 */             localWindow.show();
/*  948 */             localWindow.showWithParent = false;
/*      */           }
/*      */         }
/*  951 */         Window.updateChildFocusableWindowState(this);
/*      */ 
/*  953 */         createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
/*      */ 
/*  957 */         if ((this.componentListener != null) || ((this.eventMask & 1L) != 0L) || (Toolkit.enabledOnToolkit(1L)))
/*      */         {
/*  960 */           ComponentEvent localComponentEvent = new ComponentEvent(this, 102);
/*      */ 
/*  962 */           Toolkit.getEventQueue().postEvent(localComponentEvent);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  967 */     if ((bool) && ((this.state & 0x1) == 0)) {
/*  968 */       postWindowEvent(200);
/*  969 */       this.state |= 1;
/*      */     }
/*      */ 
/*  972 */     return bool;
/*      */   }
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/* 1005 */     super.setVisible(paramBoolean);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void show()
/*      */   {
/* 1030 */     if (!this.initialized) throw new IllegalStateException("The dialog component has not been initialized properly");
/*      */ 
/* 1033 */     this.beforeFirstShow = false;
/* 1034 */     if (!isModal()) {
/* 1035 */       conditionalShow(null, null);
/*      */     } else {
/* 1037 */       AppContext localAppContext1 = AppContext.getAppContext();
/*      */ 
/* 1039 */       AtomicLong localAtomicLong = new AtomicLong();
/* 1040 */       Component localComponent = null;
/*      */       try {
/* 1042 */         localComponent = getMostRecentFocusOwner();
/* 1043 */         if (conditionalShow(localComponent, localAtomicLong)) {
/* 1044 */           this.modalFilter = ModalEventFilter.createFilterForDialog(this);
/* 1045 */           Conditional local1 = new Conditional()
/*      */           {
/*      */             public boolean evaluate() {
/* 1048 */               return Dialog.this.windowClosingException == null;
/*      */             }
/*      */           };
/*      */           Object localObject1;
/*      */           AppContext localAppContext2;
/*      */           EventQueue localEventQueue;
/*      */           Object localObject2;
/* 1054 */           if (this.modalityType == ModalityType.TOOLKIT_MODAL) {
/* 1055 */             localObject1 = AppContext.getAppContexts().iterator();
/* 1056 */             while (((Iterator)localObject1).hasNext()) {
/* 1057 */               localAppContext2 = (AppContext)((Iterator)localObject1).next();
/* 1058 */               if (localAppContext2 != localAppContext1)
/*      */               {
/* 1061 */                 localEventQueue = (EventQueue)localAppContext2.get(AppContext.EVENT_QUEUE_KEY);
/*      */ 
/* 1064 */                 localObject2 = new Runnable()
/*      */                 {
/*      */                   public void run()
/*      */                   {
/*      */                   }
/*      */                 };
/* 1067 */                 localEventQueue.postEvent(new InvocationEvent(this, (Runnable)localObject2));
/* 1068 */                 EventDispatchThread localEventDispatchThread = localEventQueue.getDispatchThread();
/* 1069 */                 localEventDispatchThread.addEventFilter(this.modalFilter);
/*      */               }
/*      */             }
/*      */           }
/* 1073 */           modalityPushed();
/*      */           try {
/* 1075 */             localObject1 = (EventQueue)AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public EventQueue run() {
/* 1078 */                 return Toolkit.getDefaultToolkit().getSystemEventQueue();
/*      */               }
/*      */             });
/* 1081 */             this.secondaryLoop = ((EventQueue)localObject1).createSecondaryLoop(local1, this.modalFilter, 0L);
/* 1082 */             if (!this.secondaryLoop.enter())
/* 1083 */               this.secondaryLoop = null;
/*      */           }
/*      */           finally {
/* 1086 */             modalityPopped();
/*      */           }
/*      */ 
/* 1091 */           if (this.modalityType == ModalityType.TOOLKIT_MODAL) {
/* 1092 */             localObject1 = AppContext.getAppContexts().iterator();
/* 1093 */             while (((Iterator)localObject1).hasNext()) {
/* 1094 */               localAppContext2 = (AppContext)((Iterator)localObject1).next();
/* 1095 */               if (localAppContext2 != localAppContext1)
/*      */               {
/* 1098 */                 localEventQueue = (EventQueue)localAppContext2.get(AppContext.EVENT_QUEUE_KEY);
/* 1099 */                 localObject2 = localEventQueue.getDispatchThread();
/* 1100 */                 ((EventDispatchThread)localObject2).removeEventFilter(this.modalFilter);
/*      */               }
/*      */             }
/*      */           }
/* 1104 */           if (this.windowClosingException != null) {
/* 1105 */             this.windowClosingException.fillInStackTrace();
/* 1106 */             throw this.windowClosingException;
/*      */           }
/*      */         }
/*      */       } finally {
/* 1110 */         if (localComponent != null)
/*      */         {
/* 1112 */           KeyboardFocusManager.getCurrentKeyboardFocusManager().dequeueKeyEvents(localAtomicLong.get(), localComponent);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void modalityPushed()
/*      */   {
/* 1120 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1121 */     if ((localToolkit instanceof SunToolkit)) {
/* 1122 */       SunToolkit localSunToolkit = (SunToolkit)localToolkit;
/* 1123 */       localSunToolkit.notifyModalityPushed(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   final void modalityPopped() {
/* 1128 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1129 */     if ((localToolkit instanceof SunToolkit)) {
/* 1130 */       SunToolkit localSunToolkit = (SunToolkit)localToolkit;
/* 1131 */       localSunToolkit.notifyModalityPopped(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   void interruptBlocking() {
/* 1136 */     if (isModal()) {
/* 1137 */       disposeImpl();
/* 1138 */     } else if (this.windowClosingException != null) {
/* 1139 */       this.windowClosingException.fillInStackTrace();
/* 1140 */       this.windowClosingException.printStackTrace();
/* 1141 */       this.windowClosingException = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void hideAndDisposePreHandler() {
/* 1146 */     this.isInHide = true;
/* 1147 */     synchronized (getTreeLock()) {
/* 1148 */       if (this.secondaryLoop != null) {
/* 1149 */         modalHide();
/*      */ 
/* 1152 */         if (this.modalFilter != null) {
/* 1153 */           this.modalFilter.disable();
/*      */         }
/* 1155 */         modalDialogs.remove(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 1160 */   private void hideAndDisposeHandler() { if (this.secondaryLoop != null) {
/* 1161 */       this.secondaryLoop.exit();
/* 1162 */       this.secondaryLoop = null;
/*      */     }
/* 1164 */     this.isInHide = false;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void hide()
/*      */   {
/* 1178 */     hideAndDisposePreHandler();
/* 1179 */     super.hide();
/*      */ 
/* 1183 */     if (!this.isInDispose)
/* 1184 */       hideAndDisposeHandler();
/*      */   }
/*      */ 
/*      */   void doDispose()
/*      */   {
/* 1195 */     this.isInDispose = true;
/* 1196 */     super.doDispose();
/* 1197 */     hideAndDisposeHandler();
/* 1198 */     this.isInDispose = false;
/*      */   }
/*      */ 
/*      */   public void toBack()
/*      */   {
/* 1210 */     super.toBack();
/* 1211 */     if (this.visible)
/* 1212 */       synchronized (getTreeLock()) {
/* 1213 */         for (Window localWindow : this.blockedWindows)
/* 1214 */           localWindow.toBack_NoClientCode();
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean isResizable()
/*      */   {
/* 1228 */     return this.resizable;
/*      */   }
/*      */ 
/*      */   public void setResizable(boolean paramBoolean)
/*      */   {
/* 1238 */     int i = 0;
/*      */ 
/* 1240 */     synchronized (this) {
/* 1241 */       this.resizable = paramBoolean;
/* 1242 */       DialogPeer localDialogPeer = (DialogPeer)this.peer;
/* 1243 */       if (localDialogPeer != null) {
/* 1244 */         localDialogPeer.setResizable(paramBoolean);
/* 1245 */         i = 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1253 */     if (i != 0)
/* 1254 */       invalidateIfValid();
/*      */   }
/*      */ 
/*      */   public void setUndecorated(boolean paramBoolean)
/*      */   {
/* 1290 */     synchronized (getTreeLock()) {
/* 1291 */       if (isDisplayable()) {
/* 1292 */         throw new IllegalComponentStateException("The dialog is displayable.");
/*      */       }
/* 1294 */       if (!paramBoolean) {
/* 1295 */         if (getOpacity() < 1.0F) {
/* 1296 */           throw new IllegalComponentStateException("The dialog is not opaque");
/*      */         }
/* 1298 */         if (getShape() != null) {
/* 1299 */           throw new IllegalComponentStateException("The dialog does not have a default shape");
/*      */         }
/* 1301 */         Color localColor = getBackground();
/* 1302 */         if ((localColor != null) && (localColor.getAlpha() < 255)) {
/* 1303 */           throw new IllegalComponentStateException("The dialog background color is not opaque");
/*      */         }
/*      */       }
/* 1306 */       this.undecorated = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isUndecorated()
/*      */   {
/* 1319 */     return this.undecorated;
/*      */   }
/*      */ 
/*      */   public void setOpacity(float paramFloat)
/*      */   {
/* 1327 */     synchronized (getTreeLock()) {
/* 1328 */       if ((paramFloat < 1.0F) && (!isUndecorated())) {
/* 1329 */         throw new IllegalComponentStateException("The dialog is decorated");
/*      */       }
/* 1331 */       super.setOpacity(paramFloat);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setShape(Shape paramShape)
/*      */   {
/* 1340 */     synchronized (getTreeLock()) {
/* 1341 */       if ((paramShape != null) && (!isUndecorated())) {
/* 1342 */         throw new IllegalComponentStateException("The dialog is decorated");
/*      */       }
/* 1344 */       super.setShape(paramShape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/* 1353 */     synchronized (getTreeLock()) {
/* 1354 */       if ((paramColor != null) && (paramColor.getAlpha() < 255) && (!isUndecorated())) {
/* 1355 */         throw new IllegalComponentStateException("The dialog is decorated");
/*      */       }
/* 1357 */       super.setBackground(paramColor);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1371 */     String str = super.paramString() + "," + this.modalityType;
/* 1372 */     if (this.title != null) {
/* 1373 */       str = str + ",title=" + this.title;
/*      */     }
/* 1375 */     return str;
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   void modalShow()
/*      */   {
/* 1399 */     IdentityArrayList localIdentityArrayList1 = new IdentityArrayList();
/* 1400 */     for (Iterator localIterator = modalDialogs.iterator(); localIterator.hasNext(); ) { localDialog1 = (Dialog)localIterator.next();
/* 1401 */       if (localDialog1.shouldBlock(this)) {
/* 1402 */         localObject1 = localDialog1;
/* 1403 */         while ((localObject1 != null) && (localObject1 != this)) {
/* 1404 */           localObject1 = ((Window)localObject1).getOwner_NoClientCode();
/*      */         }
/* 1406 */         if ((localObject1 == this) || (!shouldBlock(localDialog1)) || (this.modalityType.compareTo(localDialog1.getModalityType()) < 0))
/* 1407 */           localIdentityArrayList1.add(localDialog1);
/*      */       }
/*      */     }
/*      */     Dialog localDialog1;
/* 1413 */     for (int i = 0; i < localIdentityArrayList1.size(); i++) {
/* 1414 */       localDialog1 = (Dialog)localIdentityArrayList1.get(i);
/* 1415 */       if (localDialog1.isModalBlocked()) {
/* 1416 */         localObject1 = localDialog1.getModalBlocker();
/* 1417 */         if (!localIdentityArrayList1.contains(localObject1)) {
/* 1418 */           localIdentityArrayList1.add(i + 1, localObject1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1423 */     if (localIdentityArrayList1.size() > 0) {
/* 1424 */       ((Dialog)localIdentityArrayList1.get(0)).blockWindow(this);
/*      */     }
/*      */ 
/* 1428 */     IdentityArrayList localIdentityArrayList2 = new IdentityArrayList(localIdentityArrayList1);
/* 1429 */     int j = 0;
/* 1430 */     while (j < localIdentityArrayList2.size()) {
/* 1431 */       localObject1 = (Window)localIdentityArrayList2.get(j);
/* 1432 */       localObject2 = ((Window)localObject1).getOwnedWindows_NoClientCode();
/* 1433 */       for (Object localObject4 : localObject2) {
/* 1434 */         localIdentityArrayList2.add(localObject4);
/*      */       }
/* 1436 */       j++;
/*      */     }
/*      */ 
/* 1439 */     Object localObject1 = new IdentityLinkedList();
/*      */ 
/* 1441 */     Object localObject2 = Window.getAllUnblockedWindows();
/* 1442 */     for (??? = ((IdentityArrayList)localObject2).iterator(); ((Iterator)???).hasNext(); ) { Window localWindow = (Window)((Iterator)???).next();
/* 1443 */       if ((shouldBlock(localWindow)) && (!localIdentityArrayList2.contains(localWindow)))
/* 1444 */         if (((localWindow instanceof Dialog)) && (((Dialog)localWindow).isModal_NoClientCode())) {
/* 1445 */           Dialog localDialog2 = (Dialog)localWindow;
/* 1446 */           if ((localDialog2.shouldBlock(this)) && (modalDialogs.indexOf(localDialog2) > modalDialogs.indexOf(this)));
/*      */         }
/*      */         else {
/* 1450 */           ((List)localObject1).add(localWindow);
/*      */         }
/*      */     }
/* 1453 */     blockWindows((List)localObject1);
/*      */ 
/* 1455 */     if (!isModalBlocked())
/* 1456 */       updateChildrenBlocking();
/*      */   }
/*      */ 
/*      */   void modalHide()
/*      */   {
/* 1469 */     IdentityArrayList localIdentityArrayList = new IdentityArrayList();
/* 1470 */     int i = this.blockedWindows.size();
/*      */     Window localWindow;
/* 1471 */     for (int j = 0; j < i; j++) {
/* 1472 */       localWindow = (Window)this.blockedWindows.get(0);
/* 1473 */       localIdentityArrayList.add(localWindow);
/* 1474 */       unblockWindow(localWindow);
/*      */     }
/*      */ 
/* 1478 */     for (j = 0; j < i; j++) {
/* 1479 */       localWindow = (Window)localIdentityArrayList.get(j);
/* 1480 */       if (((localWindow instanceof Dialog)) && (((Dialog)localWindow).isModal_NoClientCode())) {
/* 1481 */         Dialog localDialog = (Dialog)localWindow;
/* 1482 */         localDialog.modalShow();
/*      */       } else {
/* 1484 */         checkShouldBeBlocked(localWindow);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean shouldBlock(Window paramWindow)
/*      */   {
/* 1499 */     if ((!isVisible_NoClientCode()) || ((!paramWindow.isVisible_NoClientCode()) && (!paramWindow.isInShow)) || (this.isInHide) || (paramWindow == this) || (!isModal_NoClientCode()))
/*      */     {
/* 1505 */       return false;
/*      */     }
/* 1507 */     if (((paramWindow instanceof Dialog)) && (((Dialog)paramWindow).isInHide)) {
/* 1508 */       return false;
/*      */     }
/*      */ 
/* 1513 */     Dialog localDialog = this;
/*      */     Object localObject;
/* 1514 */     while (localDialog != null) {
/* 1515 */       localObject = paramWindow;
/* 1516 */       while ((localObject != null) && (localObject != localDialog)) {
/* 1517 */         localObject = ((Component)localObject).getParent_NoClientCode();
/*      */       }
/* 1519 */       if (localObject == localDialog) {
/* 1520 */         return false;
/*      */       }
/* 1522 */       localDialog = localDialog.getModalBlocker();
/*      */     }
/* 1524 */     switch (4.$SwitchMap$java$awt$Dialog$ModalityType[this.modalityType.ordinal()]) {
/*      */     case 1:
/* 1526 */       return false;
/*      */     case 2:
/* 1528 */       if (paramWindow.isModalExcluded(ModalExclusionType.APPLICATION_EXCLUDE))
/*      */       {
/* 1531 */         localObject = this;
/* 1532 */         while ((localObject != null) && (localObject != paramWindow)) {
/* 1533 */           localObject = ((Component)localObject).getParent_NoClientCode();
/*      */         }
/* 1535 */         return localObject == paramWindow;
/*      */       }
/* 1537 */       return getDocumentRoot() == paramWindow.getDocumentRoot();
/*      */     case 3:
/* 1540 */       return (!paramWindow.isModalExcluded(ModalExclusionType.APPLICATION_EXCLUDE)) && (this.appContext == paramWindow.appContext);
/*      */     case 4:
/* 1543 */       return !paramWindow.isModalExcluded(ModalExclusionType.TOOLKIT_EXCLUDE);
/*      */     }
/*      */ 
/* 1546 */     return false;
/*      */   }
/*      */ 
/*      */   void blockWindow(Window paramWindow)
/*      */   {
/* 1556 */     if (!paramWindow.isModalBlocked()) {
/* 1557 */       paramWindow.setModalBlocked(this, true, true);
/* 1558 */       this.blockedWindows.add(paramWindow);
/*      */     }
/*      */   }
/*      */ 
/*      */   void blockWindows(List<Window> paramList) {
/* 1563 */     DialogPeer localDialogPeer = (DialogPeer)this.peer;
/* 1564 */     if (localDialogPeer == null) {
/* 1565 */       return;
/*      */     }
/* 1567 */     Iterator localIterator = paramList.iterator();
/* 1568 */     while (localIterator.hasNext()) {
/* 1569 */       Window localWindow = (Window)localIterator.next();
/* 1570 */       if (!localWindow.isModalBlocked())
/* 1571 */         localWindow.setModalBlocked(this, true, false);
/*      */       else {
/* 1573 */         localIterator.remove();
/*      */       }
/*      */     }
/* 1576 */     localDialogPeer.blockWindows(paramList);
/* 1577 */     this.blockedWindows.addAll(paramList);
/*      */   }
/*      */ 
/*      */   void unblockWindow(Window paramWindow)
/*      */   {
/* 1586 */     if ((paramWindow.isModalBlocked()) && (this.blockedWindows.contains(paramWindow))) {
/* 1587 */       this.blockedWindows.remove(paramWindow);
/* 1588 */       paramWindow.setModalBlocked(this, false, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   static void checkShouldBeBlocked(Window paramWindow)
/*      */   {
/* 1597 */     synchronized (paramWindow.getTreeLock()) {
/* 1598 */       for (int i = 0; i < modalDialogs.size(); i++) {
/* 1599 */         Dialog localDialog = (Dialog)modalDialogs.get(i);
/* 1600 */         if (localDialog.shouldBlock(paramWindow)) {
/* 1601 */           localDialog.blockWindow(paramWindow);
/* 1602 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkModalityPermission(ModalityType paramModalityType) {
/* 1609 */     if (paramModalityType == ModalityType.TOOLKIT_MODAL) {
/* 1610 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1611 */       if (localSecurityManager != null)
/* 1612 */         localSecurityManager.checkPermission(SecurityConstants.AWT.TOOLKIT_MODALITY_PERMISSION);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/* 1622 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/* 1624 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*      */ 
/* 1627 */     ModalityType localModalityType = (ModalityType)localGetField.get("modalityType", null);
/*      */     try
/*      */     {
/* 1630 */       checkModalityPermission(localModalityType);
/*      */     } catch (AccessControlException localAccessControlException) {
/* 1632 */       localModalityType = DEFAULT_MODALITY_TYPE;
/*      */     }
/*      */ 
/* 1636 */     if (localModalityType == null) {
/* 1637 */       this.modal = localGetField.get("modal", false);
/* 1638 */       setModal(this.modal);
/*      */     } else {
/* 1640 */       this.modalityType = localModalityType;
/*      */     }
/*      */ 
/* 1643 */     this.resizable = localGetField.get("resizable", true);
/* 1644 */     this.undecorated = localGetField.get("undecorated", false);
/* 1645 */     this.title = ((String)localGetField.get("title", ""));
/*      */ 
/* 1647 */     this.blockedWindows = new IdentityArrayList();
/*      */ 
/* 1649 */     SunToolkit.checkAndSetPolicy(this);
/*      */ 
/* 1651 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1671 */     if (this.accessibleContext == null) {
/* 1672 */       this.accessibleContext = new AccessibleAWTDialog();
/*      */     }
/* 1674 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  101 */     Toolkit.loadLibraries();
/*  102 */     if (!GraphicsEnvironment.isHeadless())
/*  103 */       initIDs();
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTDialog extends Window.AccessibleAWTWindow
/*      */   {
/*      */     private static final long serialVersionUID = 4837230331833941201L;
/*      */ 
/*      */     protected AccessibleAWTDialog()
/*      */     {
/* 1683 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1698 */       return AccessibleRole.DIALOG;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1709 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1710 */       if (Dialog.this.getFocusOwner() != null) {
/* 1711 */         localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */       }
/* 1713 */       if (Dialog.this.isModal()) {
/* 1714 */         localAccessibleStateSet.add(AccessibleState.MODAL);
/*      */       }
/* 1716 */       if (Dialog.this.isResizable()) {
/* 1717 */         localAccessibleStateSet.add(AccessibleState.RESIZABLE);
/*      */       }
/* 1719 */       return localAccessibleStateSet;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum ModalExclusionType
/*      */   {
/*  247 */     NO_EXCLUDE, 
/*      */ 
/*  253 */     APPLICATION_EXCLUDE, 
/*      */ 
/*  265 */     TOOLKIT_EXCLUDE;
/*      */   }
/*      */ 
/*      */   public static enum ModalityType
/*      */   {
/*  151 */     MODELESS, 
/*      */ 
/*  160 */     DOCUMENT_MODAL, 
/*      */ 
/*  168 */     APPLICATION_MODAL, 
/*      */ 
/*  184 */     TOOLKIT_MODAL;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Dialog
 * JD-Core Version:    0.6.2
 */