/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.UserDataHandler;
/*      */ 
/*      */ public abstract class ParentNode extends ChildNode
/*      */ {
/*      */   static final long serialVersionUID = 2815829867152120872L;
/*      */   protected CoreDocumentImpl ownerDocument;
/*   83 */   protected ChildNode firstChild = null;
/*      */ 
/*   88 */   protected transient NodeListCache fNodeListCache = null;
/*      */ 
/*      */   protected ParentNode(CoreDocumentImpl ownerDocument)
/*      */   {
/*   99 */     super(ownerDocument);
/*  100 */     this.ownerDocument = ownerDocument;
/*      */   }
/*      */ 
/*      */   public ParentNode()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  130 */     if (needsSyncChildren()) {
/*  131 */       synchronizeChildren();
/*      */     }
/*  133 */     ParentNode newnode = (ParentNode)super.cloneNode(deep);
/*      */ 
/*  136 */     newnode.ownerDocument = this.ownerDocument;
/*      */ 
/*  139 */     newnode.firstChild = null;
/*      */ 
/*  142 */     newnode.fNodeListCache = null;
/*      */ 
/*  145 */     if (deep) {
/*  146 */       for (ChildNode child = this.firstChild; 
/*  147 */         child != null; 
/*  148 */         child = child.nextSibling) {
/*  149 */         newnode.appendChild(child.cloneNode(true));
/*      */       }
/*      */     }
/*      */ 
/*  153 */     return newnode;
/*      */   }
/*      */ 
/*      */   public Document getOwnerDocument()
/*      */   {
/*  163 */     return this.ownerDocument;
/*      */   }
/*      */ 
/*      */   CoreDocumentImpl ownerDocument()
/*      */   {
/*  171 */     return this.ownerDocument;
/*      */   }
/*      */ 
/*      */   void setOwnerDocument(CoreDocumentImpl doc)
/*      */   {
/*  179 */     if (needsSyncChildren()) {
/*  180 */       synchronizeChildren();
/*      */     }
/*  182 */     for (ChildNode child = this.firstChild; 
/*  183 */       child != null; child = child.nextSibling) {
/*  184 */       child.setOwnerDocument(doc);
/*      */     }
/*      */ 
/*  188 */     super.setOwnerDocument(doc);
/*  189 */     this.ownerDocument = doc;
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes()
/*      */   {
/*  197 */     if (needsSyncChildren()) {
/*  198 */       synchronizeChildren();
/*      */     }
/*  200 */     return this.firstChild != null;
/*      */   }
/*      */ 
/*      */   public NodeList getChildNodes()
/*      */   {
/*  218 */     if (needsSyncChildren()) {
/*  219 */       synchronizeChildren();
/*      */     }
/*  221 */     return this;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild()
/*      */   {
/*  228 */     if (needsSyncChildren()) {
/*  229 */       synchronizeChildren();
/*      */     }
/*  231 */     return this.firstChild;
/*      */   }
/*      */ 
/*      */   public Node getLastChild()
/*      */   {
/*  238 */     if (needsSyncChildren()) {
/*  239 */       synchronizeChildren();
/*      */     }
/*  241 */     return lastChild();
/*      */   }
/*      */ 
/*      */   final ChildNode lastChild()
/*      */   {
/*  247 */     return this.firstChild != null ? this.firstChild.previousSibling : null;
/*      */   }
/*      */ 
/*      */   final void lastChild(ChildNode node)
/*      */   {
/*  252 */     if (this.firstChild != null)
/*  253 */       this.firstChild.previousSibling = node;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  288 */     return internalInsertBefore(newChild, refChild, false);
/*      */   }
/*      */ 
/*      */   Node internalInsertBefore(Node newChild, Node refChild, boolean replace)
/*      */     throws DOMException
/*      */   {
/*  299 */     boolean errorChecking = this.ownerDocument.errorChecking;
/*      */ 
/*  301 */     if (newChild.getNodeType() == 11)
/*      */     {
/*  319 */       if (errorChecking) {
/*  320 */         for (Node kid = newChild.getFirstChild(); 
/*  321 */           kid != null; kid = kid.getNextSibling())
/*      */         {
/*  323 */           if (!this.ownerDocument.isKidOK(this, kid)) {
/*  324 */             throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  331 */       while (newChild.hasChildNodes()) {
/*  332 */         insertBefore(newChild.getFirstChild(), refChild);
/*      */       }
/*  334 */       return newChild;
/*      */     }
/*      */ 
/*  337 */     if (newChild == refChild)
/*      */     {
/*  339 */       refChild = refChild.getNextSibling();
/*  340 */       removeChild(newChild);
/*  341 */       insertBefore(newChild, refChild);
/*  342 */       return newChild;
/*      */     }
/*      */ 
/*  345 */     if (needsSyncChildren()) {
/*  346 */       synchronizeChildren();
/*      */     }
/*      */ 
/*  349 */     if (errorChecking) {
/*  350 */       if (isReadOnly()) {
/*  351 */         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*      */       }
/*      */ 
/*  355 */       if ((newChild.getOwnerDocument() != this.ownerDocument) && (newChild != this.ownerDocument)) {
/*  356 */         throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
/*      */       }
/*      */ 
/*  359 */       if (!this.ownerDocument.isKidOK(this, newChild)) {
/*  360 */         throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */       }
/*      */ 
/*  364 */       if ((refChild != null) && (refChild.getParentNode() != this)) {
/*  365 */         throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
/*      */       }
/*      */ 
/*  372 */       if (this.ownerDocument.ancestorChecking) {
/*  373 */         boolean treeSafe = true;
/*  374 */         for (NodeImpl a = this; (treeSafe) && (a != null); a = a.parentNode())
/*      */         {
/*  376 */           treeSafe = newChild != a;
/*      */         }
/*  378 */         if (!treeSafe) {
/*  379 */           throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  386 */     this.ownerDocument.insertingNode(this, replace);
/*      */ 
/*  389 */     ChildNode newInternal = (ChildNode)newChild;
/*      */ 
/*  391 */     Node oldparent = newInternal.parentNode();
/*  392 */     if (oldparent != null) {
/*  393 */       oldparent.removeChild(newInternal);
/*      */     }
/*      */ 
/*  397 */     ChildNode refInternal = (ChildNode)refChild;
/*      */ 
/*  400 */     newInternal.ownerNode = this;
/*  401 */     newInternal.isOwned(true);
/*      */ 
/*  405 */     if (this.firstChild == null)
/*      */     {
/*  407 */       this.firstChild = newInternal;
/*  408 */       newInternal.isFirstChild(true);
/*  409 */       newInternal.previousSibling = newInternal;
/*      */     }
/*  412 */     else if (refInternal == null)
/*      */     {
/*  414 */       ChildNode lastChild = this.firstChild.previousSibling;
/*  415 */       lastChild.nextSibling = newInternal;
/*  416 */       newInternal.previousSibling = lastChild;
/*  417 */       this.firstChild.previousSibling = newInternal;
/*      */     }
/*  421 */     else if (refChild == this.firstChild)
/*      */     {
/*  423 */       this.firstChild.isFirstChild(false);
/*  424 */       newInternal.nextSibling = this.firstChild;
/*  425 */       newInternal.previousSibling = this.firstChild.previousSibling;
/*  426 */       this.firstChild.previousSibling = newInternal;
/*  427 */       this.firstChild = newInternal;
/*  428 */       newInternal.isFirstChild(true);
/*      */     }
/*      */     else
/*      */     {
/*  432 */       ChildNode prev = refInternal.previousSibling;
/*  433 */       newInternal.nextSibling = refInternal;
/*  434 */       prev.nextSibling = newInternal;
/*  435 */       refInternal.previousSibling = newInternal;
/*  436 */       newInternal.previousSibling = prev;
/*      */     }
/*      */ 
/*  441 */     changed();
/*      */ 
/*  444 */     if (this.fNodeListCache != null) {
/*  445 */       if (this.fNodeListCache.fLength != -1) {
/*  446 */         this.fNodeListCache.fLength += 1;
/*      */       }
/*  448 */       if (this.fNodeListCache.fChildIndex != -1)
/*      */       {
/*  451 */         if (this.fNodeListCache.fChild == refInternal) {
/*  452 */           this.fNodeListCache.fChild = newInternal;
/*      */         }
/*      */         else {
/*  455 */           this.fNodeListCache.fChildIndex = -1;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  461 */     this.ownerDocument.insertedNode(this, newInternal, replace);
/*      */ 
/*  463 */     checkNormalizationAfterInsert(newInternal);
/*      */ 
/*  465 */     return newChild;
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  484 */     return internalRemoveChild(oldChild, false);
/*      */   }
/*      */ 
/*      */   Node internalRemoveChild(Node oldChild, boolean replace)
/*      */     throws DOMException
/*      */   {
/*  495 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*  496 */     if (ownerDocument.errorChecking) {
/*  497 */       if (isReadOnly()) {
/*  498 */         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
/*      */       }
/*      */ 
/*  502 */       if ((oldChild != null) && (oldChild.getParentNode() != this)) {
/*  503 */         throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  508 */     ChildNode oldInternal = (ChildNode)oldChild;
/*      */ 
/*  511 */     ownerDocument.removingNode(this, oldInternal, replace);
/*      */ 
/*  514 */     if (this.fNodeListCache != null) {
/*  515 */       if (this.fNodeListCache.fLength != -1) {
/*  516 */         this.fNodeListCache.fLength -= 1;
/*      */       }
/*  518 */       if (this.fNodeListCache.fChildIndex != -1)
/*      */       {
/*  521 */         if (this.fNodeListCache.fChild == oldInternal) {
/*  522 */           this.fNodeListCache.fChildIndex -= 1;
/*  523 */           this.fNodeListCache.fChild = oldInternal.previousSibling();
/*      */         }
/*      */         else {
/*  526 */           this.fNodeListCache.fChildIndex = -1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  533 */     if (oldInternal == this.firstChild)
/*      */     {
/*  535 */       oldInternal.isFirstChild(false);
/*  536 */       this.firstChild = oldInternal.nextSibling;
/*  537 */       if (this.firstChild != null) {
/*  538 */         this.firstChild.isFirstChild(true);
/*  539 */         this.firstChild.previousSibling = oldInternal.previousSibling;
/*      */       }
/*      */     } else {
/*  542 */       ChildNode prev = oldInternal.previousSibling;
/*  543 */       ChildNode next = oldInternal.nextSibling;
/*  544 */       prev.nextSibling = next;
/*  545 */       if (next == null)
/*      */       {
/*  547 */         this.firstChild.previousSibling = prev;
/*      */       }
/*      */       else {
/*  550 */         next.previousSibling = prev;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  555 */     ChildNode oldPreviousSibling = oldInternal.previousSibling();
/*      */ 
/*  558 */     oldInternal.ownerNode = ownerDocument;
/*  559 */     oldInternal.isOwned(false);
/*  560 */     oldInternal.nextSibling = null;
/*  561 */     oldInternal.previousSibling = null;
/*      */ 
/*  563 */     changed();
/*      */ 
/*  566 */     ownerDocument.removedNode(this, replace);
/*      */ 
/*  568 */     checkNormalizationAfterRemove(oldPreviousSibling);
/*      */ 
/*  570 */     return oldInternal;
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  604 */     this.ownerDocument.replacingNode(this);
/*      */ 
/*  606 */     internalInsertBefore(newChild, oldChild, true);
/*  607 */     if (newChild != oldChild) {
/*  608 */       internalRemoveChild(oldChild, true);
/*      */     }
/*      */ 
/*  612 */     this.ownerDocument.replacedNode(this);
/*      */ 
/*  614 */     return oldChild;
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/*  622 */     Node child = getFirstChild();
/*  623 */     if (child != null) {
/*  624 */       Node next = child.getNextSibling();
/*  625 */       if (next == null) {
/*  626 */         return hasTextContent(child) ? ((NodeImpl)child).getTextContent() : "";
/*      */       }
/*  628 */       if (this.fBufferStr == null) {
/*  629 */         this.fBufferStr = new StringBuffer();
/*      */       }
/*      */       else {
/*  632 */         this.fBufferStr.setLength(0);
/*      */       }
/*  634 */       getTextContent(this.fBufferStr);
/*  635 */       return this.fBufferStr.toString();
/*      */     }
/*  637 */     return "";
/*      */   }
/*      */ 
/*      */   void getTextContent(StringBuffer buf) throws DOMException
/*      */   {
/*  642 */     Node child = getFirstChild();
/*  643 */     while (child != null) {
/*  644 */       if (hasTextContent(child)) {
/*  645 */         ((NodeImpl)child).getTextContent(buf);
/*      */       }
/*  647 */       child = child.getNextSibling();
/*      */     }
/*      */   }
/*      */ 
/*      */   final boolean hasTextContent(Node child)
/*      */   {
/*  653 */     return (child.getNodeType() != 8) && (child.getNodeType() != 7) && ((child.getNodeType() != 3) || (!((TextImpl)child).isIgnorableWhitespace()));
/*      */   }
/*      */ 
/*      */   public void setTextContent(String textContent)
/*      */     throws DOMException
/*      */   {
/*      */     Node child;
/*  667 */     while ((child = getFirstChild()) != null) {
/*  668 */       removeChild(child);
/*      */     }
/*      */ 
/*  671 */     if ((textContent != null) && (textContent.length() != 0))
/*  672 */       appendChild(ownerDocument().createTextNode(textContent));
/*      */   }
/*      */ 
/*      */   private int nodeListGetLength()
/*      */   {
/*  687 */     if (this.fNodeListCache == null)
/*      */     {
/*  689 */       if (this.firstChild == null) {
/*  690 */         return 0;
/*      */       }
/*  692 */       if (this.firstChild == lastChild()) {
/*  693 */         return 1;
/*      */       }
/*      */ 
/*  696 */       this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
/*      */     }
/*  698 */     if (this.fNodeListCache.fLength == -1)
/*      */     {
/*      */       ChildNode n;
/*      */       ChildNode n;
/*      */       int l;
/*  702 */       if ((this.fNodeListCache.fChildIndex != -1) && (this.fNodeListCache.fChild != null))
/*      */       {
/*  704 */         int l = this.fNodeListCache.fChildIndex;
/*  705 */         n = this.fNodeListCache.fChild;
/*      */       } else {
/*  707 */         n = this.firstChild;
/*  708 */         l = 0;
/*      */       }
/*  710 */       while (n != null) {
/*  711 */         l++;
/*  712 */         n = n.nextSibling;
/*      */       }
/*  714 */       this.fNodeListCache.fLength = l;
/*      */     }
/*      */ 
/*  717 */     return this.fNodeListCache.fLength;
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  726 */     return nodeListGetLength();
/*      */   }
/*      */ 
/*      */   private Node nodeListItem(int index)
/*      */   {
/*  736 */     if (this.fNodeListCache == null)
/*      */     {
/*  738 */       if (this.firstChild == lastChild()) {
/*  739 */         return index == 0 ? this.firstChild : null;
/*      */       }
/*      */ 
/*  742 */       this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
/*      */     }
/*  744 */     int i = this.fNodeListCache.fChildIndex;
/*  745 */     ChildNode n = this.fNodeListCache.fChild;
/*  746 */     boolean firstAccess = true;
/*      */ 
/*  748 */     if ((i != -1) && (n != null)) {
/*  749 */       firstAccess = false;
/*  750 */       if (i < index) {
/*  751 */         while ((i < index) && (n != null)) {
/*  752 */           i++;
/*  753 */           n = n.nextSibling;
/*      */         }
/*      */       }
/*  756 */       if (i <= index);
/*      */     } else { while ((i > index) && (n != null)) {
/*  758 */         i--;
/*  759 */         n = n.previousSibling(); continue;
/*      */ 
/*  765 */         if (index < 0) {
/*  766 */           return null;
/*      */         }
/*  768 */         n = this.firstChild;
/*  769 */         for (i = 0; (i < index) && (n != null); i++) {
/*  770 */           n = n.nextSibling;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  775 */     if ((!firstAccess) && ((n == this.firstChild) || (n == lastChild()))) {
/*  776 */       this.fNodeListCache.fChildIndex = -1;
/*  777 */       this.fNodeListCache.fChild = null;
/*  778 */       this.ownerDocument.freeNodeListCache(this.fNodeListCache);
/*      */     }
/*      */     else
/*      */     {
/*  786 */       this.fNodeListCache.fChildIndex = i;
/*  787 */       this.fNodeListCache.fChild = n;
/*      */     }
/*  789 */     return n;
/*      */   }
/*      */ 
/*      */   public Node item(int index)
/*      */   {
/*  800 */     return nodeListItem(index);
/*      */   }
/*      */ 
/*      */   protected final NodeList getChildNodesUnoptimized()
/*      */   {
/*  816 */     if (needsSyncChildren()) {
/*  817 */       synchronizeChildren();
/*      */     }
/*  819 */     return new NodeList()
/*      */     {
/*      */       public int getLength()
/*      */       {
/*  824 */         return ParentNode.this.nodeListGetLength();
/*      */       }
/*      */ 
/*      */       public Node item(int index)
/*      */       {
/*  831 */         return ParentNode.this.nodeListItem(index);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*  847 */     if (isNormalized()) {
/*  848 */       return;
/*      */     }
/*  850 */     if (needsSyncChildren()) {
/*  851 */       synchronizeChildren();
/*      */     }
/*      */ 
/*  854 */     for (ChildNode kid = this.firstChild; kid != null; kid = kid.nextSibling) {
/*  855 */       kid.normalize();
/*      */     }
/*  857 */     isNormalized(true);
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/*  865 */     if (!super.isEqualNode(arg)) {
/*  866 */       return false;
/*      */     }
/*      */ 
/*  871 */     Node child1 = getFirstChild();
/*  872 */     Node child2 = arg.getFirstChild();
/*  873 */     while ((child1 != null) && (child2 != null)) {
/*  874 */       if (!((NodeImpl)child1).isEqualNode(child2)) {
/*  875 */         return false;
/*      */       }
/*  877 */       child1 = child1.getNextSibling();
/*  878 */       child2 = child2.getNextSibling();
/*      */     }
/*  880 */     if (child1 != child2) {
/*  881 */       return false;
/*      */     }
/*  883 */     return true;
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean readOnly, boolean deep)
/*      */   {
/*  900 */     super.setReadOnly(readOnly, deep);
/*      */ 
/*  902 */     if (deep)
/*      */     {
/*  904 */       if (needsSyncChildren()) {
/*  905 */         synchronizeChildren();
/*      */       }
/*      */ 
/*  909 */       for (ChildNode mykid = this.firstChild; 
/*  910 */         mykid != null; 
/*  911 */         mykid = mykid.nextSibling)
/*  912 */         if (mykid.getNodeType() != 5)
/*  913 */           mykid.setReadOnly(readOnly, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void synchronizeChildren()
/*      */   {
/*  929 */     needsSyncChildren(false);
/*      */   }
/*      */ 
/*      */   void checkNormalizationAfterInsert(ChildNode insertedChild)
/*      */   {
/*  949 */     if (insertedChild.getNodeType() == 3) {
/*  950 */       ChildNode prev = insertedChild.previousSibling();
/*  951 */       ChildNode next = insertedChild.nextSibling;
/*      */ 
/*  954 */       if (((prev != null) && (prev.getNodeType() == 3)) || ((next != null) && (next.getNodeType() == 3)))
/*      */       {
/*  956 */         isNormalized(false);
/*      */       }
/*      */ 
/*      */     }
/*  962 */     else if (!insertedChild.isNormalized()) {
/*  963 */       isNormalized(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkNormalizationAfterRemove(ChildNode previousSibling)
/*      */   {
/*  984 */     if ((previousSibling != null) && (previousSibling.getNodeType() == 3))
/*      */     {
/*  987 */       ChildNode next = previousSibling.nextSibling;
/*  988 */       if ((next != null) && (next.getNodeType() == 3))
/*  989 */         isNormalized(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream out)
/*      */     throws IOException
/*      */   {
/* 1002 */     if (needsSyncChildren()) {
/* 1003 */       synchronizeChildren();
/*      */     }
/*      */ 
/* 1006 */     out.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream ois)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1015 */     ois.defaultReadObject();
/*      */ 
/* 1019 */     needsSyncChildren(false);
/*      */   }
/*      */ 
/*      */   class UserDataRecord
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3258126977134310455L;
/*      */     Object fData;
/*      */     UserDataHandler fHandler;
/*      */ 
/*      */     UserDataRecord(Object data, UserDataHandler handler)
/*      */     {
/* 1033 */       this.fData = data;
/* 1034 */       this.fHandler = handler;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.ParentNode
 * JD-Core Version:    0.6.2
 */