/*      */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*      */ import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
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
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import java.io.IOException;
/*      */ 
/*      */ public class XMLDTDValidator
/*      */   implements XMLComponent, XMLDocumentFilter, XMLDTDValidatorFilter, RevalidationHandler
/*      */ {
/*      */   private static final int TOP_LEVEL_SCOPE = -1;
/*      */   protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
/*      */   protected static final String BALANCE_SYNTAX_TREES = "http://apache.org/xml/features/validation/balance-syntax-trees";
/*      */   protected static final String WARN_ON_DUPLICATE_ATTDEF = "http://apache.org/xml/features/validation/warn-on-duplicate-attdef";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*      */   protected static final String DATATYPE_VALIDATOR_FACTORY = "http://apache.org/xml/properties/internal/datatype-validator-factory";
/*      */   protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*  152 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", "http://apache.org/xml/features/validation/dynamic", "http://apache.org/xml/features/validation/balance-syntax-trees" };
/*      */ 
/*  160 */   private static final Boolean[] FEATURE_DEFAULTS = { null, null, Boolean.FALSE, Boolean.FALSE };
/*      */ 
/*  168 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/datatype-validator-factory", "http://apache.org/xml/properties/internal/validation-manager" };
/*      */ 
/*  177 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, null, null };
/*      */   private static final boolean DEBUG_ATTRIBUTES = false;
/*      */   private static final boolean DEBUG_ELEMENT_CHILDREN = false;
/*  198 */   protected ValidationManager fValidationManager = null;
/*      */ 
/*  201 */   protected final ValidationState fValidationState = new ValidationState();
/*      */   protected boolean fNamespaces;
/*      */   protected boolean fValidation;
/*      */   protected boolean fDTDValidation;
/*      */   protected boolean fDynamicValidation;
/*      */   protected boolean fBalanceSyntaxTrees;
/*      */   protected boolean fWarnDuplicateAttdef;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLGrammarPool fGrammarPool;
/*      */   protected DTDGrammarBucket fGrammarBucket;
/*      */   protected XMLLocator fDocLocation;
/*  244 */   protected NamespaceContext fNamespaceContext = null;
/*      */   protected DTDDVFactory fDatatypeValidatorFactory;
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDocumentSource fDocumentSource;
/*      */   protected DTDGrammar fDTDGrammar;
/*  263 */   protected boolean fSeenDoctypeDecl = false;
/*      */   private boolean fPerformValidation;
/*      */   private String fSchemaType;
/*  274 */   private final QName fCurrentElement = new QName();
/*      */ 
/*  277 */   private int fCurrentElementIndex = -1;
/*      */ 
/*  280 */   private int fCurrentContentSpecType = -1;
/*      */ 
/*  283 */   private final QName fRootElement = new QName();
/*      */ 
/*  285 */   private boolean fInCDATASection = false;
/*      */ 
/*  289 */   private int[] fElementIndexStack = new int[8];
/*      */ 
/*  292 */   private int[] fContentSpecTypeStack = new int[8];
/*      */ 
/*  295 */   private QName[] fElementQNamePartsStack = new QName[8];
/*      */ 
/*  312 */   private QName[] fElementChildren = new QName[32];
/*      */ 
/*  315 */   private int fElementChildrenLength = 0;
/*      */ 
/*  322 */   private int[] fElementChildrenOffsetStack = new int[32];
/*      */ 
/*  325 */   private int fElementDepth = -1;
/*      */ 
/*  330 */   private boolean fSeenRootElement = false;
/*      */ 
/*  333 */   private boolean fInElementContent = false;
/*      */ 
/*  338 */   private XMLElementDecl fTempElementDecl = new XMLElementDecl();
/*      */ 
/*  341 */   private final XMLAttributeDecl fTempAttDecl = new XMLAttributeDecl();
/*      */ 
/*  344 */   private final XMLEntityDecl fEntityDecl = new XMLEntityDecl();
/*      */ 
/*  347 */   private final QName fTempQName = new QName();
/*      */ 
/*  350 */   private final StringBuffer fBuffer = new StringBuffer();
/*      */   protected DatatypeValidator fValID;
/*      */   protected DatatypeValidator fValIDRef;
/*      */   protected DatatypeValidator fValIDRefs;
/*      */   protected DatatypeValidator fValENTITY;
/*      */   protected DatatypeValidator fValENTITIES;
/*      */   protected DatatypeValidator fValNMTOKEN;
/*      */   protected DatatypeValidator fValNMTOKENS;
/*      */   protected DatatypeValidator fValNOTATION;
/*      */ 
/*      */   public XMLDTDValidator()
/*      */   {
/*  391 */     for (int i = 0; i < this.fElementQNamePartsStack.length; i++) {
/*  392 */       this.fElementQNamePartsStack[i] = new QName();
/*      */     }
/*  394 */     this.fGrammarBucket = new DTDGrammarBucket();
/*      */   }
/*      */ 
/*      */   DTDGrammarBucket getGrammarBucket()
/*      */   {
/*  399 */     return this.fGrammarBucket;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  424 */     this.fDTDGrammar = null;
/*  425 */     this.fSeenDoctypeDecl = false;
/*  426 */     this.fInCDATASection = false;
/*      */ 
/*  428 */     this.fSeenRootElement = false;
/*  429 */     this.fInElementContent = false;
/*  430 */     this.fCurrentElementIndex = -1;
/*  431 */     this.fCurrentContentSpecType = -1;
/*      */ 
/*  433 */     this.fRootElement.clear();
/*      */ 
/*  435 */     this.fValidationState.resetIDTables();
/*      */ 
/*  437 */     this.fGrammarBucket.clear();
/*  438 */     this.fElementDepth = -1;
/*  439 */     this.fElementChildrenLength = 0;
/*      */ 
/*  441 */     boolean parser_settings = componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings", true);
/*      */ 
/*  443 */     if (!parser_settings)
/*      */     {
/*  445 */       this.fValidationManager.addValidationState(this.fValidationState);
/*  446 */       return;
/*      */     }
/*      */ 
/*  450 */     this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
/*  451 */     this.fValidation = componentManager.getFeature("http://xml.org/sax/features/validation", false);
/*  452 */     this.fDTDValidation = (!componentManager.getFeature("http://apache.org/xml/features/validation/schema", false));
/*      */ 
/*  455 */     this.fDynamicValidation = componentManager.getFeature("http://apache.org/xml/features/validation/dynamic", false);
/*  456 */     this.fBalanceSyntaxTrees = componentManager.getFeature("http://apache.org/xml/features/validation/balance-syntax-trees", false);
/*  457 */     this.fWarnDuplicateAttdef = componentManager.getFeature("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", false);
/*      */ 
/*  459 */     this.fSchemaType = ((String)componentManager.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", null));
/*      */ 
/*  462 */     this.fValidationManager = ((ValidationManager)componentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
/*  463 */     this.fValidationManager.addValidationState(this.fValidationState);
/*  464 */     this.fValidationState.setUsingNamespaces(this.fNamespaces);
/*      */ 
/*  467 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  468 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*  469 */     this.fGrammarPool = ((XMLGrammarPool)componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool", null));
/*      */ 
/*  471 */     this.fDatatypeValidatorFactory = ((DTDDVFactory)componentManager.getProperty("http://apache.org/xml/properties/internal/datatype-validator-factory"));
/*  472 */     init();
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  482 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  510 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  542 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  543 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  544 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  547 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  560 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  561 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  562 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  565 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler documentHandler)
/*      */   {
/*  574 */     this.fDocumentHandler = documentHandler;
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  579 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void setDocumentSource(XMLDocumentSource source)
/*      */   {
/*  589 */     this.fDocumentSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDocumentSource getDocumentSource()
/*      */   {
/*  594 */     return this.fDocumentSource;
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  625 */     if (this.fGrammarPool != null) {
/*  626 */       Grammar[] grammars = this.fGrammarPool.retrieveInitialGrammarSet("http://www.w3.org/TR/REC-xml");
/*  627 */       int length = grammars != null ? grammars.length : 0;
/*  628 */       for (int i = 0; i < length; i++) {
/*  629 */         this.fGrammarBucket.putGrammar((DTDGrammar)grammars[i]);
/*      */       }
/*      */     }
/*  632 */     this.fDocLocation = locator;
/*  633 */     this.fNamespaceContext = namespaceContext;
/*      */ 
/*  635 */     if (this.fDocumentHandler != null)
/*  636 */       this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  658 */     this.fGrammarBucket.setStandalone((standalone != null) && (standalone.equals("yes")));
/*      */ 
/*  661 */     if (this.fDocumentHandler != null)
/*  662 */       this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  684 */     this.fSeenDoctypeDecl = true;
/*  685 */     this.fRootElement.setValues(null, rootElement, rootElement, null);
/*      */ 
/*  687 */     String eid = null;
/*      */     try {
/*  689 */       eid = XMLEntityManager.expandSystemId(systemId, this.fDocLocation.getExpandedSystemId(), false);
/*      */     } catch (IOException e) {
/*      */     }
/*  692 */     XMLDTDDescription grammarDesc = new XMLDTDDescription(publicId, systemId, this.fDocLocation.getExpandedSystemId(), eid, rootElement);
/*  693 */     this.fDTDGrammar = this.fGrammarBucket.getGrammar(grammarDesc);
/*  694 */     if (this.fDTDGrammar == null)
/*      */     {
/*  703 */       if ((this.fGrammarPool != null) && ((systemId != null) || (publicId != null))) {
/*  704 */         this.fDTDGrammar = ((DTDGrammar)this.fGrammarPool.retrieveGrammar(grammarDesc));
/*      */       }
/*      */     }
/*  707 */     if (this.fDTDGrammar == null)
/*      */     {
/*  709 */       if (!this.fBalanceSyntaxTrees) {
/*  710 */         this.fDTDGrammar = new DTDGrammar(this.fSymbolTable, grammarDesc);
/*      */       }
/*      */       else {
/*  713 */         this.fDTDGrammar = new BalancedDTDGrammar(this.fSymbolTable, grammarDesc);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  718 */       this.fValidationManager.setCachedDTD(true);
/*      */     }
/*  720 */     this.fGrammarBucket.setActiveGrammar(this.fDTDGrammar);
/*      */ 
/*  723 */     if (this.fDocumentHandler != null)
/*  724 */       this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  742 */     handleStartElement(element, attributes, augs);
/*      */ 
/*  744 */     if (this.fDocumentHandler != null)
/*  745 */       this.fDocumentHandler.startElement(element, attributes, augs);
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  763 */     boolean removed = handleStartElement(element, attributes, augs);
/*      */ 
/*  765 */     if (this.fDocumentHandler != null) {
/*  766 */       this.fDocumentHandler.emptyElement(element, attributes, augs);
/*      */     }
/*  768 */     if (!removed)
/*  769 */       handleEndElement(element, augs, true);
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  786 */     boolean callNextCharacters = true;
/*      */ 
/*  791 */     boolean allWhiteSpace = true;
/*  792 */     for (int i = text.offset; i < text.offset + text.length; i++) {
/*  793 */       if (!isSpace(text.ch[i])) {
/*  794 */         allWhiteSpace = false;
/*  795 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  800 */     if ((this.fInElementContent) && (allWhiteSpace) && (!this.fInCDATASection) && 
/*  801 */       (this.fDocumentHandler != null)) {
/*  802 */       this.fDocumentHandler.ignorableWhitespace(text, augs);
/*  803 */       callNextCharacters = false;
/*      */     }
/*      */ 
/*  808 */     if (this.fPerformValidation) {
/*  809 */       if (this.fInElementContent) {
/*  810 */         if ((this.fGrammarBucket.getStandalone()) && (this.fDTDGrammar.getElementDeclIsExternal(this.fCurrentElementIndex)))
/*      */         {
/*  812 */           if (allWhiteSpace) {
/*  813 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", null, (short)1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  818 */         if (!allWhiteSpace) {
/*  819 */           charDataInContent();
/*      */         }
/*      */ 
/*  823 */         if ((augs != null) && (augs.getItem("CHAR_REF_PROBABLE_WS") == Boolean.TRUE)) {
/*  824 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { this.fCurrentElement.rawname, this.fDTDGrammar.getContentSpecAsString(this.fElementDepth), "character reference" }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  833 */       if (this.fCurrentContentSpecType == 1) {
/*  834 */         charDataInContent();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  839 */     if ((callNextCharacters) && (this.fDocumentHandler != null))
/*  840 */       this.fDocumentHandler.characters(text, augs);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  863 */     if (this.fDocumentHandler != null)
/*  864 */       this.fDocumentHandler.ignorableWhitespace(text, augs);
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  879 */     handleEndElement(element, augs, false);
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  891 */     if ((this.fPerformValidation) && (this.fInElementContent)) {
/*  892 */       charDataInContent();
/*      */     }
/*  894 */     this.fInCDATASection = true;
/*      */ 
/*  896 */     if (this.fDocumentHandler != null)
/*  897 */       this.fDocumentHandler.startCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  910 */     this.fInCDATASection = false;
/*      */ 
/*  912 */     if (this.fDocumentHandler != null)
/*  913 */       this.fDocumentHandler.endCDATA(augs);
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  927 */     if (this.fDocumentHandler != null)
/*  928 */       this.fDocumentHandler.endDocument(augs);
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  943 */     if ((this.fPerformValidation) && (this.fElementDepth >= 0) && (this.fDTDGrammar != null)) {
/*  944 */       this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
/*  945 */       if (this.fTempElementDecl.type == 1) {
/*  946 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { this.fCurrentElement.rawname, "EMPTY", "comment" }, (short)1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  955 */     if (this.fDocumentHandler != null)
/*  956 */       this.fDocumentHandler.comment(text, augs);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  983 */     if ((this.fPerformValidation) && (this.fElementDepth >= 0) && (this.fDTDGrammar != null)) {
/*  984 */       this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
/*  985 */       if (this.fTempElementDecl.type == 1) {
/*  986 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { this.fCurrentElement.rawname, "EMPTY", "processing instruction" }, (short)1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  995 */     if (this.fDocumentHandler != null)
/*  996 */       this.fDocumentHandler.processingInstruction(target, data, augs);
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1021 */     if ((this.fPerformValidation) && (this.fElementDepth >= 0) && (this.fDTDGrammar != null)) {
/* 1022 */       this.fDTDGrammar.getElementDecl(this.fCurrentElementIndex, this.fTempElementDecl);
/*      */ 
/* 1024 */       if (this.fTempElementDecl.type == 1) {
/* 1025 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { this.fCurrentElement.rawname, "EMPTY", "ENTITY" }, (short)1);
/*      */       }
/*      */ 
/* 1031 */       if (this.fGrammarBucket.getStandalone()) {
/* 1032 */         XMLDTDLoader.checkStandaloneEntityRef(name, this.fDTDGrammar, this.fEntityDecl, this.fErrorReporter);
/*      */       }
/*      */     }
/* 1035 */     if (this.fDocumentHandler != null)
/* 1036 */       this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1054 */     if (this.fDocumentHandler != null)
/* 1055 */       this.fDocumentHandler.endGeneralEntity(name, augs);
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1076 */     if (this.fDocumentHandler != null)
/* 1077 */       this.fDocumentHandler.textDecl(version, encoding, augs);
/*      */   }
/*      */ 
/*      */   public final boolean hasGrammar()
/*      */   {
/* 1084 */     return this.fDTDGrammar != null;
/*      */   }
/*      */ 
/*      */   public final boolean validate()
/*      */   {
/* 1099 */     return (this.fSchemaType != Constants.NS_XMLSCHEMA) && (((!this.fDynamicValidation) && (this.fValidation)) || ((this.fDynamicValidation) && (this.fSeenDoctypeDecl) && ((this.fDTDValidation) || (this.fSeenDoctypeDecl))));
/*      */   }
/*      */ 
/*      */   protected void addDTDDefaultAttrsAndValidate(QName elementName, int elementIndex, XMLAttributes attributes)
/*      */     throws XNIException
/*      */   {
/* 1113 */     if ((elementIndex == -1) || (this.fDTDGrammar == null)) {
/* 1114 */       return;
/*      */     }
/*      */ 
/* 1122 */     int attlistIndex = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
/*      */ 
/* 1124 */     while (attlistIndex != -1)
/*      */     {
/* 1126 */       this.fDTDGrammar.getAttributeDecl(attlistIndex, this.fTempAttDecl);
/*      */ 
/* 1142 */       String attPrefix = this.fTempAttDecl.name.prefix;
/* 1143 */       String attLocalpart = this.fTempAttDecl.name.localpart;
/* 1144 */       String attRawName = this.fTempAttDecl.name.rawname;
/* 1145 */       String attType = getAttributeTypeName(this.fTempAttDecl);
/* 1146 */       int attDefaultType = this.fTempAttDecl.simpleType.defaultType;
/* 1147 */       String attValue = null;
/*      */ 
/* 1149 */       if (this.fTempAttDecl.simpleType.defaultValue != null) {
/* 1150 */         attValue = this.fTempAttDecl.simpleType.defaultValue;
/*      */       }
/*      */ 
/* 1153 */       boolean specified = false;
/* 1154 */       boolean required = attDefaultType == 2;
/* 1155 */       boolean cdata = attType == XMLSymbols.fCDATASymbol;
/*      */ 
/* 1157 */       if ((!cdata) || (required) || (attValue != null)) {
/* 1158 */         int attrCount = attributes.getLength();
/* 1159 */         for (int i = 0; i < attrCount; i++)
/* 1160 */           if (attributes.getQName(i) == attRawName) {
/* 1161 */             specified = true;
/* 1162 */             break;
/*      */           }
/*      */       }
/*      */       int newAttr;
/* 1167 */       if (!specified) {
/* 1168 */         if (required) {
/* 1169 */           if (this.fPerformValidation) {
/* 1170 */             Object[] args = { elementName.localpart, attRawName };
/* 1171 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REQUIRED_ATTRIBUTE_NOT_SPECIFIED", args, (short)1);
/*      */           }
/*      */ 
/*      */         }
/* 1176 */         else if (attValue != null) {
/* 1177 */           if ((this.fPerformValidation) && (this.fGrammarBucket.getStandalone()) && 
/* 1178 */             (this.fDTDGrammar.getAttributeDeclIsExternal(attlistIndex)))
/*      */           {
/* 1180 */             Object[] args = { elementName.localpart, attRawName };
/* 1181 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DEFAULTED_ATTRIBUTE_NOT_SPECIFIED", args, (short)1);
/*      */           }
/*      */ 
/* 1188 */           if (this.fNamespaces) {
/* 1189 */             int index = attRawName.indexOf(':');
/* 1190 */             if (index != -1) {
/* 1191 */               attPrefix = attRawName.substring(0, index);
/* 1192 */               attPrefix = this.fSymbolTable.addSymbol(attPrefix);
/* 1193 */               attLocalpart = attRawName.substring(index + 1);
/* 1194 */               attLocalpart = this.fSymbolTable.addSymbol(attLocalpart);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1199 */           this.fTempQName.setValues(attPrefix, attLocalpart, attRawName, this.fTempAttDecl.name.uri);
/* 1200 */           newAttr = attributes.addAttribute(this.fTempQName, attType, attValue);
/*      */         }
/*      */       }
/*      */ 
/* 1204 */       attlistIndex = this.fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
/*      */     }
/*      */ 
/* 1211 */     int attrCount = attributes.getLength();
/* 1212 */     for (int i = 0; i < attrCount; i++) {
/* 1213 */       String attrRawName = attributes.getQName(i);
/* 1214 */       boolean declared = false;
/* 1215 */       if ((this.fPerformValidation) && 
/* 1216 */         (this.fGrammarBucket.getStandalone()))
/*      */       {
/* 1222 */         String nonNormalizedValue = attributes.getNonNormalizedValue(i);
/* 1223 */         if (nonNormalizedValue != null) {
/* 1224 */           String entityName = getExternalEntityRefInAttrValue(nonNormalizedValue);
/* 1225 */           if (entityName != null) {
/* 1226 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[] { entityName }, (short)1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1234 */       int attDefIndex = -1;
/* 1235 */       int position = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
/*      */ 
/* 1237 */       while (position != -1) {
/* 1238 */         this.fDTDGrammar.getAttributeDecl(position, this.fTempAttDecl);
/* 1239 */         if (this.fTempAttDecl.name.rawname == attrRawName)
/*      */         {
/* 1241 */           attDefIndex = position;
/* 1242 */           declared = true;
/* 1243 */           break;
/*      */         }
/* 1245 */         position = this.fDTDGrammar.getNextAttributeDeclIndex(position);
/*      */       }
/* 1247 */       if (!declared) {
/* 1248 */         if (this.fPerformValidation)
/*      */         {
/* 1251 */           Object[] args = { elementName.rawname, attrRawName };
/*      */ 
/* 1253 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_NOT_DECLARED", args, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1265 */         String type = getAttributeTypeName(this.fTempAttDecl);
/* 1266 */         attributes.setType(i, type);
/* 1267 */         attributes.getAugmentations(i).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
/*      */ 
/* 1269 */         boolean changedByNormalization = false;
/* 1270 */         String oldValue = attributes.getValue(i);
/* 1271 */         String attrValue = oldValue;
/* 1272 */         if ((attributes.isSpecified(i)) && (type != XMLSymbols.fCDATASymbol)) {
/* 1273 */           changedByNormalization = normalizeAttrValue(attributes, i);
/* 1274 */           attrValue = attributes.getValue(i);
/* 1275 */           if ((this.fPerformValidation) && (this.fGrammarBucket.getStandalone()) && (changedByNormalization) && (this.fDTDGrammar.getAttributeDeclIsExternal(position)))
/*      */           {
/* 1280 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTVALUE_CHANGED_DURING_NORMALIZATION_WHEN_STANDALONE", new Object[] { attrRawName, oldValue, attrValue }, (short)1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1286 */         if (this.fPerformValidation)
/*      */         {
/* 1289 */           if (this.fTempAttDecl.simpleType.defaultType == 1)
/*      */           {
/* 1291 */             String defaultValue = this.fTempAttDecl.simpleType.defaultValue;
/*      */ 
/* 1293 */             if (!attrValue.equals(defaultValue)) {
/* 1294 */               Object[] args = { elementName.localpart, attrRawName, attrValue, defaultValue };
/*      */ 
/* 1298 */               this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_FIXED_ATTVALUE_INVALID", args, (short)1);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1304 */           if ((this.fTempAttDecl.simpleType.type == 1) || (this.fTempAttDecl.simpleType.type == 2) || (this.fTempAttDecl.simpleType.type == 3) || (this.fTempAttDecl.simpleType.type == 4) || (this.fTempAttDecl.simpleType.type == 5) || (this.fTempAttDecl.simpleType.type == 6))
/*      */           {
/* 1311 */             validateDTDattribute(elementName, attrValue, this.fTempAttDecl);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String getExternalEntityRefInAttrValue(String nonNormalizedValue) {
/* 1319 */     int valLength = nonNormalizedValue.length();
/* 1320 */     int ampIndex = nonNormalizedValue.indexOf('&');
/* 1321 */     while (ampIndex != -1) {
/* 1322 */       if ((ampIndex + 1 < valLength) && (nonNormalizedValue.charAt(ampIndex + 1) != '#'))
/*      */       {
/* 1324 */         int semicolonIndex = nonNormalizedValue.indexOf(';', ampIndex + 1);
/* 1325 */         String entityName = nonNormalizedValue.substring(ampIndex + 1, semicolonIndex);
/* 1326 */         entityName = this.fSymbolTable.addSymbol(entityName);
/* 1327 */         int entIndex = this.fDTDGrammar.getEntityDeclIndex(entityName);
/* 1328 */         if (entIndex > -1) {
/* 1329 */           this.fDTDGrammar.getEntityDecl(entIndex, this.fEntityDecl);
/* 1330 */           if ((this.fEntityDecl.inExternal) || ((entityName = getExternalEntityRefInAttrValue(this.fEntityDecl.value)) != null))
/*      */           {
/* 1332 */             return entityName;
/*      */           }
/*      */         }
/*      */       }
/* 1336 */       ampIndex = nonNormalizedValue.indexOf('&', ampIndex + 1);
/*      */     }
/* 1338 */     return null;
/*      */   }
/*      */ 
/*      */   protected void validateDTDattribute(QName element, String attValue, XMLAttributeDecl attributeDecl)
/*      */     throws XNIException
/*      */   {
/* 1348 */     switch (attributeDecl.simpleType.type)
/*      */     {
/*      */     case 1:
/* 1351 */       boolean isAlistAttribute = attributeDecl.simpleType.list;
/*      */       try
/*      */       {
/* 1354 */         if (isAlistAttribute) {
/* 1355 */           this.fValENTITIES.validate(attValue, this.fValidationState);
/*      */         }
/*      */         else
/* 1358 */           this.fValENTITY.validate(attValue, this.fValidationState);
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex)
/*      */       {
/* 1362 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex.getKey(), ex.getArgs(), (short)1);
/*      */       }
/*      */ 
/*      */     case 2:
/*      */     case 6:
/* 1373 */       boolean found = false;
/* 1374 */       String[] enumVals = attributeDecl.simpleType.enumeration;
/* 1375 */       if (enumVals == null) {
/* 1376 */         found = false;
/*      */       }
/*      */       else {
/* 1379 */         for (int i = 0; i < enumVals.length; i++) {
/* 1380 */           if ((attValue == enumVals[i]) || (attValue.equals(enumVals[i]))) {
/* 1381 */             found = true;
/* 1382 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1386 */       if (!found) {
/* 1387 */         StringBuffer enumValueString = new StringBuffer();
/* 1388 */         if (enumVals != null) {
/* 1389 */           for (int i = 0; i < enumVals.length; i++)
/* 1390 */             enumValueString.append(enumVals[i] + " ");
/*      */         }
/* 1392 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ATTRIBUTE_VALUE_NOT_IN_LIST", new Object[] { attributeDecl.name.rawname, attValue, enumValueString }, (short)1);
/*      */       }
/*      */ 
/* 1396 */       break;
/*      */     case 3:
/*      */       try
/*      */       {
/* 1402 */         this.fValID.validate(attValue, this.fValidationState);
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex) {
/* 1405 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex.getKey(), ex.getArgs(), (short)1);
/*      */       }
/*      */ 
/*      */     case 4:
/* 1414 */       boolean isAlistAttribute = attributeDecl.simpleType.list;
/*      */       try
/*      */       {
/* 1417 */         if (isAlistAttribute) {
/* 1418 */           this.fValIDRefs.validate(attValue, this.fValidationState);
/*      */         }
/*      */         else
/* 1421 */           this.fValIDRef.validate(attValue, this.fValidationState);
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex)
/*      */       {
/* 1425 */         if (isAlistAttribute) {
/* 1426 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IDREFSInvalid", new Object[] { attValue }, (short)1);
/*      */         }
/*      */         else
/*      */         {
/* 1432 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", ex.getKey(), ex.getArgs(), (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     case 5:
/* 1443 */       boolean isAlistAttribute = attributeDecl.simpleType.list;
/*      */       try
/*      */       {
/* 1446 */         if (isAlistAttribute) {
/* 1447 */           this.fValNMTOKENS.validate(attValue, this.fValidationState);
/*      */         }
/*      */         else
/* 1450 */           this.fValNMTOKEN.validate(attValue, this.fValidationState);
/*      */       }
/*      */       catch (InvalidDatatypeValueException ex)
/*      */       {
/* 1454 */         if (isAlistAttribute) {
/* 1455 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENSInvalid", new Object[] { attValue }, (short)1);
/*      */         }
/*      */         else
/*      */         {
/* 1461 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "NMTOKENInvalid", new Object[] { attValue }, (short)1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean invalidStandaloneAttDef(QName element, QName attribute)
/*      */   {
/* 1478 */     boolean state = true;
/*      */ 
/* 1489 */     return state;
/*      */   }
/*      */ 
/*      */   private boolean normalizeAttrValue(XMLAttributes attributes, int index)
/*      */   {
/* 1507 */     boolean leadingSpace = true;
/* 1508 */     boolean spaceStart = false;
/* 1509 */     boolean readingNonSpace = false;
/* 1510 */     int count = 0;
/* 1511 */     int eaten = 0;
/* 1512 */     String attrValue = attributes.getValue(index);
/* 1513 */     char[] attValue = new char[attrValue.length()];
/*      */ 
/* 1515 */     this.fBuffer.setLength(0);
/* 1516 */     attrValue.getChars(0, attrValue.length(), attValue, 0);
/* 1517 */     for (int i = 0; i < attValue.length; i++)
/*      */     {
/* 1519 */       if (attValue[i] == ' ')
/*      */       {
/* 1522 */         if (readingNonSpace) {
/* 1523 */           spaceStart = true;
/* 1524 */           readingNonSpace = false;
/*      */         }
/*      */ 
/* 1527 */         if ((spaceStart) && (!leadingSpace)) {
/* 1528 */           spaceStart = false;
/* 1529 */           this.fBuffer.append(attValue[i]);
/* 1530 */           count++;
/*      */         }
/* 1533 */         else if ((leadingSpace) || (!spaceStart)) {
/* 1534 */           eaten++;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1559 */         readingNonSpace = true;
/* 1560 */         spaceStart = false;
/* 1561 */         leadingSpace = false;
/* 1562 */         this.fBuffer.append(attValue[i]);
/* 1563 */         count++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1568 */     if ((count > 0) && (this.fBuffer.charAt(count - 1) == ' ')) {
/* 1569 */       this.fBuffer.setLength(count - 1);
/*      */     }
/*      */ 
/* 1588 */     String newValue = this.fBuffer.toString();
/* 1589 */     attributes.setValue(index, newValue);
/* 1590 */     return !attrValue.equals(newValue);
/*      */   }
/*      */ 
/*      */   private final void rootElementSpecified(QName rootElement) throws XNIException
/*      */   {
/* 1595 */     if (this.fPerformValidation) {
/* 1596 */       String root1 = this.fRootElement.rawname;
/* 1597 */       String root2 = rootElement.rawname;
/* 1598 */       if ((root1 == null) || (!root1.equals(root2)))
/* 1599 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "RootElementTypeMustMatchDoctypedecl", new Object[] { root1, root2 }, (short)1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int checkContent(int elementIndex, QName[] children, int childOffset, int childCount)
/*      */     throws XNIException
/*      */   {
/* 1645 */     this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl);
/*      */ 
/* 1648 */     String elementType = this.fCurrentElement.rawname;
/*      */ 
/* 1651 */     int contentType = this.fCurrentContentSpecType;
/*      */ 
/* 1659 */     if (contentType == 1)
/*      */     {
/* 1664 */       if (childCount != 0) {
/* 1665 */         return 0;
/*      */       }
/*      */     }
/* 1668 */     else if (contentType != 0)
/*      */     {
/* 1674 */       if ((contentType == 2) || (contentType == 3))
/*      */       {
/* 1677 */         ContentModelValidator cmElem = null;
/* 1678 */         cmElem = this.fTempElementDecl.contentModelValidator;
/* 1679 */         int result = cmElem.validate(children, childOffset, childCount);
/* 1680 */         return result;
/*      */       }
/* 1682 */       if (contentType != -1)
/*      */       {
/* 1690 */         if (contentType != 4);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1709 */     return -1;
/*      */   }
/*      */ 
/*      */   private int getContentSpecType(int elementIndex)
/*      */   {
/* 1716 */     int contentSpecType = -1;
/* 1717 */     if ((elementIndex > -1) && 
/* 1718 */       (this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl))) {
/* 1719 */       contentSpecType = this.fTempElementDecl.type;
/*      */     }
/*      */ 
/* 1722 */     return contentSpecType;
/*      */   }
/*      */ 
/*      */   private void charDataInContent()
/*      */   {
/* 1731 */     if (this.fElementChildren.length <= this.fElementChildrenLength) {
/* 1732 */       QName[] newarray = new QName[this.fElementChildren.length * 2];
/* 1733 */       System.arraycopy(this.fElementChildren, 0, newarray, 0, this.fElementChildren.length);
/* 1734 */       this.fElementChildren = newarray;
/*      */     }
/* 1736 */     QName qname = this.fElementChildren[this.fElementChildrenLength];
/* 1737 */     if (qname == null) {
/* 1738 */       for (int i = this.fElementChildrenLength; i < this.fElementChildren.length; i++) {
/* 1739 */         this.fElementChildren[i] = new QName();
/*      */       }
/* 1741 */       qname = this.fElementChildren[this.fElementChildrenLength];
/*      */     }
/* 1743 */     qname.clear();
/* 1744 */     this.fElementChildrenLength += 1;
/*      */   }
/*      */ 
/*      */   private String getAttributeTypeName(XMLAttributeDecl attrDecl)
/*      */   {
/* 1751 */     switch (attrDecl.simpleType.type) {
/*      */     case 1:
/* 1753 */       return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
/*      */     case 2:
/* 1756 */       StringBuffer buffer = new StringBuffer();
/* 1757 */       buffer.append('(');
/* 1758 */       for (int i = 0; i < attrDecl.simpleType.enumeration.length; i++) {
/* 1759 */         if (i > 0) {
/* 1760 */           buffer.append('|');
/*      */         }
/* 1762 */         buffer.append(attrDecl.simpleType.enumeration[i]);
/*      */       }
/* 1764 */       buffer.append(')');
/* 1765 */       return this.fSymbolTable.addSymbol(buffer.toString());
/*      */     case 3:
/* 1768 */       return XMLSymbols.fIDSymbol;
/*      */     case 4:
/* 1771 */       return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
/*      */     case 5:
/* 1774 */       return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
/*      */     case 6:
/* 1777 */       return XMLSymbols.fNOTATIONSymbol;
/*      */     }
/*      */ 
/* 1780 */     return XMLSymbols.fCDATASymbol;
/*      */   }
/*      */ 
/*      */   protected void init()
/*      */   {
/* 1788 */     if ((this.fValidation) || (this.fDynamicValidation))
/*      */     {
/*      */       try
/*      */       {
/* 1792 */         this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDSymbol);
/* 1793 */         this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSymbol);
/* 1794 */         this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fIDREFSSymbol);
/* 1795 */         this.fValENTITY = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITYSymbol);
/* 1796 */         this.fValENTITIES = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fENTITIESSymbol);
/* 1797 */         this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSymbol);
/* 1798 */         this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNMTOKENSSymbol);
/* 1799 */         this.fValNOTATION = this.fDatatypeValidatorFactory.getBuiltInDV(XMLSymbols.fNOTATIONSymbol);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/* 1804 */         e.printStackTrace(System.err);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureStackCapacity(int newElementDepth)
/*      */   {
/* 1813 */     if (newElementDepth == this.fElementQNamePartsStack.length)
/*      */     {
/* 1815 */       QName[] newStackOfQueue = new QName[newElementDepth * 2];
/* 1816 */       System.arraycopy(this.fElementQNamePartsStack, 0, newStackOfQueue, 0, newElementDepth);
/* 1817 */       this.fElementQNamePartsStack = newStackOfQueue;
/*      */ 
/* 1819 */       QName qname = this.fElementQNamePartsStack[newElementDepth];
/* 1820 */       if (qname == null) {
/* 1821 */         for (int i = newElementDepth; i < this.fElementQNamePartsStack.length; i++) {
/* 1822 */           this.fElementQNamePartsStack[i] = new QName();
/*      */         }
/*      */       }
/*      */ 
/* 1826 */       int[] newStack = new int[newElementDepth * 2];
/* 1827 */       System.arraycopy(this.fElementIndexStack, 0, newStack, 0, newElementDepth);
/* 1828 */       this.fElementIndexStack = newStack;
/*      */ 
/* 1830 */       newStack = new int[newElementDepth * 2];
/* 1831 */       System.arraycopy(this.fContentSpecTypeStack, 0, newStack, 0, newElementDepth);
/* 1832 */       this.fContentSpecTypeStack = newStack;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean handleStartElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1851 */     if (!this.fSeenRootElement)
/*      */     {
/* 1873 */       this.fPerformValidation = validate();
/* 1874 */       this.fSeenRootElement = true;
/* 1875 */       this.fValidationManager.setEntityState(this.fDTDGrammar);
/* 1876 */       this.fValidationManager.setGrammarFound(this.fSeenDoctypeDecl);
/* 1877 */       rootElementSpecified(element);
/*      */     }
/* 1879 */     if (this.fDTDGrammar == null)
/*      */     {
/* 1881 */       if (!this.fPerformValidation) {
/* 1882 */         this.fCurrentElementIndex = -1;
/* 1883 */         this.fCurrentContentSpecType = -1;
/* 1884 */         this.fInElementContent = false;
/*      */       }
/* 1886 */       if (this.fPerformValidation) {
/* 1887 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_GRAMMAR_NOT_FOUND", new Object[] { element.rawname }, (short)1);
/*      */       }
/*      */ 
/* 1893 */       if (this.fDocumentSource != null) {
/* 1894 */         this.fDocumentSource.setDocumentHandler(this.fDocumentHandler);
/* 1895 */         if (this.fDocumentHandler != null)
/* 1896 */           this.fDocumentHandler.setDocumentSource(this.fDocumentSource);
/* 1897 */         return true;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1902 */       this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(element);
/*      */ 
/* 1904 */       this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
/* 1905 */       if ((this.fCurrentContentSpecType == -1) && (this.fPerformValidation)) {
/* 1906 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_NOT_DECLARED", new Object[] { element.rawname }, (short)1);
/*      */       }
/*      */ 
/* 1917 */       addDTDDefaultAttrsAndValidate(element, this.fCurrentElementIndex, attributes);
/*      */     }
/*      */ 
/* 1921 */     this.fInElementContent = (this.fCurrentContentSpecType == 3);
/*      */ 
/* 1925 */     this.fElementDepth += 1;
/* 1926 */     if (this.fPerformValidation)
/*      */     {
/* 1928 */       if (this.fElementChildrenOffsetStack.length <= this.fElementDepth) {
/* 1929 */         int[] newarray = new int[this.fElementChildrenOffsetStack.length * 2];
/* 1930 */         System.arraycopy(this.fElementChildrenOffsetStack, 0, newarray, 0, this.fElementChildrenOffsetStack.length);
/* 1931 */         this.fElementChildrenOffsetStack = newarray;
/*      */       }
/* 1933 */       this.fElementChildrenOffsetStack[this.fElementDepth] = this.fElementChildrenLength;
/*      */ 
/* 1936 */       if (this.fElementChildren.length <= this.fElementChildrenLength) {
/* 1937 */         QName[] newarray = new QName[this.fElementChildrenLength * 2];
/* 1938 */         System.arraycopy(this.fElementChildren, 0, newarray, 0, this.fElementChildren.length);
/* 1939 */         this.fElementChildren = newarray;
/*      */       }
/* 1941 */       QName qname = this.fElementChildren[this.fElementChildrenLength];
/* 1942 */       if (qname == null) {
/* 1943 */         for (int i = this.fElementChildrenLength; i < this.fElementChildren.length; i++) {
/* 1944 */           this.fElementChildren[i] = new QName();
/*      */         }
/* 1946 */         qname = this.fElementChildren[this.fElementChildrenLength];
/*      */       }
/* 1948 */       qname.setValues(element);
/* 1949 */       this.fElementChildrenLength += 1;
/*      */     }
/*      */ 
/* 1953 */     this.fCurrentElement.setValues(element);
/* 1954 */     ensureStackCapacity(this.fElementDepth);
/* 1955 */     this.fElementQNamePartsStack[this.fElementDepth].setValues(this.fCurrentElement);
/* 1956 */     this.fElementIndexStack[this.fElementDepth] = this.fCurrentElementIndex;
/* 1957 */     this.fContentSpecTypeStack[this.fElementDepth] = this.fCurrentContentSpecType;
/* 1958 */     startNamespaceScope(element, attributes, augs);
/* 1959 */     return false;
/*      */   }
/*      */ 
/*      */   protected void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleEndElement(QName element, Augmentations augs, boolean isEmpty)
/*      */     throws XNIException
/*      */   {
/* 1971 */     this.fElementDepth -= 1;
/*      */ 
/* 1974 */     if (this.fPerformValidation) {
/* 1975 */       int elementIndex = this.fCurrentElementIndex;
/* 1976 */       if ((elementIndex != -1) && (this.fCurrentContentSpecType != -1)) {
/* 1977 */         QName[] children = this.fElementChildren;
/* 1978 */         int childrenOffset = this.fElementChildrenOffsetStack[(this.fElementDepth + 1)] + 1;
/* 1979 */         int childrenLength = this.fElementChildrenLength - childrenOffset;
/* 1980 */         int result = checkContent(elementIndex, children, childrenOffset, childrenLength);
/*      */ 
/* 1983 */         if (result != -1) {
/* 1984 */           this.fDTDGrammar.getElementDecl(elementIndex, this.fTempElementDecl);
/* 1985 */           if (this.fTempElementDecl.type == 1) {
/* 1986 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_CONTENT_INVALID", new Object[] { element.rawname, "EMPTY" }, (short)1);
/*      */           }
/*      */           else
/*      */           {
/* 1992 */             String messageKey = result != childrenLength ? "MSG_CONTENT_INVALID" : "MSG_CONTENT_INCOMPLETE";
/*      */ 
/* 1994 */             this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", messageKey, new Object[] { element.rawname, this.fDTDGrammar.getContentSpecAsString(elementIndex) }, (short)1);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2002 */       this.fElementChildrenLength = (this.fElementChildrenOffsetStack[(this.fElementDepth + 1)] + 1);
/*      */     }
/*      */ 
/* 2005 */     endNamespaceScope(this.fCurrentElement, augs, isEmpty);
/*      */ 
/* 2008 */     if (this.fElementDepth < -1) {
/* 2009 */       throw new RuntimeException("FWK008 Element stack underflow");
/*      */     }
/* 2011 */     if (this.fElementDepth < 0) {
/* 2012 */       this.fCurrentElement.clear();
/* 2013 */       this.fCurrentElementIndex = -1;
/* 2014 */       this.fCurrentContentSpecType = -1;
/* 2015 */       this.fInElementContent = false;
/*      */ 
/* 2023 */       if (this.fPerformValidation) {
/* 2024 */         String value = this.fValidationState.checkIDRefID();
/* 2025 */         if (value != null) {
/* 2026 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_ELEMENT_WITH_ID_REQUIRED", new Object[] { value }, (short)1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2032 */       return;
/*      */     }
/*      */ 
/* 2036 */     this.fCurrentElement.setValues(this.fElementQNamePartsStack[this.fElementDepth]);
/*      */ 
/* 2038 */     this.fCurrentElementIndex = this.fElementIndexStack[this.fElementDepth];
/* 2039 */     this.fCurrentContentSpecType = this.fContentSpecTypeStack[this.fElementDepth];
/* 2040 */     this.fInElementContent = (this.fCurrentContentSpecType == 3);
/*      */   }
/*      */ 
/*      */   protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty)
/*      */   {
/* 2047 */     if ((this.fDocumentHandler != null) && (!isEmpty))
/*      */     {
/* 2054 */       this.fDocumentHandler.endElement(this.fCurrentElement, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isSpace(int c)
/*      */   {
/* 2061 */     return XMLChar.isSpace(c);
/*      */   }
/*      */ 
/*      */   public boolean characterData(String data, Augmentations augs) {
/* 2065 */     characters(new XMLString(data.toCharArray(), 0, data.length()), augs);
/* 2066 */     return true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator
 * JD-Core Version:    0.6.2
 */