/*     */ package org.xml.sax.helpers;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.DocumentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class XMLReaderAdapter
/*     */   implements Parser, ContentHandler
/*     */ {
/*     */   XMLReader xmlReader;
/*     */   DocumentHandler documentHandler;
/*     */   AttributesAdapter qAtts;
/*     */ 
/*     */   public XMLReaderAdapter()
/*     */     throws SAXException
/*     */   {
/*  98 */     setup(XMLReaderFactory.createXMLReader());
/*     */   }
/*     */ 
/*     */   public XMLReaderAdapter(XMLReader xmlReader)
/*     */   {
/* 114 */     setup(xmlReader);
/*     */   }
/*     */ 
/*     */   private void setup(XMLReader xmlReader)
/*     */   {
/* 126 */     if (xmlReader == null) {
/* 127 */       throw new NullPointerException("XMLReader must not be null");
/*     */     }
/* 129 */     this.xmlReader = xmlReader;
/* 130 */     this.qAtts = new AttributesAdapter();
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */     throws SAXException
/*     */   {
/* 153 */     throw new SAXNotSupportedException("setLocale not supported");
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */   {
/* 165 */     this.xmlReader.setEntityResolver(resolver);
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */   {
/* 177 */     this.xmlReader.setDTDHandler(handler);
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(DocumentHandler handler)
/*     */   {
/* 192 */     this.documentHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */   {
/* 204 */     this.xmlReader.setErrorHandler(handler);
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws IOException, SAXException
/*     */   {
/* 226 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input)
/*     */     throws IOException, SAXException
/*     */   {
/* 248 */     setupXMLReader();
/* 249 */     this.xmlReader.parse(input);
/*     */   }
/*     */ 
/*     */   private void setupXMLReader()
/*     */     throws SAXException
/*     */   {
/* 259 */     this.xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*     */     try {
/* 261 */       this.xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/* 266 */     this.xmlReader.setContentHandler(this);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 284 */     if (this.documentHandler != null)
/* 285 */       this.documentHandler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 299 */     if (this.documentHandler != null)
/* 300 */       this.documentHandler.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 314 */     if (this.documentHandler != null)
/* 315 */       this.documentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 357 */     if (this.documentHandler != null) {
/* 358 */       this.qAtts.setAttributes(atts);
/* 359 */       this.documentHandler.startElement(qName, this.qAtts);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 378 */     if (this.documentHandler != null)
/* 379 */       this.documentHandler.endElement(qName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 396 */     if (this.documentHandler != null)
/* 397 */       this.documentHandler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 414 */     if (this.documentHandler != null)
/* 415 */       this.documentHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 431 */     if (this.documentHandler != null)
/* 432 */       this.documentHandler.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   final class AttributesAdapter
/*     */     implements AttributeList
/*     */   {
/*     */     private Attributes attributes;
/*     */ 
/*     */     AttributesAdapter()
/*     */     {
/*     */     }
/*     */ 
/*     */     void setAttributes(Attributes attributes)
/*     */     {
/* 482 */       this.attributes = attributes;
/*     */     }
/*     */ 
/*     */     public int getLength()
/*     */     {
/* 494 */       return this.attributes.getLength();
/*     */     }
/*     */ 
/*     */     public String getName(int i)
/*     */     {
/* 506 */       return this.attributes.getQName(i);
/*     */     }
/*     */ 
/*     */     public String getType(int i)
/*     */     {
/* 518 */       return this.attributes.getType(i);
/*     */     }
/*     */ 
/*     */     public String getValue(int i)
/*     */     {
/* 530 */       return this.attributes.getValue(i);
/*     */     }
/*     */ 
/*     */     public String getType(String qName)
/*     */     {
/* 542 */       return this.attributes.getType(qName);
/*     */     }
/*     */ 
/*     */     public String getValue(String qName)
/*     */     {
/* 554 */       return this.attributes.getValue(qName);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.XMLReaderAdapter
 * JD-Core Version:    0.6.2
 */