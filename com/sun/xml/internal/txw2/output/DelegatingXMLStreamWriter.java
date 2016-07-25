/*     */ package com.sun.xml.internal.txw2.output;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ abstract class DelegatingXMLStreamWriter
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private final XMLStreamWriter writer;
/*     */ 
/*     */   public DelegatingXMLStreamWriter(XMLStreamWriter writer)
/*     */   {
/*  41 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/*  45 */     this.writer.writeStartElement(localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/*  49 */     this.writer.writeStartElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/*  53 */     this.writer.writeStartElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/*  57 */     this.writer.writeEmptyElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/*  61 */     this.writer.writeEmptyElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/*  65 */     this.writer.writeEmptyElement(localName);
/*     */   }
/*     */ 
/*     */   public void writeEndElement() throws XMLStreamException {
/*  69 */     this.writer.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeEndDocument() throws XMLStreamException {
/*  73 */     this.writer.writeEndDocument();
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException {
/*  77 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void flush() throws XMLStreamException {
/*  81 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException {
/*  85 */     this.writer.writeAttribute(localName, value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/*  89 */     this.writer.writeAttribute(prefix, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/*  93 */     this.writer.writeAttribute(namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/*  97 */     this.writer.writeNamespace(prefix, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/* 101 */     this.writer.writeDefaultNamespace(namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeComment(String data) throws XMLStreamException {
/* 105 */     this.writer.writeComment(data);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/* 109 */     this.writer.writeProcessingInstruction(target);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/* 113 */     this.writer.writeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 117 */     this.writer.writeCData(data);
/*     */   }
/*     */ 
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/* 121 */     this.writer.writeDTD(dtd);
/*     */   }
/*     */ 
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 125 */     this.writer.writeEntityRef(name);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument() throws XMLStreamException {
/* 129 */     this.writer.writeStartDocument();
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 133 */     this.writer.writeStartDocument(version);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 137 */     this.writer.writeStartDocument(encoding, version);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 141 */     this.writer.writeCharacters(text);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 145 */     this.writer.writeCharacters(text, start, len);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/* 149 */     return this.writer.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException {
/* 153 */     this.writer.setPrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/* 157 */     this.writer.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
/* 161 */     this.writer.setNamespaceContext(context);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 165 */     return this.writer.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 169 */     return this.writer.getProperty(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.DelegatingXMLStreamWriter
 * JD-Core Version:    0.6.2
 */