/*      */ package com.sun.org.apache.xerces.internal.impl.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDHandler;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMErrorHandlerWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*      */ import com.sun.org.apache.xerces.internal.util.Status;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xs.LSInputList;
/*      */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSLoader;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.DOMConfiguration;
/*      */ import org.w3c.dom.DOMErrorHandler;
/*      */ import org.w3c.dom.DOMException;
/*      */ import org.w3c.dom.DOMStringList;
/*      */ import org.w3c.dom.ls.LSInput;
/*      */ import org.w3c.dom.ls.LSResourceResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ 
/*      */ public class XMLSchemaLoader
/*      */   implements XMLGrammarLoader, XMLComponent, XSLoader, DOMConfiguration
/*      */ {
/*      */   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
/*      */   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
/*      */   protected static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
/*      */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*      */   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
/*      */   protected static final String AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
/*      */   protected static final String USE_SERVICE_MECHANISM = "http://www.oracle.com/feature/use-service-mechanism";
/*  164 */   private static final String[] RECOGNIZED_FEATURES = { "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/validation/schema/augment-psvi", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/disallow-doctype-decl", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates", "http://www.oracle.com/feature/use-service-mechanism" };
/*      */   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*      */   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
/*      */   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
/*      */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*      */   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   protected static final String LOCALE = "http://apache.org/xml/properties/locale";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   public static final String ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD";
/*      */   public static final String ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
/*  234 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://apache.org/xml/properties/security-manager", "http://apache.org/xml/properties/locale", "http://apache.org/xml/properties/internal/validation/schema/dv-factory", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  253 */   private ParserConfigurationSettings fLoaderConfig = new ParserConfigurationSettings();
/*  254 */   private SymbolTable fSymbolTable = null;
/*  255 */   private XMLErrorReporter fErrorReporter = new XMLErrorReporter();
/*  256 */   private XMLEntityManager fEntityManager = null;
/*  257 */   private XMLEntityResolver fUserEntityResolver = null;
/*  258 */   private XMLGrammarPool fGrammarPool = null;
/*  259 */   private String fExternalSchemas = null;
/*  260 */   private String fExternalNoNSSchema = null;
/*      */ 
/*  262 */   private Object fJAXPSource = null;
/*      */ 
/*  264 */   private boolean fIsCheckedFully = false;
/*      */ 
/*  266 */   private boolean fJAXPProcessed = false;
/*      */ 
/*  268 */   private boolean fSettingsChanged = true;
/*      */   private XSDHandler fSchemaHandler;
/*      */   private XSGrammarBucket fGrammarBucket;
/*  273 */   private XSDeclarationPool fDeclPool = null;
/*      */   private SubstitutionGroupHandler fSubGroupHandler;
/*  275 */   private final CMNodeFactory fNodeFactory = new CMNodeFactory();
/*      */   private CMBuilder fCMBuilder;
/*  277 */   private XSDDescription fXSDDescription = new XSDDescription();
/*  278 */   private String faccessExternalSchema = "all";
/*      */   private Map fJAXPCache;
/*  281 */   private Locale fLocale = Locale.getDefault();
/*      */ 
/*  284 */   private DOMStringList fRecognizedParameters = null;
/*      */ 
/*  287 */   private DOMErrorHandlerWrapper fErrorHandler = null;
/*      */ 
/*  290 */   private DOMEntityResolverWrapper fResourceResolver = null;
/*      */ 
/*      */   public XMLSchemaLoader()
/*      */   {
/*  294 */     this(new SymbolTable(), null, new XMLEntityManager(), null, null, null);
/*      */   }
/*      */ 
/*      */   public XMLSchemaLoader(SymbolTable symbolTable) {
/*  298 */     this(symbolTable, null, new XMLEntityManager(), null, null, null);
/*      */   }
/*      */ 
/*      */   XMLSchemaLoader(XMLErrorReporter errorReporter, XSGrammarBucket grammarBucket, SubstitutionGroupHandler sHandler, CMBuilder builder)
/*      */   {
/*  312 */     this(null, errorReporter, null, grammarBucket, sHandler, builder);
/*      */   }
/*      */ 
/*      */   XMLSchemaLoader(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityResolver, XSGrammarBucket grammarBucket, SubstitutionGroupHandler sHandler, CMBuilder builder)
/*      */   {
/*  323 */     this.fLoaderConfig.addRecognizedFeatures(RECOGNIZED_FEATURES);
/*  324 */     this.fLoaderConfig.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/*  325 */     if (symbolTable != null) {
/*  326 */       this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*      */     }
/*      */ 
/*  329 */     if (errorReporter == null) {
/*  330 */       errorReporter = new XMLErrorReporter();
/*  331 */       errorReporter.setLocale(this.fLocale);
/*  332 */       errorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", new DefaultErrorHandler());
/*      */     }
/*      */ 
/*  335 */     this.fErrorReporter = errorReporter;
/*      */ 
/*  337 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/*  338 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
/*      */     }
/*  340 */     this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*  341 */     this.fEntityManager = entityResolver;
/*      */ 
/*  343 */     if (this.fEntityManager != null) {
/*  344 */       this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/entity-manager", this.fEntityManager);
/*      */     }
/*      */ 
/*  348 */     this.fLoaderConfig.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
/*      */ 
/*  350 */     if (grammarBucket == null) {
/*  351 */       grammarBucket = new XSGrammarBucket();
/*      */     }
/*  353 */     this.fGrammarBucket = grammarBucket;
/*  354 */     if (sHandler == null) {
/*  355 */       sHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
/*      */     }
/*  357 */     this.fSubGroupHandler = sHandler;
/*      */ 
/*  359 */     if (builder == null) {
/*  360 */       builder = new CMBuilder(this.fNodeFactory);
/*      */     }
/*  362 */     this.fCMBuilder = builder;
/*  363 */     this.fSchemaHandler = new XSDHandler(this.fGrammarBucket);
/*  364 */     if (this.fDeclPool != null) {
/*  365 */       this.fDeclPool.reset();
/*      */     }
/*  367 */     this.fJAXPCache = new HashMap();
/*      */ 
/*  369 */     this.fSettingsChanged = true;
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  378 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String featureId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  390 */     return this.fLoaderConfig.getFeature(featureId);
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  404 */     this.fSettingsChanged = true;
/*  405 */     if (featureId.equals("http://apache.org/xml/features/continue-after-fatal-error")) {
/*  406 */       this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", state);
/*      */     }
/*  408 */     else if (featureId.equals("http://apache.org/xml/features/generate-synthetic-annotations")) {
/*  409 */       this.fSchemaHandler.setGenerateSyntheticAnnotations(state);
/*      */     }
/*  411 */     this.fLoaderConfig.setFeature(featureId, state);
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  420 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public Object getProperty(String propertyId)
/*      */     throws XMLConfigurationException
/*      */   {
/*  432 */     return this.fLoaderConfig.getProperty(propertyId);
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  446 */     this.fSettingsChanged = true;
/*  447 */     this.fLoaderConfig.setProperty(propertyId, state);
/*  448 */     if (propertyId.equals("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
/*  449 */       this.fJAXPSource = state;
/*  450 */       this.fJAXPProcessed = false;
/*      */     }
/*  452 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
/*  453 */       this.fGrammarPool = ((XMLGrammarPool)state);
/*      */     }
/*  455 */     else if (propertyId.equals("http://apache.org/xml/properties/schema/external-schemaLocation")) {
/*  456 */       this.fExternalSchemas = ((String)state);
/*      */     }
/*  458 */     else if (propertyId.equals("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation")) {
/*  459 */       this.fExternalNoNSSchema = ((String)state);
/*      */     }
/*  461 */     else if (propertyId.equals("http://apache.org/xml/properties/locale")) {
/*  462 */       setLocale((Locale)state);
/*      */     }
/*  464 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
/*  465 */       this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", state);
/*      */     }
/*  467 */     else if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
/*  468 */       this.fErrorReporter = ((XMLErrorReporter)state);
/*  469 */       if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/*  470 */         this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
/*      */       }
/*      */     }
/*  473 */     else if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/*  474 */       XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)state;
/*  475 */       this.faccessExternalSchema = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */   {
/*  488 */     this.fLocale = locale;
/*  489 */     this.fErrorReporter.setLocale(locale);
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  494 */     return this.fLocale;
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*      */   {
/*  503 */     this.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*      */   }
/*      */ 
/*      */   public XMLErrorHandler getErrorHandler()
/*      */   {
/*  508 */     return this.fErrorReporter.getErrorHandler();
/*      */   }
/*      */ 
/*      */   public void setEntityResolver(XMLEntityResolver entityResolver)
/*      */   {
/*  517 */     this.fUserEntityResolver = entityResolver;
/*  518 */     this.fLoaderConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
/*  519 */     this.fEntityManager.setProperty("http://apache.org/xml/properties/internal/entity-resolver", entityResolver);
/*      */   }
/*      */ 
/*      */   public XMLEntityResolver getEntityResolver()
/*      */   {
/*  524 */     return this.fUserEntityResolver;
/*      */   }
/*      */ 
/*      */   public void loadGrammar(XMLInputSource[] source)
/*      */     throws IOException, XNIException
/*      */   {
/*  539 */     int numSource = source.length;
/*  540 */     for (int i = 0; i < numSource; i++)
/*  541 */       loadGrammar(source[i]);
/*      */   }
/*      */ 
/*      */   public Grammar loadGrammar(XMLInputSource source)
/*      */     throws IOException, XNIException
/*      */   {
/*  562 */     reset(this.fLoaderConfig);
/*  563 */     this.fSettingsChanged = false;
/*  564 */     XSDDescription desc = new XSDDescription();
/*  565 */     desc.fContextType = 3;
/*  566 */     desc.setBaseSystemId(source.getBaseSystemId());
/*  567 */     desc.setLiteralSystemId(source.getSystemId());
/*      */ 
/*  569 */     Map locationPairs = new HashMap();
/*      */ 
/*  573 */     processExternalHints(this.fExternalSchemas, this.fExternalNoNSSchema, locationPairs, this.fErrorReporter);
/*      */ 
/*  575 */     SchemaGrammar grammar = loadSchema(desc, source, locationPairs);
/*      */ 
/*  577 */     if ((grammar != null) && (this.fGrammarPool != null)) {
/*  578 */       this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", this.fGrammarBucket.getGrammars());
/*      */ 
/*  581 */       if ((this.fIsCheckedFully) && (this.fJAXPCache.get(grammar) != grammar)) {
/*  582 */         XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
/*      */       }
/*      */     }
/*  585 */     return grammar;
/*      */   }
/*      */ 
/*      */   SchemaGrammar loadSchema(XSDDescription desc, XMLInputSource source, Map locationPairs)
/*      */     throws IOException, XNIException
/*      */   {
/*  605 */     if (!this.fJAXPProcessed) {
/*  606 */       processJAXPSchemaSource(locationPairs);
/*      */     }
/*      */ 
/*  609 */     if (desc.isExternal()) {
/*  610 */       String accessError = SecuritySupport.checkAccess(desc.getExpandedSystemId(), this.faccessExternalSchema, "all");
/*  611 */       if (accessError != null) {
/*  612 */         throw new XNIException(this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.access", new Object[] { SecuritySupport.sanitizePath(desc.getExpandedSystemId()), accessError }, (short)1));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  617 */     SchemaGrammar grammar = this.fSchemaHandler.parseSchema(source, desc, locationPairs);
/*      */ 
/*  619 */     return grammar;
/*      */   }
/*      */ 
/*      */   public static XMLInputSource resolveDocument(XSDDescription desc, Map locationPairs, XMLEntityResolver entityResolver)
/*      */     throws IOException
/*      */   {
/*  636 */     String loc = null;
/*      */ 
/*  638 */     if ((desc.getContextType() == 2) || (desc.fromInstance()))
/*      */     {
/*  641 */       String namespace = desc.getTargetNamespace();
/*  642 */       String ns = namespace == null ? XMLSymbols.EMPTY_STRING : namespace;
/*      */ 
/*  644 */       LocationArray tempLA = (LocationArray)locationPairs.get(ns);
/*  645 */       if (tempLA != null) {
/*  646 */         loc = tempLA.getFirstLocation();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  651 */     if (loc == null) {
/*  652 */       String[] hints = desc.getLocationHints();
/*  653 */       if ((hints != null) && (hints.length > 0)) {
/*  654 */         loc = hints[0];
/*      */       }
/*      */     }
/*  657 */     String expandedLoc = XMLEntityManager.expandSystemId(loc, desc.getBaseSystemId(), false);
/*  658 */     desc.setLiteralSystemId(loc);
/*  659 */     desc.setExpandedSystemId(expandedLoc);
/*  660 */     return entityResolver.resolveEntity(desc);
/*      */   }
/*      */ 
/*      */   public static void processExternalHints(String sl, String nsl, Map locations, XMLErrorReporter er)
/*      */   {
/*  667 */     if (sl != null)
/*      */     {
/*      */       try
/*      */       {
/*  672 */         XSAttributeDecl attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
/*      */ 
/*  674 */         attrDecl.fType.validate(sl, null, null);
/*  675 */         if (!tokenizeSchemaLocationStr(sl, locations))
/*      */         {
/*  677 */           er.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[] { sl }, (short)0);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex)
/*      */       {
/*  685 */         er.reportError("http://www.w3.org/TR/xml-schema-1", ex.getKey(), ex.getArgs(), (short)0);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  691 */     if (nsl != null)
/*      */       try
/*      */       {
/*  694 */         XSAttributeDecl attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
/*  695 */         attrDecl.fType.validate(nsl, null, null);
/*  696 */         LocationArray la = (LocationArray)locations.get(XMLSymbols.EMPTY_STRING);
/*  697 */         if (la == null) {
/*  698 */           la = new LocationArray();
/*  699 */           locations.put(XMLSymbols.EMPTY_STRING, la);
/*      */         }
/*  701 */         la.addLocation(nsl);
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex)
/*      */       {
/*  705 */         er.reportError("http://www.w3.org/TR/xml-schema-1", ex.getKey(), ex.getArgs(), (short)0);
/*      */       }
/*      */   }
/*      */ 
/*      */   public static boolean tokenizeSchemaLocationStr(String schemaStr, Map locations)
/*      */   {
/*  719 */     if (schemaStr != null) {
/*  720 */       StringTokenizer t = new StringTokenizer(schemaStr, " \n\t\r");
/*      */ 
/*  722 */       while (t.hasMoreTokens()) {
/*  723 */         String namespace = t.nextToken();
/*  724 */         if (!t.hasMoreTokens()) {
/*  725 */           return false;
/*      */         }
/*  727 */         String location = t.nextToken();
/*  728 */         LocationArray la = (LocationArray)locations.get(namespace);
/*  729 */         if (la == null) {
/*  730 */           la = new LocationArray();
/*  731 */           locations.put(namespace, la);
/*      */         }
/*  733 */         la.addLocation(location);
/*      */       }
/*      */     }
/*  736 */     return true;
/*      */   }
/*      */ 
/*      */   private void processJAXPSchemaSource(Map locationPairs)
/*      */     throws IOException
/*      */   {
/*  750 */     this.fJAXPProcessed = true;
/*  751 */     if (this.fJAXPSource == null) {
/*  752 */       return;
/*      */     }
/*      */ 
/*  755 */     Class componentType = this.fJAXPSource.getClass().getComponentType();
/*  756 */     XMLInputSource xis = null;
/*  757 */     String sid = null;
/*  758 */     if (componentType == null)
/*      */     {
/*  760 */       if (((this.fJAXPSource instanceof InputStream)) || ((this.fJAXPSource instanceof InputSource)))
/*      */       {
/*  762 */         SchemaGrammar g = (SchemaGrammar)this.fJAXPCache.get(this.fJAXPSource);
/*  763 */         if (g != null) {
/*  764 */           this.fGrammarBucket.putGrammar(g);
/*  765 */           return;
/*      */         }
/*      */       }
/*  768 */       this.fXSDDescription.reset();
/*  769 */       xis = xsdToXMLInputSource(this.fJAXPSource);
/*  770 */       sid = xis.getSystemId();
/*  771 */       this.fXSDDescription.fContextType = 3;
/*  772 */       if (sid != null) {
/*  773 */         this.fXSDDescription.setBaseSystemId(xis.getBaseSystemId());
/*  774 */         this.fXSDDescription.setLiteralSystemId(sid);
/*  775 */         this.fXSDDescription.setExpandedSystemId(sid);
/*  776 */         this.fXSDDescription.fLocationHints = new String[] { sid };
/*      */       }
/*  778 */       SchemaGrammar g = loadSchema(this.fXSDDescription, xis, locationPairs);
/*      */ 
/*  780 */       if (g != null) {
/*  781 */         if (((this.fJAXPSource instanceof InputStream)) || ((this.fJAXPSource instanceof InputSource)))
/*      */         {
/*  783 */           this.fJAXPCache.put(this.fJAXPSource, g);
/*  784 */           if (this.fIsCheckedFully) {
/*  785 */             XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
/*      */           }
/*      */         }
/*  788 */         this.fGrammarBucket.putGrammar(g);
/*      */       }
/*  790 */       return;
/*  791 */     }if ((componentType != Object.class) && (componentType != String.class) && (componentType != File.class) && (componentType != InputStream.class) && (componentType != InputSource.class))
/*      */     {
/*  798 */       throw new XMLConfigurationException(Status.NOT_SUPPORTED, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have an array of type {" + componentType.getName() + "}. Possible types of the array supported are Object, String, File, " + "InputStream, InputSource.");
/*      */     }
/*      */ 
/*  807 */     Object[] objArr = (Object[])this.fJAXPSource;
/*      */ 
/*  809 */     Vector jaxpSchemaSourceNamespaces = new Vector();
/*  810 */     for (int i = 0; i < objArr.length; i++) {
/*  811 */       if (((objArr[i] instanceof InputStream)) || ((objArr[i] instanceof InputSource)))
/*      */       {
/*  813 */         SchemaGrammar g = (SchemaGrammar)this.fJAXPCache.get(objArr[i]);
/*  814 */         if (g != null) {
/*  815 */           this.fGrammarBucket.putGrammar(g);
/*  816 */           continue;
/*      */         }
/*      */       }
/*  819 */       this.fXSDDescription.reset();
/*  820 */       xis = xsdToXMLInputSource(objArr[i]);
/*  821 */       sid = xis.getSystemId();
/*  822 */       this.fXSDDescription.fContextType = 3;
/*  823 */       if (sid != null) {
/*  824 */         this.fXSDDescription.setBaseSystemId(xis.getBaseSystemId());
/*  825 */         this.fXSDDescription.setLiteralSystemId(sid);
/*  826 */         this.fXSDDescription.setExpandedSystemId(sid);
/*  827 */         this.fXSDDescription.fLocationHints = new String[] { sid };
/*      */       }
/*  829 */       String targetNamespace = null;
/*      */ 
/*  831 */       SchemaGrammar grammar = this.fSchemaHandler.parseSchema(xis, this.fXSDDescription, locationPairs);
/*      */ 
/*  833 */       if (this.fIsCheckedFully) {
/*  834 */         XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fErrorReporter);
/*      */       }
/*  836 */       if (grammar != null) {
/*  837 */         targetNamespace = grammar.getTargetNamespace();
/*  838 */         if (jaxpSchemaSourceNamespaces.contains(targetNamespace))
/*      */         {
/*  840 */           throw new IllegalArgumentException(" When using array of Objects as the value of SCHEMA_SOURCE property , no two Schemas should share the same targetNamespace. ");
/*      */         }
/*      */ 
/*  845 */         jaxpSchemaSourceNamespaces.add(targetNamespace);
/*      */ 
/*  847 */         if (((objArr[i] instanceof InputStream)) || ((objArr[i] instanceof InputSource)))
/*      */         {
/*  849 */           this.fJAXPCache.put(objArr[i], grammar);
/*      */         }
/*  851 */         this.fGrammarBucket.putGrammar(grammar);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private XMLInputSource xsdToXMLInputSource(Object val)
/*      */   {
/*  862 */     if ((val instanceof String))
/*      */     {
/*  865 */       String loc = (String)val;
/*  866 */       this.fXSDDescription.reset();
/*  867 */       this.fXSDDescription.setValues(null, loc, null, null);
/*  868 */       XMLInputSource xis = null;
/*      */       try {
/*  870 */         xis = this.fEntityManager.resolveEntity(this.fXSDDescription);
/*      */       } catch (IOException ex) {
/*  872 */         this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[] { loc }, (short)1);
/*      */       }
/*      */ 
/*  876 */       if (xis == null)
/*      */       {
/*  879 */         return new XMLInputSource(null, loc, null);
/*      */       }
/*  881 */       return xis;
/*  882 */     }if ((val instanceof InputSource))
/*  883 */       return saxToXMLInputSource((InputSource)val);
/*  884 */     if ((val instanceof InputStream)) {
/*  885 */       return new XMLInputSource(null, null, null, (InputStream)val, null);
/*      */     }
/*  887 */     if ((val instanceof File)) {
/*  888 */       File file = (File)val;
/*  889 */       InputStream is = null;
/*      */       try {
/*  891 */         is = new BufferedInputStream(new FileInputStream(file));
/*      */       } catch (FileNotFoundException ex) {
/*  893 */         this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[] { file.toString() }, (short)1);
/*      */       }
/*      */ 
/*  897 */       return new XMLInputSource(null, null, null, is, null);
/*      */     }
/*  899 */     throw new XMLConfigurationException(Status.NOT_SUPPORTED, "\"http://java.sun.com/xml/jaxp/properties/schemaSource\" property cannot have a value of type {" + val.getClass().getName() + "}. Possible types of the value supported are String, File, InputStream, " + "InputSource OR an array of these types.");
/*      */   }
/*      */ 
/*      */   private static XMLInputSource saxToXMLInputSource(InputSource sis)
/*      */   {
/*  910 */     String publicId = sis.getPublicId();
/*  911 */     String systemId = sis.getSystemId();
/*      */ 
/*  913 */     Reader charStream = sis.getCharacterStream();
/*  914 */     if (charStream != null) {
/*  915 */       return new XMLInputSource(publicId, systemId, null, charStream, null);
/*      */     }
/*      */ 
/*  919 */     InputStream byteStream = sis.getByteStream();
/*  920 */     if (byteStream != null) {
/*  921 */       return new XMLInputSource(publicId, systemId, null, byteStream, sis.getEncoding());
/*      */     }
/*      */ 
/*  925 */     return new XMLInputSource(publicId, systemId, null);
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  968 */     if (featureId.equals("http://apache.org/xml/features/validation/schema/augment-psvi")) {
/*  969 */       return Boolean.TRUE;
/*      */     }
/*  971 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  979 */     return null;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  987 */     XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
/*  988 */     if (spm == null) {
/*  989 */       spm = new XMLSecurityPropertyManager();
/*  990 */       setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", spm);
/*      */     }
/*      */ 
/*  993 */     XMLSecurityManager sm = (XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager");
/*  994 */     if (sm == null) {
/*  995 */       setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
/*      */     }
/*  997 */     this.faccessExternalSchema = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_SCHEMA);
/*      */ 
/*  999 */     this.fGrammarBucket.reset();
/*      */ 
/* 1001 */     this.fSubGroupHandler.reset();
/*      */ 
/* 1003 */     boolean parser_settings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/* 1005 */     if ((!parser_settings) || (!this.fSettingsChanged))
/*      */     {
/* 1007 */       this.fJAXPProcessed = false;
/*      */ 
/* 1009 */       initGrammarBucket();
/* 1010 */       return;
/*      */     }
/*      */ 
/* 1014 */     this.fNodeFactory.reset(componentManager);
/*      */ 
/* 1019 */     this.fEntityManager = ((XMLEntityManager)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/*      */ 
/* 1022 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */ 
/* 1025 */     SchemaDVFactory dvFactory = null;
/* 1026 */     dvFactory = this.fSchemaHandler.getDVFactory();
/* 1027 */     if (dvFactory == null) {
/* 1028 */       dvFactory = SchemaDVFactory.getInstance();
/* 1029 */       this.fSchemaHandler.setDVFactory(dvFactory);
/*      */     }
/*      */ 
/* 1032 */     boolean psvi = componentManager.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi", false);
/*      */ 
/* 1034 */     if (!psvi) {
/* 1035 */       if (this.fDeclPool != null) {
/* 1036 */         this.fDeclPool.reset();
/*      */       }
/*      */       else {
/* 1039 */         this.fDeclPool = new XSDeclarationPool();
/*      */       }
/* 1041 */       this.fCMBuilder.setDeclPool(this.fDeclPool);
/* 1042 */       this.fSchemaHandler.setDeclPool(this.fDeclPool);
/* 1043 */       if ((dvFactory instanceof SchemaDVFactoryImpl)) {
/* 1044 */         this.fDeclPool.setDVFactory((SchemaDVFactoryImpl)dvFactory);
/* 1045 */         ((SchemaDVFactoryImpl)dvFactory).setDeclPool(this.fDeclPool);
/*      */       }
/*      */     } else {
/* 1048 */       this.fCMBuilder.setDeclPool(null);
/* 1049 */       this.fSchemaHandler.setDeclPool(null);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1054 */       this.fExternalSchemas = ((String)componentManager.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation"));
/* 1055 */       this.fExternalNoNSSchema = ((String)componentManager.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation"));
/*      */     } catch (XMLConfigurationException e) {
/* 1057 */       this.fExternalSchemas = null;
/* 1058 */       this.fExternalNoNSSchema = null;
/*      */     }
/*      */ 
/* 1062 */     this.fJAXPSource = componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
/* 1063 */     this.fJAXPProcessed = false;
/*      */ 
/* 1066 */     this.fGrammarPool = ((XMLGrammarPool)componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null));
/* 1067 */     initGrammarBucket();
/*      */     try
/*      */     {
/* 1070 */       boolean fatalError = componentManager.getFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
/* 1071 */       if (!fatalError)
/* 1072 */         this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", fatalError);
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/* 1077 */     this.fIsCheckedFully = componentManager.getFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
/*      */ 
/* 1080 */     this.fSchemaHandler.setGenerateSyntheticAnnotations(componentManager.getFeature("http://apache.org/xml/features/generate-synthetic-annotations", false));
/* 1081 */     this.fSchemaHandler.reset(componentManager);
/*      */   }
/*      */ 
/*      */   private void initGrammarBucket() {
/* 1085 */     if (this.fGrammarPool != null) {
/* 1086 */       Grammar[] initialGrammars = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/2001/XMLSchema");
/* 1087 */       for (int i = 0; i < initialGrammars.length; i++)
/*      */       {
/* 1090 */         if (!this.fGrammarBucket.putGrammar((SchemaGrammar)initialGrammars[i], true))
/*      */         {
/* 1093 */           this.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", null, (short)0);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public DOMConfiguration getConfig()
/*      */   {
/* 1106 */     return this;
/*      */   }
/*      */ 
/*      */   public XSModel load(LSInput is)
/*      */   {
/*      */     try
/*      */     {
/* 1114 */       Grammar g = loadGrammar(dom2xmlInputSource(is));
/* 1115 */       return ((XSGrammar)g).toXSModel();
/*      */     } catch (Exception e) {
/* 1117 */       reportDOMFatalError(e);
/* 1118 */     }return null;
/*      */   }
/*      */ 
/*      */   public XSModel loadInputList(LSInputList is)
/*      */   {
/* 1126 */     int length = is.getLength();
/* 1127 */     SchemaGrammar[] gs = new SchemaGrammar[length];
/* 1128 */     for (int i = 0; i < length; i++) {
/*      */       try {
/* 1130 */         gs[i] = ((SchemaGrammar)loadGrammar(dom2xmlInputSource(is.item(i))));
/*      */       } catch (Exception e) {
/* 1132 */         reportDOMFatalError(e);
/* 1133 */         return null;
/*      */       }
/*      */     }
/* 1136 */     return new XSModelImpl(gs);
/*      */   }
/*      */ 
/*      */   public XSModel loadURI(String uri)
/*      */   {
/*      */     try
/*      */     {
/* 1144 */       Grammar g = loadGrammar(new XMLInputSource(null, uri, null));
/* 1145 */       return ((XSGrammar)g).toXSModel();
/*      */     }
/*      */     catch (Exception e) {
/* 1148 */       reportDOMFatalError(e);
/* 1149 */     }return null;
/*      */   }
/*      */ 
/*      */   public XSModel loadURIList(StringList uriList)
/*      */   {
/* 1157 */     int length = uriList.getLength();
/* 1158 */     SchemaGrammar[] gs = new SchemaGrammar[length];
/* 1159 */     for (int i = 0; i < length; i++) {
/*      */       try {
/* 1161 */         gs[i] = ((SchemaGrammar)loadGrammar(new XMLInputSource(null, uriList.item(i), null)));
/*      */       }
/*      */       catch (Exception e) {
/* 1164 */         reportDOMFatalError(e);
/* 1165 */         return null;
/*      */       }
/*      */     }
/* 1168 */     return new XSModelImpl(gs);
/*      */   }
/*      */ 
/*      */   void reportDOMFatalError(Exception e) {
/* 1172 */     if (this.fErrorHandler != null) {
/* 1173 */       DOMErrorImpl error = new DOMErrorImpl();
/* 1174 */       error.fException = e;
/* 1175 */       error.fMessage = e.getMessage();
/* 1176 */       error.fSeverity = 3;
/* 1177 */       this.fErrorHandler.getErrorHandler().handleError(error);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean canSetParameter(String name, Object value)
/*      */   {
/* 1185 */     if ((value instanceof Boolean)) {
/* 1186 */       if ((name.equals("validate")) || (name.equals("http://apache.org/xml/features/validation/schema-full-checking")) || (name.equals("http://apache.org/xml/features/validate-annotations")) || (name.equals("http://apache.org/xml/features/continue-after-fatal-error")) || (name.equals("http://apache.org/xml/features/allow-java-encodings")) || (name.equals("http://apache.org/xml/features/standard-uri-conformant")) || (name.equals("http://apache.org/xml/features/generate-synthetic-annotations")) || (name.equals("http://apache.org/xml/features/honour-all-schemaLocations")) || (name.equals("http://apache.org/xml/features/namespace-growth")) || (name.equals("http://apache.org/xml/features/internal/tolerate-duplicates")) || (name.equals("http://www.oracle.com/feature/use-service-mechanism")))
/*      */       {
/* 1197 */         return true;
/*      */       }
/*      */ 
/* 1200 */       return false;
/*      */     }
/* 1202 */     if ((name.equals("error-handler")) || (name.equals("resource-resolver")) || (name.equals("http://apache.org/xml/properties/internal/symbol-table")) || (name.equals("http://apache.org/xml/properties/internal/error-reporter")) || (name.equals("http://apache.org/xml/properties/internal/error-handler")) || (name.equals("http://apache.org/xml/properties/internal/entity-resolver")) || (name.equals("http://apache.org/xml/properties/internal/grammar-pool")) || (name.equals("http://apache.org/xml/properties/schema/external-schemaLocation")) || (name.equals("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation")) || (name.equals("http://java.sun.com/xml/jaxp/properties/schemaSource")) || (name.equals("http://apache.org/xml/properties/internal/validation/schema/dv-factory")))
/*      */     {
/* 1213 */       return true;
/*      */     }
/* 1215 */     return false;
/*      */   }
/*      */ 
/*      */   public Object getParameter(String name)
/*      */     throws DOMException
/*      */   {
/* 1223 */     if (name.equals("error-handler")) {
/* 1224 */       return this.fErrorHandler != null ? this.fErrorHandler.getErrorHandler() : null;
/*      */     }
/* 1226 */     if (name.equals("resource-resolver")) {
/* 1227 */       return this.fResourceResolver != null ? this.fResourceResolver.getEntityResolver() : null;
/*      */     }
/*      */     try
/*      */     {
/* 1231 */       boolean feature = getFeature(name);
/* 1232 */       return feature ? Boolean.TRUE : Boolean.FALSE;
/*      */     }
/*      */     catch (Exception e) {
/*      */       try {
/* 1236 */         return getProperty(name);
/*      */       }
/*      */       catch (Exception ex) {
/* 1239 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/* 1244 */         throw new DOMException((short)9, msg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public DOMStringList getParameterNames()
/*      */   {
/* 1253 */     if (this.fRecognizedParameters == null) {
/* 1254 */       Vector v = new Vector();
/* 1255 */       v.add("validate");
/* 1256 */       v.add("error-handler");
/* 1257 */       v.add("resource-resolver");
/* 1258 */       v.add("http://apache.org/xml/properties/internal/symbol-table");
/* 1259 */       v.add("http://apache.org/xml/properties/internal/error-reporter");
/* 1260 */       v.add("http://apache.org/xml/properties/internal/error-handler");
/* 1261 */       v.add("http://apache.org/xml/properties/internal/entity-resolver");
/* 1262 */       v.add("http://apache.org/xml/properties/internal/grammar-pool");
/* 1263 */       v.add("http://apache.org/xml/properties/schema/external-schemaLocation");
/* 1264 */       v.add("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
/* 1265 */       v.add("http://java.sun.com/xml/jaxp/properties/schemaSource");
/* 1266 */       v.add("http://apache.org/xml/features/validation/schema-full-checking");
/* 1267 */       v.add("http://apache.org/xml/features/continue-after-fatal-error");
/* 1268 */       v.add("http://apache.org/xml/features/allow-java-encodings");
/* 1269 */       v.add("http://apache.org/xml/features/standard-uri-conformant");
/* 1270 */       v.add("http://apache.org/xml/features/validate-annotations");
/* 1271 */       v.add("http://apache.org/xml/features/generate-synthetic-annotations");
/* 1272 */       v.add("http://apache.org/xml/features/honour-all-schemaLocations");
/* 1273 */       v.add("http://apache.org/xml/features/namespace-growth");
/* 1274 */       v.add("http://apache.org/xml/features/internal/tolerate-duplicates");
/* 1275 */       v.add("http://www.oracle.com/feature/use-service-mechanism");
/* 1276 */       this.fRecognizedParameters = new DOMStringListImpl(v);
/*      */     }
/* 1278 */     return this.fRecognizedParameters;
/*      */   }
/*      */ 
/*      */   public void setParameter(String name, Object value)
/*      */     throws DOMException
/*      */   {
/* 1285 */     if ((value instanceof Boolean)) {
/* 1286 */       boolean state = ((Boolean)value).booleanValue();
/* 1287 */       if ((name.equals("validate")) && (state))
/* 1288 */         return;
/*      */       try
/*      */       {
/* 1291 */         setFeature(name, state);
/*      */       } catch (Exception e) {
/* 1293 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/* 1298 */         throw new DOMException((short)9, msg);
/*      */       }
/* 1300 */       return;
/*      */     }
/* 1302 */     if (name.equals("error-handler")) {
/* 1303 */       if ((value instanceof DOMErrorHandler)) {
/*      */         try {
/* 1305 */           this.fErrorHandler = new DOMErrorHandlerWrapper((DOMErrorHandler)value);
/* 1306 */           setErrorHandler(this.fErrorHandler);
/*      */         } catch (XMLConfigurationException e) {
/*      */         }
/*      */       }
/*      */       else {
/* 1311 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/* 1316 */         throw new DOMException((short)9, msg);
/*      */       }
/* 1318 */       return;
/*      */     }
/*      */ 
/* 1321 */     if (name.equals("resource-resolver")) {
/* 1322 */       if ((value instanceof LSResourceResolver)) {
/*      */         try {
/* 1324 */           this.fResourceResolver = new DOMEntityResolverWrapper((LSResourceResolver)value);
/* 1325 */           setEntityResolver(this.fResourceResolver);
/*      */         } catch (XMLConfigurationException e) {
/*      */         }
/*      */       }
/*      */       else {
/* 1330 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/* 1335 */         throw new DOMException((short)9, msg);
/*      */       }
/* 1337 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1341 */       setProperty(name, value);
/*      */     }
/*      */     catch (Exception ex) {
/* 1344 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[] { name });
/*      */ 
/* 1349 */       throw new DOMException((short)9, msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   XMLInputSource dom2xmlInputSource(LSInput is)
/*      */   {
/* 1357 */     XMLInputSource xis = null;
/*      */ 
/* 1368 */     if (is.getCharacterStream() != null) {
/* 1369 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getCharacterStream(), "UTF-16");
/*      */     }
/* 1374 */     else if (is.getByteStream() != null) {
/* 1375 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), is.getByteStream(), is.getEncoding());
/*      */     }
/* 1381 */     else if ((is.getStringData() != null) && (is.getStringData().length() != 0)) {
/* 1382 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI(), new StringReader(is.getStringData()), "UTF-16");
/*      */     }
/*      */     else
/*      */     {
/* 1388 */       xis = new XMLInputSource(is.getPublicId(), is.getSystemId(), is.getBaseURI());
/*      */     }
/*      */ 
/* 1392 */     return xis;
/*      */   }
/*      */ 
/*      */   static class LocationArray
/*      */   {
/*      */     int length;
/*  931 */     String[] locations = new String[2];
/*      */ 
/*      */     public void resize(int oldLength, int newLength) {
/*  934 */       String[] temp = new String[newLength];
/*  935 */       System.arraycopy(this.locations, 0, temp, 0, Math.min(oldLength, newLength));
/*  936 */       this.locations = temp;
/*  937 */       this.length = Math.min(oldLength, newLength);
/*      */     }
/*      */ 
/*      */     public void addLocation(String location) {
/*  941 */       if (this.length >= this.locations.length) {
/*  942 */         resize(this.length, Math.max(1, this.length * 2));
/*      */       }
/*  944 */       this.locations[(this.length++)] = location;
/*      */     }
/*      */ 
/*      */     public String[] getLocationArray() {
/*  948 */       if (this.length < this.locations.length) {
/*  949 */         resize(this.locations.length, this.length);
/*      */       }
/*  951 */       return this.locations;
/*      */     }
/*      */ 
/*      */     public String getFirstLocation() {
/*  955 */       return this.length > 0 ? this.locations[0] : null;
/*      */     }
/*      */ 
/*      */     public int getLength() {
/*  959 */       return this.length;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader
 * JD-Core Version:    0.6.2
 */