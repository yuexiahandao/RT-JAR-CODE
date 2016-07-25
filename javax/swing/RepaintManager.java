/*      */ package javax.swing;
/*      */ 
/*      */ import com.sun.java.swing.SwingUtilities3;
/*      */ import java.applet.Applet;
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.InvocationEvent;
/*      */ import java.awt.image.VolatileImage;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.awt.AWTAccessor.WindowAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.DisplayChangedListener;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.java2d.SunGraphicsEnvironment;
/*      */ import sun.misc.JavaSecurityAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class RepaintManager
/*      */ {
/*      */   static final boolean HANDLE_TOP_LEVEL_PAINT;
/*      */   private static final short BUFFER_STRATEGY_NOT_SPECIFIED = 0;
/*      */   private static final short BUFFER_STRATEGY_SPECIFIED_ON = 1;
/*      */   private static final short BUFFER_STRATEGY_SPECIFIED_OFF = 2;
/*      */   private static final short BUFFER_STRATEGY_TYPE;
/*   79 */   private Map<GraphicsConfiguration, VolatileImage> volatileMap = new HashMap(1);
/*      */   private Map<Container, Rectangle> hwDirtyComponents;
/*      */   private Map<Component, Rectangle> dirtyComponents;
/*      */   private Map<Component, Rectangle> tmpDirtyComponents;
/*      */   private List<Component> invalidComponents;
/*      */   private List<Runnable> runnableList;
/*  102 */   boolean doubleBufferingEnabled = true;
/*      */   private Dimension doubleBufferMaxSize;
/*      */   DoubleBufferInfo standardDoubleBuffer;
/*      */   private PaintManager paintManager;
/*  120 */   private static final Object repaintManagerKey = RepaintManager.class;
/*      */ 
/*  123 */   static boolean volatileImageBufferEnabled = true;
/*      */   private static final int volatileBufferType;
/*      */   private static boolean nativeDoubleBuffering;
/*      */   private static final int VOLATILE_LOOP_MAX = 2;
/*  141 */   private int paintDepth = 0;
/*      */   private short bufferStrategyType;
/*      */   private boolean painting;
/*      */   private JComponent repaintRoot;
/*      */   private Thread paintThread;
/*      */   private final ProcessingRunnable processingRunnable;
/*  182 */   private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
/*      */ 
/*  872 */   Rectangle tmp = new Rectangle();
/*      */ 
/*      */   public static RepaintManager currentManager(Component paramComponent)
/*      */   {
/*  240 */     return currentManager(AppContext.getAppContext());
/*      */   }
/*      */ 
/*      */   static RepaintManager currentManager(AppContext paramAppContext)
/*      */   {
/*  249 */     RepaintManager localRepaintManager = (RepaintManager)paramAppContext.get(repaintManagerKey);
/*  250 */     if (localRepaintManager == null) {
/*  251 */       localRepaintManager = new RepaintManager(BUFFER_STRATEGY_TYPE);
/*  252 */       paramAppContext.put(repaintManagerKey, localRepaintManager);
/*      */     }
/*  254 */     return localRepaintManager;
/*      */   }
/*      */ 
/*      */   public static RepaintManager currentManager(JComponent paramJComponent)
/*      */   {
/*  268 */     return currentManager(paramJComponent);
/*      */   }
/*      */ 
/*      */   public static void setCurrentManager(RepaintManager paramRepaintManager)
/*      */   {
/*  279 */     if (paramRepaintManager != null)
/*  280 */       SwingUtilities.appContextPut(repaintManagerKey, paramRepaintManager);
/*      */     else
/*  282 */       SwingUtilities.appContextRemove(repaintManagerKey);
/*      */   }
/*      */ 
/*      */   public RepaintManager()
/*      */   {
/*  296 */     this((short)2);
/*      */   }
/*      */ 
/*      */   private RepaintManager(short paramShort)
/*      */   {
/*  302 */     this.doubleBufferingEnabled = (!nativeDoubleBuffering);
/*  303 */     synchronized (this) {
/*  304 */       this.dirtyComponents = new IdentityHashMap();
/*  305 */       this.tmpDirtyComponents = new IdentityHashMap();
/*  306 */       this.bufferStrategyType = paramShort;
/*  307 */       this.hwDirtyComponents = new IdentityHashMap();
/*      */     }
/*  309 */     this.processingRunnable = new ProcessingRunnable(null);
/*      */   }
/*      */ 
/*      */   private void displayChanged() {
/*  313 */     clearImages();
/*      */   }
/*      */ 
/*      */   public synchronized void addInvalidComponent(JComponent paramJComponent)
/*      */   {
/*  326 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  327 */     if (localRepaintManager != null) {
/*  328 */       localRepaintManager.addInvalidComponent(paramJComponent);
/*  329 */       return;
/*      */     }
/*  331 */     Container localContainer = SwingUtilities.getValidateRoot(paramJComponent, true);
/*      */ 
/*  334 */     if (localContainer == null) {
/*  335 */       return;
/*      */     }
/*      */ 
/*  342 */     if (this.invalidComponents == null) {
/*  343 */       this.invalidComponents = new ArrayList();
/*      */     }
/*      */     else {
/*  346 */       int i = this.invalidComponents.size();
/*  347 */       for (int j = 0; j < i; j++) {
/*  348 */         if (localContainer == this.invalidComponents.get(j)) {
/*  349 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  353 */     this.invalidComponents.add(localContainer);
/*      */ 
/*  357 */     scheduleProcessingRunnable(SunToolkit.targetToAppContext(paramJComponent));
/*      */   }
/*      */ 
/*      */   public synchronized void removeInvalidComponent(JComponent paramJComponent)
/*      */   {
/*  367 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  368 */     if (localRepaintManager != null) {
/*  369 */       localRepaintManager.removeInvalidComponent(paramJComponent);
/*  370 */       return;
/*      */     }
/*  372 */     if (this.invalidComponents != null) {
/*  373 */       int i = this.invalidComponents.indexOf(paramJComponent);
/*  374 */       if (i != -1)
/*  375 */         this.invalidComponents.remove(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addDirtyRegion0(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  391 */     if ((paramInt3 <= 0) || (paramInt4 <= 0) || (paramContainer == null)) {
/*  392 */       return;
/*      */     }
/*      */ 
/*  395 */     if ((paramContainer.getWidth() <= 0) || (paramContainer.getHeight() <= 0)) {
/*  396 */       return;
/*      */     }
/*      */ 
/*  399 */     if (extendDirtyRegion(paramContainer, paramInt1, paramInt2, paramInt3, paramInt4))
/*      */     {
/*  402 */       return;
/*      */     }
/*      */ 
/*  411 */     Object localObject1 = null;
/*      */ 
/*  417 */     for (Container localContainer = paramContainer; localContainer != null; localContainer = localContainer.getParent()) {
/*  418 */       if ((!localContainer.isVisible()) || (localContainer.getPeer() == null)) {
/*  419 */         return;
/*      */       }
/*  421 */       if (((localContainer instanceof Window)) || ((localContainer instanceof Applet)))
/*      */       {
/*  423 */         if (((localContainer instanceof Frame)) && ((((Frame)localContainer).getExtendedState() & 0x1) == 1))
/*      */         {
/*  426 */           return;
/*      */         }
/*  428 */         localObject1 = localContainer;
/*  429 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  433 */     if (localObject1 == null) return;
/*      */ 
/*  435 */     synchronized (this) {
/*  436 */       if (extendDirtyRegion(paramContainer, paramInt1, paramInt2, paramInt3, paramInt4))
/*      */       {
/*  439 */         return;
/*      */       }
/*  441 */       this.dirtyComponents.put(paramContainer, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     }
/*      */ 
/*  446 */     scheduleProcessingRunnable(SunToolkit.targetToAppContext(paramContainer));
/*      */   }
/*      */ 
/*      */   public void addDirtyRegion(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  463 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  464 */     if (localRepaintManager != null) {
/*  465 */       localRepaintManager.addDirtyRegion(paramJComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*  466 */       return;
/*      */     }
/*  468 */     addDirtyRegion0(paramJComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void addDirtyRegion(Window paramWindow, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  486 */     addDirtyRegion0(paramWindow, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void addDirtyRegion(Applet paramApplet, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  502 */     addDirtyRegion0(paramApplet, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   void scheduleHeavyWeightPaints()
/*      */   {
/*      */     Map localMap;
/*  508 */     synchronized (this) {
/*  509 */       if (this.hwDirtyComponents.size() == 0) {
/*  510 */         return;
/*      */       }
/*  512 */       localMap = this.hwDirtyComponents;
/*  513 */       this.hwDirtyComponents = new IdentityHashMap();
/*      */     }
/*  515 */     for (??? = localMap.keySet().iterator(); ((Iterator)???).hasNext(); ) { Container localContainer = (Container)((Iterator)???).next();
/*  516 */       Rectangle localRectangle = (Rectangle)localMap.get(localContainer);
/*  517 */       if ((localContainer instanceof Window)) {
/*  518 */         addDirtyRegion((Window)localContainer, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*  521 */       else if ((localContainer instanceof Applet)) {
/*  522 */         addDirtyRegion((Applet)localContainer, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */       else
/*      */       {
/*  526 */         addDirtyRegion0(localContainer, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void nativeAddDirtyRegion(AppContext paramAppContext, Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  538 */     if ((paramInt3 > 0) && (paramInt4 > 0)) {
/*  539 */       synchronized (this) {
/*  540 */         Rectangle localRectangle = (Rectangle)this.hwDirtyComponents.get(paramContainer);
/*  541 */         if (localRectangle == null) {
/*  542 */           this.hwDirtyComponents.put(paramContainer, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */         }
/*      */         else {
/*  545 */           this.hwDirtyComponents.put(paramContainer, SwingUtilities.computeUnion(paramInt1, paramInt2, paramInt3, paramInt4, localRectangle));
/*      */         }
/*      */       }
/*      */ 
/*  549 */       scheduleProcessingRunnable(paramAppContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   void nativeQueueSurfaceDataRunnable(AppContext paramAppContext, final Component paramComponent, final Runnable paramRunnable)
/*      */   {
/*  560 */     synchronized (this) {
/*  561 */       if (this.runnableList == null) {
/*  562 */         this.runnableList = new LinkedList();
/*      */       }
/*  564 */       this.runnableList.add(new Runnable() {
/*      */         public void run() {
/*  566 */           AccessControlContext localAccessControlContext1 = AccessController.getContext();
/*  567 */           AccessControlContext localAccessControlContext2 = AWTAccessor.getComponentAccessor().getAccessControlContext(paramComponent);
/*      */ 
/*  569 */           RepaintManager.javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction() {
/*      */             public Void run() {
/*  571 */               RepaintManager.1.this.val$r.run();
/*  572 */               return null;
/*      */             }
/*      */           }
/*      */           , localAccessControlContext1, localAccessControlContext2);
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  578 */     scheduleProcessingRunnable(paramAppContext);
/*      */   }
/*      */ 
/*      */   private synchronized boolean extendDirtyRegion(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  589 */     Rectangle localRectangle = (Rectangle)this.dirtyComponents.get(paramComponent);
/*  590 */     if (localRectangle != null)
/*      */     {
/*  594 */       SwingUtilities.computeUnion(paramInt1, paramInt2, paramInt3, paramInt4, localRectangle);
/*  595 */       return true;
/*      */     }
/*  597 */     return false;
/*      */   }
/*      */ 
/*      */   public Rectangle getDirtyRegion(JComponent paramJComponent)
/*      */   {
/*  605 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  606 */     if (localRepaintManager != null)
/*  607 */       return localRepaintManager.getDirtyRegion(paramJComponent);
/*      */     Rectangle localRectangle;
/*  610 */     synchronized (this) {
/*  611 */       localRectangle = (Rectangle)this.dirtyComponents.get(paramJComponent);
/*      */     }
/*  613 */     if (localRectangle == null) {
/*  614 */       return new Rectangle(0, 0, 0, 0);
/*      */     }
/*  616 */     return new Rectangle(localRectangle);
/*      */   }
/*      */ 
/*      */   public void markCompletelyDirty(JComponent paramJComponent)
/*      */   {
/*  624 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  625 */     if (localRepaintManager != null) {
/*  626 */       localRepaintManager.markCompletelyDirty(paramJComponent);
/*  627 */       return;
/*      */     }
/*  629 */     addDirtyRegion(paramJComponent, 0, 0, 2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   public void markCompletelyClean(JComponent paramJComponent)
/*      */   {
/*  637 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  638 */     if (localRepaintManager != null) {
/*  639 */       localRepaintManager.markCompletelyClean(paramJComponent);
/*  640 */       return;
/*      */     }
/*  642 */     synchronized (this) {
/*  643 */       this.dirtyComponents.remove(paramJComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isCompletelyDirty(JComponent paramJComponent)
/*      */   {
/*  654 */     RepaintManager localRepaintManager = getDelegate(paramJComponent);
/*  655 */     if (localRepaintManager != null) {
/*  656 */       return localRepaintManager.isCompletelyDirty(paramJComponent);
/*      */     }
/*      */ 
/*  660 */     Rectangle localRectangle = getDirtyRegion(paramJComponent);
/*  661 */     if ((localRectangle.width == 2147483647) && (localRectangle.height == 2147483647))
/*      */     {
/*  663 */       return true;
/*      */     }
/*  665 */     return false;
/*      */   }
/*      */ 
/*      */   public void validateInvalidComponents()
/*      */   {
/*      */     List localList;
/*  675 */     synchronized (this) {
/*  676 */       if (this.invalidComponents == null) {
/*  677 */         return;
/*      */       }
/*  679 */       localList = this.invalidComponents;
/*  680 */       this.invalidComponents = null;
/*      */     }
/*  682 */     ??? = localList.size();
/*  683 */     for (Object localObject2 = 0; localObject2 < ???; localObject2++) {
/*  684 */       final Component localComponent = (Component)localList.get(localObject2);
/*  685 */       AccessControlContext localAccessControlContext1 = AccessController.getContext();
/*  686 */       AccessControlContext localAccessControlContext2 = AWTAccessor.getComponentAccessor().getAccessControlContext(localComponent);
/*      */ 
/*  688 */       javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*  691 */           localComponent.validate();
/*  692 */           return null;
/*      */         }
/*      */       }
/*      */       , localAccessControlContext1, localAccessControlContext2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void prePaintDirtyRegions()
/*      */   {
/*      */     Map localMap;
/*      */     List localList;
/*  708 */     synchronized (this) {
/*  709 */       localMap = this.dirtyComponents;
/*  710 */       localList = this.runnableList;
/*  711 */       this.runnableList = null;
/*      */     }
/*  713 */     if (localList != null) {
/*  714 */       for (??? = localList.iterator(); ((Iterator)???).hasNext(); ) { Runnable localRunnable = (Runnable)((Iterator)???).next();
/*  715 */         localRunnable.run();
/*      */       }
/*      */     }
/*  718 */     paintDirtyRegions();
/*  719 */     if (localMap.size() > 0)
/*      */     {
/*  722 */       paintDirtyRegions(localMap);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateWindows(Map<Component, Rectangle> paramMap) {
/*  727 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  728 */     if ((!(localToolkit instanceof SunToolkit)) || (!((SunToolkit)localToolkit).needUpdateWindow()))
/*      */     {
/*  731 */       return;
/*      */     }
/*      */ 
/*  734 */     HashSet localHashSet = new HashSet();
/*  735 */     Set localSet = paramMap.keySet();
/*  736 */     for (Iterator localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/*  737 */       localObject = (Component)localIterator.next();
/*  738 */       Window localWindow = (localObject instanceof Window) ? (Window)localObject : SwingUtilities.getWindowAncestor((Component)localObject);
/*      */ 
/*  741 */       if ((localWindow != null) && (!localWindow.isOpaque()))
/*      */       {
/*  744 */         localHashSet.add(localWindow);
/*      */       }
/*      */     }
/*  748 */     Object localObject;
/*  748 */     for (localIterator = localHashSet.iterator(); localIterator.hasNext(); ) { localObject = (Window)localIterator.next();
/*  749 */       AWTAccessor.getWindowAccessor().updateWindow((Window)localObject); }
/*      */   }
/*      */ 
/*      */   boolean isPainting()
/*      */   {
/*  754 */     return this.painting;
/*      */   }
/*      */ 
/*      */   public void paintDirtyRegions()
/*      */   {
/*  763 */     synchronized (this) {
/*  764 */       Map localMap = this.tmpDirtyComponents;
/*  765 */       this.tmpDirtyComponents = this.dirtyComponents;
/*  766 */       this.dirtyComponents = localMap;
/*  767 */       this.dirtyComponents.clear();
/*      */     }
/*  769 */     paintDirtyRegions(this.tmpDirtyComponents);
/*      */   }
/*      */ 
/*      */   private void paintDirtyRegions(final Map<Component, Rectangle> paramMap)
/*      */   {
/*  775 */     if (paramMap.isEmpty()) {
/*  776 */       return;
/*      */     }
/*      */ 
/*  779 */     final ArrayList localArrayList = new ArrayList(paramMap.size());
/*      */ 
/*  782 */     for (Object localObject1 = paramMap.keySet().iterator(); ((Iterator)localObject1).hasNext(); ) { Component localComponent1 = (Component)((Iterator)localObject1).next();
/*  783 */       collectDirtyComponents(paramMap, localComponent1, localArrayList);
/*      */     }
/*      */ 
/*  786 */     localObject1 = new AtomicInteger(localArrayList.size());
/*  787 */     this.painting = true;
/*      */     try {
/*  789 */       for (int i = 0; i < ((AtomicInteger)localObject1).get(); i++) {
/*  790 */         final int j = i;
/*  791 */         final Component localComponent2 = (Component)localArrayList.get(i);
/*      */ 
/*  793 */         AccessControlContext localAccessControlContext1 = AccessController.getContext();
/*  794 */         AccessControlContext localAccessControlContext2 = AWTAccessor.getComponentAccessor().getAccessControlContext(localComponent2);
/*      */ 
/*  796 */         javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction() {
/*      */           public Void run() {
/*  798 */             Rectangle localRectangle = (Rectangle)paramMap.get(localComponent2);
/*      */ 
/*  800 */             int i = localComponent2.getHeight();
/*  801 */             int j = localComponent2.getWidth();
/*  802 */             SwingUtilities.computeIntersection(0, 0, j, i, localRectangle);
/*      */ 
/*  807 */             if ((localComponent2 instanceof JComponent)) {
/*  808 */               ((JComponent)localComponent2).paintImmediately(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */             }
/*  811 */             else if (localComponent2.isShowing()) {
/*  812 */               Graphics localGraphics = JComponent.safelyGetGraphics(localComponent2, localComponent2);
/*      */ 
/*  816 */               if (localGraphics != null) {
/*  817 */                 localGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */                 try {
/*  819 */                   localComponent2.paint(localGraphics);
/*      */                 } finally {
/*  821 */                   localGraphics.dispose();
/*      */                 }
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  827 */             if (RepaintManager.this.repaintRoot != null) {
/*  828 */               RepaintManager.this.adjustRoots(RepaintManager.this.repaintRoot, localArrayList, j + 1);
/*  829 */               this.val$count.set(localArrayList.size());
/*  830 */               RepaintManager.this.paintManager.isRepaintingRoot = true;
/*  831 */               RepaintManager.this.repaintRoot.paintImmediately(0, 0, RepaintManager.this.repaintRoot.getWidth(), RepaintManager.this.repaintRoot.getHeight());
/*      */ 
/*  833 */               RepaintManager.this.paintManager.isRepaintingRoot = false;
/*      */ 
/*  835 */               RepaintManager.this.repaintRoot = null;
/*      */             }
/*      */ 
/*  838 */             return null;
/*      */           }
/*      */         }
/*      */         , localAccessControlContext1, localAccessControlContext2);
/*      */       }
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*  843 */       this.painting = false;
/*      */     }
/*      */ 
/*  846 */     updateWindows(paramMap);
/*      */ 
/*  848 */     paramMap.clear();
/*      */   }
/*      */ 
/*      */   private void adjustRoots(JComponent paramJComponent, List<Component> paramList, int paramInt)
/*      */   {
/*  858 */     for (int i = paramList.size() - 1; i >= paramInt; i--) {
/*  859 */       Object localObject = (Component)paramList.get(i);
/*      */ 
/*  861 */       while ((localObject != paramJComponent) && (localObject != null) && ((localObject instanceof JComponent)))
/*      */       {
/*  864 */         localObject = ((Component)localObject).getParent();
/*      */       }
/*  866 */       if (localObject == paramJComponent)
/*  867 */         paramList.remove(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   void collectDirtyComponents(Map<Component, Rectangle> paramMap, Component paramComponent, List<Component> paramList)
/*      */   {
/*      */     Object localObject2;
/*  887 */     Object localObject1 = localObject2 = paramComponent;
/*      */ 
/*  889 */     int n = paramComponent.getX();
/*  890 */     int i1 = paramComponent.getY();
/*  891 */     int i2 = paramComponent.getWidth();
/*  892 */     int i3 = paramComponent.getHeight();
/*      */     int k;
/*  894 */     int i = k = 0;
/*      */     int m;
/*  895 */     int j = m = 0;
/*  896 */     this.tmp.setBounds((Rectangle)paramMap.get(paramComponent));
/*      */ 
/*  900 */     SwingUtilities.computeIntersection(0, 0, i2, i3, this.tmp);
/*      */ 
/*  902 */     if (this.tmp.isEmpty())
/*      */     {
/*  904 */       return;
/*      */     }
/*      */ 
/*  908 */     while ((localObject1 instanceof JComponent))
/*      */     {
/*  911 */       Container localContainer = ((Component)localObject1).getParent();
/*  912 */       if (localContainer == null) {
/*      */         break;
/*      */       }
/*  915 */       localObject1 = localContainer;
/*      */ 
/*  917 */       i += n;
/*  918 */       j += i1;
/*  919 */       this.tmp.setLocation(this.tmp.x + n, this.tmp.y + i1);
/*      */ 
/*  921 */       n = ((Component)localObject1).getX();
/*  922 */       i1 = ((Component)localObject1).getY();
/*  923 */       i2 = ((Component)localObject1).getWidth();
/*  924 */       i3 = ((Component)localObject1).getHeight();
/*  925 */       this.tmp = SwingUtilities.computeIntersection(0, 0, i2, i3, this.tmp);
/*      */ 
/*  927 */       if (this.tmp.isEmpty())
/*      */       {
/*  929 */         return;
/*      */       }
/*      */ 
/*  932 */       if (paramMap.get(localObject1) != null) {
/*  933 */         localObject2 = localObject1;
/*  934 */         k = i;
/*  935 */         m = j;
/*      */       }
/*      */     }
/*      */ 
/*  939 */     if (paramComponent != localObject2)
/*      */     {
/*  941 */       this.tmp.setLocation(this.tmp.x + k - i, this.tmp.y + m - j);
/*      */ 
/*  943 */       Rectangle localRectangle = (Rectangle)paramMap.get(localObject2);
/*  944 */       SwingUtilities.computeUnion(this.tmp.x, this.tmp.y, this.tmp.width, this.tmp.height, localRectangle);
/*      */     }
/*      */ 
/*  950 */     if (!paramList.contains(localObject2))
/*  951 */       paramList.add(localObject2);
/*      */   }
/*      */ 
/*      */   public synchronized String toString()
/*      */   {
/*  962 */     StringBuffer localStringBuffer = new StringBuffer();
/*  963 */     if (this.dirtyComponents != null)
/*  964 */       localStringBuffer.append("" + this.dirtyComponents);
/*  965 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public Image getOffscreenBuffer(Component paramComponent, int paramInt1, int paramInt2)
/*      */   {
/*  978 */     RepaintManager localRepaintManager = getDelegate(paramComponent);
/*  979 */     if (localRepaintManager != null) {
/*  980 */       return localRepaintManager.getOffscreenBuffer(paramComponent, paramInt1, paramInt2);
/*      */     }
/*  982 */     return _getOffscreenBuffer(paramComponent, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public Image getVolatileOffscreenBuffer(Component paramComponent, int paramInt1, int paramInt2)
/*      */   {
/*  999 */     RepaintManager localRepaintManager = getDelegate(paramComponent);
/* 1000 */     if (localRepaintManager != null) {
/* 1001 */       return localRepaintManager.getVolatileOffscreenBuffer(paramComponent, paramInt1, paramInt2);
/*      */     }
/*      */ 
/* 1006 */     Window localWindow = (paramComponent instanceof Window) ? (Window)paramComponent : SwingUtilities.getWindowAncestor(paramComponent);
/* 1007 */     if (!localWindow.isOpaque()) {
/* 1008 */       localObject = Toolkit.getDefaultToolkit();
/* 1009 */       if (((localObject instanceof SunToolkit)) && (((SunToolkit)localObject).needUpdateWindow())) {
/* 1010 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1014 */     Object localObject = paramComponent.getGraphicsConfiguration();
/* 1015 */     if (localObject == null) {
/* 1016 */       localObject = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*      */     }
/*      */ 
/* 1019 */     Dimension localDimension = getDoubleBufferMaximumSize();
/* 1020 */     int i = paramInt1 > localDimension.width ? localDimension.width : paramInt1 < 1 ? 1 : paramInt1;
/*      */ 
/* 1022 */     int j = paramInt2 > localDimension.height ? localDimension.height : paramInt2 < 1 ? 1 : paramInt2;
/*      */ 
/* 1024 */     VolatileImage localVolatileImage = (VolatileImage)this.volatileMap.get(localObject);
/* 1025 */     if ((localVolatileImage == null) || (localVolatileImage.getWidth() < i) || (localVolatileImage.getHeight() < j))
/*      */     {
/* 1027 */       if (localVolatileImage != null) {
/* 1028 */         localVolatileImage.flush();
/*      */       }
/* 1030 */       localVolatileImage = ((GraphicsConfiguration)localObject).createCompatibleVolatileImage(i, j, volatileBufferType);
/*      */ 
/* 1032 */       this.volatileMap.put(localObject, localVolatileImage);
/*      */     }
/* 1034 */     return localVolatileImage;
/*      */   }
/*      */ 
/*      */   private Image _getOffscreenBuffer(Component paramComponent, int paramInt1, int paramInt2) {
/* 1038 */     Dimension localDimension = getDoubleBufferMaximumSize();
/*      */ 
/* 1043 */     Window localWindow = (paramComponent instanceof Window) ? (Window)paramComponent : SwingUtilities.getWindowAncestor(paramComponent);
/* 1044 */     if (!localWindow.isOpaque()) {
/* 1045 */       localObject = Toolkit.getDefaultToolkit();
/* 1046 */       if (((localObject instanceof SunToolkit)) && (((SunToolkit)localObject).needUpdateWindow())) {
/* 1047 */         return null;
/*      */       }
/*      */     }
/*      */ 
/* 1051 */     if (this.standardDoubleBuffer == null) {
/* 1052 */       this.standardDoubleBuffer = new DoubleBufferInfo(null);
/*      */     }
/* 1054 */     DoubleBufferInfo localDoubleBufferInfo = this.standardDoubleBuffer;
/*      */ 
/* 1056 */     int i = paramInt1 > localDimension.width ? localDimension.width : paramInt1 < 1 ? 1 : paramInt1;
/*      */ 
/* 1058 */     int j = paramInt2 > localDimension.height ? localDimension.height : paramInt2 < 1 ? 1 : paramInt2;
/*      */ 
/* 1061 */     if ((localDoubleBufferInfo.needsReset) || ((localDoubleBufferInfo.image != null) && ((localDoubleBufferInfo.size.width < i) || (localDoubleBufferInfo.size.height < j))))
/*      */     {
/* 1064 */       localDoubleBufferInfo.needsReset = false;
/* 1065 */       if (localDoubleBufferInfo.image != null) {
/* 1066 */         localDoubleBufferInfo.image.flush();
/* 1067 */         localDoubleBufferInfo.image = null;
/*      */       }
/* 1069 */       i = Math.max(localDoubleBufferInfo.size.width, i);
/* 1070 */       j = Math.max(localDoubleBufferInfo.size.height, j);
/*      */     }
/*      */ 
/* 1073 */     Object localObject = localDoubleBufferInfo.image;
/*      */ 
/* 1075 */     if (localDoubleBufferInfo.image == null) {
/* 1076 */       localObject = paramComponent.createImage(i, j);
/* 1077 */       localDoubleBufferInfo.size = new Dimension(i, j);
/* 1078 */       if ((paramComponent instanceof JComponent)) {
/* 1079 */         ((JComponent)paramComponent).setCreatedDoubleBuffer(true);
/* 1080 */         localDoubleBufferInfo.image = ((Image)localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1087 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void setDoubleBufferMaximumSize(Dimension paramDimension)
/*      */   {
/* 1093 */     this.doubleBufferMaxSize = paramDimension;
/* 1094 */     if (this.doubleBufferMaxSize == null)
/* 1095 */       clearImages();
/*      */     else
/* 1097 */       clearImages(paramDimension.width, paramDimension.height);
/*      */   }
/*      */ 
/*      */   private void clearImages()
/*      */   {
/* 1102 */     clearImages(0, 0);
/*      */   }
/*      */ 
/*      */   private void clearImages(int paramInt1, int paramInt2) {
/* 1106 */     if ((this.standardDoubleBuffer != null) && (this.standardDoubleBuffer.image != null) && (
/* 1107 */       (this.standardDoubleBuffer.image.getWidth(null) > paramInt1) || (this.standardDoubleBuffer.image.getHeight(null) > paramInt2)))
/*      */     {
/* 1109 */       this.standardDoubleBuffer.image.flush();
/* 1110 */       this.standardDoubleBuffer.image = null;
/*      */     }
/*      */ 
/* 1114 */     Iterator localIterator = this.volatileMap.keySet().iterator();
/* 1115 */     while (localIterator.hasNext()) {
/* 1116 */       GraphicsConfiguration localGraphicsConfiguration = (GraphicsConfiguration)localIterator.next();
/* 1117 */       VolatileImage localVolatileImage = (VolatileImage)this.volatileMap.get(localGraphicsConfiguration);
/* 1118 */       if ((localVolatileImage.getWidth() > paramInt1) || (localVolatileImage.getHeight() > paramInt2)) {
/* 1119 */         localVolatileImage.flush();
/* 1120 */         localIterator.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getDoubleBufferMaximumSize()
/*      */   {
/* 1131 */     if (this.doubleBufferMaxSize == null) {
/*      */       try {
/* 1133 */         Rectangle localRectangle = new Rectangle();
/* 1134 */         GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/* 1136 */         for (GraphicsDevice localGraphicsDevice : localGraphicsEnvironment.getScreenDevices()) {
/* 1137 */           GraphicsConfiguration localGraphicsConfiguration = localGraphicsDevice.getDefaultConfiguration();
/* 1138 */           localRectangle = localRectangle.union(localGraphicsConfiguration.getBounds());
/*      */         }
/* 1140 */         this.doubleBufferMaxSize = new Dimension(localRectangle.width, localRectangle.height);
/*      */       }
/*      */       catch (HeadlessException localHeadlessException) {
/* 1143 */         this.doubleBufferMaxSize = new Dimension(2147483647, 2147483647);
/*      */       }
/*      */     }
/* 1146 */     return this.doubleBufferMaxSize;
/*      */   }
/*      */ 
/*      */   public void setDoubleBufferingEnabled(boolean paramBoolean)
/*      */   {
/* 1159 */     this.doubleBufferingEnabled = paramBoolean;
/* 1160 */     PaintManager localPaintManager = getPaintManager();
/* 1161 */     if ((!paramBoolean) && (localPaintManager.getClass() != PaintManager.class))
/* 1162 */       setPaintManager(new PaintManager());
/*      */   }
/*      */ 
/*      */   public boolean isDoubleBufferingEnabled()
/*      */   {
/* 1178 */     return this.doubleBufferingEnabled;
/*      */   }
/*      */ 
/*      */   void resetDoubleBuffer()
/*      */   {
/* 1187 */     if (this.standardDoubleBuffer != null)
/* 1188 */       this.standardDoubleBuffer.needsReset = true;
/*      */   }
/*      */ 
/*      */   void resetVolatileDoubleBuffer(GraphicsConfiguration paramGraphicsConfiguration)
/*      */   {
/* 1196 */     Image localImage = (Image)this.volatileMap.remove(paramGraphicsConfiguration);
/* 1197 */     if (localImage != null)
/* 1198 */       localImage.flush();
/*      */   }
/*      */ 
/*      */   boolean useVolatileDoubleBuffer()
/*      */   {
/* 1207 */     return volatileImageBufferEnabled;
/*      */   }
/*      */ 
/*      */   private synchronized boolean isPaintingThread()
/*      */   {
/* 1215 */     return Thread.currentThread() == this.paintThread;
/*      */   }
/*      */ 
/*      */   void paint(JComponent paramJComponent1, JComponent paramJComponent2, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1239 */     PaintManager localPaintManager = getPaintManager();
/* 1240 */     if (!isPaintingThread())
/*      */     {
/* 1244 */       if (localPaintManager.getClass() != PaintManager.class) {
/* 1245 */         localPaintManager = new PaintManager();
/* 1246 */         localPaintManager.repaintManager = this;
/*      */       }
/*      */     }
/* 1249 */     if (!localPaintManager.paint(paramJComponent1, paramJComponent2, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4))
/*      */     {
/* 1251 */       paramGraphics.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/* 1252 */       paramJComponent1.paintToOffscreen(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4);
/*      */     }
/*      */   }
/*      */ 
/*      */   void copyArea(JComponent paramJComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */   {
/* 1264 */     getPaintManager().copyArea(paramJComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean);
/*      */   }
/*      */ 
/*      */   void beginPaint()
/*      */   {
/* 1284 */     int i = 0;
/*      */ 
/* 1286 */     Thread localThread = Thread.currentThread();
/*      */     int j;
/* 1287 */     synchronized (this) {
/* 1288 */       j = this.paintDepth;
/* 1289 */       if ((this.paintThread == null) || (localThread == this.paintThread)) {
/* 1290 */         this.paintThread = localThread;
/* 1291 */         this.paintDepth += 1;
/*      */       } else {
/* 1293 */         i = 1;
/*      */       }
/*      */     }
/* 1296 */     if ((i == 0) && (j == 0))
/* 1297 */       getPaintManager().beginPaint();
/*      */   }
/*      */ 
/*      */   void endPaint()
/*      */   {
/* 1305 */     if (isPaintingThread()) {
/* 1306 */       PaintManager localPaintManager = null;
/* 1307 */       synchronized (this) {
/* 1308 */         if (--this.paintDepth == 0) {
/* 1309 */           localPaintManager = getPaintManager();
/*      */         }
/*      */       }
/* 1312 */       if (localPaintManager != null) {
/* 1313 */         localPaintManager.endPaint();
/* 1314 */         synchronized (this) {
/* 1315 */           this.paintThread = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean show(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1329 */     return getPaintManager().show(paramContainer, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   void doubleBufferingChanged(JRootPane paramJRootPane)
/*      */   {
/* 1337 */     getPaintManager().doubleBufferingChanged(paramJRootPane);
/*      */   }
/*      */ 
/*      */   void setPaintManager(PaintManager paramPaintManager)
/*      */   {
/* 1348 */     if (paramPaintManager == null)
/* 1349 */       paramPaintManager = new PaintManager();
/*      */     PaintManager localPaintManager;
/* 1352 */     synchronized (this) {
/* 1353 */       localPaintManager = this.paintManager;
/* 1354 */       this.paintManager = paramPaintManager;
/* 1355 */       paramPaintManager.repaintManager = this;
/*      */     }
/* 1357 */     if (localPaintManager != null)
/* 1358 */       localPaintManager.dispose();
/*      */   }
/*      */ 
/*      */   private synchronized PaintManager getPaintManager()
/*      */   {
/* 1363 */     if (this.paintManager == null) {
/* 1364 */       BufferStrategyPaintManager localBufferStrategyPaintManager = null;
/* 1365 */       if ((this.doubleBufferingEnabled) && (!nativeDoubleBuffering)) {
/* 1366 */         switch (this.bufferStrategyType) {
/*      */         case 0:
/* 1368 */           Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1369 */           if ((localToolkit instanceof SunToolkit)) {
/* 1370 */             SunToolkit localSunToolkit = (SunToolkit)localToolkit;
/* 1371 */             if (localSunToolkit.useBufferPerWindow())
/* 1372 */               localBufferStrategyPaintManager = new BufferStrategyPaintManager();
/*      */           }
/* 1374 */           break;
/*      */         case 1:
/* 1377 */           localBufferStrategyPaintManager = new BufferStrategyPaintManager();
/* 1378 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1384 */       setPaintManager(localBufferStrategyPaintManager);
/*      */     }
/* 1386 */     return this.paintManager;
/*      */   }
/*      */ 
/*      */   private void scheduleProcessingRunnable(AppContext paramAppContext) {
/* 1390 */     if (this.processingRunnable.markPending()) {
/* 1391 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 1392 */       if ((localToolkit instanceof SunToolkit)) {
/* 1393 */         SunToolkit.getSystemEventQueueImplPP(paramAppContext).postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), this.processingRunnable));
/*      */       }
/*      */       else
/*      */       {
/* 1397 */         Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), this.processingRunnable));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private RepaintManager getDelegate(Component paramComponent)
/*      */   {
/* 1681 */     RepaintManager localRepaintManager = SwingUtilities3.getDelegateRepaintManager(paramComponent);
/* 1682 */     if (this == localRepaintManager) {
/* 1683 */       localRepaintManager = null;
/*      */     }
/* 1685 */     return localRepaintManager;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  187 */     volatileImageBufferEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.volatileImageBufferEnabled", "true")));
/*      */ 
/*  190 */     boolean bool = GraphicsEnvironment.isHeadless();
/*  191 */     if ((volatileImageBufferEnabled) && (bool)) {
/*  192 */       volatileImageBufferEnabled = false;
/*      */     }
/*  194 */     nativeDoubleBuffering = "true".equals(AccessController.doPrivileged(new GetPropertyAction("awt.nativeDoubleBuffering")));
/*      */ 
/*  196 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("swing.bufferPerWindow"));
/*      */ 
/*  198 */     if (bool) {
/*  199 */       BUFFER_STRATEGY_TYPE = 2;
/*      */     }
/*  201 */     else if (str == null) {
/*  202 */       BUFFER_STRATEGY_TYPE = 0;
/*      */     }
/*  204 */     else if ("true".equals(str)) {
/*  205 */       BUFFER_STRATEGY_TYPE = 1;
/*      */     }
/*      */     else {
/*  208 */       BUFFER_STRATEGY_TYPE = 2;
/*      */     }
/*  210 */     HANDLE_TOP_LEVEL_PAINT = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.handleTopLevelPaint", "true")));
/*      */ 
/*  212 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*      */ 
/*  214 */     if ((localGraphicsEnvironment instanceof SunGraphicsEnvironment)) {
/*  215 */       ((SunGraphicsEnvironment)localGraphicsEnvironment).addDisplayChangedListener(new DisplayChangedHandler(null));
/*      */     }
/*      */ 
/*  218 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  219 */     if (((localToolkit instanceof SunToolkit)) && (((SunToolkit)localToolkit).isSwingBackbufferTranslucencySupported()))
/*      */     {
/*  221 */       volatileBufferType = 3;
/*      */     }
/*  223 */     else volatileBufferType = 1;
/*      */   }
/*      */ 
/*      */   private static final class DisplayChangedHandler
/*      */     implements DisplayChangedListener
/*      */   {
/*      */     public void displayChanged()
/*      */     {
/* 1610 */       scheduleDisplayChanges();
/*      */     }
/*      */ 
/*      */     public void paletteChanged()
/*      */     {
/*      */     }
/*      */ 
/*      */     private void scheduleDisplayChanges()
/*      */     {
/* 1619 */       for (Iterator localIterator = AppContext.getAppContexts().iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/* 1620 */         AppContext localAppContext = (AppContext)localObject1;
/* 1621 */         synchronized (localAppContext) {
/* 1622 */           if (!localAppContext.isDisposed()) {
/* 1623 */             EventQueue localEventQueue = (EventQueue)localAppContext.get(AppContext.EVENT_QUEUE_KEY);
/*      */ 
/* 1625 */             if (localEventQueue != null)
/* 1626 */               localEventQueue.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), new RepaintManager.DisplayChangedRunnable(null)));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class DisplayChangedRunnable
/*      */     implements Runnable
/*      */   {
/*      */     public void run()
/*      */     {
/* 1639 */       RepaintManager.currentManager((JComponent)null).displayChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DoubleBufferInfo
/*      */   {
/*      */     public Image image;
/*      */     public Dimension size;
/* 1597 */     public boolean needsReset = false;
/*      */ 
/*      */     private DoubleBufferInfo()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PaintManager
/*      */   {
/*      */     protected RepaintManager repaintManager;
/*      */     boolean isRepaintingRoot;
/*      */ 
/*      */     public boolean paint(JComponent paramJComponent1, JComponent paramJComponent2, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1435 */       boolean bool = false;
/*      */       Image localImage;
/* 1437 */       if ((this.repaintManager.useVolatileDoubleBuffer()) && ((localImage = getValidImage(this.repaintManager.getVolatileOffscreenBuffer(paramJComponent2, paramInt3, paramInt4))) != null))
/*      */       {
/* 1440 */         VolatileImage localVolatileImage = (VolatileImage)localImage;
/* 1441 */         GraphicsConfiguration localGraphicsConfiguration = paramJComponent2.getGraphicsConfiguration();
/*      */ 
/* 1443 */         for (int i = 0; (!bool) && (i < 2); 
/* 1444 */           i++) {
/* 1445 */           if (localVolatileImage.validate(localGraphicsConfiguration) == 2)
/*      */           {
/* 1447 */             this.repaintManager.resetVolatileDoubleBuffer(localGraphicsConfiguration);
/* 1448 */             localImage = this.repaintManager.getVolatileOffscreenBuffer(paramJComponent2, paramInt3, paramInt4);
/*      */ 
/* 1450 */             localVolatileImage = (VolatileImage)localImage;
/*      */           }
/* 1452 */           paintDoubleBuffered(paramJComponent1, localVolatileImage, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/* 1454 */           bool = !localVolatileImage.contentsLost();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1459 */       if ((!bool) && ((localImage = getValidImage(this.repaintManager.getOffscreenBuffer(paramJComponent2, paramInt3, paramInt4))) != null))
/*      */       {
/* 1462 */         paintDoubleBuffered(paramJComponent1, localImage, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */ 
/* 1464 */         bool = true;
/*      */       }
/* 1466 */       return bool;
/*      */     }
/*      */ 
/*      */     public void copyArea(JComponent paramJComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */     {
/* 1474 */       paramGraphics.copyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     }
/*      */ 
/*      */     public void beginPaint()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endPaint()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean show(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1495 */       return false;
/*      */     }
/*      */ 
/*      */     public void doubleBufferingChanged(JRootPane paramJRootPane)
/*      */     {
/*      */     }
/*      */ 
/*      */     protected void paintDoubleBuffered(JComponent paramJComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1511 */       Graphics localGraphics = paramImage.getGraphics();
/* 1512 */       int i = Math.min(paramInt3, paramImage.getWidth(null));
/* 1513 */       int j = Math.min(paramInt4, paramImage.getHeight(null));
/*      */       try
/*      */       {
/* 1517 */         int k = paramInt1; for (int n = paramInt1 + paramInt3; k < n; k += i) {
/* 1518 */           int m = paramInt2; for (int i1 = paramInt2 + paramInt4; m < i1; m += j) {
/* 1519 */             localGraphics.translate(-k, -m);
/* 1520 */             localGraphics.setClip(k, m, i, j);
/*      */             Graphics2D localGraphics2D;
/*      */             Object localObject1;
/* 1521 */             if ((RepaintManager.volatileBufferType != 1) && ((localGraphics instanceof Graphics2D)))
/*      */             {
/* 1523 */               localGraphics2D = (Graphics2D)localGraphics;
/* 1524 */               localObject1 = localGraphics2D.getBackground();
/* 1525 */               localGraphics2D.setBackground(paramJComponent.getBackground());
/* 1526 */               localGraphics2D.clearRect(k, m, i, j);
/* 1527 */               localGraphics2D.setBackground((Color)localObject1);
/*      */             }
/* 1529 */             paramJComponent.paintToOffscreen(localGraphics, k, m, i, j, n, i1);
/* 1530 */             paramGraphics.setClip(k, m, i, j);
/* 1531 */             if ((RepaintManager.volatileBufferType != 1) && ((paramGraphics instanceof Graphics2D)))
/*      */             {
/* 1533 */               localGraphics2D = (Graphics2D)paramGraphics;
/* 1534 */               localObject1 = localGraphics2D.getComposite();
/* 1535 */               localGraphics2D.setComposite(AlphaComposite.Src);
/* 1536 */               localGraphics2D.drawImage(paramImage, k, m, paramJComponent);
/* 1537 */               localGraphics2D.setComposite((Composite)localObject1);
/*      */             } else {
/* 1539 */               paramGraphics.drawImage(paramImage, k, m, paramJComponent);
/*      */             }
/* 1541 */             localGraphics.translate(k, m);
/*      */           }
/*      */         }
/*      */       } finally {
/* 1545 */         localGraphics.dispose();
/*      */       }
/*      */     }
/*      */ 
/*      */     private Image getValidImage(Image paramImage)
/*      */     {
/* 1554 */       if ((paramImage != null) && (paramImage.getWidth(null) > 0) && (paramImage.getHeight(null) > 0))
/*      */       {
/* 1556 */         return paramImage;
/*      */       }
/* 1558 */       return null;
/*      */     }
/*      */ 
/*      */     protected void repaintRoot(JComponent paramJComponent)
/*      */     {
/* 1568 */       assert (this.repaintManager.repaintRoot == null);
/* 1569 */       if (this.repaintManager.painting) {
/* 1570 */         this.repaintManager.repaintRoot = paramJComponent;
/*      */       }
/*      */       else
/* 1573 */         paramJComponent.repaint();
/*      */     }
/*      */ 
/*      */     protected boolean isRepaintingRoot()
/*      */     {
/* 1582 */       return this.isRepaintingRoot;
/*      */     }
/*      */ 
/*      */     protected void dispose()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class ProcessingRunnable
/*      */     implements Runnable
/*      */   {
/*      */     private boolean pending;
/*      */ 
/*      */     private ProcessingRunnable()
/*      */     {
/*      */     }
/*      */ 
/*      */     public synchronized boolean markPending()
/*      */     {
/* 1656 */       if (!this.pending) {
/* 1657 */         this.pending = true;
/* 1658 */         return true;
/*      */       }
/* 1660 */       return false;
/*      */     }
/*      */ 
/*      */     public void run() {
/* 1664 */       synchronized (this) {
/* 1665 */         this.pending = false;
/*      */       }
/*      */ 
/* 1674 */       RepaintManager.this.scheduleHeavyWeightPaints();
/*      */ 
/* 1676 */       RepaintManager.this.validateInvalidComponents();
/* 1677 */       RepaintManager.this.prePaintDirtyRegions();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.RepaintManager
 * JD-Core Version:    0.6.2
 */