/*     */ package com.sun.xml.internal.fastinfoset.tools;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class SAX2StAXWriter extends DefaultHandler
/*     */   implements LexicalHandler
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(SAX2StAXWriter.class.getName());
/*     */   XMLStreamWriter _writer;
/*  53 */   ArrayList _namespaces = new ArrayList();
/*     */ 
/*     */   public SAX2StAXWriter(XMLStreamWriter writer) {
/*  56 */     this._writer = writer;
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter getWriter() {
/*  60 */     return this._writer;
/*     */   }
/*     */ 
/*     */   public void startDocument() throws SAXException {
/*     */     try {
/*  65 */       this._writer.writeStartDocument();
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  68 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument() throws SAXException {
/*     */     try {
/*  74 */       this._writer.writeEndDocument();
/*  75 */       this._writer.flush();
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  78 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       this._writer.writeCharacters(ch, start, length);
/*     */     }
/*     */     catch (XMLStreamException e) {
/*  89 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
/*     */   {
/*     */     try
/*     */     {
/*  97 */       int k = qName.indexOf(':');
/*  98 */       String prefix = k > 0 ? qName.substring(0, k) : "";
/*  99 */       this._writer.writeStartElement(prefix, localName, namespaceURI);
/*     */ 
/* 101 */       int length = this._namespaces.size();
/* 102 */       for (int i = 0; i < length; i++) {
/* 103 */         QualifiedName nsh = (QualifiedName)this._namespaces.get(i);
/* 104 */         this._writer.writeNamespace(nsh.prefix, nsh.namespaceName);
/*     */       }
/* 106 */       this._namespaces.clear();
/*     */ 
/* 108 */       length = atts.getLength();
/* 109 */       for (int i = 0; i < length; i++) {
/* 110 */         this._writer.writeAttribute(atts.getURI(i), atts.getLocalName(i), atts.getValue(i));
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 116 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 124 */       this._writer.writeEndElement();
/*     */     }
/*     */     catch (XMLStreamException e) {
/* 127 */       logger.log(Level.FINE, "Exception on endElement", e);
/* 128 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 139 */     this._namespaces.add(new QualifiedName(prefix, uri));
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 152 */     characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data) throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 159 */       this._writer.writeProcessingInstruction(target, data);
/*     */     }
/*     */     catch (XMLStreamException e) {
/* 162 */       throw new SAXException(e);
/*     */     }
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
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 176 */       this._writer.writeComment(new String(ch, start, length));
/*     */     }
/*     */     catch (XMLStreamException e) {
/* 179 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDTD()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endEntity(String name)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startCDATA()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startEntity(String name)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.SAX2StAXWriter
 * JD-Core Version:    0.6.2
 */