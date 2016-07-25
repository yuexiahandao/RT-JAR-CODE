/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.server.PortAddressResolver;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*     */ import com.sun.xml.internal.ws.resources.WsservletMessages;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapterList;
/*     */ import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.spi.http.HttpExchange;
/*     */ 
/*     */ final class PortableConnectionImpl extends WSHTTPConnection
/*     */   implements WebServiceContextDelegate
/*     */ {
/*     */   private final HttpExchange httpExchange;
/*     */   private int status;
/*     */   private final HttpAdapter adapter;
/*     */   private boolean outputWritten;
/* 227 */   private static final PropertySet.PropertyMap model = parse(PortableConnectionImpl.class);
/*     */ 
/*     */   public PortableConnectionImpl(@NotNull HttpAdapter adapter, @NotNull HttpExchange httpExchange)
/*     */   {
/*  64 */     this.adapter = adapter;
/*  65 */     this.httpExchange = httpExchange;
/*     */   }
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.headers", "com.sun.xml.internal.ws.api.message.packet.inbound.transport.headers"})
/*     */   @NotNull
/*     */   public Map<String, List<String>> getRequestHeaders() {
/*  71 */     return this.httpExchange.getRequestHeaders();
/*     */   }
/*     */ 
/*     */   public String getRequestHeader(String headerName)
/*     */   {
/*  76 */     return this.httpExchange.getRequestHeader(headerName);
/*     */   }
/*     */ 
/*     */   public void setResponseHeaders(Map<String, List<String>> headers)
/*     */   {
/*  81 */     Map r = this.httpExchange.getResponseHeaders();
/*  82 */     r.clear();
/*  83 */     for (Map.Entry entry : headers.entrySet()) {
/*  84 */       String name = (String)entry.getKey();
/*  85 */       List values = (List)entry.getValue();
/*     */ 
/*  87 */       if ((!name.equalsIgnoreCase("Content-Length")) && (!name.equalsIgnoreCase("Content-Type")))
/*  88 */         r.put(name, new ArrayList(values));
/*     */     }
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.response.headers", "com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers"})
/*     */   public Map<String, List<String>> getResponseHeaders()
/*     */   {
/*  96 */     return this.httpExchange.getResponseHeaders();
/*     */   }
/*     */ 
/*     */   public void setContentTypeResponseHeader(@NotNull String value)
/*     */   {
/* 101 */     this.httpExchange.addResponseHeader("Content-Type", value);
/*     */   }
/*     */ 
/*     */   public void setStatus(int status)
/*     */   {
/* 106 */     this.status = status;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.response.code"})
/*     */   public int getStatus()
/*     */   {
/* 112 */     return this.status;
/*     */   }
/*     */   @NotNull
/*     */   public InputStream getInput() throws IOException {
/* 116 */     return this.httpExchange.getRequestBody();
/*     */   }
/*     */   @NotNull
/*     */   public OutputStream getOutput() throws IOException {
/* 120 */     assert (!this.outputWritten);
/* 121 */     this.outputWritten = true;
/*     */ 
/* 123 */     this.httpExchange.setStatus(getStatus());
/* 124 */     return this.httpExchange.getResponseBody();
/*     */   }
/*     */   @NotNull
/*     */   public WebServiceContextDelegate getWebServiceContextDelegate() {
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   public Principal getUserPrincipal(Packet request) {
/* 132 */     return this.httpExchange.getUserPrincipal();
/*     */   }
/*     */ 
/*     */   public boolean isUserInRole(Packet request, String role) {
/* 136 */     return this.httpExchange.isUserInRole(role);
/*     */   }
/*     */   @NotNull
/*     */   public String getEPRAddress(Packet request, WSEndpoint endpoint) {
/* 140 */     PortAddressResolver resolver = this.adapter.owner.createPortAddressResolver(getBaseAddress());
/* 141 */     String address = resolver.getAddressFor(endpoint.getServiceName(), endpoint.getPortName().getLocalPart());
/* 142 */     if (address == null)
/* 143 */       throw new WebServiceException(WsservletMessages.SERVLET_NO_ADDRESS_AVAILABLE(endpoint.getPortName()));
/* 144 */     return address;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.servlet.context"})
/*     */   public Object getServletContext() {
/* 149 */     return this.httpExchange.getAttribute("javax.xml.ws.servlet.context");
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.servlet.response"})
/*     */   public Object getServletResponse() {
/* 154 */     return this.httpExchange.getAttribute("javax.xml.ws.servlet.response");
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.servlet.request"})
/*     */   public Object getServletRequest() {
/* 159 */     return this.httpExchange.getAttribute("javax.xml.ws.servlet.request");
/*     */   }
/*     */ 
/*     */   public String getWSDLAddress(@NotNull Packet request, @NotNull WSEndpoint endpoint) {
/* 163 */     String eprAddress = getEPRAddress(request, endpoint);
/* 164 */     if (this.adapter.getEndpoint().getPort() != null) {
/* 165 */       return eprAddress + "?wsdl";
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 172 */     return this.httpExchange.getScheme().equals("https");
/*     */   }
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.method"})
/*     */   @NotNull
/*     */   public String getRequestMethod() {
/* 178 */     return this.httpExchange.getRequestMethod();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.querystring"})
/*     */   public String getQueryString()
/*     */   {
/* 184 */     return this.httpExchange.getQueryString();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.pathinfo"})
/*     */   public String getPathInfo()
/*     */   {
/* 190 */     return this.httpExchange.getPathInfo();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.http.exchange"})
/*     */   public HttpExchange getExchange() {
/* 195 */     return this.httpExchange;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getBaseAddress() {
/* 200 */     StringBuilder sb = new StringBuilder();
/* 201 */     sb.append(this.httpExchange.getScheme());
/* 202 */     sb.append("://");
/* 203 */     sb.append(this.httpExchange.getLocalAddress().getHostName());
/* 204 */     sb.append(":");
/* 205 */     sb.append(this.httpExchange.getLocalAddress().getPort());
/* 206 */     sb.append(this.httpExchange.getContextPath());
/* 207 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 212 */     return this.httpExchange.getProtocol();
/*     */   }
/*     */ 
/*     */   public void setContentLengthResponseHeader(int value)
/*     */   {
/* 217 */     this.httpExchange.addResponseHeader("Content-Length", "" + value);
/*     */   }
/*     */ 
/*     */   protected PropertySet.PropertyMap getPropertyMap() {
/* 221 */     return model;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.PortableConnectionImpl
 * JD-Core Version:    0.6.2
 */