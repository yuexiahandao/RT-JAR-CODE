/*      */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*      */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.AttributesProxy;
/*      */ import com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper;
/*      */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*      */ import com.sun.org.apache.xerces.internal.util.Status;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.Property;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*      */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.ItemPSVI;
/*      */ import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.util.HashMap;
/*      */ import javax.xml.parsers.FactoryConfigurationError;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.sax.SAXResult;
/*      */ import javax.xml.transform.sax.SAXSource;
/*      */ import javax.xml.validation.TypeInfoProvider;
/*      */ import javax.xml.validation.ValidatorHandler;
/*      */ import org.w3c.dom.TypeInfo;
/*      */ import org.w3c.dom.ls.LSInput;
/*      */ import org.w3c.dom.ls.LSResourceResolver;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXNotRecognizedException;
/*      */ import org.xml.sax.SAXNotSupportedException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.ext.Attributes2;
/*      */ import org.xml.sax.ext.EntityResolver2;
/*      */ 
/*      */ final class ValidatorHandlerImpl extends ValidatorHandler
/*      */   implements DTDHandler, EntityState, PSVIProvider, ValidatorHelper, XMLDocumentHandler
/*      */ {
/*      */   private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
/*      */   protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
/*      */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*      */   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*      */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*      */   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*      */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*      */   private XMLErrorReporter fErrorReporter;
/*      */   private NamespaceContext fNamespaceContext;
/*      */   private XMLSchemaValidator fSchemaValidator;
/*      */   private SymbolTable fSymbolTable;
/*      */   private ValidationManager fValidationManager;
/*      */   private XMLSchemaValidatorComponentManager fComponentManager;
/*  165 */   private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
/*      */ 
/*  168 */   private boolean fNeedPushNSContext = true;
/*      */ 
/*  171 */   private HashMap fUnparsedEntities = null;
/*      */ 
/*  174 */   private boolean fStringsInternalized = false;
/*      */ 
/*  177 */   private final QName fElementQName = new QName();
/*  178 */   private final QName fAttributeQName = new QName();
/*  179 */   private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
/*  180 */   private final AttributesProxy fAttrAdapter = new AttributesProxy(this.fAttributes);
/*  181 */   private final XMLString fTempString = new XMLString();
/*      */ 
/*  187 */   private ContentHandler fContentHandler = null;
/*      */ 
/*  826 */   private final XMLSchemaTypeInfoProvider fTypeInfoProvider = new XMLSchemaTypeInfoProvider(null);
/*      */ 
/*  976 */   private final ResolutionForwarder fResolutionForwarder = new ResolutionForwarder(null);
/*      */ 
/*      */   public ValidatorHandlerImpl(XSGrammarPoolContainer grammarContainer)
/*      */   {
/*  194 */     this(new XMLSchemaValidatorComponentManager(grammarContainer));
/*  195 */     this.fComponentManager.addRecognizedFeatures(new String[] { "http://xml.org/sax/features/namespace-prefixes" });
/*  196 */     this.fComponentManager.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
/*  197 */     setErrorHandler(null);
/*  198 */     setResourceResolver(null);
/*      */   }
/*      */ 
/*      */   public ValidatorHandlerImpl(XMLSchemaValidatorComponentManager componentManager) {
/*  202 */     this.fComponentManager = componentManager;
/*  203 */     this.fErrorReporter = ((XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  204 */     this.fNamespaceContext = ((NamespaceContext)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
/*  205 */     this.fSchemaValidator = ((XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
/*  206 */     this.fSymbolTable = ((SymbolTable)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*  207 */     this.fValidationManager = ((ValidationManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
/*      */   }
/*      */ 
/*      */   public void setContentHandler(ContentHandler receiver)
/*      */   {
/*  215 */     this.fContentHandler = receiver;
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler() {
/*  219 */     return this.fContentHandler;
/*      */   }
/*      */ 
/*      */   public void setErrorHandler(ErrorHandler errorHandler) {
/*  223 */     this.fComponentManager.setErrorHandler(errorHandler);
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler() {
/*  227 */     return this.fComponentManager.getErrorHandler();
/*      */   }
/*      */ 
/*      */   public void setResourceResolver(LSResourceResolver resourceResolver) {
/*  231 */     this.fComponentManager.setResourceResolver(resourceResolver);
/*      */   }
/*      */ 
/*      */   public LSResourceResolver getResourceResolver() {
/*  235 */     return this.fComponentManager.getResourceResolver();
/*      */   }
/*      */ 
/*      */   public TypeInfoProvider getTypeInfoProvider() {
/*  239 */     return this.fTypeInfoProvider;
/*      */   }
/*      */ 
/*      */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  244 */     if (name == null)
/*  245 */       throw new NullPointerException();
/*      */     try
/*      */     {
/*  248 */       return this.fComponentManager.getFeature(name);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*  251 */       String identifier = e.getIdentifier();
/*  252 */       String key = e.getType() == Status.NOT_RECOGNIZED ? "feature-not-recognized" : "feature-not-supported";
/*      */ 
/*  254 */       throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[] { identifier }));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFeature(String name, boolean value)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  262 */     if (name == null)
/*  263 */       throw new NullPointerException();
/*      */     try
/*      */     {
/*  266 */       this.fComponentManager.setFeature(name, value);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*  269 */       String identifier = e.getIdentifier();
/*      */ 
/*  271 */       if (e.getType() == Status.NOT_ALLOWED)
/*      */       {
/*  273 */         throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "jaxp-secureprocessing-feature", null));
/*      */       }
/*      */       String key;
/*      */       String key;
/*  276 */       if (e.getType() == Status.NOT_RECOGNIZED)
/*  277 */         key = "feature-not-recognized";
/*      */       else {
/*  279 */         key = "feature-not-supported";
/*      */       }
/*  281 */       throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[] { identifier }));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  289 */     if (name == null)
/*  290 */       throw new NullPointerException();
/*      */     try
/*      */     {
/*  293 */       return this.fComponentManager.getProperty(name);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*  296 */       String identifier = e.getIdentifier();
/*  297 */       String key = e.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
/*      */ 
/*  299 */       throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[] { identifier }));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setProperty(String name, Object object)
/*      */     throws SAXNotRecognizedException, SAXNotSupportedException
/*      */   {
/*  307 */     if (name == null)
/*  308 */       throw new NullPointerException();
/*      */     try
/*      */     {
/*  311 */       this.fComponentManager.setProperty(name, object);
/*      */     }
/*      */     catch (XMLConfigurationException e) {
/*  314 */       String identifier = e.getIdentifier();
/*  315 */       String key = e.getType() == Status.NOT_RECOGNIZED ? "property-not-recognized" : "property-not-supported";
/*      */ 
/*  317 */       throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fComponentManager.getLocale(), key, new Object[] { identifier }));
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isEntityDeclared(String name)
/*      */   {
/*  328 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isEntityUnparsed(String name) {
/*  332 */     if (this.fUnparsedEntities != null) {
/*  333 */       return this.fUnparsedEntities.containsKey(name);
/*      */     }
/*  335 */     return false;
/*      */   }
/*      */ 
/*      */   public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  345 */     if (this.fContentHandler != null)
/*      */       try {
/*  347 */         this.fContentHandler.startDocument();
/*      */       }
/*      */       catch (SAXException e) {
/*  350 */         throw new XNIException(e);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs) throws XNIException {
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/*  365 */     if (this.fContentHandler != null)
/*      */       try {
/*  367 */         this.fContentHandler.processingInstruction(target, data.toString());
/*      */       }
/*      */       catch (SAXException e) {
/*  370 */         throw new XNIException(e);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  377 */     if (this.fContentHandler != null)
/*      */       try {
/*  379 */         this.fTypeInfoProvider.beginStartElement(augs, attributes);
/*  380 */         this.fContentHandler.startElement(element.uri != null ? element.uri : XMLSymbols.EMPTY_STRING, element.localpart, element.rawname, this.fAttrAdapter);
/*      */       }
/*      */       catch (SAXException e)
/*      */       {
/*  384 */         throw new XNIException(e);
/*      */       }
/*      */       finally {
/*  387 */         this.fTypeInfoProvider.finishStartElement();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  395 */     startElement(element, attributes, augs);
/*  396 */     endElement(element, augs);
/*      */   }
/*      */ 
/*      */   public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs) throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
/*      */   }
/*      */ 
/*      */   public void characters(XMLString text, Augmentations augs) throws XNIException {
/*  411 */     if (this.fContentHandler != null)
/*      */     {
/*  414 */       if (text.length == 0)
/*  415 */         return;
/*      */       try
/*      */       {
/*  418 */         this.fContentHandler.characters(text.ch, text.offset, text.length);
/*      */       }
/*      */       catch (SAXException e) {
/*  421 */         throw new XNIException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException
/*      */   {
/*  428 */     if (this.fContentHandler != null)
/*      */       try {
/*  430 */         this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
/*      */       }
/*      */       catch (SAXException e) {
/*  433 */         throw new XNIException(e);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void endElement(QName element, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  440 */     if (this.fContentHandler != null)
/*      */       try {
/*  442 */         this.fTypeInfoProvider.beginEndElement(augs);
/*  443 */         this.fContentHandler.endElement(element.uri != null ? element.uri : XMLSymbols.EMPTY_STRING, element.localpart, element.rawname);
/*      */       }
/*      */       catch (SAXException e)
/*      */       {
/*  447 */         throw new XNIException(e);
/*      */       }
/*      */       finally {
/*  450 */         this.fTypeInfoProvider.finishEndElement();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void startCDATA(Augmentations augs) throws XNIException {
/*      */   }
/*      */ 
/*      */   public void endCDATA(Augmentations augs) throws XNIException {
/*      */   }
/*      */ 
/*  460 */   public void endDocument(Augmentations augs) throws XNIException { if (this.fContentHandler != null)
/*      */       try {
/*  462 */         this.fContentHandler.endDocument();
/*      */       }
/*      */       catch (SAXException e) {
/*  465 */         throw new XNIException(e);
/*      */       } }
/*      */ 
/*      */   public void setDocumentSource(XMLDocumentSource source)
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLDocumentSource getDocumentSource()
/*      */   {
/*  474 */     return this.fSchemaValidator;
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator locator)
/*      */   {
/*  482 */     this.fSAXLocatorWrapper.setLocator(locator);
/*  483 */     if (this.fContentHandler != null)
/*  484 */       this.fContentHandler.setDocumentLocator(locator);
/*      */   }
/*      */ 
/*      */   public void startDocument() throws SAXException
/*      */   {
/*  489 */     this.fComponentManager.reset();
/*  490 */     this.fSchemaValidator.setDocumentHandler(this);
/*  491 */     this.fValidationManager.setEntityState(this);
/*  492 */     this.fTypeInfoProvider.finishStartElement();
/*  493 */     this.fNeedPushNSContext = true;
/*  494 */     if ((this.fUnparsedEntities != null) && (!this.fUnparsedEntities.isEmpty()))
/*      */     {
/*  496 */       this.fUnparsedEntities.clear();
/*      */     }
/*  498 */     this.fErrorReporter.setDocumentLocator(this.fSAXLocatorWrapper);
/*      */     try {
/*  500 */       this.fSchemaValidator.startDocument(this.fSAXLocatorWrapper, this.fSAXLocatorWrapper.getEncoding(), this.fNamespaceContext, null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  503 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  506 */       throw Util.toSAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument() throws SAXException {
/*  511 */     this.fSAXLocatorWrapper.setLocator(null);
/*      */     try {
/*  513 */       this.fSchemaValidator.endDocument(null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  516 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  519 */       throw Util.toSAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*      */     String uriSymbol;
/*      */     String prefixSymbol;
/*      */     String uriSymbol;
/*  527 */     if (!this.fStringsInternalized) {
/*  528 */       String prefixSymbol = prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING;
/*  529 */       uriSymbol = (uri != null) && (uri.length() > 0) ? this.fSymbolTable.addSymbol(uri) : null;
/*      */     }
/*      */     else {
/*  532 */       prefixSymbol = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
/*  533 */       uriSymbol = (uri != null) && (uri.length() > 0) ? uri : null;
/*      */     }
/*  535 */     if (this.fNeedPushNSContext) {
/*  536 */       this.fNeedPushNSContext = false;
/*  537 */       this.fNamespaceContext.pushContext();
/*      */     }
/*  539 */     this.fNamespaceContext.declarePrefix(prefixSymbol, uriSymbol);
/*  540 */     if (this.fContentHandler != null)
/*  541 */       this.fContentHandler.startPrefixMapping(prefix, uri);
/*      */   }
/*      */ 
/*      */   public void endPrefixMapping(String prefix) throws SAXException
/*      */   {
/*  546 */     if (this.fContentHandler != null)
/*  547 */       this.fContentHandler.endPrefixMapping(prefix);
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*      */     throws SAXException
/*      */   {
/*  553 */     if (this.fNeedPushNSContext) {
/*  554 */       this.fNamespaceContext.pushContext();
/*      */     }
/*  556 */     this.fNeedPushNSContext = true;
/*      */ 
/*  559 */     fillQName(this.fElementQName, uri, localName, qName);
/*      */ 
/*  562 */     if ((atts instanceof Attributes2)) {
/*  563 */       fillXMLAttributes2((Attributes2)atts);
/*      */     }
/*      */     else {
/*  566 */       fillXMLAttributes(atts);
/*      */     }
/*      */     try
/*      */     {
/*  570 */       this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  573 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  576 */       throw Util.toSAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName, String qName) throws SAXException
/*      */   {
/*  582 */     fillQName(this.fElementQName, uri, localName, qName);
/*      */     try {
/*  584 */       this.fSchemaValidator.endElement(this.fElementQName, null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  587 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  590 */       throw Util.toSAXException(e);
/*      */     }
/*      */     finally {
/*  593 */       this.fNamespaceContext.popContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int length) throws SAXException
/*      */   {
/*      */     try {
/*  600 */       this.fTempString.setValues(ch, start, length);
/*  601 */       this.fSchemaValidator.characters(this.fTempString, null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  604 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  607 */       throw Util.toSAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
/*      */   {
/*      */     try {
/*  614 */       this.fTempString.setValues(ch, start, length);
/*  615 */       this.fSchemaValidator.ignorableWhitespace(this.fTempString, null);
/*      */     }
/*      */     catch (XMLParseException e) {
/*  618 */       throw Util.toSAXParseException(e);
/*      */     }
/*      */     catch (XNIException e) {
/*  621 */       throw Util.toSAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*  632 */     if (this.fContentHandler != null)
/*  633 */       this.fContentHandler.processingInstruction(target, data);
/*      */   }
/*      */ 
/*      */   public void skippedEntity(String name)
/*      */     throws SAXException
/*      */   {
/*  640 */     if (this.fContentHandler != null)
/*  641 */       this.fContentHandler.skippedEntity(name);
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, String publicId, String systemId)
/*      */     throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*      */     throws SAXException
/*      */   {
/*  654 */     if (this.fUnparsedEntities == null) {
/*  655 */       this.fUnparsedEntities = new HashMap();
/*      */     }
/*  657 */     this.fUnparsedEntities.put(name, name);
/*      */   }
/*      */ 
/*      */   public void validate(Source source, Result result)
/*      */     throws SAXException, IOException
/*      */   {
/*  666 */     if (((result instanceof SAXResult)) || (result == null)) {
/*  667 */       SAXSource saxSource = (SAXSource)source;
/*  668 */       SAXResult saxResult = (SAXResult)result;
/*      */ 
/*  670 */       if (result != null) {
/*  671 */         setContentHandler(saxResult.getHandler());
/*      */       }
/*      */       try
/*      */       {
/*  675 */         XMLReader reader = saxSource.getXMLReader();
/*  676 */         if (reader == null)
/*      */         {
/*  678 */           SAXParserFactory spf = this.fComponentManager.getFeature("http://www.oracle.com/feature/use-service-mechanism") ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*      */ 
/*  680 */           spf.setNamespaceAware(true);
/*      */           try {
/*  682 */             spf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this.fComponentManager.getFeature("http://javax.xml.XMLConstants/feature/secure-processing"));
/*      */ 
/*  684 */             reader = spf.newSAXParser().getXMLReader();
/*      */ 
/*  686 */             if ((reader instanceof com.sun.org.apache.xerces.internal.parsers.SAXParser)) {
/*  687 */               XMLSecurityManager securityManager = (XMLSecurityManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
/*  688 */               if (securityManager != null)
/*      */                 try {
/*  690 */                   reader.setProperty("http://apache.org/xml/properties/security-manager", securityManager);
/*      */                 }
/*      */                 catch (SAXException exc)
/*      */                 {
/*      */                 }
/*      */               try {
/*  696 */                 XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager)this.fComponentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
/*      */ 
/*  698 */                 reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD));
/*      */               }
/*      */               catch (SAXException exc) {
/*  701 */                 System.err.println("Warning: " + reader.getClass().getName() + ": " + exc.getMessage());
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (Exception e)
/*      */           {
/*  707 */             throw new FactoryConfigurationError(e);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  714 */           this.fStringsInternalized = reader.getFeature("http://xml.org/sax/features/string-interning");
/*      */         }
/*      */         catch (SAXException exc)
/*      */         {
/*  719 */           this.fStringsInternalized = false;
/*      */         }
/*      */ 
/*  722 */         ErrorHandler errorHandler = this.fComponentManager.getErrorHandler();
/*  723 */         reader.setErrorHandler(errorHandler != null ? errorHandler : DraconianErrorHandler.getInstance());
/*  724 */         reader.setEntityResolver(this.fResolutionForwarder);
/*  725 */         this.fResolutionForwarder.setEntityResolver(this.fComponentManager.getResourceResolver());
/*  726 */         reader.setContentHandler(this);
/*  727 */         reader.setDTDHandler(this);
/*      */ 
/*  729 */         InputSource is = saxSource.getInputSource();
/*  730 */         reader.parse(is);
/*      */       }
/*      */       finally
/*      */       {
/*  734 */         setContentHandler(null);
/*      */       }
/*  736 */       return;
/*      */     }
/*  738 */     throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { source.getClass().getName(), result.getClass().getName() }));
/*      */   }
/*      */ 
/*      */   public ElementPSVI getElementPSVI()
/*      */   {
/*  748 */     return this.fTypeInfoProvider.getElementPSVI();
/*      */   }
/*      */ 
/*      */   public AttributePSVI getAttributePSVI(int index) {
/*  752 */     return this.fTypeInfoProvider.getAttributePSVI(index);
/*      */   }
/*      */ 
/*      */   public AttributePSVI getAttributePSVIByName(String uri, String localname) {
/*  756 */     return this.fTypeInfoProvider.getAttributePSVIByName(uri, localname);
/*      */   }
/*      */ 
/*      */   private void fillQName(QName toFill, String uri, String localpart, String raw)
/*      */   {
/*  767 */     if (!this.fStringsInternalized) {
/*  768 */       uri = (uri != null) && (uri.length() > 0) ? this.fSymbolTable.addSymbol(uri) : null;
/*  769 */       localpart = localpart != null ? this.fSymbolTable.addSymbol(localpart) : XMLSymbols.EMPTY_STRING;
/*  770 */       raw = raw != null ? this.fSymbolTable.addSymbol(raw) : XMLSymbols.EMPTY_STRING;
/*      */     }
/*      */     else {
/*  773 */       if ((uri != null) && (uri.length() == 0)) {
/*  774 */         uri = null;
/*      */       }
/*  776 */       if (localpart == null) {
/*  777 */         localpart = XMLSymbols.EMPTY_STRING;
/*      */       }
/*  779 */       if (raw == null) {
/*  780 */         raw = XMLSymbols.EMPTY_STRING;
/*      */       }
/*      */     }
/*  783 */     String prefix = XMLSymbols.EMPTY_STRING;
/*  784 */     int prefixIdx = raw.indexOf(':');
/*  785 */     if (prefixIdx != -1) {
/*  786 */       prefix = this.fSymbolTable.addSymbol(raw.substring(0, prefixIdx));
/*      */     }
/*  788 */     toFill.setValues(prefix, localpart, raw, uri);
/*      */   }
/*      */ 
/*      */   private void fillXMLAttributes(Attributes att)
/*      */   {
/*  793 */     this.fAttributes.removeAllAttributes();
/*  794 */     int len = att.getLength();
/*  795 */     for (int i = 0; i < len; i++) {
/*  796 */       fillXMLAttribute(att, i);
/*  797 */       this.fAttributes.setSpecified(i, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fillXMLAttributes2(Attributes2 att)
/*      */   {
/*  803 */     this.fAttributes.removeAllAttributes();
/*  804 */     int len = att.getLength();
/*  805 */     for (int i = 0; i < len; i++) {
/*  806 */       fillXMLAttribute(att, i);
/*  807 */       this.fAttributes.setSpecified(i, att.isSpecified(i));
/*  808 */       if (att.isDeclared(i))
/*  809 */         this.fAttributes.getAugmentations(i).putItem("ATTRIBUTE_DECLARED", Boolean.TRUE);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fillXMLAttribute(Attributes att, int index)
/*      */   {
/*  816 */     fillQName(this.fAttributeQName, att.getURI(index), att.getLocalName(index), att.getQName(index));
/*  817 */     String type = att.getType(index);
/*  818 */     this.fAttributes.addAttributeNS(this.fAttributeQName, type != null ? type : XMLSymbols.fCDATASymbol, att.getValue(index));
/*      */   }
/*      */ 
/*      */   static final class ResolutionForwarder
/*      */     implements EntityResolver2
/*      */   {
/*      */     private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
/*      */     protected LSResourceResolver fEntityResolver;
/*      */ 
/*      */     public ResolutionForwarder()
/*      */     {
/*      */     }
/*      */ 
/*      */     public ResolutionForwarder(LSResourceResolver entityResolver)
/*      */     {
/*  999 */       setEntityResolver(entityResolver);
/*      */     }
/*      */ 
/*      */     public void setEntityResolver(LSResourceResolver entityResolver)
/*      */     {
/* 1008 */       this.fEntityResolver = entityResolver;
/*      */     }
/*      */ 
/*      */     public LSResourceResolver getEntityResolver()
/*      */     {
/* 1013 */       return this.fEntityResolver;
/*      */     }
/*      */ 
/*      */     public InputSource getExternalSubset(String name, String baseURI)
/*      */       throws SAXException, IOException
/*      */     {
/* 1021 */       return null;
/*      */     }
/*      */ 
/*      */     public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
/*      */       throws SAXException, IOException
/*      */     {
/* 1030 */       if (this.fEntityResolver != null) {
/* 1031 */         LSInput lsInput = this.fEntityResolver.resolveResource("http://www.w3.org/TR/REC-xml", null, publicId, systemId, baseURI);
/* 1032 */         if (lsInput != null) {
/* 1033 */           String pubId = lsInput.getPublicId();
/* 1034 */           String sysId = lsInput.getSystemId();
/* 1035 */           String baseSystemId = lsInput.getBaseURI();
/* 1036 */           Reader charStream = lsInput.getCharacterStream();
/* 1037 */           InputStream byteStream = lsInput.getByteStream();
/* 1038 */           String data = lsInput.getStringData();
/* 1039 */           String encoding = lsInput.getEncoding();
/*      */ 
/* 1048 */           InputSource inputSource = new InputSource();
/* 1049 */           inputSource.setPublicId(pubId);
/* 1050 */           inputSource.setSystemId(baseSystemId != null ? resolveSystemId(systemId, baseSystemId) : systemId);
/*      */ 
/* 1052 */           if (charStream != null) {
/* 1053 */             inputSource.setCharacterStream(charStream);
/*      */           }
/* 1055 */           else if (byteStream != null) {
/* 1056 */             inputSource.setByteStream(byteStream);
/*      */           }
/* 1058 */           else if ((data != null) && (data.length() != 0)) {
/* 1059 */             inputSource.setCharacterStream(new StringReader(data));
/*      */           }
/* 1061 */           inputSource.setEncoding(encoding);
/* 1062 */           return inputSource;
/*      */         }
/*      */       }
/* 1065 */       return null;
/*      */     }
/*      */ 
/*      */     public InputSource resolveEntity(String publicId, String systemId)
/*      */       throws SAXException, IOException
/*      */     {
/* 1071 */       return resolveEntity(null, publicId, null, systemId);
/*      */     }
/*      */ 
/*      */     private String resolveSystemId(String systemId, String baseURI)
/*      */     {
/*      */       try {
/* 1077 */         return XMLEntityManager.expandSystemId(systemId, baseURI, false);
/*      */       }
/*      */       catch (URI.MalformedURIException ex)
/*      */       {
/*      */       }
/*      */ 
/* 1083 */       return systemId;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class XMLSchemaTypeInfoProvider extends TypeInfoProvider
/*      */   {
/*      */     private Augmentations fElementAugs;
/*      */     private XMLAttributes fAttributes;
/*  836 */     private boolean fInStartElement = false;
/*      */ 
/*  839 */     private boolean fInEndElement = false;
/*      */ 
/*      */     private XMLSchemaTypeInfoProvider() {
/*      */     }
/*  843 */     void beginStartElement(Augmentations elementAugs, XMLAttributes attributes) { this.fInStartElement = true;
/*  844 */       this.fElementAugs = elementAugs;
/*  845 */       this.fAttributes = attributes;
/*      */     }
/*      */ 
/*      */     void finishStartElement()
/*      */     {
/*  850 */       this.fInStartElement = false;
/*  851 */       this.fElementAugs = null;
/*  852 */       this.fAttributes = null;
/*      */     }
/*      */ 
/*      */     void beginEndElement(Augmentations elementAugs)
/*      */     {
/*  857 */       this.fInEndElement = true;
/*  858 */       this.fElementAugs = elementAugs;
/*      */     }
/*      */ 
/*      */     void finishEndElement()
/*      */     {
/*  863 */       this.fInEndElement = false;
/*  864 */       this.fElementAugs = null;
/*      */     }
/*      */ 
/*      */     private void checkState(boolean forElementInfo)
/*      */     {
/*  873 */       if ((!this.fInStartElement) && ((!this.fInEndElement) || (!forElementInfo)))
/*  874 */         throw new IllegalStateException(JAXPValidationMessageFormatter.formatMessage(ValidatorHandlerImpl.this.fComponentManager.getLocale(), "TypeInfoProviderIllegalState", null));
/*      */     }
/*      */ 
/*      */     public TypeInfo getAttributeTypeInfo(int index)
/*      */     {
/*  880 */       checkState(false);
/*  881 */       return getAttributeType(index);
/*      */     }
/*      */ 
/*      */     private TypeInfo getAttributeType(int index) {
/*  885 */       checkState(false);
/*  886 */       if ((index < 0) || (this.fAttributes.getLength() <= index))
/*  887 */         throw new IndexOutOfBoundsException(Integer.toString(index));
/*  888 */       Augmentations augs = this.fAttributes.getAugmentations(index);
/*  889 */       if (augs == null) return null;
/*  890 */       AttributePSVI psvi = (AttributePSVI)augs.getItem("ATTRIBUTE_PSVI");
/*  891 */       return getTypeInfoFromPSVI(psvi);
/*      */     }
/*      */ 
/*      */     public TypeInfo getAttributeTypeInfo(String attributeUri, String attributeLocalName) {
/*  895 */       checkState(false);
/*  896 */       return getAttributeTypeInfo(this.fAttributes.getIndex(attributeUri, attributeLocalName));
/*      */     }
/*      */ 
/*      */     public TypeInfo getAttributeTypeInfo(String attributeQName) {
/*  900 */       checkState(false);
/*  901 */       return getAttributeTypeInfo(this.fAttributes.getIndex(attributeQName));
/*      */     }
/*      */ 
/*      */     public TypeInfo getElementTypeInfo() {
/*  905 */       checkState(true);
/*  906 */       if (this.fElementAugs == null) return null;
/*  907 */       ElementPSVI psvi = (ElementPSVI)this.fElementAugs.getItem("ELEMENT_PSVI");
/*  908 */       return getTypeInfoFromPSVI(psvi);
/*      */     }
/*      */ 
/*      */     private TypeInfo getTypeInfoFromPSVI(ItemPSVI psvi) {
/*  912 */       if (psvi == null) return null;
/*      */ 
/*  918 */       if (psvi.getValidity() == 2) {
/*  919 */         XSTypeDefinition t = psvi.getMemberTypeDefinition();
/*  920 */         if (t != null) {
/*  921 */           return (t instanceof TypeInfo) ? (TypeInfo)t : null;
/*      */         }
/*      */       }
/*      */ 
/*  925 */       XSTypeDefinition t = psvi.getTypeDefinition();
/*      */ 
/*  927 */       if (t != null) {
/*  928 */         return (t instanceof TypeInfo) ? (TypeInfo)t : null;
/*      */       }
/*  930 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isIdAttribute(int index) {
/*  934 */       checkState(false);
/*  935 */       XSSimpleType type = (XSSimpleType)getAttributeType(index);
/*  936 */       if (type == null) return false;
/*  937 */       return type.isIDType();
/*      */     }
/*      */ 
/*      */     public boolean isSpecified(int index) {
/*  941 */       checkState(false);
/*  942 */       return this.fAttributes.isSpecified(index);
/*      */     }
/*      */ 
/*      */     ElementPSVI getElementPSVI()
/*      */     {
/*  951 */       return this.fElementAugs != null ? (ElementPSVI)this.fElementAugs.getItem("ELEMENT_PSVI") : null;
/*      */     }
/*      */ 
/*      */     AttributePSVI getAttributePSVI(int index) {
/*  955 */       if (this.fAttributes != null) {
/*  956 */         Augmentations augs = this.fAttributes.getAugmentations(index);
/*  957 */         if (augs != null) {
/*  958 */           return (AttributePSVI)augs.getItem("ATTRIBUTE_PSVI");
/*      */         }
/*      */       }
/*  961 */       return null;
/*      */     }
/*      */ 
/*      */     AttributePSVI getAttributePSVIByName(String uri, String localname) {
/*  965 */       if (this.fAttributes != null) {
/*  966 */         Augmentations augs = this.fAttributes.getAugmentations(uri, localname);
/*  967 */         if (augs != null) {
/*  968 */           return (AttributePSVI)augs.getItem("ATTRIBUTE_PSVI");
/*      */         }
/*      */       }
/*  971 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHandlerImpl
 * JD-Core Version:    0.6.2
 */