/*     */ package com.sun.xml.internal.ws.message.stream;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.XMLStreamReaderToContentHandler;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.encoding.TagInfoset;
/*     */ import com.sun.xml.internal.ws.message.AbstractMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.util.xml.DummyLocation;
/*     */ import com.sun.xml.internal.ws.util.xml.StAXSource;
/*     */ import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.NamespaceSupport;
/*     */ 
/*     */ public final class StreamMessage extends AbstractMessageImpl
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private XMLStreamReader reader;
/*     */ 
/*     */   @Nullable
/*     */   private HeaderList headers;
/*     */   private final String payloadLocalName;
/*     */   private final String payloadNamespaceURI;
/*     */ 
/*     */   @NotNull
/*     */   private TagInfoset envelopeTag;
/*     */ 
/*     */   @NotNull
/*     */   private TagInfoset headerTag;
/*     */ 
/*     */   @NotNull
/*     */   private TagInfoset bodyTag;
/*     */   private Throwable consumedAt;
/*     */   private static final TagInfoset[] DEFAULT_TAGS;
/*     */ 
/*     */   public StreamMessage(@Nullable HeaderList headers, @NotNull AttachmentSet attachmentSet, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion)
/*     */   {
/* 110 */     super(soapVersion);
/* 111 */     this.headers = headers;
/* 112 */     this.attachmentSet = attachmentSet;
/* 113 */     this.reader = reader;
/*     */ 
/* 115 */     if (reader.getEventType() == 7) {
/* 116 */       XMLStreamReaderUtil.nextElementContent(reader);
/*     */     }
/*     */ 
/* 120 */     if (reader.getEventType() == 2) {
/* 121 */       String body = reader.getLocalName();
/* 122 */       String nsUri = reader.getNamespaceURI();
/* 123 */       assert (body != null);
/* 124 */       assert (nsUri != null);
/*     */ 
/* 126 */       if ((body.equals("Body")) && (nsUri.equals(soapVersion.nsUri))) {
/* 127 */         this.payloadLocalName = null;
/* 128 */         this.payloadNamespaceURI = null;
/*     */       } else {
/* 130 */         throw new WebServiceException("Malformed stream: {" + nsUri + "}" + body);
/*     */       }
/*     */     } else {
/* 133 */       this.payloadLocalName = reader.getLocalName();
/* 134 */       this.payloadNamespaceURI = reader.getNamespaceURI();
/*     */     }
/*     */ 
/* 138 */     int base = soapVersion.ordinal() * 3;
/* 139 */     this.envelopeTag = DEFAULT_TAGS[base];
/* 140 */     this.headerTag = DEFAULT_TAGS[(base + 1)];
/* 141 */     this.bodyTag = DEFAULT_TAGS[(base + 2)];
/*     */   }
/*     */ 
/*     */   public StreamMessage(@NotNull TagInfoset envelopeTag, @Nullable TagInfoset headerTag, @NotNull AttachmentSet attachmentSet, @Nullable HeaderList headers, @NotNull TagInfoset bodyTag, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion)
/*     */   {
/* 157 */     this(headers, attachmentSet, reader, soapVersion);
/* 158 */     if (envelopeTag == null) {
/* 159 */       throw new IllegalArgumentException("EnvelopeTag TagInfoset cannot be null");
/*     */     }
/* 161 */     if (bodyTag == null) {
/* 162 */       throw new IllegalArgumentException("BodyTag TagInfoset cannot be null");
/*     */     }
/* 164 */     this.envelopeTag = envelopeTag;
/* 165 */     this.headerTag = (headerTag != null ? headerTag : new TagInfoset(envelopeTag.nsUri, "Header", envelopeTag.prefix, EMPTY_ATTS, new String[0]));
/*     */ 
/* 167 */     this.bodyTag = bodyTag;
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/* 171 */     return (this.headers != null) && (!this.headers.isEmpty());
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/* 175 */     if (this.headers == null) {
/* 176 */       this.headers = new HeaderList();
/*     */     }
/* 178 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/* 182 */     return this.payloadLocalName;
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/* 186 */     return this.payloadNamespaceURI;
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/* 190 */     return this.payloadLocalName != null;
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/* 194 */     if (hasPayload()) {
/* 195 */       assert (unconsumed());
/* 196 */       return new StAXSource(this.reader, true, getInscopeNamespaces());
/*     */     }
/* 198 */     return null;
/*     */   }
/*     */ 
/*     */   private String[] getInscopeNamespaces()
/*     */   {
/* 209 */     NamespaceSupport nss = new NamespaceSupport();
/*     */ 
/* 211 */     nss.pushContext();
/* 212 */     for (int i = 0; i < this.envelopeTag.ns.length; i += 2) {
/* 213 */       nss.declarePrefix(this.envelopeTag.ns[i], this.envelopeTag.ns[(i + 1)]);
/*     */     }
/*     */ 
/* 216 */     nss.pushContext();
/* 217 */     for (int i = 0; i < this.bodyTag.ns.length; i += 2) {
/* 218 */       nss.declarePrefix(this.bodyTag.ns[i], this.bodyTag.ns[(i + 1)]);
/*     */     }
/*     */ 
/* 221 */     List inscope = new ArrayList();
/* 222 */     for (Enumeration en = nss.getPrefixes(); en.hasMoreElements(); ) {
/* 223 */       String prefix = (String)en.nextElement();
/* 224 */       inscope.add(prefix);
/* 225 */       inscope.add(nss.getURI(prefix));
/*     */     }
/* 227 */     return (String[])inscope.toArray(new String[inscope.size()]);
/*     */   }
/*     */ 
/*     */   public Object readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 231 */     if (!hasPayload())
/* 232 */       return null;
/* 233 */     assert (unconsumed());
/*     */ 
/* 235 */     if (hasAttachments())
/* 236 */       unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
/*     */     try {
/* 238 */       return unmarshaller.unmarshal(this.reader);
/*     */     } finally {
/* 240 */       unmarshaller.setAttachmentUnmarshaller(null);
/* 241 */       XMLStreamReaderUtil.readRest(this.reader);
/* 242 */       XMLStreamReaderUtil.close(this.reader);
/* 243 */       XMLStreamReaderFactory.recycle(this.reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 248 */     if (!hasPayload())
/* 249 */       return null;
/* 250 */     assert (unconsumed());
/* 251 */     Object r = bridge.unmarshal(this.reader, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
/*     */ 
/* 253 */     XMLStreamReaderUtil.readRest(this.reader);
/* 254 */     XMLStreamReaderUtil.close(this.reader);
/* 255 */     XMLStreamReaderFactory.recycle(this.reader);
/* 256 */     return r;
/*     */   }
/*     */ 
/*     */   public void consume()
/*     */   {
/* 261 */     assert (unconsumed());
/* 262 */     XMLStreamReaderUtil.readRest(this.reader);
/* 263 */     XMLStreamReaderUtil.close(this.reader);
/* 264 */     XMLStreamReaderFactory.recycle(this.reader);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() {
/* 268 */     if (!hasPayload()) {
/* 269 */       return null;
/*     */     }
/* 271 */     assert (unconsumed());
/* 272 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter writer) throws XMLStreamException {
/* 276 */     if (this.payloadLocalName == null)
/* 277 */       return;
/* 278 */     assert (unconsumed());
/* 279 */     XMLStreamReaderToXMLStreamWriter conv = new XMLStreamReaderToXMLStreamWriter();
/* 280 */     while (this.reader.getEventType() != 8) {
/* 281 */       String name = this.reader.getLocalName();
/* 282 */       String nsUri = this.reader.getNamespaceURI();
/*     */ 
/* 287 */       if ((this.reader.getEventType() == 2) && (
/* 288 */         (!name.equals("Body")) || (!nsUri.equals(this.soapVersion.nsUri)))) {
/* 289 */         XMLStreamReaderUtil.nextElementContent(this.reader);
/* 290 */         if (this.reader.getEventType() == 8)
/*     */           break;
/* 292 */         name = this.reader.getLocalName();
/* 293 */         nsUri = this.reader.getNamespaceURI();
/*     */       }
/*     */ 
/* 296 */       if (((name.equals("Body")) && (nsUri.equals(this.soapVersion.nsUri))) || (this.reader.getEventType() == 8))
/*     */         break;
/* 298 */       conv.bridge(this.reader, writer);
/*     */     }
/* 300 */     XMLStreamReaderUtil.readRest(this.reader);
/* 301 */     XMLStreamReaderUtil.close(this.reader);
/* 302 */     XMLStreamReaderFactory.recycle(this.reader);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
/* 306 */     writeEnvelope(sw);
/*     */   }
/*     */ 
/*     */   private void writeEnvelope(XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/* 314 */     writer.writeStartDocument();
/* 315 */     this.envelopeTag.writeStart(writer);
/*     */ 
/* 318 */     HeaderList hl = getHeaders();
/* 319 */     if (hl.size() > 0) {
/* 320 */       this.headerTag.writeStart(writer);
/* 321 */       for (Header h : hl) {
/* 322 */         h.writeTo(writer);
/*     */       }
/* 324 */       writer.writeEndElement();
/*     */     }
/* 326 */     this.bodyTag.writeStart(writer);
/* 327 */     if (hasPayload())
/* 328 */       writePayloadTo(writer);
/* 329 */     writer.writeEndElement();
/* 330 */     writer.writeEndElement();
/* 331 */     writer.writeEndDocument();
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 335 */     assert (unconsumed());
/*     */     try {
/* 337 */       if (this.payloadLocalName == null) {
/* 338 */         return;
/*     */       }
/* 340 */       XMLStreamReaderToContentHandler conv = new XMLStreamReaderToContentHandler(this.reader, contentHandler, true, fragment, getInscopeNamespaces());
/*     */ 
/* 343 */       while (this.reader.getEventType() != 8) {
/* 344 */         String name = this.reader.getLocalName();
/* 345 */         String nsUri = this.reader.getNamespaceURI();
/*     */ 
/* 350 */         if ((this.reader.getEventType() == 2) && (
/* 351 */           (!name.equals("Body")) || (!nsUri.equals(this.soapVersion.nsUri)))) {
/* 352 */           XMLStreamReaderUtil.nextElementContent(this.reader);
/* 353 */           if (this.reader.getEventType() == 8)
/*     */             break;
/* 355 */           name = this.reader.getLocalName();
/* 356 */           nsUri = this.reader.getNamespaceURI();
/*     */         }
/*     */ 
/* 359 */         if (((name.equals("Body")) && (nsUri.equals(this.soapVersion.nsUri))) || (this.reader.getEventType() == 8)) {
/*     */           break;
/*     */         }
/* 362 */         conv.bridge();
/*     */       }
/* 364 */       XMLStreamReaderUtil.readRest(this.reader);
/* 365 */       XMLStreamReaderUtil.close(this.reader);
/* 366 */       XMLStreamReaderFactory.recycle(this.reader);
/*     */     } catch (XMLStreamException e) {
/* 368 */       Location loc = e.getLocation();
/* 369 */       if (loc == null) loc = DummyLocation.INSTANCE;
/*     */ 
/* 371 */       SAXParseException x = new SAXParseException(e.getMessage(), loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber(), e);
/*     */ 
/* 373 */       errorHandler.error(x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Message copy() {
/*     */     try {
/* 379 */       assert (unconsumed());
/* 380 */       this.consumedAt = null;
/* 381 */       MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/* 382 */       StreamReaderBufferCreator c = new StreamReaderBufferCreator(xsb);
/*     */ 
/* 386 */       c.storeElement(this.envelopeTag.nsUri, this.envelopeTag.localName, this.envelopeTag.prefix, this.envelopeTag.ns);
/* 387 */       c.storeElement(this.bodyTag.nsUri, this.bodyTag.localName, this.bodyTag.prefix, this.bodyTag.ns);
/*     */ 
/* 389 */       if (hasPayload())
/*     */       {
/* 391 */         while (this.reader.getEventType() != 8) {
/* 392 */           String name = this.reader.getLocalName();
/* 393 */           String nsUri = this.reader.getNamespaceURI();
/* 394 */           if (((name.equals("Body")) && (nsUri.equals(this.soapVersion.nsUri))) || (this.reader.getEventType() == 8))
/*     */             break;
/* 396 */           c.create(this.reader);
/*     */ 
/* 398 */           if (this.reader.isWhiteSpace()) {
/* 399 */             XMLStreamReaderUtil.nextElementContent(this.reader);
/*     */           }
/*     */         }
/*     */       }
/* 403 */       c.storeEndElement();
/* 404 */       c.storeEndElement();
/* 405 */       c.storeEndElement();
/*     */ 
/* 407 */       XMLStreamReaderUtil.readRest(this.reader);
/* 408 */       XMLStreamReaderUtil.close(this.reader);
/* 409 */       XMLStreamReaderFactory.recycle(this.reader);
/*     */ 
/* 411 */       this.reader = xsb.readAsXMLStreamReader();
/* 412 */       XMLStreamReader clone = xsb.readAsXMLStreamReader();
/*     */ 
/* 415 */       proceedToRootElement(this.reader);
/* 416 */       proceedToRootElement(clone);
/*     */ 
/* 418 */       return new StreamMessage(this.envelopeTag, this.headerTag, this.attachmentSet, HeaderList.copy(this.headers), this.bodyTag, clone, this.soapVersion);
/*     */     } catch (XMLStreamException e) {
/* 420 */       throw new WebServiceException("Failed to copy a message", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void proceedToRootElement(XMLStreamReader xsr) throws XMLStreamException {
/* 425 */     assert (xsr.getEventType() == 7);
/* 426 */     xsr.nextTag();
/* 427 */     xsr.nextTag();
/* 428 */     xsr.nextTag();
/* 429 */     assert ((xsr.getEventType() == 1) || (xsr.getEventType() == 2));
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 433 */     contentHandler.setDocumentLocator(NULL_LOCATOR);
/* 434 */     contentHandler.startDocument();
/* 435 */     this.envelopeTag.writeStart(contentHandler);
/* 436 */     this.headerTag.writeStart(contentHandler);
/* 437 */     if (hasHeaders()) {
/* 438 */       HeaderList headers = getHeaders();
/* 439 */       int len = headers.size();
/* 440 */       for (int i = 0; i < len; i++)
/*     */       {
/* 442 */         headers.get(i).writeTo(contentHandler, errorHandler);
/*     */       }
/*     */     }
/* 445 */     this.headerTag.writeEnd(contentHandler);
/* 446 */     this.bodyTag.writeStart(contentHandler);
/* 447 */     writePayloadTo(contentHandler, errorHandler, true);
/* 448 */     this.bodyTag.writeEnd(contentHandler);
/* 449 */     this.envelopeTag.writeEnd(contentHandler);
/*     */   }
/*     */ 
/*     */   private boolean unconsumed()
/*     */   {
/* 461 */     if (this.payloadLocalName == null) {
/* 462 */       return true;
/*     */     }
/* 464 */     if (this.reader.getEventType() != 1) {
/* 465 */       AssertionError error = new AssertionError("StreamMessage has been already consumed. See the nested exception for where it's consumed");
/* 466 */       error.initCause(this.consumedAt);
/* 467 */       throw error;
/*     */     }
/* 469 */     this.consumedAt = new Exception().fillInStackTrace();
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   private static void create(SOAPVersion v)
/*     */   {
/* 492 */     int base = v.ordinal() * 3;
/* 493 */     DEFAULT_TAGS[base] = new TagInfoset(v.nsUri, "Envelope", "S", EMPTY_ATTS, new String[] { "S", v.nsUri });
/* 494 */     DEFAULT_TAGS[(base + 1)] = new TagInfoset(v.nsUri, "Header", "S", EMPTY_ATTS, new String[0]);
/* 495 */     DEFAULT_TAGS[(base + 2)] = new TagInfoset(v.nsUri, "Body", "S", EMPTY_ATTS, new String[0]);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 486 */     DEFAULT_TAGS = new TagInfoset[6];
/* 487 */     create(SOAPVersion.SOAP_11);
/* 488 */     create(SOAPVersion.SOAP_12);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.StreamMessage
 * JD-Core Version:    0.6.2
 */