/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMFilter;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLReaderManager;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class DTMManagerDefault extends DTMManager
/*     */ {
/*     */   private static final boolean DUMPTREE = false;
/*     */   private static final boolean DEBUG = false;
/*  97 */   protected DTM[] m_dtms = new DTM[256];
/*     */ 
/* 112 */   int[] m_dtm_offsets = new int[256];
/*     */ 
/* 118 */   protected XMLReaderManager m_readerManager = null;
/*     */ 
/* 123 */   protected DefaultHandler m_defaultHandler = new DefaultHandler();
/*     */ 
/* 203 */   private ExpandedNameTable m_expandedNameTable = new ExpandedNameTable();
/*     */ 
/*     */   public synchronized void addDTM(DTM dtm, int id)
/*     */   {
/* 133 */     addDTM(dtm, id, 0);
/*     */   }
/*     */ 
/*     */   public synchronized void addDTM(DTM dtm, int id, int offset)
/*     */   {
/* 148 */     if (id >= 65536)
/*     */     {
/* 151 */       throw new DTMException(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
/*     */     }
/*     */ 
/* 159 */     int oldlen = this.m_dtms.length;
/* 160 */     if (oldlen <= id)
/*     */     {
/* 167 */       int newlen = Math.min(id + 256, 65536);
/*     */ 
/* 169 */       DTM[] new_m_dtms = new DTM[newlen];
/* 170 */       System.arraycopy(this.m_dtms, 0, new_m_dtms, 0, oldlen);
/* 171 */       this.m_dtms = new_m_dtms;
/* 172 */       int[] new_m_dtm_offsets = new int[newlen];
/* 173 */       System.arraycopy(this.m_dtm_offsets, 0, new_m_dtm_offsets, 0, oldlen);
/* 174 */       this.m_dtm_offsets = new_m_dtm_offsets;
/*     */     }
/*     */ 
/* 177 */     this.m_dtms[id] = dtm;
/* 178 */     this.m_dtm_offsets[id] = offset;
/* 179 */     dtm.documentRegistration();
/*     */   }
/*     */ 
/*     */   public synchronized int getFirstFreeDTMID()
/*     */   {
/* 189 */     int n = this.m_dtms.length;
/* 190 */     for (int i = 1; i < n; i++)
/*     */     {
/* 192 */       if (null == this.m_dtms[i])
/*     */       {
/* 194 */         return i;
/*     */       }
/*     */     }
/* 197 */     return n;
/*     */   }
/*     */ 
/*     */   public synchronized DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing)
/*     */   {
/* 250 */     XMLStringFactory xstringFactory = this.m_xsf;
/* 251 */     int dtmPos = getFirstFreeDTMID();
/* 252 */     int documentID = dtmPos << 16;
/*     */ 
/* 254 */     if ((null != source) && ((source instanceof DOMSource)))
/*     */     {
/* 256 */       DOM2DTM dtm = new DOM2DTM(this, (DOMSource)source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
/*     */ 
/* 259 */       addDTM(dtm, dtmPos, 0);
/*     */ 
/* 266 */       return dtm;
/*     */     }
/*     */ 
/* 270 */     boolean isSAXSource = null != source ? source instanceof SAXSource : true;
/*     */ 
/* 272 */     boolean isStreamSource = null != source ? source instanceof StreamSource : false;
/*     */ 
/* 275 */     if ((isSAXSource) || (isStreamSource)) {
/* 276 */       XMLReader reader = null;
/*     */       try
/*     */       {
/*     */         InputSource xmlSource;
/*     */         InputSource xmlSource;
/* 282 */         if (null == source) {
/* 283 */           xmlSource = null;
/*     */         } else {
/* 285 */           reader = getXMLReader(source);
/* 286 */           xmlSource = SAXSource.sourceToInputSource(source);
/*     */ 
/* 288 */           String urlOfSource = xmlSource.getSystemId();
/*     */ 
/* 290 */           if (null != urlOfSource) {
/*     */             try {
/* 292 */               urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
/*     */             }
/*     */             catch (Exception e) {
/* 295 */               System.err.println("Can not absolutize URL: " + urlOfSource);
/*     */             }
/*     */ 
/* 298 */             xmlSource.setSystemId(urlOfSource);
/*     */           }
/*     */         }
/*     */         SAX2DTM dtm;
/*     */         SAX2DTM dtm;
/* 302 */         if ((source == null) && (unique) && (!incremental) && (!doIndexing))
/*     */         {
/* 310 */           dtm = new SAX2RTFDTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
/*     */         }
/*     */         else
/*     */         {
/* 322 */           dtm = new SAX2DTM(this, source, documentID, whiteSpaceFilter, xstringFactory, doIndexing);
/*     */         }
/*     */ 
/* 329 */         addDTM(dtm, dtmPos, 0);
/*     */ 
/* 332 */         boolean haveXercesParser = (null != reader) && (reader.getClass().getName().equals("com.sun.org.apache.xerces.internal.parsers.SAXParser"));
/*     */ 
/* 338 */         if (haveXercesParser)
/* 339 */           incremental = true;
/*     */         IncrementalSAXSource coParser;
/* 344 */         if ((this.m_incremental) && (incremental))
/*     */         {
/* 346 */           coParser = null;
/*     */ 
/* 348 */           if (haveXercesParser)
/*     */             try
/*     */             {
/* 351 */               coParser = (IncrementalSAXSource)Class.forName("com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Xerces").newInstance();
/*     */             }
/*     */             catch (Exception ex) {
/* 354 */               ex.printStackTrace();
/* 355 */               coParser = null;
/*     */             }
/*     */           IncrementalSAXSource_Filter filter;
/* 359 */           if (coParser == null)
/*     */           {
/* 361 */             if (null == reader) {
/* 362 */               coParser = new IncrementalSAXSource_Filter();
/*     */             } else {
/* 364 */               filter = new IncrementalSAXSource_Filter();
/*     */ 
/* 366 */               filter.setXMLReader(reader);
/* 367 */               coParser = filter;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 391 */           dtm.setIncrementalSAXSource(coParser);
/*     */ 
/* 393 */           if (null == xmlSource)
/*     */           {
/* 396 */             return dtm;
/*     */           }
/*     */ 
/* 399 */           if (null == reader.getErrorHandler()) {
/* 400 */             reader.setErrorHandler(dtm);
/*     */           }
/* 402 */           reader.setDTDHandler(dtm);
/*     */           try
/*     */           {
/* 408 */             coParser.startParse(xmlSource);
/*     */           }
/*     */           catch (RuntimeException re) {
/* 411 */             dtm.clearCoRoutine();
/*     */ 
/* 413 */             throw re;
/*     */           }
/*     */           catch (Exception e) {
/* 416 */             dtm.clearCoRoutine();
/*     */ 
/* 418 */             throw new WrappedRuntimeException(e);
/*     */           }
/*     */         } else {
/* 421 */           if (null == reader)
/*     */           {
/* 424 */             return dtm;
/*     */           }
/*     */ 
/* 428 */           reader.setContentHandler(dtm);
/* 429 */           reader.setDTDHandler(dtm);
/* 430 */           if (null == reader.getErrorHandler()) {
/* 431 */             reader.setErrorHandler(dtm);
/*     */           }
/*     */           try
/*     */           {
/* 435 */             reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
/*     */           }
/*     */           catch (SAXNotRecognizedException e) {
/*     */           }
/*     */           catch (SAXNotSupportedException e) {
/*     */           }
/*     */           try {
/* 442 */             reader.parse(xmlSource);
/*     */           } catch (RuntimeException re) {
/* 444 */             dtm.clearCoRoutine();
/*     */ 
/* 446 */             throw re;
/*     */           } catch (Exception e) {
/* 448 */             dtm.clearCoRoutine();
/*     */ 
/* 450 */             throw new WrappedRuntimeException(e);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 459 */         return dtm;
/*     */       }
/*     */       finally
/*     */       {
/* 463 */         if ((reader != null) && ((!this.m_incremental) || (!incremental))) {
/* 464 */           reader.setContentHandler(this.m_defaultHandler);
/* 465 */           reader.setDTDHandler(this.m_defaultHandler);
/* 466 */           reader.setErrorHandler(this.m_defaultHandler);
/*     */           try
/*     */           {
/* 470 */             reader.setProperty("http://xml.org/sax/properties/lexical-handler", null);
/*     */           } catch (Exception e) {
/*     */           }
/*     */         }
/* 474 */         releaseXMLReader(reader);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 480 */     throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[] { source }));
/*     */   }
/*     */ 
/*     */   public synchronized int getDTMHandleFromNode(Node node)
/*     */   {
/* 496 */     if (null == node) {
/* 497 */       throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_NODE_NON_NULL", null));
/*     */     }
/* 499 */     if ((node instanceof DTMNodeProxy)) {
/* 500 */       return ((DTMNodeProxy)node).getDTMNodeNumber();
/*     */     }
/*     */ 
/* 525 */     int max = this.m_dtms.length;
/* 526 */     for (int i = 0; i < max; i++)
/*     */     {
/* 528 */       DTM thisDTM = this.m_dtms[i];
/* 529 */       if ((null != thisDTM) && ((thisDTM instanceof DOM2DTM)))
/*     */       {
/* 531 */         int handle = ((DOM2DTM)thisDTM).getHandleOfNode(node);
/* 532 */         if (handle != -1) return handle;
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 555 */     Node root = node;
/* 556 */     for (Node p = root.getNodeType() == 2 ? ((Attr)root).getOwnerElement() : root.getParentNode(); 
/* 557 */       p != null; p = p.getParentNode())
/*     */     {
/* 559 */       root = p;
/*     */     }
/*     */ 
/* 562 */     DOM2DTM dtm = (DOM2DTM)getDTM(new DOMSource(root), false, null, true, true);
/*     */     int handle;
/* 567 */     if ((node instanceof DOM2DTMdefaultNamespaceDeclarationNode))
/*     */     {
/* 572 */       int handle = dtm.getHandleOfNode(((Attr)node).getOwnerElement());
/* 573 */       handle = dtm.getAttributeNode(handle, node.getNamespaceURI(), node.getLocalName());
/*     */     }
/*     */     else {
/* 576 */       handle = dtm.getHandleOfNode(node);
/*     */     }
/* 578 */     if (-1 == handle) {
/* 579 */       throw new RuntimeException(XMLMessages.createXMLMessage("ER_COULD_NOT_RESOLVE_NODE", null));
/*     */     }
/* 581 */     return handle;
/*     */   }
/*     */ 
/*     */   public synchronized XMLReader getXMLReader(Source inputSource)
/*     */   {
/*     */     try
/*     */     {
/* 604 */       XMLReader reader = (inputSource instanceof SAXSource) ? ((SAXSource)inputSource).getXMLReader() : null;
/*     */ 
/* 608 */       if (null == reader) {
/* 609 */         if (this.m_readerManager == null) {
/* 610 */           this.m_readerManager = XMLReaderManager.getInstance(super.useServicesMechnism());
/*     */         }
/*     */       }
/* 613 */       return this.m_readerManager.getXMLReader();
/*     */     }
/*     */     catch (SAXException se)
/*     */     {
/* 619 */       throw new DTMException(se.getMessage(), se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void releaseXMLReader(XMLReader reader)
/*     */   {
/* 634 */     if (this.m_readerManager != null)
/* 635 */       this.m_readerManager.releaseXMLReader(reader);
/*     */   }
/*     */ 
/*     */   public synchronized DTM getDTM(int nodeHandle)
/*     */   {
/*     */     try
/*     */     {
/* 651 */       return this.m_dtms[(nodeHandle >>> 16)];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e)
/*     */     {
/* 655 */       if (nodeHandle == -1) {
/* 656 */         return null;
/*     */       }
/* 658 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized int getDTMIdentity(DTM dtm)
/*     */   {
/* 676 */     if ((dtm instanceof DTMDefaultBase))
/*     */     {
/* 678 */       DTMDefaultBase dtmdb = (DTMDefaultBase)dtm;
/* 679 */       if (dtmdb.getManager() == this) {
/* 680 */         return dtmdb.getDTMIDs().elementAt(0);
/*     */       }
/* 682 */       return -1;
/*     */     }
/*     */ 
/* 685 */     int n = this.m_dtms.length;
/*     */ 
/* 687 */     for (int i = 0; i < n; i++)
/*     */     {
/* 689 */       DTM tdtm = this.m_dtms[i];
/*     */ 
/* 691 */       if ((tdtm == dtm) && (this.m_dtm_offsets[i] == 0)) {
/* 692 */         return i << 16;
/*     */       }
/*     */     }
/* 695 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized boolean release(DTM dtm, boolean shouldHardDelete)
/*     */   {
/* 726 */     if ((dtm instanceof SAX2DTM))
/*     */     {
/* 728 */       ((SAX2DTM)dtm).clearCoRoutine();
/*     */     }
/*     */ 
/* 739 */     if ((dtm instanceof DTMDefaultBase))
/*     */     {
/* 741 */       SuballocatedIntVector ids = ((DTMDefaultBase)dtm).getDTMIDs();
/* 742 */       for (int i = ids.size() - 1; i >= 0; i--)
/* 743 */         this.m_dtms[(ids.elementAt(i) >>> 16)] = null;
/*     */     }
/*     */     else
/*     */     {
/* 747 */       int i = getDTMIdentity(dtm);
/* 748 */       if (i >= 0)
/*     */       {
/* 750 */         this.m_dtms[(i >>> 16)] = null;
/*     */       }
/*     */     }
/*     */ 
/* 754 */     dtm.documentRelease();
/* 755 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized DTM createDocumentFragment()
/*     */   {
/*     */     try
/*     */     {
/* 769 */       DocumentBuilderFactory dbf = FactoryImpl.getDOMFactory(super.useServicesMechnism());
/* 770 */       dbf.setNamespaceAware(true);
/*     */ 
/* 772 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 773 */       Document doc = db.newDocument();
/* 774 */       Node df = doc.createDocumentFragment();
/*     */ 
/* 776 */       return getDTM(new DOMSource(df), true, null, false, false);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 780 */       throw new DTMException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion)
/*     */   {
/* 799 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver)
/*     */   {
/* 816 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator createDTMIterator(int node)
/*     */   {
/* 831 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator createDTMIterator(Object xpathCompiler, int pos)
/*     */   {
/* 847 */     return null;
/*     */   }
/*     */ 
/*     */   public ExpandedNameTable getExpandedNameTable(DTM dtm)
/*     */   {
/* 859 */     return this.m_expandedNameTable;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault
 * JD-Core Version:    0.6.2
 */