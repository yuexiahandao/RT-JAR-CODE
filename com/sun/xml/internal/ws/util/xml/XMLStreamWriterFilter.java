/*     */ package com.sun.xml.internal.ws.util.xml;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.RecycleAware;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class XMLStreamWriterFilter
/*     */   implements XMLStreamWriter, XMLStreamWriterFactory.RecycleAware
/*     */ {
/*     */   protected XMLStreamWriter writer;
/*     */ 
/*     */   public XMLStreamWriterFilter(XMLStreamWriter writer)
/*     */   {
/*  48 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException {
/*  52 */     this.writer.close();
/*     */   }
/*     */ 
/*     */   public void flush() throws XMLStreamException {
/*  56 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   public void writeEndDocument() throws XMLStreamException {
/*  60 */     this.writer.writeEndDocument();
/*     */   }
/*     */ 
/*     */   public void writeEndElement() throws XMLStreamException {
/*  64 */     this.writer.writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeStartDocument() throws XMLStreamException {
/*  68 */     this.writer.writeStartDocument();
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/*  72 */     this.writer.writeCharacters(text, start, len);
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/*  76 */     this.writer.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   public void writeCData(String data) throws XMLStreamException {
/*  80 */     this.writer.writeCData(data);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/*  84 */     this.writer.writeCharacters(text);
/*     */   }
/*     */ 
/*     */   public void writeComment(String data) throws XMLStreamException {
/*  88 */     this.writer.writeComment(data);
/*     */   }
/*     */ 
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/*  92 */     this.writer.writeDTD(dtd);
/*     */   }
/*     */ 
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/*  96 */     this.writer.writeDefaultNamespace(namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 100 */     this.writer.writeEmptyElement(localName);
/*     */   }
/*     */ 
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 104 */     this.writer.writeEntityRef(name);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/* 108 */     this.writer.writeProcessingInstruction(target);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 112 */     this.writer.writeStartDocument(version);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 116 */     this.writer.writeStartElement(localName);
/*     */   }
/*     */ 
/*     */   public NamespaceContext getNamespaceContext() {
/* 120 */     return this.writer.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
/* 124 */     this.writer.setNamespaceContext(context);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/* 128 */     return this.writer.getProperty(name);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/* 132 */     return this.writer.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException {
/* 136 */     this.writer.setPrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException {
/* 140 */     this.writer.writeAttribute(localName, value);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 144 */     this.writer.writeEmptyElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/* 148 */     this.writer.writeNamespace(prefix, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/* 152 */     this.writer.writeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 156 */     this.writer.writeStartDocument(encoding, version);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 160 */     this.writer.writeStartElement(namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/* 164 */     this.writer.writeAttribute(namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 168 */     this.writer.writeEmptyElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 172 */     this.writer.writeStartElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/* 176 */     this.writer.writeAttribute(prefix, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void onRecycled() {
/* 180 */     XMLStreamWriterFactory.recycle(this.writer);
/* 181 */     this.writer = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter
 * JD-Core Version:    0.6.2
 */