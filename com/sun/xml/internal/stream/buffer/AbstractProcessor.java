/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ public abstract class AbstractProcessor extends AbstractCreatorProcessor
/*     */ {
/*     */   protected static final int STATE_ILLEGAL = 0;
/*     */   protected static final int STATE_DOCUMENT = 1;
/*     */   protected static final int STATE_DOCUMENT_FRAGMENT = 2;
/*     */   protected static final int STATE_ELEMENT_U_LN_QN = 3;
/*     */   protected static final int STATE_ELEMENT_P_U_LN = 4;
/*     */   protected static final int STATE_ELEMENT_U_LN = 5;
/*     */   protected static final int STATE_ELEMENT_LN = 6;
/*     */   protected static final int STATE_TEXT_AS_CHAR_ARRAY_SMALL = 7;
/*     */   protected static final int STATE_TEXT_AS_CHAR_ARRAY_MEDIUM = 8;
/*     */   protected static final int STATE_TEXT_AS_CHAR_ARRAY_COPY = 9;
/*     */   protected static final int STATE_TEXT_AS_STRING = 10;
/*     */   protected static final int STATE_TEXT_AS_OBJECT = 11;
/*     */   protected static final int STATE_COMMENT_AS_CHAR_ARRAY_SMALL = 12;
/*     */   protected static final int STATE_COMMENT_AS_CHAR_ARRAY_MEDIUM = 13;
/*     */   protected static final int STATE_COMMENT_AS_CHAR_ARRAY_COPY = 14;
/*     */   protected static final int STATE_COMMENT_AS_STRING = 15;
/*     */   protected static final int STATE_PROCESSING_INSTRUCTION = 16;
/*     */   protected static final int STATE_END = 17;
/*  52 */   private static final int[] _eiiStateTable = new int[256];
/*     */   protected static final int STATE_NAMESPACE_ATTRIBUTE = 1;
/*     */   protected static final int STATE_NAMESPACE_ATTRIBUTE_P = 2;
/*     */   protected static final int STATE_NAMESPACE_ATTRIBUTE_P_U = 3;
/*     */   protected static final int STATE_NAMESPACE_ATTRIBUTE_U = 4;
/*  58 */   private static final int[] _niiStateTable = new int[256];
/*     */   protected static final int STATE_ATTRIBUTE_U_LN_QN = 1;
/*     */   protected static final int STATE_ATTRIBUTE_P_U_LN = 2;
/*     */   protected static final int STATE_ATTRIBUTE_U_LN = 3;
/*     */   protected static final int STATE_ATTRIBUTE_LN = 4;
/*     */   protected static final int STATE_ATTRIBUTE_U_LN_QN_OBJECT = 5;
/*     */   protected static final int STATE_ATTRIBUTE_P_U_LN_OBJECT = 6;
/*     */   protected static final int STATE_ATTRIBUTE_U_LN_OBJECT = 7;
/*     */   protected static final int STATE_ATTRIBUTE_LN_OBJECT = 8;
/*  68 */   private static final int[] _aiiStateTable = new int[256];
/*     */   protected XMLStreamBuffer _buffer;
/*     */   protected boolean _fragmentMode;
/* 117 */   protected boolean _stringInterningFeature = false;
/*     */   protected int _treeCount;
/* 242 */   protected final StringBuilder _qNameBuffer = new StringBuilder();
/*     */ 
/*     */   /** @deprecated */
/*     */   protected final void setBuffer(XMLStreamBuffer buffer)
/*     */   {
/* 130 */     setBuffer(buffer, buffer.isFragment());
/*     */   }
/*     */   protected final void setBuffer(XMLStreamBuffer buffer, boolean fragmentMode) {
/* 133 */     this._buffer = buffer;
/* 134 */     this._fragmentMode = fragmentMode;
/*     */ 
/* 136 */     this._currentStructureFragment = this._buffer.getStructure();
/* 137 */     this._structure = ((byte[])this._currentStructureFragment.getArray());
/* 138 */     this._structurePtr = this._buffer.getStructurePtr();
/*     */ 
/* 140 */     this._currentStructureStringFragment = this._buffer.getStructureStrings();
/* 141 */     this._structureStrings = ((String[])this._currentStructureStringFragment.getArray());
/* 142 */     this._structureStringsPtr = this._buffer.getStructureStringsPtr();
/*     */ 
/* 144 */     this._currentContentCharactersBufferFragment = this._buffer.getContentCharactersBuffer();
/* 145 */     this._contentCharactersBuffer = ((char[])this._currentContentCharactersBufferFragment.getArray());
/* 146 */     this._contentCharactersBufferPtr = this._buffer.getContentCharactersBufferPtr();
/*     */ 
/* 148 */     this._currentContentObjectFragment = this._buffer.getContentObjects();
/* 149 */     this._contentObjects = ((Object[])this._currentContentObjectFragment.getArray());
/* 150 */     this._contentObjectsPtr = this._buffer.getContentObjectsPtr();
/*     */ 
/* 152 */     this._stringInterningFeature = this._buffer.hasInternedStrings();
/* 153 */     this._treeCount = this._buffer.treeCount;
/*     */   }
/*     */ 
/*     */   protected final int peekStructure() {
/* 157 */     if (this._structurePtr < this._structure.length) {
/* 158 */       return this._structure[this._structurePtr] & 0xFF;
/*     */     }
/*     */ 
/* 161 */     return readFromNextStructure(0);
/*     */   }
/*     */ 
/*     */   protected final int readStructure() {
/* 165 */     if (this._structurePtr < this._structure.length) {
/* 166 */       return this._structure[(this._structurePtr++)] & 0xFF;
/*     */     }
/*     */ 
/* 169 */     return readFromNextStructure(1);
/*     */   }
/*     */ 
/*     */   protected final int readEiiState() {
/* 173 */     return _eiiStateTable[readStructure()];
/*     */   }
/*     */ 
/*     */   protected static int getEIIState(int item) {
/* 177 */     return _eiiStateTable[item];
/*     */   }
/*     */ 
/*     */   protected static int getNIIState(int item) {
/* 181 */     return _niiStateTable[item];
/*     */   }
/*     */ 
/*     */   protected static int getAIIState(int item) {
/* 185 */     return _aiiStateTable[item];
/*     */   }
/*     */ 
/*     */   protected final int readStructure16() {
/* 189 */     return readStructure() << 8 | readStructure();
/*     */   }
/*     */ 
/*     */   private int readFromNextStructure(int v) {
/* 193 */     this._structurePtr = v;
/* 194 */     this._currentStructureFragment = this._currentStructureFragment.getNext();
/* 195 */     this._structure = ((byte[])this._currentStructureFragment.getArray());
/* 196 */     return this._structure[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   protected final String readStructureString() {
/* 200 */     if (this._structureStringsPtr < this._structureStrings.length) {
/* 201 */       return this._structureStrings[(this._structureStringsPtr++)];
/*     */     }
/*     */ 
/* 204 */     this._structureStringsPtr = 1;
/* 205 */     this._currentStructureStringFragment = this._currentStructureStringFragment.getNext();
/* 206 */     this._structureStrings = ((String[])this._currentStructureStringFragment.getArray());
/* 207 */     return this._structureStrings[0];
/*     */   }
/*     */ 
/*     */   protected final String readContentString() {
/* 211 */     return (String)readContentObject();
/*     */   }
/*     */ 
/*     */   protected final char[] readContentCharactersCopy() {
/* 215 */     return (char[])readContentObject();
/*     */   }
/*     */ 
/*     */   protected final int readContentCharactersBuffer(int length) {
/* 219 */     if (this._contentCharactersBufferPtr + length < this._contentCharactersBuffer.length) {
/* 220 */       int start = this._contentCharactersBufferPtr;
/* 221 */       this._contentCharactersBufferPtr += length;
/* 222 */       return start;
/*     */     }
/*     */ 
/* 225 */     this._contentCharactersBufferPtr = length;
/* 226 */     this._currentContentCharactersBufferFragment = this._currentContentCharactersBufferFragment.getNext();
/* 227 */     this._contentCharactersBuffer = ((char[])this._currentContentCharactersBufferFragment.getArray());
/* 228 */     return 0;
/*     */   }
/*     */ 
/*     */   protected final Object readContentObject() {
/* 232 */     if (this._contentObjectsPtr < this._contentObjects.length) {
/* 233 */       return this._contentObjects[(this._contentObjectsPtr++)];
/*     */     }
/*     */ 
/* 236 */     this._contentObjectsPtr = 1;
/* 237 */     this._currentContentObjectFragment = this._currentContentObjectFragment.getNext();
/* 238 */     this._contentObjects = ((Object[])this._currentContentObjectFragment.getArray());
/* 239 */     return this._contentObjects[0];
/*     */   }
/*     */ 
/*     */   protected final String getQName(String prefix, String localName)
/*     */   {
/* 245 */     this._qNameBuffer.append(prefix).append(':').append(localName);
/* 246 */     String qName = this._qNameBuffer.toString();
/* 247 */     this._qNameBuffer.setLength(0);
/* 248 */     return this._stringInterningFeature ? qName.intern() : qName;
/*     */   }
/*     */ 
/*     */   protected final String getPrefixFromQName(String qName) {
/* 252 */     int pIndex = qName.indexOf(':');
/* 253 */     if (this._stringInterningFeature) {
/* 254 */       return pIndex != -1 ? qName.substring(0, pIndex).intern() : "";
/*     */     }
/* 256 */     return pIndex != -1 ? qName.substring(0, pIndex) : "";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  77 */     _eiiStateTable[16] = 1;
/*  78 */     _eiiStateTable[17] = 2;
/*  79 */     _eiiStateTable[38] = 3;
/*  80 */     _eiiStateTable[35] = 4;
/*  81 */     _eiiStateTable[34] = 5;
/*  82 */     _eiiStateTable[32] = 6;
/*  83 */     _eiiStateTable[80] = 7;
/*  84 */     _eiiStateTable[81] = 8;
/*  85 */     _eiiStateTable[84] = 9;
/*  86 */     _eiiStateTable[88] = 10;
/*  87 */     _eiiStateTable[92] = 11;
/*  88 */     _eiiStateTable[96] = 12;
/*  89 */     _eiiStateTable[97] = 13;
/*  90 */     _eiiStateTable[100] = 14;
/*  91 */     _eiiStateTable[104] = 15;
/*  92 */     _eiiStateTable[112] = 16;
/*  93 */     _eiiStateTable[''] = 17;
/*     */ 
/*  95 */     _niiStateTable[64] = 1;
/*  96 */     _niiStateTable[65] = 2;
/*  97 */     _niiStateTable[67] = 3;
/*  98 */     _niiStateTable[66] = 4;
/*     */ 
/* 100 */     _aiiStateTable[54] = 1;
/* 101 */     _aiiStateTable[51] = 2;
/* 102 */     _aiiStateTable[50] = 3;
/* 103 */     _aiiStateTable[48] = 4;
/* 104 */     _aiiStateTable[62] = 5;
/* 105 */     _aiiStateTable[59] = 6;
/* 106 */     _aiiStateTable[58] = 7;
/* 107 */     _aiiStateTable[56] = 8;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.AbstractProcessor
 * JD-Core Version:    0.6.2
 */