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
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDProcessor;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XML11DTDValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XML11NSDTDValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLNSDTDValidator;
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
/*      */ public class XML11DTDConfiguration extends ParserConfigurationSettings
/*      */   implements XMLPullParserConfiguration, XML11Configurable
/*      */ {
/*      */   protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
/*      */   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*      */   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*      */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*      */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String DTD_PROCESSOR = "http://apache.org/xml/properties/internal/dtd-processor";
/*      */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*      */   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
/*      */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*      */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*      */   protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLInputSource fInputSource;
/*      */   protected ValidationManager fValidationManager;
/*      */   protected XMLVersionDetector fVersionDetector;
/*      */   protected XMLLocator fLocator;
/*      */   protected Locale fLocale;
/*      */   protected ArrayList fComponents;
/*  227 */   protected ArrayList fXML11Components = null;
/*      */ 
/*  230 */   protected ArrayList fCommonComponents = null;
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDTDHandler fDTDHandler;
/*      */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*      */   protected XMLDocumentSource fLastComponent;
/*  249 */   protected boolean fParseInProgress = false;
/*      */ 
/*  255 */   protected boolean fConfigUpdated = false;
/*      */   protected DTDDVFactory fDatatypeValidatorFactory;
/*      */   protected XMLNSDocumentScannerImpl fNamespaceScanner;
/*      */   protected XMLDocumentScannerImpl fNonNSScanner;
/*      */   protected XMLDTDValidator fDTDValidator;
/*      */   protected XMLDTDValidator fNonNSDTDValidator;
/*      */   protected XMLDTDScanner fDTDScanner;
/*      */   protected XMLDTDProcessor fDTDProcessor;
/*  287 */   protected DTDDVFactory fXML11DatatypeFactory = null;
/*      */ 
/*  290 */   protected XML11NSDocumentScannerImpl fXML11NSDocScanner = null;
/*      */ 
/*  293 */   protected XML11DocumentScannerImpl fXML11DocScanner = null;
/*      */ 
/*  296 */   protected XML11NSDTDValidator fXML11NSDTDValidator = null;
/*      */ 
/*  299 */   protected XML11DTDValidator fXML11DTDValidator = null;
/*      */ 
/*  302 */   protected XML11DTDScannerImpl fXML11DTDScanner = null;
/*      */ 
/*  305 */   protected XML11DTDProcessor fXML11DTDProcessor = null;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLEntityManager fEntityManager;
/*      */   protected XMLDocumentScanner fCurrentScanner;
/*      */   protected DTDDVFactory fCurrentDVFactory;
/*      */   protected XMLDTDScanner fCurrentDTDScanner;
/*  330 */   private boolean f11Initialized = false;
/*      */ 
/*      */   public XML11DTDConfiguration()
/*      */   {
/*  338 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public XML11DTDConfiguration(SymbolTable symbolTable)
/*      */   {
/*  347 */     this(symbolTable, null, null);
/*      */   }
/*      */ 
/*      */   public XML11DTDConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*      */   {
/*  362 */     this(symbolTable, grammarPool, null);
/*      */   }
/*      */ 
/*      */   public XML11DTDConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*      */   {
/*  382 */     super(parentSettings);
/*      */ 
/*  386 */     this.fComponents = new ArrayList();
/*      */ 
/*  388 */     this.fXML11Components = new ArrayList();
/*      */ 
/*  390 */     this.fCommonComponents = new ArrayList();
/*      */ 
/*  393 */     this.fFeatures = new HashMap();
/*  394 */     this.fProperties = new HashMap();
/*      */ 
/*  397 */     String[] recognizedFeatures = { "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", "http://apache.org/xml/features/internal/parser-settings" };
/*      */ 
/*  406 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/*  408 */     this.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
/*  409 */     this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/*  410 */     this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
/*  411 */     this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
/*  412 */     this.fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
/*  413 */     this.fFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.TRUE);
/*  414 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/*      */ 
/*  417 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/dtd-processor", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/grammar-pool", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage" };
/*      */ 
/*  434 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  436 */     if (symbolTable == null) {
/*  437 */       symbolTable = new SymbolTable();
/*      */     }
/*  439 */     this.fSymbolTable = symbolTable;
/*  440 */     this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*      */ 
/*  442 */     this.fGrammarPool = grammarPool;
/*  443 */     if (this.fGrammarPool != null) {
/*  444 */       this.fProperties.put("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*      */     }
/*      */ 
/*  447 */     this.fEntityManager = new XMLEntityManager();
/*  448 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*  449 */     addCommonComponent(this.fEntityManager);
/*      */ 
/*  451 */     this.fErrorReporter = new XMLErrorReporter();
/*  452 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*  453 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  454 */     addCommonComponent(this.fErrorReporter);
/*      */ 
/*  456 */     this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/*  457 */     this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/*  458 */     addComponent(this.fNamespaceScanner);
/*      */ 
/*  460 */     this.fDTDScanner = new XMLDTDScannerImpl();
/*  461 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/*  462 */     addComponent((XMLComponent)this.fDTDScanner);
/*      */ 
/*  464 */     this.fDTDProcessor = new XMLDTDProcessor();
/*  465 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/*  466 */     addComponent(this.fDTDProcessor);
/*      */ 
/*  468 */     this.fDTDValidator = new XMLNSDTDValidator();
/*  469 */     this.fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/*  470 */     addComponent(this.fDTDValidator);
/*      */ 
/*  472 */     this.fDatatypeValidatorFactory = DTDDVFactory.getInstance();
/*  473 */     this.fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*      */ 
/*  475 */     this.fValidationManager = new ValidationManager();
/*  476 */     this.fProperties.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*      */ 
/*  478 */     this.fVersionDetector = new XMLVersionDetector();
/*      */ 
/*  481 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/*  482 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/*  483 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/*  484 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  489 */       setLocale(Locale.getDefault());
/*      */     }
/*      */     catch (XNIException e)
/*      */     {
/*      */     }
/*      */ 
/*  495 */     this.fConfigUpdated = false;
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws XMLConfigurationException, IOException
/*      */   {
/*  522 */     this.fInputSource = inputSource;
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws XNIException
/*      */   {
/*  535 */     this.fLocale = locale;
/*  536 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  546 */     this.fDocumentHandler = documentHandler;
/*  547 */     if (this.fLastComponent != null) {
/*  548 */       this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
/*  549 */       if (this.fDocumentHandler != null)
/*  550 */         this.fDocumentHandler.setDocumentSource(this.fLastComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  557 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*  566 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  571 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
/*      */   {
/*  580 */     this.fDTDContentModelHandler = handler;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  585 */     return this.fDTDContentModelHandler;
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(XMLEntityResolver resolver)
/*      */   {
/*  596 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
/*      */   }
/*      */ 
/*      */   public XMLEntityResolver getEntityResolver()
/*      */   {
/*  607 */     return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*      */   {
/*  629 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*      */   }
/*      */ 
/*      */   public XMLErrorHandler getErrorHandler()
/*      */   {
/*  641 */     return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
/*      */   }
/*      */ 
/*      */   public void cleanup()
/*      */   {
/*  651 */     this.fEntityManager.closeReaders();
/*      */   }
/*      */ 
/*      */   public void parse(XMLInputSource source)
/*      */     throws XNIException, IOException
/*      */   {
/*  664 */     if (this.fParseInProgress)
/*      */     {
/*  666 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*      */     }
/*  668 */     this.fParseInProgress = true;
/*      */     try
/*      */     {
/*  671 */       setInputSource(source);
/*  672 */       parse(true);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  676 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  680 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  684 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  688 */       throw new XNIException(ex);
/*      */     } finally {
/*  690 */       this.fParseInProgress = false;
/*      */ 
/*  692 */       cleanup();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean parse(boolean complete)
/*      */     throws XNIException, IOException
/*      */   {
/*  700 */     if (this.fInputSource != null) {
/*      */       try {
/*  702 */         this.fValidationManager.reset();
/*  703 */         this.fVersionDetector.reset(this);
/*  704 */         resetCommon();
/*      */ 
/*  706 */         short version = this.fVersionDetector.determineDocVersion(this.fInputSource);
/*  707 */         if (version == 2) {
/*  708 */           initXML11Components();
/*  709 */           configureXML11Pipeline();
/*  710 */           resetXML11();
/*      */         } else {
/*  712 */           configurePipeline();
/*  713 */           reset();
/*      */         }
/*      */ 
/*  717 */         this.fConfigUpdated = false;
/*      */ 
/*  720 */         this.fVersionDetector.startDocumentParsing((XMLEntityHandler)this.fCurrentScanner, version);
/*  721 */         this.fInputSource = null;
/*      */       }
/*      */       catch (XNIException ex)
/*      */       {
/*  725 */         throw ex;
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*  729 */         throw ex;
/*      */       }
/*      */       catch (RuntimeException ex)
/*      */       {
/*  733 */         throw ex;
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  737 */         throw new XNIException(ex);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  742 */       return this.fCurrentScanner.scanDocument(complete);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  746 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  750 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  754 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  758 */       throw new XNIException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public FeatureState getFeatureState(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  778 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/*  779 */       return FeatureState.is(this.fConfigUpdated);
/*      */     }
/*  781 */     return super.getFeatureState(featureId);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  800 */     this.fConfigUpdated = true;
/*      */ 
/*  802 */     int count = this.fComponents.size();
/*  803 */     for (int i = 0; i < count; i++) {
/*  804 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  805 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  808 */     count = this.fCommonComponents.size();
/*  809 */     for (int i = 0; i < count; i++) {
/*  810 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  811 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  815 */     count = this.fXML11Components.size();
/*  816 */     for (int i = 0; i < count; i++) {
/*  817 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  819 */         c.setFeature(featureId, state);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  826 */     super.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  838 */     this.fConfigUpdated = true;
/*      */ 
/*  840 */     int count = this.fComponents.size();
/*  841 */     for (int i = 0; i < count; i++) {
/*  842 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  843 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  846 */     count = this.fCommonComponents.size();
/*  847 */     for (int i = 0; i < count; i++) {
/*  848 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  849 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  852 */     count = this.fXML11Components.size();
/*  853 */     for (int i = 0; i < count; i++) {
/*  854 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  856 */         c.setProperty(propertyId, value);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  864 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  871 */     return this.fLocale;
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */     throws XNIException
/*      */   {
/*  878 */     int count = this.fComponents.size();
/*  879 */     for (int i = 0; i < count; i++) {
/*  880 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  881 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetCommon()
/*      */     throws XNIException
/*      */   {
/*  891 */     int count = this.fCommonComponents.size();
/*  892 */     for (int i = 0; i < count; i++) {
/*  893 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  894 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetXML11()
/*      */     throws XNIException
/*      */   {
/*  904 */     int count = this.fXML11Components.size();
/*  905 */     for (int i = 0; i < count; i++) {
/*  906 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*  907 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configureXML11Pipeline()
/*      */   {
/*  917 */     if (this.fCurrentDVFactory != this.fXML11DatatypeFactory) {
/*  918 */       this.fCurrentDVFactory = this.fXML11DatatypeFactory;
/*  919 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*  921 */     if (this.fCurrentDTDScanner != this.fXML11DTDScanner) {
/*  922 */       this.fCurrentDTDScanner = this.fXML11DTDScanner;
/*  923 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*  924 */       setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/*  927 */     this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
/*  928 */     this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
/*  929 */     this.fXML11DTDProcessor.setDTDHandler(this.fDTDHandler);
/*  930 */     if (this.fDTDHandler != null) {
/*  931 */       this.fDTDHandler.setDTDSource(this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/*  934 */     this.fXML11DTDScanner.setDTDContentModelHandler(this.fXML11DTDProcessor);
/*  935 */     this.fXML11DTDProcessor.setDTDContentModelSource(this.fXML11DTDScanner);
/*  936 */     this.fXML11DTDProcessor.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*  937 */     if (this.fDTDContentModelHandler != null) {
/*  938 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/*  942 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/*  943 */       if (this.fCurrentScanner != this.fXML11NSDocScanner) {
/*  944 */         this.fCurrentScanner = this.fXML11NSDocScanner;
/*  945 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11NSDocScanner);
/*  946 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fXML11NSDTDValidator);
/*      */       }
/*      */ 
/*  949 */       this.fXML11NSDocScanner.setDTDValidator(this.fXML11NSDTDValidator);
/*  950 */       this.fXML11NSDocScanner.setDocumentHandler(this.fXML11NSDTDValidator);
/*  951 */       this.fXML11NSDTDValidator.setDocumentSource(this.fXML11NSDocScanner);
/*  952 */       this.fXML11NSDTDValidator.setDocumentHandler(this.fDocumentHandler);
/*      */ 
/*  954 */       if (this.fDocumentHandler != null) {
/*  955 */         this.fDocumentHandler.setDocumentSource(this.fXML11NSDTDValidator);
/*      */       }
/*  957 */       this.fLastComponent = this.fXML11NSDTDValidator;
/*      */     }
/*      */     else
/*      */     {
/*  961 */       if (this.fXML11DocScanner == null)
/*      */       {
/*  963 */         this.fXML11DocScanner = new XML11DocumentScannerImpl();
/*  964 */         addXML11Component(this.fXML11DocScanner);
/*  965 */         this.fXML11DTDValidator = new XML11DTDValidator();
/*  966 */         addXML11Component(this.fXML11DTDValidator);
/*      */       }
/*  968 */       if (this.fCurrentScanner != this.fXML11DocScanner) {
/*  969 */         this.fCurrentScanner = this.fXML11DocScanner;
/*  970 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11DocScanner);
/*  971 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fXML11DTDValidator);
/*      */       }
/*  973 */       this.fXML11DocScanner.setDocumentHandler(this.fXML11DTDValidator);
/*  974 */       this.fXML11DTDValidator.setDocumentSource(this.fXML11DocScanner);
/*  975 */       this.fXML11DTDValidator.setDocumentHandler(this.fDocumentHandler);
/*      */ 
/*  977 */       if (this.fDocumentHandler != null) {
/*  978 */         this.fDocumentHandler.setDocumentSource(this.fXML11DTDValidator);
/*      */       }
/*  980 */       this.fLastComponent = this.fXML11DTDValidator;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configurePipeline()
/*      */   {
/*  987 */     if (this.fCurrentDVFactory != this.fDatatypeValidatorFactory) {
/*  988 */       this.fCurrentDVFactory = this.fDatatypeValidatorFactory;
/*      */ 
/*  990 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/*  994 */     if (this.fCurrentDTDScanner != this.fDTDScanner) {
/*  995 */       this.fCurrentDTDScanner = this.fDTDScanner;
/*  996 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*  997 */       setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/*      */     }
/*  999 */     this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 1000 */     this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/* 1001 */     this.fDTDProcessor.setDTDHandler(this.fDTDHandler);
/* 1002 */     if (this.fDTDHandler != null) {
/* 1003 */       this.fDTDHandler.setDTDSource(this.fDTDProcessor);
/*      */     }
/*      */ 
/* 1006 */     this.fDTDScanner.setDTDContentModelHandler(this.fDTDProcessor);
/* 1007 */     this.fDTDProcessor.setDTDContentModelSource(this.fDTDScanner);
/* 1008 */     this.fDTDProcessor.setDTDContentModelHandler(this.fDTDContentModelHandler);
/* 1009 */     if (this.fDTDContentModelHandler != null) {
/* 1010 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDProcessor);
/*      */     }
/*      */ 
/* 1014 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 1015 */       if (this.fCurrentScanner != this.fNamespaceScanner) {
/* 1016 */         this.fCurrentScanner = this.fNamespaceScanner;
/* 1017 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/* 1018 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/*      */       }
/* 1020 */       this.fNamespaceScanner.setDTDValidator(this.fDTDValidator);
/* 1021 */       this.fNamespaceScanner.setDocumentHandler(this.fDTDValidator);
/* 1022 */       this.fDTDValidator.setDocumentSource(this.fNamespaceScanner);
/* 1023 */       this.fDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 1024 */       if (this.fDocumentHandler != null) {
/* 1025 */         this.fDocumentHandler.setDocumentSource(this.fDTDValidator);
/*      */       }
/* 1027 */       this.fLastComponent = this.fDTDValidator;
/*      */     }
/*      */     else {
/* 1030 */       if (this.fNonNSScanner == null) {
/* 1031 */         this.fNonNSScanner = new XMLDocumentScannerImpl();
/* 1032 */         this.fNonNSDTDValidator = new XMLDTDValidator();
/*      */ 
/* 1034 */         addComponent(this.fNonNSScanner);
/* 1035 */         addComponent(this.fNonNSDTDValidator);
/*      */       }
/* 1037 */       if (this.fCurrentScanner != this.fNonNSScanner) {
/* 1038 */         this.fCurrentScanner = this.fNonNSScanner;
/* 1039 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNonNSScanner);
/* 1040 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fNonNSDTDValidator);
/*      */       }
/*      */ 
/* 1043 */       this.fNonNSScanner.setDocumentHandler(this.fNonNSDTDValidator);
/* 1044 */       this.fNonNSDTDValidator.setDocumentSource(this.fNonNSScanner);
/* 1045 */       this.fNonNSDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 1046 */       if (this.fDocumentHandler != null) {
/* 1047 */         this.fDocumentHandler.setDocumentSource(this.fNonNSDTDValidator);
/*      */       }
/* 1049 */       this.fLastComponent = this.fNonNSDTDValidator;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected FeatureState checkFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1075 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 1076 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*      */ 
/* 1084 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*      */       {
/* 1086 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1092 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*      */       {
/* 1095 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1100 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*      */       {
/* 1103 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1108 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*      */       {
/* 1110 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1115 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/* 1117 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1123 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*      */       {
/* 1125 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1129 */       if ((suffixLength == "internal/parser-settings".length()) && (featureId.endsWith("internal/parser-settings")))
/*      */       {
/* 1131 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1139 */     return super.checkFeature(featureId);
/*      */   }
/*      */ 
/*      */   protected PropertyState checkProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1162 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 1163 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/* 1165 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*      */       {
/* 1167 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1172 */     if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 1173 */       int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*      */ 
/* 1185 */       if ((suffixLength == "xml-string".length()) && (propertyId.endsWith("xml-string")))
/*      */       {
/* 1190 */         return PropertyState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1198 */     return super.checkProperty(propertyId);
/*      */   }
/*      */ 
/*      */   protected void addComponent(XMLComponent component)
/*      */   {
/* 1213 */     if (this.fComponents.contains(component)) {
/* 1214 */       return;
/*      */     }
/* 1216 */     this.fComponents.add(component);
/* 1217 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addCommonComponent(XMLComponent component)
/*      */   {
/* 1231 */     if (this.fCommonComponents.contains(component)) {
/* 1232 */       return;
/*      */     }
/* 1234 */     this.fCommonComponents.add(component);
/* 1235 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addXML11Component(XMLComponent component)
/*      */   {
/* 1249 */     if (this.fXML11Components.contains(component)) {
/* 1250 */       return;
/*      */     }
/* 1252 */     this.fXML11Components.add(component);
/* 1253 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addRecognizedParamsAndSetDefaults(XMLComponent component)
/*      */   {
/* 1269 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 1270 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/* 1273 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 1274 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/* 1277 */     if (recognizedFeatures != null) {
/* 1278 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/* 1279 */         String featureId = recognizedFeatures[i];
/* 1280 */         Boolean state = component.getFeatureDefault(featureId);
/* 1281 */         if (state != null)
/*      */         {
/* 1283 */           if (!this.fFeatures.containsKey(featureId)) {
/* 1284 */             this.fFeatures.put(featureId, state);
/*      */ 
/* 1289 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1294 */     if (recognizedProperties != null)
/* 1295 */       for (int i = 0; i < recognizedProperties.length; i++) {
/* 1296 */         String propertyId = recognizedProperties[i];
/* 1297 */         Object value = component.getPropertyDefault(propertyId);
/* 1298 */         if (value != null)
/*      */         {
/* 1300 */           if (!this.fProperties.containsKey(propertyId)) {
/* 1301 */             this.fProperties.put(propertyId, value);
/*      */ 
/* 1306 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void initXML11Components()
/*      */   {
/* 1314 */     if (!this.f11Initialized)
/*      */     {
/* 1317 */       this.fXML11DatatypeFactory = DTDDVFactory.getInstance("com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl");
/*      */ 
/* 1320 */       this.fXML11DTDScanner = new XML11DTDScannerImpl();
/* 1321 */       addXML11Component(this.fXML11DTDScanner);
/* 1322 */       this.fXML11DTDProcessor = new XML11DTDProcessor();
/* 1323 */       addXML11Component(this.fXML11DTDProcessor);
/*      */ 
/* 1326 */       this.fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
/* 1327 */       addXML11Component(this.fXML11NSDocScanner);
/* 1328 */       this.fXML11NSDTDValidator = new XML11NSDTDValidator();
/* 1329 */       addXML11Component(this.fXML11NSDTDValidator);
/*      */ 
/* 1331 */       this.f11Initialized = true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XML11DTDConfiguration
 * JD-Core Version:    0.6.2
 */