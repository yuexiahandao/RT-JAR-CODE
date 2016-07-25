/*      */ package javax.imageio.metadata;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ import org.w3c.dom.UserDataHandler;
/*      */ 
/*      */ public class IIOMetadataNode
/*      */   implements Element, NodeList
/*      */ {
/*  235 */   private String nodeName = null;
/*      */ 
/*  241 */   private String nodeValue = null;
/*      */ 
/*  246 */   private Object userObject = null;
/*      */ 
/*  252 */   private IIOMetadataNode parent = null;
/*      */ 
/*  257 */   private int numChildren = 0;
/*      */ 
/*  263 */   private IIOMetadataNode firstChild = null;
/*      */ 
/*  269 */   private IIOMetadataNode lastChild = null;
/*      */ 
/*  275 */   private IIOMetadataNode nextSibling = null;
/*      */ 
/*  281 */   private IIOMetadataNode previousSibling = null;
/*      */ 
/*  287 */   private List attributes = new ArrayList();
/*      */ 
/*      */   public IIOMetadataNode()
/*      */   {
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode(String paramString)
/*      */   {
/*  301 */     this.nodeName = paramString;
/*      */   }
/*      */ 
/*      */   private void checkNode(Node paramNode)
/*      */     throws DOMException
/*      */   {
/*  309 */     if (paramNode == null) {
/*  310 */       return;
/*      */     }
/*  312 */     if (!(paramNode instanceof IIOMetadataNode))
/*  313 */       throw new IIODOMException((short)4, "Node not an IIOMetadataNode!");
/*      */   }
/*      */ 
/*      */   public String getNodeName()
/*      */   {
/*  326 */     return this.nodeName;
/*      */   }
/*      */ 
/*      */   public String getNodeValue()
/*      */   {
/*  335 */     return this.nodeValue;
/*      */   }
/*      */ 
/*      */   public void setNodeValue(String paramString)
/*      */   {
/*  342 */     this.nodeValue = paramString;
/*      */   }
/*      */ 
/*      */   public short getNodeType()
/*      */   {
/*  352 */     return 1;
/*      */   }
/*      */ 
/*      */   public Node getParentNode()
/*      */   {
/*  369 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public NodeList getChildNodes()
/*      */   {
/*  380 */     return this;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild()
/*      */   {
/*  391 */     return this.firstChild;
/*      */   }
/*      */ 
/*      */   public Node getLastChild()
/*      */   {
/*  402 */     return this.lastChild;
/*      */   }
/*      */ 
/*      */   public Node getPreviousSibling()
/*      */   {
/*  413 */     return this.previousSibling;
/*      */   }
/*      */ 
/*      */   public Node getNextSibling()
/*      */   {
/*  424 */     return this.nextSibling;
/*      */   }
/*      */ 
/*      */   public NamedNodeMap getAttributes()
/*      */   {
/*  435 */     return new IIONamedNodeMap(this.attributes);
/*      */   }
/*      */ 
/*      */   public Document getOwnerDocument()
/*      */   {
/*  445 */     return null;
/*      */   }
/*      */ 
/*      */   public Node insertBefore(Node paramNode1, Node paramNode2)
/*      */   {
/*  464 */     if (paramNode1 == null) {
/*  465 */       throw new IllegalArgumentException("newChild == null!");
/*      */     }
/*      */ 
/*  468 */     checkNode(paramNode1);
/*  469 */     checkNode(paramNode2);
/*      */ 
/*  471 */     IIOMetadataNode localIIOMetadataNode1 = (IIOMetadataNode)paramNode1;
/*  472 */     IIOMetadataNode localIIOMetadataNode2 = (IIOMetadataNode)paramNode2;
/*      */ 
/*  475 */     IIOMetadataNode localIIOMetadataNode3 = null;
/*  476 */     IIOMetadataNode localIIOMetadataNode4 = null;
/*      */ 
/*  478 */     if (paramNode2 == null) {
/*  479 */       localIIOMetadataNode3 = this.lastChild;
/*  480 */       localIIOMetadataNode4 = null;
/*  481 */       this.lastChild = localIIOMetadataNode1;
/*      */     } else {
/*  483 */       localIIOMetadataNode3 = localIIOMetadataNode2.previousSibling;
/*  484 */       localIIOMetadataNode4 = localIIOMetadataNode2;
/*      */     }
/*      */ 
/*  487 */     if (localIIOMetadataNode3 != null) {
/*  488 */       localIIOMetadataNode3.nextSibling = localIIOMetadataNode1;
/*      */     }
/*  490 */     if (localIIOMetadataNode4 != null) {
/*  491 */       localIIOMetadataNode4.previousSibling = localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  494 */     localIIOMetadataNode1.parent = this;
/*  495 */     localIIOMetadataNode1.previousSibling = localIIOMetadataNode3;
/*  496 */     localIIOMetadataNode1.nextSibling = localIIOMetadataNode4;
/*      */ 
/*  499 */     if (this.firstChild == localIIOMetadataNode2) {
/*  500 */       this.firstChild = localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  503 */     this.numChildren += 1;
/*  504 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public Node replaceChild(Node paramNode1, Node paramNode2)
/*      */   {
/*  522 */     if (paramNode1 == null) {
/*  523 */       throw new IllegalArgumentException("newChild == null!");
/*      */     }
/*      */ 
/*  526 */     checkNode(paramNode1);
/*  527 */     checkNode(paramNode2);
/*      */ 
/*  529 */     IIOMetadataNode localIIOMetadataNode1 = (IIOMetadataNode)paramNode1;
/*  530 */     IIOMetadataNode localIIOMetadataNode2 = (IIOMetadataNode)paramNode2;
/*      */ 
/*  532 */     IIOMetadataNode localIIOMetadataNode3 = localIIOMetadataNode2.previousSibling;
/*  533 */     IIOMetadataNode localIIOMetadataNode4 = localIIOMetadataNode2.nextSibling;
/*      */ 
/*  535 */     if (localIIOMetadataNode3 != null) {
/*  536 */       localIIOMetadataNode3.nextSibling = localIIOMetadataNode1;
/*      */     }
/*  538 */     if (localIIOMetadataNode4 != null) {
/*  539 */       localIIOMetadataNode4.previousSibling = localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  542 */     localIIOMetadataNode1.parent = this;
/*  543 */     localIIOMetadataNode1.previousSibling = localIIOMetadataNode3;
/*  544 */     localIIOMetadataNode1.nextSibling = localIIOMetadataNode4;
/*      */ 
/*  546 */     if (this.firstChild == localIIOMetadataNode2) {
/*  547 */       this.firstChild = localIIOMetadataNode1;
/*      */     }
/*  549 */     if (this.lastChild == localIIOMetadataNode2) {
/*  550 */       this.lastChild = localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  553 */     localIIOMetadataNode2.parent = null;
/*  554 */     localIIOMetadataNode2.previousSibling = null;
/*  555 */     localIIOMetadataNode2.nextSibling = null;
/*      */ 
/*  557 */     return localIIOMetadataNode2;
/*      */   }
/*      */ 
/*      */   public Node removeChild(Node paramNode)
/*      */   {
/*  572 */     if (paramNode == null) {
/*  573 */       throw new IllegalArgumentException("oldChild == null!");
/*      */     }
/*  575 */     checkNode(paramNode);
/*      */ 
/*  577 */     IIOMetadataNode localIIOMetadataNode1 = (IIOMetadataNode)paramNode;
/*      */ 
/*  579 */     IIOMetadataNode localIIOMetadataNode2 = localIIOMetadataNode1.previousSibling;
/*  580 */     IIOMetadataNode localIIOMetadataNode3 = localIIOMetadataNode1.nextSibling;
/*      */ 
/*  582 */     if (localIIOMetadataNode2 != null) {
/*  583 */       localIIOMetadataNode2.nextSibling = localIIOMetadataNode3;
/*      */     }
/*  585 */     if (localIIOMetadataNode3 != null) {
/*  586 */       localIIOMetadataNode3.previousSibling = localIIOMetadataNode2;
/*      */     }
/*      */ 
/*  589 */     if (this.firstChild == localIIOMetadataNode1) {
/*  590 */       this.firstChild = localIIOMetadataNode3;
/*      */     }
/*  592 */     if (this.lastChild == localIIOMetadataNode1) {
/*  593 */       this.lastChild = localIIOMetadataNode2;
/*      */     }
/*      */ 
/*  596 */     localIIOMetadataNode1.parent = null;
/*  597 */     localIIOMetadataNode1.previousSibling = null;
/*  598 */     localIIOMetadataNode1.nextSibling = null;
/*      */ 
/*  600 */     this.numChildren -= 1;
/*  601 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public Node appendChild(Node paramNode)
/*      */   {
/*  616 */     if (paramNode == null) {
/*  617 */       throw new IllegalArgumentException("newChild == null!");
/*      */     }
/*  619 */     checkNode(paramNode);
/*      */ 
/*  622 */     return insertBefore(paramNode, null);
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes()
/*      */   {
/*  631 */     return this.numChildren > 0;
/*      */   }
/*      */ 
/*      */   public Node cloneNode(boolean paramBoolean)
/*      */   {
/*  649 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode(this.nodeName);
/*  650 */     localIIOMetadataNode1.setUserObject(getUserObject());
/*      */ 
/*  653 */     if (paramBoolean) {
/*  654 */       for (IIOMetadataNode localIIOMetadataNode2 = this.firstChild; 
/*  655 */         localIIOMetadataNode2 != null; 
/*  656 */         localIIOMetadataNode2 = localIIOMetadataNode2.nextSibling) {
/*  657 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode2.cloneNode(true));
/*      */       }
/*      */     }
/*      */ 
/*  661 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public void normalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String paramString1, String paramString2)
/*      */   {
/*  681 */     return false;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI()
/*      */     throws DOMException
/*      */   {
/*  688 */     return null;
/*      */   }
/*      */ 
/*      */   public String getPrefix()
/*      */   {
/*  699 */     return null;
/*      */   }
/*      */ 
/*      */   public void setPrefix(String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getLocalName()
/*      */   {
/*  718 */     return this.nodeName;
/*      */   }
/*      */ 
/*      */   public String getTagName()
/*      */   {
/*  730 */     return this.nodeName;
/*      */   }
/*      */ 
/*      */   public String getAttribute(String paramString)
/*      */   {
/*  740 */     Attr localAttr = getAttributeNode(paramString);
/*  741 */     if (localAttr == null) {
/*  742 */       return "";
/*      */     }
/*  744 */     return localAttr.getValue();
/*      */   }
/*      */ 
/*      */   public String getAttributeNS(String paramString1, String paramString2)
/*      */   {
/*  753 */     return getAttribute(paramString2);
/*      */   }
/*      */ 
/*      */   public void setAttribute(String paramString1, String paramString2)
/*      */   {
/*  758 */     int i = 1;
/*  759 */     char[] arrayOfChar = paramString1.toCharArray();
/*  760 */     for (int j = 0; j < arrayOfChar.length; j++) {
/*  761 */       if (arrayOfChar[j] >= 65534) {
/*  762 */         i = 0;
/*  763 */         break;
/*      */       }
/*      */     }
/*  766 */     if (i == 0) {
/*  767 */       throw new IIODOMException((short)5, "Attribute name is illegal!");
/*      */     }
/*      */ 
/*  770 */     removeAttribute(paramString1, false);
/*  771 */     this.attributes.add(new IIOAttr(this, paramString1, paramString2));
/*      */   }
/*      */ 
/*      */   public void setAttributeNS(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  781 */     setAttribute(paramString2, paramString3);
/*      */   }
/*      */ 
/*      */   public void removeAttribute(String paramString) {
/*  785 */     removeAttribute(paramString, true);
/*      */   }
/*      */ 
/*      */   private void removeAttribute(String paramString, boolean paramBoolean) {
/*  789 */     int i = this.attributes.size();
/*  790 */     for (int j = 0; j < i; j++) {
/*  791 */       IIOAttr localIIOAttr = (IIOAttr)this.attributes.get(j);
/*  792 */       if (paramString.equals(localIIOAttr.getName())) {
/*  793 */         localIIOAttr.setOwnerElement(null);
/*  794 */         this.attributes.remove(j);
/*  795 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  800 */     if (paramBoolean)
/*  801 */       throw new IIODOMException((short)8, "No such attribute!");
/*      */   }
/*      */ 
/*      */   public void removeAttributeNS(String paramString1, String paramString2)
/*      */   {
/*  811 */     removeAttribute(paramString2);
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNode(String paramString) {
/*  815 */     Node localNode = getAttributes().getNamedItem(paramString);
/*  816 */     return (Attr)localNode;
/*      */   }
/*      */ 
/*      */   public Attr getAttributeNodeNS(String paramString1, String paramString2)
/*      */   {
/*  826 */     return getAttributeNode(paramString2);
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNode(Attr paramAttr) throws DOMException {
/*  830 */     Element localElement = paramAttr.getOwnerElement();
/*  831 */     if (localElement != null) {
/*  832 */       if (localElement == this) {
/*  833 */         return null;
/*      */       }
/*  835 */       throw new DOMException((short)10, "Attribute is already in use");
/*      */     }
/*      */     IIOAttr localIIOAttr;
/*  841 */     if ((paramAttr instanceof IIOAttr)) {
/*  842 */       localIIOAttr = (IIOAttr)paramAttr;
/*  843 */       localIIOAttr.setOwnerElement(this);
/*      */     } else {
/*  845 */       localIIOAttr = new IIOAttr(this, paramAttr.getName(), paramAttr.getValue());
/*      */     }
/*      */ 
/*  850 */     Attr localAttr = getAttributeNode(localIIOAttr.getName());
/*  851 */     if (localAttr != null) {
/*  852 */       removeAttributeNode(localAttr);
/*      */     }
/*      */ 
/*  855 */     this.attributes.add(localIIOAttr);
/*      */ 
/*  857 */     return localAttr;
/*      */   }
/*      */ 
/*      */   public Attr setAttributeNodeNS(Attr paramAttr)
/*      */   {
/*  866 */     return setAttributeNode(paramAttr);
/*      */   }
/*      */ 
/*      */   public Attr removeAttributeNode(Attr paramAttr) {
/*  870 */     removeAttribute(paramAttr.getName());
/*  871 */     return paramAttr;
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagName(String paramString) {
/*  875 */     ArrayList localArrayList = new ArrayList();
/*  876 */     getElementsByTagName(paramString, localArrayList);
/*  877 */     return new IIONodeList(localArrayList);
/*      */   }
/*      */ 
/*      */   private void getElementsByTagName(String paramString, List paramList) {
/*  881 */     if (this.nodeName.equals(paramString)) {
/*  882 */       paramList.add(this);
/*      */     }
/*      */ 
/*  885 */     Node localNode = getFirstChild();
/*  886 */     while (localNode != null) {
/*  887 */       ((IIOMetadataNode)localNode).getElementsByTagName(paramString, paramList);
/*  888 */       localNode = localNode.getNextSibling();
/*      */     }
/*      */   }
/*      */ 
/*      */   public NodeList getElementsByTagNameNS(String paramString1, String paramString2)
/*      */   {
/*  897 */     return getElementsByTagName(paramString2);
/*      */   }
/*      */ 
/*      */   public boolean hasAttributes() {
/*  901 */     return this.attributes.size() > 0;
/*      */   }
/*      */ 
/*      */   public boolean hasAttribute(String paramString) {
/*  905 */     return getAttributeNode(paramString) != null;
/*      */   }
/*      */ 
/*      */   public boolean hasAttributeNS(String paramString1, String paramString2)
/*      */   {
/*  913 */     return hasAttribute(paramString2);
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  919 */     return this.numChildren;
/*      */   }
/*      */ 
/*      */   public Node item(int paramInt) {
/*  923 */     if (paramInt < 0) {
/*  924 */       return null;
/*      */     }
/*      */ 
/*  927 */     Node localNode = getFirstChild();
/*  928 */     while ((localNode != null) && (paramInt-- > 0)) {
/*  929 */       localNode = localNode.getNextSibling();
/*      */     }
/*  931 */     return localNode;
/*      */   }
/*      */ 
/*      */   public Object getUserObject()
/*      */   {
/*  942 */     return this.userObject;
/*      */   }
/*      */ 
/*      */   public void setUserObject(Object paramObject)
/*      */   {
/*  953 */     this.userObject = paramObject;
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(String paramString, boolean paramBoolean)
/*      */     throws DOMException
/*      */   {
/*  966 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNS(String paramString1, String paramString2, boolean paramBoolean)
/*      */     throws DOMException
/*      */   {
/*  979 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNode(Attr paramAttr, boolean paramBoolean)
/*      */     throws DOMException
/*      */   {
/*  991 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public TypeInfo getSchemaTypeInfo()
/*      */     throws DOMException
/*      */   {
/* 1001 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public Object setUserData(String paramString, Object paramObject, UserDataHandler paramUserDataHandler)
/*      */     throws DOMException
/*      */   {
/* 1013 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public Object getUserData(String paramString)
/*      */     throws DOMException
/*      */   {
/* 1023 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public Object getFeature(String paramString1, String paramString2)
/*      */     throws DOMException
/*      */   {
/* 1034 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public boolean isSameNode(Node paramNode)
/*      */     throws DOMException
/*      */   {
/* 1044 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public boolean isEqualNode(Node paramNode)
/*      */     throws DOMException
/*      */   {
/* 1054 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public String lookupNamespaceURI(String paramString)
/*      */     throws DOMException
/*      */   {
/* 1064 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public boolean isDefaultNamespace(String paramString)
/*      */     throws DOMException
/*      */   {
/* 1075 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public String lookupPrefix(String paramString)
/*      */     throws DOMException
/*      */   {
/* 1085 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public String getTextContent()
/*      */     throws DOMException
/*      */   {
/* 1095 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public void setTextContent(String paramString)
/*      */     throws DOMException
/*      */   {
/* 1105 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public short compareDocumentPosition(Node paramNode)
/*      */     throws DOMException
/*      */   {
/* 1116 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ 
/*      */   public String getBaseURI()
/*      */     throws DOMException
/*      */   {
/* 1126 */     throw new DOMException((short)9, "Method not supported");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOMetadataNode
 * JD-Core Version:    0.6.2
 */