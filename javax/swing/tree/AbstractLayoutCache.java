/*     */ package javax.swing.tree;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Enumeration;
/*     */ import javax.swing.event.TreeModelEvent;
/*     */ 
/*     */ public abstract class AbstractLayoutCache
/*     */   implements RowMapper
/*     */ {
/*     */   protected NodeDimensions nodeDimensions;
/*     */   protected TreeModel treeModel;
/*     */   protected TreeSelectionModel treeSelectionModel;
/*     */   protected boolean rootVisible;
/*     */   protected int rowHeight;
/*     */ 
/*     */   public void setNodeDimensions(NodeDimensions paramNodeDimensions)
/*     */   {
/*  77 */     this.nodeDimensions = paramNodeDimensions;
/*     */   }
/*     */ 
/*     */   public NodeDimensions getNodeDimensions()
/*     */   {
/*  87 */     return this.nodeDimensions;
/*     */   }
/*     */ 
/*     */   public void setModel(TreeModel paramTreeModel)
/*     */   {
/*  97 */     this.treeModel = paramTreeModel;
/*     */   }
/*     */ 
/*     */   public TreeModel getModel()
/*     */   {
/* 106 */     return this.treeModel;
/*     */   }
/*     */ 
/*     */   public void setRootVisible(boolean paramBoolean)
/*     */   {
/* 121 */     this.rootVisible = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isRootVisible()
/*     */   {
/* 131 */     return this.rootVisible;
/*     */   }
/*     */ 
/*     */   public void setRowHeight(int paramInt)
/*     */   {
/* 145 */     this.rowHeight = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRowHeight()
/*     */   {
/* 154 */     return this.rowHeight;
/*     */   }
/*     */ 
/*     */   public void setSelectionModel(TreeSelectionModel paramTreeSelectionModel)
/*     */   {
/* 164 */     if (this.treeSelectionModel != null)
/* 165 */       this.treeSelectionModel.setRowMapper(null);
/* 166 */     this.treeSelectionModel = paramTreeSelectionModel;
/* 167 */     if (this.treeSelectionModel != null)
/* 168 */       this.treeSelectionModel.setRowMapper(this);
/*     */   }
/*     */ 
/*     */   public TreeSelectionModel getSelectionModel()
/*     */   {
/* 177 */     return this.treeSelectionModel;
/*     */   }
/*     */ 
/*     */   public int getPreferredHeight()
/*     */   {
/* 187 */     int i = getRowCount();
/*     */ 
/* 189 */     if (i > 0) {
/* 190 */       Rectangle localRectangle = getBounds(getPathForRow(i - 1), null);
/*     */ 
/* 193 */       if (localRectangle != null)
/* 194 */         return localRectangle.y + localRectangle.height;
/*     */     }
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getPreferredWidth(Rectangle paramRectangle)
/*     */   {
/* 213 */     int i = getRowCount();
/*     */ 
/* 215 */     if (i > 0)
/*     */     {
/*     */       TreePath localTreePath;
/*     */       int j;
/* 220 */       if (paramRectangle == null) {
/* 221 */         localTreePath = getPathForRow(0);
/* 222 */         j = 2147483647;
/*     */       }
/*     */       else {
/* 225 */         localTreePath = getPathClosestTo(paramRectangle.x, paramRectangle.y);
/* 226 */         j = paramRectangle.height + paramRectangle.y;
/*     */       }
/*     */ 
/* 229 */       Enumeration localEnumeration = getVisiblePathsFrom(localTreePath);
/*     */ 
/* 231 */       if ((localEnumeration != null) && (localEnumeration.hasMoreElements())) {
/* 232 */         Rectangle localRectangle = getBounds((TreePath)localEnumeration.nextElement(), null);
/*     */         int k;
/* 236 */         if (localRectangle != null) {
/* 237 */           k = localRectangle.x + localRectangle.width;
/* 238 */           if (localRectangle.y >= j)
/* 239 */             return k;
/*     */         }
/*     */         else
/*     */         {
/* 243 */           k = 0;
/* 244 */         }while ((localRectangle != null) && (localEnumeration.hasMoreElements())) {
/* 245 */           localRectangle = getBounds((TreePath)localEnumeration.nextElement(), localRectangle);
/*     */ 
/* 247 */           if ((localRectangle != null) && (localRectangle.y < j)) {
/* 248 */             k = Math.max(k, localRectangle.x + localRectangle.width);
/*     */           }
/*     */           else {
/* 251 */             localRectangle = null;
/*     */           }
/*     */         }
/* 254 */         return k;
/*     */       }
/*     */     }
/* 257 */     return 0;
/*     */   }
/*     */ 
/*     */   public abstract boolean isExpanded(TreePath paramTreePath);
/*     */ 
/*     */   public abstract Rectangle getBounds(TreePath paramTreePath, Rectangle paramRectangle);
/*     */ 
/*     */   public abstract TreePath getPathForRow(int paramInt);
/*     */ 
/*     */   public abstract int getRowForPath(TreePath paramTreePath);
/*     */ 
/*     */   public abstract TreePath getPathClosestTo(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract Enumeration<TreePath> getVisiblePathsFrom(TreePath paramTreePath);
/*     */ 
/*     */   public abstract int getVisibleChildCount(TreePath paramTreePath);
/*     */ 
/*     */   public abstract void setExpandedState(TreePath paramTreePath, boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean getExpandedState(TreePath paramTreePath);
/*     */ 
/*     */   public abstract int getRowCount();
/*     */ 
/*     */   public abstract void invalidateSizes();
/*     */ 
/*     */   public abstract void invalidatePathBounds(TreePath paramTreePath);
/*     */ 
/*     */   public abstract void treeNodesChanged(TreeModelEvent paramTreeModelEvent);
/*     */ 
/*     */   public abstract void treeNodesInserted(TreeModelEvent paramTreeModelEvent);
/*     */ 
/*     */   public abstract void treeNodesRemoved(TreeModelEvent paramTreeModelEvent);
/*     */ 
/*     */   public abstract void treeStructureChanged(TreeModelEvent paramTreeModelEvent);
/*     */ 
/*     */   public int[] getRowsForPaths(TreePath[] paramArrayOfTreePath)
/*     */   {
/* 453 */     if (paramArrayOfTreePath == null) {
/* 454 */       return null;
/*     */     }
/* 456 */     int i = paramArrayOfTreePath.length;
/* 457 */     int[] arrayOfInt = new int[i];
/*     */ 
/* 459 */     for (int j = 0; j < i; j++)
/* 460 */       arrayOfInt[j] = getRowForPath(paramArrayOfTreePath[j]);
/* 461 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   protected Rectangle getNodeDimensions(Object paramObject, int paramInt1, int paramInt2, boolean paramBoolean, Rectangle paramRectangle)
/*     */   {
/* 489 */     NodeDimensions localNodeDimensions = getNodeDimensions();
/*     */ 
/* 491 */     if (localNodeDimensions != null) {
/* 492 */       return localNodeDimensions.getNodeDimensions(paramObject, paramInt1, paramInt2, paramBoolean, paramRectangle);
/*     */     }
/* 494 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean isFixedRowHeight()
/*     */   {
/* 501 */     return this.rowHeight > 0;
/*     */   }
/*     */ 
/*     */   public static abstract class NodeDimensions
/*     */   {
/*     */     public abstract Rectangle getNodeDimensions(Object paramObject, int paramInt1, int paramInt2, boolean paramBoolean, Rectangle paramRectangle);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.AbstractLayoutCache
 * JD-Core Version:    0.6.2
 */