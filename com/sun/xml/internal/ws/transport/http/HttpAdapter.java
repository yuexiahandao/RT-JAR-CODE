/*     */ package com.sun.xml.internal.ws.transport.http;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.PropertySet;
/*     */ import com.sun.xml.internal.ws.api.ha.HaInfo;
/*     */ import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.api.pipe.ContentType;
/*     */ import com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport;
/*     */ import com.sun.xml.internal.ws.api.server.Adapter;
/*     */ import com.sun.xml.internal.ws.api.server.Adapter.Toolkit;
/*     */ import com.sun.xml.internal.ws.api.server.BoundEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;
/*     */ import com.sun.xml.internal.ws.api.server.EndpointComponent;
/*     */ import com.sun.xml.internal.ws.api.server.Module;
/*     */ import com.sun.xml.internal.ws.api.server.PortAddressResolver;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*     */ import com.sun.xml.internal.ws.api.server.ServiceDefinition;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint.CompletionCallback;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint.PipeHead;
/*     */ import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
/*     */ import com.sun.xml.internal.ws.resources.WsservletMessages;
/*     */ import com.sun.xml.internal.ws.server.UnsupportedMediaException;
/*     */ import com.sun.xml.internal.ws.util.ByteArrayBuffer;
/*     */ import com.sun.xml.internal.ws.util.Pool;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Binding;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.http.HTTPBinding;
/*     */ 
/*     */ public class HttpAdapter extends Adapter<HttpToolkit>
/*     */ {
/*     */   private Map<String, SDDocument> wsdls;
/*     */   private Map<SDDocument, String> revWsdls;
/* 102 */   private ServiceDefinition serviceDefinition = null;
/*     */   public final HttpAdapterList<? extends HttpAdapter> owner;
/*     */   public final String urlPattern;
/*     */   protected boolean stickyCookie;
/*     */   public static final CompletionCallback NO_OP_COMPLETION_CALLBACK;
/*     */   public static boolean dump;
/*     */   public static boolean publishStatusPage;
/* 828 */   private static final Logger LOGGER = Logger.getLogger(HttpAdapter.class.getName());
/*     */ 
/*     */   public static HttpAdapter createAlone(WSEndpoint endpoint)
/*     */   {
/* 125 */     return new DummyList(null).createAdapter("", "", endpoint);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   protected HttpAdapter(WSEndpoint endpoint, HttpAdapterList<? extends HttpAdapter> owner)
/*     */   {
/* 135 */     this(endpoint, owner, null);
/*     */   }
/*     */ 
/*     */   protected HttpAdapter(WSEndpoint endpoint, HttpAdapterList<? extends HttpAdapter> owner, String urlPattern) {
/* 139 */     super(endpoint);
/* 140 */     this.owner = owner;
/* 141 */     this.urlPattern = urlPattern;
/*     */ 
/* 143 */     initWSDLMap(endpoint.getServiceDefinition());
/*     */   }
/*     */ 
/*     */   public ServiceDefinition getServiceDefinition()
/*     */   {
/* 152 */     return this.serviceDefinition;
/*     */   }
/*     */ 
/*     */   public void initWSDLMap(ServiceDefinition sdef)
/*     */   {
/* 161 */     this.serviceDefinition = sdef;
/* 162 */     if (sdef == null) {
/* 163 */       this.wsdls = Collections.emptyMap();
/* 164 */       this.revWsdls = Collections.emptyMap();
/*     */     } else {
/* 166 */       this.wsdls = new HashMap();
/*     */ 
/* 169 */       Map systemIds = new TreeMap();
/* 170 */       for (SDDocument sdd : sdef) {
/* 171 */         if (sdd == sdef.getPrimary()) {
/* 172 */           this.wsdls.put("wsdl", sdd);
/* 173 */           this.wsdls.put("WSDL", sdd);
/*     */         } else {
/* 175 */           systemIds.put(sdd.getURL().toString(), sdd);
/*     */         }
/*     */       }
/*     */ 
/* 179 */       int wsdlnum = 1;
/* 180 */       int xsdnum = 1;
/* 181 */       for (Map.Entry e : systemIds.entrySet()) {
/* 182 */         SDDocument sdd = (SDDocument)e.getValue();
/* 183 */         if (sdd.isWSDL()) {
/* 184 */           this.wsdls.put("wsdl=" + wsdlnum++, sdd);
/*     */         }
/* 186 */         if (sdd.isSchema()) {
/* 187 */           this.wsdls.put("xsd=" + xsdnum++, sdd);
/*     */         }
/*     */       }
/*     */ 
/* 191 */       this.revWsdls = new HashMap();
/* 192 */       for (Map.Entry e : this.wsdls.entrySet())
/* 193 */         if (!((String)e.getKey()).equals("WSDL"))
/* 194 */           this.revWsdls.put(e.getValue(), e.getKey());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getValidPath()
/*     */   {
/* 205 */     if (this.urlPattern.endsWith("/*")) {
/* 206 */       return this.urlPattern.substring(0, this.urlPattern.length() - 2);
/*     */     }
/* 208 */     return this.urlPattern;
/*     */   }
/*     */ 
/*     */   protected HttpToolkit createToolkit()
/*     */   {
/* 213 */     return new HttpToolkit();
/*     */   }
/*     */ 
/*     */   public void handle(@NotNull WSHTTPConnection connection)
/*     */     throws IOException
/*     */   {
/* 235 */     if (handleGet(connection)) {
/* 236 */       return;
/*     */     }
/*     */ 
/* 240 */     Pool currentPool = getPool();
/*     */ 
/* 242 */     HttpToolkit tk = (HttpToolkit)currentPool.take();
/*     */     try {
/* 244 */       tk.handle(connection);
/*     */     } finally {
/* 246 */       currentPool.recycle(tk);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean handleGet(@NotNull WSHTTPConnection connection) throws IOException {
/* 251 */     if (connection.getRequestMethod().equals("GET"))
/*     */     {
/* 253 */       for (EndpointComponent c : this.endpoint.getComponentRegistry()) {
/* 254 */         HttpMetadataPublisher spi = (HttpMetadataPublisher)c.getSPI(HttpMetadataPublisher.class);
/* 255 */         if ((spi != null) && (spi.handleMetadataRequest(this, connection))) {
/* 256 */           return true;
/*     */         }
/*     */       }
/* 259 */       if (isMetadataQuery(connection.getQueryString()))
/*     */       {
/* 261 */         publishWSDL(connection);
/* 262 */         return true;
/*     */       }
/*     */ 
/* 265 */       Binding binding = getEndpoint().getBinding();
/* 266 */       if (!(binding instanceof HTTPBinding))
/*     */       {
/* 268 */         writeWebServicesHtmlPage(connection);
/* 269 */         return true;
/*     */       }
/* 271 */     } else if (connection.getRequestMethod().equals("HEAD")) {
/* 272 */       connection.getInput().close();
/* 273 */       Binding binding = getEndpoint().getBinding();
/* 274 */       if (isMetadataQuery(connection.getQueryString())) {
/* 275 */         SDDocument doc = (SDDocument)this.wsdls.get(connection.getQueryString());
/* 276 */         connection.setStatus(doc != null ? 200 : 404);
/*     */ 
/* 279 */         connection.getOutput().close();
/* 280 */         connection.close();
/* 281 */         return true;
/* 282 */       }if (!(binding instanceof HTTPBinding)) {
/* 283 */         connection.setStatus(404);
/* 284 */         connection.getOutput().close();
/* 285 */         connection.close();
/* 286 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 291 */     return false;
/*     */   }
/*     */ 
/*     */   private Packet decodePacket(@NotNull WSHTTPConnection con, @NotNull Codec codec)
/*     */     throws IOException
/*     */   {
/* 304 */     String ct = con.getRequestHeader("Content-Type");
/* 305 */     InputStream in = con.getInput();
/* 306 */     Packet packet = new Packet();
/* 307 */     packet.soapAction = fixQuotesAroundSoapAction(con.getRequestHeader("SOAPAction"));
/* 308 */     packet.wasTransportSecure = con.isSecure();
/* 309 */     packet.acceptableMimeTypes = con.getRequestHeader("Accept");
/* 310 */     packet.addSatellite(con);
/* 311 */     packet.transportBackChannel = new Oneway(con);
/* 312 */     packet.webServiceContextDelegate = con.getWebServiceContextDelegate();
/*     */ 
/* 314 */     if (dump) {
/* 315 */       ByteArrayBuffer buf = new ByteArrayBuffer();
/* 316 */       buf.write(in);
/* 317 */       in.close();
/* 318 */       dump(buf, "HTTP request", con.getRequestHeaders());
/* 319 */       in = buf.newInputStream();
/*     */     }
/* 321 */     codec.decode(in, ct, packet);
/* 322 */     return packet;
/*     */   }
/*     */ 
/*     */   private String fixQuotesAroundSoapAction(String soapAction)
/*     */   {
/* 333 */     if ((soapAction != null) && ((!soapAction.startsWith("\"")) || (!soapAction.endsWith("\"")))) {
/* 334 */       LOGGER.warning("Received WS-I BP non-conformant Unquoted SoapAction HTTP header: " + soapAction);
/* 335 */       String fixedSoapAction = soapAction;
/* 336 */       if (!soapAction.startsWith("\""))
/* 337 */         fixedSoapAction = "\"" + fixedSoapAction;
/* 338 */       if (!soapAction.endsWith("\""))
/* 339 */         fixedSoapAction = fixedSoapAction + "\"";
/* 340 */       return fixedSoapAction;
/*     */     }
/* 342 */     return soapAction;
/*     */   }
/*     */ 
/*     */   private void encodePacket(@NotNull Packet packet, @NotNull WSHTTPConnection con, @NotNull Codec codec) throws IOException
/*     */   {
/* 347 */     if (con.isClosed()) {
/* 348 */       return;
/*     */     }
/* 350 */     Message responseMessage = packet.getMessage();
/* 351 */     addStickyCookie(con);
/* 352 */     addReplicaCookie(con, packet);
/* 353 */     if (responseMessage == null) {
/* 354 */       if (!con.isClosed())
/*     */       {
/* 357 */         if (con.getStatus() == 0)
/* 358 */           con.setStatus(202);
/*     */         try
/*     */         {
/* 361 */           con.getOutput().close();
/*     */         } catch (IOException e) {
/* 363 */           throw new WebServiceException(e);
/*     */         }
/*     */       }
/*     */     } else {
/* 367 */       if (con.getStatus() == 0)
/*     */       {
/* 370 */         con.setStatus(responseMessage.isFault() ? 500 : 200);
/*     */       }
/*     */ 
/* 375 */       ContentType contentType = codec.getStaticContentType(packet);
/* 376 */       if (contentType != null) {
/* 377 */         con.setContentTypeResponseHeader(contentType.getContentType());
/* 378 */         OutputStream os = con.getProtocol().contains("1.1") ? con.getOutput() : new Http10OutputStream(con);
/* 379 */         if (dump) {
/* 380 */           ByteArrayBuffer buf = new ByteArrayBuffer();
/* 381 */           codec.encode(packet, buf);
/* 382 */           dump(buf, "HTTP response " + con.getStatus(), con.getResponseHeaders());
/* 383 */           buf.writeTo(os);
/*     */         } else {
/* 385 */           codec.encode(packet, os);
/*     */         }
/* 387 */         os.close();
/*     */       }
/*     */       else {
/* 390 */         ByteArrayBuffer buf = new ByteArrayBuffer();
/* 391 */         contentType = codec.encode(packet, buf);
/* 392 */         con.setContentTypeResponseHeader(contentType.getContentType());
/* 393 */         if (dump) {
/* 394 */           dump(buf, "HTTP response " + con.getStatus(), con.getResponseHeaders());
/*     */         }
/* 396 */         OutputStream os = con.getOutput();
/* 397 */         buf.writeTo(os);
/* 398 */         os.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addStickyCookie(WSHTTPConnection con)
/*     */   {
/* 416 */     if (this.stickyCookie) {
/* 417 */       String proxyJroute = con.getRequestHeader("proxy-jroute");
/* 418 */       if (proxyJroute == null)
/*     */       {
/* 420 */         return;
/*     */       }
/*     */ 
/* 423 */       String jrouteId = con.getCookie("JROUTE");
/* 424 */       if ((jrouteId == null) || (!jrouteId.equals(proxyJroute)))
/*     */       {
/* 426 */         con.setCookie("JROUTE", proxyJroute);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addReplicaCookie(WSHTTPConnection con, Packet packet) {
/* 432 */     if (this.stickyCookie) {
/* 433 */       HaInfo haInfo = null;
/* 434 */       if (packet.supports("com.sun.xml.internal.ws.api.message.packet.hainfo")) {
/* 435 */         haInfo = (HaInfo)packet.get("com.sun.xml.internal.ws.api.message.packet.hainfo");
/*     */       }
/* 437 */       if (haInfo != null) {
/* 438 */         con.setCookie("METRO_KEY", haInfo.getKey());
/* 439 */         con.setCookie("JREPLICA", haInfo.getReplicaInstance());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invokeAsync(WSHTTPConnection con) throws IOException {
/* 445 */     invokeAsync(con, NO_OP_COMPLETION_CALLBACK);
/*     */   }
/*     */ 
/*     */   public void invokeAsync(final WSHTTPConnection con, final CompletionCallback callback) throws IOException
/*     */   {
/* 450 */     if (handleGet(con)) {
/* 451 */       callback.onCompletion();
/* 452 */       return;
/*     */     }
/* 454 */     final Pool currentPool = getPool();
/* 455 */     final HttpToolkit tk = (HttpToolkit)currentPool.take();
/*     */     Packet request;
/*     */     try {
/* 460 */       request = decodePacket(con, tk.codec);
/*     */     } catch (ExceptionHasMessage e) {
/* 462 */       LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 463 */       Packet response = new Packet();
/* 464 */       response.setMessage(e.getFaultMessage());
/* 465 */       encodePacket(response, con, tk.codec);
/* 466 */       currentPool.recycle(tk);
/* 467 */       con.close();
/* 468 */       callback.onCompletion();
/* 469 */       return;
/*     */     } catch (UnsupportedMediaException e) {
/* 471 */       LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 472 */       Packet response = new Packet();
/* 473 */       con.setStatus(415);
/* 474 */       encodePacket(response, con, tk.codec);
/* 475 */       currentPool.recycle(tk);
/* 476 */       con.close();
/* 477 */       callback.onCompletion();
/* 478 */       return;
/*     */     }
/*     */ 
/* 481 */     this.endpoint.process(request, new WSEndpoint.CompletionCallback() {
/*     */       public void onCompletion(@NotNull Packet response) {
/*     */         try {
/*     */           try {
/* 485 */             HttpAdapter.this.encodePacket(response, con, tk.codec);
/*     */           } catch (IOException ioe) {
/* 487 */             HttpAdapter.LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
/*     */           }
/* 489 */           currentPool.recycle(tk);
/*     */         } finally {
/* 491 */           con.close();
/* 492 */           callback.onCompletion();
/*     */         }
/*     */       }
/*     */     }
/*     */     , null);
/*     */   }
/*     */ 
/*     */   private boolean isMetadataQuery(String query)
/*     */   {
/* 624 */     return (query != null) && ((query.equals("WSDL")) || (query.startsWith("wsdl")) || (query.startsWith("xsd=")));
/*     */   }
/*     */ 
/*     */   public void publishWSDL(@NotNull WSHTTPConnection con)
/*     */     throws IOException
/*     */   {
/* 637 */     con.getInput().close();
/*     */ 
/* 639 */     SDDocument doc = (SDDocument)this.wsdls.get(con.getQueryString());
/* 640 */     if (doc == null) {
/* 641 */       writeNotFoundErrorPage(con, "Invalid Request");
/* 642 */       return;
/*     */     }
/*     */ 
/* 645 */     con.setStatus(200);
/* 646 */     con.setContentTypeResponseHeader("text/xml;charset=utf-8");
/*     */ 
/* 648 */     OutputStream os = con.getProtocol().contains("1.1") ? con.getOutput() : new Http10OutputStream(con);
/*     */ 
/* 650 */     PortAddressResolver portAddressResolver = this.owner.createPortAddressResolver(con.getBaseAddress());
/* 651 */     final String address = portAddressResolver.getAddressFor(this.endpoint.getServiceName(), this.endpoint.getPortName().getLocalPart());
/* 652 */     assert (address != null);
/* 653 */     DocumentAddressResolver resolver = new DocumentAddressResolver()
/*     */     {
/*     */       public String getRelativeAddressFor(@NotNull SDDocument current, @NotNull SDDocument referenced) {
/* 656 */         assert (HttpAdapter.this.revWsdls.containsKey(referenced));
/* 657 */         return address + '?' + (String)HttpAdapter.this.revWsdls.get(referenced);
/*     */       }
/*     */     };
/* 661 */     doc.writeTo(portAddressResolver, resolver, os);
/* 662 */     os.close();
/*     */   }
/*     */ 
/*     */   private void writeNotFoundErrorPage(WSHTTPConnection con, String message)
/*     */     throws IOException
/*     */   {
/* 687 */     con.setStatus(404);
/* 688 */     con.setContentTypeResponseHeader("text/html; charset=utf-8");
/*     */ 
/* 690 */     PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutput(), "UTF-8"));
/* 691 */     out.println("<html>");
/* 692 */     out.println("<head><title>");
/* 693 */     out.println(WsservletMessages.SERVLET_HTML_TITLE());
/* 694 */     out.println("</title></head>");
/* 695 */     out.println("<body>");
/* 696 */     out.println(WsservletMessages.SERVLET_HTML_NOT_FOUND(message));
/* 697 */     out.println("</body>");
/* 698 */     out.println("</html>");
/* 699 */     out.close();
/*     */   }
/*     */ 
/*     */   private void writeInternalServerError(WSHTTPConnection con) throws IOException {
/* 703 */     con.setStatus(500);
/* 704 */     con.getOutput().close();
/*     */   }
/*     */ 
/*     */   private void dump(ByteArrayBuffer buf, String caption, Map<String, List<String>> headers)
/*     */     throws IOException
/*     */   {
/* 715 */     System.out.println("---[" + caption + "]---");
/*     */     Iterator i$;
/* 716 */     if (headers != null)
/* 717 */       for (i$ = headers.entrySet().iterator(); i$.hasNext(); ) { header = (Map.Entry)i$.next();
/* 718 */         if (((List)header.getValue()).isEmpty())
/*     */         {
/* 721 */           System.out.println(header.getValue());
/*     */         }
/* 723 */         else for (String value : (List)header.getValue())
/* 724 */             System.out.println((String)header.getKey() + ": " + value);
/*     */       }
/*     */     Map.Entry header;
/* 729 */     buf.writeTo(System.out);
/* 730 */     System.out.println("--------------------");
/*     */   }
/*     */ 
/*     */   private void writeWebServicesHtmlPage(WSHTTPConnection con)
/*     */     throws IOException
/*     */   {
/* 737 */     if (!publishStatusPage) return;
/*     */ 
/* 741 */     con.getInput().close();
/*     */ 
/* 744 */     con.setStatus(200);
/* 745 */     con.setContentTypeResponseHeader("text/html; charset=utf-8");
/*     */ 
/* 747 */     PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutput(), "UTF-8"));
/* 748 */     out.println("<html>");
/* 749 */     out.println("<head><title>");
/*     */ 
/* 751 */     out.println(WsservletMessages.SERVLET_HTML_TITLE());
/* 752 */     out.println("</title></head>");
/* 753 */     out.println("<body>");
/*     */ 
/* 755 */     out.println(WsservletMessages.SERVLET_HTML_TITLE_2());
/*     */ 
/* 758 */     Module module = (Module)getEndpoint().getContainer().getSPI(Module.class);
/* 759 */     List endpoints = Collections.emptyList();
/* 760 */     if (module != null) {
/* 761 */       endpoints = module.getBoundEndpoints();
/*     */     }
/*     */ 
/* 764 */     if (endpoints.isEmpty())
/*     */     {
/* 766 */       out.println(WsservletMessages.SERVLET_HTML_NO_INFO_AVAILABLE());
/*     */     } else {
/* 768 */       out.println("<table width='100%' border='1'>");
/* 769 */       out.println("<tr>");
/* 770 */       out.println("<td>");
/*     */ 
/* 772 */       out.println(WsservletMessages.SERVLET_HTML_COLUMN_HEADER_PORT_NAME());
/* 773 */       out.println("</td>");
/*     */ 
/* 775 */       out.println("<td>");
/*     */ 
/* 777 */       out.println(WsservletMessages.SERVLET_HTML_COLUMN_HEADER_INFORMATION());
/* 778 */       out.println("</td>");
/* 779 */       out.println("</tr>");
/*     */ 
/* 781 */       for (BoundEndpoint a : endpoints) {
/* 782 */         String endpointAddress = a.getAddress(con.getBaseAddress()).toString();
/* 783 */         out.println("<tr>");
/*     */ 
/* 785 */         out.println("<td>");
/* 786 */         out.println(WsservletMessages.SERVLET_HTML_ENDPOINT_TABLE(a.getEndpoint().getServiceName(), a.getEndpoint().getPortName()));
/*     */ 
/* 790 */         out.println("</td>");
/*     */ 
/* 792 */         out.println("<td>");
/* 793 */         out.println(WsservletMessages.SERVLET_HTML_INFORMATION_TABLE(endpointAddress, a.getEndpoint().getImplementationClass().getName()));
/*     */ 
/* 797 */         out.println("</td>");
/*     */ 
/* 799 */         out.println("</tr>");
/*     */       }
/* 801 */       out.println("</table>");
/*     */     }
/* 803 */     out.println("</body>");
/* 804 */     out.println("</html>");
/* 805 */     out.close();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 500 */     NO_OP_COMPLETION_CALLBACK = new CompletionCallback()
/*     */     {
/*     */       public void onCompletion()
/*     */       {
/*     */       }
/*     */     };
/* 811 */     dump = false;
/*     */ 
/* 813 */     publishStatusPage = true;
/*     */     try
/*     */     {
/* 817 */       dump = Boolean.getBoolean(HttpAdapter.class.getName() + ".dump");
/*     */     }
/*     */     catch (Throwable t) {
/*     */     }
/*     */     try {
/* 822 */       publishStatusPage = System.getProperty(HttpAdapter.class.getName() + ".publishStatusPage").equals("true");
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   final class AsyncTransport extends AbstractServerAsyncTransport<WSHTTPConnection>
/*     */   {
/*     */     public AsyncTransport()
/*     */     {
/* 514 */       super();
/*     */     }
/*     */ 
/*     */     public void handleAsync(WSHTTPConnection con) throws IOException {
/* 518 */       super.handle(con);
/*     */     }
/*     */ 
/*     */     protected void encodePacket(WSHTTPConnection con, @NotNull Packet packet, @NotNull Codec codec) throws IOException {
/* 522 */       HttpAdapter.this.encodePacket(packet, con, codec);
/*     */     }
/*     */     @Nullable
/*     */     protected String getAcceptableMimeTypes(WSHTTPConnection con) {
/* 526 */       return null;
/*     */     }
/*     */     @Nullable
/*     */     protected TransportBackChannel getTransportBackChannel(WSHTTPConnection con) {
/* 530 */       return new HttpAdapter.Oneway(HttpAdapter.this, con);
/*     */     }
/*     */ 
/*     */     @NotNull
/*     */     protected PropertySet getPropertySet(WSHTTPConnection con) {
/* 535 */       return con;
/*     */     }
/*     */     @NotNull
/*     */     protected WebServiceContextDelegate getWebServiceContextDelegate(WSHTTPConnection con) {
/* 539 */       return con.getWebServiceContextDelegate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface CompletionCallback
/*     */   {
/*     */     public abstract void onCompletion();
/*     */   }
/*     */ 
/*     */   private static final class DummyList extends HttpAdapterList<HttpAdapter>
/*     */   {
/*     */     protected HttpAdapter createHttpAdapter(String name, String urlPattern, WSEndpoint<?> endpoint)
/*     */     {
/* 710 */       return new HttpAdapter(endpoint, this, urlPattern);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Http10OutputStream extends ByteArrayBuffer
/*     */   {
/*     */     private final WSHTTPConnection con;
/*     */ 
/*     */     Http10OutputStream(WSHTTPConnection con)
/*     */     {
/* 673 */       this.con = con;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 678 */       super.close();
/* 679 */       this.con.setContentLengthResponseHeader(size());
/* 680 */       OutputStream os = this.con.getOutput();
/* 681 */       writeTo(os);
/* 682 */       os.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   final class HttpToolkit extends Adapter.Toolkit
/*     */   {
/*     */     HttpToolkit()
/*     */     {
/* 570 */       super();
/*     */     }
/*     */     public void handle(WSHTTPConnection con) throws IOException { try { boolean invoke = false;
/*     */         Packet packet;
/*     */         try {
/* 576 */           packet = HttpAdapter.this.decodePacket(con, this.codec);
/* 577 */           invoke = true;
/*     */         } catch (Exception e) {
/* 579 */           packet = new Packet();
/* 580 */           if ((e instanceof ExceptionHasMessage)) {
/* 581 */             HttpAdapter.LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 582 */             packet.setMessage(((ExceptionHasMessage)e).getFaultMessage());
/* 583 */           } else if ((e instanceof UnsupportedMediaException)) {
/* 584 */             HttpAdapter.LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 585 */             con.setStatus(415);
/*     */           } else {
/* 587 */             HttpAdapter.LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 588 */             con.setStatus(500);
/*     */           }
/*     */         }
/* 591 */         if (invoke) {
/*     */           try {
/* 593 */             packet = this.head.process(packet, con.getWebServiceContextDelegate(), packet.transportBackChannel);
/*     */           }
/*     */           catch (Exception e) {
/* 596 */             HttpAdapter.LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 597 */             if (!con.isClosed()) {
/* 598 */               HttpAdapter.this.writeInternalServerError(con);
/*     */             }
/*     */             return;
/*     */           }
/*     */         }
/* 603 */         HttpAdapter.this.encodePacket(packet, con, this.codec);
/*     */       } finally {
/* 605 */         if (!con.isClosed())
/* 606 */           con.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final class Oneway
/*     */     implements TransportBackChannel
/*     */   {
/*     */     WSHTTPConnection con;
/*     */     boolean closed;
/*     */ 
/*     */     Oneway(WSHTTPConnection con)
/*     */     {
/* 548 */       this.con = con;
/*     */     }
/*     */     public void close() {
/* 551 */       if (!this.closed) {
/* 552 */         this.closed = true;
/*     */ 
/* 554 */         if (this.con.getStatus() == 0)
/*     */         {
/* 557 */           this.con.setStatus(202);
/*     */         }
/*     */         try
/*     */         {
/* 561 */           this.con.getOutput().close();
/*     */         } catch (IOException e) {
/* 563 */           throw new WebServiceException(e);
/*     */         }
/* 565 */         this.con.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.HttpAdapter
 * JD-Core Version:    0.6.2
 */