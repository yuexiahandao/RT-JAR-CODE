/*     */ package com.sun.xml.internal.ws.client.dispatch;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.AsyncInvoker;
/*     */ import com.sun.xml.internal.ws.client.AsyncResponseImpl;
/*     */ import com.sun.xml.internal.ws.client.RequestContext;
/*     */ import com.sun.xml.internal.ws.client.ResponseContext;
/*     */ import com.sun.xml.internal.ws.client.ResponseContextReceiver;
/*     */ import com.sun.xml.internal.ws.client.Stub;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.message.AttachmentSetImpl;
/*     */ import com.sun.xml.internal.ws.message.DataHandlerAttachment;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*     */ import com.sun.xml.internal.ws.resources.DispatchMessages;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.AsyncHandler;
/*     */ import javax.xml.ws.Dispatch;
/*     */ import javax.xml.ws.Response;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ 
/*     */ public abstract class DispatchImpl<T> extends Stub
/*     */   implements Dispatch<T>
/*     */ {
/*     */   final Service.Mode mode;
/*     */   final QName portname;
/*     */   final SOAPVersion soapVersion;
/*     */   static final long AWAIT_TERMINATION_TIME = 800L;
/*     */   static final String HTTP_REQUEST_METHOD_GET = "GET";
/*     */   static final String HTTP_REQUEST_METHOD_POST = "POST";
/*     */   static final String HTTP_REQUEST_METHOD_PUT = "PUT";
/*     */ 
/*     */   @Deprecated
/*     */   protected DispatchImpl(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, @Nullable WSEndpointReference epr)
/*     */   {
/* 105 */     super(owner, pipe, binding, owner.getWsdlService() != null ? owner.getWsdlService().get(port) : null, owner.getEndpointAddress(port), epr);
/* 106 */     this.portname = port;
/* 107 */     this.mode = mode;
/* 108 */     this.soapVersion = binding.getSOAPVersion();
/*     */   }
/*     */ 
/*     */   protected DispatchImpl(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, @Nullable WSEndpointReference epr)
/*     */   {
/* 118 */     super(portInfo, binding, portInfo.getEndpointAddress(), epr);
/* 119 */     this.portname = portInfo.getPortName();
/* 120 */     this.mode = mode;
/* 121 */     this.soapVersion = binding.getSOAPVersion();
/*     */   }
/*     */ 
/*     */   abstract Packet createPacket(T paramT);
/*     */ 
/*     */   abstract T toReturnValue(Packet paramPacket);
/*     */ 
/*     */   public final Response<T> invokeAsync(T param)
/*     */   {
/* 138 */     AsyncInvoker invoker = new DispatchAsyncInvoker(param);
/* 139 */     AsyncResponseImpl ft = new AsyncResponseImpl(invoker, null);
/* 140 */     invoker.setReceiver(ft);
/* 141 */     ft.run();
/* 142 */     return ft;
/*     */   }
/*     */ 
/*     */   public final Future<?> invokeAsync(T param, AsyncHandler<T> asyncHandler) {
/* 146 */     AsyncInvoker invoker = new DispatchAsyncInvoker(param);
/* 147 */     AsyncResponseImpl ft = new AsyncResponseImpl(invoker, asyncHandler);
/* 148 */     invoker.setReceiver(ft);
/*     */ 
/* 159 */     ft.run();
/* 160 */     return ft;
/*     */   }
/*     */ 
/*     */   public final T doInvoke(T in, RequestContext rc, ResponseContextReceiver receiver)
/*     */   {
/*     */     Packet response;
/*     */     try
/*     */     {
/* 172 */       checkNullAllowed(in, rc, this.binding, this.mode);
/*     */ 
/* 174 */       Packet message = createPacket(in);
/* 175 */       resolveEndpointAddress(message, rc);
/* 176 */       setProperties(message, true);
/* 177 */       response = process(message, rc, receiver);
/* 178 */       Message msg = response.getMessage();
/*     */ 
/* 180 */       if ((msg != null) && (msg.isFault())) {
/* 181 */         SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
/*     */ 
/* 184 */         throw ((SOAPFaultException)faultBuilder.createException(null));
/*     */       }
/*     */     }
/*     */     catch (JAXBException e) {
/* 188 */       throw new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), new Object[] { e });
/*     */     }
/*     */     catch (WebServiceException e) {
/* 191 */       throw e;
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 196 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 199 */     return toReturnValue(response);
/*     */   }
/*     */ 
/*     */   public final T invoke(T in) {
/* 203 */     return doInvoke(in, this.requestContext, this);
/*     */   }
/*     */ 
/*     */   public final void invokeOneWay(T in) {
/*     */     try {
/* 208 */       checkNullAllowed(in, this.requestContext, this.binding, this.mode);
/*     */ 
/* 210 */       Packet request = createPacket(in);
/* 211 */       setProperties(request, false);
/* 212 */       response = process(request, this.requestContext, this);
/*     */     }
/*     */     catch (WebServiceException e)
/*     */     {
/*     */       Packet response;
/* 215 */       throw e;
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 220 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setProperties(Packet packet, boolean expectReply) {
/* 225 */     packet.expectReply = Boolean.valueOf(expectReply);
/*     */   }
/*     */ 
/*     */   static boolean isXMLHttp(@NotNull WSBinding binding) {
/* 229 */     return binding.getBindingId().equals(BindingID.XML_HTTP);
/*     */   }
/*     */ 
/*     */   static boolean isPAYLOADMode(@NotNull Service.Mode mode) {
/* 233 */     return mode == Service.Mode.PAYLOAD;
/*     */   }
/*     */ 
/*     */   static void checkNullAllowed(@Nullable Object in, RequestContext rc, WSBinding binding, Service.Mode mode)
/*     */   {
/* 238 */     if (in != null) {
/* 239 */       return;
/*     */     }
/*     */ 
/* 243 */     if (isXMLHttp(binding)) {
/* 244 */       if (methodNotOk(rc))
/* 245 */         throw new WebServiceException(DispatchMessages.INVALID_NULLARG_XMLHTTP_REQUEST_METHOD("POST", "GET"));
/*     */     }
/* 247 */     else if (mode == Service.Mode.MESSAGE)
/* 248 */       throw new WebServiceException(DispatchMessages.INVALID_NULLARG_SOAP_MSGMODE(mode.name(), Service.Mode.PAYLOAD.toString()));
/*     */   }
/*     */ 
/*     */   static boolean methodNotOk(@NotNull RequestContext rc)
/*     */   {
/* 253 */     String requestMethod = (String)rc.get("javax.xml.ws.http.request.method");
/* 254 */     String request = requestMethod == null ? "POST" : requestMethod;
/*     */ 
/* 256 */     return ("POST".equalsIgnoreCase(request)) || ("PUT".equalsIgnoreCase(request));
/*     */   }
/*     */ 
/*     */   public static void checkValidSOAPMessageDispatch(WSBinding binding, Service.Mode mode)
/*     */   {
/* 261 */     if (isXMLHttp(binding))
/* 262 */       throw new WebServiceException(DispatchMessages.INVALID_SOAPMESSAGE_DISPATCH_BINDING("http://www.w3.org/2004/08/wsdl/http", "http://schemas.xmlsoap.org/wsdl/soap/http or http://www.w3.org/2003/05/soap/bindings/HTTP/"));
/* 263 */     if (isPAYLOADMode(mode))
/* 264 */       throw new WebServiceException(DispatchMessages.INVALID_SOAPMESSAGE_DISPATCH_MSGMODE(mode.name(), Service.Mode.MESSAGE.toString()));
/*     */   }
/*     */ 
/*     */   public static void checkValidDataSourceDispatch(WSBinding binding, Service.Mode mode)
/*     */   {
/* 269 */     if (!isXMLHttp(binding))
/* 270 */       throw new WebServiceException(DispatchMessages.INVALID_DATASOURCE_DISPATCH_BINDING("SOAP/HTTP", "http://www.w3.org/2004/08/wsdl/http"));
/* 271 */     if (isPAYLOADMode(mode))
/* 272 */       throw new WebServiceException(DispatchMessages.INVALID_DATASOURCE_DISPATCH_MSGMODE(mode.name(), Service.Mode.MESSAGE.toString())); 
/*     */   }
/*     */ 
/* 276 */   @NotNull
/*     */   protected final QName getPortName() { return this.portname; }
/*     */ 
/*     */ 
/*     */   void resolveEndpointAddress(@NotNull Packet message, @NotNull RequestContext requestContext)
/*     */   {
/* 282 */     String endpoint = (String)requestContext.get("javax.xml.ws.service.endpoint.address");
/* 283 */     if (endpoint == null) {
/* 284 */       endpoint = message.endpointAddress.toString();
/*     */     }
/* 286 */     String pathInfo = null;
/* 287 */     String queryString = null;
/* 288 */     if (requestContext.get("javax.xml.ws.http.request.pathinfo") != null) {
/* 289 */       pathInfo = (String)requestContext.get("javax.xml.ws.http.request.pathinfo");
/*     */     }
/* 291 */     if (requestContext.get("javax.xml.ws.http.request.querystring") != null) {
/* 292 */       queryString = (String)requestContext.get("javax.xml.ws.http.request.querystring");
/*     */     }
/*     */ 
/* 295 */     String resolvedEndpoint = null;
/* 296 */     if ((pathInfo != null) || (queryString != null)) {
/* 297 */       pathInfo = checkPath(pathInfo);
/* 298 */       queryString = checkQuery(queryString);
/* 299 */       if (endpoint != null) {
/*     */         try {
/* 301 */           URI endpointURI = new URI(endpoint);
/* 302 */           resolvedEndpoint = resolveURI(endpointURI, pathInfo, queryString);
/*     */         } catch (URISyntaxException e) {
/* 304 */           throw new WebServiceException(DispatchMessages.INVALID_URI(endpoint));
/*     */         }
/*     */       }
/* 307 */       requestContext.put("javax.xml.ws.service.endpoint.address", resolvedEndpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   protected String resolveURI(@NotNull URI endpointURI, @Nullable String pathInfo, @Nullable String queryString) {
/* 313 */     String query = null;
/* 314 */     String fragment = null;
/* 315 */     if (queryString != null) {
/*     */       URI result;
/*     */       try {
/* 318 */         URI tp = new URI(null, null, endpointURI.getPath(), queryString, null);
/* 319 */         result = endpointURI.resolve(tp);
/*     */       } catch (URISyntaxException e) {
/* 321 */         throw new WebServiceException(DispatchMessages.INVALID_QUERY_STRING(queryString));
/*     */       }
/* 323 */       query = result.getQuery();
/* 324 */       fragment = result.getFragment();
/*     */     }
/*     */ 
/* 327 */     String path = pathInfo != null ? pathInfo : endpointURI.getPath();
/*     */     try
/*     */     {
/* 336 */       StringBuilder spec = new StringBuilder();
/* 337 */       if (path != null) {
/* 338 */         spec.append(path);
/*     */       }
/* 340 */       if (query != null) {
/* 341 */         spec.append("?");
/* 342 */         spec.append(query);
/*     */       }
/* 344 */       if (fragment != null) {
/* 345 */         spec.append("#");
/* 346 */         spec.append(fragment);
/*     */       }
/* 348 */       return new URL(endpointURI.toURL(), spec.toString()).toExternalForm(); } catch (MalformedURLException e) {
/*     */     }
/* 350 */     throw new WebServiceException(DispatchMessages.INVALID_URI_RESOLUTION(path));
/*     */   }
/*     */ 
/*     */   private static String checkPath(@Nullable String path)
/*     */   {
/* 356 */     return "/" + path;
/*     */   }
/*     */ 
/*     */   private static String checkQuery(@Nullable String query) {
/* 360 */     if (query == null) return null;
/*     */ 
/* 362 */     if (query.indexOf('?') == 0)
/* 363 */       throw new WebServiceException(DispatchMessages.INVALID_QUERY_LEADING_CHAR(query));
/* 364 */     return query;
/*     */   }
/*     */ 
/*     */   protected AttachmentSet setOutboundAttachments()
/*     */   {
/* 369 */     HashMap attachments = (HashMap)getRequestContext().get("javax.xml.ws.binding.attachments.outbound");
/*     */ 
/* 372 */     if (attachments != null) {
/* 373 */       List alist = new ArrayList();
/* 374 */       for (Map.Entry att : attachments.entrySet()) {
/* 375 */         DataHandlerAttachment dha = new DataHandlerAttachment((String)att.getKey(), (DataHandler)att.getValue());
/* 376 */         alist.add(dha);
/*     */       }
/* 378 */       return new AttachmentSetImpl(alist);
/*     */     }
/* 380 */     return new AttachmentSetImpl();
/*     */   }
/*     */ 
/*     */   public void setOutboundHeaders(Object[] headers)
/*     */   {
/* 483 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static Dispatch<Source> createSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*     */   {
/* 492 */     if (isXMLHttp(binding)) {
/* 493 */       return new RESTSourceDispatch(port, mode, owner, pipe, binding, epr);
/*     */     }
/* 495 */     return new SOAPSourceDispatch(port, mode, owner, pipe, binding, epr);
/*     */   }
/*     */ 
/*     */   public static Dispatch<Source> createSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/* 499 */     if (isXMLHttp(binding)) {
/* 500 */       return new RESTSourceDispatch(portInfo, mode, binding, epr);
/*     */     }
/* 502 */     return new SOAPSourceDispatch(portInfo, mode, binding, epr);
/*     */   }
/*     */ 
/*     */   private class DispatchAsyncInvoker extends AsyncInvoker
/*     */   {
/*     */     private final T param;
/* 431 */     private final RequestContext rc = DispatchImpl.this.requestContext.copy();
/*     */ 
/*     */     DispatchAsyncInvoker() {
/* 434 */       this.param = param;
/*     */     }
/*     */ 
/*     */     public void do_run() {
/* 438 */       DispatchImpl.checkNullAllowed(this.param, this.rc, DispatchImpl.this.binding, DispatchImpl.this.mode);
/* 439 */       Packet message = DispatchImpl.this.createPacket(this.param);
/* 440 */       DispatchImpl.this.resolveEndpointAddress(message, this.rc);
/* 441 */       DispatchImpl.this.setProperties(message, true);
/* 442 */       Fiber.CompletionCallback callback = new Fiber.CompletionCallback() {
/*     */         public void onCompletion(@NotNull Packet response) {
/* 444 */           Message msg = response.getMessage();
/*     */           try {
/* 446 */             if ((msg != null) && (msg.isFault())) {
/* 447 */               SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
/*     */ 
/* 450 */               throw ((SOAPFaultException)faultBuilder.createException(null));
/*     */             }
/* 452 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.setResponseContext(new ResponseContext(response));
/* 453 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(DispatchImpl.this.toReturnValue(response), null);
/*     */           }
/*     */           catch (JAXBException e) {
/* 456 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(null, new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), new Object[] { e }));
/*     */           }
/*     */           catch (WebServiceException e) {
/* 459 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(null, e);
/*     */           }
/*     */           catch (Throwable e)
/*     */           {
/* 464 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(null, new WebServiceException(e));
/*     */           }
/*     */         }
/*     */ 
/* 468 */         public void onCompletion(@NotNull Throwable error) { if ((error instanceof WebServiceException)) {
/* 469 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(null, error);
/*     */           }
/*     */           else
/*     */           {
/* 474 */             DispatchImpl.DispatchAsyncInvoker.this.responseImpl.set(null, new WebServiceException(error));
/*     */           }
/*     */         }
/*     */       };
/* 478 */       DispatchImpl.this.processAsync(message, this.rc, callback);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Invoker
/*     */     implements Callable
/*     */   {
/*     */     private final T param;
/* 403 */     private final RequestContext rc = DispatchImpl.this.requestContext.copy();
/*     */     private ResponseContextReceiver receiver;
/*     */ 
/*     */     Invoker()
/*     */     {
/* 412 */       this.param = param;
/*     */     }
/*     */ 
/*     */     public T call() throws Exception {
/* 416 */       return DispatchImpl.this.doInvoke(this.param, this.rc, this.receiver);
/*     */     }
/*     */ 
/*     */     void setReceiver(ResponseContextReceiver receiver) {
/* 420 */       this.receiver = receiver;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.DispatchImpl
 * JD-Core Version:    0.6.2
 */