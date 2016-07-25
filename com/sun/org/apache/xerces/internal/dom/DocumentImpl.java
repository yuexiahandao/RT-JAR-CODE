/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.events.EventImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.events.MutationEventImpl;
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.events.DocumentEvent;
/*      */ import org.w3c.dom.events.Event;
/*      */ import org.w3c.dom.events.EventException;
/*      */ import org.w3c.dom.events.EventListener;
/*      */ import org.w3c.dom.events.MutationEvent;
/*      */ import org.w3c.dom.ranges.DocumentRange;
/*      */ import org.w3c.dom.ranges.Range;
/*      */ import org.w3c.dom.traversal.DocumentTraversal;
/*      */ import org.w3c.dom.traversal.NodeFilter;
/*      */ import org.w3c.dom.traversal.NodeIterator;
/*      */ import org.w3c.dom.traversal.TreeWalker;
/*      */ 
/*      */ public class DocumentImpl extends CoreDocumentImpl
/*      */   implements DocumentTraversal, DocumentEvent, DocumentRange
/*      */ {
/*      */   static final long serialVersionUID = 515687835542616694L;
/*      */   protected Vector iterators;
/*      */   protected Vector ranges;
/*      */   protected Hashtable eventListeners;
/*  109 */   protected boolean mutationEvents = false;
/*      */   EnclosingAttr savedEnclosingAttr;
/*      */ 
/*      */   public DocumentImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   public DocumentImpl(boolean grammarAccess)
/*      */   {
/*  125 */     super(grammarAccess);
/*      */   }
/*      */ 
/*      */   public DocumentImpl(DocumentType doctype)
/*      */   {
/*  134 */     super(doctype);
/*      */   }
/*      */ 
/*      */   public DocumentImpl(DocumentType doctype, boolean grammarAccess)
/*      */   {
/*  139 */     super(doctype, grammarAccess);
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  157 */     DocumentImpl newdoc = new DocumentImpl();
/*  158 */     callUserDataHandlers(this, newdoc, (short)1);
/*  159 */     cloneNode(newdoc, deep);
/*      */ 
/*  162 */     newdoc.mutationEvents = this.mutationEvents;
/*      */ 
/*  164 */     return newdoc;
/*      */   }
/*      */ 
/*      */   public DOMImplementation getImplementation()
/*      */   {
/*  177 */     return DOMImplementationImpl.getDOMImplementation();
/*      */   }
/*      */ 
/*      */   public NodeIterator createNodeIterator(Node root, short whatToShow, NodeFilter filter)
/*      */   {
/*  198 */     return createNodeIterator(root, whatToShow, filter, true);
/*      */   }
/*      */ 
/*      */   public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion)
/*      */   {
/*  219 */     if (root == null) {
/*  220 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/*  221 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */ 
/*  224 */     NodeIterator iterator = new NodeIteratorImpl(this, root, whatToShow, filter, entityReferenceExpansion);
/*      */ 
/*  229 */     if (this.iterators == null) {
/*  230 */       this.iterators = new Vector();
/*      */     }
/*      */ 
/*  233 */     this.iterators.addElement(iterator);
/*      */ 
/*  235 */     return iterator;
/*      */   }
/*      */ 
/*      */   public TreeWalker createTreeWalker(Node root, short whatToShow, NodeFilter filter)
/*      */   {
/*  250 */     return createTreeWalker(root, whatToShow, filter, true);
/*      */   }
/*      */ 
/*      */   public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion)
/*      */   {
/*  267 */     if (root == null) {
/*  268 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/*  269 */       throw new DOMException((short)9, msg);
/*      */     }
/*  271 */     return new TreeWalkerImpl(root, whatToShow, filter, entityReferenceExpansion);
/*      */   }
/*      */ 
/*      */   void removeNodeIterator(NodeIterator nodeIterator)
/*      */   {
/*  287 */     if (nodeIterator == null) return;
/*  288 */     if (this.iterators == null) return;
/*      */ 
/*  290 */     this.iterators.removeElement(nodeIterator);
/*      */   }
/*      */ 
/*      */   public Range createRange()
/*      */   {
/*  300 */     if (this.ranges == null) {
/*  301 */       this.ranges = new Vector();
/*      */     }
/*      */ 
/*  304 */     Range range = new RangeImpl(this);
/*      */ 
/*  306 */     this.ranges.addElement(range);
/*      */ 
/*  308 */     return range;
/*      */   }
/*      */ 
/*      */   void removeRange(Range range)
/*      */   {
/*  318 */     if (range == null) return;
/*  319 */     if (this.ranges == null) return;
/*      */ 
/*  321 */     this.ranges.removeElement(range);
/*      */   }
/*      */ 
/*      */   void replacedText(NodeImpl node)
/*      */   {
/*  330 */     if (this.ranges != null) {
/*  331 */       int size = this.ranges.size();
/*  332 */       for (int i = 0; i != size; i++)
/*  333 */         ((RangeImpl)this.ranges.elementAt(i)).receiveReplacedText(node);
/*      */     }
/*      */   }
/*      */ 
/*      */   void deletedText(NodeImpl node, int offset, int count)
/*      */   {
/*  344 */     if (this.ranges != null) {
/*  345 */       int size = this.ranges.size();
/*  346 */       for (int i = 0; i != size; i++)
/*  347 */         ((RangeImpl)this.ranges.elementAt(i)).receiveDeletedText(node, offset, count);
/*      */     }
/*      */   }
/*      */ 
/*      */   void insertedText(NodeImpl node, int offset, int count)
/*      */   {
/*  359 */     if (this.ranges != null) {
/*  360 */       int size = this.ranges.size();
/*  361 */       for (int i = 0; i != size; i++)
/*  362 */         ((RangeImpl)this.ranges.elementAt(i)).receiveInsertedText(node, offset, count);
/*      */     }
/*      */   }
/*      */ 
/*      */   void splitData(Node node, Node newNode, int offset)
/*      */   {
/*  374 */     if (this.ranges != null) {
/*  375 */       int size = this.ranges.size();
/*  376 */       for (int i = 0; i != size; i++)
/*  377 */         ((RangeImpl)this.ranges.elementAt(i)).receiveSplitData(node, newNode, offset);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Event createEvent(String type)
/*      */     throws DOMException
/*      */   {
/*  408 */     if ((type.equalsIgnoreCase("Events")) || ("Event".equals(type)))
/*  409 */       return new EventImpl();
/*  410 */     if ((type.equalsIgnoreCase("MutationEvents")) || ("MutationEvent".equals(type)))
/*      */     {
/*  412 */       return new MutationEventImpl();
/*      */     }
/*  414 */     String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/*  415 */     throw new DOMException((short)9, msg);
/*      */   }
/*      */ 
/*      */   void setMutationEvents(boolean set)
/*      */   {
/*  424 */     this.mutationEvents = set;
/*      */   }
/*      */ 
/*      */   boolean getMutationEvents()
/*      */   {
/*  431 */     return this.mutationEvents;
/*      */   }
/*      */ 
/*      */   protected void setEventListeners(NodeImpl n, Vector listeners)
/*      */   {
/*  441 */     if (this.eventListeners == null) {
/*  442 */       this.eventListeners = new Hashtable();
/*      */     }
/*  444 */     if (listeners == null) {
/*  445 */       this.eventListeners.remove(n);
/*  446 */       if (this.eventListeners.isEmpty())
/*      */       {
/*  448 */         this.mutationEvents = false;
/*      */       }
/*      */     } else {
/*  451 */       this.eventListeners.put(n, listeners);
/*      */ 
/*  453 */       this.mutationEvents = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Vector getEventListeners(NodeImpl n)
/*      */   {
/*  461 */     if (this.eventListeners == null) {
/*  462 */       return null;
/*      */     }
/*  464 */     return (Vector)this.eventListeners.get(n);
/*      */   }
/*      */ 
/*      */   protected void addEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture)
/*      */   {
/*  523 */     if ((type == null) || (type.equals("")) || (listener == null)) {
/*  524 */       return;
/*      */     }
/*      */ 
/*  528 */     removeEventListener(node, type, listener, useCapture);
/*      */ 
/*  530 */     Vector nodeListeners = getEventListeners(node);
/*  531 */     if (nodeListeners == null) {
/*  532 */       nodeListeners = new Vector();
/*  533 */       setEventListeners(node, nodeListeners);
/*      */     }
/*  535 */     nodeListeners.addElement(new LEntry(type, listener, useCapture));
/*      */ 
/*  538 */     LCount lc = LCount.lookup(type);
/*  539 */     if (useCapture) {
/*  540 */       lc.captures += 1;
/*  541 */       lc.total += 1;
/*      */     }
/*      */     else {
/*  544 */       lc.bubbles += 1;
/*  545 */       lc.total += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void removeEventListener(NodeImpl node, String type, EventListener listener, boolean useCapture)
/*      */   {
/*  566 */     if ((type == null) || (type.equals("")) || (listener == null))
/*  567 */       return;
/*  568 */     Vector nodeListeners = getEventListeners(node);
/*  569 */     if (nodeListeners == null) {
/*  570 */       return;
/*      */     }
/*      */ 
/*  575 */     for (int i = nodeListeners.size() - 1; i >= 0; i--) {
/*  576 */       LEntry le = (LEntry)nodeListeners.elementAt(i);
/*  577 */       if ((le.useCapture == useCapture) && (le.listener == listener) && (le.type.equals(type)))
/*      */       {
/*  579 */         nodeListeners.removeElementAt(i);
/*      */ 
/*  581 */         if (nodeListeners.size() == 0) {
/*  582 */           setEventListeners(node, null);
/*      */         }
/*      */ 
/*  585 */         LCount lc = LCount.lookup(type);
/*  586 */         if (useCapture) {
/*  587 */           lc.captures -= 1;
/*  588 */           lc.total -= 1; break;
/*      */         }
/*      */ 
/*  591 */         lc.bubbles -= 1;
/*  592 */         lc.total -= 1;
/*      */ 
/*  595 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void copyEventListeners(NodeImpl src, NodeImpl tgt) {
/*  601 */     Vector nodeListeners = getEventListeners(src);
/*  602 */     if (nodeListeners == null) {
/*  603 */       return;
/*      */     }
/*  605 */     setEventListeners(tgt, (Vector)nodeListeners.clone());
/*      */   }
/*      */ 
/*      */   protected boolean dispatchEvent(NodeImpl node, Event event)
/*      */   {
/*  659 */     if (event == null) return false;
/*      */ 
/*  663 */     EventImpl evt = (EventImpl)event;
/*      */ 
/*  667 */     if ((!evt.initialized) || (evt.type == null) || (evt.type.equals(""))) {
/*  668 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UNSPECIFIED_EVENT_TYPE_ERR", null);
/*  669 */       throw new EventException((short)0, msg);
/*      */     }
/*      */ 
/*  673 */     LCount lc = LCount.lookup(evt.getType());
/*  674 */     if (lc.total == 0) {
/*  675 */       return evt.preventDefault;
/*      */     }
/*      */ 
/*  681 */     evt.target = node;
/*  682 */     evt.stopPropagation = false;
/*  683 */     evt.preventDefault = false;
/*      */ 
/*  694 */     Vector pv = new Vector(10, 10);
/*  695 */     Node p = node;
/*  696 */     Node n = p.getParentNode();
/*  697 */     while (n != null) {
/*  698 */       pv.addElement(n);
/*  699 */       p = n;
/*  700 */       n = n.getParentNode();
/*      */     }
/*      */ 
/*  704 */     if (lc.captures > 0) {
/*  705 */       evt.eventPhase = 1;
/*      */ 
/*  708 */       for (int j = pv.size() - 1; (j >= 0) && 
/*  709 */         (!evt.stopPropagation); j--)
/*      */       {
/*  713 */         NodeImpl nn = (NodeImpl)pv.elementAt(j);
/*  714 */         evt.currentTarget = nn;
/*  715 */         Vector nodeListeners = getEventListeners(nn);
/*  716 */         if (nodeListeners != null) {
/*  717 */           Vector nl = (Vector)nodeListeners.clone();
/*      */ 
/*  719 */           int nlsize = nl.size();
/*  720 */           for (int i = 0; i < nlsize; i++) {
/*  721 */             LEntry le = (LEntry)nl.elementAt(i);
/*  722 */             if ((le.useCapture) && (le.type.equals(evt.type)) && (nodeListeners.contains(le))) {
/*      */               try
/*      */               {
/*  725 */                 le.listener.handleEvent(evt);
/*      */               }
/*      */               catch (Exception e)
/*      */               {
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  738 */     if (lc.bubbles > 0)
/*      */     {
/*  742 */       evt.eventPhase = 2;
/*  743 */       evt.currentTarget = node;
/*  744 */       Vector nodeListeners = getEventListeners(node);
/*  745 */       if ((!evt.stopPropagation) && (nodeListeners != null)) {
/*  746 */         Vector nl = (Vector)nodeListeners.clone();
/*      */ 
/*  748 */         int nlsize = nl.size();
/*  749 */         for (int i = 0; i < nlsize; i++) {
/*  750 */           LEntry le = (LEntry)nl.elementAt(i);
/*  751 */           if ((!le.useCapture) && (le.type.equals(evt.type)) && (nodeListeners.contains(le))) {
/*      */             try
/*      */             {
/*  754 */               le.listener.handleEvent(evt);
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  767 */       if (evt.bubbles) {
/*  768 */         evt.eventPhase = 3;
/*  769 */         int pvsize = pv.size();
/*  770 */         for (int j = 0; (j < pvsize) && 
/*  771 */           (!evt.stopPropagation); j++)
/*      */         {
/*  775 */           NodeImpl nn = (NodeImpl)pv.elementAt(j);
/*  776 */           evt.currentTarget = nn;
/*  777 */           nodeListeners = getEventListeners(nn);
/*  778 */           if (nodeListeners != null) {
/*  779 */             Vector nl = (Vector)nodeListeners.clone();
/*      */ 
/*  782 */             int nlsize = nl.size();
/*  783 */             for (int i = 0; i < nlsize; i++) {
/*  784 */               LEntry le = (LEntry)nl.elementAt(i);
/*  785 */               if ((!le.useCapture) && (le.type.equals(evt.type)) && (nodeListeners.contains(le))) {
/*      */                 try
/*      */                 {
/*  788 */                   le.listener.handleEvent(evt);
/*      */                 }
/*      */                 catch (Exception e)
/*      */                 {
/*      */                 }
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  805 */     if ((lc.defaults > 0) && (evt.cancelable) && (!evt.preventDefault));
/*  811 */     return evt.preventDefault;
/*      */   }
/*      */ 
/*      */   protected void dispatchEventToSubtree(Node n, Event e)
/*      */   {
/*  831 */     ((NodeImpl)n).dispatchEvent(e);
/*  832 */     if (n.getNodeType() == 1) {
/*  833 */       NamedNodeMap a = n.getAttributes();
/*  834 */       for (int i = a.getLength() - 1; i >= 0; i--)
/*  835 */         dispatchingEventToSubtree(a.item(i), e);
/*      */     }
/*  837 */     dispatchingEventToSubtree(n.getFirstChild(), e);
/*      */   }
/*      */ 
/*      */   protected void dispatchingEventToSubtree(Node n, Event e)
/*      */   {
/*  849 */     if (n == null) {
/*  850 */       return;
/*      */     }
/*      */ 
/*  855 */     ((NodeImpl)n).dispatchEvent(e);
/*  856 */     if (n.getNodeType() == 1) {
/*  857 */       NamedNodeMap a = n.getAttributes();
/*  858 */       for (int i = a.getLength() - 1; i >= 0; i--)
/*  859 */         dispatchingEventToSubtree(a.item(i), e);
/*      */     }
/*  861 */     dispatchingEventToSubtree(n.getFirstChild(), e);
/*  862 */     dispatchingEventToSubtree(n.getNextSibling(), e);
/*      */   }
/*      */ 
/*      */   protected void dispatchAggregateEvents(NodeImpl node, EnclosingAttr ea)
/*      */   {
/*  886 */     if (ea != null) {
/*  887 */       dispatchAggregateEvents(node, ea.node, ea.oldvalue, (short)1);
/*      */     }
/*      */     else
/*  890 */       dispatchAggregateEvents(node, null, null, (short)0);
/*      */   }
/*      */ 
/*      */   protected void dispatchAggregateEvents(NodeImpl node, AttrImpl enclosingAttr, String oldvalue, short change)
/*      */   {
/*  922 */     NodeImpl owner = null;
/*  923 */     if (enclosingAttr != null) {
/*  924 */       LCount lc = LCount.lookup("DOMAttrModified");
/*  925 */       owner = (NodeImpl)enclosingAttr.getOwnerElement();
/*  926 */       if ((lc.total > 0) && 
/*  927 */         (owner != null)) {
/*  928 */         MutationEventImpl me = new MutationEventImpl();
/*  929 */         me.initMutationEvent("DOMAttrModified", true, false, enclosingAttr, oldvalue, enclosingAttr.getNodeValue(), enclosingAttr.getNodeName(), change);
/*      */ 
/*  935 */         owner.dispatchEvent(me);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  943 */     LCount lc = LCount.lookup("DOMSubtreeModified");
/*  944 */     if (lc.total > 0) {
/*  945 */       MutationEvent me = new MutationEventImpl();
/*  946 */       me.initMutationEvent("DOMSubtreeModified", true, false, null, null, null, null, (short)0);
/*      */ 
/*  953 */       if (enclosingAttr != null) {
/*  954 */         dispatchEvent(enclosingAttr, me);
/*  955 */         if (owner != null)
/*  956 */           dispatchEvent(owner, me);
/*      */       }
/*      */       else {
/*  959 */         dispatchEvent(node, me);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void saveEnclosingAttr(NodeImpl node)
/*      */   {
/*  971 */     this.savedEnclosingAttr = null;
/*      */ 
/*  976 */     LCount lc = LCount.lookup("DOMAttrModified");
/*  977 */     if (lc.total > 0) {
/*  978 */       NodeImpl eventAncestor = node;
/*      */       while (true) {
/*  980 */         if (eventAncestor == null)
/*  981 */           return;
/*  982 */         int type = eventAncestor.getNodeType();
/*  983 */         if (type == 2) {
/*  984 */           EnclosingAttr retval = new EnclosingAttr();
/*  985 */           retval.node = ((AttrImpl)eventAncestor);
/*  986 */           retval.oldvalue = retval.node.getNodeValue();
/*  987 */           this.savedEnclosingAttr = retval;
/*  988 */           return;
/*      */         }
/*  990 */         if (type == 5)
/*  991 */           eventAncestor = eventAncestor.parentNode();
/*  992 */         else if (type == 3)
/*  993 */           eventAncestor = eventAncestor.parentNode();
/*      */         else
/*  995 */           return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void modifyingCharacterData(NodeImpl node, boolean replace)
/*      */   {
/* 1005 */     if ((this.mutationEvents) && 
/* 1006 */       (!replace))
/* 1007 */       saveEnclosingAttr(node);
/*      */   }
/*      */ 
/*      */   void modifiedCharacterData(NodeImpl node, String oldvalue, String value, boolean replace)
/*      */   {
/* 1016 */     if ((this.mutationEvents) && 
/* 1017 */       (!replace))
/*      */     {
/* 1019 */       LCount lc = LCount.lookup("DOMCharacterDataModified");
/*      */ 
/* 1021 */       if (lc.total > 0) {
/* 1022 */         MutationEvent me = new MutationEventImpl();
/* 1023 */         me.initMutationEvent("DOMCharacterDataModified", true, false, null, oldvalue, value, null, (short)0);
/*      */ 
/* 1027 */         dispatchEvent(node, me);
/*      */       }
/*      */ 
/* 1032 */       dispatchAggregateEvents(node, this.savedEnclosingAttr);
/*      */     }
/*      */   }
/*      */ 
/*      */   void replacedCharacterData(NodeImpl node, String oldvalue, String value)
/*      */   {
/* 1045 */     modifiedCharacterData(node, oldvalue, value, false);
/*      */   }
/*      */ 
/*      */   void insertingNode(NodeImpl node, boolean replace)
/*      */   {
/* 1054 */     if ((this.mutationEvents) && 
/* 1055 */       (!replace))
/* 1056 */       saveEnclosingAttr(node);
/*      */   }
/*      */ 
/*      */   void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace)
/*      */   {
/* 1065 */     if (this.mutationEvents)
/*      */     {
/* 1069 */       LCount lc = LCount.lookup("DOMNodeInserted");
/* 1070 */       if (lc.total > 0) {
/* 1071 */         MutationEventImpl me = new MutationEventImpl();
/* 1072 */         me.initMutationEvent("DOMNodeInserted", true, false, node, null, null, null, (short)0);
/*      */ 
/* 1075 */         dispatchEvent(newInternal, me);
/*      */       }
/*      */ 
/* 1080 */       lc = LCount.lookup("DOMNodeInsertedIntoDocument");
/*      */ 
/* 1082 */       if (lc.total > 0) {
/* 1083 */         NodeImpl eventAncestor = node;
/* 1084 */         if (this.savedEnclosingAttr != null) {
/* 1085 */           eventAncestor = (NodeImpl)this.savedEnclosingAttr.node.getOwnerElement();
/*      */         }
/* 1087 */         if (eventAncestor != null) {
/* 1088 */           NodeImpl p = eventAncestor;
/* 1089 */           while (p != null) {
/* 1090 */             eventAncestor = p;
/*      */ 
/* 1093 */             if (p.getNodeType() == 2) {
/* 1094 */               p = (NodeImpl)((AttrImpl)p).getOwnerElement();
/*      */             }
/*      */             else {
/* 1097 */               p = p.parentNode();
/*      */             }
/*      */           }
/* 1100 */           if (eventAncestor.getNodeType() == 9) {
/* 1101 */             MutationEventImpl me = new MutationEventImpl();
/* 1102 */             me.initMutationEvent("DOMNodeInsertedIntoDocument", false, false, null, null, null, null, (short)0);
/*      */ 
/* 1106 */             dispatchEventToSubtree(newInternal, me);
/*      */           }
/*      */         }
/*      */       }
/* 1110 */       if (!replace)
/*      */       {
/* 1113 */         dispatchAggregateEvents(node, this.savedEnclosingAttr);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1118 */     if (this.ranges != null) {
/* 1119 */       int size = this.ranges.size();
/* 1120 */       for (int i = 0; i != size; i++)
/* 1121 */         ((RangeImpl)this.ranges.elementAt(i)).insertedNodeFromDOM(newInternal);
/*      */     }
/*      */   }
/*      */ 
/*      */   void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace)
/*      */   {
/* 1132 */     if (this.iterators != null) {
/* 1133 */       int size = this.iterators.size();
/* 1134 */       for (int i = 0; i != size; i++) {
/* 1135 */         ((NodeIteratorImpl)this.iterators.elementAt(i)).removeNode(oldChild);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1140 */     if (this.ranges != null) {
/* 1141 */       int size = this.ranges.size();
/* 1142 */       for (int i = 0; i != size; i++) {
/* 1143 */         ((RangeImpl)this.ranges.elementAt(i)).removeNode(oldChild);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1148 */     if (this.mutationEvents)
/*      */     {
/* 1153 */       if (!replace) {
/* 1154 */         saveEnclosingAttr(node);
/*      */       }
/*      */ 
/* 1157 */       LCount lc = LCount.lookup("DOMNodeRemoved");
/* 1158 */       if (lc.total > 0) {
/* 1159 */         MutationEventImpl me = new MutationEventImpl();
/* 1160 */         me.initMutationEvent("DOMNodeRemoved", true, false, node, null, null, null, (short)0);
/*      */ 
/* 1163 */         dispatchEvent(oldChild, me);
/*      */       }
/*      */ 
/* 1168 */       lc = LCount.lookup("DOMNodeRemovedFromDocument");
/*      */ 
/* 1170 */       if (lc.total > 0) {
/* 1171 */         NodeImpl eventAncestor = this;
/* 1172 */         if (this.savedEnclosingAttr != null) {
/* 1173 */           eventAncestor = (NodeImpl)this.savedEnclosingAttr.node.getOwnerElement();
/*      */         }
/* 1175 */         if (eventAncestor != null) {
/* 1176 */           for (NodeImpl p = eventAncestor.parentNode(); 
/* 1177 */             p != null; p = p.parentNode()) {
/* 1178 */             eventAncestor = p;
/*      */           }
/* 1180 */           if (eventAncestor.getNodeType() == 9) {
/* 1181 */             MutationEventImpl me = new MutationEventImpl();
/* 1182 */             me.initMutationEvent("DOMNodeRemovedFromDocument", false, false, null, null, null, null, (short)0);
/*      */ 
/* 1186 */             dispatchEventToSubtree(oldChild, me);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void removedNode(NodeImpl node, boolean replace)
/*      */   {
/* 1197 */     if (this.mutationEvents)
/*      */     {
/* 1201 */       if (!replace)
/* 1202 */         dispatchAggregateEvents(node, this.savedEnclosingAttr);
/*      */     }
/*      */   }
/*      */ 
/*      */   void replacingNode(NodeImpl node)
/*      */   {
/* 1211 */     if (this.mutationEvents)
/* 1212 */       saveEnclosingAttr(node);
/*      */   }
/*      */ 
/*      */   void replacingData(NodeImpl node)
/*      */   {
/* 1220 */     if (this.mutationEvents)
/* 1221 */       saveEnclosingAttr(node);
/*      */   }
/*      */ 
/*      */   void replacedNode(NodeImpl node)
/*      */   {
/* 1229 */     if (this.mutationEvents)
/* 1230 */       dispatchAggregateEvents(node, this.savedEnclosingAttr);
/*      */   }
/*      */ 
/*      */   void modifiedAttrValue(AttrImpl attr, String oldvalue)
/*      */   {
/* 1238 */     if (this.mutationEvents)
/*      */     {
/* 1240 */       dispatchAggregateEvents(attr, attr, oldvalue, (short)1);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setAttrNode(AttrImpl attr, AttrImpl previous)
/*      */   {
/* 1249 */     if (this.mutationEvents)
/*      */     {
/* 1251 */       if (previous == null) {
/* 1252 */         dispatchAggregateEvents(attr.ownerNode, attr, null, (short)2);
/*      */       }
/*      */       else
/*      */       {
/* 1256 */         dispatchAggregateEvents(attr.ownerNode, attr, previous.getNodeValue(), (short)1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name)
/*      */   {
/* 1270 */     if (this.mutationEvents)
/*      */     {
/* 1273 */       LCount lc = LCount.lookup("DOMAttrModified");
/* 1274 */       if (lc.total > 0) {
/* 1275 */         MutationEventImpl me = new MutationEventImpl();
/* 1276 */         me.initMutationEvent("DOMAttrModified", true, false, attr, attr.getNodeValue(), null, name, (short)3);
/*      */ 
/* 1280 */         dispatchEvent(oldOwner, me);
/*      */       }
/*      */ 
/* 1286 */       dispatchAggregateEvents(oldOwner, null, null, (short)0);
/*      */     }
/*      */   }
/*      */ 
/*      */   void renamedAttrNode(Attr oldAt, Attr newAt)
/*      */   {
/*      */   }
/*      */ 
/*      */   void renamedElement(Element oldEl, Element newEl)
/*      */   {
/*      */   }
/*      */ 
/*      */   class EnclosingAttr
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 5208387723391647216L;
/*      */     AttrImpl node;
/*      */     String oldvalue;
/*      */ 
/*      */     EnclosingAttr()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class LEntry
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -8426757059492421631L;
/*      */     String type;
/*      */     EventListener listener;
/*      */     boolean useCapture;
/*      */ 
/*      */     LEntry(String type, EventListener listener, boolean useCapture)
/*      */     {
/*  500 */       this.type = type;
/*  501 */       this.listener = listener;
/*  502 */       this.useCapture = useCapture;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DocumentImpl
 * JD-Core Version:    0.6.2
 */