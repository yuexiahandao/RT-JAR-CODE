/*      */ package com.sun.org.apache.xerces.internal.impl.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.FieldActivator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.KeyRef;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector.Matcher;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.ValueStore;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMNodeFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
/*      */ import com.sun.org.apache.xerces.internal.parsers.XMLParser;
/*      */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.IntStack;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*      */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XMLSchemaValidator
/*      */   implements XMLComponent, XMLDocumentFilter, FieldActivator, RevalidationHandler
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*      */   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
/*      */   protected static final String NORMALIZE_DATA = "http://apache.org/xml/features/validation/schema/normalized-value";
/*      */   protected static final String SCHEMA_ELEMENT_DEFAULT = "http://apache.org/xml/features/validation/schema/element-default";
/*      */   protected static final String SCHEMA_AUGMENT_PSVI = "http://apache.org/xml/features/validation/schema/augment-psvi";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*      */   protected static final String STANDARD_URI_CONFORMANT_FEATURE = "http://apache.org/xml/features/standard-uri-conformant";
/*      */   protected static final String GENERATE_SYNTHETIC_ANNOTATIONS = "http://apache.org/xml/features/generate-synthetic-annotations";
/*      */   protected static final String VALIDATE_ANNOTATIONS = "http://apache.org/xml/features/validate-annotations";
/*      */   protected static final String HONOUR_ALL_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
/*      */   protected static final String USE_GRAMMAR_POOL_ONLY = "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only";
/*      */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String NAMESPACE_GROWTH = "http://apache.org/xml/features/namespace-growth";
/*      */   protected static final String TOLERATE_DUPLICATES = "http://apache.org/xml/features/internal/tolerate-duplicates";
/*      */   protected static final String REPORT_WHITESPACE = "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace";
/*      */   public static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   public static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   public static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   public static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*      */   protected static final String SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";
/*      */   protected static final String SCHEMA_NONS_LOCATION = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";
/*      */   protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/*      */   protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*      */   protected static final String SCHEMA_DV_FACTORY = "http://apache.org/xml/properties/internal/validation/schema/dv-factory";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   protected static final String USE_SERVICE_MECHANISM = "http://www.oracle.com/feature/use-service-mechanism";
/*  245 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/schema", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/schema-full-checking", "http://apache.org/xml/features/allow-java-encodings", "http://apache.org/xml/features/continue-after-fatal-error", "http://apache.org/xml/features/standard-uri-conformant", "http://apache.org/xml/features/generate-synthetic-annotations", "http://apache.org/xml/features/validate-annotations", "http://apache.org/xml/features/honour-all-schemaLocations", "http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", "http://apache.org/xml/features/namespace-growth", "http://apache.org/xml/features/internal/tolerate-duplicates", "http://www.oracle.com/feature/use-service-mechanism" };
/*      */ 
/*  264 */   private static final Boolean[] FEATURE_DEFAULTS = { null, null, null, null, null, null, null, null, null, null, null, null, null, Boolean.TRUE };
/*      */ 
/*  287 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/validation-manager", "http://apache.org/xml/properties/schema/external-schemaLocation", "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "http://java.sun.com/xml/jaxp/properties/schemaSource", "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://apache.org/xml/properties/internal/validation/schema/dv-factory", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager" };
/*      */ 
/*  302 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null, null, null, null, null, null, null, null, null };
/*      */   protected static final int ID_CONSTRAINT_NUM = 1;
/*  311 */   private static final Hashtable EMPTY_TABLE = new Hashtable();
/*      */ 
/*  318 */   protected ElementPSVImpl fCurrentPSVI = new ElementPSVImpl();
/*      */ 
/*  325 */   protected final AugmentationsImpl fAugmentations = new AugmentationsImpl();
/*      */ 
/*  331 */   protected final HashMap fMayMatchFieldMap = new HashMap();
/*      */   protected XMLString fDefaultValue;
/*  337 */   protected boolean fDynamicValidation = false;
/*  338 */   protected boolean fSchemaDynamicValidation = false;
/*  339 */   protected boolean fDoValidation = false;
/*  340 */   protected boolean fFullChecking = false;
/*  341 */   protected boolean fNormalizeData = true;
/*  342 */   protected boolean fSchemaElementDefault = true;
/*  343 */   protected boolean fAugPSVI = true;
/*  344 */   protected boolean fIdConstraint = false;
/*  345 */   protected boolean fUseGrammarPoolOnly = false;
/*      */ 
/*  348 */   protected boolean fNamespaceGrowth = false;
/*      */ 
/*  351 */   private String fSchemaType = null;
/*      */ 
/*  354 */   protected boolean fEntityRef = false;
/*  355 */   protected boolean fInCDATA = false;
/*      */ 
/*  358 */   protected boolean fSawOnlyWhitespaceInElementContent = false;
/*      */   protected SymbolTable fSymbolTable;
/*      */   private XMLLocator fLocator;
/*  479 */   protected final XSIErrorReporter fXSIErrorReporter = new XSIErrorReporter();
/*      */   protected XMLEntityResolver fEntityResolver;
/*  485 */   protected ValidationManager fValidationManager = null;
/*  486 */   protected ValidationState fValidationState = new ValidationState();
/*      */   protected XMLGrammarPool fGrammarPool;
/*  490 */   protected String fExternalSchemas = null;
/*  491 */   protected String fExternalNoNamespaceSchema = null;
/*      */ 
/*  494 */   protected Object fJaxpSchemaSource = null;
/*      */ 
/*  497 */   protected final XSDDescription fXSDDescription = new XSDDescription();
/*  498 */   protected final Hashtable fLocationPairs = new Hashtable();
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDocumentSource fDocumentSource;
/*  508 */   boolean reportWhitespace = false;
/*      */   static final int INITIAL_STACK_SIZE = 8;
/*      */   static final int INC_STACK_SIZE = 8;
/*      */   private static final boolean DEBUG_NORMALIZATION = false;
/* 1118 */   private final XMLString fEmptyXMLStr = new XMLString(null, 0, -1);
/*      */   private static final int BUFFER_SIZE = 20;
/* 1121 */   private final XMLString fNormalizedStr = new XMLString();
/* 1122 */   private boolean fFirstChunk = true;
/*      */ 
/* 1124 */   private boolean fTrailing = false;
/* 1125 */   private short fWhiteSpace = -1;
/* 1126 */   private boolean fUnionType = false;
/*      */ 
/* 1129 */   private final XSGrammarBucket fGrammarBucket = new XSGrammarBucket();
/* 1130 */   private final SubstitutionGroupHandler fSubGroupHandler = new SubstitutionGroupHandler(this.fGrammarBucket);
/*      */ 
/* 1135 */   private final XSSimpleType fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
/*      */ 
/* 1138 */   private final CMNodeFactory nodeFactory = new CMNodeFactory();
/*      */ 
/* 1141 */   private final CMBuilder fCMBuilder = new CMBuilder(this.nodeFactory);
/*      */ 
/* 1144 */   private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader(this.fXSIErrorReporter.fErrorReporter, this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder);
/*      */   private String fValidationRoot;
/*      */   private int fSkipValidationDepth;
/*      */   private int fNFullValidationDepth;
/*      */   private int fNNoneValidationDepth;
/*      */   private int fElementDepth;
/*      */   private boolean fSubElement;
/* 1173 */   private boolean[] fSubElementStack = new boolean[8];
/*      */   private XSElementDecl fCurrentElemDecl;
/* 1179 */   private XSElementDecl[] fElemDeclStack = new XSElementDecl[8];
/*      */   private boolean fNil;
/* 1185 */   private boolean[] fNilStack = new boolean[8];
/*      */   private XSNotationDecl fNotation;
/* 1191 */   private XSNotationDecl[] fNotationStack = new XSNotationDecl[8];
/*      */   private XSTypeDefinition fCurrentType;
/* 1197 */   private XSTypeDefinition[] fTypeStack = new XSTypeDefinition[8];
/*      */   private XSCMValidator fCurrentCM;
/* 1203 */   private XSCMValidator[] fCMStack = new XSCMValidator[8];
/*      */   private int[] fCurrCMState;
/* 1209 */   private int[][] fCMStateStack = new int[8][];
/*      */ 
/* 1212 */   private boolean fStrictAssess = true;
/*      */ 
/* 1215 */   private boolean[] fStrictAssessStack = new boolean[8];
/*      */ 
/* 1218 */   private final StringBuffer fBuffer = new StringBuffer();
/*      */ 
/* 1221 */   private boolean fAppendBuffer = true;
/*      */ 
/* 1224 */   private boolean fSawText = false;
/*      */ 
/* 1227 */   private boolean[] fSawTextStack = new boolean[8];
/*      */ 
/* 1230 */   private boolean fSawCharacters = false;
/*      */ 
/* 1233 */   private boolean[] fStringContent = new boolean[8];
/*      */ 
/* 1236 */   private final QName fTempQName = new QName();
/*      */ 
/* 1239 */   private ValidatedInfo fValidatedInfo = new ValidatedInfo();
/*      */ 
/* 1243 */   private ValidationState fState4XsiType = new ValidationState();
/*      */ 
/* 1247 */   private ValidationState fState4ApplyDefault = new ValidationState();
/*      */ 
/* 1265 */   protected XPathMatcherStack fMatcherStack = new XPathMatcherStack();
/*      */ 
/* 1268 */   protected ValueStoreCache fValueStoreCache = new ValueStoreCache();
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  520 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  547 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  578 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  579 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  580 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  583 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  596 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  597 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  598 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  601 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  610 */     this.fDocumentHandler = documentHandler;
/*      */ 
/*  613 */     if ((documentHandler instanceof XMLParser))
/*      */       try {
/*  615 */         this.reportWhitespace = ((XMLParser)documentHandler).getFeature("http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace");
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*  619 */         this.reportWhitespace = false;
/*      */       }
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  626 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDocumentSource(XMLDocumentSource source)
/*      */   {
/*  635 */     this.fDocumentSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDocumentSource getDocumentSource()
/*      */   {
/*  640 */     return this.fDocumentSource;
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  672 */     this.fValidationState.setNamespaceSupport(namespaceContext);
/*  673 */     this.fState4XsiType.setNamespaceSupport(namespaceContext);
/*  674 */     this.fState4ApplyDefault.setNamespaceSupport(namespaceContext);
/*  675 */     this.fLocator = locator;
/*      */ 
/*  677 */     handleStartDocument(locator, encoding);
/*      */ 
/*  679 */     if (this.fDocumentHandler != null)
/*  680 */       this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  702 */     if (this.fDocumentHandler != null)
/*  703 */       this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  728 */     if (this.fDocumentHandler != null)
/*  729 */       this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  746 */     Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
/*      */ 
/*  748 */     if (this.fDocumentHandler != null)
/*  749 */       this.fDocumentHandler.startElement(element, attributes, modifiedAugs);
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  766 */     Augmentations modifiedAugs = handleStartElement(element, attributes, augs);
/*      */ 
/*  771 */     this.fDefaultValue = null;
/*      */ 
/*  774 */     if (this.fElementDepth != -2) {
/*  775 */       modifiedAugs = handleEndElement(element, modifiedAugs);
/*      */     }
/*      */ 
/*  778 */     if (this.fDocumentHandler != null)
/*  779 */       if ((!this.fSchemaElementDefault) || (this.fDefaultValue == null)) {
/*  780 */         this.fDocumentHandler.emptyElement(element, attributes, modifiedAugs);
/*      */       } else {
/*  782 */         this.fDocumentHandler.startElement(element, attributes, modifiedAugs);
/*  783 */         this.fDocumentHandler.characters(this.fDefaultValue, null);
/*  784 */         this.fDocumentHandler.endElement(element, modifiedAugs);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  798 */     text = handleCharacters(text);
/*      */ 
/*  800 */     if (this.fSawOnlyWhitespaceInElementContent) {
/*  801 */       this.fSawOnlyWhitespaceInElementContent = false;
/*  802 */       if (!this.reportWhitespace) {
/*  803 */         ignorableWhitespace(text, augs);
/*  804 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  809 */     if (this.fDocumentHandler != null)
/*  810 */       if ((this.fNormalizeData) && (this.fUnionType))
/*      */       {
/*  815 */         if (augs != null)
/*  816 */           this.fDocumentHandler.characters(this.fEmptyXMLStr, augs);
/*      */       }
/*  818 */       else this.fDocumentHandler.characters(text, augs);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  838 */     handleIgnorableWhitespace(text);
/*      */ 
/*  840 */     if (this.fDocumentHandler != null)
/*  841 */       this.fDocumentHandler.ignorableWhitespace(text, augs);
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  858 */     this.fDefaultValue = null;
/*  859 */     Augmentations modifiedAugs = handleEndElement(element, augs);
/*      */ 
/*  861 */     if (this.fDocumentHandler != null)
/*  862 */       if ((!this.fSchemaElementDefault) || (this.fDefaultValue == null)) {
/*  863 */         this.fDocumentHandler.endElement(element, modifiedAugs);
/*      */       } else {
/*  865 */         this.fDocumentHandler.characters(this.fDefaultValue, null);
/*  866 */         this.fDocumentHandler.endElement(element, modifiedAugs);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  881 */     this.fInCDATA = true;
/*      */ 
/*  883 */     if (this.fDocumentHandler != null)
/*  884 */       this.fDocumentHandler.startCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  899 */     this.fInCDATA = false;
/*  900 */     if (this.fDocumentHandler != null)
/*  901 */       this.fDocumentHandler.endCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  915 */     handleEndDocument();
/*      */ 
/*  918 */     if (this.fDocumentHandler != null) {
/*  919 */       this.fDocumentHandler.endDocument(augs);
/*      */     }
/*  921 */     this.fLocator = null;
/*      */   }
/*      */ 
/*      */   public boolean characterData(String data, Augmentations augs)
/*      */   {
/*  935 */     this.fSawText = ((this.fSawText) || (data.length() > 0));
/*      */ 
/*  942 */     if ((this.fNormalizeData) && (this.fWhiteSpace != -1) && (this.fWhiteSpace != 0))
/*      */     {
/*  944 */       normalizeWhitespace(data, this.fWhiteSpace == 2);
/*  945 */       this.fBuffer.append(this.fNormalizedStr.ch, this.fNormalizedStr.offset, this.fNormalizedStr.length);
/*      */     }
/*  947 */     else if (this.fAppendBuffer) {
/*  948 */       this.fBuffer.append(data);
/*      */     }
/*      */ 
/*  953 */     boolean allWhiteSpace = true;
/*  954 */     if ((this.fCurrentType != null) && (this.fCurrentType.getTypeCategory() == 15))
/*      */     {
/*  956 */       XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/*  957 */       if (ctype.fContentType == 2)
/*      */       {
/*  959 */         for (int i = 0; i < data.length(); i++) {
/*  960 */           if (!XMLChar.isSpace(data.charAt(i))) {
/*  961 */             allWhiteSpace = false;
/*  962 */             this.fSawCharacters = true;
/*  963 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  969 */     return allWhiteSpace;
/*      */   }
/*      */ 
/*      */   public void elementDefault(String data)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1005 */     this.fEntityRef = true;
/*      */ 
/* 1007 */     if (this.fDocumentHandler != null)
/* 1008 */       this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1033 */     if (this.fDocumentHandler != null)
/* 1034 */       this.fDocumentHandler.textDecl(version, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1050 */     if (this.fDocumentHandler != null)
/* 1051 */       this.fDocumentHandler.comment(text, augs);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1077 */     if (this.fDocumentHandler != null)
/* 1078 */       this.fDocumentHandler.processingInstruction(target, data, augs);
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1098 */     this.fEntityRef = false;
/* 1099 */     if (this.fDocumentHandler != null)
/* 1100 */       this.fDocumentHandler.endGeneralEntity(name, augs);
/*      */   }
/*      */ 
/*      */   public XMLSchemaValidator()
/*      */   {
/* 1276 */     this.fState4XsiType.setExtraChecking(false);
/* 1277 */     this.fState4ApplyDefault.setFacetChecking(false);
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/* 1298 */     this.fIdConstraint = false;
/*      */ 
/* 1300 */     this.fLocationPairs.clear();
/*      */ 
/* 1303 */     this.fValidationState.resetIDTables();
/*      */ 
/* 1306 */     this.nodeFactory.reset(componentManager);
/*      */ 
/* 1309 */     this.fSchemaLoader.reset(componentManager);
/*      */ 
/* 1312 */     this.fCurrentElemDecl = null;
/* 1313 */     this.fCurrentCM = null;
/* 1314 */     this.fCurrCMState = null;
/* 1315 */     this.fSkipValidationDepth = -1;
/* 1316 */     this.fNFullValidationDepth = -1;
/* 1317 */     this.fNNoneValidationDepth = -1;
/* 1318 */     this.fElementDepth = -1;
/* 1319 */     this.fSubElement = false;
/* 1320 */     this.fSchemaDynamicValidation = false;
/*      */ 
/* 1323 */     this.fEntityRef = false;
/* 1324 */     this.fInCDATA = false;
/*      */ 
/* 1326 */     this.fMatcherStack.clear();
/*      */ 
/* 1328 */     if (!this.fMayMatchFieldMap.isEmpty())
/*      */     {
/* 1330 */       this.fMayMatchFieldMap.clear();
/*      */     }
/*      */ 
/* 1334 */     this.fXSIErrorReporter.reset((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*      */ 
/* 1336 */     boolean parser_settings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/* 1338 */     if (!parser_settings)
/*      */     {
/* 1340 */       this.fValidationManager.addValidationState(this.fValidationState);
/*      */ 
/* 1342 */       XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
/*      */ 
/* 1347 */       return;
/*      */     }
/*      */ 
/* 1352 */     SymbolTable symbolTable = (SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
/* 1353 */     if (symbolTable != this.fSymbolTable) {
/* 1354 */       this.fSymbolTable = symbolTable;
/*      */     }
/*      */ 
/* 1357 */     this.fNamespaceGrowth = componentManager.getFeature("http://apache.org/xml/features/namespace-growth", false);
/* 1358 */     this.fDynamicValidation = componentManager.getFeature("http://apache.org/xml/features/validation/dynamic", false);
/*      */ 
/* 1360 */     if (this.fDynamicValidation)
/* 1361 */       this.fDoValidation = true;
/*      */     else {
/* 1363 */       this.fDoValidation = componentManager.getFeature("http://xml.org/sax/features/validation", false);
/*      */     }
/*      */ 
/* 1366 */     if (this.fDoValidation) {
/* 1367 */       this.fDoValidation |= componentManager.getFeature("http://apache.org/xml/features/validation/schema", false);
/*      */     }
/*      */ 
/* 1370 */     this.fFullChecking = componentManager.getFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
/* 1371 */     this.fNormalizeData = componentManager.getFeature("http://apache.org/xml/features/validation/schema/normalized-value", false);
/* 1372 */     this.fSchemaElementDefault = componentManager.getFeature("http://apache.org/xml/features/validation/schema/element-default", false);
/*      */ 
/* 1374 */     this.fAugPSVI = componentManager.getFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
/*      */ 
/* 1376 */     this.fSchemaType = ((String)componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null));
/*      */ 
/* 1380 */     this.fUseGrammarPoolOnly = componentManager.getFeature("http://apache.org/xml/features/internal/validation/schema/use-grammar-pool-only", false);
/*      */ 
/* 1382 */     this.fEntityResolver = ((XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/*      */ 
/* 1384 */     this.fValidationManager = ((ValidationManager)componentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
/* 1385 */     this.fValidationManager.addValidationState(this.fValidationState);
/* 1386 */     this.fValidationState.setSymbolTable(this.fSymbolTable);
/*      */     try
/*      */     {
/* 1391 */       this.fExternalSchemas = ((String)componentManager.getProperty("http://apache.org/xml/properties/schema/external-schemaLocation"));
/* 1392 */       this.fExternalNoNamespaceSchema = ((String)componentManager.getProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation"));
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/* 1395 */       this.fExternalSchemas = null;
/* 1396 */       this.fExternalNoNamespaceSchema = null;
/*      */     }
/*      */ 
/* 1403 */     XMLSchemaLoader.processExternalHints(this.fExternalSchemas, this.fExternalNoNamespaceSchema, this.fLocationPairs, this.fXSIErrorReporter.fErrorReporter);
/*      */ 
/* 1409 */     this.fJaxpSchemaSource = componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", null);
/*      */ 
/* 1412 */     this.fGrammarPool = ((XMLGrammarPool)componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null));
/*      */ 
/* 1414 */     this.fState4XsiType.setSymbolTable(symbolTable);
/* 1415 */     this.fState4ApplyDefault.setSymbolTable(symbolTable);
/*      */   }
/*      */ 
/*      */   public void startValueScopeFor(IdentityConstraint identityConstraint, int initialDepth)
/*      */   {
/* 1432 */     ValueStoreBase valueStore = this.fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
/*      */ 
/* 1434 */     valueStore.startValueScope();
/*      */   }
/*      */ 
/*      */   public XPathMatcher activateField(Field field, int initialDepth)
/*      */   {
/* 1445 */     ValueStore valueStore = this.fValueStoreCache.getValueStoreFor(field.getIdentityConstraint(), initialDepth);
/*      */ 
/* 1447 */     setMayMatch(field, Boolean.TRUE);
/* 1448 */     XPathMatcher matcher = field.createMatcher(this, valueStore);
/* 1449 */     this.fMatcherStack.addMatcher(matcher);
/* 1450 */     matcher.startDocumentFragment();
/* 1451 */     return matcher;
/*      */   }
/*      */ 
/*      */   public void endValueScopeFor(IdentityConstraint identityConstraint, int initialDepth)
/*      */   {
/* 1461 */     ValueStoreBase valueStore = this.fValueStoreCache.getValueStoreFor(identityConstraint, initialDepth);
/*      */ 
/* 1463 */     valueStore.endValueScope();
/*      */   }
/*      */ 
/*      */   public void setMayMatch(Field field, Boolean state)
/*      */   {
/* 1476 */     this.fMayMatchFieldMap.put(field, state);
/*      */   }
/*      */ 
/*      */   public Boolean mayMatch(Field field)
/*      */   {
/* 1486 */     return (Boolean)this.fMayMatchFieldMap.get(field);
/*      */   }
/*      */ 
/*      */   private void activateSelectorFor(IdentityConstraint ic)
/*      */   {
/* 1491 */     Selector selector = ic.getSelector();
/* 1492 */     FieldActivator activator = this;
/* 1493 */     if (selector == null)
/* 1494 */       return;
/* 1495 */     XPathMatcher matcher = selector.createMatcher(activator, this.fElementDepth);
/* 1496 */     this.fMatcherStack.addMatcher(matcher);
/* 1497 */     matcher.startDocumentFragment();
/*      */   }
/*      */ 
/*      */   void ensureStackCapacity()
/*      */   {
/* 1507 */     if (this.fElementDepth == this.fElemDeclStack.length) {
/* 1508 */       int newSize = this.fElementDepth + 8;
/* 1509 */       boolean[] newArrayB = new boolean[newSize];
/* 1510 */       System.arraycopy(this.fSubElementStack, 0, newArrayB, 0, this.fElementDepth);
/* 1511 */       this.fSubElementStack = newArrayB;
/*      */ 
/* 1513 */       XSElementDecl[] newArrayE = new XSElementDecl[newSize];
/* 1514 */       System.arraycopy(this.fElemDeclStack, 0, newArrayE, 0, this.fElementDepth);
/* 1515 */       this.fElemDeclStack = newArrayE;
/*      */ 
/* 1517 */       newArrayB = new boolean[newSize];
/* 1518 */       System.arraycopy(this.fNilStack, 0, newArrayB, 0, this.fElementDepth);
/* 1519 */       this.fNilStack = newArrayB;
/*      */ 
/* 1521 */       XSNotationDecl[] newArrayN = new XSNotationDecl[newSize];
/* 1522 */       System.arraycopy(this.fNotationStack, 0, newArrayN, 0, this.fElementDepth);
/* 1523 */       this.fNotationStack = newArrayN;
/*      */ 
/* 1525 */       XSTypeDefinition[] newArrayT = new XSTypeDefinition[newSize];
/* 1526 */       System.arraycopy(this.fTypeStack, 0, newArrayT, 0, this.fElementDepth);
/* 1527 */       this.fTypeStack = newArrayT;
/*      */ 
/* 1529 */       XSCMValidator[] newArrayC = new XSCMValidator[newSize];
/* 1530 */       System.arraycopy(this.fCMStack, 0, newArrayC, 0, this.fElementDepth);
/* 1531 */       this.fCMStack = newArrayC;
/*      */ 
/* 1533 */       newArrayB = new boolean[newSize];
/* 1534 */       System.arraycopy(this.fSawTextStack, 0, newArrayB, 0, this.fElementDepth);
/* 1535 */       this.fSawTextStack = newArrayB;
/*      */ 
/* 1537 */       newArrayB = new boolean[newSize];
/* 1538 */       System.arraycopy(this.fStringContent, 0, newArrayB, 0, this.fElementDepth);
/* 1539 */       this.fStringContent = newArrayB;
/*      */ 
/* 1541 */       newArrayB = new boolean[newSize];
/* 1542 */       System.arraycopy(this.fStrictAssessStack, 0, newArrayB, 0, this.fElementDepth);
/* 1543 */       this.fStrictAssessStack = newArrayB;
/*      */ 
/* 1545 */       int[][] newArrayIA = new int[newSize][];
/* 1546 */       System.arraycopy(this.fCMStateStack, 0, newArrayIA, 0, this.fElementDepth);
/* 1547 */       this.fCMStateStack = newArrayIA;
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleStartDocument(XMLLocator locator, String encoding)
/*      */   {
/* 1554 */     this.fValueStoreCache.startDocument();
/* 1555 */     if (this.fAugPSVI) {
/* 1556 */       this.fCurrentPSVI.fGrammars = null;
/* 1557 */       this.fCurrentPSVI.fSchemaInformation = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleEndDocument() {
/* 1562 */     this.fValueStoreCache.endDocument();
/*      */   }
/*      */ 
/*      */   XMLString handleCharacters(XMLString text)
/*      */   {
/* 1569 */     if (this.fSkipValidationDepth >= 0) {
/* 1570 */       return text;
/*      */     }
/* 1572 */     this.fSawText = ((this.fSawText) || (text.length > 0));
/*      */ 
/* 1577 */     if ((this.fNormalizeData) && (this.fWhiteSpace != -1) && (this.fWhiteSpace != 0))
/*      */     {
/* 1579 */       normalizeWhitespace(text, this.fWhiteSpace == 2);
/* 1580 */       text = this.fNormalizedStr;
/*      */     }
/* 1582 */     if (this.fAppendBuffer) {
/* 1583 */       this.fBuffer.append(text.ch, text.offset, text.length);
/*      */     }
/*      */ 
/* 1587 */     this.fSawOnlyWhitespaceInElementContent = false;
/* 1588 */     if ((this.fCurrentType != null) && (this.fCurrentType.getTypeCategory() == 15))
/*      */     {
/* 1590 */       XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/* 1591 */       if (ctype.fContentType == 2)
/*      */       {
/* 1593 */         for (int i = text.offset; i < text.offset + text.length; i++) {
/* 1594 */           if (!XMLChar.isSpace(text.ch[i])) {
/* 1595 */             this.fSawCharacters = true;
/* 1596 */             break;
/*      */           }
/* 1598 */           this.fSawOnlyWhitespaceInElementContent = (!this.fSawCharacters);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1603 */     return text;
/*      */   }
/*      */ 
/*      */   private void normalizeWhitespace(XMLString value, boolean collapse)
/*      */   {
/* 1613 */     boolean skipSpace = collapse;
/* 1614 */     boolean sawNonWS = false;
/* 1615 */     boolean leading = false;
/* 1616 */     boolean trailing = false;
/*      */ 
/* 1618 */     int size = value.offset + value.length;
/*      */ 
/* 1621 */     if ((this.fNormalizedStr.ch == null) || (this.fNormalizedStr.ch.length < value.length + 1)) {
/* 1622 */       this.fNormalizedStr.ch = new char[value.length + 1];
/*      */     }
/*      */ 
/* 1625 */     this.fNormalizedStr.offset = 1;
/* 1626 */     this.fNormalizedStr.length = 1;
/*      */ 
/* 1628 */     for (int i = value.offset; i < size; i++) {
/* 1629 */       char c = value.ch[i];
/* 1630 */       if (XMLChar.isSpace(c)) {
/* 1631 */         if (!skipSpace)
/*      */         {
/* 1633 */           this.fNormalizedStr.ch[(this.fNormalizedStr.length++)] = ' ';
/* 1634 */           skipSpace = collapse;
/*      */         }
/* 1636 */         if (!sawNonWS)
/*      */         {
/* 1638 */           leading = true;
/*      */         }
/*      */       } else {
/* 1641 */         this.fNormalizedStr.ch[(this.fNormalizedStr.length++)] = c;
/* 1642 */         skipSpace = false;
/* 1643 */         sawNonWS = true;
/*      */       }
/*      */     }
/* 1646 */     if (skipSpace) {
/* 1647 */       if (this.fNormalizedStr.length > 1)
/*      */       {
/* 1649 */         this.fNormalizedStr.length -= 1;
/* 1650 */         trailing = true;
/* 1651 */       } else if ((leading) && (!this.fFirstChunk))
/*      */       {
/* 1654 */         trailing = true;
/*      */       }
/*      */     }
/*      */ 
/* 1658 */     if ((this.fNormalizedStr.length > 1) && 
/* 1659 */       (!this.fFirstChunk) && (this.fWhiteSpace == 2)) {
/* 1660 */       if (this.fTrailing)
/*      */       {
/* 1663 */         this.fNormalizedStr.offset = 0;
/* 1664 */         this.fNormalizedStr.ch[0] = ' ';
/* 1665 */       } else if (leading)
/*      */       {
/* 1668 */         this.fNormalizedStr.offset = 0;
/* 1669 */         this.fNormalizedStr.ch[0] = ' ';
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1675 */     this.fNormalizedStr.length -= this.fNormalizedStr.offset;
/*      */ 
/* 1677 */     this.fTrailing = trailing;
/*      */ 
/* 1679 */     if ((trailing) || (sawNonWS))
/* 1680 */       this.fFirstChunk = false;
/*      */   }
/*      */ 
/*      */   private void normalizeWhitespace(String value, boolean collapse) {
/* 1684 */     boolean skipSpace = collapse;
/*      */ 
/* 1686 */     int size = value.length();
/*      */ 
/* 1689 */     if ((this.fNormalizedStr.ch == null) || (this.fNormalizedStr.ch.length < size)) {
/* 1690 */       this.fNormalizedStr.ch = new char[size];
/*      */     }
/* 1692 */     this.fNormalizedStr.offset = 0;
/* 1693 */     this.fNormalizedStr.length = 0;
/*      */ 
/* 1695 */     for (int i = 0; i < size; i++) {
/* 1696 */       char c = value.charAt(i);
/* 1697 */       if (XMLChar.isSpace(c)) {
/* 1698 */         if (!skipSpace)
/*      */         {
/* 1700 */           this.fNormalizedStr.ch[(this.fNormalizedStr.length++)] = ' ';
/* 1701 */           skipSpace = collapse;
/*      */         }
/*      */       } else {
/* 1704 */         this.fNormalizedStr.ch[(this.fNormalizedStr.length++)] = c;
/* 1705 */         skipSpace = false;
/*      */       }
/*      */     }
/* 1708 */     if ((skipSpace) && 
/* 1709 */       (this.fNormalizedStr.length != 0))
/*      */     {
/* 1711 */       this.fNormalizedStr.length -= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleIgnorableWhitespace(XMLString text)
/*      */   {
/* 1718 */     if (this.fSkipValidationDepth >= 0);
/*      */   }
/*      */ 
/*      */   Augmentations handleStartElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */   {
/* 1734 */     if ((this.fElementDepth == -1) && (this.fValidationManager.isGrammarFound()) && 
/* 1735 */       (this.fSchemaType == null))
/*      */     {
/* 1740 */       this.fSchemaDynamicValidation = true;
/*      */     }
/*      */ 
/* 1753 */     String sLocation = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_SCHEMALOCATION);
/*      */ 
/* 1755 */     String nsLocation = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
/*      */ 
/* 1760 */     storeLocations(sLocation, nsLocation);
/*      */ 
/* 1764 */     if (this.fSkipValidationDepth >= 0) {
/* 1765 */       this.fElementDepth += 1;
/* 1766 */       if (this.fAugPSVI)
/* 1767 */         augs = getEmptyAugs(augs);
/* 1768 */       return augs;
/*      */     }
/*      */ 
/* 1772 */     SchemaGrammar sGrammar = findSchemaGrammar((short)5, element.uri, null, element, attributes);
/*      */ 
/* 1785 */     Object decl = null;
/* 1786 */     if (this.fCurrentCM != null) {
/* 1787 */       decl = this.fCurrentCM.oneTransition(element, this.fCurrCMState, this.fSubGroupHandler);
/*      */ 
/* 1789 */       if (this.fCurrCMState[0] == -1) {
/* 1790 */         XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/*      */         Vector next;
/* 1793 */         if ((ctype.fParticle != null) && ((next = this.fCurrentCM.whatCanGoHere(this.fCurrCMState)).size() > 0))
/*      */         {
/* 1795 */           String expected = expectedStr(next);
/* 1796 */           reportSchemaError("cvc-complex-type.2.4.a", new Object[] { element.rawname, expected });
/*      */         }
/*      */         else
/*      */         {
/* 1800 */           reportSchemaError("cvc-complex-type.2.4.d", new Object[] { element.rawname });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1806 */     if (this.fElementDepth != -1) {
/* 1807 */       ensureStackCapacity();
/* 1808 */       this.fSubElementStack[this.fElementDepth] = true;
/* 1809 */       this.fSubElement = false;
/* 1810 */       this.fElemDeclStack[this.fElementDepth] = this.fCurrentElemDecl;
/* 1811 */       this.fNilStack[this.fElementDepth] = this.fNil;
/* 1812 */       this.fNotationStack[this.fElementDepth] = this.fNotation;
/* 1813 */       this.fTypeStack[this.fElementDepth] = this.fCurrentType;
/* 1814 */       this.fStrictAssessStack[this.fElementDepth] = this.fStrictAssess;
/* 1815 */       this.fCMStack[this.fElementDepth] = this.fCurrentCM;
/* 1816 */       this.fCMStateStack[this.fElementDepth] = this.fCurrCMState;
/* 1817 */       this.fSawTextStack[this.fElementDepth] = this.fSawText;
/* 1818 */       this.fStringContent[this.fElementDepth] = this.fSawCharacters;
/*      */     }
/*      */ 
/* 1823 */     this.fElementDepth += 1;
/* 1824 */     this.fCurrentElemDecl = null;
/* 1825 */     XSWildcardDecl wildcard = null;
/* 1826 */     this.fCurrentType = null;
/* 1827 */     this.fStrictAssess = true;
/* 1828 */     this.fNil = false;
/* 1829 */     this.fNotation = null;
/*      */ 
/* 1832 */     this.fBuffer.setLength(0);
/* 1833 */     this.fSawText = false;
/* 1834 */     this.fSawCharacters = false;
/*      */ 
/* 1838 */     if (decl != null) {
/* 1839 */       if ((decl instanceof XSElementDecl))
/* 1840 */         this.fCurrentElemDecl = ((XSElementDecl)decl);
/*      */       else {
/* 1842 */         wildcard = (XSWildcardDecl)decl;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1847 */     if ((wildcard != null) && (wildcard.fProcessContents == 2)) {
/* 1848 */       this.fSkipValidationDepth = this.fElementDepth;
/* 1849 */       if (this.fAugPSVI)
/* 1850 */         augs = getEmptyAugs(augs);
/* 1851 */       return augs;
/*      */     }
/*      */ 
/* 1857 */     if ((this.fCurrentElemDecl == null) && 
/* 1858 */       (sGrammar != null)) {
/* 1859 */       this.fCurrentElemDecl = sGrammar.getGlobalElementDecl(element.localpart);
/*      */     }
/*      */ 
/* 1863 */     if (this.fCurrentElemDecl != null)
/*      */     {
/* 1865 */       this.fCurrentType = this.fCurrentElemDecl.fType;
/*      */     }
/*      */ 
/* 1869 */     String xsiType = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_TYPE);
/*      */ 
/* 1872 */     if ((this.fCurrentType == null) && (xsiType == null))
/*      */     {
/* 1876 */       if (this.fElementDepth == 0)
/*      */       {
/* 1879 */         if ((this.fDynamicValidation) || (this.fSchemaDynamicValidation))
/*      */         {
/* 1886 */           if (this.fDocumentSource != null) {
/* 1887 */             this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
/* 1888 */             if (this.fDocumentHandler != null) {
/* 1889 */               this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
/*      */             }
/* 1891 */             this.fElementDepth = -2;
/* 1892 */             return augs;
/*      */           }
/*      */ 
/* 1895 */           this.fSkipValidationDepth = this.fElementDepth;
/* 1896 */           if (this.fAugPSVI)
/* 1897 */             augs = getEmptyAugs(augs);
/* 1898 */           return augs;
/*      */         }
/*      */ 
/* 1906 */         this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "cvc-elt.1", new Object[] { element.rawname }, (short)1);
/*      */       }
/* 1915 */       else if ((wildcard != null) && (wildcard.fProcessContents == 1))
/*      */       {
/* 1917 */         reportSchemaError("cvc-complex-type.2.4.c", new Object[] { element.rawname });
/*      */       }
/*      */ 
/* 1922 */       this.fCurrentType = SchemaGrammar.fAnyType;
/* 1923 */       this.fStrictAssess = false;
/* 1924 */       this.fNFullValidationDepth = this.fElementDepth;
/*      */ 
/* 1926 */       this.fAppendBuffer = false;
/*      */ 
/* 1931 */       this.fXSIErrorReporter.pushContext();
/*      */     }
/*      */     else
/*      */     {
/* 1936 */       this.fXSIErrorReporter.pushContext();
/*      */ 
/* 1939 */       if (xsiType != null) {
/* 1940 */         XSTypeDefinition oldType = this.fCurrentType;
/* 1941 */         this.fCurrentType = getAndCheckXsiType(element, xsiType, attributes);
/*      */ 
/* 1943 */         if (this.fCurrentType == null) {
/* 1944 */           if (oldType == null)
/* 1945 */             this.fCurrentType = SchemaGrammar.fAnyType;
/*      */           else {
/* 1947 */             this.fCurrentType = oldType;
/*      */           }
/*      */         }
/*      */       }
/* 1951 */       this.fNNoneValidationDepth = this.fElementDepth;
/*      */ 
/* 1953 */       if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.getConstraintType() == 2))
/*      */       {
/* 1955 */         this.fAppendBuffer = true;
/*      */       }
/* 1958 */       else if (this.fCurrentType.getTypeCategory() == 16) {
/* 1959 */         this.fAppendBuffer = true;
/*      */       }
/*      */       else {
/* 1962 */         XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/* 1963 */         this.fAppendBuffer = (ctype.fContentType == 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1969 */     if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.getAbstract())) {
/* 1970 */       reportSchemaError("cvc-elt.2", new Object[] { element.rawname });
/*      */     }
/*      */ 
/* 1973 */     if (this.fElementDepth == 0) {
/* 1974 */       this.fValidationRoot = element.rawname;
/*      */     }
/*      */ 
/* 1978 */     if (this.fNormalizeData)
/*      */     {
/* 1980 */       this.fFirstChunk = true;
/* 1981 */       this.fTrailing = false;
/* 1982 */       this.fUnionType = false;
/* 1983 */       this.fWhiteSpace = -1;
/*      */     }
/*      */ 
/* 1988 */     if (this.fCurrentType.getTypeCategory() == 15) {
/* 1989 */       XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/* 1990 */       if (ctype.getAbstract()) {
/* 1991 */         reportSchemaError("cvc-type.2", new Object[] { element.rawname });
/*      */       }
/* 1993 */       if (this.fNormalizeData)
/*      */       {
/* 1996 */         if (ctype.fContentType == 1) {
/* 1997 */           if (ctype.fXSSimpleType.getVariety() == 3)
/* 1998 */             this.fUnionType = true;
/*      */           else {
/*      */             try {
/* 2001 */               this.fWhiteSpace = ctype.fXSSimpleType.getWhitespace();
/*      */             }
/*      */             catch (DatatypeException e)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2010 */     else if (this.fNormalizeData)
/*      */     {
/* 2012 */       XSSimpleType dv = (XSSimpleType)this.fCurrentType;
/* 2013 */       if (dv.getVariety() == 3)
/* 2014 */         this.fUnionType = true;
/*      */       else {
/*      */         try {
/* 2017 */           this.fWhiteSpace = dv.getWhitespace();
/*      */         }
/*      */         catch (DatatypeException e)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2025 */     this.fCurrentCM = null;
/* 2026 */     if (this.fCurrentType.getTypeCategory() == 15) {
/* 2027 */       this.fCurrentCM = ((XSComplexTypeDecl)this.fCurrentType).getContentModel(this.fCMBuilder);
/*      */     }
/*      */ 
/* 2031 */     this.fCurrCMState = null;
/* 2032 */     if (this.fCurrentCM != null) {
/* 2033 */       this.fCurrCMState = this.fCurrentCM.startContentModel();
/*      */     }
/*      */ 
/* 2036 */     String xsiNil = attributes.getValue(SchemaSymbols.URI_XSI, SchemaSymbols.XSI_NIL);
/*      */ 
/* 2038 */     if ((xsiNil != null) && (this.fCurrentElemDecl != null)) {
/* 2039 */       this.fNil = getXsiNil(element, xsiNil);
/*      */     }
/*      */ 
/* 2043 */     XSAttributeGroupDecl attrGrp = null;
/* 2044 */     if (this.fCurrentType.getTypeCategory() == 15) {
/* 2045 */       XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/* 2046 */       attrGrp = ctype.getAttrGrp();
/*      */     }
/*      */ 
/* 2049 */     this.fValueStoreCache.startElement();
/* 2050 */     this.fMatcherStack.pushContext();
/* 2051 */     if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.fIDCPos > 0)) {
/* 2052 */       this.fIdConstraint = true;
/*      */ 
/* 2054 */       this.fValueStoreCache.initValueStoresFor(this.fCurrentElemDecl, this);
/*      */     }
/* 2056 */     processAttributes(element, attributes, attrGrp);
/*      */ 
/* 2059 */     if (attrGrp != null) {
/* 2060 */       addDefaultAttributes(element, attributes, attrGrp);
/*      */     }
/*      */ 
/* 2064 */     int count = this.fMatcherStack.getMatcherCount();
/* 2065 */     for (int i = 0; i < count; i++) {
/* 2066 */       XPathMatcher matcher = this.fMatcherStack.getMatcherAt(i);
/* 2067 */       matcher.startElement(element, attributes);
/*      */     }
/*      */ 
/* 2070 */     if (this.fAugPSVI) {
/* 2071 */       augs = getEmptyAugs(augs);
/*      */ 
/* 2074 */       this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
/*      */ 
/* 2076 */       this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
/*      */ 
/* 2078 */       this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
/*      */ 
/* 2080 */       this.fCurrentPSVI.fNotation = this.fNotation;
/*      */     }
/*      */ 
/* 2083 */     return augs;
/*      */   }
/*      */ 
/*      */   Augmentations handleEndElement(QName element, Augmentations augs)
/*      */   {
/* 2098 */     if (this.fSkipValidationDepth >= 0)
/*      */     {
/* 2101 */       if ((this.fSkipValidationDepth == this.fElementDepth) && (this.fSkipValidationDepth > 0))
/*      */       {
/* 2103 */         this.fNFullValidationDepth = (this.fSkipValidationDepth - 1);
/* 2104 */         this.fSkipValidationDepth = -1;
/* 2105 */         this.fElementDepth -= 1;
/* 2106 */         this.fSubElement = this.fSubElementStack[this.fElementDepth];
/* 2107 */         this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
/* 2108 */         this.fNil = this.fNilStack[this.fElementDepth];
/* 2109 */         this.fNotation = this.fNotationStack[this.fElementDepth];
/* 2110 */         this.fCurrentType = this.fTypeStack[this.fElementDepth];
/* 2111 */         this.fCurrentCM = this.fCMStack[this.fElementDepth];
/* 2112 */         this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
/* 2113 */         this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
/* 2114 */         this.fSawText = this.fSawTextStack[this.fElementDepth];
/* 2115 */         this.fSawCharacters = this.fStringContent[this.fElementDepth];
/*      */       }
/*      */       else {
/* 2118 */         this.fElementDepth -= 1;
/*      */       }
/*      */ 
/* 2126 */       if ((this.fElementDepth == -1) && (this.fFullChecking)) {
/* 2127 */         XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
/*      */       }
/*      */ 
/* 2134 */       if (this.fAugPSVI)
/* 2135 */         augs = getEmptyAugs(augs);
/* 2136 */       return augs;
/*      */     }
/*      */ 
/* 2140 */     processElementContent(element);
/*      */ 
/* 2146 */     int oldCount = this.fMatcherStack.getMatcherCount();
/* 2147 */     for (int i = oldCount - 1; i >= 0; i--) {
/* 2148 */       XPathMatcher matcher = this.fMatcherStack.getMatcherAt(i);
/* 2149 */       if (this.fCurrentElemDecl == null) {
/* 2150 */         matcher.endElement(element, null, false, this.fValidatedInfo.actualValue, this.fValidatedInfo.actualValueType, this.fValidatedInfo.itemValueTypes);
/*      */       }
/*      */       else {
/* 2153 */         matcher.endElement(element, this.fCurrentType, this.fCurrentElemDecl.getNillable(), this.fDefaultValue == null ? this.fValidatedInfo.actualValue : this.fCurrentElemDecl.fDefault.actualValue, this.fDefaultValue == null ? this.fValidatedInfo.actualValueType : this.fCurrentElemDecl.fDefault.actualValueType, this.fDefaultValue == null ? this.fValidatedInfo.itemValueTypes : this.fCurrentElemDecl.fDefault.itemValueTypes);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2168 */     if (this.fMatcherStack.size() > 0) {
/* 2169 */       this.fMatcherStack.popContext();
/*      */     }
/*      */ 
/* 2172 */     int newCount = this.fMatcherStack.getMatcherCount();
/*      */ 
/* 2174 */     for (int i = oldCount - 1; i >= newCount; i--) {
/* 2175 */       XPathMatcher matcher = this.fMatcherStack.getMatcherAt(i);
/* 2176 */       if ((matcher instanceof Selector.Matcher)) {
/* 2177 */         Selector.Matcher selMatcher = (Selector.Matcher)matcher;
/*      */         IdentityConstraint id;
/* 2179 */         if (((id = selMatcher.getIdentityConstraint()) != null) && (id.getCategory() != 2))
/*      */         {
/* 2181 */           this.fValueStoreCache.transplant(id, selMatcher.getInitialDepth());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2187 */     for (int i = oldCount - 1; i >= newCount; i--) {
/* 2188 */       XPathMatcher matcher = this.fMatcherStack.getMatcherAt(i);
/* 2189 */       if ((matcher instanceof Selector.Matcher)) {
/* 2190 */         Selector.Matcher selMatcher = (Selector.Matcher)matcher;
/*      */         IdentityConstraint id;
/* 2192 */         if (((id = selMatcher.getIdentityConstraint()) != null) && (id.getCategory() == 2))
/*      */         {
/* 2194 */           ValueStoreBase values = this.fValueStoreCache.getValueStoreFor(id, selMatcher.getInitialDepth());
/*      */ 
/* 2196 */           if (values != null)
/* 2197 */             values.endDocumentFragment();
/*      */         }
/*      */       }
/*      */     }
/* 2201 */     this.fValueStoreCache.endElement();
/*      */ 
/* 2203 */     SchemaGrammar[] grammars = null;
/*      */ 
/* 2205 */     if (this.fElementDepth == 0)
/*      */     {
/* 2207 */       String invIdRef = this.fValidationState.checkIDRefID();
/* 2208 */       this.fValidationState.resetIDTables();
/* 2209 */       if (invIdRef != null) {
/* 2210 */         reportSchemaError("cvc-id.1", new Object[] { invIdRef });
/*      */       }
/*      */ 
/* 2213 */       if (this.fFullChecking) {
/* 2214 */         XSConstraints.fullSchemaChecking(this.fGrammarBucket, this.fSubGroupHandler, this.fCMBuilder, this.fXSIErrorReporter.fErrorReporter);
/*      */       }
/*      */ 
/* 2221 */       grammars = this.fGrammarBucket.getGrammars();
/*      */ 
/* 2223 */       if (this.fGrammarPool != null)
/*      */       {
/* 2225 */         for (int k = 0; k < grammars.length; k++) {
/* 2226 */           grammars[k].setImmutable(true);
/*      */         }
/* 2228 */         this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", grammars);
/*      */       }
/* 2230 */       augs = endElementPSVI(true, grammars, augs);
/*      */     } else {
/* 2232 */       augs = endElementPSVI(false, grammars, augs);
/*      */ 
/* 2235 */       this.fElementDepth -= 1;
/*      */ 
/* 2238 */       this.fSubElement = this.fSubElementStack[this.fElementDepth];
/* 2239 */       this.fCurrentElemDecl = this.fElemDeclStack[this.fElementDepth];
/* 2240 */       this.fNil = this.fNilStack[this.fElementDepth];
/* 2241 */       this.fNotation = this.fNotationStack[this.fElementDepth];
/* 2242 */       this.fCurrentType = this.fTypeStack[this.fElementDepth];
/* 2243 */       this.fCurrentCM = this.fCMStack[this.fElementDepth];
/* 2244 */       this.fStrictAssess = this.fStrictAssessStack[this.fElementDepth];
/* 2245 */       this.fCurrCMState = this.fCMStateStack[this.fElementDepth];
/* 2246 */       this.fSawText = this.fSawTextStack[this.fElementDepth];
/* 2247 */       this.fSawCharacters = this.fStringContent[this.fElementDepth];
/*      */ 
/* 2254 */       this.fWhiteSpace = -1;
/*      */ 
/* 2257 */       this.fAppendBuffer = false;
/*      */ 
/* 2259 */       this.fUnionType = false;
/*      */     }
/*      */ 
/* 2262 */     return augs;
/*      */   }
/*      */ 
/*      */   final Augmentations endElementPSVI(boolean root, SchemaGrammar[] grammars, Augmentations augs)
/*      */   {
/* 2270 */     if (this.fAugPSVI) {
/* 2271 */       augs = getEmptyAugs(augs);
/*      */ 
/* 2274 */       this.fCurrentPSVI.fDeclaration = this.fCurrentElemDecl;
/* 2275 */       this.fCurrentPSVI.fTypeDecl = this.fCurrentType;
/* 2276 */       this.fCurrentPSVI.fNotation = this.fNotation;
/* 2277 */       this.fCurrentPSVI.fValidationContext = this.fValidationRoot;
/*      */ 
/* 2282 */       if (this.fElementDepth > this.fNFullValidationDepth) {
/* 2283 */         this.fCurrentPSVI.fValidationAttempted = 2;
/*      */       }
/* 2288 */       else if (this.fElementDepth > this.fNNoneValidationDepth) {
/* 2289 */         this.fCurrentPSVI.fValidationAttempted = 0;
/*      */       }
/*      */       else
/*      */       {
/* 2293 */         this.fCurrentPSVI.fValidationAttempted = 1;
/* 2294 */         this.fNFullValidationDepth = (this.fNNoneValidationDepth = this.fElementDepth - 1);
/*      */       }
/*      */ 
/* 2297 */       if (this.fDefaultValue != null)
/* 2298 */         this.fCurrentPSVI.fSpecified = true;
/* 2299 */       this.fCurrentPSVI.fNil = this.fNil;
/* 2300 */       this.fCurrentPSVI.fMemberType = this.fValidatedInfo.memberType;
/* 2301 */       this.fCurrentPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
/* 2302 */       this.fCurrentPSVI.fActualValue = this.fValidatedInfo.actualValue;
/* 2303 */       this.fCurrentPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
/* 2304 */       this.fCurrentPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
/*      */ 
/* 2306 */       if (this.fStrictAssess)
/*      */       {
/* 2311 */         String[] errors = this.fXSIErrorReporter.mergeContext();
/*      */ 
/* 2314 */         this.fCurrentPSVI.fErrorCodes = errors;
/*      */ 
/* 2316 */         this.fCurrentPSVI.fValidity = (errors == null ? 2 : 1);
/*      */       }
/*      */       else
/*      */       {
/* 2320 */         this.fCurrentPSVI.fValidity = 0;
/*      */ 
/* 2324 */         this.fXSIErrorReporter.popContext();
/*      */       }
/*      */ 
/* 2327 */       if (root)
/*      */       {
/* 2329 */         this.fCurrentPSVI.fGrammars = grammars;
/* 2330 */         this.fCurrentPSVI.fSchemaInformation = null;
/*      */       }
/*      */     }
/*      */ 
/* 2334 */     return augs;
/*      */   }
/*      */ 
/*      */   Augmentations getEmptyAugs(Augmentations augs)
/*      */   {
/* 2339 */     if (augs == null) {
/* 2340 */       augs = this.fAugmentations;
/* 2341 */       augs.removeAllItems();
/*      */     }
/* 2343 */     augs.putItem("ELEMENT_PSVI", this.fCurrentPSVI);
/* 2344 */     this.fCurrentPSVI.reset();
/*      */ 
/* 2346 */     return augs;
/*      */   }
/*      */ 
/*      */   void storeLocations(String sLocation, String nsLocation) {
/* 2350 */     if ((sLocation != null) && 
/* 2351 */       (!XMLSchemaLoader.tokenizeSchemaLocationStr(sLocation, this.fLocationPairs)))
/*      */     {
/* 2353 */       this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "SchemaLocation", new Object[] { sLocation }, (short)0);
/*      */     }
/*      */ 
/* 2360 */     if (nsLocation != null) {
/* 2361 */       XMLSchemaLoader.LocationArray la = (XMLSchemaLoader.LocationArray)this.fLocationPairs.get(XMLSymbols.EMPTY_STRING);
/*      */ 
/* 2363 */       if (la == null) {
/* 2364 */         la = new XMLSchemaLoader.LocationArray();
/* 2365 */         this.fLocationPairs.put(XMLSymbols.EMPTY_STRING, la);
/*      */       }
/* 2367 */       la.addLocation(nsLocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   SchemaGrammar findSchemaGrammar(short contextType, String namespace, QName enclosingElement, QName triggeringComponet, XMLAttributes attributes)
/*      */   {
/* 2381 */     SchemaGrammar grammar = null;
/*      */ 
/* 2383 */     grammar = this.fGrammarBucket.getGrammar(namespace);
/*      */ 
/* 2385 */     if (grammar == null) {
/* 2386 */       this.fXSDDescription.setNamespace(namespace);
/*      */ 
/* 2388 */       if (this.fGrammarPool != null) {
/* 2389 */         grammar = (SchemaGrammar)this.fGrammarPool.retrieveGrammar(this.fXSDDescription);
/* 2390 */         if (grammar != null)
/*      */         {
/* 2393 */           if (!this.fGrammarBucket.putGrammar(grammar, true, this.fNamespaceGrowth))
/*      */           {
/* 2396 */             this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "GrammarConflict", null, (short)0);
/*      */ 
/* 2401 */             grammar = null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2406 */     if (((grammar == null) && (!this.fUseGrammarPoolOnly)) || (this.fNamespaceGrowth)) {
/* 2407 */       this.fXSDDescription.reset();
/* 2408 */       this.fXSDDescription.fContextType = contextType;
/* 2409 */       this.fXSDDescription.setNamespace(namespace);
/* 2410 */       this.fXSDDescription.fEnclosedElementName = enclosingElement;
/* 2411 */       this.fXSDDescription.fTriggeringComponent = triggeringComponet;
/* 2412 */       this.fXSDDescription.fAttributes = attributes;
/* 2413 */       if (this.fLocator != null) {
/* 2414 */         this.fXSDDescription.setBaseSystemId(this.fLocator.getExpandedSystemId());
/*      */       }
/*      */ 
/* 2417 */       Hashtable locationPairs = this.fLocationPairs;
/* 2418 */       Object locationArray = locationPairs.get(namespace == null ? XMLSymbols.EMPTY_STRING : namespace);
/*      */ 
/* 2420 */       if (locationArray != null) {
/* 2421 */         String[] temp = ((XMLSchemaLoader.LocationArray)locationArray).getLocationArray();
/* 2422 */         if (temp.length != 0) {
/* 2423 */           setLocationHints(this.fXSDDescription, temp, grammar);
/*      */         }
/*      */       }
/*      */ 
/* 2427 */       if ((grammar == null) || (this.fXSDDescription.fLocationHints != null)) {
/* 2428 */         boolean toParseSchema = true;
/* 2429 */         if (grammar != null)
/*      */         {
/* 2431 */           locationPairs = EMPTY_TABLE;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 2436 */           XMLInputSource xis = XMLSchemaLoader.resolveDocument(this.fXSDDescription, locationPairs, this.fEntityResolver);
/*      */ 
/* 2441 */           if ((grammar != null) && (this.fNamespaceGrowth))
/*      */           {
/*      */             try
/*      */             {
/* 2445 */               if (grammar.getDocumentLocations().contains(XMLEntityManager.expandSystemId(xis.getSystemId(), xis.getBaseSystemId(), false)))
/* 2446 */                 toParseSchema = false;
/*      */             }
/*      */             catch (URI.MalformedURIException e)
/*      */             {
/*      */             }
/*      */           }
/* 2452 */           if (toParseSchema)
/* 2453 */             grammar = this.fSchemaLoader.loadSchema(this.fXSDDescription, xis, this.fLocationPairs);
/*      */         }
/*      */         catch (IOException ex) {
/* 2456 */           String[] locationHints = this.fXSDDescription.getLocationHints();
/* 2457 */           this.fXSIErrorReporter.fErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", "schema_reference.4", new Object[] { locationHints != null ? locationHints[0] : XMLSymbols.EMPTY_STRING }, (short)0);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2466 */     return grammar;
/*      */   }
/*      */ 
/*      */   private void setLocationHints(XSDDescription desc, String[] locations, SchemaGrammar grammar) {
/* 2470 */     int length = locations.length;
/* 2471 */     if (grammar == null) {
/* 2472 */       this.fXSDDescription.fLocationHints = new String[length];
/* 2473 */       System.arraycopy(locations, 0, this.fXSDDescription.fLocationHints, 0, length);
/*      */     }
/*      */     else {
/* 2476 */       setLocationHints(desc, locations, grammar.getDocumentLocations());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setLocationHints(XSDDescription desc, String[] locations, StringList docLocations) {
/* 2481 */     int length = locations.length;
/* 2482 */     String[] hints = new String[length];
/* 2483 */     int counter = 0;
/*      */ 
/* 2485 */     for (int i = 0; i < length; i++) {
/*      */       try {
/* 2487 */         String id = XMLEntityManager.expandSystemId(locations[i], desc.getBaseSystemId(), false);
/* 2488 */         if (!docLocations.contains(id)) {
/* 2489 */           hints[(counter++)] = locations[i];
/*      */         }
/*      */       }
/*      */       catch (URI.MalformedURIException e)
/*      */       {
/*      */       }
/*      */     }
/* 2496 */     if (counter > 0)
/* 2497 */       if (counter == length) {
/* 2498 */         this.fXSDDescription.fLocationHints = hints;
/*      */       }
/*      */       else {
/* 2501 */         this.fXSDDescription.fLocationHints = new String[counter];
/* 2502 */         System.arraycopy(hints, 0, this.fXSDDescription.fLocationHints, 0, counter);
/*      */       }
/*      */   }
/*      */ 
/*      */   XSTypeDefinition getAndCheckXsiType(QName element, String xsiType, XMLAttributes attributes)
/*      */   {
/* 2515 */     QName typeName = null;
/*      */     try {
/* 2517 */       typeName = (QName)this.fQNameDV.validate(xsiType, this.fValidationState, null);
/*      */     } catch (InvalidDatatypeValueException e) {
/* 2519 */       reportSchemaError(e.getKey(), e.getArgs());
/* 2520 */       reportSchemaError("cvc-elt.4.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_TYPE, xsiType });
/*      */ 
/* 2526 */       return null;
/*      */     }
/*      */ 
/* 2530 */     XSTypeDefinition type = null;
/*      */ 
/* 2532 */     if (typeName.uri == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
/* 2533 */       type = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(typeName.localpart);
/*      */     }
/*      */ 
/* 2536 */     if (type == null)
/*      */     {
/* 2538 */       SchemaGrammar grammar = findSchemaGrammar((short)7, typeName.uri, element, typeName, attributes);
/*      */ 
/* 2546 */       if (grammar != null) {
/* 2547 */         type = grammar.getGlobalTypeDecl(typeName.localpart);
/*      */       }
/*      */     }
/* 2550 */     if (type == null) {
/* 2551 */       reportSchemaError("cvc-elt.4.2", new Object[] { element.rawname, xsiType });
/* 2552 */       return null;
/*      */     }
/*      */ 
/* 2557 */     if (this.fCurrentType != null)
/*      */     {
/* 2559 */       short block = this.fCurrentElemDecl.fBlock;
/* 2560 */       if (this.fCurrentType.getTypeCategory() == 15)
/* 2561 */         block = (short)(block | ((XSComplexTypeDecl)this.fCurrentType).fBlock);
/* 2562 */       if (!XSConstraints.checkTypeDerivationOk(type, this.fCurrentType, block)) {
/* 2563 */         reportSchemaError("cvc-elt.4.3", new Object[] { element.rawname, xsiType, this.fCurrentType.getName() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2568 */     return type;
/*      */   }
/*      */ 
/*      */   boolean getXsiNil(QName element, String xsiNil)
/*      */   {
/* 2575 */     if ((this.fCurrentElemDecl != null) && (!this.fCurrentElemDecl.getNillable())) {
/* 2576 */       reportSchemaError("cvc-elt.3.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
/*      */     }
/*      */     else
/*      */     {
/* 2585 */       String value = XMLChar.trim(xsiNil);
/* 2586 */       if ((value.equals("true")) || (value.equals("1")))
/*      */       {
/* 2588 */         if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.getConstraintType() == 2))
/*      */         {
/* 2590 */           reportSchemaError("cvc-elt.3.2.2", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
/*      */         }
/*      */ 
/* 2596 */         return true;
/*      */       }
/*      */     }
/* 2599 */     return false;
/*      */   }
/*      */ 
/*      */   void processAttributes(QName element, XMLAttributes attributes, XSAttributeGroupDecl attrGrp)
/*      */   {
/* 2609 */     String wildcardIDName = null;
/*      */ 
/* 2612 */     int attCount = attributes.getLength();
/*      */ 
/* 2614 */     Augmentations augs = null;
/* 2615 */     AttributePSVImpl attrPSVI = null;
/*      */ 
/* 2617 */     boolean isSimple = (this.fCurrentType == null) || (this.fCurrentType.getTypeCategory() == 16);
/*      */ 
/* 2620 */     XSObjectList attrUses = null;
/* 2621 */     int useCount = 0;
/* 2622 */     XSWildcardDecl attrWildcard = null;
/* 2623 */     if (!isSimple) {
/* 2624 */       attrUses = attrGrp.getAttributeUses();
/* 2625 */       useCount = attrUses.getLength();
/* 2626 */       attrWildcard = attrGrp.fAttributeWC;
/*      */     }
/*      */ 
/* 2632 */     for (int index = 0; index < attCount; index++)
/*      */     {
/* 2634 */       attributes.getName(index, this.fTempQName);
/*      */ 
/* 2640 */       if ((this.fAugPSVI) || (this.fIdConstraint)) {
/* 2641 */         augs = attributes.getAugmentations(index);
/* 2642 */         attrPSVI = (AttributePSVImpl)augs.getItem("ATTRIBUTE_PSVI");
/* 2643 */         if (attrPSVI != null) {
/* 2644 */           attrPSVI.reset();
/*      */         } else {
/* 2646 */           attrPSVI = new AttributePSVImpl();
/* 2647 */           augs.putItem("ATTRIBUTE_PSVI", attrPSVI);
/*      */         }
/*      */ 
/* 2650 */         attrPSVI.fValidationContext = this.fValidationRoot;
/*      */       }
/*      */ 
/* 2659 */       if (this.fTempQName.uri == SchemaSymbols.URI_XSI) {
/* 2660 */         XSAttributeDecl attrDecl = null;
/* 2661 */         if (this.fTempQName.localpart == SchemaSymbols.XSI_SCHEMALOCATION) {
/* 2662 */           attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_SCHEMALOCATION);
/*      */         }
/* 2665 */         else if (this.fTempQName.localpart == SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION) {
/* 2666 */           attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION);
/*      */         }
/* 2669 */         else if (this.fTempQName.localpart == SchemaSymbols.XSI_NIL)
/* 2670 */           attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_NIL);
/* 2671 */         else if (this.fTempQName.localpart == SchemaSymbols.XSI_TYPE)
/* 2672 */           attrDecl = SchemaGrammar.SG_XSI.getGlobalAttributeDecl(SchemaSymbols.XSI_TYPE);
/* 2673 */         if (attrDecl != null) {
/* 2674 */           processOneAttribute(element, attributes, index, attrDecl, null, attrPSVI);
/* 2675 */           continue;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2680 */       if ((this.fTempQName.rawname != XMLSymbols.PREFIX_XMLNS) && (!this.fTempQName.rawname.startsWith("xmlns:")))
/*      */       {
/* 2686 */         if (isSimple) {
/* 2687 */           reportSchemaError("cvc-type.3.1.1", new Object[] { element.rawname, this.fTempQName.rawname });
/*      */         }
/*      */         else
/*      */         {
/* 2694 */           XSAttributeUseImpl currUse = null;
/* 2695 */           for (int i = 0; i < useCount; i++) {
/* 2696 */             XSAttributeUseImpl oneUse = (XSAttributeUseImpl)attrUses.item(i);
/* 2697 */             if ((oneUse.fAttrDecl.fName == this.fTempQName.localpart) && (oneUse.fAttrDecl.fTargetNamespace == this.fTempQName.uri))
/*      */             {
/* 2699 */               currUse = oneUse;
/* 2700 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2709 */           if (currUse == null)
/*      */           {
/* 2712 */             if ((attrWildcard == null) || (!attrWildcard.allowNamespace(this.fTempQName.uri)))
/*      */             {
/* 2714 */               reportSchemaError("cvc-complex-type.3.2.2", new Object[] { element.rawname, this.fTempQName.rawname });
/*      */ 
/* 2717 */               continue;
/*      */             }
/*      */           }
/*      */ 
/* 2721 */           XSAttributeDecl currDecl = null;
/* 2722 */           if (currUse != null) {
/* 2723 */             currDecl = currUse.fAttrDecl;
/*      */           }
/*      */           else
/*      */           {
/* 2727 */             if (attrWildcard.fProcessContents == 2)
/*      */             {
/*      */               continue;
/*      */             }
/* 2731 */             SchemaGrammar grammar = findSchemaGrammar((short)6, this.fTempQName.uri, element, this.fTempQName, attributes);
/*      */ 
/* 2739 */             if (grammar != null) {
/* 2740 */               currDecl = grammar.getGlobalAttributeDecl(this.fTempQName.localpart);
/*      */             }
/*      */ 
/* 2744 */             if (currDecl == null)
/*      */             {
/* 2746 */               if (attrWildcard.fProcessContents != 1) continue;
/* 2747 */               reportSchemaError("cvc-complex-type.3.2.2", new Object[] { element.rawname, this.fTempQName.rawname }); continue;
/*      */             }
/*      */ 
/* 2757 */             if ((currDecl.fType.getTypeCategory() == 16) && (currDecl.fType.isIDType()))
/*      */             {
/* 2759 */               if (wildcardIDName != null) {
/* 2760 */                 reportSchemaError("cvc-complex-type.5.1", new Object[] { element.rawname, currDecl.fName, wildcardIDName });
/*      */               }
/*      */               else
/*      */               {
/* 2764 */                 wildcardIDName = currDecl.fName;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 2769 */           processOneAttribute(element, attributes, index, currDecl, currUse, attrPSVI);
/*      */         }
/*      */       }
/*      */     }
/* 2773 */     if ((!isSimple) && (attrGrp.fIDAttrName != null) && (wildcardIDName != null))
/* 2774 */       reportSchemaError("cvc-complex-type.5.2", new Object[] { element.rawname, wildcardIDName, attrGrp.fIDAttrName });
/*      */   }
/*      */ 
/*      */   void processOneAttribute(QName element, XMLAttributes attributes, int index, XSAttributeDecl currDecl, XSAttributeUseImpl currUse, AttributePSVImpl attrPSVI)
/*      */   {
/* 2789 */     String attrValue = attributes.getValue(index);
/* 2790 */     this.fXSIErrorReporter.pushContext();
/*      */ 
/* 2798 */     XSSimpleType attDV = currDecl.fType;
/*      */ 
/* 2800 */     Object actualValue = null;
/*      */     try {
/* 2802 */       actualValue = attDV.validate(attrValue, this.fValidationState, this.fValidatedInfo);
/*      */ 
/* 2804 */       if (this.fNormalizeData)
/* 2805 */         attributes.setValue(index, this.fValidatedInfo.normalizedValue);
/* 2806 */       if ((attributes instanceof XMLAttributesImpl)) {
/* 2807 */         XMLAttributesImpl attrs = (XMLAttributesImpl)attributes;
/* 2808 */         boolean schemaId = this.fValidatedInfo.memberType != null ? this.fValidatedInfo.memberType.isIDType() : attDV.isIDType();
/*      */ 
/* 2812 */         attrs.setSchemaId(index, schemaId);
/*      */       }
/*      */ 
/* 2816 */       if ((attDV.getVariety() == 1) && (attDV.getPrimitiveKind() == 20))
/*      */       {
/* 2818 */         QName qName = (QName)actualValue;
/* 2819 */         SchemaGrammar grammar = this.fGrammarBucket.getGrammar(qName.uri);
/*      */ 
/* 2827 */         if (grammar != null)
/* 2828 */           this.fNotation = grammar.getGlobalNotationDecl(qName.localpart);
/*      */       }
/*      */     }
/*      */     catch (InvalidDatatypeValueException idve) {
/* 2832 */       reportSchemaError(idve.getKey(), idve.getArgs());
/* 2833 */       reportSchemaError("cvc-attribute.3", new Object[] { element.rawname, this.fTempQName.rawname, attrValue, attDV.getName() });
/*      */     }
/*      */ 
/* 2840 */     if ((actualValue != null) && (currDecl.getConstraintType() == 2) && (
/* 2841 */       (!isComparable(this.fValidatedInfo, currDecl.fDefault)) || (!actualValue.equals(currDecl.fDefault.actualValue)))) {
/* 2842 */       reportSchemaError("cvc-attribute.4", new Object[] { element.rawname, this.fTempQName.rawname, attrValue, currDecl.fDefault.stringValue() });
/*      */     }
/*      */ 
/* 2853 */     if ((actualValue != null) && (currUse != null) && (currUse.fConstraintType == 2))
/*      */     {
/* 2856 */       if ((!isComparable(this.fValidatedInfo, currUse.fDefault)) || (!actualValue.equals(currUse.fDefault.actualValue))) {
/* 2857 */         reportSchemaError("cvc-complex-type.3.1", new Object[] { element.rawname, this.fTempQName.rawname, attrValue, currUse.fDefault.stringValue() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2866 */     if (this.fIdConstraint) {
/* 2867 */       attrPSVI.fActualValue = actualValue;
/*      */     }
/*      */ 
/* 2870 */     if (this.fAugPSVI)
/*      */     {
/* 2872 */       attrPSVI.fDeclaration = currDecl;
/*      */ 
/* 2874 */       attrPSVI.fTypeDecl = attDV;
/*      */ 
/* 2877 */       attrPSVI.fMemberType = this.fValidatedInfo.memberType;
/*      */ 
/* 2882 */       attrPSVI.fNormalizedValue = this.fValidatedInfo.normalizedValue;
/* 2883 */       attrPSVI.fActualValue = this.fValidatedInfo.actualValue;
/* 2884 */       attrPSVI.fActualValueType = this.fValidatedInfo.actualValueType;
/* 2885 */       attrPSVI.fItemValueTypes = this.fValidatedInfo.itemValueTypes;
/*      */ 
/* 2890 */       attrPSVI.fValidationAttempted = 2;
/*      */ 
/* 2892 */       String[] errors = this.fXSIErrorReporter.mergeContext();
/*      */ 
/* 2894 */       attrPSVI.fErrorCodes = errors;
/*      */ 
/* 2896 */       attrPSVI.fValidity = (errors == null ? 2 : 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   void addDefaultAttributes(QName element, XMLAttributes attributes, XSAttributeGroupDecl attrGrp)
/*      */   {
/* 2914 */     XSObjectList attrUses = attrGrp.getAttributeUses();
/* 2915 */     int useCount = attrUses.getLength();
/*      */ 
/* 2923 */     for (int i = 0; i < useCount; i++)
/*      */     {
/* 2925 */       XSAttributeUseImpl currUse = (XSAttributeUseImpl)attrUses.item(i);
/* 2926 */       XSAttributeDecl currDecl = currUse.fAttrDecl;
/*      */ 
/* 2928 */       short constType = currUse.fConstraintType;
/* 2929 */       ValidatedInfo defaultValue = currUse.fDefault;
/* 2930 */       if (constType == 0) {
/* 2931 */         constType = currDecl.getConstraintType();
/* 2932 */         defaultValue = currDecl.fDefault;
/*      */       }
/*      */ 
/* 2935 */       boolean isSpecified = attributes.getValue(currDecl.fTargetNamespace, currDecl.fName) != null;
/*      */ 
/* 2941 */       if ((currUse.fUse == 1) && 
/* 2942 */         (!isSpecified)) {
/* 2943 */         reportSchemaError("cvc-complex-type.4", new Object[] { element.rawname, currDecl.fName });
/*      */       }
/*      */ 
/* 2948 */       if ((!isSpecified) && (constType != 0)) {
/* 2949 */         QName attName = new QName(null, currDecl.fName, currDecl.fName, currDecl.fTargetNamespace);
/*      */ 
/* 2951 */         String normalized = defaultValue != null ? defaultValue.stringValue() : "";
/* 2952 */         int attrIndex = attributes.addAttribute(attName, "CDATA", normalized);
/* 2953 */         if ((attributes instanceof XMLAttributesImpl)) {
/* 2954 */           XMLAttributesImpl attrs = (XMLAttributesImpl)attributes;
/* 2955 */           boolean schemaId = (defaultValue != null) && (defaultValue.memberType != null) ? defaultValue.memberType.isIDType() : currDecl.fType.isIDType();
/*      */ 
/* 2960 */           attrs.setSchemaId(attrIndex, schemaId);
/*      */         }
/*      */ 
/* 2963 */         if (this.fAugPSVI)
/*      */         {
/* 2966 */           Augmentations augs = attributes.getAugmentations(attrIndex);
/* 2967 */           AttributePSVImpl attrPSVI = new AttributePSVImpl();
/* 2968 */           augs.putItem("ATTRIBUTE_PSVI", attrPSVI);
/*      */ 
/* 2970 */           attrPSVI.fDeclaration = currDecl;
/* 2971 */           attrPSVI.fTypeDecl = currDecl.fType;
/* 2972 */           attrPSVI.fMemberType = defaultValue.memberType;
/* 2973 */           attrPSVI.fNormalizedValue = normalized;
/* 2974 */           attrPSVI.fActualValue = defaultValue.actualValue;
/* 2975 */           attrPSVI.fActualValueType = defaultValue.actualValueType;
/* 2976 */           attrPSVI.fItemValueTypes = defaultValue.itemValueTypes;
/* 2977 */           attrPSVI.fValidationContext = this.fValidationRoot;
/* 2978 */           attrPSVI.fValidity = 2;
/* 2979 */           attrPSVI.fValidationAttempted = 2;
/* 2980 */           attrPSVI.fSpecified = true;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void processElementContent(QName element)
/*      */   {
/* 2994 */     if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.fDefault != null) && (!this.fSawText) && (!this.fSubElement) && (!this.fNil))
/*      */     {
/* 3000 */       String strv = this.fCurrentElemDecl.fDefault.stringValue();
/* 3001 */       int bufLen = strv.length();
/* 3002 */       if ((this.fNormalizedStr.ch == null) || (this.fNormalizedStr.ch.length < bufLen)) {
/* 3003 */         this.fNormalizedStr.ch = new char[bufLen];
/*      */       }
/* 3005 */       strv.getChars(0, bufLen, this.fNormalizedStr.ch, 0);
/* 3006 */       this.fNormalizedStr.offset = 0;
/* 3007 */       this.fNormalizedStr.length = bufLen;
/* 3008 */       this.fDefaultValue = this.fNormalizedStr;
/*      */     }
/*      */ 
/* 3012 */     this.fValidatedInfo.normalizedValue = null;
/*      */ 
/* 3016 */     if ((this.fNil) && (
/* 3017 */       (this.fSubElement) || (this.fSawText))) {
/* 3018 */       reportSchemaError("cvc-elt.3.2.1", new Object[] { element.rawname, SchemaSymbols.URI_XSI + "," + SchemaSymbols.XSI_NIL });
/*      */     }
/*      */ 
/* 3026 */     this.fValidatedInfo.reset();
/*      */ 
/* 3030 */     if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.getConstraintType() != 0) && (!this.fSubElement) && (!this.fSawText) && (!this.fNil))
/*      */     {
/* 3036 */       if (this.fCurrentType != this.fCurrentElemDecl.fType)
/*      */       {
/* 3038 */         if (XSConstraints.ElementDefaultValidImmediate(this.fCurrentType, this.fCurrentElemDecl.fDefault.stringValue(), this.fState4XsiType, null) == null)
/*      */         {
/* 3045 */           reportSchemaError("cvc-elt.5.1.1", new Object[] { element.rawname, this.fCurrentType.getName(), this.fCurrentElemDecl.fDefault.stringValue() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3055 */       elementLocallyValidType(element, this.fCurrentElemDecl.fDefault.stringValue());
/*      */     }
/*      */     else
/*      */     {
/* 3062 */       Object actualValue = elementLocallyValidType(element, this.fBuffer);
/*      */ 
/* 3064 */       if ((this.fCurrentElemDecl != null) && (this.fCurrentElemDecl.getConstraintType() == 2) && (!this.fNil))
/*      */       {
/* 3067 */         String content = this.fBuffer.toString();
/*      */ 
/* 3069 */         if (this.fSubElement) {
/* 3070 */           reportSchemaError("cvc-elt.5.2.2.1", new Object[] { element.rawname });
/*      */         }
/* 3072 */         if (this.fCurrentType.getTypeCategory() == 15) {
/* 3073 */           XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/*      */ 
/* 3075 */           if (ctype.fContentType == 3)
/*      */           {
/* 3077 */             if (!this.fCurrentElemDecl.fDefault.normalizedValue.equals(content)) {
/* 3078 */               reportSchemaError("cvc-elt.5.2.2.2.1", new Object[] { element.rawname, content, this.fCurrentElemDecl.fDefault.normalizedValue });
/*      */             }
/*      */ 
/*      */           }
/* 3086 */           else if ((ctype.fContentType == 1) && 
/* 3087 */             (actualValue != null) && (
/* 3087 */             (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault)) || (!actualValue.equals(this.fCurrentElemDecl.fDefault.actualValue))))
/*      */           {
/* 3089 */             reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { element.rawname, content, this.fCurrentElemDecl.fDefault.stringValue() });
/*      */           }
/*      */ 
/*      */         }
/* 3097 */         else if ((this.fCurrentType.getTypeCategory() == 16) && 
/* 3098 */           (actualValue != null) && (
/* 3098 */           (!isComparable(this.fValidatedInfo, this.fCurrentElemDecl.fDefault)) || (!actualValue.equals(this.fCurrentElemDecl.fDefault.actualValue))))
/*      */         {
/* 3102 */           reportSchemaError("cvc-elt.5.2.2.2.2", new Object[] { element.rawname, content, this.fCurrentElemDecl.fDefault.stringValue() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3113 */     if ((this.fDefaultValue == null) && (this.fNormalizeData) && (this.fDocumentHandler != null) && (this.fUnionType))
/*      */     {
/* 3116 */       String content = this.fValidatedInfo.normalizedValue;
/* 3117 */       if (content == null) {
/* 3118 */         content = this.fBuffer.toString();
/*      */       }
/* 3120 */       int bufLen = content.length();
/* 3121 */       if ((this.fNormalizedStr.ch == null) || (this.fNormalizedStr.ch.length < bufLen)) {
/* 3122 */         this.fNormalizedStr.ch = new char[bufLen];
/*      */       }
/* 3124 */       content.getChars(0, bufLen, this.fNormalizedStr.ch, 0);
/* 3125 */       this.fNormalizedStr.offset = 0;
/* 3126 */       this.fNormalizedStr.length = bufLen;
/* 3127 */       this.fDocumentHandler.characters(this.fNormalizedStr, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   Object elementLocallyValidType(QName element, Object textContent) {
/* 3132 */     if (this.fCurrentType == null) {
/* 3133 */       return null;
/*      */     }
/* 3135 */     Object retValue = null;
/*      */ 
/* 3139 */     if (this.fCurrentType.getTypeCategory() == 16)
/*      */     {
/* 3141 */       if (this.fSubElement) {
/* 3142 */         reportSchemaError("cvc-type.3.1.2", new Object[] { element.rawname });
/*      */       }
/* 3144 */       if (!this.fNil) {
/* 3145 */         XSSimpleType dv = (XSSimpleType)this.fCurrentType;
/*      */         try {
/* 3147 */           if ((!this.fNormalizeData) || (this.fUnionType)) {
/* 3148 */             this.fValidationState.setNormalizationRequired(true);
/*      */           }
/* 3150 */           retValue = dv.validate(textContent, this.fValidationState, this.fValidatedInfo);
/*      */         } catch (InvalidDatatypeValueException e) {
/* 3152 */           reportSchemaError(e.getKey(), e.getArgs());
/* 3153 */           reportSchemaError("cvc-type.3.1.3", new Object[] { element.rawname, textContent });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 3160 */       retValue = elementLocallyValidComplexType(element, textContent);
/*      */     }
/*      */ 
/* 3163 */     return retValue;
/*      */   }
/*      */ 
/*      */   Object elementLocallyValidComplexType(QName element, Object textContent) {
/* 3167 */     Object actualValue = null;
/* 3168 */     XSComplexTypeDecl ctype = (XSComplexTypeDecl)this.fCurrentType;
/*      */ 
/* 3174 */     if (!this.fNil)
/*      */     {
/* 3176 */       if ((ctype.fContentType == 0) && ((this.fSubElement) || (this.fSawText)))
/*      */       {
/* 3178 */         reportSchemaError("cvc-complex-type.2.1", new Object[] { element.rawname });
/*      */       }
/* 3181 */       else if (ctype.fContentType == 1) {
/* 3182 */         if (this.fSubElement)
/* 3183 */           reportSchemaError("cvc-complex-type.2.2", new Object[] { element.rawname });
/* 3184 */         XSSimpleType dv = ctype.fXSSimpleType;
/*      */         try {
/* 3186 */           if ((!this.fNormalizeData) || (this.fUnionType)) {
/* 3187 */             this.fValidationState.setNormalizationRequired(true);
/*      */           }
/* 3189 */           actualValue = dv.validate(textContent, this.fValidationState, this.fValidatedInfo);
/*      */         } catch (InvalidDatatypeValueException e) {
/* 3191 */           reportSchemaError(e.getKey(), e.getArgs());
/* 3192 */           reportSchemaError("cvc-complex-type.2.2", new Object[] { element.rawname });
/*      */         }
/*      */ 
/*      */       }
/* 3198 */       else if ((ctype.fContentType == 2) && 
/* 3199 */         (this.fSawCharacters)) {
/* 3200 */         reportSchemaError("cvc-complex-type.2.3", new Object[] { element.rawname });
/*      */       }
/*      */ 
/* 3204 */       if ((ctype.fContentType == 2) || (ctype.fContentType == 3))
/*      */       {
/* 3211 */         if ((this.fCurrCMState[0] >= 0) && (!this.fCurrentCM.endContentModel(this.fCurrCMState))) {
/* 3212 */           String expected = expectedStr(this.fCurrentCM.whatCanGoHere(this.fCurrCMState));
/* 3213 */           reportSchemaError("cvc-complex-type.2.4.b", new Object[] { element.rawname, expected });
/*      */         }
/*      */         else
/*      */         {
/* 3221 */           ArrayList errors = this.fCurrentCM.checkMinMaxBounds();
/* 3222 */           if (errors != null) {
/* 3223 */             for (int i = 0; i < errors.size(); i += 2) {
/* 3224 */               reportSchemaError((String)errors.get(i), new Object[] { element.rawname, errors.get(i + 1) });
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3232 */     return actualValue;
/*      */   }
/*      */ 
/*      */   void reportSchemaError(String key, Object[] arguments) {
/* 3236 */     if (this.fDoValidation)
/* 3237 */       this.fXSIErrorReporter.reportError("http://www.w3.org/TR/xml-schema-1", key, arguments, (short)1);
/*      */   }
/*      */ 
/*      */   private boolean isComparable(ValidatedInfo info1, ValidatedInfo info2)
/*      */   {
/* 3246 */     short primitiveType1 = convertToPrimitiveKind(info1.actualValueType);
/* 3247 */     short primitiveType2 = convertToPrimitiveKind(info2.actualValueType);
/* 3248 */     if (primitiveType1 != primitiveType2) {
/* 3249 */       return ((primitiveType1 == 1) && (primitiveType2 == 2)) || ((primitiveType1 == 2) && (primitiveType2 == 1));
/*      */     }
/*      */ 
/* 3252 */     if ((primitiveType1 == 44) || (primitiveType1 == 43)) {
/* 3253 */       ShortList typeList1 = info1.itemValueTypes;
/* 3254 */       ShortList typeList2 = info2.itemValueTypes;
/* 3255 */       int typeList1Length = typeList1 != null ? typeList1.getLength() : 0;
/* 3256 */       int typeList2Length = typeList2 != null ? typeList2.getLength() : 0;
/* 3257 */       if (typeList1Length != typeList2Length) {
/* 3258 */         return false;
/*      */       }
/* 3260 */       for (int i = 0; i < typeList1Length; i++) {
/* 3261 */         short primitiveItem1 = convertToPrimitiveKind(typeList1.item(i));
/* 3262 */         short primitiveItem2 = convertToPrimitiveKind(typeList2.item(i));
/* 3263 */         if ((primitiveItem1 != primitiveItem2) && 
/* 3264 */           ((primitiveItem1 != 1) || (primitiveItem2 != 2)) && (
/* 3264 */           (primitiveItem1 != 2) || (primitiveItem2 != 1)))
/*      */         {
/* 3268 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 3272 */     return true;
/*      */   }
/*      */ 
/*      */   private short convertToPrimitiveKind(short valueType)
/*      */   {
/* 3277 */     if (valueType <= 20) {
/* 3278 */       return valueType;
/*      */     }
/*      */ 
/* 3281 */     if (valueType <= 29) {
/* 3282 */       return 2;
/*      */     }
/*      */ 
/* 3285 */     if (valueType <= 42) {
/* 3286 */       return 4;
/*      */     }
/*      */ 
/* 3289 */     return valueType;
/*      */   }
/*      */ 
/*      */   private String expectedStr(Vector expected) {
/* 3293 */     StringBuffer ret = new StringBuffer("{");
/* 3294 */     int size = expected.size();
/* 3295 */     for (int i = 0; i < size; i++) {
/* 3296 */       if (i > 0)
/* 3297 */         ret.append(", ");
/* 3298 */       ret.append(expected.elementAt(i).toString());
/*      */     }
/* 3300 */     ret.append('}');
/* 3301 */     return ret.toString();
/*      */   }
/*      */ 
/*      */   protected class KeyRefValueStore extends XMLSchemaValidator.ValueStoreBase
/*      */   {
/*      */     protected XMLSchemaValidator.ValueStoreBase fKeyValueStore;
/*      */ 
/*      */     public KeyRefValueStore(KeyRef keyRef, XMLSchemaValidator.KeyValueStore keyValueStore)
/*      */     {
/* 3925 */       super(keyRef);
/* 3926 */       this.fKeyValueStore = keyValueStore;
/*      */     }
/*      */ 
/*      */     public void endDocumentFragment()
/*      */     {
/* 3938 */       super.endDocumentFragment();
/*      */ 
/* 3942 */       this.fKeyValueStore = ((XMLSchemaValidator.ValueStoreBase)XMLSchemaValidator.this.fValueStoreCache.fGlobalIDConstraintMap.get(((KeyRef)this.fIdentityConstraint).getKey()));
/*      */ 
/* 3946 */       if (this.fKeyValueStore == null)
/*      */       {
/* 3948 */         String code = "KeyRefOutOfScope";
/* 3949 */         String value = this.fIdentityConstraint.toString();
/* 3950 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { value });
/* 3951 */         return;
/*      */       }
/* 3953 */       int errorIndex = this.fKeyValueStore.contains(this);
/* 3954 */       if (errorIndex != -1) {
/* 3955 */         String code = "KeyNotFound";
/* 3956 */         String values = toString(this.fValues, errorIndex, this.fFieldCount);
/* 3957 */         String element = this.fIdentityConstraint.getElementName();
/* 3958 */         String name = this.fIdentityConstraint.getName();
/* 3959 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { name, values, element });
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endDocument()
/*      */     {
/* 3966 */       super.endDocument();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class KeyValueStore extends XMLSchemaValidator.ValueStoreBase
/*      */   {
/*      */     public KeyValueStore(UniqueOrKey key)
/*      */     {
/* 3883 */       super(key);
/*      */     }
/*      */ 
/*      */     protected void checkDuplicateValues()
/*      */     {
/* 3894 */       if (contains()) {
/* 3895 */         String code = "DuplicateKey";
/* 3896 */         String value = toString(this.fLocalValues);
/* 3897 */         String eName = this.fIdentityConstraint.getElementName();
/* 3898 */         String cName = this.fIdentityConstraint.getIdentityConstraintName();
/* 3899 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { value, eName, cName });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class LocalIDKey
/*      */   {
/*      */     public IdentityConstraint fId;
/*      */     public int fDepth;
/*      */ 
/*      */     public LocalIDKey()
/*      */     {
/*      */     }
/*      */ 
/*      */     public LocalIDKey(IdentityConstraint id, int depth)
/*      */     {
/* 4218 */       this.fId = id;
/* 4219 */       this.fDepth = depth;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 4224 */       return this.fId.hashCode() + this.fDepth;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object localIDKey) {
/* 4228 */       if ((localIDKey instanceof LocalIDKey)) {
/* 4229 */         LocalIDKey lIDKey = (LocalIDKey)localIDKey;
/* 4230 */         return (lIDKey.fId == this.fId) && (lIDKey.fDepth == this.fDepth);
/*      */       }
/* 4232 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static final class ShortVector
/*      */   {
/*      */     private int fLength;
/*      */     private short[] fData;
/*      */ 
/*      */     public ShortVector()
/*      */     {
/*      */     }
/*      */ 
/*      */     public ShortVector(int initialCapacity)
/*      */     {
/* 4258 */       this.fData = new short[initialCapacity];
/*      */     }
/*      */ 
/*      */     public int length()
/*      */     {
/* 4267 */       return this.fLength;
/*      */     }
/*      */ 
/*      */     public void add(short value)
/*      */     {
/* 4272 */       ensureCapacity(this.fLength + 1);
/* 4273 */       this.fData[(this.fLength++)] = value;
/*      */     }
/*      */ 
/*      */     public short valueAt(int position)
/*      */     {
/* 4278 */       return this.fData[position];
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 4283 */       this.fLength = 0;
/*      */     }
/*      */ 
/*      */     public boolean contains(short value)
/*      */     {
/* 4288 */       for (int i = 0; i < this.fLength; i++) {
/* 4289 */         if (this.fData[i] == value) {
/* 4290 */           return true;
/*      */         }
/*      */       }
/* 4293 */       return false;
/*      */     }
/*      */ 
/*      */     private void ensureCapacity(int size)
/*      */     {
/* 4302 */       if (this.fData == null) {
/* 4303 */         this.fData = new short[8];
/*      */       }
/* 4305 */       else if (this.fData.length <= size) {
/* 4306 */         short[] newdata = new short[this.fData.length * 2];
/* 4307 */         System.arraycopy(this.fData, 0, newdata, 0, this.fData.length);
/* 4308 */         this.fData = newdata;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class UniqueValueStore extends XMLSchemaValidator.ValueStoreBase
/*      */   {
/*      */     public UniqueValueStore(UniqueOrKey unique)
/*      */     {
/* 3845 */       super(unique);
/*      */     }
/*      */ 
/*      */     protected void checkDuplicateValues()
/*      */     {
/* 3857 */       if (contains()) {
/* 3858 */         String code = "DuplicateUnique";
/* 3859 */         String value = toString(this.fLocalValues);
/* 3860 */         String eName = this.fIdentityConstraint.getElementName();
/* 3861 */         String cName = this.fIdentityConstraint.getIdentityConstraintName();
/* 3862 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { value, eName, cName });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract class ValueStoreBase
/*      */     implements ValueStore
/*      */   {
/*      */     protected IdentityConstraint fIdentityConstraint;
/* 3410 */     protected int fFieldCount = 0;
/* 3411 */     protected Field[] fFields = null;
/*      */ 
/* 3413 */     protected Object[] fLocalValues = null;
/* 3414 */     protected short[] fLocalValueTypes = null;
/* 3415 */     protected ShortList[] fLocalItemValueTypes = null;
/*      */     protected int fValuesCount;
/* 3421 */     public final Vector fValues = new Vector();
/* 3422 */     public XMLSchemaValidator.ShortVector fValueTypes = null;
/* 3423 */     public Vector fItemValueTypes = null;
/*      */ 
/* 3425 */     private boolean fUseValueTypeVector = false;
/* 3426 */     private int fValueTypesLength = 0;
/* 3427 */     private short fValueType = 0;
/*      */ 
/* 3429 */     private boolean fUseItemValueTypeVector = false;
/* 3430 */     private int fItemValueTypesLength = 0;
/* 3431 */     private ShortList fItemValueType = null;
/*      */ 
/* 3434 */     final StringBuffer fTempBuffer = new StringBuffer();
/*      */ 
/*      */     protected ValueStoreBase(IdentityConstraint identityConstraint)
/*      */     {
/* 3442 */       this.fIdentityConstraint = identityConstraint;
/* 3443 */       this.fFieldCount = this.fIdentityConstraint.getFieldCount();
/* 3444 */       this.fFields = new Field[this.fFieldCount];
/* 3445 */       this.fLocalValues = new Object[this.fFieldCount];
/* 3446 */       this.fLocalValueTypes = new short[this.fFieldCount];
/* 3447 */       this.fLocalItemValueTypes = new ShortList[this.fFieldCount];
/* 3448 */       for (int i = 0; i < this.fFieldCount; i++)
/* 3449 */         this.fFields[i] = this.fIdentityConstraint.getFieldAt(i);
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 3460 */       this.fValuesCount = 0;
/* 3461 */       this.fUseValueTypeVector = false;
/* 3462 */       this.fValueTypesLength = 0;
/* 3463 */       this.fValueType = 0;
/* 3464 */       this.fUseItemValueTypeVector = false;
/* 3465 */       this.fItemValueTypesLength = 0;
/* 3466 */       this.fItemValueType = null;
/* 3467 */       this.fValues.setSize(0);
/* 3468 */       if (this.fValueTypes != null) {
/* 3469 */         this.fValueTypes.clear();
/*      */       }
/* 3471 */       if (this.fItemValueTypes != null)
/* 3472 */         this.fItemValueTypes.setSize(0);
/*      */     }
/*      */ 
/*      */     public void append(ValueStoreBase newVal)
/*      */     {
/* 3478 */       for (int i = 0; i < newVal.fValues.size(); i++)
/* 3479 */         this.fValues.addElement(newVal.fValues.elementAt(i));
/*      */     }
/*      */ 
/*      */     public void startValueScope()
/*      */     {
/* 3485 */       this.fValuesCount = 0;
/* 3486 */       for (int i = 0; i < this.fFieldCount; i++) {
/* 3487 */         this.fLocalValues[i] = null;
/* 3488 */         this.fLocalValueTypes[i] = 0;
/* 3489 */         this.fLocalItemValueTypes[i] = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endValueScope()
/*      */     {
/* 3496 */       if (this.fValuesCount == 0) {
/* 3497 */         if (this.fIdentityConstraint.getCategory() == 1) {
/* 3498 */           String code = "AbsentKeyValue";
/* 3499 */           String eName = this.fIdentityConstraint.getElementName();
/* 3500 */           String cName = this.fIdentityConstraint.getIdentityConstraintName();
/* 3501 */           XMLSchemaValidator.this.reportSchemaError(code, new Object[] { eName, cName });
/*      */         }
/* 3503 */         return;
/*      */       }
/*      */ 
/* 3512 */       if (this.fValuesCount != this.fFieldCount) {
/* 3513 */         if (this.fIdentityConstraint.getCategory() == 1) {
/* 3514 */           String code = "KeyNotEnoughValues";
/* 3515 */           UniqueOrKey key = (UniqueOrKey)this.fIdentityConstraint;
/* 3516 */           String eName = this.fIdentityConstraint.getElementName();
/* 3517 */           String cName = key.getIdentityConstraintName();
/* 3518 */           XMLSchemaValidator.this.reportSchemaError(code, new Object[] { eName, cName });
/*      */         }
/* 3520 */         return;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endDocumentFragment()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void endDocument()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void reportError(String key, Object[] args)
/*      */     {
/* 3550 */       XMLSchemaValidator.this.reportSchemaError(key, args);
/*      */     }
/*      */ 
/*      */     public void addValue(Field field, Object actualValue, short valueType, ShortList itemValueType)
/*      */     {
/* 3563 */       for (int i = this.fFieldCount - 1; (i > -1) && 
/* 3564 */         (this.fFields[i] != field); i--);
/* 3569 */       if (i == -1) {
/* 3570 */         String code = "UnknownField";
/* 3571 */         String eName = this.fIdentityConstraint.getElementName();
/* 3572 */         String cName = this.fIdentityConstraint.getIdentityConstraintName();
/* 3573 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { field.toString(), eName, cName });
/* 3574 */         return;
/*      */       }
/* 3576 */       if (Boolean.TRUE != XMLSchemaValidator.this.mayMatch(field)) {
/* 3577 */         String code = "FieldMultipleMatch";
/* 3578 */         String cName = this.fIdentityConstraint.getIdentityConstraintName();
/* 3579 */         XMLSchemaValidator.this.reportSchemaError(code, new Object[] { field.toString(), cName });
/*      */       } else {
/* 3581 */         this.fValuesCount += 1;
/*      */       }
/* 3583 */       this.fLocalValues[i] = actualValue;
/* 3584 */       this.fLocalValueTypes[i] = valueType;
/* 3585 */       this.fLocalItemValueTypes[i] = itemValueType;
/* 3586 */       if (this.fValuesCount == this.fFieldCount) {
/* 3587 */         checkDuplicateValues();
/*      */ 
/* 3589 */         for (i = 0; i < this.fFieldCount; i++) {
/* 3590 */           this.fValues.addElement(this.fLocalValues[i]);
/* 3591 */           addValueType(this.fLocalValueTypes[i]);
/* 3592 */           addItemValueType(this.fLocalItemValueTypes[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean contains()
/*      */     {
/* 3603 */       int next = 0;
/* 3604 */       int size = this.fValues.size();
/* 3605 */       label165: for (int i = 0; i < size; i = next) {
/* 3606 */         next = i + this.fFieldCount;
/* 3607 */         for (int j = 0; j < this.fFieldCount; j++) {
/* 3608 */           Object value1 = this.fLocalValues[j];
/* 3609 */           Object value2 = this.fValues.elementAt(i);
/* 3610 */           short valueType1 = this.fLocalValueTypes[j];
/* 3611 */           short valueType2 = getValueTypeAt(i);
/* 3612 */           if ((value1 == null) || (value2 == null) || (valueType1 != valueType2) || (!value1.equals(value2))) {
/*      */             break label165;
/*      */           }
/* 3615 */           if ((valueType1 == 44) || (valueType1 == 43)) {
/* 3616 */             ShortList list1 = this.fLocalItemValueTypes[j];
/* 3617 */             ShortList list2 = getItemValueTypeAt(i);
/* 3618 */             if ((list1 == null) || (list2 == null) || (!list1.equals(list2)))
/*      */               break label165;
/*      */           }
/* 3621 */           i++;
/*      */         }
/*      */ 
/* 3624 */         return true;
/*      */       }
/*      */ 
/* 3627 */       return false;
/*      */     }
/*      */ 
/*      */     public int contains(ValueStoreBase vsb)
/*      */     {
/* 3637 */       Vector values = vsb.fValues;
/* 3638 */       int size1 = values.size();
/* 3639 */       if (this.fFieldCount <= 1) {
/* 3640 */         for (int i = 0; i < size1; i++) {
/* 3641 */           short val = vsb.getValueTypeAt(i);
/* 3642 */           if ((!valueTypeContains(val)) || (!this.fValues.contains(values.elementAt(i)))) {
/* 3643 */             return i;
/*      */           }
/* 3645 */           if ((val == 44) || (val == 43)) {
/* 3646 */             ShortList list1 = vsb.getItemValueTypeAt(i);
/* 3647 */             if (!itemValueTypeContains(list1)) {
/* 3648 */               return i;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 3655 */         int size2 = this.fValues.size();
/*      */ 
/* 3657 */         label293: label308: for (int i = 0; i < size1; i += this.fFieldCount)
/*      */         {
/* 3659 */           for (int j = 0; j < size2; j += this.fFieldCount) {
/* 3660 */             for (int k = 0; k < this.fFieldCount; k++) {
/* 3661 */               Object value1 = values.elementAt(i + k);
/* 3662 */               Object value2 = this.fValues.elementAt(j + k);
/* 3663 */               short valueType1 = vsb.getValueTypeAt(i + k);
/* 3664 */               short valueType2 = getValueTypeAt(j + k);
/* 3665 */               if ((value1 != value2) && ((valueType1 != valueType2) || (value1 == null) || (!value1.equals(value2)))) {
/*      */                 break label293;
/*      */               }
/* 3668 */               if ((valueType1 == 44) || (valueType1 == 43)) {
/* 3669 */                 ShortList list1 = vsb.getItemValueTypeAt(i + k);
/* 3670 */                 ShortList list2 = getItemValueTypeAt(j + k);
/* 3671 */                 if ((list1 == null) || (list2 == null) || (!list1.equals(list2))) {
/*      */                   break label293;
/*      */                 }
/*      */               }
/*      */             }
/* 3676 */             break label308;
/*      */           }
/* 3678 */           return i;
/*      */         }
/*      */       }
/* 3681 */       return -1;
/*      */     }
/*      */ 
/*      */     protected void checkDuplicateValues()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected String toString(Object[] values)
/*      */     {
/* 3697 */       int size = values.length;
/* 3698 */       if (size == 0) {
/* 3699 */         return "";
/*      */       }
/*      */ 
/* 3702 */       this.fTempBuffer.setLength(0);
/*      */ 
/* 3705 */       for (int i = 0; i < size; i++) {
/* 3706 */         if (i > 0) {
/* 3707 */           this.fTempBuffer.append(',');
/*      */         }
/* 3709 */         this.fTempBuffer.append(values[i]);
/*      */       }
/* 3711 */       return this.fTempBuffer.toString();
/*      */     }
/*      */ 
/*      */     protected String toString(Vector values, int start, int length)
/*      */     {
/* 3719 */       if (length == 0) {
/* 3720 */         return "";
/*      */       }
/*      */ 
/* 3724 */       if (length == 1) {
/* 3725 */         return String.valueOf(values.elementAt(start));
/*      */       }
/*      */ 
/* 3729 */       StringBuffer str = new StringBuffer();
/* 3730 */       for (int i = 0; i < length; i++) {
/* 3731 */         if (i > 0) {
/* 3732 */           str.append(',');
/*      */         }
/* 3734 */         str.append(values.elementAt(start + i));
/*      */       }
/* 3736 */       return str.toString();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 3746 */       String s = super.toString();
/* 3747 */       int index1 = s.lastIndexOf('$');
/* 3748 */       if (index1 != -1) {
/* 3749 */         s = s.substring(index1 + 1);
/*      */       }
/* 3751 */       int index2 = s.lastIndexOf('.');
/* 3752 */       if (index2 != -1) {
/* 3753 */         s = s.substring(index2 + 1);
/*      */       }
/* 3755 */       return s + '[' + this.fIdentityConstraint + ']';
/*      */     }
/*      */ 
/*      */     private void addValueType(short type)
/*      */     {
/* 3763 */       if (this.fUseValueTypeVector) {
/* 3764 */         this.fValueTypes.add(type);
/*      */       }
/* 3766 */       else if (this.fValueTypesLength++ == 0) {
/* 3767 */         this.fValueType = type;
/*      */       }
/* 3769 */       else if (this.fValueType != type) {
/* 3770 */         this.fUseValueTypeVector = true;
/* 3771 */         if (this.fValueTypes == null) {
/* 3772 */           this.fValueTypes = new XMLSchemaValidator.ShortVector(this.fValueTypesLength * 2);
/*      */         }
/* 3774 */         for (int i = 1; i < this.fValueTypesLength; i++) {
/* 3775 */           this.fValueTypes.add(this.fValueType);
/*      */         }
/* 3777 */         this.fValueTypes.add(type);
/*      */       }
/*      */     }
/*      */ 
/*      */     private short getValueTypeAt(int index) {
/* 3782 */       if (this.fUseValueTypeVector) {
/* 3783 */         return this.fValueTypes.valueAt(index);
/*      */       }
/* 3785 */       return this.fValueType;
/*      */     }
/*      */ 
/*      */     private boolean valueTypeContains(short value) {
/* 3789 */       if (this.fUseValueTypeVector) {
/* 3790 */         return this.fValueTypes.contains(value);
/*      */       }
/* 3792 */       return this.fValueType == value;
/*      */     }
/*      */ 
/*      */     private void addItemValueType(ShortList itemValueType) {
/* 3796 */       if (this.fUseItemValueTypeVector) {
/* 3797 */         this.fItemValueTypes.add(itemValueType);
/*      */       }
/* 3799 */       else if (this.fItemValueTypesLength++ == 0) {
/* 3800 */         this.fItemValueType = itemValueType;
/*      */       }
/* 3802 */       else if ((this.fItemValueType != itemValueType) && ((this.fItemValueType == null) || (!this.fItemValueType.equals(itemValueType))))
/*      */       {
/* 3804 */         this.fUseItemValueTypeVector = true;
/* 3805 */         if (this.fItemValueTypes == null) {
/* 3806 */           this.fItemValueTypes = new Vector(this.fItemValueTypesLength * 2);
/*      */         }
/* 3808 */         for (int i = 1; i < this.fItemValueTypesLength; i++) {
/* 3809 */           this.fItemValueTypes.add(this.fItemValueType);
/*      */         }
/* 3811 */         this.fItemValueTypes.add(itemValueType);
/*      */       }
/*      */     }
/*      */ 
/*      */     private ShortList getItemValueTypeAt(int index) {
/* 3816 */       if (this.fUseItemValueTypeVector) {
/* 3817 */         return (ShortList)this.fItemValueTypes.elementAt(index);
/*      */       }
/* 3819 */       return this.fItemValueType;
/*      */     }
/*      */ 
/*      */     private boolean itemValueTypeContains(ShortList value) {
/* 3823 */       if (this.fUseItemValueTypeVector) {
/* 3824 */         return this.fItemValueTypes.contains(value);
/*      */       }
/* 3826 */       return (this.fItemValueType == value) || ((this.fItemValueType != null) && (this.fItemValueType.equals(value)));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ValueStoreCache
/*      */   {
/* 3985 */     final XMLSchemaValidator.LocalIDKey fLocalId = new XMLSchemaValidator.LocalIDKey(XMLSchemaValidator.this);
/*      */ 
/* 3989 */     protected final Vector fValueStores = new Vector();
/*      */ 
/* 4000 */     protected final Hashtable fIdentityConstraint2ValueStoreMap = new Hashtable();
/*      */ 
/* 4021 */     protected final Stack fGlobalMapStack = new Stack();
/* 4022 */     protected final Hashtable fGlobalIDConstraintMap = new Hashtable();
/*      */ 
/*      */     public ValueStoreCache()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void startDocument()
/*      */     {
/* 4038 */       this.fValueStores.removeAllElements();
/* 4039 */       this.fIdentityConstraint2ValueStoreMap.clear();
/* 4040 */       this.fGlobalIDConstraintMap.clear();
/* 4041 */       this.fGlobalMapStack.removeAllElements();
/*      */     }
/*      */ 
/*      */     public void startElement()
/*      */     {
/* 4048 */       if (this.fGlobalIDConstraintMap.size() > 0)
/* 4049 */         this.fGlobalMapStack.push(this.fGlobalIDConstraintMap.clone());
/*      */       else
/* 4051 */         this.fGlobalMapStack.push(null);
/* 4052 */       this.fGlobalIDConstraintMap.clear();
/*      */     }
/*      */ 
/*      */     public void endElement()
/*      */     {
/* 4059 */       if (this.fGlobalMapStack.isEmpty()) {
/* 4060 */         return;
/*      */       }
/* 4062 */       Hashtable oldMap = (Hashtable)this.fGlobalMapStack.pop();
/*      */ 
/* 4064 */       if (oldMap == null) {
/* 4065 */         return;
/*      */       }
/*      */ 
/* 4068 */       Iterator entries = oldMap.entrySet().iterator();
/* 4069 */       while (entries.hasNext()) {
/* 4070 */         Map.Entry entry = (Map.Entry)entries.next();
/* 4071 */         IdentityConstraint id = (IdentityConstraint)entry.getKey();
/* 4072 */         XMLSchemaValidator.ValueStoreBase oldVal = (XMLSchemaValidator.ValueStoreBase)entry.getValue();
/* 4073 */         if (oldVal != null) {
/* 4074 */           XMLSchemaValidator.ValueStoreBase currVal = (XMLSchemaValidator.ValueStoreBase)this.fGlobalIDConstraintMap.get(id);
/* 4075 */           if (currVal == null) {
/* 4076 */             this.fGlobalIDConstraintMap.put(id, oldVal);
/*      */           }
/* 4078 */           else if (currVal != oldVal)
/* 4079 */             currVal.append(oldVal);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void initValueStoresFor(XSElementDecl eDecl, FieldActivator activator)
/*      */     {
/* 4091 */       IdentityConstraint[] icArray = eDecl.fIDConstraints;
/* 4092 */       int icCount = eDecl.fIDCPos;
/* 4093 */       for (int i = 0; i < icCount; i++)
/*      */       {
/*      */         XMLSchemaValidator.LocalIDKey toHash;
/* 4094 */         switch (icArray[i].getCategory())
/*      */         {
/*      */         case 3:
/* 4097 */           UniqueOrKey unique = (UniqueOrKey)icArray[i];
/* 4098 */           toHash = new XMLSchemaValidator.LocalIDKey(XMLSchemaValidator.this, unique, XMLSchemaValidator.this.fElementDepth);
/* 4099 */           XMLSchemaValidator.UniqueValueStore uniqueValueStore = (XMLSchemaValidator.UniqueValueStore)this.fIdentityConstraint2ValueStoreMap.get(toHash);
/*      */ 
/* 4101 */           if (uniqueValueStore == null) {
/* 4102 */             uniqueValueStore = new XMLSchemaValidator.UniqueValueStore(XMLSchemaValidator.this, unique);
/* 4103 */             this.fIdentityConstraint2ValueStoreMap.put(toHash, uniqueValueStore);
/*      */           } else {
/* 4105 */             uniqueValueStore.clear();
/*      */           }
/* 4107 */           this.fValueStores.addElement(uniqueValueStore);
/* 4108 */           XMLSchemaValidator.this.activateSelectorFor(icArray[i]);
/* 4109 */           break;
/*      */         case 1:
/* 4112 */           UniqueOrKey key = (UniqueOrKey)icArray[i];
/* 4113 */           toHash = new XMLSchemaValidator.LocalIDKey(XMLSchemaValidator.this, key, XMLSchemaValidator.this.fElementDepth);
/* 4114 */           XMLSchemaValidator.KeyValueStore keyValueStore = (XMLSchemaValidator.KeyValueStore)this.fIdentityConstraint2ValueStoreMap.get(toHash);
/*      */ 
/* 4116 */           if (keyValueStore == null) {
/* 4117 */             keyValueStore = new XMLSchemaValidator.KeyValueStore(XMLSchemaValidator.this, key);
/* 4118 */             this.fIdentityConstraint2ValueStoreMap.put(toHash, keyValueStore);
/*      */           } else {
/* 4120 */             keyValueStore.clear();
/*      */           }
/* 4122 */           this.fValueStores.addElement(keyValueStore);
/* 4123 */           XMLSchemaValidator.this.activateSelectorFor(icArray[i]);
/* 4124 */           break;
/*      */         case 2:
/* 4127 */           KeyRef keyRef = (KeyRef)icArray[i];
/* 4128 */           toHash = new XMLSchemaValidator.LocalIDKey(XMLSchemaValidator.this, keyRef, XMLSchemaValidator.this.fElementDepth);
/* 4129 */           XMLSchemaValidator.KeyRefValueStore keyRefValueStore = (XMLSchemaValidator.KeyRefValueStore)this.fIdentityConstraint2ValueStoreMap.get(toHash);
/*      */ 
/* 4131 */           if (keyRefValueStore == null) {
/* 4132 */             keyRefValueStore = new XMLSchemaValidator.KeyRefValueStore(XMLSchemaValidator.this, keyRef, null);
/* 4133 */             this.fIdentityConstraint2ValueStoreMap.put(toHash, keyRefValueStore);
/*      */           } else {
/* 4135 */             keyRefValueStore.clear();
/*      */           }
/* 4137 */           this.fValueStores.addElement(keyRefValueStore);
/* 4138 */           XMLSchemaValidator.this.activateSelectorFor(icArray[i]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public XMLSchemaValidator.ValueStoreBase getValueStoreFor(IdentityConstraint id, int initialDepth)
/*      */     {
/* 4146 */       this.fLocalId.fDepth = initialDepth;
/* 4147 */       this.fLocalId.fId = id;
/* 4148 */       return (XMLSchemaValidator.ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
/*      */     }
/*      */ 
/*      */     public XMLSchemaValidator.ValueStoreBase getGlobalValueStoreFor(IdentityConstraint id)
/*      */     {
/* 4153 */       return (XMLSchemaValidator.ValueStoreBase)this.fGlobalIDConstraintMap.get(id);
/*      */     }
/*      */ 
/*      */     public void transplant(IdentityConstraint id, int initialDepth)
/*      */     {
/* 4161 */       this.fLocalId.fDepth = initialDepth;
/* 4162 */       this.fLocalId.fId = id;
/* 4163 */       XMLSchemaValidator.ValueStoreBase newVals = (XMLSchemaValidator.ValueStoreBase)this.fIdentityConstraint2ValueStoreMap.get(this.fLocalId);
/*      */ 
/* 4165 */       if (id.getCategory() == 2)
/* 4166 */         return;
/* 4167 */       XMLSchemaValidator.ValueStoreBase currVals = (XMLSchemaValidator.ValueStoreBase)this.fGlobalIDConstraintMap.get(id);
/* 4168 */       if (currVals != null) {
/* 4169 */         currVals.append(newVals);
/* 4170 */         this.fGlobalIDConstraintMap.put(id, currVals);
/*      */       } else {
/* 4172 */         this.fGlobalIDConstraintMap.put(id, newVals);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void endDocument()
/*      */     {
/* 4179 */       int count = this.fValueStores.size();
/* 4180 */       for (int i = 0; i < count; i++) {
/* 4181 */         XMLSchemaValidator.ValueStoreBase valueStore = (XMLSchemaValidator.ValueStoreBase)this.fValueStores.elementAt(i);
/* 4182 */         valueStore.endDocument();
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 4193 */       String s = super.toString();
/* 4194 */       int index1 = s.lastIndexOf('$');
/* 4195 */       if (index1 != -1) {
/* 4196 */         return s.substring(index1 + 1);
/*      */       }
/* 4198 */       int index2 = s.lastIndexOf('.');
/* 4199 */       if (index2 != -1) {
/* 4200 */         return s.substring(index2 + 1);
/*      */       }
/* 4202 */       return s;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class XPathMatcherStack
/*      */   {
/* 3320 */     protected XPathMatcher[] fMatchers = new XPathMatcher[4];
/*      */     protected int fMatchersCount;
/* 3326 */     protected IntStack fContextStack = new IntStack();
/*      */ 
/*      */     public void clear()
/*      */     {
/* 3341 */       for (int i = 0; i < this.fMatchersCount; i++) {
/* 3342 */         this.fMatchers[i] = null;
/*      */       }
/* 3344 */       this.fMatchersCount = 0;
/* 3345 */       this.fContextStack.clear();
/*      */     }
/*      */ 
/*      */     public int size()
/*      */     {
/* 3350 */       return this.fContextStack.size();
/*      */     }
/*      */ 
/*      */     public int getMatcherCount()
/*      */     {
/* 3355 */       return this.fMatchersCount;
/*      */     }
/*      */ 
/*      */     public void addMatcher(XPathMatcher matcher)
/*      */     {
/* 3360 */       ensureMatcherCapacity();
/* 3361 */       this.fMatchers[(this.fMatchersCount++)] = matcher;
/*      */     }
/*      */ 
/*      */     public XPathMatcher getMatcherAt(int index)
/*      */     {
/* 3366 */       return this.fMatchers[index];
/*      */     }
/*      */ 
/*      */     public void pushContext()
/*      */     {
/* 3371 */       this.fContextStack.push(this.fMatchersCount);
/*      */     }
/*      */ 
/*      */     public void popContext()
/*      */     {
/* 3376 */       this.fMatchersCount = this.fContextStack.pop();
/*      */     }
/*      */ 
/*      */     private void ensureMatcherCapacity()
/*      */     {
/* 3385 */       if (this.fMatchersCount == this.fMatchers.length) {
/* 3386 */         XPathMatcher[] array = new XPathMatcher[this.fMatchers.length * 2];
/* 3387 */         System.arraycopy(this.fMatchers, 0, array, 0, this.fMatchers.length);
/* 3388 */         this.fMatchers = array;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final class XSIErrorReporter
/*      */   {
/*      */     XMLErrorReporter fErrorReporter;
/*  382 */     Vector fErrors = new Vector();
/*  383 */     int[] fContext = new int[8];
/*      */     int fContextCount;
/*      */ 
/*      */     protected XSIErrorReporter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void reset(XMLErrorReporter errorReporter)
/*      */     {
/*  388 */       this.fErrorReporter = errorReporter;
/*  389 */       this.fErrors.removeAllElements();
/*  390 */       this.fContextCount = 0;
/*      */     }
/*      */ 
/*      */     public void pushContext()
/*      */     {
/*  396 */       if (!XMLSchemaValidator.this.fAugPSVI) {
/*  397 */         return;
/*      */       }
/*      */ 
/*  400 */       if (this.fContextCount == this.fContext.length) {
/*  401 */         int newSize = this.fContextCount + 8;
/*  402 */         int[] newArray = new int[newSize];
/*  403 */         System.arraycopy(this.fContext, 0, newArray, 0, this.fContextCount);
/*  404 */         this.fContext = newArray;
/*      */       }
/*      */ 
/*  407 */       this.fContext[(this.fContextCount++)] = this.fErrors.size();
/*      */     }
/*      */ 
/*      */     public String[] popContext()
/*      */     {
/*  412 */       if (!XMLSchemaValidator.this.fAugPSVI) {
/*  413 */         return null;
/*      */       }
/*      */ 
/*  416 */       int contextPos = this.fContext[(--this.fContextCount)];
/*      */ 
/*  418 */       int size = this.fErrors.size() - contextPos;
/*      */ 
/*  420 */       if (size == 0) {
/*  421 */         return null;
/*      */       }
/*  423 */       String[] errors = new String[size];
/*  424 */       for (int i = 0; i < size; i++) {
/*  425 */         errors[i] = ((String)this.fErrors.elementAt(contextPos + i));
/*      */       }
/*      */ 
/*  428 */       this.fErrors.setSize(contextPos);
/*  429 */       return errors;
/*      */     }
/*      */ 
/*      */     public String[] mergeContext()
/*      */     {
/*  436 */       if (!XMLSchemaValidator.this.fAugPSVI) {
/*  437 */         return null;
/*      */       }
/*      */ 
/*  440 */       int contextPos = this.fContext[(--this.fContextCount)];
/*      */ 
/*  442 */       int size = this.fErrors.size() - contextPos;
/*      */ 
/*  444 */       if (size == 0) {
/*  445 */         return null;
/*      */       }
/*  447 */       String[] errors = new String[size];
/*  448 */       for (int i = 0; i < size; i++) {
/*  449 */         errors[i] = ((String)this.fErrors.elementAt(contextPos + i));
/*      */       }
/*      */ 
/*  453 */       return errors;
/*      */     }
/*      */ 
/*      */     public void reportError(String domain, String key, Object[] arguments, short severity) throws XNIException
/*      */     {
/*  458 */       this.fErrorReporter.reportError(domain, key, arguments, severity);
/*  459 */       if (XMLSchemaValidator.this.fAugPSVI)
/*  460 */         this.fErrors.addElement(key);
/*      */     }
/*      */ 
/*      */     public void reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity)
/*      */       throws XNIException
/*      */     {
/*  471 */       this.fErrorReporter.reportError(location, domain, key, arguments, severity);
/*  472 */       if (XMLSchemaValidator.this.fAugPSVI)
/*  473 */         this.fErrors.addElement(key);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator
 * JD-Core Version:    0.6.2
 */