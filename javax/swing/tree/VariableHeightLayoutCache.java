/*      */ package javax.swing.tree;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ 
/*      */ public class VariableHeightLayoutCache extends AbstractLayoutCache
/*      */ {
/*      */   private Vector<Object> visibleNodes;
/*      */   private boolean updateNodeSizes;
/*      */   private TreeStateNode root;
/*      */   private Rectangle boundsBuffer;
/*      */   private Hashtable<TreePath, TreeStateNode> treePathMapping;
/*      */   private Stack<Stack<TreePath>> tempStacks;
/*      */ 
/*      */   public VariableHeightLayoutCache()
/*      */   {
/*   92 */     this.tempStacks = new Stack();
/*   93 */     this.visibleNodes = new Vector();
/*   94 */     this.boundsBuffer = new Rectangle();
/*   95 */     this.treePathMapping = new Hashtable();
/*      */   }
/*      */ 
/*      */   public void setModel(TreeModel paramTreeModel)
/*      */   {
/*  107 */     super.setModel(paramTreeModel);
/*  108 */     rebuild(false);
/*      */   }
/*      */ 
/*      */   public void setRootVisible(boolean paramBoolean)
/*      */   {
/*  123 */     if ((isRootVisible() != paramBoolean) && (this.root != null)) {
/*  124 */       if (paramBoolean) {
/*  125 */         this.root.updatePreferredSize(0);
/*  126 */         this.visibleNodes.insertElementAt(this.root, 0);
/*      */       }
/*  128 */       else if (this.visibleNodes.size() > 0) {
/*  129 */         this.visibleNodes.removeElementAt(0);
/*  130 */         if (this.treeSelectionModel != null) {
/*  131 */           this.treeSelectionModel.removeSelectionPath(this.root.getTreePath());
/*      */         }
/*      */       }
/*  134 */       if (this.treeSelectionModel != null)
/*  135 */         this.treeSelectionModel.resetRowSelection();
/*  136 */       if (getRowCount() > 0)
/*  137 */         getNode(0).setYOrigin(0);
/*  138 */       updateYLocationsFrom(0);
/*  139 */       visibleNodesChanged();
/*      */     }
/*  141 */     super.setRootVisible(paramBoolean);
/*      */   }
/*      */ 
/*      */   public void setRowHeight(int paramInt)
/*      */   {
/*  155 */     if (paramInt != getRowHeight()) {
/*  156 */       super.setRowHeight(paramInt);
/*  157 */       invalidateSizes();
/*  158 */       visibleNodesChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setNodeDimensions(AbstractLayoutCache.NodeDimensions paramNodeDimensions)
/*      */   {
/*  167 */     super.setNodeDimensions(paramNodeDimensions);
/*  168 */     invalidateSizes();
/*  169 */     visibleNodesChanged();
/*      */   }
/*      */ 
/*      */   public void setExpandedState(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/*  179 */     if (paramTreePath != null)
/*  180 */       if (paramBoolean) {
/*  181 */         ensurePathIsExpanded(paramTreePath, true);
/*      */       } else {
/*  183 */         TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, false, true);
/*      */ 
/*  185 */         if (localTreeStateNode != null) {
/*  186 */           localTreeStateNode.makeVisible();
/*  187 */           localTreeStateNode.collapse();
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean getExpandedState(TreePath paramTreePath)
/*      */   {
/*  198 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  200 */     return (localTreeStateNode.isVisible()) && (localTreeStateNode.isExpanded());
/*      */   }
/*      */ 
/*      */   public Rectangle getBounds(TreePath paramTreePath, Rectangle paramRectangle)
/*      */   {
/*  214 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  216 */     if (localTreeStateNode != null) {
/*  217 */       if (this.updateNodeSizes)
/*  218 */         updateNodeSizes(false);
/*  219 */       return localTreeStateNode.getNodeBounds(paramRectangle);
/*      */     }
/*  221 */     return null;
/*      */   }
/*      */ 
/*      */   public TreePath getPathForRow(int paramInt)
/*      */   {
/*  233 */     if ((paramInt >= 0) && (paramInt < getRowCount())) {
/*  234 */       return getNode(paramInt).getTreePath();
/*      */     }
/*  236 */     return null;
/*      */   }
/*      */ 
/*      */   public int getRowForPath(TreePath paramTreePath)
/*      */   {
/*  248 */     if (paramTreePath == null) {
/*  249 */       return -1;
/*      */     }
/*  251 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  253 */     if (localTreeStateNode != null)
/*  254 */       return localTreeStateNode.getRow();
/*  255 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getRowCount()
/*      */   {
/*  263 */     return this.visibleNodes.size();
/*      */   }
/*      */ 
/*      */   public void invalidatePathBounds(TreePath paramTreePath)
/*      */   {
/*  273 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  275 */     if (localTreeStateNode != null) {
/*  276 */       localTreeStateNode.markSizeInvalid();
/*  277 */       if (localTreeStateNode.isVisible())
/*  278 */         updateYLocationsFrom(localTreeStateNode.getRow());
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getPreferredHeight()
/*      */   {
/*  288 */     int i = getRowCount();
/*      */ 
/*  290 */     if (i > 0) {
/*  291 */       TreeStateNode localTreeStateNode = getNode(i - 1);
/*      */ 
/*  293 */       return localTreeStateNode.getYOrigin() + localTreeStateNode.getPreferredHeight();
/*      */     }
/*  295 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getPreferredWidth(Rectangle paramRectangle)
/*      */   {
/*  305 */     if (this.updateNodeSizes) {
/*  306 */       updateNodeSizes(false);
/*      */     }
/*  308 */     return getMaxNodeWidth();
/*      */   }
/*      */ 
/*      */   public TreePath getPathClosestTo(int paramInt1, int paramInt2)
/*      */   {
/*  324 */     if (getRowCount() == 0) {
/*  325 */       return null;
/*      */     }
/*  327 */     if (this.updateNodeSizes) {
/*  328 */       updateNodeSizes(false);
/*      */     }
/*  330 */     int i = getRowContainingYLocation(paramInt2);
/*      */ 
/*  332 */     return getNode(i).getTreePath();
/*      */   }
/*      */ 
/*      */   public Enumeration<TreePath> getVisiblePathsFrom(TreePath paramTreePath)
/*      */   {
/*  345 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  347 */     if (localTreeStateNode != null) {
/*  348 */       return new VisibleTreeStateNodeEnumeration(localTreeStateNode);
/*      */     }
/*  350 */     return null;
/*      */   }
/*      */ 
/*      */   public int getVisibleChildCount(TreePath paramTreePath)
/*      */   {
/*  358 */     TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  360 */     return localTreeStateNode != null ? localTreeStateNode.getVisibleChildCount() : 0;
/*      */   }
/*      */ 
/*      */   public void invalidateSizes()
/*      */   {
/*  368 */     if (this.root != null)
/*  369 */       this.root.deepMarkSizeInvalid();
/*  370 */     if ((!isFixedRowHeight()) && (this.visibleNodes.size() > 0))
/*  371 */       updateNodeSizes(true);
/*      */   }
/*      */ 
/*      */   public boolean isExpanded(TreePath paramTreePath)
/*      */   {
/*  382 */     if (paramTreePath != null) {
/*  383 */       TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, true, false);
/*      */ 
/*  385 */       return (localTreeStateNode != null) && (localTreeStateNode.isExpanded());
/*      */     }
/*  387 */     return false;
/*      */   }
/*      */ 
/*      */   public void treeNodesChanged(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  411 */     if (paramTreeModelEvent != null)
/*      */     {
/*  415 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*  416 */       TreeStateNode localTreeStateNode1 = getNodeForPath(paramTreeModelEvent.getTreePath(), false, false);
/*  417 */       if (localTreeStateNode1 != null) {
/*  418 */         Object localObject = localTreeStateNode1.getValue();
/*      */ 
/*  422 */         localTreeStateNode1.updatePreferredSize();
/*      */         int i;
/*  423 */         if ((localTreeStateNode1.hasBeenExpanded()) && (arrayOfInt != null))
/*      */         {
/*  427 */           for (i = 0; i < arrayOfInt.length; 
/*  428 */             i++) {
/*  429 */             TreeStateNode localTreeStateNode2 = (TreeStateNode)localTreeStateNode1.getChildAt(arrayOfInt[i]);
/*      */ 
/*  432 */             localTreeStateNode2.setUserObject(this.treeModel.getChild(localObject, arrayOfInt[i]));
/*      */ 
/*  435 */             localTreeStateNode2.updatePreferredSize();
/*      */           }
/*      */         }
/*  438 */         else if (localTreeStateNode1 == this.root)
/*      */         {
/*  440 */           localTreeStateNode1.updatePreferredSize();
/*      */         }
/*  442 */         if (!isFixedRowHeight()) {
/*  443 */           i = localTreeStateNode1.getRow();
/*      */ 
/*  445 */           if (i != -1)
/*  446 */             updateYLocationsFrom(i);
/*      */         }
/*  448 */         visibleNodesChanged();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeNodesInserted(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  464 */     if (paramTreeModelEvent != null)
/*      */     {
/*  468 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*  469 */       TreeStateNode localTreeStateNode1 = getNodeForPath(paramTreeModelEvent.getTreePath(), false, false);
/*      */ 
/*  473 */       if ((localTreeStateNode1 != null) && (arrayOfInt != null) && (arrayOfInt.length > 0))
/*      */       {
/*  475 */         if (localTreeStateNode1.hasBeenExpanded())
/*      */         {
/*  480 */           int k = localTreeStateNode1.getChildCount();
/*      */ 
/*  483 */           Object localObject = localTreeStateNode1.getValue();
/*  484 */           int i = ((localTreeStateNode1 == this.root) && (!this.rootVisible)) || ((localTreeStateNode1.getRow() != -1) && (localTreeStateNode1.isExpanded())) ? 1 : 0;
/*      */ 
/*  488 */           for (int j = 0; j < arrayOfInt.length; j++)
/*      */           {
/*  490 */             TreeStateNode localTreeStateNode2 = createNodeAt(localTreeStateNode1, arrayOfInt[j]);
/*      */           }
/*      */ 
/*  493 */           if (k == 0)
/*      */           {
/*  495 */             localTreeStateNode1.updatePreferredSize();
/*      */           }
/*  497 */           if (this.treeSelectionModel != null) {
/*  498 */             this.treeSelectionModel.resetRowSelection();
/*      */           }
/*      */ 
/*  501 */           if ((!isFixedRowHeight()) && ((i != 0) || ((k == 0) && (localTreeStateNode1.isVisible()))))
/*      */           {
/*  504 */             if (localTreeStateNode1 == this.root)
/*  505 */               updateYLocationsFrom(0);
/*      */             else {
/*  507 */               updateYLocationsFrom(localTreeStateNode1.getRow());
/*      */             }
/*  509 */             visibleNodesChanged();
/*      */           }
/*  511 */           else if (i != 0) {
/*  512 */             visibleNodesChanged();
/*      */           }
/*  514 */         } else if (this.treeModel.getChildCount(localTreeStateNode1.getValue()) - arrayOfInt.length == 0)
/*      */         {
/*  516 */           localTreeStateNode1.updatePreferredSize();
/*  517 */           if ((!isFixedRowHeight()) && (localTreeStateNode1.isVisible()))
/*  518 */             updateYLocationsFrom(localTreeStateNode1.getRow());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeNodesRemoved(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  538 */     if (paramTreeModelEvent != null)
/*      */     {
/*  542 */       int[] arrayOfInt = paramTreeModelEvent.getChildIndices();
/*  543 */       TreeStateNode localTreeStateNode1 = getNodeForPath(paramTreeModelEvent.getTreePath(), false, false);
/*      */ 
/*  546 */       if ((localTreeStateNode1 != null) && (arrayOfInt != null) && (arrayOfInt.length > 0))
/*      */       {
/*  548 */         if (localTreeStateNode1.hasBeenExpanded())
/*      */         {
/*  554 */           int i = ((localTreeStateNode1 == this.root) && (!this.rootVisible)) || ((localTreeStateNode1.getRow() != -1) && (localTreeStateNode1.isExpanded())) ? 1 : 0;
/*      */ 
/*  558 */           for (int j = arrayOfInt.length - 1; j >= 0; 
/*  559 */             j--) {
/*  560 */             TreeStateNode localTreeStateNode2 = (TreeStateNode)localTreeStateNode1.getChildAt(arrayOfInt[j]);
/*      */ 
/*  562 */             if (localTreeStateNode2.isExpanded()) {
/*  563 */               localTreeStateNode2.collapse(false);
/*      */             }
/*      */ 
/*  567 */             if (i != 0) {
/*  568 */               int k = localTreeStateNode2.getRow();
/*  569 */               if (k != -1) {
/*  570 */                 this.visibleNodes.removeElementAt(k);
/*      */               }
/*      */             }
/*  573 */             localTreeStateNode1.remove(arrayOfInt[j]);
/*      */           }
/*  575 */           if (localTreeStateNode1.getChildCount() == 0)
/*      */           {
/*  577 */             localTreeStateNode1.updatePreferredSize();
/*  578 */             if ((localTreeStateNode1.isExpanded()) && (localTreeStateNode1.isLeaf()))
/*      */             {
/*  581 */               localTreeStateNode1.collapse(false);
/*      */             }
/*      */           }
/*  584 */           if (this.treeSelectionModel != null) {
/*  585 */             this.treeSelectionModel.resetRowSelection();
/*      */           }
/*      */ 
/*  588 */           if ((!isFixedRowHeight()) && ((i != 0) || ((localTreeStateNode1.getChildCount() == 0) && (localTreeStateNode1.isVisible()))))
/*      */           {
/*  591 */             if (localTreeStateNode1 == this.root)
/*      */             {
/*  595 */               if (getRowCount() > 0)
/*  596 */                 getNode(0).setYOrigin(0);
/*  597 */               updateYLocationsFrom(0);
/*      */             }
/*      */             else {
/*  600 */               updateYLocationsFrom(localTreeStateNode1.getRow());
/*  601 */             }visibleNodesChanged();
/*      */           }
/*  603 */           else if (i != 0) {
/*  604 */             visibleNodesChanged();
/*      */           }
/*  606 */         } else if (this.treeModel.getChildCount(localTreeStateNode1.getValue()) == 0)
/*      */         {
/*  608 */           localTreeStateNode1.updatePreferredSize();
/*  609 */           if ((!isFixedRowHeight()) && (localTreeStateNode1.isVisible()))
/*  610 */             updateYLocationsFrom(localTreeStateNode1.getRow());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void treeStructureChanged(TreeModelEvent paramTreeModelEvent)
/*      */   {
/*  629 */     if (paramTreeModelEvent != null)
/*      */     {
/*  631 */       TreePath localTreePath = paramTreeModelEvent.getTreePath();
/*      */ 
/*  634 */       TreeStateNode localTreeStateNode1 = getNodeForPath(localTreePath, false, false);
/*      */ 
/*  638 */       if ((localTreeStateNode1 == this.root) || ((localTreeStateNode1 == null) && (((localTreePath == null) && (this.treeModel != null) && (this.treeModel.getRoot() == null)) || ((localTreePath != null) && (localTreePath.getPathCount() == 1)))))
/*      */       {
/*  643 */         rebuild(true);
/*      */       }
/*  645 */       else if (localTreeStateNode1 != null)
/*      */       {
/*  651 */         boolean bool = localTreeStateNode1.isExpanded();
/*  652 */         int j = localTreeStateNode1.getRow() != -1 ? 1 : 0;
/*      */ 
/*  654 */         TreeStateNode localTreeStateNode3 = (TreeStateNode)localTreeStateNode1.getParent();
/*  655 */         int i = localTreeStateNode3.getIndex(localTreeStateNode1);
/*  656 */         if ((j != 0) && (bool)) {
/*  657 */           localTreeStateNode1.collapse(false);
/*      */         }
/*  659 */         if (j != 0)
/*  660 */           this.visibleNodes.removeElement(localTreeStateNode1);
/*  661 */         localTreeStateNode1.removeFromParent();
/*  662 */         createNodeAt(localTreeStateNode3, i);
/*  663 */         TreeStateNode localTreeStateNode2 = (TreeStateNode)localTreeStateNode3.getChildAt(i);
/*  664 */         if ((j != 0) && (bool))
/*  665 */           localTreeStateNode2.expand(false);
/*  666 */         int k = localTreeStateNode2.getRow();
/*  667 */         if ((!isFixedRowHeight()) && (j != 0)) {
/*  668 */           if (k == 0)
/*  669 */             updateYLocationsFrom(k);
/*      */           else
/*  671 */             updateYLocationsFrom(k - 1);
/*  672 */           visibleNodesChanged();
/*      */         }
/*  674 */         else if (j != 0) {
/*  675 */           visibleNodesChanged();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void visibleNodesChanged()
/*      */   {
/*      */   }
/*      */ 
/*      */   private void addMapping(TreeStateNode paramTreeStateNode)
/*      */   {
/*  692 */     this.treePathMapping.put(paramTreeStateNode.getTreePath(), paramTreeStateNode);
/*      */   }
/*      */ 
/*      */   private void removeMapping(TreeStateNode paramTreeStateNode)
/*      */   {
/*  699 */     this.treePathMapping.remove(paramTreeStateNode.getTreePath());
/*      */   }
/*      */ 
/*      */   private TreeStateNode getMapping(TreePath paramTreePath)
/*      */   {
/*  707 */     return (TreeStateNode)this.treePathMapping.get(paramTreePath);
/*      */   }
/*      */ 
/*      */   private Rectangle getBounds(int paramInt, Rectangle paramRectangle)
/*      */   {
/*  716 */     if (this.updateNodeSizes) {
/*  717 */       updateNodeSizes(false);
/*      */     }
/*  719 */     if ((paramInt >= 0) && (paramInt < getRowCount())) {
/*  720 */       return getNode(paramInt).getNodeBounds(paramRectangle);
/*      */     }
/*  722 */     return null;
/*      */   }
/*      */ 
/*      */   private void rebuild(boolean paramBoolean)
/*      */   {
/*  732 */     this.treePathMapping.clear();
/*      */     Object localObject;
/*  733 */     if ((this.treeModel != null) && ((localObject = this.treeModel.getRoot()) != null)) {
/*  734 */       this.root = createNodeForValue(localObject);
/*  735 */       this.root.path = new TreePath(localObject);
/*  736 */       addMapping(this.root);
/*  737 */       this.root.updatePreferredSize(0);
/*  738 */       this.visibleNodes.removeAllElements();
/*  739 */       if (isRootVisible())
/*  740 */         this.visibleNodes.addElement(this.root);
/*  741 */       if (!this.root.isExpanded()) {
/*  742 */         this.root.expand();
/*      */       } else {
/*  744 */         Enumeration localEnumeration = this.root.children();
/*  745 */         while (localEnumeration.hasMoreElements()) {
/*  746 */           this.visibleNodes.addElement(localEnumeration.nextElement());
/*      */         }
/*  748 */         if (!isFixedRowHeight())
/*  749 */           updateYLocationsFrom(0);
/*      */       }
/*      */     }
/*      */     else {
/*  753 */       this.visibleNodes.removeAllElements();
/*  754 */       this.root = null;
/*      */     }
/*  756 */     if ((paramBoolean) && (this.treeSelectionModel != null)) {
/*  757 */       this.treeSelectionModel.clearSelection();
/*      */     }
/*  759 */     visibleNodesChanged();
/*      */   }
/*      */ 
/*      */   private TreeStateNode createNodeAt(TreeStateNode paramTreeStateNode, int paramInt)
/*      */   {
/*  778 */     Object localObject = this.treeModel.getChild(paramTreeStateNode.getValue(), paramInt);
/*  779 */     TreeStateNode localTreeStateNode1 = createNodeForValue(localObject);
/*  780 */     paramTreeStateNode.insert(localTreeStateNode1, paramInt);
/*  781 */     localTreeStateNode1.updatePreferredSize(-1);
/*  782 */     int i = paramTreeStateNode == this.root ? 1 : 0;
/*  783 */     if ((localTreeStateNode1 != null) && (paramTreeStateNode.isExpanded()) && ((paramTreeStateNode.getRow() != -1) || (i != 0)))
/*      */     {
/*      */       int j;
/*  788 */       if (paramInt == 0) {
/*  789 */         if ((i != 0) && (!isRootVisible()))
/*  790 */           j = 0;
/*      */         else
/*  792 */           j = paramTreeStateNode.getRow() + 1;
/*      */       }
/*  794 */       else if (paramInt == paramTreeStateNode.getChildCount()) {
/*  795 */         j = paramTreeStateNode.getLastVisibleNode().getRow() + 1;
/*      */       }
/*      */       else
/*      */       {
/*  799 */         TreeStateNode localTreeStateNode2 = (TreeStateNode)paramTreeStateNode.getChildAt(paramInt - 1);
/*      */ 
/*  801 */         j = localTreeStateNode2.getLastVisibleNode().getRow() + 1;
/*      */       }
/*  803 */       this.visibleNodes.insertElementAt(localTreeStateNode1, j);
/*      */     }
/*  805 */     return localTreeStateNode1;
/*      */   }
/*      */ 
/*      */   private TreeStateNode getNodeForPath(TreePath paramTreePath, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  816 */     if (paramTreePath != null)
/*      */     {
/*  819 */       TreeStateNode localTreeStateNode1 = getMapping(paramTreePath);
/*  820 */       if (localTreeStateNode1 != null) {
/*  821 */         if ((paramBoolean1) && (!localTreeStateNode1.isVisible()))
/*  822 */           return null;
/*  823 */         return localTreeStateNode1;
/*      */       }
/*      */       Stack localStack;
/*  829 */       if (this.tempStacks.size() == 0) {
/*  830 */         localStack = new Stack();
/*      */       }
/*      */       else {
/*  833 */         localStack = (Stack)this.tempStacks.pop();
/*      */       }
/*      */       try
/*      */       {
/*  837 */         localStack.push(paramTreePath);
/*  838 */         paramTreePath = paramTreePath.getParentPath();
/*  839 */         localTreeStateNode1 = null;
/*  840 */         while (paramTreePath != null) {
/*  841 */           localTreeStateNode1 = getMapping(paramTreePath);
/*  842 */           if (localTreeStateNode1 != null)
/*      */           {
/*  845 */             while ((localTreeStateNode1 != null) && (localStack.size() > 0)) {
/*  846 */               paramTreePath = (TreePath)localStack.pop();
/*  847 */               localTreeStateNode1.getLoadedChildren(paramBoolean2);
/*      */ 
/*  849 */               int i = this.treeModel.getIndexOfChild(localTreeStateNode1.getUserObject(), paramTreePath.getLastPathComponent());
/*      */ 
/*  853 */               if ((i == -1) || (i >= localTreeStateNode1.getChildCount()) || ((paramBoolean1) && (!localTreeStateNode1.isVisible())))
/*      */               {
/*  856 */                 localTreeStateNode1 = null;
/*      */               }
/*      */               else {
/*  859 */                 localTreeStateNode1 = (TreeStateNode)localTreeStateNode1.getChildAt(i);
/*      */               }
/*      */             }
/*  862 */             return localTreeStateNode1;
/*      */           }
/*  864 */           localStack.push(paramTreePath);
/*  865 */           paramTreePath = paramTreePath.getParentPath();
/*      */         }
/*      */       }
/*      */       finally {
/*  869 */         localStack.removeAllElements();
/*  870 */         this.tempStacks.push(localStack);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  875 */     return null;
/*      */   }
/*      */ 
/*      */   private void updateYLocationsFrom(int paramInt)
/*      */   {
/*  883 */     if ((paramInt >= 0) && (paramInt < getRowCount()))
/*      */     {
/*  887 */       TreeStateNode localTreeStateNode = getNode(paramInt);
/*  888 */       int k = localTreeStateNode.getYOrigin() + localTreeStateNode.getPreferredHeight();
/*  889 */       int i = paramInt + 1; int j = this.visibleNodes.size();
/*  890 */       for (; i < j; i++) {
/*  891 */         localTreeStateNode = (TreeStateNode)this.visibleNodes.elementAt(i);
/*      */ 
/*  893 */         localTreeStateNode.setYOrigin(k);
/*  894 */         k += localTreeStateNode.getPreferredHeight();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateNodeSizes(boolean paramBoolean)
/*      */   {
/*  911 */     this.updateNodeSizes = false;
/*      */     int j;
/*  912 */     int i = j = 0; int k = this.visibleNodes.size();
/*  913 */     for (; j < k; j++) {
/*  914 */       TreeStateNode localTreeStateNode = (TreeStateNode)this.visibleNodes.elementAt(j);
/*  915 */       localTreeStateNode.setYOrigin(i);
/*  916 */       if ((paramBoolean) || (!localTreeStateNode.hasValidSize()))
/*  917 */         localTreeStateNode.updatePreferredSize(j);
/*  918 */       i += localTreeStateNode.getPreferredHeight();
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getRowContainingYLocation(int paramInt)
/*      */   {
/*  928 */     if (isFixedRowHeight()) {
/*  929 */       if (getRowCount() == 0)
/*  930 */         return -1;
/*  931 */       return Math.max(0, Math.min(getRowCount() - 1, paramInt / getRowHeight()));
/*      */     }
/*      */     int i;
/*  938 */     if ((i = getRowCount()) <= 0)
/*  939 */       return -1;
/*      */     int m;
/*  940 */     int k = m = 0;
/*  941 */     while (m < i) {
/*  942 */       k = (i - m) / 2 + m;
/*  943 */       TreeStateNode localTreeStateNode = (TreeStateNode)this.visibleNodes.elementAt(k);
/*  944 */       int n = localTreeStateNode.getYOrigin();
/*  945 */       int j = n + localTreeStateNode.getPreferredHeight();
/*  946 */       if (paramInt < n) {
/*  947 */         i = k - 1;
/*      */       } else {
/*  949 */         if (paramInt < j) break;
/*  950 */         m = k + 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  955 */     if (m == i) {
/*  956 */       k = m;
/*  957 */       if (k >= getRowCount())
/*  958 */         k = getRowCount() - 1;
/*      */     }
/*  960 */     return k;
/*      */   }
/*      */ 
/*      */   private void ensurePathIsExpanded(TreePath paramTreePath, boolean paramBoolean)
/*      */   {
/*  970 */     if (paramTreePath != null)
/*      */     {
/*  972 */       if (this.treeModel.isLeaf(paramTreePath.getLastPathComponent())) {
/*  973 */         paramTreePath = paramTreePath.getParentPath();
/*  974 */         paramBoolean = true;
/*      */       }
/*  976 */       if (paramTreePath != null) {
/*  977 */         TreeStateNode localTreeStateNode = getNodeForPath(paramTreePath, false, true);
/*      */ 
/*  980 */         if (localTreeStateNode != null) {
/*  981 */           localTreeStateNode.makeVisible();
/*  982 */           if (paramBoolean)
/*  983 */             localTreeStateNode.expand();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private TreeStateNode getNode(int paramInt)
/*      */   {
/*  993 */     return (TreeStateNode)this.visibleNodes.elementAt(paramInt);
/*      */   }
/*      */ 
/*      */   private int getMaxNodeWidth()
/*      */   {
/* 1000 */     int i = 0;
/*      */ 
/* 1005 */     for (int k = getRowCount() - 1; k >= 0; k--) {
/* 1006 */       TreeStateNode localTreeStateNode = getNode(k);
/* 1007 */       int j = localTreeStateNode.getPreferredWidth() + localTreeStateNode.getXOrigin();
/* 1008 */       if (j > i)
/* 1009 */         i = j;
/*      */     }
/* 1011 */     return i;
/*      */   }
/*      */ 
/*      */   private TreeStateNode createNodeForValue(Object paramObject)
/*      */   {
/* 1019 */     return new TreeStateNode(paramObject);
/*      */   }
/*      */ 
/*      */   private class TreeStateNode extends DefaultMutableTreeNode
/*      */   {
/*      */     protected int preferredWidth;
/*      */     protected int preferredHeight;
/*      */     protected int xOrigin;
/*      */     protected int yOrigin;
/*      */     protected boolean expanded;
/*      */     protected boolean hasBeenExpanded;
/*      */     protected TreePath path;
/*      */ 
/*      */     public TreeStateNode(Object arg2)
/*      */     {
/* 1050 */       super();
/*      */     }
/*      */ 
/*      */     public void setParent(MutableTreeNode paramMutableTreeNode)
/*      */     {
/* 1062 */       super.setParent(paramMutableTreeNode);
/* 1063 */       if (paramMutableTreeNode != null) {
/* 1064 */         this.path = ((TreeStateNode)paramMutableTreeNode).getTreePath().pathByAddingChild(getUserObject());
/*      */ 
/* 1066 */         VariableHeightLayoutCache.this.addMapping(this);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove(int paramInt)
/*      */     {
/* 1075 */       TreeStateNode localTreeStateNode = (TreeStateNode)getChildAt(paramInt);
/*      */ 
/* 1077 */       localTreeStateNode.removeFromMapping();
/* 1078 */       super.remove(paramInt);
/*      */     }
/*      */ 
/*      */     public void setUserObject(Object paramObject)
/*      */     {
/* 1085 */       super.setUserObject(paramObject);
/* 1086 */       if (this.path != null) {
/* 1087 */         TreeStateNode localTreeStateNode = (TreeStateNode)getParent();
/*      */ 
/* 1089 */         if (localTreeStateNode != null)
/* 1090 */           resetChildrenPaths(localTreeStateNode.getTreePath());
/*      */         else
/* 1092 */           resetChildrenPaths(null);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Enumeration children()
/*      */     {
/* 1102 */       if (!isExpanded()) {
/* 1103 */         return DefaultMutableTreeNode.EMPTY_ENUMERATION;
/*      */       }
/* 1105 */       return super.children();
/*      */     }
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 1113 */       return VariableHeightLayoutCache.this.getModel().isLeaf(getValue());
/*      */     }
/*      */ 
/*      */     public Rectangle getNodeBounds(Rectangle paramRectangle)
/*      */     {
/* 1124 */       if (paramRectangle == null) {
/* 1125 */         paramRectangle = new Rectangle(getXOrigin(), getYOrigin(), getPreferredWidth(), getPreferredHeight());
/*      */       }
/*      */       else
/*      */       {
/* 1129 */         paramRectangle.x = getXOrigin();
/* 1130 */         paramRectangle.y = getYOrigin();
/* 1131 */         paramRectangle.width = getPreferredWidth();
/* 1132 */         paramRectangle.height = getPreferredHeight();
/*      */       }
/* 1134 */       return paramRectangle;
/*      */     }
/*      */ 
/*      */     public int getXOrigin()
/*      */     {
/* 1141 */       if (!hasValidSize())
/* 1142 */         updatePreferredSize(getRow());
/* 1143 */       return this.xOrigin;
/*      */     }
/*      */ 
/*      */     public int getYOrigin()
/*      */     {
/* 1150 */       if (VariableHeightLayoutCache.this.isFixedRowHeight()) {
/* 1151 */         int i = getRow();
/*      */ 
/* 1153 */         if (i == -1)
/* 1154 */           return -1;
/* 1155 */         return VariableHeightLayoutCache.this.getRowHeight() * i;
/*      */       }
/* 1157 */       return this.yOrigin;
/*      */     }
/*      */ 
/*      */     public int getPreferredHeight()
/*      */     {
/* 1164 */       if (VariableHeightLayoutCache.this.isFixedRowHeight())
/* 1165 */         return VariableHeightLayoutCache.this.getRowHeight();
/* 1166 */       if (!hasValidSize())
/* 1167 */         updatePreferredSize(getRow());
/* 1168 */       return this.preferredHeight;
/*      */     }
/*      */ 
/*      */     public int getPreferredWidth()
/*      */     {
/* 1175 */       if (!hasValidSize())
/* 1176 */         updatePreferredSize(getRow());
/* 1177 */       return this.preferredWidth;
/*      */     }
/*      */ 
/*      */     public boolean hasValidSize()
/*      */     {
/* 1184 */       return this.preferredHeight != 0;
/*      */     }
/*      */ 
/*      */     public int getRow()
/*      */     {
/* 1191 */       return VariableHeightLayoutCache.this.visibleNodes.indexOf(this);
/*      */     }
/*      */ 
/*      */     public boolean hasBeenExpanded()
/*      */     {
/* 1198 */       return this.hasBeenExpanded;
/*      */     }
/*      */ 
/*      */     public boolean isExpanded()
/*      */     {
/* 1205 */       return this.expanded;
/*      */     }
/*      */ 
/*      */     public TreeStateNode getLastVisibleNode()
/*      */     {
/* 1213 */       TreeStateNode localTreeStateNode = this;
/*      */ 
/* 1215 */       while ((localTreeStateNode.isExpanded()) && (localTreeStateNode.getChildCount() > 0))
/* 1216 */         localTreeStateNode = (TreeStateNode)localTreeStateNode.getLastChild();
/* 1217 */       return localTreeStateNode;
/*      */     }
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/* 1224 */       if (this == VariableHeightLayoutCache.this.root) {
/* 1225 */         return true;
/*      */       }
/* 1227 */       TreeStateNode localTreeStateNode = (TreeStateNode)getParent();
/*      */ 
/* 1229 */       return (localTreeStateNode != null) && (localTreeStateNode.isExpanded()) && (localTreeStateNode.isVisible());
/*      */     }
/*      */ 
/*      */     public int getModelChildCount()
/*      */     {
/* 1238 */       if (this.hasBeenExpanded)
/* 1239 */         return super.getChildCount();
/* 1240 */       return VariableHeightLayoutCache.this.getModel().getChildCount(getValue());
/*      */     }
/*      */ 
/*      */     public int getVisibleChildCount()
/*      */     {
/* 1248 */       int i = 0;
/*      */ 
/* 1250 */       if (isExpanded()) {
/* 1251 */         int j = getChildCount();
/*      */ 
/* 1253 */         i += j;
/* 1254 */         for (int k = 0; k < j; k++) {
/* 1255 */           i += ((TreeStateNode)getChildAt(k)).getVisibleChildCount();
/*      */         }
/*      */       }
/* 1258 */       return i;
/*      */     }
/*      */ 
/*      */     public void toggleExpanded()
/*      */     {
/* 1265 */       if (isExpanded())
/* 1266 */         collapse();
/*      */       else
/* 1268 */         expand();
/*      */     }
/*      */ 
/*      */     public void makeVisible()
/*      */     {
/* 1277 */       TreeStateNode localTreeStateNode = (TreeStateNode)getParent();
/*      */ 
/* 1279 */       if (localTreeStateNode != null)
/* 1280 */         localTreeStateNode.expandParentAndReceiver();
/*      */     }
/*      */ 
/*      */     public void expand()
/*      */     {
/* 1287 */       expand(true);
/*      */     }
/*      */ 
/*      */     public void collapse()
/*      */     {
/* 1294 */       collapse(true);
/*      */     }
/*      */ 
/*      */     public Object getValue()
/*      */     {
/* 1302 */       return getUserObject();
/*      */     }
/*      */ 
/*      */     public TreePath getTreePath()
/*      */     {
/* 1309 */       return this.path;
/*      */     }
/*      */ 
/*      */     protected void resetChildrenPaths(TreePath paramTreePath)
/*      */     {
/* 1320 */       VariableHeightLayoutCache.this.removeMapping(this);
/* 1321 */       if (paramTreePath == null)
/* 1322 */         this.path = new TreePath(getUserObject());
/*      */       else
/* 1324 */         this.path = paramTreePath.pathByAddingChild(getUserObject());
/* 1325 */       VariableHeightLayoutCache.this.addMapping(this);
/* 1326 */       for (int i = getChildCount() - 1; i >= 0; i--)
/* 1327 */         ((TreeStateNode)getChildAt(i)).resetChildrenPaths(this.path);
/*      */     }
/*      */ 
/*      */     protected void setYOrigin(int paramInt)
/*      */     {
/* 1335 */       this.yOrigin = paramInt;
/*      */     }
/*      */ 
/*      */     protected void shiftYOriginBy(int paramInt)
/*      */     {
/* 1342 */       this.yOrigin += paramInt;
/*      */     }
/*      */ 
/*      */     protected void updatePreferredSize()
/*      */     {
/* 1350 */       updatePreferredSize(getRow());
/*      */     }
/*      */ 
/*      */     protected void updatePreferredSize(int paramInt)
/*      */     {
/* 1359 */       Rectangle localRectangle = VariableHeightLayoutCache.this.getNodeDimensions(getUserObject(), paramInt, getLevel(), isExpanded(), VariableHeightLayoutCache.this.boundsBuffer);
/*      */ 
/* 1364 */       if (localRectangle == null) {
/* 1365 */         this.xOrigin = 0;
/* 1366 */         this.preferredWidth = (this.preferredHeight = 0);
/* 1367 */         VariableHeightLayoutCache.this.updateNodeSizes = true;
/*      */       }
/* 1369 */       else if (localRectangle.height == 0) {
/* 1370 */         this.xOrigin = 0;
/* 1371 */         this.preferredWidth = (this.preferredHeight = 0);
/* 1372 */         VariableHeightLayoutCache.this.updateNodeSizes = true;
/*      */       }
/*      */       else {
/* 1375 */         this.xOrigin = localRectangle.x;
/* 1376 */         this.preferredWidth = localRectangle.width;
/* 1377 */         if (VariableHeightLayoutCache.this.isFixedRowHeight())
/* 1378 */           this.preferredHeight = VariableHeightLayoutCache.this.getRowHeight();
/*      */         else
/* 1380 */           this.preferredHeight = localRectangle.height;
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void markSizeInvalid()
/*      */     {
/* 1389 */       this.preferredHeight = 0;
/*      */     }
/*      */ 
/*      */     protected void deepMarkSizeInvalid()
/*      */     {
/* 1396 */       markSizeInvalid();
/* 1397 */       for (int i = getChildCount() - 1; i >= 0; i--)
/* 1398 */         ((TreeStateNode)getChildAt(i)).deepMarkSizeInvalid();
/*      */     }
/*      */ 
/*      */     protected Enumeration getLoadedChildren(boolean paramBoolean)
/*      */     {
/* 1408 */       if ((!paramBoolean) || (this.hasBeenExpanded)) {
/* 1409 */         return super.children();
/*      */       }
/*      */ 
/* 1412 */       Object localObject = getValue();
/* 1413 */       TreeModel localTreeModel = VariableHeightLayoutCache.this.getModel();
/* 1414 */       int i = localTreeModel.getChildCount(localObject);
/*      */ 
/* 1416 */       this.hasBeenExpanded = true;
/*      */ 
/* 1418 */       int j = getRow();
/*      */       int k;
/*      */       TreeStateNode localTreeStateNode;
/* 1420 */       if (j == -1) {
/* 1421 */         for (k = 0; k < i; k++) {
/* 1422 */           localTreeStateNode = VariableHeightLayoutCache.this.createNodeForValue(localTreeModel.getChild(localObject, k));
/*      */ 
/* 1424 */           add(localTreeStateNode);
/* 1425 */           localTreeStateNode.updatePreferredSize(-1);
/*      */         }
/*      */       }
/*      */       else {
/* 1429 */         j++;
/* 1430 */         for (k = 0; k < i; k++) {
/* 1431 */           localTreeStateNode = VariableHeightLayoutCache.this.createNodeForValue(localTreeModel.getChild(localObject, k));
/*      */ 
/* 1433 */           add(localTreeStateNode);
/* 1434 */           localTreeStateNode.updatePreferredSize(j++);
/*      */         }
/*      */       }
/* 1437 */       return super.children();
/*      */     }
/*      */ 
/*      */     protected void didAdjustTree()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected void expandParentAndReceiver()
/*      */     {
/* 1452 */       TreeStateNode localTreeStateNode = (TreeStateNode)getParent();
/*      */ 
/* 1454 */       if (localTreeStateNode != null)
/* 1455 */         localTreeStateNode.expandParentAndReceiver();
/* 1456 */       expand();
/*      */     }
/*      */ 
/*      */     protected void expand(boolean paramBoolean)
/*      */     {
/* 1466 */       if ((!isExpanded()) && (!isLeaf())) {
/* 1467 */         boolean bool = VariableHeightLayoutCache.this.isFixedRowHeight();
/* 1468 */         int i = getPreferredHeight();
/* 1469 */         TreeStateNode localTreeStateNode1 = getRow();
/*      */ 
/* 1471 */         this.expanded = true;
/* 1472 */         updatePreferredSize(localTreeStateNode1);
/*      */         TreeStateNode localTreeStateNode4;
/*      */         int m;
/* 1474 */         if (!this.hasBeenExpanded)
/*      */         {
/* 1476 */           localObject = getValue();
/* 1477 */           TreeModel localTreeModel = VariableHeightLayoutCache.this.getModel();
/* 1478 */           int k = localTreeModel.getChildCount(localObject);
/*      */ 
/* 1480 */           this.hasBeenExpanded = true;
/* 1481 */           if (localTreeStateNode1 == -1) {
/* 1482 */             for (localTreeStateNode4 = 0; localTreeStateNode4 < k; localTreeStateNode4++) {
/* 1483 */               localTreeStateNode2 = VariableHeightLayoutCache.this.createNodeForValue(localTreeModel.getChild(localObject, localTreeStateNode4));
/*      */ 
/* 1485 */               add(localTreeStateNode2);
/* 1486 */               localTreeStateNode2.updatePreferredSize(-1);
/*      */             }
/*      */           }
/*      */           else {
/* 1490 */             localTreeStateNode4 = localTreeStateNode1 + 1;
/* 1491 */             for (m = 0; m < k; m++) {
/* 1492 */               localTreeStateNode2 = VariableHeightLayoutCache.this.createNodeForValue(localTreeModel.getChild(localObject, m));
/*      */ 
/* 1494 */               add(localTreeStateNode2);
/* 1495 */               localTreeStateNode2.updatePreferredSize(localTreeStateNode4);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1500 */         TreeStateNode localTreeStateNode2 = localTreeStateNode1;
/* 1501 */         Object localObject = preorderEnumeration();
/* 1502 */         ((Enumeration)localObject).nextElement();
/*      */         int j;
/* 1506 */         if (bool)
/* 1507 */           j = 0;
/* 1508 */         else if ((this == VariableHeightLayoutCache.this.root) && (!VariableHeightLayoutCache.this.isRootVisible()))
/* 1509 */           j = 0;
/*      */         else
/* 1511 */           j = getYOrigin() + getPreferredHeight();
/*      */         TreeStateNode localTreeStateNode3;
/* 1513 */         if (!bool) {
/* 1514 */           while (((Enumeration)localObject).hasMoreElements()) {
/* 1515 */             localTreeStateNode3 = (TreeStateNode)((Enumeration)localObject).nextElement();
/* 1516 */             if ((!VariableHeightLayoutCache.this.updateNodeSizes) && (!localTreeStateNode3.hasValidSize()))
/* 1517 */               localTreeStateNode3.updatePreferredSize(localTreeStateNode2 + 1);
/* 1518 */             localTreeStateNode3.setYOrigin(j);
/* 1519 */             j += localTreeStateNode3.getPreferredHeight();
/* 1520 */             VariableHeightLayoutCache.this.visibleNodes.insertElementAt(localTreeStateNode3, ++localTreeStateNode2);
/*      */           }
/*      */         }
/*      */ 
/* 1524 */         while (((Enumeration)localObject).hasMoreElements()) {
/* 1525 */           localTreeStateNode3 = (TreeStateNode)((Enumeration)localObject).nextElement();
/* 1526 */           VariableHeightLayoutCache.this.visibleNodes.insertElementAt(localTreeStateNode3, ++localTreeStateNode2);
/*      */         }
/*      */ 
/* 1530 */         if ((paramBoolean) && ((localTreeStateNode1 != localTreeStateNode2) || (getPreferredHeight() != i)))
/*      */         {
/* 1533 */           if (!bool) { localTreeStateNode2++; if (localTreeStateNode2 < VariableHeightLayoutCache.this.getRowCount())
/*      */             {
/* 1535 */               m = j - (getYOrigin() + getPreferredHeight()) + (getPreferredHeight() - i);
/*      */ 
/* 1539 */               for (localTreeStateNode4 = VariableHeightLayoutCache.this.visibleNodes.size() - 1; localTreeStateNode4 >= localTreeStateNode2; 
/* 1540 */                 localTreeStateNode4--)
/* 1541 */                 ((TreeStateNode)VariableHeightLayoutCache.this.visibleNodes.elementAt(localTreeStateNode4)).shiftYOriginBy(m);
/*      */             }
/*      */           }
/* 1544 */           didAdjustTree();
/* 1545 */           VariableHeightLayoutCache.this.visibleNodesChanged();
/*      */         }
/*      */ 
/* 1549 */         if (VariableHeightLayoutCache.this.treeSelectionModel != null)
/* 1550 */           VariableHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void collapse(boolean paramBoolean)
/*      */     {
/* 1560 */       if (isExpanded()) {
/* 1561 */         Enumeration localEnumeration = preorderEnumeration();
/* 1562 */         localEnumeration.nextElement();
/* 1563 */         int i = 0;
/* 1564 */         boolean bool = VariableHeightLayoutCache.this.isFixedRowHeight();
/*      */         int j;
/* 1566 */         if (bool)
/* 1567 */           j = 0;
/*      */         else
/* 1569 */           j = getPreferredHeight() + getYOrigin();
/* 1570 */         int k = getPreferredHeight();
/* 1571 */         int m = j;
/* 1572 */         int n = getRow();
/*      */         TreeStateNode localTreeStateNode;
/* 1574 */         if (!bool) {
/* 1575 */           while (localEnumeration.hasMoreElements()) {
/* 1576 */             localTreeStateNode = (TreeStateNode)localEnumeration.nextElement();
/*      */ 
/* 1578 */             if (localTreeStateNode.isVisible()) {
/* 1579 */               i++;
/*      */ 
/* 1581 */               j = localTreeStateNode.getYOrigin() + localTreeStateNode.getPreferredHeight();
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1587 */         while (localEnumeration.hasMoreElements()) {
/* 1588 */           localTreeStateNode = (TreeStateNode)localEnumeration.nextElement();
/*      */ 
/* 1590 */           if (localTreeStateNode.isVisible()) {
/* 1591 */             i++;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1598 */         for (int i1 = i + n; i1 > n; 
/* 1599 */           i1--) {
/* 1600 */           VariableHeightLayoutCache.this.visibleNodes.removeElementAt(i1);
/*      */         }
/*      */ 
/* 1603 */         this.expanded = false;
/*      */ 
/* 1605 */         if (n == -1)
/* 1606 */           markSizeInvalid();
/* 1607 */         else if (paramBoolean) {
/* 1608 */           updatePreferredSize(n);
/*      */         }
/* 1610 */         if ((n != -1) && (paramBoolean) && ((i > 0) || (k != getPreferredHeight())))
/*      */         {
/* 1613 */           m += getPreferredHeight() - k;
/* 1614 */           if ((!bool) && (n + 1 < VariableHeightLayoutCache.this.getRowCount()) && (m != j))
/*      */           {
/* 1618 */             int i3 = m - j;
/* 1619 */             i1 = n + 1; int i2 = VariableHeightLayoutCache.this.visibleNodes.size();
/*      */ 
/* 1621 */             for (; i1 < i2; i1++) {
/* 1622 */               ((TreeStateNode)VariableHeightLayoutCache.this.visibleNodes.elementAt(i1)).shiftYOriginBy(i3);
/*      */             }
/*      */           }
/* 1625 */           didAdjustTree();
/* 1626 */           VariableHeightLayoutCache.this.visibleNodesChanged();
/*      */         }
/* 1628 */         if ((VariableHeightLayoutCache.this.treeSelectionModel != null) && (i > 0) && (n != -1))
/*      */         {
/* 1630 */           VariableHeightLayoutCache.this.treeSelectionModel.resetRowSelection();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void removeFromMapping()
/*      */     {
/* 1640 */       if (this.path != null) {
/* 1641 */         VariableHeightLayoutCache.this.removeMapping(this);
/* 1642 */         for (int i = getChildCount() - 1; i >= 0; i--)
/* 1643 */           ((TreeStateNode)getChildAt(i)).removeFromMapping();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class VisibleTreeStateNodeEnumeration
/*      */     implements Enumeration<TreePath>
/*      */   {
/*      */     protected VariableHeightLayoutCache.TreeStateNode parent;
/*      */     protected int nextIndex;
/*      */     protected int childCount;
/*      */ 
/*      */     protected VisibleTreeStateNodeEnumeration(VariableHeightLayoutCache.TreeStateNode arg2)
/*      */     {
/* 1663 */       this(localTreeStateNode, -1);
/*      */     }
/*      */ 
/*      */     protected VisibleTreeStateNodeEnumeration(VariableHeightLayoutCache.TreeStateNode paramInt, int arg3)
/*      */     {
/* 1668 */       this.parent = paramInt;
/*      */       int i;
/* 1669 */       this.nextIndex = i;
/* 1670 */       this.childCount = this.parent.getChildCount();
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements()
/*      */     {
/* 1677 */       return this.parent != null;
/*      */     }
/*      */ 
/*      */     public TreePath nextElement()
/*      */     {
/* 1684 */       if (!hasMoreElements())
/* 1685 */         throw new NoSuchElementException("No more visible paths");
/*      */       TreePath localTreePath;
/* 1689 */       if (this.nextIndex == -1) {
/* 1690 */         localTreePath = this.parent.getTreePath();
/*      */       }
/*      */       else {
/* 1693 */         VariableHeightLayoutCache.TreeStateNode localTreeStateNode = (VariableHeightLayoutCache.TreeStateNode)this.parent.getChildAt(this.nextIndex);
/*      */ 
/* 1696 */         localTreePath = localTreeStateNode.getTreePath();
/*      */       }
/* 1698 */       updateNextObject();
/* 1699 */       return localTreePath;
/*      */     }
/*      */ 
/*      */     protected void updateNextObject()
/*      */     {
/* 1707 */       if (!updateNextIndex())
/* 1708 */         findNextValidParent();
/*      */     }
/*      */ 
/*      */     protected boolean findNextValidParent()
/*      */     {
/* 1717 */       if (this.parent == VariableHeightLayoutCache.this.root)
/*      */       {
/* 1719 */         this.parent = null;
/* 1720 */         return false;
/*      */       }
/* 1722 */       while (this.parent != null) {
/* 1723 */         VariableHeightLayoutCache.TreeStateNode localTreeStateNode = (VariableHeightLayoutCache.TreeStateNode)this.parent.getParent();
/*      */ 
/* 1726 */         if (localTreeStateNode != null) {
/* 1727 */           this.nextIndex = localTreeStateNode.getIndex(this.parent);
/* 1728 */           this.parent = localTreeStateNode;
/* 1729 */           this.childCount = this.parent.getChildCount();
/* 1730 */           if (updateNextIndex())
/* 1731 */             return true;
/*      */         }
/*      */         else {
/* 1734 */           this.parent = null;
/*      */         }
/*      */       }
/* 1736 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean updateNextIndex()
/*      */     {
/* 1746 */       if ((this.nextIndex == -1) && (!this.parent.isExpanded())) {
/* 1747 */         return false;
/*      */       }
/*      */ 
/* 1750 */       if (this.childCount == 0) {
/* 1751 */         return false;
/*      */       }
/* 1753 */       if (++this.nextIndex >= this.childCount) {
/* 1754 */         return false;
/*      */       }
/* 1756 */       VariableHeightLayoutCache.TreeStateNode localTreeStateNode = (VariableHeightLayoutCache.TreeStateNode)this.parent.getChildAt(this.nextIndex);
/*      */ 
/* 1759 */       if ((localTreeStateNode != null) && (localTreeStateNode.isExpanded())) {
/* 1760 */         this.parent = localTreeStateNode;
/* 1761 */         this.nextIndex = -1;
/* 1762 */         this.childCount = localTreeStateNode.getChildCount();
/*      */       }
/* 1764 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.VariableHeightLayoutCache
 * JD-Core Version:    0.6.2
 */