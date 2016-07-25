/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JSplitPane;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import sun.swing.DefaultLookup;
/*      */ 
/*      */ public class BasicSplitPaneDivider extends Container
/*      */   implements PropertyChangeListener
/*      */ {
/*      */   protected static final int ONE_TOUCH_SIZE = 6;
/*      */   protected static final int ONE_TOUCH_OFFSET = 2;
/*      */   protected DragController dragger;
/*      */   protected BasicSplitPaneUI splitPaneUI;
/*   85 */   protected int dividerSize = 0;
/*      */   protected Component hiddenDivider;
/*      */   protected JSplitPane splitPane;
/*      */   protected MouseHandler mouseHandler;
/*      */   protected int orientation;
/*      */   protected JButton leftButton;
/*      */   protected JButton rightButton;
/*      */   private Border border;
/*      */   private boolean mouseOver;
/*      */   private int oneTouchSize;
/*      */   private int oneTouchOffset;
/*      */   private boolean centerOneTouchButtons;
/*      */ 
/*      */   public BasicSplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
/*      */   {
/*  142 */     this.oneTouchSize = DefaultLookup.getInt(paramBasicSplitPaneUI.getSplitPane(), paramBasicSplitPaneUI, "SplitPane.oneTouchButtonSize", 6);
/*      */ 
/*  144 */     this.oneTouchOffset = DefaultLookup.getInt(paramBasicSplitPaneUI.getSplitPane(), paramBasicSplitPaneUI, "SplitPane.oneTouchButtonOffset", 2);
/*      */ 
/*  146 */     this.centerOneTouchButtons = DefaultLookup.getBoolean(paramBasicSplitPaneUI.getSplitPane(), paramBasicSplitPaneUI, "SplitPane.centerOneTouchButtons", true);
/*      */ 
/*  148 */     setLayout(new DividerLayout());
/*  149 */     setBasicSplitPaneUI(paramBasicSplitPaneUI);
/*  150 */     this.orientation = this.splitPane.getOrientation();
/*  151 */     setCursor(this.orientation == 1 ? Cursor.getPredefinedCursor(11) : Cursor.getPredefinedCursor(9));
/*      */ 
/*  154 */     setBackground(UIManager.getColor("SplitPane.background"));
/*      */   }
/*      */ 
/*      */   private void revalidateSplitPane() {
/*  158 */     invalidate();
/*  159 */     if (this.splitPane != null)
/*  160 */       this.splitPane.revalidate();
/*      */   }
/*      */ 
/*      */   public void setBasicSplitPaneUI(BasicSplitPaneUI paramBasicSplitPaneUI)
/*      */   {
/*  168 */     if (this.splitPane != null) {
/*  169 */       this.splitPane.removePropertyChangeListener(this);
/*  170 */       if (this.mouseHandler != null) {
/*  171 */         this.splitPane.removeMouseListener(this.mouseHandler);
/*  172 */         this.splitPane.removeMouseMotionListener(this.mouseHandler);
/*  173 */         removeMouseListener(this.mouseHandler);
/*  174 */         removeMouseMotionListener(this.mouseHandler);
/*  175 */         this.mouseHandler = null;
/*      */       }
/*      */     }
/*  178 */     this.splitPaneUI = paramBasicSplitPaneUI;
/*  179 */     if (paramBasicSplitPaneUI != null) {
/*  180 */       this.splitPane = paramBasicSplitPaneUI.getSplitPane();
/*  181 */       if (this.splitPane != null) {
/*  182 */         if (this.mouseHandler == null) this.mouseHandler = new MouseHandler();
/*  183 */         this.splitPane.addMouseListener(this.mouseHandler);
/*  184 */         this.splitPane.addMouseMotionListener(this.mouseHandler);
/*  185 */         addMouseListener(this.mouseHandler);
/*  186 */         addMouseMotionListener(this.mouseHandler);
/*  187 */         this.splitPane.addPropertyChangeListener(this);
/*  188 */         if (this.splitPane.isOneTouchExpandable())
/*  189 */           oneTouchExpandableChanged();
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  194 */       this.splitPane = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public BasicSplitPaneUI getBasicSplitPaneUI()
/*      */   {
/*  204 */     return this.splitPaneUI;
/*      */   }
/*      */ 
/*      */   public void setDividerSize(int paramInt)
/*      */   {
/*  214 */     this.dividerSize = paramInt;
/*      */   }
/*      */ 
/*      */   public int getDividerSize()
/*      */   {
/*  223 */     return this.dividerSize;
/*      */   }
/*      */ 
/*      */   public void setBorder(Border paramBorder)
/*      */   {
/*  232 */     Border localBorder = this.border;
/*      */ 
/*  234 */     this.border = paramBorder;
/*      */   }
/*      */ 
/*      */   public Border getBorder()
/*      */   {
/*  246 */     return this.border;
/*      */   }
/*      */ 
/*      */   public Insets getInsets()
/*      */   {
/*  257 */     Border localBorder = getBorder();
/*      */ 
/*  259 */     if (localBorder != null) {
/*  260 */       return localBorder.getBorderInsets(this);
/*      */     }
/*  262 */     return super.getInsets();
/*      */   }
/*      */ 
/*      */   protected void setMouseOver(boolean paramBoolean)
/*      */   {
/*  272 */     this.mouseOver = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isMouseOver()
/*      */   {
/*  282 */     return this.mouseOver;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/*  293 */     if (this.orientation == 1) {
/*  294 */       return new Dimension(getDividerSize(), 1);
/*      */     }
/*  296 */     return new Dimension(1, getDividerSize());
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/*  303 */     return getPreferredSize();
/*      */   }
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  312 */     if (paramPropertyChangeEvent.getSource() == this.splitPane)
/*  313 */       if (paramPropertyChangeEvent.getPropertyName() == "orientation") {
/*  314 */         this.orientation = this.splitPane.getOrientation();
/*  315 */         setCursor(this.orientation == 1 ? Cursor.getPredefinedCursor(11) : Cursor.getPredefinedCursor(9));
/*      */ 
/*  318 */         revalidateSplitPane();
/*      */       }
/*  320 */       else if (paramPropertyChangeEvent.getPropertyName() == "oneTouchExpandable")
/*      */       {
/*  322 */         oneTouchExpandableChanged();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/*  332 */     super.paint(paramGraphics);
/*      */ 
/*  335 */     Border localBorder = getBorder();
/*      */ 
/*  337 */     if (localBorder != null) {
/*  338 */       Dimension localDimension = getSize();
/*      */ 
/*  340 */       localBorder.paintBorder(this, paramGraphics, 0, 0, localDimension.width, localDimension.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void oneTouchExpandableChanged()
/*      */   {
/*  352 */     if (!DefaultLookup.getBoolean(this.splitPane, this.splitPaneUI, "SplitPane.supportsOneTouchButtons", true))
/*      */     {
/*  355 */       return;
/*      */     }
/*  357 */     if ((this.splitPane.isOneTouchExpandable()) && (this.leftButton == null) && (this.rightButton == null))
/*      */     {
/*  362 */       this.leftButton = createLeftOneTouchButton();
/*  363 */       if (this.leftButton != null) {
/*  364 */         this.leftButton.addActionListener(new OneTouchActionHandler(true));
/*      */       }
/*      */ 
/*  369 */       this.rightButton = createRightOneTouchButton();
/*  370 */       if (this.rightButton != null) {
/*  371 */         this.rightButton.addActionListener(new OneTouchActionHandler(false));
/*      */       }
/*      */ 
/*  374 */       if ((this.leftButton != null) && (this.rightButton != null)) {
/*  375 */         add(this.leftButton);
/*  376 */         add(this.rightButton);
/*      */       }
/*      */     }
/*  379 */     revalidateSplitPane();
/*      */   }
/*      */ 
/*      */   protected JButton createLeftOneTouchButton()
/*      */   {
/*  388 */     JButton local1 = new JButton() {
/*      */       public void setBorder(Border paramAnonymousBorder) {
/*      */       }
/*      */       public void paint(Graphics paramAnonymousGraphics) {
/*  392 */         if (BasicSplitPaneDivider.this.splitPane != null) {
/*  393 */           int[] arrayOfInt1 = new int[3];
/*  394 */           int[] arrayOfInt2 = new int[3];
/*      */ 
/*  398 */           paramAnonymousGraphics.setColor(getBackground());
/*  399 */           paramAnonymousGraphics.fillRect(0, 0, getWidth(), getHeight());
/*      */ 
/*  403 */           paramAnonymousGraphics.setColor(Color.black);
/*      */           int i;
/*  404 */           if (BasicSplitPaneDivider.this.orientation == 0) {
/*  405 */             i = Math.min(getHeight(), BasicSplitPaneDivider.this.oneTouchSize);
/*  406 */             arrayOfInt1[0] = i;
/*  407 */             arrayOfInt1[1] = 0;
/*  408 */             arrayOfInt1[2] = (i << 1);
/*  409 */             arrayOfInt2[0] = 0;
/*      */             int tmp99_97 = i; arrayOfInt2[2] = tmp99_97; arrayOfInt2[1] = tmp99_97;
/*  411 */             paramAnonymousGraphics.drawPolygon(arrayOfInt1, arrayOfInt2, 3);
/*      */           }
/*      */           else
/*      */           {
/*  415 */             i = Math.min(getWidth(), BasicSplitPaneDivider.this.oneTouchSize);
/*      */             int tmp134_132 = i; arrayOfInt1[2] = tmp134_132; arrayOfInt1[0] = tmp134_132;
/*  417 */             arrayOfInt1[1] = 0;
/*  418 */             arrayOfInt2[0] = 0;
/*  419 */             arrayOfInt2[1] = i;
/*  420 */             arrayOfInt2[2] = (i << 1);
/*      */           }
/*  422 */           paramAnonymousGraphics.fillPolygon(arrayOfInt1, arrayOfInt2, 3);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable() {
/*  427 */         return false;
/*      */       }
/*      */     };
/*  430 */     local1.setMinimumSize(new Dimension(this.oneTouchSize, this.oneTouchSize));
/*  431 */     local1.setCursor(Cursor.getPredefinedCursor(0));
/*  432 */     local1.setFocusPainted(false);
/*  433 */     local1.setBorderPainted(false);
/*  434 */     local1.setRequestFocusEnabled(false);
/*  435 */     return local1;
/*      */   }
/*      */ 
/*      */   protected JButton createRightOneTouchButton()
/*      */   {
/*  444 */     JButton local2 = new JButton() {
/*      */       public void setBorder(Border paramAnonymousBorder) {
/*      */       }
/*      */       public void paint(Graphics paramAnonymousGraphics) {
/*  448 */         if (BasicSplitPaneDivider.this.splitPane != null) {
/*  449 */           int[] arrayOfInt1 = new int[3];
/*  450 */           int[] arrayOfInt2 = new int[3];
/*      */ 
/*  454 */           paramAnonymousGraphics.setColor(getBackground());
/*  455 */           paramAnonymousGraphics.fillRect(0, 0, getWidth(), getHeight());
/*      */           int i;
/*  459 */           if (BasicSplitPaneDivider.this.orientation == 0) {
/*  460 */             i = Math.min(getHeight(), BasicSplitPaneDivider.this.oneTouchSize);
/*  461 */             arrayOfInt1[0] = i;
/*  462 */             arrayOfInt1[1] = (i << 1);
/*  463 */             arrayOfInt1[2] = 0;
/*  464 */             arrayOfInt2[0] = i;
/*      */             int tmp92_91 = 0; arrayOfInt2[2] = tmp92_91; arrayOfInt2[1] = tmp92_91;
/*      */           }
/*      */           else {
/*  468 */             i = Math.min(getWidth(), BasicSplitPaneDivider.this.oneTouchSize);
/*      */             int tmp119_118 = 0; arrayOfInt1[2] = tmp119_118; arrayOfInt1[0] = tmp119_118;
/*  470 */             arrayOfInt1[1] = i;
/*  471 */             arrayOfInt2[0] = 0;
/*  472 */             arrayOfInt2[1] = i;
/*  473 */             arrayOfInt2[2] = (i << 1);
/*      */           }
/*  475 */           paramAnonymousGraphics.setColor(Color.black);
/*  476 */           paramAnonymousGraphics.fillPolygon(arrayOfInt1, arrayOfInt2, 3);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable() {
/*  481 */         return false;
/*      */       }
/*      */     };
/*  484 */     local2.setMinimumSize(new Dimension(this.oneTouchSize, this.oneTouchSize));
/*  485 */     local2.setCursor(Cursor.getPredefinedCursor(0));
/*  486 */     local2.setFocusPainted(false);
/*  487 */     local2.setBorderPainted(false);
/*  488 */     local2.setRequestFocusEnabled(false);
/*  489 */     return local2;
/*      */   }
/*      */ 
/*      */   protected void prepareForDragging()
/*      */   {
/*  498 */     this.splitPaneUI.startDragging();
/*      */   }
/*      */ 
/*      */   protected void dragDividerTo(int paramInt)
/*      */   {
/*  507 */     this.splitPaneUI.dragDividerTo(paramInt);
/*      */   }
/*      */ 
/*      */   protected void finishDraggingTo(int paramInt)
/*      */   {
/*  516 */     this.splitPaneUI.finishDraggingTo(paramInt);
/*      */   }
/*      */ 
/*      */   protected class DividerLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     protected DividerLayout()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/*  898 */       if ((BasicSplitPaneDivider.this.leftButton != null) && (BasicSplitPaneDivider.this.rightButton != null) && (paramContainer == BasicSplitPaneDivider.this))
/*      */       {
/*  900 */         if (BasicSplitPaneDivider.this.splitPane.isOneTouchExpandable()) {
/*  901 */           Insets localInsets = BasicSplitPaneDivider.this.getInsets();
/*      */           int i;
/*      */           int j;
/*      */           int k;
/*  903 */           if (BasicSplitPaneDivider.this.orientation == 0) {
/*  904 */             i = localInsets != null ? localInsets.left : 0;
/*  905 */             j = BasicSplitPaneDivider.this.getHeight();
/*      */ 
/*  907 */             if (localInsets != null) {
/*  908 */               j -= localInsets.top + localInsets.bottom;
/*  909 */               j = Math.max(j, 0);
/*      */             }
/*  911 */             j = Math.min(j, BasicSplitPaneDivider.this.oneTouchSize);
/*      */ 
/*  913 */             k = (paramContainer.getSize().height - j) / 2;
/*      */ 
/*  915 */             if (!BasicSplitPaneDivider.this.centerOneTouchButtons) {
/*  916 */               k = localInsets != null ? localInsets.top : 0;
/*  917 */               i = 0;
/*      */             }
/*  919 */             BasicSplitPaneDivider.this.leftButton.setBounds(i + BasicSplitPaneDivider.this.oneTouchOffset, k, j * 2, j);
/*      */ 
/*  921 */             BasicSplitPaneDivider.this.rightButton.setBounds(i + BasicSplitPaneDivider.this.oneTouchOffset + BasicSplitPaneDivider.this.oneTouchSize * 2, k, j * 2, j);
/*      */           }
/*      */           else
/*      */           {
/*  926 */             i = localInsets != null ? localInsets.top : 0;
/*  927 */             j = BasicSplitPaneDivider.this.getWidth();
/*      */ 
/*  929 */             if (localInsets != null) {
/*  930 */               j -= localInsets.left + localInsets.right;
/*  931 */               j = Math.max(j, 0);
/*      */             }
/*  933 */             j = Math.min(j, BasicSplitPaneDivider.this.oneTouchSize);
/*      */ 
/*  935 */             k = (paramContainer.getSize().width - j) / 2;
/*      */ 
/*  937 */             if (!BasicSplitPaneDivider.this.centerOneTouchButtons) {
/*  938 */               k = localInsets != null ? localInsets.left : 0;
/*  939 */               i = 0;
/*      */             }
/*      */ 
/*  942 */             BasicSplitPaneDivider.this.leftButton.setBounds(k, i + BasicSplitPaneDivider.this.oneTouchOffset, j, j * 2);
/*      */ 
/*  944 */             BasicSplitPaneDivider.this.rightButton.setBounds(k, i + BasicSplitPaneDivider.this.oneTouchOffset + BasicSplitPaneDivider.this.oneTouchSize * 2, j, j * 2);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  950 */           BasicSplitPaneDivider.this.leftButton.setBounds(-5, -5, 1, 1);
/*  951 */           BasicSplitPaneDivider.this.rightButton.setBounds(-5, -5, 1, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/*  961 */       if ((paramContainer != BasicSplitPaneDivider.this) || (BasicSplitPaneDivider.this.splitPane == null)) {
/*  962 */         return new Dimension(0, 0);
/*      */       }
/*  964 */       Dimension localDimension = null;
/*      */ 
/*  966 */       if ((BasicSplitPaneDivider.this.splitPane.isOneTouchExpandable()) && (BasicSplitPaneDivider.this.leftButton != null)) {
/*  967 */         localDimension = BasicSplitPaneDivider.this.leftButton.getMinimumSize();
/*      */       }
/*      */ 
/*  970 */       Insets localInsets = BasicSplitPaneDivider.this.getInsets();
/*  971 */       int i = BasicSplitPaneDivider.this.getDividerSize();
/*  972 */       int j = i;
/*      */       int k;
/*  974 */       if (BasicSplitPaneDivider.this.orientation == 0) {
/*  975 */         if (localDimension != null) {
/*  976 */           k = localDimension.height;
/*  977 */           if (localInsets != null) {
/*  978 */             k += localInsets.top + localInsets.bottom;
/*      */           }
/*  980 */           j = Math.max(j, k);
/*      */         }
/*  982 */         i = 1;
/*      */       }
/*      */       else {
/*  985 */         if (localDimension != null) {
/*  986 */           k = localDimension.width;
/*  987 */           if (localInsets != null) {
/*  988 */             k += localInsets.left + localInsets.right;
/*      */           }
/*  990 */           i = Math.max(i, k);
/*      */         }
/*  992 */         j = 1;
/*      */       }
/*  994 */       return new Dimension(i, j);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/*  999 */       return minimumLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class DragController
/*      */   {
/*      */     int initialX;
/*      */     int maxX;
/*      */     int minX;
/*      */     int offset;
/*      */ 
/*      */     protected DragController(MouseEvent arg2)
/*      */     {
/*  697 */       JSplitPane localJSplitPane = BasicSplitPaneDivider.this.splitPaneUI.getSplitPane();
/*  698 */       Component localComponent1 = localJSplitPane.getLeftComponent();
/*  699 */       Component localComponent2 = localJSplitPane.getRightComponent();
/*      */ 
/*  701 */       this.initialX = BasicSplitPaneDivider.this.getLocation().x;
/*      */       Object localObject;
/*  702 */       if (localObject.getSource() == BasicSplitPaneDivider.this) {
/*  703 */         this.offset = localObject.getX();
/*      */       }
/*      */       else {
/*  706 */         this.offset = (localObject.getX() - this.initialX);
/*      */       }
/*  708 */       if ((localComponent1 == null) || (localComponent2 == null) || (this.offset < -1) || (this.offset >= BasicSplitPaneDivider.this.getSize().width))
/*      */       {
/*  711 */         this.maxX = -1;
/*      */       }
/*      */       else {
/*  714 */         Insets localInsets = localJSplitPane.getInsets();
/*      */ 
/*  716 */         if (localComponent1.isVisible()) {
/*  717 */           this.minX = localComponent1.getMinimumSize().width;
/*  718 */           if (localInsets != null)
/*  719 */             this.minX += localInsets.left;
/*      */         }
/*      */         else
/*      */         {
/*  723 */           this.minX = 0;
/*      */         }
/*      */         int i;
/*  725 */         if (localComponent2.isVisible()) {
/*  726 */           i = localInsets != null ? localInsets.right : 0;
/*  727 */           this.maxX = Math.max(0, localJSplitPane.getSize().width - (BasicSplitPaneDivider.this.getSize().width + i) - localComponent2.getMinimumSize().width);
/*      */         }
/*      */         else
/*      */         {
/*  732 */           i = localInsets != null ? localInsets.right : 0;
/*  733 */           this.maxX = Math.max(0, localJSplitPane.getSize().width - (BasicSplitPaneDivider.this.getSize().width + i));
/*      */         }
/*      */ 
/*  736 */         if (this.maxX < this.minX) this.minX = (this.maxX = 0);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected boolean isValid()
/*      */     {
/*  745 */       return this.maxX > 0;
/*      */     }
/*      */ 
/*      */     protected int positionForMouseEvent(MouseEvent paramMouseEvent)
/*      */     {
/*  754 */       int i = paramMouseEvent.getSource() == BasicSplitPaneDivider.this ? paramMouseEvent.getX() + BasicSplitPaneDivider.this.getLocation().x : paramMouseEvent.getX();
/*      */ 
/*  757 */       i = Math.min(this.maxX, Math.max(this.minX, i - this.offset));
/*  758 */       return i;
/*      */     }
/*      */ 
/*      */     protected int getNeededLocation(int paramInt1, int paramInt2)
/*      */     {
/*  769 */       int i = Math.min(this.maxX, Math.max(this.minX, paramInt1 - this.offset));
/*  770 */       return i;
/*      */     }
/*      */ 
/*      */     protected void continueDrag(int paramInt1, int paramInt2)
/*      */     {
/*  775 */       BasicSplitPaneDivider.this.dragDividerTo(getNeededLocation(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     protected void continueDrag(MouseEvent paramMouseEvent)
/*      */     {
/*  784 */       BasicSplitPaneDivider.this.dragDividerTo(positionForMouseEvent(paramMouseEvent));
/*      */     }
/*      */ 
/*      */     protected void completeDrag(int paramInt1, int paramInt2)
/*      */     {
/*  789 */       BasicSplitPaneDivider.this.finishDraggingTo(getNeededLocation(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     protected void completeDrag(MouseEvent paramMouseEvent)
/*      */     {
/*  798 */       BasicSplitPaneDivider.this.finishDraggingTo(positionForMouseEvent(paramMouseEvent));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class MouseHandler extends MouseAdapter
/*      */     implements MouseMotionListener
/*      */   {
/*      */     protected MouseHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/*  534 */       if (((paramMouseEvent.getSource() == BasicSplitPaneDivider.this) || (paramMouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane)) && (BasicSplitPaneDivider.this.dragger == null) && (BasicSplitPaneDivider.this.splitPane.isEnabled()))
/*      */       {
/*  537 */         Component localComponent = BasicSplitPaneDivider.this.splitPaneUI.getNonContinuousLayoutDivider();
/*      */ 
/*  540 */         if (BasicSplitPaneDivider.this.hiddenDivider != localComponent) {
/*  541 */           if (BasicSplitPaneDivider.this.hiddenDivider != null) {
/*  542 */             BasicSplitPaneDivider.this.hiddenDivider.removeMouseListener(this);
/*  543 */             BasicSplitPaneDivider.this.hiddenDivider.removeMouseMotionListener(this);
/*      */           }
/*  545 */           BasicSplitPaneDivider.this.hiddenDivider = localComponent;
/*  546 */           if (BasicSplitPaneDivider.this.hiddenDivider != null) {
/*  547 */             BasicSplitPaneDivider.this.hiddenDivider.addMouseMotionListener(this);
/*  548 */             BasicSplitPaneDivider.this.hiddenDivider.addMouseListener(this);
/*      */           }
/*      */         }
/*  551 */         if ((BasicSplitPaneDivider.this.splitPane.getLeftComponent() != null) && (BasicSplitPaneDivider.this.splitPane.getRightComponent() != null))
/*      */         {
/*  553 */           if (BasicSplitPaneDivider.this.orientation == 1) {
/*  554 */             BasicSplitPaneDivider.this.dragger = new BasicSplitPaneDivider.DragController(BasicSplitPaneDivider.this, paramMouseEvent);
/*      */           }
/*      */           else {
/*  557 */             BasicSplitPaneDivider.this.dragger = new BasicSplitPaneDivider.VerticalDragController(BasicSplitPaneDivider.this, paramMouseEvent);
/*      */           }
/*  559 */           if (!BasicSplitPaneDivider.this.dragger.isValid()) {
/*  560 */             BasicSplitPaneDivider.this.dragger = null;
/*      */           }
/*      */           else {
/*  563 */             BasicSplitPaneDivider.this.prepareForDragging();
/*  564 */             BasicSplitPaneDivider.this.dragger.continueDrag(paramMouseEvent);
/*      */           }
/*      */         }
/*  567 */         paramMouseEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/*  576 */       if (BasicSplitPaneDivider.this.dragger != null) {
/*  577 */         if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane) {
/*  578 */           BasicSplitPaneDivider.this.dragger.completeDrag(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */         }
/*      */         else
/*      */         {
/*      */           Point localPoint;
/*  580 */           if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this) {
/*  581 */             localPoint = BasicSplitPaneDivider.this.getLocation();
/*      */ 
/*  583 */             BasicSplitPaneDivider.this.dragger.completeDrag(paramMouseEvent.getX() + localPoint.x, paramMouseEvent.getY() + localPoint.y);
/*      */           }
/*  586 */           else if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this.hiddenDivider) {
/*  587 */             localPoint = BasicSplitPaneDivider.this.hiddenDivider.getLocation();
/*  588 */             int i = paramMouseEvent.getX() + localPoint.x;
/*  589 */             int j = paramMouseEvent.getY() + localPoint.y;
/*      */ 
/*  591 */             BasicSplitPaneDivider.this.dragger.completeDrag(i, j);
/*      */           }
/*      */         }
/*  593 */         BasicSplitPaneDivider.this.dragger = null;
/*  594 */         paramMouseEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/*  607 */       if (BasicSplitPaneDivider.this.dragger != null) {
/*  608 */         if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane) {
/*  609 */           BasicSplitPaneDivider.this.dragger.continueDrag(paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */         }
/*      */         else
/*      */         {
/*      */           Point localPoint;
/*  611 */           if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this) {
/*  612 */             localPoint = BasicSplitPaneDivider.this.getLocation();
/*      */ 
/*  614 */             BasicSplitPaneDivider.this.dragger.continueDrag(paramMouseEvent.getX() + localPoint.x, paramMouseEvent.getY() + localPoint.y);
/*      */           }
/*  617 */           else if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this.hiddenDivider) {
/*  618 */             localPoint = BasicSplitPaneDivider.this.hiddenDivider.getLocation();
/*  619 */             int i = paramMouseEvent.getX() + localPoint.x;
/*  620 */             int j = paramMouseEvent.getY() + localPoint.y;
/*      */ 
/*  622 */             BasicSplitPaneDivider.this.dragger.continueDrag(i, j);
/*      */           }
/*      */         }
/*  624 */         paramMouseEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*  642 */       if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this)
/*  643 */         BasicSplitPaneDivider.this.setMouseOver(true);
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*  654 */       if (paramMouseEvent.getSource() == BasicSplitPaneDivider.this)
/*  655 */         BasicSplitPaneDivider.this.setMouseOver(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class OneTouchActionHandler
/*      */     implements ActionListener
/*      */   {
/*      */     private boolean toMinimum;
/*      */ 
/*      */     OneTouchActionHandler(boolean arg2)
/*      */     {
/*      */       boolean bool;
/* 1019 */       this.toMinimum = bool;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1023 */       Insets localInsets = BasicSplitPaneDivider.this.splitPane.getInsets();
/* 1024 */       int i = BasicSplitPaneDivider.this.splitPane.getLastDividerLocation();
/* 1025 */       int j = BasicSplitPaneDivider.this.splitPaneUI.getDividerLocation(BasicSplitPaneDivider.this.splitPane);
/*      */       int m;
/*      */       int k;
/* 1030 */       if (this.toMinimum) {
/* 1031 */         if (BasicSplitPaneDivider.this.orientation == 0) {
/* 1032 */           if (j >= BasicSplitPaneDivider.this.splitPane.getHeight() - localInsets.bottom - BasicSplitPaneDivider.this.getHeight())
/*      */           {
/* 1034 */             m = BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation();
/* 1035 */             k = Math.min(i, m);
/* 1036 */             BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
/*      */           }
/*      */           else {
/* 1039 */             k = localInsets.top;
/* 1040 */             BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
/*      */           }
/*      */ 
/*      */         }
/* 1044 */         else if (j >= BasicSplitPaneDivider.this.splitPane.getWidth() - localInsets.right - BasicSplitPaneDivider.this.getWidth())
/*      */         {
/* 1046 */           m = BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation();
/* 1047 */           k = Math.min(i, m);
/* 1048 */           BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
/*      */         }
/*      */         else {
/* 1051 */           k = localInsets.left;
/* 1052 */           BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
/*      */         }
/*      */ 
/*      */       }
/* 1057 */       else if (BasicSplitPaneDivider.this.orientation == 0) {
/* 1058 */         if (j == localInsets.top) {
/* 1059 */           m = BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation();
/* 1060 */           k = Math.min(i, m);
/* 1061 */           BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
/*      */         }
/*      */         else {
/* 1064 */           k = BasicSplitPaneDivider.this.splitPane.getHeight() - BasicSplitPaneDivider.this.getHeight() - localInsets.top;
/*      */ 
/* 1066 */           BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
/*      */         }
/*      */ 
/*      */       }
/* 1070 */       else if (j == localInsets.left) {
/* 1071 */         m = BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation();
/* 1072 */         k = Math.min(i, m);
/* 1073 */         BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
/*      */       }
/*      */       else {
/* 1076 */         k = BasicSplitPaneDivider.this.splitPane.getWidth() - BasicSplitPaneDivider.this.getWidth() - localInsets.left;
/*      */ 
/* 1078 */         BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
/*      */       }
/*      */ 
/* 1082 */       if (j != k) {
/* 1083 */         BasicSplitPaneDivider.this.splitPane.setDividerLocation(k);
/*      */ 
/* 1086 */         BasicSplitPaneDivider.this.splitPane.setLastDividerLocation(j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class VerticalDragController extends BasicSplitPaneDivider.DragController
/*      */   {
/*      */     protected VerticalDragController(MouseEvent arg2)
/*      */     {
/*  815 */       super(localMouseEvent);
/*  816 */       JSplitPane localJSplitPane = BasicSplitPaneDivider.this.splitPaneUI.getSplitPane();
/*  817 */       Component localComponent1 = localJSplitPane.getLeftComponent();
/*  818 */       Component localComponent2 = localJSplitPane.getRightComponent();
/*      */ 
/*  820 */       this.initialX = BasicSplitPaneDivider.this.getLocation().y;
/*  821 */       if (localMouseEvent.getSource() == BasicSplitPaneDivider.this) {
/*  822 */         this.offset = localMouseEvent.getY();
/*      */       }
/*      */       else {
/*  825 */         this.offset = (localMouseEvent.getY() - this.initialX);
/*      */       }
/*  827 */       if ((localComponent1 == null) || (localComponent2 == null) || (this.offset < -1) || (this.offset > BasicSplitPaneDivider.this.getSize().height))
/*      */       {
/*  830 */         this.maxX = -1;
/*      */       }
/*      */       else {
/*  833 */         Insets localInsets = localJSplitPane.getInsets();
/*      */ 
/*  835 */         if (localComponent1.isVisible()) {
/*  836 */           this.minX = localComponent1.getMinimumSize().height;
/*  837 */           if (localInsets != null)
/*  838 */             this.minX += localInsets.top;
/*      */         }
/*      */         else
/*      */         {
/*  842 */           this.minX = 0;
/*      */         }
/*      */         int i;
/*  844 */         if (localComponent2.isVisible()) {
/*  845 */           i = localInsets != null ? localInsets.bottom : 0;
/*      */ 
/*  847 */           this.maxX = Math.max(0, localJSplitPane.getSize().height - (BasicSplitPaneDivider.this.getSize().height + i) - localComponent2.getMinimumSize().height);
/*      */         }
/*      */         else
/*      */         {
/*  852 */           i = localInsets != null ? localInsets.bottom : 0;
/*      */ 
/*  854 */           this.maxX = Math.max(0, localJSplitPane.getSize().height - (BasicSplitPaneDivider.this.getSize().height + i));
/*      */         }
/*      */ 
/*  857 */         if (this.maxX < this.minX) this.minX = (this.maxX = 0);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected int getNeededLocation(int paramInt1, int paramInt2)
/*      */     {
/*  869 */       int i = Math.min(this.maxX, Math.max(this.minX, paramInt2 - this.offset));
/*  870 */       return i;
/*      */     }
/*      */ 
/*      */     protected int positionForMouseEvent(MouseEvent paramMouseEvent)
/*      */     {
/*  879 */       int i = paramMouseEvent.getSource() == BasicSplitPaneDivider.this ? paramMouseEvent.getY() + BasicSplitPaneDivider.this.getLocation().y : paramMouseEvent.getY();
/*      */ 
/*  883 */       i = Math.min(this.maxX, Math.max(this.minX, i - this.offset));
/*  884 */       return i;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicSplitPaneDivider
 * JD-Core Version:    0.6.2
 */