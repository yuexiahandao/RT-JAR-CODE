/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.util.AttributesProxy;
/*     */ import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
/*     */ import com.sun.org.apache.xerces.internal.util.LocatorProxy;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import javax.xml.validation.TypeInfoProvider;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ final class JAXPValidatorComponent extends TeeXMLDocumentFilterImpl
/*     */   implements XMLComponent
/*     */ {
/*     */   private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   private final ValidatorHandler validator;
/* 103 */   private final XNI2SAX xni2sax = new XNI2SAX(null);
/* 104 */   private final SAX2XNI sax2xni = new SAX2XNI(null);
/*     */   private final TypeInfoProvider typeInfoProvider;
/*     */   private Augmentations fCurrentAug;
/*     */   private XMLAttributes fCurrentAttributes;
/*     */   private SymbolTable fSymbolTable;
/*     */   private XMLErrorReporter fErrorReporter;
/*     */   private XMLEntityResolver fEntityResolver;
/* 549 */   private static final TypeInfoProvider noInfoProvider = new TypeInfoProvider() {
/*     */     public TypeInfo getElementTypeInfo() {
/* 551 */       return null;
/*     */     }
/*     */     public TypeInfo getAttributeTypeInfo(int index) {
/* 554 */       return null;
/*     */     }
/*     */     public TypeInfo getAttributeTypeInfo(String attributeQName) {
/* 557 */       return null;
/*     */     }
/*     */     public TypeInfo getAttributeTypeInfo(String attributeUri, String attributeLocalName) {
/* 560 */       return null;
/*     */     }
/*     */     public boolean isIdAttribute(int index) {
/* 563 */       return false;
/*     */     }
/*     */     public boolean isSpecified(int index) {
/* 566 */       return false;
/*     */     }
/* 549 */   };
/*     */ 
/*     */   public JAXPValidatorComponent(ValidatorHandler validatorHandler)
/*     */   {
/* 133 */     this.validator = validatorHandler;
/* 134 */     TypeInfoProvider tip = validatorHandler.getTypeInfoProvider();
/* 135 */     if (tip == null) tip = noInfoProvider;
/* 136 */     this.typeInfoProvider = tip;
/*     */ 
/* 139 */     this.xni2sax.setContentHandler(this.validator);
/* 140 */     this.validator.setContentHandler(this.sax2xni);
/* 141 */     setSide(this.xni2sax);
/*     */ 
/* 144 */     this.validator.setErrorHandler(new ErrorHandlerProxy() {
/*     */       protected XMLErrorHandler getErrorHandler() {
/* 146 */         XMLErrorHandler handler = JAXPValidatorComponent.this.fErrorReporter.getErrorHandler();
/* 147 */         if (handler != null) return handler;
/* 148 */         return new ErrorHandlerWrapper(JAXPValidatorComponent.DraconianErrorHandler.getInstance());
/*     */       }
/*     */     });
/* 151 */     this.validator.setResourceResolver(new LSResourceResolver() {
/*     */       public LSInput resolveResource(String type, String ns, String publicId, String systemId, String baseUri) {
/* 153 */         if (JAXPValidatorComponent.this.fEntityResolver == null) return null; try
/*     */         {
/* 155 */           XMLInputSource is = JAXPValidatorComponent.this.fEntityResolver.resolveEntity(new XMLResourceIdentifierImpl(publicId, systemId, baseUri, null));
/*     */ 
/* 157 */           if (is == null) return null;
/*     */ 
/* 159 */           LSInput di = new DOMInputImpl();
/* 160 */           di.setBaseURI(is.getBaseSystemId());
/* 161 */           di.setByteStream(is.getByteStream());
/* 162 */           di.setCharacterStream(is.getCharacterStream());
/* 163 */           di.setEncoding(is.getEncoding());
/* 164 */           di.setPublicId(is.getPublicId());
/* 165 */           di.setSystemId(is.getSystemId());
/*     */ 
/* 167 */           return di;
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 171 */           throw new XNIException(e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException
/*     */   {
/* 179 */     this.fCurrentAttributes = attributes;
/* 180 */     this.fCurrentAug = augs;
/* 181 */     this.xni2sax.startElement(element, attributes, null);
/* 182 */     this.fCurrentAttributes = null;
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, Augmentations augs) throws XNIException {
/* 186 */     this.fCurrentAug = augs;
/* 187 */     this.xni2sax.endElement(element, null);
/*     */   }
/*     */ 
/*     */   public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 191 */     startElement(element, attributes, augs);
/* 192 */     endElement(element, augs);
/*     */   }
/*     */ 
/*     */   public void characters(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 199 */     this.fCurrentAug = augs;
/* 200 */     this.xni2sax.characters(text, null);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(XMLString text, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 206 */     this.fCurrentAug = augs;
/* 207 */     this.xni2sax.ignorableWhitespace(text, null);
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager) throws XMLConfigurationException
/*     */   {
/* 212 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 213 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*     */     try {
/* 215 */       this.fEntityResolver = ((XMLEntityResolver)componentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
/*     */     }
/*     */     catch (XMLConfigurationException e) {
/* 218 */       this.fEntityResolver = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateAttributes(Attributes atts)
/*     */   {
/* 502 */     int len = atts.getLength();
/* 503 */     for (int i = 0; i < len; i++) {
/* 504 */       String aqn = atts.getQName(i);
/* 505 */       int j = this.fCurrentAttributes.getIndex(aqn);
/* 506 */       String av = atts.getValue(i);
/* 507 */       if (j == -1)
/*     */       {
/* 511 */         int idx = aqn.indexOf(':');
/*     */         String prefix;
/*     */         String prefix;
/* 512 */         if (idx < 0)
/* 513 */           prefix = null;
/*     */         else {
/* 515 */           prefix = symbolize(aqn.substring(0, idx));
/*     */         }
/*     */ 
/* 518 */         j = this.fCurrentAttributes.addAttribute(new QName(prefix, symbolize(atts.getLocalName(i)), symbolize(aqn), symbolize(atts.getURI(i))), atts.getType(i), av);
/*     */       }
/* 527 */       else if (!av.equals(this.fCurrentAttributes.getValue(j)))
/*     */       {
/* 529 */         this.fCurrentAttributes.setValue(j, av);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String symbolize(String s)
/*     */   {
/* 542 */     return this.fSymbolTable.addSymbol(s);
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedFeatures()
/*     */   {
/* 578 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedProperties() {
/* 585 */     return new String[] { "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/symbol-table" };
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
/*     */   }
/*     */ 
/*     */   public Boolean getFeatureDefault(String featureId) {
/* 592 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getPropertyDefault(String propertyId) {
/* 596 */     return null;
/*     */   }
/*     */ 
/*     */   private static final class DraconianErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/* 469 */     private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();
/*     */ 
/*     */     public static DraconianErrorHandler getInstance()
/*     */     {
/* 476 */       return ERROR_HANDLER_INSTANCE;
/*     */     }
/*     */ 
/*     */     public void warning(SAXParseException e)
/*     */       throws SAXException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void error(SAXParseException e) throws SAXException
/*     */     {
/* 486 */       throw e;
/*     */     }
/*     */ 
/*     */     public void fatalError(SAXParseException e) throws SAXException
/*     */     {
/* 491 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class SAX2XNI extends DefaultHandler
/*     */   {
/* 237 */     private final Augmentations fAugmentations = new AugmentationsImpl();
/*     */ 
/* 243 */     private final QName fQName = new QName();
/*     */ 
/*     */     private SAX2XNI() {
/*     */     }
/*     */     public void characters(char[] ch, int start, int len) throws SAXException { try { handler().characters(new XMLString(ch, start, len), aug());
/*     */       } catch (XNIException e) {
/* 249 */         throw toSAXException(e);
/*     */       } }
/*     */ 
/*     */     public void ignorableWhitespace(char[] ch, int start, int len) throws SAXException
/*     */     {
/*     */       try {
/* 255 */         handler().ignorableWhitespace(new XMLString(ch, start, len), aug());
/*     */       } catch (XNIException e) {
/* 257 */         throw toSAXException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException {
/*     */       try {
/* 263 */         JAXPValidatorComponent.this.updateAttributes(atts);
/* 264 */         handler().startElement(toQName(uri, localName, qname), JAXPValidatorComponent.this.fCurrentAttributes, elementAug());
/*     */       } catch (XNIException e) {
/* 266 */         throw toSAXException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void endElement(String uri, String localName, String qname) throws SAXException {
/*     */       try {
/* 272 */         handler().endElement(toQName(uri, localName, qname), aug());
/*     */       } catch (XNIException e) {
/* 274 */         throw toSAXException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     private Augmentations elementAug() {
/* 279 */       Augmentations aug = aug();
/*     */ 
/* 281 */       return aug;
/*     */     }
/*     */ 
/*     */     private Augmentations aug()
/*     */     {
/* 290 */       if (JAXPValidatorComponent.this.fCurrentAug != null) {
/* 291 */         Augmentations r = JAXPValidatorComponent.this.fCurrentAug;
/* 292 */         JAXPValidatorComponent.this.fCurrentAug = null;
/* 293 */         return r;
/*     */       }
/* 295 */       this.fAugmentations.removeAllItems();
/* 296 */       return this.fAugmentations;
/*     */     }
/*     */ 
/*     */     private XMLDocumentHandler handler()
/*     */     {
/* 303 */       return JAXPValidatorComponent.this.getDocumentHandler();
/*     */     }
/*     */ 
/*     */     private SAXException toSAXException(XNIException xe)
/*     */     {
/* 311 */       Exception e = xe.getException();
/* 312 */       if (e == null) e = xe;
/* 313 */       if ((e instanceof SAXException)) return (SAXException)e;
/* 314 */       return new SAXException(e);
/*     */     }
/*     */ 
/*     */     private QName toQName(String uri, String localName, String qname)
/*     */     {
/* 323 */       String prefix = null;
/* 324 */       int idx = qname.indexOf(':');
/* 325 */       if (idx > 0) {
/* 326 */         prefix = JAXPValidatorComponent.this.symbolize(qname.substring(0, idx));
/*     */       }
/* 328 */       localName = JAXPValidatorComponent.this.symbolize(localName);
/* 329 */       qname = JAXPValidatorComponent.this.symbolize(qname);
/* 330 */       uri = JAXPValidatorComponent.this.symbolize(uri);
/*     */ 
/* 333 */       this.fQName.setValues(prefix, localName, qname, uri);
/* 334 */       return this.fQName;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class XNI2SAX extends DefaultXMLDocumentHandler
/*     */   {
/*     */     private ContentHandler fContentHandler;
/*     */     private String fVersion;
/*     */     protected NamespaceContext fNamespaceContext;
/* 360 */     private final AttributesProxy fAttributesProxy = new AttributesProxy(null);
/*     */ 
/*     */     private XNI2SAX() {  } 
/* 363 */     public void setContentHandler(ContentHandler handler) { this.fContentHandler = handler; }
/*     */ 
/*     */     public ContentHandler getContentHandler()
/*     */     {
/* 367 */       return this.fContentHandler;
/*     */     }
/*     */ 
/*     */     public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException
/*     */     {
/* 372 */       this.fVersion = version;
/*     */     }
/*     */ 
/*     */     public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
/* 376 */       this.fNamespaceContext = namespaceContext;
/* 377 */       this.fContentHandler.setDocumentLocator(new LocatorProxy(locator));
/*     */       try {
/* 379 */         this.fContentHandler.startDocument();
/*     */       } catch (SAXException e) {
/* 381 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void endDocument(Augmentations augs) throws XNIException {
/*     */       try {
/* 387 */         this.fContentHandler.endDocument();
/*     */       } catch (SAXException e) {
/* 389 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
/*     */       try {
/* 395 */         this.fContentHandler.processingInstruction(target, data.toString());
/*     */       } catch (SAXException e) {
/* 397 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException
/*     */     {
/*     */       try {
/* 404 */         int count = this.fNamespaceContext.getDeclaredPrefixCount();
/* 405 */         if (count > 0) {
/* 406 */           String prefix = null;
/* 407 */           String uri = null;
/* 408 */           for (int i = 0; i < count; i++) {
/* 409 */             prefix = this.fNamespaceContext.getDeclaredPrefixAt(i);
/* 410 */             uri = this.fNamespaceContext.getURI(prefix);
/* 411 */             this.fContentHandler.startPrefixMapping(prefix, uri == null ? "" : uri);
/*     */           }
/*     */         }
/*     */ 
/* 415 */         String uri = element.uri != null ? element.uri : "";
/* 416 */         String localpart = element.localpart;
/* 417 */         this.fAttributesProxy.setAttributes(attributes);
/* 418 */         this.fContentHandler.startElement(uri, localpart, element.rawname, this.fAttributesProxy);
/*     */       } catch (SAXException e) {
/* 420 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void endElement(QName element, Augmentations augs) throws XNIException {
/*     */       try {
/* 426 */         String uri = element.uri != null ? element.uri : "";
/* 427 */         String localpart = element.localpart;
/* 428 */         this.fContentHandler.endElement(uri, localpart, element.rawname);
/*     */ 
/* 431 */         int count = this.fNamespaceContext.getDeclaredPrefixCount();
/* 432 */         if (count > 0)
/* 433 */           for (int i = 0; i < count; i++)
/* 434 */             this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(i));
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 438 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
/* 443 */       startElement(element, attributes, augs);
/* 444 */       endElement(element, augs);
/*     */     }
/*     */ 
/*     */     public void characters(XMLString text, Augmentations augs) throws XNIException {
/*     */       try {
/* 449 */         this.fContentHandler.characters(text.ch, text.offset, text.length);
/*     */       } catch (SAXException e) {
/* 451 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
/*     */       try {
/* 457 */         this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
/*     */       } catch (SAXException e) {
/* 459 */         throw new XNIException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.JAXPValidatorComponent
 * JD-Core Version:    0.6.2
 */