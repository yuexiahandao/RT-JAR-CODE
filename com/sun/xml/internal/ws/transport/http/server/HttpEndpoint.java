/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import com.sun.xml.internal.ws.server.ServerRtException;
/*     */ import com.sun.xml.internal.ws.server.WSEndpointImpl;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapterList;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public final class HttpEndpoint extends com.sun.xml.internal.ws.api.server.HttpEndpoint
/*     */ {
/*     */   private String address;
/*     */   private com.sun.net.httpserver.HttpContext httpContext;
/*     */   private final HttpAdapter adapter;
/*     */   private final Executor executor;
/*     */ 
/*     */   public HttpEndpoint(Executor executor, HttpAdapter adapter)
/*     */   {
/*  57 */     this.executor = executor;
/*  58 */     this.adapter = adapter;
/*     */   }
/*     */ 
/*     */   public void publish(String address) {
/*  62 */     this.address = address;
/*  63 */     this.httpContext = ServerMgr.getInstance().createContext(address);
/*  64 */     publish(this.httpContext);
/*     */   }
/*     */ 
/*     */   public void publish(Object serverContext) {
/*  68 */     if ((serverContext instanceof javax.xml.ws.spi.http.HttpContext)) {
/*  69 */       setHandler((javax.xml.ws.spi.http.HttpContext)serverContext);
/*  70 */       return;
/*     */     }
/*  72 */     if ((serverContext instanceof com.sun.net.httpserver.HttpContext)) {
/*  73 */       this.httpContext = ((com.sun.net.httpserver.HttpContext)serverContext);
/*  74 */       setHandler(this.httpContext);
/*  75 */       return;
/*     */     }
/*  77 */     throw new ServerRtException(ServerMessages.NOT_KNOW_HTTP_CONTEXT_TYPE(serverContext.getClass(), com.sun.net.httpserver.HttpContext.class, javax.xml.ws.spi.http.HttpContext.class), new Object[0]);
/*     */   }
/*     */ 
/*     */   HttpAdapterList getAdapterOwner()
/*     */   {
/*  83 */     return this.adapter.owner;
/*     */   }
/*     */ 
/*     */   private String getEPRAddress()
/*     */   {
/*  91 */     if (this.address == null)
/*     */     {
/*  93 */       return this.httpContext.getServer().getAddress().toString();
/*     */     }
/*  95 */     return this.address;
/*     */   }
/*     */ 
/*     */   public void stop() {
/*  99 */     if (this.httpContext != null) {
/* 100 */       if (this.address == null)
/*     */       {
/* 103 */         this.httpContext.getServer().removeContext(this.httpContext);
/*     */       }
/*     */       else {
/* 106 */         ServerMgr.getInstance().removeContext(this.httpContext);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     this.adapter.getEndpoint().dispose();
/*     */   }
/*     */ 
/*     */   private void setHandler(com.sun.net.httpserver.HttpContext context) {
/* 115 */     context.setHandler(new WSHttpHandler(this.adapter, this.executor));
/*     */   }
/*     */ 
/*     */   private void setHandler(javax.xml.ws.spi.http.HttpContext context) {
/* 119 */     context.setHandler(new PortableHttpHandler(this.adapter, this.executor));
/*     */   }
/*     */ 
/*     */   public <T extends EndpointReference> T getEndpointReference(Class<T> clazz, Element[] referenceParameters) {
/* 123 */     WSEndpointImpl endpointImpl = (WSEndpointImpl)this.adapter.getEndpoint();
/* 124 */     String eprAddress = getEPRAddress();
/* 125 */     return (EndpointReference)clazz.cast(endpointImpl.getEndpointReference(clazz, eprAddress, eprAddress + "?wsdl", referenceParameters));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.HttpEndpoint
 * JD-Core Version:    0.6.2
 */