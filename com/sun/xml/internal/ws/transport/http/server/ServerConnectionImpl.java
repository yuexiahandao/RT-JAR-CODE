/*     */ package com.sun.xml.internal.ws.transport.http.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpsExchange;
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
/*     */ import com.sun.xml.internal.ws.util.ReadAllStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ final class ServerConnectionImpl extends WSHTTPConnection
/*     */   implements WebServiceContextDelegate
/*     */ {
/*     */   private final HttpExchange httpExchange;
/*     */   private int status;
/*     */   private final HttpAdapter adapter;
/*     */   private LWHSInputStream in;
/*     */   private OutputStream out;
/* 310 */   private static final PropertySet.PropertyMap model = parse(ServerConnectionImpl.class);
/*     */ 
/*     */   public ServerConnectionImpl(@NotNull HttpAdapter adapter, @NotNull HttpExchange httpExchange)
/*     */   {
/*  72 */     this.adapter = adapter;
/*  73 */     this.httpExchange = httpExchange;
/*     */   }
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.headers", "com.sun.xml.internal.ws.api.message.packet.inbound.transport.headers"})
/*     */   @NotNull
/*     */   public Map<String, List<String>> getRequestHeaders() {
/*  79 */     return this.httpExchange.getRequestHeaders();
/*     */   }
/*     */ 
/*     */   public String getRequestHeader(String headerName)
/*     */   {
/*  84 */     return this.httpExchange.getRequestHeaders().getFirst(headerName);
/*     */   }
/*     */ 
/*     */   public void setResponseHeaders(Map<String, List<String>> headers)
/*     */   {
/*  89 */     Headers r = this.httpExchange.getResponseHeaders();
/*  90 */     r.clear();
/*  91 */     for (Map.Entry entry : headers.entrySet()) {
/*  92 */       String name = (String)entry.getKey();
/*  93 */       List values = (List)entry.getValue();
/*     */ 
/*  95 */       if ((!name.equalsIgnoreCase("Content-Length")) && (!name.equalsIgnoreCase("Content-Type")))
/*  96 */         r.put(name, new ArrayList(values));
/*     */     }
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.response.headers", "com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers"})
/*     */   public Map<String, List<String>> getResponseHeaders()
/*     */   {
/* 103 */     return this.httpExchange.getResponseHeaders();
/*     */   }
/*     */ 
/*     */   public void setContentTypeResponseHeader(@NotNull String value)
/*     */   {
/* 108 */     this.httpExchange.getResponseHeaders().set("Content-Type", value);
/*     */   }
/*     */ 
/*     */   public void setStatus(int status)
/*     */   {
/* 113 */     this.status = status;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.response.code"})
/*     */   public int getStatus()
/*     */   {
/* 119 */     return this.status;
/*     */   }
/*     */   @NotNull
/*     */   public InputStream getInput() {
/* 123 */     if (this.in == null) {
/* 124 */       this.in = new LWHSInputStream(this.httpExchange.getRequestBody());
/*     */     }
/* 126 */     return this.in;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public OutputStream getOutput()
/*     */     throws IOException
/*     */   {
/* 163 */     if (this.out == null) {
/* 164 */       String lenHeader = this.httpExchange.getResponseHeaders().getFirst("Content-Length");
/* 165 */       int length = lenHeader != null ? Integer.parseInt(lenHeader) : 0;
/* 166 */       this.httpExchange.sendResponseHeaders(getStatus(), length);
/*     */ 
/* 171 */       this.out = new FilterOutputStream(this.httpExchange.getResponseBody()) {
/*     */         boolean closed;
/*     */ 
/*     */         public void close() throws IOException {
/* 175 */           if (!this.closed) {
/* 176 */             this.closed = true;
/*     */ 
/* 179 */             ServerConnectionImpl.this.in.readAll();
/*     */             try {
/* 181 */               super.close();
/*     */             }
/*     */             catch (IOException ioe)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */         public void write(byte[] buf, int start, int len) throws IOException
/*     */         {
/* 191 */           this.out.write(buf, start, len);
/*     */         }
/*     */       };
/*     */     }
/* 195 */     return this.out;
/*     */   }
/*     */   @NotNull
/*     */   public WebServiceContextDelegate getWebServiceContextDelegate() {
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   public Principal getUserPrincipal(Packet request) {
/* 203 */     return this.httpExchange.getPrincipal();
/*     */   }
/*     */ 
/*     */   public boolean isUserInRole(Packet request, String role) {
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getEPRAddress(Packet request, WSEndpoint endpoint)
/*     */   {
/* 213 */     PortAddressResolver resolver = this.adapter.owner.createPortAddressResolver(getBaseAddress());
/* 214 */     String address = resolver.getAddressFor(endpoint.getServiceName(), endpoint.getPortName().getLocalPart());
/* 215 */     if (address == null)
/* 216 */       throw new WebServiceException(WsservletMessages.SERVLET_NO_ADDRESS_AVAILABLE(endpoint.getPortName()));
/* 217 */     return address;
/*     */   }
/*     */ 
/*     */   public String getWSDLAddress(@NotNull Packet request, @NotNull WSEndpoint endpoint)
/*     */   {
/* 222 */     String eprAddress = getEPRAddress(request, endpoint);
/* 223 */     if (this.adapter.getEndpoint().getPort() != null) {
/* 224 */       return eprAddress + "?wsdl";
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 231 */     return this.httpExchange instanceof HttpsExchange;
/*     */   }
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.method"})
/*     */   @NotNull
/*     */   public String getRequestMethod() {
/* 237 */     return this.httpExchange.getRequestMethod();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.querystring"})
/*     */   public String getQueryString()
/*     */   {
/* 243 */     URI requestUri = this.httpExchange.getRequestURI();
/* 244 */     String query = requestUri.getQuery();
/* 245 */     if (query != null)
/* 246 */       return query;
/* 247 */     return null;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.http.request.pathinfo"})
/*     */   public String getPathInfo()
/*     */   {
/* 253 */     URI requestUri = this.httpExchange.getRequestURI();
/* 254 */     String reqPath = requestUri.getPath();
/* 255 */     String ctxtPath = this.httpExchange.getHttpContext().getPath();
/* 256 */     if (reqPath.length() > ctxtPath.length()) {
/* 257 */       return reqPath.substring(ctxtPath.length());
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.http.exchange"})
/*     */   public HttpExchange getExchange() {
/* 264 */     return this.httpExchange;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public String getBaseAddress()
/*     */   {
/* 275 */     StringBuilder strBuf = new StringBuilder();
/* 276 */     strBuf.append((this.httpExchange instanceof HttpsExchange) ? "https" : "http");
/* 277 */     strBuf.append("://");
/*     */ 
/* 279 */     String hostHeader = this.httpExchange.getRequestHeaders().getFirst("Host");
/* 280 */     if (hostHeader != null) {
/* 281 */       strBuf.append(hostHeader);
/*     */     } else {
/* 283 */       strBuf.append(this.httpExchange.getLocalAddress().getHostName());
/* 284 */       strBuf.append(":");
/* 285 */       strBuf.append(this.httpExchange.getLocalAddress().getPort());
/*     */     }
/*     */ 
/* 290 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 295 */     return this.httpExchange.getProtocol();
/*     */   }
/*     */ 
/*     */   public void setContentLengthResponseHeader(int value)
/*     */   {
/* 300 */     this.httpExchange.getResponseHeaders().set("Content-Length", "" + value);
/*     */   }
/*     */ 
/*     */   protected PropertySet.PropertyMap getPropertyMap() {
/* 304 */     return model;
/*     */   }
/*     */ 
/*     */   private static class LWHSInputStream extends FilterInputStream
/*     */   {
/*     */     boolean closed;
/*     */     boolean readAll;
/*     */ 
/*     */     LWHSInputStream(InputStream in)
/*     */     {
/* 137 */       super();
/*     */     }
/*     */ 
/*     */     void readAll() throws IOException {
/* 141 */       if ((!this.closed) && (!this.readAll)) {
/* 142 */         ReadAllStream all = new ReadAllStream();
/* 143 */         all.readAll(this.in, 4000000L);
/* 144 */         this.in.close();
/* 145 */         this.in = all;
/* 146 */         this.readAll = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 152 */       if (!this.closed) {
/* 153 */         readAll();
/* 154 */         super.close();
/* 155 */         this.closed = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.server.ServerConnectionImpl
 * JD-Core Version:    0.6.2
 */