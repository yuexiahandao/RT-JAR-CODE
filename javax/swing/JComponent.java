/*      */ package javax.swing;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.AWTKeyStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.Container;
/*      */ import java.awt.Container.AccessibleAWTContainer;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.beans.Transient;
/*      */ import java.beans.VetoableChangeListener;
/*      */ import java.beans.VetoableChangeSupport;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputValidation;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleExtendedComponent;
/*      */ import javax.accessibility.AccessibleKeyBinding;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.swing.border.AbstractBorder;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.border.TitledBorder;
/*      */ import javax.swing.event.AncestorListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import sun.awt.CausedFocusEvent.Cause;
/*      */ import sun.awt.RequestFocusController;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIClientPropertyKey;
/*      */ 
/*      */ public abstract class JComponent extends Container
/*      */   implements Serializable, TransferHandler.HasGetTransferHandler
/*      */ {
/*      */   private static final String uiClassID = "ComponentUI";
/*  196 */   private static final Hashtable<ObjectInputStream, ReadObjectCallback> readObjectCallbacks = new Hashtable(1);
/*      */   private static Set<KeyStroke> managingFocusForwardTraversalKeys;
/*      */   private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
/*      */   private static final int NOT_OBSCURED = 0;
/*      */   private static final int PARTIALLY_OBSCURED = 1;
/*      */   private static final int COMPLETELY_OBSCURED = 2;
/*      */   static boolean DEBUG_GRAPHICS_LOADED;
/*  228 */   private static final Object INPUT_VERIFIER_SOURCE_KEY = new StringBuilder("InputVerifierSourceKey");
/*      */   private boolean isAlignmentXSet;
/*      */   private float alignmentX;
/*      */   private boolean isAlignmentYSet;
/*      */   private float alignmentY;
/*      */   protected transient ComponentUI ui;
/*  246 */   protected EventListenerList listenerList = new EventListenerList();
/*      */   private transient ArrayTable clientProperties;
/*      */   private VetoableChangeSupport vetoableChangeSupport;
/*      */   private boolean autoscrolls;
/*      */   private Border border;
/*      */   private int flags;
/*  258 */   private InputVerifier inputVerifier = null;
/*      */ 
/*  260 */   private boolean verifyInputWhenFocusTarget = true;
/*      */   transient Component paintingChild;
/*      */   public static final int WHEN_FOCUSED = 0;
/*      */   public static final int WHEN_ANCESTOR_OF_FOCUSED_COMPONENT = 1;
/*      */   public static final int WHEN_IN_FOCUSED_WINDOW = 2;
/*      */   public static final int UNDEFINED_CONDITION = -1;
/*      */   private static final String KEYBOARD_BINDINGS_KEY = "_KeyboardBindings";
/*      */   private static final String WHEN_IN_FOCUSED_WINDOW_BINDINGS = "_WhenInFocusedWindow";
/*      */   public static final String TOOL_TIP_TEXT_KEY = "ToolTipText";
/*      */   private static final String NEXT_FOCUS = "nextFocus";
/*      */   private JPopupMenu popupMenu;
/*      */   private static final int IS_DOUBLE_BUFFERED = 0;
/*      */   private static final int ANCESTOR_USING_BUFFER = 1;
/*      */   private static final int IS_PAINTING_TILE = 2;
/*      */   private static final int IS_OPAQUE = 3;
/*      */   private static final int KEY_EVENTS_ENABLED = 4;
/*      */   private static final int FOCUS_INPUTMAP_CREATED = 5;
/*      */   private static final int ANCESTOR_INPUTMAP_CREATED = 6;
/*      */   private static final int WIF_INPUTMAP_CREATED = 7;
/*      */   private static final int ACTIONMAP_CREATED = 8;
/*      */   private static final int CREATED_DOUBLE_BUFFER = 9;
/*      */   private static final int IS_PRINTING = 11;
/*      */   private static final int IS_PRINTING_ALL = 12;
/*      */   private static final int IS_REPAINTING = 13;
/*      */   private static final int WRITE_OBJ_COUNTER_FIRST = 14;
/*      */   private static final int RESERVED_1 = 15;
/*      */   private static final int RESERVED_2 = 16;
/*      */   private static final int RESERVED_3 = 17;
/*      */   private static final int RESERVED_4 = 18;
/*      */   private static final int RESERVED_5 = 19;
/*      */   private static final int RESERVED_6 = 20;
/*      */   private static final int WRITE_OBJ_COUNTER_LAST = 21;
/*      */   private static final int REQUEST_FOCUS_DISABLED = 22;
/*      */   private static final int INHERITS_POPUP_MENU = 23;
/*      */   private static final int OPAQUE_SET = 24;
/*      */   private static final int AUTOSCROLLS_SET = 25;
/*      */   private static final int FOCUS_TRAVERSAL_KEYS_FORWARD_SET = 26;
/*      */   private static final int FOCUS_TRAVERSAL_KEYS_BACKWARD_SET = 27;
/*      */   private static final int REVALIDATE_RUNNABLE_SCHEDULED = 28;
/*  361 */   private static List<Rectangle> tempRectangles = new ArrayList(11);
/*      */   private InputMap focusInputMap;
/*      */   private InputMap ancestorInputMap;
/*      */   private ComponentInputMap windowInputMap;
/*      */   private ActionMap actionMap;
/*      */   private static final String defaultLocale = "JComponent.defaultLocale";
/*      */   private static Component componentObtainingGraphicsFrom;
/*  377 */   private static Object componentObtainingGraphicsFromLock = new StringBuilder("componentObtainingGraphicsFrom");
/*      */   private transient Object aaTextInfo;
/* 3554 */   static final RequestFocusController focusController = new RequestFocusController()
/*      */   {
/*      */     public boolean acceptRequestFocus(Component paramAnonymousComponent1, Component paramAnonymousComponent2, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, CausedFocusEvent.Cause paramAnonymousCause)
/*      */     {
/* 3560 */       if ((paramAnonymousComponent2 == null) || (!(paramAnonymousComponent2 instanceof JComponent))) {
/* 3561 */         return true;
/*      */       }
/*      */ 
/* 3564 */       if ((paramAnonymousComponent1 == null) || (!(paramAnonymousComponent1 instanceof JComponent))) {
/* 3565 */         return true;
/*      */       }
/*      */ 
/* 3568 */       JComponent localJComponent1 = (JComponent)paramAnonymousComponent2;
/* 3569 */       if (!localJComponent1.getVerifyInputWhenFocusTarget()) {
/* 3570 */         return true;
/*      */       }
/*      */ 
/* 3573 */       JComponent localJComponent2 = (JComponent)paramAnonymousComponent1;
/* 3574 */       InputVerifier localInputVerifier = localJComponent2.getInputVerifier();
/*      */ 
/* 3576 */       if (localInputVerifier == null) {
/* 3577 */         return true;
/*      */       }
/* 3579 */       Object localObject1 = SwingUtilities.appContextGet(JComponent.INPUT_VERIFIER_SOURCE_KEY);
/*      */ 
/* 3581 */       if (localObject1 == localJComponent2)
/*      */       {
/* 3584 */         return true;
/*      */       }
/* 3586 */       SwingUtilities.appContextPut(JComponent.INPUT_VERIFIER_SOURCE_KEY, localJComponent2);
/*      */       try
/*      */       {
/* 3589 */         return localInputVerifier.shouldYieldFocus(localJComponent2);
/*      */       } finally {
/* 3591 */         if (localObject1 != null)
/*      */         {
/* 3597 */           SwingUtilities.appContextPut(JComponent.INPUT_VERIFIER_SOURCE_KEY, localObject1);
/*      */         }
/*      */         else
/* 3600 */           SwingUtilities.appContextRemove(JComponent.INPUT_VERIFIER_SOURCE_KEY);
/*      */       }
/*      */     }
/* 3554 */   };
/*      */ 
/* 3648 */   protected AccessibleContext accessibleContext = null;
/*      */ 
/*      */   static Graphics safelyGetGraphics(Component paramComponent)
/*      */   {
/*  386 */     return safelyGetGraphics(paramComponent, SwingUtilities.getRoot(paramComponent));
/*      */   }
/*      */ 
/*      */   static Graphics safelyGetGraphics(Component paramComponent1, Component paramComponent2) {
/*  390 */     synchronized (componentObtainingGraphicsFromLock) {
/*  391 */       componentObtainingGraphicsFrom = paramComponent2;
/*  392 */       Graphics localGraphics = paramComponent1.getGraphics();
/*  393 */       componentObtainingGraphicsFrom = null;
/*  394 */       return localGraphics;
/*      */     }
/*      */   }
/*      */ 
/*      */   static void getGraphicsInvoked(Component paramComponent) {
/*  399 */     if (!isComponentObtainingGraphicsFrom(paramComponent)) {
/*  400 */       JRootPane localJRootPane = ((RootPaneContainer)paramComponent).getRootPane();
/*  401 */       if (localJRootPane != null)
/*  402 */         localJRootPane.disableTrueDoubleBuffering();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isComponentObtainingGraphicsFrom(Component paramComponent)
/*      */   {
/*  413 */     synchronized (componentObtainingGraphicsFromLock) {
/*  414 */       return componentObtainingGraphicsFrom == paramComponent;
/*      */     }
/*      */   }
/*      */ 
/*      */   static Set<KeyStroke> getManagingFocusForwardTraversalKeys()
/*      */   {
/*  423 */     synchronized (JComponent.class) {
/*  424 */       if (managingFocusForwardTraversalKeys == null) {
/*  425 */         managingFocusForwardTraversalKeys = new HashSet(1);
/*  426 */         managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 2));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  431 */     return managingFocusForwardTraversalKeys;
/*      */   }
/*      */ 
/*      */   static Set<KeyStroke> getManagingFocusBackwardTraversalKeys()
/*      */   {
/*  439 */     synchronized (JComponent.class) {
/*  440 */       if (managingFocusBackwardTraversalKeys == null) {
/*  441 */         managingFocusBackwardTraversalKeys = new HashSet(1);
/*  442 */         managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 3));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  448 */     return managingFocusBackwardTraversalKeys;
/*      */   }
/*      */ 
/*      */   private static Rectangle fetchRectangle() {
/*  452 */     synchronized (tempRectangles)
/*      */     {
/*  454 */       int i = tempRectangles.size();
/*      */       Rectangle localRectangle;
/*  455 */       if (i > 0) {
/*  456 */         localRectangle = (Rectangle)tempRectangles.remove(i - 1);
/*      */       }
/*      */       else {
/*  459 */         localRectangle = new Rectangle(0, 0, 0, 0);
/*      */       }
/*  461 */       return localRectangle;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void recycleRectangle(Rectangle paramRectangle) {
/*  466 */     synchronized (tempRectangles) {
/*  467 */       tempRectangles.add(paramRectangle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setInheritsPopupMenu(boolean paramBoolean)
/*      */   {
/*  490 */     boolean bool = getFlag(23);
/*  491 */     setFlag(23, paramBoolean);
/*  492 */     firePropertyChange("inheritsPopupMenu", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getInheritsPopupMenu()
/*      */   {
/*  502 */     return getFlag(23);
/*      */   }
/*      */ 
/*      */   public void setComponentPopupMenu(JPopupMenu paramJPopupMenu)
/*      */   {
/*  530 */     if (paramJPopupMenu != null) {
/*  531 */       enableEvents(16L);
/*      */     }
/*  533 */     JPopupMenu localJPopupMenu = this.popupMenu;
/*  534 */     this.popupMenu = paramJPopupMenu;
/*  535 */     firePropertyChange("componentPopupMenu", localJPopupMenu, paramJPopupMenu);
/*      */   }
/*      */ 
/*      */   public JPopupMenu getComponentPopupMenu()
/*      */   {
/*  552 */     if (!getInheritsPopupMenu()) {
/*  553 */       return this.popupMenu;
/*      */     }
/*      */ 
/*  556 */     if (this.popupMenu == null)
/*      */     {
/*  558 */       Container localContainer = getParent();
/*  559 */       while (localContainer != null) {
/*  560 */         if ((localContainer instanceof JComponent)) {
/*  561 */           return ((JComponent)localContainer).getComponentPopupMenu();
/*      */         }
/*  563 */         if (((localContainer instanceof Window)) || ((localContainer instanceof Applet)))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*  568 */         localContainer = localContainer.getParent();
/*      */       }
/*  570 */       return null;
/*      */     }
/*      */ 
/*  573 */     return this.popupMenu;
/*      */   }
/*      */ 
/*      */   public JComponent()
/*      */   {
/*  591 */     enableEvents(8L);
/*  592 */     if (isManagingFocus()) {
/*  593 */       LookAndFeel.installProperty(this, "focusTraversalKeysForward", getManagingFocusForwardTraversalKeys());
/*      */ 
/*  596 */       LookAndFeel.installProperty(this, "focusTraversalKeysBackward", getManagingFocusBackwardTraversalKeys());
/*      */     }
/*      */ 
/*  601 */     super.setLocale(getDefaultLocale());
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void setUI(ComponentUI paramComponentUI)
/*      */   {
/*  657 */     uninstallUIAndProperties();
/*      */ 
/*  660 */     this.aaTextInfo = UIManager.getDefaults().get(SwingUtilities2.AA_TEXT_PROPERTY_KEY);
/*      */ 
/*  662 */     ComponentUI localComponentUI = this.ui;
/*  663 */     this.ui = paramComponentUI;
/*  664 */     if (this.ui != null) {
/*  665 */       this.ui.installUI(this);
/*      */     }
/*      */ 
/*  668 */     firePropertyChange("UI", localComponentUI, paramComponentUI);
/*  669 */     revalidate();
/*  670 */     repaint();
/*      */   }
/*      */ 
/*      */   private void uninstallUIAndProperties()
/*      */   {
/*  679 */     if (this.ui != null) {
/*  680 */       this.ui.uninstallUI(this);
/*      */ 
/*  682 */       if (this.clientProperties != null)
/*  683 */         synchronized (this.clientProperties) {
/*  684 */           Object[] arrayOfObject1 = this.clientProperties.getKeys(null);
/*      */ 
/*  686 */           if (arrayOfObject1 != null)
/*  687 */             for (Object localObject1 : arrayOfObject1)
/*  688 */               if ((localObject1 instanceof UIClientPropertyKey))
/*  689 */                 putClientProperty(localObject1, null);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  716 */     return "ComponentUI";
/*      */   }
/*      */ 
/*      */   protected Graphics getComponentGraphics(Graphics paramGraphics)
/*      */   {
/*  731 */     Object localObject = paramGraphics;
/*  732 */     if ((this.ui != null) && (DEBUG_GRAPHICS_LOADED) && 
/*  733 */       (DebugGraphics.debugComponentCount() != 0) && (shouldDebugGraphics() != 0) && (!(paramGraphics instanceof DebugGraphics)))
/*      */     {
/*  736 */       localObject = new DebugGraphics(paramGraphics, this);
/*      */     }
/*      */ 
/*  739 */     ((Graphics)localObject).setColor(getForeground());
/*  740 */     ((Graphics)localObject).setFont(getFont());
/*      */ 
/*  742 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected void paintComponent(Graphics paramGraphics)
/*      */   {
/*  776 */     if (this.ui != null) {
/*  777 */       Graphics localGraphics = paramGraphics == null ? null : paramGraphics.create();
/*      */       try {
/*  779 */         this.ui.update(localGraphics, this);
/*      */       }
/*      */       finally {
/*  782 */         localGraphics.dispose();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintChildren(Graphics paramGraphics)
/*      */   {
/*  799 */     Graphics localGraphics1 = paramGraphics;
/*      */ 
/*  801 */     synchronized (getTreeLock()) {
/*  802 */       int i = getComponentCount() - 1;
/*  803 */       if (i < 0) {
/*  804 */         return;
/*      */       }
/*      */ 
/*  808 */       if ((this.paintingChild != null) && ((this.paintingChild instanceof JComponent)) && (this.paintingChild.isOpaque()))
/*      */       {
/*  811 */         while ((i >= 0) && 
/*  812 */           (getComponent(i) != this.paintingChild)) {
/*  811 */           i--;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  817 */       Rectangle localRectangle1 = fetchRectangle();
/*  818 */       int j = (!isOptimizedDrawingEnabled()) && (checkIfChildObscuredBySibling()) ? 1 : 0;
/*      */ 
/*  820 */       Rectangle localRectangle2 = null;
/*  821 */       if (j != 0) {
/*  822 */         localRectangle2 = localGraphics1.getClipBounds();
/*  823 */         if (localRectangle2 == null) {
/*  824 */           localRectangle2 = new Rectangle(0, 0, getWidth(), getHeight());
/*      */         }
/*      */       }
/*      */ 
/*  828 */       boolean bool1 = getFlag(11);
/*  829 */       Window localWindow = SwingUtilities.getWindowAncestor(this);
/*  830 */       int k = (localWindow == null) || (localWindow.isOpaque()) ? 1 : 0;
/*  831 */       for (; i >= 0; i--) {
/*  832 */         Component localComponent = getComponent(i);
/*  833 */         if (localComponent != null)
/*      */         {
/*  837 */           boolean bool2 = localComponent instanceof JComponent;
/*      */ 
/*  841 */           if (((k == 0) || (bool2) || (isLightweightComponent(localComponent))) && (localComponent.isVisible()))
/*      */           {
/*  846 */             Rectangle localRectangle3 = localComponent.getBounds(localRectangle1);
/*      */ 
/*  848 */             boolean bool3 = paramGraphics.hitClip(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*      */ 
/*  851 */             if (bool3)
/*      */             {
/*      */               int n;
/*  852 */               if ((j != 0) && (i > 0)) {
/*  853 */                 int m = localRectangle3.x;
/*  854 */                 n = localRectangle3.y;
/*  855 */                 int i1 = localRectangle3.width;
/*  856 */                 int i2 = localRectangle3.height;
/*  857 */                 SwingUtilities.computeIntersection(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, localRectangle3);
/*      */ 
/*  861 */                 if (getObscuredState(i, localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height) != 2)
/*      */                 {
/*  865 */                   localRectangle3.x = m;
/*  866 */                   localRectangle3.y = n;
/*  867 */                   localRectangle3.width = i1;
/*  868 */                   localRectangle3.height = i2;
/*      */                 }
/*      */               } else { Graphics localGraphics2 = localGraphics1.create(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*      */ 
/*  872 */                 localGraphics2.setColor(localComponent.getForeground());
/*  873 */                 localGraphics2.setFont(localComponent.getFont());
/*  874 */                 n = 0;
/*      */                 try {
/*  876 */                   if (bool2) {
/*  877 */                     if (getFlag(1)) {
/*  878 */                       ((JComponent)localComponent).setFlag(1, true);
/*      */ 
/*  880 */                       n = 1;
/*      */                     }
/*  882 */                     if (getFlag(2)) {
/*  883 */                       ((JComponent)localComponent).setFlag(2, true);
/*      */ 
/*  885 */                       n = 1;
/*      */                     }
/*  887 */                     if (!bool1) {
/*  888 */                       localComponent.paint(localGraphics2);
/*      */                     }
/*  891 */                     else if (!getFlag(12)) {
/*  892 */                       localComponent.print(localGraphics2);
/*      */                     }
/*      */                     else {
/*  895 */                       localComponent.printAll(localGraphics2);
/*      */                     }
/*      */ 
/*      */                   }
/*  901 */                   else if (!bool1) {
/*  902 */                     localComponent.paint(localGraphics2);
/*      */                   }
/*  905 */                   else if (!getFlag(12)) {
/*  906 */                     localComponent.print(localGraphics2);
/*      */                   }
/*      */                   else {
/*  909 */                     localComponent.printAll(localGraphics2);
/*      */                   }
/*      */                 }
/*      */                 finally
/*      */                 {
/*  914 */                   localGraphics2.dispose();
/*  915 */                   if (n != 0) {
/*  916 */                     ((JComponent)localComponent).setFlag(1, false);
/*      */ 
/*  918 */                     ((JComponent)localComponent).setFlag(2, false);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  926 */       recycleRectangle(localRectangle1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintBorder(Graphics paramGraphics)
/*      */   {
/*  946 */     Border localBorder = getBorder();
/*  947 */     if (localBorder != null)
/*  948 */       localBorder.paintBorder(this, paramGraphics, 0, 0, getWidth(), getHeight());
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics)
/*      */   {
/*  964 */     paint(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/*  994 */     int i = 0;
/*      */ 
/*  996 */     if ((getWidth() <= 0) || (getHeight() <= 0)) {
/*  997 */       return;
/*      */     }
/*      */ 
/* 1000 */     Graphics localGraphics1 = getComponentGraphics(paramGraphics);
/* 1001 */     Graphics localGraphics2 = localGraphics1.create();
/*      */     try {
/* 1003 */       RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/* 1004 */       Rectangle localRectangle = localGraphics2.getClipBounds();
/*      */       int k;
/*      */       int j;
/*      */       int m;
/*      */       int n;
/* 1009 */       if (localRectangle == null) {
/* 1010 */         j = k = 0;
/* 1011 */         m = getWidth();
/* 1012 */         n = getHeight();
/*      */       }
/*      */       else {
/* 1015 */         j = localRectangle.x;
/* 1016 */         k = localRectangle.y;
/* 1017 */         m = localRectangle.width;
/* 1018 */         n = localRectangle.height;
/*      */       }
/*      */ 
/* 1021 */       if (m > getWidth()) {
/* 1022 */         m = getWidth();
/*      */       }
/* 1024 */       if (n > getHeight()) {
/* 1025 */         n = getHeight();
/*      */       }
/*      */ 
/* 1028 */       if ((getParent() != null) && (!(getParent() instanceof JComponent))) {
/* 1029 */         adjustPaintFlags();
/* 1030 */         i = 1;
/*      */       }
/*      */ 
/* 1034 */       boolean bool = getFlag(11);
/* 1035 */       if ((!bool) && (localRepaintManager.isDoubleBufferingEnabled()) && (!getFlag(1)) && (isDoubleBuffered()) && ((getFlag(13)) || (localRepaintManager.isPainting())))
/*      */       {
/* 1039 */         localRepaintManager.beginPaint();
/*      */         try {
/* 1041 */           localRepaintManager.paint(this, this, localGraphics2, j, k, m, n);
/*      */         }
/*      */         finally {
/* 1044 */           localRepaintManager.endPaint();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1049 */         if (localRectangle == null) {
/* 1050 */           localGraphics2.setClip(j, k, m, n);
/*      */         }
/*      */ 
/* 1053 */         if (!rectangleIsObscured(j, k, m, n)) {
/* 1054 */           if (!bool) {
/* 1055 */             paintComponent(localGraphics2);
/* 1056 */             paintBorder(localGraphics2);
/*      */           }
/*      */           else {
/* 1059 */             printComponent(localGraphics2);
/* 1060 */             printBorder(localGraphics2);
/*      */           }
/*      */         }
/* 1063 */         if (!bool) {
/* 1064 */           paintChildren(localGraphics2);
/*      */         }
/*      */         else
/* 1067 */           printChildren(localGraphics2);
/*      */       }
/*      */     }
/*      */     finally {
/* 1071 */       localGraphics2.dispose();
/* 1072 */       if (i != 0) {
/* 1073 */         setFlag(1, false);
/* 1074 */         setFlag(2, false);
/* 1075 */         setFlag(11, false);
/* 1076 */         setFlag(12, false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void paintForceDoubleBuffered(Graphics paramGraphics)
/*      */   {
/* 1086 */     RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/* 1087 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/* 1088 */     localRepaintManager.beginPaint();
/* 1089 */     setFlag(13, true);
/*      */     try {
/* 1091 */       localRepaintManager.paint(this, this, paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     } finally {
/* 1093 */       localRepaintManager.endPaint();
/* 1094 */       setFlag(13, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean isPainting()
/*      */   {
/* 1103 */     Object localObject = this;
/* 1104 */     while (localObject != null) {
/* 1105 */       if (((localObject instanceof JComponent)) && (((JComponent)localObject).getFlag(1)))
/*      */       {
/* 1107 */         return true;
/*      */       }
/* 1109 */       localObject = ((Container)localObject).getParent();
/*      */     }
/* 1111 */     return false;
/*      */   }
/*      */ 
/*      */   private void adjustPaintFlags()
/*      */   {
/* 1117 */     for (Container localContainer = getParent(); localContainer != null; localContainer = localContainer.getParent())
/*      */     {
/* 1119 */       if ((localContainer instanceof JComponent)) {
/* 1120 */         JComponent localJComponent = (JComponent)localContainer;
/* 1121 */         if (localJComponent.getFlag(1))
/* 1122 */           setFlag(1, true);
/* 1123 */         if (localJComponent.getFlag(2))
/* 1124 */           setFlag(2, true);
/* 1125 */         if (localJComponent.getFlag(11))
/* 1126 */           setFlag(11, true);
/* 1127 */         if (!localJComponent.getFlag(12)) break;
/* 1128 */         setFlag(12, true); break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printAll(Graphics paramGraphics)
/*      */   {
/* 1145 */     setFlag(12, true);
/*      */     try {
/* 1147 */       print(paramGraphics);
/*      */     }
/*      */     finally {
/* 1150 */       setFlag(12, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void print(Graphics paramGraphics)
/*      */   {
/* 1198 */     setFlag(11, true);
/* 1199 */     firePropertyChange("paintingForPrint", false, true);
/*      */     try {
/* 1201 */       paint(paramGraphics);
/*      */     }
/*      */     finally {
/* 1204 */       setFlag(11, false);
/* 1205 */       firePropertyChange("paintingForPrint", true, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void printComponent(Graphics paramGraphics)
/*      */   {
/* 1219 */     paintComponent(paramGraphics);
/*      */   }
/*      */ 
/*      */   protected void printChildren(Graphics paramGraphics)
/*      */   {
/* 1232 */     paintChildren(paramGraphics);
/*      */   }
/*      */ 
/*      */   protected void printBorder(Graphics paramGraphics)
/*      */   {
/* 1245 */     paintBorder(paramGraphics);
/*      */   }
/*      */ 
/*      */   public boolean isPaintingTile()
/*      */   {
/* 1259 */     return getFlag(2);
/*      */   }
/*      */ 
/*      */   public final boolean isPaintingForPrint()
/*      */   {
/* 1289 */     return getFlag(11);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean isManagingFocus()
/*      */   {
/* 1312 */     return false;
/*      */   }
/*      */ 
/*      */   private void registerNextFocusableComponent() {
/* 1316 */     registerNextFocusableComponent(getNextFocusableComponent());
/*      */   }
/*      */ 
/*      */   private void registerNextFocusableComponent(Component paramComponent)
/*      */   {
/* 1321 */     if (paramComponent == null) {
/* 1322 */       return;
/*      */     }
/*      */ 
/* 1325 */     Container localContainer = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
/*      */ 
/* 1327 */     Object localObject = localContainer.getFocusTraversalPolicy();
/* 1328 */     if (!(localObject instanceof LegacyGlueFocusTraversalPolicy)) {
/* 1329 */       localObject = new LegacyGlueFocusTraversalPolicy((FocusTraversalPolicy)localObject);
/* 1330 */       localContainer.setFocusTraversalPolicy((FocusTraversalPolicy)localObject);
/*      */     }
/* 1332 */     ((LegacyGlueFocusTraversalPolicy)localObject).setNextFocusableComponent(this, paramComponent);
/*      */   }
/*      */ 
/*      */   private void deregisterNextFocusableComponent()
/*      */   {
/* 1337 */     Component localComponent = getNextFocusableComponent();
/* 1338 */     if (localComponent == null) {
/* 1339 */       return;
/*      */     }
/*      */ 
/* 1342 */     Container localContainer = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
/*      */ 
/* 1344 */     if (localContainer == null) {
/* 1345 */       return;
/*      */     }
/* 1347 */     FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/* 1348 */     if ((localFocusTraversalPolicy instanceof LegacyGlueFocusTraversalPolicy))
/* 1349 */       ((LegacyGlueFocusTraversalPolicy)localFocusTraversalPolicy).unsetNextFocusableComponent(this, localComponent);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setNextFocusableComponent(Component paramComponent)
/*      */   {
/* 1377 */     boolean bool = isDisplayable();
/* 1378 */     if (bool) {
/* 1379 */       deregisterNextFocusableComponent();
/*      */     }
/* 1381 */     putClientProperty("nextFocus", paramComponent);
/* 1382 */     if (bool)
/* 1383 */       registerNextFocusableComponent(paramComponent);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Component getNextFocusableComponent()
/*      */   {
/* 1407 */     return (Component)getClientProperty("nextFocus");
/*      */   }
/*      */ 
/*      */   public void setRequestFocusEnabled(boolean paramBoolean)
/*      */   {
/* 1434 */     setFlag(22, !paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isRequestFocusEnabled()
/*      */   {
/* 1455 */     return !getFlag(22);
/*      */   }
/*      */ 
/*      */   public void requestFocus()
/*      */   {
/* 1477 */     super.requestFocus();
/*      */   }
/*      */ 
/*      */   public boolean requestFocus(boolean paramBoolean)
/*      */   {
/* 1503 */     return super.requestFocus(paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean requestFocusInWindow()
/*      */   {
/* 1524 */     return super.requestFocusInWindow();
/*      */   }
/*      */ 
/*      */   protected boolean requestFocusInWindow(boolean paramBoolean)
/*      */   {
/* 1546 */     return super.requestFocusInWindow(paramBoolean);
/*      */   }
/*      */ 
/*      */   public void grabFocus()
/*      */   {
/* 1562 */     requestFocus();
/*      */   }
/*      */ 
/*      */   public void setVerifyInputWhenFocusTarget(boolean paramBoolean)
/*      */   {
/* 1588 */     boolean bool = this.verifyInputWhenFocusTarget;
/*      */ 
/* 1590 */     this.verifyInputWhenFocusTarget = paramBoolean;
/* 1591 */     firePropertyChange("verifyInputWhenFocusTarget", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getVerifyInputWhenFocusTarget()
/*      */   {
/* 1611 */     return this.verifyInputWhenFocusTarget;
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/* 1625 */     return SwingUtilities2.getFontMetrics(this, paramFont);
/*      */   }
/*      */ 
/*      */   public void setPreferredSize(Dimension paramDimension)
/*      */   {
/* 1639 */     super.setPreferredSize(paramDimension);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Dimension getPreferredSize()
/*      */   {
/* 1656 */     if (isPreferredSizeSet()) {
/* 1657 */       return super.getPreferredSize();
/*      */     }
/* 1659 */     Dimension localDimension = null;
/* 1660 */     if (this.ui != null) {
/* 1661 */       localDimension = this.ui.getPreferredSize(this);
/*      */     }
/* 1663 */     return localDimension != null ? localDimension : super.getPreferredSize();
/*      */   }
/*      */ 
/*      */   public void setMaximumSize(Dimension paramDimension)
/*      */   {
/* 1682 */     super.setMaximumSize(paramDimension);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Dimension getMaximumSize()
/*      */   {
/* 1698 */     if (isMaximumSizeSet()) {
/* 1699 */       return super.getMaximumSize();
/*      */     }
/* 1701 */     Dimension localDimension = null;
/* 1702 */     if (this.ui != null) {
/* 1703 */       localDimension = this.ui.getMaximumSize(this);
/*      */     }
/* 1705 */     return localDimension != null ? localDimension : super.getMaximumSize();
/*      */   }
/*      */ 
/*      */   public void setMinimumSize(Dimension paramDimension)
/*      */   {
/* 1723 */     super.setMinimumSize(paramDimension);
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Dimension getMinimumSize()
/*      */   {
/* 1738 */     if (isMinimumSizeSet()) {
/* 1739 */       return super.getMinimumSize();
/*      */     }
/* 1741 */     Dimension localDimension = null;
/* 1742 */     if (this.ui != null) {
/* 1743 */       localDimension = this.ui.getMinimumSize(this);
/*      */     }
/* 1745 */     return localDimension != null ? localDimension : super.getMinimumSize();
/*      */   }
/*      */ 
/*      */   public boolean contains(int paramInt1, int paramInt2)
/*      */   {
/* 1757 */     return this.ui != null ? this.ui.contains(this, paramInt1, paramInt2) : super.contains(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setBorder(Border paramBorder)
/*      */   {
/* 1792 */     Border localBorder = this.border;
/*      */ 
/* 1794 */     this.border = paramBorder;
/* 1795 */     firePropertyChange("border", localBorder, paramBorder);
/* 1796 */     if (paramBorder != localBorder) {
/* 1797 */       if ((paramBorder == null) || (localBorder == null) || (!paramBorder.getBorderInsets(this).equals(localBorder.getBorderInsets(this))))
/*      */       {
/* 1799 */         revalidate();
/*      */       }
/* 1801 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Border getBorder()
/*      */   {
/* 1813 */     return this.border;
/*      */   }
/*      */ 
/*      */   public Insets getInsets()
/*      */   {
/* 1824 */     if (this.border != null) {
/* 1825 */       return this.border.getBorderInsets(this);
/*      */     }
/* 1827 */     return super.getInsets();
/*      */   }
/*      */ 
/*      */   public Insets getInsets(Insets paramInsets)
/*      */   {
/* 1845 */     if (paramInsets == null) {
/* 1846 */       paramInsets = new Insets(0, 0, 0, 0);
/*      */     }
/* 1848 */     if (this.border != null) {
/* 1849 */       if ((this.border instanceof AbstractBorder)) {
/* 1850 */         return ((AbstractBorder)this.border).getBorderInsets(this, paramInsets);
/*      */       }
/*      */ 
/* 1854 */       return this.border.getBorderInsets(this);
/*      */     }
/*      */ 
/* 1859 */     paramInsets.left = (paramInsets.top = paramInsets.right = paramInsets.bottom = 0);
/* 1860 */     return paramInsets;
/*      */   }
/*      */ 
/*      */   public float getAlignmentY()
/*      */   {
/* 1873 */     if (this.isAlignmentYSet) {
/* 1874 */       return this.alignmentY;
/*      */     }
/* 1876 */     return super.getAlignmentY();
/*      */   }
/*      */ 
/*      */   public void setAlignmentY(float paramFloat)
/*      */   {
/* 1888 */     this.alignmentY = (paramFloat < 0.0F ? 0.0F : paramFloat > 1.0F ? 1.0F : paramFloat);
/* 1889 */     this.isAlignmentYSet = true;
/*      */   }
/*      */ 
/*      */   public float getAlignmentX()
/*      */   {
/* 1902 */     if (this.isAlignmentXSet) {
/* 1903 */       return this.alignmentX;
/*      */     }
/* 1905 */     return super.getAlignmentX();
/*      */   }
/*      */ 
/*      */   public void setAlignmentX(float paramFloat)
/*      */   {
/* 1917 */     this.alignmentX = (paramFloat < 0.0F ? 0.0F : paramFloat > 1.0F ? 1.0F : paramFloat);
/* 1918 */     this.isAlignmentXSet = true;
/*      */   }
/*      */ 
/*      */   public void setInputVerifier(InputVerifier paramInputVerifier)
/*      */   {
/* 1932 */     InputVerifier localInputVerifier = (InputVerifier)getClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER);
/*      */ 
/* 1934 */     putClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER, paramInputVerifier);
/* 1935 */     firePropertyChange("inputVerifier", localInputVerifier, paramInputVerifier);
/*      */   }
/*      */ 
/*      */   public InputVerifier getInputVerifier()
/*      */   {
/* 1946 */     return (InputVerifier)getClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER);
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/* 1956 */     if ((DEBUG_GRAPHICS_LOADED) && (shouldDebugGraphics() != 0)) {
/* 1957 */       DebugGraphics localDebugGraphics = new DebugGraphics(super.getGraphics(), this);
/*      */ 
/* 1959 */       return localDebugGraphics;
/*      */     }
/* 1961 */     return super.getGraphics();
/*      */   }
/*      */ 
/*      */   public void setDebugGraphicsOptions(int paramInt)
/*      */   {
/* 1991 */     DebugGraphics.setDebugOptions(this, paramInt);
/*      */   }
/*      */ 
/*      */   public int getDebugGraphicsOptions()
/*      */   {
/* 2010 */     return DebugGraphics.getDebugOptions(this);
/*      */   }
/*      */ 
/*      */   int shouldDebugGraphics()
/*      */   {
/* 2019 */     return DebugGraphics.shouldComponentDebug(this);
/*      */   }
/*      */ 
/*      */   public void registerKeyboardAction(ActionListener paramActionListener, String paramString, KeyStroke paramKeyStroke, int paramInt)
/*      */   {
/* 2088 */     InputMap localInputMap = getInputMap(paramInt, true);
/*      */ 
/* 2090 */     if (localInputMap != null) {
/* 2091 */       ActionMap localActionMap = getActionMap(true);
/* 2092 */       ActionStandin localActionStandin = new ActionStandin(paramActionListener, paramString);
/* 2093 */       localInputMap.put(paramKeyStroke, localActionStandin);
/* 2094 */       if (localActionMap != null)
/* 2095 */         localActionMap.put(localActionStandin, localActionStandin);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void registerWithKeyboardManager(boolean paramBoolean)
/*      */   {
/* 2111 */     InputMap localInputMap = getInputMap(2, false);
/*      */ 
/* 2113 */     Hashtable localHashtable = (Hashtable)getClientProperty("_WhenInFocusedWindow");
/*      */     KeyStroke[] arrayOfKeyStroke;
/* 2116 */     if (localInputMap != null)
/*      */     {
/* 2118 */       arrayOfKeyStroke = localInputMap.allKeys();
/* 2119 */       if (arrayOfKeyStroke != null) {
/* 2120 */         for (int i = arrayOfKeyStroke.length - 1; i >= 0; 
/* 2121 */           i--) {
/* 2122 */           if ((!paramBoolean) || (localHashtable == null) || (localHashtable.get(arrayOfKeyStroke[i]) == null))
/*      */           {
/* 2124 */             registerWithKeyboardManager(arrayOfKeyStroke[i]);
/*      */           }
/* 2126 */           if (localHashtable != null)
/* 2127 */             localHashtable.remove(arrayOfKeyStroke[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2133 */       arrayOfKeyStroke = null;
/*      */     }
/*      */ 
/* 2136 */     if ((localHashtable != null) && (localHashtable.size() > 0)) {
/* 2137 */       Enumeration localEnumeration = localHashtable.keys();
/*      */ 
/* 2139 */       while (localEnumeration.hasMoreElements()) {
/* 2140 */         KeyStroke localKeyStroke = (KeyStroke)localEnumeration.nextElement();
/* 2141 */         unregisterWithKeyboardManager(localKeyStroke);
/*      */       }
/* 2143 */       localHashtable.clear();
/*      */     }
/*      */ 
/* 2146 */     if ((arrayOfKeyStroke != null) && (arrayOfKeyStroke.length > 0)) {
/* 2147 */       if (localHashtable == null) {
/* 2148 */         localHashtable = new Hashtable(arrayOfKeyStroke.length);
/* 2149 */         putClientProperty("_WhenInFocusedWindow", localHashtable);
/*      */       }
/* 2151 */       for (int j = arrayOfKeyStroke.length - 1; j >= 0; j--)
/* 2152 */         localHashtable.put(arrayOfKeyStroke[j], arrayOfKeyStroke[j]);
/*      */     }
/*      */     else
/*      */     {
/* 2156 */       putClientProperty("_WhenInFocusedWindow", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void unregisterWithKeyboardManager()
/*      */   {
/* 2165 */     Hashtable localHashtable = (Hashtable)getClientProperty("_WhenInFocusedWindow");
/*      */ 
/* 2168 */     if ((localHashtable != null) && (localHashtable.size() > 0)) {
/* 2169 */       Enumeration localEnumeration = localHashtable.keys();
/*      */ 
/* 2171 */       while (localEnumeration.hasMoreElements()) {
/* 2172 */         KeyStroke localKeyStroke = (KeyStroke)localEnumeration.nextElement();
/* 2173 */         unregisterWithKeyboardManager(localKeyStroke);
/*      */       }
/*      */     }
/* 2176 */     putClientProperty("_WhenInFocusedWindow", null);
/*      */   }
/*      */ 
/*      */   void componentInputMapChanged(ComponentInputMap paramComponentInputMap)
/*      */   {
/* 2188 */     InputMap localInputMap = getInputMap(2, false);
/*      */ 
/* 2190 */     while ((localInputMap != paramComponentInputMap) && (localInputMap != null)) {
/* 2191 */       localInputMap = localInputMap.getParent();
/*      */     }
/* 2193 */     if (localInputMap != null)
/* 2194 */       registerWithKeyboardManager(false);
/*      */   }
/*      */ 
/*      */   private void registerWithKeyboardManager(KeyStroke paramKeyStroke)
/*      */   {
/* 2199 */     KeyboardManager.getCurrentManager().registerKeyStroke(paramKeyStroke, this);
/*      */   }
/*      */ 
/*      */   private void unregisterWithKeyboardManager(KeyStroke paramKeyStroke) {
/* 2203 */     KeyboardManager.getCurrentManager().unregisterKeyStroke(paramKeyStroke, this);
/*      */   }
/*      */ 
/*      */   public void registerKeyboardAction(ActionListener paramActionListener, KeyStroke paramKeyStroke, int paramInt)
/*      */   {
/* 2213 */     registerKeyboardAction(paramActionListener, null, paramKeyStroke, paramInt);
/*      */   }
/*      */ 
/*      */   public void unregisterKeyboardAction(KeyStroke paramKeyStroke)
/*      */   {
/* 2231 */     ActionMap localActionMap = getActionMap(false);
/* 2232 */     for (int i = 0; i < 3; i++) {
/* 2233 */       InputMap localInputMap = getInputMap(i, false);
/* 2234 */       if (localInputMap != null) {
/* 2235 */         Object localObject = localInputMap.get(paramKeyStroke);
/*      */ 
/* 2237 */         if ((localActionMap != null) && (localObject != null)) {
/* 2238 */           localActionMap.remove(localObject);
/*      */         }
/* 2240 */         localInputMap.remove(paramKeyStroke);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public KeyStroke[] getRegisteredKeyStrokes()
/*      */   {
/* 2253 */     int[] arrayOfInt = new int[3];
/* 2254 */     KeyStroke[][] arrayOfKeyStroke; = new KeyStroke[3][];
/*      */ 
/* 2256 */     for (int i = 0; i < 3; i++) {
/* 2257 */       InputMap localInputMap = getInputMap(i, false);
/* 2258 */       arrayOfKeyStroke;[i] = (localInputMap != null ? localInputMap.allKeys() : null);
/* 2259 */       arrayOfInt[i] = (arrayOfKeyStroke;[i] != null ? arrayOfKeyStroke;[i].length : 0);
/*      */     }
/*      */ 
/* 2262 */     KeyStroke[] arrayOfKeyStroke = new KeyStroke[arrayOfInt[0] + arrayOfInt[1] + arrayOfInt[2]];
/*      */ 
/* 2264 */     int j = 0; for (int k = 0; j < 3; j++) {
/* 2265 */       if (arrayOfInt[j] > 0) {
/* 2266 */         System.arraycopy(arrayOfKeyStroke;[j], 0, arrayOfKeyStroke, k, arrayOfInt[j]);
/*      */ 
/* 2268 */         k += arrayOfInt[j];
/*      */       }
/*      */     }
/* 2271 */     return arrayOfKeyStroke;
/*      */   }
/*      */ 
/*      */   public int getConditionForKeyStroke(KeyStroke paramKeyStroke)
/*      */   {
/* 2287 */     for (int i = 0; i < 3; i++) {
/* 2288 */       InputMap localInputMap = getInputMap(i, false);
/* 2289 */       if ((localInputMap != null) && (localInputMap.get(paramKeyStroke) != null)) {
/* 2290 */         return i;
/*      */       }
/*      */     }
/* 2293 */     return -1;
/*      */   }
/*      */ 
/*      */   public ActionListener getActionForKeyStroke(KeyStroke paramKeyStroke)
/*      */   {
/* 2304 */     ActionMap localActionMap = getActionMap(false);
/*      */ 
/* 2306 */     if (localActionMap == null) {
/* 2307 */       return null;
/*      */     }
/* 2309 */     for (int i = 0; i < 3; i++) {
/* 2310 */       InputMap localInputMap = getInputMap(i, false);
/* 2311 */       if (localInputMap != null) {
/* 2312 */         Object localObject = localInputMap.get(paramKeyStroke);
/*      */ 
/* 2314 */         if (localObject != null) {
/* 2315 */           Action localAction = localActionMap.get(localObject);
/* 2316 */           if ((localAction instanceof ActionStandin)) {
/* 2317 */             return ((ActionStandin)localAction).actionListener;
/*      */           }
/* 2319 */           return localAction;
/*      */         }
/*      */       }
/*      */     }
/* 2323 */     return null;
/*      */   }
/*      */ 
/*      */   public void resetKeyboardActions()
/*      */   {
/* 2335 */     for (int i = 0; i < 3; i++) {
/* 2336 */       InputMap localInputMap = getInputMap(i, false);
/*      */ 
/* 2338 */       if (localInputMap != null) {
/* 2339 */         localInputMap.clear();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2344 */     ActionMap localActionMap = getActionMap(false);
/*      */ 
/* 2346 */     if (localActionMap != null)
/* 2347 */       localActionMap.clear();
/*      */   }
/*      */ 
/*      */   public final void setInputMap(int paramInt, InputMap paramInputMap)
/*      */   {
/* 2379 */     switch (paramInt) {
/*      */     case 2:
/* 2381 */       if ((paramInputMap != null) && (!(paramInputMap instanceof ComponentInputMap))) {
/* 2382 */         throw new IllegalArgumentException("WHEN_IN_FOCUSED_WINDOW InputMaps must be of type ComponentInputMap");
/*      */       }
/* 2384 */       this.windowInputMap = ((ComponentInputMap)paramInputMap);
/* 2385 */       setFlag(7, true);
/* 2386 */       registerWithKeyboardManager(false);
/* 2387 */       break;
/*      */     case 1:
/* 2389 */       this.ancestorInputMap = paramInputMap;
/* 2390 */       setFlag(6, true);
/* 2391 */       break;
/*      */     case 0:
/* 2393 */       this.focusInputMap = paramInputMap;
/* 2394 */       setFlag(5, true);
/* 2395 */       break;
/*      */     default:
/* 2397 */       throw new IllegalArgumentException("condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
/*      */     }
/*      */   }
/*      */ 
/*      */   public final InputMap getInputMap(int paramInt)
/*      */   {
/* 2412 */     return getInputMap(paramInt, true);
/*      */   }
/*      */ 
/*      */   public final InputMap getInputMap()
/*      */   {
/* 2424 */     return getInputMap(0, true);
/*      */   }
/*      */ 
/*      */   public final void setActionMap(ActionMap paramActionMap)
/*      */   {
/* 2436 */     this.actionMap = paramActionMap;
/* 2437 */     setFlag(8, true);
/*      */   }
/*      */ 
/*      */   public final ActionMap getActionMap()
/*      */   {
/* 2450 */     return getActionMap(true);
/*      */   }
/*      */ 
/*      */   final InputMap getInputMap(int paramInt, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/* 2474 */     switch (paramInt) {
/*      */     case 0:
/* 2476 */       if (getFlag(5)) {
/* 2477 */         return this.focusInputMap;
/*      */       }
/*      */ 
/* 2480 */       if (paramBoolean) {
/* 2481 */         localObject = new InputMap();
/* 2482 */         setInputMap(paramInt, (InputMap)localObject);
/* 2483 */         return localObject;
/*      */       }
/*      */       break;
/*      */     case 1:
/* 2487 */       if (getFlag(6)) {
/* 2488 */         return this.ancestorInputMap;
/*      */       }
/*      */ 
/* 2491 */       if (paramBoolean) {
/* 2492 */         localObject = new InputMap();
/* 2493 */         setInputMap(paramInt, (InputMap)localObject);
/* 2494 */         return localObject;
/*      */       }
/*      */       break;
/*      */     case 2:
/* 2498 */       if (getFlag(7)) {
/* 2499 */         return this.windowInputMap;
/*      */       }
/*      */ 
/* 2502 */       if (paramBoolean) {
/* 2503 */         localObject = new ComponentInputMap(this);
/* 2504 */         setInputMap(paramInt, (InputMap)localObject);
/* 2505 */         return localObject;
/*      */       }
/*      */       break;
/*      */     default:
/* 2509 */       throw new IllegalArgumentException("condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
/*      */     }
/* 2511 */     return null;
/*      */   }
/*      */ 
/*      */   final ActionMap getActionMap(boolean paramBoolean)
/*      */   {
/* 2524 */     if (getFlag(8)) {
/* 2525 */       return this.actionMap;
/*      */     }
/*      */ 
/* 2528 */     if (paramBoolean) {
/* 2529 */       ActionMap localActionMap = new ActionMap();
/* 2530 */       setActionMap(localActionMap);
/* 2531 */       return localActionMap;
/*      */     }
/* 2533 */     return null;
/*      */   }
/*      */ 
/*      */   public int getBaseline(int paramInt1, int paramInt2)
/*      */   {
/* 2559 */     super.getBaseline(paramInt1, paramInt2);
/* 2560 */     if (this.ui != null) {
/* 2561 */       return this.ui.getBaseline(this, paramInt1, paramInt2);
/*      */     }
/* 2563 */     return -1;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior()
/*      */   {
/* 2588 */     if (this.ui != null) {
/* 2589 */       return this.ui.getBaselineResizeBehavior(this);
/*      */     }
/* 2591 */     return Component.BaselineResizeBehavior.OTHER;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean requestDefaultFocus()
/*      */   {
/* 2614 */     Container localContainer = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
/*      */ 
/* 2616 */     if (localContainer == null) {
/* 2617 */       return false;
/*      */     }
/* 2619 */     Component localComponent = localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer);
/*      */ 
/* 2621 */     if (localComponent != null) {
/* 2622 */       localComponent.requestFocus();
/* 2623 */       return true;
/*      */     }
/* 2625 */     return false;
/*      */   }
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/* 2640 */     if (paramBoolean != isVisible()) {
/* 2641 */       super.setVisible(paramBoolean);
/* 2642 */       if (paramBoolean) {
/* 2643 */         Container localContainer = getParent();
/* 2644 */         if (localContainer != null) {
/* 2645 */           Rectangle localRectangle = getBounds();
/* 2646 */           localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */         }
/* 2648 */         revalidate();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnabled(boolean paramBoolean)
/*      */   {
/* 2676 */     boolean bool = isEnabled();
/* 2677 */     super.setEnabled(paramBoolean);
/* 2678 */     firePropertyChange("enabled", bool, paramBoolean);
/* 2679 */     if (paramBoolean != bool)
/* 2680 */       repaint();
/*      */   }
/*      */ 
/*      */   public void setForeground(Color paramColor)
/*      */   {
/* 2699 */     Color localColor = getForeground();
/* 2700 */     super.setForeground(paramColor);
/* 2701 */     if (localColor != null ? !localColor.equals(paramColor) : (paramColor != null) && (!paramColor.equals(localColor)))
/*      */     {
/* 2703 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/* 2729 */     Color localColor = getBackground();
/* 2730 */     super.setBackground(paramColor);
/* 2731 */     if (localColor != null ? !localColor.equals(paramColor) : (paramColor != null) && (!paramColor.equals(localColor)))
/*      */     {
/* 2733 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/* 2750 */     Font localFont = getFont();
/* 2751 */     super.setFont(paramFont);
/*      */ 
/* 2753 */     if (paramFont != localFont) {
/* 2754 */       revalidate();
/* 2755 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static Locale getDefaultLocale()
/*      */   {
/* 2775 */     Locale localLocale = (Locale)SwingUtilities.appContextGet("JComponent.defaultLocale");
/* 2776 */     if (localLocale == null)
/*      */     {
/* 2779 */       localLocale = Locale.getDefault();
/* 2780 */       setDefaultLocale(localLocale);
/*      */     }
/* 2782 */     return localLocale;
/*      */   }
/*      */ 
/*      */   public static void setDefaultLocale(Locale paramLocale)
/*      */   {
/* 2802 */     SwingUtilities.appContextPut("JComponent.defaultLocale", paramLocale);
/*      */   }
/*      */ 
/*      */   protected void processComponentKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void processKeyEvent(KeyEvent paramKeyEvent)
/*      */   {
/* 2829 */     super.processKeyEvent(paramKeyEvent);
/*      */ 
/* 2832 */     if (!paramKeyEvent.isConsumed()) {
/* 2833 */       processComponentKeyEvent(paramKeyEvent);
/*      */     }
/*      */ 
/* 2836 */     boolean bool = KeyboardState.shouldProcess(paramKeyEvent);
/*      */ 
/* 2838 */     if (paramKeyEvent.isConsumed()) {
/* 2839 */       return;
/*      */     }
/*      */ 
/* 2842 */     if (bool) if (processKeyBindings(paramKeyEvent, paramKeyEvent.getID() == 401))
/*      */       {
/* 2844 */         paramKeyEvent.consume();
/*      */       }
/*      */   }
/*      */ 
/*      */   protected boolean processKeyBinding(KeyStroke paramKeyStroke, KeyEvent paramKeyEvent, int paramInt, boolean paramBoolean)
/*      */   {
/* 2872 */     InputMap localInputMap = getInputMap(paramInt, false);
/* 2873 */     ActionMap localActionMap = getActionMap(false);
/*      */ 
/* 2875 */     if ((localInputMap != null) && (localActionMap != null) && (isEnabled())) {
/* 2876 */       Object localObject = localInputMap.get(paramKeyStroke);
/* 2877 */       Action localAction = localObject == null ? null : localActionMap.get(localObject);
/* 2878 */       if (localAction != null) {
/* 2879 */         return SwingUtilities.notifyAction(localAction, paramKeyStroke, paramKeyEvent, this, paramKeyEvent.getModifiers());
/*      */       }
/*      */     }
/*      */ 
/* 2883 */     return false;
/*      */   }
/*      */ 
/*      */   boolean processKeyBindings(KeyEvent paramKeyEvent, boolean paramBoolean)
/*      */   {
/* 2899 */     if (!SwingUtilities.isValidKeyEventForKeyBindings(paramKeyEvent)) {
/* 2900 */       return false;
/*      */     }
/*      */ 
/* 2906 */     KeyStroke localKeyStroke2 = null;
/*      */     KeyStroke localKeyStroke1;
/* 2908 */     if (paramKeyEvent.getID() == 400) {
/* 2909 */       localKeyStroke1 = KeyStroke.getKeyStroke(paramKeyEvent.getKeyChar());
/*      */     }
/*      */     else {
/* 2912 */       localKeyStroke1 = KeyStroke.getKeyStroke(paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers(), !paramBoolean);
/*      */ 
/* 2914 */       if (paramKeyEvent.getKeyCode() != paramKeyEvent.getExtendedKeyCode()) {
/* 2915 */         localKeyStroke2 = KeyStroke.getKeyStroke(paramKeyEvent.getExtendedKeyCode(), paramKeyEvent.getModifiers(), !paramBoolean);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2923 */     if ((localKeyStroke2 != null) && (processKeyBinding(localKeyStroke2, paramKeyEvent, 0, paramBoolean))) {
/* 2924 */       return true;
/*      */     }
/* 2926 */     if (processKeyBinding(localKeyStroke1, paramKeyEvent, 0, paramBoolean)) {
/* 2927 */       return true;
/*      */     }
/*      */ 
/* 2933 */     Object localObject = this;
/* 2934 */     while ((localObject != null) && (!(localObject instanceof Window)) && (!(localObject instanceof Applet)))
/*      */     {
/* 2936 */       if ((localObject instanceof JComponent)) {
/* 2937 */         if ((localKeyStroke2 != null) && (((JComponent)localObject).processKeyBinding(localKeyStroke2, paramKeyEvent, 1, paramBoolean)))
/*      */         {
/* 2939 */           return true;
/* 2940 */         }if (((JComponent)localObject).processKeyBinding(localKeyStroke1, paramKeyEvent, 1, paramBoolean))
/*      */         {
/* 2942 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2951 */       if (((localObject instanceof JInternalFrame)) && (processKeyBindingsForAllComponents(paramKeyEvent, (Container)localObject, paramBoolean)))
/*      */       {
/* 2953 */         return true;
/*      */       }
/* 2955 */       localObject = ((Container)localObject).getParent();
/*      */     }
/*      */ 
/* 2962 */     if (localObject != null) {
/* 2963 */       return processKeyBindingsForAllComponents(paramKeyEvent, (Container)localObject, paramBoolean);
/*      */     }
/* 2965 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean processKeyBindingsForAllComponents(KeyEvent paramKeyEvent, Container paramContainer, boolean paramBoolean)
/*      */   {
/*      */     while (true) {
/* 2971 */       if (KeyboardManager.getCurrentManager().fireKeyboardAction(paramKeyEvent, paramBoolean, paramContainer))
/*      */       {
/* 2973 */         return true;
/*      */       }
/* 2975 */       if (!(paramContainer instanceof Popup.HeavyWeightWindow)) break;
/* 2976 */       paramContainer = ((Window)paramContainer).getOwner();
/*      */     }
/*      */ 
/* 2979 */     return false;
/*      */   }
/*      */ 
/*      */   public void setToolTipText(String paramString)
/*      */   {
/* 3000 */     String str = getToolTipText();
/* 3001 */     putClientProperty("ToolTipText", paramString);
/* 3002 */     ToolTipManager localToolTipManager = ToolTipManager.sharedInstance();
/* 3003 */     if (paramString != null) {
/* 3004 */       if (str == null)
/* 3005 */         localToolTipManager.registerComponent(this);
/*      */     }
/*      */     else
/* 3008 */       localToolTipManager.unregisterComponent(this);
/*      */   }
/*      */ 
/*      */   public String getToolTipText()
/*      */   {
/* 3020 */     return (String)getClientProperty("ToolTipText");
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 3032 */     return getToolTipText();
/*      */   }
/*      */ 
/*      */   public Point getToolTipLocation(MouseEvent paramMouseEvent)
/*      */   {
/* 3045 */     return null;
/*      */   }
/*      */ 
/*      */   public Point getPopupLocation(MouseEvent paramMouseEvent)
/*      */   {
/* 3061 */     return null;
/*      */   }
/*      */ 
/*      */   public JToolTip createToolTip()
/*      */   {
/* 3075 */     JToolTip localJToolTip = new JToolTip();
/* 3076 */     localJToolTip.setComponent(this);
/* 3077 */     return localJToolTip;
/*      */   }
/*      */ 
/*      */   public void scrollRectToVisible(Rectangle paramRectangle)
/*      */   {
/* 3091 */     int i = getX(); int j = getY();
/*      */ 
/* 3093 */     for (Container localContainer = getParent(); 
/* 3095 */       (localContainer != null) && (!(localContainer instanceof JComponent)) && (!(localContainer instanceof CellRendererPane)); 
/* 3097 */       localContainer = localContainer.getParent()) {
/* 3098 */       Rectangle localRectangle = localContainer.getBounds();
/*      */ 
/* 3100 */       i += localRectangle.x;
/* 3101 */       j += localRectangle.y;
/*      */     }
/*      */ 
/* 3104 */     if ((localContainer != null) && (!(localContainer instanceof CellRendererPane))) {
/* 3105 */       paramRectangle.x += i;
/* 3106 */       paramRectangle.y += j;
/*      */ 
/* 3108 */       ((JComponent)localContainer).scrollRectToVisible(paramRectangle);
/* 3109 */       paramRectangle.x -= i;
/* 3110 */       paramRectangle.y -= j;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setAutoscrolls(boolean paramBoolean)
/*      */   {
/* 3159 */     setFlag(25, true);
/* 3160 */     if (this.autoscrolls != paramBoolean) {
/* 3161 */       this.autoscrolls = paramBoolean;
/* 3162 */       if (paramBoolean) {
/* 3163 */         enableEvents(16L);
/* 3164 */         enableEvents(32L);
/*      */       }
/*      */       else {
/* 3167 */         Autoscroller.stop(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAutoscrolls()
/*      */   {
/* 3180 */     return this.autoscrolls;
/*      */   }
/*      */ 
/*      */   public void setTransferHandler(TransferHandler paramTransferHandler)
/*      */   {
/* 3223 */     TransferHandler localTransferHandler = (TransferHandler)getClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER);
/*      */ 
/* 3225 */     putClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER, paramTransferHandler);
/*      */ 
/* 3227 */     SwingUtilities.installSwingDropTargetAsNecessary(this, paramTransferHandler);
/* 3228 */     firePropertyChange("transferHandler", localTransferHandler, paramTransferHandler);
/*      */   }
/*      */ 
/*      */   public TransferHandler getTransferHandler()
/*      */   {
/* 3241 */     return (TransferHandler)getClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER);
/*      */   }
/*      */ 
/*      */   TransferHandler.DropLocation dropLocationForPoint(Point paramPoint)
/*      */   {
/* 3256 */     return null;
/*      */   }
/*      */ 
/*      */   Object setDropLocation(TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean)
/*      */   {
/* 3296 */     return null;
/*      */   }
/*      */ 
/*      */   void dndDone()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void processMouseEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 3318 */     if ((this.autoscrolls) && (paramMouseEvent.getID() == 502)) {
/* 3319 */       Autoscroller.stop(this);
/*      */     }
/* 3321 */     super.processMouseEvent(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   protected void processMouseMotionEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 3331 */     int i = 1;
/* 3332 */     if ((this.autoscrolls) && (paramMouseEvent.getID() == 506))
/*      */     {
/* 3335 */       i = !Autoscroller.isRunning(this) ? 1 : 0;
/* 3336 */       Autoscroller.processMouseDragged(paramMouseEvent);
/*      */     }
/* 3338 */     if (i != 0)
/* 3339 */       super.processMouseMotionEvent(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   void superProcessMouseMotionEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 3345 */     super.processMouseMotionEvent(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   void setCreatedDoubleBuffer(boolean paramBoolean)
/*      */   {
/* 3355 */     setFlag(9, paramBoolean);
/*      */   }
/*      */ 
/*      */   boolean getCreatedDoubleBuffer()
/*      */   {
/* 3365 */     return getFlag(9);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void enable()
/*      */   {
/* 3618 */     if (isEnabled() != true) {
/* 3619 */       super.enable();
/* 3620 */       if (this.accessibleContext != null)
/* 3621 */         this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.ENABLED);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void disable()
/*      */   {
/* 3634 */     if (isEnabled()) {
/* 3635 */       super.disable();
/* 3636 */       if (this.accessibleContext != null)
/* 3637 */         this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.ENABLED, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 3661 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   private ArrayTable getClientProperties()
/*      */   {
/* 4020 */     if (this.clientProperties == null) {
/* 4021 */       this.clientProperties = new ArrayTable();
/*      */     }
/* 4023 */     return this.clientProperties;
/*      */   }
/*      */ 
/*      */   public final Object getClientProperty(Object paramObject)
/*      */   {
/* 4037 */     if (paramObject == SwingUtilities2.AA_TEXT_PROPERTY_KEY)
/* 4038 */       return this.aaTextInfo;
/* 4039 */     if (paramObject == SwingUtilities2.COMPONENT_UI_PROPERTY_KEY) {
/* 4040 */       return this.ui;
/*      */     }
/* 4042 */     if (this.clientProperties == null) {
/* 4043 */       return null;
/*      */     }
/* 4045 */     synchronized (this.clientProperties) {
/* 4046 */       return this.clientProperties.get(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void putClientProperty(Object paramObject1, Object paramObject2)
/*      */   {
/* 4080 */     if (paramObject1 == SwingUtilities2.AA_TEXT_PROPERTY_KEY) {
/* 4081 */       this.aaTextInfo = paramObject2;
/* 4082 */       return;
/*      */     }
/* 4084 */     if ((paramObject2 == null) && (this.clientProperties == null))
/*      */     {
/* 4087 */       return;
/*      */     }
/* 4089 */     ArrayTable localArrayTable = getClientProperties();
/*      */     Object localObject1;
/* 4091 */     synchronized (localArrayTable) {
/* 4092 */       localObject1 = localArrayTable.get(paramObject1);
/* 4093 */       if (paramObject2 != null)
/* 4094 */         localArrayTable.put(paramObject1, paramObject2);
/* 4095 */       else if (localObject1 != null) {
/* 4096 */         localArrayTable.remove(paramObject1);
/*      */       }
/*      */       else {
/* 4099 */         return;
/*      */       }
/*      */     }
/* 4102 */     clientPropertyChanged(paramObject1, localObject1, paramObject2);
/* 4103 */     firePropertyChange(paramObject1.toString(), localObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   void clientPropertyChanged(Object paramObject1, Object paramObject2, Object paramObject3)
/*      */   {
/*      */   }
/*      */ 
/*      */   void setUIProperty(String paramString, Object paramObject)
/*      */   {
/* 4124 */     if (paramString == "opaque") {
/* 4125 */       if (!getFlag(24)) {
/* 4126 */         setOpaque(((Boolean)paramObject).booleanValue());
/* 4127 */         setFlag(24, false);
/*      */       }
/* 4129 */     } else if (paramString == "autoscrolls") {
/* 4130 */       if (!getFlag(25)) {
/* 4131 */         setAutoscrolls(((Boolean)paramObject).booleanValue());
/* 4132 */         setFlag(25, false);
/*      */       }
/* 4134 */     } else if (paramString == "focusTraversalKeysForward") {
/* 4135 */       if (!getFlag(26)) {
/* 4136 */         super.setFocusTraversalKeys(0, (Set)paramObject);
/*      */       }
/*      */ 
/*      */     }
/* 4140 */     else if (paramString == "focusTraversalKeysBackward") {
/* 4141 */       if (!getFlag(27)) {
/* 4142 */         super.setFocusTraversalKeys(1, (Set)paramObject);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4147 */       throw new IllegalArgumentException("property \"" + paramString + "\" cannot be set using this method");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFocusTraversalKeys(int paramInt, Set<? extends AWTKeyStroke> paramSet)
/*      */   {
/* 4182 */     if (paramInt == 0)
/* 4183 */       setFlag(26, true);
/* 4184 */     else if (paramInt == 1) {
/* 4185 */       setFlag(27, true);
/*      */     }
/* 4187 */     super.setFocusTraversalKeys(paramInt, paramSet);
/*      */   }
/*      */ 
/*      */   public static boolean isLightweightComponent(Component paramComponent)
/*      */   {
/* 4202 */     return paramComponent.getPeer() instanceof LightweightPeer;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 4220 */     super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds(Rectangle paramRectangle)
/*      */   {
/* 4238 */     if (paramRectangle == null) {
/* 4239 */       return new Rectangle(getX(), getY(), getWidth(), getHeight());
/*      */     }
/*      */ 
/* 4242 */     paramRectangle.setBounds(getX(), getY(), getWidth(), getHeight());
/* 4243 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   public Dimension getSize(Dimension paramDimension)
/*      */   {
/* 4260 */     if (paramDimension == null) {
/* 4261 */       return new Dimension(getWidth(), getHeight());
/*      */     }
/*      */ 
/* 4264 */     paramDimension.setSize(getWidth(), getHeight());
/* 4265 */     return paramDimension;
/*      */   }
/*      */ 
/*      */   public Point getLocation(Point paramPoint)
/*      */   {
/* 4282 */     if (paramPoint == null) {
/* 4283 */       return new Point(getX(), getY());
/*      */     }
/*      */ 
/* 4286 */     paramPoint.setLocation(getX(), getY());
/* 4287 */     return paramPoint;
/*      */   }
/*      */ 
/*      */   public int getX()
/*      */   {
/* 4301 */     return super.getX();
/*      */   }
/*      */ 
/*      */   public int getY()
/*      */   {
/* 4313 */     return super.getY();
/*      */   }
/*      */ 
/*      */   public int getWidth()
/*      */   {
/* 4325 */     return super.getWidth();
/*      */   }
/*      */ 
/*      */   public int getHeight()
/*      */   {
/* 4337 */     return super.getHeight();
/*      */   }
/*      */ 
/*      */   public boolean isOpaque()
/*      */   {
/* 4355 */     return getFlag(3);
/*      */   }
/*      */ 
/*      */   public void setOpaque(boolean paramBoolean)
/*      */   {
/* 4376 */     boolean bool = getFlag(3);
/* 4377 */     setFlag(3, paramBoolean);
/* 4378 */     setFlag(24, true);
/* 4379 */     firePropertyChange("opaque", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   boolean rectangleIsObscured(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 4400 */     int i = getComponentCount();
/*      */ 
/* 4402 */     for (int j = 0; j < i; j++) {
/* 4403 */       Component localComponent = getComponent(j);
/*      */ 
/* 4406 */       int k = localComponent.getX();
/* 4407 */       int m = localComponent.getY();
/* 4408 */       int n = localComponent.getWidth();
/* 4409 */       int i1 = localComponent.getHeight();
/*      */ 
/* 4411 */       if ((paramInt1 >= k) && (paramInt1 + paramInt3 <= k + n) && (paramInt2 >= m) && (paramInt2 + paramInt4 <= m + i1) && (localComponent.isVisible()))
/*      */       {
/* 4414 */         if ((localComponent instanceof JComponent))
/*      */         {
/* 4418 */           return localComponent.isOpaque();
/*      */         }
/*      */ 
/* 4423 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4428 */     return false;
/*      */   }
/*      */ 
/*      */   static final void computeVisibleRect(Component paramComponent, Rectangle paramRectangle)
/*      */   {
/* 4446 */     Container localContainer = paramComponent.getParent();
/* 4447 */     Rectangle localRectangle = paramComponent.getBounds();
/*      */ 
/* 4449 */     if ((localContainer == null) || ((localContainer instanceof Window)) || ((localContainer instanceof Applet))) {
/* 4450 */       paramRectangle.setBounds(0, 0, localRectangle.width, localRectangle.height);
/*      */     } else {
/* 4452 */       computeVisibleRect(localContainer, paramRectangle);
/* 4453 */       paramRectangle.x -= localRectangle.x;
/* 4454 */       paramRectangle.y -= localRectangle.y;
/* 4455 */       SwingUtilities.computeIntersection(0, 0, localRectangle.width, localRectangle.height, paramRectangle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void computeVisibleRect(Rectangle paramRectangle)
/*      */   {
/* 4473 */     computeVisibleRect(this, paramRectangle);
/*      */   }
/*      */ 
/*      */   public Rectangle getVisibleRect()
/*      */   {
/* 4486 */     Rectangle localRectangle = new Rectangle();
/*      */ 
/* 4488 */     computeVisibleRect(localRectangle);
/* 4489 */     return localRectangle;
/*      */   }
/*      */ 
/*      */   public void firePropertyChange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 4504 */     super.firePropertyChange(paramString, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public void firePropertyChange(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 4520 */     super.firePropertyChange(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void firePropertyChange(String paramString, char paramChar1, char paramChar2)
/*      */   {
/* 4526 */     super.firePropertyChange(paramString, paramChar1, paramChar2);
/*      */   }
/*      */ 
/*      */   protected void fireVetoableChange(String paramString, Object paramObject1, Object paramObject2)
/*      */     throws PropertyVetoException
/*      */   {
/* 4544 */     if (this.vetoableChangeSupport == null) {
/* 4545 */       return;
/*      */     }
/* 4547 */     this.vetoableChangeSupport.fireVetoableChange(paramString, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public synchronized void addVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 4558 */     if (this.vetoableChangeSupport == null) {
/* 4559 */       this.vetoableChangeSupport = new VetoableChangeSupport(this);
/*      */     }
/* 4561 */     this.vetoableChangeSupport.addVetoableChangeListener(paramVetoableChangeListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removeVetoableChangeListener(VetoableChangeListener paramVetoableChangeListener)
/*      */   {
/* 4573 */     if (this.vetoableChangeSupport == null) {
/* 4574 */       return;
/*      */     }
/* 4576 */     this.vetoableChangeSupport.removeVetoableChangeListener(paramVetoableChangeListener);
/*      */   }
/*      */ 
/*      */   public synchronized VetoableChangeListener[] getVetoableChangeListeners()
/*      */   {
/* 4594 */     if (this.vetoableChangeSupport == null) {
/* 4595 */       return new VetoableChangeListener[0];
/*      */     }
/* 4597 */     return this.vetoableChangeSupport.getVetoableChangeListeners();
/*      */   }
/*      */ 
/*      */   public Container getTopLevelAncestor()
/*      */   {
/* 4611 */     for (Object localObject = this; localObject != null; localObject = ((Container)localObject).getParent()) {
/* 4612 */       if (((localObject instanceof Window)) || ((localObject instanceof Applet))) {
/* 4613 */         return localObject;
/*      */       }
/*      */     }
/* 4616 */     return null;
/*      */   }
/*      */ 
/*      */   private AncestorNotifier getAncestorNotifier() {
/* 4620 */     return (AncestorNotifier)getClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER);
/*      */   }
/*      */ 
/*      */   public void addAncestorListener(AncestorListener paramAncestorListener)
/*      */   {
/* 4635 */     AncestorNotifier localAncestorNotifier = getAncestorNotifier();
/* 4636 */     if (localAncestorNotifier == null) {
/* 4637 */       localAncestorNotifier = new AncestorNotifier(this);
/* 4638 */       putClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER, localAncestorNotifier);
/*      */     }
/*      */ 
/* 4641 */     localAncestorNotifier.addAncestorListener(paramAncestorListener);
/*      */   }
/*      */ 
/*      */   public void removeAncestorListener(AncestorListener paramAncestorListener)
/*      */   {
/* 4652 */     AncestorNotifier localAncestorNotifier = getAncestorNotifier();
/* 4653 */     if (localAncestorNotifier == null) {
/* 4654 */       return;
/*      */     }
/* 4656 */     localAncestorNotifier.removeAncestorListener(paramAncestorListener);
/* 4657 */     if (localAncestorNotifier.listenerList.getListenerList().length == 0) {
/* 4658 */       localAncestorNotifier.removeAllListeners();
/* 4659 */       putClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AncestorListener[] getAncestorListeners()
/*      */   {
/* 4677 */     AncestorNotifier localAncestorNotifier = getAncestorNotifier();
/* 4678 */     if (localAncestorNotifier == null) {
/* 4679 */       return new AncestorListener[0];
/*      */     }
/* 4681 */     return localAncestorNotifier.getAncestorListeners();
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/*      */     EventListener[] arrayOfEventListener;
/* 4721 */     if (paramClass == AncestorListener.class)
/*      */     {
/* 4723 */       arrayOfEventListener = (EventListener[])getAncestorListeners();
/*      */     }
/* 4725 */     else if (paramClass == VetoableChangeListener.class)
/*      */     {
/* 4727 */       arrayOfEventListener = (EventListener[])getVetoableChangeListeners();
/*      */     }
/* 4729 */     else if (paramClass == PropertyChangeListener.class)
/*      */     {
/* 4731 */       arrayOfEventListener = (EventListener[])getPropertyChangeListeners();
/*      */     }
/*      */     else {
/* 4734 */       arrayOfEventListener = this.listenerList.getListeners(paramClass);
/*      */     }
/*      */ 
/* 4737 */     if (arrayOfEventListener.length == 0) {
/* 4738 */       return super.getListeners(paramClass);
/*      */     }
/* 4740 */     return arrayOfEventListener;
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/* 4753 */     super.addNotify();
/* 4754 */     firePropertyChange("ancestor", null, getParent());
/*      */ 
/* 4756 */     registerWithKeyboardManager(false);
/* 4757 */     registerNextFocusableComponent();
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 4771 */     super.removeNotify();
/*      */ 
/* 4775 */     firePropertyChange("ancestor", getParent(), null);
/*      */ 
/* 4777 */     unregisterWithKeyboardManager();
/* 4778 */     deregisterNextFocusableComponent();
/*      */ 
/* 4780 */     if (getCreatedDoubleBuffer()) {
/* 4781 */       RepaintManager.currentManager(this).resetDoubleBuffer();
/* 4782 */       setCreatedDoubleBuffer(false);
/*      */     }
/* 4784 */     if (this.autoscrolls)
/* 4785 */       Autoscroller.stop(this);
/*      */   }
/*      */ 
/*      */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 4805 */     RepaintManager.currentManager(SunToolkit.targetToAppContext(this)).addDirtyRegion(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void repaint(Rectangle paramRectangle)
/*      */   {
/* 4821 */     repaint(0L, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public void revalidate()
/*      */   {
/* 4852 */     if (getParent() == null)
/*      */     {
/* 4859 */       return;
/*      */     }
/* 4861 */     if (SunToolkit.isDispatchThreadForAppContext(this)) {
/* 4862 */       invalidate();
/* 4863 */       RepaintManager.currentManager(this).addInvalidComponent(this);
/*      */     }
/*      */     else
/*      */     {
/* 4869 */       synchronized (this) {
/* 4870 */         if (getFlag(28)) {
/* 4871 */           return;
/*      */         }
/* 4873 */         setFlag(28, true);
/*      */       }
/* 4875 */       ??? = new Runnable() {
/*      */         public void run() {
/* 4877 */           synchronized (JComponent.this) {
/* 4878 */             JComponent.this.setFlag(28, false);
/*      */           }
/* 4880 */           JComponent.this.revalidate();
/*      */         }
/*      */       };
/* 4883 */       SunToolkit.executeOnEventHandlerThread(this, (Runnable)???);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/* 4902 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isOptimizedDrawingEnabled()
/*      */   {
/* 4917 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean isPaintingOrigin()
/*      */   {
/* 4937 */     return false;
/*      */   }
/*      */ 
/*      */   public void paintImmediately(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 4961 */     Object localObject = this;
/*      */ 
/* 4964 */     if (!isShowing()) {
/* 4965 */       return;
/*      */     }
/*      */ 
/* 4968 */     JComponent localJComponent = SwingUtilities.getPaintingOrigin(this);
/* 4969 */     if (localJComponent != null) {
/* 4970 */       Rectangle localRectangle = SwingUtilities.convertRectangle((Component)localObject, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4), localJComponent);
/*      */ 
/* 4972 */       localJComponent.paintImmediately(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/* 4973 */       return;
/*      */     }
/*      */ 
/* 4976 */     while (!((Component)localObject).isOpaque()) {
/* 4977 */       Container localContainer = ((Component)localObject).getParent();
/* 4978 */       if (localContainer != null) {
/* 4979 */         paramInt1 += ((Component)localObject).getX();
/* 4980 */         paramInt2 += ((Component)localObject).getY();
/* 4981 */         localObject = localContainer;
/*      */ 
/* 4986 */         if (!(localObject instanceof JComponent))
/* 4987 */           break;
/*      */       }
/*      */     }
/* 4990 */     if ((localObject instanceof JComponent))
/* 4991 */       ((JComponent)localObject)._paintImmediately(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     else
/* 4993 */       ((Component)localObject).repaint(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void paintImmediately(Rectangle paramRectangle)
/*      */   {
/* 5003 */     paintImmediately(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   boolean alwaysOnTop()
/*      */   {
/* 5016 */     return false;
/*      */   }
/*      */ 
/*      */   void setPaintingChild(Component paramComponent) {
/* 5020 */     this.paintingChild = paramComponent;
/*      */   }
/*      */ 
/*      */   void _paintImmediately(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 5029 */     int n = 0; int i1 = 0;
/*      */ 
/* 5031 */     int i2 = 0;
/*      */ 
/* 5033 */     Object localObject2 = null;
/* 5034 */     Object localObject3 = this;
/*      */ 
/* 5036 */     RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/*      */ 
/* 5041 */     ArrayList localArrayList = new ArrayList(7);
/* 5042 */     int i3 = -1;
/* 5043 */     int i4 = 0;
/*      */     int m;
/*      */     int k;
/*      */     int j;
/* 5045 */     int i = j = k = m = 0;
/*      */ 
/* 5047 */     Rectangle localRectangle = fetchRectangle();
/* 5048 */     localRectangle.x = paramInt1;
/* 5049 */     localRectangle.y = paramInt2;
/* 5050 */     localRectangle.width = paramInt3;
/* 5051 */     localRectangle.height = paramInt4;
/*      */ 
/* 5056 */     int i5 = (alwaysOnTop()) && (isOpaque()) ? 1 : 0;
/* 5057 */     if (i5 != 0) {
/* 5058 */       SwingUtilities.computeIntersection(0, 0, getWidth(), getHeight(), localRectangle);
/*      */ 
/* 5060 */       if (localRectangle.width == 0) {
/* 5061 */         recycleRectangle(localRectangle);
/* 5062 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 5066 */     Object localObject1 = this; Object localObject4 = null;
/*      */     Object localObject5;
/*      */     int i6;
/* 5068 */     for (; (localObject1 != null) && (!(localObject1 instanceof Window)) && (!(localObject1 instanceof Applet)); 
/* 5068 */       localObject1 = ((Container)localObject1).getParent()) {
/* 5069 */       localObject5 = (localObject1 instanceof JComponent) ? (JComponent)localObject1 : null;
/*      */ 
/* 5071 */       localArrayList.add(localObject1);
/* 5072 */       if ((i5 == 0) && (localObject5 != null) && (!((JComponent)localObject5).isOptimizedDrawingEnabled()))
/*      */       {
/* 5083 */         if (localObject1 != this) {
/* 5084 */           if (((JComponent)localObject5).isPaintingOrigin()) {
/* 5085 */             i6 = 1;
/*      */           }
/*      */           else {
/* 5088 */             Component[] arrayOfComponent = ((Container)localObject1).getComponents();
/* 5089 */             int i8 = 0;
/* 5090 */             while ((i8 < arrayOfComponent.length) && 
/* 5091 */               (arrayOfComponent[i8] != localObject4)) {
/* 5090 */               i8++;
/*      */             }
/*      */ 
/* 5093 */             switch (((JComponent)localObject5).getObscuredState(i8, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height))
/*      */             {
/*      */             case 0:
/* 5099 */               i6 = 0;
/* 5100 */               break;
/*      */             case 2:
/* 5102 */               recycleRectangle(localRectangle);
/* 5103 */               return;
/*      */             default:
/* 5105 */               i6 = 1;
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 5111 */           i6 = 0;
/*      */         }
/*      */ 
/* 5114 */         if (i6 != 0)
/*      */         {
/* 5117 */           localObject3 = localObject5;
/* 5118 */           i3 = i4;
/* 5119 */           n = i1 = 0;
/* 5120 */           i2 = 0;
/*      */         }
/*      */       }
/* 5123 */       i4++;
/*      */ 
/* 5127 */       if ((localRepaintManager.isDoubleBufferingEnabled()) && (localObject5 != null) && (((JComponent)localObject5).isDoubleBuffered()))
/*      */       {
/* 5129 */         i2 = 1;
/* 5130 */         localObject2 = localObject5;
/*      */       }
/*      */ 
/* 5134 */       if (i5 == 0) {
/* 5135 */         i6 = ((Container)localObject1).getX();
/* 5136 */         int i7 = ((Container)localObject1).getY();
/* 5137 */         k = ((Container)localObject1).getWidth();
/* 5138 */         m = ((Container)localObject1).getHeight();
/* 5139 */         SwingUtilities.computeIntersection(i, j, k, m, localRectangle);
/* 5140 */         localRectangle.x += i6;
/* 5141 */         localRectangle.y += i7;
/* 5142 */         n += i6;
/* 5143 */         i1 += i7;
/*      */       }
/* 5068 */       localObject4 = localObject1;
/*      */     }
/*      */ 
/* 5148 */     if ((localObject1 == null) || (((Container)localObject1).getPeer() == null) || (localRectangle.width <= 0) || (localRectangle.height <= 0))
/*      */     {
/* 5151 */       recycleRectangle(localRectangle);
/* 5152 */       return;
/*      */     }
/*      */ 
/* 5155 */     ((JComponent)localObject3).setFlag(13, true);
/*      */ 
/* 5157 */     localRectangle.x -= n;
/* 5158 */     localRectangle.y -= i1;
/*      */ 
/* 5162 */     if (localObject3 != this)
/*      */     {
/* 5164 */       for (i6 = i3; 
/* 5165 */         i6 > 0; i6--) {
/* 5166 */         localObject5 = (Component)localArrayList.get(i6);
/* 5167 */         if ((localObject5 instanceof JComponent))
/* 5168 */           ((JComponent)localObject5).setPaintingChild((Component)localArrayList.get(i6 - 1));
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*      */       Graphics localGraphics;
/* 5173 */       if ((localGraphics = safelyGetGraphics((Component)localObject3, (Component)localObject1)) != null) {
/*      */         try {
/* 5175 */           if (i2 != 0) {
/* 5176 */             localObject5 = RepaintManager.currentManager(localObject2);
/*      */ 
/* 5178 */             ((RepaintManager)localObject5).beginPaint();
/*      */             try {
/* 5180 */               ((RepaintManager)localObject5).paint((JComponent)localObject3, localObject2, localGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */             }
/*      */             finally
/*      */             {
/* 5186 */               ((RepaintManager)localObject5).endPaint();
/*      */             }
/*      */           } else {
/* 5189 */             localGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 5191 */             ((JComponent)localObject3).paint(localGraphics);
/*      */           }
/*      */         } finally {
/* 5194 */           localGraphics.dispose();
/*      */         }
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 5200 */       if (localObject3 != this)
/*      */       {
/* 5202 */         for (int i9 = i3; 
/* 5203 */           i9 > 0; i9--) {
/* 5204 */           Component localComponent = (Component)localArrayList.get(i9);
/* 5205 */           if ((localComponent instanceof JComponent)) {
/* 5206 */             ((JComponent)localComponent).setPaintingChild(null);
/*      */           }
/*      */         }
/*      */       }
/* 5210 */       ((JComponent)localObject3).setFlag(13, false);
/*      */     }
/* 5212 */     recycleRectangle(localRectangle);
/*      */   }
/*      */ 
/*      */   void paintToOffscreen(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*      */     try
/*      */     {
/* 5225 */       setFlag(1, true);
/* 5226 */       if ((paramInt2 + paramInt4 < paramInt6) || (paramInt1 + paramInt3 < paramInt5)) {
/* 5227 */         setFlag(2, true);
/*      */       }
/* 5229 */       if (getFlag(13))
/*      */       {
/* 5232 */         paint(paramGraphics);
/*      */       }
/*      */       else {
/* 5235 */         if (!rectangleIsObscured(paramInt1, paramInt2, paramInt3, paramInt4)) {
/* 5236 */           paintComponent(paramGraphics);
/* 5237 */           paintBorder(paramGraphics);
/*      */         }
/* 5239 */         paintChildren(paramGraphics);
/*      */       }
/*      */     } finally {
/* 5242 */       setFlag(1, false);
/* 5243 */       setFlag(2, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getObscuredState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 5258 */     int i = 0;
/* 5259 */     Rectangle localRectangle1 = fetchRectangle();
/*      */ 
/* 5261 */     for (int j = paramInt1 - 1; j >= 0; j--) {
/* 5262 */       Component localComponent = getComponent(j);
/* 5263 */       if (localComponent.isVisible())
/*      */       {
/*      */         boolean bool;
/* 5268 */         if ((localComponent instanceof JComponent)) {
/* 5269 */           bool = localComponent.isOpaque();
/* 5270 */           if ((!bool) && 
/* 5271 */             (i == 1)) {
/* 5272 */             continue;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 5277 */           bool = true;
/*      */         }
/* 5279 */         Rectangle localRectangle2 = localComponent.getBounds(localRectangle1);
/* 5280 */         if ((bool) && (paramInt2 >= localRectangle2.x) && (paramInt2 + paramInt4 <= localRectangle2.x + localRectangle2.width) && (paramInt3 >= localRectangle2.y) && (paramInt3 + paramInt5 <= localRectangle2.y + localRectangle2.height))
/*      */         {
/* 5284 */           recycleRectangle(localRectangle1);
/* 5285 */           return 2;
/*      */         }
/* 5287 */         if ((i == 0) && (paramInt2 + paramInt4 > localRectangle2.x) && (paramInt3 + paramInt5 > localRectangle2.y) && (paramInt2 < localRectangle2.x + localRectangle2.width) && (paramInt3 < localRectangle2.y + localRectangle2.height))
/*      */         {
/* 5292 */           i = 1;
/*      */         }
/*      */       }
/*      */     }
/* 5295 */     recycleRectangle(localRectangle1);
/* 5296 */     return i;
/*      */   }
/*      */ 
/*      */   boolean checkIfChildObscuredBySibling()
/*      */   {
/* 5308 */     return true;
/*      */   }
/*      */ 
/*      */   private void setFlag(int paramInt, boolean paramBoolean)
/*      */   {
/* 5313 */     if (paramBoolean)
/* 5314 */       this.flags |= 1 << paramInt;
/*      */     else
/* 5316 */       this.flags &= (1 << paramInt ^ 0xFFFFFFFF);
/*      */   }
/*      */ 
/*      */   private boolean getFlag(int paramInt) {
/* 5320 */     int i = 1 << paramInt;
/* 5321 */     return (this.flags & i) == i;
/*      */   }
/*      */ 
/*      */   static void setWriteObjCounter(JComponent paramJComponent, byte paramByte)
/*      */   {
/* 5327 */     paramJComponent.flags = (paramJComponent.flags & 0xFFC03FFF | paramByte << 14);
/*      */   }
/*      */ 
/*      */   static byte getWriteObjCounter(JComponent paramJComponent) {
/* 5331 */     return (byte)(paramJComponent.flags >> 14 & 0xFF);
/*      */   }
/*      */ 
/*      */   public void setDoubleBuffered(boolean paramBoolean)
/*      */   {
/* 5347 */     setFlag(0, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isDoubleBuffered()
/*      */   {
/* 5356 */     return getFlag(0);
/*      */   }
/*      */ 
/*      */   public JRootPane getRootPane()
/*      */   {
/* 5366 */     return SwingUtilities.getRootPane(this);
/*      */   }
/*      */ 
/*      */   void compWriteObjectNotify()
/*      */   {
/* 5377 */     int i = getWriteObjCounter(this);
/* 5378 */     setWriteObjCounter(this, (byte)(i + 1));
/* 5379 */     if (i != 0) {
/* 5380 */       return;
/*      */     }
/*      */ 
/* 5383 */     uninstallUIAndProperties();
/*      */ 
/* 5392 */     if ((getToolTipText() != null) || ((this instanceof JTableHeader)))
/*      */     {
/* 5394 */       ToolTipManager.sharedInstance().unregisterComponent(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 5489 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 5497 */     ReadObjectCallback localReadObjectCallback = (ReadObjectCallback)readObjectCallbacks.get(paramObjectInputStream);
/* 5498 */     if (localReadObjectCallback == null) {
/*      */       try {
/* 5500 */         readObjectCallbacks.put(paramObjectInputStream, localReadObjectCallback = new ReadObjectCallback(paramObjectInputStream));
/*      */       }
/*      */       catch (Exception localException) {
/* 5503 */         throw new IOException(localException.toString());
/*      */       }
/*      */     }
/* 5506 */     localReadObjectCallback.registerComponent(this);
/*      */ 
/* 5509 */     int i = paramObjectInputStream.readInt();
/* 5510 */     if (i > 0) {
/* 5511 */       this.clientProperties = new ArrayTable();
/* 5512 */       for (int j = 0; j < i; j++) {
/* 5513 */         this.clientProperties.put(paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
/*      */       }
/*      */     }
/*      */ 
/* 5517 */     if (getToolTipText() != null) {
/* 5518 */       ToolTipManager.sharedInstance().registerComponent(this);
/*      */     }
/* 5520 */     setWriteObjCounter(this, (byte)0);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 5536 */     paramObjectOutputStream.defaultWriteObject();
/* 5537 */     if (getUIClassID().equals("ComponentUI")) {
/* 5538 */       byte b = getWriteObjCounter(this);
/* 5539 */       b = (byte)(b - 1); setWriteObjCounter(this, b);
/* 5540 */       if ((b == 0) && (this.ui != null)) {
/* 5541 */         this.ui.installUI(this);
/*      */       }
/*      */     }
/* 5544 */     ArrayTable.writeArrayTable(paramObjectOutputStream, this.clientProperties);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 5559 */     String str1 = isPreferredSizeSet() ? getPreferredSize().toString() : "";
/*      */ 
/* 5561 */     String str2 = isMinimumSizeSet() ? getMinimumSize().toString() : "";
/*      */ 
/* 5563 */     String str3 = isMaximumSizeSet() ? getMaximumSize().toString() : "";
/*      */ 
/* 5565 */     String str4 = this.border == this ? "this" : this.border == null ? "" : this.border.toString();
/*      */ 
/* 5568 */     return super.paramString() + ",alignmentX=" + this.alignmentX + ",alignmentY=" + this.alignmentY + ",border=" + str4 + ",flags=" + this.flags + ",maximumSize=" + str3 + ",minimumSize=" + str2 + ",preferredSize=" + str1;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void hide()
/*      */   {
/* 5584 */     boolean bool = isShowing();
/* 5585 */     super.hide();
/* 5586 */     if (bool) {
/* 5587 */       Container localContainer = getParent();
/* 5588 */       if (localContainer != null) {
/* 5589 */         Rectangle localRectangle = getBounds();
/* 5590 */         localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/* 5592 */       revalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract class AccessibleJComponent extends Container.AccessibleAWTContainer
/*      */     implements AccessibleExtendedComponent
/*      */   {
/* 3690 */     protected FocusListener accessibleFocusHandler = null;
/*      */ 
/*      */     protected AccessibleJComponent()
/*      */     {
/* 3687 */       super();
/*      */     }
/*      */ 
/*      */     public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 3745 */       if (this.accessibleFocusHandler == null) {
/* 3746 */         this.accessibleFocusHandler = new AccessibleFocusHandler();
/* 3747 */         JComponent.this.addFocusListener(this.accessibleFocusHandler);
/*      */       }
/* 3749 */       if (this.accessibleContainerHandler == null) {
/* 3750 */         this.accessibleContainerHandler = new AccessibleContainerHandler();
/* 3751 */         JComponent.this.addContainerListener(this.accessibleContainerHandler);
/*      */       }
/* 3753 */       super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/* 3764 */       if (this.accessibleFocusHandler != null) {
/* 3765 */         JComponent.this.removeFocusListener(this.accessibleFocusHandler);
/* 3766 */         this.accessibleFocusHandler = null;
/*      */       }
/* 3768 */       super.removePropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */ 
/*      */     protected String getBorderTitle(Border paramBorder)
/*      */     {
/* 3784 */       if ((paramBorder instanceof TitledBorder))
/* 3785 */         return ((TitledBorder)paramBorder).getTitle();
/* 3786 */       if ((paramBorder instanceof CompoundBorder)) {
/* 3787 */         String str = getBorderTitle(((CompoundBorder)paramBorder).getInsideBorder());
/* 3788 */         if (str == null) {
/* 3789 */           str = getBorderTitle(((CompoundBorder)paramBorder).getOutsideBorder());
/*      */         }
/* 3791 */         return str;
/*      */       }
/* 3793 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/* 3814 */       String str = this.accessibleName;
/*      */ 
/* 3818 */       if (str == null) {
/* 3819 */         str = (String)JComponent.this.getClientProperty("AccessibleName");
/*      */       }
/*      */ 
/* 3824 */       if (str == null) {
/* 3825 */         str = getBorderTitle(JComponent.this.getBorder());
/*      */       }
/*      */ 
/* 3830 */       if (str == null) {
/* 3831 */         Object localObject = JComponent.this.getClientProperty("labeledBy");
/* 3832 */         if ((localObject instanceof Accessible)) {
/* 3833 */           AccessibleContext localAccessibleContext = ((Accessible)localObject).getAccessibleContext();
/* 3834 */           if (localAccessibleContext != null) {
/* 3835 */             str = localAccessibleContext.getAccessibleName();
/*      */           }
/*      */         }
/*      */       }
/* 3839 */       return str;
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription()
/*      */     {
/* 3858 */       String str = this.accessibleDescription;
/*      */ 
/* 3862 */       if (str == null) {
/* 3863 */         str = (String)JComponent.this.getClientProperty("AccessibleDescription");
/*      */       }
/*      */ 
/* 3868 */       if (str == null) {
/*      */         try {
/* 3870 */           str = getToolTipText();
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3886 */       if (str == null) {
/* 3887 */         Object localObject = JComponent.this.getClientProperty("labeledBy");
/* 3888 */         if ((localObject instanceof Accessible)) {
/* 3889 */           AccessibleContext localAccessibleContext = ((Accessible)localObject).getAccessibleContext();
/* 3890 */           if (localAccessibleContext != null) {
/* 3891 */             str = localAccessibleContext.getAccessibleDescription();
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 3896 */       return str;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 3907 */       return AccessibleRole.SWING_COMPONENT;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 3918 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 3919 */       if (JComponent.this.isOpaque()) {
/* 3920 */         localAccessibleStateSet.add(AccessibleState.OPAQUE);
/*      */       }
/* 3922 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 3933 */       return super.getAccessibleChildrenCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 3943 */       return super.getAccessibleChild(paramInt);
/*      */     }
/*      */ 
/*      */     AccessibleExtendedComponent getAccessibleExtendedComponent()
/*      */     {
/* 3954 */       return this;
/*      */     }
/*      */ 
/*      */     public String getToolTipText()
/*      */     {
/* 3965 */       return JComponent.this.getToolTipText();
/*      */     }
/*      */ 
/*      */     public String getTitledBorderText()
/*      */     {
/* 3976 */       Border localBorder = JComponent.this.getBorder();
/* 3977 */       if ((localBorder instanceof TitledBorder)) {
/* 3978 */         return ((TitledBorder)localBorder).getTitle();
/*      */       }
/* 3980 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleKeyBinding getAccessibleKeyBinding()
/*      */     {
/* 3994 */       Object localObject = JComponent.this.getClientProperty("labeledBy");
/* 3995 */       if ((localObject instanceof Accessible)) {
/* 3996 */         AccessibleContext localAccessibleContext = ((Accessible)localObject).getAccessibleContext();
/* 3997 */         if (localAccessibleContext != null) {
/* 3998 */           AccessibleComponent localAccessibleComponent = localAccessibleContext.getAccessibleComponent();
/* 3999 */           if (!(localAccessibleComponent instanceof AccessibleExtendedComponent))
/* 4000 */             return null;
/* 4001 */           return ((AccessibleExtendedComponent)localAccessibleComponent).getAccessibleKeyBinding();
/*      */         }
/*      */       }
/* 4004 */       return null;
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
/* 3699 */         Component localComponent = paramContainerEvent.getChild();
/* 3700 */         if ((localComponent != null) && ((localComponent instanceof Accessible)))
/* 3701 */           JComponent.AccessibleJComponent.this.firePropertyChange("AccessibleChild", null, localComponent.getAccessibleContext());
/*      */       }
/*      */ 
/*      */       public void componentRemoved(ContainerEvent paramContainerEvent)
/*      */       {
/* 3707 */         Component localComponent = paramContainerEvent.getChild();
/* 3708 */         if ((localComponent != null) && ((localComponent instanceof Accessible)))
/* 3709 */           JComponent.AccessibleJComponent.this.firePropertyChange("AccessibleChild", localComponent.getAccessibleContext(), null);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected class AccessibleFocusHandler
/*      */       implements FocusListener
/*      */     {
/*      */       protected AccessibleFocusHandler()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void focusGained(FocusEvent paramFocusEvent)
/*      */       {
/* 3723 */         if (JComponent.this.accessibleContext != null)
/* 3724 */           JComponent.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.FOCUSED);
/*      */       }
/*      */ 
/*      */       public void focusLost(FocusEvent paramFocusEvent)
/*      */       {
/* 3730 */         if (JComponent.this.accessibleContext != null)
/* 3731 */           JComponent.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.FOCUSED, null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class ActionStandin
/*      */     implements Action
/*      */   {
/*      */     private final ActionListener actionListener;
/*      */     private final String command;
/*      */     private final Action action;
/*      */ 
/*      */     ActionStandin(ActionListener paramString, String arg3)
/*      */     {
/* 3380 */       this.actionListener = paramString;
/* 3381 */       if ((paramString instanceof Action)) {
/* 3382 */         this.action = ((Action)paramString);
/*      */       }
/*      */       else
/* 3385 */         this.action = null;
/*      */       Object localObject;
/* 3387 */       this.command = localObject;
/*      */     }
/*      */ 
/*      */     public Object getValue(String paramString) {
/* 3391 */       if (paramString != null) {
/* 3392 */         if (paramString.equals("ActionCommandKey")) {
/* 3393 */           return this.command;
/*      */         }
/* 3395 */         if (this.action != null) {
/* 3396 */           return this.action.getValue(paramString);
/*      */         }
/* 3398 */         if (paramString.equals("Name")) {
/* 3399 */           return "ActionStandin";
/*      */         }
/*      */       }
/* 3402 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 3406 */       if (this.actionListener == null)
/*      */       {
/* 3412 */         return false;
/*      */       }
/* 3414 */       if (this.action == null) {
/* 3415 */         return true;
/*      */       }
/* 3417 */       return this.action.isEnabled();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 3421 */       if (this.actionListener != null)
/* 3422 */         this.actionListener.actionPerformed(paramActionEvent);
/*      */     }
/*      */ 
/*      */     public void putValue(String paramString, Object paramObject)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setEnabled(boolean paramBoolean)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class IntVector {
/* 3443 */     int[] array = null;
/* 3444 */     int count = 0;
/* 3445 */     int capacity = 0;
/*      */ 
/*      */     int size() {
/* 3448 */       return this.count;
/*      */     }
/*      */ 
/*      */     int elementAt(int paramInt) {
/* 3452 */       return this.array[paramInt];
/*      */     }
/*      */ 
/*      */     void addElement(int paramInt) {
/* 3456 */       if (this.count == this.capacity) {
/* 3457 */         this.capacity = ((this.capacity + 2) * 2);
/* 3458 */         int[] arrayOfInt = new int[this.capacity];
/* 3459 */         if (this.count > 0) {
/* 3460 */           System.arraycopy(this.array, 0, arrayOfInt, 0, this.count);
/*      */         }
/* 3462 */         this.array = arrayOfInt;
/*      */       }
/* 3464 */       this.array[(this.count++)] = paramInt;
/*      */     }
/*      */ 
/*      */     void setElementAt(int paramInt1, int paramInt2) {
/* 3468 */       this.array[paramInt2] = paramInt1;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class KeyboardState implements Serializable {
/* 3473 */     private static final Object keyCodesKey = KeyboardState.class;
/*      */ 
/*      */     static JComponent.IntVector getKeyCodeArray()
/*      */     {
/* 3478 */       JComponent.IntVector localIntVector = (JComponent.IntVector)SwingUtilities.appContextGet(keyCodesKey);
/*      */ 
/* 3480 */       if (localIntVector == null) {
/* 3481 */         localIntVector = new JComponent.IntVector();
/* 3482 */         SwingUtilities.appContextPut(keyCodesKey, localIntVector);
/*      */       }
/* 3484 */       return localIntVector;
/*      */     }
/*      */ 
/*      */     static void registerKeyPressed(int paramInt) {
/* 3488 */       JComponent.IntVector localIntVector = getKeyCodeArray();
/* 3489 */       int i = localIntVector.size();
/*      */ 
/* 3491 */       for (int j = 0; j < i; j++) {
/* 3492 */         if (localIntVector.elementAt(j) == -1) {
/* 3493 */           localIntVector.setElementAt(paramInt, j);
/* 3494 */           return;
/*      */         }
/*      */       }
/* 3497 */       localIntVector.addElement(paramInt);
/*      */     }
/*      */ 
/*      */     static void registerKeyReleased(int paramInt) {
/* 3501 */       JComponent.IntVector localIntVector = getKeyCodeArray();
/* 3502 */       int i = localIntVector.size();
/*      */ 
/* 3504 */       for (int j = 0; j < i; j++)
/* 3505 */         if (localIntVector.elementAt(j) == paramInt) {
/* 3506 */           localIntVector.setElementAt(-1, j);
/* 3507 */           return;
/*      */         }
/*      */     }
/*      */ 
/*      */     static boolean keyIsPressed(int paramInt)
/*      */     {
/* 3513 */       JComponent.IntVector localIntVector = getKeyCodeArray();
/* 3514 */       int i = localIntVector.size();
/*      */ 
/* 3516 */       for (int j = 0; j < i; j++) {
/* 3517 */         if (localIntVector.elementAt(j) == paramInt) {
/* 3518 */           return true;
/*      */         }
/*      */       }
/* 3521 */       return false;
/*      */     }
/*      */ 
/*      */     static boolean shouldProcess(KeyEvent paramKeyEvent)
/*      */     {
/* 3529 */       switch (paramKeyEvent.getID()) {
/*      */       case 401:
/* 3531 */         if (!keyIsPressed(paramKeyEvent.getKeyCode())) {
/* 3532 */           registerKeyPressed(paramKeyEvent.getKeyCode());
/*      */         }
/* 3534 */         return true;
/*      */       case 402:
/* 3540 */         if ((keyIsPressed(paramKeyEvent.getKeyCode())) || (paramKeyEvent.getKeyCode() == 154)) {
/* 3541 */           registerKeyReleased(paramKeyEvent.getKeyCode());
/* 3542 */           return true;
/*      */         }
/* 3544 */         return false;
/*      */       case 400:
/* 3546 */         return true;
/*      */       }
/*      */ 
/* 3549 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ReadObjectCallback
/*      */     implements ObjectInputValidation
/*      */   {
/* 5416 */     private final Vector<JComponent> roots = new Vector(1);
/*      */     private final ObjectInputStream inputStream;
/*      */ 
/*      */     ReadObjectCallback(ObjectInputStream arg2)
/*      */       throws Exception
/*      */     {
/*      */       Object localObject;
/* 5420 */       this.inputStream = localObject;
/* 5421 */       localObject.registerValidation(this, 0);
/*      */     }
/*      */ 
/*      */     public void validateObject()
/*      */       throws InvalidObjectException
/*      */     {
/*      */       try
/*      */       {
/* 5432 */         for (JComponent localJComponent : this.roots)
/* 5433 */           SwingUtilities.updateComponentTreeUI(localJComponent);
/*      */       }
/*      */       finally
/*      */       {
/* 5437 */         JComponent.readObjectCallbacks.remove(this.inputStream);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void registerComponent(JComponent paramJComponent)
/*      */     {
/* 5452 */       for (Iterator localIterator = this.roots.iterator(); localIterator.hasNext(); ) { localJComponent = (JComponent)localIterator.next();
/* 5453 */         for (localObject = paramJComponent; localObject != null; localObject = ((Component)localObject).getParent())
/* 5454 */           if (localObject == localJComponent)
/*      */             return;
/*      */       }
/*      */       JComponent localJComponent;
/*      */       Object localObject;
/* 5464 */       for (int i = 0; i < this.roots.size(); i++) {
/* 5465 */         localJComponent = (JComponent)this.roots.elementAt(i);
/* 5466 */         for (localObject = localJComponent.getParent(); localObject != null; localObject = ((Component)localObject).getParent()) {
/* 5467 */           if (localObject == paramJComponent) {
/* 5468 */             this.roots.removeElementAt(i--);
/* 5469 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 5474 */       this.roots.addElement(paramJComponent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JComponent
 * JD-Core Version:    0.6.2
 */