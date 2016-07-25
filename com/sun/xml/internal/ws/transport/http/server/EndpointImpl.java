/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.InstanceResolver;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentSource;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.server.EndpointFactory;
/*     */ import com.sun.xml.internal.ws.server.ServerRtException;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapterList;
/*     */ import com.sun.xml.internal.ws.util.xml.XmlUtil;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.ws.Binding;
/*     */ import javax.xml.ws.Endpoint;
/*     */ import javax.xml.ws.EndpointContext;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.WebServicePermission;
/*     */ import javax.xml.ws.wsaddressing.W3CEndpointReference;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class EndpointImpl extends Endpoint
/*     */ {
/*  83 */   private static final WebServicePermission ENDPOINT_PUBLISH_PERMISSION = new WebServicePermission("publishEndpoint");
/*     */   private Object actualEndpoint;
/*     */   private final WSBinding binding;
/*     */ 
/*     */   @Nullable
/*     */   private final Object implementor;
/*     */   private List<Source> metadata;
/*     */   private Executor executor;
/* 101 */   private Map<String, Object> properties = Collections.emptyMap();
/*     */   private boolean stopped;
/*     */ 
/*     */   @Nullable
/*     */   private EndpointContext endpointContext;
/*     */ 
/*     */   @NotNull
/*     */   private final Class<?> implClass;
/*     */   private final com.sun.xml.internal.ws.api.server.Invoker invoker;
/*     */   private Container container;
/*     */ 
/*     */   public EndpointImpl(@NotNull BindingID bindingId, @NotNull Object impl, WebServiceFeature[] features)
/*     */   {
/* 111 */     this(bindingId, impl, impl.getClass(), InstanceResolver.createSingleton(impl).createInvoker(), features);
/*     */   }
/*     */ 
/*     */   public EndpointImpl(@NotNull BindingID bindingId, @NotNull Class implClass, javax.xml.ws.spi.Invoker invoker, WebServiceFeature[] features)
/*     */   {
/* 118 */     this(bindingId, null, implClass, new InvokerImpl(invoker), features);
/*     */   }
/*     */ 
/*     */   private EndpointImpl(@NotNull BindingID bindingId, Object impl, @NotNull Class implClass, com.sun.xml.internal.ws.api.server.Invoker invoker, WebServiceFeature[] features)
/*     */   {
/* 123 */     this.binding = BindingImpl.create(bindingId, features);
/* 124 */     this.implClass = implClass;
/* 125 */     this.invoker = invoker;
/* 126 */     this.implementor = impl;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public EndpointImpl(WSEndpoint wse, Object serverContext)
/*     */   {
/* 139 */     this.actualEndpoint = new HttpEndpoint(null, getAdapter(wse, ""));
/* 140 */     ((HttpEndpoint)this.actualEndpoint).publish(serverContext);
/* 141 */     this.binding = wse.getBinding();
/* 142 */     this.implementor = null;
/* 143 */     this.implClass = null;
/* 144 */     this.invoker = null;
/*     */   }
/*     */ 
/*     */   public Binding getBinding() {
/* 148 */     return this.binding;
/*     */   }
/*     */ 
/*     */   public Object getImplementor() {
/* 152 */     return this.implementor;
/*     */   }
/*     */ 
/* 156 */   public void publish(String address) { canPublish();
/*     */     URL url;
/*     */     try {
/* 159 */       url = new URL(address);
/*     */     } catch (MalformedURLException ex) {
/* 161 */       throw new IllegalArgumentException("Cannot create URL for this address " + address);
/*     */     }
/* 163 */     if (!url.getProtocol().equals("http")) {
/* 164 */       throw new IllegalArgumentException(url.getProtocol() + " protocol based address is not supported");
/*     */     }
/* 166 */     if (!url.getPath().startsWith("/")) {
/* 167 */       throw new IllegalArgumentException("Incorrect WebService address=" + address + ". The address's path should start with /");
/*     */     }
/*     */ 
/* 170 */     createEndpoint(url.getPath());
/* 171 */     ((HttpEndpoint)this.actualEndpoint).publish(address); }
/*     */ 
/*     */   public void publish(Object serverContext)
/*     */   {
/* 175 */     canPublish();
/* 176 */     if (!com.sun.net.httpserver.HttpContext.class.isAssignableFrom(serverContext.getClass())) {
/* 177 */       throw new IllegalArgumentException(serverContext.getClass() + " is not a supported context.");
/*     */     }
/* 179 */     createEndpoint(((com.sun.net.httpserver.HttpContext)serverContext).getPath());
/* 180 */     ((HttpEndpoint)this.actualEndpoint).publish(serverContext);
/*     */   }
/*     */ 
/*     */   public void publish(javax.xml.ws.spi.http.HttpContext serverContext) {
/* 184 */     canPublish();
/* 185 */     createEndpoint(serverContext.getPath());
/* 186 */     ((HttpEndpoint)this.actualEndpoint).publish(serverContext);
/*     */   }
/*     */ 
/*     */   public void stop() {
/* 190 */     if (isPublished()) {
/* 191 */       ((HttpEndpoint)this.actualEndpoint).stop();
/* 192 */       this.actualEndpoint = null;
/* 193 */       this.stopped = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isPublished() {
/* 198 */     return this.actualEndpoint != null;
/*     */   }
/*     */ 
/*     */   public List<Source> getMetadata() {
/* 202 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   public void setMetadata(List<Source> metadata) {
/* 206 */     if (isPublished()) {
/* 207 */       throw new IllegalStateException("Cannot set Metadata. Endpoint is already published");
/*     */     }
/* 209 */     this.metadata = metadata;
/*     */   }
/*     */ 
/*     */   public Executor getExecutor() {
/* 213 */     return this.executor;
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor executor) {
/* 217 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getProperties() {
/* 221 */     return new HashMap(this.properties);
/*     */   }
/*     */ 
/*     */   public void setProperties(Map<String, Object> map) {
/* 225 */     this.properties = new HashMap(map);
/*     */   }
/*     */ 
/*     */   private void createEndpoint(String urlPattern)
/*     */   {
/* 234 */     SecurityManager sm = System.getSecurityManager();
/* 235 */     if (sm != null) {
/* 236 */       sm.checkPermission(ENDPOINT_PUBLISH_PERMISSION);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 241 */       Class.forName("com.sun.net.httpserver.HttpServer");
/*     */     } catch (Exception e) {
/* 243 */       throw new UnsupportedOperationException("Couldn't load light weight http server", e);
/*     */     }
/* 245 */     this.container = getContainer();
/* 246 */     WSEndpoint wse = WSEndpoint.create(this.implClass, true, this.invoker, (QName)getProperty(QName.class, "javax.xml.ws.wsdl.service"), (QName)getProperty(QName.class, "javax.xml.ws.wsdl.port"), this.container, this.binding, getPrimaryWsdl(), buildDocList(), (EntityResolver)null, false);
/*     */ 
/* 259 */     this.actualEndpoint = new HttpEndpoint(this.executor, getAdapter(wse, urlPattern));
/*     */   }
/*     */ 
/*     */   private <T> T getProperty(Class<T> type, String key) {
/* 263 */     Object o = this.properties.get(key);
/* 264 */     if (o == null) return null;
/* 265 */     if (type.isInstance(o)) {
/* 266 */       return type.cast(o);
/*     */     }
/* 268 */     throw new IllegalArgumentException("Property " + key + " has to be of type " + type);
/*     */   }
/*     */ 
/*     */   private List<SDDocumentSource> buildDocList()
/*     */   {
/* 276 */     List r = new ArrayList();
/*     */ 
/* 278 */     if (this.metadata != null) {
/* 279 */       for (Source source : this.metadata) {
/*     */         try {
/* 281 */           XMLStreamBufferResult xsbr = (XMLStreamBufferResult)XmlUtil.identityTransform(source, new XMLStreamBufferResult());
/* 282 */           String systemId = source.getSystemId();
/*     */ 
/* 284 */           r.add(SDDocumentSource.create(new URL(systemId), xsbr.getXMLStreamBuffer()));
/*     */         } catch (TransformerException te) {
/* 286 */           throw new ServerRtException("server.rt.err", new Object[] { te });
/*     */         } catch (IOException te) {
/* 288 */           throw new ServerRtException("server.rt.err", new Object[] { te });
/*     */         } catch (SAXException e) {
/* 290 */           throw new ServerRtException("server.rt.err", new Object[] { e });
/*     */         } catch (ParserConfigurationException e) {
/* 292 */           throw new ServerRtException("server.rt.err", new Object[] { e });
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 297 */     return r;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   private SDDocumentSource getPrimaryWsdl()
/*     */   {
/* 305 */     EndpointFactory.verifyImplementorClass(this.implClass);
/* 306 */     String wsdlLocation = EndpointFactory.getWsdlLocation(this.implClass);
/* 307 */     if (wsdlLocation != null) {
/* 308 */       ClassLoader cl = this.implClass.getClassLoader();
/* 309 */       URL url = cl.getResource(wsdlLocation);
/* 310 */       if (url != null) {
/* 311 */         return SDDocumentSource.create(url);
/*     */       }
/* 313 */       throw new ServerRtException("cannot.load.wsdl", new Object[] { wsdlLocation });
/*     */     }
/* 315 */     return null;
/*     */   }
/*     */ 
/*     */   private void canPublish() {
/* 319 */     if (isPublished()) {
/* 320 */       throw new IllegalStateException("Cannot publish this endpoint. Endpoint has been already published.");
/*     */     }
/*     */ 
/* 323 */     if (this.stopped)
/* 324 */       throw new IllegalStateException("Cannot publish this endpoint. Endpoint has been already stopped.");
/*     */   }
/*     */ 
/*     */   public EndpointReference getEndpointReference(Element[] referenceParameters)
/*     */   {
/* 330 */     return getEndpointReference(W3CEndpointReference.class, referenceParameters);
/*     */   }
/*     */ 
/*     */   public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, Element[] referenceParameters) {
/* 334 */     if (!isPublished()) {
/* 335 */       throw new WebServiceException("Endpoint is not published yet");
/*     */     }
/* 337 */     return ((HttpEndpoint)this.actualEndpoint).getEndpointReference(clazz, referenceParameters);
/*     */   }
/*     */ 
/*     */   public void setEndpointContext(EndpointContext ctxt)
/*     */   {
/* 342 */     this.endpointContext = ctxt;
/*     */   }
/*     */ 
/*     */   private HttpAdapter getAdapter(WSEndpoint endpoint, String urlPattern) {
/* 346 */     HttpAdapterList adapterList = null;
/* 347 */     if (this.endpointContext != null) {
/* 348 */       for (Iterator i$ = this.endpointContext.getEndpoints().iterator(); i$.hasNext(); 
/* 351 */         throw new AssertionError())
/*     */       {
/* 348 */         Endpoint e = (Endpoint)i$.next();
/* 349 */         if ((e.isPublished()) && (e != this)) {
/* 350 */           adapterList = ((HttpEndpoint)((EndpointImpl)e).actualEndpoint).getAdapterOwner();
/* 351 */           if (($assertionsDisabled) || (adapterList != null))
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/* 356 */     if (adapterList == null) {
/* 357 */       adapterList = new ServerAdapterList();
/*     */     }
/* 359 */     return adapterList.createAdapter("", urlPattern, endpoint);
/*     */   }
/*     */ 
/*     */   private Container getContainer()
/*     */   {
/* 366 */     if (this.endpointContext != null) {
/* 367 */       for (Endpoint e : this.endpointContext.getEndpoints()) {
/* 368 */         if ((e.isPublished()) && (e != this)) {
/* 369 */           return ((EndpointImpl)e).container;
/*     */         }
/*     */       }
/*     */     }
/* 373 */     return new ServerContainer();
/*     */   }
/*     */ 
/*     */   private static class InvokerImpl extends com.sun.xml.internal.ws.api.server.Invoker {
/*     */     private javax.xml.ws.spi.Invoker spiInvoker;
/*     */ 
/*     */     InvokerImpl(javax.xml.ws.spi.Invoker spiInvoker) {
/* 380 */       this.spiInvoker = spiInvoker;
/*     */     }
/*     */ 
/*     */     public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint)
/*     */     {
/*     */       try {
/* 386 */         this.spiInvoker.inject(wsc);
/*     */       } catch (IllegalAccessException e) {
/* 388 */         throw new WebServiceException(e);
/*     */       } catch (InvocationTargetException e) {
/* 390 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object invoke(@NotNull Packet p, @NotNull Method m, @NotNull Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 395 */       return this.spiInvoker.invoke(m, args);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.EndpointImpl
 * JD-Core Version:    0.6.2
 */