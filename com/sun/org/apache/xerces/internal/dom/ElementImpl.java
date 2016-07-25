/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.URI;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.Text;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ 
/*      */ public class ElementImpl extends ParentNode
/*      */   implements Element, TypeInfo
/*      */ {
/*      */   static final long serialVersionUID = 3717253516652722278L;
/*      */   protected String name;
/*      */   protected AttributeMap attributes;
/*      */ 
/*      */   public ElementImpl(CoreDocumentImpl ownerDoc, String name)
/*      */   {
/*   85 */     super(ownerDoc);
/*   86 */     this.name = name;
/*   87 */     needsSyncData(true);
/*      */   }
/*      */ 
/*      */   protected ElementImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   void rename(String name)
/*      */   {
/*   97 */     if (needsSyncData()) {
/*   98 */       synchronizeData();
/*      */     }
/*  100 */     this.name = name;
/*  101 */     reconcileDefaultAttributes();
/*      */   }
/*      */ 
/*      */   public short getNodeType()
/*      */   {
/*  114 */     return 1;
/*      */   }
/*      */ 
/*      */   public String getNodeName()
/*      */   {
/*  121 */     if (needsSyncData()) {
/*  122 */       synchronizeData();
/*      */     }
/*  124 */     return this.name;
/*      */   }
/*      */ 
/*      */   public NamedNodeMap getAttributes()
/*      */   {
/*  135 */     if (needsSyncData()) {
/*  136 */       synchronizeData();
/*      */     }
/*  138 */     if (this.attributes == null) {
/*  139 */       this.attributes = new AttributeMap(this, null);
/*      */     }
/*  141 */     return this.attributes;
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  154 */     ElementImpl newnode = (ElementImpl)super.cloneNode(deep);
/*      */ 
/*  156 */     if (this.attributes != null) {
/*  157 */       newnode.attributes = ((AttributeMap)this.attributes.cloneMap(newnode));
/*      */     }
/*  159 */     return newnode;
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */   {
/*  169 */     if (needsSyncData()) {
/*  170 */       synchronizeData();
/*      */     }
/*      */ 
/*  176 */     if (this.attributes != null) {
/*  177 */       Attr attrNode = (Attr)this.attributes.getNamedItem("xml:base");
/*  178 */       if (attrNode != null) {
/*  179 */         String uri = attrNode.getNodeValue();
/*  180 */         if (uri.length() != 0) {
/*      */           try {
/*  182 */             uri = new URI(uri).toString();
/*      */           }
/*      */           catch (URI.MalformedURIException e)
/*      */           {
/*  188 */             String parentBaseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
/*  189 */             if (parentBaseURI != null) {
/*      */               try {
/*  191 */                 uri = new URI(new URI(parentBaseURI), uri).toString();
/*      */               }
/*      */               catch (URI.MalformedURIException ex)
/*      */               {
/*  195 */                 return null;
/*      */               }
/*  197 */               return uri;
/*      */             }
/*  199 */             return null;
/*      */           }
/*  201 */           return uri;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  212 */     String baseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
/*      */ 
/*  214 */     if (baseURI != null) {
/*      */       try
/*      */       {
/*  217 */         return new URI(baseURI).toString();
/*      */       }
/*      */       catch (URI.MalformedURIException e) {
/*  220 */         return null;
/*      */       }
/*      */     }
/*  223 */     return null;
/*      */   }
/*      */ 
/*      */   void setOwnerDocument(CoreDocumentImpl doc)
/*      */   {
/*  233 */     super.setOwnerDocument(doc);
/*  234 */     if (this.attributes != null)
/*  235 */       this.attributes.setOwnerDocument(doc);
/*      */   }
/*      */ 
/*      */   public String getAttribute(String name)
/*      */   {
/*  254 */     if (needsSyncData()) {
/*  255 */       synchronizeData();
/*      */     }
/*  257 */     if (this.attributes == null) {
/*  258 */       return "";
/*      */     }
/*  260 */     Attr attr = (Attr)this.attributes.getNamedItem(name);
/*  261 */     return attr == null ? "" : attr.getValue();
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNode(String name)
/*      */   {
/*  275 */     if (needsSyncData()) {
/*  276 */       synchronizeData();
/*      */     }
/*  278 */     if (this.attributes == null) {
/*  279 */       return null;
/*      */     }
/*  281 */     return (Attr)this.attributes.getNamedItem(name);
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagName(String tagname)
/*      */   {
/*  302 */     return new DeepNodeListImpl(this, tagname);
/*      */   }
/*      */ 
/*      */   public String getTagName()
/*      */   {
/*  313 */     if (needsSyncData()) {
/*  314 */       synchronizeData();
/*      */     }
/*  316 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*  335 */     if (isNormalized()) {
/*  336 */       return;
/*      */     }
/*  338 */     if (needsSyncChildren())
/*  339 */       synchronizeChildren();
/*      */     ChildNode next;
/*  342 */     for (ChildNode kid = this.firstChild; kid != null; kid = next) {
/*  343 */       next = kid.nextSibling;
/*      */ 
/*  350 */       if (kid.getNodeType() == 3)
/*      */       {
/*  353 */         if ((next != null) && (next.getNodeType() == 3))
/*      */         {
/*  355 */           ((Text)kid).appendData(next.getNodeValue());
/*  356 */           removeChild(next);
/*  357 */           next = kid;
/*      */         }
/*  362 */         else if ((kid.getNodeValue() == null) || (kid.getNodeValue().length() == 0)) {
/*  363 */           removeChild(kid);
/*      */         }
/*      */ 
/*      */       }
/*  369 */       else if (kid.getNodeType() == 1) {
/*  370 */         kid.normalize();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  375 */     if (this.attributes != null)
/*      */     {
/*  377 */       for (int i = 0; i < this.attributes.getLength(); i++)
/*      */       {
/*  379 */         Node attr = this.attributes.item(i);
/*  380 */         attr.normalize();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  387 */     isNormalized(true);
/*      */   }
/*      */ 
/*      */   public void removeAttribute(String name)
/*      */   {
/*  407 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/*  408 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  409 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  412 */     if (needsSyncData()) {
/*  413 */       synchronizeData();
/*      */     }
/*      */ 
/*  416 */     if (this.attributes == null) {
/*  417 */       return;
/*      */     }
/*      */ 
/*  420 */     this.attributes.safeRemoveNamedItem(name);
/*      */   }
/*      */ 
/*      */   public Attr removeAttributeNode(Attr oldAttr)
/*      */     throws DOMException
/*      */   {
/*  444 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/*  445 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  446 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  449 */     if (needsSyncData()) {
/*  450 */       synchronizeData();
/*      */     }
/*      */ 
/*  453 */     if (this.attributes == null) {
/*  454 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  455 */       throw new DOMException((short)8, msg);
/*      */     }
/*  457 */     return (Attr)this.attributes.removeItem(oldAttr, true);
/*      */   }
/*      */ 
/*      */   public void setAttribute(String name, String value)
/*      */   {
/*  483 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/*  484 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*      */ 
/*  489 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  492 */     if (needsSyncData()) {
/*  493 */       synchronizeData();
/*      */     }
/*      */ 
/*  496 */     Attr newAttr = getAttributeNode(name);
/*  497 */     if (newAttr == null) {
/*  498 */       newAttr = getOwnerDocument().createAttribute(name);
/*      */ 
/*  500 */       if (this.attributes == null) {
/*  501 */         this.attributes = new AttributeMap(this, null);
/*      */       }
/*      */ 
/*  504 */       newAttr.setNodeValue(value);
/*  505 */       this.attributes.setNamedItem(newAttr);
/*      */     }
/*      */     else {
/*  508 */       newAttr.setNodeValue(value);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNode(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/*  530 */     if (needsSyncData()) {
/*  531 */       synchronizeData();
/*      */     }
/*      */ 
/*  534 */     if (this.ownerDocument.errorChecking) {
/*  535 */       if (isReadOnly()) {
/*  536 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  537 */         throw new DOMException((short)7, msg);
/*      */       }
/*      */ 
/*  542 */       if (newAttr.getOwnerDocument() != this.ownerDocument) {
/*  543 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*  544 */         throw new DOMException((short)4, msg);
/*      */       }
/*      */     }
/*      */ 
/*  548 */     if (this.attributes == null) {
/*  549 */       this.attributes = new AttributeMap(this, null);
/*      */     }
/*      */ 
/*  552 */     return (Attr)this.attributes.setNamedItem(newAttr);
/*      */   }
/*      */ 
/*      */   public String getAttributeNS(String namespaceURI, String localName)
/*      */   {
/*  576 */     if (needsSyncData()) {
/*  577 */       synchronizeData();
/*      */     }
/*      */ 
/*  580 */     if (this.attributes == null) {
/*  581 */       return "";
/*      */     }
/*      */ 
/*  584 */     Attr attr = (Attr)this.attributes.getNamedItemNS(namespaceURI, localName);
/*  585 */     return attr == null ? "" : attr.getValue();
/*      */   }
/*      */ 
/*      */   public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
/*      */   {
/*  632 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/*  633 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*      */ 
/*  638 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  642 */     if (needsSyncData()) {
/*  643 */       synchronizeData();
/*      */     }
/*  645 */     int index = qualifiedName.indexOf(':');
/*      */     String localName;
/*      */     String prefix;
/*      */     String localName;
/*  647 */     if (index < 0) {
/*  648 */       String prefix = null;
/*  649 */       localName = qualifiedName;
/*      */     }
/*      */     else {
/*  652 */       prefix = qualifiedName.substring(0, index);
/*  653 */       localName = qualifiedName.substring(index + 1);
/*      */     }
/*  655 */     Attr newAttr = getAttributeNodeNS(namespaceURI, localName);
/*  656 */     if (newAttr == null)
/*      */     {
/*  659 */       newAttr = getOwnerDocument().createAttributeNS(namespaceURI, qualifiedName);
/*      */ 
/*  662 */       if (this.attributes == null) {
/*  663 */         this.attributes = new AttributeMap(this, null);
/*      */       }
/*  665 */       newAttr.setNodeValue(value);
/*  666 */       this.attributes.setNamedItemNS(newAttr);
/*      */     }
/*      */     else {
/*  669 */       if ((newAttr instanceof AttrNSImpl)) {
/*  670 */         String origNodeName = ((AttrNSImpl)newAttr).name;
/*  671 */         String newName = prefix != null ? prefix + ":" + localName : localName;
/*      */ 
/*  673 */         ((AttrNSImpl)newAttr).name = newName;
/*      */ 
/*  675 */         if (!newName.equals(origNodeName))
/*      */         {
/*  682 */           newAttr = (Attr)this.attributes.removeItem(newAttr, false);
/*  683 */           this.attributes.addItem(newAttr);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  693 */         newAttr = new AttrNSImpl((CoreDocumentImpl)getOwnerDocument(), namespaceURI, qualifiedName, localName);
/*  694 */         this.attributes.setNamedItemNS(newAttr);
/*      */       }
/*      */ 
/*  697 */       newAttr.setNodeValue(value);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAttributeNS(String namespaceURI, String localName)
/*      */   {
/*  720 */     if ((this.ownerDocument.errorChecking) && (isReadOnly())) {
/*  721 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  722 */       throw new DOMException((short)7, msg);
/*      */     }
/*      */ 
/*  725 */     if (needsSyncData()) {
/*  726 */       synchronizeData();
/*      */     }
/*      */ 
/*  729 */     if (this.attributes == null) {
/*  730 */       return;
/*      */     }
/*      */ 
/*  733 */     this.attributes.safeRemoveNamedItemNS(namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNodeNS(String namespaceURI, String localName)
/*      */   {
/*  750 */     if (needsSyncData()) {
/*  751 */       synchronizeData();
/*      */     }
/*  753 */     if (this.attributes == null) {
/*  754 */       return null;
/*      */     }
/*  756 */     return (Attr)this.attributes.getNamedItemNS(namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNodeNS(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/*  791 */     if (needsSyncData()) {
/*  792 */       synchronizeData();
/*      */     }
/*  794 */     if (this.ownerDocument.errorChecking) {
/*  795 */       if (isReadOnly()) {
/*  796 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  797 */         throw new DOMException((short)7, msg);
/*      */       }
/*      */ 
/*  801 */       if (newAttr.getOwnerDocument() != this.ownerDocument) {
/*  802 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
/*  803 */         throw new DOMException((short)4, msg);
/*      */       }
/*      */     }
/*      */ 
/*  807 */     if (this.attributes == null) {
/*  808 */       this.attributes = new AttributeMap(this, null);
/*      */     }
/*      */ 
/*  811 */     return (Attr)this.attributes.setNamedItemNS(newAttr);
/*      */   }
/*      */ 
/*      */   protected int setXercesAttributeNode(Attr attr)
/*      */   {
/*  820 */     if (needsSyncData()) {
/*  821 */       synchronizeData();
/*      */     }
/*      */ 
/*  824 */     if (this.attributes == null) {
/*  825 */       this.attributes = new AttributeMap(this, null);
/*      */     }
/*  827 */     return this.attributes.addItem(attr);
/*      */   }
/*      */ 
/*      */   protected int getXercesAttribute(String namespaceURI, String localName)
/*      */   {
/*  836 */     if (needsSyncData()) {
/*  837 */       synchronizeData();
/*      */     }
/*  839 */     if (this.attributes == null) {
/*  840 */       return -1;
/*      */     }
/*  842 */     return this.attributes.getNamedItemIndex(namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes()
/*      */   {
/*  850 */     if (needsSyncData()) {
/*  851 */       synchronizeData();
/*      */     }
/*  853 */     return (this.attributes != null) && (this.attributes.getLength() != 0);
/*      */   }
/*      */ 
/*      */   public boolean hasAttribute(String name)
/*      */   {
/*  860 */     return getAttributeNode(name) != null;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributeNS(String namespaceURI, String localName)
/*      */   {
/*  867 */     return getAttributeNodeNS(namespaceURI, localName) != null;
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*      */   {
/*  890 */     return new DeepNodeListImpl(this, namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/*  899 */     if (!super.isEqualNode(arg)) {
/*  900 */       return false;
/*      */     }
/*  902 */     boolean hasAttrs = hasAttributes();
/*  903 */     if (hasAttrs != ((TypeInfo)arg).hasAttributes()) {
/*  904 */       return false;
/*      */     }
/*  906 */     if (hasAttrs) {
/*  907 */       NamedNodeMap map1 = getAttributes();
/*  908 */       NamedNodeMap map2 = ((TypeInfo)arg).getAttributes();
/*  909 */       int len = map1.getLength();
/*  910 */       if (len != map2.getLength()) {
/*  911 */         return false;
/*      */       }
/*  913 */       for (int i = 0; i < len; i++) {
/*  914 */         Node n1 = map1.item(i);
/*  915 */         if (n1.getLocalName() == null) {
/*  916 */           Node n2 = map2.getNamedItem(n1.getNodeName());
/*  917 */           if ((n2 == null) || (!((NodeImpl)n1).isEqualNode(n2)))
/*  918 */             return false;
/*      */         }
/*      */         else
/*      */         {
/*  922 */           Node n2 = map2.getNamedItemNS(n1.getNamespaceURI(), n1.getLocalName());
/*      */ 
/*  924 */           if ((n2 == null) || (!((NodeImpl)n1).isEqualNode(n2))) {
/*  925 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  930 */     return true;
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNode(Attr at, boolean makeId)
/*      */   {
/*  937 */     if (needsSyncData()) {
/*  938 */       synchronizeData();
/*      */     }
/*  940 */     if (this.ownerDocument.errorChecking) {
/*  941 */       if (isReadOnly()) {
/*  942 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  943 */         throw new DOMException((short)7, msg);
/*      */       }
/*      */ 
/*  948 */       if (at.getOwnerElement() != this) {
/*  949 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  950 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */     }
/*  953 */     ((AttrImpl)at).isIdAttribute(makeId);
/*  954 */     if (!makeId) {
/*  955 */       this.ownerDocument.removeIdentifier(at.getValue());
/*      */     }
/*      */     else
/*  958 */       this.ownerDocument.putIdentifier(at.getValue(), this);
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(String name, boolean makeId)
/*      */   {
/*  966 */     if (needsSyncData()) {
/*  967 */       synchronizeData();
/*      */     }
/*  969 */     Attr at = getAttributeNode(name);
/*      */ 
/*  971 */     if (at == null) {
/*  972 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*      */ 
/*  975 */       throw new DOMException((short)8, msg);
/*      */     }
/*      */ 
/*  978 */     if (this.ownerDocument.errorChecking) {
/*  979 */       if (isReadOnly()) {
/*  980 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/*  981 */         throw new DOMException((short)7, msg);
/*      */       }
/*      */ 
/*  986 */       if (at.getOwnerElement() != this) {
/*  987 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*  988 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */     }
/*      */ 
/*  992 */     ((AttrImpl)at).isIdAttribute(makeId);
/*  993 */     if (!makeId) {
/*  994 */       this.ownerDocument.removeIdentifier(at.getValue());
/*      */     }
/*      */     else
/*  997 */       this.ownerDocument.putIdentifier(at.getValue(), this);
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId)
/*      */   {
/* 1006 */     if (needsSyncData()) {
/* 1007 */       synchronizeData();
/*      */     }
/*      */ 
/* 1010 */     if (namespaceURI != null) {
/* 1011 */       namespaceURI = namespaceURI.length() == 0 ? null : namespaceURI;
/*      */     }
/* 1013 */     Attr at = getAttributeNodeNS(namespaceURI, localName);
/*      */ 
/* 1015 */     if (at == null) {
/* 1016 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/*      */ 
/* 1019 */       throw new DOMException((short)8, msg);
/*      */     }
/*      */ 
/* 1022 */     if (this.ownerDocument.errorChecking) {
/* 1023 */       if (isReadOnly()) {
/* 1024 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 1025 */         throw new DOMException((short)7, msg);
/*      */       }
/*      */ 
/* 1030 */       if (at.getOwnerElement() != this) {
/* 1031 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
/* 1032 */         throw new DOMException((short)8, msg);
/*      */       }
/*      */     }
/* 1035 */     ((AttrImpl)at).isIdAttribute(makeId);
/* 1036 */     if (!makeId) {
/* 1037 */       this.ownerDocument.removeIdentifier(at.getValue());
/*      */     }
/*      */     else
/* 1040 */       this.ownerDocument.putIdentifier(at.getValue(), this);
/*      */   }
/*      */ 
/*      */   public String getTypeName()
/*      */   {
/* 1048 */     return null;
/*      */   }
/*      */ 
/*      */   public String getTypeNamespace()
/*      */   {
/* 1055 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod)
/*      */   {
/* 1077 */     return false;
/*      */   }
/*      */ 
/*      */   public TypeInfo getSchemaTypeInfo()
/*      */   {
/* 1085 */     if (needsSyncData()) {
/* 1086 */       synchronizeData();
/*      */     }
/* 1088 */     return this;
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean readOnly, boolean deep)
/*      */   {
/* 1100 */     super.setReadOnly(readOnly, deep);
/* 1101 */     if (this.attributes != null)
/* 1102 */       this.attributes.setReadOnly(readOnly, true);
/*      */   }
/*      */ 
/*      */   protected void synchronizeData()
/*      */   {
/* 1116 */     needsSyncData(false);
/*      */ 
/* 1119 */     boolean orig = this.ownerDocument.getMutationEvents();
/* 1120 */     this.ownerDocument.setMutationEvents(false);
/*      */ 
/* 1123 */     setupDefaultAttributes();
/*      */ 
/* 1126 */     this.ownerDocument.setMutationEvents(orig);
/*      */   }
/*      */ 
/*      */   void moveSpecifiedAttributes(ElementImpl el)
/*      */   {
/* 1133 */     if (needsSyncData()) {
/* 1134 */       synchronizeData();
/*      */     }
/* 1136 */     if (el.hasAttributes()) {
/* 1137 */       if (this.attributes == null) {
/* 1138 */         this.attributes = new AttributeMap(this, null);
/*      */       }
/* 1140 */       this.attributes.moveSpecifiedAttributes(el.attributes);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setupDefaultAttributes()
/*      */   {
/* 1146 */     NamedNodeMapImpl defaults = getDefaultAttributes();
/* 1147 */     if (defaults != null)
/* 1148 */       this.attributes = new AttributeMap(this, defaults);
/*      */   }
/*      */ 
/*      */   protected void reconcileDefaultAttributes()
/*      */   {
/* 1154 */     if (this.attributes != null) {
/* 1155 */       NamedNodeMapImpl defaults = getDefaultAttributes();
/* 1156 */       this.attributes.reconcileDefaults(defaults);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected NamedNodeMapImpl getDefaultAttributes()
/*      */   {
/* 1163 */     DocumentTypeImpl doctype = (DocumentTypeImpl)this.ownerDocument.getDoctype();
/*      */ 
/* 1165 */     if (doctype == null) {
/* 1166 */       return null;
/*      */     }
/* 1168 */     ElementDefinitionImpl eldef = (ElementDefinitionImpl)doctype.getElements().getNamedItem(getNodeName());
/*      */ 
/* 1171 */     if (eldef == null) {
/* 1172 */       return null;
/*      */     }
/* 1174 */     return (NamedNodeMapImpl)eldef.getAttributes();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.ElementImpl
 * JD-Core Version:    0.6.2
 */