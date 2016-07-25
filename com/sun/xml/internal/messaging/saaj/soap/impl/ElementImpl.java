/*      */ package com.sun.xml.internal.messaging.saaj.soap.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
/*      */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
/*      */ import com.sun.xml.internal.messaging.saaj.util.JaxmURI;
/*      */ import com.sun.xml.internal.messaging.saaj.util.JaxmURI.MalformedURIException;
/*      */ import com.sun.xml.internal.messaging.saaj.util.NamespaceContextIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.soap.Name;
/*      */ import javax.xml.soap.SOAPBodyElement;
/*      */ import javax.xml.soap.SOAPElement;
/*      */ import javax.xml.soap.SOAPException;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentFragment;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Text;
/*      */ 
/*      */ public class ElementImpl extends ElementNSImpl
/*      */   implements SOAPElement, SOAPBodyElement
/*      */ {
/*   48 */   public static final String DSIG_NS = "http://www.w3.org/2000/09/xmldsig#".intern();
/*   49 */   public static final String XENC_NS = "http://www.w3.org/2001/04/xmlenc#".intern();
/*   50 */   public static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd".intern();
/*      */ 
/*   52 */   private AttributeManager encodingStyleAttribute = new AttributeManager();
/*      */   protected QName elementQName;
/*   56 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.impl", "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");
/*      */ 
/*   65 */   public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
/*      */ 
/*   71 */   public static final String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();
/*      */ 
/*      */   public ElementImpl(SOAPDocumentImpl ownerDoc, Name name) {
/*   74 */     super(ownerDoc, name.getURI(), name.getQualifiedName(), name.getLocalName());
/*      */ 
/*   79 */     this.elementQName = NameImpl.convertToQName(name);
/*      */   }
/*      */ 
/*      */   public ElementImpl(SOAPDocumentImpl ownerDoc, QName name) {
/*   83 */     super(ownerDoc, name.getNamespaceURI(), getQualifiedName(name), name.getLocalPart());
/*      */ 
/*   88 */     this.elementQName = name;
/*      */   }
/*      */ 
/*      */   public ElementImpl(SOAPDocumentImpl ownerDoc, String uri, String qualifiedName)
/*      */   {
/*   96 */     super(ownerDoc, uri, qualifiedName);
/*   97 */     this.elementQName = new QName(uri, getLocalPart(qualifiedName), getPrefix(qualifiedName));
/*      */   }
/*      */ 
/*      */   public void ensureNamespaceIsDeclared(String prefix, String uri)
/*      */   {
/*  102 */     String alreadyDeclaredUri = getNamespaceURI(prefix);
/*  103 */     if ((alreadyDeclaredUri == null) || (!alreadyDeclaredUri.equals(uri)))
/*      */       try {
/*  105 */         addNamespaceDeclaration(prefix, uri);
/*      */       }
/*      */       catch (SOAPException e) {
/*      */       }
/*      */   }
/*      */ 
/*      */   public Document getOwnerDocument() {
/*  112 */     SOAPDocument ownerSOAPDocument = (SOAPDocument)super.getOwnerDocument();
/*      */ 
/*  114 */     if (ownerSOAPDocument == null) {
/*  115 */       return null;
/*      */     }
/*  117 */     return ownerSOAPDocument.getDocument();
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(Name name) throws SOAPException {
/*  121 */     return addElement(name);
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(QName qname) throws SOAPException {
/*  125 */     return addElement(qname);
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(String localName) throws SOAPException {
/*  129 */     return addChildElement(NameImpl.createFromUnqualifiedName(localName));
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(String localName, String prefix)
/*      */     throws SOAPException
/*      */   {
/*  135 */     String uri = getNamespaceURI(prefix);
/*  136 */     if (uri == null) {
/*  137 */       log.log(Level.SEVERE, "SAAJ0101.impl.parent.of.body.elem.mustbe.body", new String[] { prefix });
/*      */ 
/*  141 */       throw new SOAPExceptionImpl("Unable to locate namespace for prefix " + prefix);
/*      */     }
/*      */ 
/*  144 */     return addChildElement(localName, prefix, uri);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String prefix)
/*      */   {
/*  149 */     if ("xmlns".equals(prefix)) {
/*  150 */       return XMLNS_URI;
/*      */     }
/*      */ 
/*  153 */     if ("xml".equals(prefix)) {
/*  154 */       return XML_URI;
/*      */     }
/*      */ 
/*  157 */     if ("".equals(prefix))
/*      */     {
/*  159 */       org.w3c.dom.Node currentAncestor = this;
/*  160 */       while ((currentAncestor != null) && (!(currentAncestor instanceof Document)))
/*      */       {
/*  163 */         if ((currentAncestor instanceof ElementImpl)) {
/*  164 */           QName name = ((ElementImpl)currentAncestor).getElementQName();
/*      */ 
/*  175 */           if (((Element)currentAncestor).hasAttributeNS(XMLNS_URI, "xmlns"))
/*      */           {
/*  178 */             String uri = ((Element)currentAncestor).getAttributeNS(XMLNS_URI, "xmlns");
/*      */ 
/*  181 */             if ("".equals(uri)) {
/*  182 */               return null;
/*      */             }
/*  184 */             return uri;
/*      */           }
/*      */         }
/*      */ 
/*  188 */         currentAncestor = currentAncestor.getParentNode();
/*      */       }
/*      */     }
/*  191 */     else if (prefix != null)
/*      */     {
/*  193 */       org.w3c.dom.Node currentAncestor = this;
/*      */ 
/*  197 */       while ((currentAncestor != null) && (!(currentAncestor instanceof Document)))
/*      */       {
/*  212 */         if (((Element)currentAncestor).hasAttributeNS(XMLNS_URI, prefix))
/*      */         {
/*  214 */           return ((Element)currentAncestor).getAttributeNS(XMLNS_URI, prefix);
/*      */         }
/*      */ 
/*  218 */         currentAncestor = currentAncestor.getParentNode();
/*      */       }
/*      */     }
/*      */ 
/*  222 */     return null;
/*      */   }
/*      */ 
/*      */   public SOAPElement setElementQName(QName newName) throws SOAPException {
/*  226 */     ElementImpl copy = new ElementImpl((SOAPDocumentImpl)getOwnerDocument(), newName);
/*      */ 
/*  228 */     return replaceElementWithSOAPElement(this, copy);
/*      */   }
/*      */ 
/*      */   public QName createQName(String localName, String prefix) throws SOAPException
/*      */   {
/*  233 */     String uri = getNamespaceURI(prefix);
/*  234 */     if (uri == null) {
/*  235 */       log.log(Level.SEVERE, "SAAJ0102.impl.cannot.locate.ns", new Object[] { prefix });
/*      */ 
/*  237 */       throw new SOAPException("Unable to locate namespace for prefix " + prefix);
/*      */     }
/*      */ 
/*  240 */     return new QName(uri, localName, prefix);
/*      */   }
/*      */ 
/*      */   public String getNamespacePrefix(String uri)
/*      */   {
/*  245 */     NamespaceContextIterator eachNamespace = getNamespaceContextNodes();
/*  246 */     while (eachNamespace.hasNext()) {
/*  247 */       Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
/*  248 */       if (namespaceDecl.getNodeValue().equals(uri)) {
/*  249 */         String candidatePrefix = namespaceDecl.getLocalName();
/*  250 */         if ("xmlns".equals(candidatePrefix)) {
/*  251 */           return "";
/*      */         }
/*  253 */         return candidatePrefix;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  258 */     org.w3c.dom.Node currentAncestor = this;
/*  259 */     while ((currentAncestor != null) && (!(currentAncestor instanceof Document)))
/*      */     {
/*  262 */       if (uri.equals(currentAncestor.getNamespaceURI()))
/*  263 */         return currentAncestor.getPrefix();
/*  264 */       currentAncestor = currentAncestor.getParentNode();
/*      */     }
/*      */ 
/*  267 */     return null;
/*      */   }
/*      */ 
/*      */   protected Attr getNamespaceAttr(String prefix) {
/*  271 */     NamespaceContextIterator eachNamespace = getNamespaceContextNodes();
/*  272 */     if (!"".equals(prefix))
/*  273 */       prefix = ":" + prefix;
/*  274 */     while (eachNamespace.hasNext()) {
/*  275 */       Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
/*  276 */       if (!"".equals(prefix)) {
/*  277 */         if (namespaceDecl.getNodeName().endsWith(prefix))
/*  278 */           return namespaceDecl;
/*      */       }
/*  280 */       else if (namespaceDecl.getNodeName().equals("xmlns")) {
/*  281 */         return namespaceDecl;
/*      */       }
/*      */     }
/*  284 */     return null;
/*      */   }
/*      */ 
/*      */   public NamespaceContextIterator getNamespaceContextNodes() {
/*  288 */     return getNamespaceContextNodes(true);
/*      */   }
/*      */ 
/*      */   public NamespaceContextIterator getNamespaceContextNodes(boolean traverseStack) {
/*  292 */     return new NamespaceContextIterator(this, traverseStack);
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(String localName, String prefix, String uri)
/*      */     throws SOAPException
/*      */   {
/*  301 */     SOAPElement newElement = createElement(NameImpl.create(localName, prefix, uri));
/*  302 */     addNode(newElement);
/*  303 */     return convertToSoapElement(newElement);
/*      */   }
/*      */ 
/*      */   public SOAPElement addChildElement(SOAPElement element)
/*      */     throws SOAPException
/*      */   {
/*  310 */     String elementURI = element.getElementName().getURI();
/*  311 */     String localName = element.getLocalName();
/*      */ 
/*  313 */     if (("http://schemas.xmlsoap.org/soap/envelope/".equals(elementURI)) || ("http://www.w3.org/2003/05/soap-envelope".equals(elementURI)))
/*      */     {
/*  317 */       if (("Envelope".equalsIgnoreCase(localName)) || ("Header".equalsIgnoreCase(localName)) || ("Body".equalsIgnoreCase(localName)))
/*      */       {
/*  319 */         log.severe("SAAJ0103.impl.cannot.add.fragements");
/*  320 */         throw new SOAPExceptionImpl("Cannot add fragments which contain elements which are in the SOAP namespace");
/*      */       }
/*      */ 
/*  325 */       if (("Fault".equalsIgnoreCase(localName)) && (!"Body".equalsIgnoreCase(getLocalName()))) {
/*  326 */         log.severe("SAAJ0154.impl.adding.fault.to.nonbody");
/*  327 */         throw new SOAPExceptionImpl("Cannot add a SOAPFault as a child of " + getLocalName());
/*      */       }
/*      */ 
/*  330 */       if (("Detail".equalsIgnoreCase(localName)) && (!"Fault".equalsIgnoreCase(getLocalName()))) {
/*  331 */         log.severe("SAAJ0155.impl.adding.detail.nonfault");
/*  332 */         throw new SOAPExceptionImpl("Cannot add a Detail as a child of " + getLocalName());
/*      */       }
/*      */ 
/*  335 */       if ("Fault".equalsIgnoreCase(localName))
/*      */       {
/*  337 */         if (!elementURI.equals(getElementName().getURI())) {
/*  338 */           log.severe("SAAJ0158.impl.version.mismatch.fault");
/*  339 */           throw new SOAPExceptionImpl("SOAP Version mismatch encountered when trying to add SOAPFault to SOAPBody");
/*      */         }
/*  341 */         Iterator it = getChildElements();
/*  342 */         if (it.hasNext()) {
/*  343 */           log.severe("SAAJ0156.impl.adding.fault.error");
/*  344 */           throw new SOAPExceptionImpl("Cannot add SOAPFault as a child of a non-Empty SOAPBody");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  350 */     String encodingStyle = element.getEncodingStyle();
/*      */ 
/*  352 */     ElementImpl importedElement = (ElementImpl)importElement(element);
/*  353 */     addNode(importedElement);
/*      */ 
/*  355 */     if (encodingStyle != null) {
/*  356 */       importedElement.setEncodingStyle(encodingStyle);
/*      */     }
/*  358 */     return convertToSoapElement(importedElement);
/*      */   }
/*      */ 
/*      */   protected Element importElement(Element element) {
/*  362 */     Document document = getOwnerDocument();
/*  363 */     Document oldDocument = element.getOwnerDocument();
/*  364 */     if (!oldDocument.equals(document)) {
/*  365 */       return (Element)document.importNode(element, true);
/*      */     }
/*  367 */     return element;
/*      */   }
/*      */ 
/*      */   protected SOAPElement addElement(Name name) throws SOAPException
/*      */   {
/*  372 */     SOAPElement newElement = createElement(name);
/*  373 */     addNode(newElement);
/*  374 */     return circumventBug5034339(newElement);
/*      */   }
/*      */ 
/*      */   protected SOAPElement addElement(QName name) throws SOAPException {
/*  378 */     SOAPElement newElement = createElement(name);
/*  379 */     addNode(newElement);
/*  380 */     return circumventBug5034339(newElement);
/*      */   }
/*      */ 
/*      */   protected SOAPElement createElement(Name name)
/*      */   {
/*  385 */     if (isNamespaceQualified(name)) {
/*  386 */       return (SOAPBodyElement)getOwnerDocument().createElementNS(name.getURI(), name.getQualifiedName());
/*      */     }
/*      */ 
/*  391 */     return (SOAPBodyElement)getOwnerDocument().createElement(name.getQualifiedName());
/*      */   }
/*      */ 
/*      */   protected SOAPElement createElement(QName name)
/*      */   {
/*  398 */     if (isNamespaceQualified(name)) {
/*  399 */       return (SOAPBodyElement)getOwnerDocument().createElementNS(name.getNamespaceURI(), getQualifiedName(name));
/*      */     }
/*      */ 
/*  404 */     return (SOAPBodyElement)getOwnerDocument().createElement(getQualifiedName(name));
/*      */   }
/*      */ 
/*      */   protected void addNode(org.w3c.dom.Node newElement)
/*      */     throws SOAPException
/*      */   {
/*  410 */     insertBefore(newElement, null);
/*      */ 
/*  412 */     if ((getOwnerDocument() instanceof DocumentFragment)) {
/*  413 */       return;
/*      */     }
/*  415 */     if ((newElement instanceof ElementImpl)) {
/*  416 */       ElementImpl element = (ElementImpl)newElement;
/*  417 */       QName elementName = element.getElementQName();
/*  418 */       if (!"".equals(elementName.getNamespaceURI()))
/*  419 */         element.ensureNamespaceIsDeclared(elementName.getPrefix(), elementName.getNamespaceURI());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected SOAPElement findChild(NameImpl name)
/*      */   {
/*  427 */     Iterator eachChild = getChildElementNodes();
/*  428 */     while (eachChild.hasNext()) {
/*  429 */       SOAPElement child = (SOAPBodyElement)eachChild.next();
/*  430 */       if (child.getElementName().equals(name)) {
/*  431 */         return child;
/*      */       }
/*      */     }
/*      */ 
/*  435 */     return null;
/*      */   }
/*      */ 
/*      */   public SOAPElement addTextNode(String text) throws SOAPException {
/*  439 */     if ((text.startsWith("<![CDATA[")) || (text.startsWith("<![cdata[")))
/*      */     {
/*  441 */       return addCDATA(text.substring("<![CDATA[".length(), text.length() - 3));
/*      */     }
/*  443 */     return addText(text);
/*      */   }
/*      */ 
/*      */   protected SOAPElement addCDATA(String text) throws SOAPException {
/*  447 */     Text cdata = getOwnerDocument().createCDATASection(text);
/*      */ 
/*  449 */     addNode(cdata);
/*  450 */     return this;
/*      */   }
/*      */ 
/*      */   protected SOAPElement addText(String text) throws SOAPException {
/*  454 */     Text textNode = getOwnerDocument().createTextNode(text);
/*      */ 
/*  456 */     addNode(textNode);
/*  457 */     return this;
/*      */   }
/*      */ 
/*      */   public SOAPElement addAttribute(Name name, String value) throws SOAPException
/*      */   {
/*  462 */     addAttributeBare(name, value);
/*  463 */     if (!"".equals(name.getURI())) {
/*  464 */       ensureNamespaceIsDeclared(name.getPrefix(), name.getURI());
/*      */     }
/*  466 */     return this;
/*      */   }
/*      */ 
/*      */   public SOAPElement addAttribute(QName qname, String value) throws SOAPException
/*      */   {
/*  471 */     addAttributeBare(qname, value);
/*  472 */     if (!"".equals(qname.getNamespaceURI())) {
/*  473 */       ensureNamespaceIsDeclared(qname.getPrefix(), qname.getNamespaceURI());
/*      */     }
/*  475 */     return this;
/*      */   }
/*      */ 
/*      */   private void addAttributeBare(Name name, String value) {
/*  479 */     addAttributeBare(name.getURI(), name.getPrefix(), name.getQualifiedName(), value);
/*      */   }
/*      */ 
/*      */   private void addAttributeBare(QName name, String value)
/*      */   {
/*  486 */     addAttributeBare(name.getNamespaceURI(), name.getPrefix(), getQualifiedName(name), value);
/*      */   }
/*      */ 
/*      */   private void addAttributeBare(String uri, String prefix, String qualifiedName, String value)
/*      */   {
/*  499 */     uri = uri.length() == 0 ? null : uri;
/*  500 */     if (qualifiedName.equals("xmlns")) {
/*  501 */       uri = XMLNS_URI;
/*      */     }
/*      */ 
/*  504 */     if (uri == null)
/*  505 */       setAttribute(qualifiedName, value);
/*      */     else
/*  507 */       setAttributeNS(uri, qualifiedName, value);
/*      */   }
/*      */ 
/*      */   public SOAPElement addNamespaceDeclaration(String prefix, String uri)
/*      */     throws SOAPException
/*      */   {
/*  513 */     if (prefix.length() > 0)
/*  514 */       setAttributeNS(XMLNS_URI, "xmlns:" + prefix, uri);
/*      */     else {
/*  516 */       setAttributeNS(XMLNS_URI, "xmlns", uri);
/*      */     }
/*      */ 
/*  520 */     return this;
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(Name name) {
/*  524 */     return getAttributeValueFrom(this, name);
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(QName qname) {
/*  528 */     return getAttributeValueFrom(this, qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix(), getQualifiedName(qname));
/*      */   }
/*      */ 
/*      */   public Iterator getAllAttributes()
/*      */   {
/*  537 */     Iterator i = getAllAttributesFrom(this);
/*  538 */     ArrayList list = new ArrayList();
/*  539 */     while (i.hasNext()) {
/*  540 */       Name name = (Name)i.next();
/*  541 */       if (!"xmlns".equalsIgnoreCase(name.getPrefix()))
/*  542 */         list.add(name);
/*      */     }
/*  544 */     return list.iterator();
/*      */   }
/*      */ 
/*      */   public Iterator getAllAttributesAsQNames() {
/*  548 */     Iterator i = getAllAttributesFrom(this);
/*  549 */     ArrayList list = new ArrayList();
/*  550 */     while (i.hasNext()) {
/*  551 */       Name name = (Name)i.next();
/*  552 */       if (!"xmlns".equalsIgnoreCase(name.getPrefix())) {
/*  553 */         list.add(NameImpl.convertToQName(name));
/*      */       }
/*      */     }
/*  556 */     return list.iterator();
/*      */   }
/*      */ 
/*      */   public Iterator getNamespacePrefixes()
/*      */   {
/*  561 */     return doGetNamespacePrefixes(false);
/*      */   }
/*      */ 
/*      */   public Iterator getVisibleNamespacePrefixes() {
/*  565 */     return doGetNamespacePrefixes(true);
/*      */   }
/*      */ 
/*      */   protected Iterator doGetNamespacePrefixes(final boolean deep) {
/*  569 */     return new Iterator() {
/*  570 */       String next = null;
/*  571 */       String last = null;
/*  572 */       NamespaceContextIterator eachNamespace = ElementImpl.this.getNamespaceContextNodes(deep);
/*      */ 
/*      */       void findNext()
/*      */       {
/*  576 */         while ((this.next == null) && (this.eachNamespace.hasNext())) {
/*  577 */           String attributeKey = this.eachNamespace.nextNamespaceAttr().getNodeName();
/*      */ 
/*  579 */           if (attributeKey.startsWith("xmlns:"))
/*  580 */             this.next = attributeKey.substring("xmlns:".length());
/*      */         }
/*      */       }
/*      */ 
/*      */       public boolean hasNext()
/*      */       {
/*  586 */         findNext();
/*  587 */         return this.next != null;
/*      */       }
/*      */ 
/*      */       public Object next() {
/*  591 */         findNext();
/*  592 */         if (this.next == null) {
/*  593 */           throw new NoSuchElementException();
/*      */         }
/*      */ 
/*  596 */         this.last = this.next;
/*  597 */         this.next = null;
/*  598 */         return this.last;
/*      */       }
/*      */ 
/*      */       public void remove() {
/*  602 */         if (this.last == null) {
/*  603 */           throw new IllegalStateException();
/*      */         }
/*  605 */         this.eachNamespace.remove();
/*  606 */         this.next = null;
/*  607 */         this.last = null;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public Name getElementName() {
/*  613 */     return NameImpl.convertToName(this.elementQName);
/*      */   }
/*      */ 
/*      */   public QName getElementQName() {
/*  617 */     return this.elementQName;
/*      */   }
/*      */ 
/*      */   public boolean removeAttribute(Name name) {
/*  621 */     return removeAttribute(name.getURI(), name.getLocalName());
/*      */   }
/*      */ 
/*      */   public boolean removeAttribute(QName name) {
/*  625 */     return removeAttribute(name.getNamespaceURI(), name.getLocalPart());
/*      */   }
/*      */ 
/*      */   private boolean removeAttribute(String uri, String localName) {
/*  629 */     String nonzeroLengthUri = (uri == null) || (uri.length() == 0) ? null : uri;
/*      */ 
/*  631 */     Attr attribute = getAttributeNodeNS(nonzeroLengthUri, localName);
/*      */ 
/*  633 */     if (attribute == null) {
/*  634 */       return false;
/*      */     }
/*  636 */     removeAttributeNode(attribute);
/*  637 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean removeNamespaceDeclaration(String prefix) {
/*  641 */     Attr declaration = getNamespaceAttr(prefix);
/*  642 */     if (declaration == null)
/*  643 */       return false;
/*      */     try
/*      */     {
/*  646 */       removeAttributeNode(declaration);
/*      */     }
/*      */     catch (DOMException de) {
/*      */     }
/*  650 */     return true;
/*      */   }
/*      */ 
/*      */   public Iterator getChildElements() {
/*  654 */     return getChildElementsFrom(this);
/*      */   }
/*      */ 
/*      */   protected SOAPElement convertToSoapElement(Element element) {
/*  658 */     if ((element instanceof SOAPBodyElement)) {
/*  659 */       return (SOAPBodyElement)element;
/*      */     }
/*  661 */     return replaceElementWithSOAPElement(element, (ElementImpl)createElement(NameImpl.copyElementName(element)));
/*      */   }
/*      */ 
/*      */   protected static SOAPElement replaceElementWithSOAPElement(Element element, ElementImpl copy)
/*      */   {
/*  671 */     Iterator eachAttribute = getAllAttributesFrom(element);
/*  672 */     while (eachAttribute.hasNext()) {
/*  673 */       Name name = (Name)eachAttribute.next();
/*  674 */       copy.addAttributeBare(name, getAttributeValueFrom(element, name));
/*      */     }
/*      */ 
/*  677 */     Iterator eachChild = getChildElementsFrom(element);
/*  678 */     while (eachChild.hasNext()) {
/*  679 */       org.w3c.dom.Node nextChild = (org.w3c.dom.Node)eachChild.next();
/*  680 */       copy.insertBefore(nextChild, null);
/*      */     }
/*      */ 
/*  683 */     org.w3c.dom.Node parent = element.getParentNode();
/*  684 */     if (parent != null) {
/*  685 */       parent.replaceChild(copy, element);
/*      */     }
/*      */ 
/*  688 */     return copy;
/*      */   }
/*      */ 
/*      */   protected Iterator getChildElementNodes() {
/*  692 */     return new Iterator() {
/*  693 */       Iterator eachNode = ElementImpl.this.getChildElements();
/*  694 */       org.w3c.dom.Node next = null;
/*  695 */       org.w3c.dom.Node last = null;
/*      */ 
/*      */       public boolean hasNext() {
/*  698 */         if (this.next == null) {
/*  699 */           while (this.eachNode.hasNext()) {
/*  700 */             org.w3c.dom.Node node = (org.w3c.dom.Node)this.eachNode.next();
/*  701 */             if ((node instanceof SOAPElement)) {
/*  702 */               this.next = node;
/*  703 */               break;
/*      */             }
/*      */           }
/*      */         }
/*  707 */         return this.next != null;
/*      */       }
/*      */ 
/*      */       public Object next() {
/*  711 */         if (hasNext()) {
/*  712 */           this.last = this.next;
/*  713 */           this.next = null;
/*  714 */           return this.last;
/*      */         }
/*  716 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       public void remove() {
/*  720 */         if (this.last == null) {
/*  721 */           throw new IllegalStateException();
/*      */         }
/*  723 */         org.w3c.dom.Node target = this.last;
/*  724 */         this.last = null;
/*  725 */         ElementImpl.this.removeChild(target);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public Iterator getChildElements(Name name) {
/*  731 */     return getChildElements(name.getURI(), name.getLocalName());
/*      */   }
/*      */ 
/*      */   public Iterator getChildElements(QName qname) {
/*  735 */     return getChildElements(qname.getNamespaceURI(), qname.getLocalPart());
/*      */   }
/*      */ 
/*      */   private Iterator getChildElements(final String nameUri, final String nameLocal) {
/*  739 */     return new Iterator() {
/*  740 */       Iterator eachElement = ElementImpl.this.getChildElementNodes();
/*  741 */       org.w3c.dom.Node next = null;
/*  742 */       org.w3c.dom.Node last = null;
/*      */ 
/*      */       public boolean hasNext() {
/*  745 */         if (this.next == null) {
/*  746 */           while (this.eachElement.hasNext()) {
/*  747 */             org.w3c.dom.Node element = (org.w3c.dom.Node)this.eachElement.next();
/*  748 */             String elementUri = element.getNamespaceURI();
/*  749 */             elementUri = elementUri == null ? "" : elementUri;
/*  750 */             String elementName = element.getLocalName();
/*  751 */             if ((elementUri.equals(nameUri)) && (elementName.equals(nameLocal)))
/*      */             {
/*  753 */               this.next = element;
/*  754 */               break;
/*      */             }
/*      */           }
/*      */         }
/*  758 */         return this.next != null;
/*      */       }
/*      */ 
/*      */       public Object next() {
/*  762 */         if (!hasNext()) {
/*  763 */           throw new NoSuchElementException();
/*      */         }
/*  765 */         this.last = this.next;
/*  766 */         this.next = null;
/*  767 */         return this.last;
/*      */       }
/*      */ 
/*      */       public void remove() {
/*  771 */         if (this.last == null) {
/*  772 */           throw new IllegalStateException();
/*      */         }
/*  774 */         org.w3c.dom.Node target = this.last;
/*  775 */         this.last = null;
/*  776 */         ElementImpl.this.removeChild(target);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public void removeContents() {
/*  782 */     org.w3c.dom.Node currentChild = getFirstChild();
/*      */ 
/*  784 */     while (currentChild != null) {
/*  785 */       org.w3c.dom.Node temp = currentChild.getNextSibling();
/*  786 */       if ((currentChild instanceof javax.xml.soap.Node)) {
/*  787 */         ((javax.xml.soap.Node)currentChild).detachNode();
/*      */       } else {
/*  789 */         org.w3c.dom.Node parent = currentChild.getParentNode();
/*  790 */         if (parent != null) {
/*  791 */           parent.removeChild(currentChild);
/*      */         }
/*      */       }
/*      */ 
/*  795 */       currentChild = temp;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEncodingStyle(String encodingStyle) throws SOAPException {
/*  800 */     if (!"".equals(encodingStyle)) {
/*      */       try {
/*  802 */         uri = new JaxmURI(encodingStyle);
/*      */       }
/*      */       catch (JaxmURI.MalformedURIException m)
/*      */       {
/*      */         JaxmURI uri;
/*  804 */         log.log(Level.SEVERE, "SAAJ0105.impl.encoding.style.mustbe.valid.URI", new String[] { encodingStyle });
/*      */ 
/*  808 */         throw new IllegalArgumentException("Encoding style (" + encodingStyle + ") should be a valid URI");
/*      */       }
/*      */     }
/*      */ 
/*  812 */     this.encodingStyleAttribute.setValue(encodingStyle);
/*  813 */     tryToFindEncodingStyleAttributeName();
/*      */   }
/*      */ 
/*      */   public String getEncodingStyle() {
/*  817 */     String encodingStyle = this.encodingStyleAttribute.getValue();
/*  818 */     if (encodingStyle != null)
/*  819 */       return encodingStyle;
/*  820 */     String soapNamespace = getSOAPNamespace();
/*  821 */     if (soapNamespace != null) {
/*  822 */       Attr attr = getAttributeNodeNS(soapNamespace, "encodingStyle");
/*  823 */       if (attr != null) {
/*  824 */         encodingStyle = attr.getValue();
/*      */         try {
/*  826 */           setEncodingStyle(encodingStyle);
/*      */         }
/*      */         catch (SOAPException se) {
/*      */         }
/*  830 */         return encodingStyle;
/*      */       }
/*      */     }
/*  833 */     return null;
/*      */   }
/*      */ 
/*      */   public String getValue()
/*      */   {
/*  838 */     javax.xml.soap.Node valueNode = getValueNode();
/*  839 */     return valueNode == null ? null : valueNode.getValue();
/*      */   }
/*      */ 
/*      */   public void setValue(String value) {
/*  843 */     org.w3c.dom.Node valueNode = getValueNodeStrict();
/*  844 */     if (valueNode != null)
/*  845 */       valueNode.setNodeValue(value);
/*      */     else
/*      */       try {
/*  848 */         addTextNode(value);
/*      */       } catch (SOAPException e) {
/*  850 */         throw new RuntimeException(e.getMessage());
/*      */       }
/*      */   }
/*      */ 
/*      */   protected org.w3c.dom.Node getValueNodeStrict()
/*      */   {
/*  856 */     org.w3c.dom.Node node = getFirstChild();
/*  857 */     if (node != null) {
/*  858 */       if ((node.getNextSibling() == null) && (node.getNodeType() == 3))
/*      */       {
/*  860 */         return node;
/*      */       }
/*  862 */       log.severe("SAAJ0107.impl.elem.child.not.single.text");
/*  863 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*  867 */     return null;
/*      */   }
/*      */ 
/*      */   protected javax.xml.soap.Node getValueNode() {
/*  871 */     Iterator i = getChildElements();
/*  872 */     while (i.hasNext()) {
/*  873 */       javax.xml.soap.Node n = (javax.xml.soap.Node)i.next();
/*  874 */       if ((n.getNodeType() == 3) || (n.getNodeType() == 4))
/*      */       {
/*  877 */         normalize();
/*      */ 
/*  880 */         return n;
/*      */       }
/*      */     }
/*  883 */     return null;
/*      */   }
/*      */ 
/*      */   public void setParentElement(SOAPElement element) throws SOAPException {
/*  887 */     if (element == null) {
/*  888 */       log.severe("SAAJ0106.impl.no.null.to.parent.elem");
/*  889 */       throw new SOAPException("Cannot pass NULL to setParentElement");
/*      */     }
/*  891 */     element.addChildElement(this);
/*  892 */     findEncodingStyleAttributeName();
/*      */   }
/*      */ 
/*      */   protected void findEncodingStyleAttributeName() throws SOAPException {
/*  896 */     String soapNamespace = getSOAPNamespace();
/*  897 */     if (soapNamespace != null) {
/*  898 */       String soapNamespacePrefix = getNamespacePrefix(soapNamespace);
/*  899 */       if (soapNamespacePrefix != null)
/*  900 */         setEncodingStyleNamespace(soapNamespace, soapNamespacePrefix);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setEncodingStyleNamespace(String soapNamespace, String soapNamespacePrefix)
/*      */     throws SOAPException
/*      */   {
/*  909 */     Name encodingStyleAttributeName = NameImpl.create("encodingStyle", soapNamespacePrefix, soapNamespace);
/*      */ 
/*  914 */     this.encodingStyleAttribute.setName(encodingStyleAttributeName);
/*      */   }
/*      */ 
/*      */   public SOAPElement getParentElement() {
/*  918 */     org.w3c.dom.Node parentNode = getParentNode();
/*  919 */     if ((parentNode instanceof SOAPDocument)) {
/*  920 */       return null;
/*      */     }
/*  922 */     return (SOAPBodyElement)parentNode;
/*      */   }
/*      */ 
/*      */   protected String getSOAPNamespace() {
/*  926 */     String soapNamespace = null;
/*      */ 
/*  928 */     SOAPElement antecedent = this;
/*  929 */     while (antecedent != null) {
/*  930 */       Name antecedentName = antecedent.getElementName();
/*  931 */       String antecedentNamespace = antecedentName.getURI();
/*      */ 
/*  933 */       if (("http://schemas.xmlsoap.org/soap/envelope/".equals(antecedentNamespace)) || ("http://www.w3.org/2003/05/soap-envelope".equals(antecedentNamespace)))
/*      */       {
/*  936 */         soapNamespace = antecedentNamespace;
/*  937 */         break;
/*      */       }
/*      */ 
/*  940 */       antecedent = antecedent.getParentElement();
/*      */     }
/*      */ 
/*  943 */     return soapNamespace;
/*      */   }
/*      */ 
/*      */   public void detachNode() {
/*  947 */     org.w3c.dom.Node parent = getParentNode();
/*  948 */     if (parent != null) {
/*  949 */       parent.removeChild(this);
/*      */     }
/*  951 */     this.encodingStyleAttribute.clearNameAndValue();
/*      */   }
/*      */ 
/*      */   public void tryToFindEncodingStyleAttributeName()
/*      */   {
/*      */     try
/*      */     {
/*  958 */       findEncodingStyleAttributeName();
/*      */     } catch (SOAPException e) {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void recycleNode() {
/*  964 */     detachNode();
/*      */   }
/*      */ 
/*      */   protected static Attr getNamespaceAttrFrom(Element element, String prefix)
/*      */   {
/* 1018 */     NamespaceContextIterator eachNamespace = new NamespaceContextIterator(element);
/*      */ 
/* 1020 */     while (eachNamespace.hasNext()) {
/* 1021 */       Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
/* 1022 */       String declaredPrefix = NameImpl.getLocalNameFromTagName(namespaceDecl.getNodeName());
/*      */ 
/* 1024 */       if (declaredPrefix.equals(prefix)) {
/* 1025 */         return namespaceDecl;
/*      */       }
/*      */     }
/* 1028 */     return null;
/*      */   }
/*      */ 
/*      */   protected static Iterator getAllAttributesFrom(Element element) {
/* 1032 */     NamedNodeMap attributes = element.getAttributes();
/*      */ 
/* 1034 */     return new Iterator() { int attributesLength = this.val$attributes.getLength();
/* 1036 */       int attributeIndex = 0;
/*      */       String currentName;
/*      */ 
/* 1040 */       public boolean hasNext() { return this.attributeIndex < this.attributesLength; }
/*      */ 
/*      */       public Object next()
/*      */       {
/* 1044 */         if (!hasNext()) {
/* 1045 */           throw new NoSuchElementException();
/*      */         }
/* 1047 */         org.w3c.dom.Node current = this.val$attributes.item(this.attributeIndex++);
/* 1048 */         this.currentName = current.getNodeName();
/*      */ 
/* 1050 */         String prefix = NameImpl.getPrefixFromTagName(this.currentName);
/* 1051 */         if (prefix.length() == 0) {
/* 1052 */           return NameImpl.createFromUnqualifiedName(this.currentName);
/*      */         }
/* 1054 */         Name attributeName = NameImpl.createFromQualifiedName(this.currentName, current.getNamespaceURI());
/*      */ 
/* 1058 */         return attributeName;
/*      */       }
/*      */ 
/*      */       public void remove()
/*      */       {
/* 1063 */         if (this.currentName == null) {
/* 1064 */           throw new IllegalStateException();
/*      */         }
/* 1066 */         this.val$attributes.removeNamedItem(this.currentName);
/*      */       } } ;
/*      */   }
/*      */ 
/*      */   protected static String getAttributeValueFrom(Element element, Name name)
/*      */   {
/* 1072 */     return getAttributeValueFrom(element, name.getURI(), name.getLocalName(), name.getPrefix(), name.getQualifiedName());
/*      */   }
/*      */ 
/*      */   private static String getAttributeValueFrom(Element element, String uri, String localName, String prefix, String qualifiedName)
/*      */   {
/* 1087 */     String nonzeroLengthUri = (uri == null) || (uri.length() == 0) ? null : uri;
/*      */ 
/* 1090 */     boolean mustUseGetAttributeNodeNS = nonzeroLengthUri != null;
/*      */ 
/* 1092 */     if (mustUseGetAttributeNodeNS)
/*      */     {
/* 1094 */       if (!element.hasAttributeNS(uri, localName)) {
/* 1095 */         return null;
/*      */       }
/*      */ 
/* 1098 */       String attrValue = element.getAttributeNS(nonzeroLengthUri, localName);
/*      */ 
/* 1101 */       return attrValue;
/*      */     }
/*      */ 
/* 1104 */     Attr attribute = null;
/* 1105 */     attribute = element.getAttributeNode(qualifiedName);
/*      */ 
/* 1107 */     return attribute == null ? null : attribute.getValue();
/*      */   }
/*      */ 
/*      */   protected static Iterator getChildElementsFrom(Element element) {
/* 1111 */     return new Iterator() {
/* 1112 */       org.w3c.dom.Node next = this.val$element.getFirstChild();
/* 1113 */       org.w3c.dom.Node nextNext = null;
/* 1114 */       org.w3c.dom.Node last = null;
/*      */ 
/*      */       public boolean hasNext() {
/* 1117 */         if (this.next != null) {
/* 1118 */           return true;
/*      */         }
/* 1120 */         if ((this.next == null) && (this.nextNext != null)) {
/* 1121 */           this.next = this.nextNext;
/*      */         }
/*      */ 
/* 1124 */         return this.next != null;
/*      */       }
/*      */ 
/*      */       public Object next() {
/* 1128 */         if (hasNext()) {
/* 1129 */           this.last = this.next;
/* 1130 */           this.next = null;
/*      */ 
/* 1132 */           if (((this.val$element instanceof ElementImpl)) && ((this.last instanceof Element)))
/*      */           {
/* 1134 */             this.last = ((ElementImpl)this.val$element).convertToSoapElement((Element)this.last);
/*      */           }
/*      */ 
/* 1139 */           this.nextNext = this.last.getNextSibling();
/* 1140 */           return this.last;
/*      */         }
/* 1142 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       public void remove() {
/* 1146 */         if (this.last == null) {
/* 1147 */           throw new IllegalStateException();
/*      */         }
/* 1149 */         org.w3c.dom.Node target = this.last;
/* 1150 */         this.last = null;
/* 1151 */         this.val$element.removeChild(target);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static String getQualifiedName(QName name) {
/* 1157 */     String prefix = name.getPrefix();
/* 1158 */     String localName = name.getLocalPart();
/* 1159 */     String qualifiedName = null;
/*      */ 
/* 1161 */     if ((prefix != null) && (prefix.length() > 0))
/* 1162 */       qualifiedName = prefix + ":" + localName;
/*      */     else {
/* 1164 */       qualifiedName = localName;
/*      */     }
/* 1166 */     return qualifiedName;
/*      */   }
/*      */ 
/*      */   public static String getLocalPart(String qualifiedName) {
/* 1170 */     if (qualifiedName == null)
/*      */     {
/* 1172 */       throw new IllegalArgumentException("Cannot get local name for a \"null\" qualified name");
/*      */     }
/*      */ 
/* 1175 */     int index = qualifiedName.indexOf(':');
/* 1176 */     if (index < 0) {
/* 1177 */       return qualifiedName;
/*      */     }
/* 1179 */     return qualifiedName.substring(index + 1);
/*      */   }
/*      */ 
/*      */   public static String getPrefix(String qualifiedName) {
/* 1183 */     if (qualifiedName == null)
/*      */     {
/* 1185 */       throw new IllegalArgumentException("Cannot get prefix for a  \"null\" qualified name");
/*      */     }
/*      */ 
/* 1188 */     int index = qualifiedName.indexOf(':');
/* 1189 */     if (index < 0) {
/* 1190 */       return "";
/*      */     }
/* 1192 */     return qualifiedName.substring(0, index);
/*      */   }
/*      */ 
/*      */   protected boolean isNamespaceQualified(Name name) {
/* 1196 */     return !"".equals(name.getURI());
/*      */   }
/*      */ 
/*      */   protected boolean isNamespaceQualified(QName name) {
/* 1200 */     return !"".equals(name.getNamespaceURI());
/*      */   }
/*      */ 
/*      */   protected SOAPElement circumventBug5034339(SOAPElement element)
/*      */   {
/* 1205 */     Name elementName = element.getElementName();
/* 1206 */     if (!isNamespaceQualified(elementName)) {
/* 1207 */       String prefix = elementName.getPrefix();
/* 1208 */       String defaultNamespace = getNamespaceURI(prefix);
/* 1209 */       if (defaultNamespace != null) {
/* 1210 */         Name newElementName = NameImpl.create(elementName.getLocalName(), elementName.getPrefix(), defaultNamespace);
/*      */ 
/* 1215 */         SOAPElement newElement = createElement(newElementName);
/* 1216 */         replaceChild(newElement, element);
/* 1217 */         return newElement;
/*      */       }
/*      */     }
/* 1220 */     return element;
/*      */   }
/*      */ 
/*      */   public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
/*      */   {
/* 1228 */     int index = qualifiedName.indexOf(':');
/*      */     String localName;
/*      */     String localName;
/* 1230 */     if (index < 0) {
/* 1231 */       String prefix = null;
/* 1232 */       localName = qualifiedName;
/*      */     }
/*      */     else {
/* 1235 */       String prefix = qualifiedName.substring(0, index);
/* 1236 */       localName = qualifiedName.substring(index + 1);
/*      */     }
/*      */ 
/* 1249 */     super.setAttributeNS(namespaceURI, qualifiedName, value);
/*      */ 
/* 1251 */     String tmpURI = getNamespaceURI();
/* 1252 */     boolean isIDNS = false;
/* 1253 */     if ((tmpURI != null) && ((tmpURI.equals(DSIG_NS)) || (tmpURI.equals(XENC_NS)))) {
/* 1254 */       isIDNS = true;
/*      */     }
/*      */ 
/* 1258 */     if (localName.equals("Id"))
/* 1259 */       if ((namespaceURI == null) || (namespaceURI.equals("")))
/* 1260 */         setIdAttribute(localName, true);
/* 1261 */       else if ((isIDNS) || (WSU_NS.equals(namespaceURI)))
/* 1262 */         setIdAttributeNS(namespaceURI, localName, true);
/*      */   }
/*      */ 
/*      */   class AttributeManager
/*      */   {
/*  971 */     Name attributeName = null;
/*  972 */     String attributeValue = null;
/*      */ 
/*      */     AttributeManager() {  } 
/*  975 */     public void setName(Name newName) throws SOAPException { clearAttribute();
/*  976 */       this.attributeName = newName;
/*  977 */       reconcileAttribute(); }
/*      */ 
/*      */     public void clearName() {
/*  980 */       clearAttribute();
/*  981 */       this.attributeName = null;
/*      */     }
/*      */     public void setValue(String value) throws SOAPException {
/*  984 */       this.attributeValue = value;
/*  985 */       reconcileAttribute();
/*      */     }
/*      */     public Name getName() {
/*  988 */       return this.attributeName;
/*      */     }
/*      */     public String getValue() {
/*  991 */       return this.attributeValue;
/*      */     }
/*      */ 
/*      */     public void clearNameAndValue()
/*      */     {
/*  996 */       this.attributeName = null;
/*  997 */       this.attributeValue = null;
/*      */     }
/*      */ 
/*      */     private void reconcileAttribute() throws SOAPException {
/* 1001 */       if (this.attributeName != null) {
/* 1002 */         ElementImpl.this.removeAttribute(this.attributeName);
/* 1003 */         if (this.attributeValue != null)
/* 1004 */           ElementImpl.this.addAttribute(this.attributeName, this.attributeValue);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void clearAttribute() {
/* 1009 */       if (this.attributeName != null)
/* 1010 */         ElementImpl.this.removeAttribute(this.attributeName);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
 * JD-Core Version:    0.6.2
 */