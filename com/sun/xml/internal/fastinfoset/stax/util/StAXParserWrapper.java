/*     */ package com.sun.xml.internal.fastinfoset.stax.util;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class StAXParserWrapper
/*     */   implements XMLStreamReader
/*     */ {
/*     */   private XMLStreamReader _reader;
/*     */ 
/*     */   public StAXParserWrapper()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StAXParserWrapper(XMLStreamReader reader)
/*     */   {
/*  46 */     this._reader = reader;
/*     */   }
/*     */   public void setReader(XMLStreamReader reader) {
/*  49 */     this._reader = reader;
/*     */   }
/*     */   public XMLStreamReader getReader() {
/*  52 */     return this._reader;
/*     */   }
/*     */ 
/*     */   public int next() throws XMLStreamException
/*     */   {
/*  57 */     return this._reader.next();
/*     */   }
/*     */ 
/*     */   public int nextTag() throws XMLStreamException
/*     */   {
/*  62 */     return this._reader.nextTag();
/*     */   }
/*     */ 
/*     */   public String getElementText() throws XMLStreamException
/*     */   {
/*  67 */     return this._reader.getElementText();
/*     */   }
/*     */ 
/*     */   public void require(int type, String namespaceURI, String localName) throws XMLStreamException
/*     */   {
/*  72 */     this._reader.require(type, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public boolean hasNext() throws XMLStreamException
/*     */   {
/*  77 */     return this._reader.hasNext();
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException
/*     */   {
/*  82 */     this._reader.close();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/*  87 */     return this._reader.getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/*  91 */     return this._reader.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public boolean isStartElement() {
/*  95 */     return this._reader.isStartElement();
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/*  99 */     return this._reader.isEndElement();
/*     */   }
/*     */ 
/*     */   public boolean isCharacters() {
/* 103 */     return this._reader.isCharacters();
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace() {
/* 107 */     return this._reader.isWhiteSpace();
/*     */   }
/*     */ 
/*     */   public QName getAttributeName(int index) {
/* 111 */     return this._reader.getAttributeName(index);
/*     */   }
/*     */ 
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/*     */     throws XMLStreamException
/*     */   {
/* 117 */     return this._reader.getTextCharacters(sourceStart, target, targetStart, length);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(String namespaceUri, String localName)
/*     */   {
/* 123 */     return this._reader.getAttributeValue(namespaceUri, localName);
/*     */   }
/*     */   public int getAttributeCount() {
/* 126 */     return this._reader.getAttributeCount();
/*     */   }
/*     */   public String getAttributePrefix(int index) {
/* 129 */     return this._reader.getAttributePrefix(index);
/*     */   }
/*     */   public String getAttributeNamespace(int index) {
/* 132 */     return this._reader.getAttributeNamespace(index);
/*     */   }
/*     */   public String getAttributeLocalName(int index) {
/* 135 */     return this._reader.getAttributeLocalName(index);
/*     */   }
/*     */   public String getAttributeType(int index) {
/* 138 */     return this._reader.getAttributeType(index);
/*     */   }
/*     */   public String getAttributeValue(int index) {
/* 141 */     return this._reader.getAttributeValue(index);
/*     */   }
/*     */   public boolean isAttributeSpecified(int index) {
/* 144 */     return this._reader.isAttributeSpecified(index);
/*     */   }
/*     */ 
/*     */   public int getNamespaceCount() {
/* 148 */     return this._reader.getNamespaceCount();
/*     */   }
/*     */   public String getNamespacePrefix(int index) {
/* 151 */     return this._reader.getNamespacePrefix(index);
/*     */   }
/*     */   public String getNamespaceURI(int index) {
/* 154 */     return this._reader.getNamespaceURI(index);
/*     */   }
/*     */ 
/*     */   public int getEventType() {
/* 158 */     return this._reader.getEventType();
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 162 */     return this._reader.getText();
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters() {
/* 166 */     return this._reader.getTextCharacters();
/*     */   }
/*     */ 
/*     */   public int getTextStart() {
/* 170 */     return this._reader.getTextStart();
/*     */   }
/*     */ 
/*     */   public int getTextLength() {
/* 174 */     return this._reader.getTextLength();
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 178 */     return this._reader.getEncoding();
/*     */   }
/*     */ 
/*     */   public boolean hasText() {
/* 182 */     return this._reader.hasText();
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 186 */     return this._reader.getLocation();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 190 */     return this._reader.getName();
/*     */   }
/*     */ 
/*     */   public String getLocalName() {
/* 194 */     return this._reader.getLocalName();
/*     */   }
/*     */ 
/*     */   public boolean hasName() {
/* 198 */     return this._reader.hasName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 202 */     return this._reader.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 206 */     return this._reader.getPrefix();
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 210 */     return this._reader.getVersion();
/*     */   }
/*     */ 
/*     */   public boolean isStandalone() {
/* 214 */     return this._reader.isStandalone();
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet() {
/* 218 */     return this._reader.standaloneSet();
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme() {
/* 222 */     return this._reader.getCharacterEncodingScheme();
/*     */   }
/*     */ 
/*     */   public String getPITarget() {
/* 226 */     return this._reader.getPITarget();
/*     */   }
/*     */ 
/*     */   public String getPIData() {
/* 230 */     return this._reader.getPIData();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) {
/* 234 */     return this._reader.getProperty(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.util.StAXParserWrapper
 * JD-Core Version:    0.6.2
 */