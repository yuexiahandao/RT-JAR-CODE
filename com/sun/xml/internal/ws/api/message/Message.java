/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import java.util.UUID;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Message
/*     */ {
/*     */   protected AttachmentSet attachmentSet;
/* 238 */   private WSDLBoundOperation operation = null;
/*     */   private Boolean isOneWay;
/*     */   private String uuid;
/*     */ 
/*     */   public abstract boolean hasHeaders();
/*     */ 
/*     */   @NotNull
/*     */   public abstract HeaderList getHeaders();
/*     */ 
/*     */   @NotNull
/*     */   public AttachmentSet getAttachments()
/*     */   {
/* 222 */     if (this.attachmentSet == null) {
/* 223 */       this.attachmentSet = new AttachmentSetImpl();
/*     */     }
/* 225 */     return this.attachmentSet;
/*     */   }
/*     */ 
/*     */   protected boolean hasAttachments()
/*     */   {
/* 233 */     return this.attachmentSet != null;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public final WSDLBoundOperation getOperation(@NotNull WSDLBoundPortType boundPortType)
/*     */   {
/* 269 */     if (this.operation == null)
/* 270 */       this.operation = boundPortType.getOperation(getPayloadNamespaceURI(), getPayloadLocalPart());
/* 271 */     return this.operation;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public final WSDLBoundOperation getOperation(@NotNull WSDLPort port)
/*     */   {
/* 284 */     return getOperation(port.getBinding());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public final JavaMethod getMethod(@NotNull SEIModel seiModel)
/*     */   {
/* 314 */     String localPart = getPayloadLocalPart();
/*     */     String nsUri;
/*     */     String nsUri;
/* 316 */     if (localPart == null) {
/* 317 */       localPart = "";
/* 318 */       nsUri = "";
/*     */     } else {
/* 320 */       nsUri = getPayloadNamespaceURI();
/*     */     }
/* 322 */     QName name = new QName(nsUri, localPart);
/* 323 */     return seiModel.getJavaMethod(name);
/*     */   }
/*     */ 
/*     */   public boolean isOneWay(@NotNull WSDLPort port)
/*     */   {
/* 352 */     if (this.isOneWay == null)
/*     */     {
/* 354 */       WSDLBoundOperation op = getOperation(port);
/* 355 */       if (op != null) {
/* 356 */         this.isOneWay = Boolean.valueOf(op.getOperation().isOneWay());
/*     */       }
/*     */       else
/* 359 */         this.isOneWay = Boolean.valueOf(false);
/*     */     }
/* 361 */     return this.isOneWay.booleanValue();
/*     */   }
/*     */ 
/*     */   public final void assertOneWay(boolean value)
/*     */   {
/* 390 */     assert ((this.isOneWay == null) || (this.isOneWay.booleanValue() == value));
/*     */ 
/* 392 */     this.isOneWay = Boolean.valueOf(value);
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public abstract String getPayloadLocalPart();
/*     */ 
/*     */   public abstract String getPayloadNamespaceURI();
/*     */ 
/*     */   public abstract boolean hasPayload();
/*     */ 
/*     */   public boolean isFault()
/*     */   {
/* 440 */     String localPart = getPayloadLocalPart();
/* 441 */     if ((localPart == null) || (!localPart.equals("Fault"))) {
/* 442 */       return false;
/*     */     }
/* 444 */     String nsUri = getPayloadNamespaceURI();
/* 445 */     return (nsUri.equals(SOAPVersion.SOAP_11.nsUri)) || (nsUri.equals(SOAPVersion.SOAP_12.nsUri));
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public QName getFirstDetailEntryName()
/*     */   {
/* 459 */     assert (isFault());
/* 460 */     Message msg = copy();
/*     */     try {
/* 462 */       SOAPFaultBuilder fault = SOAPFaultBuilder.create(msg);
/* 463 */       return fault.getFirstDetailEntryName();
/*     */     } catch (JAXBException e) {
/* 465 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract Source readEnvelopeAsSource();
/*     */ 
/*     */   public abstract Source readPayloadAsSource();
/*     */ 
/*     */   public abstract SOAPMessage readAsSOAPMessage()
/*     */     throws SOAPException;
/*     */ 
/*     */   public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound)
/*     */     throws SOAPException
/*     */   {
/* 508 */     return readAsSOAPMessage();
/*     */   }
/*     */ 
/*     */   public abstract <T> T readPayloadAsJAXB(Unmarshaller paramUnmarshaller)
/*     */     throws JAXBException;
/*     */ 
/*     */   public abstract <T> T readPayloadAsJAXB(Bridge<T> paramBridge)
/*     */     throws JAXBException;
/*     */ 
/*     */   public abstract XMLStreamReader readPayload()
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public void consume()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract void writePayloadTo(XMLStreamWriter paramXMLStreamWriter)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract void writeTo(XMLStreamWriter paramXMLStreamWriter)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract void writeTo(ContentHandler paramContentHandler, ErrorHandler paramErrorHandler)
/*     */     throws SAXException;
/*     */ 
/*     */   public abstract Message copy();
/*     */ 
/*     */   @NotNull
/*     */   public String getID(@NotNull WSBinding binding)
/*     */   {
/* 695 */     return getID(binding.getAddressingVersion(), binding.getSOAPVersion());
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getID(AddressingVersion av, SOAPVersion sv)
/*     */   {
/* 707 */     if (this.uuid == null) {
/* 708 */       if (av != null) {
/* 709 */         this.uuid = getHeaders().getMessageID(av, sv);
/*     */       }
/* 711 */       if (this.uuid == null) {
/* 712 */         this.uuid = ("uuid:" + UUID.randomUUID().toString());
/*     */       }
/*     */     }
/* 715 */     return this.uuid;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.Message
 * JD-Core Version:    0.6.2
 */