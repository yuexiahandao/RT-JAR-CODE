/*      */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ import org.xml.sax.helpers.AttributesImpl;
/*      */ 
/*      */ public class AdaptiveResultTreeImpl extends SimpleResultTreeImpl
/*      */ {
/*   80 */   private static int _documentURIIndex = 0;
/*      */ 
/*   82 */   private static final String EMPTY_STRING = "".intern();
/*      */   private SAXImpl _dom;
/*      */   private DTMWSFilter _wsfilter;
/*      */   private int _initSize;
/*      */   private boolean _buildIdIndex;
/*   99 */   private final AttributesImpl _attributes = new AttributesImpl();
/*      */   private String _openElementName;
/*      */ 
/*      */   public AdaptiveResultTreeImpl(XSLTCDTMManager dtmManager, int documentID, DTMWSFilter wsfilter, int initSize, boolean buildIdIndex)
/*      */   {
/*  110 */     super(dtmManager, documentID);
/*      */ 
/*  112 */     this._wsfilter = wsfilter;
/*  113 */     this._initSize = initSize;
/*  114 */     this._buildIdIndex = buildIdIndex;
/*      */   }
/*      */ 
/*      */   public DOM getNestedDOM()
/*      */   {
/*  120 */     return this._dom;
/*      */   }
/*      */ 
/*      */   public int getDocument()
/*      */   {
/*  126 */     if (this._dom != null) {
/*  127 */       return this._dom.getDocument();
/*      */     }
/*      */ 
/*  130 */     return super.getDocument();
/*      */   }
/*      */ 
/*      */   public String getStringValue()
/*      */   {
/*  137 */     if (this._dom != null) {
/*  138 */       return this._dom.getStringValue();
/*      */     }
/*      */ 
/*  141 */     return super.getStringValue();
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getIterator()
/*      */   {
/*  147 */     if (this._dom != null) {
/*  148 */       return this._dom.getIterator();
/*      */     }
/*      */ 
/*  151 */     return super.getIterator();
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getChildren(int node)
/*      */   {
/*  157 */     if (this._dom != null) {
/*  158 */       return this._dom.getChildren(node);
/*      */     }
/*      */ 
/*  161 */     return super.getChildren(node);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedChildren(int type)
/*      */   {
/*  167 */     if (this._dom != null) {
/*  168 */       return this._dom.getTypedChildren(type);
/*      */     }
/*      */ 
/*  171 */     return super.getTypedChildren(type);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getAxisIterator(int axis)
/*      */   {
/*  177 */     if (this._dom != null) {
/*  178 */       return this._dom.getAxisIterator(axis);
/*      */     }
/*      */ 
/*  181 */     return super.getAxisIterator(axis);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*      */   {
/*  187 */     if (this._dom != null) {
/*  188 */       return this._dom.getTypedAxisIterator(axis, type);
/*      */     }
/*      */ 
/*  191 */     return super.getTypedAxisIterator(axis, type);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
/*      */   {
/*  197 */     if (this._dom != null) {
/*  198 */       return this._dom.getNthDescendant(node, n, includeself);
/*      */     }
/*      */ 
/*  201 */     return super.getNthDescendant(node, n, includeself);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
/*      */   {
/*  207 */     if (this._dom != null) {
/*  208 */       return this._dom.getNamespaceAxisIterator(axis, ns);
/*      */     }
/*      */ 
/*  211 */     return super.getNamespaceAxisIterator(axis, ns);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op)
/*      */   {
/*  218 */     if (this._dom != null) {
/*  219 */       return this._dom.getNodeValueIterator(iter, returnType, value, op);
/*      */     }
/*      */ 
/*  222 */     return super.getNodeValueIterator(iter, returnType, value, op);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
/*      */   {
/*  228 */     if (this._dom != null) {
/*  229 */       return this._dom.orderNodes(source, node);
/*      */     }
/*      */ 
/*  232 */     return super.orderNodes(source, node);
/*      */   }
/*      */ 
/*      */   public String getNodeName(int node)
/*      */   {
/*  238 */     if (this._dom != null) {
/*  239 */       return this._dom.getNodeName(node);
/*      */     }
/*      */ 
/*  242 */     return super.getNodeName(node);
/*      */   }
/*      */ 
/*      */   public String getNodeNameX(int node)
/*      */   {
/*  248 */     if (this._dom != null) {
/*  249 */       return this._dom.getNodeNameX(node);
/*      */     }
/*      */ 
/*  252 */     return super.getNodeNameX(node);
/*      */   }
/*      */ 
/*      */   public String getNamespaceName(int node)
/*      */   {
/*  258 */     if (this._dom != null) {
/*  259 */       return this._dom.getNamespaceName(node);
/*      */     }
/*      */ 
/*  262 */     return super.getNamespaceName(node);
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(int nodeHandle)
/*      */   {
/*  269 */     if (this._dom != null) {
/*  270 */       return this._dom.getExpandedTypeID(nodeHandle);
/*      */     }
/*      */ 
/*  273 */     return super.getExpandedTypeID(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getNamespaceType(int node)
/*      */   {
/*  279 */     if (this._dom != null) {
/*  280 */       return this._dom.getNamespaceType(node);
/*      */     }
/*      */ 
/*  283 */     return super.getNamespaceType(node);
/*      */   }
/*      */ 
/*      */   public int getParent(int nodeHandle)
/*      */   {
/*  289 */     if (this._dom != null) {
/*  290 */       return this._dom.getParent(nodeHandle);
/*      */     }
/*      */ 
/*  293 */     return super.getParent(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int gType, int element)
/*      */   {
/*  299 */     if (this._dom != null) {
/*  300 */       return this._dom.getAttributeNode(gType, element);
/*      */     }
/*      */ 
/*  303 */     return super.getAttributeNode(gType, element);
/*      */   }
/*      */ 
/*      */   public String getStringValueX(int nodeHandle)
/*      */   {
/*  309 */     if (this._dom != null) {
/*  310 */       return this._dom.getStringValueX(nodeHandle);
/*      */     }
/*      */ 
/*  313 */     return super.getStringValueX(nodeHandle);
/*      */   }
/*      */ 
/*      */   public void copy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  320 */     if (this._dom != null) {
/*  321 */       this._dom.copy(node, handler);
/*      */     }
/*      */     else
/*  324 */       super.copy(node, handler);
/*      */   }
/*      */ 
/*      */   public void copy(DTMAxisIterator nodes, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  331 */     if (this._dom != null) {
/*  332 */       this._dom.copy(nodes, handler);
/*      */     }
/*      */     else
/*  335 */       super.copy(nodes, handler);
/*      */   }
/*      */ 
/*      */   public String shallowCopy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  342 */     if (this._dom != null) {
/*  343 */       return this._dom.shallowCopy(node, handler);
/*      */     }
/*      */ 
/*  346 */     return super.shallowCopy(node, handler);
/*      */   }
/*      */ 
/*      */   public boolean lessThan(int node1, int node2)
/*      */   {
/*  352 */     if (this._dom != null) {
/*  353 */       return this._dom.lessThan(node1, node2);
/*      */     }
/*      */ 
/*  356 */     return super.lessThan(node1, node2);
/*      */   }
/*      */ 
/*      */   public void characters(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  369 */     if (this._dom != null) {
/*  370 */       this._dom.characters(node, handler);
/*      */     }
/*      */     else
/*  373 */       super.characters(node, handler);
/*      */   }
/*      */ 
/*      */   public Node makeNode(int index)
/*      */   {
/*  379 */     if (this._dom != null) {
/*  380 */       return this._dom.makeNode(index);
/*      */     }
/*      */ 
/*  383 */     return super.makeNode(index);
/*      */   }
/*      */ 
/*      */   public Node makeNode(DTMAxisIterator iter)
/*      */   {
/*  389 */     if (this._dom != null) {
/*  390 */       return this._dom.makeNode(iter);
/*      */     }
/*      */ 
/*  393 */     return super.makeNode(iter);
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(int index)
/*      */   {
/*  399 */     if (this._dom != null) {
/*  400 */       return this._dom.makeNodeList(index);
/*      */     }
/*      */ 
/*  403 */     return super.makeNodeList(index);
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(DTMAxisIterator iter)
/*      */   {
/*  409 */     if (this._dom != null) {
/*  410 */       return this._dom.makeNodeList(iter);
/*      */     }
/*      */ 
/*  413 */     return super.makeNodeList(iter);
/*      */   }
/*      */ 
/*      */   public String getLanguage(int node)
/*      */   {
/*  419 */     if (this._dom != null) {
/*  420 */       return this._dom.getLanguage(node);
/*      */     }
/*      */ 
/*  423 */     return super.getLanguage(node);
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  429 */     if (this._dom != null) {
/*  430 */       return this._dom.getSize();
/*      */     }
/*      */ 
/*  433 */     return super.getSize();
/*      */   }
/*      */ 
/*      */   public String getDocumentURI(int node)
/*      */   {
/*  439 */     if (this._dom != null) {
/*  440 */       return this._dom.getDocumentURI(node);
/*      */     }
/*      */ 
/*  443 */     return "adaptive_rtf" + _documentURIIndex++;
/*      */   }
/*      */ 
/*      */   public void setFilter(StripFilter filter)
/*      */   {
/*  449 */     if (this._dom != null) {
/*  450 */       this._dom.setFilter(filter);
/*      */     }
/*      */     else
/*  453 */       super.setFilter(filter);
/*      */   }
/*      */ 
/*      */   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces)
/*      */   {
/*  459 */     if (this._dom != null) {
/*  460 */       this._dom.setupMapping(names, uris, types, namespaces);
/*      */     }
/*      */     else
/*  463 */       super.setupMapping(names, uris, types, namespaces);
/*      */   }
/*      */ 
/*      */   public boolean isElement(int node)
/*      */   {
/*  469 */     if (this._dom != null) {
/*  470 */       return this._dom.isElement(node);
/*      */     }
/*      */ 
/*  473 */     return super.isElement(node);
/*      */   }
/*      */ 
/*      */   public boolean isAttribute(int node)
/*      */   {
/*  479 */     if (this._dom != null) {
/*  480 */       return this._dom.isAttribute(node);
/*      */     }
/*      */ 
/*  483 */     return super.isAttribute(node);
/*      */   }
/*      */ 
/*      */   public String lookupNamespace(int node, String prefix)
/*      */     throws TransletException
/*      */   {
/*  490 */     if (this._dom != null) {
/*  491 */       return this._dom.lookupNamespace(node, prefix);
/*      */     }
/*      */ 
/*  494 */     return super.lookupNamespace(node, prefix);
/*      */   }
/*      */ 
/*      */   public final int getNodeIdent(int nodehandle)
/*      */   {
/*  503 */     if (this._dom != null) {
/*  504 */       return this._dom.getNodeIdent(nodehandle);
/*      */     }
/*      */ 
/*  507 */     return super.getNodeIdent(nodehandle);
/*      */   }
/*      */ 
/*      */   public final int getNodeHandle(int nodeId)
/*      */   {
/*  516 */     if (this._dom != null) {
/*  517 */       return this._dom.getNodeHandle(nodeId);
/*      */     }
/*      */ 
/*  520 */     return super.getNodeHandle(nodeId);
/*      */   }
/*      */ 
/*      */   public DOM getResultTreeFrag(int initialSize, int rtfType)
/*      */   {
/*  526 */     if (this._dom != null) {
/*  527 */       return this._dom.getResultTreeFrag(initialSize, rtfType);
/*      */     }
/*      */ 
/*  530 */     return super.getResultTreeFrag(initialSize, rtfType);
/*      */   }
/*      */ 
/*      */   public SerializationHandler getOutputDomBuilder()
/*      */   {
/*  536 */     return this;
/*      */   }
/*      */ 
/*      */   public int getNSType(int node)
/*      */   {
/*  541 */     if (this._dom != null) {
/*  542 */       return this._dom.getNSType(node);
/*      */     }
/*      */ 
/*  545 */     return super.getNSType(node);
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/*  551 */     if (this._dom != null) {
/*  552 */       return this._dom.getUnparsedEntityURI(name);
/*      */     }
/*      */ 
/*  555 */     return super.getUnparsedEntityURI(name);
/*      */   }
/*      */ 
/*      */   public Hashtable getElementsWithIDs()
/*      */   {
/*  561 */     if (this._dom != null) {
/*  562 */       return this._dom.getElementsWithIDs();
/*      */     }
/*      */ 
/*  565 */     return super.getElementsWithIDs();
/*      */   }
/*      */ 
/*      */   private void maybeEmitStartElement()
/*      */     throws SAXException
/*      */   {
/*  575 */     if (this._openElementName != null)
/*      */     {
/*      */       int index;
/*  578 */       if ((index = this._openElementName.indexOf(":")) < 0) {
/*  579 */         this._dom.startElement(null, this._openElementName, this._openElementName, this._attributes);
/*      */       } else {
/*  581 */         String uri = this._dom.getNamespaceURI(this._openElementName.substring(0, index));
/*  582 */         this._dom.startElement(uri, this._openElementName.substring(index + 1), this._openElementName, this._attributes);
/*      */       }
/*      */ 
/*  586 */       this._openElementName = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void prepareNewDOM()
/*      */     throws SAXException
/*      */   {
/*  594 */     this._dom = ((SAXImpl)this._dtmManager.getDTM(null, true, this._wsfilter, true, false, false, this._initSize, this._buildIdIndex));
/*      */ 
/*  597 */     this._dom.startDocument();
/*      */ 
/*  599 */     for (int i = 0; i < this._size; i++) {
/*  600 */       String str = this._textArray[i];
/*  601 */       this._dom.characters(str.toCharArray(), 0, str.length());
/*      */     }
/*  603 */     this._size = 0;
/*      */   }
/*      */ 
/*      */   public void startDocument() throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDocument() throws SAXException
/*      */   {
/*  612 */     if (this._dom != null) {
/*  613 */       this._dom.endDocument();
/*      */     }
/*      */     else
/*  616 */       super.endDocument();
/*      */   }
/*      */ 
/*      */   public void characters(String str)
/*      */     throws SAXException
/*      */   {
/*  622 */     if (this._dom != null) {
/*  623 */       characters(str.toCharArray(), 0, str.length());
/*      */     }
/*      */     else
/*  626 */       super.characters(str);
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int offset, int length)
/*      */     throws SAXException
/*      */   {
/*  633 */     if (this._dom != null) {
/*  634 */       maybeEmitStartElement();
/*  635 */       this._dom.characters(ch, offset, length);
/*      */     }
/*      */     else {
/*  638 */       super.characters(ch, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean setEscaping(boolean escape) throws SAXException
/*      */   {
/*  644 */     if (this._dom != null) {
/*  645 */       return this._dom.setEscaping(escape);
/*      */     }
/*      */ 
/*  648 */     return super.setEscaping(escape);
/*      */   }
/*      */ 
/*      */   public void startElement(String elementName)
/*      */     throws SAXException
/*      */   {
/*  654 */     if (this._dom == null) {
/*  655 */       prepareNewDOM();
/*      */     }
/*      */ 
/*  658 */     maybeEmitStartElement();
/*  659 */     this._openElementName = elementName;
/*  660 */     this._attributes.clear();
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*  666 */     startElement(qName);
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*      */     throws SAXException
/*      */   {
/*  672 */     startElement(qName);
/*      */   }
/*      */ 
/*      */   public void endElement(String elementName) throws SAXException
/*      */   {
/*  677 */     maybeEmitStartElement();
/*  678 */     this._dom.endElement(null, null, elementName);
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*  684 */     endElement(qName);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String qName, String value)
/*      */   {
/*  690 */     int colonpos = qName.indexOf(":");
/*  691 */     String uri = EMPTY_STRING;
/*  692 */     String localName = qName;
/*  693 */     if (colonpos > 0)
/*      */     {
/*  695 */       String prefix = qName.substring(0, colonpos);
/*  696 */       localName = qName.substring(colonpos + 1);
/*  697 */       uri = this._dom.getNamespaceURI(prefix);
/*      */     }
/*      */ 
/*  700 */     addAttribute(uri, localName, qName, "CDATA", value);
/*      */   }
/*      */ 
/*      */   public void addUniqueAttribute(String qName, String value, int flags)
/*      */     throws SAXException
/*      */   {
/*  706 */     addAttribute(qName, value);
/*      */   }
/*      */ 
/*      */   public void addAttribute(String uri, String localName, String qname, String type, String value)
/*      */   {
/*  712 */     if (this._openElementName != null) {
/*  713 */       this._attributes.addAttribute(uri, localName, qname, type, value);
/*      */     }
/*      */     else
/*  716 */       BasisLibrary.runTimeError("STRAY_ATTRIBUTE_ERR", qname);
/*      */   }
/*      */ 
/*      */   public void namespaceAfterStartElement(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  723 */     if (this._dom == null) {
/*  724 */       prepareNewDOM();
/*      */     }
/*      */ 
/*  727 */     this._dom.startPrefixMapping(prefix, uri);
/*      */   }
/*      */ 
/*      */   public void comment(String comment) throws SAXException
/*      */   {
/*  732 */     if (this._dom == null) {
/*  733 */       prepareNewDOM();
/*      */     }
/*      */ 
/*  736 */     maybeEmitStartElement();
/*  737 */     char[] chars = comment.toCharArray();
/*  738 */     this._dom.comment(chars, 0, chars.length);
/*      */   }
/*      */ 
/*      */   public void comment(char[] chars, int offset, int length)
/*      */     throws SAXException
/*      */   {
/*  744 */     if (this._dom == null) {
/*  745 */       prepareNewDOM();
/*      */     }
/*      */ 
/*  748 */     maybeEmitStartElement();
/*  749 */     this._dom.comment(chars, offset, length);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*  755 */     if (this._dom == null) {
/*  756 */       prepareNewDOM();
/*      */     }
/*      */ 
/*  759 */     maybeEmitStartElement();
/*  760 */     this._dom.processingInstruction(target, data);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */   {
/*  767 */     if (this._dom != null)
/*  768 */       this._dom.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value)
/*      */   {
/*  774 */     if (this._dom != null)
/*  775 */       this._dom.setProperty(property, value);
/*      */   }
/*      */ 
/*      */   public DTMAxisTraverser getAxisTraverser(int axis)
/*      */   {
/*  781 */     if (this._dom != null) {
/*  782 */       return this._dom.getAxisTraverser(axis);
/*      */     }
/*      */ 
/*  785 */     return super.getAxisTraverser(axis);
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes(int nodeHandle)
/*      */   {
/*  791 */     if (this._dom != null) {
/*  792 */       return this._dom.hasChildNodes(nodeHandle);
/*      */     }
/*      */ 
/*  795 */     return super.hasChildNodes(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getFirstChild(int nodeHandle)
/*      */   {
/*  801 */     if (this._dom != null) {
/*  802 */       return this._dom.getFirstChild(nodeHandle);
/*      */     }
/*      */ 
/*  805 */     return super.getFirstChild(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeHandle)
/*      */   {
/*  811 */     if (this._dom != null) {
/*  812 */       return this._dom.getLastChild(nodeHandle);
/*      */     }
/*      */ 
/*  815 */     return super.getLastChild(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int elementHandle, String namespaceURI, String name)
/*      */   {
/*  821 */     if (this._dom != null) {
/*  822 */       return this._dom.getAttributeNode(elementHandle, namespaceURI, name);
/*      */     }
/*      */ 
/*  825 */     return super.getAttributeNode(elementHandle, namespaceURI, name);
/*      */   }
/*      */ 
/*      */   public int getFirstAttribute(int nodeHandle)
/*      */   {
/*  831 */     if (this._dom != null) {
/*  832 */       return this._dom.getFirstAttribute(nodeHandle);
/*      */     }
/*      */ 
/*  835 */     return super.getFirstAttribute(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
/*      */   {
/*  841 */     if (this._dom != null) {
/*  842 */       return this._dom.getFirstNamespaceNode(nodeHandle, inScope);
/*      */     }
/*      */ 
/*  845 */     return super.getFirstNamespaceNode(nodeHandle, inScope);
/*      */   }
/*      */ 
/*      */   public int getNextSibling(int nodeHandle)
/*      */   {
/*  851 */     if (this._dom != null) {
/*  852 */       return this._dom.getNextSibling(nodeHandle);
/*      */     }
/*      */ 
/*  855 */     return super.getNextSibling(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getPreviousSibling(int nodeHandle)
/*      */   {
/*  861 */     if (this._dom != null) {
/*  862 */       return this._dom.getPreviousSibling(nodeHandle);
/*      */     }
/*      */ 
/*  865 */     return super.getPreviousSibling(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getNextAttribute(int nodeHandle)
/*      */   {
/*  871 */     if (this._dom != null) {
/*  872 */       return this._dom.getNextAttribute(nodeHandle);
/*      */     }
/*      */ 
/*  875 */     return super.getNextAttribute(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
/*      */   {
/*  882 */     if (this._dom != null) {
/*  883 */       return this._dom.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
/*      */     }
/*      */ 
/*  886 */     return super.getNextNamespaceNode(baseHandle, namespaceHandle, inScope);
/*      */   }
/*      */ 
/*      */   public int getOwnerDocument(int nodeHandle)
/*      */   {
/*  892 */     if (this._dom != null) {
/*  893 */       return this._dom.getOwnerDocument(nodeHandle);
/*      */     }
/*      */ 
/*  896 */     return super.getOwnerDocument(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getDocumentRoot(int nodeHandle)
/*      */   {
/*  902 */     if (this._dom != null) {
/*  903 */       return this._dom.getDocumentRoot(nodeHandle);
/*      */     }
/*      */ 
/*  906 */     return super.getDocumentRoot(nodeHandle);
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/*  912 */     if (this._dom != null) {
/*  913 */       return this._dom.getStringValue(nodeHandle);
/*      */     }
/*      */ 
/*  916 */     return super.getStringValue(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getStringValueChunkCount(int nodeHandle)
/*      */   {
/*  922 */     if (this._dom != null) {
/*  923 */       return this._dom.getStringValueChunkCount(nodeHandle);
/*      */     }
/*      */ 
/*  926 */     return super.getStringValueChunkCount(nodeHandle);
/*      */   }
/*      */ 
/*      */   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
/*      */   {
/*  933 */     if (this._dom != null) {
/*  934 */       return this._dom.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
/*      */     }
/*      */ 
/*  937 */     return super.getStringValueChunk(nodeHandle, chunkIndex, startAndLen);
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(String namespace, String localName, int type)
/*      */   {
/*  943 */     if (this._dom != null) {
/*  944 */       return this._dom.getExpandedTypeID(namespace, localName, type);
/*      */     }
/*      */ 
/*  947 */     return super.getExpandedTypeID(namespace, localName, type);
/*      */   }
/*      */ 
/*      */   public String getLocalNameFromExpandedNameID(int ExpandedNameID)
/*      */   {
/*  953 */     if (this._dom != null) {
/*  954 */       return this._dom.getLocalNameFromExpandedNameID(ExpandedNameID);
/*      */     }
/*      */ 
/*  957 */     return super.getLocalNameFromExpandedNameID(ExpandedNameID);
/*      */   }
/*      */ 
/*      */   public String getNamespaceFromExpandedNameID(int ExpandedNameID)
/*      */   {
/*  963 */     if (this._dom != null) {
/*  964 */       return this._dom.getNamespaceFromExpandedNameID(ExpandedNameID);
/*      */     }
/*      */ 
/*  967 */     return super.getNamespaceFromExpandedNameID(ExpandedNameID);
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/*  973 */     if (this._dom != null) {
/*  974 */       return this._dom.getLocalName(nodeHandle);
/*      */     }
/*      */ 
/*  977 */     return super.getLocalName(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getPrefix(int nodeHandle)
/*      */   {
/*  983 */     if (this._dom != null) {
/*  984 */       return this._dom.getPrefix(nodeHandle);
/*      */     }
/*      */ 
/*  987 */     return super.getPrefix(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int nodeHandle)
/*      */   {
/*  993 */     if (this._dom != null) {
/*  994 */       return this._dom.getNamespaceURI(nodeHandle);
/*      */     }
/*      */ 
/*  997 */     return super.getNamespaceURI(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/* 1003 */     if (this._dom != null) {
/* 1004 */       return this._dom.getNodeValue(nodeHandle);
/*      */     }
/*      */ 
/* 1007 */     return super.getNodeValue(nodeHandle);
/*      */   }
/*      */ 
/*      */   public short getNodeType(int nodeHandle)
/*      */   {
/* 1013 */     if (this._dom != null) {
/* 1014 */       return this._dom.getNodeType(nodeHandle);
/*      */     }
/*      */ 
/* 1017 */     return super.getNodeType(nodeHandle);
/*      */   }
/*      */ 
/*      */   public short getLevel(int nodeHandle)
/*      */   {
/* 1023 */     if (this._dom != null) {
/* 1024 */       return this._dom.getLevel(nodeHandle);
/*      */     }
/*      */ 
/* 1027 */     return super.getLevel(nodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/* 1033 */     if (this._dom != null) {
/* 1034 */       return this._dom.isSupported(feature, version);
/*      */     }
/*      */ 
/* 1037 */     return super.isSupported(feature, version);
/*      */   }
/*      */ 
/*      */   public String getDocumentBaseURI()
/*      */   {
/* 1043 */     if (this._dom != null) {
/* 1044 */       return this._dom.getDocumentBaseURI();
/*      */     }
/*      */ 
/* 1047 */     return super.getDocumentBaseURI();
/*      */   }
/*      */ 
/*      */   public void setDocumentBaseURI(String baseURI)
/*      */   {
/* 1053 */     if (this._dom != null) {
/* 1054 */       this._dom.setDocumentBaseURI(baseURI);
/*      */     }
/*      */     else
/* 1057 */       super.setDocumentBaseURI(baseURI);
/*      */   }
/*      */ 
/*      */   public String getDocumentSystemIdentifier(int nodeHandle)
/*      */   {
/* 1063 */     if (this._dom != null) {
/* 1064 */       return this._dom.getDocumentSystemIdentifier(nodeHandle);
/*      */     }
/*      */ 
/* 1067 */     return super.getDocumentSystemIdentifier(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getDocumentEncoding(int nodeHandle)
/*      */   {
/* 1073 */     if (this._dom != null) {
/* 1074 */       return this._dom.getDocumentEncoding(nodeHandle);
/*      */     }
/*      */ 
/* 1077 */     return super.getDocumentEncoding(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getDocumentStandalone(int nodeHandle)
/*      */   {
/* 1083 */     if (this._dom != null) {
/* 1084 */       return this._dom.getDocumentStandalone(nodeHandle);
/*      */     }
/*      */ 
/* 1087 */     return super.getDocumentStandalone(nodeHandle);
/*      */   }
/*      */ 
/*      */   public String getDocumentVersion(int documentHandle)
/*      */   {
/* 1093 */     if (this._dom != null) {
/* 1094 */       return this._dom.getDocumentVersion(documentHandle);
/*      */     }
/*      */ 
/* 1097 */     return super.getDocumentVersion(documentHandle);
/*      */   }
/*      */ 
/*      */   public boolean getDocumentAllDeclarationsProcessed()
/*      */   {
/* 1103 */     if (this._dom != null) {
/* 1104 */       return this._dom.getDocumentAllDeclarationsProcessed();
/*      */     }
/*      */ 
/* 1107 */     return super.getDocumentAllDeclarationsProcessed();
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationSystemIdentifier()
/*      */   {
/* 1113 */     if (this._dom != null) {
/* 1114 */       return this._dom.getDocumentTypeDeclarationSystemIdentifier();
/*      */     }
/*      */ 
/* 1117 */     return super.getDocumentTypeDeclarationSystemIdentifier();
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationPublicIdentifier()
/*      */   {
/* 1123 */     if (this._dom != null) {
/* 1124 */       return this._dom.getDocumentTypeDeclarationPublicIdentifier();
/*      */     }
/*      */ 
/* 1127 */     return super.getDocumentTypeDeclarationPublicIdentifier();
/*      */   }
/*      */ 
/*      */   public int getElementById(String elementId)
/*      */   {
/* 1133 */     if (this._dom != null) {
/* 1134 */       return this._dom.getElementById(elementId);
/*      */     }
/*      */ 
/* 1137 */     return super.getElementById(elementId);
/*      */   }
/*      */ 
/*      */   public boolean supportsPreStripping()
/*      */   {
/* 1143 */     if (this._dom != null) {
/* 1144 */       return this._dom.supportsPreStripping();
/*      */     }
/*      */ 
/* 1147 */     return super.supportsPreStripping();
/*      */   }
/*      */ 
/*      */   public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle)
/*      */   {
/* 1153 */     if (this._dom != null) {
/* 1154 */       return this._dom.isNodeAfter(firstNodeHandle, secondNodeHandle);
/*      */     }
/*      */ 
/* 1157 */     return super.isNodeAfter(firstNodeHandle, secondNodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean isCharacterElementContentWhitespace(int nodeHandle)
/*      */   {
/* 1163 */     if (this._dom != null) {
/* 1164 */       return this._dom.isCharacterElementContentWhitespace(nodeHandle);
/*      */     }
/*      */ 
/* 1167 */     return super.isCharacterElementContentWhitespace(nodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
/*      */   {
/* 1173 */     if (this._dom != null) {
/* 1174 */       return this._dom.isDocumentAllDeclarationsProcessed(documentHandle);
/*      */     }
/*      */ 
/* 1177 */     return super.isDocumentAllDeclarationsProcessed(documentHandle);
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int attributeHandle)
/*      */   {
/* 1183 */     if (this._dom != null) {
/* 1184 */       return this._dom.isAttributeSpecified(attributeHandle);
/*      */     }
/*      */ 
/* 1187 */     return super.isAttributeSpecified(attributeHandle);
/*      */   }
/*      */ 
/*      */   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
/*      */     throws SAXException
/*      */   {
/* 1195 */     if (this._dom != null) {
/* 1196 */       this._dom.dispatchCharactersEvents(nodeHandle, ch, normalize);
/*      */     }
/*      */     else
/* 1199 */       super.dispatchCharactersEvents(nodeHandle, ch, normalize);
/*      */   }
/*      */ 
/*      */   public void dispatchToEvents(int nodeHandle, ContentHandler ch)
/*      */     throws SAXException
/*      */   {
/* 1206 */     if (this._dom != null) {
/* 1207 */       this._dom.dispatchToEvents(nodeHandle, ch);
/*      */     }
/*      */     else
/* 1210 */       super.dispatchToEvents(nodeHandle, ch);
/*      */   }
/*      */ 
/*      */   public Node getNode(int nodeHandle)
/*      */   {
/* 1216 */     if (this._dom != null) {
/* 1217 */       return this._dom.getNode(nodeHandle);
/*      */     }
/*      */ 
/* 1220 */     return super.getNode(nodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean needsTwoThreads()
/*      */   {
/* 1226 */     if (this._dom != null) {
/* 1227 */       return this._dom.needsTwoThreads();
/*      */     }
/*      */ 
/* 1230 */     return super.needsTwoThreads();
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/* 1236 */     if (this._dom != null) {
/* 1237 */       return this._dom.getContentHandler();
/*      */     }
/*      */ 
/* 1240 */     return super.getContentHandler();
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler()
/*      */   {
/* 1246 */     if (this._dom != null) {
/* 1247 */       return this._dom.getLexicalHandler();
/*      */     }
/*      */ 
/* 1250 */     return super.getLexicalHandler();
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/* 1256 */     if (this._dom != null) {
/* 1257 */       return this._dom.getEntityResolver();
/*      */     }
/*      */ 
/* 1260 */     return super.getEntityResolver();
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/* 1266 */     if (this._dom != null) {
/* 1267 */       return this._dom.getDTDHandler();
/*      */     }
/*      */ 
/* 1270 */     return super.getDTDHandler();
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/* 1276 */     if (this._dom != null) {
/* 1277 */       return this._dom.getErrorHandler();
/*      */     }
/*      */ 
/* 1280 */     return super.getErrorHandler();
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler()
/*      */   {
/* 1286 */     if (this._dom != null) {
/* 1287 */       return this._dom.getDeclHandler();
/*      */     }
/*      */ 
/* 1290 */     return super.getDeclHandler();
/*      */   }
/*      */ 
/*      */   public void appendChild(int newChild, boolean clone, boolean cloneDepth)
/*      */   {
/* 1296 */     if (this._dom != null) {
/* 1297 */       this._dom.appendChild(newChild, clone, cloneDepth);
/*      */     }
/*      */     else
/* 1300 */       super.appendChild(newChild, clone, cloneDepth);
/*      */   }
/*      */ 
/*      */   public void appendTextChild(String str)
/*      */   {
/* 1306 */     if (this._dom != null) {
/* 1307 */       this._dom.appendTextChild(str);
/*      */     }
/*      */     else
/* 1310 */       super.appendTextChild(str);
/*      */   }
/*      */ 
/*      */   public SourceLocator getSourceLocatorFor(int node)
/*      */   {
/* 1316 */     if (this._dom != null) {
/* 1317 */       return this._dom.getSourceLocatorFor(node);
/*      */     }
/*      */ 
/* 1320 */     return super.getSourceLocatorFor(node);
/*      */   }
/*      */ 
/*      */   public void documentRegistration()
/*      */   {
/* 1326 */     if (this._dom != null) {
/* 1327 */       this._dom.documentRegistration();
/*      */     }
/*      */     else
/* 1330 */       super.documentRegistration();
/*      */   }
/*      */ 
/*      */   public void documentRelease()
/*      */   {
/* 1336 */     if (this._dom != null) {
/* 1337 */       this._dom.documentRelease();
/*      */     }
/*      */     else
/* 1340 */       super.documentRelease();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.AdaptiveResultTreeImpl
 * JD-Core Version:    0.6.2
 */