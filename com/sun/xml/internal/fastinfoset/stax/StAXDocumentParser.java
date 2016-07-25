/*      */ package com.sun.xml.internal.fastinfoset.stax;
/*      */ 
/*      */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*      */ import com.sun.xml.internal.fastinfoset.Decoder;
/*      */ import com.sun.xml.internal.fastinfoset.DecoderStateTables;
/*      */ import com.sun.xml.internal.fastinfoset.OctetBufferListener;
/*      */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BASE64EncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*      */ import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
/*      */ import com.sun.xml.internal.fastinfoset.sax.AttributesHolder;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayString;
/*      */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
/*      */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*      */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.xml.namespace.NamespaceContext;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.Location;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.stream.XMLStreamReader;
/*      */ 
/*      */ public class StAXDocumentParser extends Decoder
/*      */   implements XMLStreamReader, FastInfosetStreamReader, OctetBufferListener
/*      */ {
/*   71 */   private static final Logger logger = Logger.getLogger(StAXDocumentParser.class.getName());
/*      */   protected static final int INTERNAL_STATE_START_DOCUMENT = 0;
/*      */   protected static final int INTERNAL_STATE_START_ELEMENT_TERMINATE = 1;
/*      */   protected static final int INTERNAL_STATE_SINGLE_TERMINATE_ELEMENT_WITH_NAMESPACES = 2;
/*      */   protected static final int INTERNAL_STATE_DOUBLE_TERMINATE_ELEMENT = 3;
/*      */   protected static final int INTERNAL_STATE_END_DOCUMENT = 4;
/*      */   protected static final int INTERNAL_STATE_VOID = -1;
/*      */   protected int _internalState;
/*      */   protected int _eventType;
/*   90 */   protected QualifiedName[] _qNameStack = new QualifiedName[32];
/*   91 */   protected int[] _namespaceAIIsStartStack = new int[32];
/*   92 */   protected int[] _namespaceAIIsEndStack = new int[32];
/*   93 */   protected int _stackCount = -1;
/*      */ 
/*   95 */   protected String[] _namespaceAIIsPrefix = new String[32];
/*   96 */   protected String[] _namespaceAIIsNamespaceName = new String[32];
/*   97 */   protected int[] _namespaceAIIsPrefixIndex = new int[32];
/*      */   protected int _namespaceAIIsIndex;
/*      */   protected int _currentNamespaceAIIsStart;
/*      */   protected int _currentNamespaceAIIsEnd;
/*      */   protected QualifiedName _qualifiedName;
/*  114 */   protected AttributesHolder _attributes = new AttributesHolder();
/*      */ 
/*  116 */   protected boolean _clearAttributes = false;
/*      */   protected char[] _characters;
/*      */   protected int _charactersOffset;
/*      */   protected String _algorithmURI;
/*      */   protected int _algorithmId;
/*      */   protected boolean _isAlgorithmDataCloned;
/*      */   protected byte[] _algorithmData;
/*      */   protected int _algorithmDataOffset;
/*      */   protected int _algorithmDataLength;
/*      */   protected String _piTarget;
/*      */   protected String _piData;
/*  137 */   protected NamespaceContextImpl _nsContext = new NamespaceContextImpl();
/*      */   protected String _characterEncodingScheme;
/*      */   protected StAXManager _manager;
/* 1705 */   private byte[] base64TaleBytes = new byte[3];
/*      */   private int base64TaleLength;
/*      */ 
/*      */   public StAXDocumentParser()
/*      */   {
/*  144 */     reset();
/*  145 */     this._manager = new StAXManager(1);
/*      */   }
/*      */ 
/*      */   public StAXDocumentParser(InputStream s) {
/*  149 */     this();
/*  150 */     setInputStream(s);
/*  151 */     this._manager = new StAXManager(1);
/*      */   }
/*      */ 
/*      */   public StAXDocumentParser(InputStream s, StAXManager manager) {
/*  155 */     this(s);
/*  156 */     this._manager = manager;
/*      */   }
/*      */ 
/*      */   public void setInputStream(InputStream s)
/*      */   {
/*  161 */     super.setInputStream(s);
/*  162 */     reset();
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  167 */     super.reset();
/*  168 */     if ((this._internalState != 0) && (this._internalState != 4))
/*      */     {
/*  171 */       for (int i = this._namespaceAIIsIndex - 1; i >= 0; i--) {
/*  172 */         this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i]);
/*      */       }
/*      */ 
/*  175 */       this._stackCount = -1;
/*      */ 
/*  177 */       this._namespaceAIIsIndex = 0;
/*  178 */       this._characters = null;
/*  179 */       this._algorithmData = null;
/*      */     }
/*      */ 
/*  182 */     this._characterEncodingScheme = "UTF-8";
/*  183 */     this._eventType = 7;
/*  184 */     this._internalState = 0;
/*      */   }
/*      */ 
/*      */   protected void resetOnError() {
/*  188 */     super.reset();
/*      */ 
/*  190 */     if (this._v != null) {
/*  191 */       this._prefixTable.clearCompletely();
/*      */     }
/*  193 */     this._duplicateAttributeVerifier.clear();
/*      */ 
/*  195 */     this._stackCount = -1;
/*      */ 
/*  197 */     this._namespaceAIIsIndex = 0;
/*  198 */     this._characters = null;
/*  199 */     this._algorithmData = null;
/*      */ 
/*  201 */     this._eventType = 7;
/*  202 */     this._internalState = 0;
/*      */   }
/*      */ 
/*      */   public Object getProperty(String name)
/*      */     throws IllegalArgumentException
/*      */   {
/*  209 */     if (this._manager != null) {
/*  210 */       return this._manager.getProperty(name);
/*      */     }
/*  212 */     return null;
/*      */   }
/*      */ 
/*      */   public int next() throws XMLStreamException {
/*      */     try {
/*  217 */       if (this._internalState != -1) {
/*  218 */         switch (this._internalState) {
/*      */         case 0:
/*  220 */           decodeHeader();
/*  221 */           processDII();
/*      */ 
/*  223 */           this._internalState = -1;
/*  224 */           break;
/*      */         case 1:
/*  226 */           if (this._currentNamespaceAIIsEnd > 0) {
/*  227 */             for (int i = this._currentNamespaceAIIsEnd - 1; i >= this._currentNamespaceAIIsStart; i--) {
/*  228 */               this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i]);
/*      */             }
/*  230 */             this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
/*      */           }
/*      */ 
/*  234 */           popStack();
/*      */ 
/*  236 */           this._internalState = -1;
/*  237 */           return this._eventType = 2;
/*      */         case 2:
/*  240 */           for (int i = this._currentNamespaceAIIsEnd - 1; i >= this._currentNamespaceAIIsStart; i--) {
/*  241 */             this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i]);
/*      */           }
/*  243 */           this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
/*  244 */           this._internalState = -1;
/*  245 */           break;
/*      */         case 3:
/*  248 */           if (this._currentNamespaceAIIsEnd > 0) {
/*  249 */             for (int i = this._currentNamespaceAIIsEnd - 1; i >= this._currentNamespaceAIIsStart; i--) {
/*  250 */               this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i]);
/*      */             }
/*  252 */             this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
/*      */           }
/*      */ 
/*  255 */           if (this._stackCount == -1) {
/*  256 */             this._internalState = 4;
/*  257 */             return this._eventType = 8;
/*      */           }
/*      */ 
/*  261 */           popStack();
/*      */ 
/*  263 */           this._internalState = (this._currentNamespaceAIIsEnd > 0 ? 2 : -1);
/*      */ 
/*  266 */           return this._eventType = 2;
/*      */         case 4:
/*  268 */           throw new NoSuchElementException(CommonResourceBundle.getInstance().getString("message.noMoreEvents"));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  273 */       this._characters = null;
/*  274 */       this._algorithmData = null;
/*  275 */       this._currentNamespaceAIIsEnd = 0;
/*      */ 
/*  278 */       int b = read();
/*  279 */       switch (DecoderStateTables.EII(b)) {
/*      */       case 0:
/*  281 */         processEII(this._elementNameTable._array[b], false);
/*  282 */         return this._eventType;
/*      */       case 1:
/*  284 */         processEII(this._elementNameTable._array[(b & 0x1F)], true);
/*  285 */         return this._eventType;
/*      */       case 2:
/*  287 */         processEII(processEIIIndexMedium(b), (b & 0x40) > 0);
/*  288 */         return this._eventType;
/*      */       case 3:
/*  290 */         processEII(processEIIIndexLarge(b), (b & 0x40) > 0);
/*  291 */         return this._eventType;
/*      */       case 5:
/*  294 */         QualifiedName qn = processLiteralQualifiedName(b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  297 */         this._elementNameTable.add(qn);
/*  298 */         processEII(qn, (b & 0x40) > 0);
/*  299 */         return this._eventType;
/*      */       case 4:
/*  302 */         processEIIWithNamespaces((b & 0x40) > 0);
/*  303 */         return this._eventType;
/*      */       case 6:
/*  305 */         this._octetBufferLength = ((b & 0x1) + 1);
/*      */ 
/*  307 */         processUtf8CharacterString(b);
/*  308 */         return this._eventType = 4;
/*      */       case 7:
/*  310 */         this._octetBufferLength = (read() + 3);
/*  311 */         processUtf8CharacterString(b);
/*  312 */         return this._eventType = 4;
/*      */       case 8:
/*  314 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  319 */         processUtf8CharacterString(b);
/*  320 */         return this._eventType = 4;
/*      */       case 9:
/*  322 */         this._octetBufferLength = ((b & 0x1) + 1);
/*      */ 
/*  324 */         processUtf16CharacterString(b);
/*  325 */         return this._eventType = 4;
/*      */       case 10:
/*  327 */         this._octetBufferLength = (read() + 3);
/*  328 */         processUtf16CharacterString(b);
/*  329 */         return this._eventType = 4;
/*      */       case 11:
/*  331 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 259);
/*      */ 
/*  336 */         processUtf16CharacterString(b);
/*  337 */         return this._eventType = 4;
/*      */       case 12:
/*  340 */         boolean addToTable = (b & 0x10) > 0;
/*      */ 
/*  342 */         this._identifier = ((b & 0x2) << 6);
/*  343 */         int b2 = read();
/*  344 */         this._identifier |= (b2 & 0xFC) >> 2;
/*      */ 
/*  346 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(b2);
/*      */ 
/*  348 */         decodeRestrictedAlphabetAsCharBuffer();
/*      */ 
/*  350 */         if (addToTable) {
/*  351 */           this._charactersOffset = this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*  352 */           this._characters = this._characterContentChunkTable._array;
/*      */         } else {
/*  354 */           this._characters = this._charBuffer;
/*  355 */           this._charactersOffset = 0;
/*      */         }
/*  357 */         return this._eventType = 4;
/*      */       case 13:
/*  361 */         boolean addToTable = (b & 0x10) > 0;
/*      */ 
/*  363 */         this._algorithmId = ((b & 0x2) << 6);
/*  364 */         int b2 = read();
/*  365 */         this._algorithmId |= (b2 & 0xFC) >> 2;
/*      */ 
/*  367 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(b2);
/*  368 */         processCIIEncodingAlgorithm(addToTable);
/*      */ 
/*  370 */         if (this._algorithmId == 9) {
/*  371 */           return this._eventType = 12;
/*      */         }
/*      */ 
/*  374 */         return this._eventType = 4;
/*      */       case 14:
/*  378 */         int index = b & 0xF;
/*  379 */         this._characterContentChunkTable._cachedIndex = index;
/*      */ 
/*  381 */         this._characters = this._characterContentChunkTable._array;
/*  382 */         this._charactersOffset = this._characterContentChunkTable._offset[index];
/*  383 */         this._charBufferLength = this._characterContentChunkTable._length[index];
/*  384 */         return this._eventType = 4;
/*      */       case 15:
/*  388 */         int index = ((b & 0x3) << 8 | read()) + 16;
/*      */ 
/*  390 */         this._characterContentChunkTable._cachedIndex = index;
/*      */ 
/*  392 */         this._characters = this._characterContentChunkTable._array;
/*  393 */         this._charactersOffset = this._characterContentChunkTable._offset[index];
/*  394 */         this._charBufferLength = this._characterContentChunkTable._length[index];
/*  395 */         return this._eventType = 4;
/*      */       case 16:
/*  399 */         int index = ((b & 0x3) << 16 | read() << 8 | read()) + 1040;
/*      */ 
/*  403 */         this._characterContentChunkTable._cachedIndex = index;
/*      */ 
/*  405 */         this._characters = this._characterContentChunkTable._array;
/*  406 */         this._charactersOffset = this._characterContentChunkTable._offset[index];
/*  407 */         this._charBufferLength = this._characterContentChunkTable._length[index];
/*  408 */         return this._eventType = 4;
/*      */       case 17:
/*  412 */         int index = (read() << 16 | read() << 8 | read()) + 263184;
/*      */ 
/*  416 */         this._characterContentChunkTable._cachedIndex = index;
/*      */ 
/*  418 */         this._characters = this._characterContentChunkTable._array;
/*  419 */         this._charactersOffset = this._characterContentChunkTable._offset[index];
/*  420 */         this._charBufferLength = this._characterContentChunkTable._length[index];
/*  421 */         return this._eventType = 4;
/*      */       case 18:
/*  424 */         processCommentII();
/*  425 */         return this._eventType;
/*      */       case 19:
/*  427 */         processProcessingII();
/*  428 */         return this._eventType;
/*      */       case 21:
/*  431 */         processUnexpandedEntityReference(b);
/*      */ 
/*  433 */         return next();
/*      */       case 23:
/*  436 */         if (this._stackCount != -1)
/*      */         {
/*  438 */           popStack();
/*      */ 
/*  440 */           this._internalState = 3;
/*  441 */           return this._eventType = 2;
/*      */         }
/*      */ 
/*  444 */         this._internalState = 4;
/*  445 */         return this._eventType = 8;
/*      */       case 22:
/*  447 */         if (this._stackCount != -1)
/*      */         {
/*  449 */           popStack();
/*      */ 
/*  451 */           if (this._currentNamespaceAIIsEnd > 0) {
/*  452 */             this._internalState = 2;
/*      */           }
/*  454 */           return this._eventType = 2;
/*      */         }
/*      */ 
/*  457 */         this._internalState = 4;
/*  458 */         return this._eventType = 8;
/*      */       case 20:
/*  460 */       }throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
/*      */     }
/*      */     catch (IOException e) {
/*  463 */       resetOnError();
/*  464 */       logger.log(Level.FINE, "next() exception", e);
/*  465 */       throw new XMLStreamException(e);
/*      */     } catch (FastInfosetException e) {
/*  467 */       resetOnError();
/*  468 */       logger.log(Level.FINE, "next() exception", e);
/*  469 */       throw new XMLStreamException(e);
/*      */     } catch (RuntimeException e) {
/*  471 */       resetOnError();
/*  472 */       logger.log(Level.FINE, "next() exception", e);
/*  473 */       throw e;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void processUtf8CharacterString(int b) throws IOException {
/*  478 */     if ((b & 0x10) > 0) {
/*  479 */       this._characterContentChunkTable.ensureSize(this._octetBufferLength);
/*  480 */       this._characters = this._characterContentChunkTable._array;
/*  481 */       this._charactersOffset = this._characterContentChunkTable._arrayIndex;
/*  482 */       decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, this._charactersOffset);
/*  483 */       this._characterContentChunkTable.add(this._charBufferLength);
/*      */     } else {
/*  485 */       decodeUtf8StringAsCharBuffer();
/*  486 */       this._characters = this._charBuffer;
/*  487 */       this._charactersOffset = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void processUtf16CharacterString(int b) throws IOException {
/*  492 */     decodeUtf16StringAsCharBuffer();
/*  493 */     if ((b & 0x10) > 0) {
/*  494 */       this._charactersOffset = this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*  495 */       this._characters = this._characterContentChunkTable._array;
/*      */     } else {
/*  497 */       this._characters = this._charBuffer;
/*  498 */       this._charactersOffset = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void popStack()
/*      */   {
/*  504 */     this._qualifiedName = this._qNameStack[this._stackCount];
/*  505 */     this._currentNamespaceAIIsStart = this._namespaceAIIsStartStack[this._stackCount];
/*  506 */     this._currentNamespaceAIIsEnd = this._namespaceAIIsEndStack[this._stackCount];
/*  507 */     this._qNameStack[(this._stackCount--)] = null;
/*      */   }
/*      */ 
/*      */   public final void require(int type, String namespaceURI, String localName)
/*      */     throws XMLStreamException
/*      */   {
/*  519 */     if (type != this._eventType)
/*  520 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.eventTypeNotMatch", new Object[] { getEventTypeString(type) }));
/*  521 */     if ((namespaceURI != null) && (!namespaceURI.equals(getNamespaceURI())))
/*  522 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.namespaceURINotMatch", new Object[] { namespaceURI }));
/*  523 */     if ((localName != null) && (!localName.equals(getLocalName())))
/*  524 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.localNameNotMatch", new Object[] { localName }));
/*      */   }
/*      */ 
/*      */   public final String getElementText()
/*      */     throws XMLStreamException
/*      */   {
/*  537 */     if (getEventType() != 1) {
/*  538 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTARTELEMENT"), getLocation());
/*      */     }
/*      */ 
/*  542 */     next();
/*  543 */     return getElementText(true);
/*      */   }
/*      */ 
/*      */   public final String getElementText(boolean startElementRead)
/*      */     throws XMLStreamException
/*      */   {
/*  549 */     if (!startElementRead) {
/*  550 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTARTELEMENT"), getLocation());
/*      */     }
/*      */ 
/*  553 */     int eventType = getEventType();
/*  554 */     StringBuffer content = new StringBuffer();
/*  555 */     while (eventType != 2) {
/*  556 */       if ((eventType == 4) || (eventType == 12) || (eventType == 6) || (eventType == 9))
/*      */       {
/*  560 */         content.append(getText());
/*  561 */       } else if ((eventType != 3) && (eventType != 5))
/*      */       {
/*  564 */         if (eventType == 8)
/*  565 */           throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.unexpectedEOF"));
/*  566 */         if (eventType == 1) {
/*  567 */           throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.getElementTextExpectTextOnly"), getLocation());
/*      */         }
/*      */ 
/*  570 */         throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.unexpectedEventType") + getEventTypeString(eventType), getLocation());
/*      */       }
/*      */ 
/*  573 */       eventType = next();
/*      */     }
/*  575 */     return content.toString();
/*      */   }
/*      */ 
/*      */   public final int nextTag()
/*      */     throws XMLStreamException
/*      */   {
/*  592 */     next();
/*  593 */     return nextTag(true);
/*      */   }
/*      */ 
/*      */   public final int nextTag(boolean currentTagRead)
/*      */     throws XMLStreamException
/*      */   {
/*  599 */     int eventType = getEventType();
/*  600 */     if (!currentTagRead) {
/*  601 */       eventType = next();
/*      */     }
/*      */ 
/*  607 */     while (((eventType == 4) && (isWhiteSpace())) || ((eventType == 12) && (isWhiteSpace())) || (eventType == 6) || (eventType == 3) || (eventType == 5)) {
/*  608 */       eventType = next();
/*      */     }
/*  610 */     if ((eventType != 1) && (eventType != 2)) {
/*  611 */       throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.expectedStartOrEnd"), getLocation());
/*      */     }
/*  613 */     return eventType;
/*      */   }
/*      */ 
/*      */   public final boolean hasNext() throws XMLStreamException {
/*  617 */     return this._eventType != 8;
/*      */   }
/*      */ 
/*      */   public void close() throws XMLStreamException {
/*      */     try {
/*  622 */       super.closeIfRequired();
/*      */     } catch (IOException ex) {
/*      */     }
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI(String prefix) {
/*  628 */     String namespace = getNamespaceDecl(prefix);
/*  629 */     if (namespace == null) {
/*  630 */       if (prefix == null) {
/*  631 */         throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.nullPrefix"));
/*      */       }
/*  633 */       return null;
/*      */     }
/*  635 */     return namespace;
/*      */   }
/*      */ 
/*      */   public final boolean isStartElement() {
/*  639 */     return this._eventType == 1;
/*      */   }
/*      */ 
/*      */   public final boolean isEndElement() {
/*  643 */     return this._eventType == 2;
/*      */   }
/*      */ 
/*      */   public final boolean isCharacters() {
/*  647 */     return this._eventType == 4;
/*      */   }
/*      */ 
/*      */   public final boolean isWhiteSpace()
/*      */   {
/*  657 */     if ((isCharacters()) || (this._eventType == 12)) {
/*  658 */       char[] ch = getTextCharacters();
/*  659 */       int start = getTextStart();
/*  660 */       int length = getTextLength();
/*  661 */       for (int i = start; i < start + length; i++) {
/*  662 */         if (!XMLChar.isSpace(ch[i])) {
/*  663 */           return false;
/*      */         }
/*      */       }
/*  666 */       return true;
/*      */     }
/*  668 */     return false;
/*      */   }
/*      */ 
/*      */   public final String getAttributeValue(String namespaceURI, String localName) {
/*  672 */     if (this._eventType != 1) {
/*  673 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*      */ 
/*  676 */     if (localName == null) {
/*  677 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*  680 */     if (namespaceURI != null) {
/*  681 */       for (int i = 0; i < this._attributes.getLength(); i++)
/*  682 */         if ((this._attributes.getLocalName(i).equals(localName)) && (this._attributes.getURI(i).equals(namespaceURI)))
/*      */         {
/*  684 */           return this._attributes.getValue(i);
/*      */         }
/*      */     }
/*      */     else {
/*  688 */       for (int i = 0; i < this._attributes.getLength(); i++) {
/*  689 */         if (this._attributes.getLocalName(i).equals(localName)) {
/*  690 */           return this._attributes.getValue(i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  695 */     return null;
/*      */   }
/*      */ 
/*      */   public final int getAttributeCount() {
/*  699 */     if (this._eventType != 1) {
/*  700 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*      */ 
/*  703 */     return this._attributes.getLength();
/*      */   }
/*      */ 
/*      */   public final QName getAttributeName(int index) {
/*  707 */     if (this._eventType != 1) {
/*  708 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  710 */     return this._attributes.getQualifiedName(index).getQName();
/*      */   }
/*      */ 
/*      */   public final String getAttributeNamespace(int index) {
/*  714 */     if (this._eventType != 1) {
/*  715 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*      */ 
/*  718 */     return this._attributes.getURI(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributeLocalName(int index) {
/*  722 */     if (this._eventType != 1) {
/*  723 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  725 */     return this._attributes.getLocalName(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributePrefix(int index) {
/*  729 */     if (this._eventType != 1) {
/*  730 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  732 */     return this._attributes.getPrefix(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributeType(int index) {
/*  736 */     if (this._eventType != 1) {
/*  737 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  739 */     return this._attributes.getType(index);
/*      */   }
/*      */ 
/*      */   public final String getAttributeValue(int index) {
/*  743 */     if (this._eventType != 1) {
/*  744 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  746 */     return this._attributes.getValue(index);
/*      */   }
/*      */ 
/*      */   public final boolean isAttributeSpecified(int index) {
/*  750 */     return false;
/*      */   }
/*      */ 
/*      */   public final int getNamespaceCount() {
/*  754 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  755 */       return this._currentNamespaceAIIsEnd > 0 ? this._currentNamespaceAIIsEnd - this._currentNamespaceAIIsStart : 0;
/*      */     }
/*  757 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespaceCount"));
/*      */   }
/*      */ 
/*      */   public final String getNamespacePrefix(int index)
/*      */   {
/*  762 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  763 */       return this._namespaceAIIsPrefix[(this._currentNamespaceAIIsStart + index)];
/*      */     }
/*  765 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespacePrefix"));
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI(int index)
/*      */   {
/*  770 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  771 */       return this._namespaceAIIsNamespaceName[(this._currentNamespaceAIIsStart + index)];
/*      */     }
/*  773 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespacePrefix"));
/*      */   }
/*      */ 
/*      */   public final NamespaceContext getNamespaceContext()
/*      */   {
/*  778 */     return this._nsContext;
/*      */   }
/*      */ 
/*      */   public final int getEventType() {
/*  782 */     return this._eventType;
/*      */   }
/*      */ 
/*      */   public final String getText() {
/*  786 */     if (this._characters == null) {
/*  787 */       checkTextState();
/*      */     }
/*      */ 
/*  790 */     if (this._characters == this._characterContentChunkTable._array) {
/*  791 */       return this._characterContentChunkTable.getString(this._characterContentChunkTable._cachedIndex);
/*      */     }
/*  793 */     return new String(this._characters, this._charactersOffset, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   public final char[] getTextCharacters()
/*      */   {
/*  798 */     if (this._characters == null) {
/*  799 */       checkTextState();
/*      */     }
/*      */ 
/*  802 */     return this._characters;
/*      */   }
/*      */ 
/*      */   public final int getTextStart() {
/*  806 */     if (this._characters == null) {
/*  807 */       checkTextState();
/*      */     }
/*      */ 
/*  810 */     return this._charactersOffset;
/*      */   }
/*      */ 
/*      */   public final int getTextLength() {
/*  814 */     if (this._characters == null) {
/*  815 */       checkTextState();
/*      */     }
/*      */ 
/*  818 */     return this._charBufferLength;
/*      */   }
/*      */ 
/*      */   public final int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException
/*      */   {
/*  823 */     if (this._characters == null) {
/*  824 */       checkTextState();
/*      */     }
/*      */     try
/*      */     {
/*  828 */       int bytesToCopy = Math.min(this._charBufferLength, length);
/*  829 */       System.arraycopy(this._characters, this._charactersOffset + sourceStart, target, targetStart, bytesToCopy);
/*      */ 
/*  831 */       return bytesToCopy;
/*      */     } catch (IndexOutOfBoundsException e) {
/*  833 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void checkTextState() {
/*  838 */     if (this._algorithmData == null) {
/*  839 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.InvalidStateForText"));
/*      */     }
/*      */     try
/*      */     {
/*  843 */       convertEncodingAlgorithmDataToCharacters();
/*      */     } catch (Exception e) {
/*  845 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.InvalidStateForText"));
/*      */     }
/*      */   }
/*      */ 
/*      */   public final String getEncoding() {
/*  850 */     return this._characterEncodingScheme;
/*      */   }
/*      */ 
/*      */   public final boolean hasText() {
/*  854 */     return this._characters != null;
/*      */   }
/*      */ 
/*      */   public final Location getLocation()
/*      */   {
/*  860 */     return EventLocation.getNilLocation();
/*      */   }
/*      */ 
/*      */   public final QName getName() {
/*  864 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  865 */       return this._qualifiedName.getQName();
/*      */     }
/*  867 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetName"));
/*      */   }
/*      */ 
/*      */   public final String getLocalName()
/*      */   {
/*  872 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  873 */       return this._qualifiedName.localName;
/*      */     }
/*  875 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetLocalName"));
/*      */   }
/*      */ 
/*      */   public final boolean hasName()
/*      */   {
/*  880 */     return (this._eventType == 1) || (this._eventType == 2);
/*      */   }
/*      */ 
/*      */   public final String getNamespaceURI() {
/*  884 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  885 */       return this._qualifiedName.namespaceName;
/*      */     }
/*  887 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespaceURI"));
/*      */   }
/*      */ 
/*      */   public final String getPrefix()
/*      */   {
/*  892 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  893 */       return this._qualifiedName.prefix;
/*      */     }
/*  895 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPrefix"));
/*      */   }
/*      */ 
/*      */   public final String getVersion()
/*      */   {
/*  900 */     return null;
/*      */   }
/*      */ 
/*      */   public final boolean isStandalone() {
/*  904 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean standaloneSet() {
/*  908 */     return false;
/*      */   }
/*      */ 
/*      */   public final String getCharacterEncodingScheme() {
/*  912 */     return null;
/*      */   }
/*      */ 
/*      */   public final String getPITarget() {
/*  916 */     if (this._eventType != 3) {
/*  917 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPITarget"));
/*      */     }
/*      */ 
/*  920 */     return this._piTarget;
/*      */   }
/*      */ 
/*      */   public final String getPIData() {
/*  924 */     if (this._eventType != 3) {
/*  925 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPIData"));
/*      */     }
/*      */ 
/*  928 */     return this._piData;
/*      */   }
/*      */ 
/*      */   public final String getNameString()
/*      */   {
/*  935 */     if ((this._eventType == 1) || (this._eventType == 2)) {
/*  936 */       return this._qualifiedName.getQNameString();
/*      */     }
/*  938 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetName"));
/*      */   }
/*      */ 
/*      */   public final String getAttributeNameString(int index)
/*      */   {
/*  943 */     if (this._eventType != 1) {
/*  944 */       throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
/*      */     }
/*  946 */     return this._attributes.getQualifiedName(index).getQNameString();
/*      */   }
/*      */ 
/*      */   public final String getTextAlgorithmURI()
/*      */   {
/*  951 */     return this._algorithmURI;
/*      */   }
/*      */ 
/*      */   public final int getTextAlgorithmIndex() {
/*  955 */     return this._algorithmId;
/*      */   }
/*      */ 
/*      */   public final byte[] getTextAlgorithmBytes() {
/*  959 */     return this._algorithmData;
/*      */   }
/*      */ 
/*      */   public final byte[] getTextAlgorithmBytesClone() {
/*  963 */     if (this._algorithmData == null) {
/*  964 */       return null;
/*      */     }
/*      */ 
/*  967 */     byte[] algorithmData = new byte[this._algorithmDataLength];
/*  968 */     System.arraycopy(this._algorithmData, this._algorithmDataOffset, algorithmData, 0, this._algorithmDataLength);
/*  969 */     return algorithmData;
/*      */   }
/*      */ 
/*      */   public final int getTextAlgorithmStart() {
/*  973 */     return this._algorithmDataOffset;
/*      */   }
/*      */ 
/*      */   public final int getTextAlgorithmLength() {
/*  977 */     return this._algorithmDataLength;
/*      */   }
/*      */ 
/*      */   public final int getTextAlgorithmBytes(int sourceStart, byte[] target, int targetStart, int length) throws XMLStreamException
/*      */   {
/*      */     try {
/*  983 */       System.arraycopy(this._algorithmData, sourceStart, target, targetStart, length);
/*      */ 
/*  985 */       return length;
/*      */     } catch (IndexOutOfBoundsException e) {
/*  987 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int peekNext() throws XMLStreamException
/*      */   {
/*      */     try
/*      */     {
/*  995 */       switch (DecoderStateTables.EII(peek(this))) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/* 1002 */         return 1;
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
/* 1015 */         return 4;
/*      */       case 18:
/* 1017 */         return 5;
/*      */       case 19:
/* 1019 */         return 3;
/*      */       case 21:
/* 1021 */         return 9;
/*      */       case 22:
/*      */       case 23:
/* 1024 */         return this._stackCount != -1 ? 2 : 8;
/*      */       case 20:
/* 1026 */       }throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1030 */       throw new XMLStreamException(e);
/*      */     } catch (FastInfosetException e) {
/* 1032 */       throw new XMLStreamException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void onBeforeOctetBufferOverwrite() {
/* 1037 */     if (this._algorithmData != null) {
/* 1038 */       this._algorithmData = getTextAlgorithmBytesClone();
/* 1039 */       this._algorithmDataOffset = 0;
/* 1040 */       this._isAlgorithmDataCloned = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int accessNamespaceCount()
/*      */   {
/* 1047 */     return this._currentNamespaceAIIsEnd > 0 ? this._currentNamespaceAIIsEnd - this._currentNamespaceAIIsStart : 0;
/*      */   }
/*      */ 
/*      */   public final String accessLocalName() {
/* 1051 */     return this._qualifiedName.localName;
/*      */   }
/*      */ 
/*      */   public final String accessNamespaceURI() {
/* 1055 */     return this._qualifiedName.namespaceName;
/*      */   }
/*      */ 
/*      */   public final String accessPrefix() {
/* 1059 */     return this._qualifiedName.prefix;
/*      */   }
/*      */ 
/*      */   public final char[] accessTextCharacters() {
/* 1063 */     return this._characters;
/*      */   }
/*      */ 
/*      */   public final int accessTextStart() {
/* 1067 */     return this._charactersOffset;
/*      */   }
/*      */ 
/*      */   public final int accessTextLength() {
/* 1071 */     return this._charBufferLength;
/*      */   }
/*      */ 
/*      */   protected final void processDII()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1077 */     int b = read();
/* 1078 */     if (b > 0)
/* 1079 */       processDIIOptionalProperties(b);
/*      */   }
/*      */ 
/*      */   protected final void processDIIOptionalProperties(int b)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1085 */     if (b == 32) {
/* 1086 */       decodeInitialVocabulary();
/* 1087 */       return;
/*      */     }
/*      */ 
/* 1090 */     if ((b & 0x40) > 0) {
/* 1091 */       decodeAdditionalData();
/*      */     }
/*      */ 
/* 1098 */     if ((b & 0x20) > 0) {
/* 1099 */       decodeInitialVocabulary();
/*      */     }
/*      */ 
/* 1102 */     if ((b & 0x10) > 0) {
/* 1103 */       decodeNotations();
/*      */     }
/*      */ 
/* 1113 */     if ((b & 0x8) > 0) {
/* 1114 */       decodeUnparsedEntities();
/*      */     }
/*      */ 
/* 1124 */     if ((b & 0x4) > 0)
/* 1125 */       this._characterEncodingScheme = decodeCharacterEncodingScheme();
/*      */     boolean standalone;
/* 1128 */     if ((b & 0x2) > 0) {
/* 1129 */       standalone = read() > 0;
/*      */     }
/*      */ 
/* 1136 */     if ((b & 0x1) > 0)
/* 1137 */       decodeVersion();
/*      */   }
/*      */ 
/*      */   protected final void resizeNamespaceAIIs()
/*      */   {
/* 1147 */     String[] namespaceAIIsPrefix = new String[this._namespaceAIIsIndex * 2];
/* 1148 */     System.arraycopy(this._namespaceAIIsPrefix, 0, namespaceAIIsPrefix, 0, this._namespaceAIIsIndex);
/* 1149 */     this._namespaceAIIsPrefix = namespaceAIIsPrefix;
/*      */ 
/* 1151 */     String[] namespaceAIIsNamespaceName = new String[this._namespaceAIIsIndex * 2];
/* 1152 */     System.arraycopy(this._namespaceAIIsNamespaceName, 0, namespaceAIIsNamespaceName, 0, this._namespaceAIIsIndex);
/* 1153 */     this._namespaceAIIsNamespaceName = namespaceAIIsNamespaceName;
/*      */ 
/* 1155 */     int[] namespaceAIIsPrefixIndex = new int[this._namespaceAIIsIndex * 2];
/* 1156 */     System.arraycopy(this._namespaceAIIsPrefixIndex, 0, namespaceAIIsPrefixIndex, 0, this._namespaceAIIsIndex);
/* 1157 */     this._namespaceAIIsPrefixIndex = namespaceAIIsPrefixIndex;
/*      */   }
/*      */ 
/*      */   protected final void processEIIWithNamespaces(boolean hasAttributes) throws FastInfosetException, IOException {
/* 1161 */     if (++this._prefixTable._declarationId == 2147483647) {
/* 1162 */       this._prefixTable.clearDeclarationIds();
/*      */     }
/*      */ 
/* 1165 */     this._currentNamespaceAIIsStart = this._namespaceAIIsIndex;
/* 1166 */     String prefix = ""; String namespaceName = "";
/* 1167 */     int b = read();
/* 1168 */     while ((b & 0xFC) == 204) {
/* 1169 */       if (this._namespaceAIIsIndex == this._namespaceAIIsPrefix.length) {
/* 1170 */         resizeNamespaceAIIs();
/*      */       }
/*      */ 
/* 1173 */       switch (b & 0x3)
/*      */       {
/*      */       case 0:
/* 1177 */         prefix = namespaceName = this._namespaceAIIsPrefix[this._namespaceAIIsIndex] =  = this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] =  = "";
/*      */ 
/* 1181 */         this._namespaceNameIndex = (this._prefixIndex = this._namespaceAIIsPrefixIndex[(this._namespaceAIIsIndex++)] = -1);
/* 1182 */         break;
/*      */       case 1:
/* 1186 */         prefix = this._namespaceAIIsPrefix[this._namespaceAIIsIndex] =  = "";
/* 1187 */         namespaceName = this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] =  = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false);
/*      */ 
/* 1190 */         this._prefixIndex = (this._namespaceAIIsPrefixIndex[(this._namespaceAIIsIndex++)] = -1);
/* 1191 */         break;
/*      */       case 2:
/* 1195 */         prefix = this._namespaceAIIsPrefix[this._namespaceAIIsIndex] =  = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
/*      */ 
/* 1197 */         namespaceName = this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] =  = "";
/*      */ 
/* 1199 */         this._namespaceNameIndex = -1;
/* 1200 */         this._namespaceAIIsPrefixIndex[(this._namespaceAIIsIndex++)] = this._prefixIndex;
/* 1201 */         break;
/*      */       case 3:
/* 1205 */         prefix = this._namespaceAIIsPrefix[this._namespaceAIIsIndex] =  = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
/*      */ 
/* 1207 */         namespaceName = this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] =  = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true);
/*      */ 
/* 1210 */         this._namespaceAIIsPrefixIndex[(this._namespaceAIIsIndex++)] = this._prefixIndex;
/*      */       }
/*      */ 
/* 1215 */       this._prefixTable.pushScopeWithPrefixEntry(prefix, namespaceName, this._prefixIndex, this._namespaceNameIndex);
/*      */ 
/* 1217 */       b = read();
/*      */     }
/* 1219 */     if (b != 240) {
/* 1220 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
/*      */     }
/* 1222 */     this._currentNamespaceAIIsEnd = this._namespaceAIIsIndex;
/*      */ 
/* 1224 */     b = read();
/* 1225 */     switch (DecoderStateTables.EII(b)) {
/*      */     case 0:
/* 1227 */       processEII(this._elementNameTable._array[b], hasAttributes);
/* 1228 */       break;
/*      */     case 2:
/* 1230 */       processEII(processEIIIndexMedium(b), hasAttributes);
/* 1231 */       break;
/*      */     case 3:
/* 1233 */       processEII(processEIIIndexLarge(b), hasAttributes);
/* 1234 */       break;
/*      */     case 5:
/* 1237 */       QualifiedName qn = processLiteralQualifiedName(b & 0x3, this._elementNameTable.getNext());
/*      */ 
/* 1240 */       this._elementNameTable.add(qn);
/* 1241 */       processEII(qn, hasAttributes);
/* 1242 */       break;
/*      */     case 1:
/*      */     case 4:
/*      */     default:
/* 1245 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processEII(QualifiedName name, boolean hasAttributes) throws FastInfosetException, IOException {
/* 1250 */     if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
/* 1251 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qnameOfEIINotInScope"));
/*      */     }
/*      */ 
/* 1254 */     this._eventType = 1;
/* 1255 */     this._qualifiedName = name;
/*      */ 
/* 1257 */     if (this._clearAttributes) {
/* 1258 */       this._attributes.clear();
/* 1259 */       this._clearAttributes = false;
/*      */     }
/*      */ 
/* 1262 */     if (hasAttributes) {
/* 1263 */       processAIIs();
/*      */     }
/*      */ 
/* 1267 */     this._stackCount += 1;
/* 1268 */     if (this._stackCount == this._qNameStack.length) {
/* 1269 */       QualifiedName[] qNameStack = new QualifiedName[this._qNameStack.length * 2];
/* 1270 */       System.arraycopy(this._qNameStack, 0, qNameStack, 0, this._qNameStack.length);
/* 1271 */       this._qNameStack = qNameStack;
/*      */ 
/* 1273 */       int[] namespaceAIIsStartStack = new int[this._namespaceAIIsStartStack.length * 2];
/* 1274 */       System.arraycopy(this._namespaceAIIsStartStack, 0, namespaceAIIsStartStack, 0, this._namespaceAIIsStartStack.length);
/* 1275 */       this._namespaceAIIsStartStack = namespaceAIIsStartStack;
/*      */ 
/* 1277 */       int[] namespaceAIIsEndStack = new int[this._namespaceAIIsEndStack.length * 2];
/* 1278 */       System.arraycopy(this._namespaceAIIsEndStack, 0, namespaceAIIsEndStack, 0, this._namespaceAIIsEndStack.length);
/* 1279 */       this._namespaceAIIsEndStack = namespaceAIIsEndStack;
/*      */     }
/* 1281 */     this._qNameStack[this._stackCount] = this._qualifiedName;
/* 1282 */     this._namespaceAIIsStartStack[this._stackCount] = this._currentNamespaceAIIsStart;
/* 1283 */     this._namespaceAIIsEndStack[this._stackCount] = this._currentNamespaceAIIsEnd;
/*      */   }
/*      */ 
/*      */   protected final void processAIIs()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1291 */     if (++this._duplicateAttributeVerifier._currentIteration == 2147483647) {
/* 1292 */       this._duplicateAttributeVerifier.clear();
/*      */     }
/*      */ 
/* 1295 */     this._clearAttributes = true;
/* 1296 */     boolean terminate = false;
/*      */     do
/*      */     {
/* 1299 */       int b = read();
/*      */       QualifiedName name;
/* 1300 */       switch (DecoderStateTables.AII(b)) {
/*      */       case 0:
/* 1302 */         name = this._attributeNameTable._array[b];
/* 1303 */         break;
/*      */       case 1:
/* 1306 */         int i = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/* 1308 */         name = this._attributeNameTable._array[i];
/* 1309 */         break;
/*      */       case 2:
/* 1313 */         int i = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/* 1315 */         name = this._attributeNameTable._array[i];
/* 1316 */         break;
/*      */       case 3:
/* 1319 */         name = processLiteralQualifiedName(b & 0x3, this._attributeNameTable.getNext());
/*      */ 
/* 1322 */         name.createAttributeValues(256);
/* 1323 */         this._attributeNameTable.add(name);
/* 1324 */         break;
/*      */       case 5:
/* 1326 */         this._internalState = 1;
/*      */       case 4:
/* 1328 */         terminate = true;
/*      */ 
/* 1330 */         break;
/*      */       default:
/* 1332 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingAIIs"));
/*      */       }
/*      */ 
/* 1337 */       if ((name.prefixIndex > 0) && (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex)) {
/* 1338 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.AIIqNameNotInScope"));
/*      */       }
/*      */ 
/* 1341 */       this._duplicateAttributeVerifier.checkForDuplicateAttribute(name.attributeHash, name.attributeId);
/*      */ 
/* 1343 */       b = read();
/*      */       String value;
/* 1344 */       switch (DecoderStateTables.NISTRING(b)) {
/*      */       case 0:
/* 1346 */         this._octetBufferLength = ((b & 0x7) + 1);
/* 1347 */         value = decodeUtf8StringAsString();
/* 1348 */         if ((b & 0x40) > 0) {
/* 1349 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1352 */         this._attributes.addAttribute(name, value);
/* 1353 */         break;
/*      */       case 1:
/* 1355 */         this._octetBufferLength = (read() + 9);
/* 1356 */         value = decodeUtf8StringAsString();
/* 1357 */         if ((b & 0x40) > 0) {
/* 1358 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1361 */         this._attributes.addAttribute(name, value);
/* 1362 */         break;
/*      */       case 2:
/* 1364 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 265);
/*      */ 
/* 1369 */         value = decodeUtf8StringAsString();
/* 1370 */         if ((b & 0x40) > 0) {
/* 1371 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1374 */         this._attributes.addAttribute(name, value);
/* 1375 */         break;
/*      */       case 3:
/* 1377 */         this._octetBufferLength = ((b & 0x7) + 1);
/* 1378 */         value = decodeUtf16StringAsString();
/* 1379 */         if ((b & 0x40) > 0) {
/* 1380 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1383 */         this._attributes.addAttribute(name, value);
/* 1384 */         break;
/*      */       case 4:
/* 1386 */         this._octetBufferLength = (read() + 9);
/* 1387 */         value = decodeUtf16StringAsString();
/* 1388 */         if ((b & 0x40) > 0) {
/* 1389 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1392 */         this._attributes.addAttribute(name, value);
/* 1393 */         break;
/*      */       case 5:
/* 1395 */         this._octetBufferLength = ((read() << 24 | read() << 16 | read() << 8 | read()) + 265);
/*      */ 
/* 1400 */         value = decodeUtf16StringAsString();
/* 1401 */         if ((b & 0x40) > 0) {
/* 1402 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1405 */         this._attributes.addAttribute(name, value);
/* 1406 */         break;
/*      */       case 6:
/* 1409 */         boolean addToTable = (b & 0x40) > 0;
/*      */ 
/* 1411 */         this._identifier = ((b & 0xF) << 4);
/* 1412 */         b = read();
/* 1413 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/* 1415 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/*      */ 
/* 1417 */         value = decodeRestrictedAlphabetAsString();
/* 1418 */         if (addToTable) {
/* 1419 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/* 1422 */         this._attributes.addAttribute(name, value);
/* 1423 */         break;
/*      */       case 7:
/* 1427 */         boolean addToTable = (b & 0x40) > 0;
/*      */ 
/* 1429 */         this._identifier = ((b & 0xF) << 4);
/* 1430 */         b = read();
/* 1431 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/* 1433 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/* 1434 */         processAIIEncodingAlgorithm(name, addToTable);
/* 1435 */         break;
/*      */       case 8:
/* 1438 */         this._attributes.addAttribute(name, this._attributeValueTable._array[(b & 0x3F)]);
/*      */ 
/* 1440 */         break;
/*      */       case 9:
/* 1443 */         int index = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/* 1446 */         this._attributes.addAttribute(name, this._attributeValueTable._array[index]);
/*      */ 
/* 1448 */         break;
/*      */       case 10:
/* 1452 */         int index = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/* 1455 */         this._attributes.addAttribute(name, this._attributeValueTable._array[index]);
/*      */ 
/* 1457 */         break;
/*      */       case 11:
/* 1460 */         this._attributes.addAttribute(name, "");
/* 1461 */         break;
/*      */       default:
/* 1463 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingAIIValue"));
/*      */       }
/*      */     }
/* 1466 */     while (!terminate);
/*      */ 
/* 1469 */     this._duplicateAttributeVerifier._poolCurrent = this._duplicateAttributeVerifier._poolHead;
/*      */   }
/*      */ 
/*      */   protected final QualifiedName processEIIIndexMedium(int b) throws FastInfosetException, IOException {
/* 1473 */     int i = ((b & 0x7) << 8 | read()) + 32;
/*      */ 
/* 1475 */     return this._elementNameTable._array[i];
/*      */   }
/*      */ 
/*      */   protected final QualifiedName processEIIIndexLarge(int b)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*      */     int i;
/*      */     int i;
/* 1480 */     if ((b & 0x30) == 32)
/*      */     {
/* 1482 */       i = ((b & 0x7) << 16 | read() << 8 | read()) + 2080;
/*      */     }
/*      */     else
/*      */     {
/* 1486 */       i = ((read() & 0xF) << 16 | read() << 8 | read()) + 526368;
/*      */     }
/*      */ 
/* 1489 */     return this._elementNameTable._array[i];
/*      */   }
/*      */ 
/*      */   protected final QualifiedName processLiteralQualifiedName(int state, QualifiedName q) throws FastInfosetException, IOException
/*      */   {
/* 1494 */     if (q == null) q = new QualifiedName();
/*      */ 
/* 1496 */     switch (state)
/*      */     {
/*      */     case 0:
/* 1499 */       return q.set("", "", decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, -1, -1, this._identifier);
/*      */     case 1:
/* 1510 */       return q.set("", decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, -1, this._namespaceNameIndex, this._identifier);
/*      */     case 2:
/* 1521 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
/*      */     case 3:
/* 1524 */       return q.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, this._prefixIndex, this._namespaceNameIndex, this._identifier);
/*      */     }
/*      */ 
/* 1534 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
/*      */   }
/*      */ 
/*      */   protected final void processCommentII() throws FastInfosetException, IOException
/*      */   {
/* 1539 */     this._eventType = 5;
/*      */ 
/* 1541 */     switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */     case 0:
/* 1543 */       if (this._addToTable) {
/* 1544 */         this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
/*      */       }
/*      */ 
/* 1547 */       this._characters = this._charBuffer;
/* 1548 */       this._charactersOffset = 0;
/* 1549 */       break;
/*      */     case 2:
/* 1551 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
/*      */     case 1:
/* 1553 */       CharArray ca = this._v.otherString.get(this._integer);
/*      */ 
/* 1555 */       this._characters = ca.ch;
/* 1556 */       this._charactersOffset = ca.start;
/* 1557 */       this._charBufferLength = ca.length;
/* 1558 */       break;
/*      */     case 3:
/* 1560 */       this._characters = this._charBuffer;
/* 1561 */       this._charactersOffset = 0;
/* 1562 */       this._charBufferLength = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processProcessingII() throws FastInfosetException, IOException
/*      */   {
/* 1568 */     this._eventType = 3;
/*      */ 
/* 1570 */     this._piTarget = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/* 1572 */     switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */     case 0:
/* 1574 */       this._piData = new String(this._charBuffer, 0, this._charBufferLength);
/* 1575 */       if (this._addToTable)
/* 1576 */         this._v.otherString.add(new CharArrayString(this._piData)); break;
/*      */     case 2:
/* 1580 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
/*      */     case 1:
/* 1582 */       this._piData = this._v.otherString.get(this._integer).toString();
/* 1583 */       break;
/*      */     case 3:
/* 1585 */       this._piData = "";
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processUnexpandedEntityReference(int b) throws FastInfosetException, IOException
/*      */   {
/* 1591 */     this._eventType = 9;
/*      */ 
/* 1597 */     String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/* 1599 */     String system_identifier = (b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/* 1601 */     String public_identifier = (b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */   }
/*      */ 
/*      */   protected final void processCIIEncodingAlgorithm(boolean addToTable) throws FastInfosetException, IOException
/*      */   {
/* 1606 */     this._algorithmData = this._octetBuffer;
/* 1607 */     this._algorithmDataOffset = this._octetBufferStart;
/* 1608 */     this._algorithmDataLength = this._octetBufferLength;
/* 1609 */     this._isAlgorithmDataCloned = false;
/*      */ 
/* 1611 */     if (this._algorithmId >= 32) {
/* 1612 */       this._algorithmURI = this._v.encodingAlgorithm.get(this._algorithmId - 32);
/* 1613 */       if (this._algorithmURI == null)
/* 1614 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[] { Integer.valueOf(this._identifier) }));
/*      */     }
/* 1616 */     else if (this._algorithmId > 9)
/*      */     {
/* 1620 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */     }
/*      */ 
/* 1623 */     if (addToTable) {
/* 1624 */       convertEncodingAlgorithmDataToCharacters();
/* 1625 */       this._characterContentChunkTable.add(this._characters, this._characters.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processAIIEncodingAlgorithm(QualifiedName name, boolean addToTable) throws FastInfosetException, IOException {
/* 1630 */     EncodingAlgorithm ea = null;
/* 1631 */     String URI = null;
/* 1632 */     if (this._identifier >= 32) {
/* 1633 */       URI = this._v.encodingAlgorithm.get(this._identifier - 32);
/* 1634 */       if (URI == null)
/* 1635 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[] { Integer.valueOf(this._identifier) }));
/* 1636 */       if (this._registeredEncodingAlgorithms != null)
/* 1637 */         ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/*      */     } else {
/* 1639 */       if (this._identifier >= 9) {
/* 1640 */         if (this._identifier == 9) {
/* 1641 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
/*      */         }
/*      */ 
/* 1647 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */       }
/* 1649 */       ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier);
/*      */     }
/*      */     Object algorithmData;
/*      */     Object algorithmData;
/* 1654 */     if (ea != null) {
/* 1655 */       algorithmData = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */     }
/*      */     else {
/* 1658 */       byte[] data = new byte[this._octetBufferLength];
/* 1659 */       System.arraycopy(this._octetBuffer, this._octetBufferStart, data, 0, this._octetBufferLength);
/*      */ 
/* 1661 */       algorithmData = data;
/*      */     }
/*      */ 
/* 1664 */     this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, algorithmData);
/*      */ 
/* 1666 */     if (addToTable)
/* 1667 */       this._attributeValueTable.add(this._attributes.getValue(this._attributes.getIndex(name.qName)));
/*      */   }
/*      */ 
/*      */   protected final void convertEncodingAlgorithmDataToCharacters() throws FastInfosetException, IOException
/*      */   {
/* 1672 */     StringBuffer buffer = new StringBuffer();
/* 1673 */     if (this._algorithmId == 1) {
/* 1674 */       convertBase64AlorithmDataToCharacters(buffer);
/* 1675 */     } else if (this._algorithmId < 9) {
/* 1676 */       Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._algorithmId).decodeFromBytes(this._algorithmData, this._algorithmDataOffset, this._algorithmDataLength);
/*      */ 
/* 1678 */       BuiltInEncodingAlgorithmFactory.getAlgorithm(this._algorithmId).convertToCharacters(array, buffer); } else {
/* 1679 */       if (this._algorithmId == 9) {
/* 1680 */         this._octetBufferOffset -= this._octetBufferLength;
/* 1681 */         decodeUtf8StringIntoCharBuffer();
/*      */ 
/* 1683 */         this._characters = this._charBuffer;
/* 1684 */         this._charactersOffset = 0;
/* 1685 */         return;
/* 1686 */       }if (this._algorithmId >= 32) {
/* 1687 */         EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(this._algorithmURI);
/* 1688 */         if (ea != null) {
/* 1689 */           Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/* 1690 */           ea.convertToCharacters(data, buffer);
/*      */         } else {
/* 1692 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1697 */     this._characters = new char[buffer.length()];
/* 1698 */     buffer.getChars(0, buffer.length(), this._characters, 0);
/* 1699 */     this._charactersOffset = 0;
/* 1700 */     this._charBufferLength = this._characters.length;
/*      */   }
/*      */ 
/*      */   protected void convertBase64AlorithmDataToCharacters(StringBuffer buffer)
/*      */     throws EncodingAlgorithmException, IOException
/*      */   {
/* 1714 */     int afterTaleOffset = 0;
/*      */ 
/* 1716 */     if (this.base64TaleLength > 0)
/*      */     {
/* 1718 */       int bytesToCopy = Math.min(3 - this.base64TaleLength, this._algorithmDataLength);
/* 1719 */       System.arraycopy(this._algorithmData, this._algorithmDataOffset, this.base64TaleBytes, this.base64TaleLength, bytesToCopy);
/* 1720 */       if (this.base64TaleLength + bytesToCopy == 3) {
/* 1721 */         base64DecodeWithCloning(buffer, this.base64TaleBytes, 0, 3); } else {
/* 1722 */         if (!isBase64Follows())
/*      */         {
/* 1724 */           base64DecodeWithCloning(buffer, this.base64TaleBytes, 0, this.base64TaleLength + bytesToCopy);
/* 1725 */           return;
/*      */         }
/*      */ 
/* 1728 */         this.base64TaleLength += bytesToCopy;
/* 1729 */         return;
/*      */       }
/*      */ 
/* 1732 */       afterTaleOffset = bytesToCopy;
/* 1733 */       this.base64TaleLength = 0;
/*      */     }
/*      */ 
/* 1736 */     int taleBytesRemaining = isBase64Follows() ? (this._algorithmDataLength - afterTaleOffset) % 3 : 0;
/*      */ 
/* 1738 */     if (this._isAlgorithmDataCloned) {
/* 1739 */       base64DecodeWithoutCloning(buffer, this._algorithmData, this._algorithmDataOffset + afterTaleOffset, this._algorithmDataLength - afterTaleOffset - taleBytesRemaining);
/*      */     }
/*      */     else {
/* 1742 */       base64DecodeWithCloning(buffer, this._algorithmData, this._algorithmDataOffset + afterTaleOffset, this._algorithmDataLength - afterTaleOffset - taleBytesRemaining);
/*      */     }
/*      */ 
/* 1746 */     if (taleBytesRemaining > 0) {
/* 1747 */       System.arraycopy(this._algorithmData, this._algorithmDataOffset + this._algorithmDataLength - taleBytesRemaining, this.base64TaleBytes, 0, taleBytesRemaining);
/*      */ 
/* 1749 */       this.base64TaleLength = taleBytesRemaining;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void base64DecodeWithCloning(StringBuffer dstBuffer, byte[] data, int offset, int length)
/*      */     throws EncodingAlgorithmException
/*      */   {
/* 1758 */     Object array = BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.decodeFromBytes(data, offset, length);
/*      */ 
/* 1760 */     BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.convertToCharacters(array, dstBuffer);
/*      */   }
/*      */ 
/*      */   private void base64DecodeWithoutCloning(StringBuffer dstBuffer, byte[] data, int offset, int length)
/*      */     throws EncodingAlgorithmException
/*      */   {
/* 1768 */     BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.convertToCharacters(data, offset, length, dstBuffer);
/*      */   }
/*      */ 
/*      */   public boolean isBase64Follows()
/*      */     throws IOException
/*      */   {
/* 1777 */     int b = peek(this);
/* 1778 */     switch (DecoderStateTables.EII(b)) {
/*      */     case 13:
/* 1780 */       int algorithmId = (b & 0x2) << 6;
/* 1781 */       int b2 = peek2(this);
/* 1782 */       algorithmId |= (b2 & 0xFC) >> 2;
/*      */ 
/* 1784 */       return algorithmId == 1;
/*      */     }
/* 1786 */     return false;
/*      */   }
/*      */ 
/*      */   public final String getNamespaceDecl(String prefix)
/*      */   {
/* 1805 */     return this._prefixTable.getNamespaceFromPrefix(prefix);
/*      */   }
/*      */ 
/*      */   public final String getURI(String prefix) {
/* 1809 */     return getNamespaceDecl(prefix);
/*      */   }
/*      */ 
/*      */   public final Iterator getPrefixes() {
/* 1813 */     return this._prefixTable.getPrefixes();
/*      */   }
/*      */ 
/*      */   public final AttributesHolder getAttributesHolder() {
/* 1817 */     return this._attributes;
/*      */   }
/*      */ 
/*      */   public final void setManager(StAXManager manager) {
/* 1821 */     this._manager = manager;
/*      */   }
/*      */ 
/*      */   static final String getEventTypeString(int eventType) {
/* 1825 */     switch (eventType) {
/*      */     case 1:
/* 1827 */       return "START_ELEMENT";
/*      */     case 2:
/* 1829 */       return "END_ELEMENT";
/*      */     case 3:
/* 1831 */       return "PROCESSING_INSTRUCTION";
/*      */     case 4:
/* 1833 */       return "CHARACTERS";
/*      */     case 5:
/* 1835 */       return "COMMENT";
/*      */     case 7:
/* 1837 */       return "START_DOCUMENT";
/*      */     case 8:
/* 1839 */       return "END_DOCUMENT";
/*      */     case 9:
/* 1841 */       return "ENTITY_REFERENCE";
/*      */     case 10:
/* 1843 */       return "ATTRIBUTE";
/*      */     case 11:
/* 1845 */       return "DTD";
/*      */     case 12:
/* 1847 */       return "CDATA";
/*      */     case 6:
/* 1849 */     }return "UNKNOWN_EVENT_TYPE";
/*      */   }
/*      */ 
/*      */   protected class NamespaceContextImpl
/*      */     implements NamespaceContext
/*      */   {
/*      */     protected NamespaceContextImpl()
/*      */     {
/*      */     }
/*      */ 
/*      */     public final String getNamespaceURI(String prefix)
/*      */     {
/* 1792 */       return StAXDocumentParser.this._prefixTable.getNamespaceFromPrefix(prefix);
/*      */     }
/*      */ 
/*      */     public final String getPrefix(String namespaceURI) {
/* 1796 */       return StAXDocumentParser.this._prefixTable.getPrefixFromNamespace(namespaceURI);
/*      */     }
/*      */ 
/*      */     public final Iterator getPrefixes(String namespaceURI) {
/* 1800 */       return StAXDocumentParser.this._prefixTable.getPrefixesFromNamespace(namespaceURI);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser
 * JD-Core Version:    0.6.2
 */