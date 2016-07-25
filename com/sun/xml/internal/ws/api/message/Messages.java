/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
/*     */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codecs;
/*     */ import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import com.sun.xml.internal.ws.message.DOMMessage;
/*     */ import com.sun.xml.internal.ws.message.EmptyMessageImpl;
/*     */ import com.sun.xml.internal.ws.message.ProblemActionHeader;
/*     */ import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
/*     */ import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
/*     */ import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
/*     */ import com.sun.xml.internal.ws.message.source.ProtocolSourceMessage;
/*     */ import com.sun.xml.internal.ws.message.stream.PayloadStreamReaderMessage;
/*     */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderException;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.SOAPConstants;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class Messages
/*     */ {
/*     */   public static Message create(JAXBRIContext context, Object jaxbObject, SOAPVersion soapVersion)
/*     */   {
/* 108 */     return JAXBMessage.create(context, jaxbObject, soapVersion);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Message create(Marshaller marshaller, Object jaxbObject, SOAPVersion soapVersion)
/*     */   {
/* 116 */     return create(((MarshallerImpl)marshaller).getContext(), jaxbObject, soapVersion);
/*     */   }
/*     */ 
/*     */   public static Message create(SOAPMessage saaj)
/*     */   {
/* 133 */     return new SAAJMessage(saaj);
/*     */   }
/*     */ 
/*     */   public static Message createUsingPayload(Source payload, SOAPVersion ver)
/*     */   {
/* 149 */     if ((payload instanceof DOMSource)) {
/* 150 */       if (((DOMSource)payload).getNode() == null)
/* 151 */         return new EmptyMessageImpl(ver);
/*     */     }
/* 153 */     else if ((payload instanceof StreamSource)) {
/* 154 */       StreamSource ss = (StreamSource)payload;
/* 155 */       if ((ss.getInputStream() == null) && (ss.getReader() == null) && (ss.getSystemId() == null))
/* 156 */         return new EmptyMessageImpl(ver);
/*     */     }
/* 158 */     else if ((payload instanceof SAXSource)) {
/* 159 */       SAXSource ss = (SAXSource)payload;
/* 160 */       if ((ss.getInputSource() == null) && (ss.getXMLReader() == null)) {
/* 161 */         return new EmptyMessageImpl(ver);
/*     */       }
/*     */     }
/* 164 */     return new PayloadSourceMessage(payload, ver);
/*     */   }
/*     */ 
/*     */   public static Message createUsingPayload(XMLStreamReader payload, SOAPVersion ver)
/*     */   {
/* 180 */     return new PayloadStreamReaderMessage(payload, ver);
/*     */   }
/*     */ 
/*     */   public static Message createUsingPayload(Element payload, SOAPVersion ver)
/*     */   {
/* 195 */     return new DOMMessage(ver, payload);
/*     */   }
/*     */ 
/*     */   public static Message create(Element soapEnvelope)
/*     */   {
/* 206 */     SOAPVersion ver = SOAPVersion.fromNsUri(soapEnvelope.getNamespaceURI());
/*     */ 
/* 208 */     Element header = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Header");
/* 209 */     HeaderList headers = null;
/* 210 */     if (header != null) {
/* 211 */       for (Node n = header.getFirstChild(); n != null; n = n.getNextSibling()) {
/* 212 */         if (n.getNodeType() == 1) {
/* 213 */           if (headers == null)
/* 214 */             headers = new HeaderList();
/* 215 */           headers.add(Headers.create((Element)n));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 221 */     Element body = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Body");
/* 222 */     if (body == null)
/* 223 */       throw new WebServiceException("Message doesn't have <S:Body> " + soapEnvelope);
/* 224 */     Element payload = DOMUtil.getFirstChild(soapEnvelope, ver.nsUri, "Body");
/*     */ 
/* 226 */     if (payload == null) {
/* 227 */       return new EmptyMessageImpl(headers, new AttachmentSetImpl(), ver);
/*     */     }
/* 229 */     return new DOMMessage(ver, headers, payload);
/*     */   }
/*     */ 
/*     */   public static Message create(Source envelope, SOAPVersion soapVersion)
/*     */   {
/* 244 */     return new ProtocolSourceMessage(envelope, soapVersion);
/*     */   }
/*     */ 
/*     */   public static Message createEmpty(SOAPVersion soapVersion)
/*     */   {
/* 252 */     return new EmptyMessageImpl(soapVersion);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Message create(@NotNull XMLStreamReader reader)
/*     */   {
/* 264 */     if (reader.getEventType() != 1)
/* 265 */       XMLStreamReaderUtil.nextElementContent(reader);
/* 266 */     assert (reader.getEventType() == 1) : reader.getEventType();
/*     */ 
/* 268 */     SOAPVersion ver = SOAPVersion.fromNsUri(reader.getNamespaceURI());
/*     */ 
/* 270 */     return Codecs.createSOAPEnvelopeXmlCodec(ver).decode(reader);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Message create(@NotNull XMLStreamBuffer xsb)
/*     */   {
/*     */     try
/*     */     {
/* 286 */       return create(xsb.readAsXMLStreamReader());
/*     */     } catch (XMLStreamException e) {
/* 288 */       throw new XMLStreamReaderException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Message create(Throwable t, SOAPVersion soapVersion)
/*     */   {
/* 304 */     return SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, null, t);
/*     */   }
/*     */ 
/*     */   public static Message create(SOAPFault fault)
/*     */   {
/* 322 */     SOAPVersion ver = SOAPVersion.fromNsUri(fault.getNamespaceURI());
/* 323 */     return new DOMMessage(ver, fault);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Message createAddressingFaultMessage(WSBinding binding, QName missingHeader)
/*     */   {
/* 331 */     return createAddressingFaultMessage(binding, null, missingHeader);
/*     */   }
/*     */ 
/*     */   public static Message createAddressingFaultMessage(WSBinding binding, Packet p, QName missingHeader)
/*     */   {
/* 347 */     AddressingVersion av = binding.getAddressingVersion();
/* 348 */     if (av == null)
/*     */     {
/* 350 */       throw new WebServiceException(AddressingMessages.ADDRESSING_SHOULD_BE_ENABLED());
/*     */     }
/* 352 */     WsaTubeHelper helper = av.getWsaHelper(null, null, binding);
/* 353 */     return create(helper.newMapRequiredFault(new MissingAddressingHeaderException(missingHeader, p)));
/*     */   }
/*     */ 
/*     */   public static Message create(@NotNull String unsupportedAction, @NotNull AddressingVersion av, @NotNull SOAPVersion sv)
/*     */   {
/* 367 */     QName subcode = av.actionNotSupportedTag;
/* 368 */     String faultstring = String.format(av.actionNotSupportedText, new Object[] { unsupportedAction });
/*     */     Message faultMessage;
/*     */     try
/*     */     {
/*     */       SOAPFault fault;
/* 373 */       if (sv == SOAPVersion.SOAP_12) {
/* 374 */         SOAPFault fault = SOAPVersion.SOAP_12.saajSoapFactory.createFault();
/* 375 */         fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
/* 376 */         fault.appendFaultSubcode(subcode);
/* 377 */         Detail detail = fault.addDetail();
/* 378 */         SOAPElement se = detail.addChildElement(av.problemActionTag);
/* 379 */         se = se.addChildElement(av.actionTag);
/* 380 */         se.addTextNode(unsupportedAction);
/*     */       } else {
/* 382 */         fault = SOAPVersion.SOAP_11.saajSoapFactory.createFault();
/* 383 */         fault.setFaultCode(subcode);
/*     */       }
/* 385 */       fault.setFaultString(faultstring);
/*     */ 
/* 387 */       faultMessage = SOAPFaultBuilder.createSOAPFaultMessage(sv, fault);
/* 388 */       if (sv == SOAPVersion.SOAP_11)
/* 389 */         faultMessage.getHeaders().add(new ProblemActionHeader(unsupportedAction, av));
/*     */     }
/*     */     catch (SOAPException e) {
/* 392 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 395 */     return faultMessage;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Message create(@NotNull SOAPVersion soapVersion, @NotNull ProtocolException pex, @Nullable QName faultcode)
/*     */   {
/* 408 */     return SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, pex, faultcode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.Messages
 * JD-Core Version:    0.6.2
 */