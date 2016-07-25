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
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
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
/*      */ public class XML11Configuration extends ParserConfigurationSettings
/*      */   implements XMLPullParserConfiguration, XML11Configurable
/*      */ {
/*      */   protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";
/*      */   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
/*      */   protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
/*      */   protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*      */   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/*      */   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/*      */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*      */   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*      */   protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*      */   protected static final String XMLSCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String XMLSCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*      */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*      */   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
/*      */   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
/*      */   protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
/*      */   protected static final String XML_STRING = "http://xml.org/sax/properties/xml-string";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*      */   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
/*      */   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
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
/*      */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*      */   protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLInputSource fInputSource;
/*      */   protected ValidationManager fValidationManager;
/*      */   protected XMLVersionDetector fVersionDetector;
/*      */   protected XMLLocator fLocator;
/*      */   protected Locale fLocale;
/*      */   protected ArrayList fComponents;
/*  306 */   protected ArrayList fXML11Components = null;
/*      */ 
/*  309 */   protected ArrayList fCommonComponents = null;
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDTDHandler fDTDHandler;
/*      */   protected XMLDTDContentModelHandler fDTDContentModelHandler;
/*      */   protected XMLDocumentSource fLastComponent;
/*  328 */   protected boolean fParseInProgress = false;
/*      */ 
/*  333 */   protected boolean fConfigUpdated = false;
/*      */   protected DTDDVFactory fDatatypeValidatorFactory;
/*      */   protected XMLNSDocumentScannerImpl fNamespaceScanner;
/*      */   protected XMLDocumentScannerImpl fNonNSScanner;
/*      */   protected XMLDTDValidator fDTDValidator;
/*      */   protected XMLDTDValidator fNonNSDTDValidator;
/*      */   protected XMLDTDScanner fDTDScanner;
/*      */   protected XMLDTDProcessor fDTDProcessor;
/*  360 */   protected DTDDVFactory fXML11DatatypeFactory = null;
/*      */ 
/*  363 */   protected XML11NSDocumentScannerImpl fXML11NSDocScanner = null;
/*      */ 
/*  366 */   protected XML11DocumentScannerImpl fXML11DocScanner = null;
/*      */ 
/*  369 */   protected XML11NSDTDValidator fXML11NSDTDValidator = null;
/*      */ 
/*  372 */   protected XML11DTDValidator fXML11DTDValidator = null;
/*      */ 
/*  375 */   protected XML11DTDScannerImpl fXML11DTDScanner = null;
/*      */ 
/*  377 */   protected XML11DTDProcessor fXML11DTDProcessor = null;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLEntityManager fEntityManager;
/*      */   protected XMLSchemaValidator fSchemaValidator;
/*      */   protected XMLDocumentScanner fCurrentScanner;
/*      */   protected DTDDVFactory fCurrentDVFactory;
/*      */   protected XMLDTDScanner fCurrentDTDScanner;
/*  403 */   private boolean f11Initialized = false;
/*      */ 
/*      */   public XML11Configuration()
/*      */   {
/*  411 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public XML11Configuration(SymbolTable symbolTable)
/*      */   {
/*  420 */     this(symbolTable, null, null);
/*      */   }
/*      */ 
/*      */   public XML11Configuration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*      */   {
/*  435 */     this(symbolTable, grammarPool, null);
/*      */   }
/*      */ 
/*      */   public XML11Configuration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*      */   {
/*  455 */     super(parentSettings);
/*      */ 
/*  459 */     this.fComponents = new ArrayList();
/*      */ 
/*  461 */     this.fXML11Components = new ArrayList();
/*      */ 
/*  463 */     this.fCommonComponents = new ArrayList();
/*      */ 
/*  466 */     this.fFeatures = new HashMap();
/*  467 */     this.fProperties = new HashMap();
/*      */ 
/*  470 */     String[] recognizedFeatures = { "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://xml.org/sax/features/validation", "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/validation/schema/normalized-value", "http://apache.org/xml/features/validation/schema/element-default", "http://apache.org/xml/features/validation/schema/augment-psvi", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates", "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/schema-full-checking", "http://xml.org/sax/features/external-general-entities", "http://xml.org/sax/features/external-parameter-entities", "http://apache.org/xml/features/internal/parser-settings", "http://javax.xml.XMLConstants/feature/secure-processing" };
/*      */ 
/*  490 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/*  492 */     this.fFeatures.put("http://xml.org/sax/features/validation", Boolean.FALSE);
/*  493 */     this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/*  494 */     this.fFeatures.put("http://xml.org/sax/features/external-general-entities", Boolean.TRUE);
/*  495 */     this.fFeatures.put("http://xml.org/sax/features/external-parameter-entities", Boolean.TRUE);
/*  496 */     this.fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
/*  497 */     this.fFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.TRUE);
/*  498 */     this.fFeatures.put("http://apache.org/xml/features/validation/schema/element-default", Boolean.TRUE);
/*  499 */     this.fFeatures.put("http://apache.org/xml/features/validation/schema/normalized-value", Boolean.TRUE);
/*  500 */     this.fFeatures.put("http://apache.org/xml/features/validation/schema/augment-psvi", Boolean.TRUE);
/*  501 */     this.fFeatures.put("http://apache.org/xml/features/generate-synthetic-annotations", Boolean.FALSE);
/*  502 */     this.fFeatures.put("http://apache.org/xml/features/validate-annotations", Boolean.FALSE);
/*  503 */     this.fFeatures.put("http://apache.org/xml/features/honour-all-schemaLocations", Boolean.FALSE);
/*  504 */     this.fFeatures.put("http://apache.org/xml/features/namespace-growth", Boolean.FALSE);
/*  505 */     this.fFeatures.put("http://apache.org/xml/features/internal/tolerate-duplicates", Boolean.FALSE);
/*  506 */     this.fFeatures.put("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", Boolean.FALSE);
/*  507 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/*  508 */     this.fFeatures.put("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE);
/*      */ 
/*  511 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/dtd-processor", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/internal/validator/schema", "http://xml.org/sax/properties/xml-string", "http://apache.org/xml/properties/internal/grammar-pool", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://apache.org/xml/properties/locale", "http://apache.org/xml/properties/internal/validation/schema/dv-factory", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  540 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  542 */     if (symbolTable == null) {
/*  543 */       symbolTable = new SymbolTable();
/*      */     }
/*  545 */     this.fSymbolTable = symbolTable;
/*  546 */     this.fProperties.put("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/*      */ 
/*  548 */     this.fGrammarPool = grammarPool;
/*  549 */     if (this.fGrammarPool != null) {
/*  550 */       this.fProperties.put("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*      */     }
/*      */ 
/*  553 */     this.fEntityManager = new XMLEntityManager();
/*  554 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*  555 */     addCommonComponent(this.fEntityManager);
/*      */ 
/*  557 */     this.fErrorReporter = new XMLErrorReporter();
/*  558 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*  559 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  560 */     addCommonComponent(this.fErrorReporter);
/*      */ 
/*  562 */     this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/*  563 */     this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/*  564 */     addComponent(this.fNamespaceScanner);
/*      */ 
/*  566 */     this.fDTDScanner = new XMLDTDScannerImpl();
/*  567 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/*  568 */     addComponent((XMLComponent)this.fDTDScanner);
/*      */ 
/*  570 */     this.fDTDProcessor = new XMLDTDProcessor();
/*  571 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/*  572 */     addComponent(this.fDTDProcessor);
/*      */ 
/*  574 */     this.fDTDValidator = new XMLNSDTDValidator();
/*  575 */     this.fProperties.put("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/*  576 */     addComponent(this.fDTDValidator);
/*      */ 
/*  578 */     this.fDatatypeValidatorFactory = DTDDVFactory.getInstance();
/*  579 */     this.fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*      */ 
/*  581 */     this.fValidationManager = new ValidationManager();
/*  582 */     this.fProperties.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*      */ 
/*  584 */     this.fVersionDetector = new XMLVersionDetector();
/*      */ 
/*  587 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/*  588 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/*  589 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/*  590 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  595 */       setLocale(Locale.getDefault());
/*      */     }
/*      */     catch (XNIException e)
/*      */     {
/*      */     }
/*      */ 
/*  601 */     this.fConfigUpdated = false;
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws XMLConfigurationException, IOException
/*      */   {
/*  628 */     this.fInputSource = inputSource;
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws XNIException
/*      */   {
/*  641 */     this.fLocale = locale;
/*  642 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  651 */     this.fDocumentHandler = documentHandler;
/*  652 */     if (this.fLastComponent != null) {
/*  653 */       this.fLastComponent.setDocumentHandler(this.fDocumentHandler);
/*  654 */       if (this.fDocumentHandler != null)
/*  655 */         this.fDocumentHandler.setDocumentSource(this.fLastComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  662 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler dtdHandler)
/*      */   {
/*  671 */     this.fDTDHandler = dtdHandler;
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/*  676 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelHandler(XMLDTDContentModelHandler handler)
/*      */   {
/*  685 */     this.fDTDContentModelHandler = handler;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelHandler getDTDContentModelHandler()
/*      */   {
/*  690 */     return this.fDTDContentModelHandler;
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(XMLEntityResolver resolver)
/*      */   {
/*  701 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-resolver", resolver);
/*      */   }
/*      */ 
/*      */   public XMLEntityResolver getEntityResolver()
/*      */   {
/*  712 */     return (XMLEntityResolver)this.fProperties.get("http://apache.org/xml/properties/internal/entity-resolver");
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*      */   {
/*  734 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*      */   }
/*      */ 
/*      */   public XMLErrorHandler getErrorHandler()
/*      */   {
/*  746 */     return (XMLErrorHandler)this.fProperties.get("http://apache.org/xml/properties/internal/error-handler");
/*      */   }
/*      */ 
/*      */   public void cleanup()
/*      */   {
/*  756 */     this.fEntityManager.closeReaders();
/*      */   }
/*      */ 
/*      */   public void parse(XMLInputSource source)
/*      */     throws XNIException, IOException
/*      */   {
/*  769 */     if (this.fParseInProgress)
/*      */     {
/*  771 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*      */     }
/*  773 */     this.fParseInProgress = true;
/*      */     try
/*      */     {
/*  776 */       setInputSource(source);
/*  777 */       parse(true);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  781 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  785 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  789 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  793 */       throw new XNIException(ex);
/*      */     } finally {
/*  795 */       this.fParseInProgress = false;
/*      */ 
/*  797 */       cleanup();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean parse(boolean complete)
/*      */     throws XNIException, IOException
/*      */   {
/*  805 */     if (this.fInputSource != null) {
/*      */       try {
/*  807 */         this.fValidationManager.reset();
/*  808 */         this.fVersionDetector.reset(this);
/*  809 */         this.fConfigUpdated = true;
/*  810 */         resetCommon();
/*      */ 
/*  812 */         short version = this.fVersionDetector.determineDocVersion(this.fInputSource);
/*  813 */         if (version == 2) {
/*  814 */           initXML11Components();
/*  815 */           configureXML11Pipeline();
/*  816 */           resetXML11();
/*      */         } else {
/*  818 */           configurePipeline();
/*  819 */           reset();
/*      */         }
/*      */ 
/*  823 */         this.fConfigUpdated = false;
/*      */ 
/*  826 */         this.fVersionDetector.startDocumentParsing((XMLEntityHandler)this.fCurrentScanner, version);
/*  827 */         this.fInputSource = null;
/*      */       }
/*      */       catch (XNIException ex)
/*      */       {
/*  831 */         throw ex;
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*  835 */         throw ex;
/*      */       }
/*      */       catch (RuntimeException ex)
/*      */       {
/*  839 */         throw ex;
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  843 */         throw new XNIException(ex);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  848 */       return this.fCurrentScanner.scanDocument(complete);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  852 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  856 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  860 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  864 */       throw new XNIException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public FeatureState getFeatureState(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  884 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/*  885 */       return FeatureState.is(this.fConfigUpdated);
/*      */     }
/*  887 */     return super.getFeatureState(featureId);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  906 */     this.fConfigUpdated = true;
/*      */ 
/*  908 */     int count = this.fComponents.size();
/*  909 */     for (int i = 0; i < count; i++) {
/*  910 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  911 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  914 */     count = this.fCommonComponents.size();
/*  915 */     for (int i = 0; i < count; i++) {
/*  916 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  917 */       c.setFeature(featureId, state);
/*      */     }
/*      */ 
/*  921 */     count = this.fXML11Components.size();
/*  922 */     for (int i = 0; i < count; i++) {
/*  923 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  925 */         c.setFeature(featureId, state);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  932 */     super.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public PropertyState getPropertyState(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  950 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/*  951 */       return PropertyState.is(getLocale());
/*      */     }
/*  953 */     return super.getPropertyState(propertyId);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  964 */     this.fConfigUpdated = true;
/*  965 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/*  966 */       setLocale((Locale)value);
/*      */     }
/*      */ 
/*  969 */     int count = this.fComponents.size();
/*  970 */     for (int i = 0; i < count; i++) {
/*  971 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/*  972 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  975 */     count = this.fCommonComponents.size();
/*  976 */     for (int i = 0; i < count; i++) {
/*  977 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/*  978 */       c.setProperty(propertyId, value);
/*      */     }
/*      */ 
/*  981 */     count = this.fXML11Components.size();
/*  982 */     for (int i = 0; i < count; i++) {
/*  983 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/*      */       try {
/*  985 */         c.setProperty(propertyId, value);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  993 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/* 1000 */     return this.fLocale;
/*      */   }
/*      */ 
/*      */   protected void reset()
/*      */     throws XNIException
/*      */   {
/* 1007 */     int count = this.fComponents.size();
/* 1008 */     for (int i = 0; i < count; i++) {
/* 1009 */       XMLComponent c = (XMLComponent)this.fComponents.get(i);
/* 1010 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetCommon()
/*      */     throws XNIException
/*      */   {
/* 1020 */     int count = this.fCommonComponents.size();
/* 1021 */     for (int i = 0; i < count; i++) {
/* 1022 */       XMLComponent c = (XMLComponent)this.fCommonComponents.get(i);
/* 1023 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void resetXML11()
/*      */     throws XNIException
/*      */   {
/* 1034 */     int count = this.fXML11Components.size();
/* 1035 */     for (int i = 0; i < count; i++) {
/* 1036 */       XMLComponent c = (XMLComponent)this.fXML11Components.get(i);
/* 1037 */       c.reset(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configureXML11Pipeline()
/*      */   {
/* 1048 */     if (this.fCurrentDVFactory != this.fXML11DatatypeFactory) {
/* 1049 */       this.fCurrentDVFactory = this.fXML11DatatypeFactory;
/* 1050 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/* 1052 */     if (this.fCurrentDTDScanner != this.fXML11DTDScanner) {
/* 1053 */       this.fCurrentDTDScanner = this.fXML11DTDScanner;
/* 1054 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/* 1055 */       setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/* 1058 */     this.fXML11DTDScanner.setDTDHandler(this.fXML11DTDProcessor);
/* 1059 */     this.fXML11DTDProcessor.setDTDSource(this.fXML11DTDScanner);
/* 1060 */     this.fXML11DTDProcessor.setDTDHandler(this.fDTDHandler);
/* 1061 */     if (this.fDTDHandler != null) {
/* 1062 */       this.fDTDHandler.setDTDSource(this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/* 1065 */     this.fXML11DTDScanner.setDTDContentModelHandler(this.fXML11DTDProcessor);
/* 1066 */     this.fXML11DTDProcessor.setDTDContentModelSource(this.fXML11DTDScanner);
/* 1067 */     this.fXML11DTDProcessor.setDTDContentModelHandler(this.fDTDContentModelHandler);
/* 1068 */     if (this.fDTDContentModelHandler != null) {
/* 1069 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fXML11DTDProcessor);
/*      */     }
/*      */ 
/* 1073 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 1074 */       if (this.fCurrentScanner != this.fXML11NSDocScanner) {
/* 1075 */         this.fCurrentScanner = this.fXML11NSDocScanner;
/* 1076 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11NSDocScanner);
/* 1077 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fXML11NSDTDValidator);
/*      */       }
/*      */ 
/* 1080 */       this.fXML11NSDocScanner.setDTDValidator(this.fXML11NSDTDValidator);
/* 1081 */       this.fXML11NSDocScanner.setDocumentHandler(this.fXML11NSDTDValidator);
/* 1082 */       this.fXML11NSDTDValidator.setDocumentSource(this.fXML11NSDocScanner);
/* 1083 */       this.fXML11NSDTDValidator.setDocumentHandler(this.fDocumentHandler);
/*      */ 
/* 1085 */       if (this.fDocumentHandler != null) {
/* 1086 */         this.fDocumentHandler.setDocumentSource(this.fXML11NSDTDValidator);
/*      */       }
/* 1088 */       this.fLastComponent = this.fXML11NSDTDValidator;
/*      */     }
/*      */     else
/*      */     {
/* 1092 */       if (this.fXML11DocScanner == null)
/*      */       {
/* 1094 */         this.fXML11DocScanner = new XML11DocumentScannerImpl();
/* 1095 */         addXML11Component(this.fXML11DocScanner);
/* 1096 */         this.fXML11DTDValidator = new XML11DTDValidator();
/* 1097 */         addXML11Component(this.fXML11DTDValidator);
/*      */       }
/* 1099 */       if (this.fCurrentScanner != this.fXML11DocScanner) {
/* 1100 */         this.fCurrentScanner = this.fXML11DocScanner;
/* 1101 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fXML11DocScanner);
/* 1102 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fXML11DTDValidator);
/*      */       }
/* 1104 */       this.fXML11DocScanner.setDocumentHandler(this.fXML11DTDValidator);
/* 1105 */       this.fXML11DTDValidator.setDocumentSource(this.fXML11DocScanner);
/* 1106 */       this.fXML11DTDValidator.setDocumentHandler(this.fDocumentHandler);
/*      */ 
/* 1108 */       if (this.fDocumentHandler != null) {
/* 1109 */         this.fDocumentHandler.setDocumentSource(this.fXML11DTDValidator);
/*      */       }
/* 1111 */       this.fLastComponent = this.fXML11DTDValidator;
/*      */     }
/*      */ 
/* 1115 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*      */     {
/* 1117 */       if (this.fSchemaValidator == null) {
/* 1118 */         this.fSchemaValidator = new XMLSchemaValidator();
/*      */ 
/* 1120 */         setProperty("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
/* 1121 */         addCommonComponent(this.fSchemaValidator);
/* 1122 */         this.fSchemaValidator.reset(this);
/*      */ 
/* 1124 */         if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/* 1125 */           XSMessageFormatter xmft = new XSMessageFormatter();
/* 1126 */           this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*      */         }
/*      */       }
/*      */ 
/* 1130 */       this.fLastComponent.setDocumentHandler(this.fSchemaValidator);
/* 1131 */       this.fSchemaValidator.setDocumentSource(this.fLastComponent);
/* 1132 */       this.fSchemaValidator.setDocumentHandler(this.fDocumentHandler);
/* 1133 */       if (this.fDocumentHandler != null) {
/* 1134 */         this.fDocumentHandler.setDocumentSource(this.fSchemaValidator);
/*      */       }
/* 1136 */       this.fLastComponent = this.fSchemaValidator;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void configurePipeline()
/*      */   {
/* 1143 */     if (this.fCurrentDVFactory != this.fDatatypeValidatorFactory) {
/* 1144 */       this.fCurrentDVFactory = this.fDatatypeValidatorFactory;
/*      */ 
/* 1146 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/* 1150 */     if (this.fCurrentDTDScanner != this.fDTDScanner) {
/* 1151 */       this.fCurrentDTDScanner = this.fDTDScanner;
/* 1152 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/* 1153 */       setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/*      */     }
/* 1155 */     this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 1156 */     this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/* 1157 */     this.fDTDProcessor.setDTDHandler(this.fDTDHandler);
/* 1158 */     if (this.fDTDHandler != null) {
/* 1159 */       this.fDTDHandler.setDTDSource(this.fDTDProcessor);
/*      */     }
/*      */ 
/* 1162 */     this.fDTDScanner.setDTDContentModelHandler(this.fDTDProcessor);
/* 1163 */     this.fDTDProcessor.setDTDContentModelSource(this.fDTDScanner);
/* 1164 */     this.fDTDProcessor.setDTDContentModelHandler(this.fDTDContentModelHandler);
/* 1165 */     if (this.fDTDContentModelHandler != null) {
/* 1166 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDProcessor);
/*      */     }
/*      */ 
/* 1170 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 1171 */       if (this.fCurrentScanner != this.fNamespaceScanner) {
/* 1172 */         this.fCurrentScanner = this.fNamespaceScanner;
/* 1173 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/* 1174 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/*      */       }
/* 1176 */       this.fNamespaceScanner.setDTDValidator(this.fDTDValidator);
/* 1177 */       this.fNamespaceScanner.setDocumentHandler(this.fDTDValidator);
/* 1178 */       this.fDTDValidator.setDocumentSource(this.fNamespaceScanner);
/* 1179 */       this.fDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 1180 */       if (this.fDocumentHandler != null) {
/* 1181 */         this.fDocumentHandler.setDocumentSource(this.fDTDValidator);
/*      */       }
/* 1183 */       this.fLastComponent = this.fDTDValidator;
/*      */     }
/*      */     else {
/* 1186 */       if (this.fNonNSScanner == null) {
/* 1187 */         this.fNonNSScanner = new XMLDocumentScannerImpl();
/* 1188 */         this.fNonNSDTDValidator = new XMLDTDValidator();
/*      */ 
/* 1190 */         addComponent(this.fNonNSScanner);
/* 1191 */         addComponent(this.fNonNSDTDValidator);
/*      */       }
/* 1193 */       if (this.fCurrentScanner != this.fNonNSScanner) {
/* 1194 */         this.fCurrentScanner = this.fNonNSScanner;
/* 1195 */         setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fNonNSScanner);
/* 1196 */         setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fNonNSDTDValidator);
/*      */       }
/*      */ 
/* 1199 */       this.fNonNSScanner.setDocumentHandler(this.fNonNSDTDValidator);
/* 1200 */       this.fNonNSDTDValidator.setDocumentSource(this.fNonNSScanner);
/* 1201 */       this.fNonNSDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 1202 */       if (this.fDocumentHandler != null) {
/* 1203 */         this.fDocumentHandler.setDocumentSource(this.fNonNSDTDValidator);
/*      */       }
/* 1205 */       this.fLastComponent = this.fNonNSDTDValidator;
/*      */     }
/*      */ 
/* 1209 */     if (this.fFeatures.get("http://apache.org/xml/features/validation/schema") == Boolean.TRUE)
/*      */     {
/* 1211 */       if (this.fSchemaValidator == null) {
/* 1212 */         this.fSchemaValidator = new XMLSchemaValidator();
/*      */ 
/* 1214 */         setProperty("http://apache.org/xml/properties/internal/validator/schema", this.fSchemaValidator);
/* 1215 */         addCommonComponent(this.fSchemaValidator);
/* 1216 */         this.fSchemaValidator.reset(this);
/*      */ 
/* 1218 */         if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/* 1219 */           XSMessageFormatter xmft = new XSMessageFormatter();
/* 1220 */           this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*      */         }
/*      */       }
/*      */ 
/* 1224 */       this.fLastComponent.setDocumentHandler(this.fSchemaValidator);
/* 1225 */       this.fSchemaValidator.setDocumentSource(this.fLastComponent);
/* 1226 */       this.fSchemaValidator.setDocumentHandler(this.fDocumentHandler);
/* 1227 */       if (this.fDocumentHandler != null) {
/* 1228 */         this.fDocumentHandler.setDocumentSource(this.fSchemaValidator);
/*      */       }
/* 1230 */       this.fLastComponent = this.fSchemaValidator;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected FeatureState checkFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1255 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 1256 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*      */ 
/* 1264 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*      */       {
/* 1266 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1272 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*      */       {
/* 1275 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1280 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*      */       {
/* 1283 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1288 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*      */       {
/* 1290 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1295 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/* 1297 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1303 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*      */       {
/* 1305 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/* 1312 */       if ((suffixLength == "validation/schema".length()) && (featureId.endsWith("validation/schema")))
/*      */       {
/* 1314 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1317 */       if ((suffixLength == "validation/schema-full-checking".length()) && (featureId.endsWith("validation/schema-full-checking")))
/*      */       {
/* 1319 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1323 */       if ((suffixLength == "validation/schema/normalized-value".length()) && (featureId.endsWith("validation/schema/normalized-value")))
/*      */       {
/* 1325 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1329 */       if ((suffixLength == "validation/schema/element-default".length()) && (featureId.endsWith("validation/schema/element-default")))
/*      */       {
/* 1331 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/* 1335 */       if ((suffixLength == "internal/parser-settings".length()) && (featureId.endsWith("internal/parser-settings")))
/*      */       {
/* 1337 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1346 */     return super.checkFeature(featureId);
/*      */   }
/*      */ 
/*      */   protected PropertyState checkProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1369 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 1370 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/* 1372 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*      */       {
/* 1374 */         return PropertyState.RECOGNIZED;
/*      */       }
/* 1376 */       if ((suffixLength == "schema/external-schemaLocation".length()) && (propertyId.endsWith("schema/external-schemaLocation")))
/*      */       {
/* 1378 */         return PropertyState.RECOGNIZED;
/*      */       }
/* 1380 */       if ((suffixLength == "schema/external-noNamespaceSchemaLocation".length()) && (propertyId.endsWith("schema/external-noNamespaceSchemaLocation")))
/*      */       {
/* 1382 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */     }
/*      */ 
/* 1386 */     if (propertyId.startsWith("http://java.sun.com/xml/jaxp/properties/")) {
/* 1387 */       int suffixLength = propertyId.length() - "http://java.sun.com/xml/jaxp/properties/".length();
/*      */ 
/* 1389 */       if ((suffixLength == "schemaSource".length()) && (propertyId.endsWith("schemaSource")))
/*      */       {
/* 1391 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1396 */     if (propertyId.startsWith("http://xml.org/sax/properties/")) {
/* 1397 */       int suffixLength = propertyId.length() - "http://xml.org/sax/properties/".length();
/*      */ 
/* 1409 */       if ((suffixLength == "xml-string".length()) && (propertyId.endsWith("xml-string")))
/*      */       {
/* 1414 */         return PropertyState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1422 */     return super.checkProperty(propertyId);
/*      */   }
/*      */ 
/*      */   protected void addComponent(XMLComponent component)
/*      */   {
/* 1437 */     if (this.fComponents.contains(component)) {
/* 1438 */       return;
/*      */     }
/* 1440 */     this.fComponents.add(component);
/* 1441 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addCommonComponent(XMLComponent component)
/*      */   {
/* 1455 */     if (this.fCommonComponents.contains(component)) {
/* 1456 */       return;
/*      */     }
/* 1458 */     this.fCommonComponents.add(component);
/* 1459 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addXML11Component(XMLComponent component)
/*      */   {
/* 1473 */     if (this.fXML11Components.contains(component)) {
/* 1474 */       return;
/*      */     }
/* 1476 */     this.fXML11Components.add(component);
/* 1477 */     addRecognizedParamsAndSetDefaults(component);
/*      */   }
/*      */ 
/*      */   protected void addRecognizedParamsAndSetDefaults(XMLComponent component)
/*      */   {
/* 1493 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/* 1494 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/* 1497 */     String[] recognizedProperties = component.getRecognizedProperties();
/* 1498 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/* 1501 */     if (recognizedFeatures != null) {
/* 1502 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/* 1503 */         String featureId = recognizedFeatures[i];
/* 1504 */         Boolean state = component.getFeatureDefault(featureId);
/* 1505 */         if (state != null)
/*      */         {
/* 1507 */           if (!this.fFeatures.containsKey(featureId)) {
/* 1508 */             this.fFeatures.put(featureId, state);
/*      */ 
/* 1513 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1518 */     if (recognizedProperties != null)
/* 1519 */       for (int i = 0; i < recognizedProperties.length; i++) {
/* 1520 */         String propertyId = recognizedProperties[i];
/* 1521 */         Object value = component.getPropertyDefault(propertyId);
/* 1522 */         if (value != null)
/*      */         {
/* 1524 */           if (!this.fProperties.containsKey(propertyId)) {
/* 1525 */             this.fProperties.put(propertyId, value);
/*      */ 
/* 1530 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void initXML11Components()
/*      */   {
/* 1538 */     if (!this.f11Initialized)
/*      */     {
/* 1541 */       this.fXML11DatatypeFactory = DTDDVFactory.getInstance("com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl");
/*      */ 
/* 1544 */       this.fXML11DTDScanner = new XML11DTDScannerImpl();
/* 1545 */       addXML11Component(this.fXML11DTDScanner);
/* 1546 */       this.fXML11DTDProcessor = new XML11DTDProcessor();
/* 1547 */       addXML11Component(this.fXML11DTDProcessor);
/*      */ 
/* 1550 */       this.fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
/* 1551 */       addXML11Component(this.fXML11NSDocScanner);
/* 1552 */       this.fXML11NSDTDValidator = new XML11NSDTDValidator();
/* 1553 */       addXML11Component(this.fXML11NSDTDValidator);
/*      */ 
/* 1555 */       this.f11Initialized = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   FeatureState getFeatureState0(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1566 */     return super.getFeatureState(featureId);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XML11Configuration
 * JD-Core Version:    0.6.2
 */