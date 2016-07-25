/*     */ package com.sun.xml.internal.fastinfoset.vocab;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.FixedEntryStringIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringIntMap;
/*     */ import com.sun.xml.internal.fastinfoset.util.ValueArray;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class ParserVocabulary extends Vocabulary
/*     */ {
/*     */   public static final String IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.IdentifyingStringTable.maximumItems";
/*     */   public static final String NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumItems";
/*     */   public static final String NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS_PEOPERTY = "com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumCharacters";
/*  54 */   protected static final int IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS = getIntegerValueFromProperty("com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.IdentifyingStringTable.maximumItems");
/*     */ 
/*  56 */   protected static final int NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS = getIntegerValueFromProperty("com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumItems");
/*     */ 
/*  58 */   protected static final int NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS = getIntegerValueFromProperty("com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary.NonIdentifyingStringTable.maximumCharacters");
/*     */ 
/*  74 */   public final CharArrayArray restrictedAlphabet = new CharArrayArray(10, 256);
/*  75 */   public final StringArray encodingAlgorithm = new StringArray(10, 256, true);
/*     */   public final StringArray namespaceName;
/*     */   public final PrefixArray prefix;
/*     */   public final StringArray localName;
/*     */   public final StringArray otherNCName;
/*     */   public final StringArray otherURI;
/*     */   public final StringArray attributeValue;
/*     */   public final CharArrayArray otherString;
/*     */   public final ContiguousCharArrayArray characterContentChunk;
/*     */   public final QualifiedNameArray elementName;
/*     */   public final QualifiedNameArray attributeName;
/*  90 */   public final ValueArray[] tables = new ValueArray[12];
/*     */   protected SerializerVocabulary _readOnlyVocabulary;
/*     */ 
/*     */   private static int getIntegerValueFromProperty(String property)
/*     */   {
/*  62 */     String value = System.getProperty(property);
/*  63 */     if (value == null) {
/*  64 */       return 2147483647;
/*     */     }
/*     */     try
/*     */     {
/*  68 */       return Math.max(Integer.parseInt(value), 10); } catch (NumberFormatException e) {
/*     */     }
/*  70 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public ParserVocabulary()
/*     */   {
/*  96 */     this.namespaceName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
/*  97 */     this.prefix = new PrefixArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
/*  98 */     this.localName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
/*  99 */     this.otherNCName = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, false);
/* 100 */     this.otherURI = new StringArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, true);
/* 101 */     this.attributeValue = new StringArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, true);
/* 102 */     this.otherString = new CharArrayArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
/*     */ 
/* 104 */     this.characterContentChunk = new ContiguousCharArrayArray(10, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS, 512, NON_IDENTIFYING_STRING_TABLE_MAXIMUM_CHARACTERS);
/*     */ 
/* 109 */     this.elementName = new QualifiedNameArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
/* 110 */     this.attributeName = new QualifiedNameArray(10, IDENTIFYING_STRING_TABLE_MAXIMUM_ITEMS);
/*     */ 
/* 112 */     this.tables[0] = this.restrictedAlphabet;
/* 113 */     this.tables[1] = this.encodingAlgorithm;
/* 114 */     this.tables[2] = this.prefix;
/* 115 */     this.tables[3] = this.namespaceName;
/* 116 */     this.tables[4] = this.localName;
/* 117 */     this.tables[5] = this.otherNCName;
/* 118 */     this.tables[6] = this.otherURI;
/* 119 */     this.tables[7] = this.attributeValue;
/* 120 */     this.tables[8] = this.otherString;
/* 121 */     this.tables[9] = this.characterContentChunk;
/* 122 */     this.tables[10] = this.elementName;
/* 123 */     this.tables[11] = this.attributeName;
/*     */   }
/*     */ 
/*     */   public ParserVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v)
/*     */   {
/* 128 */     this();
/*     */ 
/* 130 */     convertVocabulary(v);
/*     */   }
/*     */ 
/*     */   void setReadOnlyVocabulary(ParserVocabulary readOnlyVocabulary, boolean clear) {
/* 134 */     for (int i = 0; i < this.tables.length; i++)
/* 135 */       this.tables[i].setReadOnlyArray(readOnlyVocabulary.tables[i], clear);
/*     */   }
/*     */ 
/*     */   public void setInitialVocabulary(ParserVocabulary initialVocabulary, boolean clear)
/*     */   {
/* 140 */     setExternalVocabularyURI(null);
/* 141 */     setInitialReadOnlyVocabulary(true);
/* 142 */     setReadOnlyVocabulary(initialVocabulary, clear);
/*     */   }
/*     */ 
/*     */   public void setReferencedVocabulary(String referencedVocabularyURI, ParserVocabulary referencedVocabulary, boolean clear) {
/* 146 */     if (!referencedVocabularyURI.equals(getExternalVocabularyURI())) {
/* 147 */       setInitialReadOnlyVocabulary(false);
/* 148 */       setExternalVocabularyURI(referencedVocabularyURI);
/* 149 */       setReadOnlyVocabulary(referencedVocabulary, clear);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 154 */     for (int i = 0; i < this.tables.length; i++)
/* 155 */       this.tables[i].clear();
/*     */   }
/*     */ 
/*     */   private void convertVocabulary(com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary v)
/*     */   {
/* 160 */     StringIntMap prefixMap = new FixedEntryStringIntMap("xml", 8);
/*     */ 
/* 162 */     StringIntMap namespaceNameMap = new FixedEntryStringIntMap("http://www.w3.org/XML/1998/namespace", 8);
/*     */ 
/* 164 */     StringIntMap localNameMap = new StringIntMap();
/*     */ 
/* 166 */     addToTable(v.restrictedAlphabets.iterator(), this.restrictedAlphabet);
/* 167 */     addToTable(v.encodingAlgorithms.iterator(), this.encodingAlgorithm);
/* 168 */     addToTable(v.prefixes.iterator(), this.prefix, prefixMap);
/* 169 */     addToTable(v.namespaceNames.iterator(), this.namespaceName, namespaceNameMap);
/* 170 */     addToTable(v.localNames.iterator(), this.localName, localNameMap);
/* 171 */     addToTable(v.otherNCNames.iterator(), this.otherNCName);
/* 172 */     addToTable(v.otherURIs.iterator(), this.otherURI);
/* 173 */     addToTable(v.attributeValues.iterator(), this.attributeValue);
/* 174 */     addToTable(v.otherStrings.iterator(), this.otherString);
/* 175 */     addToTable(v.characterContentChunks.iterator(), this.characterContentChunk);
/* 176 */     addToTable(v.elements.iterator(), this.elementName, false, prefixMap, namespaceNameMap, localNameMap);
/*     */ 
/* 178 */     addToTable(v.attributes.iterator(), this.attributeName, true, prefixMap, namespaceNameMap, localNameMap);
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, StringArray a)
/*     */   {
/* 183 */     while (i.hasNext())
/* 184 */       addToTable((String)i.next(), a, null);
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, StringArray a, StringIntMap m)
/*     */   {
/* 189 */     while (i.hasNext())
/* 190 */       addToTable((String)i.next(), a, m);
/*     */   }
/*     */ 
/*     */   private void addToTable(String s, StringArray a, StringIntMap m)
/*     */   {
/* 195 */     if (s.length() == 0) {
/* 196 */       return;
/*     */     }
/*     */ 
/* 199 */     if (m != null) m.obtainIndex(s);
/* 200 */     a.add(s);
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, PrefixArray a, StringIntMap m) {
/* 204 */     while (i.hasNext())
/* 205 */       addToTable((String)i.next(), a, m);
/*     */   }
/*     */ 
/*     */   private void addToTable(String s, PrefixArray a, StringIntMap m)
/*     */   {
/* 210 */     if (s.length() == 0) {
/* 211 */       return;
/*     */     }
/*     */ 
/* 214 */     if (m != null) m.obtainIndex(s);
/* 215 */     a.add(s);
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, ContiguousCharArrayArray a) {
/* 219 */     while (i.hasNext())
/* 220 */       addToTable((String)i.next(), a);
/*     */   }
/*     */ 
/*     */   private void addToTable(String s, ContiguousCharArrayArray a)
/*     */   {
/* 225 */     if (s.length() == 0) {
/* 226 */       return;
/*     */     }
/*     */ 
/* 229 */     char[] c = s.toCharArray();
/* 230 */     a.add(c, c.length);
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, CharArrayArray a) {
/* 234 */     while (i.hasNext())
/* 235 */       addToTable((String)i.next(), a);
/*     */   }
/*     */ 
/*     */   private void addToTable(String s, CharArrayArray a)
/*     */   {
/* 240 */     if (s.length() == 0) {
/* 241 */       return;
/*     */     }
/*     */ 
/* 244 */     char[] c = s.toCharArray();
/* 245 */     a.add(new CharArray(c, 0, c.length, false));
/*     */   }
/*     */ 
/*     */   private void addToTable(Iterator i, QualifiedNameArray a, boolean isAttribute, StringIntMap prefixMap, StringIntMap namespaceNameMap, StringIntMap localNameMap)
/*     */   {
/* 252 */     while (i.hasNext())
/* 253 */       addToNameTable((QName)i.next(), a, isAttribute, prefixMap, namespaceNameMap, localNameMap);
/*     */   }
/*     */ 
/*     */   private void addToNameTable(QName n, QualifiedNameArray a, boolean isAttribute, StringIntMap prefixMap, StringIntMap namespaceNameMap, StringIntMap localNameMap)
/*     */   {
/* 262 */     int namespaceURIIndex = -1;
/* 263 */     int prefixIndex = -1;
/* 264 */     if (n.getNamespaceURI().length() > 0) {
/* 265 */       namespaceURIIndex = namespaceNameMap.obtainIndex(n.getNamespaceURI());
/* 266 */       if (namespaceURIIndex == -1) {
/* 267 */         namespaceURIIndex = this.namespaceName.getSize();
/* 268 */         this.namespaceName.add(n.getNamespaceURI());
/*     */       }
/*     */ 
/* 271 */       if (n.getPrefix().length() > 0) {
/* 272 */         prefixIndex = prefixMap.obtainIndex(n.getPrefix());
/* 273 */         if (prefixIndex == -1) {
/* 274 */           prefixIndex = this.prefix.getSize();
/* 275 */           this.prefix.add(n.getPrefix());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 280 */     int localNameIndex = localNameMap.obtainIndex(n.getLocalPart());
/* 281 */     if (localNameIndex == -1) {
/* 282 */       localNameIndex = this.localName.getSize();
/* 283 */       this.localName.add(n.getLocalPart());
/*     */     }
/*     */ 
/* 286 */     QualifiedName name = new QualifiedName(n.getPrefix(), n.getNamespaceURI(), n.getLocalPart(), a.getSize(), prefixIndex, namespaceURIIndex, localNameIndex);
/*     */ 
/* 289 */     if (isAttribute) {
/* 290 */       name.createAttributeValues(256);
/*     */     }
/* 292 */     a.add(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary
 * JD-Core Version:    0.6.2
 */