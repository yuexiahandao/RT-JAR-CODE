/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.Closeable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.WSService;
/*     */ import com.sun.xml.internal.ws.api.WSService.InitParams;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;
/*     */ import com.sun.xml.internal.ws.api.client.ServiceInterceptor;
/*     */ import com.sun.xml.internal.ws.api.client.ServiceInterceptorFactory;
/*     */ import com.sun.xml.internal.ws.api.pipe.Stubs;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.ContainerResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*     */ import com.sun.xml.internal.ws.client.sei.SEIStub;
/*     */ import com.sun.xml.internal.ws.developer.UsesJAXBContextFeature;
/*     */ import com.sun.xml.internal.ws.developer.WSBindingProvider;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.RuntimeModeler;
/*     */ import com.sun.xml.internal.ws.model.SOAPSEIModel;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.resources.DispatchMessages;
/*     */ import com.sun.xml.internal.ws.resources.ProviderApiMessages;
/*     */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*     */ import com.sun.xml.internal.ws.util.ServiceConfigurationError;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.jws.HandlerChain;
/*     */ import javax.jws.WebService;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.ws.Dispatch;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceClient;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.handler.HandlerResolver;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class WSServiceDelegate extends WSService
/*     */ {
/* 125 */   private final Map<QName, PortInfo> ports = new HashMap();
/*     */ 
/*     */   @NotNull
/* 132 */   private HandlerConfigurator handlerConfigurator = new HandlerConfigurator.HandlerResolverImpl(null);
/*     */   private final Class<? extends Service> serviceClass;
/*     */ 
/*     */   @NotNull
/*     */   private final QName serviceName;
/* 145 */   private final Map<QName, SEIPortInfo> seiContext = new HashMap();
/*     */   private volatile Executor executor;
/*     */ 
/*     */   @Nullable
/*     */   private WSDLServiceImpl wsdlService;
/*     */   private final Container container;
/*     */ 
/*     */   @NotNull
/*     */   final ServiceInterceptor serviceInterceptor;
/* 693 */   private static final WebServiceFeature[] EMPTY_FEATURES = new WebServiceFeature[0];
/*     */ 
/*     */   Map<QName, PortInfo> getQNameToPortInfoMap()
/*     */   {
/* 127 */     return this.ports;
/*     */   }
/*     */ 
/*     */   public WSServiceDelegate(URL wsdlDocumentLocation, QName serviceName, Class<? extends Service> serviceClass)
/*     */   {
/* 169 */     this(wsdlDocumentLocation == null ? null : new StreamSource(wsdlDocumentLocation.toExternalForm()), serviceName, serviceClass);
/*     */   }
/*     */ 
/*     */   public WSServiceDelegate(@Nullable Source wsdl, @NotNull QName serviceName, @NotNull final Class<? extends Service> serviceClass)
/*     */   {
/* 180 */     if (serviceName == null) {
/* 181 */       throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME_NULL(serviceName));
/*     */     }
/* 183 */     WSService.InitParams initParams = (WSService.InitParams)INIT_PARAMS.get();
/* 184 */     INIT_PARAMS.set(null);
/* 185 */     if (initParams == null) initParams = EMPTY_PARAMS;
/*     */ 
/* 187 */     this.serviceName = serviceName;
/* 188 */     this.serviceClass = serviceClass;
/* 189 */     Container tContainer = initParams.getContainer() != null ? initParams.getContainer() : ContainerResolver.getInstance().getContainer();
/* 190 */     if (tContainer == Container.NONE) {
/* 191 */       tContainer = new ClientContainer();
/*     */     }
/* 193 */     this.container = tContainer;
/*     */ 
/* 196 */     ServiceInterceptor interceptor = ServiceInterceptorFactory.load(this, Thread.currentThread().getContextClassLoader());
/* 197 */     ServiceInterceptor si = (ServiceInterceptor)this.container.getSPI(ServiceInterceptor.class);
/* 198 */     if (si != null) {
/* 199 */       interceptor = ServiceInterceptor.aggregate(new ServiceInterceptor[] { interceptor, si });
/*     */     }
/* 201 */     this.serviceInterceptor = interceptor;
/*     */ 
/* 205 */     if ((wsdl == null) && 
/* 206 */       (serviceClass != Service.class)) {
/* 207 */       WebServiceClient wsClient = (WebServiceClient)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public WebServiceClient run() {
/* 209 */           return (WebServiceClient)serviceClass.getAnnotation(WebServiceClient.class);
/*     */         }
/*     */       });
/* 212 */       String wsdlLocation = wsClient.wsdlLocation();
/* 213 */       wsdlLocation = JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(wsdlLocation));
/* 214 */       wsdl = new StreamSource(wsdlLocation);
/*     */     }
/*     */ 
/* 217 */     WSDLServiceImpl service = null;
/* 218 */     if (wsdl != null) {
/*     */       try {
/* 220 */         URL url = wsdl.getSystemId() == null ? null : new URL(wsdl.getSystemId());
/* 221 */         WSDLModelImpl model = parseWSDL(url, wsdl);
/* 222 */         service = model.getService(this.serviceName);
/* 223 */         if (service == null) {
/* 224 */           throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(model.getServices().keySet())));
/*     */         }
/*     */ 
/* 228 */         for (WSDLPortImpl port : service.getPorts())
/* 229 */           this.ports.put(port.getName(), new PortInfo(this, port));
/*     */       } catch (MalformedURLException e) {
/* 231 */         throw new WebServiceException(ClientMessages.INVALID_WSDL_URL(wsdl.getSystemId()));
/*     */       }
/*     */     }
/* 234 */     this.wsdlService = service;
/*     */ 
/* 236 */     if (serviceClass != Service.class)
/*     */     {
/* 238 */       HandlerChain handlerChain = (HandlerChain)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public HandlerChain run() {
/* 241 */           return (HandlerChain)serviceClass.getAnnotation(HandlerChain.class);
/*     */         }
/*     */       });
/* 244 */       if (handlerChain != null)
/* 245 */         this.handlerConfigurator = new HandlerConfigurator.AnnotationConfigurator(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   private WSDLModelImpl parseWSDL(URL wsdlDocumentLocation, Source wsdlSource)
/*     */   {
/*     */     try
/*     */     {
/* 258 */       return RuntimeWSDLParser.parse(wsdlDocumentLocation, wsdlSource, XmlUtil.createDefaultCatalogResolver(), true, getContainer(), (WSDLParserExtension[])ServiceFinder.find(WSDLParserExtension.class).toArray());
/*     */     }
/*     */     catch (IOException e) {
/* 261 */       throw new WebServiceException(e);
/*     */     } catch (XMLStreamException e) {
/* 263 */       throw new WebServiceException(e);
/*     */     } catch (SAXException e) {
/* 265 */       throw new WebServiceException(e);
/*     */     } catch (ServiceConfigurationError e) {
/* 267 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 275 */     return this.executor;
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor executor) {
/* 279 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   public HandlerResolver getHandlerResolver() {
/* 283 */     return this.handlerConfigurator.getResolver();
/*     */   }
/*     */ 
/*     */   final HandlerConfigurator getHandlerConfigurator() {
/* 287 */     return this.handlerConfigurator;
/*     */   }
/*     */ 
/*     */   public void setHandlerResolver(HandlerResolver resolver) {
/* 291 */     this.handlerConfigurator = new HandlerConfigurator.HandlerResolverImpl(resolver);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(QName portName, Class<T> portInterface) throws WebServiceException {
/* 295 */     return getPort(portName, portInterface, EMPTY_FEATURES);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(QName portName, Class<T> portInterface, WebServiceFeature[] features) {
/* 299 */     if ((portName == null) || (portInterface == null))
/* 300 */       throw new IllegalArgumentException();
/* 301 */     WSDLServiceImpl tWsdlService = this.wsdlService;
/* 302 */     if (tWsdlService == null)
/*     */     {
/* 305 */       tWsdlService = getWSDLModelfromSEI(portInterface);
/*     */ 
/* 307 */       if (tWsdlService == null) {
/* 308 */         throw new WebServiceException(ProviderApiMessages.NO_WSDL_NO_PORT(portInterface.getName()));
/*     */       }
/*     */     }
/*     */ 
/* 312 */     WSDLPortImpl portModel = getPortModel(tWsdlService, portName);
/* 313 */     return getPort(portModel.getEPR(), portName, portInterface, features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(EndpointReference epr, Class<T> portInterface, WebServiceFeature[] features) {
/* 317 */     return getPort(WSEndpointReference.create(epr), portInterface, features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(WSEndpointReference wsepr, Class<T> portInterface, WebServiceFeature[] features)
/*     */   {
/* 322 */     QName portTypeName = RuntimeModeler.getPortTypeName(portInterface);
/*     */ 
/* 324 */     QName portName = getPortNameFromEPR(wsepr, portTypeName);
/* 325 */     return getPort(wsepr, portName, portInterface, features);
/*     */   }
/*     */ 
/*     */   private <T> T getPort(WSEndpointReference wsepr, QName portName, Class<T> portInterface, WebServiceFeature[] features)
/*     */   {
/* 330 */     SEIPortInfo spi = addSEI(portName, portInterface, features);
/* 331 */     return createEndpointIFBaseProxy(wsepr, portName, portInterface, features, spi);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(Class<T> portInterface, WebServiceFeature[] features) {
/* 335 */     QName portTypeName = RuntimeModeler.getPortTypeName(portInterface);
/* 336 */     WSDLServiceImpl wsdlService = this.wsdlService;
/* 337 */     if (wsdlService == null)
/*     */     {
/* 340 */       wsdlService = getWSDLModelfromSEI(portInterface);
/*     */ 
/* 342 */       if (wsdlService == null) {
/* 343 */         throw new WebServiceException(ProviderApiMessages.NO_WSDL_NO_PORT(portInterface.getName()));
/*     */       }
/*     */     }
/*     */ 
/* 347 */     WSDLPortImpl port = wsdlService.getMatchingPort(portTypeName);
/* 348 */     if (port == null)
/* 349 */       throw new WebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(portTypeName));
/* 350 */     QName portName = port.getName();
/* 351 */     return getPort(portName, portInterface, features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(Class<T> portInterface) throws WebServiceException {
/* 355 */     return getPort(portInterface, EMPTY_FEATURES);
/*     */   }
/*     */ 
/*     */   public void addPort(QName portName, String bindingId, String endpointAddress) throws WebServiceException {
/* 359 */     if (!this.ports.containsKey(portName)) {
/* 360 */       BindingID bid = bindingId == null ? BindingID.SOAP11_HTTP : BindingID.parse(bindingId);
/* 361 */       this.ports.put(portName, new PortInfo(this, endpointAddress == null ? null : EndpointAddress.create(endpointAddress), portName, bid));
/*     */     }
/*     */     else
/*     */     {
/* 365 */       throw new WebServiceException(DispatchMessages.DUPLICATE_PORT(portName.toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(QName portName, Class<T> aClass, Service.Mode mode) throws WebServiceException {
/* 370 */     return createDispatch(portName, aClass, mode, EMPTY_FEATURES);
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(QName portName, WSEndpointReference wsepr, Class<T> aClass, Service.Mode mode, WebServiceFeature[] features)
/*     */   {
/* 375 */     PortInfo port = safeGetPort(portName);
/* 376 */     BindingImpl binding = port.createBinding(features, null);
/* 377 */     binding.setMode(mode);
/* 378 */     Dispatch dispatch = Stubs.createDispatch(port, this, binding, aClass, mode, wsepr);
/* 379 */     this.serviceInterceptor.postCreateDispatch((WSBindingProvider)dispatch);
/* 380 */     return dispatch;
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(QName portName, Class<T> aClass, Service.Mode mode, WebServiceFeature[] features) {
/* 384 */     WebServiceFeatureList featureList = new WebServiceFeatureList(features);
/* 385 */     WSEndpointReference wsepr = null;
/* 386 */     if ((featureList.isEnabled(AddressingFeature.class)) && (this.wsdlService != null) && (this.wsdlService.get(portName) != null)) {
/* 387 */       wsepr = this.wsdlService.get(portName).getEPR();
/*     */     }
/* 389 */     return createDispatch(portName, wsepr, aClass, mode, features);
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> type, Service.Mode mode, WebServiceFeature[] features) {
/* 393 */     WSEndpointReference wsepr = new WSEndpointReference(endpointReference);
/* 394 */     QName portName = addPortEpr(wsepr);
/* 395 */     return createDispatch(portName, wsepr, type, mode, features);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public PortInfo safeGetPort(QName portName)
/*     */   {
/* 404 */     PortInfo port = (PortInfo)this.ports.get(portName);
/* 405 */     if (port == null) {
/* 406 */       throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(portName, buildNameList(this.ports.keySet())));
/*     */     }
/* 408 */     return port;
/*     */   }
/*     */ 
/*     */   private StringBuilder buildNameList(Collection<QName> names) {
/* 412 */     StringBuilder sb = new StringBuilder();
/* 413 */     for (QName qn : names) {
/* 414 */       if (sb.length() > 0) sb.append(',');
/* 415 */       sb.append(qn);
/*     */     }
/* 417 */     return sb;
/*     */   }
/*     */ 
/*     */   public EndpointAddress getEndpointAddress(QName qName) {
/* 421 */     return ((PortInfo)this.ports.get(qName)).targetEndpoint;
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(QName portName, JAXBContext jaxbContext, Service.Mode mode) throws WebServiceException {
/* 425 */     return createDispatch(portName, jaxbContext, mode, EMPTY_FEATURES);
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(QName portName, WSEndpointReference wsepr, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeature[] features)
/*     */   {
/* 430 */     PortInfo port = safeGetPort(portName);
/* 431 */     BindingImpl binding = port.createBinding(features, null);
/* 432 */     binding.setMode(mode);
/* 433 */     Dispatch dispatch = Stubs.createJAXBDispatch(port, binding, jaxbContext, mode, wsepr);
/*     */ 
/* 435 */     this.serviceInterceptor.postCreateDispatch((WSBindingProvider)dispatch);
/* 436 */     return dispatch;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Container getContainer() {
/* 441 */     return this.container;
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(QName portName, JAXBContext jaxbContext, Service.Mode mode, WebServiceFeature[] webServiceFeatures) {
/* 445 */     WebServiceFeatureList featureList = new WebServiceFeatureList(webServiceFeatures);
/* 446 */     WSEndpointReference wsepr = null;
/* 447 */     if ((featureList.isEnabled(AddressingFeature.class)) && (this.wsdlService != null) && (this.wsdlService.get(portName) != null)) {
/* 448 */       wsepr = this.wsdlService.get(portName).getEPR();
/*     */     }
/* 450 */     return createDispatch(portName, wsepr, jaxbContext, mode, webServiceFeatures);
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext context, Service.Mode mode, WebServiceFeature[] features) {
/* 454 */     WSEndpointReference wsepr = new WSEndpointReference(endpointReference);
/* 455 */     QName portName = addPortEpr(wsepr);
/* 456 */     return createDispatch(portName, wsepr, context, mode, features);
/*     */   }
/*     */ 
/*     */   private QName addPortEpr(WSEndpointReference wsepr) {
/* 460 */     if (wsepr == null)
/* 461 */       throw new WebServiceException(ProviderApiMessages.NULL_EPR());
/* 462 */     QName eprPortName = getPortNameFromEPR(wsepr, null);
/*     */ 
/* 466 */     PortInfo portInfo = new PortInfo(this, wsepr.getAddress() == null ? null : EndpointAddress.create(wsepr.getAddress()), eprPortName, getPortModel(this.wsdlService, eprPortName).getBinding().getBindingId());
/*     */ 
/* 468 */     if (!this.ports.containsKey(eprPortName)) {
/* 469 */       this.ports.put(eprPortName, portInfo);
/*     */     }
/*     */ 
/* 472 */     return eprPortName;
/*     */   }
/*     */ 
/*     */   private QName getPortNameFromEPR(@NotNull WSEndpointReference wsepr, @Nullable QName portTypeName)
/*     */   {
/* 489 */     WSEndpointReference.Metadata metadata = wsepr.getMetaData();
/* 490 */     QName eprServiceName = metadata.getServiceName();
/* 491 */     QName eprPortName = metadata.getPortName();
/* 492 */     if ((eprServiceName != null) && (!eprServiceName.equals(this.serviceName))) {
/* 493 */       throw new WebServiceException("EndpointReference WSDL ServiceName differs from Service Instance WSDL Service QName.\n The two Service QNames must match");
/*     */     }
/*     */ 
/* 496 */     if (this.wsdlService == null) {
/* 497 */       Source eprWsdlSource = metadata.getWsdlSource();
/* 498 */       if (eprWsdlSource == null)
/* 499 */         throw new WebServiceException(ProviderApiMessages.NULL_WSDL());
/*     */       try
/*     */       {
/* 502 */         WSDLModelImpl eprWsdlMdl = parseWSDL(new URL(wsepr.getAddress()), eprWsdlSource);
/* 503 */         this.wsdlService = eprWsdlMdl.getService(this.serviceName);
/* 504 */         if (this.wsdlService == null)
/* 505 */           throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(eprWsdlMdl.getServices().keySet())));
/*     */       }
/*     */       catch (MalformedURLException e) {
/* 508 */         throw new WebServiceException(ClientMessages.INVALID_ADDRESS(wsepr.getAddress()));
/*     */       }
/*     */     }
/* 511 */     QName portName = eprPortName;
/*     */ 
/* 513 */     if ((portName == null) && (portTypeName != null))
/*     */     {
/* 515 */       WSDLPortImpl port = this.wsdlService.getMatchingPort(portTypeName);
/* 516 */       if (port == null)
/* 517 */         throw new WebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(portTypeName));
/* 518 */       portName = port.getName();
/*     */     }
/* 520 */     if (portName == null)
/* 521 */       throw new WebServiceException(ProviderApiMessages.NULL_PORTNAME());
/* 522 */     if (this.wsdlService.get(portName) == null) {
/* 523 */       throw new WebServiceException(ClientMessages.INVALID_EPR_PORT_NAME(portName, buildWsdlPortNames()));
/*     */     }
/* 525 */     return portName;
/*     */   }
/*     */ 
/*     */   private WSDLServiceImpl getWSDLModelfromSEI(final Class sei)
/*     */   {
/* 530 */     WebService ws = (WebService)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public WebService run() {
/* 532 */         return (WebService)sei.getAnnotation(WebService.class);
/*     */       }
/*     */     });
/* 535 */     if ((ws == null) || (ws.wsdlLocation().equals("")))
/* 536 */       return null;
/* 537 */     String wsdlLocation = ws.wsdlLocation();
/* 538 */     wsdlLocation = JAXWSUtils.absolutize(JAXWSUtils.getFileOrURLName(wsdlLocation));
/* 539 */     Source wsdl = new StreamSource(wsdlLocation);
/* 540 */     WSDLServiceImpl service = null;
/*     */     try
/*     */     {
/* 543 */       URL url = wsdl.getSystemId() == null ? null : new URL(wsdl.getSystemId());
/* 544 */       WSDLModelImpl model = parseWSDL(url, wsdl);
/* 545 */       service = model.getService(this.serviceName);
/* 546 */       if (service == null)
/* 547 */         throw new WebServiceException(ClientMessages.INVALID_SERVICE_NAME(this.serviceName, buildNameList(model.getServices().keySet())));
/*     */     }
/*     */     catch (MalformedURLException e)
/*     */     {
/* 551 */       throw new WebServiceException(ClientMessages.INVALID_WSDL_URL(wsdl.getSystemId()));
/*     */     }
/* 553 */     return service;
/*     */   }
/*     */ 
/*     */   public QName getServiceName() {
/* 557 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */   protected Class getServiceClass() {
/* 561 */     return this.serviceClass;
/*     */   }
/*     */ 
/*     */   public Iterator<QName> getPorts()
/*     */     throws WebServiceException
/*     */   {
/* 567 */     return this.ports.keySet().iterator();
/*     */   }
/*     */ 
/*     */   public URL getWSDLDocumentLocation() {
/* 571 */     if (this.wsdlService == null) return null; try
/*     */     {
/* 573 */       return new URL(this.wsdlService.getParent().getLocation().getSystemId());
/*     */     } catch (MalformedURLException e) {
/* 575 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T> T createEndpointIFBaseProxy(@Nullable WSEndpointReference epr, QName portName, Class<T> portInterface, WebServiceFeature[] webServiceFeatures, SEIPortInfo eif)
/*     */   {
/* 582 */     if (this.wsdlService == null) {
/* 583 */       throw new WebServiceException(ClientMessages.INVALID_SERVICE_NO_WSDL(this.serviceName));
/*     */     }
/* 585 */     if (this.wsdlService.get(portName) == null) {
/* 586 */       throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(portName, buildWsdlPortNames()));
/*     */     }
/*     */ 
/* 590 */     BindingImpl binding = eif.createBinding(webServiceFeatures, portInterface);
/* 591 */     SEIStub pis = new SEIStub(eif, binding, eif.model, epr);
/*     */ 
/* 593 */     Object proxy = createProxy(portInterface, pis);
/*     */ 
/* 595 */     if (this.serviceInterceptor != null) {
/* 596 */       this.serviceInterceptor.postCreateProxy((WSBindingProvider)proxy, portInterface);
/*     */     }
/* 598 */     return proxy;
/*     */   }
/*     */ 
/*     */   private <T> T createProxy(final Class<T> portInterface, final SEIStub pis)
/*     */   {
/* 604 */     RuntimePermission perm = new RuntimePermission("accessClassInPackage.com.sun.xml.internal.*");
/* 605 */     PermissionCollection perms = perm.newPermissionCollection();
/* 606 */     perms.add(perm);
/*     */ 
/* 608 */     return AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public T run()
/*     */       {
/* 612 */         Object proxy = Proxy.newProxyInstance(portInterface.getClassLoader(), new Class[] { portInterface, WSBindingProvider.class, Closeable.class }, pis);
/*     */ 
/* 614 */         return portInterface.cast(proxy);
/*     */       }
/*     */     }
/*     */     , new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, perms) }));
/*     */   }
/*     */ 
/*     */   private StringBuilder buildWsdlPortNames()
/*     */   {
/* 628 */     Set wsdlPortNames = new HashSet();
/* 629 */     for (WSDLPortImpl port : this.wsdlService.getPorts())
/* 630 */       wsdlPortNames.add(port.getName());
/* 631 */     return buildNameList(wsdlPortNames);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private WSDLPortImpl getPortModel(WSDLServiceImpl wsdlService, QName portName)
/*     */   {
/* 640 */     WSDLPortImpl port = wsdlService.get(portName);
/* 641 */     if (port == null) {
/* 642 */       throw new WebServiceException(ClientMessages.INVALID_PORT_NAME(portName, buildWsdlPortNames()));
/*     */     }
/* 644 */     return port;
/*     */   }
/*     */ 
/*     */   private SEIPortInfo addSEI(QName portName, Class portInterface, WebServiceFeature[] features)
/*     */     throws WebServiceException
/*     */   {
/* 653 */     boolean ownModel = useOwnSEIModel(features);
/* 654 */     if (ownModel)
/*     */     {
/* 656 */       return createSEIPortInfo(portName, portInterface, features);
/*     */     }
/*     */ 
/* 659 */     SEIPortInfo spi = (SEIPortInfo)this.seiContext.get(portName);
/* 660 */     if (spi == null) {
/* 661 */       spi = createSEIPortInfo(portName, portInterface, features);
/* 662 */       this.seiContext.put(spi.portName, spi);
/* 663 */       this.ports.put(spi.portName, spi);
/*     */     }
/* 665 */     return spi;
/*     */   }
/*     */ 
/*     */   private SEIPortInfo createSEIPortInfo(QName portName, Class portInterface, WebServiceFeature[] features) {
/* 669 */     WSDLPortImpl wsdlPort = getPortModel(this.wsdlService, portName);
/* 670 */     RuntimeModeler modeler = new RuntimeModeler(portInterface, this.serviceName, wsdlPort, features);
/* 671 */     modeler.setClassLoader(portInterface.getClassLoader());
/* 672 */     modeler.setPortName(portName);
/* 673 */     AbstractSEIModelImpl model = modeler.buildRuntimeModel();
/* 674 */     return new SEIPortInfo(this, portInterface, (SOAPSEIModel)model, wsdlPort);
/*     */   }
/*     */ 
/*     */   private boolean useOwnSEIModel(WebServiceFeature[] features) {
/* 678 */     return WebServiceFeatureList.getFeature(features, UsesJAXBContextFeature.class) != null;
/*     */   }
/*     */ 
/*     */   public WSDLServiceImpl getWsdlService() {
/* 682 */     return this.wsdlService;
/*     */   }
/*     */   class DaemonThreadFactory implements ThreadFactory {
/*     */     DaemonThreadFactory() {
/*     */     }
/* 687 */     public Thread newThread(Runnable r) { Thread daemonThread = new Thread(r);
/* 688 */       daemonThread.setDaemon(Boolean.TRUE.booleanValue());
/* 689 */       return daemonThread;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.WSServiceDelegate
 * JD-Core Version:    0.6.2
 */