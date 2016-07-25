/*      */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*      */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.NamespaceIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.NthDescendantIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.RootIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.SingletonIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.AncestorIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.AttributeIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.ChildrenIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.DescendantIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.FollowingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.FollowingSiblingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.ParentIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.PrecedingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.PrecedingSiblingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedAncestorIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedAttributeIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedChildrenIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedDescendantIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedFollowingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedFollowingSiblingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedPrecedingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedPrecedingSiblingIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedRootIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2.TypedSingletonIterator;
/*      */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*      */ import com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.IntStack;
/*      */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Enumeration;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Entity;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public final class SAXImpl extends SAX2DTM2
/*      */   implements DOMEnhancedForDTM, DOMBuilder
/*      */ {
/*   89 */   private int _uriCount = 0;
/*   90 */   private int _prefixCount = 0;
/*      */   private int[] _xmlSpaceStack;
/*   95 */   private int _idx = 1;
/*   96 */   private boolean _preserve = false;
/*      */   private static final String XML_STRING = "xml:";
/*      */   private static final String XML_PREFIX = "xml";
/*      */   private static final String XMLSPACE_STRING = "xml:space";
/*      */   private static final String PRESERVE_STRING = "preserve";
/*      */   private static final String XMLNS_PREFIX = "xmlns";
/*      */   private static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
/*  105 */   private boolean _escaping = true;
/*  106 */   private boolean _disableEscaping = false;
/*  107 */   private int _textNodeToProcess = -1;
/*      */   private static final String EMPTYSTRING = "";
/*  117 */   private static final DTMAxisIterator EMPTYITERATOR = EmptyIterator.getInstance();
/*      */ 
/*  119 */   private int _namesSize = -1;
/*      */ 
/*  122 */   private com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable _nsIndex = new com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable();
/*      */ 
/*  125 */   private int _size = 0;
/*      */ 
/*  128 */   private BitArray _dontEscape = null;
/*      */ 
/*  131 */   private String _documentURI = null;
/*  132 */   private static int _documentURIIndex = 0;
/*      */   private Document _document;
/*  140 */   private com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable _node2Ids = null;
/*      */ 
/*  143 */   private boolean _hasDOMSource = false;
/*      */   private XSLTCDTMManager _dtmManager;
/*      */   private Node[] _nodes;
/*      */   private NodeList[] _nodeLists;
/*      */   private static final String XML_LANG_ATTRIBUTE = "http://www.w3.org/XML/1998/namespace:@lang";
/*      */ 
/*      */   public void setDocumentURI(String uri)
/*      */   {
/*  158 */     if (uri != null)
/*  159 */       setDocumentBaseURI(SystemIDResolver.getAbsoluteURI(uri));
/*      */   }
/*      */ 
/*      */   public String getDocumentURI()
/*      */   {
/*  167 */     String baseURI = getDocumentBaseURI();
/*  168 */     return "rtf" + _documentURIIndex++;
/*      */   }
/*      */ 
/*      */   public String getDocumentURI(int node) {
/*  172 */     return getDocumentURI();
/*      */   }
/*      */ 
/*      */   public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String lookupNamespace(int node, String prefix)
/*      */     throws TransletException
/*      */   {
/*  189 */     SAX2DTM2.AncestorIterator ancestors = new SAX2DTM2.AncestorIterator(this);
/*      */ 
/*  191 */     if (isElement(node)) {
/*  192 */       ancestors.includeSelf();
/*      */     }
/*      */ 
/*  195 */     ancestors.setStartNode(node);
/*      */     int anode;
/*  196 */     while ((anode = ancestors.next()) != -1) {
/*  197 */       DTMDefaultBaseIterators.NamespaceIterator namespaces = new DTMDefaultBaseIterators.NamespaceIterator(this);
/*      */ 
/*  199 */       namespaces.setStartNode(anode);
/*      */       int nsnode;
/*  200 */       while ((nsnode = namespaces.next()) != -1) {
/*  201 */         if (getLocalName(nsnode).equals(prefix)) {
/*  202 */           return getNodeValue(nsnode);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  207 */     BasisLibrary.runTimeError("NAMESPACE_PREFIX_ERR", prefix);
/*  208 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isElement(int node)
/*      */   {
/*  215 */     return getNodeType(node) == 1;
/*      */   }
/*      */ 
/*      */   public boolean isAttribute(int node)
/*      */   {
/*  222 */     return getNodeType(node) == 2;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  229 */     return getNumberOfNodes();
/*      */   }
/*      */ 
/*      */   public void setFilter(StripFilter filter)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean lessThan(int node1, int node2)
/*      */   {
/*  243 */     if (node1 == -1) {
/*  244 */       return false;
/*      */     }
/*      */ 
/*  247 */     if (node2 == -1) {
/*  248 */       return true;
/*      */     }
/*      */ 
/*  251 */     return node1 < node2;
/*      */   }
/*      */ 
/*      */   public Node makeNode(int index)
/*      */   {
/*  258 */     if (this._nodes == null) {
/*  259 */       this._nodes = new Node[this._namesSize];
/*      */     }
/*      */ 
/*  262 */     int nodeID = makeNodeIdentity(index);
/*  263 */     if (nodeID < 0) {
/*  264 */       return null;
/*      */     }
/*  266 */     if (nodeID < this._nodes.length) {
/*  267 */       return this._nodes[nodeID] = (this._nodes[nodeID] != null ? this._nodes[nodeID] : ) = new DTMNodeProxy(this, index);
/*      */     }
/*      */ 
/*  271 */     return new DTMNodeProxy(this, index);
/*      */   }
/*      */ 
/*      */   public Node makeNode(DTMAxisIterator iter)
/*      */   {
/*  280 */     return makeNode(iter.next());
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(int index)
/*      */   {
/*  287 */     if (this._nodeLists == null) {
/*  288 */       this._nodeLists = new NodeList[this._namesSize];
/*      */     }
/*      */ 
/*  291 */     int nodeID = makeNodeIdentity(index);
/*  292 */     if (nodeID < 0) {
/*  293 */       return null;
/*      */     }
/*  295 */     if (nodeID < this._nodeLists.length) {
/*  296 */       return this._nodeLists[nodeID] = (this._nodeLists[nodeID] != null ? this._nodeLists[nodeID] : ) = new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
/*      */     }
/*      */ 
/*  301 */     return new DTMAxisIterNodeList(this, new DTMDefaultBaseIterators.SingletonIterator(this, index));
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(DTMAxisIterator iter)
/*      */   {
/*  310 */     return new DTMAxisIterNodeList(this, iter);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op)
/*      */   {
/*  452 */     return new NodeValueIterator(iterator, type, value, op);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
/*      */   {
/*  460 */     return new DupFilterIterator(source);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getIterator()
/*      */   {
/*  470 */     return new DTMDefaultBaseIterators.SingletonIterator(this, getDocument(), true);
/*      */   }
/*      */ 
/*      */   public int getNSType(int node)
/*      */   {
/*  478 */     String s = getNamespaceURI(node);
/*  479 */     if (s == null) {
/*  480 */       return 0;
/*      */     }
/*  482 */     int eType = getIdForNamespace(s);
/*  483 */     return ((Integer)this._nsIndex.get(new Integer(eType))).intValue();
/*      */   }
/*      */ 
/*      */   public int getNamespaceType(int node)
/*      */   {
/*  493 */     return super.getNamespaceType(node);
/*      */   }
/*      */ 
/*      */   private int[] setupMapping(String[] names, String[] uris, int[] types, int nNames)
/*      */   {
/*  502 */     int[] result = new int[this.m_expandedNameTable.getSize()];
/*  503 */     for (int i = 0; i < nNames; i++)
/*      */     {
/*  505 */       int type = this.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
/*  506 */       result[type] = type;
/*      */     }
/*  508 */     return result;
/*      */   }
/*      */ 
/*      */   public int getGeneralizedType(String name)
/*      */   {
/*  515 */     return getGeneralizedType(name, true);
/*      */   }
/*      */ 
/*      */   public int getGeneralizedType(String name, boolean searchOnly)
/*      */   {
/*  522 */     String ns = null;
/*  523 */     int index = -1;
/*      */ 
/*  527 */     if ((index = name.lastIndexOf(":")) > -1) {
/*  528 */       ns = name.substring(0, index);
/*      */     }
/*      */ 
/*  533 */     int lNameStartIdx = index + 1;
/*      */     int code;
/*  537 */     if (name.charAt(lNameStartIdx) == '@') {
/*  538 */       int code = 2;
/*  539 */       lNameStartIdx++;
/*      */     }
/*      */     else {
/*  542 */       code = 1;
/*      */     }
/*      */ 
/*  546 */     String lName = lNameStartIdx == 0 ? name : name.substring(lNameStartIdx);
/*      */ 
/*  548 */     return this.m_expandedNameTable.getExpandedTypeID(ns, lName, code, searchOnly);
/*      */   }
/*      */ 
/*      */   public short[] getMapping(String[] names, String[] uris, int[] types)
/*      */   {
/*  558 */     if (this._namesSize < 0) {
/*  559 */       return getMapping2(names, uris, types);
/*      */     }
/*      */ 
/*  563 */     int namesLength = names.length;
/*  564 */     int exLength = this.m_expandedNameTable.getSize();
/*      */ 
/*  566 */     short[] result = new short[exLength];
/*      */ 
/*  569 */     for (int i = 0; i < 14; i++) {
/*  570 */       result[i] = ((short)i);
/*      */     }
/*      */ 
/*  573 */     for (i = 14; i < exLength; i++) {
/*  574 */       result[i] = this.m_expandedNameTable.getType(i);
/*      */     }
/*      */ 
/*  578 */     for (i = 0; i < namesLength; i++) {
/*  579 */       int genType = this.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
/*      */ 
/*  583 */       if ((genType >= 0) && (genType < exLength)) {
/*  584 */         result[genType] = ((short)(i + 14));
/*      */       }
/*      */     }
/*      */ 
/*  588 */     return result;
/*      */   }
/*      */ 
/*      */   public int[] getReverseMapping(String[] names, String[] uris, int[] types)
/*      */   {
/*  597 */     int[] result = new int[names.length + 14];
/*      */ 
/*  600 */     for (int i = 0; i < 14; i++) {
/*  601 */       result[i] = i;
/*      */     }
/*      */ 
/*  605 */     for (i = 0; i < names.length; i++) {
/*  606 */       int type = this.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], true);
/*  607 */       result[(i + 14)] = type;
/*      */     }
/*  609 */     return result;
/*      */   }
/*      */ 
/*      */   private short[] getMapping2(String[] names, String[] uris, int[] types)
/*      */   {
/*  619 */     int namesLength = names.length;
/*  620 */     int exLength = this.m_expandedNameTable.getSize();
/*  621 */     int[] generalizedTypes = null;
/*  622 */     if (namesLength > 0) {
/*  623 */       generalizedTypes = new int[namesLength];
/*      */     }
/*      */ 
/*  626 */     int resultLength = exLength;
/*      */ 
/*  628 */     for (int i = 0; i < namesLength; i++)
/*      */     {
/*  633 */       generalizedTypes[i] = this.m_expandedNameTable.getExpandedTypeID(uris[i], names[i], types[i], false);
/*      */ 
/*  638 */       if ((this._namesSize < 0) && (generalizedTypes[i] >= resultLength)) {
/*  639 */         resultLength = generalizedTypes[i] + 1;
/*      */       }
/*      */     }
/*      */ 
/*  643 */     short[] result = new short[resultLength];
/*      */ 
/*  646 */     for (i = 0; i < 14; i++) {
/*  647 */       result[i] = ((short)i);
/*      */     }
/*      */ 
/*  650 */     for (i = 14; i < exLength; i++) {
/*  651 */       result[i] = this.m_expandedNameTable.getType(i);
/*      */     }
/*      */ 
/*  655 */     for (i = 0; i < namesLength; i++) {
/*  656 */       int genType = generalizedTypes[i];
/*  657 */       if ((genType >= 0) && (genType < resultLength)) {
/*  658 */         result[genType] = ((short)(i + 14));
/*      */       }
/*      */     }
/*      */ 
/*  662 */     return result;
/*      */   }
/*      */ 
/*      */   public short[] getNamespaceMapping(String[] namespaces)
/*      */   {
/*  670 */     int nsLength = namespaces.length;
/*  671 */     int mappingLength = this._uriCount;
/*      */ 
/*  673 */     short[] result = new short[mappingLength];
/*      */ 
/*  676 */     for (int i = 0; i < mappingLength; i++) {
/*  677 */       result[i] = -1;
/*      */     }
/*      */ 
/*  680 */     for (i = 0; i < nsLength; i++) {
/*  681 */       int eType = getIdForNamespace(namespaces[i]);
/*  682 */       Integer type = (Integer)this._nsIndex.get(new Integer(eType));
/*  683 */       if (type != null) {
/*  684 */         result[type.intValue()] = ((short)i);
/*      */       }
/*      */     }
/*      */ 
/*  688 */     return result;
/*      */   }
/*      */ 
/*      */   public short[] getReverseNamespaceMapping(String[] namespaces)
/*      */   {
/*  697 */     int length = namespaces.length;
/*  698 */     short[] result = new short[length];
/*      */ 
/*  700 */     for (int i = 0; i < length; i++) {
/*  701 */       int eType = getIdForNamespace(namespaces[i]);
/*  702 */       Integer type = (Integer)this._nsIndex.get(new Integer(eType));
/*  703 */       result[i] = (type == null ? -1 : type.shortValue());
/*      */     }
/*      */ 
/*  706 */     return result;
/*      */   }
/*      */ 
/*      */   public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, boolean buildIdIndex)
/*      */   {
/*  717 */     this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, buildIdIndex, false);
/*      */   }
/*      */ 
/*      */   public SAXImpl(XSLTCDTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean buildIdIndex, boolean newNameTable)
/*      */   {
/*  731 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, false, buildIdIndex, newNameTable);
/*      */ 
/*  734 */     this._dtmManager = mgr;
/*  735 */     this._size = blocksize;
/*      */ 
/*  738 */     this._xmlSpaceStack = new int[blocksize <= 64 ? 4 : 64];
/*      */ 
/*  741 */     this._xmlSpaceStack[0] = 0;
/*      */ 
/*  745 */     if ((source instanceof DOMSource)) {
/*  746 */       this._hasDOMSource = true;
/*  747 */       DOMSource domsrc = (DOMSource)source;
/*  748 */       Node node = domsrc.getNode();
/*  749 */       if ((node instanceof Document)) {
/*  750 */         this._document = ((Document)node);
/*      */       }
/*      */       else {
/*  753 */         this._document = node.getOwnerDocument();
/*      */       }
/*  755 */       this._node2Ids = new com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void migrateTo(DTMManager manager)
/*      */   {
/*  767 */     super.migrateTo(manager);
/*  768 */     if ((manager instanceof XSLTCDTMManager))
/*  769 */       this._dtmManager = ((XSLTCDTMManager)manager);
/*      */   }
/*      */ 
/*      */   public int getElementById(String idString)
/*      */   {
/*  781 */     Node node = this._document.getElementById(idString);
/*  782 */     if (node != null) {
/*  783 */       Integer id = (Integer)this._node2Ids.get(node);
/*  784 */       return id != null ? id.intValue() : -1;
/*      */     }
/*      */ 
/*  787 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean hasDOMSource()
/*      */   {
/*  796 */     return this._hasDOMSource;
/*      */   }
/*      */ 
/*      */   private void xmlSpaceDefine(String val, int node)
/*      */   {
/*  809 */     boolean setting = val.equals("preserve");
/*  810 */     if (setting != this._preserve) {
/*  811 */       this._xmlSpaceStack[(this._idx++)] = node;
/*  812 */       this._preserve = setting;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void xmlSpaceRevert(int node)
/*      */   {
/*  822 */     if (node == this._xmlSpaceStack[(this._idx - 1)]) {
/*  823 */       this._idx -= 1;
/*  824 */       this._preserve = (!this._preserve);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean getShouldStripWhitespace()
/*      */   {
/*  836 */     return this._preserve ? false : super.getShouldStripWhitespace();
/*      */   }
/*      */ 
/*      */   private void handleTextEscaping()
/*      */   {
/*  843 */     if ((this._disableEscaping) && (this._textNodeToProcess != -1) && (_type(this._textNodeToProcess) == 3))
/*      */     {
/*  845 */       if (this._dontEscape == null) {
/*  846 */         this._dontEscape = new BitArray(this._size);
/*      */       }
/*      */ 
/*  850 */       if (this._textNodeToProcess >= this._dontEscape.size()) {
/*  851 */         this._dontEscape.resize(this._dontEscape.size() * 2);
/*      */       }
/*      */ 
/*  854 */       this._dontEscape.setBit(this._textNodeToProcess);
/*  855 */       this._disableEscaping = false;
/*      */     }
/*  857 */     this._textNodeToProcess = -1;
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  870 */     super.characters(ch, start, length);
/*      */ 
/*  872 */     this._disableEscaping = (!this._escaping);
/*  873 */     this._textNodeToProcess = getNumberOfNodes();
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*  881 */     super.startDocument();
/*      */ 
/*  883 */     this._nsIndex.put(new Integer(0), new Integer(this._uriCount++));
/*  884 */     definePrefixAndUri("xml", "http://www.w3.org/XML/1998/namespace");
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  892 */     super.endDocument();
/*      */ 
/*  894 */     handleTextEscaping();
/*  895 */     this._namesSize = this.m_expandedNameTable.getSize();
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qname, Attributes attributes, Node node)
/*      */     throws SAXException
/*      */   {
/*  907 */     startElement(uri, localName, qname, attributes);
/*      */ 
/*  909 */     if (this.m_buildIdIndex)
/*  910 */       this._node2Ids.put(node, new Integer(this.m_parents.peek()));
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qname, Attributes attributes)
/*      */     throws SAXException
/*      */   {
/*  921 */     super.startElement(uri, localName, qname, attributes);
/*      */ 
/*  923 */     handleTextEscaping();
/*      */ 
/*  925 */     if (this.m_wsfilter != null)
/*      */     {
/*  929 */       int index = attributes.getIndex("xml:space");
/*  930 */       if (index >= 0)
/*  931 */         xmlSpaceDefine(attributes.getValue(index), this.m_parents.peek());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String qname)
/*      */     throws SAXException
/*      */   {
/*  942 */     super.endElement(namespaceURI, localName, qname);
/*      */ 
/*  944 */     handleTextEscaping();
/*      */ 
/*  947 */     if (this.m_wsfilter != null)
/*  948 */       xmlSpaceRevert(this.m_previous);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*  958 */     super.processingInstruction(target, data);
/*  959 */     handleTextEscaping();
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  969 */     super.ignorableWhitespace(ch, start, length);
/*  970 */     this._textNodeToProcess = getNumberOfNodes();
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  979 */     super.startPrefixMapping(prefix, uri);
/*  980 */     handleTextEscaping();
/*      */ 
/*  982 */     definePrefixAndUri(prefix, uri);
/*      */   }
/*      */ 
/*      */   private void definePrefixAndUri(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  989 */     Integer eType = new Integer(getIdForNamespace(uri));
/*  990 */     if ((Integer)this._nsIndex.get(eType) == null)
/*  991 */       this._nsIndex.put(eType, new Integer(this._uriCount++));
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 1001 */     super.comment(ch, start, length);
/* 1002 */     handleTextEscaping();
/*      */   }
/*      */ 
/*      */   public boolean setEscaping(boolean value) {
/* 1006 */     boolean temp = this._escaping;
/* 1007 */     this._escaping = value;
/* 1008 */     return temp;
/*      */   }
/*      */ 
/*      */   public void print(int node, int level)
/*      */   {
/* 1020 */     switch (getNodeType(node))
/*      */     {
/*      */     case 0:
/*      */     case 9:
/* 1024 */       print(getFirstChild(node), level);
/* 1025 */       break;
/*      */     case 3:
/*      */     case 7:
/*      */     case 8:
/* 1029 */       System.out.print(getStringValueX(node));
/* 1030 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     default:
/* 1032 */       String name = getNodeName(node);
/* 1033 */       System.out.print("<" + name);
/* 1034 */       for (int a = getFirstAttribute(node); a != -1; a = getNextAttribute(a))
/*      */       {
/* 1036 */         System.out.print("\n" + getNodeName(a) + "=\"" + getStringValueX(a) + "\"");
/*      */       }
/* 1038 */       System.out.print('>');
/* 1039 */       for (int child = getFirstChild(node); child != -1; 
/* 1040 */         child = getNextSibling(child)) {
/* 1041 */         print(child, level + 1);
/*      */       }
/* 1043 */       System.out.println("</" + name + '>');
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getNodeName(int node)
/*      */   {
/* 1054 */     int nodeh = node;
/* 1055 */     short type = getNodeType(nodeh);
/* 1056 */     switch (type)
/*      */     {
/*      */     case 0:
/*      */     case 3:
/*      */     case 8:
/*      */     case 9:
/* 1062 */       return "";
/*      */     case 13:
/* 1064 */       return getLocalName(nodeh);
/*      */     case 1:
/*      */     case 2:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 10:
/*      */     case 11:
/* 1066 */     case 12: } return super.getNodeName(nodeh);
/*      */   }
/*      */ 
/*      */   public String getNamespaceName(int node)
/*      */   {
/* 1075 */     if (node == -1)
/* 1076 */       return "";
/*      */     String s;
/* 1080 */     return (s = getNamespaceURI(node)) == null ? "" : s;
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int type, int element)
/*      */   {
/* 1089 */     for (int attr = getFirstAttribute(element); 
/* 1090 */       attr != -1; 
/* 1091 */       attr = getNextAttribute(attr))
/*      */     {
/* 1093 */       if (getExpandedTypeID(attr) == type) return attr;
/*      */     }
/* 1095 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(int type, int element)
/*      */   {
/* 1103 */     int attr = getAttributeNode(type, element);
/* 1104 */     return attr != -1 ? getStringValueX(attr) : "";
/*      */   }
/*      */ 
/*      */   public String getAttributeValue(String name, int element)
/*      */   {
/* 1112 */     return getAttributeValue(getGeneralizedType(name), element);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getChildren(int node)
/*      */   {
/* 1120 */     return new SAX2DTM2.ChildrenIterator(this).setStartNode(node);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedChildren(int type)
/*      */   {
/* 1129 */     return new SAX2DTM2.TypedChildrenIterator(this, type);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getAxisIterator(int axis)
/*      */   {
/* 1140 */     switch (axis)
/*      */     {
/*      */     case 13:
/* 1143 */       return new DTMDefaultBaseIterators.SingletonIterator(this);
/*      */     case 3:
/* 1145 */       return new SAX2DTM2.ChildrenIterator(this);
/*      */     case 10:
/* 1147 */       return new SAX2DTM2.ParentIterator(this);
/*      */     case 0:
/* 1149 */       return new SAX2DTM2.AncestorIterator(this);
/*      */     case 1:
/* 1151 */       return new SAX2DTM2.AncestorIterator(this).includeSelf();
/*      */     case 2:
/* 1153 */       return new SAX2DTM2.AttributeIterator(this);
/*      */     case 4:
/* 1155 */       return new SAX2DTM2.DescendantIterator(this);
/*      */     case 5:
/* 1157 */       return new SAX2DTM2.DescendantIterator(this).includeSelf();
/*      */     case 6:
/* 1159 */       return new SAX2DTM2.FollowingIterator(this);
/*      */     case 11:
/* 1161 */       return new SAX2DTM2.PrecedingIterator(this);
/*      */     case 7:
/* 1163 */       return new SAX2DTM2.FollowingSiblingIterator(this);
/*      */     case 12:
/* 1165 */       return new SAX2DTM2.PrecedingSiblingIterator(this);
/*      */     case 9:
/* 1167 */       return new DTMDefaultBaseIterators.NamespaceIterator(this);
/*      */     case 19:
/* 1169 */       return new DTMDefaultBaseIterators.RootIterator(this);
/*      */     case 8:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/* 1171 */     case 18: } BasisLibrary.runTimeError("AXIS_SUPPORT_ERR", Axis.getNames(axis));
/*      */ 
/* 1174 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*      */   {
/* 1184 */     if (axis == 3) {
/* 1185 */       return new SAX2DTM2.TypedChildrenIterator(this, type);
/*      */     }
/*      */ 
/* 1188 */     if (type == -1) {
/* 1189 */       return EMPTYITERATOR;
/*      */     }
/*      */ 
/* 1192 */     switch (axis)
/*      */     {
/*      */     case 13:
/* 1195 */       return new SAX2DTM2.TypedSingletonIterator(this, type);
/*      */     case 3:
/* 1197 */       return new SAX2DTM2.TypedChildrenIterator(this, type);
/*      */     case 10:
/* 1199 */       return new SAX2DTM2.ParentIterator(this).setNodeType(type);
/*      */     case 0:
/* 1201 */       return new SAX2DTM2.TypedAncestorIterator(this, type);
/*      */     case 1:
/* 1203 */       return new SAX2DTM2.TypedAncestorIterator(this, type).includeSelf();
/*      */     case 2:
/* 1205 */       return new SAX2DTM2.TypedAttributeIterator(this, type);
/*      */     case 4:
/* 1207 */       return new SAX2DTM2.TypedDescendantIterator(this, type);
/*      */     case 5:
/* 1209 */       return new SAX2DTM2.TypedDescendantIterator(this, type).includeSelf();
/*      */     case 6:
/* 1211 */       return new SAX2DTM2.TypedFollowingIterator(this, type);
/*      */     case 11:
/* 1213 */       return new SAX2DTM2.TypedPrecedingIterator(this, type);
/*      */     case 7:
/* 1215 */       return new SAX2DTM2.TypedFollowingSiblingIterator(this, type);
/*      */     case 12:
/* 1217 */       return new SAX2DTM2.TypedPrecedingSiblingIterator(this, type);
/*      */     case 9:
/* 1219 */       return new TypedNamespaceIterator(type);
/*      */     case 19:
/* 1221 */       return new SAX2DTM2.TypedRootIterator(this, type);
/*      */     case 8:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/* 1223 */     case 18: } BasisLibrary.runTimeError("TYPED_AXIS_SUPPORT_ERR", Axis.getNames(axis));
/*      */ 
/* 1226 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
/*      */   {
/* 1239 */     DTMAxisIterator iterator = null;
/*      */ 
/* 1241 */     if (ns == -1) {
/* 1242 */       return EMPTYITERATOR;
/*      */     }
/*      */ 
/* 1245 */     switch (axis) {
/*      */     case 3:
/* 1247 */       return new NamespaceChildrenIterator(ns);
/*      */     case 2:
/* 1249 */       return new NamespaceAttributeIterator(ns);
/*      */     }
/* 1251 */     return new NamespaceWildcardIterator(axis, ns);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedDescendantIterator(int type)
/*      */   {
/* 1546 */     return new SAX2DTM2.TypedDescendantIterator(this, type);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNthDescendant(int type, int n, boolean includeself)
/*      */   {
/* 1554 */     DTMAxisIterator source = new SAX2DTM2.TypedDescendantIterator(this, type);
/* 1555 */     return new DTMDefaultBaseIterators.NthDescendantIterator(this, n);
/*      */   }
/*      */ 
/*      */   public void characters(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/* 1564 */     if (node != -1)
/*      */       try {
/* 1566 */         dispatchCharactersEvents(node, handler, false);
/*      */       } catch (SAXException e) {
/* 1568 */         throw new TransletException(e);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void copy(DTMAxisIterator nodes, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*      */     int node;
/* 1580 */     while ((node = nodes.next()) != -1)
/* 1581 */       copy(node, handler);
/*      */   }
/*      */ 
/*      */   public void copy(SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/* 1590 */     copy(getDocument(), handler);
/*      */   }
/*      */ 
/*      */   public void copy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/* 1603 */     copy(node, handler, false);
/*      */   }
/*      */ 
/*      */   private final void copy(int node, SerializationHandler handler, boolean isChild)
/*      */     throws TransletException
/*      */   {
/* 1610 */     int nodeID = makeNodeIdentity(node);
/* 1611 */     int eType = _exptype2(nodeID);
/* 1612 */     int type = _exptype2Type(eType);
/*      */     try
/*      */     {
/* 1615 */       switch (type)
/*      */       {
/*      */       case 0:
/*      */       case 9:
/* 1619 */         for (int c = _firstch2(nodeID); c != -1; c = _nextsib2(c)) {
/* 1620 */           copy(makeNodeHandle(c), handler, true);
/*      */         }
/* 1622 */         break;
/*      */       case 7:
/* 1624 */         copyPI(node, handler);
/* 1625 */         break;
/*      */       case 8:
/* 1627 */         handler.comment(getStringValueX(node));
/* 1628 */         break;
/*      */       case 3:
/* 1630 */         boolean oldEscapeSetting = false;
/* 1631 */         boolean escapeBit = false;
/*      */ 
/* 1633 */         if (this._dontEscape != null) {
/* 1634 */           escapeBit = this._dontEscape.getBit(getNodeIdent(node));
/* 1635 */           if (escapeBit) {
/* 1636 */             oldEscapeSetting = handler.setEscaping(false);
/*      */           }
/*      */         }
/*      */ 
/* 1640 */         copyTextNode(nodeID, handler);
/*      */ 
/* 1642 */         if (escapeBit)
/* 1643 */           handler.setEscaping(oldEscapeSetting); break;
/*      */       case 2:
/* 1647 */         copyAttribute(nodeID, eType, handler);
/* 1648 */         break;
/*      */       case 13:
/* 1650 */         handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
/* 1651 */         break;
/*      */       case 1:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       default:
/* 1653 */         if (type == 1)
/*      */         {
/* 1656 */           String name = copyElement(nodeID, eType, handler);
/*      */ 
/* 1659 */           copyNS(nodeID, handler, !isChild);
/* 1660 */           copyAttributes(nodeID, handler);
/*      */ 
/* 1662 */           for (int c = _firstch2(nodeID); c != -1; c = _nextsib2(c)) {
/* 1663 */             copy(makeNodeHandle(c), handler, true);
/*      */           }
/*      */ 
/* 1667 */           handler.endElement(name);
/*      */         }
/*      */         else
/*      */         {
/* 1671 */           String uri = getNamespaceName(node);
/* 1672 */           if (uri.length() != 0) {
/* 1673 */             String prefix = getPrefix(node);
/* 1674 */             handler.namespaceAfterStartElement(prefix, uri);
/*      */           }
/* 1676 */           handler.addAttribute(getNodeName(node), getNodeValue(node));
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/* 1682 */       throw new TransletException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void copyPI(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/* 1692 */     String target = getNodeName(node);
/* 1693 */     String value = getStringValueX(node);
/*      */     try
/*      */     {
/* 1696 */       handler.processingInstruction(target, value);
/*      */     } catch (Exception e) {
/* 1698 */       throw new TransletException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String shallowCopy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/* 1708 */     int nodeID = makeNodeIdentity(node);
/* 1709 */     int exptype = _exptype2(nodeID);
/* 1710 */     int type = _exptype2Type(exptype);
/*      */     try
/*      */     {
/* 1713 */       switch (type)
/*      */       {
/*      */       case 1:
/* 1716 */         String name = copyElement(nodeID, exptype, handler);
/* 1717 */         copyNS(nodeID, handler, true);
/* 1718 */         return name;
/*      */       case 0:
/*      */       case 9:
/* 1721 */         return "";
/*      */       case 3:
/* 1723 */         copyTextNode(nodeID, handler);
/* 1724 */         return null;
/*      */       case 7:
/* 1726 */         copyPI(node, handler);
/* 1727 */         return null;
/*      */       case 8:
/* 1729 */         handler.comment(getStringValueX(node));
/* 1730 */         return null;
/*      */       case 13:
/* 1732 */         handler.namespaceAfterStartElement(getNodeNameX(node), getNodeValue(node));
/* 1733 */         return null;
/*      */       case 2:
/* 1735 */         copyAttribute(nodeID, exptype, handler);
/* 1736 */         return null;
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 10:
/*      */       case 11:
/* 1738 */       case 12: } String uri1 = getNamespaceName(node);
/* 1739 */       if (uri1.length() != 0) {
/* 1740 */         String prefix = getPrefix(node);
/* 1741 */         handler.namespaceAfterStartElement(prefix, uri1);
/*      */       }
/* 1743 */       handler.addAttribute(getNodeName(node), getNodeValue(node));
/* 1744 */       return null;
/*      */     }
/*      */     catch (Exception e) {
/* 1747 */       throw new TransletException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getLanguage(int node)
/*      */   {
/* 1756 */     int parent = node;
/* 1757 */     while (-1 != parent) {
/* 1758 */       if (1 == getNodeType(parent)) {
/* 1759 */         int langAttr = getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang");
/*      */ 
/* 1761 */         if (-1 != langAttr) {
/* 1762 */           return getNodeValue(langAttr);
/*      */         }
/*      */       }
/*      */ 
/* 1766 */       parent = getParent(parent);
/*      */     }
/* 1768 */     return null;
/*      */   }
/*      */ 
/*      */   public DOMBuilder getBuilder()
/*      */   {
/* 1778 */     return this;
/*      */   }
/*      */ 
/*      */   public SerializationHandler getOutputDomBuilder()
/*      */   {
/* 1787 */     return new ToXMLSAXHandler(this, "UTF-8");
/*      */   }
/*      */ 
/*      */   public DOM getResultTreeFrag(int initSize, int rtfType)
/*      */   {
/* 1795 */     return getResultTreeFrag(initSize, rtfType, true);
/*      */   }
/*      */ 
/*      */   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager)
/*      */   {
/* 1808 */     if (rtfType == 0) {
/* 1809 */       if (addToManager) {
/* 1810 */         int dtmPos = this._dtmManager.getFirstFreeDTMID();
/* 1811 */         SimpleResultTreeImpl rtf = new SimpleResultTreeImpl(this._dtmManager, dtmPos << 16);
/*      */ 
/* 1813 */         this._dtmManager.addDTM(rtf, dtmPos, 0);
/* 1814 */         return rtf;
/*      */       }
/*      */ 
/* 1817 */       return new SimpleResultTreeImpl(this._dtmManager, 0);
/*      */     }
/*      */ 
/* 1820 */     if (rtfType == 1) {
/* 1821 */       if (addToManager) {
/* 1822 */         int dtmPos = this._dtmManager.getFirstFreeDTMID();
/* 1823 */         AdaptiveResultTreeImpl rtf = new AdaptiveResultTreeImpl(this._dtmManager, dtmPos << 16, this.m_wsfilter, initSize, this.m_buildIdIndex);
/*      */ 
/* 1826 */         this._dtmManager.addDTM(rtf, dtmPos, 0);
/* 1827 */         return rtf;
/*      */       }
/*      */ 
/* 1831 */       return new AdaptiveResultTreeImpl(this._dtmManager, 0, this.m_wsfilter, initSize, this.m_buildIdIndex);
/*      */     }
/*      */ 
/* 1836 */     return (DOM)this._dtmManager.getDTM(null, true, this.m_wsfilter, true, false, false, initSize, this.m_buildIdIndex);
/*      */   }
/*      */ 
/*      */   public com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable getElementsWithIDs()
/*      */   {
/* 1846 */     if (this.m_idAttributes == null) {
/* 1847 */       return null;
/*      */     }
/*      */ 
/* 1851 */     Enumeration idValues = this.m_idAttributes.keys();
/* 1852 */     if (!idValues.hasMoreElements()) {
/* 1853 */       return null;
/*      */     }
/*      */ 
/* 1856 */     com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable idAttrsTable = new com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable();
/*      */ 
/* 1858 */     while (idValues.hasMoreElements()) {
/* 1859 */       Object idValue = idValues.nextElement();
/*      */ 
/* 1861 */       idAttrsTable.put(idValue, this.m_idAttributes.get(idValue));
/*      */     }
/*      */ 
/* 1864 */     return idAttrsTable;
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/* 1876 */     if (this._document != null) {
/* 1877 */       String uri = "";
/* 1878 */       DocumentType doctype = this._document.getDoctype();
/* 1879 */       if (doctype != null) {
/* 1880 */         NamedNodeMap entities = doctype.getEntities();
/*      */ 
/* 1882 */         if (entities == null) {
/* 1883 */           return uri;
/*      */         }
/*      */ 
/* 1886 */         Entity entity = (Entity)entities.getNamedItem(name);
/*      */ 
/* 1888 */         if (entity == null) {
/* 1889 */           return uri;
/*      */         }
/*      */ 
/* 1892 */         String notationName = entity.getNotationName();
/* 1893 */         if (notationName != null) {
/* 1894 */           uri = entity.getSystemId();
/* 1895 */           if (uri == null) {
/* 1896 */             uri = entity.getPublicId();
/*      */           }
/*      */         }
/*      */       }
/* 1900 */       return uri;
/*      */     }
/*      */ 
/* 1903 */     return super.getUnparsedEntityURI(name);
/*      */   }
/*      */ 
/*      */   public final class NamespaceAttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private final int _nsType;
/*      */ 
/*      */     public NamespaceAttributeIterator(int nsType)
/*      */     {
/* 1473 */       super();
/*      */ 
/* 1475 */       this._nsType = nsType;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1488 */       if (node == 0) {
/* 1489 */         node = SAXImpl.this.getDocument();
/*      */       }
/*      */ 
/* 1492 */       if (this._isRestartable) {
/* 1493 */         int nsType = this._nsType;
/*      */ 
/* 1495 */         this._startNode = node;
/*      */ 
/* 1497 */         for (node = SAXImpl.this.getFirstAttribute(node); 
/* 1498 */           node != -1; 
/* 1499 */           node = SAXImpl.this.getNextAttribute(node)) {
/* 1500 */           if (SAXImpl.this.getNSType(node) == nsType)
/*      */           {
/*      */             break;
/*      */           }
/*      */         }
/* 1505 */         this._currentNode = node;
/* 1506 */         return resetPosition();
/*      */       }
/*      */ 
/* 1509 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1518 */       int node = this._currentNode;
/* 1519 */       int nsType = this._nsType;
/*      */ 
/* 1522 */       if (node == -1) {
/* 1523 */         return -1;
/*      */       }
/*      */ 
/* 1526 */       for (int nextNode = SAXImpl.this.getNextAttribute(node); 
/* 1527 */         nextNode != -1; 
/* 1528 */         nextNode = SAXImpl.this.getNextAttribute(nextNode)) {
/* 1529 */         if (SAXImpl.this.getNSType(nextNode) == nsType)
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/* 1534 */       this._currentNode = nextNode;
/*      */ 
/* 1536 */       return returnNode(node);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class NamespaceChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private final int _nsType;
/*      */ 
/*      */     public NamespaceChildrenIterator(int type)
/*      */     {
/* 1402 */       super();
/* 1403 */       this._nsType = type;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1416 */       if (node == 0) {
/* 1417 */         node = SAXImpl.this.getDocument();
/*      */       }
/*      */ 
/* 1420 */       if (this._isRestartable) {
/* 1421 */         this._startNode = node;
/* 1422 */         this._currentNode = (node == -1 ? -1 : -2);
/*      */ 
/* 1424 */         return resetPosition();
/*      */       }
/*      */ 
/* 1427 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1436 */       if (this._currentNode != -1) {
/* 1437 */         for (int node = -2 == this._currentNode ? SAXImpl.this._firstch(SAXImpl.this.makeNodeIdentity(this._startNode)) : SAXImpl.this._nextsib(this._currentNode); 
/* 1440 */           node != -1; 
/* 1441 */           node = SAXImpl.this._nextsib(node)) {
/* 1442 */           int nodeHandle = SAXImpl.this.makeNodeHandle(node);
/*      */ 
/* 1444 */           if (SAXImpl.this.getNSType(nodeHandle) == this._nsType) {
/* 1445 */             this._currentNode = node;
/*      */ 
/* 1447 */             return returnNode(nodeHandle);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1452 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class NamespaceWildcardIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     protected int m_nsType;
/*      */     protected DTMAxisIterator m_baseIterator;
/*      */ 
/*      */     public NamespaceWildcardIterator(int axis, int nsType)
/*      */     {
/* 1281 */       super();
/* 1282 */       this.m_nsType = nsType;
/*      */ 
/* 1286 */       switch (axis)
/*      */       {
/*      */       case 2:
/* 1290 */         this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
/*      */       case 9:
/* 1295 */         this.m_baseIterator = SAXImpl.this.getAxisIterator(axis);
/*      */       }
/*      */ 
/* 1300 */       this.m_baseIterator = SAXImpl.this.getTypedAxisIterator(axis, 1);
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1315 */       if (this._isRestartable) {
/* 1316 */         this._startNode = node;
/* 1317 */         this.m_baseIterator.setStartNode(node);
/* 1318 */         resetPosition();
/*      */       }
/* 1320 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*      */       int node;
/* 1331 */       while ((node = this.m_baseIterator.next()) != -1)
/*      */       {
/* 1333 */         if (SAXImpl.this.getNSType(node) == this.m_nsType) {
/* 1334 */           return returnNode(node);
/*      */         }
/*      */       }
/*      */ 
/* 1338 */       return -1;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator cloneIterator()
/*      */     {
/*      */       try
/*      */       {
/* 1349 */         DTMAxisIterator nestedClone = this.m_baseIterator.cloneIterator();
/* 1350 */         NamespaceWildcardIterator clone = (NamespaceWildcardIterator)super.clone();
/*      */ 
/* 1353 */         clone.m_baseIterator = nestedClone;
/* 1354 */         clone.m_nsType = this.m_nsType;
/* 1355 */         clone._isRestartable = false;
/*      */ 
/* 1357 */         return clone;
/*      */       } catch (CloneNotSupportedException e) {
/* 1359 */         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*      */       }
/* 1361 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isReverse()
/*      */     {
/* 1371 */       return this.m_baseIterator.isReverse();
/*      */     }
/*      */ 
/*      */     public void setMark() {
/* 1375 */       this.m_baseIterator.setMark();
/*      */     }
/*      */ 
/*      */     public void gotoMark() {
/* 1379 */       this.m_baseIterator.gotoMark();
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class NodeValueIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private DTMAxisIterator _source;
/*      */     private String _value;
/*      */     private boolean _op;
/*      */     private final boolean _isReverse;
/*  366 */     private int _returnType = 1;
/*      */ 
/*      */     public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op)
/*      */     {
/*  370 */       super();
/*  371 */       this._source = source;
/*  372 */       this._returnType = returnType;
/*  373 */       this._value = value;
/*  374 */       this._op = op;
/*  375 */       this._isReverse = source.isReverse();
/*      */     }
/*      */ 
/*      */     public boolean isReverse()
/*      */     {
/*  380 */       return this._isReverse;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator cloneIterator()
/*      */     {
/*      */       try {
/*  386 */         NodeValueIterator clone = (NodeValueIterator)super.clone();
/*  387 */         clone._isRestartable = false;
/*  388 */         clone._source = this._source.cloneIterator();
/*  389 */         clone._value = this._value;
/*  390 */         clone._op = this._op;
/*  391 */         return clone.reset();
/*      */       }
/*      */       catch (CloneNotSupportedException e) {
/*  394 */         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*      */       }
/*  396 */       return null;
/*      */     }
/*      */ 
/*      */     public void setRestartable(boolean isRestartable)
/*      */     {
/*  402 */       this._isRestartable = isRestartable;
/*  403 */       this._source.setRestartable(isRestartable);
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator reset()
/*      */     {
/*  408 */       this._source.reset();
/*  409 */       return resetPosition();
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*      */       int node;
/*  415 */       while ((node = this._source.next()) != -1) {
/*  416 */         String val = SAXImpl.this.getStringValueX(node);
/*  417 */         if (this._value.equals(val) == this._op) {
/*  418 */           if (this._returnType == 0) {
/*  419 */             return returnNode(node);
/*      */           }
/*      */ 
/*  422 */           return returnNode(SAXImpl.this.getParent(node));
/*      */         }
/*      */       }
/*      */ 
/*  426 */       return -1;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  431 */       if (this._isRestartable) {
/*  432 */         this._source.setStartNode(this._startNode = node);
/*  433 */         return resetPosition();
/*      */       }
/*  435 */       return this;
/*      */     }
/*      */ 
/*      */     public void setMark()
/*      */     {
/*  440 */       this._source.setMark();
/*      */     }
/*      */ 
/*      */     public void gotoMark()
/*      */     {
/*  445 */       this._source.gotoMark();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TypedNamespaceIterator extends DTMDefaultBaseIterators.NamespaceIterator
/*      */   {
/*      */     private String _nsPrefix;
/*      */ 
/*      */     public TypedNamespaceIterator(int nodeType)
/*      */     {
/*  328 */       super();
/*  329 */       if (SAXImpl.this.m_expandedNameTable != null)
/*  330 */         this._nsPrefix = SAXImpl.this.m_expandedNameTable.getLocalName(nodeType);
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  340 */       if ((this._nsPrefix == null) || (this._nsPrefix.length() == 0)) {
/*  341 */         return -1;
/*      */       }
/*  343 */       int node = -1;
/*  344 */       for (node = super.next(); node != -1; node = super.next()) {
/*  345 */         if (this._nsPrefix.compareTo(SAXImpl.this.getLocalName(node)) == 0) {
/*  346 */           return returnNode(node);
/*      */         }
/*      */       }
/*  349 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl
 * JD-Core Version:    0.6.2
 */