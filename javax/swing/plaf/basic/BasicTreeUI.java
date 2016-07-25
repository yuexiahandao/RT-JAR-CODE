/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JScrollBar;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JTree;
/*      */ import javax.swing.JTree.DropLocation;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CellEditorListener;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.event.TreeExpansionEvent;
/*      */ import javax.swing.event.TreeExpansionListener;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ import javax.swing.event.TreeModelListener;
/*      */ import javax.swing.event.TreeSelectionEvent;
/*      */ import javax.swing.event.TreeSelectionListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TreeUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.tree.AbstractLayoutCache;
/*      */ import javax.swing.tree.AbstractLayoutCache.NodeDimensions;
/*      */ import javax.swing.tree.DefaultTreeCellEditor;
/*      */ import javax.swing.tree.DefaultTreeCellRenderer;
/*      */ import javax.swing.tree.FixedHeightLayoutCache;
/*      */ import javax.swing.tree.TreeCellEditor;
/*      */ import javax.swing.tree.TreeCellRenderer;
/*      */ import javax.swing.tree.TreeModel;
/*      */ import javax.swing.tree.TreePath;
/*      */ import javax.swing.tree.TreeSelectionModel;
/*      */ import javax.swing.tree.VariableHeightLayoutCache;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.UIAction;
/*      */ 
/*      */ public class BasicTreeUI extends TreeUI
/*      */ {
/*   61 */   private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("Tree.baselineComponent");
/*      */ 
/*   65 */   private static final Actions SHARED_ACTION = new Actions();
/*      */   protected transient Icon collapsedIcon;
/*      */   protected transient Icon expandedIcon;
/*      */   private Color hashColor;
/*      */   protected int leftChildIndent;
/*      */   protected int rightChildIndent;
/*      */   protected int totalChildIndent;
/*      */   protected Dimension preferredMinSize;
/*      */   protected int lastSelectedRow;
/*      */   protected JTree tree;
/*      */   protected transient TreeCellRenderer currentCellRenderer;
/*      */   protected boolean createdRenderer;
/*      */   protected transient TreeCellEditor cellEditor;
/*      */   protected boolean createdCellEditor;
/*      */   protected boolean stopEditingInCompleteEditing;
/*      */   protected CellRendererPane rendererPane;
/*      */   protected Dimension preferredSize;
/*      */   protected boolean validCachedPreferredSize;
/*      */   protected AbstractLayoutCache treeState;
/*      */   protected Hashtable<TreePath, Boolean> drawingCache;
/*      */   protected boolean largeModel;
/*      */   protected AbstractLayoutCache.NodeDimensions nodeDimensions;
/*      */   protected TreeModel treeModel;
/*      */   protected TreeSelectionModel treeSelectionModel;
/*      */   protected int depthOffset;
/*      */   protected Component editingComponent;
/*      */   protected TreePath editingPath;
/*      */   protected int editingRow;
/*      */   protected boolean editorHasDifferentSize;
/*      */   private int leadRow;
/*      */   private boolean ignoreLAChange;
/*      */   private boolean leftToRight;
/*      */   private PropertyChangeListener propertyChangeListener;
/*      */   private PropertyChangeListener selectionModelPropertyChangeListener;
/*      */   private MouseListener mouseListener;
/*      */   private FocusListener focusListener;
/*      */   private KeyListener keyListener;
/*      */   private ComponentListener componentListener;
/*      */   private CellEditorListener cellEditorListener;
/*      */   private TreeSelectionListener treeSelectionListener;
/*      */   private TreeModelListener treeModelListener;
/*      */   private TreeExpansionListener treeExpansionListener;
/*  195 */   private boolean paintLines = true;
/*      */   private boolean lineTypeDashed;
/*  204 */   private long timeFactor = 1000L;
/*      */   private Handler handler;
/*      */   private MouseEvent releaseEvent;
/* 3186 */   private static final TransferHandler defaultTransferHandler = new TreeTransferHandler();
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*  215 */     return new BasicTreeUI();
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*      */   {
/*  220 */     paramLazyActionMap.put(new Actions("selectPrevious"));
/*  221 */     paramLazyActionMap.put(new Actions("selectPreviousChangeLead"));
/*  222 */     paramLazyActionMap.put(new Actions("selectPreviousExtendSelection"));
/*      */ 
/*  224 */     paramLazyActionMap.put(new Actions("selectNext"));
/*  225 */     paramLazyActionMap.put(new Actions("selectNextChangeLead"));
/*  226 */     paramLazyActionMap.put(new Actions("selectNextExtendSelection"));
/*      */ 
/*  228 */     paramLazyActionMap.put(new Actions("selectChild"));
/*  229 */     paramLazyActionMap.put(new Actions("selectChildChangeLead"));
/*      */ 
/*  231 */     paramLazyActionMap.put(new Actions("selectParent"));
/*  232 */     paramLazyActionMap.put(new Actions("selectParentChangeLead"));
/*      */ 
/*  234 */     paramLazyActionMap.put(new Actions("scrollUpChangeSelection"));
/*  235 */     paramLazyActionMap.put(new Actions("scrollUpChangeLead"));
/*  236 */     paramLazyActionMap.put(new Actions("scrollUpExtendSelection"));
/*      */ 
/*  238 */     paramLazyActionMap.put(new Actions("scrollDownChangeSelection"));
/*  239 */     paramLazyActionMap.put(new Actions("scrollDownExtendSelection"));
/*  240 */     paramLazyActionMap.put(new Actions("scrollDownChangeLead"));
/*      */ 
/*  242 */     paramLazyActionMap.put(new Actions("selectFirst"));
/*  243 */     paramLazyActionMap.put(new Actions("selectFirstChangeLead"));
/*  244 */     paramLazyActionMap.put(new Actions("selectFirstExtendSelection"));
/*      */ 
/*  246 */     paramLazyActionMap.put(new Actions("selectLast"));
/*  247 */     paramLazyActionMap.put(new Actions("selectLastChangeLead"));
/*  248 */     paramLazyActionMap.put(new Actions("selectLastExtendSelection"));
/*      */ 
/*  250 */     paramLazyActionMap.put(new Actions("toggle"));
/*      */ 
/*  252 */     paramLazyActionMap.put(new Actions("cancel"));
/*      */ 
/*  254 */     paramLazyActionMap.put(new Actions("startEditing"));
/*      */ 
/*  256 */     paramLazyActionMap.put(new Actions("selectAll"));
/*      */ 
/*  258 */     paramLazyActionMap.put(new Actions("clearSelection"));
/*      */ 
/*  260 */     paramLazyActionMap.put(new Actions("scrollLeft"));
/*  261 */     paramLazyActionMap.put(new Actions("scrollRight"));
/*      */ 
/*  263 */     paramLazyActionMap.put(new Actions("scrollLeftExtendSelection"));
/*  264 */     paramLazyActionMap.put(new Actions("scrollRightExtendSelection"));
/*      */ 
/*  266 */     paramLazyActionMap.put(new Actions("scrollRightChangeLead"));
/*  267 */     paramLazyActionMap.put(new Actions("scrollLeftChangeLead"));
/*      */ 
/*  269 */     paramLazyActionMap.put(new Actions("expand"));
/*  270 */     paramLazyActionMap.put(new Actions("collapse"));
/*  271 */     paramLazyActionMap.put(new Actions("moveSelectionToParent"));
/*      */ 
/*  273 */     paramLazyActionMap.put(new Actions("addToSelection"));
/*  274 */     paramLazyActionMap.put(new Actions("toggleAndAnchor"));
/*  275 */     paramLazyActionMap.put(new Actions("extendTo"));
/*  276 */     paramLazyActionMap.put(new Actions("moveSelectionTo"));
/*      */ 
/*  278 */     paramLazyActionMap.put(TransferHandler.getCutAction());
/*  279 */     paramLazyActionMap.put(TransferHandler.getCopyAction());
/*  280 */     paramLazyActionMap.put(TransferHandler.getPasteAction());
/*      */   }
/*      */ 
/*      */   protected Color getHashColor()
/*      */   {
/*  289 */     return this.hashColor;
/*      */   }
/*      */ 
/*      */   protected void setHashColor(Color paramColor) {
/*  293 */     this.hashColor = paramColor;
/*      */   }
/*      */ 
/*      */   public void setLeftChildIndent(int paramInt) {
/*  297 */     this.leftChildIndent = paramInt;
/*  298 */     this.totalChildIndent = (this.leftChildIndent + this.rightChildIndent);
/*  299 */     if (this.treeState != null)
/*  300 */       this.treeState.invalidateSizes();
/*  301 */     updateSize();
/*      */   }
/*      */ 
/*      */   public int getLeftChildIndent() {
/*  305 */     return this.leftChildIndent;
/*      */   }
/*      */ 
/*      */   public void setRightChildIndent(int paramInt) {
/*  309 */     this.rightChildIndent = paramInt;
/*  310 */     this.totalChildIndent = (this.leftChildIndent + this.rightChildIndent);
/*  311 */     if (this.treeState != null)
/*  312 */       this.treeState.invalidateSizes();
/*  313 */     updateSize();
/*      */   }
/*      */ 
/*      */   public int getRightChildIndent() {
/*  317 */     return this.rightChildIndent;
/*      */   }
/*      */ 
/*      */   public void setExpandedIcon(Icon paramIcon) {
/*  321 */     this.expandedIcon = paramIcon;
/*      */   }
/*      */ 
/*      */   public Icon getExpandedIcon() {
/*  325 */     return this.expandedIcon;
/*      */   }
/*      */ 
/*      */   public void setCollapsedIcon(Icon paramIcon) {
/*  329 */     this.collapsedIcon = paramIcon;
/*      */   }
/*      */ 
/*      */   public Icon getCollapsedIcon() {
/*  333 */     return this.collapsedIcon;
/*      */   }
/*      */ 
/*      */   protected void setLargeModel(boolean paramBoolean)
/*      */   {
/*  346 */     if (getRowHeight() < 1)
/*  347 */       paramBoolean = false;
/*  348 */     if (this.largeModel != paramBoolean) {
/*  349 */       completeEditing();
/*  350 */       this.largeModel = paramBoolean;
/*  351 */       this.treeState = createLayoutCache();
/*  352 */       configureLayoutCache();
/*  353 */       updateLayoutCacheExpandedNodesIfNecessary();
/*  354 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isLargeModel() {
/*  359 */     return this.largeModel;
/*      */   }
/*      */ 
/*      */   protected void setRowHeight(int paramInt)
/*      */   {
/*  366 */     completeEditing();
/*  367 */     if (this.treeState != null) {
/*  368 */       setLargeModel(this.tree.isLargeModel());
/*  369 */       this.treeState.setRowHeight(paramInt);
/*  370 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int getRowHeight() {
/*  375 */     return this.tree == null ? -1 : this.tree.getRowHeight();
/*      */   }
/*      */ 
/*      */   protected void setCellRenderer(TreeCellRenderer paramTreeCellRenderer)
/*      */   {
/*  383 */     completeEditing();
/*  384 */     updateRenderer();
/*  385 */     if (this.treeState != null) {
/*  386 */       this.treeState.invalidateSizes();
/*  387 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected TreeCellRenderer getCellRenderer()
/*      */   {
/*  396 */     return this.currentCellRenderer;
/*      */   }
/*      */ 
/*      */   protected void setModel(TreeModel paramTreeModel)
/*      */   {
/*  403 */     completeEditing();
/*  404 */     if ((this.treeModel != null) && (this.treeModelListener != null))
/*  405 */       this.treeModel.removeTreeModelListener(this.treeModelListener);
/*  406 */     this.treeModel = paramTreeModel;
/*  407 */     if ((this.treeModel != null) && 
/*  408 */       (this.treeModelListener != null)) {
/*  409 */       this.treeModel.addTreeModelListener(this.treeModelListener);
/*      */     }
/*  411 */     if (this.treeState != null) {
/*  412 */       this.treeState.setModel(paramTreeModel);
/*  413 */       updateLayoutCacheExpandedNodesIfNecessary();
/*  414 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected TreeModel getModel() {
/*  419 */     return this.treeModel;
/*      */   }
/*      */ 
/*      */   protected void setRootVisible(boolean paramBoolean)
/*      */   {
/*  426 */     completeEditing();
/*  427 */     updateDepthOffset();
/*  428 */     if (this.treeState != null) {
/*  429 */       this.treeState.setRootVisible(paramBoolean);
/*  430 */       this.treeState.invalidateSizes();
/*  431 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isRootVisible() {
/*  436 */     return this.tree != null ? this.tree.isRootVisible() : false;
/*      */   }
/*      */ 
/*      */   protected void setShowsRootHandles(boolean paramBoolean)
/*      */   {
/*  443 */     completeEditing();
/*  444 */     updateDepthOffset();
/*  445 */     if (this.treeState != null) {
/*  446 */       this.treeState.invalidateSizes();
/*  447 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean getShowsRootHandles() {
/*  452 */     return this.tree != null ? this.tree.getShowsRootHandles() : false;
/*      */   }
/*      */ 
/*      */   protected void setCellEditor(TreeCellEditor paramTreeCellEditor)
/*      */   {
/*  459 */     updateCellEditor();
/*      */   }
/*      */ 
/*      */   protected TreeCellEditor getCellEditor() {
/*  463 */     return this.tree != null ? this.tree.getCellEditor() : null;
/*      */   }
/*      */ 
/*      */   protected void setEditable(boolean paramBoolean)
/*      */   {
/*  470 */     updateCellEditor();
/*      */   }
/*      */ 
/*      */   protected boolean isEditable() {
/*  474 */     return this.tree != null ? this.tree.isEditable() : false;
/*      */   }
/*      */ 
/*      */   protected void setSelectionModel(TreeSelectionModel paramTreeSelectionModel)
/*      */   {
/*  482 */     completeEditing();
/*  483 */     if ((this.selectionModelPropertyChangeListener != null) && (this.treeSelectionModel != null))
/*      */     {
/*  485 */       this.treeSelectionModel.removePropertyChangeListener(this.selectionModelPropertyChangeListener);
/*      */     }
/*  487 */     if ((this.treeSelectionListener != null) && (this.treeSelectionModel != null)) {
/*  488 */       this.treeSelectionModel.removeTreeSelectionListener(this.treeSelectionListener);
/*      */     }
/*  490 */     this.treeSelectionModel = paramTreeSelectionModel;
/*  491 */     if (this.treeSelectionModel != null) {
/*  492 */       if (this.selectionModelPropertyChangeListener != null) {
/*  493 */         this.treeSelectionModel.addPropertyChangeListener(this.selectionModelPropertyChangeListener);
/*      */       }
/*  495 */       if (this.treeSelectionListener != null) {
/*  496 */         this.treeSelectionModel.addTreeSelectionListener(this.treeSelectionListener);
/*      */       }
/*  498 */       if (this.treeState != null)
/*  499 */         this.treeState.setSelectionModel(this.treeSelectionModel);
/*      */     }
/*  501 */     else if (this.treeState != null) {
/*  502 */       this.treeState.setSelectionModel(null);
/*  503 */     }if (this.tree != null)
/*  504 */       this.tree.repaint();
/*      */   }
/*      */ 
/*      */   protected TreeSelectionModel getSelectionModel() {
/*  508 */     return this.treeSelectionModel;
/*      */   }
/*      */ 
/*      */   public Rectangle getPathBounds(JTree paramJTree, TreePath paramTreePath)
/*      */   {
/*  521 */     if ((paramJTree != null) && (this.treeState != null)) {
/*  522 */       return getPathBounds(paramTreePath, paramJTree.getInsets(), new Rectangle());
/*      */     }
/*  524 */     return null;
/*      */   }
/*      */ 
/*      */   private Rectangle getPathBounds(TreePath paramTreePath, Insets paramInsets, Rectangle paramRectangle)
/*      */   {
/*  529 */     paramRectangle = this.treeState.getBounds(paramTreePath, paramRectangle);
/*  530 */     if (paramRectangle != null) {
/*  531 */       if (this.leftToRight)
/*  532 */         paramRectangle.x += paramInsets.left;
/*      */       else {
/*  534 */         paramRectangle.x = (this.tree.getWidth() - (paramRectangle.x + paramRectangle.width) - paramInsets.right);
/*      */       }
/*      */ 
/*  537 */       paramRectangle.y += paramInsets.top;
/*      */     }
/*  539 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   public TreePath getPathForRow(JTree paramJTree, int paramInt)
/*      */   {
/*  547 */     return this.treeState != null ? this.treeState.getPathForRow(paramInt) : null;
/*      */   }
/*      */ 
/*      */   public int getRowForPath(JTree paramJTree, TreePath paramTreePath)
/*      */   {
/*  556 */     return this.treeState != null ? this.treeState.getRowForPath(paramTreePath) : -1;
/*      */   }
/*      */ 
/*      */   public int getRowCount(JTree paramJTree)
/*      */   {
/*  563 */     return this.treeState != null ? this.treeState.getRowCount() : 0;
/*      */   }
/*      */ 
/*      */   public TreePath getClosestPathForLocation(JTree paramJTree, int paramInt1, int paramInt2)
/*      */   {
/*  574 */     if ((paramJTree != null) && (this.treeState != null))
/*      */     {
/*  577 */       paramInt2 -= paramJTree.getInsets().top;
/*  578 */       return this.treeState.getPathClosestTo(paramInt1, paramInt2);
/*      */     }
/*  580 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isEditing(JTree paramJTree)
/*      */   {
/*  588 */     return this.editingComponent != null;
/*      */   }
/*      */ 
/*      */   public boolean stopEditing(JTree paramJTree)
/*      */   {
/*  597 */     if ((this.editingComponent != null) && (this.cellEditor.stopCellEditing())) {
/*  598 */       completeEditing(false, false, true);
/*  599 */       return true;
/*      */     }
/*  601 */     return false;
/*      */   }
/*      */ 
/*      */   public void cancelEditing(JTree paramJTree)
/*      */   {
/*  608 */     if (this.editingComponent != null)
/*  609 */       completeEditing(false, true, false);
/*      */   }
/*      */ 
/*      */   public void startEditingAtPath(JTree paramJTree, TreePath paramTreePath)
/*      */   {
/*  618 */     paramJTree.scrollPathToVisible(paramTreePath);
/*  619 */     if ((paramTreePath != null) && (paramJTree.isVisible(paramTreePath)))
/*  620 */       startEditing(paramTreePath, null);
/*      */   }
/*      */ 
/*      */   public TreePath getEditingPath(JTree paramJTree)
/*      */   {
/*  627 */     return this.editingPath;
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  635 */     if (paramJComponent == null) {
/*  636 */       throw new NullPointerException("null component passed to BasicTreeUI.installUI()");
/*      */     }
/*      */ 
/*  639 */     this.tree = ((JTree)paramJComponent);
/*      */ 
/*  641 */     prepareForUIInstall();
/*      */ 
/*  644 */     installDefaults();
/*  645 */     installKeyboardActions();
/*  646 */     installComponents();
/*  647 */     installListeners();
/*      */ 
/*  649 */     completeUIInstall();
/*      */   }
/*      */ 
/*      */   protected void prepareForUIInstall()
/*      */   {
/*  657 */     this.drawingCache = new Hashtable(7);
/*      */ 
/*  660 */     this.leftToRight = BasicGraphicsUtils.isLeftToRight(this.tree);
/*  661 */     this.stopEditingInCompleteEditing = true;
/*  662 */     this.lastSelectedRow = -1;
/*  663 */     this.leadRow = -1;
/*  664 */     this.preferredSize = new Dimension();
/*      */ 
/*  666 */     this.largeModel = this.tree.isLargeModel();
/*  667 */     if (getRowHeight() <= 0)
/*  668 */       this.largeModel = false;
/*  669 */     setModel(this.tree.getModel());
/*      */   }
/*      */ 
/*      */   protected void completeUIInstall()
/*      */   {
/*  679 */     setShowsRootHandles(this.tree.getShowsRootHandles());
/*      */ 
/*  681 */     updateRenderer();
/*      */ 
/*  683 */     updateDepthOffset();
/*      */ 
/*  685 */     setSelectionModel(this.tree.getSelectionModel());
/*      */ 
/*  688 */     this.treeState = createLayoutCache();
/*  689 */     configureLayoutCache();
/*      */ 
/*  691 */     updateSize();
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*  695 */     if ((this.tree.getBackground() == null) || ((this.tree.getBackground() instanceof UIResource)))
/*      */     {
/*  697 */       this.tree.setBackground(UIManager.getColor("Tree.background"));
/*      */     }
/*  699 */     if ((getHashColor() == null) || ((getHashColor() instanceof UIResource))) {
/*  700 */       setHashColor(UIManager.getColor("Tree.hash"));
/*      */     }
/*  702 */     if ((this.tree.getFont() == null) || ((this.tree.getFont() instanceof UIResource))) {
/*  703 */       this.tree.setFont(UIManager.getFont("Tree.font"));
/*      */     }
/*      */ 
/*  712 */     setExpandedIcon((Icon)UIManager.get("Tree.expandedIcon"));
/*  713 */     setCollapsedIcon((Icon)UIManager.get("Tree.collapsedIcon"));
/*      */ 
/*  715 */     setLeftChildIndent(((Integer)UIManager.get("Tree.leftChildIndent")).intValue());
/*      */ 
/*  717 */     setRightChildIndent(((Integer)UIManager.get("Tree.rightChildIndent")).intValue());
/*      */ 
/*  720 */     LookAndFeel.installProperty(this.tree, "rowHeight", UIManager.get("Tree.rowHeight"));
/*      */ 
/*  723 */     this.largeModel = ((this.tree.isLargeModel()) && (this.tree.getRowHeight() > 0));
/*      */ 
/*  725 */     Object localObject1 = UIManager.get("Tree.scrollsOnExpand");
/*  726 */     if (localObject1 != null) {
/*  727 */       LookAndFeel.installProperty(this.tree, "scrollsOnExpand", localObject1);
/*      */     }
/*      */ 
/*  730 */     this.paintLines = UIManager.getBoolean("Tree.paintLines");
/*  731 */     this.lineTypeDashed = UIManager.getBoolean("Tree.lineTypeDashed");
/*      */ 
/*  733 */     Long localLong = (Long)UIManager.get("Tree.timeFactor");
/*  734 */     this.timeFactor = (localLong != null ? localLong.longValue() : 1000L);
/*      */ 
/*  736 */     Object localObject2 = UIManager.get("Tree.showsRootHandles");
/*  737 */     if (localObject2 != null)
/*  738 */       LookAndFeel.installProperty(this.tree, "showsRootHandles", localObject2);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  744 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null)
/*      */     {
/*  746 */       this.tree.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*  748 */     if ((this.mouseListener = createMouseListener()) != null) {
/*  749 */       this.tree.addMouseListener(this.mouseListener);
/*  750 */       if ((this.mouseListener instanceof MouseMotionListener)) {
/*  751 */         this.tree.addMouseMotionListener((MouseMotionListener)this.mouseListener);
/*      */       }
/*      */     }
/*  754 */     if ((this.focusListener = createFocusListener()) != null) {
/*  755 */       this.tree.addFocusListener(this.focusListener);
/*      */     }
/*  757 */     if ((this.keyListener = createKeyListener()) != null) {
/*  758 */       this.tree.addKeyListener(this.keyListener);
/*      */     }
/*  760 */     if ((this.treeExpansionListener = createTreeExpansionListener()) != null) {
/*  761 */       this.tree.addTreeExpansionListener(this.treeExpansionListener);
/*      */     }
/*  763 */     if (((this.treeModelListener = createTreeModelListener()) != null) && (this.treeModel != null))
/*      */     {
/*  765 */       this.treeModel.addTreeModelListener(this.treeModelListener);
/*      */     }
/*  767 */     if (((this.selectionModelPropertyChangeListener = createSelectionModelPropertyChangeListener()) != null) && (this.treeSelectionModel != null))
/*      */     {
/*  770 */       this.treeSelectionModel.addPropertyChangeListener(this.selectionModelPropertyChangeListener);
/*      */     }
/*      */ 
/*  773 */     if (((this.treeSelectionListener = createTreeSelectionListener()) != null) && (this.treeSelectionModel != null))
/*      */     {
/*  775 */       this.treeSelectionModel.addTreeSelectionListener(this.treeSelectionListener);
/*      */     }
/*      */ 
/*  778 */     TransferHandler localTransferHandler = this.tree.getTransferHandler();
/*  779 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource))) {
/*  780 */       this.tree.setTransferHandler(defaultTransferHandler);
/*      */ 
/*  783 */       if ((this.tree.getDropTarget() instanceof UIResource)) {
/*  784 */         this.tree.setDropTarget(null);
/*      */       }
/*      */     }
/*      */ 
/*  788 */     LookAndFeel.installProperty(this.tree, "opaque", Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions() {
/*  792 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  795 */     SwingUtilities.replaceUIInputMap(this.tree, 1, localInputMap);
/*      */ 
/*  798 */     localInputMap = getInputMap(0);
/*  799 */     SwingUtilities.replaceUIInputMap(this.tree, 0, localInputMap);
/*      */ 
/*  801 */     LazyActionMap.installLazyActionMap(this.tree, BasicTreeUI.class, "Tree.actionMap");
/*      */   }
/*      */ 
/*      */   InputMap getInputMap(int paramInt)
/*      */   {
/*  806 */     if (paramInt == 1) {
/*  807 */       return (InputMap)DefaultLookup.get(this.tree, this, "Tree.ancestorInputMap");
/*      */     }
/*      */ 
/*  810 */     if (paramInt == 0) {
/*  811 */       InputMap localInputMap1 = (InputMap)DefaultLookup.get(this.tree, this, "Tree.focusInputMap");
/*      */       InputMap localInputMap2;
/*  815 */       if ((this.tree.getComponentOrientation().isLeftToRight()) || ((localInputMap2 = (InputMap)DefaultLookup.get(this.tree, this, "Tree.focusInputMap.RightToLeft")) == null))
/*      */       {
/*  818 */         return localInputMap1;
/*      */       }
/*  820 */       localInputMap2.setParent(localInputMap1);
/*  821 */       return localInputMap2;
/*      */     }
/*      */ 
/*  824 */     return null;
/*      */   }
/*      */ 
/*      */   protected void installComponents()
/*      */   {
/*  831 */     if ((this.rendererPane = createCellRendererPane()) != null)
/*  832 */       this.tree.add(this.rendererPane);
/*      */   }
/*      */ 
/*      */   protected AbstractLayoutCache.NodeDimensions createNodeDimensions()
/*      */   {
/*  845 */     return new NodeDimensionsHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  853 */     return getHandler();
/*      */   }
/*      */ 
/*      */   private Handler getHandler() {
/*  857 */     if (this.handler == null) {
/*  858 */       this.handler = new Handler(null);
/*      */     }
/*  860 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected MouseListener createMouseListener()
/*      */   {
/*  868 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected FocusListener createFocusListener()
/*      */   {
/*  876 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected KeyListener createKeyListener()
/*      */   {
/*  884 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createSelectionModelPropertyChangeListener()
/*      */   {
/*  892 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected TreeSelectionListener createTreeSelectionListener()
/*      */   {
/*  900 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected CellEditorListener createCellEditorListener()
/*      */   {
/*  907 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected ComponentListener createComponentListener()
/*      */   {
/*  916 */     return new ComponentHandler();
/*      */   }
/*      */ 
/*      */   protected TreeExpansionListener createTreeExpansionListener()
/*      */   {
/*  924 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected AbstractLayoutCache createLayoutCache()
/*      */   {
/*  932 */     if ((isLargeModel()) && (getRowHeight() > 0)) {
/*  933 */       return new FixedHeightLayoutCache();
/*      */     }
/*  935 */     return new VariableHeightLayoutCache();
/*      */   }
/*      */ 
/*      */   protected CellRendererPane createCellRendererPane()
/*      */   {
/*  942 */     return new CellRendererPane();
/*      */   }
/*      */ 
/*      */   protected TreeCellEditor createDefaultCellEditor()
/*      */   {
/*  949 */     if ((this.currentCellRenderer != null) && ((this.currentCellRenderer instanceof DefaultTreeCellRenderer)))
/*      */     {
/*  951 */       DefaultTreeCellEditor localDefaultTreeCellEditor = new DefaultTreeCellEditor(this.tree, (DefaultTreeCellRenderer)this.currentCellRenderer);
/*      */ 
/*  954 */       return localDefaultTreeCellEditor;
/*      */     }
/*  956 */     return new DefaultTreeCellEditor(this.tree, null);
/*      */   }
/*      */ 
/*      */   protected TreeCellRenderer createDefaultCellRenderer()
/*      */   {
/*  964 */     return new DefaultTreeCellRenderer();
/*      */   }
/*      */ 
/*      */   protected TreeModelListener createTreeModelListener()
/*      */   {
/*  971 */     return getHandler();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  979 */     completeEditing();
/*      */ 
/*  981 */     prepareForUIUninstall();
/*      */ 
/*  983 */     uninstallDefaults();
/*  984 */     uninstallListeners();
/*  985 */     uninstallKeyboardActions();
/*  986 */     uninstallComponents();
/*      */ 
/*  988 */     completeUIUninstall();
/*      */   }
/*      */ 
/*      */   protected void prepareForUIUninstall() {
/*      */   }
/*      */ 
/*      */   protected void completeUIUninstall() {
/*  995 */     if (this.createdRenderer) {
/*  996 */       this.tree.setCellRenderer(null);
/*      */     }
/*  998 */     if (this.createdCellEditor) {
/*  999 */       this.tree.setCellEditor(null);
/*      */     }
/* 1001 */     this.cellEditor = null;
/* 1002 */     this.currentCellRenderer = null;
/* 1003 */     this.rendererPane = null;
/* 1004 */     this.componentListener = null;
/* 1005 */     this.propertyChangeListener = null;
/* 1006 */     this.mouseListener = null;
/* 1007 */     this.focusListener = null;
/* 1008 */     this.keyListener = null;
/* 1009 */     setSelectionModel(null);
/* 1010 */     this.treeState = null;
/* 1011 */     this.drawingCache = null;
/* 1012 */     this.selectionModelPropertyChangeListener = null;
/* 1013 */     this.tree = null;
/* 1014 */     this.treeModel = null;
/* 1015 */     this.treeSelectionModel = null;
/* 1016 */     this.treeSelectionListener = null;
/* 1017 */     this.treeExpansionListener = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults() {
/* 1021 */     if ((this.tree.getTransferHandler() instanceof UIResource))
/* 1022 */       this.tree.setTransferHandler(null);
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/* 1027 */     if (this.componentListener != null) {
/* 1028 */       this.tree.removeComponentListener(this.componentListener);
/*      */     }
/* 1030 */     if (this.propertyChangeListener != null) {
/* 1031 */       this.tree.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/* 1033 */     if (this.mouseListener != null) {
/* 1034 */       this.tree.removeMouseListener(this.mouseListener);
/* 1035 */       if ((this.mouseListener instanceof MouseMotionListener)) {
/* 1036 */         this.tree.removeMouseMotionListener((MouseMotionListener)this.mouseListener);
/*      */       }
/*      */     }
/* 1039 */     if (this.focusListener != null) {
/* 1040 */       this.tree.removeFocusListener(this.focusListener);
/*      */     }
/* 1042 */     if (this.keyListener != null) {
/* 1043 */       this.tree.removeKeyListener(this.keyListener);
/*      */     }
/* 1045 */     if (this.treeExpansionListener != null) {
/* 1046 */       this.tree.removeTreeExpansionListener(this.treeExpansionListener);
/*      */     }
/* 1048 */     if ((this.treeModel != null) && (this.treeModelListener != null)) {
/* 1049 */       this.treeModel.removeTreeModelListener(this.treeModelListener);
/*      */     }
/* 1051 */     if ((this.selectionModelPropertyChangeListener != null) && (this.treeSelectionModel != null))
/*      */     {
/* 1053 */       this.treeSelectionModel.removePropertyChangeListener(this.selectionModelPropertyChangeListener);
/*      */     }
/*      */ 
/* 1056 */     if ((this.treeSelectionListener != null) && (this.treeSelectionModel != null)) {
/* 1057 */       this.treeSelectionModel.removeTreeSelectionListener(this.treeSelectionListener);
/*      */     }
/*      */ 
/* 1060 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions() {
/* 1064 */     SwingUtilities.replaceUIActionMap(this.tree, null);
/* 1065 */     SwingUtilities.replaceUIInputMap(this.tree, 1, null);
/*      */ 
/* 1068 */     SwingUtilities.replaceUIInputMap(this.tree, 0, null);
/*      */   }
/*      */ 
/*      */   protected void uninstallComponents()
/*      */   {
/* 1075 */     if (this.rendererPane != null)
/* 1076 */       this.tree.remove(this.rendererPane);
/*      */   }
/*      */ 
/*      */   private void redoTheLayout()
/*      */   {
/* 1084 */     if (this.treeState != null)
/* 1085 */       this.treeState.invalidateSizes();
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/* 1098 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 1099 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 1100 */     Component localComponent = (Component)localUIDefaults.get(BASELINE_COMPONENT_KEY);
/*      */ 
/* 1102 */     if (localComponent == null) {
/* 1103 */       TreeCellRenderer localTreeCellRenderer = createDefaultCellRenderer();
/* 1104 */       localComponent = localTreeCellRenderer.getTreeCellRendererComponent(this.tree, "a", false, false, false, -1, false);
/*      */ 
/* 1106 */       localUIDefaults.put(BASELINE_COMPONENT_KEY, localComponent);
/*      */     }
/* 1108 */     int i = this.tree.getRowHeight();
/*      */     int j;
/* 1110 */     if (i > 0) {
/* 1111 */       j = localComponent.getBaseline(2147483647, i);
/*      */     }
/*      */     else {
/* 1114 */       Dimension localDimension = localComponent.getPreferredSize();
/* 1115 */       j = localComponent.getBaseline(localDimension.width, localDimension.height);
/*      */     }
/* 1117 */     return j + this.tree.getInsets().top;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/* 1130 */     super.getBaselineResizeBehavior(paramJComponent);
/* 1131 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/* 1139 */     if (this.tree != paramJComponent) {
/* 1140 */       throw new InternalError("incorrect component");
/*      */     }
/*      */ 
/* 1144 */     if (this.treeState == null) {
/* 1145 */       return;
/*      */     }
/*      */ 
/* 1148 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/* 1149 */     Insets localInsets = this.tree.getInsets();
/* 1150 */     TreePath localTreePath1 = getClosestPathForLocation(this.tree, 0, localRectangle1.y);
/*      */ 
/* 1152 */     Enumeration localEnumeration = this.treeState.getVisiblePathsFrom(localTreePath1);
/*      */ 
/* 1154 */     int i = this.treeState.getRowForPath(localTreePath1);
/* 1155 */     int j = localRectangle1.y + localRectangle1.height;
/*      */ 
/* 1157 */     this.drawingCache.clear();
/*      */ 
/* 1159 */     if ((localTreePath1 != null) && (localEnumeration != null)) {
/* 1160 */       TreePath localTreePath2 = localTreePath1;
/*      */ 
/* 1165 */       localTreePath2 = localTreePath2.getParentPath();
/* 1166 */       while (localTreePath2 != null) {
/* 1167 */         paintVerticalPartOfLeg(paramGraphics, localRectangle1, localInsets, localTreePath2);
/* 1168 */         this.drawingCache.put(localTreePath2, Boolean.TRUE);
/* 1169 */         localTreePath2 = localTreePath2.getParentPath();
/*      */       }
/*      */ 
/* 1172 */       int k = 0;
/*      */ 
/* 1177 */       Rectangle localRectangle2 = new Rectangle();
/*      */ 
/* 1180 */       boolean bool4 = isRootVisible();
/*      */ 
/* 1182 */       while ((k == 0) && (localEnumeration.hasMoreElements())) {
/* 1183 */         TreePath localTreePath3 = (TreePath)localEnumeration.nextElement();
/* 1184 */         if (localTreePath3 != null) {
/* 1185 */           boolean bool3 = this.treeModel.isLeaf(localTreePath3.getLastPathComponent());
/*      */           boolean bool2;
/*      */           boolean bool1;
/* 1186 */           if (bool3) {
/* 1187 */             bool1 = bool2 = 0;
/*      */           } else {
/* 1189 */             bool1 = this.treeState.getExpandedState(localTreePath3);
/* 1190 */             bool2 = this.tree.hasBeenExpanded(localTreePath3);
/*      */           }
/* 1192 */           Rectangle localRectangle3 = getPathBounds(localTreePath3, localInsets, localRectangle2);
/* 1193 */           if (localRectangle3 == null)
/*      */           {
/* 1198 */             return;
/*      */           }
/* 1200 */           localTreePath2 = localTreePath3.getParentPath();
/* 1201 */           if (localTreePath2 != null) {
/* 1202 */             if (this.drawingCache.get(localTreePath2) == null) {
/* 1203 */               paintVerticalPartOfLeg(paramGraphics, localRectangle1, localInsets, localTreePath2);
/*      */ 
/* 1205 */               this.drawingCache.put(localTreePath2, Boolean.TRUE);
/*      */             }
/* 1207 */             paintHorizontalPartOfLeg(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath3, i, bool1, bool2, bool3);
/*      */           }
/* 1212 */           else if ((bool4) && (i == 0)) {
/* 1213 */             paintHorizontalPartOfLeg(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath3, i, bool1, bool2, bool3);
/*      */           }
/*      */ 
/* 1218 */           if (shouldPaintExpandControl(localTreePath3, i, bool1, bool2, bool3))
/*      */           {
/* 1220 */             paintExpandControl(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath3, i, bool1, bool2, bool3);
/*      */           }
/*      */ 
/* 1224 */           paintRow(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath3, i, bool1, bool2, bool3);
/*      */ 
/* 1226 */           if (localRectangle3.y + localRectangle3.height >= j)
/* 1227 */             k = 1;
/*      */         }
/*      */         else {
/* 1230 */           k = 1;
/*      */         }
/* 1232 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1236 */     paintDropLine(paramGraphics);
/*      */ 
/* 1239 */     this.rendererPane.removeAll();
/*      */ 
/* 1241 */     this.drawingCache.clear();
/*      */   }
/*      */ 
/*      */   protected boolean isDropLine(JTree.DropLocation paramDropLocation)
/*      */   {
/* 1254 */     return (paramDropLocation != null) && (paramDropLocation.getPath() != null) && (paramDropLocation.getChildIndex() != -1);
/*      */   }
/*      */ 
/*      */   protected void paintDropLine(Graphics paramGraphics)
/*      */   {
/* 1264 */     JTree.DropLocation localDropLocation = this.tree.getDropLocation();
/* 1265 */     if (!isDropLine(localDropLocation)) {
/* 1266 */       return;
/*      */     }
/*      */ 
/* 1269 */     Color localColor = UIManager.getColor("Tree.dropLineColor");
/* 1270 */     if (localColor != null) {
/* 1271 */       paramGraphics.setColor(localColor);
/* 1272 */       Rectangle localRectangle = getDropLineRect(localDropLocation);
/* 1273 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Rectangle getDropLineRect(JTree.DropLocation paramDropLocation)
/*      */   {
/* 1286 */     TreePath localTreePath1 = paramDropLocation.getPath();
/* 1287 */     int i = paramDropLocation.getChildIndex();
/* 1288 */     boolean bool = this.leftToRight;
/*      */ 
/* 1290 */     Insets localInsets = this.tree.getInsets();
/*      */     Rectangle localRectangle1;
/* 1292 */     if (this.tree.getRowCount() == 0) {
/* 1293 */       localRectangle1 = new Rectangle(localInsets.left, localInsets.top, this.tree.getWidth() - localInsets.left - localInsets.right, 0);
/*      */     }
/*      */     else
/*      */     {
/* 1298 */       TreeModel localTreeModel = getModel();
/* 1299 */       Object localObject = localTreeModel.getRoot();
/*      */ 
/* 1301 */       if ((localTreePath1.getLastPathComponent() == localObject) && (i >= localTreeModel.getChildCount(localObject)))
/*      */       {
/* 1304 */         localRectangle1 = this.tree.getRowBounds(this.tree.getRowCount() - 1);
/* 1305 */         localRectangle1.y += localRectangle1.height;
/*      */         Rectangle localRectangle2;
/* 1308 */         if (!this.tree.isRootVisible()) {
/* 1309 */           localRectangle2 = this.tree.getRowBounds(0);
/* 1310 */         } else if (localTreeModel.getChildCount(localObject) == 0) {
/* 1311 */           localRectangle2 = this.tree.getRowBounds(0);
/* 1312 */           localRectangle2.x += this.totalChildIndent;
/* 1313 */           localRectangle2.width -= this.totalChildIndent + this.totalChildIndent;
/*      */         } else {
/* 1315 */           TreePath localTreePath2 = localTreePath1.pathByAddingChild(localTreeModel.getChild(localObject, localTreeModel.getChildCount(localObject) - 1));
/*      */ 
/* 1317 */           localRectangle2 = this.tree.getPathBounds(localTreePath2);
/*      */         }
/*      */ 
/* 1320 */         localRectangle1.x = localRectangle2.x;
/* 1321 */         localRectangle1.width = localRectangle2.width;
/*      */       } else {
/* 1323 */         localRectangle1 = this.tree.getPathBounds(localTreePath1.pathByAddingChild(localTreeModel.getChild(localTreePath1.getLastPathComponent(), i)));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1328 */     if (localRectangle1.y != 0) {
/* 1329 */       localRectangle1.y -= 1;
/*      */     }
/*      */ 
/* 1332 */     if (!bool) {
/* 1333 */       localRectangle1.x = (localRectangle1.x + localRectangle1.width - 100);
/*      */     }
/*      */ 
/* 1336 */     localRectangle1.width = 100;
/* 1337 */     localRectangle1.height = 2;
/*      */ 
/* 1339 */     return localRectangle1;
/*      */   }
/*      */ 
/*      */   protected void paintHorizontalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 1353 */     if (!this.paintLines) {
/* 1354 */       return;
/*      */     }
/*      */ 
/* 1358 */     int i = paramTreePath.getPathCount() - 1;
/* 1359 */     if (((i == 0) || ((i == 1) && (!isRootVisible()))) && (!getShowsRootHandles()))
/*      */     {
/* 1361 */       return;
/*      */     }
/*      */ 
/* 1364 */     int j = paramRectangle1.x;
/* 1365 */     int k = paramRectangle1.x + paramRectangle1.width;
/* 1366 */     int m = paramRectangle1.y;
/* 1367 */     int n = paramRectangle1.y + paramRectangle1.height;
/* 1368 */     int i1 = paramRectangle2.y + paramRectangle2.height / 2;
/*      */     int i2;
/*      */     int i3;
/* 1370 */     if (this.leftToRight) {
/* 1371 */       i2 = paramRectangle2.x - getRightChildIndent();
/* 1372 */       i3 = paramRectangle2.x - getHorizontalLegBuffer();
/*      */ 
/* 1374 */       if ((i1 >= m) && (i1 < n) && (i3 >= j) && (i2 < k) && (i2 < i3))
/*      */       {
/* 1380 */         paramGraphics.setColor(getHashColor());
/* 1381 */         paintHorizontalLine(paramGraphics, this.tree, i1, i2, i3 - 1);
/*      */       }
/*      */     } else {
/* 1384 */       i2 = paramRectangle2.x + paramRectangle2.width + getHorizontalLegBuffer();
/* 1385 */       i3 = paramRectangle2.x + paramRectangle2.width + getRightChildIndent();
/*      */ 
/* 1387 */       if ((i1 >= m) && (i1 < n) && (i3 >= j) && (i2 < k) && (i2 < i3))
/*      */       {
/* 1393 */         paramGraphics.setColor(getHashColor());
/* 1394 */         paintHorizontalLine(paramGraphics, this.tree, i1, i2, i3 - 1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintVerticalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle, Insets paramInsets, TreePath paramTreePath)
/*      */   {
/* 1405 */     if (!this.paintLines) {
/* 1406 */       return;
/*      */     }
/*      */ 
/* 1409 */     int i = paramTreePath.getPathCount() - 1;
/* 1410 */     if ((i == 0) && (!getShowsRootHandles()) && (!isRootVisible())) {
/* 1411 */       return;
/*      */     }
/* 1413 */     int j = getRowX(-1, i + 1);
/* 1414 */     if (this.leftToRight) {
/* 1415 */       j = j - getRightChildIndent() + paramInsets.left;
/*      */     }
/*      */     else {
/* 1418 */       j = this.tree.getWidth() - j - paramInsets.right + getRightChildIndent() - 1;
/*      */     }
/*      */ 
/* 1421 */     int k = paramRectangle.x;
/* 1422 */     int m = paramRectangle.x + (paramRectangle.width - 1);
/*      */ 
/* 1424 */     if ((j >= k) && (j <= m)) {
/* 1425 */       int n = paramRectangle.y;
/* 1426 */       int i1 = paramRectangle.y + paramRectangle.height;
/* 1427 */       Rectangle localRectangle1 = getPathBounds(this.tree, paramTreePath);
/* 1428 */       Rectangle localRectangle2 = getPathBounds(this.tree, getLastChildPath(paramTreePath));
/*      */ 
/* 1431 */       if (localRectangle2 == null)
/*      */         return;
/*      */       int i2;
/* 1440 */       if (localRectangle1 == null) {
/* 1441 */         i2 = Math.max(paramInsets.top + getVerticalLegBuffer(), n);
/*      */       }
/*      */       else
/*      */       {
/* 1445 */         i2 = Math.max(localRectangle1.y + localRectangle1.height + getVerticalLegBuffer(), n);
/*      */       }
/* 1447 */       if ((i == 0) && (!isRootVisible())) {
/* 1448 */         TreeModel localTreeModel = getModel();
/*      */ 
/* 1450 */         if (localTreeModel != null) {
/* 1451 */           Object localObject = localTreeModel.getRoot();
/*      */ 
/* 1453 */           if (localTreeModel.getChildCount(localObject) > 0) {
/* 1454 */             localRectangle1 = getPathBounds(this.tree, paramTreePath.pathByAddingChild(localTreeModel.getChild(localObject, 0)));
/*      */ 
/* 1456 */             if (localRectangle1 != null) {
/* 1457 */               i2 = Math.max(paramInsets.top + getVerticalLegBuffer(), localRectangle1.y + localRectangle1.height / 2);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1464 */       int i3 = Math.min(localRectangle2.y + localRectangle2.height / 2, i1);
/*      */ 
/* 1467 */       if (i2 <= i3) {
/* 1468 */         paramGraphics.setColor(getHashColor());
/* 1469 */         paintVerticalLine(paramGraphics, this.tree, j, i2, i3);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintExpandControl(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 1484 */     Object localObject = paramTreePath.getLastPathComponent();
/*      */ 
/* 1488 */     if ((!paramBoolean3) && ((!paramBoolean2) || (this.treeModel.getChildCount(localObject) > 0)))
/*      */     {
/*      */       int i;
/* 1491 */       if (this.leftToRight)
/* 1492 */         i = paramRectangle2.x - getRightChildIndent() + 1;
/*      */       else {
/* 1494 */         i = paramRectangle2.x + paramRectangle2.width + getRightChildIndent() - 1;
/*      */       }
/* 1496 */       int j = paramRectangle2.y + paramRectangle2.height / 2;
/*      */       Icon localIcon;
/* 1498 */       if (paramBoolean1) {
/* 1499 */         localIcon = getExpandedIcon();
/* 1500 */         if (localIcon != null)
/* 1501 */           drawCentered(this.tree, paramGraphics, localIcon, i, j);
/*      */       }
/*      */       else
/*      */       {
/* 1505 */         localIcon = getCollapsedIcon();
/* 1506 */         if (localIcon != null)
/* 1507 */           drawCentered(this.tree, paramGraphics, localIcon, i, j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintRow(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 1522 */     if ((this.editingComponent != null) && (this.editingRow == paramInt))
/*      */       return;
/*      */     int i;
/* 1527 */     if (this.tree.hasFocus()) {
/* 1528 */       i = getLeadSelectionRow();
/*      */     }
/*      */     else {
/* 1531 */       i = -1;
/*      */     }
/*      */ 
/* 1535 */     Component localComponent = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, paramTreePath.getLastPathComponent(), this.tree.isRowSelected(paramInt), paramBoolean1, paramBoolean3, paramInt, i == paramInt);
/*      */ 
/* 1540 */     this.rendererPane.paintComponent(paramGraphics, localComponent, this.tree, paramRectangle2.x, paramRectangle2.y, paramRectangle2.width, paramRectangle2.height, true);
/*      */   }
/*      */ 
/*      */   protected boolean shouldPaintExpandControl(TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 1552 */     if (paramBoolean3) {
/* 1553 */       return false;
/*      */     }
/* 1555 */     int i = paramTreePath.getPathCount() - 1;
/*      */ 
/* 1557 */     if (((i == 0) || ((i == 1) && (!isRootVisible()))) && (!getShowsRootHandles()))
/*      */     {
/* 1559 */       return false;
/* 1560 */     }return true;
/*      */   }
/*      */ 
/*      */   protected void paintVerticalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1568 */     if (this.lineTypeDashed)
/* 1569 */       drawDashedVerticalLine(paramGraphics, paramInt1, paramInt2, paramInt3);
/*      */     else
/* 1571 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt1, paramInt3);
/*      */   }
/*      */ 
/*      */   protected void paintHorizontalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1580 */     if (this.lineTypeDashed)
/* 1581 */       drawDashedHorizontalLine(paramGraphics, paramInt1, paramInt2, paramInt3);
/*      */     else
/* 1583 */       paramGraphics.drawLine(paramInt2, paramInt1, paramInt3, paramInt1);
/*      */   }
/*      */ 
/*      */   protected int getVerticalLegBuffer()
/*      */   {
/* 1592 */     return 0;
/*      */   }
/*      */ 
/*      */   protected int getHorizontalLegBuffer()
/*      */   {
/* 1601 */     return 0;
/*      */   }
/*      */ 
/*      */   private int findCenteredX(int paramInt1, int paramInt2) {
/* 1605 */     return this.leftToRight ? paramInt1 - (int)Math.ceil(paramInt2 / 2.0D) : paramInt1 - (int)Math.floor(paramInt2 / 2.0D);
/*      */   }
/*      */ 
/*      */   protected void drawCentered(Component paramComponent, Graphics paramGraphics, Icon paramIcon, int paramInt1, int paramInt2)
/*      */   {
/* 1617 */     paramIcon.paintIcon(paramComponent, paramGraphics, findCenteredX(paramInt1, paramIcon.getIconWidth()), paramInt2 - paramIcon.getIconHeight() / 2);
/*      */   }
/*      */ 
/*      */   protected void drawDashedHorizontalLine(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1628 */     paramInt2 += paramInt2 % 2;
/*      */ 
/* 1630 */     for (int i = paramInt2; i <= paramInt3; i += 2)
/* 1631 */       paramGraphics.drawLine(i, paramInt1, i, paramInt1);
/*      */   }
/*      */ 
/*      */   protected void drawDashedVerticalLine(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 1641 */     paramInt2 += paramInt2 % 2;
/*      */ 
/* 1643 */     for (int i = paramInt2; i <= paramInt3; i += 2)
/* 1644 */       paramGraphics.drawLine(paramInt1, i, paramInt1, i);
/*      */   }
/*      */ 
/*      */   protected int getRowX(int paramInt1, int paramInt2)
/*      */   {
/* 1665 */     return this.totalChildIndent * (paramInt2 + this.depthOffset);
/*      */   }
/*      */ 
/*      */   protected void updateLayoutCacheExpandedNodes()
/*      */   {
/* 1673 */     if ((this.treeModel != null) && (this.treeModel.getRoot() != null))
/* 1674 */       updateExpandedDescendants(new TreePath(this.treeModel.getRoot()));
/*      */   }
/*      */ 
/*      */   private void updateLayoutCacheExpandedNodesIfNecessary() {
/* 1678 */     if ((this.treeModel != null) && (this.treeModel.getRoot() != null)) {
/* 1679 */       TreePath localTreePath = new TreePath(this.treeModel.getRoot());
/* 1680 */       if (this.tree.isExpanded(localTreePath))
/* 1681 */         updateLayoutCacheExpandedNodes();
/*      */       else
/* 1683 */         this.treeState.setExpandedState(localTreePath, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateExpandedDescendants(TreePath paramTreePath)
/*      */   {
/* 1694 */     completeEditing();
/* 1695 */     if (this.treeState != null) {
/* 1696 */       this.treeState.setExpandedState(paramTreePath, true);
/*      */ 
/* 1698 */       Enumeration localEnumeration = this.tree.getExpandedDescendants(paramTreePath);
/*      */ 
/* 1700 */       if (localEnumeration != null) {
/* 1701 */         while (localEnumeration.hasMoreElements()) {
/* 1702 */           paramTreePath = (TreePath)localEnumeration.nextElement();
/* 1703 */           this.treeState.setExpandedState(paramTreePath, true);
/*      */         }
/*      */       }
/* 1706 */       updateLeadSelectionRow();
/* 1707 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected TreePath getLastChildPath(TreePath paramTreePath)
/*      */   {
/* 1715 */     if (this.treeModel != null) {
/* 1716 */       int i = this.treeModel.getChildCount(paramTreePath.getLastPathComponent());
/*      */ 
/* 1719 */       if (i > 0) {
/* 1720 */         return paramTreePath.pathByAddingChild(this.treeModel.getChild(paramTreePath.getLastPathComponent(), i - 1));
/*      */       }
/*      */     }
/* 1723 */     return null;
/*      */   }
/*      */ 
/*      */   protected void updateDepthOffset()
/*      */   {
/* 1730 */     if (isRootVisible()) {
/* 1731 */       if (getShowsRootHandles())
/* 1732 */         this.depthOffset = 1;
/*      */       else
/* 1734 */         this.depthOffset = 0;
/*      */     }
/* 1736 */     else if (!getShowsRootHandles())
/* 1737 */       this.depthOffset = -1;
/*      */     else
/* 1739 */       this.depthOffset = 0;
/*      */   }
/*      */ 
/*      */   protected void updateCellEditor()
/*      */   {
/* 1750 */     completeEditing();
/*      */     TreeCellEditor localTreeCellEditor;
/* 1751 */     if (this.tree == null) {
/* 1752 */       localTreeCellEditor = null;
/*      */     }
/* 1754 */     else if (this.tree.isEditable()) {
/* 1755 */       localTreeCellEditor = this.tree.getCellEditor();
/* 1756 */       if (localTreeCellEditor == null) {
/* 1757 */         localTreeCellEditor = createDefaultCellEditor();
/* 1758 */         if (localTreeCellEditor != null) {
/* 1759 */           this.tree.setCellEditor(localTreeCellEditor);
/* 1760 */           this.createdCellEditor = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1765 */       localTreeCellEditor = null;
/*      */     }
/* 1767 */     if (localTreeCellEditor != this.cellEditor) {
/* 1768 */       if ((this.cellEditor != null) && (this.cellEditorListener != null))
/* 1769 */         this.cellEditor.removeCellEditorListener(this.cellEditorListener);
/* 1770 */       this.cellEditor = localTreeCellEditor;
/* 1771 */       if (this.cellEditorListener == null)
/* 1772 */         this.cellEditorListener = createCellEditorListener();
/* 1773 */       if ((localTreeCellEditor != null) && (this.cellEditorListener != null))
/* 1774 */         localTreeCellEditor.addCellEditorListener(this.cellEditorListener);
/* 1775 */       this.createdCellEditor = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateRenderer()
/*      */   {
/* 1783 */     if (this.tree != null)
/*      */     {
/* 1786 */       TreeCellRenderer localTreeCellRenderer = this.tree.getCellRenderer();
/* 1787 */       if (localTreeCellRenderer == null) {
/* 1788 */         this.tree.setCellRenderer(createDefaultCellRenderer());
/* 1789 */         this.createdRenderer = true;
/*      */       }
/*      */       else {
/* 1792 */         this.createdRenderer = false;
/* 1793 */         this.currentCellRenderer = localTreeCellRenderer;
/* 1794 */         if (this.createdCellEditor)
/* 1795 */           this.tree.setCellEditor(null);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1800 */       this.createdRenderer = false;
/* 1801 */       this.currentCellRenderer = null;
/*      */     }
/* 1803 */     updateCellEditor();
/*      */   }
/*      */ 
/*      */   protected void configureLayoutCache()
/*      */   {
/* 1811 */     if ((this.treeState != null) && (this.tree != null)) {
/* 1812 */       if (this.nodeDimensions == null)
/* 1813 */         this.nodeDimensions = createNodeDimensions();
/* 1814 */       this.treeState.setNodeDimensions(this.nodeDimensions);
/* 1815 */       this.treeState.setRootVisible(this.tree.isRootVisible());
/* 1816 */       this.treeState.setRowHeight(this.tree.getRowHeight());
/* 1817 */       this.treeState.setSelectionModel(getSelectionModel());
/*      */ 
/* 1820 */       if (this.treeState.getModel() != this.tree.getModel())
/* 1821 */         this.treeState.setModel(this.tree.getModel());
/* 1822 */       updateLayoutCacheExpandedNodesIfNecessary();
/*      */ 
/* 1825 */       if (isLargeModel()) {
/* 1826 */         if (this.componentListener == null) {
/* 1827 */           this.componentListener = createComponentListener();
/* 1828 */           if (this.componentListener != null)
/* 1829 */             this.tree.addComponentListener(this.componentListener);
/*      */         }
/*      */       }
/* 1832 */       else if (this.componentListener != null) {
/* 1833 */         this.tree.removeComponentListener(this.componentListener);
/* 1834 */         this.componentListener = null;
/*      */       }
/*      */     }
/* 1837 */     else if (this.componentListener != null) {
/* 1838 */       this.tree.removeComponentListener(this.componentListener);
/* 1839 */       this.componentListener = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateSize()
/*      */   {
/* 1848 */     this.validCachedPreferredSize = false;
/* 1849 */     this.tree.treeDidChange();
/*      */   }
/*      */ 
/*      */   private void updateSize0() {
/* 1853 */     this.validCachedPreferredSize = false;
/* 1854 */     this.tree.revalidate();
/*      */   }
/*      */ 
/*      */   protected void updateCachedPreferredSize()
/*      */   {
/* 1865 */     if (this.treeState != null) {
/* 1866 */       Insets localInsets = this.tree.getInsets();
/*      */ 
/* 1868 */       if (isLargeModel()) {
/* 1869 */         Rectangle localRectangle = this.tree.getVisibleRect();
/*      */ 
/* 1871 */         if ((localRectangle.x == 0) && (localRectangle.y == 0) && (localRectangle.width == 0) && (localRectangle.height == 0) && (this.tree.getVisibleRowCount() > 0))
/*      */         {
/* 1876 */           localRectangle.width = 1;
/* 1877 */           localRectangle.height = (this.tree.getRowHeight() * this.tree.getVisibleRowCount());
/*      */         }
/*      */         else {
/* 1880 */           localRectangle.x -= localInsets.left;
/* 1881 */           localRectangle.y -= localInsets.top;
/*      */         }
/*      */ 
/* 1884 */         Container localContainer = SwingUtilities.getUnwrappedParent(this.tree);
/* 1885 */         if ((localContainer instanceof JViewport)) {
/* 1886 */           localContainer = localContainer.getParent();
/* 1887 */           if ((localContainer instanceof JScrollPane)) {
/* 1888 */             JScrollPane localJScrollPane = (JScrollPane)localContainer;
/* 1889 */             JScrollBar localJScrollBar = localJScrollPane.getHorizontalScrollBar();
/* 1890 */             if ((localJScrollBar != null) && (localJScrollBar.isVisible())) {
/* 1891 */               int i = localJScrollBar.getHeight();
/* 1892 */               localRectangle.y -= i;
/* 1893 */               localRectangle.height += i;
/*      */             }
/*      */           }
/*      */         }
/* 1897 */         this.preferredSize.width = this.treeState.getPreferredWidth(localRectangle);
/*      */       }
/*      */       else {
/* 1900 */         this.preferredSize.width = this.treeState.getPreferredWidth(null);
/*      */       }
/* 1902 */       this.preferredSize.height = this.treeState.getPreferredHeight();
/* 1903 */       this.preferredSize.width += localInsets.left + localInsets.right;
/* 1904 */       this.preferredSize.height += localInsets.top + localInsets.bottom;
/*      */     }
/* 1906 */     this.validCachedPreferredSize = true;
/*      */   }
/*      */ 
/*      */   protected void pathWasExpanded(TreePath paramTreePath)
/*      */   {
/* 1913 */     if (this.tree != null)
/* 1914 */       this.tree.fireTreeExpanded(paramTreePath);
/*      */   }
/*      */ 
/*      */   protected void pathWasCollapsed(TreePath paramTreePath)
/*      */   {
/* 1922 */     if (this.tree != null)
/* 1923 */       this.tree.fireTreeCollapsed(paramTreePath);
/*      */   }
/*      */ 
/*      */   protected void ensureRowsAreVisible(int paramInt1, int paramInt2)
/*      */   {
/* 1932 */     if ((this.tree != null) && (paramInt1 >= 0) && (paramInt2 < getRowCount(this.tree))) {
/* 1933 */       boolean bool = DefaultLookup.getBoolean(this.tree, this, "Tree.scrollsHorizontallyAndVertically", false);
/*      */       Rectangle localRectangle1;
/* 1935 */       if (paramInt1 == paramInt2) {
/* 1936 */         localRectangle1 = getPathBounds(this.tree, getPathForRow(this.tree, paramInt1));
/*      */ 
/* 1939 */         if (localRectangle1 != null) {
/* 1940 */           if (!bool) {
/* 1941 */             localRectangle1.x = this.tree.getVisibleRect().x;
/* 1942 */             localRectangle1.width = 1;
/*      */           }
/* 1944 */           this.tree.scrollRectToVisible(localRectangle1);
/*      */         }
/*      */       }
/*      */       else {
/* 1948 */         localRectangle1 = getPathBounds(this.tree, getPathForRow(this.tree, paramInt1));
/*      */ 
/* 1950 */         if (localRectangle1 != null) {
/* 1951 */           Rectangle localRectangle2 = this.tree.getVisibleRect();
/* 1952 */           Rectangle localRectangle3 = localRectangle1;
/* 1953 */           int i = localRectangle1.y;
/* 1954 */           int j = i + localRectangle2.height;
/*      */ 
/* 1956 */           for (int k = paramInt1 + 1; k <= paramInt2; k++) {
/* 1957 */             localRectangle3 = getPathBounds(this.tree, getPathForRow(this.tree, k));
/*      */ 
/* 1959 */             if (localRectangle3 == null) {
/* 1960 */               return;
/*      */             }
/* 1962 */             if (localRectangle3.y + localRectangle3.height > j)
/* 1963 */               k = paramInt2;
/*      */           }
/* 1965 */           this.tree.scrollRectToVisible(new Rectangle(localRectangle2.x, i, 1, localRectangle3.y + localRectangle3.height - i));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPreferredMinSize(Dimension paramDimension)
/*      */   {
/* 1976 */     this.preferredMinSize = paramDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredMinSize()
/*      */   {
/* 1982 */     if (this.preferredMinSize == null)
/* 1983 */       return null;
/* 1984 */     return new Dimension(this.preferredMinSize);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/* 1991 */     return getPreferredSize(paramJComponent, true);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent, boolean paramBoolean)
/*      */   {
/* 2000 */     Dimension localDimension = getPreferredMinSize();
/*      */ 
/* 2002 */     if (!this.validCachedPreferredSize)
/* 2003 */       updateCachedPreferredSize();
/* 2004 */     if (this.tree != null) {
/* 2005 */       if (localDimension != null) {
/* 2006 */         return new Dimension(Math.max(localDimension.width, this.preferredSize.width), Math.max(localDimension.height, this.preferredSize.height));
/*      */       }
/*      */ 
/* 2009 */       return new Dimension(this.preferredSize.width, this.preferredSize.height);
/*      */     }
/* 2011 */     if (localDimension != null) {
/* 2012 */       return localDimension;
/*      */     }
/* 2014 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/* 2022 */     if (getPreferredMinSize() != null)
/* 2023 */       return getPreferredMinSize();
/* 2024 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/* 2032 */     if (this.tree != null)
/* 2033 */       return getPreferredSize(this.tree);
/* 2034 */     if (getPreferredMinSize() != null)
/* 2035 */       return getPreferredMinSize();
/* 2036 */     return new Dimension(0, 0);
/*      */   }
/*      */ 
/*      */   protected void completeEditing()
/*      */   {
/* 2050 */     if ((this.tree.getInvokesStopCellEditing()) && (this.stopEditingInCompleteEditing) && (this.editingComponent != null))
/*      */     {
/* 2052 */       this.cellEditor.stopCellEditing();
/*      */     }
/*      */ 
/* 2056 */     completeEditing(false, true, false);
/*      */   }
/*      */ 
/*      */   protected void completeEditing(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/* 2068 */     if ((this.stopEditingInCompleteEditing) && (this.editingComponent != null)) {
/* 2069 */       Component localComponent = this.editingComponent;
/* 2070 */       TreePath localTreePath = this.editingPath;
/* 2071 */       TreeCellEditor localTreeCellEditor = this.cellEditor;
/* 2072 */       Object localObject = localTreeCellEditor.getCellEditorValue();
/* 2073 */       Rectangle localRectangle = getPathBounds(this.tree, this.editingPath);
/*      */ 
/* 2075 */       int i = (this.tree != null) && ((this.tree.hasFocus()) || (SwingUtilities.findFocusOwner(this.editingComponent) != null)) ? 1 : 0;
/*      */ 
/* 2079 */       this.editingComponent = null;
/* 2080 */       this.editingPath = null;
/* 2081 */       if (paramBoolean1)
/* 2082 */         localTreeCellEditor.stopCellEditing();
/* 2083 */       else if (paramBoolean2)
/* 2084 */         localTreeCellEditor.cancelCellEditing();
/* 2085 */       this.tree.remove(localComponent);
/* 2086 */       if (this.editorHasDifferentSize) {
/* 2087 */         this.treeState.invalidatePathBounds(localTreePath);
/* 2088 */         updateSize();
/*      */       }
/* 2090 */       else if (localRectangle != null) {
/* 2091 */         localRectangle.x = 0;
/* 2092 */         localRectangle.width = this.tree.getSize().width;
/* 2093 */         this.tree.repaint(localRectangle);
/*      */       }
/* 2095 */       if (i != 0)
/* 2096 */         this.tree.requestFocus();
/* 2097 */       if (paramBoolean3)
/* 2098 */         this.treeModel.valueForPathChanged(localTreePath, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean startEditingOnRelease(TreePath paramTreePath, MouseEvent paramMouseEvent1, MouseEvent paramMouseEvent2)
/*      */   {
/* 2107 */     this.releaseEvent = paramMouseEvent2;
/*      */     try {
/* 2109 */       return startEditing(paramTreePath, paramMouseEvent1);
/*      */     } finally {
/* 2111 */       this.releaseEvent = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean startEditing(TreePath paramTreePath, MouseEvent paramMouseEvent)
/*      */   {
/* 2121 */     if ((isEditing(this.tree)) && (this.tree.getInvokesStopCellEditing()) && (!stopEditing(this.tree)))
/*      */     {
/* 2123 */       return false;
/*      */     }
/* 2125 */     completeEditing();
/* 2126 */     if ((this.cellEditor != null) && (this.tree.isPathEditable(paramTreePath))) {
/* 2127 */       int i = getRowForPath(this.tree, paramTreePath);
/*      */ 
/* 2129 */       if (this.cellEditor.isCellEditable(paramMouseEvent)) {
/* 2130 */         this.editingComponent = this.cellEditor.getTreeCellEditorComponent(this.tree, paramTreePath.getLastPathComponent(), this.tree.isPathSelected(paramTreePath), this.tree.isExpanded(paramTreePath), this.treeModel.isLeaf(paramTreePath.getLastPathComponent()), i);
/*      */ 
/* 2134 */         Rectangle localRectangle = getPathBounds(this.tree, paramTreePath);
/* 2135 */         if (localRectangle == null) {
/* 2136 */           return false;
/*      */         }
/*      */ 
/* 2139 */         this.editingRow = i;
/*      */ 
/* 2141 */         Dimension localDimension = this.editingComponent.getPreferredSize();
/*      */ 
/* 2144 */         if ((localDimension.height != localRectangle.height) && (getRowHeight() > 0))
/*      */         {
/* 2146 */           localDimension.height = getRowHeight();
/*      */         }
/* 2148 */         if ((localDimension.width != localRectangle.width) || (localDimension.height != localRectangle.height))
/*      */         {
/* 2152 */           this.editorHasDifferentSize = true;
/* 2153 */           this.treeState.invalidatePathBounds(paramTreePath);
/* 2154 */           updateSize();
/*      */ 
/* 2157 */           localRectangle = getPathBounds(this.tree, paramTreePath);
/* 2158 */           if (localRectangle == null)
/* 2159 */             return false;
/*      */         }
/*      */         else
/*      */         {
/* 2163 */           this.editorHasDifferentSize = false;
/* 2164 */         }this.tree.add(this.editingComponent);
/* 2165 */         this.editingComponent.setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */ 
/* 2168 */         this.editingPath = paramTreePath;
/* 2169 */         AWTAccessor.getComponentAccessor().revalidateSynchronously(this.editingComponent);
/* 2170 */         this.editingComponent.repaint();
/* 2171 */         if (this.cellEditor.shouldSelectCell(paramMouseEvent)) {
/* 2172 */           this.stopEditingInCompleteEditing = false;
/* 2173 */           this.tree.setSelectionRow(i);
/* 2174 */           this.stopEditingInCompleteEditing = true;
/*      */         }
/*      */ 
/* 2177 */         Component localComponent1 = SwingUtilities2.compositeRequestFocus(this.editingComponent);
/*      */ 
/* 2179 */         int j = 1;
/*      */ 
/* 2181 */         if (paramMouseEvent != null)
/*      */         {
/* 2184 */           Point localPoint = SwingUtilities.convertPoint(this.tree, new Point(paramMouseEvent.getX(), paramMouseEvent.getY()), this.editingComponent);
/*      */ 
/* 2193 */           Component localComponent2 = SwingUtilities.getDeepestComponentAt(this.editingComponent, localPoint.x, localPoint.y);
/*      */ 
/* 2196 */           if (localComponent2 != null) {
/* 2197 */             MouseInputHandler localMouseInputHandler = new MouseInputHandler(this.tree, localComponent2, paramMouseEvent, localComponent1);
/*      */ 
/* 2201 */             if (this.releaseEvent != null) {
/* 2202 */               localMouseInputHandler.mouseReleased(this.releaseEvent);
/*      */             }
/*      */ 
/* 2205 */             j = 0;
/*      */           }
/*      */         }
/* 2208 */         if ((j != 0) && ((localComponent1 instanceof JTextField))) {
/* 2209 */           ((JTextField)localComponent1).selectAll();
/*      */         }
/* 2211 */         return true;
/*      */       }
/*      */ 
/* 2214 */       this.editingComponent = null;
/*      */     }
/* 2216 */     return false;
/*      */   }
/*      */ 
/*      */   protected void checkForClickInExpandControl(TreePath paramTreePath, int paramInt1, int paramInt2)
/*      */   {
/* 2230 */     if (isLocationInExpandControl(paramTreePath, paramInt1, paramInt2))
/* 2231 */       handleExpandControlClick(paramTreePath, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected boolean isLocationInExpandControl(TreePath paramTreePath, int paramInt1, int paramInt2)
/*      */   {
/* 2242 */     if ((paramTreePath != null) && (!this.treeModel.isLeaf(paramTreePath.getLastPathComponent())))
/*      */     {
/* 2244 */       Insets localInsets = this.tree.getInsets();
/*      */       int i;
/* 2246 */       if (getExpandedIcon() != null)
/* 2247 */         i = getExpandedIcon().getIconWidth();
/*      */       else {
/* 2249 */         i = 8;
/*      */       }
/* 2251 */       int j = getRowX(this.tree.getRowForPath(paramTreePath), paramTreePath.getPathCount() - 1);
/*      */ 
/* 2254 */       if (this.leftToRight)
/* 2255 */         j = j + localInsets.left - getRightChildIndent() + 1;
/*      */       else {
/* 2257 */         j = this.tree.getWidth() - j - localInsets.right + getRightChildIndent() - 1;
/*      */       }
/*      */ 
/* 2260 */       j = findCenteredX(j, i);
/*      */ 
/* 2262 */       return (paramInt1 >= j) && (paramInt1 < j + i);
/*      */     }
/* 2264 */     return false;
/*      */   }
/*      */ 
/*      */   protected void handleExpandControlClick(TreePath paramTreePath, int paramInt1, int paramInt2)
/*      */   {
/* 2273 */     toggleExpandState(paramTreePath);
/*      */   }
/*      */ 
/*      */   protected void toggleExpandState(TreePath paramTreePath)
/*      */   {
/* 2283 */     if (!this.tree.isExpanded(paramTreePath)) {
/* 2284 */       int i = getRowForPath(this.tree, paramTreePath);
/*      */ 
/* 2286 */       this.tree.expandPath(paramTreePath);
/* 2287 */       updateSize();
/* 2288 */       if (i != -1)
/* 2289 */         if (this.tree.getScrollsOnExpand()) {
/* 2290 */           ensureRowsAreVisible(i, i + this.treeState.getVisibleChildCount(paramTreePath));
/*      */         }
/*      */         else
/* 2293 */           ensureRowsAreVisible(i, i);
/*      */     }
/*      */     else
/*      */     {
/* 2297 */       this.tree.collapsePath(paramTreePath);
/* 2298 */       updateSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isToggleSelectionEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 2307 */     return (SwingUtilities.isLeftMouseButton(paramMouseEvent)) && (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent));
/*      */   }
/*      */ 
/*      */   protected boolean isMultiSelectEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 2316 */     return (SwingUtilities.isLeftMouseButton(paramMouseEvent)) && (paramMouseEvent.isShiftDown());
/*      */   }
/*      */ 
/*      */   protected boolean isToggleEvent(MouseEvent paramMouseEvent)
/*      */   {
/* 2326 */     if (!SwingUtilities.isLeftMouseButton(paramMouseEvent)) {
/* 2327 */       return false;
/*      */     }
/* 2329 */     int i = this.tree.getToggleClickCount();
/*      */ 
/* 2331 */     if (i <= 0) {
/* 2332 */       return false;
/*      */     }
/* 2334 */     return paramMouseEvent.getClickCount() % i == 0;
/*      */   }
/*      */ 
/*      */   protected void selectPathForEvent(TreePath paramTreePath, MouseEvent paramMouseEvent)
/*      */   {
/* 2347 */     if (isMultiSelectEvent(paramMouseEvent)) {
/* 2348 */       TreePath localTreePath1 = getAnchorSelectionPath();
/* 2349 */       int i = localTreePath1 == null ? -1 : getRowForPath(this.tree, localTreePath1);
/*      */ 
/* 2352 */       if ((i == -1) || (this.tree.getSelectionModel().getSelectionMode() == 1))
/*      */       {
/* 2355 */         this.tree.setSelectionPath(paramTreePath);
/*      */       }
/*      */       else {
/* 2358 */         int j = getRowForPath(this.tree, paramTreePath);
/* 2359 */         TreePath localTreePath2 = localTreePath1;
/*      */ 
/* 2361 */         if (isToggleSelectionEvent(paramMouseEvent)) {
/* 2362 */           if (this.tree.isRowSelected(i)) {
/* 2363 */             this.tree.addSelectionInterval(i, j);
/*      */           } else {
/* 2365 */             this.tree.removeSelectionInterval(i, j);
/* 2366 */             this.tree.addSelectionInterval(j, j);
/*      */           }
/* 2368 */         } else if (j < i)
/* 2369 */           this.tree.setSelectionInterval(j, i);
/*      */         else {
/* 2371 */           this.tree.setSelectionInterval(i, j);
/*      */         }
/* 2373 */         this.lastSelectedRow = j;
/* 2374 */         setAnchorSelectionPath(localTreePath2);
/* 2375 */         setLeadSelectionPath(paramTreePath);
/*      */       }
/*      */ 
/*      */     }
/* 2381 */     else if (isToggleSelectionEvent(paramMouseEvent)) {
/* 2382 */       if (this.tree.isPathSelected(paramTreePath))
/* 2383 */         this.tree.removeSelectionPath(paramTreePath);
/*      */       else
/* 2385 */         this.tree.addSelectionPath(paramTreePath);
/* 2386 */       this.lastSelectedRow = getRowForPath(this.tree, paramTreePath);
/* 2387 */       setAnchorSelectionPath(paramTreePath);
/* 2388 */       setLeadSelectionPath(paramTreePath);
/*      */     }
/* 2392 */     else if (SwingUtilities.isLeftMouseButton(paramMouseEvent)) {
/* 2393 */       this.tree.setSelectionPath(paramTreePath);
/* 2394 */       if (isToggleEvent(paramMouseEvent))
/* 2395 */         toggleExpandState(paramTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isLeaf(int paramInt)
/*      */   {
/* 2404 */     TreePath localTreePath = getPathForRow(this.tree, paramInt);
/*      */ 
/* 2406 */     if (localTreePath != null) {
/* 2407 */       return this.treeModel.isLeaf(localTreePath.getLastPathComponent());
/*      */     }
/* 2409 */     return true;
/*      */   }
/*      */ 
/*      */   private void setAnchorSelectionPath(TreePath paramTreePath)
/*      */   {
/* 2417 */     this.ignoreLAChange = true;
/*      */     try {
/* 2419 */       this.tree.setAnchorSelectionPath(paramTreePath);
/*      */     } finally {
/* 2421 */       this.ignoreLAChange = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private TreePath getAnchorSelectionPath() {
/* 2426 */     return this.tree.getAnchorSelectionPath();
/*      */   }
/*      */ 
/*      */   private void setLeadSelectionPath(TreePath paramTreePath) {
/* 2430 */     setLeadSelectionPath(paramTreePath, false);
/*      */   }
/*      */ 
/*      */   private void setLeadSelectionPath(TreePath paramTreePath, boolean paramBoolean) {
/* 2434 */     Rectangle localRectangle = paramBoolean ? getPathBounds(this.tree, getLeadSelectionPath()) : null;
/*      */ 
/* 2437 */     this.ignoreLAChange = true;
/*      */     try {
/* 2439 */       this.tree.setLeadSelectionPath(paramTreePath);
/*      */     } finally {
/* 2441 */       this.ignoreLAChange = false;
/*      */     }
/* 2443 */     this.leadRow = getRowForPath(this.tree, paramTreePath);
/*      */ 
/* 2445 */     if (paramBoolean) {
/* 2446 */       if (localRectangle != null) {
/* 2447 */         this.tree.repaint(getRepaintPathBounds(localRectangle));
/*      */       }
/* 2449 */       localRectangle = getPathBounds(this.tree, paramTreePath);
/* 2450 */       if (localRectangle != null)
/* 2451 */         this.tree.repaint(getRepaintPathBounds(localRectangle));
/*      */     }
/*      */   }
/*      */ 
/*      */   private Rectangle getRepaintPathBounds(Rectangle paramRectangle)
/*      */   {
/* 2457 */     if (UIManager.getBoolean("Tree.repaintWholeRow")) {
/* 2458 */       paramRectangle.x = 0;
/* 2459 */       paramRectangle.width = this.tree.getWidth();
/*      */     }
/* 2461 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   private TreePath getLeadSelectionPath() {
/* 2465 */     return this.tree.getLeadSelectionPath();
/*      */   }
/*      */ 
/*      */   protected void updateLeadSelectionRow()
/*      */   {
/* 2473 */     this.leadRow = getRowForPath(this.tree, getLeadSelectionPath());
/*      */   }
/*      */ 
/*      */   protected int getLeadSelectionRow()
/*      */   {
/* 2483 */     return this.leadRow;
/*      */   }
/*      */ 
/*      */   private void extendSelection(TreePath paramTreePath)
/*      */   {
/* 2491 */     TreePath localTreePath = getAnchorSelectionPath();
/* 2492 */     int i = localTreePath == null ? -1 : getRowForPath(this.tree, localTreePath);
/*      */ 
/* 2494 */     int j = getRowForPath(this.tree, paramTreePath);
/*      */ 
/* 2496 */     if (i == -1) {
/* 2497 */       this.tree.setSelectionRow(j);
/*      */     }
/*      */     else {
/* 2500 */       if (i < j) {
/* 2501 */         this.tree.setSelectionInterval(i, j);
/*      */       }
/*      */       else {
/* 2504 */         this.tree.setSelectionInterval(j, i);
/*      */       }
/* 2506 */       setAnchorSelectionPath(localTreePath);
/* 2507 */       setLeadSelectionPath(paramTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void repaintPath(TreePath paramTreePath)
/*      */   {
/* 2516 */     if (paramTreePath != null) {
/* 2517 */       Rectangle localRectangle = getPathBounds(this.tree, paramTreePath);
/* 2518 */       if (localRectangle != null)
/* 2519 */         this.tree.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Actions extends UIAction
/*      */   {
/*      */     private static final String SELECT_PREVIOUS = "selectPrevious";
/*      */     private static final String SELECT_PREVIOUS_CHANGE_LEAD = "selectPreviousChangeLead";
/*      */     private static final String SELECT_PREVIOUS_EXTEND_SELECTION = "selectPreviousExtendSelection";
/*      */     private static final String SELECT_NEXT = "selectNext";
/*      */     private static final String SELECT_NEXT_CHANGE_LEAD = "selectNextChangeLead";
/*      */     private static final String SELECT_NEXT_EXTEND_SELECTION = "selectNextExtendSelection";
/*      */     private static final String SELECT_CHILD = "selectChild";
/*      */     private static final String SELECT_CHILD_CHANGE_LEAD = "selectChildChangeLead";
/*      */     private static final String SELECT_PARENT = "selectParent";
/*      */     private static final String SELECT_PARENT_CHANGE_LEAD = "selectParentChangeLead";
/*      */     private static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
/*      */     private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
/*      */     private static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
/*      */     private static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
/*      */     private static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
/*      */     private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
/*      */     private static final String SELECT_FIRST = "selectFirst";
/*      */     private static final String SELECT_FIRST_CHANGE_LEAD = "selectFirstChangeLead";
/*      */     private static final String SELECT_FIRST_EXTEND_SELECTION = "selectFirstExtendSelection";
/*      */     private static final String SELECT_LAST = "selectLast";
/*      */     private static final String SELECT_LAST_CHANGE_LEAD = "selectLastChangeLead";
/*      */     private static final String SELECT_LAST_EXTEND_SELECTION = "selectLastExtendSelection";
/*      */     private static final String TOGGLE = "toggle";
/*      */     private static final String CANCEL_EDITING = "cancel";
/*      */     private static final String START_EDITING = "startEditing";
/*      */     private static final String SELECT_ALL = "selectAll";
/*      */     private static final String CLEAR_SELECTION = "clearSelection";
/*      */     private static final String SCROLL_LEFT = "scrollLeft";
/*      */     private static final String SCROLL_RIGHT = "scrollRight";
/*      */     private static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
/*      */     private static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
/*      */     private static final String SCROLL_RIGHT_CHANGE_LEAD = "scrollRightChangeLead";
/*      */     private static final String SCROLL_LEFT_CHANGE_LEAD = "scrollLeftChangeLead";
/*      */     private static final String EXPAND = "expand";
/*      */     private static final String COLLAPSE = "collapse";
/*      */     private static final String MOVE_SELECTION_TO_PARENT = "moveSelectionToParent";
/*      */     private static final String ADD_TO_SELECTION = "addToSelection";
/*      */     private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
/*      */     private static final String EXTEND_TO = "extendTo";
/*      */     private static final String MOVE_SELECTION_TO = "moveSelectionTo";
/*      */ 
/*      */     Actions()
/*      */     {
/* 4006 */       super();
/*      */     }
/*      */ 
/*      */     Actions(String paramString) {
/* 4010 */       super();
/*      */     }
/*      */ 
/*      */     public boolean isEnabled(Object paramObject) {
/* 4014 */       if (((paramObject instanceof JTree)) && 
/* 4015 */         (getName() == "cancel")) {
/* 4016 */         return ((JTree)paramObject).isEditing();
/*      */       }
/*      */ 
/* 4019 */       return true;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 4023 */       JTree localJTree = (JTree)paramActionEvent.getSource();
/* 4024 */       BasicTreeUI localBasicTreeUI = (BasicTreeUI)BasicLookAndFeel.getUIOfType(localJTree.getUI(), BasicTreeUI.class);
/*      */ 
/* 4026 */       if (localBasicTreeUI == null) {
/* 4027 */         return;
/*      */       }
/* 4029 */       String str = getName();
/* 4030 */       if (str == "selectPrevious") {
/* 4031 */         increment(localJTree, localBasicTreeUI, -1, false, true);
/*      */       }
/* 4033 */       else if (str == "selectPreviousChangeLead") {
/* 4034 */         increment(localJTree, localBasicTreeUI, -1, false, false);
/*      */       }
/* 4036 */       else if (str == "selectPreviousExtendSelection") {
/* 4037 */         increment(localJTree, localBasicTreeUI, -1, true, true);
/*      */       }
/* 4039 */       else if (str == "selectNext") {
/* 4040 */         increment(localJTree, localBasicTreeUI, 1, false, true);
/*      */       }
/* 4042 */       else if (str == "selectNextChangeLead") {
/* 4043 */         increment(localJTree, localBasicTreeUI, 1, false, false);
/*      */       }
/* 4045 */       else if (str == "selectNextExtendSelection") {
/* 4046 */         increment(localJTree, localBasicTreeUI, 1, true, true);
/*      */       }
/* 4048 */       else if (str == "selectChild") {
/* 4049 */         traverse(localJTree, localBasicTreeUI, 1, true);
/*      */       }
/* 4051 */       else if (str == "selectChildChangeLead") {
/* 4052 */         traverse(localJTree, localBasicTreeUI, 1, false);
/*      */       }
/* 4054 */       else if (str == "selectParent") {
/* 4055 */         traverse(localJTree, localBasicTreeUI, -1, true);
/*      */       }
/* 4057 */       else if (str == "selectParentChangeLead") {
/* 4058 */         traverse(localJTree, localBasicTreeUI, -1, false);
/*      */       }
/* 4060 */       else if (str == "scrollUpChangeSelection") {
/* 4061 */         page(localJTree, localBasicTreeUI, -1, false, true);
/*      */       }
/* 4063 */       else if (str == "scrollUpChangeLead") {
/* 4064 */         page(localJTree, localBasicTreeUI, -1, false, false);
/*      */       }
/* 4066 */       else if (str == "scrollUpExtendSelection") {
/* 4067 */         page(localJTree, localBasicTreeUI, -1, true, true);
/*      */       }
/* 4069 */       else if (str == "scrollDownChangeSelection") {
/* 4070 */         page(localJTree, localBasicTreeUI, 1, false, true);
/*      */       }
/* 4072 */       else if (str == "scrollDownExtendSelection") {
/* 4073 */         page(localJTree, localBasicTreeUI, 1, true, true);
/*      */       }
/* 4075 */       else if (str == "scrollDownChangeLead") {
/* 4076 */         page(localJTree, localBasicTreeUI, 1, false, false);
/*      */       }
/* 4078 */       else if (str == "selectFirst") {
/* 4079 */         home(localJTree, localBasicTreeUI, -1, false, true);
/*      */       }
/* 4081 */       else if (str == "selectFirstChangeLead") {
/* 4082 */         home(localJTree, localBasicTreeUI, -1, false, false);
/*      */       }
/* 4084 */       else if (str == "selectFirstExtendSelection") {
/* 4085 */         home(localJTree, localBasicTreeUI, -1, true, true);
/*      */       }
/* 4087 */       else if (str == "selectLast") {
/* 4088 */         home(localJTree, localBasicTreeUI, 1, false, true);
/*      */       }
/* 4090 */       else if (str == "selectLastChangeLead") {
/* 4091 */         home(localJTree, localBasicTreeUI, 1, false, false);
/*      */       }
/* 4093 */       else if (str == "selectLastExtendSelection") {
/* 4094 */         home(localJTree, localBasicTreeUI, 1, true, true);
/*      */       }
/* 4096 */       else if (str == "toggle") {
/* 4097 */         toggle(localJTree, localBasicTreeUI);
/*      */       }
/* 4099 */       else if (str == "cancel") {
/* 4100 */         cancelEditing(localJTree, localBasicTreeUI);
/*      */       }
/* 4102 */       else if (str == "startEditing") {
/* 4103 */         startEditing(localJTree, localBasicTreeUI);
/*      */       }
/* 4105 */       else if (str == "selectAll") {
/* 4106 */         selectAll(localJTree, localBasicTreeUI, true);
/*      */       }
/* 4108 */       else if (str == "clearSelection") {
/* 4109 */         selectAll(localJTree, localBasicTreeUI, false);
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/*      */         TreePath localTreePath;
/* 4111 */         if (str == "addToSelection") {
/* 4112 */           if (localBasicTreeUI.getRowCount(localJTree) > 0) {
/* 4113 */             i = localBasicTreeUI.getLeadSelectionRow();
/* 4114 */             if (!localJTree.isRowSelected(i)) {
/* 4115 */               localTreePath = localBasicTreeUI.getAnchorSelectionPath();
/* 4116 */               localJTree.addSelectionRow(i);
/* 4117 */               localBasicTreeUI.setAnchorSelectionPath(localTreePath);
/*      */             }
/*      */           }
/*      */         }
/* 4121 */         else if (str == "toggleAndAnchor") {
/* 4122 */           if (localBasicTreeUI.getRowCount(localJTree) > 0) {
/* 4123 */             i = localBasicTreeUI.getLeadSelectionRow();
/* 4124 */             localTreePath = localBasicTreeUI.getLeadSelectionPath();
/* 4125 */             if (!localJTree.isRowSelected(i)) {
/* 4126 */               localJTree.addSelectionRow(i);
/*      */             } else {
/* 4128 */               localJTree.removeSelectionRow(i);
/* 4129 */               localBasicTreeUI.setLeadSelectionPath(localTreePath);
/*      */             }
/* 4131 */             localBasicTreeUI.setAnchorSelectionPath(localTreePath);
/*      */           }
/*      */         }
/* 4134 */         else if (str == "extendTo") {
/* 4135 */           extendSelection(localJTree, localBasicTreeUI);
/*      */         }
/* 4137 */         else if (str == "moveSelectionTo") {
/* 4138 */           if (localBasicTreeUI.getRowCount(localJTree) > 0) {
/* 4139 */             i = localBasicTreeUI.getLeadSelectionRow();
/* 4140 */             localJTree.setSelectionInterval(i, i);
/*      */           }
/*      */         }
/* 4143 */         else if (str == "scrollLeft") {
/* 4144 */           scroll(localJTree, localBasicTreeUI, 0, -10);
/*      */         }
/* 4146 */         else if (str == "scrollRight") {
/* 4147 */           scroll(localJTree, localBasicTreeUI, 0, 10);
/*      */         }
/* 4149 */         else if (str == "scrollLeftExtendSelection") {
/* 4150 */           scrollChangeSelection(localJTree, localBasicTreeUI, -1, true, true);
/*      */         }
/* 4152 */         else if (str == "scrollRightExtendSelection") {
/* 4153 */           scrollChangeSelection(localJTree, localBasicTreeUI, 1, true, true);
/*      */         }
/* 4155 */         else if (str == "scrollRightChangeLead") {
/* 4156 */           scrollChangeSelection(localJTree, localBasicTreeUI, 1, false, false);
/*      */         }
/* 4158 */         else if (str == "scrollLeftChangeLead") {
/* 4159 */           scrollChangeSelection(localJTree, localBasicTreeUI, -1, false, false);
/*      */         }
/* 4161 */         else if (str == "expand") {
/* 4162 */           expand(localJTree, localBasicTreeUI);
/*      */         }
/* 4164 */         else if (str == "collapse") {
/* 4165 */           collapse(localJTree, localBasicTreeUI);
/*      */         }
/* 4167 */         else if (str == "moveSelectionToParent")
/* 4168 */           moveSelectionToParent(localJTree, localBasicTreeUI);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void scrollChangeSelection(JTree paramJTree, BasicTreeUI paramBasicTreeUI, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*      */       int i;
/* 4177 */       if (((i = paramBasicTreeUI.getRowCount(paramJTree)) > 0) && (paramBasicTreeUI.treeSelectionModel != null))
/*      */       {
/* 4180 */         Rectangle localRectangle = paramJTree.getVisibleRect();
/*      */         TreePath localTreePath;
/* 4182 */         if (paramInt == -1) {
/* 4183 */           localTreePath = paramBasicTreeUI.getClosestPathForLocation(paramJTree, localRectangle.x, localRectangle.y);
/*      */ 
/* 4185 */           localRectangle.x = Math.max(0, localRectangle.x - localRectangle.width);
/*      */         }
/*      */         else {
/* 4188 */           localRectangle.x = Math.min(Math.max(0, paramJTree.getWidth() - localRectangle.width), localRectangle.x + localRectangle.width);
/*      */ 
/* 4190 */           localTreePath = paramBasicTreeUI.getClosestPathForLocation(paramJTree, localRectangle.x, localRectangle.y + localRectangle.height);
/*      */         }
/*      */ 
/* 4194 */         paramJTree.scrollRectToVisible(localRectangle);
/*      */ 
/* 4196 */         if (paramBoolean1) {
/* 4197 */           paramBasicTreeUI.extendSelection(localTreePath);
/*      */         }
/* 4199 */         else if (paramBoolean2) {
/* 4200 */           paramJTree.setSelectionPath(localTreePath);
/*      */         }
/*      */         else
/* 4203 */           paramBasicTreeUI.setLeadSelectionPath(localTreePath, true);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void scroll(JTree paramJTree, BasicTreeUI paramBasicTreeUI, int paramInt1, int paramInt2)
/*      */     {
/* 4210 */       Rectangle localRectangle = paramJTree.getVisibleRect();
/* 4211 */       Dimension localDimension = paramJTree.getSize();
/* 4212 */       if (paramInt1 == 0) {
/* 4213 */         localRectangle.x += paramInt2;
/* 4214 */         localRectangle.x = Math.max(0, localRectangle.x);
/* 4215 */         localRectangle.x = Math.min(Math.max(0, localDimension.width - localRectangle.width), localRectangle.x);
/*      */       }
/*      */       else
/*      */       {
/* 4219 */         localRectangle.y += paramInt2;
/* 4220 */         localRectangle.y = Math.max(0, localRectangle.y);
/* 4221 */         localRectangle.y = Math.min(Math.max(0, localDimension.width - localRectangle.height), localRectangle.y);
/*      */       }
/*      */ 
/* 4224 */       paramJTree.scrollRectToVisible(localRectangle);
/*      */     }
/*      */ 
/*      */     private void extendSelection(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4228 */       if (paramBasicTreeUI.getRowCount(paramJTree) > 0) {
/* 4229 */         int i = paramBasicTreeUI.getLeadSelectionRow();
/*      */ 
/* 4231 */         if (i != -1) {
/* 4232 */           TreePath localTreePath1 = paramBasicTreeUI.getLeadSelectionPath();
/* 4233 */           TreePath localTreePath2 = paramBasicTreeUI.getAnchorSelectionPath();
/* 4234 */           int j = paramBasicTreeUI.getRowForPath(paramJTree, localTreePath2);
/*      */ 
/* 4236 */           if (j == -1)
/* 4237 */             j = 0;
/* 4238 */           paramJTree.setSelectionInterval(j, i);
/* 4239 */           paramBasicTreeUI.setLeadSelectionPath(localTreePath1);
/* 4240 */           paramBasicTreeUI.setAnchorSelectionPath(localTreePath2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void selectAll(JTree paramJTree, BasicTreeUI paramBasicTreeUI, boolean paramBoolean) {
/* 4246 */       int i = paramBasicTreeUI.getRowCount(paramJTree);
/*      */ 
/* 4248 */       if (i > 0)
/*      */       {
/*      */         TreePath localTreePath1;
/*      */         TreePath localTreePath2;
/* 4249 */         if (paramBoolean) {
/* 4250 */           if (paramJTree.getSelectionModel().getSelectionMode() == 1)
/*      */           {
/* 4253 */             int j = paramBasicTreeUI.getLeadSelectionRow();
/* 4254 */             if (j != -1) {
/* 4255 */               paramJTree.setSelectionRow(j);
/* 4256 */             } else if (paramJTree.getMinSelectionRow() == -1) {
/* 4257 */               paramJTree.setSelectionRow(0);
/* 4258 */               paramBasicTreeUI.ensureRowsAreVisible(0, 0);
/*      */             }
/* 4260 */             return;
/*      */           }
/*      */ 
/* 4263 */           localTreePath1 = paramBasicTreeUI.getLeadSelectionPath();
/* 4264 */           localTreePath2 = paramBasicTreeUI.getAnchorSelectionPath();
/*      */ 
/* 4266 */           if ((localTreePath1 != null) && (!paramJTree.isVisible(localTreePath1))) {
/* 4267 */             localTreePath1 = null;
/*      */           }
/* 4269 */           paramJTree.setSelectionInterval(0, i - 1);
/* 4270 */           if (localTreePath1 != null) {
/* 4271 */             paramBasicTreeUI.setLeadSelectionPath(localTreePath1);
/*      */           }
/* 4273 */           if ((localTreePath2 != null) && (paramJTree.isVisible(localTreePath2)))
/* 4274 */             paramBasicTreeUI.setAnchorSelectionPath(localTreePath2);
/*      */         }
/*      */         else
/*      */         {
/* 4278 */           localTreePath1 = paramBasicTreeUI.getLeadSelectionPath();
/* 4279 */           localTreePath2 = paramBasicTreeUI.getAnchorSelectionPath();
/*      */ 
/* 4281 */           paramJTree.clearSelection();
/* 4282 */           paramBasicTreeUI.setAnchorSelectionPath(localTreePath2);
/* 4283 */           paramBasicTreeUI.setLeadSelectionPath(localTreePath1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void startEditing(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4289 */       TreePath localTreePath = paramBasicTreeUI.getLeadSelectionPath();
/* 4290 */       int i = localTreePath != null ? paramBasicTreeUI.getRowForPath(paramJTree, localTreePath) : -1;
/*      */ 
/* 4293 */       if (i != -1)
/* 4294 */         paramJTree.startEditingAtPath(localTreePath);
/*      */     }
/*      */ 
/*      */     private void cancelEditing(JTree paramJTree, BasicTreeUI paramBasicTreeUI)
/*      */     {
/* 4299 */       paramJTree.cancelEditing();
/*      */     }
/*      */ 
/*      */     private void toggle(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4303 */       int i = paramBasicTreeUI.getLeadSelectionRow();
/*      */ 
/* 4305 */       if ((i != -1) && (!paramBasicTreeUI.isLeaf(i))) {
/* 4306 */         TreePath localTreePath1 = paramBasicTreeUI.getAnchorSelectionPath();
/* 4307 */         TreePath localTreePath2 = paramBasicTreeUI.getLeadSelectionPath();
/*      */ 
/* 4309 */         paramBasicTreeUI.toggleExpandState(paramBasicTreeUI.getPathForRow(paramJTree, i));
/* 4310 */         paramBasicTreeUI.setAnchorSelectionPath(localTreePath1);
/* 4311 */         paramBasicTreeUI.setLeadSelectionPath(localTreePath2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void expand(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4316 */       int i = paramBasicTreeUI.getLeadSelectionRow();
/* 4317 */       paramJTree.expandRow(i);
/*      */     }
/*      */ 
/*      */     private void collapse(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4321 */       int i = paramBasicTreeUI.getLeadSelectionRow();
/* 4322 */       paramJTree.collapseRow(i);
/*      */     }
/*      */ 
/*      */     private void increment(JTree paramJTree, BasicTreeUI paramBasicTreeUI, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 4330 */       if ((!paramBoolean1) && (!paramBoolean2) && (paramJTree.getSelectionModel().getSelectionMode() != 4))
/*      */       {
/* 4333 */         paramBoolean2 = true;
/*      */       }
/*      */       int i;
/* 4338 */       if ((paramBasicTreeUI.treeSelectionModel != null) && ((i = paramJTree.getRowCount()) > 0))
/*      */       {
/* 4340 */         int j = paramBasicTreeUI.getLeadSelectionRow();
/*      */         int k;
/* 4343 */         if (j == -1) {
/* 4344 */           if (paramInt == 1)
/* 4345 */             k = 0;
/*      */           else {
/* 4347 */             k = i - 1;
/*      */           }
/*      */         }
/*      */         else {
/* 4351 */           k = Math.min(i - 1, Math.max(0, j + paramInt));
/*      */         }
/* 4353 */         if ((paramBoolean1) && (paramBasicTreeUI.treeSelectionModel.getSelectionMode() != 1))
/*      */         {
/* 4356 */           paramBasicTreeUI.extendSelection(paramJTree.getPathForRow(k));
/*      */         }
/* 4358 */         else if (paramBoolean2) {
/* 4359 */           paramJTree.setSelectionInterval(k, k);
/*      */         }
/*      */         else {
/* 4362 */           paramBasicTreeUI.setLeadSelectionPath(paramJTree.getPathForRow(k), true);
/*      */         }
/* 4364 */         paramBasicTreeUI.ensureRowsAreVisible(k, k);
/* 4365 */         paramBasicTreeUI.lastSelectedRow = k;
/*      */       }
/*      */     }
/*      */ 
/*      */     private void traverse(JTree paramJTree, BasicTreeUI paramBasicTreeUI, int paramInt, boolean paramBoolean)
/*      */     {
/* 4373 */       if ((!paramBoolean) && (paramJTree.getSelectionModel().getSelectionMode() != 4))
/*      */       {
/* 4376 */         paramBoolean = true;
/*      */       }
/*      */       int i;
/* 4381 */       if ((i = paramJTree.getRowCount()) > 0) {
/* 4382 */         int j = paramBasicTreeUI.getLeadSelectionRow();
/*      */         int k;
/* 4385 */         if (j == -1) {
/* 4386 */           k = 0;
/*      */         }
/*      */         else
/*      */         {
/*      */           TreePath localTreePath;
/* 4390 */           if (paramInt == 1) {
/* 4391 */             localTreePath = paramBasicTreeUI.getPathForRow(paramJTree, j);
/* 4392 */             int m = paramJTree.getModel().getChildCount(localTreePath.getLastPathComponent());
/*      */ 
/* 4394 */             k = -1;
/* 4395 */             if (!paramBasicTreeUI.isLeaf(j)) {
/* 4396 */               if (!paramJTree.isExpanded(j)) {
/* 4397 */                 paramBasicTreeUI.toggleExpandState(localTreePath);
/*      */               }
/* 4399 */               else if (m > 0) {
/* 4400 */                 k = Math.min(j + 1, i - 1);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/* 4406 */           else if ((!paramBasicTreeUI.isLeaf(j)) && (paramJTree.isExpanded(j)))
/*      */           {
/* 4408 */             paramBasicTreeUI.toggleExpandState(paramBasicTreeUI.getPathForRow(paramJTree, j));
/*      */ 
/* 4410 */             k = -1;
/*      */           }
/*      */           else {
/* 4413 */             localTreePath = paramBasicTreeUI.getPathForRow(paramJTree, j);
/*      */ 
/* 4416 */             if ((localTreePath != null) && (localTreePath.getPathCount() > 1)) {
/* 4417 */               k = paramBasicTreeUI.getRowForPath(paramJTree, localTreePath.getParentPath());
/*      */             }
/*      */             else
/*      */             {
/* 4421 */               k = -1;
/*      */             }
/*      */           }
/*      */         }
/* 4425 */         if (k != -1) {
/* 4426 */           if (paramBoolean) {
/* 4427 */             paramJTree.setSelectionInterval(k, k);
/*      */           }
/*      */           else {
/* 4430 */             paramBasicTreeUI.setLeadSelectionPath(paramBasicTreeUI.getPathForRow(paramJTree, k), true);
/*      */           }
/*      */ 
/* 4433 */           paramBasicTreeUI.ensureRowsAreVisible(k, k);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void moveSelectionToParent(JTree paramJTree, BasicTreeUI paramBasicTreeUI) {
/* 4439 */       int i = paramBasicTreeUI.getLeadSelectionRow();
/* 4440 */       TreePath localTreePath = paramBasicTreeUI.getPathForRow(paramJTree, i);
/* 4441 */       if ((localTreePath != null) && (localTreePath.getPathCount() > 1)) {
/* 4442 */         int j = paramBasicTreeUI.getRowForPath(paramJTree, localTreePath.getParentPath());
/* 4443 */         if (j != -1) {
/* 4444 */           paramJTree.setSelectionInterval(j, j);
/* 4445 */           paramBasicTreeUI.ensureRowsAreVisible(j, j);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void page(JTree paramJTree, BasicTreeUI paramBasicTreeUI, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 4454 */       if ((!paramBoolean1) && (!paramBoolean2) && (paramJTree.getSelectionModel().getSelectionMode() != 4))
/*      */       {
/* 4457 */         paramBoolean2 = true;
/*      */       }
/*      */       int i;
/* 4462 */       if (((i = paramBasicTreeUI.getRowCount(paramJTree)) > 0) && (paramBasicTreeUI.treeSelectionModel != null))
/*      */       {
/* 4464 */         Dimension localDimension = paramJTree.getSize();
/* 4465 */         TreePath localTreePath1 = paramBasicTreeUI.getLeadSelectionPath();
/*      */ 
/* 4467 */         Rectangle localRectangle1 = paramJTree.getVisibleRect();
/*      */         TreePath localTreePath2;
/* 4469 */         if (paramInt == -1)
/*      */         {
/* 4471 */           localTreePath2 = paramBasicTreeUI.getClosestPathForLocation(paramJTree, localRectangle1.x, localRectangle1.y);
/*      */ 
/* 4473 */           if (localTreePath2.equals(localTreePath1)) {
/* 4474 */             localRectangle1.y = Math.max(0, localRectangle1.y - localRectangle1.height);
/* 4475 */             localTreePath2 = paramJTree.getClosestPathForLocation(localRectangle1.x, localRectangle1.y);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 4481 */           localRectangle1.y = Math.min(localDimension.height, localRectangle1.y + localRectangle1.height - 1);
/*      */ 
/* 4483 */           localTreePath2 = paramJTree.getClosestPathForLocation(localRectangle1.x, localRectangle1.y);
/*      */ 
/* 4485 */           if (localTreePath2.equals(localTreePath1)) {
/* 4486 */             localRectangle1.y = Math.min(localDimension.height, localRectangle1.y + localRectangle1.height - 1);
/*      */ 
/* 4488 */             localTreePath2 = paramJTree.getClosestPathForLocation(localRectangle1.x, localRectangle1.y);
/*      */           }
/*      */         }
/*      */ 
/* 4492 */         Rectangle localRectangle2 = paramBasicTreeUI.getPathBounds(paramJTree, localTreePath2);
/* 4493 */         if (localRectangle2 != null) {
/* 4494 */           localRectangle2.x = localRectangle1.x;
/* 4495 */           localRectangle2.width = localRectangle1.width;
/* 4496 */           if (paramInt == -1) {
/* 4497 */             localRectangle2.height = localRectangle1.height;
/*      */           }
/*      */           else {
/* 4500 */             localRectangle2.y -= localRectangle1.height - localRectangle2.height;
/* 4501 */             localRectangle2.height = localRectangle1.height;
/*      */           }
/*      */ 
/* 4504 */           if (paramBoolean1) {
/* 4505 */             paramBasicTreeUI.extendSelection(localTreePath2);
/*      */           }
/* 4507 */           else if (paramBoolean2) {
/* 4508 */             paramJTree.setSelectionPath(localTreePath2);
/*      */           }
/*      */           else {
/* 4511 */             paramBasicTreeUI.setLeadSelectionPath(localTreePath2, true);
/*      */           }
/* 4513 */           paramJTree.scrollRectToVisible(localRectangle2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void home(JTree paramJTree, final BasicTreeUI paramBasicTreeUI, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 4522 */       if ((!paramBoolean1) && (!paramBoolean2) && (paramJTree.getSelectionModel().getSelectionMode() != 4))
/*      */       {
/* 4525 */         paramBoolean2 = true;
/*      */       }
/*      */ 
/* 4528 */       final int i = paramBasicTreeUI.getRowCount(paramJTree);
/*      */ 
/* 4530 */       if (i > 0)
/*      */       {
/*      */         TreePath localTreePath;
/*      */         int j;
/* 4531 */         if (paramInt == -1) {
/* 4532 */           paramBasicTreeUI.ensureRowsAreVisible(0, 0);
/* 4533 */           if (paramBoolean1) {
/* 4534 */             localTreePath = paramBasicTreeUI.getAnchorSelectionPath();
/* 4535 */             j = localTreePath == null ? -1 : paramBasicTreeUI.getRowForPath(paramJTree, localTreePath);
/*      */ 
/* 4538 */             if (j == -1) {
/* 4539 */               paramJTree.setSelectionInterval(0, 0);
/*      */             }
/*      */             else {
/* 4542 */               paramJTree.setSelectionInterval(0, j);
/* 4543 */               paramBasicTreeUI.setAnchorSelectionPath(localTreePath);
/* 4544 */               paramBasicTreeUI.setLeadSelectionPath(paramBasicTreeUI.getPathForRow(paramJTree, 0));
/*      */             }
/*      */           }
/* 4547 */           else if (paramBoolean2) {
/* 4548 */             paramJTree.setSelectionInterval(0, 0);
/*      */           }
/*      */           else {
/* 4551 */             paramBasicTreeUI.setLeadSelectionPath(paramBasicTreeUI.getPathForRow(paramJTree, 0), true);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 4556 */           paramBasicTreeUI.ensureRowsAreVisible(i - 1, i - 1);
/* 4557 */           if (paramBoolean1) {
/* 4558 */             localTreePath = paramBasicTreeUI.getAnchorSelectionPath();
/* 4559 */             j = localTreePath == null ? -1 : paramBasicTreeUI.getRowForPath(paramJTree, localTreePath);
/*      */ 
/* 4562 */             if (j == -1) {
/* 4563 */               paramJTree.setSelectionInterval(i - 1, i - 1);
/*      */             }
/*      */             else
/*      */             {
/* 4567 */               paramJTree.setSelectionInterval(j, i - 1);
/* 4568 */               paramBasicTreeUI.setAnchorSelectionPath(localTreePath);
/* 4569 */               paramBasicTreeUI.setLeadSelectionPath(paramBasicTreeUI.getPathForRow(paramJTree, i - 1));
/*      */             }
/*      */ 
/*      */           }
/* 4573 */           else if (paramBoolean2) {
/* 4574 */             paramJTree.setSelectionInterval(i - 1, i - 1);
/*      */           }
/*      */           else {
/* 4577 */             paramBasicTreeUI.setLeadSelectionPath(paramBasicTreeUI.getPathForRow(paramJTree, i - 1), true);
/*      */           }
/*      */ 
/* 4580 */           if (paramBasicTreeUI.isLargeModel())
/* 4581 */             SwingUtilities.invokeLater(new Runnable() {
/*      */               public void run() {
/* 4583 */                 paramBasicTreeUI.ensureRowsAreVisible(i - 1, i - 1);
/*      */               }
/*      */             });
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class CellEditorHandler
/*      */     implements CellEditorListener
/*      */   {
/*      */     public CellEditorHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void editingStopped(ChangeEvent paramChangeEvent)
/*      */     {
/* 2687 */       BasicTreeUI.this.getHandler().editingStopped(paramChangeEvent);
/*      */     }
/*      */ 
/*      */     public void editingCanceled(ChangeEvent paramChangeEvent)
/*      */     {
/* 2692 */       BasicTreeUI.this.getHandler().editingCanceled(paramChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ComponentHandler extends ComponentAdapter
/*      */     implements ActionListener
/*      */   {
/*      */     protected Timer timer;
/*      */     protected JScrollBar scrollBar;
/*      */ 
/*      */     public ComponentHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentMoved(ComponentEvent paramComponentEvent)
/*      */     {
/* 2561 */       if (this.timer == null) {
/* 2562 */         JScrollPane localJScrollPane = getScrollPane();
/*      */ 
/* 2564 */         if (localJScrollPane == null) {
/* 2565 */           BasicTreeUI.this.updateSize();
/*      */         } else {
/* 2567 */           this.scrollBar = localJScrollPane.getVerticalScrollBar();
/* 2568 */           if ((this.scrollBar == null) || (!this.scrollBar.getValueIsAdjusting()))
/*      */           {
/* 2571 */             if (((this.scrollBar = localJScrollPane.getHorizontalScrollBar()) != null) && (this.scrollBar.getValueIsAdjusting()))
/*      */             {
/* 2573 */               startTimer();
/*      */             }
/* 2575 */             else BasicTreeUI.this.updateSize();
/*      */           }
/*      */           else
/* 2578 */             startTimer();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void startTimer()
/*      */     {
/* 2588 */       if (this.timer == null) {
/* 2589 */         this.timer = new Timer(200, this);
/* 2590 */         this.timer.setRepeats(true);
/*      */       }
/* 2592 */       this.timer.start();
/*      */     }
/*      */ 
/*      */     protected JScrollPane getScrollPane()
/*      */     {
/* 2600 */       Container localContainer = BasicTreeUI.this.tree.getParent();
/*      */ 
/* 2602 */       while ((localContainer != null) && (!(localContainer instanceof JScrollPane)))
/* 2603 */         localContainer = localContainer.getParent();
/* 2604 */       if ((localContainer instanceof JScrollPane))
/* 2605 */         return (JScrollPane)localContainer;
/* 2606 */       return null;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2614 */       if ((this.scrollBar == null) || (!this.scrollBar.getValueIsAdjusting())) {
/* 2615 */         if (this.timer != null)
/* 2616 */           this.timer.stop();
/* 2617 */         BasicTreeUI.this.updateSize();
/* 2618 */         this.timer = null;
/* 2619 */         this.scrollBar = null;
/*      */       }
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
/* 2755 */       BasicTreeUI.this.getHandler().focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent)
/*      */     {
/* 2763 */       BasicTreeUI.this.getHandler().focusLost(paramFocusEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Handler
/*      */     implements CellEditorListener, FocusListener, KeyListener, MouseListener, MouseMotionListener, PropertyChangeListener, TreeExpansionListener, TreeModelListener, TreeSelectionListener, DragRecognitionSupport.BeforeDrag
/*      */   {
/* 3289 */     private String prefix = "";
/* 3290 */     private String typedString = "";
/* 3291 */     private long lastTime = 0L;
/*      */     private boolean dragPressDidSelection;
/*      */     private boolean dragStarted;
/*      */     private TreePath pressedPath;
/*      */     private MouseEvent pressedEvent;
/*      */     private boolean valueChangedOnPress;
/*      */ 
/*      */     private Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent)
/*      */     {
/* 3306 */       if ((BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.getRowCount() > 0) && (BasicTreeUI.this.tree.hasFocus()) && (BasicTreeUI.this.tree.isEnabled()))
/*      */       {
/* 3308 */         if ((paramKeyEvent.isAltDown()) || (BasicGraphicsUtils.isMenuShortcutKeyDown(paramKeyEvent)) || (isNavigationKey(paramKeyEvent)))
/*      */         {
/* 3310 */           return;
/*      */         }
/* 3312 */         int i = 1;
/*      */ 
/* 3314 */         char c = paramKeyEvent.getKeyChar();
/*      */ 
/* 3316 */         long l = paramKeyEvent.getWhen();
/* 3317 */         int j = BasicTreeUI.this.tree.getLeadSelectionRow();
/* 3318 */         if (l - this.lastTime < BasicTreeUI.this.timeFactor) {
/* 3319 */           this.typedString += c;
/* 3320 */           if ((this.prefix.length() == 1) && (c == this.prefix.charAt(0)))
/*      */           {
/* 3323 */             j++;
/*      */           }
/* 3325 */           else this.prefix = this.typedString; 
/*      */         }
/*      */         else
/*      */         {
/* 3328 */           j++;
/* 3329 */           this.typedString = ("" + c);
/* 3330 */           this.prefix = this.typedString;
/*      */         }
/* 3332 */         this.lastTime = l;
/*      */ 
/* 3334 */         if ((j < 0) || (j >= BasicTreeUI.this.tree.getRowCount())) {
/* 3335 */           i = 0;
/* 3336 */           j = 0;
/*      */         }
/* 3338 */         TreePath localTreePath = BasicTreeUI.this.tree.getNextMatch(this.prefix, j, Position.Bias.Forward);
/*      */         int k;
/* 3340 */         if (localTreePath != null) {
/* 3341 */           BasicTreeUI.this.tree.setSelectionPath(localTreePath);
/* 3342 */           k = BasicTreeUI.this.getRowForPath(BasicTreeUI.this.tree, localTreePath);
/* 3343 */           BasicTreeUI.this.ensureRowsAreVisible(k, k);
/* 3344 */         } else if (i != 0) {
/* 3345 */           localTreePath = BasicTreeUI.this.tree.getNextMatch(this.prefix, 0, Position.Bias.Forward);
/*      */ 
/* 3347 */           if (localTreePath != null) {
/* 3348 */             BasicTreeUI.this.tree.setSelectionPath(localTreePath);
/* 3349 */             k = BasicTreeUI.this.getRowForPath(BasicTreeUI.this.tree, localTreePath);
/* 3350 */             BasicTreeUI.this.ensureRowsAreVisible(k, k);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent)
/*      */     {
/* 3363 */       if ((BasicTreeUI.this.tree != null) && (isNavigationKey(paramKeyEvent))) {
/* 3364 */         this.prefix = "";
/* 3365 */         this.typedString = "";
/* 3366 */         this.lastTime = 0L;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     private boolean isNavigationKey(KeyEvent paramKeyEvent)
/*      */     {
/* 3379 */       InputMap localInputMap = BasicTreeUI.this.tree.getInputMap(1);
/* 3380 */       KeyStroke localKeyStroke = KeyStroke.getKeyStrokeForEvent(paramKeyEvent);
/*      */ 
/* 3382 */       return (localInputMap != null) && (localInputMap.get(localKeyStroke) != null);
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 3390 */       if (paramPropertyChangeEvent.getSource() == BasicTreeUI.this.treeSelectionModel) {
/* 3391 */         BasicTreeUI.this.treeSelectionModel.resetRowSelection();
/*      */       }
/* 3393 */       else if (paramPropertyChangeEvent.getSource() == BasicTreeUI.this.tree) {
/* 3394 */         String str = paramPropertyChangeEvent.getPropertyName();
/*      */ 
/* 3396 */         if (str == "leadSelectionPath") {
/* 3397 */           if (!BasicTreeUI.this.ignoreLAChange) {
/* 3398 */             BasicTreeUI.this.updateLeadSelectionRow();
/* 3399 */             BasicTreeUI.this.repaintPath((TreePath)paramPropertyChangeEvent.getOldValue());
/* 3400 */             BasicTreeUI.this.repaintPath((TreePath)paramPropertyChangeEvent.getNewValue());
/*      */           }
/*      */         }
/* 3403 */         else if ((str == "anchorSelectionPath") && 
/* 3404 */           (!BasicTreeUI.this.ignoreLAChange)) {
/* 3405 */           BasicTreeUI.this.repaintPath((TreePath)paramPropertyChangeEvent.getOldValue());
/* 3406 */           BasicTreeUI.this.repaintPath((TreePath)paramPropertyChangeEvent.getNewValue());
/*      */         }
/*      */ 
/* 3409 */         if (str == "cellRenderer") {
/* 3410 */           BasicTreeUI.this.setCellRenderer((TreeCellRenderer)paramPropertyChangeEvent.getNewValue());
/* 3411 */           BasicTreeUI.this.redoTheLayout();
/*      */         }
/* 3413 */         else if (str == "model") {
/* 3414 */           BasicTreeUI.this.setModel((TreeModel)paramPropertyChangeEvent.getNewValue());
/*      */         }
/* 3416 */         else if (str == "rootVisible") {
/* 3417 */           BasicTreeUI.this.setRootVisible(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/*      */         }
/* 3420 */         else if (str == "showsRootHandles") {
/* 3421 */           BasicTreeUI.this.setShowsRootHandles(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/*      */         }
/* 3424 */         else if (str == "rowHeight") {
/* 3425 */           BasicTreeUI.this.setRowHeight(((Integer)paramPropertyChangeEvent.getNewValue()).intValue());
/*      */         }
/* 3428 */         else if (str == "cellEditor") {
/* 3429 */           BasicTreeUI.this.setCellEditor((TreeCellEditor)paramPropertyChangeEvent.getNewValue());
/*      */         }
/* 3431 */         else if (str == "editable") {
/* 3432 */           BasicTreeUI.this.setEditable(((Boolean)paramPropertyChangeEvent.getNewValue()).booleanValue());
/*      */         }
/* 3434 */         else if (str == "largeModel") {
/* 3435 */           BasicTreeUI.this.setLargeModel(BasicTreeUI.this.tree.isLargeModel());
/*      */         }
/* 3437 */         else if (str == "selectionModel") {
/* 3438 */           BasicTreeUI.this.setSelectionModel(BasicTreeUI.this.tree.getSelectionModel());
/*      */         }
/* 3440 */         else if (str == "font") {
/* 3441 */           BasicTreeUI.this.completeEditing();
/* 3442 */           if (BasicTreeUI.this.treeState != null)
/* 3443 */             BasicTreeUI.this.treeState.invalidateSizes();
/* 3444 */           BasicTreeUI.this.updateSize();
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject;
/* 3446 */           if (str == "componentOrientation") {
/* 3447 */             if (BasicTreeUI.this.tree != null) {
/* 3448 */               BasicTreeUI.this.leftToRight = BasicGraphicsUtils.isLeftToRight(BasicTreeUI.this.tree);
/* 3449 */               BasicTreeUI.this.redoTheLayout();
/* 3450 */               BasicTreeUI.this.tree.treeDidChange();
/*      */ 
/* 3452 */               localObject = BasicTreeUI.this.getInputMap(0);
/* 3453 */               SwingUtilities.replaceUIInputMap(BasicTreeUI.this.tree, 0, (InputMap)localObject);
/*      */             }
/*      */           }
/* 3456 */           else if ("dropLocation" == str) {
/* 3457 */             localObject = (JTree.DropLocation)paramPropertyChangeEvent.getOldValue();
/* 3458 */             repaintDropLocation((JTree.DropLocation)localObject);
/* 3459 */             repaintDropLocation(BasicTreeUI.this.tree.getDropLocation());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3465 */     private void repaintDropLocation(JTree.DropLocation paramDropLocation) { if (paramDropLocation == null)
/*      */         return;
/*      */       Rectangle localRectangle;
/* 3471 */       if (BasicTreeUI.this.isDropLine(paramDropLocation))
/* 3472 */         localRectangle = BasicTreeUI.this.getDropLineRect(paramDropLocation);
/*      */       else {
/* 3474 */         localRectangle = BasicTreeUI.this.tree.getPathBounds(paramDropLocation.getPath());
/*      */       }
/*      */ 
/* 3477 */       if (localRectangle != null)
/* 3478 */         BasicTreeUI.this.tree.repaint(localRectangle);
/*      */     }
/*      */ 
/*      */     private boolean isActualPath(TreePath paramTreePath, int paramInt1, int paramInt2)
/*      */     {
/* 3505 */       if (paramTreePath == null) {
/* 3506 */         return false;
/*      */       }
/*      */ 
/* 3509 */       Rectangle localRectangle = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, paramTreePath);
/* 3510 */       if ((localRectangle == null) || (paramInt2 > localRectangle.y + localRectangle.height)) {
/* 3511 */         return false;
/*      */       }
/*      */ 
/* 3514 */       return (paramInt1 >= localRectangle.x) && (paramInt1 <= localRectangle.x + localRectangle.width);
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
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 3530 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTreeUI.this.tree)) {
/* 3531 */         return;
/*      */       }
/*      */ 
/* 3535 */       if ((BasicTreeUI.this.isEditing(BasicTreeUI.this.tree)) && (BasicTreeUI.this.tree.getInvokesStopCellEditing()) && (!BasicTreeUI.this.stopEditing(BasicTreeUI.this.tree)))
/*      */       {
/* 3537 */         return;
/*      */       }
/*      */ 
/* 3540 */       BasicTreeUI.this.completeEditing();
/*      */ 
/* 3542 */       this.pressedPath = BasicTreeUI.this.getClosestPathForLocation(BasicTreeUI.this.tree, paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */ 
/* 3544 */       if (BasicTreeUI.this.tree.getDragEnabled()) {
/* 3545 */         mousePressedDND(paramMouseEvent);
/*      */       } else {
/* 3547 */         SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
/* 3548 */         handleSelection(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void mousePressedDND(MouseEvent paramMouseEvent) {
/* 3553 */       this.pressedEvent = paramMouseEvent;
/* 3554 */       int i = 1;
/* 3555 */       this.dragStarted = false;
/* 3556 */       this.valueChangedOnPress = false;
/*      */ 
/* 3559 */       if ((isActualPath(this.pressedPath, paramMouseEvent.getX(), paramMouseEvent.getY())) && (DragRecognitionSupport.mousePressed(paramMouseEvent)))
/*      */       {
/* 3562 */         this.dragPressDidSelection = false;
/*      */ 
/* 3564 */         if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent))
/*      */         {
/* 3567 */           return;
/* 3568 */         }if ((!paramMouseEvent.isShiftDown()) && (BasicTreeUI.this.tree.isPathSelected(this.pressedPath)))
/*      */         {
/* 3571 */           BasicTreeUI.this.setAnchorSelectionPath(this.pressedPath);
/* 3572 */           BasicTreeUI.this.setLeadSelectionPath(this.pressedPath, true);
/* 3573 */           return;
/*      */         }
/*      */ 
/* 3576 */         this.dragPressDidSelection = true;
/*      */ 
/* 3579 */         i = 0;
/*      */       }
/*      */ 
/* 3582 */       if (i != 0) {
/* 3583 */         SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
/*      */       }
/*      */ 
/* 3586 */       handleSelection(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     void handleSelection(MouseEvent paramMouseEvent) {
/* 3590 */       if (this.pressedPath != null) {
/* 3591 */         Rectangle localRectangle = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, this.pressedPath);
/*      */ 
/* 3593 */         if ((localRectangle == null) || (paramMouseEvent.getY() >= localRectangle.y + localRectangle.height)) {
/* 3594 */           return;
/*      */         }
/*      */ 
/* 3599 */         if (SwingUtilities.isLeftMouseButton(paramMouseEvent)) {
/* 3600 */           BasicTreeUI.this.checkForClickInExpandControl(this.pressedPath, paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */         }
/*      */ 
/* 3603 */         int i = paramMouseEvent.getX();
/*      */ 
/* 3607 */         if ((i >= localRectangle.x) && (i < localRectangle.x + localRectangle.width) && (
/* 3608 */           (BasicTreeUI.this.tree.getDragEnabled()) || (!BasicTreeUI.this.startEditing(this.pressedPath, paramMouseEvent))))
/* 3609 */           BasicTreeUI.this.selectPathForEvent(this.pressedPath, paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dragStarting(MouseEvent paramMouseEvent)
/*      */     {
/* 3616 */       this.dragStarted = true;
/*      */ 
/* 3618 */       if (BasicGraphicsUtils.isMenuShortcutKeyDown(paramMouseEvent)) {
/* 3619 */         BasicTreeUI.this.tree.addSelectionPath(this.pressedPath);
/* 3620 */         BasicTreeUI.this.setAnchorSelectionPath(this.pressedPath);
/* 3621 */         BasicTreeUI.this.setLeadSelectionPath(this.pressedPath, true);
/*      */       }
/*      */ 
/* 3624 */       this.pressedEvent = null;
/* 3625 */       this.pressedPath = null;
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 3629 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTreeUI.this.tree)) {
/* 3630 */         return;
/*      */       }
/*      */ 
/* 3633 */       if (BasicTreeUI.this.tree.getDragEnabled())
/* 3634 */         DragRecognitionSupport.mouseDragged(paramMouseEvent, this);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 3646 */       if (SwingUtilities2.shouldIgnore(paramMouseEvent, BasicTreeUI.this.tree)) {
/* 3647 */         return;
/*      */       }
/*      */ 
/* 3650 */       if (BasicTreeUI.this.tree.getDragEnabled()) {
/* 3651 */         mouseReleasedDND(paramMouseEvent);
/*      */       }
/*      */ 
/* 3654 */       this.pressedEvent = null;
/* 3655 */       this.pressedPath = null;
/*      */     }
/*      */ 
/*      */     private void mouseReleasedDND(MouseEvent paramMouseEvent) {
/* 3659 */       MouseEvent localMouseEvent = DragRecognitionSupport.mouseReleased(paramMouseEvent);
/* 3660 */       if (localMouseEvent != null) {
/* 3661 */         SwingUtilities2.adjustFocus(BasicTreeUI.this.tree);
/* 3662 */         if (!this.dragPressDidSelection) {
/* 3663 */           handleSelection(localMouseEvent);
/*      */         }
/*      */       }
/*      */ 
/* 3667 */       if (!this.dragStarted)
/*      */       {
/* 3678 */         if ((this.pressedPath != null) && (!this.valueChangedOnPress) && (isActualPath(this.pressedPath, this.pressedEvent.getX(), this.pressedEvent.getY())))
/*      */         {
/* 3681 */           BasicTreeUI.this.startEditingOnRelease(this.pressedPath, this.pressedEvent, paramMouseEvent);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 3690 */       if (BasicTreeUI.this.tree != null)
/*      */       {
/* 3693 */         Rectangle localRectangle = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, BasicTreeUI.this.tree.getLeadSelectionPath());
/* 3694 */         if (localRectangle != null)
/* 3695 */           BasicTreeUI.this.tree.repaint(BasicTreeUI.this.getRepaintPathBounds(localRectangle));
/* 3696 */         localRectangle = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, BasicTreeUI.this.getLeadSelectionPath());
/* 3697 */         if (localRectangle != null)
/* 3698 */           BasicTreeUI.this.tree.repaint(BasicTreeUI.this.getRepaintPathBounds(localRectangle));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/* 3703 */       focusGained(paramFocusEvent);
/*      */     }
/*      */ 
/*      */     public void editingStopped(ChangeEvent paramChangeEvent)
/*      */     {
/* 3710 */       BasicTreeUI.this.completeEditing(false, false, true);
/*      */     }
/*      */ 
/*      */     public void editingCanceled(ChangeEvent paramChangeEvent)
/*      */     {
/* 3715 */       BasicTreeUI.this.completeEditing(false, false, false);
/*      */     }
/*      */ 
/*      */     public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */     {
/* 3723 */       this.valueChangedOnPress = true;
/*      */ 
/* 3726 */       BasicTreeUI.this.completeEditing();
/*      */ 
/* 3729 */       if ((BasicTreeUI.this.tree.getExpandsSelectedPaths()) && (BasicTreeUI.this.treeSelectionModel != null)) {
/* 3730 */         localObject1 = BasicTreeUI.this.treeSelectionModel.getSelectionPaths();
/*      */ 
/* 3733 */         if (localObject1 != null) {
/* 3734 */           for (int i = localObject1.length - 1; i >= 0; 
/* 3735 */             i--) {
/* 3736 */             localObject2 = localObject1[i].getParentPath();
/* 3737 */             int j = 1;
/*      */ 
/* 3739 */             while (localObject2 != null)
/*      */             {
/* 3742 */               if (BasicTreeUI.this.treeModel.isLeaf(((TreePath)localObject2).getLastPathComponent())) {
/* 3743 */                 j = 0;
/* 3744 */                 localObject2 = null;
/*      */               }
/*      */               else {
/* 3747 */                 localObject2 = ((TreePath)localObject2).getParentPath();
/*      */               }
/*      */             }
/* 3750 */             if (j != 0) {
/* 3751 */               BasicTreeUI.this.tree.makeVisible(localObject1[i]);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 3757 */       Object localObject1 = BasicTreeUI.this.getLeadSelectionPath();
/* 3758 */       BasicTreeUI.this.lastSelectedRow = BasicTreeUI.this.tree.getMinSelectionRow();
/* 3759 */       TreePath localTreePath = BasicTreeUI.this.tree.getSelectionModel().getLeadSelectionPath();
/* 3760 */       BasicTreeUI.this.setAnchorSelectionPath(localTreePath);
/* 3761 */       BasicTreeUI.this.setLeadSelectionPath(localTreePath);
/*      */ 
/* 3763 */       Object localObject2 = paramTreeSelectionEvent.getPaths();
/*      */ 
/* 3765 */       Rectangle localRectangle2 = BasicTreeUI.this.tree.getVisibleRect();
/* 3766 */       int k = 1;
/* 3767 */       int m = BasicTreeUI.this.tree.getWidth();
/*      */       Rectangle localRectangle1;
/* 3769 */       if (localObject2 != null) {
/* 3770 */         int i1 = localObject2.length;
/*      */ 
/* 3772 */         if (i1 > 4) {
/* 3773 */           BasicTreeUI.this.tree.repaint();
/* 3774 */           k = 0;
/*      */         }
/*      */         else {
/* 3777 */           for (int n = 0; n < i1; n++) {
/* 3778 */             localRectangle1 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, localObject2[n]);
/*      */ 
/* 3780 */             if ((localRectangle1 != null) && (localRectangle2.intersects(localRectangle1)))
/*      */             {
/* 3782 */               BasicTreeUI.this.tree.repaint(0, localRectangle1.y, m, localRectangle1.height);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 3787 */       if (k != 0) {
/* 3788 */         localRectangle1 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, (TreePath)localObject1);
/* 3789 */         if ((localRectangle1 != null) && (localRectangle2.intersects(localRectangle1)))
/* 3790 */           BasicTreeUI.this.tree.repaint(0, localRectangle1.y, m, localRectangle1.height);
/* 3791 */         localRectangle1 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, localTreePath);
/* 3792 */         if ((localRectangle1 != null) && (localRectangle2.intersects(localRectangle1)))
/* 3793 */           BasicTreeUI.this.tree.repaint(0, localRectangle1.y, m, localRectangle1.height);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeExpanded(TreeExpansionEvent paramTreeExpansionEvent)
/*      */     {
/* 3802 */       if ((paramTreeExpansionEvent != null) && (BasicTreeUI.this.tree != null)) {
/* 3803 */         TreePath localTreePath = paramTreeExpansionEvent.getPath();
/*      */ 
/* 3805 */         BasicTreeUI.this.updateExpandedDescendants(localTreePath);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeCollapsed(TreeExpansionEvent paramTreeExpansionEvent) {
/* 3810 */       if ((paramTreeExpansionEvent != null) && (BasicTreeUI.this.tree != null)) {
/* 3811 */         TreePath localTreePath = paramTreeExpansionEvent.getPath();
/*      */ 
/* 3813 */         BasicTreeUI.this.completeEditing();
/* 3814 */         if ((localTreePath != null) && (BasicTreeUI.this.tree.isVisible(localTreePath))) {
/* 3815 */           BasicTreeUI.this.treeState.setExpandedState(localTreePath, false);
/* 3816 */           BasicTreeUI.this.updateLeadSelectionRow();
/* 3817 */           BasicTreeUI.this.updateSize();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 3826 */       if ((BasicTreeUI.this.treeState != null) && (paramTreeModelEvent != null)) {
/* 3827 */         TreePath localTreePath1 = paramTreeModelEvent.getTreePath();
/* 3828 */         int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/* 3829 */         if ((arrayOfInt == null) || (arrayOfInt.length == 0))
/*      */         {
/* 3831 */           BasicTreeUI.this.treeState.treeNodesChanged(paramTreeModelEvent);
/* 3832 */           BasicTreeUI.this.updateSize();
/*      */         }
/* 3834 */         else if (BasicTreeUI.this.treeState.isExpanded(localTreePath1))
/*      */         {
/* 3838 */           int i = arrayOfInt[0];
/* 3839 */           for (int j = arrayOfInt.length - 1; j > 0; j--) {
/* 3840 */             i = Math.min(arrayOfInt[j], i);
/*      */           }
/* 3842 */           Object localObject = BasicTreeUI.this.treeModel.getChild(localTreePath1.getLastPathComponent(), i);
/*      */ 
/* 3844 */           TreePath localTreePath2 = localTreePath1.pathByAddingChild(localObject);
/* 3845 */           Rectangle localRectangle1 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, localTreePath2);
/*      */ 
/* 3848 */           BasicTreeUI.this.treeState.treeNodesChanged(paramTreeModelEvent);
/*      */ 
/* 3851 */           BasicTreeUI.this.updateSize0();
/*      */ 
/* 3854 */           Rectangle localRectangle2 = BasicTreeUI.this.getPathBounds(BasicTreeUI.this.tree, localTreePath2);
/* 3855 */           if ((localRectangle1 == null) || (localRectangle2 == null)) {
/* 3856 */             return;
/*      */           }
/*      */ 
/* 3859 */           if ((arrayOfInt.length == 1) && (localRectangle2.height == localRectangle1.height))
/*      */           {
/* 3861 */             BasicTreeUI.this.tree.repaint(0, localRectangle1.y, BasicTreeUI.this.tree.getWidth(), localRectangle1.height);
/*      */           }
/*      */           else
/*      */           {
/* 3865 */             BasicTreeUI.this.tree.repaint(0, localRectangle1.y, BasicTreeUI.this.tree.getWidth(), BasicTreeUI.this.tree.getHeight() - localRectangle1.y);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 3871 */           BasicTreeUI.this.treeState.treeNodesChanged(paramTreeModelEvent);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeNodesInserted(TreeModelEvent paramTreeModelEvent) {
/* 3877 */       if ((BasicTreeUI.this.treeState != null) && (paramTreeModelEvent != null)) {
/* 3878 */         BasicTreeUI.this.treeState.treeNodesInserted(paramTreeModelEvent);
/*      */ 
/* 3880 */         BasicTreeUI.this.updateLeadSelectionRow();
/*      */ 
/* 3882 */         TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*      */ 
/* 3884 */         if (BasicTreeUI.this.treeState.isExpanded(localTreePath)) {
/* 3885 */           BasicTreeUI.this.updateSize();
/*      */         }
/*      */         else
/*      */         {
/* 3891 */           int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/* 3892 */           int i = BasicTreeUI.this.treeModel.getChildCount(localTreePath.getLastPathComponent());
/*      */ 
/* 3895 */           if ((arrayOfInt != null) && (i - arrayOfInt.length == 0))
/* 3896 */             BasicTreeUI.this.updateSize();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent) {
/* 3902 */       if ((BasicTreeUI.this.treeState != null) && (paramTreeModelEvent != null)) {
/* 3903 */         BasicTreeUI.this.treeState.treeNodesRemoved(paramTreeModelEvent);
/*      */ 
/* 3905 */         BasicTreeUI.this.updateLeadSelectionRow();
/*      */ 
/* 3907 */         TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*      */ 
/* 3909 */         if ((BasicTreeUI.this.treeState.isExpanded(localTreePath)) || (BasicTreeUI.this.treeModel.getChildCount(localTreePath.getLastPathComponent()) == 0))
/*      */         {
/* 3911 */           BasicTreeUI.this.updateSize();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3916 */     public void treeStructureChanged(TreeModelEvent paramTreeModelEvent) { if ((BasicTreeUI.this.treeState != null) && (paramTreeModelEvent != null)) {
/* 3917 */         BasicTreeUI.this.treeState.treeStructureChanged(paramTreeModelEvent);
/*      */ 
/* 3919 */         BasicTreeUI.this.updateLeadSelectionRow();
/*      */ 
/* 3921 */         TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*      */ 
/* 3923 */         if (localTreePath != null) {
/* 3924 */           localTreePath = localTreePath.getParentPath();
/*      */         }
/* 3926 */         if ((localTreePath == null) || (BasicTreeUI.this.treeState.isExpanded(localTreePath)))
/* 3927 */           BasicTreeUI.this.updateSize();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class KeyHandler extends KeyAdapter
/*      */   {
/*      */     protected Action repeatKeyAction;
/*      */     protected boolean isKeyDown;
/*      */ 
/*      */     public KeyHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void keyTyped(KeyEvent paramKeyEvent)
/*      */     {
/* 2728 */       BasicTreeUI.this.getHandler().keyTyped(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     public void keyPressed(KeyEvent paramKeyEvent) {
/* 2732 */       BasicTreeUI.this.getHandler().keyPressed(paramKeyEvent);
/*      */     }
/*      */ 
/*      */     public void keyReleased(KeyEvent paramKeyEvent) {
/* 2736 */       BasicTreeUI.this.getHandler().keyReleased(paramKeyEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class MouseHandler extends MouseAdapter
/*      */     implements MouseMotionListener
/*      */   {
/*      */     public MouseHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 2856 */       BasicTreeUI.this.getHandler().mousePressed(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 2860 */       BasicTreeUI.this.getHandler().mouseDragged(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/* 2869 */       BasicTreeUI.this.getHandler().mouseMoved(paramMouseEvent);
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 2873 */       BasicTreeUI.this.getHandler().mouseReleased(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class MouseInputHandler
/*      */     implements MouseInputListener
/*      */   {
/*      */     protected Component source;
/*      */     protected Component destination;
/*      */     private Component focusComponent;
/*      */     private boolean dispatchedEvent;
/*      */ 
/*      */     public MouseInputHandler(Component paramComponent1, Component paramMouseEvent, MouseEvent arg4)
/*      */     {
/* 3110 */       this(paramComponent1, paramMouseEvent, localMouseEvent, null);
/*      */     }
/*      */ 
/*      */     MouseInputHandler(Component paramComponent1, Component paramMouseEvent, MouseEvent paramComponent2, Component arg5)
/*      */     {
/* 3115 */       this.source = paramComponent1;
/* 3116 */       this.destination = paramMouseEvent;
/* 3117 */       this.source.addMouseListener(this);
/* 3118 */       this.source.addMouseMotionListener(this);
/*      */ 
/* 3120 */       SwingUtilities2.setSkipClickCount(paramMouseEvent, paramComponent2.getClickCount() - 1);
/*      */ 
/* 3124 */       paramMouseEvent.dispatchEvent(SwingUtilities.convertMouseEvent(paramComponent1, paramComponent2, paramMouseEvent));
/*      */       Object localObject;
/* 3126 */       this.focusComponent = localObject;
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 3130 */       if (this.destination != null) {
/* 3131 */         this.dispatchedEvent = true;
/* 3132 */         this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, paramMouseEvent, this.destination));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 3141 */       if (this.destination != null) {
/* 3142 */         this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, paramMouseEvent, this.destination));
/*      */       }
/* 3144 */       removeFromSource();
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 3148 */       if (!SwingUtilities.isLeftMouseButton(paramMouseEvent))
/* 3149 */         removeFromSource();
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/* 3154 */       if (!SwingUtilities.isLeftMouseButton(paramMouseEvent))
/* 3155 */         removeFromSource();
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent)
/*      */     {
/* 3160 */       if (this.destination != null) {
/* 3161 */         this.dispatchedEvent = true;
/* 3162 */         this.destination.dispatchEvent(SwingUtilities.convertMouseEvent(this.source, paramMouseEvent, this.destination));
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseMoved(MouseEvent paramMouseEvent)
/*      */     {
/* 3168 */       removeFromSource();
/*      */     }
/*      */ 
/*      */     protected void removeFromSource() {
/* 3172 */       if (this.source != null) {
/* 3173 */         this.source.removeMouseListener(this);
/* 3174 */         this.source.removeMouseMotionListener(this);
/* 3175 */         if ((this.focusComponent != null) && (this.focusComponent == this.destination) && (!this.dispatchedEvent) && ((this.focusComponent instanceof JTextField)))
/*      */         {
/* 3178 */           ((JTextField)this.focusComponent).selectAll();
/*      */         }
/*      */       }
/* 3181 */       this.source = (this.destination = null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class NodeDimensionsHandler extends AbstractLayoutCache.NodeDimensions
/*      */   {
/*      */     public NodeDimensionsHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Rectangle getNodeDimensions(Object paramObject, int paramInt1, int paramInt2, boolean paramBoolean, Rectangle paramRectangle)
/*      */     {
/*      */       Object localObject;
/* 2784 */       if ((BasicTreeUI.this.editingComponent != null) && (BasicTreeUI.this.editingRow == paramInt1)) {
/* 2785 */         localObject = BasicTreeUI.this.editingComponent.getPreferredSize();
/*      */ 
/* 2787 */         int i = BasicTreeUI.this.getRowHeight();
/*      */ 
/* 2789 */         if ((i > 0) && (i != ((Dimension)localObject).height))
/* 2790 */           ((Dimension)localObject).height = i;
/* 2791 */         if (paramRectangle != null) {
/* 2792 */           paramRectangle.x = getRowX(paramInt1, paramInt2);
/* 2793 */           paramRectangle.width = ((Dimension)localObject).width;
/* 2794 */           paramRectangle.height = ((Dimension)localObject).height;
/*      */         }
/*      */         else {
/* 2797 */           paramRectangle = new Rectangle(getRowX(paramInt1, paramInt2), 0, ((Dimension)localObject).width, ((Dimension)localObject).height);
/*      */         }
/*      */ 
/* 2800 */         return paramRectangle;
/*      */       }
/*      */ 
/* 2803 */       if (BasicTreeUI.this.currentCellRenderer != null)
/*      */       {
/* 2806 */         localObject = BasicTreeUI.this.currentCellRenderer.getTreeCellRendererComponent(BasicTreeUI.this.tree, paramObject, BasicTreeUI.this.tree.isRowSelected(paramInt1), paramBoolean, BasicTreeUI.this.treeModel.isLeaf(paramObject), paramInt1, false);
/*      */ 
/* 2810 */         if (BasicTreeUI.this.tree != null)
/*      */         {
/* 2812 */           BasicTreeUI.this.rendererPane.add((Component)localObject);
/* 2813 */           ((Component)localObject).validate();
/*      */         }
/* 2815 */         Dimension localDimension = ((Component)localObject).getPreferredSize();
/*      */ 
/* 2817 */         if (paramRectangle != null) {
/* 2818 */           paramRectangle.x = getRowX(paramInt1, paramInt2);
/* 2819 */           paramRectangle.width = localDimension.width;
/* 2820 */           paramRectangle.height = localDimension.height;
/*      */         }
/*      */         else {
/* 2823 */           paramRectangle = new Rectangle(getRowX(paramInt1, paramInt2), 0, localDimension.width, localDimension.height);
/*      */         }
/*      */ 
/* 2826 */         return paramRectangle;
/*      */       }
/* 2828 */       return null;
/*      */     }
/*      */ 
/*      */     protected int getRowX(int paramInt1, int paramInt2)
/*      */     {
/* 2835 */       return BasicTreeUI.this.getRowX(paramInt1, paramInt2);
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
/* 2891 */       BasicTreeUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class SelectionModelPropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public SelectionModelPropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 2909 */       BasicTreeUI.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeCancelEditingAction extends AbstractAction
/*      */   {
/*      */     public TreeCancelEditingAction(String arg2)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 3081 */       if (BasicTreeUI.this.tree != null)
/* 3082 */         BasicTreeUI.SHARED_ACTION.cancelEditing(BasicTreeUI.this.tree, BasicTreeUI.this);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 3086 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled()) && (BasicTreeUI.this.isEditing(BasicTreeUI.this.tree));
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeExpansionHandler
/*      */     implements TreeExpansionListener
/*      */   {
/*      */     public TreeExpansionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void treeExpanded(TreeExpansionEvent paramTreeExpansionEvent)
/*      */     {
/* 2537 */       BasicTreeUI.this.getHandler().treeExpanded(paramTreeExpansionEvent);
/*      */     }
/*      */ 
/*      */     public void treeCollapsed(TreeExpansionEvent paramTreeExpansionEvent)
/*      */     {
/* 2544 */       BasicTreeUI.this.getHandler().treeCollapsed(paramTreeExpansionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeHomeAction extends AbstractAction
/*      */   {
/*      */     protected int direction;
/*      */     private boolean addToSelection;
/*      */     private boolean changeSelection;
/*      */ 
/*      */     public TreeHomeAction(int paramString, String arg3)
/*      */     {
/* 3030 */       this(paramString, str, false, true);
/*      */     }
/*      */ 
/*      */     private TreeHomeAction(int paramString, String paramBoolean1, boolean paramBoolean2, boolean arg5)
/*      */     {
/* 3036 */       this.direction = paramString;
/*      */       boolean bool;
/* 3037 */       this.changeSelection = bool;
/* 3038 */       this.addToSelection = paramBoolean2;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 3042 */       if (BasicTreeUI.this.tree != null)
/* 3043 */         BasicTreeUI.SHARED_ACTION.home(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 3048 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled());
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeIncrementAction extends AbstractAction
/*      */   {
/*      */     protected int direction;
/*      */     private boolean addToSelection;
/*      */     private boolean changeSelection;
/*      */ 
/*      */     public TreeIncrementAction(int paramString, String arg3)
/*      */     {
/* 2995 */       this(paramString, str, false, true);
/*      */     }
/*      */ 
/*      */     private TreeIncrementAction(int paramString, String paramBoolean1, boolean paramBoolean2, boolean arg5)
/*      */     {
/* 3001 */       this.direction = paramString;
/* 3002 */       this.addToSelection = paramBoolean2;
/*      */       boolean bool;
/* 3003 */       this.changeSelection = bool;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 3007 */       if (BasicTreeUI.this.tree != null)
/* 3008 */         BasicTreeUI.SHARED_ACTION.increment(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 3013 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled());
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeModelHandler
/*      */     implements TreeModelListener
/*      */   {
/*      */     public TreeModelHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 2636 */       BasicTreeUI.this.getHandler().treeNodesChanged(paramTreeModelEvent);
/*      */     }
/*      */ 
/*      */     public void treeNodesInserted(TreeModelEvent paramTreeModelEvent) {
/* 2640 */       BasicTreeUI.this.getHandler().treeNodesInserted(paramTreeModelEvent);
/*      */     }
/*      */ 
/*      */     public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent) {
/* 2644 */       BasicTreeUI.this.getHandler().treeNodesRemoved(paramTreeModelEvent);
/*      */     }
/*      */ 
/*      */     public void treeStructureChanged(TreeModelEvent paramTreeModelEvent) {
/* 2648 */       BasicTreeUI.this.getHandler().treeStructureChanged(paramTreeModelEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreePageAction extends AbstractAction
/*      */   {
/*      */     protected int direction;
/*      */     private boolean addToSelection;
/*      */     private boolean changeSelection;
/*      */ 
/*      */     public TreePageAction(int paramString, String arg3)
/*      */     {
/* 2959 */       this(paramString, str, false, true);
/*      */     }
/*      */ 
/*      */     private TreePageAction(int paramString, String paramBoolean1, boolean paramBoolean2, boolean arg5)
/*      */     {
/* 2965 */       this.direction = paramString;
/* 2966 */       this.addToSelection = paramBoolean2;
/*      */       boolean bool;
/* 2967 */       this.changeSelection = bool;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2971 */       if (BasicTreeUI.this.tree != null)
/* 2972 */         BasicTreeUI.SHARED_ACTION.page(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.addToSelection, this.changeSelection);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 2977 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled());
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeSelectionHandler
/*      */     implements TreeSelectionListener
/*      */   {
/*      */     public TreeSelectionHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */     {
/* 2669 */       BasicTreeUI.this.getHandler().valueChanged(paramTreeSelectionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeToggleAction extends AbstractAction
/*      */   {
/*      */     public TreeToggleAction(String arg2)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 3062 */       if (BasicTreeUI.this.tree != null)
/* 3063 */         BasicTreeUI.SHARED_ACTION.toggle(BasicTreeUI.this.tree, BasicTreeUI.this);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 3067 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled());
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TreeTransferHandler extends TransferHandler
/*      */     implements UIResource, Comparator<TreePath>
/*      */   {
/*      */     private JTree tree;
/*      */ 
/*      */     protected Transferable createTransferable(JComponent paramJComponent)
/*      */     {
/* 3202 */       if ((paramJComponent instanceof JTree)) {
/* 3203 */         this.tree = ((JTree)paramJComponent);
/* 3204 */         TreePath[] arrayOfTreePath1 = this.tree.getSelectionPaths();
/*      */ 
/* 3206 */         if ((arrayOfTreePath1 == null) || (arrayOfTreePath1.length == 0)) {
/* 3207 */           return null;
/*      */         }
/*      */ 
/* 3210 */         StringBuffer localStringBuffer1 = new StringBuffer();
/* 3211 */         StringBuffer localStringBuffer2 = new StringBuffer();
/*      */ 
/* 3213 */         localStringBuffer2.append("<html>\n<body>\n<ul>\n");
/*      */ 
/* 3215 */         TreeModel localTreeModel = this.tree.getModel();
/* 3216 */         Object localObject1 = null;
/* 3217 */         TreePath[] arrayOfTreePath2 = getDisplayOrderPaths(arrayOfTreePath1);
/*      */ 
/* 3219 */         for (TreePath localTreePath : arrayOfTreePath2) {
/* 3220 */           Object localObject2 = localTreePath.getLastPathComponent();
/* 3221 */           boolean bool = localTreeModel.isLeaf(localObject2);
/* 3222 */           String str = getDisplayString(localTreePath, true, bool);
/*      */ 
/* 3224 */           localStringBuffer1.append(str + "\n");
/* 3225 */           localStringBuffer2.append("  <li>" + str + "\n");
/*      */         }
/*      */ 
/* 3229 */         localStringBuffer1.deleteCharAt(localStringBuffer1.length() - 1);
/* 3230 */         localStringBuffer2.append("</ul>\n</body>\n</html>");
/*      */ 
/* 3232 */         this.tree = null;
/*      */ 
/* 3234 */         return new BasicTransferable(localStringBuffer1.toString(), localStringBuffer2.toString());
/*      */       }
/*      */ 
/* 3237 */       return null;
/*      */     }
/*      */ 
/*      */     public int compare(TreePath paramTreePath1, TreePath paramTreePath2) {
/* 3241 */       int i = this.tree.getRowForPath(paramTreePath1);
/* 3242 */       int j = this.tree.getRowForPath(paramTreePath2);
/* 3243 */       return i - j;
/*      */     }
/*      */ 
/*      */     String getDisplayString(TreePath paramTreePath, boolean paramBoolean1, boolean paramBoolean2) {
/* 3247 */       int i = this.tree.getRowForPath(paramTreePath);
/* 3248 */       boolean bool = this.tree.getLeadSelectionRow() == i;
/* 3249 */       Object localObject = paramTreePath.getLastPathComponent();
/* 3250 */       return this.tree.convertValueToText(localObject, paramBoolean1, this.tree.isExpanded(i), paramBoolean2, i, bool);
/*      */     }
/*      */ 
/*      */     TreePath[] getDisplayOrderPaths(TreePath[] paramArrayOfTreePath)
/*      */     {
/* 3261 */       ArrayList localArrayList = new ArrayList();
/* 3262 */       for (TreePath localTreePath : paramArrayOfTreePath) {
/* 3263 */         localArrayList.add(localTreePath);
/*      */       }
/* 3265 */       Collections.sort(localArrayList, this);
/* 3266 */       int i = localArrayList.size();
/* 3267 */       TreePath[] arrayOfTreePath2 = new TreePath[i];
/* 3268 */       for (??? = 0; ??? < i; ???++) {
/* 3269 */         arrayOfTreePath2[???] = ((TreePath)localArrayList.get(???));
/*      */       }
/* 3271 */       return arrayOfTreePath2;
/*      */     }
/*      */ 
/*      */     public int getSourceActions(JComponent paramJComponent) {
/* 3275 */       return 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TreeTraverseAction extends AbstractAction
/*      */   {
/*      */     protected int direction;
/*      */     private boolean changeSelection;
/*      */ 
/*      */     public TreeTraverseAction(int paramString, String arg3)
/*      */     {
/* 2928 */       this(paramString, str, true);
/*      */     }
/*      */ 
/*      */     private TreeTraverseAction(int paramString, String paramBoolean, boolean arg4)
/*      */     {
/* 2933 */       this.direction = paramString;
/*      */       boolean bool;
/* 2934 */       this.changeSelection = bool;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 2938 */       if (BasicTreeUI.this.tree != null)
/* 2939 */         BasicTreeUI.SHARED_ACTION.traverse(BasicTreeUI.this.tree, BasicTreeUI.this, this.direction, this.changeSelection);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 2944 */       return (BasicTreeUI.this.tree != null) && (BasicTreeUI.this.tree.isEnabled());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTreeUI
 * JD-Core Version:    0.6.2
 */