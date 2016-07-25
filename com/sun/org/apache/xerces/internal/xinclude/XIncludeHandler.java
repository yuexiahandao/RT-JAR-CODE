/*      */ package com.sun.org.apache.xerces.internal.xinclude;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.Constants;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
/*      */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
/*      */ import com.sun.org.apache.xerces.internal.util.IntStack;
/*      */ import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.URI;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*      */ import com.sun.org.apache.xerces.internal.utils.Objects;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*      */ import com.sun.org.apache.xerces.internal.xpointer.XPointerHandler;
/*      */ import com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor;
/*      */ import java.io.CharConversionException;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Locale;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ public class XIncludeHandler
/*      */   implements XMLComponent, XMLDocumentFilter, XMLDTDFilter
/*      */ {
/*      */   public static final String XINCLUDE_DEFAULT_CONFIGURATION = "com.sun.org.apache.xerces.internal.parsers.XIncludeParserConfiguration";
/*      */   public static final String HTTP_ACCEPT = "Accept";
/*      */   public static final String HTTP_ACCEPT_LANGUAGE = "Accept-Language";
/*      */   public static final String XPOINTER = "xpointer";
/*  131 */   public static final String XINCLUDE_NS_URI = "http://www.w3.org/2001/XInclude".intern();
/*      */ 
/*  133 */   public static final String XINCLUDE_INCLUDE = "include".intern();
/*  134 */   public static final String XINCLUDE_FALLBACK = "fallback".intern();
/*      */ 
/*  136 */   public static final String XINCLUDE_PARSE_XML = "xml".intern();
/*  137 */   public static final String XINCLUDE_PARSE_TEXT = "text".intern();
/*      */ 
/*  139 */   public static final String XINCLUDE_ATTR_HREF = "href".intern();
/*  140 */   public static final String XINCLUDE_ATTR_PARSE = "parse".intern();
/*  141 */   public static final String XINCLUDE_ATTR_ENCODING = "encoding".intern();
/*  142 */   public static final String XINCLUDE_ATTR_ACCEPT = "accept".intern();
/*  143 */   public static final String XINCLUDE_ATTR_ACCEPT_LANGUAGE = "accept-language".intern();
/*      */ 
/*  146 */   public static final String XINCLUDE_INCLUDED = "[included]".intern();
/*      */   public static final String CURRENT_BASE_URI = "currentBaseURI";
/*  152 */   public static final String XINCLUDE_BASE = "base".intern();
/*  153 */   public static final QName XML_BASE_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_BASE, (XMLSymbols.PREFIX_XML + ":" + XINCLUDE_BASE).intern(), NamespaceContext.XML_URI);
/*      */ 
/*  161 */   public static final String XINCLUDE_LANG = "lang".intern();
/*  162 */   public static final QName XML_LANG_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_LANG, (XMLSymbols.PREFIX_XML + ":" + XINCLUDE_LANG).intern(), NamespaceContext.XML_URI);
/*      */ 
/*  169 */   public static final QName NEW_NS_ATTR_QNAME = new QName(XMLSymbols.PREFIX_XMLNS, "", XMLSymbols.PREFIX_XMLNS + ":", NamespaceContext.XMLNS_URI);
/*      */   private static final int STATE_NORMAL_PROCESSING = 1;
/*      */   private static final int STATE_IGNORE = 2;
/*      */   private static final int STATE_EXPECT_FALLBACK = 3;
/*      */   protected static final String VALIDATION = "http://xml.org/sax/features/validation";
/*      */   protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
/*      */   protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
/*      */   protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
/*      */   protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
/*      */   protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*      */   protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   public static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
/*      */   protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*      */   protected static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*  240 */   private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/allow-dtd-events-after-endDTD", "http://apache.org/xml/features/xinclude/fixup-base-uris", "http://apache.org/xml/features/xinclude/fixup-language" };
/*      */ 
/*  244 */   private static final Boolean[] FEATURE_DEFAULTS = { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE };
/*      */ 
/*  247 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/security-manager", "http://apache.org/xml/properties/input-buffer-size" };
/*      */ 
/*  251 */   private static final Object[] PROPERTY_DEFAULTS = { null, null, null, new Integer(8192) };
/*      */   protected XMLDocumentHandler fDocumentHandler;
/*      */   protected XMLDocumentSource fDocumentSource;
/*      */   protected XMLDTDHandler fDTDHandler;
/*      */   protected XMLDTDSource fDTDSource;
/*      */   protected XIncludeHandler fParentXIncludeHandler;
/*  267 */   protected int fBufferSize = 8192;
/*      */   protected String fParentRelativeURI;
/*      */   protected XMLParserConfiguration fChildConfig;
/*      */   protected XMLParserConfiguration fXIncludeChildConfig;
/*      */   protected XMLParserConfiguration fXPointerChildConfig;
/*  284 */   protected XPointerProcessor fXPtrProcessor = null;
/*      */   protected XMLLocator fDocLocation;
/*  287 */   protected XIncludeMessageFormatter fXIncludeMessageFormatter = new XIncludeMessageFormatter();
/*      */   protected XIncludeNamespaceSupport fNamespaceContext;
/*      */   protected SymbolTable fSymbolTable;
/*      */   protected XMLErrorReporter fErrorReporter;
/*      */   protected XMLEntityResolver fEntityResolver;
/*      */   protected XMLSecurityManager fSecurityManager;
/*      */   protected XMLSecurityPropertyManager fSecurityPropertyMgr;
/*      */   protected XIncludeTextReader fXInclude10TextReader;
/*      */   protected XIncludeTextReader fXInclude11TextReader;
/*      */   protected XMLResourceIdentifier fCurrentBaseURI;
/*      */   protected IntStack fBaseURIScope;
/*      */   protected Stack fBaseURI;
/*      */   protected Stack fLiteralSystemID;
/*      */   protected Stack fExpandedSystemID;
/*      */   protected IntStack fLanguageScope;
/*      */   protected Stack fLanguageStack;
/*      */   protected String fCurrentLanguage;
/*      */   protected ParserConfigurationSettings fSettings;
/*      */   private int fDepth;
/*      */   private int fResultDepth;
/*      */   private static final int INITIAL_SIZE = 8;
/*  328 */   private boolean[] fSawInclude = new boolean[8];
/*      */ 
/*  333 */   private boolean[] fSawFallback = new boolean[8];
/*      */ 
/*  336 */   private int[] fState = new int[8];
/*      */   private ArrayList fNotations;
/*      */   private ArrayList fUnparsedEntities;
/*  343 */   private boolean fFixupBaseURIs = true;
/*  344 */   private boolean fFixupLanguage = true;
/*      */   private boolean fSendUEAndNotationEvents;
/*      */   private boolean fIsXML11;
/*      */   private boolean fInDTD;
/*      */   private boolean fSeenRootElement;
/*  360 */   private boolean fNeedCopyFeatures = true;
/*      */ 
/* 2956 */   private static final boolean[] gNeedEscaping = new boolean[''];
/*      */ 
/* 2958 */   private static final char[] gAfterEscaping1 = new char[''];
/*      */ 
/* 2960 */   private static final char[] gAfterEscaping2 = new char[''];
/* 2961 */   private static final char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/*      */   public XIncludeHandler()
/*      */   {
/*  365 */     this.fDepth = 0;
/*      */ 
/*  367 */     this.fSawFallback[this.fDepth] = false;
/*  368 */     this.fSawInclude[this.fDepth] = false;
/*  369 */     this.fState[this.fDepth] = 1;
/*  370 */     this.fNotations = new ArrayList();
/*  371 */     this.fUnparsedEntities = new ArrayList();
/*      */ 
/*  373 */     this.fBaseURIScope = new IntStack();
/*  374 */     this.fBaseURI = new Stack();
/*  375 */     this.fLiteralSystemID = new Stack();
/*  376 */     this.fExpandedSystemID = new Stack();
/*  377 */     this.fCurrentBaseURI = new XMLResourceIdentifierImpl();
/*      */ 
/*  379 */     this.fLanguageScope = new IntStack();
/*  380 */     this.fLanguageStack = new Stack();
/*  381 */     this.fCurrentLanguage = null;
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XNIException
/*      */   {
/*  389 */     this.fNamespaceContext = null;
/*  390 */     this.fDepth = 0;
/*  391 */     this.fResultDepth = (isRootDocument() ? 0 : this.fParentXIncludeHandler.getResultDepth());
/*  392 */     this.fNotations.clear();
/*  393 */     this.fUnparsedEntities.clear();
/*  394 */     this.fParentRelativeURI = null;
/*  395 */     this.fIsXML11 = false;
/*  396 */     this.fInDTD = false;
/*  397 */     this.fSeenRootElement = false;
/*      */ 
/*  399 */     this.fBaseURIScope.clear();
/*  400 */     this.fBaseURI.clear();
/*  401 */     this.fLiteralSystemID.clear();
/*  402 */     this.fExpandedSystemID.clear();
/*  403 */     this.fLanguageScope.clear();
/*  404 */     this.fLanguageStack.clear();
/*      */ 
/*  412 */     for (int i = 0; i < this.fState.length; i++) {
/*  413 */       this.fState[i] = 1;
/*      */     }
/*  415 */     for (int i = 0; i < this.fSawFallback.length; i++) {
/*  416 */       this.fSawFallback[i] = false;
/*      */     }
/*  418 */     for (int i = 0; i < this.fSawInclude.length; i++) {
/*  419 */       this.fSawInclude[i] = false;
/*      */     }
/*      */     try
/*      */     {
/*  423 */       if (!componentManager.getFeature("http://apache.org/xml/features/internal/parser-settings"))
/*      */       {
/*  425 */         return;
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*  431 */     this.fNeedCopyFeatures = true;
/*      */     try
/*      */     {
/*  434 */       this.fSendUEAndNotationEvents = componentManager.getFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD");
/*      */ 
/*  436 */       if (this.fChildConfig != null) {
/*  437 */         this.fChildConfig.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", this.fSendUEAndNotationEvents);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  446 */       this.fFixupBaseURIs = componentManager.getFeature("http://apache.org/xml/features/xinclude/fixup-base-uris");
/*      */ 
/*  448 */       if (this.fChildConfig != null) {
/*  449 */         this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", this.fFixupBaseURIs);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  455 */       this.fFixupBaseURIs = true;
/*      */     }
/*      */     try
/*      */     {
/*  459 */       this.fFixupLanguage = componentManager.getFeature("http://apache.org/xml/features/xinclude/fixup-language");
/*      */ 
/*  461 */       if (this.fChildConfig != null) {
/*  462 */         this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-language", this.fFixupLanguage);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  468 */       this.fFixupLanguage = true;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  473 */       SymbolTable value = (SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
/*      */ 
/*  475 */       if (value != null) {
/*  476 */         this.fSymbolTable = value;
/*  477 */         if (this.fChildConfig != null)
/*  478 */           this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", value);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  483 */       this.fSymbolTable = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  488 */       XMLErrorReporter value = (XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
/*      */ 
/*  490 */       if (value != null) {
/*  491 */         setErrorReporter(value);
/*  492 */         if (this.fChildConfig != null)
/*  493 */           this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", value);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  498 */       this.fErrorReporter = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  503 */       XMLEntityResolver value = (XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
/*      */ 
/*  507 */       if (value != null) {
/*  508 */         this.fEntityResolver = value;
/*  509 */         if (this.fChildConfig != null)
/*  510 */           this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", value);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  515 */       this.fEntityResolver = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  520 */       XMLSecurityManager value = (XMLSecurityManager)componentManager.getProperty("http://apache.org/xml/properties/security-manager");
/*      */ 
/*  524 */       if (value != null) {
/*  525 */         this.fSecurityManager = value;
/*  526 */         if (this.fChildConfig != null)
/*  527 */           this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", value);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*  532 */       this.fSecurityManager = null;
/*      */     }
/*      */ 
/*  535 */     this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"));
/*      */     try
/*      */     {
/*  540 */       Integer value = (Integer)componentManager.getProperty("http://apache.org/xml/properties/input-buffer-size");
/*      */ 
/*  544 */       if ((value != null) && (value.intValue() > 0)) {
/*  545 */         this.fBufferSize = value.intValue();
/*  546 */         if (this.fChildConfig != null)
/*  547 */           this.fChildConfig.setProperty("http://apache.org/xml/properties/input-buffer-size", value);
/*      */       }
/*      */       else
/*      */       {
/*  551 */         this.fBufferSize = ((Integer)getPropertyDefault("http://apache.org/xml/properties/input-buffer-size")).intValue();
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*  555 */       this.fBufferSize = ((Integer)getPropertyDefault("http://apache.org/xml/properties/input-buffer-size")).intValue();
/*      */     }
/*      */ 
/*  559 */     if (this.fXInclude10TextReader != null) {
/*  560 */       this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
/*      */     }
/*      */ 
/*  563 */     if (this.fXInclude11TextReader != null) {
/*  564 */       this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
/*      */     }
/*      */ 
/*  567 */     this.fSettings = new ParserConfigurationSettings();
/*  568 */     copyFeatures(componentManager, this.fSettings);
/*      */     try
/*      */     {
/*  577 */       if (componentManager.getFeature("http://apache.org/xml/features/validation/schema")) {
/*  578 */         this.fSettings.setFeature("http://apache.org/xml/features/validation/schema", false);
/*  579 */         if (componentManager.getFeature("http://xml.org/sax/features/validation"))
/*  580 */           this.fSettings.setFeature("http://apache.org/xml/features/validation/dynamic", true);
/*      */       }
/*      */     }
/*      */     catch (XMLConfigurationException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedFeatures()
/*      */   {
/*  597 */     return (String[])RECOGNIZED_FEATURES.clone();
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */     throws XMLConfigurationException
/*      */   {
/*  618 */     if (featureId.equals("http://xml.org/sax/features/allow-dtd-events-after-endDTD")) {
/*  619 */       this.fSendUEAndNotationEvents = state;
/*      */     }
/*  621 */     if (this.fSettings != null) {
/*  622 */       this.fNeedCopyFeatures = true;
/*  623 */       this.fSettings.setFeature(featureId, state);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] getRecognizedProperties()
/*      */   {
/*  634 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*      */   }
/*      */ 
/*      */   public void setProperty(String propertyId, Object value)
/*      */     throws XMLConfigurationException
/*      */   {
/*  655 */     if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
/*  656 */       this.fSymbolTable = ((SymbolTable)value);
/*  657 */       if (this.fChildConfig != null) {
/*  658 */         this.fChildConfig.setProperty(propertyId, value);
/*      */       }
/*  660 */       return;
/*      */     }
/*  662 */     if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
/*  663 */       setErrorReporter((XMLErrorReporter)value);
/*  664 */       if (this.fChildConfig != null) {
/*  665 */         this.fChildConfig.setProperty(propertyId, value);
/*      */       }
/*  667 */       return;
/*      */     }
/*  669 */     if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
/*  670 */       this.fEntityResolver = ((XMLEntityResolver)value);
/*  671 */       if (this.fChildConfig != null) {
/*  672 */         this.fChildConfig.setProperty(propertyId, value);
/*      */       }
/*  674 */       return;
/*      */     }
/*  676 */     if (propertyId.equals("http://apache.org/xml/properties/security-manager")) {
/*  677 */       this.fSecurityManager = ((XMLSecurityManager)value);
/*  678 */       if (this.fChildConfig != null) {
/*  679 */         this.fChildConfig.setProperty(propertyId, value);
/*      */       }
/*  681 */       return;
/*      */     }
/*  683 */     if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/*  684 */       this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)value);
/*      */ 
/*  686 */       if (this.fChildConfig != null) {
/*  687 */         this.fChildConfig.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", value);
/*      */       }
/*      */ 
/*  690 */       return;
/*      */     }
/*      */ 
/*  693 */     if (propertyId.equals("http://apache.org/xml/properties/input-buffer-size")) {
/*  694 */       Integer bufferSize = (Integer)value;
/*  695 */       if (this.fChildConfig != null) {
/*  696 */         this.fChildConfig.setProperty(propertyId, value);
/*      */       }
/*  698 */       if ((bufferSize != null) && (bufferSize.intValue() > 0)) {
/*  699 */         this.fBufferSize = bufferSize.intValue();
/*      */ 
/*  701 */         if (this.fXInclude10TextReader != null) {
/*  702 */           this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
/*      */         }
/*      */ 
/*  705 */         if (this.fXInclude11TextReader != null) {
/*  706 */           this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
/*      */         }
/*      */       }
/*  709 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Boolean getFeatureDefault(String featureId)
/*      */   {
/*  725 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/*  726 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/*  727 */         return FEATURE_DEFAULTS[i];
/*      */       }
/*      */     }
/*  730 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getPropertyDefault(String propertyId)
/*      */   {
/*  744 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/*  745 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/*  746 */         return PROPERTY_DEFAULTS[i];
/*      */       }
/*      */     }
/*  749 */     return null;
/*      */   }
/*      */ 
/*      */   public void setDocumentHandler(XMLDocumentHandler handler)
/*      */   {
/*  754 */     this.fDocumentHandler = handler;
/*      */   }
/*      */ 
/*      */   public XMLDocumentHandler getDocumentHandler()
/*      */   {
/*  759 */     return this.fDocumentHandler;
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  782 */     this.fErrorReporter.setDocumentLocator(locator);
/*      */ 
/*  784 */     if ((!isRootDocument()) && (this.fParentXIncludeHandler.searchForRecursiveIncludes(locator)))
/*      */     {
/*  786 */       reportFatalError("RecursiveInclude", new Object[] { locator.getExpandedSystemId() });
/*      */     }
/*      */ 
/*  791 */     if (!(namespaceContext instanceof XIncludeNamespaceSupport)) {
/*  792 */       reportFatalError("IncompatibleNamespaceContext");
/*      */     }
/*  794 */     this.fNamespaceContext = ((XIncludeNamespaceSupport)namespaceContext);
/*  795 */     this.fDocLocation = locator;
/*      */ 
/*  798 */     this.fCurrentBaseURI.setBaseSystemId(locator.getBaseSystemId());
/*  799 */     this.fCurrentBaseURI.setExpandedSystemId(locator.getExpandedSystemId());
/*  800 */     this.fCurrentBaseURI.setLiteralSystemId(locator.getLiteralSystemId());
/*  801 */     saveBaseURI();
/*  802 */     if (augs == null) {
/*  803 */       augs = new AugmentationsImpl();
/*      */     }
/*  805 */     augs.putItem("currentBaseURI", this.fCurrentBaseURI);
/*      */ 
/*  808 */     this.fCurrentLanguage = XMLSymbols.EMPTY_STRING;
/*  809 */     saveLanguage(this.fCurrentLanguage);
/*      */ 
/*  811 */     if ((isRootDocument()) && (this.fDocumentHandler != null))
/*  812 */       this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  827 */     this.fIsXML11 = "1.1".equals(version);
/*  828 */     if ((isRootDocument()) && (this.fDocumentHandler != null))
/*  829 */       this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  840 */     if ((isRootDocument()) && (this.fDocumentHandler != null))
/*  841 */       this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  848 */     if (!this.fInDTD) {
/*  849 */       if ((this.fDocumentHandler != null) && (getState() == 1))
/*      */       {
/*  851 */         this.fDepth += 1;
/*  852 */         augs = modifyAugmentations(augs);
/*  853 */         this.fDocumentHandler.comment(text, augs);
/*  854 */         this.fDepth -= 1;
/*      */       }
/*      */     }
/*  857 */     else if (this.fDTDHandler != null)
/*  858 */       this.fDTDHandler.comment(text, augs);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  868 */     if (!this.fInDTD) {
/*  869 */       if ((this.fDocumentHandler != null) && (getState() == 1))
/*      */       {
/*  872 */         this.fDepth += 1;
/*  873 */         augs = modifyAugmentations(augs);
/*  874 */         this.fDocumentHandler.processingInstruction(target, data, augs);
/*  875 */         this.fDepth -= 1;
/*      */       }
/*      */     }
/*  878 */     else if (this.fDTDHandler != null)
/*  879 */       this.fDTDHandler.processingInstruction(target, data, augs);
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  889 */     this.fDepth += 1;
/*  890 */     int lastState = getState(this.fDepth - 1);
/*      */ 
/*  894 */     if ((lastState == 3) && (getState(this.fDepth - 2) == 3)) {
/*  895 */       setState(2);
/*      */     }
/*      */     else {
/*  898 */       setState(lastState);
/*      */     }
/*      */ 
/*  903 */     processXMLBaseAttributes(attributes);
/*  904 */     if (this.fFixupLanguage) {
/*  905 */       processXMLLangAttributes(attributes);
/*      */     }
/*      */ 
/*  908 */     if (isIncludeElement(element)) {
/*  909 */       boolean success = handleIncludeElement(attributes);
/*  910 */       if (success) {
/*  911 */         setState(2);
/*      */       }
/*      */       else {
/*  914 */         setState(3);
/*      */       }
/*      */     }
/*  917 */     else if (isFallbackElement(element)) {
/*  918 */       handleFallbackElement();
/*      */     }
/*  920 */     else if (hasXIncludeNamespace(element)) {
/*  921 */       if (getSawInclude(this.fDepth - 1)) {
/*  922 */         reportFatalError("IncludeChild", new Object[] { element.rawname });
/*      */       }
/*      */ 
/*  926 */       if (getSawFallback(this.fDepth - 1)) {
/*  927 */         reportFatalError("FallbackChild", new Object[] { element.rawname });
/*      */       }
/*      */ 
/*  931 */       if (getState() == 1) {
/*  932 */         if (this.fResultDepth++ == 0) {
/*  933 */           checkMultipleRootElements();
/*      */         }
/*  935 */         if (this.fDocumentHandler != null) {
/*  936 */           augs = modifyAugmentations(augs);
/*  937 */           attributes = processAttributes(attributes);
/*  938 */           this.fDocumentHandler.startElement(element, attributes, augs);
/*      */         }
/*      */       }
/*      */     }
/*  942 */     else if (getState() == 1) {
/*  943 */       if (this.fResultDepth++ == 0) {
/*  944 */         checkMultipleRootElements();
/*      */       }
/*  946 */       if (this.fDocumentHandler != null) {
/*  947 */         augs = modifyAugmentations(augs);
/*  948 */         attributes = processAttributes(attributes);
/*  949 */         this.fDocumentHandler.startElement(element, attributes, augs);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  960 */     this.fDepth += 1;
/*  961 */     int lastState = getState(this.fDepth - 1);
/*      */ 
/*  965 */     if ((lastState == 3) && (getState(this.fDepth - 2) == 3)) {
/*  966 */       setState(2);
/*      */     }
/*      */     else {
/*  969 */       setState(lastState);
/*      */     }
/*      */ 
/*  974 */     processXMLBaseAttributes(attributes);
/*  975 */     if (this.fFixupLanguage) {
/*  976 */       processXMLLangAttributes(attributes);
/*      */     }
/*      */ 
/*  979 */     if (isIncludeElement(element)) {
/*  980 */       boolean success = handleIncludeElement(attributes);
/*  981 */       if (success) {
/*  982 */         setState(2);
/*      */       }
/*      */       else {
/*  985 */         reportFatalError("NoFallback", new Object[] { attributes.getValue(null, "href") });
/*      */       }
/*      */ 
/*      */     }
/*  989 */     else if (isFallbackElement(element)) {
/*  990 */       handleFallbackElement();
/*      */     }
/*  992 */     else if (hasXIncludeNamespace(element)) {
/*  993 */       if (getSawInclude(this.fDepth - 1)) {
/*  994 */         reportFatalError("IncludeChild", new Object[] { element.rawname });
/*      */       }
/*      */ 
/*  998 */       if (getSawFallback(this.fDepth - 1)) {
/*  999 */         reportFatalError("FallbackChild", new Object[] { element.rawname });
/*      */       }
/*      */ 
/* 1003 */       if (getState() == 1) {
/* 1004 */         if (this.fResultDepth == 0) {
/* 1005 */           checkMultipleRootElements();
/*      */         }
/* 1007 */         if (this.fDocumentHandler != null) {
/* 1008 */           augs = modifyAugmentations(augs);
/* 1009 */           attributes = processAttributes(attributes);
/* 1010 */           this.fDocumentHandler.emptyElement(element, attributes, augs);
/*      */         }
/*      */       }
/*      */     }
/* 1014 */     else if (getState() == 1) {
/* 1015 */       if (this.fResultDepth == 0) {
/* 1016 */         checkMultipleRootElements();
/*      */       }
/* 1018 */       if (this.fDocumentHandler != null) {
/* 1019 */         augs = modifyAugmentations(augs);
/* 1020 */         attributes = processAttributes(attributes);
/* 1021 */         this.fDocumentHandler.emptyElement(element, attributes, augs);
/*      */       }
/*      */     }
/*      */ 
/* 1025 */     setSawFallback(this.fDepth + 1, false);
/* 1026 */     setSawInclude(this.fDepth, false);
/*      */ 
/* 1029 */     if ((this.fBaseURIScope.size() > 0) && (this.fDepth == this.fBaseURIScope.peek()))
/*      */     {
/* 1031 */       restoreBaseURI();
/*      */     }
/* 1033 */     this.fDepth -= 1;
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1040 */     if (isIncludeElement(element))
/*      */     {
/* 1043 */       if ((getState() == 3) && (!getSawFallback(this.fDepth + 1)))
/*      */       {
/* 1045 */         reportFatalError("NoFallback", new Object[] { "unknown" });
/*      */       }
/*      */     }
/*      */ 
/* 1049 */     if (isFallbackElement(element))
/*      */     {
/* 1052 */       if (getState() == 1) {
/* 1053 */         setState(2);
/*      */       }
/*      */     }
/* 1056 */     else if (getState() == 1) {
/* 1057 */       this.fResultDepth -= 1;
/* 1058 */       if (this.fDocumentHandler != null) {
/* 1059 */         this.fDocumentHandler.endElement(element, augs);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1064 */     setSawFallback(this.fDepth + 1, false);
/* 1065 */     setSawInclude(this.fDepth, false);
/*      */ 
/* 1068 */     if ((this.fBaseURIScope.size() > 0) && (this.fDepth == this.fBaseURIScope.peek()))
/*      */     {
/* 1070 */       restoreBaseURI();
/*      */     }
/*      */ 
/* 1074 */     if ((this.fLanguageScope.size() > 0) && (this.fDepth == this.fLanguageScope.peek()))
/*      */     {
/* 1076 */       this.fCurrentLanguage = restoreLanguage();
/*      */     }
/*      */ 
/* 1079 */     this.fDepth -= 1;
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier resId, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1089 */     if (getState() == 1)
/* 1090 */       if (this.fResultDepth == 0) {
/* 1091 */         if ((augs != null) && (Boolean.TRUE.equals(augs.getItem("ENTITY_SKIPPED")))) {
/* 1092 */           reportFatalError("UnexpandedEntityReferenceIllegal");
/*      */         }
/*      */       }
/* 1095 */       else if (this.fDocumentHandler != null)
/* 1096 */         this.fDocumentHandler.startGeneralEntity(name, resId, encoding, augs);
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1104 */     if ((this.fDocumentHandler != null) && (getState() == 1))
/*      */     {
/* 1106 */       this.fDocumentHandler.textDecl(version, encoding, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1113 */     if ((this.fDocumentHandler != null) && (getState() == 1) && (this.fResultDepth != 0))
/*      */     {
/* 1116 */       this.fDocumentHandler.endGeneralEntity(name, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1123 */     if (getState() == 1)
/* 1124 */       if (this.fResultDepth == 0) {
/* 1125 */         checkWhitespace(text);
/*      */       }
/* 1127 */       else if (this.fDocumentHandler != null)
/*      */       {
/* 1129 */         this.fDepth += 1;
/* 1130 */         augs = modifyAugmentations(augs);
/* 1131 */         this.fDocumentHandler.characters(text, augs);
/* 1132 */         this.fDepth -= 1;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1140 */     if ((this.fDocumentHandler != null) && (getState() == 1) && (this.fResultDepth != 0))
/*      */     {
/* 1143 */       this.fDocumentHandler.ignorableWhitespace(text, augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs) throws XNIException
/*      */   {
/* 1149 */     if ((this.fDocumentHandler != null) && (getState() == 1) && (this.fResultDepth != 0))
/*      */     {
/* 1152 */       this.fDocumentHandler.startCDATA(augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs) throws XNIException
/*      */   {
/* 1158 */     if ((this.fDocumentHandler != null) && (getState() == 1) && (this.fResultDepth != 0))
/*      */     {
/* 1161 */       this.fDocumentHandler.endCDATA(augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument(Augmentations augs) throws XNIException
/*      */   {
/* 1167 */     if (isRootDocument()) {
/* 1168 */       if (!this.fSeenRootElement) {
/* 1169 */         reportFatalError("RootElementRequired");
/*      */       }
/* 1171 */       if (this.fDocumentHandler != null)
/* 1172 */         this.fDocumentHandler.endDocument(augs);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDocumentSource(XMLDocumentSource source)
/*      */   {
/* 1179 */     this.fDocumentSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDocumentSource getDocumentSource()
/*      */   {
/* 1184 */     return this.fDocumentSource;
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1205 */     if (this.fDTDHandler != null)
/* 1206 */       this.fDTDHandler.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augmentations);
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String contentModel, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1227 */     if (this.fDTDHandler != null)
/* 1228 */       this.fDTDHandler.elementDecl(name, contentModel, augmentations);
/*      */   }
/*      */ 
/*      */   public void endAttlist(Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1237 */     if (this.fDTDHandler != null)
/* 1238 */       this.fDTDHandler.endAttlist(augmentations);
/*      */   }
/*      */ 
/*      */   public void endConditional(Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1248 */     if (this.fDTDHandler != null)
/* 1249 */       this.fDTDHandler.endConditional(augmentations);
/*      */   }
/*      */ 
/*      */   public void endDTD(Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1258 */     if (this.fDTDHandler != null) {
/* 1259 */       this.fDTDHandler.endDTD(augmentations);
/*      */     }
/* 1261 */     this.fInDTD = false;
/*      */   }
/*      */ 
/*      */   public void endExternalSubset(Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1270 */     if (this.fDTDHandler != null)
/* 1271 */       this.fDTDHandler.endExternalSubset(augmentations);
/*      */   }
/*      */ 
/*      */   public void endParameterEntity(String name, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1281 */     if (this.fDTDHandler != null)
/* 1282 */       this.fDTDHandler.endParameterEntity(name, augmentations);
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1295 */     if (this.fDTDHandler != null)
/* 1296 */       this.fDTDHandler.externalEntityDecl(name, identifier, augmentations);
/*      */   }
/*      */ 
/*      */   public XMLDTDSource getDTDSource()
/*      */   {
/* 1305 */     return this.fDTDSource;
/*      */   }
/*      */ 
/*      */   public void ignoredCharacters(XMLString text, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1314 */     if (this.fDTDHandler != null)
/* 1315 */       this.fDTDHandler.ignoredCharacters(text, augmentations);
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1329 */     if (this.fDTDHandler != null)
/* 1330 */       this.fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augmentations);
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1347 */     addNotation(name, identifier, augmentations);
/* 1348 */     if (this.fDTDHandler != null)
/* 1349 */       this.fDTDHandler.notationDecl(name, identifier, augmentations);
/*      */   }
/*      */ 
/*      */   public void setDTDSource(XMLDTDSource source)
/*      */   {
/* 1358 */     this.fDTDSource = source;
/*      */   }
/*      */ 
/*      */   public void startAttlist(String elementName, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1367 */     if (this.fDTDHandler != null)
/* 1368 */       this.fDTDHandler.startAttlist(elementName, augmentations);
/*      */   }
/*      */ 
/*      */   public void startConditional(short type, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1378 */     if (this.fDTDHandler != null)
/* 1379 */       this.fDTDHandler.startConditional(type, augmentations);
/*      */   }
/*      */ 
/*      */   public void startDTD(XMLLocator locator, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1389 */     this.fInDTD = true;
/* 1390 */     if (this.fDTDHandler != null)
/* 1391 */       this.fDTDHandler.startDTD(locator, augmentations);
/*      */   }
/*      */ 
/*      */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1403 */     if (this.fDTDHandler != null)
/* 1404 */       this.fDTDHandler.startExternalSubset(identifier, augmentations);
/*      */   }
/*      */ 
/*      */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1418 */     if (this.fDTDHandler != null)
/* 1419 */       this.fDTDHandler.startParameterEntity(name, identifier, encoding, augmentations);
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations)
/*      */     throws XNIException
/*      */   {
/* 1437 */     addUnparsedEntity(name, identifier, notation, augmentations);
/* 1438 */     if (this.fDTDHandler != null)
/* 1439 */       this.fDTDHandler.unparsedEntityDecl(name, identifier, notation, augmentations);
/*      */   }
/*      */ 
/*      */   public XMLDTDHandler getDTDHandler()
/*      */   {
/* 1452 */     return this.fDTDHandler;
/*      */   }
/*      */ 
/*      */   public void setDTDHandler(XMLDTDHandler handler)
/*      */   {
/* 1460 */     this.fDTDHandler = handler;
/*      */   }
/*      */ 
/*      */   private void setErrorReporter(XMLErrorReporter reporter)
/*      */   {
/* 1466 */     this.fErrorReporter = reporter;
/* 1467 */     if (this.fErrorReporter != null) {
/* 1468 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xinclude", this.fXIncludeMessageFormatter);
/*      */ 
/* 1471 */       if (this.fDocLocation != null)
/* 1472 */         this.fErrorReporter.setDocumentLocator(this.fDocLocation);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void handleFallbackElement()
/*      */   {
/* 1478 */     if (!getSawInclude(this.fDepth - 1)) {
/* 1479 */       if (getState() == 2) {
/* 1480 */         return;
/*      */       }
/* 1482 */       reportFatalError("FallbackParent");
/*      */     }
/*      */ 
/* 1485 */     setSawInclude(this.fDepth, false);
/* 1486 */     this.fNamespaceContext.setContextInvalid();
/*      */ 
/* 1488 */     if (getSawFallback(this.fDepth)) {
/* 1489 */       reportFatalError("MultipleFallbacks");
/*      */     }
/*      */     else {
/* 1492 */       setSawFallback(this.fDepth, true);
/*      */     }
/*      */ 
/* 1498 */     if (getState() == 3)
/* 1499 */       setState(1);
/*      */   }
/*      */ 
/*      */   protected boolean handleIncludeElement(XMLAttributes attributes)
/*      */     throws XNIException
/*      */   {
/* 1505 */     if (getSawInclude(this.fDepth - 1)) {
/* 1506 */       reportFatalError("IncludeChild", new Object[] { XINCLUDE_INCLUDE });
/*      */     }
/* 1508 */     if (getState() == 2) {
/* 1509 */       return true;
/*      */     }
/* 1511 */     setSawInclude(this.fDepth, true);
/* 1512 */     this.fNamespaceContext.setContextInvalid();
/*      */ 
/* 1520 */     String href = attributes.getValue(XINCLUDE_ATTR_HREF);
/* 1521 */     String parse = attributes.getValue(XINCLUDE_ATTR_PARSE);
/* 1522 */     String xpointer = attributes.getValue("xpointer");
/* 1523 */     String accept = attributes.getValue(XINCLUDE_ATTR_ACCEPT);
/* 1524 */     String acceptLanguage = attributes.getValue(XINCLUDE_ATTR_ACCEPT_LANGUAGE);
/*      */ 
/* 1526 */     if (parse == null) {
/* 1527 */       parse = XINCLUDE_PARSE_XML;
/*      */     }
/* 1529 */     if (href == null) {
/* 1530 */       href = XMLSymbols.EMPTY_STRING;
/*      */     }
/* 1532 */     if ((href.length() == 0) && (XINCLUDE_PARSE_XML.equals(parse))) {
/* 1533 */       if (xpointer == null) {
/* 1534 */         reportFatalError("XpointerMissing");
/*      */       }
/*      */       else
/*      */       {
/* 1539 */         Locale locale = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
/* 1540 */         String reason = this.fXIncludeMessageFormatter.formatMessage(locale, "XPointerStreamability", null);
/* 1541 */         reportResourceError("XMLResourceError", new Object[] { href, reason });
/* 1542 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1546 */     URI hrefURI = null;
/*      */     try
/*      */     {
/* 1552 */       hrefURI = new URI(href, true);
/* 1553 */       if (hrefURI.getFragment() != null)
/* 1554 */         reportFatalError("HrefFragmentIdentifierIllegal", new Object[] { href });
/*      */     }
/*      */     catch (URI.MalformedURIException exc)
/*      */     {
/* 1558 */       String newHref = escapeHref(href);
/* 1559 */       if (href != newHref) {
/* 1560 */         href = newHref;
/*      */         try {
/* 1562 */           hrefURI = new URI(href, true);
/* 1563 */           if (hrefURI.getFragment() != null)
/* 1564 */             reportFatalError("HrefFragmentIdentifierIllegal", new Object[] { href });
/*      */         }
/*      */         catch (URI.MalformedURIException exc2)
/*      */         {
/* 1568 */           reportFatalError("HrefSyntacticallyInvalid", new Object[] { href });
/*      */         }
/*      */       }
/*      */       else {
/* 1572 */         reportFatalError("HrefSyntacticallyInvalid", new Object[] { href });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1578 */     if ((accept != null) && (!isValidInHTTPHeader(accept))) {
/* 1579 */       reportFatalError("AcceptMalformed", null);
/* 1580 */       accept = null;
/*      */     }
/* 1582 */     if ((acceptLanguage != null) && (!isValidInHTTPHeader(acceptLanguage))) {
/* 1583 */       reportFatalError("AcceptLanguageMalformed", null);
/* 1584 */       acceptLanguage = null;
/*      */     }
/*      */ 
/* 1587 */     XMLInputSource includedSource = null;
/* 1588 */     if (this.fEntityResolver != null) {
/*      */       try {
/* 1590 */         XMLResourceIdentifier resourceIdentifier = new XMLResourceIdentifierImpl(null, href, this.fCurrentBaseURI.getExpandedSystemId(), XMLEntityManager.expandSystemId(href, this.fCurrentBaseURI.getExpandedSystemId(), false));
/*      */ 
/* 1600 */         includedSource = this.fEntityResolver.resolveEntity(resourceIdentifier);
/*      */ 
/* 1603 */         if ((includedSource != null) && (!(includedSource instanceof HTTPInputSource)) && ((accept != null) || (acceptLanguage != null)) && (includedSource.getCharacterStream() == null) && (includedSource.getByteStream() == null))
/*      */         {
/* 1609 */           includedSource = createInputSource(includedSource.getPublicId(), includedSource.getSystemId(), includedSource.getBaseSystemId(), accept, acceptLanguage);
/*      */         }
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1614 */         reportResourceError("XMLResourceError", new Object[] { href, e.getMessage() });
/*      */ 
/* 1617 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1621 */     if (includedSource == null)
/*      */     {
/* 1623 */       if ((accept != null) || (acceptLanguage != null)) {
/* 1624 */         includedSource = createInputSource(null, href, this.fCurrentBaseURI.getExpandedSystemId(), accept, acceptLanguage);
/*      */       }
/*      */       else {
/* 1627 */         includedSource = new XMLInputSource(null, href, this.fCurrentBaseURI.getExpandedSystemId());
/*      */       }
/*      */     }
/*      */ 
/* 1631 */     if (parse.equals(XINCLUDE_PARSE_XML))
/*      */     {
/* 1633 */       if (((xpointer != null) && (this.fXPointerChildConfig == null)) || ((xpointer == null) && (this.fXIncludeChildConfig == null)))
/*      */       {
/* 1636 */         String parserName = "com.sun.org.apache.xerces.internal.parsers.XIncludeParserConfiguration";
/* 1637 */         if (xpointer != null) {
/* 1638 */           parserName = "com.sun.org.apache.xerces.internal.parsers.XPointerParserConfiguration";
/*      */         }
/* 1640 */         this.fChildConfig = ((XMLParserConfiguration)ObjectFactory.newInstance(parserName, true));
/*      */ 
/* 1646 */         if (this.fSymbolTable != null) this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/* 1647 */         if (this.fErrorReporter != null) this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/* 1648 */         if (this.fEntityResolver != null) this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
/* 1649 */         this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/* 1650 */         this.fChildConfig.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/* 1651 */         this.fChildConfig.setProperty("http://apache.org/xml/properties/input-buffer-size", new Integer(this.fBufferSize));
/*      */ 
/* 1654 */         this.fNeedCopyFeatures = true;
/*      */ 
/* 1657 */         this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
/*      */ 
/* 1662 */         this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", this.fFixupBaseURIs);
/*      */ 
/* 1666 */         this.fChildConfig.setFeature("http://apache.org/xml/features/xinclude/fixup-language", this.fFixupLanguage);
/*      */ 
/* 1672 */         if (xpointer != null)
/*      */         {
/* 1674 */           XPointerHandler newHandler = (XPointerHandler)this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xpointer-handler");
/*      */ 
/* 1679 */           this.fXPtrProcessor = newHandler;
/*      */ 
/* 1682 */           ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
/*      */ 
/* 1687 */           ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/features/xinclude/fixup-base-uris", Boolean.valueOf(this.fFixupBaseURIs));
/*      */ 
/* 1690 */           ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/features/xinclude/fixup-language", Boolean.valueOf(this.fFixupLanguage));
/*      */ 
/* 1693 */           if (this.fErrorReporter != null) {
/* 1694 */             ((XPointerHandler)this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*      */           }
/*      */ 
/* 1697 */           newHandler.setParent(this);
/* 1698 */           newHandler.setDocumentHandler(getDocumentHandler());
/* 1699 */           this.fXPointerChildConfig = this.fChildConfig;
/*      */         } else {
/* 1701 */           XIncludeHandler newHandler = (XIncludeHandler)this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xinclude-handler");
/*      */ 
/* 1706 */           newHandler.setParent(this);
/* 1707 */           newHandler.setDocumentHandler(getDocumentHandler());
/* 1708 */           this.fXIncludeChildConfig = this.fChildConfig;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1713 */       if (xpointer != null) {
/* 1714 */         this.fChildConfig = this.fXPointerChildConfig;
/*      */         try
/*      */         {
/* 1718 */           this.fXPtrProcessor.parseXPointer(xpointer);
/*      */         }
/*      */         catch (XNIException ex)
/*      */         {
/* 1722 */           reportResourceError("XMLResourceError", new Object[] { href, ex.getMessage() });
/*      */ 
/* 1725 */           return false;
/*      */         }
/*      */       } else {
/* 1728 */         this.fChildConfig = this.fXIncludeChildConfig;
/*      */       }
/*      */ 
/* 1732 */       if (this.fNeedCopyFeatures) {
/* 1733 */         copyFeatures(this.fSettings, this.fChildConfig);
/*      */       }
/* 1735 */       this.fNeedCopyFeatures = false;
/*      */       try
/*      */       {
/* 1738 */         this.fNamespaceContext.pushScope();
/*      */ 
/* 1740 */         this.fChildConfig.parse(includedSource);
/*      */ 
/* 1742 */         if (this.fErrorReporter != null) {
/* 1743 */           this.fErrorReporter.setDocumentLocator(this.fDocLocation);
/*      */         }
/*      */ 
/* 1747 */         if (xpointer != null)
/*      */         {
/* 1749 */           if (!this.fXPtrProcessor.isXPointerResolved()) {
/* 1750 */             Locale locale = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
/* 1751 */             reason = this.fXIncludeMessageFormatter.formatMessage(locale, "XPointerResolutionUnsuccessful", null);
/* 1752 */             reportResourceError("XMLResourceError", new Object[] { href, reason });
/*      */ 
/* 1754 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (XNIException e)
/*      */       {
/* 1760 */         if (this.fErrorReporter != null) {
/* 1761 */           this.fErrorReporter.setDocumentLocator(this.fDocLocation);
/*      */         }
/* 1763 */         reportFatalError("XMLParseError", new Object[] { href, e.getMessage() });
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*      */         String reason;
/* 1767 */         if (this.fErrorReporter != null) {
/* 1768 */           this.fErrorReporter.setDocumentLocator(this.fDocLocation);
/*      */         }
/*      */ 
/* 1773 */         reportResourceError("XMLResourceError", new Object[] { href, e.getMessage() });
/*      */ 
/* 1776 */         return 0;
/*      */       }
/*      */       finally {
/* 1779 */         this.fNamespaceContext.popScope();
/*      */       }
/*      */     }
/* 1782 */     else if (parse.equals(XINCLUDE_PARSE_TEXT))
/*      */     {
/* 1784 */       String encoding = attributes.getValue(XINCLUDE_ATTR_ENCODING);
/* 1785 */       includedSource.setEncoding(encoding);
/* 1786 */       XIncludeTextReader textReader = null;
/*      */       try
/*      */       {
/* 1790 */         if (!this.fIsXML11) {
/* 1791 */           if (this.fXInclude10TextReader == null) {
/* 1792 */             this.fXInclude10TextReader = new XIncludeTextReader(includedSource, this, this.fBufferSize);
/*      */           }
/*      */           else {
/* 1795 */             this.fXInclude10TextReader.setInputSource(includedSource);
/*      */           }
/* 1797 */           textReader = this.fXInclude10TextReader;
/*      */         }
/*      */         else {
/* 1800 */           if (this.fXInclude11TextReader == null) {
/* 1801 */             this.fXInclude11TextReader = new XInclude11TextReader(includedSource, this, this.fBufferSize);
/*      */           }
/*      */           else {
/* 1804 */             this.fXInclude11TextReader.setInputSource(includedSource);
/*      */           }
/* 1806 */           textReader = this.fXInclude11TextReader;
/*      */         }
/* 1808 */         textReader.setErrorReporter(this.fErrorReporter);
/* 1809 */         textReader.parse();
/*      */       }
/*      */       catch (MalformedByteSequenceException ex)
/*      */       {
/* 1813 */         this.fErrorReporter.reportError(ex.getDomain(), ex.getKey(), ex.getArguments(), (short)2);
/*      */       }
/*      */       catch (CharConversionException e)
/*      */       {
/* 1817 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, (short)2);
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1821 */         reportResourceError("TextResourceError", new Object[] { href, e.getMessage() });
/*      */ 
/* 1824 */         return false;
/*      */       }
/*      */       finally {
/* 1827 */         if (textReader != null)
/*      */           try {
/* 1829 */             textReader.close();
/*      */           }
/*      */           catch (IOException e) {
/* 1832 */             reportResourceError("TextResourceError", new Object[] { href, e.getMessage() });
/*      */ 
/* 1835 */             return false;
/*      */           }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1841 */       reportFatalError("InvalidParseValue", new Object[] { parse });
/*      */     }
/* 1843 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean hasXIncludeNamespace(QName element)
/*      */   {
/* 1855 */     return (element.uri == XINCLUDE_NS_URI) || (this.fNamespaceContext.getURI(element.prefix) == XINCLUDE_NS_URI);
/*      */   }
/*      */ 
/*      */   protected boolean isIncludeElement(QName element)
/*      */   {
/* 1868 */     return (element.localpart.equals(XINCLUDE_INCLUDE)) && (hasXIncludeNamespace(element));
/*      */   }
/*      */ 
/*      */   protected boolean isFallbackElement(QName element)
/*      */   {
/* 1881 */     return (element.localpart.equals(XINCLUDE_FALLBACK)) && (hasXIncludeNamespace(element));
/*      */   }
/*      */ 
/*      */   protected boolean sameBaseURIAsIncludeParent()
/*      */   {
/* 1895 */     String parentBaseURI = getIncludeParentBaseURI();
/* 1896 */     String baseURI = this.fCurrentBaseURI.getExpandedSystemId();
/*      */ 
/* 1905 */     return (parentBaseURI != null) && (parentBaseURI.equals(baseURI));
/*      */   }
/*      */ 
/*      */   protected boolean sameLanguageAsIncludeParent()
/*      */   {
/* 1921 */     String parentLanguage = getIncludeParentLanguage();
/* 1922 */     return (parentLanguage != null) && (parentLanguage.equalsIgnoreCase(this.fCurrentLanguage));
/*      */   }
/*      */ 
/*      */   protected boolean searchForRecursiveIncludes(XMLLocator includedSource)
/*      */   {
/* 1932 */     String includedSystemId = includedSource.getExpandedSystemId();
/*      */ 
/* 1934 */     if (includedSystemId == null) {
/*      */       try {
/* 1936 */         includedSystemId = XMLEntityManager.expandSystemId(includedSource.getLiteralSystemId(), includedSource.getBaseSystemId(), false);
/*      */       }
/*      */       catch (URI.MalformedURIException e)
/*      */       {
/* 1943 */         reportFatalError("ExpandedSystemId");
/*      */       }
/*      */     }
/*      */ 
/* 1947 */     if (includedSystemId.equals(this.fCurrentBaseURI.getExpandedSystemId())) {
/* 1948 */       return true;
/*      */     }
/*      */ 
/* 1951 */     if (this.fParentXIncludeHandler == null) {
/* 1952 */       return false;
/*      */     }
/* 1954 */     return this.fParentXIncludeHandler.searchForRecursiveIncludes(includedSource);
/*      */   }
/*      */ 
/*      */   protected boolean isTopLevelIncludedItem()
/*      */   {
/* 1965 */     return (isTopLevelIncludedItemViaInclude()) || (isTopLevelIncludedItemViaFallback());
/*      */   }
/*      */ 
/*      */   protected boolean isTopLevelIncludedItemViaInclude()
/*      */   {
/* 1970 */     return (this.fDepth == 1) && (!isRootDocument());
/*      */   }
/*      */ 
/*      */   protected boolean isTopLevelIncludedItemViaFallback()
/*      */   {
/* 1978 */     return getSawFallback(this.fDepth - 1);
/*      */   }
/*      */ 
/*      */   protected XMLAttributes processAttributes(XMLAttributes attributes)
/*      */   {
/* 1996 */     if (isTopLevelIncludedItem())
/*      */     {
/* 2000 */       if ((this.fFixupBaseURIs) && (!sameBaseURIAsIncludeParent())) {
/* 2001 */         if (attributes == null) {
/* 2002 */           attributes = new XMLAttributesImpl();
/*      */         }
/*      */ 
/* 2007 */         String uri = null;
/*      */         try {
/* 2009 */           uri = getRelativeBaseURI();
/*      */         }
/*      */         catch (URI.MalformedURIException e)
/*      */         {
/* 2014 */           uri = this.fCurrentBaseURI.getExpandedSystemId();
/*      */         }
/* 2016 */         int index = attributes.addAttribute(XML_BASE_QNAME, XMLSymbols.fCDATASymbol, uri);
/*      */ 
/* 2021 */         attributes.setSpecified(index, true);
/*      */       }
/*      */ 
/* 2027 */       if ((this.fFixupLanguage) && (!sameLanguageAsIncludeParent())) {
/* 2028 */         if (attributes == null) {
/* 2029 */           attributes = new XMLAttributesImpl();
/*      */         }
/* 2031 */         int index = attributes.addAttribute(XML_LANG_QNAME, XMLSymbols.fCDATASymbol, this.fCurrentLanguage);
/*      */ 
/* 2036 */         attributes.setSpecified(index, true);
/*      */       }
/*      */ 
/* 2040 */       Enumeration inscopeNS = this.fNamespaceContext.getAllPrefixes();
/* 2041 */       while (inscopeNS.hasMoreElements()) {
/* 2042 */         String prefix = (String)inscopeNS.nextElement();
/* 2043 */         String parentURI = this.fNamespaceContext.getURIFromIncludeParent(prefix);
/*      */ 
/* 2045 */         String uri = this.fNamespaceContext.getURI(prefix);
/* 2046 */         if ((parentURI != uri) && (attributes != null)) {
/* 2047 */           if (prefix == XMLSymbols.EMPTY_STRING) {
/* 2048 */             if (attributes.getValue(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS) == null)
/*      */             {
/* 2053 */               if (attributes == null) {
/* 2054 */                 attributes = new XMLAttributesImpl();
/*      */               }
/*      */ 
/* 2057 */               QName ns = (QName)NEW_NS_ATTR_QNAME.clone();
/* 2058 */               ns.prefix = null;
/* 2059 */               ns.localpart = XMLSymbols.PREFIX_XMLNS;
/* 2060 */               ns.rawname = XMLSymbols.PREFIX_XMLNS;
/* 2061 */               int index = attributes.addAttribute(ns, XMLSymbols.fCDATASymbol, uri != null ? uri : XMLSymbols.EMPTY_STRING);
/*      */ 
/* 2066 */               attributes.setSpecified(index, true);
/*      */ 
/* 2070 */               this.fNamespaceContext.declarePrefix(prefix, uri);
/*      */             }
/*      */           }
/* 2073 */           else if (attributes.getValue(NamespaceContext.XMLNS_URI, prefix) == null)
/*      */           {
/* 2076 */             if (attributes == null) {
/* 2077 */               attributes = new XMLAttributesImpl();
/*      */             }
/*      */ 
/* 2080 */             QName ns = (QName)NEW_NS_ATTR_QNAME.clone();
/* 2081 */             ns.localpart = prefix;
/* 2082 */             ns.rawname += prefix;
/* 2083 */             ns.rawname = (this.fSymbolTable != null ? this.fSymbolTable.addSymbol(ns.rawname) : ns.rawname.intern());
/*      */ 
/* 2086 */             int index = attributes.addAttribute(ns, XMLSymbols.fCDATASymbol, uri != null ? uri : XMLSymbols.EMPTY_STRING);
/*      */ 
/* 2091 */             attributes.setSpecified(index, true);
/*      */ 
/* 2095 */             this.fNamespaceContext.declarePrefix(prefix, uri);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2101 */     if (attributes != null) {
/* 2102 */       int length = attributes.getLength();
/* 2103 */       for (int i = 0; i < length; i++) {
/* 2104 */         String type = attributes.getType(i);
/* 2105 */         String value = attributes.getValue(i);
/* 2106 */         if (type == XMLSymbols.fENTITYSymbol) {
/* 2107 */           checkUnparsedEntity(value);
/*      */         }
/* 2109 */         if (type == XMLSymbols.fENTITIESSymbol)
/*      */         {
/* 2111 */           StringTokenizer st = new StringTokenizer(value);
/* 2112 */           while (st.hasMoreTokens()) {
/* 2113 */             String entName = st.nextToken();
/* 2114 */             checkUnparsedEntity(entName);
/*      */           }
/*      */         }
/* 2117 */         else if (type == XMLSymbols.fNOTATIONSymbol)
/*      */         {
/* 2119 */           checkNotation(value);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2130 */     return attributes;
/*      */   }
/*      */ 
/*      */   protected String getRelativeBaseURI()
/*      */     throws URI.MalformedURIException
/*      */   {
/* 2140 */     int includeParentDepth = getIncludeParentDepth();
/* 2141 */     String relativeURI = getRelativeURI(includeParentDepth);
/* 2142 */     if (isRootDocument()) {
/* 2143 */       return relativeURI;
/*      */     }
/*      */ 
/* 2146 */     if (relativeURI.equals("")) {
/* 2147 */       relativeURI = this.fCurrentBaseURI.getLiteralSystemId();
/*      */     }
/*      */ 
/* 2150 */     if (includeParentDepth == 0) {
/* 2151 */       if (this.fParentRelativeURI == null) {
/* 2152 */         this.fParentRelativeURI = this.fParentXIncludeHandler.getRelativeBaseURI();
/*      */       }
/*      */ 
/* 2155 */       if (this.fParentRelativeURI.equals("")) {
/* 2156 */         return relativeURI;
/*      */       }
/*      */ 
/* 2159 */       URI base = new URI(this.fParentRelativeURI, true);
/* 2160 */       URI uri = new URI(base, relativeURI);
/*      */ 
/* 2163 */       String baseScheme = base.getScheme();
/* 2164 */       String literalScheme = uri.getScheme();
/* 2165 */       if (!Objects.equals(baseScheme, literalScheme)) {
/* 2166 */         return relativeURI;
/*      */       }
/*      */ 
/* 2170 */       String baseAuthority = base.getAuthority();
/* 2171 */       String literalAuthority = uri.getAuthority();
/* 2172 */       if (!Objects.equals(baseAuthority, literalAuthority)) {
/* 2173 */         return uri.getSchemeSpecificPart();
/*      */       }
/*      */ 
/* 2181 */       String literalPath = uri.getPath();
/* 2182 */       String literalQuery = uri.getQueryString();
/* 2183 */       String literalFragment = uri.getFragment();
/* 2184 */       if ((literalQuery != null) || (literalFragment != null)) {
/* 2185 */         StringBuilder buffer = new StringBuilder();
/* 2186 */         if (literalPath != null) {
/* 2187 */           buffer.append(literalPath);
/*      */         }
/* 2189 */         if (literalQuery != null) {
/* 2190 */           buffer.append('?');
/* 2191 */           buffer.append(literalQuery);
/*      */         }
/* 2193 */         if (literalFragment != null) {
/* 2194 */           buffer.append('#');
/* 2195 */           buffer.append(literalFragment);
/*      */         }
/* 2197 */         return buffer.toString();
/*      */       }
/* 2199 */       return literalPath;
/*      */     }
/*      */ 
/* 2202 */     return relativeURI;
/*      */   }
/*      */ 
/*      */   private String getIncludeParentBaseURI()
/*      */   {
/* 2212 */     int depth = getIncludeParentDepth();
/* 2213 */     if ((!isRootDocument()) && (depth == 0)) {
/* 2214 */       return this.fParentXIncludeHandler.getIncludeParentBaseURI();
/*      */     }
/*      */ 
/* 2217 */     return getBaseURI(depth);
/*      */   }
/*      */ 
/*      */   private String getIncludeParentLanguage()
/*      */   {
/* 2227 */     int depth = getIncludeParentDepth();
/* 2228 */     if ((!isRootDocument()) && (depth == 0)) {
/* 2229 */       return this.fParentXIncludeHandler.getIncludeParentLanguage();
/*      */     }
/*      */ 
/* 2232 */     return getLanguage(depth);
/*      */   }
/*      */ 
/*      */   private int getIncludeParentDepth()
/*      */   {
/* 2247 */     for (int i = this.fDepth - 1; i >= 0; i--)
/*      */     {
/* 2254 */       if ((!getSawInclude(i)) && (!getSawFallback(i))) {
/* 2255 */         return i;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2260 */     return 0;
/*      */   }
/*      */ 
/*      */   private int getResultDepth()
/*      */   {
/* 2267 */     return this.fResultDepth;
/*      */   }
/*      */ 
/*      */   protected Augmentations modifyAugmentations(Augmentations augs)
/*      */   {
/* 2277 */     return modifyAugmentations(augs, false);
/*      */   }
/*      */ 
/*      */   protected Augmentations modifyAugmentations(Augmentations augs, boolean force)
/*      */   {
/* 2290 */     if ((force) || (isTopLevelIncludedItem())) {
/* 2291 */       if (augs == null) {
/* 2292 */         augs = new AugmentationsImpl();
/*      */       }
/* 2294 */       augs.putItem(XINCLUDE_INCLUDED, Boolean.TRUE);
/*      */     }
/* 2296 */     return augs;
/*      */   }
/*      */ 
/*      */   protected int getState(int depth) {
/* 2300 */     return this.fState[depth];
/*      */   }
/*      */ 
/*      */   protected int getState() {
/* 2304 */     return this.fState[this.fDepth];
/*      */   }
/*      */ 
/*      */   protected void setState(int state) {
/* 2308 */     if (this.fDepth >= this.fState.length) {
/* 2309 */       int[] newarray = new int[this.fDepth * 2];
/* 2310 */       System.arraycopy(this.fState, 0, newarray, 0, this.fState.length);
/* 2311 */       this.fState = newarray;
/*      */     }
/* 2313 */     this.fState[this.fDepth] = state;
/*      */   }
/*      */ 
/*      */   protected void setSawFallback(int depth, boolean val)
/*      */   {
/* 2325 */     if (depth >= this.fSawFallback.length) {
/* 2326 */       boolean[] newarray = new boolean[depth * 2];
/* 2327 */       System.arraycopy(this.fSawFallback, 0, newarray, 0, this.fSawFallback.length);
/* 2328 */       this.fSawFallback = newarray;
/*      */     }
/* 2330 */     this.fSawFallback[depth] = val;
/*      */   }
/*      */ 
/*      */   protected boolean getSawFallback(int depth)
/*      */   {
/* 2341 */     if (depth >= this.fSawFallback.length) {
/* 2342 */       return false;
/*      */     }
/* 2344 */     return this.fSawFallback[depth];
/*      */   }
/*      */ 
/*      */   protected void setSawInclude(int depth, boolean val)
/*      */   {
/* 2355 */     if (depth >= this.fSawInclude.length) {
/* 2356 */       boolean[] newarray = new boolean[depth * 2];
/* 2357 */       System.arraycopy(this.fSawInclude, 0, newarray, 0, this.fSawInclude.length);
/* 2358 */       this.fSawInclude = newarray;
/*      */     }
/* 2360 */     this.fSawInclude[depth] = val;
/*      */   }
/*      */ 
/*      */   protected boolean getSawInclude(int depth)
/*      */   {
/* 2371 */     if (depth >= this.fSawInclude.length) {
/* 2372 */       return false;
/*      */     }
/* 2374 */     return this.fSawInclude[depth];
/*      */   }
/*      */ 
/*      */   protected void reportResourceError(String key) {
/* 2378 */     reportFatalError(key, null);
/*      */   }
/*      */ 
/*      */   protected void reportResourceError(String key, Object[] args) {
/* 2382 */     reportError(key, args, (short)0);
/*      */   }
/*      */ 
/*      */   protected void reportFatalError(String key) {
/* 2386 */     reportFatalError(key, null);
/*      */   }
/*      */ 
/*      */   protected void reportFatalError(String key, Object[] args) {
/* 2390 */     reportError(key, args, (short)2);
/*      */   }
/*      */ 
/*      */   private void reportError(String key, Object[] args, short severity) {
/* 2394 */     if (this.fErrorReporter != null)
/* 2395 */       this.fErrorReporter.reportError("http://www.w3.org/TR/xinclude", key, args, severity);
/*      */   }
/*      */ 
/*      */   protected void setParent(XIncludeHandler parent)
/*      */   {
/* 2410 */     this.fParentXIncludeHandler = parent;
/*      */   }
/*      */ 
/*      */   protected boolean isRootDocument()
/*      */   {
/* 2415 */     return this.fParentXIncludeHandler == null;
/*      */   }
/*      */ 
/*      */   protected void addUnparsedEntity(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations)
/*      */   {
/* 2429 */     UnparsedEntity ent = new UnparsedEntity();
/* 2430 */     ent.name = name;
/* 2431 */     ent.systemId = identifier.getLiteralSystemId();
/* 2432 */     ent.publicId = identifier.getPublicId();
/* 2433 */     ent.baseURI = identifier.getBaseSystemId();
/* 2434 */     ent.expandedSystemId = identifier.getExpandedSystemId();
/* 2435 */     ent.notation = notation;
/* 2436 */     ent.augmentations = augmentations;
/* 2437 */     this.fUnparsedEntities.add(ent);
/*      */   }
/*      */ 
/*      */   protected void addNotation(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
/*      */   {
/* 2450 */     Notation not = new Notation();
/* 2451 */     not.name = name;
/* 2452 */     not.systemId = identifier.getLiteralSystemId();
/* 2453 */     not.publicId = identifier.getPublicId();
/* 2454 */     not.baseURI = identifier.getBaseSystemId();
/* 2455 */     not.expandedSystemId = identifier.getExpandedSystemId();
/* 2456 */     not.augmentations = augmentations;
/* 2457 */     this.fNotations.add(not);
/*      */   }
/*      */ 
/*      */   protected void checkUnparsedEntity(String entName)
/*      */   {
/* 2469 */     UnparsedEntity ent = new UnparsedEntity();
/* 2470 */     ent.name = entName;
/* 2471 */     int index = this.fUnparsedEntities.indexOf(ent);
/* 2472 */     if (index != -1) {
/* 2473 */       ent = (UnparsedEntity)this.fUnparsedEntities.get(index);
/*      */ 
/* 2475 */       checkNotation(ent.notation);
/* 2476 */       checkAndSendUnparsedEntity(ent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkNotation(String notName)
/*      */   {
/* 2488 */     Notation not = new Notation();
/* 2489 */     not.name = notName;
/* 2490 */     int index = this.fNotations.indexOf(not);
/* 2491 */     if (index != -1) {
/* 2492 */       not = (Notation)this.fNotations.get(index);
/* 2493 */       checkAndSendNotation(not);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkAndSendUnparsedEntity(UnparsedEntity ent)
/*      */   {
/* 2505 */     if (isRootDocument()) {
/* 2506 */       int index = this.fUnparsedEntities.indexOf(ent);
/* 2507 */       if (index == -1)
/*      */       {
/* 2511 */         XMLResourceIdentifier id = new XMLResourceIdentifierImpl(ent.publicId, ent.systemId, ent.baseURI, ent.expandedSystemId);
/*      */ 
/* 2517 */         addUnparsedEntity(ent.name, id, ent.notation, ent.augmentations);
/*      */ 
/* 2522 */         if ((this.fSendUEAndNotationEvents) && (this.fDTDHandler != null)) {
/* 2523 */           this.fDTDHandler.unparsedEntityDecl(ent.name, id, ent.notation, ent.augmentations);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2531 */         UnparsedEntity localEntity = (UnparsedEntity)this.fUnparsedEntities.get(index);
/*      */ 
/* 2533 */         if (!ent.isDuplicate(localEntity)) {
/* 2534 */           reportFatalError("NonDuplicateUnparsedEntity", new Object[] { ent.name });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2541 */       this.fParentXIncludeHandler.checkAndSendUnparsedEntity(ent);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void checkAndSendNotation(Notation not)
/*      */   {
/* 2553 */     if (isRootDocument()) {
/* 2554 */       int index = this.fNotations.indexOf(not);
/* 2555 */       if (index == -1)
/*      */       {
/* 2557 */         XMLResourceIdentifier id = new XMLResourceIdentifierImpl(not.publicId, not.systemId, not.baseURI, not.expandedSystemId);
/*      */ 
/* 2563 */         addNotation(not.name, id, not.augmentations);
/* 2564 */         if ((this.fSendUEAndNotationEvents) && (this.fDTDHandler != null))
/* 2565 */           this.fDTDHandler.notationDecl(not.name, id, not.augmentations);
/*      */       }
/*      */       else
/*      */       {
/* 2569 */         Notation localNotation = (Notation)this.fNotations.get(index);
/* 2570 */         if (!not.isDuplicate(localNotation)) {
/* 2571 */           reportFatalError("NonDuplicateNotation", new Object[] { not.name });
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2578 */       this.fParentXIncludeHandler.checkAndSendNotation(not);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkWhitespace(XMLString value)
/*      */   {
/* 2588 */     int end = value.offset + value.length;
/* 2589 */     for (int i = value.offset; i < end; i++)
/* 2590 */       if (!XMLChar.isSpace(value.ch[i])) {
/* 2591 */         reportFatalError("ContentIllegalAtTopLevel");
/* 2592 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   private void checkMultipleRootElements()
/*      */   {
/* 2601 */     if (getRootElementProcessed()) {
/* 2602 */       reportFatalError("MultipleRootElements");
/*      */     }
/* 2604 */     setRootElementProcessed(true);
/*      */   }
/*      */ 
/*      */   private void setRootElementProcessed(boolean seenRoot)
/*      */   {
/* 2611 */     if (isRootDocument()) {
/* 2612 */       this.fSeenRootElement = seenRoot;
/* 2613 */       return;
/*      */     }
/* 2615 */     this.fParentXIncludeHandler.setRootElementProcessed(seenRoot);
/*      */   }
/*      */ 
/*      */   private boolean getRootElementProcessed()
/*      */   {
/* 2622 */     return isRootDocument() ? this.fSeenRootElement : this.fParentXIncludeHandler.getRootElementProcessed();
/*      */   }
/*      */ 
/*      */   protected void copyFeatures(XMLComponentManager from, ParserConfigurationSettings to)
/*      */   {
/* 2630 */     Enumeration features = Constants.getXercesFeatures();
/* 2631 */     copyFeatures1(features, "http://apache.org/xml/features/", from, to);
/* 2632 */     features = Constants.getSAXFeatures();
/* 2633 */     copyFeatures1(features, "http://xml.org/sax/features/", from, to);
/*      */   }
/*      */ 
/*      */   protected void copyFeatures(XMLComponentManager from, XMLParserConfiguration to)
/*      */   {
/* 2639 */     Enumeration features = Constants.getXercesFeatures();
/* 2640 */     copyFeatures1(features, "http://apache.org/xml/features/", from, to);
/* 2641 */     features = Constants.getSAXFeatures();
/* 2642 */     copyFeatures1(features, "http://xml.org/sax/features/", from, to);
/*      */   }
/*      */ 
/*      */   private void copyFeatures1(Enumeration features, String featurePrefix, XMLComponentManager from, ParserConfigurationSettings to)
/*      */   {
/* 2650 */     while (features.hasMoreElements()) {
/* 2651 */       String featureId = featurePrefix + (String)features.nextElement();
/*      */ 
/* 2653 */       to.addRecognizedFeatures(new String[] { featureId });
/*      */       try
/*      */       {
/* 2656 */         to.setFeature(featureId, from.getFeature(featureId));
/*      */       }
/*      */       catch (XMLConfigurationException e)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void copyFeatures1(Enumeration features, String featurePrefix, XMLComponentManager from, XMLParserConfiguration to)
/*      */   {
/* 2670 */     while (features.hasMoreElements()) {
/* 2671 */       String featureId = featurePrefix + (String)features.nextElement();
/* 2672 */       boolean value = from.getFeature(featureId);
/*      */       try
/*      */       {
/* 2675 */         to.setFeature(featureId, value);
/*      */       }
/*      */       catch (XMLConfigurationException e)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void saveBaseURI()
/*      */   {
/* 2774 */     this.fBaseURIScope.push(this.fDepth);
/* 2775 */     this.fBaseURI.push(this.fCurrentBaseURI.getBaseSystemId());
/* 2776 */     this.fLiteralSystemID.push(this.fCurrentBaseURI.getLiteralSystemId());
/* 2777 */     this.fExpandedSystemID.push(this.fCurrentBaseURI.getExpandedSystemId());
/*      */   }
/*      */ 
/*      */   protected void restoreBaseURI()
/*      */   {
/* 2784 */     this.fBaseURI.pop();
/* 2785 */     this.fLiteralSystemID.pop();
/* 2786 */     this.fExpandedSystemID.pop();
/* 2787 */     this.fBaseURIScope.pop();
/* 2788 */     this.fCurrentBaseURI.setBaseSystemId((String)this.fBaseURI.peek());
/* 2789 */     this.fCurrentBaseURI.setLiteralSystemId((String)this.fLiteralSystemID.peek());
/* 2790 */     this.fCurrentBaseURI.setExpandedSystemId((String)this.fExpandedSystemID.peek());
/*      */   }
/*      */ 
/*      */   protected void saveLanguage(String language)
/*      */   {
/* 2801 */     this.fLanguageScope.push(this.fDepth);
/* 2802 */     this.fLanguageStack.push(language);
/*      */   }
/*      */ 
/*      */   public String restoreLanguage()
/*      */   {
/* 2809 */     this.fLanguageStack.pop();
/* 2810 */     this.fLanguageScope.pop();
/* 2811 */     return (String)this.fLanguageStack.peek();
/*      */   }
/*      */ 
/*      */   public String getBaseURI(int depth)
/*      */   {
/* 2820 */     int scope = scopeOfBaseURI(depth);
/* 2821 */     return (String)this.fExpandedSystemID.elementAt(scope);
/*      */   }
/*      */ 
/*      */   public String getLanguage(int depth)
/*      */   {
/* 2830 */     int scope = scopeOfLanguage(depth);
/* 2831 */     return (String)this.fLanguageStack.elementAt(scope);
/*      */   }
/*      */ 
/*      */   public String getRelativeURI(int depth)
/*      */     throws URI.MalformedURIException
/*      */   {
/* 2846 */     int start = scopeOfBaseURI(depth) + 1;
/* 2847 */     if (start == this.fBaseURIScope.size())
/*      */     {
/* 2849 */       return "";
/*      */     }
/* 2851 */     URI uri = new URI("file", (String)this.fLiteralSystemID.elementAt(start));
/* 2852 */     for (int i = start + 1; i < this.fBaseURIScope.size(); i++) {
/* 2853 */       uri = new URI(uri, (String)this.fLiteralSystemID.elementAt(i));
/*      */     }
/* 2855 */     return uri.getPath();
/*      */   }
/*      */ 
/*      */   private int scopeOfBaseURI(int depth)
/*      */   {
/* 2862 */     for (int i = this.fBaseURIScope.size() - 1; i >= 0; i--) {
/* 2863 */       if (this.fBaseURIScope.elementAt(i) <= depth) {
/* 2864 */         return i;
/*      */       }
/*      */     }
/* 2867 */     return -1;
/*      */   }
/*      */ 
/*      */   private int scopeOfLanguage(int depth) {
/* 2871 */     for (int i = this.fLanguageScope.size() - 1; i >= 0; i--) {
/* 2872 */       if (this.fLanguageScope.elementAt(i) <= depth) {
/* 2873 */         return i;
/*      */       }
/*      */     }
/* 2876 */     return -1;
/*      */   }
/*      */ 
/*      */   protected void processXMLBaseAttributes(XMLAttributes attributes)
/*      */   {
/* 2884 */     String baseURIValue = attributes.getValue(NamespaceContext.XML_URI, "base");
/*      */ 
/* 2886 */     if (baseURIValue != null)
/*      */       try {
/* 2888 */         String expandedValue = XMLEntityManager.expandSystemId(baseURIValue, this.fCurrentBaseURI.getExpandedSystemId(), false);
/*      */ 
/* 2893 */         this.fCurrentBaseURI.setLiteralSystemId(baseURIValue);
/* 2894 */         this.fCurrentBaseURI.setBaseSystemId(this.fCurrentBaseURI.getExpandedSystemId());
/*      */ 
/* 2896 */         this.fCurrentBaseURI.setExpandedSystemId(expandedValue);
/*      */ 
/* 2899 */         saveBaseURI();
/*      */       }
/*      */       catch (URI.MalformedURIException e)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void processXMLLangAttributes(XMLAttributes attributes)
/*      */   {
/* 2912 */     String language = attributes.getValue(NamespaceContext.XML_URI, "lang");
/* 2913 */     if (language != null) {
/* 2914 */       this.fCurrentLanguage = language;
/* 2915 */       saveLanguage(this.fCurrentLanguage);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isValidInHTTPHeader(String value)
/*      */   {
/* 2929 */     for (int i = value.length() - 1; i >= 0; i--) {
/* 2930 */       char ch = value.charAt(i);
/* 2931 */       if ((ch < ' ') || (ch > '~')) {
/* 2932 */         return false;
/*      */       }
/*      */     }
/* 2935 */     return true;
/*      */   }
/*      */ 
/*      */   private XMLInputSource createInputSource(String publicId, String systemId, String baseSystemId, String accept, String acceptLanguage)
/*      */   {
/* 2945 */     HTTPInputSource httpSource = new HTTPInputSource(publicId, systemId, baseSystemId);
/* 2946 */     if ((accept != null) && (accept.length() > 0)) {
/* 2947 */       httpSource.setHTTPRequestProperty("Accept", accept);
/*      */     }
/* 2949 */     if ((acceptLanguage != null) && (acceptLanguage.length() > 0)) {
/* 2950 */       httpSource.setHTTPRequestProperty("Accept-Language", acceptLanguage);
/*      */     }
/* 2952 */     return httpSource;
/*      */   }
/*      */ 
/*      */   private String escapeHref(String href)
/*      */   {
/* 2989 */     int len = href.length();
/*      */ 
/* 2991 */     StringBuilder buffer = new StringBuilder(len * 3);
/*      */ 
/* 2994 */     for (int i = 0; 
/* 2995 */       i < len; i++) {
/* 2996 */       int ch = href.charAt(i);
/*      */ 
/* 2998 */       if (ch > 126)
/*      */       {
/*      */         break;
/*      */       }
/* 3002 */       if (ch < 32) {
/* 3003 */         return href;
/*      */       }
/* 3005 */       if (gNeedEscaping[ch] != 0) {
/* 3006 */         buffer.append('%');
/* 3007 */         buffer.append(gAfterEscaping1[ch]);
/* 3008 */         buffer.append(gAfterEscaping2[ch]);
/*      */       }
/*      */       else {
/* 3011 */         buffer.append((char)ch);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3016 */     if (i < len)
/*      */     {
/* 3018 */       for (int j = i; j < len; j++) {
/* 3019 */         int ch = href.charAt(j);
/* 3020 */         if (((ch < 32) || (ch > 126)) && ((ch < 160) || (ch > 55295)) && ((ch < 63744) || (ch > 64975)) && ((ch < 65008) || (ch > 65519)))
/*      */         {
/* 3026 */           if (XMLChar.isHighSurrogate(ch)) { j++; if (j < len) {
/* 3027 */               int ch2 = href.charAt(j);
/* 3028 */               if (XMLChar.isLowSurrogate(ch2)) {
/* 3029 */                 ch2 = XMLChar.supplemental((char)ch, (char)ch2);
/* 3030 */                 if ((ch2 < 983040) && ((ch2 & 0xFFFF) <= 65533)) {
/*      */                   continue;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 3036 */           return href;
/*      */         }
/*      */       }
/*      */ 
/* 3040 */       byte[] bytes = null;
/*      */       try
/*      */       {
/* 3043 */         bytes = href.substring(i).getBytes("UTF-8");
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {
/* 3046 */         return href;
/*      */       }
/* 3048 */       len = bytes.length;
/*      */ 
/* 3051 */       for (i = 0; i < len; i++) {
/* 3052 */         byte b = bytes[i];
/*      */ 
/* 3054 */         if (b < 0) {
/* 3055 */           int ch = b + 256;
/* 3056 */           buffer.append('%');
/* 3057 */           buffer.append(gHexChs[(ch >> 4)]);
/* 3058 */           buffer.append(gHexChs[(ch & 0xF)]);
/*      */         }
/* 3060 */         else if (gNeedEscaping[b] != 0) {
/* 3061 */           buffer.append('%');
/* 3062 */           buffer.append(gAfterEscaping1[b]);
/* 3063 */           buffer.append(gAfterEscaping2[b]);
/*      */         }
/*      */         else {
/* 3066 */           buffer.append((char)b);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3073 */     if (buffer.length() != len) {
/* 3074 */       return buffer.toString();
/*      */     }
/*      */ 
/* 3077 */     return href;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 2965 */     char[] escChs = { ' ', '<', '>', '"', '{', '}', '|', '\\', '^', '`' };
/* 2966 */     int len = escChs.length;
/*      */ 
/* 2968 */     for (int i = 0; i < len; i++) {
/* 2969 */       char ch = escChs[i];
/* 2970 */       gNeedEscaping[ch] = true;
/* 2971 */       gAfterEscaping1[ch] = gHexChs[(ch >> '\004')];
/* 2972 */       gAfterEscaping2[ch] = gHexChs[(ch & 0xF)];
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class Notation
/*      */   {
/*      */     public String name;
/*      */     public String systemId;
/*      */     public String baseURI;
/*      */     public String publicId;
/*      */     public String expandedSystemId;
/*      */     public Augmentations augmentations;
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/* 2698 */       return (obj == this) || (((obj instanceof Notation)) && (Objects.equals(this.name, ((Notation)obj).name)));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2704 */       return Objects.hashCode(this.name);
/*      */     }
/*      */ 
/*      */     public boolean isDuplicate(Object obj)
/*      */     {
/* 2715 */       if ((obj != null) && ((obj instanceof Notation))) {
/* 2716 */         Notation other = (Notation)obj;
/* 2717 */         return (Objects.equals(this.name, other.name)) && (Objects.equals(this.publicId, other.publicId)) && (Objects.equals(this.expandedSystemId, other.expandedSystemId));
/*      */       }
/*      */ 
/* 2721 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class UnparsedEntity {
/*      */     public String name;
/*      */     public String systemId;
/*      */     public String baseURI;
/*      */     public String publicId;
/*      */     public String expandedSystemId;
/*      */     public String notation;
/*      */     public Augmentations augmentations;
/*      */ 
/* 2740 */     public boolean equals(Object obj) { return (obj == this) || (((obj instanceof UnparsedEntity)) && (Objects.equals(this.name, ((UnparsedEntity)obj).name))); }
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2746 */       return Objects.hashCode(this.name);
/*      */     }
/*      */ 
/*      */     public boolean isDuplicate(Object obj)
/*      */     {
/* 2757 */       if ((obj != null) && ((obj instanceof UnparsedEntity))) {
/* 2758 */         UnparsedEntity other = (UnparsedEntity)obj;
/* 2759 */         return (Objects.equals(this.name, other.name)) && (Objects.equals(this.publicId, other.publicId)) && (Objects.equals(this.expandedSystemId, other.expandedSystemId)) && (Objects.equals(this.notation, other.notation));
/*      */       }
/*      */ 
/* 2764 */       return false;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler
 * JD-Core Version:    0.6.2
 */