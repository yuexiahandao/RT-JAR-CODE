/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpPrincipal;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLSession;
/*     */ 
/*     */ class ExchangeImpl
/*     */ {
/*     */   Headers reqHdrs;
/*     */   Headers rspHdrs;
/*     */   Request req;
/*     */   String method;
/*     */   boolean writefinished;
/*     */   URI uri;
/*     */   HttpConnection connection;
/*     */   long reqContentLen;
/*     */   long rspContentLen;
/*     */   InputStream ris;
/*     */   OutputStream ros;
/*     */   Thread thread;
/*     */   boolean close;
/*     */   boolean closed;
/*  53 */   boolean http10 = false;
/*     */   private static final String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*  57 */   private static final TimeZone gmtTZ = TimeZone.getTimeZone("GMT");
/*  58 */   private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal()
/*     */   {
/*     */     protected DateFormat initialValue() {
/*  61 */       SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
/*  62 */       localSimpleDateFormat.setTimeZone(ExchangeImpl.gmtTZ);
/*  63 */       return localSimpleDateFormat;
/*     */     }
/*  58 */   };
/*     */   private static final String HEAD = "HEAD";
/*     */   InputStream uis;
/*     */   OutputStream uos;
/*     */   LeftOverInputStream uis_orig;
/*     */   PlaceholderOutputStream uos_orig;
/*     */   boolean sentHeaders;
/*     */   Map<String, Object> attributes;
/*  79 */   int rcode = -1;
/*     */   HttpPrincipal principal;
/*     */   ServerImpl server;
/* 295 */   private byte[] rspbuf = new byte[''];
/*     */ 
/*     */   ExchangeImpl(String paramString, URI paramURI, Request paramRequest, long paramLong, HttpConnection paramHttpConnection)
/*     */     throws IOException
/*     */   {
/*  86 */     this.req = paramRequest;
/*  87 */     this.reqHdrs = paramRequest.headers();
/*  88 */     this.rspHdrs = new Headers();
/*  89 */     this.method = paramString;
/*  90 */     this.uri = paramURI;
/*  91 */     this.connection = paramHttpConnection;
/*  92 */     this.reqContentLen = paramLong;
/*     */ 
/*  94 */     this.ros = paramRequest.outputStream();
/*  95 */     this.ris = paramRequest.inputStream();
/*  96 */     this.server = getServerImpl();
/*  97 */     this.server.startExchange();
/*     */   }
/*     */ 
/*     */   public Headers getRequestHeaders() {
/* 101 */     return new UnmodifiableHeaders(this.reqHdrs);
/*     */   }
/*     */ 
/*     */   public Headers getResponseHeaders() {
/* 105 */     return this.rspHdrs;
/*     */   }
/*     */ 
/*     */   public URI getRequestURI() {
/* 109 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getRequestMethod() {
/* 113 */     return this.method;
/*     */   }
/*     */ 
/*     */   public HttpContextImpl getHttpContext() {
/* 117 */     return this.connection.getHttpContext();
/*     */   }
/*     */ 
/*     */   private boolean isHeadRequest() {
/* 121 */     return "HEAD".equals(getRequestMethod());
/*     */   }
/*     */ 
/*     */   public void close() {
/* 125 */     if (this.closed) {
/* 126 */       return;
/*     */     }
/* 128 */     this.closed = true;
/*     */     try
/*     */     {
/* 136 */       if ((this.uis_orig == null) || (this.uos == null)) {
/* 137 */         this.connection.close();
/* 138 */         return;
/*     */       }
/* 140 */       if (!this.uos_orig.isWrapped()) {
/* 141 */         this.connection.close();
/* 142 */         return;
/*     */       }
/* 144 */       if (!this.uis_orig.isClosed()) {
/* 145 */         this.uis_orig.close();
/*     */       }
/* 147 */       this.uos.close();
/*     */     } catch (IOException localIOException) {
/* 149 */       this.connection.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getRequestBody() {
/* 154 */     if (this.uis != null) {
/* 155 */       return this.uis;
/*     */     }
/* 157 */     if (this.reqContentLen == -1L) {
/* 158 */       this.uis_orig = new ChunkedInputStream(this, this.ris);
/* 159 */       this.uis = this.uis_orig;
/*     */     } else {
/* 161 */       this.uis_orig = new FixedLengthInputStream(this, this.ris, this.reqContentLen);
/* 162 */       this.uis = this.uis_orig;
/*     */     }
/* 164 */     return this.uis;
/*     */   }
/*     */ 
/*     */   LeftOverInputStream getOriginalInputStream() {
/* 168 */     return this.uis_orig;
/*     */   }
/*     */ 
/*     */   public int getResponseCode() {
/* 172 */     return this.rcode;
/*     */   }
/*     */ 
/*     */   public OutputStream getResponseBody()
/*     */   {
/* 183 */     if (this.uos == null) {
/* 184 */       this.uos_orig = new PlaceholderOutputStream(null);
/* 185 */       this.uos = this.uos_orig;
/*     */     }
/* 187 */     return this.uos;
/*     */   }
/*     */ 
/*     */   PlaceholderOutputStream getPlaceholderResponseBody()
/*     */   {
/* 196 */     getResponseBody();
/* 197 */     return this.uos_orig;
/*     */   }
/*     */ 
/*     */   public void sendResponseHeaders(int paramInt, long paramLong)
/*     */     throws IOException
/*     */   {
/* 203 */     if (this.sentHeaders) {
/* 204 */       throw new IOException("headers already sent");
/*     */     }
/* 206 */     this.rcode = paramInt;
/* 207 */     String str1 = "HTTP/1.1 " + paramInt + Code.msg(paramInt) + "\r\n";
/* 208 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(this.ros);
/* 209 */     PlaceholderOutputStream localPlaceholderOutputStream = getPlaceholderResponseBody();
/* 210 */     localBufferedOutputStream.write(bytes(str1, 0), 0, str1.length());
/* 211 */     int i = 0;
/* 212 */     this.rspHdrs.set("Date", ((DateFormat)dateFormat.get()).format(new Date()));
/*     */     Object localObject;
/*     */     String str2;
/* 216 */     if (((paramInt >= 100) && (paramInt < 200)) || (paramInt == 204) || (paramInt == 304))
/*     */     {
/* 220 */       if (paramLong != -1L) {
/* 221 */         localObject = this.server.getLogger();
/* 222 */         str2 = "sendResponseHeaders: rCode = " + paramInt + ": forcing contentLen = -1";
/*     */ 
/* 224 */         ((Logger)localObject).warning(str2);
/*     */       }
/* 226 */       paramLong = -1L;
/*     */     }
/*     */ 
/* 229 */     if (isHeadRequest())
/*     */     {
/* 233 */       if (paramLong >= 0L) {
/* 234 */         localObject = this.server.getLogger();
/* 235 */         str2 = "sendResponseHeaders: being invoked with a content length for a HEAD request";
/*     */ 
/* 237 */         ((Logger)localObject).warning(str2);
/*     */       }
/* 239 */       i = 1;
/* 240 */       paramLong = 0L;
/*     */     }
/* 242 */     else if (paramLong == 0L) {
/* 243 */       if (this.http10) {
/* 244 */         localPlaceholderOutputStream.setWrappedStream(new UndefLengthOutputStream(this, this.ros));
/* 245 */         this.close = true;
/*     */       } else {
/* 247 */         this.rspHdrs.set("Transfer-encoding", "chunked");
/* 248 */         localPlaceholderOutputStream.setWrappedStream(new ChunkedOutputStream(this, this.ros));
/*     */       }
/*     */     } else {
/* 251 */       if (paramLong == -1L) {
/* 252 */         i = 1;
/* 253 */         paramLong = 0L;
/*     */       }
/* 255 */       this.rspHdrs.set("Content-length", Long.toString(paramLong));
/* 256 */       localPlaceholderOutputStream.setWrappedStream(new FixedLengthOutputStream(this, this.ros, paramLong));
/*     */     }
/*     */ 
/* 259 */     write(this.rspHdrs, localBufferedOutputStream);
/* 260 */     this.rspContentLen = paramLong;
/* 261 */     localBufferedOutputStream.flush();
/* 262 */     localBufferedOutputStream = null;
/* 263 */     this.sentHeaders = true;
/* 264 */     if (i != 0) {
/* 265 */       localObject = new WriteFinishedEvent(this);
/* 266 */       this.server.addEvent((Event)localObject);
/* 267 */       this.closed = true;
/*     */     }
/* 269 */     this.server.logReply(paramInt, this.req.requestLine(), null);
/*     */   }
/*     */ 
/*     */   void write(Headers paramHeaders, OutputStream paramOutputStream) throws IOException {
/* 273 */     Set localSet = paramHeaders.entrySet();
/* 274 */     for (Map.Entry localEntry : localSet) {
/* 275 */       str1 = (String)localEntry.getKey();
/*     */ 
/* 277 */       List localList = (List)localEntry.getValue();
/* 278 */       for (String str2 : localList) {
/* 279 */         int i = str1.length();
/* 280 */         byte[] arrayOfByte = bytes(str1, 2);
/* 281 */         arrayOfByte[(i++)] = 58;
/* 282 */         arrayOfByte[(i++)] = 32;
/* 283 */         paramOutputStream.write(arrayOfByte, 0, i);
/* 284 */         arrayOfByte = bytes(str2, 2);
/* 285 */         i = str2.length();
/* 286 */         arrayOfByte[(i++)] = 13;
/* 287 */         arrayOfByte[(i++)] = 10;
/* 288 */         paramOutputStream.write(arrayOfByte, 0, i);
/*     */       }
/*     */     }
/*     */     String str1;
/* 291 */     paramOutputStream.write(13);
/* 292 */     paramOutputStream.write(10);
/*     */   }
/*     */ 
/*     */   private byte[] bytes(String paramString, int paramInt)
/*     */   {
/* 304 */     int i = paramString.length();
/* 305 */     if (i + paramInt > this.rspbuf.length) {
/* 306 */       int j = i + paramInt - this.rspbuf.length;
/* 307 */       this.rspbuf = new byte[2 * (this.rspbuf.length + j)];
/*     */     }
/* 309 */     char[] arrayOfChar = paramString.toCharArray();
/* 310 */     for (int k = 0; k < arrayOfChar.length; k++) {
/* 311 */       this.rspbuf[k] = ((byte)arrayOfChar[k]);
/*     */     }
/* 313 */     return this.rspbuf;
/*     */   }
/*     */ 
/*     */   public InetSocketAddress getRemoteAddress() {
/* 317 */     Socket localSocket = this.connection.getChannel().socket();
/* 318 */     InetAddress localInetAddress = localSocket.getInetAddress();
/* 319 */     int i = localSocket.getPort();
/* 320 */     return new InetSocketAddress(localInetAddress, i);
/*     */   }
/*     */ 
/*     */   public InetSocketAddress getLocalAddress() {
/* 324 */     Socket localSocket = this.connection.getChannel().socket();
/* 325 */     InetAddress localInetAddress = localSocket.getLocalAddress();
/* 326 */     int i = localSocket.getLocalPort();
/* 327 */     return new InetSocketAddress(localInetAddress, i);
/*     */   }
/*     */ 
/*     */   public String getProtocol() {
/* 331 */     String str = this.req.requestLine();
/* 332 */     int i = str.lastIndexOf(' ');
/* 333 */     return str.substring(i + 1);
/*     */   }
/*     */ 
/*     */   public SSLSession getSSLSession() {
/* 337 */     SSLEngine localSSLEngine = this.connection.getSSLEngine();
/* 338 */     if (localSSLEngine == null) {
/* 339 */       return null;
/*     */     }
/* 341 */     return localSSLEngine.getSession();
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString) {
/* 345 */     if (paramString == null) {
/* 346 */       throw new NullPointerException("null name parameter");
/*     */     }
/* 348 */     if (this.attributes == null) {
/* 349 */       this.attributes = getHttpContext().getAttributes();
/*     */     }
/* 351 */     return this.attributes.get(paramString);
/*     */   }
/*     */ 
/*     */   public void setAttribute(String paramString, Object paramObject) {
/* 355 */     if (paramString == null) {
/* 356 */       throw new NullPointerException("null name parameter");
/*     */     }
/* 358 */     if (this.attributes == null) {
/* 359 */       this.attributes = getHttpContext().getAttributes();
/*     */     }
/* 361 */     this.attributes.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void setStreams(InputStream paramInputStream, OutputStream paramOutputStream) {
/* 365 */     assert (this.uis != null);
/* 366 */     if (paramInputStream != null) {
/* 367 */       this.uis = paramInputStream;
/*     */     }
/* 369 */     if (paramOutputStream != null)
/* 370 */       this.uos = paramOutputStream;
/*     */   }
/*     */ 
/*     */   HttpConnection getConnection()
/*     */   {
/* 378 */     return this.connection;
/*     */   }
/*     */ 
/*     */   ServerImpl getServerImpl() {
/* 382 */     return getHttpContext().getServerImpl();
/*     */   }
/*     */ 
/*     */   public HttpPrincipal getPrincipal() {
/* 386 */     return this.principal;
/*     */   }
/*     */ 
/*     */   void setPrincipal(HttpPrincipal paramHttpPrincipal) {
/* 390 */     this.principal = paramHttpPrincipal;
/*     */   }
/*     */ 
/*     */   static ExchangeImpl get(HttpExchange paramHttpExchange) {
/* 394 */     if ((paramHttpExchange instanceof HttpExchangeImpl)) {
/* 395 */       return ((HttpExchangeImpl)paramHttpExchange).getExchangeImpl();
/*     */     }
/* 397 */     assert ((paramHttpExchange instanceof HttpsExchangeImpl));
/* 398 */     return ((HttpsExchangeImpl)paramHttpExchange).getExchangeImpl();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ExchangeImpl
 * JD-Core Version:    0.6.2
 */