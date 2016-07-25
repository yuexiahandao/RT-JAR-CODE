/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.BoundedRangeModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JScrollBar;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ScrollBarUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicScrollBarUI extends ScrollBarUI
/*      */   implements LayoutManager, SwingConstants
/*      */ {
/*      */   private static final int POSITIVE_SCROLL = 1;
/*      */   private static final int NEGATIVE_SCROLL = -1;
/*      */   private static final int MIN_SCROLL = 2;
/*      */   private static final int MAX_SCROLL = 3;
/*      */   protected Dimension minimumThumbSize;
/*      */   protected Dimension maximumThumbSize;
/*      */   protected Color thumbHighlightColor;
/*      */   protected Color thumbLightShadowColor;
/*      */   protected Color thumbDarkShadowColor;
/*      */   protected Color thumbColor;
/*      */   protected Color trackColor;
/*      */   protected Color trackHighlightColor;
/*      */   protected JScrollBar scrollbar;
/*      */   protected JButton incrButton;
/*      */   protected JButton decrButton;
/*      */   protected boolean isDragging;
/*      */   protected TrackListener trackListener;
/*      */   protected ArrowButtonListener buttonListener;
/*      */   protected ModelListener modelListener;
/*      */   protected Rectangle thumbRect;
/*      */   protected Rectangle trackRect;
/*      */   protected int trackHighlight;
/*      */   protected static final int NO_HIGHLIGHT = 0;
/*      */   protected static final int DECREASE_HIGHLIGHT = 1;
/*      */   protected static final int INCREASE_HIGHLIGHT = 2;
/*      */   protected ScrollListener scrollListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   protected Timer scrollTimer;
/*      */   private static final int scrollSpeedThrottle = 60;
/*      */   private boolean supportsAbsolutePositioning;
/*      */   protected int scrollBarWidth;
/*      */   private Handler handler;
/*      */   private boolean thumbActive;
/*      */   private boolean useCachedValue;
/*      */   private int scrollBarValue;
/*      */   protected int incrGap;
/*      */   protected int decrGap;
/*      */ 
/*      */   public BasicScrollBarUI()
/*      */   {
/*  112 */     this.useCachedValue = false;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  137 */     paramLazyActionMap.put(new Actions("positiveUnitIncrement"));
/*  138 */     paramLazyActionMap.put(new Actions("positiveBlockIncrement"));
/*  139 */     paramLazyActionMap.put(new Actions("negativeUnitIncrement"));
/*  140 */     paramLazyActionMap.put(new Actions("negativeBlockIncrement"));
/*  141 */     paramLazyActionMap.put(new Actions("minScroll"));
/*  142 */     paramLazyActionMap.put(new Actions("maxScroll"));
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  147 */     return new BasicScrollBarUI();
/*      */   }
/*      */ 
/*      */   protected void configureScrollBarColors()
/*      */   {
/*  153 */     LookAndFeel.installColors(this.scrollbar, "ScrollBar.background", "ScrollBar.foreground");
/*      */ 
/*  155 */     this.thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
/*  156 */     this.thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
/*  157 */     this.thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
/*  158 */     this.thumbColor = UIManager.getColor("ScrollBar.thumb");
/*  159 */     this.trackColor = UIManager.getColor("ScrollBar.track");
/*  160 */     this.trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  165 */     this.scrollbar = ((JScrollBar)paramJComponent);
/*  166 */     this.thumbRect = new Rectangle(0, 0, 0, 0);
/*  167 */     this.trackRect = new Rectangle(0, 0, 0, 0);
/*  168 */     installDefaults();
/*  169 */     installComponents();
/*  170 */     installListeners();
/*  171 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent) {
/*  175 */     this.scrollbar = ((JScrollBar)paramJComponent);
/*  176 */     uninstallListeners();
/*  177 */     uninstallDefaults();
/*  178 */     uninstallComponents();
/*  179 */     uninstallKeyboardActions();
/*  180 */     this.thumbRect = null;
/*  181 */     this.scrollbar = null;
/*  182 */     this.incrButton = null;
/*  183 */     this.decrButton = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  189 */     this.scrollBarWidth = UIManager.getInt("ScrollBar.width");
/*  190 */     if (this.scrollBarWidth <= 0) {
/*  191 */       this.scrollBarWidth = 16;
/*      */     }
/*  193 */     this.minimumThumbSize = ((Dimension)UIManager.get("ScrollBar.minimumThumbSize"));
/*  194 */     this.maximumThumbSize = ((Dimension)UIManager.get("ScrollBar.maximumThumbSize"));
/*      */ 
/*  196 */     Boolean localBoolean = (Boolean)UIManager.get("ScrollBar.allowsAbsolutePositioning");
/*  197 */     this.supportsAbsolutePositioning = (localBoolean != null ? localBoolean.booleanValue() : false);
/*      */ 
/*  200 */     this.trackHighlight = 0;
/*  201 */     if ((this.scrollbar.getLayout() == null) || ((this.scrollbar.getLayout() instanceof UIResource)))
/*      */     {
/*  203 */       this.scrollbar.setLayout(this);
/*      */     }
/*  205 */     configureScrollBarColors();
/*  206 */     LookAndFeel.installBorder(this.scrollbar, "ScrollBar.border");
/*  207 */     LookAndFeel.installProperty(this.scrollbar, "opaque", Boolean.TRUE);
/*      */ 
/*  209 */     this.scrollBarValue = this.scrollbar.getValue();
/*      */ 
/*  211 */     this.incrGap = UIManager.getInt("ScrollBar.incrementButtonGap");
/*  212 */     this.decrGap = UIManager.getInt("ScrollBar.decrementButtonGap");
/*      */ 
/*  218 */     String str = (String)this.scrollbar.getClientProperty("JComponent.sizeVariant");
/*      */ 
/*  220 */     if (str != null)
/*  221 */       if ("large".equals(str)) {
/*  222 */         this.scrollBarWidth = ((int)(this.scrollBarWidth * 1.15D));
/*  223 */         this.incrGap = ((int)(this.incrGap * 1.15D));
/*  224 */         this.decrGap = ((int)(this.decrGap * 1.15D));
/*  225 */       } else if ("small".equals(str)) {
/*  226 */         this.scrollBarWidth = ((int)(this.scrollBarWidth * 0.857D));
/*  227 */         this.incrGap = ((int)(this.incrGap * 0.857D));
/*  228 */         this.decrGap = ((int)(this.decrGap * 0.714D));
/*  229 */       } else if ("mini".equals(str)) {
/*  230 */         this.scrollBarWidth = ((int)(this.scrollBarWidth * 0.714D));
/*  231 */         this.incrGap = ((int)(this.incrGap * 0.714D));
/*  232 */         this.decrGap = ((int)(this.decrGap * 0.714D));
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*  239 */     switch (this.scrollbar.getOrientation()) {
/*      */     case 1:
/*  241 */       this.incrButton = createIncreaseButton(5);
/*  242 */       this.decrButton = createDecreaseButton(1);
/*  243 */       break;
/*      */     case 0:
/*  246 */       if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
/*  247 */         this.incrButton = createIncreaseButton(3);
/*  248 */         this.decrButton = createDecreaseButton(7);
/*      */       } else {
/*  250 */         this.incrButton = createIncreaseButton(7);
/*  251 */         this.decrButton = createDecreaseButton(3);
/*      */       }
/*      */       break;
/*      */     }
/*  255 */     this.scrollbar.add(this.incrButton);
/*  256 */     this.scrollbar.add(this.decrButton);
/*      */ 
/*  258 */     this.scrollbar.setEnabled(this.scrollbar.isEnabled());
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents() {
/*  262 */     this.scrollbar.remove(this.incrButton);
/*  263 */     this.scrollbar.remove(this.decrButton);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  268 */     this.trackListener = createTrackListener();
/*  269 */     this.buttonListener = createArrowButtonListener();
/*  270 */     this.modelListener = createModelListener();
/*  271 */     this.propertyChangeListener = createPropertyChangeListener();
/*      */ 
/*  273 */     this.scrollbar.addMouseListener(this.trackListener);
/*  274 */     this.scrollbar.addMouseMotionListener(this.trackListener);
/*  275 */     this.scrollbar.getModel().addChangeListener(this.modelListener);
/*  276 */     this.scrollbar.addPropertyChangeListener(this.propertyChangeListener);
/*  277 */     this.scrollbar.addFocusListener(getHandler());
/*      */ 
/*  279 */     if (this.incrButton != null) {
/*  280 */       this.incrButton.addMouseListener(this.buttonListener);
/*      */     }
/*  282 */     if (this.decrButton != null) {
/*  283 */       this.decrButton.addMouseListener(this.buttonListener);
/*      */     }
/*      */ 
/*  286 */     this.scrollListener = createScrollListener();
/*  287 */     this.scrollTimer = new Timer(60, this.scrollListener);
/*  288 */     this.scrollTimer.setInitialDelay(300);
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  293 */     LazyActionMap.installLazyActionMap(this.scrollbar, BasicScrollBarUI.class, "ScrollBar.actionMap");
/*      */ 
/*  296 */     InputMap localInputMap = getInputMap(0);
/*  297 */     SwingUtilities.replaceUIInputMap(this.scrollbar, 0, localInputMap);
/*      */ 
/*  299 */     localInputMap = getInputMap(1);
/*  300 */     SwingUtilities.replaceUIInputMap(this.scrollbar, 1, localInputMap);
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  305 */     SwingUtilities.replaceUIInputMap(this.scrollbar, 0, null);
/*      */ 
/*  307 */     SwingUtilities.replaceUIActionMap(this.scrollbar, null);
/*      */   }
/*      */ 
/*      */   private InputMap getInputMap(int paramInt)
/*      */   {
/*      */     InputMap localInputMap1;
/*      */     InputMap localInputMap2;
/*  311 */     if (paramInt == 0) {
/*  312 */       localInputMap1 = (InputMap)DefaultLookup.get(this.scrollbar, this, "ScrollBar.focusInputMap");
/*      */ 
/*  316 */       if ((this.scrollbar.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.scrollbar, this, "ScrollBar.focusInputMap.RightToLeft")) == null))
/*      */       {
/*  318 */         return localInputMap1;
/*      */       }
/*  320 */       localInputMap2.setParent(localInputMap1);
/*  321 */       return localInputMap2;
/*      */     }
/*      */ 
/*  324 */     if (paramInt == 1) {
/*  325 */       localInputMap1 = (InputMap)DefaultLookup.get(this.scrollbar, this, "ScrollBar.ancestorInputMap");
/*      */ 
/*  329 */       if ((this.scrollbar.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.scrollbar, this, "ScrollBar.ancestorInputMap.RightToLeft")) == null))
/*      */       {
/*  331 */         return localInputMap1;
/*      */       }
/*  333 */       localInputMap2.setParent(localInputMap1);
/*  334 */       return localInputMap2;
/*      */     }
/*      */ 
/*  337 */     return null;
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  342 */     this.scrollTimer.stop();
/*  343 */     this.scrollTimer = null;
/*      */ 
/*  345 */     if (this.decrButton != null) {
/*  346 */       this.decrButton.removeMouseListener(this.buttonListener);
/*      */     }
/*  348 */     if (this.incrButton != null) {
/*  349 */       this.incrButton.removeMouseListener(this.buttonListener);
/*      */     }
/*      */ 
/*  352 */     this.scrollbar.getModel().removeChangeListener(this.modelListener);
/*  353 */     this.scrollbar.removeMouseListener(this.trackListener);
/*  354 */     this.scrollbar.removeMouseMotionListener(this.trackListener);
/*  355 */     this.scrollbar.removePropertyChangeListener(this.propertyChangeListener);
/*  356 */     this.scrollbar.removeFocusListener(getHandler());
/*  357 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  362 */     LookAndFeel.uninstallBorder(this.scrollbar);
/*  363 */     if (this.scrollbar.getLayout() == this)
/*  364 */       this.scrollbar.setLayout(null);
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/*  370 */     if (this.handler == null) {
/*  371 */       this.handler = new Handler(null);
/*      */     }
/*  373 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected TrackListener createTrackListener() {
/*  377 */     return new TrackListener();
/*      */   }
/*      */ 
/*      */   protected ArrowButtonListener createArrowButtonListener() {
/*  381 */     return new ArrowButtonListener();
/*      */   }
/*      */ 
/*      */   protected ModelListener createModelListener() {
/*  385 */     return new ModelListener();
/*      */   }
/*      */ 
/*      */   protected ScrollListener createScrollListener() {
/*  389 */     return new ScrollListener();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  393 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private void updateThumbState(int paramInt1, int paramInt2) {
/*  397 */     Rectangle localRectangle = getThumbBounds();
/*      */ 
/*  399 */     setThumbRollover(localRectangle.contains(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   protected void setThumbRollover(boolean paramBoolean)
/*      */   {
/*  409 */     if (this.thumbActive != paramBoolean) {
/*  410 */       this.thumbActive = paramBoolean;
/*  411 */       this.scrollbar.repaint(getThumbBounds());
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isThumbRollover()
/*      */   {
/*  422 */     return this.thumbActive;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*  426 */     paintTrack(paramGraphics, paramJComponent, getTrackBounds());
/*  427 */     Rectangle localRectangle = getThumbBounds();
/*  428 */     if (localRectangle.intersects(paramGraphics.getClipBounds()))
/*  429 */       paintThumb(paramGraphics, paramJComponent, localRectangle);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  451 */     return this.scrollbar.getOrientation() == 1 ? new Dimension(this.scrollBarWidth, 48) : new Dimension(48, this.scrollBarWidth);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  464 */     return new Dimension(2147483647, 2147483647);
/*      */   }
/*      */ 
/*      */   protected JButton createDecreaseButton(int paramInt) {
/*  468 */     return new BasicArrowButton(paramInt, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
/*      */   }
/*      */ 
/*      */   protected JButton createIncreaseButton(int paramInt)
/*      */   {
/*  476 */     return new BasicArrowButton(paramInt, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
/*      */   }
/*      */ 
/*      */   protected void paintDecreaseHighlight(Graphics paramGraphics)
/*      */   {
/*  486 */     Insets localInsets = this.scrollbar.getInsets();
/*  487 */     Rectangle localRectangle = getThumbBounds();
/*  488 */     paramGraphics.setColor(this.trackHighlightColor);
/*      */     int i;
/*      */     int j;
/*      */     int k;
/*      */     int m;
/*  490 */     if (this.scrollbar.getOrientation() == 1)
/*      */     {
/*  492 */       i = localInsets.left;
/*  493 */       j = this.trackRect.y;
/*  494 */       k = this.scrollbar.getWidth() - (localInsets.left + localInsets.right);
/*  495 */       m = localRectangle.y - j;
/*  496 */       paramGraphics.fillRect(i, j, k, m);
/*      */     }
/*      */     else
/*      */     {
/*  502 */       if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
/*  503 */         i = this.trackRect.x;
/*  504 */         j = localRectangle.x - i;
/*      */       } else {
/*  506 */         i = localRectangle.x + localRectangle.width;
/*  507 */         j = this.trackRect.x + this.trackRect.width - i;
/*      */       }
/*  509 */       k = localInsets.top;
/*  510 */       m = this.scrollbar.getHeight() - (localInsets.top + localInsets.bottom);
/*  511 */       paramGraphics.fillRect(i, k, j, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintIncreaseHighlight(Graphics paramGraphics)
/*      */   {
/*  518 */     Insets localInsets = this.scrollbar.getInsets();
/*  519 */     Rectangle localRectangle = getThumbBounds();
/*  520 */     paramGraphics.setColor(this.trackHighlightColor);
/*      */     int i;
/*      */     int j;
/*      */     int k;
/*      */     int m;
/*  522 */     if (this.scrollbar.getOrientation() == 1)
/*      */     {
/*  524 */       i = localInsets.left;
/*  525 */       j = localRectangle.y + localRectangle.height;
/*  526 */       k = this.scrollbar.getWidth() - (localInsets.left + localInsets.right);
/*  527 */       m = this.trackRect.y + this.trackRect.height - j;
/*  528 */       paramGraphics.fillRect(i, j, k, m);
/*      */     }
/*      */     else
/*      */     {
/*  535 */       if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
/*  536 */         i = localRectangle.x + localRectangle.width;
/*  537 */         j = this.trackRect.x + this.trackRect.width - i;
/*      */       } else {
/*  539 */         i = this.trackRect.x;
/*  540 */         j = localRectangle.x - i;
/*      */       }
/*  542 */       k = localInsets.top;
/*  543 */       m = this.scrollbar.getHeight() - (localInsets.top + localInsets.bottom);
/*  544 */       paramGraphics.fillRect(i, k, j, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintTrack(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*      */   {
/*  551 */     paramGraphics.setColor(this.trackColor);
/*  552 */     paramGraphics.fillRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */ 
/*  554 */     if (this.trackHighlight == 1) {
/*  555 */       paintDecreaseHighlight(paramGraphics);
/*      */     }
/*  557 */     else if (this.trackHighlight == 2)
/*  558 */       paintIncreaseHighlight(paramGraphics);
/*      */   }
/*      */ 
/*      */   protected void paintThumb(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*      */   {
/*  565 */     if ((paramRectangle.isEmpty()) || (!this.scrollbar.isEnabled())) {
/*  566 */       return;
/*      */     }
/*      */ 
/*  569 */     int i = paramRectangle.width;
/*  570 */     int j = paramRectangle.height;
/*      */ 
/*  572 */     paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*      */ 
/*  574 */     paramGraphics.setColor(this.thumbDarkShadowColor);
/*  575 */     paramGraphics.drawRect(0, 0, i - 1, j - 1);
/*  576 */     paramGraphics.setColor(this.thumbColor);
/*  577 */     paramGraphics.fillRect(0, 0, i - 1, j - 1);
/*      */ 
/*  579 */     paramGraphics.setColor(this.thumbHighlightColor);
/*  580 */     paramGraphics.drawLine(1, 1, 1, j - 2);
/*  581 */     paramGraphics.drawLine(2, 1, i - 3, 1);
/*      */ 
/*  583 */     paramGraphics.setColor(this.thumbLightShadowColor);
/*  584 */     paramGraphics.drawLine(2, j - 2, i - 2, j - 2);
/*  585 */     paramGraphics.drawLine(i - 2, 1, i - 2, j - 3);
/*      */ 
/*  587 */     paramGraphics.translate(-paramRectangle.x, -paramRectangle.y);
/*      */   }
/*      */ 
/*      */   protected Dimension getMinimumThumbSize()
/*      */   {
/*  603 */     return this.minimumThumbSize;
/*      */   }
/*      */ 
/*      */   protected Dimension getMaximumThumbSize()
/*      */   {
/*  618 */     return this.maximumThumbSize;
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(String paramString, Component paramComponent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void removeLayoutComponent(Component paramComponent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Dimension preferredLayoutSize(Container paramContainer) {
/*  630 */     return getPreferredSize((JComponent)paramContainer);
/*      */   }
/*      */ 
/*      */   public Dimension minimumLayoutSize(Container paramContainer) {
/*  634 */     return getMinimumSize((JComponent)paramContainer);
/*      */   }
/*      */ 
/*      */   private int getValue(JScrollBar paramJScrollBar) {
/*  638 */     return this.useCachedValue ? this.scrollBarValue : paramJScrollBar.getValue();
/*      */   }
/*      */ 
/*      */   protected void layoutVScrollbar(JScrollBar paramJScrollBar)
/*      */   {
/*  643 */     Dimension localDimension = paramJScrollBar.getSize();
/*  644 */     Insets localInsets = paramJScrollBar.getInsets();
/*      */ 
/*  649 */     int i = localDimension.width - (localInsets.left + localInsets.right);
/*  650 */     int j = localInsets.left;
/*      */ 
/*  655 */     boolean bool = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
/*      */ 
/*  657 */     int k = bool ? i : this.decrButton.getPreferredSize().height;
/*      */ 
/*  659 */     int m = localInsets.top;
/*      */ 
/*  661 */     int n = bool ? i : this.incrButton.getPreferredSize().height;
/*      */ 
/*  663 */     int i1 = localDimension.height - (localInsets.bottom + n);
/*      */ 
/*  669 */     int i2 = localInsets.top + localInsets.bottom;
/*  670 */     int i3 = k + n;
/*  671 */     int i4 = this.decrGap + this.incrGap;
/*  672 */     float f1 = localDimension.height - (i2 + i3) - i4;
/*      */ 
/*  680 */     float f2 = paramJScrollBar.getMinimum();
/*  681 */     float f3 = paramJScrollBar.getVisibleAmount();
/*  682 */     float f4 = paramJScrollBar.getMaximum() - f2;
/*  683 */     float f5 = getValue(paramJScrollBar);
/*      */ 
/*  685 */     int i5 = f4 <= 0.0F ? getMaximumThumbSize().height : (int)(f1 * (f3 / f4));
/*      */ 
/*  687 */     i5 = Math.max(i5, getMinimumThumbSize().height);
/*  688 */     i5 = Math.min(i5, getMaximumThumbSize().height);
/*      */ 
/*  690 */     int i6 = i1 - this.incrGap - i5;
/*  691 */     if (f5 < paramJScrollBar.getMaximum() - paramJScrollBar.getVisibleAmount()) {
/*  692 */       float f6 = f1 - i5;
/*  693 */       i6 = (int)(0.5F + f6 * ((f5 - f2) / (f4 - f3)));
/*  694 */       i6 += m + k + this.decrGap;
/*      */     }
/*      */ 
/*  700 */     int i7 = localDimension.height - i2;
/*  701 */     if (i7 < i3) {
/*  702 */       n = k = i7 / 2;
/*  703 */       i1 = localDimension.height - (localInsets.bottom + n);
/*      */     }
/*  705 */     this.decrButton.setBounds(j, m, i, k);
/*  706 */     this.incrButton.setBounds(j, i1, i, n);
/*      */ 
/*  710 */     int i8 = m + k + this.decrGap;
/*  711 */     int i9 = i1 - this.incrGap - i8;
/*  712 */     this.trackRect.setBounds(j, i8, i, i9);
/*      */ 
/*  718 */     if (i5 >= (int)f1) {
/*  719 */       if (UIManager.getBoolean("ScrollBar.alwaysShowThumb"))
/*      */       {
/*  722 */         setThumbBounds(j, i8, i, i9);
/*      */       }
/*      */       else
/*  725 */         setThumbBounds(0, 0, 0, 0);
/*      */     }
/*      */     else
/*      */     {
/*  729 */       if (i6 + i5 > i1 - this.incrGap) {
/*  730 */         i6 = i1 - this.incrGap - i5;
/*      */       }
/*  732 */       if (i6 < m + k + this.decrGap) {
/*  733 */         i6 = m + k + this.decrGap + 1;
/*      */       }
/*  735 */       setThumbBounds(j, i6, i, i5);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void layoutHScrollbar(JScrollBar paramJScrollBar)
/*      */   {
/*  742 */     Dimension localDimension = paramJScrollBar.getSize();
/*  743 */     Insets localInsets = paramJScrollBar.getInsets();
/*      */ 
/*  747 */     int i = localDimension.height - (localInsets.top + localInsets.bottom);
/*  748 */     int j = localInsets.top;
/*      */ 
/*  750 */     boolean bool1 = paramJScrollBar.getComponentOrientation().isLeftToRight();
/*      */ 
/*  755 */     boolean bool2 = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
/*      */ 
/*  757 */     int k = bool2 ? i : this.decrButton.getPreferredSize().width;
/*      */ 
/*  759 */     int m = bool2 ? i : this.incrButton.getPreferredSize().width;
/*      */ 
/*  761 */     if (!bool1) {
/*  762 */       n = k;
/*  763 */       k = m;
/*  764 */       m = n;
/*      */     }
/*  766 */     int n = localInsets.left;
/*  767 */     int i1 = localDimension.width - (localInsets.right + m);
/*  768 */     int i2 = bool1 ? this.decrGap : this.incrGap;
/*  769 */     int i3 = bool1 ? this.incrGap : this.decrGap;
/*      */ 
/*  775 */     int i4 = localInsets.left + localInsets.right;
/*  776 */     int i5 = k + m;
/*  777 */     float f1 = localDimension.width - (i4 + i5) - (i2 + i3);
/*      */ 
/*  785 */     float f2 = paramJScrollBar.getMinimum();
/*  786 */     float f3 = paramJScrollBar.getMaximum();
/*  787 */     float f4 = paramJScrollBar.getVisibleAmount();
/*  788 */     float f5 = f3 - f2;
/*  789 */     float f6 = getValue(paramJScrollBar);
/*      */ 
/*  791 */     int i6 = f5 <= 0.0F ? getMaximumThumbSize().width : (int)(f1 * (f4 / f5));
/*      */ 
/*  793 */     i6 = Math.max(i6, getMinimumThumbSize().width);
/*  794 */     i6 = Math.min(i6, getMaximumThumbSize().width);
/*      */ 
/*  796 */     int i7 = bool1 ? i1 - i3 - i6 : n + k + i2;
/*  797 */     if (f6 < f3 - paramJScrollBar.getVisibleAmount()) {
/*  798 */       float f7 = f1 - i6;
/*  799 */       if (bool1)
/*  800 */         i7 = (int)(0.5F + f7 * ((f6 - f2) / (f5 - f4)));
/*      */       else {
/*  802 */         i7 = (int)(0.5F + f7 * ((f3 - f4 - f6) / (f5 - f4)));
/*      */       }
/*  804 */       i7 += n + k + i2;
/*      */     }
/*      */ 
/*  810 */     int i8 = localDimension.width - i4;
/*  811 */     if (i8 < i5) {
/*  812 */       m = k = i8 / 2;
/*  813 */       i1 = localDimension.width - (localInsets.right + m + i3);
/*      */     }
/*      */ 
/*  816 */     (bool1 ? this.decrButton : this.incrButton).setBounds(n, j, k, i);
/*  817 */     (bool1 ? this.incrButton : this.decrButton).setBounds(i1, j, m, i);
/*      */ 
/*  821 */     int i9 = n + k + i2;
/*  822 */     int i10 = i1 - i3 - i9;
/*  823 */     this.trackRect.setBounds(i9, j, i10, i);
/*      */ 
/*  828 */     if (i6 >= (int)f1) {
/*  829 */       if (UIManager.getBoolean("ScrollBar.alwaysShowThumb"))
/*      */       {
/*  832 */         setThumbBounds(i9, j, i10, i);
/*      */       }
/*      */       else
/*  835 */         setThumbBounds(0, 0, 0, 0);
/*      */     }
/*      */     else
/*      */     {
/*  839 */       if (i7 + i6 > i1 - i3) {
/*  840 */         i7 = i1 - i3 - i6;
/*      */       }
/*  842 */       if (i7 < n + k + i2) {
/*  843 */         i7 = n + k + i2 + 1;
/*      */       }
/*  845 */       setThumbBounds(i7, j, i6, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void layoutContainer(Container paramContainer)
/*      */   {
/*  855 */     if (this.isDragging) {
/*  856 */       return;
/*      */     }
/*      */ 
/*  859 */     JScrollBar localJScrollBar = (JScrollBar)paramContainer;
/*  860 */     switch (localJScrollBar.getOrientation()) {
/*      */     case 1:
/*  862 */       layoutVScrollbar(localJScrollBar);
/*  863 */       break;
/*      */     case 0:
/*  866 */       layoutHScrollbar(localJScrollBar);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setThumbBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  882 */     if ((this.thumbRect.x == paramInt1) && (this.thumbRect.y == paramInt2) && (this.thumbRect.width == paramInt3) && (this.thumbRect.height == paramInt4))
/*      */     {
/*  886 */       return;
/*      */     }
/*      */ 
/*  892 */     int i = Math.min(paramInt1, this.thumbRect.x);
/*  893 */     int j = Math.min(paramInt2, this.thumbRect.y);
/*  894 */     int k = Math.max(paramInt1 + paramInt3, this.thumbRect.x + this.thumbRect.width);
/*  895 */     int m = Math.max(paramInt2 + paramInt4, this.thumbRect.y + this.thumbRect.height);
/*      */ 
/*  897 */     this.thumbRect.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*  898 */     this.scrollbar.repaint(i, j, k - i, m - j);
/*      */ 
/*  902 */     setThumbRollover(false);
/*      */   }
/*      */ 
/*      */   protected Rectangle getThumbBounds()
/*      */   {
/*  916 */     return this.thumbRect;
/*      */   }
/*      */ 
/*      */   protected Rectangle getTrackBounds()
/*      */   {
/*  933 */     return this.trackRect;
/*      */   }
/*      */ 
/*      */   static void scrollByBlock(JScrollBar paramJScrollBar, int paramInt)
/*      */   {
/*  943 */     int i = paramJScrollBar.getValue();
/*  944 */     int j = paramJScrollBar.getBlockIncrement(paramInt);
/*  945 */     int k = j * (paramInt > 0 ? 1 : -1);
/*  946 */     int m = i + k;
/*      */ 
/*  949 */     if ((k > 0) && (m < i)) {
/*  950 */       m = paramJScrollBar.getMaximum();
/*      */     }
/*  952 */     else if ((k < 0) && (m > i)) {
/*  953 */       m = paramJScrollBar.getMinimum();
/*      */     }
/*      */ 
/*  956 */     paramJScrollBar.setValue(m);
/*      */   }
/*      */ 
/*      */   protected void scrollByBlock(int paramInt)
/*      */   {
/*  961 */     scrollByBlock(this.scrollbar, paramInt);
/*  962 */     this.trackHighlight = (paramInt > 0 ? 2 : 1);
/*  963 */     Rectangle localRectangle = getTrackBounds();
/*  964 */     this.scrollbar.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */   }
/*      */ 
/*      */   static void scrollByUnits(JScrollBar paramJScrollBar, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  980 */     int j = -1;
/*      */ 
/*  982 */     if (paramBoolean) {
/*  983 */       if (paramInt1 < 0) {
/*  984 */         j = paramJScrollBar.getValue() - paramJScrollBar.getBlockIncrement(paramInt1);
/*      */       }
/*      */       else
/*      */       {
/*  988 */         j = paramJScrollBar.getValue() + paramJScrollBar.getBlockIncrement(paramInt1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  993 */     for (int k = 0; k < paramInt2; k++)
/*      */     {
/*      */       int i;
/*  994 */       if (paramInt1 > 0) {
/*  995 */         i = paramJScrollBar.getUnitIncrement(paramInt1);
/*      */       }
/*      */       else {
/*  998 */         i = -paramJScrollBar.getUnitIncrement(paramInt1);
/*      */       }
/*      */ 
/* 1001 */       int m = paramJScrollBar.getValue();
/* 1002 */       int n = m + i;
/*      */ 
/* 1005 */       if ((i > 0) && (n < m)) {
/* 1006 */         n = paramJScrollBar.getMaximum();
/*      */       }
/* 1008 */       else if ((i < 0) && (n > m)) {
/* 1009 */         n = paramJScrollBar.getMinimum();
/*      */       }
/* 1011 */       if (m == n)
/*      */       {
/*      */         break;
/*      */       }
/* 1015 */       if ((paramBoolean) && (k > 0)) {
/* 1016 */         assert (j != -1);
/* 1017 */         if (((paramInt1 < 0) && (n < j)) || ((paramInt1 > 0) && (n > j)))
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/* 1022 */       paramJScrollBar.setValue(n);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void scrollByUnit(int paramInt) {
/* 1027 */     scrollByUnits(this.scrollbar, paramInt, 1, false);
/*      */   }
/*      */ 
/*      */   public boolean getSupportsAbsolutePositioning()
/*      */   {
/* 1038 */     return this.supportsAbsolutePositioning;
/*      */   }
/*      */ 
/*      */   private boolean isMouseLeftOfThumb()
/*      */   {
/* 1446 */     return this.trackListener.currentMouseX < getThumbBounds().x;
/*      */   }
/*      */ 
/*      */   private boolean isMouseRightOfThumb() {
/* 1450 */     Rectangle localRectangle = getThumbBounds();
/* 1451 */     return this.trackListener.currentMouseX > localRectangle.x + localRectangle.width;
/*      */   }
/*      */ 
/*      */   private boolean isMouseBeforeThumb() {
/* 1455 */     return this.scrollbar.getComponentOrientation().isLeftToRight() ? isMouseLeftOfThumb() : isMouseRightOfThumb();
/*      */   }
/*      */ 
/*      */   private boolean isMouseAfterThumb()
/*      */   {
/* 1461 */     return this.scrollbar.getComponentOrientation().isLeftToRight() ? isMouseRightOfThumb() : isMouseLeftOfThumb();
/*      */   }
/*      */ 
/*      */   private void updateButtonDirections()
/*      */   {
/* 1467 */     int i = this.scrollbar.getOrientation();
/* 1468 */     if (this.scrollbar.getComponentOrientation().isLeftToRight()) {
/* 1469 */       if ((this.incrButton instanceof BasicArrowButton)) {
/* 1470 */         ((BasicArrowButton)this.incrButton).setDirection(i == 0 ? 3 : 5);
/*      */       }
/*      */ 
/* 1473 */       if ((this.decrButton instanceof BasicArrowButton)) {
/* 1474 */         ((BasicArrowButton)this.decrButton).setDirection(i == 0 ? 7 : 1);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1479 */       if ((this.incrButton instanceof BasicArrowButton)) {
/* 1480 */         ((BasicArrowButton)this.incrButton).setDirection(i == 0 ? 7 : 5);
/*      */       }
/*      */ 
/* 1483 */       if ((this.decrButton instanceof BasicArrowButton))
/* 1484 */         ((BasicArrowButton)this.decrButton).setDirection(i == 0 ? 3 : 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
/*      */     private static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
/*      */     private static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
/*      */     private static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
/*      */     private static final String MIN_SCROLL = "minScroll";
/*      */     private static final String MAX_SCROLL = "maxScroll";
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/* 1519 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1523 */       JScrollBar localJScrollBar = (JScrollBar)paramActionEvent.getSource();
/* 1524 */       String str = getName();
/* 1525 */       if (str == "positiveUnitIncrement") {
/* 1526 */         scroll(localJScrollBar, 1, false);
/*      */       }
/* 1528 */       else if (str == "positiveBlockIncrement") {
/* 1529 */         scroll(localJScrollBar, 1, true);
/*      */       }
/* 1531 */       else if (str == "negativeUnitIncrement") {
/* 1532 */         scroll(localJScrollBar, -1, false);
/*      */       }
/* 1534 */       else if (str == "negativeBlockIncrement") {
/* 1535 */         scroll(localJScrollBar, -1, true);
/*      */       }
/* 1537 */       else if (str == "minScroll") {
/* 1538 */         scroll(localJScrollBar, 2, true);
/*      */       }
/* 1540 */       else if (str == "maxScroll")
/* 1541 */         scroll(localJScrollBar, 3, true);
/*      */     }
/*      */ 
/*      */     private void scroll(JScrollBar paramJScrollBar, int paramInt, boolean paramBoolean)
/*      */     {
/* 1546 */       if ((paramInt == -1) || (paramInt == 1))
/*      */       {
/*      */         int i;
/* 1551 */         if (paramBoolean) {
/* 1552 */           if (paramInt == -1) {
/* 1553 */             i = -1 * paramJScrollBar.getBlockIncrement(-1);
/*      */           }
/*      */           else {
/* 1556 */             i = paramJScrollBar.getBlockIncrement(1);
/*      */           }
/*      */ 
/*      */         }
/* 1560 */         else if (paramInt == -1) {
/* 1561 */           i = -1 * paramJScrollBar.getUnitIncrement(-1);
/*      */         }
/*      */         else {
/* 1564 */           i = paramJScrollBar.getUnitIncrement(1);
/*      */         }
/*      */ 
/* 1567 */         paramJScrollBar.setValue(paramJScrollBar.getValue() + i);
/*      */       }
/* 1569 */       else if (paramInt == 2) {
/* 1570 */         paramJScrollBar.setValue(paramJScrollBar.getMinimum());
/*      */       }
/* 1572 */       else if (paramInt == 3) {
/* 1573 */         paramJScrollBar.setValue(paramJScrollBar.getMaximum());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ArrowButtonListener extends MouseAdapter
/*      */   {
/*      */     boolean handledEvent;
/*      */ 
/*      */     protected ArrowButtonListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1363 */       if (!BasicScrollBarUI.this.scrollbar.isEnabled()) return;
/*      */ 
/* 1366 */       if (!SwingUtilities.isLeftMouseButton(paramMouseEvent)) return;
/*      */ 
/* 1368 */       int i = paramMouseEvent.getSource() == BasicScrollBarUI.this.incrButton ? 1 : -1;
/*      */ 
/* 1370 */       BasicScrollBarUI.this.scrollByUnit(i);
/* 1371 */       BasicScrollBarUI.this.scrollTimer.stop();
/* 1372 */       BasicScrollBarUI.this.scrollListener.setDirection(i);
/* 1373 */       BasicScrollBarUI.this.scrollListener.setScrollByBlock(false);
/* 1374 */       BasicScrollBarUI.this.scrollTimer.start();
/*      */ 
/* 1376 */       this.handledEvent = true;
/* 1377 */       if ((!BasicScrollBarUI.this.scrollbar.hasFocus()) && (BasicScrollBarUI.this.scrollbar.isRequestFocusEnabled()))
/* 1378 */         BasicScrollBarUI.this.scrollbar.requestFocus();
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 1383 */       BasicScrollBarUI.this.scrollTimer.stop();
/* 1384 */       this.handledEvent = false;
/* 1385 */       BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
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
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 1587 */       BasicScrollBarUI.this.scrollbar.repaint();
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 1591 */       BasicScrollBarUI.this.scrollbar.repaint();
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1599 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */       Object localObject;
/* 1601 */       if ("model" == str) {
/* 1602 */         localObject = (BoundedRangeModel)paramPropertyChangeEvent.getOldValue();
/* 1603 */         BoundedRangeModel localBoundedRangeModel = (BoundedRangeModel)paramPropertyChangeEvent.getNewValue();
/* 1604 */         ((BoundedRangeModel)localObject).removeChangeListener(BasicScrollBarUI.this.modelListener);
/* 1605 */         localBoundedRangeModel.addChangeListener(BasicScrollBarUI.this.modelListener);
/* 1606 */         BasicScrollBarUI.this.scrollBarValue = BasicScrollBarUI.this.scrollbar.getValue();
/* 1607 */         BasicScrollBarUI.this.scrollbar.repaint();
/* 1608 */         BasicScrollBarUI.this.scrollbar.revalidate();
/* 1609 */       } else if ("orientation" == str) {
/* 1610 */         BasicScrollBarUI.this.updateButtonDirections();
/* 1611 */       } else if ("componentOrientation" == str) {
/* 1612 */         BasicScrollBarUI.this.updateButtonDirections();
/* 1613 */         localObject = BasicScrollBarUI.this.getInputMap(0);
/* 1614 */         SwingUtilities.replaceUIInputMap(BasicScrollBarUI.this.scrollbar, 0, (InputMap)localObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ModelListener
/*      */     implements ChangeListener
/*      */   {
/*      */     protected ModelListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1047 */       if (!BasicScrollBarUI.this.useCachedValue) {
/* 1048 */         BasicScrollBarUI.this.scrollBarValue = BasicScrollBarUI.this.scrollbar.getValue();
/*      */       }
/* 1050 */       BasicScrollBarUI.this.layoutContainer(BasicScrollBarUI.this.scrollbar);
/* 1051 */       BasicScrollBarUI.this.useCachedValue = false;
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
/* 1498 */       BasicScrollBarUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ScrollListener
/*      */     implements ActionListener
/*      */   {
/* 1396 */     int direction = 1;
/*      */     boolean useBlockIncrement;
/*      */ 
/*      */     public ScrollListener()
/*      */     {
/* 1400 */       this.direction = 1;
/* 1401 */       this.useBlockIncrement = false;
/*      */     }
/*      */ 
/*      */     public ScrollListener(int paramBoolean, boolean arg3) {
/* 1405 */       this.direction = paramBoolean;
/*      */       boolean bool;
/* 1406 */       this.useBlockIncrement = bool;
/*      */     }
/*      */     public void setDirection(int paramInt) {
/* 1409 */       this.direction = paramInt; } 
/* 1410 */     public void setScrollByBlock(boolean paramBoolean) { this.useBlockIncrement = paramBoolean; }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1413 */       if (this.useBlockIncrement) {
/* 1414 */         BasicScrollBarUI.this.scrollByBlock(this.direction);
/*      */ 
/* 1416 */         if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1) {
/* 1417 */           if (this.direction > 0) {
/* 1418 */             if (BasicScrollBarUI.this.getThumbBounds().y + BasicScrollBarUI.this.getThumbBounds().height >= BasicScrollBarUI.this.trackListener.currentMouseY)
/*      */             {
/* 1420 */               ((Timer)paramActionEvent.getSource()).stop();
/*      */             } } else if (BasicScrollBarUI.this.getThumbBounds().y <= BasicScrollBarUI.this.trackListener.currentMouseY) {
/* 1422 */             ((Timer)paramActionEvent.getSource()).stop();
/*      */           }
/*      */         }
/* 1425 */         else if (((this.direction > 0) && (!BasicScrollBarUI.this.isMouseAfterThumb())) || ((this.direction < 0) && (!BasicScrollBarUI.this.isMouseBeforeThumb())))
/*      */         {
/* 1428 */           ((Timer)paramActionEvent.getSource()).stop();
/*      */         }
/*      */       }
/*      */       else {
/* 1432 */         BasicScrollBarUI.this.scrollByUnit(this.direction);
/*      */       }
/*      */ 
/* 1435 */       if ((this.direction > 0) && (BasicScrollBarUI.this.scrollbar.getValue() + BasicScrollBarUI.this.scrollbar.getVisibleAmount() >= BasicScrollBarUI.this.scrollbar.getMaximum()))
/*      */       {
/* 1438 */         ((Timer)paramActionEvent.getSource()).stop();
/* 1439 */       } else if ((this.direction < 0) && (BasicScrollBarUI.this.scrollbar.getValue() <= BasicScrollBarUI.this.scrollbar.getMinimum()))
/*      */       {
/* 1441 */         ((Timer)paramActionEvent.getSource()).stop();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class TrackListener extends MouseAdapter
/*      */     implements MouseMotionListener
/*      */   {
/*      */     protected transient int offset;
/*      */     protected transient int currentMouseX;
/*      */     protected transient int currentMouseY;
/* 1064 */     private transient int direction = 1;
/*      */ 
/*      */     protected TrackListener() {
/*      */     }
/* 1068 */     public void mouseReleased(MouseEvent paramMouseEvent) { if (BasicScrollBarUI.this.isDragging) {
/* 1069 */         BasicScrollBarUI.this.updateThumbState(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */       }
/* 1071 */       if ((SwingUtilities.isRightMouseButton(paramMouseEvent)) || ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning()) && (SwingUtilities.isMiddleMouseButton(paramMouseEvent))))
/*      */       {
/* 1074 */         return;
/* 1075 */       }if (!BasicScrollBarUI.this.scrollbar.isEnabled()) {
/* 1076 */         return;
/*      */       }
/* 1078 */       Rectangle localRectangle = BasicScrollBarUI.this.getTrackBounds();
/* 1079 */       BasicScrollBarUI.this.scrollbar.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 1081 */       BasicScrollBarUI.this.trackHighlight = 0;
/* 1082 */       BasicScrollBarUI.this.isDragging = false;
/* 1083 */       this.offset = 0;
/* 1084 */       BasicScrollBarUI.this.scrollTimer.stop();
/* 1085 */       BasicScrollBarUI.this.useCachedValue = true;
/* 1086 */       BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(false);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 1099 */       if ((SwingUtilities.isRightMouseButton(paramMouseEvent)) || ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning()) && (SwingUtilities.isMiddleMouseButton(paramMouseEvent))))
/*      */       {
/* 1102 */         return;
/* 1103 */       }if (!BasicScrollBarUI.this.scrollbar.isEnabled()) {
/* 1104 */         return;
/*      */       }
/* 1106 */       if ((!BasicScrollBarUI.this.scrollbar.hasFocus()) && (BasicScrollBarUI.this.scrollbar.isRequestFocusEnabled())) {
/* 1107 */         BasicScrollBarUI.this.scrollbar.requestFocus();
/*      */       }
/*      */ 
/* 1110 */       BasicScrollBarUI.this.useCachedValue = true;
/* 1111 */       BasicScrollBarUI.this.scrollbar.setValueIsAdjusting(true);
/*      */ 
/* 1113 */       this.currentMouseX = paramMouseEvent.getX();
/* 1114 */       this.currentMouseY = paramMouseEvent.getY();
/*      */ 
/* 1117 */       if (BasicScrollBarUI.this.getThumbBounds().contains(this.currentMouseX, this.currentMouseY)) {
/* 1118 */         switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
/*      */         case 1:
/* 1120 */           this.offset = (this.currentMouseY - BasicScrollBarUI.this.getThumbBounds().y);
/* 1121 */           break;
/*      */         case 0:
/* 1123 */           this.offset = (this.currentMouseX - BasicScrollBarUI.this.getThumbBounds().x);
/*      */         }
/*      */ 
/* 1126 */         BasicScrollBarUI.this.isDragging = true;
/* 1127 */         return;
/*      */       }
/* 1129 */       if ((BasicScrollBarUI.this.getSupportsAbsolutePositioning()) && (SwingUtilities.isMiddleMouseButton(paramMouseEvent)))
/*      */       {
/* 1131 */         switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
/*      */         case 1:
/* 1133 */           this.offset = (BasicScrollBarUI.this.getThumbBounds().height / 2);
/* 1134 */           break;
/*      */         case 0:
/* 1136 */           this.offset = (BasicScrollBarUI.this.getThumbBounds().width / 2);
/*      */         }
/*      */ 
/* 1139 */         BasicScrollBarUI.this.isDragging = true;
/* 1140 */         setValueFrom(paramMouseEvent);
/* 1141 */         return;
/*      */       }
/* 1143 */       BasicScrollBarUI.this.isDragging = false;
/*      */ 
/* 1145 */       Dimension localDimension = BasicScrollBarUI.this.scrollbar.getSize();
/* 1146 */       this.direction = 1;
/*      */       int i;
/* 1148 */       switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
/*      */       case 1:
/* 1150 */         if (BasicScrollBarUI.this.getThumbBounds().isEmpty()) {
/* 1151 */           i = localDimension.height / 2;
/* 1152 */           this.direction = (this.currentMouseY < i ? -1 : 1);
/*      */         } else {
/* 1154 */           i = BasicScrollBarUI.this.getThumbBounds().y;
/* 1155 */           this.direction = (this.currentMouseY < i ? -1 : 1);
/*      */         }
/* 1157 */         break;
/*      */       case 0:
/* 1159 */         if (BasicScrollBarUI.this.getThumbBounds().isEmpty()) {
/* 1160 */           i = localDimension.width / 2;
/* 1161 */           this.direction = (this.currentMouseX < i ? -1 : 1);
/*      */         } else {
/* 1163 */           i = BasicScrollBarUI.this.getThumbBounds().x;
/* 1164 */           this.direction = (this.currentMouseX < i ? -1 : 1);
/*      */         }
/* 1166 */         if (!BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
/* 1167 */           this.direction = (-this.direction);
/*      */         }
/*      */         break;
/*      */       }
/* 1171 */       BasicScrollBarUI.this.scrollByBlock(this.direction);
/*      */ 
/* 1173 */       BasicScrollBarUI.this.scrollTimer.stop();
/* 1174 */       BasicScrollBarUI.this.scrollListener.setDirection(this.direction);
/* 1175 */       BasicScrollBarUI.this.scrollListener.setScrollByBlock(true);
/* 1176 */       startScrollTimerIfNecessary();
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/* 1187 */       if ((SwingUtilities.isRightMouseButton(paramMouseEvent)) || ((!BasicScrollBarUI.this.getSupportsAbsolutePositioning()) && (SwingUtilities.isMiddleMouseButton(paramMouseEvent))))
/*      */       {
/* 1190 */         return;
/* 1191 */       }if ((!BasicScrollBarUI.this.scrollbar.isEnabled()) || (BasicScrollBarUI.this.getThumbBounds().isEmpty())) {
/* 1192 */         return;
/*      */       }
/* 1194 */       if (BasicScrollBarUI.this.isDragging) {
/* 1195 */         setValueFrom(paramMouseEvent);
/*      */       } else {
/* 1197 */         this.currentMouseX = paramMouseEvent.getX();
/* 1198 */         this.currentMouseY = paramMouseEvent.getY();
/* 1199 */         BasicScrollBarUI.this.updateThumbState(this.currentMouseX, this.currentMouseY);
/* 1200 */         startScrollTimerIfNecessary();
/*      */       }
/*      */     }
/*      */ 
/*      */     private void setValueFrom(MouseEvent paramMouseEvent) {
/* 1205 */       boolean bool = BasicScrollBarUI.this.isThumbRollover();
/* 1206 */       BoundedRangeModel localBoundedRangeModel = BasicScrollBarUI.this.scrollbar.getModel();
/* 1207 */       Rectangle localRectangle = BasicScrollBarUI.this.getThumbBounds();
/*      */       int i;
/*      */       int j;
/*      */       int k;
/*      */       float f1;
/* 1211 */       if (BasicScrollBarUI.this.scrollbar.getOrientation() == 1) {
/* 1212 */         i = BasicScrollBarUI.this.trackRect.y;
/* 1213 */         j = BasicScrollBarUI.this.trackRect.y + BasicScrollBarUI.this.trackRect.height - localRectangle.height;
/* 1214 */         k = Math.min(j, Math.max(i, paramMouseEvent.getY() - this.offset));
/* 1215 */         BasicScrollBarUI.this.setThumbBounds(localRectangle.x, k, localRectangle.width, localRectangle.height);
/* 1216 */         f1 = BasicScrollBarUI.this.getTrackBounds().height;
/*      */       }
/*      */       else {
/* 1219 */         i = BasicScrollBarUI.this.trackRect.x;
/* 1220 */         j = BasicScrollBarUI.this.trackRect.x + BasicScrollBarUI.this.trackRect.width - localRectangle.width;
/* 1221 */         k = Math.min(j, Math.max(i, paramMouseEvent.getX() - this.offset));
/* 1222 */         BasicScrollBarUI.this.setThumbBounds(k, localRectangle.y, localRectangle.width, localRectangle.height);
/* 1223 */         f1 = BasicScrollBarUI.this.getTrackBounds().width;
/*      */       }
/*      */ 
/* 1230 */       if (k == j) {
/* 1231 */         if ((BasicScrollBarUI.this.scrollbar.getOrientation() == 1) || (BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()))
/*      */         {
/* 1233 */           BasicScrollBarUI.this.scrollbar.setValue(localBoundedRangeModel.getMaximum() - localBoundedRangeModel.getExtent());
/*      */         }
/* 1235 */         else BasicScrollBarUI.this.scrollbar.setValue(localBoundedRangeModel.getMinimum());
/*      */       }
/*      */       else
/*      */       {
/* 1239 */         float f2 = localBoundedRangeModel.getMaximum() - localBoundedRangeModel.getExtent();
/* 1240 */         float f3 = f2 - localBoundedRangeModel.getMinimum();
/* 1241 */         float f4 = k - i;
/* 1242 */         float f5 = j - i;
/*      */         int m;
/* 1244 */         if ((BasicScrollBarUI.this.scrollbar.getOrientation() == 1) || (BasicScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()))
/*      */         {
/* 1246 */           m = (int)(0.5D + f4 / f5 * f3);
/*      */         }
/* 1248 */         else m = (int)(0.5D + (j - k) / f5 * f3);
/*      */ 
/* 1251 */         BasicScrollBarUI.this.useCachedValue = true;
/* 1252 */         BasicScrollBarUI.this.scrollBarValue = (m + localBoundedRangeModel.getMinimum());
/* 1253 */         BasicScrollBarUI.this.scrollbar.setValue(adjustValueIfNecessary(BasicScrollBarUI.this.scrollBarValue));
/*      */       }
/* 1255 */       BasicScrollBarUI.this.setThumbRollover(bool);
/*      */     }
/*      */ 
/*      */     private int adjustValueIfNecessary(int paramInt) {
/* 1259 */       if ((BasicScrollBarUI.this.scrollbar.getParent() instanceof JScrollPane)) {
/* 1260 */         JScrollPane localJScrollPane = (JScrollPane)BasicScrollBarUI.this.scrollbar.getParent();
/* 1261 */         JViewport localJViewport = localJScrollPane.getViewport();
/* 1262 */         Component localComponent = localJViewport.getView();
/* 1263 */         if ((localComponent instanceof JList)) {
/* 1264 */           JList localJList = (JList)localComponent;
/* 1265 */           if (DefaultLookup.getBoolean(localJList, localJList.getUI(), "List.lockToPositionOnScroll", false))
/*      */           {
/* 1267 */             int i = paramInt;
/* 1268 */             int j = localJList.getLayoutOrientation();
/* 1269 */             int k = BasicScrollBarUI.this.scrollbar.getOrientation();
/*      */             int m;
/*      */             Rectangle localRectangle1;
/* 1270 */             if ((k == 1) && (j == 0)) {
/* 1271 */               m = localJList.locationToIndex(new Point(0, paramInt));
/* 1272 */               localRectangle1 = localJList.getCellBounds(m, m);
/* 1273 */               if (localRectangle1 != null) {
/* 1274 */                 i = localRectangle1.y;
/*      */               }
/*      */             }
/* 1277 */             if ((k == 0) && ((j == 1) || (j == 2)))
/*      */             {
/* 1279 */               if (localJScrollPane.getComponentOrientation().isLeftToRight()) {
/* 1280 */                 m = localJList.locationToIndex(new Point(paramInt, 0));
/* 1281 */                 localRectangle1 = localJList.getCellBounds(m, m);
/* 1282 */                 if (localRectangle1 != null)
/* 1283 */                   i = localRectangle1.x;
/*      */               }
/*      */               else
/*      */               {
/* 1287 */                 Point localPoint = new Point(paramInt, 0);
/* 1288 */                 int n = localJViewport.getExtentSize().width;
/* 1289 */                 localPoint.x += n - 1;
/* 1290 */                 int i1 = localJList.locationToIndex(localPoint);
/* 1291 */                 Rectangle localRectangle2 = localJList.getCellBounds(i1, i1);
/* 1292 */                 if (localRectangle2 != null) {
/* 1293 */                   i = localRectangle2.x + localRectangle2.width - n;
/*      */                 }
/*      */               }
/*      */             }
/* 1297 */             paramInt = i;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1302 */       return paramInt;
/*      */     }
/*      */ 
/*      */     private void startScrollTimerIfNecessary() {
/* 1306 */       if (BasicScrollBarUI.this.scrollTimer.isRunning()) {
/* 1307 */         return;
/*      */       }
/*      */ 
/* 1310 */       Rectangle localRectangle = BasicScrollBarUI.this.getThumbBounds();
/*      */ 
/* 1312 */       switch (BasicScrollBarUI.this.scrollbar.getOrientation()) {
/*      */       case 1:
/* 1314 */         if (this.direction > 0) {
/* 1315 */           if (localRectangle.y + localRectangle.height < BasicScrollBarUI.this.trackListener.currentMouseY)
/* 1316 */             BasicScrollBarUI.this.scrollTimer.start();
/*      */         }
/* 1318 */         else if (localRectangle.y > BasicScrollBarUI.this.trackListener.currentMouseY)
/* 1319 */           BasicScrollBarUI.this.scrollTimer.start(); break;
/*      */       case 0:
/* 1323 */         if (((this.direction > 0) && (BasicScrollBarUI.this.isMouseAfterThumb())) || ((this.direction < 0) && (BasicScrollBarUI.this.isMouseBeforeThumb())))
/*      */         {
/* 1326 */           BasicScrollBarUI.this.scrollTimer.start();
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 1333 */       if (!BasicScrollBarUI.this.isDragging)
/* 1334 */         BasicScrollBarUI.this.updateThumbState(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/* 1345 */       if (!BasicScrollBarUI.this.isDragging)
/* 1346 */         BasicScrollBarUI.this.setThumbRollover(false);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicScrollBarUI
 * JD-Core Version:    0.6.2
 */