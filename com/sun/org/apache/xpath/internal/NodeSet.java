/*      */ package com.sun.org.apache.xpath.internal;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.DOM2Helper;
/*      */ import com.sun.org.apache.xpath.internal.axes.ContextNodeList;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.traversal.NodeFilter;
/*      */ import org.w3c.dom.traversal.NodeIterator;
/*      */ 
/*      */ public class NodeSet
/*      */   implements NodeList, NodeIterator, Cloneable, ContextNodeList
/*      */ {
/*  720 */   protected transient int m_next = 0;
/*      */ 
/*  772 */   protected transient boolean m_mutable = true;
/*      */ 
/*  776 */   protected transient boolean m_cacheNodes = true;
/*      */ 
/*  812 */   private transient int m_last = 0;
/*      */   private int m_blocksize;
/*      */   Node[] m_map;
/*  834 */   protected int m_firstFree = 0;
/*      */   private int m_mapSize;
/*      */ 
/*      */   public NodeSet()
/*      */   {
/*   70 */     this.m_blocksize = 32;
/*   71 */     this.m_mapSize = 0;
/*      */   }
/*      */ 
/*      */   public NodeSet(int blocksize)
/*      */   {
/*   81 */     this.m_blocksize = blocksize;
/*   82 */     this.m_mapSize = 0;
/*      */   }
/*      */ 
/*      */   public NodeSet(NodeList nodelist)
/*      */   {
/*   94 */     this(32);
/*      */ 
/*   96 */     addNodes(nodelist);
/*      */   }
/*      */ 
/*      */   public NodeSet(NodeSet nodelist)
/*      */   {
/*  108 */     this(32);
/*      */ 
/*  110 */     addNodes(nodelist);
/*      */   }
/*      */ 
/*      */   public NodeSet(NodeIterator ni)
/*      */   {
/*  122 */     this(32);
/*      */ 
/*  124 */     addNodes(ni);
/*      */   }
/*      */ 
/*      */   public NodeSet(Node node)
/*      */   {
/*  135 */     this(32);
/*      */ 
/*  137 */     addNode(node);
/*      */   }
/*      */ 
/*      */   public Node getRoot()
/*      */   {
/*  146 */     return null;
/*      */   }
/*      */ 
/*      */   public NodeIterator cloneWithReset()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  162 */     NodeSet clone = (NodeSet)clone();
/*      */ 
/*  164 */     clone.reset();
/*      */ 
/*  166 */     return clone;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  174 */     this.m_next = 0;
/*      */   }
/*      */ 
/*      */   public int getWhatToShow()
/*      */   {
/*  191 */     return -17;
/*      */   }
/*      */ 
/*      */   public NodeFilter getFilter()
/*      */   {
/*  209 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getExpandEntityReferences()
/*      */   {
/*  230 */     return true;
/*      */   }
/*      */ 
/*      */   public Node nextNode()
/*      */     throws DOMException
/*      */   {
/*  246 */     if (this.m_next < size())
/*      */     {
/*  248 */       Node next = elementAt(this.m_next);
/*      */ 
/*  250 */       this.m_next += 1;
/*      */ 
/*  252 */       return next;
/*      */     }
/*      */ 
/*  255 */     return null;
/*      */   }
/*      */ 
/*      */   public Node previousNode()
/*      */     throws DOMException
/*      */   {
/*  272 */     if (!this.m_cacheNodes) {
/*  273 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_ITERATE", null));
/*      */     }
/*      */ 
/*  276 */     if (this.m_next - 1 > 0)
/*      */     {
/*  278 */       this.m_next -= 1;
/*      */ 
/*  280 */       return elementAt(this.m_next);
/*      */     }
/*      */ 
/*  283 */     return null;
/*      */   }
/*      */ 
/*      */   public void detach()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isFresh()
/*      */   {
/*  309 */     return this.m_next == 0;
/*      */   }
/*      */ 
/*      */   public void runTo(int index)
/*      */   {
/*  327 */     if (!this.m_cacheNodes) {
/*  328 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
/*      */     }
/*      */ 
/*  331 */     if ((index >= 0) && (this.m_next < this.m_firstFree))
/*  332 */       this.m_next = index;
/*      */     else
/*  334 */       this.m_next = (this.m_firstFree - 1);
/*      */   }
/*      */ 
/*      */   public Node item(int index)
/*      */   {
/*  352 */     runTo(index);
/*      */ 
/*  354 */     return elementAt(index);
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  368 */     runTo(-1);
/*      */ 
/*  370 */     return size();
/*      */   }
/*      */ 
/*      */   public void addNode(Node n)
/*      */   {
/*  384 */     if (!this.m_mutable) {
/*  385 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  387 */     addElement(n);
/*      */   }
/*      */ 
/*      */   public void insertNode(Node n, int pos)
/*      */   {
/*  402 */     if (!this.m_mutable) {
/*  403 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  405 */     insertElementAt(n, pos);
/*      */   }
/*      */ 
/*      */   public void removeNode(Node n)
/*      */   {
/*  418 */     if (!this.m_mutable) {
/*  419 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  421 */     removeElement(n);
/*      */   }
/*      */ 
/*      */   public void addNodes(NodeList nodelist)
/*      */   {
/*  436 */     if (!this.m_mutable) {
/*  437 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  439 */     if (null != nodelist)
/*      */     {
/*  441 */       int nChildren = nodelist.getLength();
/*      */ 
/*  443 */       for (int i = 0; i < nChildren; i++)
/*      */       {
/*  445 */         Node obj = nodelist.item(i);
/*      */ 
/*  447 */         if (null != obj)
/*      */         {
/*  449 */           addElement(obj);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNodes(NodeSet ns)
/*      */   {
/*  476 */     if (!this.m_mutable) {
/*  477 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  479 */     addNodes(ns);
/*      */   }
/*      */ 
/*      */   public void addNodes(NodeIterator iterator)
/*      */   {
/*  493 */     if (!this.m_mutable) {
/*  494 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  496 */     if (null != iterator)
/*      */     {
/*      */       Node obj;
/*  500 */       while (null != (obj = iterator.nextNode()))
/*      */       {
/*  502 */         addElement(obj);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNodesInDocOrder(NodeList nodelist, XPathContext support)
/*      */   {
/*  521 */     if (!this.m_mutable) {
/*  522 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  524 */     int nChildren = nodelist.getLength();
/*      */ 
/*  526 */     for (int i = 0; i < nChildren; i++)
/*      */     {
/*  528 */       Node node = nodelist.item(i);
/*      */ 
/*  530 */       if (null != node)
/*      */       {
/*  532 */         addNodeInDocOrder(node, support);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNodesInDocOrder(NodeIterator iterator, XPathContext support)
/*      */   {
/*  549 */     if (!this.m_mutable)
/*  550 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     Node node;
/*  554 */     while (null != (node = iterator.nextNode()))
/*      */     {
/*  556 */       addNodeInDocOrder(node, support);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean addNodesInDocOrder(int start, int end, int testIndex, NodeList nodelist, XPathContext support)
/*      */   {
/*  577 */     if (!this.m_mutable) {
/*  578 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  580 */     boolean foundit = false;
/*      */ 
/*  582 */     Node node = nodelist.item(testIndex);
/*      */ 
/*  584 */     for (int i = end; i >= start; i--)
/*      */     {
/*  586 */       Node child = elementAt(i);
/*      */ 
/*  588 */       if (child == node)
/*      */       {
/*  590 */         i = -2;
/*      */ 
/*  592 */         break;
/*      */       }
/*      */ 
/*  595 */       if (!DOM2Helper.isNodeAfter(node, child))
/*      */       {
/*  597 */         insertElementAt(node, i + 1);
/*      */ 
/*  599 */         testIndex--;
/*      */ 
/*  601 */         if (testIndex <= 0)
/*      */           break;
/*  603 */         boolean foundPrev = addNodesInDocOrder(0, i, testIndex, nodelist, support);
/*      */ 
/*  606 */         if (!foundPrev)
/*      */         {
/*  608 */           addNodesInDocOrder(i, size() - 1, testIndex, nodelist, support);
/*      */         }
/*  610 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  616 */     if (i == -1)
/*      */     {
/*  618 */       insertElementAt(node, 0);
/*      */     }
/*      */ 
/*  621 */     return foundit;
/*      */   }
/*      */ 
/*      */   public int addNodeInDocOrder(Node node, boolean test, XPathContext support)
/*      */   {
/*  637 */     if (!this.m_mutable) {
/*  638 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  640 */     int insertIndex = -1;
/*      */ 
/*  642 */     if (test)
/*      */     {
/*  648 */       int size = size();
/*      */ 
/*  650 */       for (int i = size - 1; i >= 0; i--)
/*      */       {
/*  652 */         Node child = elementAt(i);
/*      */ 
/*  654 */         if (child == node)
/*      */         {
/*  656 */           i = -2;
/*      */         }
/*      */         else
/*      */         {
/*  661 */           if (!DOM2Helper.isNodeAfter(node, child))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  667 */       if (i != -2)
/*      */       {
/*  669 */         insertIndex = i + 1;
/*      */ 
/*  671 */         insertElementAt(node, insertIndex);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  676 */       insertIndex = size();
/*      */ 
/*  678 */       boolean foundit = false;
/*      */ 
/*  680 */       for (int i = 0; i < insertIndex; i++)
/*      */       {
/*  682 */         if (item(i).equals(node))
/*      */         {
/*  684 */           foundit = true;
/*      */ 
/*  686 */           break;
/*      */         }
/*      */       }
/*      */ 
/*  690 */       if (!foundit) {
/*  691 */         addElement(node);
/*      */       }
/*      */     }
/*      */ 
/*  695 */     return insertIndex;
/*      */   }
/*      */ 
/*      */   public int addNodeInDocOrder(Node node, XPathContext support)
/*      */   {
/*  711 */     if (!this.m_mutable) {
/*  712 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  714 */     return addNodeInDocOrder(node, true, support);
/*      */   }
/*      */ 
/*      */   public int getCurrentPos()
/*      */   {
/*  732 */     return this.m_next;
/*      */   }
/*      */ 
/*      */   public void setCurrentPos(int i)
/*      */   {
/*  744 */     if (!this.m_cacheNodes) {
/*  745 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
/*      */     }
/*      */ 
/*  748 */     this.m_next = i;
/*      */   }
/*      */ 
/*      */   public Node getCurrentNode()
/*      */   {
/*  761 */     if (!this.m_cacheNodes) {
/*  762 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_CANNOT_INDEX", null));
/*      */     }
/*      */ 
/*  765 */     int saved = this.m_next;
/*  766 */     Node n = this.m_next < this.m_firstFree ? elementAt(this.m_next) : null;
/*  767 */     this.m_next = saved;
/*  768 */     return n;
/*      */   }
/*      */ 
/*      */   public boolean getShouldCacheNodes()
/*      */   {
/*  786 */     return this.m_cacheNodes;
/*      */   }
/*      */ 
/*      */   public void setShouldCacheNodes(boolean b)
/*      */   {
/*  803 */     if (!isFresh()) {
/*  804 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
/*      */     }
/*      */ 
/*  807 */     this.m_cacheNodes = b;
/*  808 */     this.m_mutable = true;
/*      */   }
/*      */ 
/*      */   public int getLast()
/*      */   {
/*  816 */     return this.m_last;
/*      */   }
/*      */ 
/*      */   public void setLast(int last)
/*      */   {
/*  821 */     this.m_last = last;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  850 */     NodeSet clone = (NodeSet)super.clone();
/*      */ 
/*  852 */     if ((null != this.m_map) && (this.m_map == clone.m_map))
/*      */     {
/*  854 */       clone.m_map = new Node[this.m_map.length];
/*      */ 
/*  856 */       System.arraycopy(this.m_map, 0, clone.m_map, 0, this.m_map.length);
/*      */     }
/*      */ 
/*  859 */     return clone;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  869 */     return this.m_firstFree;
/*      */   }
/*      */ 
/*      */   public void addElement(Node value)
/*      */   {
/*  879 */     if (!this.m_mutable) {
/*  880 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/*  882 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*      */     {
/*  884 */       if (null == this.m_map)
/*      */       {
/*  886 */         this.m_map = new Node[this.m_blocksize];
/*  887 */         this.m_mapSize = this.m_blocksize;
/*      */       }
/*      */       else
/*      */       {
/*  891 */         this.m_mapSize += this.m_blocksize;
/*      */ 
/*  893 */         Node[] newMap = new Node[this.m_mapSize];
/*      */ 
/*  895 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*      */ 
/*  897 */         this.m_map = newMap;
/*      */       }
/*      */     }
/*      */ 
/*  901 */     this.m_map[this.m_firstFree] = value;
/*      */ 
/*  903 */     this.m_firstFree += 1;
/*      */   }
/*      */ 
/*      */   public final void push(Node value)
/*      */   {
/*  914 */     int ff = this.m_firstFree;
/*      */ 
/*  916 */     if (ff + 1 >= this.m_mapSize)
/*      */     {
/*  918 */       if (null == this.m_map)
/*      */       {
/*  920 */         this.m_map = new Node[this.m_blocksize];
/*  921 */         this.m_mapSize = this.m_blocksize;
/*      */       }
/*      */       else
/*      */       {
/*  925 */         this.m_mapSize += this.m_blocksize;
/*      */ 
/*  927 */         Node[] newMap = new Node[this.m_mapSize];
/*      */ 
/*  929 */         System.arraycopy(this.m_map, 0, newMap, 0, ff + 1);
/*      */ 
/*  931 */         this.m_map = newMap;
/*      */       }
/*      */     }
/*      */ 
/*  935 */     this.m_map[ff] = value;
/*      */ 
/*  937 */     ff++;
/*      */ 
/*  939 */     this.m_firstFree = ff;
/*      */   }
/*      */ 
/*      */   public final Node pop()
/*      */   {
/*  950 */     this.m_firstFree -= 1;
/*      */ 
/*  952 */     Node n = this.m_map[this.m_firstFree];
/*      */ 
/*  954 */     this.m_map[this.m_firstFree] = null;
/*      */ 
/*  956 */     return n;
/*      */   }
/*      */ 
/*      */   public final Node popAndTop()
/*      */   {
/*  968 */     this.m_firstFree -= 1;
/*      */ 
/*  970 */     this.m_map[this.m_firstFree] = null;
/*      */ 
/*  972 */     return this.m_firstFree == 0 ? null : this.m_map[(this.m_firstFree - 1)];
/*      */   }
/*      */ 
/*      */   public final void popQuick()
/*      */   {
/*  981 */     this.m_firstFree -= 1;
/*      */ 
/*  983 */     this.m_map[this.m_firstFree] = null;
/*      */   }
/*      */ 
/*      */   public final Node peepOrNull()
/*      */   {
/*  995 */     return (null != this.m_map) && (this.m_firstFree > 0) ? this.m_map[(this.m_firstFree - 1)] : null;
/*      */   }
/*      */ 
/*      */   public final void pushPair(Node v1, Node v2)
/*      */   {
/* 1010 */     if (null == this.m_map)
/*      */     {
/* 1012 */       this.m_map = new Node[this.m_blocksize];
/* 1013 */       this.m_mapSize = this.m_blocksize;
/*      */     }
/* 1017 */     else if (this.m_firstFree + 2 >= this.m_mapSize)
/*      */     {
/* 1019 */       this.m_mapSize += this.m_blocksize;
/*      */ 
/* 1021 */       Node[] newMap = new Node[this.m_mapSize];
/*      */ 
/* 1023 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
/*      */ 
/* 1025 */       this.m_map = newMap;
/*      */     }
/*      */ 
/* 1029 */     this.m_map[this.m_firstFree] = v1;
/* 1030 */     this.m_map[(this.m_firstFree + 1)] = v2;
/* 1031 */     this.m_firstFree += 2;
/*      */   }
/*      */ 
/*      */   public final void popPair()
/*      */   {
/* 1042 */     this.m_firstFree -= 2;
/* 1043 */     this.m_map[this.m_firstFree] = null;
/* 1044 */     this.m_map[(this.m_firstFree + 1)] = null;
/*      */   }
/*      */ 
/*      */   public final void setTail(Node n)
/*      */   {
/* 1056 */     this.m_map[(this.m_firstFree - 1)] = n;
/*      */   }
/*      */ 
/*      */   public final void setTailSub1(Node n)
/*      */   {
/* 1068 */     this.m_map[(this.m_firstFree - 2)] = n;
/*      */   }
/*      */ 
/*      */   public final Node peepTail()
/*      */   {
/* 1080 */     return this.m_map[(this.m_firstFree - 1)];
/*      */   }
/*      */ 
/*      */   public final Node peepTailSub1()
/*      */   {
/* 1092 */     return this.m_map[(this.m_firstFree - 2)];
/*      */   }
/*      */ 
/*      */   public void insertElementAt(Node value, int at)
/*      */   {
/* 1106 */     if (!this.m_mutable) {
/* 1107 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/* 1109 */     if (null == this.m_map)
/*      */     {
/* 1111 */       this.m_map = new Node[this.m_blocksize];
/* 1112 */       this.m_mapSize = this.m_blocksize;
/*      */     }
/* 1114 */     else if (this.m_firstFree + 1 >= this.m_mapSize)
/*      */     {
/* 1116 */       this.m_mapSize += this.m_blocksize;
/*      */ 
/* 1118 */       Node[] newMap = new Node[this.m_mapSize];
/*      */ 
/* 1120 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*      */ 
/* 1122 */       this.m_map = newMap;
/*      */     }
/*      */ 
/* 1125 */     if (at <= this.m_firstFree - 1)
/*      */     {
/* 1127 */       System.arraycopy(this.m_map, at, this.m_map, at + 1, this.m_firstFree - at);
/*      */     }
/*      */ 
/* 1130 */     this.m_map[at] = value;
/*      */ 
/* 1132 */     this.m_firstFree += 1;
/*      */   }
/*      */ 
/*      */   public void appendNodes(NodeSet nodes)
/*      */   {
/* 1143 */     int nNodes = nodes.size();
/*      */ 
/* 1145 */     if (null == this.m_map)
/*      */     {
/* 1147 */       this.m_mapSize = (nNodes + this.m_blocksize);
/* 1148 */       this.m_map = new Node[this.m_mapSize];
/*      */     }
/* 1150 */     else if (this.m_firstFree + nNodes >= this.m_mapSize)
/*      */     {
/* 1152 */       this.m_mapSize += nNodes + this.m_blocksize;
/*      */ 
/* 1154 */       Node[] newMap = new Node[this.m_mapSize];
/*      */ 
/* 1156 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + nNodes);
/*      */ 
/* 1158 */       this.m_map = newMap;
/*      */     }
/*      */ 
/* 1161 */     System.arraycopy(nodes.m_map, 0, this.m_map, this.m_firstFree, nNodes);
/*      */ 
/* 1163 */     this.m_firstFree += nNodes;
/*      */   }
/*      */ 
/*      */   public void removeAllElements()
/*      */   {
/* 1175 */     if (null == this.m_map) {
/* 1176 */       return;
/*      */     }
/* 1178 */     for (int i = 0; i < this.m_firstFree; i++)
/*      */     {
/* 1180 */       this.m_map[i] = null;
/*      */     }
/*      */ 
/* 1183 */     this.m_firstFree = 0;
/*      */   }
/*      */ 
/*      */   public boolean removeElement(Node s)
/*      */   {
/* 1199 */     if (!this.m_mutable) {
/* 1200 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/* 1202 */     if (null == this.m_map) {
/* 1203 */       return false;
/*      */     }
/* 1205 */     for (int i = 0; i < this.m_firstFree; i++)
/*      */     {
/* 1207 */       Node node = this.m_map[i];
/*      */ 
/* 1209 */       if ((null != node) && (node.equals(s)))
/*      */       {
/* 1211 */         if (i < this.m_firstFree - 1) {
/* 1212 */           System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree - i - 1);
/*      */         }
/* 1214 */         this.m_firstFree -= 1;
/* 1215 */         this.m_map[this.m_firstFree] = null;
/*      */ 
/* 1217 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1221 */     return false;
/*      */   }
/*      */ 
/*      */   public void removeElementAt(int i)
/*      */   {
/* 1235 */     if (null == this.m_map) {
/* 1236 */       return;
/*      */     }
/* 1238 */     if (i >= this.m_firstFree)
/* 1239 */       throw new ArrayIndexOutOfBoundsException(i + " >= " + this.m_firstFree);
/* 1240 */     if (i < 0) {
/* 1241 */       throw new ArrayIndexOutOfBoundsException(i);
/*      */     }
/* 1243 */     if (i < this.m_firstFree - 1) {
/* 1244 */       System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree - i - 1);
/*      */     }
/* 1246 */     this.m_firstFree -= 1;
/* 1247 */     this.m_map[this.m_firstFree] = null;
/*      */   }
/*      */ 
/*      */   public void setElementAt(Node node, int index)
/*      */   {
/* 1262 */     if (!this.m_mutable) {
/* 1263 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESET_NOT_MUTABLE", null));
/*      */     }
/* 1265 */     if (null == this.m_map)
/*      */     {
/* 1267 */       this.m_map = new Node[this.m_blocksize];
/* 1268 */       this.m_mapSize = this.m_blocksize;
/*      */     }
/*      */ 
/* 1271 */     this.m_map[index] = node;
/*      */   }
/*      */ 
/*      */   public Node elementAt(int i)
/*      */   {
/* 1284 */     if (null == this.m_map) {
/* 1285 */       return null;
/*      */     }
/* 1287 */     return this.m_map[i];
/*      */   }
/*      */ 
/*      */   public boolean contains(Node s)
/*      */   {
/* 1299 */     runTo(-1);
/*      */ 
/* 1301 */     if (null == this.m_map) {
/* 1302 */       return false;
/*      */     }
/* 1304 */     for (int i = 0; i < this.m_firstFree; i++)
/*      */     {
/* 1306 */       Node node = this.m_map[i];
/*      */ 
/* 1308 */       if ((null != node) && (node.equals(s))) {
/* 1309 */         return true;
/*      */       }
/*      */     }
/* 1312 */     return false;
/*      */   }
/*      */ 
/*      */   public int indexOf(Node elem, int index)
/*      */   {
/* 1328 */     runTo(-1);
/*      */ 
/* 1330 */     if (null == this.m_map) {
/* 1331 */       return -1;
/*      */     }
/* 1333 */     for (int i = index; i < this.m_firstFree; i++)
/*      */     {
/* 1335 */       Node node = this.m_map[i];
/*      */ 
/* 1337 */       if ((null != node) && (node.equals(elem))) {
/* 1338 */         return i;
/*      */       }
/*      */     }
/* 1341 */     return -1;
/*      */   }
/*      */ 
/*      */   public int indexOf(Node elem)
/*      */   {
/* 1356 */     runTo(-1);
/*      */ 
/* 1358 */     if (null == this.m_map) {
/* 1359 */       return -1;
/*      */     }
/* 1361 */     for (int i = 0; i < this.m_firstFree; i++)
/*      */     {
/* 1363 */       Node node = this.m_map[i];
/*      */ 
/* 1365 */       if ((null != node) && (node.equals(elem))) {
/* 1366 */         return i;
/*      */       }
/*      */     }
/* 1369 */     return -1;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.NodeSet
 * JD-Core Version:    0.6.2
 */