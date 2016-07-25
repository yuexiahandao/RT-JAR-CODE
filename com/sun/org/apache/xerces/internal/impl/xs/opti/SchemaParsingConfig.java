/*      */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XML11DTDScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XML11NSDocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLVersionDetector;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.parsers.BasicParserConfiguration;
/*      */ import com.sun.org.apache.xerces.internal.util.FeatureState;
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
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration;
/*      */ import java.io.IOException;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class SchemaParsingConfig extends BasicParserConfiguration
/*      */   implements XMLPullParserConfiguration
/*      */ {
/*      */   protected static final String XML11_DATATYPE_VALIDATOR_FACTORY = "com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl";
/*      */   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
/*      */   protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*      */   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/*      */   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/*      */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*      */   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*      */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*      */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*      */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*      */   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
/*      */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*      */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*      */   private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*      */   protected final DTDDVFactory fDatatypeValidatorFactory;
/*      */   protected final XMLNSDocumentScannerImpl fNamespaceScanner;
/*      */   protected final XMLDTDScannerImpl fDTDScanner;
/*  191 */   protected DTDDVFactory fXML11DatatypeFactory = null;
/*      */ 
/*  194 */   protected XML11NSDocumentScannerImpl fXML11NSDocScanner = null;
/*      */ 
/*  197 */   protected XML11DTDScannerImpl fXML11DTDScanner = null;
/*      */   protected DTDDVFactory fCurrentDVFactory;
/*      */   protected XMLDocumentScanner fCurrentScanner;
/*      */   protected XMLDTDScanner fCurrentDTDScanner;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected final XMLVersionDetector fVersionDetector;
/*      */   protected final XMLErrorReporter fErrorReporter;
/*      */   protected final XMLEntityManager fEntityManager;
/*      */   protected XMLInputSource fInputSource;
/*      */   protected final ValidationManager fValidationManager;
/*      */   protected XMLLocator fLocator;
/*  238 */   protected boolean fParseInProgress = false;
/*      */ 
/*  244 */   protected boolean fConfigUpdated = false;
/*      */ 
/*  247 */   private boolean f11Initialized = false;
/*      */ 
/*      */   public SchemaParsingConfig()
/*      */   {
/*  255 */     this(null, null, null);
/*      */   }
/*      */ 
/*      */   public SchemaParsingConfig(SymbolTable symbolTable)
/*      */   {
/*  264 */     this(symbolTable, null, null);
/*      */   }
/*      */ 
/*      */   public SchemaParsingConfig(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*      */   {
/*  280 */     this(symbolTable, grammarPool, null);
/*      */   }
/*      */ 
/*      */   public SchemaParsingConfig(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*      */   {
/*  298 */     super(symbolTable, parentSettings);
/*      */ 
/*  301 */     String[] recognizedFeatures = { "http://apache.org/xml/features/internal/parser-settings", "http://apache.org/xml/features/validation/warn-on-duplicate-attdef", "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd", "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://apache.org/xml/features/scanner/notify-char-refs", "http://apache.org/xml/features/generate-synthetic-annotations" };
/*      */ 
/*  307 */     addRecognizedFeatures(recognizedFeatures);
/*  308 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/*      */ 
/*  310 */     this.fFeatures.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", Boolean.FALSE);
/*      */ 
/*  312 */     this.fFeatures.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", Boolean.FALSE);
/*  313 */     this.fFeatures.put("http://apache.org/xml/features/allow-java-encodings", Boolean.FALSE);
/*  314 */     this.fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
/*  315 */     this.fFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.TRUE);
/*  316 */     this.fFeatures.put("http://apache.org/xml/features/scanner/notify-builtin-refs", Boolean.FALSE);
/*  317 */     this.fFeatures.put("http://apache.org/xml/features/scanner/notify-char-refs", Boolean.FALSE);
/*  318 */     this.fFeatures.put("http://apache.org/xml/features/generate-synthetic-annotations", Boolean.FALSE);
/*      */ 
/*  321 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/properties/locale" };
/*      */ 
/*  334 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  336 */     this.fGrammarPool = grammarPool;
/*  337 */     if (this.fGrammarPool != null) {
/*  338 */       setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*      */     }
/*      */ 
/*  341 */     this.fEntityManager = new XMLEntityManager();
/*  342 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*  343 */     addComponent(this.fEntityManager);
/*      */ 
/*  345 */     this.fErrorReporter = new XMLErrorReporter();
/*  346 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/*  347 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  348 */     addComponent(this.fErrorReporter);
/*      */ 
/*  350 */     this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/*  351 */     this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/*  352 */     addRecognizedParamsAndSetDefaults(this.fNamespaceScanner);
/*      */ 
/*  354 */     this.fDTDScanner = new XMLDTDScannerImpl();
/*  355 */     this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/*  356 */     addRecognizedParamsAndSetDefaults(this.fDTDScanner);
/*      */ 
/*  358 */     this.fDatatypeValidatorFactory = DTDDVFactory.getInstance();
/*  359 */     this.fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*      */ 
/*  362 */     this.fValidationManager = new ValidationManager();
/*  363 */     this.fProperties.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*      */ 
/*  365 */     this.fVersionDetector = new XMLVersionDetector();
/*      */ 
/*  368 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/*  369 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/*  370 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/*  371 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*      */     }
/*      */ 
/*  374 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/*  375 */       XSMessageFormatter xmft = new XSMessageFormatter();
/*  376 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", xmft);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  381 */       setLocale(Locale.getDefault());
/*      */     }
/*      */     catch (XNIException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public FeatureState getFeatureState(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  409 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/*  410 */       return FeatureState.is(this.fConfigUpdated);
/*      */     }
/*  412 */     return super.getFeatureState(featureId);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  432 */     this.fConfigUpdated = true;
/*      */ 
/*  435 */     this.fNamespaceScanner.setFeature(featureId, state);
/*  436 */     this.fDTDScanner.setFeature(featureId, state);
/*      */ 
/*  439 */     if (this.f11Initialized) {
/*      */       try {
/*  441 */         this.fXML11DTDScanner.setFeature(featureId, state);
/*      */       }
/*      */       catch (Exception e) {
/*      */       }
/*      */       try {
/*  446 */         this.fXML11NSDocScanner.setFeature(featureId, state);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  453 */     super.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public PropertyState getPropertyState(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  471 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/*  472 */       return PropertyState.is(getLocale());
/*      */     }
/*  474 */     return super.getPropertyState(propertyId);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  486 */     this.fConfigUpdated = true;
/*  487 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/*  488 */       setLocale((Locale)value);
/*      */     }
/*      */ 
/*  492 */     this.fNamespaceScanner.setProperty(propertyId, value);
/*  493 */     this.fDTDScanner.setProperty(propertyId, value);
/*      */ 
/*  496 */     if (this.f11Initialized) {
/*      */       try {
/*  498 */         this.fXML11DTDScanner.setProperty(propertyId, value);
/*      */       }
/*      */       catch (Exception e) {
/*      */       }
/*      */       try {
/*  503 */         this.fXML11NSDocScanner.setProperty(propertyId, value);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  510 */     super.setProperty(propertyId, value);
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */     throws XNIException
/*      */   {
/*  523 */     super.setLocale(locale);
/*  524 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public void setInputSource(XMLInputSource inputSource)
/*      */     throws XMLConfigurationException, IOException
/*      */   {
/*  553 */     this.fInputSource = inputSource;
/*      */   }
/*      */ 
/*      */   public boolean parse(boolean complete)
/*      */     throws XNIException, IOException
/*      */   {
/*  576 */     if (this.fInputSource != null) {
/*      */       try {
/*  578 */         this.fValidationManager.reset();
/*  579 */         this.fVersionDetector.reset(this);
/*  580 */         reset();
/*      */ 
/*  582 */         short version = this.fVersionDetector.determineDocVersion(this.fInputSource);
/*      */ 
/*  584 */         if (version == 1) {
/*  585 */           configurePipeline();
/*  586 */           resetXML10();
/*      */         }
/*  589 */         else if (version == 2) {
/*  590 */           initXML11Components();
/*  591 */           configureXML11Pipeline();
/*  592 */           resetXML11();
/*      */         }
/*      */         else
/*      */         {
/*  596 */           return false;
/*      */         }
/*      */ 
/*  600 */         this.fConfigUpdated = false;
/*      */ 
/*  603 */         this.fVersionDetector.startDocumentParsing((XMLEntityHandler)this.fCurrentScanner, version);
/*  604 */         this.fInputSource = null;
/*      */       }
/*      */       catch (XNIException ex)
/*      */       {
/*  609 */         throw ex;
/*      */       }
/*      */       catch (IOException ex)
/*      */       {
/*  614 */         throw ex;
/*      */       }
/*      */       catch (RuntimeException ex)
/*      */       {
/*  619 */         throw ex;
/*      */       }
/*      */       catch (Exception ex)
/*      */       {
/*  624 */         throw new XNIException(ex);
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  629 */       return this.fCurrentScanner.scanDocument(complete);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  634 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  639 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  644 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  649 */       throw new XNIException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void cleanup()
/*      */   {
/*  660 */     this.fEntityManager.closeReaders();
/*      */   }
/*      */ 
/*      */   public void parse(XMLInputSource source)
/*      */     throws XNIException, IOException
/*      */   {
/*  677 */     if (this.fParseInProgress)
/*      */     {
/*  679 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*      */     }
/*  681 */     this.fParseInProgress = true;
/*      */     try
/*      */     {
/*  684 */       setInputSource(source);
/*  685 */       parse(true);
/*      */     }
/*      */     catch (XNIException ex)
/*      */     {
/*  690 */       throw ex;
/*      */     }
/*      */     catch (IOException ex)
/*      */     {
/*  695 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  700 */       throw ex;
/*      */     }
/*      */     catch (Exception ex)
/*      */     {
/*  705 */       throw new XNIException(ex);
/*      */     }
/*      */     finally {
/*  708 */       this.fParseInProgress = false;
/*      */ 
/*  710 */       cleanup();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws XNIException
/*      */   {
/*  727 */     super.reset();
/*      */   }
/*      */ 
/*      */   protected void configurePipeline()
/*      */   {
/*  734 */     if (this.fCurrentDVFactory != this.fDatatypeValidatorFactory) {
/*  735 */       this.fCurrentDVFactory = this.fDatatypeValidatorFactory;
/*      */ 
/*  737 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/*  741 */     if (this.fCurrentScanner != this.fNamespaceScanner) {
/*  742 */       this.fCurrentScanner = this.fNamespaceScanner;
/*  743 */       setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fCurrentScanner);
/*      */     }
/*  745 */     this.fNamespaceScanner.setDocumentHandler(this.fDocumentHandler);
/*  746 */     if (this.fDocumentHandler != null) {
/*  747 */       this.fDocumentHandler.setDocumentSource(this.fNamespaceScanner);
/*      */     }
/*  749 */     this.fLastComponent = this.fNamespaceScanner;
/*      */ 
/*  752 */     if (this.fCurrentDTDScanner != this.fDTDScanner) {
/*  753 */       this.fCurrentDTDScanner = this.fDTDScanner;
/*  754 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*      */     }
/*  756 */     this.fDTDScanner.setDTDHandler(this.fDTDHandler);
/*  757 */     if (this.fDTDHandler != null) {
/*  758 */       this.fDTDHandler.setDTDSource(this.fDTDScanner);
/*      */     }
/*  760 */     this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*  761 */     if (this.fDTDContentModelHandler != null)
/*  762 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDScanner);
/*      */   }
/*      */ 
/*      */   protected void configureXML11Pipeline()
/*      */   {
/*  770 */     if (this.fCurrentDVFactory != this.fXML11DatatypeFactory) {
/*  771 */       this.fCurrentDVFactory = this.fXML11DatatypeFactory;
/*      */ 
/*  773 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fCurrentDVFactory);
/*      */     }
/*      */ 
/*  777 */     if (this.fCurrentScanner != this.fXML11NSDocScanner) {
/*  778 */       this.fCurrentScanner = this.fXML11NSDocScanner;
/*  779 */       setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fCurrentScanner);
/*      */     }
/*  781 */     this.fXML11NSDocScanner.setDocumentHandler(this.fDocumentHandler);
/*  782 */     if (this.fDocumentHandler != null) {
/*  783 */       this.fDocumentHandler.setDocumentSource(this.fXML11NSDocScanner);
/*      */     }
/*  785 */     this.fLastComponent = this.fXML11NSDocScanner;
/*      */ 
/*  788 */     if (this.fCurrentDTDScanner != this.fXML11DTDScanner) {
/*  789 */       this.fCurrentDTDScanner = this.fXML11DTDScanner;
/*  790 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fCurrentDTDScanner);
/*      */     }
/*  792 */     this.fXML11DTDScanner.setDTDHandler(this.fDTDHandler);
/*  793 */     if (this.fDTDHandler != null) {
/*  794 */       this.fDTDHandler.setDTDSource(this.fXML11DTDScanner);
/*      */     }
/*  796 */     this.fXML11DTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*  797 */     if (this.fDTDContentModelHandler != null)
/*  798 */       this.fDTDContentModelHandler.setDTDContentModelSource(this.fXML11DTDScanner);
/*      */   }
/*      */ 
/*      */   protected FeatureState checkFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  824 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/*  825 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*      */ 
/*  833 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*      */       {
/*  835 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/*  840 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*      */       {
/*  843 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*  848 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*      */       {
/*  851 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*  856 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*      */       {
/*  858 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/*  863 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*      */       {
/*  865 */         return FeatureState.RECOGNIZED;
/*      */       }
/*      */ 
/*  871 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*      */       {
/*  873 */         return FeatureState.NOT_SUPPORTED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  881 */     return super.checkFeature(featureId);
/*      */   }
/*      */ 
/*      */   protected PropertyState checkProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  905 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/*  906 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*      */ 
/*  908 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*      */       {
/*  910 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */     }
/*      */ 
/*  914 */     if (propertyId.startsWith("http://java.sun.com/xml/jaxp/properties/")) {
/*  915 */       int suffixLength = propertyId.length() - "http://java.sun.com/xml/jaxp/properties/".length();
/*      */ 
/*  917 */       if ((suffixLength == "schemaSource".length()) && (propertyId.endsWith("schemaSource")))
/*      */       {
/*  919 */         return PropertyState.RECOGNIZED;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  927 */     return super.checkProperty(propertyId);
/*      */   }
/*      */ 
/*      */   private void addRecognizedParamsAndSetDefaults(XMLComponent component)
/*      */   {
/*  943 */     String[] recognizedFeatures = component.getRecognizedFeatures();
/*  944 */     addRecognizedFeatures(recognizedFeatures);
/*      */ 
/*  947 */     String[] recognizedProperties = component.getRecognizedProperties();
/*  948 */     addRecognizedProperties(recognizedProperties);
/*      */ 
/*  951 */     if (recognizedFeatures != null) {
/*  952 */       for (int i = 0; i < recognizedFeatures.length; i++) {
/*  953 */         String featureId = recognizedFeatures[i];
/*  954 */         Boolean state = component.getFeatureDefault(featureId);
/*  955 */         if (state != null)
/*      */         {
/*  957 */           if (!this.fFeatures.containsKey(featureId)) {
/*  958 */             this.fFeatures.put(featureId, state);
/*      */ 
/*  963 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  968 */     if (recognizedProperties != null)
/*  969 */       for (int i = 0; i < recognizedProperties.length; i++) {
/*  970 */         String propertyId = recognizedProperties[i];
/*  971 */         Object value = component.getPropertyDefault(propertyId);
/*  972 */         if (value != null)
/*      */         {
/*  974 */           if (!this.fProperties.containsKey(propertyId)) {
/*  975 */             this.fProperties.put(propertyId, value);
/*      */ 
/*  980 */             this.fConfigUpdated = true;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   protected final void resetXML10()
/*      */     throws XNIException
/*      */   {
/*  992 */     this.fNamespaceScanner.reset(this);
/*  993 */     this.fDTDScanner.reset(this);
/*      */   }
/*      */ 
/*      */   protected final void resetXML11()
/*      */     throws XNIException
/*      */   {
/* 1001 */     this.fXML11NSDocScanner.reset(this);
/* 1002 */     this.fXML11DTDScanner.reset(this);
/*      */   }
/*      */ 
/*      */   public void resetNodePool()
/*      */   {
/*      */   }
/*      */ 
/*      */   private void initXML11Components()
/*      */   {
/* 1016 */     if (!this.f11Initialized)
/*      */     {
/* 1018 */       this.fXML11DatatypeFactory = DTDDVFactory.getInstance("com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl");
/*      */ 
/* 1021 */       this.fXML11DTDScanner = new XML11DTDScannerImpl();
/* 1022 */       addRecognizedParamsAndSetDefaults(this.fXML11DTDScanner);
/*      */ 
/* 1025 */       this.fXML11NSDocScanner = new XML11NSDocumentScannerImpl();
/* 1026 */       addRecognizedParamsAndSetDefaults(this.fXML11NSDocScanner);
/*      */ 
/* 1028 */       this.f11Initialized = true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaParsingConfig
 * JD-Core Version:    0.6.2
 */