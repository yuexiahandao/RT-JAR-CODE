/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.DOM2SAX;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.StAXEvent2SAX;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class XSLTCDTMManager extends DTMManagerDefault
/*     */ {
/*     */   private static final String DEFAULT_CLASS_NAME = "com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager";
/*     */   private static final String DEFAULT_PROP_NAME = "com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager";
/*     */   private static final boolean DUMPTREE = false;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public static XSLTCDTMManager newInstance()
/*     */   {
/*  87 */     return new XSLTCDTMManager();
/*     */   }
/*     */ 
/*     */   public static Class getDTMManagerClass()
/*     */   {
/* 106 */     return getDTMManagerClass(true);
/*     */   }
/*     */ 
/*     */   public static Class getDTMManagerClass(boolean useServicesMechanism) {
/* 110 */     Class mgrClass = null;
/* 111 */     if (useServicesMechanism) {
/* 112 */       mgrClass = ObjectFactory.lookUpFactoryClass("com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager", null, "com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager");
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/* 117 */         mgrClass = ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager", true);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     return mgrClass != null ? mgrClass : XSLTCDTMManager.class;
/*     */   }
/*     */ 
/*     */   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing)
/*     */   {
/* 153 */     return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, true, false);
/*     */   }
/*     */ 
/*     */   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex)
/*     */   {
/* 183 */     return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, false);
/*     */   }
/*     */ 
/*     */   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex, boolean newNameTable)
/*     */   {
/* 216 */     return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, newNameTable);
/*     */   }
/*     */ 
/*     */   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex)
/*     */   {
/* 252 */     return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, hasUserReader, size, buildIdIndex, false);
/*     */   }
/*     */ 
/*     */   public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex, boolean newNameTable)
/*     */   {
/* 297 */     int dtmPos = getFirstFreeDTMID();
/* 298 */     int documentID = dtmPos << 16;
/*     */ 
/* 300 */     if ((null != source) && ((source instanceof StAXSource))) {
/* 301 */       StAXSource staxSource = (StAXSource)source;
/* 302 */       StAXEvent2SAX staxevent2sax = null;
/* 303 */       StAXStream2SAX staxStream2SAX = null;
/* 304 */       if (staxSource.getXMLEventReader() != null) {
/* 305 */         XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
/* 306 */         staxevent2sax = new StAXEvent2SAX(xmlEventReader);
/* 307 */       } else if (staxSource.getXMLStreamReader() != null) {
/* 308 */         XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
/* 309 */         staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
/*     */       }
/*     */       SAXImpl dtm;
/*     */       SAXImpl dtm;
/* 314 */       if (size <= 0) {
/* 315 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
/*     */       }
/*     */       else
/*     */       {
/* 320 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
/*     */       }
/*     */ 
/* 325 */       dtm.setDocumentURI(source.getSystemId());
/*     */ 
/* 327 */       addDTM(dtm, dtmPos, 0);
/*     */       try
/*     */       {
/* 330 */         if (staxevent2sax != null) {
/* 331 */           staxevent2sax.setContentHandler(dtm);
/* 332 */           staxevent2sax.parse();
/*     */         }
/* 334 */         else if (staxStream2SAX != null) {
/* 335 */           staxStream2SAX.setContentHandler(dtm);
/* 336 */           staxStream2SAX.parse();
/*     */         }
/*     */       }
/*     */       catch (RuntimeException re)
/*     */       {
/* 341 */         throw re;
/*     */       }
/*     */       catch (Exception e) {
/* 344 */         throw new WrappedRuntimeException(e);
/*     */       }
/*     */ 
/* 347 */       return dtm;
/* 348 */     }if ((null != source) && ((source instanceof DOMSource))) {
/* 349 */       DOMSource domsrc = (DOMSource)source;
/* 350 */       Node node = domsrc.getNode();
/* 351 */       DOM2SAX dom2sax = new DOM2SAX(node);
/*     */       SAXImpl dtm;
/*     */       SAXImpl dtm;
/* 355 */       if (size <= 0) {
/* 356 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
/*     */       }
/*     */       else
/*     */       {
/* 361 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
/*     */       }
/*     */ 
/* 366 */       dtm.setDocumentURI(source.getSystemId());
/*     */ 
/* 368 */       addDTM(dtm, dtmPos, 0);
/*     */ 
/* 370 */       dom2sax.setContentHandler(dtm);
/*     */       try
/*     */       {
/* 373 */         dom2sax.parse();
/*     */       }
/*     */       catch (RuntimeException re) {
/* 376 */         throw re;
/*     */       }
/*     */       catch (Exception e) {
/* 379 */         throw new WrappedRuntimeException(e);
/*     */       }
/*     */ 
/* 382 */       return dtm;
/*     */     }
/*     */ 
/* 386 */     boolean isSAXSource = null != source ? source instanceof SAXSource : true;
/*     */ 
/* 388 */     boolean isStreamSource = null != source ? source instanceof StreamSource : false;
/*     */ 
/* 391 */     if ((isSAXSource) || (isStreamSource))
/*     */     {
/*     */       XMLReader reader;
/*     */       InputSource xmlSource;
/* 395 */       if (null == source) {
/* 396 */         InputSource xmlSource = null;
/* 397 */         XMLReader reader = null;
/* 398 */         hasUserReader = false;
/*     */       }
/*     */       else {
/* 401 */         reader = getXMLReader(source);
/* 402 */         xmlSource = SAXSource.sourceToInputSource(source);
/*     */ 
/* 404 */         String urlOfSource = xmlSource.getSystemId();
/*     */ 
/* 406 */         if (null != urlOfSource) {
/*     */           try {
/* 408 */             urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 412 */             System.err.println("Can not absolutize URL: " + urlOfSource);
/*     */           }
/*     */ 
/* 415 */           xmlSource.setSystemId(urlOfSource);
/*     */         }
/*     */       }
/*     */       SAXImpl dtm;
/*     */       SAXImpl dtm;
/* 421 */       if (size <= 0) {
/* 422 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
/*     */       }
/*     */       else
/*     */       {
/* 427 */         dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
/*     */       }
/*     */ 
/* 434 */       addDTM(dtm, dtmPos, 0);
/*     */ 
/* 436 */       if (null == reader)
/*     */       {
/* 438 */         return dtm;
/*     */       }
/*     */ 
/* 441 */       reader.setContentHandler(dtm.getBuilder());
/*     */ 
/* 443 */       if ((!hasUserReader) || (null == reader.getDTDHandler())) {
/* 444 */         reader.setDTDHandler(dtm);
/*     */       }
/*     */ 
/* 447 */       if ((!hasUserReader) || (null == reader.getErrorHandler())) {
/* 448 */         reader.setErrorHandler(dtm);
/*     */       }
/*     */       try
/*     */       {
/* 452 */         reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
/*     */       } catch (SAXNotRecognizedException e) {
/*     */       }
/*     */       catch (SAXNotSupportedException e) {
/*     */       }
/*     */       try {
/* 458 */         reader.parse(xmlSource);
/*     */       }
/*     */       catch (RuntimeException re) {
/* 461 */         throw re;
/*     */       }
/*     */       catch (Exception e) {
/* 464 */         throw new WrappedRuntimeException(e);
/*     */       } finally {
/* 466 */         if (!hasUserReader) {
/* 467 */           releaseXMLReader(reader);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 476 */       return dtm;
/*     */     }
/*     */ 
/* 481 */     throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[] { source }));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager
 * JD-Core Version:    0.6.2
 */