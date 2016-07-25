/*     */ package com.sun.xml.internal.fastinfoset.stax;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.Encoder;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap.Entry;
/*     */ import com.sun.xml.internal.fastinfoset.util.NamespaceContextImplementation;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.EmptyStackException;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class StAXDocumentSerializer extends Encoder
/*     */   implements XMLStreamWriter, LowLevelFastInfosetStreamWriter
/*     */ {
/*     */   protected StAXManager _manager;
/*     */   protected String _encoding;
/*     */   protected String _currentLocalName;
/*     */   protected String _currentUri;
/*     */   protected String _currentPrefix;
/*  78 */   protected boolean _inStartElement = false;
/*     */ 
/*  83 */   protected boolean _isEmptyElement = false;
/*     */ 
/*  88 */   protected String[] _attributesArray = new String[64];
/*  89 */   protected int _attributesArrayIndex = 0;
/*     */ 
/*  91 */   protected boolean[] _nsSupportContextStack = new boolean[32];
/*  92 */   protected int _stackCount = -1;
/*     */ 
/*  97 */   protected NamespaceContextImplementation _nsContext = new NamespaceContextImplementation();
/*     */ 
/* 103 */   protected String[] _namespacesArray = new String[16];
/* 104 */   protected int _namespacesArrayIndex = 0;
/*     */ 
/*     */   public StAXDocumentSerializer() {
/* 107 */     super(true);
/* 108 */     this._manager = new StAXManager(2);
/*     */   }
/*     */ 
/*     */   public StAXDocumentSerializer(OutputStream outputStream) {
/* 112 */     super(true);
/* 113 */     setOutputStream(outputStream);
/* 114 */     this._manager = new StAXManager(2);
/*     */   }
/*     */ 
/*     */   public StAXDocumentSerializer(OutputStream outputStream, StAXManager manager) {
/* 118 */     super(true);
/* 119 */     setOutputStream(outputStream);
/* 120 */     this._manager = manager;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 124 */     super.reset();
/*     */ 
/* 126 */     this._attributesArrayIndex = 0;
/* 127 */     this._namespacesArrayIndex = 0;
/*     */ 
/* 129 */     this._nsContext.reset();
/* 130 */     this._stackCount = -1;
/*     */ 
/* 132 */     this._currentUri = (this._currentPrefix = null);
/* 133 */     this._currentLocalName = null;
/*     */ 
/* 135 */     this._inStartElement = (this._isEmptyElement = 0);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument()
/*     */     throws XMLStreamException
/*     */   {
/* 141 */     writeStartDocument("finf", "1.0");
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 145 */     writeStartDocument("finf", version);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version)
/*     */     throws XMLStreamException
/*     */   {
/* 151 */     reset();
/*     */     try
/*     */     {
/* 154 */       encodeHeader(false);
/* 155 */       encodeInitialVocabulary();
/*     */     } catch (IOException e) {
/* 157 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeEndDocument()
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 166 */       for (; this._stackCount >= 0; this._stackCount -= 1) {
/* 167 */         writeEndElement();
/*     */       }
/*     */ 
/* 170 */       encodeDocumentTermination();
/*     */     }
/*     */     catch (IOException e) {
/* 173 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException {
/* 178 */     reset();
/*     */   }
/*     */ 
/*     */   public void flush() throws XMLStreamException {
/*     */     try {
/* 183 */       this._s.flush();
/*     */     }
/*     */     catch (IOException e) {
/* 186 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 194 */     writeStartElement("", localName, "");
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 200 */     writeStartElement("", localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 206 */     encodeTerminationAndCurrentElement(false);
/*     */ 
/* 208 */     this._inStartElement = true;
/* 209 */     this._isEmptyElement = false;
/*     */ 
/* 211 */     this._currentLocalName = localName;
/* 212 */     this._currentPrefix = prefix;
/* 213 */     this._currentUri = namespaceURI;
/*     */ 
/* 215 */     this._stackCount += 1;
/* 216 */     if (this._stackCount == this._nsSupportContextStack.length) {
/* 217 */       boolean[] nsSupportContextStack = new boolean[this._stackCount * 2];
/* 218 */       System.arraycopy(this._nsSupportContextStack, 0, nsSupportContextStack, 0, this._nsSupportContextStack.length);
/* 219 */       this._nsSupportContextStack = nsSupportContextStack;
/*     */     }
/*     */ 
/* 222 */     this._nsSupportContextStack[this._stackCount] = false;
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 228 */     writeEmptyElement("", localName, "");
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName)
/*     */     throws XMLStreamException
/*     */   {
/* 234 */     writeEmptyElement("", localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 240 */     encodeTerminationAndCurrentElement(false);
/*     */ 
/* 242 */     this._isEmptyElement = (this._inStartElement = 1);
/*     */ 
/* 244 */     this._currentLocalName = localName;
/* 245 */     this._currentPrefix = prefix;
/* 246 */     this._currentUri = namespaceURI;
/*     */ 
/* 248 */     this._stackCount += 1;
/* 249 */     if (this._stackCount == this._nsSupportContextStack.length) {
/* 250 */       boolean[] nsSupportContextStack = new boolean[this._stackCount * 2];
/* 251 */       System.arraycopy(this._nsSupportContextStack, 0, nsSupportContextStack, 0, this._nsSupportContextStack.length);
/* 252 */       this._nsSupportContextStack = nsSupportContextStack;
/*     */     }
/*     */ 
/* 255 */     this._nsSupportContextStack[this._stackCount] = false;
/*     */   }
/*     */ 
/*     */   public void writeEndElement() throws XMLStreamException {
/* 259 */     if (this._inStartElement) {
/* 260 */       encodeTerminationAndCurrentElement(false);
/*     */     }
/*     */     try
/*     */     {
/* 264 */       encodeElementTermination();
/* 265 */       if (this._nsSupportContextStack[(this._stackCount--)] == 1)
/* 266 */         this._nsContext.popContext();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 270 */       throw new XMLStreamException(e);
/*     */     }
/*     */     catch (EmptyStackException e) {
/* 273 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 281 */     writeAttribute("", "", localName, value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String namespaceURI, String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 287 */     String prefix = "";
/*     */ 
/* 290 */     if (namespaceURI.length() > 0) {
/* 291 */       prefix = this._nsContext.getNonDefaultPrefix(namespaceURI);
/*     */ 
/* 294 */       if ((prefix == null) || (prefix.length() == 0))
/*     */       {
/* 297 */         if ((namespaceURI == "http://www.w3.org/2000/xmlns/") || (namespaceURI.equals("http://www.w3.org/2000/xmlns/")))
/*     */         {
/* 304 */           return;
/*     */         }
/* 306 */         throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.URIUnbound", new Object[] { namespaceURI }));
/*     */       }
/*     */     }
/* 309 */     writeAttribute(prefix, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 315 */     if (!this._inStartElement) {
/* 316 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
/*     */     }
/*     */ 
/* 324 */     if ((namespaceURI == "http://www.w3.org/2000/xmlns/") || (namespaceURI.equals("http://www.w3.org/2000/xmlns/")))
/*     */     {
/* 326 */       return;
/*     */     }
/*     */ 
/* 329 */     if (this._attributesArrayIndex == this._attributesArray.length) {
/* 330 */       String[] attributesArray = new String[this._attributesArrayIndex * 2];
/* 331 */       System.arraycopy(this._attributesArray, 0, attributesArray, 0, this._attributesArrayIndex);
/* 332 */       this._attributesArray = attributesArray;
/*     */     }
/*     */ 
/* 335 */     this._attributesArray[(this._attributesArrayIndex++)] = namespaceURI;
/* 336 */     this._attributesArray[(this._attributesArrayIndex++)] = prefix;
/* 337 */     this._attributesArray[(this._attributesArrayIndex++)] = localName;
/* 338 */     this._attributesArray[(this._attributesArrayIndex++)] = value;
/*     */   }
/*     */ 
/*     */   public void writeNamespace(String prefix, String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 344 */     if ((prefix == null) || (prefix.length() == 0) || (prefix.equals("xmlns"))) {
/* 345 */       writeDefaultNamespace(namespaceURI);
/*     */     }
/*     */     else {
/* 348 */       if (!this._inStartElement) {
/* 349 */         throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
/*     */       }
/*     */ 
/* 352 */       if (this._namespacesArrayIndex == this._namespacesArray.length) {
/* 353 */         String[] namespacesArray = new String[this._namespacesArrayIndex * 2];
/* 354 */         System.arraycopy(this._namespacesArray, 0, namespacesArray, 0, this._namespacesArrayIndex);
/* 355 */         this._namespacesArray = namespacesArray;
/*     */       }
/*     */ 
/* 358 */       this._namespacesArray[(this._namespacesArrayIndex++)] = prefix;
/* 359 */       this._namespacesArray[(this._namespacesArrayIndex++)] = namespaceURI;
/* 360 */       setPrefix(prefix, namespaceURI);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeDefaultNamespace(String namespaceURI)
/*     */     throws XMLStreamException
/*     */   {
/* 367 */     if (!this._inStartElement) {
/* 368 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
/*     */     }
/*     */ 
/* 371 */     if (this._namespacesArrayIndex == this._namespacesArray.length) {
/* 372 */       String[] namespacesArray = new String[this._namespacesArrayIndex * 2];
/* 373 */       System.arraycopy(this._namespacesArray, 0, namespacesArray, 0, this._namespacesArrayIndex);
/* 374 */       this._namespacesArray = namespacesArray;
/*     */     }
/*     */ 
/* 377 */     this._namespacesArray[(this._namespacesArrayIndex++)] = "";
/* 378 */     this._namespacesArray[(this._namespacesArrayIndex++)] = namespaceURI;
/* 379 */     setPrefix("", namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeComment(String data) throws XMLStreamException {
/*     */     try {
/* 384 */       if (getIgnoreComments()) return;
/*     */ 
/* 386 */       encodeTerminationAndCurrentElement(true);
/*     */ 
/* 389 */       encodeComment(data.toCharArray(), 0, data.length());
/*     */     }
/*     */     catch (IOException e) {
/* 392 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target)
/*     */     throws XMLStreamException
/*     */   {
/* 399 */     writeProcessingInstruction(target, "");
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 406 */       if (getIgnoreProcesingInstructions()) return;
/*     */ 
/* 408 */       encodeTerminationAndCurrentElement(true);
/*     */ 
/* 410 */       encodeProcessingInstruction(target, data);
/*     */     }
/*     */     catch (IOException e) {
/* 413 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeCData(String text) throws XMLStreamException {
/*     */     try {
/* 419 */       int length = text.length();
/* 420 */       if (length == 0)
/* 421 */         return;
/* 422 */       if (length < this._charBuffer.length) {
/* 423 */         if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(text))) {
/* 424 */           return;
/*     */         }
/*     */ 
/* 430 */         encodeTerminationAndCurrentElement(true);
/*     */ 
/* 432 */         text.getChars(0, length, this._charBuffer, 0);
/* 433 */         encodeCIIBuiltInAlgorithmDataAsCDATA(this._charBuffer, 0, length);
/*     */       } else {
/* 435 */         char[] ch = text.toCharArray();
/* 436 */         if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(ch, 0, length))) {
/* 437 */           return;
/*     */         }
/* 439 */         encodeTerminationAndCurrentElement(true);
/*     */ 
/* 441 */         encodeCIIBuiltInAlgorithmDataAsCDATA(ch, 0, length);
/*     */       }
/*     */     } catch (Exception e) {
/* 444 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/* 449 */     throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
/*     */   }
/*     */ 
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 453 */     throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/*     */     try {
/* 458 */       int length = text.length();
/* 459 */       if (length == 0)
/* 460 */         return;
/* 461 */       if (length < this._charBuffer.length) {
/* 462 */         if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(text))) {
/* 463 */           return;
/*     */         }
/*     */ 
/* 469 */         encodeTerminationAndCurrentElement(true);
/*     */ 
/* 471 */         text.getChars(0, length, this._charBuffer, 0);
/* 472 */         encodeCharacters(this._charBuffer, 0, length);
/*     */       } else {
/* 474 */         char[] ch = text.toCharArray();
/* 475 */         if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(ch, 0, length))) {
/* 476 */           return;
/*     */         }
/* 478 */         encodeTerminationAndCurrentElement(true);
/*     */ 
/* 480 */         encodeCharactersNoClone(ch, 0, length);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 484 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 492 */       if (len <= 0) {
/* 493 */         return;
/*     */       }
/*     */ 
/* 496 */       if ((getIgnoreWhiteSpaceTextContent()) && (isWhiteSpace(text, start, len))) {
/* 497 */         return;
/*     */       }
/* 499 */       encodeTerminationAndCurrentElement(true);
/*     */ 
/* 501 */       encodeCharacters(text, start, len);
/*     */     }
/*     */     catch (IOException e) {
/* 504 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/* 509 */     return this._nsContext.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri)
/*     */     throws XMLStreamException
/*     */   {
/* 515 */     if ((this._stackCount > -1) && (this._nsSupportContextStack[this._stackCount] == 0)) {
/* 516 */       this._nsSupportContextStack[this._stackCount] = true;
/* 517 */       this._nsContext.pushContext();
/*     */     }
/*     */ 
/* 520 */     this._nsContext.declarePrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/* 524 */     setPrefix("", uri);
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext context)
/*     */     throws XMLStreamException
/*     */   {
/* 544 */     throw new UnsupportedOperationException("setNamespaceContext");
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 548 */     return this._nsContext;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 554 */     if (this._manager != null) {
/* 555 */       return this._manager.getProperty(name);
/*     */     }
/* 557 */     return null;
/*     */   }
/*     */ 
/*     */   public void setManager(StAXManager manager) {
/* 561 */     this._manager = manager;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding) {
/* 565 */     this._encoding = encoding;
/*     */   }
/*     */ 
/*     */   public void writeOctets(byte[] b, int start, int len)
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 573 */       if (len == 0) {
/* 574 */         return;
/*     */       }
/*     */ 
/* 577 */       encodeTerminationAndCurrentElement(true);
/*     */ 
/* 579 */       encodeCIIOctetAlgorithmData(1, b, start, len);
/*     */     }
/*     */     catch (IOException e) {
/* 582 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void encodeTerminationAndCurrentElement(boolean terminateAfter) throws XMLStreamException {
/*     */     try {
/* 588 */       encodeTermination();
/*     */ 
/* 590 */       if (this._inStartElement)
/*     */       {
/* 592 */         this._b = 0;
/* 593 */         if (this._attributesArrayIndex > 0) {
/* 594 */           this._b |= 64;
/*     */         }
/*     */ 
/* 598 */         if (this._namespacesArrayIndex > 0) {
/* 599 */           write(this._b | 0x38);
/* 600 */           for (int i = 0; i < this._namespacesArrayIndex; ) {
/* 601 */             encodeNamespaceAttribute(this._namespacesArray[(i++)], this._namespacesArray[(i++)]);
/*     */           }
/* 603 */           this._namespacesArrayIndex = 0;
/*     */ 
/* 605 */           write(240);
/*     */ 
/* 607 */           this._b = 0;
/*     */         }
/*     */ 
/* 611 */         if (this._currentPrefix.length() == 0) {
/* 612 */           if (this._currentUri.length() == 0) {
/* 613 */             this._currentUri = this._nsContext.getNamespaceURI("");
/*     */           } else {
/* 615 */             String tmpPrefix = getPrefix(this._currentUri);
/* 616 */             if (tmpPrefix != null) {
/* 617 */               this._currentPrefix = tmpPrefix;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 622 */         encodeElementQualifiedNameOnThirdBit(this._currentUri, this._currentPrefix, this._currentLocalName);
/*     */ 
/* 624 */         for (int i = 0; i < this._attributesArrayIndex; ) {
/* 625 */           encodeAttributeQualifiedNameOnSecondBit(this._attributesArray[(i++)], this._attributesArray[(i++)], this._attributesArray[(i++)]);
/*     */ 
/* 628 */           String value = this._attributesArray[i];
/* 629 */           this._attributesArray[(i++)] = null;
/* 630 */           boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
/* 631 */           encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
/*     */ 
/* 633 */           this._b = 240;
/* 634 */           this._terminate = true;
/*     */         }
/* 636 */         this._attributesArrayIndex = 0;
/* 637 */         this._inStartElement = false;
/*     */ 
/* 639 */         if (this._isEmptyElement) {
/* 640 */           encodeElementTermination();
/* 641 */           if (this._nsSupportContextStack[(this._stackCount--)] == 1) {
/* 642 */             this._nsContext.popContext();
/*     */           }
/*     */ 
/* 645 */           this._isEmptyElement = false;
/*     */         }
/*     */ 
/* 648 */         if (terminateAfter)
/* 649 */           encodeTermination();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 653 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void initiateLowLevelWriting()
/*     */     throws XMLStreamException
/*     */   {
/* 661 */     encodeTerminationAndCurrentElement(false);
/*     */   }
/*     */ 
/*     */   public final int getNextElementIndex() {
/* 665 */     return this._v.elementName.getNextIndex();
/*     */   }
/*     */ 
/*     */   public final int getNextAttributeIndex() {
/* 669 */     return this._v.attributeName.getNextIndex();
/*     */   }
/*     */ 
/*     */   public final int getLocalNameIndex() {
/* 673 */     return this._v.localName.getIndex();
/*     */   }
/*     */ 
/*     */   public final int getNextLocalNameIndex() {
/* 677 */     return this._v.localName.getNextIndex();
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelTerminationAndMark() throws IOException {
/* 681 */     encodeTermination();
/* 682 */     mark();
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelStartElementIndexed(int type, int index) throws IOException {
/* 686 */     this._b = type;
/* 687 */     encodeNonZeroIntegerOnThirdBit(index);
/*     */   }
/*     */ 
/*     */   public final boolean writeLowLevelStartElement(int type, String prefix, String localName, String namespaceURI) throws IOException
/*     */   {
/* 692 */     boolean isIndexed = encodeElement(type, namespaceURI, prefix, localName);
/*     */ 
/* 694 */     if (!isIndexed) {
/* 695 */       encodeLiteral(type | 0x3C, namespaceURI, prefix, localName);
/*     */     }
/*     */ 
/* 698 */     return isIndexed;
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelStartNamespaces() throws IOException {
/* 702 */     write(56);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelNamespace(String prefix, String namespaceName) throws IOException
/*     */   {
/* 707 */     encodeNamespaceAttribute(prefix, namespaceName);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelEndNamespaces() throws IOException {
/* 711 */     write(240);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelStartAttributes() throws IOException {
/* 715 */     if (hasMark())
/*     */     {
/*     */       int tmp15_12 = this._markIndex;
/*     */       byte[] tmp15_8 = this._octetBuffer; tmp15_8[tmp15_12] = ((byte)(tmp15_8[tmp15_12] | 0x40));
/* 717 */       resetMark();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelAttributeIndexed(int index) throws IOException {
/* 722 */     encodeNonZeroIntegerOnSecondBitFirstBitZero(index);
/*     */   }
/*     */ 
/*     */   public final boolean writeLowLevelAttribute(String prefix, String namespaceURI, String localName) throws IOException {
/* 726 */     boolean isIndexed = encodeAttribute(namespaceURI, prefix, localName);
/*     */ 
/* 728 */     if (!isIndexed) {
/* 729 */       encodeLiteral(120, namespaceURI, prefix, localName);
/*     */     }
/*     */ 
/* 732 */     return isIndexed;
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelAttributeValue(String value) throws IOException
/*     */   {
/* 737 */     boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
/* 738 */     encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelStartNameLiteral(int type, String prefix, byte[] utf8LocalName, String namespaceURI) throws IOException
/*     */   {
/* 743 */     encodeLiteralHeader(type, namespaceURI, prefix);
/* 744 */     encodeNonZeroOctetStringLengthOnSecondBit(utf8LocalName.length);
/* 745 */     write(utf8LocalName, 0, utf8LocalName.length);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelStartNameLiteral(int type, String prefix, int localNameIndex, String namespaceURI) throws IOException
/*     */   {
/* 750 */     encodeLiteralHeader(type, namespaceURI, prefix);
/* 751 */     encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelEndStartElement() throws IOException {
/* 755 */     if (hasMark()) {
/* 756 */       resetMark();
/*     */     }
/*     */     else {
/* 759 */       this._b = 240;
/* 760 */       this._terminate = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelEndElement() throws IOException {
/* 765 */     encodeElementTermination();
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelText(char[] text, int length) throws IOException {
/* 769 */     if (length == 0) {
/* 770 */       return;
/*     */     }
/* 772 */     encodeTermination();
/*     */ 
/* 774 */     encodeCharacters(text, 0, length);
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelText(String text) throws IOException {
/* 778 */     int length = text.length();
/* 779 */     if (length == 0) {
/* 780 */       return;
/*     */     }
/* 782 */     encodeTermination();
/*     */ 
/* 784 */     if (length < this._charBuffer.length) {
/* 785 */       text.getChars(0, length, this._charBuffer, 0);
/* 786 */       encodeCharacters(this._charBuffer, 0, length);
/*     */     } else {
/* 788 */       char[] ch = text.toCharArray();
/* 789 */       encodeCharactersNoClone(ch, 0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeLowLevelOctets(byte[] octets, int length) throws IOException {
/* 794 */     if (length == 0) {
/* 795 */       return;
/*     */     }
/* 797 */     encodeTermination();
/*     */ 
/* 799 */     encodeCIIOctetAlgorithmData(1, octets, 0, length);
/*     */   }
/*     */ 
/*     */   private boolean encodeElement(int type, String namespaceURI, String prefix, String localName) throws IOException {
/* 803 */     LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
/* 804 */     for (int i = 0; i < entry._valueIndex; i++) {
/* 805 */       QualifiedName name = entry._value[i];
/* 806 */       if (((prefix == name.prefix) || (prefix.equals(name.prefix))) && ((namespaceURI == name.namespaceName) || (namespaceURI.equals(name.namespaceName))))
/*     */       {
/* 808 */         this._b = type;
/* 809 */         encodeNonZeroIntegerOnThirdBit(name.index);
/* 810 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 814 */     entry.addQualifiedName(new QualifiedName(prefix, namespaceURI, localName, "", this._v.elementName.getNextIndex()));
/* 815 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean encodeAttribute(String namespaceURI, String prefix, String localName) throws IOException {
/* 819 */     LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
/* 820 */     for (int i = 0; i < entry._valueIndex; i++) {
/* 821 */       QualifiedName name = entry._value[i];
/* 822 */       if (((prefix == name.prefix) || (prefix.equals(name.prefix))) && ((namespaceURI == name.namespaceName) || (namespaceURI.equals(name.namespaceName))))
/*     */       {
/* 824 */         encodeNonZeroIntegerOnSecondBitFirstBitZero(name.index);
/* 825 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 829 */     entry.addQualifiedName(new QualifiedName(prefix, namespaceURI, localName, "", this._v.attributeName.getNextIndex()));
/* 830 */     return false;
/*     */   }
/*     */ 
/*     */   private void encodeLiteralHeader(int type, String namespaceURI, String prefix) throws IOException {
/* 834 */     if (namespaceURI != "") {
/* 835 */       type |= 1;
/* 836 */       if (prefix != "") {
/* 837 */         type |= 2;
/*     */       }
/* 839 */       write(type);
/* 840 */       if (prefix != "")
/* 841 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(this._v.prefix.get(prefix));
/* 842 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(this._v.namespaceName.get(namespaceURI));
/*     */     } else {
/* 844 */       write(type);
/*     */     }
/*     */   }
/*     */ 
/* 848 */   private void encodeLiteral(int type, String namespaceURI, String prefix, String localName) throws IOException { encodeLiteralHeader(type, namespaceURI, prefix);
/*     */ 
/* 850 */     int localNameIndex = this._v.localName.obtainIndex(localName);
/* 851 */     if (localNameIndex == -1)
/* 852 */       encodeNonEmptyOctetStringOnSecondBit(localName);
/*     */     else
/* 854 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.StAXDocumentSerializer
 * JD-Core Version:    0.6.2
 */