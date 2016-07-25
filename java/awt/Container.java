/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.dnd.DropTarget;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.peer.ContainerPeer;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.io.OptionalDataException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.EventListener;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.JInternalFrame;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ContainerAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.PeerEvent;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.java2d.pipe.Region;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class Container extends Component
/*      */ {
/*   93 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Container");
/*   94 */   private static final PlatformLogger eventLog = PlatformLogger.getLogger("java.awt.event.Container");
/*      */ 
/*   96 */   private static final Component[] EMPTY_ARRAY = new Component[0];
/*      */ 
/*  103 */   private List<Component> component = new ArrayList();
/*      */   LayoutManager layoutMgr;
/*      */   private LightweightDispatcher dispatcher;
/*      */   private transient FocusTraversalPolicy focusTraversalPolicy;
/*  153 */   private boolean focusCycleRoot = false;
/*      */   private boolean focusTraversalPolicyProvider;
/*      */   private transient Set printingThreads;
/*  166 */   private transient boolean printing = false;
/*      */   transient ContainerListener containerListener;
/*      */   transient int listeningChildren;
/*      */   transient int listeningBoundsChildren;
/*      */   transient int descendantsCount;
/*  176 */   transient Color preserveBackgroundColor = null;
/*      */   private static final long serialVersionUID = 4613797578919906343L;
/*      */   static final boolean INCLUDE_SELF = true;
/*      */   static final boolean SEARCH_HEAVYWEIGHTS = true;
/*  206 */   private transient int numOfHWComponents = 0;
/*  207 */   private transient int numOfLWComponents = 0;
/*      */ 
/*  209 */   private static final PlatformLogger mixingLog = PlatformLogger.getLogger("java.awt.mixing.Container");
/*      */ 
/*  238 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("ncomponents", Integer.TYPE), new ObjectStreamField("component", [Ljava.awt.Component.class), new ObjectStreamField("layoutMgr", LayoutManager.class), new ObjectStreamField("dispatcher", LightweightDispatcher.class), new ObjectStreamField("maxSize", Dimension.class), new ObjectStreamField("focusCycleRoot", Boolean.TYPE), new ObjectStreamField("containerSerializedDataVersion", Integer.TYPE), new ObjectStreamField("focusTraversalPolicyProvider", Boolean.TYPE) };
/*      */ 
/* 1543 */   private static final boolean isJavaAwtSmartInvalidate = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("java.awt.smartInvalidate"))).booleanValue();
/*      */ 
/* 1650 */   private static boolean descendUnconditionallyWhenValidating = false;
/*      */   transient Component modalComp;
/*      */   transient AppContext modalAppContext;
/* 3643 */   private int containerSerializedDataVersion = 1;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   void initializeFocusTraversalKeys()
/*      */   {
/*  280 */     this.focusTraversalKeys = new Set[4];
/*      */   }
/*      */ 
/*      */   public int getComponentCount()
/*      */   {
/*  294 */     return countComponents();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int countComponents()
/*      */   {
/*  306 */     return this.component.size();
/*      */   }
/*      */ 
/*      */   public Component getComponent(int paramInt)
/*      */   {
/*      */     try
/*      */     {
/*  325 */       return (Component)this.component.get(paramInt); } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*      */     }
/*  327 */     throw new ArrayIndexOutOfBoundsException("No such child: " + paramInt);
/*      */   }
/*      */ 
/*      */   public Component[] getComponents()
/*      */   {
/*  343 */     return getComponents_NoClientCode();
/*      */   }
/*      */ 
/*      */   final Component[] getComponents_NoClientCode()
/*      */   {
/*  351 */     return (Component[])this.component.toArray(EMPTY_ARRAY);
/*      */   }
/*      */ 
/*      */   Component[] getComponentsSync()
/*      */   {
/*  358 */     synchronized (getTreeLock()) {
/*  359 */       return getComponents();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Insets getInsets()
/*      */   {
/*  375 */     return insets();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Insets insets()
/*      */   {
/*  384 */     ComponentPeer localComponentPeer = this.peer;
/*  385 */     if ((localComponentPeer instanceof ContainerPeer)) {
/*  386 */       ContainerPeer localContainerPeer = (ContainerPeer)localComponentPeer;
/*  387 */       return (Insets)localContainerPeer.getInsets().clone();
/*      */     }
/*  389 */     return new Insets(0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent)
/*      */   {
/*  410 */     addImpl(paramComponent, null, -1);
/*  411 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public Component add(String paramString, Component paramComponent)
/*      */   {
/*  431 */     addImpl(paramComponent, paramString, -1);
/*  432 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent, int paramInt)
/*      */   {
/*  460 */     addImpl(paramComponent, null, paramInt);
/*  461 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   private void checkAddToSelf(Component paramComponent)
/*      */   {
/*  469 */     if ((paramComponent instanceof Container))
/*  470 */       for (Container localContainer = this; localContainer != null; localContainer = localContainer.parent)
/*  471 */         if (localContainer == paramComponent)
/*  472 */           throw new IllegalArgumentException("adding container's parent to itself");
/*      */   }
/*      */ 
/*      */   private void checkNotAWindow(Component paramComponent)
/*      */   {
/*  482 */     if ((paramComponent instanceof Window))
/*  483 */       throw new IllegalArgumentException("adding a window to a container");
/*      */   }
/*      */ 
/*      */   private void checkAdding(Component paramComponent, int paramInt)
/*      */   {
/*  499 */     checkTreeLock();
/*      */ 
/*  501 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
/*      */ 
/*  503 */     if ((paramInt > this.component.size()) || (paramInt < 0)) {
/*  504 */       throw new IllegalArgumentException("illegal component position");
/*      */     }
/*  506 */     if ((paramComponent.parent == this) && 
/*  507 */       (paramInt == this.component.size())) {
/*  508 */       throw new IllegalArgumentException("illegal component position " + paramInt + " should be less then " + this.component.size());
/*      */     }
/*      */ 
/*  512 */     checkAddToSelf(paramComponent);
/*  513 */     checkNotAWindow(paramComponent);
/*      */ 
/*  515 */     Window localWindow1 = getContainingWindow();
/*  516 */     Window localWindow2 = paramComponent.getContainingWindow();
/*  517 */     if (localWindow1 != localWindow2) {
/*  518 */       throw new IllegalArgumentException("component and container should be in the same top-level window");
/*      */     }
/*  520 */     if (localGraphicsConfiguration != null)
/*  521 */       paramComponent.checkGD(localGraphicsConfiguration.getDevice().getIDstring());
/*      */   }
/*      */ 
/*      */   private boolean removeDelicately(Component paramComponent, Container paramContainer, int paramInt)
/*      */   {
/*  535 */     checkTreeLock();
/*      */ 
/*  537 */     int i = getComponentZOrder(paramComponent);
/*  538 */     boolean bool = isRemoveNotifyNeeded(paramComponent, this, paramContainer);
/*  539 */     if (bool) {
/*  540 */       paramComponent.removeNotify();
/*      */     }
/*  542 */     if (paramContainer != this) {
/*  543 */       if (this.layoutMgr != null) {
/*  544 */         this.layoutMgr.removeLayoutComponent(paramComponent);
/*      */       }
/*  546 */       adjustListeningChildren(32768L, -paramComponent.numListening(32768L));
/*      */ 
/*  548 */       adjustListeningChildren(65536L, -paramComponent.numListening(65536L));
/*      */ 
/*  550 */       adjustDescendants(-paramComponent.countHierarchyMembers());
/*      */ 
/*  552 */       paramComponent.parent = null;
/*  553 */       if (bool) {
/*  554 */         paramComponent.setGraphicsConfiguration(null);
/*      */       }
/*  556 */       this.component.remove(i);
/*      */ 
/*  558 */       invalidateIfValid();
/*      */     }
/*      */     else
/*      */     {
/*  565 */       this.component.remove(i);
/*  566 */       this.component.add(paramInt, paramComponent);
/*      */     }
/*  568 */     if (paramComponent.parent == null) {
/*  569 */       if ((this.containerListener != null) || ((this.eventMask & 0x2) != 0L) || (Toolkit.enabledOnToolkit(2L)))
/*      */       {
/*  572 */         ContainerEvent localContainerEvent = new ContainerEvent(this, 301, paramComponent);
/*      */ 
/*  575 */         dispatchEvent(localContainerEvent);
/*      */       }
/*      */ 
/*  578 */       paramComponent.createHierarchyEvents(1400, paramComponent, this, 1L, Toolkit.enabledOnToolkit(32768L));
/*      */ 
/*  581 */       if ((this.peer != null) && (this.layoutMgr == null) && (isVisible())) {
/*  582 */         updateCursorImmediately();
/*      */       }
/*      */     }
/*  585 */     return bool;
/*      */   }
/*      */ 
/*      */   boolean canContainFocusOwner(Component paramComponent)
/*      */   {
/*  595 */     if ((!isEnabled()) || (!isDisplayable()) || (!isVisible()) || (!isFocusable()))
/*      */     {
/*  598 */       return false;
/*      */     }
/*  600 */     if (isFocusCycleRoot()) {
/*  601 */       FocusTraversalPolicy localFocusTraversalPolicy = getFocusTraversalPolicy();
/*  602 */       if (((localFocusTraversalPolicy instanceof DefaultFocusTraversalPolicy)) && 
/*  603 */         (!((DefaultFocusTraversalPolicy)localFocusTraversalPolicy).accept(paramComponent))) {
/*  604 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*  608 */     synchronized (getTreeLock()) {
/*  609 */       if (this.parent != null) {
/*  610 */         return this.parent.canContainFocusOwner(paramComponent);
/*      */       }
/*      */     }
/*  613 */     return true;
/*      */   }
/*      */ 
/*      */   final boolean hasHeavyweightDescendants()
/*      */   {
/*  623 */     checkTreeLock();
/*  624 */     return this.numOfHWComponents > 0;
/*      */   }
/*      */ 
/*      */   final boolean hasLightweightDescendants()
/*      */   {
/*  634 */     checkTreeLock();
/*  635 */     return this.numOfLWComponents > 0;
/*      */   }
/*      */ 
/*      */   Container getHeavyweightContainer()
/*      */   {
/*  644 */     checkTreeLock();
/*  645 */     if ((this.peer != null) && (!(this.peer instanceof LightweightPeer))) {
/*  646 */       return this;
/*      */     }
/*  648 */     return getNativeContainer();
/*      */   }
/*      */ 
/*      */   private static boolean isRemoveNotifyNeeded(Component paramComponent, Container paramContainer1, Container paramContainer2)
/*      */   {
/*  660 */     if (paramContainer1 == null) {
/*  661 */       return false;
/*      */     }
/*  663 */     if (paramComponent.peer == null) {
/*  664 */       return false;
/*      */     }
/*  666 */     if (paramContainer2.peer == null)
/*      */     {
/*  668 */       return true;
/*      */     }
/*      */ 
/*  673 */     if (paramComponent.isLightweight()) {
/*  674 */       boolean bool = paramComponent instanceof Container;
/*      */ 
/*  676 */       if ((!bool) || ((bool) && (!((Container)paramComponent).hasHeavyweightDescendants()))) {
/*  677 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  684 */     Container localContainer1 = paramContainer1.getHeavyweightContainer();
/*  685 */     Container localContainer2 = paramContainer2.getHeavyweightContainer();
/*  686 */     if (localContainer1 != localContainer2)
/*      */     {
/*  694 */       return !paramComponent.peer.isReparentSupported();
/*      */     }
/*  696 */     return false;
/*      */   }
/*      */ 
/*      */   public void setComponentZOrder(Component paramComponent, int paramInt)
/*      */   {
/*  751 */     synchronized (getTreeLock())
/*      */     {
/*  753 */       Container localContainer = paramComponent.parent;
/*  754 */       int i = getComponentZOrder(paramComponent);
/*      */ 
/*  756 */       if ((localContainer == this) && (paramInt == i)) {
/*  757 */         return;
/*      */       }
/*  759 */       checkAdding(paramComponent, paramInt);
/*      */ 
/*  761 */       int j = localContainer != null ? localContainer.removeDelicately(paramComponent, this, paramInt) : 0;
/*      */ 
/*  764 */       addDelicately(paramComponent, localContainer, paramInt);
/*      */ 
/*  768 */       if ((j == 0) && (i != -1))
/*      */       {
/*  774 */         paramComponent.mixOnZOrderChanging(i, paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void reparentTraverse(ContainerPeer paramContainerPeer, Container paramContainer)
/*      */   {
/*  785 */     checkTreeLock();
/*      */ 
/*  787 */     for (int i = 0; i < paramContainer.getComponentCount(); i++) {
/*  788 */       Component localComponent = paramContainer.getComponent(i);
/*  789 */       if (localComponent.isLightweight())
/*      */       {
/*  792 */         if ((localComponent instanceof Container)) {
/*  793 */           reparentTraverse(paramContainerPeer, (Container)localComponent);
/*      */         }
/*      */       }
/*      */       else
/*  797 */         localComponent.getPeer().reparent(paramContainerPeer);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void reparentChild(Component paramComponent)
/*      */   {
/*  808 */     checkTreeLock();
/*  809 */     if (paramComponent == null) {
/*  810 */       return;
/*      */     }
/*  812 */     if (paramComponent.isLightweight())
/*      */     {
/*  814 */       if ((paramComponent instanceof Container))
/*      */       {
/*  816 */         reparentTraverse((ContainerPeer)getPeer(), (Container)paramComponent);
/*      */       }
/*      */     }
/*  819 */     else paramComponent.getPeer().reparent((ContainerPeer)getPeer());
/*      */   }
/*      */ 
/*      */   private void addDelicately(Component paramComponent, Container paramContainer, int paramInt)
/*      */   {
/*  829 */     checkTreeLock();
/*      */ 
/*  832 */     if (paramContainer != this)
/*      */     {
/*  834 */       if (paramInt == -1)
/*  835 */         this.component.add(paramComponent);
/*      */       else {
/*  837 */         this.component.add(paramInt, paramComponent);
/*      */       }
/*  839 */       paramComponent.parent = this;
/*  840 */       paramComponent.setGraphicsConfiguration(getGraphicsConfiguration());
/*      */ 
/*  842 */       adjustListeningChildren(32768L, paramComponent.numListening(32768L));
/*      */ 
/*  844 */       adjustListeningChildren(65536L, paramComponent.numListening(65536L));
/*      */ 
/*  846 */       adjustDescendants(paramComponent.countHierarchyMembers());
/*      */     }
/*  848 */     else if (paramInt < this.component.size()) {
/*  849 */       this.component.set(paramInt, paramComponent);
/*      */     }
/*      */ 
/*  853 */     invalidateIfValid();
/*      */     Object localObject;
/*  854 */     if (this.peer != null) {
/*  855 */       if (paramComponent.peer == null) {
/*  856 */         paramComponent.addNotify();
/*      */       }
/*      */       else {
/*  859 */         localObject = getHeavyweightContainer();
/*  860 */         Container localContainer = paramContainer.getHeavyweightContainer();
/*  861 */         if (localContainer != localObject)
/*      */         {
/*  863 */           ((Container)localObject).reparentChild(paramComponent);
/*      */         }
/*  865 */         paramComponent.updateZOrder();
/*      */ 
/*  867 */         if ((!paramComponent.isLightweight()) && (isLightweight()))
/*      */         {
/*  870 */           paramComponent.relocateComponent();
/*      */         }
/*      */       }
/*      */     }
/*  874 */     if (paramContainer != this)
/*      */     {
/*  876 */       if (this.layoutMgr != null) {
/*  877 */         if ((this.layoutMgr instanceof LayoutManager2))
/*  878 */           ((LayoutManager2)this.layoutMgr).addLayoutComponent(paramComponent, null);
/*      */         else {
/*  880 */           this.layoutMgr.addLayoutComponent(null, paramComponent);
/*      */         }
/*      */       }
/*  883 */       if ((this.containerListener != null) || ((this.eventMask & 0x2) != 0L) || (Toolkit.enabledOnToolkit(2L)))
/*      */       {
/*  886 */         localObject = new ContainerEvent(this, 300, paramComponent);
/*      */ 
/*  889 */         dispatchEvent((AWTEvent)localObject);
/*      */       }
/*  891 */       paramComponent.createHierarchyEvents(1400, paramComponent, this, 1L, Toolkit.enabledOnToolkit(32768L));
/*      */ 
/*  897 */       if ((paramComponent.isFocusOwner()) && (!paramComponent.canBeFocusOwnerRecursively())) {
/*  898 */         paramComponent.transferFocus();
/*  899 */       } else if ((paramComponent instanceof Container)) {
/*  900 */         localObject = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*  901 */         if ((localObject != null) && (isParentOf((Component)localObject)) && (!((Component)localObject).canBeFocusOwnerRecursively()))
/*  902 */           ((Component)localObject).transferFocus();
/*      */       }
/*      */     }
/*      */     else {
/*  906 */       paramComponent.createHierarchyEvents(1400, paramComponent, this, 1400L, Toolkit.enabledOnToolkit(32768L));
/*      */     }
/*      */ 
/*  911 */     if ((this.peer != null) && (this.layoutMgr == null) && (isVisible()))
/*  912 */       updateCursorImmediately();
/*      */   }
/*      */ 
/*      */   public int getComponentZOrder(Component paramComponent)
/*      */   {
/*  930 */     if (paramComponent == null) {
/*  931 */       return -1;
/*      */     }
/*  933 */     synchronized (getTreeLock())
/*      */     {
/*  935 */       if (paramComponent.parent != this) {
/*  936 */         return -1;
/*      */       }
/*  938 */       return this.component.indexOf(paramComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void add(Component paramComponent, Object paramObject)
/*      */   {
/*  966 */     addImpl(paramComponent, paramObject, -1);
/*      */   }
/*      */ 
/*      */   public void add(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  998 */     addImpl(paramComponent, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/* 1069 */     synchronized (getTreeLock())
/*      */     {
/* 1077 */       GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
/*      */ 
/* 1079 */       if ((paramInt > this.component.size()) || ((paramInt < 0) && (paramInt != -1))) {
/* 1080 */         throw new IllegalArgumentException("illegal component position");
/*      */       }
/*      */ 
/* 1083 */       checkAddToSelf(paramComponent);
/* 1084 */       checkNotAWindow(paramComponent);
/* 1085 */       if (localGraphicsConfiguration != null) {
/* 1086 */         paramComponent.checkGD(localGraphicsConfiguration.getDevice().getIDstring());
/*      */       }
/*      */ 
/* 1090 */       if (paramComponent.parent != null) {
/* 1091 */         paramComponent.parent.remove(paramComponent);
/* 1092 */         if (paramInt > this.component.size()) {
/* 1093 */           throw new IllegalArgumentException("illegal component position");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1098 */       if (paramInt == -1)
/* 1099 */         this.component.add(paramComponent);
/*      */       else {
/* 1101 */         this.component.add(paramInt, paramComponent);
/*      */       }
/* 1103 */       paramComponent.parent = this;
/* 1104 */       paramComponent.setGraphicsConfiguration(localGraphicsConfiguration);
/*      */ 
/* 1106 */       adjustListeningChildren(32768L, paramComponent.numListening(32768L));
/*      */ 
/* 1108 */       adjustListeningChildren(65536L, paramComponent.numListening(65536L));
/*      */ 
/* 1110 */       adjustDescendants(paramComponent.countHierarchyMembers());
/*      */ 
/* 1112 */       invalidateIfValid();
/* 1113 */       if (this.peer != null) {
/* 1114 */         paramComponent.addNotify();
/*      */       }
/*      */ 
/* 1118 */       if (this.layoutMgr != null) {
/* 1119 */         if ((this.layoutMgr instanceof LayoutManager2))
/* 1120 */           ((LayoutManager2)this.layoutMgr).addLayoutComponent(paramComponent, paramObject);
/* 1121 */         else if ((paramObject instanceof String)) {
/* 1122 */           this.layoutMgr.addLayoutComponent((String)paramObject, paramComponent);
/*      */         }
/*      */       }
/* 1125 */       if ((this.containerListener != null) || ((this.eventMask & 0x2) != 0L) || (Toolkit.enabledOnToolkit(2L)))
/*      */       {
/* 1128 */         ContainerEvent localContainerEvent = new ContainerEvent(this, 300, paramComponent);
/*      */ 
/* 1131 */         dispatchEvent(localContainerEvent);
/*      */       }
/*      */ 
/* 1134 */       paramComponent.createHierarchyEvents(1400, paramComponent, this, 1L, Toolkit.enabledOnToolkit(32768L));
/*      */ 
/* 1137 */       if ((this.peer != null) && (this.layoutMgr == null) && (isVisible()))
/* 1138 */         updateCursorImmediately();
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean updateGraphicsData(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/* 1145 */     checkTreeLock();
/*      */ 
/* 1147 */     boolean bool = super.updateGraphicsData(paramGraphicsConfiguration);
/*      */ 
/* 1149 */     for (Component localComponent : this.component) {
/* 1150 */       if (localComponent != null) {
/* 1151 */         bool |= localComponent.updateGraphicsData(paramGraphicsConfiguration);
/*      */       }
/*      */     }
/* 1154 */     return bool;
/*      */   }
/*      */ 
/*      */   void checkGD(String paramString)
/*      */   {
/* 1163 */     for (Component localComponent : this.component)
/* 1164 */       if (localComponent != null)
/* 1165 */         localComponent.checkGD(paramString);
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/* 1193 */     synchronized (getTreeLock()) {
/* 1194 */       if ((paramInt < 0) || (paramInt >= this.component.size())) {
/* 1195 */         throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */       }
/* 1197 */       Component localComponent = (Component)this.component.get(paramInt);
/* 1198 */       if (this.peer != null) {
/* 1199 */         localComponent.removeNotify();
/*      */       }
/* 1201 */       if (this.layoutMgr != null) {
/* 1202 */         this.layoutMgr.removeLayoutComponent(localComponent);
/*      */       }
/*      */ 
/* 1205 */       adjustListeningChildren(32768L, -localComponent.numListening(32768L));
/*      */ 
/* 1207 */       adjustListeningChildren(65536L, -localComponent.numListening(65536L));
/*      */ 
/* 1209 */       adjustDescendants(-localComponent.countHierarchyMembers());
/*      */ 
/* 1211 */       localComponent.parent = null;
/* 1212 */       this.component.remove(paramInt);
/* 1213 */       localComponent.setGraphicsConfiguration(null);
/*      */ 
/* 1215 */       invalidateIfValid();
/* 1216 */       if ((this.containerListener != null) || ((this.eventMask & 0x2) != 0L) || (Toolkit.enabledOnToolkit(2L)))
/*      */       {
/* 1219 */         ContainerEvent localContainerEvent = new ContainerEvent(this, 301, localComponent);
/*      */ 
/* 1222 */         dispatchEvent(localContainerEvent);
/*      */       }
/*      */ 
/* 1225 */       localComponent.createHierarchyEvents(1400, localComponent, this, 1L, Toolkit.enabledOnToolkit(32768L));
/*      */ 
/* 1228 */       if ((this.peer != null) && (this.layoutMgr == null) && (isVisible()))
/* 1229 */         updateCursorImmediately();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/* 1253 */     synchronized (getTreeLock()) {
/* 1254 */       if (paramComponent.parent == this) {
/* 1255 */         int i = this.component.indexOf(paramComponent);
/* 1256 */         if (i >= 0)
/* 1257 */           remove(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/* 1279 */     synchronized (getTreeLock()) {
/* 1280 */       adjustListeningChildren(32768L, -this.listeningChildren);
/*      */ 
/* 1282 */       adjustListeningChildren(65536L, -this.listeningBoundsChildren);
/*      */ 
/* 1284 */       adjustDescendants(-this.descendantsCount);
/*      */ 
/* 1286 */       while (!this.component.isEmpty()) {
/* 1287 */         Component localComponent = (Component)this.component.remove(this.component.size() - 1);
/*      */ 
/* 1289 */         if (this.peer != null) {
/* 1290 */           localComponent.removeNotify();
/*      */         }
/* 1292 */         if (this.layoutMgr != null) {
/* 1293 */           this.layoutMgr.removeLayoutComponent(localComponent);
/*      */         }
/* 1295 */         localComponent.parent = null;
/* 1296 */         localComponent.setGraphicsConfiguration(null);
/* 1297 */         if ((this.containerListener != null) || ((this.eventMask & 0x2) != 0L) || (Toolkit.enabledOnToolkit(2L)))
/*      */         {
/* 1300 */           ContainerEvent localContainerEvent = new ContainerEvent(this, 301, localComponent);
/*      */ 
/* 1303 */           dispatchEvent(localContainerEvent);
/*      */         }
/*      */ 
/* 1306 */         localComponent.createHierarchyEvents(1400, localComponent, this, 1L, Toolkit.enabledOnToolkit(32768L));
/*      */       }
/*      */ 
/* 1311 */       if ((this.peer != null) && (this.layoutMgr == null) && (isVisible())) {
/* 1312 */         updateCursorImmediately();
/*      */       }
/* 1314 */       invalidateIfValid();
/*      */     }
/*      */   }
/*      */ 
/*      */   int numListening(long paramLong)
/*      */   {
/* 1320 */     int i = super.numListening(paramLong);
/*      */     int j;
/*      */     Iterator localIterator;
/*      */     Component localComponent;
/* 1322 */     if (paramLong == 32768L) {
/* 1323 */       if (eventLog.isLoggable(500))
/*      */       {
/* 1325 */         j = 0;
/* 1326 */         for (localIterator = this.component.iterator(); localIterator.hasNext(); ) { localComponent = (Component)localIterator.next();
/* 1327 */           j += localComponent.numListening(paramLong);
/*      */         }
/* 1329 */         if (this.listeningChildren != j) {
/* 1330 */           eventLog.fine("Assertion (listeningChildren == sum) failed");
/*      */         }
/*      */       }
/* 1333 */       return this.listeningChildren + i;
/* 1334 */     }if (paramLong == 65536L) {
/* 1335 */       if (eventLog.isLoggable(500))
/*      */       {
/* 1337 */         j = 0;
/* 1338 */         for (localIterator = this.component.iterator(); localIterator.hasNext(); ) { localComponent = (Component)localIterator.next();
/* 1339 */           j += localComponent.numListening(paramLong);
/*      */         }
/* 1341 */         if (this.listeningBoundsChildren != j) {
/* 1342 */           eventLog.fine("Assertion (listeningBoundsChildren == sum) failed");
/*      */         }
/*      */       }
/* 1345 */       return this.listeningBoundsChildren + i;
/*      */     }
/*      */ 
/* 1348 */     if (eventLog.isLoggable(500)) {
/* 1349 */       eventLog.fine("This code must never be reached");
/*      */     }
/* 1351 */     return i;
/*      */   }
/*      */ 
/*      */   void adjustListeningChildren(long paramLong, int paramInt)
/*      */   {
/* 1357 */     if (eventLog.isLoggable(500)) {
/* 1358 */       int i = (paramLong == 32768L) || (paramLong == 65536L) || (paramLong == 98304L) ? 1 : 0;
/*      */ 
/* 1362 */       if (i == 0) {
/* 1363 */         eventLog.fine("Assertion failed");
/*      */       }
/*      */     }
/*      */ 
/* 1367 */     if (paramInt == 0) {
/* 1368 */       return;
/*      */     }
/* 1370 */     if ((paramLong & 0x8000) != 0L) {
/* 1371 */       this.listeningChildren += paramInt;
/*      */     }
/* 1373 */     if ((paramLong & 0x10000) != 0L) {
/* 1374 */       this.listeningBoundsChildren += paramInt;
/*      */     }
/*      */ 
/* 1377 */     adjustListeningChildrenOnParent(paramLong, paramInt);
/*      */   }
/*      */ 
/*      */   void adjustDescendants(int paramInt)
/*      */   {
/* 1382 */     if (paramInt == 0) {
/* 1383 */       return;
/*      */     }
/* 1385 */     this.descendantsCount += paramInt;
/* 1386 */     adjustDecendantsOnParent(paramInt);
/*      */   }
/*      */ 
/*      */   void adjustDecendantsOnParent(int paramInt)
/*      */   {
/* 1391 */     if (this.parent != null)
/* 1392 */       this.parent.adjustDescendants(paramInt);
/*      */   }
/*      */ 
/*      */   int countHierarchyMembers()
/*      */   {
/* 1398 */     if (log.isLoggable(500))
/*      */     {
/* 1400 */       int i = 0;
/* 1401 */       for (Component localComponent : this.component) {
/* 1402 */         i += localComponent.countHierarchyMembers();
/*      */       }
/* 1404 */       if (this.descendantsCount != i) {
/* 1405 */         log.fine("Assertion (descendantsCount == sum) failed");
/*      */       }
/*      */     }
/* 1408 */     return this.descendantsCount + 1;
/*      */   }
/*      */ 
/*      */   private int getListenersCount(int paramInt, boolean paramBoolean) {
/* 1412 */     checkTreeLock();
/* 1413 */     if (paramBoolean) {
/* 1414 */       return this.descendantsCount;
/*      */     }
/* 1416 */     switch (paramInt) {
/*      */     case 1400:
/* 1418 */       return this.listeningChildren;
/*      */     case 1401:
/*      */     case 1402:
/* 1421 */       return this.listeningBoundsChildren;
/*      */     }
/* 1423 */     return 0;
/*      */   }
/*      */ 
/*      */   final int createHierarchyEvents(int paramInt, Component paramComponent, Container paramContainer, long paramLong, boolean paramBoolean)
/*      */   {
/* 1430 */     checkTreeLock();
/* 1431 */     int i = getListenersCount(paramInt, paramBoolean);
/*      */ 
/* 1433 */     int j = i; for (int k = 0; j > 0; k++) {
/* 1434 */       j -= ((Component)this.component.get(k)).createHierarchyEvents(paramInt, paramComponent, paramContainer, paramLong, paramBoolean);
/*      */     }
/*      */ 
/* 1437 */     return i + super.createHierarchyEvents(paramInt, paramComponent, paramContainer, paramLong, paramBoolean);
/*      */   }
/*      */ 
/*      */   final void createChildHierarchyEvents(int paramInt, long paramLong, boolean paramBoolean)
/*      */   {
/* 1445 */     checkTreeLock();
/* 1446 */     if (this.component.isEmpty()) {
/* 1447 */       return;
/*      */     }
/* 1449 */     int i = getListenersCount(paramInt, paramBoolean);
/*      */ 
/* 1451 */     int j = i; for (int k = 0; j > 0; k++)
/* 1452 */       j -= ((Component)this.component.get(k)).createHierarchyEvents(paramInt, this, this.parent, paramLong, paramBoolean);
/*      */   }
/*      */ 
/*      */   public LayoutManager getLayout()
/*      */   {
/* 1463 */     return this.layoutMgr;
/*      */   }
/*      */ 
/*      */   public void setLayout(LayoutManager paramLayoutManager)
/*      */   {
/* 1478 */     this.layoutMgr = paramLayoutManager;
/* 1479 */     invalidateIfValid();
/*      */   }
/*      */ 
/*      */   public void doLayout()
/*      */   {
/* 1492 */     layout();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void layout()
/*      */   {
/* 1501 */     LayoutManager localLayoutManager = this.layoutMgr;
/* 1502 */     if (localLayoutManager != null)
/* 1503 */       localLayoutManager.layoutContainer(this);
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/* 1537 */     return false;
/*      */   }
/*      */ 
/*      */   void invalidateParent()
/*      */   {
/* 1553 */     if ((!isJavaAwtSmartInvalidate) || (!isValidateRoot()))
/* 1554 */       super.invalidateParent();
/*      */   }
/*      */ 
/*      */   public void invalidate()
/*      */   {
/* 1575 */     LayoutManager localLayoutManager = this.layoutMgr;
/* 1576 */     if ((localLayoutManager instanceof LayoutManager2)) {
/* 1577 */       LayoutManager2 localLayoutManager2 = (LayoutManager2)localLayoutManager;
/* 1578 */       localLayoutManager2.invalidateLayout(this);
/*      */     }
/* 1580 */     super.invalidate();
/*      */   }
/*      */ 
/*      */   public void validate()
/*      */   {
/* 1611 */     boolean bool = false;
/* 1612 */     synchronized (getTreeLock()) {
/* 1613 */       if (((!isValid()) || (descendUnconditionallyWhenValidating)) && (this.peer != null))
/*      */       {
/* 1616 */         ContainerPeer localContainerPeer = null;
/* 1617 */         if ((this.peer instanceof ContainerPeer)) {
/* 1618 */           localContainerPeer = (ContainerPeer)this.peer;
/*      */         }
/* 1620 */         if (localContainerPeer != null) {
/* 1621 */           localContainerPeer.beginValidate();
/*      */         }
/* 1623 */         validateTree();
/* 1624 */         if (localContainerPeer != null) {
/* 1625 */           localContainerPeer.endValidate();
/*      */ 
/* 1628 */           if (!descendUnconditionallyWhenValidating) {
/* 1629 */             bool = isVisible();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1634 */     if (bool)
/* 1635 */       updateCursorImmediately();
/*      */   }
/*      */ 
/*      */   final void validateUnconditionally()
/*      */   {
/* 1656 */     boolean bool = false;
/* 1657 */     synchronized (getTreeLock()) {
/* 1658 */       descendUnconditionallyWhenValidating = true;
/*      */ 
/* 1660 */       validate();
/* 1661 */       if ((this.peer instanceof ContainerPeer)) {
/* 1662 */         bool = isVisible();
/*      */       }
/*      */ 
/* 1665 */       descendUnconditionallyWhenValidating = false;
/*      */     }
/* 1667 */     if (bool)
/* 1668 */       updateCursorImmediately();
/*      */   }
/*      */ 
/*      */   protected void validateTree()
/*      */   {
/* 1682 */     checkTreeLock();
/* 1683 */     if ((!isValid()) || (descendUnconditionallyWhenValidating)) {
/* 1684 */       if ((this.peer instanceof ContainerPeer)) {
/* 1685 */         ((ContainerPeer)this.peer).beginLayout();
/*      */       }
/* 1687 */       if (!isValid()) {
/* 1688 */         doLayout();
/*      */       }
/* 1690 */       for (int i = 0; i < this.component.size(); i++) {
/* 1691 */         Component localComponent = (Component)this.component.get(i);
/* 1692 */         if (((localComponent instanceof Container)) && (!(localComponent instanceof Window)) && ((!localComponent.isValid()) || (descendUnconditionallyWhenValidating)))
/*      */         {
/* 1697 */           ((Container)localComponent).validateTree();
/*      */         }
/* 1699 */         else localComponent.validate();
/*      */       }
/*      */ 
/* 1702 */       if ((this.peer instanceof ContainerPeer)) {
/* 1703 */         ((ContainerPeer)this.peer).endLayout();
/*      */       }
/*      */     }
/* 1706 */     super.validate();
/*      */   }
/*      */ 
/*      */   void invalidateTree()
/*      */   {
/* 1714 */     synchronized (getTreeLock()) {
/* 1715 */       for (int i = 0; i < this.component.size(); i++) {
/* 1716 */         Component localComponent = (Component)this.component.get(i);
/* 1717 */         if ((localComponent instanceof Container)) {
/* 1718 */           ((Container)localComponent).invalidateTree();
/*      */         }
/*      */         else {
/* 1721 */           localComponent.invalidateIfValid();
/*      */         }
/*      */       }
/* 1724 */       invalidateIfValid();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/* 1740 */     int i = 0;
/*      */ 
/* 1742 */     Font localFont1 = getFont();
/* 1743 */     super.setFont(paramFont);
/* 1744 */     Font localFont2 = getFont();
/* 1745 */     if ((localFont2 != localFont1) && ((localFont1 == null) || (!localFont1.equals(localFont2))))
/*      */     {
/* 1747 */       invalidateTree();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/* 1773 */     return preferredSize();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension preferredSize()
/*      */   {
/* 1785 */     Dimension localDimension = this.prefSize;
/* 1786 */     if ((localDimension == null) || ((!isPreferredSizeSet()) && (!isValid()))) {
/* 1787 */       synchronized (getTreeLock()) {
/* 1788 */         this.prefSize = (this.layoutMgr != null ? this.layoutMgr.preferredLayoutSize(this) : super.preferredSize());
/*      */ 
/* 1791 */         localDimension = this.prefSize;
/*      */       }
/*      */     }
/* 1794 */     if (localDimension != null) {
/* 1795 */       return new Dimension(localDimension);
/*      */     }
/*      */ 
/* 1798 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/* 1825 */     return minimumSize();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension minimumSize()
/*      */   {
/* 1837 */     Dimension localDimension = this.minSize;
/* 1838 */     if ((localDimension == null) || ((!isMinimumSizeSet()) && (!isValid()))) {
/* 1839 */       synchronized (getTreeLock()) {
/* 1840 */         this.minSize = (this.layoutMgr != null ? this.layoutMgr.minimumLayoutSize(this) : super.minimumSize());
/*      */ 
/* 1843 */         localDimension = this.minSize;
/*      */       }
/*      */     }
/* 1846 */     if (localDimension != null) {
/* 1847 */       return new Dimension(localDimension);
/*      */     }
/*      */ 
/* 1850 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize()
/*      */   {
/* 1880 */     Dimension localDimension = this.maxSize;
/* 1881 */     if ((localDimension == null) || ((!isMaximumSizeSet()) && (!isValid()))) {
/* 1882 */       synchronized (getTreeLock()) {
/* 1883 */         if ((this.layoutMgr instanceof LayoutManager2)) {
/* 1884 */           LayoutManager2 localLayoutManager2 = (LayoutManager2)this.layoutMgr;
/* 1885 */           this.maxSize = localLayoutManager2.maximumLayoutSize(this);
/*      */         } else {
/* 1887 */           this.maxSize = super.getMaximumSize();
/*      */         }
/* 1889 */         localDimension = this.maxSize;
/*      */       }
/*      */     }
/* 1892 */     if (localDimension != null) {
/* 1893 */       return new Dimension(localDimension);
/*      */     }
/*      */ 
/* 1896 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public float getAlignmentX()
/*      */   {
/*      */     float f;
/* 1909 */     if ((this.layoutMgr instanceof LayoutManager2))
/* 1910 */       synchronized (getTreeLock()) {
/* 1911 */         LayoutManager2 localLayoutManager2 = (LayoutManager2)this.layoutMgr;
/* 1912 */         f = localLayoutManager2.getLayoutAlignmentX(this);
/*      */       }
/*      */     else {
/* 1915 */       f = super.getAlignmentX();
/*      */     }
/* 1917 */     return f;
/*      */   }
/*      */ 
/*      */   public float getAlignmentY()
/*      */   {
/*      */     float f;
/* 1929 */     if ((this.layoutMgr instanceof LayoutManager2))
/* 1930 */       synchronized (getTreeLock()) {
/* 1931 */         LayoutManager2 localLayoutManager2 = (LayoutManager2)this.layoutMgr;
/* 1932 */         f = localLayoutManager2.getLayoutAlignmentY(this);
/*      */       }
/*      */     else {
/* 1935 */       f = super.getAlignmentY();
/*      */     }
/* 1937 */     return f;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/* 1952 */     if (isShowing()) {
/* 1953 */       synchronized (getObjectLock()) {
/* 1954 */         if ((this.printing) && 
/* 1955 */           (this.printingThreads.contains(Thread.currentThread()))) {
/* 1956 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1967 */       GraphicsCallback.PaintCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics)
/*      */   {
/* 1984 */     if (isShowing()) {
/* 1985 */       if (!(this.peer instanceof LightweightPeer)) {
/* 1986 */         paramGraphics.clearRect(0, 0, this.width, this.height);
/*      */       }
/* 1988 */       paint(paramGraphics);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void print(Graphics paramGraphics)
/*      */   {
/* 2004 */     if (isShowing()) {
/* 2005 */       Thread localThread = Thread.currentThread();
/*      */       try {
/* 2007 */         synchronized (getObjectLock()) {
/* 2008 */           if (this.printingThreads == null) {
/* 2009 */             this.printingThreads = new HashSet();
/*      */           }
/* 2011 */           this.printingThreads.add(localThread);
/* 2012 */           this.printing = true;
/*      */         }
/* 2014 */         super.print(paramGraphics);
/*      */       } finally {
/* 2016 */         synchronized (getObjectLock()) {
/* 2017 */           this.printingThreads.remove(localThread);
/* 2018 */           this.printing = (!this.printingThreads.isEmpty());
/*      */         }
/*      */       }
/*      */ 
/* 2022 */       GraphicsCallback.PrintCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintComponents(Graphics paramGraphics)
/*      */   {
/* 2034 */     if (isShowing())
/* 2035 */       GraphicsCallback.PaintAllCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 4);
/*      */   }
/*      */ 
/*      */   void lightweightPaint(Graphics paramGraphics)
/*      */   {
/* 2048 */     super.lightweightPaint(paramGraphics);
/* 2049 */     paintHeavyweightComponents(paramGraphics);
/*      */   }
/*      */ 
/*      */   void paintHeavyweightComponents(Graphics paramGraphics)
/*      */   {
/* 2056 */     if (isShowing())
/* 2057 */       GraphicsCallback.PaintHeavyweightComponentsCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 3);
/*      */   }
/*      */ 
/*      */   public void printComponents(Graphics paramGraphics)
/*      */   {
/* 2070 */     if (isShowing())
/* 2071 */       GraphicsCallback.PrintAllCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 4);
/*      */   }
/*      */ 
/*      */   void lightweightPrint(Graphics paramGraphics)
/*      */   {
/* 2084 */     super.lightweightPrint(paramGraphics);
/* 2085 */     printHeavyweightComponents(paramGraphics);
/*      */   }
/*      */ 
/*      */   void printHeavyweightComponents(Graphics paramGraphics)
/*      */   {
/* 2092 */     if (isShowing())
/* 2093 */       GraphicsCallback.PrintHeavyweightComponentsCallback.getInstance().runComponents(getComponentsSync(), paramGraphics, 3);
/*      */   }
/*      */ 
/*      */   public synchronized void addContainerListener(ContainerListener paramContainerListener)
/*      */   {
/* 2112 */     if (paramContainerListener == null) {
/* 2113 */       return;
/*      */     }
/* 2115 */     this.containerListener = AWTEventMulticaster.add(this.containerListener, paramContainerListener);
/* 2116 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeContainerListener(ContainerListener paramContainerListener)
/*      */   {
/* 2132 */     if (paramContainerListener == null) {
/* 2133 */       return;
/*      */     }
/* 2135 */     this.containerListener = AWTEventMulticaster.remove(this.containerListener, paramContainerListener);
/*      */   }
/*      */ 
/*      */   public synchronized ContainerListener[] getContainerListeners()
/*      */   {
/* 2151 */     return (ContainerListener[])getListeners(ContainerListener.class);
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/* 2189 */     ContainerListener localContainerListener = null;
/* 2190 */     if (paramClass == ContainerListener.class)
/* 2191 */       localContainerListener = this.containerListener;
/*      */     else {
/* 2193 */       return super.getListeners(paramClass);
/*      */     }
/* 2195 */     return AWTEventMulticaster.getListeners(localContainerListener, paramClass);
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/* 2200 */     int i = paramAWTEvent.getID();
/*      */ 
/* 2202 */     if ((i == 300) || (i == 301))
/*      */     {
/* 2204 */       if (((this.eventMask & 0x2) != 0L) || (this.containerListener != null))
/*      */       {
/* 2206 */         return true;
/*      */       }
/* 2208 */       return false;
/*      */     }
/* 2210 */     return super.eventEnabled(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 2225 */     if ((paramAWTEvent instanceof ContainerEvent)) {
/* 2226 */       processContainerEvent((ContainerEvent)paramAWTEvent);
/* 2227 */       return;
/*      */     }
/* 2229 */     super.processEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processContainerEvent(ContainerEvent paramContainerEvent)
/*      */   {
/* 2251 */     ContainerListener localContainerListener = this.containerListener;
/* 2252 */     if (localContainerListener != null)
/* 2253 */       switch (paramContainerEvent.getID()) {
/*      */       case 300:
/* 2255 */         localContainerListener.componentAdded(paramContainerEvent);
/* 2256 */         break;
/*      */       case 301:
/* 2258 */         localContainerListener.componentRemoved(paramContainerEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   void dispatchEventImpl(AWTEvent paramAWTEvent)
/*      */   {
/* 2273 */     if ((this.dispatcher != null) && (this.dispatcher.dispatchEvent(paramAWTEvent)))
/*      */     {
/* 2280 */       paramAWTEvent.consume();
/* 2281 */       if (this.peer != null) {
/* 2282 */         this.peer.handleEvent(paramAWTEvent);
/*      */       }
/* 2284 */       return;
/*      */     }
/*      */ 
/* 2287 */     super.dispatchEventImpl(paramAWTEvent);
/*      */ 
/* 2289 */     synchronized (getTreeLock()) {
/* 2290 */       switch (paramAWTEvent.getID()) {
/*      */       case 101:
/* 2292 */         createChildHierarchyEvents(1402, 0L, Toolkit.enabledOnToolkit(65536L));
/*      */ 
/* 2294 */         break;
/*      */       case 100:
/* 2296 */         createChildHierarchyEvents(1401, 0L, Toolkit.enabledOnToolkit(65536L));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void dispatchEventToSelf(AWTEvent paramAWTEvent)
/*      */   {
/* 2311 */     super.dispatchEventImpl(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   Component getMouseEventTarget(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2319 */     return getMouseEventTarget(paramInt1, paramInt2, paramBoolean, MouseEventTargetFilter.FILTER, false);
/*      */   }
/*      */ 
/*      */   Component getDropTargetEventTarget(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2328 */     return getMouseEventTarget(paramInt1, paramInt2, paramBoolean, DropTargetEventTargetFilter.FILTER, true);
/*      */   }
/*      */ 
/*      */   private Component getMouseEventTarget(int paramInt1, int paramInt2, boolean paramBoolean1, EventTargetFilter paramEventTargetFilter, boolean paramBoolean2)
/*      */   {
/* 2348 */     Component localComponent = null;
/* 2349 */     if (paramBoolean2) {
/* 2350 */       localComponent = getMouseEventTargetImpl(paramInt1, paramInt2, paramBoolean1, paramEventTargetFilter, true, paramBoolean2);
/*      */     }
/*      */ 
/* 2355 */     if ((localComponent == null) || (localComponent == this)) {
/* 2356 */       localComponent = getMouseEventTargetImpl(paramInt1, paramInt2, paramBoolean1, paramEventTargetFilter, false, paramBoolean2);
/*      */     }
/*      */ 
/* 2361 */     return localComponent;
/*      */   }
/*      */ 
/*      */   private Component getMouseEventTargetImpl(int paramInt1, int paramInt2, boolean paramBoolean1, EventTargetFilter paramEventTargetFilter, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 2390 */     synchronized (getTreeLock())
/*      */     {
/* 2392 */       for (int i = 0; i < this.component.size(); i++) {
/* 2393 */         Component localComponent1 = (Component)this.component.get(i);
/* 2394 */         if ((localComponent1 != null) && (localComponent1.visible) && (((!paramBoolean2) && ((localComponent1.peer instanceof LightweightPeer))) || ((paramBoolean2) && (!(localComponent1.peer instanceof LightweightPeer)) && (localComponent1.contains(paramInt1 - localComponent1.x, paramInt2 - localComponent1.y)))))
/*      */         {
/* 2403 */           if ((localComponent1 instanceof Container)) {
/* 2404 */             Container localContainer = (Container)localComponent1;
/* 2405 */             Component localComponent2 = localContainer.getMouseEventTarget(paramInt1 - localContainer.x, paramInt2 - localContainer.y, paramBoolean1, paramEventTargetFilter, paramBoolean3);
/*      */ 
/* 2411 */             if (localComponent2 != null) {
/* 2412 */               return localComponent2;
/*      */             }
/*      */           }
/* 2415 */           else if (paramEventTargetFilter.accept(localComponent1))
/*      */           {
/* 2418 */             return localComponent1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2427 */       i = ((this.peer instanceof LightweightPeer)) || (paramBoolean1) ? 1 : 0;
/* 2428 */       boolean bool = contains(paramInt1, paramInt2);
/*      */ 
/* 2432 */       if ((bool) && (i != 0) && (paramEventTargetFilter.accept(this))) {
/* 2433 */         return this;
/*      */       }
/*      */ 
/* 2436 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void proxyEnableEvents(long paramLong)
/*      */   {
/* 2478 */     if ((this.peer instanceof LightweightPeer))
/*      */     {
/* 2481 */       if (this.parent != null) {
/* 2482 */         this.parent.proxyEnableEvents(paramLong);
/*      */       }
/*      */ 
/*      */     }
/* 2490 */     else if (this.dispatcher != null)
/* 2491 */       this.dispatcher.enableEvents(paramLong);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void deliverEvent(Event paramEvent)
/*      */   {
/* 2502 */     Component localComponent = getComponentAt(paramEvent.x, paramEvent.y);
/* 2503 */     if ((localComponent != null) && (localComponent != this)) {
/* 2504 */       paramEvent.translate(-localComponent.x, -localComponent.y);
/* 2505 */       localComponent.deliverEvent(paramEvent);
/*      */     } else {
/* 2507 */       postEvent(paramEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Component getComponentAt(int paramInt1, int paramInt2)
/*      */   {
/* 2530 */     return locate(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Component locate(int paramInt1, int paramInt2)
/*      */   {
/* 2539 */     if (!contains(paramInt1, paramInt2)) {
/* 2540 */       return null;
/*      */     }
/* 2542 */     synchronized (getTreeLock())
/*      */     {
/*      */       Component localComponent;
/* 2544 */       for (int i = 0; i < this.component.size(); i++) {
/* 2545 */         localComponent = (Component)this.component.get(i);
/* 2546 */         if ((localComponent != null) && (!(localComponent.peer instanceof LightweightPeer)))
/*      */         {
/* 2548 */           if (localComponent.contains(paramInt1 - localComponent.x, paramInt2 - localComponent.y)) {
/* 2549 */             return localComponent;
/*      */           }
/*      */         }
/*      */       }
/* 2553 */       for (i = 0; i < this.component.size(); i++) {
/* 2554 */         localComponent = (Component)this.component.get(i);
/* 2555 */         if ((localComponent != null) && ((localComponent.peer instanceof LightweightPeer)))
/*      */         {
/* 2557 */           if (localComponent.contains(paramInt1 - localComponent.x, paramInt2 - localComponent.y)) {
/* 2558 */             return localComponent;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2563 */     return this;
/*      */   }
/*      */ 
/*      */   public Component getComponentAt(Point paramPoint)
/*      */   {
/* 2576 */     return getComponentAt(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public Point getMousePosition(boolean paramBoolean)
/*      */     throws HeadlessException
/*      */   {
/* 2599 */     if (GraphicsEnvironment.isHeadless()) {
/* 2600 */       throw new HeadlessException();
/*      */     }
/* 2602 */     PointerInfo localPointerInfo = (PointerInfo)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 2605 */         return MouseInfo.getPointerInfo();
/*      */       }
/*      */     });
/* 2609 */     synchronized (getTreeLock()) {
/* 2610 */       Component localComponent = findUnderMouseInWindow(localPointerInfo);
/* 2611 */       if (isSameOrAncestorOf(localComponent, paramBoolean)) {
/* 2612 */         return pointRelativeToComponent(localPointerInfo.getLocation());
/*      */       }
/* 2614 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isSameOrAncestorOf(Component paramComponent, boolean paramBoolean) {
/* 2619 */     return (this == paramComponent) || ((paramBoolean) && (isParentOf(paramComponent)));
/*      */   }
/*      */ 
/*      */   public Component findComponentAt(int paramInt1, int paramInt2)
/*      */   {
/* 2646 */     return findComponentAt(paramInt1, paramInt2, true);
/*      */   }
/*      */ 
/*      */   final Component findComponentAt(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 2660 */     synchronized (getTreeLock()) {
/* 2661 */       if (isRecursivelyVisible()) {
/* 2662 */         return findComponentAtImpl(paramInt1, paramInt2, paramBoolean);
/*      */       }
/*      */     }
/* 2665 */     return null;
/*      */   }
/*      */ 
/*      */   final Component findComponentAtImpl(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 2669 */     checkTreeLock();
/*      */ 
/* 2671 */     if ((!contains(paramInt1, paramInt2)) || (!this.visible) || ((!paramBoolean) && (!this.enabled)))
/* 2672 */       return null;
/*      */     Component localComponent;
/* 2676 */     for (int i = 0; i < this.component.size(); i++) {
/* 2677 */       localComponent = (Component)this.component.get(i);
/* 2678 */       if ((localComponent != null) && (!(localComponent.peer instanceof LightweightPeer)))
/*      */       {
/* 2680 */         if ((localComponent instanceof Container)) {
/* 2681 */           localComponent = ((Container)localComponent).findComponentAtImpl(paramInt1 - localComponent.x, paramInt2 - localComponent.y, paramBoolean);
/*      */         }
/*      */         else
/*      */         {
/* 2685 */           localComponent = localComponent.locate(paramInt1 - localComponent.x, paramInt2 - localComponent.y);
/*      */         }
/* 2687 */         if ((localComponent != null) && (localComponent.visible) && ((paramBoolean) || (localComponent.enabled)))
/*      */         {
/* 2690 */           return localComponent;
/*      */         }
/*      */       }
/*      */     }
/* 2694 */     for (i = 0; i < this.component.size(); i++) {
/* 2695 */       localComponent = (Component)this.component.get(i);
/* 2696 */       if ((localComponent != null) && ((localComponent.peer instanceof LightweightPeer)))
/*      */       {
/* 2698 */         if ((localComponent instanceof Container)) {
/* 2699 */           localComponent = ((Container)localComponent).findComponentAtImpl(paramInt1 - localComponent.x, paramInt2 - localComponent.y, paramBoolean);
/*      */         }
/*      */         else
/*      */         {
/* 2703 */           localComponent = localComponent.locate(paramInt1 - localComponent.x, paramInt2 - localComponent.y);
/*      */         }
/* 2705 */         if ((localComponent != null) && (localComponent.visible) && ((paramBoolean) || (localComponent.enabled)))
/*      */         {
/* 2708 */           return localComponent;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2713 */     return this;
/*      */   }
/*      */ 
/*      */   public Component findComponentAt(Point paramPoint)
/*      */   {
/* 2740 */     return findComponentAt(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/* 2753 */     synchronized (getTreeLock())
/*      */     {
/* 2758 */       super.addNotify();
/* 2759 */       if (!(this.peer instanceof LightweightPeer)) {
/* 2760 */         this.dispatcher = new LightweightDispatcher(this);
/*      */       }
/*      */ 
/* 2768 */       for (int i = 0; i < this.component.size(); i++)
/* 2769 */         ((Component)this.component.get(i)).addNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 2784 */     synchronized (getTreeLock())
/*      */     {
/* 2790 */       for (int i = this.component.size() - 1; i >= 0; i--) {
/* 2791 */         Component localComponent = (Component)this.component.get(i);
/* 2792 */         if (localComponent != null)
/*      */         {
/* 2799 */           localComponent.setAutoFocusTransferOnDisposal(false);
/* 2800 */           localComponent.removeNotify();
/* 2801 */           localComponent.setAutoFocusTransferOnDisposal(true);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2807 */       if ((containsFocus()) && (KeyboardFocusManager.isAutoFocusTransferEnabledFor(this)) && 
/* 2808 */         (!transferFocus(false))) {
/* 2809 */         transferFocusBackward(true);
/*      */       }
/*      */ 
/* 2812 */       if (this.dispatcher != null) {
/* 2813 */         this.dispatcher.dispose();
/* 2814 */         this.dispatcher = null;
/*      */       }
/* 2816 */       super.removeNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isAncestorOf(Component paramComponent)
/*      */   {
/*      */     Container localContainer;
/* 2830 */     if ((paramComponent == null) || ((localContainer = paramComponent.getParent()) == null)) {
/* 2831 */       return false;
/*      */     }
/* 2833 */     while (localContainer != null) {
/* 2834 */       if (localContainer == this) {
/* 2835 */         return true;
/*      */       }
/* 2837 */       localContainer = localContainer.getParent();
/*      */     }
/* 2839 */     return false;
/*      */   }
/*      */ 
/*      */   private void startLWModal()
/*      */   {
/* 2862 */     this.modalAppContext = AppContext.getAppContext();
/*      */ 
/* 2866 */     Toolkit.getEventQueue(); long l = EventQueue.getMostRecentEventTime();
/* 2867 */     Component localComponent = Component.isInstanceOf(this, "javax.swing.JInternalFrame") ? ((JInternalFrame)this).getMostRecentFocusOwner() : null;
/* 2868 */     if (localComponent != null)
/* 2869 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().enqueueKeyEvents(l, localComponent);
/*      */     final Container localContainer;
/* 2876 */     synchronized (getTreeLock()) {
/* 2877 */       localContainer = getHeavyweightContainer();
/* 2878 */       if (localContainer.modalComp != null) {
/* 2879 */         this.modalComp = localContainer.modalComp;
/* 2880 */         localContainer.modalComp = this;
/* 2881 */         return;
/*      */       }
/*      */ 
/* 2884 */       localContainer.modalComp = this;
/*      */     }
/*      */ 
/* 2888 */     ??? = new Runnable() {
/*      */       public void run() {
/* 2890 */         EventDispatchThread localEventDispatchThread = (EventDispatchThread)Thread.currentThread();
/*      */ 
/* 2892 */         localEventDispatchThread.pumpEventsForHierarchy(new Conditional()
/*      */         {
/*      */           public boolean evaluate() {
/* 2895 */             return (Container.this.windowClosingException == null) && (Container.3.this.val$nativeContainer.modalComp != null);
/*      */           }
/*      */         }
/*      */         , Container.this);
/*      */       }
/*      */     };
/* 2901 */     if (EventQueue.isDispatchThread()) {
/* 2902 */       SequencedEvent localSequencedEvent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent();
/*      */ 
/* 2905 */       if (localSequencedEvent != null) {
/* 2906 */         localSequencedEvent.dispose();
/*      */       }
/*      */ 
/* 2909 */       ((Runnable)???).run();
/*      */     } else {
/* 2911 */       synchronized (getTreeLock()) {
/* 2912 */         Toolkit.getEventQueue().postEvent(new PeerEvent(this, (Runnable)???, 1L));
/*      */         while (true)
/*      */         {
/* 2916 */           if ((this.windowClosingException == null) && (localContainer.modalComp != null))
/*      */           {
/*      */             try
/*      */             {
/* 2920 */               getTreeLock().wait();
/*      */             } catch (InterruptedException localInterruptedException) {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2927 */     if (this.windowClosingException != null) {
/* 2928 */       this.windowClosingException.fillInStackTrace();
/* 2929 */       throw this.windowClosingException;
/*      */     }
/* 2931 */     if (localComponent != null)
/* 2932 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().dequeueKeyEvents(l, localComponent);
/*      */   }
/*      */ 
/*      */   private void stopLWModal()
/*      */   {
/* 2938 */     synchronized (getTreeLock()) {
/* 2939 */       if (this.modalAppContext != null) {
/* 2940 */         Container localContainer = getHeavyweightContainer();
/* 2941 */         if (localContainer != null) {
/* 2942 */           if (this.modalComp != null) {
/* 2943 */             localContainer.modalComp = this.modalComp;
/* 2944 */             this.modalComp = null;
/* 2945 */             return;
/*      */           }
/*      */ 
/* 2948 */           localContainer.modalComp = null;
/*      */         }
/*      */ 
/* 2953 */         SunToolkit.postEvent(this.modalAppContext, new PeerEvent(this, new WakingRunnable(), 1L));
/*      */       }
/*      */ 
/* 2958 */       EventQueue.invokeLater(new WakingRunnable());
/* 2959 */       getTreeLock().notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 2980 */     String str = super.paramString();
/* 2981 */     LayoutManager localLayoutManager = this.layoutMgr;
/* 2982 */     if (localLayoutManager != null) {
/* 2983 */       str = str + ",layout=" + localLayoutManager.getClass().getName();
/*      */     }
/* 2985 */     return str;
/*      */   }
/*      */ 
/*      */   public void list(PrintStream paramPrintStream, int paramInt)
/*      */   {
/* 3004 */     super.list(paramPrintStream, paramInt);
/* 3005 */     synchronized (getTreeLock()) {
/* 3006 */       for (int i = 0; i < this.component.size(); i++) {
/* 3007 */         Component localComponent = (Component)this.component.get(i);
/* 3008 */         if (localComponent != null)
/* 3009 */           localComponent.list(paramPrintStream, paramInt + 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void list(PrintWriter paramPrintWriter, int paramInt)
/*      */   {
/* 3031 */     super.list(paramPrintWriter, paramInt);
/* 3032 */     synchronized (getTreeLock()) {
/* 3033 */       for (int i = 0; i < this.component.size(); i++) {
/* 3034 */         Component localComponent = (Component)this.component.get(i);
/* 3035 */         if (localComponent != null)
/* 3036 */           localComponent.list(paramPrintWriter, paramInt + 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFocusTraversalKeys(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*      */   {
/* 3123 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 3124 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/*      */ 
/* 3129 */     setFocusTraversalKeys_NoIDCheck(paramInt, paramSet);
/*      */   }
/*      */ 
/*      */   public Set<AWTKeyStroke> getFocusTraversalKeys(int paramInt)
/*      */   {
/* 3162 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 3163 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/*      */ 
/* 3168 */     return getFocusTraversalKeys_NoIDCheck(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean areFocusTraversalKeysSet(int paramInt)
/*      */   {
/* 3192 */     if ((paramInt < 0) || (paramInt >= 4)) {
/* 3193 */       throw new IllegalArgumentException("invalid focus traversal key identifier");
/*      */     }
/*      */ 
/* 3196 */     return (this.focusTraversalKeys != null) && (this.focusTraversalKeys[paramInt] != null);
/*      */   }
/*      */ 
/*      */   public boolean isFocusCycleRoot(Container paramContainer)
/*      */   {
/* 3216 */     if ((isFocusCycleRoot()) && (paramContainer == this)) {
/* 3217 */       return true;
/*      */     }
/* 3219 */     return super.isFocusCycleRoot(paramContainer);
/*      */   }
/*      */ 
/*      */   private Container findTraversalRoot()
/*      */   {
/* 3231 */     Container localContainer1 = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentFocusCycleRoot();
/*      */     Container localContainer2;
/* 3235 */     if (localContainer1 == this) {
/* 3236 */       localContainer2 = this;
/*      */     } else {
/* 3238 */       localContainer2 = getFocusCycleRootAncestor();
/* 3239 */       if (localContainer2 == null) {
/* 3240 */         localContainer2 = this;
/*      */       }
/*      */     }
/*      */ 
/* 3244 */     if (localContainer2 != localContainer1) {
/* 3245 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRoot(localContainer2);
/*      */     }
/*      */ 
/* 3248 */     return localContainer2;
/*      */   }
/*      */ 
/*      */   final boolean containsFocus() {
/* 3252 */     Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 3254 */     return isParentOf(localComponent);
/*      */   }
/*      */ 
/*      */   private boolean isParentOf(Component paramComponent)
/*      */   {
/* 3264 */     synchronized (getTreeLock()) {
/* 3265 */       while ((paramComponent != null) && (paramComponent != this) && (!(paramComponent instanceof Window))) {
/* 3266 */         paramComponent = paramComponent.getParent();
/*      */       }
/* 3268 */       return paramComponent == this;
/*      */     }
/*      */   }
/*      */ 
/*      */   void clearMostRecentFocusOwnerOnHide() {
/* 3273 */     int i = 0;
/* 3274 */     Window localWindow = null;
/*      */ 
/* 3276 */     synchronized (getTreeLock()) {
/* 3277 */       localWindow = getContainingWindow();
/* 3278 */       if (localWindow != null) {
/* 3279 */         Component localComponent1 = KeyboardFocusManager.getMostRecentFocusOwner(localWindow);
/* 3280 */         i = (localComponent1 == this) || (isParentOf(localComponent1)) ? 1 : 0;
/*      */ 
/* 3283 */         synchronized (KeyboardFocusManager.class) {
/* 3284 */           Component localComponent2 = localWindow.getTemporaryLostComponent();
/* 3285 */           if ((isParentOf(localComponent2)) || (localComponent2 == this)) {
/* 3286 */             localWindow.setTemporaryLostComponent(null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3292 */     if (i != 0)
/* 3293 */       KeyboardFocusManager.setMostRecentFocusOwner(localWindow, null);
/*      */   }
/*      */ 
/*      */   void clearCurrentFocusCycleRootOnHide()
/*      */   {
/* 3298 */     KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */ 
/* 3300 */     Container localContainer = localKeyboardFocusManager.getCurrentFocusCycleRoot();
/*      */ 
/* 3302 */     if ((localContainer == this) || (isParentOf(localContainer)))
/* 3303 */       localKeyboardFocusManager.setGlobalCurrentFocusCycleRoot(null);
/*      */   }
/*      */ 
/*      */   final Container getTraversalRoot()
/*      */   {
/* 3308 */     if (isFocusCycleRoot()) {
/* 3309 */       return findTraversalRoot();
/*      */     }
/*      */ 
/* 3312 */     return super.getTraversalRoot();
/*      */   }
/*      */ 
/*      */   public void setFocusTraversalPolicy(FocusTraversalPolicy paramFocusTraversalPolicy)
/*      */   {
/*      */     FocusTraversalPolicy localFocusTraversalPolicy;
/* 3338 */     synchronized (this) {
/* 3339 */       localFocusTraversalPolicy = this.focusTraversalPolicy;
/* 3340 */       this.focusTraversalPolicy = paramFocusTraversalPolicy;
/*      */     }
/* 3342 */     firePropertyChange("focusTraversalPolicy", localFocusTraversalPolicy, paramFocusTraversalPolicy);
/*      */   }
/*      */ 
/*      */   public FocusTraversalPolicy getFocusTraversalPolicy()
/*      */   {
/* 3360 */     if ((!isFocusTraversalPolicyProvider()) && (!isFocusCycleRoot())) {
/* 3361 */       return null;
/*      */     }
/*      */ 
/* 3364 */     FocusTraversalPolicy localFocusTraversalPolicy = this.focusTraversalPolicy;
/* 3365 */     if (localFocusTraversalPolicy != null) {
/* 3366 */       return localFocusTraversalPolicy;
/*      */     }
/*      */ 
/* 3369 */     Container localContainer = getFocusCycleRootAncestor();
/* 3370 */     if (localContainer != null) {
/* 3371 */       return localContainer.getFocusTraversalPolicy();
/*      */     }
/* 3373 */     return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy();
/*      */   }
/*      */ 
/*      */   public boolean isFocusTraversalPolicySet()
/*      */   {
/* 3388 */     return this.focusTraversalPolicy != null;
/*      */   }
/*      */ 
/*      */   public void setFocusCycleRoot(boolean paramBoolean)
/*      */   {
/*      */     boolean bool;
/* 3418 */     synchronized (this) {
/* 3419 */       bool = this.focusCycleRoot;
/* 3420 */       this.focusCycleRoot = paramBoolean;
/*      */     }
/* 3422 */     firePropertyChange("focusCycleRoot", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isFocusCycleRoot()
/*      */   {
/* 3444 */     return this.focusCycleRoot;
/*      */   }
/*      */ 
/*      */   public final void setFocusTraversalPolicyProvider(boolean paramBoolean)
/*      */   {
/*      */     boolean bool;
/* 3463 */     synchronized (this) {
/* 3464 */       bool = this.focusTraversalPolicyProvider;
/* 3465 */       this.focusTraversalPolicyProvider = paramBoolean;
/*      */     }
/* 3467 */     firePropertyChange("focusTraversalPolicyProvider", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public final boolean isFocusTraversalPolicyProvider()
/*      */   {
/* 3489 */     return this.focusTraversalPolicyProvider;
/*      */   }
/*      */ 
/*      */   public void transferFocusDownCycle()
/*      */   {
/* 3505 */     if (isFocusCycleRoot()) {
/* 3506 */       KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRoot(this);
/*      */ 
/* 3508 */       Component localComponent = getFocusTraversalPolicy().getDefaultComponent(this);
/*      */ 
/* 3510 */       if (localComponent != null)
/* 3511 */         localComponent.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_DOWN);
/*      */     }
/*      */   }
/*      */ 
/*      */   void preProcessKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 3517 */     Container localContainer = this.parent;
/* 3518 */     if (localContainer != null)
/* 3519 */       localContainer.preProcessKeyEvent(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   void postProcessKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 3524 */     Container localContainer = this.parent;
/* 3525 */     if (localContainer != null)
/* 3526 */       localContainer.postProcessKeyEvent(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   boolean postsOldMouseEvents()
/*      */   {
/* 3531 */     return true;
/*      */   }
/*      */ 
/*      */   public void applyComponentOrientation(ComponentOrientation paramComponentOrientation)
/*      */   {
/* 3550 */     super.applyComponentOrientation(paramComponentOrientation);
/* 3551 */     synchronized (getTreeLock()) {
/* 3552 */       for (int i = 0; i < this.component.size(); i++) {
/* 3553 */         Component localComponent = (Component)this.component.get(i);
/* 3554 */         localComponent.applyComponentOrientation(paramComponentOrientation);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 3593 */     super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 3634 */     super.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 3672 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 3673 */     localPutField.put("ncomponents", this.component.size());
/* 3674 */     localPutField.put("component", getComponentsSync());
/* 3675 */     localPutField.put("layoutMgr", this.layoutMgr);
/* 3676 */     localPutField.put("dispatcher", this.dispatcher);
/* 3677 */     localPutField.put("maxSize", this.maxSize);
/* 3678 */     localPutField.put("focusCycleRoot", this.focusCycleRoot);
/* 3679 */     localPutField.put("containerSerializedDataVersion", this.containerSerializedDataVersion);
/* 3680 */     localPutField.put("focusTraversalPolicyProvider", this.focusTraversalPolicyProvider);
/* 3681 */     paramObjectOutputStream.writeFields();
/*      */ 
/* 3683 */     AWTEventMulticaster.save(paramObjectOutputStream, "containerL", this.containerListener);
/* 3684 */     paramObjectOutputStream.writeObject(null);
/*      */ 
/* 3686 */     if ((this.focusTraversalPolicy instanceof Serializable))
/* 3687 */       paramObjectOutputStream.writeObject(this.focusTraversalPolicy);
/*      */     else
/* 3689 */       paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 3712 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 3713 */     Component[] arrayOfComponent = (Component[])localGetField.get("component", EMPTY_ARRAY);
/* 3714 */     int i = Integer.valueOf(localGetField.get("ncomponents", 0)).intValue();
/* 3715 */     this.component = new ArrayList(i);
/* 3716 */     for (int j = 0; j < i; j++) {
/* 3717 */       this.component.add(arrayOfComponent[j]);
/*      */     }
/* 3719 */     this.layoutMgr = ((LayoutManager)localGetField.get("layoutMgr", null));
/* 3720 */     this.dispatcher = ((LightweightDispatcher)localGetField.get("dispatcher", null));
/*      */ 
/* 3722 */     if (this.maxSize == null) {
/* 3723 */       this.maxSize = ((Dimension)localGetField.get("maxSize", null));
/*      */     }
/* 3725 */     this.focusCycleRoot = localGetField.get("focusCycleRoot", false);
/* 3726 */     this.containerSerializedDataVersion = localGetField.get("containerSerializedDataVersion", 1);
/* 3727 */     this.focusTraversalPolicyProvider = localGetField.get("focusTraversalPolicyProvider", false);
/* 3728 */     List localList = this.component;
/* 3729 */     for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Component)((Iterator)localObject1).next();
/* 3730 */       ((Component)localObject2).parent = this;
/* 3731 */       adjustListeningChildren(32768L, ((Component)localObject2).numListening(32768L));
/*      */ 
/* 3733 */       adjustListeningChildren(65536L, ((Component)localObject2).numListening(65536L));
/*      */ 
/* 3735 */       adjustDescendants(((Component)localObject2).countHierarchyMembers());
/*      */     }
/*      */     Object localObject2;
/* 3739 */     while (null != (localObject1 = paramObjectInputStream.readObject())) {
/* 3740 */       localObject2 = ((String)localObject1).intern();
/*      */ 
/* 3742 */       if ("containerL" == localObject2) {
/* 3743 */         addContainerListener((ContainerListener)paramObjectInputStream.readObject());
/*      */       }
/*      */       else {
/* 3746 */         paramObjectInputStream.readObject();
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 3751 */       localObject2 = paramObjectInputStream.readObject();
/* 3752 */       if ((localObject2 instanceof FocusTraversalPolicy)) {
/* 3753 */         this.focusTraversalPolicy = ((FocusTraversalPolicy)localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (OptionalDataException localOptionalDataException)
/*      */     {
/* 3762 */       if (!localOptionalDataException.eof)
/* 3763 */         throw localOptionalDataException;
/*      */     }
/*      */   }
/*      */ 
/*      */   Accessible getAccessibleAt(Point paramPoint)
/*      */   {
/* 3879 */     synchronized (getTreeLock())
/*      */     {
/*      */       Object localObject2;
/* 3880 */       if ((this instanceof Accessible)) {
/* 3881 */         localObject1 = (Accessible)this;
/* 3882 */         AccessibleContext localAccessibleContext = ((Accessible)localObject1).getAccessibleContext();
/* 3883 */         if (localAccessibleContext != null)
/*      */         {
/* 3886 */           int k = localAccessibleContext.getAccessibleChildrenCount();
/* 3887 */           for (int m = 0; m < k; m++) {
/* 3888 */             localObject1 = localAccessibleContext.getAccessibleChild(m);
/* 3889 */             if (localObject1 != null) {
/* 3890 */               localAccessibleContext = ((Accessible)localObject1).getAccessibleContext();
/* 3891 */               if (localAccessibleContext != null) {
/* 3892 */                 AccessibleComponent localAccessibleComponent = localAccessibleContext.getAccessibleComponent();
/* 3893 */                 if ((localAccessibleComponent != null) && (localAccessibleComponent.isShowing())) {
/* 3894 */                   localObject2 = localAccessibleComponent.getLocation();
/* 3895 */                   Point localPoint2 = new Point(paramPoint.x - ((Point)localObject2).x, paramPoint.y - ((Point)localObject2).y);
/*      */ 
/* 3897 */                   if (localAccessibleComponent.contains(localPoint2)) {
/* 3898 */                     return localObject1;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 3905 */         return (Accessible)this;
/*      */       }
/* 3907 */       Object localObject1 = this;
/* 3908 */       if (!contains(paramPoint.x, paramPoint.y)) {
/* 3909 */         localObject1 = null;
/*      */       } else {
/* 3911 */         int i = getComponentCount();
/* 3912 */         for (int j = 0; j < i; j++) {
/* 3913 */           localObject2 = getComponent(j);
/* 3914 */           if ((localObject2 != null) && (((Component)localObject2).isShowing())) {
/* 3915 */             Point localPoint1 = ((Component)localObject2).getLocation();
/* 3916 */             if (((Component)localObject2).contains(paramPoint.x - localPoint1.x, paramPoint.y - localPoint1.y)) {
/* 3917 */               localObject1 = localObject2;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 3922 */       if ((localObject1 instanceof Accessible)) {
/* 3923 */         return (Accessible)localObject1;
/*      */       }
/*      */ 
/* 3926 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   int getAccessibleChildrenCount()
/*      */   {
/* 3938 */     synchronized (getTreeLock()) {
/* 3939 */       int i = 0;
/* 3940 */       Component[] arrayOfComponent = getComponents();
/* 3941 */       for (int j = 0; j < arrayOfComponent.length; j++) {
/* 3942 */         if ((arrayOfComponent[j] instanceof Accessible)) {
/* 3943 */           i++;
/*      */         }
/*      */       }
/* 3946 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   Accessible getAccessibleChild(int paramInt)
/*      */   {
/* 3957 */     synchronized (getTreeLock()) {
/* 3958 */       Component[] arrayOfComponent = getComponents();
/* 3959 */       int i = 0;
/* 3960 */       for (int j = 0; j < arrayOfComponent.length; j++) {
/* 3961 */         if ((arrayOfComponent[j] instanceof Accessible)) {
/* 3962 */           if (i == paramInt) {
/* 3963 */             return (Accessible)arrayOfComponent[j];
/*      */           }
/* 3965 */           i++;
/*      */         }
/*      */       }
/*      */ 
/* 3969 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void increaseComponentCount(Component paramComponent)
/*      */   {
/* 3976 */     synchronized (getTreeLock()) {
/* 3977 */       if (!paramComponent.isDisplayable()) {
/* 3978 */         throw new IllegalStateException("Peer does not exist while invoking the increaseComponentCount() method");
/*      */       }
/*      */ 
/* 3983 */       int i = 0;
/* 3984 */       int j = 0;
/*      */ 
/* 3986 */       if ((paramComponent instanceof Container)) {
/* 3987 */         j = ((Container)paramComponent).numOfLWComponents;
/* 3988 */         i = ((Container)paramComponent).numOfHWComponents;
/*      */       }
/* 3990 */       if (paramComponent.isLightweight())
/* 3991 */         j++;
/*      */       else {
/* 3993 */         i++;
/*      */       }
/*      */ 
/* 3996 */       for (Container localContainer = this; localContainer != null; localContainer = localContainer.getContainer()) {
/* 3997 */         localContainer.numOfLWComponents += j;
/* 3998 */         localContainer.numOfHWComponents += i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void decreaseComponentCount(Component paramComponent) {
/* 4004 */     synchronized (getTreeLock()) {
/* 4005 */       if (!paramComponent.isDisplayable()) {
/* 4006 */         throw new IllegalStateException("Peer does not exist while invoking the decreaseComponentCount() method");
/*      */       }
/*      */ 
/* 4011 */       int i = 0;
/* 4012 */       int j = 0;
/*      */ 
/* 4014 */       if ((paramComponent instanceof Container)) {
/* 4015 */         j = ((Container)paramComponent).numOfLWComponents;
/* 4016 */         i = ((Container)paramComponent).numOfHWComponents;
/*      */       }
/* 4018 */       if (paramComponent.isLightweight())
/* 4019 */         j++;
/*      */       else {
/* 4021 */         i++;
/*      */       }
/*      */ 
/* 4024 */       for (Container localContainer = this; localContainer != null; localContainer = localContainer.getContainer()) {
/* 4025 */         localContainer.numOfLWComponents -= j;
/* 4026 */         localContainer.numOfHWComponents -= i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getTopmostComponentIndex() {
/* 4032 */     checkTreeLock();
/* 4033 */     if (getComponentCount() > 0) {
/* 4034 */       return 0;
/*      */     }
/* 4036 */     return -1;
/*      */   }
/*      */ 
/*      */   private int getBottommostComponentIndex() {
/* 4040 */     checkTreeLock();
/* 4041 */     if (getComponentCount() > 0) {
/* 4042 */       return getComponentCount() - 1;
/*      */     }
/* 4044 */     return -1;
/*      */   }
/*      */ 
/*      */   final Region getOpaqueShape()
/*      */   {
/* 4053 */     checkTreeLock();
/* 4054 */     if ((isLightweight()) && (isNonOpaqueForMixing()) && (hasLightweightDescendants()))
/*      */     {
/* 4057 */       Region localRegion = Region.EMPTY_REGION;
/* 4058 */       for (int i = 0; i < getComponentCount(); i++) {
/* 4059 */         Component localComponent = getComponent(i);
/* 4060 */         if ((localComponent.isLightweight()) && (localComponent.isShowing())) {
/* 4061 */           localRegion = localRegion.getUnion(localComponent.getOpaqueShape());
/*      */         }
/*      */       }
/* 4064 */       return localRegion.getIntersection(getNormalShape());
/*      */     }
/* 4066 */     return super.getOpaqueShape();
/*      */   }
/*      */ 
/*      */   final void recursiveSubtractAndApplyShape(Region paramRegion) {
/* 4070 */     recursiveSubtractAndApplyShape(paramRegion, getTopmostComponentIndex(), getBottommostComponentIndex());
/*      */   }
/*      */ 
/*      */   final void recursiveSubtractAndApplyShape(Region paramRegion, int paramInt) {
/* 4074 */     recursiveSubtractAndApplyShape(paramRegion, paramInt, getBottommostComponentIndex());
/*      */   }
/*      */ 
/*      */   final void recursiveSubtractAndApplyShape(Region paramRegion, int paramInt1, int paramInt2) {
/* 4078 */     checkTreeLock();
/* 4079 */     if (mixingLog.isLoggable(500)) {
/* 4080 */       mixingLog.fine("this = " + this + "; shape=" + paramRegion + "; fromZ=" + paramInt1 + "; toZ=" + paramInt2);
/*      */     }
/*      */ 
/* 4083 */     if (paramInt1 == -1) {
/* 4084 */       return;
/*      */     }
/* 4086 */     if (paramRegion.isEmpty()) {
/* 4087 */       return;
/*      */     }
/*      */ 
/* 4092 */     if ((getLayout() != null) && (!isValid())) {
/* 4093 */       return;
/*      */     }
/* 4095 */     for (int i = paramInt1; i <= paramInt2; i++) {
/* 4096 */       Component localComponent = getComponent(i);
/* 4097 */       if (!localComponent.isLightweight())
/* 4098 */         localComponent.subtractAndApplyShape(paramRegion);
/* 4099 */       else if (((localComponent instanceof Container)) && (((Container)localComponent).hasHeavyweightDescendants()) && (localComponent.isShowing()))
/*      */       {
/* 4101 */         ((Container)localComponent).recursiveSubtractAndApplyShape(paramRegion);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void recursiveApplyCurrentShape() {
/* 4107 */     recursiveApplyCurrentShape(getTopmostComponentIndex(), getBottommostComponentIndex());
/*      */   }
/*      */ 
/*      */   final void recursiveApplyCurrentShape(int paramInt) {
/* 4111 */     recursiveApplyCurrentShape(paramInt, getBottommostComponentIndex());
/*      */   }
/*      */ 
/*      */   final void recursiveApplyCurrentShape(int paramInt1, int paramInt2) {
/* 4115 */     checkTreeLock();
/* 4116 */     if (mixingLog.isLoggable(500)) {
/* 4117 */       mixingLog.fine("this = " + this + "; fromZ=" + paramInt1 + "; toZ=" + paramInt2);
/*      */     }
/*      */ 
/* 4120 */     if (paramInt1 == -1) {
/* 4121 */       return;
/*      */     }
/*      */ 
/* 4126 */     if ((getLayout() != null) && (!isValid())) {
/* 4127 */       return;
/*      */     }
/* 4129 */     for (int i = paramInt1; i <= paramInt2; i++) {
/* 4130 */       Component localComponent = getComponent(i);
/* 4131 */       if (!localComponent.isLightweight()) {
/* 4132 */         localComponent.applyCurrentShape();
/*      */       }
/* 4134 */       if (((localComponent instanceof Container)) && (((Container)localComponent).hasHeavyweightDescendants()))
/*      */       {
/* 4136 */         ((Container)localComponent).recursiveApplyCurrentShape();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void recursiveShowHeavyweightChildren() {
/* 4142 */     if ((!hasHeavyweightDescendants()) || (!isVisible())) {
/* 4143 */       return;
/*      */     }
/* 4145 */     for (int i = 0; i < getComponentCount(); i++) {
/* 4146 */       Component localComponent = getComponent(i);
/* 4147 */       if (localComponent.isLightweight()) {
/* 4148 */         if ((localComponent instanceof Container)) {
/* 4149 */           ((Container)localComponent).recursiveShowHeavyweightChildren();
/*      */         }
/*      */       }
/* 4152 */       else if (localComponent.isVisible()) {
/* 4153 */         ComponentPeer localComponentPeer = localComponent.getPeer();
/* 4154 */         if (localComponentPeer != null)
/* 4155 */           localComponentPeer.setVisible(true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void recursiveHideHeavyweightChildren()
/*      */   {
/* 4163 */     if (!hasHeavyweightDescendants()) {
/* 4164 */       return;
/*      */     }
/* 4166 */     for (int i = 0; i < getComponentCount(); i++) {
/* 4167 */       Component localComponent = getComponent(i);
/* 4168 */       if (localComponent.isLightweight()) {
/* 4169 */         if ((localComponent instanceof Container)) {
/* 4170 */           ((Container)localComponent).recursiveHideHeavyweightChildren();
/*      */         }
/*      */       }
/* 4173 */       else if (localComponent.isVisible()) {
/* 4174 */         ComponentPeer localComponentPeer = localComponent.getPeer();
/* 4175 */         if (localComponentPeer != null)
/* 4176 */           localComponentPeer.setVisible(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void recursiveRelocateHeavyweightChildren(Point paramPoint)
/*      */   {
/* 4184 */     for (int i = 0; i < getComponentCount(); i++) {
/* 4185 */       Component localComponent = getComponent(i);
/*      */       Object localObject;
/* 4186 */       if (localComponent.isLightweight()) {
/* 4187 */         if (((localComponent instanceof Container)) && (((Container)localComponent).hasHeavyweightDescendants()))
/*      */         {
/* 4190 */           localObject = new Point(paramPoint);
/* 4191 */           ((Point)localObject).translate(localComponent.getX(), localComponent.getY());
/* 4192 */           ((Container)localComponent).recursiveRelocateHeavyweightChildren((Point)localObject);
/*      */         }
/*      */       } else {
/* 4195 */         localObject = localComponent.getPeer();
/* 4196 */         if (localObject != null)
/* 4197 */           ((ComponentPeer)localObject).setBounds(paramPoint.x + localComponent.getX(), paramPoint.y + localComponent.getY(), localComponent.getWidth(), localComponent.getHeight(), 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean isRecursivelyVisibleUpToHeavyweightContainer()
/*      */   {
/* 4215 */     if (!isLightweight()) {
/* 4216 */       return true;
/*      */     }
/*      */ 
/* 4219 */     for (Container localContainer = this; 
/* 4220 */       (localContainer != null) && (localContainer.isLightweight()); 
/* 4221 */       localContainer = localContainer.getContainer())
/*      */     {
/* 4223 */       if (!localContainer.isVisible()) {
/* 4224 */         return false;
/*      */       }
/*      */     }
/* 4227 */     return true;
/*      */   }
/*      */ 
/*      */   void mixOnShowing()
/*      */   {
/* 4232 */     synchronized (getTreeLock()) {
/* 4233 */       if (mixingLog.isLoggable(500)) {
/* 4234 */         mixingLog.fine("this = " + this);
/*      */       }
/*      */ 
/* 4237 */       boolean bool = isLightweight();
/*      */ 
/* 4239 */       if ((bool) && (isRecursivelyVisibleUpToHeavyweightContainer())) {
/* 4240 */         recursiveShowHeavyweightChildren();
/*      */       }
/*      */ 
/* 4243 */       if (!isMixingNeeded()) {
/* 4244 */         return;
/*      */       }
/*      */ 
/* 4247 */       if ((!bool) || ((bool) && (hasHeavyweightDescendants()))) {
/* 4248 */         recursiveApplyCurrentShape();
/*      */       }
/*      */ 
/* 4251 */       super.mixOnShowing();
/*      */     }
/*      */   }
/*      */ 
/*      */   void mixOnHiding(boolean paramBoolean)
/*      */   {
/* 4257 */     synchronized (getTreeLock()) {
/* 4258 */       if (mixingLog.isLoggable(500)) {
/* 4259 */         mixingLog.fine("this = " + this + "; isLightweight=" + paramBoolean);
/*      */       }
/*      */ 
/* 4262 */       if (paramBoolean) {
/* 4263 */         recursiveHideHeavyweightChildren();
/*      */       }
/* 4265 */       super.mixOnHiding(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   void mixOnReshaping()
/*      */   {
/* 4271 */     synchronized (getTreeLock()) {
/* 4272 */       if (mixingLog.isLoggable(500)) {
/* 4273 */         mixingLog.fine("this = " + this);
/*      */       }
/*      */ 
/* 4276 */       boolean bool = isMixingNeeded();
/*      */ 
/* 4278 */       if ((isLightweight()) && (hasHeavyweightDescendants())) {
/* 4279 */         Point localPoint = new Point(getX(), getY());
/* 4280 */         for (Container localContainer = getContainer(); 
/* 4281 */           (localContainer != null) && (localContainer.isLightweight()); 
/* 4282 */           localContainer = localContainer.getContainer())
/*      */         {
/* 4284 */           localPoint.translate(localContainer.getX(), localContainer.getY());
/*      */         }
/*      */ 
/* 4287 */         recursiveRelocateHeavyweightChildren(localPoint);
/*      */ 
/* 4289 */         if (!bool) {
/* 4290 */           return;
/*      */         }
/*      */ 
/* 4293 */         recursiveApplyCurrentShape();
/*      */       }
/*      */ 
/* 4296 */       if (!bool) {
/* 4297 */         return;
/*      */       }
/*      */ 
/* 4300 */       super.mixOnReshaping();
/*      */     }
/*      */   }
/*      */ 
/*      */   void mixOnZOrderChanging(int paramInt1, int paramInt2)
/*      */   {
/* 4306 */     synchronized (getTreeLock()) {
/* 4307 */       if (mixingLog.isLoggable(500)) {
/* 4308 */         mixingLog.fine("this = " + this + "; oldZ=" + paramInt1 + "; newZ=" + paramInt2);
/*      */       }
/*      */ 
/* 4312 */       if (!isMixingNeeded()) {
/* 4313 */         return;
/*      */       }
/*      */ 
/* 4316 */       int i = paramInt2 < paramInt1 ? 1 : 0;
/*      */ 
/* 4318 */       if ((i != 0) && (isLightweight()) && (hasHeavyweightDescendants())) {
/* 4319 */         recursiveApplyCurrentShape();
/*      */       }
/* 4321 */       super.mixOnZOrderChanging(paramInt1, paramInt2);
/*      */     }
/*      */   }
/*      */ 
/*      */   void mixOnValidating()
/*      */   {
/* 4327 */     synchronized (getTreeLock()) {
/* 4328 */       if (mixingLog.isLoggable(500)) {
/* 4329 */         mixingLog.fine("this = " + this);
/*      */       }
/*      */ 
/* 4332 */       if (!isMixingNeeded()) {
/* 4333 */         return;
/*      */       }
/*      */ 
/* 4336 */       if (hasHeavyweightDescendants()) {
/* 4337 */         recursiveApplyCurrentShape();
/*      */       }
/*      */ 
/* 4340 */       if ((isLightweight()) && (isNonOpaqueForMixing())) {
/* 4341 */         subtractAndApplyShapeBelowMe();
/*      */       }
/*      */ 
/* 4344 */       super.mixOnValidating();
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  251 */     Toolkit.loadLibraries();
/*  252 */     if (!GraphicsEnvironment.isHeadless()) {
/*  253 */       initIDs();
/*      */     }
/*      */ 
/*  256 */     AWTAccessor.setContainerAccessor(new AWTAccessor.ContainerAccessor()
/*      */     {
/*      */       public void validateUnconditionally(Container paramAnonymousContainer) {
/*  259 */         paramAnonymousContainer.validateUnconditionally();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTContainer extends Component.AccessibleAWTComponent
/*      */   {
/*      */     private static final long serialVersionUID = 5081320404842566097L;
/*      */     protected ContainerListener accessibleContainerHandler;
/*      */ 
/*      */     protected AccessibleAWTContainer()
/*      */     {
/* 3783 */       super();
/*      */ 
/* 3825 */       this.accessibleContainerHandler = null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 3798 */       return Container.this.getAccessibleChildrenCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 3808 */       return Container.this.getAccessibleChild(paramInt);
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 3822 */       return Container.this.getAccessibleAt(paramPoint);
/*      */     }
/*      */ 
/*      */     public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 3858 */       if (this.accessibleContainerHandler == null) {
/* 3859 */         this.accessibleContainerHandler = new AccessibleContainerHandler();
/* 3860 */         Container.this.addContainerListener(this.accessibleContainerHandler);
/*      */       }
/* 3862 */       super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     protected class AccessibleContainerHandler
/*      */       implements ContainerListener
/*      */     {
/*      */       protected AccessibleContainerHandler()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void componentAdded(ContainerEvent paramContainerEvent)
/*      */       {
/* 3835 */         Component localComponent = paramContainerEvent.getChild();
/* 3836 */         if ((localComponent != null) && ((localComponent instanceof Accessible)))
/* 3837 */           Container.AccessibleAWTContainer.this.firePropertyChange("AccessibleChild", null, ((Accessible)localComponent).getAccessibleContext());
/*      */       }
/*      */ 
/*      */       public void componentRemoved(ContainerEvent paramContainerEvent)
/*      */       {
/* 3843 */         Component localComponent = paramContainerEvent.getChild();
/* 3844 */         if ((localComponent != null) && ((localComponent instanceof Accessible)))
/* 3845 */           Container.AccessibleAWTContainer.this.firePropertyChange("AccessibleChild", ((Accessible)localComponent).getAccessibleContext(), null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DropTargetEventTargetFilter
/*      */     implements Container.EventTargetFilter
/*      */   {
/* 2460 */     static final Container.EventTargetFilter FILTER = new DropTargetEventTargetFilter();
/*      */ 
/*      */     public boolean accept(Component paramComponent)
/*      */     {
/* 2465 */       DropTarget localDropTarget = paramComponent.getDropTarget();
/* 2466 */       return (localDropTarget != null) && (localDropTarget.isActive());
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract interface EventTargetFilter
/*      */   {
/*      */     public abstract boolean accept(Component paramComponent);
/*      */   }
/*      */ 
/*      */   static class MouseEventTargetFilter
/*      */     implements Container.EventTargetFilter
/*      */   {
/* 2445 */     static final Container.EventTargetFilter FILTER = new MouseEventTargetFilter();
/*      */ 
/*      */     public boolean accept(Component paramComponent)
/*      */     {
/* 2450 */       return ((paramComponent.eventMask & 0x20) != 0L) || ((paramComponent.eventMask & 0x10) != 0L) || ((paramComponent.eventMask & 0x20000) != 0L) || (paramComponent.mouseListener != null) || (paramComponent.mouseMotionListener != null) || (paramComponent.mouseWheelListener != null);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class WakingRunnable
/*      */     implements Runnable
/*      */   {
/*      */     public void run()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Container
 * JD-Core Version:    0.6.2
 */