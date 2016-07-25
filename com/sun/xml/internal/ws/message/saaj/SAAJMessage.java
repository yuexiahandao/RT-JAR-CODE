/*     */ package com.sun.xml.internal.ws.message.saaj;
/*     */ 
/*     */ import com.sun.istack.internal.FragmentContentHandler;
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.XMLStreamException2;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
/*     */ import com.sun.xml.internal.ws.streaming.DOMStreamReader;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.soap.AttachmentPart;
/*     */ import javax.xml.soap.MessageFactory;
/*     */ import javax.xml.soap.SOAPBody;
/*     */ import javax.xml.soap.SOAPEnvelope;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPHeader;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.soap.SOAPPart;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.TypeInfo;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class SAAJMessage extends Message
/*     */ {
/*     */   private boolean parsedMessage;
/*     */   private boolean accessedMessage;
/*     */   private final SOAPMessage sm;
/*     */   private HeaderList headers;
/*     */   private List<Element> bodyParts;
/*     */   private Element payload;
/*     */   private String payloadLocalName;
/*     */   private String payloadNamespace;
/*     */   private SOAPVersion soapVersion;
/*     */   private NamedNodeMap bodyAttrs;
/*     */   private NamedNodeMap headerAttrs;
/*     */   private NamedNodeMap envelopeAttrs;
/* 507 */   private static final AttributesImpl EMPTY_ATTS = new AttributesImpl();
/* 508 */   private static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
/*     */ 
/*     */   public SAAJMessage(SOAPMessage sm)
/*     */   {
/* 103 */     this.sm = sm;
/*     */   }
/*     */ 
/*     */   private SAAJMessage(HeaderList headers, AttachmentSet as, SOAPMessage sm)
/*     */   {
/* 113 */     this.sm = sm;
/* 114 */     parse();
/* 115 */     if (headers == null)
/* 116 */       headers = new HeaderList();
/* 117 */     this.headers = headers;
/* 118 */     this.attachmentSet = as;
/*     */   }
/*     */ 
/*     */   private void parse() {
/* 122 */     if (!this.parsedMessage)
/*     */       try {
/* 124 */         access();
/* 125 */         if (this.headers == null)
/* 126 */           this.headers = new HeaderList();
/* 127 */         SOAPHeader header = this.sm.getSOAPHeader();
/* 128 */         if (header != null) {
/* 129 */           this.headerAttrs = header.getAttributes();
/* 130 */           Iterator iter = header.examineAllHeaderElements();
/* 131 */           while (iter.hasNext()) {
/* 132 */             this.headers.add(new SAAJHeader((SOAPHeaderElement)iter.next()));
/*     */           }
/*     */         }
/* 135 */         this.attachmentSet = new SAAJAttachmentSet(this.sm);
/*     */ 
/* 137 */         this.parsedMessage = true;
/*     */       } catch (SOAPException e) {
/* 139 */         throw new WebServiceException(e);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void access()
/*     */   {
/* 145 */     if (!this.accessedMessage)
/*     */       try {
/* 147 */         this.envelopeAttrs = this.sm.getSOAPPart().getEnvelope().getAttributes();
/* 148 */         Node body = this.sm.getSOAPBody();
/* 149 */         this.bodyAttrs = body.getAttributes();
/* 150 */         this.soapVersion = SOAPVersion.fromNsUri(body.getNamespaceURI());
/*     */ 
/* 152 */         this.bodyParts = DOMUtil.getChildElements(body);
/*     */ 
/* 154 */         this.payload = (this.bodyParts.size() > 0 ? (Element)this.bodyParts.get(0) : null);
/*     */ 
/* 158 */         if (this.payload != null) {
/* 159 */           this.payloadLocalName = this.payload.getLocalName();
/* 160 */           this.payloadNamespace = this.payload.getNamespaceURI();
/*     */         }
/* 162 */         this.accessedMessage = true;
/*     */       } catch (SOAPException e) {
/* 164 */         throw new WebServiceException(e);
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders()
/*     */   {
/* 170 */     parse();
/* 171 */     return this.headers.size() > 0;
/*     */   }
/*     */   @NotNull
/*     */   public HeaderList getHeaders() {
/* 175 */     parse();
/* 176 */     return this.headers;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public AttachmentSet getAttachments()
/*     */   {
/* 184 */     parse();
/* 185 */     return this.attachmentSet;
/*     */   }
/*     */ 
/*     */   protected boolean hasAttachments()
/*     */   {
/* 194 */     parse();
/* 195 */     return this.attachmentSet != null;
/*     */   }
/*     */   @Nullable
/*     */   public String getPayloadLocalPart() {
/* 199 */     access();
/* 200 */     return this.payloadLocalName;
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/* 204 */     access();
/* 205 */     return this.payloadNamespace;
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/* 209 */     access();
/* 210 */     return this.payloadNamespace != null;
/*     */   }
/*     */ 
/*     */   private void addAttributes(Element e, NamedNodeMap attrs) {
/* 214 */     if (attrs == null)
/* 215 */       return;
/* 216 */     String elPrefix = e.getPrefix();
/* 217 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 218 */       Attr a = (Attr)attrs.item(i);
/*     */ 
/* 220 */       if (("xmlns".equals(a.getPrefix())) || (a.getLocalName().equals("xmlns"))) {
/* 221 */         if ((elPrefix != null) || (!a.getLocalName().equals("xmlns")))
/*     */         {
/* 224 */           if ((elPrefix == null) || (!"xmlns".equals(a.getPrefix())) || (!elPrefix.equals(a.getLocalName())))
/*     */           {
/* 228 */             e.setAttributeNS(a.getNamespaceURI(), a.getName(), a.getValue());
/*     */           }
/*     */         }
/*     */       } else e.setAttributeNS(a.getNamespaceURI(), a.getName(), a.getValue()); 
/*     */     }
/*     */   }
/*     */ 
/*     */   public Source readEnvelopeAsSource()
/*     */   {
/*     */     try { if (!this.parsedMessage) {
/* 238 */         SOAPEnvelope se = this.sm.getSOAPPart().getEnvelope();
/* 239 */         return new DOMSource(se);
/*     */       }
/*     */ 
/* 242 */       SOAPMessage msg = this.soapVersion.saajMessageFactory.createMessage();
/* 243 */       addAttributes(msg.getSOAPPart().getEnvelope(), this.envelopeAttrs);
/* 244 */       SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
/* 245 */       addAttributes(newBody, this.bodyAttrs);
/* 246 */       for (Element part : this.bodyParts) {
/* 247 */         Node n = newBody.getOwnerDocument().importNode(part, true);
/* 248 */         newBody.appendChild(n);
/*     */       }
/* 250 */       addAttributes(msg.getSOAPHeader(), this.headerAttrs);
/* 251 */       for (Header header : this.headers) {
/* 252 */         header.writeTo(msg);
/*     */       }
/* 254 */       SOAPEnvelope se = msg.getSOAPPart().getEnvelope();
/* 255 */       return new DOMSource(se);
/*     */     } catch (SOAPException e)
/*     */     {
/* 258 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage() throws SOAPException {
/* 263 */     if (!this.parsedMessage) {
/* 264 */       return this.sm;
/*     */     }
/* 266 */     SOAPMessage msg = this.soapVersion.saajMessageFactory.createMessage();
/* 267 */     addAttributes(msg.getSOAPPart().getEnvelope(), this.envelopeAttrs);
/* 268 */     SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
/* 269 */     addAttributes(newBody, this.bodyAttrs);
/* 270 */     for (Element part : this.bodyParts) {
/* 271 */       Node n = newBody.getOwnerDocument().importNode(part, true);
/* 272 */       newBody.appendChild(n);
/*     */     }
/* 274 */     addAttributes(msg.getSOAPHeader(), this.headerAttrs);
/* 275 */     for (Header header : this.headers) {
/* 276 */       header.writeTo(msg);
/*     */     }
/* 278 */     for (Attachment att : getAttachments()) {
/* 279 */       AttachmentPart part = msg.createAttachmentPart();
/* 280 */       part.setDataHandler(att.asDataHandler());
/* 281 */       part.setContentId('<' + att.getContentId() + '>');
/* 282 */       msg.addAttachmentPart(part);
/*     */     }
/* 284 */     msg.saveChanges();
/* 285 */     return msg;
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource()
/*     */   {
/* 290 */     access();
/* 291 */     return this.payload != null ? new DOMSource(this.payload) : null;
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 295 */     access();
/* 296 */     if (this.payload != null) {
/* 297 */       if (hasAttachments())
/* 298 */         unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
/* 299 */       return unmarshaller.unmarshal(this.payload);
/*     */     }
/*     */ 
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 306 */     access();
/* 307 */     if (this.payload != null)
/* 308 */       return bridge.unmarshal(this.payload, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/* 313 */     access();
/* 314 */     if (this.payload != null) {
/* 315 */       DOMStreamReader dss = new DOMStreamReader();
/* 316 */       dss.setCurrentNode(this.payload);
/* 317 */       dss.nextTag();
/* 318 */       assert (dss.getEventType() == 1);
/* 319 */       return dss;
/*     */     }
/* 321 */     return null;
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
/* 325 */     access();
/*     */     try {
/* 327 */       for (Element part : this.bodyParts)
/* 328 */         DOMUtil.serializeNode(part, sw);
/*     */     } catch (XMLStreamException e) {
/* 330 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter writer) throws XMLStreamException {
/*     */     try {
/* 336 */       writer.writeStartDocument();
/* 337 */       if (!this.parsedMessage) {
/* 338 */         DOMUtil.serializeNode(this.sm.getSOAPPart().getEnvelope(), writer);
/*     */       } else {
/* 340 */         SOAPEnvelope env = this.sm.getSOAPPart().getEnvelope();
/* 341 */         DOMUtil.writeTagWithAttributes(env, writer);
/* 342 */         if (hasHeaders()) {
/* 343 */           if (env.getHeader() != null)
/* 344 */             DOMUtil.writeTagWithAttributes(env.getHeader(), writer);
/*     */           else {
/* 346 */             writer.writeStartElement(env.getPrefix(), "Header", env.getNamespaceURI());
/*     */           }
/* 348 */           int len = this.headers.size();
/* 349 */           for (int i = 0; i < len; i++) {
/* 350 */             this.headers.get(i).writeTo(writer);
/*     */           }
/* 352 */           writer.writeEndElement();
/*     */         }
/*     */ 
/* 355 */         DOMUtil.serializeNode(this.sm.getSOAPBody(), writer);
/* 356 */         writer.writeEndElement();
/*     */       }
/* 358 */       writer.writeEndDocument();
/* 359 */       writer.flush();
/*     */     } catch (SOAPException ex) {
/* 361 */       throw new XMLStreamException2(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException
/*     */   {
/* 367 */     String soapNsUri = this.soapVersion.nsUri;
/* 368 */     if (!this.parsedMessage) {
/* 369 */       DOMScanner ds = new DOMScanner();
/* 370 */       ds.setContentHandler(contentHandler);
/* 371 */       ds.scan(this.sm.getSOAPPart());
/*     */     } else {
/* 373 */       contentHandler.setDocumentLocator(NULL_LOCATOR);
/* 374 */       contentHandler.startDocument();
/* 375 */       contentHandler.startPrefixMapping("S", soapNsUri);
/* 376 */       startPrefixMapping(contentHandler, this.envelopeAttrs, "S");
/* 377 */       contentHandler.startElement(soapNsUri, "Envelope", "S:Envelope", getAttributes(this.envelopeAttrs));
/* 378 */       if (hasHeaders()) {
/* 379 */         startPrefixMapping(contentHandler, this.headerAttrs, "S");
/* 380 */         contentHandler.startElement(soapNsUri, "Header", "S:Header", getAttributes(this.headerAttrs));
/* 381 */         HeaderList headers = getHeaders();
/* 382 */         int len = headers.size();
/* 383 */         for (int i = 0; i < len; i++)
/*     */         {
/* 385 */           headers.get(i).writeTo(contentHandler, errorHandler);
/*     */         }
/* 387 */         endPrefixMapping(contentHandler, this.headerAttrs, "S");
/* 388 */         contentHandler.endElement(soapNsUri, "Header", "S:Header");
/*     */       }
/*     */ 
/* 391 */       startPrefixMapping(contentHandler, this.bodyAttrs, "S");
/*     */ 
/* 393 */       contentHandler.startElement(soapNsUri, "Body", "S:Body", getAttributes(this.bodyAttrs));
/* 394 */       writePayloadTo(contentHandler, errorHandler, true);
/* 395 */       endPrefixMapping(contentHandler, this.bodyAttrs, "S");
/* 396 */       contentHandler.endElement(soapNsUri, "Body", "S:Body");
/* 397 */       endPrefixMapping(contentHandler, this.envelopeAttrs, "S");
/* 398 */       contentHandler.endElement(soapNsUri, "Envelope", "S:Envelope");
/*     */     }
/*     */   }
/*     */ 
/*     */   private AttributesImpl getAttributes(NamedNodeMap attrs)
/*     */   {
/* 407 */     AttributesImpl atts = new AttributesImpl();
/* 408 */     if (attrs == null)
/* 409 */       return EMPTY_ATTS;
/* 410 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 411 */       Attr a = (Attr)attrs.item(i);
/*     */ 
/* 413 */       if ((!"xmlns".equals(a.getPrefix())) && (!a.getLocalName().equals("xmlns")))
/*     */       {
/* 416 */         atts.addAttribute(fixNull(a.getNamespaceURI()), a.getLocalName(), a.getName(), a.getSchemaTypeInfo().getTypeName(), a.getValue());
/*     */       }
/*     */     }
/* 418 */     return atts;
/*     */   }
/*     */ 
/*     */   private void startPrefixMapping(ContentHandler contentHandler, NamedNodeMap attrs, String excludePrefix)
/*     */     throws SAXException
/*     */   {
/* 429 */     if (attrs == null)
/* 430 */       return;
/* 431 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 432 */       Attr a = (Attr)attrs.item(i);
/*     */ 
/* 434 */       if ((("xmlns".equals(a.getPrefix())) || (a.getLocalName().equals("xmlns"))) && 
/* 435 */         (!fixNull(a.getPrefix()).equals(excludePrefix)))
/* 436 */         contentHandler.startPrefixMapping(fixNull(a.getPrefix()), a.getNamespaceURI());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void endPrefixMapping(ContentHandler contentHandler, NamedNodeMap attrs, String excludePrefix)
/*     */     throws SAXException
/*     */   {
/* 443 */     if (attrs == null)
/* 444 */       return;
/* 445 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 446 */       Attr a = (Attr)attrs.item(i);
/*     */ 
/* 448 */       if ((("xmlns".equals(a.getPrefix())) || (a.getLocalName().equals("xmlns"))) && 
/* 449 */         (!fixNull(a.getPrefix()).equals(excludePrefix)))
/* 450 */         contentHandler.endPrefixMapping(fixNull(a.getPrefix()));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String fixNull(String s)
/*     */   {
/* 457 */     if (s == null) return "";
/* 458 */     return s;
/*     */   }
/*     */ 
/*     */   private void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 462 */     if (fragment)
/* 463 */       contentHandler = new FragmentContentHandler(contentHandler);
/* 464 */     DOMScanner ds = new DOMScanner();
/* 465 */     ds.setContentHandler(contentHandler);
/* 466 */     ds.scan(this.payload);
/*     */   }
/*     */ 
/*     */   public Message copy()
/*     */   {
/*     */     try
/*     */     {
/* 492 */       if (!this.parsedMessage) {
/* 493 */         return new SAAJMessage(readAsSOAPMessage());
/*     */       }
/* 495 */       SOAPMessage msg = this.soapVersion.saajMessageFactory.createMessage();
/* 496 */       SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
/* 497 */       for (Element part : this.bodyParts) {
/* 498 */         Node n = newBody.getOwnerDocument().importNode(part, true);
/* 499 */         newBody.appendChild(n);
/*     */       }
/* 501 */       return new SAAJMessage(getHeaders(), getAttachments(), msg);
/*     */     }
/*     */     catch (SOAPException e) {
/* 504 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SAAJAttachment
/*     */     implements Attachment
/*     */   {
/*     */     final AttachmentPart ap;
/*     */ 
/*     */     public SAAJAttachment(AttachmentPart part)
/*     */     {
/* 515 */       this.ap = part;
/*     */     }
/*     */ 
/*     */     public String getContentId()
/*     */     {
/* 522 */       return this.ap.getContentId();
/*     */     }
/*     */ 
/*     */     public String getContentType()
/*     */     {
/* 529 */       return this.ap.getContentType();
/*     */     }
/*     */ 
/*     */     public byte[] asByteArray()
/*     */     {
/*     */       try
/*     */       {
/* 537 */         return this.ap.getRawContentBytes();
/*     */       } catch (SOAPException e) {
/* 539 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public DataHandler asDataHandler()
/*     */     {
/*     */       try
/*     */       {
/* 548 */         return this.ap.getDataHandler();
/*     */       } catch (SOAPException e) {
/* 550 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Source asSource()
/*     */     {
/*     */       try
/*     */       {
/* 560 */         return new StreamSource(this.ap.getRawContent());
/*     */       } catch (SOAPException e) {
/* 562 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public InputStream asInputStream()
/*     */     {
/*     */       try
/*     */       {
/* 571 */         return this.ap.getRawContent();
/*     */       } catch (SOAPException e) {
/* 573 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeTo(OutputStream os)
/*     */       throws IOException
/*     */     {
/* 581 */       os.write(asByteArray());
/*     */     }
/*     */ 
/*     */     public void writeTo(SOAPMessage saaj)
/*     */     {
/* 588 */       saaj.addAttachmentPart(this.ap);
/*     */     }
/*     */ 
/*     */     AttachmentPart asAttachmentPart() {
/* 592 */       return this.ap;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SAAJAttachmentSet
/*     */     implements AttachmentSet
/*     */   {
/*     */     private Map<String, Attachment> attMap;
/*     */     private Iterator attIter;
/*     */ 
/*     */     public SAAJAttachmentSet(SOAPMessage sm)
/*     */     {
/* 608 */       this.attIter = sm.getAttachments();
/*     */     }
/*     */ 
/*     */     public Attachment get(String contentId)
/*     */     {
/* 619 */       if (this.attMap == null) {
/* 620 */         if (!this.attIter.hasNext())
/* 621 */           return null;
/* 622 */         this.attMap = createAttachmentMap();
/*     */       }
/* 624 */       if (contentId.charAt(0) != '<') {
/* 625 */         return (Attachment)this.attMap.get('<' + contentId + '>');
/*     */       }
/* 627 */       return (Attachment)this.attMap.get(contentId);
/*     */     }
/*     */ 
/*     */     public boolean isEmpty() {
/* 631 */       if (this.attMap != null) {
/* 632 */         return this.attMap.isEmpty();
/*     */       }
/* 634 */       return !this.attIter.hasNext();
/*     */     }
/*     */ 
/*     */     public Iterator<Attachment> iterator()
/*     */     {
/* 643 */       if (this.attMap == null) {
/* 644 */         this.attMap = createAttachmentMap();
/*     */       }
/* 646 */       return this.attMap.values().iterator();
/*     */     }
/*     */ 
/*     */     private Map<String, Attachment> createAttachmentMap() {
/* 650 */       HashMap map = new HashMap();
/* 651 */       while (this.attIter.hasNext()) {
/* 652 */         AttachmentPart ap = (AttachmentPart)this.attIter.next();
/* 653 */         map.put(ap.getContentId(), new SAAJMessage.SAAJAttachment(SAAJMessage.this, ap));
/*     */       }
/* 655 */       return map;
/*     */     }
/*     */ 
/*     */     public void add(Attachment att) {
/* 659 */       this.attMap.put('<' + att.getContentId() + '>', att);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.saaj.SAAJMessage
 * JD-Core Version:    0.6.2
 */