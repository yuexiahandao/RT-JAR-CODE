/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLNamespaceBinder;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLPullParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DTDConfiguration extends BasicParserConfiguration
/*     */   implements XMLPullParserConfiguration
/*     */ {
/*     */   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
/*     */   protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
/*     */   protected static final String WARN_ON_UNDECLARED_ELEMDEF = "http://apache.org/xml/features/validation/warn-on-undeclared-elemdef";
/*     */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*     */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*     */   protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
/*     */   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/*     */   protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*     */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*     */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*     */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*     */   protected static final String DTD_PROCESSOR = "http://apache.org/xml/properties/internal/dtd-processor";
/*     */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*     */   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
/*     */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*     */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*     */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*     */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*     */   protected static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   protected static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*     */   protected XMLGrammarPool fGrammarPool;
/*     */   protected DTDDVFactory fDatatypeValidatorFactory;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLEntityManager fEntityManager;
/*     */   protected XMLDocumentScanner fScanner;
/*     */   protected XMLInputSource fInputSource;
/*     */   protected XMLDTDScanner fDTDScanner;
/*     */   protected XMLDTDProcessor fDTDProcessor;
/*     */   protected XMLDTDValidator fDTDValidator;
/*     */   protected XMLNamespaceBinder fNamespaceBinder;
/*     */   protected ValidationManager fValidationManager;
/*     */   protected XMLLocator fLocator;
/* 248 */   protected boolean fParseInProgress = false;
/*     */ 
/*     */   public DTDConfiguration()
/*     */   {
/* 256 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public DTDConfiguration(SymbolTable symbolTable)
/*     */   {
/* 265 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public DTDConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 281 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public DTDConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 299 */     super(symbolTable, parentSettings);
/*     */ 
/* 302 */     String[] recognizedFeatures = { "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/nonvalidating/load-external-dtd" };
/*     */ 
/* 312 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 318 */     setFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
/* 319 */     setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
/*     */ 
/* 325 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/dtd-processor", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/locale", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*     */ 
/* 342 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 344 */     this.fGrammarPool = grammarPool;
/* 345 */     if (this.fGrammarPool != null) {
/* 346 */       setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*     */     }
/*     */ 
/* 349 */     this.fEntityManager = createEntityManager();
/* 350 */     setProperty("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/* 351 */     addComponent(this.fEntityManager);
/*     */ 
/* 353 */     this.fErrorReporter = createErrorReporter();
/* 354 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/* 355 */     setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/* 356 */     addComponent(this.fErrorReporter);
/*     */ 
/* 358 */     this.fScanner = createDocumentScanner();
/* 359 */     setProperty("http://apache.org/xml/properties/internal/document-scanner", this.fScanner);
/* 360 */     if ((this.fScanner instanceof XMLComponent)) {
/* 361 */       addComponent((XMLComponent)this.fScanner);
/*     */     }
/*     */ 
/* 364 */     this.fDTDScanner = createDTDScanner();
/* 365 */     if (this.fDTDScanner != null) {
/* 366 */       setProperty("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/* 367 */       if ((this.fDTDScanner instanceof XMLComponent)) {
/* 368 */         addComponent((XMLComponent)this.fDTDScanner);
/*     */       }
/*     */     }
/*     */ 
/* 372 */     this.fDTDProcessor = createDTDProcessor();
/* 373 */     if (this.fDTDProcessor != null) {
/* 374 */       setProperty("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/* 375 */       if ((this.fDTDProcessor instanceof XMLComponent)) {
/* 376 */         addComponent(this.fDTDProcessor);
/*     */       }
/*     */     }
/*     */ 
/* 380 */     this.fDTDValidator = createDTDValidator();
/* 381 */     if (this.fDTDValidator != null) {
/* 382 */       setProperty("http://apache.org/xml/properties/internal/validator/dtd", this.fDTDValidator);
/* 383 */       addComponent(this.fDTDValidator);
/*     */     }
/*     */ 
/* 386 */     this.fNamespaceBinder = createNamespaceBinder();
/* 387 */     if (this.fNamespaceBinder != null) {
/* 388 */       setProperty("http://apache.org/xml/properties/internal/namespace-binder", this.fNamespaceBinder);
/* 389 */       addComponent(this.fNamespaceBinder);
/*     */     }
/*     */ 
/* 392 */     this.fDatatypeValidatorFactory = createDatatypeValidatorFactory();
/* 393 */     if (this.fDatatypeValidatorFactory != null) {
/* 394 */       setProperty("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*     */     }
/*     */ 
/* 397 */     this.fValidationManager = createValidationManager();
/*     */ 
/* 399 */     if (this.fValidationManager != null) {
/* 400 */       setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*     */     }
/*     */ 
/* 403 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/* 404 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/* 405 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/* 406 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 411 */       setLocale(Locale.getDefault());
/*     */     }
/*     */     catch (XNIException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public PropertyState getPropertyState(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 425 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 426 */       return PropertyState.is(getLocale());
/*     */     }
/* 428 */     return super.getPropertyState(propertyId);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value) throws XMLConfigurationException
/*     */   {
/* 433 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 434 */       setLocale((Locale)value);
/*     */     }
/* 436 */     super.setProperty(propertyId, value);
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */     throws XNIException
/*     */   {
/* 448 */     super.setLocale(locale);
/* 449 */     this.fErrorReporter.setLocale(locale);
/*     */   }
/*     */ 
/*     */   public void setInputSource(XMLInputSource inputSource)
/*     */     throws XMLConfigurationException, IOException
/*     */   {
/* 478 */     this.fInputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public boolean parse(boolean complete)
/*     */     throws XNIException, IOException
/*     */   {
/* 501 */     if (this.fInputSource != null) {
/*     */       try
/*     */       {
/* 504 */         reset();
/* 505 */         this.fScanner.setInputSource(this.fInputSource);
/* 506 */         this.fInputSource = null;
/*     */       }
/*     */       catch (XNIException ex)
/*     */       {
/* 511 */         throw ex;
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 516 */         throw ex;
/*     */       }
/*     */       catch (RuntimeException ex)
/*     */       {
/* 521 */         throw ex;
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 526 */         throw new XNIException(ex);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 531 */       return this.fScanner.scanDocument(complete);
/*     */     }
/*     */     catch (XNIException ex)
/*     */     {
/* 536 */       throw ex;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 541 */       throw ex;
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/* 546 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 551 */       throw new XNIException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cleanup()
/*     */   {
/* 562 */     this.fEntityManager.closeReaders();
/*     */   }
/*     */ 
/*     */   public void parse(XMLInputSource source)
/*     */     throws XNIException, IOException
/*     */   {
/* 579 */     if (this.fParseInProgress)
/*     */     {
/* 581 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*     */     }
/* 583 */     this.fParseInProgress = true;
/*     */     try
/*     */     {
/* 586 */       setInputSource(source);
/* 587 */       parse(true);
/*     */     }
/*     */     catch (XNIException ex)
/*     */     {
/* 592 */       throw ex;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 597 */       throw ex;
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/* 602 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 607 */       throw new XNIException(ex);
/*     */     }
/*     */     finally {
/* 610 */       this.fParseInProgress = false;
/*     */ 
/* 612 */       cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */     throws XNIException
/*     */   {
/* 628 */     if (this.fValidationManager != null) {
/* 629 */       this.fValidationManager.reset();
/*     */     }
/* 631 */     configurePipeline();
/* 632 */     super.reset();
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 646 */     if (this.fDTDValidator != null) {
/* 647 */       this.fScanner.setDocumentHandler(this.fDTDValidator);
/* 648 */       if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE)
/*     */       {
/* 651 */         this.fDTDValidator.setDocumentHandler(this.fNamespaceBinder);
/* 652 */         this.fDTDValidator.setDocumentSource(this.fScanner);
/* 653 */         this.fNamespaceBinder.setDocumentHandler(this.fDocumentHandler);
/* 654 */         this.fNamespaceBinder.setDocumentSource(this.fDTDValidator);
/* 655 */         this.fLastComponent = this.fNamespaceBinder;
/*     */       }
/*     */       else {
/* 658 */         this.fDTDValidator.setDocumentHandler(this.fDocumentHandler);
/* 659 */         this.fDTDValidator.setDocumentSource(this.fScanner);
/* 660 */         this.fLastComponent = this.fDTDValidator;
/*     */       }
/*     */ 
/*     */     }
/* 664 */     else if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 665 */       this.fScanner.setDocumentHandler(this.fNamespaceBinder);
/* 666 */       this.fNamespaceBinder.setDocumentHandler(this.fDocumentHandler);
/* 667 */       this.fNamespaceBinder.setDocumentSource(this.fScanner);
/* 668 */       this.fLastComponent = this.fNamespaceBinder;
/*     */     }
/*     */     else {
/* 671 */       this.fScanner.setDocumentHandler(this.fDocumentHandler);
/* 672 */       this.fLastComponent = this.fScanner;
/*     */     }
/*     */ 
/* 676 */     configureDTDPipeline();
/*     */   }
/*     */ 
/*     */   protected void configureDTDPipeline()
/*     */   {
/* 682 */     if (this.fDTDScanner != null) {
/* 683 */       this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/* 684 */       if (this.fDTDProcessor != null) {
/* 685 */         this.fProperties.put("http://apache.org/xml/properties/internal/dtd-processor", this.fDTDProcessor);
/* 686 */         this.fDTDScanner.setDTDHandler(this.fDTDProcessor);
/* 687 */         this.fDTDProcessor.setDTDSource(this.fDTDScanner);
/* 688 */         this.fDTDProcessor.setDTDHandler(this.fDTDHandler);
/* 689 */         if (this.fDTDHandler != null) {
/* 690 */           this.fDTDHandler.setDTDSource(this.fDTDProcessor);
/*     */         }
/*     */ 
/* 693 */         this.fDTDScanner.setDTDContentModelHandler(this.fDTDProcessor);
/* 694 */         this.fDTDProcessor.setDTDContentModelSource(this.fDTDScanner);
/* 695 */         this.fDTDProcessor.setDTDContentModelHandler(this.fDTDContentModelHandler);
/* 696 */         if (this.fDTDContentModelHandler != null)
/* 697 */           this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDProcessor);
/*     */       }
/*     */       else
/*     */       {
/* 701 */         this.fDTDScanner.setDTDHandler(this.fDTDHandler);
/* 702 */         if (this.fDTDHandler != null) {
/* 703 */           this.fDTDHandler.setDTDSource(this.fDTDScanner);
/*     */         }
/* 705 */         this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/* 706 */         if (this.fDTDContentModelHandler != null)
/* 707 */           this.fDTDContentModelHandler.setDTDContentModelSource(this.fDTDScanner);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected FeatureState checkFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 736 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 737 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 745 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*     */       {
/* 747 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 753 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*     */       {
/* 756 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/* 761 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*     */       {
/* 764 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/* 769 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*     */       {
/* 771 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 776 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*     */       {
/* 778 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 784 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*     */       {
/* 786 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 794 */     return super.checkFeature(featureId);
/*     */   }
/*     */ 
/*     */   protected PropertyState checkProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 818 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 819 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 821 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*     */       {
/* 823 */         return PropertyState.RECOGNIZED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 831 */     return super.checkProperty(propertyId);
/*     */   }
/*     */ 
/*     */   protected XMLEntityManager createEntityManager()
/*     */   {
/* 839 */     return new XMLEntityManager();
/*     */   }
/*     */ 
/*     */   protected XMLErrorReporter createErrorReporter()
/*     */   {
/* 844 */     return new XMLErrorReporter();
/*     */   }
/*     */ 
/*     */   protected XMLDocumentScanner createDocumentScanner()
/*     */   {
/* 849 */     return new XMLDocumentScannerImpl();
/*     */   }
/*     */ 
/*     */   protected XMLDTDScanner createDTDScanner()
/*     */   {
/* 854 */     return new XMLDTDScannerImpl();
/*     */   }
/*     */ 
/*     */   protected XMLDTDProcessor createDTDProcessor()
/*     */   {
/* 859 */     return new XMLDTDProcessor();
/*     */   }
/*     */ 
/*     */   protected XMLDTDValidator createDTDValidator()
/*     */   {
/* 864 */     return new XMLDTDValidator();
/*     */   }
/*     */ 
/*     */   protected XMLNamespaceBinder createNamespaceBinder()
/*     */   {
/* 869 */     return new XMLNamespaceBinder();
/*     */   }
/*     */ 
/*     */   protected DTDDVFactory createDatatypeValidatorFactory()
/*     */   {
/* 874 */     return DTDDVFactory.getInstance();
/*     */   }
/*     */   protected ValidationManager createValidationManager() {
/* 877 */     return new ValidationManager();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.DTDConfiguration
 * JD-Core Version:    0.6.2
 */