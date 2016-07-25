/*     */ package com.sun.xml.internal.ws.api.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.config.management.EndpointCreationAttributes;
/*     */ import com.sun.xml.internal.ws.api.config.management.ManagedEndpointFactory;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
/*     */ import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.server.EndpointFactory;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.EntityResolver;
/*     */ 
/*     */ public abstract class WSEndpoint<T>
/*     */ {
/*     */   @NotNull
/*     */   public abstract Codec createCodec();
/*     */ 
/*     */   @NotNull
/*     */   public abstract QName getServiceName();
/*     */ 
/*     */   @NotNull
/*     */   public abstract QName getPortName();
/*     */ 
/*     */   @NotNull
/*     */   public abstract Class<T> getImplementationClass();
/*     */ 
/*     */   @NotNull
/*     */   public abstract WSBinding getBinding();
/*     */ 
/*     */   @NotNull
/*     */   public abstract Container getContainer();
/*     */ 
/*     */   @Nullable
/*     */   public abstract WSDLPort getPort();
/*     */ 
/*     */   public abstract void setExecutor(@NotNull Executor paramExecutor);
/*     */ 
/*     */   public final void schedule(@NotNull Packet request, @NotNull CompletionCallback callback)
/*     */   {
/* 224 */     schedule(request, callback, null);
/*     */   }
/*     */ 
/*     */   public abstract void schedule(@NotNull Packet paramPacket, @NotNull CompletionCallback paramCompletionCallback, @Nullable FiberContextSwitchInterceptor paramFiberContextSwitchInterceptor);
/*     */ 
/*     */   public void process(@NotNull Packet request, @NotNull CompletionCallback callback, @Nullable FiberContextSwitchInterceptor interceptor)
/*     */   {
/* 239 */     schedule(request, callback, interceptor);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public abstract PipeHead createPipeHead();
/*     */ 
/*     */   public abstract void dispose();
/*     */ 
/*     */   @Nullable
/*     */   public abstract ServiceDefinition getServiceDefinition();
/*     */ 
/*     */   @NotNull
/*     */   public abstract Set<EndpointComponent> getComponentRegistry();
/*     */ 
/*     */   @Nullable
/*     */   public abstract SEIModel getSEIModel();
/*     */ 
/*     */   /** @deprecated */
/*     */   public abstract PolicyMap getPolicyMap();
/*     */ 
/*     */   @NotNull
/*     */   public abstract ManagedObjectManager getManagedObjectManager();
/*     */ 
/*     */   public abstract void closeManagedObjectManager();
/*     */ 
/*     */   @NotNull
/*     */   public abstract ServerTubeAssemblerContext getAssemblerContext();
/*     */ 
/*     */   public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable EntityResolver resolver, boolean isTransportSynchronous)
/*     */   {
/* 498 */     WSEndpoint endpoint = EndpointFactory.createEndpoint(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, resolver, isTransportSynchronous);
/*     */ 
/* 501 */     endpoint.getManagedObjectManager().resumeJMXRegistration();
/*     */ 
/* 503 */     Iterator managementFactories = ServiceFinder.find(ManagedEndpointFactory.class).iterator();
/* 504 */     if (managementFactories.hasNext()) {
/* 505 */       ManagedEndpointFactory managementFactory = (ManagedEndpointFactory)managementFactories.next();
/* 506 */       EndpointCreationAttributes attributes = new EndpointCreationAttributes(processHandlerAnnotation, invoker, resolver, isTransportSynchronous);
/*     */ 
/* 508 */       return managementFactory.createEndpoint(endpoint, attributes);
/*     */     }
/*     */ 
/* 511 */     return endpoint;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable EntityResolver resolver)
/*     */   {
/* 529 */     return create(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, resolver, false);
/*     */   }
/*     */ 
/*     */   public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable URL catalogUrl)
/*     */   {
/* 553 */     return create(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, XmlUtil.createEntityResolver(catalogUrl), false);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static QName getDefaultServiceName(Class endpointClass)
/*     */   {
/* 562 */     return EndpointFactory.getDefaultServiceName(endpointClass);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static QName getDefaultPortName(@NotNull QName serviceName, Class endpointClass)
/*     */   {
/* 569 */     return EndpointFactory.getDefaultPortName(serviceName, endpointClass);
/*     */   }
/*     */ 
/*     */   public static abstract interface CompletionCallback
/*     */   {
/*     */     public abstract void onCompletion(@NotNull Packet paramPacket);
/*     */   }
/*     */ 
/*     */   public static abstract interface PipeHead
/*     */   {
/*     */     @NotNull
/*     */     public abstract Packet process(@NotNull Packet paramPacket, @Nullable WebServiceContextDelegate paramWebServiceContextDelegate, @Nullable TransportBackChannel paramTransportBackChannel);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.WSEndpoint
 * JD-Core Version:    0.6.2
 */