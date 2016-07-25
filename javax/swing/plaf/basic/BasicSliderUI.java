/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.BoundedRangeModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JSlider;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.MouseInputAdapter;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.SliderUI;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicSliderUI extends SliderUI
/*      */ {
/*   48 */   private static final Actions SHARED_ACTION = new Actions();
/*      */   public static final int POSITIVE_SCROLL = 1;
/*      */   public static final int NEGATIVE_SCROLL = -1;
/*      */   public static final int MIN_SCROLL = -2;
/*      */   public static final int MAX_SCROLL = 2;
/*      */   protected Timer scrollTimer;
/*      */   protected JSlider slider;
/*   58 */   protected Insets focusInsets = null;
/*   59 */   protected Insets insetCache = null;
/*   60 */   protected boolean leftToRightCache = true;
/*   61 */   protected Rectangle focusRect = null;
/*   62 */   protected Rectangle contentRect = null;
/*   63 */   protected Rectangle labelRect = null;
/*   64 */   protected Rectangle tickRect = null;
/*   65 */   protected Rectangle trackRect = null;
/*   66 */   protected Rectangle thumbRect = null;
/*      */ 
/*   68 */   protected int trackBuffer = 0;
/*      */   private transient boolean isDragging;
/*      */   protected TrackListener trackListener;
/*      */   protected ChangeListener changeListener;
/*      */   protected ComponentListener componentListener;
/*      */   protected FocusListener focusListener;
/*      */   protected ScrollListener scrollListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   private Handler handler;
/*      */   private int lastValue;
/*      */   private Color shadowColor;
/*      */   private Color highlightColor;
/*      */   private Color focusColor;
/*      */   private boolean checkedLabelBaselines;
/*      */   private boolean sameLabelBaselines;
/* 1300 */   private static Rectangle unionRect = new Rectangle();
/*      */ 
/*      */   protected Color getShadowColor()
/*      */   {
/*   98 */     return this.shadowColor;
/*      */   }
/*      */ 
/*      */   protected Color getHighlightColor() {
/*  102 */     return this.highlightColor;
/*      */   }
/*      */ 
/*      */   protected Color getFocusColor() {
/*  106 */     return this.focusColor;
/*      */   }
/*      */ 
/*      */   protected boolean isDragging()
/*      */   {
/*  116 */     return this.isDragging;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  123 */     return new BasicSliderUI((JSlider)paramJComponent);
/*      */   }
/*      */ 
/*      */   public BasicSliderUI(JSlider paramJSlider) {
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent) {
/*  130 */     this.slider = ((JSlider)paramJComponent);
/*      */ 
/*  132 */     this.checkedLabelBaselines = false;
/*      */ 
/*  134 */     this.slider.setEnabled(this.slider.isEnabled());
/*  135 */     LookAndFeel.installProperty(this.slider, "opaque", Boolean.TRUE);
/*      */ 
/*  137 */     this.isDragging = false;
/*  138 */     this.trackListener = createTrackListener(this.slider);
/*  139 */     this.changeListener = createChangeListener(this.slider);
/*  140 */     this.componentListener = createComponentListener(this.slider);
/*  141 */     this.focusListener = createFocusListener(this.slider);
/*  142 */     this.scrollListener = createScrollListener(this.slider);
/*  143 */     this.propertyChangeListener = createPropertyChangeListener(this.slider);
/*      */ 
/*  145 */     installDefaults(this.slider);
/*  146 */     installListeners(this.slider);
/*  147 */     installKeyboardActions(this.slider);
/*      */ 
/*  149 */     this.scrollTimer = new Timer(100, this.scrollListener);
/*  150 */     this.scrollTimer.setInitialDelay(300);
/*      */ 
/*  152 */     this.insetCache = this.slider.getInsets();
/*  153 */     this.leftToRightCache = BasicGraphicsUtils.isLeftToRight(this.slider);
/*  154 */     this.focusRect = new Rectangle();
/*  155 */     this.contentRect = new Rectangle();
/*  156 */     this.labelRect = new Rectangle();
/*  157 */     this.tickRect = new Rectangle();
/*  158 */     this.trackRect = new Rectangle();
/*  159 */     this.thumbRect = new Rectangle();
/*  160 */     this.lastValue = this.slider.getValue();
/*      */ 
/*  162 */     calculateGeometry();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  166 */     if (paramJComponent != this.slider) {
/*  167 */       throw new IllegalComponentStateException(this + " was asked to deinstall() " + paramJComponent + " when it only knows about " + this.slider + ".");
/*      */     }
/*      */ 
/*  172 */     this.scrollTimer.stop();
/*  173 */     this.scrollTimer = null;
/*      */ 
/*  175 */     uninstallDefaults(this.slider);
/*  176 */     uninstallListeners(this.slider);
/*  177 */     uninstallKeyboardActions(this.slider);
/*      */ 
/*  179 */     this.insetCache = null;
/*  180 */     this.leftToRightCache = true;
/*  181 */     this.focusRect = null;
/*  182 */     this.contentRect = null;
/*  183 */     this.labelRect = null;
/*  184 */     this.tickRect = null;
/*  185 */     this.trackRect = null;
/*  186 */     this.thumbRect = null;
/*  187 */     this.trackListener = null;
/*  188 */     this.changeListener = null;
/*  189 */     this.componentListener = null;
/*  190 */     this.focusListener = null;
/*  191 */     this.scrollListener = null;
/*  192 */     this.propertyChangeListener = null;
/*  193 */     this.slider = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults(JSlider paramJSlider) {
/*  197 */     LookAndFeel.installBorder(paramJSlider, "Slider.border");
/*  198 */     LookAndFeel.installColorsAndFont(paramJSlider, "Slider.background", "Slider.foreground", "Slider.font");
/*      */ 
/*  200 */     this.highlightColor = UIManager.getColor("Slider.highlight");
/*      */ 
/*  202 */     this.shadowColor = UIManager.getColor("Slider.shadow");
/*  203 */     this.focusColor = UIManager.getColor("Slider.focus");
/*      */ 
/*  205 */     this.focusInsets = ((Insets)UIManager.get("Slider.focusInsets"));
/*      */ 
/*  208 */     if (this.focusInsets == null) this.focusInsets = new InsetsUIResource(2, 2, 2, 2); 
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults(JSlider paramJSlider)
/*      */   {
/*  212 */     LookAndFeel.uninstallBorder(paramJSlider);
/*      */ 
/*  214 */     this.focusInsets = null;
/*      */   }
/*      */ 
/*      */   protected TrackListener createTrackListener(JSlider paramJSlider) {
/*  218 */     return new TrackListener();
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener(JSlider paramJSlider) {
/*  222 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ComponentListener createComponentListener(JSlider paramJSlider) {
/*  226 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener(JSlider paramJSlider) {
/*  230 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ScrollListener createScrollListener(JSlider paramJSlider) {
/*  234 */     return new ScrollListener();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener(JSlider paramJSlider)
/*      */   {
/*  239 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  243 */     if (this.handler == null) {
/*  244 */       this.handler = new Handler(null);
/*      */     }
/*  246 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void installListeners(JSlider paramJSlider) {
/*  250 */     paramJSlider.addMouseListener(this.trackListener);
/*  251 */     paramJSlider.addMouseMotionListener(this.trackListener);
/*  252 */     paramJSlider.addFocusListener(this.focusListener);
/*  253 */     paramJSlider.addComponentListener(this.componentListener);
/*  254 */     paramJSlider.addPropertyChangeListener(this.propertyChangeListener);
/*  255 */     paramJSlider.getModel().addChangeListener(this.changeListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners(JSlider paramJSlider) {
/*  259 */     paramJSlider.removeMouseListener(this.trackListener);
/*  260 */     paramJSlider.removeMouseMotionListener(this.trackListener);
/*  261 */     paramJSlider.removeFocusListener(this.focusListener);
/*  262 */     paramJSlider.removeComponentListener(this.componentListener);
/*  263 */     paramJSlider.removePropertyChangeListener(this.propertyChangeListener);
/*  264 */     paramJSlider.getModel().removeChangeListener(this.changeListener);
/*  265 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions(JSlider paramJSlider) {
/*  269 */     InputMap localInputMap = getInputMap(0, paramJSlider);
/*  270 */     SwingUtilities.replaceUIInputMap(paramJSlider, 0, localInputMap);
/*  271 */     LazyActionMap.installLazyActionMap(paramJSlider, BasicSliderUI.class, "Slider.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt, JSlider paramJSlider)
/*      */   {
/*  276 */     if (paramInt == 0) {
/*  277 */       InputMap localInputMap1 = (InputMap)DefaultLookup.get(paramJSlider, this, "Slider.focusInputMap");
/*      */       InputMap localInputMap2;
/*  281 */       if ((paramJSlider.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(paramJSlider, this, "Slider.focusInputMap.RightToLeft")) == null))
/*      */       {
/*  284 */         return localInputMap1;
/*      */       }
/*  286 */       localInputMap2.setParent(localInputMap1);
/*  287 */       return localInputMap2;
/*      */     }
/*      */ 
/*  290 */     return null;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  297 */     paramLazyActionMap.put(new Actions("positiveUnitIncrement"));
/*  298 */     paramLazyActionMap.put(new Actions("positiveBlockIncrement"));
/*  299 */     paramLazyActionMap.put(new Actions("negativeUnitIncrement"));
/*  300 */     paramLazyActionMap.put(new Actions("negativeBlockIncrement"));
/*  301 */     paramLazyActionMap.put(new Actions("minScroll"));
/*  302 */     paramLazyActionMap.put(new Actions("maxScroll"));
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions(JSlider paramJSlider) {
/*  306 */     SwingUtilities.replaceUIActionMap(paramJSlider, null);
/*  307 */     SwingUtilities.replaceUIInputMap(paramJSlider, 0, null);
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  321 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  322 */     if ((this.slider.getPaintLabels()) && (labelsHaveSameBaselines())) {
/*  323 */       FontMetrics localFontMetrics = this.slider.getFontMetrics(this.slider.getFont());
/*  324 */       Insets localInsets = this.slider.getInsets();
/*  325 */       Dimension localDimension = getThumbSize();
/*      */       int k;
/*      */       int m;
/*      */       int n;
/*      */       int i1;
/*      */       int i2;
/*      */       int i3;
/*  326 */       if (this.slider.getOrientation() == 0) {
/*  327 */         int i = getTickLength();
/*  328 */         int j = paramInt2 - localInsets.top - localInsets.bottom - this.focusInsets.top - this.focusInsets.bottom;
/*      */ 
/*  330 */         k = localDimension.height;
/*  331 */         m = k;
/*  332 */         if (this.slider.getPaintTicks()) {
/*  333 */           m += i;
/*      */         }
/*      */ 
/*  336 */         m += getHeightOfTallestLabel();
/*  337 */         n = localInsets.top + this.focusInsets.top + (j - m - 1) / 2;
/*      */ 
/*  339 */         i1 = k;
/*  340 */         i2 = n + i1;
/*  341 */         i3 = i;
/*  342 */         if (!this.slider.getPaintTicks()) {
/*  343 */           i3 = 0;
/*      */         }
/*  345 */         int i4 = i2 + i3;
/*  346 */         return i4 + localFontMetrics.getAscent();
/*      */       }
/*      */ 
/*  349 */       boolean bool = this.slider.getInverted();
/*  350 */       Integer localInteger = bool ? getLowestValue() : getHighestValue();
/*      */ 
/*  352 */       if (localInteger != null) {
/*  353 */         k = localDimension.height;
/*  354 */         m = Math.max(localFontMetrics.getHeight() / 2, k / 2);
/*      */ 
/*  356 */         n = this.focusInsets.top + localInsets.top;
/*  357 */         i1 = n + m;
/*  358 */         i2 = paramInt2 - this.focusInsets.top - this.focusInsets.bottom - localInsets.top - localInsets.bottom - m - m;
/*      */ 
/*  361 */         i3 = yPositionForValue(localInteger.intValue(), i1, i2);
/*      */ 
/*  363 */         return i3 - localFontMetrics.getHeight() / 2 + localFontMetrics.getAscent();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  368 */     return 0;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  381 */     super.getBaselineResizeBehavior(paramJComponent);
/*      */ 
/*  386 */     return Component.BaselineResizeBehavior.OTHER;
/*      */   }
/*      */ 
/*      */   protected boolean labelsHaveSameBaselines()
/*      */   {
/*  398 */     if (!this.checkedLabelBaselines) {
/*  399 */       this.checkedLabelBaselines = true;
/*  400 */       Dictionary localDictionary = this.slider.getLabelTable();
/*  401 */       if (localDictionary != null) {
/*  402 */         this.sameLabelBaselines = true;
/*  403 */         Enumeration localEnumeration = localDictionary.elements();
/*  404 */         int i = -1;
/*  405 */         while (localEnumeration.hasMoreElements()) {
/*  406 */           JComponent localJComponent = (JComponent)localEnumeration.nextElement();
/*  407 */           Dimension localDimension = localJComponent.getPreferredSize();
/*  408 */           int j = localJComponent.getBaseline(localDimension.width, localDimension.height);
/*      */ 
/*  410 */           if (j >= 0) {
/*  411 */             if (i == -1) {
/*  412 */               i = j;
/*      */             }
/*  414 */             else if (i != j) {
/*  415 */               this.sameLabelBaselines = false;
/*  416 */               break;
/*      */             }
/*      */           }
/*      */           else {
/*  420 */             this.sameLabelBaselines = false;
/*  421 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  426 */         this.sameLabelBaselines = false;
/*      */       }
/*      */     }
/*  429 */     return this.sameLabelBaselines;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredHorizontalSize() {
/*  433 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.slider, this, "Slider.horizontalSize");
/*      */ 
/*  435 */     if (localDimension == null) {
/*  436 */       localDimension = new Dimension(200, 21);
/*      */     }
/*  438 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredVerticalSize() {
/*  442 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.slider, this, "Slider.verticalSize");
/*      */ 
/*  444 */     if (localDimension == null) {
/*  445 */       localDimension = new Dimension(21, 200);
/*      */     }
/*  447 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumHorizontalSize() {
/*  451 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.slider, this, "Slider.minimumHorizontalSize");
/*      */ 
/*  453 */     if (localDimension == null) {
/*  454 */       localDimension = new Dimension(36, 21);
/*      */     }
/*  456 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumVerticalSize() {
/*  460 */     Dimension localDimension = (Dimension)DefaultLookup.get(this.slider, this, "Slider.minimumVerticalSize");
/*      */ 
/*  462 */     if (localDimension == null) {
/*  463 */       localDimension = new Dimension(21, 36);
/*      */     }
/*  465 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent) {
/*  469 */     recalculateIfInsetsChanged();
/*      */     Dimension localDimension;
/*  471 */     if (this.slider.getOrientation() == 1) {
/*  472 */       localDimension = new Dimension(getPreferredVerticalSize());
/*  473 */       localDimension.width = (this.insetCache.left + this.insetCache.right);
/*  474 */       localDimension.width += this.focusInsets.left + this.focusInsets.right;
/*  475 */       localDimension.width += this.trackRect.width + this.tickRect.width + this.labelRect.width;
/*      */     }
/*      */     else {
/*  478 */       localDimension = new Dimension(getPreferredHorizontalSize());
/*  479 */       localDimension.height = (this.insetCache.top + this.insetCache.bottom);
/*  480 */       localDimension.height += this.focusInsets.top + this.focusInsets.bottom;
/*  481 */       localDimension.height += this.trackRect.height + this.tickRect.height + this.labelRect.height;
/*      */     }
/*      */ 
/*  484 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent) {
/*  488 */     recalculateIfInsetsChanged();
/*      */     Dimension localDimension;
/*  491 */     if (this.slider.getOrientation() == 1) {
/*  492 */       localDimension = new Dimension(getMinimumVerticalSize());
/*  493 */       localDimension.width = (this.insetCache.left + this.insetCache.right);
/*  494 */       localDimension.width += this.focusInsets.left + this.focusInsets.right;
/*  495 */       localDimension.width += this.trackRect.width + this.tickRect.width + this.labelRect.width;
/*      */     }
/*      */     else {
/*  498 */       localDimension = new Dimension(getMinimumHorizontalSize());
/*  499 */       localDimension.height = (this.insetCache.top + this.insetCache.bottom);
/*  500 */       localDimension.height += this.focusInsets.top + this.focusInsets.bottom;
/*  501 */       localDimension.height += this.trackRect.height + this.tickRect.height + this.labelRect.height;
/*      */     }
/*      */ 
/*  504 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent) {
/*  508 */     Dimension localDimension = getPreferredSize(paramJComponent);
/*  509 */     if (this.slider.getOrientation() == 1) {
/*  510 */       localDimension.height = 32767;
/*      */     }
/*      */     else {
/*  513 */       localDimension.width = 32767;
/*      */     }
/*      */ 
/*  516 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected void calculateGeometry() {
/*  520 */     calculateFocusRect();
/*  521 */     calculateContentRect();
/*  522 */     calculateThumbSize();
/*  523 */     calculateTrackBuffer();
/*  524 */     calculateTrackRect();
/*  525 */     calculateTickRect();
/*  526 */     calculateLabelRect();
/*  527 */     calculateThumbLocation();
/*      */   }
/*      */ 
/*      */   protected void calculateFocusRect() {
/*  531 */     this.focusRect.x = this.insetCache.left;
/*  532 */     this.focusRect.y = this.insetCache.top;
/*  533 */     this.focusRect.width = (this.slider.getWidth() - (this.insetCache.left + this.insetCache.right));
/*  534 */     this.focusRect.height = (this.slider.getHeight() - (this.insetCache.top + this.insetCache.bottom));
/*      */   }
/*      */ 
/*      */   protected void calculateThumbSize() {
/*  538 */     Dimension localDimension = getThumbSize();
/*  539 */     this.thumbRect.setSize(localDimension.width, localDimension.height);
/*      */   }
/*      */ 
/*      */   protected void calculateContentRect() {
/*  543 */     this.contentRect.x = (this.focusRect.x + this.focusInsets.left);
/*  544 */     this.contentRect.y = (this.focusRect.y + this.focusInsets.top);
/*  545 */     this.contentRect.width = (this.focusRect.width - (this.focusInsets.left + this.focusInsets.right));
/*  546 */     this.contentRect.height = (this.focusRect.height - (this.focusInsets.top + this.focusInsets.bottom));
/*      */   }
/*      */ 
/*      */   private int getTickSpacing() {
/*  550 */     int i = this.slider.getMajorTickSpacing();
/*  551 */     int j = this.slider.getMinorTickSpacing();
/*      */     int k;
/*  555 */     if (j > 0)
/*  556 */       k = j;
/*  557 */     else if (i > 0)
/*  558 */       k = i;
/*      */     else {
/*  560 */       k = 0;
/*      */     }
/*      */ 
/*  563 */     return k;
/*      */   }
/*      */ 
/*      */   protected void calculateThumbLocation()
/*      */   {
/*      */     int i;
/*  567 */     if (this.slider.getSnapToTicks()) {
/*  568 */       i = this.slider.getValue();
/*  569 */       int j = i;
/*  570 */       int k = getTickSpacing();
/*      */ 
/*  572 */       if (k != 0)
/*      */       {
/*  574 */         if ((i - this.slider.getMinimum()) % k != 0) {
/*  575 */           float f = (i - this.slider.getMinimum()) / k;
/*  576 */           int m = Math.round(f);
/*      */ 
/*  579 */           if ((f - (int)f == 0.5D) && (i < this.lastValue)) {
/*  580 */             m--;
/*      */           }
/*  582 */           j = this.slider.getMinimum() + m * k;
/*      */         }
/*      */ 
/*  585 */         if (j != i) {
/*  586 */           this.slider.setValue(j);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  591 */     if (this.slider.getOrientation() == 0) {
/*  592 */       i = xPositionForValue(this.slider.getValue());
/*      */ 
/*  594 */       this.thumbRect.x = (i - this.thumbRect.width / 2);
/*  595 */       this.thumbRect.y = this.trackRect.y;
/*      */     }
/*      */     else {
/*  598 */       i = yPositionForValue(this.slider.getValue());
/*      */ 
/*  600 */       this.thumbRect.x = this.trackRect.x;
/*  601 */       this.thumbRect.y = (i - this.thumbRect.height / 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void calculateTrackBuffer() {
/*  606 */     if ((this.slider.getPaintLabels()) && (this.slider.getLabelTable() != null)) {
/*  607 */       Component localComponent1 = getHighestValueLabel();
/*  608 */       Component localComponent2 = getLowestValueLabel();
/*      */ 
/*  610 */       if (this.slider.getOrientation() == 0) {
/*  611 */         this.trackBuffer = (Math.max(localComponent1.getBounds().width, localComponent2.getBounds().width) / 2);
/*  612 */         this.trackBuffer = Math.max(this.trackBuffer, this.thumbRect.width / 2);
/*      */       }
/*      */       else {
/*  615 */         this.trackBuffer = (Math.max(localComponent1.getBounds().height, localComponent2.getBounds().height) / 2);
/*  616 */         this.trackBuffer = Math.max(this.trackBuffer, this.thumbRect.height / 2);
/*      */       }
/*      */ 
/*      */     }
/*  620 */     else if (this.slider.getOrientation() == 0) {
/*  621 */       this.trackBuffer = (this.thumbRect.width / 2);
/*      */     }
/*      */     else {
/*  624 */       this.trackBuffer = (this.thumbRect.height / 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void calculateTrackRect()
/*      */   {
/*      */     int i;
/*  632 */     if (this.slider.getOrientation() == 0) {
/*  633 */       i = this.thumbRect.height;
/*  634 */       if (this.slider.getPaintTicks()) i += getTickLength();
/*  635 */       if (this.slider.getPaintLabels()) i += getHeightOfTallestLabel();
/*  636 */       this.trackRect.x = (this.contentRect.x + this.trackBuffer);
/*  637 */       this.trackRect.y = (this.contentRect.y + (this.contentRect.height - i - 1) / 2);
/*  638 */       this.trackRect.width = (this.contentRect.width - this.trackBuffer * 2);
/*  639 */       this.trackRect.height = this.thumbRect.height;
/*      */     }
/*      */     else {
/*  642 */       i = this.thumbRect.width;
/*  643 */       if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/*  644 */         if (this.slider.getPaintTicks()) i += getTickLength();
/*  645 */         if (this.slider.getPaintLabels()) i += getWidthOfWidestLabel(); 
/*      */       }
/*  647 */       else { if (this.slider.getPaintTicks()) i -= getTickLength();
/*  648 */         if (this.slider.getPaintLabels()) i -= getWidthOfWidestLabel();
/*      */       }
/*  650 */       this.trackRect.x = (this.contentRect.x + (this.contentRect.width - i - 1) / 2);
/*  651 */       this.trackRect.y = (this.contentRect.y + this.trackBuffer);
/*  652 */       this.trackRect.width = this.thumbRect.width;
/*  653 */       this.trackRect.height = (this.contentRect.height - this.trackBuffer * 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getTickLength()
/*      */   {
/*  665 */     return 8;
/*      */   }
/*      */ 
/*      */   protected void calculateTickRect() {
/*  669 */     if (this.slider.getOrientation() == 0) {
/*  670 */       this.tickRect.x = this.trackRect.x;
/*  671 */       this.tickRect.y = (this.trackRect.y + this.trackRect.height);
/*  672 */       this.tickRect.width = this.trackRect.width;
/*  673 */       this.tickRect.height = (this.slider.getPaintTicks() ? getTickLength() : 0);
/*      */     }
/*      */     else {
/*  676 */       this.tickRect.width = (this.slider.getPaintTicks() ? getTickLength() : 0);
/*  677 */       if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/*  678 */         this.tickRect.x = (this.trackRect.x + this.trackRect.width);
/*      */       }
/*      */       else {
/*  681 */         this.tickRect.x = (this.trackRect.x - this.tickRect.width);
/*      */       }
/*  683 */       this.tickRect.y = this.trackRect.y;
/*  684 */       this.tickRect.height = this.trackRect.height;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void calculateLabelRect() {
/*  689 */     if (this.slider.getPaintLabels()) {
/*  690 */       if (this.slider.getOrientation() == 0) {
/*  691 */         this.labelRect.x = (this.tickRect.x - this.trackBuffer);
/*  692 */         this.labelRect.y = (this.tickRect.y + this.tickRect.height);
/*  693 */         this.labelRect.width = (this.tickRect.width + this.trackBuffer * 2);
/*  694 */         this.labelRect.height = getHeightOfTallestLabel();
/*      */       }
/*      */       else {
/*  697 */         if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/*  698 */           this.labelRect.x = (this.tickRect.x + this.tickRect.width);
/*  699 */           this.labelRect.width = getWidthOfWidestLabel();
/*      */         }
/*      */         else {
/*  702 */           this.labelRect.width = getWidthOfWidestLabel();
/*  703 */           this.labelRect.x = (this.tickRect.x - this.labelRect.width);
/*      */         }
/*  705 */         this.labelRect.y = (this.tickRect.y - this.trackBuffer);
/*  706 */         this.labelRect.height = (this.tickRect.height + this.trackBuffer * 2);
/*      */       }
/*      */ 
/*      */     }
/*  710 */     else if (this.slider.getOrientation() == 0) {
/*  711 */       this.labelRect.x = this.tickRect.x;
/*  712 */       this.labelRect.y = (this.tickRect.y + this.tickRect.height);
/*  713 */       this.labelRect.width = this.tickRect.width;
/*  714 */       this.labelRect.height = 0;
/*      */     }
/*      */     else {
/*  717 */       if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/*  718 */         this.labelRect.x = (this.tickRect.x + this.tickRect.width);
/*      */       }
/*      */       else {
/*  721 */         this.labelRect.x = this.tickRect.x;
/*      */       }
/*  723 */       this.labelRect.y = this.tickRect.y;
/*  724 */       this.labelRect.width = 0;
/*  725 */       this.labelRect.height = this.tickRect.height;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Dimension getThumbSize()
/*      */   {
/*  731 */     Dimension localDimension = new Dimension();
/*      */ 
/*  733 */     if (this.slider.getOrientation() == 1) {
/*  734 */       localDimension.width = 20;
/*  735 */       localDimension.height = 11;
/*      */     }
/*      */     else {
/*  738 */       localDimension.width = 11;
/*  739 */       localDimension.height = 20;
/*      */     }
/*      */ 
/*  742 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected int getWidthOfWidestLabel()
/*      */   {
/*  756 */     Dictionary localDictionary = this.slider.getLabelTable();
/*  757 */     int i = 0;
/*  758 */     if (localDictionary != null) {
/*  759 */       Enumeration localEnumeration = localDictionary.keys();
/*  760 */       while (localEnumeration.hasMoreElements()) {
/*  761 */         JComponent localJComponent = (JComponent)localDictionary.get(localEnumeration.nextElement());
/*  762 */         i = Math.max(localJComponent.getPreferredSize().width, i);
/*      */       }
/*      */     }
/*  765 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getHeightOfTallestLabel() {
/*  769 */     Dictionary localDictionary = this.slider.getLabelTable();
/*  770 */     int i = 0;
/*  771 */     if (localDictionary != null) {
/*  772 */       Enumeration localEnumeration = localDictionary.keys();
/*  773 */       while (localEnumeration.hasMoreElements()) {
/*  774 */         JComponent localJComponent = (JComponent)localDictionary.get(localEnumeration.nextElement());
/*  775 */         i = Math.max(localJComponent.getPreferredSize().height, i);
/*      */       }
/*      */     }
/*  778 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getWidthOfHighValueLabel() {
/*  782 */     Component localComponent = getHighestValueLabel();
/*  783 */     int i = 0;
/*      */ 
/*  785 */     if (localComponent != null) {
/*  786 */       i = localComponent.getPreferredSize().width;
/*      */     }
/*      */ 
/*  789 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getWidthOfLowValueLabel() {
/*  793 */     Component localComponent = getLowestValueLabel();
/*  794 */     int i = 0;
/*      */ 
/*  796 */     if (localComponent != null) {
/*  797 */       i = localComponent.getPreferredSize().width;
/*      */     }
/*      */ 
/*  800 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getHeightOfHighValueLabel() {
/*  804 */     Component localComponent = getHighestValueLabel();
/*  805 */     int i = 0;
/*      */ 
/*  807 */     if (localComponent != null) {
/*  808 */       i = localComponent.getPreferredSize().height;
/*      */     }
/*      */ 
/*  811 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getHeightOfLowValueLabel() {
/*  815 */     Component localComponent = getLowestValueLabel();
/*  816 */     int i = 0;
/*      */ 
/*  818 */     if (localComponent != null) {
/*  819 */       i = localComponent.getPreferredSize().height;
/*      */     }
/*      */ 
/*  822 */     return i;
/*      */   }
/*      */ 
/*      */   protected boolean drawInverted() {
/*  826 */     if (this.slider.getOrientation() == 0) {
/*  827 */       if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/*  828 */         return this.slider.getInverted();
/*      */       }
/*  830 */       return !this.slider.getInverted();
/*      */     }
/*      */ 
/*  833 */     return this.slider.getInverted();
/*      */   }
/*      */ 
/*      */   protected Integer getHighestValue()
/*      */   {
/*  845 */     Dictionary localDictionary = this.slider.getLabelTable();
/*      */ 
/*  847 */     if (localDictionary == null) {
/*  848 */       return null;
/*      */     }
/*      */ 
/*  851 */     Enumeration localEnumeration = localDictionary.keys();
/*      */ 
/*  853 */     Object localObject = null;
/*      */ 
/*  855 */     while (localEnumeration.hasMoreElements()) {
/*  856 */       Integer localInteger = (Integer)localEnumeration.nextElement();
/*      */ 
/*  858 */       if ((localObject == null) || (localInteger.intValue() > localObject.intValue())) {
/*  859 */         localObject = localInteger;
/*      */       }
/*      */     }
/*      */ 
/*  863 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected Integer getLowestValue()
/*      */   {
/*  874 */     Dictionary localDictionary = this.slider.getLabelTable();
/*      */ 
/*  876 */     if (localDictionary == null) {
/*  877 */       return null;
/*      */     }
/*      */ 
/*  880 */     Enumeration localEnumeration = localDictionary.keys();
/*      */ 
/*  882 */     Object localObject = null;
/*      */ 
/*  884 */     while (localEnumeration.hasMoreElements()) {
/*  885 */       Integer localInteger = (Integer)localEnumeration.nextElement();
/*      */ 
/*  887 */       if ((localObject == null) || (localInteger.intValue() < localObject.intValue())) {
/*  888 */         localObject = localInteger;
/*      */       }
/*      */     }
/*      */ 
/*  892 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected Component getLowestValueLabel()
/*      */   {
/*  901 */     Integer localInteger = getLowestValue();
/*  902 */     if (localInteger != null) {
/*  903 */       return (Component)this.slider.getLabelTable().get(localInteger);
/*      */     }
/*  905 */     return null;
/*      */   }
/*      */ 
/*      */   protected Component getHighestValueLabel()
/*      */   {
/*  913 */     Integer localInteger = getHighestValue();
/*  914 */     if (localInteger != null) {
/*  915 */       return (Component)this.slider.getLabelTable().get(localInteger);
/*      */     }
/*  917 */     return null;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*  921 */     recalculateIfInsetsChanged();
/*  922 */     recalculateIfOrientationChanged();
/*  923 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/*      */ 
/*  925 */     if ((!localRectangle.intersects(this.trackRect)) && (this.slider.getPaintTrack())) {
/*  926 */       calculateGeometry();
/*      */     }
/*  928 */     if ((this.slider.getPaintTrack()) && (localRectangle.intersects(this.trackRect))) {
/*  929 */       paintTrack(paramGraphics);
/*      */     }
/*  931 */     if ((this.slider.getPaintTicks()) && (localRectangle.intersects(this.tickRect))) {
/*  932 */       paintTicks(paramGraphics);
/*      */     }
/*  934 */     if ((this.slider.getPaintLabels()) && (localRectangle.intersects(this.labelRect))) {
/*  935 */       paintLabels(paramGraphics);
/*      */     }
/*  937 */     if ((this.slider.hasFocus()) && (localRectangle.intersects(this.focusRect))) {
/*  938 */       paintFocus(paramGraphics);
/*      */     }
/*  940 */     if (localRectangle.intersects(this.thumbRect))
/*  941 */       paintThumb(paramGraphics);
/*      */   }
/*      */ 
/*      */   protected void recalculateIfInsetsChanged()
/*      */   {
/*  946 */     Insets localInsets = this.slider.getInsets();
/*  947 */     if (!localInsets.equals(this.insetCache)) {
/*  948 */       this.insetCache = localInsets;
/*  949 */       calculateGeometry();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void recalculateIfOrientationChanged() {
/*  954 */     boolean bool = BasicGraphicsUtils.isLeftToRight(this.slider);
/*  955 */     if (bool != this.leftToRightCache) {
/*  956 */       this.leftToRightCache = bool;
/*  957 */       calculateGeometry();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintFocus(Graphics paramGraphics) {
/*  962 */     paramGraphics.setColor(getFocusColor());
/*      */ 
/*  964 */     BasicGraphicsUtils.drawDashedRect(paramGraphics, this.focusRect.x, this.focusRect.y, this.focusRect.width, this.focusRect.height);
/*      */   }
/*      */ 
/*      */   public void paintTrack(Graphics paramGraphics)
/*      */   {
/*  970 */     Rectangle localRectangle = this.trackRect;
/*      */     int i;
/*      */     int j;
/*  972 */     if (this.slider.getOrientation() == 0) {
/*  973 */       i = localRectangle.height / 2 - 2;
/*  974 */       j = localRectangle.width;
/*      */ 
/*  976 */       paramGraphics.translate(localRectangle.x, localRectangle.y + i);
/*      */ 
/*  978 */       paramGraphics.setColor(getShadowColor());
/*  979 */       paramGraphics.drawLine(0, 0, j - 1, 0);
/*  980 */       paramGraphics.drawLine(0, 1, 0, 2);
/*  981 */       paramGraphics.setColor(getHighlightColor());
/*  982 */       paramGraphics.drawLine(0, 3, j, 3);
/*  983 */       paramGraphics.drawLine(j, 0, j, 3);
/*  984 */       paramGraphics.setColor(Color.black);
/*  985 */       paramGraphics.drawLine(1, 1, j - 2, 1);
/*      */ 
/*  987 */       paramGraphics.translate(-localRectangle.x, -(localRectangle.y + i));
/*      */     }
/*      */     else {
/*  990 */       i = localRectangle.width / 2 - 2;
/*  991 */       j = localRectangle.height;
/*      */ 
/*  993 */       paramGraphics.translate(localRectangle.x + i, localRectangle.y);
/*      */ 
/*  995 */       paramGraphics.setColor(getShadowColor());
/*  996 */       paramGraphics.drawLine(0, 0, 0, j - 1);
/*  997 */       paramGraphics.drawLine(1, 0, 2, 0);
/*  998 */       paramGraphics.setColor(getHighlightColor());
/*  999 */       paramGraphics.drawLine(3, 0, 3, j);
/* 1000 */       paramGraphics.drawLine(0, j, 3, j);
/* 1001 */       paramGraphics.setColor(Color.black);
/* 1002 */       paramGraphics.drawLine(1, 1, 1, j - 2);
/*      */ 
/* 1004 */       paramGraphics.translate(-(localRectangle.x + i), -localRectangle.y);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintTicks(Graphics paramGraphics) {
/* 1009 */     Rectangle localRectangle = this.tickRect;
/*      */ 
/* 1011 */     paramGraphics.setColor(DefaultLookup.getColor(this.slider, this, "Slider.tickColor", Color.black));
/*      */     int i;
/*      */     int j;
/* 1013 */     if (this.slider.getOrientation() == 0) {
/* 1014 */       paramGraphics.translate(0, localRectangle.y);
/*      */ 
/* 1016 */       if (this.slider.getMinorTickSpacing() > 0) {
/* 1017 */         i = this.slider.getMinimum();
/*      */ 
/* 1019 */         while (i <= this.slider.getMaximum()) {
/* 1020 */           j = xPositionForValue(i);
/* 1021 */           paintMinorTickForHorizSlider(paramGraphics, localRectangle, j);
/*      */ 
/* 1024 */           if (2147483647 - this.slider.getMinorTickSpacing() < i)
/*      */           {
/*      */             break;
/*      */           }
/* 1028 */           i += this.slider.getMinorTickSpacing();
/*      */         }
/*      */       }
/*      */ 
/* 1032 */       if (this.slider.getMajorTickSpacing() > 0) {
/* 1033 */         i = this.slider.getMinimum();
/*      */ 
/* 1035 */         while (i <= this.slider.getMaximum()) {
/* 1036 */           j = xPositionForValue(i);
/* 1037 */           paintMajorTickForHorizSlider(paramGraphics, localRectangle, j);
/*      */ 
/* 1040 */           if (2147483647 - this.slider.getMajorTickSpacing() < i)
/*      */           {
/*      */             break;
/*      */           }
/* 1044 */           i += this.slider.getMajorTickSpacing();
/*      */         }
/*      */       }
/*      */ 
/* 1048 */       paramGraphics.translate(0, -localRectangle.y);
/*      */     } else {
/* 1050 */       paramGraphics.translate(localRectangle.x, 0);
/*      */ 
/* 1052 */       if (this.slider.getMinorTickSpacing() > 0) {
/* 1053 */         i = 0;
/* 1054 */         if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1055 */           i = localRectangle.width - localRectangle.width / 2;
/* 1056 */           paramGraphics.translate(i, 0);
/*      */         }
/*      */ 
/* 1059 */         j = this.slider.getMinimum();
/*      */ 
/* 1061 */         while (j <= this.slider.getMaximum()) {
/* 1062 */           int k = yPositionForValue(j);
/* 1063 */           paintMinorTickForVertSlider(paramGraphics, localRectangle, k);
/*      */ 
/* 1066 */           if (2147483647 - this.slider.getMinorTickSpacing() < j)
/*      */           {
/*      */             break;
/*      */           }
/* 1070 */           j += this.slider.getMinorTickSpacing();
/*      */         }
/*      */ 
/* 1073 */         if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1074 */           paramGraphics.translate(-i, 0);
/*      */         }
/*      */       }
/*      */ 
/* 1078 */       if (this.slider.getMajorTickSpacing() > 0) {
/* 1079 */         if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1080 */           paramGraphics.translate(2, 0);
/*      */         }
/*      */ 
/* 1083 */         i = this.slider.getMinimum();
/*      */ 
/* 1085 */         while (i <= this.slider.getMaximum()) {
/* 1086 */           j = yPositionForValue(i);
/* 1087 */           paintMajorTickForVertSlider(paramGraphics, localRectangle, j);
/*      */ 
/* 1090 */           if (2147483647 - this.slider.getMajorTickSpacing() < i)
/*      */           {
/*      */             break;
/*      */           }
/* 1094 */           i += this.slider.getMajorTickSpacing();
/*      */         }
/*      */ 
/* 1097 */         if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1098 */           paramGraphics.translate(-2, 0);
/*      */         }
/*      */       }
/* 1101 */       paramGraphics.translate(-localRectangle.x, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintMinorTickForHorizSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 1106 */     paramGraphics.drawLine(paramInt, 0, paramInt, paramRectangle.height / 2 - 1);
/*      */   }
/*      */ 
/*      */   protected void paintMajorTickForHorizSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 1110 */     paramGraphics.drawLine(paramInt, 0, paramInt, paramRectangle.height - 2);
/*      */   }
/*      */ 
/*      */   protected void paintMinorTickForVertSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 1114 */     paramGraphics.drawLine(0, paramInt, paramRectangle.width / 2 - 1, paramInt);
/*      */   }
/*      */ 
/*      */   protected void paintMajorTickForVertSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 1118 */     paramGraphics.drawLine(0, paramInt, paramRectangle.width - 2, paramInt);
/*      */   }
/*      */ 
/*      */   public void paintLabels(Graphics paramGraphics) {
/* 1122 */     Rectangle localRectangle = this.labelRect;
/*      */ 
/* 1124 */     Dictionary localDictionary = this.slider.getLabelTable();
/* 1125 */     if (localDictionary != null) {
/* 1126 */       Enumeration localEnumeration = localDictionary.keys();
/* 1127 */       int i = this.slider.getMinimum();
/* 1128 */       int j = this.slider.getMaximum();
/* 1129 */       boolean bool = this.slider.isEnabled();
/* 1130 */       while (localEnumeration.hasMoreElements()) {
/* 1131 */         Integer localInteger = (Integer)localEnumeration.nextElement();
/* 1132 */         int k = localInteger.intValue();
/* 1133 */         if ((k >= i) && (k <= j)) {
/* 1134 */           JComponent localJComponent = (JComponent)localDictionary.get(localInteger);
/* 1135 */           localJComponent.setEnabled(bool);
/*      */ 
/* 1137 */           if ((localJComponent instanceof JLabel)) {
/* 1138 */             Icon localIcon = localJComponent.isEnabled() ? ((JLabel)localJComponent).getIcon() : ((JLabel)localJComponent).getDisabledIcon();
/*      */ 
/* 1140 */             if ((localIcon instanceof ImageIcon))
/*      */             {
/* 1143 */               Toolkit.getDefaultToolkit().checkImage(((ImageIcon)localIcon).getImage(), -1, -1, this.slider);
/*      */             }
/*      */           }
/*      */ 
/* 1147 */           if (this.slider.getOrientation() == 0) {
/* 1148 */             paramGraphics.translate(0, localRectangle.y);
/* 1149 */             paintHorizontalLabel(paramGraphics, k, localJComponent);
/* 1150 */             paramGraphics.translate(0, -localRectangle.y);
/*      */           }
/*      */           else {
/* 1153 */             int m = 0;
/* 1154 */             if (!BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1155 */               m = localRectangle.width - localJComponent.getPreferredSize().width;
/*      */             }
/*      */ 
/* 1158 */             paramGraphics.translate(localRectangle.x + m, 0);
/* 1159 */             paintVerticalLabel(paramGraphics, k, localJComponent);
/* 1160 */             paramGraphics.translate(-localRectangle.x - m, 0);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintHorizontalLabel(Graphics paramGraphics, int paramInt, Component paramComponent)
/*      */   {
/* 1174 */     int i = xPositionForValue(paramInt);
/* 1175 */     int j = i - paramComponent.getPreferredSize().width / 2;
/* 1176 */     paramGraphics.translate(j, 0);
/* 1177 */     paramComponent.paint(paramGraphics);
/* 1178 */     paramGraphics.translate(-j, 0);
/*      */   }
/*      */ 
/*      */   protected void paintVerticalLabel(Graphics paramGraphics, int paramInt, Component paramComponent)
/*      */   {
/* 1187 */     int i = yPositionForValue(paramInt);
/* 1188 */     int j = i - paramComponent.getPreferredSize().height / 2;
/* 1189 */     paramGraphics.translate(0, j);
/* 1190 */     paramComponent.paint(paramGraphics);
/* 1191 */     paramGraphics.translate(0, -j);
/*      */   }
/*      */ 
/*      */   public void paintThumb(Graphics paramGraphics) {
/* 1195 */     Rectangle localRectangle = this.thumbRect;
/* 1196 */     int i = localRectangle.width;
/* 1197 */     int j = localRectangle.height;
/*      */ 
/* 1199 */     paramGraphics.translate(localRectangle.x, localRectangle.y);
/*      */ 
/* 1201 */     if (this.slider.isEnabled()) {
/* 1202 */       paramGraphics.setColor(this.slider.getBackground());
/*      */     }
/*      */     else {
/* 1205 */       paramGraphics.setColor(this.slider.getBackground().darker());
/*      */     }
/*      */ 
/* 1208 */     Boolean localBoolean = (Boolean)this.slider.getClientProperty("Slider.paintThumbArrowShape");
/*      */ 
/* 1211 */     if (((!this.slider.getPaintTicks()) && (localBoolean == null)) || (localBoolean == Boolean.FALSE))
/*      */     {
/* 1215 */       paramGraphics.fillRect(0, 0, i, j);
/*      */ 
/* 1217 */       paramGraphics.setColor(Color.black);
/* 1218 */       paramGraphics.drawLine(0, j - 1, i - 1, j - 1);
/* 1219 */       paramGraphics.drawLine(i - 1, 0, i - 1, j - 1);
/*      */ 
/* 1221 */       paramGraphics.setColor(this.highlightColor);
/* 1222 */       paramGraphics.drawLine(0, 0, 0, j - 2);
/* 1223 */       paramGraphics.drawLine(1, 0, i - 2, 0);
/*      */ 
/* 1225 */       paramGraphics.setColor(this.shadowColor);
/* 1226 */       paramGraphics.drawLine(1, j - 2, i - 2, j - 2);
/* 1227 */       paramGraphics.drawLine(i - 2, 1, i - 2, j - 3);
/*      */     }
/*      */     else
/*      */     {
/*      */       int k;
/*      */       Polygon localPolygon;
/* 1229 */       if (this.slider.getOrientation() == 0) {
/* 1230 */         k = i / 2;
/* 1231 */         paramGraphics.fillRect(1, 1, i - 3, j - 1 - k);
/* 1232 */         localPolygon = new Polygon();
/* 1233 */         localPolygon.addPoint(1, j - k);
/* 1234 */         localPolygon.addPoint(k - 1, j - 1);
/* 1235 */         localPolygon.addPoint(i - 2, j - 1 - k);
/* 1236 */         paramGraphics.fillPolygon(localPolygon);
/*      */ 
/* 1238 */         paramGraphics.setColor(this.highlightColor);
/* 1239 */         paramGraphics.drawLine(0, 0, i - 2, 0);
/* 1240 */         paramGraphics.drawLine(0, 1, 0, j - 1 - k);
/* 1241 */         paramGraphics.drawLine(0, j - k, k - 1, j - 1);
/*      */ 
/* 1243 */         paramGraphics.setColor(Color.black);
/* 1244 */         paramGraphics.drawLine(i - 1, 0, i - 1, j - 2 - k);
/* 1245 */         paramGraphics.drawLine(i - 1, j - 1 - k, i - 1 - k, j - 1);
/*      */ 
/* 1247 */         paramGraphics.setColor(this.shadowColor);
/* 1248 */         paramGraphics.drawLine(i - 2, 1, i - 2, j - 2 - k);
/* 1249 */         paramGraphics.drawLine(i - 2, j - 1 - k, i - 1 - k, j - 2);
/*      */       }
/*      */       else {
/* 1252 */         k = j / 2;
/* 1253 */         if (BasicGraphicsUtils.isLeftToRight(this.slider)) {
/* 1254 */           paramGraphics.fillRect(1, 1, i - 1 - k, j - 3);
/* 1255 */           localPolygon = new Polygon();
/* 1256 */           localPolygon.addPoint(i - k - 1, 0);
/* 1257 */           localPolygon.addPoint(i - 1, k);
/* 1258 */           localPolygon.addPoint(i - 1 - k, j - 2);
/* 1259 */           paramGraphics.fillPolygon(localPolygon);
/*      */ 
/* 1261 */           paramGraphics.setColor(this.highlightColor);
/* 1262 */           paramGraphics.drawLine(0, 0, 0, j - 2);
/* 1263 */           paramGraphics.drawLine(1, 0, i - 1 - k, 0);
/* 1264 */           paramGraphics.drawLine(i - k - 1, 0, i - 1, k);
/*      */ 
/* 1266 */           paramGraphics.setColor(Color.black);
/* 1267 */           paramGraphics.drawLine(0, j - 1, i - 2 - k, j - 1);
/* 1268 */           paramGraphics.drawLine(i - 1 - k, j - 1, i - 1, j - 1 - k);
/*      */ 
/* 1270 */           paramGraphics.setColor(this.shadowColor);
/* 1271 */           paramGraphics.drawLine(1, j - 2, i - 2 - k, j - 2);
/* 1272 */           paramGraphics.drawLine(i - 1 - k, j - 2, i - 2, j - k - 1);
/*      */         }
/*      */         else {
/* 1275 */           paramGraphics.fillRect(5, 1, i - 1 - k, j - 3);
/* 1276 */           localPolygon = new Polygon();
/* 1277 */           localPolygon.addPoint(k, 0);
/* 1278 */           localPolygon.addPoint(0, k);
/* 1279 */           localPolygon.addPoint(k, j - 2);
/* 1280 */           paramGraphics.fillPolygon(localPolygon);
/*      */ 
/* 1282 */           paramGraphics.setColor(this.highlightColor);
/* 1283 */           paramGraphics.drawLine(k - 1, 0, i - 2, 0);
/* 1284 */           paramGraphics.drawLine(0, k, k, 0);
/*      */ 
/* 1286 */           paramGraphics.setColor(Color.black);
/* 1287 */           paramGraphics.drawLine(0, j - 1 - k, k, j - 1);
/* 1288 */           paramGraphics.drawLine(k, j - 1, i - 1, j - 1);
/*      */ 
/* 1290 */           paramGraphics.setColor(this.shadowColor);
/* 1291 */           paramGraphics.drawLine(k, j - 2, i - 2, j - 2);
/* 1292 */           paramGraphics.drawLine(i - 1, 1, i - 1, j - 2);
/*      */         }
/*      */       }
/*      */     }
/* 1296 */     paramGraphics.translate(-localRectangle.x, -localRectangle.y);
/*      */   }
/*      */ 
/*      */   public void setThumbLocation(int paramInt1, int paramInt2)
/*      */   {
/* 1303 */     unionRect.setBounds(this.thumbRect);
/*      */ 
/* 1305 */     this.thumbRect.setLocation(paramInt1, paramInt2);
/*      */ 
/* 1307 */     SwingUtilities.computeUnion(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width, this.thumbRect.height, unionRect);
/* 1308 */     this.slider.repaint(unionRect.x, unionRect.y, unionRect.width, unionRect.height);
/*      */   }
/*      */ 
/*      */   public void scrollByBlock(int paramInt) {
/* 1312 */     synchronized (this.slider) {
/* 1313 */       int i = (this.slider.getMaximum() - this.slider.getMinimum()) / 10;
/*      */ 
/* 1315 */       if (i == 0) {
/* 1316 */         i = 1;
/*      */       }
/*      */ 
/* 1319 */       if (this.slider.getSnapToTicks()) {
/* 1320 */         j = getTickSpacing();
/*      */ 
/* 1322 */         if (i < j) {
/* 1323 */           i = j;
/*      */         }
/*      */       }
/*      */ 
/* 1327 */       int j = i * (paramInt > 0 ? 1 : -1);
/* 1328 */       this.slider.setValue(this.slider.getValue() + j);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void scrollByUnit(int paramInt) {
/* 1333 */     synchronized (this.slider) {
/* 1334 */       int i = paramInt > 0 ? 1 : -1;
/*      */ 
/* 1336 */       if (this.slider.getSnapToTicks()) {
/* 1337 */         i *= getTickSpacing();
/*      */       }
/*      */ 
/* 1340 */       this.slider.setValue(this.slider.getValue() + i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void scrollDueToClickInTrack(int paramInt)
/*      */   {
/* 1350 */     scrollByBlock(paramInt);
/*      */   }
/*      */ 
/*      */   protected int xPositionForValue(int paramInt) {
/* 1354 */     int i = this.slider.getMinimum();
/* 1355 */     int j = this.slider.getMaximum();
/* 1356 */     int k = this.trackRect.width;
/* 1357 */     double d1 = j - i;
/* 1358 */     double d2 = k / d1;
/* 1359 */     int m = this.trackRect.x;
/* 1360 */     int n = this.trackRect.x + (this.trackRect.width - 1);
/*      */ 
/* 1363 */     if (!drawInverted()) {
/* 1364 */       i1 = m;
/* 1365 */       i1 = (int)(i1 + Math.round(d2 * (paramInt - i)));
/*      */     }
/*      */     else {
/* 1368 */       i1 = n;
/* 1369 */       i1 = (int)(i1 - Math.round(d2 * (paramInt - i)));
/*      */     }
/*      */ 
/* 1372 */     int i1 = Math.max(m, i1);
/* 1373 */     i1 = Math.min(n, i1);
/*      */ 
/* 1375 */     return i1;
/*      */   }
/*      */ 
/*      */   protected int yPositionForValue(int paramInt) {
/* 1379 */     return yPositionForValue(paramInt, this.trackRect.y, this.trackRect.height);
/*      */   }
/*      */ 
/*      */   protected int yPositionForValue(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1393 */     int i = this.slider.getMinimum();
/* 1394 */     int j = this.slider.getMaximum();
/* 1395 */     double d1 = j - i;
/* 1396 */     double d2 = paramInt3 / d1;
/* 1397 */     int k = paramInt2 + (paramInt3 - 1);
/*      */ 
/* 1400 */     if (!drawInverted()) {
/* 1401 */       m = paramInt2;
/* 1402 */       m = (int)(m + Math.round(d2 * (j - paramInt1)));
/*      */     }
/*      */     else {
/* 1405 */       m = paramInt2;
/* 1406 */       m = (int)(m + Math.round(d2 * (paramInt1 - i)));
/*      */     }
/*      */ 
/* 1409 */     int m = Math.max(paramInt2, m);
/* 1410 */     m = Math.min(k, m);
/*      */ 
/* 1412 */     return m;
/*      */   }
/*      */ 
/*      */   public int valueForYPosition(int paramInt)
/*      */   {
/* 1423 */     int j = this.slider.getMinimum();
/* 1424 */     int k = this.slider.getMaximum();
/* 1425 */     int m = this.trackRect.height;
/* 1426 */     int n = this.trackRect.y;
/* 1427 */     int i1 = this.trackRect.y + (this.trackRect.height - 1);
/*      */     int i;
/* 1429 */     if (paramInt <= n) {
/* 1430 */       i = drawInverted() ? j : k;
/*      */     }
/* 1432 */     else if (paramInt >= i1) {
/* 1433 */       i = drawInverted() ? k : j;
/*      */     }
/*      */     else {
/* 1436 */       int i2 = paramInt - n;
/* 1437 */       double d1 = k - j;
/* 1438 */       double d2 = d1 / m;
/* 1439 */       int i3 = (int)Math.round(i2 * d2);
/*      */ 
/* 1441 */       i = drawInverted() ? j + i3 : k - i3;
/*      */     }
/*      */ 
/* 1444 */     return i;
/*      */   }
/*      */ 
/*      */   public int valueForXPosition(int paramInt)
/*      */   {
/* 1455 */     int j = this.slider.getMinimum();
/* 1456 */     int k = this.slider.getMaximum();
/* 1457 */     int m = this.trackRect.width;
/* 1458 */     int n = this.trackRect.x;
/* 1459 */     int i1 = this.trackRect.x + (this.trackRect.width - 1);
/*      */     int i;
/* 1461 */     if (paramInt <= n) {
/* 1462 */       i = drawInverted() ? k : j;
/*      */     }
/* 1464 */     else if (paramInt >= i1) {
/* 1465 */       i = drawInverted() ? j : k;
/*      */     }
/*      */     else {
/* 1468 */       int i2 = paramInt - n;
/* 1469 */       double d1 = k - j;
/* 1470 */       double d2 = d1 / m;
/* 1471 */       int i3 = (int)Math.round(i2 * d2);
/*      */ 
/* 1473 */       i = drawInverted() ? k - i3 : j + i3;
/*      */     }
/*      */ 
/* 1477 */     return i;
/*      */   }
/*      */ 
/*      */   public class ActionScroller extends AbstractAction
/*      */   {
/*      */     int dir;
/*      */     boolean block;
/*      */     JSlider slider;
/*      */ 
/*      */     public ActionScroller(JSlider paramInt, int paramBoolean, boolean arg4)
/*      */     {
/* 1907 */       this.dir = paramBoolean;
/*      */       boolean bool;
/* 1908 */       this.block = bool;
/* 1909 */       this.slider = paramInt;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1913 */       BasicSliderUI.Actions.access$600(BasicSliderUI.SHARED_ACTION, this.slider, BasicSliderUI.this, this.dir, this.block);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 1917 */       boolean bool = true;
/* 1918 */       if (this.slider != null) {
/* 1919 */         bool = this.slider.isEnabled();
/*      */       }
/* 1921 */       return bool;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     public static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
/*      */     public static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
/*      */     public static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
/*      */     public static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
/*      */     public static final String MIN_SCROLL_INCREMENT = "minScroll";
/*      */     public static final String MAX_SCROLL_INCREMENT = "maxScroll";
/*      */ 
/*      */     Actions()
/*      */     {
/* 1968 */       super();
/*      */     }
/*      */ 
/*      */     public Actions(String paramString) {
/* 1972 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1976 */       JSlider localJSlider = (JSlider)paramActionEvent.getSource();
/* 1977 */       BasicSliderUI localBasicSliderUI = (BasicSliderUI)BasicLookAndFeel.getUIOfType(localJSlider.getUI(), BasicSliderUI.class);
/*      */ 
/* 1979 */       String str = getName();
/*      */ 
/* 1981 */       if (localBasicSliderUI == null) {
/* 1982 */         return;
/*      */       }
/* 1984 */       if ("positiveUnitIncrement" == str)
/* 1985 */         scroll(localJSlider, localBasicSliderUI, 1, false);
/* 1986 */       else if ("negativeUnitIncrement" == str)
/* 1987 */         scroll(localJSlider, localBasicSliderUI, -1, false);
/* 1988 */       else if ("positiveBlockIncrement" == str)
/* 1989 */         scroll(localJSlider, localBasicSliderUI, 1, true);
/* 1990 */       else if ("negativeBlockIncrement" == str)
/* 1991 */         scroll(localJSlider, localBasicSliderUI, -1, true);
/* 1992 */       else if ("minScroll" == str)
/* 1993 */         scroll(localJSlider, localBasicSliderUI, -2, false);
/* 1994 */       else if ("maxScroll" == str)
/* 1995 */         scroll(localJSlider, localBasicSliderUI, 2, false);
/*      */     }
/*      */ 
/*      */     private void scroll(JSlider paramJSlider, BasicSliderUI paramBasicSliderUI, int paramInt, boolean paramBoolean)
/*      */     {
/* 2001 */       boolean bool = paramJSlider.getInverted();
/*      */ 
/* 2003 */       if ((paramInt == -1) || (paramInt == 1)) {
/* 2004 */         if (bool) {
/* 2005 */           paramInt = paramInt == 1 ? -1 : 1;
/*      */         }
/*      */ 
/* 2009 */         if (paramBoolean)
/* 2010 */           paramBasicSliderUI.scrollByBlock(paramInt);
/*      */         else
/* 2012 */           paramBasicSliderUI.scrollByUnit(paramInt);
/*      */       }
/*      */       else {
/* 2015 */         if (bool) {
/* 2016 */           paramInt = paramInt == -2 ? 2 : -2;
/*      */         }
/*      */ 
/* 2020 */         paramJSlider.setValue(paramInt == -2 ? paramJSlider.getMinimum() : paramJSlider.getMaximum());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ChangeHandler
/*      */     implements ChangeListener
/*      */   {
/*      */     public ChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1553 */       BasicSliderUI.this.getHandler().stateChanged(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ComponentHandler extends ComponentAdapter
/*      */   {
/*      */     public ComponentHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentResized(ComponentEvent paramComponentEvent)
/*      */     {
/* 1860 */       BasicSliderUI.this.getHandler().componentResized(paramComponentEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FocusHandler
/*      */     implements FocusListener
/*      */   {
/*      */     public FocusHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 1876 */       BasicSliderUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 1880 */       BasicSliderUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements ChangeListener, ComponentListener, FocusListener, PropertyChangeListener
/*      */   {
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1485 */       if (!BasicSliderUI.this.isDragging) {
/* 1486 */         BasicSliderUI.this.calculateThumbLocation();
/* 1487 */         BasicSliderUI.this.slider.repaint();
/*      */       }
/* 1489 */       BasicSliderUI.this.lastValue = BasicSliderUI.this.slider.getValue();
/*      */     }
/*      */     public void componentHidden(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */     public void componentMoved(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */     public void componentResized(ComponentEvent paramComponentEvent) {
/* 1496 */       BasicSliderUI.this.calculateGeometry();
/* 1497 */       BasicSliderUI.this.slider.repaint();
/*      */     }
/*      */     public void componentShown(ComponentEvent paramComponentEvent) {
/*      */     }
/*      */     public void focusGained(FocusEvent paramFocusEvent) {
/* 1502 */       BasicSliderUI.this.slider.repaint(); } 
/* 1503 */     public void focusLost(FocusEvent paramFocusEvent) { BasicSliderUI.this.slider.repaint(); }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1507 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1508 */       if ((str == "orientation") || (str == "inverted") || (str == "labelTable") || (str == "majorTickSpacing") || (str == "minorTickSpacing") || (str == "paintTicks") || (str == "paintTrack") || (str == "font") || (str == "paintLabels") || (str == "Slider.paintThumbArrowShape"))
/*      */       {
/* 1518 */         BasicSliderUI.this.checkedLabelBaselines = false;
/* 1519 */         BasicSliderUI.this.calculateGeometry();
/* 1520 */         BasicSliderUI.this.slider.repaint();
/* 1521 */       } else if (str == "componentOrientation") {
/* 1522 */         BasicSliderUI.this.calculateGeometry();
/* 1523 */         BasicSliderUI.this.slider.repaint();
/* 1524 */         InputMap localInputMap = BasicSliderUI.this.getInputMap(0, BasicSliderUI.this.slider);
/* 1525 */         SwingUtilities.replaceUIInputMap(BasicSliderUI.this.slider, 0, localInputMap);
/*      */       }
/* 1527 */       else if (str == "model") {
/* 1528 */         ((BoundedRangeModel)paramPropertyChangeEvent.getOldValue()).removeChangeListener(BasicSliderUI.this.changeListener);
/*      */ 
/* 1530 */         ((BoundedRangeModel)paramPropertyChangeEvent.getNewValue()).addChangeListener(BasicSliderUI.this.changeListener);
/*      */ 
/* 1532 */         BasicSliderUI.this.calculateThumbLocation();
/* 1533 */         BasicSliderUI.this.slider.repaint();
/*      */       }
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
/*  751 */       BasicSliderUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ScrollListener
/*      */     implements ActionListener
/*      */   {
/* 1814 */     int direction = 1;
/*      */     boolean useBlockIncrement;
/*      */ 
/*      */     public ScrollListener()
/*      */     {
/* 1818 */       this.direction = 1;
/* 1819 */       this.useBlockIncrement = true;
/*      */     }
/*      */ 
/*      */     public ScrollListener(int paramBoolean, boolean arg3) {
/* 1823 */       this.direction = paramBoolean;
/*      */       boolean bool;
/* 1824 */       this.useBlockIncrement = bool;
/*      */     }
/*      */ 
/*      */     public void setDirection(int paramInt) {
/* 1828 */       this.direction = paramInt;
/*      */     }
/*      */ 
/*      */     public void setScrollByBlock(boolean paramBoolean) {
/* 1832 */       this.useBlockIncrement = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1836 */       if (this.useBlockIncrement) {
/* 1837 */         BasicSliderUI.this.scrollByBlock(this.direction);
/*      */       }
/*      */       else {
/* 1840 */         BasicSliderUI.this.scrollByUnit(this.direction);
/*      */       }
/* 1842 */       if (!BasicSliderUI.this.trackListener.shouldScroll(this.direction))
/* 1843 */         ((Timer)paramActionEvent.getSource()).stop();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SharedActionScroller extends AbstractAction
/*      */   {
/*      */     int dir;
/*      */     boolean block;
/*      */ 
/*      */     public SharedActionScroller(int paramInt, boolean paramBoolean)
/*      */     {
/* 1939 */       this.dir = paramInt;
/* 1940 */       this.block = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1944 */       JSlider localJSlider = (JSlider)paramActionEvent.getSource();
/* 1945 */       BasicSliderUI localBasicSliderUI = (BasicSliderUI)BasicLookAndFeel.getUIOfType(localJSlider.getUI(), BasicSliderUI.class);
/*      */ 
/* 1947 */       if (localBasicSliderUI == null) {
/* 1948 */         return;
/*      */       }
/* 1950 */       BasicSliderUI.SHARED_ACTION.scroll(localJSlider, localBasicSliderUI, this.dir, this.block);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TrackListener extends MouseInputAdapter
/*      */   {
/*      */     protected transient int offset;
/*      */     protected transient int currentMouseX;
/*      */     protected transient int currentMouseY;
/*      */ 
/*      */     public TrackListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 1571 */       if (!BasicSliderUI.this.slider.isEnabled()) {
/* 1572 */         return;
/*      */       }
/*      */ 
/* 1575 */       this.offset = 0;
/* 1576 */       BasicSliderUI.this.scrollTimer.stop();
/*      */ 
/* 1578 */       BasicSliderUI.this.isDragging = false;
/* 1579 */       BasicSliderUI.this.slider.setValueIsAdjusting(false);
/* 1580 */       BasicSliderUI.this.slider.repaint();
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1591 */       if (!BasicSliderUI.this.slider.isEnabled()) {
/* 1592 */         return;
/*      */       }
/*      */ 
/* 1599 */       BasicSliderUI.this.calculateGeometry();
/*      */ 
/* 1601 */       this.currentMouseX = paramMouseEvent.getX();
/* 1602 */       this.currentMouseY = paramMouseEvent.getY();
/*      */ 
/* 1604 */       if (BasicSliderUI.this.slider.isRequestFocusEnabled()) {
/* 1605 */         BasicSliderUI.this.slider.requestFocus();
/*      */       }
/*      */ 
/* 1609 */       if (BasicSliderUI.this.thumbRect.contains(this.currentMouseX, this.currentMouseY)) {
/* 1610 */         if ((UIManager.getBoolean("Slider.onlyLeftMouseButtonDrag")) && (!SwingUtilities.isLeftMouseButton(paramMouseEvent)))
/*      */         {
/* 1612 */           return;
/*      */         }
/*      */ 
/* 1615 */         switch (BasicSliderUI.this.slider.getOrientation()) {
/*      */         case 1:
/* 1617 */           this.offset = (this.currentMouseY - BasicSliderUI.this.thumbRect.y);
/* 1618 */           break;
/*      */         case 0:
/* 1620 */           this.offset = (this.currentMouseX - BasicSliderUI.this.thumbRect.x);
/*      */         }
/*      */ 
/* 1623 */         BasicSliderUI.this.isDragging = true;
/* 1624 */         return;
/*      */       }
/*      */ 
/* 1627 */       if (!SwingUtilities.isLeftMouseButton(paramMouseEvent)) {
/* 1628 */         return;
/*      */       }
/*      */ 
/* 1631 */       BasicSliderUI.this.isDragging = false;
/* 1632 */       BasicSliderUI.this.slider.setValueIsAdjusting(true);
/*      */ 
/* 1634 */       Dimension localDimension = BasicSliderUI.this.slider.getSize();
/* 1635 */       int i = 1;
/*      */       int j;
/* 1637 */       switch (BasicSliderUI.this.slider.getOrientation()) {
/*      */       case 1:
/* 1639 */         if (BasicSliderUI.this.thumbRect.isEmpty()) {
/* 1640 */           j = localDimension.height / 2;
/* 1641 */           if (!BasicSliderUI.this.drawInverted()) {
/* 1642 */             i = this.currentMouseY < j ? 1 : -1;
/*      */           }
/*      */           else
/*      */           {
/* 1646 */             i = this.currentMouseY < j ? -1 : 1;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1651 */           j = BasicSliderUI.this.thumbRect.y;
/* 1652 */           if (!BasicSliderUI.this.drawInverted()) {
/* 1653 */             i = this.currentMouseY < j ? 1 : -1;
/*      */           }
/*      */           else
/*      */           {
/* 1657 */             i = this.currentMouseY < j ? -1 : 1;
/*      */           }
/*      */         }
/*      */ 
/* 1661 */         break;
/*      */       case 0:
/* 1663 */         if (BasicSliderUI.this.thumbRect.isEmpty()) {
/* 1664 */           j = localDimension.width / 2;
/* 1665 */           if (!BasicSliderUI.this.drawInverted()) {
/* 1666 */             i = this.currentMouseX < j ? -1 : 1;
/*      */           }
/*      */           else
/*      */           {
/* 1670 */             i = this.currentMouseX < j ? 1 : -1;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1675 */           j = BasicSliderUI.this.thumbRect.x;
/* 1676 */           if (!BasicSliderUI.this.drawInverted()) {
/* 1677 */             i = this.currentMouseX < j ? -1 : 1;
/*      */           }
/*      */           else
/*      */           {
/* 1681 */             i = this.currentMouseX < j ? 1 : -1;
/*      */           }
/*      */         }
/*      */ 
/*      */         break;
/*      */       }
/*      */ 
/* 1688 */       if (shouldScroll(i)) {
/* 1689 */         BasicSliderUI.this.scrollDueToClickInTrack(i);
/*      */       }
/* 1691 */       if (shouldScroll(i)) {
/* 1692 */         BasicSliderUI.this.scrollTimer.stop();
/* 1693 */         BasicSliderUI.this.scrollListener.setDirection(i);
/* 1694 */         BasicSliderUI.this.scrollTimer.start();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean shouldScroll(int paramInt) {
/* 1699 */       Rectangle localRectangle = BasicSliderUI.this.thumbRect;
/* 1700 */       if (BasicSliderUI.this.slider.getOrientation() == 1) {
/* 1701 */         if (BasicSliderUI.this.drawInverted() ? paramInt < 0 : paramInt > 0) {
/* 1702 */           if (localRectangle.y <= this.currentMouseY) {
/* 1703 */             return false;
/*      */           }
/*      */         }
/* 1706 */         else if (localRectangle.y + localRectangle.height >= this.currentMouseY) {
/* 1707 */           return false;
/*      */         }
/*      */ 
/*      */       }
/* 1711 */       else if (BasicSliderUI.this.drawInverted() ? paramInt < 0 : paramInt > 0) {
/* 1712 */         if (localRectangle.x + localRectangle.width >= this.currentMouseX) {
/* 1713 */           return false;
/*      */         }
/*      */       }
/* 1716 */       else if (localRectangle.x <= this.currentMouseX) {
/* 1717 */         return false;
/*      */       }
/*      */ 
/* 1721 */       if ((paramInt > 0) && (BasicSliderUI.this.slider.getValue() + BasicSliderUI.this.slider.getExtent() >= BasicSliderUI.this.slider.getMaximum()))
/*      */       {
/* 1723 */         return false;
/*      */       }
/* 1725 */       if ((paramInt < 0) && (BasicSliderUI.this.slider.getValue() <= BasicSliderUI.this.slider.getMinimum()))
/*      */       {
/* 1727 */         return false;
/*      */       }
/*      */ 
/* 1730 */       return true;
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/* 1740 */       if (!BasicSliderUI.this.slider.isEnabled()) {
/* 1741 */         return;
/*      */       }
/*      */ 
/* 1744 */       this.currentMouseX = paramMouseEvent.getX();
/* 1745 */       this.currentMouseY = paramMouseEvent.getY();
/*      */ 
/* 1747 */       if (!BasicSliderUI.this.isDragging) {
/* 1748 */         return;
/*      */       }
/*      */ 
/* 1751 */       BasicSliderUI.this.slider.setValueIsAdjusting(true);
/*      */       int i;
/* 1753 */       switch (BasicSliderUI.this.slider.getOrientation()) {
/*      */       case 1:
/* 1755 */         int j = BasicSliderUI.this.thumbRect.height / 2;
/* 1756 */         int k = paramMouseEvent.getY() - this.offset;
/* 1757 */         int m = BasicSliderUI.this.trackRect.y;
/* 1758 */         int n = BasicSliderUI.this.trackRect.y + (BasicSliderUI.this.trackRect.height - 1);
/* 1759 */         int i1 = BasicSliderUI.this.yPositionForValue(BasicSliderUI.this.slider.getMaximum() - BasicSliderUI.this.slider.getExtent());
/*      */ 
/* 1762 */         if (BasicSliderUI.this.drawInverted()) {
/* 1763 */           n = i1;
/*      */         }
/*      */         else {
/* 1766 */           m = i1;
/*      */         }
/* 1768 */         k = Math.max(k, m - j);
/* 1769 */         k = Math.min(k, n - j);
/*      */ 
/* 1771 */         BasicSliderUI.this.setThumbLocation(BasicSliderUI.this.thumbRect.x, k);
/*      */ 
/* 1773 */         i = k + j;
/* 1774 */         BasicSliderUI.this.slider.setValue(BasicSliderUI.this.valueForYPosition(i));
/* 1775 */         break;
/*      */       case 0:
/* 1777 */         int i2 = BasicSliderUI.this.thumbRect.width / 2;
/* 1778 */         int i3 = paramMouseEvent.getX() - this.offset;
/* 1779 */         int i4 = BasicSliderUI.this.trackRect.x;
/* 1780 */         int i5 = BasicSliderUI.this.trackRect.x + (BasicSliderUI.this.trackRect.width - 1);
/* 1781 */         int i6 = BasicSliderUI.this.xPositionForValue(BasicSliderUI.this.slider.getMaximum() - BasicSliderUI.this.slider.getExtent());
/*      */ 
/* 1784 */         if (BasicSliderUI.this.drawInverted()) {
/* 1785 */           i4 = i6;
/*      */         }
/*      */         else {
/* 1788 */           i5 = i6;
/*      */         }
/* 1790 */         i3 = Math.max(i3, i4 - i2);
/* 1791 */         i3 = Math.min(i3, i5 - i2);
/*      */ 
/* 1793 */         BasicSliderUI.this.setThumbLocation(i3, BasicSliderUI.this.thumbRect.y);
/*      */ 
/* 1795 */         i = i3 + i2;
/* 1796 */         BasicSliderUI.this.slider.setValue(BasicSliderUI.this.valueForXPosition(i));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicSliderUI
 * JD-Core Version:    0.6.2
 */