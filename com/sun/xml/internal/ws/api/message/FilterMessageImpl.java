/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class FilterMessageImpl extends Message
/*     */ {
/*     */   private final Message delegate;
/*     */ 
/*     */   protected FilterMessageImpl(Message delegate)
/*     */   {
/*  67 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/*  71 */     return this.delegate.hasHeaders();
/*     */   }
/*     */   @NotNull
/*     */   public HeaderList getHeaders() {
/*  75 */     return this.delegate.getHeaders();
/*     */   }
/*     */   @NotNull
/*     */   public AttachmentSet getAttachments() {
/*  79 */     return this.delegate.getAttachments();
/*     */   }
/*     */ 
/*     */   protected boolean hasAttachments() {
/*  83 */     return this.delegate.hasAttachments();
/*     */   }
/*     */ 
/*     */   public boolean isOneWay(@NotNull WSDLPort port) {
/*  87 */     return this.delegate.isOneWay(port);
/*     */   }
/*     */   @Nullable
/*     */   public String getPayloadLocalPart() {
/*  91 */     return this.delegate.getPayloadLocalPart();
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/*  95 */     return this.delegate.getPayloadNamespaceURI();
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/*  99 */     return this.delegate.hasPayload();
/*     */   }
/*     */ 
/*     */   public boolean isFault() {
/* 103 */     return this.delegate.isFault();
/*     */   }
/*     */   @Nullable
/*     */   public QName getFirstDetailEntryName() {
/* 107 */     return this.delegate.getFirstDetailEntryName();
/*     */   }
/*     */ 
/*     */   public Source readEnvelopeAsSource() {
/* 111 */     return this.delegate.readEnvelopeAsSource();
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/* 115 */     return this.delegate.readPayloadAsSource();
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage() throws SOAPException {
/* 119 */     return this.delegate.readAsSOAPMessage();
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
/* 123 */     return this.delegate.readAsSOAPMessage(packet, inbound);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 127 */     return this.delegate.readPayloadAsJAXB(unmarshaller);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 131 */     return this.delegate.readPayloadAsJAXB(bridge);
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/* 135 */     return this.delegate.readPayload();
/*     */   }
/*     */ 
/*     */   public void consume() {
/* 139 */     this.delegate.consume();
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
/* 143 */     this.delegate.writePayloadTo(sw);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
/* 147 */     this.delegate.writeTo(sw);
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 151 */     this.delegate.writeTo(contentHandler, errorHandler);
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 155 */     return this.delegate.copy();
/*     */   }
/*     */   @NotNull
/*     */   public String getID(@NotNull WSBinding binding) {
/* 159 */     return this.delegate.getID(binding);
/*     */   }
/*     */   @NotNull
/*     */   public String getID(AddressingVersion av, SOAPVersion sv) {
/* 163 */     return this.delegate.getID(av, sv);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.FilterMessageImpl
 * JD-Core Version:    0.6.2
 */