/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.util.FeatureState;
/*     */ import com.sun.org.apache.xerces.internal.util.PropertyState;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
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
/*     */ public class NonValidatingConfiguration extends BasicParserConfiguration
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
/*     */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*     */   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*     */   protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
/*     */   protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
/*     */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*     */   protected static final String DTD_VALIDATOR = "http://apache.org/xml/properties/internal/validator/dtd";
/*     */   protected static final String NAMESPACE_BINDER = "http://apache.org/xml/properties/internal/namespace-binder";
/*     */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*     */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   protected static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*     */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*     */   protected static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final boolean PRINT_EXCEPTION_STACK_TRACE = false;
/*     */   protected XMLGrammarPool fGrammarPool;
/*     */   protected DTDDVFactory fDatatypeValidatorFactory;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLEntityManager fEntityManager;
/*     */   protected XMLDocumentScanner fScanner;
/*     */   protected XMLInputSource fInputSource;
/*     */   protected XMLDTDScanner fDTDScanner;
/*     */   protected ValidationManager fValidationManager;
/*     */   private XMLNSDocumentScannerImpl fNamespaceScanner;
/*     */   private XMLDocumentScannerImpl fNonNSScanner;
/* 216 */   protected boolean fConfigUpdated = false;
/*     */   protected XMLLocator fLocator;
/* 229 */   protected boolean fParseInProgress = false;
/*     */ 
/*     */   public NonValidatingConfiguration()
/*     */   {
/* 237 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public NonValidatingConfiguration(SymbolTable symbolTable)
/*     */   {
/* 246 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 262 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public NonValidatingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 280 */     super(symbolTable, parentSettings);
/*     */ 
/* 283 */     String[] recognizedFeatures = { "http://apache.org/xml/features/internal/parser-settings", "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/continue-after-fatal-error" };
/*     */ 
/* 295 */     addRecognizedFeatures(recognizedFeatures);
/*     */ 
/* 301 */     this.fFeatures.put("http://apache.org/xml/features/continue-after-fatal-error", Boolean.FALSE);
/* 302 */     this.fFeatures.put("http://apache.org/xml/features/internal/parser-settings", Boolean.TRUE);
/* 303 */     this.fFeatures.put("http://xml.org/sax/features/namespaces", Boolean.TRUE);
/*     */ 
/* 310 */     String[] recognizedProperties = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/document-scanner", "http://apache.org/xml/properties/internal/dtd-scanner", "http://apache.org/xml/properties/internal/validator/dtd", "http://apache.org/xml/properties/internal/namespace-binder", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/locale", "http://apache.org/xml/properties/security-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*     */ 
/* 324 */     addRecognizedProperties(recognizedProperties);
/*     */ 
/* 326 */     this.fGrammarPool = grammarPool;
/* 327 */     if (this.fGrammarPool != null) {
/* 328 */       this.fProperties.put("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*     */     }
/*     */ 
/* 331 */     this.fEntityManager = createEntityManager();
/* 332 */     this.fProperties.put("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/* 333 */     addComponent(this.fEntityManager);
/*     */ 
/* 335 */     this.fErrorReporter = createErrorReporter();
/* 336 */     this.fErrorReporter.setDocumentLocator(this.fEntityManager.getEntityScanner());
/* 337 */     this.fProperties.put("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/* 338 */     addComponent(this.fErrorReporter);
/*     */ 
/* 343 */     this.fDTDScanner = createDTDScanner();
/* 344 */     if (this.fDTDScanner != null) {
/* 345 */       this.fProperties.put("http://apache.org/xml/properties/internal/dtd-scanner", this.fDTDScanner);
/* 346 */       if ((this.fDTDScanner instanceof XMLComponent)) {
/* 347 */         addComponent((XMLComponent)this.fDTDScanner);
/*     */       }
/*     */     }
/*     */ 
/* 351 */     this.fDatatypeValidatorFactory = createDatatypeValidatorFactory();
/* 352 */     if (this.fDatatypeValidatorFactory != null) {
/* 353 */       this.fProperties.put("http://apache.org/xml/properties/internal/datatype-validator-factory", this.fDatatypeValidatorFactory);
/*     */     }
/*     */ 
/* 356 */     this.fValidationManager = createValidationManager();
/*     */ 
/* 358 */     if (this.fValidationManager != null) {
/* 359 */       this.fProperties.put("http://apache.org/xml/properties/internal/validation-manager", this.fValidationManager);
/*     */     }
/*     */ 
/* 362 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/* 363 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/* 364 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/* 365 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*     */     }
/*     */ 
/* 368 */     this.fConfigUpdated = false;
/*     */     try
/*     */     {
/* 372 */       setLocale(Locale.getDefault());
/*     */     }
/*     */     catch (XNIException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 385 */     this.fConfigUpdated = true;
/* 386 */     super.setFeature(featureId, state);
/*     */   }
/*     */ 
/*     */   public PropertyState getPropertyState(String propertyId) throws XMLConfigurationException
/*     */   {
/* 391 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 392 */       return PropertyState.is(getLocale());
/*     */     }
/* 394 */     return super.getPropertyState(propertyId);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value) throws XMLConfigurationException
/*     */   {
/* 399 */     this.fConfigUpdated = true;
/* 400 */     if ("http://apache.org/xml/properties/locale".equals(propertyId)) {
/* 401 */       setLocale((Locale)value);
/*     */     }
/* 403 */     super.setProperty(propertyId, value);
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */     throws XNIException
/*     */   {
/* 415 */     super.setLocale(locale);
/* 416 */     this.fErrorReporter.setLocale(locale);
/*     */   }
/*     */ 
/*     */   public FeatureState getFeatureState(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 422 */     if (featureId.equals("http://apache.org/xml/features/internal/parser-settings")) {
/* 423 */       return FeatureState.is(this.fConfigUpdated);
/*     */     }
/* 425 */     return super.getFeatureState(featureId);
/*     */   }
/*     */ 
/*     */   public void setInputSource(XMLInputSource inputSource)
/*     */     throws XMLConfigurationException, IOException
/*     */   {
/* 454 */     this.fInputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public boolean parse(boolean complete)
/*     */     throws XNIException, IOException
/*     */   {
/* 477 */     if (this.fInputSource != null) {
/*     */       try
/*     */       {
/* 480 */         reset();
/* 481 */         this.fScanner.setInputSource(this.fInputSource);
/* 482 */         this.fInputSource = null;
/*     */       }
/*     */       catch (XNIException ex)
/*     */       {
/* 487 */         throw ex;
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 492 */         throw ex;
/*     */       }
/*     */       catch (RuntimeException ex)
/*     */       {
/* 497 */         throw ex;
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 502 */         throw new XNIException(ex);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 507 */       return this.fScanner.scanDocument(complete);
/*     */     }
/*     */     catch (XNIException ex)
/*     */     {
/* 512 */       throw ex;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 517 */       throw ex;
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/* 522 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 527 */       throw new XNIException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cleanup()
/*     */   {
/* 538 */     this.fEntityManager.closeReaders();
/*     */   }
/*     */ 
/*     */   public void parse(XMLInputSource source)
/*     */     throws XNIException, IOException
/*     */   {
/* 555 */     if (this.fParseInProgress)
/*     */     {
/* 557 */       throw new XNIException("FWK005 parse may not be called while parsing.");
/*     */     }
/* 559 */     this.fParseInProgress = true;
/*     */     try
/*     */     {
/* 562 */       setInputSource(source);
/* 563 */       parse(true);
/*     */     }
/*     */     catch (XNIException ex)
/*     */     {
/* 568 */       throw ex;
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 573 */       throw ex;
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/* 578 */       throw ex;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 583 */       throw new XNIException(ex);
/*     */     }
/*     */     finally {
/* 586 */       this.fParseInProgress = false;
/*     */ 
/* 588 */       cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */     throws XNIException
/*     */   {
/* 604 */     if (this.fValidationManager != null) {
/* 605 */       this.fValidationManager.reset();
/*     */     }
/* 607 */     configurePipeline();
/* 608 */     super.reset();
/*     */   }
/*     */ 
/*     */   protected void configurePipeline()
/*     */   {
/* 616 */     if (this.fFeatures.get("http://xml.org/sax/features/namespaces") == Boolean.TRUE) {
/* 617 */       if (this.fNamespaceScanner == null) {
/* 618 */         this.fNamespaceScanner = new XMLNSDocumentScannerImpl();
/* 619 */         addComponent(this.fNamespaceScanner);
/*     */       }
/* 621 */       this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNamespaceScanner);
/* 622 */       this.fNamespaceScanner.setDTDValidator(null);
/* 623 */       this.fScanner = this.fNamespaceScanner;
/*     */     }
/*     */     else {
/* 626 */       if (this.fNonNSScanner == null) {
/* 627 */         this.fNonNSScanner = new XMLDocumentScannerImpl();
/* 628 */         addComponent(this.fNonNSScanner);
/*     */       }
/* 630 */       this.fProperties.put("http://apache.org/xml/properties/internal/document-scanner", this.fNonNSScanner);
/* 631 */       this.fScanner = this.fNonNSScanner;
/*     */     }
/*     */ 
/* 634 */     this.fScanner.setDocumentHandler(this.fDocumentHandler);
/* 635 */     this.fLastComponent = this.fScanner;
/*     */ 
/* 637 */     if (this.fDTDScanner != null) {
/* 638 */       this.fDTDScanner.setDTDHandler(this.fDTDHandler);
/* 639 */       this.fDTDScanner.setDTDContentModelHandler(this.fDTDContentModelHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected FeatureState checkFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 666 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 667 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 675 */       if ((suffixLength == "validation/dynamic".length()) && (featureId.endsWith("validation/dynamic")))
/*     */       {
/* 677 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 682 */       if ((suffixLength == "validation/default-attribute-values".length()) && (featureId.endsWith("validation/default-attribute-values")))
/*     */       {
/* 685 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/* 690 */       if ((suffixLength == "validation/validate-content-models".length()) && (featureId.endsWith("validation/validate-content-models")))
/*     */       {
/* 693 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/* 698 */       if ((suffixLength == "nonvalidating/load-dtd-grammar".length()) && (featureId.endsWith("nonvalidating/load-dtd-grammar")))
/*     */       {
/* 700 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 705 */       if ((suffixLength == "nonvalidating/load-external-dtd".length()) && (featureId.endsWith("nonvalidating/load-external-dtd")))
/*     */       {
/* 707 */         return FeatureState.RECOGNIZED;
/*     */       }
/*     */ 
/* 713 */       if ((suffixLength == "validation/validate-datatypes".length()) && (featureId.endsWith("validation/validate-datatypes")))
/*     */       {
/* 715 */         return FeatureState.NOT_SUPPORTED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 723 */     return super.checkFeature(featureId);
/*     */   }
/*     */ 
/*     */   protected PropertyState checkProperty(String propertyId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 747 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 748 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 750 */       if ((suffixLength == "internal/dtd-scanner".length()) && (propertyId.endsWith("internal/dtd-scanner")))
/*     */       {
/* 752 */         return PropertyState.RECOGNIZED;
/*     */       }
/*     */     }
/*     */ 
/* 756 */     if (propertyId.startsWith("http://java.sun.com/xml/jaxp/properties/")) {
/* 757 */       int suffixLength = propertyId.length() - "http://java.sun.com/xml/jaxp/properties/".length();
/*     */ 
/* 759 */       if ((suffixLength == "schemaSource".length()) && (propertyId.endsWith("schemaSource")))
/*     */       {
/* 761 */         return PropertyState.RECOGNIZED;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 769 */     return super.checkProperty(propertyId);
/*     */   }
/*     */ 
/*     */   protected XMLEntityManager createEntityManager()
/*     */   {
/* 777 */     return new XMLEntityManager();
/*     */   }
/*     */ 
/*     */   protected XMLErrorReporter createErrorReporter()
/*     */   {
/* 782 */     return new XMLErrorReporter();
/*     */   }
/*     */ 
/*     */   protected XMLDocumentScanner createDocumentScanner()
/*     */   {
/* 787 */     return null;
/*     */   }
/*     */ 
/*     */   protected XMLDTDScanner createDTDScanner()
/*     */   {
/* 792 */     return new XMLDTDScannerImpl();
/*     */   }
/*     */ 
/*     */   protected DTDDVFactory createDatatypeValidatorFactory()
/*     */   {
/* 797 */     return DTDDVFactory.getInstance();
/*     */   }
/*     */   protected ValidationManager createValidationManager() {
/* 800 */     return new ValidationManager();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.NonValidatingConfiguration
 * JD-Core Version:    0.6.2
 */