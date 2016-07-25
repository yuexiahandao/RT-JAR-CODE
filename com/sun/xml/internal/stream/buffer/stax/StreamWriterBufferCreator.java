/*     */ package com.sun.xml.internal.stream.buffer.stax;
/*     */ 
/*     */ import com.sun.xml.internal.org.jvnet.staxex.Base64Data;
/*     */ import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
/*     */ import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ 
/*     */ public class StreamWriterBufferCreator extends StreamBufferCreator
/*     */   implements XMLStreamWriterEx
/*     */ {
/*  49 */   private final NamespaceContexHelper namespaceContext = new NamespaceContexHelper();
/*     */ 
/*  56 */   private int depth = 0;
/*     */ 
/*     */   public StreamWriterBufferCreator() {
/*  59 */     setXMLStreamBuffer(new MutableXMLStreamBuffer());
/*     */   }
/*     */ 
/*     */   public StreamWriterBufferCreator(MutableXMLStreamBuffer buffer) {
/*  63 */     setXMLStreamBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String str)
/*     */     throws IllegalArgumentException
/*     */   {
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   public void close() throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void flush() throws XMLStreamException {
/*     */   }
/*     */ 
/*     */   public NamespaceContextEx getNamespaceContext() {
/*  80 */     return this.namespaceContext;
/*     */   }
/*     */ 
/*     */   public void setNamespaceContext(NamespaceContext namespaceContext)
/*     */     throws XMLStreamException
/*     */   {
/*  87 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setDefaultNamespace(String namespaceURI) throws XMLStreamException {
/*  91 */     setPrefix("", namespaceURI);
/*     */   }
/*     */ 
/*     */   public void setPrefix(String prefix, String namespaceURI) throws XMLStreamException {
/*  95 */     this.namespaceContext.declareNamespace(prefix, namespaceURI);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String namespaceURI) throws XMLStreamException {
/*  99 */     return this.namespaceContext.getPrefix(namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeStartDocument() throws XMLStreamException
/*     */   {
/* 104 */     writeStartDocument("", "");
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/* 108 */     writeStartDocument("", "");
/*     */   }
/*     */ 
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 112 */     this.namespaceContext.resetContexts();
/*     */ 
/* 114 */     storeStructure(16);
/*     */   }
/*     */ 
/*     */   public void writeEndDocument() throws XMLStreamException {
/* 118 */     storeStructure(144);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 122 */     this.namespaceContext.pushContext();
/* 123 */     this.depth += 1;
/*     */ 
/* 125 */     String defaultNamespaceURI = this.namespaceContext.getNamespaceURI("");
/*     */ 
/* 127 */     if (defaultNamespaceURI == null)
/* 128 */       storeQualifiedName(32, null, null, localName);
/*     */     else
/* 130 */       storeQualifiedName(32, null, defaultNamespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 134 */     this.namespaceContext.pushContext();
/* 135 */     this.depth += 1;
/*     */ 
/* 137 */     String prefix = this.namespaceContext.getPrefix(namespaceURI);
/* 138 */     if (prefix == null) {
/* 139 */       throw new XMLStreamException();
/*     */     }
/*     */ 
/* 142 */     this.namespaceContext.pushContext();
/* 143 */     storeQualifiedName(32, prefix, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 147 */     this.namespaceContext.pushContext();
/* 148 */     this.depth += 1;
/*     */ 
/* 150 */     storeQualifiedName(32, prefix, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 154 */     writeStartElement(localName);
/* 155 */     writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 159 */     writeStartElement(namespaceURI, localName);
/* 160 */     writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 164 */     writeStartElement(prefix, localName, namespaceURI);
/* 165 */     writeEndElement();
/*     */   }
/*     */ 
/*     */   public void writeEndElement() throws XMLStreamException {
/* 169 */     this.namespaceContext.popContext();
/*     */ 
/* 171 */     storeStructure(144);
/* 172 */     if (--this.depth == 0)
/* 173 */       increaseTreeCount();
/*     */   }
/*     */ 
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/* 177 */     storeNamespaceAttribute(null, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/* 181 */     if ("xmlns".equals(prefix))
/* 182 */       prefix = null;
/* 183 */     storeNamespaceAttribute(prefix, namespaceURI);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException
/*     */   {
/* 188 */     storeAttribute(null, null, localName, "CDATA", value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/* 192 */     String prefix = this.namespaceContext.getPrefix(namespaceURI);
/* 193 */     if (prefix == null)
/*     */     {
/* 195 */       throw new XMLStreamException();
/*     */     }
/*     */ 
/* 198 */     writeAttribute(prefix, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/* 202 */     storeAttribute(prefix, namespaceURI, localName, "CDATA", value);
/*     */   }
/*     */ 
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 206 */     storeStructure(88);
/* 207 */     storeContentString(data);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(String charData) throws XMLStreamException {
/* 211 */     storeStructure(88);
/* 212 */     storeContentString(charData);
/*     */   }
/*     */ 
/*     */   public void writeCharacters(char[] buf, int start, int len) throws XMLStreamException {
/* 216 */     storeContentCharacters(80, buf, start, len);
/*     */   }
/*     */ 
/*     */   public void writeComment(String str) throws XMLStreamException {
/* 220 */     storeStructure(104);
/* 221 */     storeContentString(str);
/*     */   }
/*     */ 
/*     */   public void writeDTD(String str) throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeEntityRef(String str) throws XMLStreamException {
/* 229 */     storeStructure(128);
/* 230 */     storeContentString(str);
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/* 234 */     writeProcessingInstruction(target, "");
/*     */   }
/*     */ 
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/* 238 */     storeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void writePCDATA(CharSequence charSequence)
/*     */     throws XMLStreamException
/*     */   {
/* 244 */     if ((charSequence instanceof Base64Data)) {
/* 245 */       storeStructure(92);
/* 246 */       storeContentObject(((Base64Data)charSequence).clone());
/*     */     } else {
/* 248 */       writeCharacters(charSequence.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeBinary(byte[] bytes, int offset, int length, String endpointURL) throws XMLStreamException {
/* 253 */     Base64Data d = new Base64Data();
/* 254 */     byte[] b = new byte[length];
/* 255 */     System.arraycopy(bytes, offset, b, 0, length);
/* 256 */     d.set(b, length, null, true);
/* 257 */     storeStructure(92);
/* 258 */     storeContentObject(d);
/*     */   }
/*     */ 
/*     */   public void writeBinary(DataHandler dataHandler) throws XMLStreamException {
/* 262 */     Base64Data d = new Base64Data();
/* 263 */     d.set(dataHandler);
/* 264 */     storeStructure(92);
/* 265 */     storeContentObject(d);
/*     */   }
/*     */ 
/*     */   public OutputStream writeBinary(String endpointURL) throws XMLStreamException
/*     */   {
/* 270 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator
 * JD-Core Version:    0.6.2
 */