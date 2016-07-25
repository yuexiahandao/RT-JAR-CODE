/*      */ package com.sun.org.apache.xml.internal.dtm.ref;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.io.PrintStream;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class DTMDocumentImpl
/*      */   implements DTM, ContentHandler, LexicalHandler
/*      */ {
/*      */   protected static final byte DOCHANDLE_SHIFT = 22;
/*      */   protected static final int NODEHANDLE_MASK = 8388607;
/*      */   protected static final int DOCHANDLE_MASK = -8388608;
/*   78 */   int m_docHandle = -1;
/*   79 */   int m_docElement = -1;
/*      */ 
/*   82 */   int currentParent = 0;
/*   83 */   int previousSibling = 0;
/*   84 */   protected int m_currentNode = -1;
/*      */ 
/*   90 */   private boolean previousSiblingWasParent = false;
/*      */ 
/*   92 */   int[] gotslot = new int[4];
/*      */ 
/*   95 */   private boolean done = false;
/*   96 */   boolean m_isError = false;
/*      */ 
/*   98 */   private final boolean DEBUG = false;
/*      */   protected String m_documentBaseURI;
/*  114 */   private IncrementalSAXSource m_incrSAXSource = null;
/*      */ 
/*  123 */   ChunkedIntArray nodes = new ChunkedIntArray(4);
/*      */ 
/*  127 */   private FastStringBuffer m_char = new FastStringBuffer();
/*      */ 
/*  130 */   private int m_char_current_start = 0;
/*      */ 
/*  137 */   private DTMStringPool m_localNames = new DTMStringPool();
/*  138 */   private DTMStringPool m_nsNames = new DTMStringPool();
/*  139 */   private DTMStringPool m_prefixNames = new DTMStringPool();
/*      */ 
/*  147 */   private ExpandedNameTable m_expandedNames = new ExpandedNameTable();
/*      */   private XMLStringFactory m_xsf;
/* 1588 */   private static final String[] fixednames = { null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null };
/*      */ 
/*      */   public DTMDocumentImpl(DTMManager mgr, int documentNumber, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory)
/*      */   {
/*  166 */     initDocument(documentNumber);
/*  167 */     this.m_xsf = xstringfactory;
/*      */   }
/*      */ 
/*      */   public void setIncrementalSAXSource(IncrementalSAXSource source)
/*      */   {
/*  183 */     this.m_incrSAXSource = source;
/*      */ 
/*  186 */     source.setContentHandler(this);
/*  187 */     source.setLexicalHandler(this);
/*      */   }
/*      */ 
/*      */   private final int appendNode(int w0, int w1, int w2, int w3)
/*      */   {
/*  211 */     int slotnumber = this.nodes.appendSlot(w0, w1, w2, w3);
/*      */ 
/*  215 */     if (this.previousSiblingWasParent) {
/*  216 */       this.nodes.writeEntry(this.previousSibling, 2, slotnumber);
/*      */     }
/*  218 */     this.previousSiblingWasParent = false;
/*      */ 
/*  220 */     return slotnumber;
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setLocalNameTable(DTMStringPool poolRef)
/*      */   {
/*  243 */     this.m_localNames = poolRef;
/*      */   }
/*      */ 
/*      */   public DTMStringPool getLocalNameTable()
/*      */   {
/*  252 */     return this.m_localNames;
/*      */   }
/*      */ 
/*      */   public void setNsNameTable(DTMStringPool poolRef)
/*      */   {
/*  263 */     this.m_nsNames = poolRef;
/*      */   }
/*      */ 
/*      */   public DTMStringPool getNsNameTable()
/*      */   {
/*  272 */     return this.m_nsNames;
/*      */   }
/*      */ 
/*      */   public void setPrefixNameTable(DTMStringPool poolRef)
/*      */   {
/*  283 */     this.m_prefixNames = poolRef;
/*      */   }
/*      */ 
/*      */   public DTMStringPool getPrefixNameTable()
/*      */   {
/*  292 */     return this.m_prefixNames;
/*      */   }
/*      */ 
/*      */   void setContentBuffer(FastStringBuffer buffer)
/*      */   {
/*  302 */     this.m_char = buffer;
/*      */   }
/*      */ 
/*      */   FastStringBuffer getContentBuffer()
/*      */   {
/*  311 */     return this.m_char;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/*  325 */     if ((this.m_incrSAXSource instanceof IncrementalSAXSource_Filter)) {
/*  326 */       return (ContentHandler)this.m_incrSAXSource;
/*      */     }
/*  328 */     return this;
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler()
/*      */   {
/*  344 */     if ((this.m_incrSAXSource instanceof IncrementalSAXSource_Filter)) {
/*  345 */       return (LexicalHandler)this.m_incrSAXSource;
/*      */     }
/*  347 */     return this;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/*  358 */     return null;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/*  369 */     return null;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/*  380 */     return null;
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler()
/*      */   {
/*  391 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean needsTwoThreads()
/*      */   {
/*  401 */     return null != this.m_incrSAXSource;
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  415 */     this.m_char.append(ch, start, length);
/*      */   }
/*      */ 
/*      */   private void processAccumulatedText()
/*      */   {
/*  421 */     int len = this.m_char.length();
/*  422 */     if (len != this.m_char_current_start)
/*      */     {
/*  425 */       appendTextChild(this.m_char_current_start, len - this.m_char_current_start);
/*  426 */       this.m_char_current_start = len;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*  434 */     appendEndDocument();
/*      */   }
/*      */ 
/*      */   public void endElement(String namespaceURI, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*  440 */     processAccumulatedText();
/*      */ 
/*  443 */     appendEndElement();
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data) throws SAXException
/*      */   {
/*  458 */     processAccumulatedText();
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  468 */     processAccumulatedText();
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*  474 */     appendStartDocument();
/*      */   }
/*      */ 
/*      */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  480 */     processAccumulatedText();
/*      */ 
/*  483 */     String prefix = null;
/*  484 */     int colon = qName.indexOf(':');
/*  485 */     if (colon > 0) {
/*  486 */       prefix = qName.substring(0, colon);
/*      */     }
/*      */ 
/*  489 */     System.out.println("Prefix=" + prefix + " index=" + this.m_prefixNames.stringToIndex(prefix));
/*  490 */     appendStartElement(this.m_nsNames.stringToIndex(namespaceURI), this.m_localNames.stringToIndex(localName), this.m_prefixNames.stringToIndex(prefix));
/*      */ 
/*  497 */     int nAtts = atts == null ? 0 : atts.getLength();
/*      */ 
/*  499 */     for (int i = nAtts - 1; i >= 0; i--)
/*      */     {
/*  501 */       qName = atts.getQName(i);
/*  502 */       if ((qName.startsWith("xmlns:")) || ("xmlns".equals(qName)))
/*      */       {
/*  504 */         prefix = null;
/*  505 */         colon = qName.indexOf(':');
/*  506 */         if (colon > 0)
/*      */         {
/*  508 */           prefix = qName.substring(0, colon);
/*      */         }
/*      */         else
/*      */         {
/*  513 */           prefix = null;
/*      */         }
/*      */ 
/*  517 */         appendNSDeclaration(this.m_prefixNames.stringToIndex(prefix), this.m_nsNames.stringToIndex(atts.getValue(i)), atts.getType(i).equalsIgnoreCase("ID"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  524 */     for (int i = nAtts - 1; i >= 0; i--)
/*      */     {
/*  526 */       qName = atts.getQName(i);
/*  527 */       if ((!qName.startsWith("xmlns:")) && (!"xmlns".equals(qName)))
/*      */       {
/*  532 */         prefix = null;
/*  533 */         colon = qName.indexOf(':');
/*  534 */         if (colon > 0)
/*      */         {
/*  536 */           prefix = qName.substring(0, colon);
/*  537 */           localName = qName.substring(colon + 1);
/*      */         }
/*      */         else
/*      */         {
/*  541 */           prefix = "";
/*  542 */           localName = qName;
/*      */         }
/*      */ 
/*  546 */         this.m_char.append(atts.getValue(i));
/*  547 */         int contentEnd = this.m_char.length();
/*      */ 
/*  549 */         if ((!"xmlns".equals(prefix)) && (!"xmlns".equals(qName))) {
/*  550 */           appendAttribute(this.m_nsNames.stringToIndex(atts.getURI(i)), this.m_localNames.stringToIndex(localName), this.m_prefixNames.stringToIndex(prefix), atts.getType(i).equalsIgnoreCase("ID"), this.m_char_current_start, contentEnd - this.m_char_current_start);
/*      */         }
/*      */ 
/*  555 */         this.m_char_current_start = contentEnd;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*  572 */     processAccumulatedText();
/*      */ 
/*  574 */     this.m_char.append(ch, start, length);
/*  575 */     appendComment(this.m_char_current_start, length);
/*  576 */     this.m_char_current_start += length;
/*      */   }
/*      */ 
/*      */   public void endCDATA()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDTD()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endEntity(String name)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startCDATA()
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startEntity(String name)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   final void initDocument(int documentNumber)
/*      */   {
/*  627 */     this.m_docHandle = (documentNumber << 22);
/*      */ 
/*  630 */     this.nodes.writeSlot(0, 9, -1, -1, 0);
/*      */ 
/*  632 */     this.done = false;
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes(int nodeHandle)
/*      */   {
/* 1001 */     return getFirstChild(nodeHandle) != -1;
/*      */   }
/*      */ 
/*      */   public int getFirstChild(int nodeHandle)
/*      */   {
/* 1015 */     nodeHandle &= 8388607;
/*      */ 
/* 1017 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/*      */ 
/* 1020 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/*      */ 
/* 1023 */     if ((type == 1) || (type == 9) || (type == 5))
/*      */     {
/* 1033 */       int kid = nodeHandle + 1;
/* 1034 */       this.nodes.readSlot(kid, this.gotslot);
/* 1035 */       while (2 == (this.gotslot[0] & 0xFFFF))
/*      */       {
/* 1037 */         kid = this.gotslot[2];
/*      */ 
/* 1039 */         if (kid == -1) return -1;
/* 1040 */         this.nodes.readSlot(kid, this.gotslot);
/*      */       }
/*      */ 
/* 1043 */       if (this.gotslot[1] == nodeHandle)
/*      */       {
/* 1045 */         int firstChild = kid | this.m_docHandle;
/*      */ 
/* 1047 */         return firstChild;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1052 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeHandle)
/*      */   {
/* 1066 */     nodeHandle &= 8388607;
/*      */ 
/* 1068 */     int lastChild = -1;
/* 1069 */     for (int nextkid = getFirstChild(nodeHandle); nextkid != -1; 
/* 1070 */       nextkid = getNextSibling(nextkid)) {
/* 1071 */       lastChild = nextkid;
/*      */     }
/* 1073 */     return lastChild | this.m_docHandle;
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int nodeHandle, String namespaceURI, String name)
/*      */   {
/* 1089 */     int nsIndex = this.m_nsNames.stringToIndex(namespaceURI);
/* 1090 */     int nameIndex = this.m_localNames.stringToIndex(name);
/* 1091 */     nodeHandle &= 8388607;
/* 1092 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1093 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/*      */ 
/* 1095 */     if (type == 1) {
/* 1096 */       nodeHandle++;
/*      */     }
/* 1098 */     while (type == 2) {
/* 1099 */       if ((nsIndex == this.gotslot[0] << 16) && (this.gotslot[3] == nameIndex)) {
/* 1100 */         return nodeHandle | this.m_docHandle;
/*      */       }
/* 1102 */       nodeHandle = this.gotslot[2];
/* 1103 */       this.nodes.readSlot(nodeHandle, this.gotslot);
/*      */     }
/* 1105 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getFirstAttribute(int nodeHandle)
/*      */   {
/* 1115 */     nodeHandle &= 8388607;
/*      */ 
/* 1123 */     if (1 != (this.nodes.readEntry(nodeHandle, 0) & 0xFFFF)) {
/* 1124 */       return -1;
/*      */     }
/* 1126 */     nodeHandle++;
/* 1127 */     return 2 == (this.nodes.readEntry(nodeHandle, 0) & 0xFFFF) ? nodeHandle | this.m_docHandle : -1;
/*      */   }
/*      */ 
/*      */   public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
/*      */   {
/* 1146 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextSibling(int nodeHandle)
/*      */   {
/* 1167 */     nodeHandle &= 8388607;
/*      */ 
/* 1169 */     if (nodeHandle == 0) {
/* 1170 */       return -1;
/*      */     }
/* 1172 */     short type = (short)(this.nodes.readEntry(nodeHandle, 0) & 0xFFFF);
/* 1173 */     if ((type == 1) || (type == 2) || (type == 5))
/*      */     {
/* 1175 */       int nextSib = this.nodes.readEntry(nodeHandle, 2);
/* 1176 */       if (nextSib == -1)
/* 1177 */         return -1;
/* 1178 */       if (nextSib != 0) {
/* 1179 */         return this.m_docHandle | nextSib;
/*      */       }
/*      */     }
/*      */ 
/* 1183 */     int thisParent = this.nodes.readEntry(nodeHandle, 1);
/*      */ 
/* 1185 */     if (this.nodes.readEntry(++nodeHandle, 1) == thisParent) {
/* 1186 */       return this.m_docHandle | nodeHandle;
/*      */     }
/* 1188 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getPreviousSibling(int nodeHandle)
/*      */   {
/* 1201 */     nodeHandle &= 8388607;
/*      */ 
/* 1203 */     if (nodeHandle == 0) {
/* 1204 */       return -1;
/*      */     }
/* 1206 */     int parent = this.nodes.readEntry(nodeHandle, 1);
/* 1207 */     int kid = -1;
/* 1208 */     for (int nextkid = getFirstChild(parent); nextkid != nodeHandle; 
/* 1209 */       nextkid = getNextSibling(nextkid)) {
/* 1210 */       kid = nextkid;
/*      */     }
/* 1212 */     return kid | this.m_docHandle;
/*      */   }
/*      */ 
/*      */   public int getNextAttribute(int nodeHandle)
/*      */   {
/* 1225 */     nodeHandle &= 8388607;
/* 1226 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/*      */ 
/* 1232 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/*      */ 
/* 1234 */     if (type == 1)
/* 1235 */       return getFirstAttribute(nodeHandle);
/* 1236 */     if ((type == 2) && 
/* 1237 */       (this.gotslot[2] != -1)) {
/* 1238 */       return this.m_docHandle | this.gotslot[2];
/*      */     }
/* 1240 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextNamespaceNode(int baseHandle, int namespaceHandle, boolean inScope)
/*      */   {
/* 1255 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextDescendant(int subtreeRootHandle, int nodeHandle)
/*      */   {
/* 1269 */     subtreeRootHandle &= 8388607;
/* 1270 */     nodeHandle &= 8388607;
/*      */ 
/* 1272 */     if (nodeHandle == 0)
/* 1273 */       return -1;
/* 1274 */     while (!this.m_isError)
/*      */     {
/* 1276 */       if ((this.done) && (nodeHandle > this.nodes.slotsUsed()))
/*      */         break;
/* 1278 */       if (nodeHandle > subtreeRootHandle) {
/* 1279 */         this.nodes.readSlot(nodeHandle + 1, this.gotslot);
/* 1280 */         if (this.gotslot[2] != 0) {
/* 1281 */           short type = (short)(this.gotslot[0] & 0xFFFF);
/* 1282 */           if (type == 2) {
/* 1283 */             nodeHandle += 2;
/*      */           } else {
/* 1285 */             int nextParentPos = this.gotslot[1];
/* 1286 */             if (nextParentPos < subtreeRootHandle) break;
/* 1287 */             return this.m_docHandle | nodeHandle + 1;
/*      */           }
/*      */         }
/*      */         else {
/* 1291 */           if (this.done)
/*      */             break;
/*      */         }
/*      */       }
/*      */       else {
/* 1296 */         nodeHandle++;
/*      */       }
/*      */     }
/*      */ 
/* 1300 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextFollowing(int axisContextHandle, int nodeHandle)
/*      */   {
/* 1313 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextPreceding(int axisContextHandle, int nodeHandle)
/*      */   {
/* 1326 */     nodeHandle &= 8388607;
/* 1327 */     while (nodeHandle > 1) {
/* 1328 */       nodeHandle--;
/* 1329 */       if (2 != (this.nodes.readEntry(nodeHandle, 0) & 0xFFFF))
/*      */       {
/* 1340 */         return this.m_docHandle | this.nodes.specialFind(axisContextHandle, nodeHandle);
/*      */       }
/*      */     }
/* 1342 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getParent(int nodeHandle)
/*      */   {
/* 1356 */     return this.m_docHandle | this.nodes.readEntry(nodeHandle, 1);
/*      */   }
/*      */ 
/*      */   public int getDocumentRoot()
/*      */   {
/* 1364 */     return this.m_docHandle | this.m_docElement;
/*      */   }
/*      */ 
/*      */   public int getDocument()
/*      */   {
/* 1373 */     return this.m_docHandle;
/*      */   }
/*      */ 
/*      */   public int getOwnerDocument(int nodeHandle)
/*      */   {
/* 1391 */     if ((nodeHandle & 0x7FFFFF) == 0)
/* 1392 */       return -1;
/* 1393 */     return nodeHandle & 0xFF800000;
/*      */   }
/*      */ 
/*      */   public int getDocumentRoot(int nodeHandle)
/*      */   {
/* 1410 */     if ((nodeHandle & 0x7FFFFF) == 0)
/* 1411 */       return -1;
/* 1412 */     return nodeHandle & 0xFF800000;
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/* 1426 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1427 */     int nodetype = this.gotslot[0] & 0xFF;
/* 1428 */     String value = null;
/*      */ 
/* 1430 */     switch (nodetype) {
/*      */     case 3:
/*      */     case 4:
/*      */     case 8:
/* 1434 */       value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
/* 1435 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     }
/*      */ 
/* 1443 */     return this.m_xsf.newstr(value);
/*      */   }
/*      */ 
/*      */   public int getStringValueChunkCount(int nodeHandle)
/*      */   {
/* 1474 */     return 0;
/*      */   }
/*      */ 
/*      */   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
/*      */   {
/* 1503 */     return new char[0];
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(int nodeHandle)
/*      */   {
/* 1513 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1514 */     String qName = this.m_localNames.indexToString(this.gotslot[3]);
/*      */ 
/* 1517 */     int colonpos = qName.indexOf(":");
/* 1518 */     String localName = qName.substring(colonpos + 1);
/*      */ 
/* 1520 */     String namespace = this.m_nsNames.indexToString(this.gotslot[0] << 16);
/*      */ 
/* 1522 */     String expandedName = namespace + ":" + localName;
/* 1523 */     int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
/*      */ 
/* 1525 */     return expandedNameID;
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(String namespace, String localName, int type)
/*      */   {
/* 1543 */     String expandedName = namespace + ":" + localName;
/* 1544 */     int expandedNameID = this.m_nsNames.stringToIndex(expandedName);
/*      */ 
/* 1546 */     return expandedNameID;
/*      */   }
/*      */ 
/*      */   public String getLocalNameFromExpandedNameID(int ExpandedNameID)
/*      */   {
/* 1559 */     String expandedName = this.m_localNames.indexToString(ExpandedNameID);
/*      */ 
/* 1561 */     int colonpos = expandedName.indexOf(":");
/* 1562 */     String localName = expandedName.substring(colonpos + 1);
/* 1563 */     return localName;
/*      */   }
/*      */ 
/*      */   public String getNamespaceFromExpandedNameID(int ExpandedNameID)
/*      */   {
/* 1576 */     String expandedName = this.m_localNames.indexToString(ExpandedNameID);
/*      */ 
/* 1578 */     int colonpos = expandedName.indexOf(":");
/* 1579 */     String nsName = expandedName.substring(0, colonpos);
/*      */ 
/* 1581 */     return nsName;
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeHandle)
/*      */   {
/* 1607 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1608 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/* 1609 */     String name = fixednames[type];
/* 1610 */     if (null == name) {
/* 1611 */       int i = this.gotslot[3];
/* 1612 */       System.out.println("got i=" + i + " " + (i >> 16) + "/" + (i & 0xFFFF));
/*      */ 
/* 1614 */       name = this.m_localNames.indexToString(i & 0xFFFF);
/* 1615 */       String prefix = this.m_prefixNames.indexToString(i >> 16);
/* 1616 */       if ((prefix != null) && (prefix.length() > 0))
/* 1617 */         name = prefix + ":" + name;
/*      */     }
/* 1619 */     return name;
/*      */   }
/*      */ 
/*      */   public String getNodeNameX(int nodeHandle)
/*      */   {
/* 1630 */     return null;
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/* 1644 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1645 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/* 1646 */     String name = "";
/* 1647 */     if ((type == 1) || (type == 2)) {
/* 1648 */       int i = this.gotslot[3];
/* 1649 */       name = this.m_localNames.indexToString(i & 0xFFFF);
/* 1650 */       if (name == null) name = "";
/*      */     }
/* 1652 */     return name;
/*      */   }
/*      */ 
/*      */   public String getPrefix(int nodeHandle)
/*      */   {
/* 1670 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1671 */     short type = (short)(this.gotslot[0] & 0xFFFF);
/* 1672 */     String name = "";
/* 1673 */     if ((type == 1) || (type == 2)) {
/* 1674 */       int i = this.gotslot[3];
/* 1675 */       name = this.m_prefixNames.indexToString(i >> 16);
/* 1676 */       if (name == null) name = "";
/*      */     }
/* 1678 */     return name;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int nodeHandle)
/*      */   {
/* 1690 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/* 1703 */     this.nodes.readSlot(nodeHandle, this.gotslot);
/* 1704 */     int nodetype = this.gotslot[0] & 0xFF;
/* 1705 */     String value = null;
/*      */ 
/* 1707 */     switch (nodetype) {
/*      */     case 2:
/* 1709 */       this.nodes.readSlot(nodeHandle + 1, this.gotslot);
/*      */     case 3:
/*      */     case 4:
/*      */     case 8:
/* 1713 */       value = this.m_char.getString(this.gotslot[2], this.gotslot[3]);
/* 1714 */       break;
/*      */     case 1:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     }
/*      */ 
/* 1721 */     return value;
/*      */   }
/*      */ 
/*      */   public short getNodeType(int nodeHandle)
/*      */   {
/* 1733 */     return (short)(this.nodes.readEntry(nodeHandle, 0) & 0xFFFF);
/*      */   }
/*      */ 
/*      */   public short getLevel(int nodeHandle)
/*      */   {
/* 1745 */     short count = 0;
/* 1746 */     while (nodeHandle != 0) {
/* 1747 */       count = (short)(count + 1);
/* 1748 */       nodeHandle = this.nodes.readEntry(nodeHandle, 1);
/*      */     }
/* 1750 */     return count;
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/* 1767 */     return false;
/*      */   }
/*      */ 
/*      */   public String getDocumentBaseURI()
/*      */   {
/* 1779 */     return this.m_documentBaseURI;
/*      */   }
/*      */ 
/*      */   public void setDocumentBaseURI(String baseURI)
/*      */   {
/* 1790 */     this.m_documentBaseURI = baseURI;
/*      */   }
/*      */ 
/*      */   public String getDocumentSystemIdentifier(int nodeHandle)
/*      */   {
/* 1800 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentEncoding(int nodeHandle)
/*      */   {
/* 1809 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentStandalone(int nodeHandle)
/*      */   {
/* 1821 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentVersion(int documentHandle)
/*      */   {
/* 1833 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getDocumentAllDeclarationsProcessed()
/*      */   {
/* 1845 */     return false;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationSystemIdentifier()
/*      */   {
/* 1855 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationPublicIdentifier()
/*      */   {
/* 1865 */     return null;
/*      */   }
/*      */ 
/*      */   public int getElementById(String elementId)
/*      */   {
/* 1884 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/* 1920 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean supportsPreStripping()
/*      */   {
/* 1932 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isNodeAfter(int nodeHandle1, int nodeHandle2)
/*      */   {
/* 1952 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isCharacterElementContentWhitespace(int nodeHandle)
/*      */   {
/* 1970 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
/*      */   {
/* 1984 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int attributeHandle)
/*      */   {
/* 1995 */     return false;
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
/* 2037 */     return null;
/*      */   }
/*      */ 
/*      */   public void appendChild(int newChild, boolean clone, boolean cloneDepth)
/*      */   {
/* 2058 */     boolean sameDoc = (newChild & 0xFF800000) == this.m_docHandle;
/* 2059 */     if ((!clone) && (!sameDoc));
/*      */   }
/*      */ 
/*      */   public void appendTextChild(String str)
/*      */   {
/*      */   }
/*      */ 
/*      */   void appendTextChild(int m_char_current_start, int contentLength)
/*      */   {
/* 2097 */     int w0 = 3;
/*      */ 
/* 2099 */     int w1 = this.currentParent;
/*      */ 
/* 2101 */     int w2 = m_char_current_start;
/*      */ 
/* 2103 */     int w3 = contentLength;
/*      */ 
/* 2105 */     int ourslot = appendNode(w0, w1, w2, w3);
/* 2106 */     this.previousSibling = ourslot;
/*      */   }
/*      */ 
/*      */   void appendComment(int m_char_current_start, int contentLength)
/*      */   {
/* 2120 */     int w0 = 8;
/*      */ 
/* 2122 */     int w1 = this.currentParent;
/*      */ 
/* 2124 */     int w2 = m_char_current_start;
/*      */ 
/* 2126 */     int w3 = contentLength;
/*      */ 
/* 2128 */     int ourslot = appendNode(w0, w1, w2, w3);
/* 2129 */     this.previousSibling = ourslot;
/*      */   }
/*      */ 
/*      */   void appendStartElement(int namespaceIndex, int localNameIndex, int prefixIndex)
/*      */   {
/* 2156 */     int w0 = namespaceIndex << 16 | 0x1;
/*      */ 
/* 2158 */     int w1 = this.currentParent;
/*      */ 
/* 2160 */     int w2 = 0;
/*      */ 
/* 2162 */     int w3 = localNameIndex | prefixIndex << 16;
/* 2163 */     System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 0xFFFF));
/*      */ 
/* 2166 */     int ourslot = appendNode(w0, w1, w2, w3);
/* 2167 */     this.currentParent = ourslot;
/* 2168 */     this.previousSibling = 0;
/*      */ 
/* 2171 */     if (this.m_docElement == -1)
/* 2172 */       this.m_docElement = ourslot;
/*      */   }
/*      */ 
/*      */   void appendNSDeclaration(int prefixIndex, int namespaceIndex, boolean isID)
/*      */   {
/* 2199 */     int namespaceForNamespaces = this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/");
/*      */ 
/* 2202 */     int w0 = 0xD | this.m_nsNames.stringToIndex("http://www.w3.org/2000/xmlns/") << 16;
/*      */ 
/* 2205 */     int w1 = this.currentParent;
/*      */ 
/* 2207 */     int w2 = 0;
/*      */ 
/* 2209 */     int w3 = namespaceIndex;
/*      */ 
/* 2211 */     int ourslot = appendNode(w0, w1, w2, w3);
/* 2212 */     this.previousSibling = ourslot;
/* 2213 */     this.previousSiblingWasParent = false;
/*      */   }
/*      */ 
/*      */   void appendAttribute(int namespaceIndex, int localNameIndex, int prefixIndex, boolean isID, int m_char_current_start, int contentLength)
/*      */   {
/* 2239 */     int w0 = 0x2 | namespaceIndex << 16;
/*      */ 
/* 2242 */     int w1 = this.currentParent;
/*      */ 
/* 2244 */     int w2 = 0;
/*      */ 
/* 2246 */     int w3 = localNameIndex | prefixIndex << 16;
/* 2247 */     System.out.println("set w3=" + w3 + " " + (w3 >> 16) + "/" + (w3 & 0xFFFF));
/*      */ 
/* 2249 */     int ourslot = appendNode(w0, w1, w2, w3);
/* 2250 */     this.previousSibling = ourslot;
/*      */ 
/* 2255 */     w0 = 3;
/*      */ 
/* 2257 */     w1 = ourslot;
/*      */ 
/* 2259 */     w2 = m_char_current_start;
/*      */ 
/* 2261 */     w3 = contentLength;
/* 2262 */     appendNode(w0, w1, w2, w3);
/*      */ 
/* 2265 */     this.previousSiblingWasParent = true;
/*      */   }
/*      */ 
/*      */   public DTMAxisTraverser getAxisTraverser(int axis)
/*      */   {
/* 2279 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getAxisIterator(int axis)
/*      */   {
/* 2295 */     return null;
/*      */   }
/*      */ 
/*      */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*      */   {
/* 2311 */     return null;
/*      */   }
/*      */ 
/*      */   void appendEndElement()
/*      */   {
/* 2322 */     if (this.previousSiblingWasParent) {
/* 2323 */       this.nodes.writeEntry(this.previousSibling, 2, -1);
/*      */     }
/*      */ 
/* 2326 */     this.previousSibling = this.currentParent;
/* 2327 */     this.nodes.readSlot(this.currentParent, this.gotslot);
/* 2328 */     this.currentParent = (this.gotslot[1] & 0xFFFF);
/*      */ 
/* 2332 */     this.previousSiblingWasParent = true;
/*      */   }
/*      */ 
/*      */   void appendStartDocument()
/*      */   {
/* 2346 */     this.m_docElement = -1;
/* 2347 */     initDocument(0);
/*      */   }
/*      */ 
/*      */   void appendEndDocument()
/*      */   {
/* 2355 */     this.done = true;
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value)
/*      */   {
/*      */   }
/*      */ 
/*      */   public SourceLocator getSourceLocatorFor(int node)
/*      */   {
/* 2380 */     return null;
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
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMDocumentImpl
 * JD-Core Version:    0.6.2
 */