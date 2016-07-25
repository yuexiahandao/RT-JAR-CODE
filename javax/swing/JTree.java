/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.ConstructorProperties;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.TreeExpansionEvent;
/*      */ import javax.swing.event.TreeExpansionListener;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ import javax.swing.event.TreeModelListener;
/*      */ import javax.swing.event.TreeSelectionEvent;
/*      */ import javax.swing.event.TreeSelectionListener;
/*      */ import javax.swing.event.TreeWillExpandListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TreeUI;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.tree.DefaultMutableTreeNode;
/*      */ import javax.swing.tree.DefaultTreeModel;
/*      */ import javax.swing.tree.DefaultTreeSelectionModel;
/*      */ import javax.swing.tree.ExpandVetoException;
/*      */ import javax.swing.tree.RowMapper;
/*      */ import javax.swing.tree.TreeCellEditor;
/*      */ import javax.swing.tree.TreeCellRenderer;
/*      */ import javax.swing.tree.TreeModel;
/*      */ import javax.swing.tree.TreeNode;
/*      */ import javax.swing.tree.TreePath;
/*      */ import javax.swing.tree.TreeSelectionModel;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.SwingUtilities2.Section;
/*      */ 
/*      */ public class JTree extends JComponent
/*      */   implements Scrollable, Accessible
/*      */ {
/*      */   private static final String uiClassID = "TreeUI";
/*      */   protected transient TreeModel treeModel;
/*      */   protected transient TreeSelectionModel selectionModel;
/*      */   protected boolean rootVisible;
/*      */   protected transient TreeCellRenderer cellRenderer;
/*      */   protected int rowHeight;
/*  180 */   private boolean rowHeightSet = false;
/*      */   private transient Hashtable<TreePath, Boolean> expandedState;
/*      */   protected boolean showsRootHandles;
/*  216 */   private boolean showsRootHandlesSet = false;
/*      */   protected transient TreeSelectionRedirector selectionRedirector;
/*      */   protected transient TreeCellEditor cellEditor;
/*      */   protected boolean editable;
/*      */   protected boolean largeModel;
/*      */   protected int visibleRowCount;
/*      */   protected boolean invokesStopCellEditing;
/*      */   protected boolean scrollsOnExpand;
/*  268 */   private boolean scrollsOnExpandSet = false;
/*      */   protected int toggleClickCount;
/*      */   protected transient TreeModelListener treeModelListener;
/*      */   private transient Stack<Stack<TreePath>> expandedStack;
/*      */   private TreePath leadPath;
/*      */   private TreePath anchorPath;
/*      */   private boolean expandsSelectedPaths;
/*      */   private boolean settingUI;
/*      */   private boolean dragEnabled;
/*  312 */   private DropMode dropMode = DropMode.USE_SELECTION;
/*      */   private transient DropLocation dropLocation;
/*  422 */   private int expandRow = -1;
/*      */   private TreeTimer dropTimer;
/*      */   private transient TreeExpansionListener uiTreeExpansionListener;
/*  453 */   private static int TEMP_STACK_SIZE = 11;
/*      */   public static final String CELL_RENDERER_PROPERTY = "cellRenderer";
/*      */   public static final String TREE_MODEL_PROPERTY = "model";
/*      */   public static final String ROOT_VISIBLE_PROPERTY = "rootVisible";
/*      */   public static final String SHOWS_ROOT_HANDLES_PROPERTY = "showsRootHandles";
/*      */   public static final String ROW_HEIGHT_PROPERTY = "rowHeight";
/*      */   public static final String CELL_EDITOR_PROPERTY = "cellEditor";
/*      */   public static final String EDITABLE_PROPERTY = "editable";
/*      */   public static final String LARGE_MODEL_PROPERTY = "largeModel";
/*      */   public static final String SELECTION_MODEL_PROPERTY = "selectionModel";
/*      */   public static final String VISIBLE_ROW_COUNT_PROPERTY = "visibleRowCount";
/*      */   public static final String INVOKES_STOP_CELL_EDITING_PROPERTY = "invokesStopCellEditing";
/*      */   public static final String SCROLLS_ON_EXPAND_PROPERTY = "scrollsOnExpand";
/*      */   public static final String TOGGLE_CLICK_COUNT_PROPERTY = "toggleClickCount";
/*      */   public static final String LEAD_SELECTION_PATH_PROPERTY = "leadSelectionPath";
/*      */   public static final String ANCHOR_SELECTION_PATH_PROPERTY = "anchorSelectionPath";
/*      */   public static final String EXPANDS_SELECTED_PATHS_PROPERTY = "expandsSelectedPaths";
/*      */ 
/*      */   protected static TreeModel getDefaultTreeModel()
/*      */   {
/*  502 */     DefaultMutableTreeNode localDefaultMutableTreeNode1 = new DefaultMutableTreeNode("JTree");
/*      */ 
/*  505 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = new DefaultMutableTreeNode("colors");
/*  506 */     localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
/*  507 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("blue"));
/*  508 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("violet"));
/*  509 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("red"));
/*  510 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("yellow"));
/*      */ 
/*  512 */     localDefaultMutableTreeNode2 = new DefaultMutableTreeNode("sports");
/*  513 */     localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
/*  514 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("basketball"));
/*  515 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("soccer"));
/*  516 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("football"));
/*  517 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("hockey"));
/*      */ 
/*  519 */     localDefaultMutableTreeNode2 = new DefaultMutableTreeNode("food");
/*  520 */     localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
/*  521 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("hot dogs"));
/*  522 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("pizza"));
/*  523 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("ravioli"));
/*  524 */     localDefaultMutableTreeNode2.add(new DefaultMutableTreeNode("bananas"));
/*  525 */     return new DefaultTreeModel(localDefaultMutableTreeNode1);
/*      */   }
/*      */ 
/*      */   protected static TreeModel createTreeModel(Object paramObject)
/*      */   {
/*      */     Object localObject;
/*  545 */     if (((paramObject instanceof Object[])) || ((paramObject instanceof Hashtable)) || ((paramObject instanceof Vector)))
/*      */     {
/*  547 */       localObject = new DefaultMutableTreeNode("root");
/*  548 */       DynamicUtilTreeNode.createChildren((DefaultMutableTreeNode)localObject, paramObject);
/*      */     }
/*      */     else {
/*  551 */       localObject = new DynamicUtilTreeNode("root", paramObject);
/*      */     }
/*  553 */     return new DefaultTreeModel((TreeNode)localObject, false);
/*      */   }
/*      */ 
/*      */   public JTree()
/*      */   {
/*  564 */     this(getDefaultTreeModel());
/*      */   }
/*      */ 
/*      */   public JTree(Object[] paramArrayOfObject)
/*      */   {
/*  578 */     this(createTreeModel(paramArrayOfObject));
/*  579 */     setRootVisible(false);
/*  580 */     setShowsRootHandles(true);
/*  581 */     expandRoot();
/*      */   }
/*      */ 
/*      */   public JTree(Vector<?> paramVector)
/*      */   {
/*  594 */     this(createTreeModel(paramVector));
/*  595 */     setRootVisible(false);
/*  596 */     setShowsRootHandles(true);
/*  597 */     expandRoot();
/*      */   }
/*      */ 
/*      */   public JTree(Hashtable<?, ?> paramHashtable)
/*      */   {
/*  611 */     this(createTreeModel(paramHashtable));
/*  612 */     setRootVisible(false);
/*  613 */     setShowsRootHandles(true);
/*  614 */     expandRoot();
/*      */   }
/*      */ 
/*      */   public JTree(TreeNode paramTreeNode)
/*      */   {
/*  627 */     this(paramTreeNode, false);
/*      */   }
/*      */ 
/*      */   public JTree(TreeNode paramTreeNode, boolean paramBoolean)
/*      */   {
/*  643 */     this(new DefaultTreeModel(paramTreeNode, paramBoolean));
/*      */   }
/*      */ 
/*      */   @ConstructorProperties({"model"})
/*      */   public JTree(TreeModel paramTreeModel)
/*      */   {
/*  655 */     this.expandedStack = new Stack();
/*  656 */     this.toggleClickCount = 2;
/*  657 */     this.expandedState = new Hashtable();
/*  658 */     setLayout(null);
/*  659 */     this.rowHeight = 16;
/*  660 */     this.visibleRowCount = 20;
/*  661 */     this.rootVisible = true;
/*  662 */     this.selectionModel = new DefaultTreeSelectionModel();
/*  663 */     this.cellRenderer = null;
/*  664 */     this.scrollsOnExpand = true;
/*  665 */     setOpaque(true);
/*  666 */     this.expandsSelectedPaths = true;
/*  667 */     updateUI();
/*  668 */     setModel(paramTreeModel);
/*      */   }
/*      */ 
/*      */   public TreeUI getUI()
/*      */   {
/*  677 */     return (TreeUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(TreeUI paramTreeUI)
/*      */   {
/*  694 */     if (this.ui != paramTreeUI) {
/*  695 */       this.settingUI = true;
/*  696 */       this.uiTreeExpansionListener = null;
/*      */       try {
/*  698 */         super.setUI(paramTreeUI);
/*      */       }
/*      */       finally {
/*  701 */         this.settingUI = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  714 */     setUI((TreeUI)UIManager.getUI(this));
/*      */ 
/*  716 */     SwingUtilities.updateRendererOrEditorUI(getCellRenderer());
/*  717 */     SwingUtilities.updateRendererOrEditorUI(getCellEditor());
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  729 */     return "TreeUI";
/*      */   }
/*      */ 
/*      */   public TreeCellRenderer getCellRenderer()
/*      */   {
/*  740 */     return this.cellRenderer;
/*      */   }
/*      */ 
/*      */   public void setCellRenderer(TreeCellRenderer paramTreeCellRenderer)
/*      */   {
/*  756 */     TreeCellRenderer localTreeCellRenderer = this.cellRenderer;
/*      */ 
/*  758 */     this.cellRenderer = paramTreeCellRenderer;
/*  759 */     firePropertyChange("cellRenderer", localTreeCellRenderer, this.cellRenderer);
/*  760 */     invalidate();
/*      */   }
/*      */ 
/*      */   public void setEditable(boolean paramBoolean)
/*      */   {
/*  776 */     boolean bool = this.editable;
/*      */ 
/*  778 */     this.editable = paramBoolean;
/*  779 */     firePropertyChange("editable", bool, paramBoolean);
/*  780 */     if (this.accessibleContext != null)
/*  781 */       this.accessibleContext.firePropertyChange("AccessibleState", bool ? AccessibleState.EDITABLE : null, paramBoolean ? AccessibleState.EDITABLE : null);
/*      */   }
/*      */ 
/*      */   public boolean isEditable()
/*      */   {
/*  794 */     return this.editable;
/*      */   }
/*      */ 
/*      */   public void setCellEditor(TreeCellEditor paramTreeCellEditor)
/*      */   {
/*  812 */     TreeCellEditor localTreeCellEditor = this.cellEditor;
/*      */ 
/*  814 */     this.cellEditor = paramTreeCellEditor;
/*  815 */     firePropertyChange("cellEditor", localTreeCellEditor, paramTreeCellEditor);
/*  816 */     invalidate();
/*      */   }
/*      */ 
/*      */   public TreeCellEditor getCellEditor()
/*      */   {
/*  826 */     return this.cellEditor;
/*      */   }
/*      */ 
/*      */   public TreeModel getModel()
/*      */   {
/*  835 */     return this.treeModel;
/*      */   }
/*      */ 
/*      */   public void setModel(TreeModel paramTreeModel)
/*      */   {
/*  849 */     clearSelection();
/*      */ 
/*  851 */     TreeModel localTreeModel = this.treeModel;
/*      */ 
/*  853 */     if ((this.treeModel != null) && (this.treeModelListener != null)) {
/*  854 */       this.treeModel.removeTreeModelListener(this.treeModelListener);
/*      */     }
/*  856 */     if (this.accessibleContext != null) {
/*  857 */       if (this.treeModel != null) {
/*  858 */         this.treeModel.removeTreeModelListener((TreeModelListener)this.accessibleContext);
/*      */       }
/*  860 */       if (paramTreeModel != null) {
/*  861 */         paramTreeModel.addTreeModelListener((TreeModelListener)this.accessibleContext);
/*      */       }
/*      */     }
/*      */ 
/*  865 */     this.treeModel = paramTreeModel;
/*  866 */     clearToggledPaths();
/*  867 */     if (this.treeModel != null) {
/*  868 */       if (this.treeModelListener == null)
/*  869 */         this.treeModelListener = createTreeModelListener();
/*  870 */       if (this.treeModelListener != null) {
/*  871 */         this.treeModel.addTreeModelListener(this.treeModelListener);
/*      */       }
/*  873 */       if ((this.treeModel.getRoot() != null) && (!this.treeModel.isLeaf(this.treeModel.getRoot())))
/*      */       {
/*  875 */         this.expandedState.put(new TreePath(this.treeModel.getRoot()), Boolean.TRUE);
/*      */       }
/*      */     }
/*      */ 
/*  879 */     firePropertyChange("model", localTreeModel, this.treeModel);
/*  880 */     invalidate();
/*      */   }
/*      */ 
/*      */   public boolean isRootVisible()
/*      */   {
/*  890 */     return this.rootVisible;
/*      */   }
/*      */ 
/*      */   public void setRootVisible(boolean paramBoolean)
/*      */   {
/*  907 */     boolean bool = this.rootVisible;
/*      */ 
/*  909 */     this.rootVisible = paramBoolean;
/*  910 */     firePropertyChange("rootVisible", bool, this.rootVisible);
/*  911 */     if (this.accessibleContext != null)
/*  912 */       ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */   }
/*      */ 
/*      */   public void setShowsRootHandles(boolean paramBoolean)
/*      */   {
/*  936 */     boolean bool = this.showsRootHandles;
/*  937 */     TreeModel localTreeModel = getModel();
/*      */ 
/*  939 */     this.showsRootHandles = paramBoolean;
/*  940 */     this.showsRootHandlesSet = true;
/*  941 */     firePropertyChange("showsRootHandles", bool, this.showsRootHandles);
/*      */ 
/*  943 */     if (this.accessibleContext != null) {
/*  944 */       ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */     }
/*  946 */     invalidate();
/*      */   }
/*      */ 
/*      */   public boolean getShowsRootHandles()
/*      */   {
/*  957 */     return this.showsRootHandles;
/*      */   }
/*      */ 
/*      */   public void setRowHeight(int paramInt)
/*      */   {
/*  974 */     int i = this.rowHeight;
/*      */ 
/*  976 */     this.rowHeight = paramInt;
/*  977 */     this.rowHeightSet = true;
/*  978 */     firePropertyChange("rowHeight", i, this.rowHeight);
/*  979 */     invalidate();
/*      */   }
/*      */ 
/*      */   public int getRowHeight()
/*      */   {
/*  990 */     return this.rowHeight;
/*      */   }
/*      */ 
/*      */   public boolean isFixedRowHeight()
/*      */   {
/* 1000 */     return this.rowHeight > 0;
/*      */   }
/*      */ 
/*      */   public void setLargeModel(boolean paramBoolean)
/*      */   {
/* 1018 */     boolean bool = this.largeModel;
/*      */ 
/* 1020 */     this.largeModel = paramBoolean;
/* 1021 */     firePropertyChange("largeModel", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isLargeModel()
/*      */   {
/* 1031 */     return this.largeModel;
/*      */   }
/*      */ 
/*      */   public void setInvokesStopCellEditing(boolean paramBoolean)
/*      */   {
/* 1052 */     boolean bool = this.invokesStopCellEditing;
/*      */ 
/* 1054 */     this.invokesStopCellEditing = paramBoolean;
/* 1055 */     firePropertyChange("invokesStopCellEditing", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getInvokesStopCellEditing()
/*      */   {
/* 1068 */     return this.invokesStopCellEditing;
/*      */   }
/*      */ 
/*      */   public void setScrollsOnExpand(boolean paramBoolean)
/*      */   {
/* 1093 */     boolean bool = this.scrollsOnExpand;
/*      */ 
/* 1095 */     this.scrollsOnExpand = paramBoolean;
/* 1096 */     this.scrollsOnExpandSet = true;
/* 1097 */     firePropertyChange("scrollsOnExpand", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getScrollsOnExpand()
/*      */   {
/* 1107 */     return this.scrollsOnExpand;
/*      */   }
/*      */ 
/*      */   public void setToggleClickCount(int paramInt)
/*      */   {
/* 1122 */     int i = this.toggleClickCount;
/*      */ 
/* 1124 */     this.toggleClickCount = paramInt;
/* 1125 */     firePropertyChange("toggleClickCount", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getToggleClickCount()
/*      */   {
/* 1136 */     return this.toggleClickCount;
/*      */   }
/*      */ 
/*      */   public void setExpandsSelectedPaths(boolean paramBoolean)
/*      */   {
/* 1162 */     boolean bool = this.expandsSelectedPaths;
/*      */ 
/* 1164 */     this.expandsSelectedPaths = paramBoolean;
/* 1165 */     firePropertyChange("expandsSelectedPaths", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getExpandsSelectedPaths()
/*      */   {
/* 1177 */     return this.expandsSelectedPaths;
/*      */   }
/*      */ 
/*      */   public void setDragEnabled(boolean paramBoolean)
/*      */   {
/* 1214 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/* 1215 */       throw new HeadlessException();
/*      */     }
/* 1217 */     this.dragEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDragEnabled()
/*      */   {
/* 1228 */     return this.dragEnabled;
/*      */   }
/*      */ 
/*      */   public final void setDropMode(DropMode paramDropMode)
/*      */   {
/* 1260 */     if (paramDropMode != null) {
/* 1261 */       switch (1.$SwitchMap$javax$swing$DropMode[paramDropMode.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/* 1266 */         this.dropMode = paramDropMode;
/* 1267 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 1271 */     throw new IllegalArgumentException(paramDropMode + ": Unsupported drop mode for tree");
/*      */   }
/*      */ 
/*      */   public final DropMode getDropMode()
/*      */   {
/* 1282 */     return this.dropMode;
/*      */   }
/*      */ 
/*      */   DropLocation dropLocationForPoint(Point paramPoint)
/*      */   {
/* 1293 */     DropLocation localDropLocation = null;
/*      */ 
/* 1295 */     int i = getClosestRowForLocation(paramPoint.x, paramPoint.y);
/* 1296 */     Rectangle localRectangle = getRowBounds(i);
/* 1297 */     TreeModel localTreeModel = getModel();
/* 1298 */     Object localObject = localTreeModel == null ? null : localTreeModel.getRoot();
/* 1299 */     TreePath localTreePath1 = localObject == null ? null : new TreePath(localObject);
/*      */ 
/* 1303 */     int j = (i == -1) || (paramPoint.y < localRectangle.y) || (paramPoint.y >= localRectangle.y + localRectangle.height) ? 1 : 0;
/*      */ 
/* 1307 */     switch (1.$SwitchMap$javax$swing$DropMode[this.dropMode.ordinal()]) {
/*      */     case 1:
/*      */     case 2:
/* 1310 */       if (j != 0)
/* 1311 */         localDropLocation = new DropLocation(paramPoint, null, -1, null);
/*      */       else {
/* 1313 */         localDropLocation = new DropLocation(paramPoint, getPathForRow(i), -1, null);
/*      */       }
/*      */ 
/* 1316 */       break;
/*      */     case 3:
/*      */     case 4:
/* 1319 */       if (i == -1) {
/* 1320 */         if ((localObject != null) && (!localTreeModel.isLeaf(localObject)) && (isExpanded(localTreePath1)))
/* 1321 */           localDropLocation = new DropLocation(paramPoint, localTreePath1, 0, null);
/*      */         else {
/* 1323 */           localDropLocation = new DropLocation(paramPoint, null, -1, null);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1329 */         boolean bool = (this.dropMode == DropMode.ON_OR_INSERT) || (!localTreeModel.isLeaf(getPathForRow(i).getLastPathComponent()));
/*      */ 
/* 1332 */         SwingUtilities2.Section localSection = SwingUtilities2.liesInVertical(localRectangle, paramPoint, bool);
/*      */         TreePath localTreePath2;
/*      */         TreePath localTreePath3;
/* 1333 */         if (localSection == SwingUtilities2.Section.LEADING) {
/* 1334 */           localTreePath2 = getPathForRow(i);
/* 1335 */           localTreePath3 = localTreePath2.getParentPath();
/* 1336 */         } else if (localSection == SwingUtilities2.Section.TRAILING) {
/* 1337 */           int k = i + 1;
/* 1338 */           if (k >= getRowCount()) {
/* 1339 */             if ((localTreeModel.isLeaf(localObject)) || (!isExpanded(localTreePath1))) {
/* 1340 */               localDropLocation = new DropLocation(paramPoint, null, -1, null); break;
/*      */             }
/* 1342 */             localTreePath3 = localTreePath1;
/* 1343 */             k = localTreeModel.getChildCount(localObject);
/* 1344 */             localDropLocation = new DropLocation(paramPoint, localTreePath3, k, null);
/*      */ 
/* 1347 */             break;
/*      */           }
/*      */ 
/* 1350 */           localTreePath2 = getPathForRow(k);
/* 1351 */           localTreePath3 = localTreePath2.getParentPath();
/*      */         } else {
/* 1353 */           assert (bool);
/* 1354 */           localDropLocation = new DropLocation(paramPoint, getPathForRow(i), -1, null);
/* 1355 */           break;
/*      */         }
/*      */ 
/* 1358 */         if (localTreePath3 != null) {
/* 1359 */           localDropLocation = new DropLocation(paramPoint, localTreePath3, localTreeModel.getIndexOfChild(localTreePath3.getLastPathComponent(), localTreePath2.getLastPathComponent()), null);
/*      */         }
/* 1362 */         else if ((bool) || (!localTreeModel.isLeaf(localObject)))
/* 1363 */           localDropLocation = new DropLocation(paramPoint, localTreePath1, -1, null);
/*      */         else {
/* 1365 */           localDropLocation = new DropLocation(paramPoint, null, -1, null);
/*      */         }
/*      */       }
/* 1368 */       break;
/*      */     default:
/* 1370 */       if (!$assertionsDisabled) throw new AssertionError("Unexpected drop mode");
/*      */       break;
/*      */     }
/* 1373 */     if ((j != 0) || (i != this.expandRow)) {
/* 1374 */       cancelDropTimer();
/*      */     }
/*      */ 
/* 1377 */     if ((j == 0) && (i != this.expandRow) && 
/* 1378 */       (isCollapsed(i))) {
/* 1379 */       this.expandRow = i;
/* 1380 */       startDropTimer();
/*      */     }
/*      */ 
/* 1384 */     return localDropLocation;
/*      */   }
/*      */ 
/*      */   Object setDropLocation(TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean)
/*      */   {
/* 1424 */     Object localObject1 = null;
/* 1425 */     DropLocation localDropLocation = (DropLocation)paramDropLocation;
/*      */ 
/* 1427 */     if (this.dropMode == DropMode.USE_SELECTION) {
/* 1428 */       if (localDropLocation == null) {
/* 1429 */         if ((!paramBoolean) && (paramObject != null)) {
/* 1430 */           setSelectionPaths(((TreePath[][])(TreePath[][])paramObject)[0]);
/* 1431 */           setAnchorSelectionPath(((TreePath[][])(TreePath[][])paramObject)[1][0]);
/* 1432 */           setLeadSelectionPath(((TreePath[][])(TreePath[][])paramObject)[1][1]);
/*      */         }
/*      */       } else {
/* 1435 */         if (this.dropLocation == null) {
/* 1436 */           localObject2 = getSelectionPaths();
/* 1437 */           if (localObject2 == null) {
/* 1438 */             localObject2 = new TreePath[0];
/*      */           }
/*      */ 
/* 1441 */           localObject1 = new TreePath[][] { localObject2, { getAnchorSelectionPath(), getLeadSelectionPath() } };
/*      */         }
/*      */         else {
/* 1444 */           localObject1 = paramObject;
/*      */         }
/*      */ 
/* 1447 */         setSelectionPath(localDropLocation.getPath());
/*      */       }
/*      */     }
/*      */ 
/* 1451 */     Object localObject2 = this.dropLocation;
/* 1452 */     this.dropLocation = localDropLocation;
/* 1453 */     firePropertyChange("dropLocation", localObject2, this.dropLocation);
/*      */ 
/* 1455 */     return localObject1;
/*      */   }
/*      */ 
/*      */   void dndDone()
/*      */   {
/* 1463 */     cancelDropTimer();
/* 1464 */     this.dropTimer = null;
/*      */   }
/*      */ 
/*      */   public final DropLocation getDropLocation()
/*      */   {
/* 1486 */     return this.dropLocation;
/*      */   }
/*      */ 
/*      */   private void startDropTimer() {
/* 1490 */     if (this.dropTimer == null) {
/* 1491 */       this.dropTimer = new TreeTimer();
/*      */     }
/* 1493 */     this.dropTimer.start();
/*      */   }
/*      */ 
/*      */   private void cancelDropTimer() {
/* 1497 */     if ((this.dropTimer != null) && (this.dropTimer.isRunning())) {
/* 1498 */       this.expandRow = -1;
/* 1499 */       this.dropTimer.stop();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isPathEditable(TreePath paramTreePath)
/*      */   {
/* 1513 */     return isEditable();
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 1533 */     String str = null;
/*      */ 
/* 1535 */     if (paramMouseEvent != null) {
/* 1536 */       Point localPoint = paramMouseEvent.getPoint();
/* 1537 */       int i = getRowForLocation(localPoint.x, localPoint.y);
/* 1538 */       TreeCellRenderer localTreeCellRenderer = getCellRenderer();
/*      */ 
/* 1540 */       if ((i != -1) && (localTreeCellRenderer != null)) {
/* 1541 */         TreePath localTreePath = getPathForRow(i);
/* 1542 */         Object localObject = localTreePath.getLastPathComponent();
/* 1543 */         Component localComponent = localTreeCellRenderer.getTreeCellRendererComponent(this, localObject, isRowSelected(i), isExpanded(i), getModel().isLeaf(localObject), i, true);
/*      */ 
/* 1548 */         if ((localComponent instanceof JComponent))
/*      */         {
/* 1550 */           Rectangle localRectangle = getPathBounds(localTreePath);
/*      */ 
/* 1552 */           localPoint.translate(-localRectangle.x, -localRectangle.y);
/* 1553 */           MouseEvent localMouseEvent = new MouseEvent(localComponent, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*      */ 
/* 1563 */           str = ((JComponent)localComponent).getToolTipText(localMouseEvent);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1568 */     if (str == null) {
/* 1569 */       str = getToolTipText();
/*      */     }
/* 1571 */     return str;
/*      */   }
/*      */ 
/*      */   public String convertValueToText(Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
/*      */   {
/* 1592 */     if (paramObject != null) {
/* 1593 */       String str = paramObject.toString();
/* 1594 */       if (str != null) {
/* 1595 */         return str;
/*      */       }
/*      */     }
/* 1598 */     return "";
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */   {
/* 1615 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 1617 */     if (localTreeUI != null)
/* 1618 */       return localTreeUI.getRowCount(this);
/* 1619 */     return 0;
/*      */   }
/*      */ 
/*      */   public void setSelectionPath(TreePath paramTreePath)
/*      */   {
/* 1631 */     getSelectionModel().setSelectionPath(paramTreePath);
/*      */   }
/*      */ 
/*      */   public void setSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/* 1644 */     getSelectionModel().setSelectionPaths(paramArrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void setLeadSelectionPath(TreePath paramTreePath)
/*      */   {
/* 1661 */     TreePath localTreePath = this.leadPath;
/*      */ 
/* 1663 */     this.leadPath = paramTreePath;
/* 1664 */     firePropertyChange("leadSelectionPath", localTreePath, paramTreePath);
/*      */ 
/* 1666 */     if (this.accessibleContext != null)
/* 1667 */       ((AccessibleJTree)this.accessibleContext).fireActiveDescendantPropertyChange(localTreePath, paramTreePath);
/*      */   }
/*      */ 
/*      */   public void setAnchorSelectionPath(TreePath paramTreePath)
/*      */   {
/* 1686 */     TreePath localTreePath = this.anchorPath;
/*      */ 
/* 1688 */     this.anchorPath = paramTreePath;
/* 1689 */     firePropertyChange("anchorSelectionPath", localTreePath, paramTreePath);
/*      */   }
/*      */ 
/*      */   public void setSelectionRow(int paramInt)
/*      */   {
/* 1699 */     int[] arrayOfInt = { paramInt };
/*      */ 
/* 1701 */     setSelectionRows(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public void setSelectionRows(int[] paramArrayOfInt)
/*      */   {
/* 1717 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 1719 */     if ((localTreeUI != null) && (paramArrayOfInt != null)) {
/* 1720 */       int i = paramArrayOfInt.length;
/* 1721 */       TreePath[] arrayOfTreePath = new TreePath[i];
/*      */ 
/* 1723 */       for (int j = 0; j < i; j++) {
/* 1724 */         arrayOfTreePath[j] = localTreeUI.getPathForRow(this, paramArrayOfInt[j]);
/*      */       }
/* 1726 */       setSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addSelectionPath(TreePath paramTreePath)
/*      */   {
/* 1743 */     getSelectionModel().addSelectionPath(paramTreePath);
/*      */   }
/*      */ 
/*      */   public void addSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/* 1760 */     getSelectionModel().addSelectionPaths(paramArrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void addSelectionRow(int paramInt)
/*      */   {
/* 1770 */     int[] arrayOfInt = { paramInt };
/*      */ 
/* 1772 */     addSelectionRows(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public void addSelectionRows(int[] paramArrayOfInt)
/*      */   {
/* 1782 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 1784 */     if ((localTreeUI != null) && (paramArrayOfInt != null)) {
/* 1785 */       int i = paramArrayOfInt.length;
/* 1786 */       TreePath[] arrayOfTreePath = new TreePath[i];
/*      */ 
/* 1788 */       for (int j = 0; j < i; j++)
/* 1789 */         arrayOfTreePath[j] = localTreeUI.getPathForRow(this, paramArrayOfInt[j]);
/* 1790 */       addSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getLastSelectedPathComponent()
/*      */   {
/* 1805 */     TreePath localTreePath = getSelectionModel().getSelectionPath();
/*      */ 
/* 1807 */     if (localTreePath != null)
/* 1808 */       return localTreePath.getLastPathComponent();
/* 1809 */     return null;
/*      */   }
/*      */ 
/*      */   public TreePath getLeadSelectionPath()
/*      */   {
/* 1817 */     return this.leadPath;
/*      */   }
/*      */ 
/*      */   public TreePath getAnchorSelectionPath()
/*      */   {
/* 1826 */     return this.anchorPath;
/*      */   }
/*      */ 
/*      */   public TreePath getSelectionPath()
/*      */   {
/* 1836 */     return getSelectionModel().getSelectionPath();
/*      */   }
/*      */ 
/*      */   public TreePath[] getSelectionPaths()
/*      */   {
/* 1846 */     TreePath[] arrayOfTreePath = getSelectionModel().getSelectionPaths();
/*      */ 
/* 1848 */     return (arrayOfTreePath != null) && (arrayOfTreePath.length > 0) ? arrayOfTreePath : null;
/*      */   }
/*      */ 
/*      */   public int[] getSelectionRows()
/*      */   {
/* 1862 */     return getSelectionModel().getSelectionRows();
/*      */   }
/*      */ 
/*      */   public int getSelectionCount()
/*      */   {
/* 1871 */     return this.selectionModel.getSelectionCount();
/*      */   }
/*      */ 
/*      */   public int getMinSelectionRow()
/*      */   {
/* 1881 */     return getSelectionModel().getMinSelectionRow();
/*      */   }
/*      */ 
/*      */   public int getMaxSelectionRow()
/*      */   {
/* 1891 */     return getSelectionModel().getMaxSelectionRow();
/*      */   }
/*      */ 
/*      */   public int getLeadSelectionRow()
/*      */   {
/* 1902 */     TreePath localTreePath = getLeadSelectionPath();
/*      */ 
/* 1904 */     if (localTreePath != null) {
/* 1905 */       return getRowForPath(localTreePath);
/*      */     }
/* 1907 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean isPathSelected(TreePath paramTreePath)
/*      */   {
/* 1917 */     return getSelectionModel().isPathSelected(paramTreePath);
/*      */   }
/*      */ 
/*      */   public boolean isRowSelected(int paramInt)
/*      */   {
/* 1928 */     return getSelectionModel().isRowSelected(paramInt);
/*      */   }
/*      */ 
/*      */   public Enumeration<TreePath> getExpandedDescendants(TreePath paramTreePath)
/*      */   {
/* 1947 */     if (!isExpanded(paramTreePath)) {
/* 1948 */       return null;
/*      */     }
/* 1950 */     Enumeration localEnumeration = this.expandedState.keys();
/* 1951 */     Vector localVector = null;
/*      */ 
/* 1955 */     if (localEnumeration != null) {
/* 1956 */       while (localEnumeration.hasMoreElements()) {
/* 1957 */         TreePath localTreePath = (TreePath)localEnumeration.nextElement();
/* 1958 */         Object localObject = this.expandedState.get(localTreePath);
/*      */ 
/* 1962 */         if ((localTreePath != paramTreePath) && (localObject != null) && (((Boolean)localObject).booleanValue()) && (paramTreePath.isDescendant(localTreePath)) && (isVisible(localTreePath)))
/*      */         {
/* 1965 */           if (localVector == null) {
/* 1966 */             localVector = new Vector();
/*      */           }
/* 1968 */           localVector.addElement(localTreePath);
/*      */         }
/*      */       }
/*      */     }
/* 1972 */     if (localVector == null) {
/* 1973 */       Set localSet = Collections.emptySet();
/* 1974 */       return Collections.enumeration(localSet);
/*      */     }
/* 1976 */     return localVector.elements();
/*      */   }
/*      */ 
/*      */   public boolean hasBeenExpanded(TreePath paramTreePath)
/*      */   {
/* 1985 */     return (paramTreePath != null) && (this.expandedState.get(paramTreePath) != null);
/*      */   }
/*      */ 
/*      */   public boolean isExpanded(TreePath paramTreePath)
/*      */   {
/* 1997 */     if (paramTreePath == null) {
/* 1998 */       return false;
/*      */     }
/*      */     do
/*      */     {
/* 2002 */       Object localObject = this.expandedState.get(paramTreePath);
/* 2003 */       if ((localObject == null) || (!((Boolean)localObject).booleanValue()))
/* 2004 */         return false; 
/*      */     }
/* 2005 */     while ((paramTreePath = paramTreePath.getParentPath()) != null);
/*      */ 
/* 2007 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isExpanded(int paramInt)
/*      */   {
/* 2019 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2021 */     if (localTreeUI != null) {
/* 2022 */       TreePath localTreePath = localTreeUI.getPathForRow(this, paramInt);
/*      */ 
/* 2024 */       if (localTreePath != null) {
/* 2025 */         Boolean localBoolean = (Boolean)this.expandedState.get(localTreePath);
/*      */ 
/* 2027 */         return (localBoolean != null) && (localBoolean.booleanValue());
/*      */       }
/*      */     }
/* 2030 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isCollapsed(TreePath paramTreePath)
/*      */   {
/* 2043 */     return !isExpanded(paramTreePath);
/*      */   }
/*      */ 
/*      */   public boolean isCollapsed(int paramInt)
/*      */   {
/* 2054 */     return !isExpanded(paramInt);
/*      */   }
/*      */ 
/*      */   public void makeVisible(TreePath paramTreePath)
/*      */   {
/* 2063 */     if (paramTreePath != null) {
/* 2064 */       TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/* 2066 */       if (localTreePath != null)
/* 2067 */         expandPath(localTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isVisible(TreePath paramTreePath)
/*      */   {
/* 2080 */     if (paramTreePath != null) {
/* 2081 */       TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/* 2083 */       if (localTreePath != null) {
/* 2084 */         return isExpanded(localTreePath);
/*      */       }
/* 2086 */       return true;
/*      */     }
/* 2088 */     return false;
/*      */   }
/*      */ 
/*      */   public Rectangle getPathBounds(TreePath paramTreePath)
/*      */   {
/* 2105 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2107 */     if (localTreeUI != null)
/* 2108 */       return localTreeUI.getPathBounds(this, paramTreePath);
/* 2109 */     return null;
/*      */   }
/*      */ 
/*      */   public Rectangle getRowBounds(int paramInt)
/*      */   {
/* 2121 */     return getPathBounds(getPathForRow(paramInt));
/*      */   }
/*      */ 
/*      */   public void scrollPathToVisible(TreePath paramTreePath)
/*      */   {
/* 2134 */     if (paramTreePath != null) {
/* 2135 */       makeVisible(paramTreePath);
/*      */ 
/* 2137 */       Rectangle localRectangle = getPathBounds(paramTreePath);
/*      */ 
/* 2139 */       if (localRectangle != null) {
/* 2140 */         scrollRectToVisible(localRectangle);
/* 2141 */         if (this.accessibleContext != null)
/* 2142 */           ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void scrollRowToVisible(int paramInt)
/*      */   {
/* 2158 */     scrollPathToVisible(getPathForRow(paramInt));
/*      */   }
/*      */ 
/*      */   public TreePath getPathForRow(int paramInt)
/*      */   {
/* 2172 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2174 */     if (localTreeUI != null)
/* 2175 */       return localTreeUI.getPathForRow(this, paramInt);
/* 2176 */     return null;
/*      */   }
/*      */ 
/*      */   public int getRowForPath(TreePath paramTreePath)
/*      */   {
/* 2189 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2191 */     if (localTreeUI != null)
/* 2192 */       return localTreeUI.getRowForPath(this, paramTreePath);
/* 2193 */     return -1;
/*      */   }
/*      */ 
/*      */   public void expandPath(TreePath paramTreePath)
/*      */   {
/* 2205 */     TreeModel localTreeModel = getModel();
/*      */ 
/* 2207 */     if ((paramTreePath != null) && (localTreeModel != null) && (!localTreeModel.isLeaf(paramTreePath.getLastPathComponent())))
/*      */     {
/* 2209 */       setExpandedState(paramTreePath, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void expandRow(int paramInt)
/*      */   {
/* 2224 */     expandPath(getPathForRow(paramInt));
/*      */   }
/*      */ 
/*      */   public void collapsePath(TreePath paramTreePath)
/*      */   {
/* 2234 */     setExpandedState(paramTreePath, false);
/*      */   }
/*      */ 
/*      */   public void collapseRow(int paramInt)
/*      */   {
/* 2247 */     collapsePath(getPathForRow(paramInt));
/*      */   }
/*      */ 
/*      */   public TreePath getPathForLocation(int paramInt1, int paramInt2)
/*      */   {
/* 2260 */     TreePath localTreePath = getClosestPathForLocation(paramInt1, paramInt2);
/*      */ 
/* 2262 */     if (localTreePath != null) {
/* 2263 */       Rectangle localRectangle = getPathBounds(localTreePath);
/*      */ 
/* 2265 */       if ((localRectangle != null) && (paramInt1 >= localRectangle.x) && (paramInt1 < localRectangle.x + localRectangle.width) && (paramInt2 >= localRectangle.y) && (paramInt2 < localRectangle.y + localRectangle.height))
/*      */       {
/* 2268 */         return localTreePath;
/*      */       }
/*      */     }
/* 2270 */     return null;
/*      */   }
/*      */ 
/*      */   public int getRowForLocation(int paramInt1, int paramInt2)
/*      */   {
/* 2285 */     return getRowForPath(getPathForLocation(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public TreePath getClosestPathForLocation(int paramInt1, int paramInt2)
/*      */   {
/* 2306 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2308 */     if (localTreeUI != null)
/* 2309 */       return localTreeUI.getClosestPathForLocation(this, paramInt1, paramInt2);
/* 2310 */     return null;
/*      */   }
/*      */ 
/*      */   public int getClosestRowForLocation(int paramInt1, int paramInt2)
/*      */   {
/* 2331 */     return getRowForPath(getClosestPathForLocation(paramInt1, paramInt2));
/*      */   }
/*      */ 
/*      */   public boolean isEditing()
/*      */   {
/* 2342 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2344 */     if (localTreeUI != null)
/* 2345 */       return localTreeUI.isEditing(this);
/* 2346 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean stopEditing()
/*      */   {
/* 2365 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2367 */     if (localTreeUI != null)
/* 2368 */       return localTreeUI.stopEditing(this);
/* 2369 */     return false;
/*      */   }
/*      */ 
/*      */   public void cancelEditing()
/*      */   {
/* 2377 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2379 */     if (localTreeUI != null)
/* 2380 */       localTreeUI.cancelEditing(this);
/*      */   }
/*      */ 
/*      */   public void startEditingAtPath(TreePath paramTreePath)
/*      */   {
/* 2392 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2394 */     if (localTreeUI != null)
/* 2395 */       localTreeUI.startEditingAtPath(this, paramTreePath);
/*      */   }
/*      */ 
/*      */   public TreePath getEditingPath()
/*      */   {
/* 2404 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2406 */     if (localTreeUI != null)
/* 2407 */       return localTreeUI.getEditingPath(this);
/* 2408 */     return null;
/*      */   }
/*      */ 
/*      */   public void setSelectionModel(TreeSelectionModel paramTreeSelectionModel)
/*      */   {
/* 2435 */     if (paramTreeSelectionModel == null) {
/* 2436 */       paramTreeSelectionModel = EmptySelectionModel.sharedInstance();
/*      */     }
/* 2438 */     TreeSelectionModel localTreeSelectionModel = this.selectionModel;
/*      */ 
/* 2440 */     if ((this.selectionModel != null) && (this.selectionRedirector != null)) {
/* 2441 */       this.selectionModel.removeTreeSelectionListener(this.selectionRedirector);
/*      */     }
/*      */ 
/* 2444 */     if (this.accessibleContext != null) {
/* 2445 */       this.selectionModel.removeTreeSelectionListener((TreeSelectionListener)this.accessibleContext);
/* 2446 */       paramTreeSelectionModel.addTreeSelectionListener((TreeSelectionListener)this.accessibleContext);
/*      */     }
/*      */ 
/* 2449 */     this.selectionModel = paramTreeSelectionModel;
/* 2450 */     if (this.selectionRedirector != null) {
/* 2451 */       this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
/*      */     }
/* 2453 */     firePropertyChange("selectionModel", localTreeSelectionModel, this.selectionModel);
/*      */ 
/* 2456 */     if (this.accessibleContext != null)
/* 2457 */       this.accessibleContext.firePropertyChange("AccessibleSelection", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */   }
/*      */ 
/*      */   public TreeSelectionModel getSelectionModel()
/*      */   {
/* 2473 */     return this.selectionModel;
/*      */   }
/*      */ 
/*      */   protected TreePath[] getPathBetweenRows(int paramInt1, int paramInt2)
/*      */   {
/* 2503 */     TreeUI localTreeUI = getUI();
/* 2504 */     if (localTreeUI != null) {
/* 2505 */       int i = getRowCount();
/* 2506 */       if ((i > 0) && ((paramInt1 >= 0) || (paramInt2 >= 0)) && ((paramInt1 < i) || (paramInt2 < i)))
/*      */       {
/* 2508 */         paramInt1 = Math.min(i - 1, Math.max(paramInt1, 0));
/* 2509 */         paramInt2 = Math.min(i - 1, Math.max(paramInt2, 0));
/* 2510 */         int j = Math.min(paramInt1, paramInt2);
/* 2511 */         int k = Math.max(paramInt1, paramInt2);
/* 2512 */         TreePath[] arrayOfTreePath = new TreePath[k - j + 1];
/*      */ 
/* 2514 */         for (int m = j; m <= k; m++) {
/* 2515 */           arrayOfTreePath[(m - j)] = localTreeUI.getPathForRow(this, m);
/*      */         }
/*      */ 
/* 2518 */         return arrayOfTreePath;
/*      */       }
/*      */     }
/* 2521 */     return new TreePath[0];
/*      */   }
/*      */ 
/*      */   public void setSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2545 */     TreePath[] arrayOfTreePath = getPathBetweenRows(paramInt1, paramInt2);
/*      */ 
/* 2547 */     getSelectionModel().setSelectionPaths(arrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void addSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2572 */     TreePath[] arrayOfTreePath = getPathBetweenRows(paramInt1, paramInt2);
/*      */ 
/* 2574 */     if ((arrayOfTreePath != null) && (arrayOfTreePath.length > 0))
/* 2575 */       getSelectionModel().addSelectionPaths(arrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void removeSelectionInterval(int paramInt1, int paramInt2)
/*      */   {
/* 2600 */     TreePath[] arrayOfTreePath = getPathBetweenRows(paramInt1, paramInt2);
/*      */ 
/* 2602 */     if ((arrayOfTreePath != null) && (arrayOfTreePath.length > 0))
/* 2603 */       getSelectionModel().removeSelectionPaths(arrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void removeSelectionPath(TreePath paramTreePath)
/*      */   {
/* 2614 */     getSelectionModel().removeSelectionPath(paramTreePath);
/*      */   }
/*      */ 
/*      */   public void removeSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/* 2625 */     getSelectionModel().removeSelectionPaths(paramArrayOfTreePath);
/*      */   }
/*      */ 
/*      */   public void removeSelectionRow(int paramInt)
/*      */   {
/* 2635 */     int[] arrayOfInt = { paramInt };
/*      */ 
/* 2637 */     removeSelectionRows(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public void removeSelectionRows(int[] paramArrayOfInt)
/*      */   {
/* 2648 */     TreeUI localTreeUI = getUI();
/*      */ 
/* 2650 */     if ((localTreeUI != null) && (paramArrayOfInt != null)) {
/* 2651 */       int i = paramArrayOfInt.length;
/* 2652 */       TreePath[] arrayOfTreePath = new TreePath[i];
/*      */ 
/* 2654 */       for (int j = 0; j < i; j++)
/* 2655 */         arrayOfTreePath[j] = localTreeUI.getPathForRow(this, paramArrayOfInt[j]);
/* 2656 */       removeSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/* 2664 */     getSelectionModel().clearSelection();
/*      */   }
/*      */ 
/*      */   public boolean isSelectionEmpty()
/*      */   {
/* 2673 */     return getSelectionModel().isSelectionEmpty();
/*      */   }
/*      */ 
/*      */   public void addTreeExpansionListener(TreeExpansionListener paramTreeExpansionListener)
/*      */   {
/* 2684 */     if (this.settingUI) {
/* 2685 */       this.uiTreeExpansionListener = paramTreeExpansionListener;
/*      */     }
/* 2687 */     this.listenerList.add(TreeExpansionListener.class, paramTreeExpansionListener);
/*      */   }
/*      */ 
/*      */   public void removeTreeExpansionListener(TreeExpansionListener paramTreeExpansionListener)
/*      */   {
/* 2696 */     this.listenerList.remove(TreeExpansionListener.class, paramTreeExpansionListener);
/* 2697 */     if (this.uiTreeExpansionListener == paramTreeExpansionListener)
/* 2698 */       this.uiTreeExpansionListener = null;
/*      */   }
/*      */ 
/*      */   public TreeExpansionListener[] getTreeExpansionListeners()
/*      */   {
/* 2711 */     return (TreeExpansionListener[])this.listenerList.getListeners(TreeExpansionListener.class);
/*      */   }
/*      */ 
/*      */   public void addTreeWillExpandListener(TreeWillExpandListener paramTreeWillExpandListener)
/*      */   {
/* 2722 */     this.listenerList.add(TreeWillExpandListener.class, paramTreeWillExpandListener);
/*      */   }
/*      */ 
/*      */   public void removeTreeWillExpandListener(TreeWillExpandListener paramTreeWillExpandListener)
/*      */   {
/* 2731 */     this.listenerList.remove(TreeWillExpandListener.class, paramTreeWillExpandListener);
/*      */   }
/*      */ 
/*      */   public TreeWillExpandListener[] getTreeWillExpandListeners()
/*      */   {
/* 2743 */     return (TreeWillExpandListener[])this.listenerList.getListeners(TreeWillExpandListener.class);
/*      */   }
/*      */ 
/*      */   public void fireTreeExpanded(TreePath paramTreePath)
/*      */   {
/* 2757 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2758 */     TreeExpansionEvent localTreeExpansionEvent = null;
/* 2759 */     if (this.uiTreeExpansionListener != null) {
/* 2760 */       localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2761 */       this.uiTreeExpansionListener.treeExpanded(localTreeExpansionEvent);
/*      */     }
/*      */ 
/* 2765 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 2766 */       if ((arrayOfObject[i] == TreeExpansionListener.class) && (arrayOfObject[(i + 1)] != this.uiTreeExpansionListener))
/*      */       {
/* 2769 */         if (localTreeExpansionEvent == null)
/* 2770 */           localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2771 */         ((TreeExpansionListener)arrayOfObject[(i + 1)]).treeExpanded(localTreeExpansionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void fireTreeCollapsed(TreePath paramTreePath)
/*      */   {
/* 2788 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2789 */     TreeExpansionEvent localTreeExpansionEvent = null;
/* 2790 */     if (this.uiTreeExpansionListener != null) {
/* 2791 */       localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2792 */       this.uiTreeExpansionListener.treeCollapsed(localTreeExpansionEvent);
/*      */     }
/*      */ 
/* 2796 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 2797 */       if ((arrayOfObject[i] == TreeExpansionListener.class) && (arrayOfObject[(i + 1)] != this.uiTreeExpansionListener))
/*      */       {
/* 2800 */         if (localTreeExpansionEvent == null)
/* 2801 */           localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2802 */         ((TreeExpansionListener)arrayOfObject[(i + 1)]).treeCollapsed(localTreeExpansionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void fireTreeWillExpand(TreePath paramTreePath)
/*      */     throws ExpandVetoException
/*      */   {
/* 2819 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2820 */     TreeExpansionEvent localTreeExpansionEvent = null;
/*      */ 
/* 2823 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 2824 */       if (arrayOfObject[i] == TreeWillExpandListener.class)
/*      */       {
/* 2826 */         if (localTreeExpansionEvent == null)
/* 2827 */           localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2828 */         ((TreeWillExpandListener)arrayOfObject[(i + 1)]).treeWillExpand(localTreeExpansionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void fireTreeWillCollapse(TreePath paramTreePath)
/*      */     throws ExpandVetoException
/*      */   {
/* 2845 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 2846 */     TreeExpansionEvent localTreeExpansionEvent = null;
/*      */ 
/* 2849 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 2850 */       if (arrayOfObject[i] == TreeWillExpandListener.class)
/*      */       {
/* 2852 */         if (localTreeExpansionEvent == null)
/* 2853 */           localTreeExpansionEvent = new TreeExpansionEvent(this, paramTreePath);
/* 2854 */         ((TreeWillExpandListener)arrayOfObject[(i + 1)]).treeWillCollapse(localTreeExpansionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
/*      */   {
/* 2868 */     this.listenerList.add(TreeSelectionListener.class, paramTreeSelectionListener);
/* 2869 */     if ((this.listenerList.getListenerCount(TreeSelectionListener.class) != 0) && (this.selectionRedirector == null))
/*      */     {
/* 2871 */       this.selectionRedirector = new TreeSelectionRedirector();
/* 2872 */       this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
/*      */   {
/* 2882 */     this.listenerList.remove(TreeSelectionListener.class, paramTreeSelectionListener);
/* 2883 */     if ((this.listenerList.getListenerCount(TreeSelectionListener.class) == 0) && (this.selectionRedirector != null))
/*      */     {
/* 2885 */       this.selectionModel.removeTreeSelectionListener(this.selectionRedirector);
/*      */ 
/* 2887 */       this.selectionRedirector = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TreeSelectionListener[] getTreeSelectionListeners()
/*      */   {
/* 2900 */     return (TreeSelectionListener[])this.listenerList.getListeners(TreeSelectionListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireValueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */   {
/* 2915 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 2918 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*      */     {
/* 2920 */       if (arrayOfObject[i] == TreeSelectionListener.class)
/*      */       {
/* 2924 */         ((TreeSelectionListener)arrayOfObject[(i + 1)]).valueChanged(paramTreeSelectionEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeDidChange()
/*      */   {
/* 2937 */     revalidate();
/* 2938 */     repaint();
/*      */   }
/*      */ 
/*      */   public void setVisibleRowCount(int paramInt)
/*      */   {
/* 2955 */     int i = this.visibleRowCount;
/*      */ 
/* 2957 */     this.visibleRowCount = paramInt;
/* 2958 */     firePropertyChange("visibleRowCount", i, this.visibleRowCount);
/*      */ 
/* 2960 */     invalidate();
/* 2961 */     if (this.accessibleContext != null)
/* 2962 */       ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */   }
/*      */ 
/*      */   public int getVisibleRowCount()
/*      */   {
/* 2972 */     return this.visibleRowCount;
/*      */   }
/*      */ 
/*      */   private void expandRoot()
/*      */   {
/* 2979 */     TreeModel localTreeModel = getModel();
/*      */ 
/* 2981 */     if ((localTreeModel != null) && (localTreeModel.getRoot() != null))
/* 2982 */       expandPath(new TreePath(localTreeModel.getRoot()));
/*      */   }
/*      */ 
/*      */   public TreePath getNextMatch(String paramString, int paramInt, Position.Bias paramBias)
/*      */   {
/* 3005 */     int i = getRowCount();
/* 3006 */     if (paramString == null) {
/* 3007 */       throw new IllegalArgumentException();
/*      */     }
/* 3009 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 3010 */       throw new IllegalArgumentException();
/*      */     }
/* 3012 */     paramString = paramString.toUpperCase();
/*      */ 
/* 3016 */     int j = paramBias == Position.Bias.Forward ? 1 : -1;
/* 3017 */     int k = paramInt;
/*      */     do {
/* 3019 */       TreePath localTreePath = getPathForRow(k);
/* 3020 */       String str = convertValueToText(localTreePath.getLastPathComponent(), isRowSelected(k), isExpanded(k), true, k, false);
/*      */ 
/* 3024 */       if (str.toUpperCase().startsWith(paramString)) {
/* 3025 */         return localTreePath;
/*      */       }
/* 3027 */       k = (k + j + i) % i;
/* 3028 */     }while (k != paramInt);
/* 3029 */     return null;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 3034 */     Vector localVector = new Vector();
/*      */ 
/* 3036 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 3038 */     if ((this.cellRenderer != null) && ((this.cellRenderer instanceof Serializable))) {
/* 3039 */       localVector.addElement("cellRenderer");
/* 3040 */       localVector.addElement(this.cellRenderer);
/*      */     }
/*      */ 
/* 3043 */     if ((this.cellEditor != null) && ((this.cellEditor instanceof Serializable))) {
/* 3044 */       localVector.addElement("cellEditor");
/* 3045 */       localVector.addElement(this.cellEditor);
/*      */     }
/*      */ 
/* 3048 */     if ((this.treeModel != null) && ((this.treeModel instanceof Serializable))) {
/* 3049 */       localVector.addElement("treeModel");
/* 3050 */       localVector.addElement(this.treeModel);
/*      */     }
/*      */ 
/* 3053 */     if ((this.selectionModel != null) && ((this.selectionModel instanceof Serializable))) {
/* 3054 */       localVector.addElement("selectionModel");
/* 3055 */       localVector.addElement(this.selectionModel);
/*      */     }
/*      */ 
/* 3058 */     Object localObject = getArchivableExpandedState();
/*      */ 
/* 3060 */     if (localObject != null) {
/* 3061 */       localVector.addElement("expandedState");
/* 3062 */       localVector.addElement(localObject);
/*      */     }
/*      */ 
/* 3065 */     paramObjectOutputStream.writeObject(localVector);
/* 3066 */     if (getUIClassID().equals("TreeUI")) {
/* 3067 */       byte b = JComponent.getWriteObjCounter(this);
/* 3068 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 3069 */       if ((b == 0) && (this.ui != null))
/* 3070 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 3077 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 3081 */     this.expandedState = new Hashtable();
/*      */ 
/* 3083 */     this.expandedStack = new Stack();
/*      */ 
/* 3085 */     Vector localVector = (Vector)paramObjectInputStream.readObject();
/* 3086 */     int i = 0;
/* 3087 */     int j = localVector.size();
/*      */ 
/* 3089 */     if ((i < j) && (localVector.elementAt(i).equals("cellRenderer")))
/*      */     {
/* 3091 */       this.cellRenderer = ((TreeCellRenderer)localVector.elementAt(++i));
/* 3092 */       i++;
/*      */     }
/* 3094 */     if ((i < j) && (localVector.elementAt(i).equals("cellEditor")))
/*      */     {
/* 3096 */       this.cellEditor = ((TreeCellEditor)localVector.elementAt(++i));
/* 3097 */       i++;
/*      */     }
/* 3099 */     if ((i < j) && (localVector.elementAt(i).equals("treeModel")))
/*      */     {
/* 3101 */       this.treeModel = ((TreeModel)localVector.elementAt(++i));
/* 3102 */       i++;
/*      */     }
/* 3104 */     if ((i < j) && (localVector.elementAt(i).equals("selectionModel")))
/*      */     {
/* 3106 */       this.selectionModel = ((TreeSelectionModel)localVector.elementAt(++i));
/* 3107 */       i++;
/*      */     }
/* 3109 */     if ((i < j) && (localVector.elementAt(i).equals("expandedState")))
/*      */     {
/* 3111 */       unarchiveExpandedState(localVector.elementAt(++i));
/* 3112 */       i++;
/*      */     }
/*      */ 
/* 3115 */     if (this.listenerList.getListenerCount(TreeSelectionListener.class) != 0) {
/* 3116 */       this.selectionRedirector = new TreeSelectionRedirector();
/* 3117 */       this.selectionModel.addTreeSelectionListener(this.selectionRedirector);
/*      */     }
/*      */ 
/* 3120 */     if (this.treeModel != null) {
/* 3121 */       this.treeModelListener = createTreeModelListener();
/* 3122 */       if (this.treeModelListener != null)
/* 3123 */         this.treeModel.addTreeModelListener(this.treeModelListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object getArchivableExpandedState()
/*      */   {
/* 3133 */     TreeModel localTreeModel = getModel();
/*      */ 
/* 3135 */     if (localTreeModel != null) {
/* 3136 */       Enumeration localEnumeration = this.expandedState.keys();
/*      */ 
/* 3138 */       if (localEnumeration != null) {
/* 3139 */         Vector localVector = new Vector();
/*      */ 
/* 3141 */         while (localEnumeration.hasMoreElements()) {
/* 3142 */           TreePath localTreePath = (TreePath)localEnumeration.nextElement();
/*      */           int[] arrayOfInt;
/*      */           try {
/* 3146 */             arrayOfInt = getModelIndexsForPath(localTreePath);
/*      */           } catch (Error localError) {
/* 3148 */             arrayOfInt = null;
/*      */           }
/* 3150 */           if (arrayOfInt != null) {
/* 3151 */             localVector.addElement(arrayOfInt);
/* 3152 */             localVector.addElement(this.expandedState.get(localTreePath));
/*      */           }
/*      */         }
/* 3155 */         return localVector;
/*      */       }
/*      */     }
/* 3158 */     return null;
/*      */   }
/*      */ 
/*      */   private void unarchiveExpandedState(Object paramObject)
/*      */   {
/* 3166 */     if ((paramObject instanceof Vector)) {
/* 3167 */       Vector localVector = (Vector)paramObject;
/*      */ 
/* 3169 */       for (int i = localVector.size() - 1; i >= 0; i--) {
/* 3170 */         Boolean localBoolean = (Boolean)localVector.elementAt(i--);
/*      */         try
/*      */         {
/* 3174 */           TreePath localTreePath = getPathForIndexs((int[])localVector.elementAt(i));
/* 3175 */           if (localTreePath != null)
/* 3176 */             this.expandedState.put(localTreePath, localBoolean);
/*      */         }
/*      */         catch (Error localError)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] getModelIndexsForPath(TreePath paramTreePath)
/*      */   {
/* 3189 */     if (paramTreePath != null) {
/* 3190 */       TreeModel localTreeModel = getModel();
/* 3191 */       int i = paramTreePath.getPathCount();
/* 3192 */       int[] arrayOfInt = new int[i - 1];
/* 3193 */       Object localObject = localTreeModel.getRoot();
/*      */ 
/* 3195 */       for (int j = 1; j < i; j++) {
/* 3196 */         arrayOfInt[(j - 1)] = localTreeModel.getIndexOfChild(localObject, paramTreePath.getPathComponent(j));
/*      */ 
/* 3198 */         localObject = paramTreePath.getPathComponent(j);
/* 3199 */         if (arrayOfInt[(j - 1)] < 0)
/* 3200 */           return null;
/*      */       }
/* 3202 */       return arrayOfInt;
/*      */     }
/* 3204 */     return null;
/*      */   }
/*      */ 
/*      */   private TreePath getPathForIndexs(int[] paramArrayOfInt)
/*      */   {
/* 3214 */     if (paramArrayOfInt == null) {
/* 3215 */       return null;
/*      */     }
/* 3217 */     TreeModel localTreeModel = getModel();
/*      */ 
/* 3219 */     if (localTreeModel == null) {
/* 3220 */       return null;
/*      */     }
/* 3222 */     int i = paramArrayOfInt.length;
/* 3223 */     Object localObject = localTreeModel.getRoot();
/* 3224 */     TreePath localTreePath = new TreePath(localObject);
/*      */ 
/* 3226 */     for (int j = 0; j < i; j++) {
/* 3227 */       localObject = localTreeModel.getChild(localObject, paramArrayOfInt[j]);
/* 3228 */       if (localObject == null)
/* 3229 */         return null;
/* 3230 */       localTreePath = localTreePath.pathByAddingChild(localObject);
/*      */     }
/* 3232 */     return localTreePath;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredScrollableViewportSize()
/*      */   {
/* 3399 */     int i = getPreferredSize().width;
/* 3400 */     int j = getVisibleRowCount();
/* 3401 */     int k = -1;
/*      */ 
/* 3403 */     if (isFixedRowHeight()) {
/* 3404 */       k = j * getRowHeight();
/*      */     } else {
/* 3406 */       TreeUI localTreeUI = getUI();
/*      */ 
/* 3408 */       if ((localTreeUI != null) && (j > 0)) {
/* 3409 */         int m = localTreeUI.getRowCount(this);
/*      */         Rectangle localRectangle;
/* 3411 */         if (m >= j) {
/* 3412 */           localRectangle = getRowBounds(j - 1);
/* 3413 */           if (localRectangle != null) {
/* 3414 */             k = localRectangle.y + localRectangle.height;
/*      */           }
/*      */         }
/* 3417 */         else if (m > 0) {
/* 3418 */           localRectangle = getRowBounds(0);
/* 3419 */           if (localRectangle != null) {
/* 3420 */             k = localRectangle.height * j;
/*      */           }
/*      */         }
/*      */       }
/* 3424 */       if (k == -1) {
/* 3425 */         k = 16 * j;
/*      */       }
/*      */     }
/* 3428 */     return new Dimension(i, k);
/*      */   }
/*      */ 
/*      */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 3447 */     if (paramInt1 == 1)
/*      */     {
/* 3449 */       int i = getClosestRowForLocation(0, paramRectangle.y);
/*      */ 
/* 3452 */       if (i != -1) {
/* 3453 */         Rectangle localRectangle = getRowBounds(i);
/* 3454 */         if (localRectangle.y != paramRectangle.y) {
/* 3455 */           if (paramInt2 < 0)
/*      */           {
/* 3457 */             return Math.max(0, paramRectangle.y - localRectangle.y);
/*      */           }
/* 3459 */           return localRectangle.y + localRectangle.height - paramRectangle.y;
/*      */         }
/* 3461 */         if (paramInt2 < 0) {
/* 3462 */           if (i != 0) {
/* 3463 */             localRectangle = getRowBounds(i - 1);
/* 3464 */             return localRectangle.height;
/*      */           }
/*      */         }
/*      */         else {
/* 3468 */           return localRectangle.height;
/*      */         }
/*      */       }
/* 3471 */       return 0;
/*      */     }
/* 3473 */     return 4;
/*      */   }
/*      */ 
/*      */   public int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 3491 */     return paramInt1 == 1 ? paramRectangle.height : paramRectangle.width;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 3505 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 3506 */     if ((localContainer instanceof JViewport)) {
/* 3507 */       return localContainer.getWidth() > getPreferredSize().width;
/*      */     }
/* 3509 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportHeight()
/*      */   {
/* 3522 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 3523 */     if ((localContainer instanceof JViewport)) {
/* 3524 */       return localContainer.getHeight() > getPreferredSize().height;
/*      */     }
/* 3526 */     return false;
/*      */   }
/*      */ 
/*      */   protected void setExpandedState(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/* 3539 */     if (paramTreePath != null)
/*      */     {
/* 3542 */       TreePath localTreePath = paramTreePath.getParentPath();
/*      */       Stack localStack;
/* 3544 */       if (this.expandedStack.size() == 0) {
/* 3545 */         localStack = new Stack();
/*      */       }
/*      */       else {
/* 3548 */         localStack = (Stack)this.expandedStack.pop();
/*      */       }
/*      */       try
/*      */       {
/* 3552 */         while (localTreePath != null) {
/* 3553 */           if (isExpanded(localTreePath)) {
/* 3554 */             localTreePath = null;
/*      */           }
/*      */           else {
/* 3557 */             localStack.push(localTreePath);
/* 3558 */             localTreePath = localTreePath.getParentPath();
/*      */           }
/*      */         }
/* 3561 */         for (int i = localStack.size() - 1; i >= 0; i--) {
/* 3562 */           localTreePath = (TreePath)localStack.pop();
/* 3563 */           if (!isExpanded(localTreePath)) {
/*      */             try {
/* 3565 */               fireTreeWillExpand(localTreePath);
/*      */             }
/*      */             catch (ExpandVetoException localExpandVetoException1) {
/*      */               return;
/*      */             }
/* 3570 */             this.expandedState.put(localTreePath, Boolean.TRUE);
/* 3571 */             fireTreeExpanded(localTreePath);
/* 3572 */             if (this.accessibleContext != null) {
/* 3573 */               ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3580 */         if (this.expandedStack.size() < TEMP_STACK_SIZE) {
/* 3581 */           localStack.removeAllElements();
/* 3582 */           this.expandedStack.push(localStack);
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 3580 */         if (this.expandedStack.size() < TEMP_STACK_SIZE) {
/* 3581 */           localStack.removeAllElements();
/* 3582 */           this.expandedStack.push(localStack);
/*      */         }
/*      */       }
/*      */       Object localObject1;
/* 3585 */       if (!paramBoolean)
/*      */       {
/* 3587 */         localObject1 = this.expandedState.get(paramTreePath);
/*      */ 
/* 3589 */         if ((localObject1 != null) && (((Boolean)localObject1).booleanValue())) {
/*      */           try {
/* 3591 */             fireTreeWillCollapse(paramTreePath);
/*      */           }
/*      */           catch (ExpandVetoException localExpandVetoException2) {
/* 3594 */             return;
/*      */           }
/* 3596 */           this.expandedState.put(paramTreePath, Boolean.FALSE);
/* 3597 */           fireTreeCollapsed(paramTreePath);
/* 3598 */           if ((removeDescendantSelectedPaths(paramTreePath, false)) && (!isPathSelected(paramTreePath)))
/*      */           {
/* 3601 */             addSelectionPath(paramTreePath);
/*      */           }
/* 3603 */           if (this.accessibleContext != null) {
/* 3604 */             ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3611 */         localObject1 = this.expandedState.get(paramTreePath);
/*      */ 
/* 3613 */         if ((localObject1 == null) || (!((Boolean)localObject1).booleanValue())) {
/*      */           try {
/* 3615 */             fireTreeWillExpand(paramTreePath);
/*      */           }
/*      */           catch (ExpandVetoException localExpandVetoException3) {
/* 3618 */             return;
/*      */           }
/* 3620 */           this.expandedState.put(paramTreePath, Boolean.TRUE);
/* 3621 */           fireTreeExpanded(paramTreePath);
/* 3622 */           if (this.accessibleContext != null)
/* 3623 */             ((AccessibleJTree)this.accessibleContext).fireVisibleDataPropertyChange();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Enumeration<TreePath> getDescendantToggledPaths(TreePath paramTreePath)
/*      */   {
/* 3639 */     if (paramTreePath == null) {
/* 3640 */       return null;
/*      */     }
/* 3642 */     Vector localVector = new Vector();
/* 3643 */     Enumeration localEnumeration = this.expandedState.keys();
/*      */ 
/* 3645 */     while (localEnumeration.hasMoreElements()) {
/* 3646 */       TreePath localTreePath = (TreePath)localEnumeration.nextElement();
/* 3647 */       if (paramTreePath.isDescendant(localTreePath))
/* 3648 */         localVector.addElement(localTreePath);
/*      */     }
/* 3650 */     return localVector.elements();
/*      */   }
/*      */ 
/*      */   protected void removeDescendantToggledPaths(Enumeration<TreePath> paramEnumeration)
/*      */   {
/* 3667 */     if (paramEnumeration != null)
/* 3668 */       while (paramEnumeration.hasMoreElements()) {
/* 3669 */         Enumeration localEnumeration = getDescendantToggledPaths((TreePath)paramEnumeration.nextElement());
/*      */ 
/* 3672 */         if (localEnumeration != null)
/* 3673 */           while (localEnumeration.hasMoreElements())
/* 3674 */             this.expandedState.remove(localEnumeration.nextElement());
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void clearToggledPaths()
/*      */   {
/* 3686 */     this.expandedState.clear();
/*      */   }
/*      */ 
/*      */   protected TreeModelListener createTreeModelListener()
/*      */   {
/* 3699 */     return new TreeModelHandler();
/*      */   }
/*      */ 
/*      */   protected boolean removeDescendantSelectedPaths(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/* 3712 */     TreePath[] arrayOfTreePath = getDescendantSelectedPaths(paramTreePath, paramBoolean);
/*      */ 
/* 3714 */     if (arrayOfTreePath != null) {
/* 3715 */       getSelectionModel().removeSelectionPaths(arrayOfTreePath);
/* 3716 */       return true;
/*      */     }
/* 3718 */     return false;
/*      */   }
/*      */ 
/*      */   private TreePath[] getDescendantSelectedPaths(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/* 3727 */     TreeSelectionModel localTreeSelectionModel = getSelectionModel();
/* 3728 */     TreePath[] arrayOfTreePath = localTreeSelectionModel != null ? localTreeSelectionModel.getSelectionPaths() : null;
/*      */ 
/* 3731 */     if (arrayOfTreePath != null) {
/* 3732 */       int i = 0;
/*      */ 
/* 3734 */       for (int j = arrayOfTreePath.length - 1; j >= 0; j--) {
/* 3735 */         if ((arrayOfTreePath[j] != null) && (paramTreePath.isDescendant(arrayOfTreePath[j])) && ((!paramTreePath.equals(arrayOfTreePath[j])) || (paramBoolean)))
/*      */         {
/* 3738 */           i = 1;
/*      */         }
/* 3740 */         else arrayOfTreePath[j] = null;
/*      */       }
/* 3742 */       if (i == 0) {
/* 3743 */         arrayOfTreePath = null;
/*      */       }
/* 3745 */       return arrayOfTreePath;
/*      */     }
/* 3747 */     return null;
/*      */   }
/*      */ 
/*      */   void removeDescendantSelectedPaths(TreeModelEvent paramTreeModelEvent)
/*      */   {
/* 3755 */     TreePath localTreePath = paramTreeModelEvent.getTreePath();
/* 3756 */     Object[] arrayOfObject = paramTreeModelEvent.getChildren();
/* 3757 */     TreeSelectionModel localTreeSelectionModel = getSelectionModel();
/*      */ 
/* 3759 */     if ((localTreeSelectionModel != null) && (localTreePath != null) && (arrayOfObject != null) && (arrayOfObject.length > 0))
/*      */     {
/* 3761 */       for (int i = arrayOfObject.length - 1; i >= 0; 
/* 3762 */         i--)
/*      */       {
/* 3765 */         removeDescendantSelectedPaths(localTreePath.pathByAddingChild(arrayOfObject[i]), true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void setUIProperty(String paramString, Object paramObject)
/*      */   {
/* 4009 */     if (paramString == "rowHeight") {
/* 4010 */       if (!this.rowHeightSet) {
/* 4011 */         setRowHeight(((Number)paramObject).intValue());
/* 4012 */         this.rowHeightSet = false;
/*      */       }
/* 4014 */     } else if (paramString == "scrollsOnExpand") {
/* 4015 */       if (!this.scrollsOnExpandSet) {
/* 4016 */         setScrollsOnExpand(((Boolean)paramObject).booleanValue());
/* 4017 */         this.scrollsOnExpandSet = false;
/*      */       }
/* 4019 */     } else if (paramString == "showsRootHandles") {
/* 4020 */       if (!this.showsRootHandlesSet) {
/* 4021 */         setShowsRootHandles(((Boolean)paramObject).booleanValue());
/* 4022 */         this.showsRootHandlesSet = false;
/*      */       }
/*      */     }
/* 4025 */     else super.setUIProperty(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 4041 */     String str1 = this.rootVisible ? "true" : "false";
/*      */ 
/* 4043 */     String str2 = this.showsRootHandles ? "true" : "false";
/*      */ 
/* 4045 */     String str3 = this.editable ? "true" : "false";
/*      */ 
/* 4047 */     String str4 = this.largeModel ? "true" : "false";
/*      */ 
/* 4049 */     String str5 = this.invokesStopCellEditing ? "true" : "false";
/*      */ 
/* 4051 */     String str6 = this.scrollsOnExpand ? "true" : "false";
/*      */ 
/* 4054 */     return super.paramString() + ",editable=" + str3 + ",invokesStopCellEditing=" + str5 + ",largeModel=" + str4 + ",rootVisible=" + str1 + ",rowHeight=" + this.rowHeight + ",scrollsOnExpand=" + str6 + ",showsRootHandles=" + str2 + ",toggleClickCount=" + this.toggleClickCount + ",visibleRowCount=" + this.visibleRowCount;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 4080 */     if (this.accessibleContext == null) {
/* 4081 */       this.accessibleContext = new AccessibleJTree();
/*      */     }
/* 4083 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJTree extends JComponent.AccessibleJComponent
/*      */     implements AccessibleSelection, TreeSelectionListener, TreeModelListener, TreeExpansionListener
/*      */   {
/*      */     TreePath leadSelectionPath;
/*      */     Accessible leadSelectionAccessible;
/*      */ 
/*      */     public AccessibleJTree()
/*      */     {
/* 4107 */       super();
/*      */ 
/* 4109 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4110 */       if (localTreeModel != null) {
/* 4111 */         localTreeModel.addTreeModelListener(this);
/*      */       }
/* 4113 */       JTree.this.addTreeExpansionListener(this);
/* 4114 */       JTree.this.addTreeSelectionListener(this);
/* 4115 */       this.leadSelectionPath = JTree.this.getLeadSelectionPath();
/* 4116 */       this.leadSelectionAccessible = (this.leadSelectionPath != null ? new AccessibleJTreeNode(JTree.this, this.leadSelectionPath, JTree.this) : null);
/*      */     }
/*      */ 
/*      */     public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */     {
/* 4131 */       firePropertyChange("AccessibleSelection", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public void fireVisibleDataPropertyChange()
/*      */     {
/* 4145 */       firePropertyChange("AccessibleVisibleData", Boolean.valueOf(false), Boolean.valueOf(true));
/*      */     }
/*      */ 
/*      */     public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 4157 */       fireVisibleDataPropertyChange();
/*      */     }
/*      */ 
/*      */     public void treeNodesInserted(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 4166 */       fireVisibleDataPropertyChange();
/*      */     }
/*      */ 
/*      */     public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 4175 */       fireVisibleDataPropertyChange();
/*      */     }
/*      */ 
/*      */     public void treeStructureChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 4184 */       fireVisibleDataPropertyChange();
/*      */     }
/*      */ 
/*      */     public void treeCollapsed(TreeExpansionEvent paramTreeExpansionEvent)
/*      */     {
/* 4193 */       fireVisibleDataPropertyChange();
/* 4194 */       TreePath localTreePath = paramTreeExpansionEvent.getPath();
/* 4195 */       if (localTreePath != null)
/*      */       {
/* 4198 */         AccessibleJTreeNode localAccessibleJTreeNode = new AccessibleJTreeNode(JTree.this, localTreePath, null);
/*      */ 
/* 4201 */         PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(localAccessibleJTreeNode, "AccessibleState", AccessibleState.EXPANDED, AccessibleState.COLLAPSED);
/*      */ 
/* 4205 */         firePropertyChange("AccessibleState", null, localPropertyChangeEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void treeExpanded(TreeExpansionEvent paramTreeExpansionEvent)
/*      */     {
/* 4216 */       fireVisibleDataPropertyChange();
/* 4217 */       TreePath localTreePath = paramTreeExpansionEvent.getPath();
/* 4218 */       if (localTreePath != null)
/*      */       {
/* 4222 */         AccessibleJTreeNode localAccessibleJTreeNode = new AccessibleJTreeNode(JTree.this, localTreePath, null);
/*      */ 
/* 4225 */         PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(localAccessibleJTreeNode, "AccessibleState", AccessibleState.COLLAPSED, AccessibleState.EXPANDED);
/*      */ 
/* 4229 */         firePropertyChange("AccessibleState", null, localPropertyChangeEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     void fireActiveDescendantPropertyChange(TreePath paramTreePath1, TreePath paramTreePath2)
/*      */     {
/* 4246 */       if (paramTreePath1 != paramTreePath2) {
/* 4247 */         Object localObject1 = paramTreePath1 != null ? new AccessibleJTreeNode(JTree.this, paramTreePath1, null) : null;
/*      */ 
/* 4253 */         Object localObject2 = paramTreePath2 != null ? new AccessibleJTreeNode(JTree.this, paramTreePath2, null) : null;
/*      */ 
/* 4258 */         firePropertyChange("AccessibleActiveDescendant", localObject1, localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private AccessibleContext getCurrentAccessibleContext()
/*      */     {
/* 4265 */       Component localComponent = getCurrentComponent();
/* 4266 */       if ((localComponent instanceof Accessible)) {
/* 4267 */         return localComponent.getAccessibleContext();
/*      */       }
/* 4269 */       return null;
/*      */     }
/*      */ 
/*      */     private Component getCurrentComponent()
/*      */     {
/* 4277 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4278 */       if (localTreeModel == null) {
/* 4279 */         return null;
/*      */       }
/* 4281 */       TreePath localTreePath = new TreePath(localTreeModel.getRoot());
/* 4282 */       if (JTree.this.isVisible(localTreePath)) {
/* 4283 */         TreeCellRenderer localTreeCellRenderer = JTree.this.getCellRenderer();
/* 4284 */         TreeUI localTreeUI = JTree.this.getUI();
/* 4285 */         if (localTreeUI != null) {
/* 4286 */           int i = localTreeUI.getRowForPath(JTree.this, localTreePath);
/* 4287 */           int j = JTree.this.getLeadSelectionRow();
/* 4288 */           boolean bool1 = (JTree.this.isFocusOwner()) && (j == i);
/*      */ 
/* 4290 */           boolean bool2 = JTree.this.isPathSelected(localTreePath);
/* 4291 */           boolean bool3 = JTree.this.isExpanded(localTreePath);
/*      */ 
/* 4293 */           return localTreeCellRenderer.getTreeCellRendererComponent(JTree.this, localTreeModel.getRoot(), bool2, bool3, localTreeModel.isLeaf(localTreeModel.getRoot()), i, bool1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4298 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 4311 */       return AccessibleRole.TREE;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 4324 */       TreePath localTreePath = JTree.this.getClosestPathForLocation(paramPoint.x, paramPoint.y);
/* 4325 */       if (localTreePath != null)
/*      */       {
/* 4327 */         return new AccessibleJTreeNode(JTree.this, localTreePath, null);
/*      */       }
/* 4329 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 4340 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4341 */       if (localTreeModel == null) {
/* 4342 */         return 0;
/*      */       }
/* 4344 */       if (JTree.this.isRootVisible()) {
/* 4345 */         return 1;
/*      */       }
/*      */ 
/* 4349 */       return localTreeModel.getChildCount(localTreeModel.getRoot());
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 4359 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4360 */       if (localTreeModel == null) {
/* 4361 */         return null;
/*      */       }
/* 4363 */       if (JTree.this.isRootVisible()) {
/* 4364 */         if (paramInt == 0) {
/* 4365 */           Object[] arrayOfObject1 = { localTreeModel.getRoot() };
/* 4366 */           localObject = new TreePath(arrayOfObject1);
/* 4367 */           return new AccessibleJTreeNode(JTree.this, (TreePath)localObject, JTree.this);
/*      */         }
/* 4369 */         return null;
/*      */       }
/*      */ 
/* 4374 */       int i = localTreeModel.getChildCount(localTreeModel.getRoot());
/* 4375 */       if ((paramInt < 0) || (paramInt >= i)) {
/* 4376 */         return null;
/*      */       }
/* 4378 */       Object localObject = localTreeModel.getChild(localTreeModel.getRoot(), paramInt);
/* 4379 */       Object[] arrayOfObject2 = { localTreeModel.getRoot(), localObject };
/* 4380 */       TreePath localTreePath = new TreePath(arrayOfObject2);
/* 4381 */       return new AccessibleJTreeNode(JTree.this, localTreePath, JTree.this);
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexInParent()
/*      */     {
/* 4393 */       return super.getAccessibleIndexInParent();
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 4406 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/* 4416 */       Object[] arrayOfObject = new Object[1];
/* 4417 */       arrayOfObject[0] = JTree.this.treeModel.getRoot();
/* 4418 */       TreePath localTreePath = new TreePath(arrayOfObject);
/* 4419 */       if (JTree.this.isPathSelected(localTreePath)) {
/* 4420 */         return 1;
/*      */       }
/* 4422 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/* 4437 */       if (paramInt == 0) {
/* 4438 */         Object[] arrayOfObject = new Object[1];
/* 4439 */         arrayOfObject[0] = JTree.this.treeModel.getRoot();
/* 4440 */         TreePath localTreePath = new TreePath(arrayOfObject);
/* 4441 */         if (JTree.this.isPathSelected(localTreePath)) {
/* 4442 */           return new AccessibleJTreeNode(JTree.this, localTreePath, JTree.this);
/*      */         }
/*      */       }
/* 4445 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/* 4456 */       if (paramInt == 0) {
/* 4457 */         Object[] arrayOfObject = new Object[1];
/* 4458 */         arrayOfObject[0] = JTree.this.treeModel.getRoot();
/* 4459 */         TreePath localTreePath = new TreePath(arrayOfObject);
/* 4460 */         return JTree.this.isPathSelected(localTreePath);
/*      */       }
/* 4462 */       return false;
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/* 4476 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4477 */       if ((localTreeModel != null) && 
/* 4478 */         (paramInt == 0)) {
/* 4479 */         Object[] arrayOfObject = { localTreeModel.getRoot() };
/* 4480 */         TreePath localTreePath = new TreePath(arrayOfObject);
/* 4481 */         JTree.this.addSelectionPath(localTreePath);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/* 4494 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4495 */       if ((localTreeModel != null) && 
/* 4496 */         (paramInt == 0)) {
/* 4497 */         Object[] arrayOfObject = { localTreeModel.getRoot() };
/* 4498 */         TreePath localTreePath = new TreePath(arrayOfObject);
/* 4499 */         JTree.this.removeSelectionPath(localTreePath);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/* 4509 */       int i = getAccessibleChildrenCount();
/* 4510 */       for (int j = 0; j < i; j++)
/* 4511 */         removeAccessibleSelection(j);
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/* 4520 */       TreeModel localTreeModel = JTree.this.getModel();
/* 4521 */       if (localTreeModel != null) {
/* 4522 */         Object[] arrayOfObject = { localTreeModel.getRoot() };
/* 4523 */         TreePath localTreePath = new TreePath(arrayOfObject);
/* 4524 */         JTree.this.addSelectionPath(localTreePath);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected class AccessibleJTreeNode extends AccessibleContext
/*      */       implements Accessible, AccessibleComponent, AccessibleSelection, AccessibleAction
/*      */     {
/* 4537 */       private JTree tree = null;
/* 4538 */       private TreeModel treeModel = null;
/* 4539 */       private Object obj = null;
/* 4540 */       private TreePath path = null;
/* 4541 */       private Accessible accessibleParent = null;
/* 4542 */       private int index = 0;
/* 4543 */       private boolean isLeaf = false;
/*      */ 
/*      */       public AccessibleJTreeNode(JTree paramTreePath, TreePath paramAccessible, Accessible arg4)
/*      */       {
/* 4550 */         this.tree = paramTreePath;
/* 4551 */         this.path = paramAccessible;
/*      */         Object localObject;
/* 4552 */         this.accessibleParent = localObject;
/* 4553 */         this.treeModel = paramTreePath.getModel();
/* 4554 */         this.obj = paramAccessible.getLastPathComponent();
/* 4555 */         if (this.treeModel != null)
/* 4556 */           this.isLeaf = this.treeModel.isLeaf(this.obj);
/*      */       }
/*      */ 
/*      */       private TreePath getChildTreePath(int paramInt)
/*      */       {
/* 4563 */         if ((paramInt < 0) || (paramInt >= getAccessibleChildrenCount())) {
/* 4564 */           return null;
/*      */         }
/* 4566 */         Object localObject = this.treeModel.getChild(this.obj, paramInt);
/* 4567 */         Object[] arrayOfObject1 = this.path.getPath();
/* 4568 */         Object[] arrayOfObject2 = new Object[arrayOfObject1.length + 1];
/* 4569 */         System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length);
/* 4570 */         arrayOfObject2[(arrayOfObject2.length - 1)] = localObject;
/* 4571 */         return new TreePath(arrayOfObject2);
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/* 4584 */         return this;
/*      */       }
/*      */ 
/*      */       private AccessibleContext getCurrentAccessibleContext() {
/* 4588 */         Component localComponent = getCurrentComponent();
/* 4589 */         if ((localComponent instanceof AccessibleAction)) {
/* 4590 */           return localComponent.getAccessibleContext();
/*      */         }
/* 4592 */         return null;
/*      */       }
/*      */ 
/*      */       private Component getCurrentComponent()
/*      */       {
/* 4600 */         if (this.tree.isVisible(this.path)) {
/* 4601 */           TreeCellRenderer localTreeCellRenderer = this.tree.getCellRenderer();
/* 4602 */           if (localTreeCellRenderer == null) {
/* 4603 */             return null;
/*      */           }
/* 4605 */           TreeUI localTreeUI = this.tree.getUI();
/* 4606 */           if (localTreeUI != null) {
/* 4607 */             int i = localTreeUI.getRowForPath(JTree.this, this.path);
/* 4608 */             boolean bool1 = this.tree.isPathSelected(this.path);
/* 4609 */             boolean bool2 = this.tree.isExpanded(this.path);
/* 4610 */             boolean bool3 = false;
/* 4611 */             return localTreeCellRenderer.getTreeCellRendererComponent(this.tree, this.obj, bool1, bool2, this.isLeaf, i, bool3);
/*      */           }
/*      */         }
/*      */ 
/* 4615 */         return null;
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 4627 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4628 */         if (localAccessibleContext != null) {
/* 4629 */           String str = localAccessibleContext.getAccessibleName();
/* 4630 */           if ((str != null) && (str != "")) {
/* 4631 */             return localAccessibleContext.getAccessibleName();
/*      */           }
/* 4633 */           return null;
/*      */         }
/*      */ 
/* 4636 */         if ((this.accessibleName != null) && (this.accessibleName != "")) {
/* 4637 */           return this.accessibleName;
/*      */         }
/*      */ 
/* 4640 */         return (String)JTree.this.getClientProperty("AccessibleName");
/*      */       }
/*      */ 
/*      */       public void setAccessibleName(String paramString)
/*      */       {
/* 4650 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4651 */         if (localAccessibleContext != null)
/* 4652 */           localAccessibleContext.setAccessibleName(paramString);
/*      */         else
/* 4654 */           super.setAccessibleName(paramString);
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 4668 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4669 */         if (localAccessibleContext != null) {
/* 4670 */           return localAccessibleContext.getAccessibleDescription();
/*      */         }
/* 4672 */         return super.getAccessibleDescription();
/*      */       }
/*      */ 
/*      */       public void setAccessibleDescription(String paramString)
/*      */       {
/* 4682 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4683 */         if (localAccessibleContext != null)
/* 4684 */           localAccessibleContext.setAccessibleDescription(paramString);
/*      */         else
/* 4686 */           super.setAccessibleDescription(paramString);
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 4697 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4698 */         if (localAccessibleContext != null) {
/* 4699 */           return localAccessibleContext.getAccessibleRole();
/*      */         }
/* 4701 */         return AccessibleRole.UNKNOWN;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 4713 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/*      */         AccessibleStateSet localAccessibleStateSet;
/* 4715 */         if (localAccessibleContext != null)
/* 4716 */           localAccessibleStateSet = localAccessibleContext.getAccessibleStateSet();
/*      */         else {
/* 4718 */           localAccessibleStateSet = new AccessibleStateSet();
/*      */         }
/*      */ 
/* 4722 */         if (isShowing())
/* 4723 */           localAccessibleStateSet.add(AccessibleState.SHOWING);
/* 4724 */         else if (localAccessibleStateSet.contains(AccessibleState.SHOWING)) {
/* 4725 */           localAccessibleStateSet.remove(AccessibleState.SHOWING);
/*      */         }
/* 4727 */         if (isVisible())
/* 4728 */           localAccessibleStateSet.add(AccessibleState.VISIBLE);
/* 4729 */         else if (localAccessibleStateSet.contains(AccessibleState.VISIBLE)) {
/* 4730 */           localAccessibleStateSet.remove(AccessibleState.VISIBLE);
/*      */         }
/* 4732 */         if (this.tree.isPathSelected(this.path)) {
/* 4733 */           localAccessibleStateSet.add(AccessibleState.SELECTED);
/*      */         }
/* 4735 */         if (this.path == JTree.this.getLeadSelectionPath()) {
/* 4736 */           localAccessibleStateSet.add(AccessibleState.ACTIVE);
/*      */         }
/* 4738 */         if (!this.isLeaf) {
/* 4739 */           localAccessibleStateSet.add(AccessibleState.EXPANDABLE);
/*      */         }
/* 4741 */         if (this.tree.isExpanded(this.path))
/* 4742 */           localAccessibleStateSet.add(AccessibleState.EXPANDED);
/*      */         else {
/* 4744 */           localAccessibleStateSet.add(AccessibleState.COLLAPSED);
/*      */         }
/* 4746 */         if (this.tree.isEditable()) {
/* 4747 */           localAccessibleStateSet.add(AccessibleState.EDITABLE);
/*      */         }
/* 4749 */         return localAccessibleStateSet;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleParent()
/*      */       {
/* 4761 */         if (this.accessibleParent == null) {
/* 4762 */           Object[] arrayOfObject1 = this.path.getPath();
/* 4763 */           if (arrayOfObject1.length > 1) {
/* 4764 */             Object localObject = arrayOfObject1[(arrayOfObject1.length - 2)];
/* 4765 */             if (this.treeModel != null) {
/* 4766 */               this.index = this.treeModel.getIndexOfChild(localObject, this.obj);
/*      */             }
/* 4768 */             Object[] arrayOfObject2 = new Object[arrayOfObject1.length - 1];
/* 4769 */             System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length - 1);
/*      */ 
/* 4771 */             TreePath localTreePath = new TreePath(arrayOfObject2);
/* 4772 */             this.accessibleParent = new AccessibleJTreeNode(JTree.AccessibleJTree.this, this.tree, localTreePath, null);
/*      */ 
/* 4775 */             setAccessibleParent(this.accessibleParent);
/* 4776 */           } else if (this.treeModel != null) {
/* 4777 */             this.accessibleParent = this.tree;
/* 4778 */             this.index = 0;
/* 4779 */             setAccessibleParent(this.accessibleParent);
/*      */           }
/*      */         }
/* 4782 */         return this.accessibleParent;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 4794 */         if (this.accessibleParent == null) {
/* 4795 */           getAccessibleParent();
/*      */         }
/* 4797 */         Object[] arrayOfObject = this.path.getPath();
/* 4798 */         if (arrayOfObject.length > 1) {
/* 4799 */           Object localObject = arrayOfObject[(arrayOfObject.length - 2)];
/* 4800 */           if (this.treeModel != null) {
/* 4801 */             this.index = this.treeModel.getIndexOfChild(localObject, this.obj);
/*      */           }
/*      */         }
/* 4804 */         return this.index;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount()
/*      */       {
/* 4815 */         return this.treeModel.getChildCount(this.obj);
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 4827 */         if ((paramInt < 0) || (paramInt >= getAccessibleChildrenCount())) {
/* 4828 */           return null;
/*      */         }
/* 4830 */         Object localObject = this.treeModel.getChild(this.obj, paramInt);
/* 4831 */         Object[] arrayOfObject1 = this.path.getPath();
/* 4832 */         Object[] arrayOfObject2 = new Object[arrayOfObject1.length + 1];
/* 4833 */         System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, arrayOfObject1.length);
/* 4834 */         arrayOfObject2[(arrayOfObject2.length - 1)] = localObject;
/* 4835 */         TreePath localTreePath = new TreePath(arrayOfObject2);
/* 4836 */         return new AccessibleJTreeNode(JTree.AccessibleJTree.this, JTree.this, localTreePath, this);
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 4853 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4854 */         if (localAccessibleContext != null) {
/* 4855 */           return localAccessibleContext.getLocale();
/*      */         }
/* 4857 */         return this.tree.getLocale();
/*      */       }
/*      */ 
/*      */       public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 4868 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4869 */         if (localAccessibleContext != null)
/* 4870 */           localAccessibleContext.addPropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 4872 */           super.addPropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */       {
/* 4884 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4885 */         if (localAccessibleContext != null)
/* 4886 */           localAccessibleContext.removePropertyChangeListener(paramPropertyChangeListener);
/*      */         else
/* 4888 */           super.removePropertyChangeListener(paramPropertyChangeListener);
/*      */       }
/*      */ 
/*      */       public AccessibleAction getAccessibleAction()
/*      */       {
/* 4901 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleComponent getAccessibleComponent()
/*      */       {
/* 4913 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleSelection getAccessibleSelection()
/*      */       {
/* 4923 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4924 */         if ((localAccessibleContext != null) && (this.isLeaf)) {
/* 4925 */           return getCurrentAccessibleContext().getAccessibleSelection();
/*      */         }
/* 4927 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText()
/*      */       {
/* 4938 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4939 */         if (localAccessibleContext != null) {
/* 4940 */           return getCurrentAccessibleContext().getAccessibleText();
/*      */         }
/* 4942 */         return null;
/*      */       }
/*      */ 
/*      */       public AccessibleValue getAccessibleValue()
/*      */       {
/* 4953 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4954 */         if (localAccessibleContext != null) {
/* 4955 */           return getCurrentAccessibleContext().getAccessibleValue();
/*      */         }
/* 4957 */         return null;
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 4971 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4972 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 4973 */           return ((AccessibleComponent)localAccessibleContext).getBackground();
/*      */         }
/* 4975 */         Component localComponent = getCurrentComponent();
/* 4976 */         if (localComponent != null) {
/* 4977 */           return localComponent.getBackground();
/*      */         }
/* 4979 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 4990 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 4991 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 4992 */           ((AccessibleComponent)localAccessibleContext).setBackground(paramColor);
/*      */         } else {
/* 4994 */           Component localComponent = getCurrentComponent();
/* 4995 */           if (localComponent != null)
/* 4996 */             localComponent.setBackground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 5009 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5010 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5011 */           return ((AccessibleComponent)localAccessibleContext).getForeground();
/*      */         }
/* 5013 */         Component localComponent = getCurrentComponent();
/* 5014 */         if (localComponent != null) {
/* 5015 */           return localComponent.getForeground();
/*      */         }
/* 5017 */         return null;
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 5023 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5024 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5025 */           ((AccessibleComponent)localAccessibleContext).setForeground(paramColor);
/*      */         } else {
/* 5027 */           Component localComponent = getCurrentComponent();
/* 5028 */           if (localComponent != null)
/* 5029 */             localComponent.setForeground(paramColor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 5035 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5036 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5037 */           return ((AccessibleComponent)localAccessibleContext).getCursor();
/*      */         }
/* 5039 */         Component localComponent = getCurrentComponent();
/* 5040 */         if (localComponent != null) {
/* 5041 */           return localComponent.getCursor();
/*      */         }
/* 5043 */         Accessible localAccessible = getAccessibleParent();
/* 5044 */         if ((localAccessible instanceof AccessibleComponent)) {
/* 5045 */           return ((AccessibleComponent)localAccessible).getCursor();
/*      */         }
/* 5047 */         return null;
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 5054 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5055 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5056 */           ((AccessibleComponent)localAccessibleContext).setCursor(paramCursor);
/*      */         } else {
/* 5058 */           Component localComponent = getCurrentComponent();
/* 5059 */           if (localComponent != null)
/* 5060 */             localComponent.setCursor(paramCursor);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 5066 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5067 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5068 */           return ((AccessibleComponent)localAccessibleContext).getFont();
/*      */         }
/* 5070 */         Component localComponent = getCurrentComponent();
/* 5071 */         if (localComponent != null) {
/* 5072 */           return localComponent.getFont();
/*      */         }
/* 5074 */         return null;
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 5080 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5081 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5082 */           ((AccessibleComponent)localAccessibleContext).setFont(paramFont);
/*      */         } else {
/* 5084 */           Component localComponent = getCurrentComponent();
/* 5085 */           if (localComponent != null)
/* 5086 */             localComponent.setFont(paramFont);
/*      */         }
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 5092 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5093 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5094 */           return ((AccessibleComponent)localAccessibleContext).getFontMetrics(paramFont);
/*      */         }
/* 5096 */         Component localComponent = getCurrentComponent();
/* 5097 */         if (localComponent != null) {
/* 5098 */           return localComponent.getFontMetrics(paramFont);
/*      */         }
/* 5100 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 5106 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5107 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5108 */           return ((AccessibleComponent)localAccessibleContext).isEnabled();
/*      */         }
/* 5110 */         Component localComponent = getCurrentComponent();
/* 5111 */         if (localComponent != null) {
/* 5112 */           return localComponent.isEnabled();
/*      */         }
/* 5114 */         return false;
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 5120 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5121 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5122 */           ((AccessibleComponent)localAccessibleContext).setEnabled(paramBoolean);
/*      */         } else {
/* 5124 */           Component localComponent = getCurrentComponent();
/* 5125 */           if (localComponent != null)
/* 5126 */             localComponent.setEnabled(paramBoolean);
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 5132 */         Rectangle localRectangle1 = this.tree.getPathBounds(this.path);
/* 5133 */         Rectangle localRectangle2 = this.tree.getVisibleRect();
/* 5134 */         return (localRectangle1 != null) && (localRectangle2 != null) && (localRectangle2.intersects(localRectangle1));
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/*      */       }
/*      */ 
/*      */       public boolean isShowing() {
/* 5142 */         return (this.tree.isShowing()) && (isVisible());
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint) {
/* 5146 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5147 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5148 */           localObject = ((AccessibleComponent)localAccessibleContext).getBounds();
/* 5149 */           return ((Rectangle)localObject).contains(paramPoint);
/*      */         }
/* 5151 */         Object localObject = getCurrentComponent();
/* 5152 */         if (localObject != null) {
/* 5153 */           Rectangle localRectangle = ((Component)localObject).getBounds();
/* 5154 */           return localRectangle.contains(paramPoint);
/*      */         }
/* 5156 */         return getBounds().contains(paramPoint);
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 5162 */         if (this.tree != null) {
/* 5163 */           Point localPoint1 = this.tree.getLocationOnScreen();
/* 5164 */           Rectangle localRectangle = this.tree.getPathBounds(this.path);
/* 5165 */           if ((localPoint1 != null) && (localRectangle != null)) {
/* 5166 */             Point localPoint2 = new Point(localRectangle.x, localRectangle.y);
/*      */ 
/* 5168 */             localPoint2.translate(localPoint1.x, localPoint1.y);
/* 5169 */             return localPoint2;
/*      */           }
/* 5171 */           return null;
/*      */         }
/*      */ 
/* 5174 */         return null;
/*      */       }
/*      */ 
/*      */       protected Point getLocationInJTree()
/*      */       {
/* 5179 */         Rectangle localRectangle = this.tree.getPathBounds(this.path);
/* 5180 */         if (localRectangle != null) {
/* 5181 */           return localRectangle.getLocation();
/*      */         }
/* 5183 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 5188 */         Rectangle localRectangle = getBounds();
/* 5189 */         if (localRectangle != null) {
/* 5190 */           return localRectangle.getLocation();
/*      */         }
/* 5192 */         return null;
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds() {
/* 5200 */         Rectangle localRectangle = this.tree.getPathBounds(this.path);
/* 5201 */         Accessible localAccessible = getAccessibleParent();
/* 5202 */         if ((localAccessible != null) && 
/* 5203 */           ((localAccessible instanceof AccessibleJTreeNode))) {
/* 5204 */           Point localPoint = ((AccessibleJTreeNode)localAccessible).getLocationInJTree();
/* 5205 */           if ((localPoint != null) && (localRectangle != null))
/* 5206 */             localRectangle.translate(-localPoint.x, -localPoint.y);
/*      */           else {
/* 5208 */             return null;
/*      */           }
/*      */         }
/*      */ 
/* 5212 */         return localRectangle;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle) {
/* 5216 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5217 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5218 */           ((AccessibleComponent)localAccessibleContext).setBounds(paramRectangle);
/*      */         } else {
/* 5220 */           Component localComponent = getCurrentComponent();
/* 5221 */           if (localComponent != null)
/* 5222 */             localComponent.setBounds(paramRectangle);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 5228 */         return getBounds().getSize();
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension) {
/* 5232 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5233 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5234 */           ((AccessibleComponent)localAccessibleContext).setSize(paramDimension);
/*      */         } else {
/* 5236 */           Component localComponent = getCurrentComponent();
/* 5237 */           if (localComponent != null)
/* 5238 */             localComponent.setSize(paramDimension);
/*      */         }
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 5254 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5255 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5256 */           return ((AccessibleComponent)localAccessibleContext).getAccessibleAt(paramPoint);
/*      */         }
/* 5258 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 5263 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5264 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5265 */           return ((AccessibleComponent)localAccessibleContext).isFocusTraversable();
/*      */         }
/* 5267 */         Component localComponent = getCurrentComponent();
/* 5268 */         if (localComponent != null) {
/* 5269 */           return localComponent.isFocusTraversable();
/*      */         }
/* 5271 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/* 5277 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5278 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5279 */           ((AccessibleComponent)localAccessibleContext).requestFocus();
/*      */         } else {
/* 5281 */           Component localComponent = getCurrentComponent();
/* 5282 */           if (localComponent != null)
/* 5283 */             localComponent.requestFocus();
/*      */         }
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 5289 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5290 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5291 */           ((AccessibleComponent)localAccessibleContext).addFocusListener(paramFocusListener);
/*      */         } else {
/* 5293 */           Component localComponent = getCurrentComponent();
/* 5294 */           if (localComponent != null)
/* 5295 */             localComponent.addFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/* 5301 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5302 */         if ((localAccessibleContext instanceof AccessibleComponent)) {
/* 5303 */           ((AccessibleComponent)localAccessibleContext).removeFocusListener(paramFocusListener);
/*      */         } else {
/* 5305 */           Component localComponent = getCurrentComponent();
/* 5306 */           if (localComponent != null)
/* 5307 */             localComponent.removeFocusListener(paramFocusListener);
/*      */         }
/*      */       }
/*      */ 
/*      */       public int getAccessibleSelectionCount()
/*      */       {
/* 5321 */         int i = 0;
/* 5322 */         int j = getAccessibleChildrenCount();
/* 5323 */         for (int k = 0; k < j; k++) {
/* 5324 */           TreePath localTreePath = getChildTreePath(k);
/* 5325 */           if (this.tree.isPathSelected(localTreePath)) {
/* 5326 */             i++;
/*      */           }
/*      */         }
/* 5329 */         return i;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleSelection(int paramInt)
/*      */       {
/* 5342 */         int i = getAccessibleChildrenCount();
/* 5343 */         if ((paramInt < 0) || (paramInt >= i)) {
/* 5344 */           return null;
/*      */         }
/* 5346 */         int j = 0;
/* 5347 */         for (int k = 0; (k < i) && (paramInt >= j); k++) {
/* 5348 */           TreePath localTreePath = getChildTreePath(k);
/* 5349 */           if (this.tree.isPathSelected(localTreePath)) {
/* 5350 */             if (j == paramInt) {
/* 5351 */               return new AccessibleJTreeNode(JTree.AccessibleJTree.this, this.tree, localTreePath, this);
/*      */             }
/* 5353 */             j++;
/*      */           }
/*      */         }
/*      */ 
/* 5357 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleChildSelected(int paramInt)
/*      */       {
/* 5368 */         int i = getAccessibleChildrenCount();
/* 5369 */         if ((paramInt < 0) || (paramInt >= i)) {
/* 5370 */           return false;
/*      */         }
/* 5372 */         TreePath localTreePath = getChildTreePath(paramInt);
/* 5373 */         return this.tree.isPathSelected(localTreePath);
/*      */       }
/*      */ 
/*      */       public void addAccessibleSelection(int paramInt)
/*      */       {
/* 5387 */         TreeModel localTreeModel = JTree.this.getModel();
/* 5388 */         if ((localTreeModel != null) && 
/* 5389 */           (paramInt >= 0) && (paramInt < getAccessibleChildrenCount())) {
/* 5390 */           TreePath localTreePath = getChildTreePath(paramInt);
/* 5391 */           JTree.this.addSelectionPath(localTreePath);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void removeAccessibleSelection(int paramInt)
/*      */       {
/* 5405 */         TreeModel localTreeModel = JTree.this.getModel();
/* 5406 */         if ((localTreeModel != null) && 
/* 5407 */           (paramInt >= 0) && (paramInt < getAccessibleChildrenCount())) {
/* 5408 */           TreePath localTreePath = getChildTreePath(paramInt);
/* 5409 */           JTree.this.removeSelectionPath(localTreePath);
/*      */         }
/*      */       }
/*      */ 
/*      */       public void clearAccessibleSelection()
/*      */       {
/* 5419 */         int i = getAccessibleChildrenCount();
/* 5420 */         for (int j = 0; j < i; j++)
/* 5421 */           removeAccessibleSelection(j);
/*      */       }
/*      */ 
/*      */       public void selectAllAccessibleSelection()
/*      */       {
/* 5430 */         TreeModel localTreeModel = JTree.this.getModel();
/* 5431 */         if (localTreeModel != null) {
/* 5432 */           int i = getAccessibleChildrenCount();
/*      */ 
/* 5434 */           for (int j = 0; j < i; j++) {
/* 5435 */             TreePath localTreePath = getChildTreePath(j);
/* 5436 */             JTree.this.addSelectionPath(localTreePath);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public int getAccessibleActionCount()
/*      */       {
/* 5452 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5453 */         if (localAccessibleContext != null) {
/* 5454 */           AccessibleAction localAccessibleAction = localAccessibleContext.getAccessibleAction();
/* 5455 */           if (localAccessibleAction != null) {
/* 5456 */             return localAccessibleAction.getAccessibleActionCount() + (this.isLeaf ? 0 : 1);
/*      */           }
/*      */         }
/* 5459 */         return this.isLeaf ? 0 : 1;
/*      */       }
/*      */ 
/*      */       public String getAccessibleActionDescription(int paramInt)
/*      */       {
/* 5472 */         if ((paramInt < 0) || (paramInt >= getAccessibleActionCount())) {
/* 5473 */           return null;
/*      */         }
/* 5475 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5476 */         if (paramInt == 0)
/*      */         {
/* 5478 */           return AccessibleAction.TOGGLE_EXPAND;
/* 5479 */         }if (localAccessibleContext != null) {
/* 5480 */           AccessibleAction localAccessibleAction = localAccessibleContext.getAccessibleAction();
/* 5481 */           if (localAccessibleAction != null) {
/* 5482 */             return localAccessibleAction.getAccessibleActionDescription(paramInt - 1);
/*      */           }
/*      */         }
/* 5485 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean doAccessibleAction(int paramInt)
/*      */       {
/* 5498 */         if ((paramInt < 0) || (paramInt >= getAccessibleActionCount())) {
/* 5499 */           return false;
/*      */         }
/* 5501 */         AccessibleContext localAccessibleContext = getCurrentAccessibleContext();
/* 5502 */         if (paramInt == 0) {
/* 5503 */           if (JTree.this.isExpanded(this.path))
/* 5504 */             JTree.this.collapsePath(this.path);
/*      */           else {
/* 5506 */             JTree.this.expandPath(this.path);
/*      */           }
/* 5508 */           return true;
/* 5509 */         }if (localAccessibleContext != null) {
/* 5510 */           AccessibleAction localAccessibleAction = localAccessibleContext.getAccessibleAction();
/* 5511 */           if (localAccessibleAction != null) {
/* 5512 */             return localAccessibleAction.doAccessibleAction(paramInt - 1);
/*      */           }
/*      */         }
/* 5515 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DropLocation extends TransferHandler.DropLocation
/*      */   {
/*      */     private final TreePath path;
/*      */     private final int index;
/*      */ 
/*      */     private DropLocation(Point paramPoint, TreePath paramTreePath, int paramInt)
/*      */     {
/*  331 */       super();
/*  332 */       this.path = paramTreePath;
/*  333 */       this.index = paramInt;
/*      */     }
/*      */ 
/*      */     public int getChildIndex()
/*      */     {
/*  361 */       return this.index;
/*      */     }
/*      */ 
/*      */     public TreePath getPath()
/*      */     {
/*  400 */       return this.path;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  412 */       return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "path=" + this.path + "," + "childIndex=" + this.index + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DynamicUtilTreeNode extends DefaultMutableTreeNode
/*      */   {
/*      */     protected boolean hasChildren;
/*      */     protected Object childValue;
/*      */     protected boolean loadedChildren;
/*      */ 
/*      */     public static void createChildren(DefaultMutableTreeNode paramDefaultMutableTreeNode, Object paramObject)
/*      */     {
/*      */       Object localObject1;
/* 3889 */       if ((paramObject instanceof Vector)) {
/* 3890 */         localObject1 = (Vector)paramObject;
/*      */ 
/* 3892 */         int i = 0; int k = ((Vector)localObject1).size();
/* 3893 */         for (; i < k; i++) {
/* 3894 */           paramDefaultMutableTreeNode.add(new DynamicUtilTreeNode(((Vector)localObject1).elementAt(i), ((Vector)localObject1).elementAt(i)));
/*      */         }
/*      */ 
/*      */       }
/* 3898 */       else if ((paramObject instanceof Hashtable)) {
/* 3899 */         localObject1 = (Hashtable)paramObject;
/* 3900 */         Enumeration localEnumeration = ((Hashtable)localObject1).keys();
/*      */ 
/* 3903 */         while (localEnumeration.hasMoreElements()) {
/* 3904 */           Object localObject2 = localEnumeration.nextElement();
/* 3905 */           paramDefaultMutableTreeNode.add(new DynamicUtilTreeNode(localObject2, ((Hashtable)localObject1).get(localObject2)));
/*      */         }
/*      */ 
/*      */       }
/* 3909 */       else if ((paramObject instanceof Object[])) {
/* 3910 */         localObject1 = (Object[])paramObject;
/*      */ 
/* 3912 */         int j = 0; int m = localObject1.length;
/* 3913 */         for (; j < m; j++)
/* 3914 */           paramDefaultMutableTreeNode.add(new DynamicUtilTreeNode(localObject1[j], localObject1[j]));
/*      */       }
/*      */     }
/*      */ 
/*      */     public DynamicUtilTreeNode(Object paramObject1, Object paramObject2)
/*      */     {
/* 3937 */       super();
/* 3938 */       this.loadedChildren = false;
/* 3939 */       this.childValue = paramObject2;
/* 3940 */       if (paramObject2 != null) {
/* 3941 */         if ((paramObject2 instanceof Vector))
/* 3942 */           setAllowsChildren(true);
/* 3943 */         else if ((paramObject2 instanceof Hashtable))
/* 3944 */           setAllowsChildren(true);
/* 3945 */         else if ((paramObject2 instanceof Object[]))
/* 3946 */           setAllowsChildren(true);
/*      */         else
/* 3948 */           setAllowsChildren(false);
/*      */       }
/*      */       else
/* 3951 */         setAllowsChildren(false);
/*      */     }
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 3962 */       return !getAllowsChildren();
/*      */     }
/*      */ 
/*      */     public int getChildCount()
/*      */     {
/* 3971 */       if (!this.loadedChildren)
/* 3972 */         loadChildren();
/* 3973 */       return super.getChildCount();
/*      */     }
/*      */ 
/*      */     protected void loadChildren()
/*      */     {
/* 3985 */       this.loadedChildren = true;
/* 3986 */       createChildren(this, this.childValue);
/*      */     }
/*      */ 
/*      */     public TreeNode getChildAt(int paramInt)
/*      */     {
/* 3993 */       if (!this.loadedChildren)
/* 3994 */         loadChildren();
/* 3995 */       return super.getChildAt(paramInt);
/*      */     }
/*      */ 
/*      */     public Enumeration children()
/*      */     {
/* 4002 */       if (!this.loadedChildren)
/* 4003 */         loadChildren();
/* 4004 */       return super.children();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class EmptySelectionModel extends DefaultTreeSelectionModel
/*      */   {
/* 3254 */     protected static final EmptySelectionModel sharedInstance = new EmptySelectionModel();
/*      */ 
/*      */     public static EmptySelectionModel sharedInstance()
/*      */     {
/* 3263 */       return sharedInstance;
/*      */     }
/*      */ 
/*      */     public void setSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setSelectionMode(int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setRowMapper(RowMapper paramRowMapper)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
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
/*      */   protected class TreeModelHandler
/*      */     implements TreeModelListener
/*      */   {
/*      */     protected TreeModelHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void treeNodesInserted(TreeModelEvent paramTreeModelEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void treeStructureChanged(TreeModelEvent paramTreeModelEvent)
/*      */     {
/* 3782 */       if (paramTreeModelEvent == null) {
/* 3783 */         return;
/*      */       }
/*      */ 
/* 3789 */       TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*      */ 
/* 3791 */       if (localTreePath == null) {
/* 3792 */         return;
/*      */       }
/* 3794 */       if (localTreePath.getPathCount() == 1)
/*      */       {
/* 3796 */         JTree.this.clearToggledPaths();
/* 3797 */         if ((JTree.this.treeModel.getRoot() != null) && (!JTree.this.treeModel.isLeaf(JTree.this.treeModel.getRoot())))
/*      */         {
/* 3800 */           JTree.this.expandedState.put(localTreePath, Boolean.TRUE);
/*      */         }
/*      */       }
/* 3803 */       else if (JTree.this.expandedState.get(localTreePath) != null) {
/* 3804 */         Vector localVector = new Vector(1);
/* 3805 */         boolean bool = JTree.this.isExpanded(localTreePath);
/*      */ 
/* 3807 */         localVector.addElement(localTreePath);
/* 3808 */         JTree.this.removeDescendantToggledPaths(localVector.elements());
/* 3809 */         if (bool) {
/* 3810 */           TreeModel localTreeModel = JTree.this.getModel();
/*      */ 
/* 3812 */           if ((localTreeModel == null) || (localTreeModel.isLeaf(localTreePath.getLastPathComponent())))
/*      */           {
/* 3814 */             JTree.this.collapsePath(localTreePath);
/*      */           }
/* 3816 */           else JTree.this.expandedState.put(localTreePath, Boolean.TRUE);
/*      */         }
/*      */       }
/* 3819 */       JTree.this.removeDescendantSelectedPaths(localTreePath, false);
/*      */     }
/*      */ 
/*      */     public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent) {
/* 3823 */       if (paramTreeModelEvent == null) {
/* 3824 */         return;
/*      */       }
/* 3826 */       TreePath localTreePath1 = paramTreeModelEvent.getTreePath();
/* 3827 */       Object[] arrayOfObject = paramTreeModelEvent.getChildren();
/*      */ 
/* 3829 */       if (arrayOfObject == null) {
/* 3830 */         return;
/*      */       }
/*      */ 
/* 3833 */       Vector localVector = new Vector(Math.max(1, arrayOfObject.length));
/*      */ 
/* 3836 */       for (int i = arrayOfObject.length - 1; i >= 0; i--) {
/* 3837 */         TreePath localTreePath2 = localTreePath1.pathByAddingChild(arrayOfObject[i]);
/* 3838 */         if (JTree.this.expandedState.get(localTreePath2) != null)
/* 3839 */           localVector.addElement(localTreePath2);
/*      */       }
/* 3841 */       if (localVector.size() > 0) {
/* 3842 */         JTree.this.removeDescendantToggledPaths(localVector.elements());
/*      */       }
/* 3844 */       TreeModel localTreeModel = JTree.this.getModel();
/*      */ 
/* 3846 */       if ((localTreeModel == null) || (localTreeModel.isLeaf(localTreePath1.getLastPathComponent()))) {
/* 3847 */         JTree.this.expandedState.remove(localTreePath1);
/*      */       }
/* 3849 */       JTree.this.removeDescendantSelectedPaths(paramTreeModelEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class TreeSelectionRedirector
/*      */     implements Serializable, TreeSelectionListener
/*      */   {
/*      */     protected TreeSelectionRedirector()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */     {
/* 3382 */       TreeSelectionEvent localTreeSelectionEvent = (TreeSelectionEvent)paramTreeSelectionEvent.cloneWithSource(JTree.this);
/* 3383 */       JTree.this.fireValueChanged(localTreeSelectionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TreeTimer extends Timer
/*      */   {
/*      */     public TreeTimer()
/*      */     {
/*  426 */       super(null);
/*  427 */       setRepeats(false);
/*      */     }
/*      */ 
/*      */     public void fireActionPerformed(ActionEvent paramActionEvent) {
/*  431 */       JTree.this.expandRow(JTree.this.expandRow);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTree
 * JD-Core Version:    0.6.2
 */