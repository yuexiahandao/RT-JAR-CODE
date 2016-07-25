/*      */ package com.sun.org.apache.xml.internal.dtm.ref.sax2dtm;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.InternalAxisIteratorBase;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.RootIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.SingletonIterator;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.ExtendedType;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*      */ import com.sun.org.apache.xml.internal.utils.IntStack;
/*      */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringDefault;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.Source;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public class SAX2DTM2 extends SAX2DTM
/*      */ {
/*      */   private int[] m_exptype_map0;
/*      */   private int[] m_nextsib_map0;
/*      */   private int[] m_firstch_map0;
/*      */   private int[] m_parent_map0;
/*      */   private int[][] m_exptype_map;
/*      */   private int[][] m_nextsib_map;
/*      */   private int[][] m_firstch_map;
/*      */   private int[][] m_parent_map;
/*      */   protected ExtendedType[] m_extendedTypes;
/*      */   protected Vector m_values;
/* 1807 */   private int m_valueIndex = 0;
/*      */   private int m_maxNodeIndex;
/*      */   protected int m_SHIFT;
/*      */   protected int m_MASK;
/*      */   protected int m_blocksize;
/*      */   protected static final int TEXT_LENGTH_BITS = 10;
/*      */   protected static final int TEXT_OFFSET_BITS = 21;
/*      */   protected static final int TEXT_LENGTH_MAX = 1023;
/*      */   protected static final int TEXT_OFFSET_MAX = 2097151;
/* 1836 */   protected boolean m_buildIdIndex = true;
/*      */   private static final String EMPTY_STR = "";
/* 1842 */   private static final XMLString EMPTY_XML_STR = new XMLStringDefault("");
/*      */ 
/*      */   public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*      */   {
/* 1853 */     this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, true, false);
/*      */   }
/*      */ 
/*      */   public SAX2DTM2(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean buildIdIndex, boolean newNameTable)
/*      */   {
/* 1870 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
/*      */ 
/* 1875 */     for (int shift = 0; blocksize >>>= 1 != 0; shift++);
/* 1877 */     this.m_blocksize = (1 << shift);
/* 1878 */     this.m_SHIFT = shift;
/* 1879 */     this.m_MASK = (this.m_blocksize - 1);
/*      */ 
/* 1881 */     this.m_buildIdIndex = buildIdIndex;
/*      */ 
/* 1886 */     this.m_values = new Vector(32, 512);
/*      */ 
/* 1888 */     this.m_maxNodeIndex = 65536;
/*      */ 
/* 1891 */     this.m_exptype_map0 = this.m_exptype.getMap0();
/* 1892 */     this.m_nextsib_map0 = this.m_nextsib.getMap0();
/* 1893 */     this.m_firstch_map0 = this.m_firstch.getMap0();
/* 1894 */     this.m_parent_map0 = this.m_parent.getMap0();
/*      */   }
/*      */ 
/*      */   public final int _exptype(int identity)
/*      */   {
/* 1905 */     return this.m_exptype.elementAt(identity);
/*      */   }
/*      */ 
/*      */   public final int _exptype2(int identity)
/*      */   {
/* 1928 */     if (identity < this.m_blocksize) {
/* 1929 */       return this.m_exptype_map0[identity];
/*      */     }
/* 1931 */     return this.m_exptype_map[(identity >>> this.m_SHIFT)][(identity & this.m_MASK)];
/*      */   }
/*      */ 
/*      */   public final int _nextsib2(int identity)
/*      */   {
/* 1944 */     if (identity < this.m_blocksize) {
/* 1945 */       return this.m_nextsib_map0[identity];
/*      */     }
/* 1947 */     return this.m_nextsib_map[(identity >>> this.m_SHIFT)][(identity & this.m_MASK)];
/*      */   }
/*      */ 
/*      */   public final int _firstch2(int identity)
/*      */   {
/* 1960 */     if (identity < this.m_blocksize) {
/* 1961 */       return this.m_firstch_map0[identity];
/*      */     }
/* 1963 */     return this.m_firstch_map[(identity >>> this.m_SHIFT)][(identity & this.m_MASK)];
/*      */   }
/*      */ 
/*      */   public final int _parent2(int identity)
/*      */   {
/* 1976 */     if (identity < this.m_blocksize) {
/* 1977 */       return this.m_parent_map0[identity];
/*      */     }
/* 1979 */     return this.m_parent_map[(identity >>> this.m_SHIFT)][(identity & this.m_MASK)];
/*      */   }
/*      */ 
/*      */   public final int _type2(int identity)
/*      */   {
/*      */     int eType;
/*      */     int eType;
/* 1992 */     if (identity < this.m_blocksize)
/* 1993 */       eType = this.m_exptype_map0[identity];
/*      */     else {
/* 1995 */       eType = this.m_exptype_map[(identity >>> this.m_SHIFT)][(identity & this.m_MASK)];
/*      */     }
/* 1997 */     if (-1 != eType) {
/* 1998 */       return this.m_extendedTypes[eType].getNodeType();
/*      */     }
/* 2000 */     return -1;
/*      */   }
/*      */ 
/*      */   public final int getExpandedTypeID2(int nodeHandle)
/*      */   {
/* 2011 */     int nodeID = makeNodeIdentity(nodeHandle);
/*      */ 
/* 2015 */     if (nodeID != -1) {
/* 2016 */       if (nodeID < this.m_blocksize) {
/* 2017 */         return this.m_exptype_map0[nodeID];
/*      */       }
/* 2019 */       return this.m_exptype_map[(nodeID >>> this.m_SHIFT)][(nodeID & this.m_MASK)];
/*      */     }
/*      */ 
/* 2022 */     return -1;
/*      */   }
/*      */ 
/*      */   public final int _exptype2Type(int exptype)
/*      */   {
/* 2035 */     if (-1 != exptype) {
/* 2036 */       return this.m_extendedTypes[exptype].getNodeType();
/*      */     }
/* 2038 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getIdForNamespace(String uri)
/*      */   {
/* 2051 */     int index = this.m_values.indexOf(uri);
/* 2052 */     if (index < 0)
/*      */     {
/* 2054 */       this.m_values.addElement(uri);
/* 2055 */       return this.m_valueIndex++;
/*      */     }
/*      */ 
/* 2058 */     return index;
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*      */     throws SAXException
/*      */   {
/* 2088 */     charactersFlush();
/*      */ 
/* 2090 */     int exName = this.m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
/*      */ 
/* 2092 */     int prefixIndex = qName.length() != localName.length() ? this.m_valuesOrPrefixes.stringToIndex(qName) : 0;
/*      */ 
/* 2095 */     int elemNode = addNode(1, exName, this.m_parents.peek(), this.m_previous, prefixIndex, true);
/*      */ 
/* 2098 */     if (this.m_indexing) {
/* 2099 */       indexNode(exName, elemNode);
/*      */     }
/* 2101 */     this.m_parents.push(elemNode);
/*      */ 
/* 2103 */     int startDecls = this.m_contextIndexes.peek();
/* 2104 */     int nDecls = this.m_prefixMappings.size();
/*      */ 
/* 2107 */     if (!this.m_pastFirstElement)
/*      */     {
/* 2110 */       String prefix = "xml";
/* 2111 */       String declURL = "http://www.w3.org/XML/1998/namespace";
/* 2112 */       exName = this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
/* 2113 */       this.m_values.addElement(declURL);
/* 2114 */       int val = this.m_valueIndex++;
/* 2115 */       addNode(13, exName, elemNode, -1, val, false);
/*      */ 
/* 2117 */       this.m_pastFirstElement = true;
/*      */     }
/*      */ 
/* 2120 */     for (int i = startDecls; i < nDecls; i += 2)
/*      */     {
/* 2122 */       String prefix = (String)this.m_prefixMappings.elementAt(i);
/*      */ 
/* 2124 */       if (prefix != null)
/*      */       {
/* 2127 */         String declURL = (String)this.m_prefixMappings.elementAt(i + 1);
/*      */ 
/* 2129 */         exName = this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
/*      */ 
/* 2131 */         this.m_values.addElement(declURL);
/* 2132 */         int val = this.m_valueIndex++;
/*      */ 
/* 2134 */         addNode(13, exName, elemNode, -1, val, false);
/*      */       }
/*      */     }
/* 2137 */     int n = attributes.getLength();
/*      */ 
/* 2139 */     for (int i = 0; i < n; i++)
/*      */     {
/* 2141 */       String attrUri = attributes.getURI(i);
/* 2142 */       String attrQName = attributes.getQName(i);
/* 2143 */       String valString = attributes.getValue(i);
/*      */ 
/* 2147 */       String attrLocalName = attributes.getLocalName(i);
/*      */       int nodeType;
/*      */       int nodeType;
/* 2149 */       if ((null != attrQName) && ((attrQName.equals("xmlns")) || (attrQName.startsWith("xmlns:"))))
/*      */       {
/* 2153 */         String prefix = getPrefix(attrQName, attrUri);
/* 2154 */         if (declAlreadyDeclared(prefix)) {
/*      */           continue;
/*      */         }
/* 2157 */         nodeType = 13;
/*      */       }
/*      */       else
/*      */       {
/* 2161 */         nodeType = 2;
/*      */ 
/* 2163 */         if ((this.m_buildIdIndex) && (attributes.getType(i).equalsIgnoreCase("ID"))) {
/* 2164 */           setIDAttribute(valString, elemNode);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2169 */       if (null == valString) {
/* 2170 */         valString = "";
/*      */       }
/* 2172 */       this.m_values.addElement(valString);
/* 2173 */       int val = this.m_valueIndex++;
/*      */ 
/* 2175 */       if (attrLocalName.length() != attrQName.length())
/*      */       {
/* 2178 */         prefixIndex = this.m_valuesOrPrefixes.stringToIndex(attrQName);
/*      */ 
/* 2180 */         int dataIndex = this.m_data.size();
/*      */ 
/* 2182 */         this.m_data.addElement(prefixIndex);
/* 2183 */         this.m_data.addElement(val);
/*      */ 
/* 2185 */         val = -dataIndex;
/*      */       }
/*      */ 
/* 2188 */       exName = this.m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType);
/* 2189 */       addNode(nodeType, exName, elemNode, -1, val, false);
/*      */     }
/*      */ 
/* 2193 */     if (null != this.m_wsfilter)
/*      */     {
/* 2195 */       short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
/* 2196 */       boolean shouldStrip = 2 == wsv ? true : 3 == wsv ? getShouldStripWhitespace() : false;
/*      */ 
/* 2200 */       pushShouldStripWhitespace(shouldStrip);
/*      */     }
/*      */ 
/* 2203 */     this.m_previous = -1;
/*      */ 
/* 2205 */     this.m_contextIndexes.push(this.m_prefixMappings.size());
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/* 2231 */     charactersFlush();
/*      */ 
/* 2235 */     this.m_contextIndexes.quickPop(1);
/*      */ 
/* 2238 */     int topContextIndex = this.m_contextIndexes.peek();
/* 2239 */     if (topContextIndex != this.m_prefixMappings.size()) {
/* 2240 */       this.m_prefixMappings.setSize(topContextIndex);
/*      */     }
/*      */ 
/* 2243 */     this.m_previous = this.m_parents.pop();
/*      */ 
/* 2245 */     popShouldStripWhitespace();
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2263 */     if (this.m_insideDTD) {
/* 2264 */       return;
/*      */     }
/* 2266 */     charactersFlush();
/*      */ 
/* 2270 */     this.m_values.addElement(new String(ch, start, length));
/* 2271 */     int dataIndex = this.m_valueIndex++;
/*      */ 
/* 2273 */     this.m_previous = addNode(8, 8, this.m_parents.peek(), this.m_previous, dataIndex, false);
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/* 2287 */     int doc = addNode(9, 9, -1, -1, 0, true);
/*      */ 
/* 2291 */     this.m_parents.push(doc);
/* 2292 */     this.m_previous = -1;
/*      */ 
/* 2294 */     this.m_contextIndexes.push(this.m_prefixMappings.size());
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/* 2306 */     super.endDocument();
/*      */ 
/* 2310 */     this.m_exptype.addElement(-1);
/* 2311 */     this.m_parent.addElement(-1);
/* 2312 */     this.m_nextsib.addElement(-1);
/* 2313 */     this.m_firstch.addElement(-1);
/*      */ 
/* 2316 */     this.m_extendedTypes = this.m_expandedNameTable.getExtendedTypes();
/* 2317 */     this.m_exptype_map = this.m_exptype.getMap();
/* 2318 */     this.m_nextsib_map = this.m_nextsib.getMap();
/* 2319 */     this.m_firstch_map = this.m_firstch.getMap();
/* 2320 */     this.m_parent_map = this.m_parent.getMap();
/*      */   }
/*      */ 
/*      */   protected final int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild)
/*      */   {
/* 2341 */     int nodeIndex = this.m_size++;
/*      */ 
/* 2345 */     if (nodeIndex == this.m_maxNodeIndex)
/*      */     {
/* 2347 */       addNewDTMID(nodeIndex);
/* 2348 */       this.m_maxNodeIndex += 65536;
/*      */     }
/*      */ 
/* 2351 */     this.m_firstch.addElement(-1);
/* 2352 */     this.m_nextsib.addElement(-1);
/* 2353 */     this.m_parent.addElement(parentIndex);
/* 2354 */     this.m_exptype.addElement(expandedTypeID);
/* 2355 */     this.m_dataOrQName.addElement(dataOrPrefix);
/*      */ 
/* 2357 */     if (this.m_prevsib != null) {
/* 2358 */       this.m_prevsib.addElement(previousSibling);
/*      */     }
/*      */ 
/* 2361 */     if ((this.m_locator != null) && (this.m_useSourceLocationProperty)) {
/* 2362 */       setSourceLocation();
/*      */     }
/*      */ 
/* 2369 */     switch (type)
/*      */     {
/*      */     case 13:
/* 2372 */       declareNamespaceInContext(parentIndex, nodeIndex);
/* 2373 */       break;
/*      */     case 2:
/* 2375 */       break;
/*      */     default:
/* 2377 */       if (-1 != previousSibling) {
/* 2378 */         this.m_nextsib.setElementAt(nodeIndex, previousSibling);
/*      */       }
/* 2380 */       else if (-1 != parentIndex) {
/* 2381 */         this.m_firstch.setElementAt(nodeIndex, parentIndex);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 2386 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   protected final void charactersFlush()
/*      */   {
/* 2396 */     if (this.m_textPendingStart >= 0)
/*      */     {
/* 2398 */       int length = this.m_chars.size() - this.m_textPendingStart;
/* 2399 */       boolean doStrip = false;
/*      */ 
/* 2401 */       if (getShouldStripWhitespace())
/*      */       {
/* 2403 */         doStrip = this.m_chars.isWhitespace(this.m_textPendingStart, length);
/*      */       }
/*      */ 
/* 2406 */       if (doStrip) {
/* 2407 */         this.m_chars.setLength(this.m_textPendingStart);
/*      */       }
/* 2411 */       else if (length > 0)
/*      */       {
/* 2415 */         if ((length <= 1023) && (this.m_textPendingStart <= 2097151))
/*      */         {
/* 2417 */           this.m_previous = addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, length + (this.m_textPendingStart << 10), false);
/*      */         }
/*      */         else
/*      */         {
/* 2425 */           int dataIndex = this.m_data.size();
/* 2426 */           this.m_previous = addNode(this.m_coalescedTextType, 3, this.m_parents.peek(), this.m_previous, -dataIndex, false);
/*      */ 
/* 2429 */           this.m_data.addElement(this.m_textPendingStart);
/* 2430 */           this.m_data.addElement(length);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2436 */       this.m_textPendingStart = -1;
/* 2437 */       this.m_textType = (this.m_coalescedTextType = 3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/* 2461 */     charactersFlush();
/*      */ 
/* 2463 */     int dataIndex = this.m_data.size();
/* 2464 */     this.m_previous = addNode(7, 7, this.m_parents.peek(), this.m_previous, -dataIndex, false);
/*      */ 
/* 2469 */     this.m_data.addElement(this.m_valuesOrPrefixes.stringToIndex(target));
/* 2470 */     this.m_values.addElement(data);
/* 2471 */     this.m_data.addElement(this.m_valueIndex++);
/*      */   }
/*      */ 
/*      */   public final int getFirstAttribute(int nodeHandle)
/*      */   {
/* 2485 */     int nodeID = makeNodeIdentity(nodeHandle);
/*      */ 
/* 2487 */     if (nodeID == -1) {
/* 2488 */       return -1;
/*      */     }
/* 2490 */     int type = _type2(nodeID);
/*      */ 
/* 2492 */     if (1 == type)
/*      */     {
/*      */       while (true)
/*      */       {
/* 2497 */         nodeID++;
/*      */ 
/* 2499 */         type = _type2(nodeID);
/*      */ 
/* 2501 */         if (type == 2)
/*      */         {
/* 2503 */           return makeNodeHandle(nodeID);
/*      */         }
/* 2505 */         if (13 != type)
/*      */         {
/* 2507 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2512 */     return -1;
/*      */   }
/*      */ 
/*      */   protected int getFirstAttributeIdentity(int identity)
/*      */   {
/* 2524 */     if (identity == -1) {
/* 2525 */       return -1;
/*      */     }
/* 2527 */     int type = _type2(identity);
/*      */ 
/* 2529 */     if (1 == type)
/*      */     {
/*      */       while (true)
/*      */       {
/* 2534 */         identity++;
/*      */ 
/* 2537 */         type = _type2(identity);
/*      */ 
/* 2539 */         if (type == 2)
/*      */         {
/* 2541 */           return identity;
/*      */         }
/* 2543 */         if (13 != type)
/*      */         {
/* 2545 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2550 */     return -1;
/*      */   }
/*      */ 
/*      */   protected int getNextAttributeIdentity(int identity)
/*      */   {
/*      */     while (true)
/*      */     {
/* 2568 */       identity++;
/* 2569 */       int type = _type2(identity);
/*      */ 
/* 2571 */       if (type == 2)
/* 2572 */         return identity;
/* 2573 */       if (type != 13)
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/* 2578 */     return -1;
/*      */   }
/*      */ 
/*      */   protected final int getTypedAttribute(int nodeHandle, int attType)
/*      */   {
/* 2595 */     int nodeID = makeNodeIdentity(nodeHandle);
/*      */ 
/* 2597 */     if (nodeID == -1) {
/* 2598 */       return -1;
/*      */     }
/* 2600 */     int type = _type2(nodeID);
/*      */ 
/* 2602 */     if (1 == type)
/*      */     {
/*      */       while (true)
/*      */       {
/* 2607 */         nodeID++;
/* 2608 */         int expType = _exptype2(nodeID);
/*      */ 
/* 2610 */         if (expType != -1)
/* 2611 */           type = this.m_extendedTypes[expType].getNodeType();
/*      */         else {
/* 2613 */           return -1;
/*      */         }
/* 2615 */         if (type == 2)
/*      */         {
/* 2617 */           if (expType == attType) return makeNodeHandle(nodeID);
/*      */         }
/* 2619 */         else if (13 != type)
/*      */         {
/* 2621 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2626 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/* 2641 */     int expType = _exptype(makeNodeIdentity(nodeHandle));
/*      */ 
/* 2643 */     if (expType == 7)
/*      */     {
/* 2645 */       int dataIndex = _dataOrQName(makeNodeIdentity(nodeHandle));
/* 2646 */       dataIndex = this.m_data.elementAt(-dataIndex);
/* 2647 */       return this.m_valuesOrPrefixes.indexToString(dataIndex);
/*      */     }
/*      */ 
/* 2650 */     return this.m_expandedNameTable.getLocalName(expType);
/*      */   }
/*      */ 
/*      */   public final String getNodeNameX(int nodeHandle)
/*      */   {
/* 2665 */     int nodeID = makeNodeIdentity(nodeHandle);
/* 2666 */     int eType = _exptype2(nodeID);
/*      */ 
/* 2668 */     if (eType == 7)
/*      */     {
/* 2670 */       int dataIndex = _dataOrQName(nodeID);
/* 2671 */       dataIndex = this.m_data.elementAt(-dataIndex);
/* 2672 */       return this.m_valuesOrPrefixes.indexToString(dataIndex);
/*      */     }
/*      */ 
/* 2675 */     ExtendedType extType = this.m_extendedTypes[eType];
/*      */ 
/* 2677 */     if (extType.getNamespace().length() == 0)
/*      */     {
/* 2679 */       return extType.getLocalName();
/*      */     }
/*      */ 
/* 2683 */     int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
/*      */ 
/* 2685 */     if (qnameIndex == 0) {
/* 2686 */       return extType.getLocalName();
/*      */     }
/* 2688 */     if (qnameIndex < 0)
/*      */     {
/* 2690 */       qnameIndex = -qnameIndex;
/* 2691 */       qnameIndex = this.m_data.elementAt(qnameIndex);
/*      */     }
/*      */ 
/* 2694 */     return this.m_valuesOrPrefixes.indexToString(qnameIndex);
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeHandle)
/*      */   {
/* 2712 */     int nodeID = makeNodeIdentity(nodeHandle);
/* 2713 */     int eType = _exptype2(nodeID);
/*      */ 
/* 2715 */     ExtendedType extType = this.m_extendedTypes[eType];
/* 2716 */     if (extType.getNamespace().length() == 0)
/*      */     {
/* 2718 */       int type = extType.getNodeType();
/*      */ 
/* 2720 */       String localName = extType.getLocalName();
/* 2721 */       if (type == 13)
/*      */       {
/* 2723 */         if (localName.length() == 0) {
/* 2724 */           return "xmlns";
/*      */         }
/* 2726 */         return "xmlns:" + localName;
/*      */       }
/* 2728 */       if (type == 7)
/*      */       {
/* 2730 */         int dataIndex = _dataOrQName(nodeID);
/* 2731 */         dataIndex = this.m_data.elementAt(-dataIndex);
/* 2732 */         return this.m_valuesOrPrefixes.indexToString(dataIndex);
/*      */       }
/* 2734 */       if (localName.length() == 0)
/*      */       {
/* 2736 */         return getFixedNames(type);
/*      */       }
/*      */ 
/* 2739 */       return localName;
/*      */     }
/*      */ 
/* 2743 */     int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
/*      */ 
/* 2745 */     if (qnameIndex == 0) {
/* 2746 */       return extType.getLocalName();
/*      */     }
/* 2748 */     if (qnameIndex < 0)
/*      */     {
/* 2750 */       qnameIndex = -qnameIndex;
/* 2751 */       qnameIndex = this.m_data.elementAt(qnameIndex);
/*      */     }
/*      */ 
/* 2754 */     return this.m_valuesOrPrefixes.indexToString(qnameIndex);
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/* 2777 */     int identity = makeNodeIdentity(nodeHandle);
/* 2778 */     if (identity == -1) {
/* 2779 */       return EMPTY_XML_STR;
/*      */     }
/* 2781 */     int type = _type2(identity);
/*      */ 
/* 2783 */     if ((type == 1) || (type == 9))
/*      */     {
/* 2785 */       int startNode = identity;
/* 2786 */       identity = _firstch2(identity);
/* 2787 */       if (-1 != identity)
/*      */       {
/* 2789 */         int offset = -1;
/* 2790 */         int length = 0;
/*      */         do
/*      */         {
/* 2794 */           type = _exptype2(identity);
/*      */ 
/* 2796 */           if ((type == 3) || (type == 4))
/*      */           {
/* 2798 */             int dataIndex = this.m_dataOrQName.elementAt(identity);
/* 2799 */             if (dataIndex >= 0)
/*      */             {
/* 2801 */               if (-1 == offset)
/*      */               {
/* 2803 */                 offset = dataIndex >>> 10;
/*      */               }
/*      */ 
/* 2806 */               length += (dataIndex & 0x3FF);
/*      */             }
/*      */             else
/*      */             {
/* 2810 */               if (-1 == offset)
/*      */               {
/* 2812 */                 offset = this.m_data.elementAt(-dataIndex);
/*      */               }
/*      */ 
/* 2815 */               length += this.m_data.elementAt(-dataIndex + 1);
/*      */             }
/*      */           }
/*      */ 
/* 2819 */           identity++;
/* 2820 */         }while (_parent2(identity) >= startNode);
/*      */ 
/* 2822 */         if (length > 0)
/*      */         {
/* 2824 */           if (this.m_xstrf != null) {
/* 2825 */             return this.m_xstrf.newstr(this.m_chars, offset, length);
/*      */           }
/* 2827 */           return new XMLStringDefault(this.m_chars.getString(offset, length));
/*      */         }
/*      */ 
/* 2830 */         return EMPTY_XML_STR;
/*      */       }
/*      */ 
/* 2833 */       return EMPTY_XML_STR;
/*      */     }
/* 2835 */     if ((3 == type) || (4 == type))
/*      */     {
/* 2837 */       int dataIndex = this.m_dataOrQName.elementAt(identity);
/* 2838 */       if (dataIndex >= 0)
/*      */       {
/* 2840 */         if (this.m_xstrf != null) {
/* 2841 */           return this.m_xstrf.newstr(this.m_chars, dataIndex >>> 10, dataIndex & 0x3FF);
/*      */         }
/*      */ 
/* 2844 */         return new XMLStringDefault(this.m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF));
/*      */       }
/*      */ 
/* 2849 */       if (this.m_xstrf != null) {
/* 2850 */         return this.m_xstrf.newstr(this.m_chars, this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */       }
/*      */ 
/* 2853 */       return new XMLStringDefault(this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1)));
/*      */     }
/*      */ 
/* 2859 */     int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 2861 */     if (dataIndex < 0)
/*      */     {
/* 2863 */       dataIndex = -dataIndex;
/* 2864 */       dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */     }
/*      */ 
/* 2867 */     if (this.m_xstrf != null) {
/* 2868 */       return this.m_xstrf.newstr((String)this.m_values.elementAt(dataIndex));
/*      */     }
/* 2870 */     return new XMLStringDefault((String)this.m_values.elementAt(dataIndex));
/*      */   }
/*      */ 
/*      */   public final String getStringValueX(int nodeHandle)
/*      */   {
/* 2890 */     int identity = makeNodeIdentity(nodeHandle);
/* 2891 */     if (identity == -1) {
/* 2892 */       return "";
/*      */     }
/* 2894 */     int type = _type2(identity);
/*      */ 
/* 2896 */     if ((type == 1) || (type == 9))
/*      */     {
/* 2898 */       int startNode = identity;
/* 2899 */       identity = _firstch2(identity);
/* 2900 */       if (-1 != identity)
/*      */       {
/* 2902 */         int offset = -1;
/* 2903 */         int length = 0;
/*      */         do
/*      */         {
/* 2907 */           type = _exptype2(identity);
/*      */ 
/* 2909 */           if ((type == 3) || (type == 4))
/*      */           {
/* 2911 */             int dataIndex = this.m_dataOrQName.elementAt(identity);
/* 2912 */             if (dataIndex >= 0)
/*      */             {
/* 2914 */               if (-1 == offset)
/*      */               {
/* 2916 */                 offset = dataIndex >>> 10;
/*      */               }
/*      */ 
/* 2919 */               length += (dataIndex & 0x3FF);
/*      */             }
/*      */             else
/*      */             {
/* 2923 */               if (-1 == offset)
/*      */               {
/* 2925 */                 offset = this.m_data.elementAt(-dataIndex);
/*      */               }
/*      */ 
/* 2928 */               length += this.m_data.elementAt(-dataIndex + 1);
/*      */             }
/*      */           }
/*      */ 
/* 2932 */           identity++;
/* 2933 */         }while (_parent2(identity) >= startNode);
/*      */ 
/* 2935 */         if (length > 0)
/*      */         {
/* 2937 */           return this.m_chars.getString(offset, length);
/*      */         }
/*      */ 
/* 2940 */         return "";
/*      */       }
/*      */ 
/* 2943 */       return "";
/*      */     }
/* 2945 */     if ((3 == type) || (4 == type))
/*      */     {
/* 2947 */       int dataIndex = this.m_dataOrQName.elementAt(identity);
/* 2948 */       if (dataIndex >= 0)
/*      */       {
/* 2950 */         return this.m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
/*      */       }
/*      */ 
/* 2955 */       return this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */     }
/*      */ 
/* 2961 */     int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 2963 */     if (dataIndex < 0)
/*      */     {
/* 2965 */       dataIndex = -dataIndex;
/* 2966 */       dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */     }
/*      */ 
/* 2969 */     return (String)this.m_values.elementAt(dataIndex);
/*      */   }
/*      */ 
/*      */   public String getStringValue()
/*      */   {
/* 2978 */     int child = _firstch2(0);
/* 2979 */     if (child == -1) return "";
/*      */ 
/* 2982 */     if ((_exptype2(child) == 3) && (_nextsib2(child) == -1))
/*      */     {
/* 2984 */       int dataIndex = this.m_dataOrQName.elementAt(child);
/* 2985 */       if (dataIndex >= 0) {
/* 2986 */         return this.m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
/*      */       }
/* 2988 */       return this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */     }
/*      */ 
/* 2992 */     return getStringValueX(getDocument());
/*      */   }
/*      */ 
/*      */   public final void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
/*      */     throws SAXException
/*      */   {
/* 3020 */     int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/* 3022 */     if (identity == -1) {
/* 3023 */       return;
/*      */     }
/* 3025 */     int type = _type2(identity);
/*      */ 
/* 3027 */     if ((type == 1) || (type == 9))
/*      */     {
/* 3029 */       int startNode = identity;
/* 3030 */       identity = _firstch2(identity);
/* 3031 */       if (-1 != identity)
/*      */       {
/* 3033 */         int offset = -1;
/* 3034 */         int length = 0;
/*      */         do
/*      */         {
/* 3038 */           type = _exptype2(identity);
/*      */ 
/* 3040 */           if ((type == 3) || (type == 4))
/*      */           {
/* 3042 */             int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 3044 */             if (dataIndex >= 0)
/*      */             {
/* 3046 */               if (-1 == offset)
/*      */               {
/* 3048 */                 offset = dataIndex >>> 10;
/*      */               }
/*      */ 
/* 3051 */               length += (dataIndex & 0x3FF);
/*      */             }
/*      */             else
/*      */             {
/* 3055 */               if (-1 == offset)
/*      */               {
/* 3057 */                 offset = this.m_data.elementAt(-dataIndex);
/*      */               }
/*      */ 
/* 3060 */               length += this.m_data.elementAt(-dataIndex + 1);
/*      */             }
/*      */           }
/*      */ 
/* 3064 */           identity++;
/* 3065 */         }while (_parent2(identity) >= startNode);
/*      */ 
/* 3067 */         if (length > 0)
/*      */         {
/* 3069 */           if (normalize)
/* 3070 */             this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
/*      */           else
/* 3072 */             this.m_chars.sendSAXcharacters(ch, offset, length);
/*      */         }
/*      */       }
/*      */     }
/* 3076 */     else if ((3 == type) || (4 == type))
/*      */     {
/* 3078 */       int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 3080 */       if (dataIndex >= 0)
/*      */       {
/* 3082 */         if (normalize) {
/* 3083 */           this.m_chars.sendNormalizedSAXcharacters(ch, dataIndex >>> 10, dataIndex & 0x3FF);
/*      */         }
/*      */         else {
/* 3086 */           this.m_chars.sendSAXcharacters(ch, dataIndex >>> 10, dataIndex & 0x3FF);
/*      */         }
/*      */ 
/*      */       }
/* 3091 */       else if (normalize) {
/* 3092 */         this.m_chars.sendNormalizedSAXcharacters(ch, this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */       }
/*      */       else {
/* 3095 */         this.m_chars.sendSAXcharacters(ch, this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 3101 */       int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 3103 */       if (dataIndex < 0)
/*      */       {
/* 3105 */         dataIndex = -dataIndex;
/* 3106 */         dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */       }
/*      */ 
/* 3109 */       String str = (String)this.m_values.elementAt(dataIndex);
/*      */ 
/* 3111 */       if (normalize) {
/* 3112 */         FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
/*      */       }
/*      */       else
/* 3115 */         ch.characters(str.toCharArray(), 0, str.length());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/* 3131 */     int identity = makeNodeIdentity(nodeHandle);
/* 3132 */     int type = _type2(identity);
/*      */ 
/* 3134 */     if ((type == 3) || (type == 4))
/*      */     {
/* 3136 */       int dataIndex = _dataOrQName(identity);
/* 3137 */       if (dataIndex > 0)
/*      */       {
/* 3139 */         return this.m_chars.getString(dataIndex >>> 10, dataIndex & 0x3FF);
/*      */       }
/*      */ 
/* 3144 */       return this.m_chars.getString(this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */     }
/*      */ 
/* 3148 */     if (1 == type)
/*      */     {
/* 3150 */       return getStringValueX(nodeHandle);
/*      */     }
/* 3152 */     if ((11 == type) || (9 == type))
/*      */     {
/* 3155 */       return null;
/*      */     }
/*      */ 
/* 3159 */     int dataIndex = this.m_dataOrQName.elementAt(identity);
/*      */ 
/* 3161 */     if (dataIndex < 0)
/*      */     {
/* 3163 */       dataIndex = -dataIndex;
/* 3164 */       dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */     }
/*      */ 
/* 3167 */     return (String)this.m_values.elementAt(dataIndex);
/*      */   }
/*      */ 
/*      */   protected final void copyTextNode(int nodeID, SerializationHandler handler)
/*      */     throws SAXException
/*      */   {
/* 3177 */     if (nodeID != -1) {
/* 3178 */       int dataIndex = this.m_dataOrQName.elementAt(nodeID);
/* 3179 */       if (dataIndex >= 0) {
/* 3180 */         this.m_chars.sendSAXcharacters(handler, dataIndex >>> 10, dataIndex & 0x3FF);
/*      */       }
/*      */       else
/*      */       {
/* 3184 */         this.m_chars.sendSAXcharacters(handler, this.m_data.elementAt(-dataIndex), this.m_data.elementAt(-dataIndex + 1));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final String copyElement(int nodeID, int exptype, SerializationHandler handler)
/*      */     throws SAXException
/*      */   {
/* 3202 */     ExtendedType extType = this.m_extendedTypes[exptype];
/* 3203 */     String uri = extType.getNamespace();
/* 3204 */     String name = extType.getLocalName();
/*      */ 
/* 3206 */     if (uri.length() == 0) {
/* 3207 */       handler.startElement(name);
/* 3208 */       return name;
/*      */     }
/*      */ 
/* 3211 */     int qnameIndex = this.m_dataOrQName.elementAt(nodeID);
/*      */ 
/* 3213 */     if (qnameIndex == 0) {
/* 3214 */       handler.startElement(name);
/* 3215 */       handler.namespaceAfterStartElement("", uri);
/* 3216 */       return name;
/*      */     }
/*      */ 
/* 3219 */     if (qnameIndex < 0) {
/* 3220 */       qnameIndex = -qnameIndex;
/* 3221 */       qnameIndex = this.m_data.elementAt(qnameIndex);
/*      */     }
/*      */ 
/* 3224 */     String qName = this.m_valuesOrPrefixes.indexToString(qnameIndex);
/* 3225 */     handler.startElement(qName);
/* 3226 */     int prefixIndex = qName.indexOf(':');
/*      */     String prefix;
/*      */     String prefix;
/* 3228 */     if (prefixIndex > 0) {
/* 3229 */       prefix = qName.substring(0, prefixIndex);
/*      */     }
/*      */     else {
/* 3232 */       prefix = null;
/*      */     }
/* 3234 */     handler.namespaceAfterStartElement(prefix, uri);
/* 3235 */     return qName;
/*      */   }
/*      */ 
/*      */   protected final void copyNS(int nodeID, SerializationHandler handler, boolean inScope)
/*      */     throws SAXException
/*      */   {
/* 3257 */     if ((this.m_namespaceDeclSetElements != null) && (this.m_namespaceDeclSetElements.size() == 1) && (this.m_namespaceDeclSets != null) && (((SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(0)).size() == 1))
/*      */     {
/* 3262 */       return;
/*      */     }
/* 3264 */     SuballocatedIntVector nsContext = null;
/*      */     int nextNSNode;
/*      */     int nextNSNode;
/* 3268 */     if (inScope) {
/* 3269 */       nsContext = findNamespaceContext(nodeID);
/* 3270 */       if ((nsContext == null) || (nsContext.size() < 1)) {
/* 3271 */         return;
/*      */       }
/* 3273 */       nextNSNode = makeNodeIdentity(nsContext.elementAt(0));
/*      */     }
/*      */     else {
/* 3276 */       nextNSNode = getNextNamespaceNode2(nodeID);
/*      */     }
/* 3278 */     int nsIndex = 1;
/* 3279 */     while (nextNSNode != -1)
/*      */     {
/* 3281 */       int eType = _exptype2(nextNSNode);
/* 3282 */       String nodeName = this.m_extendedTypes[eType].getLocalName();
/*      */ 
/* 3285 */       int dataIndex = this.m_dataOrQName.elementAt(nextNSNode);
/*      */ 
/* 3287 */       if (dataIndex < 0) {
/* 3288 */         dataIndex = -dataIndex;
/* 3289 */         dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */       }
/*      */ 
/* 3292 */       String nodeValue = (String)this.m_values.elementAt(dataIndex);
/*      */ 
/* 3294 */       handler.namespaceAfterStartElement(nodeName, nodeValue);
/*      */ 
/* 3296 */       if (inScope) {
/* 3297 */         if (nsIndex < nsContext.size()) {
/* 3298 */           nextNSNode = makeNodeIdentity(nsContext.elementAt(nsIndex));
/* 3299 */           nsIndex++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3305 */         nextNSNode = getNextNamespaceNode2(nextNSNode);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final int getNextNamespaceNode2(int baseID)
/*      */   {
/*      */     int type;
/* 3318 */     while ((type = _type2(++baseID)) == 2);
/* 3320 */     if (type == 13) {
/* 3321 */       return baseID;
/*      */     }
/* 3323 */     return -1;
/*      */   }
/*      */ 
/*      */   protected final void copyAttributes(int nodeID, SerializationHandler handler)
/*      */     throws SAXException
/*      */   {
/* 3335 */     for (int current = getFirstAttributeIdentity(nodeID); current != -1; current = getNextAttributeIdentity(current)) {
/* 3336 */       int eType = _exptype2(current);
/* 3337 */       copyAttribute(current, eType, handler);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void copyAttribute(int nodeID, int exptype, SerializationHandler handler)
/*      */     throws SAXException
/*      */   {
/* 3362 */     ExtendedType extType = this.m_extendedTypes[exptype];
/* 3363 */     String uri = extType.getNamespace();
/* 3364 */     String localName = extType.getLocalName();
/*      */ 
/* 3366 */     String prefix = null;
/* 3367 */     String qname = null;
/* 3368 */     int dataIndex = _dataOrQName(nodeID);
/* 3369 */     int valueIndex = dataIndex;
/* 3370 */     if (dataIndex <= 0) {
/* 3371 */       int prefixIndex = this.m_data.elementAt(-dataIndex);
/* 3372 */       valueIndex = this.m_data.elementAt(-dataIndex + 1);
/* 3373 */       qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
/* 3374 */       int colonIndex = qname.indexOf(':');
/* 3375 */       if (colonIndex > 0) {
/* 3376 */         prefix = qname.substring(0, colonIndex);
/*      */       }
/*      */     }
/* 3379 */     if (uri.length() != 0) {
/* 3380 */       handler.namespaceAfterStartElement(prefix, uri);
/*      */     }
/*      */ 
/* 3383 */     String nodeName = prefix != null ? qname : localName;
/* 3384 */     String nodeValue = (String)this.m_values.elementAt(valueIndex);
/*      */ 
/* 3386 */     handler.addAttribute(uri, localName, nodeName, "CDATA", nodeValue);
/*      */   }
/*      */ 
/*      */   public class AncestorIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private static final int m_blocksize = 32;
/* 1224 */     int[] m_ancestors = new int[32];
/*      */ 
/* 1227 */     int m_size = 0;
/*      */     int m_ancestorsPos;
/*      */     int m_markedPos;
/*      */     int m_realStartNode;
/*      */ 
/*      */     public AncestorIterator()
/*      */     {
/* 1218 */       super();
/*      */     }
/*      */ 
/*      */     public int getStartNode()
/*      */     {
/* 1244 */       return this.m_realStartNode;
/*      */     }
/*      */ 
/*      */     public final boolean isReverse()
/*      */     {
/* 1254 */       return true;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator cloneIterator()
/*      */     {
/* 1264 */       this._isRestartable = false;
/*      */       try
/*      */       {
/* 1268 */         AncestorIterator clone = (AncestorIterator)super.clone();
/*      */ 
/* 1270 */         clone._startNode = this._startNode;
/*      */ 
/* 1273 */         return clone;
/*      */       }
/*      */       catch (CloneNotSupportedException e) {
/*      */       }
/* 1277 */       throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1292 */       if (node == 0)
/* 1293 */         node = SAX2DTM2.this.getDocument();
/* 1294 */       this.m_realStartNode = node;
/*      */ 
/* 1296 */       if (this._isRestartable)
/*      */       {
/* 1298 */         int nodeID = SAX2DTM2.this.makeNodeIdentity(node);
/* 1299 */         this.m_size = 0;
/*      */ 
/* 1301 */         if (nodeID == -1) {
/* 1302 */           this._currentNode = -1;
/* 1303 */           this.m_ancestorsPos = 0;
/* 1304 */           return this;
/*      */         }
/*      */ 
/* 1309 */         if (!this._includeSelf) {
/* 1310 */           nodeID = SAX2DTM2.this._parent2(nodeID);
/* 1311 */           node = SAX2DTM2.this.makeNodeHandle(nodeID);
/*      */         }
/*      */ 
/* 1314 */         this._startNode = node;
/*      */ 
/* 1316 */         while (nodeID != -1)
/*      */         {
/* 1318 */           if (this.m_size >= this.m_ancestors.length)
/*      */           {
/* 1320 */             int[] newAncestors = new int[this.m_size * 2];
/* 1321 */             System.arraycopy(this.m_ancestors, 0, newAncestors, 0, this.m_ancestors.length);
/* 1322 */             this.m_ancestors = newAncestors;
/*      */           }
/*      */ 
/* 1325 */           this.m_ancestors[(this.m_size++)] = node;
/* 1326 */           nodeID = SAX2DTM2.this._parent2(nodeID);
/* 1327 */           node = SAX2DTM2.this.makeNodeHandle(nodeID);
/*      */         }
/*      */ 
/* 1330 */         this.m_ancestorsPos = (this.m_size - 1);
/*      */ 
/* 1332 */         this._currentNode = (this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1);
/*      */ 
/* 1336 */         return resetPosition();
/*      */       }
/*      */ 
/* 1339 */       return this;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator reset()
/*      */     {
/* 1351 */       this.m_ancestorsPos = (this.m_size - 1);
/*      */ 
/* 1353 */       this._currentNode = (this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1);
/*      */ 
/* 1356 */       return resetPosition();
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1367 */       int next = this._currentNode;
/*      */ 
/* 1369 */       int pos = --this.m_ancestorsPos;
/*      */ 
/* 1371 */       this._currentNode = (pos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1);
/*      */ 
/* 1374 */       return returnNode(next);
/*      */     }
/*      */ 
/*      */     public void setMark() {
/* 1378 */       this.m_markedPos = this.m_ancestorsPos;
/*      */     }
/*      */ 
/*      */     public void gotoMark() {
/* 1382 */       this.m_ancestorsPos = this.m_markedPos;
/* 1383 */       this._currentNode = (this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class AttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     public AttributeIterator()
/*      */     {
/*  501 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  517 */       if (node == 0)
/*  518 */         node = SAX2DTM2.this.getDocument();
/*  519 */       if (this._isRestartable)
/*      */       {
/*  521 */         this._startNode = node;
/*  522 */         this._currentNode = SAX2DTM2.this.getFirstAttributeIdentity(SAX2DTM2.this.makeNodeIdentity(node));
/*      */ 
/*  524 */         return resetPosition();
/*      */       }
/*      */ 
/*  527 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  538 */       int node = this._currentNode;
/*      */ 
/*  540 */       if (node != -1) {
/*  541 */         this._currentNode = SAX2DTM2.this.getNextAttributeIdentity(node);
/*  542 */         return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */       }
/*      */ 
/*  545 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class ChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     public ChildrenIterator()
/*      */     {
/*   73 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*   91 */       if (node == 0)
/*   92 */         node = SAX2DTM2.this.getDocument();
/*   93 */       if (this._isRestartable)
/*      */       {
/*   95 */         this._startNode = node;
/*   96 */         this._currentNode = (node == -1 ? -1 : SAX2DTM2.this._firstch2(SAX2DTM2.this.makeNodeIdentity(node)));
/*      */ 
/*   99 */         return resetPosition();
/*      */       }
/*      */ 
/*  102 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  113 */       if (this._currentNode != -1) {
/*  114 */         int node = this._currentNode;
/*  115 */         this._currentNode = SAX2DTM2.this._nextsib2(node);
/*  116 */         return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */       }
/*      */ 
/*  119 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class DescendantIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     public DescendantIterator()
/*      */     {
/* 1513 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1527 */       if (node == 0)
/* 1528 */         node = SAX2DTM2.this.getDocument();
/* 1529 */       if (this._isRestartable)
/*      */       {
/* 1531 */         node = SAX2DTM2.this.makeNodeIdentity(node);
/* 1532 */         this._startNode = node;
/*      */ 
/* 1534 */         if (this._includeSelf) {
/* 1535 */           node--;
/*      */         }
/* 1537 */         this._currentNode = node;
/*      */ 
/* 1539 */         return resetPosition();
/*      */       }
/*      */ 
/* 1542 */       return this;
/*      */     }
/*      */ 
/*      */     protected final boolean isDescendant(int identity)
/*      */     {
/* 1561 */       return (SAX2DTM2.this._parent2(identity) >= this._startNode) || (this._startNode == identity);
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1571 */       int startNode = this._startNode;
/* 1572 */       if (startNode == -1) {
/* 1573 */         return -1;
/*      */       }
/*      */ 
/* 1576 */       if ((this._includeSelf) && (this._currentNode + 1 == startNode)) {
/* 1577 */         return returnNode(SAX2DTM2.this.makeNodeHandle(++this._currentNode));
/*      */       }
/* 1579 */       int node = this._currentNode;
/*      */ 
/* 1584 */       if (startNode == 0) { int eType;
/*      */         int type;
/*      */         do { node++;
/* 1588 */           eType = SAX2DTM2.this._exptype2(node);
/*      */ 
/* 1590 */           if (-1 == eType) {
/* 1591 */             this._currentNode = -1;
/* 1592 */             return -1;
/*      */           }
/*      */         }
/*      */ 
/* 1596 */         while ((eType == 3) || ((type = SAX2DTM2.this.m_extendedTypes[eType].getNodeType()) == 2) || (type == 13));
/*      */       } else {
/*      */         int type;
/*      */         do {
/* 1600 */           node++;
/* 1601 */           type = SAX2DTM2.this._type2(node);
/*      */ 
/* 1603 */           if ((-1 == type) || (!isDescendant(node))) {
/* 1604 */             this._currentNode = -1;
/* 1605 */             return -1;
/*      */           }
/*      */         }
/* 1608 */         while ((2 == type) || (3 == type) || (13 == type));
/*      */       }
/*      */ 
/* 1611 */       this._currentNode = node;
/* 1612 */       return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator reset()
/*      */     {
/* 1622 */       boolean temp = this._isRestartable;
/*      */ 
/* 1624 */       this._isRestartable = true;
/*      */ 
/* 1626 */       setStartNode(SAX2DTM2.this.makeNodeHandle(this._startNode));
/*      */ 
/* 1628 */       this._isRestartable = temp;
/*      */ 
/* 1630 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FollowingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     public FollowingIterator()
/*      */     {
/* 1053 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1068 */       if (node == 0)
/* 1069 */         node = SAX2DTM2.this.getDocument();
/* 1070 */       if (this._isRestartable)
/*      */       {
/* 1072 */         this._startNode = node;
/*      */ 
/* 1076 */         node = SAX2DTM2.this.makeNodeIdentity(node);
/*      */ 
/* 1079 */         int type = SAX2DTM2.this._type2(node);
/*      */ 
/* 1081 */         if ((2 == type) || (13 == type))
/*      */         {
/* 1083 */           node = SAX2DTM2.this._parent2(node);
/* 1084 */           int first = SAX2DTM2.this._firstch2(node);
/*      */ 
/* 1086 */           if (-1 != first) {
/* 1087 */             this._currentNode = SAX2DTM2.this.makeNodeHandle(first);
/* 1088 */             return resetPosition();
/*      */           }
/*      */         }
/*      */         int first;
/*      */         do
/*      */         {
/* 1094 */           first = SAX2DTM2.this._nextsib2(node);
/*      */ 
/* 1096 */           if (-1 == first)
/* 1097 */             node = SAX2DTM2.this._parent2(node);
/*      */         }
/* 1099 */         while ((-1 == first) && (-1 != node));
/*      */ 
/* 1101 */         this._currentNode = SAX2DTM2.this.makeNodeHandle(first);
/*      */ 
/* 1104 */         return resetPosition();
/*      */       }
/*      */ 
/* 1107 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1118 */       int node = this._currentNode;
/*      */ 
/* 1121 */       int current = SAX2DTM2.this.makeNodeIdentity(node);
/*      */       int type;
/*      */       do
/*      */       {
/* 1125 */         current++;
/*      */ 
/* 1127 */         type = SAX2DTM2.this._type2(current);
/* 1128 */         if (-1 == type) {
/* 1129 */           this._currentNode = -1;
/* 1130 */           return returnNode(node);
/*      */         }
/*      */       }
/* 1133 */       while ((2 == type) || (13 == type));
/*      */ 
/* 1136 */       this._currentNode = SAX2DTM2.this.makeNodeHandle(current);
/* 1137 */       return returnNode(node);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class FollowingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     public FollowingSiblingIterator()
/*      */     {
/*  407 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  421 */       if (node == 0)
/*  422 */         node = SAX2DTM2.this.getDocument();
/*  423 */       if (this._isRestartable)
/*      */       {
/*  425 */         this._startNode = node;
/*  426 */         this._currentNode = SAX2DTM2.this.makeNodeIdentity(node);
/*      */ 
/*  428 */         return resetPosition();
/*      */       }
/*      */ 
/*  431 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  441 */       this._currentNode = (this._currentNode == -1 ? -1 : SAX2DTM2.this._nextsib2(this._currentNode));
/*      */ 
/*  443 */       return returnNode(SAX2DTM2.this.makeNodeHandle(this._currentNode));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class ParentIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*  132 */     private int _nodeType = -1;
/*      */ 
/*      */     public ParentIterator()
/*      */     {
/*  128 */       super();
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  145 */       if (node == 0)
/*  146 */         node = SAX2DTM2.this.getDocument();
/*  147 */       if (this._isRestartable)
/*      */       {
/*  149 */         this._startNode = node;
/*      */ 
/*  151 */         if (node != -1)
/*  152 */           this._currentNode = SAX2DTM2.this._parent2(SAX2DTM2.this.makeNodeIdentity(node));
/*      */         else {
/*  154 */           this._currentNode = -1;
/*      */         }
/*  156 */         return resetPosition();
/*      */       }
/*      */ 
/*  159 */       return this;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setNodeType(int type)
/*      */     {
/*  175 */       this._nodeType = type;
/*      */ 
/*  177 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  188 */       int result = this._currentNode;
/*  189 */       if (result == -1) {
/*  190 */         return -1;
/*      */       }
/*      */ 
/*  193 */       if (this._nodeType == -1) {
/*  194 */         this._currentNode = -1;
/*  195 */         return returnNode(SAX2DTM2.this.makeNodeHandle(result));
/*      */       }
/*  197 */       if (this._nodeType >= 14) {
/*  198 */         if (this._nodeType == SAX2DTM2.this._exptype2(result)) {
/*  199 */           this._currentNode = -1;
/*  200 */           return returnNode(SAX2DTM2.this.makeNodeHandle(result));
/*      */         }
/*      */ 
/*      */       }
/*  204 */       else if (this._nodeType == SAX2DTM2.this._type2(result)) {
/*  205 */         this._currentNode = -1;
/*  206 */         return returnNode(SAX2DTM2.this.makeNodeHandle(result));
/*      */       }
/*      */ 
/*  210 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class PrecedingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*  802 */     private final int _maxAncestors = 8;
/*      */ 
/*  808 */     protected int[] _stack = new int[8];
/*      */     protected int _sp;
/*      */     protected int _oldsp;
/*      */     protected int _markedsp;
/*      */     protected int _markedNode;
/*      */     protected int _markedDescendant;
/*      */ 
/*      */     public PrecedingIterator()
/*      */     {
/*  798 */       super();
/*      */     }
/*      */ 
/*      */     public boolean isReverse()
/*      */     {
/*  824 */       return true;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator cloneIterator()
/*      */     {
/*  834 */       this._isRestartable = false;
/*      */       try
/*      */       {
/*  838 */         PrecedingIterator clone = (PrecedingIterator)super.clone();
/*  839 */         int[] stackCopy = new int[this._stack.length];
/*  840 */         System.arraycopy(this._stack, 0, stackCopy, 0, this._stack.length);
/*      */ 
/*  842 */         clone._stack = stackCopy;
/*      */ 
/*  845 */         return clone;
/*      */       }
/*      */       catch (CloneNotSupportedException e) {
/*      */       }
/*  849 */       throw new DTMException(XMLMessages.createXMLMessage("ER_ITERATOR_CLONE_NOT_SUPPORTED", null));
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  864 */       if (node == 0)
/*  865 */         node = SAX2DTM2.this.getDocument();
/*  866 */       if (this._isRestartable)
/*      */       {
/*  868 */         node = SAX2DTM2.this.makeNodeIdentity(node);
/*      */ 
/*  873 */         if (SAX2DTM2.this._type2(node) == 2) {
/*  874 */           node = SAX2DTM2.this._parent2(node);
/*      */         }
/*  876 */         this._startNode = node;
/*      */         int tmp59_58 = 0; int index = tmp59_58; this._stack[tmp59_58] = node;
/*      */ 
/*  879 */         int parent = node;
/*  880 */         while ((parent = SAX2DTM2.this._parent2(parent)) != -1)
/*      */         {
/*  882 */           index++; if (index == this._stack.length)
/*      */           {
/*  884 */             int[] stack = new int[index * 2];
/*  885 */             System.arraycopy(this._stack, 0, stack, 0, index);
/*  886 */             this._stack = stack;
/*      */           }
/*  888 */           this._stack[index] = parent;
/*      */         }
/*      */ 
/*  891 */         if (index > 0) {
/*  892 */           index--;
/*      */         }
/*  894 */         this._currentNode = this._stack[index];
/*      */ 
/*  896 */         this._oldsp = (this._sp = index);
/*      */ 
/*  898 */         return resetPosition();
/*      */       }
/*      */ 
/*  901 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  914 */       for (this._currentNode += 1; this._sp >= 0; this._currentNode += 1)
/*      */       {
/*  916 */         if (this._currentNode < this._stack[this._sp])
/*      */         {
/*  918 */           int type = SAX2DTM2.this._type2(this._currentNode);
/*  919 */           if ((type != 2) && (type != 13))
/*  920 */             return returnNode(SAX2DTM2.this.makeNodeHandle(this._currentNode));
/*      */         }
/*      */         else {
/*  923 */           this._sp -= 1;
/*      */         }
/*      */       }
/*  925 */       return -1;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator reset()
/*      */     {
/*  939 */       this._sp = this._oldsp;
/*      */ 
/*  941 */       return resetPosition();
/*      */     }
/*      */ 
/*      */     public void setMark() {
/*  945 */       this._markedsp = this._sp;
/*  946 */       this._markedNode = this._currentNode;
/*  947 */       this._markedDescendant = this._stack[0];
/*      */     }
/*      */ 
/*      */     public void gotoMark() {
/*  951 */       this._sp = this._markedsp;
/*  952 */       this._currentNode = this._markedNode;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class PrecedingSiblingIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     protected int _startNodeID;
/*      */ 
/*      */     public PrecedingSiblingIterator()
/*      */     {
/*  614 */       super();
/*      */     }
/*      */ 
/*      */     public boolean isReverse()
/*      */     {
/*  629 */       return true;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  643 */       if (node == 0)
/*  644 */         node = SAX2DTM2.this.getDocument();
/*  645 */       if (this._isRestartable)
/*      */       {
/*  647 */         this._startNode = node;
/*  648 */         node = this._startNodeID = SAX2DTM2.this.makeNodeIdentity(node);
/*      */ 
/*  650 */         if (node == -1)
/*      */         {
/*  652 */           this._currentNode = node;
/*  653 */           return resetPosition();
/*      */         }
/*      */ 
/*  656 */         int type = SAX2DTM2.this._type2(node);
/*  657 */         if ((2 == type) || (13 == type))
/*      */         {
/*  660 */           this._currentNode = node;
/*      */         }
/*      */         else
/*      */         {
/*  665 */           this._currentNode = SAX2DTM2.this._parent2(node);
/*  666 */           if (-1 != this._currentNode)
/*  667 */             this._currentNode = SAX2DTM2.this._firstch2(this._currentNode);
/*      */           else {
/*  669 */             this._currentNode = node;
/*      */           }
/*      */         }
/*  672 */         return resetPosition();
/*      */       }
/*      */ 
/*  675 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  686 */       if ((this._currentNode == this._startNodeID) || (this._currentNode == -1))
/*      */       {
/*  688 */         return -1;
/*      */       }
/*      */ 
/*  692 */       int node = this._currentNode;
/*  693 */       this._currentNode = SAX2DTM2.this._nextsib2(node);
/*      */ 
/*  695 */       return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedAncestorIterator extends SAX2DTM2.AncestorIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedAncestorIterator(int type)
/*      */     {
/* 1404 */       super();
/* 1405 */       this._nodeType = type;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/* 1419 */       if (node == 0)
/* 1420 */         node = SAX2DTM2.this.getDocument();
/* 1421 */       this.m_realStartNode = node;
/*      */ 
/* 1423 */       if (this._isRestartable)
/*      */       {
/* 1425 */         int nodeID = SAX2DTM2.this.makeNodeIdentity(node);
/* 1426 */         this.m_size = 0;
/*      */ 
/* 1428 */         if (nodeID == -1) {
/* 1429 */           this._currentNode = -1;
/* 1430 */           this.m_ancestorsPos = 0;
/* 1431 */           return this;
/*      */         }
/*      */ 
/* 1434 */         int nodeType = this._nodeType;
/*      */ 
/* 1436 */         if (!this._includeSelf) {
/* 1437 */           nodeID = SAX2DTM2.this._parent2(nodeID);
/* 1438 */           node = SAX2DTM2.this.makeNodeHandle(nodeID);
/*      */         }
/*      */ 
/* 1441 */         this._startNode = node;
/*      */ 
/* 1443 */         if (nodeType >= 14) {
/* 1444 */           while (nodeID != -1) {
/* 1445 */             int eType = SAX2DTM2.this._exptype2(nodeID);
/*      */ 
/* 1447 */             if (eType == nodeType) {
/* 1448 */               if (this.m_size >= this.m_ancestors.length)
/*      */               {
/* 1450 */                 int[] newAncestors = new int[this.m_size * 2];
/* 1451 */                 System.arraycopy(this.m_ancestors, 0, newAncestors, 0, this.m_ancestors.length);
/* 1452 */                 this.m_ancestors = newAncestors;
/*      */               }
/* 1454 */               this.m_ancestors[(this.m_size++)] = SAX2DTM2.this.makeNodeHandle(nodeID);
/*      */             }
/* 1456 */             nodeID = SAX2DTM2.this._parent2(nodeID);
/*      */           }
/*      */         }
/*      */ 
/* 1460 */         while (nodeID != -1) {
/* 1461 */           int eType = SAX2DTM2.this._exptype2(nodeID);
/*      */ 
/* 1463 */           if (((eType < 14) && (eType == nodeType)) || ((eType >= 14) && (SAX2DTM2.this.m_extendedTypes[eType].getNodeType() == nodeType)))
/*      */           {
/* 1466 */             if (this.m_size >= this.m_ancestors.length)
/*      */             {
/* 1468 */               int[] newAncestors = new int[this.m_size * 2];
/* 1469 */               System.arraycopy(this.m_ancestors, 0, newAncestors, 0, this.m_ancestors.length);
/* 1470 */               this.m_ancestors = newAncestors;
/*      */             }
/* 1472 */             this.m_ancestors[(this.m_size++)] = SAX2DTM2.this.makeNodeHandle(nodeID);
/*      */           }
/* 1474 */           nodeID = SAX2DTM2.this._parent2(nodeID);
/*      */         }
/*      */ 
/* 1477 */         this.m_ancestorsPos = (this.m_size - 1);
/*      */ 
/* 1479 */         this._currentNode = (this.m_ancestorsPos >= 0 ? this.m_ancestors[this.m_ancestorsPos] : -1);
/*      */ 
/* 1483 */         return resetPosition();
/*      */       }
/*      */ 
/* 1486 */       return this;
/*      */     }
/*      */ 
/*      */     public int getNodeByPosition(int position)
/*      */     {
/* 1494 */       if ((position > 0) && (position <= this.m_size)) {
/* 1495 */         return this.m_ancestors[(position - 1)];
/*      */       }
/*      */ 
/* 1498 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getLast()
/*      */     {
/* 1506 */       return this.m_size;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedAttributeIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedAttributeIterator(int nodeType)
/*      */     {
/*  565 */       super();
/*  566 */       this._nodeType = nodeType;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  581 */       if (this._isRestartable)
/*      */       {
/*  583 */         this._startNode = node;
/*      */ 
/*  585 */         this._currentNode = SAX2DTM2.this.getTypedAttribute(node, this._nodeType);
/*      */ 
/*  587 */         return resetPosition();
/*      */       }
/*      */ 
/*  590 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  601 */       int node = this._currentNode;
/*      */ 
/*  605 */       this._currentNode = -1;
/*      */ 
/*  607 */       return returnNode(node);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedChildrenIterator extends DTMDefaultBaseIterators.InternalAxisIteratorBase
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedChildrenIterator(int nodeType)
/*      */     {
/*  233 */       super();
/*  234 */       this._nodeType = nodeType;
/*      */     }
/*      */ 
/*      */     public DTMAxisIterator setStartNode(int node)
/*      */     {
/*  248 */       if (node == 0)
/*  249 */         node = SAX2DTM2.this.getDocument();
/*  250 */       if (this._isRestartable)
/*      */       {
/*  252 */         this._startNode = node;
/*  253 */         this._currentNode = (node == -1 ? -1 : SAX2DTM2.this._firstch2(SAX2DTM2.this.makeNodeIdentity(this._startNode)));
/*      */ 
/*  257 */         return resetPosition();
/*      */       }
/*      */ 
/*  260 */       return this;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  270 */       int node = this._currentNode;
/*  271 */       if (node == -1) {
/*  272 */         return -1;
/*      */       }
/*  274 */       int nodeType = this._nodeType;
/*      */ 
/*  276 */       if (nodeType != 1) {
/*  277 */         while ((node != -1) && (SAX2DTM2.this._exptype2(node) != nodeType)) {
/*  278 */           node = SAX2DTM2.this._nextsib2(node);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  288 */       while (node != -1) {
/*  289 */         int eType = SAX2DTM2.this._exptype2(node);
/*  290 */         if (eType >= 14) {
/*      */           break;
/*      */         }
/*  293 */         node = SAX2DTM2.this._nextsib2(node);
/*      */       }
/*      */ 
/*  297 */       if (node == -1) {
/*  298 */         this._currentNode = -1;
/*  299 */         return -1;
/*      */       }
/*  301 */       this._currentNode = SAX2DTM2.this._nextsib2(node);
/*  302 */       return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */ 
/*      */     public int getNodeByPosition(int position)
/*      */     {
/*  312 */       if (position <= 0) {
/*  313 */         return -1;
/*      */       }
/*  315 */       int node = this._currentNode;
/*  316 */       int pos = 0;
/*      */ 
/*  318 */       int nodeType = this._nodeType;
/*  319 */       if (nodeType != 1) {
/*  320 */         while (node != -1) {
/*  321 */           if (SAX2DTM2.this._exptype2(node) == nodeType) {
/*  322 */             pos++;
/*  323 */             if (pos == position) {
/*  324 */               return SAX2DTM2.this.makeNodeHandle(node);
/*      */             }
/*      */           }
/*  327 */           node = SAX2DTM2.this._nextsib2(node);
/*      */         }
/*  329 */         return -1;
/*      */       }
/*      */ 
/*  332 */       while (node != -1) {
/*  333 */         if (SAX2DTM2.this._exptype2(node) >= 14) {
/*  334 */           pos++;
/*  335 */           if (pos == position)
/*  336 */             return SAX2DTM2.this.makeNodeHandle(node);
/*      */         }
/*  338 */         node = SAX2DTM2.this._nextsib2(node);
/*      */       }
/*  340 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedDescendantIterator extends SAX2DTM2.DescendantIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedDescendantIterator(int nodeType)
/*      */     {
/* 1651 */       super();
/* 1652 */       this._nodeType = nodeType;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1662 */       int startNode = this._startNode;
/* 1663 */       if (this._startNode == -1) {
/* 1664 */         return -1;
/*      */       }
/*      */ 
/* 1667 */       int node = this._currentNode;
/*      */ 
/* 1670 */       int nodeType = this._nodeType;
/*      */ 
/* 1672 */       if (nodeType != 1)
/*      */       {
/*      */         int expType;
/*      */         do {
/* 1676 */           node++;
/* 1677 */           expType = SAX2DTM2.this._exptype2(node);
/*      */ 
/* 1679 */           if ((-1 == expType) || ((SAX2DTM2.this._parent2(node) < startNode) && (startNode != node))) {
/* 1680 */             this._currentNode = -1;
/* 1681 */             return -1;
/*      */           }
/*      */         }
/* 1684 */         while (expType != nodeType);
/*      */       }
/* 1689 */       else if (startNode == 0)
/*      */       {
/*      */         int expType;
/*      */         do {
/* 1693 */           node++;
/* 1694 */           expType = SAX2DTM2.this._exptype2(node);
/*      */ 
/* 1696 */           if (-1 == expType) {
/* 1697 */             this._currentNode = -1;
/* 1698 */             return -1;
/*      */           }
/*      */         }
/* 1701 */         while ((expType < 14) || (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() != 1));
/*      */       }
/*      */       else
/*      */       {
/*      */         int expType;
/*      */         do {
/* 1707 */           node++;
/* 1708 */           expType = SAX2DTM2.this._exptype2(node);
/*      */ 
/* 1710 */           if ((-1 == expType) || ((SAX2DTM2.this._parent2(node) < startNode) && (startNode != node))) {
/* 1711 */             this._currentNode = -1;
/* 1712 */             return -1;
/*      */           }
/*      */         }
/*      */ 
/* 1716 */         while ((expType < 14) || (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() != 1));
/*      */       }
/*      */ 
/* 1719 */       this._currentNode = node;
/* 1720 */       return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedFollowingIterator extends SAX2DTM2.FollowingIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedFollowingIterator(int type)
/*      */     {
/* 1159 */       super();
/* 1160 */       this._nodeType = type;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1174 */       int nodeType = this._nodeType;
/* 1175 */       int currentNodeID = SAX2DTM2.this.makeNodeIdentity(this._currentNode);
/*      */       int node;
/* 1177 */       if (nodeType >= 14) { int node;
/*      */         do { node = currentNodeID;
/* 1180 */           int current = node;
/*      */           int type;
/*      */           do { current++;
/* 1184 */             type = SAX2DTM2.this._type2(current);
/*      */           }
/* 1186 */           while ((type != -1) && ((2 == type) || (13 == type)));
/*      */ 
/* 1188 */           currentNodeID = type != -1 ? current : -1;
/*      */ 
/* 1190 */           if (node == -1) break;  } while (SAX2DTM2.this._exptype2(node) != nodeType); } else
/*      */       {
/*      */         do { node = currentNodeID;
/* 1195 */           int current = node;
/*      */           int type;
/*      */           do {
/* 1198 */             current++;
/* 1199 */             type = SAX2DTM2.this._type2(current);
/*      */           }
/* 1201 */           while ((type != -1) && ((2 == type) || (13 == type)));
/*      */ 
/* 1203 */           currentNodeID = type != -1 ? current : -1;
/*      */         }
/*      */ 
/* 1206 */         while ((node != -1) && (SAX2DTM2.this._exptype2(node) != nodeType) && (SAX2DTM2.this._type2(node) != nodeType));
/*      */       }
/*      */ 
/* 1209 */       this._currentNode = SAX2DTM2.this.makeNodeHandle(currentNodeID);
/* 1210 */       return node == -1 ? -1 : returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedFollowingSiblingIterator extends SAX2DTM2.FollowingSiblingIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedFollowingSiblingIterator(int type)
/*      */     {
/*  464 */       super();
/*  465 */       this._nodeType = type;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  475 */       if (this._currentNode == -1) {
/*  476 */         return -1;
/*      */       }
/*      */ 
/*  479 */       int node = this._currentNode;
/*  480 */       int nodeType = this._nodeType;
/*      */ 
/*  482 */       if (nodeType != 1)
/*      */       {
/*  483 */         while (((node = SAX2DTM2.this._nextsib2(node)) != -1) && (SAX2DTM2.this._exptype2(node) != nodeType));
/*      */       }
/*  486 */       while (((node = SAX2DTM2.this._nextsib2(node)) != -1) && (SAX2DTM2.this._exptype2(node) < 14));
/*  489 */       this._currentNode = node;
/*      */ 
/*  491 */       return node == -1 ? -1 : returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedPrecedingIterator extends SAX2DTM2.PrecedingIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedPrecedingIterator(int type)
/*      */     {
/*  974 */       super();
/*  975 */       this._nodeType = type;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  985 */       int node = this._currentNode;
/*  986 */       int nodeType = this._nodeType;
/*      */ 
/*  988 */       if (nodeType >= 14) do {
/*      */           do {
/*  990 */             node++;
/*      */ 
/*  992 */             if (this._sp < 0) {
/*  993 */               node = -1;
/*  994 */               break label167;
/*      */             }
/*  996 */             if (node < this._stack[this._sp]) break; 
/*  997 */           }while (--this._sp >= 0);
/*  998 */           node = -1;
/*  999 */           break;
/*      */         }
/*      */ 
/* 1002 */         while (SAX2DTM2.this._exptype2(node) != nodeType);
/*      */       else
/*      */       {
/*      */         while (true)
/*      */         {
/* 1011 */           node++;
/*      */ 
/* 1013 */           if (this._sp < 0) {
/* 1014 */             node = -1;
/*      */           }
/* 1017 */           else if (node >= this._stack[this._sp]) {
/* 1018 */             if (--this._sp < 0) {
/* 1019 */               node = -1;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1024 */             int expType = SAX2DTM2.this._exptype2(node);
/* 1025 */             if (expType < 14) {
/* 1026 */               if (expType == nodeType) {
/* 1027 */                 break;
/*      */               }
/*      */ 
/*      */             }
/* 1031 */             else if (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() == nodeType) {
/* 1032 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1039 */       label167: this._currentNode = node;
/*      */ 
/* 1041 */       return node == -1 ? -1 : returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedPrecedingSiblingIterator extends SAX2DTM2.PrecedingSiblingIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedPrecedingSiblingIterator(int type)
/*      */     {
/*  718 */       super();
/*  719 */       this._nodeType = type;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  729 */       int node = this._currentNode;
/*      */ 
/*  731 */       int nodeType = this._nodeType;
/*  732 */       int startNodeID = this._startNodeID;
/*      */ 
/*  734 */       if (nodeType != 1) {
/*  735 */         while ((node != -1) && (node != startNodeID) && (SAX2DTM2.this._exptype2(node) != nodeType)) {
/*  736 */           node = SAX2DTM2.this._nextsib2(node);
/*      */         }
/*      */       }
/*      */ 
/*  740 */       while ((node != -1) && (node != startNodeID) && (SAX2DTM2.this._exptype2(node) < 14)) {
/*  741 */         node = SAX2DTM2.this._nextsib2(node);
/*      */       }
/*      */ 
/*  745 */       if ((node == -1) || (node == startNodeID)) {
/*  746 */         this._currentNode = -1;
/*  747 */         return -1;
/*      */       }
/*      */ 
/*  750 */       this._currentNode = SAX2DTM2.this._nextsib2(node);
/*  751 */       return returnNode(SAX2DTM2.this.makeNodeHandle(node));
/*      */     }
/*      */ 
/*      */     public int getLast()
/*      */     {
/*  760 */       if (this._last != -1) {
/*  761 */         return this._last;
/*      */       }
/*  763 */       setMark();
/*      */ 
/*  765 */       int node = this._currentNode;
/*  766 */       int nodeType = this._nodeType;
/*  767 */       int startNodeID = this._startNodeID;
/*      */ 
/*  769 */       int last = 0;
/*  770 */       if (nodeType != 1) {
/*  771 */         while ((node != -1) && (node != startNodeID)) {
/*  772 */           if (SAX2DTM2.this._exptype2(node) == nodeType) {
/*  773 */             last++;
/*      */           }
/*  775 */           node = SAX2DTM2.this._nextsib2(node);
/*      */         }
/*      */       }
/*      */ 
/*  779 */       while ((node != -1) && (node != startNodeID)) {
/*  780 */         if (SAX2DTM2.this._exptype2(node) >= 14) {
/*  781 */           last++;
/*      */         }
/*  783 */         node = SAX2DTM2.this._nextsib2(node);
/*      */       }
/*      */ 
/*  787 */       gotoMark();
/*      */ 
/*  789 */       return this._last = last;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class TypedRootIterator extends DTMDefaultBaseIterators.RootIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedRootIterator(int nodeType)
/*      */     {
/*  363 */       super();
/*  364 */       this._nodeType = nodeType;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/*  374 */       if (this._startNode == this._currentNode) {
/*  375 */         return -1;
/*      */       }
/*  377 */       int node = this._startNode;
/*  378 */       int expType = SAX2DTM2.this._exptype2(SAX2DTM2.this.makeNodeIdentity(node));
/*      */ 
/*  380 */       this._currentNode = node;
/*      */ 
/*  382 */       if (this._nodeType >= 14) {
/*  383 */         if (this._nodeType == expType) {
/*  384 */           return returnNode(node);
/*      */         }
/*      */ 
/*      */       }
/*  388 */       else if (expType < 14) {
/*  389 */         if (expType == this._nodeType) {
/*  390 */           return returnNode(node);
/*      */         }
/*      */ 
/*      */       }
/*  394 */       else if (SAX2DTM2.this.m_extendedTypes[expType].getNodeType() == this._nodeType) {
/*  395 */         return returnNode(node);
/*      */       }
/*      */ 
/*  400 */       return -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TypedSingletonIterator extends DTMDefaultBaseIterators.SingletonIterator
/*      */   {
/*      */     private final int _nodeType;
/*      */ 
/*      */     public TypedSingletonIterator(int nodeType)
/*      */     {
/* 1740 */       super();
/* 1741 */       this._nodeType = nodeType;
/*      */     }
/*      */ 
/*      */     public int next()
/*      */     {
/* 1752 */       int result = this._currentNode;
/* 1753 */       if (result == -1) {
/* 1754 */         return -1;
/*      */       }
/* 1756 */       this._currentNode = -1;
/*      */ 
/* 1758 */       if (this._nodeType >= 14) {
/* 1759 */         if (SAX2DTM2.this._exptype2(SAX2DTM2.this.makeNodeIdentity(result)) == this._nodeType) {
/* 1760 */           return returnNode(result);
/*      */         }
/*      */ 
/*      */       }
/* 1764 */       else if (SAX2DTM2.this._type2(SAX2DTM2.this.makeNodeIdentity(result)) == this._nodeType) {
/* 1765 */         return returnNode(result);
/*      */       }
/*      */ 
/* 1769 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2
 * JD-Core Version:    0.6.2
 */