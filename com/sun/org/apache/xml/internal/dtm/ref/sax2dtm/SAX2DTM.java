/*      */ package com.sun.org.apache.xml.internal.dtm.ref.sax2dtm;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMTreeWalker;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.NodeLocator;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*      */ import com.sun.org.apache.xml.internal.utils.IntStack;
/*      */ import com.sun.org.apache.xml.internal.utils.IntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.StringVector;
/*      */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*      */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class SAX2DTM extends DTMDefaultBaseIterators
/*      */   implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, DeclHandler, LexicalHandler
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*   69 */   private IncrementalSAXSource m_incrementalSAXSource = null;
/*      */   protected FastStringBuffer m_chars;
/*      */   protected SuballocatedIntVector m_data;
/*      */   protected transient IntStack m_parents;
/*   99 */   protected transient int m_previous = 0;
/*      */ 
/*  104 */   protected transient Vector m_prefixMappings = new Vector();
/*      */   protected transient IntStack m_contextIndexes;
/*  113 */   protected transient int m_textType = 3;
/*      */ 
/*  119 */   protected transient int m_coalescedTextType = 3;
/*      */ 
/*  122 */   protected transient Locator m_locator = null;
/*      */ 
/*  125 */   private transient String m_systemId = null;
/*      */ 
/*  128 */   protected transient boolean m_insideDTD = false;
/*      */ 
/*  131 */   protected DTMTreeWalker m_walker = new DTMTreeWalker();
/*      */   protected DTMStringPool m_valuesOrPrefixes;
/*  139 */   protected boolean m_endDocumentOccured = false;
/*      */   protected SuballocatedIntVector m_dataOrQName;
/*  148 */   protected Hashtable m_idAttributes = new Hashtable();
/*      */ 
/*  153 */   private static final String[] m_fixednames = { null, null, null, "#text", "#cdata_section", null, null, null, "#comment", "#document", null, "#document-fragment", null };
/*      */ 
/*  166 */   private Vector m_entities = null;
/*      */   private static final int ENTITY_FIELD_PUBLICID = 0;
/*      */   private static final int ENTITY_FIELD_SYSTEMID = 1;
/*      */   private static final int ENTITY_FIELD_NOTATIONNAME = 2;
/*      */   private static final int ENTITY_FIELD_NAME = 3;
/*      */   private static final int ENTITY_FIELDS_PER = 4;
/*  188 */   protected int m_textPendingStart = -1;
/*      */ 
/*  196 */   protected boolean m_useSourceLocationProperty = false;
/*      */   protected StringVector m_sourceSystemId;
/*      */   protected IntVector m_sourceLine;
/*      */   protected IntVector m_sourceColumn;
/* 1838 */   boolean m_pastFirstElement = false;
/*      */ 
/*      */   public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*      */   {
/*  226 */     this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
/*      */   }
/*      */ 
/*      */   public SAX2DTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
/*      */   {
/*  255 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, blocksize, usePrevsib, newNameTable);
/*      */ 
/*  260 */     if (blocksize <= 64)
/*      */     {
/*  262 */       this.m_data = new SuballocatedIntVector(blocksize, 4);
/*  263 */       this.m_dataOrQName = new SuballocatedIntVector(blocksize, 4);
/*  264 */       this.m_valuesOrPrefixes = new DTMStringPool(16);
/*  265 */       this.m_chars = new FastStringBuffer(7, 10);
/*  266 */       this.m_contextIndexes = new IntStack(4);
/*  267 */       this.m_parents = new IntStack(4);
/*      */     }
/*      */     else
/*      */     {
/*  271 */       this.m_data = new SuballocatedIntVector(blocksize, 32);
/*  272 */       this.m_dataOrQName = new SuballocatedIntVector(blocksize, 32);
/*  273 */       this.m_valuesOrPrefixes = new DTMStringPool();
/*  274 */       this.m_chars = new FastStringBuffer(10, 13);
/*  275 */       this.m_contextIndexes = new IntStack();
/*  276 */       this.m_parents = new IntStack();
/*      */     }
/*      */ 
/*  284 */     this.m_data.addElement(0);
/*      */ 
/*  289 */     this.m_useSourceLocationProperty = mgr.getSource_location();
/*  290 */     this.m_sourceSystemId = (this.m_useSourceLocationProperty ? new StringVector() : null);
/*  291 */     this.m_sourceLine = (this.m_useSourceLocationProperty ? new IntVector() : null);
/*  292 */     this.m_sourceColumn = (this.m_useSourceLocationProperty ? new IntVector() : null);
/*      */   }
/*      */ 
/*      */   public void setUseSourceLocation(boolean useSourceLocation)
/*      */   {
/*  301 */     this.m_useSourceLocationProperty = useSourceLocation;
/*      */   }
/*      */ 
/*      */   protected int _dataOrQName(int identity)
/*      */   {
/*  314 */     if (identity < this.m_size) {
/*  315 */       return this.m_dataOrQName.elementAt(identity);
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  322 */       boolean isMore = nextNode();
/*      */ 
/*  324 */       if (!isMore)
/*  325 */         return -1;
/*  326 */       if (identity < this.m_size)
/*  327 */         return this.m_dataOrQName.elementAt(identity);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearCoRoutine()
/*      */   {
/*  336 */     clearCoRoutine(true);
/*      */   }
/*      */ 
/*      */   public void clearCoRoutine(boolean callDoTerminate)
/*      */   {
/*  349 */     if (null != this.m_incrementalSAXSource)
/*      */     {
/*  351 */       if (callDoTerminate) {
/*  352 */         this.m_incrementalSAXSource.deliverMoreNodes(false);
/*      */       }
/*  354 */       this.m_incrementalSAXSource = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setIncrementalSAXSource(IncrementalSAXSource incrementalSAXSource)
/*      */   {
/*  380 */     this.m_incrementalSAXSource = incrementalSAXSource;
/*      */ 
/*  383 */     incrementalSAXSource.setContentHandler(this);
/*  384 */     incrementalSAXSource.setLexicalHandler(this);
/*  385 */     incrementalSAXSource.setDTDHandler(this);
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/*  411 */     if (this.m_incrementalSAXSource.getClass().getName().equals("com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter"))
/*      */     {
/*  413 */       return (ContentHandler)this.m_incrementalSAXSource;
/*      */     }
/*  415 */     return this;
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler()
/*      */   {
/*  434 */     if (this.m_incrementalSAXSource.getClass().getName().equals("com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Filter"))
/*      */     {
/*  436 */       return (LexicalHandler)this.m_incrementalSAXSource;
/*      */     }
/*  438 */     return this;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/*  448 */     return this;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/*  458 */     return this;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/*  468 */     return this;
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler()
/*      */   {
/*  478 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean needsTwoThreads()
/*      */   {
/*  489 */     return null != this.m_incrementalSAXSource;
/*      */   }
/*      */ 
/*      */   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
/*      */     throws SAXException
/*      */   {
/*  514 */     int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/*  516 */     if (identity == -1) {
/*  517 */       return;
/*      */     }
/*  519 */     int type = _type(identity);
/*      */ 
/*  521 */     if (isTextType(type))
/*      */     {
/*  523 */       int dataIndex = this.m_dataOrQName.elementAt(identity);
/*  524 */       int offset = this.m_data.elementAt(dataIndex);
/*  525 */       int length = this.m_data.elementAt(dataIndex + 1);
/*      */ 
/*  527 */       if (normalize)
/*  528 */         this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
/*      */       else
/*  530 */         this.m_chars.sendSAXcharacters(ch, offset, length);
/*      */     }
/*      */     else
/*      */     {
/*  534 */       int firstChild = _firstch(identity);
/*      */ 
/*  536 */       if (-1 != firstChild)
/*      */       {
/*  538 */         int offset = -1;
/*  539 */         int length = 0;
/*  540 */         int startNode = identity;
/*      */ 
/*  542 */         identity = firstChild;
/*      */         do
/*      */         {
/*  545 */           type = _type(identity);
/*      */ 
/*  547 */           if (isTextType(type))
/*      */           {
/*  549 */             int dataIndex = _dataOrQName(identity);
/*      */ 
/*  551 */             if (-1 == offset)
/*      */             {
/*  553 */               offset = this.m_data.elementAt(dataIndex);
/*      */             }
/*      */ 
/*  556 */             length += this.m_data.elementAt(dataIndex + 1);
/*      */           }
/*      */ 
/*  559 */           identity = getNextNodeIdentity(identity);
/*  560 */         }while ((-1 != identity) && (_parent(identity) >= startNode));
/*      */ 
/*  562 */         if (length > 0)
/*      */         {
/*  564 */           if (normalize)
/*  565 */             this.m_chars.sendNormalizedSAXcharacters(ch, offset, length);
/*      */           else
/*  567 */             this.m_chars.sendSAXcharacters(ch, offset, length);
/*      */         }
/*      */       }
/*  570 */       else if (type != 1)
/*      */       {
/*  572 */         int dataIndex = _dataOrQName(identity);
/*      */ 
/*  574 */         if (dataIndex < 0)
/*      */         {
/*  576 */           dataIndex = -dataIndex;
/*  577 */           dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */         }
/*      */ 
/*  580 */         String str = this.m_valuesOrPrefixes.indexToString(dataIndex);
/*      */ 
/*  582 */         if (normalize) {
/*  583 */           FastStringBuffer.sendNormalizedSAXcharacters(str.toCharArray(), 0, str.length(), ch);
/*      */         }
/*      */         else
/*  586 */           ch.characters(str.toCharArray(), 0, str.length());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeHandle)
/*      */   {
/*  604 */     int expandedTypeID = getExpandedTypeID(nodeHandle);
/*      */ 
/*  606 */     int namespaceID = this.m_expandedNameTable.getNamespaceID(expandedTypeID);
/*      */ 
/*  608 */     if (0 == namespaceID)
/*      */     {
/*  612 */       int type = getNodeType(nodeHandle);
/*      */ 
/*  614 */       if (type == 13)
/*      */       {
/*  616 */         if (null == this.m_expandedNameTable.getLocalName(expandedTypeID)) {
/*  617 */           return "xmlns";
/*      */         }
/*  619 */         return "xmlns:" + this.m_expandedNameTable.getLocalName(expandedTypeID);
/*      */       }
/*  621 */       if (0 == this.m_expandedNameTable.getLocalNameID(expandedTypeID))
/*      */       {
/*  623 */         return m_fixednames[type];
/*      */       }
/*      */ 
/*  626 */       return this.m_expandedNameTable.getLocalName(expandedTypeID);
/*      */     }
/*      */ 
/*  630 */     int qnameIndex = this.m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
/*      */ 
/*  632 */     if (qnameIndex < 0)
/*      */     {
/*  634 */       qnameIndex = -qnameIndex;
/*  635 */       qnameIndex = this.m_data.elementAt(qnameIndex);
/*      */     }
/*      */ 
/*  638 */     return this.m_valuesOrPrefixes.indexToString(qnameIndex);
/*      */   }
/*      */ 
/*      */   public String getNodeNameX(int nodeHandle)
/*      */   {
/*  653 */     int expandedTypeID = getExpandedTypeID(nodeHandle);
/*  654 */     int namespaceID = this.m_expandedNameTable.getNamespaceID(expandedTypeID);
/*      */ 
/*  656 */     if (0 == namespaceID)
/*      */     {
/*  658 */       String name = this.m_expandedNameTable.getLocalName(expandedTypeID);
/*      */ 
/*  660 */       if (name == null) {
/*  661 */         return "";
/*      */       }
/*  663 */       return name;
/*      */     }
/*      */ 
/*  667 */     int qnameIndex = this.m_dataOrQName.elementAt(makeNodeIdentity(nodeHandle));
/*      */ 
/*  669 */     if (qnameIndex < 0)
/*      */     {
/*  671 */       qnameIndex = -qnameIndex;
/*  672 */       qnameIndex = this.m_data.elementAt(qnameIndex);
/*      */     }
/*      */ 
/*  675 */     return this.m_valuesOrPrefixes.indexToString(qnameIndex);
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int attributeHandle)
/*      */   {
/*  692 */     return true;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationSystemIdentifier()
/*      */   {
/*  707 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */ 
/*  709 */     return null;
/*      */   }
/*      */ 
/*      */   protected int getNextNodeIdentity(int identity)
/*      */   {
/*  722 */     identity++;
/*      */ 
/*  724 */     while (identity >= this.m_size)
/*      */     {
/*  726 */       if (null == this.m_incrementalSAXSource) {
/*  727 */         return -1;
/*      */       }
/*  729 */       nextNode();
/*      */     }
/*      */ 
/*  732 */     return identity;
/*      */   }
/*      */ 
/*      */   public void dispatchToEvents(int nodeHandle, ContentHandler ch)
/*      */     throws SAXException
/*      */   {
/*  747 */     DTMTreeWalker treeWalker = this.m_walker;
/*  748 */     ContentHandler prevCH = treeWalker.getcontentHandler();
/*      */ 
/*  750 */     if (null != prevCH)
/*      */     {
/*  752 */       treeWalker = new DTMTreeWalker();
/*      */     }
/*      */ 
/*  755 */     treeWalker.setcontentHandler(ch);
/*  756 */     treeWalker.setDTM(this);
/*      */     try
/*      */     {
/*  760 */       treeWalker.traverse(nodeHandle);
/*      */     }
/*      */     finally
/*      */     {
/*  764 */       treeWalker.setcontentHandler(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getNumberOfNodes()
/*      */   {
/*  775 */     return this.m_size;
/*      */   }
/*      */ 
/*      */   protected boolean nextNode()
/*      */   {
/*  787 */     if (null == this.m_incrementalSAXSource) {
/*  788 */       return false;
/*      */     }
/*  790 */     if (this.m_endDocumentOccured)
/*      */     {
/*  792 */       clearCoRoutine();
/*      */ 
/*  794 */       return false;
/*      */     }
/*      */ 
/*  797 */     Object gotMore = this.m_incrementalSAXSource.deliverMoreNodes(true);
/*      */ 
/*  806 */     if (!(gotMore instanceof Boolean))
/*      */     {
/*  808 */       if ((gotMore instanceof RuntimeException))
/*      */       {
/*  810 */         throw ((RuntimeException)gotMore);
/*      */       }
/*  812 */       if ((gotMore instanceof Exception))
/*      */       {
/*  814 */         throw new WrappedRuntimeException((Exception)gotMore);
/*      */       }
/*      */ 
/*  817 */       clearCoRoutine();
/*      */ 
/*  819 */       return false;
/*      */     }
/*      */ 
/*  824 */     if (gotMore != Boolean.TRUE)
/*      */     {
/*  828 */       clearCoRoutine();
/*      */     }
/*      */ 
/*  833 */     return true;
/*      */   }
/*      */ 
/*      */   private final boolean isTextType(int type)
/*      */   {
/*  845 */     return (3 == type) || (4 == type);
/*      */   }
/*      */ 
/*      */   protected int addNode(int type, int expandedTypeID, int parentIndex, int previousSibling, int dataOrPrefix, boolean canHaveFirstChild)
/*      */   {
/*  882 */     int nodeIndex = this.m_size++;
/*      */ 
/*  885 */     if (this.m_dtmIdent.size() == nodeIndex >>> 16)
/*      */     {
/*  887 */       addNewDTMID(nodeIndex);
/*      */     }
/*      */ 
/*  890 */     this.m_firstch.addElement(canHaveFirstChild ? -2 : -1);
/*  891 */     this.m_nextsib.addElement(-2);
/*  892 */     this.m_parent.addElement(parentIndex);
/*  893 */     this.m_exptype.addElement(expandedTypeID);
/*  894 */     this.m_dataOrQName.addElement(dataOrPrefix);
/*      */ 
/*  896 */     if (this.m_prevsib != null) {
/*  897 */       this.m_prevsib.addElement(previousSibling);
/*      */     }
/*      */ 
/*  900 */     if (-1 != previousSibling) {
/*  901 */       this.m_nextsib.setElementAt(nodeIndex, previousSibling);
/*      */     }
/*      */ 
/*  904 */     if ((this.m_locator != null) && (this.m_useSourceLocationProperty)) {
/*  905 */       setSourceLocation();
/*      */     }
/*      */ 
/*  912 */     switch (type)
/*      */     {
/*      */     case 13:
/*  915 */       declareNamespaceInContext(parentIndex, nodeIndex);
/*  916 */       break;
/*      */     case 2:
/*  918 */       break;
/*      */     default:
/*  920 */       if ((-1 == previousSibling) && (-1 != parentIndex)) {
/*  921 */         this.m_firstch.setElementAt(nodeIndex, parentIndex);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/*  926 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   protected void addNewDTMID(int nodeIndex)
/*      */   {
/*      */     try
/*      */     {
/*  937 */       if (this.m_mgr == null) {
/*  938 */         throw new ClassCastException();
/*      */       }
/*      */ 
/*  941 */       DTMManagerDefault mgrD = (DTMManagerDefault)this.m_mgr;
/*  942 */       int id = mgrD.getFirstFreeDTMID();
/*  943 */       mgrD.addDTM(this, id, nodeIndex);
/*  944 */       this.m_dtmIdent.addElement(id << 16);
/*      */     }
/*      */     catch (ClassCastException e)
/*      */     {
/*  951 */       error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void migrateTo(DTMManager manager)
/*      */   {
/*  963 */     super.migrateTo(manager);
/*      */ 
/*  967 */     int numDTMs = this.m_dtmIdent.size();
/*  968 */     int dtmId = this.m_mgrDefault.getFirstFreeDTMID();
/*  969 */     int nodeIndex = 0;
/*  970 */     for (int i = 0; i < numDTMs; i++)
/*      */     {
/*  972 */       this.m_dtmIdent.setElementAt(dtmId << 16, i);
/*  973 */       this.m_mgrDefault.addDTM(this, dtmId, nodeIndex);
/*  974 */       dtmId++;
/*  975 */       nodeIndex += 65536;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setSourceLocation()
/*      */   {
/*  984 */     this.m_sourceSystemId.addElement(this.m_locator.getSystemId());
/*  985 */     this.m_sourceLine.addElement(this.m_locator.getLineNumber());
/*  986 */     this.m_sourceColumn.addElement(this.m_locator.getColumnNumber());
/*      */ 
/*  991 */     if (this.m_sourceSystemId.size() != this.m_size) {
/*  992 */       String msg = "CODING ERROR in Source Location: " + this.m_size + " != " + this.m_sourceSystemId.size();
/*      */ 
/*  994 */       System.err.println(msg);
/*  995 */       throw new RuntimeException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/* 1011 */     int identity = makeNodeIdentity(nodeHandle);
/* 1012 */     int type = _type(identity);
/*      */ 
/* 1014 */     if (isTextType(type))
/*      */     {
/* 1016 */       int dataIndex = _dataOrQName(identity);
/* 1017 */       int offset = this.m_data.elementAt(dataIndex);
/* 1018 */       int length = this.m_data.elementAt(dataIndex + 1);
/*      */ 
/* 1021 */       return this.m_chars.getString(offset, length);
/*      */     }
/* 1023 */     if ((1 == type) || (11 == type) || (9 == type))
/*      */     {
/* 1026 */       return null;
/*      */     }
/*      */ 
/* 1030 */     int dataIndex = _dataOrQName(identity);
/*      */ 
/* 1032 */     if (dataIndex < 0)
/*      */     {
/* 1034 */       dataIndex = -dataIndex;
/* 1035 */       dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */     }
/*      */ 
/* 1038 */     return this.m_valuesOrPrefixes.indexToString(dataIndex);
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/* 1052 */     return this.m_expandedNameTable.getLocalName(_exptype(makeNodeIdentity(nodeHandle)));
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/* 1092 */     String url = "";
/*      */ 
/* 1094 */     if (null == this.m_entities) {
/* 1095 */       return url;
/*      */     }
/* 1097 */     int n = this.m_entities.size();
/*      */ 
/* 1099 */     for (int i = 0; i < n; i += 4)
/*      */     {
/* 1101 */       String ename = (String)this.m_entities.elementAt(i + 3);
/*      */ 
/* 1103 */       if ((null != ename) && (ename.equals(name)))
/*      */       {
/* 1105 */         String nname = (String)this.m_entities.elementAt(i + 2);
/*      */ 
/* 1108 */         if (null == nname)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/* 1120 */         url = (String)this.m_entities.elementAt(i + 1);
/*      */ 
/* 1122 */         if (null != url)
/*      */           break;
/* 1124 */         url = (String)this.m_entities.elementAt(i + 0); break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1132 */     return url;
/*      */   }
/*      */ 
/*      */   public String getPrefix(int nodeHandle)
/*      */   {
/* 1150 */     int identity = makeNodeIdentity(nodeHandle);
/* 1151 */     int type = _type(identity);
/*      */ 
/* 1153 */     if (1 == type)
/*      */     {
/* 1155 */       int prefixIndex = _dataOrQName(identity);
/*      */ 
/* 1157 */       if (0 == prefixIndex) {
/* 1158 */         return "";
/*      */       }
/*      */ 
/* 1161 */       String qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
/*      */ 
/* 1163 */       return getPrefix(qname, null);
/*      */     }
/*      */ 
/* 1166 */     if (2 == type)
/*      */     {
/* 1168 */       int prefixIndex = _dataOrQName(identity);
/*      */ 
/* 1170 */       if (prefixIndex < 0)
/*      */       {
/* 1172 */         prefixIndex = this.m_data.elementAt(-prefixIndex);
/*      */ 
/* 1174 */         String qname = this.m_valuesOrPrefixes.indexToString(prefixIndex);
/*      */ 
/* 1176 */         return getPrefix(qname, null);
/*      */       }
/*      */     }
/*      */ 
/* 1180 */     return "";
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int nodeHandle, String namespaceURI, String name)
/*      */   {
/* 1199 */     for (int attrH = getFirstAttribute(nodeHandle); -1 != attrH; 
/* 1200 */       attrH = getNextAttribute(attrH))
/*      */     {
/* 1202 */       String attrNS = getNamespaceURI(attrH);
/* 1203 */       String attrName = getLocalName(attrH);
/* 1204 */       boolean nsMatch = (namespaceURI == attrNS) || ((namespaceURI != null) && (namespaceURI.equals(attrNS)));
/*      */ 
/* 1208 */       if ((nsMatch) && (name.equals(attrName))) {
/* 1209 */         return attrH;
/*      */       }
/*      */     }
/* 1212 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationPublicIdentifier()
/*      */   {
/* 1227 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */ 
/* 1229 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int nodeHandle)
/*      */   {
/* 1246 */     return this.m_expandedNameTable.getNamespace(_exptype(makeNodeIdentity(nodeHandle)));
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/* 1260 */     int identity = makeNodeIdentity(nodeHandle);
/*      */     int type;
/*      */     int type;
/* 1262 */     if (identity == -1)
/* 1263 */       type = -1;
/*      */     else {
/* 1265 */       type = _type(identity);
/*      */     }
/* 1267 */     if (isTextType(type))
/*      */     {
/* 1269 */       int dataIndex = _dataOrQName(identity);
/* 1270 */       int offset = this.m_data.elementAt(dataIndex);
/* 1271 */       int length = this.m_data.elementAt(dataIndex + 1);
/*      */ 
/* 1273 */       return this.m_xstrf.newstr(this.m_chars, offset, length);
/*      */     }
/*      */ 
/* 1277 */     int firstChild = _firstch(identity);
/*      */ 
/* 1279 */     if (-1 != firstChild)
/*      */     {
/* 1281 */       int offset = -1;
/* 1282 */       int length = 0;
/* 1283 */       int startNode = identity;
/*      */ 
/* 1285 */       identity = firstChild;
/*      */       do
/*      */       {
/* 1288 */         type = _type(identity);
/*      */ 
/* 1290 */         if (isTextType(type))
/*      */         {
/* 1292 */           int dataIndex = _dataOrQName(identity);
/*      */ 
/* 1294 */           if (-1 == offset)
/*      */           {
/* 1296 */             offset = this.m_data.elementAt(dataIndex);
/*      */           }
/*      */ 
/* 1299 */           length += this.m_data.elementAt(dataIndex + 1);
/*      */         }
/*      */ 
/* 1302 */         identity = getNextNodeIdentity(identity);
/* 1303 */       }while ((-1 != identity) && (_parent(identity) >= startNode));
/*      */ 
/* 1305 */       if (length > 0)
/*      */       {
/* 1307 */         return this.m_xstrf.newstr(this.m_chars, offset, length);
/*      */       }
/*      */     }
/* 1310 */     else if (type != 1)
/*      */     {
/* 1312 */       int dataIndex = _dataOrQName(identity);
/*      */ 
/* 1314 */       if (dataIndex < 0)
/*      */       {
/* 1316 */         dataIndex = -dataIndex;
/* 1317 */         dataIndex = this.m_data.elementAt(dataIndex + 1);
/*      */       }
/* 1319 */       return this.m_xstrf.newstr(this.m_valuesOrPrefixes.indexToString(dataIndex));
/*      */     }
/*      */ 
/* 1323 */     return this.m_xstrf.emptystr();
/*      */   }
/*      */ 
/*      */   public boolean isWhitespace(int nodeHandle)
/*      */   {
/* 1335 */     int identity = makeNodeIdentity(nodeHandle);
/*      */     int type;
/*      */     int type;
/* 1337 */     if (identity == -1)
/* 1338 */       type = -1;
/*      */     else {
/* 1340 */       type = _type(identity);
/*      */     }
/* 1342 */     if (isTextType(type))
/*      */     {
/* 1344 */       int dataIndex = _dataOrQName(identity);
/* 1345 */       int offset = this.m_data.elementAt(dataIndex);
/* 1346 */       int length = this.m_data.elementAt(dataIndex + 1);
/*      */ 
/* 1348 */       return this.m_chars.isWhitespace(offset, length);
/*      */     }
/* 1350 */     return false;
/*      */   }
/*      */ 
/*      */   public int getElementById(String elementId)
/*      */   {
/* 1374 */     boolean isMore = true;
/*      */     Integer intObj;
/*      */     do
/*      */     {
/* 1378 */       intObj = (Integer)this.m_idAttributes.get(elementId);
/*      */ 
/* 1380 */       if (null != intObj) {
/* 1381 */         return makeNodeHandle(intObj.intValue());
/*      */       }
/* 1383 */       if ((!isMore) || (this.m_endDocumentOccured)) {
/*      */         break;
/*      */       }
/* 1386 */       isMore = nextNode();
/*      */     }
/* 1388 */     while (null == intObj);
/*      */ 
/* 1390 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getPrefix(String qname, String uri)
/*      */   {
/* 1406 */     int uriIndex = -1;
/*      */     String prefix;
/*      */     String prefix;
/* 1408 */     if ((null != uri) && (uri.length() > 0))
/*      */     {
/*      */       do
/*      */       {
/* 1413 */         uriIndex = this.m_prefixMappings.indexOf(uri, ++uriIndex);
/* 1414 */       }while ((uriIndex & 0x1) == 0);
/*      */       String prefix;
/* 1416 */       if (uriIndex >= 0)
/*      */       {
/* 1418 */         prefix = (String)this.m_prefixMappings.elementAt(uriIndex - 1);
/*      */       }
/*      */       else
/*      */       {
/*      */         String prefix;
/* 1420 */         if (null != qname)
/*      */         {
/* 1422 */           int indexOfNSSep = qname.indexOf(':');
/*      */           String prefix;
/* 1424 */           if (qname.equals("xmlns")) {
/* 1425 */             prefix = "";
/*      */           }
/*      */           else
/*      */           {
/*      */             String prefix;
/* 1426 */             if (qname.startsWith("xmlns:"))
/* 1427 */               prefix = qname.substring(indexOfNSSep + 1);
/*      */             else
/* 1429 */               prefix = indexOfNSSep > 0 ? qname.substring(0, indexOfNSSep) : null;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1434 */           prefix = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       String prefix;
/* 1437 */       if (null != qname)
/*      */       {
/* 1439 */         int indexOfNSSep = qname.indexOf(':');
/*      */         String prefix;
/* 1441 */         if (indexOfNSSep > 0)
/*      */         {
/*      */           String prefix;
/* 1443 */           if (qname.startsWith("xmlns:"))
/* 1444 */             prefix = qname.substring(indexOfNSSep + 1);
/*      */           else
/* 1446 */             prefix = qname.substring(0, indexOfNSSep);
/*      */         }
/*      */         else
/*      */         {
/*      */           String prefix;
/* 1450 */           if (qname.equals("xmlns"))
/* 1451 */             prefix = "";
/*      */           else
/* 1453 */             prefix = null;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1458 */         prefix = null;
/*      */       }
/*      */     }
/* 1461 */     return prefix;
/*      */   }
/*      */ 
/*      */   public int getIdForNamespace(String uri)
/*      */   {
/* 1475 */     return this.m_valuesOrPrefixes.stringToIndex(uri);
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String prefix)
/*      */   {
/* 1488 */     String uri = "";
/* 1489 */     int prefixIndex = this.m_contextIndexes.peek() - 1;
/*      */ 
/* 1491 */     if (null == prefix) {
/* 1492 */       prefix = "";
/*      */     }
/*      */     do
/*      */     {
/* 1496 */       prefixIndex = this.m_prefixMappings.indexOf(prefix, ++prefixIndex);
/* 1497 */     }while ((prefixIndex >= 0) && ((prefixIndex & 0x1) == 1));
/*      */ 
/* 1499 */     if (prefixIndex > -1)
/*      */     {
/* 1501 */       uri = (String)this.m_prefixMappings.elementAt(prefixIndex + 1);
/*      */     }
/*      */ 
/* 1505 */     return uri;
/*      */   }
/*      */ 
/*      */   public void setIDAttribute(String id, int elem)
/*      */   {
/* 1516 */     this.m_idAttributes.put(id, new Integer(elem));
/*      */   }
/*      */ 
/*      */   protected void charactersFlush()
/*      */   {
/* 1526 */     if (this.m_textPendingStart >= 0)
/*      */     {
/* 1528 */       int length = this.m_chars.size() - this.m_textPendingStart;
/* 1529 */       boolean doStrip = false;
/*      */ 
/* 1531 */       if (getShouldStripWhitespace())
/*      */       {
/* 1533 */         doStrip = this.m_chars.isWhitespace(this.m_textPendingStart, length);
/*      */       }
/*      */ 
/* 1536 */       if (doStrip) {
/* 1537 */         this.m_chars.setLength(this.m_textPendingStart);
/*      */       }
/* 1541 */       else if (length > 0) {
/* 1542 */         int exName = this.m_expandedNameTable.getExpandedTypeID(3);
/* 1543 */         int dataIndex = this.m_data.size();
/*      */ 
/* 1545 */         this.m_previous = addNode(this.m_coalescedTextType, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
/*      */ 
/* 1548 */         this.m_data.addElement(this.m_textPendingStart);
/* 1549 */         this.m_data.addElement(length);
/*      */       }
/*      */ 
/* 1554 */       this.m_textPendingStart = -1;
/* 1555 */       this.m_textType = (this.m_coalescedTextType = 3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputSource resolveEntity(String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/* 1587 */     return null;
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*      */     throws SAXException
/*      */   {
/* 1641 */     if (null == this.m_entities)
/*      */     {
/* 1643 */       this.m_entities = new Vector();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1648 */       systemId = SystemIDResolver.getAbsoluteURI(systemId, getDocumentBaseURI());
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1653 */       throw new SAXException(e);
/*      */     }
/*      */ 
/* 1657 */     this.m_entities.addElement(publicId);
/*      */ 
/* 1660 */     this.m_entities.addElement(systemId);
/*      */ 
/* 1663 */     this.m_entities.addElement(notationName);
/*      */ 
/* 1666 */     this.m_entities.addElement(name);
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/* 1686 */     this.m_locator = locator;
/* 1687 */     this.m_systemId = locator.getSystemId();
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/* 1703 */     int doc = addNode(9, this.m_expandedNameTable.getExpandedTypeID(9), -1, -1, 0, true);
/*      */ 
/* 1707 */     this.m_parents.push(doc);
/* 1708 */     this.m_previous = -1;
/*      */ 
/* 1710 */     this.m_contextIndexes.push(this.m_prefixMappings.size());
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/* 1725 */     charactersFlush();
/*      */ 
/* 1727 */     this.m_nextsib.setElementAt(-1, 0);
/*      */ 
/* 1729 */     if (this.m_firstch.elementAt(0) == -2) {
/* 1730 */       this.m_firstch.setElementAt(-1, 0);
/*      */     }
/* 1732 */     if (-1 != this.m_previous) {
/* 1733 */       this.m_nextsib.setElementAt(-1, this.m_previous);
/*      */     }
/* 1735 */     this.m_parents = null;
/* 1736 */     this.m_prefixMappings = null;
/* 1737 */     this.m_contextIndexes = null;
/*      */ 
/* 1739 */     this.m_endDocumentOccured = true;
/*      */ 
/* 1742 */     this.m_locator = null;
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/* 1766 */     if (null == prefix)
/* 1767 */       prefix = "";
/* 1768 */     this.m_prefixMappings.addElement(prefix);
/* 1769 */     this.m_prefixMappings.addElement(uri);
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix)
/*      */     throws SAXException
/*      */   {
/* 1789 */     if (null == prefix) {
/* 1790 */       prefix = "";
/*      */     }
/* 1792 */     int index = this.m_contextIndexes.peek() - 1;
/*      */     do
/*      */     {
/* 1796 */       index = this.m_prefixMappings.indexOf(prefix, ++index);
/* 1797 */     }while ((index >= 0) && ((index & 0x1) == 1));
/*      */ 
/* 1800 */     if (index > -1)
/*      */     {
/* 1802 */       this.m_prefixMappings.setElementAt("%@$#^@#", index);
/* 1803 */       this.m_prefixMappings.setElementAt("%@$#^@#", index + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean declAlreadyDeclared(String prefix)
/*      */   {
/* 1820 */     int startDecls = this.m_contextIndexes.peek();
/* 1821 */     Vector prefixMappings = this.m_prefixMappings;
/* 1822 */     int nDecls = prefixMappings.size();
/*      */ 
/* 1824 */     for (int i = startDecls; i < nDecls; i += 2)
/*      */     {
/* 1826 */       String prefixDecl = (String)prefixMappings.elementAt(i);
/*      */ 
/* 1828 */       if (prefixDecl != null)
/*      */       {
/* 1831 */         if (prefixDecl.equals(prefix))
/* 1832 */           return true;
/*      */       }
/*      */     }
/* 1835 */     return false;
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*      */     throws SAXException
/*      */   {
/* 1886 */     charactersFlush();
/*      */ 
/* 1888 */     int exName = this.m_expandedNameTable.getExpandedTypeID(uri, localName, 1);
/* 1889 */     String prefix = getPrefix(qName, uri);
/* 1890 */     int prefixIndex = null != prefix ? this.m_valuesOrPrefixes.stringToIndex(qName) : 0;
/*      */ 
/* 1893 */     int elemNode = addNode(1, exName, this.m_parents.peek(), this.m_previous, prefixIndex, true);
/*      */ 
/* 1896 */     if (this.m_indexing) {
/* 1897 */       indexNode(exName, elemNode);
/*      */     }
/*      */ 
/* 1900 */     this.m_parents.push(elemNode);
/*      */ 
/* 1902 */     int startDecls = this.m_contextIndexes.peek();
/* 1903 */     int nDecls = this.m_prefixMappings.size();
/* 1904 */     int prev = -1;
/*      */ 
/* 1906 */     if (!this.m_pastFirstElement)
/*      */     {
/* 1909 */       prefix = "xml";
/* 1910 */       String declURL = "http://www.w3.org/XML/1998/namespace";
/* 1911 */       exName = this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
/* 1912 */       int val = this.m_valuesOrPrefixes.stringToIndex(declURL);
/* 1913 */       prev = addNode(13, exName, elemNode, prev, val, false);
/*      */ 
/* 1915 */       this.m_pastFirstElement = true;
/*      */     }
/*      */ 
/* 1918 */     for (int i = startDecls; i < nDecls; i += 2)
/*      */     {
/* 1920 */       prefix = (String)this.m_prefixMappings.elementAt(i);
/*      */ 
/* 1922 */       if (prefix != null)
/*      */       {
/* 1925 */         String declURL = (String)this.m_prefixMappings.elementAt(i + 1);
/*      */ 
/* 1927 */         exName = this.m_expandedNameTable.getExpandedTypeID(null, prefix, 13);
/*      */ 
/* 1929 */         int val = this.m_valuesOrPrefixes.stringToIndex(declURL);
/*      */ 
/* 1931 */         prev = addNode(13, exName, elemNode, prev, val, false);
/*      */       }
/*      */     }
/*      */ 
/* 1935 */     int n = attributes.getLength();
/*      */ 
/* 1937 */     for (int i = 0; i < n; i++)
/*      */     {
/* 1939 */       String attrUri = attributes.getURI(i);
/* 1940 */       String attrQName = attributes.getQName(i);
/* 1941 */       String valString = attributes.getValue(i);
/*      */ 
/* 1943 */       prefix = getPrefix(attrQName, attrUri);
/*      */ 
/* 1947 */       String attrLocalName = attributes.getLocalName(i);
/*      */       int nodeType;
/*      */       int nodeType;
/* 1949 */       if ((null != attrQName) && ((attrQName.equals("xmlns")) || (attrQName.startsWith("xmlns:"))))
/*      */       {
/* 1953 */         if (declAlreadyDeclared(prefix)) {
/*      */           continue;
/*      */         }
/* 1956 */         nodeType = 13;
/*      */       }
/*      */       else
/*      */       {
/* 1960 */         nodeType = 2;
/*      */ 
/* 1962 */         if (attributes.getType(i).equalsIgnoreCase("ID")) {
/* 1963 */           setIDAttribute(valString, elemNode);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1968 */       if (null == valString) {
/* 1969 */         valString = "";
/*      */       }
/* 1971 */       int val = this.m_valuesOrPrefixes.stringToIndex(valString);
/*      */ 
/* 1974 */       if (null != prefix)
/*      */       {
/* 1977 */         prefixIndex = this.m_valuesOrPrefixes.stringToIndex(attrQName);
/*      */ 
/* 1979 */         int dataIndex = this.m_data.size();
/*      */ 
/* 1981 */         this.m_data.addElement(prefixIndex);
/* 1982 */         this.m_data.addElement(val);
/*      */ 
/* 1984 */         val = -dataIndex;
/*      */       }
/*      */ 
/* 1987 */       exName = this.m_expandedNameTable.getExpandedTypeID(attrUri, attrLocalName, nodeType);
/* 1988 */       prev = addNode(nodeType, exName, elemNode, prev, val, false);
/*      */     }
/*      */ 
/* 1992 */     if (-1 != prev) {
/* 1993 */       this.m_nextsib.setElementAt(-1, prev);
/*      */     }
/* 1995 */     if (null != this.m_wsfilter)
/*      */     {
/* 1997 */       short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(elemNode), this);
/* 1998 */       boolean shouldStrip = 2 == wsv ? true : 3 == wsv ? getShouldStripWhitespace() : false;
/*      */ 
/* 2002 */       pushShouldStripWhitespace(shouldStrip);
/*      */     }
/*      */ 
/* 2005 */     this.m_previous = -1;
/*      */ 
/* 2007 */     this.m_contextIndexes.push(this.m_prefixMappings.size());
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/* 2037 */     charactersFlush();
/*      */ 
/* 2041 */     this.m_contextIndexes.quickPop(1);
/*      */ 
/* 2044 */     int topContextIndex = this.m_contextIndexes.peek();
/* 2045 */     if (topContextIndex != this.m_prefixMappings.size()) {
/* 2046 */       this.m_prefixMappings.setSize(topContextIndex);
/*      */     }
/*      */ 
/* 2049 */     int lastNode = this.m_previous;
/*      */ 
/* 2051 */     this.m_previous = this.m_parents.pop();
/*      */ 
/* 2054 */     if (-1 == lastNode)
/* 2055 */       this.m_firstch.setElementAt(-1, this.m_previous);
/*      */     else {
/* 2057 */       this.m_nextsib.setElementAt(-1, lastNode);
/*      */     }
/* 2059 */     popShouldStripWhitespace();
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2080 */     if (this.m_textPendingStart == -1)
/*      */     {
/* 2082 */       this.m_textPendingStart = this.m_chars.size();
/* 2083 */       this.m_coalescedTextType = this.m_textType;
/*      */     }
/* 2089 */     else if (this.m_textType == 3)
/*      */     {
/* 2091 */       this.m_coalescedTextType = 3;
/*      */     }
/*      */ 
/* 2094 */     this.m_chars.append(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2119 */     characters(ch, start, length);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/* 2143 */     charactersFlush();
/*      */ 
/* 2145 */     int exName = this.m_expandedNameTable.getExpandedTypeID(null, target, 7);
/*      */ 
/* 2147 */     int dataIndex = this.m_valuesOrPrefixes.stringToIndex(data);
/*      */ 
/* 2149 */     this.m_previous = addNode(7, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void warning(SAXParseException e)
/*      */     throws SAXException
/*      */   {
/* 2196 */     System.err.println(e.getMessage());
/*      */   }
/*      */ 
/*      */   public void error(SAXParseException e)
/*      */     throws SAXException
/*      */   {
/* 2215 */     throw e;
/*      */   }
/*      */ 
/*      */   public void fatalError(SAXParseException e)
/*      */     throws SAXException
/*      */   {
/* 2237 */     throw e;
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String model)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String eName, String aName, String type, String valueDefault, String value)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, String value)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/* 2361 */     this.m_insideDTD = true;
/*      */   }
/*      */ 
/*      */   public void endDTD()
/*      */     throws SAXException
/*      */   {
/* 2373 */     this.m_insideDTD = false;
/*      */   }
/*      */ 
/*      */   public void startEntity(String name)
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
/* 2429 */     this.m_textType = 4;
/*      */   }
/*      */ 
/*      */   public void endCDATA()
/*      */     throws SAXException
/*      */   {
/* 2440 */     this.m_textType = 3;
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/* 2458 */     if (this.m_insideDTD) {
/* 2459 */       return;
/*      */     }
/* 2461 */     charactersFlush();
/*      */ 
/* 2463 */     int exName = this.m_expandedNameTable.getExpandedTypeID(8);
/*      */ 
/* 2467 */     int dataIndex = this.m_valuesOrPrefixes.stringToIndex(new String(ch, start, length));
/*      */ 
/* 2471 */     this.m_previous = addNode(8, exName, this.m_parents.peek(), this.m_previous, dataIndex, false);
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value)
/*      */   {
/*      */   }
/*      */ 
/*      */   public SourceLocator getSourceLocatorFor(int node)
/*      */   {
/* 2498 */     if (this.m_useSourceLocationProperty)
/*      */     {
/* 2501 */       node = makeNodeIdentity(node);
/*      */ 
/* 2504 */       return new NodeLocator(null, this.m_sourceSystemId.elementAt(node), this.m_sourceLine.elementAt(node), this.m_sourceColumn.elementAt(node));
/*      */     }
/*      */ 
/* 2509 */     if (this.m_locator != null)
/*      */     {
/* 2511 */       return new NodeLocator(null, this.m_locator.getSystemId(), -1, -1);
/*      */     }
/* 2513 */     if (this.m_systemId != null)
/*      */     {
/* 2515 */       return new NodeLocator(null, this.m_systemId, -1, -1);
/*      */     }
/* 2517 */     return null;
/*      */   }
/*      */ 
/*      */   public String getFixedNames(int type) {
/* 2521 */     return m_fixednames[type];
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM
 * JD-Core Version:    0.6.2
 */