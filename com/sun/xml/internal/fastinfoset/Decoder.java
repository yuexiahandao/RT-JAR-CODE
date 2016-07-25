/*      */ package com.sun.xml.internal.fastinfoset;
/*      */ 
/*      */ import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayString;
/*      */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
/*      */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*      */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class Decoder
/*      */   implements FastInfosetParser
/*      */ {
/*   76 */   private static final char[] XML_NAMESPACE_NAME_CHARS = "http://www.w3.org/XML/1998/namespace".toCharArray();
/*      */ 
/*   79 */   private static final char[] XMLNS_NAMESPACE_PREFIX_CHARS = "xmlns".toCharArray();
/*      */ 
/*   82 */   private static final char[] XMLNS_NAMESPACE_NAME_CHARS = "http://www.w3.org/2000/xmlns/".toCharArray();
/*      */   public static final String STRING_INTERNING_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.parser.string-interning";
/*      */   public static final String BUFFER_SIZE_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.parser.buffer-size";
/*   97 */   private static boolean _stringInterningSystemDefault = false;
/*      */ 
/*   99 */   private static int _bufferSizeSystemDefault = 1024;
/*      */ 
/*  120 */   private boolean _stringInterning = _stringInterningSystemDefault;
/*      */   private InputStream _s;
/*      */   private Map _externalVocabularies;
/*      */   protected boolean _parseFragments;
/*      */   protected boolean _needForceStreamClose;
/*      */   private boolean _vIsInternal;
/*      */   protected List _notations;
/*      */   protected List _unparsedEntities;
/*  162 */   protected Map _registeredEncodingAlgorithms = new HashMap();
/*      */   protected ParserVocabulary _v;
/*      */   protected PrefixArray _prefixTable;
/*      */   protected QualifiedNameArray _elementNameTable;
/*      */   protected QualifiedNameArray _attributeNameTable;
/*      */   protected ContiguousCharArrayArray _characterContentChunkTable;
/*      */   protected StringArray _attributeValueTable;
/*      */   protected int _b;
/*      */   protected boolean _terminate;
/*      */   protected boolean _doubleTerminate;
/*      */   protected boolean _addToTable;
/*      */   protected int _integer;
/*      */   protected int _identifier;
/*  228 */   protected int _bufferSize = _bufferSizeSystemDefault;
/*      */ 
/*  233 */   protected byte[] _octetBuffer = new byte[_bufferSizeSystemDefault];
/*      */   protected int _octetBufferStart;
/*      */   protected int _octetBufferOffset;
/*      */   protected int _octetBufferEnd;
/*      */   protected int _octetBufferLength;
/*  259 */   protected char[] _charBuffer = new char[512];
/*      */   protected int _charBufferLength;
/*  269 */   protected DuplicateAttributeVerifier _duplicateAttributeVerifier = new DuplicateAttributeVerifier();
/*      */   protected static final int NISTRING_STRING = 0;
/*      */   protected static final int NISTRING_INDEX = 1;
/*      */   protected static final int NISTRING_ENCODING_ALGORITHM = 2;
/*      */   protected static final int NISTRING_EMPTY_STRING = 3;
/*      */   protected int _prefixIndex;
/*      */   protected int _namespaceNameIndex;
/*      */   private int _bitsLeftInOctet;
/*      */   private char _utf8_highSurrogate;
/*      */   private char _utf8_lowSurrogate;
/*      */ 
/*      */   protected Decoder()
/*      */   {
/*  275 */     this._v = new ParserVocabulary();
/*  276 */     this._prefixTable = this._v.prefix;
/*  277 */     this._elementNameTable = this._v.elementName;
/*  278 */     this._attributeNameTable = this._v.attributeName;
/*  279 */     this._characterContentChunkTable = this._v.characterContentChunk;
/*  280 */     this._attributeValueTable = this._v.attributeValue;
/*  281 */     this._vIsInternal = true;
/*      */   }
/*      */ 
/*      */   public void setStringInterning(boolean stringInterning)
/*      */   {
/*  291 */     this._stringInterning = stringInterning;
/*      */   }
/*      */ 
/*      */   public boolean getStringInterning()
/*      */   {
/*  298 */     return this._stringInterning;
/*      */   }
/*      */ 
/*      */   public void setBufferSize(int bufferSize)
/*      */   {
/*  305 */     if (this._bufferSize > this._octetBuffer.length)
/*  306 */       this._bufferSize = bufferSize;
/*      */   }
/*      */ 
/*      */   public int getBufferSize()
/*      */   {
/*  314 */     return this._bufferSize;
/*      */   }
/*      */ 
/*      */   public void setRegisteredEncodingAlgorithms(Map algorithms)
/*      */   {
/*  321 */     this._registeredEncodingAlgorithms = algorithms;
/*  322 */     if (this._registeredEncodingAlgorithms == null)
/*  323 */       this._registeredEncodingAlgorithms = new HashMap();
/*      */   }
/*      */ 
/*      */   public Map getRegisteredEncodingAlgorithms()
/*      */   {
/*  331 */     return this._registeredEncodingAlgorithms;
/*      */   }
/*      */ 
/*      */   public void setExternalVocabularies(Map referencedVocabualries)
/*      */   {
/*  338 */     if (referencedVocabualries != null)
/*      */     {
/*  340 */       this._externalVocabularies = new HashMap();
/*  341 */       this._externalVocabularies.putAll(referencedVocabualries);
/*      */     } else {
/*  343 */       this._externalVocabularies = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Map getExternalVocabularies()
/*      */   {
/*  351 */     return this._externalVocabularies;
/*      */   }
/*      */ 
/*      */   public void setParseFragments(boolean parseFragments)
/*      */   {
/*  358 */     this._parseFragments = parseFragments;
/*      */   }
/*      */ 
/*      */   public boolean getParseFragments()
/*      */   {
/*  365 */     return this._parseFragments;
/*      */   }
/*      */ 
/*      */   public void setForceStreamClose(boolean needForceStreamClose)
/*      */   {
/*  372 */     this._needForceStreamClose = needForceStreamClose;
/*      */   }
/*      */ 
/*      */   public boolean getForceStreamClose()
/*      */   {
/*  379 */     return this._needForceStreamClose;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  388 */     this._terminate = (this._doubleTerminate = 0);
/*      */   }
/*      */ 
/*      */   public void setVocabulary(ParserVocabulary v)
/*      */   {
/*  397 */     this._v = v;
/*  398 */     this._prefixTable = this._v.prefix;
/*  399 */     this._elementNameTable = this._v.elementName;
/*  400 */     this._attributeNameTable = this._v.attributeName;
/*  401 */     this._characterContentChunkTable = this._v.characterContentChunk;
/*  402 */     this._attributeValueTable = this._v.attributeValue;
/*  403 */     this._vIsInternal = false;
/*      */   }
/*      */ 
/*      */   public void setInputStream(InputStream s)
/*      */   {
/*  412 */     this._s = s;
/*  413 */     this._octetBufferOffset = 0;
/*  414 */     this._octetBufferEnd = 0;
/*  415 */     if (this._vIsInternal == true)
/*  416 */       this._v.clear();
/*      */   }
/*      */ 
/*      */   protected final void decodeDII() throws FastInfosetException, IOException
/*      */   {
/*  421 */     int b = read();
/*  422 */     if (b == 32)
/*  423 */       decodeInitialVocabulary();
/*  424 */     else if (b != 0)
/*  425 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.optinalValues"));
/*      */   }
/*      */ 
/*      */   protected final void decodeAdditionalData()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  431 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  433 */     for (int i = 0; i < noOfItems; i++) {
/*  434 */       String URI = decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
/*      */ 
/*  436 */       decodeNonEmptyOctetStringLengthOnSecondBit();
/*  437 */       ensureOctetBufferSize();
/*  438 */       this._octetBufferStart = this._octetBufferOffset;
/*  439 */       this._octetBufferOffset += this._octetBufferLength;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void decodeInitialVocabulary() throws FastInfosetException, IOException
/*      */   {
/*  445 */     int b = read();
/*      */ 
/*  447 */     int b2 = read();
/*      */ 
/*  450 */     if ((b == 16) && (b2 == 0)) {
/*  451 */       decodeExternalVocabularyURI();
/*  452 */       return;
/*      */     }
/*      */ 
/*  455 */     if ((b & 0x10) > 0) {
/*  456 */       decodeExternalVocabularyURI();
/*      */     }
/*      */ 
/*  459 */     if ((b & 0x8) > 0) {
/*  460 */       decodeTableItems(this._v.restrictedAlphabet);
/*      */     }
/*      */ 
/*  463 */     if ((b & 0x4) > 0) {
/*  464 */       decodeTableItems(this._v.encodingAlgorithm);
/*      */     }
/*      */ 
/*  467 */     if ((b & 0x2) > 0) {
/*  468 */       decodeTableItems(this._v.prefix);
/*      */     }
/*      */ 
/*  471 */     if ((b & 0x1) > 0) {
/*  472 */       decodeTableItems(this._v.namespaceName);
/*      */     }
/*      */ 
/*  475 */     if ((b2 & 0x80) > 0) {
/*  476 */       decodeTableItems(this._v.localName);
/*      */     }
/*      */ 
/*  479 */     if ((b2 & 0x40) > 0) {
/*  480 */       decodeTableItems(this._v.otherNCName);
/*      */     }
/*      */ 
/*  483 */     if ((b2 & 0x20) > 0) {
/*  484 */       decodeTableItems(this._v.otherURI);
/*      */     }
/*      */ 
/*  487 */     if ((b2 & 0x10) > 0) {
/*  488 */       decodeTableItems(this._v.attributeValue);
/*      */     }
/*      */ 
/*  491 */     if ((b2 & 0x8) > 0) {
/*  492 */       decodeTableItems(this._v.characterContentChunk);
/*      */     }
/*      */ 
/*  495 */     if ((b2 & 0x4) > 0) {
/*  496 */       decodeTableItems(this._v.otherString);
/*      */     }
/*      */ 
/*  499 */     if ((b2 & 0x2) > 0) {
/*  500 */       decodeTableItems(this._v.elementName, false);
/*      */     }
/*      */ 
/*  503 */     if ((b2 & 0x1) > 0)
/*  504 */       decodeTableItems(this._v.attributeName, true);
/*      */   }
/*      */ 
/*      */   private void decodeExternalVocabularyURI() throws FastInfosetException, IOException
/*      */   {
/*  509 */     if (this._externalVocabularies == null) {
/*  510 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.noExternalVocabularies"));
/*      */     }
/*      */ 
/*  514 */     String externalVocabularyURI = decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
/*      */ 
/*  517 */     Object o = this._externalVocabularies.get(externalVocabularyURI);
/*  518 */     if ((o instanceof ParserVocabulary)) {
/*  519 */       this._v.setReferencedVocabulary(externalVocabularyURI, (ParserVocabulary)o, false);
/*      */     }
/*  521 */     else if ((o instanceof ExternalVocabulary)) {
/*  522 */       ExternalVocabulary v = (ExternalVocabulary)o;
/*      */ 
/*  524 */       ParserVocabulary pv = new ParserVocabulary(v.vocabulary);
/*      */ 
/*  526 */       this._externalVocabularies.put(externalVocabularyURI, pv);
/*  527 */       this._v.setReferencedVocabulary(externalVocabularyURI, pv, false);
/*      */     }
/*      */     else {
/*  530 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.externalVocabularyNotRegistered", new Object[] { externalVocabularyURI }));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void decodeTableItems(StringArray array)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  537 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  539 */     for (int i = 0; i < noOfItems; i++)
/*  540 */       array.add(decodeNonEmptyOctetStringOnSecondBitAsUtf8String());
/*      */   }
/*      */ 
/*      */   private void decodeTableItems(PrefixArray array) throws FastInfosetException, IOException
/*      */   {
/*  545 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  547 */     for (int i = 0; i < noOfItems; i++)
/*  548 */       array.add(decodeNonEmptyOctetStringOnSecondBitAsUtf8String());
/*      */   }
/*      */ 
/*      */   private void decodeTableItems(ContiguousCharArrayArray array) throws FastInfosetException, IOException
/*      */   {
/*  553 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  555 */     for (int i = 0; i < noOfItems; i++)
/*  556 */       switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */       case 0:
/*  558 */         array.add(this._charBuffer, this._charBufferLength);
/*  559 */         break;
/*      */       default:
/*  561 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.illegalState"));
/*      */       }
/*      */   }
/*      */ 
/*      */   private void decodeTableItems(CharArrayArray array) throws FastInfosetException, IOException
/*      */   {
/*  567 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  569 */     for (int i = 0; i < noOfItems; i++)
/*  570 */       switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */       case 0:
/*  572 */         array.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
/*  573 */         break;
/*      */       default:
/*  575 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.illegalState"));
/*      */       }
/*      */   }
/*      */ 
/*      */   private void decodeTableItems(QualifiedNameArray array, boolean isAttribute) throws FastInfosetException, IOException
/*      */   {
/*  581 */     int noOfItems = decodeNumberOfItemsOfSequence();
/*      */ 
/*  583 */     for (int i = 0; i < noOfItems; i++) {
/*  584 */       int b = read();
/*      */ 
/*  586 */       String prefix = "";
/*  587 */       int prefixIndex = -1;
/*  588 */       if ((b & 0x2) > 0) {
/*  589 */         prefixIndex = decodeIntegerIndexOnSecondBit();
/*  590 */         prefix = this._v.prefix.get(prefixIndex);
/*      */       }
/*      */ 
/*  593 */       String namespaceName = "";
/*  594 */       int namespaceNameIndex = -1;
/*  595 */       if ((b & 0x1) > 0) {
/*  596 */         namespaceNameIndex = decodeIntegerIndexOnSecondBit();
/*  597 */         namespaceName = this._v.namespaceName.get(namespaceNameIndex);
/*      */       }
/*      */ 
/*  600 */       if ((namespaceName == "") && (prefix != "")) {
/*  601 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespace"));
/*      */       }
/*      */ 
/*  604 */       int localNameIndex = decodeIntegerIndexOnSecondBit();
/*  605 */       String localName = this._v.localName.get(localNameIndex);
/*      */ 
/*  607 */       QualifiedName qualifiedName = new QualifiedName(prefix, namespaceName, localName, prefixIndex, namespaceNameIndex, localNameIndex, this._charBuffer);
/*      */ 
/*  610 */       if (isAttribute) {
/*  611 */         qualifiedName.createAttributeValues(256);
/*      */       }
/*  613 */       array.add(qualifiedName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int decodeNumberOfItemsOfSequence() throws IOException {
/*  618 */     int b = read();
/*  619 */     if (b < 128) {
/*  620 */       return b + 1;
/*      */     }
/*  622 */     return ((b & 0xF) << 16 | read() << 8 | read()) + 129;
/*      */   }
/*      */ 
/*      */   protected final void decodeNotations() throws FastInfosetException, IOException
/*      */   {
/*  627 */     if (this._notations == null)
/*  628 */       this._notations = new ArrayList();
/*      */     else {
/*  630 */       this._notations.clear();
/*      */     }
/*      */ 
/*  633 */     int b = read();
/*  634 */     while ((b & 0xFC) == 192) {
/*  635 */       String name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/*  637 */       String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  639 */       String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  642 */       Notation notation = new Notation(name, system_identifier, public_identifier);
/*  643 */       this._notations.add(notation);
/*      */ 
/*  645 */       b = read();
/*      */     }
/*  647 */     if (b != 240)
/*  648 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IIsNotTerminatedCorrectly"));
/*      */   }
/*      */ 
/*      */   protected final void decodeUnparsedEntities() throws FastInfosetException, IOException
/*      */   {
/*  653 */     if (this._unparsedEntities == null)
/*  654 */       this._unparsedEntities = new ArrayList();
/*      */     else {
/*  656 */       this._unparsedEntities.clear();
/*      */     }
/*      */ 
/*  659 */     int b = read();
/*  660 */     while ((b & 0xFE) == 208) {
/*  661 */       String name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*  662 */       String system_identifier = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI);
/*      */ 
/*  664 */       String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
/*      */ 
/*  667 */       String notation_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/*  669 */       UnparsedEntity unparsedEntity = new UnparsedEntity(name, system_identifier, public_identifier, notation_name);
/*  670 */       this._unparsedEntities.add(unparsedEntity);
/*      */ 
/*  672 */       b = read();
/*      */     }
/*  674 */     if (b != 240)
/*  675 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.unparsedEntities"));
/*      */   }
/*      */ 
/*      */   protected final String decodeCharacterEncodingScheme() throws FastInfosetException, IOException
/*      */   {
/*  680 */     return decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
/*      */   }
/*      */ 
/*      */   protected final String decodeVersion() throws FastInfosetException, IOException {
/*  684 */     switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */     case 0:
/*  686 */       String data = new String(this._charBuffer, 0, this._charBufferLength);
/*  687 */       if (this._addToTable) {
/*  688 */         this._v.otherString.add(new CharArrayString(data));
/*      */       }
/*  690 */       return data;
/*      */     case 2:
/*  692 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNotSupported"));
/*      */     case 1:
/*  694 */       return this._v.otherString.get(this._integer).toString();
/*      */     case 3:
/*      */     }
/*  697 */     return "";
/*      */   }
/*      */ 
/*      */   protected final QualifiedName decodeEIIIndexMedium() throws FastInfosetException, IOException
/*      */   {
/*  702 */     int i = ((this._b & 0x7) << 8 | read()) + 32;
/*      */ 
/*  704 */     return this._v.elementName._array[i];
/*      */   }
/*      */ 
/*      */   protected final QualifiedName decodeEIIIndexLarge()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*      */     int i;
/*      */     int i;
/*  709 */     if ((this._b & 0x30) == 32)
/*      */     {
/*  711 */       i = ((this._b & 0x7) << 16 | read() << 8 | read()) + 2080;
/*      */     }
/*      */     else
/*      */     {
/*  715 */       i = ((read() & 0xF) << 16 | read() << 8 | read()) + 526368;
/*      */     }
/*      */ 
/*  718 */     return this._v.elementName._array[i];
/*      */   }
/*      */ 
/*      */   protected final QualifiedName decodeLiteralQualifiedName(int state, QualifiedName q) throws FastInfosetException, IOException
/*      */   {
/*  723 */     if (q == null) q = new QualifiedName();
/*  724 */     switch (state)
/*      */     {
/*      */     case 0:
/*  727 */       return q.set("", "", decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, null);
/*      */     case 1:
/*  737 */       return q.set("", decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, null);
/*      */     case 2:
/*  747 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
/*      */     case 3:
/*  750 */       return q.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
/*      */     }
/*      */ 
/*  759 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
/*      */   }
/*      */ 
/*      */   protected final int decodeNonIdentifyingStringOnFirstBit()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  773 */     int b = read();
/*  774 */     switch (DecoderStateTables.NISTRING(b)) {
/*      */     case 0:
/*  776 */       this._addToTable = ((b & 0x40) > 0);
/*  777 */       this._octetBufferLength = ((b & 0x7) + 1);
/*  778 */       decodeUtf8StringAsCharBuffer();
/*  779 */       return 0;
/*      */     case 1:
/*  781 */       this._addToTable = ((b & 0x40) > 0);
/*  782 */       this._octetBufferLength = (read() + 9);
/*  783 */       decodeUtf8StringAsCharBuffer();
/*  784 */       return 0;
/*      */     case 2:
/*  787 */       this._addToTable = ((b & 0x40) > 0);
/*  788 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  792 */       this._octetBufferLength = (length + 265);
/*  793 */       decodeUtf8StringAsCharBuffer();
/*  794 */       return 0;
/*      */     case 3:
/*  797 */       this._addToTable = ((b & 0x40) > 0);
/*  798 */       this._octetBufferLength = ((b & 0x7) + 1);
/*  799 */       decodeUtf16StringAsCharBuffer();
/*  800 */       return 0;
/*      */     case 4:
/*  802 */       this._addToTable = ((b & 0x40) > 0);
/*  803 */       this._octetBufferLength = (read() + 9);
/*  804 */       decodeUtf16StringAsCharBuffer();
/*  805 */       return 0;
/*      */     case 5:
/*  808 */       this._addToTable = ((b & 0x40) > 0);
/*  809 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  813 */       this._octetBufferLength = (length + 265);
/*  814 */       decodeUtf16StringAsCharBuffer();
/*  815 */       return 0;
/*      */     case 6:
/*  819 */       this._addToTable = ((b & 0x40) > 0);
/*      */ 
/*  821 */       this._identifier = ((b & 0xF) << 4);
/*  822 */       int b2 = read();
/*  823 */       this._identifier |= (b2 & 0xF0) >> 4;
/*      */ 
/*  825 */       decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b2);
/*      */ 
/*  827 */       decodeRestrictedAlphabetAsCharBuffer();
/*  828 */       return 0;
/*      */     case 7:
/*  832 */       this._addToTable = ((b & 0x40) > 0);
/*      */ 
/*  834 */       this._identifier = ((b & 0xF) << 4);
/*  835 */       int b2 = read();
/*  836 */       this._identifier |= (b2 & 0xF0) >> 4;
/*      */ 
/*  838 */       decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b2);
/*  839 */       return 2;
/*      */     case 8:
/*  842 */       this._integer = (b & 0x3F);
/*  843 */       return 1;
/*      */     case 9:
/*  845 */       this._integer = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/*  847 */       return 1;
/*      */     case 10:
/*  849 */       this._integer = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/*  851 */       return 1;
/*      */     case 11:
/*  853 */       return 3;
/*      */     }
/*  855 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNonIdentifyingString"));
/*      */   }
/*      */ 
/*      */   protected final void decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(int b)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  861 */     b &= 15;
/*      */ 
/*  863 */     switch (DecoderStateTables.NISTRING(b)) {
/*      */     case 0:
/*  865 */       this._octetBufferLength = (b + 1);
/*  866 */       break;
/*      */     case 1:
/*  868 */       this._octetBufferLength = (read() + 9);
/*  869 */       break;
/*      */     case 2:
/*  871 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  875 */       this._octetBufferLength = (length + 265);
/*  876 */       break;
/*      */     default:
/*  878 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingOctets"));
/*      */     }
/*  880 */     ensureOctetBufferSize();
/*  881 */     this._octetBufferStart = this._octetBufferOffset;
/*  882 */     this._octetBufferOffset += this._octetBufferLength;
/*      */   }
/*      */ 
/*      */   protected final void decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(int b) throws FastInfosetException, IOException
/*      */   {
/*  887 */     switch (b & 0x3)
/*      */     {
/*      */     case 0:
/*  890 */       this._octetBufferLength = 1;
/*  891 */       break;
/*      */     case 1:
/*  894 */       this._octetBufferLength = 2;
/*  895 */       break;
/*      */     case 2:
/*  898 */       this._octetBufferLength = (read() + 3);
/*  899 */       break;
/*      */     case 3:
/*  902 */       this._octetBufferLength = (read() << 24 | read() << 16 | read() << 8 | read());
/*      */ 
/*  906 */       this._octetBufferLength += 259;
/*      */     }
/*      */ 
/*  910 */     ensureOctetBufferSize();
/*  911 */     this._octetBufferStart = this._octetBufferOffset;
/*  912 */     this._octetBufferOffset += this._octetBufferLength;
/*      */   }
/*      */ 
/*      */   protected final String decodeIdentifyingNonEmptyStringOnFirstBit(StringArray table)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  919 */     int b = read();
/*  920 */     switch (DecoderStateTables.ISTRING(b))
/*      */     {
/*      */     case 0:
/*  923 */       this._octetBufferLength = (b + 1);
/*  924 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/*  925 */       this._identifier = (table.add(s) - 1);
/*  926 */       return s;
/*      */     case 1:
/*  930 */       this._octetBufferLength = (read() + 65);
/*  931 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/*  932 */       this._identifier = (table.add(s) - 1);
/*  933 */       return s;
/*      */     case 2:
/*  937 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  941 */       this._octetBufferLength = (length + 321);
/*  942 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/*  943 */       this._identifier = (table.add(s) - 1);
/*  944 */       return s;
/*      */     case 3:
/*  947 */       this._identifier = (b & 0x3F);
/*  948 */       return table._array[this._identifier];
/*      */     case 4:
/*  950 */       this._identifier = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/*  952 */       return table._array[this._identifier];
/*      */     case 5:
/*  954 */       this._identifier = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/*  956 */       return table._array[this._identifier];
/*      */     }
/*  958 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingString"));
/*      */   }
/*      */ 
/*      */   protected final String decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(boolean namespaceNamePresent)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  968 */     int b = read();
/*  969 */     switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b))
/*      */     {
/*      */     case 6:
/*  972 */       this._octetBufferLength = EncodingConstants.XML_NAMESPACE_PREFIX_LENGTH;
/*  973 */       decodeUtf8StringAsCharBuffer();
/*      */ 
/*  975 */       if ((this._charBuffer[0] == 'x') && (this._charBuffer[1] == 'm') && (this._charBuffer[2] == 'l'))
/*      */       {
/*  978 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.prefixIllegal"));
/*      */       }
/*      */ 
/*  981 */       String s = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
/*      */ 
/*  983 */       this._prefixIndex = this._v.prefix.add(s);
/*  984 */       return s;
/*      */     case 7:
/*  988 */       this._octetBufferLength = EncodingConstants.XMLNS_NAMESPACE_PREFIX_LENGTH;
/*  989 */       decodeUtf8StringAsCharBuffer();
/*      */ 
/*  991 */       if ((this._charBuffer[0] == 'x') && (this._charBuffer[1] == 'm') && (this._charBuffer[2] == 'l') && (this._charBuffer[3] == 'n') && (this._charBuffer[4] == 's'))
/*      */       {
/*  996 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.xmlns"));
/*      */       }
/*      */ 
/*  999 */       String s = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
/*      */ 
/* 1001 */       this._prefixIndex = this._v.prefix.add(s);
/* 1002 */       return s;
/*      */     case 0:
/*      */     case 8:
/*      */     case 9:
/* 1008 */       this._octetBufferLength = (b + 1);
/* 1009 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1010 */       this._prefixIndex = this._v.prefix.add(s);
/* 1011 */       return s;
/*      */     case 1:
/* 1015 */       this._octetBufferLength = (read() + 65);
/* 1016 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1017 */       this._prefixIndex = this._v.prefix.add(s);
/* 1018 */       return s;
/*      */     case 2:
/* 1022 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/* 1026 */       this._octetBufferLength = (length + 321);
/* 1027 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1028 */       this._prefixIndex = this._v.prefix.add(s);
/* 1029 */       return s;
/*      */     case 10:
/* 1032 */       if (namespaceNamePresent) {
/* 1033 */         this._prefixIndex = 0;
/*      */ 
/* 1035 */         if (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(peek()) != 10)
/*      */         {
/* 1037 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.wrongNamespaceName"));
/*      */         }
/* 1039 */         return "xml";
/*      */       }
/* 1041 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespaceName"));
/*      */     case 3:
/* 1044 */       this._prefixIndex = (b & 0x3F);
/* 1045 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     case 4:
/* 1047 */       this._prefixIndex = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/* 1049 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     case 5:
/* 1051 */       this._prefixIndex = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/* 1053 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     }
/* 1055 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingStringForPrefix"));
/*      */   }
/*      */ 
/*      */   protected final String decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(boolean namespaceNamePresent)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1063 */     int b = read();
/* 1064 */     switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b)) {
/*      */     case 10:
/* 1066 */       if (namespaceNamePresent) {
/* 1067 */         this._prefixIndex = 0;
/*      */ 
/* 1069 */         if (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(peek()) != 10)
/*      */         {
/* 1071 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.wrongNamespaceName"));
/*      */         }
/* 1073 */         return "xml";
/*      */       }
/* 1075 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespaceName"));
/*      */     case 3:
/* 1078 */       this._prefixIndex = (b & 0x3F);
/* 1079 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     case 4:
/* 1081 */       this._prefixIndex = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/* 1083 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     case 5:
/* 1085 */       this._prefixIndex = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/* 1087 */       return this._v.prefix._array[(this._prefixIndex - 1)];
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/* 1089 */     case 9: } throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingStringForPrefix"));
/*      */   }
/*      */ 
/*      */   protected final String decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(boolean prefixPresent)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1099 */     int b = read();
/* 1100 */     switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b))
/*      */     {
/*      */     case 0:
/*      */     case 6:
/*      */     case 7:
/* 1105 */       this._octetBufferLength = (b + 1);
/* 1106 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1107 */       this._namespaceNameIndex = this._v.namespaceName.add(s);
/* 1108 */       return s;
/*      */     case 8:
/* 1112 */       this._octetBufferLength = EncodingConstants.XMLNS_NAMESPACE_NAME_LENGTH;
/* 1113 */       decodeUtf8StringAsCharBuffer();
/*      */ 
/* 1115 */       if (compareCharsWithCharBufferFromEndToStart(XMLNS_NAMESPACE_NAME_CHARS)) {
/* 1116 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.xmlnsConnotBeBoundToPrefix"));
/*      */       }
/*      */ 
/* 1119 */       String s = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
/*      */ 
/* 1121 */       this._namespaceNameIndex = this._v.namespaceName.add(s);
/* 1122 */       return s;
/*      */     case 9:
/* 1126 */       this._octetBufferLength = EncodingConstants.XML_NAMESPACE_NAME_LENGTH;
/* 1127 */       decodeUtf8StringAsCharBuffer();
/*      */ 
/* 1129 */       if (compareCharsWithCharBufferFromEndToStart(XML_NAMESPACE_NAME_CHARS)) {
/* 1130 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.illegalNamespaceName"));
/*      */       }
/*      */ 
/* 1133 */       String s = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
/*      */ 
/* 1135 */       this._namespaceNameIndex = this._v.namespaceName.add(s);
/* 1136 */       return s;
/*      */     case 1:
/* 1140 */       this._octetBufferLength = (read() + 65);
/* 1141 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1142 */       this._namespaceNameIndex = this._v.namespaceName.add(s);
/* 1143 */       return s;
/*      */     case 2:
/* 1147 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/* 1151 */       this._octetBufferLength = (length + 321);
/* 1152 */       String s = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
/* 1153 */       this._namespaceNameIndex = this._v.namespaceName.add(s);
/* 1154 */       return s;
/*      */     case 10:
/* 1157 */       if (prefixPresent) {
/* 1158 */         this._namespaceNameIndex = 0;
/* 1159 */         return "http://www.w3.org/XML/1998/namespace";
/*      */       }
/* 1161 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.namespaceWithoutPrefix"));
/*      */     case 3:
/* 1164 */       this._namespaceNameIndex = (b & 0x3F);
/* 1165 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     case 4:
/* 1167 */       this._namespaceNameIndex = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/* 1169 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     case 5:
/* 1171 */       this._namespaceNameIndex = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/* 1173 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     }
/* 1175 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingForNamespaceName"));
/*      */   }
/*      */ 
/*      */   protected final String decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(boolean prefixPresent)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1183 */     int b = read();
/* 1184 */     switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b)) {
/*      */     case 10:
/* 1186 */       if (prefixPresent) {
/* 1187 */         this._namespaceNameIndex = 0;
/* 1188 */         return "http://www.w3.org/XML/1998/namespace";
/*      */       }
/* 1190 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.namespaceWithoutPrefix"));
/*      */     case 3:
/* 1193 */       this._namespaceNameIndex = (b & 0x3F);
/* 1194 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     case 4:
/* 1196 */       this._namespaceNameIndex = (((b & 0x1F) << 8 | read()) + 64);
/*      */ 
/* 1198 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     case 5:
/* 1200 */       this._namespaceNameIndex = (((b & 0xF) << 16 | read() << 8 | read()) + 8256);
/*      */ 
/* 1202 */       return this._v.namespaceName._array[(this._namespaceNameIndex - 1)];
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/* 1204 */     case 9: } throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingForNamespaceName"));
/*      */   }
/*      */ 
/*      */   private boolean compareCharsWithCharBufferFromEndToStart(char[] c)
/*      */   {
/* 1209 */     int i = this._charBufferLength;
/*      */     do { i--; if (i < 0) break; }
/* 1211 */     while (c[i] == this._charBuffer[i]);
/* 1212 */     return false;
/*      */ 
/* 1215 */     return true;
/*      */   }
/*      */ 
/*      */   protected final String decodeNonEmptyOctetStringOnSecondBitAsUtf8String()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1222 */     decodeNonEmptyOctetStringOnSecondBitAsUtf8CharArray();
/* 1223 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   protected final void decodeNonEmptyOctetStringOnSecondBitAsUtf8CharArray()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1230 */     decodeNonEmptyOctetStringLengthOnSecondBit();
/* 1231 */     decodeUtf8StringAsCharBuffer();
/*      */   }
/*      */ 
/*      */   protected final void decodeNonEmptyOctetStringLengthOnSecondBit()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1238 */     int b = read();
/* 1239 */     switch (DecoderStateTables.ISTRING(b)) {
/*      */     case 0:
/* 1241 */       this._octetBufferLength = (b + 1);
/* 1242 */       break;
/*      */     case 1:
/* 1244 */       this._octetBufferLength = (read() + 65);
/* 1245 */       break;
/*      */     case 2:
/* 1248 */       int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/* 1252 */       this._octetBufferLength = (length + 321);
/* 1253 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     default:
/* 1259 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNonEmptyOctet"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final int decodeIntegerIndexOnSecondBit()
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1267 */     int b = read() | 0x80;
/* 1268 */     switch (DecoderStateTables.ISTRING(b)) {
/*      */     case 3:
/* 1270 */       return b & 0x3F;
/*      */     case 4:
/* 1272 */       return ((b & 0x1F) << 8 | read()) + 64;
/*      */     case 5:
/* 1275 */       return ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     }
/*      */ 
/* 1281 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIndexOnSecondBit"));
/*      */   }
/*      */ 
/*      */   protected final void decodeHeader() throws FastInfosetException, IOException
/*      */   {
/* 1286 */     if (!_isFastInfosetDocument())
/* 1287 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.notFIDocument"));
/*      */   }
/*      */ 
/*      */   protected final void decodeRestrictedAlphabetAsCharBuffer() throws FastInfosetException, IOException
/*      */   {
/* 1292 */     if (this._identifier <= 1) {
/* 1293 */       decodeFourBitAlphabetOctetsAsCharBuffer(com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table[this._identifier]);
/*      */     }
/* 1295 */     else if (this._identifier >= 32) {
/* 1296 */       CharArray ca = this._v.restrictedAlphabet.get(this._identifier - 32);
/* 1297 */       if (ca == null) {
/* 1298 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetNotPresent", new Object[] { Integer.valueOf(this._identifier) }));
/*      */       }
/* 1300 */       decodeAlphabetOctetsAsCharBuffer(ca.ch);
/*      */     }
/*      */     else
/*      */     {
/* 1305 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetIdentifiersReserved"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final String decodeRestrictedAlphabetAsString() throws FastInfosetException, IOException {
/* 1310 */     decodeRestrictedAlphabetAsCharBuffer();
/* 1311 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   protected final String decodeRAOctetsAsString(char[] restrictedAlphabet) throws FastInfosetException, IOException {
/* 1315 */     decodeAlphabetOctetsAsCharBuffer(restrictedAlphabet);
/* 1316 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   protected final void decodeFourBitAlphabetOctetsAsCharBuffer(char[] restrictedAlphabet) throws FastInfosetException, IOException {
/* 1320 */     this._charBufferLength = 0;
/* 1321 */     int characters = this._octetBufferLength * 2;
/* 1322 */     if (this._charBuffer.length < characters) {
/* 1323 */       this._charBuffer = new char[characters];
/*      */     }
/*      */ 
/* 1326 */     int v = 0;
/* 1327 */     for (int i = 0; i < this._octetBufferLength - 1; i++) {
/* 1328 */       v = this._octetBuffer[(this._octetBufferStart++)] & 0xFF;
/* 1329 */       this._charBuffer[(this._charBufferLength++)] = restrictedAlphabet[(v >> 4)];
/* 1330 */       this._charBuffer[(this._charBufferLength++)] = restrictedAlphabet[(v & 0xF)];
/*      */     }
/* 1332 */     v = this._octetBuffer[(this._octetBufferStart++)] & 0xFF;
/* 1333 */     this._charBuffer[(this._charBufferLength++)] = restrictedAlphabet[(v >> 4)];
/* 1334 */     v &= 15;
/* 1335 */     if (v != 15)
/* 1336 */       this._charBuffer[(this._charBufferLength++)] = restrictedAlphabet[(v & 0xF)];
/*      */   }
/*      */ 
/*      */   protected final void decodeAlphabetOctetsAsCharBuffer(char[] restrictedAlphabet) throws FastInfosetException, IOException
/*      */   {
/* 1341 */     if (restrictedAlphabet.length < 2) {
/* 1342 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.alphabetMustContain2orMoreChars"));
/*      */     }
/*      */ 
/* 1345 */     int bitsPerCharacter = 1;
/* 1346 */     while (1 << bitsPerCharacter <= restrictedAlphabet.length) {
/* 1347 */       bitsPerCharacter++;
/*      */     }
/* 1349 */     int terminatingValue = (1 << bitsPerCharacter) - 1;
/*      */ 
/* 1351 */     int characters = (this._octetBufferLength << 3) / bitsPerCharacter;
/* 1352 */     if (characters == 0) {
/* 1353 */       throw new IOException("");
/*      */     }
/*      */ 
/* 1356 */     this._charBufferLength = 0;
/* 1357 */     if (this._charBuffer.length < characters) {
/* 1358 */       this._charBuffer = new char[characters];
/*      */     }
/*      */ 
/* 1361 */     resetBits();
/* 1362 */     for (int i = 0; i < characters; i++) {
/* 1363 */       int value = readBits(bitsPerCharacter);
/* 1364 */       if ((bitsPerCharacter < 8) && (value == terminatingValue)) {
/* 1365 */         int octetPosition = i * bitsPerCharacter >>> 3;
/* 1366 */         if (octetPosition == this._octetBufferLength - 1) break;
/* 1367 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetIncorrectlyTerminated"));
/*      */       }
/*      */ 
/* 1371 */       this._charBuffer[(this._charBufferLength++)] = restrictedAlphabet[value];
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetBits()
/*      */   {
/* 1378 */     this._bitsLeftInOctet = 0;
/*      */   }
/*      */ 
/*      */   private int readBits(int bits) throws IOException {
/* 1382 */     int value = 0;
/* 1383 */     while (bits > 0) {
/* 1384 */       if (this._bitsLeftInOctet == 0) {
/* 1385 */         this._b = (this._octetBuffer[(this._octetBufferStart++)] & 0xFF);
/* 1386 */         this._bitsLeftInOctet = 8;
/*      */       }
/* 1388 */       int bit = (this._b & 1 << --this._bitsLeftInOctet) > 0 ? 1 : 0;
/* 1389 */       value |= bit << --bits;
/*      */     }
/*      */ 
/* 1392 */     return value;
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf8StringAsCharBuffer() throws IOException {
/* 1396 */     ensureOctetBufferSize();
/* 1397 */     decodeUtf8StringIntoCharBuffer();
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf8StringAsCharBuffer(char[] ch, int offset) throws IOException {
/* 1401 */     ensureOctetBufferSize();
/* 1402 */     decodeUtf8StringIntoCharBuffer(ch, offset);
/*      */   }
/*      */ 
/*      */   protected final String decodeUtf8StringAsString() throws IOException {
/* 1406 */     decodeUtf8StringAsCharBuffer();
/* 1407 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf16StringAsCharBuffer() throws IOException {
/* 1411 */     ensureOctetBufferSize();
/* 1412 */     decodeUtf16StringIntoCharBuffer();
/*      */   }
/*      */ 
/*      */   protected final String decodeUtf16StringAsString() throws IOException {
/* 1416 */     decodeUtf16StringAsCharBuffer();
/* 1417 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   private void ensureOctetBufferSize() throws IOException {
/* 1421 */     if (this._octetBufferEnd < this._octetBufferOffset + this._octetBufferLength) {
/* 1422 */       int octetsInBuffer = this._octetBufferEnd - this._octetBufferOffset;
/*      */ 
/* 1424 */       if (this._octetBuffer.length < this._octetBufferLength)
/*      */       {
/* 1426 */         byte[] newOctetBuffer = new byte[this._octetBufferLength];
/*      */ 
/* 1428 */         System.arraycopy(this._octetBuffer, this._octetBufferOffset, newOctetBuffer, 0, octetsInBuffer);
/* 1429 */         this._octetBuffer = newOctetBuffer;
/*      */       }
/*      */       else {
/* 1432 */         System.arraycopy(this._octetBuffer, this._octetBufferOffset, this._octetBuffer, 0, octetsInBuffer);
/*      */       }
/* 1434 */       this._octetBufferOffset = 0;
/*      */ 
/* 1437 */       int octetsRead = this._s.read(this._octetBuffer, octetsInBuffer, this._octetBuffer.length - octetsInBuffer);
/* 1438 */       if (octetsRead < 0) {
/* 1439 */         throw new EOFException("Unexpeceted EOF");
/*      */       }
/* 1441 */       this._octetBufferEnd = (octetsInBuffer + octetsRead);
/*      */ 
/* 1445 */       if (this._octetBufferEnd < this._octetBufferLength)
/* 1446 */         repeatedRead();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void repeatedRead()
/*      */     throws IOException
/*      */   {
/* 1453 */     while (this._octetBufferEnd < this._octetBufferLength)
/*      */     {
/* 1455 */       int octetsRead = this._s.read(this._octetBuffer, this._octetBufferEnd, this._octetBuffer.length - this._octetBufferEnd);
/* 1456 */       if (octetsRead < 0) {
/* 1457 */         throw new EOFException("Unexpeceted EOF");
/*      */       }
/* 1459 */       this._octetBufferEnd += octetsRead;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf8StringIntoCharBuffer() throws IOException {
/* 1464 */     if (this._charBuffer.length < this._octetBufferLength) {
/* 1465 */       this._charBuffer = new char[this._octetBufferLength];
/*      */     }
/*      */ 
/* 1468 */     this._charBufferLength = 0;
/* 1469 */     int end = this._octetBufferLength + this._octetBufferOffset;
/*      */ 
/* 1471 */     while (end != this._octetBufferOffset) {
/* 1472 */       int b1 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1473 */       if (DecoderStateTables.UTF8(b1) == 1)
/* 1474 */         this._charBuffer[(this._charBufferLength++)] = ((char)b1);
/*      */       else
/* 1476 */         decodeTwoToFourByteUtf8Character(b1, end);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf8StringIntoCharBuffer(char[] ch, int offset) throws IOException
/*      */   {
/* 1482 */     this._charBufferLength = offset;
/* 1483 */     int end = this._octetBufferLength + this._octetBufferOffset;
/*      */ 
/* 1485 */     while (end != this._octetBufferOffset) {
/* 1486 */       int b1 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1487 */       if (DecoderStateTables.UTF8(b1) == 1)
/* 1488 */         ch[(this._charBufferLength++)] = ((char)b1);
/*      */       else {
/* 1490 */         decodeTwoToFourByteUtf8Character(ch, b1, end);
/*      */       }
/*      */     }
/* 1493 */     this._charBufferLength -= offset;
/*      */   }
/*      */ 
/*      */   private void decodeTwoToFourByteUtf8Character(int b1, int end) throws IOException {
/* 1497 */     switch (DecoderStateTables.UTF8(b1))
/*      */     {
/*      */     case 2:
/* 1501 */       if (end == this._octetBufferOffset) {
/* 1502 */         decodeUtf8StringLengthTooSmall();
/*      */       }
/* 1504 */       int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1505 */       if ((b2 & 0xC0) != 128) {
/* 1506 */         decodeUtf8StringIllegalState();
/*      */       }
/*      */ 
/* 1512 */       this._charBuffer[(this._charBufferLength++)] = ((char)((b1 & 0x1F) << 6 | b2 & 0x3F));
/*      */ 
/* 1515 */       break;
/*      */     case 3:
/* 1518 */       char c = decodeUtf8ThreeByteChar(end, b1);
/* 1519 */       if (XMLChar.isContent(c))
/* 1520 */         this._charBuffer[(this._charBufferLength++)] = c;
/*      */       else {
/* 1522 */         decodeUtf8StringIllegalState();
/*      */       }
/* 1524 */       break;
/*      */     case 4:
/* 1527 */       int supplemental = decodeUtf8FourByteChar(end, b1);
/* 1528 */       if (XMLChar.isContent(supplemental)) {
/* 1529 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_highSurrogate;
/* 1530 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_lowSurrogate;
/*      */       } else {
/* 1532 */         decodeUtf8StringIllegalState();
/*      */       }
/* 1534 */       break;
/*      */     default:
/* 1537 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void decodeTwoToFourByteUtf8Character(char[] ch, int b1, int end) throws IOException {
/* 1542 */     switch (DecoderStateTables.UTF8(b1))
/*      */     {
/*      */     case 2:
/* 1546 */       if (end == this._octetBufferOffset) {
/* 1547 */         decodeUtf8StringLengthTooSmall();
/*      */       }
/* 1549 */       int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1550 */       if ((b2 & 0xC0) != 128) {
/* 1551 */         decodeUtf8StringIllegalState();
/*      */       }
/*      */ 
/* 1557 */       ch[(this._charBufferLength++)] = ((char)((b1 & 0x1F) << 6 | b2 & 0x3F));
/*      */ 
/* 1560 */       break;
/*      */     case 3:
/* 1563 */       char c = decodeUtf8ThreeByteChar(end, b1);
/* 1564 */       if (XMLChar.isContent(c))
/* 1565 */         ch[(this._charBufferLength++)] = c;
/*      */       else {
/* 1567 */         decodeUtf8StringIllegalState();
/*      */       }
/* 1569 */       break;
/*      */     case 4:
/* 1572 */       int supplemental = decodeUtf8FourByteChar(end, b1);
/* 1573 */       if (XMLChar.isContent(supplemental)) {
/* 1574 */         ch[(this._charBufferLength++)] = this._utf8_highSurrogate;
/* 1575 */         ch[(this._charBufferLength++)] = this._utf8_lowSurrogate;
/*      */       } else {
/* 1577 */         decodeUtf8StringIllegalState();
/*      */       }
/* 1579 */       break;
/*      */     default:
/* 1582 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void decodeUtf8NCNameIntoCharBuffer() throws IOException {
/* 1587 */     this._charBufferLength = 0;
/* 1588 */     if (this._charBuffer.length < this._octetBufferLength) {
/* 1589 */       this._charBuffer = new char[this._octetBufferLength];
/*      */     }
/*      */ 
/* 1592 */     int end = this._octetBufferLength + this._octetBufferOffset;
/*      */ 
/* 1594 */     int b1 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1595 */     if (DecoderStateTables.UTF8_NCNAME(b1) == 0)
/* 1596 */       this._charBuffer[(this._charBufferLength++)] = ((char)b1);
/*      */     else {
/* 1598 */       decodeUtf8NCNameStartTwoToFourByteCharacters(b1, end);
/*      */     }
/*      */ 
/* 1601 */     while (end != this._octetBufferOffset) {
/* 1602 */       b1 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1603 */       if (DecoderStateTables.UTF8_NCNAME(b1) < 2)
/* 1604 */         this._charBuffer[(this._charBufferLength++)] = ((char)b1);
/*      */       else
/* 1606 */         decodeUtf8NCNameTwoToFourByteCharacters(b1, end);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void decodeUtf8NCNameStartTwoToFourByteCharacters(int b1, int end) throws IOException
/*      */   {
/* 1612 */     switch (DecoderStateTables.UTF8_NCNAME(b1))
/*      */     {
/*      */     case 2:
/* 1616 */       if (end == this._octetBufferOffset) {
/* 1617 */         decodeUtf8StringLengthTooSmall();
/*      */       }
/* 1619 */       int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1620 */       if ((b2 & 0xC0) != 128) {
/* 1621 */         decodeUtf8StringIllegalState();
/*      */       }
/*      */ 
/* 1624 */       char c = (char)((b1 & 0x1F) << 6 | b2 & 0x3F);
/*      */ 
/* 1627 */       if (XMLChar.isNCNameStart(c))
/* 1628 */         this._charBuffer[(this._charBufferLength++)] = c;
/*      */       else {
/* 1630 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1632 */       break;
/*      */     case 3:
/* 1635 */       char c = decodeUtf8ThreeByteChar(end, b1);
/* 1636 */       if (XMLChar.isNCNameStart(c))
/* 1637 */         this._charBuffer[(this._charBufferLength++)] = c;
/*      */       else {
/* 1639 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1641 */       break;
/*      */     case 4:
/* 1644 */       int supplemental = decodeUtf8FourByteChar(end, b1);
/* 1645 */       if (XMLChar.isNCNameStart(supplemental)) {
/* 1646 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_highSurrogate;
/* 1647 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_lowSurrogate;
/*      */       } else {
/* 1649 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1651 */       break;
/*      */     case 1:
/*      */     default:
/* 1655 */       decodeUtf8NCNameIllegalState();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void decodeUtf8NCNameTwoToFourByteCharacters(int b1, int end) throws IOException
/*      */   {
/* 1661 */     switch (DecoderStateTables.UTF8_NCNAME(b1))
/*      */     {
/*      */     case 2:
/* 1665 */       if (end == this._octetBufferOffset) {
/* 1666 */         decodeUtf8StringLengthTooSmall();
/*      */       }
/* 1668 */       int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1669 */       if ((b2 & 0xC0) != 128) {
/* 1670 */         decodeUtf8StringIllegalState();
/*      */       }
/*      */ 
/* 1673 */       char c = (char)((b1 & 0x1F) << 6 | b2 & 0x3F);
/*      */ 
/* 1676 */       if (XMLChar.isNCName(c))
/* 1677 */         this._charBuffer[(this._charBufferLength++)] = c;
/*      */       else {
/* 1679 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1681 */       break;
/*      */     case 3:
/* 1684 */       char c = decodeUtf8ThreeByteChar(end, b1);
/* 1685 */       if (XMLChar.isNCName(c))
/* 1686 */         this._charBuffer[(this._charBufferLength++)] = c;
/*      */       else {
/* 1688 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1690 */       break;
/*      */     case 4:
/* 1693 */       int supplemental = decodeUtf8FourByteChar(end, b1);
/* 1694 */       if (XMLChar.isNCName(supplemental)) {
/* 1695 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_highSurrogate;
/* 1696 */         this._charBuffer[(this._charBufferLength++)] = this._utf8_lowSurrogate;
/*      */       } else {
/* 1698 */         decodeUtf8NCNameIllegalState();
/*      */       }
/* 1700 */       break;
/*      */     default:
/* 1703 */       decodeUtf8NCNameIllegalState();
/*      */     }
/*      */   }
/*      */ 
/*      */   private char decodeUtf8ThreeByteChar(int end, int b1) throws IOException
/*      */   {
/* 1709 */     if (end == this._octetBufferOffset) {
/* 1710 */       decodeUtf8StringLengthTooSmall();
/*      */     }
/* 1712 */     int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1713 */     if (((b2 & 0xC0) != 128) || ((b1 == 237) && (b2 >= 160)) || (((b1 & 0xF) == 0) && ((b2 & 0x20) == 0)))
/*      */     {
/* 1716 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */ 
/* 1720 */     if (end == this._octetBufferOffset) {
/* 1721 */       decodeUtf8StringLengthTooSmall();
/*      */     }
/* 1723 */     int b3 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1724 */     if ((b3 & 0xC0) != 128) {
/* 1725 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */ 
/* 1728 */     return (char)((b1 & 0xF) << 12 | (b2 & 0x3F) << 6 | b3 & 0x3F);
/*      */   }
/*      */ 
/*      */   private int decodeUtf8FourByteChar(int end, int b1)
/*      */     throws IOException
/*      */   {
/* 1739 */     if (end == this._octetBufferOffset) {
/* 1740 */       decodeUtf8StringLengthTooSmall();
/*      */     }
/* 1742 */     int b2 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1743 */     if (((b2 & 0xC0) != 128) || (((b2 & 0x30) == 0) && ((b1 & 0x7) == 0)))
/*      */     {
/* 1745 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */ 
/* 1749 */     if (end == this._octetBufferOffset) {
/* 1750 */       decodeUtf8StringLengthTooSmall();
/*      */     }
/* 1752 */     int b3 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1753 */     if ((b3 & 0xC0) != 128) {
/* 1754 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */ 
/* 1758 */     if (end == this._octetBufferOffset) {
/* 1759 */       decodeUtf8StringLengthTooSmall();
/*      */     }
/* 1761 */     int b4 = this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/* 1762 */     if ((b4 & 0xC0) != 128) {
/* 1763 */       decodeUtf8StringIllegalState();
/*      */     }
/*      */ 
/* 1766 */     int uuuuu = b1 << 2 & 0x1C | b2 >> 4 & 0x3;
/* 1767 */     if (uuuuu > 16) {
/* 1768 */       decodeUtf8StringIllegalState();
/*      */     }
/* 1770 */     int wwww = uuuuu - 1;
/*      */ 
/* 1772 */     this._utf8_highSurrogate = ((char)(0xD800 | wwww << 6 & 0x3C0 | b2 << 2 & 0x3C | b3 >> 4 & 0x3));
/*      */ 
/* 1775 */     this._utf8_lowSurrogate = ((char)(0xDC00 | b3 << 6 & 0x3C0 | b4 & 0x3F));
/*      */ 
/* 1777 */     return XMLChar.supplemental(this._utf8_highSurrogate, this._utf8_lowSurrogate);
/*      */   }
/*      */ 
/*      */   private void decodeUtf8StringLengthTooSmall() throws IOException {
/* 1781 */     throw new IOException(CommonResourceBundle.getInstance().getString("message.deliminatorTooSmall"));
/*      */   }
/*      */ 
/*      */   private void decodeUtf8StringIllegalState() throws IOException {
/* 1785 */     throw new IOException(CommonResourceBundle.getInstance().getString("message.UTF8Encoded"));
/*      */   }
/*      */ 
/*      */   private void decodeUtf8NCNameIllegalState() throws IOException {
/* 1789 */     throw new IOException(CommonResourceBundle.getInstance().getString("message.UTF8EncodedNCName"));
/*      */   }
/*      */ 
/*      */   private void decodeUtf16StringIntoCharBuffer() throws IOException {
/* 1793 */     this._charBufferLength = (this._octetBufferLength / 2);
/* 1794 */     if (this._charBuffer.length < this._charBufferLength) {
/* 1795 */       this._charBuffer = new char[this._charBufferLength];
/*      */     }
/*      */ 
/* 1798 */     for (int i = 0; i < this._charBufferLength; i++) {
/* 1799 */       char c = (char)(read() << 8 | read());
/*      */ 
/* 1801 */       this._charBuffer[i] = c;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String createQualifiedNameString(String second)
/*      */   {
/* 1807 */     return createQualifiedNameString(XMLNS_NAMESPACE_PREFIX_CHARS, second);
/*      */   }
/*      */ 
/*      */   protected String createQualifiedNameString(char[] first, String second) {
/* 1811 */     int l1 = first.length;
/* 1812 */     int l2 = second.length();
/* 1813 */     int total = l1 + l2 + 1;
/* 1814 */     if (total < this._charBuffer.length) {
/* 1815 */       System.arraycopy(first, 0, this._charBuffer, 0, l1);
/* 1816 */       this._charBuffer[l1] = ':';
/* 1817 */       second.getChars(0, l2, this._charBuffer, l1 + 1);
/* 1818 */       return new String(this._charBuffer, 0, total);
/*      */     }
/* 1820 */     StringBuffer b = new StringBuffer(new String(first));
/* 1821 */     b.append(':');
/* 1822 */     b.append(second);
/* 1823 */     return b.toString();
/*      */   }
/*      */ 
/*      */   protected final int read() throws IOException
/*      */   {
/* 1828 */     if (this._octetBufferOffset < this._octetBufferEnd) {
/* 1829 */       return this._octetBuffer[(this._octetBufferOffset++)] & 0xFF;
/*      */     }
/* 1831 */     this._octetBufferEnd = this._s.read(this._octetBuffer);
/* 1832 */     if (this._octetBufferEnd < 0) {
/* 1833 */       throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
/*      */     }
/*      */ 
/* 1836 */     this._octetBufferOffset = 1;
/* 1837 */     return this._octetBuffer[0] & 0xFF;
/*      */   }
/*      */ 
/*      */   protected final void closeIfRequired() throws IOException
/*      */   {
/* 1842 */     if ((this._s != null) && (this._needForceStreamClose))
/* 1843 */       this._s.close();
/*      */   }
/*      */ 
/*      */   protected final int peek() throws IOException
/*      */   {
/* 1848 */     return peek(null);
/*      */   }
/*      */ 
/*      */   protected final int peek(OctetBufferListener octetBufferListener) throws IOException {
/* 1852 */     if (this._octetBufferOffset < this._octetBufferEnd) {
/* 1853 */       return this._octetBuffer[this._octetBufferOffset] & 0xFF;
/*      */     }
/* 1855 */     if (octetBufferListener != null) {
/* 1856 */       octetBufferListener.onBeforeOctetBufferOverwrite();
/*      */     }
/*      */ 
/* 1859 */     this._octetBufferEnd = this._s.read(this._octetBuffer);
/* 1860 */     if (this._octetBufferEnd < 0) {
/* 1861 */       throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
/*      */     }
/*      */ 
/* 1864 */     this._octetBufferOffset = 0;
/* 1865 */     return this._octetBuffer[0] & 0xFF;
/*      */   }
/*      */ 
/*      */   protected final int peek2(OctetBufferListener octetBufferListener) throws IOException
/*      */   {
/* 1870 */     if (this._octetBufferOffset + 1 < this._octetBufferEnd) {
/* 1871 */       return this._octetBuffer[(this._octetBufferOffset + 1)] & 0xFF;
/*      */     }
/* 1873 */     if (octetBufferListener != null) {
/* 1874 */       octetBufferListener.onBeforeOctetBufferOverwrite();
/*      */     }
/*      */ 
/* 1877 */     int offset = 0;
/* 1878 */     if (this._octetBufferOffset < this._octetBufferEnd) {
/* 1879 */       this._octetBuffer[0] = this._octetBuffer[this._octetBufferOffset];
/* 1880 */       offset = 1;
/*      */     }
/* 1882 */     this._octetBufferEnd = this._s.read(this._octetBuffer, offset, this._octetBuffer.length - offset);
/*      */ 
/* 1884 */     if (this._octetBufferEnd < 0) {
/* 1885 */       throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
/*      */     }
/*      */ 
/* 1888 */     this._octetBufferOffset = 0;
/* 1889 */     return this._octetBuffer[1] & 0xFF;
/*      */   }
/*      */ 
/*      */   protected final boolean _isFastInfosetDocument()
/*      */     throws IOException
/*      */   {
/* 1937 */     peek();
/*      */ 
/* 1939 */     this._octetBufferLength = EncodingConstants.BINARY_HEADER.length;
/* 1940 */     ensureOctetBufferSize();
/* 1941 */     this._octetBufferOffset += this._octetBufferLength;
/*      */ 
/* 1944 */     if ((this._octetBuffer[0] != EncodingConstants.BINARY_HEADER[0]) || (this._octetBuffer[1] != EncodingConstants.BINARY_HEADER[1]) || (this._octetBuffer[2] != EncodingConstants.BINARY_HEADER[2]) || (this._octetBuffer[3] != EncodingConstants.BINARY_HEADER[3]))
/*      */     {
/* 1950 */       for (int i = 0; i < EncodingConstants.XML_DECLARATION_VALUES.length; i++) {
/* 1951 */         this._octetBufferLength = (EncodingConstants.XML_DECLARATION_VALUES[i].length - this._octetBufferOffset);
/* 1952 */         ensureOctetBufferSize();
/* 1953 */         this._octetBufferOffset += this._octetBufferLength;
/*      */ 
/* 1956 */         if (arrayEquals(this._octetBuffer, 0, EncodingConstants.XML_DECLARATION_VALUES[i], EncodingConstants.XML_DECLARATION_VALUES[i].length))
/*      */         {
/* 1959 */           this._octetBufferLength = EncodingConstants.BINARY_HEADER.length;
/* 1960 */           ensureOctetBufferSize();
/*      */ 
/* 1963 */           if ((this._octetBuffer[(this._octetBufferOffset++)] != EncodingConstants.BINARY_HEADER[0]) || (this._octetBuffer[(this._octetBufferOffset++)] != EncodingConstants.BINARY_HEADER[1]) || (this._octetBuffer[(this._octetBufferOffset++)] != EncodingConstants.BINARY_HEADER[2]) || (this._octetBuffer[(this._octetBufferOffset++)] != EncodingConstants.BINARY_HEADER[3]))
/*      */           {
/* 1967 */             return false;
/*      */           }
/*      */ 
/* 1970 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1975 */       return false;
/*      */     }
/*      */ 
/* 1979 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean arrayEquals(byte[] b1, int offset, byte[] b2, int length) {
/* 1983 */     for (int i = 0; i < length; i++) {
/* 1984 */       if (b1[(offset + i)] != b2[i]) {
/* 1985 */         return false;
/*      */       }
/*      */     }
/*      */ 
/* 1989 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isFastInfosetDocument(InputStream s)
/*      */     throws IOException
/*      */   {
/* 1996 */     byte[] header = new byte[4];
/* 1997 */     s.read(header);
/* 1998 */     if ((header[0] != EncodingConstants.BINARY_HEADER[0]) || (header[1] != EncodingConstants.BINARY_HEADER[1]) || (header[2] != EncodingConstants.BINARY_HEADER[2]) || (header[3] != EncodingConstants.BINARY_HEADER[3]))
/*      */     {
/* 2002 */       return false;
/*      */     }
/*      */ 
/* 2006 */     return true;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  102 */     String p = System.getProperty("com.sun.xml.internal.fastinfoset.parser.string-interning", Boolean.toString(_stringInterningSystemDefault));
/*      */ 
/*  104 */     _stringInterningSystemDefault = Boolean.valueOf(p).booleanValue();
/*      */ 
/*  106 */     p = System.getProperty("com.sun.xml.internal.fastinfoset.parser.buffer-size", Integer.toString(_bufferSizeSystemDefault));
/*      */     try
/*      */     {
/*  109 */       int i = Integer.valueOf(p).intValue();
/*  110 */       if (i > 0)
/*  111 */         _bufferSizeSystemDefault = i;
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class EncodingAlgorithmInputStream extends InputStream
/*      */   {
/*      */     protected EncodingAlgorithmInputStream()
/*      */     {
/*      */     }
/*      */ 
/*      */     public int read()
/*      */       throws IOException
/*      */     {
/* 1896 */       if (Decoder.this._octetBufferStart < Decoder.this._octetBufferOffset) {
/* 1897 */         return Decoder.this._octetBuffer[(Decoder.this._octetBufferStart++)] & 0xFF;
/*      */       }
/* 1899 */       return -1;
/*      */     }
/*      */ 
/*      */     public int read(byte[] b)
/*      */       throws IOException
/*      */     {
/* 1905 */       return read(b, 0, b.length);
/*      */     }
/*      */ 
/*      */     public int read(byte[] b, int off, int len) throws IOException
/*      */     {
/* 1910 */       if (b == null)
/* 1911 */         throw new NullPointerException();
/* 1912 */       if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*      */       {
/* 1914 */         throw new IndexOutOfBoundsException();
/* 1915 */       }if (len == 0) {
/* 1916 */         return 0;
/*      */       }
/*      */ 
/* 1919 */       int newOctetBufferStart = Decoder.this._octetBufferStart + len;
/* 1920 */       if (newOctetBufferStart < Decoder.this._octetBufferOffset) {
/* 1921 */         System.arraycopy(Decoder.this._octetBuffer, Decoder.this._octetBufferStart, b, off, len);
/* 1922 */         Decoder.this._octetBufferStart = newOctetBufferStart;
/* 1923 */         return len;
/* 1924 */       }if (Decoder.this._octetBufferStart < Decoder.this._octetBufferOffset) {
/* 1925 */         int bytesToRead = Decoder.this._octetBufferOffset - Decoder.this._octetBufferStart;
/* 1926 */         System.arraycopy(Decoder.this._octetBuffer, Decoder.this._octetBufferStart, b, off, bytesToRead);
/* 1927 */         Decoder.this._octetBufferStart += bytesToRead;
/* 1928 */         return bytesToRead;
/*      */       }
/* 1930 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.Decoder
 * JD-Core Version:    0.6.2
 */