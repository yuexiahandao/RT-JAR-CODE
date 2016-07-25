/*     */ package com.sun.xml.internal.ws.message.jaxb;
/*     */ 
/*     */ import com.sun.istack.internal.FragmentContentHandler;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.message.AbstractMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import com.sun.xml.internal.ws.message.RootElementSniffer;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamMessage;
/*     */ import com.sun.xml.internal.ws.streaming.MtomStreamWriter;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.bind.util.JAXBResult;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class JAXBMessage extends AbstractMessageImpl
/*     */ {
/*     */   private HeaderList headers;
/*     */   private final Object jaxbObject;
/*     */   private final Bridge bridge;
/*     */   private String nsUri;
/*     */   private String localName;
/*     */   private XMLStreamBuffer infoset;
/*     */ 
/*     */   public static Message create(JAXBRIContext context, Object jaxbObject, SOAPVersion soapVersion, HeaderList headers, AttachmentSet attachments)
/*     */   {
/*  93 */     if (!context.hasSwaRef()) {
/*  94 */       return new JAXBMessage(context, jaxbObject, soapVersion, headers, attachments);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 102 */       MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/*     */ 
/* 104 */       Marshaller m = context.createMarshaller();
/* 105 */       AttachmentMarshallerImpl am = new AttachmentMarshallerImpl(attachments);
/* 106 */       m.setAttachmentMarshaller(am);
/* 107 */       am.cleanup();
/* 108 */       m.marshal(jaxbObject, xsb.createFromXMLStreamWriter());
/*     */ 
/* 111 */       return new StreamMessage(headers, attachments, xsb.readAsXMLStreamReader(), soapVersion);
/*     */     } catch (JAXBException e) {
/* 113 */       throw new WebServiceException(e);
/*     */     } catch (XMLStreamException e) {
/* 115 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Message create(JAXBRIContext context, Object jaxbObject, SOAPVersion soapVersion)
/*     */   {
/* 131 */     return create(context, jaxbObject, soapVersion, null, null);
/*     */   }
/*     */ 
/*     */   private JAXBMessage(JAXBRIContext context, Object jaxbObject, SOAPVersion soapVer, HeaderList headers, AttachmentSet attachments) {
/* 135 */     super(soapVer);
/* 136 */     this.bridge = new MarshallerBridge(context);
/* 137 */     this.jaxbObject = jaxbObject;
/* 138 */     this.headers = headers;
/* 139 */     this.attachmentSet = attachments;
/*     */   }
/*     */ 
/*     */   public static Message create(Bridge bridge, Object jaxbObject, SOAPVersion soapVer)
/*     */   {
/* 150 */     if (!bridge.getContext().hasSwaRef()) {
/* 151 */       return new JAXBMessage(bridge, jaxbObject, soapVer);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 159 */       MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/*     */ 
/* 161 */       AttachmentSetImpl attachments = new AttachmentSetImpl();
/* 162 */       AttachmentMarshallerImpl am = new AttachmentMarshallerImpl(attachments);
/* 163 */       bridge.marshal(jaxbObject, xsb.createFromXMLStreamWriter(), am);
/* 164 */       am.cleanup();
/*     */ 
/* 167 */       return new StreamMessage(null, attachments, xsb.readAsXMLStreamReader(), soapVer);
/*     */     } catch (JAXBException e) {
/* 169 */       throw new WebServiceException(e);
/*     */     } catch (XMLStreamException e) {
/* 171 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private JAXBMessage(Bridge bridge, Object jaxbObject, SOAPVersion soapVer) {
/* 176 */     super(soapVer);
/*     */ 
/* 178 */     this.bridge = bridge;
/* 179 */     this.jaxbObject = jaxbObject;
/* 180 */     QName tagName = bridge.getTypeReference().tagName;
/* 181 */     this.nsUri = tagName.getNamespaceURI();
/* 182 */     this.localName = tagName.getLocalPart();
/* 183 */     this.attachmentSet = new AttachmentSetImpl();
/*     */   }
/*     */ 
/*     */   public JAXBMessage(JAXBMessage that)
/*     */   {
/* 190 */     super(that);
/* 191 */     this.headers = that.headers;
/* 192 */     if (this.headers != null)
/* 193 */       this.headers = new HeaderList(this.headers);
/* 194 */     this.attachmentSet = that.attachmentSet;
/*     */ 
/* 196 */     this.jaxbObject = that.jaxbObject;
/* 197 */     this.bridge = that.bridge;
/*     */   }
/*     */ 
/*     */   public boolean hasHeaders() {
/* 201 */     return (this.headers != null) && (!this.headers.isEmpty());
/*     */   }
/*     */ 
/*     */   public HeaderList getHeaders() {
/* 205 */     if (this.headers == null)
/* 206 */       this.headers = new HeaderList();
/* 207 */     return this.headers;
/*     */   }
/*     */ 
/*     */   public String getPayloadLocalPart() {
/* 211 */     if (this.localName == null)
/* 212 */       sniff();
/* 213 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getPayloadNamespaceURI() {
/* 217 */     if (this.nsUri == null)
/* 218 */       sniff();
/* 219 */     return this.nsUri;
/*     */   }
/*     */ 
/*     */   public boolean hasPayload() {
/* 223 */     return true;
/*     */   }
/*     */ 
/*     */   private void sniff()
/*     */   {
/* 230 */     RootElementSniffer sniffer = new RootElementSniffer(false);
/*     */     try {
/* 232 */       this.bridge.marshal(this.jaxbObject, sniffer);
/*     */     }
/*     */     catch (JAXBException e)
/*     */     {
/* 240 */       this.nsUri = sniffer.getNsUri();
/* 241 */       this.localName = sniffer.getLocalName();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Source readPayloadAsSource() {
/* 246 */     return new JAXBBridgeSource(this.bridge, this.jaxbObject);
/*     */   }
/*     */ 
/*     */   public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 250 */     JAXBResult out = new JAXBResult(unmarshaller);
/*     */     try
/*     */     {
/* 253 */       out.getHandler().startDocument();
/* 254 */       this.bridge.marshal(this.jaxbObject, out);
/* 255 */       out.getHandler().endDocument();
/*     */     } catch (SAXException e) {
/* 257 */       throw new JAXBException(e);
/*     */     }
/* 259 */     return out.getResult();
/*     */   }
/*     */ 
/*     */   public XMLStreamReader readPayload() throws XMLStreamException {
/*     */     try {
/* 264 */       if (this.infoset == null) {
/* 265 */         XMLStreamBufferResult sbr = new XMLStreamBufferResult();
/* 266 */         this.bridge.marshal(this.jaxbObject, sbr);
/* 267 */         this.infoset = sbr.getXMLStreamBuffer();
/*     */       }
/* 269 */       XMLStreamReader reader = this.infoset.readAsXMLStreamReader();
/* 270 */       if (reader.getEventType() == 7)
/* 271 */         XMLStreamReaderUtil.nextElementContent(reader);
/* 272 */       return reader;
/*     */     }
/*     */     catch (JAXBException e) {
/* 275 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 284 */       if (fragment)
/* 285 */         contentHandler = new FragmentContentHandler(contentHandler);
/* 286 */       AttachmentMarshallerImpl am = new AttachmentMarshallerImpl(this.attachmentSet);
/* 287 */       this.bridge.marshal(this.jaxbObject, contentHandler, am);
/* 288 */       am.cleanup();
/*     */     }
/*     */     catch (JAXBException e)
/*     */     {
/* 293 */       throw new WebServiceException(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException
/*     */   {
/*     */     try {
/* 300 */       AttachmentMarshaller am = (sw instanceof MtomStreamWriter) ? ((MtomStreamWriter)sw).getAttachmentMarshaller() : new AttachmentMarshallerImpl(this.attachmentSet);
/*     */ 
/* 305 */       OutputStream os = XMLStreamWriterUtil.getOutputStream(sw);
/* 306 */       if (os != null)
/* 307 */         this.bridge.marshal(this.jaxbObject, os, sw.getNamespaceContext(), am);
/*     */       else {
/* 309 */         this.bridge.marshal(this.jaxbObject, sw, am);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (JAXBException e)
/*     */     {
/* 315 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Message copy() {
/* 320 */     return new JAXBMessage(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.jaxb.JAXBMessage
 * JD-Core Version:    0.6.2
 */