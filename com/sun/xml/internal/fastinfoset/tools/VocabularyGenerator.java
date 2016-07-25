/*     */ package com.sun.xml.internal.fastinfoset.tools;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap.Entry;
/*     */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class VocabularyGenerator extends DefaultHandler
/*     */   implements LexicalHandler
/*     */ {
/*     */   protected SerializerVocabulary _serializerVocabulary;
/*     */   protected ParserVocabulary _parserVocabulary;
/*     */   protected Vocabulary _v;
/*  56 */   protected int attributeValueSizeConstraint = 32;
/*     */ 
/*  58 */   protected int characterContentChunkSizeContraint = 32;
/*     */ 
/*     */   public VocabularyGenerator()
/*     */   {
/*  62 */     this._serializerVocabulary = new SerializerVocabulary();
/*  63 */     this._parserVocabulary = new ParserVocabulary();
/*     */ 
/*  65 */     this._v = new Vocabulary();
/*     */   }
/*     */ 
/*     */   public VocabularyGenerator(SerializerVocabulary serializerVocabulary) {
/*  69 */     this._serializerVocabulary = serializerVocabulary;
/*  70 */     this._parserVocabulary = new ParserVocabulary();
/*     */ 
/*  72 */     this._v = new Vocabulary();
/*     */   }
/*     */ 
/*     */   public VocabularyGenerator(ParserVocabulary parserVocabulary) {
/*  76 */     this._serializerVocabulary = new SerializerVocabulary();
/*  77 */     this._parserVocabulary = parserVocabulary;
/*     */ 
/*  79 */     this._v = new Vocabulary();
/*     */   }
/*     */ 
/*     */   public VocabularyGenerator(SerializerVocabulary serializerVocabulary, ParserVocabulary parserVocabulary)
/*     */   {
/*  84 */     this._serializerVocabulary = serializerVocabulary;
/*  85 */     this._parserVocabulary = parserVocabulary;
/*     */ 
/*  87 */     this._v = new Vocabulary();
/*     */   }
/*     */ 
/*     */   public Vocabulary getVocabulary() {
/*  91 */     return this._v;
/*     */   }
/*     */ 
/*     */   public void setCharacterContentChunkSizeLimit(int size) {
/*  95 */     if (size < 0) {
/*  96 */       size = 0;
/*     */     }
/*     */ 
/*  99 */     this.characterContentChunkSizeContraint = size;
/*     */   }
/*     */ 
/*     */   public int getCharacterContentChunkSizeLimit() {
/* 103 */     return this.characterContentChunkSizeContraint;
/*     */   }
/*     */ 
/*     */   public void setAttributeValueSizeLimit(int size) {
/* 107 */     if (size < 0) {
/* 108 */       size = 0;
/*     */     }
/*     */ 
/* 111 */     this.attributeValueSizeConstraint = size;
/*     */   }
/*     */ 
/*     */   public int getAttributeValueSizeLimit() {
/* 115 */     return this.attributeValueSizeConstraint;
/*     */   }
/*     */ 
/*     */   public void startDocument() throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 127 */     addToTable(prefix, this._v.prefixes, this._serializerVocabulary.prefix, this._parserVocabulary.prefix);
/* 128 */     addToTable(uri, this._v.namespaceNames, this._serializerVocabulary.namespaceName, this._parserVocabulary.namespaceName);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/* 135 */     addToNameTable(namespaceURI, qName, localName, this._v.elements, this._serializerVocabulary.elementName, this._parserVocabulary.elementName, false);
/*     */ 
/* 138 */     for (int a = 0; a < atts.getLength(); a++) {
/* 139 */       addToNameTable(atts.getURI(a), atts.getQName(a), atts.getLocalName(a), this._v.attributes, this._serializerVocabulary.attributeName, this._parserVocabulary.attributeName, true);
/*     */ 
/* 142 */       String value = atts.getValue(a);
/* 143 */       if (value.length() < this.attributeValueSizeConstraint)
/* 144 */         addToTable(value, this._v.attributeValues, this._serializerVocabulary.attributeValue, this._parserVocabulary.attributeValue);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 153 */     if (length < this.characterContentChunkSizeContraint)
/* 154 */       addToCharArrayTable(new CharArray(ch, start, length, true));
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void addToTable(String s, Set v, StringIntMap m, StringArray a) {
/* 197 */     if (s.length() == 0) {
/* 198 */       return;
/*     */     }
/*     */ 
/* 201 */     if (m.obtainIndex(s) == -1) {
/* 202 */       a.add(s);
/*     */     }
/*     */ 
/* 205 */     v.add(s);
/*     */   }
/*     */ 
/*     */   public void addToTable(String s, Set v, StringIntMap m, PrefixArray a) {
/* 209 */     if (s.length() == 0) {
/* 210 */       return;
/*     */     }
/*     */ 
/* 213 */     if (m.obtainIndex(s) == -1) {
/* 214 */       a.add(s);
/*     */     }
/*     */ 
/* 217 */     v.add(s);
/*     */   }
/*     */ 
/*     */   public void addToCharArrayTable(CharArray c) {
/* 221 */     if (this._serializerVocabulary.characterContentChunk.obtainIndex(c.ch, c.start, c.length, false) == -1) {
/* 222 */       this._parserVocabulary.characterContentChunk.add(c.ch, c.length);
/*     */     }
/*     */ 
/* 225 */     this._v.characterContentChunks.add(c.toString());
/*     */   }
/*     */ 
/*     */   public void addToNameTable(String namespaceURI, String qName, String localName, Set v, LocalNameQualifiedNamesMap m, QualifiedNameArray a, boolean isAttribute)
/*     */     throws SAXException
/*     */   {
/* 231 */     LocalNameQualifiedNamesMap.Entry entry = m.obtainEntry(qName);
/* 232 */     if (entry._valueIndex > 0) {
/* 233 */       QualifiedName[] names = entry._value;
/* 234 */       for (int i = 0; i < entry._valueIndex; i++) {
/* 235 */         if ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))) {
/* 236 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 241 */     String prefix = getPrefixFromQualifiedName(qName);
/*     */ 
/* 243 */     int namespaceURIIndex = -1;
/* 244 */     int prefixIndex = -1;
/* 245 */     int localNameIndex = -1;
/* 246 */     if (namespaceURI.length() > 0) {
/* 247 */       namespaceURIIndex = this._serializerVocabulary.namespaceName.get(namespaceURI);
/* 248 */       if (namespaceURIIndex == -1) {
/* 249 */         throw new SAXException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[] { Integer.valueOf(namespaceURIIndex) }));
/*     */       }
/*     */ 
/* 253 */       if (prefix.length() > 0) {
/* 254 */         prefixIndex = this._serializerVocabulary.prefix.get(prefix);
/* 255 */         if (prefixIndex == -1) {
/* 256 */           throw new SAXException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[] { Integer.valueOf(prefixIndex) }));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 262 */     localNameIndex = this._serializerVocabulary.localName.obtainIndex(localName);
/* 263 */     if (localNameIndex == -1) {
/* 264 */       this._parserVocabulary.localName.add(localName);
/* 265 */       localNameIndex = this._parserVocabulary.localName.getSize() - 1;
/*     */     }
/* 267 */     QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, m.getNextIndex(), prefixIndex, namespaceURIIndex, localNameIndex);
/*     */ 
/* 269 */     if (isAttribute) {
/* 270 */       name.createAttributeValues(256);
/*     */     }
/* 272 */     entry.addQualifiedName(name);
/* 273 */     a.add(name);
/*     */ 
/* 275 */     v.add(name.getQName());
/*     */   }
/*     */ 
/*     */   public static String getPrefixFromQualifiedName(String qName) {
/* 279 */     int i = qName.indexOf(':');
/* 280 */     String prefix = "";
/* 281 */     if (i != -1) {
/* 282 */       prefix = qName.substring(0, i);
/*     */     }
/* 284 */     return prefix;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.VocabularyGenerator
 * JD-Core Version:    0.6.2
 */