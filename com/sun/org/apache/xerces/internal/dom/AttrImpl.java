/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.Text;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ 
/*      */ public class AttrImpl extends NodeImpl
/*      */   implements Attr, TypeInfo
/*      */ {
/*      */   static final long serialVersionUID = 7277707688218972102L;
/*      */   static final String DTD_URI = "http://www.w3.org/TR/REC-xml";
/*  135 */   protected Object value = null;
/*      */   protected String name;
/*      */   transient Object type;
/*  144 */   protected TextImpl textNode = null;
/*      */ 
/*      */   protected AttrImpl(CoreDocumentImpl ownerDocument, String name)
/*      */   {
/*  155 */     super(ownerDocument);
/*  156 */     this.name = name;
/*      */ 
/*  158 */     isSpecified(true);
/*  159 */     hasStringValue(true);
/*      */   }
/*      */ 
/*      */   protected AttrImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   void rename(String name)
/*      */   {
/*  170 */     if (needsSyncData()) {
/*  171 */       synchronizeData();
/*      */     }
/*  173 */     this.name = name;
/*      */   }
/*      */ 
/*      */   protected void makeChildNode()
/*      */   {
/*  178 */     if (hasStringValue()) {
/*  179 */       if (this.value != null) {
/*  180 */         TextImpl text = (TextImpl)ownerDocument().createTextNode((String)this.value);
/*      */ 
/*  182 */         this.value = text;
/*  183 */         text.isFirstChild(true);
/*  184 */         text.previousSibling = text;
/*  185 */         text.ownerNode = this;
/*  186 */         text.isOwned(true);
/*      */       }
/*  188 */       hasStringValue(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setOwnerDocument(CoreDocumentImpl doc)
/*      */   {
/*  197 */     if (needsSyncChildren()) {
/*  198 */       synchronizeChildren();
/*      */     }
/*  200 */     super.setOwnerDocument(doc);
/*  201 */     if (!hasStringValue())
/*  202 */       for (ChildNode child = (ChildNode)this.value; 
/*  203 */         child != null; child = child.nextSibling)
/*  204 */         child.setOwnerDocument(doc);
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(boolean id)
/*      */   {
/*  215 */     if (needsSyncData()) {
/*  216 */       synchronizeData();
/*      */     }
/*  218 */     isIdAttribute(id);
/*      */   }
/*      */ 
/*      */   public boolean isId()
/*      */   {
/*  224 */     return isIdAttribute();
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  234 */     if (needsSyncChildren()) {
/*  235 */       synchronizeChildren();
/*      */     }
/*  237 */     AttrImpl clone = (AttrImpl)super.cloneNode(deep);
/*      */ 
/*  240 */     if (!clone.hasStringValue())
/*      */     {
/*  243 */       clone.value = null;
/*      */ 
/*  248 */       for (Node child = (Node)this.value; child != null; 
/*  249 */         child = child.getNextSibling()) {
/*  250 */         clone.appendChild(child.cloneNode(true));
/*      */       }
/*      */     }
/*  253 */     clone.isSpecified(true);
/*  254 */     return clone;
/*      */   }
/*      */ 
/*      */   public short getNodeType()
/*      */   {
/*  262 */     return 2;
/*      */   }
/*      */ 
/*      */   public String getNodeName()
/*      */   {
/*  269 */     if (needsSyncData()) {
/*  270 */       synchronizeData();
/*      */     }
/*  272 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void setNodeValue(String value)
/*      */     throws DOMException
/*      */   {
/*  282 */     setValue(value);
/*      */   }
/*      */ 
/*      */   public String getTypeName()
/*      */   {
/*  289 */     return (String)this.type;
/*      */   }
/*      */ 
/*      */   public String getTypeNamespace()
/*      */   {
/*  296 */     if (this.type != null) {
/*  297 */       return "http://www.w3.org/TR/REC-xml";
/*      */     }
/*  299 */     return null;
/*      */   }
/*      */ 
/*      */   public TypeInfo getSchemaTypeInfo()
/*      */   {
/*  307 */     return this;
/*      */   }
/*      */ 
/*      */   public String getNodeValue()
/*      */   {
/*  317 */     return getValue();
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  330 */     if (needsSyncData()) {
/*  331 */       synchronizeData();
/*      */     }
/*  333 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void setValue(String newvalue)
/*      */   {
/*  344 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*      */ 
/*  346 */     if ((ownerDocument.errorChecking) && (isReadOnly())) {
/*  347 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  348 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  351 */     Element ownerElement = getOwnerElement();
/*  352 */     String oldvalue = "";
/*  353 */     if (needsSyncData()) {
/*  354 */       synchronizeData();
/*      */     }
/*  356 */     if (needsSyncChildren()) {
/*  357 */       synchronizeChildren();
/*      */     }
/*  359 */     if (this.value != null) {
/*  360 */       if (ownerDocument.getMutationEvents())
/*      */       {
/*  363 */         if (hasStringValue()) {
/*  364 */           oldvalue = (String)this.value;
/*      */ 
/*  367 */           if (this.textNode == null) {
/*  368 */             this.textNode = ((TextImpl)ownerDocument.createTextNode((String)this.value));
/*      */           }
/*      */           else
/*      */           {
/*  372 */             this.textNode.data = ((String)this.value);
/*      */           }
/*  374 */           this.value = this.textNode;
/*  375 */           this.textNode.isFirstChild(true);
/*  376 */           this.textNode.previousSibling = this.textNode;
/*  377 */           this.textNode.ownerNode = this;
/*  378 */           this.textNode.isOwned(true);
/*  379 */           hasStringValue(false);
/*  380 */           internalRemoveChild(this.textNode, true);
/*      */         }
/*      */         else {
/*  383 */           oldvalue = getValue();
/*  384 */           while (this.value != null)
/*  385 */             internalRemoveChild((Node)this.value, true);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  390 */         if (hasStringValue()) {
/*  391 */           oldvalue = (String)this.value;
/*      */         }
/*      */         else
/*      */         {
/*  395 */           oldvalue = getValue();
/*      */ 
/*  397 */           ChildNode firstChild = (ChildNode)this.value;
/*  398 */           firstChild.previousSibling = null;
/*  399 */           firstChild.isFirstChild(false);
/*  400 */           firstChild.ownerNode = ownerDocument;
/*      */         }
/*      */ 
/*  403 */         this.value = null;
/*  404 */         needsSyncChildren(false);
/*      */       }
/*  406 */       if ((isIdAttribute()) && (ownerElement != null)) {
/*  407 */         ownerDocument.removeIdentifier(oldvalue);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  416 */     isSpecified(true);
/*  417 */     if (ownerDocument.getMutationEvents())
/*      */     {
/*  419 */       internalInsertBefore(ownerDocument.createTextNode(newvalue), null, true);
/*      */ 
/*  421 */       hasStringValue(false);
/*      */ 
/*  423 */       ownerDocument.modifiedAttrValue(this, oldvalue);
/*      */     }
/*      */     else {
/*  426 */       this.value = newvalue;
/*  427 */       hasStringValue(true);
/*  428 */       changed();
/*      */     }
/*  430 */     if ((isIdAttribute()) && (ownerElement != null))
/*  431 */       ownerDocument.putIdentifier(newvalue, ownerElement);
/*      */   }
/*      */ 
/*      */   public String getValue()
/*      */   {
/*  442 */     if (needsSyncData()) {
/*  443 */       synchronizeData();
/*      */     }
/*  445 */     if (needsSyncChildren()) {
/*  446 */       synchronizeChildren();
/*      */     }
/*  448 */     if (this.value == null) {
/*  449 */       return "";
/*      */     }
/*  451 */     if (hasStringValue()) {
/*  452 */       return (String)this.value;
/*      */     }
/*      */ 
/*  455 */     ChildNode firstChild = (ChildNode)this.value;
/*      */ 
/*  457 */     String data = null;
/*  458 */     if (firstChild.getNodeType() == 5) {
/*  459 */       data = ((EntityReferenceImpl)firstChild).getEntityRefValue();
/*      */     }
/*      */     else {
/*  462 */       data = firstChild.getNodeValue();
/*      */     }
/*      */ 
/*  465 */     ChildNode node = firstChild.nextSibling;
/*      */ 
/*  467 */     if ((node == null) || (data == null)) return data == null ? "" : data;
/*      */ 
/*  469 */     StringBuffer value = new StringBuffer(data);
/*  470 */     while (node != null) {
/*  471 */       if (node.getNodeType() == 5) {
/*  472 */         data = ((EntityReferenceImpl)node).getEntityRefValue();
/*  473 */         if (data == null) return "";
/*  474 */         value.append(data);
/*      */       }
/*      */       else {
/*  477 */         value.append(node.getNodeValue());
/*      */       }
/*  479 */       node = node.nextSibling;
/*      */     }
/*  481 */     return value.toString();
/*      */   }
/*      */ 
/*      */   public boolean getSpecified()
/*      */   {
/*  499 */     if (needsSyncData()) {
/*  500 */       synchronizeData();
/*      */     }
/*  502 */     return isSpecified();
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Element getElement()
/*      */   {
/*  522 */     return (Element)(isOwned() ? this.ownerNode : null);
/*      */   }
/*      */ 
/*      */   public Element getOwnerElement()
/*      */   {
/*  534 */     return (Element)(isOwned() ? this.ownerNode : null);
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*  541 */     if ((isNormalized()) || (hasStringValue())) {
/*  542 */       return;
/*      */     }
/*      */ 
/*  545 */     ChildNode firstChild = (ChildNode)this.value;
/*      */     Node next;
/*  546 */     for (Node kid = firstChild; kid != null; kid = next) {
/*  547 */       next = kid.getNextSibling();
/*      */ 
/*  554 */       if (kid.getNodeType() == 3)
/*      */       {
/*  557 */         if ((next != null) && (next.getNodeType() == 3))
/*      */         {
/*  559 */           ((Text)kid).appendData(next.getNodeValue());
/*  560 */           removeChild(next);
/*  561 */           next = kid;
/*      */         }
/*  566 */         else if ((kid.getNodeValue() == null) || (kid.getNodeValue().length() == 0)) {
/*  567 */           removeChild(kid);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  573 */     isNormalized(true);
/*      */   }
/*      */ 
/*      */   public void setSpecified(boolean arg)
/*      */   {
/*  583 */     if (needsSyncData()) {
/*  584 */       synchronizeData();
/*      */     }
/*  586 */     isSpecified(arg);
/*      */   }
/*      */ 
/*      */   public void setType(Object type)
/*      */   {
/*  595 */     this.type = type;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  604 */     return getName() + "=" + "\"" + getValue() + "\"";
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes()
/*      */   {
/*  612 */     if (needsSyncChildren()) {
/*  613 */       synchronizeChildren();
/*      */     }
/*  615 */     return this.value != null;
/*      */   }
/*      */ 
/*      */   public NodeList getChildNodes()
/*      */   {
/*  634 */     if (needsSyncChildren()) {
/*  635 */       synchronizeChildren();
/*      */     }
/*  637 */     return this;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild()
/*      */   {
/*  644 */     if (needsSyncChildren()) {
/*  645 */       synchronizeChildren();
/*      */     }
/*  647 */     makeChildNode();
/*  648 */     return (Node)this.value;
/*      */   }
/*      */ 
/*      */   public Node getLastChild()
/*      */   {
/*  655 */     if (needsSyncChildren()) {
/*  656 */       synchronizeChildren();
/*      */     }
/*  658 */     return lastChild();
/*      */   }
/*      */ 
/*      */   final ChildNode lastChild()
/*      */   {
/*  664 */     makeChildNode();
/*  665 */     return this.value != null ? ((ChildNode)this.value).previousSibling : null;
/*      */   }
/*      */ 
/*      */   final void lastChild(ChildNode node)
/*      */   {
/*  670 */     if (this.value != null)
/*  671 */       ((ChildNode)this.value).previousSibling = node;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  706 */     return internalInsertBefore(newChild, refChild, false);
/*      */   }
/*      */ 
/*      */   Node internalInsertBefore(Node newChild, Node refChild, boolean replace)
/*      */     throws DOMException
/*      */   {
/*  717 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*  718 */     boolean errorChecking = ownerDocument.errorChecking;
/*      */ 
/*  720 */     if (newChild.getNodeType() == 11)
/*      */     {
/*  738 */       if (errorChecking) {
/*  739 */         for (Node kid = newChild.getFirstChild(); 
/*  740 */           kid != null; kid = kid.getNextSibling())
/*      */         {
/*  742 */           if (!ownerDocument.isKidOK(this, kid)) {
/*  743 */             String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/*  744 */             throw new DOMException((short)3, msg);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  749 */       while (newChild.hasChildNodes()) {
/*  750 */         insertBefore(newChild.getFirstChild(), refChild);
/*      */       }
/*  752 */       return newChild;
/*      */     }
/*      */ 
/*  755 */     if (newChild == refChild)
/*      */     {
/*  757 */       refChild = refChild.getNextSibling();
/*  758 */       removeChild(newChild);
/*  759 */       insertBefore(newChild, refChild);
/*  760 */       return newChild;
/*      */     }
/*      */ 
/*  763 */     if (needsSyncChildren()) {
/*  764 */       synchronizeChildren();
/*      */     }
/*      */ 
/*  767 */     if (errorChecking) {
/*  768 */       if (isReadOnly()) {
/*  769 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  770 */         throw new DOMException((short)7, msg);
/*      */       }
/*  772 */       if (newChild.getOwnerDocument() != ownerDocument) {
/*  773 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*  774 */         throw new DOMException((short)4, msg);
/*      */       }
/*  776 */       if (!ownerDocument.isKidOK(this, newChild)) {
/*  777 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/*  778 */         throw new DOMException((short)3, msg);
/*      */       }
/*      */ 
/*  781 */       if ((refChild != null) && (refChild.getParentNode() != this)) {
/*  782 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  783 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */ 
/*  789 */       boolean treeSafe = true;
/*  790 */       for (NodeImpl a = this; (treeSafe) && (a != null); a = a.parentNode())
/*      */       {
/*  792 */         treeSafe = newChild != a;
/*      */       }
/*  794 */       if (!treeSafe) {
/*  795 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
/*  796 */         throw new DOMException((short)3, msg);
/*      */       }
/*      */     }
/*      */ 
/*  800 */     makeChildNode();
/*      */ 
/*  803 */     ownerDocument.insertingNode(this, replace);
/*      */ 
/*  806 */     ChildNode newInternal = (ChildNode)newChild;
/*      */ 
/*  808 */     Node oldparent = newInternal.parentNode();
/*  809 */     if (oldparent != null) {
/*  810 */       oldparent.removeChild(newInternal);
/*      */     }
/*      */ 
/*  814 */     ChildNode refInternal = (ChildNode)refChild;
/*      */ 
/*  817 */     newInternal.ownerNode = this;
/*  818 */     newInternal.isOwned(true);
/*      */ 
/*  822 */     ChildNode firstChild = (ChildNode)this.value;
/*  823 */     if (firstChild == null)
/*      */     {
/*  825 */       this.value = newInternal;
/*  826 */       newInternal.isFirstChild(true);
/*  827 */       newInternal.previousSibling = newInternal;
/*      */     }
/*  830 */     else if (refInternal == null)
/*      */     {
/*  832 */       ChildNode lastChild = firstChild.previousSibling;
/*  833 */       lastChild.nextSibling = newInternal;
/*  834 */       newInternal.previousSibling = lastChild;
/*  835 */       firstChild.previousSibling = newInternal;
/*      */     }
/*  839 */     else if (refChild == firstChild)
/*      */     {
/*  841 */       firstChild.isFirstChild(false);
/*  842 */       newInternal.nextSibling = firstChild;
/*  843 */       newInternal.previousSibling = firstChild.previousSibling;
/*  844 */       firstChild.previousSibling = newInternal;
/*  845 */       this.value = newInternal;
/*  846 */       newInternal.isFirstChild(true);
/*      */     }
/*      */     else
/*      */     {
/*  850 */       ChildNode prev = refInternal.previousSibling;
/*  851 */       newInternal.nextSibling = refInternal;
/*  852 */       prev.nextSibling = newInternal;
/*  853 */       refInternal.previousSibling = newInternal;
/*  854 */       newInternal.previousSibling = prev;
/*      */     }
/*      */ 
/*  859 */     changed();
/*      */ 
/*  862 */     ownerDocument.insertedNode(this, newInternal, replace);
/*      */ 
/*  864 */     checkNormalizationAfterInsert(newInternal);
/*      */ 
/*  866 */     return newChild;
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  885 */     if (hasStringValue())
/*      */     {
/*  887 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  888 */       throw new DOMException((short)8, msg);
/*      */     }
/*  890 */     return internalRemoveChild(oldChild, false);
/*      */   }
/*      */ 
/*      */   Node internalRemoveChild(Node oldChild, boolean replace)
/*      */     throws DOMException
/*      */   {
/*  901 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*  902 */     if (ownerDocument.errorChecking) {
/*  903 */       if (isReadOnly()) {
/*  904 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  905 */         throw new DOMException((short)7, msg);
/*      */       }
/*  907 */       if ((oldChild != null) && (oldChild.getParentNode() != this)) {
/*  908 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  909 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */     }
/*      */ 
/*  913 */     ChildNode oldInternal = (ChildNode)oldChild;
/*      */ 
/*  916 */     ownerDocument.removingNode(this, oldInternal, replace);
/*      */ 
/*  920 */     if (oldInternal == this.value)
/*      */     {
/*  922 */       oldInternal.isFirstChild(false);
/*      */ 
/*  924 */       this.value = oldInternal.nextSibling;
/*  925 */       ChildNode firstChild = (ChildNode)this.value;
/*  926 */       if (firstChild != null) {
/*  927 */         firstChild.isFirstChild(true);
/*  928 */         firstChild.previousSibling = oldInternal.previousSibling;
/*      */       }
/*      */     } else {
/*  931 */       ChildNode prev = oldInternal.previousSibling;
/*  932 */       ChildNode next = oldInternal.nextSibling;
/*  933 */       prev.nextSibling = next;
/*  934 */       if (next == null)
/*      */       {
/*  936 */         ChildNode firstChild = (ChildNode)this.value;
/*  937 */         firstChild.previousSibling = prev;
/*      */       }
/*      */       else {
/*  940 */         next.previousSibling = prev;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  945 */     ChildNode oldPreviousSibling = oldInternal.previousSibling();
/*      */ 
/*  948 */     oldInternal.ownerNode = ownerDocument;
/*  949 */     oldInternal.isOwned(false);
/*  950 */     oldInternal.nextSibling = null;
/*  951 */     oldInternal.previousSibling = null;
/*      */ 
/*  953 */     changed();
/*      */ 
/*  956 */     ownerDocument.removedNode(this, replace);
/*      */ 
/*  958 */     checkNormalizationAfterRemove(oldPreviousSibling);
/*      */ 
/*  960 */     return oldInternal;
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  988 */     makeChildNode();
/*      */ 
/*  997 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*  998 */     ownerDocument.replacingNode(this);
/*      */ 
/* 1000 */     internalInsertBefore(newChild, oldChild, true);
/* 1001 */     if (newChild != oldChild) {
/* 1002 */       internalRemoveChild(oldChild, true);
/*      */     }
/*      */ 
/* 1006 */     ownerDocument.replacedNode(this);
/*      */ 
/* 1008 */     return oldChild;
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/* 1021 */     if (hasStringValue()) {
/* 1022 */       return 1;
/*      */     }
/* 1024 */     ChildNode node = (ChildNode)this.value;
/* 1025 */     int length = 0;
/* 1026 */     for (; node != null; node = node.nextSibling) {
/* 1027 */       length++;
/*      */     }
/* 1029 */     return length;
/*      */   }
/*      */ 
/*      */   public Node item(int index)
/*      */   {
/* 1041 */     if (hasStringValue()) {
/* 1042 */       if ((index != 0) || (this.value == null)) {
/* 1043 */         return null;
/*      */       }
/*      */ 
/* 1046 */       makeChildNode();
/* 1047 */       return (Node)this.value;
/*      */     }
/*      */ 
/* 1050 */     if (index < 0) {
/* 1051 */       return null;
/*      */     }
/* 1053 */     ChildNode node = (ChildNode)this.value;
/* 1054 */     for (int i = 0; (i < index) && (node != null); i++) {
/* 1055 */       node = node.nextSibling;
/*      */     }
/* 1057 */     return node;
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/* 1071 */     return super.isEqualNode(arg);
/*      */   }
/*      */ 
/*      */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod)
/*      */   {
/* 1093 */     return false;
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean readOnly, boolean deep)
/*      */   {
/* 1111 */     super.setReadOnly(readOnly, deep);
/*      */ 
/* 1113 */     if (deep)
/*      */     {
/* 1115 */       if (needsSyncChildren()) {
/* 1116 */         synchronizeChildren();
/*      */       }
/*      */ 
/* 1119 */       if (hasStringValue()) {
/* 1120 */         return;
/*      */       }
/*      */ 
/* 1123 */       for (ChildNode mykid = (ChildNode)this.value; 
/* 1124 */         mykid != null; 
/* 1125 */         mykid = mykid.nextSibling)
/* 1126 */         if (mykid.getNodeType() != 5)
/* 1127 */           mykid.setReadOnly(readOnly, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void synchronizeChildren()
/*      */   {
/* 1143 */     needsSyncChildren(false);
/*      */   }
/*      */ 
/*      */   void checkNormalizationAfterInsert(ChildNode insertedChild)
/*      */   {
/* 1163 */     if (insertedChild.getNodeType() == 3) {
/* 1164 */       ChildNode prev = insertedChild.previousSibling();
/* 1165 */       ChildNode next = insertedChild.nextSibling;
/*      */ 
/* 1168 */       if (((prev != null) && (prev.getNodeType() == 3)) || ((next != null) && (next.getNodeType() == 3)))
/*      */       {
/* 1170 */         isNormalized(false);
/*      */       }
/*      */ 
/*      */     }
/* 1176 */     else if (!insertedChild.isNormalized()) {
/* 1177 */       isNormalized(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   void checkNormalizationAfterRemove(ChildNode previousSibling)
/*      */   {
/* 1198 */     if ((previousSibling != null) && (previousSibling.getNodeType() == 3))
/*      */     {
/* 1201 */       ChildNode next = previousSibling.nextSibling;
/* 1202 */       if ((next != null) && (next.getNodeType() == 3))
/* 1203 */         isNormalized(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream out)
/*      */     throws IOException
/*      */   {
/* 1216 */     if (needsSyncChildren()) {
/* 1217 */       synchronizeChildren();
/*      */     }
/*      */ 
/* 1220 */     out.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream ois)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1229 */     ois.defaultReadObject();
/*      */ 
/* 1234 */     needsSyncChildren(false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.AttrImpl
 * JD-Core Version:    0.6.2
 */