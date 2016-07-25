/*     */ package com.sun.xml.internal.stream.buffer.sax;
/*     */ 
/*     */ import com.sun.xml.internal.stream.buffer.AbstractCreator;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class SAXBufferCreator extends AbstractCreator
/*     */   implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, LexicalHandler
/*     */ {
/*     */   protected String[] _namespaceAttributes;
/*     */   protected int _namespaceAttributesPtr;
/*  57 */   private int depth = 0;
/*     */ 
/*     */   public SAXBufferCreator() {
/*  60 */     this._namespaceAttributes = new String[32];
/*     */   }
/*     */ 
/*     */   public SAXBufferCreator(MutableXMLStreamBuffer buffer) {
/*  64 */     this();
/*  65 */     setBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer create(XMLReader reader, InputStream in) throws IOException, SAXException {
/*  69 */     return create(reader, in, null);
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer create(XMLReader reader, InputStream in, String systemId) throws IOException, SAXException {
/*  73 */     if (this._buffer == null) {
/*  74 */       createBuffer();
/*     */     }
/*  76 */     this._buffer.setSystemId(systemId);
/*  77 */     reader.setContentHandler(this);
/*  78 */     reader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
/*     */     try
/*     */     {
/*  81 */       setHasInternedStrings(reader.getFeature("http://xml.org/sax/features/string-interning"));
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*  86 */     if (systemId != null) {
/*  87 */       InputSource s = new InputSource(systemId);
/*  88 */       s.setByteStream(in);
/*  89 */       reader.parse(s);
/*     */     } else {
/*  91 */       reader.parse(new InputSource(in));
/*     */     }
/*     */ 
/*  94 */     return getXMLStreamBuffer();
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  98 */     this._buffer = null;
/*  99 */     this._namespaceAttributesPtr = 0;
/* 100 */     this.depth = 0;
/*     */   }
/*     */ 
/*     */   public void startDocument() throws SAXException {
/* 104 */     storeStructure(16);
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/* 108 */     storeStructure(144);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri) throws SAXException {
/* 112 */     cacheNamespaceAttribute(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 116 */     storeQualifiedName(32, uri, localName, qName);
/*     */ 
/* 120 */     if (this._namespaceAttributesPtr > 0) {
/* 121 */       storeNamespaceAttributes();
/*     */     }
/*     */ 
/* 125 */     if (attributes.getLength() > 0) {
/* 126 */       storeAttributes(attributes);
/*     */     }
/* 128 */     this.depth += 1;
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 132 */     storeStructure(144);
/* 133 */     if (--this.depth == 0)
/* 134 */       increaseTreeCount();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 138 */     storeContentCharacters(80, ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 142 */     characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException {
/* 146 */     storeStructure(112);
/* 147 */     storeStructureString(target);
/* 148 */     storeStructureString(data);
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 152 */     storeContentCharacters(96, ch, start, length);
/*     */   }
/*     */ 
/*     */   private void cacheNamespaceAttribute(String prefix, String uri)
/*     */   {
/* 158 */     this._namespaceAttributes[(this._namespaceAttributesPtr++)] = prefix;
/* 159 */     this._namespaceAttributes[(this._namespaceAttributesPtr++)] = uri;
/*     */ 
/* 161 */     if (this._namespaceAttributesPtr == this._namespaceAttributes.length) {
/* 162 */       String[] namespaceAttributes = new String[this._namespaceAttributesPtr * 2];
/* 163 */       System.arraycopy(this._namespaceAttributes, 0, namespaceAttributes, 0, this._namespaceAttributesPtr);
/* 164 */       this._namespaceAttributes = namespaceAttributes;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void storeNamespaceAttributes() {
/* 169 */     for (int i = 0; i < this._namespaceAttributesPtr; i += 2) {
/* 170 */       int item = 64;
/* 171 */       if (this._namespaceAttributes[i].length() > 0) {
/* 172 */         item |= 1;
/* 173 */         storeStructureString(this._namespaceAttributes[i]);
/*     */       }
/* 175 */       if (this._namespaceAttributes[(i + 1)].length() > 0) {
/* 176 */         item |= 2;
/* 177 */         storeStructureString(this._namespaceAttributes[(i + 1)]);
/*     */       }
/* 179 */       storeStructure(item);
/*     */     }
/* 181 */     this._namespaceAttributesPtr = 0;
/*     */   }
/*     */ 
/*     */   private void storeAttributes(Attributes attributes) {
/* 185 */     for (int i = 0; i < attributes.getLength(); i++)
/*     */     {
/* 188 */       if (!attributes.getQName(i).startsWith("xmlns"))
/*     */       {
/* 190 */         storeQualifiedName(48, attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i));
/*     */ 
/* 195 */         storeStructureString(attributes.getType(i));
/* 196 */         storeContentString(attributes.getValue(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 201 */   private void storeQualifiedName(int item, String uri, String localName, String qName) { if (uri.length() > 0) {
/* 202 */       item |= 2;
/* 203 */       storeStructureString(uri);
/*     */     }
/*     */ 
/* 206 */     storeStructureString(localName);
/*     */ 
/* 208 */     if (qName.indexOf(':') >= 0) {
/* 209 */       item |= 4;
/* 210 */       storeStructureString(qName);
/*     */     }
/*     */ 
/* 213 */     storeStructure(item);
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws IOException, SAXException
/*     */   {
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId) throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator) {
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name) throws SAXException {
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
/*     */   public void startCDATA() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e) throws SAXException {
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e) throws SAXException {
/* 268 */     throw e;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator
 * JD-Core Version:    0.6.2
 */