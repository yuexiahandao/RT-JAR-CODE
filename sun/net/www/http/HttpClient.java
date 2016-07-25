/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.CacheRequest;
/*     */ import java.net.CookieHandler;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.Proxy.Type;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Locale;
/*     */ import sun.net.NetworkClient;
/*     */ import sun.net.ProgressSource;
/*     */ import sun.net.www.HeaderParser;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.net.www.MeteredStream;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.net.www.URLConnection;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ import sun.net.www.protocol.http.HttpURLConnection.TunnelState;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class HttpClient extends NetworkClient
/*     */ {
/*  47 */   protected boolean cachedHttpClient = false;
/*     */   protected boolean inCache;
/*     */   MessageHeader requests;
/*  55 */   PosterOutputStream poster = null;
/*     */   boolean streaming;
/*  61 */   boolean failedOnce = false;
/*     */ 
/*  64 */   private boolean ignoreContinue = true;
/*     */   private static final int HTTP_CONTINUE = 100;
/*     */   static final int httpPortNumber = 80;
/*     */   protected boolean proxyDisabled;
/*  87 */   public boolean usingProxy = false;
/*     */   protected String host;
/*     */   protected int port;
/*     */   protected static KeepAliveCache kac;
/*     */   private static boolean keepAliveProp;
/*     */   private static boolean retryPostProp;
/* 101 */   volatile boolean keepingAlive = false;
/* 102 */   int keepAliveConnections = -1;
/*     */ 
/* 112 */   int keepAliveTimeout = 0;
/*     */ 
/* 115 */   private CacheRequest cacheRequest = null;
/*     */   protected URL url;
/* 121 */   public boolean reuse = false;
/*     */ 
/* 124 */   private HttpCapture capture = null;
/*     */   private static final PlatformLogger logger;
/*     */ 
/*     */   protected int getDefaultPort()
/*     */   {
/*  71 */     return 80;
/*     */   }
/*     */   private static int getDefaultPort(String paramString) {
/*  74 */     if ("http".equalsIgnoreCase(paramString))
/*  75 */       return 80;
/*  76 */     if ("https".equalsIgnoreCase(paramString))
/*  77 */       return 443;
/*  78 */     return -1;
/*     */   }
/*     */ 
/*     */   private static void logFinest(String paramString)
/*     */   {
/* 128 */     if (logger.isLoggable(300))
/* 129 */       logger.finest(paramString);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static synchronized void resetProperties()
/*     */   {
/*     */   }
/*     */ 
/*     */   int getKeepAliveTimeout()
/*     */   {
/* 142 */     return this.keepAliveTimeout;
/*     */   }
/*     */ 
/*     */   public boolean getHttpKeepAliveSet()
/*     */   {
/* 170 */     return keepAliveProp;
/*     */   }
/*     */ 
/*     */   protected HttpClient()
/*     */   {
/*     */   }
/*     */ 
/*     */   private HttpClient(URL paramURL) throws IOException
/*     */   {
/* 179 */     this(paramURL, (String)null, -1, false);
/*     */   }
/*     */ 
/*     */   protected HttpClient(URL paramURL, boolean paramBoolean) throws IOException
/*     */   {
/* 184 */     this(paramURL, null, -1, paramBoolean);
/*     */   }
/*     */ 
/*     */   public HttpClient(URL paramURL, String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 197 */     this(paramURL, paramString, paramInt, false);
/*     */   }
/*     */ 
/*     */   protected HttpClient(URL paramURL, Proxy paramProxy, int paramInt) throws IOException {
/* 201 */     this.proxy = (paramProxy == null ? Proxy.NO_PROXY : paramProxy);
/* 202 */     this.host = paramURL.getHost();
/* 203 */     this.url = paramURL;
/* 204 */     this.port = paramURL.getPort();
/* 205 */     if (this.port == -1) {
/* 206 */       this.port = getDefaultPort();
/*     */     }
/* 208 */     setConnectTimeout(paramInt);
/*     */ 
/* 210 */     this.capture = HttpCapture.getCapture(paramURL);
/* 211 */     openServer();
/*     */   }
/*     */ 
/*     */   protected static Proxy newHttpProxy(String paramString1, int paramInt, String paramString2)
/*     */   {
/* 216 */     if ((paramString1 == null) || (paramString2 == null))
/* 217 */       return Proxy.NO_PROXY;
/* 218 */     int i = paramInt < 0 ? getDefaultPort(paramString2) : paramInt;
/* 219 */     InetSocketAddress localInetSocketAddress = InetSocketAddress.createUnresolved(paramString1, i);
/* 220 */     return new Proxy(Proxy.Type.HTTP, localInetSocketAddress);
/*     */   }
/*     */ 
/*     */   private HttpClient(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 238 */     this(paramURL, paramBoolean ? Proxy.NO_PROXY : newHttpProxy(paramString, paramInt, "http"), -1);
/*     */   }
/*     */ 
/*     */   public HttpClient(URL paramURL, String paramString, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 245 */     this(paramURL, paramBoolean ? Proxy.NO_PROXY : newHttpProxy(paramString, paramInt1, "http"), paramInt2);
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 255 */     return New(paramURL, Proxy.NO_PROXY, -1, true, null);
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL, boolean paramBoolean) throws IOException
/*     */   {
/* 260 */     return New(paramURL, Proxy.NO_PROXY, -1, paramBoolean, null);
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL, Proxy paramProxy, int paramInt, boolean paramBoolean, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 266 */     if (paramProxy == null) {
/* 267 */       paramProxy = Proxy.NO_PROXY;
/*     */     }
/* 269 */     HttpClient localHttpClient = null;
/*     */ 
/* 271 */     if (paramBoolean) {
/* 272 */       localHttpClient = kac.get(paramURL, null);
/* 273 */       if ((localHttpClient != null) && (paramHttpURLConnection != null) && (paramHttpURLConnection.streaming()) && (paramHttpURLConnection.getRequestMethod() == "POST"))
/*     */       {
/* 276 */         if (!localHttpClient.available()) {
/* 277 */           localHttpClient.inCache = false;
/* 278 */           localHttpClient.closeServer();
/* 279 */           localHttpClient = null;
/*     */         }
/*     */       }
/*     */ 
/* 283 */       if (localHttpClient != null) {
/* 284 */         if (((localHttpClient.proxy != null) && (localHttpClient.proxy.equals(paramProxy))) || ((localHttpClient.proxy == null) && (paramProxy == null)))
/*     */         {
/* 286 */           synchronized (localHttpClient) {
/* 287 */             localHttpClient.cachedHttpClient = true;
/* 288 */             assert (localHttpClient.inCache);
/* 289 */             localHttpClient.inCache = false;
/* 290 */             if ((paramHttpURLConnection != null) && (localHttpClient.needsTunneling()))
/* 291 */               paramHttpURLConnection.setTunnelState(HttpURLConnection.TunnelState.TUNNELING);
/* 292 */             logFinest("KeepAlive stream retrieved from the cache, " + localHttpClient);
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 299 */           synchronized (localHttpClient) {
/* 300 */             localHttpClient.inCache = false;
/* 301 */             localHttpClient.closeServer();
/*     */           }
/* 303 */           localHttpClient = null;
/*     */         }
/*     */       }
/*     */     }
/* 307 */     if (localHttpClient == null) {
/* 308 */       localHttpClient = new HttpClient(paramURL, paramProxy, paramInt);
/*     */     } else {
/* 310 */       ??? = System.getSecurityManager();
/* 311 */       if (??? != null) {
/* 312 */         if ((localHttpClient.proxy == Proxy.NO_PROXY) || (localHttpClient.proxy == null))
/* 313 */           ((SecurityManager)???).checkConnect(InetAddress.getByName(paramURL.getHost()).getHostAddress(), paramURL.getPort());
/*     */         else {
/* 315 */           ((SecurityManager)???).checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */         }
/*     */       }
/* 318 */       localHttpClient.url = paramURL;
/*     */     }
/* 320 */     return localHttpClient;
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL, Proxy paramProxy, int paramInt, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 326 */     return New(paramURL, paramProxy, paramInt, true, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 332 */     return New(paramURL, newHttpProxy(paramString, paramInt, "http"), -1, paramBoolean, null);
/*     */   }
/*     */ 
/*     */   public static HttpClient New(URL paramURL, String paramString, int paramInt1, boolean paramBoolean, int paramInt2, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 340 */     return New(paramURL, newHttpProxy(paramString, paramInt1, "http"), paramInt2, paramBoolean, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   public void finished()
/*     */   {
/* 355 */     if (this.reuse)
/* 356 */       return;
/* 357 */     this.keepAliveConnections -= 1;
/* 358 */     this.poster = null;
/* 359 */     if ((this.keepAliveConnections > 0) && (isKeepingAlive()) && (!this.serverOutput.checkError()))
/*     */     {
/* 364 */       putInKeepAliveCache();
/*     */     }
/* 366 */     else closeServer();
/*     */   }
/*     */ 
/*     */   protected synchronized boolean available()
/*     */   {
/* 371 */     boolean bool = true;
/* 372 */     int i = -1;
/*     */     try
/*     */     {
/*     */       try {
/* 376 */         i = this.serverSocket.getSoTimeout();
/* 377 */         this.serverSocket.setSoTimeout(1);
/* 378 */         BufferedInputStream localBufferedInputStream = new BufferedInputStream(this.serverSocket.getInputStream());
/*     */ 
/* 380 */         int j = localBufferedInputStream.read();
/* 381 */         if (j == -1) {
/* 382 */           logFinest("HttpClient.available(): read returned -1: not available");
/*     */ 
/* 384 */           bool = false;
/*     */         }
/*     */ 
/* 390 */         if (i != -1)
/* 391 */           this.serverSocket.setSoTimeout(i);
/*     */       }
/*     */       catch (SocketTimeoutException localSocketTimeoutException)
/*     */       {
/* 387 */         logFinest("HttpClient.available(): SocketTimeout: its available");
/*     */ 
/* 390 */         if (i != -1)
/* 391 */           this.serverSocket.setSoTimeout(i);
/*     */       }
/*     */       finally
/*     */       {
/* 390 */         if (i != -1)
/* 391 */           this.serverSocket.setSoTimeout(i);
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 394 */       logFinest("HttpClient.available(): SocketException: not available");
/*     */ 
/* 396 */       bool = false;
/*     */     }
/* 398 */     return bool;
/*     */   }
/*     */ 
/*     */   protected synchronized void putInKeepAliveCache() {
/* 402 */     if (this.inCache) {
/* 403 */       if (!$assertionsDisabled) throw new AssertionError("Duplicate put to keep alive cache");
/* 404 */       return;
/*     */     }
/* 406 */     this.inCache = true;
/* 407 */     kac.put(this.url, null, this);
/*     */   }
/*     */ 
/*     */   protected synchronized boolean isInKeepAliveCache() {
/* 411 */     return this.inCache;
/*     */   }
/*     */ 
/*     */   public void closeIdleConnection()
/*     */   {
/* 419 */     HttpClient localHttpClient = kac.get(this.url, null);
/* 420 */     if (localHttpClient != null)
/* 421 */       localHttpClient.closeServer();
/*     */   }
/*     */ 
/*     */   public void openServer(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 432 */     this.serverSocket = doConnect(paramString, paramInt);
/*     */     try {
/* 434 */       Object localObject = this.serverSocket.getOutputStream();
/* 435 */       if (this.capture != null) {
/* 436 */         localObject = new HttpCaptureOutputStream((OutputStream)localObject, this.capture);
/*     */       }
/* 438 */       this.serverOutput = new PrintStream(new BufferedOutputStream((OutputStream)localObject), false, encoding);
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */     {
/* 442 */       throw new InternalError(encoding + " encoding not found");
/*     */     }
/* 444 */     this.serverSocket.setTcpNoDelay(true);
/*     */   }
/*     */ 
/*     */   public boolean needsTunneling()
/*     */   {
/* 452 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isCachedConnection()
/*     */   {
/* 459 */     return this.cachedHttpClient;
/*     */   }
/*     */ 
/*     */   public void afterConnect()
/*     */     throws IOException, UnknownHostException
/*     */   {
/*     */   }
/*     */ 
/*     */   private synchronized void privilegedOpenServer(final InetSocketAddress paramInetSocketAddress)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 481 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/* 484 */           HttpClient.this.openServer(paramInetSocketAddress.getHostString(), paramInetSocketAddress.getPort());
/* 485 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 489 */       throw ((IOException)localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void superOpenServer(String paramString, int paramInt)
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 500 */     super.openServer(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected synchronized void openServer()
/*     */     throws IOException
/*     */   {
/* 507 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */ 
/* 509 */     if (localSecurityManager != null) {
/* 510 */       localSecurityManager.checkConnect(this.host, this.port);
/*     */     }
/*     */ 
/* 513 */     if (this.keepingAlive) {
/* 514 */       return;
/*     */     }
/*     */ 
/* 517 */     if ((this.url.getProtocol().equals("http")) || (this.url.getProtocol().equals("https")))
/*     */     {
/* 520 */       if ((this.proxy != null) && (this.proxy.type() == Proxy.Type.HTTP)) {
/* 521 */         URLConnection.setProxiedHost(this.host);
/* 522 */         privilegedOpenServer((InetSocketAddress)this.proxy.address());
/* 523 */         this.usingProxy = true;
/* 524 */         return;
/*     */       }
/*     */ 
/* 527 */       openServer(this.host, this.port);
/* 528 */       this.usingProxy = false;
/* 529 */       return;
/*     */     }
/*     */ 
/* 536 */     if ((this.proxy != null) && (this.proxy.type() == Proxy.Type.HTTP)) {
/* 537 */       URLConnection.setProxiedHost(this.host);
/* 538 */       privilegedOpenServer((InetSocketAddress)this.proxy.address());
/* 539 */       this.usingProxy = true;
/* 540 */       return;
/*     */     }
/*     */ 
/* 543 */     super.openServer(this.host, this.port);
/* 544 */     this.usingProxy = false;
/*     */   }
/*     */ 
/*     */   public String getURLFile()
/*     */     throws IOException
/*     */   {
/* 552 */     String str = this.url.getFile();
/* 553 */     if ((str == null) || (str.length() == 0)) {
/* 554 */       str = "/";
/*     */     }
/*     */ 
/* 559 */     if ((this.usingProxy) && (!this.proxyDisabled))
/*     */     {
/* 563 */       StringBuffer localStringBuffer = new StringBuffer(128);
/* 564 */       localStringBuffer.append(this.url.getProtocol());
/* 565 */       localStringBuffer.append(":");
/* 566 */       if ((this.url.getAuthority() != null) && (this.url.getAuthority().length() > 0)) {
/* 567 */         localStringBuffer.append("//");
/* 568 */         localStringBuffer.append(this.url.getAuthority());
/*     */       }
/* 570 */       if (this.url.getPath() != null) {
/* 571 */         localStringBuffer.append(this.url.getPath());
/*     */       }
/* 573 */       if (this.url.getQuery() != null) {
/* 574 */         localStringBuffer.append('?');
/* 575 */         localStringBuffer.append(this.url.getQuery());
/*     */       }
/*     */ 
/* 578 */       str = localStringBuffer.toString();
/*     */     }
/* 580 */     if (str.indexOf('\n') == -1) {
/* 581 */       return str;
/*     */     }
/* 583 */     throw new MalformedURLException("Illegal character in URL");
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void writeRequests(MessageHeader paramMessageHeader)
/*     */   {
/* 591 */     this.requests = paramMessageHeader;
/* 592 */     this.requests.print(this.serverOutput);
/* 593 */     this.serverOutput.flush();
/*     */   }
/*     */ 
/*     */   public void writeRequests(MessageHeader paramMessageHeader, PosterOutputStream paramPosterOutputStream) throws IOException
/*     */   {
/* 598 */     this.requests = paramMessageHeader;
/* 599 */     this.requests.print(this.serverOutput);
/* 600 */     this.poster = paramPosterOutputStream;
/* 601 */     if (this.poster != null)
/* 602 */       this.poster.writeTo(this.serverOutput);
/* 603 */     this.serverOutput.flush();
/*     */   }
/*     */ 
/*     */   public void writeRequests(MessageHeader paramMessageHeader, PosterOutputStream paramPosterOutputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 609 */     this.streaming = paramBoolean;
/* 610 */     writeRequests(paramMessageHeader, paramPosterOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean parseHTTP(MessageHeader paramMessageHeader, ProgressSource paramProgressSource, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 628 */       this.serverInput = this.serverSocket.getInputStream();
/* 629 */       if (this.capture != null) {
/* 630 */         this.serverInput = new HttpCaptureInputStream(this.serverInput, this.capture);
/*     */       }
/* 632 */       this.serverInput = new BufferedInputStream(this.serverInput);
/* 633 */       return parseHTTPHeader(paramMessageHeader, paramProgressSource, paramHttpURLConnection);
/*     */     }
/*     */     catch (SocketTimeoutException localSocketTimeoutException)
/*     */     {
/* 637 */       if (this.ignoreContinue) {
/* 638 */         closeServer();
/*     */       }
/* 640 */       throw localSocketTimeoutException;
/*     */     } catch (IOException localIOException) {
/* 642 */       closeServer();
/* 643 */       this.cachedHttpClient = false;
/* 644 */       if ((!this.failedOnce) && (this.requests != null)) {
/* 645 */         this.failedOnce = true;
/* 646 */         if ((!getRequestMethod().equals("CONNECT")) && ((!paramHttpURLConnection.getRequestMethod().equals("POST")) || ((retryPostProp) && (!this.streaming))))
/*     */         {
/* 652 */           openServer();
/* 653 */           if (needsTunneling()) {
/* 654 */             MessageHeader localMessageHeader = this.requests;
/* 655 */             paramHttpURLConnection.doTunneling();
/* 656 */             this.requests = localMessageHeader;
/*     */           }
/* 658 */           afterConnect();
/* 659 */           writeRequests(this.requests, this.poster);
/* 660 */           return parseHTTP(paramMessageHeader, paramProgressSource, paramHttpURLConnection);
/*     */         }
/*     */       }
/* 663 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean parseHTTPHeader(MessageHeader paramMessageHeader, ProgressSource paramProgressSource, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 679 */     this.keepAliveConnections = -1;
/* 680 */     this.keepAliveTimeout = 0;
/*     */ 
/* 682 */     boolean bool = false;
/* 683 */     byte[] arrayOfByte = new byte[8];
/*     */     String str1;
/*     */     try
/*     */     {
/* 686 */       int i = 0;
/* 687 */       this.serverInput.mark(10);
/* 688 */       while (i < 8) {
/* 689 */         int k = this.serverInput.read(arrayOfByte, i, 8 - i);
/* 690 */         if (k < 0) {
/*     */           break;
/*     */         }
/* 693 */         i += k;
/*     */       }
/* 695 */       str1 = null;
/* 696 */       bool = (arrayOfByte[0] == 72) && (arrayOfByte[1] == 84) && (arrayOfByte[2] == 84) && (arrayOfByte[3] == 80) && (arrayOfByte[4] == 47) && (arrayOfByte[5] == 49) && (arrayOfByte[6] == 46);
/*     */ 
/* 699 */       this.serverInput.reset();
/*     */       Object localObject1;
/* 700 */       if (bool) {
/* 701 */         paramMessageHeader.parseHeader(this.serverInput);
/*     */ 
/* 705 */         localObject1 = paramHttpURLConnection.getCookieHandler();
/* 706 */         if (localObject1 != null) {
/* 707 */           localObject2 = ParseUtil.toURI(this.url);
/*     */ 
/* 712 */           if (localObject2 != null) {
/* 713 */             ((CookieHandler)localObject1).put((URI)localObject2, paramMessageHeader.getHeaders());
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 722 */         if (this.usingProxy) {
/* 723 */           str1 = paramMessageHeader.findValue("Proxy-Connection");
/*     */         }
/* 725 */         if (str1 == null) {
/* 726 */           str1 = paramMessageHeader.findValue("Connection");
/*     */         }
/* 728 */         if ((str1 != null) && (str1.toLowerCase(Locale.US).equals("keep-alive")))
/*     */         {
/* 732 */           localObject2 = new HeaderParser(paramMessageHeader.findValue("Keep-Alive"));
/*     */ 
/* 734 */           if (localObject2 != null)
/*     */           {
/* 736 */             this.keepAliveConnections = ((HeaderParser)localObject2).findInt("max", this.usingProxy ? 50 : 5);
/* 737 */             this.keepAliveTimeout = ((HeaderParser)localObject2).findInt("timeout", this.usingProxy ? 60 : 5);
/*     */           }
/* 739 */         } else if (arrayOfByte[7] != 48)
/*     */         {
/* 744 */           if (str1 != null)
/*     */           {
/* 750 */             this.keepAliveConnections = 1;
/*     */           }
/* 752 */           else this.keepAliveConnections = 5; 
/*     */         }
/*     */       }
/* 755 */       else { if (i != 8) {
/* 756 */           if ((!this.failedOnce) && (this.requests != null)) {
/* 757 */             this.failedOnce = true;
/* 758 */             if ((!getRequestMethod().equals("CONNECT")) && ((!paramHttpURLConnection.getRequestMethod().equals("POST")) || ((retryPostProp) && (!this.streaming))))
/*     */             {
/* 763 */               closeServer();
/* 764 */               this.cachedHttpClient = false;
/* 765 */               openServer();
/* 766 */               if (needsTunneling()) {
/* 767 */                 localObject1 = this.requests;
/* 768 */                 paramHttpURLConnection.doTunneling();
/* 769 */                 this.requests = ((MessageHeader)localObject1);
/*     */               }
/* 771 */               afterConnect();
/* 772 */               writeRequests(this.requests, this.poster);
/* 773 */               return parseHTTP(paramMessageHeader, paramProgressSource, paramHttpURLConnection);
/*     */             }
/*     */           }
/* 776 */           throw new SocketException("Unexpected end of file from server");
/*     */         }
/*     */ 
/* 779 */         paramMessageHeader.set("Content-type", "unknown/unknown"); }
/*     */     }
/*     */     catch (IOException localIOException) {
/* 782 */       throw localIOException;
/*     */     }
/*     */ 
/* 785 */     int j = -1;
/*     */     try
/*     */     {
/* 788 */       str1 = paramMessageHeader.getValue(0);
/*     */ 
/* 794 */       int m = str1.indexOf(' ');
/* 795 */       while (str1.charAt(m) == ' ')
/* 796 */         m++;
/* 797 */       j = Integer.parseInt(str1.substring(m, m + 3));
/*     */     } catch (Exception localException) {
/*     */     }
/* 800 */     if ((j == 100) && (this.ignoreContinue)) {
/* 801 */       paramMessageHeader.reset();
/* 802 */       return parseHTTPHeader(paramMessageHeader, paramProgressSource, paramHttpURLConnection);
/*     */     }
/*     */ 
/* 805 */     long l = -1L;
/*     */ 
/* 813 */     Object localObject2 = paramMessageHeader.findValue("Transfer-Encoding");
/* 814 */     if ((localObject2 != null) && (((String)localObject2).equalsIgnoreCase("chunked"))) {
/* 815 */       this.serverInput = new ChunkedInputStream(this.serverInput, this, paramMessageHeader);
/*     */ 
/* 821 */       if (this.keepAliveConnections <= 1) {
/* 822 */         this.keepAliveConnections = 1;
/* 823 */         this.keepingAlive = false;
/*     */       } else {
/* 825 */         this.keepingAlive = true;
/*     */       }
/* 827 */       this.failedOnce = false;
/*     */     }
/*     */     else
/*     */     {
/* 837 */       String str2 = paramMessageHeader.findValue("content-length");
/* 838 */       if (str2 != null) {
/*     */         try {
/* 840 */           l = Long.parseLong(str2);
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 842 */           l = -1L;
/*     */         }
/*     */       }
/* 845 */       String str3 = this.requests.getKey(0);
/*     */ 
/* 847 */       if (((str3 != null) && (str3.startsWith("HEAD"))) || (j == 304) || (j == 204))
/*     */       {
/* 851 */         l = 0L;
/*     */       }
/*     */ 
/* 854 */       if ((this.keepAliveConnections > 1) && ((l >= 0L) || (j == 304) || (j == 204)))
/*     */       {
/* 858 */         this.keepingAlive = true;
/* 859 */         this.failedOnce = false;
/* 860 */       } else if (this.keepingAlive)
/*     */       {
/* 865 */         this.keepingAlive = false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 871 */     if (l > 0L)
/*     */     {
/* 875 */       if (paramProgressSource != null)
/*     */       {
/* 877 */         paramProgressSource.setContentType(paramMessageHeader.findValue("content-type"));
/*     */       }
/*     */ 
/* 880 */       if (isKeepingAlive())
/*     */       {
/* 882 */         logFinest("KeepAlive stream used: " + this.url);
/* 883 */         this.serverInput = new KeepAliveStream(this.serverInput, paramProgressSource, l, this);
/* 884 */         this.failedOnce = false;
/*     */       }
/*     */       else {
/* 887 */         this.serverInput = new MeteredStream(this.serverInput, paramProgressSource, l);
/*     */       }
/*     */     }
/* 890 */     else if (l == -1L)
/*     */     {
/* 895 */       if (paramProgressSource != null)
/*     */       {
/* 898 */         paramProgressSource.setContentType(paramMessageHeader.findValue("content-type"));
/*     */ 
/* 902 */         this.serverInput = new MeteredStream(this.serverInput, paramProgressSource, l);
/*     */       }
/*     */ 
/*     */     }
/* 912 */     else if (paramProgressSource != null) {
/* 913 */       paramProgressSource.finishTracking();
/*     */     }
/*     */ 
/* 916 */     return bool;
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream() {
/* 920 */     return this.serverInput;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream() {
/* 924 */     return this.serverOutput;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 929 */     return getClass().getName() + "(" + this.url + ")";
/*     */   }
/*     */ 
/*     */   public final boolean isKeepingAlive() {
/* 933 */     return (getHttpKeepAliveSet()) && (this.keepingAlive);
/*     */   }
/*     */ 
/*     */   public void setCacheRequest(CacheRequest paramCacheRequest) {
/* 937 */     this.cacheRequest = paramCacheRequest;
/*     */   }
/*     */ 
/*     */   CacheRequest getCacheRequest() {
/* 941 */     return this.cacheRequest;
/*     */   }
/*     */ 
/*     */   String getRequestMethod() {
/* 945 */     if (this.requests != null) {
/* 946 */       String str = this.requests.getKey(0);
/* 947 */       if (str != null) {
/* 948 */         return str.split("\\s+")[0];
/*     */       }
/*     */     }
/* 951 */     return "";
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDoNotRetry(boolean paramBoolean)
/*     */   {
/* 962 */     this.failedOnce = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setIgnoreContinue(boolean paramBoolean) {
/* 966 */     this.ignoreContinue = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void closeServer()
/*     */   {
/*     */     try
/*     */     {
/* 973 */       this.keepingAlive = false;
/* 974 */       this.serverSocket.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getProxyHostUsed()
/*     */   {
/* 983 */     if (!this.usingProxy) {
/* 984 */       return null;
/*     */     }
/* 986 */     return ((InetSocketAddress)this.proxy.address()).getHostString();
/*     */   }
/*     */ 
/*     */   public int getProxyPortUsed()
/*     */   {
/* 995 */     if (this.usingProxy)
/* 996 */       return ((InetSocketAddress)this.proxy.address()).getPort();
/* 997 */     return -1;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  93 */     kac = new KeepAliveCache();
/*     */ 
/*  95 */     keepAliveProp = true;
/*     */ 
/*  99 */     retryPostProp = true;
/*     */ 
/* 126 */     logger = HttpURLConnection.getHttpLogger();
/*     */ 
/* 146 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("http.keepAlive"));
/*     */ 
/* 149 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.net.http.retryPost"));
/*     */ 
/* 152 */     if (str1 != null)
/* 153 */       keepAliveProp = Boolean.valueOf(str1).booleanValue();
/*     */     else {
/* 155 */       keepAliveProp = true;
/*     */     }
/*     */ 
/* 158 */     if (str2 != null)
/* 159 */       retryPostProp = Boolean.valueOf(str2).booleanValue();
/*     */     else
/* 161 */       retryPostProp = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.HttpClient
 * JD-Core Version:    0.6.2
 */