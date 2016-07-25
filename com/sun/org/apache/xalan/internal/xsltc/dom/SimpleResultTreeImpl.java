/*      */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
/*      */ import com.sun.org.apache.xml.internal.serializer.EmptySerializer;
/*      */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringDefault;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class SimpleResultTreeImpl extends EmptySerializer
/*      */   implements DOM, DTM
/*      */ {
/*  224 */   private static final DTMAxisIterator EMPTY_ITERATOR = new DTMAxisIteratorBase() {
/*      */     public DTMAxisIterator reset() {
/*  226 */       return this; } 
/*  227 */     public DTMAxisIterator setStartNode(int node) { return this; } 
/*  228 */     public int next() { return -1; } 
/*      */     public void setMark() {  } 
/*      */     public void gotoMark() {  } 
/*  231 */     public int getLast() { return 0; } 
/*  232 */     public int getPosition() { return 0; } 
/*  233 */     public DTMAxisIterator cloneIterator() { return this; }
/*      */ 
/*      */ 
/*      */     public void setRestartable(boolean isRestartable)
/*      */     {
/*      */     }
/*  224 */   };
/*      */   public static final int RTF_ROOT = 0;
/*      */   public static final int RTF_TEXT = 1;
/*      */   public static final int NUMBER_OF_NODES = 2;
/*  248 */   private static int _documentURIIndex = 0;
/*      */   private static final String EMPTY_STR = "";
/*      */   private String _text;
/*      */   protected String[] _textArray;
/*      */   protected XSLTCDTMManager _dtmManager;
/*  266 */   protected int _size = 0;
/*      */   private int _documentID;
/*  272 */   private BitArray _dontEscape = null;
/*      */ 
/*  275 */   private boolean _escaping = true;
/*      */ 
/*      */   public SimpleResultTreeImpl(XSLTCDTMManager dtmManager, int documentID)
/*      */   {
/*  280 */     this._dtmManager = dtmManager;
/*  281 */     this._documentID = documentID;
/*  282 */     this._textArray = new String[4];
/*      */   }
/*      */ 
/*      */   public DTMManagerDefault getDTMManager()
/*      */   {
/*  287 */     return this._dtmManager;
/*      */   }
/*      */ 
/*      */   public int getDocument()
/*      */   {
/*  293 */     return this._documentID;
/*      */   }
/*      */ 
/*      */   public String getStringValue()
/*      */   {
/*  299 */     return this._text;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getIterator()
/*      */   {
/*  304 */     return new SingletonIterator(getDocument());
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getChildren(int node)
/*      */   {
/*  309 */     return new SimpleIterator().setStartNode(node);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedChildren(int type)
/*      */   {
/*  314 */     return new SimpleIterator(1, type);
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getAxisIterator(int axis)
/*      */   {
/*  321 */     switch (axis)
/*      */     {
/*      */     case 3:
/*      */     case 4:
/*  325 */       return new SimpleIterator(1);
/*      */     case 0:
/*      */     case 10:
/*  328 */       return new SimpleIterator(0);
/*      */     case 1:
/*  330 */       return new SimpleIterator(0).includeSelf();
/*      */     case 5:
/*  332 */       return new SimpleIterator(1).includeSelf();
/*      */     case 13:
/*  334 */       return new SingletonIterator();
/*      */     case 2:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*  336 */     case 12: } return EMPTY_ITERATOR;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*      */   {
/*  342 */     switch (axis)
/*      */     {
/*      */     case 3:
/*      */     case 4:
/*  346 */       return new SimpleIterator(1, type);
/*      */     case 0:
/*      */     case 10:
/*  349 */       return new SimpleIterator(0, type);
/*      */     case 1:
/*  351 */       return new SimpleIterator(0, type).includeSelf();
/*      */     case 5:
/*  353 */       return new SimpleIterator(1, type).includeSelf();
/*      */     case 13:
/*  355 */       return new SingletonIterator(type);
/*      */     case 2:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*  357 */     case 12: } return EMPTY_ITERATOR;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
/*      */   {
/*  364 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
/*      */   {
/*  369 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iter, int returnType, String value, boolean op)
/*      */   {
/*  376 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node)
/*      */   {
/*  381 */     return source;
/*      */   }
/*      */ 
/*      */   public String getNodeName(int node)
/*      */   {
/*  386 */     if (getNodeIdent(node) == 1) {
/*  387 */       return "#text";
/*      */     }
/*  389 */     return "";
/*      */   }
/*      */ 
/*      */   public String getNodeNameX(int node)
/*      */   {
/*  394 */     return "";
/*      */   }
/*      */ 
/*      */   public String getNamespaceName(int node)
/*      */   {
/*  399 */     return "";
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(int nodeHandle)
/*      */   {
/*  405 */     int nodeID = getNodeIdent(nodeHandle);
/*  406 */     if (nodeID == 1)
/*  407 */       return 3;
/*  408 */     if (nodeID == 0) {
/*  409 */       return 0;
/*      */     }
/*  411 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNamespaceType(int node)
/*      */   {
/*  416 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getParent(int nodeHandle)
/*      */   {
/*  421 */     int nodeID = getNodeIdent(nodeHandle);
/*  422 */     return nodeID == 1 ? getNodeHandle(0) : -1;
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int gType, int element)
/*      */   {
/*  427 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getStringValueX(int nodeHandle)
/*      */   {
/*  432 */     int nodeID = getNodeIdent(nodeHandle);
/*  433 */     if ((nodeID == 0) || (nodeID == 1)) {
/*  434 */       return this._text;
/*      */     }
/*  436 */     return "";
/*      */   }
/*      */ 
/*      */   public void copy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  442 */     characters(node, handler);
/*      */   }
/*      */ 
/*      */   public void copy(DTMAxisIterator nodes, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*      */     int node;
/*  449 */     while ((node = nodes.next()) != -1)
/*      */     {
/*  451 */       copy(node, handler);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String shallowCopy(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  458 */     characters(node, handler);
/*  459 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean lessThan(int node1, int node2)
/*      */   {
/*  464 */     if (node1 == -1) {
/*  465 */       return false;
/*      */     }
/*  467 */     if (node2 == -1) {
/*  468 */       return true;
/*      */     }
/*      */ 
/*  471 */     return node1 < node2;
/*      */   }
/*      */ 
/*      */   public void characters(int node, SerializationHandler handler)
/*      */     throws TransletException
/*      */   {
/*  483 */     int nodeID = getNodeIdent(node);
/*  484 */     if ((nodeID == 0) || (nodeID == 1)) {
/*  485 */       boolean escapeBit = false;
/*  486 */       boolean oldEscapeSetting = false;
/*      */       try
/*      */       {
/*  489 */         for (int i = 0; i < this._size; i++)
/*      */         {
/*  491 */           if (this._dontEscape != null) {
/*  492 */             escapeBit = this._dontEscape.getBit(i);
/*  493 */             if (escapeBit) {
/*  494 */               oldEscapeSetting = handler.setEscaping(false);
/*      */             }
/*      */           }
/*      */ 
/*  498 */           handler.characters(this._textArray[i]);
/*      */ 
/*  500 */           if (escapeBit)
/*  501 */             handler.setEscaping(oldEscapeSetting);
/*      */         }
/*      */       }
/*      */       catch (SAXException e) {
/*  505 */         throw new TransletException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Node makeNode(int index)
/*      */   {
/*  513 */     return null;
/*      */   }
/*      */ 
/*      */   public Node makeNode(DTMAxisIterator iter)
/*      */   {
/*  518 */     return null;
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(int index)
/*      */   {
/*  523 */     return null;
/*      */   }
/*      */ 
/*      */   public NodeList makeNodeList(DTMAxisIterator iter)
/*      */   {
/*  528 */     return null;
/*      */   }
/*      */ 
/*      */   public String getLanguage(int node)
/*      */   {
/*  533 */     return null;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  538 */     return 2;
/*      */   }
/*      */ 
/*      */   public String getDocumentURI(int node)
/*      */   {
/*  543 */     return "simple_rtf" + _documentURIIndex++;
/*      */   }
/*      */ 
/*      */   public void setFilter(StripFilter filter)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isElement(int node)
/*      */   {
/*  556 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAttribute(int node)
/*      */   {
/*  561 */     return false;
/*      */   }
/*      */ 
/*      */   public String lookupNamespace(int node, String prefix)
/*      */     throws TransletException
/*      */   {
/*  567 */     return null;
/*      */   }
/*      */ 
/*      */   public int getNodeIdent(int nodehandle)
/*      */   {
/*  575 */     return nodehandle != -1 ? nodehandle - this._documentID : -1;
/*      */   }
/*      */ 
/*      */   public int getNodeHandle(int nodeId)
/*      */   {
/*  583 */     return nodeId != -1 ? nodeId + this._documentID : -1;
/*      */   }
/*      */ 
/*      */   public DOM getResultTreeFrag(int initialSize, int rtfType)
/*      */   {
/*  588 */     return null;
/*      */   }
/*      */ 
/*      */   public DOM getResultTreeFrag(int initialSize, int rtfType, boolean addToManager)
/*      */   {
/*  593 */     return null;
/*      */   }
/*      */ 
/*      */   public SerializationHandler getOutputDomBuilder()
/*      */   {
/*  598 */     return this;
/*      */   }
/*      */ 
/*      */   public int getNSType(int node)
/*      */   {
/*  603 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/*  608 */     return null;
/*      */   }
/*      */ 
/*      */   public Hashtable getElementsWithIDs()
/*      */   {
/*  613 */     return null;
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  632 */     if (this._size == 1) {
/*  633 */       this._text = this._textArray[0];
/*      */     } else {
/*  635 */       StringBuffer buffer = new StringBuffer();
/*  636 */       for (int i = 0; i < this._size; i++) {
/*  637 */         buffer.append(this._textArray[i]);
/*      */       }
/*  639 */       this._text = buffer.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(String str)
/*      */     throws SAXException
/*      */   {
/*  646 */     if (this._size >= this._textArray.length) {
/*  647 */       String[] newTextArray = new String[this._textArray.length * 2];
/*  648 */       System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
/*  649 */       this._textArray = newTextArray;
/*      */     }
/*      */ 
/*  654 */     if (!this._escaping)
/*      */     {
/*  656 */       if (this._dontEscape == null) {
/*  657 */         this._dontEscape = new BitArray(8);
/*      */       }
/*      */ 
/*  661 */       if (this._size >= this._dontEscape.size()) {
/*  662 */         this._dontEscape.resize(this._dontEscape.size() * 2);
/*      */       }
/*  664 */       this._dontEscape.setBit(this._size);
/*      */     }
/*      */ 
/*  667 */     this._textArray[(this._size++)] = str;
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int offset, int length)
/*      */     throws SAXException
/*      */   {
/*  673 */     if (this._size >= this._textArray.length) {
/*  674 */       String[] newTextArray = new String[this._textArray.length * 2];
/*  675 */       System.arraycopy(this._textArray, 0, newTextArray, 0, this._textArray.length);
/*  676 */       this._textArray = newTextArray;
/*      */     }
/*      */ 
/*  679 */     if (!this._escaping) {
/*  680 */       if (this._dontEscape == null) {
/*  681 */         this._dontEscape = new BitArray(8);
/*      */       }
/*      */ 
/*  684 */       if (this._size >= this._dontEscape.size()) {
/*  685 */         this._dontEscape.resize(this._dontEscape.size() * 2);
/*      */       }
/*  687 */       this._dontEscape.setBit(this._size);
/*      */     }
/*      */ 
/*  690 */     this._textArray[(this._size++)] = new String(ch, offset, length);
/*      */   }
/*      */ 
/*      */   public boolean setEscaping(boolean escape)
/*      */     throws SAXException
/*      */   {
/*  696 */     boolean temp = this._escaping;
/*  697 */     this._escaping = escape;
/*  698 */     return temp;
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value)
/*      */   {
/*      */   }
/*      */ 
/*      */   public DTMAxisTraverser getAxisTraverser(int axis)
/*      */   {
/*  722 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes(int nodeHandle)
/*      */   {
/*  727 */     return getNodeIdent(nodeHandle) == 0;
/*      */   }
/*      */ 
/*      */   public int getFirstChild(int nodeHandle)
/*      */   {
/*  732 */     int nodeID = getNodeIdent(nodeHandle);
/*  733 */     if (nodeID == 0) {
/*  734 */       return getNodeHandle(1);
/*      */     }
/*  736 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeHandle)
/*      */   {
/*  741 */     return getFirstChild(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int elementHandle, String namespaceURI, String name)
/*      */   {
/*  746 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getFirstAttribute(int nodeHandle)
/*      */   {
/*  751 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
/*      */   {
/*  756 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextSibling(int nodeHandle)
/*      */   {
/*  761 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getPreviousSibling(int nodeHandle)
/*      */   {
/*  766 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextAttribute(int nodeHandle)
/*      */   {
/*  771 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
/*      */   {
/*  777 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getOwnerDocument(int nodeHandle)
/*      */   {
/*  782 */     return getDocument();
/*      */   }
/*      */ 
/*      */   public int getDocumentRoot(int nodeHandle)
/*      */   {
/*  787 */     return getDocument();
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/*  792 */     return new XMLStringDefault(getStringValueX(nodeHandle));
/*      */   }
/*      */ 
/*      */   public int getStringValueChunkCount(int nodeHandle)
/*      */   {
/*  797 */     return 0;
/*      */   }
/*      */ 
/*      */   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
/*      */   {
/*  803 */     return null;
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(String namespace, String localName, int type)
/*      */   {
/*  808 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getLocalNameFromExpandedNameID(int ExpandedNameID)
/*      */   {
/*  813 */     return "";
/*      */   }
/*      */ 
/*      */   public String getNamespaceFromExpandedNameID(int ExpandedNameID)
/*      */   {
/*  818 */     return "";
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/*  823 */     return "";
/*      */   }
/*      */ 
/*      */   public String getPrefix(int nodeHandle)
/*      */   {
/*  828 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int nodeHandle)
/*      */   {
/*  833 */     return "";
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/*  838 */     return getNodeIdent(nodeHandle) == 1 ? this._text : null;
/*      */   }
/*      */ 
/*      */   public short getNodeType(int nodeHandle)
/*      */   {
/*  843 */     int nodeID = getNodeIdent(nodeHandle);
/*  844 */     if (nodeID == 1)
/*  845 */       return 3;
/*  846 */     if (nodeID == 0) {
/*  847 */       return 0;
/*      */     }
/*  849 */     return -1;
/*      */   }
/*      */ 
/*      */   public short getLevel(int nodeHandle)
/*      */   {
/*  855 */     int nodeID = getNodeIdent(nodeHandle);
/*  856 */     if (nodeID == 1)
/*  857 */       return 2;
/*  858 */     if (nodeID == 0) {
/*  859 */       return 1;
/*      */     }
/*  861 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/*  866 */     return false;
/*      */   }
/*      */ 
/*      */   public String getDocumentBaseURI()
/*      */   {
/*  871 */     return "";
/*      */   }
/*      */ 
/*      */   public void setDocumentBaseURI(String baseURI)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String getDocumentSystemIdentifier(int nodeHandle)
/*      */   {
/*  880 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentEncoding(int nodeHandle)
/*      */   {
/*  885 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentStandalone(int nodeHandle)
/*      */   {
/*  890 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentVersion(int documentHandle)
/*      */   {
/*  895 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getDocumentAllDeclarationsProcessed()
/*      */   {
/*  900 */     return false;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationSystemIdentifier()
/*      */   {
/*  905 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationPublicIdentifier()
/*      */   {
/*  910 */     return null;
/*      */   }
/*      */ 
/*      */   public int getElementById(String elementId)
/*      */   {
/*  915 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean supportsPreStripping()
/*      */   {
/*  920 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isNodeAfter(int firstNodeHandle, int secondNodeHandle)
/*      */   {
/*  925 */     return lessThan(firstNodeHandle, secondNodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean isCharacterElementContentWhitespace(int nodeHandle)
/*      */   {
/*  930 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
/*      */   {
/*  935 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int attributeHandle)
/*      */   {
/*  940 */     return false;
/*      */   }
/*      */ 
/*      */   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void dispatchToEvents(int nodeHandle, ContentHandler ch)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Node getNode(int nodeHandle)
/*      */   {
/*  958 */     return makeNode(nodeHandle);
/*      */   }
/*      */ 
/*      */   public boolean needsTwoThreads()
/*      */   {
/*  963 */     return false;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/*  968 */     return null;
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler()
/*      */   {
/*  973 */     return null;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/*  978 */     return null;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/*  983 */     return null;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/*  988 */     return null;
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler()
/*      */   {
/*  993 */     return null;
/*      */   }
/*      */ 
/*      */   public void appendChild(int newChild, boolean clone, boolean cloneDepth)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void appendTextChild(String str)
/*      */   {
/*      */   }
/*      */ 
/*      */   public SourceLocator getSourceLocatorFor(int node)
/*      */   {
/* 1006 */     return null;
/*      */   }
/*      */ 
/*      */   public void documentRegistration()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void documentRelease()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void migrateTo(DTMManager manager)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final class SimpleIterator extends DTMAxisIteratorBase
/*      */   {
/*      */     static final int DIRECTION_UP = 0;
/*      */     static final int DIRECTION_DOWN = 1;
/*      */     static final int NO_TYPE = -1;
/*   83 */     int _direction = 1;
/*      */ 
/*   85 */     int _type = -1;
/*      */     int _currentNode;
/*      */ 
/*      */     public SimpleIterator()
/*      */     {
/*      */     }
/*      */ 
/*      */     public SimpleIterator(int direction)
/*      */     {
/*   94 */       this._direction = direction;
/*      */     }
/*      */ 
/*      */     public SimpleIterator(int direction, int type)
/*      */     {
/*   99 */       this._direction = direction;
/*  100 */       this._type = type;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  107 */       if (this._direction == 1) {
/*  108 */         while (this._currentNode < 2) {
/*  109 */           if (this._type != -1) {
/*  110 */             if (((this._currentNode == 0) && (this._type == 0)) || ((this._currentNode == 1) && (this._type == 3)))
/*      */             {
/*  112 */               return returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode++));
/*      */             }
/*  114 */             this._currentNode += 1;
/*      */           }
/*      */           else {
/*  117 */             return returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode++));
/*      */           }
/*      */         }
/*  120 */         return -1;
/*      */       }
/*      */ 
/*  124 */       while (this._currentNode >= 0) {
/*  125 */         if (this._type != -1) {
/*  126 */           if (((this._currentNode == 0) && (this._type == 0)) || ((this._currentNode == 1) && (this._type == 3)))
/*      */           {
/*  128 */             return returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode--));
/*      */           }
/*  130 */           this._currentNode -= 1;
/*      */         }
/*      */         else {
/*  133 */           return returnNode(SimpleResultTreeImpl.this.getNodeHandle(this._currentNode--));
/*      */         }
/*      */       }
/*  136 */       return -1;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int nodeHandle)
/*      */     {
/*  142 */       int nodeID = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle);
/*  143 */       this._startNode = nodeID;
/*      */ 
/*  146 */       if ((!this._includeSelf) && (nodeID != -1)) {
/*  147 */         if (this._direction == 1)
/*  148 */           nodeID++;
/*  149 */         else if (this._direction == 0) {
/*  150 */           nodeID--;
/*      */         }
/*      */       }
/*  153 */       this._currentNode = nodeID;
/*  154 */       return this;
/*      */     }
/*      */ 
/*      */     public void setMark()
/*      */     {
/*  159 */       this._markedNode = this._currentNode;
/*      */     }
/*      */ 
/*      */     public void gotoMark()
/*      */     {
/*  164 */       this._currentNode = this._markedNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class SingletonIterator extends DTMAxisIteratorBase
/*      */   {
/*      */     static final int NO_TYPE = -1;
/*  175 */     int _type = -1;
/*      */     int _currentNode;
/*      */ 
/*      */     public SingletonIterator()
/*      */     {
/*      */     }
/*      */ 
/*      */     public SingletonIterator(int type)
/*      */     {
/*  184 */       this._type = type;
/*      */     }
/*      */ 
/*      */     public void setMark()
/*      */     {
/*  189 */       this._markedNode = this._currentNode;
/*      */     }
/*      */ 
/*      */     public void gotoMark()
/*      */     {
/*  194 */       this._currentNode = this._markedNode;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int nodeHandle)
/*      */     {
/*  199 */       this._currentNode = (this._startNode = SimpleResultTreeImpl.this.getNodeIdent(nodeHandle));
/*  200 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  205 */       if (this._currentNode == -1) {
/*  206 */         return -1;
/*      */       }
/*  208 */       this._currentNode = -1;
/*      */ 
/*  210 */       if (this._type != -1) {
/*  211 */         if (((this._currentNode == 0) && (this._type == 0)) || ((this._currentNode == 1) && (this._type == 3)))
/*      */         {
/*  213 */           return SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
/*      */         }
/*      */       }
/*  216 */       else return SimpleResultTreeImpl.this.getNodeHandle(this._currentNode);
/*      */ 
/*  218 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.SimpleResultTreeImpl
 * JD-Core Version:    0.6.2
 */