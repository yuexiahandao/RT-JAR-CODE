/*     */ package com.sun.xml.internal.ws.spi;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.server.BoundEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.ContainerResolver;
/*     */ import com.sun.xml.internal.ws.api.server.Module;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.resources.ProviderApiMessages;
/*     */ import com.sun.xml.internal.ws.transport.http.server.EndpointImpl;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.spi.Invoker;
/*     */ import javax.xml.ws.spi.Provider;
/*     */ import javax.xml.ws.spi.ServiceDelegate;
/*     */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.EntityResolver;
/*     */ 
/*     */ public class ProviderImpl extends Provider
/*     */ {
/*  80 */   private static final ContextClassloaderLocal<JAXBContext> eprjc = new ContextClassloaderLocal()
/*     */   {
/*     */     protected JAXBContext initialValue() throws Exception {
/*  83 */       return ProviderImpl.access$000();
/*     */     }
/*  80 */   };
/*     */ 
/*  90 */   public static final ProviderImpl INSTANCE = new ProviderImpl();
/*     */ 
/*     */   public Endpoint createEndpoint(String bindingId, Object implementor)
/*     */   {
/*  94 */     return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse(implementor.getClass()), implementor, new WebServiceFeature[0]);
/*     */   }
/*     */ 
/*     */   public ServiceDelegate createServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class serviceClass)
/*     */   {
/* 101 */     return new WSServiceDelegate(wsdlDocumentLocation, serviceName, serviceClass);
/*     */   }
/*     */ 
/*     */   public ServiceDelegate createServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class serviceClass, WebServiceFeature[] features)
/*     */   {
/* 106 */     if (features.length > 0) {
/* 107 */       throw new WebServiceException("Doesn't support any Service specific features");
/*     */     }
/* 109 */     return new WSServiceDelegate(wsdlDocumentLocation, serviceName, serviceClass);
/*     */   }
/*     */ 
/*     */   public Endpoint createAndPublishEndpoint(String address, Object implementor)
/*     */   {
/* 115 */     Endpoint endpoint = new EndpointImpl(BindingID.parse(implementor.getClass()), implementor, new WebServiceFeature[0]);
/*     */ 
/* 118 */     endpoint.publish(address);
/* 119 */     return endpoint;
/*     */   }
/*     */ 
/*     */   public Endpoint createEndpoint(String bindingId, Object implementor, WebServiceFeature[] features) {
/* 123 */     return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse(implementor.getClass()), implementor, features);
/*     */   }
/*     */ 
/*     */   public Endpoint createAndPublishEndpoint(String address, Object implementor, WebServiceFeature[] features)
/*     */   {
/* 129 */     Endpoint endpoint = new EndpointImpl(BindingID.parse(implementor.getClass()), implementor, features);
/*     */ 
/* 131 */     endpoint.publish(address);
/* 132 */     return endpoint;
/*     */   }
/*     */ 
/*     */   public Endpoint createEndpoint(String bindingId, Class implementorClass, Invoker invoker, WebServiceFeature[] features) {
/* 136 */     return new EndpointImpl(bindingId != null ? BindingID.parse(bindingId) : BindingID.parse(implementorClass), implementorClass, invoker, features);
/*     */   }
/*     */ 
/*     */   public EndpointReference readEndpointReference(Source eprInfoset)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       Unmarshaller unmarshaller = ((JAXBContext)eprjc.get()).createUnmarshaller();
/* 144 */       return (EndpointReference)unmarshaller.unmarshal(eprInfoset);
/*     */     } catch (JAXBException e) {
/* 146 */       throw new WebServiceException("Error creating Marshaller or marshalling.", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T getPort(EndpointReference endpointReference, Class<T> clazz, WebServiceFeature[] webServiceFeatures)
/*     */   {
/* 156 */     if (endpointReference == null)
/* 157 */       throw new WebServiceException(ProviderApiMessages.NULL_EPR());
/* 158 */     WSEndpointReference wsepr = new WSEndpointReference(endpointReference);
/* 159 */     WSEndpointReference.Metadata metadata = wsepr.getMetaData();
/*     */     WSService service;
/* 161 */     if (metadata.getWsdlSource() != null)
/* 162 */       service = new WSServiceDelegate(metadata.getWsdlSource(), metadata.getServiceName(), Service.class);
/*     */     else
/* 164 */       throw new WebServiceException("WSDL metadata is missing in EPR");
/*     */     WSService service;
/* 165 */     return service.getPort(wsepr, clazz, webServiceFeatures);
/*     */   }
/*     */ 
/*     */   public W3CEndpointReference createW3CEndpointReference(String address, QName serviceName, QName portName, List<Element> metadata, String wsdlDocumentLocation, List<Element> referenceParameters) {
/* 169 */     return createW3CEndpointReference(address, null, serviceName, portName, metadata, wsdlDocumentLocation, referenceParameters, null, null);
/*     */   }
/*     */ 
/*     */   public W3CEndpointReference createW3CEndpointReference(String address, QName interfaceName, QName serviceName, QName portName, List<Element> metadata, String wsdlDocumentLocation, List<Element> referenceParameters, List<Element> elements, Map<QName, String> attributes)
/*     */   {
/* 175 */     Container container = ContainerResolver.getInstance().getContainer();
/* 176 */     if (address == null) {
/* 177 */       if ((serviceName == null) || (portName == null)) {
/* 178 */         throw new IllegalStateException(ProviderApiMessages.NULL_ADDRESS_SERVICE_ENDPOINT());
/*     */       }
/*     */ 
/* 181 */       Module module = (Module)container.getSPI(Module.class);
/* 182 */       if (module != null) {
/* 183 */         List beList = module.getBoundEndpoints();
/* 184 */         for (BoundEndpoint be : beList) {
/* 185 */           WSEndpoint wse = be.getEndpoint();
/* 186 */           if ((wse.getServiceName().equals(serviceName)) && (wse.getPortName().equals(portName))) {
/*     */             try {
/* 188 */               address = be.getAddress().toString();
/*     */             }
/*     */             catch (WebServiceException e)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 198 */       if (address == null) {
/* 199 */         throw new IllegalStateException(ProviderApiMessages.NULL_ADDRESS());
/*     */       }
/*     */     }
/* 202 */     if ((serviceName == null) && (portName != null)) {
/* 203 */       throw new IllegalStateException(ProviderApiMessages.NULL_SERVICE());
/*     */     }
/*     */ 
/* 206 */     String wsdlTargetNamespace = null;
/* 207 */     if (wsdlDocumentLocation != null) {
/*     */       try {
/* 209 */         EntityResolver er = XmlUtil.createDefaultCatalogResolver();
/*     */ 
/* 211 */         URL wsdlLoc = new URL(wsdlDocumentLocation);
/* 212 */         WSDLModelImpl wsdlDoc = RuntimeWSDLParser.parse(wsdlLoc, new StreamSource(wsdlLoc.toExternalForm()), er, true, container, (WSDLParserExtension[])ServiceFinder.find(WSDLParserExtension.class).toArray());
/*     */ 
/* 214 */         if (serviceName != null) {
/* 215 */           WSDLService wsdlService = wsdlDoc.getService(serviceName);
/* 216 */           if (wsdlService == null) {
/* 217 */             throw new IllegalStateException(ProviderApiMessages.NOTFOUND_SERVICE_IN_WSDL(serviceName, wsdlDocumentLocation));
/*     */           }
/* 219 */           if (portName != null) {
/* 220 */             WSDLPort wsdlPort = wsdlService.get(portName);
/* 221 */             if (wsdlPort == null) {
/* 222 */               throw new IllegalStateException(ProviderApiMessages.NOTFOUND_PORT_IN_WSDL(portName, serviceName, wsdlDocumentLocation));
/*     */             }
/*     */           }
/* 225 */           wsdlTargetNamespace = serviceName.getNamespaceURI();
/*     */         } else {
/* 227 */           QName firstService = wsdlDoc.getFirstServiceName();
/* 228 */           wsdlTargetNamespace = firstService.getNamespaceURI();
/*     */         }
/*     */       } catch (Exception e) {
/* 231 */         throw new IllegalStateException(ProviderApiMessages.ERROR_WSDL(wsdlDocumentLocation), e);
/*     */       }
/*     */     }
/* 234 */     return (W3CEndpointReference)new WSEndpointReference(AddressingVersion.fromSpecClass(W3CEndpointReference.class), address, serviceName, portName, interfaceName, metadata, wsdlDocumentLocation, wsdlTargetNamespace, referenceParameters, elements, attributes).toSpec(W3CEndpointReference.class);
/*     */   }
/*     */ 
/*     */   private static JAXBContext getEPRJaxbContext()
/*     */   {
/* 244 */     return (JAXBContext)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public JAXBContext run() {
/*     */         try {
/* 247 */           return JAXBContext.newInstance(new Class[] { MemberSubmissionEndpointReference.class, W3CEndpointReference.class });
/*     */         } catch (JAXBException e) {
/* 249 */           throw new WebServiceException("Error creating JAXBContext for W3CEndpointReference. ", e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.spi.ProviderImpl
 * JD-Core Version:    0.6.2
 */