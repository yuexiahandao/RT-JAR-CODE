/*      */ package javax.swing.tree;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Stack;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ 
/*      */ public class FixedHeightLayoutCache extends AbstractLayoutCache
/*      */ {
/*      */   private FHTreeStateNode root;
/*      */   private int rowCount;
/*      */   private Rectangle boundsBuffer;
/*      */   private Hashtable<TreePath, FHTreeStateNode> treePathMapping;
/*      */   private SearchInfo info;
/*      */   private Stack<Stack<TreePath>> tempStacks;
/*      */ 
/*      */   public FixedHeightLayoutCache()
/*      */   {
/*   79 */     this.tempStacks = new Stack();
/*   80 */     this.boundsBuffer = new Rectangle();
/*   81 */     this.treePathMapping = new Hashtable();
/*   82 */     this.info = new SearchInfo(null);
/*   83 */     setRowHeight(1);
/*      */   }
/*      */ 
/*      */   public void setModel(TreeModel paramTreeModel)
/*      */   {
/*   92 */     super.setModel(paramTreeModel);
/*   93 */     rebuild(false);
/*      */   }
/*      */ 
/*      */   public void setRootVisible(boolean paramBoolean)
/*      */   {
/*  104 */     if (isRootVisible() != paramBoolean) {
/*  105 */       super.setRootVisible(paramBoolean);
/*  106 */       if (this.root != null) {
/*  107 */         if (paramBoolean) {
/*  108 */           this.rowCount += 1;
/*  109 */           this.root.adjustRowBy(1);
/*      */         }
/*      */         else {
/*  112 */           this.rowCount -= 1;
/*  113 */           this.root.adjustRowBy(-1);
/*      */         }
/*  115 */         visibleNodesChanged();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setRowHeight(int paramInt)
/*      */   {
/*  127 */     if (paramInt <= 0)
/*  128 */       throw new IllegalArgumentException("FixedHeightLayoutCache only supports row heights greater than 0");
/*  129 */     if (getRowHeight() != paramInt) {
/*  130 */       super.setRowHeight(paramInt);
/*  131 */       visibleNodesChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */   {
/*  139 */     return this.rowCount;
/*      */   }
/*      */ 
/*      */   public void invalidatePathBounds(TreePath paramTreePath)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void invalidateSizes()
/*      */   {
/*  157 */     visibleNodesChanged();
/*      */   }
/*      */ 
/*      */   public boolean isExpanded(TreePath paramTreePath)
/*      */   {
/*  164 */     if (paramTreePath != null) {
/*  165 */       FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  167 */       return (localFHTreeStateNode != null) && (localFHTreeStateNode.isExpanded());
/*      */     }
/*  169 */     return false;
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds(TreePath paramTreePath, Rectangle paramRectangle)
/*      */   {
/*  180 */     if (paramTreePath == null) {
/*  181 */       return null;
/*      */     }
/*  183 */     FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  185 */     if (localFHTreeStateNode != null) {
/*  186 */       return getBounds(localFHTreeStateNode, -1, paramRectangle);
/*      */     }
/*      */ 
/*  189 */     TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/*  191 */     localFHTreeStateNode = getNodeForPath(localTreePath, true, false);
/*  192 */     if ((localFHTreeStateNode != null) && (localFHTreeStateNode.isExpanded())) {
/*  193 */       int i = this.treeModel.getIndexOfChild(localTreePath.getLastPathComponent(), paramTreePath.getLastPathComponent());
/*      */ 
/*  197 */       if (i != -1)
/*  198 */         return getBounds(localFHTreeStateNode, i, paramRectangle);
/*      */     }
/*  200 */     return null;
/*      */   }
/*      */ 
/*      */   public TreePath getPathForRow(int paramInt)
/*      */   {
/*  208 */     if ((paramInt >= 0) && (paramInt < getRowCount()) && 
/*  209 */       (this.root.getPathForRow(paramInt, getRowCount(), this.info))) {
/*  210 */       return this.info.getPath();
/*      */     }
/*      */ 
/*  213 */     return null;
/*      */   }
/*      */ 
/*      */   public int getRowForPath(TreePath paramTreePath)
/*      */   {
/*  222 */     if ((paramTreePath == null) || (this.root == null)) {
/*  223 */       return -1;
/*      */     }
/*  225 */     FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  227 */     if (localFHTreeStateNode != null) {
/*  228 */       return localFHTreeStateNode.getRow();
/*      */     }
/*  230 */     TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/*  232 */     localFHTreeStateNode = getNodeForPath(localTreePath, true, false);
/*  233 */     if ((localFHTreeStateNode != null) && (localFHTreeStateNode.isExpanded())) {
/*  234 */       return localFHTreeStateNode.getRowToModelIndex(this.treeModel.getIndexOfChild(localTreePath.getLastPathComponent(), paramTreePath.getLastPathComponent()));
/*      */     }
/*      */ 
/*  238 */     return -1;
/*      */   }
/*      */ 
/*      */   public TreePath getPathClosestTo(int paramInt1, int paramInt2)
/*      */   {
/*  249 */     if (getRowCount() == 0) {
/*  250 */       return null;
/*      */     }
/*  252 */     int i = getRowContainingYLocation(paramInt2);
/*      */ 
/*  254 */     return getPathForRow(i);
/*      */   }
/*      */ 
/*      */   public int getVisibleChildCount(TreePath paramTreePath)
/*      */   {
/*  261 */     FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  263 */     if (localFHTreeStateNode == null)
/*  264 */       return 0;
/*  265 */     return localFHTreeStateNode.getTotalChildCount();
/*      */   }
/*      */ 
/*      */   public Enumeration<TreePath> getVisiblePathsFrom(TreePath paramTreePath)
/*      */   {
/*  274 */     if (paramTreePath == null) {
/*  275 */       return null;
/*      */     }
/*  277 */     FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  279 */     if (localFHTreeStateNode != null) {
/*  280 */       return new VisibleFHTreeStateNodeEnumeration(localFHTreeStateNode);
/*      */     }
/*  282 */     TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/*  284 */     localFHTreeStateNode = getNodeForPath(localTreePath, true, false);
/*  285 */     if ((localFHTreeStateNode != null) && (localFHTreeStateNode.isExpanded())) {
/*  286 */       return new VisibleFHTreeStateNodeEnumeration(localFHTreeStateNode, this.treeModel.getIndexOfChild(localTreePath.getLastPathComponent(), paramTreePath.getLastPathComponent()));
/*      */     }
/*      */ 
/*  290 */     return null;
/*      */   }
/*      */ 
/*      */   public void setExpandedState(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/*  298 */     if (paramBoolean) {
/*  299 */       ensurePathIsExpanded(paramTreePath, true);
/*  300 */     } else if (paramTreePath != null) {
/*  301 */       TreePath localTreePath = paramTreePath.getParentPath();
/*      */ 
/*  304 */       if (localTreePath != null) {
/*  305 */         localFHTreeStateNode = getNodeForPath(localTreePath, false, true);
/*      */ 
/*  307 */         if (localFHTreeStateNode != null) {
/*  308 */           localFHTreeStateNode.makeVisible();
/*      */         }
/*      */       }
/*  311 */       FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  314 */       if (localFHTreeStateNode != null)
/*  315 */         localFHTreeStateNode.collapse(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getExpandedState(TreePath paramTreePath)
/*      */   {
/*  323 */     FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  325 */     return (localFHTreeStateNode.isVisible()) && (localFHTreeStateNode.isExpanded());
/*      */   }
/*      */ 
/*      */   public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  346 */     if (paramTreeModelEvent != null)
/*      */     {
/*  348 */       FHTreeStateNode localFHTreeStateNode1 = getNodeForPath(paramTreeModelEvent.getTreePath(), false, false);
/*      */ 
/*  352 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*      */ 
/*  356 */       if (localFHTreeStateNode1 != null)
/*      */       {
/*      */         int i;
/*  357 */         if ((arrayOfInt != null) && ((i = arrayOfInt.length) > 0))
/*      */         {
/*  359 */           Object localObject = localFHTreeStateNode1.getUserObject();
/*      */ 
/*  361 */           for (int j = 0; j < i; j++) {
/*  362 */             FHTreeStateNode localFHTreeStateNode2 = localFHTreeStateNode1.getChildAtModelIndex(arrayOfInt[j]);
/*      */ 
/*  365 */             if (localFHTreeStateNode2 != null) {
/*  366 */               localFHTreeStateNode2.setUserObject(this.treeModel.getChild(localObject, arrayOfInt[j]));
/*      */             }
/*      */           }
/*      */ 
/*  370 */           if ((localFHTreeStateNode1.isVisible()) && (localFHTreeStateNode1.isExpanded())) {
/*  371 */             visibleNodesChanged();
/*      */           }
/*      */         }
/*  374 */         else if ((localFHTreeStateNode1 == this.root) && (localFHTreeStateNode1.isVisible()) && (localFHTreeStateNode1.isExpanded()))
/*      */         {
/*  376 */           visibleNodesChanged();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeNodesInserted(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  390 */     if (paramTreeModelEvent != null)
/*      */     {
/*  392 */       FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreeModelEvent.getTreePath(), false, false);
/*      */ 
/*  396 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*      */       int i;
/*  400 */       if ((localFHTreeStateNode != null) && (arrayOfInt != null) && ((i = arrayOfInt.length) > 0))
/*      */       {
/*  402 */         boolean bool = (localFHTreeStateNode.isVisible()) && (localFHTreeStateNode.isExpanded());
/*      */ 
/*  406 */         for (int j = 0; j < i; j++) {
/*  407 */           localFHTreeStateNode.childInsertedAtModelIndex(arrayOfInt[j], bool);
/*      */         }
/*      */ 
/*  410 */         if ((bool) && (this.treeSelectionModel != null))
/*  411 */           this.treeSelectionModel.resetRowSelection();
/*  412 */         if (localFHTreeStateNode.isVisible())
/*  413 */           visibleNodesChanged();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  429 */     if (paramTreeModelEvent != null)
/*      */     {
/*  432 */       TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*  433 */       FHTreeStateNode localFHTreeStateNode = getNodeForPath(localTreePath, false, false);
/*      */ 
/*  436 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*      */       int i;
/*  439 */       if ((localFHTreeStateNode != null) && (arrayOfInt != null) && ((i = arrayOfInt.length) > 0))
/*      */       {
/*  441 */         Object[] arrayOfObject = paramTreeModelEvent.getChildren();
/*  442 */         boolean bool = (localFHTreeStateNode.isVisible()) && (localFHTreeStateNode.isExpanded());
/*      */ 
/*  446 */         for (int j = i - 1; j >= 0; j--) {
/*  447 */           localFHTreeStateNode.removeChildAtModelIndex(arrayOfInt[j], bool);
/*      */         }
/*      */ 
/*  450 */         if (bool) {
/*  451 */           if (this.treeSelectionModel != null)
/*  452 */             this.treeSelectionModel.resetRowSelection();
/*  453 */           if ((this.treeModel.getChildCount(localFHTreeStateNode.getUserObject()) == 0) && (localFHTreeStateNode.isLeaf()))
/*      */           {
/*  457 */             localFHTreeStateNode.collapse(false);
/*      */           }
/*  459 */           visibleNodesChanged();
/*      */         }
/*  461 */         else if (localFHTreeStateNode.isVisible()) {
/*  462 */           visibleNodesChanged();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeStructureChanged(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  477 */     if (paramTreeModelEvent != null) {
/*  478 */       TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*  479 */       FHTreeStateNode localFHTreeStateNode1 = getNodeForPath(localTreePath, false, false);
/*      */ 
/*  484 */       if ((localFHTreeStateNode1 == this.root) || ((localFHTreeStateNode1 == null) && (((localTreePath == null) && (this.treeModel != null) && (this.treeModel.getRoot() == null)) || ((localTreePath != null) && (localTreePath.getPathCount() <= 1)))))
/*      */       {
/*  489 */         rebuild(true);
/*      */       }
/*  491 */       else if (localFHTreeStateNode1 != null)
/*      */       {
/*  493 */         FHTreeStateNode localFHTreeStateNode2 = (FHTreeStateNode)localFHTreeStateNode1.getParent();
/*      */ 
/*  496 */         boolean bool1 = localFHTreeStateNode1.isExpanded();
/*  497 */         boolean bool2 = localFHTreeStateNode1.isVisible();
/*      */ 
/*  499 */         int i = localFHTreeStateNode2.getIndex(localFHTreeStateNode1);
/*  500 */         localFHTreeStateNode1.collapse(false);
/*  501 */         localFHTreeStateNode2.remove(i);
/*      */ 
/*  503 */         if ((bool2) && (bool1)) {
/*  504 */           int j = localFHTreeStateNode1.getRow();
/*  505 */           localFHTreeStateNode2.resetChildrenRowsFrom(j, i, localFHTreeStateNode1.getChildIndex());
/*      */ 
/*  507 */           localFHTreeStateNode1 = getNodeForPath(localTreePath, false, true);
/*  508 */           localFHTreeStateNode1.expand();
/*      */         }
/*  510 */         if ((this.treeSelectionModel != null) && (bool2) && (bool1))
/*  511 */           this.treeSelectionModel.resetRowSelection();
/*  512 */         if (bool2)
/*  513 */           visibleNodesChanged();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void visibleNodesChanged()
/*      */   {
/*      */   }
/*      */ 
/*      */   private Rectangle getBounds(FHTreeStateNode paramFHTreeStateNode, int paramInt, Rectangle paramRectangle)
/*      */   {
/*      */     int j;
/*      */     Object localObject;
/*      */     boolean bool;
/*      */     int i;
/*  538 */     if (paramInt == -1)
/*      */     {
/*  540 */       j = paramFHTreeStateNode.getRow();
/*  541 */       localObject = paramFHTreeStateNode.getUserObject();
/*  542 */       bool = paramFHTreeStateNode.isExpanded();
/*  543 */       i = paramFHTreeStateNode.getLevel();
/*      */     }
/*      */     else {
/*  546 */       j = paramFHTreeStateNode.getRowToModelIndex(paramInt);
/*  547 */       localObject = this.treeModel.getChild(paramFHTreeStateNode.getUserObject(), paramInt);
/*  548 */       bool = false;
/*  549 */       i = paramFHTreeStateNode.getLevel() + 1;
/*      */     }
/*      */ 
/*  552 */     Rectangle localRectangle = getNodeDimensions(localObject, j, i, bool, this.boundsBuffer);
/*      */ 
/*  555 */     if (localRectangle == null) {
/*  556 */       return null;
/*      */     }
/*  558 */     if (paramRectangle == null) {
/*  559 */       paramRectangle = new Rectangle();
/*      */     }
/*  561 */     paramRectangle.x = localRectangle.x;
/*  562 */     paramRectangle.height = getRowHeight();
/*  563 */     paramRectangle.y = (j * paramRectangle.height);
/*  564 */     paramRectangle.width = localRectangle.width;
/*  565 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   private void adjustRowCountBy(int paramInt)
/*      */   {
/*  573 */     this.rowCount += paramInt;
/*      */   }
/*      */ 
/*      */   private void addMapping(FHTreeStateNode paramFHTreeStateNode)
/*      */   {
/*  580 */     this.treePathMapping.put(paramFHTreeStateNode.getTreePath(), paramFHTreeStateNode);
/*      */   }
/*      */ 
/*      */   private void removeMapping(FHTreeStateNode paramFHTreeStateNode)
/*      */   {
/*  587 */     this.treePathMapping.remove(paramFHTreeStateNode.getTreePath());
/*      */   }
/*      */ 
/*      */   private FHTreeStateNode getMapping(TreePath paramTreePath)
/*      */   {
/*  595 */     return (FHTreeStateNode)this.treePathMapping.get(paramTreePath);
/*      */   }
/*      */ 
/*      */   private void rebuild(boolean paramBoolean)
/*      */   {
/*  604 */     this.treePathMapping.clear();
/*      */     Object localObject;
/*  605 */     if ((this.treeModel != null) && ((localObject = this.treeModel.getRoot()) != null)) {
/*  606 */       this.root = createNodeForValue(localObject, 0);
/*  607 */       this.root.path = new TreePath(localObject);
/*  608 */       addMapping(this.root);
/*  609 */       if (isRootVisible()) {
/*  610 */         this.rowCount = 1;
/*  611 */         this.root.row = 0;
/*      */       }
/*      */       else {
/*  614 */         this.rowCount = 0;
/*  615 */         this.root.row = -1;
/*      */       }
/*  617 */       this.root.expand();
/*      */     }
/*      */     else {
/*  620 */       this.root = null;
/*  621 */       this.rowCount = 0;
/*      */     }
/*  623 */     if ((paramBoolean) && (this.treeSelectionModel != null)) {
/*  624 */       this.treeSelectionModel.clearSelection();
/*      */     }
/*  626 */     visibleNodesChanged();
/*      */   }
/*      */ 
/*      */   private int getRowContainingYLocation(int paramInt)
/*      */   {
/*  635 */     if (getRowCount() == 0)
/*  636 */       return -1;
/*  637 */     return Math.max(0, Math.min(getRowCount() - 1, paramInt / getRowHeight()));
/*      */   }
/*      */ 
/*      */   private boolean ensurePathIsExpanded(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/*  649 */     if (paramTreePath != null)
/*      */     {
/*  651 */       if (this.treeModel.isLeaf(paramTreePath.getLastPathComponent())) {
/*  652 */         paramTreePath = paramTreePath.getParentPath();
/*  653 */         paramBoolean = true;
/*      */       }
/*  655 */       if (paramTreePath != null) {
/*  656 */         FHTreeStateNode localFHTreeStateNode = getNodeForPath(paramTreePath, false, true);
/*      */ 
/*  659 */         if (localFHTreeStateNode != null) {
/*  660 */           localFHTreeStateNode.makeVisible();
/*  661 */           if (paramBoolean)
/*  662 */             localFHTreeStateNode.expand();
/*  663 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  667 */     return false;
/*      */   }
/*      */ 
/*      */   private FHTreeStateNode createNodeForValue(Object paramObject, int paramInt)
/*      */   {
/*  674 */     return new FHTreeStateNode(paramObject, paramInt, -1);
/*      */   }
/*      */ 
/*      */   private FHTreeStateNode getNodeForPath(TreePath paramTreePath, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  685 */     if (paramTreePath != null)
/*      */     {
/*  688 */       FHTreeStateNode localFHTreeStateNode1 = getMapping(paramTreePath);
/*  689 */       if (localFHTreeStateNode1 != null) {
/*  690 */         if ((paramBoolean1) && (!localFHTreeStateNode1.isVisible()))
/*  691 */           return null;
/*  692 */         return localFHTreeStateNode1;
/*      */       }
/*  694 */       if (paramBoolean1)
/*  695 */         return null;
/*      */       Stack localStack;
/*  700 */       if (this.tempStacks.size() == 0) {
/*  701 */         localStack = new Stack();
/*      */       }
/*      */       else {
/*  704 */         localStack = (Stack)this.tempStacks.pop();
/*      */       }
/*      */       try
/*      */       {
/*  708 */         localStack.push(paramTreePath);
/*  709 */         paramTreePath = paramTreePath.getParentPath();
/*  710 */         localFHTreeStateNode1 = null;
/*  711 */         while (paramTreePath != null) {
/*  712 */           localFHTreeStateNode1 = getMapping(paramTreePath);
/*  713 */           if (localFHTreeStateNode1 != null)
/*      */           {
/*  716 */             while ((localFHTreeStateNode1 != null) && (localStack.size() > 0)) {
/*  717 */               paramTreePath = (TreePath)localStack.pop();
/*  718 */               localFHTreeStateNode1 = localFHTreeStateNode1.createChildFor(paramTreePath.getLastPathComponent());
/*      */             }
/*      */ 
/*  721 */             return localFHTreeStateNode1;
/*      */           }
/*  723 */           localStack.push(paramTreePath);
/*  724 */           paramTreePath = paramTreePath.getParentPath();
/*      */         }
/*      */       }
/*      */       finally {
/*  728 */         localStack.removeAllElements();
/*  729 */         this.tempStacks.push(localStack);
/*      */       }
/*      */ 
/*  732 */       return null;
/*      */     }
/*  734 */     return null;
/*      */   }
/*      */ 
/*      */   private class FHTreeStateNode extends DefaultMutableTreeNode
/*      */   {
/*      */     protected boolean isExpanded;
/*      */     protected int childIndex;
/*      */     protected int childCount;
/*      */     protected int row;
/*      */     protected TreePath path;
/*      */ 
/*      */     public FHTreeStateNode(Object paramInt1, int paramInt2, int arg4)
/*      */     {
/*  763 */       super();
/*  764 */       this.childIndex = paramInt2;
/*      */       int i;
/*  765 */       this.row = i;
/*      */     }
/*      */ 
/*      */     public void setParent(MutableTreeNode paramMutableTreeNode)
/*      */     {
/*  777 */       super.setParent(paramMutableTreeNode);
/*  778 */       if (paramMutableTreeNode != null) {
/*  779 */         this.path = ((FHTreeStateNode)paramMutableTreeNode).getTreePath().pathByAddingChild(getUserObject());
/*      */ 
/*  781 */         FixedHeightLayoutCache.this.addMapping(this);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove(int paramInt)
/*      */     {
/*  790 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(paramInt);
/*      */ 
/*  792 */       localFHTreeStateNode.removeFromMapping();
/*  793 */       super.remove(paramInt);
/*      */     }
/*      */ 
/*      */     public void setUserObject(Object paramObject)
/*      */     {
/*  800 */       super.setUserObject(paramObject);
/*  801 */       if (this.path != null) {
/*  802 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/*      */ 
/*  804 */         if (localFHTreeStateNode != null)
/*  805 */           resetChildrenPaths(localFHTreeStateNode.getTreePath());
/*      */         else
/*  807 */           resetChildrenPaths(null);
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getChildIndex()
/*      */     {
/*  818 */       return this.childIndex;
/*      */     }
/*      */ 
/*      */     public TreePath getTreePath()
/*      */     {
/*  825 */       return this.path;
/*      */     }
/*      */ 
/*      */     public FHTreeStateNode getChildAtModelIndex(int paramInt)
/*      */     {
/*  835 */       for (int i = getChildCount() - 1; i >= 0; i--)
/*  836 */         if (((FHTreeStateNode)getChildAt(i)).childIndex == paramInt)
/*  837 */           return (FHTreeStateNode)getChildAt(i);
/*  838 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/*  846 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/*      */ 
/*  848 */       if (localFHTreeStateNode == null)
/*  849 */         return true;
/*  850 */       return (localFHTreeStateNode.isExpanded()) && (localFHTreeStateNode.isVisible());
/*      */     }
/*      */ 
/*      */     public int getRow()
/*      */     {
/*  857 */       return this.row;
/*      */     }
/*      */ 
/*      */     public int getRowToModelIndex(int paramInt)
/*      */     {
/*  866 */       int i = getRow() + 1;
/*  867 */       int j = i;
/*      */ 
/*  870 */       int k = 0; int m = getChildCount();
/*  871 */       for (; k < m; k++) {
/*  872 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(k);
/*  873 */         if (localFHTreeStateNode.childIndex >= paramInt) {
/*  874 */           if (localFHTreeStateNode.childIndex == paramInt)
/*  875 */             return localFHTreeStateNode.row;
/*  876 */           if (k == 0)
/*  877 */             return getRow() + 1 + paramInt;
/*  878 */           return localFHTreeStateNode.row - (localFHTreeStateNode.childIndex - paramInt);
/*      */         }
/*      */       }
/*      */ 
/*  882 */       return getRow() + 1 + getTotalChildCount() - (this.childCount - paramInt);
/*      */     }
/*      */ 
/*      */     public int getTotalChildCount()
/*      */     {
/*  891 */       if (isExpanded()) {
/*  892 */         FHTreeStateNode localFHTreeStateNode1 = (FHTreeStateNode)getParent();
/*      */         int i;
/*  895 */         if ((localFHTreeStateNode1 != null) && ((i = localFHTreeStateNode1.getIndex(this)) + 1 < localFHTreeStateNode1.getChildCount()))
/*      */         {
/*  899 */           FHTreeStateNode localFHTreeStateNode2 = (FHTreeStateNode)localFHTreeStateNode1.getChildAt(i + 1);
/*      */ 
/*  902 */           return localFHTreeStateNode2.row - this.row - (localFHTreeStateNode2.childIndex - this.childIndex);
/*      */         }
/*      */ 
/*  906 */         int j = this.childCount;
/*      */ 
/*  908 */         for (int k = getChildCount() - 1; k >= 0; 
/*  909 */           k--) {
/*  910 */           j += ((FHTreeStateNode)getChildAt(k)).getTotalChildCount();
/*      */         }
/*      */ 
/*  913 */         return j;
/*      */       }
/*      */ 
/*  916 */       return 0;
/*      */     }
/*      */ 
/*      */     public boolean isExpanded()
/*      */     {
/*  923 */       return this.isExpanded;
/*      */     }
/*      */ 
/*      */     public int getVisibleLevel()
/*      */     {
/*  930 */       if (FixedHeightLayoutCache.this.isRootVisible()) {
/*  931 */         return getLevel();
/*      */       }
/*  933 */       return getLevel() - 1;
/*      */     }
/*      */ 
/*      */     protected void resetChildrenPaths(TreePath paramTreePath)
/*      */     {
/*  941 */       FixedHeightLayoutCache.this.removeMapping(this);
/*  942 */       if (paramTreePath == null)
/*  943 */         this.path = new TreePath(getUserObject());
/*      */       else
/*  945 */         this.path = paramTreePath.pathByAddingChild(getUserObject());
/*  946 */       FixedHeightLayoutCache.this.addMapping(this);
/*  947 */       for (int i = getChildCount() - 1; i >= 0; i--)
/*  948 */         ((FHTreeStateNode)getChildAt(i)).resetChildrenPaths(this.path);
/*      */     }
/*      */ 
/*      */     protected void removeFromMapping()
/*      */     {
/*  957 */       if (this.path != null) {
/*  958 */         FixedHeightLayoutCache.this.removeMapping(this);
/*  959 */         for (int i = getChildCount() - 1; i >= 0; i--)
/*  960 */           ((FHTreeStateNode)getChildAt(i)).removeFromMapping();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected FHTreeStateNode createChildFor(Object paramObject)
/*      */     {
/*  970 */       int i = FixedHeightLayoutCache.this.treeModel.getIndexOfChild(getUserObject(), paramObject);
/*      */ 
/*  973 */       if (i < 0) {
/*  974 */         return null;
/*      */       }
/*      */ 
/*  977 */       FHTreeStateNode localFHTreeStateNode2 = FixedHeightLayoutCache.this.createNodeForValue(paramObject, i);
/*      */       int j;
/*  981 */       if (isVisible()) {
/*  982 */         j = getRowToModelIndex(i);
/*      */       }
/*      */       else {
/*  985 */         j = -1;
/*      */       }
/*  987 */       localFHTreeStateNode2.row = j;
/*  988 */       int k = 0; int m = getChildCount();
/*  989 */       for (; k < m; k++) {
/*  990 */         FHTreeStateNode localFHTreeStateNode1 = (FHTreeStateNode)getChildAt(k);
/*  991 */         if (localFHTreeStateNode1.childIndex > i) {
/*  992 */           insert(localFHTreeStateNode2, k);
/*  993 */           return localFHTreeStateNode2;
/*      */         }
/*      */       }
/*  996 */       add(localFHTreeStateNode2);
/*  997 */       return localFHTreeStateNode2;
/*      */     }
/*      */ 
/*      */     protected void adjustRowBy(int paramInt)
/*      */     {
/* 1005 */       this.row += paramInt;
/* 1006 */       if (this.isExpanded)
/* 1007 */         for (int i = getChildCount() - 1; i >= 0; 
/* 1008 */           i--)
/* 1009 */           ((FHTreeStateNode)getChildAt(i)).adjustRowBy(paramInt);
/*      */     }
/*      */ 
/*      */     protected void adjustRowBy(int paramInt1, int paramInt2)
/*      */     {
/* 1021 */       if (this.isExpanded)
/*      */       {
/* 1023 */         for (int i = getChildCount() - 1; i >= paramInt2; 
/* 1024 */           i--) {
/* 1025 */           ((FHTreeStateNode)getChildAt(i)).adjustRowBy(paramInt1);
/*      */         }
/*      */       }
/* 1028 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/*      */ 
/* 1030 */       if (localFHTreeStateNode != null)
/* 1031 */         localFHTreeStateNode.adjustRowBy(paramInt1, localFHTreeStateNode.getIndex(this) + 1);
/*      */     }
/*      */ 
/*      */     protected void didExpand()
/*      */     {
/* 1040 */       int i = setRowAndChildren(this.row);
/* 1041 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/* 1042 */       int j = i - this.row - 1;
/*      */ 
/* 1044 */       if (localFHTreeStateNode != null) {
/* 1045 */         localFHTreeStateNode.adjustRowBy(j, localFHTreeStateNode.getIndex(this) + 1);
/*      */       }
/* 1047 */       FixedHeightLayoutCache.this.adjustRowCountBy(j);
/*      */     }
/*      */ 
/*      */     protected int setRowAndChildren(int paramInt)
/*      */     {
/* 1056 */       this.row = paramInt;
/*      */ 
/* 1058 */       if (!isExpanded()) {
/* 1059 */         return this.row + 1;
/*      */       }
/* 1061 */       int i = this.row + 1;
/* 1062 */       int j = 0;
/*      */ 
/* 1064 */       int k = getChildCount();
/*      */ 
/* 1066 */       for (int m = 0; m < k; m++) {
/* 1067 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(m);
/* 1068 */         i += localFHTreeStateNode.childIndex - j;
/* 1069 */         j = localFHTreeStateNode.childIndex + 1;
/* 1070 */         if (localFHTreeStateNode.isExpanded) {
/* 1071 */           i = localFHTreeStateNode.setRowAndChildren(i);
/*      */         }
/*      */         else {
/* 1074 */           localFHTreeStateNode.row = (i++);
/*      */         }
/*      */       }
/* 1077 */       return i + this.childCount - j;
/*      */     }
/*      */ 
/*      */     protected void resetChildrenRowsFrom(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 1094 */       int i = paramInt1;
/* 1095 */       int j = paramInt3;
/*      */ 
/* 1097 */       int k = getChildCount();
/*      */ 
/* 1099 */       for (int m = paramInt2; m < k; m++) {
/* 1100 */         localFHTreeStateNode = (FHTreeStateNode)getChildAt(m);
/* 1101 */         i += localFHTreeStateNode.childIndex - j;
/* 1102 */         j = localFHTreeStateNode.childIndex + 1;
/* 1103 */         if (localFHTreeStateNode.isExpanded) {
/* 1104 */           i = localFHTreeStateNode.setRowAndChildren(i);
/*      */         }
/*      */         else {
/* 1107 */           localFHTreeStateNode.row = (i++);
/*      */         }
/*      */       }
/* 1110 */       i += this.childCount - j;
/* 1111 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/* 1112 */       if (localFHTreeStateNode != null) {
/* 1113 */         localFHTreeStateNode.resetChildrenRowsFrom(i, localFHTreeStateNode.getIndex(this) + 1, this.childIndex + 1);
/*      */       }
/*      */       else
/*      */       {
/* 1117 */         FixedHeightLayoutCache.this.rowCount = i;
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void makeVisible()
/*      */     {
/* 1126 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/*      */ 
/* 1128 */       if (localFHTreeStateNode != null)
/* 1129 */         localFHTreeStateNode.expandParentAndReceiver();
/*      */     }
/*      */ 
/*      */     protected void expandParentAndReceiver()
/*      */     {
/* 1137 */       FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getParent();
/*      */ 
/* 1139 */       if (localFHTreeStateNode != null)
/* 1140 */         localFHTreeStateNode.expandParentAndReceiver();
/* 1141 */       expand();
/*      */     }
/*      */ 
/*      */     protected void expand()
/*      */     {
/* 1148 */       if ((!this.isExpanded) && (!isLeaf())) {
/* 1149 */         boolean bool = isVisible();
/*      */ 
/* 1151 */         this.isExpanded = true;
/* 1152 */         this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(getUserObject());
/*      */ 
/* 1154 */         if (bool) {
/* 1155 */           didExpand();
/*      */         }
/*      */ 
/* 1159 */         if ((bool) && (FixedHeightLayoutCache.this.treeSelectionModel != null))
/* 1160 */           FixedHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void collapse(boolean paramBoolean)
/*      */     {
/* 1170 */       if (this.isExpanded) {
/* 1171 */         if ((isVisible()) && (paramBoolean)) {
/* 1172 */           int i = getTotalChildCount();
/*      */ 
/* 1174 */           this.isExpanded = false;
/* 1175 */           FixedHeightLayoutCache.this.adjustRowCountBy(-i);
/*      */ 
/* 1178 */           adjustRowBy(-i, 0);
/*      */         }
/*      */         else {
/* 1181 */           this.isExpanded = false;
/*      */         }
/* 1183 */         if ((paramBoolean) && (isVisible()) && (FixedHeightLayoutCache.this.treeSelectionModel != null))
/* 1184 */           FixedHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 1192 */       TreeModel localTreeModel = FixedHeightLayoutCache.this.getModel();
/*      */ 
/* 1194 */       return localTreeModel != null ? localTreeModel.isLeaf(getUserObject()) : true;
/*      */     }
/*      */ 
/*      */     protected void addNode(FHTreeStateNode paramFHTreeStateNode)
/*      */     {
/* 1203 */       int i = 0;
/* 1204 */       int j = paramFHTreeStateNode.getChildIndex();
/*      */ 
/* 1206 */       int k = 0; int m = getChildCount();
/* 1207 */       for (; k < m; k++) {
/* 1208 */         if (((FHTreeStateNode)getChildAt(k)).getChildIndex() > j)
/*      */         {
/* 1210 */           i = 1;
/* 1211 */           insert(paramFHTreeStateNode, k);
/* 1212 */           k = m;
/*      */         }
/*      */       }
/* 1215 */       if (i == 0)
/* 1216 */         add(paramFHTreeStateNode);
/*      */     }
/*      */ 
/*      */     protected void removeChildAtModelIndex(int paramInt, boolean paramBoolean)
/*      */     {
/* 1226 */       FHTreeStateNode localFHTreeStateNode1 = getChildAtModelIndex(paramInt);
/*      */       int i;
/* 1228 */       if (localFHTreeStateNode1 != null) {
/* 1229 */         i = localFHTreeStateNode1.getRow();
/* 1230 */         int j = getIndex(localFHTreeStateNode1);
/*      */ 
/* 1232 */         localFHTreeStateNode1.collapse(false);
/* 1233 */         remove(j);
/* 1234 */         adjustChildIndexs(j, -1);
/* 1235 */         this.childCount -= 1;
/* 1236 */         if (paramBoolean)
/*      */         {
/* 1238 */           resetChildrenRowsFrom(i, j, paramInt);
/*      */         }
/*      */       }
/*      */       else {
/* 1242 */         i = getChildCount();
/*      */ 
/* 1245 */         for (int k = 0; k < i; k++) {
/* 1246 */           FHTreeStateNode localFHTreeStateNode2 = (FHTreeStateNode)getChildAt(k);
/* 1247 */           if (localFHTreeStateNode2.childIndex >= paramInt) {
/* 1248 */             if (paramBoolean) {
/* 1249 */               adjustRowBy(-1, k);
/* 1250 */               FixedHeightLayoutCache.this.adjustRowCountBy(-1);
/*      */             }
/*      */ 
/* 1255 */             for (; k < i; k++) {
/* 1256 */               ((FHTreeStateNode)getChildAt(k)).childIndex -= 1;
/*      */             }
/* 1258 */             this.childCount -= 1;
/* 1259 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1264 */         if (paramBoolean) {
/* 1265 */           adjustRowBy(-1, i);
/* 1266 */           FixedHeightLayoutCache.this.adjustRowCountBy(-1);
/*      */         }
/* 1268 */         this.childCount -= 1;
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void adjustChildIndexs(int paramInt1, int paramInt2)
/*      */     {
/* 1277 */       int i = paramInt1; int j = getChildCount();
/* 1278 */       for (; i < j; i++)
/* 1279 */         ((FHTreeStateNode)getChildAt(i)).childIndex += paramInt2;
/*      */     }
/*      */ 
/*      */     protected void childInsertedAtModelIndex(int paramInt, boolean paramBoolean)
/*      */     {
/* 1291 */       int i = getChildCount();
/*      */ 
/* 1293 */       for (int j = 0; j < i; j++) {
/* 1294 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(j);
/* 1295 */         if (localFHTreeStateNode.childIndex >= paramInt) {
/* 1296 */           if (paramBoolean) {
/* 1297 */             adjustRowBy(1, j);
/* 1298 */             FixedHeightLayoutCache.this.adjustRowCountBy(1);
/*      */           }
/*      */ 
/* 1302 */           for (; j < i; j++)
/* 1303 */             ((FHTreeStateNode)getChildAt(j)).childIndex += 1;
/* 1304 */           this.childCount += 1;
/* 1305 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1310 */       if (paramBoolean) {
/* 1311 */         adjustRowBy(1, i);
/* 1312 */         FixedHeightLayoutCache.this.adjustRowCountBy(1);
/*      */       }
/* 1314 */       this.childCount += 1;
/*      */     }
/*      */ 
/*      */     protected boolean getPathForRow(int paramInt1, int paramInt2, FixedHeightLayoutCache.SearchInfo paramSearchInfo)
/*      */     {
/* 1326 */       if (this.row == paramInt1) {
/* 1327 */         paramSearchInfo.node = this;
/* 1328 */         paramSearchInfo.isNodeParentNode = false;
/* 1329 */         paramSearchInfo.childIndex = this.childIndex;
/* 1330 */         return true;
/*      */       }
/*      */ 
/* 1334 */       Object localObject = null;
/*      */ 
/* 1336 */       int i = 0; int j = getChildCount();
/* 1337 */       for (; i < j; i++) {
/* 1338 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(i);
/* 1339 */         if (localFHTreeStateNode.row > paramInt1) {
/* 1340 */           if (i == 0)
/*      */           {
/* 1342 */             paramSearchInfo.node = this;
/* 1343 */             paramSearchInfo.isNodeParentNode = true;
/* 1344 */             paramSearchInfo.childIndex = (paramInt1 - this.row - 1);
/* 1345 */             return true;
/*      */           }
/*      */ 
/* 1349 */           int k = 1 + localFHTreeStateNode.row - (localFHTreeStateNode.childIndex - localObject.childIndex);
/*      */ 
/* 1352 */           if (paramInt1 < k) {
/* 1353 */             return localObject.getPathForRow(paramInt1, k, paramSearchInfo);
/*      */           }
/*      */ 
/* 1357 */           paramSearchInfo.node = this;
/* 1358 */           paramSearchInfo.isNodeParentNode = true;
/* 1359 */           paramSearchInfo.childIndex = (paramInt1 - k + localObject.childIndex + 1);
/*      */ 
/* 1361 */           return true;
/*      */         }
/*      */ 
/* 1364 */         localObject = localFHTreeStateNode;
/*      */       }
/*      */ 
/* 1369 */       if (localObject != null) {
/* 1370 */         i = paramInt2 - (this.childCount - localObject.childIndex) + 1;
/*      */ 
/* 1373 */         if (paramInt1 < i) {
/* 1374 */           return localObject.getPathForRow(paramInt1, i, paramSearchInfo);
/*      */         }
/*      */ 
/* 1377 */         paramSearchInfo.node = this;
/* 1378 */         paramSearchInfo.isNodeParentNode = true;
/* 1379 */         paramSearchInfo.childIndex = (paramInt1 - i + localObject.childIndex + 1);
/*      */ 
/* 1381 */         return true;
/*      */       }
/*      */ 
/* 1385 */       i = paramInt1 - this.row - 1;
/*      */ 
/* 1387 */       if (i >= this.childCount) {
/* 1388 */         return false;
/*      */       }
/* 1390 */       paramSearchInfo.node = this;
/* 1391 */       paramSearchInfo.isNodeParentNode = true;
/* 1392 */       paramSearchInfo.childIndex = i;
/* 1393 */       return true;
/*      */     }
/*      */ 
/*      */     protected int getCountTo(int paramInt)
/*      */     {
/* 1403 */       int i = paramInt + 1;
/*      */ 
/* 1405 */       int j = 0; int k = getChildCount();
/* 1406 */       for (; j < k; j++) {
/* 1407 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(j);
/* 1408 */         if (localFHTreeStateNode.childIndex >= paramInt)
/* 1409 */           j = k;
/*      */         else
/* 1411 */           i += localFHTreeStateNode.getTotalChildCount();
/*      */       }
/* 1413 */       if (this.parent != null) {
/* 1414 */         return i + ((FHTreeStateNode)getParent()).getCountTo(this.childIndex);
/*      */       }
/* 1416 */       if (!FixedHeightLayoutCache.this.isRootVisible())
/* 1417 */         return i - 1;
/* 1418 */       return i;
/*      */     }
/*      */ 
/*      */     protected int getNumExpandedChildrenTo(int paramInt)
/*      */     {
/* 1429 */       int i = paramInt;
/*      */ 
/* 1431 */       int j = 0; int k = getChildCount();
/* 1432 */       for (; j < k; j++) {
/* 1433 */         FHTreeStateNode localFHTreeStateNode = (FHTreeStateNode)getChildAt(j);
/* 1434 */         if (localFHTreeStateNode.childIndex >= paramInt) {
/* 1435 */           return i;
/*      */         }
/* 1437 */         i += localFHTreeStateNode.getTotalChildCount();
/*      */       }
/*      */ 
/* 1440 */       return i;
/*      */     }
/*      */ 
/*      */     protected void didAdjustTree()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SearchInfo
/*      */   {
/*      */     protected FixedHeightLayoutCache.FHTreeStateNode node;
/*      */     protected boolean isNodeParentNode;
/*      */     protected int childIndex;
/*      */ 
/*      */     private SearchInfo()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected TreePath getPath() {
/* 1461 */       if (this.node == null) {
/* 1462 */         return null;
/*      */       }
/* 1464 */       if (this.isNodeParentNode) {
/* 1465 */         return this.node.getTreePath().pathByAddingChild(FixedHeightLayoutCache.this.treeModel.getChild(this.node.getUserObject(), this.childIndex));
/*      */       }
/*      */ 
/* 1468 */       return this.node.path;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class VisibleFHTreeStateNodeEnumeration
/*      */     implements Enumeration<TreePath>
/*      */   {
/*      */     protected FixedHeightLayoutCache.FHTreeStateNode parent;
/*      */     protected int nextIndex;
/*      */     protected int childCount;
/*      */ 
/*      */     protected VisibleFHTreeStateNodeEnumeration(FixedHeightLayoutCache.FHTreeStateNode arg2)
/*      */     {
/* 1490 */       this(localFHTreeStateNode, -1);
/*      */     }
/*      */ 
/*      */     protected VisibleFHTreeStateNodeEnumeration(FixedHeightLayoutCache.FHTreeStateNode paramInt, int arg3)
/*      */     {
/* 1495 */       this.parent = paramInt;
/*      */       int i;
/* 1496 */       this.nextIndex = i;
/* 1497 */       this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(this.parent.getUserObject());
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements()
/*      */     {
/* 1505 */       return this.parent != null;
/*      */     }
/*      */ 
/*      */     public TreePath nextElement()
/*      */     {
/* 1512 */       if (!hasMoreElements())
/* 1513 */         throw new NoSuchElementException("No more visible paths");
/*      */       TreePath localTreePath;
/* 1517 */       if (this.nextIndex == -1) {
/* 1518 */         localTreePath = this.parent.getTreePath();
/*      */       } else {
/* 1520 */         FixedHeightLayoutCache.FHTreeStateNode localFHTreeStateNode = this.parent.getChildAtModelIndex(this.nextIndex);
/*      */ 
/* 1522 */         if (localFHTreeStateNode == null) {
/* 1523 */           localTreePath = this.parent.getTreePath().pathByAddingChild(FixedHeightLayoutCache.this.treeModel.getChild(this.parent.getUserObject(), this.nextIndex));
/*      */         }
/*      */         else
/*      */         {
/* 1527 */           localTreePath = localFHTreeStateNode.getTreePath();
/*      */         }
/*      */       }
/* 1529 */       updateNextObject();
/* 1530 */       return localTreePath;
/*      */     }
/*      */ 
/*      */     protected void updateNextObject()
/*      */     {
/* 1538 */       if (!updateNextIndex())
/* 1539 */         findNextValidParent();
/*      */     }
/*      */ 
/*      */     protected boolean findNextValidParent()
/*      */     {
/* 1548 */       if (this.parent == FixedHeightLayoutCache.this.root)
/*      */       {
/* 1550 */         this.parent = null;
/* 1551 */         return false;
/*      */       }
/* 1553 */       while (this.parent != null) {
/* 1554 */         FixedHeightLayoutCache.FHTreeStateNode localFHTreeStateNode = (FixedHeightLayoutCache.FHTreeStateNode)this.parent.getParent();
/*      */ 
/* 1557 */         if (localFHTreeStateNode != null) {
/* 1558 */           this.nextIndex = this.parent.childIndex;
/* 1559 */           this.parent = localFHTreeStateNode;
/* 1560 */           this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(this.parent.getUserObject());
/*      */ 
/* 1562 */           if (updateNextIndex())
/* 1563 */             return true;
/*      */         }
/*      */         else {
/* 1566 */           this.parent = null;
/*      */         }
/*      */       }
/* 1568 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean updateNextIndex()
/*      */     {
/* 1578 */       if ((this.nextIndex == -1) && (!this.parent.isExpanded())) {
/* 1579 */         return false;
/*      */       }
/*      */ 
/* 1583 */       if (this.childCount == 0) {
/* 1584 */         return false;
/*      */       }
/*      */ 
/* 1587 */       if (++this.nextIndex >= this.childCount) {
/* 1588 */         return false;
/*      */       }
/*      */ 
/* 1591 */       FixedHeightLayoutCache.FHTreeStateNode localFHTreeStateNode = this.parent.getChildAtModelIndex(this.nextIndex);
/*      */ 
/* 1593 */       if ((localFHTreeStateNode != null) && (localFHTreeStateNode.isExpanded())) {
/* 1594 */         this.parent = localFHTreeStateNode;
/* 1595 */         this.nextIndex = -1;
/* 1596 */         this.childCount = FixedHeightLayoutCache.this.treeModel.getChildCount(localFHTreeStateNode.getUserObject());
/*      */       }
/* 1598 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.FixedHeightLayoutCache
 * JD-Core Version:    0.6.2
 */