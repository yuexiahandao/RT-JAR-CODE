/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
/*     */ import com.sun.xml.internal.ws.addressing.WSEPRExtension;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.EPRExtension;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
/*     */ import com.sun.xml.internal.ws.api.pipe.Engine;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubelineAssemblerFactory;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.developer.WSBindingProvider;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLProperties;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.server.MonitorBase;
/*     */ import com.sun.xml.internal.ws.util.Pool;
/*     */ import com.sun.xml.internal.ws.util.Pool.TubePool;
/*     */ import com.sun.xml.internal.ws.util.RuntimeVersion;
/*     */ import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.RespectBindingFeature;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*     */ 
/*     */ public abstract class Stub
/*     */   implements WSBindingProvider, ResponseContextReceiver
/*     */ {
/*     */   private Pool<Tube> tubes;
/*     */   private final Engine engine;
/*     */   protected final WSServiceDelegate owner;
/*     */ 
/*     */   @Nullable
/*     */   protected WSEndpointReference endpointReference;
/*     */   protected final BindingImpl binding;
/*     */   protected final WSPortInfo portInfo;
/*     */   protected final AddressingVersion addrVersion;
/* 116 */   public final RequestContext requestContext = new RequestContext();
/*     */   private ResponseContext responseContext;
/*     */ 
/*     */   @Nullable
/*     */   protected final WSDLPort wsdlPort;
/*     */ 
/*     */   @Nullable
/*     */   private volatile Header[] userOutboundHeaders;
/*     */ 
/*     */   @Nullable
/*     */   private final WSDLProperties wsdlProperties;
/* 131 */   protected OperationDispatcher operationDispatcher = null;
/*     */ 
/*     */   @NotNull
/*     */   private final ManagedObjectManager managedObjectManager;
/* 133 */   private boolean managedObjectManagerClosed = false;
/*     */ 
/*     */   @Deprecated
/*     */   protected Stub(WSServiceDelegate owner, Tube master, BindingImpl binding, WSDLPort wsdlPort, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr)
/*     */   {
/* 148 */     this(owner, master, null, binding, wsdlPort, defaultEndPointAddress, epr);
/*     */   }
/*     */ 
/*     */   protected Stub(WSPortInfo portInfo, BindingImpl binding, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr)
/*     */   {
/* 163 */     this((WSServiceDelegate)portInfo.getOwner(), null, portInfo, binding, portInfo.getPort(), defaultEndPointAddress, epr);
/*     */   }
/*     */ 
/*     */   private Stub(WSServiceDelegate owner, @Nullable Tube master, @Nullable WSPortInfo portInfo, BindingImpl binding, @Nullable WSDLPort wsdlPort, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr)
/*     */   {
/* 168 */     this.owner = owner;
/* 169 */     this.portInfo = portInfo;
/* 170 */     this.wsdlPort = portInfo.getPort();
/* 171 */     this.binding = binding;
/* 172 */     this.addrVersion = binding.getAddressingVersion();
/*     */ 
/* 174 */     if (epr != null)
/* 175 */       this.requestContext.setEndPointAddressString(epr.getAddress());
/*     */     else
/* 177 */       this.requestContext.setEndpointAddress(defaultEndPointAddress);
/* 178 */     this.engine = new Engine(toString(), owner.getExecutor());
/* 179 */     this.endpointReference = epr;
/* 180 */     this.wsdlProperties = (wsdlPort == null ? null : new WSDLProperties(wsdlPort));
/*     */ 
/* 185 */     this.managedObjectManager = new MonitorRootClient(this).createManagedObjectManager(this);
/*     */ 
/* 187 */     if (master != null)
/* 188 */       this.tubes = new Pool.TubePool(master);
/*     */     else {
/* 190 */       this.tubes = new Pool.TubePool(createPipeline(portInfo, binding));
/*     */     }
/*     */ 
/* 194 */     this.managedObjectManager.resumeJMXRegistration();
/*     */   }
/*     */ 
/*     */   private Tube createPipeline(WSPortInfo portInfo, WSBinding binding)
/*     */   {
/* 202 */     checkAllWSDLExtensionsUnderstood(portInfo, binding);
/* 203 */     SEIModel seiModel = null;
/* 204 */     if ((portInfo instanceof SEIPortInfo)) {
/* 205 */       seiModel = ((SEIPortInfo)portInfo).model;
/*     */     }
/* 207 */     BindingID bindingId = portInfo.getBindingId();
/*     */ 
/* 209 */     TubelineAssembler assembler = TubelineAssemblerFactory.create(Thread.currentThread().getContextClassLoader(), bindingId);
/*     */ 
/* 211 */     if (assembler == null)
/* 212 */       throw new WebServiceException("Unable to process bindingID=" + bindingId);
/* 213 */     return assembler.createClient(new ClientTubeAssemblerContext(portInfo.getEndpointAddress(), portInfo.getPort(), this, binding, this.owner.getContainer(), ((BindingImpl)binding).createCodec(), seiModel));
/*     */   }
/*     */ 
/*     */   private static void checkAllWSDLExtensionsUnderstood(WSPortInfo port, WSBinding binding)
/*     */   {
/* 228 */     if ((port.getPort() != null) && (binding.isFeatureEnabled(RespectBindingFeature.class)))
/* 229 */       ((WSDLPortImpl)port.getPort()).areRequiredExtensionsUnderstood();
/*     */   }
/*     */ 
/*     */   public WSPortInfo getPortInfo()
/*     */   {
/* 234 */     return this.portInfo;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public OperationDispatcher getOperationDispatcher()
/*     */   {
/* 242 */     if ((this.operationDispatcher == null) && (this.wsdlPort != null))
/* 243 */       this.operationDispatcher = new OperationDispatcher(this.wsdlPort, this.binding, null);
/* 244 */     return this.operationDispatcher;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   protected abstract QName getPortName();
/*     */ 
/*     */   @NotNull
/*     */   protected final QName getServiceName()
/*     */   {
/* 265 */     return this.owner.getServiceName();
/*     */   }
/*     */ 
/*     */   public final Executor getExecutor()
/*     */   {
/* 277 */     return this.owner.getExecutor();
/*     */   }
/*     */ 
/*     */   protected final Packet process(Packet packet, RequestContext requestContext, ResponseContextReceiver receiver)
/*     */   {
/* 298 */     configureRequestPacket(packet, requestContext);
/* 299 */     Pool pool = this.tubes;
/* 300 */     if (pool == null) {
/* 301 */       throw new WebServiceException("close method has already been invoked");
/*     */     }
/* 303 */     Fiber fiber = this.engine.createFiber();
/*     */ 
/* 305 */     Tube tube = (Tube)pool.take();
/*     */     try
/*     */     {
/*     */       Packet reply;
/* 308 */       return fiber.runSync(tube, packet);
/*     */     }
/*     */     finally
/*     */     {
/* 316 */       Packet reply = fiber.getPacket() == null ? packet : fiber.getPacket();
/* 317 */       receiver.setResponseContext(new ResponseContext(reply));
/*     */ 
/* 319 */       pool.recycle(tube);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void configureRequestPacket(Packet packet, RequestContext requestContext)
/*     */   {
/* 325 */     packet.proxy = this;
/* 326 */     packet.handlerConfig = this.binding.getHandlerConfig();
/* 327 */     requestContext.fill(packet, this.binding.getAddressingVersion() != null);
/* 328 */     if (this.wsdlProperties != null) {
/* 329 */       packet.addSatellite(this.wsdlProperties);
/*     */     }
/* 331 */     if (this.addrVersion != null)
/*     */     {
/* 333 */       HeaderList headerList = packet.getMessage().getHeaders();
/* 334 */       headerList.fillRequestAddressingHeaders(this.wsdlPort, this.binding, packet);
/*     */ 
/* 340 */       if (this.endpointReference != null) {
/* 341 */         this.endpointReference.addReferenceParameters(packet.getMessage().getHeaders());
/*     */       }
/*     */     }
/*     */ 
/* 345 */     Header[] hl = this.userOutboundHeaders;
/* 346 */     if (hl != null)
/* 347 */       packet.getMessage().getHeaders().addAll(hl);
/*     */   }
/*     */ 
/*     */   protected final void processAsync(Packet request, RequestContext requestContext, final Fiber.CompletionCallback completionCallback)
/*     */   {
/* 369 */     configureRequestPacket(request, requestContext);
/*     */ 
/* 371 */     final Pool pool = this.tubes;
/* 372 */     if (pool == null) {
/* 373 */       throw new WebServiceException("close method has already been invoked");
/*     */     }
/* 375 */     Fiber fiber = this.engine.createFiber();
/*     */ 
/* 377 */     final Tube tube = (Tube)pool.take();
/* 378 */     fiber.start(tube, request, new Fiber.CompletionCallback() {
/*     */       public void onCompletion(@NotNull Packet response) {
/* 380 */         pool.recycle(tube);
/* 381 */         completionCallback.onCompletion(response);
/*     */       }
/*     */ 
/*     */       public void onCompletion(@NotNull Throwable error)
/*     */       {
/* 386 */         completionCallback.onCompletion(error);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void close() {
/* 392 */     if (this.tubes != null)
/*     */     {
/* 396 */       Tube p = (Tube)this.tubes.take();
/* 397 */       this.tubes = null;
/* 398 */       p.preDestroy();
/*     */     }
/* 400 */     if (this.managedObjectManagerClosed) {
/* 401 */       return;
/*     */     }
/* 403 */     MonitorBase.closeMOM(this.managedObjectManager);
/* 404 */     this.managedObjectManagerClosed = true;
/*     */   }
/*     */ 
/*     */   public final WSBinding getBinding()
/*     */   {
/* 410 */     return this.binding;
/*     */   }
/*     */ 
/*     */   public final Map<String, Object> getRequestContext() {
/* 414 */     return this.requestContext.getMapView();
/*     */   }
/*     */ 
/*     */   public final ResponseContext getResponseContext() {
/* 418 */     return this.responseContext;
/*     */   }
/*     */ 
/*     */   public void setResponseContext(ResponseContext rc) {
/* 422 */     this.responseContext = rc;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 426 */     return RuntimeVersion.VERSION + ": Stub for " + getRequestContext().get("javax.xml.ws.service.endpoint.address");
/*     */   }
/*     */ 
/*     */   public final WSEndpointReference getWSEndpointReference() {
/* 430 */     if (this.binding.getBindingID().equals("http://www.w3.org/2004/08/wsdl/http")) {
/* 431 */       throw new UnsupportedOperationException(ClientMessages.UNSUPPORTED_OPERATION("BindingProvider.getEndpointReference(Class<T> class)", "XML/HTTP Binding", "SOAP11 or SOAP12 Binding"));
/*     */     }
/* 433 */     if (this.endpointReference != null) {
/* 434 */       return this.endpointReference;
/*     */     }
/*     */ 
/* 437 */     String eprAddress = this.requestContext.getEndpointAddress().toString();
/* 438 */     QName portTypeName = null;
/* 439 */     String wsdlAddress = null;
/* 440 */     List wsdlEPRExtensions = new ArrayList();
/* 441 */     if (this.wsdlPort != null) {
/* 442 */       portTypeName = this.wsdlPort.getBinding().getPortTypeName();
/* 443 */       wsdlAddress = eprAddress + "?wsdl";
/*     */       try
/*     */       {
/* 447 */         WSEndpointReference wsdlEpr = ((WSDLPortImpl)this.wsdlPort).getEPR();
/* 448 */         if (wsdlEpr != null) {
/* 449 */           for (WSEndpointReference.EPRExtension extnEl : wsdlEpr.getEPRExtensions()) {
/* 450 */             wsdlEPRExtensions.add(new WSEPRExtension(XMLStreamBuffer.createNewBufferFromXMLStreamReader(extnEl.readAsXMLStreamReader()), extnEl.getQName()));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (XMLStreamException ex)
/*     */       {
/* 456 */         throw new WebServiceException(ex);
/*     */       }
/*     */     }
/* 459 */     AddressingVersion av = AddressingVersion.W3C;
/* 460 */     this.endpointReference = new WSEndpointReference(av, eprAddress, getServiceName(), getPortName(), portTypeName, null, wsdlAddress, null, wsdlEPRExtensions, null);
/*     */ 
/* 463 */     return this.endpointReference;
/*     */   }
/*     */ 
/*     */   public final W3CEndpointReference getEndpointReference()
/*     */   {
/* 468 */     if (this.binding.getBindingID().equals("http://www.w3.org/2004/08/wsdl/http"))
/* 469 */       throw new UnsupportedOperationException(ClientMessages.UNSUPPORTED_OPERATION("BindingProvider.getEndpointReference()", "XML/HTTP Binding", "SOAP11 or SOAP12 Binding"));
/* 470 */     return (W3CEndpointReference)getEndpointReference(W3CEndpointReference.class);
/*     */   }
/*     */ 
/*     */   public final <T extends EndpointReference> T getEndpointReference(Class<T> clazz) {
/* 474 */     return getWSEndpointReference().toSpec(clazz);
/*     */   }
/*     */   @NotNull
/*     */   public ManagedObjectManager getManagedObjectManager() {
/* 478 */     return this.managedObjectManager;
/*     */   }
/*     */ 
/*     */   public final void setOutboundHeaders(List<Header> headers)
/*     */   {
/* 487 */     if (headers == null) {
/* 488 */       this.userOutboundHeaders = null;
/*     */     } else {
/* 490 */       for (Header h : headers) {
/* 491 */         if (h == null)
/* 492 */           throw new IllegalArgumentException();
/*     */       }
/* 494 */       this.userOutboundHeaders = ((Header[])headers.toArray(new Header[headers.size()]));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setOutboundHeaders(Header[] headers) {
/* 499 */     if (headers == null) {
/* 500 */       this.userOutboundHeaders = null;
/*     */     } else {
/* 502 */       for (Header h : headers) {
/* 503 */         if (h == null)
/* 504 */           throw new IllegalArgumentException();
/*     */       }
/* 506 */       Header[] hl = new Header[headers.length];
/* 507 */       System.arraycopy(headers, 0, hl, 0, headers.length);
/* 508 */       this.userOutboundHeaders = hl;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final List<Header> getInboundHeaders() {
/* 513 */     return Collections.unmodifiableList((HeaderList)this.responseContext.get("com.sun.xml.internal.ws.api.message.HeaderList"));
/*     */   }
/*     */ 
/*     */   public final void setAddress(String address)
/*     */   {
/* 518 */     this.requestContext.put("javax.xml.ws.service.endpoint.address", address);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.Stub
 * JD-Core Version:    0.6.2
 */