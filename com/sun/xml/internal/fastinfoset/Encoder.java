/*      */ package com.sun.xml.internal.fastinfoset;
/*      */ 
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*      */ import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
/*      */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
/*      */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap.Entry;
/*      */ import com.sun.xml.internal.fastinfoset.util.StringIntMap;
/*      */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.VocabularyApplicationData;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ public abstract class Encoder extends DefaultHandler
/*      */   implements FastInfosetSerializer
/*      */ {
/*      */   public static final String CHARACTER_ENCODING_SCHEME_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.serializer.character-encoding-scheme";
/*   87 */   protected static final String _characterEncodingSchemeSystemDefault = getDefaultEncodingScheme();
/*      */ 
/*  104 */   private static int[] NUMERIC_CHARACTERS_TABLE = new int[maxCharacter("0123456789-+.E ") + 1];
/*  105 */   private static int[] DATE_TIME_CHARACTERS_TABLE = new int[maxCharacter("0123456789-:TZ ") + 1];
/*      */   private boolean _ignoreDTD;
/*      */   private boolean _ignoreComments;
/*      */   private boolean _ignoreProcessingInstructions;
/*      */   private boolean _ignoreWhiteSpaceTextContent;
/*      */   private boolean _useLocalNameAsKeyForQualifiedNameLookup;
/*  166 */   private boolean _encodingStringsAsUtf8 = true;
/*      */   private int _nonIdentifyingStringOnThirdBitCES;
/*      */   private int _nonIdentifyingStringOnFirstBitCES;
/*  181 */   private Map _registeredEncodingAlgorithms = new HashMap();
/*      */   protected SerializerVocabulary _v;
/*      */   protected VocabularyApplicationData _vData;
/*      */   private boolean _vIsInternal;
/*  201 */   protected boolean _terminate = false;
/*      */   protected int _b;
/*      */   protected OutputStream _s;
/*  218 */   protected char[] _charBuffer = new char[512];
/*      */ 
/*  223 */   protected byte[] _octetBuffer = new byte[1024];
/*      */   protected int _octetBufferIndex;
/*  236 */   protected int _markIndex = -1;
/*      */ 
/*  242 */   protected int minAttributeValueSize = 0;
/*      */ 
/*  248 */   protected int maxAttributeValueSize = 32;
/*      */ 
/*  254 */   protected int attributeValueMapTotalCharactersConstraint = 1073741823;
/*      */ 
/*  261 */   protected int minCharacterContentChunkSize = 0;
/*      */ 
/*  268 */   protected int maxCharacterContentChunkSize = 32;
/*      */ 
/*  274 */   protected int characterContentChunkMapTotalCharactersConstraint = 1073741823;
/*      */   private int _bitsLeftInOctet;
/* 2365 */   private EncodingBufferOutputStream _encodingBufferOutputStream = new EncodingBufferOutputStream(null);
/*      */ 
/* 2367 */   private byte[] _encodingBuffer = new byte[512];
/*      */   private int _encodingBufferIndex;
/*      */ 
/*      */   private static String getDefaultEncodingScheme()
/*      */   {
/*   90 */     String p = System.getProperty("com.sun.xml.internal.fastinfoset.serializer.character-encoding-scheme", "UTF-8");
/*      */ 
/*   92 */     if (p.equals("UTF-16BE")) {
/*   93 */       return "UTF-16BE";
/*      */     }
/*   95 */     return "UTF-8";
/*      */   }
/*      */ 
/*      */   private static int maxCharacter(String alphabet)
/*      */   {
/*  123 */     int c = 0;
/*  124 */     for (int i = 0; i < alphabet.length(); i++) {
/*  125 */       if (c < alphabet.charAt(i)) {
/*  126 */         c = alphabet.charAt(i);
/*      */       }
/*      */     }
/*      */ 
/*  130 */     return c;
/*      */   }
/*      */ 
/*      */   protected Encoder()
/*      */   {
/*  280 */     setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
/*      */   }
/*      */ 
/*      */   protected Encoder(boolean useLocalNameAsKeyForQualifiedNameLookup) {
/*  284 */     setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
/*  285 */     this._useLocalNameAsKeyForQualifiedNameLookup = useLocalNameAsKeyForQualifiedNameLookup;
/*      */   }
/*      */ 
/*      */   public final void setIgnoreDTD(boolean ignoreDTD)
/*      */   {
/*  295 */     this._ignoreDTD = ignoreDTD;
/*      */   }
/*      */ 
/*      */   public final boolean getIgnoreDTD()
/*      */   {
/*  302 */     return this._ignoreDTD;
/*      */   }
/*      */ 
/*      */   public final void setIgnoreComments(boolean ignoreComments)
/*      */   {
/*  309 */     this._ignoreComments = ignoreComments;
/*      */   }
/*      */ 
/*      */   public final boolean getIgnoreComments()
/*      */   {
/*  316 */     return this._ignoreComments;
/*      */   }
/*      */ 
/*      */   public final void setIgnoreProcesingInstructions(boolean ignoreProcesingInstructions)
/*      */   {
/*  324 */     this._ignoreProcessingInstructions = ignoreProcesingInstructions;
/*      */   }
/*      */ 
/*      */   public final boolean getIgnoreProcesingInstructions()
/*      */   {
/*  331 */     return this._ignoreProcessingInstructions;
/*      */   }
/*      */ 
/*      */   public final void setIgnoreWhiteSpaceTextContent(boolean ignoreWhiteSpaceTextContent)
/*      */   {
/*  338 */     this._ignoreWhiteSpaceTextContent = ignoreWhiteSpaceTextContent;
/*      */   }
/*      */ 
/*      */   public final boolean getIgnoreWhiteSpaceTextContent()
/*      */   {
/*  345 */     return this._ignoreWhiteSpaceTextContent;
/*      */   }
/*      */ 
/*      */   public void setCharacterEncodingScheme(String characterEncodingScheme)
/*      */   {
/*  352 */     if (characterEncodingScheme.equals("UTF-16BE")) {
/*  353 */       this._encodingStringsAsUtf8 = false;
/*  354 */       this._nonIdentifyingStringOnThirdBitCES = 132;
/*  355 */       this._nonIdentifyingStringOnFirstBitCES = 16;
/*      */     } else {
/*  357 */       this._encodingStringsAsUtf8 = true;
/*  358 */       this._nonIdentifyingStringOnThirdBitCES = 128;
/*  359 */       this._nonIdentifyingStringOnFirstBitCES = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getCharacterEncodingScheme()
/*      */   {
/*  367 */     return this._encodingStringsAsUtf8 ? "UTF-8" : "UTF-16BE";
/*      */   }
/*      */ 
/*      */   public void setRegisteredEncodingAlgorithms(Map algorithms)
/*      */   {
/*  374 */     this._registeredEncodingAlgorithms = algorithms;
/*  375 */     if (this._registeredEncodingAlgorithms == null)
/*  376 */       this._registeredEncodingAlgorithms = new HashMap();
/*      */   }
/*      */ 
/*      */   public Map getRegisteredEncodingAlgorithms()
/*      */   {
/*  384 */     return this._registeredEncodingAlgorithms;
/*      */   }
/*      */ 
/*      */   public int getMinCharacterContentChunkSize()
/*      */   {
/*  391 */     return this.minCharacterContentChunkSize;
/*      */   }
/*      */ 
/*      */   public void setMinCharacterContentChunkSize(int size)
/*      */   {
/*  398 */     if (size < 0) {
/*  399 */       size = 0;
/*      */     }
/*      */ 
/*  402 */     this.minCharacterContentChunkSize = size;
/*      */   }
/*      */ 
/*      */   public int getMaxCharacterContentChunkSize()
/*      */   {
/*  409 */     return this.maxCharacterContentChunkSize;
/*      */   }
/*      */ 
/*      */   public void setMaxCharacterContentChunkSize(int size)
/*      */   {
/*  416 */     if (size < 0) {
/*  417 */       size = 0;
/*      */     }
/*      */ 
/*  420 */     this.maxCharacterContentChunkSize = size;
/*      */   }
/*      */ 
/*      */   public int getCharacterContentChunkMapMemoryLimit()
/*      */   {
/*  427 */     return this.characterContentChunkMapTotalCharactersConstraint * 2;
/*      */   }
/*      */ 
/*      */   public void setCharacterContentChunkMapMemoryLimit(int size)
/*      */   {
/*  434 */     if (size < 0) {
/*  435 */       size = 0;
/*      */     }
/*      */ 
/*  438 */     this.characterContentChunkMapTotalCharactersConstraint = (size / 2);
/*      */   }
/*      */ 
/*      */   public boolean isCharacterContentChunkLengthMatchesLimit(int length)
/*      */   {
/*  448 */     return (length >= this.minCharacterContentChunkSize) && (length < this.maxCharacterContentChunkSize);
/*      */   }
/*      */ 
/*      */   public boolean canAddCharacterContentToTable(int length, CharArrayIntMap map)
/*      */   {
/*  461 */     return map.getTotalCharacterCount() + length < this.characterContentChunkMapTotalCharactersConstraint;
/*      */   }
/*      */ 
/*      */   public int getMinAttributeValueSize()
/*      */   {
/*  469 */     return this.minAttributeValueSize;
/*      */   }
/*      */ 
/*      */   public void setMinAttributeValueSize(int size)
/*      */   {
/*  476 */     if (size < 0) {
/*  477 */       size = 0;
/*      */     }
/*      */ 
/*  480 */     this.minAttributeValueSize = size;
/*      */   }
/*      */ 
/*      */   public int getMaxAttributeValueSize()
/*      */   {
/*  487 */     return this.maxAttributeValueSize;
/*      */   }
/*      */ 
/*      */   public void setMaxAttributeValueSize(int size)
/*      */   {
/*  494 */     if (size < 0) {
/*  495 */       size = 0;
/*      */     }
/*      */ 
/*  498 */     this.maxAttributeValueSize = size;
/*      */   }
/*      */ 
/*      */   public void setAttributeValueMapMemoryLimit(int size)
/*      */   {
/*  505 */     if (size < 0) {
/*  506 */       size = 0;
/*      */     }
/*      */ 
/*  509 */     this.attributeValueMapTotalCharactersConstraint = (size / 2);
/*      */   }
/*      */ 
/*      */   public int getAttributeValueMapMemoryLimit()
/*      */   {
/*  517 */     return this.attributeValueMapTotalCharactersConstraint * 2;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeValueLengthMatchesLimit(int length)
/*      */   {
/*  527 */     return (length >= this.minAttributeValueSize) && (length < this.maxAttributeValueSize);
/*      */   }
/*      */ 
/*      */   public boolean canAddAttributeToTable(int length)
/*      */   {
/*  539 */     return this._v.attributeValue.getTotalCharacterCount() + length < this.attributeValueMapTotalCharactersConstraint;
/*      */   }
/*      */ 
/*      */   public void setExternalVocabulary(ExternalVocabulary v)
/*      */   {
/*  548 */     this._v = new SerializerVocabulary();
/*      */ 
/*  550 */     SerializerVocabulary ev = new SerializerVocabulary(v.vocabulary, this._useLocalNameAsKeyForQualifiedNameLookup);
/*      */ 
/*  552 */     this._v.setExternalVocabulary(v.URI, ev, false);
/*      */ 
/*  555 */     this._vIsInternal = true;
/*      */   }
/*      */ 
/*      */   public void setVocabularyApplicationData(VocabularyApplicationData data)
/*      */   {
/*  562 */     this._vData = data;
/*      */   }
/*      */ 
/*      */   public VocabularyApplicationData getVocabularyApplicationData()
/*      */   {
/*  569 */     return this._vData;
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  578 */     this._terminate = false;
/*      */   }
/*      */ 
/*      */   public void setOutputStream(OutputStream s)
/*      */   {
/*  588 */     this._octetBufferIndex = 0;
/*  589 */     this._markIndex = -1;
/*  590 */     this._s = s;
/*      */   }
/*      */ 
/*      */   public void setVocabulary(SerializerVocabulary vocabulary)
/*      */   {
/*  599 */     this._v = vocabulary;
/*  600 */     this._vIsInternal = false;
/*      */   }
/*      */ 
/*      */   protected final void encodeHeader(boolean encodeXmlDecl)
/*      */     throws IOException
/*      */   {
/*  609 */     if (encodeXmlDecl) {
/*  610 */       this._s.write(EncodingConstants.XML_DECLARATION_VALUES[0]);
/*      */     }
/*  612 */     this._s.write(EncodingConstants.BINARY_HEADER);
/*      */   }
/*      */ 
/*      */   protected final void encodeInitialVocabulary()
/*      */     throws IOException
/*      */   {
/*  620 */     if (this._v == null) {
/*  621 */       this._v = new SerializerVocabulary();
/*  622 */       this._vIsInternal = true;
/*  623 */     } else if (this._vIsInternal) {
/*  624 */       this._v.clear();
/*  625 */       if (this._vData != null) {
/*  626 */         this._vData.clear();
/*      */       }
/*      */     }
/*  629 */     if ((!this._v.hasInitialVocabulary()) && (!this._v.hasExternalVocabulary())) {
/*  630 */       write(0);
/*  631 */     } else if (this._v.hasInitialVocabulary()) {
/*  632 */       this._b = 32;
/*  633 */       write(this._b);
/*      */ 
/*  635 */       SerializerVocabulary initialVocabulary = this._v.getReadOnlyVocabulary();
/*      */ 
/*  638 */       if (initialVocabulary.hasExternalVocabulary()) {
/*  639 */         this._b = 16;
/*  640 */         write(this._b);
/*  641 */         write(0);
/*      */       }
/*      */ 
/*  644 */       if (initialVocabulary.hasExternalVocabulary()) {
/*  645 */         encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
/*      */       }
/*      */ 
/*      */     }
/*  649 */     else if (this._v.hasExternalVocabulary()) {
/*  650 */       this._b = 32;
/*  651 */       write(this._b);
/*      */ 
/*  653 */       this._b = 16;
/*  654 */       write(this._b);
/*  655 */       write(0);
/*      */ 
/*  657 */       encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeDocumentTermination()
/*      */     throws IOException
/*      */   {
/*  666 */     encodeElementTermination();
/*  667 */     encodeTermination();
/*  668 */     _flush();
/*  669 */     this._s.flush();
/*      */   }
/*      */ 
/*      */   protected final void encodeElementTermination()
/*      */     throws IOException
/*      */   {
/*  677 */     this._terminate = true;
/*  678 */     switch (this._b) {
/*      */     case 240:
/*  680 */       this._b = 255;
/*  681 */       break;
/*      */     case 255:
/*  683 */       write(255);
/*      */     default:
/*  685 */       this._b = 240;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeTermination()
/*      */     throws IOException
/*      */   {
/*  694 */     if (this._terminate) {
/*  695 */       write(this._b);
/*  696 */       this._b = 0;
/*  697 */       this._terminate = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNamespaceAttribute(String prefix, String uri)
/*      */     throws IOException
/*      */   {
/*  710 */     this._b = 204;
/*  711 */     if (prefix.length() > 0) {
/*  712 */       this._b |= 2;
/*      */     }
/*  714 */     if (uri.length() > 0) {
/*  715 */       this._b |= 1;
/*      */     }
/*      */ 
/*  723 */     write(this._b);
/*      */ 
/*  725 */     if (prefix.length() > 0) {
/*  726 */       encodeIdentifyingNonEmptyStringOnFirstBit(prefix, this._v.prefix);
/*      */     }
/*  728 */     if (uri.length() > 0)
/*  729 */       encodeIdentifyingNonEmptyStringOnFirstBit(uri, this._v.namespaceName);
/*      */   }
/*      */ 
/*      */   protected final void encodeCharacters(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  742 */     boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
/*  743 */     encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, true);
/*      */   }
/*      */ 
/*      */   protected final void encodeCharactersNoClone(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  759 */     boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
/*  760 */     encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, false);
/*      */   }
/*      */ 
/*      */   protected final void encodeNumericFourBitCharacters(char[] ch, int offset, int length, boolean addToTable)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  778 */     encodeFourBitCharacters(0, NUMERIC_CHARACTERS_TABLE, ch, offset, length, addToTable);
/*      */   }
/*      */ 
/*      */   protected final void encodeDateTimeFourBitCharacters(char[] ch, int offset, int length, boolean addToTable)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  797 */     encodeFourBitCharacters(1, DATE_TIME_CHARACTERS_TABLE, ch, offset, length, addToTable);
/*      */   }
/*      */ 
/*      */   protected final void encodeFourBitCharacters(int id, int[] table, char[] ch, int offset, int length, boolean addToTable)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  816 */     if (addToTable)
/*      */     {
/*  818 */       boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
/*      */ 
/*  822 */       int index = canAddCharacterContentToTable ? this._v.characterContentChunk.obtainIndex(ch, offset, length, true) : this._v.characterContentChunk.get(ch, offset, length);
/*      */ 
/*  826 */       if (index != -1)
/*      */       {
/*  828 */         this._b = 160;
/*  829 */         encodeNonZeroIntegerOnFourthBit(index);
/*  830 */         return;
/*  831 */       }if (canAddCharacterContentToTable)
/*      */       {
/*  833 */         this._b = 152;
/*      */       }
/*      */       else
/*  836 */         this._b = 136;
/*      */     }
/*      */     else {
/*  839 */       this._b = 136;
/*      */     }
/*      */ 
/*  842 */     write(this._b);
/*      */ 
/*  845 */     this._b = (id << 2);
/*      */ 
/*  847 */     encodeNonEmptyFourBitCharacterStringOnSeventhBit(table, ch, offset, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeAlphabetCharacters(String alphabet, char[] ch, int offset, int length, boolean addToTable)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  866 */     if (addToTable)
/*      */     {
/*  868 */       boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
/*      */ 
/*  872 */       int index = canAddCharacterContentToTable ? this._v.characterContentChunk.obtainIndex(ch, offset, length, true) : this._v.characterContentChunk.get(ch, offset, length);
/*      */ 
/*  876 */       if (index != -1)
/*      */       {
/*  878 */         this._b = 160;
/*  879 */         encodeNonZeroIntegerOnFourthBit(index);
/*  880 */         return;
/*  881 */       }if (canAddCharacterContentToTable)
/*      */       {
/*  883 */         this._b = 152;
/*      */       }
/*      */       else
/*  886 */         this._b = 136;
/*      */     }
/*      */     else {
/*  889 */       this._b = 136;
/*      */     }
/*      */ 
/*  892 */     int id = this._v.restrictedAlphabet.get(alphabet);
/*  893 */     if (id == -1) {
/*  894 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.restrictedAlphabetNotPresent"));
/*      */     }
/*  896 */     id += 32;
/*      */ 
/*  898 */     this._b |= (id & 0xC0) >> 6;
/*  899 */     write(this._b);
/*      */ 
/*  902 */     this._b = ((id & 0x3F) << 2);
/*      */ 
/*  904 */     encodeNonEmptyNBitCharacterStringOnSeventhBit(alphabet, ch, offset, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeProcessingInstruction(String target, String data)
/*      */     throws IOException
/*      */   {
/*  914 */     write(225);
/*      */ 
/*  917 */     encodeIdentifyingNonEmptyStringOnFirstBit(target, this._v.otherNCName);
/*      */ 
/*  920 */     boolean addToTable = isCharacterContentChunkLengthMatchesLimit(data.length());
/*  921 */     encodeNonIdentifyingStringOnFirstBit(data, this._v.otherString, addToTable);
/*      */   }
/*      */ 
/*      */   protected final void encodeDocumentTypeDeclaration(String systemId, String publicId)
/*      */     throws IOException
/*      */   {
/*  931 */     this._b = 196;
/*  932 */     if ((systemId != null) && (systemId.length() > 0)) {
/*  933 */       this._b |= 2;
/*      */     }
/*  935 */     if ((publicId != null) && (publicId.length() > 0)) {
/*  936 */       this._b |= 1;
/*      */     }
/*  938 */     write(this._b);
/*      */ 
/*  940 */     if ((systemId != null) && (systemId.length() > 0)) {
/*  941 */       encodeIdentifyingNonEmptyStringOnFirstBit(systemId, this._v.otherURI);
/*      */     }
/*  943 */     if ((publicId != null) && (publicId.length() > 0))
/*  944 */       encodeIdentifyingNonEmptyStringOnFirstBit(publicId, this._v.otherURI);
/*      */   }
/*      */ 
/*      */   protected final void encodeComment(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  957 */     write(226);
/*      */ 
/*  959 */     boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
/*  960 */     encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, true);
/*      */   }
/*      */ 
/*      */   protected final void encodeCommentNoClone(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  976 */     write(226);
/*      */ 
/*  978 */     boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
/*  979 */     encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, false);
/*      */   }
/*      */ 
/*      */   protected final void encodeElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName)
/*      */     throws IOException
/*      */   {
/*  997 */     LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
/*  998 */     if (entry._valueIndex > 0) {
/*  999 */       QualifiedName[] names = entry._value;
/* 1000 */       for (int i = 0; i < entry._valueIndex; i++) {
/* 1001 */         if (((prefix == names[i].prefix) || (prefix.equals(names[i].prefix))) && ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))))
/*      */         {
/* 1003 */           encodeNonZeroIntegerOnThirdBit(names[i].index);
/* 1004 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1009 */     encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, prefix, localName, entry);
/*      */   }
/*      */ 
/*      */   protected final void encodeLiteralElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry)
/*      */     throws IOException
/*      */   {
/* 1024 */     QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.elementName.getNextIndex());
/* 1025 */     entry.addQualifiedName(name);
/*      */ 
/* 1027 */     int namespaceURIIndex = -1;
/* 1028 */     int prefixIndex = -1;
/* 1029 */     if (namespaceURI.length() > 0) {
/* 1030 */       namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
/* 1031 */       if (namespaceURIIndex == -1) {
/* 1032 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[] { namespaceURI }));
/*      */       }
/*      */ 
/* 1035 */       if (prefix.length() > 0) {
/* 1036 */         prefixIndex = this._v.prefix.get(prefix);
/* 1037 */         if (prefixIndex == -1) {
/* 1038 */           throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[] { prefix }));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1043 */     int localNameIndex = this._v.localName.obtainIndex(localName);
/*      */ 
/* 1045 */     this._b |= 60;
/* 1046 */     if (namespaceURIIndex >= 0) {
/* 1047 */       this._b |= 1;
/* 1048 */       if (prefixIndex >= 0) {
/* 1049 */         this._b |= 2;
/*      */       }
/*      */     }
/* 1052 */     write(this._b);
/*      */ 
/* 1054 */     if (namespaceURIIndex >= 0) {
/* 1055 */       if (prefixIndex >= 0) {
/* 1056 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
/*      */       }
/* 1058 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
/*      */     }
/*      */ 
/* 1061 */     if (localNameIndex >= 0)
/* 1062 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
/*      */     else
/* 1064 */       encodeNonEmptyOctetStringOnSecondBit(localName);
/*      */   }
/*      */ 
/*      */   protected final void encodeAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName)
/*      */     throws IOException
/*      */   {
/* 1083 */     LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
/* 1084 */     if (entry._valueIndex > 0) {
/* 1085 */       QualifiedName[] names = entry._value;
/* 1086 */       for (int i = 0; i < entry._valueIndex; i++) {
/* 1087 */         if (((prefix == names[i].prefix) || (prefix.equals(names[i].prefix))) && ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))))
/*      */         {
/* 1089 */           encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
/* 1090 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1095 */     encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, prefix, localName, entry);
/*      */   }
/*      */ 
/*      */   protected final boolean encodeLiteralAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry)
/*      */     throws IOException
/*      */   {
/* 1110 */     int namespaceURIIndex = -1;
/* 1111 */     int prefixIndex = -1;
/* 1112 */     if (namespaceURI.length() > 0) {
/* 1113 */       namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
/* 1114 */       if (namespaceURIIndex == -1) {
/* 1115 */         if ((namespaceURI == "http://www.w3.org/2000/xmlns/") || (namespaceURI.equals("http://www.w3.org/2000/xmlns/")))
/*      */         {
/* 1117 */           return false;
/*      */         }
/* 1119 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[] { namespaceURI }));
/*      */       }
/*      */ 
/* 1123 */       if (prefix.length() > 0) {
/* 1124 */         prefixIndex = this._v.prefix.get(prefix);
/* 1125 */         if (prefixIndex == -1) {
/* 1126 */           throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[] { prefix }));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1131 */     int localNameIndex = this._v.localName.obtainIndex(localName);
/*      */ 
/* 1133 */     QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.attributeName.getNextIndex());
/* 1134 */     entry.addQualifiedName(name);
/*      */ 
/* 1136 */     this._b = 120;
/* 1137 */     if (namespaceURI.length() > 0) {
/* 1138 */       this._b |= 1;
/* 1139 */       if (prefix.length() > 0) {
/* 1140 */         this._b |= 2;
/*      */       }
/*      */     }
/*      */ 
/* 1144 */     write(this._b);
/*      */ 
/* 1146 */     if (namespaceURIIndex >= 0) {
/* 1147 */       if (prefixIndex >= 0) {
/* 1148 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
/*      */       }
/* 1150 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
/* 1151 */     } else if (namespaceURI != "")
/*      */     {
/* 1153 */       encodeNonEmptyOctetStringOnSecondBit("xml");
/* 1154 */       encodeNonEmptyOctetStringOnSecondBit("http://www.w3.org/XML/1998/namespace");
/*      */     }
/*      */ 
/* 1157 */     if (localNameIndex >= 0)
/* 1158 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
/*      */     else {
/* 1160 */       encodeNonEmptyOctetStringOnSecondBit(localName);
/*      */     }
/*      */ 
/* 1163 */     return true;
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnFirstBit(String s, StringIntMap map, boolean addToTable, boolean mustBeAddedToTable)
/*      */     throws IOException
/*      */   {
/* 1179 */     if ((s == null) || (s.length() == 0))
/*      */     {
/* 1181 */       write(255);
/*      */     }
/* 1183 */     else if ((addToTable) || (mustBeAddedToTable))
/*      */     {
/* 1185 */       boolean canAddAttributeToTable = (mustBeAddedToTable) || (canAddAttributeToTable(s.length()));
/*      */ 
/* 1189 */       int index = canAddAttributeToTable ? map.obtainIndex(s) : map.get(s);
/*      */ 
/* 1193 */       if (index != -1)
/*      */       {
/* 1195 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
/* 1196 */       } else if (canAddAttributeToTable)
/*      */       {
/* 1198 */         this._b = (0x40 | this._nonIdentifyingStringOnFirstBitCES);
/*      */ 
/* 1200 */         encodeNonEmptyCharacterStringOnFifthBit(s);
/*      */       }
/*      */       else {
/* 1203 */         this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1204 */         encodeNonEmptyCharacterStringOnFifthBit(s);
/*      */       }
/*      */     } else {
/* 1207 */       this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1208 */       encodeNonEmptyCharacterStringOnFifthBit(s);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnFirstBit(String s, CharArrayIntMap map, boolean addToTable)
/*      */     throws IOException
/*      */   {
/* 1223 */     if ((s == null) || (s.length() == 0))
/*      */     {
/* 1225 */       write(255);
/*      */     }
/* 1227 */     else if (addToTable) {
/* 1228 */       char[] ch = s.toCharArray();
/* 1229 */       int length = s.length();
/*      */ 
/* 1232 */       boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
/*      */ 
/* 1236 */       int index = canAddCharacterContentToTable ? map.obtainIndex(ch, 0, length, false) : map.get(ch, 0, length);
/*      */ 
/* 1240 */       if (index != -1)
/*      */       {
/* 1242 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
/* 1243 */       } else if (canAddCharacterContentToTable)
/*      */       {
/* 1245 */         this._b = (0x40 | this._nonIdentifyingStringOnFirstBitCES);
/*      */ 
/* 1247 */         encodeNonEmptyCharacterStringOnFifthBit(ch, 0, length);
/*      */       }
/*      */       else {
/* 1250 */         this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1251 */         encodeNonEmptyCharacterStringOnFifthBit(s);
/*      */       }
/*      */     } else {
/* 1254 */       this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1255 */       encodeNonEmptyCharacterStringOnFifthBit(s);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnFirstBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone)
/*      */     throws IOException
/*      */   {
/* 1275 */     if (length == 0)
/*      */     {
/* 1277 */       write(255);
/*      */     }
/* 1279 */     else if (addToTable)
/*      */     {
/* 1281 */       boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
/*      */ 
/* 1285 */       int index = canAddCharacterContentToTable ? map.obtainIndex(ch, offset, length, clone) : map.get(ch, offset, length);
/*      */ 
/* 1289 */       if (index != -1)
/*      */       {
/* 1291 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
/* 1292 */       } else if (canAddCharacterContentToTable)
/*      */       {
/* 1294 */         this._b = (0x40 | this._nonIdentifyingStringOnFirstBitCES);
/*      */ 
/* 1296 */         encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
/*      */       }
/*      */       else {
/* 1299 */         this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1300 */         encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
/*      */       }
/*      */     } else {
/* 1303 */       this._b = this._nonIdentifyingStringOnFirstBitCES;
/* 1304 */       encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNumericNonIdentifyingStringOnFirstBit(String s, boolean addToTable, boolean mustBeAddedToTable)
/*      */     throws IOException, FastInfosetException
/*      */   {
/* 1312 */     encodeNonIdentifyingStringOnFirstBit(0, NUMERIC_CHARACTERS_TABLE, s, addToTable, mustBeAddedToTable);
/*      */   }
/*      */ 
/*      */   protected final void encodeDateTimeNonIdentifyingStringOnFirstBit(String s, boolean addToTable, boolean mustBeAddedToTable)
/*      */     throws IOException, FastInfosetException
/*      */   {
/* 1321 */     encodeNonIdentifyingStringOnFirstBit(1, DATE_TIME_CHARACTERS_TABLE, s, addToTable, mustBeAddedToTable);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnFirstBit(int id, int[] table, String s, boolean addToTable, boolean mustBeAddedToTable)
/*      */     throws IOException, FastInfosetException
/*      */   {
/* 1330 */     if ((s == null) || (s.length() == 0))
/*      */     {
/* 1332 */       write(255);
/* 1333 */       return;
/*      */     }
/*      */ 
/* 1336 */     if ((addToTable) || (mustBeAddedToTable))
/*      */     {
/* 1338 */       boolean canAddAttributeToTable = (mustBeAddedToTable) || (canAddAttributeToTable(s.length()));
/*      */ 
/* 1342 */       int index = canAddAttributeToTable ? this._v.attributeValue.obtainIndex(s) : this._v.attributeValue.get(s);
/*      */ 
/* 1346 */       if (index != -1)
/*      */       {
/* 1348 */         encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
/* 1349 */         return;
/* 1350 */       }if (canAddAttributeToTable)
/*      */       {
/* 1352 */         this._b = 96;
/*      */       }
/*      */       else
/*      */       {
/* 1356 */         this._b = 32;
/*      */       }
/*      */     } else {
/* 1359 */       this._b = 32;
/*      */     }
/*      */ 
/* 1363 */     write(this._b | (id & 0xF0) >> 4);
/*      */ 
/* 1365 */     this._b = ((id & 0xF) << 4);
/*      */ 
/* 1367 */     int length = s.length();
/* 1368 */     int octetPairLength = length / 2;
/* 1369 */     int octetSingleLength = length % 2;
/* 1370 */     encodeNonZeroOctetStringLengthOnFifthBit(octetPairLength + octetSingleLength);
/* 1371 */     encodeNonEmptyFourBitCharacterString(table, s.toCharArray(), 0, octetPairLength, octetSingleLength);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnFirstBit(String URI, int id, Object data)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1388 */     if (URI != null) {
/* 1389 */       id = this._v.encodingAlgorithm.get(URI);
/* 1390 */       if (id == -1) {
/* 1391 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[] { URI }));
/*      */       }
/* 1393 */       id += 32;
/*      */ 
/* 1395 */       EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 1396 */       if (ea != null) {
/* 1397 */         encodeAIIObjectAlgorithmData(id, data, ea);
/*      */       }
/* 1399 */       else if ((data instanceof byte[])) {
/* 1400 */         byte[] d = (byte[])data;
/* 1401 */         encodeAIIOctetAlgorithmData(id, d, 0, d.length);
/*      */       } else {
/* 1403 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
/*      */       }
/*      */     }
/* 1406 */     else if (id <= 9) {
/* 1407 */       int length = 0;
/* 1408 */       switch (id) {
/*      */       case 0:
/*      */       case 1:
/* 1411 */         length = ((byte[])data).length;
/* 1412 */         break;
/*      */       case 2:
/* 1414 */         length = ((short[])data).length;
/* 1415 */         break;
/*      */       case 3:
/* 1417 */         length = ((int[])data).length;
/* 1418 */         break;
/*      */       case 4:
/*      */       case 8:
/* 1421 */         length = ((long[])data).length;
/* 1422 */         break;
/*      */       case 5:
/* 1424 */         length = ((boolean[])data).length;
/* 1425 */         break;
/*      */       case 6:
/* 1427 */         length = ((float[])data).length;
/* 1428 */         break;
/*      */       case 7:
/* 1430 */         length = ((double[])data).length;
/* 1431 */         break;
/*      */       case 9:
/* 1433 */         throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
/*      */       default:
/* 1435 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[] { Integer.valueOf(id) }));
/*      */       }
/* 1437 */       encodeAIIBuiltInAlgorithmData(id, data, 0, length);
/* 1438 */     } else if (id >= 32) {
/* 1439 */       if ((data instanceof byte[])) {
/* 1440 */         byte[] d = (byte[])data;
/* 1441 */         encodeAIIOctetAlgorithmData(id, d, 0, d.length);
/*      */       } else {
/* 1443 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
/*      */       }
/*      */     } else {
/* 1446 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeAIIOctetAlgorithmData(int id, byte[] d, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1462 */     write(0x30 | (id & 0xF0) >> 4);
/*      */ 
/* 1466 */     this._b = ((id & 0xF) << 4);
/*      */ 
/* 1469 */     encodeNonZeroOctetStringLengthOnFifthBit(length);
/*      */ 
/* 1471 */     write(d, offset, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeAIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1486 */     write(0x30 | (id & 0xF0) >> 4);
/*      */ 
/* 1490 */     this._b = ((id & 0xF) << 4);
/*      */ 
/* 1492 */     this._encodingBufferOutputStream.reset();
/* 1493 */     ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
/* 1494 */     encodeNonZeroOctetStringLengthOnFifthBit(this._encodingBufferIndex);
/* 1495 */     write(this._encodingBuffer, this._encodingBufferIndex);
/*      */   }
/*      */ 
/*      */   protected final void encodeAIIBuiltInAlgorithmData(int id, Object data, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1512 */     write(0x30 | (id & 0xF0) >> 4);
/*      */ 
/* 1516 */     this._b = ((id & 0xF) << 4);
/*      */ 
/* 1518 */     int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
/*      */ 
/* 1521 */     encodeNonZeroOctetStringLengthOnFifthBit(octetLength);
/*      */ 
/* 1523 */     ensureSize(octetLength);
/* 1524 */     BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
/*      */ 
/* 1526 */     this._octetBufferIndex += octetLength;
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnThirdBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone)
/*      */     throws IOException
/*      */   {
/* 1546 */     if (addToTable)
/*      */     {
/* 1548 */       boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
/*      */ 
/* 1552 */       int index = canAddCharacterContentToTable ? map.obtainIndex(ch, offset, length, clone) : map.get(ch, offset, length);
/*      */ 
/* 1556 */       if (index != -1)
/*      */       {
/* 1558 */         this._b = 160;
/* 1559 */         encodeNonZeroIntegerOnFourthBit(index);
/* 1560 */       } else if (canAddCharacterContentToTable)
/*      */       {
/* 1562 */         this._b = (0x10 | this._nonIdentifyingStringOnThirdBitCES);
/*      */ 
/* 1564 */         encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
/*      */       }
/*      */       else {
/* 1567 */         this._b = this._nonIdentifyingStringOnThirdBitCES;
/* 1568 */         encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
/*      */       }
/*      */     }
/*      */     else {
/* 1572 */       this._b = this._nonIdentifyingStringOnThirdBitCES;
/* 1573 */       encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, Object data)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1591 */     if (URI != null) {
/* 1592 */       id = this._v.encodingAlgorithm.get(URI);
/* 1593 */       if (id == -1) {
/* 1594 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[] { URI }));
/*      */       }
/* 1596 */       id += 32;
/*      */ 
/* 1598 */       EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 1599 */       if (ea != null) {
/* 1600 */         encodeCIIObjectAlgorithmData(id, data, ea);
/*      */       }
/* 1602 */       else if ((data instanceof byte[])) {
/* 1603 */         byte[] d = (byte[])data;
/* 1604 */         encodeCIIOctetAlgorithmData(id, d, 0, d.length);
/*      */       } else {
/* 1606 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
/*      */       }
/*      */     }
/* 1609 */     else if (id <= 9) {
/* 1610 */       int length = 0;
/* 1611 */       switch (id) {
/*      */       case 0:
/*      */       case 1:
/* 1614 */         length = ((byte[])data).length;
/* 1615 */         break;
/*      */       case 2:
/* 1617 */         length = ((short[])data).length;
/* 1618 */         break;
/*      */       case 3:
/* 1620 */         length = ((int[])data).length;
/* 1621 */         break;
/*      */       case 4:
/*      */       case 8:
/* 1624 */         length = ((long[])data).length;
/* 1625 */         break;
/*      */       case 5:
/* 1627 */         length = ((boolean[])data).length;
/* 1628 */         break;
/*      */       case 6:
/* 1630 */         length = ((float[])data).length;
/* 1631 */         break;
/*      */       case 7:
/* 1633 */         length = ((double[])data).length;
/* 1634 */         break;
/*      */       case 9:
/* 1636 */         throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
/*      */       default:
/* 1638 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[] { Integer.valueOf(id) }));
/*      */       }
/* 1640 */       encodeCIIBuiltInAlgorithmData(id, data, 0, length);
/* 1641 */     } else if (id >= 32) {
/* 1642 */       if ((data instanceof byte[])) {
/* 1643 */         byte[] d = (byte[])data;
/* 1644 */         encodeCIIOctetAlgorithmData(id, d, 0, d.length);
/*      */       } else {
/* 1646 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
/*      */       }
/*      */     } else {
/* 1649 */       throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, byte[] d, int offset, int length)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1668 */     if (URI != null) {
/* 1669 */       id = this._v.encodingAlgorithm.get(URI);
/* 1670 */       if (id == -1) {
/* 1671 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[] { URI }));
/*      */       }
/* 1673 */       id += 32;
/*      */     }
/*      */ 
/* 1676 */     encodeCIIOctetAlgorithmData(id, d, offset, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeCIIOctetAlgorithmData(int id, byte[] d, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1691 */     write(0x8C | (id & 0xC0) >> 6);
/*      */ 
/* 1695 */     this._b = ((id & 0x3F) << 2);
/*      */ 
/* 1698 */     encodeNonZeroOctetStringLengthOnSenventhBit(length);
/*      */ 
/* 1700 */     write(d, offset, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeCIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1715 */     write(0x8C | (id & 0xC0) >> 6);
/*      */ 
/* 1719 */     this._b = ((id & 0x3F) << 2);
/*      */ 
/* 1721 */     this._encodingBufferOutputStream.reset();
/* 1722 */     ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
/* 1723 */     encodeNonZeroOctetStringLengthOnSenventhBit(this._encodingBufferIndex);
/* 1724 */     write(this._encodingBuffer, this._encodingBufferIndex);
/*      */   }
/*      */ 
/*      */   protected final void encodeCIIBuiltInAlgorithmData(int id, Object data, int offset, int length)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1741 */     write(0x8C | (id & 0xC0) >> 6);
/*      */ 
/* 1745 */     this._b = ((id & 0x3F) << 2);
/*      */ 
/* 1747 */     int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
/*      */ 
/* 1750 */     encodeNonZeroOctetStringLengthOnSenventhBit(octetLength);
/*      */ 
/* 1752 */     ensureSize(octetLength);
/* 1753 */     BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
/*      */ 
/* 1755 */     this._octetBufferIndex += octetLength;
/*      */   }
/*      */ 
/*      */   protected final void encodeCIIBuiltInAlgorithmDataAsCDATA(char[] ch, int offset, int length)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1769 */     write(140);
/*      */ 
/* 1772 */     this._b = 36;
/*      */ 
/* 1775 */     length = encodeUTF8String(ch, offset, length);
/* 1776 */     encodeNonZeroOctetStringLengthOnSenventhBit(length);
/* 1777 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeIdentifyingNonEmptyStringOnFirstBit(String s, StringIntMap map)
/*      */     throws IOException
/*      */   {
/* 1789 */     int index = map.obtainIndex(s);
/* 1790 */     if (index == -1)
/*      */     {
/* 1792 */       encodeNonEmptyOctetStringOnSecondBit(s);
/*      */     }
/*      */     else
/* 1795 */       encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyOctetStringOnSecondBit(String s)
/*      */     throws IOException
/*      */   {
/* 1807 */     int length = encodeUTF8String(s);
/* 1808 */     encodeNonZeroOctetStringLengthOnSecondBit(length);
/* 1809 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroOctetStringLengthOnSecondBit(int length)
/*      */     throws IOException
/*      */   {
/* 1819 */     if (length < 65)
/*      */     {
/* 1821 */       write(length - 1);
/* 1822 */     } else if (length < 321)
/*      */     {
/* 1824 */       write(64);
/* 1825 */       write(length - 65);
/*      */     }
/*      */     else {
/* 1828 */       write(96);
/* 1829 */       length -= 321;
/* 1830 */       write(length >>> 24);
/* 1831 */       write(length >> 16 & 0xFF);
/* 1832 */       write(length >> 8 & 0xFF);
/* 1833 */       write(length & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyCharacterStringOnFifthBit(String s)
/*      */     throws IOException
/*      */   {
/* 1845 */     int length = this._encodingStringsAsUtf8 ? encodeUTF8String(s) : encodeUtf16String(s);
/* 1846 */     encodeNonZeroOctetStringLengthOnFifthBit(length);
/* 1847 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyCharacterStringOnFifthBit(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1860 */     length = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
/* 1861 */     encodeNonZeroOctetStringLengthOnFifthBit(length);
/* 1862 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroOctetStringLengthOnFifthBit(int length)
/*      */     throws IOException
/*      */   {
/* 1873 */     if (length < 9)
/*      */     {
/* 1875 */       write(this._b | length - 1);
/* 1876 */     } else if (length < 265)
/*      */     {
/* 1878 */       write(this._b | 0x8);
/* 1879 */       write(length - 9);
/*      */     }
/*      */     else {
/* 1882 */       write(this._b | 0xC);
/* 1883 */       length -= 265;
/* 1884 */       write(length >>> 24);
/* 1885 */       write(length >> 16 & 0xFF);
/* 1886 */       write(length >> 8 & 0xFF);
/* 1887 */       write(length & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyCharacterStringOnSeventhBit(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1901 */     length = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
/* 1902 */     encodeNonZeroOctetStringLengthOnSenventhBit(length);
/* 1903 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyFourBitCharacterStringOnSeventhBit(int[] table, char[] ch, int offset, int length)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1918 */     int octetPairLength = length / 2;
/* 1919 */     int octetSingleLength = length % 2;
/*      */ 
/* 1922 */     encodeNonZeroOctetStringLengthOnSenventhBit(octetPairLength + octetSingleLength);
/* 1923 */     encodeNonEmptyFourBitCharacterString(table, ch, offset, octetPairLength, octetSingleLength);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyFourBitCharacterString(int[] table, char[] ch, int offset, int octetPairLength, int octetSingleLength) throws FastInfosetException, IOException
/*      */   {
/* 1928 */     ensureSize(octetPairLength + octetSingleLength);
/*      */ 
/* 1930 */     int v = 0;
/* 1931 */     for (int i = 0; i < octetPairLength; i++) {
/* 1932 */       v = table[ch[(offset++)]] << 4 | table[ch[(offset++)]];
/* 1933 */       if (v < 0) {
/* 1934 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
/*      */       }
/* 1936 */       this._octetBuffer[(this._octetBufferIndex++)] = ((byte)v);
/*      */     }
/*      */ 
/* 1939 */     if (octetSingleLength == 1) {
/* 1940 */       v = table[ch[offset]] << 4 | 0xF;
/* 1941 */       if (v < 0) {
/* 1942 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
/*      */       }
/* 1944 */       this._octetBuffer[(this._octetBufferIndex++)] = ((byte)v);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyNBitCharacterStringOnSeventhBit(String alphabet, char[] ch, int offset, int length)
/*      */     throws FastInfosetException, IOException
/*      */   {
/* 1960 */     int bitsPerCharacter = 1;
/* 1961 */     while (1 << bitsPerCharacter <= alphabet.length()) {
/* 1962 */       bitsPerCharacter++;
/*      */     }
/*      */ 
/* 1965 */     int bits = length * bitsPerCharacter;
/* 1966 */     int octets = bits / 8;
/* 1967 */     int bitsOfLastOctet = bits % 8;
/* 1968 */     int totalOctets = octets + (bitsOfLastOctet > 0 ? 1 : 0);
/*      */ 
/* 1971 */     encodeNonZeroOctetStringLengthOnSenventhBit(totalOctets);
/*      */ 
/* 1973 */     resetBits();
/* 1974 */     ensureSize(totalOctets);
/* 1975 */     int v = 0;
/* 1976 */     for (int i = 0; i < length; i++) {
/* 1977 */       char c = ch[(offset + i)];
/*      */ 
/* 1979 */       for (v = 0; (v < alphabet.length()) && 
/* 1980 */         (c != alphabet.charAt(v)); v++);
/* 1984 */       if (v == alphabet.length()) {
/* 1985 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
/*      */       }
/* 1987 */       writeBits(bitsPerCharacter, v);
/*      */     }
/*      */ 
/* 1990 */     if (bitsOfLastOctet > 0) {
/* 1991 */       this._b |= (1 << 8 - bitsOfLastOctet) - 1;
/* 1992 */       write(this._b);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void resetBits()
/*      */   {
/* 1999 */     this._bitsLeftInOctet = 8;
/* 2000 */     this._b = 0;
/*      */   }
/*      */ 
/*      */   private final void writeBits(int bits, int v) throws IOException {
/* 2004 */     while (bits > 0) {
/* 2005 */       int bit = (v & 1 << --bits) > 0 ? 1 : 0;
/* 2006 */       this._b |= bit << --this._bitsLeftInOctet;
/* 2007 */       if (this._bitsLeftInOctet == 0) {
/* 2008 */         write(this._b);
/* 2009 */         this._bitsLeftInOctet = 8;
/* 2010 */         this._b = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroOctetStringLengthOnSenventhBit(int length)
/*      */     throws IOException
/*      */   {
/* 2023 */     if (length < 3)
/*      */     {
/* 2025 */       write(this._b | length - 1);
/* 2026 */     } else if (length < 259)
/*      */     {
/* 2028 */       write(this._b | 0x2);
/* 2029 */       write(length - 3);
/*      */     }
/*      */     else {
/* 2032 */       write(this._b | 0x3);
/* 2033 */       length -= 259;
/* 2034 */       write(length >>> 24);
/* 2035 */       write(length >> 16 & 0xFF);
/* 2036 */       write(length >> 8 & 0xFF);
/* 2037 */       write(length & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroIntegerOnSecondBitFirstBitOne(int i)
/*      */     throws IOException
/*      */   {
/* 2055 */     if (i < 64)
/*      */     {
/* 2057 */       write(0x80 | i);
/* 2058 */     } else if (i < 8256)
/*      */     {
/* 2060 */       i -= 64;
/* 2061 */       this._b = (0xC0 | i >> 8);
/*      */ 
/* 2063 */       write(this._b);
/* 2064 */       write(i & 0xFF);
/* 2065 */     } else if (i < 1048576)
/*      */     {
/* 2067 */       i -= 8256;
/* 2068 */       this._b = (0xE0 | i >> 16);
/*      */ 
/* 2070 */       write(this._b);
/* 2071 */       write(i >> 8 & 0xFF);
/* 2072 */       write(i & 0xFF);
/*      */     } else {
/* 2074 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[] { Integer.valueOf(1048576) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroIntegerOnSecondBitFirstBitZero(int i)
/*      */     throws IOException
/*      */   {
/* 2094 */     if (i < 64)
/*      */     {
/* 2096 */       write(i);
/* 2097 */     } else if (i < 8256)
/*      */     {
/* 2099 */       i -= 64;
/* 2100 */       this._b = (0x40 | i >> 8);
/* 2101 */       write(this._b);
/* 2102 */       write(i & 0xFF);
/*      */     }
/*      */     else {
/* 2105 */       i -= 8256;
/* 2106 */       this._b = (0x60 | i >> 16);
/* 2107 */       write(this._b);
/* 2108 */       write(i >> 8 & 0xFF);
/* 2109 */       write(i & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroIntegerOnThirdBit(int i)
/*      */     throws IOException
/*      */   {
/* 2122 */     if (i < 32)
/*      */     {
/* 2124 */       write(this._b | i);
/* 2125 */     } else if (i < 2080)
/*      */     {
/* 2127 */       i -= 32;
/* 2128 */       this._b |= 0x20 | i >> 8;
/* 2129 */       write(this._b);
/* 2130 */       write(i & 0xFF);
/* 2131 */     } else if (i < 526368)
/*      */     {
/* 2133 */       i -= 2080;
/* 2134 */       this._b |= 0x28 | i >> 16;
/* 2135 */       write(this._b);
/* 2136 */       write(i >> 8 & 0xFF);
/* 2137 */       write(i & 0xFF);
/*      */     }
/*      */     else {
/* 2140 */       i -= 526368;
/* 2141 */       this._b |= 48;
/* 2142 */       write(this._b);
/* 2143 */       write(i >> 16);
/* 2144 */       write(i >> 8 & 0xFF);
/* 2145 */       write(i & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroIntegerOnFourthBit(int i)
/*      */     throws IOException
/*      */   {
/* 2158 */     if (i < 16)
/*      */     {
/* 2160 */       write(this._b | i);
/* 2161 */     } else if (i < 1040)
/*      */     {
/* 2163 */       i -= 16;
/* 2164 */       this._b |= 0x10 | i >> 8;
/* 2165 */       write(this._b);
/* 2166 */       write(i & 0xFF);
/* 2167 */     } else if (i < 263184)
/*      */     {
/* 2169 */       i -= 1040;
/* 2170 */       this._b |= 0x14 | i >> 16;
/* 2171 */       write(this._b);
/* 2172 */       write(i >> 8 & 0xFF);
/* 2173 */       write(i & 0xFF);
/*      */     }
/*      */     else {
/* 2176 */       i -= 263184;
/* 2177 */       this._b |= 24;
/* 2178 */       write(this._b);
/* 2179 */       write(i >> 16);
/* 2180 */       write(i >> 8 & 0xFF);
/* 2181 */       write(i & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyUTF8StringAsOctetString(int b, String s, int[] constants)
/*      */     throws IOException
/*      */   {
/* 2194 */     char[] ch = s.toCharArray();
/* 2195 */     encodeNonEmptyUTF8StringAsOctetString(b, ch, 0, ch.length, constants);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonEmptyUTF8StringAsOctetString(int b, char[] ch, int offset, int length, int[] constants)
/*      */     throws IOException
/*      */   {
/* 2210 */     length = encodeUTF8String(ch, offset, length);
/* 2211 */     encodeNonZeroOctetStringLength(b, length, constants);
/* 2212 */     write(this._encodingBuffer, length);
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroOctetStringLength(int b, int length, int[] constants)
/*      */     throws IOException
/*      */   {
/* 2225 */     if (length < constants[0]) {
/* 2226 */       write(b | length - 1);
/* 2227 */     } else if (length < constants[1]) {
/* 2228 */       write(b | constants[2]);
/* 2229 */       write(length - constants[0]);
/*      */     } else {
/* 2231 */       write(b | constants[3]);
/* 2232 */       length -= constants[1];
/* 2233 */       write(length >>> 24);
/* 2234 */       write(length >> 16 & 0xFF);
/* 2235 */       write(length >> 8 & 0xFF);
/* 2236 */       write(length & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void encodeNonZeroInteger(int b, int i, int[] constants)
/*      */     throws IOException
/*      */   {
/* 2249 */     if (i < constants[0]) {
/* 2250 */       write(b | i);
/* 2251 */     } else if (i < constants[1]) {
/* 2252 */       i -= constants[0];
/* 2253 */       write(b | constants[3] | i >> 8);
/* 2254 */       write(i & 0xFF);
/* 2255 */     } else if (i < constants[2]) {
/* 2256 */       i -= constants[1];
/* 2257 */       write(b | constants[4] | i >> 16);
/* 2258 */       write(i >> 8 & 0xFF);
/* 2259 */       write(i & 0xFF);
/* 2260 */     } else if (i < 1048576) {
/* 2261 */       i -= constants[2];
/* 2262 */       write(b | constants[5]);
/* 2263 */       write(i >> 16);
/* 2264 */       write(i >> 8 & 0xFF);
/* 2265 */       write(i & 0xFF);
/*      */     } else {
/* 2267 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[] { Integer.valueOf(1048576) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void mark()
/*      */   {
/* 2275 */     this._markIndex = this._octetBufferIndex;
/*      */   }
/*      */ 
/*      */   protected final void resetMark()
/*      */   {
/* 2282 */     this._markIndex = -1;
/*      */   }
/*      */ 
/*      */   protected final boolean hasMark()
/*      */   {
/* 2290 */     return this._markIndex != -1;
/*      */   }
/*      */ 
/*      */   protected final void write(int i)
/*      */     throws IOException
/*      */   {
/* 2297 */     if (this._octetBufferIndex < this._octetBuffer.length) {
/* 2298 */       this._octetBuffer[(this._octetBufferIndex++)] = ((byte)i);
/*      */     }
/* 2300 */     else if (this._markIndex == -1) {
/* 2301 */       this._s.write(this._octetBuffer);
/* 2302 */       this._octetBufferIndex = 1;
/* 2303 */       this._octetBuffer[0] = ((byte)i);
/*      */     } else {
/* 2305 */       resize(this._octetBuffer.length * 3 / 2);
/* 2306 */       this._octetBuffer[(this._octetBufferIndex++)] = ((byte)i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void write(byte[] b, int length)
/*      */     throws IOException
/*      */   {
/* 2318 */     write(b, 0, length);
/*      */   }
/*      */ 
/*      */   protected final void write(byte[] b, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 2329 */     if (this._octetBufferIndex + length < this._octetBuffer.length) {
/* 2330 */       System.arraycopy(b, offset, this._octetBuffer, this._octetBufferIndex, length);
/* 2331 */       this._octetBufferIndex += length;
/*      */     }
/* 2333 */     else if (this._markIndex == -1) {
/* 2334 */       this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
/* 2335 */       this._s.write(b, offset, length);
/* 2336 */       this._octetBufferIndex = 0;
/*      */     } else {
/* 2338 */       resize((this._octetBuffer.length + length) * 3 / 2 + 1);
/* 2339 */       System.arraycopy(b, offset, this._octetBuffer, this._octetBufferIndex, length);
/* 2340 */       this._octetBufferIndex += length;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureSize(int length)
/*      */   {
/* 2346 */     if (this._octetBufferIndex + length > this._octetBuffer.length)
/* 2347 */       resize((this._octetBufferIndex + length) * 3 / 2 + 1);
/*      */   }
/*      */ 
/*      */   private void resize(int length)
/*      */   {
/* 2352 */     byte[] b = new byte[length];
/* 2353 */     System.arraycopy(this._octetBuffer, 0, b, 0, this._octetBufferIndex);
/* 2354 */     this._octetBuffer = b;
/*      */   }
/*      */ 
/*      */   private void _flush() throws IOException {
/* 2358 */     if (this._octetBufferIndex > 0) {
/* 2359 */       this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
/* 2360 */       this._octetBufferIndex = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final int encodeUTF8String(String s)
/*      */     throws IOException
/*      */   {
/* 2417 */     int length = s.length();
/* 2418 */     if (length < this._charBuffer.length) {
/* 2419 */       s.getChars(0, length, this._charBuffer, 0);
/* 2420 */       return encodeUTF8String(this._charBuffer, 0, length);
/*      */     }
/* 2422 */     char[] ch = s.toCharArray();
/* 2423 */     return encodeUTF8String(ch, 0, length);
/*      */   }
/*      */ 
/*      */   private void ensureEncodingBufferSizeForUtf8String(int length)
/*      */   {
/* 2428 */     int newLength = 4 * length;
/* 2429 */     if (this._encodingBuffer.length < newLength)
/* 2430 */       this._encodingBuffer = new byte[newLength];
/*      */   }
/*      */ 
/*      */   protected final int encodeUTF8String(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 2442 */     int bpos = 0;
/*      */ 
/* 2445 */     ensureEncodingBufferSizeForUtf8String(length);
/*      */ 
/* 2447 */     int end = offset + length;
/*      */ 
/* 2449 */     while (end != offset) {
/* 2450 */       int c = ch[(offset++)];
/* 2451 */       if (c < 128)
/*      */       {
/* 2453 */         this._encodingBuffer[(bpos++)] = ((byte)c);
/* 2454 */       } else if (c < 2048)
/*      */       {
/* 2456 */         this._encodingBuffer[(bpos++)] = ((byte)(0xC0 | c >> 6));
/*      */ 
/* 2458 */         this._encodingBuffer[(bpos++)] = ((byte)(0x80 | c & 0x3F));
/*      */       }
/* 2460 */       else if (c <= 65535) {
/* 2461 */         if ((!XMLChar.isHighSurrogate(c)) && (!XMLChar.isLowSurrogate(c)))
/*      */         {
/* 2463 */           this._encodingBuffer[(bpos++)] = ((byte)(0xE0 | c >> 12));
/*      */ 
/* 2465 */           this._encodingBuffer[(bpos++)] = ((byte)(0x80 | c >> 6 & 0x3F));
/*      */ 
/* 2467 */           this._encodingBuffer[(bpos++)] = ((byte)(0x80 | c & 0x3F));
/*      */         }
/*      */         else
/*      */         {
/* 2471 */           encodeCharacterAsUtf8FourByte(c, ch, offset, end, bpos);
/* 2472 */           bpos += 4;
/* 2473 */           offset++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2478 */     return bpos;
/*      */   }
/*      */ 
/*      */   private void encodeCharacterAsUtf8FourByte(int c, char[] ch, int chpos, int chend, int bpos) throws IOException {
/* 2482 */     if (chpos == chend) {
/* 2483 */       throw new IOException("");
/*      */     }
/*      */ 
/* 2486 */     char d = ch[chpos];
/* 2487 */     if (!XMLChar.isLowSurrogate(d)) {
/* 2488 */       throw new IOException("");
/*      */     }
/*      */ 
/* 2491 */     int uc = ((c & 0x3FF) << 10 | d & 0x3FF) + 65536;
/* 2492 */     if ((uc < 0) || (uc >= 2097152)) {
/* 2493 */       throw new IOException("");
/*      */     }
/*      */ 
/* 2496 */     this._encodingBuffer[(bpos++)] = ((byte)(0xF0 | uc >> 18));
/* 2497 */     this._encodingBuffer[(bpos++)] = ((byte)(0x80 | uc >> 12 & 0x3F));
/* 2498 */     this._encodingBuffer[(bpos++)] = ((byte)(0x80 | uc >> 6 & 0x3F));
/* 2499 */     this._encodingBuffer[(bpos++)] = ((byte)(0x80 | uc & 0x3F));
/*      */   }
/*      */ 
/*      */   protected final int encodeUtf16String(String s)
/*      */     throws IOException
/*      */   {
/* 2508 */     int length = s.length();
/* 2509 */     if (length < this._charBuffer.length) {
/* 2510 */       s.getChars(0, length, this._charBuffer, 0);
/* 2511 */       return encodeUtf16String(this._charBuffer, 0, length);
/*      */     }
/* 2513 */     char[] ch = s.toCharArray();
/* 2514 */     return encodeUtf16String(ch, 0, length);
/*      */   }
/*      */ 
/*      */   private void ensureEncodingBufferSizeForUtf16String(int length)
/*      */   {
/* 2519 */     int newLength = 2 * length;
/* 2520 */     if (this._encodingBuffer.length < newLength)
/* 2521 */       this._encodingBuffer = new byte[newLength];
/*      */   }
/*      */ 
/*      */   protected final int encodeUtf16String(char[] ch, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 2533 */     int byteLength = 0;
/*      */ 
/* 2536 */     ensureEncodingBufferSizeForUtf16String(length);
/*      */ 
/* 2538 */     int n = offset + length;
/* 2539 */     for (int i = offset; i < n; i++) {
/* 2540 */       int c = ch[i];
/* 2541 */       this._encodingBuffer[(byteLength++)] = ((byte)(c >> 8));
/* 2542 */       this._encodingBuffer[(byteLength++)] = ((byte)(c & 0xFF));
/*      */     }
/*      */ 
/* 2545 */     return byteLength;
/*      */   }
/*      */ 
/*      */   public static String getPrefixFromQualifiedName(String qName)
/*      */   {
/* 2555 */     int i = qName.indexOf(':');
/* 2556 */     String prefix = "";
/* 2557 */     if (i != -1) {
/* 2558 */       prefix = qName.substring(0, i);
/*      */     }
/* 2560 */     return prefix;
/*      */   }
/*      */ 
/*      */   public static boolean isWhiteSpace(char[] ch, int start, int length)
/*      */   {
/* 2572 */     if (!XMLChar.isSpace(ch[start])) return false;
/*      */ 
/* 2574 */     int end = start + length;
/*      */     do start++; while ((start < end) && (XMLChar.isSpace(ch[start])));
/*      */ 
/* 2577 */     return start == end;
/*      */   }
/*      */ 
/*      */   public static boolean isWhiteSpace(String s)
/*      */   {
/* 2587 */     if (!XMLChar.isSpace(s.charAt(0))) return false;
/*      */ 
/* 2589 */     int end = s.length();
/* 2590 */     int start = 1;
/* 2591 */     while ((start < end) && (XMLChar.isSpace(s.charAt(start++))));
/* 2592 */     return start == end;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  107 */     for (int i = 0; i < NUMERIC_CHARACTERS_TABLE.length; i++) {
/*  108 */       NUMERIC_CHARACTERS_TABLE[i] = -1;
/*      */     }
/*  110 */     for (int i = 0; i < DATE_TIME_CHARACTERS_TABLE.length; i++) {
/*  111 */       DATE_TIME_CHARACTERS_TABLE[i] = -1;
/*      */     }
/*      */ 
/*  114 */     for (int i = 0; i < "0123456789-+.E ".length(); i++) {
/*  115 */       NUMERIC_CHARACTERS_TABLE["0123456789-+.E ".charAt(i)] = i;
/*      */     }
/*  117 */     for (int i = 0; i < "0123456789-:TZ ".length(); i++)
/*  118 */       DATE_TIME_CHARACTERS_TABLE["0123456789-:TZ ".charAt(i)] = i;
/*      */   }
/*      */ 
/*      */   private class EncodingBufferOutputStream extends OutputStream
/*      */   {
/*      */     private EncodingBufferOutputStream()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void write(int b)
/*      */       throws IOException
/*      */     {
/* 2374 */       if (Encoder.this._encodingBufferIndex < Encoder.this._encodingBuffer.length) {
/* 2375 */         Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = ((byte)b);
/*      */       } else {
/* 2377 */         byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, Encoder.this._encodingBufferIndex)];
/* 2378 */         System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
/* 2379 */         Encoder.this._encodingBuffer = newbuf;
/*      */ 
/* 2381 */         Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = ((byte)b);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void write(byte[] b, int off, int len) throws IOException {
/* 2386 */       if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*      */       {
/* 2388 */         throw new IndexOutOfBoundsException();
/* 2389 */       }if (len == 0) {
/* 2390 */         return;
/*      */       }
/* 2392 */       int newoffset = Encoder.this._encodingBufferIndex + len;
/* 2393 */       if (newoffset > Encoder.this._encodingBuffer.length) {
/* 2394 */         byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, newoffset)];
/* 2395 */         System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
/* 2396 */         Encoder.this._encodingBuffer = newbuf;
/*      */       }
/* 2398 */       System.arraycopy(b, off, Encoder.this._encodingBuffer, Encoder.this._encodingBufferIndex, len);
/* 2399 */       Encoder.this._encodingBufferIndex = newoffset;
/*      */     }
/*      */ 
/*      */     public int getLength() {
/* 2403 */       return Encoder.this._encodingBufferIndex;
/*      */     }
/*      */ 
/*      */     public void reset() {
/* 2407 */       Encoder.this._encodingBufferIndex = 0;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.Encoder
 * JD-Core Version:    0.6.2
 */