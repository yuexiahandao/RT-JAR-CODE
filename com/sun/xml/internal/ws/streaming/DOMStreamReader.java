/*     */ package com.sun.xml.internal.ws.streaming;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.XMLStreamException2;
/*     */ import com.sun.xml.internal.ws.util.xml.DummyLocation;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public final class DOMStreamReader
/*     */   implements XMLStreamReader, NamespaceContext
/*     */ {
/*     */   private Node _current;
/*     */   private Node _start;
/*     */   private NamedNodeMap _namedNodeMap;
/*     */   private String wholeText;
/* 104 */   private final FinalArrayList<Attr> _currentAttributes = new FinalArrayList();
/*     */ 
/* 109 */   private Scope[] scopes = new Scope[8];
/*     */ 
/* 115 */   private int depth = 0;
/*     */   int _state;
/*     */ 
/*     */   public DOMStreamReader()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMStreamReader(Node node)
/*     */   {
/* 229 */     setCurrentNode(node);
/*     */   }
/*     */ 
/*     */   public void setCurrentNode(Node node) {
/* 233 */     this.scopes[0] = new Scope(null);
/* 234 */     this.depth = 0;
/*     */ 
/* 236 */     this._start = (this._current = node);
/* 237 */     this._state = 7;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   private void splitAttributes()
/*     */   {
/* 252 */     this._currentAttributes.clear();
/*     */ 
/* 254 */     Scope scope = allocateScope();
/*     */ 
/* 256 */     this._namedNodeMap = this._current.getAttributes();
/* 257 */     if (this._namedNodeMap != null) {
/* 258 */       int n = this._namedNodeMap.getLength();
/* 259 */       for (int i = 0; i < n; i++) {
/* 260 */         Attr attr = (Attr)this._namedNodeMap.item(i);
/* 261 */         String attrName = attr.getNodeName();
/* 262 */         if ((attrName.startsWith("xmlns:")) || (attrName.equals("xmlns"))) {
/* 263 */           scope.currentNamespaces.add(attr);
/*     */         }
/*     */         else {
/* 266 */           this._currentAttributes.add(attr);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 272 */     ensureNs(this._current);
/* 273 */     for (int i = this._currentAttributes.size() - 1; i >= 0; i--) {
/* 274 */       Attr a = (Attr)this._currentAttributes.get(i);
/* 275 */       if (fixNull(a.getNamespaceURI()).length() > 0)
/* 276 */         ensureNs(a);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void ensureNs(Node n)
/*     */   {
/* 291 */     String prefix = fixNull(n.getPrefix());
/* 292 */     String uri = fixNull(n.getNamespaceURI());
/*     */ 
/* 294 */     Scope scope = this.scopes[this.depth];
/*     */ 
/* 296 */     String currentUri = scope.getNamespaceURI(prefix);
/*     */ 
/* 298 */     if (prefix.length() == 0) {
/* 299 */       currentUri = fixNull(currentUri);
/* 300 */       if (!currentUri.equals(uri));
/*     */     }
/* 303 */     else if ((currentUri != null) && (currentUri.equals(uri))) {
/* 304 */       return;
/*     */     }
/*     */ 
/* 307 */     if ((prefix.equals("xml")) || (prefix.equals("xmlns"))) {
/* 308 */       return;
/*     */     }
/*     */ 
/* 311 */     scope.additionalNamespaces.add(prefix);
/* 312 */     scope.additionalNamespaces.add(uri);
/*     */   }
/*     */ 
/*     */   private Scope allocateScope()
/*     */   {
/* 319 */     if (this.scopes.length == ++this.depth) {
/* 320 */       Scope[] newBuf = new Scope[this.scopes.length * 2];
/* 321 */       System.arraycopy(this.scopes, 0, newBuf, 0, this.scopes.length);
/* 322 */       this.scopes = newBuf;
/*     */     }
/* 324 */     Scope scope = this.scopes[this.depth];
/* 325 */     if (scope == null)
/* 326 */       scope = this.scopes[this.depth] =  = new Scope(this.scopes[(this.depth - 1)]);
/*     */     else {
/* 328 */       scope.reset();
/*     */     }
/* 330 */     return scope;
/*     */   }
/*     */ 
/*     */   public int getAttributeCount() {
/* 334 */     if (this._state == 1)
/* 335 */       return this._currentAttributes.size();
/* 336 */     throw new IllegalStateException("DOMStreamReader: getAttributeCount() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributeLocalName(int index)
/*     */   {
/* 343 */     if (this._state == 1) {
/* 344 */       String localName = ((Attr)this._currentAttributes.get(index)).getLocalName();
/* 345 */       return localName != null ? localName : QName.valueOf(((Attr)this._currentAttributes.get(index)).getNodeName()).getLocalPart();
/*     */     }
/*     */ 
/* 348 */     throw new IllegalStateException("DOMStreamReader: getAttributeLocalName() called in illegal state");
/*     */   }
/*     */ 
/*     */   public QName getAttributeName(int index)
/*     */   {
/* 355 */     if (this._state == 1) {
/* 356 */       Node attr = (Node)this._currentAttributes.get(index);
/* 357 */       String localName = attr.getLocalName();
/* 358 */       if (localName != null) {
/* 359 */         String prefix = attr.getPrefix();
/* 360 */         String uri = attr.getNamespaceURI();
/* 361 */         return new QName(fixNull(uri), localName, fixNull(prefix));
/*     */       }
/*     */ 
/* 364 */       return QName.valueOf(attr.getNodeName());
/*     */     }
/*     */ 
/* 367 */     throw new IllegalStateException("DOMStreamReader: getAttributeName() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributeNamespace(int index) {
/* 371 */     if (this._state == 1) {
/* 372 */       String uri = ((Attr)this._currentAttributes.get(index)).getNamespaceURI();
/* 373 */       return fixNull(uri);
/*     */     }
/* 375 */     throw new IllegalStateException("DOMStreamReader: getAttributeNamespace() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributePrefix(int index) {
/* 379 */     if (this._state == 1) {
/* 380 */       String prefix = ((Attr)this._currentAttributes.get(index)).getPrefix();
/* 381 */       return fixNull(prefix);
/*     */     }
/* 383 */     throw new IllegalStateException("DOMStreamReader: getAttributePrefix() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributeType(int index) {
/* 387 */     if (this._state == 1) {
/* 388 */       return "CDATA";
/*     */     }
/* 390 */     throw new IllegalStateException("DOMStreamReader: getAttributeType() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(int index) {
/* 394 */     if (this._state == 1) {
/* 395 */       return ((Attr)this._currentAttributes.get(index)).getNodeValue();
/*     */     }
/* 397 */     throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(String namespaceURI, String localName) {
/* 401 */     if (this._state == 1) {
/* 402 */       if (this._namedNodeMap != null) {
/* 403 */         Node attr = this._namedNodeMap.getNamedItemNS(namespaceURI, localName);
/* 404 */         return attr != null ? attr.getNodeValue() : null;
/*     */       }
/* 406 */       return null;
/*     */     }
/* 408 */     throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme() {
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */   public String getElementText() throws XMLStreamException {
/* 416 */     throw new RuntimeException("DOMStreamReader: getElementText() not implemented");
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 420 */     return null;
/*     */   }
/*     */ 
/*     */   public int getEventType() {
/* 424 */     return this._state;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 431 */     if ((this._state == 1) || (this._state == 2)) {
/* 432 */       String localName = this._current.getLocalName();
/* 433 */       return localName != null ? localName : QName.valueOf(this._current.getNodeName()).getLocalPart();
/*     */     }
/*     */ 
/* 436 */     if (this._state == 9) {
/* 437 */       return this._current.getNodeName();
/*     */     }
/* 439 */     throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 443 */     return DummyLocation.INSTANCE;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/* 450 */     if ((this._state == 1) || (this._state == 2)) {
/* 451 */       String localName = this._current.getLocalName();
/* 452 */       if (localName != null) {
/* 453 */         String prefix = this._current.getPrefix();
/* 454 */         String uri = this._current.getNamespaceURI();
/* 455 */         return new QName(fixNull(uri), localName, fixNull(prefix));
/*     */       }
/*     */ 
/* 458 */       return QName.valueOf(this._current.getNodeName());
/*     */     }
/*     */ 
/* 461 */     throw new IllegalStateException("DOMStreamReader: getName() called in illegal state");
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 465 */     return this;
/*     */   }
/*     */ 
/*     */   private Scope getCheckedScope()
/*     */   {
/* 475 */     if ((this._state == 1) || (this._state == 2)) {
/* 476 */       return this.scopes[this.depth];
/*     */     }
/* 478 */     throw new IllegalStateException("DOMStreamReader: neither on START_ELEMENT nor END_ELEMENT");
/*     */   }
/*     */ 
/*     */   public int getNamespaceCount() {
/* 482 */     return getCheckedScope().getNamespaceCount();
/*     */   }
/*     */ 
/*     */   public String getNamespacePrefix(int index) {
/* 486 */     return getCheckedScope().getNamespacePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int index) {
/* 490 */     return getCheckedScope().getNamespaceURI(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 494 */     if ((this._state == 1) || (this._state == 2)) {
/* 495 */       String uri = this._current.getNamespaceURI();
/* 496 */       return fixNull(uri);
/*     */     }
/* 498 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/* 507 */     if (prefix == null) {
/* 508 */       throw new IllegalArgumentException("DOMStreamReader: getNamespaceURI(String) call with a null prefix");
/*     */     }
/* 510 */     if (prefix.equals("xml")) {
/* 511 */       return "http://www.w3.org/XML/1998/namespace";
/*     */     }
/* 513 */     if (prefix.equals("xmlns")) {
/* 514 */       return "http://www.w3.org/2000/xmlns/";
/*     */     }
/*     */ 
/* 518 */     String nsUri = this.scopes[this.depth].getNamespaceURI(prefix);
/* 519 */     if (nsUri != null) return nsUri;
/*     */ 
/* 522 */     Node node = findRootElement();
/* 523 */     String nsDeclName = "xmlns:" + prefix;
/* 524 */     while (node.getNodeType() != 9)
/*     */     {
/* 526 */       NamedNodeMap namedNodeMap = node.getAttributes();
/* 527 */       Attr attr = (Attr)namedNodeMap.getNamedItem(nsDeclName);
/* 528 */       if (attr != null)
/* 529 */         return attr.getValue();
/* 530 */       node = node.getParentNode();
/*     */     }
/* 532 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix(String nsUri) {
/* 536 */     if (nsUri == null) {
/* 537 */       throw new IllegalArgumentException("DOMStreamReader: getPrefix(String) call with a null namespace URI");
/*     */     }
/* 539 */     if (nsUri.equals("http://www.w3.org/XML/1998/namespace")) {
/* 540 */       return "xml";
/*     */     }
/* 542 */     if (nsUri.equals("http://www.w3.org/2000/xmlns/")) {
/* 543 */       return "xmlns";
/*     */     }
/*     */ 
/* 547 */     String prefix = this.scopes[this.depth].getPrefix(nsUri);
/* 548 */     if (prefix != null) return prefix;
/*     */ 
/* 551 */     Node node = findRootElement();
/*     */ 
/* 553 */     while (node.getNodeType() != 9)
/*     */     {
/* 555 */       NamedNodeMap namedNodeMap = node.getAttributes();
/* 556 */       for (int i = namedNodeMap.getLength() - 1; i >= 0; i--) {
/* 557 */         Attr attr = (Attr)namedNodeMap.item(i);
/* 558 */         prefix = getPrefixForAttr(attr, nsUri);
/* 559 */         if (prefix != null)
/* 560 */           return prefix;
/*     */       }
/* 562 */       node = node.getParentNode();
/*     */     }
/* 564 */     return null;
/*     */   }
/*     */ 
/*     */   private Node findRootElement()
/*     */   {
/* 573 */     Node node = this._start;
/*     */     int type;
/* 575 */     while (((type = node.getNodeType()) != 9) && (type != 1)) {
/* 576 */       node = node.getParentNode();
/*     */     }
/* 578 */     return node;
/*     */   }
/*     */ 
/*     */   private static String getPrefixForAttr(Attr attr, String nsUri)
/*     */   {
/* 586 */     String attrName = attr.getNodeName();
/* 587 */     if ((!attrName.startsWith("xmlns:")) && (!attrName.equals("xmlns"))) {
/* 588 */       return null;
/*     */     }
/* 590 */     if (attr.getValue().equals(nsUri)) {
/* 591 */       if (attrName.equals("xmlns"))
/* 592 */         return "";
/* 593 */       String localName = attr.getLocalName();
/* 594 */       return localName != null ? localName : QName.valueOf(attrName).getLocalPart();
/*     */     }
/*     */ 
/* 598 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator getPrefixes(String nsUri)
/*     */   {
/* 604 */     String prefix = getPrefix(nsUri);
/* 605 */     if (prefix == null) return Collections.emptyList().iterator();
/* 606 */     return Collections.singletonList(prefix).iterator();
/*     */   }
/*     */ 
/*     */   public String getPIData() {
/* 610 */     if (this._state == 3) {
/* 611 */       return ((ProcessingInstruction)this._current).getData();
/*     */     }
/* 613 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPITarget() {
/* 617 */     if (this._state == 3) {
/* 618 */       return ((ProcessingInstruction)this._current).getTarget();
/*     */     }
/* 620 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 624 */     if ((this._state == 1) || (this._state == 2)) {
/* 625 */       String prefix = this._current.getPrefix();
/* 626 */       return fixNull(prefix);
/*     */     }
/* 628 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String str) throws IllegalArgumentException {
/* 632 */     return null;
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 636 */     if (this._state == 4)
/* 637 */       return this.wholeText;
/* 638 */     if ((this._state == 12) || (this._state == 5) || (this._state == 9))
/* 639 */       return this._current.getNodeValue();
/* 640 */     throw new IllegalStateException("DOMStreamReader: getTextLength() called in illegal state");
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters() {
/* 644 */     return getText().toCharArray();
/*     */   }
/*     */ 
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int targetLength) throws XMLStreamException
/*     */   {
/* 649 */     String text = getText();
/* 650 */     int copiedSize = Math.min(targetLength, text.length() - sourceStart);
/* 651 */     text.getChars(sourceStart, sourceStart + copiedSize, target, targetStart);
/*     */ 
/* 653 */     return copiedSize;
/*     */   }
/*     */ 
/*     */   public int getTextLength() {
/* 657 */     return getText().length();
/*     */   }
/*     */ 
/*     */   public int getTextStart() {
/* 661 */     if ((this._state == 4) || (this._state == 12) || (this._state == 5) || (this._state == 9)) {
/* 662 */       return 0;
/*     */     }
/* 664 */     throw new IllegalStateException("DOMStreamReader: getTextStart() called in illegal state");
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 668 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasName() {
/* 672 */     return (this._state == 1) || (this._state == 2);
/*     */   }
/*     */ 
/*     */   public boolean hasNext() throws XMLStreamException {
/* 676 */     return this._state != 8;
/*     */   }
/*     */ 
/*     */   public boolean hasText() {
/* 680 */     if ((this._state == 4) || (this._state == 12) || (this._state == 5) || (this._state == 9)) {
/* 681 */       return getText().trim().length() > 0;
/*     */     }
/* 683 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isAttributeSpecified(int param) {
/* 687 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCharacters() {
/* 691 */     return this._state == 4;
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/* 695 */     return this._state == 2;
/*     */   }
/*     */ 
/*     */   public boolean isStandalone() {
/* 699 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isStartElement() {
/* 703 */     return this._state == 1;
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace() {
/* 707 */     if ((this._state == 4) || (this._state == 12))
/* 708 */       return getText().trim().length() == 0;
/* 709 */     return false;
/*     */   }
/*     */ 
/*     */   private static int mapNodeTypeToState(int nodetype) {
/* 713 */     switch (nodetype) {
/*     */     case 4:
/* 715 */       return 12;
/*     */     case 8:
/* 717 */       return 5;
/*     */     case 1:
/* 719 */       return 1;
/*     */     case 6:
/* 721 */       return 15;
/*     */     case 5:
/* 723 */       return 9;
/*     */     case 12:
/* 725 */       return 14;
/*     */     case 7:
/* 727 */       return 3;
/*     */     case 3:
/* 729 */       return 4;
/*     */     case 2:
/*     */     case 9:
/*     */     case 10:
/* 731 */     case 11: } throw new RuntimeException("DOMStreamReader: Unexpected node type");
/*     */   }
/*     */   public int next() throws XMLStreamException {
/*     */     int r;
/*     */     do {
/*     */       Node prev;
/*     */       do { r = _next();
/* 738 */         switch (r)
/*     */         {
/*     */         case 4:
/* 741 */           prev = this._current.getPreviousSibling();
/* 742 */         case 1: }  } while ((prev != null) && (prev.getNodeType() == 3));
/*     */ 
/* 745 */       Text t = (Text)this._current;
/* 746 */       this.wholeText = t.getWholeText();
/* 747 */     }while (this.wholeText.length() == 0);
/*     */ 
/* 749 */     return 4;
/*     */ 
/* 751 */     splitAttributes();
/* 752 */     return 1;
/*     */ 
/* 754 */     return r;
/*     */   }
/*     */ 
/*     */   private int _next()
/*     */     throws XMLStreamException
/*     */   {
/*     */     Node child;
/* 762 */     switch (this._state) {
/*     */     case 8:
/* 764 */       throw new IllegalStateException("DOMStreamReader: Calling next() at END_DOCUMENT");
/*     */     case 7:
/* 767 */       if (this._current.getNodeType() == 1) {
/* 768 */         return this._state = 1;
/*     */       }
/*     */ 
/* 771 */       child = this._current.getFirstChild();
/* 772 */       if (child == null) {
/* 773 */         return this._state = 8;
/*     */       }
/*     */ 
/* 776 */       this._current = child;
/* 777 */       return this._state = mapNodeTypeToState(this._current.getNodeType());
/*     */     case 1:
/* 780 */       child = this._current.getFirstChild();
/* 781 */       if (child == null) {
/* 782 */         return this._state = 2;
/*     */       }
/*     */ 
/* 785 */       this._current = child;
/* 786 */       return this._state = mapNodeTypeToState(this._current.getNodeType());
/*     */     case 2:
/* 789 */       this.depth -= 1;
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 9:
/*     */     case 12:
/* 797 */       if (this._current == this._start) {
/* 798 */         return this._state = 8;
/*     */       }
/*     */ 
/* 801 */       Node sibling = this._current.getNextSibling();
/* 802 */       if (sibling == null) {
/* 803 */         this._current = this._current.getParentNode();
/*     */ 
/* 805 */         this._state = ((this._current == null) || (this._current.getNodeType() == 9) ? 8 : 2);
/*     */ 
/* 807 */         return this._state;
/*     */       }
/*     */ 
/* 810 */       this._current = sibling;
/* 811 */       return this._state = mapNodeTypeToState(this._current.getNodeType());
/*     */     case 6:
/*     */     case 10:
/*     */     case 11:
/*     */     case 13:
/*     */     }
/* 817 */     throw new RuntimeException("DOMStreamReader: Unexpected internal state");
/*     */   }
/*     */ 
/*     */   public int nextTag() throws XMLStreamException
/*     */   {
/* 822 */     int eventType = next();
/*     */ 
/* 827 */     while (((eventType == 4) && (isWhiteSpace())) || ((eventType == 12) && (isWhiteSpace())) || (eventType == 6) || (eventType == 3) || (eventType == 5))
/*     */     {
/* 829 */       eventType = next();
/*     */     }
/* 831 */     if ((eventType != 1) && (eventType != 2)) {
/* 832 */       throw new XMLStreamException2("DOMStreamReader: Expected start or end tag");
/*     */     }
/* 834 */     return eventType;
/*     */   }
/*     */ 
/*     */   public void require(int type, String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 840 */     if (type != this._state) {
/* 841 */       throw new XMLStreamException2("DOMStreamReader: Required event type not found");
/*     */     }
/* 843 */     if ((namespaceURI != null) && (!namespaceURI.equals(getNamespaceURI()))) {
/* 844 */       throw new XMLStreamException2("DOMStreamReader: Required namespaceURI not found");
/*     */     }
/* 846 */     if ((localName != null) && (!localName.equals(getLocalName())))
/* 847 */       throw new XMLStreamException2("DOMStreamReader: Required localName not found");
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet()
/*     */   {
/* 852 */     return true;
/*     */   }
/*     */ 
/*     */   private static void displayDOM(Node node, OutputStream ostream)
/*     */   {
/*     */     try
/*     */     {
/* 861 */       System.out.println("\n====\n");
/* 862 */       XmlUtil.newTransformer().transform(new DOMSource(node), new StreamResult(ostream));
/*     */ 
/* 864 */       System.out.println("\n====\n");
/*     */     }
/*     */     catch (Exception e) {
/* 867 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void verifyDOMIntegrity(Node node) {
/* 872 */     switch (node.getNodeType())
/*     */     {
/*     */     case 1:
/*     */     case 2:
/* 877 */       if (node.getLocalName() == null) {
/* 878 */         System.out.println("WARNING: DOM level 1 node found");
/* 879 */         System.out.println(" -> node.getNodeName() = " + node.getNodeName());
/* 880 */         System.out.println(" -> node.getNamespaceURI() = " + node.getNamespaceURI());
/* 881 */         System.out.println(" -> node.getLocalName() = " + node.getLocalName());
/* 882 */         System.out.println(" -> node.getPrefix() = " + node.getPrefix());
/*     */       }
/*     */ 
/* 885 */       if (node.getNodeType() == 2) return;
/*     */ 
/* 887 */       NamedNodeMap attrs = node.getAttributes();
/* 888 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 889 */         verifyDOMIntegrity(attrs.item(i));
/*     */       }
/*     */     case 9:
/* 892 */       NodeList children = node.getChildNodes();
/* 893 */       for (int i = 0; i < children.getLength(); i++)
/* 894 */         verifyDOMIntegrity(children.item(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String fixNull(String s)
/*     */   {
/* 901 */     if (s == null) return "";
/* 902 */     return s;
/*     */   }
/*     */ 
/*     */   private static final class Scope
/*     */   {
/*     */     final Scope parent;
/* 137 */     final FinalArrayList<Attr> currentNamespaces = new FinalArrayList();
/*     */ 
/* 145 */     final FinalArrayList<String> additionalNamespaces = new FinalArrayList();
/*     */ 
/*     */     Scope(Scope parent) {
/* 148 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     void reset() {
/* 152 */       this.currentNamespaces.clear();
/* 153 */       this.additionalNamespaces.clear();
/*     */     }
/*     */ 
/*     */     int getNamespaceCount() {
/* 157 */       return this.currentNamespaces.size() + this.additionalNamespaces.size() / 2;
/*     */     }
/*     */ 
/*     */     String getNamespacePrefix(int index) {
/* 161 */       int sz = this.currentNamespaces.size();
/* 162 */       if (index < sz) {
/* 163 */         Attr attr = (Attr)this.currentNamespaces.get(index);
/* 164 */         String result = attr.getLocalName();
/* 165 */         if (result == null) {
/* 166 */           result = QName.valueOf(attr.getNodeName()).getLocalPart();
/*     */         }
/* 168 */         return result.equals("xmlns") ? null : result;
/*     */       }
/* 170 */       return (String)this.additionalNamespaces.get((index - sz) * 2);
/*     */     }
/*     */ 
/*     */     String getNamespaceURI(int index)
/*     */     {
/* 175 */       int sz = this.currentNamespaces.size();
/* 176 */       if (index < sz) {
/* 177 */         return ((Attr)this.currentNamespaces.get(index)).getValue();
/*     */       }
/* 179 */       return (String)this.additionalNamespaces.get((index - sz) * 2 + 1);
/*     */     }
/*     */ 
/*     */     String getPrefix(String nsUri)
/*     */     {
/* 188 */       for (Scope sp = this; sp != null; sp = sp.parent) {
/* 189 */         for (int i = sp.currentNamespaces.size() - 1; i >= 0; i--) {
/* 190 */           String result = DOMStreamReader.getPrefixForAttr((Attr)sp.currentNamespaces.get(i), nsUri);
/* 191 */           if (result != null)
/* 192 */             return result;
/*     */         }
/* 194 */         for (int i = sp.additionalNamespaces.size() - 2; i >= 0; i -= 2)
/* 195 */           if (((String)sp.additionalNamespaces.get(i + 1)).equals(nsUri))
/* 196 */             return (String)sp.additionalNamespaces.get(i);
/*     */       }
/* 198 */       return null;
/*     */     }
/*     */ 
/*     */     String getNamespaceURI(@NotNull String prefix)
/*     */     {
/* 208 */       String nsDeclName = "xmlns:" + prefix;
/*     */ 
/* 210 */       for (Scope sp = this; sp != null; sp = sp.parent) {
/* 211 */         for (int i = sp.currentNamespaces.size() - 1; i >= 0; i--) {
/* 212 */           Attr a = (Attr)sp.currentNamespaces.get(i);
/* 213 */           if (a.getNodeName().equals(nsDeclName))
/* 214 */             return a.getValue();
/*     */         }
/* 216 */         for (int i = sp.additionalNamespaces.size() - 2; i >= 0; i -= 2)
/* 217 */           if (((String)sp.additionalNamespaces.get(i)).equals(prefix))
/* 218 */             return (String)sp.additionalNamespaces.get(i + 1);
/*     */       }
/* 220 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.DOMStreamReader
 * JD-Core Version:    0.6.2
 */