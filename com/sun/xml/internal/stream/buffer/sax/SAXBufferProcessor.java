/*     */ package com.sun.xml.internal.stream.buffer.sax;
/*     */ 
/*     */ import com.sun.xml.internal.stream.buffer.AbstractProcessor;
/*     */ import com.sun.xml.internal.stream.buffer.AttributesHolder;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class SAXBufferProcessor extends AbstractProcessor
/*     */   implements XMLReader
/*     */ {
/*  55 */   protected EntityResolver _entityResolver = DEFAULT_LEXICAL_HANDLER;
/*     */ 
/*  60 */   protected DTDHandler _dtdHandler = DEFAULT_LEXICAL_HANDLER;
/*     */ 
/*  65 */   protected ContentHandler _contentHandler = DEFAULT_LEXICAL_HANDLER;
/*     */ 
/*  70 */   protected ErrorHandler _errorHandler = DEFAULT_LEXICAL_HANDLER;
/*     */ 
/*  75 */   protected LexicalHandler _lexicalHandler = DEFAULT_LEXICAL_HANDLER;
/*     */ 
/*  80 */   protected boolean _namespacePrefixesFeature = false;
/*     */ 
/*  82 */   protected AttributesHolder _attributes = new AttributesHolder();
/*     */ 
/*  84 */   protected String[] _namespacePrefixes = new String[16];
/*     */   protected int _namespacePrefixesIndex;
/*  87 */   protected int[] _namespaceAttributesStack = new int[16];
/*     */   protected int _namespaceAttributesStackIndex;
/* 671 */   private static final DefaultWithLexicalHandler DEFAULT_LEXICAL_HANDLER = new DefaultWithLexicalHandler();
/*     */ 
/*     */   public SAXBufferProcessor()
/*     */   {
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public SAXBufferProcessor(XMLStreamBuffer buffer)
/*     */   {
/*  98 */     setXMLStreamBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public SAXBufferProcessor(XMLStreamBuffer buffer, boolean produceFragmentEvent)
/*     */   {
/* 107 */     setXMLStreamBuffer(buffer, produceFragmentEvent);
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 112 */     if (name.equals("http://xml.org/sax/features/namespaces"))
/* 113 */       return true;
/* 114 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/* 115 */       return this._namespacePrefixesFeature;
/* 116 */     if (name.equals("http://xml.org/sax/features/external-general-entities"))
/* 117 */       return true;
/* 118 */     if (name.equals("http://xml.org/sax/features/external-parameter-entities"))
/* 119 */       return true;
/* 120 */     if (name.equals("http://xml.org/sax/features/string-interning")) {
/* 121 */       return this._stringInterningFeature;
/*     */     }
/* 123 */     throw new SAXNotRecognizedException("Feature not supported: " + name);
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 130 */     if (name.equals("http://xml.org/sax/features/namespaces")) {
/* 131 */       if (!value)
/* 132 */         throw new SAXNotSupportedException(name + ":" + value);
/*     */     }
/* 134 */     else if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/* 135 */       this._namespacePrefixesFeature = value;
/* 136 */     else if (!name.equals("http://xml.org/sax/features/external-general-entities"))
/*     */     {
/* 138 */       if (!name.equals("http://xml.org/sax/features/external-parameter-entities"))
/*     */       {
/* 140 */         if (name.equals("http://xml.org/sax/features/string-interning")) {
/* 141 */           if (value != this._stringInterningFeature)
/* 142 */             throw new SAXNotSupportedException(name + ":" + value);
/*     */         }
/*     */         else
/* 145 */           throw new SAXNotRecognizedException("Feature not supported: " + name);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 152 */     if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
/* 153 */       return getLexicalHandler();
/*     */     }
/* 155 */     throw new SAXNotRecognizedException("Property not recognized: " + name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 161 */     if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
/* 162 */       if ((value instanceof LexicalHandler))
/* 163 */         setLexicalHandler((LexicalHandler)value);
/*     */       else
/* 165 */         throw new SAXNotSupportedException("http://xml.org/sax/properties/lexical-handler");
/*     */     }
/*     */     else
/* 168 */       throw new SAXNotRecognizedException("Property not recognized: " + name);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */   {
/* 173 */     this._entityResolver = resolver;
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver() {
/* 177 */     return this._entityResolver;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler) {
/* 181 */     this._dtdHandler = handler;
/*     */   }
/*     */ 
/*     */   public DTDHandler getDTDHandler() {
/* 185 */     return this._dtdHandler;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler handler) {
/* 189 */     this._contentHandler = handler;
/*     */   }
/*     */ 
/*     */   public ContentHandler getContentHandler() {
/* 193 */     return this._contentHandler;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler) {
/* 197 */     this._errorHandler = handler;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getErrorHandler() {
/* 201 */     return this._errorHandler;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler handler) {
/* 205 */     this._lexicalHandler = handler;
/*     */   }
/*     */ 
/*     */   public LexicalHandler getLexicalHandler() {
/* 209 */     return this._lexicalHandler;
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input) throws IOException, SAXException
/*     */   {
/* 214 */     process();
/*     */   }
/*     */ 
/*     */   public void parse(String systemId) throws IOException, SAXException
/*     */   {
/* 219 */     process();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void process(XMLStreamBuffer buffer)
/*     */     throws SAXException
/*     */   {
/* 229 */     setXMLStreamBuffer(buffer);
/* 230 */     process();
/*     */   }
/*     */ 
/*     */   public final void process(XMLStreamBuffer buffer, boolean produceFragmentEvent)
/*     */     throws SAXException
/*     */   {
/* 241 */     setXMLStreamBuffer(buffer);
/* 242 */     process();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setXMLStreamBuffer(XMLStreamBuffer buffer)
/*     */   {
/* 252 */     setBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public void setXMLStreamBuffer(XMLStreamBuffer buffer, boolean produceFragmentEvent)
/*     */   {
/* 263 */     if ((!produceFragmentEvent) && (this._treeCount > 1))
/* 264 */       throw new IllegalStateException("Can't write a forest to a full XML infoset");
/* 265 */     setBuffer(buffer, produceFragmentEvent);
/*     */   }
/*     */ 
/*     */   public final void process()
/*     */     throws SAXException
/*     */   {
/* 284 */     if (!this._fragmentMode) {
/* 285 */       LocatorImpl nullLocator = new LocatorImpl();
/* 286 */       nullLocator.setSystemId(this._buffer.getSystemId());
/* 287 */       nullLocator.setLineNumber(-1);
/* 288 */       nullLocator.setColumnNumber(-1);
/* 289 */       this._contentHandler.setDocumentLocator(nullLocator);
/*     */ 
/* 291 */       this._contentHandler.startDocument();
/*     */     }
/*     */ 
/* 296 */     while (this._treeCount > 0) {
/* 297 */       int item = readEiiState();
/* 298 */       switch (item) {
/*     */       case 1:
/* 300 */         processDocument();
/* 301 */         this._treeCount -= 1;
/* 302 */         break;
/*     */       case 17:
/* 305 */         return;
/*     */       case 3:
/* 307 */         processElement(readStructureString(), readStructureString(), readStructureString());
/* 308 */         this._treeCount -= 1;
/* 309 */         break;
/*     */       case 4:
/* 312 */         String prefix = readStructureString();
/* 313 */         String uri = readStructureString();
/* 314 */         String localName = readStructureString();
/* 315 */         processElement(uri, localName, getQName(prefix, localName));
/* 316 */         this._treeCount -= 1;
/* 317 */         break;
/*     */       case 5:
/* 320 */         String uri = readStructureString();
/* 321 */         String localName = readStructureString();
/* 322 */         processElement(uri, localName, localName);
/* 323 */         this._treeCount -= 1;
/* 324 */         break;
/*     */       case 6:
/* 328 */         String localName = readStructureString();
/* 329 */         processElement("", localName, localName);
/* 330 */         this._treeCount -= 1;
/* 331 */         break;
/*     */       case 12:
/* 334 */         processCommentAsCharArraySmall();
/* 335 */         break;
/*     */       case 13:
/* 337 */         processCommentAsCharArrayMedium();
/* 338 */         break;
/*     */       case 14:
/* 340 */         processCommentAsCharArrayCopy();
/* 341 */         break;
/*     */       case 15:
/* 343 */         processComment(readContentString());
/* 344 */         break;
/*     */       case 16:
/* 346 */         processProcessingInstruction(readStructureString(), readStructureString());
/* 347 */         break;
/*     */       case 2:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 349 */         throw reportFatalError("Illegal state for DIIs: " + item);
/*     */       }
/*     */     }
/*     */ 
/* 353 */     if (!this._fragmentMode)
/* 354 */       this._contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   private void processCommentAsCharArraySmall() throws SAXException {
/* 358 */     int length = readStructure();
/* 359 */     int start = readContentCharactersBuffer(length);
/* 360 */     processComment(this._contentCharactersBuffer, start, length);
/*     */   }
/*     */ 
/*     */   private SAXParseException reportFatalError(String msg)
/*     */     throws SAXException
/*     */   {
/* 369 */     SAXParseException spe = new SAXParseException(msg, null);
/* 370 */     if (this._errorHandler != null)
/* 371 */       this._errorHandler.fatalError(spe);
/* 372 */     return spe;
/*     */   }
/*     */ 
/*     */   private void processDocument() throws SAXException {
/*     */     while (true) {
/* 377 */       int item = readEiiState();
/* 378 */       switch (item) {
/*     */       case 3:
/* 380 */         processElement(readStructureString(), readStructureString(), readStructureString());
/* 381 */         break;
/*     */       case 4:
/* 384 */         String prefix = readStructureString();
/* 385 */         String uri = readStructureString();
/* 386 */         String localName = readStructureString();
/* 387 */         processElement(uri, localName, getQName(prefix, localName));
/* 388 */         break;
/*     */       case 5:
/* 391 */         String uri = readStructureString();
/* 392 */         String localName = readStructureString();
/* 393 */         processElement(uri, localName, localName);
/* 394 */         break;
/*     */       case 6:
/* 398 */         String localName = readStructureString();
/* 399 */         processElement("", localName, localName);
/* 400 */         break;
/*     */       case 12:
/* 403 */         processCommentAsCharArraySmall();
/* 404 */         break;
/*     */       case 13:
/* 406 */         processCommentAsCharArrayMedium();
/* 407 */         break;
/*     */       case 14:
/* 409 */         processCommentAsCharArrayCopy();
/* 410 */         break;
/*     */       case 15:
/* 412 */         processComment(readContentString());
/* 413 */         break;
/*     */       case 16:
/* 415 */         processProcessingInstruction(readStructureString(), readStructureString());
/* 416 */         break;
/*     */       case 17:
/* 418 */         return;
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 420 */         throw reportFatalError("Illegal state for child of DII: " + item);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void processElement(String uri, String localName, String qName) throws SAXException {
/* 426 */     boolean hasAttributes = false;
/* 427 */     boolean hasNamespaceAttributes = false;
/* 428 */     int item = peekStructure();
/* 429 */     if ((item & 0xF0) == 64) {
/* 430 */       hasNamespaceAttributes = true;
/* 431 */       item = processNamespaceAttributes(item);
/*     */     }
/* 433 */     if ((item & 0xF0) == 48) {
/* 434 */       hasAttributes = true;
/* 435 */       processAttributes(item);
/*     */     }
/*     */ 
/* 438 */     this._contentHandler.startElement(uri, localName, qName, this._attributes);
/*     */ 
/* 440 */     if (hasAttributes) {
/* 441 */       this._attributes.clear();
/*     */     }
/*     */     do
/*     */     {
/* 445 */       item = readEiiState();
/* 446 */       switch (item) {
/*     */       case 3:
/* 448 */         processElement(readStructureString(), readStructureString(), readStructureString());
/* 449 */         break;
/*     */       case 4:
/* 452 */         String p = readStructureString();
/* 453 */         String u = readStructureString();
/* 454 */         String ln = readStructureString();
/* 455 */         processElement(u, ln, getQName(p, ln));
/* 456 */         break;
/*     */       case 5:
/* 459 */         String u = readStructureString();
/* 460 */         String ln = readStructureString();
/* 461 */         processElement(u, ln, ln);
/* 462 */         break;
/*     */       case 6:
/* 465 */         String ln = readStructureString();
/* 466 */         processElement("", ln, ln);
/* 467 */         break;
/*     */       case 7:
/* 471 */         int length = readStructure();
/* 472 */         int start = readContentCharactersBuffer(length);
/* 473 */         this._contentHandler.characters(this._contentCharactersBuffer, start, length);
/* 474 */         break;
/*     */       case 8:
/* 478 */         int length = readStructure16();
/* 479 */         int start = readContentCharactersBuffer(length);
/* 480 */         this._contentHandler.characters(this._contentCharactersBuffer, start, length);
/* 481 */         break;
/*     */       case 9:
/* 485 */         char[] ch = readContentCharactersCopy();
/*     */ 
/* 487 */         this._contentHandler.characters(ch, 0, ch.length);
/* 488 */         break;
/*     */       case 10:
/* 492 */         String s = readContentString();
/* 493 */         this._contentHandler.characters(s.toCharArray(), 0, s.length());
/* 494 */         break;
/*     */       case 11:
/* 498 */         CharSequence c = (CharSequence)readContentObject();
/* 499 */         String s = c.toString();
/* 500 */         this._contentHandler.characters(s.toCharArray(), 0, s.length());
/* 501 */         break;
/*     */       case 12:
/* 504 */         processCommentAsCharArraySmall();
/* 505 */         break;
/*     */       case 13:
/* 507 */         processCommentAsCharArrayMedium();
/* 508 */         break;
/*     */       case 14:
/* 510 */         processCommentAsCharArrayCopy();
/* 511 */         break;
/*     */       case 104:
/* 513 */         processComment(readContentString());
/* 514 */         break;
/*     */       case 16:
/* 516 */         processProcessingInstruction(readStructureString(), readStructureString());
/* 517 */         break;
/*     */       case 17:
/* 519 */         break;
/*     */       default:
/* 521 */         throw reportFatalError("Illegal state for child of EII: " + item);
/*     */       }
/*     */     }
/* 523 */     while (item != 17);
/*     */ 
/* 525 */     this._contentHandler.endElement(uri, localName, qName);
/*     */ 
/* 527 */     if (hasNamespaceAttributes)
/* 528 */       processEndPrefixMapping();
/*     */   }
/*     */ 
/*     */   private void processCommentAsCharArrayCopy() throws SAXException
/*     */   {
/* 533 */     char[] ch = readContentCharactersCopy();
/* 534 */     processComment(ch, 0, ch.length);
/*     */   }
/*     */ 
/*     */   private void processCommentAsCharArrayMedium() throws SAXException {
/* 538 */     int length = readStructure16();
/* 539 */     int start = readContentCharactersBuffer(length);
/* 540 */     processComment(this._contentCharactersBuffer, start, length);
/*     */   }
/*     */ 
/*     */   private void processEndPrefixMapping() throws SAXException {
/* 544 */     int end = this._namespaceAttributesStack[(--this._namespaceAttributesStackIndex)];
/* 545 */     int start = this._namespaceAttributesStackIndex > 0 ? this._namespaceAttributesStack[this._namespaceAttributesStackIndex] : 0;
/*     */ 
/* 547 */     for (int i = end - 1; i >= start; i--) {
/* 548 */       this._contentHandler.endPrefixMapping(this._namespacePrefixes[i]);
/*     */     }
/* 550 */     this._namespacePrefixesIndex = start;
/*     */   }
/*     */ 
/*     */   private int processNamespaceAttributes(int item) throws SAXException {
/*     */     do {
/* 555 */       switch (getNIIState(item))
/*     */       {
/*     */       case 1:
/* 558 */         processNamespaceAttribute("", "");
/* 559 */         break;
/*     */       case 2:
/* 562 */         processNamespaceAttribute(readStructureString(), "");
/* 563 */         break;
/*     */       case 3:
/* 566 */         processNamespaceAttribute(readStructureString(), readStructureString());
/* 567 */         break;
/*     */       case 4:
/* 570 */         processNamespaceAttribute("", readStructureString());
/* 571 */         break;
/*     */       default:
/* 573 */         throw reportFatalError("Illegal state: " + item);
/*     */       }
/* 575 */       readStructure();
/*     */ 
/* 577 */       item = peekStructure();
/* 578 */     }while ((item & 0xF0) == 64);
/*     */ 
/* 581 */     cacheNamespacePrefixIndex();
/*     */ 
/* 583 */     return item;
/*     */   }
/*     */ 
/*     */   private void processAttributes(int item) throws SAXException {
/*     */     do {
/* 588 */       switch (getAIIState(item)) {
/*     */       case 1:
/* 590 */         this._attributes.addAttributeWithQName(readStructureString(), readStructureString(), readStructureString(), readStructureString(), readContentString());
/* 591 */         break;
/*     */       case 2:
/* 594 */         String p = readStructureString();
/* 595 */         String u = readStructureString();
/* 596 */         String ln = readStructureString();
/* 597 */         this._attributes.addAttributeWithQName(u, ln, getQName(p, ln), readStructureString(), readContentString());
/* 598 */         break;
/*     */       case 3:
/* 601 */         String u = readStructureString();
/* 602 */         String ln = readStructureString();
/* 603 */         this._attributes.addAttributeWithQName(u, ln, ln, readStructureString(), readContentString());
/* 604 */         break;
/*     */       case 4:
/* 607 */         String ln = readStructureString();
/* 608 */         this._attributes.addAttributeWithQName("", ln, ln, readStructureString(), readContentString());
/* 609 */         break;
/*     */       default:
/* 612 */         throw reportFatalError("Illegal state: " + item);
/*     */       }
/* 614 */       readStructure();
/*     */ 
/* 616 */       item = peekStructure();
/* 617 */     }while ((item & 0xF0) == 48);
/*     */   }
/*     */ 
/*     */   private void processNamespaceAttribute(String prefix, String uri) throws SAXException {
/* 621 */     this._contentHandler.startPrefixMapping(prefix, uri);
/*     */ 
/* 623 */     if (this._namespacePrefixesFeature)
/*     */     {
/* 625 */       if (prefix != "") {
/* 626 */         this._attributes.addAttributeWithQName("http://www.w3.org/2000/xmlns/", prefix, getQName("xmlns", prefix), "CDATA", uri);
/*     */       }
/*     */       else
/*     */       {
/* 630 */         this._attributes.addAttributeWithQName("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", uri);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 636 */     cacheNamespacePrefix(prefix);
/*     */   }
/*     */ 
/*     */   private void cacheNamespacePrefix(String prefix) {
/* 640 */     if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
/* 641 */       String[] namespaceAttributes = new String[this._namespacePrefixesIndex * 3 / 2 + 1];
/* 642 */       System.arraycopy(this._namespacePrefixes, 0, namespaceAttributes, 0, this._namespacePrefixesIndex);
/* 643 */       this._namespacePrefixes = namespaceAttributes;
/*     */     }
/*     */ 
/* 646 */     this._namespacePrefixes[(this._namespacePrefixesIndex++)] = prefix;
/*     */   }
/*     */ 
/*     */   private void cacheNamespacePrefixIndex() {
/* 650 */     if (this._namespaceAttributesStackIndex == this._namespaceAttributesStack.length) {
/* 651 */       int[] namespaceAttributesStack = new int[this._namespaceAttributesStackIndex * 3 / 2 + 1];
/* 652 */       System.arraycopy(this._namespaceAttributesStack, 0, namespaceAttributesStack, 0, this._namespaceAttributesStackIndex);
/* 653 */       this._namespaceAttributesStack = namespaceAttributesStack;
/*     */     }
/*     */ 
/* 656 */     this._namespaceAttributesStack[(this._namespaceAttributesStackIndex++)] = this._namespacePrefixesIndex;
/*     */   }
/*     */ 
/*     */   private void processComment(String s) throws SAXException {
/* 660 */     processComment(s.toCharArray(), 0, s.length());
/*     */   }
/*     */ 
/*     */   private void processComment(char[] ch, int start, int length) throws SAXException {
/* 664 */     this._lexicalHandler.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   private void processProcessingInstruction(String target, String data) throws SAXException {
/* 668 */     this._contentHandler.processingInstruction(target, data);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor
 * JD-Core Version:    0.6.2
 */