/*      */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*      */ 
/*      */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*      */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.MessageHandler;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
/*      */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*      */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLReaderManager;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.net.UnknownServiceException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.stream.XMLEventReader;
/*      */ import javax.xml.stream.XMLEventWriter;
/*      */ import javax.xml.stream.XMLStreamReader;
/*      */ import javax.xml.stream.XMLStreamWriter;
/*      */ import javax.xml.transform.ErrorListener;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.URIResolver;
/*      */ import javax.xml.transform.dom.DOMResult;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.sax.SAXResult;
/*      */ import javax.xml.transform.sax.SAXSource;
/*      */ import javax.xml.transform.stax.StAXResult;
/*      */ import javax.xml.transform.stax.StAXSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public final class TransformerImpl extends Transformer
/*      */   implements DOMCache, ErrorListener
/*      */ {
/*      */   private static final String EMPTY_STRING = "";
/*      */   private static final String NO_STRING = "no";
/*      */   private static final String YES_STRING = "yes";
/*      */   private static final String XML_STRING = "xml";
/*      */   private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
/*      */   private static final String NAMESPACE_FEATURE = "http://xml.org/sax/features/namespaces";
/*      */   private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*  122 */   private AbstractTranslet _translet = null;
/*      */ 
/*  127 */   private String _method = null;
/*      */ 
/*  132 */   private String _encoding = null;
/*      */ 
/*  137 */   private String _sourceSystemId = null;
/*      */ 
/*  142 */   private ErrorListener _errorListener = this;
/*      */ 
/*  147 */   private URIResolver _uriResolver = null;
/*      */   private Properties _properties;
/*      */   private Properties _propertiesClone;
/*  157 */   private TransletOutputHandlerFactory _tohFactory = null;
/*      */ 
/*  162 */   private DOM _dom = null;
/*      */   private int _indentNumber;
/*  173 */   private TransformerFactoryImpl _tfactory = null;
/*      */ 
/*  178 */   private OutputStream _ostream = null;
/*      */ 
/*  184 */   private XSLTCDTMManager _dtmManager = null;
/*      */   private XMLReaderManager _readerManager;
/*  200 */   private boolean _isIdentity = false;
/*      */ 
/*  205 */   private boolean _isSecureProcessing = false;
/*      */   private boolean _useServicesMechanism;
/*  216 */   private String _accessExternalStylesheet = "all";
/*      */ 
/*  220 */   private String _accessExternalDTD = "all";
/*      */   private XMLSecurityManager _securityManager;
/*  228 */   private Hashtable _parameters = null;
/*      */ 
/*      */   protected TransformerImpl(Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
/*      */   {
/*  261 */     this(null, outputProperties, indentNumber, tfactory);
/*  262 */     this._isIdentity = true;
/*      */   }
/*      */ 
/*      */   protected TransformerImpl(Translet translet, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
/*      */   {
/*  269 */     this._translet = ((AbstractTranslet)translet);
/*  270 */     this._properties = createOutputProperties(outputProperties);
/*  271 */     this._propertiesClone = ((Properties)this._properties.clone());
/*  272 */     this._indentNumber = indentNumber;
/*  273 */     this._tfactory = tfactory;
/*  274 */     this._useServicesMechanism = this._tfactory.useServicesMechnism();
/*  275 */     this._accessExternalStylesheet = ((String)this._tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet"));
/*  276 */     this._accessExternalDTD = ((String)this._tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD"));
/*  277 */     this._securityManager = ((XMLSecurityManager)this._tfactory.getAttribute("http://apache.org/xml/properties/security-manager"));
/*  278 */     this._readerManager = XMLReaderManager.getInstance(this._useServicesMechanism);
/*  279 */     this._readerManager.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
/*  280 */     this._readerManager.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this._isSecureProcessing);
/*  281 */     this._readerManager.setProperty("http://apache.org/xml/properties/security-manager", this._securityManager);
/*      */   }
/*      */ 
/*      */   public boolean isSecureProcessing()
/*      */   {
/*  289 */     return this._isSecureProcessing;
/*      */   }
/*      */ 
/*      */   public void setSecureProcessing(boolean flag)
/*      */   {
/*  296 */     this._isSecureProcessing = flag;
/*  297 */     this._readerManager.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this._isSecureProcessing);
/*      */   }
/*      */ 
/*      */   public boolean useServicesMechnism()
/*      */   {
/*  303 */     return this._useServicesMechanism;
/*      */   }
/*      */ 
/*      */   public void setServicesMechnism(boolean flag)
/*      */   {
/*  310 */     this._useServicesMechanism = flag;
/*      */   }
/*      */ 
/*      */   protected AbstractTranslet getTranslet()
/*      */   {
/*  318 */     return this._translet;
/*      */   }
/*      */ 
/*      */   public boolean isIdentity() {
/*  322 */     return this._isIdentity;
/*      */   }
/*      */ 
/*      */   public void transform(Source source, Result result)
/*      */     throws TransformerException
/*      */   {
/*  335 */     if (!this._isIdentity) {
/*  336 */       if (this._translet == null) {
/*  337 */         ErrorMsg err = new ErrorMsg("JAXP_NO_TRANSLET_ERR");
/*  338 */         throw new TransformerException(err.toString());
/*      */       }
/*      */ 
/*  341 */       transferOutputProperties(this._translet);
/*      */     }
/*      */ 
/*  344 */     SerializationHandler toHandler = getOutputHandler(result);
/*  345 */     if (toHandler == null) {
/*  346 */       ErrorMsg err = new ErrorMsg("JAXP_NO_HANDLER_ERR");
/*  347 */       throw new TransformerException(err.toString());
/*      */     }
/*      */ 
/*  350 */     if ((this._uriResolver != null) && (!this._isIdentity)) {
/*  351 */       this._translet.setDOMCache(this);
/*      */     }
/*      */ 
/*  355 */     if (this._isIdentity) {
/*  356 */       transferOutputProperties(toHandler);
/*      */     }
/*      */ 
/*  359 */     transform(source, toHandler, this._encoding);
/*      */     try {
/*  361 */       if ((result instanceof DOMResult))
/*  362 */         ((DOMResult)result).setNode(this._tohFactory.getNode());
/*  363 */       else if ((result instanceof StAXResult)) {
/*  364 */         if (((StAXResult)result).getXMLEventWriter() != null)
/*      */         {
/*  366 */           this._tohFactory.getXMLEventWriter().flush();
/*      */         }
/*  368 */         else if (((StAXResult)result).getXMLStreamWriter() != null)
/*  369 */           this._tohFactory.getXMLStreamWriter().flush();
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  374 */       System.out.println("Result writing error");
/*      */     }
/*      */   }
/*      */ 
/*      */   public SerializationHandler getOutputHandler(Result result)
/*      */     throws TransformerException
/*      */   {
/*  387 */     this._method = ((String)this._properties.get("method"));
/*      */ 
/*  390 */     this._encoding = this._properties.getProperty("encoding");
/*      */ 
/*  392 */     this._tohFactory = TransletOutputHandlerFactory.newInstance(this._useServicesMechanism);
/*  393 */     this._tohFactory.setEncoding(this._encoding);
/*  394 */     if (this._method != null) {
/*  395 */       this._tohFactory.setOutputMethod(this._method);
/*      */     }
/*      */ 
/*  399 */     if (this._indentNumber >= 0) {
/*  400 */       this._tohFactory.setIndentNumber(this._indentNumber);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  406 */       if ((result instanceof SAXResult)) {
/*  407 */         SAXResult target = (SAXResult)result;
/*  408 */         ContentHandler handler = target.getHandler();
/*      */ 
/*  410 */         this._tohFactory.setHandler(handler);
/*      */ 
/*  417 */         LexicalHandler lexicalHandler = target.getLexicalHandler();
/*      */ 
/*  419 */         if (lexicalHandler != null) {
/*  420 */           this._tohFactory.setLexicalHandler(lexicalHandler);
/*      */         }
/*      */ 
/*  423 */         this._tohFactory.setOutputType(1);
/*  424 */         return this._tohFactory.getSerializationHandler();
/*      */       }
/*  426 */       if ((result instanceof StAXResult)) {
/*  427 */         if (((StAXResult)result).getXMLEventWriter() != null)
/*  428 */           this._tohFactory.setXMLEventWriter(((StAXResult)result).getXMLEventWriter());
/*  429 */         else if (((StAXResult)result).getXMLStreamWriter() != null)
/*  430 */           this._tohFactory.setXMLStreamWriter(((StAXResult)result).getXMLStreamWriter());
/*  431 */         this._tohFactory.setOutputType(3);
/*  432 */         return this._tohFactory.getSerializationHandler();
/*      */       }
/*  434 */       if ((result instanceof DOMResult)) {
/*  435 */         this._tohFactory.setNode(((DOMResult)result).getNode());
/*  436 */         this._tohFactory.setNextSibling(((DOMResult)result).getNextSibling());
/*  437 */         this._tohFactory.setOutputType(2);
/*  438 */         return this._tohFactory.getSerializationHandler();
/*      */       }
/*  440 */       if ((result instanceof StreamResult))
/*      */       {
/*  442 */         StreamResult target = (StreamResult)result;
/*      */ 
/*  448 */         this._tohFactory.setOutputType(0);
/*      */ 
/*  451 */         Writer writer = target.getWriter();
/*  452 */         if (writer != null) {
/*  453 */           this._tohFactory.setWriter(writer);
/*  454 */           return this._tohFactory.getSerializationHandler();
/*      */         }
/*      */ 
/*  458 */         OutputStream ostream = target.getOutputStream();
/*  459 */         if (ostream != null) {
/*  460 */           this._tohFactory.setOutputStream(ostream);
/*  461 */           return this._tohFactory.getSerializationHandler();
/*      */         }
/*      */ 
/*  465 */         String systemId = result.getSystemId();
/*  466 */         if (systemId == null) {
/*  467 */           ErrorMsg err = new ErrorMsg("JAXP_NO_RESULT_ERR");
/*  468 */           throw new TransformerException(err.toString());
/*      */         }
/*      */ 
/*  474 */         URL url = null;
/*  475 */         if (systemId.startsWith("file:"))
/*      */         {
/*      */           try
/*      */           {
/*  480 */             URI uri = new URI(systemId);
/*  481 */             systemId = "file:";
/*      */ 
/*  483 */             String host = uri.getHost();
/*  484 */             String path = uri.getPath();
/*  485 */             if (path == null) {
/*  486 */               path = "";
/*      */             }
/*      */ 
/*  491 */             if (host != null)
/*  492 */               systemId = systemId + "//" + host + path;
/*      */             else {
/*  494 */               systemId = systemId + "//" + path;
/*      */             }
/*      */           }
/*      */           catch (Exception exception)
/*      */           {
/*      */           }
/*      */ 
/*  501 */           url = new URL(systemId);
/*  502 */           this._ostream = new FileOutputStream(url.getFile());
/*  503 */           this._tohFactory.setOutputStream(this._ostream);
/*  504 */           return this._tohFactory.getSerializationHandler();
/*      */         }
/*  506 */         if (systemId.startsWith("http:")) {
/*  507 */           url = new URL(systemId);
/*  508 */           URLConnection connection = url.openConnection();
/*  509 */           this._tohFactory.setOutputStream(this._ostream = connection.getOutputStream());
/*  510 */           return this._tohFactory.getSerializationHandler();
/*      */         }
/*      */ 
/*  514 */         this._tohFactory.setOutputStream(this._ostream = new FileOutputStream(new File(systemId)));
/*      */ 
/*  516 */         return this._tohFactory.getSerializationHandler();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (UnknownServiceException e)
/*      */     {
/*  522 */       throw new TransformerException(e);
/*      */     }
/*      */     catch (ParserConfigurationException e) {
/*  525 */       throw new TransformerException(e);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  529 */       throw new TransformerException(e);
/*      */     }
/*  531 */     return null;
/*      */   }
/*      */ 
/*      */   protected void setDOM(DOM dom)
/*      */   {
/*  538 */     this._dom = dom;
/*      */   }
/*      */ 
/*      */   private DOM getDOM(Source source)
/*      */     throws TransformerException
/*      */   {
/*      */     try
/*      */     {
/*  546 */       DOM dom = null;
/*      */ 
/*  548 */       if (source != null)
/*      */       {
/*      */         DTMWSFilter wsfilter;
/*      */         DTMWSFilter wsfilter;
/*  550 */         if ((this._translet != null) && ((this._translet instanceof StripFilter)))
/*  551 */           wsfilter = new DOMWSFilter(this._translet);
/*      */         else {
/*  553 */           wsfilter = null;
/*      */         }
/*      */ 
/*  556 */         boolean hasIdCall = this._translet != null ? this._translet.hasIdCall() : false;
/*      */ 
/*  559 */         if (this._dtmManager == null) {
/*  560 */           this._dtmManager = ((XSLTCDTMManager)this._tfactory.getDTMManagerClass().newInstance());
/*      */ 
/*  563 */           this._dtmManager.setServicesMechnism(this._useServicesMechanism);
/*      */         }
/*  565 */         dom = (DOM)this._dtmManager.getDTM(source, false, wsfilter, true, false, false, 0, hasIdCall);
/*      */       }
/*  567 */       else if (this._dom != null) {
/*  568 */         dom = this._dom;
/*  569 */         this._dom = null;
/*      */       } else {
/*  571 */         return null;
/*      */       }
/*      */ 
/*  574 */       if (!this._isIdentity)
/*      */       {
/*  577 */         this._translet.prepassDocument(dom);
/*      */       }
/*      */ 
/*  580 */       return dom;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  584 */       if (this._errorListener != null) {
/*  585 */         postErrorToListener(e.getMessage());
/*      */       }
/*  587 */       throw new TransformerException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected TransformerFactoryImpl getTransformerFactory()
/*      */   {
/*  596 */     return this._tfactory;
/*      */   }
/*      */ 
/*      */   protected TransletOutputHandlerFactory getTransletOutputHandlerFactory()
/*      */   {
/*  604 */     return this._tohFactory;
/*      */   }
/*      */ 
/*      */   private void transformIdentity(Source source, SerializationHandler handler)
/*      */     throws Exception
/*      */   {
/*  611 */     if (source != null) {
/*  612 */       this._sourceSystemId = source.getSystemId();
/*      */     }
/*      */ 
/*  615 */     if ((source instanceof StreamSource)) {
/*  616 */       StreamSource stream = (StreamSource)source;
/*  617 */       InputStream streamInput = stream.getInputStream();
/*  618 */       Reader streamReader = stream.getReader();
/*  619 */       XMLReader reader = this._readerManager.getXMLReader();
/*      */       try
/*      */       {
/*      */         try
/*      */         {
/*  624 */           reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
/*  625 */           reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*      */         }
/*      */         catch (SAXException e) {
/*      */         }
/*  629 */         reader.setContentHandler(handler);
/*      */ 
/*  633 */         if (streamInput != null) {
/*  634 */           InputSource input = new InputSource(streamInput);
/*  635 */           input.setSystemId(this._sourceSystemId);
/*      */         }
/*  637 */         else if (streamReader != null) {
/*  638 */           InputSource input = new InputSource(streamReader);
/*  639 */           input.setSystemId(this._sourceSystemId);
/*      */         }
/*      */         else
/*      */         {
/*      */           InputSource input;
/*  641 */           if (this._sourceSystemId != null) {
/*  642 */             input = new InputSource(this._sourceSystemId);
/*      */           }
/*      */           else {
/*  645 */             ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
/*  646 */             throw new TransformerException(err.toString());
/*      */           }
/*      */         }
/*      */         InputSource input;
/*  650 */         reader.parse(input);
/*      */       } finally {
/*  652 */         this._readerManager.releaseXMLReader(reader);
/*      */       }
/*  654 */     } else if ((source instanceof SAXSource)) {
/*  655 */       SAXSource sax = (SAXSource)source;
/*  656 */       XMLReader reader = sax.getXMLReader();
/*  657 */       InputSource input = sax.getInputSource();
/*  658 */       boolean userReader = true;
/*      */       try
/*      */       {
/*  662 */         if (reader == null) {
/*  663 */           reader = this._readerManager.getXMLReader();
/*  664 */           userReader = false;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  669 */           reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
/*  670 */           reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*      */         }
/*      */         catch (SAXException e) {
/*      */         }
/*  674 */         reader.setContentHandler(handler);
/*      */ 
/*  677 */         reader.parse(input);
/*      */       } finally {
/*  679 */         if (!userReader)
/*  680 */           this._readerManager.releaseXMLReader(reader);
/*      */       }
/*      */     }
/*  683 */     else if ((source instanceof StAXSource)) {
/*  684 */       StAXSource staxSource = (StAXSource)source;
/*  685 */       StAXEvent2SAX staxevent2sax = null;
/*  686 */       StAXStream2SAX staxStream2SAX = null;
/*  687 */       if (staxSource.getXMLEventReader() != null) {
/*  688 */         XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
/*  689 */         staxevent2sax = new StAXEvent2SAX(xmlEventReader);
/*  690 */         staxevent2sax.setContentHandler(handler);
/*  691 */         staxevent2sax.parse();
/*  692 */         handler.flushPending();
/*  693 */       } else if (staxSource.getXMLStreamReader() != null) {
/*  694 */         XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
/*  695 */         staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
/*  696 */         staxStream2SAX.setContentHandler(handler);
/*  697 */         staxStream2SAX.parse();
/*  698 */         handler.flushPending();
/*      */       }
/*  700 */     } else if ((source instanceof DOMSource)) {
/*  701 */       DOMSource domsrc = (DOMSource)source;
/*  702 */       new DOM2TO(domsrc.getNode(), handler).parse();
/*  703 */     } else if ((source instanceof XSLTCSource)) {
/*  704 */       DOM dom = ((XSLTCSource)source).getDOM(null, this._translet);
/*  705 */       ((SAXImpl)dom).copy(handler);
/*      */     } else {
/*  707 */       ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR");
/*  708 */       throw new TransformerException(err.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void transform(Source source, SerializationHandler handler, String encoding)
/*      */     throws TransformerException
/*      */   {
/*      */     try
/*      */     {
/*  727 */       if ((((source instanceof StreamSource)) && (source.getSystemId() == null) && (((StreamSource)source).getInputStream() == null) && (((StreamSource)source).getReader() == null)) || (((source instanceof SAXSource)) && (((SAXSource)source).getInputSource() == null) && (((SAXSource)source).getXMLReader() == null)) || (((source instanceof DOMSource)) && (((DOMSource)source).getNode() == null)))
/*      */       {
/*  735 */         DocumentBuilderFactory builderF = FactoryImpl.getDOMFactory(this._useServicesMechanism);
/*  736 */         DocumentBuilder builder = builderF.newDocumentBuilder();
/*  737 */         String systemID = source.getSystemId();
/*  738 */         source = new DOMSource(builder.newDocument());
/*      */ 
/*  741 */         if (systemID != null) {
/*  742 */           source.setSystemId(systemID);
/*      */         }
/*      */       }
/*  745 */       if (this._isIdentity)
/*  746 */         transformIdentity(source, handler);
/*      */       else
/*  748 */         this._translet.transform(getDOM(source), handler);
/*      */     }
/*      */     catch (TransletException e) {
/*  751 */       if (this._errorListener != null) postErrorToListener(e.getMessage());
/*  752 */       throw new TransformerException(e);
/*      */     } catch (RuntimeException e) {
/*  754 */       if (this._errorListener != null) postErrorToListener(e.getMessage());
/*  755 */       throw new TransformerException(e);
/*      */     } catch (Exception e) {
/*  757 */       if (this._errorListener != null) postErrorToListener(e.getMessage());
/*  758 */       throw new TransformerException(e);
/*      */     } finally {
/*  760 */       this._dtmManager = null;
/*      */     }
/*      */ 
/*  764 */     if (this._ostream != null) {
/*      */       try {
/*  766 */         this._ostream.close();
/*      */       } catch (IOException e) {
/*      */       }
/*  769 */       this._ostream = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ErrorListener getErrorListener()
/*      */   {
/*  780 */     return this._errorListener;
/*      */   }
/*      */ 
/*      */   public void setErrorListener(ErrorListener listener)
/*      */     throws IllegalArgumentException
/*      */   {
/*  794 */     if (listener == null) {
/*  795 */       ErrorMsg err = new ErrorMsg("ERROR_LISTENER_NULL_ERR", "Transformer");
/*      */ 
/*  797 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*  799 */     this._errorListener = listener;
/*      */ 
/*  802 */     if (this._translet != null)
/*  803 */       this._translet.setMessageHandler(new MessageHandler(this._errorListener));
/*      */   }
/*      */ 
/*      */   private void postErrorToListener(String message)
/*      */   {
/*      */     try
/*      */     {
/*  811 */       this._errorListener.error(new TransformerException(message));
/*      */     }
/*      */     catch (TransformerException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void postWarningToListener(String message)
/*      */   {
/*      */     try
/*      */     {
/*  823 */       this._errorListener.warning(new TransformerException(message));
/*      */     }
/*      */     catch (TransformerException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private String makeCDATAString(Hashtable cdata)
/*      */   {
/*  837 */     if (cdata == null) return null;
/*      */ 
/*  839 */     StringBuffer result = new StringBuffer();
/*      */ 
/*  842 */     Enumeration elements = cdata.keys();
/*  843 */     if (elements.hasMoreElements()) {
/*  844 */       result.append((String)elements.nextElement());
/*  845 */       while (elements.hasMoreElements()) {
/*  846 */         String element = (String)elements.nextElement();
/*  847 */         result.append(' ');
/*  848 */         result.append(element);
/*      */       }
/*      */     }
/*      */ 
/*  852 */     return result.toString();
/*      */   }
/*      */ 
/*      */   public Properties getOutputProperties()
/*      */   {
/*  867 */     return (Properties)this._properties.clone();
/*      */   }
/*      */ 
/*      */   public String getOutputProperty(String name)
/*      */     throws IllegalArgumentException
/*      */   {
/*  882 */     if (!validOutputProperty(name)) {
/*  883 */       ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
/*  884 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*  886 */     return this._properties.getProperty(name);
/*      */   }
/*      */ 
/*      */   public void setOutputProperties(Properties properties)
/*      */     throws IllegalArgumentException
/*      */   {
/*  901 */     if (properties != null) {
/*  902 */       Enumeration names = properties.propertyNames();
/*      */ 
/*  904 */       while (names.hasMoreElements()) {
/*  905 */         String name = (String)names.nextElement();
/*      */ 
/*  908 */         if (!isDefaultProperty(name, properties))
/*      */         {
/*  910 */           if (validOutputProperty(name)) {
/*  911 */             this._properties.setProperty(name, properties.getProperty(name));
/*      */           }
/*      */           else {
/*  914 */             ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
/*  915 */             throw new IllegalArgumentException(err.toString());
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  920 */       this._properties = this._propertiesClone;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOutputProperty(String name, String value)
/*      */     throws IllegalArgumentException
/*      */   {
/*  937 */     if (!validOutputProperty(name)) {
/*  938 */       ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_PROP_ERR", name);
/*  939 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*  941 */     this._properties.setProperty(name, value);
/*      */   }
/*      */ 
/*      */   private void transferOutputProperties(AbstractTranslet translet)
/*      */   {
/*  951 */     if (this._properties == null) return;
/*      */ 
/*  954 */     Enumeration names = this._properties.propertyNames();
/*  955 */     while (names.hasMoreElements())
/*      */     {
/*  957 */       String name = (String)names.nextElement();
/*  958 */       String value = (String)this._properties.get(name);
/*      */ 
/*  961 */       if (value != null)
/*      */       {
/*  964 */         if (name.equals("encoding")) {
/*  965 */           translet._encoding = value;
/*      */         }
/*  967 */         else if (name.equals("method")) {
/*  968 */           translet._method = value;
/*      */         }
/*  970 */         else if (name.equals("doctype-public")) {
/*  971 */           translet._doctypePublic = value;
/*      */         }
/*  973 */         else if (name.equals("doctype-system")) {
/*  974 */           translet._doctypeSystem = value;
/*      */         }
/*  976 */         else if (name.equals("media-type")) {
/*  977 */           translet._mediaType = value;
/*      */         }
/*  979 */         else if (name.equals("standalone")) {
/*  980 */           translet._standalone = value;
/*      */         }
/*  982 */         else if (name.equals("version")) {
/*  983 */           translet._version = value;
/*      */         }
/*  985 */         else if (name.equals("omit-xml-declaration")) {
/*  986 */           translet._omitHeader = ((value != null) && (value.toLowerCase().equals("yes")));
/*      */         }
/*  989 */         else if (name.equals("indent")) {
/*  990 */           translet._indent = ((value != null) && (value.toLowerCase().equals("yes")));
/*      */         }
/*  993 */         else if (name.equals("{http://xml.apache.org/xslt}indent-amount")) {
/*  994 */           if (value != null) {
/*  995 */             translet._indentamount = Integer.parseInt(value);
/*      */           }
/*      */         }
/*  998 */         else if (name.equals("{http://xml.apache.org/xalan}indent-amount")) {
/*  999 */           if (value != null) {
/* 1000 */             translet._indentamount = Integer.parseInt(value);
/*      */           }
/*      */         }
/* 1003 */         else if (name.equals("cdata-section-elements")) {
/* 1004 */           if (value != null) {
/* 1005 */             translet._cdata = null;
/* 1006 */             StringTokenizer e = new StringTokenizer(value);
/* 1007 */             while (e.hasMoreTokens()) {
/* 1008 */               translet.addCdataElement(e.nextToken());
/*      */             }
/*      */           }
/*      */         }
/* 1012 */         else if ((name.equals("http://www.oracle.com/xml/is-standalone")) && 
/* 1013 */           (value != null) && (value.equals("yes")))
/* 1014 */           translet._isStandalone = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void transferOutputProperties(SerializationHandler handler)
/*      */   {
/* 1027 */     if (this._properties == null) return;
/*      */ 
/* 1029 */     String doctypePublic = null;
/* 1030 */     String doctypeSystem = null;
/*      */ 
/* 1033 */     Enumeration names = this._properties.propertyNames();
/* 1034 */     while (names.hasMoreElements())
/*      */     {
/* 1036 */       String name = (String)names.nextElement();
/* 1037 */       String value = (String)this._properties.get(name);
/*      */ 
/* 1040 */       if (value != null)
/*      */       {
/* 1043 */         if (name.equals("doctype-public")) {
/* 1044 */           doctypePublic = value;
/*      */         }
/* 1046 */         else if (name.equals("doctype-system")) {
/* 1047 */           doctypeSystem = value;
/*      */         }
/* 1049 */         else if (name.equals("media-type")) {
/* 1050 */           handler.setMediaType(value);
/*      */         }
/* 1052 */         else if (name.equals("standalone")) {
/* 1053 */           handler.setStandalone(value);
/*      */         }
/* 1055 */         else if (name.equals("version")) {
/* 1056 */           handler.setVersion(value);
/*      */         }
/* 1058 */         else if (name.equals("omit-xml-declaration")) {
/* 1059 */           handler.setOmitXMLDeclaration((value != null) && (value.toLowerCase().equals("yes")));
/*      */         }
/* 1062 */         else if (name.equals("indent")) {
/* 1063 */           handler.setIndent((value != null) && (value.toLowerCase().equals("yes")));
/*      */         }
/* 1066 */         else if (name.equals("{http://xml.apache.org/xslt}indent-amount")) {
/* 1067 */           if (value != null) {
/* 1068 */             handler.setIndentAmount(Integer.parseInt(value));
/*      */           }
/*      */         }
/* 1071 */         else if (name.equals("{http://xml.apache.org/xalan}indent-amount")) {
/* 1072 */           if (value != null) {
/* 1073 */             handler.setIndentAmount(Integer.parseInt(value));
/*      */           }
/*      */         }
/* 1076 */         else if (name.equals("http://www.oracle.com/xml/is-standalone")) {
/* 1077 */           if ((value != null) && (value.equals("yes"))) {
/* 1078 */             handler.setIsStandalone(true);
/*      */           }
/*      */         }
/* 1081 */         else if ((name.equals("cdata-section-elements")) && 
/* 1082 */           (value != null)) {
/* 1083 */           StringTokenizer e = new StringTokenizer(value);
/* 1084 */           Vector uriAndLocalNames = null;
/* 1085 */           while (e.hasMoreTokens()) {
/* 1086 */             String token = e.nextToken();
/*      */ 
/* 1090 */             int lastcolon = token.lastIndexOf(':');
/*      */             String localName;
/*      */             String uri;
/*      */             String localName;
/* 1093 */             if (lastcolon > 0) {
/* 1094 */               String uri = token.substring(0, lastcolon);
/* 1095 */               localName = token.substring(lastcolon + 1);
/*      */             }
/*      */             else
/*      */             {
/* 1099 */               uri = null;
/* 1100 */               localName = token;
/*      */             }
/*      */ 
/* 1103 */             if (uriAndLocalNames == null) {
/* 1104 */               uriAndLocalNames = new Vector();
/*      */             }
/*      */ 
/* 1107 */             uriAndLocalNames.addElement(uri);
/* 1108 */             uriAndLocalNames.addElement(localName);
/*      */           }
/* 1110 */           handler.setCdataSectionElements(uriAndLocalNames);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1116 */     if ((doctypePublic != null) || (doctypeSystem != null))
/* 1117 */       handler.setDoctype(doctypeSystem, doctypePublic);
/*      */   }
/*      */ 
/*      */   private Properties createOutputProperties(Properties outputProperties)
/*      */   {
/* 1128 */     Properties defaults = new Properties();
/* 1129 */     setDefaults(defaults, "xml");
/*      */ 
/* 1132 */     Properties base = new Properties(defaults);
/* 1133 */     if (outputProperties != null) {
/* 1134 */       Enumeration names = outputProperties.propertyNames();
/* 1135 */       while (names.hasMoreElements()) {
/* 1136 */         String name = (String)names.nextElement();
/* 1137 */         base.setProperty(name, outputProperties.getProperty(name));
/*      */       }
/*      */     }
/*      */     else {
/* 1141 */       base.setProperty("encoding", this._translet._encoding);
/* 1142 */       if (this._translet._method != null) {
/* 1143 */         base.setProperty("method", this._translet._method);
/*      */       }
/*      */     }
/*      */ 
/* 1147 */     String method = base.getProperty("method");
/* 1148 */     if (method != null) {
/* 1149 */       if (method.equals("html")) {
/* 1150 */         setDefaults(defaults, "html");
/*      */       }
/* 1152 */       else if (method.equals("text")) {
/* 1153 */         setDefaults(defaults, "text");
/*      */       }
/*      */     }
/*      */ 
/* 1157 */     return base;
/*      */   }
/*      */ 
/*      */   private void setDefaults(Properties props, String method)
/*      */   {
/* 1168 */     Properties method_props = OutputPropertiesFactory.getDefaultMethodProperties(method);
/*      */ 
/* 1171 */     Enumeration names = method_props.propertyNames();
/* 1172 */     while (names.hasMoreElements())
/*      */     {
/* 1174 */       String name = (String)names.nextElement();
/* 1175 */       props.setProperty(name, method_props.getProperty(name));
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean validOutputProperty(String name)
/*      */   {
/* 1184 */     return (name.equals("encoding")) || (name.equals("method")) || (name.equals("indent")) || (name.equals("doctype-public")) || (name.equals("doctype-system")) || (name.equals("cdata-section-elements")) || (name.equals("media-type")) || (name.equals("omit-xml-declaration")) || (name.equals("standalone")) || (name.equals("version")) || (name.equals("http://www.oracle.com/xml/is-standalone")) || (name.charAt(0) == '{');
/*      */   }
/*      */ 
/*      */   private boolean isDefaultProperty(String name, Properties properties)
/*      */   {
/* 1202 */     return properties.get(name) == null;
/*      */   }
/*      */ 
/*      */   public void setParameter(String name, Object value)
/*      */   {
/* 1216 */     if (value == null) {
/* 1217 */       ErrorMsg err = new ErrorMsg("JAXP_INVALID_SET_PARAM_VALUE", name);
/* 1218 */       throw new IllegalArgumentException(err.toString());
/*      */     }
/*      */ 
/* 1221 */     if (this._isIdentity) {
/* 1222 */       if (this._parameters == null) {
/* 1223 */         this._parameters = new Hashtable();
/*      */       }
/* 1225 */       this._parameters.put(name, value);
/*      */     }
/*      */     else {
/* 1228 */       this._translet.addParameter(name, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearParameters()
/*      */   {
/* 1238 */     if ((this._isIdentity) && (this._parameters != null)) {
/* 1239 */       this._parameters.clear();
/*      */     }
/*      */     else
/* 1242 */       this._translet.clearParameters();
/*      */   }
/*      */ 
/*      */   public final Object getParameter(String name)
/*      */   {
/* 1255 */     if (this._isIdentity) {
/* 1256 */       return this._parameters != null ? this._parameters.get(name) : null;
/*      */     }
/*      */ 
/* 1259 */     return this._translet.getParameter(name);
/*      */   }
/*      */ 
/*      */   public URIResolver getURIResolver()
/*      */   {
/* 1270 */     return this._uriResolver;
/*      */   }
/*      */ 
/*      */   public void setURIResolver(URIResolver resolver)
/*      */   {
/* 1280 */     this._uriResolver = resolver;
/*      */   }
/*      */ 
/*      */   public DOM retrieveDocument(String baseURI, String href, Translet translet)
/*      */   {
/*      */     try
/*      */     {
/* 1300 */       if (href.length() == 0) {
/* 1301 */         href = baseURI;
/*      */       }
/*      */ 
/* 1312 */       Source resolvedSource = this._uriResolver.resolve(href, baseURI);
/* 1313 */       if (resolvedSource == null) {
/* 1314 */         StreamSource streamSource = new StreamSource(SystemIDResolver.getAbsoluteURI(href, baseURI));
/*      */ 
/* 1316 */         return getDOM(streamSource);
/*      */       }
/*      */ 
/* 1319 */       return getDOM(resolvedSource);
/*      */     }
/*      */     catch (TransformerException e) {
/* 1322 */       if (this._errorListener != null)
/* 1323 */         postErrorToListener("File not found: " + e.getMessage()); 
/*      */     }
/* 1324 */     return null;
/*      */   }
/*      */ 
/*      */   public void error(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1342 */     Throwable wrapped = e.getException();
/* 1343 */     if (wrapped != null) {
/* 1344 */       System.err.println(new ErrorMsg("ERROR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1348 */       System.err.println(new ErrorMsg("ERROR_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */ 
/* 1351 */     throw e;
/*      */   }
/*      */ 
/*      */   public void fatalError(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1370 */     Throwable wrapped = e.getException();
/* 1371 */     if (wrapped != null) {
/* 1372 */       System.err.println(new ErrorMsg("FATAL_ERR_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1376 */       System.err.println(new ErrorMsg("FATAL_ERR_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */ 
/* 1379 */     throw e;
/*      */   }
/*      */ 
/*      */   public void warning(TransformerException e)
/*      */     throws TransformerException
/*      */   {
/* 1398 */     Throwable wrapped = e.getException();
/* 1399 */     if (wrapped != null) {
/* 1400 */       System.err.println(new ErrorMsg("WARNING_PLUS_WRAPPED_MSG", e.getMessageAndLocation(), wrapped.getMessage()));
/*      */     }
/*      */     else
/*      */     {
/* 1404 */       System.err.println(new ErrorMsg("WARNING_MSG", e.getMessageAndLocation()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 1417 */     this._method = null;
/* 1418 */     this._encoding = null;
/* 1419 */     this._sourceSystemId = null;
/* 1420 */     this._errorListener = this;
/* 1421 */     this._uriResolver = null;
/* 1422 */     this._dom = null;
/* 1423 */     this._parameters = null;
/* 1424 */     this._indentNumber = 0;
/* 1425 */     setOutputProperties(null);
/* 1426 */     this._tohFactory = null;
/* 1427 */     this._ostream = null;
/*      */   }
/*      */ 
/*      */   static class MessageHandler extends MessageHandler
/*      */   {
/*      */     private ErrorListener _errorListener;
/*      */ 
/*      */     public MessageHandler(ErrorListener errorListener)
/*      */     {
/*  240 */       this._errorListener = errorListener;
/*      */     }
/*      */ 
/*      */     public void displayMessage(String msg) {
/*  244 */       if (this._errorListener == null)
/*  245 */         System.err.println(msg);
/*      */       else
/*      */         try
/*      */         {
/*  249 */           this._errorListener.warning(new TransformerException(msg));
/*      */         }
/*      */         catch (TransformerException e)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl
 * JD-Core Version:    0.6.2
 */