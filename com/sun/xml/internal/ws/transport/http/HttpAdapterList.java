/*     */ package com.sun.xml.internal.ws.transport.http;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.server.PortAddressResolver;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class HttpAdapterList<T extends HttpAdapter> extends AbstractList<T>
/*     */   implements DeploymentDescriptorParser.AdapterFactory<T>
/*     */ {
/*     */   private final List<T> adapters;
/*     */   private final Map<PortInfo, String> addressMap;
/*     */ 
/*     */   public HttpAdapterList()
/*     */   {
/*  57 */     this.adapters = new ArrayList();
/*  58 */     this.addressMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public T createAdapter(String name, String urlPattern, WSEndpoint<?> endpoint) {
/*  62 */     HttpAdapter t = createHttpAdapter(name, urlPattern, endpoint);
/*  63 */     this.adapters.add(t);
/*  64 */     WSDLPort port = endpoint.getPort();
/*  65 */     if (port != null) {
/*  66 */       PortInfo portInfo = new PortInfo(port.getOwner().getName(), port.getName().getLocalPart());
/*  67 */       this.addressMap.put(portInfo, getValidPath(urlPattern));
/*     */     }
/*  69 */     return t;
/*     */   }
/*     */ 
/*     */   protected abstract T createHttpAdapter(String paramString1, String paramString2, WSEndpoint<?> paramWSEndpoint);
/*     */ 
/*     */   private String getValidPath(@NotNull String urlPattern)
/*     */   {
/*  82 */     if (urlPattern.endsWith("/*")) {
/*  83 */       return urlPattern.substring(0, urlPattern.length() - 2);
/*     */     }
/*  85 */     return urlPattern;
/*     */   }
/*     */ 
/*     */   public PortAddressResolver createPortAddressResolver(final String baseAddress)
/*     */   {
/*  93 */     return new PortAddressResolver() {
/*     */       public String getAddressFor(@NotNull QName serviceName, @NotNull String portName) {
/*  95 */         String urlPattern = (String)HttpAdapterList.this.addressMap.get(new HttpAdapterList.PortInfo(serviceName, portName));
/*  96 */         return baseAddress + urlPattern;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public T get(int index)
/*     */   {
/* 103 */     return (HttpAdapter)this.adapters.get(index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 107 */     return this.adapters.size();
/*     */   }
/*     */   private static class PortInfo {
/*     */     private final QName serviceName;
/*     */     private final String portName;
/*     */ 
/*     */     PortInfo(@NotNull QName serviceName, @NotNull String portName) {
/* 115 */       this.serviceName = serviceName;
/* 116 */       this.portName = portName;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object portInfo)
/*     */     {
/* 121 */       if ((portInfo instanceof PortInfo)) {
/* 122 */         PortInfo that = (PortInfo)portInfo;
/* 123 */         return (this.serviceName.equals(that.serviceName)) && (this.portName.equals(that.portName));
/*     */       }
/* 125 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 130 */       return this.serviceName.hashCode() + this.portName.hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.HttpAdapterList
 * JD-Core Version:    0.6.2
 */