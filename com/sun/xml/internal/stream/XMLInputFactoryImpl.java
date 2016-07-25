/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.PropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLStreamFilterImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
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
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ public class XMLInputFactoryImpl extends XMLInputFactory
/*     */ {
/*  51 */   private PropertyManager fPropertyManager = new PropertyManager(1);
/*     */   private static final boolean DEBUG = false;
/*  55 */   private XMLStreamReaderImpl fTempReader = null;
/*     */ 
/*  57 */   boolean fPropertyChanged = false;
/*     */ 
/*  59 */   boolean fReuseInstance = false;
/*     */ 
/*     */   void initEventReader()
/*     */   {
/*  67 */     this.fPropertyChanged = true;
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(InputStream inputstream)
/*     */     throws XMLStreamException
/*     */   {
/*  76 */     initEventReader();
/*     */ 
/*  78 */     return new XMLEventReaderImpl(createXMLStreamReader(inputstream));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(Reader reader) throws XMLStreamException {
/*  82 */     initEventReader();
/*     */ 
/*  84 */     return new XMLEventReaderImpl(createXMLStreamReader(reader));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(Source source) throws XMLStreamException {
/*  88 */     initEventReader();
/*     */ 
/*  90 */     return new XMLEventReaderImpl(createXMLStreamReader(source));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(String systemId, InputStream inputstream) throws XMLStreamException {
/*  94 */     initEventReader();
/*     */ 
/*  96 */     return new XMLEventReaderImpl(createXMLStreamReader(systemId, inputstream));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(InputStream stream, String encoding) throws XMLStreamException {
/* 100 */     initEventReader();
/*     */ 
/* 102 */     return new XMLEventReaderImpl(createXMLStreamReader(stream, encoding));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(String systemId, Reader reader) throws XMLStreamException {
/* 106 */     initEventReader();
/*     */ 
/* 108 */     return new XMLEventReaderImpl(createXMLStreamReader(systemId, reader));
/*     */   }
/*     */ 
/*     */   public XMLEventReader createXMLEventReader(XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/* 124 */     return new XMLEventReaderImpl(reader);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(InputStream inputstream) throws XMLStreamException {
/* 128 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, inputstream, null);
/* 129 */     return getXMLStreamReaderImpl(inputSource);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(Reader reader) throws XMLStreamException {
/* 133 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, reader, null);
/* 134 */     return getXMLStreamReaderImpl(inputSource);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(String systemId, Reader reader) throws XMLStreamException {
/* 138 */     XMLInputSource inputSource = new XMLInputSource(null, systemId, null, reader, null);
/* 139 */     return getXMLStreamReaderImpl(inputSource);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(Source source) throws XMLStreamException {
/* 143 */     return new XMLStreamReaderImpl(jaxpSourcetoXMLInputSource(source), new PropertyManager(this.fPropertyManager));
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(String systemId, InputStream inputstream) throws XMLStreamException
/*     */   {
/* 148 */     XMLInputSource inputSource = new XMLInputSource(null, systemId, null, inputstream, null);
/* 149 */     return getXMLStreamReaderImpl(inputSource);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createXMLStreamReader(InputStream inputstream, String encoding) throws XMLStreamException
/*     */   {
/* 154 */     XMLInputSource inputSource = new XMLInputSource(null, null, null, inputstream, encoding);
/* 155 */     return getXMLStreamReaderImpl(inputSource);
/*     */   }
/*     */ 
/*     */   public XMLEventAllocator getEventAllocator() {
/* 159 */     return (XMLEventAllocator)getProperty("javax.xml.stream.allocator");
/*     */   }
/*     */ 
/*     */   public XMLReporter getXMLReporter() {
/* 163 */     return (XMLReporter)this.fPropertyManager.getProperty("javax.xml.stream.reporter");
/*     */   }
/*     */ 
/*     */   public XMLResolver getXMLResolver() {
/* 167 */     Object object = this.fPropertyManager.getProperty("javax.xml.stream.resolver");
/* 168 */     return (XMLResolver)object;
/*     */   }
/*     */ 
/*     */   public void setXMLReporter(XMLReporter xmlreporter)
/*     */   {
/* 173 */     this.fPropertyManager.setProperty("javax.xml.stream.reporter", xmlreporter);
/*     */   }
/*     */ 
/*     */   public void setXMLResolver(XMLResolver xmlresolver) {
/* 177 */     this.fPropertyManager.setProperty("javax.xml.stream.resolver", xmlresolver);
/*     */   }
/*     */ 
/*     */   public XMLEventReader createFilteredReader(XMLEventReader reader, EventFilter filter)
/*     */     throws XMLStreamException
/*     */   {
/* 186 */     return new EventFilterSupport(reader, filter);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader createFilteredReader(XMLStreamReader reader, StreamFilter filter)
/*     */     throws XMLStreamException
/*     */   {
/* 195 */     if ((reader != null) && (filter != null)) {
/* 196 */       return new XMLStreamFilterImpl(reader, filter);
/*     */     }
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 209 */     if (name == null) {
/* 210 */       throw new IllegalArgumentException("Property not supported");
/*     */     }
/* 212 */     if (this.fPropertyManager.containsProperty(name))
/* 213 */       return this.fPropertyManager.getProperty(name);
/* 214 */     throw new IllegalArgumentException("Property not supported");
/*     */   }
/*     */ 
/*     */   public boolean isPropertySupported(String name)
/*     */   {
/* 223 */     if (name == null) {
/* 224 */       return false;
/*     */     }
/* 226 */     return this.fPropertyManager.containsProperty(name);
/*     */   }
/*     */ 
/*     */   public void setEventAllocator(XMLEventAllocator allocator)
/*     */   {
/* 233 */     this.fPropertyManager.setProperty("javax.xml.stream.allocator", allocator);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws IllegalArgumentException
/*     */   {
/* 245 */     if ((name == null) || (value == null) || (!this.fPropertyManager.containsProperty(name))) {
/* 246 */       throw new IllegalArgumentException("Property " + name + " is not supported");
/*     */     }
/* 248 */     if ((name == "reuse-instance") || (name.equals("reuse-instance"))) {
/* 249 */       this.fReuseInstance = ((Boolean)value).booleanValue();
/*     */     }
/*     */     else
/*     */     {
/* 253 */       this.fPropertyChanged = true;
/*     */     }
/* 255 */     this.fPropertyManager.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   XMLStreamReader getXMLStreamReaderImpl(XMLInputSource inputSource) throws XMLStreamException
/*     */   {
/* 260 */     if (this.fTempReader == null) {
/* 261 */       this.fPropertyChanged = false;
/* 262 */       return this.fTempReader = new XMLStreamReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
/*     */     }
/*     */ 
/* 267 */     if ((this.fReuseInstance) && (this.fTempReader.canReuse()) && (!this.fPropertyChanged))
/*     */     {
/* 270 */       this.fTempReader.reset();
/* 271 */       this.fTempReader.setInputSource(inputSource);
/* 272 */       this.fPropertyChanged = false;
/* 273 */       return this.fTempReader;
/*     */     }
/* 275 */     this.fPropertyChanged = false;
/*     */ 
/* 277 */     return this.fTempReader = new XMLStreamReaderImpl(inputSource, new PropertyManager(this.fPropertyManager));
/*     */   }
/*     */ 
/*     */   XMLInputSource jaxpSourcetoXMLInputSource(Source source)
/*     */   {
/* 283 */     if ((source instanceof StreamSource)) {
/* 284 */       StreamSource stSource = (StreamSource)source;
/* 285 */       String systemId = stSource.getSystemId();
/* 286 */       String publicId = stSource.getPublicId();
/* 287 */       InputStream istream = stSource.getInputStream();
/* 288 */       Reader reader = stSource.getReader();
/*     */ 
/* 290 */       if (istream != null) {
/* 291 */         return new XMLInputSource(publicId, systemId, null, istream, null);
/*     */       }
/* 293 */       if (reader != null) {
/* 294 */         return new XMLInputSource(publicId, systemId, null, reader, null);
/*     */       }
/* 296 */       return new XMLInputSource(publicId, systemId, null);
/*     */     }
/*     */ 
/* 300 */     throw new UnsupportedOperationException("Cannot create XMLStreamReader or XMLEventReader from a " + source.getClass().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.XMLInputFactoryImpl
 * JD-Core Version:    0.6.2
 */