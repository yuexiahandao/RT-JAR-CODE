/*      */ package com.sun.org.apache.xml.internal.dtm.ref;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.Objects;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMDOMException;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xpath.internal.NodeSet;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.CDATASection;
/*      */ import org.w3c.dom.Comment;
/*      */ import org.w3c.dom.DOMConfiguration;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.EntityReference;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.ProcessingInstruction;
/*      */ import org.w3c.dom.Text;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ import org.w3c.dom.UserDataHandler;
/*      */ 
/*      */ public class DTMNodeProxy
/*      */   implements Node, Document, Text, Element, Attr, ProcessingInstruction, Comment, DocumentFragment
/*      */ {
/*      */   public DTM dtm;
/*      */   int node;
/*      */   private static final String EMPTYSTRING = "";
/*   80 */   static final DOMImplementation implementation = new DTMNodeProxyImplementation();
/*      */   protected String fDocumentURI;
/*      */   protected String actualEncoding;
/*      */   private String xmlEncoding;
/*      */   private boolean xmlStandalone;
/*      */   private String xmlVersion;
/*      */ 
/*      */   public DTMNodeProxy(DTM dtm, int node)
/*      */   {
/*   90 */     this.dtm = dtm;
/*   91 */     this.node = node;
/*      */   }
/*      */ 
/*      */   public final DTM getDTM()
/*      */   {
/*  101 */     return this.dtm;
/*      */   }
/*      */ 
/*      */   public final int getDTMNodeNumber()
/*      */   {
/*  111 */     return this.node;
/*      */   }
/*      */ 
/*      */   public final boolean equals(Node node)
/*      */   {
/*      */     try
/*      */     {
/*  126 */       DTMNodeProxy dtmp = (DTMNodeProxy)node;
/*      */ 
/*  130 */       return (dtmp.node == this.node) && (dtmp.dtm == this.dtm);
/*      */     }
/*      */     catch (ClassCastException cce) {
/*      */     }
/*  134 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean equals(Object node)
/*      */   {
/*  151 */     return ((node instanceof DocumentFragment)) && (equals((DocumentFragment)node));
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  156 */     int hash = 7;
/*  157 */     hash = 29 * hash + Objects.hashCode(this.dtm);
/*  158 */     hash = 29 * hash + this.node;
/*  159 */     return hash;
/*      */   }
/*      */ 
/*      */   public final boolean sameNodeAs(Node other)
/*      */   {
/*  172 */     if (!(other instanceof DTMNodeProxy)) {
/*  173 */       return false;
/*      */     }
/*  175 */     DTMNodeProxy that = (DTMNodeProxy)other;
/*      */ 
/*  177 */     return (this.dtm == that.dtm) && (this.node == that.node);
/*      */   }
/*      */ 
/*      */   public final String getNodeName()
/*      */   {
/*  188 */     return this.dtm.getNodeName(this.node);
/*      */   }
/*      */ 
/*      */   public final String getTarget()
/*      */   {
/*  207 */     return this.dtm.getNodeName(this.node);
/*      */   }
/*      */ 
/*      */   public final String getLocalName()
/*      */   {
/*  218 */     return this.dtm.getLocalName(this.node);
/*      */   }
/*      */ 
/*      */   public final String getPrefix()
/*      */   {
/*  228 */     return this.dtm.getPrefix(this.node);
/*      */   }
/*      */ 
/*      */   public final void setPrefix(String prefix)
/*      */     throws DOMException
/*      */   {
/*  241 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI()
/*      */   {
/*  252 */     return this.dtm.getNamespaceURI(this.node);
/*      */   }
/*      */ 
/*      */   public final boolean supports(String feature, String version)
/*      */   {
/*  273 */     return implementation.hasFeature(feature, version);
/*      */   }
/*      */ 
/*      */   public final boolean isSupported(String feature, String version)
/*      */   {
/*  290 */     return implementation.hasFeature(feature, version);
/*      */   }
/*      */ 
/*      */   public final String getNodeValue()
/*      */     throws DOMException
/*      */   {
/*  304 */     return this.dtm.getNodeValue(this.node);
/*      */   }
/*      */ 
/*      */   public final String getStringValue()
/*      */     throws DOMException
/*      */   {
/*  314 */     return this.dtm.getStringValue(this.node).toString();
/*      */   }
/*      */ 
/*      */   public final void setNodeValue(String nodeValue)
/*      */     throws DOMException
/*      */   {
/*  327 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final short getNodeType()
/*      */   {
/*  338 */     return this.dtm.getNodeType(this.node);
/*      */   }
/*      */ 
/*      */   public final Node getParentNode()
/*      */   {
/*  350 */     if (getNodeType() == 2) {
/*  351 */       return null;
/*      */     }
/*  353 */     int newnode = this.dtm.getParent(this.node);
/*      */ 
/*  355 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final Node getOwnerNode()
/*      */   {
/*  366 */     int newnode = this.dtm.getParent(this.node);
/*      */ 
/*  368 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final NodeList getChildNodes()
/*      */   {
/*  383 */     return new DTMChildIterNodeList(this.dtm, this.node);
/*      */   }
/*      */ 
/*      */   public final Node getFirstChild()
/*      */   {
/*  397 */     int newnode = this.dtm.getFirstChild(this.node);
/*      */ 
/*  399 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final Node getLastChild()
/*      */   {
/*  411 */     int newnode = this.dtm.getLastChild(this.node);
/*      */ 
/*  413 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final Node getPreviousSibling()
/*      */   {
/*  425 */     int newnode = this.dtm.getPreviousSibling(this.node);
/*      */ 
/*  427 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final Node getNextSibling()
/*      */   {
/*  440 */     if (this.dtm.getNodeType(this.node) == 2) {
/*  441 */       return null;
/*      */     }
/*  443 */     int newnode = this.dtm.getNextSibling(this.node);
/*      */ 
/*  445 */     return newnode == -1 ? null : this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public final NamedNodeMap getAttributes()
/*      */   {
/*  459 */     return new DTMNamedNodeMap(this.dtm, this.node);
/*      */   }
/*      */ 
/*      */   public boolean hasAttribute(String name)
/*      */   {
/*  472 */     return -1 != this.dtm.getAttributeNode(this.node, null, name);
/*      */   }
/*      */ 
/*      */   public boolean hasAttributeNS(String namespaceURI, String localName)
/*      */   {
/*  487 */     return -1 != this.dtm.getAttributeNode(this.node, namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public final Document getOwnerDocument()
/*      */   {
/*  499 */     return (Document)this.dtm.getNode(this.dtm.getOwnerDocument(this.node));
/*      */   }
/*      */ 
/*      */   public final Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  516 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  533 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  548 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final Node appendChild(Node newChild)
/*      */     throws DOMException
/*      */   {
/*  563 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final boolean hasChildNodes()
/*      */   {
/*  574 */     return -1 != this.dtm.getFirstChild(this.node);
/*      */   }
/*      */ 
/*      */   public final Node cloneNode(boolean deep)
/*      */   {
/*  587 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final DocumentType getDoctype()
/*      */   {
/*  598 */     return null;
/*      */   }
/*      */ 
/*      */   public final DOMImplementation getImplementation()
/*      */   {
/*  609 */     return implementation;
/*      */   }
/*      */ 
/*      */   public final Element getDocumentElement()
/*      */   {
/*  622 */     int dochandle = this.dtm.getDocument();
/*  623 */     int elementhandle = -1;
/*  624 */     for (int kidhandle = this.dtm.getFirstChild(dochandle); 
/*  625 */       kidhandle != -1; 
/*  626 */       kidhandle = this.dtm.getNextSibling(kidhandle))
/*      */     {
/*  628 */       switch (this.dtm.getNodeType(kidhandle))
/*      */       {
/*      */       case 1:
/*  631 */         if (elementhandle != -1)
/*      */         {
/*  633 */           elementhandle = -1;
/*  634 */           kidhandle = this.dtm.getLastChild(dochandle);
/*      */         }
/*      */         else {
/*  637 */           elementhandle = kidhandle;
/*  638 */         }break;
/*      */       case 7:
/*      */       case 8:
/*      */       case 10:
/*  644 */         break;
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 9:
/*      */       default:
/*  647 */         elementhandle = -1;
/*  648 */         kidhandle = this.dtm.getLastChild(dochandle);
/*      */       }
/*      */     }
/*      */ 
/*  652 */     if (elementhandle == -1) {
/*  653 */       throw new DTMDOMException((short)9);
/*      */     }
/*  655 */     return (Element)this.dtm.getNode(elementhandle);
/*      */   }
/*      */ 
/*      */   public final Element createElement(String tagName)
/*      */     throws DOMException
/*      */   {
/*  670 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final DocumentFragment createDocumentFragment()
/*      */   {
/*  681 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Text createTextNode(String data)
/*      */   {
/*  694 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Comment createComment(String data)
/*      */   {
/*  707 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final CDATASection createCDATASection(String data)
/*      */     throws DOMException
/*      */   {
/*  723 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final ProcessingInstruction createProcessingInstruction(String target, String data)
/*      */     throws DOMException
/*      */   {
/*  740 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Attr createAttribute(String name)
/*      */     throws DOMException
/*      */   {
/*  755 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final EntityReference createEntityReference(String name)
/*      */     throws DOMException
/*      */   {
/*  771 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final NodeList getElementsByTagName(String tagname)
/*      */   {
/*  783 */     Vector listVector = new Vector();
/*  784 */     Node retNode = this.dtm.getNode(this.node);
/*  785 */     if (retNode != null)
/*      */     {
/*  787 */       boolean isTagNameWildCard = "*".equals(tagname);
/*  788 */       if (1 == retNode.getNodeType())
/*      */       {
/*  790 */         NodeList nodeList = retNode.getChildNodes();
/*  791 */         for (int i = 0; i < nodeList.getLength(); i++)
/*      */         {
/*  793 */           traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
/*      */         }
/*      */       }
/*  796 */       else if (9 == retNode.getNodeType()) {
/*  797 */         traverseChildren(listVector, this.dtm.getNode(this.node), tagname, isTagNameWildCard);
/*      */       }
/*      */     }
/*      */ 
/*  801 */     int size = listVector.size();
/*  802 */     NodeSet nodeSet = new NodeSet(size);
/*  803 */     for (int i = 0; i < size; i++)
/*      */     {
/*  805 */       nodeSet.addNode((DocumentFragment)listVector.elementAt(i));
/*      */     }
/*  807 */     return nodeSet;
/*      */   }
/*      */ 
/*      */   private final void traverseChildren(Vector listVector, Node tempNode, String tagname, boolean isTagNameWildCard)
/*      */   {
/*  826 */     if (tempNode == null)
/*      */     {
/*  828 */       return;
/*      */     }
/*      */ 
/*  832 */     if ((tempNode.getNodeType() == 1) && ((isTagNameWildCard) || (tempNode.getNodeName().equals(tagname))))
/*      */     {
/*  835 */       listVector.add(tempNode);
/*      */     }
/*  837 */     if (tempNode.hasChildNodes())
/*      */     {
/*  839 */       NodeList nodeList = tempNode.getChildNodes();
/*  840 */       for (int i = 0; i < nodeList.getLength(); i++)
/*      */       {
/*  842 */         traverseChildren(listVector, nodeList.item(i), tagname, isTagNameWildCard);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Node importNode(Node importedNode, boolean deep)
/*      */     throws DOMException
/*      */   {
/*  865 */     throw new DTMDOMException((short)7);
/*      */   }
/*      */ 
/*      */   public final Element createElementNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/*  882 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Attr createAttributeNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/*  899 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*      */   {
/*  914 */     Vector listVector = new Vector();
/*  915 */     Node retNode = this.dtm.getNode(this.node);
/*  916 */     if (retNode != null)
/*      */     {
/*  918 */       boolean isNamespaceURIWildCard = "*".equals(namespaceURI);
/*  919 */       boolean isLocalNameWildCard = "*".equals(localName);
/*  920 */       if (1 == retNode.getNodeType())
/*      */       {
/*  922 */         NodeList nodeList = retNode.getChildNodes();
/*  923 */         for (int i = 0; i < nodeList.getLength(); i++)
/*      */         {
/*  925 */           traverseChildren(listVector, nodeList.item(i), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
/*      */         }
/*      */       }
/*  928 */       else if (9 == retNode.getNodeType())
/*      */       {
/*  930 */         traverseChildren(listVector, this.dtm.getNode(this.node), namespaceURI, localName, isNamespaceURIWildCard, isLocalNameWildCard);
/*      */       }
/*      */     }
/*  933 */     int size = listVector.size();
/*  934 */     NodeSet nodeSet = new NodeSet(size);
/*  935 */     for (int i = 0; i < size; i++)
/*      */     {
/*  937 */       nodeSet.addNode((DocumentFragment)listVector.elementAt(i));
/*      */     }
/*  939 */     return nodeSet;
/*      */   }
/*      */ 
/*      */   private final void traverseChildren(Vector listVector, Node tempNode, String namespaceURI, String localname, boolean isNamespaceURIWildCard, boolean isLocalNameWildCard)
/*      */   {
/*  962 */     if (tempNode == null)
/*      */     {
/*  964 */       return;
/*      */     }
/*      */ 
/*  968 */     if ((tempNode.getNodeType() == 1) && ((isLocalNameWildCard) || (tempNode.getLocalName().equals(localname))))
/*      */     {
/*  972 */       String nsURI = tempNode.getNamespaceURI();
/*  973 */       if (((namespaceURI == null) && (nsURI == null)) || (isNamespaceURIWildCard) || ((namespaceURI != null) && (namespaceURI.equals(nsURI))))
/*      */       {
/*  977 */         listVector.add(tempNode);
/*      */       }
/*      */     }
/*  980 */     if (tempNode.hasChildNodes())
/*      */     {
/*  982 */       NodeList nl = tempNode.getChildNodes();
/*  983 */       for (int i = 0; i < nl.getLength(); i++)
/*      */       {
/*  985 */         traverseChildren(listVector, nl.item(i), namespaceURI, localname, isNamespaceURIWildCard, isLocalNameWildCard);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Element getElementById(String elementId)
/*      */   {
/* 1001 */     return (Element)this.dtm.getNode(this.dtm.getElementById(elementId));
/*      */   }
/*      */ 
/*      */   public final Text splitText(int offset)
/*      */     throws DOMException
/*      */   {
/* 1016 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final String getData()
/*      */     throws DOMException
/*      */   {
/* 1029 */     return this.dtm.getNodeValue(this.node);
/*      */   }
/*      */ 
/*      */   public final void setData(String data)
/*      */     throws DOMException
/*      */   {
/* 1042 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final int getLength()
/*      */   {
/* 1054 */     return this.dtm.getNodeValue(this.node).length();
/*      */   }
/*      */ 
/*      */   public final String substringData(int offset, int count)
/*      */     throws DOMException
/*      */   {
/* 1070 */     return getData().substring(offset, offset + count);
/*      */   }
/*      */ 
/*      */   public final void appendData(String arg)
/*      */     throws DOMException
/*      */   {
/* 1083 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final void insertData(int offset, String arg)
/*      */     throws DOMException
/*      */   {
/* 1097 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final void deleteData(int offset, int count)
/*      */     throws DOMException
/*      */   {
/* 1111 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final void replaceData(int offset, int count, String arg)
/*      */     throws DOMException
/*      */   {
/* 1127 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final String getTagName()
/*      */   {
/* 1138 */     return this.dtm.getNodeName(this.node);
/*      */   }
/*      */ 
/*      */   public final String getAttribute(String name)
/*      */   {
/* 1151 */     DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
/* 1152 */     Node n = map.getNamedItem(name);
/* 1153 */     return null == n ? "" : n.getNodeValue();
/*      */   }
/*      */ 
/*      */   public final void setAttribute(String name, String value)
/*      */     throws DOMException
/*      */   {
/* 1168 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final void removeAttribute(String name)
/*      */     throws DOMException
/*      */   {
/* 1181 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Attr getAttributeNode(String name)
/*      */   {
/* 1194 */     DTMNamedNodeMap map = new DTMNamedNodeMap(this.dtm, this.node);
/* 1195 */     return (Attr)map.getNamedItem(name);
/*      */   }
/*      */ 
/*      */   public final Attr setAttributeNode(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/* 1210 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Attr removeAttributeNode(Attr oldAttr)
/*      */     throws DOMException
/*      */   {
/* 1225 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes()
/*      */   {
/* 1236 */     return -1 != this.dtm.getFirstAttribute(this.node);
/*      */   }
/*      */ 
/*      */   public final void normalize()
/*      */   {
/* 1243 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final String getAttributeNS(String namespaceURI, String localName)
/*      */   {
/* 1257 */     Node retNode = null;
/* 1258 */     int n = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
/* 1259 */     if (n != -1)
/* 1260 */       retNode = this.dtm.getNode(n);
/* 1261 */     return null == retNode ? "" : retNode.getNodeValue();
/*      */   }
/*      */ 
/*      */   public final void setAttributeNS(String namespaceURI, String qualifiedName, String value)
/*      */     throws DOMException
/*      */   {
/* 1278 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final void removeAttributeNS(String namespaceURI, String localName)
/*      */     throws DOMException
/*      */   {
/* 1293 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Attr getAttributeNodeNS(String namespaceURI, String localName)
/*      */   {
/* 1307 */     Attr retAttr = null;
/* 1308 */     int n = this.dtm.getAttributeNode(this.node, namespaceURI, localName);
/* 1309 */     if (n != -1)
/* 1310 */       retAttr = (Attr)this.dtm.getNode(n);
/* 1311 */     return retAttr;
/*      */   }
/*      */ 
/*      */   public final Attr setAttributeNodeNS(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/* 1327 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final String getName()
/*      */   {
/* 1338 */     return this.dtm.getNodeName(this.node);
/*      */   }
/*      */ 
/*      */   public final boolean getSpecified()
/*      */   {
/* 1353 */     return true;
/*      */   }
/*      */ 
/*      */   public final String getValue()
/*      */   {
/* 1364 */     return this.dtm.getNodeValue(this.node);
/*      */   }
/*      */ 
/*      */   public final void setValue(String value)
/*      */   {
/* 1375 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public final Element getOwnerElement()
/*      */   {
/* 1387 */     if (getNodeType() != 2) {
/* 1388 */       return null;
/*      */     }
/*      */ 
/* 1391 */     int newnode = this.dtm.getParent(this.node);
/* 1392 */     return newnode == -1 ? null : (Element)this.dtm.getNode(newnode);
/*      */   }
/*      */ 
/*      */   public Node adoptNode(Node source)
/*      */     throws DOMException
/*      */   {
/* 1408 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public String getInputEncoding()
/*      */   {
/* 1425 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public void setEncoding(String encoding)
/*      */   {
/* 1441 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public boolean getStandalone()
/*      */   {
/* 1457 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public void setStandalone(boolean standalone)
/*      */   {
/* 1473 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public boolean getStrictErrorChecking()
/*      */   {
/* 1494 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public void setStrictErrorChecking(boolean strictErrorChecking)
/*      */   {
/* 1515 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public String getVersion()
/*      */   {
/* 1531 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/* 1547 */     throw new DTMDOMException((short)9);
/*      */   }
/*      */ 
/*      */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*      */   {
/* 1623 */     return getOwnerDocument().setUserData(key, data, handler);
/*      */   }
/*      */ 
/*      */   public Object getUserData(String key)
/*      */   {
/* 1637 */     return getOwnerDocument().getUserData(key);
/*      */   }
/*      */ 
/*      */   public Object getFeature(String feature, String version)
/*      */   {
/* 1664 */     return isSupported(feature, version) ? this : null;
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/* 1711 */     if (arg == this) {
/* 1712 */       return true;
/*      */     }
/* 1714 */     if (arg.getNodeType() != getNodeType()) {
/* 1715 */       return false;
/*      */     }
/*      */ 
/* 1719 */     if (getNodeName() == null) {
/* 1720 */       if (arg.getNodeName() != null) {
/* 1721 */         return false;
/*      */       }
/*      */     }
/* 1724 */     else if (!getNodeName().equals(arg.getNodeName())) {
/* 1725 */       return false;
/*      */     }
/*      */ 
/* 1728 */     if (getLocalName() == null) {
/* 1729 */       if (arg.getLocalName() != null) {
/* 1730 */         return false;
/*      */       }
/*      */     }
/* 1733 */     else if (!getLocalName().equals(arg.getLocalName())) {
/* 1734 */       return false;
/*      */     }
/*      */ 
/* 1737 */     if (getNamespaceURI() == null) {
/* 1738 */       if (arg.getNamespaceURI() != null) {
/* 1739 */         return false;
/*      */       }
/*      */     }
/* 1742 */     else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
/* 1743 */       return false;
/*      */     }
/*      */ 
/* 1746 */     if (getPrefix() == null) {
/* 1747 */       if (arg.getPrefix() != null) {
/* 1748 */         return false;
/*      */       }
/*      */     }
/* 1751 */     else if (!getPrefix().equals(arg.getPrefix())) {
/* 1752 */       return false;
/*      */     }
/*      */ 
/* 1755 */     if (getNodeValue() == null) {
/* 1756 */       if (arg.getNodeValue() != null) {
/* 1757 */         return false;
/*      */       }
/*      */     }
/* 1760 */     else if (!getNodeValue().equals(arg.getNodeValue())) {
/* 1761 */       return false;
/*      */     }
/*      */ 
/* 1774 */     return true;
/*      */   }
/*      */ 
/*      */   public String lookupNamespaceURI(String specifiedPrefix)
/*      */   {
/* 1788 */     short type = getNodeType();
/* 1789 */     switch (type)
/*      */     {
/*      */     case 1:
/* 1792 */       String namespace = getNamespaceURI();
/* 1793 */       String prefix = getPrefix();
/* 1794 */       if (namespace != null)
/*      */       {
/* 1796 */         if ((specifiedPrefix == null) && (prefix == specifiedPrefix))
/*      */         {
/* 1798 */           return namespace;
/* 1799 */         }if ((prefix != null) && (prefix.equals(specifiedPrefix)))
/*      */         {
/* 1801 */           return namespace;
/*      */         }
/*      */       }
/* 1804 */       if (hasAttributes()) {
/* 1805 */         NamedNodeMap map = getAttributes();
/* 1806 */         int length = map.getLength();
/* 1807 */         for (int i = 0; i < length; i++) {
/* 1808 */           Node attr = map.item(i);
/* 1809 */           String attrPrefix = attr.getPrefix();
/* 1810 */           String value = attr.getNodeValue();
/* 1811 */           namespace = attr.getNamespaceURI();
/* 1812 */           if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
/*      */           {
/* 1814 */             if ((specifiedPrefix == null) && (attr.getNodeName().equals("xmlns")))
/*      */             {
/* 1817 */               return value;
/* 1818 */             }if ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (attr.getLocalName().equals(specifiedPrefix)))
/*      */             {
/* 1822 */               return value;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1834 */       return null;
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1848 */       return null;
/*      */     case 2:
/* 1850 */       if (getOwnerElement().getNodeType() == 1) {
/* 1851 */         return getOwnerElement().lookupNamespaceURI(specifiedPrefix);
/*      */       }
/*      */ 
/* 1854 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     }
/*      */ 
/* 1863 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(String namespaceURI)
/*      */   {
/* 1941 */     return false;
/*      */   }
/*      */ 
/*      */   public String lookupPrefix(String namespaceURI)
/*      */   {
/* 1959 */     if (namespaceURI == null) {
/* 1960 */       return null;
/*      */     }
/*      */ 
/* 1963 */     short type = getNodeType();
/*      */ 
/* 1965 */     switch (type)
/*      */     {
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1982 */       return null;
/*      */     case 2:
/* 1984 */       if (getOwnerElement().getNodeType() == 1) {
/* 1985 */         return getOwnerElement().lookupPrefix(namespaceURI);
/*      */       }
/*      */ 
/* 1988 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     }
/*      */ 
/* 1997 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isSameNode(Node other)
/*      */   {
/* 2019 */     return this == other;
/*      */   }
/*      */ 
/*      */   public void setTextContent(String textContent)
/*      */     throws DOMException
/*      */   {
/* 2070 */     setNodeValue(textContent);
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/* 2119 */     return getNodeValue();
/*      */   }
/*      */ 
/*      */   public short compareDocumentPosition(Node other)
/*      */     throws DOMException
/*      */   {
/* 2132 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */   {
/* 2161 */     return null;
/*      */   }
/*      */ 
/*      */   public Node renameNode(Node n, String namespaceURI, String name)
/*      */     throws DOMException
/*      */   {
/* 2173 */     return n;
/*      */   }
/*      */ 
/*      */   public void normalizeDocument()
/*      */   {
/*      */   }
/*      */ 
/*      */   public DOMConfiguration getDomConfig()
/*      */   {
/* 2192 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentURI(String documentURI)
/*      */   {
/* 2204 */     this.fDocumentURI = documentURI;
/*      */   }
/*      */ 
/*      */   public String getDocumentURI()
/*      */   {
/* 2217 */     return this.fDocumentURI;
/*      */   }
/*      */ 
/*      */   public String getActualEncoding()
/*      */   {
/* 2232 */     return this.actualEncoding;
/*      */   }
/*      */ 
/*      */   public void setActualEncoding(String value)
/*      */   {
/* 2244 */     this.actualEncoding = value;
/*      */   }
/*      */ 
/*      */   public Text replaceWholeText(String content)
/*      */     throws DOMException
/*      */   {
/* 2295 */     return null;
/*      */   }
/*      */ 
/*      */   public String getWholeText()
/*      */   {
/* 2321 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isElementContentWhitespace()
/*      */   {
/* 2332 */     return false;
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(boolean id)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(String name, boolean makeId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNode(Attr at, boolean makeId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNS(String namespaceURI, String localName, boolean makeId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public TypeInfo getSchemaTypeInfo()
/*      */   {
/* 2375 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isId()
/*      */   {
/* 2380 */     return false;
/*      */   }
/*      */ 
/*      */   public String getXmlEncoding()
/*      */   {
/* 2387 */     return this.xmlEncoding;
/*      */   }
/*      */   public void setXmlEncoding(String xmlEncoding) {
/* 2390 */     this.xmlEncoding = xmlEncoding;
/*      */   }
/*      */ 
/*      */   public boolean getXmlStandalone()
/*      */   {
/* 2396 */     return this.xmlStandalone;
/*      */   }
/*      */ 
/*      */   public void setXmlStandalone(boolean xmlStandalone) throws DOMException
/*      */   {
/* 2401 */     this.xmlStandalone = xmlStandalone;
/*      */   }
/*      */ 
/*      */   public String getXmlVersion()
/*      */   {
/* 2407 */     return this.xmlVersion;
/*      */   }
/*      */ 
/*      */   public void setXmlVersion(String xmlVersion) throws DOMException
/*      */   {
/* 2412 */     this.xmlVersion = xmlVersion;
/*      */   }
/*      */ 
/*      */   static class DTMNodeProxyImplementation
/*      */     implements DOMImplementation
/*      */   {
/*      */     public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId)
/*      */     {
/* 1558 */       throw new DTMDOMException((short)9);
/*      */     }
/*      */ 
/*      */     public Document createDocument(String namespaceURI, String qualfiedName, DocumentType doctype)
/*      */     {
/* 1564 */       throw new DTMDOMException((short)9);
/*      */     }
/*      */ 
/*      */     public boolean hasFeature(String feature, String version)
/*      */     {
/* 1578 */       if ((("CORE".equals(feature.toUpperCase())) || ("XML".equals(feature.toUpperCase()))) && (("1.0".equals(version)) || ("2.0".equals(version))))
/*      */       {
/* 1581 */         return true;
/* 1582 */       }return false;
/*      */     }
/*      */ 
/*      */     public Object getFeature(String feature, String version)
/*      */     {
/* 1611 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy
 * JD-Core Version:    0.6.2
 */