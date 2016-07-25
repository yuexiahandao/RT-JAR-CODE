/*      */ package com.sun.org.apache.xpath.internal;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.utils.NodeVector;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.traversal.NodeIterator;
/*      */ 
/*      */ public class NodeSetDTM extends NodeVector
/*      */   implements DTMIterator, Cloneable
/*      */ {
/*      */   static final long serialVersionUID = 7686480133331317070L;
/*      */   DTMManager m_manager;
/* 1105 */   protected transient int m_next = 0;
/*      */ 
/* 1161 */   protected transient boolean m_mutable = true;
/*      */ 
/* 1165 */   protected transient boolean m_cacheNodes = true;
/*      */ 
/* 1168 */   protected int m_root = -1;
/*      */ 
/* 1214 */   private transient int m_last = 0;
/*      */ 
/*      */   public NodeSetDTM(DTMManager dtmManager)
/*      */   {
/*   74 */     this.m_manager = dtmManager;
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(int blocksize, int dummy, DTMManager dtmManager)
/*      */   {
/*   85 */     super(blocksize);
/*   86 */     this.m_manager = dtmManager;
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(NodeSetDTM nodelist)
/*      */   {
/*  114 */     this.m_manager = nodelist.getDTMManager();
/*  115 */     this.m_root = nodelist.getRoot();
/*      */ 
/*  117 */     addNodes(nodelist);
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(DTMIterator ni)
/*      */   {
/*  131 */     this.m_manager = ni.getDTMManager();
/*  132 */     this.m_root = ni.getRoot();
/*  133 */     addNodes(ni);
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(NodeIterator iterator, XPathContext xctxt)
/*      */   {
/*  148 */     this.m_manager = xctxt.getDTMManager();
/*      */     Node node;
/*  150 */     while (null != (node = iterator.nextNode()))
/*      */     {
/*  152 */       int handle = xctxt.getDTMHandleFromNode(node);
/*  153 */       addNodeInDocOrder(handle, xctxt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(NodeList nodeList, XPathContext xctxt)
/*      */   {
/*  167 */     this.m_manager = xctxt.getDTMManager();
/*      */ 
/*  169 */     int n = nodeList.getLength();
/*  170 */     for (int i = 0; i < n; i++)
/*      */     {
/*  172 */       Node node = nodeList.item(i);
/*  173 */       int handle = xctxt.getDTMHandleFromNode(node);
/*      */ 
/*  175 */       addNode(handle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NodeSetDTM(int node, DTMManager dtmManager)
/*      */   {
/*  189 */     this.m_manager = dtmManager;
/*      */ 
/*  191 */     addNode(node);
/*      */   }
/*      */ 
/*      */   public void setEnvironment(Object environment)
/*      */   {
/*      */   }
/*      */ 
/*      */   public int getRoot()
/*      */   {
/*  219 */     if (-1 == this.m_root)
/*      */     {
/*  221 */       if (size() > 0) {
/*  222 */         return item(0);
/*      */       }
/*  224 */       return -1;
/*      */     }
/*      */ 
/*  227 */     return this.m_root;
/*      */   }
/*      */ 
/*      */   public void setRoot(int context, Object environment)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  257 */     NodeSetDTM clone = (NodeSetDTM)super.clone();
/*      */ 
/*  259 */     return clone;
/*      */   }
/*      */ 
/*      */   public DTMIterator cloneWithReset()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  275 */     NodeSetDTM clone = (NodeSetDTM)clone();
/*      */ 
/*  277 */     clone.reset();
/*      */ 
/*  279 */     return clone;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  287 */     this.m_next = 0;
/*      */   }
/*      */ 
/*      */   public int getWhatToShow()
/*      */   {
/*  304 */     return -17;
/*      */   }
/*      */ 
/*      */   public DTMFilter getFilter()
/*      */   {
/*  322 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getExpandEntityReferences()
/*      */   {
/*  343 */     return true;
/*      */   }
/*      */ 
/*      */   public DTM getDTM(int nodeHandle)
/*      */   {
/*  358 */     return this.m_manager.getDTM(nodeHandle);
/*      */   }
/*      */ 
/*      */   public DTMManager getDTMManager()
/*      */   {
/*  374 */     return this.m_manager;
/*      */   }
/*      */ 
/*      */   public int nextNode()
/*      */   {
/*  390 */     if (this.m_next < size())
/*      */     {
/*  392 */       int next = elementAt(this.m_next);
/*      */ 
/*  394 */       this.m_next += 1;
/*      */ 
/*  396 */       return next;
/*      */     }
/*      */ 
/*  399 */     return -1;
/*      */   }
/*      */ 
/*      */   public int previousNode()
/*      */   {
/*  416 */     if (!this.m_cacheNodes) {
/*  417 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_ITERATE", null));
/*      */     }
/*      */ 
/*  420 */     if (this.m_next - 1 > 0)
/*      */     {
/*  422 */       this.m_next -= 1;
/*      */ 
/*  424 */       return elementAt(this.m_next);
/*      */     }
/*      */ 
/*  427 */     return -1;
/*      */   }
/*      */ 
/*      */   public void detach()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void allowDetachToRelease(boolean allowRelease)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isFresh()
/*      */   {
/*  465 */     return this.m_next == 0;
/*      */   }
/*      */ 
/*      */   public void runTo(int index)
/*      */   {
/*  483 */     if (!this.m_cacheNodes) {
/*  484 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
/*      */     }
/*      */ 
/*  487 */     if ((index >= 0) && (this.m_next < this.m_firstFree))
/*  488 */       this.m_next = index;
/*      */     else
/*  490 */       this.m_next = (this.m_firstFree - 1);
/*      */   }
/*      */ 
/*      */   public int item(int index)
/*      */   {
/*  508 */     runTo(index);
/*      */ 
/*  510 */     return elementAt(index);
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  524 */     runTo(-1);
/*      */ 
/*  526 */     return size();
/*      */   }
/*      */ 
/*      */   public void addNode(int n)
/*      */   {
/*  540 */     if (!this.m_mutable) {
/*  541 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  543 */     addElement(n);
/*      */   }
/*      */ 
/*      */   public void insertNode(int n, int pos)
/*      */   {
/*  558 */     if (!this.m_mutable) {
/*  559 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  561 */     insertElementAt(n, pos);
/*      */   }
/*      */ 
/*      */   public void removeNode(int n)
/*      */   {
/*  574 */     if (!this.m_mutable) {
/*  575 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  577 */     removeElement(n);
/*      */   }
/*      */ 
/*      */   public void addNodes(DTMIterator iterator)
/*      */   {
/*  651 */     if (!this.m_mutable) {
/*  652 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  654 */     if (null != iterator)
/*      */     {
/*      */       int obj;
/*  658 */       while (-1 != (obj = iterator.nextNode()))
/*      */       {
/*  660 */         addElement(obj);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNodesInDocOrder(DTMIterator iterator, XPathContext support)
/*      */   {
/*  708 */     if (!this.m_mutable)
/*  709 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     int node;
/*  713 */     while (-1 != (node = iterator.nextNode()))
/*      */     {
/*  715 */       addNodeInDocOrder(node, support);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int addNodeInDocOrder(int node, boolean test, XPathContext support)
/*      */   {
/*  797 */     if (!this.m_mutable) {
/*  798 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  800 */     int insertIndex = -1;
/*      */ 
/*  802 */     if (test)
/*      */     {
/*  808 */       int size = size();
/*      */ 
/*  810 */       for (int i = size - 1; i >= 0; i--)
/*      */       {
/*  812 */         int child = elementAt(i);
/*      */ 
/*  814 */         if (child == node)
/*      */         {
/*  816 */           i = -2;
/*      */         }
/*      */         else
/*      */         {
/*  821 */           DTM dtm = support.getDTM(node);
/*  822 */           if (!dtm.isNodeAfter(node, child))
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  828 */       if (i != -2)
/*      */       {
/*  830 */         insertIndex = i + 1;
/*      */ 
/*  832 */         insertElementAt(node, insertIndex);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  837 */       insertIndex = size();
/*      */ 
/*  839 */       boolean foundit = false;
/*      */ 
/*  841 */       for (int i = 0; i < insertIndex; i++)
/*      */       {
/*  843 */         if (i == node)
/*      */         {
/*  845 */           foundit = true;
/*      */ 
/*  847 */           break;
/*      */         }
/*      */       }
/*      */ 
/*  851 */       if (!foundit) {
/*  852 */         addElement(node);
/*      */       }
/*      */     }
/*      */ 
/*  856 */     return insertIndex;
/*      */   }
/*      */ 
/*      */   public int addNodeInDocOrder(int node, XPathContext support)
/*      */   {
/*  872 */     if (!this.m_mutable) {
/*  873 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  875 */     return addNodeInDocOrder(node, true, support);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  885 */     return super.size();
/*      */   }
/*      */ 
/*      */   public void addElement(int value)
/*      */   {
/*  898 */     if (!this.m_mutable) {
/*  899 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  901 */     super.addElement(value);
/*      */   }
/*      */ 
/*      */   public void insertElementAt(int value, int at)
/*      */   {
/*  918 */     if (!this.m_mutable) {
/*  919 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  921 */     super.insertElementAt(value, at);
/*      */   }
/*      */ 
/*      */   public void appendNodes(NodeVector nodes)
/*      */   {
/*  934 */     if (!this.m_mutable) {
/*  935 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  937 */     super.appendNodes(nodes);
/*      */   }
/*      */ 
/*      */   public void removeAllElements()
/*      */   {
/*  951 */     if (!this.m_mutable) {
/*  952 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  954 */     super.removeAllElements();
/*      */   }
/*      */ 
/*      */   public boolean removeElement(int s)
/*      */   {
/*  973 */     if (!this.m_mutable) {
/*  974 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  976 */     return super.removeElement(s);
/*      */   }
/*      */ 
/*      */   public void removeElementAt(int i)
/*      */   {
/*  992 */     if (!this.m_mutable) {
/*  993 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/*  995 */     super.removeElementAt(i);
/*      */   }
/*      */ 
/*      */   public void setElementAt(int node, int index)
/*      */   {
/* 1013 */     if (!this.m_mutable) {
/* 1014 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/* 1016 */     super.setElementAt(node, index);
/*      */   }
/*      */ 
/*      */   public void setItem(int node, int index)
/*      */   {
/* 1030 */     if (!this.m_mutable) {
/* 1031 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_NOT_MUTABLE", null));
/*      */     }
/* 1033 */     super.setElementAt(node, index);
/*      */   }
/*      */ 
/*      */   public int elementAt(int i)
/*      */   {
/* 1046 */     runTo(i);
/*      */ 
/* 1048 */     return super.elementAt(i);
/*      */   }
/*      */ 
/*      */   public boolean contains(int s)
/*      */   {
/* 1061 */     runTo(-1);
/*      */ 
/* 1063 */     return super.contains(s);
/*      */   }
/*      */ 
/*      */   public int indexOf(int elem, int index)
/*      */   {
/* 1080 */     runTo(-1);
/*      */ 
/* 1082 */     return super.indexOf(elem, index);
/*      */   }
/*      */ 
/*      */   public int indexOf(int elem)
/*      */   {
/* 1098 */     runTo(-1);
/*      */ 
/* 1100 */     return super.indexOf(elem);
/*      */   }
/*      */ 
/*      */   public int getCurrentPos()
/*      */   {
/* 1117 */     return this.m_next;
/*      */   }
/*      */ 
/*      */   public void setCurrentPos(int i)
/*      */   {
/* 1129 */     if (!this.m_cacheNodes) {
/* 1130 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_NODESETDTM_CANNOT_INDEX", null));
/*      */     }
/*      */ 
/* 1133 */     this.m_next = i;
/*      */   }
/*      */ 
/*      */   public int getCurrentNode()
/*      */   {
/* 1146 */     if (!this.m_cacheNodes) {
/* 1147 */       throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");
/*      */     }
/*      */ 
/* 1150 */     int saved = this.m_next;
/*      */ 
/* 1154 */     int current = this.m_next > 0 ? this.m_next - 1 : this.m_next;
/* 1155 */     int n = current < this.m_firstFree ? elementAt(current) : -1;
/* 1156 */     this.m_next = saved;
/* 1157 */     return n;
/*      */   }
/*      */ 
/*      */   public boolean getShouldCacheNodes()
/*      */   {
/* 1178 */     return this.m_cacheNodes;
/*      */   }
/*      */ 
/*      */   public void setShouldCacheNodes(boolean b)
/*      */   {
/* 1195 */     if (!isFresh()) {
/* 1196 */       throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_CALL_SETSHOULDCACHENODE", null));
/*      */     }
/*      */ 
/* 1199 */     this.m_cacheNodes = b;
/* 1200 */     this.m_mutable = true;
/*      */   }
/*      */ 
/*      */   public boolean isMutable()
/*      */   {
/* 1211 */     return this.m_mutable;
/*      */   }
/*      */ 
/*      */   public int getLast()
/*      */   {
/* 1218 */     return this.m_last;
/*      */   }
/*      */ 
/*      */   public void setLast(int last)
/*      */   {
/* 1223 */     this.m_last = last;
/*      */   }
/*      */ 
/*      */   public boolean isDocOrdered()
/*      */   {
/* 1234 */     return true;
/*      */   }
/*      */ 
/*      */   public int getAxis()
/*      */   {
/* 1245 */     return -1;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.NodeSetDTM
 * JD-Core Version:    0.6.2
 */