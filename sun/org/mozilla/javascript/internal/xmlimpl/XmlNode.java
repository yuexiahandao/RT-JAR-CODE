/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ class XmlNode
/*     */ {
/*     */   private static final String XML_NAMESPACES_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
/*  57 */   private static final String USER_DATA_XMLNODE_KEY = XmlNode.class.getName();
/*     */   private static final boolean DOM_LEVEL_3 = true;
/*     */   private static final long serialVersionUID = 1L;
/* 131 */   private UserDataHandler events = new XmlNodeUserDataHandler();
/*     */   private Node dom;
/*     */   private XML xml;
/*     */ 
/*     */   private static XmlNode getUserData(Node paramNode)
/*     */   {
/*  63 */     return (XmlNode)paramNode.getUserData(USER_DATA_XMLNODE_KEY);
/*     */   }
/*     */ 
/*     */   private static void setUserData(Node paramNode, XmlNode paramXmlNode)
/*     */   {
/*  70 */     paramNode.setUserData(USER_DATA_XMLNODE_KEY, paramXmlNode, paramXmlNode.events);
/*     */   }
/*     */ 
/*     */   private static XmlNode createImpl(Node paramNode)
/*     */   {
/*  75 */     if ((paramNode instanceof Document)) throw new IllegalArgumentException();
/*  76 */     XmlNode localXmlNode = null;
/*  77 */     if (getUserData(paramNode) == null) {
/*  78 */       localXmlNode = new XmlNode();
/*  79 */       localXmlNode.dom = paramNode;
/*  80 */       setUserData(paramNode, localXmlNode);
/*     */     } else {
/*  82 */       localXmlNode = getUserData(paramNode);
/*     */     }
/*  84 */     return localXmlNode;
/*     */   }
/*     */ 
/*     */   static XmlNode newElementWithText(XmlProcessor paramXmlProcessor, XmlNode paramXmlNode, QName paramQName, String paramString) {
/*  88 */     if ((paramXmlNode instanceof Document)) throw new IllegalArgumentException("Cannot use Document node as reference");
/*  89 */     Document localDocument = null;
/*  90 */     if (paramXmlNode != null)
/*  91 */       localDocument = paramXmlNode.dom.getOwnerDocument();
/*     */     else {
/*  93 */       localDocument = paramXmlProcessor.newDocument();
/*     */     }
/*  95 */     Node localNode = paramXmlNode != null ? paramXmlNode.dom : null;
/*  96 */     Namespace localNamespace = paramQName.getNamespace();
/*  97 */     Element localElement = (localNamespace == null) || (localNamespace.getUri().length() == 0) ? localDocument.createElementNS(null, paramQName.getLocalName()) : localDocument.createElementNS(localNamespace.getUri(), paramQName.qualify(localNode));
/*     */ 
/* 101 */     if (paramString != null) {
/* 102 */       localElement.appendChild(localDocument.createTextNode(paramString));
/*     */     }
/* 104 */     return createImpl(localElement);
/*     */   }
/*     */ 
/*     */   static XmlNode createText(XmlProcessor paramXmlProcessor, String paramString) {
/* 108 */     return createImpl(paramXmlProcessor.newDocument().createTextNode(paramString));
/*     */   }
/*     */ 
/*     */   static XmlNode createElementFromNode(Node paramNode) {
/* 112 */     if ((paramNode instanceof Document))
/* 113 */       paramNode = ((Document)paramNode).getDocumentElement();
/* 114 */     return createImpl(paramNode);
/*     */   }
/*     */ 
/*     */   static XmlNode createElement(XmlProcessor paramXmlProcessor, String paramString1, String paramString2) throws SAXException {
/* 118 */     return createImpl(paramXmlProcessor.toXml(paramString1, paramString2));
/*     */   }
/*     */ 
/*     */   static XmlNode createEmpty(XmlProcessor paramXmlProcessor) {
/* 122 */     return createText(paramXmlProcessor, "");
/*     */   }
/*     */ 
/*     */   private static XmlNode copy(XmlNode paramXmlNode) {
/* 126 */     return createImpl(paramXmlNode.dom.cloneNode(true));
/*     */   }
/*     */ 
/*     */   String debug()
/*     */   {
/* 141 */     XmlProcessor localXmlProcessor = new XmlProcessor();
/* 142 */     localXmlProcessor.setIgnoreComments(false);
/* 143 */     localXmlProcessor.setIgnoreProcessingInstructions(false);
/* 144 */     localXmlProcessor.setIgnoreWhitespace(false);
/* 145 */     localXmlProcessor.setPrettyPrinting(false);
/* 146 */     return localXmlProcessor.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     return "XmlNode: type=" + this.dom.getNodeType() + " dom=" + this.dom.toString();
/*     */   }
/*     */ 
/*     */   XML getXml() {
/* 155 */     return this.xml;
/*     */   }
/*     */ 
/*     */   void setXml(XML paramXML) {
/* 159 */     this.xml = paramXML;
/*     */   }
/*     */ 
/*     */   int getChildCount() {
/* 163 */     return this.dom.getChildNodes().getLength();
/*     */   }
/*     */ 
/*     */   XmlNode parent() {
/* 167 */     Node localNode = this.dom.getParentNode();
/* 168 */     if ((localNode instanceof Document)) return null;
/* 169 */     if (localNode == null) return null;
/* 170 */     return createImpl(localNode);
/*     */   }
/*     */ 
/*     */   int getChildIndex() {
/* 174 */     if (isAttributeType()) return -1;
/* 175 */     if (parent() == null) return -1;
/* 176 */     NodeList localNodeList = this.dom.getParentNode().getChildNodes();
/* 177 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 178 */       if (localNodeList.item(i) == this.dom) {
/* 179 */         return i;
/*     */       }
/*     */     }
/*     */ 
/* 183 */     throw new RuntimeException("Unreachable.");
/*     */   }
/*     */ 
/*     */   void removeChild(int paramInt) {
/* 187 */     this.dom.removeChild(this.dom.getChildNodes().item(paramInt));
/*     */   }
/*     */ 
/*     */   String toXmlString(XmlProcessor paramXmlProcessor) {
/* 191 */     return paramXmlProcessor.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */   String ecmaValue()
/*     */   {
/* 196 */     if (isTextType())
/* 197 */       return ((Text)this.dom).getData();
/* 198 */     if (isAttributeType())
/* 199 */       return ((Attr)this.dom).getValue();
/* 200 */     if (isProcessingInstructionType())
/* 201 */       return ((ProcessingInstruction)this.dom).getData();
/* 202 */     if (isCommentType())
/* 203 */       return ((Comment)this.dom).getNodeValue();
/* 204 */     if (isElementType()) {
/* 205 */       throw new RuntimeException("Unimplemented ecmaValue() for elements.");
/*     */     }
/* 207 */     throw new RuntimeException("Unimplemented for node " + this.dom);
/*     */   }
/*     */ 
/*     */   void deleteMe()
/*     */   {
/* 212 */     if ((this.dom instanceof Attr)) {
/* 213 */       Attr localAttr = (Attr)this.dom;
/* 214 */       localAttr.getOwnerElement().getAttributes().removeNamedItemNS(localAttr.getNamespaceURI(), localAttr.getLocalName());
/*     */     }
/* 216 */     else if (this.dom.getParentNode() != null) {
/* 217 */       this.dom.getParentNode().removeChild(this.dom);
/*     */     }
/*     */   }
/*     */ 
/*     */   void normalize()
/*     */   {
/* 226 */     this.dom.normalize();
/*     */   }
/*     */ 
/*     */   void insertChildAt(int paramInt, XmlNode paramXmlNode) {
/* 230 */     Node localNode1 = this.dom;
/* 231 */     Node localNode2 = localNode1.getOwnerDocument().importNode(paramXmlNode.dom, true);
/* 232 */     if (localNode1.getChildNodes().getLength() < paramInt)
/*     */     {
/* 234 */       throw new IllegalArgumentException("index=" + paramInt + " length=" + localNode1.getChildNodes().getLength());
/*     */     }
/* 236 */     if (localNode1.getChildNodes().getLength() == paramInt)
/* 237 */       localNode1.appendChild(localNode2);
/*     */     else
/* 239 */       localNode1.insertBefore(localNode2, localNode1.getChildNodes().item(paramInt));
/*     */   }
/*     */ 
/*     */   void insertChildrenAt(int paramInt, XmlNode[] paramArrayOfXmlNode)
/*     */   {
/* 244 */     for (int i = 0; i < paramArrayOfXmlNode.length; i++)
/* 245 */       insertChildAt(paramInt + i, paramArrayOfXmlNode[i]);
/*     */   }
/*     */ 
/*     */   XmlNode getChild(int paramInt)
/*     */   {
/* 250 */     Node localNode = this.dom.getChildNodes().item(paramInt);
/* 251 */     return createImpl(localNode);
/*     */   }
/*     */ 
/*     */   boolean hasChildElement()
/*     */   {
/* 256 */     NodeList localNodeList = this.dom.getChildNodes();
/* 257 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 258 */       if (localNodeList.item(i).getNodeType() == 1) return true;
/*     */     }
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */   boolean isSameNode(XmlNode paramXmlNode)
/*     */   {
/* 265 */     return this.dom == paramXmlNode.dom;
/*     */   }
/*     */ 
/*     */   private String toUri(String paramString) {
/* 269 */     return paramString == null ? "" : paramString;
/*     */   }
/*     */ 
/*     */   private void addNamespaces(Namespaces paramNamespaces, Element paramElement) {
/* 273 */     if (paramElement == null) throw new RuntimeException("element must not be null");
/* 274 */     String str1 = toUri(paramElement.lookupNamespaceURI(null));
/* 275 */     String str2 = "";
/* 276 */     if (paramElement.getParentNode() != null) {
/* 277 */       str2 = toUri(paramElement.getParentNode().lookupNamespaceURI(null));
/*     */     }
/* 279 */     if ((!str1.equals(str2)) || (!(paramElement.getParentNode() instanceof Element))) {
/* 280 */       paramNamespaces.declare(Namespace.create("", str1));
/*     */     }
/* 282 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 283 */     for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
/* 284 */       Attr localAttr = (Attr)localNamedNodeMap.item(i);
/* 285 */       if ((localAttr.getPrefix() != null) && (localAttr.getPrefix().equals("xmlns")))
/* 286 */         paramNamespaces.declare(Namespace.create(localAttr.getLocalName(), localAttr.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Namespaces getAllNamespaces()
/*     */   {
/* 292 */     Namespaces localNamespaces = new Namespaces();
/*     */ 
/* 294 */     Object localObject = this.dom;
/* 295 */     if ((localObject instanceof Attr)) {
/* 296 */       localObject = ((Attr)localObject).getOwnerElement();
/*     */     }
/* 298 */     while (localObject != null) {
/* 299 */       if ((localObject instanceof Element)) {
/* 300 */         addNamespaces(localNamespaces, (Element)localObject);
/*     */       }
/* 302 */       localObject = ((Node)localObject).getParentNode();
/*     */     }
/*     */ 
/* 305 */     localNamespaces.declare(Namespace.create("", ""));
/* 306 */     return localNamespaces;
/*     */   }
/*     */ 
/*     */   Namespace[] getInScopeNamespaces() {
/* 310 */     Namespaces localNamespaces = getAllNamespaces();
/* 311 */     return localNamespaces.getNamespaces();
/*     */   }
/*     */ 
/*     */   Namespace[] getNamespaceDeclarations()
/*     */   {
/* 316 */     if ((this.dom instanceof Element)) {
/* 317 */       Namespaces localNamespaces = new Namespaces();
/* 318 */       addNamespaces(localNamespaces, (Element)this.dom);
/* 319 */       return localNamespaces.getNamespaces();
/*     */     }
/* 321 */     return new Namespace[0];
/*     */   }
/*     */ 
/*     */   Namespace getNamespaceDeclaration(String paramString)
/*     */   {
/* 326 */     if ((paramString.equals("")) && ((this.dom instanceof Attr)))
/*     */     {
/* 328 */       return Namespace.create("", "");
/*     */     }
/* 330 */     Namespaces localNamespaces = getAllNamespaces();
/* 331 */     return localNamespaces.getNamespace(paramString);
/*     */   }
/*     */ 
/*     */   Namespace getNamespaceDeclaration() {
/* 335 */     if (this.dom.getPrefix() == null) return getNamespaceDeclaration("");
/* 336 */     return getNamespaceDeclaration(this.dom.getPrefix());
/*     */   }
/*     */ 
/*     */   final XmlNode copy()
/*     */   {
/* 388 */     return copy(this);
/*     */   }
/*     */ 
/*     */   final boolean isParentType()
/*     */   {
/* 393 */     return isElementType();
/*     */   }
/*     */ 
/*     */   final boolean isTextType() {
/* 397 */     return (this.dom.getNodeType() == 3) || (this.dom.getNodeType() == 4);
/*     */   }
/*     */ 
/*     */   final boolean isAttributeType() {
/* 401 */     return this.dom.getNodeType() == 2;
/*     */   }
/*     */ 
/*     */   final boolean isProcessingInstructionType() {
/* 405 */     return this.dom.getNodeType() == 7;
/*     */   }
/*     */ 
/*     */   final boolean isCommentType() {
/* 409 */     return this.dom.getNodeType() == 8;
/*     */   }
/*     */ 
/*     */   final boolean isElementType() {
/* 413 */     return this.dom.getNodeType() == 1;
/*     */   }
/*     */ 
/*     */   final void renameNode(QName paramQName) {
/* 417 */     this.dom = this.dom.getOwnerDocument().renameNode(this.dom, paramQName.getNamespace().getUri(), paramQName.qualify(this.dom));
/*     */   }
/*     */ 
/*     */   void invalidateNamespacePrefix() {
/* 421 */     if (!(this.dom instanceof Element)) throw new IllegalStateException();
/* 422 */     String str = this.dom.getPrefix();
/* 423 */     QName localQName = QName.create(this.dom.getNamespaceURI(), this.dom.getLocalName(), null);
/* 424 */     renameNode(localQName);
/* 425 */     NamedNodeMap localNamedNodeMap = this.dom.getAttributes();
/* 426 */     for (int i = 0; i < localNamedNodeMap.getLength(); i++)
/* 427 */       if (localNamedNodeMap.item(i).getPrefix().equals(str))
/* 428 */         createImpl(localNamedNodeMap.item(i)).renameNode(QName.create(localNamedNodeMap.item(i).getNamespaceURI(), localNamedNodeMap.item(i).getLocalName(), null));
/*     */   }
/*     */ 
/*     */   private void declareNamespace(Element paramElement, String paramString1, String paramString2)
/*     */   {
/* 434 */     if (paramString1.length() > 0)
/* 435 */       paramElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + paramString1, paramString2);
/*     */     else
/* 437 */       paramElement.setAttribute("xmlns", paramString2);
/*     */   }
/*     */ 
/*     */   void declareNamespace(String paramString1, String paramString2)
/*     */   {
/* 442 */     if (!(this.dom instanceof Element)) throw new IllegalStateException();
/* 443 */     if ((this.dom.lookupNamespaceURI(paramString2) == null) || (!this.dom.lookupNamespaceURI(paramString2).equals(paramString1)))
/*     */     {
/* 446 */       Element localElement = (Element)this.dom;
/* 447 */       declareNamespace(localElement, paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Namespace getDefaultNamespace() {
/* 452 */     String str1 = "";
/* 453 */     String str2 = this.dom.lookupNamespaceURI(null) == null ? "" : this.dom.lookupNamespaceURI(null);
/* 454 */     return Namespace.create(str1, str2);
/*     */   }
/*     */ 
/*     */   private String getExistingPrefixFor(Namespace paramNamespace) {
/* 458 */     if (getDefaultNamespace().getUri().equals(paramNamespace.getUri())) {
/* 459 */       return "";
/*     */     }
/* 461 */     return this.dom.lookupPrefix(paramNamespace.getUri());
/*     */   }
/*     */ 
/*     */   private Namespace getNodeNamespace() {
/* 465 */     String str1 = this.dom.getNamespaceURI();
/* 466 */     String str2 = this.dom.getPrefix();
/* 467 */     if (str1 == null) str1 = "";
/* 468 */     if (str2 == null) str2 = "";
/* 469 */     return Namespace.create(str2, str1);
/*     */   }
/*     */ 
/*     */   Namespace getNamespace() {
/* 473 */     return getNodeNamespace();
/*     */   }
/*     */ 
/*     */   void removeNamespace(Namespace paramNamespace) {
/* 477 */     Namespace localNamespace = getNodeNamespace();
/*     */ 
/* 480 */     if (paramNamespace.is(localNamespace)) return;
/* 481 */     NamedNodeMap localNamedNodeMap = this.dom.getAttributes();
/* 482 */     for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
/* 483 */       XmlNode localXmlNode = createImpl(localNamedNodeMap.item(i));
/* 484 */       if (paramNamespace.is(localXmlNode.getNodeNamespace())) return;
/*     */ 
/*     */     }
/*     */ 
/* 488 */     String str = getExistingPrefixFor(paramNamespace);
/* 489 */     if (str != null)
/* 490 */       if (paramNamespace.isUnspecifiedPrefix())
/*     */       {
/* 493 */         declareNamespace(str, getDefaultNamespace().getUri());
/*     */       }
/* 495 */       else if (str.equals(paramNamespace.getPrefix()))
/* 496 */         declareNamespace(str, getDefaultNamespace().getUri());
/*     */   }
/*     */ 
/*     */   private void setProcessingInstructionName(String paramString)
/*     */   {
/* 505 */     ProcessingInstruction localProcessingInstruction = (ProcessingInstruction)this.dom;
/*     */ 
/* 507 */     localProcessingInstruction.getParentNode().replaceChild(localProcessingInstruction, localProcessingInstruction.getOwnerDocument().createProcessingInstruction(paramString, localProcessingInstruction.getData()));
/*     */   }
/*     */ 
/*     */   final void setLocalName(String paramString)
/*     */   {
/* 514 */     if ((this.dom instanceof ProcessingInstruction)) {
/* 515 */       setProcessingInstructionName(paramString);
/*     */     } else {
/* 517 */       String str = this.dom.getPrefix();
/* 518 */       if (str == null) str = "";
/* 519 */       this.dom = this.dom.getOwnerDocument().renameNode(this.dom, this.dom.getNamespaceURI(), QName.qualify(str, paramString));
/*     */     }
/*     */   }
/*     */ 
/*     */   final QName getQname() {
/* 524 */     String str1 = this.dom.getNamespaceURI() == null ? "" : this.dom.getNamespaceURI();
/* 525 */     String str2 = this.dom.getPrefix() == null ? "" : this.dom.getPrefix();
/* 526 */     return QName.create(str1, this.dom.getLocalName(), str2);
/*     */   }
/*     */ 
/*     */   void addMatchingChildren(XMLList paramXMLList, Filter paramFilter) {
/* 530 */     Node localNode1 = this.dom;
/* 531 */     NodeList localNodeList = localNode1.getChildNodes();
/* 532 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 533 */       Node localNode2 = localNodeList.item(i);
/* 534 */       XmlNode localXmlNode = createImpl(localNode2);
/* 535 */       if (paramFilter.accept(localNode2))
/* 536 */         paramXMLList.addToList(localXmlNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   XmlNode[] getMatchingChildren(Filter paramFilter)
/*     */   {
/* 542 */     ArrayList localArrayList = new ArrayList();
/* 543 */     NodeList localNodeList = this.dom.getChildNodes();
/* 544 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 545 */       Node localNode = localNodeList.item(i);
/* 546 */       if (paramFilter.accept(localNode)) {
/* 547 */         localArrayList.add(createImpl(localNode));
/*     */       }
/*     */     }
/* 550 */     return (XmlNode[])localArrayList.toArray(new XmlNode[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   XmlNode[] getAttributes() {
/* 554 */     NamedNodeMap localNamedNodeMap = this.dom.getAttributes();
/*     */ 
/* 556 */     if (localNamedNodeMap == null) throw new IllegalStateException("Must be element.");
/* 557 */     XmlNode[] arrayOfXmlNode = new XmlNode[localNamedNodeMap.getLength()];
/* 558 */     for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
/* 559 */       arrayOfXmlNode[i] = createImpl(localNamedNodeMap.item(i));
/*     */     }
/* 561 */     return arrayOfXmlNode;
/*     */   }
/*     */ 
/*     */   String getAttributeValue() {
/* 565 */     return ((Attr)this.dom).getValue();
/*     */   }
/*     */ 
/*     */   void setAttribute(QName paramQName, String paramString) {
/* 569 */     if (!(this.dom instanceof Element)) throw new IllegalStateException("Can only set attribute on elements.");
/* 570 */     paramQName.setAttribute((Element)this.dom, paramString);
/*     */   }
/*     */ 
/*     */   void replaceWith(XmlNode paramXmlNode) {
/* 574 */     Node localNode = paramXmlNode.dom;
/* 575 */     if (localNode.getOwnerDocument() != this.dom.getOwnerDocument()) {
/* 576 */       localNode = this.dom.getOwnerDocument().importNode(localNode, true);
/*     */     }
/* 578 */     this.dom.getParentNode().replaceChild(localNode, this.dom);
/*     */   }
/*     */ 
/*     */   String ecmaToXMLString(XmlProcessor paramXmlProcessor) {
/* 582 */     if (isElementType()) {
/* 583 */       Element localElement = (Element)this.dom.cloneNode(true);
/* 584 */       Namespace[] arrayOfNamespace = getInScopeNamespaces();
/* 585 */       for (int i = 0; i < arrayOfNamespace.length; i++) {
/* 586 */         declareNamespace(localElement, arrayOfNamespace[i].getPrefix(), arrayOfNamespace[i].getUri());
/*     */       }
/* 588 */       return paramXmlProcessor.ecmaToXmlString(localElement);
/*     */     }
/* 590 */     return paramXmlProcessor.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */   Node toDomNode()
/*     */   {
/* 911 */     return this.dom;
/*     */   }
/*     */ 
/*     */   static abstract class Filter
/*     */   {
/* 870 */     static final Filter COMMENT = new Filter()
/*     */     {
/*     */       boolean accept(Node paramAnonymousNode) {
/* 873 */         return paramAnonymousNode.getNodeType() == 8;
/*     */       }
/* 870 */     };
/*     */ 
/* 876 */     static final Filter TEXT = new Filter()
/*     */     {
/*     */       boolean accept(Node paramAnonymousNode) {
/* 879 */         return paramAnonymousNode.getNodeType() == 3;
/*     */       }
/* 876 */     };
/*     */ 
/* 894 */     static Filter ELEMENT = new Filter()
/*     */     {
/*     */       boolean accept(Node paramAnonymousNode) {
/* 897 */         return paramAnonymousNode.getNodeType() == 1;
/*     */       }
/* 894 */     };
/*     */ 
/* 900 */     static Filter TRUE = new Filter()
/*     */     {
/*     */       boolean accept(Node paramAnonymousNode) {
/* 903 */         return true;
/*     */       }
/* 900 */     };
/*     */ 
/*     */     static Filter PROCESSING_INSTRUCTION(XMLName paramXMLName)
/*     */     {
/* 883 */       return new Filter()
/*     */       {
/*     */         boolean accept(Node paramAnonymousNode) {
/* 886 */           if (paramAnonymousNode.getNodeType() == 7) {
/* 887 */             ProcessingInstruction localProcessingInstruction = (ProcessingInstruction)paramAnonymousNode;
/* 888 */             return this.val$name.matchesLocalName(localProcessingInstruction.getTarget());
/*     */           }
/* 890 */           return false;
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     abstract boolean accept(Node paramNode);
/*     */   }
/*     */ 
/*     */   static class InternalList
/*     */   {
/*     */     private static final long serialVersionUID = -3633151157292048978L;
/*     */     private List<XmlNode> list;
/*     */ 
/*     */     InternalList()
/*     */     {
/* 809 */       this.list = new ArrayList();
/*     */     }
/*     */ 
/*     */     private void _add(XmlNode paramXmlNode) {
/* 813 */       this.list.add(paramXmlNode);
/*     */     }
/*     */ 
/*     */     XmlNode item(int paramInt) {
/* 817 */       return (XmlNode)this.list.get(paramInt);
/*     */     }
/*     */ 
/*     */     void remove(int paramInt) {
/* 821 */       this.list.remove(paramInt);
/*     */     }
/*     */ 
/*     */     void add(InternalList paramInternalList) {
/* 825 */       for (int i = 0; i < paramInternalList.length(); i++)
/* 826 */         _add(paramInternalList.item(i));
/*     */     }
/*     */ 
/*     */     void add(InternalList paramInternalList, int paramInt1, int paramInt2)
/*     */     {
/* 831 */       for (int i = paramInt1; i < paramInt2; i++)
/* 832 */         _add(paramInternalList.item(i));
/*     */     }
/*     */ 
/*     */     void add(XmlNode paramXmlNode)
/*     */     {
/* 837 */       _add(paramXmlNode);
/*     */     }
/*     */ 
/*     */     void add(XML paramXML)
/*     */     {
/* 842 */       _add(paramXML.getAnnotation());
/*     */     }
/*     */ 
/*     */     void addToList(Object paramObject)
/*     */     {
/* 847 */       if ((paramObject instanceof Undefined))
/*     */       {
/* 849 */         return;
/*     */       }
/*     */ 
/* 852 */       if ((paramObject instanceof XMLList)) {
/* 853 */         XMLList localXMLList = (XMLList)paramObject;
/* 854 */         for (int i = 0; i < localXMLList.length(); i++)
/* 855 */           _add(localXMLList.item(i).getAnnotation());
/*     */       }
/* 857 */       else if ((paramObject instanceof XML)) {
/* 858 */         _add(((XML)paramObject).getAnnotation());
/* 859 */       } else if ((paramObject instanceof XmlNode)) {
/* 860 */         _add((XmlNode)paramObject);
/*     */       }
/*     */     }
/*     */ 
/*     */     int length() {
/* 865 */       return this.list.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Namespace
/*     */   {
/*     */     private static final long serialVersionUID = 4073904386884677090L;
/* 628 */     static final Namespace GLOBAL = create("", "");
/*     */     private String prefix;
/*     */     private String uri;
/*     */ 
/*     */     static Namespace create(String paramString1, String paramString2)
/*     */     {
/* 602 */       if (paramString1 == null) {
/* 603 */         throw new IllegalArgumentException("Empty string represents default namespace prefix");
/*     */       }
/*     */ 
/* 606 */       if (paramString2 == null) {
/* 607 */         throw new IllegalArgumentException("Namespace may not lack a URI");
/*     */       }
/*     */ 
/* 610 */       Namespace localNamespace = new Namespace();
/* 611 */       localNamespace.prefix = paramString1;
/* 612 */       localNamespace.uri = paramString2;
/* 613 */       return localNamespace;
/*     */     }
/*     */ 
/*     */     static Namespace create(String paramString) {
/* 617 */       Namespace localNamespace = new Namespace();
/* 618 */       localNamespace.uri = paramString;
/*     */ 
/* 621 */       if (paramString.length() == 0) {
/* 622 */         localNamespace.prefix = "";
/*     */       }
/*     */ 
/* 625 */       return localNamespace;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 638 */       if (this.prefix == null) return "XmlNode.Namespace [" + this.uri + "]";
/* 639 */       return "XmlNode.Namespace [" + this.prefix + "{" + this.uri + "}]";
/*     */     }
/*     */ 
/*     */     boolean isUnspecifiedPrefix() {
/* 643 */       return this.prefix == null;
/*     */     }
/*     */ 
/*     */     boolean is(Namespace paramNamespace) {
/* 647 */       return (this.prefix != null) && (paramNamespace.prefix != null) && (this.prefix.equals(paramNamespace.prefix)) && (this.uri.equals(paramNamespace.uri));
/*     */     }
/*     */ 
/*     */     boolean isEmpty() {
/* 651 */       return (this.prefix != null) && (this.prefix.equals("")) && (this.uri.equals(""));
/*     */     }
/*     */ 
/*     */     boolean isDefault() {
/* 655 */       return (this.prefix != null) && (this.prefix.equals(""));
/*     */     }
/*     */ 
/*     */     boolean isGlobal() {
/* 659 */       return (this.uri != null) && (this.uri.equals(""));
/*     */     }
/*     */ 
/*     */     private void setPrefix(String paramString)
/*     */     {
/* 665 */       if (paramString == null) throw new IllegalArgumentException();
/* 666 */       this.prefix = paramString;
/*     */     }
/*     */ 
/*     */     String getPrefix() {
/* 670 */       return this.prefix;
/*     */     }
/*     */ 
/*     */     String getUri() {
/* 674 */       return this.uri;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Namespaces
/*     */   {
/* 347 */     private Map<String, String> map = new HashMap();
/* 348 */     private Map<String, String> uriToPrefix = new HashMap();
/*     */ 
/*     */     void declare(XmlNode.Namespace paramNamespace)
/*     */     {
/* 354 */       if (this.map.get(paramNamespace.prefix) == null) {
/* 355 */         this.map.put(paramNamespace.prefix, paramNamespace.uri);
/*     */       }
/*     */ 
/* 359 */       if (this.uriToPrefix.get(paramNamespace.uri) == null)
/* 360 */         this.uriToPrefix.put(paramNamespace.uri, paramNamespace.prefix);
/*     */     }
/*     */ 
/*     */     XmlNode.Namespace getNamespaceByUri(String paramString)
/*     */     {
/* 365 */       if (this.uriToPrefix.get(paramString) == null) return null;
/* 366 */       return XmlNode.Namespace.create(paramString, (String)this.uriToPrefix.get(paramString));
/*     */     }
/*     */ 
/*     */     XmlNode.Namespace getNamespace(String paramString) {
/* 370 */       if (this.map.get(paramString) == null) return null;
/* 371 */       return XmlNode.Namespace.create(paramString, (String)this.map.get(paramString));
/*     */     }
/*     */ 
/*     */     XmlNode.Namespace[] getNamespaces() {
/* 375 */       ArrayList localArrayList = new ArrayList();
/* 376 */       for (String str1 : this.map.keySet()) {
/* 377 */         String str2 = (String)this.map.get(str1);
/* 378 */         XmlNode.Namespace localNamespace = XmlNode.Namespace.create(str1, str2);
/* 379 */         if (!localNamespace.isEmpty()) {
/* 380 */           localArrayList.add(localNamespace);
/*     */         }
/*     */       }
/* 383 */       return (XmlNode.Namespace[])localArrayList.toArray(new XmlNode.Namespace[localArrayList.size()]);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class QName
/*     */   {
/*     */     private static final long serialVersionUID = -6587069811691451077L;
/*     */     private XmlNode.Namespace namespace;
/*     */     private String localName;
/*     */ 
/*     */     static QName create(XmlNode.Namespace paramNamespace, String paramString)
/*     */     {
/* 685 */       if ((paramString != null) && (paramString.equals("*"))) throw new RuntimeException("* is not valid localName");
/* 686 */       QName localQName = new QName();
/* 687 */       localQName.namespace = paramNamespace;
/* 688 */       localQName.localName = paramString;
/* 689 */       return localQName;
/*     */     }
/*     */ 
/*     */     /** @deprecated */
/*     */     static QName create(String paramString1, String paramString2, String paramString3) {
/* 694 */       return create(XmlNode.Namespace.create(paramString3, paramString1), paramString2);
/*     */     }
/*     */ 
/*     */     static String qualify(String paramString1, String paramString2) {
/* 698 */       if (paramString1 == null) throw new IllegalArgumentException("prefix must not be null");
/* 699 */       if (paramString1.length() > 0) return paramString1 + ":" + paramString2;
/* 700 */       return paramString2;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 711 */       return "XmlNode.QName [" + this.localName + "," + this.namespace + "]";
/*     */     }
/*     */ 
/*     */     private boolean equals(String paramString1, String paramString2) {
/* 715 */       if ((paramString1 == null) && (paramString2 == null)) return true;
/* 716 */       if ((paramString1 == null) || (paramString2 == null)) return false;
/* 717 */       return paramString1.equals(paramString2);
/*     */     }
/*     */ 
/*     */     private boolean namespacesEqual(XmlNode.Namespace paramNamespace1, XmlNode.Namespace paramNamespace2) {
/* 721 */       if ((paramNamespace1 == null) && (paramNamespace2 == null)) return true;
/* 722 */       if ((paramNamespace1 == null) || (paramNamespace2 == null)) return false;
/* 723 */       return equals(paramNamespace1.getUri(), paramNamespace2.getUri());
/*     */     }
/*     */ 
/*     */     final boolean equals(QName paramQName) {
/* 727 */       if (!namespacesEqual(this.namespace, paramQName.namespace)) return false;
/* 728 */       if (!equals(this.localName, paramQName.localName)) return false;
/* 729 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 734 */       if (!(paramObject instanceof QName)) {
/* 735 */         return false;
/*     */       }
/* 737 */       return equals((QName)paramObject);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 742 */       return this.localName == null ? 0 : this.localName.hashCode();
/*     */     }
/*     */ 
/*     */     void lookupPrefix(Node paramNode) {
/* 746 */       if (paramNode == null) throw new IllegalArgumentException("node must not be null");
/* 747 */       Object localObject = paramNode.lookupPrefix(this.namespace.getUri());
/*     */       String str2;
/* 748 */       if (localObject == null)
/*     */       {
/* 750 */         String str1 = paramNode.lookupNamespaceURI(null);
/* 751 */         if (str1 == null) str1 = "";
/* 752 */         str2 = this.namespace.getUri();
/* 753 */         if (str2.equals(str1)) {
/* 754 */           localObject = "";
/*     */         }
/*     */       }
/* 757 */       int i = 0;
/* 758 */       while (localObject == null) {
/* 759 */         str2 = "e4x_" + i++;
/* 760 */         String str3 = paramNode.lookupNamespaceURI(str2);
/* 761 */         if (str3 == null) {
/* 762 */           localObject = str2;
/* 763 */           Node localNode = paramNode;
/* 764 */           while ((localNode.getParentNode() != null) && ((localNode.getParentNode() instanceof Element))) {
/* 765 */             localNode = localNode.getParentNode();
/*     */           }
/* 767 */           ((Element)localNode).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + (String)localObject, this.namespace.getUri());
/*     */         }
/*     */       }
/* 770 */       this.namespace.setPrefix((String)localObject);
/*     */     }
/*     */ 
/*     */     String qualify(Node paramNode) {
/* 774 */       if (this.namespace.getPrefix() == null) {
/* 775 */         if (paramNode != null) {
/* 776 */           lookupPrefix(paramNode);
/*     */         }
/* 778 */         else if (this.namespace.getUri().equals("")) {
/* 779 */           this.namespace.setPrefix("");
/*     */         }
/*     */         else
/*     */         {
/* 783 */           this.namespace.setPrefix("");
/*     */         }
/*     */       }
/*     */ 
/* 787 */       return qualify(this.namespace.getPrefix(), this.localName);
/*     */     }
/*     */ 
/*     */     void setAttribute(Element paramElement, String paramString) {
/* 791 */       if (this.namespace.getPrefix() == null) lookupPrefix(paramElement);
/* 792 */       paramElement.setAttributeNS(this.namespace.getUri(), qualify(this.namespace.getPrefix(), this.localName), paramString);
/*     */     }
/*     */ 
/*     */     XmlNode.Namespace getNamespace() {
/* 796 */       return this.namespace;
/*     */     }
/*     */ 
/*     */     String getLocalName() {
/* 800 */       return this.localName;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class XmlNodeUserDataHandler
/*     */     implements UserDataHandler
/*     */   {
/*     */     private static final long serialVersionUID = 4666895518900769588L;
/*     */ 
/*     */     public void handle(short paramShort, String paramString, Object paramObject, Node paramNode1, Node paramNode2)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XmlNode
 * JD-Core Version:    0.6.2
 */