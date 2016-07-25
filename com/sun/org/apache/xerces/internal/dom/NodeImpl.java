/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.UserDataHandler;
/*      */ import org.w3c.dom.events.Event;
/*      */ import org.w3c.dom.events.EventListener;
/*      */ import org.w3c.dom.events.EventTarget;
/*      */ 
/*      */ public abstract class NodeImpl
/*      */   implements Node, NodeList, EventTarget, Cloneable, Serializable
/*      */ {
/*      */   public static final short TREE_POSITION_PRECEDING = 1;
/*      */   public static final short TREE_POSITION_FOLLOWING = 2;
/*      */   public static final short TREE_POSITION_ANCESTOR = 4;
/*      */   public static final short TREE_POSITION_DESCENDANT = 8;
/*      */   public static final short TREE_POSITION_EQUIVALENT = 16;
/*      */   public static final short TREE_POSITION_SAME_NODE = 32;
/*      */   public static final short TREE_POSITION_DISCONNECTED = 0;
/*      */   public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
/*      */   public static final short DOCUMENT_POSITION_PRECEDING = 2;
/*      */   public static final short DOCUMENT_POSITION_FOLLOWING = 4;
/*      */   public static final short DOCUMENT_POSITION_CONTAINS = 8;
/*      */   public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
/*      */   public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
/*      */   static final long serialVersionUID = -6316591992167219696L;
/*      */   public static final short ELEMENT_DEFINITION_NODE = 21;
/*      */   protected NodeImpl ownerNode;
/*      */   protected short flags;
/*      */   protected static final short READONLY = 1;
/*      */   protected static final short SYNCDATA = 2;
/*      */   protected static final short SYNCCHILDREN = 4;
/*      */   protected static final short OWNED = 8;
/*      */   protected static final short FIRSTCHILD = 16;
/*      */   protected static final short SPECIFIED = 32;
/*      */   protected static final short IGNORABLEWS = 64;
/*      */   protected static final short HASSTRING = 128;
/*      */   protected static final short NORMALIZED = 256;
/*      */   protected static final short ID = 512;
/*      */ 
/*      */   protected NodeImpl(CoreDocumentImpl ownerDocument)
/*      */   {
/*  178 */     this.ownerNode = ownerDocument;
/*      */   }
/*      */ 
/*      */   public NodeImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   public abstract short getNodeType();
/*      */ 
/*      */   public abstract String getNodeName();
/*      */ 
/*      */   public String getNodeValue()
/*      */     throws DOMException
/*      */   {
/*  205 */     return null;
/*      */   }
/*      */ 
/*      */   public void setNodeValue(String x)
/*      */     throws DOMException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Node appendChild(Node newChild)
/*      */     throws DOMException
/*      */   {
/*  238 */     return insertBefore(newChild, null);
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  266 */     if (needsSyncData()) {
/*  267 */       synchronizeData();
/*      */     }
/*      */     NodeImpl newnode;
/*      */     try
/*      */     {
/*  272 */       newnode = (NodeImpl)clone();
/*      */     }
/*      */     catch (CloneNotSupportedException e)
/*      */     {
/*  277 */       throw new RuntimeException("**Internal Error**" + e);
/*      */     }
/*      */ 
/*  281 */     newnode.ownerNode = ownerDocument();
/*  282 */     newnode.isOwned(false);
/*      */ 
/*  286 */     newnode.isReadOnly(false);
/*      */ 
/*  288 */     ownerDocument().callUserDataHandlers(this, newnode, (short)1);
/*      */ 
/*  291 */     return newnode;
/*      */   }
/*      */ 
/*      */   public Document getOwnerDocument()
/*      */   {
/*  303 */     if (isOwned()) {
/*  304 */       return this.ownerNode.ownerDocument();
/*      */     }
/*  306 */     return (Document)this.ownerNode;
/*      */   }
/*      */ 
/*      */   CoreDocumentImpl ownerDocument()
/*      */   {
/*  317 */     if (isOwned()) {
/*  318 */       return this.ownerNode.ownerDocument();
/*      */     }
/*  320 */     return (CoreDocumentImpl)this.ownerNode;
/*      */   }
/*      */ 
/*      */   void setOwnerDocument(CoreDocumentImpl doc)
/*      */   {
/*  329 */     if (needsSyncData()) {
/*  330 */       synchronizeData();
/*      */     }
/*      */ 
/*  334 */     if (!isOwned())
/*  335 */       this.ownerNode = doc;
/*      */   }
/*      */ 
/*      */   protected int getNodeNumber()
/*      */   {
/*  344 */     CoreDocumentImpl cd = (CoreDocumentImpl)getOwnerDocument();
/*  345 */     int nodeNumber = cd.getNodeNumber(this);
/*  346 */     return nodeNumber;
/*      */   }
/*      */ 
/*      */   public Node getParentNode()
/*      */   {
/*  356 */     return null;
/*      */   }
/*      */ 
/*      */   NodeImpl parentNode()
/*      */   {
/*  363 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getNextSibling()
/*      */   {
/*  368 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getPreviousSibling()
/*      */   {
/*  373 */     return null;
/*      */   }
/*      */ 
/*      */   ChildNode previousSibling() {
/*  377 */     return null;
/*      */   }
/*      */ 
/*      */   public NamedNodeMap getAttributes()
/*      */   {
/*  388 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes()
/*      */   {
/*  399 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes()
/*      */   {
/*  410 */     return false;
/*      */   }
/*      */ 
/*      */   public NodeList getChildNodes()
/*      */   {
/*  427 */     return this;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild()
/*      */   {
/*  436 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getLastChild()
/*      */   {
/*  445 */     return null;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  481 */     throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  503 */     throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  534 */     throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  552 */     return 0;
/*      */   }
/*      */ 
/*      */   public Node item(int index)
/*      */   {
/*  566 */     return null;
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/*  613 */     return ownerDocument().getImplementation().hasFeature(feature, version);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI()
/*      */   {
/*  636 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPrefix()
/*      */   {
/*  655 */     return null;
/*      */   }
/*      */ 
/*      */   public void setPrefix(String prefix)
/*      */     throws DOMException
/*      */   {
/*  682 */     throw new DOMException((short)14, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null));
/*      */   }
/*      */ 
/*      */   public String getLocalName()
/*      */   {
/*  701 */     return null;
/*      */   }
/*      */ 
/*      */   public void addEventListener(String type, EventListener listener, boolean useCapture)
/*      */   {
/*  711 */     ownerDocument().addEventListener(this, type, listener, useCapture);
/*      */   }
/*      */ 
/*      */   public void removeEventListener(String type, EventListener listener, boolean useCapture)
/*      */   {
/*  717 */     ownerDocument().removeEventListener(this, type, listener, useCapture);
/*      */   }
/*      */ 
/*      */   public boolean dispatchEvent(Event event)
/*      */   {
/*  722 */     return ownerDocument().dispatchEvent(this, event);
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */   {
/*  754 */     return null;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public short compareTreePosition(Node other)
/*      */   {
/*  805 */     if (this == other) {
/*  806 */       return 48;
/*      */     }
/*      */ 
/*  809 */     short thisType = getNodeType();
/*  810 */     short otherType = other.getNodeType();
/*      */ 
/*  813 */     if ((thisType == 6) || (thisType == 12) || (otherType == 6) || (otherType == 12))
/*      */     {
/*  817 */       return 0;
/*      */     }
/*      */ 
/*  828 */     Node thisAncestor = this;
/*  829 */     Node otherAncestor = other;
/*  830 */     int thisDepth = 0;
/*  831 */     int otherDepth = 0;
/*  832 */     for (Node node = this; node != null; node = node.getParentNode()) {
/*  833 */       thisDepth++;
/*  834 */       if (node == other)
/*      */       {
/*  836 */         return 5;
/*  837 */       }thisAncestor = node;
/*      */     }
/*      */ 
/*  840 */     for (node = other; node != null; node = node.getParentNode()) {
/*  841 */       otherDepth++;
/*  842 */       if (node == this)
/*      */       {
/*  844 */         return 10;
/*  845 */       }otherAncestor = node;
/*      */     }
/*      */ 
/*  849 */     Node thisNode = this;
/*  850 */     Node otherNode = other;
/*      */ 
/*  852 */     int thisAncestorType = thisAncestor.getNodeType();
/*  853 */     int otherAncestorType = otherAncestor.getNodeType();
/*      */ 
/*  858 */     if (thisAncestorType == 2) {
/*  859 */       thisNode = ((AttrImpl)thisAncestor).getOwnerElement();
/*      */     }
/*  861 */     if (otherAncestorType == 2) {
/*  862 */       otherNode = ((AttrImpl)otherAncestor).getOwnerElement();
/*      */     }
/*      */ 
/*  867 */     if ((thisAncestorType == 2) && (otherAncestorType == 2) && (thisNode == otherNode))
/*      */     {
/*  870 */       return 16;
/*      */     }
/*      */ 
/*  877 */     if (thisAncestorType == 2) {
/*  878 */       thisDepth = 0;
/*  879 */       for (node = thisNode; node != null; node = node.getParentNode()) {
/*  880 */         thisDepth++;
/*  881 */         if (node == otherNode)
/*      */         {
/*  884 */           return 1;
/*      */         }
/*  886 */         thisAncestor = node;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  892 */     if (otherAncestorType == 2) {
/*  893 */       otherDepth = 0;
/*  894 */       for (node = otherNode; node != null; node = node.getParentNode()) {
/*  895 */         otherDepth++;
/*  896 */         if (node == thisNode)
/*      */         {
/*  899 */           return 2;
/*  900 */         }otherAncestor = node;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  906 */     if (thisAncestor != otherAncestor) {
/*  907 */       return 0;
/*      */     }
/*      */ 
/*  913 */     if (thisDepth > otherDepth) {
/*  914 */       for (int i = 0; i < thisDepth - otherDepth; i++) {
/*  915 */         thisNode = thisNode.getParentNode();
/*      */       }
/*      */ 
/*  919 */       if (thisNode == otherNode)
/*  920 */         return 1;
/*      */     }
/*      */     else
/*      */     {
/*  924 */       for (int i = 0; i < otherDepth - thisDepth; i++) {
/*  925 */         otherNode = otherNode.getParentNode();
/*      */       }
/*      */ 
/*  929 */       if (otherNode == thisNode) {
/*  930 */         return 2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  936 */     Node thisNodeP = thisNode.getParentNode();
/*  937 */     Node otherNodeP = otherNode.getParentNode();
/*  938 */     while (thisNodeP != otherNodeP) {
/*  939 */       thisNode = thisNodeP;
/*  940 */       otherNode = otherNodeP;
/*  941 */       thisNodeP = thisNodeP.getParentNode();
/*  942 */       otherNodeP = otherNodeP.getParentNode();
/*      */     }
/*      */ 
/*  949 */     for (Node current = thisNodeP.getFirstChild(); 
/*  950 */       current != null; 
/*  951 */       current = current.getNextSibling()) {
/*  952 */       if (current == otherNode) {
/*  953 */         return 1;
/*      */       }
/*  955 */       if (current == thisNode) {
/*  956 */         return 2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  961 */     return 0;
/*      */   }
/*      */ 
/*      */   public short compareDocumentPosition(Node other)
/*      */     throws DOMException
/*      */   {
/*  975 */     if (this == other) {
/*  976 */       return 0;
/*      */     }
/*      */     try
/*      */     {
/*  980 */       node = (NodeImpl)other;
/*      */     }
/*      */     catch (ClassCastException e)
/*      */     {
/*      */       NodeImpl node;
/*  983 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
/*      */ 
/*  985 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */     Document thisOwnerDoc;
/*      */     Document thisOwnerDoc;
/*  990 */     if (getNodeType() == 9)
/*  991 */       thisOwnerDoc = (Document)this;
/*      */     else
/*  993 */       thisOwnerDoc = getOwnerDocument();
/*      */     Document otherOwnerDoc;
/*      */     Document otherOwnerDoc;
/*  994 */     if (other.getNodeType() == 9)
/*  995 */       otherOwnerDoc = (Document)other;
/*      */     else {
/*  997 */       otherOwnerDoc = other.getOwnerDocument();
/*      */     }
/*      */ 
/* 1001 */     if ((thisOwnerDoc != otherOwnerDoc) && (thisOwnerDoc != null) && (otherOwnerDoc != null))
/*      */     {
/* 1005 */       int otherDocNum = ((CoreDocumentImpl)otherOwnerDoc).getNodeNumber();
/* 1006 */       int thisDocNum = ((CoreDocumentImpl)thisOwnerDoc).getNodeNumber();
/* 1007 */       if (otherDocNum > thisDocNum) {
/* 1008 */         return 37;
/*      */       }
/*      */ 
/* 1012 */       return 35;
/*      */     }
/*      */ 
/* 1026 */     Node thisAncestor = this;
/* 1027 */     Node otherAncestor = other;
/*      */ 
/* 1029 */     int thisDepth = 0;
/* 1030 */     int otherDepth = 0;
/* 1031 */     for (Node node = this; node != null; node = node.getParentNode()) {
/* 1032 */       thisDepth++;
/* 1033 */       if (node == other)
/*      */       {
/* 1035 */         return 10;
/*      */       }
/* 1037 */       thisAncestor = node;
/*      */     }
/*      */ 
/* 1040 */     for (node = other; node != null; node = node.getParentNode()) {
/* 1041 */       otherDepth++;
/* 1042 */       if (node == this)
/*      */       {
/* 1044 */         return 20;
/*      */       }
/* 1046 */       otherAncestor = node;
/*      */     }
/*      */ 
/* 1051 */     int thisAncestorType = thisAncestor.getNodeType();
/* 1052 */     int otherAncestorType = otherAncestor.getNodeType();
/* 1053 */     Node thisNode = this;
/* 1054 */     Node otherNode = other;
/*      */ 
/* 1058 */     switch (thisAncestorType) {
/*      */     case 6:
/*      */     case 12:
/* 1061 */       DocumentType container = thisOwnerDoc.getDoctype();
/* 1062 */       if (container == otherAncestor) return 10;
/*      */ 
/* 1064 */       switch (otherAncestorType) {
/*      */       case 6:
/*      */       case 12:
/* 1067 */         if (thisAncestorType != otherAncestorType)
/*      */         {
/* 1069 */           return thisAncestorType > otherAncestorType ? 2 : 4;
/*      */         }
/*      */ 
/* 1073 */         if (thisAncestorType == 12)
/*      */         {
/* 1075 */           if (((NamedNodeMapImpl)container.getNotations()).precedes(otherAncestor, thisAncestor)) {
/* 1076 */             return 34;
/*      */           }
/*      */ 
/* 1079 */           return 36;
/*      */         }
/*      */ 
/* 1082 */         if (((NamedNodeMapImpl)container.getEntities()).precedes(otherAncestor, thisAncestor)) {
/* 1083 */           return 34;
/*      */         }
/*      */ 
/* 1086 */         return 36;
/*      */       }
/*      */ 
/* 1091 */       thisNode = thisAncestor = thisOwnerDoc;
/* 1092 */       break;
/*      */     case 10:
/* 1095 */       if (otherNode == thisOwnerDoc) {
/* 1096 */         return 10;
/*      */       }
/* 1098 */       if ((thisOwnerDoc != null) && (thisOwnerDoc == otherOwnerDoc)) {
/* 1099 */         return 4;
/*      */       }
/*      */       break;
/*      */     case 2:
/* 1103 */       thisNode = ((AttrImpl)thisAncestor).getOwnerElement();
/* 1104 */       if (otherAncestorType == 2) {
/* 1105 */         otherNode = ((AttrImpl)otherAncestor).getOwnerElement();
/* 1106 */         if (otherNode == thisNode) {
/* 1107 */           if (((NamedNodeMapImpl)thisNode.getAttributes()).precedes(other, this)) {
/* 1108 */             return 34;
/*      */           }
/*      */ 
/* 1111 */           return 36;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1117 */       thisDepth = 0;
/* 1118 */       for (node = thisNode; node != null; node = node.getParentNode()) {
/* 1119 */         thisDepth++;
/* 1120 */         if (node == otherNode)
/*      */         {
/* 1123 */           return 10;
/*      */         }
/*      */ 
/* 1126 */         thisAncestor = node;
/*      */       }
/*      */     }
/*      */ 
/* 1130 */     switch (otherAncestorType) {
/*      */     case 6:
/*      */     case 12:
/* 1133 */       DocumentType container = thisOwnerDoc.getDoctype();
/* 1134 */       if (container == this) return 20;
/*      */ 
/* 1136 */       otherNode = otherAncestor = thisOwnerDoc;
/* 1137 */       break;
/*      */     case 10:
/* 1140 */       if (thisNode == otherOwnerDoc) {
/* 1141 */         return 20;
/*      */       }
/* 1143 */       if ((otherOwnerDoc != null) && (thisOwnerDoc == otherOwnerDoc)) {
/* 1144 */         return 2;
/*      */       }
/*      */       break;
/*      */     case 2:
/* 1148 */       otherDepth = 0;
/* 1149 */       otherNode = ((AttrImpl)otherAncestor).getOwnerElement();
/* 1150 */       for (node = otherNode; node != null; node = node.getParentNode()) {
/* 1151 */         otherDepth++;
/* 1152 */         if (node == thisNode)
/*      */         {
/* 1155 */           return 20;
/*      */         }
/* 1157 */         otherAncestor = node;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1165 */     if (thisAncestor != otherAncestor)
/*      */     {
/* 1167 */       int thisAncestorNum = ((NodeImpl)thisAncestor).getNodeNumber();
/* 1168 */       int otherAncestorNum = ((NodeImpl)otherAncestor).getNodeNumber();
/*      */ 
/* 1170 */       if (thisAncestorNum > otherAncestorNum) {
/* 1171 */         return 37;
/*      */       }
/*      */ 
/* 1175 */       return 35;
/*      */     }
/*      */ 
/* 1184 */     if (thisDepth > otherDepth) {
/* 1185 */       for (int i = 0; i < thisDepth - otherDepth; i++) {
/* 1186 */         thisNode = thisNode.getParentNode();
/*      */       }
/*      */ 
/* 1190 */       if (thisNode == otherNode)
/*      */       {
/* 1192 */         return 2;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1197 */       for (int i = 0; i < otherDepth - thisDepth; i++) {
/* 1198 */         otherNode = otherNode.getParentNode();
/*      */       }
/*      */ 
/* 1202 */       if (otherNode == thisNode) {
/* 1203 */         return 4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1209 */     Node thisNodeP = thisNode.getParentNode();
/* 1210 */     Node otherNodeP = otherNode.getParentNode();
/* 1211 */     while (thisNodeP != otherNodeP) {
/* 1212 */       thisNode = thisNodeP;
/* 1213 */       otherNode = otherNodeP;
/* 1214 */       thisNodeP = thisNodeP.getParentNode();
/* 1215 */       otherNodeP = otherNodeP.getParentNode();
/*      */     }
/*      */ 
/* 1222 */     for (Node current = thisNodeP.getFirstChild(); 
/* 1223 */       current != null; 
/* 1224 */       current = current.getNextSibling()) {
/* 1225 */       if (current == otherNode) {
/* 1226 */         return 2;
/*      */       }
/* 1228 */       if (current == thisNode) {
/* 1229 */         return 4;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1234 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/* 1302 */     return getNodeValue();
/*      */   }
/*      */ 
/*      */   void getTextContent(StringBuffer buf) throws DOMException
/*      */   {
/* 1307 */     String content = getNodeValue();
/* 1308 */     if (content != null)
/* 1309 */       buf.append(content);
/*      */   }
/*      */ 
/*      */   public void setTextContent(String textContent)
/*      */     throws DOMException
/*      */   {
/* 1360 */     setNodeValue(textContent);
/*      */   }
/*      */ 
/*      */   public boolean isSameNode(Node other)
/*      */   {
/* 1379 */     return this == other;
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(String namespaceURI)
/*      */   {
/* 1396 */     short type = getNodeType();
/* 1397 */     switch (type) {
/*      */     case 1:
/* 1399 */       String namespace = getNamespaceURI();
/* 1400 */       String prefix = getPrefix();
/*      */ 
/* 1403 */       if ((prefix == null) || (prefix.length() == 0)) {
/* 1404 */         if (namespaceURI == null) {
/* 1405 */           return namespace == namespaceURI;
/*      */         }
/* 1407 */         return namespaceURI.equals(namespace);
/*      */       }
/* 1409 */       if (hasAttributes()) {
/* 1410 */         ElementImpl elem = (ElementImpl)this;
/* 1411 */         NodeImpl attr = (NodeImpl)elem.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/* 1412 */         if (attr != null) {
/* 1413 */           String value = attr.getNodeValue();
/* 1414 */           if (namespaceURI == null) {
/* 1415 */             return namespace == value;
/*      */           }
/* 1417 */           return namespaceURI.equals(value);
/*      */         }
/*      */       }
/*      */ 
/* 1421 */       NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/* 1422 */       if (ancestor != null) {
/* 1423 */         return ancestor.isDefaultNamespace(namespaceURI);
/*      */       }
/* 1425 */       return false;
/*      */     case 9:
/* 1428 */       return ((NodeImpl)((Document)this).getDocumentElement()).isDefaultNamespace(namespaceURI);
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1436 */       return false;
/*      */     case 2:
/* 1438 */       if (this.ownerNode.getNodeType() == 1) {
/* 1439 */         return this.ownerNode.isDefaultNamespace(namespaceURI);
/*      */       }
/*      */ 
/* 1442 */       return false;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/* 1445 */     case 8: } NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/* 1446 */     if (ancestor != null) {
/* 1447 */       return ancestor.isDefaultNamespace(namespaceURI);
/*      */     }
/* 1449 */     return false;
/*      */   }
/*      */ 
/*      */   public String lookupPrefix(String namespaceURI)
/*      */   {
/* 1470 */     if (namespaceURI == null) {
/* 1471 */       return null;
/*      */     }
/*      */ 
/* 1474 */     short type = getNodeType();
/*      */ 
/* 1476 */     switch (type)
/*      */     {
/*      */     case 1:
/* 1479 */       String namespace = getNamespaceURI();
/* 1480 */       return lookupNamespacePrefix(namespaceURI, (ElementImpl)this);
/*      */     case 9:
/* 1483 */       return ((NodeImpl)((Document)this).getDocumentElement()).lookupPrefix(namespaceURI);
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1491 */       return null;
/*      */     case 2:
/* 1493 */       if (this.ownerNode.getNodeType() == 1) {
/* 1494 */         return this.ownerNode.lookupPrefix(namespaceURI);
/*      */       }
/*      */ 
/* 1497 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/* 1500 */     case 8: } NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/* 1501 */     if (ancestor != null) {
/* 1502 */       return ancestor.lookupPrefix(namespaceURI);
/*      */     }
/* 1504 */     return null;
/*      */   }
/*      */ 
/*      */   public String lookupNamespaceURI(String specifiedPrefix)
/*      */   {
/* 1519 */     short type = getNodeType();
/* 1520 */     switch (type)
/*      */     {
/*      */     case 1:
/* 1523 */       String namespace = getNamespaceURI();
/* 1524 */       String prefix = getPrefix();
/* 1525 */       if (namespace != null)
/*      */       {
/* 1527 */         if ((specifiedPrefix == null) && (prefix == specifiedPrefix))
/*      */         {
/* 1529 */           return namespace;
/* 1530 */         }if ((prefix != null) && (prefix.equals(specifiedPrefix)))
/*      */         {
/* 1532 */           return namespace;
/*      */         }
/*      */       }
/* 1535 */       if (hasAttributes()) {
/* 1536 */         NamedNodeMap map = getAttributes();
/* 1537 */         int length = map.getLength();
/* 1538 */         for (int i = 0; i < length; i++) {
/* 1539 */           Node attr = map.item(i);
/* 1540 */           String attrPrefix = attr.getPrefix();
/* 1541 */           String value = attr.getNodeValue();
/* 1542 */           namespace = attr.getNamespaceURI();
/* 1543 */           if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
/*      */           {
/* 1545 */             if ((specifiedPrefix == null) && (attr.getNodeName().equals("xmlns")))
/*      */             {
/* 1548 */               return value;
/* 1549 */             }if ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (attr.getLocalName().equals(specifiedPrefix)))
/*      */             {
/* 1553 */               return value;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1558 */       NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/* 1559 */       if (ancestor != null) {
/* 1560 */         return ancestor.lookupNamespaceURI(specifiedPrefix);
/*      */       }
/*      */ 
/* 1563 */       return null;
/*      */     case 9:
/* 1568 */       return ((NodeImpl)((Document)this).getDocumentElement()).lookupNamespaceURI(specifiedPrefix);
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1575 */       return null;
/*      */     case 2:
/* 1577 */       if (this.ownerNode.getNodeType() == 1) {
/* 1578 */         return this.ownerNode.lookupNamespaceURI(specifiedPrefix);
/*      */       }
/*      */ 
/* 1581 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/* 1584 */     case 8: } NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/* 1585 */     if (ancestor != null) {
/* 1586 */       return ancestor.lookupNamespaceURI(specifiedPrefix);
/*      */     }
/* 1588 */     return null;
/*      */   }
/*      */ 
/*      */   Node getElementAncestor(Node currentNode)
/*      */   {
/* 1596 */     Node parent = currentNode.getParentNode();
/* 1597 */     if (parent != null) {
/* 1598 */       short type = parent.getNodeType();
/* 1599 */       if (type == 1) {
/* 1600 */         return parent;
/*      */       }
/* 1602 */       return getElementAncestor(parent);
/*      */     }
/* 1604 */     return null;
/*      */   }
/*      */ 
/*      */   String lookupNamespacePrefix(String namespaceURI, ElementImpl el) {
/* 1608 */     String namespace = getNamespaceURI();
/*      */ 
/* 1611 */     String prefix = getPrefix();
/*      */ 
/* 1613 */     if ((namespace != null) && (namespace.equals(namespaceURI)) && 
/* 1614 */       (prefix != null)) {
/* 1615 */       String foundNamespace = el.lookupNamespaceURI(prefix);
/* 1616 */       if ((foundNamespace != null) && (foundNamespace.equals(namespaceURI))) {
/* 1617 */         return prefix;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1622 */     if (hasAttributes()) {
/* 1623 */       NamedNodeMap map = getAttributes();
/* 1624 */       int length = map.getLength();
/* 1625 */       for (int i = 0; i < length; i++) {
/* 1626 */         Node attr = map.item(i);
/* 1627 */         String attrPrefix = attr.getPrefix();
/* 1628 */         String value = attr.getNodeValue();
/* 1629 */         namespace = attr.getNamespaceURI();
/* 1630 */         if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
/*      */         {
/* 1632 */           if ((attr.getNodeName().equals("xmlns")) || ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (value.equals(namespaceURI))))
/*      */           {
/* 1636 */             String localname = attr.getLocalName();
/* 1637 */             String foundNamespace = el.lookupNamespaceURI(localname);
/* 1638 */             if ((foundNamespace != null) && (foundNamespace.equals(namespaceURI))) {
/* 1639 */               return localname;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1647 */     NodeImpl ancestor = (NodeImpl)getElementAncestor(this);
/*      */ 
/* 1649 */     if (ancestor != null) {
/* 1650 */       return ancestor.lookupNamespacePrefix(namespaceURI, el);
/*      */     }
/* 1652 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/* 1698 */     if (arg == this) {
/* 1699 */       return true;
/*      */     }
/* 1701 */     if (arg.getNodeType() != getNodeType()) {
/* 1702 */       return false;
/*      */     }
/*      */ 
/* 1706 */     if (getNodeName() == null) {
/* 1707 */       if (arg.getNodeName() != null) {
/* 1708 */         return false;
/*      */       }
/*      */     }
/* 1711 */     else if (!getNodeName().equals(arg.getNodeName())) {
/* 1712 */       return false;
/*      */     }
/*      */ 
/* 1715 */     if (getLocalName() == null) {
/* 1716 */       if (arg.getLocalName() != null) {
/* 1717 */         return false;
/*      */       }
/*      */     }
/* 1720 */     else if (!getLocalName().equals(arg.getLocalName())) {
/* 1721 */       return false;
/*      */     }
/*      */ 
/* 1724 */     if (getNamespaceURI() == null) {
/* 1725 */       if (arg.getNamespaceURI() != null) {
/* 1726 */         return false;
/*      */       }
/*      */     }
/* 1729 */     else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
/* 1730 */       return false;
/*      */     }
/*      */ 
/* 1733 */     if (getPrefix() == null) {
/* 1734 */       if (arg.getPrefix() != null) {
/* 1735 */         return false;
/*      */       }
/*      */     }
/* 1738 */     else if (!getPrefix().equals(arg.getPrefix())) {
/* 1739 */       return false;
/*      */     }
/*      */ 
/* 1742 */     if (getNodeValue() == null) {
/* 1743 */       if (arg.getNodeValue() != null) {
/* 1744 */         return false;
/*      */       }
/*      */     }
/* 1747 */     else if (!getNodeValue().equals(arg.getNodeValue())) {
/* 1748 */       return false;
/*      */     }
/*      */ 
/* 1752 */     return true;
/*      */   }
/*      */ 
/*      */   public Object getFeature(String feature, String version)
/*      */   {
/* 1761 */     return isSupported(feature, version) ? this : null;
/*      */   }
/*      */ 
/*      */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*      */   {
/* 1780 */     return ownerDocument().setUserData(this, key, data, handler);
/*      */   }
/*      */ 
/*      */   public Object getUserData(String key)
/*      */   {
/* 1793 */     return ownerDocument().getUserData(this, key);
/*      */   }
/*      */ 
/*      */   protected Hashtable getUserDataRecord() {
/* 1797 */     return ownerDocument().getUserDataRecord(this);
/*      */   }
/*      */ 
/*      */   public void setReadOnly(boolean readOnly, boolean deep)
/*      */   {
/* 1824 */     if (needsSyncData()) {
/* 1825 */       synchronizeData();
/*      */     }
/* 1827 */     isReadOnly(readOnly);
/*      */   }
/*      */ 
/*      */   public boolean getReadOnly()
/*      */   {
/* 1837 */     if (needsSyncData()) {
/* 1838 */       synchronizeData();
/*      */     }
/* 1840 */     return isReadOnly();
/*      */   }
/*      */ 
/*      */   public void setUserData(Object data)
/*      */   {
/* 1857 */     ownerDocument().setUserData(this, data);
/*      */   }
/*      */ 
/*      */   public Object getUserData()
/*      */   {
/* 1865 */     return ownerDocument().getUserData(this);
/*      */   }
/*      */ 
/*      */   protected void changed()
/*      */   {
/* 1879 */     ownerDocument().changed();
/*      */   }
/*      */ 
/*      */   protected int changes()
/*      */   {
/* 1889 */     return ownerDocument().changes();
/*      */   }
/*      */ 
/*      */   protected void synchronizeData()
/*      */   {
/* 1898 */     needsSyncData(false);
/*      */   }
/*      */ 
/*      */   protected Node getContainer()
/*      */   {
/* 1906 */     return null;
/*      */   }
/*      */ 
/*      */   final boolean isReadOnly()
/*      */   {
/* 1915 */     return (this.flags & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   final void isReadOnly(boolean value) {
/* 1919 */     this.flags = ((short)(value ? this.flags | 0x1 : this.flags & 0xFFFFFFFE));
/*      */   }
/*      */ 
/*      */   final boolean needsSyncData() {
/* 1923 */     return (this.flags & 0x2) != 0;
/*      */   }
/*      */ 
/*      */   final void needsSyncData(boolean value) {
/* 1927 */     this.flags = ((short)(value ? this.flags | 0x2 : this.flags & 0xFFFFFFFD));
/*      */   }
/*      */ 
/*      */   final boolean needsSyncChildren() {
/* 1931 */     return (this.flags & 0x4) != 0;
/*      */   }
/*      */ 
/*      */   public final void needsSyncChildren(boolean value) {
/* 1935 */     this.flags = ((short)(value ? this.flags | 0x4 : this.flags & 0xFFFFFFFB));
/*      */   }
/*      */ 
/*      */   final boolean isOwned() {
/* 1939 */     return (this.flags & 0x8) != 0;
/*      */   }
/*      */ 
/*      */   final void isOwned(boolean value) {
/* 1943 */     this.flags = ((short)(value ? this.flags | 0x8 : this.flags & 0xFFFFFFF7));
/*      */   }
/*      */ 
/*      */   final boolean isFirstChild() {
/* 1947 */     return (this.flags & 0x10) != 0;
/*      */   }
/*      */ 
/*      */   final void isFirstChild(boolean value) {
/* 1951 */     this.flags = ((short)(value ? this.flags | 0x10 : this.flags & 0xFFFFFFEF));
/*      */   }
/*      */ 
/*      */   final boolean isSpecified() {
/* 1955 */     return (this.flags & 0x20) != 0;
/*      */   }
/*      */ 
/*      */   final void isSpecified(boolean value) {
/* 1959 */     this.flags = ((short)(value ? this.flags | 0x20 : this.flags & 0xFFFFFFDF));
/*      */   }
/*      */ 
/*      */   final boolean internalIsIgnorableWhitespace()
/*      */   {
/* 1964 */     return (this.flags & 0x40) != 0;
/*      */   }
/*      */ 
/*      */   final void isIgnorableWhitespace(boolean value) {
/* 1968 */     this.flags = ((short)(value ? this.flags | 0x40 : this.flags & 0xFFFFFFBF));
/*      */   }
/*      */ 
/*      */   final boolean hasStringValue() {
/* 1972 */     return (this.flags & 0x80) != 0;
/*      */   }
/*      */ 
/*      */   final void hasStringValue(boolean value) {
/* 1976 */     this.flags = ((short)(value ? this.flags | 0x80 : this.flags & 0xFFFFFF7F));
/*      */   }
/*      */ 
/*      */   final boolean isNormalized() {
/* 1980 */     return (this.flags & 0x100) != 0;
/*      */   }
/*      */ 
/*      */   final void isNormalized(boolean value)
/*      */   {
/* 1985 */     if ((!value) && (isNormalized()) && (this.ownerNode != null)) {
/* 1986 */       this.ownerNode.isNormalized(false);
/*      */     }
/* 1988 */     this.flags = ((short)(value ? this.flags | 0x100 : this.flags & 0xFFFFFEFF));
/*      */   }
/*      */ 
/*      */   final boolean isIdAttribute() {
/* 1992 */     return (this.flags & 0x200) != 0;
/*      */   }
/*      */ 
/*      */   final void isIdAttribute(boolean value) {
/* 1996 */     this.flags = ((short)(value ? this.flags | 0x200 : this.flags & 0xFFFFFDFF));
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2005 */     return "[" + getNodeName() + ": " + getNodeValue() + "]";
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream out)
/*      */     throws IOException
/*      */   {
/* 2016 */     if (needsSyncData()) {
/* 2017 */       synchronizeData();
/*      */     }
/*      */ 
/* 2020 */     out.defaultWriteObject();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.NodeImpl
 * JD-Core Version:    0.6.2
 */