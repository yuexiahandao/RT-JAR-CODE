/*     */ package com.sun.xml.internal.ws.transport.http.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSFeatureList;
/*     */ import com.sun.xml.internal.ws.api.ha.StickyFeature;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
/*     */ import com.sun.xml.internal.ws.client.ClientTransportException;
/*     */ import com.sun.xml.internal.ws.developer.HttpConfigFeature;
/*     */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*     */ import com.sun.xml.internal.ws.transport.Headers;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import com.sun.xml.internal.ws.util.RuntimeVersion;
/*     */ import com.sun.xml.internal.ws.util.StreamUtils;
/*     */ import com.sun.xml.internal.ws.util.Version;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.CookieHandler;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ public class HttpTransportPipe extends AbstractTubeImpl
/*     */ {
/*     */   private final Codec codec;
/*     */   private final WSBinding binding;
/*  73 */   private static final List<String> USER_AGENT = Collections.singletonList(RuntimeVersion.VERSION.toString());
/*     */   private final CookieHandler cookieJar;
/*     */   private final boolean sticky;
/* 394 */   public static boolean dump = b;
/*     */ 
/*     */   public HttpTransportPipe(Codec codec, WSBinding binding)
/*     */   {
/*  87 */     this.codec = codec;
/*  88 */     this.binding = binding;
/*  89 */     this.sticky = isSticky(binding);
/*  90 */     HttpConfigFeature configFeature = (HttpConfigFeature)binding.getFeature(HttpConfigFeature.class);
/*  91 */     if (configFeature == null) {
/*  92 */       configFeature = new HttpConfigFeature();
/*     */     }
/*  94 */     this.cookieJar = configFeature.getCookieHandler();
/*     */   }
/*     */ 
/*     */   private static boolean isSticky(WSBinding binding) {
/*  98 */     boolean tSticky = false;
/*  99 */     WebServiceFeature[] features = binding.getFeatures().toArray();
/* 100 */     for (WebServiceFeature f : features) {
/* 101 */       if ((f instanceof StickyFeature)) {
/* 102 */         tSticky = true;
/* 103 */         break;
/*     */       }
/*     */     }
/* 106 */     return tSticky;
/*     */   }
/*     */ 
/*     */   private HttpTransportPipe(HttpTransportPipe that, TubeCloner cloner)
/*     */   {
/* 113 */     this(that.codec.copy(), that.binding);
/* 114 */     cloner.add(that, this);
/*     */   }
/*     */ 
/*     */   public NextAction processException(@NotNull Throwable t) {
/* 118 */     throw new IllegalStateException("HttpTransportPipe's processException shouldn't be called.");
/*     */   }
/*     */ 
/*     */   public NextAction processRequest(@NotNull Packet request) {
/* 122 */     return doReturnWith(process(request));
/*     */   }
/*     */ 
/*     */   public NextAction processResponse(@NotNull Packet response) {
/* 126 */     throw new IllegalStateException("HttpTransportPipe's processResponse shouldn't be called.");
/*     */   }
/*     */ 
/*     */   public Packet process(Packet request)
/*     */   {
/*     */     try
/*     */     {
/* 134 */       Map reqHeaders = new Headers();
/*     */ 
/* 136 */       Map userHeaders = (Map)request.invocationProperties.get("javax.xml.ws.http.request.headers");
/* 137 */       boolean addUserAgent = true;
/* 138 */       if (userHeaders != null)
/*     */       {
/* 140 */         reqHeaders.putAll(userHeaders);
/*     */ 
/* 142 */         if (userHeaders.get("User-Agent") != null) {
/* 143 */           addUserAgent = false;
/*     */         }
/*     */       }
/* 146 */       if (addUserAgent) {
/* 147 */         reqHeaders.put("User-Agent", USER_AGENT);
/*     */       }
/*     */ 
/* 150 */       addBasicAuth(request, reqHeaders);
/* 151 */       addCookies(request, reqHeaders);
/*     */ 
/* 153 */       HttpClientTransport con = new HttpClientTransport(request, reqHeaders);
/* 154 */       request.addSatellite(new HttpResponseProperties(con));
/*     */ 
/* 156 */       ContentType ct = this.codec.getStaticContentType(request);
/* 157 */       if (ct == null) {
/* 158 */         ByteArrayBuffer buf = new ByteArrayBuffer();
/*     */ 
/* 160 */         ct = this.codec.encode(request, buf);
/*     */ 
/* 162 */         reqHeaders.put("Content-Length", Collections.singletonList(Integer.toString(buf.size())));
/* 163 */         reqHeaders.put("Content-Type", Collections.singletonList(ct.getContentType()));
/* 164 */         if (ct.getAcceptHeader() != null) {
/* 165 */           reqHeaders.put("Accept", Collections.singletonList(ct.getAcceptHeader()));
/*     */         }
/* 167 */         if ((this.binding instanceof SOAPBinding)) {
/* 168 */           writeSOAPAction(reqHeaders, ct.getSOAPActionHeader());
/*     */         }
/*     */ 
/* 171 */         if (dump) {
/* 172 */           dump(buf, "HTTP request", reqHeaders);
/*     */         }
/* 174 */         buf.writeTo(con.getOutput());
/*     */       }
/*     */       else {
/* 177 */         reqHeaders.put("Content-Type", Collections.singletonList(ct.getContentType()));
/* 178 */         if (ct.getAcceptHeader() != null) {
/* 179 */           reqHeaders.put("Accept", Collections.singletonList(ct.getAcceptHeader()));
/*     */         }
/* 181 */         if ((this.binding instanceof SOAPBinding)) {
/* 182 */           writeSOAPAction(reqHeaders, ct.getSOAPActionHeader());
/*     */         }
/*     */ 
/* 185 */         if (dump) {
/* 186 */           ByteArrayBuffer buf = new ByteArrayBuffer();
/* 187 */           this.codec.encode(request, buf);
/* 188 */           dump(buf, "HTTP request - " + request.endpointAddress, reqHeaders);
/* 189 */           OutputStream out = con.getOutput();
/* 190 */           if (out != null)
/* 191 */             buf.writeTo(out);
/*     */         }
/*     */         else {
/* 194 */           OutputStream os = con.getOutput();
/* 195 */           if (os != null) {
/* 196 */             this.codec.encode(request, os);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 201 */       con.closeOutput();
/*     */ 
/* 203 */       return createResponsePacket(request, con);
/*     */     } catch (WebServiceException wex) {
/* 205 */       throw wex;
/*     */     } catch (Exception ex) {
/* 207 */       throw new WebServiceException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Packet createResponsePacket(Packet request, HttpClientTransport con) throws IOException {
/* 212 */     con.readResponseCodeAndMessage();
/* 213 */     recordCookies(request, con);
/*     */ 
/* 215 */     InputStream responseStream = con.getInput();
/* 216 */     if (dump) {
/* 217 */       ByteArrayBuffer buf = new ByteArrayBuffer();
/* 218 */       if (responseStream != null) {
/* 219 */         buf.write(responseStream);
/* 220 */         responseStream.close();
/*     */       }
/* 222 */       dump(buf, "HTTP response - " + request.endpointAddress + " - " + con.statusCode, con.getHeaders());
/* 223 */       responseStream = buf.newInputStream();
/*     */     }
/*     */ 
/* 227 */     int cl = con.contentLength;
/* 228 */     InputStream tempIn = null;
/* 229 */     if (cl == -1) {
/* 230 */       tempIn = StreamUtils.hasSomeData(responseStream);
/* 231 */       if (tempIn != null) {
/* 232 */         responseStream = tempIn;
/*     */       }
/*     */     }
/* 235 */     if (((cl == 0) || ((cl == -1) && (tempIn == null))) && 
/* 236 */       (responseStream != null)) {
/* 237 */       responseStream.close();
/* 238 */       responseStream = null;
/*     */     }
/*     */ 
/* 245 */     checkStatusCode(responseStream, con);
/*     */ 
/* 247 */     Packet reply = request.createClientResponse(null);
/* 248 */     reply.wasTransportSecure = con.isSecure();
/* 249 */     if (responseStream != null) {
/* 250 */       String contentType = con.getContentType();
/* 251 */       if ((contentType != null) && (contentType.contains("text/html")) && ((this.binding instanceof SOAPBinding))) {
/* 252 */         throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(con.statusCode), con.statusMessage));
/*     */       }
/* 254 */       this.codec.decode(responseStream, contentType, reply);
/*     */     }
/* 256 */     return reply;
/*     */   }
/*     */ 
/*     */   private void checkStatusCode(InputStream in, HttpClientTransport con)
/*     */     throws IOException
/*     */   {
/* 268 */     int statusCode = con.statusCode;
/* 269 */     String statusMessage = con.statusMessage;
/*     */ 
/* 271 */     if ((this.binding instanceof SOAPBinding)) {
/* 272 */       if (this.binding.getSOAPVersion() == SOAPVersion.SOAP_12)
/*     */       {
/* 274 */         if ((statusCode == 200) || (statusCode == 202) || (isErrorCode(statusCode)))
/*     */         {
/* 276 */           if ((isErrorCode(statusCode)) && (in == null))
/*     */           {
/* 278 */             throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/* 284 */       else if ((statusCode == 200) || (statusCode == 202) || (statusCode == 500))
/*     */       {
/* 286 */         if ((statusCode == 500) && (in == null))
/*     */         {
/* 288 */           throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
/*     */         }
/* 290 */         return;
/*     */       }
/*     */ 
/* 293 */       if (in != null) {
/* 294 */         in.close();
/*     */       }
/* 296 */       throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isErrorCode(int code)
/*     */   {
/* 303 */     return (code == 500) || (code == 400);
/*     */   }
/*     */ 
/*     */   private void addCookies(Packet context, Map<String, List<String>> reqHeaders) throws IOException {
/* 307 */     Boolean shouldMaintainSessionProperty = (Boolean)context.invocationProperties.get("javax.xml.ws.session.maintain");
/*     */ 
/* 309 */     if ((shouldMaintainSessionProperty != null) && (!shouldMaintainSessionProperty.booleanValue())) {
/* 310 */       return;
/*     */     }
/* 312 */     if ((this.sticky) || ((shouldMaintainSessionProperty != null) && (shouldMaintainSessionProperty.booleanValue()))) {
/* 313 */       Map cookies = this.cookieJar.get(context.endpointAddress.getURI(), reqHeaders);
/* 314 */       List cookieList = (List)cookies.get("Cookie");
/* 315 */       if ((cookieList != null) && (!cookieList.isEmpty())) {
/* 316 */         reqHeaders.put("Cookie", cookieList);
/*     */       }
/* 318 */       cookieList = (List)cookies.get("Cookie2");
/* 319 */       if ((cookieList != null) && (!cookieList.isEmpty()))
/* 320 */         reqHeaders.put("Cookie2", cookieList);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void recordCookies(Packet context, HttpClientTransport con) throws IOException
/*     */   {
/* 326 */     Boolean shouldMaintainSessionProperty = (Boolean)context.invocationProperties.get("javax.xml.ws.session.maintain");
/*     */ 
/* 328 */     if ((shouldMaintainSessionProperty != null) && (!shouldMaintainSessionProperty.booleanValue())) {
/* 329 */       return;
/*     */     }
/* 331 */     if ((this.sticky) || ((shouldMaintainSessionProperty != null) && (shouldMaintainSessionProperty.booleanValue())))
/* 332 */       this.cookieJar.put(context.endpointAddress.getURI(), con.getHeaders());
/*     */   }
/*     */ 
/*     */   private void addBasicAuth(Packet context, Map<String, List<String>> reqHeaders)
/*     */   {
/* 337 */     String user = (String)context.invocationProperties.get("javax.xml.ws.security.auth.username");
/* 338 */     if (user != null) {
/* 339 */       String pw = (String)context.invocationProperties.get("javax.xml.ws.security.auth.password");
/* 340 */       if (pw != null) {
/* 341 */         StringBuffer buf = new StringBuffer(user);
/* 342 */         buf.append(":");
/* 343 */         buf.append(pw);
/* 344 */         String creds = DatatypeConverter.printBase64Binary(buf.toString().getBytes());
/* 345 */         reqHeaders.put("Authorization", Collections.singletonList("Basic " + creds));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeSOAPAction(Map<String, List<String>> reqHeaders, String soapAction)
/*     */   {
/* 356 */     if (SOAPVersion.SOAP_12.equals(this.binding.getSOAPVersion()))
/* 357 */       return;
/* 358 */     if (soapAction != null)
/* 359 */       reqHeaders.put("SOAPAction", Collections.singletonList(soapAction));
/*     */     else
/* 361 */       reqHeaders.put("SOAPAction", Collections.singletonList("\"\""));
/*     */   }
/*     */ 
/*     */   public void preDestroy()
/*     */   {
/*     */   }
/*     */ 
/*     */   public HttpTransportPipe copy(TubeCloner cloner) {
/* 369 */     return new HttpTransportPipe(this, cloner);
/*     */   }
/*     */ 
/*     */   private void dump(ByteArrayBuffer buf, String caption, Map<String, List<String>> headers) throws IOException {
/* 373 */     System.out.println("---[" + caption + "]---");
/* 374 */     for (Map.Entry header : headers.entrySet()) {
/* 375 */       System.out.println((String)header.getKey() + ": " + header.getValue());
/*     */     }
/*     */ 
/* 378 */     buf.writeTo(System.out);
/* 379 */     System.out.println("--------------------");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  80 */       JAXBContext.newInstance(new Class[0]).createUnmarshaller();
/*     */     }
/*     */     catch (JAXBException je)
/*     */     {
/*     */     }
/*     */ 
/*     */     boolean b;
/*     */     try
/*     */     {
/* 390 */       b = Boolean.getBoolean(HttpTransportPipe.class.getName() + ".dump");
/*     */     } catch (Throwable t) {
/* 392 */       b = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe
 * JD-Core Version:    0.6.2
 */