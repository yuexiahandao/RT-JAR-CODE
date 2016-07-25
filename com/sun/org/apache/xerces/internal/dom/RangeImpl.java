/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.CharacterData;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.ranges.Range;
/*      */ import org.w3c.dom.ranges.RangeException;
/*      */ 
/*      */ public class RangeImpl
/*      */   implements Range
/*      */ {
/*      */   DocumentImpl fDocument;
/*      */   Node fStartContainer;
/*      */   Node fEndContainer;
/*      */   int fStartOffset;
/*      */   int fEndOffset;
/*      */   boolean fIsCollapsed;
/*   57 */   boolean fDetach = false;
/*   58 */   Node fInsertNode = null;
/*   59 */   Node fDeleteNode = null;
/*   60 */   Node fSplitNode = null;
/*      */ 
/*   62 */   boolean fInsertedFromRange = false;
/*      */ 
/* 1004 */   Node fRemoveChild = null;
/*      */   static final int EXTRACT_CONTENTS = 1;
/*      */   static final int CLONE_CONTENTS = 2;
/*      */   static final int DELETE_CONTENTS = 3;
/*      */ 
/*      */   public RangeImpl(DocumentImpl document)
/*      */   {
/*   69 */     this.fDocument = document;
/*   70 */     this.fStartContainer = document;
/*   71 */     this.fEndContainer = document;
/*   72 */     this.fStartOffset = 0;
/*   73 */     this.fEndOffset = 0;
/*   74 */     this.fDetach = false;
/*      */   }
/*      */ 
/*      */   public Node getStartContainer() {
/*   78 */     if (this.fDetach) {
/*   79 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*   83 */     return this.fStartContainer;
/*      */   }
/*      */ 
/*      */   public int getStartOffset() {
/*   87 */     if (this.fDetach) {
/*   88 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*   92 */     return this.fStartOffset;
/*      */   }
/*      */ 
/*      */   public Node getEndContainer() {
/*   96 */     if (this.fDetach) {
/*   97 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  101 */     return this.fEndContainer;
/*      */   }
/*      */ 
/*      */   public int getEndOffset() {
/*  105 */     if (this.fDetach) {
/*  106 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  110 */     return this.fEndOffset;
/*      */   }
/*      */ 
/*      */   public boolean getCollapsed() {
/*  114 */     if (this.fDetach) {
/*  115 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  119 */     return (this.fStartContainer == this.fEndContainer) && (this.fStartOffset == this.fEndOffset);
/*      */   }
/*      */ 
/*      */   public Node getCommonAncestorContainer()
/*      */   {
/*  124 */     if (this.fDetach) {
/*  125 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  129 */     Vector startV = new Vector();
/*      */ 
/*  131 */     for (Node node = this.fStartContainer; node != null; 
/*  132 */       node = node.getParentNode())
/*      */     {
/*  134 */       startV.addElement(node);
/*      */     }
/*  136 */     Vector endV = new Vector();
/*  137 */     for (node = this.fEndContainer; node != null; 
/*  138 */       node = node.getParentNode())
/*      */     {
/*  140 */       endV.addElement(node);
/*      */     }
/*  142 */     int s = startV.size() - 1;
/*  143 */     int e = endV.size() - 1;
/*  144 */     Object result = null;
/*  145 */     while ((s >= 0) && (e >= 0) && 
/*  146 */       (startV.elementAt(s) == endV.elementAt(e))) {
/*  147 */       result = startV.elementAt(s);
/*      */ 
/*  151 */       s--;
/*  152 */       e--;
/*      */     }
/*  154 */     return (Node)result;
/*      */   }
/*      */ 
/*      */   public void setStart(Node refNode, int offset)
/*      */     throws RangeException, DOMException
/*      */   {
/*  161 */     if (this.fDocument.errorChecking) {
/*  162 */       if (this.fDetach) {
/*  163 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  167 */       if (!isLegalContainer(refNode)) {
/*  168 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  172 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  173 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  179 */     checkIndex(refNode, offset);
/*      */ 
/*  181 */     this.fStartContainer = refNode;
/*  182 */     this.fStartOffset = offset;
/*      */ 
/*  189 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  191 */       collapse(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnd(Node refNode, int offset)
/*      */     throws RangeException, DOMException
/*      */   {
/*  198 */     if (this.fDocument.errorChecking) {
/*  199 */       if (this.fDetach) {
/*  200 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  204 */       if (!isLegalContainer(refNode)) {
/*  205 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  209 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  210 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  216 */     checkIndex(refNode, offset);
/*      */ 
/*  218 */     this.fEndContainer = refNode;
/*  219 */     this.fEndOffset = offset;
/*      */ 
/*  226 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  228 */       collapse(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setStartBefore(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  235 */     if (this.fDocument.errorChecking) {
/*  236 */       if (this.fDetach) {
/*  237 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  241 */       if ((!hasLegalRootContainer(refNode)) || (!isLegalContainedNode(refNode)))
/*      */       {
/*  244 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  248 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  249 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  255 */     this.fStartContainer = refNode.getParentNode();
/*  256 */     int i = 0;
/*  257 */     for (Node n = refNode; n != null; n = n.getPreviousSibling()) {
/*  258 */       i++;
/*      */     }
/*  260 */     this.fStartOffset = (i - 1);
/*      */ 
/*  267 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  269 */       collapse(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setStartAfter(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  276 */     if (this.fDocument.errorChecking) {
/*  277 */       if (this.fDetach) {
/*  278 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  282 */       if ((!hasLegalRootContainer(refNode)) || (!isLegalContainedNode(refNode)))
/*      */       {
/*  284 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  288 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  289 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  294 */     this.fStartContainer = refNode.getParentNode();
/*  295 */     int i = 0;
/*  296 */     for (Node n = refNode; n != null; n = n.getPreviousSibling()) {
/*  297 */       i++;
/*      */     }
/*  299 */     this.fStartOffset = i;
/*      */ 
/*  306 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  308 */       collapse(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEndBefore(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  315 */     if (this.fDocument.errorChecking) {
/*  316 */       if (this.fDetach) {
/*  317 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  321 */       if ((!hasLegalRootContainer(refNode)) || (!isLegalContainedNode(refNode)))
/*      */       {
/*  323 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  327 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  328 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  333 */     this.fEndContainer = refNode.getParentNode();
/*  334 */     int i = 0;
/*  335 */     for (Node n = refNode; n != null; n = n.getPreviousSibling()) {
/*  336 */       i++;
/*      */     }
/*  338 */     this.fEndOffset = (i - 1);
/*      */ 
/*  345 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  347 */       collapse(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEndAfter(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  354 */     if (this.fDocument.errorChecking) {
/*  355 */       if (this.fDetach) {
/*  356 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  360 */       if ((!hasLegalRootContainer(refNode)) || (!isLegalContainedNode(refNode)))
/*      */       {
/*  362 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  366 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  367 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  372 */     this.fEndContainer = refNode.getParentNode();
/*  373 */     int i = 0;
/*  374 */     for (Node n = refNode; n != null; n = n.getPreviousSibling()) {
/*  375 */       i++;
/*      */     }
/*  377 */     this.fEndOffset = i;
/*      */ 
/*  384 */     if ((getCommonAncestorContainer() == null) || ((this.fStartContainer == this.fEndContainer) && (this.fEndOffset < this.fStartOffset)))
/*      */     {
/*  386 */       collapse(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void collapse(boolean toStart)
/*      */   {
/*  392 */     if (this.fDetach) {
/*  393 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  398 */     if (toStart) {
/*  399 */       this.fEndContainer = this.fStartContainer;
/*  400 */       this.fEndOffset = this.fStartOffset;
/*      */     } else {
/*  402 */       this.fStartContainer = this.fEndContainer;
/*  403 */       this.fStartOffset = this.fEndOffset;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void selectNode(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  410 */     if (this.fDocument.errorChecking) {
/*  411 */       if (this.fDetach) {
/*  412 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  416 */       if ((!isLegalContainer(refNode.getParentNode())) || (!isLegalContainedNode(refNode)))
/*      */       {
/*  418 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  422 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  423 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  428 */     Node parent = refNode.getParentNode();
/*  429 */     if (parent != null)
/*      */     {
/*  431 */       this.fStartContainer = parent;
/*  432 */       this.fEndContainer = parent;
/*  433 */       int i = 0;
/*  434 */       for (Node n = refNode; n != null; n = n.getPreviousSibling()) {
/*  435 */         i++;
/*      */       }
/*  437 */       this.fStartOffset = (i - 1);
/*  438 */       this.fEndOffset = (this.fStartOffset + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void selectNodeContents(Node refNode)
/*      */     throws RangeException
/*      */   {
/*  445 */     if (this.fDocument.errorChecking) {
/*  446 */       if (this.fDetach) {
/*  447 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  451 */       if (!isLegalContainer(refNode)) {
/*  452 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*  456 */       if ((this.fDocument != refNode.getOwnerDocument()) && (this.fDocument != refNode)) {
/*  457 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  462 */     this.fStartContainer = refNode;
/*  463 */     this.fEndContainer = refNode;
/*  464 */     Node first = refNode.getFirstChild();
/*  465 */     this.fStartOffset = 0;
/*  466 */     if (first == null) {
/*  467 */       this.fEndOffset = 0;
/*      */     } else {
/*  469 */       int i = 0;
/*  470 */       for (Node n = first; n != null; n = n.getNextSibling()) {
/*  471 */         i++;
/*      */       }
/*  473 */       this.fEndOffset = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   public short compareBoundaryPoints(short how, Range sourceRange)
/*      */     throws DOMException
/*      */   {
/*  481 */     if (this.fDocument.errorChecking) {
/*  482 */       if (this.fDetach) {
/*  483 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  488 */       if (((this.fDocument != sourceRange.getStartContainer().getOwnerDocument()) && (this.fDocument != sourceRange.getStartContainer()) && (sourceRange.getStartContainer() != null)) || ((this.fDocument != sourceRange.getEndContainer().getOwnerDocument()) && (this.fDocument != sourceRange.getEndContainer()) && (sourceRange.getStartContainer() != null)))
/*      */       {
/*  494 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */     }
/*      */     int offsetB;
/*      */     Node endPointA;
/*      */     Node endPointB;
/*      */     int offsetA;
/*      */     int offsetB;
/*  504 */     if (how == 0) {
/*  505 */       Node endPointA = sourceRange.getStartContainer();
/*  506 */       Node endPointB = this.fStartContainer;
/*  507 */       int offsetA = sourceRange.getStartOffset();
/*  508 */       offsetB = this.fStartOffset;
/*      */     }
/*      */     else
/*      */     {
/*      */       int offsetB;
/*  510 */       if (how == 1) {
/*  511 */         Node endPointA = sourceRange.getStartContainer();
/*  512 */         Node endPointB = this.fEndContainer;
/*  513 */         int offsetA = sourceRange.getStartOffset();
/*  514 */         offsetB = this.fEndOffset;
/*      */       }
/*      */       else
/*      */       {
/*      */         int offsetB;
/*  516 */         if (how == 3) {
/*  517 */           Node endPointA = sourceRange.getEndContainer();
/*  518 */           Node endPointB = this.fStartContainer;
/*  519 */           int offsetA = sourceRange.getEndOffset();
/*  520 */           offsetB = this.fStartOffset;
/*      */         } else {
/*  522 */           endPointA = sourceRange.getEndContainer();
/*  523 */           endPointB = this.fEndContainer;
/*  524 */           offsetA = sourceRange.getEndOffset();
/*  525 */           offsetB = this.fEndOffset;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  536 */     if (endPointA == endPointB) {
/*  537 */       if (offsetA < offsetB) return 1;
/*  538 */       if (offsetA == offsetB) return 0;
/*  539 */       return -1;
/*      */     }
/*      */ 
/*  543 */     Node c = endPointB; for (Node p = c.getParentNode(); 
/*  544 */       p != null; 
/*  545 */       p = p.getParentNode())
/*      */     {
/*  547 */       if (p == endPointA) {
/*  548 */         int index = indexOf(c, endPointA);
/*  549 */         if (offsetA <= index) return 1;
/*  550 */         return -1;
/*      */       }
/*  545 */       c = p;
/*      */     }
/*      */ 
/*  556 */     Node c = endPointA; for (Node p = c.getParentNode(); 
/*  557 */       p != null; 
/*  558 */       p = p.getParentNode())
/*      */     {
/*  560 */       if (p == endPointB) {
/*  561 */         int index = indexOf(c, endPointB);
/*  562 */         if (index < offsetB) return 1;
/*  563 */         return -1;
/*      */       }
/*  558 */       c = p;
/*      */     }
/*      */ 
/*  571 */     int depthDiff = 0;
/*  572 */     for (Node n = endPointA; n != null; n = n.getParentNode())
/*  573 */       depthDiff++;
/*  574 */     for (Node n = endPointB; n != null; n = n.getParentNode())
/*  575 */       depthDiff--;
/*  576 */     while (depthDiff > 0) {
/*  577 */       endPointA = endPointA.getParentNode();
/*  578 */       depthDiff--;
/*      */     }
/*  580 */     while (depthDiff < 0) {
/*  581 */       endPointB = endPointB.getParentNode();
/*  582 */       depthDiff++;
/*      */     }
/*  584 */     Node pA = endPointA.getParentNode();
/*  585 */     for (Node pB = endPointB.getParentNode(); 
/*  586 */       pA != pB; 
/*  587 */       pB = pB.getParentNode())
/*      */     {
/*  589 */       endPointA = pA;
/*  590 */       endPointB = pB;
/*      */ 
/*  587 */       pA = pA.getParentNode();
/*      */     }
/*      */ 
/*  592 */     for (Node n = endPointA.getNextSibling(); 
/*  593 */       n != null; 
/*  594 */       n = n.getNextSibling())
/*      */     {
/*  596 */       if (n == endPointB) {
/*  597 */         return 1;
/*      */       }
/*      */     }
/*  600 */     return -1;
/*      */   }
/*      */ 
/*      */   public void deleteContents()
/*      */     throws DOMException
/*      */   {
/*  606 */     traverseContents(3);
/*      */   }
/*      */ 
/*      */   public DocumentFragment extractContents()
/*      */     throws DOMException
/*      */   {
/*  612 */     return traverseContents(1);
/*      */   }
/*      */ 
/*      */   public DocumentFragment cloneContents()
/*      */     throws DOMException
/*      */   {
/*  618 */     return traverseContents(2);
/*      */   }
/*      */ 
/*      */   public void insertNode(Node newNode)
/*      */     throws DOMException, RangeException
/*      */   {
/*  624 */     if (newNode == null) return;
/*      */ 
/*  626 */     int type = newNode.getNodeType();
/*      */ 
/*  628 */     if (this.fDocument.errorChecking) {
/*  629 */       if (this.fDetach) {
/*  630 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  634 */       if (this.fDocument != newNode.getOwnerDocument()) {
/*  635 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*  639 */       if ((type == 2) || (type == 6) || (type == 12) || (type == 9))
/*      */       {
/*  644 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  651 */     int currentChildren = 0;
/*  652 */     this.fInsertedFromRange = true;
/*      */ 
/*  655 */     if (this.fStartContainer.getNodeType() == 3)
/*      */     {
/*  657 */       Node parent = this.fStartContainer.getParentNode();
/*  658 */       currentChildren = parent.getChildNodes().getLength();
/*      */ 
/*  660 */       Node cloneCurrent = this.fStartContainer.cloneNode(false);
/*  661 */       ((TextImpl)cloneCurrent).setNodeValueInternal(cloneCurrent.getNodeValue().substring(this.fStartOffset));
/*      */ 
/*  663 */       ((TextImpl)this.fStartContainer).setNodeValueInternal(this.fStartContainer.getNodeValue().substring(0, this.fStartOffset));
/*      */ 
/*  665 */       Node next = this.fStartContainer.getNextSibling();
/*  666 */       if (next != null) {
/*  667 */         if (parent != null) {
/*  668 */           parent.insertBefore(newNode, next);
/*  669 */           parent.insertBefore(cloneCurrent, next);
/*      */         }
/*      */       }
/*  672 */       else if (parent != null) {
/*  673 */         parent.appendChild(newNode);
/*  674 */         parent.appendChild(cloneCurrent);
/*      */       }
/*      */ 
/*  678 */       if (this.fEndContainer == this.fStartContainer) {
/*  679 */         this.fEndContainer = cloneCurrent;
/*  680 */         this.fEndOffset -= this.fStartOffset;
/*      */       }
/*  682 */       else if (this.fEndContainer == parent)
/*      */       {
/*  684 */         this.fEndOffset += parent.getChildNodes().getLength() - currentChildren;
/*      */       }
/*      */ 
/*  688 */       signalSplitData(this.fStartContainer, cloneCurrent, this.fStartOffset);
/*      */     }
/*      */     else
/*      */     {
/*  692 */       if (this.fEndContainer == this.fStartContainer) {
/*  693 */         currentChildren = this.fEndContainer.getChildNodes().getLength();
/*      */       }
/*  695 */       Node current = this.fStartContainer.getFirstChild();
/*  696 */       int i = 0;
/*  697 */       for (i = 0; (i < this.fStartOffset) && (current != null); i++) {
/*  698 */         current = current.getNextSibling();
/*      */       }
/*  700 */       if (current != null)
/*  701 */         this.fStartContainer.insertBefore(newNode, current);
/*      */       else {
/*  703 */         this.fStartContainer.appendChild(newNode);
/*      */       }
/*      */ 
/*  707 */       if ((this.fEndContainer == this.fStartContainer) && (this.fEndOffset != 0)) {
/*  708 */         this.fEndOffset += this.fEndContainer.getChildNodes().getLength() - currentChildren;
/*      */       }
/*      */     }
/*  711 */     this.fInsertedFromRange = false;
/*      */   }
/*      */ 
/*      */   public void surroundContents(Node newParent)
/*      */     throws DOMException, RangeException
/*      */   {
/*  717 */     if (newParent == null) return;
/*  718 */     int type = newParent.getNodeType();
/*      */ 
/*  720 */     if (this.fDocument.errorChecking) {
/*  721 */       if (this.fDetach) {
/*  722 */         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */       }
/*      */ 
/*  726 */       if ((type == 2) || (type == 6) || (type == 12) || (type == 10) || (type == 9) || (type == 11))
/*      */       {
/*  733 */         throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  739 */     Node realStart = this.fStartContainer;
/*  740 */     Node realEnd = this.fEndContainer;
/*  741 */     if (this.fStartContainer.getNodeType() == 3) {
/*  742 */       realStart = this.fStartContainer.getParentNode();
/*      */     }
/*  744 */     if (this.fEndContainer.getNodeType() == 3) {
/*  745 */       realEnd = this.fEndContainer.getParentNode();
/*      */     }
/*      */ 
/*  748 */     if (realStart != realEnd) {
/*  749 */       throw new RangeExceptionImpl((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "BAD_BOUNDARYPOINTS_ERR", null));
/*      */     }
/*      */ 
/*  754 */     DocumentFragment frag = extractContents();
/*  755 */     insertNode(newParent);
/*  756 */     newParent.appendChild(frag);
/*  757 */     selectNode(newParent);
/*      */   }
/*      */ 
/*      */   public Range cloneRange() {
/*  761 */     if (this.fDetach) {
/*  762 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  767 */     Range range = this.fDocument.createRange();
/*  768 */     range.setStart(this.fStartContainer, this.fStartOffset);
/*  769 */     range.setEnd(this.fEndContainer, this.fEndOffset);
/*  770 */     return range;
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  774 */     if (this.fDetach) {
/*  775 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  780 */     Node node = this.fStartContainer;
/*  781 */     Node stopNode = this.fEndContainer;
/*  782 */     StringBuffer sb = new StringBuffer();
/*  783 */     if ((this.fStartContainer.getNodeType() == 3) || (this.fStartContainer.getNodeType() == 4))
/*      */     {
/*  786 */       if (this.fStartContainer == this.fEndContainer) {
/*  787 */         sb.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset, this.fEndOffset));
/*  788 */         return sb.toString();
/*      */       }
/*  790 */       sb.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset));
/*  791 */       node = nextNode(node, true);
/*      */     }
/*      */     else
/*      */     {
/*  795 */       node = node.getFirstChild();
/*  796 */       if (this.fStartOffset > 0) {
/*  797 */         int counter = 0;
/*  798 */         while ((counter < this.fStartOffset) && (node != null)) {
/*  799 */           node = node.getNextSibling();
/*  800 */           counter++;
/*      */         }
/*      */       }
/*  803 */       if (node == null) {
/*  804 */         node = nextNode(this.fStartContainer, false);
/*      */       }
/*      */     }
/*  807 */     if ((this.fEndContainer.getNodeType() != 3) && (this.fEndContainer.getNodeType() != 4))
/*      */     {
/*  809 */       int i = this.fEndOffset;
/*  810 */       stopNode = this.fEndContainer.getFirstChild();
/*  811 */       while ((i > 0) && (stopNode != null)) {
/*  812 */         i--;
/*  813 */         stopNode = stopNode.getNextSibling();
/*      */       }
/*  815 */       if (stopNode == null)
/*  816 */         stopNode = nextNode(this.fEndContainer, false);
/*      */     }
/*  818 */     while ((node != stopNode) && 
/*  819 */       (node != null)) {
/*  820 */       if ((node.getNodeType() == 3) || (node.getNodeType() == 4))
/*      */       {
/*  822 */         sb.append(node.getNodeValue());
/*      */       }
/*      */ 
/*  825 */       node = nextNode(node, true);
/*      */     }
/*      */ 
/*  828 */     if ((this.fEndContainer.getNodeType() == 3) || (this.fEndContainer.getNodeType() == 4))
/*      */     {
/*  830 */       sb.append(this.fEndContainer.getNodeValue().substring(0, this.fEndOffset));
/*      */     }
/*  832 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public void detach() {
/*  836 */     if (this.fDetach) {
/*  837 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/*  841 */     this.fDetach = true;
/*  842 */     this.fDocument.removeRange(this);
/*      */   }
/*      */ 
/*      */   void signalSplitData(Node node, Node newNode, int offset)
/*      */   {
/*  854 */     this.fSplitNode = node;
/*      */ 
/*  856 */     this.fDocument.splitData(node, newNode, offset);
/*  857 */     this.fSplitNode = null;
/*      */   }
/*      */ 
/*      */   void receiveSplitData(Node node, Node newNode, int offset)
/*      */   {
/*  864 */     if ((node == null) || (newNode == null)) return;
/*  865 */     if (this.fSplitNode == node) return;
/*      */ 
/*  867 */     if ((node == this.fStartContainer) && (this.fStartContainer.getNodeType() == 3))
/*      */     {
/*  869 */       if (this.fStartOffset > offset) {
/*  870 */         this.fStartOffset -= offset;
/*  871 */         this.fStartContainer = newNode;
/*      */       }
/*      */     }
/*  874 */     if ((node == this.fEndContainer) && (this.fEndContainer.getNodeType() == 3))
/*      */     {
/*  876 */       if (this.fEndOffset > offset) {
/*  877 */         this.fEndOffset -= offset;
/*  878 */         this.fEndContainer = newNode;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void deleteData(CharacterData node, int offset, int count)
/*      */   {
/*  888 */     this.fDeleteNode = node;
/*  889 */     node.deleteData(offset, count);
/*  890 */     this.fDeleteNode = null;
/*      */   }
/*      */ 
/*      */   void receiveDeletedText(Node node, int offset, int count)
/*      */   {
/*  899 */     if (node == null) return;
/*  900 */     if (this.fDeleteNode == node) return;
/*  901 */     if ((node == this.fStartContainer) && (this.fStartContainer.getNodeType() == 3))
/*      */     {
/*  903 */       if (this.fStartOffset > offset + count) {
/*  904 */         this.fStartOffset = (offset + (this.fStartOffset - (offset + count)));
/*      */       }
/*  906 */       else if (this.fStartOffset > offset) {
/*  907 */         this.fStartOffset = offset;
/*      */       }
/*      */     }
/*  910 */     if ((node == this.fEndContainer) && (this.fEndContainer.getNodeType() == 3))
/*      */     {
/*  912 */       if (this.fEndOffset > offset + count) {
/*  913 */         this.fEndOffset = (offset + (this.fEndOffset - (offset + count)));
/*      */       }
/*  915 */       else if (this.fEndOffset > offset)
/*  916 */         this.fEndOffset = offset;
/*      */     }
/*      */   }
/*      */ 
/*      */   void insertData(CharacterData node, int index, String insert)
/*      */   {
/*  926 */     this.fInsertNode = node;
/*  927 */     node.insertData(index, insert);
/*  928 */     this.fInsertNode = null;
/*      */   }
/*      */ 
/*      */   void receiveInsertedText(Node node, int index, int len)
/*      */   {
/*  937 */     if (node == null) return;
/*  938 */     if (this.fInsertNode == node) return;
/*  939 */     if ((node == this.fStartContainer) && (this.fStartContainer.getNodeType() == 3))
/*      */     {
/*  941 */       if (index < this.fStartOffset) {
/*  942 */         this.fStartOffset += len;
/*      */       }
/*      */     }
/*  945 */     if ((node == this.fEndContainer) && (this.fEndContainer.getNodeType() == 3))
/*      */     {
/*  947 */       if (index < this.fEndOffset)
/*  948 */         this.fEndOffset += len;
/*      */     }
/*      */   }
/*      */ 
/*      */   void receiveReplacedText(Node node)
/*      */   {
/*  959 */     if (node == null) return;
/*  960 */     if ((node == this.fStartContainer) && (this.fStartContainer.getNodeType() == 3))
/*      */     {
/*  962 */       this.fStartOffset = 0;
/*      */     }
/*  964 */     if ((node == this.fEndContainer) && (this.fEndContainer.getNodeType() == 3))
/*      */     {
/*  966 */       this.fEndOffset = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertedNodeFromDOM(Node node)
/*      */   {
/*  976 */     if (node == null) return;
/*  977 */     if (this.fInsertNode == node) return;
/*  978 */     if (this.fInsertedFromRange) return;
/*      */ 
/*  980 */     Node parent = node.getParentNode();
/*      */ 
/*  982 */     if (parent == this.fStartContainer) {
/*  983 */       int index = indexOf(node, this.fStartContainer);
/*  984 */       if (index < this.fStartOffset) {
/*  985 */         this.fStartOffset += 1;
/*      */       }
/*      */     }
/*      */ 
/*  989 */     if (parent == this.fEndContainer) {
/*  990 */       int index = indexOf(node, this.fEndContainer);
/*  991 */       if (index < this.fEndOffset)
/*  992 */         this.fEndOffset += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   Node removeChild(Node parent, Node child)
/*      */   {
/* 1006 */     this.fRemoveChild = child;
/* 1007 */     Node n = parent.removeChild(child);
/* 1008 */     this.fRemoveChild = null;
/* 1009 */     return n;
/*      */   }
/*      */ 
/*      */   void removeNode(Node node)
/*      */   {
/* 1017 */     if (node == null) return;
/* 1018 */     if (this.fRemoveChild == node) return;
/*      */ 
/* 1020 */     Node parent = node.getParentNode();
/*      */ 
/* 1022 */     if (parent == this.fStartContainer) {
/* 1023 */       int index = indexOf(node, this.fStartContainer);
/* 1024 */       if (index < this.fStartOffset) {
/* 1025 */         this.fStartOffset -= 1;
/*      */       }
/*      */     }
/*      */ 
/* 1029 */     if (parent == this.fEndContainer) {
/* 1030 */       int index = indexOf(node, this.fEndContainer);
/* 1031 */       if (index < this.fEndOffset) {
/* 1032 */         this.fEndOffset -= 1;
/*      */       }
/*      */     }
/*      */ 
/* 1036 */     if ((parent != this.fStartContainer) || (parent != this.fEndContainer))
/*      */     {
/* 1038 */       if (isAncestorOf(node, this.fStartContainer)) {
/* 1039 */         this.fStartContainer = parent;
/* 1040 */         this.fStartOffset = indexOf(node, parent);
/*      */       }
/* 1042 */       if (isAncestorOf(node, this.fEndContainer)) {
/* 1043 */         this.fEndContainer = parent;
/* 1044 */         this.fEndOffset = indexOf(node, parent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private DocumentFragment traverseContents(int how)
/*      */     throws DOMException
/*      */   {
/* 1093 */     if ((this.fStartContainer == null) || (this.fEndContainer == null)) {
/* 1094 */       return null;
/*      */     }
/*      */ 
/* 1098 */     if (this.fDetach) {
/* 1099 */       throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
/*      */     }
/*      */ 
/* 1113 */     if (this.fStartContainer == this.fEndContainer) {
/* 1114 */       return traverseSameContainer(how);
/*      */     }
/*      */ 
/* 1120 */     int endContainerDepth = 0;
/* 1121 */     Node c = this.fEndContainer; for (Node p = c.getParentNode(); 
/* 1122 */       p != null; 
/* 1123 */       p = p.getParentNode())
/*      */     {
/* 1125 */       if (p == this.fStartContainer)
/* 1126 */         return traverseCommonStartContainer(c, how);
/* 1127 */       endContainerDepth++;
/*      */ 
/* 1123 */       c = p;
/*      */     }
/*      */ 
/* 1132 */     int startContainerDepth = 0;
/* 1133 */     Node c = this.fStartContainer; for (Node p = c.getParentNode(); 
/* 1134 */       p != null; 
/* 1135 */       p = p.getParentNode())
/*      */     {
/* 1137 */       if (p == this.fEndContainer)
/* 1138 */         return traverseCommonEndContainer(c, how);
/* 1139 */       startContainerDepth++;
/*      */ 
/* 1135 */       c = p;
/*      */     }
/*      */ 
/* 1144 */     int depthDiff = startContainerDepth - endContainerDepth;
/*      */ 
/* 1146 */     Node startNode = this.fStartContainer;
/* 1147 */     while (depthDiff > 0) {
/* 1148 */       startNode = startNode.getParentNode();
/* 1149 */       depthDiff--;
/*      */     }
/*      */ 
/* 1152 */     Node endNode = this.fEndContainer;
/* 1153 */     while (depthDiff < 0) {
/* 1154 */       endNode = endNode.getParentNode();
/* 1155 */       depthDiff++;
/*      */     }
/*      */ 
/* 1159 */     Node sp = startNode.getParentNode(); for (Node ep = endNode.getParentNode(); 
/* 1160 */       sp != ep; 
/* 1161 */       ep = ep.getParentNode())
/*      */     {
/* 1163 */       startNode = sp;
/* 1164 */       endNode = ep;
/*      */ 
/* 1161 */       sp = sp.getParentNode();
/*      */     }
/*      */ 
/* 1166 */     return traverseCommonAncestors(startNode, endNode, how);
/*      */   }
/*      */ 
/*      */   private DocumentFragment traverseSameContainer(int how)
/*      */   {
/* 1201 */     DocumentFragment frag = null;
/* 1202 */     if (how != 3) {
/* 1203 */       frag = this.fDocument.createDocumentFragment();
/*      */     }
/*      */ 
/* 1206 */     if (this.fStartOffset == this.fEndOffset) {
/* 1207 */       return frag;
/*      */     }
/*      */ 
/* 1210 */     if (this.fStartContainer.getNodeType() == 3)
/*      */     {
/* 1213 */       String s = this.fStartContainer.getNodeValue();
/* 1214 */       String sub = s.substring(this.fStartOffset, this.fEndOffset);
/*      */ 
/* 1217 */       if (how != 2)
/*      */       {
/* 1219 */         ((TextImpl)this.fStartContainer).deleteData(this.fStartOffset, this.fEndOffset - this.fStartOffset);
/*      */ 
/* 1222 */         collapse(true);
/*      */       }
/* 1224 */       if (how == 3)
/* 1225 */         return null;
/* 1226 */       frag.appendChild(this.fDocument.createTextNode(sub));
/* 1227 */       return frag;
/*      */     }
/*      */ 
/* 1231 */     Node n = getSelectedNode(this.fStartContainer, this.fStartOffset);
/* 1232 */     int cnt = this.fEndOffset - this.fStartOffset;
/* 1233 */     while (cnt > 0)
/*      */     {
/* 1235 */       Node sibling = n.getNextSibling();
/* 1236 */       Node xferNode = traverseFullySelected(n, how);
/* 1237 */       if (frag != null)
/* 1238 */         frag.appendChild(xferNode);
/* 1239 */       cnt--;
/* 1240 */       n = sibling;
/*      */     }
/*      */ 
/* 1244 */     if (how != 2)
/* 1245 */       collapse(true);
/* 1246 */     return frag;
/*      */   }
/*      */ 
/*      */   private DocumentFragment traverseCommonStartContainer(Node endAncestor, int how)
/*      */   {
/* 1287 */     DocumentFragment frag = null;
/* 1288 */     if (how != 3)
/* 1289 */       frag = this.fDocument.createDocumentFragment();
/* 1290 */     Node n = traverseRightBoundary(endAncestor, how);
/* 1291 */     if (frag != null) {
/* 1292 */       frag.appendChild(n);
/*      */     }
/* 1294 */     int endIdx = indexOf(endAncestor, this.fStartContainer);
/* 1295 */     int cnt = endIdx - this.fStartOffset;
/* 1296 */     if (cnt <= 0)
/*      */     {
/* 1300 */       if (how != 2)
/*      */       {
/* 1302 */         setEndBefore(endAncestor);
/* 1303 */         collapse(false);
/*      */       }
/* 1305 */       return frag;
/*      */     }
/*      */ 
/* 1308 */     n = endAncestor.getPreviousSibling();
/* 1309 */     while (cnt > 0)
/*      */     {
/* 1311 */       Node sibling = n.getPreviousSibling();
/* 1312 */       Node xferNode = traverseFullySelected(n, how);
/* 1313 */       if (frag != null)
/* 1314 */         frag.insertBefore(xferNode, frag.getFirstChild());
/* 1315 */       cnt--;
/* 1316 */       n = sibling;
/*      */     }
/*      */ 
/* 1320 */     if (how != 2)
/*      */     {
/* 1322 */       setEndBefore(endAncestor);
/* 1323 */       collapse(false);
/*      */     }
/* 1325 */     return frag;
/*      */   }
/*      */ 
/*      */   private DocumentFragment traverseCommonEndContainer(Node startAncestor, int how)
/*      */   {
/* 1366 */     DocumentFragment frag = null;
/* 1367 */     if (how != 3)
/* 1368 */       frag = this.fDocument.createDocumentFragment();
/* 1369 */     Node n = traverseLeftBoundary(startAncestor, how);
/* 1370 */     if (frag != null)
/* 1371 */       frag.appendChild(n);
/* 1372 */     int startIdx = indexOf(startAncestor, this.fEndContainer);
/* 1373 */     startIdx++;
/*      */ 
/* 1375 */     int cnt = this.fEndOffset - startIdx;
/* 1376 */     n = startAncestor.getNextSibling();
/* 1377 */     while (cnt > 0)
/*      */     {
/* 1379 */       Node sibling = n.getNextSibling();
/* 1380 */       Node xferNode = traverseFullySelected(n, how);
/* 1381 */       if (frag != null)
/* 1382 */         frag.appendChild(xferNode);
/* 1383 */       cnt--;
/* 1384 */       n = sibling;
/*      */     }
/*      */ 
/* 1387 */     if (how != 2)
/*      */     {
/* 1389 */       setStartAfter(startAncestor);
/* 1390 */       collapse(true);
/*      */     }
/*      */ 
/* 1393 */     return frag;
/*      */   }
/*      */ 
/*      */   private DocumentFragment traverseCommonAncestors(Node startAncestor, Node endAncestor, int how)
/*      */   {
/* 1441 */     DocumentFragment frag = null;
/* 1442 */     if (how != 3) {
/* 1443 */       frag = this.fDocument.createDocumentFragment();
/*      */     }
/* 1445 */     Node n = traverseLeftBoundary(startAncestor, how);
/* 1446 */     if (frag != null) {
/* 1447 */       frag.appendChild(n);
/*      */     }
/* 1449 */     Node commonParent = startAncestor.getParentNode();
/* 1450 */     int startOffset = indexOf(startAncestor, commonParent);
/* 1451 */     int endOffset = indexOf(endAncestor, commonParent);
/* 1452 */     startOffset++;
/*      */ 
/* 1454 */     int cnt = endOffset - startOffset;
/* 1455 */     Node sibling = startAncestor.getNextSibling();
/*      */ 
/* 1457 */     while (cnt > 0)
/*      */     {
/* 1459 */       Node nextSibling = sibling.getNextSibling();
/* 1460 */       n = traverseFullySelected(sibling, how);
/* 1461 */       if (frag != null)
/* 1462 */         frag.appendChild(n);
/* 1463 */       sibling = nextSibling;
/* 1464 */       cnt--;
/*      */     }
/*      */ 
/* 1467 */     n = traverseRightBoundary(endAncestor, how);
/* 1468 */     if (frag != null) {
/* 1469 */       frag.appendChild(n);
/*      */     }
/* 1471 */     if (how != 2)
/*      */     {
/* 1473 */       setStartAfter(startAncestor);
/* 1474 */       collapse(true);
/*      */     }
/* 1476 */     return frag;
/*      */   }
/*      */ 
/*      */   private Node traverseRightBoundary(Node root, int how)
/*      */   {
/* 1539 */     Node next = getSelectedNode(this.fEndContainer, this.fEndOffset - 1);
/* 1540 */     boolean isFullySelected = next != this.fEndContainer;
/*      */ 
/* 1542 */     if (next == root) {
/* 1543 */       return traverseNode(next, isFullySelected, false, how);
/*      */     }
/* 1545 */     Node parent = next.getParentNode();
/* 1546 */     Node clonedParent = traverseNode(parent, false, false, how);
/*      */ 
/* 1548 */     while (parent != null)
/*      */     {
/* 1550 */       while (next != null)
/*      */       {
/* 1552 */         Node prevSibling = next.getPreviousSibling();
/* 1553 */         Node clonedChild = traverseNode(next, isFullySelected, false, how);
/*      */ 
/* 1555 */         if (how != 3)
/*      */         {
/* 1557 */           clonedParent.insertBefore(clonedChild, clonedParent.getFirstChild());
/*      */         }
/*      */ 
/* 1562 */         isFullySelected = true;
/* 1563 */         next = prevSibling;
/*      */       }
/* 1565 */       if (parent == root) {
/* 1566 */         return clonedParent;
/*      */       }
/* 1568 */       next = parent.getPreviousSibling();
/* 1569 */       parent = parent.getParentNode();
/* 1570 */       Node clonedGrandParent = traverseNode(parent, false, false, how);
/* 1571 */       if (how != 3)
/* 1572 */         clonedGrandParent.appendChild(clonedParent);
/* 1573 */       clonedParent = clonedGrandParent;
/*      */     }
/*      */ 
/* 1578 */     return null;
/*      */   }
/*      */ 
/*      */   private Node traverseLeftBoundary(Node root, int how)
/*      */   {
/* 1642 */     Node next = getSelectedNode(getStartContainer(), getStartOffset());
/* 1643 */     boolean isFullySelected = next != getStartContainer();
/*      */ 
/* 1645 */     if (next == root) {
/* 1646 */       return traverseNode(next, isFullySelected, true, how);
/*      */     }
/* 1648 */     Node parent = next.getParentNode();
/* 1649 */     Node clonedParent = traverseNode(parent, false, true, how);
/*      */ 
/* 1651 */     while (parent != null)
/*      */     {
/* 1653 */       while (next != null)
/*      */       {
/* 1655 */         Node nextSibling = next.getNextSibling();
/* 1656 */         Node clonedChild = traverseNode(next, isFullySelected, true, how);
/*      */ 
/* 1658 */         if (how != 3)
/* 1659 */           clonedParent.appendChild(clonedChild);
/* 1660 */         isFullySelected = true;
/* 1661 */         next = nextSibling;
/*      */       }
/* 1663 */       if (parent == root) {
/* 1664 */         return clonedParent;
/*      */       }
/* 1666 */       next = parent.getNextSibling();
/* 1667 */       parent = parent.getParentNode();
/* 1668 */       Node clonedGrandParent = traverseNode(parent, false, true, how);
/* 1669 */       if (how != 3)
/* 1670 */         clonedGrandParent.appendChild(clonedParent);
/* 1671 */       clonedParent = clonedGrandParent;
/*      */     }
/*      */ 
/* 1676 */     return null;
/*      */   }
/*      */ 
/*      */   private Node traverseNode(Node n, boolean isFullySelected, boolean isLeft, int how)
/*      */   {
/* 1723 */     if (isFullySelected)
/* 1724 */       return traverseFullySelected(n, how);
/* 1725 */     if (n.getNodeType() == 3)
/* 1726 */       return traverseTextNode(n, isLeft, how);
/* 1727 */     return traversePartiallySelected(n, how);
/*      */   }
/*      */ 
/*      */   private Node traverseFullySelected(Node n, int how)
/*      */   {
/* 1759 */     switch (how)
/*      */     {
/*      */     case 2:
/* 1762 */       return n.cloneNode(true);
/*      */     case 1:
/* 1764 */       if (n.getNodeType() == 10)
/*      */       {
/* 1767 */         throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */       }
/*      */ 
/* 1771 */       return n;
/*      */     case 3:
/* 1773 */       n.getParentNode().removeChild(n);
/* 1774 */       return null;
/*      */     }
/* 1776 */     return null;
/*      */   }
/*      */ 
/*      */   private Node traversePartiallySelected(Node n, int how)
/*      */   {
/* 1808 */     switch (how)
/*      */     {
/*      */     case 3:
/* 1811 */       return null;
/*      */     case 1:
/*      */     case 2:
/* 1814 */       return n.cloneNode(false);
/*      */     }
/* 1816 */     return null;
/*      */   }
/*      */ 
/*      */   private Node traverseTextNode(Node n, boolean isLeft, int how)
/*      */   {
/* 1854 */     String txtValue = n.getNodeValue();
/*      */     String oldNodeValue;
/*      */     String newNodeValue;
/*      */     String oldNodeValue;
/* 1858 */     if (isLeft)
/*      */     {
/* 1860 */       int offset = getStartOffset();
/* 1861 */       String newNodeValue = txtValue.substring(offset);
/* 1862 */       oldNodeValue = txtValue.substring(0, offset);
/*      */     }
/*      */     else
/*      */     {
/* 1866 */       int offset = getEndOffset();
/* 1867 */       newNodeValue = txtValue.substring(0, offset);
/* 1868 */       oldNodeValue = txtValue.substring(offset);
/*      */     }
/*      */ 
/* 1871 */     if (how != 2)
/* 1872 */       n.setNodeValue(oldNodeValue);
/* 1873 */     if (how == 3)
/* 1874 */       return null;
/* 1875 */     Node newNode = n.cloneNode(false);
/* 1876 */     newNode.setNodeValue(newNodeValue);
/* 1877 */     return newNode;
/*      */   }
/*      */ 
/*      */   void checkIndex(Node refNode, int offset) throws DOMException
/*      */   {
/* 1882 */     if (offset < 0) {
/* 1883 */       throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
/*      */     }
/*      */ 
/* 1888 */     int type = refNode.getNodeType();
/*      */ 
/* 1892 */     if ((type == 3) || (type == 4) || (type == 8) || (type == 7))
/*      */     {
/* 1896 */       if (offset > refNode.getNodeValue().length()) {
/* 1897 */         throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/* 1904 */     else if (offset > refNode.getChildNodes().getLength())
/* 1905 */       throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null));
/*      */   }
/*      */ 
/*      */   private Node getRootContainer(Node node)
/*      */   {
/* 1917 */     if (node == null) {
/* 1918 */       return null;
/*      */     }
/* 1920 */     while (node.getParentNode() != null)
/* 1921 */       node = node.getParentNode();
/* 1922 */     return node;
/*      */   }
/*      */ 
/*      */   private boolean isLegalContainer(Node node)
/*      */   {
/* 1931 */     if (node == null) {
/* 1932 */       return false;
/*      */     }
/* 1934 */     while (node != null)
/*      */     {
/* 1936 */       switch (node.getNodeType())
/*      */       {
/*      */       case 6:
/*      */       case 10:
/*      */       case 12:
/* 1941 */         return false;
/*      */       }
/* 1943 */       node = node.getParentNode();
/*      */     }
/*      */ 
/* 1946 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean hasLegalRootContainer(Node node)
/*      */   {
/* 1959 */     if (node == null) {
/* 1960 */       return false;
/*      */     }
/* 1962 */     Node rootContainer = getRootContainer(node);
/* 1963 */     switch (rootContainer.getNodeType())
/*      */     {
/*      */     case 2:
/*      */     case 9:
/*      */     case 11:
/* 1968 */       return true;
/*      */     }
/* 1970 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isLegalContainedNode(Node node)
/*      */   {
/* 1979 */     if (node == null)
/* 1980 */       return false;
/* 1981 */     switch (node.getNodeType())
/*      */     {
/*      */     case 2:
/*      */     case 6:
/*      */     case 9:
/*      */     case 11:
/*      */     case 12:
/* 1988 */       return false;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/* 1990 */     case 10: } return true;
/*      */   }
/*      */ 
/*      */   Node nextNode(Node node, boolean visitChildren)
/*      */   {
/* 1995 */     if (node == null) return null;
/*      */ 
/* 1998 */     if (visitChildren) {
/* 1999 */       Node result = node.getFirstChild();
/* 2000 */       if (result != null) {
/* 2001 */         return result;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2006 */     Node result = node.getNextSibling();
/* 2007 */     if (result != null) {
/* 2008 */       return result;
/*      */     }
/*      */ 
/* 2013 */     Node parent = node.getParentNode();
/*      */ 
/* 2015 */     while ((parent != null) && (parent != this.fDocument))
/*      */     {
/* 2017 */       result = parent.getNextSibling();
/* 2018 */       if (result != null) {
/* 2019 */         return result;
/*      */       }
/* 2021 */       parent = parent.getParentNode();
/*      */     }
/*      */ 
/* 2027 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isAncestorOf(Node a, Node b)
/*      */   {
/* 2032 */     for (Node node = b; node != null; node = node.getParentNode()) {
/* 2033 */       if (node == a) return true;
/*      */     }
/* 2035 */     return false;
/*      */   }
/*      */ 
/*      */   int indexOf(Node child, Node parent)
/*      */   {
/* 2040 */     if (child.getParentNode() != parent) return -1;
/* 2041 */     int i = 0;
/* 2042 */     for (Node node = parent.getFirstChild(); node != child; node = node.getNextSibling()) {
/* 2043 */       i++;
/*      */     }
/* 2045 */     return i;
/*      */   }
/*      */ 
/*      */   private Node getSelectedNode(Node container, int offset)
/*      */   {
/* 2066 */     if (container.getNodeType() == 3) {
/* 2067 */       return container;
/*      */     }
/*      */ 
/* 2071 */     if (offset < 0) {
/* 2072 */       return container;
/*      */     }
/* 2074 */     Node child = container.getFirstChild();
/* 2075 */     while ((child != null) && (offset > 0))
/*      */     {
/* 2077 */       offset--;
/* 2078 */       child = child.getNextSibling();
/*      */     }
/* 2080 */     if (child != null)
/* 2081 */       return child;
/* 2082 */     return container;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.RangeImpl
 * JD-Core Version:    0.6.2
 */