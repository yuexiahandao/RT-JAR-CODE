/*     */ package javax.swing;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.awt.EmbeddedFrame;
/*     */ import sun.awt.OSInfo;
/*     */ import sun.awt.OSInfo.OSType;
/*     */ 
/*     */ public class PopupFactory
/*     */ {
/*  68 */   private static final Object SharedInstanceKey = new StringBuffer("PopupFactory.SharedInstanceKey");
/*     */   private static final int MAX_CACHE_SIZE = 5;
/*     */   static final int LIGHT_WEIGHT_POPUP = 0;
/*     */   static final int MEDIUM_WEIGHT_POPUP = 1;
/*     */   static final int HEAVY_WEIGHT_POPUP = 2;
/*     */   private int popupType;
/*     */ 
/*     */   public PopupFactory()
/*     */   {
/*  94 */     this.popupType = 0;
/*     */   }
/*     */ 
/*     */   public static void setSharedInstance(PopupFactory paramPopupFactory)
/*     */   {
/* 108 */     if (paramPopupFactory == null) {
/* 109 */       throw new IllegalArgumentException("PopupFactory can not be null");
/*     */     }
/* 111 */     SwingUtilities.appContextPut(SharedInstanceKey, paramPopupFactory);
/*     */   }
/*     */ 
/*     */   public static PopupFactory getSharedInstance()
/*     */   {
/* 121 */     PopupFactory localPopupFactory = (PopupFactory)SwingUtilities.appContextGet(SharedInstanceKey);
/*     */ 
/* 124 */     if (localPopupFactory == null) {
/* 125 */       localPopupFactory = new PopupFactory();
/* 126 */       setSharedInstance(localPopupFactory);
/*     */     }
/* 128 */     return localPopupFactory;
/*     */   }
/*     */ 
/*     */   void setPopupType(int paramInt)
/*     */   {
/* 137 */     this.popupType = paramInt;
/*     */   }
/*     */ 
/*     */   int getPopupType()
/*     */   {
/* 144 */     return this.popupType;
/*     */   }
/*     */ 
/*     */   public Popup getPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     throws IllegalArgumentException
/*     */   {
/* 168 */     if (paramComponent2 == null) {
/* 169 */       throw new IllegalArgumentException("Popup.getPopup must be passed non-null contents");
/*     */     }
/*     */ 
/* 173 */     int i = getPopupType(paramComponent1, paramComponent2, paramInt1, paramInt2);
/* 174 */     Popup localPopup = getPopup(paramComponent1, paramComponent2, paramInt1, paramInt2, i);
/*     */ 
/* 176 */     if (localPopup == null)
/*     */     {
/* 178 */       localPopup = getPopup(paramComponent1, paramComponent2, paramInt1, paramInt2, 2);
/*     */     }
/* 180 */     return localPopup;
/*     */   }
/*     */ 
/*     */   private int getPopupType(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 188 */     int i = getPopupType();
/*     */ 
/* 190 */     if ((paramComponent1 == null) || (invokerInHeavyWeightPopup(paramComponent1))) {
/* 191 */       i = 2;
/*     */     }
/* 193 */     else if ((i == 0) && (!(paramComponent2 instanceof JToolTip)) && (!(paramComponent2 instanceof JPopupMenu)))
/*     */     {
/* 196 */       i = 1;
/*     */     }
/*     */ 
/* 202 */     Object localObject = paramComponent1;
/* 203 */     while (localObject != null) {
/* 204 */       if (((localObject instanceof JComponent)) && 
/* 205 */         (((JComponent)localObject).getClientProperty(ClientPropertyKey.PopupFactory_FORCE_HEAVYWEIGHT_POPUP) == Boolean.TRUE))
/*     */       {
/* 207 */         i = 2;
/* 208 */         break;
/*     */       }
/*     */ 
/* 211 */       localObject = ((Component)localObject).getParent();
/*     */     }
/*     */ 
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   private Popup getPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 223 */     if (GraphicsEnvironment.isHeadless()) {
/* 224 */       return getHeadlessPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 227 */     switch (paramInt3) {
/*     */     case 0:
/* 229 */       return getLightWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */     case 1:
/* 231 */       return getMediumWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */     case 2:
/* 233 */       Popup localPopup = getHeavyWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/* 234 */       if ((AccessController.doPrivileged(OSInfo.getOSTypeAction()) == OSInfo.OSType.MACOSX) && (paramComponent1 != null) && (EmbeddedFrame.getAppletIfAncestorOf(paramComponent1) != null))
/*     */       {
/* 237 */         ((HeavyWeightPopup)localPopup).setCacheEnabled(false);
/*     */       }
/* 239 */       return localPopup;
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   private Popup getHeadlessPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 249 */     return HeadlessPopup.getHeadlessPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private Popup getLightWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 257 */     return LightWeightPopup.getLightWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private Popup getMediumWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 266 */     return MediumWeightPopup.getMediumWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private Popup getHeavyWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */   {
/* 275 */     if (GraphicsEnvironment.isHeadless()) {
/* 276 */       return getMediumWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */     }
/* 278 */     return HeavyWeightPopup.getHeavyWeightPopup(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private boolean invokerInHeavyWeightPopup(Component paramComponent)
/*     */   {
/* 287 */     if (paramComponent != null)
/*     */     {
/* 289 */       for (Container localContainer = paramComponent.getParent(); localContainer != null; localContainer = localContainer.getParent())
/*     */       {
/* 291 */         if ((localContainer instanceof Popup.HeavyWeightWindow)) {
/* 292 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 296 */     return false;
/*     */   }
/*     */ 
/*     */   private static class ContainerPopup extends Popup
/*     */   {
/*     */     Component owner;
/*     */     int x;
/*     */     int y;
/*     */ 
/*     */     public void hide()
/*     */     {
/* 510 */       Component localComponent = getComponent();
/*     */ 
/* 512 */       if (localComponent != null) {
/* 513 */         Container localContainer = localComponent.getParent();
/*     */ 
/* 515 */         if (localContainer != null) {
/* 516 */           Rectangle localRectangle = localComponent.getBounds();
/*     */ 
/* 518 */           localContainer.remove(localComponent);
/* 519 */           localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */         }
/*     */       }
/*     */ 
/* 523 */       this.owner = null;
/*     */     }
/*     */     public void pack() {
/* 526 */       Component localComponent = getComponent();
/*     */ 
/* 528 */       if (localComponent != null)
/* 529 */         localComponent.setSize(localComponent.getPreferredSize());
/*     */     }
/*     */ 
/*     */     void reset(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 535 */       if (((paramComponent1 instanceof JFrame)) || ((paramComponent1 instanceof JDialog)) || ((paramComponent1 instanceof JWindow)))
/*     */       {
/* 539 */         paramComponent1 = ((RootPaneContainer)paramComponent1).getLayeredPane();
/*     */       }
/* 541 */       super.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */ 
/* 543 */       this.x = paramInt1;
/* 544 */       this.y = paramInt2;
/* 545 */       this.owner = paramComponent1;
/*     */     }
/*     */ 
/*     */     boolean overlappedByOwnedWindow() {
/* 549 */       Component localComponent = getComponent();
/* 550 */       if ((this.owner != null) && (localComponent != null)) {
/* 551 */         Window localWindow1 = SwingUtilities.getWindowAncestor(this.owner);
/* 552 */         if (localWindow1 == null) {
/* 553 */           return false;
/*     */         }
/* 555 */         Window[] arrayOfWindow1 = localWindow1.getOwnedWindows();
/* 556 */         if (arrayOfWindow1 != null) {
/* 557 */           Rectangle localRectangle = localComponent.getBounds();
/* 558 */           for (Window localWindow2 : arrayOfWindow1) {
/* 559 */             if ((localWindow2.isVisible()) && (localRectangle.intersects(localWindow2.getBounds())))
/*     */             {
/* 562 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 567 */       return false;
/*     */     }
/*     */ 
/*     */     boolean fitsOnScreen()
/*     */     {
/* 575 */       boolean bool = false;
/* 576 */       Component localComponent = getComponent();
/* 577 */       if ((this.owner != null) && (localComponent != null)) {
/* 578 */         int i = localComponent.getWidth();
/* 579 */         int j = localComponent.getHeight();
/*     */ 
/* 581 */         Container localContainer = (Container)SwingUtilities.getRoot(this.owner);
/*     */         Rectangle localRectangle1;
/*     */         Object localObject;
/* 582 */         if (((localContainer instanceof JFrame)) || ((localContainer instanceof JDialog)) || ((localContainer instanceof JWindow)))
/*     */         {
/* 586 */           localRectangle1 = localContainer.getBounds();
/* 587 */           localObject = localContainer.getInsets();
/* 588 */           localRectangle1.x += ((Insets)localObject).left;
/* 589 */           localRectangle1.y += ((Insets)localObject).top;
/* 590 */           localRectangle1.width -= ((Insets)localObject).left + ((Insets)localObject).right;
/* 591 */           localRectangle1.height -= ((Insets)localObject).top + ((Insets)localObject).bottom;
/*     */ 
/* 593 */           if (JPopupMenu.canPopupOverlapTaskBar()) {
/* 594 */             GraphicsConfiguration localGraphicsConfiguration = localContainer.getGraphicsConfiguration();
/*     */ 
/* 596 */             Rectangle localRectangle2 = getContainerPopupArea(localGraphicsConfiguration);
/* 597 */             bool = localRectangle1.intersection(localRectangle2).contains(this.x, this.y, i, j);
/*     */           }
/*     */           else {
/* 600 */             bool = localRectangle1.contains(this.x, this.y, i, j);
/*     */           }
/*     */         }
/* 603 */         else if ((localContainer instanceof JApplet)) {
/* 604 */           localRectangle1 = localContainer.getBounds();
/* 605 */           localObject = localContainer.getLocationOnScreen();
/* 606 */           localRectangle1.x = ((Point)localObject).x;
/* 607 */           localRectangle1.y = ((Point)localObject).y;
/* 608 */           bool = localRectangle1.contains(this.x, this.y, i, j);
/*     */         }
/*     */       }
/* 611 */       return bool;
/*     */     }
/*     */ 
/*     */     Rectangle getContainerPopupArea(GraphicsConfiguration paramGraphicsConfiguration)
/*     */     {
/* 616 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */       Rectangle localRectangle;
/*     */       Insets localInsets;
/* 618 */       if (paramGraphicsConfiguration != null)
/*     */       {
/* 621 */         localRectangle = paramGraphicsConfiguration.getBounds();
/* 622 */         localInsets = localToolkit.getScreenInsets(paramGraphicsConfiguration);
/*     */       }
/*     */       else {
/* 625 */         localRectangle = new Rectangle(localToolkit.getScreenSize());
/* 626 */         localInsets = new Insets(0, 0, 0, 0);
/*     */       }
/*     */ 
/* 629 */       localRectangle.x += localInsets.left;
/* 630 */       localRectangle.y += localInsets.top;
/* 631 */       localRectangle.width -= localInsets.left + localInsets.right;
/* 632 */       localRectangle.height -= localInsets.top + localInsets.bottom;
/* 633 */       return localRectangle;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class HeadlessPopup extends PopupFactory.ContainerPopup
/*     */   {
/*     */     private HeadlessPopup()
/*     */     {
/* 641 */       super();
/*     */     }
/*     */     static Popup getHeadlessPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2) {
/* 644 */       HeadlessPopup localHeadlessPopup = new HeadlessPopup();
/* 645 */       localHeadlessPopup.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/* 646 */       return localHeadlessPopup;
/*     */     }
/*     */ 
/*     */     Component createComponent(Component paramComponent) {
/* 650 */       return new Panel(new BorderLayout());
/*     */     }
/*     */ 
/*     */     public void show()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void hide()
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class HeavyWeightPopup extends Popup
/*     */   {
/* 304 */     private static final Object heavyWeightPopupCacheKey = new StringBuffer("PopupFactory.heavyWeightPopupCache");
/*     */ 
/* 307 */     private volatile boolean isCacheEnabled = true;
/*     */ 
/*     */     static Popup getHeavyWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 315 */       Window localWindow = paramComponent1 != null ? SwingUtilities.getWindowAncestor(paramComponent1) : null;
/*     */ 
/* 317 */       HeavyWeightPopup localHeavyWeightPopup = null;
/*     */ 
/* 319 */       if (localWindow != null) {
/* 320 */         localHeavyWeightPopup = getRecycledHeavyWeightPopup(localWindow);
/*     */       }
/*     */ 
/* 323 */       int i = 0;
/*     */       Object localObject;
/* 324 */       if ((paramComponent2 != null) && (paramComponent2.isFocusable()) && 
/* 325 */         ((paramComponent2 instanceof JPopupMenu))) {
/* 326 */         localObject = (JPopupMenu)paramComponent2;
/* 327 */         Component[] arrayOfComponent1 = ((JPopupMenu)localObject).getComponents();
/* 328 */         for (Component localComponent : arrayOfComponent1) {
/* 329 */           if ((!(localComponent instanceof MenuElement)) && (!(localComponent instanceof JSeparator)))
/*     */           {
/* 331 */             i = 1;
/* 332 */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 338 */       if ((localHeavyWeightPopup == null) || (((JWindow)localHeavyWeightPopup.getComponent()).getFocusableWindowState() != i))
/*     */       {
/* 342 */         if (localHeavyWeightPopup != null)
/*     */         {
/* 345 */           localHeavyWeightPopup._dispose();
/*     */         }
/*     */ 
/* 348 */         localHeavyWeightPopup = new HeavyWeightPopup();
/*     */       }
/*     */ 
/* 351 */       localHeavyWeightPopup.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */ 
/* 353 */       if (i != 0) {
/* 354 */         localObject = (JWindow)localHeavyWeightPopup.getComponent();
/* 355 */         ((JWindow)localObject).setFocusableWindowState(true);
/*     */ 
/* 358 */         ((JWindow)localObject).setName("###focusableSwingPopup###");
/*     */       }
/*     */ 
/* 361 */       return localHeavyWeightPopup;
/*     */     }
/*     */ 
/*     */     private static HeavyWeightPopup getRecycledHeavyWeightPopup(Window paramWindow)
/*     */     {
/* 371 */       synchronized (HeavyWeightPopup.class)
/*     */       {
/* 373 */         Map localMap = getHeavyWeightPopupCache();
/*     */         List localList;
/* 375 */         if (localMap.containsKey(paramWindow))
/* 376 */           localList = (List)localMap.get(paramWindow);
/*     */         else {
/* 378 */           return null;
/*     */         }
/* 380 */         if (localList.size() > 0) {
/* 381 */           HeavyWeightPopup localHeavyWeightPopup = (HeavyWeightPopup)localList.get(0);
/* 382 */           localList.remove(0);
/* 383 */           return localHeavyWeightPopup;
/*     */         }
/* 385 */         return null;
/*     */       }
/*     */     }
/*     */ 
/*     */     private static Map<Window, List<HeavyWeightPopup>> getHeavyWeightPopupCache()
/*     */     {
/* 395 */       synchronized (HeavyWeightPopup.class) {
/* 396 */         Object localObject1 = (Map)SwingUtilities.appContextGet(heavyWeightPopupCacheKey);
/*     */ 
/* 399 */         if (localObject1 == null) {
/* 400 */           localObject1 = new HashMap(2);
/* 401 */           SwingUtilities.appContextPut(heavyWeightPopupCacheKey, localObject1);
/*     */         }
/*     */ 
/* 404 */         return localObject1;
/*     */       }
/*     */     }
/*     */ 
/*     */     private static void recycleHeavyWeightPopup(HeavyWeightPopup paramHeavyWeightPopup)
/*     */     {
/* 412 */       synchronized (HeavyWeightPopup.class)
/*     */       {
/* 414 */         Window localWindow1 = SwingUtilities.getWindowAncestor(paramHeavyWeightPopup.getComponent());
/*     */ 
/* 416 */         Map localMap = getHeavyWeightPopupCache();
/*     */ 
/* 418 */         if (((localWindow1 instanceof Popup.DefaultFrame)) || (!localWindow1.isVisible()))
/*     */         {
/* 425 */           paramHeavyWeightPopup._dispose();
/*     */           return;
/*     */         }
/*     */         Object localObject1;
/* 427 */         if (localMap.containsKey(localWindow1)) {
/* 428 */           localObject1 = (List)localMap.get(localWindow1);
/*     */         } else {
/* 430 */           localObject1 = new ArrayList();
/* 431 */           localMap.put(localWindow1, localObject1);
/*     */ 
/* 433 */           Window localWindow2 = localWindow1;
/*     */ 
/* 435 */           localWindow2.addWindowListener(new WindowAdapter()
/*     */           {
/*     */             public void windowClosed(WindowEvent paramAnonymousWindowEvent)
/*     */             {
/*     */               List localList;
/* 439 */               synchronized (PopupFactory.HeavyWeightPopup.class) {
/* 440 */                 Map localMap = PopupFactory.HeavyWeightPopup.access$000();
/*     */ 
/* 443 */                 localList = (List)localMap.remove(this.val$w);
/*     */               }
/* 445 */               if (localList != null) {
/* 446 */                 for (int i = localList.size() - 1; 
/* 447 */                   i >= 0; i--) {
/* 448 */                   ((PopupFactory.HeavyWeightPopup)localList.get(i))._dispose();
/*     */                 }
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */ 
/* 455 */         if (((List)localObject1).size() < 5)
/* 456 */           ((List)localObject1).add(paramHeavyWeightPopup);
/*     */         else
/* 458 */           paramHeavyWeightPopup._dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     void setCacheEnabled(boolean paramBoolean)
/*     */     {
/* 467 */       this.isCacheEnabled = paramBoolean;
/*     */     }
/*     */ 
/*     */     public void hide()
/*     */     {
/* 474 */       super.hide();
/* 475 */       if (this.isCacheEnabled)
/* 476 */         recycleHeavyWeightPopup(this);
/*     */       else
/* 478 */         _dispose();
/*     */     }
/*     */ 
/*     */     void dispose()
/*     */     {
/*     */     }
/*     */ 
/*     */     void _dispose()
/*     */     {
/* 491 */       super.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LightWeightPopup extends PopupFactory.ContainerPopup
/*     */   {
/* 664 */     private static final Object lightWeightPopupCacheKey = new StringBuffer("PopupFactory.lightPopupCache");
/*     */ 
/*     */     private LightWeightPopup()
/*     */     {
/* 663 */       super();
/*     */     }
/*     */ 
/*     */     static Popup getLightWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 674 */       LightWeightPopup localLightWeightPopup = getRecycledLightWeightPopup();
/*     */ 
/* 676 */       if (localLightWeightPopup == null) {
/* 677 */         localLightWeightPopup = new LightWeightPopup();
/*     */       }
/* 679 */       localLightWeightPopup.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/* 680 */       if ((!localLightWeightPopup.fitsOnScreen()) || (localLightWeightPopup.overlappedByOwnedWindow()))
/*     */       {
/* 682 */         localLightWeightPopup.hide();
/* 683 */         return null;
/*     */       }
/* 685 */       return localLightWeightPopup;
/*     */     }
/*     */ 
/*     */     private static List<LightWeightPopup> getLightWeightPopupCache()
/*     */     {
/* 692 */       Object localObject = (List)SwingUtilities.appContextGet(lightWeightPopupCacheKey);
/*     */ 
/* 694 */       if (localObject == null) {
/* 695 */         localObject = new ArrayList();
/* 696 */         SwingUtilities.appContextPut(lightWeightPopupCacheKey, localObject);
/*     */       }
/* 698 */       return localObject;
/*     */     }
/*     */ 
/*     */     private static void recycleLightWeightPopup(LightWeightPopup paramLightWeightPopup)
/*     */     {
/* 705 */       synchronized (LightWeightPopup.class) {
/* 706 */         List localList = getLightWeightPopupCache();
/* 707 */         if (localList.size() < 5)
/* 708 */           localList.add(paramLightWeightPopup);
/*     */       }
/*     */     }
/*     */ 
/*     */     private static LightWeightPopup getRecycledLightWeightPopup()
/*     */     {
/* 718 */       synchronized (LightWeightPopup.class) {
/* 719 */         List localList = getLightWeightPopupCache();
/* 720 */         if (localList.size() > 0) {
/* 721 */           LightWeightPopup localLightWeightPopup = (LightWeightPopup)localList.get(0);
/* 722 */           localList.remove(0);
/* 723 */           return localLightWeightPopup;
/*     */         }
/* 725 */         return null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void hide()
/*     */     {
/* 735 */       super.hide();
/*     */ 
/* 737 */       Container localContainer = (Container)getComponent();
/*     */ 
/* 739 */       localContainer.removeAll();
/* 740 */       recycleLightWeightPopup(this);
/*     */     }
/*     */     public void show() {
/* 743 */       Object localObject1 = null;
/*     */ 
/* 745 */       if (this.owner != null) {
/* 746 */         localObject1 = (this.owner instanceof Container) ? (Container)this.owner : this.owner.getParent();
/*     */       }
/*     */ 
/* 750 */       for (Object localObject2 = localObject1; localObject2 != null; localObject2 = ((Container)localObject2).getParent()) {
/* 751 */         if ((localObject2 instanceof JRootPane)) {
/* 752 */           if (!(((Container)localObject2).getParent() instanceof JInternalFrame))
/*     */           {
/* 755 */             localObject1 = ((JRootPane)localObject2).getLayeredPane();
/*     */           }
/*     */         }
/* 758 */         else if ((localObject2 instanceof Window)) {
/* 759 */           if (localObject1 == null)
/* 760 */             localObject1 = localObject2;
/*     */         }
/*     */         else {
/* 763 */           if ((localObject2 instanceof JApplet))
/*     */           {
/*     */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 771 */       localObject2 = SwingUtilities.convertScreenLocationToParent((Container)localObject1, this.x, this.y);
/*     */ 
/* 773 */       Component localComponent = getComponent();
/*     */ 
/* 775 */       localComponent.setLocation(((Point)localObject2).x, ((Point)localObject2).y);
/* 776 */       if ((localObject1 instanceof JLayeredPane))
/* 777 */         ((Container)localObject1).add(localComponent, JLayeredPane.POPUP_LAYER, 0);
/*     */       else
/* 779 */         ((Container)localObject1).add(localComponent);
/*     */     }
/*     */ 
/*     */     Component createComponent(Component paramComponent)
/*     */     {
/* 784 */       JPanel localJPanel = new JPanel(new BorderLayout(), true);
/*     */ 
/* 786 */       localJPanel.setOpaque(true);
/* 787 */       return localJPanel;
/*     */     }
/*     */ 
/*     */     void reset(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 799 */       super.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */ 
/* 801 */       JComponent localJComponent = (JComponent)getComponent();
/*     */ 
/* 803 */       localJComponent.setOpaque(paramComponent2.isOpaque());
/* 804 */       localJComponent.setLocation(paramInt1, paramInt2);
/* 805 */       localJComponent.add(paramComponent2, "Center");
/* 806 */       paramComponent2.invalidate();
/* 807 */       pack();
/*     */     }
/*     */   }
/*     */   private static class MediumWeightPopup extends PopupFactory.ContainerPopup {
/* 816 */     private static final Object mediumWeightPopupCacheKey = new StringBuffer("PopupFactory.mediumPopupCache");
/*     */     private JRootPane rootPane;
/*     */ 
/* 815 */     private MediumWeightPopup() { super(); }
/*     */ 
/*     */ 
/*     */     static Popup getMediumWeightPopup(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 830 */       MediumWeightPopup localMediumWeightPopup = getRecycledMediumWeightPopup();
/*     */ 
/* 832 */       if (localMediumWeightPopup == null) {
/* 833 */         localMediumWeightPopup = new MediumWeightPopup();
/*     */       }
/* 835 */       localMediumWeightPopup.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/* 836 */       if ((!localMediumWeightPopup.fitsOnScreen()) || (localMediumWeightPopup.overlappedByOwnedWindow()))
/*     */       {
/* 838 */         localMediumWeightPopup.hide();
/* 839 */         return null;
/*     */       }
/* 841 */       return localMediumWeightPopup;
/*     */     }
/*     */ 
/*     */     private static List<MediumWeightPopup> getMediumWeightPopupCache()
/*     */     {
/* 848 */       Object localObject = (List)SwingUtilities.appContextGet(mediumWeightPopupCacheKey);
/*     */ 
/* 851 */       if (localObject == null) {
/* 852 */         localObject = new ArrayList();
/* 853 */         SwingUtilities.appContextPut(mediumWeightPopupCacheKey, localObject);
/*     */       }
/* 855 */       return localObject;
/*     */     }
/*     */ 
/*     */     private static void recycleMediumWeightPopup(MediumWeightPopup paramMediumWeightPopup)
/*     */     {
/* 862 */       synchronized (MediumWeightPopup.class) {
/* 863 */         List localList = getMediumWeightPopupCache();
/* 864 */         if (localList.size() < 5)
/* 865 */           localList.add(paramMediumWeightPopup);
/*     */       }
/*     */     }
/*     */ 
/*     */     private static MediumWeightPopup getRecycledMediumWeightPopup()
/*     */     {
/* 875 */       synchronized (MediumWeightPopup.class) {
/* 876 */         List localList = getMediumWeightPopupCache();
/* 877 */         if (localList.size() > 0) {
/* 878 */           MediumWeightPopup localMediumWeightPopup = (MediumWeightPopup)localList.get(0);
/* 879 */           localList.remove(0);
/* 880 */           return localMediumWeightPopup;
/*     */         }
/* 882 */         return null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void hide()
/*     */     {
/* 892 */       super.hide();
/* 893 */       this.rootPane.getContentPane().removeAll();
/* 894 */       recycleMediumWeightPopup(this);
/*     */     }
/*     */     public void show() {
/* 897 */       Component localComponent = getComponent();
/* 898 */       Object localObject = null;
/*     */ 
/* 900 */       if (this.owner != null) {
/* 901 */         localObject = this.owner.getParent();
/*     */       }
/*     */ 
/* 908 */       while ((!(localObject instanceof Window)) && (!(localObject instanceof Applet)) && (localObject != null))
/*     */       {
/* 910 */         localObject = ((Container)localObject).getParent();
/*     */       }
/*     */       Point localPoint;
/* 916 */       if ((localObject instanceof RootPaneContainer)) {
/* 917 */         localObject = ((RootPaneContainer)localObject).getLayeredPane();
/* 918 */         localPoint = SwingUtilities.convertScreenLocationToParent((Container)localObject, this.x, this.y);
/*     */ 
/* 920 */         localComponent.setVisible(false);
/* 921 */         localComponent.setLocation(localPoint.x, localPoint.y);
/* 922 */         ((Container)localObject).add(localComponent, JLayeredPane.POPUP_LAYER, 0);
/*     */       }
/*     */       else {
/* 925 */         localPoint = SwingUtilities.convertScreenLocationToParent((Container)localObject, this.x, this.y);
/*     */ 
/* 928 */         localComponent.setLocation(localPoint.x, localPoint.y);
/* 929 */         localComponent.setVisible(false);
/* 930 */         ((Container)localObject).add(localComponent);
/*     */       }
/* 932 */       localComponent.setVisible(true);
/*     */     }
/*     */ 
/*     */     Component createComponent(Component paramComponent) {
/* 936 */       MediumWeightComponent localMediumWeightComponent = new MediumWeightComponent();
/*     */ 
/* 938 */       this.rootPane = new JRootPane();
/*     */ 
/* 943 */       this.rootPane.setOpaque(true);
/* 944 */       localMediumWeightComponent.add(this.rootPane, "Center");
/* 945 */       return localMediumWeightComponent;
/*     */     }
/*     */ 
/*     */     void reset(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2)
/*     */     {
/* 953 */       super.reset(paramComponent1, paramComponent2, paramInt1, paramInt2);
/*     */ 
/* 955 */       Component localComponent = getComponent();
/*     */ 
/* 957 */       localComponent.setLocation(paramInt1, paramInt2);
/* 958 */       this.rootPane.getContentPane().add(paramComponent2, "Center");
/* 959 */       paramComponent2.invalidate();
/* 960 */       localComponent.validate();
/* 961 */       pack();
/*     */     }
/*     */ 
/*     */     private static class MediumWeightComponent extends Panel
/*     */       implements SwingHeavyWeight
/*     */     {
/*     */       MediumWeightComponent()
/*     */       {
/* 970 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.PopupFactory
 * JD-Core Version:    0.6.2
 */