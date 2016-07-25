/*     */ package com.sun.xml.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLDescriptorKind;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.MetaDataResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.MetadataResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.ServiceDescriptor;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver.Parser;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundFaultImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLFaultImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLMessageImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOutputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPartDescriptorImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPartImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*     */ import com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.resources.WsdlmodelMessages;
/*     */ import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
/*     */ import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.soap.SOAPBinding.Style;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class RuntimeWSDLParser
/*     */ {
/*     */   private final WSDLModelImpl wsdlDoc;
/*     */   private String targetNamespace;
/*  93 */   private final Set<String> importedWSDLs = new HashSet();
/*     */   private final XMLEntityResolver resolver;
/*     */   private final PolicyResolver policyResolver;
/*     */   private final WSDLParserExtension extensionFacade;
/*     */   private final WSDLParserExtensionContextImpl context;
/*     */   List<WSDLParserExtension> extensions;
/* 851 */   private static final Logger LOGGER = Logger.getLogger(RuntimeWSDLParser.class.getName());
/*     */ 
/*     */   public static WSDLModelImpl parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, WSDLParserExtension[] extensions)
/*     */     throws IOException, XMLStreamException, SAXException
/*     */   {
/* 120 */     return parse(wsdlLoc, wsdlSource, resolver, isClientSide, container, PolicyResolverFactory.create(), extensions);
/*     */   }
/*     */ 
/*     */   public static WSDLModelImpl parse(@Nullable URL wsdlLoc, @NotNull Source wsdlSource, @NotNull EntityResolver resolver, boolean isClientSide, Container container, @NotNull PolicyResolver policyResolver, WSDLParserExtension[] extensions)
/*     */     throws IOException, XMLStreamException, SAXException
/*     */   {
/* 135 */     assert (resolver != null);
/*     */ 
/* 137 */     RuntimeWSDLParser wsdlParser = new RuntimeWSDLParser(wsdlSource.getSystemId(), new EntityResolverWrapper(resolver), isClientSide, container, policyResolver, extensions);
/*     */     XMLEntityResolver.Parser parser;
/*     */     try
/*     */     {
/* 140 */       parser = wsdlParser.resolveWSDL(wsdlLoc, wsdlSource);
/* 141 */       if (!hasWSDLDefinitions(parser.parser)) {
/* 142 */         throw new XMLStreamException(ClientMessages.RUNTIME_WSDLPARSER_INVALID_WSDL(parser.systemId, WSDLConstants.QNAME_DEFINITIONS, parser.parser.getName(), parser.parser.getLocation()));
/*     */       }
/*     */     }
/*     */     catch (XMLStreamException e)
/*     */     {
/* 147 */       if (wsdlLoc == null)
/* 148 */         throw e;
/* 149 */       return tryWithMex(wsdlParser, wsdlLoc, resolver, isClientSide, container, e, policyResolver, extensions);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 153 */       if (wsdlLoc == null)
/* 154 */         throw e;
/* 155 */       return tryWithMex(wsdlParser, wsdlLoc, resolver, isClientSide, container, e, policyResolver, extensions);
/*     */     }
/* 157 */     wsdlParser.parseWSDL(parser, false);
/* 158 */     wsdlParser.wsdlDoc.freeze();
/* 159 */     wsdlParser.extensionFacade.finished(wsdlParser.context);
/* 160 */     wsdlParser.extensionFacade.postFinished(wsdlParser.context);
/*     */ 
/* 162 */     if (wsdlParser.wsdlDoc.getServices().isEmpty()) {
/* 163 */       throw new WebServiceException(ClientMessages.WSDL_CONTAINS_NO_SERVICE(wsdlLoc));
/*     */     }
/* 165 */     return wsdlParser.wsdlDoc;
/*     */   }
/*     */ 
/*     */   private static WSDLModelImpl tryWithMex(@NotNull RuntimeWSDLParser wsdlParser, @NotNull URL wsdlLoc, @NotNull EntityResolver resolver, boolean isClientSide, Container container, Throwable e, PolicyResolver policyResolver, WSDLParserExtension[] extensions) throws SAXException, XMLStreamException {
/* 169 */     ArrayList exceptions = new ArrayList();
/*     */     try {
/* 171 */       WSDLModelImpl wsdlModel = wsdlParser.parseUsingMex(wsdlLoc, resolver, isClientSide, container, policyResolver, extensions);
/* 172 */       if (wsdlModel == null) {
/* 173 */         throw new WebServiceException(ClientMessages.FAILED_TO_PARSE(wsdlLoc.toExternalForm(), e.getMessage()), e);
/*     */       }
/* 175 */       return wsdlModel;
/*     */     } catch (URISyntaxException e1) {
/* 177 */       exceptions.add(e);
/* 178 */       exceptions.add(e1);
/*     */     } catch (IOException e1) {
/* 180 */       exceptions.add(e);
/* 181 */       exceptions.add(e1);
/*     */     }
/* 183 */     throw new InaccessibleWSDLException(exceptions);
/*     */   }
/*     */ 
/*     */   private WSDLModelImpl parseUsingMex(@NotNull URL wsdlLoc, @NotNull EntityResolver resolver, boolean isClientSide, Container container, PolicyResolver policyResolver, WSDLParserExtension[] extensions) throws IOException, SAXException, XMLStreamException, URISyntaxException
/*     */   {
/* 188 */     MetaDataResolver mdResolver = null;
/* 189 */     ServiceDescriptor serviceDescriptor = null;
/* 190 */     RuntimeWSDLParser wsdlParser = null;
/*     */ 
/* 193 */     for (MetadataResolverFactory resolverFactory : ServiceFinder.find(MetadataResolverFactory.class)) {
/* 194 */       mdResolver = resolverFactory.metadataResolver(resolver);
/* 195 */       serviceDescriptor = mdResolver.resolve(wsdlLoc.toURI());
/*     */ 
/* 197 */       if (serviceDescriptor != null)
/*     */         break;
/*     */     }
/* 200 */     if (serviceDescriptor != null) {
/* 201 */       List wsdls = serviceDescriptor.getWSDLs();
/* 202 */       wsdlParser = new RuntimeWSDLParser(wsdlLoc.toExternalForm(), new MexEntityResolver(wsdls), isClientSide, container, policyResolver, extensions);
/*     */ 
/* 204 */       for (Source src : wsdls) {
/* 205 */         String systemId = src.getSystemId();
/* 206 */         XMLEntityResolver.Parser parser = wsdlParser.resolver.resolveEntity(null, systemId);
/* 207 */         wsdlParser.parseWSDL(parser, false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 212 */     if (((mdResolver == null) || (serviceDescriptor == null)) && ((wsdlLoc.getProtocol().equals("http")) || (wsdlLoc.getProtocol().equals("https"))) && (wsdlLoc.getQuery() == null)) {
/* 213 */       String urlString = wsdlLoc.toExternalForm();
/* 214 */       urlString = urlString + "?wsdl";
/* 215 */       wsdlLoc = new URL(urlString);
/* 216 */       wsdlParser = new RuntimeWSDLParser(wsdlLoc.toExternalForm(), new EntityResolverWrapper(resolver), isClientSide, container, policyResolver, extensions);
/* 217 */       XMLEntityResolver.Parser parser = resolveWSDL(wsdlLoc, new StreamSource(wsdlLoc.toExternalForm()));
/* 218 */       wsdlParser.parseWSDL(parser, false);
/*     */     }
/*     */ 
/* 221 */     if (wsdlParser == null) {
/* 222 */       return null;
/*     */     }
/* 224 */     wsdlParser.wsdlDoc.freeze();
/* 225 */     wsdlParser.extensionFacade.finished(wsdlParser.context);
/* 226 */     wsdlParser.extensionFacade.postFinished(wsdlParser.context);
/* 227 */     return wsdlParser.wsdlDoc;
/*     */   }
/*     */ 
/*     */   private static boolean hasWSDLDefinitions(XMLStreamReader reader) {
/* 231 */     XMLStreamReaderUtil.nextElementContent(reader);
/* 232 */     return reader.getName().equals(WSDLConstants.QNAME_DEFINITIONS);
/*     */   }
/*     */ 
/*     */   public static WSDLModelImpl parse(XMLEntityResolver.Parser wsdl, XMLEntityResolver resolver, boolean isClientSide, Container container, PolicyResolver policyResolver, WSDLParserExtension[] extensions) throws IOException, XMLStreamException, SAXException {
/* 236 */     assert (resolver != null);
/* 237 */     RuntimeWSDLParser parser = new RuntimeWSDLParser(wsdl.systemId.toExternalForm(), resolver, isClientSide, container, policyResolver, extensions);
/* 238 */     parser.parseWSDL(wsdl, false);
/* 239 */     parser.wsdlDoc.freeze();
/* 240 */     parser.extensionFacade.finished(parser.context);
/* 241 */     parser.extensionFacade.postFinished(parser.context);
/* 242 */     return parser.wsdlDoc;
/*     */   }
/*     */ 
/*     */   public static WSDLModelImpl parse(XMLEntityResolver.Parser wsdl, XMLEntityResolver resolver, boolean isClientSide, Container container, WSDLParserExtension[] extensions) throws IOException, XMLStreamException, SAXException {
/* 246 */     assert (resolver != null);
/* 247 */     RuntimeWSDLParser parser = new RuntimeWSDLParser(wsdl.systemId.toExternalForm(), resolver, isClientSide, container, PolicyResolverFactory.create(), extensions);
/* 248 */     parser.parseWSDL(wsdl, false);
/* 249 */     parser.wsdlDoc.freeze();
/* 250 */     parser.extensionFacade.finished(parser.context);
/* 251 */     parser.extensionFacade.postFinished(parser.context);
/* 252 */     return parser.wsdlDoc;
/*     */   }
/*     */ 
/*     */   private RuntimeWSDLParser(@NotNull String sourceLocation, XMLEntityResolver resolver, boolean isClientSide, Container container, PolicyResolver policyResolver, WSDLParserExtension[] extensions) {
/* 256 */     this.wsdlDoc = (sourceLocation != null ? new WSDLModelImpl(sourceLocation) : new WSDLModelImpl());
/* 257 */     this.resolver = resolver;
/* 258 */     this.policyResolver = policyResolver;
/* 259 */     this.extensions = new ArrayList();
/* 260 */     this.context = new WSDLParserExtensionContextImpl(this.wsdlDoc, isClientSide, container, policyResolver);
/*     */ 
/* 263 */     register(new PolicyWSDLParserExtension());
/* 264 */     register(new MemberSubmissionAddressingWSDLParserExtension());
/* 265 */     register(new W3CAddressingWSDLParserExtension());
/* 266 */     register(new W3CAddressingMetadataWSDLParserExtension());
/* 267 */     for (WSDLParserExtension e : extensions) {
/* 268 */       register(e);
/*     */     }
/* 270 */     this.extensionFacade = new WSDLParserExtensionFacade((WSDLParserExtension[])this.extensions.toArray(new WSDLParserExtension[0]));
/*     */   }
/*     */ 
/*     */   private XMLEntityResolver.Parser resolveWSDL(@Nullable URL wsdlLoc, @NotNull Source wsdlSource) throws IOException, SAXException, XMLStreamException {
/* 274 */     String systemId = wsdlSource.getSystemId();
/*     */ 
/* 276 */     XMLEntityResolver.Parser parser = this.resolver.resolveEntity(null, systemId);
/* 277 */     if ((parser == null) && (wsdlLoc != null)) {
/* 278 */       parser = this.resolver.resolveEntity(null, wsdlLoc.toExternalForm());
/*     */     }
/*     */ 
/* 281 */     if (parser == null) {
/* 282 */       if (wsdlLoc != null)
/* 283 */         parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlLoc));
/*     */       else
/* 285 */         parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlSource));
/*     */     }
/* 287 */     return parser;
/*     */   }
/*     */ 
/*     */   private XMLStreamReader createReader(@NotNull Source src) throws XMLStreamException
/*     */   {
/* 292 */     return new TidyXMLStreamReader(SourceReaderFactory.createSourceReader(src, true), null);
/*     */   }
/*     */ 
/*     */   private void parseImport(@NotNull URL wsdlLoc) throws XMLStreamException, IOException, SAXException {
/* 296 */     String systemId = wsdlLoc.toExternalForm();
/* 297 */     XMLEntityResolver.Parser parser = this.resolver.resolveEntity(null, systemId);
/* 298 */     if (parser == null) {
/* 299 */       parser = new XMLEntityResolver.Parser(wsdlLoc, createReader(wsdlLoc));
/*     */     }
/* 301 */     parseWSDL(parser, true);
/*     */   }
/*     */ 
/*     */   private void parseWSDL(XMLEntityResolver.Parser parser, boolean imported) throws XMLStreamException, IOException, SAXException {
/* 305 */     XMLStreamReader reader = parser.parser;
/*     */     try
/*     */     {
/* 309 */       if ((parser.systemId != null) && (!this.importedWSDLs.add(parser.systemId.toExternalForm()))) {
/*     */         return;
/*     */       }
/* 312 */       if (reader.getEventType() == 7) {
/* 313 */         XMLStreamReaderUtil.nextElementContent(reader);
/*     */       }
/* 315 */       if ((reader.getEventType() != 8) && (reader.getName().equals(WSDLConstants.QNAME_SCHEMA)) && 
/* 316 */         (imported))
/*     */       {
/* 318 */         LOGGER.warning(WsdlmodelMessages.WSDL_IMPORT_SHOULD_BE_WSDL(parser.systemId));
/*     */       }
/*     */       else
/*     */       {
/* 324 */         String tns = ParserUtil.getMandatoryNonEmptyAttribute(reader, "targetNamespace");
/*     */ 
/* 326 */         String oldTargetNamespace = this.targetNamespace;
/* 327 */         this.targetNamespace = tns;
/*     */ 
/* 329 */         while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/*     */         {
/* 331 */           if (reader.getEventType() == 8) {
/*     */             break;
/*     */           }
/* 334 */           QName name = reader.getName();
/* 335 */           if (WSDLConstants.QNAME_IMPORT.equals(name))
/* 336 */             parseImport(parser.systemId, reader);
/* 337 */           else if (WSDLConstants.QNAME_MESSAGE.equals(name))
/* 338 */             parseMessage(reader);
/* 339 */           else if (WSDLConstants.QNAME_PORT_TYPE.equals(name))
/* 340 */             parsePortType(reader);
/* 341 */           else if (WSDLConstants.QNAME_BINDING.equals(name))
/* 342 */             parseBinding(reader);
/* 343 */           else if (WSDLConstants.QNAME_SERVICE.equals(name))
/* 344 */             parseService(reader);
/*     */           else {
/* 346 */             this.extensionFacade.definitionsElements(reader);
/*     */           }
/*     */         }
/* 349 */         this.targetNamespace = oldTargetNamespace;
/*     */       }
/*     */     } finally { reader.close(); }
/*     */   }
/*     */ 
/*     */   private void parseService(XMLStreamReader reader)
/*     */   {
/* 356 */     String serviceName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 357 */     WSDLServiceImpl service = new WSDLServiceImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, serviceName));
/* 358 */     this.extensionFacade.serviceAttributes(service, reader);
/* 359 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 360 */       QName name = reader.getName();
/* 361 */       if (WSDLConstants.QNAME_PORT.equals(name)) {
/* 362 */         parsePort(reader, service);
/* 363 */         if (reader.getEventType() != 2)
/* 364 */           XMLStreamReaderUtil.next(reader);
/*     */       }
/*     */       else {
/* 367 */         this.extensionFacade.serviceElements(service, reader);
/*     */       }
/*     */     }
/* 370 */     this.wsdlDoc.addService(service);
/*     */   }
/*     */ 
/*     */   private void parsePort(XMLStreamReader reader, WSDLServiceImpl service) {
/* 374 */     String portName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 375 */     String binding = ParserUtil.getMandatoryNonEmptyAttribute(reader, "binding");
/*     */ 
/* 377 */     QName bindingName = ParserUtil.getQName(reader, binding);
/* 378 */     QName portQName = new QName(service.getName().getNamespaceURI(), portName);
/* 379 */     WSDLPortImpl port = new WSDLPortImpl(reader, service, portQName, bindingName);
/*     */ 
/* 381 */     this.extensionFacade.portAttributes(port, reader);
/*     */ 
/* 384 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 385 */       QName name = reader.getName();
/* 386 */       if ((SOAPConstants.QNAME_ADDRESS.equals(name)) || (SOAPConstants.QNAME_SOAP12ADDRESS.equals(name))) {
/* 387 */         String location = ParserUtil.getMandatoryNonEmptyAttribute(reader, "location");
/* 388 */         if (location != null) {
/*     */           try {
/* 390 */             port.setAddress(new EndpointAddress(location));
/*     */           }
/*     */           catch (URISyntaxException e)
/*     */           {
/*     */           }
/*     */         }
/* 396 */         XMLStreamReaderUtil.next(reader);
/* 397 */       } else if ((AddressingVersion.W3C.nsUri.equals(name.getNamespaceURI())) && ("EndpointReference".equals(name.getLocalPart())))
/*     */       {
/*     */         try {
/* 400 */           WSEndpointReference wsepr = new WSEndpointReference(reader, AddressingVersion.W3C);
/* 401 */           port.setEPR(wsepr);
/*     */ 
/* 407 */           if ((reader.getEventType() == 2) && (reader.getName().equals(WSDLConstants.QNAME_PORT)))
/* 408 */             break;
/*     */         } catch (XMLStreamException e) {
/* 410 */           throw new WebServiceException(e);
/*     */         }
/*     */       }
/*     */       else {
/* 414 */         this.extensionFacade.portElements(port, reader);
/*     */       }
/*     */     }
/* 417 */     if (port.getAddress() == null) {
/*     */       try {
/* 419 */         port.setAddress(new EndpointAddress(""));
/*     */       }
/*     */       catch (URISyntaxException e)
/*     */       {
/*     */       }
/*     */     }
/* 425 */     service.put(portQName, port);
/*     */   }
/*     */ 
/*     */   private void parseBinding(XMLStreamReader reader) {
/* 429 */     String bindingName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 430 */     String portTypeName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "type");
/* 431 */     if ((bindingName == null) || (portTypeName == null))
/*     */     {
/* 435 */       XMLStreamReaderUtil.skipElement(reader);
/* 436 */       return;
/*     */     }
/* 438 */     WSDLBoundPortTypeImpl binding = new WSDLBoundPortTypeImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, bindingName), ParserUtil.getQName(reader, portTypeName));
/*     */ 
/* 440 */     this.extensionFacade.bindingAttributes(binding, reader);
/*     */ 
/* 442 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 443 */       QName name = reader.getName();
/* 444 */       if (WSDLConstants.NS_SOAP_BINDING.equals(name)) {
/* 445 */         binding.setBindingId(BindingID.SOAP11_HTTP);
/* 446 */         String style = reader.getAttributeValue(null, "style");
/*     */ 
/* 448 */         if ((style != null) && (style.equals("rpc")))
/* 449 */           binding.setStyle(SOAPBinding.Style.RPC);
/*     */         else {
/* 451 */           binding.setStyle(SOAPBinding.Style.DOCUMENT);
/*     */         }
/* 453 */         goToEnd(reader);
/* 454 */       } else if (WSDLConstants.NS_SOAP12_BINDING.equals(name)) {
/* 455 */         binding.setBindingId(BindingID.SOAP12_HTTP);
/* 456 */         String style = reader.getAttributeValue(null, "style");
/* 457 */         if ((style != null) && (style.equals("rpc")))
/* 458 */           binding.setStyle(SOAPBinding.Style.RPC);
/*     */         else {
/* 460 */           binding.setStyle(SOAPBinding.Style.DOCUMENT);
/*     */         }
/* 462 */         goToEnd(reader);
/* 463 */       } else if (WSDLConstants.QNAME_OPERATION.equals(name)) {
/* 464 */         parseBindingOperation(reader, binding);
/*     */       } else {
/* 466 */         this.extensionFacade.bindingElements(binding, reader);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseBindingOperation(XMLStreamReader reader, WSDLBoundPortTypeImpl binding)
/*     */   {
/* 473 */     String bindingOpName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 474 */     if (bindingOpName == null)
/*     */     {
/* 477 */       XMLStreamReaderUtil.skipElement(reader);
/* 478 */       return;
/*     */     }
/*     */ 
/* 481 */     QName opName = new QName(binding.getPortTypeName().getNamespaceURI(), bindingOpName);
/* 482 */     WSDLBoundOperationImpl bindingOp = new WSDLBoundOperationImpl(reader, binding, opName);
/* 483 */     binding.put(opName, bindingOp);
/* 484 */     this.extensionFacade.bindingOperationAttributes(bindingOp, reader);
/*     */ 
/* 486 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 487 */       QName name = reader.getName();
/* 488 */       String style = null;
/* 489 */       if (WSDLConstants.QNAME_INPUT.equals(name)) {
/* 490 */         parseInputBinding(reader, bindingOp);
/* 491 */       } else if (WSDLConstants.QNAME_OUTPUT.equals(name)) {
/* 492 */         parseOutputBinding(reader, bindingOp);
/* 493 */       } else if (WSDLConstants.QNAME_FAULT.equals(name)) {
/* 494 */         parseFaultBinding(reader, bindingOp);
/* 495 */       } else if ((SOAPConstants.QNAME_OPERATION.equals(name)) || (SOAPConstants.QNAME_SOAP12OPERATION.equals(name)))
/*     */       {
/* 497 */         style = reader.getAttributeValue(null, "style");
/* 498 */         String soapAction = reader.getAttributeValue(null, "soapAction");
/*     */ 
/* 500 */         if (soapAction != null) {
/* 501 */           bindingOp.setSoapAction(soapAction);
/*     */         }
/* 503 */         goToEnd(reader);
/*     */       } else {
/* 505 */         this.extensionFacade.bindingOperationElements(bindingOp, reader);
/*     */       }
/*     */ 
/* 511 */       if (style != null) {
/* 512 */         if (style.equals("rpc"))
/* 513 */           bindingOp.setStyle(SOAPBinding.Style.RPC);
/*     */         else
/* 515 */           bindingOp.setStyle(SOAPBinding.Style.DOCUMENT);
/*     */       }
/* 517 */       else bindingOp.setStyle(binding.getStyle());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseInputBinding(XMLStreamReader reader, WSDLBoundOperationImpl bindingOp)
/*     */   {
/* 523 */     boolean bodyFound = false;
/* 524 */     this.extensionFacade.bindingOperationInputAttributes(bindingOp, reader);
/* 525 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 526 */       QName name = reader.getName();
/* 527 */       if (((SOAPConstants.QNAME_BODY.equals(name)) || (SOAPConstants.QNAME_SOAP12BODY.equals(name))) && (!bodyFound)) {
/* 528 */         bodyFound = true;
/* 529 */         bindingOp.setInputExplicitBodyParts(parseSOAPBodyBinding(reader, bindingOp, BindingMode.INPUT));
/* 530 */         goToEnd(reader);
/* 531 */       } else if ((SOAPConstants.QNAME_HEADER.equals(name)) || (SOAPConstants.QNAME_SOAP12HEADER.equals(name))) {
/* 532 */         parseSOAPHeaderBinding(reader, bindingOp.getInputParts());
/* 533 */       } else if (MIMEConstants.QNAME_MULTIPART_RELATED.equals(name)) {
/* 534 */         parseMimeMultipartBinding(reader, bindingOp, BindingMode.INPUT);
/*     */       } else {
/* 536 */         this.extensionFacade.bindingOperationInputElements(bindingOp, reader);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseOutputBinding(XMLStreamReader reader, WSDLBoundOperationImpl bindingOp) {
/* 542 */     boolean bodyFound = false;
/* 543 */     this.extensionFacade.bindingOperationOutputAttributes(bindingOp, reader);
/* 544 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 545 */       QName name = reader.getName();
/* 546 */       if (((SOAPConstants.QNAME_BODY.equals(name)) || (SOAPConstants.QNAME_SOAP12BODY.equals(name))) && (!bodyFound)) {
/* 547 */         bodyFound = true;
/* 548 */         bindingOp.setOutputExplicitBodyParts(parseSOAPBodyBinding(reader, bindingOp, BindingMode.OUTPUT));
/* 549 */         goToEnd(reader);
/* 550 */       } else if ((SOAPConstants.QNAME_HEADER.equals(name)) || (SOAPConstants.QNAME_SOAP12HEADER.equals(name))) {
/* 551 */         parseSOAPHeaderBinding(reader, bindingOp.getOutputParts());
/* 552 */       } else if (MIMEConstants.QNAME_MULTIPART_RELATED.equals(name)) {
/* 553 */         parseMimeMultipartBinding(reader, bindingOp, BindingMode.OUTPUT);
/*     */       } else {
/* 555 */         this.extensionFacade.bindingOperationOutputElements(bindingOp, reader);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseFaultBinding(XMLStreamReader reader, WSDLBoundOperationImpl bindingOp) {
/* 561 */     String faultName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 562 */     WSDLBoundFaultImpl wsdlBoundFault = new WSDLBoundFaultImpl(reader, faultName, bindingOp);
/* 563 */     bindingOp.addFault(wsdlBoundFault);
/*     */ 
/* 565 */     this.extensionFacade.bindingOperationFaultAttributes(wsdlBoundFault, reader);
/*     */ 
/* 567 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 568 */       this.extensionFacade.bindingOperationFaultElements(wsdlBoundFault, reader);
/*     */   }
/*     */ 
/*     */   private static boolean parseSOAPBodyBinding(XMLStreamReader reader, WSDLBoundOperationImpl op, BindingMode mode)
/*     */   {
/* 576 */     String namespace = reader.getAttributeValue(null, "namespace");
/* 577 */     if (mode == BindingMode.INPUT) {
/* 578 */       op.setRequestNamespace(namespace);
/* 579 */       return parseSOAPBodyBinding(reader, op.getInputParts());
/*     */     }
/*     */ 
/* 582 */     op.setResponseNamespace(namespace);
/* 583 */     return parseSOAPBodyBinding(reader, op.getOutputParts());
/*     */   }
/*     */ 
/*     */   private static boolean parseSOAPBodyBinding(XMLStreamReader reader, Map<String, ParameterBinding> parts)
/*     */   {
/* 590 */     String partsString = reader.getAttributeValue(null, "parts");
/* 591 */     if (partsString != null) {
/* 592 */       List partsList = XmlUtil.parseTokenList(partsString);
/* 593 */       if (partsList.isEmpty())
/* 594 */         parts.put(" ", ParameterBinding.BODY);
/*     */       else {
/* 596 */         for (String part : partsList) {
/* 597 */           parts.put(part, ParameterBinding.BODY);
/*     */         }
/*     */       }
/* 600 */       return true;
/*     */     }
/* 602 */     return false;
/*     */   }
/*     */ 
/*     */   private static void parseSOAPHeaderBinding(XMLStreamReader reader, Map<String, ParameterBinding> parts) {
/* 606 */     String part = reader.getAttributeValue(null, "part");
/*     */ 
/* 608 */     if ((part == null) || (part.equals(""))) {
/* 609 */       return;
/*     */     }
/*     */ 
/* 615 */     parts.put(part, ParameterBinding.HEADER);
/* 616 */     goToEnd(reader);
/*     */   }
/*     */ 
/*     */   private static void parseMimeMultipartBinding(XMLStreamReader reader, WSDLBoundOperationImpl op, BindingMode mode)
/*     */   {
/* 621 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 622 */       QName name = reader.getName();
/* 623 */       if (MIMEConstants.QNAME_PART.equals(name))
/* 624 */         parseMIMEPart(reader, op, mode);
/*     */       else
/* 626 */         XMLStreamReaderUtil.skipElement(reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void parseMIMEPart(XMLStreamReader reader, WSDLBoundOperationImpl op, BindingMode mode)
/*     */   {
/* 632 */     boolean bodyFound = false;
/* 633 */     Map parts = null;
/* 634 */     if (mode == BindingMode.INPUT)
/* 635 */       parts = op.getInputParts();
/* 636 */     else if (mode == BindingMode.OUTPUT)
/* 637 */       parts = op.getOutputParts();
/* 638 */     else if (mode == BindingMode.FAULT) {
/* 639 */       parts = op.getFaultParts();
/*     */     }
/* 641 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 642 */       QName name = reader.getName();
/* 643 */       if ((SOAPConstants.QNAME_BODY.equals(name)) && (!bodyFound)) {
/* 644 */         bodyFound = true;
/* 645 */         parseSOAPBodyBinding(reader, op, mode);
/* 646 */         XMLStreamReaderUtil.next(reader);
/* 647 */       } else if (SOAPConstants.QNAME_HEADER.equals(name)) {
/* 648 */         bodyFound = true;
/* 649 */         parseSOAPHeaderBinding(reader, parts);
/* 650 */         XMLStreamReaderUtil.next(reader);
/* 651 */       } else if (MIMEConstants.QNAME_CONTENT.equals(name)) {
/* 652 */         String part = reader.getAttributeValue(null, "part");
/* 653 */         String type = reader.getAttributeValue(null, "type");
/* 654 */         if ((part == null) || (type == null)) {
/* 655 */           XMLStreamReaderUtil.skipElement(reader);
/*     */         }
/*     */         else {
/* 658 */           ParameterBinding sb = ParameterBinding.createAttachment(type);
/* 659 */           if ((parts != null) && (sb != null) && (part != null))
/* 660 */             parts.put(part, sb);
/* 661 */           XMLStreamReaderUtil.next(reader);
/*     */         }
/*     */       } else { XMLStreamReaderUtil.skipElement(reader); }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void parseImport(@Nullable URL baseURL, XMLStreamReader reader)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 670 */     String importLocation = ParserUtil.getMandatoryNonEmptyAttribute(reader, "location");
/*     */     URL importURL;
/*     */     URL importURL;
/* 673 */     if (baseURL != null)
/* 674 */       importURL = new URL(baseURL, importLocation);
/*     */     else
/* 676 */       importURL = new URL(importLocation);
/* 677 */     parseImport(importURL);
/* 678 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 679 */       XMLStreamReaderUtil.skipElement(reader);
/*     */   }
/*     */ 
/*     */   private void parsePortType(XMLStreamReader reader)
/*     */   {
/* 684 */     String portTypeName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 685 */     if (portTypeName == null)
/*     */     {
/* 688 */       XMLStreamReaderUtil.skipElement(reader);
/* 689 */       return;
/*     */     }
/* 691 */     WSDLPortTypeImpl portType = new WSDLPortTypeImpl(reader, this.wsdlDoc, new QName(this.targetNamespace, portTypeName));
/* 692 */     this.extensionFacade.portTypeAttributes(portType, reader);
/* 693 */     this.wsdlDoc.addPortType(portType);
/* 694 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 695 */       QName name = reader.getName();
/* 696 */       if (WSDLConstants.QNAME_OPERATION.equals(name))
/* 697 */         parsePortTypeOperation(reader, portType);
/*     */       else
/* 699 */         this.extensionFacade.portTypeElements(portType, reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parsePortTypeOperation(XMLStreamReader reader, WSDLPortTypeImpl portType)
/*     */   {
/* 706 */     String operationName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 707 */     if (operationName == null)
/*     */     {
/* 710 */       XMLStreamReaderUtil.skipElement(reader);
/* 711 */       return;
/*     */     }
/*     */ 
/* 714 */     QName operationQName = new QName(portType.getName().getNamespaceURI(), operationName);
/* 715 */     WSDLOperationImpl operation = new WSDLOperationImpl(reader, portType, operationQName);
/* 716 */     this.extensionFacade.portTypeOperationAttributes(operation, reader);
/* 717 */     String parameterOrder = ParserUtil.getAttribute(reader, "parameterOrder");
/* 718 */     operation.setParameterOrder(parameterOrder);
/* 719 */     portType.put(operationName, operation);
/* 720 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 721 */       QName name = reader.getName();
/* 722 */       if (name.equals(WSDLConstants.QNAME_INPUT))
/* 723 */         parsePortTypeOperationInput(reader, operation);
/* 724 */       else if (name.equals(WSDLConstants.QNAME_OUTPUT))
/* 725 */         parsePortTypeOperationOutput(reader, operation);
/* 726 */       else if (name.equals(WSDLConstants.QNAME_FAULT))
/* 727 */         parsePortTypeOperationFault(reader, operation);
/*     */       else
/* 729 */         this.extensionFacade.portTypeOperationElements(operation, reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parsePortTypeOperationFault(XMLStreamReader reader, WSDLOperationImpl operation)
/*     */   {
/* 736 */     String msg = ParserUtil.getMandatoryNonEmptyAttribute(reader, "message");
/* 737 */     QName msgName = ParserUtil.getQName(reader, msg);
/* 738 */     String name = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 739 */     WSDLFaultImpl fault = new WSDLFaultImpl(reader, name, msgName, operation);
/* 740 */     operation.addFault(fault);
/* 741 */     this.extensionFacade.portTypeOperationFaultAttributes(fault, reader);
/* 742 */     this.extensionFacade.portTypeOperationFault(operation, reader);
/* 743 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 744 */       this.extensionFacade.portTypeOperationFaultElements(fault, reader);
/*     */   }
/*     */ 
/*     */   private void parsePortTypeOperationInput(XMLStreamReader reader, WSDLOperationImpl operation)
/*     */   {
/* 749 */     String msg = ParserUtil.getMandatoryNonEmptyAttribute(reader, "message");
/* 750 */     QName msgName = ParserUtil.getQName(reader, msg);
/* 751 */     String name = ParserUtil.getAttribute(reader, "name");
/* 752 */     WSDLInputImpl input = new WSDLInputImpl(reader, name, msgName, operation);
/* 753 */     operation.setInput(input);
/* 754 */     this.extensionFacade.portTypeOperationInputAttributes(input, reader);
/* 755 */     this.extensionFacade.portTypeOperationInput(operation, reader);
/* 756 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 757 */       this.extensionFacade.portTypeOperationInputElements(input, reader);
/*     */   }
/*     */ 
/*     */   private void parsePortTypeOperationOutput(XMLStreamReader reader, WSDLOperationImpl operation)
/*     */   {
/* 762 */     String msg = ParserUtil.getAttribute(reader, "message");
/* 763 */     QName msgName = ParserUtil.getQName(reader, msg);
/* 764 */     String name = ParserUtil.getAttribute(reader, "name");
/* 765 */     WSDLOutputImpl output = new WSDLOutputImpl(reader, name, msgName, operation);
/* 766 */     operation.setOutput(output);
/* 767 */     this.extensionFacade.portTypeOperationOutputAttributes(output, reader);
/* 768 */     this.extensionFacade.portTypeOperationOutput(operation, reader);
/* 769 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 770 */       this.extensionFacade.portTypeOperationOutputElements(output, reader);
/*     */   }
/*     */ 
/*     */   private void parseMessage(XMLStreamReader reader)
/*     */   {
/* 775 */     String msgName = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 776 */     WSDLMessageImpl msg = new WSDLMessageImpl(reader, new QName(this.targetNamespace, msgName));
/* 777 */     this.extensionFacade.messageAttributes(msg, reader);
/* 778 */     int partIndex = 0;
/* 779 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2) {
/* 780 */       QName name = reader.getName();
/* 781 */       if (WSDLConstants.QNAME_PART.equals(name)) {
/* 782 */         String part = ParserUtil.getMandatoryNonEmptyAttribute(reader, "name");
/* 783 */         String desc = null;
/* 784 */         int index = reader.getAttributeCount();
/* 785 */         WSDLDescriptorKind kind = WSDLDescriptorKind.ELEMENT;
/* 786 */         for (int i = 0; i < index; i++) {
/* 787 */           QName descName = reader.getAttributeName(i);
/* 788 */           if (descName.getLocalPart().equals("element"))
/* 789 */             kind = WSDLDescriptorKind.ELEMENT;
/* 790 */           else if (descName.getLocalPart().equals("TYPE")) {
/* 791 */             kind = WSDLDescriptorKind.TYPE;
/*     */           }
/* 793 */           if ((descName.getLocalPart().equals("element")) || (descName.getLocalPart().equals("type"))) {
/* 794 */             desc = reader.getAttributeValue(i);
/* 795 */             break;
/*     */           }
/*     */         }
/* 798 */         if (desc != null) {
/* 799 */           WSDLPartImpl wsdlPart = new WSDLPartImpl(reader, part, partIndex, new WSDLPartDescriptorImpl(reader, ParserUtil.getQName(reader, desc), kind));
/* 800 */           msg.add(wsdlPart);
/*     */         }
/* 802 */         if (reader.getEventType() != 2)
/* 803 */           goToEnd(reader);
/*     */       } else {
/* 805 */         this.extensionFacade.messageElements(msg, reader);
/*     */       }
/*     */     }
/* 808 */     this.wsdlDoc.addMessage(msg);
/* 809 */     if (reader.getEventType() != 2)
/* 810 */       goToEnd(reader);
/*     */   }
/*     */ 
/*     */   private static void goToEnd(XMLStreamReader reader) {
/* 814 */     while (XMLStreamReaderUtil.nextElementContent(reader) != 2)
/* 815 */       XMLStreamReaderUtil.skipElement(reader);
/*     */   }
/*     */ 
/*     */   private static XMLStreamReader createReader(URL wsdlLoc)
/*     */     throws IOException, XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 827 */       InputStream stream = new FilterInputStream(wsdlLoc.openStream())
/*     */       {
/*     */         boolean closed;
/*     */ 
/*     */         public void close() throws IOException {
/* 832 */           if (!this.closed) {
/* 833 */             this.closed = true;
/* 834 */             byte[] buf = new byte[8192];
/* 835 */             while (read(buf) != -1);
/* 836 */             super.close();
/*     */           }
/*     */         }
/*     */       };
/* 840 */       return new TidyXMLStreamReader(XMLStreamReaderFactory.create(wsdlLoc.toExternalForm(), stream, false), stream);
/*     */     } catch (IOException e) {
/* 842 */       throw ((IOException)new IOException("Got " + e.getMessage() + " while opening stream from " + wsdlLoc).initCause(e));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void register(WSDLParserExtension e)
/*     */   {
/* 848 */     this.extensions.add(new FoolProofParserExtension(e));
/*     */   }
/*     */ 
/*     */   private static enum BindingMode
/*     */   {
/* 573 */     INPUT, OUTPUT, FAULT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser
 * JD-Core Version:    0.6.2
 */