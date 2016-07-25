/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ public class AbstractCreator extends AbstractCreatorProcessor
/*     */ {
/*     */   protected MutableXMLStreamBuffer _buffer;
/*     */ 
/*     */   public void setXMLStreamBuffer(MutableXMLStreamBuffer buffer)
/*     */   {
/*  37 */     if (buffer == null) {
/*  38 */       throw new NullPointerException("buffer cannot be null");
/*     */     }
/*  40 */     setBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer getXMLStreamBuffer() {
/*  44 */     return this._buffer;
/*     */   }
/*     */ 
/*     */   protected final void createBuffer()
/*     */   {
/*  49 */     setBuffer(new MutableXMLStreamBuffer());
/*     */   }
/*     */ 
/*     */   protected final void increaseTreeCount()
/*     */   {
/*  56 */     this._buffer.treeCount += 1;
/*     */   }
/*     */ 
/*     */   protected final void setBuffer(MutableXMLStreamBuffer buffer) {
/*  60 */     this._buffer = buffer;
/*     */ 
/*  62 */     this._currentStructureFragment = this._buffer.getStructure();
/*  63 */     this._structure = ((byte[])this._currentStructureFragment.getArray());
/*  64 */     this._structurePtr = 0;
/*     */ 
/*  66 */     this._currentStructureStringFragment = this._buffer.getStructureStrings();
/*  67 */     this._structureStrings = ((String[])this._currentStructureStringFragment.getArray());
/*  68 */     this._structureStringsPtr = 0;
/*     */ 
/*  70 */     this._currentContentCharactersBufferFragment = this._buffer.getContentCharactersBuffer();
/*  71 */     this._contentCharactersBuffer = ((char[])this._currentContentCharactersBufferFragment.getArray());
/*  72 */     this._contentCharactersBufferPtr = 0;
/*     */ 
/*  74 */     this._currentContentObjectFragment = this._buffer.getContentObjects();
/*  75 */     this._contentObjects = ((Object[])this._currentContentObjectFragment.getArray());
/*  76 */     this._contentObjectsPtr = 0;
/*     */   }
/*     */ 
/*     */   protected final void setHasInternedStrings(boolean hasInternedStrings) {
/*  80 */     this._buffer.setHasInternedStrings(hasInternedStrings);
/*     */   }
/*     */ 
/*     */   protected final void storeStructure(int b) {
/*  84 */     this._structure[(this._structurePtr++)] = ((byte)b);
/*  85 */     if (this._structurePtr == this._structure.length)
/*  86 */       resizeStructure();
/*     */   }
/*     */ 
/*     */   protected final void resizeStructure()
/*     */   {
/*  91 */     this._structurePtr = 0;
/*  92 */     if (this._currentStructureFragment.getNext() != null) {
/*  93 */       this._currentStructureFragment = this._currentStructureFragment.getNext();
/*  94 */       this._structure = ((byte[])this._currentStructureFragment.getArray());
/*     */     } else {
/*  96 */       this._structure = new byte[this._structure.length];
/*  97 */       this._currentStructureFragment = new FragmentedArray(this._structure, this._currentStructureFragment);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void storeStructureString(String s) {
/* 102 */     this._structureStrings[(this._structureStringsPtr++)] = s;
/* 103 */     if (this._structureStringsPtr == this._structureStrings.length)
/* 104 */       resizeStructureStrings();
/*     */   }
/*     */ 
/*     */   protected final void resizeStructureStrings()
/*     */   {
/* 109 */     this._structureStringsPtr = 0;
/* 110 */     if (this._currentStructureStringFragment.getNext() != null) {
/* 111 */       this._currentStructureStringFragment = this._currentStructureStringFragment.getNext();
/* 112 */       this._structureStrings = ((String[])this._currentStructureStringFragment.getArray());
/*     */     } else {
/* 114 */       this._structureStrings = new String[this._structureStrings.length];
/* 115 */       this._currentStructureStringFragment = new FragmentedArray(this._structureStrings, this._currentStructureStringFragment);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void storeContentString(String s) {
/* 120 */     storeContentObject(s);
/*     */   }
/*     */ 
/*     */   protected final void storeContentCharacters(int type, char[] ch, int start, int length) {
/* 124 */     if (this._contentCharactersBufferPtr + length >= this._contentCharactersBuffer.length) {
/* 125 */       if (length >= 512) {
/* 126 */         storeStructure(type | 0x4);
/* 127 */         storeContentCharactersCopy(ch, start, length);
/* 128 */         return;
/*     */       }
/* 130 */       resizeContentCharacters();
/*     */     }
/*     */ 
/* 133 */     if (length < 256) {
/* 134 */       storeStructure(type | 0x0);
/* 135 */       storeStructure(length);
/* 136 */       System.arraycopy(ch, start, this._contentCharactersBuffer, this._contentCharactersBufferPtr, length);
/* 137 */       this._contentCharactersBufferPtr += length;
/* 138 */     } else if (length < 65536) {
/* 139 */       storeStructure(type | 0x1);
/* 140 */       storeStructure(length >> 8);
/* 141 */       storeStructure(length & 0xFF);
/* 142 */       System.arraycopy(ch, start, this._contentCharactersBuffer, this._contentCharactersBufferPtr, length);
/* 143 */       this._contentCharactersBufferPtr += length;
/*     */     } else {
/* 145 */       storeStructure(type | 0x4);
/* 146 */       storeContentCharactersCopy(ch, start, length);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void resizeContentCharacters() {
/* 151 */     this._contentCharactersBufferPtr = 0;
/* 152 */     if (this._currentContentCharactersBufferFragment.getNext() != null) {
/* 153 */       this._currentContentCharactersBufferFragment = this._currentContentCharactersBufferFragment.getNext();
/* 154 */       this._contentCharactersBuffer = ((char[])this._currentContentCharactersBufferFragment.getArray());
/*     */     } else {
/* 156 */       this._contentCharactersBuffer = new char[this._contentCharactersBuffer.length];
/* 157 */       this._currentContentCharactersBufferFragment = new FragmentedArray(this._contentCharactersBuffer, this._currentContentCharactersBufferFragment);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void storeContentCharactersCopy(char[] ch, int start, int length)
/*     */   {
/* 163 */     char[] copyOfCh = new char[length];
/* 164 */     System.arraycopy(ch, start, copyOfCh, 0, length);
/* 165 */     storeContentObject(copyOfCh);
/*     */   }
/*     */ 
/*     */   protected final Object peekAtContentObject() {
/* 169 */     return this._contentObjects[this._contentObjectsPtr];
/*     */   }
/*     */ 
/*     */   protected final void storeContentObject(Object s) {
/* 173 */     this._contentObjects[(this._contentObjectsPtr++)] = s;
/* 174 */     if (this._contentObjectsPtr == this._contentObjects.length)
/* 175 */       resizeContentObjects();
/*     */   }
/*     */ 
/*     */   protected final void resizeContentObjects()
/*     */   {
/* 180 */     this._contentObjectsPtr = 0;
/* 181 */     if (this._currentContentObjectFragment.getNext() != null) {
/* 182 */       this._currentContentObjectFragment = this._currentContentObjectFragment.getNext();
/* 183 */       this._contentObjects = ((Object[])this._currentContentObjectFragment.getArray());
/*     */     } else {
/* 185 */       this._contentObjects = new Object[this._contentObjects.length];
/* 186 */       this._currentContentObjectFragment = new FragmentedArray(this._contentObjects, this._currentContentObjectFragment);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.AbstractCreator
 * JD-Core Version:    0.6.2
 */