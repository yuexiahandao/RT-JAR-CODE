/*     */ package javax.swing.tree;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TreeModelEvent;
/*     */ import javax.swing.event.TreeModelListener;
/*     */ 
/*     */ public class DefaultTreeModel
/*     */   implements Serializable, TreeModel
/*     */ {
/*     */   protected TreeNode root;
/*  56 */   protected EventListenerList listenerList = new EventListenerList();
/*     */   protected boolean asksAllowsChildren;
/*     */ 
/*     */   @ConstructorProperties({"root"})
/*     */   public DefaultTreeModel(TreeNode paramTreeNode)
/*     */   {
/*  84 */     this(paramTreeNode, false);
/*     */   }
/*     */ 
/*     */   public DefaultTreeModel(TreeNode paramTreeNode, boolean paramBoolean)
/*     */   {
/*  99 */     this.root = paramTreeNode;
/* 100 */     this.asksAllowsChildren = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setAsksAllowsChildren(boolean paramBoolean)
/*     */   {
/* 109 */     this.asksAllowsChildren = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean asksAllowsChildren()
/*     */   {
/* 121 */     return this.asksAllowsChildren;
/*     */   }
/*     */ 
/*     */   public void setRoot(TreeNode paramTreeNode)
/*     */   {
/* 129 */     TreeNode localTreeNode = this.root;
/* 130 */     this.root = paramTreeNode;
/* 131 */     if ((paramTreeNode == null) && (localTreeNode != null)) {
/* 132 */       fireTreeStructureChanged(this, null);
/*     */     }
/*     */     else
/* 135 */       nodeStructureChanged(paramTreeNode);
/*     */   }
/*     */ 
/*     */   public Object getRoot()
/*     */   {
/* 146 */     return this.root;
/*     */   }
/*     */ 
/*     */   public int getIndexOfChild(Object paramObject1, Object paramObject2)
/*     */   {
/* 158 */     if ((paramObject1 == null) || (paramObject2 == null))
/* 159 */       return -1;
/* 160 */     return ((TreeNode)paramObject1).getIndex((TreeNode)paramObject2);
/*     */   }
/*     */ 
/*     */   public Object getChild(Object paramObject, int paramInt)
/*     */   {
/* 174 */     return ((TreeNode)paramObject).getChildAt(paramInt);
/*     */   }
/*     */ 
/*     */   public int getChildCount(Object paramObject)
/*     */   {
/* 186 */     return ((TreeNode)paramObject).getChildCount();
/*     */   }
/*     */ 
/*     */   public boolean isLeaf(Object paramObject)
/*     */   {
/* 201 */     if (this.asksAllowsChildren)
/* 202 */       return !((TreeNode)paramObject).getAllowsChildren();
/* 203 */     return ((TreeNode)paramObject).isLeaf();
/*     */   }
/*     */ 
/*     */   public void reload()
/*     */   {
/* 212 */     reload(this.root);
/*     */   }
/*     */ 
/*     */   public void valueForPathChanged(TreePath paramTreePath, Object paramObject)
/*     */   {
/* 222 */     MutableTreeNode localMutableTreeNode = (MutableTreeNode)paramTreePath.getLastPathComponent();
/*     */ 
/* 224 */     localMutableTreeNode.setUserObject(paramObject);
/* 225 */     nodeChanged(localMutableTreeNode);
/*     */   }
/*     */ 
/*     */   public void insertNodeInto(MutableTreeNode paramMutableTreeNode1, MutableTreeNode paramMutableTreeNode2, int paramInt)
/*     */   {
/* 236 */     paramMutableTreeNode2.insert(paramMutableTreeNode1, paramInt);
/*     */ 
/* 238 */     int[] arrayOfInt = new int[1];
/*     */ 
/* 240 */     arrayOfInt[0] = paramInt;
/* 241 */     nodesWereInserted(paramMutableTreeNode2, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public void removeNodeFromParent(MutableTreeNode paramMutableTreeNode)
/*     */   {
/* 251 */     MutableTreeNode localMutableTreeNode = (MutableTreeNode)paramMutableTreeNode.getParent();
/*     */ 
/* 253 */     if (localMutableTreeNode == null) {
/* 254 */       throw new IllegalArgumentException("node does not have a parent.");
/*     */     }
/* 256 */     int[] arrayOfInt = new int[1];
/* 257 */     Object[] arrayOfObject = new Object[1];
/*     */ 
/* 259 */     arrayOfInt[0] = localMutableTreeNode.getIndex(paramMutableTreeNode);
/* 260 */     localMutableTreeNode.remove(arrayOfInt[0]);
/* 261 */     arrayOfObject[0] = paramMutableTreeNode;
/* 262 */     nodesWereRemoved(localMutableTreeNode, arrayOfInt, arrayOfObject);
/*     */   }
/*     */ 
/*     */   public void nodeChanged(TreeNode paramTreeNode)
/*     */   {
/* 270 */     if ((this.listenerList != null) && (paramTreeNode != null)) {
/* 271 */       TreeNode localTreeNode = paramTreeNode.getParent();
/*     */ 
/* 273 */       if (localTreeNode != null) {
/* 274 */         int i = localTreeNode.getIndex(paramTreeNode);
/* 275 */         if (i != -1) {
/* 276 */           int[] arrayOfInt = new int[1];
/*     */ 
/* 278 */           arrayOfInt[0] = i;
/* 279 */           nodesChanged(localTreeNode, arrayOfInt);
/*     */         }
/*     */       }
/* 282 */       else if (paramTreeNode == getRoot()) {
/* 283 */         nodesChanged(paramTreeNode, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reload(TreeNode paramTreeNode)
/*     */   {
/* 296 */     if (paramTreeNode != null)
/* 297 */       fireTreeStructureChanged(this, getPathToRoot(paramTreeNode), null, null);
/*     */   }
/*     */ 
/*     */   public void nodesWereInserted(TreeNode paramTreeNode, int[] paramArrayOfInt)
/*     */   {
/* 307 */     if ((this.listenerList != null) && (paramTreeNode != null) && (paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
/*     */     {
/* 309 */       int i = paramArrayOfInt.length;
/* 310 */       Object[] arrayOfObject = new Object[i];
/*     */ 
/* 312 */       for (int j = 0; j < i; j++)
/* 313 */         arrayOfObject[j] = paramTreeNode.getChildAt(paramArrayOfInt[j]);
/* 314 */       fireTreeNodesInserted(this, getPathToRoot(paramTreeNode), paramArrayOfInt, arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void nodesWereRemoved(TreeNode paramTreeNode, int[] paramArrayOfInt, Object[] paramArrayOfObject)
/*     */   {
/* 327 */     if ((paramTreeNode != null) && (paramArrayOfInt != null))
/* 328 */       fireTreeNodesRemoved(this, getPathToRoot(paramTreeNode), paramArrayOfInt, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void nodesChanged(TreeNode paramTreeNode, int[] paramArrayOfInt)
/*     */   {
/* 338 */     if (paramTreeNode != null)
/* 339 */       if (paramArrayOfInt != null) {
/* 340 */         int i = paramArrayOfInt.length;
/*     */ 
/* 342 */         if (i > 0) {
/* 343 */           Object[] arrayOfObject = new Object[i];
/*     */ 
/* 345 */           for (int j = 0; j < i; j++) {
/* 346 */             arrayOfObject[j] = paramTreeNode.getChildAt(paramArrayOfInt[j]);
/*     */           }
/* 348 */           fireTreeNodesChanged(this, getPathToRoot(paramTreeNode), paramArrayOfInt, arrayOfObject);
/*     */         }
/*     */ 
/*     */       }
/* 352 */       else if (paramTreeNode == getRoot()) {
/* 353 */         fireTreeNodesChanged(this, getPathToRoot(paramTreeNode), null, null);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void nodeStructureChanged(TreeNode paramTreeNode)
/*     */   {
/* 364 */     if (paramTreeNode != null)
/* 365 */       fireTreeStructureChanged(this, getPathToRoot(paramTreeNode), null, null);
/*     */   }
/*     */ 
/*     */   public TreeNode[] getPathToRoot(TreeNode paramTreeNode)
/*     */   {
/* 378 */     return getPathToRoot(paramTreeNode, 0);
/*     */   }
/*     */ 
/*     */   protected TreeNode[] getPathToRoot(TreeNode paramTreeNode, int paramInt)
/*     */   {
/*     */     TreeNode[] arrayOfTreeNode;
/* 401 */     if (paramTreeNode == null) {
/* 402 */       if (paramInt == 0) {
/* 403 */         return null;
/*     */       }
/* 405 */       arrayOfTreeNode = new TreeNode[paramInt];
/*     */     }
/*     */     else {
/* 408 */       paramInt++;
/* 409 */       if (paramTreeNode == this.root)
/* 410 */         arrayOfTreeNode = new TreeNode[paramInt];
/*     */       else
/* 412 */         arrayOfTreeNode = getPathToRoot(paramTreeNode.getParent(), paramInt);
/* 413 */       arrayOfTreeNode[(arrayOfTreeNode.length - paramInt)] = paramTreeNode;
/*     */     }
/* 415 */     return arrayOfTreeNode;
/*     */   }
/*     */ 
/*     */   public void addTreeModelListener(TreeModelListener paramTreeModelListener)
/*     */   {
/* 429 */     this.listenerList.add(TreeModelListener.class, paramTreeModelListener);
/*     */   }
/*     */ 
/*     */   public void removeTreeModelListener(TreeModelListener paramTreeModelListener)
/*     */   {
/* 439 */     this.listenerList.remove(TreeModelListener.class, paramTreeModelListener);
/*     */   }
/*     */ 
/*     */   public TreeModelListener[] getTreeModelListeners()
/*     */   {
/* 456 */     return (TreeModelListener[])this.listenerList.getListeners(TreeModelListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireTreeNodesChanged(Object paramObject, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */   {
/* 476 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 477 */     TreeModelEvent localTreeModelEvent = null;
/*     */ 
/* 480 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 481 */       if (arrayOfObject[i] == TreeModelListener.class)
/*     */       {
/* 483 */         if (localTreeModelEvent == null) {
/* 484 */           localTreeModelEvent = new TreeModelEvent(paramObject, paramArrayOfObject1, paramArrayOfInt, paramArrayOfObject2);
/*     */         }
/* 486 */         ((TreeModelListener)arrayOfObject[(i + 1)]).treeNodesChanged(localTreeModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireTreeNodesInserted(Object paramObject, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */   {
/* 507 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 508 */     TreeModelEvent localTreeModelEvent = null;
/*     */ 
/* 511 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 512 */       if (arrayOfObject[i] == TreeModelListener.class)
/*     */       {
/* 514 */         if (localTreeModelEvent == null) {
/* 515 */           localTreeModelEvent = new TreeModelEvent(paramObject, paramArrayOfObject1, paramArrayOfInt, paramArrayOfObject2);
/*     */         }
/* 517 */         ((TreeModelListener)arrayOfObject[(i + 1)]).treeNodesInserted(localTreeModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireTreeNodesRemoved(Object paramObject, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */   {
/* 538 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 539 */     TreeModelEvent localTreeModelEvent = null;
/*     */ 
/* 542 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 543 */       if (arrayOfObject[i] == TreeModelListener.class)
/*     */       {
/* 545 */         if (localTreeModelEvent == null) {
/* 546 */           localTreeModelEvent = new TreeModelEvent(paramObject, paramArrayOfObject1, paramArrayOfInt, paramArrayOfObject2);
/*     */         }
/* 548 */         ((TreeModelListener)arrayOfObject[(i + 1)]).treeNodesRemoved(localTreeModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireTreeStructureChanged(Object paramObject, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */   {
/* 570 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 571 */     TreeModelEvent localTreeModelEvent = null;
/*     */ 
/* 574 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 575 */       if (arrayOfObject[i] == TreeModelListener.class)
/*     */       {
/* 577 */         if (localTreeModelEvent == null) {
/* 578 */           localTreeModelEvent = new TreeModelEvent(paramObject, paramArrayOfObject1, paramArrayOfInt, paramArrayOfObject2);
/*     */         }
/* 580 */         ((TreeModelListener)arrayOfObject[(i + 1)]).treeStructureChanged(localTreeModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void fireTreeStructureChanged(Object paramObject, TreePath paramTreePath)
/*     */   {
/* 598 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 599 */     TreeModelEvent localTreeModelEvent = null;
/*     */ 
/* 602 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 603 */       if (arrayOfObject[i] == TreeModelListener.class)
/*     */       {
/* 605 */         if (localTreeModelEvent == null)
/* 606 */           localTreeModelEvent = new TreeModelEvent(paramObject, paramTreePath);
/* 607 */         ((TreeModelListener)arrayOfObject[(i + 1)]).treeStructureChanged(localTreeModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 649 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 654 */     Vector localVector = new Vector();
/*     */ 
/* 656 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 658 */     if ((this.root != null) && ((this.root instanceof TreeModel))) {
/* 659 */       localVector.addElement("root");
/* 660 */       localVector.addElement(this.root);
/*     */     }
/* 662 */     paramObjectOutputStream.writeObject(localVector);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 667 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 669 */     Vector localVector = (Vector)paramObjectInputStream.readObject();
/* 670 */     int i = 0;
/* 671 */     int j = localVector.size();
/*     */ 
/* 673 */     if ((i < j) && (localVector.elementAt(i).equals("root")))
/*     */     {
/* 675 */       this.root = ((TreeNode)localVector.elementAt(++i));
/* 676 */       i++;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.DefaultTreeModel
 * JD-Core Version:    0.6.2
 */