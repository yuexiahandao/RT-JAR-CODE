/*     */ package com.sun.xml.internal.ws.encoding.xml;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
/*     */ import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
/*     */ import com.sun.xml.internal.ws.encoding.MimeMultipartParser;
/*     */ import com.sun.xml.internal.ws.encoding.XMLHTTPBindingCodec;
/*     */ import com.sun.xml.internal.ws.message.AbstractMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.EmptyMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.MimeAttachmentSet;
/*     */ import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import com.sun.xml.internal.ws.util.StreamUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class XMLMessage
/*     */ {
/*     */   private static final int PLAIN_XML_FLAG = 1;
/*     */   private static final int MIME_MULTIPART_FLAG = 2;
/*     */   private static final int FI_ENCODED_FLAG = 16;
/*     */ 
/*     */   public static Message create(String ct, InputStream in, WSBinding binding)
/*     */   {
/*     */     Message data;
/*     */     try
/*     */     {
/*  81 */       in = StreamUtils.hasSomeData(in);
/*  82 */       if (in == null)
/*  83 */         return Messages.createEmpty(SOAPVersion.SOAP_11);
/*     */       Message data;
/*  86 */       if (ct != null) {
/*  87 */         com.sun.xml.internal.ws.encoding.ContentType contentType = new com.sun.xml.internal.ws.encoding.ContentType(ct);
/*  88 */         int contentTypeId = identifyContentType(contentType);
/*     */         Message data;
/*  89 */         if ((contentTypeId & 0x2) != 0) {
/*  90 */           data = new XMLMultiPart(ct, in, binding);
/*     */         }
/*     */         else
/*     */         {
/*     */           Message data;
/*  91 */           if ((contentTypeId & 0x1) != 0)
/*  92 */             data = new XmlContent(ct, in, binding);
/*     */           else
/*  94 */             data = new UnknownContent(ct, in);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  99 */         data = new UnknownContent("application/octet-stream", in);
/*     */       }
/*     */     } catch (Exception ex) {
/* 102 */       throw new WebServiceException(ex);
/*     */     }
/* 104 */     return data;
/*     */   }
/*     */ 
/*     */   public static Message create(Source source)
/*     */   {
/* 109 */     return source == null ? Messages.createEmpty(SOAPVersion.SOAP_11) : Messages.createUsingPayload(source, SOAPVersion.SOAP_11);
/*     */   }
/*     */ 
/*     */   public static Message create(DataSource ds, WSBinding binding)
/*     */   {
/*     */     try
/*     */     {
/* 116 */       return ds == null ? Messages.createEmpty(SOAPVersion.SOAP_11) : create(ds.getContentType(), ds.getInputStream(), binding);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 120 */       throw new WebServiceException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Message create(Exception e) {
/* 125 */     return new FaultMessage(SOAPVersion.SOAP_11);
/*     */   }
/*     */ 
/*     */   private static int getContentId(String ct)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       com.sun.xml.internal.ws.encoding.ContentType contentType = new com.sun.xml.internal.ws.encoding.ContentType(ct);
/* 134 */       return identifyContentType(contentType);
/*     */     } catch (Exception ex) {
/* 136 */       throw new WebServiceException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isFastInfoset(String ct)
/*     */   {
/* 144 */     return (getContentId(ct) & 0x10) != 0;
/*     */   }
/*     */ 
/*     */   public static int identifyContentType(com.sun.xml.internal.ws.encoding.ContentType contentType)
/*     */   {
/* 158 */     String primary = contentType.getPrimaryType();
/* 159 */     String sub = contentType.getSubType();
/*     */ 
/* 161 */     if ((primary.equalsIgnoreCase("multipart")) && (sub.equalsIgnoreCase("related"))) {
/* 162 */       String type = contentType.getParameter("type");
/* 163 */       if (type != null) {
/* 164 */         if (isXMLType(type))
/* 165 */           return 3;
/* 166 */         if (isFastInfosetType(type)) {
/* 167 */           return 18;
/*     */         }
/*     */       }
/* 170 */       return 0;
/* 171 */     }if (isXMLType(primary, sub))
/* 172 */       return 1;
/* 173 */     if (isFastInfosetType(primary, sub)) {
/* 174 */       return 16;
/*     */     }
/* 176 */     return 0;
/*     */   }
/*     */ 
/*     */   protected static boolean isXMLType(@NotNull String primary, @NotNull String sub) {
/* 180 */     return ((primary.equalsIgnoreCase("text")) && (sub.equalsIgnoreCase("xml"))) || ((primary.equalsIgnoreCase("application")) && (sub.equalsIgnoreCase("xml"))) || ((primary.equalsIgnoreCase("application")) && (sub.toLowerCase().endsWith("+xml")));
/*     */   }
/*     */ 
/*     */   protected static boolean isXMLType(String type)
/*     */   {
/* 186 */     String lowerType = type.toLowerCase();
/* 187 */     return (lowerType.startsWith("text/xml")) || (lowerType.startsWith("application/xml")) || ((lowerType.startsWith("application/")) && (lowerType.indexOf("+xml") != -1));
/*     */   }
/*     */ 
/*     */   protected static boolean isFastInfosetType(String primary, String sub)
/*     */   {
/* 193 */     return (primary.equalsIgnoreCase("application")) && (sub.equalsIgnoreCase("fastinfoset"));
/*     */   }
/*     */ 
/*     */   protected static boolean isFastInfosetType(String type) {
/* 197 */     return type.toLowerCase().startsWith("application/fastinfoset");
/*     */   }
/*     */ 
/*     */   public static DataSource getDataSource(Message msg, WSBinding binding)
/*     */   {
/* 564 */     if (msg == null)
/* 565 */       return null;
/* 566 */     if ((msg instanceof MessageDataSource)) {
/* 567 */       return ((MessageDataSource)msg).getDataSource();
/*     */     }
/* 569 */     AttachmentSet atts = msg.getAttachments();
/* 570 */     if ((atts != null) && (!atts.isEmpty())) {
/* 571 */       ByteArrayBuffer bos = new ByteArrayBuffer();
/*     */       try {
/* 573 */         Codec codec = new XMLHTTPBindingCodec(binding);
/* 574 */         com.sun.xml.internal.ws.api.pipe.ContentType ct = codec.getStaticContentType(new Packet(msg));
/* 575 */         codec.encode(new Packet(msg), bos);
/* 576 */         return createDataSource(ct.getContentType(), bos.newInputStream());
/*     */       } catch (IOException ioe) {
/* 578 */         throw new WebServiceException(ioe);
/*     */       }
/*     */     }
/*     */ 
/* 582 */     ByteArrayBuffer bos = new ByteArrayBuffer();
/* 583 */     XMLStreamWriter writer = XMLStreamWriterFactory.create(bos);
/*     */     try {
/* 585 */       msg.writePayloadTo(writer);
/* 586 */       writer.flush();
/*     */     } catch (XMLStreamException e) {
/* 588 */       throw new WebServiceException(e);
/*     */     }
/* 590 */     return createDataSource("text/xml", bos.newInputStream());
/*     */   }
/*     */ 
/*     */   public static DataSource createDataSource(String contentType, InputStream is)
/*     */   {
/* 596 */     return new XmlDataSource(contentType, is);
/*     */   }
/*     */ 
/*     */   private static class FaultMessage extends EmptyMessageImpl
/*     */   {
/*     */     public FaultMessage(SOAPVersion version)
/*     */     {
/* 468 */       super();
/*     */     }
/*     */ 
/*     */     public boolean isFault()
/*     */     {
/* 473 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface MessageDataSource {
/*     */     public abstract boolean hasUnconsumedDataSource();
/*     */ 
/*     */     public abstract DataSource getDataSource();
/*     */   }
/*     */ 
/*     */   public static class UnknownContent extends AbstractMessageImpl implements XMLMessage.MessageDataSource {
/*     */     private final DataSource ds;
/*     */     private final HeaderList headerList;
/*     */ 
/*     */     public UnknownContent(String ct, InputStream in) {
/* 489 */       this(XMLMessage.createDataSource(ct, in));
/*     */     }
/*     */ 
/*     */     public UnknownContent(DataSource ds) {
/* 493 */       super();
/* 494 */       this.ds = ds;
/* 495 */       this.headerList = new HeaderList();
/*     */     }
/*     */ 
/*     */     private UnknownContent(UnknownContent that)
/*     */     {
/* 502 */       super();
/* 503 */       this.ds = that.ds;
/* 504 */       this.headerList = HeaderList.copy(that.headerList);
/*     */     }
/*     */ 
/*     */     public boolean hasUnconsumedDataSource() {
/* 508 */       return true;
/*     */     }
/*     */ 
/*     */     public DataSource getDataSource() {
/* 512 */       assert (this.ds != null);
/* 513 */       return this.ds;
/*     */     }
/*     */ 
/*     */     protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException
/*     */     {
/* 518 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean hasHeaders() {
/* 522 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean isFault() {
/* 526 */       return false;
/*     */     }
/*     */ 
/*     */     public HeaderList getHeaders() {
/* 530 */       return this.headerList;
/*     */     }
/*     */ 
/*     */     public String getPayloadLocalPart() {
/* 534 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public String getPayloadNamespaceURI() {
/* 538 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean hasPayload() {
/* 542 */       return false;
/*     */     }
/*     */ 
/*     */     public Source readPayloadAsSource() {
/* 546 */       return null;
/*     */     }
/*     */ 
/*     */     public XMLStreamReader readPayload() throws XMLStreamException {
/* 550 */       throw new WebServiceException("There isn't XML payload. Shouldn't come here.");
/*     */     }
/*     */ 
/*     */     public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException
/*     */     {
/*     */     }
/*     */ 
/*     */     public Message copy() {
/* 558 */       return new UnknownContent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class XMLMultiPart extends AbstractMessageImpl
/*     */     implements XMLMessage.MessageDataSource
/*     */   {
/*     */     private final DataSource dataSource;
/*     */     private final StreamingAttachmentFeature feature;
/*     */     private Message delegate;
/* 347 */     private final HeaderList headerList = new HeaderList();
/*     */     private final WSBinding binding;
/*     */ 
/*     */     public XMLMultiPart(String contentType, InputStream is, WSBinding binding)
/*     */     {
/* 351 */       super();
/* 352 */       this.dataSource = XMLMessage.createDataSource(contentType, is);
/* 353 */       this.feature = ((StreamingAttachmentFeature)binding.getFeature(StreamingAttachmentFeature.class));
/* 354 */       this.binding = binding;
/*     */     }
/*     */ 
/*     */     private Message getMessage() {
/* 358 */       if (this.delegate == null) {
/*     */         MimeMultipartParser mpp;
/*     */         try {
/* 361 */           mpp = new MimeMultipartParser(this.dataSource.getInputStream(), this.dataSource.getContentType(), this.feature);
/*     */         }
/*     */         catch (IOException ioe) {
/* 364 */           throw new WebServiceException(ioe);
/*     */         }
/* 366 */         InputStream in = mpp.getRootPart().asInputStream();
/* 367 */         assert (in != null);
/* 368 */         this.delegate = new PayloadSourceMessage(this.headerList, new StreamSource(in), new MimeAttachmentSet(mpp), SOAPVersion.SOAP_11);
/*     */       }
/* 370 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     public boolean hasUnconsumedDataSource() {
/* 374 */       return this.delegate == null;
/*     */     }
/*     */ 
/*     */     public DataSource getDataSource() {
/* 378 */       return hasUnconsumedDataSource() ? this.dataSource : XMLMessage.getDataSource(getMessage(), this.binding);
/*     */     }
/*     */ 
/*     */     public boolean hasHeaders()
/*     */     {
/* 383 */       return false;
/*     */     }
/*     */     @NotNull
/*     */     public HeaderList getHeaders() {
/* 387 */       return this.headerList;
/*     */     }
/*     */ 
/*     */     public String getPayloadLocalPart() {
/* 391 */       return getMessage().getPayloadLocalPart();
/*     */     }
/*     */ 
/*     */     public String getPayloadNamespaceURI() {
/* 395 */       return getMessage().getPayloadNamespaceURI();
/*     */     }
/*     */ 
/*     */     public boolean hasPayload() {
/* 399 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean isFault() {
/* 403 */       return false;
/*     */     }
/*     */ 
/*     */     public Source readEnvelopeAsSource() {
/* 407 */       return getMessage().readEnvelopeAsSource();
/*     */     }
/*     */ 
/*     */     public Source readPayloadAsSource() {
/* 411 */       return getMessage().readPayloadAsSource();
/*     */     }
/*     */ 
/*     */     public SOAPMessage readAsSOAPMessage() throws SOAPException {
/* 415 */       return getMessage().readAsSOAPMessage();
/*     */     }
/*     */ 
/*     */     public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
/* 419 */       return getMessage().readAsSOAPMessage(packet, inbound);
/*     */     }
/*     */ 
/*     */     public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 423 */       return getMessage().readPayloadAsJAXB(unmarshaller);
/*     */     }
/*     */ 
/*     */     public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 427 */       return getMessage().readPayloadAsJAXB(bridge);
/*     */     }
/*     */ 
/*     */     public XMLStreamReader readPayload() throws XMLStreamException {
/* 431 */       return getMessage().readPayload();
/*     */     }
/*     */ 
/*     */     public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
/* 435 */       getMessage().writePayloadTo(sw);
/*     */     }
/*     */ 
/*     */     public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
/* 439 */       getMessage().writeTo(sw);
/*     */     }
/*     */ 
/*     */     public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 443 */       getMessage().writeTo(contentHandler, errorHandler);
/*     */     }
/*     */ 
/*     */     public Message copy() {
/* 447 */       return getMessage().copy();
/*     */     }
/*     */ 
/*     */     protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 451 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean isOneWay(@NotNull WSDLPort port)
/*     */     {
/* 456 */       return false;
/*     */     }
/*     */     @NotNull
/*     */     public AttachmentSet getAttachments() {
/* 460 */       return getMessage().getAttachments();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class XmlContent extends AbstractMessageImpl
/*     */     implements XMLMessage.MessageDataSource
/*     */   {
/*     */     private final XMLMessage.XmlDataSource dataSource;
/*     */     private boolean consumed;
/*     */     private Message delegate;
/*     */     private final HeaderList headerList;
/*     */     private final WSBinding binding;
/*     */ 
/*     */     public XmlContent(String ct, InputStream in, WSBinding binding)
/*     */     {
/* 235 */       super();
/* 236 */       this.dataSource = new XMLMessage.XmlDataSource(ct, in);
/* 237 */       this.headerList = new HeaderList();
/* 238 */       this.binding = binding;
/*     */     }
/*     */ 
/*     */     private Message getMessage() {
/* 242 */       if (this.delegate == null) {
/* 243 */         InputStream in = this.dataSource.getInputStream();
/* 244 */         assert (in != null);
/* 245 */         this.delegate = Messages.createUsingPayload(new StreamSource(in), SOAPVersion.SOAP_11);
/* 246 */         this.consumed = true;
/*     */       }
/* 248 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     public boolean hasUnconsumedDataSource() {
/* 252 */       return (!this.dataSource.consumed()) && (!this.consumed);
/*     */     }
/*     */ 
/*     */     public DataSource getDataSource() {
/* 256 */       return hasUnconsumedDataSource() ? this.dataSource : XMLMessage.getDataSource(getMessage(), this.binding);
/*     */     }
/*     */ 
/*     */     public boolean hasHeaders()
/*     */     {
/* 261 */       return false;
/*     */     }
/*     */     @NotNull
/*     */     public HeaderList getHeaders() {
/* 265 */       return this.headerList;
/*     */     }
/*     */ 
/*     */     public String getPayloadLocalPart() {
/* 269 */       return getMessage().getPayloadLocalPart();
/*     */     }
/*     */ 
/*     */     public String getPayloadNamespaceURI() {
/* 273 */       return getMessage().getPayloadNamespaceURI();
/*     */     }
/*     */ 
/*     */     public boolean hasPayload() {
/* 277 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean isFault() {
/* 281 */       return false;
/*     */     }
/*     */ 
/*     */     public Source readEnvelopeAsSource() {
/* 285 */       return getMessage().readEnvelopeAsSource();
/*     */     }
/*     */ 
/*     */     public Source readPayloadAsSource() {
/* 289 */       return getMessage().readPayloadAsSource();
/*     */     }
/*     */ 
/*     */     public SOAPMessage readAsSOAPMessage() throws SOAPException {
/* 293 */       return getMessage().readAsSOAPMessage();
/*     */     }
/*     */ 
/*     */     public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
/* 297 */       return getMessage().readAsSOAPMessage(packet, inbound);
/*     */     }
/*     */ 
/*     */     public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/* 301 */       return getMessage().readPayloadAsJAXB(unmarshaller);
/*     */     }
/*     */ 
/*     */     public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
/* 305 */       return getMessage().readPayloadAsJAXB(bridge);
/*     */     }
/*     */ 
/*     */     public XMLStreamReader readPayload() throws XMLStreamException {
/* 309 */       return getMessage().readPayload();
/*     */     }
/*     */ 
/*     */     public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException
/*     */     {
/* 314 */       getMessage().writePayloadTo(sw);
/*     */     }
/*     */ 
/*     */     public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
/* 318 */       getMessage().writeTo(sw);
/*     */     }
/*     */ 
/*     */     public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
/* 322 */       getMessage().writeTo(contentHandler, errorHandler);
/*     */     }
/*     */ 
/*     */     public Message copy() {
/* 326 */       return getMessage().copy();
/*     */     }
/*     */ 
/*     */     protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
/* 330 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class XmlDataSource
/*     */     implements DataSource
/*     */   {
/*     */     private final String contentType;
/*     */     private final InputStream is;
/*     */     private boolean consumed;
/*     */ 
/*     */     XmlDataSource(String contentType, InputStream is)
/*     */     {
/* 605 */       this.contentType = contentType;
/* 606 */       this.is = is;
/*     */     }
/*     */ 
/*     */     public boolean consumed() {
/* 610 */       return this.consumed;
/*     */     }
/*     */ 
/*     */     public InputStream getInputStream() {
/* 614 */       this.consumed = (!this.consumed);
/* 615 */       return this.is;
/*     */     }
/*     */ 
/*     */     public OutputStream getOutputStream() {
/* 619 */       return null;
/*     */     }
/*     */ 
/*     */     public String getContentType() {
/* 623 */       return this.contentType;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 627 */       return "";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.xml.XMLMessage
 * JD-Core Version:    0.6.2
 */