/*      */ package javax.swing.tree;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collections;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class DefaultMutableTreeNode
/*      */   implements Cloneable, MutableTreeNode, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -4298474751201349152L;
/*   96 */   public static final Enumeration<TreeNode> EMPTY_ENUMERATION = Collections.emptyEnumeration();
/*      */   protected MutableTreeNode parent;
/*      */   protected Vector children;
/*      */   protected transient Object userObject;
/*      */   protected boolean allowsChildren;
/*      */ 
/*      */   public DefaultMutableTreeNode()
/*      */   {
/*  117 */     this(null);
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode(Object paramObject)
/*      */   {
/*  128 */     this(paramObject, true);
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode(Object paramObject, boolean paramBoolean)
/*      */   {
/*  143 */     this.parent = null;
/*  144 */     this.allowsChildren = paramBoolean;
/*  145 */     this.userObject = paramObject;
/*      */   }
/*      */ 
/*      */   public void insert(MutableTreeNode paramMutableTreeNode, int paramInt)
/*      */   {
/*  173 */     if (!this.allowsChildren)
/*  174 */       throw new IllegalStateException("node does not allow children");
/*  175 */     if (paramMutableTreeNode == null)
/*  176 */       throw new IllegalArgumentException("new child is null");
/*  177 */     if (isNodeAncestor(paramMutableTreeNode)) {
/*  178 */       throw new IllegalArgumentException("new child is an ancestor");
/*      */     }
/*      */ 
/*  181 */     MutableTreeNode localMutableTreeNode = (MutableTreeNode)paramMutableTreeNode.getParent();
/*      */ 
/*  183 */     if (localMutableTreeNode != null) {
/*  184 */       localMutableTreeNode.remove(paramMutableTreeNode);
/*      */     }
/*  186 */     paramMutableTreeNode.setParent(this);
/*  187 */     if (this.children == null) {
/*  188 */       this.children = new Vector();
/*      */     }
/*  190 */     this.children.insertElementAt(paramMutableTreeNode, paramInt);
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  204 */     MutableTreeNode localMutableTreeNode = (MutableTreeNode)getChildAt(paramInt);
/*  205 */     this.children.removeElementAt(paramInt);
/*  206 */     localMutableTreeNode.setParent(null);
/*      */   }
/*      */ 
/*      */   public void setParent(MutableTreeNode paramMutableTreeNode)
/*      */   {
/*  219 */     this.parent = paramMutableTreeNode;
/*      */   }
/*      */ 
/*      */   public TreeNode getParent()
/*      */   {
/*  228 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public TreeNode getChildAt(int paramInt)
/*      */   {
/*  240 */     if (this.children == null) {
/*  241 */       throw new ArrayIndexOutOfBoundsException("node has no children");
/*      */     }
/*  243 */     return (TreeNode)this.children.elementAt(paramInt);
/*      */   }
/*      */ 
/*      */   public int getChildCount()
/*      */   {
/*  252 */     if (this.children == null) {
/*  253 */       return 0;
/*      */     }
/*  255 */     return this.children.size();
/*      */   }
/*      */ 
/*      */   public int getIndex(TreeNode paramTreeNode)
/*      */   {
/*  273 */     if (paramTreeNode == null) {
/*  274 */       throw new IllegalArgumentException("argument is null");
/*      */     }
/*      */ 
/*  277 */     if (!isNodeChild(paramTreeNode)) {
/*  278 */       return -1;
/*      */     }
/*  280 */     return this.children.indexOf(paramTreeNode);
/*      */   }
/*      */ 
/*      */   public Enumeration children()
/*      */   {
/*  291 */     if (this.children == null) {
/*  292 */       return EMPTY_ENUMERATION;
/*      */     }
/*  294 */     return this.children.elements();
/*      */   }
/*      */ 
/*      */   public void setAllowsChildren(boolean paramBoolean)
/*      */   {
/*  308 */     if (paramBoolean != this.allowsChildren) {
/*  309 */       this.allowsChildren = paramBoolean;
/*  310 */       if (!this.allowsChildren)
/*  311 */         removeAllChildren();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getAllowsChildren()
/*      */   {
/*  322 */     return this.allowsChildren;
/*      */   }
/*      */ 
/*      */   public void setUserObject(Object paramObject)
/*      */   {
/*  334 */     this.userObject = paramObject;
/*      */   }
/*      */ 
/*      */   public Object getUserObject()
/*      */   {
/*  345 */     return this.userObject;
/*      */   }
/*      */ 
/*      */   public void removeFromParent()
/*      */   {
/*  359 */     MutableTreeNode localMutableTreeNode = (MutableTreeNode)getParent();
/*  360 */     if (localMutableTreeNode != null)
/*  361 */       localMutableTreeNode.remove(this);
/*      */   }
/*      */ 
/*      */   public void remove(MutableTreeNode paramMutableTreeNode)
/*      */   {
/*  374 */     if (paramMutableTreeNode == null) {
/*  375 */       throw new IllegalArgumentException("argument is null");
/*      */     }
/*      */ 
/*  378 */     if (!isNodeChild(paramMutableTreeNode)) {
/*  379 */       throw new IllegalArgumentException("argument is not a child");
/*      */     }
/*  381 */     remove(getIndex(paramMutableTreeNode));
/*      */   }
/*      */ 
/*      */   public void removeAllChildren()
/*      */   {
/*  389 */     for (int i = getChildCount() - 1; i >= 0; i--)
/*  390 */       remove(i);
/*      */   }
/*      */ 
/*      */   public void add(MutableTreeNode paramMutableTreeNode)
/*      */   {
/*  406 */     if ((paramMutableTreeNode != null) && (paramMutableTreeNode.getParent() == this))
/*  407 */       insert(paramMutableTreeNode, getChildCount() - 1);
/*      */     else
/*  409 */       insert(paramMutableTreeNode, getChildCount());
/*      */   }
/*      */ 
/*      */   public boolean isNodeAncestor(TreeNode paramTreeNode)
/*      */   {
/*  432 */     if (paramTreeNode == null) {
/*  433 */       return false;
/*      */     }
/*      */ 
/*  436 */     Object localObject = this;
/*      */     do
/*      */     {
/*  439 */       if (localObject == paramTreeNode)
/*  440 */         return true;
/*      */     }
/*  442 */     while ((localObject = ((TreeNode)localObject).getParent()) != null);
/*      */ 
/*  444 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isNodeDescendant(DefaultMutableTreeNode paramDefaultMutableTreeNode)
/*      */   {
/*  461 */     if (paramDefaultMutableTreeNode == null) {
/*  462 */       return false;
/*      */     }
/*  464 */     return paramDefaultMutableTreeNode.isNodeAncestor(this);
/*      */   }
/*      */ 
/*      */   public TreeNode getSharedAncestor(DefaultMutableTreeNode paramDefaultMutableTreeNode)
/*      */   {
/*  480 */     if (paramDefaultMutableTreeNode == this)
/*  481 */       return this;
/*  482 */     if (paramDefaultMutableTreeNode == null) {
/*  483 */       return null;
/*      */     }
/*      */ 
/*  489 */     int i = getLevel();
/*  490 */     int j = paramDefaultMutableTreeNode.getLevel();
/*      */     int k;
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  492 */     if (j > i) {
/*  493 */       k = j - i;
/*  494 */       localObject1 = paramDefaultMutableTreeNode;
/*  495 */       localObject2 = this;
/*      */     } else {
/*  497 */       k = i - j;
/*  498 */       localObject1 = this;
/*  499 */       localObject2 = paramDefaultMutableTreeNode;
/*      */     }
/*      */ 
/*  503 */     while (k > 0) {
/*  504 */       localObject1 = ((TreeNode)localObject1).getParent();
/*  505 */       k--;
/*      */     }
/*      */ 
/*      */     do
/*      */     {
/*  514 */       if (localObject1 == localObject2) {
/*  515 */         return localObject1;
/*      */       }
/*  517 */       localObject1 = ((TreeNode)localObject1).getParent();
/*  518 */       localObject2 = ((TreeNode)localObject2).getParent();
/*  519 */     }while (localObject1 != null);
/*      */ 
/*  522 */     if ((localObject1 != null) || (localObject2 != null)) {
/*  523 */       throw new Error("nodes should be null");
/*      */     }
/*      */ 
/*  526 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isNodeRelated(DefaultMutableTreeNode paramDefaultMutableTreeNode)
/*      */   {
/*  540 */     return (paramDefaultMutableTreeNode != null) && (getRoot() == paramDefaultMutableTreeNode.getRoot());
/*      */   }
/*      */ 
/*      */   public int getDepth()
/*      */   {
/*  555 */     Object localObject = null;
/*  556 */     Enumeration localEnumeration = breadthFirstEnumeration();
/*      */ 
/*  558 */     while (localEnumeration.hasMoreElements()) {
/*  559 */       localObject = localEnumeration.nextElement();
/*      */     }
/*      */ 
/*  562 */     if (localObject == null) {
/*  563 */       throw new Error("nodes should be null");
/*      */     }
/*      */ 
/*  566 */     return ((DefaultMutableTreeNode)localObject).getLevel() - getLevel();
/*      */   }
/*      */ 
/*      */   public int getLevel()
/*      */   {
/*  580 */     int i = 0;
/*      */ 
/*  582 */     Object localObject = this;
/*  583 */     while ((localObject = ((TreeNode)localObject).getParent()) != null) {
/*  584 */       i++;
/*      */     }
/*      */ 
/*  587 */     return i;
/*      */   }
/*      */ 
/*      */   public TreeNode[] getPath()
/*      */   {
/*  600 */     return getPathToRoot(this, 0);
/*      */   }
/*      */ 
/*      */   protected TreeNode[] getPathToRoot(TreeNode paramTreeNode, int paramInt)
/*      */   {
/*      */     TreeNode[] arrayOfTreeNode;
/*  620 */     if (paramTreeNode == null) {
/*  621 */       if (paramInt == 0) {
/*  622 */         return null;
/*      */       }
/*  624 */       arrayOfTreeNode = new TreeNode[paramInt];
/*      */     }
/*      */     else {
/*  627 */       paramInt++;
/*  628 */       arrayOfTreeNode = getPathToRoot(paramTreeNode.getParent(), paramInt);
/*  629 */       arrayOfTreeNode[(arrayOfTreeNode.length - paramInt)] = paramTreeNode;
/*      */     }
/*  631 */     return arrayOfTreeNode;
/*      */   }
/*      */ 
/*      */   public Object[] getUserObjectPath()
/*      */   {
/*  640 */     TreeNode[] arrayOfTreeNode = getPath();
/*  641 */     Object[] arrayOfObject = new Object[arrayOfTreeNode.length];
/*      */ 
/*  643 */     for (int i = 0; i < arrayOfTreeNode.length; i++) {
/*  644 */       arrayOfObject[i] = ((DefaultMutableTreeNode)arrayOfTreeNode[i]).getUserObject();
/*      */     }
/*  646 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public TreeNode getRoot()
/*      */   {
/*  657 */     Object localObject1 = this;
/*      */     Object localObject2;
/*      */     do
/*      */     {
/*  661 */       localObject2 = localObject1;
/*  662 */       localObject1 = ((TreeNode)localObject1).getParent();
/*  663 */     }while (localObject1 != null);
/*      */ 
/*  665 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public boolean isRoot()
/*      */   {
/*  677 */     return getParent() == null;
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getNextNode()
/*      */   {
/*  692 */     if (getChildCount() == 0)
/*      */     {
/*  694 */       DefaultMutableTreeNode localDefaultMutableTreeNode1 = getNextSibling();
/*      */ 
/*  696 */       if (localDefaultMutableTreeNode1 == null) {
/*  697 */         DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */         while (true)
/*      */         {
/*  700 */           if (localDefaultMutableTreeNode2 == null) {
/*  701 */             return null;
/*      */           }
/*      */ 
/*  704 */           localDefaultMutableTreeNode1 = localDefaultMutableTreeNode2.getNextSibling();
/*  705 */           if (localDefaultMutableTreeNode1 != null) {
/*  706 */             return localDefaultMutableTreeNode1;
/*      */           }
/*      */ 
/*  709 */           localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)localDefaultMutableTreeNode2.getParent();
/*      */         }
/*      */       }
/*  712 */       return localDefaultMutableTreeNode1;
/*      */     }
/*      */ 
/*  715 */     return (DefaultMutableTreeNode)getChildAt(0);
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getPreviousNode()
/*      */   {
/*  733 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */ 
/*  735 */     if (localDefaultMutableTreeNode2 == null) {
/*  736 */       return null;
/*      */     }
/*      */ 
/*  739 */     DefaultMutableTreeNode localDefaultMutableTreeNode1 = getPreviousSibling();
/*      */ 
/*  741 */     if (localDefaultMutableTreeNode1 != null) {
/*  742 */       if (localDefaultMutableTreeNode1.getChildCount() == 0) {
/*  743 */         return localDefaultMutableTreeNode1;
/*      */       }
/*  745 */       return localDefaultMutableTreeNode1.getLastLeaf();
/*      */     }
/*  747 */     return localDefaultMutableTreeNode2;
/*      */   }
/*      */ 
/*      */   public Enumeration preorderEnumeration()
/*      */   {
/*  763 */     return new PreorderEnumeration(this);
/*      */   }
/*      */ 
/*      */   public Enumeration postorderEnumeration()
/*      */   {
/*  780 */     return new PostorderEnumeration(this);
/*      */   }
/*      */ 
/*      */   public Enumeration breadthFirstEnumeration()
/*      */   {
/*  795 */     return new BreadthFirstEnumeration(this);
/*      */   }
/*      */ 
/*      */   public Enumeration depthFirstEnumeration()
/*      */   {
/*  812 */     return postorderEnumeration();
/*      */   }
/*      */ 
/*      */   public Enumeration pathFromAncestorEnumeration(TreeNode paramTreeNode)
/*      */   {
/*  836 */     return new PathBetweenNodesEnumeration(paramTreeNode, this);
/*      */   }
/*      */ 
/*      */   public boolean isNodeChild(TreeNode paramTreeNode)
/*      */   {
/*      */     boolean bool;
/*  854 */     if (paramTreeNode == null) {
/*  855 */       bool = false;
/*      */     }
/*  857 */     else if (getChildCount() == 0)
/*  858 */       bool = false;
/*      */     else {
/*  860 */       bool = paramTreeNode.getParent() == this;
/*      */     }
/*      */ 
/*  864 */     return bool;
/*      */   }
/*      */ 
/*      */   public TreeNode getFirstChild()
/*      */   {
/*  876 */     if (getChildCount() == 0) {
/*  877 */       throw new NoSuchElementException("node has no children");
/*      */     }
/*  879 */     return getChildAt(0);
/*      */   }
/*      */ 
/*      */   public TreeNode getLastChild()
/*      */   {
/*  891 */     if (getChildCount() == 0) {
/*  892 */       throw new NoSuchElementException("node has no children");
/*      */     }
/*  894 */     return getChildAt(getChildCount() - 1);
/*      */   }
/*      */ 
/*      */   public TreeNode getChildAfter(TreeNode paramTreeNode)
/*      */   {
/*  913 */     if (paramTreeNode == null) {
/*  914 */       throw new IllegalArgumentException("argument is null");
/*      */     }
/*      */ 
/*  917 */     int i = getIndex(paramTreeNode);
/*      */ 
/*  919 */     if (i == -1) {
/*  920 */       throw new IllegalArgumentException("node is not a child");
/*      */     }
/*      */ 
/*  923 */     if (i < getChildCount() - 1) {
/*  924 */       return getChildAt(i + 1);
/*      */     }
/*  926 */     return null;
/*      */   }
/*      */ 
/*      */   public TreeNode getChildBefore(TreeNode paramTreeNode)
/*      */   {
/*  944 */     if (paramTreeNode == null) {
/*  945 */       throw new IllegalArgumentException("argument is null");
/*      */     }
/*      */ 
/*  948 */     int i = getIndex(paramTreeNode);
/*      */ 
/*  950 */     if (i == -1) {
/*  951 */       throw new IllegalArgumentException("argument is not a child");
/*      */     }
/*      */ 
/*  954 */     if (i > 0) {
/*  955 */       return getChildAt(i - 1);
/*      */     }
/*  957 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isNodeSibling(TreeNode paramTreeNode)
/*      */   {
/*      */     boolean bool;
/*  978 */     if (paramTreeNode == null) {
/*  979 */       bool = false;
/*  980 */     } else if (paramTreeNode == this) {
/*  981 */       bool = true;
/*      */     } else {
/*  983 */       TreeNode localTreeNode = getParent();
/*  984 */       bool = (localTreeNode != null) && (localTreeNode == paramTreeNode.getParent());
/*      */ 
/*  986 */       if ((bool) && (!((DefaultMutableTreeNode)getParent()).isNodeChild(paramTreeNode)))
/*      */       {
/*  988 */         throw new Error("sibling has different parent");
/*      */       }
/*      */     }
/*      */ 
/*  992 */     return bool;
/*      */   }
/*      */ 
/*      */   public int getSiblingCount()
/*      */   {
/* 1004 */     TreeNode localTreeNode = getParent();
/*      */ 
/* 1006 */     if (localTreeNode == null) {
/* 1007 */       return 1;
/*      */     }
/* 1009 */     return localTreeNode.getChildCount();
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getNextSibling()
/*      */   {
/* 1027 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */     DefaultMutableTreeNode localDefaultMutableTreeNode1;
/* 1029 */     if (localDefaultMutableTreeNode2 == null)
/* 1030 */       localDefaultMutableTreeNode1 = null;
/*      */     else {
/* 1032 */       localDefaultMutableTreeNode1 = (DefaultMutableTreeNode)localDefaultMutableTreeNode2.getChildAfter(this);
/*      */     }
/*      */ 
/* 1035 */     if ((localDefaultMutableTreeNode1 != null) && (!isNodeSibling(localDefaultMutableTreeNode1))) {
/* 1036 */       throw new Error("child of parent is not a sibling");
/*      */     }
/*      */ 
/* 1039 */     return localDefaultMutableTreeNode1;
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getPreviousSibling()
/*      */   {
/* 1054 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */     DefaultMutableTreeNode localDefaultMutableTreeNode1;
/* 1056 */     if (localDefaultMutableTreeNode2 == null)
/* 1057 */       localDefaultMutableTreeNode1 = null;
/*      */     else {
/* 1059 */       localDefaultMutableTreeNode1 = (DefaultMutableTreeNode)localDefaultMutableTreeNode2.getChildBefore(this);
/*      */     }
/*      */ 
/* 1062 */     if ((localDefaultMutableTreeNode1 != null) && (!isNodeSibling(localDefaultMutableTreeNode1))) {
/* 1063 */       throw new Error("child of parent is not a sibling");
/*      */     }
/*      */ 
/* 1066 */     return localDefaultMutableTreeNode1;
/*      */   }
/*      */ 
/*      */   public boolean isLeaf()
/*      */   {
/* 1085 */     return getChildCount() == 0;
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getFirstLeaf()
/*      */   {
/* 1099 */     DefaultMutableTreeNode localDefaultMutableTreeNode = this;
/*      */ 
/* 1101 */     while (!localDefaultMutableTreeNode.isLeaf()) {
/* 1102 */       localDefaultMutableTreeNode = (DefaultMutableTreeNode)localDefaultMutableTreeNode.getFirstChild();
/*      */     }
/*      */ 
/* 1105 */     return localDefaultMutableTreeNode;
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getLastLeaf()
/*      */   {
/* 1119 */     DefaultMutableTreeNode localDefaultMutableTreeNode = this;
/*      */ 
/* 1121 */     while (!localDefaultMutableTreeNode.isLeaf()) {
/* 1122 */       localDefaultMutableTreeNode = (DefaultMutableTreeNode)localDefaultMutableTreeNode.getLastChild();
/*      */     }
/*      */ 
/* 1125 */     return localDefaultMutableTreeNode;
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getNextLeaf()
/*      */   {
/* 1150 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */ 
/* 1152 */     if (localDefaultMutableTreeNode2 == null) {
/* 1153 */       return null;
/*      */     }
/* 1155 */     DefaultMutableTreeNode localDefaultMutableTreeNode1 = getNextSibling();
/*      */ 
/* 1157 */     if (localDefaultMutableTreeNode1 != null) {
/* 1158 */       return localDefaultMutableTreeNode1.getFirstLeaf();
/*      */     }
/* 1160 */     return localDefaultMutableTreeNode2.getNextLeaf();
/*      */   }
/*      */ 
/*      */   public DefaultMutableTreeNode getPreviousLeaf()
/*      */   {
/* 1185 */     DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)getParent();
/*      */ 
/* 1187 */     if (localDefaultMutableTreeNode2 == null) {
/* 1188 */       return null;
/*      */     }
/* 1190 */     DefaultMutableTreeNode localDefaultMutableTreeNode1 = getPreviousSibling();
/*      */ 
/* 1192 */     if (localDefaultMutableTreeNode1 != null) {
/* 1193 */       return localDefaultMutableTreeNode1.getLastLeaf();
/*      */     }
/* 1195 */     return localDefaultMutableTreeNode2.getPreviousLeaf();
/*      */   }
/*      */ 
/*      */   public int getLeafCount()
/*      */   {
/* 1208 */     int i = 0;
/*      */ 
/* 1211 */     Enumeration localEnumeration = breadthFirstEnumeration();
/*      */ 
/* 1213 */     while (localEnumeration.hasMoreElements()) {
/* 1214 */       TreeNode localTreeNode = (TreeNode)localEnumeration.nextElement();
/* 1215 */       if (localTreeNode.isLeaf()) {
/* 1216 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1220 */     if (i < 1) {
/* 1221 */       throw new Error("tree has zero leaves");
/*      */     }
/*      */ 
/* 1224 */     return i;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1239 */     if (this.userObject == null) {
/* 1240 */       return "";
/*      */     }
/* 1242 */     return this.userObject.toString();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     DefaultMutableTreeNode localDefaultMutableTreeNode;
/*      */     try
/*      */     {
/* 1257 */       localDefaultMutableTreeNode = (DefaultMutableTreeNode)super.clone();
/*      */ 
/* 1260 */       localDefaultMutableTreeNode.children = null;
/* 1261 */       localDefaultMutableTreeNode.parent = null;
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException)
/*      */     {
/* 1265 */       throw new Error(localCloneNotSupportedException.toString());
/*      */     }
/*      */ 
/* 1268 */     return localDefaultMutableTreeNode;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1276 */     paramObjectOutputStream.defaultWriteObject();
/*      */     Object[] arrayOfObject;
/* 1278 */     if ((this.userObject != null) && ((this.userObject instanceof Serializable))) {
/* 1279 */       arrayOfObject = new Object[2];
/* 1280 */       arrayOfObject[0] = "userObject";
/* 1281 */       arrayOfObject[1] = this.userObject;
/*      */     }
/*      */     else {
/* 1284 */       arrayOfObject = new Object[0];
/* 1285 */     }paramObjectOutputStream.writeObject(arrayOfObject);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1292 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1294 */     Object[] arrayOfObject = (Object[])paramObjectInputStream.readObject();
/*      */ 
/* 1296 */     if ((arrayOfObject.length > 0) && (arrayOfObject[0].equals("userObject")))
/* 1297 */       this.userObject = arrayOfObject[1];
/*      */   }
/*      */ 
/*      */   final class BreadthFirstEnumeration
/*      */     implements Enumeration<TreeNode>
/*      */   {
/*      */     protected Queue queue;
/*      */ 
/*      */     public BreadthFirstEnumeration(TreeNode arg2)
/*      */     {
/* 1373 */       Vector localVector = new Vector(1);
/*      */       Object localObject;
/* 1374 */       localVector.addElement(localObject);
/* 1375 */       this.queue = new Queue();
/* 1376 */       this.queue.enqueue(localVector.elements());
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 1380 */       return (!this.queue.isEmpty()) && (((Enumeration)this.queue.firstObject()).hasMoreElements());
/*      */     }
/*      */ 
/*      */     public TreeNode nextElement()
/*      */     {
/* 1385 */       Enumeration localEnumeration1 = (Enumeration)this.queue.firstObject();
/* 1386 */       TreeNode localTreeNode = (TreeNode)localEnumeration1.nextElement();
/* 1387 */       Enumeration localEnumeration2 = localTreeNode.children();
/*      */ 
/* 1389 */       if (!localEnumeration1.hasMoreElements()) {
/* 1390 */         this.queue.dequeue();
/*      */       }
/* 1392 */       if (localEnumeration2.hasMoreElements()) {
/* 1393 */         this.queue.enqueue(localEnumeration2);
/*      */       }
/* 1395 */       return localTreeNode;
/*      */     }
/*      */ 
/*      */     final class Queue
/*      */     {
/*      */       QNode head;
/*      */       QNode tail;
/*      */ 
/*      */       Queue()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void enqueue(Object paramObject)
/*      */       {
/* 1414 */         if (this.head == null) {
/* 1415 */           this.head = (this.tail = new QNode(paramObject, null));
/*      */         } else {
/* 1417 */           this.tail.next = new QNode(paramObject, null);
/* 1418 */           this.tail = this.tail.next;
/*      */         }
/*      */       }
/*      */ 
/*      */       public Object dequeue() {
/* 1423 */         if (this.head == null) {
/* 1424 */           throw new NoSuchElementException("No more elements");
/*      */         }
/*      */ 
/* 1427 */         Object localObject = this.head.object;
/* 1428 */         QNode localQNode = this.head;
/* 1429 */         this.head = this.head.next;
/* 1430 */         if (this.head == null)
/* 1431 */           this.tail = null;
/*      */         else {
/* 1433 */           localQNode.next = null;
/*      */         }
/* 1435 */         return localObject;
/*      */       }
/*      */ 
/*      */       public Object firstObject() {
/* 1439 */         if (this.head == null) {
/* 1440 */           throw new NoSuchElementException("No more elements");
/*      */         }
/*      */ 
/* 1443 */         return this.head.object;
/*      */       }
/*      */ 
/*      */       public boolean isEmpty() {
/* 1447 */         return this.head == null;
/*      */       }
/*      */ 
/*      */       final class QNode
/*      */       {
/*      */         public Object object;
/*      */         public QNode next;
/*      */ 
/*      */         public QNode(Object paramQNode, QNode arg3)
/*      */         {
/* 1408 */           this.object = paramQNode;
/*      */           Object localObject;
/* 1409 */           this.next = localObject;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class PathBetweenNodesEnumeration
/*      */     implements Enumeration<TreeNode>
/*      */   {
/*      */     protected Stack<TreeNode> stack;
/*      */ 
/*      */     public PathBetweenNodesEnumeration(TreeNode paramTreeNode1, TreeNode arg3)
/*      */     {
/*      */       Object localObject1;
/* 1464 */       if ((paramTreeNode1 == null) || (localObject1 == null)) {
/* 1465 */         throw new IllegalArgumentException("argument is null");
/*      */       }
/*      */ 
/* 1470 */       this.stack = new Stack();
/* 1471 */       this.stack.push(localObject1);
/*      */ 
/* 1473 */       Object localObject2 = localObject1;
/* 1474 */       while (localObject2 != paramTreeNode1) {
/* 1475 */         localObject2 = ((TreeNode)localObject2).getParent();
/* 1476 */         if ((localObject2 == null) && (localObject1 != paramTreeNode1)) {
/* 1477 */           throw new IllegalArgumentException("node " + paramTreeNode1 + " is not an ancestor of " + localObject1);
/*      */         }
/*      */ 
/* 1480 */         this.stack.push(localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 1485 */       return this.stack.size() > 0;
/*      */     }
/*      */ 
/*      */     public TreeNode nextElement() {
/*      */       try {
/* 1490 */         return (TreeNode)this.stack.pop(); } catch (EmptyStackException localEmptyStackException) {
/*      */       }
/* 1492 */       throw new NoSuchElementException("No more elements");
/*      */     }
/*      */   }
/*      */ 
/*      */   final class PostorderEnumeration
/*      */     implements Enumeration<TreeNode>
/*      */   {
/*      */     protected TreeNode root;
/*      */     protected Enumeration<TreeNode> children;
/*      */     protected Enumeration<TreeNode> subtree;
/*      */ 
/*      */     public PostorderEnumeration(TreeNode arg2)
/*      */     {
/*      */       Object localObject;
/* 1339 */       this.root = localObject;
/* 1340 */       this.children = this.root.children();
/* 1341 */       this.subtree = DefaultMutableTreeNode.EMPTY_ENUMERATION;
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 1345 */       return this.root != null;
/*      */     }
/*      */ 
/*      */     public TreeNode nextElement()
/*      */     {
/*      */       TreeNode localTreeNode;
/* 1351 */       if (this.subtree.hasMoreElements()) {
/* 1352 */         localTreeNode = (TreeNode)this.subtree.nextElement();
/* 1353 */       } else if (this.children.hasMoreElements()) {
/* 1354 */         this.subtree = new PostorderEnumeration(DefaultMutableTreeNode.this, (TreeNode)this.children.nextElement());
/* 1355 */         localTreeNode = (TreeNode)this.subtree.nextElement();
/*      */       } else {
/* 1357 */         localTreeNode = this.root;
/* 1358 */         this.root = null;
/*      */       }
/*      */ 
/* 1361 */       return localTreeNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class PreorderEnumeration
/*      */     implements Enumeration<TreeNode>
/*      */   {
/* 1301 */     private final Stack<Enumeration> stack = new Stack();
/*      */ 
/*      */     public PreorderEnumeration(TreeNode arg2)
/*      */     {
/* 1305 */       Vector localVector = new Vector(1);
/*      */       Object localObject;
/* 1306 */       localVector.addElement(localObject);
/* 1307 */       this.stack.push(localVector.elements());
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 1311 */       return (!this.stack.empty()) && (((Enumeration)this.stack.peek()).hasMoreElements());
/*      */     }
/*      */ 
/*      */     public TreeNode nextElement() {
/* 1315 */       Enumeration localEnumeration1 = (Enumeration)this.stack.peek();
/* 1316 */       TreeNode localTreeNode = (TreeNode)localEnumeration1.nextElement();
/* 1317 */       Enumeration localEnumeration2 = localTreeNode.children();
/*      */ 
/* 1319 */       if (!localEnumeration1.hasMoreElements()) {
/* 1320 */         this.stack.pop();
/*      */       }
/* 1322 */       if (localEnumeration2.hasMoreElements()) {
/* 1323 */         this.stack.push(localEnumeration2);
/*      */       }
/* 1325 */       return localTreeNode;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.DefaultMutableTreeNode
 * JD-Core Version:    0.6.2
 */