/*     */ package com.sun.xml.internal.fastinfoset.stax.factory;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*     */ import com.sun.xml.internal.fastinfoset.stax.StAXManager;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.StAXEventReader;
/*     */ import com.sun.xml.internal.fastinfoset.stax.events.StAXFilteredEvent;
/*     */ import com.sun.xml.internal.fastinfoset.stax.util.StAXFilteredParser;
/*     */ import com.sun.xml.internal.fastinfoset.tools.XML_SAX_FI;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import javax.xml.stream.EventFilter;
/*     */ import javax.xml.stream.StreamFilter;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLReporter;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public class StAXInputFactory extends XMLInputFactory
/*     */ {
/*  51 */   private StAXManager _manager = new StAXManager(1);
/*     */ 
/*     */   public static XMLInputFactory newInstance()
/*     */   {
/*  57 */     return XMLInputFactory.newInstance();
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(Reader xmlfile)
/*     */     throws XMLStreamException
/*     */   {
/*  66 */     return getXMLStreamReader(xmlfile);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(InputStream s) throws XMLStreamException {
/*  70 */     return new StAXDocumentParser(s, this._manager);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(String systemId, Reader xmlfile) throws XMLStreamException {
/*  74 */     return getXMLStreamReader(xmlfile);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(String systemId, InputStream inputstream) throws XMLStreamException {
/*  82 */     return createXMLStreamReader(inputstream);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(InputStream inputstream, String encoding) throws XMLStreamException
/*     */   {
/*  87 */     return createXMLStreamReader(inputstream);
/*     */   }
/*     */ 
/*     */   XMLStreamReader getXMLStreamReader(String systemId, InputStream inputstream, String encoding)
/*     */     throws XMLStreamException
/*     */   {
/*  93 */     return createXMLStreamReader(inputstream);
/*     */   }
/*     */ 
/*     */   XMLStreamReader getXMLStreamReader(Reader xmlfile)
/*     */     throws XMLStreamException
/*     */   {
/* 105 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
/* 106 */     BufferedOutputStream bufferedStream = new BufferedOutputStream(byteStream);
/* 107 */     StAXDocumentParser sr = null;
/*     */     try {
/* 109 */       XML_SAX_FI convertor = new XML_SAX_FI();
/* 110 */       convertor.convert(xmlfile, bufferedStream);
/*     */ 
/* 112 */       ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
/* 113 */       InputStream document = new BufferedInputStream(byteInputStream);
/* 114 */       sr = new StAXDocumentParser();
/* 115 */       sr.setInputStream(document);
/* 116 */       sr.setManager(this._manager);
/* 117 */       return sr;
/*     */     } catch (Exception e) {
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(InputStream inputstream)
/*     */     throws XMLStreamException
/*     */   {
/* 132 */     return new StAXEventReader(createXMLStreamReader(inputstream));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
/* 136 */     return new StAXEventReader(createXMLStreamReader(reader));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
/* 140 */     return new StAXEventReader(createXMLStreamReader(source));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(String systemId, InputStream inputstream) throws XMLStreamException {
/* 144 */     return new StAXEventReader(createXMLStreamReader(systemId, inputstream));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
/* 148 */     return new StAXEventReader(createXMLStreamReader(stream, encoding));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
/* 152 */     return new StAXEventReader(createXMLStreamReader(systemId, reader));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(XMLStreamReader streamReader)
/*     */     throws XMLStreamException
/*     */   {
/* 163 */     return new StAXEventReader(streamReader);
/*     */   }
/*     */ 
/*     */   public XMLEventAllocator getEventAllocator() {
/* 167 */     return (XMLEventAllocator)getProperty("javax.xml.stream.allocator");
/*     */   }
/*     */ 
/*     */   public XMLReporter getXMLReporter() {
/* 171 */     return (XMLReporter)this._manager.getProperty("javax.xml.stream.reporter");
/*     */   }
/*     */ 
/*     */   public XMLResolver getXMLResolver() {
/* 175 */     Object object = this._manager.getProperty("javax.xml.stream.resolver");
/* 176 */     return (XMLResolver)object;
/*     */   }
/*     */ 
/*     */   public void setXMLReporter(XMLReporter xmlreporter)
/*     */   {
/* 181 */     this._manager.setProperty("javax.xml.stream.reporter", xmlreporter);
/*     */   }
/*     */ 
/*     */   public void setXMLResolver(XMLResolver xmlresolver) {
/* 185 */     this._manager.setProperty("javax.xml.stream.resolver", xmlresolver);
/*     */   }
/*     */ 
/*     */   public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter)
/*     */     throws XMLStreamException
/*     */   {
/* 194 */     return new StAXFilteredEvent(reader, filter);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter)
/*     */     throws XMLStreamException
/*     */   {
/* 204 */     if ((reader != null) && (filter != null)) {
/* 205 */       return new StAXFilteredParser(reader, filter);
/*     */     }
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 218 */     if (name == null) {
/* 219 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.nullPropertyName"));
/*     */     }
/* 221 */     if (this._manager.containsProperty(name))
/* 222 */       return this._manager.getProperty(name);
/* 223 */     throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[] { name }));
/*     */   }
/*     */ 
/*     */   public boolean isPropertySupported(String name)
/*     */   {
/* 232 */     if (name == null) {
/* 233 */       return false;
/*     */     }
/* 235 */     return this._manager.containsProperty(name);
/*     */   }
/*     */ 
/*     */   public void setEventAllocator(XMLEventAllocator allocator)
/*     */   {
/* 242 */     this._manager.setProperty("javax.xml.stream.allocator", allocator);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws IllegalArgumentException
/*     */   {
/* 253 */     this._manager.setProperty(name, value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.factory.StAXInputFactory
 * JD-Core Version:    0.6.2
 */