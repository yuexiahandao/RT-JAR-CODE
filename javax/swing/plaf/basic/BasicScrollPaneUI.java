/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.MouseWheelListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.BoundedRangeModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JScrollBar;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.ScrollPaneConstants;
/*      */ import javax.swing.Scrollable;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ScrollPaneUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicScrollPaneUI extends ScrollPaneUI
/*      */   implements ScrollPaneConstants
/*      */ {
/*      */   protected JScrollPane scrollpane;
/*      */   protected ChangeListener vsbChangeListener;
/*      */   protected ChangeListener hsbChangeListener;
/*      */   protected ChangeListener viewportChangeListener;
/*      */   protected PropertyChangeListener spPropertyChangeListener;
/*      */   private MouseWheelListener mouseScrollListener;
/*      */   private int oldExtent;
/*      */   private PropertyChangeListener vsbPropertyChangeListener;
/*      */   private PropertyChangeListener hsbPropertyChangeListener;
/*      */   private Handler handler;
/*      */   private boolean setValueCalled;
/*      */ 
/*      */   public BasicScrollPaneUI()
/*      */   {
/*   61 */     this.oldExtent = -2147483648;
/*      */ 
/*   80 */     this.setValueCalled = false;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent) {
/*   84 */     return new BasicScrollPaneUI();
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*   88 */     paramLazyActionMap.put(new Actions("scrollUp"));
/*   89 */     paramLazyActionMap.put(new Actions("scrollDown"));
/*   90 */     paramLazyActionMap.put(new Actions("scrollHome"));
/*   91 */     paramLazyActionMap.put(new Actions("scrollEnd"));
/*   92 */     paramLazyActionMap.put(new Actions("unitScrollUp"));
/*   93 */     paramLazyActionMap.put(new Actions("unitScrollDown"));
/*   94 */     paramLazyActionMap.put(new Actions("scrollLeft"));
/*   95 */     paramLazyActionMap.put(new Actions("scrollRight"));
/*   96 */     paramLazyActionMap.put(new Actions("unitScrollRight"));
/*   97 */     paramLazyActionMap.put(new Actions("unitScrollLeft"));
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  103 */     Border localBorder = this.scrollpane.getViewportBorder();
/*  104 */     if (localBorder != null) {
/*  105 */       Rectangle localRectangle = this.scrollpane.getViewportBorderBounds();
/*  106 */       localBorder.paintBorder(this.scrollpane, paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  115 */     return new Dimension(32767, 32767);
/*      */   }
/*      */ 
/*      */   protected void installDefaults(JScrollPane paramJScrollPane)
/*      */   {
/*  121 */     LookAndFeel.installBorder(paramJScrollPane, "ScrollPane.border");
/*  122 */     LookAndFeel.installColorsAndFont(paramJScrollPane, "ScrollPane.background", "ScrollPane.foreground", "ScrollPane.font");
/*      */ 
/*  127 */     Border localBorder = paramJScrollPane.getViewportBorder();
/*  128 */     if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  129 */       localBorder = UIManager.getBorder("ScrollPane.viewportBorder");
/*  130 */       paramJScrollPane.setViewportBorder(localBorder);
/*      */     }
/*  132 */     LookAndFeel.installProperty(paramJScrollPane, "opaque", Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   protected void installListeners(JScrollPane paramJScrollPane)
/*      */   {
/*  138 */     this.vsbChangeListener = createVSBChangeListener();
/*  139 */     this.vsbPropertyChangeListener = createVSBPropertyChangeListener();
/*  140 */     this.hsbChangeListener = createHSBChangeListener();
/*  141 */     this.hsbPropertyChangeListener = createHSBPropertyChangeListener();
/*  142 */     this.viewportChangeListener = createViewportChangeListener();
/*  143 */     this.spPropertyChangeListener = createPropertyChangeListener();
/*      */ 
/*  145 */     JViewport localJViewport = this.scrollpane.getViewport();
/*  146 */     JScrollBar localJScrollBar1 = this.scrollpane.getVerticalScrollBar();
/*  147 */     JScrollBar localJScrollBar2 = this.scrollpane.getHorizontalScrollBar();
/*      */ 
/*  149 */     if (localJViewport != null) {
/*  150 */       localJViewport.addChangeListener(this.viewportChangeListener);
/*      */     }
/*  152 */     if (localJScrollBar1 != null) {
/*  153 */       localJScrollBar1.getModel().addChangeListener(this.vsbChangeListener);
/*  154 */       localJScrollBar1.addPropertyChangeListener(this.vsbPropertyChangeListener);
/*      */     }
/*  156 */     if (localJScrollBar2 != null) {
/*  157 */       localJScrollBar2.getModel().addChangeListener(this.hsbChangeListener);
/*  158 */       localJScrollBar2.addPropertyChangeListener(this.hsbPropertyChangeListener);
/*      */     }
/*      */ 
/*  161 */     this.scrollpane.addPropertyChangeListener(this.spPropertyChangeListener);
/*      */ 
/*  163 */     this.mouseScrollListener = createMouseWheelListener();
/*  164 */     this.scrollpane.addMouseWheelListener(this.mouseScrollListener);
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions(JScrollPane paramJScrollPane)
/*      */   {
/*  169 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  172 */     SwingUtilities.replaceUIInputMap(paramJScrollPane, 1, localInputMap);
/*      */ 
/*  175 */     LazyActionMap.installLazyActionMap(paramJScrollPane, BasicScrollPaneUI.class, "ScrollPane.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/*  180 */     if (paramInt == 1) {
/*  181 */       InputMap localInputMap1 = (InputMap)DefaultLookup.get(this.scrollpane, this, "ScrollPane.ancestorInputMap");
/*      */       InputMap localInputMap2;
/*  185 */       if ((this.scrollpane.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.scrollpane, this, "ScrollPane.ancestorInputMap.RightToLeft")) == null))
/*      */       {
/*  188 */         return localInputMap1;
/*      */       }
/*  190 */       localInputMap2.setParent(localInputMap1);
/*  191 */       return localInputMap2;
/*      */     }
/*      */ 
/*  194 */     return null;
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  198 */     this.scrollpane = ((JScrollPane)paramJComponent);
/*  199 */     installDefaults(this.scrollpane);
/*  200 */     installListeners(this.scrollpane);
/*  201 */     installKeyboardActions(this.scrollpane);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults(JScrollPane paramJScrollPane)
/*      */   {
/*  206 */     LookAndFeel.uninstallBorder(this.scrollpane);
/*      */ 
/*  208 */     if ((this.scrollpane.getViewportBorder() instanceof UIResource))
/*  209 */       this.scrollpane.setViewportBorder(null);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners(JComponent paramJComponent)
/*      */   {
/*  215 */     JViewport localJViewport = this.scrollpane.getViewport();
/*  216 */     JScrollBar localJScrollBar1 = this.scrollpane.getVerticalScrollBar();
/*  217 */     JScrollBar localJScrollBar2 = this.scrollpane.getHorizontalScrollBar();
/*      */ 
/*  219 */     if (localJViewport != null) {
/*  220 */       localJViewport.removeChangeListener(this.viewportChangeListener);
/*      */     }
/*  222 */     if (localJScrollBar1 != null) {
/*  223 */       localJScrollBar1.getModel().removeChangeListener(this.vsbChangeListener);
/*  224 */       localJScrollBar1.removePropertyChangeListener(this.vsbPropertyChangeListener);
/*      */     }
/*  226 */     if (localJScrollBar2 != null) {
/*  227 */       localJScrollBar2.getModel().removeChangeListener(this.hsbChangeListener);
/*  228 */       localJScrollBar2.removePropertyChangeListener(this.hsbPropertyChangeListener);
/*      */     }
/*      */ 
/*  231 */     this.scrollpane.removePropertyChangeListener(this.spPropertyChangeListener);
/*      */ 
/*  233 */     if (this.mouseScrollListener != null) {
/*  234 */       this.scrollpane.removeMouseWheelListener(this.mouseScrollListener);
/*      */     }
/*      */ 
/*  237 */     this.vsbChangeListener = null;
/*  238 */     this.hsbChangeListener = null;
/*  239 */     this.viewportChangeListener = null;
/*  240 */     this.spPropertyChangeListener = null;
/*  241 */     this.mouseScrollListener = null;
/*  242 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions(JScrollPane paramJScrollPane)
/*      */   {
/*  247 */     SwingUtilities.replaceUIActionMap(paramJScrollPane, null);
/*  248 */     SwingUtilities.replaceUIInputMap(paramJScrollPane, 1, null);
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  254 */     uninstallDefaults(this.scrollpane);
/*  255 */     uninstallListeners(this.scrollpane);
/*  256 */     uninstallKeyboardActions(this.scrollpane);
/*  257 */     this.scrollpane = null;
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  261 */     if (this.handler == null) {
/*  262 */       this.handler = new Handler();
/*      */     }
/*  264 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void syncScrollPaneWithViewport()
/*      */   {
/*  269 */     JViewport localJViewport1 = this.scrollpane.getViewport();
/*  270 */     JScrollBar localJScrollBar1 = this.scrollpane.getVerticalScrollBar();
/*  271 */     JScrollBar localJScrollBar2 = this.scrollpane.getHorizontalScrollBar();
/*  272 */     JViewport localJViewport2 = this.scrollpane.getRowHeader();
/*  273 */     JViewport localJViewport3 = this.scrollpane.getColumnHeader();
/*  274 */     boolean bool = this.scrollpane.getComponentOrientation().isLeftToRight();
/*      */ 
/*  276 */     if (localJViewport1 != null) {
/*  277 */       Dimension localDimension1 = localJViewport1.getExtentSize();
/*  278 */       Dimension localDimension2 = localJViewport1.getViewSize();
/*  279 */       Point localPoint1 = localJViewport1.getViewPosition();
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*  281 */       if (localJScrollBar1 != null) {
/*  282 */         i = localDimension1.height;
/*  283 */         j = localDimension2.height;
/*  284 */         k = Math.max(0, Math.min(localPoint1.y, j - i));
/*  285 */         localJScrollBar1.setValues(k, i, 0, j);
/*      */       }
/*      */ 
/*  288 */       if (localJScrollBar2 != null) {
/*  289 */         i = localDimension1.width;
/*  290 */         j = localDimension2.width;
/*      */ 
/*  293 */         if (bool) {
/*  294 */           k = Math.max(0, Math.min(localPoint1.x, j - i));
/*      */         } else {
/*  296 */           int m = localJScrollBar2.getValue();
/*      */ 
/*  301 */           if ((this.setValueCalled) && (j - m == localPoint1.x)) {
/*  302 */             k = Math.max(0, Math.min(j - i, m));
/*      */ 
/*  305 */             if (i != 0) {
/*  306 */               this.setValueCalled = false;
/*      */             }
/*      */           }
/*  309 */           else if (i > j) {
/*  310 */             localPoint1.x = (j - i);
/*  311 */             localJViewport1.setViewPosition(localPoint1);
/*  312 */             k = 0;
/*      */           }
/*      */           else
/*      */           {
/*  325 */             k = Math.max(0, Math.min(j - i, j - i - localPoint1.x));
/*  326 */             if (this.oldExtent > i) {
/*  327 */               k -= this.oldExtent - i;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  332 */         this.oldExtent = i;
/*  333 */         localJScrollBar2.setValues(k, i, 0, j);
/*      */       }
/*      */       Point localPoint2;
/*  336 */       if (localJViewport2 != null) {
/*  337 */         localPoint2 = localJViewport2.getViewPosition();
/*  338 */         localPoint2.y = localJViewport1.getViewPosition().y;
/*  339 */         localPoint2.x = 0;
/*  340 */         localJViewport2.setViewPosition(localPoint2);
/*      */       }
/*      */ 
/*  343 */       if (localJViewport3 != null) {
/*  344 */         localPoint2 = localJViewport3.getViewPosition();
/*  345 */         if (bool)
/*  346 */           localPoint2.x = localJViewport1.getViewPosition().x;
/*      */         else {
/*  348 */           localPoint2.x = Math.max(0, localJViewport1.getViewPosition().x);
/*      */         }
/*  350 */         localPoint2.y = 0;
/*  351 */         localJViewport3.setViewPosition(localPoint2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  365 */     if (paramJComponent == null) {
/*  366 */       throw new NullPointerException("Component must be non-null");
/*      */     }
/*      */ 
/*  369 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/*  370 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*      */     }
/*      */ 
/*  373 */     JViewport localJViewport1 = this.scrollpane.getViewport();
/*  374 */     Insets localInsets = this.scrollpane.getInsets();
/*  375 */     int i = localInsets.top;
/*  376 */     paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/*  377 */     paramInt1 = paramInt1 - localInsets.left - localInsets.right;
/*  378 */     JViewport localJViewport2 = this.scrollpane.getColumnHeader();
/*      */     Object localObject1;
/*  379 */     if ((localJViewport2 != null) && (localJViewport2.isVisible())) {
/*  380 */       localComponent = localJViewport2.getView();
/*  381 */       if ((localComponent != null) && (localComponent.isVisible()))
/*      */       {
/*  383 */         localObject1 = localComponent.getPreferredSize();
/*  384 */         int j = localComponent.getBaseline(((Dimension)localObject1).width, ((Dimension)localObject1).height);
/*      */ 
/*  386 */         if (j >= 0) {
/*  387 */           return i + j;
/*      */         }
/*      */       }
/*  390 */       localObject1 = localJViewport2.getPreferredSize();
/*  391 */       paramInt2 -= ((Dimension)localObject1).height;
/*  392 */       i += ((Dimension)localObject1).height;
/*      */     }
/*  394 */     Component localComponent = localJViewport1 == null ? null : localJViewport1.getView();
/*  395 */     if ((localComponent != null) && (localComponent.isVisible()) && (localComponent.getBaselineResizeBehavior() == Component.BaselineResizeBehavior.CONSTANT_ASCENT))
/*      */     {
/*  398 */       localObject1 = this.scrollpane.getViewportBorder();
/*      */       Object localObject2;
/*  399 */       if (localObject1 != null) {
/*  400 */         localObject2 = ((Border)localObject1).getBorderInsets(this.scrollpane);
/*  401 */         i += ((Insets)localObject2).top;
/*  402 */         paramInt2 = paramInt2 - ((Insets)localObject2).top - ((Insets)localObject2).bottom;
/*  403 */         paramInt1 = paramInt1 - ((Insets)localObject2).left - ((Insets)localObject2).right;
/*      */       }
/*  405 */       if ((localComponent.getWidth() > 0) && (localComponent.getHeight() > 0)) {
/*  406 */         localObject2 = localComponent.getMinimumSize();
/*  407 */         paramInt1 = Math.max(((Dimension)localObject2).width, localComponent.getWidth());
/*  408 */         paramInt2 = Math.max(((Dimension)localObject2).height, localComponent.getHeight());
/*      */       }
/*  410 */       if ((paramInt1 > 0) && (paramInt2 > 0)) {
/*  411 */         int k = localComponent.getBaseline(paramInt1, paramInt2);
/*  412 */         if (k > 0) {
/*  413 */           return i + k;
/*      */         }
/*      */       }
/*      */     }
/*  417 */     return -1;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  430 */     super.getBaselineResizeBehavior(paramJComponent);
/*      */ 
/*  436 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */   }
/*      */ 
/*      */   protected ChangeListener createViewportChangeListener()
/*      */   {
/*  457 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private PropertyChangeListener createHSBPropertyChangeListener()
/*      */   {
/*  483 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ChangeListener createHSBChangeListener() {
/*  487 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private PropertyChangeListener createVSBPropertyChangeListener()
/*      */   {
/*  514 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ChangeListener createVSBChangeListener() {
/*  518 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected MouseWheelListener createMouseWheelListener()
/*      */   {
/*  565 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected void updateScrollBarDisplayPolicy(PropertyChangeEvent paramPropertyChangeEvent) {
/*  569 */     this.scrollpane.revalidate();
/*  570 */     this.scrollpane.repaint();
/*      */   }
/*      */ 
/*      */   protected void updateViewport(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  576 */     JViewport localJViewport1 = (JViewport)paramPropertyChangeEvent.getOldValue();
/*  577 */     JViewport localJViewport2 = (JViewport)paramPropertyChangeEvent.getNewValue();
/*      */ 
/*  579 */     if (localJViewport1 != null) {
/*  580 */       localJViewport1.removeChangeListener(this.viewportChangeListener);
/*      */     }
/*      */ 
/*  583 */     if (localJViewport2 != null) {
/*  584 */       Point localPoint = localJViewport2.getViewPosition();
/*  585 */       if (this.scrollpane.getComponentOrientation().isLeftToRight()) {
/*  586 */         localPoint.x = Math.max(localPoint.x, 0);
/*      */       } else {
/*  588 */         int i = localJViewport2.getViewSize().width;
/*  589 */         int j = localJViewport2.getExtentSize().width;
/*  590 */         if (j > i)
/*  591 */           localPoint.x = (i - j);
/*      */         else {
/*  593 */           localPoint.x = Math.max(0, Math.min(i - j, localPoint.x));
/*      */         }
/*      */       }
/*  596 */       localPoint.y = Math.max(localPoint.y, 0);
/*  597 */       localJViewport2.setViewPosition(localPoint);
/*  598 */       localJViewport2.addChangeListener(this.viewportChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateRowHeader(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  605 */     JViewport localJViewport1 = (JViewport)paramPropertyChangeEvent.getNewValue();
/*  606 */     if (localJViewport1 != null) {
/*  607 */       JViewport localJViewport2 = this.scrollpane.getViewport();
/*  608 */       Point localPoint = localJViewport1.getViewPosition();
/*  609 */       localPoint.y = (localJViewport2 != null ? localJViewport2.getViewPosition().y : 0);
/*  610 */       localJViewport1.setViewPosition(localPoint);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateColumnHeader(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  617 */     JViewport localJViewport1 = (JViewport)paramPropertyChangeEvent.getNewValue();
/*  618 */     if (localJViewport1 != null) {
/*  619 */       JViewport localJViewport2 = this.scrollpane.getViewport();
/*  620 */       Point localPoint = localJViewport1.getViewPosition();
/*  621 */       if (localJViewport2 == null) {
/*  622 */         localPoint.x = 0;
/*      */       }
/*  624 */       else if (this.scrollpane.getComponentOrientation().isLeftToRight())
/*  625 */         localPoint.x = localJViewport2.getViewPosition().x;
/*      */       else {
/*  627 */         localPoint.x = Math.max(0, localJViewport2.getViewPosition().x);
/*      */       }
/*      */ 
/*  630 */       localJViewport1.setViewPosition(localPoint);
/*  631 */       this.scrollpane.add(localJViewport1, "COLUMN_HEADER");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateHorizontalScrollBar(PropertyChangeEvent paramPropertyChangeEvent) {
/*  636 */     updateScrollBar(paramPropertyChangeEvent, this.hsbChangeListener, this.hsbPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   private void updateVerticalScrollBar(PropertyChangeEvent paramPropertyChangeEvent) {
/*  640 */     updateScrollBar(paramPropertyChangeEvent, this.vsbChangeListener, this.vsbPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   private void updateScrollBar(PropertyChangeEvent paramPropertyChangeEvent, ChangeListener paramChangeListener, PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  645 */     JScrollBar localJScrollBar = (JScrollBar)paramPropertyChangeEvent.getOldValue();
/*  646 */     if (localJScrollBar != null) {
/*  647 */       if (paramChangeListener != null) {
/*  648 */         localJScrollBar.getModel().removeChangeListener(paramChangeListener);
/*      */       }
/*  650 */       if (paramPropertyChangeListener != null) {
/*  651 */         localJScrollBar.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */     }
/*  654 */     localJScrollBar = (JScrollBar)paramPropertyChangeEvent.getNewValue();
/*  655 */     if (localJScrollBar != null) {
/*  656 */       if (paramChangeListener != null) {
/*  657 */         localJScrollBar.getModel().addChangeListener(paramChangeListener);
/*      */       }
/*  659 */       if (paramPropertyChangeListener != null)
/*  660 */         localJScrollBar.addPropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  705 */     return getHandler(); } 
/*      */   private static class Actions extends UIAction { private static final String SCROLL_UP = "scrollUp";
/*      */     private static final String SCROLL_DOWN = "scrollDown";
/*      */     private static final String SCROLL_HOME = "scrollHome";
/*      */     private static final String SCROLL_END = "scrollEnd";
/*      */     private static final String UNIT_SCROLL_UP = "unitScrollUp";
/*      */     private static final String UNIT_SCROLL_DOWN = "unitScrollDown";
/*      */     private static final String SCROLL_LEFT = "scrollLeft";
/*      */     private static final String SCROLL_RIGHT = "scrollRight";
/*      */     private static final String UNIT_SCROLL_LEFT = "unitScrollLeft";
/*      */     private static final String UNIT_SCROLL_RIGHT = "unitScrollRight";
/*      */ 
/*  723 */     Actions(String paramString) { super(); }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  727 */       JScrollPane localJScrollPane = (JScrollPane)paramActionEvent.getSource();
/*  728 */       boolean bool = localJScrollPane.getComponentOrientation().isLeftToRight();
/*  729 */       String str = getName();
/*      */ 
/*  731 */       if (str == "scrollUp") {
/*  732 */         scroll(localJScrollPane, 1, -1, true);
/*      */       }
/*  734 */       else if (str == "scrollDown") {
/*  735 */         scroll(localJScrollPane, 1, 1, true);
/*      */       }
/*  737 */       else if (str == "scrollHome") {
/*  738 */         scrollHome(localJScrollPane);
/*      */       }
/*  740 */       else if (str == "scrollEnd") {
/*  741 */         scrollEnd(localJScrollPane);
/*      */       }
/*  743 */       else if (str == "unitScrollUp") {
/*  744 */         scroll(localJScrollPane, 1, -1, false);
/*      */       }
/*  746 */       else if (str == "unitScrollDown") {
/*  747 */         scroll(localJScrollPane, 1, 1, false);
/*      */       }
/*  749 */       else if (str == "scrollLeft") {
/*  750 */         scroll(localJScrollPane, 0, bool ? -1 : 1, true);
/*      */       }
/*  753 */       else if (str == "scrollRight") {
/*  754 */         scroll(localJScrollPane, 0, bool ? 1 : -1, true);
/*      */       }
/*  757 */       else if (str == "unitScrollLeft") {
/*  758 */         scroll(localJScrollPane, 0, bool ? -1 : 1, false);
/*      */       }
/*  761 */       else if (str == "unitScrollRight")
/*  762 */         scroll(localJScrollPane, 0, bool ? 1 : -1, false);
/*      */     }
/*      */ 
/*      */     private void scrollEnd(JScrollPane paramJScrollPane)
/*      */     {
/*  768 */       JViewport localJViewport = paramJScrollPane.getViewport();
/*      */       Component localComponent;
/*  770 */       if ((localJViewport != null) && ((localComponent = localJViewport.getView()) != null)) {
/*  771 */         Rectangle localRectangle1 = localJViewport.getViewRect();
/*  772 */         Rectangle localRectangle2 = localComponent.getBounds();
/*  773 */         if (paramJScrollPane.getComponentOrientation().isLeftToRight()) {
/*  774 */           localJViewport.setViewPosition(new Point(localRectangle2.width - localRectangle1.width, localRectangle2.height - localRectangle1.height));
/*      */         }
/*      */         else
/*  777 */           localJViewport.setViewPosition(new Point(0, localRectangle2.height - localRectangle1.height));
/*      */       }
/*      */     }
/*      */ 
/*      */     private void scrollHome(JScrollPane paramJScrollPane)
/*      */     {
/*  784 */       JViewport localJViewport = paramJScrollPane.getViewport();
/*      */       Component localComponent;
/*  786 */       if ((localJViewport != null) && ((localComponent = localJViewport.getView()) != null))
/*  787 */         if (paramJScrollPane.getComponentOrientation().isLeftToRight()) {
/*  788 */           localJViewport.setViewPosition(new Point(0, 0));
/*      */         } else {
/*  790 */           Rectangle localRectangle1 = localJViewport.getViewRect();
/*  791 */           Rectangle localRectangle2 = localComponent.getBounds();
/*  792 */           localJViewport.setViewPosition(new Point(localRectangle2.width - localRectangle1.width, 0));
/*      */         }
/*      */     }
/*      */ 
/*      */     private void scroll(JScrollPane paramJScrollPane, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */     {
/*  799 */       JViewport localJViewport = paramJScrollPane.getViewport();
/*      */       Component localComponent;
/*  801 */       if ((localJViewport != null) && ((localComponent = localJViewport.getView()) != null)) {
/*  802 */         Rectangle localRectangle = localJViewport.getViewRect();
/*  803 */         Dimension localDimension = localComponent.getSize();
/*      */         int i;
/*  806 */         if ((localComponent instanceof Scrollable)) {
/*  807 */           if (paramBoolean) {
/*  808 */             i = ((Scrollable)localComponent).getScrollableBlockIncrement(localRectangle, paramInt1, paramInt2);
/*      */           }
/*      */           else
/*      */           {
/*  812 */             i = ((Scrollable)localComponent).getScrollableUnitIncrement(localRectangle, paramInt1, paramInt2);
/*      */           }
/*      */ 
/*      */         }
/*  817 */         else if (paramBoolean) {
/*  818 */           if (paramInt1 == 1) {
/*  819 */             i = localRectangle.height;
/*      */           }
/*      */           else {
/*  822 */             i = localRectangle.width;
/*      */           }
/*      */         }
/*      */         else {
/*  826 */           i = 10;
/*      */         }
/*      */ 
/*  829 */         if (paramInt1 == 1) {
/*  830 */           localRectangle.y += i * paramInt2;
/*  831 */           if (localRectangle.y + localRectangle.height > localDimension.height) {
/*  832 */             localRectangle.y = Math.max(0, localDimension.height - localRectangle.height);
/*      */           }
/*  834 */           else if (localRectangle.y < 0) {
/*  835 */             localRectangle.y = 0;
/*      */           }
/*      */ 
/*      */         }
/*  839 */         else if (paramJScrollPane.getComponentOrientation().isLeftToRight()) {
/*  840 */           localRectangle.x += i * paramInt2;
/*  841 */           if (localRectangle.x + localRectangle.width > localDimension.width)
/*  842 */             localRectangle.x = Math.max(0, localDimension.width - localRectangle.width);
/*  843 */           else if (localRectangle.x < 0)
/*  844 */             localRectangle.x = 0;
/*      */         }
/*      */         else {
/*  847 */           localRectangle.x -= i * paramInt2;
/*  848 */           if (localRectangle.width > localDimension.width)
/*  849 */             localRectangle.x = (localDimension.width - localRectangle.width);
/*      */           else {
/*  851 */             localRectangle.x = Math.max(0, Math.min(localDimension.width - localRectangle.width, localRectangle.x));
/*      */           }
/*      */         }
/*      */ 
/*  855 */         localJViewport.setViewPosition(localRectangle.getLocation());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class HSBChangeListener
/*      */     implements ChangeListener
/*      */   {
/*      */     public HSBChangeListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  474 */       BasicScrollPaneUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   class Handler
/*      */     implements ChangeListener, PropertyChangeListener, MouseWheelListener
/*      */   {
/*      */     Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent)
/*      */     {
/*  866 */       if ((BasicScrollPaneUI.this.scrollpane.isWheelScrollingEnabled()) && (paramMouseWheelEvent.getWheelRotation() != 0))
/*      */       {
/*  868 */         JScrollBar localJScrollBar = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
/*  869 */         int i = paramMouseWheelEvent.getWheelRotation() < 0 ? -1 : 1;
/*  870 */         int j = 1;
/*      */ 
/*  873 */         if ((localJScrollBar == null) || (!localJScrollBar.isVisible())) {
/*  874 */           localJScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/*  875 */           if ((localJScrollBar == null) || (!localJScrollBar.isVisible())) {
/*  876 */             return;
/*      */           }
/*  878 */           j = 0;
/*      */         }
/*      */ 
/*  881 */         paramMouseWheelEvent.consume();
/*      */ 
/*  883 */         if (paramMouseWheelEvent.getScrollType() == 0) {
/*  884 */           JViewport localJViewport = BasicScrollPaneUI.this.scrollpane.getViewport();
/*  885 */           if (localJViewport == null) return;
/*  886 */           Component localComponent = localJViewport.getView();
/*  887 */           int k = Math.abs(paramMouseWheelEvent.getUnitsToScroll());
/*      */ 
/*  896 */           boolean bool1 = Math.abs(paramMouseWheelEvent.getWheelRotation()) == 1;
/*      */ 
/*  899 */           Object localObject = localJScrollBar.getClientProperty("JScrollBar.fastWheelScrolling");
/*      */ 
/*  901 */           if ((Boolean.TRUE == localObject) && ((localComponent instanceof Scrollable)))
/*      */           {
/*  913 */             Scrollable localScrollable = (Scrollable)localComponent;
/*  914 */             Rectangle localRectangle = localJViewport.getViewRect();
/*  915 */             int m = localRectangle.x;
/*  916 */             boolean bool2 = localComponent.getComponentOrientation().isLeftToRight();
/*      */ 
/*  918 */             int n = localJScrollBar.getMinimum();
/*  919 */             int i1 = localJScrollBar.getMaximum() - localJScrollBar.getModel().getExtent();
/*      */ 
/*  922 */             if (bool1) {
/*  923 */               i2 = localScrollable.getScrollableBlockIncrement(localRectangle, j, i);
/*      */ 
/*  927 */               if (i < 0) {
/*  928 */                 n = Math.max(n, localJScrollBar.getValue() - i2);
/*      */               }
/*      */               else
/*      */               {
/*  932 */                 i1 = Math.min(i1, localJScrollBar.getValue() + i2);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  937 */             for (int i2 = 0; i2 < k; i2++) {
/*  938 */               int i3 = localScrollable.getScrollableUnitIncrement(localRectangle, j, i);
/*      */ 
/*  943 */               if (j == 1) {
/*  944 */                 if (i < 0) {
/*  945 */                   localRectangle.y -= i3;
/*  946 */                   if (localRectangle.y <= n) {
/*  947 */                     localRectangle.y = n;
/*  948 */                     break;
/*      */                   }
/*      */                 }
/*      */                 else {
/*  952 */                   localRectangle.y += i3;
/*  953 */                   if (localRectangle.y >= i1) {
/*  954 */                     localRectangle.y = i1;
/*  955 */                     break;
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*      */               }
/*  961 */               else if (((bool2) && (i < 0)) || ((!bool2) && (i > 0)))
/*      */               {
/*  963 */                 localRectangle.x -= i3;
/*  964 */                 if ((bool2) && 
/*  965 */                   (localRectangle.x < n)) {
/*  966 */                   localRectangle.x = n;
/*  967 */                   break;
/*      */                 }
/*      */ 
/*      */               }
/*  972 */               else if (((bool2) && (i > 0)) || ((!bool2) && (i < 0)))
/*      */               {
/*  974 */                 localRectangle.x += i3;
/*  975 */                 if ((bool2) && 
/*  976 */                   (localRectangle.x > i1)) {
/*  977 */                   localRectangle.x = i1;
/*  978 */                   break;
/*      */                 }
/*      */ 
/*      */               }
/*  983 */               else if (!$assertionsDisabled) { throw new AssertionError("Non-sensical ComponentOrientation / scroll direction"); }
/*      */ 
/*      */ 
/*      */             }
/*      */ 
/*  988 */             if (j == 1) {
/*  989 */               localJScrollBar.setValue(localRectangle.y);
/*      */             }
/*  992 */             else if (bool2) {
/*  993 */               localJScrollBar.setValue(localRectangle.x);
/*      */             }
/*      */             else
/*      */             {
/*  999 */               i2 = localJScrollBar.getValue() - (localRectangle.x - m);
/*      */ 
/* 1001 */               if (i2 < n) {
/* 1002 */                 i2 = n;
/*      */               }
/* 1004 */               else if (i2 > i1) {
/* 1005 */                 i2 = i1;
/*      */               }
/* 1007 */               localJScrollBar.setValue(i2);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1014 */             BasicScrollBarUI.scrollByUnits(localJScrollBar, i, k, bool1);
/*      */           }
/*      */ 
/*      */         }
/* 1018 */         else if (paramMouseWheelEvent.getScrollType() == 1)
/*      */         {
/* 1020 */           BasicScrollBarUI.scrollByBlock(localJScrollBar, i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1029 */       JViewport localJViewport = BasicScrollPaneUI.this.scrollpane.getViewport();
/*      */ 
/* 1031 */       if (localJViewport != null)
/* 1032 */         if (paramChangeEvent.getSource() == localJViewport) {
/* 1033 */           BasicScrollPaneUI.this.syncScrollPaneWithViewport();
/*      */         }
/*      */         else {
/* 1036 */           JScrollBar localJScrollBar1 = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/* 1037 */           if ((localJScrollBar1 != null) && (paramChangeEvent.getSource() == localJScrollBar1.getModel())) {
/* 1038 */             hsbStateChanged(localJViewport, paramChangeEvent);
/*      */           }
/*      */           else {
/* 1041 */             JScrollBar localJScrollBar2 = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
/* 1042 */             if ((localJScrollBar2 != null) && (paramChangeEvent.getSource() == localJScrollBar2.getModel()))
/* 1043 */               vsbStateChanged(localJViewport, paramChangeEvent);
/*      */           }
/*      */         }
/*      */     }
/*      */ 
/*      */     private void vsbStateChanged(JViewport paramJViewport, ChangeEvent paramChangeEvent)
/*      */     {
/* 1051 */       BoundedRangeModel localBoundedRangeModel = (BoundedRangeModel)paramChangeEvent.getSource();
/* 1052 */       Point localPoint = paramJViewport.getViewPosition();
/* 1053 */       localPoint.y = localBoundedRangeModel.getValue();
/* 1054 */       paramJViewport.setViewPosition(localPoint);
/*      */     }
/*      */ 
/*      */     private void hsbStateChanged(JViewport paramJViewport, ChangeEvent paramChangeEvent) {
/* 1058 */       BoundedRangeModel localBoundedRangeModel = (BoundedRangeModel)paramChangeEvent.getSource();
/* 1059 */       Point localPoint = paramJViewport.getViewPosition();
/* 1060 */       int i = localBoundedRangeModel.getValue();
/* 1061 */       if (BasicScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight()) {
/* 1062 */         localPoint.x = i;
/*      */       } else {
/* 1064 */         int j = paramJViewport.getViewSize().width;
/* 1065 */         int k = paramJViewport.getExtentSize().width;
/* 1066 */         int m = localPoint.x;
/*      */ 
/* 1070 */         localPoint.x = (j - k - i);
/*      */ 
/* 1075 */         if ((k == 0) && (i != 0) && (m == j)) {
/* 1076 */           BasicScrollPaneUI.this.setValueCalled = true;
/*      */         }
/* 1082 */         else if ((k != 0) && (m < 0) && (localPoint.x == 0)) {
/* 1083 */           localPoint.x += i;
/*      */         }
/*      */       }
/*      */ 
/* 1087 */       paramJViewport.setViewPosition(localPoint);
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1098 */       if (paramPropertyChangeEvent.getSource() == BasicScrollPaneUI.this.scrollpane) {
/* 1099 */         scrollPanePropertyChange(paramPropertyChangeEvent);
/*      */       }
/*      */       else
/* 1102 */         sbPropertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */ 
/*      */     private void scrollPanePropertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1107 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */ 
/* 1109 */       if (str == "verticalScrollBarDisplayPolicy") {
/* 1110 */         BasicScrollPaneUI.this.updateScrollBarDisplayPolicy(paramPropertyChangeEvent);
/*      */       }
/* 1112 */       else if (str == "horizontalScrollBarDisplayPolicy") {
/* 1113 */         BasicScrollPaneUI.this.updateScrollBarDisplayPolicy(paramPropertyChangeEvent);
/*      */       }
/* 1115 */       else if (str == "viewport") {
/* 1116 */         BasicScrollPaneUI.this.updateViewport(paramPropertyChangeEvent);
/*      */       }
/* 1118 */       else if (str == "rowHeader") {
/* 1119 */         BasicScrollPaneUI.this.updateRowHeader(paramPropertyChangeEvent);
/*      */       }
/* 1121 */       else if (str == "columnHeader") {
/* 1122 */         BasicScrollPaneUI.this.updateColumnHeader(paramPropertyChangeEvent);
/*      */       }
/* 1124 */       else if (str == "verticalScrollBar") {
/* 1125 */         BasicScrollPaneUI.this.updateVerticalScrollBar(paramPropertyChangeEvent);
/*      */       }
/* 1127 */       else if (str == "horizontalScrollBar") {
/* 1128 */         BasicScrollPaneUI.this.updateHorizontalScrollBar(paramPropertyChangeEvent);
/*      */       }
/* 1130 */       else if (str == "componentOrientation") {
/* 1131 */         BasicScrollPaneUI.this.scrollpane.revalidate();
/* 1132 */         BasicScrollPaneUI.this.scrollpane.repaint();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void sbPropertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1138 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1139 */       Object localObject1 = paramPropertyChangeEvent.getSource();
/*      */       JScrollBar localJScrollBar;
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 1141 */       if ("model" == str) {
/* 1142 */         localJScrollBar = BasicScrollPaneUI.this.scrollpane.getVerticalScrollBar();
/* 1143 */         localObject2 = (BoundedRangeModel)paramPropertyChangeEvent.getOldValue();
/*      */ 
/* 1145 */         localObject3 = null;
/*      */ 
/* 1147 */         if (localObject1 == localJScrollBar) {
/* 1148 */           localObject3 = BasicScrollPaneUI.this.vsbChangeListener;
/*      */         }
/* 1150 */         else if (localObject1 == BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) {
/* 1151 */           localJScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/* 1152 */           localObject3 = BasicScrollPaneUI.this.hsbChangeListener;
/*      */         }
/* 1154 */         if (localObject3 != null) {
/* 1155 */           if (localObject2 != null) {
/* 1156 */             ((BoundedRangeModel)localObject2).removeChangeListener((MouseWheelListener)localObject3);
/*      */           }
/* 1158 */           if (localJScrollBar.getModel() != null) {
/* 1159 */             localJScrollBar.getModel().addChangeListener((MouseWheelListener)localObject3);
/*      */           }
/*      */         }
/*      */       }
/* 1163 */       else if (("componentOrientation" == str) && 
/* 1164 */         (localObject1 == BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar())) {
/* 1165 */         localJScrollBar = BasicScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/* 1166 */         localObject2 = BasicScrollPaneUI.this.scrollpane.getViewport();
/* 1167 */         localObject3 = ((JViewport)localObject2).getViewPosition();
/* 1168 */         if (BasicScrollPaneUI.this.scrollpane.getComponentOrientation().isLeftToRight())
/* 1169 */           ((Point)localObject3).x = localJScrollBar.getValue();
/*      */         else {
/* 1171 */           ((Point)localObject3).x = (((JViewport)localObject2).getViewSize().width - ((JViewport)localObject2).getExtentSize().width - localJScrollBar.getValue());
/*      */         }
/* 1173 */         ((JViewport)localObject2).setViewPosition((Point)localObject3);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class MouseWheelHandler
/*      */     implements MouseWheelListener
/*      */   {
/*      */     protected MouseWheelHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent)
/*      */     {
/*  550 */       BasicScrollPaneUI.this.getHandler().mouseWheelMoved(paramMouseWheelEvent);
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
/*  675 */       BasicScrollPaneUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class VSBChangeListener
/*      */     implements ChangeListener
/*      */   {
/*      */     public VSBChangeListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  504 */       BasicScrollPaneUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ViewportChangeHandler
/*      */     implements ChangeListener
/*      */   {
/*      */     public ViewportChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  452 */       BasicScrollPaneUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicScrollPaneUI
 * JD-Core Version:    0.6.2
 */