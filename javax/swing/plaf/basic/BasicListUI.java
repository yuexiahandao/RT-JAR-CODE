/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JList.DropLocation;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ListUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicListUI extends ListUI
/*      */ {
/*   59 */   private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("List.baselineComponent");
/*      */   protected JList list;
/*      */   protected CellRendererPane rendererPane;
/*      */   protected FocusListener focusListener;
/*      */   protected MouseInputListener mouseInputListener;
/*      */   protected ListSelectionListener listSelectionListener;
/*      */   protected ListDataListener listDataListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   private Handler handler;
/*      */   protected int[] cellHeights;
/*      */   protected int cellHeight;
/*      */   protected int cellWidth;
/*      */   protected int updateLayoutStateNeeded;
/*      */   private int listHeight;
/*      */   private int listWidth;
/*      */   private int layoutOrientation;
/*      */   private int columnCount;
/*      */   private int preferredHeight;
/*      */   private int rowsPerColumn;
/*      */   private long timeFactor;
/*      */   private boolean isFileList;
/*      */   private boolean isLeftToRight;
/*      */   protected static final int modelChanged = 1;
/*      */   protected static final int selectionModelChanged = 2;
/*      */   protected static final int fontChanged = 4;
/*      */   protected static final int fixedCellWidthChanged = 8;
/*      */   protected static final int fixedCellHeightChanged = 16;
/*      */   protected static final int prototypeCellValueChanged = 32;
/*      */   protected static final int cellRendererChanged = 64;
/*      */   private static final int layoutOrientationChanged = 128;
/*      */   private static final int heightChanged = 256;
/*      */   private static final int widthChanged = 512;
/*      */   private static final int componentOrientationChanged = 1024;
/*      */   private static final int DROP_LINE_THICKNESS = 2;
/*      */   private static final int CHANGE_LEAD = 0;
/*      */   private static final int CHANGE_SELECTION = 1;
/*      */   private static final int EXTEND_SELECTION = 2;
/* 2831 */   private static final TransferHandler defaultTransferHandler = new ListTransferHandler();
/*      */ 
/*      */   public BasicListUI()
/*      */   {
/*   62 */     this.list = null;
/*      */ 
/*   73 */     this.cellHeights = null;
/*   74 */     this.cellHeight = -1;
/*   75 */     this.cellWidth = -1;
/*   76 */     this.updateLayoutStateNeeded = 1;
/*      */ 
/*  115 */     this.timeFactor = 1000L;
/*      */ 
/*  120 */     this.isFileList = false;
/*      */ 
/*  125 */     this.isLeftToRight = true;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  149 */     paramLazyActionMap.put(new Actions("selectPreviousColumn"));
/*  150 */     paramLazyActionMap.put(new Actions("selectPreviousColumnExtendSelection"));
/*  151 */     paramLazyActionMap.put(new Actions("selectPreviousColumnChangeLead"));
/*  152 */     paramLazyActionMap.put(new Actions("selectNextColumn"));
/*  153 */     paramLazyActionMap.put(new Actions("selectNextColumnExtendSelection"));
/*  154 */     paramLazyActionMap.put(new Actions("selectNextColumnChangeLead"));
/*  155 */     paramLazyActionMap.put(new Actions("selectPreviousRow"));
/*  156 */     paramLazyActionMap.put(new Actions("selectPreviousRowExtendSelection"));
/*  157 */     paramLazyActionMap.put(new Actions("selectPreviousRowChangeLead"));
/*  158 */     paramLazyActionMap.put(new Actions("selectNextRow"));
/*  159 */     paramLazyActionMap.put(new Actions("selectNextRowExtendSelection"));
/*  160 */     paramLazyActionMap.put(new Actions("selectNextRowChangeLead"));
/*  161 */     paramLazyActionMap.put(new Actions("selectFirstRow"));
/*  162 */     paramLazyActionMap.put(new Actions("selectFirstRowExtendSelection"));
/*  163 */     paramLazyActionMap.put(new Actions("selectFirstRowChangeLead"));
/*  164 */     paramLazyActionMap.put(new Actions("selectLastRow"));
/*  165 */     paramLazyActionMap.put(new Actions("selectLastRowExtendSelection"));
/*  166 */     paramLazyActionMap.put(new Actions("selectLastRowChangeLead"));
/*  167 */     paramLazyActionMap.put(new Actions("scrollUp"));
/*  168 */     paramLazyActionMap.put(new Actions("scrollUpExtendSelection"));
/*  169 */     paramLazyActionMap.put(new Actions("scrollUpChangeLead"));
/*  170 */     paramLazyActionMap.put(new Actions("scrollDown"));
/*  171 */     paramLazyActionMap.put(new Actions("scrollDownExtendSelection"));
/*  172 */     paramLazyActionMap.put(new Actions("scrollDownChangeLead"));
/*  173 */     paramLazyActionMap.put(new Actions("selectAll"));
/*  174 */     paramLazyActionMap.put(new Actions("clearSelection"));
/*  175 */     paramLazyActionMap.put(new Actions("addToSelection"));
/*  176 */     paramLazyActionMap.put(new Actions("toggleAndAnchor"));
/*  177 */     paramLazyActionMap.put(new Actions("extendTo"));
/*  178 */     paramLazyActionMap.put(new Actions("moveSelectionTo"));
/*      */ 
/*  180 */     paramLazyActionMap.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
/*      */ 
/*  182 */     paramLazyActionMap.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
/*      */ 
/*  184 */     paramLazyActionMap.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
/*      */   }
/*      */ 
/*      */   protected void paintCell(Graphics paramGraphics, int paramInt1, Rectangle paramRectangle, ListCellRenderer paramListCellRenderer, ListModel paramListModel, ListSelectionModel paramListSelectionModel, int paramInt2)
/*      */   {
/*  204 */     Object localObject = paramListModel.getElementAt(paramInt1);
/*  205 */     boolean bool1 = (this.list.hasFocus()) && (paramInt1 == paramInt2);
/*  206 */     boolean bool2 = paramListSelectionModel.isSelectedIndex(paramInt1);
/*      */ 
/*  208 */     Component localComponent = paramListCellRenderer.getListCellRendererComponent(this.list, localObject, paramInt1, bool2, bool1);
/*      */ 
/*  211 */     int i = paramRectangle.x;
/*  212 */     int j = paramRectangle.y;
/*  213 */     int k = paramRectangle.width;
/*  214 */     int m = paramRectangle.height;
/*      */ 
/*  216 */     if (this.isFileList)
/*      */     {
/*  220 */       int n = Math.min(k, localComponent.getPreferredSize().width + 4);
/*  221 */       if (!this.isLeftToRight) {
/*  222 */         i += k - n;
/*      */       }
/*  224 */       k = n;
/*      */     }
/*      */ 
/*  227 */     this.rendererPane.paintComponent(paramGraphics, localComponent, this.list, i, j, k, m, true);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  239 */     Shape localShape = paramGraphics.getClip();
/*  240 */     paintImpl(paramGraphics, paramJComponent);
/*  241 */     paramGraphics.setClip(localShape);
/*      */ 
/*  243 */     paintDropLine(paramGraphics);
/*      */   }
/*      */ 
/*      */   private void paintImpl(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  248 */     switch (this.layoutOrientation) {
/*      */     case 1:
/*  250 */       if (this.list.getHeight() != this.listHeight) {
/*  251 */         this.updateLayoutStateNeeded |= 256;
/*  252 */         redrawList(); } break;
/*      */     case 2:
/*  256 */       if (this.list.getWidth() != this.listWidth) {
/*  257 */         this.updateLayoutStateNeeded |= 512;
/*  258 */         redrawList(); } break;
/*      */     }
/*      */ 
/*  264 */     maybeUpdateLayoutState();
/*      */ 
/*  266 */     ListCellRenderer localListCellRenderer = this.list.getCellRenderer();
/*  267 */     ListModel localListModel = this.list.getModel();
/*  268 */     ListSelectionModel localListSelectionModel = this.list.getSelectionModel();
/*      */     int i;
/*  271 */     if ((localListCellRenderer == null) || ((i = localListModel.getSize()) == 0)) {
/*  272 */       return;
/*      */     }
/*      */ 
/*  276 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/*      */     int j;
/*      */     int k;
/*  279 */     if (paramJComponent.getComponentOrientation().isLeftToRight()) {
/*  280 */       j = convertLocationToColumn(localRectangle1.x, localRectangle1.y);
/*      */ 
/*  282 */       k = convertLocationToColumn(localRectangle1.x + localRectangle1.width, localRectangle1.y);
/*      */     }
/*      */     else
/*      */     {
/*  286 */       j = convertLocationToColumn(localRectangle1.x + localRectangle1.width, localRectangle1.y);
/*      */ 
/*  289 */       k = convertLocationToColumn(localRectangle1.x, localRectangle1.y);
/*      */     }
/*      */ 
/*  292 */     int m = localRectangle1.y + localRectangle1.height;
/*  293 */     int n = adjustIndex(this.list.getLeadSelectionIndex(), this.list);
/*  294 */     int i1 = this.layoutOrientation == 2 ? this.columnCount : 1;
/*      */ 
/*  298 */     for (int i2 = j; i2 <= k; 
/*  299 */       i2++)
/*      */     {
/*  301 */       int i3 = convertLocationToRowInColumn(localRectangle1.y, i2);
/*  302 */       int i4 = getRowCount(i2);
/*  303 */       int i5 = getModelIndex(i2, i3);
/*  304 */       Rectangle localRectangle2 = getCellBounds(this.list, i5, i5);
/*      */ 
/*  306 */       if (localRectangle2 == null)
/*      */       {
/*  308 */         return;
/*      */       }
/*  310 */       while ((i3 < i4) && (localRectangle2.y < m) && (i5 < i))
/*      */       {
/*  312 */         localRectangle2.height = getHeight(i2, i3);
/*  313 */         paramGraphics.setClip(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
/*      */ 
/*  315 */         paramGraphics.clipRect(localRectangle1.x, localRectangle1.y, localRectangle1.width, localRectangle1.height);
/*      */ 
/*  317 */         paintCell(paramGraphics, i5, localRectangle2, localListCellRenderer, localListModel, localListSelectionModel, n);
/*      */ 
/*  319 */         localRectangle2.y += localRectangle2.height;
/*  320 */         i5 += i1;
/*  321 */         i3++;
/*      */       }
/*      */     }
/*      */ 
/*  325 */     this.rendererPane.removeAll();
/*      */   }
/*      */ 
/*      */   private void paintDropLine(Graphics paramGraphics) {
/*  329 */     JList.DropLocation localDropLocation = this.list.getDropLocation();
/*  330 */     if ((localDropLocation == null) || (!localDropLocation.isInsert())) {
/*  331 */       return;
/*      */     }
/*      */ 
/*  334 */     Color localColor = DefaultLookup.getColor(this.list, this, "List.dropLineColor", null);
/*  335 */     if (localColor != null) {
/*  336 */       paramGraphics.setColor(localColor);
/*  337 */       Rectangle localRectangle = getDropLineRect(localDropLocation);
/*  338 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Rectangle getDropLineRect(JList.DropLocation paramDropLocation) {
/*  343 */     int i = this.list.getModel().getSize();
/*      */ 
/*  345 */     if (i == 0) {
/*  346 */       localObject = this.list.getInsets();
/*  347 */       if (this.layoutOrientation == 2) {
/*  348 */         if (this.isLeftToRight) {
/*  349 */           return new Rectangle(((Insets)localObject).left, ((Insets)localObject).top, 2, 20);
/*      */         }
/*  351 */         return new Rectangle(this.list.getWidth() - 2 - ((Insets)localObject).right, ((Insets)localObject).top, 2, 20);
/*      */       }
/*      */ 
/*  355 */       return new Rectangle(((Insets)localObject).left, ((Insets)localObject).top, this.list.getWidth() - ((Insets)localObject).left - ((Insets)localObject).right, 2);
/*      */     }
/*      */ 
/*  361 */     Object localObject = null;
/*  362 */     int j = paramDropLocation.getIndex();
/*  363 */     int k = 0;
/*      */     Rectangle localRectangle1;
/*      */     Rectangle localRectangle2;
/*      */     Point localPoint;
/*  365 */     if (this.layoutOrientation == 2) {
/*  366 */       if (j == i) {
/*  367 */         k = 1;
/*  368 */       } else if ((j != 0) && (convertModelToRow(j) != convertModelToRow(j - 1)))
/*      */       {
/*  371 */         localRectangle1 = getCellBounds(this.list, j - 1);
/*  372 */         localRectangle2 = getCellBounds(this.list, j);
/*  373 */         localPoint = paramDropLocation.getDropPoint();
/*      */ 
/*  375 */         if (this.isLeftToRight) {
/*  376 */           k = Point2D.distance(localRectangle1.x + localRectangle1.width, localRectangle1.y + (int)(localRectangle1.height / 2.0D), localPoint.x, localPoint.y) < Point2D.distance(localRectangle2.x, localRectangle2.y + (int)(localRectangle2.height / 2.0D), localPoint.x, localPoint.y) ? 1 : 0;
/*      */         }
/*      */         else
/*      */         {
/*  383 */           k = Point2D.distance(localRectangle1.x, localRectangle1.y + (int)(localRectangle1.height / 2.0D), localPoint.x, localPoint.y) < Point2D.distance(localRectangle2.x + localRectangle2.width, localRectangle2.y + (int)(localRectangle1.height / 2.0D), localPoint.x, localPoint.y) ? 1 : 0;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  392 */       if (k != 0) {
/*  393 */         j--;
/*  394 */         localObject = getCellBounds(this.list, j);
/*  395 */         if (this.isLeftToRight)
/*  396 */           localObject.x += ((Rectangle)localObject).width;
/*      */         else
/*  398 */           localObject.x -= 2;
/*      */       }
/*      */       else {
/*  401 */         localObject = getCellBounds(this.list, j);
/*  402 */         if (!this.isLeftToRight) {
/*  403 */           localObject.x += ((Rectangle)localObject).width - 2;
/*      */         }
/*      */       }
/*      */ 
/*  407 */       if (((Rectangle)localObject).x >= this.list.getWidth())
/*  408 */         ((Rectangle)localObject).x = (this.list.getWidth() - 2);
/*  409 */       else if (((Rectangle)localObject).x < 0) {
/*  410 */         ((Rectangle)localObject).x = 0;
/*      */       }
/*      */ 
/*  413 */       ((Rectangle)localObject).width = 2;
/*  414 */     } else if (this.layoutOrientation == 1) {
/*  415 */       if (j == i) {
/*  416 */         j--;
/*  417 */         localObject = getCellBounds(this.list, j);
/*  418 */         localObject.y += ((Rectangle)localObject).height;
/*  419 */       } else if ((j != 0) && (convertModelToColumn(j) != convertModelToColumn(j - 1)))
/*      */       {
/*  422 */         localRectangle1 = getCellBounds(this.list, j - 1);
/*  423 */         localRectangle2 = getCellBounds(this.list, j);
/*  424 */         localPoint = paramDropLocation.getDropPoint();
/*  425 */         if (Point2D.distance(localRectangle1.x + (int)(localRectangle1.width / 2.0D), localRectangle1.y + localRectangle1.height, localPoint.x, localPoint.y) < Point2D.distance(localRectangle2.x + (int)(localRectangle2.width / 2.0D), localRectangle2.y, localPoint.x, localPoint.y))
/*      */         {
/*  432 */           j--;
/*  433 */           localObject = getCellBounds(this.list, j);
/*  434 */           localObject.y += ((Rectangle)localObject).height;
/*      */         } else {
/*  436 */           localObject = getCellBounds(this.list, j);
/*      */         }
/*      */       } else {
/*  439 */         localObject = getCellBounds(this.list, j);
/*      */       }
/*      */ 
/*  442 */       if (((Rectangle)localObject).y >= this.list.getHeight()) {
/*  443 */         ((Rectangle)localObject).y = (this.list.getHeight() - 2);
/*      */       }
/*      */ 
/*  446 */       ((Rectangle)localObject).height = 2;
/*      */     } else {
/*  448 */       if (j == i) {
/*  449 */         j--;
/*  450 */         localObject = getCellBounds(this.list, j);
/*  451 */         localObject.y += ((Rectangle)localObject).height;
/*      */       } else {
/*  453 */         localObject = getCellBounds(this.list, j);
/*      */       }
/*      */ 
/*  456 */       if (((Rectangle)localObject).y >= this.list.getHeight()) {
/*  457 */         ((Rectangle)localObject).y = (this.list.getHeight() - 2);
/*      */       }
/*      */ 
/*  460 */       ((Rectangle)localObject).height = 2;
/*      */     }
/*      */ 
/*  463 */     return localObject;
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  475 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  476 */     int i = this.list.getFixedCellHeight();
/*  477 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  478 */     Component localComponent = (Component)localUIDefaults.get(BASELINE_COMPONENT_KEY);
/*      */ 
/*  480 */     if (localComponent == null) {
/*  481 */       Object localObject = (ListCellRenderer)UIManager.get("List.cellRenderer");
/*      */ 
/*  486 */       if (localObject == null) {
/*  487 */         localObject = new DefaultListCellRenderer();
/*      */       }
/*  489 */       localComponent = ((ListCellRenderer)localObject).getListCellRendererComponent(this.list, "a", -1, false, false);
/*      */ 
/*  491 */       localUIDefaults.put(BASELINE_COMPONENT_KEY, localComponent);
/*      */     }
/*  493 */     localComponent.setFont(this.list.getFont());
/*      */ 
/*  501 */     if (i == -1) {
/*  502 */       i = localComponent.getPreferredSize().height;
/*      */     }
/*  504 */     return localComponent.getBaseline(2147483647, i) + this.list.getInsets().top;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  518 */     super.getBaselineResizeBehavior(paramJComponent);
/*  519 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  578 */     maybeUpdateLayoutState();
/*      */ 
/*  580 */     int i = this.list.getModel().getSize() - 1;
/*  581 */     if (i < 0) {
/*  582 */       return new Dimension(0, 0);
/*      */     }
/*      */ 
/*  585 */     Insets localInsets = this.list.getInsets();
/*  586 */     int j = this.cellWidth * this.columnCount + localInsets.left + localInsets.right;
/*      */     int k;
/*  589 */     if (this.layoutOrientation != 0) {
/*  590 */       k = this.preferredHeight;
/*      */     }
/*      */     else {
/*  593 */       Rectangle localRectangle = getCellBounds(this.list, i);
/*      */ 
/*  595 */       if (localRectangle != null) {
/*  596 */         k = localRectangle.y + localRectangle.height + localInsets.bottom;
/*      */       }
/*      */       else {
/*  599 */         k = 0;
/*      */       }
/*      */     }
/*  602 */     return new Dimension(j, k);
/*      */   }
/*      */ 
/*      */   protected void selectPreviousIndex()
/*      */   {
/*  612 */     int i = this.list.getSelectedIndex();
/*  613 */     if (i > 0) {
/*  614 */       i--;
/*  615 */       this.list.setSelectedIndex(i);
/*  616 */       this.list.ensureIndexIsVisible(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void selectNextIndex()
/*      */   {
/*  628 */     int i = this.list.getSelectedIndex();
/*  629 */     if (i + 1 < this.list.getModel().getSize()) {
/*  630 */       i++;
/*  631 */       this.list.setSelectedIndex(i);
/*  632 */       this.list.ensureIndexIsVisible(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  645 */     InputMap localInputMap = getInputMap(0);
/*      */ 
/*  647 */     SwingUtilities.replaceUIInputMap(this.list, 0, localInputMap);
/*      */ 
/*  650 */     LazyActionMap.installLazyActionMap(this.list, BasicListUI.class, "List.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/*  655 */     if (paramInt == 0) {
/*  656 */       InputMap localInputMap1 = (InputMap)DefaultLookup.get(this.list, this, "List.focusInputMap");
/*      */       InputMap localInputMap2;
/*  660 */       if ((this.isLeftToRight) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.list, this, "List.focusInputMap.RightToLeft")) == null))
/*      */       {
/*  663 */         return localInputMap1;
/*      */       }
/*  665 */       localInputMap2.setParent(localInputMap1);
/*  666 */       return localInputMap2;
/*      */     }
/*      */ 
/*  669 */     return null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  682 */     SwingUtilities.replaceUIActionMap(this.list, null);
/*  683 */     SwingUtilities.replaceUIInputMap(this.list, 0, null);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  696 */     TransferHandler localTransferHandler = this.list.getTransferHandler();
/*  697 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource))) {
/*  698 */       this.list.setTransferHandler(defaultTransferHandler);
/*      */ 
/*  701 */       if ((this.list.getDropTarget() instanceof UIResource)) {
/*  702 */         this.list.setDropTarget(null);
/*      */       }
/*      */     }
/*      */ 
/*  706 */     this.focusListener = createFocusListener();
/*  707 */     this.mouseInputListener = createMouseInputListener();
/*  708 */     this.propertyChangeListener = createPropertyChangeListener();
/*  709 */     this.listSelectionListener = createListSelectionListener();
/*  710 */     this.listDataListener = createListDataListener();
/*      */ 
/*  712 */     this.list.addFocusListener(this.focusListener);
/*  713 */     this.list.addMouseListener(this.mouseInputListener);
/*  714 */     this.list.addMouseMotionListener(this.mouseInputListener);
/*  715 */     this.list.addPropertyChangeListener(this.propertyChangeListener);
/*  716 */     this.list.addKeyListener(getHandler());
/*      */ 
/*  718 */     ListModel localListModel = this.list.getModel();
/*  719 */     if (localListModel != null) {
/*  720 */       localListModel.addListDataListener(this.listDataListener);
/*      */     }
/*      */ 
/*  723 */     ListSelectionModel localListSelectionModel = this.list.getSelectionModel();
/*  724 */     if (localListSelectionModel != null)
/*  725 */       localListSelectionModel.addListSelectionListener(this.listSelectionListener);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  741 */     this.list.removeFocusListener(this.focusListener);
/*  742 */     this.list.removeMouseListener(this.mouseInputListener);
/*  743 */     this.list.removeMouseMotionListener(this.mouseInputListener);
/*  744 */     this.list.removePropertyChangeListener(this.propertyChangeListener);
/*  745 */     this.list.removeKeyListener(getHandler());
/*      */ 
/*  747 */     ListModel localListModel = this.list.getModel();
/*  748 */     if (localListModel != null) {
/*  749 */       localListModel.removeListDataListener(this.listDataListener);
/*      */     }
/*      */ 
/*  752 */     ListSelectionModel localListSelectionModel = this.list.getSelectionModel();
/*  753 */     if (localListSelectionModel != null) {
/*  754 */       localListSelectionModel.removeListSelectionListener(this.listSelectionListener);
/*      */     }
/*      */ 
/*  757 */     this.focusListener = null;
/*  758 */     this.mouseInputListener = null;
/*  759 */     this.listSelectionListener = null;
/*  760 */     this.listDataListener = null;
/*  761 */     this.propertyChangeListener = null;
/*  762 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  779 */     this.list.setLayout(null);
/*      */ 
/*  781 */     LookAndFeel.installBorder(this.list, "List.border");
/*      */ 
/*  783 */     LookAndFeel.installColorsAndFont(this.list, "List.background", "List.foreground", "List.font");
/*      */ 
/*  785 */     LookAndFeel.installProperty(this.list, "opaque", Boolean.TRUE);
/*      */ 
/*  787 */     if (this.list.getCellRenderer() == null) {
/*  788 */       this.list.setCellRenderer((ListCellRenderer)UIManager.get("List.cellRenderer"));
/*      */     }
/*      */ 
/*  791 */     Color localColor1 = this.list.getSelectionBackground();
/*  792 */     if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/*  793 */       this.list.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
/*      */     }
/*      */ 
/*  796 */     Color localColor2 = this.list.getSelectionForeground();
/*  797 */     if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/*  798 */       this.list.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
/*      */     }
/*      */ 
/*  801 */     Long localLong = (Long)UIManager.get("List.timeFactor");
/*  802 */     this.timeFactor = (localLong != null ? localLong.longValue() : 1000L);
/*      */ 
/*  804 */     updateIsFileList();
/*      */   }
/*      */ 
/*      */   private void updateIsFileList() {
/*  808 */     boolean bool = Boolean.TRUE.equals(this.list.getClientProperty("List.isFileList"));
/*  809 */     if (bool != this.isFileList) {
/*  810 */       this.isFileList = bool;
/*  811 */       Font localFont1 = this.list.getFont();
/*  812 */       if ((localFont1 == null) || ((localFont1 instanceof UIResource))) {
/*  813 */         Font localFont2 = UIManager.getFont(bool ? "FileChooser.listFont" : "List.font");
/*  814 */         if ((localFont2 != null) && (localFont2 != localFont1))
/*  815 */           this.list.setFont(localFont2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  833 */     LookAndFeel.uninstallBorder(this.list);
/*  834 */     if ((this.list.getFont() instanceof UIResource)) {
/*  835 */       this.list.setFont(null);
/*      */     }
/*  837 */     if ((this.list.getForeground() instanceof UIResource)) {
/*  838 */       this.list.setForeground(null);
/*      */     }
/*  840 */     if ((this.list.getBackground() instanceof UIResource)) {
/*  841 */       this.list.setBackground(null);
/*      */     }
/*  843 */     if ((this.list.getSelectionBackground() instanceof UIResource)) {
/*  844 */       this.list.setSelectionBackground(null);
/*      */     }
/*  846 */     if ((this.list.getSelectionForeground() instanceof UIResource)) {
/*  847 */       this.list.setSelectionForeground(null);
/*      */     }
/*  849 */     if ((this.list.getCellRenderer() instanceof UIResource)) {
/*  850 */       this.list.setCellRenderer(null);
/*      */     }
/*  852 */     if ((this.list.getTransferHandler() instanceof UIResource))
/*  853 */       this.list.setTransferHandler(null);
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  869 */     this.list = ((JList)paramJComponent);
/*      */ 
/*  871 */     this.layoutOrientation = this.list.getLayoutOrientation();
/*      */ 
/*  873 */     this.rendererPane = new CellRendererPane();
/*  874 */     this.list.add(this.rendererPane);
/*      */ 
/*  876 */     this.columnCount = 1;
/*      */ 
/*  878 */     this.updateLayoutStateNeeded = 1;
/*  879 */     this.isLeftToRight = this.list.getComponentOrientation().isLeftToRight();
/*      */ 
/*  881 */     installDefaults();
/*  882 */     installListeners();
/*  883 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  898 */     uninstallListeners();
/*  899 */     uninstallDefaults();
/*  900 */     uninstallKeyboardActions();
/*      */ 
/*  902 */     this.cellWidth = (this.cellHeight = -1);
/*  903 */     this.cellHeights = null;
/*      */ 
/*  905 */     this.listWidth = (this.listHeight = -1);
/*      */ 
/*  907 */     this.list.remove(this.rendererPane);
/*  908 */     this.rendererPane = null;
/*  909 */     this.list = null;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  920 */     return new BasicListUI();
/*      */   }
/*      */ 
/*      */   public int locationToIndex(JList paramJList, Point paramPoint)
/*      */   {
/*  929 */     maybeUpdateLayoutState();
/*  930 */     return convertLocationToModel(paramPoint.x, paramPoint.y);
/*      */   }
/*      */ 
/*      */   public Point indexToLocation(JList paramJList, int paramInt)
/*      */   {
/*  938 */     maybeUpdateLayoutState();
/*  939 */     Rectangle localRectangle = getCellBounds(paramJList, paramInt, paramInt);
/*      */ 
/*  941 */     if (localRectangle != null) {
/*  942 */       return new Point(localRectangle.x, localRectangle.y);
/*      */     }
/*  944 */     return null;
/*      */   }
/*      */ 
/*      */   public Rectangle getCellBounds(JList paramJList, int paramInt1, int paramInt2)
/*      */   {
/*  952 */     maybeUpdateLayoutState();
/*      */ 
/*  954 */     int i = Math.min(paramInt1, paramInt2);
/*  955 */     int j = Math.max(paramInt1, paramInt2);
/*      */ 
/*  957 */     if (i >= paramJList.getModel().getSize()) {
/*  958 */       return null;
/*      */     }
/*      */ 
/*  961 */     Rectangle localRectangle1 = getCellBounds(paramJList, i);
/*      */ 
/*  963 */     if (localRectangle1 == null) {
/*  964 */       return null;
/*      */     }
/*  966 */     if (i == j) {
/*  967 */       return localRectangle1;
/*      */     }
/*  969 */     Rectangle localRectangle2 = getCellBounds(paramJList, j);
/*      */ 
/*  971 */     if (localRectangle2 != null) {
/*  972 */       if (this.layoutOrientation == 2) {
/*  973 */         int k = convertModelToRow(i);
/*  974 */         int m = convertModelToRow(j);
/*      */ 
/*  976 */         if (k != m) {
/*  977 */           localRectangle1.x = 0;
/*  978 */           localRectangle1.width = paramJList.getWidth();
/*      */         }
/*      */       }
/*  981 */       else if (localRectangle1.x != localRectangle2.x)
/*      */       {
/*  983 */         localRectangle1.y = 0;
/*  984 */         localRectangle1.height = paramJList.getHeight();
/*      */       }
/*  986 */       localRectangle1.add(localRectangle2);
/*      */     }
/*  988 */     return localRectangle1;
/*      */   }
/*      */ 
/*      */   private Rectangle getCellBounds(JList paramJList, int paramInt)
/*      */   {
/*  996 */     maybeUpdateLayoutState();
/*      */ 
/*  998 */     int i = convertModelToRow(paramInt);
/*  999 */     int j = convertModelToColumn(paramInt);
/*      */ 
/* 1001 */     if ((i == -1) || (j == -1)) {
/* 1002 */       return null;
/*      */     }
/*      */ 
/* 1005 */     Insets localInsets = paramJList.getInsets();
/*      */ 
/* 1007 */     int m = this.cellWidth;
/* 1008 */     int n = localInsets.top;
/*      */     int k;
/*      */     int i1;
/* 1010 */     switch (this.layoutOrientation) {
/*      */     case 1:
/*      */     case 2:
/* 1013 */       if (this.isLeftToRight)
/* 1014 */         k = localInsets.left + j * this.cellWidth;
/*      */       else {
/* 1016 */         k = paramJList.getWidth() - localInsets.right - (j + 1) * this.cellWidth;
/*      */       }
/* 1018 */       n += this.cellHeight * i;
/* 1019 */       i1 = this.cellHeight;
/* 1020 */       break;
/*      */     default:
/* 1022 */       k = localInsets.left;
/* 1023 */       if (this.cellHeights == null) {
/* 1024 */         n += this.cellHeight * i;
/*      */       }
/* 1026 */       else if (i >= this.cellHeights.length) {
/* 1027 */         n = 0;
/*      */       }
/*      */       else {
/* 1030 */         for (int i2 = 0; i2 < i; i2++) {
/* 1031 */           n += this.cellHeights[i2];
/*      */         }
/*      */       }
/* 1034 */       m = paramJList.getWidth() - (localInsets.left + localInsets.right);
/* 1035 */       i1 = getRowHeight(paramInt);
/*      */     }
/*      */ 
/* 1038 */     return new Rectangle(k, n, m, i1);
/*      */   }
/*      */ 
/*      */   protected int getRowHeight(int paramInt)
/*      */   {
/* 1051 */     return getHeight(0, paramInt);
/*      */   }
/*      */ 
/*      */   protected int convertYToRow(int paramInt)
/*      */   {
/* 1066 */     return convertLocationToRow(0, paramInt, false);
/*      */   }
/*      */ 
/*      */   protected int convertRowToY(int paramInt)
/*      */   {
/* 1080 */     if ((paramInt >= getRowCount(0)) || (paramInt < 0)) {
/* 1081 */       return -1;
/*      */     }
/* 1083 */     Rectangle localRectangle = getCellBounds(this.list, paramInt, paramInt);
/* 1084 */     return localRectangle.y;
/*      */   }
/*      */ 
/*      */   private int getHeight(int paramInt1, int paramInt2)
/*      */   {
/* 1091 */     if ((paramInt1 < 0) || (paramInt1 > this.columnCount) || (paramInt2 < 0)) {
/* 1092 */       return -1;
/*      */     }
/* 1094 */     if (this.layoutOrientation != 0) {
/* 1095 */       return this.cellHeight;
/*      */     }
/* 1097 */     if (paramInt2 >= this.list.getModel().getSize()) {
/* 1098 */       return -1;
/*      */     }
/* 1100 */     return paramInt2 < this.cellHeights.length ? this.cellHeights[paramInt2] : this.cellHeights == null ? this.cellHeight : -1;
/*      */   }
/*      */ 
/*      */   private int convertLocationToRow(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/* 1111 */     int i = this.list.getModel().getSize();
/*      */ 
/* 1113 */     if (i <= 0) {
/* 1114 */       return -1;
/*      */     }
/* 1116 */     Insets localInsets = this.list.getInsets();
/* 1117 */     if (this.cellHeights == null) {
/* 1118 */       j = this.cellHeight == 0 ? 0 : (paramInt2 - localInsets.top) / this.cellHeight;
/*      */ 
/* 1120 */       if (paramBoolean) {
/* 1121 */         if (j < 0) {
/* 1122 */           j = 0;
/*      */         }
/* 1124 */         else if (j >= i) {
/* 1125 */           j = i - 1;
/*      */         }
/*      */       }
/* 1128 */       return j;
/*      */     }
/* 1130 */     if (i > this.cellHeights.length) {
/* 1131 */       return -1;
/*      */     }
/*      */ 
/* 1134 */     int j = localInsets.top;
/* 1135 */     int k = 0;
/*      */ 
/* 1137 */     if ((paramBoolean) && (paramInt2 < j)) {
/* 1138 */       return 0;
/*      */     }
/*      */ 
/* 1141 */     for (int m = 0; m < i; m++) {
/* 1142 */       if ((paramInt2 >= j) && (paramInt2 < j + this.cellHeights[m])) {
/* 1143 */         return k;
/*      */       }
/* 1145 */       j += this.cellHeights[m];
/* 1146 */       k++;
/*      */     }
/* 1148 */     return m - 1;
/*      */   }
/*      */ 
/*      */   private int convertLocationToRowInColumn(int paramInt1, int paramInt2)
/*      */   {
/* 1157 */     int i = 0;
/*      */ 
/* 1159 */     if (this.layoutOrientation != 0) {
/* 1160 */       if (this.isLeftToRight)
/* 1161 */         i = paramInt2 * this.cellWidth;
/*      */       else {
/* 1163 */         i = this.list.getWidth() - (paramInt2 + 1) * this.cellWidth - this.list.getInsets().right;
/*      */       }
/*      */     }
/* 1166 */     return convertLocationToRow(i, paramInt1, true);
/*      */   }
/*      */ 
/*      */   private int convertLocationToModel(int paramInt1, int paramInt2)
/*      */   {
/* 1174 */     int i = convertLocationToRow(paramInt1, paramInt2, true);
/* 1175 */     int j = convertLocationToColumn(paramInt1, paramInt2);
/*      */ 
/* 1177 */     if ((i >= 0) && (j >= 0)) {
/* 1178 */       return getModelIndex(j, i);
/*      */     }
/* 1180 */     return -1;
/*      */   }
/*      */ 
/*      */   private int getRowCount(int paramInt)
/*      */   {
/* 1187 */     if ((paramInt < 0) || (paramInt >= this.columnCount)) {
/* 1188 */       return -1;
/*      */     }
/* 1190 */     if ((this.layoutOrientation == 0) || ((paramInt == 0) && (this.columnCount == 1)))
/*      */     {
/* 1192 */       return this.list.getModel().getSize();
/*      */     }
/* 1194 */     if (paramInt >= this.columnCount) {
/* 1195 */       return -1;
/*      */     }
/* 1197 */     if (this.layoutOrientation == 1) {
/* 1198 */       if (paramInt < this.columnCount - 1) {
/* 1199 */         return this.rowsPerColumn;
/*      */       }
/* 1201 */       return this.list.getModel().getSize() - (this.columnCount - 1) * this.rowsPerColumn;
/*      */     }
/*      */ 
/* 1205 */     int i = this.columnCount - (this.columnCount * this.rowsPerColumn - this.list.getModel().getSize());
/*      */ 
/* 1208 */     if (paramInt >= i) {
/* 1209 */       return Math.max(0, this.rowsPerColumn - 1);
/*      */     }
/* 1211 */     return this.rowsPerColumn;
/*      */   }
/*      */ 
/*      */   private int getModelIndex(int paramInt1, int paramInt2)
/*      */   {
/* 1220 */     switch (this.layoutOrientation) {
/*      */     case 1:
/* 1222 */       return Math.min(this.list.getModel().getSize() - 1, this.rowsPerColumn * paramInt1 + Math.min(paramInt2, this.rowsPerColumn - 1));
/*      */     case 2:
/* 1225 */       return Math.min(this.list.getModel().getSize() - 1, paramInt2 * this.columnCount + paramInt1);
/*      */     }
/*      */ 
/* 1228 */     return paramInt2;
/*      */   }
/*      */ 
/*      */   private int convertLocationToColumn(int paramInt1, int paramInt2)
/*      */   {
/* 1236 */     if (this.cellWidth > 0) {
/* 1237 */       if (this.layoutOrientation == 0) {
/* 1238 */         return 0;
/*      */       }
/* 1240 */       Insets localInsets = this.list.getInsets();
/*      */       int i;
/* 1242 */       if (this.isLeftToRight)
/* 1243 */         i = (paramInt1 - localInsets.left) / this.cellWidth;
/*      */       else {
/* 1245 */         i = (this.list.getWidth() - paramInt1 - localInsets.right - 1) / this.cellWidth;
/*      */       }
/* 1247 */       if (i < 0) {
/* 1248 */         return 0;
/*      */       }
/* 1250 */       if (i >= this.columnCount) {
/* 1251 */         return this.columnCount - 1;
/*      */       }
/* 1253 */       return i;
/*      */     }
/* 1255 */     return 0;
/*      */   }
/*      */ 
/*      */   private int convertModelToRow(int paramInt)
/*      */   {
/* 1263 */     int i = this.list.getModel().getSize();
/*      */ 
/* 1265 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 1266 */       return -1;
/*      */     }
/*      */ 
/* 1269 */     if ((this.layoutOrientation != 0) && (this.columnCount > 1) && (this.rowsPerColumn > 0))
/*      */     {
/* 1271 */       if (this.layoutOrientation == 1) {
/* 1272 */         return paramInt % this.rowsPerColumn;
/*      */       }
/* 1274 */       return paramInt / this.columnCount;
/*      */     }
/* 1276 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private int convertModelToColumn(int paramInt)
/*      */   {
/* 1284 */     int i = this.list.getModel().getSize();
/*      */ 
/* 1286 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 1287 */       return -1;
/*      */     }
/*      */ 
/* 1290 */     if ((this.layoutOrientation != 0) && (this.rowsPerColumn > 0) && (this.columnCount > 1))
/*      */     {
/* 1292 */       if (this.layoutOrientation == 1) {
/* 1293 */         return paramInt / this.rowsPerColumn;
/*      */       }
/* 1295 */       return paramInt % this.columnCount;
/*      */     }
/* 1297 */     return 0;
/*      */   }
/*      */ 
/*      */   protected void maybeUpdateLayoutState()
/*      */   {
/* 1310 */     if (this.updateLayoutStateNeeded != 0) {
/* 1311 */       updateLayoutState();
/* 1312 */       this.updateLayoutStateNeeded = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateLayoutState()
/*      */   {
/* 1331 */     int i = this.list.getFixedCellHeight();
/* 1332 */     int j = this.list.getFixedCellWidth();
/*      */ 
/* 1334 */     this.cellWidth = (j != -1 ? j : -1);
/*      */ 
/* 1336 */     if (i != -1) {
/* 1337 */       this.cellHeight = i;
/* 1338 */       this.cellHeights = null;
/*      */     }
/*      */     else {
/* 1341 */       this.cellHeight = -1;
/* 1342 */       this.cellHeights = new int[this.list.getModel().getSize()];
/*      */     }
/*      */ 
/* 1352 */     if ((j == -1) || (i == -1))
/*      */     {
/* 1354 */       ListModel localListModel = this.list.getModel();
/* 1355 */       int k = localListModel.getSize();
/* 1356 */       ListCellRenderer localListCellRenderer = this.list.getCellRenderer();
/*      */       int m;
/* 1358 */       if (localListCellRenderer != null) {
/* 1359 */         for (m = 0; m < k; m++) {
/* 1360 */           Object localObject = localListModel.getElementAt(m);
/* 1361 */           Component localComponent = localListCellRenderer.getListCellRendererComponent(this.list, localObject, m, false, false);
/* 1362 */           this.rendererPane.add(localComponent);
/* 1363 */           Dimension localDimension = localComponent.getPreferredSize();
/* 1364 */           if (j == -1) {
/* 1365 */             this.cellWidth = Math.max(localDimension.width, this.cellWidth);
/*      */           }
/* 1367 */           if (i == -1)
/* 1368 */             this.cellHeights[m] = localDimension.height;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1373 */         if (this.cellWidth == -1) {
/* 1374 */           this.cellWidth = 0;
/*      */         }
/* 1376 */         if (this.cellHeights == null) {
/* 1377 */           this.cellHeights = new int[k];
/*      */         }
/* 1379 */         for (m = 0; m < k; m++) {
/* 1380 */           this.cellHeights[m] = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1385 */     this.columnCount = 1;
/* 1386 */     if (this.layoutOrientation != 0)
/* 1387 */       updateHorizontalLayoutState(j, i);
/*      */   }
/*      */ 
/*      */   private void updateHorizontalLayoutState(int paramInt1, int paramInt2)
/*      */   {
/* 1401 */     int i = this.list.getVisibleRowCount();
/* 1402 */     int j = this.list.getModel().getSize();
/* 1403 */     Insets localInsets = this.list.getInsets();
/*      */ 
/* 1405 */     this.listHeight = this.list.getHeight();
/* 1406 */     this.listWidth = this.list.getWidth();
/*      */ 
/* 1408 */     if (j == 0) {
/* 1409 */       this.rowsPerColumn = (this.columnCount = 0);
/* 1410 */       this.preferredHeight = (localInsets.top + localInsets.bottom);
/*      */       return;
/*      */     }
/*      */     int k;
/* 1416 */     if (paramInt2 != -1) {
/* 1417 */       k = paramInt2;
/*      */     }
/*      */     else
/*      */     {
/* 1421 */       int m = 0;
/* 1422 */       if (this.cellHeights.length > 0) {
/* 1423 */         m = this.cellHeights[(this.cellHeights.length - 1)];
/* 1424 */         for (int n = this.cellHeights.length - 2; 
/* 1425 */           n >= 0; n--) {
/* 1426 */           m = Math.max(m, this.cellHeights[n]);
/*      */         }
/*      */       }
/* 1429 */       k = this.cellHeight = m;
/* 1430 */       this.cellHeights = null;
/*      */     }
/*      */ 
/* 1434 */     this.rowsPerColumn = j;
/* 1435 */     if (i > 0) {
/* 1436 */       this.rowsPerColumn = i;
/* 1437 */       this.columnCount = Math.max(1, j / this.rowsPerColumn);
/* 1438 */       if ((j > 0) && (j > this.rowsPerColumn) && (j % this.rowsPerColumn != 0))
/*      */       {
/* 1440 */         this.columnCount += 1;
/*      */       }
/* 1442 */       if (this.layoutOrientation == 2)
/*      */       {
/* 1445 */         this.rowsPerColumn = (j / this.columnCount);
/* 1446 */         if (j % this.columnCount > 0) {
/* 1447 */           this.rowsPerColumn += 1;
/*      */         }
/*      */       }
/*      */     }
/* 1451 */     else if ((this.layoutOrientation == 1) && (k != 0)) {
/* 1452 */       this.rowsPerColumn = Math.max(1, (this.listHeight - localInsets.top - localInsets.bottom) / k);
/*      */ 
/* 1454 */       this.columnCount = Math.max(1, j / this.rowsPerColumn);
/* 1455 */       if ((j > 0) && (j > this.rowsPerColumn) && (j % this.rowsPerColumn != 0))
/*      */       {
/* 1457 */         this.columnCount += 1;
/*      */       }
/*      */     }
/* 1460 */     else if ((this.layoutOrientation == 2) && (this.cellWidth > 0) && (this.listWidth > 0))
/*      */     {
/* 1462 */       this.columnCount = Math.max(1, (this.listWidth - localInsets.left - localInsets.right) / this.cellWidth);
/*      */ 
/* 1464 */       this.rowsPerColumn = (j / this.columnCount);
/* 1465 */       if (j % this.columnCount > 0) {
/* 1466 */         this.rowsPerColumn += 1;
/*      */       }
/*      */     }
/* 1469 */     this.preferredHeight = (this.rowsPerColumn * this.cellHeight + localInsets.top + localInsets.bottom);
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/* 1474 */     if (this.handler == null) {
/* 1475 */       this.handler = new Handler(null);
/*      */     }
/* 1477 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected MouseInputListener createMouseInputListener()
/*      */   {
/* 1554 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/* 1582 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ListSelectionListener createListSelectionListener()
/*      */   {
/* 1634 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private void redrawList()
/*      */   {
/* 1639 */     this.list.revalidate();
/* 1640 */     this.list.repaint();
/*      */   }
/*      */ 
/*      */   protected ListDataListener createListDataListener()
/*      */   {
/* 1704 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/* 1761 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private static int adjustIndex(int paramInt, JList paramJList)
/*      */   {
/* 2828 */     return paramInt < paramJList.getModel().getSize() ? paramInt : -1;
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String SELECT_PREVIOUS_COLUMN = "selectPreviousColumn";
/*      */     private static final String SELECT_PREVIOUS_COLUMN_EXTEND = "selectPreviousColumnExtendSelection";
/*      */     private static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
/*      */     private static final String SELECT_NEXT_COLUMN = "selectNextColumn";
/*      */     private static final String SELECT_NEXT_COLUMN_EXTEND = "selectNextColumnExtendSelection";
/*      */     private static final String SELECT_NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
/*      */     private static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
/*      */     private static final String SELECT_PREVIOUS_ROW_EXTEND = "selectPreviousRowExtendSelection";
/*      */     private static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
/*      */     private static final String SELECT_NEXT_ROW = "selectNextRow";
/*      */     private static final String SELECT_NEXT_ROW_EXTEND = "selectNextRowExtendSelection";
/*      */     private static final String SELECT_NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
/*      */     private static final String SELECT_FIRST_ROW = "selectFirstRow";
/*      */     private static final String SELECT_FIRST_ROW_EXTEND = "selectFirstRowExtendSelection";
/*      */     private static final String SELECT_FIRST_ROW_CHANGE_LEAD = "selectFirstRowChangeLead";
/*      */     private static final String SELECT_LAST_ROW = "selectLastRow";
/*      */     private static final String SELECT_LAST_ROW_EXTEND = "selectLastRowExtendSelection";
/*      */     private static final String SELECT_LAST_ROW_CHANGE_LEAD = "selectLastRowChangeLead";
/*      */     private static final String SCROLL_UP = "scrollUp";
/*      */     private static final String SCROLL_UP_EXTEND = "scrollUpExtendSelection";
/*      */     private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
/*      */     private static final String SCROLL_DOWN = "scrollDown";
/*      */     private static final String SCROLL_DOWN_EXTEND = "scrollDownExtendSelection";
/*      */     private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
/*      */     private static final String SELECT_ALL = "selectAll";
/*      */     private static final String CLEAR_SELECTION = "clearSelection";
/*      */     private static final String ADD_TO_SELECTION = "addToSelection";
/*      */     private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
/*      */     private static final String EXTEND_TO = "extendTo";
/*      */     private static final String MOVE_SELECTION_TO = "moveSelectionTo";
/*      */ 
/*      */     Actions(String paramString)
/*      */     {
/* 1833 */       super();
/*      */     }
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1836 */       String str = getName();
/* 1837 */       JList localJList = (JList)paramActionEvent.getSource();
/* 1838 */       BasicListUI localBasicListUI = (BasicListUI)BasicLookAndFeel.getUIOfType(localJList.getUI(), BasicListUI.class);
/*      */ 
/* 1841 */       if (str == "selectPreviousColumn") {
/* 1842 */         changeSelection(localJList, 1, getNextColumnIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1845 */       else if (str == "selectPreviousColumnExtendSelection") {
/* 1846 */         changeSelection(localJList, 2, getNextColumnIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1849 */       else if (str == "selectPreviousColumnChangeLead") {
/* 1850 */         changeSelection(localJList, 0, getNextColumnIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1853 */       else if (str == "selectNextColumn") {
/* 1854 */         changeSelection(localJList, 1, getNextColumnIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1857 */       else if (str == "selectNextColumnExtendSelection") {
/* 1858 */         changeSelection(localJList, 2, getNextColumnIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1861 */       else if (str == "selectNextColumnChangeLead") {
/* 1862 */         changeSelection(localJList, 0, getNextColumnIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1865 */       else if (str == "selectPreviousRow") {
/* 1866 */         changeSelection(localJList, 1, getNextIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1869 */       else if (str == "selectPreviousRowExtendSelection") {
/* 1870 */         changeSelection(localJList, 2, getNextIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1873 */       else if (str == "selectPreviousRowChangeLead") {
/* 1874 */         changeSelection(localJList, 0, getNextIndex(localJList, localBasicListUI, -1), -1);
/*      */       }
/* 1877 */       else if (str == "selectNextRow") {
/* 1878 */         changeSelection(localJList, 1, getNextIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1881 */       else if (str == "selectNextRowExtendSelection") {
/* 1882 */         changeSelection(localJList, 2, getNextIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1885 */       else if (str == "selectNextRowChangeLead") {
/* 1886 */         changeSelection(localJList, 0, getNextIndex(localJList, localBasicListUI, 1), 1);
/*      */       }
/* 1889 */       else if (str == "selectFirstRow") {
/* 1890 */         changeSelection(localJList, 1, 0, -1);
/*      */       }
/* 1892 */       else if (str == "selectFirstRowExtendSelection") {
/* 1893 */         changeSelection(localJList, 2, 0, -1);
/*      */       }
/* 1895 */       else if (str == "selectFirstRowChangeLead") {
/* 1896 */         changeSelection(localJList, 0, 0, -1);
/*      */       }
/* 1898 */       else if (str == "selectLastRow") {
/* 1899 */         changeSelection(localJList, 1, localJList.getModel().getSize() - 1, 1);
/*      */       }
/* 1902 */       else if (str == "selectLastRowExtendSelection") {
/* 1903 */         changeSelection(localJList, 2, localJList.getModel().getSize() - 1, 1);
/*      */       }
/* 1906 */       else if (str == "selectLastRowChangeLead") {
/* 1907 */         changeSelection(localJList, 0, localJList.getModel().getSize() - 1, 1);
/*      */       }
/* 1910 */       else if (str == "scrollUp") {
/* 1911 */         changeSelection(localJList, 1, getNextPageIndex(localJList, -1), -1);
/*      */       }
/* 1914 */       else if (str == "scrollUpExtendSelection") {
/* 1915 */         changeSelection(localJList, 2, getNextPageIndex(localJList, -1), -1);
/*      */       }
/* 1918 */       else if (str == "scrollUpChangeLead") {
/* 1919 */         changeSelection(localJList, 0, getNextPageIndex(localJList, -1), -1);
/*      */       }
/* 1922 */       else if (str == "scrollDown") {
/* 1923 */         changeSelection(localJList, 1, getNextPageIndex(localJList, 1), 1);
/*      */       }
/* 1926 */       else if (str == "scrollDownExtendSelection") {
/* 1927 */         changeSelection(localJList, 2, getNextPageIndex(localJList, 1), 1);
/*      */       }
/* 1930 */       else if (str == "scrollDownChangeLead") {
/* 1931 */         changeSelection(localJList, 0, getNextPageIndex(localJList, 1), 1);
/*      */       }
/* 1934 */       else if (str == "selectAll") {
/* 1935 */         selectAll(localJList);
/*      */       }
/* 1937 */       else if (str == "clearSelection") {
/* 1938 */         clearSelection(localJList);
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/* 1940 */         if (str == "addToSelection") {
/* 1941 */           i = BasicListUI.adjustIndex(localJList.getSelectionModel().getLeadSelectionIndex(), localJList);
/*      */ 
/* 1944 */           if (!localJList.isSelectedIndex(i)) {
/* 1945 */             int j = localJList.getSelectionModel().getAnchorSelectionIndex();
/* 1946 */             localJList.setValueIsAdjusting(true);
/* 1947 */             localJList.addSelectionInterval(i, i);
/* 1948 */             localJList.getSelectionModel().setAnchorSelectionIndex(j);
/* 1949 */             localJList.setValueIsAdjusting(false);
/*      */           }
/*      */         }
/* 1952 */         else if (str == "toggleAndAnchor") {
/* 1953 */           i = BasicListUI.adjustIndex(localJList.getSelectionModel().getLeadSelectionIndex(), localJList);
/*      */ 
/* 1956 */           if (localJList.isSelectedIndex(i))
/* 1957 */             localJList.removeSelectionInterval(i, i);
/*      */           else {
/* 1959 */             localJList.addSelectionInterval(i, i);
/*      */           }
/*      */         }
/* 1962 */         else if (str == "extendTo") {
/* 1963 */           changeSelection(localJList, 2, BasicListUI.adjustIndex(localJList.getSelectionModel().getLeadSelectionIndex(), localJList), 0);
/*      */         }
/* 1968 */         else if (str == "moveSelectionTo") {
/* 1969 */           changeSelection(localJList, 1, BasicListUI.adjustIndex(localJList.getSelectionModel().getLeadSelectionIndex(), localJList), 0);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isEnabled(Object paramObject)
/*      */     {
/* 1977 */       String str = getName();
/* 1978 */       if ((str == "selectPreviousColumnChangeLead") || (str == "selectNextColumnChangeLead") || (str == "selectPreviousRowChangeLead") || (str == "selectNextRowChangeLead") || (str == "selectFirstRowChangeLead") || (str == "selectLastRowChangeLead") || (str == "scrollUpChangeLead") || (str == "scrollDownChangeLead"))
/*      */       {
/* 1989 */         return (paramObject != null) && ((((JList)paramObject).getSelectionModel() instanceof DefaultListSelectionModel));
/*      */       }
/*      */ 
/* 1993 */       return true;
/*      */     }
/*      */ 
/*      */     private void clearSelection(JList paramJList) {
/* 1997 */       paramJList.clearSelection();
/*      */     }
/*      */ 
/*      */     private void selectAll(JList paramJList) {
/* 2001 */       int i = paramJList.getModel().getSize();
/* 2002 */       if (i > 0) {
/* 2003 */         ListSelectionModel localListSelectionModel = paramJList.getSelectionModel();
/* 2004 */         int j = BasicListUI.adjustIndex(localListSelectionModel.getLeadSelectionIndex(), paramJList);
/*      */         int k;
/* 2006 */         if (localListSelectionModel.getSelectionMode() == 0) {
/* 2007 */           if (j == -1) {
/* 2008 */             k = BasicListUI.adjustIndex(paramJList.getMinSelectionIndex(), paramJList);
/* 2009 */             j = k == -1 ? 0 : k;
/*      */           }
/*      */ 
/* 2012 */           paramJList.setSelectionInterval(j, j);
/* 2013 */           paramJList.ensureIndexIsVisible(j);
/*      */         } else {
/* 2015 */           paramJList.setValueIsAdjusting(true);
/*      */ 
/* 2017 */           k = BasicListUI.adjustIndex(localListSelectionModel.getAnchorSelectionIndex(), paramJList);
/*      */ 
/* 2019 */           paramJList.setSelectionInterval(0, i - 1);
/*      */ 
/* 2022 */           SwingUtilities2.setLeadAnchorWithoutSelection(localListSelectionModel, k, j);
/*      */ 
/* 2024 */           paramJList.setValueIsAdjusting(false);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getNextPageIndex(JList paramJList, int paramInt) {
/* 2030 */       if (paramJList.getModel().getSize() == 0) {
/* 2031 */         return -1;
/*      */       }
/*      */ 
/* 2034 */       int i = -1;
/* 2035 */       Rectangle localRectangle1 = paramJList.getVisibleRect();
/* 2036 */       ListSelectionModel localListSelectionModel = paramJList.getSelectionModel();
/* 2037 */       int j = BasicListUI.adjustIndex(localListSelectionModel.getLeadSelectionIndex(), paramJList);
/* 2038 */       Rectangle localRectangle2 = j == -1 ? new Rectangle() : paramJList.getCellBounds(j, j);
/*      */       Point localPoint;
/*      */       Rectangle localRectangle3;
/* 2041 */       if ((paramJList.getLayoutOrientation() == 1) && (paramJList.getVisibleRowCount() <= 0))
/*      */       {
/* 2043 */         if (!paramJList.getComponentOrientation().isLeftToRight()) {
/* 2044 */           paramInt = -paramInt;
/*      */         }
/*      */ 
/* 2048 */         if (paramInt < 0)
/*      */         {
/* 2050 */           localRectangle1.x = (localRectangle2.x + localRectangle2.width - localRectangle1.width);
/* 2051 */           localPoint = new Point(localRectangle1.x - 1, localRectangle2.y);
/* 2052 */           i = paramJList.locationToIndex(localPoint);
/* 2053 */           localRectangle3 = paramJList.getCellBounds(i, i);
/* 2054 */           if (localRectangle1.intersects(localRectangle3)) {
/* 2055 */             localPoint.x = (localRectangle3.x - 1);
/* 2056 */             i = paramJList.locationToIndex(localPoint);
/* 2057 */             localRectangle3 = paramJList.getCellBounds(i, i);
/*      */           }
/*      */ 
/* 2060 */           if (localRectangle3.y != localRectangle2.y) {
/* 2061 */             localPoint.x = (localRectangle3.x + localRectangle3.width);
/* 2062 */             i = paramJList.locationToIndex(localPoint);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2067 */           localRectangle1.x = localRectangle2.x;
/* 2068 */           localPoint = new Point(localRectangle1.x + localRectangle1.width, localRectangle2.y);
/* 2069 */           i = paramJList.locationToIndex(localPoint);
/* 2070 */           localRectangle3 = paramJList.getCellBounds(i, i);
/* 2071 */           if (localRectangle1.intersects(localRectangle3)) {
/* 2072 */             localPoint.x = (localRectangle3.x + localRectangle3.width);
/* 2073 */             i = paramJList.locationToIndex(localPoint);
/* 2074 */             localRectangle3 = paramJList.getCellBounds(i, i);
/*      */           }
/* 2076 */           if (localRectangle3.y != localRectangle2.y) {
/* 2077 */             localPoint.x = (localRectangle3.x - 1);
/* 2078 */             i = paramJList.locationToIndex(localPoint);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/* 2083 */       else if (paramInt < 0)
/*      */       {
/* 2086 */         localPoint = new Point(localRectangle2.x, localRectangle1.y);
/* 2087 */         i = paramJList.locationToIndex(localPoint);
/* 2088 */         if (j <= i)
/*      */         {
/* 2091 */           localRectangle1.y = (localRectangle2.y + localRectangle2.height - localRectangle1.height);
/* 2092 */           localPoint.y = localRectangle1.y;
/* 2093 */           i = paramJList.locationToIndex(localPoint);
/* 2094 */           localRectangle3 = paramJList.getCellBounds(i, i);
/*      */ 
/* 2097 */           if (localRectangle3.y < localRectangle1.y) {
/* 2098 */             localPoint.y = (localRectangle3.y + localRectangle3.height);
/* 2099 */             i = paramJList.locationToIndex(localPoint);
/* 2100 */             localRectangle3 = paramJList.getCellBounds(i, i);
/*      */           }
/*      */ 
/* 2104 */           if (localRectangle3.y >= localRectangle2.y) {
/* 2105 */             localPoint.y = (localRectangle2.y - 1);
/* 2106 */             i = paramJList.locationToIndex(localPoint);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2113 */         localPoint = new Point(localRectangle2.x, localRectangle1.y + localRectangle1.height - 1);
/*      */ 
/* 2115 */         i = paramJList.locationToIndex(localPoint);
/* 2116 */         localRectangle3 = paramJList.getCellBounds(i, i);
/*      */ 
/* 2119 */         if (localRectangle3.y + localRectangle3.height > localRectangle1.y + localRectangle1.height)
/*      */         {
/* 2121 */           localPoint.y = (localRectangle3.y - 1);
/* 2122 */           i = paramJList.locationToIndex(localPoint);
/* 2123 */           localRectangle3 = paramJList.getCellBounds(i, i);
/* 2124 */           i = Math.max(i, j);
/*      */         }
/*      */ 
/* 2127 */         if (j >= i)
/*      */         {
/* 2130 */           localRectangle1.y = localRectangle2.y;
/* 2131 */           localPoint.y = (localRectangle1.y + localRectangle1.height - 1);
/* 2132 */           i = paramJList.locationToIndex(localPoint);
/* 2133 */           localRectangle3 = paramJList.getCellBounds(i, i);
/*      */ 
/* 2136 */           if (localRectangle3.y + localRectangle3.height > localRectangle1.y + localRectangle1.height)
/*      */           {
/* 2138 */             localPoint.y = (localRectangle3.y - 1);
/* 2139 */             i = paramJList.locationToIndex(localPoint);
/* 2140 */             localRectangle3 = paramJList.getCellBounds(i, i);
/*      */           }
/*      */ 
/* 2144 */           if (localRectangle3.y <= localRectangle2.y) {
/* 2145 */             localPoint.y = (localRectangle2.y + localRectangle2.height);
/* 2146 */             i = paramJList.locationToIndex(localPoint);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2151 */       return i;
/*      */     }
/*      */ 
/*      */     private void changeSelection(JList paramJList, int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2156 */       if ((paramInt2 >= 0) && (paramInt2 < paramJList.getModel().getSize())) {
/* 2157 */         ListSelectionModel localListSelectionModel = paramJList.getSelectionModel();
/*      */ 
/* 2160 */         if ((paramInt1 == 0) && (paramJList.getSelectionMode() != 2))
/*      */         {
/* 2164 */           paramInt1 = 1;
/*      */         }
/*      */ 
/* 2171 */         adjustScrollPositionIfNecessary(paramJList, paramInt2, paramInt3);
/*      */ 
/* 2173 */         if (paramInt1 == 2) {
/* 2174 */           int i = BasicListUI.adjustIndex(localListSelectionModel.getAnchorSelectionIndex(), paramJList);
/* 2175 */           if (i == -1) {
/* 2176 */             i = 0;
/*      */           }
/*      */ 
/* 2179 */           paramJList.setSelectionInterval(i, paramInt2);
/*      */         }
/* 2181 */         else if (paramInt1 == 1) {
/* 2182 */           paramJList.setSelectedIndex(paramInt2);
/*      */         }
/*      */         else
/*      */         {
/* 2187 */           ((DefaultListSelectionModel)localListSelectionModel).moveLeadSelectionIndex(paramInt2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void adjustScrollPositionIfNecessary(JList paramJList, int paramInt1, int paramInt2)
/*      */     {
/* 2199 */       if (paramInt2 == 0) {
/* 2200 */         return;
/*      */       }
/* 2202 */       Object localObject = paramJList.getCellBounds(paramInt1, paramInt1);
/* 2203 */       Rectangle localRectangle1 = paramJList.getVisibleRect();
/* 2204 */       if ((localObject != null) && (!localRectangle1.contains((Rectangle)localObject)))
/*      */       {
/*      */         int i;
/*      */         int j;
/*      */         Rectangle localRectangle2;
/* 2205 */         if ((paramJList.getLayoutOrientation() == 1) && (paramJList.getVisibleRowCount() <= 0))
/*      */         {
/* 2208 */           if (paramJList.getComponentOrientation().isLeftToRight()) {
/* 2209 */             if (paramInt2 > 0)
/*      */             {
/* 2211 */               i = Math.max(0, ((Rectangle)localObject).x + ((Rectangle)localObject).width - localRectangle1.width);
/*      */ 
/* 2213 */               j = paramJList.locationToIndex(new Point(i, ((Rectangle)localObject).y));
/*      */ 
/* 2215 */               localRectangle2 = paramJList.getCellBounds(j, j);
/*      */ 
/* 2217 */               if ((localRectangle2.x < i) && (localRectangle2.x < ((Rectangle)localObject).x)) {
/* 2218 */                 localRectangle2.x += localRectangle2.width;
/* 2219 */                 j = paramJList.locationToIndex(localRectangle2.getLocation());
/*      */ 
/* 2221 */                 localRectangle2 = paramJList.getCellBounds(j, j);
/*      */               }
/*      */ 
/* 2224 */               localObject = localRectangle2;
/*      */             }
/* 2226 */             ((Rectangle)localObject).width = localRectangle1.width;
/*      */           }
/* 2229 */           else if (paramInt2 > 0)
/*      */           {
/* 2231 */             i = ((Rectangle)localObject).x + localRectangle1.width;
/* 2232 */             j = paramJList.locationToIndex(new Point(i, ((Rectangle)localObject).y));
/*      */ 
/* 2234 */             localRectangle2 = paramJList.getCellBounds(j, j);
/*      */ 
/* 2236 */             if ((localRectangle2.x + localRectangle2.width > i) && (localRectangle2.x > ((Rectangle)localObject).x))
/*      */             {
/* 2238 */               localRectangle2.width = 0;
/*      */             }
/* 2240 */             ((Rectangle)localObject).x = Math.max(0, localRectangle2.x + localRectangle2.width - localRectangle1.width);
/*      */ 
/* 2242 */             ((Rectangle)localObject).width = localRectangle1.width;
/*      */           }
/*      */           else {
/* 2245 */             localObject.x += Math.max(0, ((Rectangle)localObject).width - localRectangle1.width);
/*      */ 
/* 2248 */             ((Rectangle)localObject).width = Math.min(((Rectangle)localObject).width, localRectangle1.width);
/*      */           }
/*      */ 
/*      */         }
/* 2255 */         else if ((paramInt2 > 0) && ((((Rectangle)localObject).y < localRectangle1.y) || (((Rectangle)localObject).y + ((Rectangle)localObject).height > localRectangle1.y + localRectangle1.height)))
/*      */         {
/* 2260 */           i = Math.max(0, ((Rectangle)localObject).y + ((Rectangle)localObject).height - localRectangle1.height);
/*      */ 
/* 2262 */           j = paramJList.locationToIndex(new Point(((Rectangle)localObject).x, i));
/*      */ 
/* 2264 */           localRectangle2 = paramJList.getCellBounds(j, j);
/*      */ 
/* 2266 */           if ((localRectangle2.y < i) && (localRectangle2.y < ((Rectangle)localObject).y)) {
/* 2267 */             localRectangle2.y += localRectangle2.height;
/* 2268 */             j = paramJList.locationToIndex(localRectangle2.getLocation());
/*      */ 
/* 2270 */             localRectangle2 = paramJList.getCellBounds(j, j);
/*      */           }
/*      */ 
/* 2273 */           localObject = localRectangle2;
/* 2274 */           ((Rectangle)localObject).height = localRectangle1.height;
/*      */         }
/*      */         else
/*      */         {
/* 2278 */           ((Rectangle)localObject).height = Math.min(((Rectangle)localObject).height, localRectangle1.height);
/*      */         }
/*      */ 
/* 2281 */         paramJList.scrollRectToVisible((Rectangle)localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getNextColumnIndex(JList paramJList, BasicListUI paramBasicListUI, int paramInt)
/*      */     {
/* 2287 */       if (paramJList.getLayoutOrientation() != 0) {
/* 2288 */         int i = BasicListUI.adjustIndex(paramJList.getLeadSelectionIndex(), paramJList);
/* 2289 */         int j = paramJList.getModel().getSize();
/*      */ 
/* 2291 */         if (i == -1)
/* 2292 */           return 0;
/* 2293 */         if (j == 1)
/*      */         {
/* 2295 */           return 0;
/* 2296 */         }if ((paramBasicListUI == null) || (paramBasicListUI.columnCount <= 1)) {
/* 2297 */           return -1;
/*      */         }
/*      */ 
/* 2300 */         int k = paramBasicListUI.convertModelToColumn(i);
/* 2301 */         int m = paramBasicListUI.convertModelToRow(i);
/*      */ 
/* 2303 */         k += paramInt;
/* 2304 */         if ((k >= paramBasicListUI.columnCount) || (k < 0))
/*      */         {
/* 2306 */           return -1;
/*      */         }
/* 2308 */         int n = paramBasicListUI.getRowCount(k);
/* 2309 */         if (m >= n) {
/* 2310 */           return -1;
/*      */         }
/* 2312 */         return paramBasicListUI.getModelIndex(k, m);
/*      */       }
/*      */ 
/* 2315 */       return -1;
/*      */     }
/*      */ 
/*      */     private int getNextIndex(JList paramJList, BasicListUI paramBasicListUI, int paramInt) {
/* 2319 */       int i = BasicListUI.adjustIndex(paramJList.getLeadSelectionIndex(), paramJList);
/* 2320 */       int j = paramJList.getModel().getSize();
/*      */ 
/* 2322 */       if (i == -1) {
/* 2323 */         if (j > 0) {
/* 2324 */           if (paramInt > 0) {
/* 2325 */             i = 0;
/*      */           }
/*      */           else
/* 2328 */             i = j - 1;
/*      */         }
/*      */       }
/* 2331 */       else if (j == 1)
/*      */       {
/* 2333 */         i = 0;
/* 2334 */       } else if (paramJList.getLayoutOrientation() == 2) {
/* 2335 */         if (paramBasicListUI != null)
/* 2336 */           i += paramBasicListUI.columnCount * paramInt;
/*      */       }
/*      */       else {
/* 2339 */         i += paramInt;
/*      */       }
/*      */ 
/* 2342 */       return i;
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
/*      */     protected void repaintCellFocus()
/*      */     {
/* 1565 */       BasicListUI.this.getHandler().repaintCellFocus();
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 1573 */       BasicListUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 1577 */       BasicListUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements FocusListener, KeyListener, ListDataListener, ListSelectionListener, MouseInputListener, PropertyChangeListener, DragRecognitionSupport.BeforeDrag
/*      */   {
/* 2354 */     private String prefix = "";
/* 2355 */     private String typedString = "";
/* 2356 */     private long lastTime = 0L;
/*      */     private boolean dragPressDidSelection;
/*      */ 
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent)
/*      */     {
/* 2370 */       JList localJList = (JList)paramKeyEvent.getSource();
/* 2371 */       ListModel localListModel = localJList.getModel();
/*      */ 
/* 2373 */       if ((localListModel.getSize() == 0) || (paramKeyEvent.isAltDown()) || (BasicGraphicsUtils.isMenuShortcutKeyDown(paramKeyEvent)) || (isNavigationKey(paramKeyEvent)))
/*      */       {
/* 2377 */         return;
/*      */       }
/* 2379 */       int i = 1;
/*      */ 
/* 2381 */       char c = paramKeyEvent.getKeyChar();
/*      */ 
/* 2383 */       long l = paramKeyEvent.getWhen();
/* 2384 */       int j = BasicListUI.adjustIndex(localJList.getLeadSelectionIndex(), BasicListUI.this.list);
/* 2385 */       if (l - this.lastTime < BasicListUI.this.timeFactor) {
/* 2386 */         this.typedString += c;
/* 2387 */         if ((this.prefix.length() == 1) && (c == this.prefix.charAt(0)))
/*      */         {
/* 2390 */           j++;
/*      */         }
/* 2392 */         else this.prefix = this.typedString; 
/*      */       }
/*      */       else
/*      */       {
/* 2395 */         j++;
/* 2396 */         this.typedString = ("" + c);
/* 2397 */         this.prefix = this.typedString;
/*      */       }
/* 2399 */       this.lastTime = l;
/*      */ 
/* 2401 */       if ((j < 0) || (j >= localListModel.getSize())) {
/* 2402 */         i = 0;
/* 2403 */         j = 0;
/*      */       }
/* 2405 */       int k = localJList.getNextMatch(this.prefix, j, Position.Bias.Forward);
/*      */ 
/* 2407 */       if (k >= 0) {
/* 2408 */         localJList.setSelectedIndex(k);
/* 2409 */         localJList.ensureIndexIsVisible(k);
/* 2410 */       } else if (i != 0) {
/* 2411 */         k = localJList.getNextMatch(this.prefix, 0, Position.Bias.Forward);
/*      */ 
/* 2413 */         if (k >= 0) {
/* 2414 */           localJList.setSelectedIndex(k);
/* 2415 */           localJList.ensureIndexIsVisible(k);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent)
/*      */     {
/* 2427 */       if (isNavigationKey(paramKeyEvent)) {
/* 2428 */         this.prefix = "";
/* 2429 */         this.typedString = "";
/* 2430 */         this.lastTime = 0L;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     private boolean isNavigationKey(KeyEvent paramKeyEvent)
/*      */     {
/* 2448 */       InputMap localInputMap = BasicListUI.this.list.getInputMap(1);
/* 2449 */       KeyStroke localKeyStroke = KeyStroke.getKeyStrokeForEvent(paramKeyEvent);
/*      */ 
/* 2451 */       if ((localInputMap != null) && (localInputMap.get(localKeyStroke) != null)) {
/* 2452 */         return true;
/*      */       }
/* 2454 */       return false;
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 2461 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 2466 */       if (str == "model") {
/* 2467 */         localObject1 = (ListModel)paramPropertyChangeEvent.getOldValue();
/* 2468 */         localObject2 = (ListModel)paramPropertyChangeEvent.getNewValue();
/* 2469 */         if (localObject1 != null) {
/* 2470 */           ((ListModel)localObject1).removeListDataListener(BasicListUI.this.listDataListener);
/*      */         }
/* 2472 */         if (localObject2 != null) {
/* 2473 */           ((ListModel)localObject2).addListDataListener(BasicListUI.this.listDataListener);
/*      */         }
/* 2475 */         BasicListUI.this.updateLayoutStateNeeded |= 1;
/* 2476 */         BasicListUI.this.redrawList();
/*      */       }
/* 2482 */       else if (str == "selectionModel") {
/* 2483 */         localObject1 = (ListSelectionModel)paramPropertyChangeEvent.getOldValue();
/* 2484 */         localObject2 = (ListSelectionModel)paramPropertyChangeEvent.getNewValue();
/* 2485 */         if (localObject1 != null) {
/* 2486 */           ((ListSelectionModel)localObject1).removeListSelectionListener(BasicListUI.this.listSelectionListener);
/*      */         }
/* 2488 */         if (localObject2 != null) {
/* 2489 */           ((ListSelectionModel)localObject2).addListSelectionListener(BasicListUI.this.listSelectionListener);
/*      */         }
/* 2491 */         BasicListUI.this.updateLayoutStateNeeded |= 1;
/* 2492 */         BasicListUI.this.redrawList();
/*      */       }
/* 2494 */       else if (str == "cellRenderer") {
/* 2495 */         BasicListUI.this.updateLayoutStateNeeded |= 64;
/* 2496 */         BasicListUI.this.redrawList();
/*      */       }
/* 2498 */       else if (str == "font") {
/* 2499 */         BasicListUI.this.updateLayoutStateNeeded |= 4;
/* 2500 */         BasicListUI.this.redrawList();
/*      */       }
/* 2502 */       else if (str == "prototypeCellValue") {
/* 2503 */         BasicListUI.this.updateLayoutStateNeeded |= 32;
/* 2504 */         BasicListUI.this.redrawList();
/*      */       }
/* 2506 */       else if (str == "fixedCellHeight") {
/* 2507 */         BasicListUI.this.updateLayoutStateNeeded |= 16;
/* 2508 */         BasicListUI.this.redrawList();
/*      */       }
/* 2510 */       else if (str == "fixedCellWidth") {
/* 2511 */         BasicListUI.this.updateLayoutStateNeeded |= 8;
/* 2512 */         BasicListUI.this.redrawList();
/*      */       }
/* 2514 */       else if (str == "selectionForeground") {
/* 2515 */         BasicListUI.this.list.repaint();
/*      */       }
/* 2517 */       else if (str == "selectionBackground") {
/* 2518 */         BasicListUI.this.list.repaint();
/*      */       }
/* 2520 */       else if ("layoutOrientation" == str) {
/* 2521 */         BasicListUI.this.updateLayoutStateNeeded |= 128;
/* 2522 */         BasicListUI.this.layoutOrientation = BasicListUI.this.list.getLayoutOrientation();
/* 2523 */         BasicListUI.this.redrawList();
/*      */       }
/* 2525 */       else if ("visibleRowCount" == str) {
/* 2526 */         if (BasicListUI.this.layoutOrientation != 0) {
/* 2527 */           BasicListUI.this.updateLayoutStateNeeded |= 128;
/* 2528 */           BasicListUI.this.redrawList();
/*      */         }
/*      */       }
/* 2531 */       else if ("componentOrientation" == str) {
/* 2532 */         BasicListUI.this.isLeftToRight = BasicListUI.this.list.getComponentOrientation().isLeftToRight();
/* 2533 */         BasicListUI.this.updateLayoutStateNeeded |= 1024;
/* 2534 */         BasicListUI.this.redrawList();
/*      */ 
/* 2536 */         localObject1 = BasicListUI.this.getInputMap(0);
/* 2537 */         SwingUtilities.replaceUIInputMap(BasicListUI.this.list, 0, (InputMap)localObject1);
/*      */       }
/* 2539 */       else if ("List.isFileList" == str) {
/* 2540 */         BasicListUI.this.updateIsFileList();
/* 2541 */         BasicListUI.this.redrawList();
/* 2542 */       } else if ("dropLocation" == str) {
/* 2543 */         localObject1 = (JList.DropLocation)paramPropertyChangeEvent.getOldValue();
/* 2544 */         repaintDropLocation((JList.DropLocation)localObject1);
/* 2545 */         repaintDropLocation(BasicListUI.this.list.getDropLocation());
/*      */       }
/*      */     }
/*      */ 
/*      */     private void repaintDropLocation(JList.DropLocation paramDropLocation) {
/* 2550 */       if (paramDropLocation == null)
/*      */         return;
/*      */       Rectangle localRectangle;
/* 2556 */       if (paramDropLocation.isInsert())
/* 2557 */         localRectangle = BasicListUI.this.getDropLineRect(paramDropLocation);
/*      */       else {
/* 2559 */         localRectangle = BasicListUI.this.getCellBounds(BasicListUI.this.list, paramDropLocation.getIndex());
/*      */       }
/*      */ 
/* 2562 */       if (localRectangle != null)
/* 2563 */         BasicListUI.this.list.repaint(localRectangle);
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent)
/*      */     {
/* 2571 */       BasicListUI.this.updateLayoutStateNeeded = 1;
/*      */ 
/* 2573 */       int i = Math.min(paramListDataEvent.getIndex0(), paramListDataEvent.getIndex1());
/* 2574 */       int j = Math.max(paramListDataEvent.getIndex0(), paramListDataEvent.getIndex1());
/*      */ 
/* 2579 */       ListSelectionModel localListSelectionModel = BasicListUI.this.list.getSelectionModel();
/* 2580 */       if (localListSelectionModel != null) {
/* 2581 */         localListSelectionModel.insertIndexInterval(i, j - i + 1, true);
/*      */       }
/*      */ 
/* 2588 */       BasicListUI.this.redrawList();
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent)
/*      */     {
/* 2594 */       BasicListUI.this.updateLayoutStateNeeded = 1;
/*      */ 
/* 2599 */       ListSelectionModel localListSelectionModel = BasicListUI.this.list.getSelectionModel();
/* 2600 */       if (localListSelectionModel != null) {
/* 2601 */         localListSelectionModel.removeIndexInterval(paramListDataEvent.getIndex0(), paramListDataEvent.getIndex1());
/*      */       }
/*      */ 
/* 2609 */       BasicListUI.this.redrawList();
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/* 2614 */       BasicListUI.this.updateLayoutStateNeeded = 1;
/* 2615 */       BasicListUI.this.redrawList();
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 2623 */       BasicListUI.this.maybeUpdateLayoutState();
/*      */ 
/* 2625 */       int i = BasicListUI.this.list.getModel().getSize();
/* 2626 */       int j = Math.min(i - 1, Math.max(paramListSelectionEvent.getFirstIndex(), 0));
/* 2627 */       int k = Math.min(i - 1, Math.max(paramListSelectionEvent.getLastIndex(), 0));
/*      */ 
/* 2629 */       Rectangle localRectangle = BasicListUI.this.getCellBounds(BasicListUI.this.list, j, k);
/*      */ 
/* 2631 */       if (localRectangle != null)
/* 2632 */         BasicListUI.this.list.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 2654 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicListUI.this.list)) {
/* 2655 */         return;
/*      */       }
/*      */ 
/* 2658 */       boolean bool = BasicListUI.this.list.getDragEnabled();
/* 2659 */       int i = 1;
/*      */ 
/* 2662 */       if (bool) {
/* 2663 */         int j = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, paramMouseEvent.getPoint());
/*      */ 
/* 2665 */         if ((j != -1) && (DragRecognitionSupport.mousePressed(paramMouseEvent))) {
/* 2666 */           this.dragPressDidSelection = false;
/*      */ 
/* 2668 */           if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent))
/*      */           {
/* 2671 */             return;
/* 2672 */           }if ((!paramMouseEvent.isShiftDown()) && (BasicListUI.this.list.isSelectedIndex(j)))
/*      */           {
/* 2675 */             BasicListUI.this.list.addSelectionInterval(j, j);
/* 2676 */             return;
/*      */           }
/*      */ 
/* 2680 */           i = 0;
/*      */ 
/* 2682 */           this.dragPressDidSelection = true;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2688 */         BasicListUI.this.list.setValueIsAdjusting(true);
/*      */       }
/*      */ 
/* 2691 */       if (i != 0) {
/* 2692 */         SwingUtilities2.adjustFocus(BasicListUI.this.list);
/*      */       }
/*      */ 
/* 2695 */       adjustSelection(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     private void adjustSelection(MouseEvent paramMouseEvent) {
/* 2699 */       int i = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, paramMouseEvent.getPoint());
/* 2700 */       if (i < 0)
/*      */       {
/* 2703 */         if ((BasicListUI.this.isFileList) && (paramMouseEvent.getID() == 501) && ((!paramMouseEvent.isShiftDown()) || (BasicListUI.this.list.getSelectionMode() == 0)))
/*      */         {
/* 2707 */           BasicListUI.this.list.clearSelection();
/*      */         }
/*      */       }
/*      */       else {
/* 2711 */         int j = BasicListUI.adjustIndex(BasicListUI.this.list.getAnchorSelectionIndex(), BasicListUI.this.list);
/*      */         boolean bool;
/* 2713 */         if (j == -1) {
/* 2714 */           j = 0;
/* 2715 */           bool = false;
/*      */         } else {
/* 2717 */           bool = BasicListUI.this.list.isSelectedIndex(j);
/*      */         }
/*      */ 
/* 2720 */         if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) {
/* 2721 */           if (paramMouseEvent.isShiftDown()) {
/* 2722 */             if (bool) {
/* 2723 */               BasicListUI.this.list.addSelectionInterval(j, i);
/*      */             } else {
/* 2725 */               BasicListUI.this.list.removeSelectionInterval(j, i);
/* 2726 */               if (BasicListUI.this.isFileList) {
/* 2727 */                 BasicListUI.this.list.addSelectionInterval(i, i);
/* 2728 */                 BasicListUI.this.list.getSelectionModel().setAnchorSelectionIndex(j);
/*      */               }
/*      */             }
/* 2731 */           } else if (BasicListUI.this.list.isSelectedIndex(i))
/* 2732 */             BasicListUI.this.list.removeSelectionInterval(i, i);
/*      */           else
/* 2734 */             BasicListUI.this.list.addSelectionInterval(i, i);
/*      */         }
/* 2736 */         else if (paramMouseEvent.isShiftDown())
/* 2737 */           BasicListUI.this.list.setSelectionInterval(j, i);
/*      */         else
/* 2739 */           BasicListUI.this.list.setSelectionInterval(i, i);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dragStarting(MouseEvent paramMouseEvent)
/*      */     {
/* 2745 */       if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) {
/* 2746 */         int i = SwingUtilities2.loc2IndexFileList(BasicListUI.this.list, paramMouseEvent.getPoint());
/* 2747 */         BasicListUI.this.list.addSelectionInterval(i, i);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 2752 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicListUI.this.list)) {
/* 2753 */         return;
/*      */       }
/*      */ 
/* 2756 */       if (BasicListUI.this.list.getDragEnabled()) {
/* 2757 */         DragRecognitionSupport.mouseDragged(paramMouseEvent, this);
/* 2758 */         return;
/*      */       }
/*      */ 
/* 2761 */       if ((paramMouseEvent.isShiftDown()) || (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent))) {
/* 2762 */         return;
/*      */       }
/*      */ 
/* 2765 */       int i = BasicListUI.this.locationToIndex(BasicListUI.this.list, paramMouseEvent.getPoint());
/* 2766 */       if (i != -1)
/*      */       {
/* 2768 */         if (BasicListUI.this.isFileList) {
/* 2769 */           return;
/*      */         }
/* 2771 */         Rectangle localRectangle = BasicListUI.this.getCellBounds(BasicListUI.this.list, i, i);
/* 2772 */         if (localRectangle != null) {
/* 2773 */           BasicListUI.this.list.scrollRectToVisible(localRectangle);
/* 2774 */           BasicListUI.this.list.setSelectionInterval(i, i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 2783 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicListUI.this.list)) {
/* 2784 */         return;
/*      */       }
/*      */ 
/* 2787 */       if (BasicListUI.this.list.getDragEnabled()) {
/* 2788 */         MouseEvent localMouseEvent = DragRecognitionSupport.mouseReleased(paramMouseEvent);
/* 2789 */         if (localMouseEvent != null) {
/* 2790 */           SwingUtilities2.adjustFocus(BasicListUI.this.list);
/* 2791 */           if (!this.dragPressDidSelection)
/* 2792 */             adjustSelection(localMouseEvent);
/*      */         }
/*      */       }
/*      */       else {
/* 2796 */         BasicListUI.this.list.setValueIsAdjusting(false);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void repaintCellFocus()
/*      */     {
/* 2805 */       int i = BasicListUI.adjustIndex(BasicListUI.this.list.getLeadSelectionIndex(), BasicListUI.this.list);
/* 2806 */       if (i != -1) {
/* 2807 */         Rectangle localRectangle = BasicListUI.this.getCellBounds(BasicListUI.this.list, i, i);
/* 2808 */         if (localRectangle != null)
/* 2809 */           BasicListUI.this.list.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 2819 */       repaintCellFocus();
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 2823 */       repaintCellFocus();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ListDataHandler
/*      */     implements ListDataListener
/*      */   {
/*      */     public ListDataHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void intervalAdded(ListDataEvent paramListDataEvent)
/*      */     {
/* 1665 */       BasicListUI.this.getHandler().intervalAdded(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent paramListDataEvent)
/*      */     {
/* 1671 */       BasicListUI.this.getHandler().intervalRemoved(paramListDataEvent);
/*      */     }
/*      */ 
/*      */     public void contentsChanged(ListDataEvent paramListDataEvent)
/*      */     {
/* 1676 */       BasicListUI.this.getHandler().contentsChanged(paramListDataEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ListSelectionHandler
/*      */     implements ListSelectionListener
/*      */   {
/*      */     public ListSelectionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*      */     {
/* 1607 */       BasicListUI.this.getHandler().valueChanged(paramListSelectionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ListTransferHandler extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     protected Transferable createTransferable(JComponent paramJComponent)
/*      */     {
/* 2845 */       if ((paramJComponent instanceof JList)) {
/* 2846 */         JList localJList = (JList)paramJComponent;
/* 2847 */         Object[] arrayOfObject = localJList.getSelectedValues();
/*      */ 
/* 2849 */         if ((arrayOfObject == null) || (arrayOfObject.length == 0)) {
/* 2850 */           return null;
/*      */         }
/*      */ 
/* 2853 */         StringBuffer localStringBuffer1 = new StringBuffer();
/* 2854 */         StringBuffer localStringBuffer2 = new StringBuffer();
/*      */ 
/* 2856 */         localStringBuffer2.append("<html>\n<body>\n<ul>\n");
/*      */ 
/* 2858 */         for (int i = 0; i < arrayOfObject.length; i++) {
/* 2859 */           Object localObject = arrayOfObject[i];
/* 2860 */           String str = localObject == null ? "" : localObject.toString();
/* 2861 */           localStringBuffer1.append(str + "\n");
/* 2862 */           localStringBuffer2.append("  <li>" + str + "\n");
/*      */         }
/*      */ 
/* 2866 */         localStringBuffer1.deleteCharAt(localStringBuffer1.length() - 1);
/* 2867 */         localStringBuffer2.append("</ul>\n</body>\n</html>");
/*      */ 
/* 2869 */         return new BasicTransferable(localStringBuffer1.toString(), localStringBuffer2.toString());
/*      */       }
/*      */ 
/* 2872 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSourceActions(JComponent paramJComponent) {
/* 2876 */       return 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class MouseInputHandler
/*      */     implements MouseInputListener
/*      */   {
/*      */     public MouseInputHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/* 1502 */       BasicListUI.this.getHandler().mouseClicked(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 1506 */       BasicListUI.this.getHandler().mouseEntered(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 1510 */       BasicListUI.this.getHandler().mouseExited(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 1514 */       BasicListUI.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 1518 */       BasicListUI.this.getHandler().mouseDragged(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 1522 */       BasicListUI.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 1526 */       BasicListUI.this.getHandler().mouseReleased(paramMouseEvent);
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
/* 1732 */       BasicListUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicListUI
 * JD-Core Version:    0.6.2
 */