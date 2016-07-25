/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.addressing.EPRSDDocumentFilter;
/*     */ import com.sun.xml.internal.ws.addressing.WSEPRExtension;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.Engine;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
/*     */ import com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext;
/*     */ import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubelineAssemblerFactory;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.EndpointAwareCodec;
/*     */ import com.sun.xml.internal.ws.api.server.EndpointComponent;
/*     */ import com.sun.xml.internal.ws.api.server.EndpointReferenceExtensionContributor;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint.PipeHead;
/*     */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLProperties;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.resources.HandlerMessages;
/*     */ import com.sun.xml.internal.ws.util.Pool;
/*     */ import com.sun.xml.internal.ws.util.Pool.TubePool;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public final class WSEndpointImpl<T> extends WSEndpoint<T>
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private final QName serviceName;
/*     */ 
/*     */   @NotNull
/*     */   private final QName portName;
/*     */   private final WSBinding binding;
/*     */   private final SEIModel seiModel;
/*     */ 
/*     */   @NotNull
/*     */   private final Container container;
/*     */   private final WSDLPort port;
/*     */   private final Tube masterTubeline;
/*     */   private final ServiceDefinitionImpl serviceDef;
/*     */   private final SOAPVersion soapVersion;
/*     */   private final Engine engine;
/*     */ 
/*     */   @NotNull
/*     */   private final Codec masterCodec;
/*     */ 
/*     */   @NotNull
/*     */   private final PolicyMap endpointPolicy;
/*     */   private final Pool<Tube> tubePool;
/*     */   private final OperationDispatcher operationDispatcher;
/*     */ 
/*     */   @NotNull
/*     */   private final ManagedObjectManager managedObjectManager;
/*  99 */   private boolean managedObjectManagerClosed = false;
/*     */ 
/*     */   @NotNull
/*     */   private final ServerTubeAssemblerContext context;
/* 102 */   private Map<QName, WSEndpointReference.EPRExtension> endpointReferenceExtensions = new HashMap();
/*     */   private boolean disposed;
/*     */   private final Class<T> implementationClass;
/*     */ 
/*     */   @Nullable
/*     */   private final WSDLProperties wsdlProperties;
/* 113 */   private final Set<EndpointComponent> componentRegistry = new LinkedHashSet();
/*     */ 
/* 345 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.server.endpoint");
/*     */ 
/*     */   WSEndpointImpl(@NotNull QName serviceName, @NotNull QName portName, WSBinding binding, Container container, SEIModel seiModel, WSDLPort port, Class<T> implementationClass, @Nullable ServiceDefinitionImpl serviceDef, InvokerTube terminalTube, boolean isSynchronous, PolicyMap endpointPolicy)
/*     */   {
/* 121 */     this.serviceName = serviceName;
/* 122 */     this.portName = portName;
/* 123 */     this.binding = binding;
/* 124 */     this.soapVersion = binding.getSOAPVersion();
/* 125 */     this.container = container;
/* 126 */     this.port = port;
/* 127 */     this.implementationClass = implementationClass;
/* 128 */     this.serviceDef = serviceDef;
/* 129 */     this.seiModel = seiModel;
/* 130 */     this.endpointPolicy = endpointPolicy;
/*     */ 
/* 132 */     this.managedObjectManager = new MonitorRootService(this).createManagedObjectManager(this);
/*     */ 
/* 135 */     if (serviceDef != null) {
/* 136 */       serviceDef.setOwner(this);
/*     */     }
/*     */ 
/* 139 */     TubelineAssembler assembler = TubelineAssemblerFactory.create(Thread.currentThread().getContextClassLoader(), binding.getBindingId(), container);
/*     */ 
/* 141 */     assert (assembler != null);
/*     */ 
/* 143 */     this.operationDispatcher = (port == null ? null : new OperationDispatcher(port, binding, seiModel));
/*     */ 
/* 145 */     this.context = new ServerPipeAssemblerContext(seiModel, port, this, terminalTube, isSynchronous);
/* 146 */     this.masterTubeline = assembler.createServer(this.context);
/*     */ 
/* 148 */     Codec c = this.context.getCodec();
/* 149 */     if ((c instanceof EndpointAwareCodec))
/*     */     {
/* 151 */       c = c.copy();
/* 152 */       ((EndpointAwareCodec)c).setEndpoint(this);
/*     */     }
/* 154 */     this.masterCodec = c;
/*     */ 
/* 156 */     this.tubePool = new Pool.TubePool(this.masterTubeline);
/* 157 */     terminalTube.setEndpoint(this);
/* 158 */     this.engine = new Engine(toString());
/* 159 */     this.wsdlProperties = (port == null ? null : new WSDLProperties(port));
/*     */ 
/* 161 */     Map eprExtensions = new HashMap();
/*     */     try {
/* 163 */       if (port != null)
/*     */       {
/* 165 */         WSEndpointReference wsdlEpr = ((WSDLPortImpl)port).getEPR();
/* 166 */         if (wsdlEpr != null) {
/* 167 */           for (WSEndpointReference.EPRExtension extnEl : wsdlEpr.getEPRExtensions()) {
/* 168 */             eprExtensions.put(extnEl.getQName(), extnEl);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 173 */       EndpointReferenceExtensionContributor[] eprExtnContributors = (EndpointReferenceExtensionContributor[])ServiceFinder.find(EndpointReferenceExtensionContributor.class).toArray();
/* 174 */       for (EndpointReferenceExtensionContributor eprExtnContributor : eprExtnContributors) {
/* 175 */         WSEndpointReference.EPRExtension wsdlEPRExtn = (WSEndpointReference.EPRExtension)eprExtensions.remove(eprExtnContributor.getQName());
/* 176 */         WSEndpointReference.EPRExtension endpointEprExtn = eprExtnContributor.getEPRExtension(this, wsdlEPRExtn);
/* 177 */         if (endpointEprExtn != null) {
/* 178 */           eprExtensions.put(endpointEprExtn.getQName(), endpointEprExtn);
/*     */         }
/*     */       }
/* 181 */       for (WSEndpointReference.EPRExtension extn : eprExtensions.values())
/* 182 */         this.endpointReferenceExtensions.put(extn.getQName(), new WSEPRExtension(XMLStreamBuffer.createNewBufferFromXMLStreamReader(extn.readAsXMLStreamReader()), extn.getQName()));
/*     */     }
/*     */     catch (XMLStreamException ex)
/*     */     {
/* 186 */       throw new WebServiceException(ex);
/*     */     }
/* 188 */     if (!eprExtensions.isEmpty())
/* 189 */       serviceDef.addFilter(new EPRSDDocumentFilter(this));
/*     */   }
/*     */ 
/*     */   public Collection<WSEndpointReference.EPRExtension> getEndpointReferenceExtensions()
/*     */   {
/* 195 */     return this.endpointReferenceExtensions.values();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public OperationDispatcher getOperationDispatcher()
/*     */   {
/* 202 */     return this.operationDispatcher;
/*     */   }
/*     */ 
/*     */   public PolicyMap getPolicyMap() {
/* 206 */     return this.endpointPolicy;
/*     */   }
/*     */   @NotNull
/*     */   public Class<T> getImplementationClass() {
/* 210 */     return this.implementationClass;
/*     */   }
/*     */   @NotNull
/*     */   public WSBinding getBinding() {
/* 214 */     return this.binding;
/*     */   }
/*     */   @NotNull
/*     */   public Container getContainer() {
/* 218 */     return this.container;
/*     */   }
/*     */ 
/*     */   public WSDLPort getPort() {
/* 222 */     return this.port;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public SEIModel getSEIModel() {
/* 227 */     return this.seiModel;
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor exec) {
/* 231 */     this.engine.setExecutor(exec);
/*     */   }
/*     */ 
/*     */   public void schedule(Packet request, WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor) {
/* 235 */     processAsync(request, callback, interceptor, true);
/*     */   }
/*     */ 
/*     */   private void processAsync(final Packet request, final WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor, boolean schedule) {
/* 239 */     request.endpoint = this;
/* 240 */     if (this.wsdlProperties != null) {
/* 241 */       request.addSatellite(this.wsdlProperties);
/*     */     }
/* 243 */     Fiber fiber = this.engine.createFiber();
/* 244 */     if (interceptor != null) {
/* 245 */       fiber.addInterceptor(interceptor);
/*     */     }
/* 247 */     final Tube tube = (Tube)this.tubePool.take();
/*     */ 
/* 249 */     Fiber.CompletionCallback cbak = new Fiber.CompletionCallback() {
/*     */       public void onCompletion(@NotNull Packet response) {
/* 251 */         WSEndpointImpl.this.tubePool.recycle(tube);
/* 252 */         if (callback != null)
/* 253 */           callback.onCompletion(response);
/*     */       }
/*     */ 
/*     */       public void onCompletion(@NotNull Throwable error)
/*     */       {
/* 260 */         error.printStackTrace();
/*     */ 
/* 264 */         Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(WSEndpointImpl.this.soapVersion, null, error);
/*     */ 
/* 266 */         Packet response = request.createServerResponse(faultMsg, request.endpoint.getPort(), null, request.endpoint.getBinding());
/*     */ 
/* 268 */         if (callback != null)
/* 269 */           callback.onCompletion(response);
/*     */       }
/*     */     };
/* 273 */     if (schedule)
/* 274 */       fiber.start(tube, request, cbak);
/*     */     else
/* 276 */       fiber.runAsync(tube, request, cbak);
/*     */   }
/*     */ 
/*     */   public void process(Packet request, WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor)
/*     */   {
/* 282 */     processAsync(request, callback, interceptor, false);
/*     */   }
/*     */   @NotNull
/*     */   public WSEndpoint.PipeHead createPipeHead() {
/* 286 */     return new WSEndpoint.PipeHead() {
/* 287 */       private final Tube tube = TubeCloner.clone(WSEndpointImpl.this.masterTubeline);
/*     */ 
/* 290 */       @NotNull
/*     */       public Packet process(Packet request, WebServiceContextDelegate wscd, TransportBackChannel tbc) { request.webServiceContextDelegate = wscd;
/* 291 */         request.transportBackChannel = tbc;
/* 292 */         request.endpoint = WSEndpointImpl.this;
/* 293 */         if (WSEndpointImpl.this.wsdlProperties != null) {
/* 294 */           request.addSatellite(WSEndpointImpl.this.wsdlProperties);
/* 296 */         }Fiber fiber = WSEndpointImpl.this.engine.createFiber();
/*     */         Packet response;
/*     */         try {
/* 299 */           response = fiber.runSync(this.tube, request);
/*     */         }
/*     */         catch (RuntimeException re)
/*     */         {
/* 304 */           re.printStackTrace();
/* 305 */           Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(WSEndpointImpl.this.soapVersion, null, re);
/*     */ 
/* 307 */           response = request.createServerResponse(faultMsg, request.endpoint.getPort(), null, request.endpoint.getBinding());
/*     */         }
/* 309 */         return response; }
/*     */     };
/*     */   }
/*     */ 
/*     */   public synchronized void dispose()
/*     */   {
/* 315 */     if (this.disposed)
/* 316 */       return;
/* 317 */     this.disposed = true;
/*     */ 
/* 319 */     this.masterTubeline.preDestroy();
/*     */ 
/* 321 */     for (Handler handler : this.binding.getHandlerChain()) {
/* 322 */       for (Method method : handler.getClass().getMethods()) {
/* 323 */         if (method.getAnnotation(PreDestroy.class) != null)
/*     */         {
/*     */           try
/*     */           {
/* 327 */             method.invoke(handler, new Object[0]);
/*     */           } catch (Exception e) {
/* 329 */             logger.log(Level.WARNING, HandlerMessages.HANDLER_PREDESTROY_IGNORE(e.getMessage()), e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 334 */     closeManagedObjectManager();
/*     */   }
/*     */ 
/*     */   public ServiceDefinitionImpl getServiceDefinition() {
/* 338 */     return this.serviceDef;
/*     */   }
/*     */ 
/*     */   public Set<EndpointComponent> getComponentRegistry() {
/* 342 */     return this.componentRegistry;
/*     */   }
/*     */ 
/*     */   public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, String address, String wsdlAddress, Element[] referenceParameters)
/*     */   {
/* 350 */     List refParams = null;
/* 351 */     if (referenceParameters != null) {
/* 352 */       refParams = Arrays.asList(referenceParameters);
/*     */     }
/* 354 */     return getEndpointReference(clazz, address, wsdlAddress, null, refParams);
/*     */   }
/*     */ 
/*     */   public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, String address, String wsdlAddress, List<Element> metadata, List<Element> referenceParameters)
/*     */   {
/* 359 */     QName portType = null;
/* 360 */     if (this.port != null) {
/* 361 */       portType = this.port.getBinding().getPortTypeName();
/*     */     }
/*     */ 
/* 364 */     AddressingVersion av = AddressingVersion.fromSpecClass(clazz);
/* 365 */     return new WSEndpointReference(av, address, this.serviceName, this.portName, portType, metadata, wsdlAddress, referenceParameters, this.endpointReferenceExtensions.values(), null).toSpec(clazz);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public QName getPortName()
/*     */   {
/* 371 */     return this.portName;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public Codec createCodec() {
/* 376 */     return this.masterCodec.copy();
/*     */   }
/*     */   @NotNull
/*     */   public QName getServiceName() {
/* 380 */     return this.serviceName;
/*     */   }
/*     */   @NotNull
/*     */   public ManagedObjectManager getManagedObjectManager() {
/* 384 */     return this.managedObjectManager;
/*     */   }
/*     */ 
/*     */   public void closeManagedObjectManager()
/*     */   {
/* 390 */     if (this.managedObjectManagerClosed == true) {
/* 391 */       return;
/*     */     }
/* 393 */     MonitorBase.closeMOM(this.managedObjectManager);
/* 394 */     this.managedObjectManagerClosed = true;
/*     */   }
/*     */   @NotNull
/*     */   public ServerTubeAssemblerContext getAssemblerContext() {
/* 398 */     return this.context;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.WSEndpointImpl
 * JD-Core Version:    0.6.2
 */