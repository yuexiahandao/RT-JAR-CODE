/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.FragmentContentHandler;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.streaming.DOMStreamReader;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class DOMMessage extends AbstractMessageImpl
/*     */ {
/*     */   private HeaderList headers;
/*     */   private final Element payload;
/*     */ 
/*     */   public DOMMessage(SOAPVersion ver, Element payload)
/*     */   {
/*  61 */     this(ver, null, payload);
/*     */   }
/*     */ 
/*     */   public DOMMessage(SOAPVersion ver, HeaderList headers, Element payload) {
/*  65 */     this(ver, headers, payload, null);
/*     */   }
/*     */ 
/*     */   public DOMMessage(SOAPVersion ver, HeaderList headers, Element payload, AttachmentSet attachments) {
/*  69 */     super(ver);
/*  70 */     this.headers = headers;
/*  71 */     this.payload = payload;
/*  72 */     this.attachmentSet = attachments;
/*  73 */     assert (payload != null);
/*     */   }
/*     */ 
/*     */   private DOMMessage(DOMMessage that)
/*     */   {
/*  79 */     super(that);
/*  80 */     this.headers = HeaderList.copy(that.headers);
/*  81 */     this.payload = that.payload;
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/*  85 */     return getHeaders().size() > 0;
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/*  89 */     if (this.headers == null) {
/*  90 */       this.headers = new HeaderList();
/*     */     }
/*  92 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/*  96 */     return this.payload.getLocalName();
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/* 100 */     return this.payload.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/* 108 */     return new DOMSource(this.payload);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 112 */     if (hasAttachments())
/* 113 */       unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
/*     */     try {
/* 115 */       return unmarshaller.unmarshal(this.payload);
/*     */     } finally {
/* 117 */       unmarshaller.setAttachmentUnmarshaller(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 122 */     return bridge.unmarshal(this.payload, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException
/*     */   {
/* 127 */     DOMStreamReader dss = new DOMStreamReader();
/* 128 */     dss.setCurrentNode(this.payload);
/* 129 */     dss.nextTag();
/* 130 */     assert (dss.getEventType() == 1);
/* 131 */     return dss;
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) {
/*     */     try {
/* 136 */       if (this.payload != null)
/* 137 */         DOMUtil.serializeNode(this.payload, sw);
/*     */     } catch (XMLStreamException e) {
/* 139 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 144 */     if (fragment)
/* 145 */       contentHandler = new FragmentContentHandler(contentHandler);
/* 146 */     DOMScanner ds = new DOMScanner();
/* 147 */     ds.setContentHandler(contentHandler);
/* 148 */     ds.scan(this.payload);
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 152 */     return new DOMMessage(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.DOMMessage
 * JD-Core Version:    0.6.2
 */