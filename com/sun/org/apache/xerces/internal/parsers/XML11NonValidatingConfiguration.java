/*      */ package com.sun.org.apache.xerces.internal.parsers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XML11DocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XML11NSDocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLVersionDetector;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*      */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*      */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class XML11NonValidatingConfiguration extends ParserConfigurationSettings
/*      */   implements XMLPullParserConfiguration, XML11Configurable
/*      */ {
/*      */   protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
/*      */   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*      */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*      */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*      */   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
/*      */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLInputSource fInputSource;
/*      */   protected ValidationManager fValidationManager;
/*      */   protected XMLVersionDetector fVersionDetector;
/*      */   protected XMLLocator fLocator;
/*      */   protected Locale fLocale;
/*      */   protected ArrayList fComponents;
/*  184 */   protected ArrayList fXML11Components = null;
/*      */ 
/*  187 */   protected ArrayList fCommonComponents = null;
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDTDHandler fDTDHandler;
/*      */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*      */   protected XMLDocumentSource fLastComponent;
/*  206 */   protected boolean fParseInProgress = false;
/*      */ 
/*  211 */   protected boolean fConfigUpdated = false;
/*      */   protected DTDDVFactory fDatatypeValidatorFactory;
/*      */   protected XMLNSDocumentScannerImpl fNamespaceScanner;
/*      */   protected XMLDocumentScannerImpl fNonNSScanner;
/*      */   protected XMLDTDScanner fDTDScanner;
/*  234 */   protected DTDDVFactory fXML11DatatypeFactory = null;
/*      */ 
/*  237 */   protected XML11NSDocumentScannerImpl fXML11NSDocScanner = null;
/*      */ 
/*  240 */   protected XML11DocumentScannerImpl fXML11DocScanner = null;
/*      */ 
/*  243 */   protected XML11DTDScannerImpl fXML11DTDScanner = null;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLEntityManager fEntityManager;
/*      */   protected XMLDocumentScanner fCurrentScanner;
/*      */   protected DTDDVFactory fCurrentDVFactory;
/*      */   protected XMLDTDScanner fCurrentDTDScanner;
/*  269 */   private boolean f11Initialized = false;
/*      */ 
/*      */   public XML11NonValidatingConfiguration()
/*      */   {
/*  277 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public XML11NonValidatingConfiguration(SymbolTable symbolTable)
/*      */   {
/*  286 */     this(symbolTable, null, null);
/*      */   }
/*      */ 
/*      */   public XML11NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*      */   {
/*  301 */     this(symbolTable, grammarPool, null);
/*      */   }
/*      */ 
/*      */   public XML11NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*      */   {
/*  321 */     super(parentSettings);
/*      */ 
/*  325 */     this.fComponents = new ArrayList();
/*      */ 
/*  327 */     this.fXML11Components = new ArrayList();
/*      */ 
/*  329 */     this.fCommonComponents = new ArrayList();
/*      */ 
/*  332 */     this.fFeatures = new HashMap();
/*  333 */     this.fProperties = new HashMap();
/*      */ 
/*  336 */     String[] recognizedFeatures = { "http://apache.org/xml/features/continue-after-fatal-error", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", "http://apache.org/xml/features/internal/parser-settings" };
/*      */ 
/*  345 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/*  348 */     this.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
/*  349 */     this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/*  350 */     this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
/*  351 */     this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
/*  352 */     this.fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
/*  353 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/*      */ 
/*  356 */     String[] recognizedProperties = { "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/grammar-pool" };
/*      */ 
/*  371 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  373 */     if (symbolTable == null) {
/*  374 */       symbolTable = new SymbolTable();
/*      */     }
/*  376 */     this.fSymbolTable = symbolTable;
/*  377 */     this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*      */ 
/*  379 */     this.fGrammarPool = grammarPool;
/*  380 */     if (this.fGrammarPool != null) {
/*  381 */       this.fProperties.put("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*      */     }
/*      */ 
/*  384 */     this.fEntityManager = new XMLEntityManager();
/*  385 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*  386 */     addCommonComponent(this.fEntityManager);
/*      */ 
/*  388 */     this.fErrorReporter = new XMLErrorReporter();
/*  389 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*  390 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  391 */     addCommonComponent(this.fErrorReporter);
/*      */ 
/*  393 */     this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/*  394 */     this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/*  395 */     addComponent(this.fNamespaceScanner);
/*      */ 
/*  397 */     this.fDTDScanner = new XMLDTDScannerImpl();
/*  398 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/*  399 */     addComponent((XMLComponent)this.fDTDScanner);
/*      */ 
/*  401 */     this.fDatatypeValidatorFactory = DTDDVFactory.getInstance();
/*  402 */     this.fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*      */ 
/*  404 */     this.fValidationManager = new ValidationManager();
/*  405 */     this.fProperties.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*      */ 
/*  407 */     this.fVersionDetector = new XMLVersionDetector();
/*      */ 
/*  410 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/*  411 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/*  412 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/*  413 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  418 */       setLocale(Locale.getDefault());
/*      */     }
/*      */     catch (XNIException e)
/*      */     {
/*      */     }
/*      */ 
/*  424 */     this.fConfigUpdated = false;
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws XMLConfigurationException, IOException
/*      */   {
/*  448 */     this.fInputSource = inputSource;
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws XNIException
/*      */   {
/*  461 */     this.fLocale = locale;
/*  462 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  472 */     this.fDocumentHandler = documentHandler;
/*  473 */     if (this.fLastComponent != null) {
/*  474 */       this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
/*  475 */       if (this.fDocumentHandler != null)
/*  476 */         this.fDocumentHandler.setDocumentSource(this.fLastComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  483 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*  492 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  497 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
/*      */   {
/*  506 */     this.fDTDContentModelHandler = handler;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  511 */     return this.fDTDContentModelHandler;
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(XMLEntityResolver resolver)
/*      */   {
/*  522 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
/*      */   }
/*      */ 
/*      */   public XMLEntityResolver getEntityResolver()
/*      */   {
/*  533 */     return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*      */   {
/*  555 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*      */   }
/*      */ 
/*      */   public XMLErrorHandler getErrorHandler()
/*      */   {
/*  567 */     return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
/*      */   }
/*      */ 
/*      */   public void cleanup()
/*      */   {
/*  577 */     this.fEntityManager.closeReaders();
/*      */   }
/*      */ 
/*      */   public void parse(XMLInputSource source)
/*      */     throws XNIException, IOException
/*      */   {
/*  590 */     if (this.fParseInProgress)
/*      */     {
/*  592 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*      */     }
/*  594 */     this.fParseInProgress = true;
/*      */     try
/*      */     {
/*  597 */       setInputSource(source);
/*  598 */       parse(true);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  602 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  606 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  610 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  614 */       throw new XNIException(ex);
/*      */     } finally {
/*  616 */       this.fParseInProgress = false;
/*      */ 
/*  618 */       cleanup();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean parse(boolean complete)
/*      */     throws XNIException, IOException
/*      */   {
/*  626 */     if (this.fInputSource != null) {
/*      */       try {
/*  628 */         this.fValidationManager.reset();
/*  629 */         this.fVersionDetector.reset(this);
/*  630 */         resetCommon();
/*      */ 
/*  632 */         short version = this.fVersionDetector.determineDocVersion(this.fInputSource);
/*  633 */         if (version == 2) {
/*  634 */           initXML11Components();
/*  635 */           configureXML11Pipeline();
/*  636 */           resetXML11();
/*      */         } else {
/*  638 */           configurePipeline();
/*  639 */           reset();
/*      */         }
/*      */ 
/*  643 */         this.fConfigUpdated = false;
/*      */ 
/*  646 */         this.fVersionDetector.startDocumentParsing((XMLEntityHandler)this.fCurrentScanner, version);
/*  647 */         this.fInputSource = null;
/*      */       }
/*      */       catch (XNIException ex)
/*      */       {
/*  651 */         throw ex;
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*  655 */         throw ex;
/*      */       }
/*      */       catch (RuntimeException ex)
/*      */       {
/*  659 */         throw ex;
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  663 */         throw new XNIException(ex);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  668 */       return this.fCurrentScanner.scanDocument(complete);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  672 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  676 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  680 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  684 */       throw new XNIException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public FeatureState getFeatureState(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  704 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/*  705 */       return FeatureState.is(this.fConfigUpdated);
/*      */     }
/*  707 */     return super.getFeatureState(featureId);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  726 */     this.fConfigUpdated = true;
/*      */ 
/*  728 */     int count = this.fComponents.size();
/*  729 */     for (int i = 0; i < count; i++) {
/*  730 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  731 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  734 */     count = this.fCommonComponents.size();
/*  735 */     for (int i = 0; i < count; i++) {
/*  736 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  737 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  741 */     count = this.fXML11Components.size();
/*  742 */     for (int i = 0; i < count; i++) {
/*  743 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  745 */         c.setFeature(featureId, state);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  752 */     super.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  764 */     this.fConfigUpdated = true;
/*      */ 
/*  766 */     int count = this.fComponents.size();
/*  767 */     for (int i = 0; i < count; i++) {
/*  768 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  769 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  772 */     count = this.fCommonComponents.size();
/*  773 */     for (int i = 0; i < count; i++) {
/*  774 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  775 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  778 */     count = this.fXML11Components.size();
/*  779 */     for (int i = 0; i < count; i++) {
/*  780 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  782 */         c.setProperty(propertyId, value);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  790 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  797 */     return this.fLocale;
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */     throws XNIException
/*      */   {
/*  804 */     int count = this.fComponents.size();
/*  805 */     for (int i = 0; i < count; i++) {
/*  806 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  807 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetCommon()
/*      */     throws XNIException
/*      */   {
/*  817 */     int count = this.fCommonComponents.size();
/*  818 */     for (int i = 0; i < count; i++) {
/*  819 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  820 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetXML11()
/*      */     throws XNIException
/*      */   {
/*  831 */     int count = this.fXML11Components.size();
/*  832 */     for (int i = 0; i < count; i++) {
/*  833 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*  834 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configureXML11Pipeline()
/*      */   {
/*  845 */     if (this.fCurrentDVFactory != this.fXML11DatatypeFactory) {
/*  846 */       this.fCurrentDVFactory = this.fXML11DatatypeFactory;
/*  847 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/*  851 */     if (this.fCurrentDTDScanner != this.fXML11DTDScanner) {
/*  852 */       this.fCurrentDTDScanner = this.fXML11DTDScanner;
/*  853 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*      */     }
/*  855 */     this.fXML11DTDScanner.setDTDHandler(this.fDTDHandler);
/*  856 */     this.fXML11DTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*      */ 
/*  859 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/*  860 */       if (this.fCurrentScanner != this.fXML11NSDocScanner) {
/*  861 */         this.fCurrentScanner = this.fXML11NSDocScanner;
/*  862 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11NSDocScanner);
/*      */       }
/*      */ 
/*  865 */       this.fXML11NSDocScanner.setDTDValidator(null);
/*  866 */       this.fXML11NSDocScanner.setDocumentHandler(this.fDocumentHandler);
/*  867 */       if (this.fDocumentHandler != null) {
/*  868 */         this.fDocumentHandler.setDocumentSource(this.fXML11NSDocScanner);
/*      */       }
/*  870 */       this.fLastComponent = this.fXML11NSDocScanner;
/*      */     }
/*      */     else
/*      */     {
/*  874 */       if (this.fXML11DocScanner == null)
/*      */       {
/*  876 */         this.fXML11DocScanner = new XML11DocumentScannerImpl();
/*  877 */         addXML11Component(this.fXML11DocScanner);
/*      */       }
/*  879 */       if (this.fCurrentScanner != this.fXML11DocScanner) {
/*  880 */         this.fCurrentScanner = this.fXML11DocScanner;
/*  881 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11DocScanner);
/*      */       }
/*  883 */       this.fXML11DocScanner.setDocumentHandler(this.fDocumentHandler);
/*      */ 
/*  885 */       if (this.fDocumentHandler != null) {
/*  886 */         this.fDocumentHandler.setDocumentSource(this.fXML11DocScanner);
/*      */       }
/*  888 */       this.fLastComponent = this.fXML11DocScanner;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configurePipeline()
/*      */   {
/*  895 */     if (this.fCurrentDVFactory != this.fDatatypeValidatorFactory) {
/*  896 */       this.fCurrentDVFactory = this.fDatatypeValidatorFactory;
/*      */ 
/*  898 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/*  902 */     if (this.fCurrentDTDScanner != this.fDTDScanner) {
/*  903 */       this.fCurrentDTDScanner = this.fDTDScanner;
/*  904 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*      */     }
/*  906 */     this.fDTDScanner.setDTDHandler(this.fDTDHandler);
/*  907 */     this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*      */ 
/*  910 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/*  911 */       if (this.fCurrentScanner != this.fNamespaceScanner) {
/*  912 */         this.fCurrentScanner = this.fNamespaceScanner;
/*  913 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/*      */       }
/*  915 */       this.fNamespaceScanner.setDTDValidator(null);
/*  916 */       this.fNamespaceScanner.setDocumentHandler(this.fDocumentHandler);
/*  917 */       if (this.fDocumentHandler != null) {
/*  918 */         this.fDocumentHandler.setDocumentSource(this.fNamespaceScanner);
/*      */       }
/*  920 */       this.fLastComponent = this.fNamespaceScanner;
/*      */     }
/*      */     else {
/*  923 */       if (this.fNonNSScanner == null) {
/*  924 */         this.fNonNSScanner = new XMLDocumentScannerImpl();
/*      */ 
/*  926 */         addComponent(this.fNonNSScanner);
/*      */       }
/*      */ 
/*  929 */       if (this.fCurrentScanner != this.fNonNSScanner) {
/*  930 */         this.fCurrentScanner = this.fNonNSScanner;
/*  931 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNonNSScanner);
/*      */       }
/*      */ 
/*  935 */       this.fNonNSScanner.setDocumentHandler(this.fDocumentHandler);
/*  936 */       if (this.fDocumentHandler != null) {
/*  937 */         this.fDocumentHandler.setDocumentSource(this.fNonNSScanner);
/*      */       }
/*  939 */       this.fLastComponent = this.fNonNSScanner;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected FeatureState checkFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  965 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/*  966 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*      */ 
/*  974 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*      */       {
/*  976 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/*  982 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*      */       {
/*  985 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*  990 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*      */       {
/*  993 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*  998 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*      */       {
/* 1000 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1005 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/* 1007 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1013 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*      */       {
/* 1015 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1019 */       if ((suffixLength == "internal/parser-settings".length()) && (featureId.endsWith("internal/parser-settings")))
/*      */       {
/* 1021 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1029 */     return super.checkFeature(featureId);
/*      */   }
/*      */ 
/*      */   protected PropertyState checkProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1052 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 1053 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/* 1055 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*      */       {
/* 1057 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */     }
/*      */ 
/* 1061 */     if (propertyId.startsWith("http://java.sun.com/xml/jaxp/properties/")) {
/* 1062 */       int suffixLength = propertyId.length() - "http://java.sun.com/xml/jaxp/properties/".length();
/*      */ 
/* 1064 */       if ((suffixLength == "schemaSource".length()) && (propertyId.endsWith("schemaSource")))
/*      */       {
/* 1066 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1071 */     if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 1072 */       int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*      */ 
/* 1084 */       if ((suffixLength == "xml-string".length()) && (propertyId.endsWith("xml-string")))
/*      */       {
/* 1089 */         return PropertyState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1097 */     return super.checkProperty(propertyId);
/*      */   }
/*      */ 
/*      */   protected void addComponent(XMLComponent component)
/*      */   {
/* 1112 */     if (this.fComponents.contains(component)) {
/* 1113 */       return;
/*      */     }
/* 1115 */     this.fComponents.add(component);
/* 1116 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addCommonComponent(XMLComponent component)
/*      */   {
/* 1130 */     if (this.fCommonComponents.contains(component)) {
/* 1131 */       return;
/*      */     }
/* 1133 */     this.fCommonComponents.add(component);
/* 1134 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addXML11Component(XMLComponent component)
/*      */   {
/* 1148 */     if (this.fXML11Components.contains(component)) {
/* 1149 */       return;
/*      */     }
/* 1151 */     this.fXML11Components.add(component);
/* 1152 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addRecognizedParamsAndSetDefaults(XMLComponent component)
/*      */   {
/* 1168 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 1169 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/* 1172 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 1173 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/* 1176 */     if (recognizedFeatures != null) {
/* 1177 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/* 1178 */         String featureId = recognizedFeatures[i];
/* 1179 */         Boolean state = component.getFeatureDefault(featureId);
/* 1180 */         if (state != null)
/*      */         {
/* 1182 */           if (!this.fFeatures.containsKey(featureId)) {
/* 1183 */             this.fFeatures.put(featureId, state);
/*      */ 
/* 1188 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1193 */     if (recognizedProperties != null)
/* 1194 */       for (int i = 0; i < recognizedProperties.length; i++) {
/* 1195 */         String propertyId = recognizedProperties[i];
/* 1196 */         Object value = component.getPropertyDefault(propertyId);
/* 1197 */         if (value != null)
/*      */         {
/* 1199 */           if (!this.fProperties.containsKey(propertyId)) {
/* 1200 */             this.fProperties.put(propertyId, value);
/*      */ 
/* 1205 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void initXML11Components()
/*      */   {
/* 1213 */     if (!this.f11Initialized)
/*      */     {
/* 1216 */       this.fXML11DatatypeFactory = DTDDVFactory.getInstance("com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl");
/*      */ 
/* 1219 */       this.fXML11DTDScanner = new XML11DTDScannerImpl();
/* 1220 */       addXML11Component(this.fXML11DTDScanner);
/*      */ 
/* 1223 */       this.fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
/* 1224 */       addXML11Component(this.fXML11NSDocScanner);
/*      */ 
/* 1226 */       this.f11Initialized = true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XML11NonValidatingConfiguration
 * JD-Core Version:    0.6.2
 */