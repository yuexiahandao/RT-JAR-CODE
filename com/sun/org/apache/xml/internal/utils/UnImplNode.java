/*      */ package com.sun.org.apache.xml.internal.utils;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import java.io.PrintStream;
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
/*      */ public class UnImplNode
/*      */   implements Node, Element, NodeList, Document
/*      */ {
/*      */   protected String fDocumentURI;
/*      */   protected String actualEncoding;
/*      */   private String xmlEncoding;
/*      */   private boolean xmlStandalone;
/*      */   private String xmlVersion;
/*      */ 
/*      */   public void error(String msg)
/*      */   {
/*   68 */     System.out.println("DOM ERROR! class: " + getClass().getName());
/*      */ 
/*   70 */     throw new RuntimeException(XMLMessages.createXMLMessage(msg, null));
/*      */   }
/*      */ 
/*      */   public void error(String msg, Object[] args)
/*      */   {
/*   82 */     System.out.println("DOM ERROR! class: " + getClass().getName());
/*      */ 
/*   84 */     throw new RuntimeException(XMLMessages.createXMLMessage(msg, args));
/*      */   }
/*      */ 
/*      */   public Node appendChild(Node newChild)
/*      */     throws DOMException
/*      */   {
/*   99 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  101 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes()
/*      */   {
/*  112 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  114 */     return false;
/*      */   }
/*      */ 
/*      */   public short getNodeType()
/*      */   {
/*  125 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  127 */     return 0;
/*      */   }
/*      */ 
/*      */   public Node getParentNode()
/*      */   {
/*  138 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  140 */     return null;
/*      */   }
/*      */ 
/*      */   public NodeList getChildNodes()
/*      */   {
/*  151 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  153 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild()
/*      */   {
/*  164 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  166 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getLastChild()
/*      */   {
/*  177 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  179 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getNextSibling()
/*      */   {
/*  190 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  192 */     return null;
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  203 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  205 */     return 0;
/*      */   }
/*      */ 
/*      */   public Node item(int index)
/*      */   {
/*  218 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  220 */     return null;
/*      */   }
/*      */ 
/*      */   public Document getOwnerDocument()
/*      */   {
/*  231 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  233 */     return null;
/*      */   }
/*      */ 
/*      */   public String getTagName()
/*      */   {
/*  244 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  246 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNodeName()
/*      */   {
/*  257 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  259 */     return null;
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*  265 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagName(String name)
/*      */   {
/*  278 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  280 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr removeAttributeNode(Attr oldAttr)
/*      */     throws DOMException
/*      */   {
/*  295 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  297 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNode(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/*  312 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  314 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasAttribute(String name)
/*      */   {
/*  328 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  330 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributeNS(String name, String x)
/*      */   {
/*  345 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  347 */     return false;
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNode(String name)
/*      */   {
/*  361 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  363 */     return null;
/*      */   }
/*      */ 
/*      */   public void removeAttribute(String name)
/*      */     throws DOMException
/*      */   {
/*  375 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void setAttribute(String name, String value)
/*      */     throws DOMException
/*      */   {
/*  388 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public String getAttribute(String name)
/*      */   {
/*  401 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  403 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes()
/*      */   {
/*  414 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  416 */     return false;
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
/*      */   {
/*  431 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  433 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNodeNS(Attr newAttr)
/*      */     throws DOMException
/*      */   {
/*  448 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  450 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNodeNS(String namespaceURI, String localName)
/*      */   {
/*  464 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  466 */     return null;
/*      */   }
/*      */ 
/*      */   public void removeAttributeNS(String namespaceURI, String localName)
/*      */     throws DOMException
/*      */   {
/*  480 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
/*      */     throws DOMException
/*      */   {
/*  496 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public String getAttributeNS(String namespaceURI, String localName)
/*      */   {
/*  510 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  512 */     return null;
/*      */   }
/*      */ 
/*      */   public Node getPreviousSibling()
/*      */   {
/*  523 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  525 */     return null;
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean deep)
/*      */   {
/*  538 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  540 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNodeValue()
/*      */     throws DOMException
/*      */   {
/*  553 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  555 */     return null;
/*      */   }
/*      */ 
/*      */   public void setNodeValue(String nodeValue)
/*      */     throws DOMException
/*      */   {
/*  567 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void setValue(String value)
/*      */     throws DOMException
/*      */   {
/*  595 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public Element getOwnerElement()
/*      */   {
/*  617 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  619 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getSpecified()
/*      */   {
/*  630 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  632 */     return false;
/*      */   }
/*      */ 
/*      */   public NamedNodeMap getAttributes()
/*      */   {
/*  643 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  645 */     return null;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node newChild, Node refChild)
/*      */     throws DOMException
/*      */   {
/*  661 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  663 */     return null;
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node newChild, Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  679 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  681 */     return null;
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node oldChild)
/*      */     throws DOMException
/*      */   {
/*  696 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  698 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/*  717 */     return false;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI()
/*      */   {
/*  728 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  730 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPrefix()
/*      */   {
/*  741 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  743 */     return null;
/*      */   }
/*      */ 
/*      */   public void setPrefix(String prefix)
/*      */     throws DOMException
/*      */   {
/*  755 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public String getLocalName()
/*      */   {
/*  766 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  768 */     return null;
/*      */   }
/*      */ 
/*      */   public DocumentType getDoctype()
/*      */   {
/*  779 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  781 */     return null;
/*      */   }
/*      */ 
/*      */   public DOMImplementation getImplementation()
/*      */   {
/*  792 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  794 */     return null;
/*      */   }
/*      */ 
/*      */   public Element getDocumentElement()
/*      */   {
/*  805 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  807 */     return null;
/*      */   }
/*      */ 
/*      */   public Element createElement(String tagName)
/*      */     throws DOMException
/*      */   {
/*  822 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  824 */     return null;
/*      */   }
/*      */ 
/*      */   public DocumentFragment createDocumentFragment()
/*      */   {
/*  835 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  837 */     return null;
/*      */   }
/*      */ 
/*      */   public Text createTextNode(String data)
/*      */   {
/*  850 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  852 */     return null;
/*      */   }
/*      */ 
/*      */   public Comment createComment(String data)
/*      */   {
/*  865 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  867 */     return null;
/*      */   }
/*      */ 
/*      */   public CDATASection createCDATASection(String data)
/*      */     throws DOMException
/*      */   {
/*  882 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  884 */     return null;
/*      */   }
/*      */ 
/*      */   public ProcessingInstruction createProcessingInstruction(String target, String data)
/*      */     throws DOMException
/*      */   {
/*  901 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  903 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr createAttribute(String name)
/*      */     throws DOMException
/*      */   {
/*  918 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  920 */     return null;
/*      */   }
/*      */ 
/*      */   public EntityReference createEntityReference(String name)
/*      */     throws DOMException
/*      */   {
/*  936 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  938 */     return null;
/*      */   }
/*      */ 
/*      */   public Node importNode(Node importedNode, boolean deep)
/*      */     throws DOMException
/*      */   {
/*  957 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  959 */     return null;
/*      */   }
/*      */ 
/*      */   public Element createElementNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/*  976 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  978 */     return null;
/*      */   }
/*      */ 
/*      */   public Attr createAttributeNS(String namespaceURI, String qualifiedName)
/*      */     throws DOMException
/*      */   {
/*  995 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/*  997 */     return null;
/*      */   }
/*      */ 
/*      */   public Element getElementById(String elementId)
/*      */   {
/* 1010 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1012 */     return null;
/*      */   }
/*      */ 
/*      */   public void setData(String data)
/*      */     throws DOMException
/*      */   {
/* 1025 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public String substringData(int offset, int count)
/*      */     throws DOMException
/*      */   {
/* 1041 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1043 */     return null;
/*      */   }
/*      */ 
/*      */   public void appendData(String arg)
/*      */     throws DOMException
/*      */   {
/* 1055 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void insertData(int offset, String arg)
/*      */     throws DOMException
/*      */   {
/* 1068 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void deleteData(int offset, int count)
/*      */     throws DOMException
/*      */   {
/* 1081 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public void replaceData(int offset, int count, String arg)
/*      */     throws DOMException
/*      */   {
/* 1096 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public Text splitText(int offset)
/*      */     throws DOMException
/*      */   {
/* 1111 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1113 */     return null;
/*      */   }
/*      */ 
/*      */   public Node adoptNode(Node source)
/*      */     throws DOMException
/*      */   {
/* 1129 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1131 */     return null;
/*      */   }
/*      */ 
/*      */   public String getInputEncoding()
/*      */   {
/* 1148 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1150 */     return null;
/*      */   }
/*      */ 
/*      */   public void setInputEncoding(String encoding)
/*      */   {
/* 1166 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public boolean getStandalone()
/*      */   {
/* 1183 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1185 */     return false;
/*      */   }
/*      */ 
/*      */   public void setStandalone(boolean standalone)
/*      */   {
/* 1201 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public boolean getStrictErrorChecking()
/*      */   {
/* 1222 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1224 */     return false;
/*      */   }
/*      */ 
/*      */   public void setStrictErrorChecking(boolean strictErrorChecking)
/*      */   {
/* 1244 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public String getVersion()
/*      */   {
/* 1261 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */ 
/* 1263 */     return null;
/*      */   }
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/* 1279 */     error("ER_FUNCTION_NOT_SUPPORTED");
/*      */   }
/*      */ 
/*      */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*      */   {
/* 1289 */     return getOwnerDocument().setUserData(key, data, handler);
/*      */   }
/*      */ 
/*      */   public Object getUserData(String key)
/*      */   {
/* 1302 */     return getOwnerDocument().getUserData(key);
/*      */   }
/*      */ 
/*      */   public Object getFeature(String feature, String version)
/*      */   {
/* 1328 */     return isSupported(feature, version) ? this : null;
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node arg)
/*      */   {
/* 1372 */     if (arg == this) {
/* 1373 */       return true;
/*      */     }
/* 1375 */     if (arg.getNodeType() != getNodeType()) {
/* 1376 */       return false;
/*      */     }
/*      */ 
/* 1380 */     if (getNodeName() == null) {
/* 1381 */       if (arg.getNodeName() != null) {
/* 1382 */         return false;
/*      */       }
/*      */     }
/* 1385 */     else if (!getNodeName().equals(arg.getNodeName())) {
/* 1386 */       return false;
/*      */     }
/*      */ 
/* 1389 */     if (getLocalName() == null) {
/* 1390 */       if (arg.getLocalName() != null) {
/* 1391 */         return false;
/*      */       }
/*      */     }
/* 1394 */     else if (!getLocalName().equals(arg.getLocalName())) {
/* 1395 */       return false;
/*      */     }
/*      */ 
/* 1398 */     if (getNamespaceURI() == null) {
/* 1399 */       if (arg.getNamespaceURI() != null) {
/* 1400 */         return false;
/*      */       }
/*      */     }
/* 1403 */     else if (!getNamespaceURI().equals(arg.getNamespaceURI())) {
/* 1404 */       return false;
/*      */     }
/*      */ 
/* 1407 */     if (getPrefix() == null) {
/* 1408 */       if (arg.getPrefix() != null) {
/* 1409 */         return false;
/*      */       }
/*      */     }
/* 1412 */     else if (!getPrefix().equals(arg.getPrefix())) {
/* 1413 */       return false;
/*      */     }
/*      */ 
/* 1416 */     if (getNodeValue() == null) {
/* 1417 */       if (arg.getNodeValue() != null) {
/* 1418 */         return false;
/*      */       }
/*      */     }
/* 1421 */     else if (!getNodeValue().equals(arg.getNodeValue())) {
/* 1422 */       return false;
/*      */     }
/*      */ 
/* 1435 */     return true;
/*      */   }
/*      */ 
/*      */   public String lookupNamespaceURI(String specifiedPrefix)
/*      */   {
/* 1448 */     short type = getNodeType();
/* 1449 */     switch (type)
/*      */     {
/*      */     case 1:
/* 1452 */       String namespace = getNamespaceURI();
/* 1453 */       String prefix = getPrefix();
/* 1454 */       if (namespace != null)
/*      */       {
/* 1456 */         if ((specifiedPrefix == null) && (prefix == specifiedPrefix))
/*      */         {
/* 1458 */           return namespace;
/* 1459 */         }if ((prefix != null) && (prefix.equals(specifiedPrefix)))
/*      */         {
/* 1461 */           return namespace;
/*      */         }
/*      */       }
/* 1464 */       if (hasAttributes()) {
/* 1465 */         NamedNodeMap map = getAttributes();
/* 1466 */         int length = map.getLength();
/* 1467 */         for (int i = 0; i < length; i++) {
/* 1468 */           Node attr = map.item(i);
/* 1469 */           String attrPrefix = attr.getPrefix();
/* 1470 */           String value = attr.getNodeValue();
/* 1471 */           namespace = attr.getNamespaceURI();
/* 1472 */           if ((namespace != null) && (namespace.equals("http://www.w3.org/2000/xmlns/")))
/*      */           {
/* 1474 */             if ((specifiedPrefix == null) && (attr.getNodeName().equals("xmlns")))
/*      */             {
/* 1477 */               return value;
/* 1478 */             }if ((attrPrefix != null) && (attrPrefix.equals("xmlns")) && (attr.getLocalName().equals(specifiedPrefix)))
/*      */             {
/* 1482 */               return value;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1494 */       return null;
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1508 */       return null;
/*      */     case 2:
/* 1510 */       if (getOwnerElement().getNodeType() == 1) {
/* 1511 */         return getOwnerElement().lookupNamespaceURI(specifiedPrefix);
/*      */       }
/*      */ 
/* 1514 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     }
/*      */ 
/* 1523 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(String namespaceURI)
/*      */   {
/* 1600 */     return false;
/*      */   }
/*      */ 
/*      */   public String lookupPrefix(String namespaceURI)
/*      */   {
/* 1617 */     if (namespaceURI == null) {
/* 1618 */       return null;
/*      */     }
/*      */ 
/* 1621 */     short type = getNodeType();
/*      */ 
/* 1623 */     switch (type)
/*      */     {
/*      */     case 6:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/* 1640 */       return null;
/*      */     case 2:
/* 1642 */       if (getOwnerElement().getNodeType() == 1) {
/* 1643 */         return getOwnerElement().lookupPrefix(namespaceURI);
/*      */       }
/*      */ 
/* 1646 */       return null;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     }
/*      */ 
/* 1655 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isSameNode(Node other)
/*      */   {
/* 1676 */     return this == other;
/*      */   }
/*      */ 
/*      */   public void setTextContent(String textContent)
/*      */     throws DOMException
/*      */   {
/* 1726 */     setNodeValue(textContent);
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/* 1774 */     return getNodeValue();
/*      */   }
/*      */ 
/*      */   public short compareDocumentPosition(Node other)
/*      */     throws DOMException
/*      */   {
/* 1786 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */   {
/* 1814 */     return null;
/*      */   }
/*      */ 
/*      */   public Node renameNode(Node n, String namespaceURI, String name)
/*      */     throws DOMException
/*      */   {
/* 1825 */     return n;
/*      */   }
/*      */ 
/*      */   public void normalizeDocument()
/*      */   {
/*      */   }
/*      */ 
/*      */   public DOMConfiguration getDomConfig()
/*      */   {
/* 1842 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentURI(String documentURI)
/*      */   {
/* 1854 */     this.fDocumentURI = documentURI;
/*      */   }
/*      */ 
/*      */   public String getDocumentURI()
/*      */   {
/* 1866 */     return this.fDocumentURI;
/*      */   }
/*      */ 
/*      */   public String getActualEncoding()
/*      */   {
/* 1881 */     return this.actualEncoding;
/*      */   }
/*      */ 
/*      */   public void setActualEncoding(String value)
/*      */   {
/* 1893 */     this.actualEncoding = value;
/*      */   }
/*      */ 
/*      */   public Text replaceWholeText(String content)
/*      */     throws DOMException
/*      */   {
/* 1943 */     return null;
/*      */   }
/*      */ 
/*      */   public String getWholeText()
/*      */   {
/* 1968 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isWhitespaceInElementContent()
/*      */   {
/* 1978 */     return false;
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
/* 2020 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isId() {
/* 2024 */     return false;
/*      */   }
/*      */ 
/*      */   public String getXmlEncoding()
/*      */   {
/* 2029 */     return this.xmlEncoding;
/*      */   }
/*      */   public void setXmlEncoding(String xmlEncoding) {
/* 2032 */     this.xmlEncoding = xmlEncoding;
/*      */   }
/*      */ 
/*      */   public boolean getXmlStandalone()
/*      */   {
/* 2037 */     return this.xmlStandalone;
/*      */   }
/*      */ 
/*      */   public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
/* 2041 */     this.xmlStandalone = xmlStandalone;
/*      */   }
/*      */ 
/*      */   public String getXmlVersion()
/*      */   {
/* 2046 */     return this.xmlVersion;
/*      */   }
/*      */ 
/*      */   public void setXmlVersion(String xmlVersion) throws DOMException {
/* 2050 */     this.xmlVersion = xmlVersion;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.UnImplNode
 * JD-Core Version:    0.6.2
 */