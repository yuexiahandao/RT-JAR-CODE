/*     */ package com.sun.xml.internal.ws.message.source;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codecs;
/*     */ import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
/*     */ import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
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
/*     */ public class ProtocolSourceMessage extends Message
/*     */ {
/*     */   private final Message sm;
/*     */ 
/*     */   public ProtocolSourceMessage(Source source, SOAPVersion soapVersion)
/*     */   {
/*  62 */     XMLStreamReader reader = SourceReaderFactory.createSourceReader(source, true);
/*  63 */     StreamSOAPCodec codec = Codecs.createSOAPEnvelopeXmlCodec(soapVersion);
/*  64 */     this.sm = codec.decode(reader);
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/*  68 */     return this.sm.hasHeaders();
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/*  72 */     return this.sm.getHeaders();
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/*  76 */     return this.sm.getPayloadLocalPart();
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/*  80 */     return this.sm.getPayloadNamespaceURI();
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/*  84 */     return this.sm.hasPayload();
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/*  88 */     return this.sm.readPayloadAsSource();
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/*  92 */     return this.sm.readPayload();
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
/*  96 */     this.sm.writePayloadTo(sw);
/*     */   }
/*     */ 
/*     */   public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
/* 100 */     this.sm.writeTo(sw);
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 104 */     return this.sm.copy();
/*     */   }
/*     */ 
/*     */   public Source readEnvelopeAsSource() {
/* 108 */     return this.sm.readEnvelopeAsSource();
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage() throws SOAPException {
/* 112 */     return this.sm.readAsSOAPMessage();
/*     */   }
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
/* 116 */     return this.sm.readAsSOAPMessage(packet, inbound);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 120 */     return this.sm.readPayloadAsJAXB(unmarshaller);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 124 */     return this.sm.readPayloadAsJAXB(bridge);
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 128 */     this.sm.writeTo(contentHandler, errorHandler);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.source.ProtocolSourceMessage
 * JD-Core Version:    0.6.2
 */