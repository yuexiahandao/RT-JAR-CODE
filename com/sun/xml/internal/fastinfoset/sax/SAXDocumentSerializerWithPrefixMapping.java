/*     */ package com.sun.xml.internal.fastinfoset.sax;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap.Entry;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SAXDocumentSerializerWithPrefixMapping extends SAXDocumentSerializer
/*     */ {
/*     */   protected Map _namespaceToPrefixMapping;
/*     */   protected Map _prefixToPrefixMapping;
/*     */   protected String _lastCheckedNamespace;
/*     */   protected String _lastCheckedPrefix;
/*     */   protected StringIntMap _declaredNamespaces;
/*     */ 
/*     */   public SAXDocumentSerializerWithPrefixMapping(Map namespaceToPrefixMapping)
/*     */   {
/*  70 */     super(true);
/*  71 */     this._namespaceToPrefixMapping = new HashMap(namespaceToPrefixMapping);
/*  72 */     this._prefixToPrefixMapping = new HashMap();
/*     */ 
/*  75 */     this._namespaceToPrefixMapping.put("", "");
/*     */ 
/*  77 */     this._namespaceToPrefixMapping.put("http://www.w3.org/XML/1998/namespace", "xml");
/*     */ 
/*  79 */     this._declaredNamespaces = new StringIntMap(4);
/*     */   }
/*     */ 
/*     */   public final void startPrefixMapping(String prefix, String uri) throws SAXException {
/*     */     try {
/*  84 */       if (!this._elementHasNamespaces) {
/*  85 */         encodeTermination();
/*     */ 
/*  88 */         mark();
/*  89 */         this._elementHasNamespaces = true;
/*     */ 
/*  92 */         write(56);
/*     */ 
/*  94 */         this._declaredNamespaces.clear();
/*  95 */         this._declaredNamespaces.obtainIndex(uri);
/*     */       }
/*  97 */       else if (this._declaredNamespaces.obtainIndex(uri) != -1) {
/*  98 */         String p = getPrefix(uri);
/*  99 */         if (p != null) {
/* 100 */           this._prefixToPrefixMapping.put(prefix, p);
/*     */         }
/* 102 */         return;
/*     */       }
/*     */ 
/* 106 */       String p = getPrefix(uri);
/* 107 */       if (p != null) {
/* 108 */         encodeNamespaceAttribute(p, uri);
/* 109 */         this._prefixToPrefixMapping.put(prefix, p);
/*     */       } else {
/* 111 */         putPrefix(uri, prefix);
/* 112 */         encodeNamespaceAttribute(prefix, uri);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 116 */       throw new SAXException("startElement", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
/* 121 */     LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
/* 122 */     if (entry._valueIndex > 0) {
/* 123 */       if (encodeElementMapEntry(entry, namespaceURI)) return;
/*     */ 
/* 125 */       if (this._v.elementName.isQNameFromReadOnlyMap(entry._value[0])) {
/* 126 */         entry = this._v.elementName.obtainDynamicEntry(localName);
/* 127 */         if ((entry._valueIndex > 0) && 
/* 128 */           (encodeElementMapEntry(entry, namespaceURI))) return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 133 */     encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
/*     */   }
/*     */ 
/*     */   protected boolean encodeElementMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException
/*     */   {
/* 138 */     QualifiedName[] names = entry._value;
/* 139 */     for (int i = 0; i < entry._valueIndex; i++) {
/* 140 */       if ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))) {
/* 141 */         encodeNonZeroIntegerOnThirdBit(names[i].index);
/* 142 */         return true;
/*     */       }
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   protected final void encodeAttributes(Attributes atts)
/*     */     throws IOException, FastInfosetException
/*     */   {
/* 153 */     if ((atts instanceof EncodingAlgorithmAttributes)) {
/* 154 */       EncodingAlgorithmAttributes eAtts = (EncodingAlgorithmAttributes)atts;
/*     */ 
/* 157 */       for (int i = 0; i < eAtts.getLength(); i++) {
/* 158 */         String uri = atts.getURI(i);
/* 159 */         if (encodeAttribute(uri, atts.getQName(i), atts.getLocalName(i))) {
/* 160 */           Object data = eAtts.getAlgorithmData(i);
/*     */ 
/* 162 */           if (data == null) {
/* 163 */             String value = eAtts.getValue(i);
/* 164 */             boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
/* 165 */             boolean mustToBeAddedToTable = eAtts.getToIndex(i);
/* 166 */             String alphabet = eAtts.getAlpababet(i);
/* 167 */             if (alphabet == null) {
/* 168 */               if ((uri == "http://www.w3.org/2001/XMLSchema-instance") || (uri.equals("http://www.w3.org/2001/XMLSchema-instance")))
/*     */               {
/* 170 */                 value = convertQName(value);
/*     */               }
/* 172 */               encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
/* 173 */             } else if (alphabet == "0123456789-:TZ ") {
/* 174 */               encodeDateTimeNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
/*     */             }
/* 176 */             else if (alphabet == "0123456789-+.E ") {
/* 177 */               encodeNumericNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
/*     */             }
/*     */             else {
/* 180 */               encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
/*     */             }
/*     */           } else {
/* 183 */             encodeNonIdentifyingStringOnFirstBit(eAtts.getAlgorithmURI(i), eAtts.getAlgorithmIndex(i), data);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 189 */       for (int i = 0; i < atts.getLength(); i++) {
/* 190 */         String uri = atts.getURI(i);
/* 191 */         if (encodeAttribute(atts.getURI(i), atts.getQName(i), atts.getLocalName(i))) {
/* 192 */           String value = atts.getValue(i);
/* 193 */           boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
/*     */ 
/* 195 */           if ((uri == "http://www.w3.org/2001/XMLSchema-instance") || (uri.equals("http://www.w3.org/2001/XMLSchema-instance")))
/*     */           {
/* 197 */             value = convertQName(value);
/*     */           }
/* 199 */           encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
/*     */         }
/*     */       }
/*     */     }
/* 203 */     this._b = 240;
/* 204 */     this._terminate = true;
/*     */   }
/*     */ 
/*     */   private String convertQName(String qName) {
/* 208 */     int i = qName.indexOf(':');
/* 209 */     String prefix = "";
/* 210 */     String localName = qName;
/* 211 */     if (i != -1) {
/* 212 */       prefix = qName.substring(0, i);
/* 213 */       localName = qName.substring(i + 1);
/*     */     }
/*     */ 
/* 216 */     String p = (String)this._prefixToPrefixMapping.get(prefix);
/* 217 */     if (p != null) {
/* 218 */       if (p.length() == 0) {
/* 219 */         return localName;
/*     */       }
/* 221 */       return p + ":" + localName;
/*     */     }
/* 223 */     return qName;
/*     */   }
/*     */ 
/*     */   protected final boolean encodeAttribute(String namespaceURI, String qName, String localName) throws IOException
/*     */   {
/* 228 */     LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
/* 229 */     if (entry._valueIndex > 0) {
/* 230 */       if (encodeAttributeMapEntry(entry, namespaceURI)) return true;
/*     */ 
/* 232 */       if (this._v.attributeName.isQNameFromReadOnlyMap(entry._value[0])) {
/* 233 */         entry = this._v.attributeName.obtainDynamicEntry(localName);
/* 234 */         if ((entry._valueIndex > 0) && 
/* 235 */           (encodeAttributeMapEntry(entry, namespaceURI))) return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 240 */     return encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
/*     */   }
/*     */ 
/*     */   protected boolean encodeAttributeMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException
/*     */   {
/* 245 */     QualifiedName[] names = entry._value;
/* 246 */     for (int i = 0; i < entry._valueIndex; i++) {
/* 247 */       if ((namespaceURI == names[i].namespaceName) || (namespaceURI.equals(names[i].namespaceName))) {
/* 248 */         encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
/* 249 */         return true;
/*     */       }
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   protected final String getPrefix(String namespaceURI) {
/* 256 */     if (this._lastCheckedNamespace == namespaceURI) return this._lastCheckedPrefix;
/*     */ 
/* 258 */     this._lastCheckedNamespace = namespaceURI;
/* 259 */     return this._lastCheckedPrefix = (String)this._namespaceToPrefixMapping.get(namespaceURI);
/*     */   }
/*     */ 
/*     */   protected final void putPrefix(String namespaceURI, String prefix) {
/* 263 */     this._namespaceToPrefixMapping.put(namespaceURI, prefix);
/*     */ 
/* 265 */     this._lastCheckedNamespace = namespaceURI;
/* 266 */     this._lastCheckedPrefix = prefix;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializerWithPrefixMapping
 * JD-Core Version:    0.6.2
 */