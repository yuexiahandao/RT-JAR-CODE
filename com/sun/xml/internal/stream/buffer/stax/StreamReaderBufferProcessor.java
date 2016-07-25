/*      */ package com.sun.xml.internal.stream.buffer.stax;
/*      */ 
/*      */ import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
/*      */ import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding;
/*      */ import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
/*      */ import com.sun.xml.internal.stream.buffer.AbstractProcessor;
/*      */ import com.sun.xml.internal.stream.buffer.AttributesHolder;
/*      */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*      */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferMark;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.Location;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ 
/*      */ public class StreamReaderBufferProcessor extends AbstractProcessor
/*      */   implements XMLStreamReaderEx
/*      */ {
/*      */   private static final int CACHE_SIZE = 16;
/*   67 */   protected ElementStackEntry[] _stack = new ElementStackEntry[16];
/*      */   protected ElementStackEntry _stackTop;
/*      */   protected int _depth;
/*   77 */   protected String[] _namespaceAIIsPrefix = new String[16];
/*   78 */   protected String[] _namespaceAIIsNamespaceName = new String[16];
/*      */   protected int _namespaceAIIsEnd;
/*   82 */   protected InternalNamespaceContext _nsCtx = new InternalNamespaceContext(null);
/*      */   protected int _eventType;
/*      */   protected AttributesHolder _attributeCache;
/*      */   protected CharSequence _charSequence;
/*      */   protected char[] _characters;
/*      */   protected int _textOffset;
/*      */   protected int _textLen;
/*      */   protected String _piTarget;
/*      */   protected String _piData;
/*      */   private static final int PARSING = 1;
/*      */   private static final int PENDING_END_DOCUMENT = 2;
/*      */   private static final int COMPLETED = 3;
/*      */   private int _completionState;
/*      */ 
/*      */   public StreamReaderBufferProcessor()
/*      */   {
/*  131 */     for (int i = 0; i < this._stack.length; i++) {
/*  132 */       this._stack[i] = new ElementStackEntry(null);
/*      */     }
/*      */ 
/*  135 */     this._attributeCache = new AttributesHolder();
/*      */   }
/*      */ 
/*      */   public StreamReaderBufferProcessor(XMLStreamBuffer buffer) throws XMLStreamException {
/*  139 */     this();
/*  140 */     setXMLStreamBuffer(buffer);
/*      */   }
/*      */ 
/*      */   public void setXMLStreamBuffer(XMLStreamBuffer buffer) throws XMLStreamException {
/*  144 */     setBuffer(buffer, buffer.isFragment());
/*      */ 
/*  146 */     this._completionState = 1;
/*  147 */     this._namespaceAIIsEnd = 0;
/*  148 */     this._characters = null;
/*  149 */     this._charSequence = null;
/*  150 */     this._eventType = 7;
/*      */   }
/*      */ 
/*      */   public XMLStreamBuffer nextTagAndMark()
/*      */     throws XMLStreamException
/*      */   {
/*      */     while (true)
/*      */     {
/*  167 */       int s = peekStructure();
/*  168 */       if ((s & 0xF0) == 32)
/*      */       {
/*  170 */         Map inscope = new HashMap(this._namespaceAIIsEnd);
/*      */ 
/*  172 */         for (int i = 0; i < this._namespaceAIIsEnd; i++) {
/*  173 */           inscope.put(this._namespaceAIIsPrefix[i], this._namespaceAIIsNamespaceName[i]);
/*      */         }
/*  175 */         XMLStreamBufferMark mark = new XMLStreamBufferMark(inscope, this);
/*  176 */         next();
/*  177 */         return mark;
/*  178 */       }if ((s & 0xF0) == 16)
/*      */       {
/*  180 */         readStructure();
/*      */ 
/*  182 */         XMLStreamBufferMark mark = new XMLStreamBufferMark(new HashMap(this._namespaceAIIsEnd), this);
/*  183 */         next();
/*  184 */         return mark;
/*      */       }
/*      */ 
/*  187 */       if (next() == 2)
/*  188 */         return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name) {
/*  193 */     return null;
/*      */   }
/*      */ 
/*      */   public int next() throws XMLStreamException {
/*  197 */     switch (this._completionState) {
/*      */     case 3:
/*  199 */       throw new XMLStreamException("Invalid State");
/*      */     case 2:
/*  201 */       this._namespaceAIIsEnd = 0;
/*  202 */       this._completionState = 3;
/*  203 */       return this._eventType = 8;
/*      */     }
/*      */ 
/*  211 */     switch (this._eventType) {
/*      */     case 2:
/*  213 */       if (this._depth > 1) {
/*  214 */         this._depth -= 1;
/*      */ 
/*  217 */         popElementStack(this._depth);
/*  218 */       } else if (this._depth == 1) {
/*  219 */         this._depth -= 1;
/*      */       }break;
/*  223 */     }this._characters = null;
/*  224 */     this._charSequence = null;
/*      */     int eiiState;
/*      */     while (true) { eiiState = readEiiState();
/*  227 */       switch (eiiState) { case 1:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*  233 */       case 2: }  } String uri = readStructureString();
/*  234 */     String localName = readStructureString();
/*  235 */     String prefix = getPrefixFromQName(readStructureString());
/*      */ 
/*  237 */     processElement(prefix, uri, localName);
/*  238 */     return this._eventType = 1;
/*      */ 
/*  241 */     processElement(readStructureString(), readStructureString(), readStructureString());
/*  242 */     return this._eventType = 1;
/*      */ 
/*  244 */     processElement(null, readStructureString(), readStructureString());
/*  245 */     return this._eventType = 1;
/*      */ 
/*  247 */     processElement(null, null, readStructureString());
/*  248 */     return this._eventType = 1;
/*      */ 
/*  250 */     this._textLen = readStructure();
/*  251 */     this._textOffset = readContentCharactersBuffer(this._textLen);
/*  252 */     this._characters = this._contentCharactersBuffer;
/*      */ 
/*  254 */     return this._eventType = 4;
/*      */ 
/*  256 */     this._textLen = readStructure16();
/*  257 */     this._textOffset = readContentCharactersBuffer(this._textLen);
/*  258 */     this._characters = this._contentCharactersBuffer;
/*      */ 
/*  260 */     return this._eventType = 4;
/*      */ 
/*  262 */     this._characters = readContentCharactersCopy();
/*  263 */     this._textLen = this._characters.length;
/*  264 */     this._textOffset = 0;
/*      */ 
/*  266 */     return this._eventType = 4;
/*      */ 
/*  268 */     this._eventType = 4;
/*  269 */     this._charSequence = readContentString();
/*      */ 
/*  271 */     return this._eventType = 4;
/*      */ 
/*  273 */     this._eventType = 4;
/*  274 */     this._charSequence = ((CharSequence)readContentObject());
/*      */ 
/*  276 */     return this._eventType = 4;
/*      */ 
/*  278 */     this._textLen = readStructure();
/*  279 */     this._textOffset = readContentCharactersBuffer(this._textLen);
/*  280 */     this._characters = this._contentCharactersBuffer;
/*      */ 
/*  282 */     return this._eventType = 5;
/*      */ 
/*  284 */     this._textLen = readStructure16();
/*  285 */     this._textOffset = readContentCharactersBuffer(this._textLen);
/*  286 */     this._characters = this._contentCharactersBuffer;
/*      */ 
/*  288 */     return this._eventType = 5;
/*      */ 
/*  290 */     this._characters = readContentCharactersCopy();
/*  291 */     this._textLen = this._characters.length;
/*  292 */     this._textOffset = 0;
/*      */ 
/*  294 */     return this._eventType = 5;
/*      */ 
/*  296 */     this._charSequence = readContentString();
/*      */ 
/*  298 */     return this._eventType = 5;
/*      */ 
/*  300 */     this._piTarget = readStructureString();
/*  301 */     this._piData = readStructureString();
/*      */ 
/*  303 */     return this._eventType = 3;
/*      */ 
/*  305 */     if (this._depth > 1)
/*      */     {
/*  307 */       return this._eventType = 2;
/*  308 */     }if (this._depth == 1)
/*      */     {
/*  310 */       if ((this._fragmentMode) && 
/*  311 */         (--this._treeCount == 0)) {
/*  312 */         this._completionState = 2;
/*      */       }
/*  314 */       return this._eventType = 2;
/*      */     }
/*      */ 
/*  318 */     this._namespaceAIIsEnd = 0;
/*  319 */     this._completionState = 3;
/*  320 */     return this._eventType = 8;
/*      */ 
/*  323 */     throw new XMLStreamException("Internal XSB error: Invalid State=" + eiiState);
/*      */   }
/*      */ 
/*      */   public final void require(int type, String namespaceURI, String localName)
/*      */     throws XMLStreamException
/*      */   {
/*  330 */     if (type != this._eventType) {
/*  331 */       throw new XMLStreamException("");
/*      */     }
/*  333 */     if ((namespaceURI != null) && (!namespaceURI.equals(getNamespaceURI()))) {
/*  334 */       throw new XMLStreamException("");
/*      */     }
/*  336 */     if ((localName != null) && (!localName.equals(getLocalName())))
/*  337 */       throw new XMLStreamException("");
/*      */   }
/*      */ 
/*      */   public final String getElementTextTrim()
/*      */     throws XMLStreamException
/*      */   {
/*  343 */     return getElementText().trim();
/*      */   }
/*      */ 
/*      */   public final String getElementText() throws XMLStreamException {
/*  347 */     if (this._eventType != 1) {
/*  348 */       throw new XMLStreamException("");
/*      */     }
/*      */ 
/*  351 */     next();
/*  352 */     return getElementText(true);
/*      */   }
/*      */ 
/*      */   public final String getElementText(boolean startElementRead) throws XMLStreamException {
/*  356 */     if (!startElementRead) {
/*  357 */       throw new XMLStreamException("");
/*      */     }
/*      */ 
/*  360 */     int eventType = getEventType();
/*  361 */     StringBuffer content = new StringBuffer();
/*  362 */     while (eventType != 2) {
/*  363 */       if ((eventType == 4) || (eventType == 12) || (eventType == 6) || (eventType == 9))
/*      */       {
/*  367 */         content.append(getText());
/*  368 */       } else if ((eventType != 3) && (eventType != 5))
/*      */       {
/*  371 */         if (eventType == 8)
/*  372 */           throw new XMLStreamException("");
/*  373 */         if (eventType == 1) {
/*  374 */           throw new XMLStreamException("");
/*      */         }
/*  376 */         throw new XMLStreamException("");
/*      */       }
/*  378 */       eventType = next();
/*      */     }
/*  380 */     return content.toString();
/*      */   }
/*      */ 
/*      */   public final int nextTag() throws XMLStreamException {
/*  384 */     next();
/*  385 */     return nextTag(true);
/*      */   }
/*      */ 
/*      */   public final int nextTag(boolean currentTagRead) throws XMLStreamException {
/*  389 */     int eventType = getEventType();
/*  390 */     if (!currentTagRead) {
/*  391 */       eventType = next();
/*      */     }
/*      */ 
/*  397 */     while (((eventType == 4) && (isWhiteSpace())) || ((eventType == 12) && (isWhiteSpace())) || (eventType == 6) || (eventType == 3) || (eventType == 5)) {
/*  398 */       eventType = next();
/*      */     }
/*  400 */     if ((eventType != 1) && (eventType != 2)) {
/*  401 */       throw new XMLStreamException("");
/*      */     }
/*  403 */     return eventType;
/*      */   }
/*      */ 
/*      */   public final boolean hasNext() {
/*  407 */     return this._eventType != 8;
/*      */   }
/*      */ 
/*      */   public void close() throws XMLStreamException {
/*      */   }
/*      */ 
/*      */   public final boolean isStartElement() {
/*  414 */     return this._eventType == 1;
/*      */   }
/*      */ 
/*      */   public final boolean isEndElement() {
/*  418 */     return this._eventType == 2;
/*      */   }
/*      */ 
/*      */   public final boolean isCharacters() {
/*  422 */     return this._eventType == 4;
/*      */   }
/*      */ 
/*      */   public final boolean isWhiteSpace() {
/*  426 */     if ((isCharacters()) || (this._eventType == 12)) {
/*  427 */       char[] ch = getTextCharacters();
/*  428 */       int start = getTextStart();
/*  429 */       int length = getTextLength();
/*  430 */       for (int i = start; i < length; i++) {
/*  431 */         char c = ch[i];
/*  432 */         if ((c != ' ') && (c != '\t') && (c != '\r') && (c != '\n'))
/*  433 */           return false;
/*      */       }
/*  435 */       return true;
/*      */     }
/*  437 */     return false;
/*      */   }
/*      */ 
/*      */   public final String getAttributeValue(String namespaceURI, String localName) {
/*  441 */     if (this._eventType != 1) {
/*  442 */       throw new IllegalStateException("");
/*      */     }
/*      */ 
/*  445 */     if (namespaceURI == null)
/*      */     {
/*  448 */       namespaceURI = "";
/*      */     }
/*      */ 
/*  451 */     return this._attributeCache.getValue(namespaceURI, localName);
/*      */   }
/*      */ 
/*      */   public final int getAttributeCount() {
/*  455 */     if (this._eventType != 1) {
/*  456 */       throw new IllegalStateException("");
/*      */     }
/*      */ 
/*  459 */     return this._attributeCache.getLength();
/*      */   }
/*      */ 
/*      */   public final QName getAttributeName(int index) {
/*  463 */     if (this._eventType != 1) {
/*  464 */       throw new IllegalStateException("");
/*      */     }
/*      */ 
/*  467 */     String prefix = this._attributeCache.getPrefix(index);
/*  468 */     String localName = this._attributeCache.getLocalName(index);
/*  469 */     String uri = this._attributeCache.getURI(index);
/*  470 */     return new QName(uri, localName, prefix);
/*      */   }
/*      */ 
/*      */   public final String getAttributeNamespace(int index)
/*      */   {
/*  475 */     if (this._eventType != 1) {
/*  476 */       throw new IllegalStateException("");
/*      */     }
/*  478 */     return fixEmptyString(this._attributeCache.getURI(index));
/*      */   }
/*      */ 
/*      */   public final String getAttributeLocalName(int index) {
/*  482 */     if (this._eventType != 1) {
/*  483 */       throw new IllegalStateException("");
/*      */     }
/*  485 */     return this._attributeCache.getLocalName(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributePrefix(int index) {
/*  489 */     if (this._eventType != 1) {
/*  490 */       throw new IllegalStateException("");
/*      */     }
/*  492 */     return fixEmptyString(this._attributeCache.getPrefix(index));
/*      */   }
/*      */ 
/*      */   public final String getAttributeType(int index) {
/*  496 */     if (this._eventType != 1) {
/*  497 */       throw new IllegalStateException("");
/*      */     }
/*  499 */     return this._attributeCache.getType(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributeValue(int index) {
/*  503 */     if (this._eventType != 1) {
/*  504 */       throw new IllegalStateException("");
/*      */     }
/*      */ 
/*  507 */     return this._attributeCache.getValue(index);
/*      */   }
/*      */ 
/*      */   public final boolean isAttributeSpecified(int index) {
/*  511 */     return false;
/*      */   }
/*      */ 
/*      */   public final int getNamespaceCount() {
/*  515 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  516 */       return this._stackTop.namespaceAIIsEnd - this._stackTop.namespaceAIIsStart;
/*      */     }
/*      */ 
/*  519 */     throw new IllegalStateException("");
/*      */   }
/*      */ 
/*      */   public final String getNamespacePrefix(int index) {
/*  523 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  524 */       return this._namespaceAIIsPrefix[(this._stackTop.namespaceAIIsStart + index)];
/*      */     }
/*      */ 
/*  527 */     throw new IllegalStateException("");
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI(int index) {
/*  531 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  532 */       return this._namespaceAIIsNamespaceName[(this._stackTop.namespaceAIIsStart + index)];
/*      */     }
/*      */ 
/*  535 */     throw new IllegalStateException("");
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI(String prefix) {
/*  539 */     return this._nsCtx.getNamespaceURI(prefix);
/*      */   }
/*      */ 
/*      */   public final NamespaceContextEx getNamespaceContext() {
/*  543 */     return this._nsCtx;
/*      */   }
/*      */ 
/*      */   public final int getEventType() {
/*  547 */     return this._eventType;
/*      */   }
/*      */ 
/*      */   public final String getText() {
/*  551 */     if (this._characters != null) {
/*  552 */       String s = new String(this._characters, this._textOffset, this._textLen);
/*  553 */       this._charSequence = s;
/*  554 */       return s;
/*  555 */     }if (this._charSequence != null) {
/*  556 */       return this._charSequence.toString();
/*      */     }
/*  558 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final char[] getTextCharacters()
/*      */   {
/*  563 */     if (this._characters != null)
/*  564 */       return this._characters;
/*  565 */     if (this._charSequence != null)
/*      */     {
/*  568 */       this._characters = this._charSequence.toString().toCharArray();
/*  569 */       this._textLen = this._characters.length;
/*  570 */       this._textOffset = 0;
/*  571 */       return this._characters;
/*      */     }
/*  573 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final int getTextStart()
/*      */   {
/*  578 */     if (this._characters != null)
/*  579 */       return this._textOffset;
/*  580 */     if (this._charSequence != null) {
/*  581 */       return 0;
/*      */     }
/*  583 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final int getTextLength()
/*      */   {
/*  588 */     if (this._characters != null)
/*  589 */       return this._textLen;
/*  590 */     if (this._charSequence != null) {
/*  591 */       return this._charSequence.length();
/*      */     }
/*  593 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*      */     throws XMLStreamException
/*      */   {
/*  599 */     if (this._characters == null)
/*  600 */       if (this._charSequence != null) {
/*  601 */         this._characters = this._charSequence.toString().toCharArray();
/*  602 */         this._textLen = this._characters.length;
/*  603 */         this._textOffset = 0;
/*      */       } else {
/*  605 */         throw new IllegalStateException("");
/*      */       }
/*      */     try
/*      */     {
/*  609 */       int remaining = this._textLen - sourceStart;
/*  610 */       int len = remaining > length ? length : remaining;
/*  611 */       sourceStart += this._textOffset;
/*  612 */       System.arraycopy(this._characters, sourceStart, target, targetStart, len);
/*  613 */       return len;
/*      */     } catch (IndexOutOfBoundsException e) {
/*  615 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final CharSequence getPCDATA()
/*      */   {
/*  655 */     if (this._characters != null)
/*  656 */       return new CharSequenceImpl(this._textOffset, this._textLen);
/*  657 */     if (this._charSequence != null) {
/*  658 */       return this._charSequence;
/*      */     }
/*  660 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   public final String getEncoding()
/*      */   {
/*  665 */     return "UTF-8";
/*      */   }
/*      */ 
/*      */   public final boolean hasText() {
/*  669 */     return (this._characters != null) || (this._charSequence != null);
/*      */   }
/*      */ 
/*      */   public final Location getLocation() {
/*  673 */     return new DummyLocation(null);
/*      */   }
/*      */ 
/*      */   public final boolean hasName() {
/*  677 */     return (this._eventType == 1) || (this._eventType == 2);
/*      */   }
/*      */ 
/*      */   public final QName getName() {
/*  681 */     return this._stackTop.getQName();
/*      */   }
/*      */ 
/*      */   public final String getLocalName() {
/*  685 */     return this._stackTop.localName;
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI() {
/*  689 */     return this._stackTop.uri;
/*      */   }
/*      */ 
/*      */   public final String getPrefix() {
/*  693 */     return this._stackTop.prefix;
/*      */   }
/*      */ 
/*      */   public final String getVersion()
/*      */   {
/*  698 */     return "1.0";
/*      */   }
/*      */ 
/*      */   public final boolean isStandalone() {
/*  702 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean standaloneSet() {
/*  706 */     return false;
/*      */   }
/*      */ 
/*      */   public final String getCharacterEncodingScheme() {
/*  710 */     return "UTF-8";
/*      */   }
/*      */ 
/*      */   public final String getPITarget() {
/*  714 */     if (this._eventType == 3) {
/*  715 */       return this._piTarget;
/*      */     }
/*  717 */     throw new IllegalStateException("");
/*      */   }
/*      */ 
/*      */   public final String getPIData() {
/*  721 */     if (this._eventType == 3) {
/*  722 */       return this._piData;
/*      */     }
/*  724 */     throw new IllegalStateException("");
/*      */   }
/*      */ 
/*      */   protected void processElement(String prefix, String uri, String localName) {
/*  728 */     pushElementStack();
/*  729 */     this._stackTop.set(prefix, uri, localName);
/*      */ 
/*  731 */     this._attributeCache.clear();
/*      */ 
/*  733 */     int item = peekStructure();
/*  734 */     if ((item & 0xF0) == 64)
/*      */     {
/*  737 */       item = processNamespaceAttributes(item);
/*      */     }
/*  739 */     if ((item & 0xF0) == 48)
/*  740 */       processAttributes(item);
/*      */   }
/*      */ 
/*      */   private void resizeNamespaceAttributes()
/*      */   {
/*  745 */     String[] namespaceAIIsPrefix = new String[this._namespaceAIIsEnd * 2];
/*  746 */     System.arraycopy(this._namespaceAIIsPrefix, 0, namespaceAIIsPrefix, 0, this._namespaceAIIsEnd);
/*  747 */     this._namespaceAIIsPrefix = namespaceAIIsPrefix;
/*      */ 
/*  749 */     String[] namespaceAIIsNamespaceName = new String[this._namespaceAIIsEnd * 2];
/*  750 */     System.arraycopy(this._namespaceAIIsNamespaceName, 0, namespaceAIIsNamespaceName, 0, this._namespaceAIIsEnd);
/*  751 */     this._namespaceAIIsNamespaceName = namespaceAIIsNamespaceName;
/*      */   }
/*      */ 
/*      */   private int processNamespaceAttributes(int item) {
/*  755 */     this._stackTop.namespaceAIIsStart = this._namespaceAIIsEnd;
/*      */     do
/*      */     {
/*  758 */       if (this._namespaceAIIsEnd == this._namespaceAIIsPrefix.length) {
/*  759 */         resizeNamespaceAttributes();
/*      */       }
/*      */ 
/*  762 */       switch (getNIIState(item))
/*      */       {
/*      */       case 1:
/*      */         String tmp85_83 = ""; this._namespaceAIIsNamespaceName[(this._namespaceAIIsEnd++)] = tmp85_83; this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = tmp85_83;
/*      */ 
/*  767 */         break;
/*      */       case 2:
/*  770 */         this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = readStructureString();
/*  771 */         this._namespaceAIIsNamespaceName[(this._namespaceAIIsEnd++)] = "";
/*  772 */         break;
/*      */       case 3:
/*  775 */         this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = readStructureString();
/*  776 */         this._namespaceAIIsNamespaceName[(this._namespaceAIIsEnd++)] = readStructureString();
/*  777 */         break;
/*      */       case 4:
/*  780 */         this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = "";
/*  781 */         this._namespaceAIIsNamespaceName[(this._namespaceAIIsEnd++)] = readStructureString();
/*      */       }
/*      */ 
/*  784 */       readStructure();
/*      */ 
/*  786 */       item = peekStructure();
/*  787 */     }while ((item & 0xF0) == 64);
/*      */ 
/*  789 */     this._stackTop.namespaceAIIsEnd = this._namespaceAIIsEnd;
/*      */ 
/*  791 */     return item;
/*      */   }
/*      */ 
/*      */   private void processAttributes(int item) {
/*      */     do {
/*  796 */       switch (getAIIState(item)) {
/*      */       case 1:
/*  798 */         String uri = readStructureString();
/*  799 */         String localName = readStructureString();
/*  800 */         String prefix = getPrefixFromQName(readStructureString());
/*  801 */         this._attributeCache.addAttributeWithPrefix(prefix, uri, localName, readStructureString(), readContentString());
/*  802 */         break;
/*      */       case 2:
/*  805 */         this._attributeCache.addAttributeWithPrefix(readStructureString(), readStructureString(), readStructureString(), readStructureString(), readContentString());
/*  806 */         break;
/*      */       case 3:
/*  809 */         this._attributeCache.addAttributeWithPrefix("", readStructureString(), readStructureString(), readStructureString(), readContentString());
/*  810 */         break;
/*      */       case 4:
/*  812 */         this._attributeCache.addAttributeWithPrefix("", "", readStructureString(), readStructureString(), readContentString());
/*  813 */         break;
/*      */       default:
/*  816 */         if (!$assertionsDisabled) throw new AssertionError("Internal XSB Error: wrong attribute state, Item=" + item); break;
/*      */       }
/*  818 */       readStructure();
/*      */ 
/*  820 */       item = peekStructure();
/*  821 */     }while ((item & 0xF0) == 48);
/*      */   }
/*      */ 
/*      */   private void pushElementStack() {
/*  825 */     if (this._depth == this._stack.length)
/*      */     {
/*  827 */       ElementStackEntry[] tmp = this._stack;
/*  828 */       this._stack = new ElementStackEntry[this._stack.length * 3 / 2 + 1];
/*  829 */       System.arraycopy(tmp, 0, this._stack, 0, tmp.length);
/*  830 */       for (int i = tmp.length; i < this._stack.length; i++) {
/*  831 */         this._stack[i] = new ElementStackEntry(null);
/*      */       }
/*      */     }
/*      */ 
/*  835 */     this._stackTop = this._stack[(this._depth++)];
/*      */   }
/*      */ 
/*      */   private void popElementStack(int depth)
/*      */   {
/*  840 */     this._stackTop = this._stack[(depth - 1)];
/*      */ 
/*  842 */     this._namespaceAIIsEnd = this._stack[depth].namespaceAIIsStart;
/*      */   }
/*      */ 
/*      */   private static String fixEmptyString(String s)
/*      */   {
/* 1090 */     if (s.length() == 0) return null;
/* 1091 */     return s;
/*      */   }
/*      */ 
/*      */   private class CharSequenceImpl
/*      */     implements CharSequence
/*      */   {
/*      */     private final int _offset;
/*      */     private final int _length;
/*      */ 
/*      */     CharSequenceImpl(int offset, int length)
/*      */     {
/*  624 */       this._offset = offset;
/*  625 */       this._length = length;
/*      */     }
/*      */ 
/*      */     public int length() {
/*  629 */       return this._length;
/*      */     }
/*      */ 
/*      */     public char charAt(int index) {
/*  633 */       if ((index >= 0) && (index < StreamReaderBufferProcessor.this._textLen)) {
/*  634 */         return StreamReaderBufferProcessor.this._characters[(StreamReaderBufferProcessor.this._textOffset + index)];
/*      */       }
/*  636 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */ 
/*      */     public CharSequence subSequence(int start, int end)
/*      */     {
/*  641 */       int length = end - start;
/*  642 */       if ((end < 0) || (start < 0) || (end > length) || (start > end)) {
/*  643 */         throw new IndexOutOfBoundsException();
/*      */       }
/*      */ 
/*  646 */       return new CharSequenceImpl(StreamReaderBufferProcessor.this, this._offset + start, length);
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  650 */       return new String(StreamReaderBufferProcessor.this._characters, this._offset, this._length);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DummyLocation
/*      */     implements Location
/*      */   {
/*      */     private DummyLocation()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int getLineNumber()
/*      */     {
/* 1068 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getColumnNumber() {
/* 1072 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getCharacterOffset() {
/* 1076 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getPublicId() {
/* 1080 */       return null;
/*      */     }
/*      */ 
/*      */     public String getSystemId() {
/* 1084 */       return StreamReaderBufferProcessor.this._buffer.getSystemId();
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class ElementStackEntry
/*      */   {
/*      */     String prefix;
/*      */     String uri;
/*      */     String localName;
/*      */     QName qname;
/*      */     int namespaceAIIsStart;
/*      */     int namespaceAIIsEnd;
/*      */ 
/*      */     private ElementStackEntry()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void set(String prefix, String uri, String localName)
/*      */     {
/*  865 */       this.prefix = prefix;
/*  866 */       this.uri = uri;
/*  867 */       this.localName = localName;
/*  868 */       this.qname = null;
/*      */ 
/*  870 */       this.namespaceAIIsStart = (this.namespaceAIIsEnd = StreamReaderBufferProcessor.this._namespaceAIIsEnd);
/*      */     }
/*      */ 
/*      */     public QName getQName() {
/*  874 */       if (this.qname == null) {
/*  875 */         this.qname = new QName(fixNull(this.uri), this.localName, fixNull(this.prefix));
/*      */       }
/*  877 */       return this.qname;
/*      */     }
/*      */ 
/*      */     private String fixNull(String s) {
/*  881 */       return s == null ? "" : s;
/*      */     }
/*      */   }
/*      */   private final class InternalNamespaceContext implements NamespaceContextEx {
/*      */     private InternalNamespaceContext() {
/*      */     }
/*      */ 
/*  888 */     public String getNamespaceURI(String prefix) { if (prefix == null) {
/*  889 */         throw new IllegalArgumentException("Prefix cannot be null");
/*      */       }
/*      */ 
/*  897 */       if (StreamReaderBufferProcessor.this._stringInterningFeature) {
/*  898 */         prefix = prefix.intern();
/*      */ 
/*  901 */         for (int i = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1; i >= 0; i--) {
/*  902 */           if (prefix == StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i])
/*  903 */             return StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[i];
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  908 */         for (int i = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1; i >= 0; i--) {
/*  909 */           if (prefix.equals(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i])) {
/*  910 */             return StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[i];
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  916 */       if (prefix.equals("xml"))
/*  917 */         return "http://www.w3.org/XML/1998/namespace";
/*  918 */       if (prefix.equals("xmlns")) {
/*  919 */         return "http://www.w3.org/2000/xmlns/";
/*      */       }
/*      */ 
/*  922 */       return null; }
/*      */ 
/*      */     public String getPrefix(String namespaceURI)
/*      */     {
/*  926 */       Iterator i = getPrefixes(namespaceURI);
/*  927 */       if (i.hasNext()) {
/*  928 */         return (String)i.next();
/*      */       }
/*  930 */       return null;
/*      */     }
/*      */ 
/*      */     public Iterator getPrefixes(final String namespaceURI)
/*      */     {
/*  935 */       if (namespaceURI == null) {
/*  936 */         throw new IllegalArgumentException("NamespaceURI cannot be null");
/*      */       }
/*      */ 
/*  939 */       if (namespaceURI.equals("http://www.w3.org/XML/1998/namespace"))
/*  940 */         return Collections.singletonList("xml").iterator();
/*  941 */       if (namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
/*  942 */         return Collections.singletonList("xmlns").iterator();
/*      */       }
/*      */ 
/*  945 */       return new Iterator() { private int i = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1;
/*  947 */         private boolean requireFindNext = true;
/*      */         private String p;
/*      */ 
/*  951 */         private String findNext() { while (this.i >= 0)
/*      */           {
/*  953 */             if (namespaceURI.equals(StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.i]))
/*      */             {
/*  956 */               if (StreamReaderBufferProcessor.InternalNamespaceContext.this.getNamespaceURI(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.i]).equals(StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.i]))
/*      */               {
/*  958 */                 return this.p = StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.i];
/*      */               }
/*      */             }
/*  961 */             this.i -= 1;
/*      */           }
/*  963 */           return this.p = null; }
/*      */ 
/*      */         public boolean hasNext()
/*      */         {
/*  967 */           if (this.requireFindNext) {
/*  968 */             findNext();
/*  969 */             this.requireFindNext = false;
/*      */           }
/*  971 */           return this.p != null;
/*      */         }
/*      */ 
/*      */         public Object next() {
/*  975 */           if (this.requireFindNext) {
/*  976 */             findNext();
/*      */           }
/*  978 */           this.requireFindNext = true;
/*      */ 
/*  980 */           if (this.p == null) {
/*  981 */             throw new NoSuchElementException();
/*      */           }
/*      */ 
/*  984 */           return this.p;
/*      */         }
/*      */ 
/*      */         public void remove() {
/*  988 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public Iterator<NamespaceContextEx.Binding> iterator()
/*      */     {
/* 1012 */       return new Iterator() { private final int end = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1;
/* 1014 */         private int current = this.end;
/* 1015 */         private boolean requireFindNext = true;
/*      */         private NamespaceContextEx.Binding namespace;
/*      */ 
/* 1019 */         private NamespaceContextEx.Binding findNext() { while (this.current >= 0) {
/* 1020 */             String prefix = StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.current];
/*      */ 
/* 1024 */             int i = this.end;
/* 1025 */             while ((i > this.current) && 
/* 1026 */               (!prefix.equals(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i]))) {
/* 1025 */               i--;
/*      */             }
/*      */ 
/* 1030 */             if (i == this.current--)
/*      */             {
/* 1032 */               return this.namespace = new StreamReaderBufferProcessor.InternalNamespaceContext.BindingImpl(StreamReaderBufferProcessor.InternalNamespaceContext.this, prefix, StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.current]);
/*      */             }
/*      */           }
/* 1035 */           return this.namespace = null; }
/*      */ 
/*      */         public boolean hasNext()
/*      */         {
/* 1039 */           if (this.requireFindNext) {
/* 1040 */             findNext();
/* 1041 */             this.requireFindNext = false;
/*      */           }
/* 1043 */           return this.namespace != null;
/*      */         }
/*      */ 
/*      */         public NamespaceContextEx.Binding next() {
/* 1047 */           if (this.requireFindNext) {
/* 1048 */             findNext();
/*      */           }
/* 1050 */           this.requireFindNext = true;
/*      */ 
/* 1052 */           if (this.namespace == null) {
/* 1053 */             throw new NoSuchElementException();
/*      */           }
/*      */ 
/* 1056 */           return this.namespace;
/*      */         }
/*      */ 
/*      */         public void remove() {
/* 1060 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     private class BindingImpl
/*      */       implements NamespaceContextEx.Binding
/*      */     {
/*      */       final String _prefix;
/*      */       final String _namespaceURI;
/*      */ 
/*      */       BindingImpl(String prefix, String namespaceURI)
/*      */       {
/*  998 */         this._prefix = prefix;
/*  999 */         this._namespaceURI = namespaceURI;
/*      */       }
/*      */ 
/*      */       public String getPrefix() {
/* 1003 */         return this._prefix;
/*      */       }
/*      */ 
/*      */       public String getNamespaceURI() {
/* 1007 */         return this._namespaceURI;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor
 * JD-Core Version:    0.6.2
 */