/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class StAXStreamConnector extends StAXConnector
/*     */ {
/*     */   private final XMLStreamReader staxStreamReader;
/* 125 */   protected final StringBuilder buffer = new StringBuilder();
/*     */ 
/* 131 */   protected boolean textReported = false;
/*     */ 
/* 237 */   private final Attributes attributes = new Attributes() {
/*     */     public int getLength() {
/* 239 */       return StAXStreamConnector.this.staxStreamReader.getAttributeCount();
/*     */     }
/*     */ 
/*     */     public String getURI(int index) {
/* 243 */       String uri = StAXStreamConnector.this.staxStreamReader.getAttributeNamespace(index);
/* 244 */       if (uri == null) return "";
/* 245 */       return uri;
/*     */     }
/*     */ 
/*     */     public String getLocalName(int index) {
/* 249 */       return StAXStreamConnector.this.staxStreamReader.getAttributeLocalName(index);
/*     */     }
/*     */ 
/*     */     public String getQName(int index) {
/* 253 */       String prefix = StAXStreamConnector.this.staxStreamReader.getAttributePrefix(index);
/* 254 */       if ((prefix == null) || (prefix.length() == 0)) {
/* 255 */         return getLocalName(index);
/*     */       }
/* 257 */       return prefix + ':' + getLocalName(index);
/*     */     }
/*     */ 
/*     */     public String getType(int index) {
/* 261 */       return StAXStreamConnector.this.staxStreamReader.getAttributeType(index);
/*     */     }
/*     */ 
/*     */     public String getValue(int index) {
/* 265 */       return StAXStreamConnector.this.staxStreamReader.getAttributeValue(index);
/*     */     }
/*     */ 
/*     */     public int getIndex(String uri, String localName) {
/* 269 */       for (int i = getLength() - 1; i >= 0; i--)
/* 270 */         if ((localName.equals(getLocalName(i))) && (uri.equals(getURI(i))))
/* 271 */           return i;
/* 272 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getIndex(String qName)
/*     */     {
/* 278 */       for (int i = getLength() - 1; i >= 0; i--) {
/* 279 */         if (qName.equals(getQName(i)))
/* 280 */           return i;
/*     */       }
/* 282 */       return -1;
/*     */     }
/*     */ 
/*     */     public String getType(String uri, String localName) {
/* 286 */       int index = getIndex(uri, localName);
/* 287 */       if (index < 0) return null;
/* 288 */       return getType(index);
/*     */     }
/*     */ 
/*     */     public String getType(String qName) {
/* 292 */       int index = getIndex(qName);
/* 293 */       if (index < 0) return null;
/* 294 */       return getType(index);
/*     */     }
/*     */ 
/*     */     public String getValue(String uri, String localName) {
/* 298 */       int index = getIndex(uri, localName);
/* 299 */       if (index < 0) return null;
/* 300 */       return getValue(index);
/*     */     }
/*     */ 
/*     */     public String getValue(String qName) {
/* 304 */       int index = getIndex(qName);
/* 305 */       if (index < 0) return null;
/* 306 */       return getValue(index);
/*     */     }
/* 237 */   };
/*     */ 
/* 334 */   private static final Class FI_STAX_READER_CLASS = initFIStAXReaderClass();
/* 335 */   private static final Constructor<? extends StAXConnector> FI_CONNECTOR_CTOR = initFastInfosetConnectorClass();
/*     */ 
/* 367 */   private static final Class STAX_EX_READER_CLASS = initStAXExReader();
/* 368 */   private static final Constructor<? extends StAXConnector> STAX_EX_CONNECTOR_CTOR = initStAXExConnector();
/*     */ 
/*     */   public static StAXConnector create(XMLStreamReader reader, XmlVisitor visitor)
/*     */   {
/*  62 */     Class readerClass = reader.getClass();
/*  63 */     if ((FI_STAX_READER_CLASS != null) && (FI_STAX_READER_CLASS.isAssignableFrom(readerClass)) && (FI_CONNECTOR_CTOR != null)) {
/*     */       try {
/*  65 */         return (StAXConnector)FI_CONNECTOR_CTOR.newInstance(new Object[] { reader, visitor });
/*     */       }
/*     */       catch (Exception t)
/*     */       {
/*     */       }
/*     */     }
/*  71 */     boolean isZephyr = readerClass.getName().equals("com.sun.xml.internal.stream.XMLReaderImpl");
/*  72 */     if ((!getBoolProp(reader, "org.codehaus.stax2.internNames")) || (!getBoolProp(reader, "org.codehaus.stax2.internNsUris")))
/*     */     {
/*  76 */       if (!isZephyr)
/*     */       {
/*  79 */         if (!checkImplementaionNameOfSjsxp(reader))
/*     */         {
/*  82 */           visitor = new InterningXmlVisitor(visitor);
/*     */         }
/*     */       }
/*     */     }
/*  84 */     if ((STAX_EX_READER_CLASS != null) && (STAX_EX_READER_CLASS.isAssignableFrom(readerClass)))
/*     */       try {
/*  86 */         return (StAXConnector)STAX_EX_CONNECTOR_CTOR.newInstance(new Object[] { reader, visitor });
/*     */       }
/*     */       catch (Exception t)
/*     */       {
/*     */       }
/*  91 */     return new StAXStreamConnector(reader, visitor);
/*     */   }
/*     */ 
/*     */   private static boolean checkImplementaionNameOfSjsxp(XMLStreamReader reader) {
/*     */     try {
/*  96 */       Object name = reader.getProperty("http://java.sun.com/xml/stream/properties/implementation-name");
/*  97 */       return (name != null) && (name.equals("sjsxp"));
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean getBoolProp(XMLStreamReader r, String n)
/*     */   {
/*     */     try {
/* 107 */       Object o = r.getProperty(n);
/* 108 */       if ((o instanceof Boolean)) return ((Boolean)o).booleanValue();
/* 109 */       return false;
/*     */     }
/*     */     catch (Exception e) {
/*     */     }
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   protected StAXStreamConnector(XMLStreamReader staxStreamReader, XmlVisitor visitor)
/*     */   {
/* 134 */     super(visitor);
/* 135 */     this.staxStreamReader = staxStreamReader;
/*     */   }
/*     */ 
/*     */   public void bridge() throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 142 */       int depth = 0;
/*     */ 
/* 145 */       int event = this.staxStreamReader.getEventType();
/* 146 */       if (event == 7)
/*     */       {
/* 148 */         while (!this.staxStreamReader.isStartElement()) {
/* 149 */           event = this.staxStreamReader.next();
/*     */         }
/*     */       }
/*     */ 
/* 153 */       if (event != 1) {
/* 154 */         throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
/*     */       }
/* 156 */       handleStartDocument(this.staxStreamReader.getNamespaceContext());
/*     */       while (true)
/*     */       {
/* 163 */         switch (event) {
/*     */         case 1:
/* 165 */           handleStartElement();
/* 166 */           depth++;
/* 167 */           break;
/*     */         case 2:
/* 169 */           depth--;
/* 170 */           handleEndElement();
/* 171 */           if (depth != 0) break; break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 176 */           handleCharacters();
/*     */         case 3:
/*     */         case 5:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/* 181 */         case 11: } event = this.staxStreamReader.next();
/*     */       }
/*     */ 
/* 184 */       this.staxStreamReader.next();
/*     */ 
/* 186 */       handleEndDocument();
/*     */     } catch (SAXException e) {
/* 188 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Location getCurrentLocation() {
/* 193 */     return this.staxStreamReader.getLocation();
/*     */   }
/*     */ 
/*     */   protected String getCurrentQName() {
/* 197 */     return getQName(this.staxStreamReader.getPrefix(), this.staxStreamReader.getLocalName());
/*     */   }
/*     */ 
/*     */   private void handleEndElement() throws SAXException {
/* 201 */     processText(false);
/*     */ 
/* 204 */     this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI());
/* 205 */     this.tagName.local = this.staxStreamReader.getLocalName();
/* 206 */     this.visitor.endElement(this.tagName);
/*     */ 
/* 209 */     int nsCount = this.staxStreamReader.getNamespaceCount();
/* 210 */     for (int i = nsCount - 1; i >= 0; i--)
/* 211 */       this.visitor.endPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)));
/*     */   }
/*     */ 
/*     */   private void handleStartElement() throws SAXException
/*     */   {
/* 216 */     processText(true);
/*     */ 
/* 219 */     int nsCount = this.staxStreamReader.getNamespaceCount();
/* 220 */     for (int i = 0; i < nsCount; i++) {
/* 221 */       this.visitor.startPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)), fixNull(this.staxStreamReader.getNamespaceURI(i)));
/*     */     }
/*     */ 
/* 227 */     this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI());
/* 228 */     this.tagName.local = this.staxStreamReader.getLocalName();
/* 229 */     this.tagName.atts = this.attributes;
/*     */ 
/* 231 */     this.visitor.startElement(this.tagName);
/*     */   }
/*     */ 
/*     */   protected void handleCharacters()
/*     */     throws XMLStreamException, SAXException
/*     */   {
/* 311 */     if (this.predictor.expectText())
/* 312 */       this.buffer.append(this.staxStreamReader.getTextCharacters(), this.staxStreamReader.getTextStart(), this.staxStreamReader.getTextLength());
/*     */   }
/*     */ 
/*     */   private void processText(boolean ignorable)
/*     */     throws SAXException
/*     */   {
/* 319 */     if ((this.predictor.expectText()) && ((!ignorable) || (!WhiteSpaceProcessor.isWhiteSpace(this.buffer)) || (this.context.getCurrentState().isMixed()))) {
/* 320 */       if (this.textReported)
/* 321 */         this.textReported = false;
/*     */       else {
/* 323 */         this.visitor.text(this.buffer);
/*     */       }
/*     */     }
/* 326 */     this.buffer.setLength(0);
/*     */   }
/*     */ 
/*     */   private static Class initFIStAXReaderClass()
/*     */   {
/*     */     try
/*     */     {
/* 339 */       Class fisr = Class.forName("com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader");
/* 340 */       Class sdp = Class.forName("com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser");
/*     */ 
/* 342 */       if (fisr.isAssignableFrom(sdp)) {
/* 343 */         return sdp;
/*     */       }
/* 345 */       return null; } catch (Throwable e) {
/*     */     }
/* 347 */     return null;
/*     */   }
/*     */ 
/*     */   private static Constructor<? extends StAXConnector> initFastInfosetConnectorClass()
/*     */   {
/*     */     try {
/* 353 */       if (FI_STAX_READER_CLASS == null) {
/* 354 */         return null;
/*     */       }
/* 356 */       Class c = Class.forName("com.sun.xml.internal.bind.v2.runtime.unmarshaller.FastInfosetConnector");
/*     */ 
/* 358 */       return c.getConstructor(new Class[] { FI_STAX_READER_CLASS, XmlVisitor.class }); } catch (Throwable e) {
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */   private static Class initStAXExReader()
/*     */   {
/*     */     try
/*     */     {
/* 372 */       return Class.forName("com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx"); } catch (Throwable e) {
/*     */     }
/* 374 */     return null;
/*     */   }
/*     */ 
/*     */   private static Constructor<? extends StAXConnector> initStAXExConnector()
/*     */   {
/*     */     try {
/* 380 */       Class c = Class.forName("com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXExConnector");
/* 381 */       return c.getConstructor(new Class[] { STAX_EX_READER_CLASS, XmlVisitor.class }); } catch (Throwable e) {
/*     */     }
/* 383 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXStreamConnector
 * JD-Core Version:    0.6.2
 */