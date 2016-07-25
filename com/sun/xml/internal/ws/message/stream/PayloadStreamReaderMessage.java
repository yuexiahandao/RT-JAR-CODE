/*     */ package com.sun.xml.internal.ws.message.stream;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.message.AbstractMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class PayloadStreamReaderMessage extends AbstractMessageImpl
/*     */ {
/*     */   private final StreamMessage message;
/*     */ 
/*     */   public PayloadStreamReaderMessage(XMLStreamReader reader, SOAPVersion soapVer)
/*     */   {
/*  56 */     this(null, reader, new AttachmentSetImpl(), soapVer);
/*     */   }
/*     */ 
/*     */   public PayloadStreamReaderMessage(@Nullable HeaderList headers, @NotNull XMLStreamReader reader, @NotNull AttachmentSet attSet, @NotNull SOAPVersion soapVersion)
/*     */   {
/*  61 */     super(soapVersion);
/*  62 */     this.message = new StreamMessage(headers, attSet, reader, soapVersion);
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/*  66 */     return this.message.hasHeaders();
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/*  70 */     return this.message.getHeaders();
/*     */   }
/*     */ 
/*     */   public AttachmentSet getAttachments() {
/*  74 */     return this.message.getAttachments();
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/*  78 */     return this.message.getPayloadLocalPart();
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/*  82 */     return this.message.getPayloadNamespaceURI();
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/*  90 */     return this.message.readPayloadAsSource();
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/*  94 */     return this.message.readPayload();
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
/*  98 */     this.message.writePayloadTo(sw);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 102 */     return this.message.readPayloadAsJAXB(unmarshaller);
/*     */   }
/*     */ 
/*     */   public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 106 */     this.message.writeTo(contentHandler, errorHandler);
/*     */   }
/*     */ 
/*     */   protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 110 */     this.message.writePayloadTo(contentHandler, errorHandler, fragment);
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 114 */     return this.message.copy();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.stream.PayloadStreamReaderMessage
 * JD-Core Version:    0.6.2
 */