/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentInputMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TabbedPaneUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.View;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicTabbedPaneUI extends TabbedPaneUI
/*      */   implements SwingConstants
/*      */ {
/*      */   protected JTabbedPane tabPane;
/*      */   protected Color highlight;
/*      */   protected Color lightHighlight;
/*      */   protected Color shadow;
/*      */   protected Color darkShadow;
/*      */   protected Color focus;
/*      */   private Color selectedColor;
/*      */   protected int textIconGap;
/*      */   protected int tabRunOverlay;
/*      */   protected Insets tabInsets;
/*      */   protected Insets selectedTabPadInsets;
/*      */   protected Insets tabAreaInsets;
/*      */   protected Insets contentBorderInsets;
/*      */   private boolean tabsOverlapBorder;
/*      */   private boolean tabsOpaque;
/*      */   private boolean contentOpaque;
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
/*      */   protected int[] tabRuns;
/*      */   protected int runCount;
/*      */   protected int selectedRun;
/*      */   protected Rectangle[] rects;
/*      */   protected int maxTabHeight;
/*      */   protected int maxTabWidth;
/*      */   protected ChangeListener tabChangeListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected MouseListener mouseListener;
/*      */   protected FocusListener focusListener;
/*      */   private Insets currentPadInsets;
/*      */   private Insets currentTabAreaInsets;
/*      */   private Component visibleComponent;
/*      */   private Vector<View> htmlViews;
/*      */   private Hashtable<Integer, Integer> mnemonicToIndexMap;
/*      */   private InputMap mnemonicInputMap;
/*      */   private ScrollableTabSupport tabScroller;
/*      */   private TabContainer tabContainer;
/*      */   protected transient Rectangle calcRect;
/*      */   private int focusIndex;
/*      */   private Handler handler;
/*      */   private int rolloverTabIndex;
/*      */   private boolean isRunsDirty;
/*      */   private boolean calculatedBaseline;
/*      */   private int baseline;
/*  917 */   private static int[] xCropLen = { 1, 1, 0, 0, 1, 1, 2, 2 };
/*  918 */   private static int[] yCropLen = { 0, 3, 3, 6, 6, 9, 9, 12 };
/*      */   private static final int CROP_SEGMENT = 12;
/*      */ 
/*      */   public BasicTabbedPaneUI()
/*      */   {
/*   77 */     this.tabsOpaque = true;
/*   78 */     this.contentOpaque = true;
/*      */ 
/*  124 */     this.tabRuns = new int[10];
/*  125 */     this.runCount = 0;
/*  126 */     this.selectedRun = -1;
/*  127 */     this.rects = new Rectangle[0];
/*      */ 
/*  140 */     this.currentPadInsets = new Insets(0, 0, 0, 0);
/*  141 */     this.currentTabAreaInsets = new Insets(0, 0, 0, 0);
/*      */ 
/*  164 */     this.calcRect = new Rectangle(0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  194 */     return new BasicTabbedPaneUI();
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  198 */     paramLazyActionMap.put(new Actions("navigateNext"));
/*  199 */     paramLazyActionMap.put(new Actions("navigatePrevious"));
/*  200 */     paramLazyActionMap.put(new Actions("navigateRight"));
/*  201 */     paramLazyActionMap.put(new Actions("navigateLeft"));
/*  202 */     paramLazyActionMap.put(new Actions("navigateUp"));
/*  203 */     paramLazyActionMap.put(new Actions("navigateDown"));
/*  204 */     paramLazyActionMap.put(new Actions("navigatePageUp"));
/*  205 */     paramLazyActionMap.put(new Actions("navigatePageDown"));
/*  206 */     paramLazyActionMap.put(new Actions("requestFocus"));
/*  207 */     paramLazyActionMap.put(new Actions("requestFocusForVisibleComponent"));
/*  208 */     paramLazyActionMap.put(new Actions("setSelectedIndex"));
/*  209 */     paramLazyActionMap.put(new Actions("selectTabWithFocus"));
/*  210 */     paramLazyActionMap.put(new Actions("scrollTabsForwardAction"));
/*  211 */     paramLazyActionMap.put(new Actions("scrollTabsBackwardAction"));
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  217 */     this.tabPane = ((JTabbedPane)paramJComponent);
/*      */ 
/*  219 */     this.calculatedBaseline = false;
/*  220 */     this.rolloverTabIndex = -1;
/*  221 */     this.focusIndex = -1;
/*  222 */     paramJComponent.setLayout(createLayoutManager());
/*  223 */     installComponents();
/*  224 */     installDefaults();
/*  225 */     installListeners();
/*  226 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  230 */     uninstallKeyboardActions();
/*  231 */     uninstallListeners();
/*  232 */     uninstallDefaults();
/*  233 */     uninstallComponents();
/*  234 */     paramJComponent.setLayout(null);
/*      */ 
/*  236 */     this.tabPane = null;
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager()
/*      */   {
/*  250 */     if (this.tabPane.getTabLayoutPolicy() == 1) {
/*  251 */       return new TabbedPaneScrollLayout(null);
/*      */     }
/*  253 */     return new TabbedPaneLayout();
/*      */   }
/*      */ 
/*      */   private boolean scrollableTabLayoutEnabled()
/*      */   {
/*  263 */     return this.tabPane.getLayout() instanceof TabbedPaneScrollLayout;
/*      */   }
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*  273 */     if ((scrollableTabLayoutEnabled()) && 
/*  274 */       (this.tabScroller == null)) {
/*  275 */       this.tabScroller = new ScrollableTabSupport(this.tabPane.getTabPlacement());
/*  276 */       this.tabPane.add(this.tabScroller.viewport);
/*      */     }
/*      */ 
/*  279 */     installTabContainer();
/*      */   }
/*      */ 
/*      */   private void installTabContainer() {
/*  283 */     for (int i = 0; i < this.tabPane.getTabCount(); i++) {
/*  284 */       Component localComponent = this.tabPane.getTabComponentAt(i);
/*  285 */       if (localComponent != null) {
/*  286 */         if (this.tabContainer == null) {
/*  287 */           this.tabContainer = new TabContainer();
/*      */         }
/*  289 */         this.tabContainer.add(localComponent);
/*      */       }
/*      */     }
/*  292 */     if (this.tabContainer == null) {
/*  293 */       return;
/*      */     }
/*  295 */     if (scrollableTabLayoutEnabled())
/*  296 */       this.tabScroller.tabPanel.add(this.tabContainer);
/*      */     else
/*  298 */       this.tabPane.add(this.tabContainer);
/*      */   }
/*      */ 
/*      */   protected JButton createScrollButton(int paramInt)
/*      */   {
/*  317 */     if ((paramInt != 5) && (paramInt != 1) && (paramInt != 3) && (paramInt != 7))
/*      */     {
/*  319 */       throw new IllegalArgumentException("Direction must be one of: SOUTH, NORTH, EAST or WEST");
/*      */     }
/*      */ 
/*  322 */     return new ScrollableTabButton(paramInt);
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/*  332 */     uninstallTabContainer();
/*  333 */     if (scrollableTabLayoutEnabled()) {
/*  334 */       this.tabPane.remove(this.tabScroller.viewport);
/*  335 */       this.tabPane.remove(this.tabScroller.scrollForwardButton);
/*  336 */       this.tabPane.remove(this.tabScroller.scrollBackwardButton);
/*  337 */       this.tabScroller = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void uninstallTabContainer() {
/*  342 */     if (this.tabContainer == null) {
/*  343 */       return;
/*      */     }
/*      */ 
/*  347 */     this.tabContainer.notifyTabbedPane = false;
/*  348 */     this.tabContainer.removeAll();
/*  349 */     if (scrollableTabLayoutEnabled()) {
/*  350 */       this.tabContainer.remove(this.tabScroller.croppedEdge);
/*  351 */       this.tabScroller.tabPanel.remove(this.tabContainer);
/*      */     } else {
/*  353 */       this.tabPane.remove(this.tabContainer);
/*      */     }
/*  355 */     this.tabContainer = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*  359 */     LookAndFeel.installColorsAndFont(this.tabPane, "TabbedPane.background", "TabbedPane.foreground", "TabbedPane.font");
/*      */ 
/*  361 */     this.highlight = UIManager.getColor("TabbedPane.light");
/*  362 */     this.lightHighlight = UIManager.getColor("TabbedPane.highlight");
/*  363 */     this.shadow = UIManager.getColor("TabbedPane.shadow");
/*  364 */     this.darkShadow = UIManager.getColor("TabbedPane.darkShadow");
/*  365 */     this.focus = UIManager.getColor("TabbedPane.focus");
/*  366 */     this.selectedColor = UIManager.getColor("TabbedPane.selected");
/*      */ 
/*  368 */     this.textIconGap = UIManager.getInt("TabbedPane.textIconGap");
/*  369 */     this.tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
/*  370 */     this.selectedTabPadInsets = UIManager.getInsets("TabbedPane.selectedTabPadInsets");
/*  371 */     this.tabAreaInsets = UIManager.getInsets("TabbedPane.tabAreaInsets");
/*  372 */     this.tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
/*  373 */     this.contentBorderInsets = UIManager.getInsets("TabbedPane.contentBorderInsets");
/*  374 */     this.tabRunOverlay = UIManager.getInt("TabbedPane.tabRunOverlay");
/*  375 */     this.tabsOpaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
/*  376 */     this.contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
/*  377 */     Object localObject = UIManager.get("TabbedPane.opaque");
/*  378 */     if (localObject == null) {
/*  379 */       localObject = Boolean.FALSE;
/*      */     }
/*  381 */     LookAndFeel.installProperty(this.tabPane, "opaque", localObject);
/*      */ 
/*  386 */     if (this.tabInsets == null) this.tabInsets = new Insets(0, 4, 1, 4);
/*  387 */     if (this.selectedTabPadInsets == null) this.selectedTabPadInsets = new Insets(2, 2, 2, 1);
/*  388 */     if (this.tabAreaInsets == null) this.tabAreaInsets = new Insets(3, 2, 0, 2);
/*  389 */     if (this.contentBorderInsets == null) this.contentBorderInsets = new Insets(2, 2, 3, 3); 
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  393 */     this.highlight = null;
/*  394 */     this.lightHighlight = null;
/*  395 */     this.shadow = null;
/*  396 */     this.darkShadow = null;
/*  397 */     this.focus = null;
/*  398 */     this.tabInsets = null;
/*  399 */     this.selectedTabPadInsets = null;
/*  400 */     this.tabAreaInsets = null;
/*  401 */     this.contentBorderInsets = null;
/*      */   }
/*      */ 
/*      */   protected void installListeners() {
/*  405 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null) {
/*  406 */       this.tabPane.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  408 */     if ((this.tabChangeListener = createChangeListener()) != null) {
/*  409 */       this.tabPane.addChangeListener(this.tabChangeListener);
/*      */     }
/*  411 */     if ((this.mouseListener = createMouseListener()) != null) {
/*  412 */       this.tabPane.addMouseListener(this.mouseListener);
/*      */     }
/*  414 */     this.tabPane.addMouseMotionListener(getHandler());
/*  415 */     if ((this.focusListener = createFocusListener()) != null) {
/*  416 */       this.tabPane.addFocusListener(this.focusListener);
/*      */     }
/*  418 */     this.tabPane.addContainerListener(getHandler());
/*  419 */     if (this.tabPane.getTabCount() > 0)
/*  420 */       this.htmlViews = createHTMLVector();
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  425 */     if (this.mouseListener != null) {
/*  426 */       this.tabPane.removeMouseListener(this.mouseListener);
/*  427 */       this.mouseListener = null;
/*      */     }
/*  429 */     this.tabPane.removeMouseMotionListener(getHandler());
/*  430 */     if (this.focusListener != null) {
/*  431 */       this.tabPane.removeFocusListener(this.focusListener);
/*  432 */       this.focusListener = null;
/*      */     }
/*      */ 
/*  435 */     this.tabPane.removeContainerListener(getHandler());
/*  436 */     if (this.htmlViews != null) {
/*  437 */       this.htmlViews.removeAllElements();
/*  438 */       this.htmlViews = null;
/*      */     }
/*  440 */     if (this.tabChangeListener != null) {
/*  441 */       this.tabPane.removeChangeListener(this.tabChangeListener);
/*  442 */       this.tabChangeListener = null;
/*      */     }
/*  444 */     if (this.propertyChangeListener != null) {
/*  445 */       this.tabPane.removePropertyChangeListener(this.propertyChangeListener);
/*  446 */       this.propertyChangeListener = null;
/*      */     }
/*  448 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected MouseListener createMouseListener() {
/*  452 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener() {
/*  456 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener() {
/*  460 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  464 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  468 */     if (this.handler == null) {
/*  469 */       this.handler = new Handler(null);
/*      */     }
/*  471 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions() {
/*  475 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  478 */     SwingUtilities.replaceUIInputMap(this.tabPane, 1, localInputMap);
/*      */ 
/*  481 */     localInputMap = getInputMap(0);
/*  482 */     SwingUtilities.replaceUIInputMap(this.tabPane, 0, localInputMap);
/*      */ 
/*  484 */     LazyActionMap.installLazyActionMap(this.tabPane, BasicTabbedPaneUI.class, "TabbedPane.actionMap");
/*      */ 
/*  486 */     updateMnemonics();
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt) {
/*  490 */     if (paramInt == 1) {
/*  491 */       return (InputMap)DefaultLookup.get(this.tabPane, this, "TabbedPane.ancestorInputMap");
/*      */     }
/*      */ 
/*  494 */     if (paramInt == 0) {
/*  495 */       return (InputMap)DefaultLookup.get(this.tabPane, this, "TabbedPane.focusInputMap");
/*      */     }
/*      */ 
/*  498 */     return null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions() {
/*  502 */     SwingUtilities.replaceUIActionMap(this.tabPane, null);
/*  503 */     SwingUtilities.replaceUIInputMap(this.tabPane, 1, null);
/*      */ 
/*  506 */     SwingUtilities.replaceUIInputMap(this.tabPane, 0, null);
/*      */ 
/*  508 */     SwingUtilities.replaceUIInputMap(this.tabPane, 2, null);
/*      */ 
/*  511 */     this.mnemonicToIndexMap = null;
/*  512 */     this.mnemonicInputMap = null;
/*      */   }
/*      */ 
/*      */   private void updateMnemonics()
/*      */   {
/*  520 */     resetMnemonics();
/*  521 */     for (int i = this.tabPane.getTabCount() - 1; i >= 0; 
/*  522 */       i--) {
/*  523 */       int j = this.tabPane.getMnemonicAt(i);
/*      */ 
/*  525 */       if (j > 0)
/*  526 */         addMnemonic(i, j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetMnemonics()
/*      */   {
/*  535 */     if (this.mnemonicToIndexMap != null) {
/*  536 */       this.mnemonicToIndexMap.clear();
/*  537 */       this.mnemonicInputMap.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addMnemonic(int paramInt1, int paramInt2)
/*      */   {
/*  545 */     if (this.mnemonicToIndexMap == null) {
/*  546 */       initMnemonics();
/*      */     }
/*  548 */     this.mnemonicInputMap.put(KeyStroke.getKeyStroke(paramInt2, BasicLookAndFeel.getFocusAcceleratorKeyMask()), "setSelectedIndex");
/*      */ 
/*  550 */     this.mnemonicToIndexMap.put(Integer.valueOf(paramInt2), Integer.valueOf(paramInt1));
/*      */   }
/*      */ 
/*      */   private void initMnemonics()
/*      */   {
/*  557 */     this.mnemonicToIndexMap = new Hashtable();
/*  558 */     this.mnemonicInputMap = new ComponentInputMapUIResource(this.tabPane);
/*  559 */     this.mnemonicInputMap.setParent(SwingUtilities.getUIInputMap(this.tabPane, 2));
/*      */ 
/*  561 */     SwingUtilities.replaceUIInputMap(this.tabPane, 2, this.mnemonicInputMap);
/*      */   }
/*      */ 
/*      */   private void setRolloverTab(int paramInt1, int paramInt2)
/*      */   {
/*  575 */     setRolloverTab(tabForCoordinate(this.tabPane, paramInt1, paramInt2, false));
/*      */   }
/*      */ 
/*      */   protected void setRolloverTab(int paramInt)
/*      */   {
/*  588 */     this.rolloverTabIndex = paramInt;
/*      */   }
/*      */ 
/*      */   protected int getRolloverTab()
/*      */   {
/*  600 */     return this.rolloverTabIndex;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  605 */     return null;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  610 */     return null;
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  622 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  623 */     int i = calculateBaselineIfNecessary();
/*  624 */     if (i != -1) {
/*  625 */       int j = this.tabPane.getTabPlacement();
/*  626 */       Insets localInsets1 = this.tabPane.getInsets();
/*  627 */       Insets localInsets2 = getTabAreaInsets(j);
/*  628 */       switch (j) {
/*      */       case 1:
/*  630 */         i += localInsets1.top + localInsets2.top;
/*  631 */         return i;
/*      */       case 3:
/*  633 */         i = paramInt2 - localInsets1.bottom - localInsets2.bottom - this.maxTabHeight + i;
/*      */ 
/*  635 */         return i;
/*      */       case 2:
/*      */       case 4:
/*  638 */         i += localInsets1.top + localInsets2.top;
/*  639 */         return i;
/*      */       }
/*      */     }
/*  642 */     return -1;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  655 */     super.getBaselineResizeBehavior(paramJComponent);
/*  656 */     switch (this.tabPane.getTabPlacement()) {
/*      */     case 1:
/*      */     case 2:
/*      */     case 4:
/*  660 */       return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */     case 3:
/*  662 */       return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*      */     }
/*  664 */     return Component.BaselineResizeBehavior.OTHER;
/*      */   }
/*      */ 
/*      */   protected int getBaseline(int paramInt)
/*      */   {
/*  678 */     if (this.tabPane.getTabComponentAt(paramInt) != null) {
/*  679 */       int i = getBaselineOffset();
/*  680 */       if (i != 0)
/*      */       {
/*  684 */         return -1;
/*      */       }
/*  686 */       Component localComponent = this.tabPane.getTabComponentAt(paramInt);
/*  687 */       Dimension localDimension = localComponent.getPreferredSize();
/*  688 */       Insets localInsets = getTabInsets(this.tabPane.getTabPlacement(), paramInt);
/*  689 */       int m = this.maxTabHeight - localInsets.top - localInsets.bottom;
/*  690 */       return localComponent.getBaseline(localDimension.width, localDimension.height) + (m - localDimension.height) / 2 + localInsets.top;
/*      */     }
/*      */ 
/*  694 */     Object localObject = getTextViewForTab(paramInt);
/*  695 */     if (localObject != null) {
/*  696 */       j = (int)((View)localObject).getPreferredSpan(1);
/*  697 */       k = BasicHTML.getHTMLBaseline((View)localObject, (int)((View)localObject).getPreferredSpan(0), j);
/*      */ 
/*  699 */       if (k >= 0) {
/*  700 */         return this.maxTabHeight / 2 - j / 2 + k + getBaselineOffset();
/*      */       }
/*      */ 
/*  703 */       return -1;
/*      */     }
/*      */ 
/*  706 */     localObject = getFontMetrics();
/*  707 */     int j = ((FontMetrics)localObject).getHeight();
/*  708 */     int k = ((FontMetrics)localObject).getAscent();
/*  709 */     return this.maxTabHeight / 2 - j / 2 + k + getBaselineOffset();
/*      */   }
/*      */ 
/*      */   protected int getBaselineOffset()
/*      */   {
/*  721 */     switch (this.tabPane.getTabPlacement()) {
/*      */     case 1:
/*  723 */       if (this.tabPane.getTabCount() > 1) {
/*  724 */         return 1;
/*      */       }
/*      */ 
/*  727 */       return -1;
/*      */     case 3:
/*  730 */       if (this.tabPane.getTabCount() > 1) {
/*  731 */         return -1;
/*      */       }
/*      */ 
/*  734 */       return 1;
/*      */     }
/*      */ 
/*  737 */     return this.maxTabHeight % 2;
/*      */   }
/*      */ 
/*      */   private int calculateBaselineIfNecessary()
/*      */   {
/*  742 */     if (!this.calculatedBaseline) {
/*  743 */       this.calculatedBaseline = true;
/*  744 */       this.baseline = -1;
/*  745 */       if (this.tabPane.getTabCount() > 0) {
/*  746 */         calculateBaseline();
/*      */       }
/*      */     }
/*  749 */     return this.baseline;
/*      */   }
/*      */ 
/*      */   private void calculateBaseline() {
/*  753 */     int i = this.tabPane.getTabCount();
/*  754 */     int j = this.tabPane.getTabPlacement();
/*  755 */     this.maxTabHeight = calculateMaxTabHeight(j);
/*  756 */     this.baseline = getBaseline(0);
/*  757 */     if (isHorizontalTabPlacement()) {
/*  758 */       for (int k = 1; k < i; k++) {
/*  759 */         if (getBaseline(k) != this.baseline) {
/*  760 */           this.baseline = -1;
/*  761 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  767 */       FontMetrics localFontMetrics = getFontMetrics();
/*  768 */       int m = localFontMetrics.getHeight();
/*  769 */       int n = calculateTabHeight(j, 0, m);
/*  770 */       for (int i1 = 1; i1 < i; i1++) {
/*  771 */         int i2 = calculateTabHeight(j, i1, m);
/*  772 */         if (n != i2)
/*      */         {
/*  774 */           this.baseline = -1;
/*  775 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  784 */     int i = this.tabPane.getSelectedIndex();
/*  785 */     int j = this.tabPane.getTabPlacement();
/*      */ 
/*  787 */     ensureCurrentLayout();
/*      */ 
/*  790 */     if (this.tabsOverlapBorder) {
/*  791 */       paintContentBorder(paramGraphics, j, i);
/*      */     }
/*      */ 
/*  796 */     if (!scrollableTabLayoutEnabled()) {
/*  797 */       paintTabArea(paramGraphics, j, i);
/*      */     }
/*  799 */     if (!this.tabsOverlapBorder)
/*  800 */       paintContentBorder(paramGraphics, j, i);
/*      */   }
/*      */ 
/*      */   protected void paintTabArea(Graphics paramGraphics, int paramInt1, int paramInt2)
/*      */   {
/*  822 */     int i = this.tabPane.getTabCount();
/*      */ 
/*  824 */     Rectangle localRectangle1 = new Rectangle();
/*  825 */     Rectangle localRectangle2 = new Rectangle();
/*  826 */     Rectangle localRectangle3 = paramGraphics.getClipBounds();
/*      */ 
/*  829 */     for (int j = this.runCount - 1; j >= 0; j--) {
/*  830 */       int k = this.tabRuns[j];
/*  831 */       int m = this.tabRuns[(j + 1)];
/*  832 */       int n = m != 0 ? m - 1 : i - 1;
/*  833 */       for (int i1 = k; i1 <= n; i1++) {
/*  834 */         if ((i1 != paramInt2) && (this.rects[i1].intersects(localRectangle3))) {
/*  835 */           paintTab(paramGraphics, paramInt1, this.rects, i1, localRectangle1, localRectangle2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  842 */     if ((paramInt2 >= 0) && (this.rects[paramInt2].intersects(localRectangle3)))
/*  843 */       paintTab(paramGraphics, paramInt1, this.rects, paramInt2, localRectangle1, localRectangle2);
/*      */   }
/*      */ 
/*      */   protected void paintTab(Graphics paramGraphics, int paramInt1, Rectangle[] paramArrayOfRectangle, int paramInt2, Rectangle paramRectangle1, Rectangle paramRectangle2)
/*      */   {
/*  850 */     Rectangle localRectangle = paramArrayOfRectangle[paramInt2];
/*  851 */     int i = this.tabPane.getSelectedIndex();
/*  852 */     boolean bool = i == paramInt2;
/*      */ 
/*  854 */     if ((this.tabsOpaque) || (this.tabPane.isOpaque())) {
/*  855 */       paintTabBackground(paramGraphics, paramInt1, paramInt2, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, bool);
/*      */     }
/*      */ 
/*  859 */     paintTabBorder(paramGraphics, paramInt1, paramInt2, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, bool);
/*      */ 
/*  862 */     String str1 = this.tabPane.getTitleAt(paramInt2);
/*  863 */     Font localFont = this.tabPane.getFont();
/*  864 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.tabPane, paramGraphics, localFont);
/*  865 */     Icon localIcon = getIconForTab(paramInt2);
/*      */ 
/*  867 */     layoutLabel(paramInt1, localFontMetrics, paramInt2, str1, localIcon, localRectangle, paramRectangle1, paramRectangle2, bool);
/*      */ 
/*  870 */     if (this.tabPane.getTabComponentAt(paramInt2) == null) {
/*  871 */       String str2 = str1;
/*      */ 
/*  873 */       if ((scrollableTabLayoutEnabled()) && (this.tabScroller.croppedEdge.isParamsSet()) && (this.tabScroller.croppedEdge.getTabIndex() == paramInt2) && (isHorizontalTabPlacement()))
/*      */       {
/*  875 */         int j = this.tabScroller.croppedEdge.getCropline() - (paramRectangle2.x - localRectangle.x) - this.tabScroller.croppedEdge.getCroppedSideWidth();
/*      */ 
/*  877 */         str2 = SwingUtilities2.clipStringIfNecessary(null, localFontMetrics, str1, j);
/*  878 */       } else if ((!scrollableTabLayoutEnabled()) && (isHorizontalTabPlacement())) {
/*  879 */         str2 = SwingUtilities2.clipStringIfNecessary(null, localFontMetrics, str1, paramRectangle2.width);
/*      */       }
/*      */ 
/*  882 */       paintText(paramGraphics, paramInt1, localFont, localFontMetrics, paramInt2, str2, paramRectangle2, bool);
/*      */ 
/*  885 */       paintIcon(paramGraphics, paramInt1, paramInt2, localIcon, paramRectangle1, bool);
/*      */     }
/*  887 */     paintFocusIndicator(paramGraphics, paramInt1, paramArrayOfRectangle, paramInt2, paramRectangle1, paramRectangle2, bool);
/*      */   }
/*      */ 
/*      */   private boolean isHorizontalTabPlacement()
/*      */   {
/*  892 */     return (this.tabPane.getTabPlacement() == 1) || (this.tabPane.getTabPlacement() == 3);
/*      */   }
/*      */ 
/*      */   private static Polygon createCroppedTabShape(int paramInt1, Rectangle paramRectangle, int paramInt2)
/*      */   {
/*      */     int i;
/*      */     int j;
/*      */     int k;
/*      */     int m;
/*  927 */     switch (paramInt1) {
/*      */     case 2:
/*      */     case 4:
/*  930 */       i = paramRectangle.width;
/*  931 */       j = paramRectangle.x;
/*  932 */       k = paramRectangle.x + paramRectangle.width;
/*  933 */       m = paramRectangle.y + paramRectangle.height;
/*  934 */       break;
/*      */     case 1:
/*      */     case 3:
/*      */     default:
/*  938 */       i = paramRectangle.height;
/*  939 */       j = paramRectangle.y;
/*  940 */       k = paramRectangle.y + paramRectangle.height;
/*  941 */       m = paramRectangle.x + paramRectangle.width;
/*      */     }
/*  943 */     int n = i / 12;
/*  944 */     if (i % 12 > 0) {
/*  945 */       n++;
/*      */     }
/*  947 */     int i1 = 2 + n * 8;
/*  948 */     int[] arrayOfInt1 = new int[i1];
/*  949 */     int[] arrayOfInt2 = new int[i1];
/*  950 */     int i2 = 0;
/*      */ 
/*  952 */     arrayOfInt1[i2] = m;
/*  953 */     arrayOfInt2[(i2++)] = k;
/*  954 */     arrayOfInt1[i2] = m;
/*  955 */     arrayOfInt2[(i2++)] = j;
/*  956 */     for (int i3 = 0; i3 < n; i3++) {
/*  957 */       for (int i4 = 0; i4 < xCropLen.length; i4++) {
/*  958 */         arrayOfInt1[i2] = (paramInt2 - xCropLen[i4]);
/*  959 */         arrayOfInt2[i2] = (j + i3 * 12 + yCropLen[i4]);
/*  960 */         if (arrayOfInt2[i2] >= k) {
/*  961 */           arrayOfInt2[i2] = k;
/*  962 */           i2++;
/*  963 */           break;
/*      */         }
/*  965 */         i2++;
/*      */       }
/*      */     }
/*  968 */     if ((paramInt1 == 1) || (paramInt1 == 3)) {
/*  969 */       return new Polygon(arrayOfInt1, arrayOfInt2, i2);
/*      */     }
/*      */ 
/*  972 */     return new Polygon(arrayOfInt2, arrayOfInt1, i2);
/*      */   }
/*      */ 
/*      */   private void paintCroppedTabEdge(Graphics paramGraphics)
/*      */   {
/*  980 */     int i = this.tabScroller.croppedEdge.getTabIndex();
/*  981 */     int j = this.tabScroller.croppedEdge.getCropline();
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*  983 */     switch (this.tabPane.getTabPlacement()) {
/*      */     case 2:
/*      */     case 4:
/*  986 */       k = this.rects[i].x;
/*  987 */       m = j;
/*  988 */       n = k;
/*  989 */       paramGraphics.setColor(this.shadow);
/*      */     case 1:
/*  990 */     case 3: } while (n <= k + this.rects[i].width) {
/*  991 */       for (int i1 = 0; i1 < xCropLen.length; i1 += 2) {
/*  992 */         paramGraphics.drawLine(n + yCropLen[i1], m - xCropLen[i1], n + yCropLen[(i1 + 1)] - 1, m - xCropLen[(i1 + 1)]);
/*      */       }
/*      */ 
/*  995 */       n += 12; continue;
/*      */ 
/* 1001 */       k = j;
/* 1002 */       m = this.rects[i].y;
/* 1003 */       i1 = m;
/* 1004 */       paramGraphics.setColor(this.shadow);
/* 1005 */       while (i1 <= m + this.rects[i].height) {
/* 1006 */         for (int i2 = 0; i2 < xCropLen.length; i2 += 2) {
/* 1007 */           paramGraphics.drawLine(k - xCropLen[i2], i1 + yCropLen[i2], k - xCropLen[(i2 + 1)], i1 + yCropLen[(i2 + 1)] - 1);
/*      */         }
/*      */ 
/* 1010 */         i1 += 12;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void layoutLabel(int paramInt1, FontMetrics paramFontMetrics, int paramInt2, String paramString, Icon paramIcon, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, boolean paramBoolean)
/*      */   {
/* 1020 */     paramRectangle3.x = (paramRectangle3.y = paramRectangle2.x = paramRectangle2.y = 0);
/*      */ 
/* 1022 */     View localView = getTextViewForTab(paramInt2);
/* 1023 */     if (localView != null) {
/* 1024 */       this.tabPane.putClientProperty("html", localView);
/*      */     }
/*      */ 
/* 1027 */     SwingUtilities.layoutCompoundLabel(this.tabPane, paramFontMetrics, paramString, paramIcon, 0, 0, 0, 11, paramRectangle1, paramRectangle2, paramRectangle3, this.textIconGap);
/*      */ 
/* 1038 */     this.tabPane.putClientProperty("html", null);
/*      */ 
/* 1040 */     int i = getTabLabelShiftX(paramInt1, paramInt2, paramBoolean);
/* 1041 */     int j = getTabLabelShiftY(paramInt1, paramInt2, paramBoolean);
/* 1042 */     paramRectangle2.x += i;
/* 1043 */     paramRectangle2.y += j;
/* 1044 */     paramRectangle3.x += i;
/* 1045 */     paramRectangle3.y += j;
/*      */   }
/*      */ 
/*      */   protected void paintIcon(Graphics paramGraphics, int paramInt1, int paramInt2, Icon paramIcon, Rectangle paramRectangle, boolean paramBoolean)
/*      */   {
/* 1051 */     if (paramIcon != null)
/* 1052 */       paramIcon.paintIcon(this.tabPane, paramGraphics, paramRectangle.x, paramRectangle.y);
/*      */   }
/*      */ 
/*      */   protected void paintText(Graphics paramGraphics, int paramInt1, Font paramFont, FontMetrics paramFontMetrics, int paramInt2, String paramString, Rectangle paramRectangle, boolean paramBoolean)
/*      */   {
/* 1061 */     paramGraphics.setFont(paramFont);
/*      */ 
/* 1063 */     View localView = getTextViewForTab(paramInt2);
/* 1064 */     if (localView != null)
/*      */     {
/* 1066 */       localView.paint(paramGraphics, paramRectangle);
/*      */     }
/*      */     else {
/* 1069 */       int i = this.tabPane.getDisplayedMnemonicIndexAt(paramInt2);
/*      */ 
/* 1071 */       if ((this.tabPane.isEnabled()) && (this.tabPane.isEnabledAt(paramInt2))) {
/* 1072 */         Object localObject = this.tabPane.getForegroundAt(paramInt2);
/* 1073 */         if ((paramBoolean) && ((localObject instanceof UIResource))) {
/* 1074 */           Color localColor = UIManager.getColor("TabbedPane.selectedForeground");
/*      */ 
/* 1076 */           if (localColor != null) {
/* 1077 */             localObject = localColor;
/*      */           }
/*      */         }
/* 1080 */         paramGraphics.setColor((Color)localObject);
/* 1081 */         SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + paramFontMetrics.getAscent());
/*      */       }
/*      */       else
/*      */       {
/* 1086 */         paramGraphics.setColor(this.tabPane.getBackgroundAt(paramInt2).brighter());
/* 1087 */         SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + paramFontMetrics.getAscent());
/*      */ 
/* 1090 */         paramGraphics.setColor(this.tabPane.getBackgroundAt(paramInt2).darker());
/* 1091 */         SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, paramGraphics, paramString, i, paramRectangle.x - 1, paramRectangle.y + paramFontMetrics.getAscent() - 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getTabLabelShiftX(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1101 */     Rectangle localRectangle = this.rects[paramInt2];
/* 1102 */     String str = paramBoolean ? "selectedLabelShift" : "labelShift";
/* 1103 */     int i = DefaultLookup.getInt(this.tabPane, this, "TabbedPane." + str, 1);
/*      */ 
/* 1106 */     switch (paramInt1) {
/*      */     case 2:
/* 1108 */       return i;
/*      */     case 4:
/* 1110 */       return -i;
/*      */     case 1:
/*      */     case 3:
/*      */     }
/* 1114 */     return localRectangle.width % 2;
/*      */   }
/*      */ 
/*      */   protected int getTabLabelShiftY(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1119 */     Rectangle localRectangle = this.rects[paramInt2];
/* 1120 */     int i = paramBoolean ? DefaultLookup.getInt(this.tabPane, this, "TabbedPane.selectedLabelShift", -1) : DefaultLookup.getInt(this.tabPane, this, "TabbedPane.labelShift", 1);
/*      */ 
/* 1123 */     switch (paramInt1) {
/*      */     case 3:
/* 1125 */       return -i;
/*      */     case 2:
/*      */     case 4:
/* 1128 */       return localRectangle.height % 2;
/*      */     case 1:
/*      */     }
/* 1131 */     return i;
/*      */   }
/*      */ 
/*      */   protected void paintFocusIndicator(Graphics paramGraphics, int paramInt1, Rectangle[] paramArrayOfRectangle, int paramInt2, Rectangle paramRectangle1, Rectangle paramRectangle2, boolean paramBoolean)
/*      */   {
/* 1139 */     Rectangle localRectangle = paramArrayOfRectangle[paramInt2];
/* 1140 */     if ((this.tabPane.hasFocus()) && (paramBoolean))
/*      */     {
/* 1142 */       paramGraphics.setColor(this.focus);
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       int m;
/* 1143 */       switch (paramInt1) {
/*      */       case 2:
/* 1145 */         i = localRectangle.x + 3;
/* 1146 */         j = localRectangle.y + 3;
/* 1147 */         k = localRectangle.width - 5;
/* 1148 */         m = localRectangle.height - 6;
/* 1149 */         break;
/*      */       case 4:
/* 1151 */         i = localRectangle.x + 2;
/* 1152 */         j = localRectangle.y + 3;
/* 1153 */         k = localRectangle.width - 5;
/* 1154 */         m = localRectangle.height - 6;
/* 1155 */         break;
/*      */       case 3:
/* 1157 */         i = localRectangle.x + 3;
/* 1158 */         j = localRectangle.y + 2;
/* 1159 */         k = localRectangle.width - 6;
/* 1160 */         m = localRectangle.height - 5;
/* 1161 */         break;
/*      */       case 1:
/*      */       default:
/* 1164 */         i = localRectangle.x + 3;
/* 1165 */         j = localRectangle.y + 3;
/* 1166 */         k = localRectangle.width - 6;
/* 1167 */         m = localRectangle.height - 5;
/*      */       }
/* 1169 */       BasicGraphicsUtils.drawDashedRect(paramGraphics, i, j, k, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintTabBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */   {
/* 1182 */     paramGraphics.setColor(this.lightHighlight);
/*      */ 
/* 1184 */     switch (paramInt1) {
/*      */     case 2:
/* 1186 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 2, paramInt3 + 1, paramInt4 + paramInt6 - 2);
/* 1187 */       paramGraphics.drawLine(paramInt3, paramInt4 + 2, paramInt3, paramInt4 + paramInt6 - 3);
/* 1188 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + 1, paramInt3 + 1, paramInt4 + 1);
/* 1189 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4, paramInt3 + paramInt5 - 1, paramInt4);
/*      */ 
/* 1191 */       paramGraphics.setColor(this.shadow);
/* 1192 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 2);
/*      */ 
/* 1194 */       paramGraphics.setColor(this.darkShadow);
/* 1195 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/* 1196 */       break;
/*      */     case 4:
/* 1198 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3 + paramInt5 - 3, paramInt4);
/*      */ 
/* 1200 */       paramGraphics.setColor(this.shadow);
/* 1201 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 2);
/* 1202 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 3);
/*      */ 
/* 1204 */       paramGraphics.setColor(this.darkShadow);
/* 1205 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 1);
/* 1206 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 1207 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 2, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 3);
/* 1208 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 1);
/* 1209 */       break;
/*      */     case 3:
/* 1211 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3, paramInt4 + paramInt6 - 3);
/* 1212 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 2, paramInt3 + 1, paramInt4 + paramInt6 - 2);
/*      */ 
/* 1214 */       paramGraphics.setColor(this.shadow);
/* 1215 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 2);
/* 1216 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 3);
/*      */ 
/* 1218 */       paramGraphics.setColor(this.darkShadow);
/* 1219 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 1);
/* 1220 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 1221 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 3);
/* 1222 */       break;
/*      */     case 1:
/*      */     default:
/* 1225 */       paramGraphics.drawLine(paramInt3, paramInt4 + 2, paramInt3, paramInt4 + paramInt6 - 1);
/* 1226 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + 1, paramInt3 + 1, paramInt4 + 1);
/* 1227 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4, paramInt3 + paramInt5 - 3, paramInt4);
/*      */ 
/* 1229 */       paramGraphics.setColor(this.shadow);
/* 1230 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 1);
/*      */ 
/* 1232 */       paramGraphics.setColor(this.darkShadow);
/* 1233 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 2, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/* 1234 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintTabBackground(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */   {
/* 1242 */     paramGraphics.setColor((!paramBoolean) || (this.selectedColor == null) ? this.tabPane.getBackgroundAt(paramInt2) : this.selectedColor);
/*      */ 
/* 1244 */     switch (paramInt1) {
/*      */     case 2:
/* 1246 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4 + 1, paramInt5 - 1, paramInt6 - 3);
/* 1247 */       break;
/*      */     case 4:
/* 1249 */       paramGraphics.fillRect(paramInt3, paramInt4 + 1, paramInt5 - 2, paramInt6 - 3);
/* 1250 */       break;
/*      */     case 3:
/* 1252 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4, paramInt5 - 3, paramInt6 - 1);
/* 1253 */       break;
/*      */     case 1:
/*      */     default:
/* 1256 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4 + 1, paramInt5 - 3, paramInt6 - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorder(Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 1261 */     int i = this.tabPane.getWidth();
/* 1262 */     int j = this.tabPane.getHeight();
/* 1263 */     Insets localInsets1 = this.tabPane.getInsets();
/* 1264 */     Insets localInsets2 = getTabAreaInsets(paramInt1);
/*      */ 
/* 1266 */     int k = localInsets1.left;
/* 1267 */     int m = localInsets1.top;
/* 1268 */     int n = i - localInsets1.right - localInsets1.left;
/* 1269 */     int i1 = j - localInsets1.top - localInsets1.bottom;
/*      */ 
/* 1271 */     switch (paramInt1) {
/*      */     case 2:
/* 1273 */       k += calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth);
/* 1274 */       if (this.tabsOverlapBorder) {
/* 1275 */         k -= localInsets2.right;
/*      */       }
/* 1277 */       n -= k - localInsets1.left;
/* 1278 */       break;
/*      */     case 4:
/* 1280 */       n -= calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth);
/* 1281 */       if (this.tabsOverlapBorder)
/* 1282 */         n += localInsets2.left; break;
/*      */     case 3:
/* 1286 */       i1 -= calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight);
/* 1287 */       if (this.tabsOverlapBorder)
/* 1288 */         i1 += localInsets2.top; break;
/*      */     case 1:
/*      */     default:
/* 1293 */       m += calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight);
/* 1294 */       if (this.tabsOverlapBorder) {
/* 1295 */         m -= localInsets2.bottom;
/*      */       }
/* 1297 */       i1 -= m - localInsets1.top;
/*      */     }
/*      */ 
/* 1300 */     if ((this.tabPane.getTabCount() > 0) && ((this.contentOpaque) || (this.tabPane.isOpaque())))
/*      */     {
/* 1302 */       Color localColor = UIManager.getColor("TabbedPane.contentAreaColor");
/* 1303 */       if (localColor != null) {
/* 1304 */         paramGraphics.setColor(localColor);
/*      */       }
/* 1306 */       else if ((this.selectedColor == null) || (paramInt2 == -1)) {
/* 1307 */         paramGraphics.setColor(this.tabPane.getBackground());
/*      */       }
/*      */       else {
/* 1310 */         paramGraphics.setColor(this.selectedColor);
/*      */       }
/* 1312 */       paramGraphics.fillRect(k, m, n, i1);
/*      */     }
/*      */ 
/* 1315 */     paintContentBorderTopEdge(paramGraphics, paramInt1, paramInt2, k, m, n, i1);
/* 1316 */     paintContentBorderLeftEdge(paramGraphics, paramInt1, paramInt2, k, m, n, i1);
/* 1317 */     paintContentBorderBottomEdge(paramGraphics, paramInt1, paramInt2, k, m, n, i1);
/* 1318 */     paintContentBorderRightEdge(paramGraphics, paramInt1, paramInt2, k, m, n, i1);
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderTopEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1325 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1328 */     paramGraphics.setColor(this.lightHighlight);
/*      */ 
/* 1334 */     if ((paramInt1 != 1) || (paramInt2 < 0) || (localRectangle.y + localRectangle.height + 1 < paramInt4) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*      */     {
/* 1337 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*      */     }
/*      */     else {
/* 1340 */       paramGraphics.drawLine(paramInt3, paramInt4, localRectangle.x - 1, paramInt4);
/* 1341 */       if (localRectangle.x + localRectangle.width < paramInt3 + paramInt5 - 2) {
/* 1342 */         paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*      */       }
/*      */       else {
/* 1345 */         paramGraphics.setColor(this.shadow);
/* 1346 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderLeftEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1354 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1357 */     paramGraphics.setColor(this.lightHighlight);
/*      */ 
/* 1363 */     if ((paramInt1 != 2) || (paramInt2 < 0) || (localRectangle.x + localRectangle.width + 1 < paramInt3) || (localRectangle.y < paramInt4) || (localRectangle.y > paramInt4 + paramInt6))
/*      */     {
/* 1366 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3, paramInt4 + paramInt6 - 2);
/*      */     }
/*      */     else {
/* 1369 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3, localRectangle.y - 1);
/* 1370 */       if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2)
/* 1371 */         paramGraphics.drawLine(paramInt3, localRectangle.y + localRectangle.height, paramInt3, paramInt4 + paramInt6 - 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderBottomEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1380 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1383 */     paramGraphics.setColor(this.shadow);
/*      */ 
/* 1389 */     if ((paramInt1 != 3) || (paramInt2 < 0) || (localRectangle.y - 1 > paramInt6) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*      */     {
/* 1392 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 1393 */       paramGraphics.setColor(this.darkShadow);
/* 1394 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*      */     }
/*      */     else {
/* 1397 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 2, localRectangle.x - 1, paramInt4 + paramInt6 - 2);
/* 1398 */       paramGraphics.setColor(this.darkShadow);
/* 1399 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 1, localRectangle.x - 1, paramInt4 + paramInt6 - 1);
/* 1400 */       if (localRectangle.x + localRectangle.width < paramInt3 + paramInt5 - 2) {
/* 1401 */         paramGraphics.setColor(this.shadow);
/* 1402 */         paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 1403 */         paramGraphics.setColor(this.darkShadow);
/* 1404 */         paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderRightEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1413 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1416 */     paramGraphics.setColor(this.shadow);
/*      */ 
/* 1422 */     if ((paramInt1 != 4) || (paramInt2 < 0) || (localRectangle.x - 1 > paramInt5) || (localRectangle.y < paramInt4) || (localRectangle.y > paramInt4 + paramInt6))
/*      */     {
/* 1425 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 3);
/* 1426 */       paramGraphics.setColor(this.darkShadow);
/* 1427 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*      */     }
/*      */     else {
/* 1430 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, localRectangle.y - 1);
/* 1431 */       paramGraphics.setColor(this.darkShadow);
/* 1432 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, localRectangle.y - 1);
/*      */ 
/* 1434 */       if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2) {
/* 1435 */         paramGraphics.setColor(this.shadow);
/* 1436 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 2, localRectangle.y + localRectangle.height, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/*      */ 
/* 1438 */         paramGraphics.setColor(this.darkShadow);
/* 1439 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 1, localRectangle.y + localRectangle.height, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureCurrentLayout()
/*      */   {
/* 1446 */     if (!this.tabPane.isValid()) {
/* 1447 */       this.tabPane.validate();
/*      */     }
/*      */ 
/* 1453 */     if (!this.tabPane.isValid()) {
/* 1454 */       TabbedPaneLayout localTabbedPaneLayout = (TabbedPaneLayout)this.tabPane.getLayout();
/* 1455 */       localTabbedPaneLayout.calculateLayoutInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Rectangle getTabBounds(JTabbedPane paramJTabbedPane, int paramInt)
/*      */   {
/* 1467 */     ensureCurrentLayout();
/* 1468 */     Rectangle localRectangle = new Rectangle();
/* 1469 */     return getTabBounds(paramInt, localRectangle);
/*      */   }
/*      */ 
/*      */   public int getTabRunCount(JTabbedPane paramJTabbedPane) {
/* 1473 */     ensureCurrentLayout();
/* 1474 */     return this.runCount;
/*      */   }
/*      */ 
/*      */   public int tabForCoordinate(JTabbedPane paramJTabbedPane, int paramInt1, int paramInt2)
/*      */   {
/* 1482 */     return tabForCoordinate(paramJTabbedPane, paramInt1, paramInt2, true);
/*      */   }
/*      */ 
/*      */   private int tabForCoordinate(JTabbedPane paramJTabbedPane, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1487 */     if (paramBoolean) {
/* 1488 */       ensureCurrentLayout();
/*      */     }
/* 1490 */     if (this.isRunsDirty)
/*      */     {
/* 1493 */       return -1;
/*      */     }
/* 1495 */     Point localPoint = new Point(paramInt1, paramInt2);
/*      */ 
/* 1497 */     if (scrollableTabLayoutEnabled()) {
/* 1498 */       translatePointToTabPanel(paramInt1, paramInt2, localPoint);
/* 1499 */       Rectangle localRectangle = this.tabScroller.viewport.getViewRect();
/* 1500 */       if (!localRectangle.contains(localPoint)) {
/* 1501 */         return -1;
/*      */       }
/*      */     }
/* 1504 */     int i = this.tabPane.getTabCount();
/* 1505 */     for (int j = 0; j < i; j++) {
/* 1506 */       if (this.rects[j].contains(localPoint.x, localPoint.y)) {
/* 1507 */         return j;
/*      */       }
/*      */     }
/* 1510 */     return -1;
/*      */   }
/*      */ 
/*      */   protected Rectangle getTabBounds(int paramInt, Rectangle paramRectangle)
/*      */   {
/* 1534 */     paramRectangle.width = this.rects[paramInt].width;
/* 1535 */     paramRectangle.height = this.rects[paramInt].height;
/*      */ 
/* 1537 */     if (scrollableTabLayoutEnabled())
/*      */     {
/* 1540 */       Point localPoint1 = this.tabScroller.viewport.getLocation();
/* 1541 */       Point localPoint2 = this.tabScroller.viewport.getViewPosition();
/* 1542 */       paramRectangle.x = (this.rects[paramInt].x + localPoint1.x - localPoint2.x);
/* 1543 */       paramRectangle.y = (this.rects[paramInt].y + localPoint1.y - localPoint2.y);
/*      */     }
/*      */     else {
/* 1546 */       paramRectangle.x = this.rects[paramInt].x;
/* 1547 */       paramRectangle.y = this.rects[paramInt].y;
/*      */     }
/* 1549 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   private int getClosestTab(int paramInt1, int paramInt2)
/*      */   {
/* 1557 */     int i = 0;
/* 1558 */     int j = Math.min(this.rects.length, this.tabPane.getTabCount());
/* 1559 */     int k = j;
/* 1560 */     int m = this.tabPane.getTabPlacement();
/* 1561 */     int n = (m == 1) || (m == 3) ? 1 : 0;
/* 1562 */     int i1 = n != 0 ? paramInt1 : paramInt2;
/*      */ 
/* 1564 */     while (i != k) {
/* 1565 */       int i2 = (k + i) / 2;
/*      */       int i3;
/*      */       int i4;
/* 1569 */       if (n != 0) {
/* 1570 */         i3 = this.rects[i2].x;
/* 1571 */         i4 = i3 + this.rects[i2].width;
/*      */       }
/*      */       else {
/* 1574 */         i3 = this.rects[i2].y;
/* 1575 */         i4 = i3 + this.rects[i2].height;
/*      */       }
/* 1577 */       if (i1 < i3) {
/* 1578 */         k = i2;
/* 1579 */         if (i == k) {
/* 1580 */           return Math.max(0, i2 - 1);
/*      */         }
/*      */       }
/* 1583 */       else if (i1 >= i4) {
/* 1584 */         i = i2;
/* 1585 */         if (k - i <= 1)
/* 1586 */           return Math.max(i2 + 1, j - 1);
/*      */       }
/*      */       else
/*      */       {
/* 1590 */         return i2;
/*      */       }
/*      */     }
/* 1593 */     return i;
/*      */   }
/*      */ 
/*      */   private Point translatePointToTabPanel(int paramInt1, int paramInt2, Point paramPoint)
/*      */   {
/* 1602 */     Point localPoint1 = this.tabScroller.viewport.getLocation();
/* 1603 */     Point localPoint2 = this.tabScroller.viewport.getViewPosition();
/* 1604 */     paramPoint.x = (paramInt1 - localPoint1.x + localPoint2.x);
/* 1605 */     paramPoint.y = (paramInt2 - localPoint1.y + localPoint2.y);
/* 1606 */     return paramPoint;
/*      */   }
/*      */ 
/*      */   protected Component getVisibleComponent()
/*      */   {
/* 1612 */     return this.visibleComponent;
/*      */   }
/*      */ 
/*      */   protected void setVisibleComponent(Component paramComponent) {
/* 1616 */     if ((this.visibleComponent != null) && (this.visibleComponent != paramComponent) && (this.visibleComponent.getParent() == this.tabPane) && (this.visibleComponent.isVisible()))
/*      */     {
/* 1621 */       this.visibleComponent.setVisible(false);
/*      */     }
/* 1623 */     if ((paramComponent != null) && (!paramComponent.isVisible())) {
/* 1624 */       paramComponent.setVisible(true);
/*      */     }
/* 1626 */     this.visibleComponent = paramComponent;
/*      */   }
/*      */ 
/*      */   protected void assureRectsCreated(int paramInt) {
/* 1630 */     int i = this.rects.length;
/* 1631 */     if (paramInt != i) {
/* 1632 */       Rectangle[] arrayOfRectangle = new Rectangle[paramInt];
/* 1633 */       System.arraycopy(this.rects, 0, arrayOfRectangle, 0, Math.min(i, paramInt));
/*      */ 
/* 1635 */       this.rects = arrayOfRectangle;
/* 1636 */       for (int j = i; j < paramInt; j++)
/* 1637 */         this.rects[j] = new Rectangle();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void expandTabRunsArray()
/*      */   {
/* 1644 */     int i = this.tabRuns.length;
/* 1645 */     int[] arrayOfInt = new int[i + 10];
/* 1646 */     System.arraycopy(this.tabRuns, 0, arrayOfInt, 0, this.runCount);
/* 1647 */     this.tabRuns = arrayOfInt;
/*      */   }
/*      */ 
/*      */   protected int getRunForTab(int paramInt1, int paramInt2) {
/* 1651 */     for (int i = 0; i < this.runCount; i++) {
/* 1652 */       int j = this.tabRuns[i];
/* 1653 */       int k = lastTabInRun(paramInt1, i);
/* 1654 */       if ((paramInt2 >= j) && (paramInt2 <= k)) {
/* 1655 */         return i;
/*      */       }
/*      */     }
/* 1658 */     return 0;
/*      */   }
/*      */ 
/*      */   protected int lastTabInRun(int paramInt1, int paramInt2) {
/* 1662 */     if (this.runCount == 1) {
/* 1663 */       return paramInt1 - 1;
/*      */     }
/* 1665 */     int i = paramInt2 == this.runCount - 1 ? 0 : paramInt2 + 1;
/* 1666 */     if (this.tabRuns[i] == 0) {
/* 1667 */       return paramInt1 - 1;
/*      */     }
/* 1669 */     return this.tabRuns[i] - 1;
/*      */   }
/*      */ 
/*      */   protected int getTabRunOverlay(int paramInt) {
/* 1673 */     return this.tabRunOverlay;
/*      */   }
/*      */ 
/*      */   protected int getTabRunIndent(int paramInt1, int paramInt2) {
/* 1677 */     return 0;
/*      */   }
/*      */ 
/*      */   protected boolean shouldPadTabRun(int paramInt1, int paramInt2) {
/* 1681 */     return this.runCount > 1;
/*      */   }
/*      */ 
/*      */   protected boolean shouldRotateTabRuns(int paramInt) {
/* 1685 */     return true;
/*      */   }
/*      */ 
/*      */   protected Icon getIconForTab(int paramInt) {
/* 1689 */     return (!this.tabPane.isEnabled()) || (!this.tabPane.isEnabledAt(paramInt)) ? this.tabPane.getDisabledIconAt(paramInt) : this.tabPane.getIconAt(paramInt);
/*      */   }
/*      */ 
/*      */   protected View getTextViewForTab(int paramInt)
/*      */   {
/* 1705 */     if (this.htmlViews != null) {
/* 1706 */       return (View)this.htmlViews.elementAt(paramInt);
/*      */     }
/* 1708 */     return null;
/*      */   }
/*      */ 
/*      */   protected int calculateTabHeight(int paramInt1, int paramInt2, int paramInt3) {
/* 1712 */     int i = 0;
/* 1713 */     Component localComponent = this.tabPane.getTabComponentAt(paramInt2);
/* 1714 */     if (localComponent != null) {
/* 1715 */       i = localComponent.getPreferredSize().height;
/*      */     } else {
/* 1717 */       localObject = getTextViewForTab(paramInt2);
/* 1718 */       if (localObject != null)
/*      */       {
/* 1720 */         i += (int)((View)localObject).getPreferredSpan(1);
/*      */       }
/*      */       else {
/* 1723 */         i += paramInt3;
/*      */       }
/* 1725 */       Icon localIcon = getIconForTab(paramInt2);
/*      */ 
/* 1727 */       if (localIcon != null) {
/* 1728 */         i = Math.max(i, localIcon.getIconHeight());
/*      */       }
/*      */     }
/* 1731 */     Object localObject = getTabInsets(paramInt1, paramInt2);
/* 1732 */     i += ((Insets)localObject).top + ((Insets)localObject).bottom + 2;
/* 1733 */     return i;
/*      */   }
/*      */ 
/*      */   protected int calculateMaxTabHeight(int paramInt) {
/* 1737 */     FontMetrics localFontMetrics = getFontMetrics();
/* 1738 */     int i = this.tabPane.getTabCount();
/* 1739 */     int j = 0;
/* 1740 */     int k = localFontMetrics.getHeight();
/* 1741 */     for (int m = 0; m < i; m++) {
/* 1742 */       j = Math.max(calculateTabHeight(paramInt, m, k), j);
/*      */     }
/* 1744 */     return j;
/*      */   }
/*      */ 
/*      */   protected int calculateTabWidth(int paramInt1, int paramInt2, FontMetrics paramFontMetrics) {
/* 1748 */     Insets localInsets = getTabInsets(paramInt1, paramInt2);
/* 1749 */     int i = localInsets.left + localInsets.right + 3;
/* 1750 */     Component localComponent = this.tabPane.getTabComponentAt(paramInt2);
/* 1751 */     if (localComponent != null) {
/* 1752 */       i += localComponent.getPreferredSize().width;
/*      */     } else {
/* 1754 */       Icon localIcon = getIconForTab(paramInt2);
/* 1755 */       if (localIcon != null) {
/* 1756 */         i += localIcon.getIconWidth() + this.textIconGap;
/*      */       }
/* 1758 */       View localView = getTextViewForTab(paramInt2);
/* 1759 */       if (localView != null)
/*      */       {
/* 1761 */         i += (int)localView.getPreferredSpan(0);
/*      */       }
/*      */       else {
/* 1764 */         String str = this.tabPane.getTitleAt(paramInt2);
/* 1765 */         i += SwingUtilities2.stringWidth(this.tabPane, paramFontMetrics, str);
/*      */       }
/*      */     }
/* 1768 */     return i;
/*      */   }
/*      */ 
/*      */   protected int calculateMaxTabWidth(int paramInt) {
/* 1772 */     FontMetrics localFontMetrics = getFontMetrics();
/* 1773 */     int i = this.tabPane.getTabCount();
/* 1774 */     int j = 0;
/* 1775 */     for (int k = 0; k < i; k++) {
/* 1776 */       j = Math.max(calculateTabWidth(paramInt, k, localFontMetrics), j);
/*      */     }
/* 1778 */     return j;
/*      */   }
/*      */ 
/*      */   protected int calculateTabAreaHeight(int paramInt1, int paramInt2, int paramInt3) {
/* 1782 */     Insets localInsets = getTabAreaInsets(paramInt1);
/* 1783 */     int i = getTabRunOverlay(paramInt1);
/* 1784 */     return paramInt2 > 0 ? paramInt2 * (paramInt3 - i) + i + localInsets.top + localInsets.bottom : 0;
/*      */   }
/*      */ 
/*      */   protected int calculateTabAreaWidth(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1791 */     Insets localInsets = getTabAreaInsets(paramInt1);
/* 1792 */     int i = getTabRunOverlay(paramInt1);
/* 1793 */     return paramInt2 > 0 ? paramInt2 * (paramInt3 - i) + i + localInsets.left + localInsets.right : 0;
/*      */   }
/*      */ 
/*      */   protected Insets getTabInsets(int paramInt1, int paramInt2)
/*      */   {
/* 1800 */     return this.tabInsets;
/*      */   }
/*      */ 
/*      */   protected Insets getSelectedTabPadInsets(int paramInt) {
/* 1804 */     rotateInsets(this.selectedTabPadInsets, this.currentPadInsets, paramInt);
/* 1805 */     return this.currentPadInsets;
/*      */   }
/*      */ 
/*      */   protected Insets getTabAreaInsets(int paramInt) {
/* 1809 */     rotateInsets(this.tabAreaInsets, this.currentTabAreaInsets, paramInt);
/* 1810 */     return this.currentTabAreaInsets;
/*      */   }
/*      */ 
/*      */   protected Insets getContentBorderInsets(int paramInt) {
/* 1814 */     return this.contentBorderInsets;
/*      */   }
/*      */ 
/*      */   protected FontMetrics getFontMetrics() {
/* 1818 */     Font localFont = this.tabPane.getFont();
/* 1819 */     return this.tabPane.getFontMetrics(localFont);
/*      */   }
/*      */ 
/*      */   protected void navigateSelectedTab(int paramInt)
/*      */   {
/* 1826 */     int i = this.tabPane.getTabPlacement();
/* 1827 */     int j = DefaultLookup.getBoolean(this.tabPane, this, "TabbedPane.selectionFollowsFocus", true) ? this.tabPane.getSelectedIndex() : getFocusIndex();
/*      */ 
/* 1830 */     int k = this.tabPane.getTabCount();
/* 1831 */     boolean bool = BasicGraphicsUtils.isLeftToRight(this.tabPane);
/*      */ 
/* 1834 */     if (k <= 0)
/*      */       return;
/*      */     int m;
/* 1839 */     switch (i) {
/*      */     case 2:
/*      */     case 4:
/* 1842 */       switch (paramInt) {
/*      */       case 12:
/* 1844 */         selectNextTab(j);
/* 1845 */         break;
/*      */       case 13:
/* 1847 */         selectPreviousTab(j);
/* 1848 */         break;
/*      */       case 1:
/* 1850 */         selectPreviousTabInRun(j);
/* 1851 */         break;
/*      */       case 5:
/* 1853 */         selectNextTabInRun(j);
/* 1854 */         break;
/*      */       case 7:
/* 1856 */         m = getTabRunOffset(i, k, j, false);
/* 1857 */         selectAdjacentRunTab(i, j, m);
/* 1858 */         break;
/*      */       case 3:
/* 1860 */         m = getTabRunOffset(i, k, j, true);
/* 1861 */         selectAdjacentRunTab(i, j, m);
/*      */       case 2:
/*      */       case 4:
/*      */       case 6:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/* 1865 */       case 11: } break;
/*      */     case 1:
/*      */     case 3:
/*      */     default:
/* 1869 */       switch (paramInt) {
/*      */       case 12:
/* 1871 */         selectNextTab(j);
/* 1872 */         break;
/*      */       case 13:
/* 1874 */         selectPreviousTab(j);
/* 1875 */         break;
/*      */       case 1:
/* 1877 */         m = getTabRunOffset(i, k, j, false);
/* 1878 */         selectAdjacentRunTab(i, j, m);
/* 1879 */         break;
/*      */       case 5:
/* 1881 */         m = getTabRunOffset(i, k, j, true);
/* 1882 */         selectAdjacentRunTab(i, j, m);
/* 1883 */         break;
/*      */       case 3:
/* 1885 */         if (bool)
/* 1886 */           selectNextTabInRun(j);
/*      */         else {
/* 1888 */           selectPreviousTabInRun(j);
/*      */         }
/* 1890 */         break;
/*      */       case 7:
/* 1892 */         if (bool)
/* 1893 */           selectPreviousTabInRun(j);
/*      */         else
/* 1895 */           selectNextTabInRun(j); break;
/*      */       case 2:
/*      */       case 4:
/*      */       case 6:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/* 1897 */       case 11: } break;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void selectNextTabInRun(int paramInt)
/*      */   {
/* 1904 */     int i = this.tabPane.getTabCount();
/* 1905 */     int j = getNextTabIndexInRun(i, paramInt);
/*      */ 
/* 1907 */     while ((j != paramInt) && (!this.tabPane.isEnabledAt(j))) {
/* 1908 */       j = getNextTabIndexInRun(i, j);
/*      */     }
/* 1910 */     navigateTo(j);
/*      */   }
/*      */ 
/*      */   protected void selectPreviousTabInRun(int paramInt) {
/* 1914 */     int i = this.tabPane.getTabCount();
/* 1915 */     int j = getPreviousTabIndexInRun(i, paramInt);
/*      */ 
/* 1917 */     while ((j != paramInt) && (!this.tabPane.isEnabledAt(j))) {
/* 1918 */       j = getPreviousTabIndexInRun(i, j);
/*      */     }
/* 1920 */     navigateTo(j);
/*      */   }
/*      */ 
/*      */   protected void selectNextTab(int paramInt) {
/* 1924 */     int i = getNextTabIndex(paramInt);
/*      */ 
/* 1926 */     while ((i != paramInt) && (!this.tabPane.isEnabledAt(i))) {
/* 1927 */       i = getNextTabIndex(i);
/*      */     }
/* 1929 */     navigateTo(i);
/*      */   }
/*      */ 
/*      */   protected void selectPreviousTab(int paramInt) {
/* 1933 */     int i = getPreviousTabIndex(paramInt);
/*      */ 
/* 1935 */     while ((i != paramInt) && (!this.tabPane.isEnabledAt(i))) {
/* 1936 */       i = getPreviousTabIndex(i);
/*      */     }
/* 1938 */     navigateTo(i);
/*      */   }
/*      */ 
/*      */   protected void selectAdjacentRunTab(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1943 */     if (this.runCount < 2) {
/* 1944 */       return;
/*      */     }
/*      */ 
/* 1947 */     Rectangle localRectangle = this.rects[paramInt2];
/*      */     int i;
/* 1948 */     switch (paramInt1) {
/*      */     case 2:
/*      */     case 4:
/* 1951 */       i = tabForCoordinate(this.tabPane, localRectangle.x + localRectangle.width / 2 + paramInt3, localRectangle.y + localRectangle.height / 2);
/*      */ 
/* 1953 */       break;
/*      */     case 1:
/*      */     case 3:
/*      */     default:
/* 1957 */       i = tabForCoordinate(this.tabPane, localRectangle.x + localRectangle.width / 2, localRectangle.y + localRectangle.height / 2 + paramInt3);
/*      */     }
/*      */ 
/* 1960 */     if (i != -1) {
/* 1961 */       while ((!this.tabPane.isEnabledAt(i)) && (i != paramInt2)) {
/* 1962 */         i = getNextTabIndex(i);
/*      */       }
/* 1964 */       navigateTo(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void navigateTo(int paramInt) {
/* 1969 */     if (DefaultLookup.getBoolean(this.tabPane, this, "TabbedPane.selectionFollowsFocus", true))
/*      */     {
/* 1971 */       this.tabPane.setSelectedIndex(paramInt);
/*      */     }
/*      */     else
/* 1974 */       setFocusIndex(paramInt, true);
/*      */   }
/*      */ 
/*      */   void setFocusIndex(int paramInt, boolean paramBoolean)
/*      */   {
/* 1979 */     if ((paramBoolean) && (!this.isRunsDirty)) {
/* 1980 */       repaintTab(this.focusIndex);
/* 1981 */       this.focusIndex = paramInt;
/* 1982 */       repaintTab(this.focusIndex);
/*      */     }
/*      */     else {
/* 1985 */       this.focusIndex = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void repaintTab(int paramInt)
/*      */   {
/* 1995 */     if ((!this.isRunsDirty) && (paramInt >= 0) && (paramInt < this.tabPane.getTabCount()))
/* 1996 */       this.tabPane.repaint(getTabBounds(this.tabPane, paramInt));
/*      */   }
/*      */ 
/*      */   private void validateFocusIndex()
/*      */   {
/* 2004 */     if (this.focusIndex >= this.tabPane.getTabCount())
/* 2005 */       setFocusIndex(this.tabPane.getSelectedIndex(), false);
/*      */   }
/*      */ 
/*      */   protected int getFocusIndex()
/*      */   {
/* 2016 */     return this.focusIndex;
/*      */   }
/*      */ 
/*      */   protected int getTabRunOffset(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*      */   {
/* 2021 */     int i = getRunForTab(paramInt2, paramInt3);
/*      */     int j;
/* 2023 */     switch (paramInt1) {
/*      */     case 2:
/* 2025 */       if (i == 0) {
/* 2026 */         j = paramBoolean ? -(calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth) - this.maxTabWidth) : -this.maxTabWidth;
/*      */       }
/* 2030 */       else if (i == this.runCount - 1) {
/* 2031 */         j = paramBoolean ? this.maxTabWidth : calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth) - this.maxTabWidth;
/*      */       }
/*      */       else
/*      */       {
/* 2035 */         j = paramBoolean ? this.maxTabWidth : -this.maxTabWidth;
/*      */       }
/* 2037 */       break;
/*      */     case 4:
/* 2040 */       if (i == 0) {
/* 2041 */         j = paramBoolean ? this.maxTabWidth : calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth) - this.maxTabWidth;
/*      */       }
/* 2044 */       else if (i == this.runCount - 1) {
/* 2045 */         j = paramBoolean ? -(calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth) - this.maxTabWidth) : -this.maxTabWidth;
/*      */       }
/*      */       else
/*      */       {
/* 2049 */         j = paramBoolean ? this.maxTabWidth : -this.maxTabWidth;
/*      */       }
/* 2051 */       break;
/*      */     case 3:
/* 2054 */       if (i == 0) {
/* 2055 */         j = paramBoolean ? this.maxTabHeight : calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight) - this.maxTabHeight;
/*      */       }
/* 2058 */       else if (i == this.runCount - 1) {
/* 2059 */         j = paramBoolean ? -(calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight) - this.maxTabHeight) : -this.maxTabHeight;
/*      */       }
/*      */       else
/*      */       {
/* 2063 */         j = paramBoolean ? this.maxTabHeight : -this.maxTabHeight;
/*      */       }
/* 2065 */       break;
/*      */     case 1:
/*      */     default:
/* 2069 */       if (i == 0) {
/* 2070 */         j = paramBoolean ? -(calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight) - this.maxTabHeight) : -this.maxTabHeight;
/*      */       }
/* 2073 */       else if (i == this.runCount - 1) {
/* 2074 */         j = paramBoolean ? this.maxTabHeight : calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight) - this.maxTabHeight;
/*      */       }
/*      */       else
/*      */       {
/* 2078 */         j = paramBoolean ? this.maxTabHeight : -this.maxTabHeight;
/*      */       }
/*      */       break;
/*      */     }
/* 2082 */     return j;
/*      */   }
/*      */ 
/*      */   protected int getPreviousTabIndex(int paramInt) {
/* 2086 */     int i = paramInt - 1 >= 0 ? paramInt - 1 : this.tabPane.getTabCount() - 1;
/* 2087 */     return i >= 0 ? i : 0;
/*      */   }
/*      */ 
/*      */   protected int getNextTabIndex(int paramInt) {
/* 2091 */     return (paramInt + 1) % this.tabPane.getTabCount();
/*      */   }
/*      */ 
/*      */   protected int getNextTabIndexInRun(int paramInt1, int paramInt2) {
/* 2095 */     if (this.runCount < 2) {
/* 2096 */       return getNextTabIndex(paramInt2);
/*      */     }
/* 2098 */     int i = getRunForTab(paramInt1, paramInt2);
/* 2099 */     int j = getNextTabIndex(paramInt2);
/* 2100 */     if (j == this.tabRuns[getNextTabRun(i)]) {
/* 2101 */       return this.tabRuns[i];
/*      */     }
/* 2103 */     return j;
/*      */   }
/*      */ 
/*      */   protected int getPreviousTabIndexInRun(int paramInt1, int paramInt2) {
/* 2107 */     if (this.runCount < 2) {
/* 2108 */       return getPreviousTabIndex(paramInt2);
/*      */     }
/* 2110 */     int i = getRunForTab(paramInt1, paramInt2);
/* 2111 */     if (paramInt2 == this.tabRuns[i]) {
/* 2112 */       int j = this.tabRuns[getNextTabRun(i)] - 1;
/* 2113 */       return j != -1 ? j : paramInt1 - 1;
/*      */     }
/* 2115 */     return getPreviousTabIndex(paramInt2);
/*      */   }
/*      */ 
/*      */   protected int getPreviousTabRun(int paramInt) {
/* 2119 */     int i = paramInt - 1 >= 0 ? paramInt - 1 : this.runCount - 1;
/* 2120 */     return i >= 0 ? i : 0;
/*      */   }
/*      */ 
/*      */   protected int getNextTabRun(int paramInt) {
/* 2124 */     return (paramInt + 1) % this.runCount;
/*      */   }
/*      */ 
/*      */   protected static void rotateInsets(Insets paramInsets1, Insets paramInsets2, int paramInt)
/*      */   {
/* 2129 */     switch (paramInt) {
/*      */     case 2:
/* 2131 */       paramInsets2.top = paramInsets1.left;
/* 2132 */       paramInsets2.left = paramInsets1.top;
/* 2133 */       paramInsets2.bottom = paramInsets1.right;
/* 2134 */       paramInsets2.right = paramInsets1.bottom;
/* 2135 */       break;
/*      */     case 3:
/* 2137 */       paramInsets2.top = paramInsets1.bottom;
/* 2138 */       paramInsets2.left = paramInsets1.left;
/* 2139 */       paramInsets2.bottom = paramInsets1.top;
/* 2140 */       paramInsets2.right = paramInsets1.right;
/* 2141 */       break;
/*      */     case 4:
/* 2143 */       paramInsets2.top = paramInsets1.left;
/* 2144 */       paramInsets2.left = paramInsets1.bottom;
/* 2145 */       paramInsets2.bottom = paramInsets1.right;
/* 2146 */       paramInsets2.right = paramInsets1.top;
/* 2147 */       break;
/*      */     case 1:
/*      */     default:
/* 2150 */       paramInsets2.top = paramInsets1.top;
/* 2151 */       paramInsets2.left = paramInsets1.left;
/* 2152 */       paramInsets2.bottom = paramInsets1.bottom;
/* 2153 */       paramInsets2.right = paramInsets1.right;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean requestFocusForVisibleComponent()
/*      */   {
/* 2161 */     return SwingUtilities2.tabbedPaneChangeFocusTo(getVisibleComponent());
/*      */   }
/*      */ 
/*      */   private Vector<View> createHTMLVector()
/*      */   {
/* 3805 */     Vector localVector = new Vector();
/* 3806 */     int i = this.tabPane.getTabCount();
/* 3807 */     if (i > 0) {
/* 3808 */       for (int j = 0; j < i; j++) {
/* 3809 */         String str = this.tabPane.getTitleAt(j);
/* 3810 */         if (BasicHTML.isHTMLString(str))
/* 3811 */           localVector.addElement(BasicHTML.createHTMLView(this.tabPane, str));
/*      */         else {
/* 3813 */           localVector.addElement(null);
/*      */         }
/*      */       }
/*      */     }
/* 3817 */     return localVector;
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     static final String NEXT = "navigateNext";
/*      */     static final String PREVIOUS = "navigatePrevious";
/*      */     static final String RIGHT = "navigateRight";
/*      */     static final String LEFT = "navigateLeft";
/*      */     static final String UP = "navigateUp";
/*      */     static final String DOWN = "navigateDown";
/*      */     static final String PAGE_UP = "navigatePageUp";
/*      */     static final String PAGE_DOWN = "navigatePageDown";
/*      */     static final String REQUEST_FOCUS = "requestFocus";
/*      */     static final String REQUEST_FOCUS_FOR_VISIBLE = "requestFocusForVisibleComponent";
/*      */     static final String SET_SELECTED = "setSelectedIndex";
/*      */     static final String SELECT_FOCUSED = "selectTabWithFocus";
/*      */     static final String SCROLL_FORWARD = "scrollTabsForwardAction";
/*      */     static final String SCROLL_BACKWARD = "scrollTabsBackwardAction";
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/* 2182 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2186 */       String str1 = getName();
/* 2187 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramActionEvent.getSource();
/* 2188 */       BasicTabbedPaneUI localBasicTabbedPaneUI = (BasicTabbedPaneUI)BasicLookAndFeel.getUIOfType(localJTabbedPane.getUI(), BasicTabbedPaneUI.class);
/*      */ 
/* 2191 */       if (localBasicTabbedPaneUI == null) {
/* 2192 */         return;
/*      */       }
/* 2194 */       if (str1 == "navigateNext") {
/* 2195 */         localBasicTabbedPaneUI.navigateSelectedTab(12);
/*      */       }
/* 2197 */       else if (str1 == "navigatePrevious") {
/* 2198 */         localBasicTabbedPaneUI.navigateSelectedTab(13);
/*      */       }
/* 2200 */       else if (str1 == "navigateRight") {
/* 2201 */         localBasicTabbedPaneUI.navigateSelectedTab(3);
/*      */       }
/* 2203 */       else if (str1 == "navigateLeft") {
/* 2204 */         localBasicTabbedPaneUI.navigateSelectedTab(7);
/*      */       }
/* 2206 */       else if (str1 == "navigateUp") {
/* 2207 */         localBasicTabbedPaneUI.navigateSelectedTab(1);
/*      */       }
/* 2209 */       else if (str1 == "navigateDown") {
/* 2210 */         localBasicTabbedPaneUI.navigateSelectedTab(5);
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/* 2212 */         if (str1 == "navigatePageUp") {
/* 2213 */           i = localJTabbedPane.getTabPlacement();
/* 2214 */           if ((i == 1) || (i == 3))
/* 2215 */             localBasicTabbedPaneUI.navigateSelectedTab(7);
/*      */           else {
/* 2217 */             localBasicTabbedPaneUI.navigateSelectedTab(1);
/*      */           }
/*      */         }
/* 2220 */         else if (str1 == "navigatePageDown") {
/* 2221 */           i = localJTabbedPane.getTabPlacement();
/* 2222 */           if ((i == 1) || (i == 3))
/* 2223 */             localBasicTabbedPaneUI.navigateSelectedTab(3);
/*      */           else {
/* 2225 */             localBasicTabbedPaneUI.navigateSelectedTab(5);
/*      */           }
/*      */         }
/* 2228 */         else if (str1 == "requestFocus") {
/* 2229 */           localJTabbedPane.requestFocus();
/*      */         }
/* 2231 */         else if (str1 == "requestFocusForVisibleComponent") {
/* 2232 */           localBasicTabbedPaneUI.requestFocusForVisibleComponent();
/*      */         }
/* 2234 */         else if (str1 == "setSelectedIndex") {
/* 2235 */           String str2 = paramActionEvent.getActionCommand();
/*      */ 
/* 2237 */           if ((str2 != null) && (str2.length() > 0)) {
/* 2238 */             int k = paramActionEvent.getActionCommand().charAt(0);
/* 2239 */             if ((k >= 97) && (k <= 122)) {
/* 2240 */               k -= 32;
/*      */             }
/* 2242 */             Integer localInteger = (Integer)localBasicTabbedPaneUI.mnemonicToIndexMap.get(Integer.valueOf(k));
/* 2243 */             if ((localInteger != null) && (localJTabbedPane.isEnabledAt(localInteger.intValue()))) {
/* 2244 */               localJTabbedPane.setSelectedIndex(localInteger.intValue());
/*      */             }
/*      */           }
/*      */         }
/* 2248 */         else if (str1 == "selectTabWithFocus") {
/* 2249 */           int j = localBasicTabbedPaneUI.getFocusIndex();
/* 2250 */           if (j != -1) {
/* 2251 */             localJTabbedPane.setSelectedIndex(j);
/*      */           }
/*      */         }
/* 2254 */         else if (str1 == "scrollTabsForwardAction") {
/* 2255 */           if (localBasicTabbedPaneUI.scrollableTabLayoutEnabled()) {
/* 2256 */             localBasicTabbedPaneUI.tabScroller.scrollForward(localJTabbedPane.getTabPlacement());
/*      */           }
/*      */         }
/* 2259 */         else if ((str1 == "scrollTabsBackwardAction") && 
/* 2260 */           (localBasicTabbedPaneUI.scrollableTabLayoutEnabled())) {
/* 2261 */           localBasicTabbedPaneUI.tabScroller.scrollBackward(localJTabbedPane.getTabPlacement());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class CroppedEdge extends JPanel
/*      */     implements UIResource
/*      */   {
/*      */     private Shape shape;
/*      */     private int tabIndex;
/*      */     private int cropline;
/*      */     private int cropx;
/*      */     private int cropy;
/*      */ 
/*      */     public CroppedEdge()
/*      */     {
/* 3871 */       setOpaque(false);
/*      */     }
/*      */ 
/*      */     public void setParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 3875 */       this.tabIndex = paramInt1;
/* 3876 */       this.cropline = paramInt2;
/* 3877 */       this.cropx = paramInt3;
/* 3878 */       this.cropy = paramInt4;
/* 3879 */       Rectangle localRectangle = BasicTabbedPaneUI.this.rects[paramInt1];
/* 3880 */       setBounds(localRectangle);
/* 3881 */       this.shape = BasicTabbedPaneUI.createCroppedTabShape(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), localRectangle, paramInt2);
/* 3882 */       if ((getParent() == null) && (BasicTabbedPaneUI.this.tabContainer != null))
/* 3883 */         BasicTabbedPaneUI.this.tabContainer.add(this, 0);
/*      */     }
/*      */ 
/*      */     public void resetParams()
/*      */     {
/* 3888 */       this.shape = null;
/* 3889 */       if ((getParent() == BasicTabbedPaneUI.this.tabContainer) && (BasicTabbedPaneUI.this.tabContainer != null))
/* 3890 */         BasicTabbedPaneUI.this.tabContainer.remove(this);
/*      */     }
/*      */ 
/*      */     public boolean isParamsSet()
/*      */     {
/* 3895 */       return this.shape != null;
/*      */     }
/*      */ 
/*      */     public int getTabIndex() {
/* 3899 */       return this.tabIndex;
/*      */     }
/*      */ 
/*      */     public int getCropline() {
/* 3903 */       return this.cropline;
/*      */     }
/*      */ 
/*      */     public int getCroppedSideWidth() {
/* 3907 */       return 3;
/*      */     }
/*      */ 
/*      */     private Color getBgColor() {
/* 3911 */       Container localContainer = BasicTabbedPaneUI.this.tabPane.getParent();
/* 3912 */       if (localContainer != null) {
/* 3913 */         Color localColor = localContainer.getBackground();
/* 3914 */         if (localColor != null) {
/* 3915 */           return localColor;
/*      */         }
/*      */       }
/* 3918 */       return UIManager.getColor("control");
/*      */     }
/*      */ 
/*      */     protected void paintComponent(Graphics paramGraphics) {
/* 3922 */       super.paintComponent(paramGraphics);
/* 3923 */       if ((isParamsSet()) && ((paramGraphics instanceof Graphics2D))) {
/* 3924 */         Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 3925 */         localGraphics2D.clipRect(0, 0, getWidth(), getHeight());
/* 3926 */         localGraphics2D.setColor(getBgColor());
/* 3927 */         localGraphics2D.translate(this.cropx, this.cropy);
/* 3928 */         localGraphics2D.fill(this.shape);
/* 3929 */         BasicTabbedPaneUI.this.paintCroppedTabEdge(paramGraphics);
/* 3930 */         localGraphics2D.translate(-this.cropx, -this.cropy);
/*      */       }
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
/* 3797 */       BasicTabbedPaneUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 3800 */       BasicTabbedPaneUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ChangeListener, ContainerListener, FocusListener, MouseListener, MouseMotionListener, PropertyChangeListener
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 3516 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramPropertyChangeEvent.getSource();
/* 3517 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 3518 */       boolean bool1 = BasicTabbedPaneUI.this.scrollableTabLayoutEnabled();
/* 3519 */       if (str == "mnemonicAt") {
/* 3520 */         BasicTabbedPaneUI.this.updateMnemonics();
/* 3521 */         localJTabbedPane.repaint();
/*      */       }
/* 3523 */       else if (str == "displayedMnemonicIndexAt") {
/* 3524 */         localJTabbedPane.repaint();
/*      */       }
/* 3526 */       else if (str == "indexForTitle") {
/* 3527 */         BasicTabbedPaneUI.this.calculatedBaseline = false;
/* 3528 */         Integer localInteger = (Integer)paramPropertyChangeEvent.getNewValue();
/*      */ 
/* 3531 */         if (BasicTabbedPaneUI.this.htmlViews != null) {
/* 3532 */           BasicTabbedPaneUI.this.htmlViews.removeElementAt(localInteger.intValue());
/*      */         }
/* 3534 */         updateHtmlViews(localInteger.intValue());
/* 3535 */       } else if (str == "tabLayoutPolicy") {
/* 3536 */         BasicTabbedPaneUI.this.uninstallUI(localJTabbedPane);
/* 3537 */         BasicTabbedPaneUI.this.installUI(localJTabbedPane);
/* 3538 */         BasicTabbedPaneUI.this.calculatedBaseline = false;
/* 3539 */       } else if (str == "tabPlacement") {
/* 3540 */         if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
/* 3541 */           BasicTabbedPaneUI.this.tabScroller.createButtons();
/*      */         }
/* 3543 */         BasicTabbedPaneUI.this.calculatedBaseline = false;
/* 3544 */       } else if ((str == "opaque") && (bool1)) {
/* 3545 */         boolean bool2 = ((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue();
/* 3546 */         BasicTabbedPaneUI.this.tabScroller.tabPanel.setOpaque(bool2);
/* 3547 */         BasicTabbedPaneUI.this.tabScroller.viewport.setOpaque(bool2);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject;
/* 3548 */         if ((str == "background") && (bool1)) {
/* 3549 */           localObject = (Color)paramPropertyChangeEvent.getNewValue();
/* 3550 */           BasicTabbedPaneUI.this.tabScroller.tabPanel.setBackground((Color)localObject);
/* 3551 */           BasicTabbedPaneUI.this.tabScroller.viewport.setBackground((Color)localObject);
/* 3552 */           Color localColor = BasicTabbedPaneUI.this.selectedColor == null ? localObject : BasicTabbedPaneUI.this.selectedColor;
/* 3553 */           BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.setBackground(localColor);
/* 3554 */           BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton.setBackground(localColor);
/* 3555 */         } else if (str == "indexForTabComponent") {
/* 3556 */           if (BasicTabbedPaneUI.this.tabContainer != null) {
/* 3557 */             BasicTabbedPaneUI.TabContainer.access$1700(BasicTabbedPaneUI.this.tabContainer);
/*      */           }
/* 3559 */           localObject = BasicTabbedPaneUI.this.tabPane.getTabComponentAt(((Integer)paramPropertyChangeEvent.getNewValue()).intValue());
/*      */ 
/* 3561 */           if (localObject != null) {
/* 3562 */             if (BasicTabbedPaneUI.this.tabContainer == null)
/* 3563 */               BasicTabbedPaneUI.this.installTabContainer();
/*      */             else {
/* 3565 */               BasicTabbedPaneUI.this.tabContainer.add((Component)localObject);
/*      */             }
/*      */           }
/* 3568 */           BasicTabbedPaneUI.this.tabPane.revalidate();
/* 3569 */           BasicTabbedPaneUI.this.tabPane.repaint();
/* 3570 */           BasicTabbedPaneUI.this.calculatedBaseline = false;
/* 3571 */         } else if (str == "indexForNullComponent") {
/* 3572 */           BasicTabbedPaneUI.this.isRunsDirty = true;
/* 3573 */           updateHtmlViews(((Integer)paramPropertyChangeEvent.getNewValue()).intValue());
/* 3574 */         } else if (str == "font") {
/* 3575 */           BasicTabbedPaneUI.this.calculatedBaseline = false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3580 */     private void updateHtmlViews(int paramInt) { String str = BasicTabbedPaneUI.this.tabPane.getTitleAt(paramInt);
/* 3581 */       boolean bool = BasicHTML.isHTMLString(str);
/* 3582 */       if (bool) {
/* 3583 */         if (BasicTabbedPaneUI.this.htmlViews == null) {
/* 3584 */           BasicTabbedPaneUI.this.htmlViews = BasicTabbedPaneUI.this.createHTMLVector();
/*      */         } else {
/* 3586 */           View localView = BasicHTML.createHTMLView(BasicTabbedPaneUI.this.tabPane, str);
/* 3587 */           BasicTabbedPaneUI.this.htmlViews.insertElementAt(localView, paramInt);
/*      */         }
/*      */       }
/* 3590 */       else if (BasicTabbedPaneUI.this.htmlViews != null) {
/* 3591 */         BasicTabbedPaneUI.this.htmlViews.insertElementAt(null, paramInt);
/*      */       }
/*      */ 
/* 3594 */       BasicTabbedPaneUI.this.updateMnemonics();
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 3601 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramChangeEvent.getSource();
/* 3602 */       localJTabbedPane.revalidate();
/* 3603 */       localJTabbedPane.repaint();
/*      */ 
/* 3605 */       BasicTabbedPaneUI.this.setFocusIndex(localJTabbedPane.getSelectedIndex(), false);
/*      */ 
/* 3607 */       if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
/* 3608 */         int i = localJTabbedPane.getSelectedIndex();
/* 3609 */         if ((i < BasicTabbedPaneUI.this.rects.length) && (i != -1))
/* 3610 */           BasicTabbedPaneUI.this.tabScroller.tabPanel.scrollRectToVisible((Rectangle)BasicTabbedPaneUI.this.rects[i].clone());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/* 3626 */       BasicTabbedPaneUI.this.setRolloverTab(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 3630 */       BasicTabbedPaneUI.this.setRolloverTab(-1);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 3634 */       if (!BasicTabbedPaneUI.this.tabPane.isEnabled()) {
/* 3635 */         return;
/*      */       }
/* 3637 */       int i = BasicTabbedPaneUI.this.tabForCoordinate(BasicTabbedPaneUI.this.tabPane, paramMouseEvent.getX(), paramMouseEvent.getY());
/* 3638 */       if ((i >= 0) && (BasicTabbedPaneUI.this.tabPane.isEnabledAt(i)))
/* 3639 */         if (i != BasicTabbedPaneUI.this.tabPane.getSelectedIndex())
/*      */         {
/* 3644 */           BasicTabbedPaneUI.this.tabPane.setSelectedIndex(i);
/*      */         }
/* 3646 */         else if (BasicTabbedPaneUI.this.tabPane.isRequestFocusEnabled())
/*      */         {
/* 3649 */           BasicTabbedPaneUI.this.tabPane.requestFocus();
/*      */         }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/* 3661 */       BasicTabbedPaneUI.this.setRolloverTab(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 3668 */       BasicTabbedPaneUI.this.setFocusIndex(BasicTabbedPaneUI.this.tabPane.getSelectedIndex(), true);
/*      */     }
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 3671 */       BasicTabbedPaneUI.this.repaintTab(BasicTabbedPaneUI.this.focusIndex);
/*      */     }
/*      */ 
/*      */     public void componentAdded(ContainerEvent paramContainerEvent)
/*      */     {
/* 3709 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramContainerEvent.getContainer();
/* 3710 */       Component localComponent = paramContainerEvent.getChild();
/* 3711 */       if ((localComponent instanceof UIResource)) {
/* 3712 */         return;
/*      */       }
/* 3714 */       BasicTabbedPaneUI.this.isRunsDirty = true;
/* 3715 */       updateHtmlViews(localJTabbedPane.indexOfComponent(localComponent));
/*      */     }
/*      */     public void componentRemoved(ContainerEvent paramContainerEvent) {
/* 3718 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramContainerEvent.getContainer();
/* 3719 */       Component localComponent = paramContainerEvent.getChild();
/* 3720 */       if ((localComponent instanceof UIResource)) {
/* 3721 */         return;
/*      */       }
/*      */ 
/* 3729 */       Integer localInteger = (Integer)localJTabbedPane.getClientProperty("__index_to_remove__");
/*      */ 
/* 3731 */       if (localInteger != null) {
/* 3732 */         int i = localInteger.intValue();
/* 3733 */         if ((BasicTabbedPaneUI.this.htmlViews != null) && (BasicTabbedPaneUI.this.htmlViews.size() > i)) {
/* 3734 */           BasicTabbedPaneUI.this.htmlViews.removeElementAt(i);
/*      */         }
/* 3736 */         localJTabbedPane.putClientProperty("__index_to_remove__", null);
/*      */       }
/* 3738 */       BasicTabbedPaneUI.this.isRunsDirty = true;
/* 3739 */       BasicTabbedPaneUI.this.updateMnemonics();
/*      */ 
/* 3741 */       BasicTabbedPaneUI.this.validateFocusIndex();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class MouseHandler extends MouseAdapter
/*      */   {
/*      */     public MouseHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 3783 */       BasicTabbedPaneUI.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public PropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 3755 */       BasicTabbedPaneUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ScrollableTabButton extends BasicArrowButton
/*      */     implements UIResource, SwingConstants
/*      */   {
/*      */     public ScrollableTabButton(int arg2)
/*      */     {
/* 3498 */       super(UIManager.getColor("TabbedPane.selected"), UIManager.getColor("TabbedPane.shadow"), UIManager.getColor("TabbedPane.darkShadow"), UIManager.getColor("TabbedPane.highlight"));
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ScrollableTabPanel extends JPanel
/*      */     implements UIResource
/*      */   {
/*      */     public ScrollableTabPanel()
/*      */     {
/* 3467 */       super();
/* 3468 */       setOpaque(BasicTabbedPaneUI.this.tabPane.isOpaque());
/* 3469 */       Color localColor = UIManager.getColor("TabbedPane.tabAreaBackground");
/* 3470 */       if (localColor == null) {
/* 3471 */         localColor = BasicTabbedPaneUI.this.tabPane.getBackground();
/*      */       }
/* 3473 */       setBackground(localColor);
/*      */     }
/*      */     public void paintComponent(Graphics paramGraphics) {
/* 3476 */       super.paintComponent(paramGraphics);
/* 3477 */       BasicTabbedPaneUI.this.paintTabArea(paramGraphics, BasicTabbedPaneUI.this.tabPane.getTabPlacement(), BasicTabbedPaneUI.this.tabPane.getSelectedIndex());
/*      */ 
/* 3479 */       if ((BasicTabbedPaneUI.this.tabScroller.croppedEdge.isParamsSet()) && (BasicTabbedPaneUI.this.tabContainer == null)) {
/* 3480 */         Rectangle localRectangle = BasicTabbedPaneUI.this.rects[BasicTabbedPaneUI.this.tabScroller.croppedEdge.getTabIndex()];
/* 3481 */         paramGraphics.translate(localRectangle.x, localRectangle.y);
/* 3482 */         BasicTabbedPaneUI.this.tabScroller.croppedEdge.paintComponent(paramGraphics);
/* 3483 */         paramGraphics.translate(-localRectangle.x, -localRectangle.y);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void doLayout() {
/* 3488 */       if (getComponentCount() > 0) {
/* 3489 */         Component localComponent = getComponent(0);
/* 3490 */         localComponent.setBounds(0, 0, getWidth(), getHeight());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ScrollableTabSupport
/*      */     implements ActionListener, ChangeListener
/*      */   {
/*      */     public BasicTabbedPaneUI.ScrollableTabViewport viewport;
/*      */     public BasicTabbedPaneUI.ScrollableTabPanel tabPanel;
/*      */     public JButton scrollForwardButton;
/*      */     public JButton scrollBackwardButton;
/*      */     public BasicTabbedPaneUI.CroppedEdge croppedEdge;
/*      */     public int leadingTabIndex;
/* 3254 */     private Point tabViewPosition = new Point(0, 0);
/*      */ 
/*      */     ScrollableTabSupport(int arg2) {
/* 3257 */       this.viewport = new BasicTabbedPaneUI.ScrollableTabViewport(BasicTabbedPaneUI.this);
/* 3258 */       this.tabPanel = new BasicTabbedPaneUI.ScrollableTabPanel(BasicTabbedPaneUI.this);
/* 3259 */       this.viewport.setView(this.tabPanel);
/* 3260 */       this.viewport.addChangeListener(this);
/* 3261 */       this.croppedEdge = new BasicTabbedPaneUI.CroppedEdge(BasicTabbedPaneUI.this);
/* 3262 */       createButtons();
/*      */     }
/*      */ 
/*      */     void createButtons()
/*      */     {
/* 3269 */       if (this.scrollForwardButton != null) {
/* 3270 */         BasicTabbedPaneUI.this.tabPane.remove(this.scrollForwardButton);
/* 3271 */         this.scrollForwardButton.removeActionListener(this);
/* 3272 */         BasicTabbedPaneUI.this.tabPane.remove(this.scrollBackwardButton);
/* 3273 */         this.scrollBackwardButton.removeActionListener(this);
/*      */       }
/* 3275 */       int i = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 3276 */       if ((i == 1) || (i == 3)) {
/* 3277 */         this.scrollForwardButton = BasicTabbedPaneUI.this.createScrollButton(3);
/* 3278 */         this.scrollBackwardButton = BasicTabbedPaneUI.this.createScrollButton(7);
/*      */       }
/*      */       else {
/* 3281 */         this.scrollForwardButton = BasicTabbedPaneUI.this.createScrollButton(5);
/* 3282 */         this.scrollBackwardButton = BasicTabbedPaneUI.this.createScrollButton(1);
/*      */       }
/* 3284 */       this.scrollForwardButton.addActionListener(this);
/* 3285 */       this.scrollBackwardButton.addActionListener(this);
/* 3286 */       BasicTabbedPaneUI.this.tabPane.add(this.scrollForwardButton);
/* 3287 */       BasicTabbedPaneUI.this.tabPane.add(this.scrollBackwardButton);
/*      */     }
/*      */ 
/*      */     public void scrollForward(int paramInt) {
/* 3291 */       Dimension localDimension = this.viewport.getViewSize();
/* 3292 */       Rectangle localRectangle = this.viewport.getViewRect();
/*      */ 
/* 3294 */       if ((paramInt == 1) || (paramInt == 3))
/*      */       {
/* 3295 */         if (localRectangle.width < localDimension.width - localRectangle.x);
/*      */       }
/* 3299 */       else if (localRectangle.height >= localDimension.height - localRectangle.y) {
/* 3300 */         return;
/*      */       }
/*      */ 
/* 3303 */       setLeadingTabIndex(paramInt, this.leadingTabIndex + 1);
/*      */     }
/*      */ 
/*      */     public void scrollBackward(int paramInt) {
/* 3307 */       if (this.leadingTabIndex == 0) {
/* 3308 */         return;
/*      */       }
/* 3310 */       setLeadingTabIndex(paramInt, this.leadingTabIndex - 1);
/*      */     }
/*      */ 
/*      */     public void setLeadingTabIndex(int paramInt1, int paramInt2) {
/* 3314 */       this.leadingTabIndex = paramInt2;
/* 3315 */       Dimension localDimension1 = this.viewport.getViewSize();
/* 3316 */       Rectangle localRectangle = this.viewport.getViewRect();
/*      */       Dimension localDimension2;
/* 3318 */       switch (paramInt1) {
/*      */       case 1:
/*      */       case 3:
/* 3321 */         this.tabViewPosition.x = (this.leadingTabIndex == 0 ? 0 : BasicTabbedPaneUI.this.rects[this.leadingTabIndex].x);
/*      */ 
/* 3323 */         if (localDimension1.width - this.tabViewPosition.x < localRectangle.width)
/*      */         {
/* 3326 */           localDimension2 = new Dimension(localDimension1.width - this.tabViewPosition.x, localRectangle.height);
/*      */ 
/* 3328 */           this.viewport.setExtentSize(localDimension2);
/* 3329 */         }break;
/*      */       case 2:
/*      */       case 4:
/* 3333 */         this.tabViewPosition.y = (this.leadingTabIndex == 0 ? 0 : BasicTabbedPaneUI.this.rects[this.leadingTabIndex].y);
/*      */ 
/* 3335 */         if (localDimension1.height - this.tabViewPosition.y < localRectangle.height)
/*      */         {
/* 3338 */           localDimension2 = new Dimension(localRectangle.width, localDimension1.height - this.tabViewPosition.y);
/*      */ 
/* 3340 */           this.viewport.setExtentSize(localDimension2);
/*      */         }break;
/*      */       }
/* 3343 */       this.viewport.setViewPosition(this.tabViewPosition);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent) {
/* 3347 */       updateView();
/*      */     }
/*      */ 
/*      */     private void updateView() {
/* 3351 */       int i = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 3352 */       int j = BasicTabbedPaneUI.this.tabPane.getTabCount();
/* 3353 */       Rectangle localRectangle1 = this.viewport.getBounds();
/* 3354 */       Dimension localDimension = this.viewport.getViewSize();
/* 3355 */       Rectangle localRectangle2 = this.viewport.getViewRect();
/*      */ 
/* 3357 */       this.leadingTabIndex = BasicTabbedPaneUI.this.getClosestTab(localRectangle2.x, localRectangle2.y);
/*      */ 
/* 3360 */       if (this.leadingTabIndex + 1 < j) {
/* 3361 */         switch (i) {
/*      */         case 1:
/*      */         case 3:
/* 3364 */           if (BasicTabbedPaneUI.this.rects[this.leadingTabIndex].x < localRectangle2.x)
/* 3365 */             this.leadingTabIndex += 1; break;
/*      */         case 2:
/*      */         case 4:
/* 3370 */           if (BasicTabbedPaneUI.this.rects[this.leadingTabIndex].y < localRectangle2.y) {
/* 3371 */             this.leadingTabIndex += 1;
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/* 3376 */       Insets localInsets = BasicTabbedPaneUI.this.getContentBorderInsets(i);
/* 3377 */       switch (i) {
/*      */       case 2:
/* 3379 */         BasicTabbedPaneUI.this.tabPane.repaint(localRectangle1.x + localRectangle1.width, localRectangle1.y, localInsets.left, localRectangle1.height);
/*      */ 
/* 3381 */         this.scrollBackwardButton.setEnabled((localRectangle2.y > 0) && (this.leadingTabIndex > 0));
/*      */ 
/* 3383 */         this.scrollForwardButton.setEnabled((this.leadingTabIndex < j - 1) && (localDimension.height - localRectangle2.y > localRectangle2.height));
/*      */ 
/* 3386 */         break;
/*      */       case 4:
/* 3388 */         BasicTabbedPaneUI.this.tabPane.repaint(localRectangle1.x - localInsets.right, localRectangle1.y, localInsets.right, localRectangle1.height);
/*      */ 
/* 3390 */         this.scrollBackwardButton.setEnabled((localRectangle2.y > 0) && (this.leadingTabIndex > 0));
/*      */ 
/* 3392 */         this.scrollForwardButton.setEnabled((this.leadingTabIndex < j - 1) && (localDimension.height - localRectangle2.y > localRectangle2.height));
/*      */ 
/* 3395 */         break;
/*      */       case 3:
/* 3397 */         BasicTabbedPaneUI.this.tabPane.repaint(localRectangle1.x, localRectangle1.y - localInsets.bottom, localRectangle1.width, localInsets.bottom);
/*      */ 
/* 3399 */         this.scrollBackwardButton.setEnabled((localRectangle2.x > 0) && (this.leadingTabIndex > 0));
/*      */ 
/* 3401 */         this.scrollForwardButton.setEnabled((this.leadingTabIndex < j - 1) && (localDimension.width - localRectangle2.x > localRectangle2.width));
/*      */ 
/* 3404 */         break;
/*      */       case 1:
/*      */       default:
/* 3407 */         BasicTabbedPaneUI.this.tabPane.repaint(localRectangle1.x, localRectangle1.y + localRectangle1.height, localRectangle1.width, localInsets.top);
/*      */ 
/* 3409 */         this.scrollBackwardButton.setEnabled((localRectangle2.x > 0) && (this.leadingTabIndex > 0));
/*      */ 
/* 3411 */         this.scrollForwardButton.setEnabled((this.leadingTabIndex < j - 1) && (localDimension.width - localRectangle2.x > localRectangle2.width));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 3421 */       ActionMap localActionMap = BasicTabbedPaneUI.this.tabPane.getActionMap();
/*      */ 
/* 3423 */       if (localActionMap != null)
/*      */       {
/*      */         String str;
/* 3426 */         if (paramActionEvent.getSource() == this.scrollForwardButton) {
/* 3427 */           str = "scrollTabsForwardAction";
/*      */         }
/*      */         else {
/* 3430 */           str = "scrollTabsBackwardAction";
/*      */         }
/* 3432 */         Action localAction = localActionMap.get(str);
/*      */ 
/* 3434 */         if ((localAction != null) && (localAction.isEnabled()))
/* 3435 */           localAction.actionPerformed(new ActionEvent(BasicTabbedPaneUI.this.tabPane, 1001, null, paramActionEvent.getWhen(), paramActionEvent.getModifiers()));
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 3443 */       return "viewport.viewSize=" + this.viewport.getViewSize() + "\n" + "viewport.viewRectangle=" + this.viewport.getViewRect() + "\n" + "leadingTabIndex=" + this.leadingTabIndex + "\n" + "tabViewPosition=" + this.tabViewPosition;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ScrollableTabViewport extends JViewport
/*      */     implements UIResource
/*      */   {
/*      */     public ScrollableTabViewport()
/*      */     {
/* 3454 */       setName("TabbedPane.scrollableViewport");
/* 3455 */       setScrollMode(0);
/* 3456 */       setOpaque(BasicTabbedPaneUI.this.tabPane.isOpaque());
/* 3457 */       Color localColor = UIManager.getColor("TabbedPane.tabAreaBackground");
/* 3458 */       if (localColor == null) {
/* 3459 */         localColor = BasicTabbedPaneUI.this.tabPane.getBackground();
/*      */       }
/* 3461 */       setBackground(localColor);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TabContainer extends JPanel
/*      */     implements UIResource
/*      */   {
/* 3821 */     private boolean notifyTabbedPane = true;
/*      */ 
/*      */     public TabContainer() {
/* 3824 */       super();
/* 3825 */       setOpaque(false);
/*      */     }
/*      */ 
/*      */     public void remove(Component paramComponent) {
/* 3829 */       int i = BasicTabbedPaneUI.this.tabPane.indexOfTabComponent(paramComponent);
/* 3830 */       super.remove(paramComponent);
/* 3831 */       if ((this.notifyTabbedPane) && (i != -1))
/* 3832 */         BasicTabbedPaneUI.this.tabPane.setTabComponentAt(i, null);
/*      */     }
/*      */ 
/*      */     private void removeUnusedTabComponents()
/*      */     {
/* 3837 */       for (Component localComponent : getComponents())
/* 3838 */         if (!(localComponent instanceof UIResource)) {
/* 3839 */           int k = BasicTabbedPaneUI.this.tabPane.indexOfTabComponent(localComponent);
/* 3840 */           if (k == -1)
/* 3841 */             super.remove(localComponent);
/*      */         }
/*      */     }
/*      */ 
/*      */     public boolean isOptimizedDrawingEnabled()
/*      */     {
/* 3848 */       return (BasicTabbedPaneUI.this.tabScroller != null) && (!BasicTabbedPaneUI.this.tabScroller.croppedEdge.isParamsSet());
/*      */     }
/*      */ 
/*      */     public void doLayout()
/*      */     {
/* 3855 */       if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
/* 3856 */         BasicTabbedPaneUI.this.tabScroller.tabPanel.repaint();
/* 3857 */         BasicTabbedPaneUI.this.tabScroller.updateView();
/*      */       } else {
/* 3859 */         BasicTabbedPaneUI.this.tabPane.repaint(getBounds());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TabSelectionHandler
/*      */     implements ChangeListener
/*      */   {
/*      */     public TabSelectionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 3769 */       BasicTabbedPaneUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TabbedPaneLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     public TabbedPaneLayout()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/* 2278 */       return calculateSize(false);
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 2282 */       return calculateSize(true);
/*      */     }
/*      */ 
/*      */     protected Dimension calculateSize(boolean paramBoolean) {
/* 2286 */       int i = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2287 */       Insets localInsets1 = BasicTabbedPaneUI.this.tabPane.getInsets();
/* 2288 */       Insets localInsets2 = BasicTabbedPaneUI.this.getContentBorderInsets(i);
/* 2289 */       Insets localInsets3 = BasicTabbedPaneUI.this.getTabAreaInsets(i);
/*      */ 
/* 2291 */       Dimension localDimension1 = new Dimension(0, 0);
/* 2292 */       int j = 0;
/* 2293 */       int k = 0;
/* 2294 */       int m = 0;
/* 2295 */       int n = 0;
/*      */ 
/* 2300 */       for (int i1 = 0; i1 < BasicTabbedPaneUI.this.tabPane.getTabCount(); i1++) {
/* 2301 */         Component localComponent = BasicTabbedPaneUI.this.tabPane.getComponentAt(i1);
/* 2302 */         if (localComponent != null) {
/* 2303 */           Dimension localDimension2 = paramBoolean ? localComponent.getMinimumSize() : localComponent.getPreferredSize();
/*      */ 
/* 2306 */           if (localDimension2 != null) {
/* 2307 */             n = Math.max(localDimension2.height, n);
/* 2308 */             m = Math.max(localDimension2.width, m);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2313 */       k += m;
/* 2314 */       j += n;
/*      */ 
/* 2320 */       switch (i) {
/*      */       case 2:
/*      */       case 4:
/* 2323 */         j = Math.max(j, BasicTabbedPaneUI.this.calculateMaxTabHeight(i));
/* 2324 */         i1 = preferredTabAreaWidth(i, j - localInsets3.top - localInsets3.bottom);
/* 2325 */         k += i1;
/* 2326 */         break;
/*      */       case 1:
/*      */       case 3:
/*      */       default:
/* 2330 */         k = Math.max(k, BasicTabbedPaneUI.this.calculateMaxTabWidth(i));
/* 2331 */         i1 = preferredTabAreaHeight(i, k - localInsets3.left - localInsets3.right);
/* 2332 */         j += i1;
/*      */       }
/* 2334 */       return new Dimension(k + localInsets1.left + localInsets1.right + localInsets2.left + localInsets2.right, j + localInsets1.bottom + localInsets1.top + localInsets2.top + localInsets2.bottom);
/*      */     }
/*      */ 
/*      */     protected int preferredTabAreaHeight(int paramInt1, int paramInt2)
/*      */     {
/* 2340 */       FontMetrics localFontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
/* 2341 */       int i = BasicTabbedPaneUI.this.tabPane.getTabCount();
/* 2342 */       int j = 0;
/* 2343 */       if (i > 0) {
/* 2344 */         int k = 1;
/* 2345 */         int m = 0;
/*      */ 
/* 2347 */         int n = BasicTabbedPaneUI.this.calculateMaxTabHeight(paramInt1);
/*      */ 
/* 2349 */         for (int i1 = 0; i1 < i; i1++) {
/* 2350 */           int i2 = BasicTabbedPaneUI.this.calculateTabWidth(paramInt1, i1, localFontMetrics);
/*      */ 
/* 2352 */           if ((m != 0) && (m + i2 > paramInt2)) {
/* 2353 */             k++;
/* 2354 */             m = 0;
/*      */           }
/* 2356 */           m += i2;
/*      */         }
/* 2358 */         j = BasicTabbedPaneUI.this.calculateTabAreaHeight(paramInt1, k, n);
/*      */       }
/* 2360 */       return j;
/*      */     }
/*      */ 
/*      */     protected int preferredTabAreaWidth(int paramInt1, int paramInt2) {
/* 2364 */       FontMetrics localFontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
/* 2365 */       int i = BasicTabbedPaneUI.this.tabPane.getTabCount();
/* 2366 */       int j = 0;
/* 2367 */       if (i > 0) {
/* 2368 */         int k = 1;
/* 2369 */         int m = 0;
/* 2370 */         int n = localFontMetrics.getHeight();
/*      */ 
/* 2372 */         BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(paramInt1);
/*      */ 
/* 2374 */         for (int i1 = 0; i1 < i; i1++) {
/* 2375 */           int i2 = BasicTabbedPaneUI.this.calculateTabHeight(paramInt1, i1, n);
/*      */ 
/* 2377 */           if ((m != 0) && (m + i2 > paramInt2)) {
/* 2378 */             k++;
/* 2379 */             m = 0;
/*      */           }
/* 2381 */           m += i2;
/*      */         }
/* 2383 */         j = BasicTabbedPaneUI.this.calculateTabAreaWidth(paramInt1, k, BasicTabbedPaneUI.this.maxTabWidth);
/*      */       }
/* 2385 */       return j;
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/* 2400 */       BasicTabbedPaneUI.this.setRolloverTab(-1);
/*      */ 
/* 2402 */       int i = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2403 */       Insets localInsets1 = BasicTabbedPaneUI.this.tabPane.getInsets();
/* 2404 */       int j = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
/* 2405 */       Component localComponent1 = BasicTabbedPaneUI.this.getVisibleComponent();
/*      */ 
/* 2407 */       calculateLayoutInfo();
/*      */ 
/* 2409 */       Component localComponent2 = null;
/* 2410 */       if (j < 0) {
/* 2411 */         if (localComponent1 != null)
/*      */         {
/* 2413 */           BasicTabbedPaneUI.this.setVisibleComponent(null);
/*      */         }
/*      */       }
/* 2416 */       else localComponent2 = BasicTabbedPaneUI.this.tabPane.getComponentAt(j);
/*      */ 
/* 2419 */       int i2 = 0;
/* 2420 */       int i3 = 0;
/* 2421 */       Insets localInsets2 = BasicTabbedPaneUI.this.getContentBorderInsets(i);
/*      */ 
/* 2423 */       int i4 = 0;
/*      */ 
/* 2432 */       if (localComponent2 != null) {
/* 2433 */         if ((localComponent2 != localComponent1) && (localComponent1 != null))
/*      */         {
/* 2435 */           if (SwingUtilities.findFocusOwner(localComponent1) != null) {
/* 2436 */             i4 = 1;
/*      */           }
/*      */         }
/* 2439 */         BasicTabbedPaneUI.this.setVisibleComponent(localComponent2);
/*      */       }
/*      */ 
/* 2442 */       Rectangle localRectangle = BasicTabbedPaneUI.this.tabPane.getBounds();
/* 2443 */       int i5 = BasicTabbedPaneUI.this.tabPane.getComponentCount();
/*      */ 
/* 2445 */       if (i5 > 0)
/*      */       {
/*      */         int k;
/*      */         int m;
/* 2447 */         switch (i) {
/*      */         case 2:
/* 2449 */           i2 = BasicTabbedPaneUI.this.calculateTabAreaWidth(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
/* 2450 */           k = localInsets1.left + i2 + localInsets2.left;
/* 2451 */           m = localInsets1.top + localInsets2.top;
/* 2452 */           break;
/*      */         case 4:
/* 2454 */           i2 = BasicTabbedPaneUI.this.calculateTabAreaWidth(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
/* 2455 */           k = localInsets1.left + localInsets2.left;
/* 2456 */           m = localInsets1.top + localInsets2.top;
/* 2457 */           break;
/*      */         case 3:
/* 2459 */           i3 = BasicTabbedPaneUI.this.calculateTabAreaHeight(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
/* 2460 */           k = localInsets1.left + localInsets2.left;
/* 2461 */           m = localInsets1.top + localInsets2.top;
/* 2462 */           break;
/*      */         case 1:
/*      */         default:
/* 2465 */           i3 = BasicTabbedPaneUI.this.calculateTabAreaHeight(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
/* 2466 */           k = localInsets1.left + localInsets2.left;
/* 2467 */           m = localInsets1.top + i3 + localInsets2.top;
/*      */         }
/*      */ 
/* 2470 */         int n = localRectangle.width - i2 - localInsets1.left - localInsets1.right - localInsets2.left - localInsets2.right;
/*      */ 
/* 2473 */         int i1 = localRectangle.height - i3 - localInsets1.top - localInsets1.bottom - localInsets2.top - localInsets2.bottom;
/*      */ 
/* 2477 */         for (int i6 = 0; i6 < i5; i6++) {
/* 2478 */           Component localComponent3 = BasicTabbedPaneUI.this.tabPane.getComponent(i6);
/* 2479 */           if (localComponent3 == BasicTabbedPaneUI.this.tabContainer)
/*      */           {
/* 2481 */             int i7 = i2 == 0 ? localRectangle.width : i2 + localInsets1.left + localInsets1.right + localInsets2.left + localInsets2.right;
/*      */ 
/* 2484 */             int i8 = i3 == 0 ? localRectangle.height : i3 + localInsets1.top + localInsets1.bottom + localInsets2.top + localInsets2.bottom;
/*      */ 
/* 2488 */             int i9 = 0;
/* 2489 */             int i10 = 0;
/* 2490 */             if (i == 3)
/* 2491 */               i10 = localRectangle.height - i8;
/* 2492 */             else if (i == 4) {
/* 2493 */               i9 = localRectangle.width - i7;
/*      */             }
/* 2495 */             localComponent3.setBounds(i9, i10, i7, i8);
/*      */           } else {
/* 2497 */             localComponent3.setBounds(k, m, n, i1);
/*      */           }
/*      */         }
/*      */       }
/* 2501 */       layoutTabComponents();
/* 2502 */       if ((i4 != 0) && 
/* 2503 */         (!BasicTabbedPaneUI.this.requestFocusForVisibleComponent()))
/* 2504 */         BasicTabbedPaneUI.this.tabPane.requestFocus();
/*      */     }
/*      */ 
/*      */     public void calculateLayoutInfo()
/*      */     {
/* 2510 */       int i = BasicTabbedPaneUI.this.tabPane.getTabCount();
/* 2511 */       BasicTabbedPaneUI.this.assureRectsCreated(i);
/* 2512 */       calculateTabRects(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), i);
/* 2513 */       BasicTabbedPaneUI.this.isRunsDirty = false;
/*      */     }
/*      */ 
/*      */     private void layoutTabComponents() {
/* 2517 */       if (BasicTabbedPaneUI.this.tabContainer == null) {
/* 2518 */         return;
/*      */       }
/* 2520 */       Rectangle localRectangle = new Rectangle();
/* 2521 */       Point localPoint = new Point(-BasicTabbedPaneUI.this.tabContainer.getX(), -BasicTabbedPaneUI.this.tabContainer.getY());
/* 2522 */       if (BasicTabbedPaneUI.this.scrollableTabLayoutEnabled()) {
/* 2523 */         BasicTabbedPaneUI.this.translatePointToTabPanel(0, 0, localPoint);
/*      */       }
/* 2525 */       for (int i = 0; i < BasicTabbedPaneUI.this.tabPane.getTabCount(); i++) {
/* 2526 */         Component localComponent = BasicTabbedPaneUI.this.tabPane.getTabComponentAt(i);
/* 2527 */         if (localComponent != null)
/*      */         {
/* 2530 */           BasicTabbedPaneUI.this.getTabBounds(i, localRectangle);
/* 2531 */           Dimension localDimension = localComponent.getPreferredSize();
/* 2532 */           Insets localInsets = BasicTabbedPaneUI.this.getTabInsets(BasicTabbedPaneUI.this.tabPane.getTabPlacement(), i);
/* 2533 */           int j = localRectangle.x + localInsets.left + localPoint.x;
/* 2534 */           int k = localRectangle.y + localInsets.top + localPoint.y;
/* 2535 */           int m = localRectangle.width - localInsets.left - localInsets.right;
/* 2536 */           int n = localRectangle.height - localInsets.top - localInsets.bottom;
/*      */ 
/* 2538 */           int i1 = j + (m - localDimension.width) / 2;
/* 2539 */           int i2 = k + (n - localDimension.height) / 2;
/* 2540 */           int i3 = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2541 */           boolean bool = i == BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
/* 2542 */           localComponent.setBounds(i1 + BasicTabbedPaneUI.this.getTabLabelShiftX(i3, i, bool), i2 + BasicTabbedPaneUI.this.getTabLabelShiftY(i3, i, bool), localDimension.width, localDimension.height);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void calculateTabRects(int paramInt1, int paramInt2)
/*      */     {
/* 2549 */       FontMetrics localFontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
/* 2550 */       Dimension localDimension = BasicTabbedPaneUI.this.tabPane.getSize();
/* 2551 */       Insets localInsets1 = BasicTabbedPaneUI.this.tabPane.getInsets();
/* 2552 */       Insets localInsets2 = BasicTabbedPaneUI.this.getTabAreaInsets(paramInt1);
/* 2553 */       int i = localFontMetrics.getHeight();
/* 2554 */       int j = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
/*      */ 
/* 2559 */       int i4 = (paramInt1 == 2) || (paramInt1 == 4) ? 1 : 0;
/* 2560 */       boolean bool = BasicGraphicsUtils.isLeftToRight(BasicTabbedPaneUI.this.tabPane);
/*      */       int i1;
/*      */       int i2;
/*      */       int i3;
/* 2565 */       switch (paramInt1) {
/*      */       case 2:
/* 2567 */         BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(paramInt1);
/* 2568 */         i1 = localInsets1.left + localInsets2.left;
/* 2569 */         i2 = localInsets1.top + localInsets2.top;
/* 2570 */         i3 = localDimension.height - (localInsets1.bottom + localInsets2.bottom);
/* 2571 */         break;
/*      */       case 4:
/* 2573 */         BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(paramInt1);
/* 2574 */         i1 = localDimension.width - localInsets1.right - localInsets2.right - BasicTabbedPaneUI.this.maxTabWidth;
/* 2575 */         i2 = localInsets1.top + localInsets2.top;
/* 2576 */         i3 = localDimension.height - (localInsets1.bottom + localInsets2.bottom);
/* 2577 */         break;
/*      */       case 3:
/* 2579 */         BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(paramInt1);
/* 2580 */         i1 = localInsets1.left + localInsets2.left;
/* 2581 */         i2 = localDimension.height - localInsets1.bottom - localInsets2.bottom - BasicTabbedPaneUI.this.maxTabHeight;
/* 2582 */         i3 = localDimension.width - (localInsets1.right + localInsets2.right);
/* 2583 */         break;
/*      */       case 1:
/*      */       default:
/* 2586 */         BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(paramInt1);
/* 2587 */         i1 = localInsets1.left + localInsets2.left;
/* 2588 */         i2 = localInsets1.top + localInsets2.top;
/* 2589 */         i3 = localDimension.width - (localInsets1.right + localInsets2.right);
/*      */       }
/*      */ 
/* 2593 */       int k = BasicTabbedPaneUI.this.getTabRunOverlay(paramInt1);
/*      */ 
/* 2595 */       BasicTabbedPaneUI.this.runCount = 0;
/* 2596 */       BasicTabbedPaneUI.this.selectedRun = -1;
/*      */ 
/* 2598 */       if (paramInt2 == 0)
/*      */         return;
/*      */       Rectangle localRectangle;
/* 2604 */       for (int m = 0; m < paramInt2; m++) {
/* 2605 */         localRectangle = BasicTabbedPaneUI.this.rects[m];
/*      */ 
/* 2607 */         if (i4 == 0)
/*      */         {
/* 2609 */           if (m > 0) {
/* 2610 */             localRectangle.x = (BasicTabbedPaneUI.this.rects[(m - 1)].x + BasicTabbedPaneUI.this.rects[(m - 1)].width);
/*      */           } else {
/* 2612 */             BasicTabbedPaneUI.this.tabRuns[0] = 0;
/* 2613 */             BasicTabbedPaneUI.this.runCount = 1;
/* 2614 */             BasicTabbedPaneUI.this.maxTabWidth = 0;
/* 2615 */             localRectangle.x = i1;
/*      */           }
/* 2617 */           localRectangle.width = BasicTabbedPaneUI.this.calculateTabWidth(paramInt1, m, localFontMetrics);
/* 2618 */           BasicTabbedPaneUI.this.maxTabWidth = Math.max(BasicTabbedPaneUI.this.maxTabWidth, localRectangle.width);
/*      */ 
/* 2623 */           if ((localRectangle.x != 2 + localInsets1.left) && (localRectangle.x + localRectangle.width > i3)) {
/* 2624 */             if (BasicTabbedPaneUI.this.runCount > BasicTabbedPaneUI.this.tabRuns.length - 1) {
/* 2625 */               BasicTabbedPaneUI.this.expandTabRunsArray();
/*      */             }
/* 2627 */             BasicTabbedPaneUI.this.tabRuns[BasicTabbedPaneUI.this.runCount] = m;
/* 2628 */             BasicTabbedPaneUI.this.runCount += 1;
/* 2629 */             localRectangle.x = i1;
/*      */           }
/*      */ 
/* 2632 */           localRectangle.y = i2;
/* 2633 */           localRectangle.height = BasicTabbedPaneUI.this.maxTabHeight;
/*      */         }
/*      */         else
/*      */         {
/* 2637 */           if (m > 0) {
/* 2638 */             localRectangle.y = (BasicTabbedPaneUI.this.rects[(m - 1)].y + BasicTabbedPaneUI.this.rects[(m - 1)].height);
/*      */           } else {
/* 2640 */             BasicTabbedPaneUI.this.tabRuns[0] = 0;
/* 2641 */             BasicTabbedPaneUI.this.runCount = 1;
/* 2642 */             BasicTabbedPaneUI.this.maxTabHeight = 0;
/* 2643 */             localRectangle.y = i2;
/*      */           }
/* 2645 */           localRectangle.height = BasicTabbedPaneUI.this.calculateTabHeight(paramInt1, m, i);
/* 2646 */           BasicTabbedPaneUI.this.maxTabHeight = Math.max(BasicTabbedPaneUI.this.maxTabHeight, localRectangle.height);
/*      */ 
/* 2651 */           if ((localRectangle.y != 2 + localInsets1.top) && (localRectangle.y + localRectangle.height > i3)) {
/* 2652 */             if (BasicTabbedPaneUI.this.runCount > BasicTabbedPaneUI.this.tabRuns.length - 1) {
/* 2653 */               BasicTabbedPaneUI.this.expandTabRunsArray();
/*      */             }
/* 2655 */             BasicTabbedPaneUI.this.tabRuns[BasicTabbedPaneUI.this.runCount] = m;
/* 2656 */             BasicTabbedPaneUI.this.runCount += 1;
/* 2657 */             localRectangle.y = i2;
/*      */           }
/*      */ 
/* 2660 */           localRectangle.x = i1;
/* 2661 */           localRectangle.width = BasicTabbedPaneUI.this.maxTabWidth;
/*      */         }
/*      */ 
/* 2664 */         if (m == j) {
/* 2665 */           BasicTabbedPaneUI.this.selectedRun = (BasicTabbedPaneUI.this.runCount - 1);
/*      */         }
/*      */       }
/*      */ 
/* 2669 */       if (BasicTabbedPaneUI.this.runCount > 1)
/*      */       {
/* 2671 */         normalizeTabRuns(paramInt1, paramInt2, i4 != 0 ? i2 : i1, i3);
/*      */ 
/* 2673 */         BasicTabbedPaneUI.this.selectedRun = BasicTabbedPaneUI.this.getRunForTab(paramInt2, j);
/*      */ 
/* 2676 */         if (BasicTabbedPaneUI.this.shouldRotateTabRuns(paramInt1))
/* 2677 */           rotateTabRuns(paramInt1, BasicTabbedPaneUI.this.selectedRun);
/*      */       }
/*      */       int i5;
/* 2683 */       for (m = BasicTabbedPaneUI.this.runCount - 1; m >= 0; m--) {
/* 2684 */         i5 = BasicTabbedPaneUI.this.tabRuns[m];
/* 2685 */         int i6 = BasicTabbedPaneUI.this.tabRuns[(m + 1)];
/* 2686 */         int i7 = i6 != 0 ? i6 - 1 : paramInt2 - 1;
/*      */         int n;
/* 2687 */         if (i4 == 0) {
/* 2688 */           for (n = i5; n <= i7; n++) {
/* 2689 */             localRectangle = BasicTabbedPaneUI.this.rects[n];
/* 2690 */             localRectangle.y = i2;
/* 2691 */             localRectangle.x += BasicTabbedPaneUI.this.getTabRunIndent(paramInt1, m);
/*      */           }
/* 2693 */           if (BasicTabbedPaneUI.this.shouldPadTabRun(paramInt1, m)) {
/* 2694 */             padTabRun(paramInt1, i5, i7, i3);
/*      */           }
/* 2696 */           if (paramInt1 == 3)
/* 2697 */             i2 -= BasicTabbedPaneUI.this.maxTabHeight - k;
/*      */           else
/* 2699 */             i2 += BasicTabbedPaneUI.this.maxTabHeight - k;
/*      */         }
/*      */         else {
/* 2702 */           for (n = i5; n <= i7; n++) {
/* 2703 */             localRectangle = BasicTabbedPaneUI.this.rects[n];
/* 2704 */             localRectangle.x = i1;
/* 2705 */             localRectangle.y += BasicTabbedPaneUI.this.getTabRunIndent(paramInt1, m);
/*      */           }
/* 2707 */           if (BasicTabbedPaneUI.this.shouldPadTabRun(paramInt1, m)) {
/* 2708 */             padTabRun(paramInt1, i5, i7, i3);
/*      */           }
/* 2710 */           if (paramInt1 == 4)
/* 2711 */             i1 -= BasicTabbedPaneUI.this.maxTabWidth - k;
/*      */           else {
/* 2713 */             i1 += BasicTabbedPaneUI.this.maxTabWidth - k;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2719 */       padSelectedTab(paramInt1, j);
/*      */ 
/* 2723 */       if ((!bool) && (i4 == 0)) {
/* 2724 */         i5 = localDimension.width - (localInsets1.right + localInsets2.right);
/*      */ 
/* 2726 */         for (m = 0; m < paramInt2; m++)
/* 2727 */           BasicTabbedPaneUI.this.rects[m].x = (i5 - BasicTabbedPaneUI.this.rects[m].x - BasicTabbedPaneUI.this.rects[m].width);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void rotateTabRuns(int paramInt1, int paramInt2)
/*      */     {
/* 2737 */       for (int i = 0; i < paramInt2; i++) {
/* 2738 */         int j = BasicTabbedPaneUI.this.tabRuns[0];
/* 2739 */         for (int k = 1; k < BasicTabbedPaneUI.this.runCount; k++) {
/* 2740 */           BasicTabbedPaneUI.this.tabRuns[(k - 1)] = BasicTabbedPaneUI.this.tabRuns[k];
/*      */         }
/* 2742 */         BasicTabbedPaneUI.this.tabRuns[(BasicTabbedPaneUI.this.runCount - 1)] = j;
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void normalizeTabRuns(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 2748 */       int i = (paramInt1 == 2) || (paramInt1 == 4) ? 1 : 0;
/* 2749 */       int j = BasicTabbedPaneUI.this.runCount - 1;
/* 2750 */       int k = 1;
/* 2751 */       double d = 1.25D;
/*      */ 
/* 2764 */       while (k != 0) {
/* 2765 */         int m = BasicTabbedPaneUI.this.lastTabInRun(paramInt2, j);
/* 2766 */         int n = BasicTabbedPaneUI.this.lastTabInRun(paramInt2, j - 1);
/*      */         int i1;
/*      */         int i2;
/* 2770 */         if (i == 0) {
/* 2771 */           i1 = BasicTabbedPaneUI.this.rects[m].x + BasicTabbedPaneUI.this.rects[m].width;
/* 2772 */           i2 = (int)(BasicTabbedPaneUI.this.maxTabWidth * d);
/*      */         } else {
/* 2774 */           i1 = BasicTabbedPaneUI.this.rects[m].y + BasicTabbedPaneUI.this.rects[m].height;
/* 2775 */           i2 = (int)(BasicTabbedPaneUI.this.maxTabHeight * d * 2.0D);
/*      */         }
/*      */ 
/* 2780 */         if (paramInt4 - i1 > i2)
/*      */         {
/* 2783 */           BasicTabbedPaneUI.this.tabRuns[j] = n;
/* 2784 */           if (i == 0)
/* 2785 */             BasicTabbedPaneUI.this.rects[n].x = paramInt3;
/*      */           else {
/* 2787 */             BasicTabbedPaneUI.this.rects[n].y = paramInt3;
/*      */           }
/* 2789 */           for (int i3 = n + 1; i3 <= m; i3++) {
/* 2790 */             if (i == 0)
/* 2791 */               BasicTabbedPaneUI.this.rects[i3].x = (BasicTabbedPaneUI.this.rects[(i3 - 1)].x + BasicTabbedPaneUI.this.rects[(i3 - 1)].width);
/*      */             else {
/* 2793 */               BasicTabbedPaneUI.this.rects[i3].y = (BasicTabbedPaneUI.this.rects[(i3 - 1)].y + BasicTabbedPaneUI.this.rects[(i3 - 1)].height);
/*      */             }
/*      */           }
/*      */         }
/* 2797 */         else if (j == BasicTabbedPaneUI.this.runCount - 1)
/*      */         {
/* 2799 */           k = 0;
/*      */         }
/* 2801 */         if (j - 1 > 0)
/*      */         {
/* 2803 */           j--;
/*      */         }
/*      */         else
/*      */         {
/* 2808 */           j = BasicTabbedPaneUI.this.runCount - 1;
/* 2809 */           d += 0.25D;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void padTabRun(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 2815 */       Rectangle localRectangle1 = BasicTabbedPaneUI.this.rects[paramInt3];
/*      */       int i;
/*      */       int j;
/*      */       float f;
/*      */       int k;
/*      */       Rectangle localRectangle2;
/* 2816 */       if ((paramInt1 == 1) || (paramInt1 == 3)) {
/* 2817 */         i = localRectangle1.x + localRectangle1.width - BasicTabbedPaneUI.this.rects[paramInt2].x;
/* 2818 */         j = paramInt4 - (localRectangle1.x + localRectangle1.width);
/* 2819 */         f = j / i;
/*      */ 
/* 2821 */         for (k = paramInt2; k <= paramInt3; k++) {
/* 2822 */           localRectangle2 = BasicTabbedPaneUI.this.rects[k];
/* 2823 */           if (k > paramInt2) {
/* 2824 */             localRectangle2.x = (BasicTabbedPaneUI.this.rects[(k - 1)].x + BasicTabbedPaneUI.this.rects[(k - 1)].width);
/*      */           }
/* 2826 */           localRectangle2.width += Math.round(localRectangle2.width * f);
/*      */         }
/* 2828 */         localRectangle1.width = (paramInt4 - localRectangle1.x);
/*      */       } else {
/* 2830 */         i = localRectangle1.y + localRectangle1.height - BasicTabbedPaneUI.this.rects[paramInt2].y;
/* 2831 */         j = paramInt4 - (localRectangle1.y + localRectangle1.height);
/* 2832 */         f = j / i;
/*      */ 
/* 2834 */         for (k = paramInt2; k <= paramInt3; k++) {
/* 2835 */           localRectangle2 = BasicTabbedPaneUI.this.rects[k];
/* 2836 */           if (k > paramInt2) {
/* 2837 */             localRectangle2.y = (BasicTabbedPaneUI.this.rects[(k - 1)].y + BasicTabbedPaneUI.this.rects[(k - 1)].height);
/*      */           }
/* 2839 */           localRectangle2.height += Math.round(localRectangle2.height * f);
/*      */         }
/* 2841 */         localRectangle1.height = (paramInt4 - localRectangle1.y);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void padSelectedTab(int paramInt1, int paramInt2)
/*      */     {
/* 2847 */       if (paramInt2 >= 0) {
/* 2848 */         Rectangle localRectangle = BasicTabbedPaneUI.this.rects[paramInt2];
/* 2849 */         Insets localInsets1 = BasicTabbedPaneUI.this.getSelectedTabPadInsets(paramInt1);
/* 2850 */         localRectangle.x -= localInsets1.left;
/* 2851 */         localRectangle.width += localInsets1.left + localInsets1.right;
/* 2852 */         localRectangle.y -= localInsets1.top;
/* 2853 */         localRectangle.height += localInsets1.top + localInsets1.bottom;
/*      */ 
/* 2855 */         if (!BasicTabbedPaneUI.this.scrollableTabLayoutEnabled())
/*      */         {
/* 2857 */           Dimension localDimension = BasicTabbedPaneUI.this.tabPane.getSize();
/* 2858 */           Insets localInsets2 = BasicTabbedPaneUI.this.tabPane.getInsets();
/*      */           int i;
/*      */           int j;
/* 2860 */           if ((paramInt1 == 2) || (paramInt1 == 4)) {
/* 2861 */             i = localInsets2.top - localRectangle.y;
/* 2862 */             if (i > 0) {
/* 2863 */               localRectangle.y += i;
/* 2864 */               localRectangle.height -= i;
/*      */             }
/* 2866 */             j = localRectangle.y + localRectangle.height + localInsets2.bottom - localDimension.height;
/* 2867 */             if (j > 0)
/* 2868 */               localRectangle.height -= j;
/*      */           }
/*      */           else {
/* 2871 */             i = localInsets2.left - localRectangle.x;
/* 2872 */             if (i > 0) {
/* 2873 */               localRectangle.x += i;
/* 2874 */               localRectangle.width -= i;
/*      */             }
/* 2876 */             j = localRectangle.x + localRectangle.width + localInsets2.right - localDimension.width;
/* 2877 */             if (j > 0)
/* 2878 */               localRectangle.width -= j; 
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TabbedPaneScrollLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
/* 2886 */     private TabbedPaneScrollLayout() { super(); }
/*      */ 
/*      */     protected int preferredTabAreaHeight(int paramInt1, int paramInt2) {
/* 2889 */       return BasicTabbedPaneUI.this.calculateMaxTabHeight(paramInt1);
/*      */     }
/*      */ 
/*      */     protected int preferredTabAreaWidth(int paramInt1, int paramInt2) {
/* 2893 */       return BasicTabbedPaneUI.this.calculateMaxTabWidth(paramInt1);
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/* 2908 */       BasicTabbedPaneUI.this.setRolloverTab(-1);
/*      */ 
/* 2910 */       int i = BasicTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2911 */       int j = BasicTabbedPaneUI.this.tabPane.getTabCount();
/* 2912 */       Insets localInsets1 = BasicTabbedPaneUI.this.tabPane.getInsets();
/* 2913 */       int k = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
/* 2914 */       Component localComponent1 = BasicTabbedPaneUI.this.getVisibleComponent();
/*      */ 
/* 2916 */       calculateLayoutInfo();
/*      */ 
/* 2918 */       Component localComponent2 = null;
/* 2919 */       if (k < 0) {
/* 2920 */         if (localComponent1 != null)
/*      */         {
/* 2922 */           BasicTabbedPaneUI.this.setVisibleComponent(null);
/*      */         }
/*      */       }
/* 2925 */       else localComponent2 = BasicTabbedPaneUI.this.tabPane.getComponentAt(k);
/*      */ 
/* 2928 */       if (BasicTabbedPaneUI.this.tabPane.getTabCount() == 0) {
/* 2929 */         BasicTabbedPaneUI.this.tabScroller.croppedEdge.resetParams();
/* 2930 */         BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.setVisible(false);
/* 2931 */         BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton.setVisible(false);
/* 2932 */         return;
/*      */       }
/*      */ 
/* 2935 */       int m = 0;
/*      */ 
/* 2944 */       if (localComponent2 != null) {
/* 2945 */         if ((localComponent2 != localComponent1) && (localComponent1 != null))
/*      */         {
/* 2947 */           if (SwingUtilities.findFocusOwner(localComponent1) != null) {
/* 2948 */             m = 1;
/*      */           }
/*      */         }
/* 2951 */         BasicTabbedPaneUI.this.setVisibleComponent(localComponent2);
/*      */       }
/*      */ 
/* 2955 */       Insets localInsets2 = BasicTabbedPaneUI.this.getContentBorderInsets(i);
/* 2956 */       Rectangle localRectangle = BasicTabbedPaneUI.this.tabPane.getBounds();
/* 2957 */       int i8 = BasicTabbedPaneUI.this.tabPane.getComponentCount();
/*      */ 
/* 2959 */       if (i8 > 0)
/*      */       {
/*      */         int i2;
/*      */         int i3;
/*      */         int n;
/*      */         int i1;
/*      */         int i4;
/*      */         int i5;
/*      */         int i6;
/*      */         int i7;
/* 2960 */         switch (i)
/*      */         {
/*      */         case 2:
/* 2963 */           i2 = BasicTabbedPaneUI.this.calculateTabAreaWidth(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
/* 2964 */           i3 = localRectangle.height - localInsets1.top - localInsets1.bottom;
/* 2965 */           n = localInsets1.left;
/* 2966 */           i1 = localInsets1.top;
/*      */ 
/* 2969 */           i4 = n + i2 + localInsets2.left;
/* 2970 */           i5 = i1 + localInsets2.top;
/* 2971 */           i6 = localRectangle.width - localInsets1.left - localInsets1.right - i2 - localInsets2.left - localInsets2.right;
/*      */ 
/* 2973 */           i7 = localRectangle.height - localInsets1.top - localInsets1.bottom - localInsets2.top - localInsets2.bottom;
/*      */ 
/* 2975 */           break;
/*      */         case 4:
/* 2978 */           i2 = BasicTabbedPaneUI.this.calculateTabAreaWidth(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabWidth);
/* 2979 */           i3 = localRectangle.height - localInsets1.top - localInsets1.bottom;
/* 2980 */           n = localRectangle.width - localInsets1.right - i2;
/* 2981 */           i1 = localInsets1.top;
/*      */ 
/* 2984 */           i4 = localInsets1.left + localInsets2.left;
/* 2985 */           i5 = localInsets1.top + localInsets2.top;
/* 2986 */           i6 = localRectangle.width - localInsets1.left - localInsets1.right - i2 - localInsets2.left - localInsets2.right;
/*      */ 
/* 2988 */           i7 = localRectangle.height - localInsets1.top - localInsets1.bottom - localInsets2.top - localInsets2.bottom;
/*      */ 
/* 2990 */           break;
/*      */         case 3:
/* 2993 */           i2 = localRectangle.width - localInsets1.left - localInsets1.right;
/* 2994 */           i3 = BasicTabbedPaneUI.this.calculateTabAreaHeight(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
/* 2995 */           n = localInsets1.left;
/* 2996 */           i1 = localRectangle.height - localInsets1.bottom - i3;
/*      */ 
/* 2999 */           i4 = localInsets1.left + localInsets2.left;
/* 3000 */           i5 = localInsets1.top + localInsets2.top;
/* 3001 */           i6 = localRectangle.width - localInsets1.left - localInsets1.right - localInsets2.left - localInsets2.right;
/*      */ 
/* 3003 */           i7 = localRectangle.height - localInsets1.top - localInsets1.bottom - i3 - localInsets2.top - localInsets2.bottom;
/*      */ 
/* 3005 */           break;
/*      */         case 1:
/*      */         default:
/* 3009 */           i2 = localRectangle.width - localInsets1.left - localInsets1.right;
/* 3010 */           i3 = BasicTabbedPaneUI.this.calculateTabAreaHeight(i, BasicTabbedPaneUI.this.runCount, BasicTabbedPaneUI.this.maxTabHeight);
/* 3011 */           n = localInsets1.left;
/* 3012 */           i1 = localInsets1.top;
/*      */ 
/* 3015 */           i4 = n + localInsets2.left;
/* 3016 */           i5 = i1 + i3 + localInsets2.top;
/* 3017 */           i6 = localRectangle.width - localInsets1.left - localInsets1.right - localInsets2.left - localInsets2.right;
/*      */ 
/* 3019 */           i7 = localRectangle.height - localInsets1.top - localInsets1.bottom - i3 - localInsets2.top - localInsets2.bottom;
/*      */         }
/*      */ 
/* 3023 */         for (int i9 = 0; i9 < i8; i9++) {
/* 3024 */           Component localComponent3 = BasicTabbedPaneUI.this.tabPane.getComponent(i9);
/*      */           Object localObject1;
/*      */           Object localObject2;
/*      */           int i10;
/*      */           int i11;
/*      */           int i13;
/*      */           int i14;
/* 3026 */           if ((BasicTabbedPaneUI.this.tabScroller != null) && (localComponent3 == BasicTabbedPaneUI.this.tabScroller.viewport)) {
/* 3027 */             localObject1 = (JViewport)localComponent3;
/* 3028 */             localObject2 = ((JViewport)localObject1).getViewRect();
/* 3029 */             i10 = i2;
/* 3030 */             i11 = i3;
/* 3031 */             Dimension localDimension = BasicTabbedPaneUI.this.tabScroller.scrollForwardButton.getPreferredSize();
/* 3032 */             switch (i) {
/*      */             case 2:
/*      */             case 4:
/* 3035 */               i13 = BasicTabbedPaneUI.this.rects[(j - 1)].y + BasicTabbedPaneUI.this.rects[(j - 1)].height;
/* 3036 */               if (i13 > i3)
/*      */               {
/* 3038 */                 i11 = i3 > 2 * localDimension.height ? i3 - 2 * localDimension.height : 0;
/* 3039 */                 if (i13 - ((Rectangle)localObject2).y <= i11)
/*      */                 {
/* 3042 */                   i11 = i13 - ((Rectangle)localObject2).y; }  } break;
/*      */             case 1:
/*      */             case 3:
/*      */             default:
/* 3049 */               i14 = BasicTabbedPaneUI.this.rects[(j - 1)].x + BasicTabbedPaneUI.this.rects[(j - 1)].width;
/* 3050 */               if (i14 > i2)
/*      */               {
/* 3052 */                 i10 = i2 > 2 * localDimension.width ? i2 - 2 * localDimension.width : 0;
/* 3053 */                 if (i14 - ((Rectangle)localObject2).x <= i10)
/*      */                 {
/* 3056 */                   i10 = i14 - ((Rectangle)localObject2).x;
/*      */                 }
/*      */               }
/*      */               break;
/*      */             }
/* 3060 */             localComponent3.setBounds(n, i1, i10, i11);
/*      */           }
/* 3062 */           else if ((BasicTabbedPaneUI.this.tabScroller != null) && ((localComponent3 == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton) || (localComponent3 == BasicTabbedPaneUI.this.tabScroller.scrollBackwardButton)))
/*      */           {
/* 3065 */             localObject1 = localComponent3;
/* 3066 */             localObject2 = ((Component)localObject1).getPreferredSize();
/* 3067 */             i10 = 0;
/* 3068 */             i11 = 0;
/* 3069 */             int i12 = ((Dimension)localObject2).width;
/* 3070 */             i13 = ((Dimension)localObject2).height;
/* 3071 */             i14 = 0;
/*      */ 
/* 3073 */             switch (i) {
/*      */             case 2:
/*      */             case 4:
/* 3076 */               int i15 = BasicTabbedPaneUI.this.rects[(j - 1)].y + BasicTabbedPaneUI.this.rects[(j - 1)].height;
/* 3077 */               if (i15 > i3) {
/* 3078 */                 i14 = 1;
/* 3079 */                 i10 = i == 2 ? n + i2 - ((Dimension)localObject2).width : n;
/* 3080 */                 i11 = localComponent3 == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton ? localRectangle.height - localInsets1.bottom - ((Dimension)localObject2).height : localRectangle.height - localInsets1.bottom - 2 * ((Dimension)localObject2).height; } break;
/*      */             case 1:
/*      */             case 3:
/*      */             default:
/* 3089 */               int i16 = BasicTabbedPaneUI.this.rects[(j - 1)].x + BasicTabbedPaneUI.this.rects[(j - 1)].width;
/*      */ 
/* 3091 */               if (i16 > i2) {
/* 3092 */                 i14 = 1;
/* 3093 */                 i10 = localComponent3 == BasicTabbedPaneUI.this.tabScroller.scrollForwardButton ? localRectangle.width - localInsets1.left - ((Dimension)localObject2).width : localRectangle.width - localInsets1.left - 2 * ((Dimension)localObject2).width;
/*      */ 
/* 3096 */                 i11 = i == 1 ? i1 + i3 - ((Dimension)localObject2).height : i1;
/*      */               }break;
/*      */             }
/* 3099 */             localComponent3.setVisible(i14);
/* 3100 */             if (i14 != 0) {
/* 3101 */               localComponent3.setBounds(i10, i11, i12, i13);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 3106 */             localComponent3.setBounds(i4, i5, i6, i7);
/*      */           }
/*      */         }
/* 3109 */         super.layoutTabComponents();
/* 3110 */         layoutCroppedEdge();
/* 3111 */         if ((m != 0) && 
/* 3112 */           (!BasicTabbedPaneUI.this.requestFocusForVisibleComponent()))
/* 3113 */           BasicTabbedPaneUI.this.tabPane.requestFocus();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void layoutCroppedEdge()
/*      */     {
/* 3120 */       BasicTabbedPaneUI.this.tabScroller.croppedEdge.resetParams();
/* 3121 */       Rectangle localRectangle1 = BasicTabbedPaneUI.this.tabScroller.viewport.getViewRect();
/*      */ 
/* 3123 */       for (int j = 0; j < BasicTabbedPaneUI.this.rects.length; j++) {
/* 3124 */         Rectangle localRectangle2 = BasicTabbedPaneUI.this.rects[j];
/*      */         int i;
/* 3125 */         switch (BasicTabbedPaneUI.this.tabPane.getTabPlacement()) {
/*      */         case 2:
/*      */         case 4:
/* 3128 */           i = localRectangle1.y + localRectangle1.height;
/* 3129 */           if ((localRectangle2.y < i) && (localRectangle2.y + localRectangle2.height > i))
/* 3130 */             BasicTabbedPaneUI.this.tabScroller.croppedEdge.setParams(j, i - localRectangle2.y - 1, -BasicTabbedPaneUI.this.currentTabAreaInsets.left, 0); break;
/*      */         case 1:
/*      */         case 3:
/*      */         default:
/* 3137 */           i = localRectangle1.x + localRectangle1.width;
/* 3138 */           if ((localRectangle2.x < i - 1) && (localRectangle2.x + localRectangle2.width > i))
/* 3139 */             BasicTabbedPaneUI.this.tabScroller.croppedEdge.setParams(j, i - localRectangle2.x - 1, 0, -BasicTabbedPaneUI.this.currentTabAreaInsets.top);
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void calculateTabRects(int paramInt1, int paramInt2)
/*      */     {
/* 3147 */       FontMetrics localFontMetrics = BasicTabbedPaneUI.this.getFontMetrics();
/* 3148 */       Dimension localDimension = BasicTabbedPaneUI.this.tabPane.getSize();
/* 3149 */       Insets localInsets1 = BasicTabbedPaneUI.this.tabPane.getInsets();
/* 3150 */       Insets localInsets2 = BasicTabbedPaneUI.this.getTabAreaInsets(paramInt1);
/* 3151 */       int i = localFontMetrics.getHeight();
/* 3152 */       int j = BasicTabbedPaneUI.this.tabPane.getSelectedIndex();
/*      */ 
/* 3154 */       int m = (paramInt1 == 2) || (paramInt1 == 4) ? 1 : 0;
/* 3155 */       boolean bool = BasicGraphicsUtils.isLeftToRight(BasicTabbedPaneUI.this.tabPane);
/* 3156 */       int n = localInsets2.left;
/* 3157 */       int i1 = localInsets2.top;
/* 3158 */       int i2 = 0;
/* 3159 */       int i3 = 0;
/*      */ 
/* 3164 */       switch (paramInt1) {
/*      */       case 2:
/*      */       case 4:
/* 3167 */         BasicTabbedPaneUI.this.maxTabWidth = BasicTabbedPaneUI.this.calculateMaxTabWidth(paramInt1);
/* 3168 */         break;
/*      */       case 1:
/*      */       case 3:
/*      */       default:
/* 3172 */         BasicTabbedPaneUI.this.maxTabHeight = BasicTabbedPaneUI.this.calculateMaxTabHeight(paramInt1);
/*      */       }
/*      */ 
/* 3175 */       BasicTabbedPaneUI.this.runCount = 0;
/* 3176 */       BasicTabbedPaneUI.this.selectedRun = -1;
/*      */ 
/* 3178 */       if (paramInt2 == 0) {
/* 3179 */         return;
/*      */       }
/*      */ 
/* 3182 */       BasicTabbedPaneUI.this.selectedRun = 0;
/* 3183 */       BasicTabbedPaneUI.this.runCount = 1;
/*      */ 
/* 3187 */       for (int k = 0; k < paramInt2; k++) {
/* 3188 */         Rectangle localRectangle = BasicTabbedPaneUI.this.rects[k];
/*      */ 
/* 3190 */         if (m == 0)
/*      */         {
/* 3192 */           if (k > 0) {
/* 3193 */             localRectangle.x = (BasicTabbedPaneUI.this.rects[(k - 1)].x + BasicTabbedPaneUI.this.rects[(k - 1)].width);
/*      */           } else {
/* 3195 */             BasicTabbedPaneUI.this.tabRuns[0] = 0;
/* 3196 */             BasicTabbedPaneUI.this.maxTabWidth = 0;
/* 3197 */             i3 += BasicTabbedPaneUI.this.maxTabHeight;
/* 3198 */             localRectangle.x = n;
/*      */           }
/* 3200 */           localRectangle.width = BasicTabbedPaneUI.this.calculateTabWidth(paramInt1, k, localFontMetrics);
/* 3201 */           i2 = localRectangle.x + localRectangle.width;
/* 3202 */           BasicTabbedPaneUI.this.maxTabWidth = Math.max(BasicTabbedPaneUI.this.maxTabWidth, localRectangle.width);
/*      */ 
/* 3204 */           localRectangle.y = i1;
/* 3205 */           localRectangle.height = BasicTabbedPaneUI.this.maxTabHeight;
/*      */         }
/*      */         else
/*      */         {
/* 3209 */           if (k > 0) {
/* 3210 */             localRectangle.y = (BasicTabbedPaneUI.this.rects[(k - 1)].y + BasicTabbedPaneUI.this.rects[(k - 1)].height);
/*      */           } else {
/* 3212 */             BasicTabbedPaneUI.this.tabRuns[0] = 0;
/* 3213 */             BasicTabbedPaneUI.this.maxTabHeight = 0;
/* 3214 */             i2 = BasicTabbedPaneUI.this.maxTabWidth;
/* 3215 */             localRectangle.y = i1;
/*      */           }
/* 3217 */           localRectangle.height = BasicTabbedPaneUI.this.calculateTabHeight(paramInt1, k, i);
/* 3218 */           i3 = localRectangle.y + localRectangle.height;
/* 3219 */           BasicTabbedPaneUI.this.maxTabHeight = Math.max(BasicTabbedPaneUI.this.maxTabHeight, localRectangle.height);
/*      */ 
/* 3221 */           localRectangle.x = n;
/* 3222 */           localRectangle.width = BasicTabbedPaneUI.this.maxTabWidth;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3227 */       if (BasicTabbedPaneUI.this.tabsOverlapBorder)
/*      */       {
/* 3229 */         padSelectedTab(paramInt1, j);
/*      */       }
/*      */ 
/* 3234 */       if ((!bool) && (m == 0)) {
/* 3235 */         int i4 = localDimension.width - (localInsets1.right + localInsets2.right);
/*      */ 
/* 3237 */         for (k = 0; k < paramInt2; k++) {
/* 3238 */           BasicTabbedPaneUI.this.rects[k].x = (i4 - BasicTabbedPaneUI.this.rects[k].x - BasicTabbedPaneUI.this.rects[k].width);
/*      */         }
/*      */       }
/* 3241 */       BasicTabbedPaneUI.this.tabScroller.tabPanel.setPreferredSize(new Dimension(i2, i3));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTabbedPaneUI
 * JD-Core Version:    0.6.2
 */