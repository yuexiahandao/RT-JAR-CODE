/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
/*     */ import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.CDATASection;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Entity;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class DOMValidatorHelper
/*     */   implements ValidatorHelper, EntityState
/*     */ {
/*     */   private static final int CHUNK_SIZE = 1024;
/*     */   private static final int CHUNK_MASK = 1023;
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
/*     */   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*     */   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   private XMLErrorReporter fErrorReporter;
/*     */   private NamespaceSupport fNamespaceContext;
/* 113 */   private DOMNamespaceContext fDOMNamespaceContext = new DOMNamespaceContext();
/*     */   private XMLSchemaValidator fSchemaValidator;
/*     */   private SymbolTable fSymbolTable;
/*     */   private ValidationManager fValidationManager;
/*     */   private XMLSchemaValidatorComponentManager fComponentManager;
/* 128 */   private final SimpleLocator fXMLLocator = new SimpleLocator(null, null, -1, -1, -1);
/*     */   private DOMDocumentHandler fDOMValidatorHandler;
/* 134 */   private final DOMResultAugmentor fDOMResultAugmentor = new DOMResultAugmentor(this);
/*     */ 
/* 137 */   private final DOMResultBuilder fDOMResultBuilder = new DOMResultBuilder();
/*     */ 
/* 140 */   private NamedNodeMap fEntities = null;
/*     */ 
/* 143 */   private char[] fCharBuffer = new char[1024];
/*     */   private Node fRoot;
/*     */   private Node fCurrentElement;
/* 152 */   final QName fElementQName = new QName();
/* 153 */   final QName fAttributeQName = new QName();
/* 154 */   final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
/* 155 */   final XMLString fTempString = new XMLString();
/*     */ 
/*     */   public DOMValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
/* 158 */     this.fComponentManager = componentManager;
/* 159 */     this.fErrorReporter = ((XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/* 160 */     this.fNamespaceContext = ((NamespaceSupport)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
/* 161 */     this.fSchemaValidator = ((XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
/* 162 */     this.fSymbolTable = ((SymbolTable)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 163 */     this.fValidationManager = ((ValidationManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
/*     */   }
/*     */ 
/*     */   public void validate(Source source, Result result)
/*     */     throws SAXException, IOException
/*     */   {
/* 172 */     if (((result instanceof DOMResult)) || (result == null)) {
/* 173 */       DOMSource domSource = (DOMSource)source;
/* 174 */       DOMResult domResult = (DOMResult)result;
/* 175 */       Node node = domSource.getNode();
/* 176 */       this.fRoot = node;
/* 177 */       if (node != null) {
/* 178 */         this.fComponentManager.reset();
/* 179 */         this.fValidationManager.setEntityState(this);
/* 180 */         this.fDOMNamespaceContext.reset();
/* 181 */         String systemId = domSource.getSystemId();
/* 182 */         this.fXMLLocator.setLiteralSystemId(systemId);
/* 183 */         this.fXMLLocator.setExpandedSystemId(systemId);
/* 184 */         this.fErrorReporter.setDocumentLocator(this.fXMLLocator);
/*     */         try
/*     */         {
/* 187 */           setupEntityMap(node.getNodeType() == 9 ? (Document)node : node.getOwnerDocument());
/* 188 */           setupDOMResultHandler(domSource, domResult);
/* 189 */           this.fSchemaValidator.startDocument(this.fXMLLocator, null, this.fDOMNamespaceContext, null);
/* 190 */           validate(node);
/* 191 */           this.fSchemaValidator.endDocument(null);
/*     */         }
/*     */         catch (XMLParseException e) {
/* 194 */           throw Util.toSAXParseException(e);
/*     */         }
/*     */         catch (XNIException e) {
/* 197 */           throw Util.toSAXException(e);
/*     */         }
/*     */         finally
/*     */         {
/* 201 */           this.fRoot = null;
/*     */ 
/* 203 */           this.fEntities = null;
/* 204 */           if (this.fDOMValidatorHandler != null) {
/* 205 */             this.fDOMValidatorHandler.setDOMResult(null);
/*     */           }
/*     */         }
/*     */       }
/* 209 */       return;
/*     */     }
/* 211 */     throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { source.getClass().getName(), result.getClass().getName() }));
/*     */   }
/*     */ 
/*     */   public boolean isEntityDeclared(String name)
/*     */   {
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEntityUnparsed(String name) {
/* 225 */     if (this.fEntities != null) {
/* 226 */       Entity entity = (Entity)this.fEntities.getNamedItem(name);
/* 227 */       if (entity != null) {
/* 228 */         return entity.getNotationName() != null;
/*     */       }
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */   private void validate(Node node)
/*     */   {
/* 240 */     Node top = node;
/*     */ 
/* 243 */     while (node != null) {
/* 244 */       beginNode(node);
/* 245 */       Node next = node.getFirstChild();
/* 246 */       while (next == null) {
/* 247 */         finishNode(node);
/* 248 */         if (top != node)
/*     */         {
/* 251 */           next = node.getNextSibling();
/* 252 */           if (next == null) {
/* 253 */             node = node.getParentNode();
/* 254 */             if ((node == null) || (top == node)) {
/* 255 */               if (node != null) {
/* 256 */                 finishNode(node);
/*     */               }
/* 258 */               next = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 263 */       node = next;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void beginNode(Node node)
/*     */   {
/* 269 */     switch (node.getNodeType()) {
/*     */     case 1:
/* 271 */       this.fCurrentElement = node;
/*     */ 
/* 273 */       this.fNamespaceContext.pushContext();
/*     */ 
/* 275 */       fillQName(this.fElementQName, node);
/* 276 */       processAttributes(node.getAttributes());
/* 277 */       this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, null);
/* 278 */       break;
/*     */     case 3:
/* 280 */       if (this.fDOMValidatorHandler != null) {
/* 281 */         this.fDOMValidatorHandler.setIgnoringCharacters(true);
/* 282 */         sendCharactersToValidator(node.getNodeValue());
/* 283 */         this.fDOMValidatorHandler.setIgnoringCharacters(false);
/* 284 */         this.fDOMValidatorHandler.characters((Text)node);
/*     */       }
/*     */       else {
/* 287 */         sendCharactersToValidator(node.getNodeValue());
/*     */       }
/* 289 */       break;
/*     */     case 4:
/* 291 */       if (this.fDOMValidatorHandler != null) {
/* 292 */         this.fDOMValidatorHandler.setIgnoringCharacters(true);
/* 293 */         this.fSchemaValidator.startCDATA(null);
/* 294 */         sendCharactersToValidator(node.getNodeValue());
/* 295 */         this.fSchemaValidator.endCDATA(null);
/* 296 */         this.fDOMValidatorHandler.setIgnoringCharacters(false);
/* 297 */         this.fDOMValidatorHandler.cdata((CDATASection)node);
/*     */       }
/*     */       else {
/* 300 */         this.fSchemaValidator.startCDATA(null);
/* 301 */         sendCharactersToValidator(node.getNodeValue());
/* 302 */         this.fSchemaValidator.endCDATA(null);
/*     */       }
/* 304 */       break;
/*     */     case 7:
/* 310 */       if (this.fDOMValidatorHandler != null)
/* 311 */         this.fDOMValidatorHandler.processingInstruction((ProcessingInstruction)node); break;
/*     */     case 8:
/* 319 */       if (this.fDOMValidatorHandler != null)
/* 320 */         this.fDOMValidatorHandler.comment((Comment)node); break;
/*     */     case 10:
/* 327 */       if (this.fDOMValidatorHandler != null)
/* 328 */         this.fDOMValidatorHandler.doctypeDecl((DocumentType)node); break;
/*     */     case 2:
/*     */     case 5:
/*     */     case 6:
/*     */     case 9:
/*     */     }
/*     */   }
/*     */ 
/*     */   private void finishNode(Node node)
/*     */   {
/* 338 */     if (node.getNodeType() == 1) {
/* 339 */       this.fCurrentElement = node;
/*     */ 
/* 341 */       fillQName(this.fElementQName, node);
/* 342 */       this.fSchemaValidator.endElement(this.fElementQName, null);
/*     */ 
/* 344 */       this.fNamespaceContext.popContext();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setupEntityMap(Document doc)
/*     */   {
/* 354 */     if (doc != null) {
/* 355 */       DocumentType docType = doc.getDoctype();
/* 356 */       if (docType != null) {
/* 357 */         this.fEntities = docType.getEntities();
/* 358 */         return;
/*     */       }
/*     */     }
/* 361 */     this.fEntities = null;
/*     */   }
/*     */ 
/*     */   private void setupDOMResultHandler(DOMSource source, DOMResult result)
/*     */     throws SAXException
/*     */   {
/* 369 */     if (result == null) {
/* 370 */       this.fDOMValidatorHandler = null;
/* 371 */       this.fSchemaValidator.setDocumentHandler(null);
/* 372 */       return;
/*     */     }
/* 374 */     Node nodeResult = result.getNode();
/*     */ 
/* 377 */     if (source.getNode() == nodeResult) {
/* 378 */       this.fDOMValidatorHandler = this.fDOMResultAugmentor;
/* 379 */       this.fDOMResultAugmentor.setDOMResult(result);
/* 380 */       this.fSchemaValidator.setDocumentHandler(this.fDOMResultAugmentor);
/* 381 */       return;
/*     */     }
/* 383 */     if (result.getNode() == null) {
/*     */       try {
/* 385 */         DocumentBuilderFactory factory = this.fComponentManager.getFeature("http://www.oracle.com/feature/use-service-mechanism") ? DocumentBuilderFactory.newInstance() : new DocumentBuilderFactoryImpl();
/*     */ 
/* 387 */         factory.setNamespaceAware(true);
/* 388 */         DocumentBuilder builder = factory.newDocumentBuilder();
/* 389 */         result.setNode(builder.newDocument());
/*     */       }
/*     */       catch (ParserConfigurationException e) {
/* 392 */         throw new SAXException(e);
/*     */       }
/*     */     }
/* 395 */     this.fDOMValidatorHandler = this.fDOMResultBuilder;
/* 396 */     this.fDOMResultBuilder.setDOMResult(result);
/* 397 */     this.fSchemaValidator.setDocumentHandler(this.fDOMResultBuilder);
/*     */   }
/*     */ 
/*     */   private void fillQName(QName toFill, Node node) {
/* 401 */     String prefix = node.getPrefix();
/* 402 */     String localName = node.getLocalName();
/* 403 */     String rawName = node.getNodeName();
/* 404 */     String namespace = node.getNamespaceURI();
/*     */ 
/* 406 */     toFill.uri = ((namespace != null) && (namespace.length() > 0) ? this.fSymbolTable.addSymbol(namespace) : null);
/* 407 */     toFill.rawname = (rawName != null ? this.fSymbolTable.addSymbol(rawName) : XMLSymbols.EMPTY_STRING);
/*     */ 
/* 410 */     if (localName == null) {
/* 411 */       int k = rawName.indexOf(':');
/* 412 */       if (k > 0) {
/* 413 */         toFill.prefix = this.fSymbolTable.addSymbol(rawName.substring(0, k));
/* 414 */         toFill.localpart = this.fSymbolTable.addSymbol(rawName.substring(k + 1));
/*     */       }
/*     */       else {
/* 417 */         toFill.prefix = XMLSymbols.EMPTY_STRING;
/* 418 */         toFill.localpart = toFill.rawname;
/*     */       }
/*     */     }
/*     */     else {
/* 422 */       toFill.prefix = (prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING);
/* 423 */       toFill.localpart = (localName != null ? this.fSymbolTable.addSymbol(localName) : XMLSymbols.EMPTY_STRING);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processAttributes(NamedNodeMap attrMap) {
/* 428 */     int attrCount = attrMap.getLength();
/* 429 */     this.fAttributes.removeAllAttributes();
/* 430 */     for (int i = 0; i < attrCount; i++) {
/* 431 */       Attr attr = (Attr)attrMap.item(i);
/* 432 */       String value = attr.getValue();
/* 433 */       if (value == null) {
/* 434 */         value = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 436 */       fillQName(this.fAttributeQName, attr);
/*     */ 
/* 438 */       this.fAttributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, value);
/* 439 */       this.fAttributes.setSpecified(i, attr.getSpecified());
/*     */ 
/* 443 */       if (this.fAttributeQName.uri == NamespaceContext.XMLNS_URI)
/*     */       {
/* 445 */         if (this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 446 */           this.fNamespaceContext.declarePrefix(this.fAttributeQName.localpart, value.length() != 0 ? this.fSymbolTable.addSymbol(value) : null);
/*     */         }
/*     */         else
/* 449 */           this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, value.length() != 0 ? this.fSymbolTable.addSymbol(value) : null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void sendCharactersToValidator(String str)
/*     */   {
/* 456 */     if (str != null) {
/* 457 */       int length = str.length();
/* 458 */       int remainder = length & 0x3FF;
/* 459 */       if (remainder > 0) {
/* 460 */         str.getChars(0, remainder, this.fCharBuffer, 0);
/* 461 */         this.fTempString.setValues(this.fCharBuffer, 0, remainder);
/* 462 */         this.fSchemaValidator.characters(this.fTempString, null);
/*     */       }
/* 464 */       int i = remainder;
/* 465 */       while (i < length) {
/* 466 */         i += 1024; str.getChars(i, i, this.fCharBuffer, 0);
/* 467 */         this.fTempString.setValues(this.fCharBuffer, 0, 1024);
/* 468 */         this.fSchemaValidator.characters(this.fTempString, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Node getCurrentElement() {
/* 474 */     return this.fCurrentElement;
/*     */   }
/*     */ 
/*     */   final class DOMNamespaceContext
/*     */     implements NamespaceContext
/*     */   {
/* 491 */     protected String[] fNamespace = new String[32];
/*     */ 
/* 494 */     protected int fNamespaceSize = 0;
/*     */ 
/* 500 */     protected boolean fDOMContextBuilt = false;
/*     */ 
/*     */     DOMNamespaceContext()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void pushContext() {
/* 507 */       DOMValidatorHelper.this.fNamespaceContext.pushContext();
/*     */     }
/*     */ 
/*     */     public void popContext() {
/* 511 */       DOMValidatorHelper.this.fNamespaceContext.popContext();
/*     */     }
/*     */ 
/*     */     public boolean declarePrefix(String prefix, String uri) {
/* 515 */       return DOMValidatorHelper.this.fNamespaceContext.declarePrefix(prefix, uri);
/*     */     }
/*     */ 
/*     */     public String getURI(String prefix) {
/* 519 */       String uri = DOMValidatorHelper.this.fNamespaceContext.getURI(prefix);
/* 520 */       if (uri == null) {
/* 521 */         if (!this.fDOMContextBuilt) {
/* 522 */           fillNamespaceContext();
/* 523 */           this.fDOMContextBuilt = true;
/*     */         }
/* 525 */         if ((this.fNamespaceSize > 0) && (!DOMValidatorHelper.this.fNamespaceContext.containsPrefix(prefix)))
/*     */         {
/* 527 */           uri = getURI0(prefix);
/*     */         }
/*     */       }
/* 530 */       return uri;
/*     */     }
/*     */ 
/*     */     public String getPrefix(String uri) {
/* 534 */       return DOMValidatorHelper.this.fNamespaceContext.getPrefix(uri);
/*     */     }
/*     */ 
/*     */     public int getDeclaredPrefixCount() {
/* 538 */       return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixCount();
/*     */     }
/*     */ 
/*     */     public String getDeclaredPrefixAt(int index) {
/* 542 */       return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixAt(index);
/*     */     }
/*     */ 
/*     */     public Enumeration getAllPrefixes() {
/* 546 */       return DOMValidatorHelper.this.fNamespaceContext.getAllPrefixes();
/*     */     }
/*     */ 
/*     */     public void reset() {
/* 550 */       this.fDOMContextBuilt = false;
/* 551 */       this.fNamespaceSize = 0;
/*     */     }
/*     */ 
/*     */     private void fillNamespaceContext() {
/* 555 */       if (DOMValidatorHelper.this.fRoot != null) {
/* 556 */         Node currentNode = DOMValidatorHelper.this.fRoot.getParentNode();
/* 557 */         while (currentNode != null) {
/* 558 */           if (1 == currentNode.getNodeType()) {
/* 559 */             NamedNodeMap attributes = currentNode.getAttributes();
/* 560 */             int attrCount = attributes.getLength();
/* 561 */             for (int i = 0; i < attrCount; i++) {
/* 562 */               Attr attr = (Attr)attributes.item(i);
/* 563 */               String value = attr.getValue();
/* 564 */               if (value == null) {
/* 565 */                 value = XMLSymbols.EMPTY_STRING;
/*     */               }
/* 567 */               DOMValidatorHelper.this.fillQName(DOMValidatorHelper.this.fAttributeQName, attr);
/*     */ 
/* 571 */               if (DOMValidatorHelper.this.fAttributeQName.uri == NamespaceContext.XMLNS_URI)
/*     */               {
/* 573 */                 if (DOMValidatorHelper.this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
/* 574 */                   declarePrefix0(DOMValidatorHelper.this.fAttributeQName.localpart, value.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(value) : null);
/*     */                 }
/*     */                 else {
/* 577 */                   declarePrefix0(XMLSymbols.EMPTY_STRING, value.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(value) : null);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 583 */           currentNode = currentNode.getParentNode();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void declarePrefix0(String prefix, String uri)
/*     */     {
/* 590 */       if (this.fNamespaceSize == this.fNamespace.length) {
/* 591 */         String[] namespacearray = new String[this.fNamespaceSize * 2];
/* 592 */         System.arraycopy(this.fNamespace, 0, namespacearray, 0, this.fNamespaceSize);
/* 593 */         this.fNamespace = namespacearray;
/*     */       }
/*     */ 
/* 597 */       this.fNamespace[(this.fNamespaceSize++)] = prefix;
/* 598 */       this.fNamespace[(this.fNamespaceSize++)] = uri;
/*     */     }
/*     */ 
/*     */     private String getURI0(String prefix)
/*     */     {
/* 603 */       for (int i = 0; i < this.fNamespaceSize; i += 2) {
/* 604 */         if (this.fNamespace[i] == prefix) {
/* 605 */           return this.fNamespace[(i + 1)];
/*     */         }
/*     */       }
/*     */ 
/* 609 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.DOMValidatorHelper
 * JD-Core Version:    0.6.2
 */