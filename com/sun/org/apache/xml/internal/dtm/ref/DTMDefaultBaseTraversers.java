/*      */ package com.sun.org.apache.xml.internal.dtm.ref;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import javax.xml.transform.Source;
/*      */ 
/*      */ public abstract class DTMDefaultBaseTraversers extends DTMDefaultBase
/*      */ {
/*      */   public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*      */   {
/*   64 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
/*      */   }
/*      */ 
/*      */   public DTMDefaultBaseTraversers(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
/*      */   {
/*   92 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
/*      */   }
/*      */ 
/*      */   public DTMAxisTraverser getAxisTraverser(int axis)
/*      */   {
/*      */     DTMAxisTraverser traverser;
/*      */     DTMAxisTraverser traverser;
/*  109 */     if (null == this.m_traversers)
/*      */     {
/*  111 */       this.m_traversers = new DTMAxisTraverser[Axis.getNamesLength()];
/*  112 */       traverser = null;
/*      */     }
/*      */     else
/*      */     {
/*  116 */       traverser = this.m_traversers[axis];
/*      */ 
/*  118 */       if (traverser != null) {
/*  119 */         return traverser;
/*      */       }
/*      */     }
/*  122 */     switch (axis)
/*      */     {
/*      */     case 0:
/*  125 */       traverser = new AncestorTraverser(null);
/*  126 */       break;
/*      */     case 1:
/*  128 */       traverser = new AncestorOrSelfTraverser(null);
/*  129 */       break;
/*      */     case 2:
/*  131 */       traverser = new AttributeTraverser(null);
/*  132 */       break;
/*      */     case 3:
/*  134 */       traverser = new ChildTraverser(null);
/*  135 */       break;
/*      */     case 4:
/*  137 */       traverser = new DescendantTraverser(null);
/*  138 */       break;
/*      */     case 5:
/*  140 */       traverser = new DescendantOrSelfTraverser(null);
/*  141 */       break;
/*      */     case 6:
/*  143 */       traverser = new FollowingTraverser(null);
/*  144 */       break;
/*      */     case 7:
/*  146 */       traverser = new FollowingSiblingTraverser(null);
/*  147 */       break;
/*      */     case 9:
/*  149 */       traverser = new NamespaceTraverser(null);
/*  150 */       break;
/*      */     case 8:
/*  152 */       traverser = new NamespaceDeclsTraverser(null);
/*  153 */       break;
/*      */     case 10:
/*  155 */       traverser = new ParentTraverser(null);
/*  156 */       break;
/*      */     case 11:
/*  158 */       traverser = new PrecedingTraverser(null);
/*  159 */       break;
/*      */     case 12:
/*  161 */       traverser = new PrecedingSiblingTraverser(null);
/*  162 */       break;
/*      */     case 13:
/*  164 */       traverser = new SelfTraverser(null);
/*  165 */       break;
/*      */     case 16:
/*  167 */       traverser = new AllFromRootTraverser(null);
/*  168 */       break;
/*      */     case 14:
/*  170 */       traverser = new AllFromNodeTraverser(null);
/*  171 */       break;
/*      */     case 15:
/*  173 */       traverser = new PrecedingAndAncestorTraverser(null);
/*  174 */       break;
/*      */     case 17:
/*  176 */       traverser = new DescendantFromRootTraverser(null);
/*  177 */       break;
/*      */     case 18:
/*  179 */       traverser = new DescendantOrSelfFromRootTraverser(null);
/*  180 */       break;
/*      */     case 19:
/*  182 */       traverser = new RootTraverser(null);
/*  183 */       break;
/*      */     case 20:
/*  185 */       return null;
/*      */     default:
/*  187 */       throw new DTMException(XMLMessages.createXMLMessage("ER_UNKNOWN_AXIS_TYPE", new Object[] { Integer.toString(axis) }));
/*      */     }
/*      */ 
/*  190 */     if (null == traverser) {
/*  191 */       throw new DTMException(XMLMessages.createXMLMessage("ER_AXIS_TRAVERSER_NOT_SUPPORTED", new Object[] { Axis.getNames(axis) }));
/*      */     }
/*      */ 
/*  195 */     this.m_traversers[axis] = traverser;
/*      */ 
/*  197 */     return traverser;
/*      */   }
/*      */ 
/*      */   private class AllFromNodeTraverser extends DTMDefaultBaseTraversers.DescendantOrSelfTraverser
/*      */   {
/*      */     private AllFromNodeTraverser()
/*      */     {
/*  797 */       super(null);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  811 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/*  813 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
/*      */ 
/*  820 */       DTMDefaultBaseTraversers.this._exptype(current);
/*      */ 
/*  822 */       if (!isDescendant(subtreeRootIdent, current)) {
/*  823 */         return -1;
/*      */       }
/*  825 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AllFromRootTraverser extends DTMDefaultBaseTraversers.AllFromNodeTraverser
/*      */   {
/*      */     private AllFromRootTraverser()
/*      */     {
/* 1470 */       super(null);
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/* 1482 */       return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/* 1495 */       return DTMDefaultBaseTraversers.this.getExpandedTypeID(DTMDefaultBaseTraversers.this.getDocumentRoot(context)) == expandedTypeID ? context : next(context, context, expandedTypeID);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1510 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1512 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
/*      */ 
/* 1515 */       int type = DTMDefaultBaseTraversers.this._type(current);
/* 1516 */       if (type == -1) {
/* 1517 */         return -1;
/*      */       }
/* 1519 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1536 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1538 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1; ; current++)
/*      */       {
/* 1540 */         int exptype = DTMDefaultBaseTraversers.this._exptype(current);
/*      */ 
/* 1542 */         if (exptype == -1) {
/* 1543 */           return -1;
/*      */         }
/* 1545 */         if (exptype == expandedTypeID)
/*      */         {
/* 1548 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AncestorOrSelfTraverser extends DTMDefaultBaseTraversers.AncestorTraverser
/*      */   {
/*      */     private AncestorOrSelfTraverser()
/*      */     {
/*  247 */       super(null);
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/*  261 */       return context;
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/*  278 */       return DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID ? context : next(context, context, expandedTypeID);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AncestorTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private AncestorTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  216 */       return DTMDefaultBaseTraversers.this.getParent(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/*  232 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
/*      */ 
/*  234 */       while (-1 != (current = DTMDefaultBaseTraversers.this.m_parent.elementAt(current)))
/*      */       {
/*  236 */         if (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current) == expandedTypeID) {
/*  237 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/*  240 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class AttributeTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private AttributeTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  299 */       return context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/*  316 */       current = context == current ? DTMDefaultBaseTraversers.this.getFirstAttribute(context) : DTMDefaultBaseTraversers.this.getNextAttribute(current);
/*      */       do
/*      */       {
/*  321 */         if (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) == expandedTypeID)
/*  322 */           return current;
/*      */       }
/*  324 */       while (-1 != (current = DTMDefaultBaseTraversers.this.getNextAttribute(current)));
/*      */ 
/*  326 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ChildTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private ChildTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID)
/*      */     {
/*  352 */       int nsIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getNamespaceID(expandedTypeID);
/*  353 */       int lnIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getLocalNameID(expandedTypeID);
/*      */       while (true)
/*      */       {
/*  357 */         int nextID = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
/*      */ 
/*  359 */         if (-2 != nextID)
/*      */         {
/*  361 */           int parentID = DTMDefaultBaseTraversers.this.m_parent.elementAt(nextID);
/*      */ 
/*  364 */           if (parentID == axisRoot) {
/*  365 */             return nextID;
/*      */           }
/*      */ 
/*  369 */           if (parentID < axisRoot) {
/*  370 */             return -1;
/*      */           }
/*      */ 
/*      */           do
/*      */           {
/*  379 */             parentID = DTMDefaultBaseTraversers.this.m_parent.elementAt(parentID);
/*  380 */             if (parentID < axisRoot)
/*  381 */               return -1;
/*      */           }
/*  383 */           while (parentID > axisRoot);
/*      */ 
/*  386 */           nextPotential = nextID + 1;
/*      */         }
/*      */         else
/*      */         {
/*  390 */           DTMDefaultBaseTraversers.this.nextNode();
/*      */ 
/*  392 */           if (DTMDefaultBaseTraversers.this.m_nextsib.elementAt(axisRoot) != -2)
/*      */             break;
/*      */         }
/*      */       }
/*  396 */       return -1;
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/*  413 */       return DTMDefaultBaseTraversers.this.getFirstChild(context);
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/*  434 */       int identity = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/*  436 */       int firstMatch = getNextIndexed(identity, DTMDefaultBaseTraversers.this._firstch(identity), expandedTypeID);
/*      */ 
/*  439 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(firstMatch);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  465 */       return DTMDefaultBaseTraversers.this.getNextSibling(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/*  481 */       for (current = DTMDefaultBaseTraversers.this._nextsib(DTMDefaultBaseTraversers.this.makeNodeIdentity(current)); 
/*  482 */         -1 != current; 
/*  483 */         current = DTMDefaultBaseTraversers.this._nextsib(current))
/*      */       {
/*  485 */         if (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current) == expandedTypeID) {
/*  486 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/*  489 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendantFromRootTraverser extends DTMDefaultBaseTraversers.DescendantTraverser
/*      */   {
/*      */     private DescendantFromRootTraverser()
/*      */     {
/* 1679 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int getFirstPotential(int identity)
/*      */     {
/* 1692 */       return DTMDefaultBaseTraversers.this._firstch(0);
/*      */     }
/*      */ 
/*      */     protected int getSubtreeRoot(int handle)
/*      */     {
/* 1702 */       return 0;
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/* 1714 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(DTMDefaultBaseTraversers.this._firstch(0));
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/* 1733 */       if (isIndexed(expandedTypeID))
/*      */       {
/* 1735 */         int identity = 0;
/* 1736 */         int firstPotential = getFirstPotential(identity);
/*      */ 
/* 1738 */         return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
/*      */       }
/*      */ 
/* 1741 */       int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
/* 1742 */       return next(root, root, expandedTypeID);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendantOrSelfFromRootTraverser extends DTMDefaultBaseTraversers.DescendantTraverser
/*      */   {
/*      */     private DescendantOrSelfFromRootTraverser()
/*      */     {
/* 1607 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int getFirstPotential(int identity)
/*      */     {
/* 1620 */       return identity;
/*      */     }
/*      */ 
/*      */     protected int getSubtreeRoot(int handle)
/*      */     {
/* 1631 */       return DTMDefaultBaseTraversers.this.makeNodeIdentity(DTMDefaultBaseTraversers.this.getDocument());
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/* 1643 */       return DTMDefaultBaseTraversers.this.getDocumentRoot(context);
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/* 1662 */       if (isIndexed(expandedTypeID))
/*      */       {
/* 1664 */         int identity = 0;
/* 1665 */         int firstPotential = getFirstPotential(identity);
/*      */ 
/* 1667 */         return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
/*      */       }
/*      */ 
/* 1670 */       int root = first(context);
/* 1671 */       return next(root, root, expandedTypeID);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendantOrSelfTraverser extends DTMDefaultBaseTraversers.DescendantTraverser
/*      */   {
/*      */     private DescendantOrSelfTraverser()
/*      */     {
/*  763 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int getFirstPotential(int identity)
/*      */     {
/*  776 */       return identity;
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/*  790 */       return context;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DescendantTraverser extends DTMDefaultBaseTraversers.IndexedDTMAxisTraverser
/*      */   {
/*      */     private DescendantTraverser()
/*      */     {
/*  584 */       super(null);
/*      */     }
/*      */ 
/*      */     protected int getFirstPotential(int identity)
/*      */     {
/*  596 */       return identity + 1;
/*      */     }
/*      */ 
/*      */     protected boolean axisHasBeenProcessed(int axisRoot)
/*      */     {
/*  609 */       return DTMDefaultBaseTraversers.this.m_nextsib.elementAt(axisRoot) != -2;
/*      */     }
/*      */ 
/*      */     protected int getSubtreeRoot(int handle)
/*      */     {
/*  622 */       return DTMDefaultBaseTraversers.this.makeNodeIdentity(handle);
/*      */     }
/*      */ 
/*      */     protected boolean isDescendant(int subtreeRootIdentity, int identity)
/*      */     {
/*  639 */       return DTMDefaultBaseTraversers.this._parent(identity) >= subtreeRootIdentity;
/*      */     }
/*      */ 
/*      */     protected boolean isAfterAxis(int axisRoot, int identity)
/*      */     {
/*      */       do
/*      */       {
/*  659 */         if (identity == axisRoot)
/*  660 */           return false;
/*  661 */         identity = DTMDefaultBaseTraversers.this.m_parent.elementAt(identity);
/*      */       }
/*  663 */       while (identity >= axisRoot);
/*      */ 
/*  665 */       return true;
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/*  685 */       if (isIndexed(expandedTypeID))
/*      */       {
/*  687 */         int identity = getSubtreeRoot(context);
/*  688 */         int firstPotential = getFirstPotential(identity);
/*      */ 
/*  690 */         return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(identity, firstPotential, expandedTypeID));
/*      */       }
/*      */ 
/*  693 */       return next(context, context, expandedTypeID);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  707 */       int subtreeRootIdent = getSubtreeRoot(context);
/*      */ 
/*  709 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1; ; current++)
/*      */       {
/*  711 */         int type = DTMDefaultBaseTraversers.this._type(current);
/*      */ 
/*  713 */         if (!isDescendant(subtreeRootIdent, current)) {
/*  714 */           return -1;
/*      */         }
/*  716 */         if ((2 != type) && (13 != type))
/*      */         {
/*  719 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/*  736 */       int subtreeRootIdent = getSubtreeRoot(context);
/*      */ 
/*  738 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) + 1;
/*      */ 
/*  740 */       if (isIndexed(expandedTypeID))
/*      */       {
/*  742 */         return DTMDefaultBaseTraversers.this.makeNodeHandle(getNextIndexed(subtreeRootIdent, current, expandedTypeID));
/*      */       }
/*      */ 
/*  745 */       for (; ; current++)
/*      */       {
/*  747 */         int exptype = DTMDefaultBaseTraversers.this._exptype(current);
/*      */ 
/*  749 */         if (!isDescendant(subtreeRootIdent, current)) {
/*  750 */           return -1;
/*      */         }
/*  752 */         if (exptype == expandedTypeID)
/*      */         {
/*  755 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FollowingSiblingTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private FollowingSiblingTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  999 */       return DTMDefaultBaseTraversers.this.getNextSibling(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1015 */       while (-1 != (current = DTMDefaultBaseTraversers.this.getNextSibling(current)))
/*      */       {
/* 1017 */         if (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) == expandedTypeID) {
/* 1018 */           return current;
/*      */         }
/*      */       }
/* 1021 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FollowingTraverser extends DTMDefaultBaseTraversers.DescendantTraverser
/*      */   {
/*      */     private FollowingTraverser()
/*      */     {
/*  833 */       super(null);
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/*  846 */       context = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/*  849 */       int type = DTMDefaultBaseTraversers.this._type(context);
/*      */ 
/*  851 */       if ((2 == type) || (13 == type))
/*      */       {
/*  853 */         context = DTMDefaultBaseTraversers.this._parent(context);
/*  854 */         int first = DTMDefaultBaseTraversers.this._firstch(context);
/*      */ 
/*  856 */         if (-1 != first)
/*  857 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(first);
/*      */       }
/*      */       int first;
/*      */       do
/*      */       {
/*  862 */         first = DTMDefaultBaseTraversers.this._nextsib(context);
/*      */ 
/*  864 */         if (-1 == first)
/*  865 */           context = DTMDefaultBaseTraversers.this._parent(context);
/*      */       }
/*  867 */       while ((-1 == first) && (-1 != context));
/*      */ 
/*  869 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(first);
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/*  886 */       int type = DTMDefaultBaseTraversers.this.getNodeType(context);
/*      */ 
/*  888 */       if ((2 == type) || (13 == type))
/*      */       {
/*  890 */         context = DTMDefaultBaseTraversers.this.getParent(context);
/*  891 */         int first = DTMDefaultBaseTraversers.this.getFirstChild(context);
/*      */ 
/*  893 */         if (-1 != first)
/*      */         {
/*  895 */           if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first) == expandedTypeID) {
/*  896 */             return first;
/*      */           }
/*  898 */           return next(context, first, expandedTypeID);
/*      */         }
/*      */       }
/*      */       int first;
/*      */       do
/*      */       {
/*  904 */         first = DTMDefaultBaseTraversers.this.getNextSibling(context);
/*      */ 
/*  906 */         if (-1 == first) {
/*  907 */           context = DTMDefaultBaseTraversers.this.getParent(context);
/*      */         }
/*      */         else {
/*  910 */           if (DTMDefaultBaseTraversers.this.getExpandedTypeID(first) == expandedTypeID) {
/*  911 */             return first;
/*      */           }
/*  913 */           return next(context, first, expandedTypeID);
/*      */         }
/*      */       }
/*  916 */       while ((-1 == first) && (-1 != context));
/*      */ 
/*  918 */       return first;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/*  932 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
/*      */       int type;
/*      */       do
/*      */       {
/*  936 */         current++;
/*      */ 
/*  939 */         type = DTMDefaultBaseTraversers.this._type(current);
/*      */ 
/*  941 */         if (-1 == type)
/*  942 */           return -1;
/*      */       }
/*  944 */       while ((2 == type) || (13 == type));
/*      */ 
/*  947 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/*  964 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
/*      */       int etype;
/*      */       do
/*      */       {
/*  968 */         current++;
/*      */ 
/*  970 */         etype = DTMDefaultBaseTraversers.this._exptype(current);
/*      */ 
/*  972 */         if (-1 == etype)
/*  973 */           return -1;
/*      */       }
/*  975 */       while (etype != expandedTypeID);
/*      */ 
/*  978 */       return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class IndexedDTMAxisTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private IndexedDTMAxisTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected final boolean isIndexed(int expandedTypeID)
/*      */     {
/*  512 */       return (DTMDefaultBaseTraversers.this.m_indexing) && (1 == DTMDefaultBaseTraversers.this.m_expandedNameTable.getType(expandedTypeID));
/*      */     }
/*      */ 
/*      */     protected abstract boolean isAfterAxis(int paramInt1, int paramInt2);
/*      */ 
/*      */     protected abstract boolean axisHasBeenProcessed(int paramInt);
/*      */ 
/*      */     protected int getNextIndexed(int axisRoot, int nextPotential, int expandedTypeID)
/*      */     {
/*  556 */       int nsIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getNamespaceID(expandedTypeID);
/*  557 */       int lnIndex = DTMDefaultBaseTraversers.this.m_expandedNameTable.getLocalNameID(expandedTypeID);
/*      */       while (true)
/*      */       {
/*  561 */         int next = DTMDefaultBaseTraversers.this.findElementFromIndex(nsIndex, lnIndex, nextPotential);
/*      */ 
/*  563 */         if (-2 != next)
/*      */         {
/*  565 */           if (isAfterAxis(axisRoot, next)) {
/*  566 */             return -1;
/*      */           }
/*      */ 
/*  569 */           return next;
/*      */         }
/*  571 */         if (axisHasBeenProcessed(axisRoot)) {
/*      */           break;
/*      */         }
/*  574 */         DTMDefaultBaseTraversers.this.nextNode();
/*      */       }
/*      */ 
/*  577 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class NamespaceDeclsTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private NamespaceDeclsTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1042 */       return context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1060 */       current = context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, false) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false);
/*      */       do
/*      */       {
/* 1066 */         if (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) == expandedTypeID) {
/* 1067 */           return current;
/*      */         }
/*      */       }
/* 1070 */       while (-1 != (current = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, false)));
/*      */ 
/* 1072 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class NamespaceTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private NamespaceTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1093 */       return context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1111 */       current = context == current ? DTMDefaultBaseTraversers.this.getFirstNamespaceNode(context, true) : DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true);
/*      */       do
/*      */       {
/* 1117 */         if (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) == expandedTypeID) {
/* 1118 */           return current;
/*      */         }
/*      */       }
/* 1121 */       while (-1 != (current = DTMDefaultBaseTraversers.this.getNextNamespaceNode(context, current, true)));
/*      */ 
/* 1123 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ParentTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private ParentTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/* 1146 */       return DTMDefaultBaseTraversers.this.getParent(context);
/*      */     }
/*      */ 
/*      */     public int first(int current, int expandedTypeID)
/*      */     {
/* 1166 */       current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current);
/*      */ 
/* 1168 */       while (-1 != (current = DTMDefaultBaseTraversers.this.m_parent.elementAt(current)))
/*      */       {
/* 1170 */         if (DTMDefaultBaseTraversers.this.m_exptype.elementAt(current) == expandedTypeID) {
/* 1171 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/* 1174 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1189 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1207 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrecedingAndAncestorTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private PrecedingAndAncestorTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1315 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1317 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; current--)
/*      */       {
/* 1319 */         short type = DTMDefaultBaseTraversers.this._type(current);
/*      */ 
/* 1321 */         if ((2 != type) && (13 != type))
/*      */         {
/* 1324 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/* 1327 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1343 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1345 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; current--)
/*      */       {
/* 1347 */         int exptype = DTMDefaultBaseTraversers.this.m_exptype.elementAt(current);
/*      */ 
/* 1349 */         if (exptype == expandedTypeID)
/*      */         {
/* 1352 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/* 1355 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrecedingSiblingTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private PrecedingSiblingTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1375 */       return DTMDefaultBaseTraversers.this.getPreviousSibling(current);
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1391 */       while (-1 != (current = DTMDefaultBaseTraversers.this.getPreviousSibling(current)))
/*      */       {
/* 1393 */         if (DTMDefaultBaseTraversers.this.getExpandedTypeID(current) == expandedTypeID) {
/* 1394 */           return current;
/*      */         }
/*      */       }
/* 1397 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PrecedingTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private PrecedingTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected boolean isAncestor(int contextIdent, int currentIdent)
/*      */     {
/* 1230 */       for (contextIdent = DTMDefaultBaseTraversers.this.m_parent.elementAt(contextIdent); -1 != contextIdent; 
/* 1231 */         contextIdent = DTMDefaultBaseTraversers.this.m_parent.elementAt(contextIdent))
/*      */       {
/* 1233 */         if (contextIdent == currentIdent) {
/* 1234 */           return true;
/*      */         }
/*      */       }
/* 1237 */       return false;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1251 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1253 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; current--)
/*      */       {
/* 1255 */         short type = DTMDefaultBaseTraversers.this._type(current);
/*      */ 
/* 1257 */         if ((2 != type) && (13 != type) && (!isAncestor(subtreeRootIdent, current)))
/*      */         {
/* 1261 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/* 1264 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1280 */       int subtreeRootIdent = DTMDefaultBaseTraversers.this.makeNodeIdentity(context);
/*      */ 
/* 1282 */       for (current = DTMDefaultBaseTraversers.this.makeNodeIdentity(current) - 1; current >= 0; current--)
/*      */       {
/* 1284 */         int exptype = DTMDefaultBaseTraversers.this.m_exptype.elementAt(current);
/*      */ 
/* 1286 */         if ((exptype == expandedTypeID) && (!isAncestor(subtreeRootIdent, current)))
/*      */         {
/* 1290 */           return DTMDefaultBaseTraversers.this.makeNodeHandle(current);
/*      */         }
/*      */       }
/* 1293 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RootTraverser extends DTMDefaultBaseTraversers.AllFromRootTraverser
/*      */   {
/*      */     private RootTraverser()
/*      */     {
/* 1556 */       super(null);
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/* 1569 */       int root = DTMDefaultBaseTraversers.this.getDocumentRoot(context);
/* 1570 */       return DTMDefaultBaseTraversers.this.getExpandedTypeID(root) == expandedTypeID ? root : -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1584 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1599 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SelfTraverser extends DTMAxisTraverser
/*      */   {
/*      */     private SelfTraverser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int first(int context)
/*      */     {
/* 1418 */       return context;
/*      */     }
/*      */ 
/*      */     public int first(int context, int expandedTypeID)
/*      */     {
/* 1435 */       return DTMDefaultBaseTraversers.this.getExpandedTypeID(context) == expandedTypeID ? context : -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current)
/*      */     {
/* 1448 */       return -1;
/*      */     }
/*      */ 
/*      */     public int next(int context, int current, int expandedTypeID)
/*      */     {
/* 1463 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseTraversers
 * JD-Core Version:    0.6.2
 */