/*      */ package com.sun.xml.internal.ws.api.addressing;
/*      */ 
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.istack.internal.Nullable;
/*      */ import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
/*      */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*      */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*      */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
/*      */ import com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor;
/*      */ import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor;
/*      */ import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
/*      */ import com.sun.xml.internal.ws.addressing.EndpointReferenceUtil;
/*      */ import com.sun.xml.internal.ws.addressing.WSEPRExtension;
/*      */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*      */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
/*      */ import com.sun.xml.internal.ws.api.message.Header;
/*      */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*      */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
/*      */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*      */ import com.sun.xml.internal.ws.resources.AddressingMessages;
/*      */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*      */ import com.sun.xml.internal.ws.spi.ProviderImpl;
/*      */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*      */ import com.sun.xml.internal.ws.util.DOMUtil;
/*      */ import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
/*      */ import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
/*      */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*      */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringWriter;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import javax.xml.bind.JAXBContext;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.stream.XMLStreamReader;
/*      */ import javax.xml.stream.XMLStreamWriter;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.sax.SAXSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import javax.xml.ws.Dispatch;
/*      */ import javax.xml.ws.EndpointReference;
/*      */ import javax.xml.ws.Service;
/*      */ import javax.xml.ws.Service.Mode;
/*      */ import javax.xml.ws.WebServiceException;
/*      */ import javax.xml.ws.WebServiceFeature;
/*      */ import org.w3c.dom.Element;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.XMLFilterImpl;
/*      */ 
/*      */ public final class WSEndpointReference
/*      */   implements WSDLExtension
/*      */ {
/*      */   private final XMLStreamBuffer infoset;
/*      */   private final AddressingVersion version;
/*      */ 
/*      */   @NotNull
/*      */   private Header[] referenceParameters;
/*      */ 
/*      */   @NotNull
/*      */   private String address;
/*      */ 
/*      */   @NotNull
/*      */   private QName rootElement;
/*  953 */   private static final OutboundReferenceParameterHeader[] EMPTY_ARRAY = new OutboundReferenceParameterHeader[0];
/*      */   private Map<QName, EPRExtension> rootEprExtensions;
/*      */ 
/*      */   public WSEndpointReference(EndpointReference epr, AddressingVersion version)
/*      */   {
/*      */     try
/*      */     {
/*  116 */       MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/*  117 */       epr.writeTo(new XMLStreamBufferResult(xsb));
/*  118 */       this.infoset = xsb;
/*  119 */       this.version = version;
/*  120 */       this.rootElement = new QName("EndpointReference", version.nsUri);
/*  121 */       parse();
/*      */     } catch (XMLStreamException e) {
/*  123 */       throw new WebServiceException(ClientMessages.FAILED_TO_PARSE_EPR(epr), e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(EndpointReference epr)
/*      */   {
/*  135 */     this(epr, AddressingVersion.fromSpecClass(epr.getClass()));
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(XMLStreamBuffer infoset, AddressingVersion version)
/*      */   {
/*      */     try
/*      */     {
/*  143 */       this.infoset = infoset;
/*  144 */       this.version = version;
/*  145 */       this.rootElement = new QName("EndpointReference", version.nsUri);
/*  146 */       parse();
/*      */     }
/*      */     catch (XMLStreamException e) {
/*  149 */       throw new AssertionError(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(InputStream infoset, AddressingVersion version)
/*      */     throws XMLStreamException
/*      */   {
/*  157 */     this(XMLStreamReaderFactory.create(null, infoset, false), version);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(XMLStreamReader in, AddressingVersion version)
/*      */     throws XMLStreamException
/*      */   {
/*  165 */     this(XMLStreamBuffer.createNewBufferFromXMLStreamReader(in), version);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(URL address, AddressingVersion version)
/*      */   {
/*  172 */     this(address.toExternalForm(), version);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(URI address, AddressingVersion version)
/*      */   {
/*  179 */     this(address.toString(), version);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(String address, AddressingVersion version)
/*      */   {
/*  186 */     this.infoset = createBufferFromAddress(address, version);
/*  187 */     this.version = version;
/*  188 */     this.address = address;
/*  189 */     this.rootElement = new QName("EndpointReference", version.nsUri);
/*  190 */     this.referenceParameters = EMPTY_ARRAY;
/*      */   }
/*      */ 
/*      */   private static XMLStreamBuffer createBufferFromAddress(String address, AddressingVersion version) {
/*      */     try {
/*  195 */       MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/*  196 */       StreamWriterBufferCreator w = new StreamWriterBufferCreator(xsb);
/*  197 */       w.writeStartDocument();
/*  198 */       w.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
/*      */ 
/*  200 */       w.writeNamespace(version.getPrefix(), version.nsUri);
/*  201 */       w.writeStartElement(version.getPrefix(), version.eprType.address, version.nsUri);
/*  202 */       w.writeCharacters(address);
/*  203 */       w.writeEndElement();
/*  204 */       w.writeEndElement();
/*  205 */       w.writeEndDocument();
/*  206 */       w.close();
/*  207 */       return xsb;
/*      */     }
/*      */     catch (XMLStreamException e) {
/*  210 */       throw new AssertionError(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable List<Element> referenceParameters)
/*      */   {
/*  229 */     this(version, address, service, port, portType, metadata, wsdlAddress, null, referenceParameters, null, null);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable List<Element> referenceParameters, @Nullable Collection<EPRExtension> extns, @Nullable Map<QName, String> attributes)
/*      */   {
/*  248 */     this(createBufferFromData(version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, null, extns, attributes), version);
/*      */   }
/*      */ 
/*      */   public WSEndpointReference(@NotNull AddressingVersion version, @NotNull String address, @Nullable QName service, @Nullable QName port, @Nullable QName portType, @Nullable List<Element> metadata, @Nullable String wsdlAddress, @Nullable String wsdlTargetNamepsace, @Nullable List<Element> referenceParameters, @Nullable List<Element> elements, @Nullable Map<QName, String> attributes)
/*      */   {
/*  270 */     this(createBufferFromData(version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamepsace, elements, attributes), version);
/*      */   }
/*      */ 
/*      */   private static XMLStreamBuffer createBufferFromData(AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable List<Element> elements, @Nullable Map<QName, String> attributes)
/*      */   {
/*  278 */     StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
/*      */     try
/*      */     {
/*  281 */       writer.writeStartDocument();
/*  282 */       writer.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
/*  283 */       writer.writeNamespace(version.getPrefix(), version.nsUri);
/*      */ 
/*  285 */       writePartialEPRInfoset(writer, version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace, attributes);
/*      */ 
/*  289 */       if (elements != null) {
/*  290 */         for (Element e : elements) {
/*  291 */           DOMUtil.serializeNode(e, writer);
/*      */         }
/*      */       }
/*  294 */       writer.writeEndElement();
/*  295 */       writer.writeEndDocument();
/*  296 */       writer.flush();
/*      */ 
/*  298 */       return writer.getXMLStreamBuffer();
/*      */     } catch (XMLStreamException e) {
/*  300 */       throw new WebServiceException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static XMLStreamBuffer createBufferFromData(AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable Collection<EPRExtension> extns, @Nullable Map<QName, String> attributes)
/*      */   {
/*  307 */     StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
/*      */     try
/*      */     {
/*  310 */       writer.writeStartDocument();
/*  311 */       writer.writeStartElement(version.getPrefix(), "EndpointReference", version.nsUri);
/*  312 */       writer.writeNamespace(version.getPrefix(), version.nsUri);
/*      */ 
/*  314 */       writePartialEPRInfoset(writer, version, address, referenceParameters, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace, attributes);
/*      */ 
/*  318 */       if (extns != null) {
/*  319 */         for (EPRExtension e : extns) {
/*  320 */           XMLStreamReaderToXMLStreamWriter c = new XMLStreamReaderToXMLStreamWriter();
/*  321 */           XMLStreamReader r = e.readAsXMLStreamReader();
/*  322 */           c.bridge(r, writer);
/*  323 */           XMLStreamReaderFactory.recycle(r);
/*      */         }
/*      */       }
/*      */ 
/*  327 */       writer.writeEndElement();
/*  328 */       writer.writeEndDocument();
/*  329 */       writer.flush();
/*      */ 
/*  331 */       return writer.getXMLStreamBuffer();
/*      */     } catch (XMLStreamException e) {
/*  333 */       throw new WebServiceException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void writePartialEPRInfoset(StreamWriterBufferCreator writer, AddressingVersion version, String address, List<Element> referenceParameters, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace, @Nullable Map<QName, String> attributes)
/*      */     throws XMLStreamException
/*      */   {
/*  340 */     if (attributes != null) {
/*  341 */       for (Map.Entry entry : attributes.entrySet()) {
/*  342 */         QName qname = (QName)entry.getKey();
/*  343 */         writer.writeAttribute(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart(), (String)entry.getValue());
/*      */       }
/*      */     }
/*      */ 
/*  347 */     writer.writeStartElement(version.getPrefix(), version.eprType.address, version.nsUri);
/*  348 */     writer.writeCharacters(address);
/*  349 */     writer.writeEndElement();
/*  350 */     if (referenceParameters != null) {
/*  351 */       writer.writeStartElement(version.getPrefix(), version.eprType.referenceParameters, version.nsUri);
/*  352 */       for (Element e : referenceParameters)
/*  353 */         DOMUtil.serializeNode(e, writer);
/*  354 */       writer.writeEndElement();
/*      */     }
/*      */ 
/*  357 */     switch (4.$SwitchMap$com$sun$xml$internal$ws$api$addressing$AddressingVersion[version.ordinal()]) {
/*      */     case 1:
/*  359 */       writeW3CMetaData(writer, service, port, portType, metadata, wsdlAddress, wsdlTargetNamespace);
/*  360 */       break;
/*      */     case 2:
/*  363 */       writeMSMetaData(writer, service, port, portType, metadata);
/*  364 */       if (wsdlAddress != null)
/*      */       {
/*  367 */         writer.writeStartElement(MemberSubmissionAddressingConstants.MEX_METADATA.getPrefix(), MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart(), MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI());
/*      */ 
/*  370 */         writer.writeStartElement(MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getPrefix(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getLocalPart(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getNamespaceURI());
/*      */ 
/*  373 */         writer.writeAttribute("Dialect", "http://schemas.xmlsoap.org/wsdl/");
/*      */ 
/*  376 */         writeWsdl(writer, service, wsdlAddress);
/*      */ 
/*  378 */         writer.writeEndElement();
/*  379 */         writer.writeEndElement();
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void writeW3CMetaData(StreamWriterBufferCreator writer, QName service, QName port, QName portType, List<Element> metadata, String wsdlAddress, String wsdlTargetNamespace)
/*      */     throws XMLStreamException
/*      */   {
/*  393 */     writer.writeStartElement(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.eprType.wsdlMetadata.getLocalPart(), AddressingVersion.W3C.nsUri);
/*      */ 
/*  395 */     writer.writeNamespace(AddressingVersion.W3C.getWsdlPrefix(), AddressingVersion.W3C.wsdlNsUri);
/*      */ 
/*  398 */     if (wsdlAddress != null) {
/*  399 */       writeWsdliLocation(writer, service, wsdlAddress, wsdlTargetNamespace);
/*      */     }
/*      */ 
/*  402 */     if (portType != null) {
/*  403 */       writer.writeStartElement("wsam", AddressingVersion.W3C.eprType.portTypeName, "http://www.w3.org/2007/05/addressing/metadata");
/*      */ 
/*  406 */       String portTypePrefix = portType.getPrefix();
/*  407 */       if ((portTypePrefix == null) || (portTypePrefix.equals("")))
/*      */       {
/*  409 */         portTypePrefix = "wsns";
/*      */       }
/*  411 */       writer.writeNamespace(portTypePrefix, portType.getNamespaceURI());
/*  412 */       writer.writeCharacters(portTypePrefix + ":" + portType.getLocalPart());
/*  413 */       writer.writeEndElement();
/*      */     }
/*  415 */     if (service != null)
/*      */     {
/*  417 */       if ((!service.getNamespaceURI().equals("")) && (!service.getLocalPart().equals(""))) {
/*  418 */         writer.writeStartElement("wsam", AddressingVersion.W3C.eprType.serviceName, "http://www.w3.org/2007/05/addressing/metadata");
/*      */ 
/*  421 */         String servicePrefix = service.getPrefix();
/*  422 */         if ((servicePrefix == null) || (servicePrefix.equals("")))
/*      */         {
/*  424 */           servicePrefix = "wsns";
/*      */         }
/*  426 */         writer.writeNamespace(servicePrefix, service.getNamespaceURI());
/*  427 */         if (port != null) {
/*  428 */           writer.writeAttribute(AddressingVersion.W3C.eprType.portName, port.getLocalPart());
/*      */         }
/*  430 */         writer.writeCharacters(servicePrefix + ":" + service.getLocalPart());
/*  431 */         writer.writeEndElement();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  441 */     if (metadata != null) {
/*  442 */       for (Element e : metadata)
/*  443 */         DOMUtil.serializeNode(e, writer);
/*      */     }
/*  445 */     writer.writeEndElement();
/*      */   }
/*      */ 
/*      */   private static void writeWsdliLocation(StreamWriterBufferCreator writer, QName service, String wsdlAddress, String wsdlTargetNamespace)
/*      */     throws XMLStreamException
/*      */   {
/*  457 */     String wsdliLocation = "";
/*  458 */     if (wsdlTargetNamespace != null)
/*  459 */       wsdliLocation = wsdlTargetNamespace + " ";
/*  460 */     else if (service != null)
/*  461 */       wsdliLocation = service.getNamespaceURI() + " ";
/*      */     else {
/*  463 */       throw new WebServiceException("WSDL target Namespace cannot be resolved");
/*      */     }
/*  465 */     wsdliLocation = wsdliLocation + wsdlAddress;
/*      */ 
/*  467 */     writer.writeAttribute("wsdli", "http://www.w3.org/ns/wsdl-instance", "wsdlLocation", wsdliLocation);
/*      */   }
/*      */ 
/*      */   private static void writeMSMetaData(StreamWriterBufferCreator writer, QName service, QName port, QName portType, List<Element> metadata)
/*      */     throws XMLStreamException
/*      */   {
/*  479 */     if (portType != null)
/*      */     {
/*  481 */       writer.writeStartElement(AddressingVersion.MEMBER.getPrefix(), AddressingVersion.MEMBER.eprType.portTypeName, AddressingVersion.MEMBER.nsUri);
/*      */ 
/*  486 */       String portTypePrefix = portType.getPrefix();
/*  487 */       if ((portTypePrefix == null) || (portTypePrefix.equals("")))
/*      */       {
/*  489 */         portTypePrefix = "wsns";
/*      */       }
/*  491 */       writer.writeNamespace(portTypePrefix, portType.getNamespaceURI());
/*  492 */       writer.writeCharacters(portTypePrefix + ":" + portType.getLocalPart());
/*  493 */       writer.writeEndElement();
/*      */     }
/*      */ 
/*  496 */     if ((service != null) && 
/*  497 */       (!service.getNamespaceURI().equals("")) && (!service.getLocalPart().equals(""))) {
/*  498 */       writer.writeStartElement(AddressingVersion.MEMBER.getPrefix(), AddressingVersion.MEMBER.eprType.serviceName, AddressingVersion.MEMBER.nsUri);
/*      */ 
/*  501 */       String servicePrefix = service.getPrefix();
/*  502 */       if ((servicePrefix == null) || (servicePrefix.equals("")))
/*      */       {
/*  504 */         servicePrefix = "wsns";
/*      */       }
/*  506 */       writer.writeNamespace(servicePrefix, service.getNamespaceURI());
/*  507 */       if (port != null) {
/*  508 */         writer.writeAttribute(AddressingVersion.MEMBER.eprType.portName, port.getLocalPart());
/*      */       }
/*      */ 
/*  511 */       writer.writeCharacters(servicePrefix + ":" + service.getLocalPart());
/*  512 */       writer.writeEndElement();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void writeWsdl(StreamWriterBufferCreator writer, QName service, String wsdlAddress)
/*      */     throws XMLStreamException
/*      */   {
/*  519 */     writer.writeStartElement("wsdl", WSDLConstants.QNAME_DEFINITIONS.getLocalPart(), "http://schemas.xmlsoap.org/wsdl/");
/*      */ 
/*  522 */     writer.writeNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
/*  523 */     writer.writeStartElement("wsdl", WSDLConstants.QNAME_IMPORT.getLocalPart(), "http://schemas.xmlsoap.org/wsdl/");
/*      */ 
/*  526 */     writer.writeAttribute("namespace", service.getNamespaceURI());
/*  527 */     writer.writeAttribute("location", wsdlAddress);
/*  528 */     writer.writeEndElement();
/*  529 */     writer.writeEndElement();
/*      */   }
/*      */ 
/*      */   @Nullable
/*      */   public static WSEndpointReference create(@Nullable EndpointReference epr)
/*      */   {
/*  543 */     if (epr != null) {
/*  544 */       return new WSEndpointReference(epr);
/*      */     }
/*  546 */     return null;
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public WSEndpointReference createWithAddress(@NotNull URI newAddress)
/*      */   {
/*  553 */     return createWithAddress(newAddress.toString());
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public WSEndpointReference createWithAddress(@NotNull URL newAddress)
/*      */   {
/*  560 */     return createWithAddress(newAddress.toString());
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public WSEndpointReference createWithAddress(@NotNull final String newAddress)
/*      */   {
/*  580 */     MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
/*  581 */     XMLFilterImpl filter = new XMLFilterImpl() {
/*  582 */       private boolean inAddress = false;
/*      */ 
/*  584 */       public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException { if ((localName.equals("Address")) && (uri.equals(WSEndpointReference.this.version.nsUri)))
/*  585 */           this.inAddress = true;
/*  586 */         super.startElement(uri, localName, qName, atts); }
/*      */ 
/*      */       public void characters(char[] ch, int start, int length) throws SAXException
/*      */       {
/*  590 */         if (!this.inAddress)
/*  591 */           super.characters(ch, start, length);
/*      */       }
/*      */ 
/*      */       public void endElement(String uri, String localName, String qName) throws SAXException
/*      */       {
/*  596 */         if (this.inAddress)
/*  597 */           super.characters(newAddress.toCharArray(), 0, newAddress.length());
/*  598 */         this.inAddress = false;
/*  599 */         super.endElement(uri, localName, qName);
/*      */       }
/*      */     };
/*  602 */     filter.setContentHandler(xsb.createFromSAXBufferCreator());
/*      */     try {
/*  604 */       this.infoset.writeTo(filter, false);
/*      */     } catch (SAXException e) {
/*  606 */       throw new AssertionError(e);
/*      */     }
/*      */ 
/*  609 */     return new WSEndpointReference(xsb, this.version);
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public EndpointReference toSpec()
/*      */   {
/*  622 */     return ProviderImpl.INSTANCE.readEndpointReference(asSource("EndpointReference"));
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public <T extends EndpointReference> T toSpec(Class<T> clazz)
/*      */   {
/*  632 */     return EndpointReferenceUtil.transform(clazz, toSpec());
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public <T> T getPort(@NotNull Service jaxwsService, @NotNull Class<T> serviceEndpointInterface, WebServiceFeature[] features)
/*      */   {
/*  648 */     return jaxwsService.getPort(toSpec(), serviceEndpointInterface, features);
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public <T> Dispatch<T> createDispatch(@NotNull Service jaxwsService, @NotNull Class<T> type, @NotNull Service.Mode mode, WebServiceFeature[] features)
/*      */   {
/*  667 */     return jaxwsService.createDispatch(toSpec(), type, mode, features);
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public Dispatch<Object> createDispatch(@NotNull Service jaxwsService, @NotNull JAXBContext context, @NotNull Service.Mode mode, WebServiceFeature[] features)
/*      */   {
/*  686 */     return jaxwsService.createDispatch(toSpec(), context, mode, features);
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public AddressingVersion getVersion()
/*      */   {
/*  693 */     return this.version;
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public String getAddress()
/*      */   {
/*  700 */     return this.address;
/*      */   }
/*      */ 
/*      */   public boolean isAnonymous()
/*      */   {
/*  707 */     return this.address.equals(this.version.anonymousUri);
/*      */   }
/*      */ 
/*      */   public boolean isNone()
/*      */   {
/*  715 */     return this.address.equals(this.version.noneUri);
/*      */   }
/*      */ 
/*      */   private void parse()
/*      */     throws XMLStreamException
/*      */   {
/*  725 */     StreamReaderBufferProcessor xsr = this.infoset.readAsXMLStreamReader();
/*      */ 
/*  728 */     if (xsr.getEventType() == 7)
/*  729 */       xsr.nextTag();
/*  730 */     assert (xsr.getEventType() == 1);
/*      */ 
/*  732 */     String rootLocalName = xsr.getLocalName();
/*  733 */     if (!xsr.getNamespaceURI().equals(this.version.nsUri)) {
/*  734 */       throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(this.version.nsUri, xsr.getNamespaceURI()));
/*      */     }
/*      */ 
/*  737 */     this.rootElement = new QName(xsr.getNamespaceURI(), rootLocalName);
/*      */ 
/*  740 */     List marks = null;
/*      */ 
/*  742 */     while (xsr.nextTag() == 1) {
/*  743 */       String localName = xsr.getLocalName();
/*  744 */       if (this.version.isReferenceParameter(localName))
/*      */       {
/*      */         XMLStreamBuffer mark;
/*  746 */         while ((mark = xsr.nextTagAndMark()) != null) {
/*  747 */           if (marks == null) {
/*  748 */             marks = new ArrayList();
/*      */           }
/*      */ 
/*  751 */           marks.add(this.version.createReferenceParameterHeader(mark, xsr.getNamespaceURI(), xsr.getLocalName()));
/*      */ 
/*  753 */           XMLStreamReaderUtil.skipElement(xsr);
/*      */         }
/*      */       }
/*  756 */       else if (localName.equals("Address")) {
/*  757 */         if (this.address != null)
/*  758 */           throw new InvalidAddressingHeaderException(new QName(this.version.nsUri, rootLocalName), AddressingVersion.fault_duplicateAddressInEpr);
/*  759 */         this.address = xsr.getElementText().trim();
/*      */       } else {
/*  761 */         XMLStreamReaderUtil.skipElement(xsr);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  767 */     if (marks == null)
/*  768 */       this.referenceParameters = EMPTY_ARRAY;
/*      */     else {
/*  770 */       this.referenceParameters = ((Header[])marks.toArray(new Header[marks.size()]));
/*      */     }
/*      */ 
/*  773 */     if (this.address == null)
/*  774 */       throw new InvalidAddressingHeaderException(new QName(this.version.nsUri, rootLocalName), this.version.fault_missingAddressInEpr);
/*      */   }
/*      */ 
/*      */   public XMLStreamReader read(@NotNull final String localName)
/*      */     throws XMLStreamException
/*      */   {
/*  787 */     return new StreamReaderBufferProcessor(this.infoset) {
/*      */       protected void processElement(String prefix, String uri, String _localName) {
/*  789 */         if (this._depth == 0)
/*  790 */           _localName = localName;
/*  791 */         super.processElement(prefix, uri, _localName);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public Source asSource(@NotNull String localName)
/*      */   {
/*  805 */     return new SAXSource(new SAXBufferProcessorImpl(localName), new InputSource());
/*      */   }
/*      */ 
/*      */   public void writeTo(@NotNull String localName, ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment)
/*      */     throws SAXException
/*      */   {
/*  820 */     SAXBufferProcessorImpl p = new SAXBufferProcessorImpl(localName);
/*  821 */     p.setContentHandler(contentHandler);
/*  822 */     p.setErrorHandler(errorHandler);
/*  823 */     p.process(this.infoset, fragment);
/*      */   }
/*      */ 
/*      */   public void writeTo(@NotNull final String localName, @NotNull XMLStreamWriter w)
/*      */     throws XMLStreamException
/*      */   {
/*  834 */     this.infoset.writeToXMLStreamWriter(new XMLStreamWriterFilter(w) {
/*  835 */       private boolean root = true;
/*      */ 
/*      */       public void writeStartDocument() throws XMLStreamException
/*      */       {
/*      */       }
/*      */ 
/*      */       public void writeStartDocument(String encoding, String version) throws XMLStreamException
/*      */       {
/*      */       }
/*      */ 
/*      */       public void writeStartDocument(String version) throws XMLStreamException
/*      */       {
/*      */       }
/*      */ 
/*      */       public void writeEndDocument() throws XMLStreamException
/*      */       {
/*      */       }
/*      */ 
/*      */       private String override(String ln) {
/*  854 */         if (this.root) {
/*  855 */           this.root = false;
/*  856 */           return localName;
/*      */         }
/*  858 */         return ln;
/*      */       }
/*      */ 
/*      */       public void writeStartElement(String localName) throws XMLStreamException {
/*  862 */         super.writeStartElement(override(localName));
/*      */       }
/*      */ 
/*      */       public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/*  866 */         super.writeStartElement(namespaceURI, override(localName));
/*      */       }
/*      */ 
/*      */       public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/*  870 */         super.writeStartElement(prefix, override(localName), namespaceURI);
/*      */       }
/*      */     }
/*      */     , true);
/*      */   }
/*      */ 
/*      */   public Header createHeader(QName rootTagName)
/*      */   {
/*  889 */     return new EPRHeader(rootTagName, this);
/*      */   }
/*      */ 
/*      */   public void addReferenceParameters(HeaderList outbound)
/*      */   {
/*  897 */     for (Header header : this.referenceParameters)
/*  898 */       outbound.add(header);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/*  909 */       StringWriter sw = new StringWriter();
/*  910 */       XmlUtil.newTransformer().transform(asSource("EndpointReference"), new StreamResult(sw));
/*  911 */       return sw.toString();
/*      */     } catch (TransformerException e) {
/*  913 */       return e.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   public QName getName()
/*      */   {
/*  922 */     return this.rootElement;
/*      */   }
/*      */ 
/*      */   @Nullable
/*      */   public EPRExtension getEPRExtension(QName extnQName)
/*      */     throws XMLStreamException
/*      */   {
/*  971 */     if (this.rootEprExtensions == null)
/*  972 */       parseEPRExtensions();
/*  973 */     return (EPRExtension)this.rootEprExtensions.get(extnQName);
/*      */   }
/*      */   @NotNull
/*      */   public Collection<EPRExtension> getEPRExtensions() throws XMLStreamException {
/*  977 */     if (this.rootEprExtensions == null)
/*  978 */       parseEPRExtensions();
/*  979 */     return this.rootEprExtensions.values();
/*      */   }
/*      */ 
/*      */   private void parseEPRExtensions() throws XMLStreamException
/*      */   {
/*  984 */     this.rootEprExtensions = new HashMap();
/*      */ 
/*  987 */     StreamReaderBufferProcessor xsr = this.infoset.readAsXMLStreamReader();
/*      */ 
/*  990 */     if (xsr.getEventType() == 7)
/*  991 */       xsr.nextTag();
/*  992 */     assert (xsr.getEventType() == 1);
/*      */ 
/*  994 */     String rootLocalName = xsr.getLocalName();
/*  995 */     if (!xsr.getNamespaceURI().equals(this.version.nsUri))
/*  996 */       throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(this.version.nsUri, xsr.getNamespaceURI()));
/*      */     XMLStreamBuffer mark;
/* 1003 */     while ((mark = xsr.nextTagAndMark()) != null) {
/* 1004 */       String localName = xsr.getLocalName();
/* 1005 */       String ns = xsr.getNamespaceURI();
/* 1006 */       if (this.version.nsUri.equals(ns))
/*      */       {
/* 1009 */         XMLStreamReaderUtil.skipElement(xsr);
/*      */       } else {
/* 1011 */         QName qn = new QName(ns, localName);
/* 1012 */         this.rootEprExtensions.put(qn, new WSEPRExtension(mark, qn));
/* 1013 */         XMLStreamReaderUtil.skipElement(xsr);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   @NotNull
/*      */   public Metadata getMetaData()
/*      */   {
/* 1026 */     return new Metadata(null); } 
/*      */   public static abstract class EPRExtension { public abstract XMLStreamReader readAsXMLStreamReader() throws XMLStreamException;
/*      */ 
/*      */     public abstract QName getQName(); } 
/*      */   public class Metadata { 
/*      */     @Nullable
/*      */     private QName serviceName;
/*      */ 
/*      */     @Nullable
/*      */     private QName portName;
/*      */ 
/*      */     @Nullable
/*      */     private QName portTypeName;
/*      */ 
/*      */     @Nullable
/*      */     private Source wsdlSource;
/*      */ 
/*      */     @Nullable
/*      */     private String wsdliLocation;
/*      */ 
/* 1042 */     @Nullable
/*      */     public QName getServiceName() { return this.serviceName; } 
/*      */     @Nullable
/*      */     public QName getPortName() {
/* 1045 */       return this.portName;
/*      */     }
/* 1048 */     @Nullable
/*      */     public QName getPortTypeName() { return this.portTypeName; } 
/*      */     @Nullable
/*      */     public Source getWsdlSource() {
/* 1051 */       return this.wsdlSource;
/*      */     }
/* 1054 */     @Nullable
/*      */     public String getWsdliLocation() { return this.wsdliLocation; }
/*      */ 
/*      */     private Metadata()
/*      */     {
/*      */       try {
/* 1059 */         parseMetaData();
/*      */       } catch (XMLStreamException e) {
/* 1061 */         throw new WebServiceException(e);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void parseMetaData()
/*      */       throws XMLStreamException
/*      */     {
/* 1069 */       StreamReaderBufferProcessor xsr = WSEndpointReference.this.infoset.readAsXMLStreamReader();
/*      */ 
/* 1072 */       if (xsr.getEventType() == 7)
/* 1073 */         xsr.nextTag();
/* 1074 */       assert (xsr.getEventType() == 1);
/* 1075 */       String rootElement = xsr.getLocalName();
/* 1076 */       if (!xsr.getNamespaceURI().equals(WSEndpointReference.this.version.nsUri)) {
/* 1077 */         throw new WebServiceException(AddressingMessages.WRONG_ADDRESSING_VERSION(WSEndpointReference.this.version.nsUri, xsr.getNamespaceURI()));
/*      */       }
/*      */ 
/* 1081 */       if (WSEndpointReference.this.version == AddressingVersion.W3C)
/*      */       {
/*      */         do
/* 1084 */           if (xsr.getLocalName().equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getLocalPart())) {
/* 1085 */             String wsdlLoc = xsr.getAttributeValue("http://www.w3.org/ns/wsdl-instance", "wsdlLocation");
/* 1086 */             if (wsdlLoc != null)
/* 1087 */               this.wsdliLocation = wsdlLoc.trim();
/*      */             XMLStreamBuffer mark;
/* 1089 */             while ((mark = xsr.nextTagAndMark()) != null) {
/* 1090 */               String localName = xsr.getLocalName();
/* 1091 */               String ns = xsr.getNamespaceURI();
/* 1092 */               if (localName.equals(WSEndpointReference.this.version.eprType.serviceName)) {
/* 1093 */                 String portStr = xsr.getAttributeValue(null, WSEndpointReference.this.version.eprType.portName);
/* 1094 */                 if (this.serviceName != null)
/* 1095 */                   throw new RuntimeException("More than one " + WSEndpointReference.this.version.eprType.serviceName + " element in EPR Metadata");
/* 1096 */                 this.serviceName = getElementTextAsQName(xsr);
/* 1097 */                 if ((this.serviceName != null) && (portStr != null))
/* 1098 */                   this.portName = new QName(this.serviceName.getNamespaceURI(), portStr);
/* 1099 */               } else if (localName.equals(WSEndpointReference.this.version.eprType.portTypeName)) {
/* 1100 */                 if (this.portTypeName != null)
/* 1101 */                   throw new RuntimeException("More than one " + WSEndpointReference.this.version.eprType.portTypeName + " element in EPR Metadata");
/* 1102 */                 this.portTypeName = getElementTextAsQName(xsr);
/* 1103 */               } else if ((ns.equals("http://schemas.xmlsoap.org/wsdl/")) && (localName.equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())))
/*      */               {
/* 1105 */                 this.wsdlSource = new XMLStreamBufferSource(mark);
/*      */               } else {
/* 1107 */                 XMLStreamReaderUtil.skipElement(xsr);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/* 1112 */           else if (!xsr.getLocalName().equals(rootElement)) {
/* 1113 */             XMLStreamReaderUtil.skipElement(xsr);
/*      */           }
/* 1115 */         while (XMLStreamReaderUtil.nextElementContent(xsr) == 1);
/*      */ 
/* 1117 */         if (this.wsdliLocation != null) {
/* 1118 */           String wsdlLocation = this.wsdliLocation.trim();
/* 1119 */           wsdlLocation = wsdlLocation.substring(this.wsdliLocation.lastIndexOf(" "));
/* 1120 */           this.wsdlSource = new StreamSource(wsdlLocation);
/*      */         }
/* 1122 */       } else if (WSEndpointReference.this.version == AddressingVersion.MEMBER) {
/*      */         do {
/* 1124 */           String localName = xsr.getLocalName();
/* 1125 */           String ns = xsr.getNamespaceURI();
/*      */ 
/* 1127 */           if ((localName.equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getLocalPart())) && (ns.equals(WSEndpointReference.this.version.eprType.wsdlMetadata.getNamespaceURI())));
/* 1129 */           while (xsr.nextTag() == 1)
/*      */           {
/*      */             XMLStreamBuffer mark;
/* 1131 */             while ((mark = xsr.nextTagAndMark()) != null) {
/* 1132 */               localName = xsr.getLocalName();
/* 1133 */               ns = xsr.getNamespaceURI();
/* 1134 */               if ((ns.equals("http://schemas.xmlsoap.org/wsdl/")) && (localName.equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())))
/*      */               {
/* 1136 */                 this.wsdlSource = new XMLStreamBufferSource(mark);
/*      */               }
/* 1138 */               else XMLStreamReaderUtil.skipElement(xsr);
/*      */             }
/*      */ 
/* 1141 */             continue;
/* 1142 */             if (localName.equals(WSEndpointReference.this.version.eprType.serviceName)) {
/* 1143 */               String portStr = xsr.getAttributeValue(null, WSEndpointReference.this.version.eprType.portName);
/* 1144 */               this.serviceName = getElementTextAsQName(xsr);
/* 1145 */               if ((this.serviceName != null) && (portStr != null))
/* 1146 */                 this.portName = new QName(this.serviceName.getNamespaceURI(), portStr);
/* 1147 */             } else if (localName.equals(WSEndpointReference.this.version.eprType.portTypeName)) {
/* 1148 */               this.portTypeName = getElementTextAsQName(xsr);
/*      */             }
/* 1151 */             else if (!xsr.getLocalName().equals(rootElement)) {
/* 1152 */               XMLStreamReaderUtil.skipElement(xsr);
/*      */             }
/*      */           }
/*      */         }
/* 1154 */         while (XMLStreamReaderUtil.nextElementContent(xsr) == 1);
/*      */       }
/*      */     }
/*      */ 
/*      */     private QName getElementTextAsQName(StreamReaderBufferProcessor xsr) throws XMLStreamException {
/* 1159 */       String text = xsr.getElementText().trim();
/* 1160 */       String prefix = XmlUtil.getPrefix(text);
/* 1161 */       String name = XmlUtil.getLocalPart(text);
/* 1162 */       if (name != null) {
/* 1163 */         if (prefix != null) {
/* 1164 */           String ns = xsr.getNamespaceURI(prefix);
/* 1165 */           if (ns != null)
/* 1166 */             return new QName(ns, name, prefix);
/*      */         } else {
/* 1168 */           return new QName(null, name);
/*      */         }
/*      */       }
/* 1171 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   class SAXBufferProcessorImpl extends SAXBufferProcessor
/*      */   {
/*      */     private final String rootLocalName;
/*  930 */     private boolean root = true;
/*      */ 
/*      */     public SAXBufferProcessorImpl(String rootLocalName) {
/*  933 */       super(false);
/*  934 */       this.rootLocalName = rootLocalName;
/*      */     }
/*      */ 
/*      */     protected void processElement(String uri, String localName, String qName) throws SAXException {
/*  938 */       if (this.root) {
/*  939 */         this.root = false;
/*      */ 
/*  941 */         if (qName.equals(localName)) {
/*  942 */           qName = localName = this.rootLocalName;
/*      */         } else {
/*  944 */           localName = this.rootLocalName;
/*  945 */           int idx = qName.indexOf(':');
/*  946 */           qName = qName.substring(0, idx + 1) + this.rootLocalName;
/*      */         }
/*      */       }
/*  949 */       super.processElement(uri, localName, qName);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.addressing.WSEndpointReference
 * JD-Core Version:    0.6.2
 */