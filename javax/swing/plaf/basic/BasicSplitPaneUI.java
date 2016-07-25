/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Canvas;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.awt.peer.LightweightPeer;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JSplitPane;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.SplitPaneUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicSplitPaneUI extends SplitPaneUI
/*      */ {
/*      */   protected static final String NON_CONTINUOUS_DIVIDER = "nonContinuousDivider";
/*   67 */   protected static int KEYBOARD_DIVIDER_MOVE_OFFSET = 3;
/*      */   protected JSplitPane splitPane;
/*      */   protected BasicHorizontalLayoutManager layoutManager;
/*      */   protected BasicSplitPaneDivider divider;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected FocusListener focusListener;
/*      */   private Handler handler;
/*      */   private Set<KeyStroke> managingFocusForwardTraversalKeys;
/*      */   private Set<KeyStroke> managingFocusBackwardTraversalKeys;
/*      */   protected int dividerSize;
/*      */   protected Component nonContinuousLayoutDivider;
/*      */   protected boolean draggingHW;
/*      */   protected int beginDragDividerLocation;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke upKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke downKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke leftKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke rightKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke homeKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke endKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected KeyStroke dividerResizeToggleKey;
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener keyboardUpLeftListener;
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener keyboardDownRightListener;
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener keyboardHomeListener;
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener keyboardEndListener;
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener keyboardResizeToggleListener;
/*      */   private int orientation;
/*      */   private int lastDragLocation;
/*      */   private boolean continuousLayout;
/*      */   private boolean dividerKeyboardResize;
/*      */   private boolean dividerLocationIsSet;
/*      */   private Color dividerDraggingColor;
/*      */   private boolean rememberPaneSizes;
/*      */   private boolean keepHidden;
/*      */   boolean painted;
/*      */   boolean ignoreDividerLocationChange;
/*      */ 
/*      */   public BasicSplitPaneUI()
/*      */   {
/*  277 */     this.keepHidden = false;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  292 */     return new BasicSplitPaneUI();
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  296 */     paramLazyActionMap.put(new Actions("negativeIncrement"));
/*  297 */     paramLazyActionMap.put(new Actions("positiveIncrement"));
/*  298 */     paramLazyActionMap.put(new Actions("selectMin"));
/*  299 */     paramLazyActionMap.put(new Actions("selectMax"));
/*  300 */     paramLazyActionMap.put(new Actions("startResize"));
/*  301 */     paramLazyActionMap.put(new Actions("toggleFocus"));
/*  302 */     paramLazyActionMap.put(new Actions("focusOutForward"));
/*  303 */     paramLazyActionMap.put(new Actions("focusOutBackward"));
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  312 */     this.splitPane = ((JSplitPane)paramJComponent);
/*  313 */     this.dividerLocationIsSet = false;
/*  314 */     this.dividerKeyboardResize = false;
/*  315 */     this.keepHidden = false;
/*  316 */     installDefaults();
/*  317 */     installListeners();
/*  318 */     installKeyboardActions();
/*  319 */     setLastDragLocation(-1);
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  327 */     LookAndFeel.installBorder(this.splitPane, "SplitPane.border");
/*  328 */     LookAndFeel.installColors(this.splitPane, "SplitPane.background", "SplitPane.foreground");
/*      */ 
/*  330 */     LookAndFeel.installProperty(this.splitPane, "opaque", Boolean.TRUE);
/*      */ 
/*  332 */     if (this.divider == null) this.divider = createDefaultDivider();
/*  333 */     this.divider.setBasicSplitPaneUI(this);
/*      */ 
/*  335 */     Border localBorder = this.divider.getBorder();
/*      */ 
/*  337 */     if ((localBorder == null) || (!(localBorder instanceof UIResource))) {
/*  338 */       this.divider.setBorder(UIManager.getBorder("SplitPaneDivider.border"));
/*      */     }
/*      */ 
/*  341 */     this.dividerDraggingColor = UIManager.getColor("SplitPaneDivider.draggingColor");
/*      */ 
/*  343 */     setOrientation(this.splitPane.getOrientation());
/*      */ 
/*  347 */     Integer localInteger = (Integer)UIManager.get("SplitPane.dividerSize");
/*  348 */     LookAndFeel.installProperty(this.splitPane, "dividerSize", Integer.valueOf(localInteger == null ? 10 : localInteger.intValue()));
/*      */ 
/*  350 */     this.divider.setDividerSize(this.splitPane.getDividerSize());
/*  351 */     this.dividerSize = this.divider.getDividerSize();
/*  352 */     this.splitPane.add(this.divider, "divider");
/*      */ 
/*  354 */     setContinuousLayout(this.splitPane.isContinuousLayout());
/*      */ 
/*  356 */     resetLayoutManager();
/*      */ 
/*  360 */     if (this.nonContinuousLayoutDivider == null) {
/*  361 */       setNonContinuousLayoutDivider(createDefaultNonContinuousLayoutDivider(), true);
/*      */     }
/*      */     else
/*      */     {
/*  365 */       setNonContinuousLayoutDivider(this.nonContinuousLayoutDivider, true);
/*      */     }
/*      */ 
/*  369 */     if (this.managingFocusForwardTraversalKeys == null) {
/*  370 */       this.managingFocusForwardTraversalKeys = new HashSet();
/*  371 */       this.managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
/*      */     }
/*      */ 
/*  374 */     this.splitPane.setFocusTraversalKeys(0, this.managingFocusForwardTraversalKeys);
/*      */ 
/*  377 */     if (this.managingFocusBackwardTraversalKeys == null) {
/*  378 */       this.managingFocusBackwardTraversalKeys = new HashSet();
/*  379 */       this.managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
/*      */     }
/*      */ 
/*  382 */     this.splitPane.setFocusTraversalKeys(1, this.managingFocusBackwardTraversalKeys);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  391 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null)
/*      */     {
/*  393 */       this.splitPane.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */ 
/*  396 */     if ((this.focusListener = createFocusListener()) != null)
/*  397 */       this.splitPane.addFocusListener(this.focusListener);
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  406 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  409 */     SwingUtilities.replaceUIInputMap(this.splitPane, 1, localInputMap);
/*      */ 
/*  412 */     LazyActionMap.installLazyActionMap(this.splitPane, BasicSplitPaneUI.class, "SplitPane.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/*  417 */     if (paramInt == 1) {
/*  418 */       return (InputMap)DefaultLookup.get(this.splitPane, this, "SplitPane.ancestorInputMap");
/*      */     }
/*      */ 
/*  421 */     return null;
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  428 */     uninstallKeyboardActions();
/*  429 */     uninstallListeners();
/*  430 */     uninstallDefaults();
/*  431 */     this.dividerLocationIsSet = false;
/*  432 */     this.dividerKeyboardResize = false;
/*  433 */     this.splitPane = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  441 */     if (this.splitPane.getLayout() == this.layoutManager) {
/*  442 */       this.splitPane.setLayout(null);
/*      */     }
/*      */ 
/*  445 */     if (this.nonContinuousLayoutDivider != null) {
/*  446 */       this.splitPane.remove(this.nonContinuousLayoutDivider);
/*      */     }
/*      */ 
/*  449 */     LookAndFeel.uninstallBorder(this.splitPane);
/*      */ 
/*  451 */     Border localBorder = this.divider.getBorder();
/*      */ 
/*  453 */     if ((localBorder instanceof UIResource)) {
/*  454 */       this.divider.setBorder(null);
/*      */     }
/*      */ 
/*  457 */     this.splitPane.remove(this.divider);
/*  458 */     this.divider.setBasicSplitPaneUI(null);
/*  459 */     this.layoutManager = null;
/*  460 */     this.divider = null;
/*  461 */     this.nonContinuousLayoutDivider = null;
/*      */ 
/*  463 */     setNonContinuousLayoutDivider(null);
/*      */ 
/*  467 */     this.splitPane.setFocusTraversalKeys(0, null);
/*  468 */     this.splitPane.setFocusTraversalKeys(1, null);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  476 */     if (this.propertyChangeListener != null) {
/*  477 */       this.splitPane.removePropertyChangeListener(this.propertyChangeListener);
/*  478 */       this.propertyChangeListener = null;
/*      */     }
/*  480 */     if (this.focusListener != null) {
/*  481 */       this.splitPane.removeFocusListener(this.focusListener);
/*  482 */       this.focusListener = null;
/*      */     }
/*      */ 
/*  485 */     this.keyboardUpLeftListener = null;
/*  486 */     this.keyboardDownRightListener = null;
/*  487 */     this.keyboardHomeListener = null;
/*  488 */     this.keyboardEndListener = null;
/*  489 */     this.keyboardResizeToggleListener = null;
/*  490 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  498 */     SwingUtilities.replaceUIActionMap(this.splitPane, null);
/*  499 */     SwingUtilities.replaceUIInputMap(this.splitPane, 1, null);
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  509 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  513 */     if (this.handler == null) {
/*  514 */       this.handler = new Handler(null);
/*      */     }
/*  516 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/*  524 */     return getHandler();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener createKeyboardUpLeftListener()
/*      */   {
/*  543 */     return new KeyboardUpLeftHandler();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener createKeyboardDownRightListener()
/*      */   {
/*  562 */     return new KeyboardDownRightHandler();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener createKeyboardHomeListener()
/*      */   {
/*  581 */     return new KeyboardHomeHandler();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener createKeyboardEndListener()
/*      */   {
/*  600 */     return new KeyboardEndHandler();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected ActionListener createKeyboardResizeToggleListener()
/*      */   {
/*  619 */     return new KeyboardResizeToggleHandler();
/*      */   }
/*      */ 
/*      */   public int getOrientation()
/*      */   {
/*  627 */     return this.orientation;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  635 */     this.orientation = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean isContinuousLayout()
/*      */   {
/*  643 */     return this.continuousLayout;
/*      */   }
/*      */ 
/*      */   public void setContinuousLayout(boolean paramBoolean)
/*      */   {
/*  651 */     this.continuousLayout = paramBoolean;
/*      */   }
/*      */ 
/*      */   public int getLastDragLocation()
/*      */   {
/*  659 */     return this.lastDragLocation;
/*      */   }
/*      */ 
/*      */   public void setLastDragLocation(int paramInt)
/*      */   {
/*  667 */     this.lastDragLocation = paramInt;
/*      */   }
/*      */ 
/*      */   int getKeyboardMoveIncrement()
/*      */   {
/*  674 */     return 3;
/*      */   }
/*      */ 
/*      */   public BasicSplitPaneDivider getDivider()
/*      */   {
/*  824 */     return this.divider;
/*      */   }
/*      */ 
/*      */   protected Component createDefaultNonContinuousLayoutDivider()
/*      */   {
/*  833 */     return new Canvas() {
/*      */       public void paint(Graphics paramAnonymousGraphics) {
/*  835 */         if ((!BasicSplitPaneUI.this.isContinuousLayout()) && (BasicSplitPaneUI.this.getLastDragLocation() != -1)) {
/*  836 */           Dimension localDimension = BasicSplitPaneUI.this.splitPane.getSize();
/*      */ 
/*  838 */           paramAnonymousGraphics.setColor(BasicSplitPaneUI.this.dividerDraggingColor);
/*  839 */           if (BasicSplitPaneUI.this.orientation == 1)
/*  840 */             paramAnonymousGraphics.fillRect(0, 0, BasicSplitPaneUI.this.dividerSize - 1, localDimension.height - 1);
/*      */           else
/*  842 */             paramAnonymousGraphics.fillRect(0, 0, localDimension.width - 1, BasicSplitPaneUI.this.dividerSize - 1);
/*      */         }
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   protected void setNonContinuousLayoutDivider(Component paramComponent)
/*      */   {
/*  857 */     setNonContinuousLayoutDivider(paramComponent, true);
/*      */   }
/*      */ 
/*      */   protected void setNonContinuousLayoutDivider(Component paramComponent, boolean paramBoolean)
/*      */   {
/*  866 */     this.rememberPaneSizes = paramBoolean;
/*  867 */     if ((this.nonContinuousLayoutDivider != null) && (this.splitPane != null)) {
/*  868 */       this.splitPane.remove(this.nonContinuousLayoutDivider);
/*      */     }
/*  870 */     this.nonContinuousLayoutDivider = paramComponent;
/*      */   }
/*      */ 
/*      */   private void addHeavyweightDivider() {
/*  874 */     if ((this.nonContinuousLayoutDivider != null) && (this.splitPane != null))
/*      */     {
/*  880 */       Component localComponent1 = this.splitPane.getLeftComponent();
/*  881 */       Component localComponent2 = this.splitPane.getRightComponent();
/*  882 */       int i = this.splitPane.getDividerLocation();
/*      */ 
/*  885 */       if (localComponent1 != null)
/*  886 */         this.splitPane.setLeftComponent(null);
/*  887 */       if (localComponent2 != null)
/*  888 */         this.splitPane.setRightComponent(null);
/*  889 */       this.splitPane.remove(this.divider);
/*  890 */       this.splitPane.add(this.nonContinuousLayoutDivider, "nonContinuousDivider", this.splitPane.getComponentCount());
/*      */ 
/*  893 */       this.splitPane.setLeftComponent(localComponent1);
/*  894 */       this.splitPane.setRightComponent(localComponent2);
/*  895 */       this.splitPane.add(this.divider, "divider");
/*  896 */       if (this.rememberPaneSizes)
/*  897 */         this.splitPane.setDividerLocation(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Component getNonContinuousLayoutDivider()
/*      */   {
/*  910 */     return this.nonContinuousLayoutDivider;
/*      */   }
/*      */ 
/*      */   public JSplitPane getSplitPane()
/*      */   {
/*  919 */     return this.splitPane;
/*      */   }
/*      */ 
/*      */   public BasicSplitPaneDivider createDefaultDivider()
/*      */   {
/*  927 */     return new BasicSplitPaneDivider(this);
/*      */   }
/*      */ 
/*      */   public void resetToPreferredSizes(JSplitPane paramJSplitPane)
/*      */   {
/*  935 */     if (this.splitPane != null) {
/*  936 */       this.layoutManager.resetToPreferredSizes();
/*  937 */       this.splitPane.revalidate();
/*  938 */       this.splitPane.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDividerLocation(JSplitPane paramJSplitPane, int paramInt)
/*      */   {
/*  947 */     if (!this.ignoreDividerLocationChange) {
/*  948 */       this.dividerLocationIsSet = true;
/*  949 */       this.splitPane.revalidate();
/*  950 */       this.splitPane.repaint();
/*      */ 
/*  952 */       if (this.keepHidden) {
/*  953 */         Insets localInsets = this.splitPane.getInsets();
/*  954 */         int i = this.splitPane.getOrientation();
/*  955 */         if (((i == 0) && (paramInt != localInsets.top) && (paramInt != this.splitPane.getHeight() - this.divider.getHeight() - localInsets.top)) || ((i == 1) && (paramInt != localInsets.left) && (paramInt != this.splitPane.getWidth() - this.divider.getWidth() - localInsets.left)))
/*      */         {
/*  961 */           setKeepHidden(false);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  966 */       this.ignoreDividerLocationChange = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getDividerLocation(JSplitPane paramJSplitPane)
/*      */   {
/*  976 */     if (this.orientation == 1)
/*  977 */       return this.divider.getLocation().x;
/*  978 */     return this.divider.getLocation().y;
/*      */   }
/*      */ 
/*      */   public int getMinimumDividerLocation(JSplitPane paramJSplitPane)
/*      */   {
/*  986 */     int i = 0;
/*  987 */     Component localComponent = this.splitPane.getLeftComponent();
/*      */ 
/*  989 */     if ((localComponent != null) && (localComponent.isVisible())) {
/*  990 */       Insets localInsets = this.splitPane.getInsets();
/*  991 */       Dimension localDimension = localComponent.getMinimumSize();
/*  992 */       if (this.orientation == 1)
/*  993 */         i = localDimension.width;
/*      */       else {
/*  995 */         i = localDimension.height;
/*      */       }
/*  997 */       if (localInsets != null) {
/*  998 */         if (this.orientation == 1)
/*  999 */           i += localInsets.left;
/*      */         else {
/* 1001 */           i += localInsets.top;
/*      */         }
/*      */       }
/*      */     }
/* 1005 */     return i;
/*      */   }
/*      */ 
/*      */   public int getMaximumDividerLocation(JSplitPane paramJSplitPane)
/*      */   {
/* 1013 */     Dimension localDimension1 = this.splitPane.getSize();
/* 1014 */     int i = 0;
/* 1015 */     Component localComponent = this.splitPane.getRightComponent();
/*      */ 
/* 1017 */     if (localComponent != null) {
/* 1018 */       Insets localInsets = this.splitPane.getInsets();
/* 1019 */       Dimension localDimension2 = new Dimension(0, 0);
/* 1020 */       if (localComponent.isVisible()) {
/* 1021 */         localDimension2 = localComponent.getMinimumSize();
/*      */       }
/* 1023 */       if (this.orientation == 1)
/* 1024 */         i = localDimension1.width - localDimension2.width;
/*      */       else {
/* 1026 */         i = localDimension1.height - localDimension2.height;
/*      */       }
/* 1028 */       i -= this.dividerSize;
/* 1029 */       if (localInsets != null) {
/* 1030 */         if (this.orientation == 1)
/* 1031 */           i -= localInsets.right;
/*      */         else {
/* 1033 */           i -= localInsets.top;
/*      */         }
/*      */       }
/*      */     }
/* 1037 */     return Math.max(getMinimumDividerLocation(this.splitPane), i);
/*      */   }
/*      */ 
/*      */   public void finishedPaintingChildren(JSplitPane paramJSplitPane, Graphics paramGraphics)
/*      */   {
/* 1046 */     if ((paramJSplitPane == this.splitPane) && (getLastDragLocation() != -1) && (!isContinuousLayout()) && (!this.draggingHW))
/*      */     {
/* 1048 */       Dimension localDimension = this.splitPane.getSize();
/*      */ 
/* 1050 */       paramGraphics.setColor(this.dividerDraggingColor);
/* 1051 */       if (this.orientation == 1) {
/* 1052 */         paramGraphics.fillRect(getLastDragLocation(), 0, this.dividerSize - 1, localDimension.height - 1);
/*      */       }
/*      */       else
/* 1055 */         paramGraphics.fillRect(0, this.lastDragLocation, localDimension.width - 1, this.dividerSize - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/* 1066 */     if ((!this.painted) && (this.splitPane.getDividerLocation() < 0)) {
/* 1067 */       this.ignoreDividerLocationChange = true;
/* 1068 */       this.splitPane.setDividerLocation(getDividerLocation(this.splitPane));
/*      */     }
/* 1070 */     this.painted = true;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/* 1079 */     if (this.splitPane != null)
/* 1080 */       return this.layoutManager.preferredLayoutSize(this.splitPane);
/* 1081 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/* 1090 */     if (this.splitPane != null)
/* 1091 */       return this.layoutManager.minimumLayoutSize(this.splitPane);
/* 1092 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/* 1101 */     if (this.splitPane != null)
/* 1102 */       return this.layoutManager.maximumLayoutSize(this.splitPane);
/* 1103 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Insets getInsets(JComponent paramJComponent)
/*      */   {
/* 1112 */     return null;
/*      */   }
/*      */ 
/*      */   protected void resetLayoutManager()
/*      */   {
/* 1121 */     if (this.orientation == 1)
/* 1122 */       this.layoutManager = new BasicHorizontalLayoutManager(0);
/*      */     else {
/* 1124 */       this.layoutManager = new BasicHorizontalLayoutManager(1);
/*      */     }
/* 1126 */     this.splitPane.setLayout(this.layoutManager);
/* 1127 */     this.layoutManager.updateComponents();
/* 1128 */     this.splitPane.revalidate();
/* 1129 */     this.splitPane.repaint();
/*      */   }
/*      */ 
/*      */   void setKeepHidden(boolean paramBoolean)
/*      */   {
/* 1136 */     this.keepHidden = paramBoolean;
/*      */   }
/*      */ 
/*      */   private boolean getKeepHidden()
/*      */   {
/* 1144 */     return this.keepHidden;
/*      */   }
/*      */ 
/*      */   protected void startDragging()
/*      */   {
/* 1152 */     Component localComponent1 = this.splitPane.getLeftComponent();
/* 1153 */     Component localComponent2 = this.splitPane.getRightComponent();
/*      */ 
/* 1156 */     this.beginDragDividerLocation = getDividerLocation(this.splitPane);
/* 1157 */     this.draggingHW = false;
/*      */     ComponentPeer localComponentPeer;
/* 1158 */     if ((localComponent1 != null) && ((localComponentPeer = localComponent1.getPeer()) != null) && (!(localComponentPeer instanceof LightweightPeer)))
/*      */     {
/* 1160 */       this.draggingHW = true;
/* 1161 */     } else if ((localComponent2 != null) && ((localComponentPeer = localComponent2.getPeer()) != null) && (!(localComponentPeer instanceof LightweightPeer)))
/*      */     {
/* 1163 */       this.draggingHW = true;
/*      */     }
/* 1165 */     if (this.orientation == 1) {
/* 1166 */       setLastDragLocation(this.divider.getBounds().x);
/* 1167 */       this.dividerSize = this.divider.getSize().width;
/* 1168 */       if ((!isContinuousLayout()) && (this.draggingHW)) {
/* 1169 */         this.nonContinuousLayoutDivider.setBounds(getLastDragLocation(), 0, this.dividerSize, this.splitPane.getHeight());
/*      */ 
/* 1172 */         addHeavyweightDivider();
/*      */       }
/*      */     } else {
/* 1175 */       setLastDragLocation(this.divider.getBounds().y);
/* 1176 */       this.dividerSize = this.divider.getSize().height;
/* 1177 */       if ((!isContinuousLayout()) && (this.draggingHW)) {
/* 1178 */         this.nonContinuousLayoutDivider.setBounds(0, getLastDragLocation(), this.splitPane.getWidth(), this.dividerSize);
/*      */ 
/* 1181 */         addHeavyweightDivider();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void dragDividerTo(int paramInt)
/*      */   {
/* 1193 */     if (getLastDragLocation() != paramInt)
/* 1194 */       if (isContinuousLayout()) {
/* 1195 */         this.splitPane.setDividerLocation(paramInt);
/* 1196 */         setLastDragLocation(paramInt);
/*      */       } else {
/* 1198 */         int i = getLastDragLocation();
/*      */ 
/* 1200 */         setLastDragLocation(paramInt);
/*      */         int j;
/* 1201 */         if (this.orientation == 1) {
/* 1202 */           if (this.draggingHW) {
/* 1203 */             this.nonContinuousLayoutDivider.setLocation(getLastDragLocation(), 0);
/*      */           }
/*      */           else {
/* 1206 */             j = this.splitPane.getHeight();
/* 1207 */             this.splitPane.repaint(i, 0, this.dividerSize, j);
/*      */ 
/* 1209 */             this.splitPane.repaint(paramInt, 0, this.dividerSize, j);
/*      */           }
/*      */ 
/*      */         }
/* 1213 */         else if (this.draggingHW) {
/* 1214 */           this.nonContinuousLayoutDivider.setLocation(0, getLastDragLocation());
/*      */         }
/*      */         else {
/* 1217 */           j = this.splitPane.getWidth();
/*      */ 
/* 1219 */           this.splitPane.repaint(0, i, j, this.dividerSize);
/*      */ 
/* 1221 */           this.splitPane.repaint(0, paramInt, j, this.dividerSize);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void finishDraggingTo(int paramInt)
/*      */   {
/* 1235 */     dragDividerTo(paramInt);
/* 1236 */     setLastDragLocation(-1);
/* 1237 */     if (!isContinuousLayout()) {
/* 1238 */       Component localComponent = this.splitPane.getLeftComponent();
/* 1239 */       Rectangle localRectangle = localComponent.getBounds();
/*      */ 
/* 1241 */       if (this.draggingHW) {
/* 1242 */         if (this.orientation == 1) {
/* 1243 */           this.nonContinuousLayoutDivider.setLocation(-this.dividerSize, 0);
/*      */         }
/*      */         else {
/* 1246 */           this.nonContinuousLayoutDivider.setLocation(0, -this.dividerSize);
/*      */         }
/* 1248 */         this.splitPane.remove(this.nonContinuousLayoutDivider);
/*      */       }
/* 1250 */       this.splitPane.setDividerLocation(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected int getDividerBorderSize()
/*      */   {
/* 1266 */     return 1;
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String NEGATIVE_INCREMENT = "negativeIncrement";
/*      */     private static final String POSITIVE_INCREMENT = "positiveIncrement";
/*      */     private static final String SELECT_MIN = "selectMin";
/*      */     private static final String SELECT_MAX = "selectMax";
/*      */     private static final String START_RESIZE = "startResize";
/*      */     private static final String TOGGLE_FOCUS = "toggleFocus";
/*      */     private static final String FOCUS_OUT_FORWARD = "focusOutForward";
/*      */     private static final String FOCUS_OUT_BACKWARD = "focusOutBackward";
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/* 2094 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2098 */       JSplitPane localJSplitPane = (JSplitPane)paramActionEvent.getSource();
/* 2099 */       BasicSplitPaneUI localBasicSplitPaneUI = (BasicSplitPaneUI)BasicLookAndFeel.getUIOfType(localJSplitPane.getUI(), BasicSplitPaneUI.class);
/*      */ 
/* 2102 */       if (localBasicSplitPaneUI == null) {
/* 2103 */         return;
/*      */       }
/* 2105 */       String str = getName();
/* 2106 */       if (str == "negativeIncrement") {
/* 2107 */         if (localBasicSplitPaneUI.dividerKeyboardResize) {
/* 2108 */           localJSplitPane.setDividerLocation(Math.max(0, localBasicSplitPaneUI.getDividerLocation(localJSplitPane) - localBasicSplitPaneUI.getKeyboardMoveIncrement()));
/*      */         }
/*      */ 
/*      */       }
/* 2113 */       else if (str == "positiveIncrement") {
/* 2114 */         if (localBasicSplitPaneUI.dividerKeyboardResize) {
/* 2115 */           localJSplitPane.setDividerLocation(localBasicSplitPaneUI.getDividerLocation(localJSplitPane) + localBasicSplitPaneUI.getKeyboardMoveIncrement());
/*      */         }
/*      */ 
/*      */       }
/* 2120 */       else if (str == "selectMin") {
/* 2121 */         if (localBasicSplitPaneUI.dividerKeyboardResize)
/* 2122 */           localJSplitPane.setDividerLocation(0);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject;
/* 2125 */         if (str == "selectMax") {
/* 2126 */           if (localBasicSplitPaneUI.dividerKeyboardResize) {
/* 2127 */             localObject = localJSplitPane.getInsets();
/* 2128 */             int i = localObject != null ? ((Insets)localObject).bottom : 0;
/* 2129 */             int j = localObject != null ? ((Insets)localObject).right : 0;
/*      */ 
/* 2131 */             if (localBasicSplitPaneUI.orientation == 0) {
/* 2132 */               localJSplitPane.setDividerLocation(localJSplitPane.getHeight() - i);
/*      */             }
/*      */             else
/*      */             {
/* 2136 */               localJSplitPane.setDividerLocation(localJSplitPane.getWidth() - j);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/* 2141 */         else if (str == "startResize") {
/* 2142 */           if (!localBasicSplitPaneUI.dividerKeyboardResize) {
/* 2143 */             localJSplitPane.requestFocus();
/*      */           } else {
/* 2145 */             localObject = (JSplitPane)SwingUtilities.getAncestorOfClass(JSplitPane.class, localJSplitPane);
/*      */ 
/* 2148 */             if (localObject != null) {
/* 2149 */               ((JSplitPane)localObject).requestFocus();
/*      */             }
/*      */           }
/*      */         }
/* 2153 */         else if (str == "toggleFocus") {
/* 2154 */           toggleFocus(localJSplitPane);
/*      */         }
/* 2156 */         else if (str == "focusOutForward") {
/* 2157 */           moveFocus(localJSplitPane, 1);
/*      */         }
/* 2159 */         else if (str == "focusOutBackward")
/* 2160 */           moveFocus(localJSplitPane, -1);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void moveFocus(JSplitPane paramJSplitPane, int paramInt) {
/* 2165 */       Container localContainer = paramJSplitPane.getFocusCycleRootAncestor();
/* 2166 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/* 2167 */       Component localComponent = paramInt > 0 ? localFocusTraversalPolicy.getComponentAfter(localContainer, paramJSplitPane) : localFocusTraversalPolicy.getComponentBefore(localContainer, paramJSplitPane);
/*      */ 
/* 2170 */       HashSet localHashSet = new HashSet();
/* 2171 */       if (paramJSplitPane.isAncestorOf(localComponent)) {
/*      */         do {
/* 2173 */           localHashSet.add(localComponent);
/* 2174 */           localContainer = localComponent.getFocusCycleRootAncestor();
/* 2175 */           localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/* 2176 */           localComponent = paramInt > 0 ? localFocusTraversalPolicy.getComponentAfter(localContainer, localComponent) : localFocusTraversalPolicy.getComponentBefore(localContainer, localComponent);
/*      */         }
/*      */ 
/* 2179 */         while ((paramJSplitPane.isAncestorOf(localComponent)) && (!localHashSet.contains(localComponent)));
/*      */       }
/*      */ 
/* 2182 */       if ((localComponent != null) && (!paramJSplitPane.isAncestorOf(localComponent)))
/* 2183 */         localComponent.requestFocus();
/*      */     }
/*      */ 
/*      */     private void toggleFocus(JSplitPane paramJSplitPane)
/*      */     {
/* 2188 */       Component localComponent1 = paramJSplitPane.getLeftComponent();
/* 2189 */       Component localComponent2 = paramJSplitPane.getRightComponent();
/*      */ 
/* 2191 */       KeyboardFocusManager localKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */ 
/* 2193 */       Component localComponent3 = localKeyboardFocusManager.getFocusOwner();
/* 2194 */       Component localComponent4 = getNextSide(paramJSplitPane, localComponent3);
/* 2195 */       if (localComponent4 != null)
/*      */       {
/* 2198 */         if ((localComponent3 != null) && (((SwingUtilities.isDescendingFrom(localComponent3, localComponent1)) && (SwingUtilities.isDescendingFrom(localComponent4, localComponent1))) || ((SwingUtilities.isDescendingFrom(localComponent3, localComponent2)) && (SwingUtilities.isDescendingFrom(localComponent4, localComponent2)))))
/*      */         {
/* 2203 */           return;
/*      */         }
/* 2205 */         SwingUtilities2.compositeRequestFocus(localComponent4);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Component getNextSide(JSplitPane paramJSplitPane, Component paramComponent) {
/* 2210 */       Component localComponent1 = paramJSplitPane.getLeftComponent();
/* 2211 */       Component localComponent2 = paramJSplitPane.getRightComponent();
/*      */       Component localComponent3;
/* 2213 */       if ((paramComponent != null) && (SwingUtilities.isDescendingFrom(paramComponent, localComponent1)) && (localComponent2 != null))
/*      */       {
/* 2215 */         localComponent3 = getFirstAvailableComponent(localComponent2);
/* 2216 */         if (localComponent3 != null) {
/* 2217 */           return localComponent3;
/*      */         }
/*      */       }
/* 2220 */       JSplitPane localJSplitPane = (JSplitPane)SwingUtilities.getAncestorOfClass(JSplitPane.class, paramJSplitPane);
/* 2221 */       if (localJSplitPane != null)
/*      */       {
/* 2223 */         localComponent3 = getNextSide(localJSplitPane, paramComponent);
/*      */       } else {
/* 2225 */         localComponent3 = getFirstAvailableComponent(localComponent1);
/* 2226 */         if (localComponent3 == null) {
/* 2227 */           localComponent3 = getFirstAvailableComponent(localComponent2);
/*      */         }
/*      */       }
/* 2230 */       return localComponent3;
/*      */     }
/*      */ 
/*      */     private Component getFirstAvailableComponent(Component paramComponent) {
/* 2234 */       if ((paramComponent != null) && ((paramComponent instanceof JSplitPane))) {
/* 2235 */         JSplitPane localJSplitPane = (JSplitPane)paramComponent;
/* 2236 */         Component localComponent = getFirstAvailableComponent(localJSplitPane.getLeftComponent());
/* 2237 */         if (localComponent != null)
/* 2238 */           paramComponent = localComponent;
/*      */         else {
/* 2240 */           paramComponent = getFirstAvailableComponent(localJSplitPane.getRightComponent());
/*      */         }
/*      */       }
/* 2243 */       return paramComponent;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class BasicHorizontalLayoutManager
/*      */     implements LayoutManager2
/*      */   {
/*      */     protected int[] sizes;
/*      */     protected Component[] components;
/*      */     private int lastSplitPaneSize;
/*      */     private boolean doReset;
/*      */     private int axis;
/*      */ 
/*      */     BasicHorizontalLayoutManager()
/*      */     {
/* 1288 */       this(0);
/*      */     }
/*      */ 
/*      */     BasicHorizontalLayoutManager(int arg2)
/*      */     {
/*      */       int i;
/* 1292 */       this.axis = i;
/* 1293 */       this.components = new Component[3];
/*      */        tmp40_39 = (this.components[2] =  = null); this.components[1] = tmp40_39; this.components[0] = tmp40_39;
/* 1295 */       this.sizes = new int[3];
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/* 1306 */       Dimension localDimension1 = paramContainer.getSize();
/*      */ 
/* 1311 */       if ((localDimension1.height <= 0) || (localDimension1.width <= 0)) {
/* 1312 */         this.lastSplitPaneSize = 0;
/* 1313 */         return;
/*      */       }
/*      */ 
/* 1316 */       int i = BasicSplitPaneUI.this.splitPane.getDividerLocation();
/* 1317 */       Insets localInsets = BasicSplitPaneUI.this.splitPane.getInsets();
/* 1318 */       int j = getAvailableSize(localDimension1, localInsets);
/*      */ 
/* 1320 */       int k = getSizeForPrimaryAxis(localDimension1);
/* 1321 */       int m = BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane);
/* 1322 */       int n = getSizeForPrimaryAxis(localInsets, true);
/* 1323 */       Dimension localDimension2 = this.components[2] == null ? null : this.components[2].getPreferredSize();
/*      */ 
/* 1326 */       if (((this.doReset) && (!BasicSplitPaneUI.this.dividerLocationIsSet)) || (i < 0)) {
/* 1327 */         resetToPreferredSizes(j);
/*      */       }
/* 1329 */       else if ((this.lastSplitPaneSize <= 0) || (j == this.lastSplitPaneSize) || (!BasicSplitPaneUI.this.painted) || ((localDimension2 != null) && (getSizeForPrimaryAxis(localDimension2) != this.sizes[2])))
/*      */       {
/* 1333 */         if (localDimension2 != null) {
/* 1334 */           this.sizes[2] = getSizeForPrimaryAxis(localDimension2);
/*      */         }
/*      */         else {
/* 1337 */           this.sizes[2] = 0;
/*      */         }
/* 1339 */         setDividerLocation(i - n, j);
/* 1340 */         BasicSplitPaneUI.this.dividerLocationIsSet = false;
/*      */       }
/* 1342 */       else if (j != this.lastSplitPaneSize) {
/* 1343 */         distributeSpace(j - this.lastSplitPaneSize, BasicSplitPaneUI.this.getKeepHidden());
/*      */       }
/*      */ 
/* 1346 */       this.doReset = false;
/* 1347 */       BasicSplitPaneUI.this.dividerLocationIsSet = false;
/* 1348 */       this.lastSplitPaneSize = j;
/*      */ 
/* 1351 */       int i1 = getInitialLocation(localInsets);
/* 1352 */       int i2 = 0;
/*      */ 
/* 1354 */       while (i2 < 3) {
/* 1355 */         if ((this.components[i2] != null) && (this.components[i2].isVisible()))
/*      */         {
/* 1357 */           setComponentToSize(this.components[i2], this.sizes[i2], i1, localInsets, localDimension1);
/*      */ 
/* 1359 */           i1 += this.sizes[i2];
/*      */         }
/* 1361 */         switch (i2) {
/*      */         case 0:
/* 1363 */           i2 = 2;
/* 1364 */           break;
/*      */         case 2:
/* 1366 */           i2 = 1;
/* 1367 */           break;
/*      */         case 1:
/* 1369 */           i2 = 3;
/*      */         }
/*      */       }
/*      */ 
/* 1373 */       if (BasicSplitPaneUI.this.painted)
/*      */       {
/* 1378 */         int i3 = BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane);
/*      */ 
/* 1380 */         if (i3 != i - n) {
/* 1381 */           int i4 = BasicSplitPaneUI.this.splitPane.getLastDividerLocation();
/*      */ 
/* 1383 */           BasicSplitPaneUI.this.ignoreDividerLocationChange = true;
/*      */           try {
/* 1385 */             BasicSplitPaneUI.this.splitPane.setDividerLocation(i3);
/*      */ 
/* 1394 */             BasicSplitPaneUI.this.splitPane.setLastDividerLocation(i4);
/*      */           } finally {
/* 1396 */             BasicSplitPaneUI.this.ignoreDividerLocationChange = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/* 1409 */       int i = 1;
/*      */ 
/* 1411 */       if (paramString != null) {
/* 1412 */         if (paramString.equals("divider"))
/*      */         {
/* 1414 */           this.components[2] = paramComponent;
/* 1415 */           this.sizes[2] = getSizeForPrimaryAxis(paramComponent.getPreferredSize());
/*      */         }
/* 1417 */         else if ((paramString.equals("left")) || (paramString.equals("top")))
/*      */         {
/* 1419 */           this.components[0] = paramComponent;
/* 1420 */           this.sizes[0] = 0;
/* 1421 */         } else if ((paramString.equals("right")) || (paramString.equals("bottom")))
/*      */         {
/* 1423 */           this.components[1] = paramComponent;
/* 1424 */           this.sizes[1] = 0;
/* 1425 */         } else if (!paramString.equals("nonContinuousDivider"))
/*      */         {
/* 1427 */           i = 0;
/*      */         }
/*      */       } else i = 0;
/*      */ 
/* 1431 */       if (i == 0) {
/* 1432 */         throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + paramString);
/*      */       }
/*      */ 
/* 1435 */       this.doReset = true;
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/* 1445 */       int i = 0;
/* 1446 */       int j = 0;
/* 1447 */       Insets localInsets = BasicSplitPaneUI.this.splitPane.getInsets();
/*      */ 
/* 1449 */       for (int k = 0; k < 3; k++) {
/* 1450 */         if (this.components[k] != null) {
/* 1451 */           Dimension localDimension = this.components[k].getMinimumSize();
/* 1452 */           int m = getSizeForSecondaryAxis(localDimension);
/*      */ 
/* 1454 */           i += getSizeForPrimaryAxis(localDimension);
/* 1455 */           if (m > j)
/* 1456 */             j = m;
/*      */         }
/*      */       }
/* 1459 */       if (localInsets != null) {
/* 1460 */         i += getSizeForPrimaryAxis(localInsets, true) + getSizeForPrimaryAxis(localInsets, false);
/*      */ 
/* 1462 */         j += getSizeForSecondaryAxis(localInsets, true) + getSizeForSecondaryAxis(localInsets, false);
/*      */       }
/*      */ 
/* 1465 */       if (this.axis == 0) {
/* 1466 */         return new Dimension(i, j);
/*      */       }
/* 1468 */       return new Dimension(j, i);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/* 1478 */       int i = 0;
/* 1479 */       int j = 0;
/* 1480 */       Insets localInsets = BasicSplitPaneUI.this.splitPane.getInsets();
/*      */ 
/* 1482 */       for (int k = 0; k < 3; k++) {
/* 1483 */         if (this.components[k] != null) {
/* 1484 */           Dimension localDimension = this.components[k].getPreferredSize();
/*      */ 
/* 1486 */           int m = getSizeForSecondaryAxis(localDimension);
/*      */ 
/* 1488 */           i += getSizeForPrimaryAxis(localDimension);
/* 1489 */           if (m > j)
/* 1490 */             j = m;
/*      */         }
/*      */       }
/* 1493 */       if (localInsets != null) {
/* 1494 */         i += getSizeForPrimaryAxis(localInsets, true) + getSizeForPrimaryAxis(localInsets, false);
/*      */ 
/* 1496 */         j += getSizeForSecondaryAxis(localInsets, true) + getSizeForSecondaryAxis(localInsets, false);
/*      */       }
/*      */ 
/* 1499 */       if (this.axis == 0) {
/* 1500 */         return new Dimension(i, j);
/*      */       }
/* 1502 */       return new Dimension(j, i);
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/* 1510 */       for (int i = 0; i < 3; i++)
/* 1511 */         if (this.components[i] == paramComponent) {
/* 1512 */           this.components[i] = null;
/* 1513 */           this.sizes[i] = 0;
/* 1514 */           this.doReset = true;
/*      */         }
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(Component paramComponent, Object paramObject)
/*      */     {
/* 1532 */       if ((paramObject == null) || ((paramObject instanceof String)))
/* 1533 */         addLayoutComponent((String)paramObject, paramComponent);
/*      */       else
/* 1535 */         throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
/*      */     }
/*      */ 
/*      */     public float getLayoutAlignmentX(Container paramContainer)
/*      */     {
/* 1550 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     public float getLayoutAlignmentY(Container paramContainer)
/*      */     {
/* 1562 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     public void invalidateLayout(Container paramContainer)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension maximumLayoutSize(Container paramContainer)
/*      */     {
/* 1580 */       return new Dimension(2147483647, 2147483647);
/*      */     }
/*      */ 
/*      */     public void resetToPreferredSizes()
/*      */     {
/* 1593 */       this.doReset = true;
/*      */     }
/*      */ 
/*      */     protected void resetSizeAt(int paramInt)
/*      */     {
/* 1600 */       this.sizes[paramInt] = 0;
/* 1601 */       this.doReset = true;
/*      */     }
/*      */ 
/*      */     protected void setSizes(int[] paramArrayOfInt)
/*      */     {
/* 1609 */       System.arraycopy(paramArrayOfInt, 0, this.sizes, 0, 3);
/*      */     }
/*      */ 
/*      */     protected int[] getSizes()
/*      */     {
/* 1617 */       int[] arrayOfInt = new int[3];
/*      */ 
/* 1619 */       System.arraycopy(this.sizes, 0, arrayOfInt, 0, 3);
/* 1620 */       return arrayOfInt;
/*      */     }
/*      */ 
/*      */     protected int getPreferredSizeOfComponent(Component paramComponent)
/*      */     {
/* 1628 */       return getSizeForPrimaryAxis(paramComponent.getPreferredSize());
/*      */     }
/*      */ 
/*      */     int getMinimumSizeOfComponent(Component paramComponent)
/*      */     {
/* 1636 */       return getSizeForPrimaryAxis(paramComponent.getMinimumSize());
/*      */     }
/*      */ 
/*      */     protected int getSizeOfComponent(Component paramComponent)
/*      */     {
/* 1644 */       return getSizeForPrimaryAxis(paramComponent.getSize());
/*      */     }
/*      */ 
/*      */     protected int getAvailableSize(Dimension paramDimension, Insets paramInsets)
/*      */     {
/* 1654 */       if (paramInsets == null)
/* 1655 */         return getSizeForPrimaryAxis(paramDimension);
/* 1656 */       return getSizeForPrimaryAxis(paramDimension) - (getSizeForPrimaryAxis(paramInsets, true) + getSizeForPrimaryAxis(paramInsets, false));
/*      */     }
/*      */ 
/*      */     protected int getInitialLocation(Insets paramInsets)
/*      */     {
/* 1667 */       if (paramInsets != null)
/* 1668 */         return getSizeForPrimaryAxis(paramInsets, true);
/* 1669 */       return 0;
/*      */     }
/*      */ 
/*      */     protected void setComponentToSize(Component paramComponent, int paramInt1, int paramInt2, Insets paramInsets, Dimension paramDimension)
/*      */     {
/* 1681 */       if (paramInsets != null) {
/* 1682 */         if (this.axis == 0) {
/* 1683 */           paramComponent.setBounds(paramInt2, paramInsets.top, paramInt1, paramDimension.height - (paramInsets.top + paramInsets.bottom));
/*      */         }
/*      */         else
/*      */         {
/* 1688 */           paramComponent.setBounds(paramInsets.left, paramInt2, paramDimension.width - (paramInsets.left + paramInsets.right), paramInt1);
/*      */         }
/*      */ 
/*      */       }
/* 1693 */       else if (this.axis == 0) {
/* 1694 */         paramComponent.setBounds(paramInt2, 0, paramInt1, paramDimension.height);
/*      */       }
/*      */       else
/* 1697 */         paramComponent.setBounds(0, paramInt2, paramDimension.width, paramInt1);
/*      */     }
/*      */ 
/*      */     int getSizeForPrimaryAxis(Dimension paramDimension)
/*      */     {
/* 1706 */       if (this.axis == 0) {
/* 1707 */         return paramDimension.width;
/*      */       }
/* 1709 */       return paramDimension.height;
/*      */     }
/*      */ 
/*      */     int getSizeForSecondaryAxis(Dimension paramDimension)
/*      */     {
/* 1716 */       if (this.axis == 0) {
/* 1717 */         return paramDimension.height;
/*      */       }
/* 1719 */       return paramDimension.width;
/*      */     }
/*      */ 
/*      */     int getSizeForPrimaryAxis(Insets paramInsets, boolean paramBoolean)
/*      */     {
/* 1732 */       if (this.axis == 0) {
/* 1733 */         if (paramBoolean) {
/* 1734 */           return paramInsets.left;
/*      */         }
/* 1736 */         return paramInsets.right;
/*      */       }
/* 1738 */       if (paramBoolean) {
/* 1739 */         return paramInsets.top;
/*      */       }
/* 1741 */       return paramInsets.bottom;
/*      */     }
/*      */ 
/*      */     int getSizeForSecondaryAxis(Insets paramInsets, boolean paramBoolean)
/*      */     {
/* 1754 */       if (this.axis == 0) {
/* 1755 */         if (paramBoolean) {
/* 1756 */           return paramInsets.top;
/*      */         }
/* 1758 */         return paramInsets.bottom;
/*      */       }
/* 1760 */       if (paramBoolean) {
/* 1761 */         return paramInsets.left;
/*      */       }
/* 1763 */       return paramInsets.right;
/*      */     }
/*      */ 
/*      */     protected void updateComponents()
/*      */     {
/* 1774 */       Component localComponent1 = BasicSplitPaneUI.this.splitPane.getLeftComponent();
/* 1775 */       if (this.components[0] != localComponent1) {
/* 1776 */         this.components[0] = localComponent1;
/* 1777 */         if (localComponent1 == null)
/* 1778 */           this.sizes[0] = 0;
/*      */         else {
/* 1780 */           this.sizes[0] = -1;
/*      */         }
/*      */       }
/*      */ 
/* 1784 */       localComponent1 = BasicSplitPaneUI.this.splitPane.getRightComponent();
/* 1785 */       if (this.components[1] != localComponent1) {
/* 1786 */         this.components[1] = localComponent1;
/* 1787 */         if (localComponent1 == null)
/* 1788 */           this.sizes[1] = 0;
/*      */         else {
/* 1790 */           this.sizes[1] = -1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1795 */       Component[] arrayOfComponent = BasicSplitPaneUI.this.splitPane.getComponents();
/* 1796 */       Component localComponent2 = this.components[2];
/*      */ 
/* 1798 */       this.components[2] = null;
/* 1799 */       for (int i = arrayOfComponent.length - 1; i >= 0; i--) {
/* 1800 */         if ((arrayOfComponent[i] != this.components[0]) && (arrayOfComponent[i] != this.components[1]) && (arrayOfComponent[i] != BasicSplitPaneUI.this.nonContinuousLayoutDivider))
/*      */         {
/* 1803 */           if (localComponent2 != arrayOfComponent[i]) {
/* 1804 */             this.components[2] = arrayOfComponent[i]; break;
/*      */           }
/* 1806 */           this.components[2] = localComponent2;
/*      */ 
/* 1808 */           break;
/*      */         }
/*      */       }
/* 1811 */       if (this.components[2] == null) {
/* 1812 */         this.sizes[2] = 0;
/*      */       }
/*      */       else
/* 1815 */         this.sizes[2] = getSizeForPrimaryAxis(this.components[2].getPreferredSize());
/*      */     }
/*      */ 
/*      */     void setDividerLocation(int paramInt1, int paramInt2)
/*      */     {
/* 1824 */       int i = (this.components[0] != null) && (this.components[0].isVisible()) ? 1 : 0;
/*      */ 
/* 1826 */       int j = (this.components[1] != null) && (this.components[1].isVisible()) ? 1 : 0;
/*      */ 
/* 1828 */       int k = (this.components[2] != null) && (this.components[2].isVisible()) ? 1 : 0;
/*      */ 
/* 1830 */       int m = paramInt2;
/*      */ 
/* 1832 */       if (k != 0) {
/* 1833 */         m -= this.sizes[2];
/*      */       }
/* 1835 */       paramInt1 = Math.max(0, Math.min(paramInt1, m));
/* 1836 */       if (i != 0) {
/* 1837 */         if (j != 0) {
/* 1838 */           this.sizes[0] = paramInt1;
/* 1839 */           this.sizes[1] = (m - paramInt1);
/*      */         }
/*      */         else {
/* 1842 */           this.sizes[0] = m;
/* 1843 */           this.sizes[1] = 0;
/*      */         }
/*      */       }
/* 1846 */       else if (j != 0) {
/* 1847 */         this.sizes[1] = m;
/* 1848 */         this.sizes[0] = 0;
/*      */       }
/*      */     }
/*      */ 
/*      */     int[] getPreferredSizes()
/*      */     {
/* 1856 */       int[] arrayOfInt = new int[3];
/*      */ 
/* 1858 */       for (int i = 0; i < 3; i++) {
/* 1859 */         if ((this.components[i] != null) && (this.components[i].isVisible()))
/*      */         {
/* 1861 */           arrayOfInt[i] = getPreferredSizeOfComponent(this.components[i]);
/*      */         }
/*      */         else
/*      */         {
/* 1865 */           arrayOfInt[i] = -1;
/*      */         }
/*      */       }
/* 1868 */       return arrayOfInt;
/*      */     }
/*      */ 
/*      */     int[] getMinimumSizes()
/*      */     {
/* 1875 */       int[] arrayOfInt = new int[3];
/*      */ 
/* 1877 */       for (int i = 0; i < 2; i++) {
/* 1878 */         if ((this.components[i] != null) && (this.components[i].isVisible()))
/*      */         {
/* 1880 */           arrayOfInt[i] = getMinimumSizeOfComponent(this.components[i]);
/*      */         }
/*      */         else
/*      */         {
/* 1884 */           arrayOfInt[i] = -1;
/*      */         }
/*      */       }
/* 1887 */       arrayOfInt[2] = (this.components[2] != null ? getMinimumSizeOfComponent(this.components[2]) : -1);
/*      */ 
/* 1889 */       return arrayOfInt;
/*      */     }
/*      */ 
/*      */     void resetToPreferredSizes(int paramInt)
/*      */     {
/* 1898 */       int[] arrayOfInt = getPreferredSizes();
/* 1899 */       int i = 0;
/*      */ 
/* 1901 */       for (int j = 0; j < 3; j++) {
/* 1902 */         if (arrayOfInt[j] != -1) {
/* 1903 */           i += arrayOfInt[j];
/*      */         }
/*      */       }
/* 1906 */       if (i > paramInt) {
/* 1907 */         arrayOfInt = getMinimumSizes();
/*      */ 
/* 1909 */         i = 0;
/* 1910 */         for (j = 0; j < 3; j++) {
/* 1911 */           if (arrayOfInt[j] != -1) {
/* 1912 */             i += arrayOfInt[j];
/*      */           }
/*      */         }
/*      */       }
/* 1916 */       setSizes(arrayOfInt);
/* 1917 */       distributeSpace(paramInt - i, false);
/*      */     }
/*      */ 
/*      */     void distributeSpace(int paramInt, boolean paramBoolean)
/*      */     {
/* 1929 */       int i = (this.components[0] != null) && (this.components[0].isVisible()) ? 1 : 0;
/*      */ 
/* 1931 */       int j = (this.components[1] != null) && (this.components[1].isVisible()) ? 1 : 0;
/*      */ 
/* 1934 */       if (paramBoolean) {
/* 1935 */         if ((i != 0) && (getSizeForPrimaryAxis(this.components[0].getSize()) == 0))
/*      */         {
/* 1937 */           i = 0;
/* 1938 */           if ((j != 0) && (getSizeForPrimaryAxis(this.components[1].getSize()) == 0))
/*      */           {
/* 1941 */             i = 1;
/*      */           }
/*      */         }
/* 1944 */         else if ((j != 0) && (getSizeForPrimaryAxis(this.components[1].getSize()) == 0))
/*      */         {
/* 1946 */           j = 0;
/*      */         }
/*      */       }
/* 1949 */       if ((i != 0) && (j != 0)) {
/* 1950 */         double d = BasicSplitPaneUI.this.splitPane.getResizeWeight();
/* 1951 */         int k = (int)(d * paramInt);
/* 1952 */         int m = paramInt - k;
/*      */ 
/* 1954 */         this.sizes[0] += k;
/* 1955 */         this.sizes[1] += m;
/*      */ 
/* 1957 */         int n = getMinimumSizeOfComponent(this.components[0]);
/* 1958 */         int i1 = getMinimumSizeOfComponent(this.components[1]);
/* 1959 */         int i2 = this.sizes[0] >= n ? 1 : 0;
/* 1960 */         int i3 = this.sizes[1] >= i1 ? 1 : 0;
/*      */ 
/* 1962 */         if ((i2 == 0) && (i3 == 0)) {
/* 1963 */           if (this.sizes[0] < 0) {
/* 1964 */             this.sizes[1] += this.sizes[0];
/* 1965 */             this.sizes[0] = 0;
/*      */           }
/* 1967 */           else if (this.sizes[1] < 0) {
/* 1968 */             this.sizes[0] += this.sizes[1];
/* 1969 */             this.sizes[1] = 0;
/*      */           }
/*      */         }
/* 1972 */         else if (i2 == 0) {
/* 1973 */           if (this.sizes[1] - (n - this.sizes[0]) < i1)
/*      */           {
/* 1975 */             if (this.sizes[0] < 0) {
/* 1976 */               this.sizes[1] += this.sizes[0];
/* 1977 */               this.sizes[0] = 0;
/*      */             }
/*      */           }
/*      */           else {
/* 1981 */             this.sizes[1] -= n - this.sizes[0];
/* 1982 */             this.sizes[0] = n;
/*      */           }
/*      */         }
/* 1985 */         else if (i3 == 0) {
/* 1986 */           if (this.sizes[0] - (i1 - this.sizes[1]) < n)
/*      */           {
/* 1988 */             if (this.sizes[1] < 0) {
/* 1989 */               this.sizes[0] += this.sizes[1];
/* 1990 */               this.sizes[1] = 0;
/*      */             }
/*      */           }
/*      */           else {
/* 1994 */             this.sizes[0] -= i1 - this.sizes[1];
/* 1995 */             this.sizes[1] = i1;
/*      */           }
/*      */         }
/* 1998 */         if (this.sizes[0] < 0) {
/* 1999 */           this.sizes[0] = 0;
/*      */         }
/* 2001 */         if (this.sizes[1] < 0) {
/* 2002 */           this.sizes[1] = 0;
/*      */         }
/*      */       }
/* 2005 */       else if (i != 0) {
/* 2006 */         this.sizes[0] = Math.max(0, this.sizes[0] + paramInt);
/*      */       }
/* 2008 */       else if (j != 0) {
/* 2009 */         this.sizes[1] = Math.max(0, this.sizes[1] + paramInt);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class BasicVerticalLayoutManager extends BasicSplitPaneUI.BasicHorizontalLayoutManager
/*      */   {
/*      */     public BasicVerticalLayoutManager()
/*      */     {
/* 2024 */       super(1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FocusHandler extends FocusAdapter
/*      */   {
/*      */     public FocusHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/*  715 */       BasicSplitPaneUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/*  719 */       BasicSplitPaneUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements FocusListener, PropertyChangeListener
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 2039 */       if (paramPropertyChangeEvent.getSource() == BasicSplitPaneUI.this.splitPane) {
/* 2040 */         String str = paramPropertyChangeEvent.getPropertyName();
/*      */ 
/* 2042 */         if (str == "orientation") {
/* 2043 */           BasicSplitPaneUI.this.orientation = BasicSplitPaneUI.this.splitPane.getOrientation();
/* 2044 */           BasicSplitPaneUI.this.resetLayoutManager();
/* 2045 */         } else if (str == "continuousLayout") {
/* 2046 */           BasicSplitPaneUI.this.setContinuousLayout(BasicSplitPaneUI.this.splitPane.isContinuousLayout());
/* 2047 */           if (!BasicSplitPaneUI.this.isContinuousLayout()) {
/* 2048 */             if (BasicSplitPaneUI.this.nonContinuousLayoutDivider == null) {
/* 2049 */               BasicSplitPaneUI.this.setNonContinuousLayoutDivider(BasicSplitPaneUI.this.createDefaultNonContinuousLayoutDivider(), true);
/*      */             }
/* 2052 */             else if (BasicSplitPaneUI.this.nonContinuousLayoutDivider.getParent() == null)
/*      */             {
/* 2054 */               BasicSplitPaneUI.this.setNonContinuousLayoutDivider(BasicSplitPaneUI.this.nonContinuousLayoutDivider, true);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/* 2059 */         else if (str == "dividerSize") {
/* 2060 */           BasicSplitPaneUI.this.divider.setDividerSize(BasicSplitPaneUI.this.splitPane.getDividerSize());
/* 2061 */           BasicSplitPaneUI.this.dividerSize = BasicSplitPaneUI.this.divider.getDividerSize();
/* 2062 */           BasicSplitPaneUI.this.splitPane.revalidate();
/* 2063 */           BasicSplitPaneUI.this.splitPane.repaint();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 2072 */       BasicSplitPaneUI.this.dividerKeyboardResize = true;
/* 2073 */       BasicSplitPaneUI.this.splitPane.repaint();
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 2077 */       BasicSplitPaneUI.this.dividerKeyboardResize = false;
/* 2078 */       BasicSplitPaneUI.this.splitPane.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyboardDownRightHandler
/*      */     implements ActionListener
/*      */   {
/*      */     public KeyboardDownRightHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  751 */       if (BasicSplitPaneUI.this.dividerKeyboardResize)
/*  752 */         BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane) + BasicSplitPaneUI.this.getKeyboardMoveIncrement());
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyboardEndHandler
/*      */     implements ActionListener
/*      */   {
/*      */     public KeyboardEndHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  786 */       if (BasicSplitPaneUI.this.dividerKeyboardResize) {
/*  787 */         Insets localInsets = BasicSplitPaneUI.this.splitPane.getInsets();
/*  788 */         int i = localInsets != null ? localInsets.bottom : 0;
/*  789 */         int j = localInsets != null ? localInsets.right : 0;
/*      */ 
/*  791 */         if (BasicSplitPaneUI.this.orientation == 0) {
/*  792 */           BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.splitPane.getHeight() - i);
/*      */         }
/*      */         else
/*      */         {
/*  796 */           BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.splitPane.getWidth() - j);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyboardHomeHandler
/*      */     implements ActionListener
/*      */   {
/*      */     public KeyboardHomeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  769 */       if (BasicSplitPaneUI.this.dividerKeyboardResize)
/*  770 */         BasicSplitPaneUI.this.splitPane.setDividerLocation(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyboardResizeToggleHandler
/*      */     implements ActionListener
/*      */   {
/*      */     public KeyboardResizeToggleHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  814 */       if (!BasicSplitPaneUI.this.dividerKeyboardResize)
/*  815 */         BasicSplitPaneUI.this.splitPane.requestFocus();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyboardUpLeftHandler
/*      */     implements ActionListener
/*      */   {
/*      */     public KeyboardUpLeftHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  734 */       if (BasicSplitPaneUI.this.dividerKeyboardResize)
/*  735 */         BasicSplitPaneUI.this.splitPane.setDividerLocation(Math.max(0, BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane) - BasicSplitPaneUI.this.getKeyboardMoveIncrement()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public class PropertyHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public PropertyHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  697 */       BasicSplitPaneUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicSplitPaneUI
 * JD-Core Version:    0.6.2
 */