/*     */ package com.sun.xml.internal.ws.util.xml;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.RecycleAware;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public class XMLStreamReaderFilter
/*     */   implements XMLStreamReaderFactory.RecycleAware, XMLStreamReader
/*     */ {
/*     */   protected XMLStreamReader reader;
/*     */ 
/*     */   public XMLStreamReaderFilter(XMLStreamReader core)
/*     */   {
/*  52 */     this.reader = core;
/*     */   }
/*     */ 
/*     */   public void onRecycled() {
/*  56 */     XMLStreamReaderFactory.recycle(this.reader);
/*  57 */     this.reader = null;
/*     */   }
/*     */ 
/*     */   public int getAttributeCount() {
/*  61 */     return this.reader.getAttributeCount();
/*     */   }
/*     */ 
/*     */   public int getEventType() {
/*  65 */     return this.reader.getEventType();
/*     */   }
/*     */ 
/*     */   public int getNamespaceCount() {
/*  69 */     return this.reader.getNamespaceCount();
/*     */   }
/*     */ 
/*     */   public int getTextLength() {
/*  73 */     return this.reader.getTextLength();
/*     */   }
/*     */ 
/*     */   public int getTextStart() {
/*  77 */     return this.reader.getTextStart();
/*     */   }
/*     */ 
/*     */   public int next() throws XMLStreamException {
/*  81 */     return this.reader.next();
/*     */   }
/*     */ 
/*     */   public int nextTag() throws XMLStreamException {
/*  85 */     return this.reader.nextTag();
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException {
/*  89 */     this.reader.close();
/*     */   }
/*     */ 
/*     */   public boolean hasName() {
/*  93 */     return this.reader.hasName();
/*     */   }
/*     */ 
/*     */   public boolean hasNext() throws XMLStreamException {
/*  97 */     return this.reader.hasNext();
/*     */   }
/*     */ 
/*     */   public boolean hasText() {
/* 101 */     return this.reader.hasText();
/*     */   }
/*     */ 
/*     */   public boolean isCharacters() {
/* 105 */     return this.reader.isCharacters();
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/* 109 */     return this.reader.isEndElement();
/*     */   }
/*     */ 
/*     */   public boolean isStandalone() {
/* 113 */     return this.reader.isStandalone();
/*     */   }
/*     */ 
/*     */   public boolean isStartElement() {
/* 117 */     return this.reader.isStartElement();
/*     */   }
/*     */ 
/*     */   public boolean isWhiteSpace() {
/* 121 */     return this.reader.isWhiteSpace();
/*     */   }
/*     */ 
/*     */   public boolean standaloneSet() {
/* 125 */     return this.reader.standaloneSet();
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters() {
/* 129 */     return this.reader.getTextCharacters();
/*     */   }
/*     */ 
/*     */   public boolean isAttributeSpecified(int index) {
/* 133 */     return this.reader.isAttributeSpecified(index);
/*     */   }
/*     */ 
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
/* 137 */     return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
/*     */   }
/*     */ 
/*     */   public String getCharacterEncodingScheme() {
/* 141 */     return this.reader.getCharacterEncodingScheme();
/*     */   }
/*     */ 
/*     */   public String getElementText() throws XMLStreamException {
/* 145 */     return this.reader.getElementText();
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 149 */     return this.reader.getEncoding();
/*     */   }
/*     */ 
/*     */   public String getLocalName() {
/* 153 */     return this.reader.getLocalName();
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI() {
/* 157 */     return this.reader.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getPIData() {
/* 161 */     return this.reader.getPIData();
/*     */   }
/*     */ 
/*     */   public String getPITarget() {
/* 165 */     return this.reader.getPITarget();
/*     */   }
/*     */ 
/*     */   public String getPrefix() {
/* 169 */     return this.reader.getPrefix();
/*     */   }
/*     */ 
/*     */   public String getText() {
/* 173 */     return this.reader.getText();
/*     */   }
/*     */ 
/*     */   public String getVersion() {
/* 177 */     return this.reader.getVersion();
/*     */   }
/*     */ 
/*     */   public String getAttributeLocalName(int index) {
/* 181 */     return this.reader.getAttributeLocalName(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeNamespace(int index) {
/* 185 */     return this.reader.getAttributeNamespace(index);
/*     */   }
/*     */ 
/*     */   public String getAttributePrefix(int index) {
/* 189 */     return this.reader.getAttributePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeType(int index) {
/* 193 */     return this.reader.getAttributeType(index);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(int index) {
/* 197 */     return this.reader.getAttributeValue(index);
/*     */   }
/*     */ 
/*     */   public String getNamespacePrefix(int index) {
/* 201 */     return this.reader.getNamespacePrefix(index);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int index) {
/* 205 */     return this.reader.getNamespaceURI(index);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 209 */     return this.reader.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 213 */     return this.reader.getName();
/*     */   }
/*     */ 
/*     */   public QName getAttributeName(int index) {
/* 217 */     return this.reader.getAttributeName(index);
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 221 */     return this.reader.getLocation();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 225 */     return this.reader.getProperty(name);
/*     */   }
/*     */ 
/*     */   public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
/* 229 */     this.reader.require(type, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix) {
/* 233 */     return this.reader.getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(String namespaceURI, String localName) {
/* 237 */     return this.reader.getAttributeValue(namespaceURI, localName);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter
 * JD-Core Version:    0.6.2
 */