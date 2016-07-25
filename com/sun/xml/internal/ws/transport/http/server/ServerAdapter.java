/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.server.BoundEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.Module;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WebModule;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class ServerAdapter extends HttpAdapter
/*     */   implements BoundEndpoint
/*     */ {
/*     */   final String name;
/* 110 */   private static final Logger LOGGER = Logger.getLogger(ServerAdapter.class.getName());
/*     */ 
/*     */   protected ServerAdapter(String name, String urlPattern, WSEndpoint endpoint, ServerAdapterList owner)
/*     */   {
/*  58 */     super(endpoint, owner, urlPattern);
/*  59 */     this.name = name;
/*     */ 
/*  61 */     Module module = (Module)endpoint.getContainer().getSPI(Module.class);
/*  62 */     if (module == null)
/*  63 */       LOGGER.warning("Container " + endpoint.getContainer() + " doesn't support " + Module.class);
/*     */     else
/*  65 */       module.getBoundEndpoints().add(this);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  74 */     return this.name;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public URI getAddress()
/*     */   {
/*  80 */     WebModule webModule = (WebModule)this.endpoint.getContainer().getSPI(WebModule.class);
/*  81 */     if (webModule == null)
/*     */     {
/*  83 */       throw new WebServiceException("Container " + this.endpoint.getContainer() + " doesn't support " + WebModule.class);
/*     */     }
/*  85 */     return getAddress(webModule.getContextPath());
/*     */   }
/*     */   @NotNull
/*     */   public URI getAddress(String baseAddress) {
/*  89 */     String adrs = baseAddress + getValidPath();
/*     */     try {
/*  91 */       return new URI(adrs);
/*     */     }
/*     */     catch (URISyntaxException e) {
/*  94 */       throw new WebServiceException("Unable to compute address for " + this.endpoint, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispose() {
/*  99 */     this.endpoint.dispose();
/*     */   }
/*     */ 
/*     */   public String getUrlPattern() {
/* 103 */     return this.urlPattern;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 107 */     return super.toString() + "[name=" + this.name + ']';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.ServerAdapter
 * JD-Core Version:    0.6.2
 */