/*      */ package sun.net.www.protocol.http;
/*      */ 
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.net.Authenticator;
/*      */ import java.net.Authenticator.RequestorType;
/*      */ import java.net.CacheRequest;
/*      */ import java.net.CacheResponse;
/*      */ import java.net.CookieHandler;
/*      */ import java.net.HttpCookie;
/*      */ import java.net.HttpRetryException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.PasswordAuthentication;
/*      */ import java.net.ProtocolException;
/*      */ import java.net.Proxy;
/*      */ import java.net.Proxy.Type;
/*      */ import java.net.ProxySelector;
/*      */ import java.net.ResponseCache;
/*      */ import java.net.SecureCacheResponse;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import sun.misc.JavaNetHttpCookieAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.net.ApplicationProxy;
/*      */ import sun.net.ProgressMonitor;
/*      */ import sun.net.ProgressSource;
/*      */ import sun.net.www.HeaderParser;
/*      */ import sun.net.www.MessageHeader;
/*      */ import sun.net.www.MeteredStream;
/*      */ import sun.net.www.ParseUtil;
/*      */ import sun.net.www.http.ChunkedInputStream;
/*      */ import sun.net.www.http.ChunkedOutputStream;
/*      */ import sun.net.www.http.HttpClient;
/*      */ import sun.net.www.http.PosterOutputStream;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ import sun.security.action.GetIntegerAction;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class HttpURLConnection extends java.net.HttpURLConnection
/*      */ {
/*   85 */   static String HTTP_CONNECT = "CONNECT";
/*      */   static final String version;
/*      */   public static final String userAgent;
/*      */   static final int defaultmaxRedirects = 20;
/*      */   static final int maxRedirects;
/*      */   static final boolean validateProxy;
/*      */   static final boolean validateServer;
/*      */   private StreamingOutputStream strOutputStream;
/*      */   private static final String RETRY_MSG1 = "cannot retry due to proxy authentication, in streaming mode";
/*      */   private static final String RETRY_MSG2 = "cannot retry due to server authentication, in streaming mode";
/*      */   private static final String RETRY_MSG3 = "cannot retry due to redirection, in streaming mode";
/*  139 */   private static boolean enableESBuffer = false;
/*      */ 
/*  143 */   private static int timeout4ESBuffer = 0;
/*      */ 
/*  147 */   private static int bufSize4ES = 0;
/*      */   private static final boolean allowRestrictedHeaders;
/*      */   private static final Set<String> restrictedHeaderSet;
/*  172 */   private static final String[] restrictedHeaders = { "Access-Control-Request-Headers", "Access-Control-Request-Method", "Connection", "Content-Length", "Content-Transfer-Encoding", "Host", "Keep-Alive", "Origin", "Trailer", "Transfer-Encoding", "Upgrade", "Via" };
/*      */   static final String httpVersion = "HTTP/1.1";
/*      */   static final String acceptString = "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
/*  254 */   private static final String[] EXCLUDE_HEADERS = { "Proxy-Authorization", "Authorization" };
/*      */ 
/*  260 */   private static final String[] EXCLUDE_HEADERS2 = { "Proxy-Authorization", "Authorization", "Cookie", "Cookie2" };
/*      */   protected HttpClient http;
/*      */   protected Handler handler;
/*      */   protected Proxy instProxy;
/*      */   private CookieHandler cookieHandler;
/*      */   private ResponseCache cacheHandler;
/*      */   protected CacheResponse cachedResponse;
/*      */   private MessageHeader cachedHeaders;
/*      */   private InputStream cachedInputStream;
/*  280 */   protected PrintStream ps = null;
/*      */ 
/*  284 */   private InputStream errorStream = null;
/*      */ 
/*  287 */   private boolean setUserCookies = true;
/*  288 */   private String userCookies = null;
/*  289 */   private String userCookies2 = null;
/*      */   private static HttpAuthenticator defaultAuth;
/*      */   private MessageHeader requests;
/*      */   String domain;
/*      */   DigestAuthentication.Parameters digestparams;
/*  309 */   AuthenticationInfo currentProxyCredentials = null;
/*  310 */   AuthenticationInfo currentServerCredentials = null;
/*  311 */   boolean needToCheck = true;
/*  312 */   private boolean doingNTLM2ndStage = false;
/*  313 */   private boolean doingNTLMp2ndStage = false;
/*      */ 
/*  316 */   private boolean tryTransparentNTLMServer = true;
/*  317 */   private boolean tryTransparentNTLMProxy = true;
/*  318 */   private boolean useProxyResponseCode = false;
/*      */   private Object authObj;
/*      */   boolean isUserServerAuth;
/*      */   boolean isUserProxyAuth;
/*      */   String serverAuthKey;
/*      */   String proxyAuthKey;
/*      */   protected ProgressSource pi;
/*      */   private MessageHeader responses;
/*  335 */   private InputStream inputStream = null;
/*      */ 
/*  337 */   private PosterOutputStream poster = null;
/*      */ 
/*  340 */   private boolean setRequests = false;
/*      */ 
/*  343 */   private boolean failedOnce = false;
/*      */ 
/*  347 */   private Exception rememberedException = null;
/*      */ 
/*  350 */   private HttpClient reuseClient = null;
/*      */ 
/*  364 */   private TunnelState tunnelState = TunnelState.NONE;
/*      */ 
/*  369 */   private int connectTimeout = -1;
/*  370 */   private int readTimeout = -1;
/*      */ 
/*  373 */   private static final PlatformLogger logger = PlatformLogger.getLogger("sun.net.www.protocol.http.HttpURLConnection");
/*      */ 
/* 2303 */   String requestURI = null;
/*      */ 
/* 2437 */   byte[] cdata = new byte[''];
/*      */   private static final String SET_COOKIE = "set-cookie";
/*      */   private static final String SET_COOKIE2 = "set-cookie2";
/*      */   private Map<String, List<String>> filteredHeaders;
/*      */ 
/*      */   private static PasswordAuthentication privilegedRequestPasswordAuthentication(String paramString1, final InetAddress paramInetAddress, final int paramInt, final String paramString2, final String paramString3, final String paramString4, final URL paramURL, final Authenticator.RequestorType paramRequestorType)
/*      */   {
/*  390 */     return (PasswordAuthentication)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public PasswordAuthentication run() {
/*  393 */         if (HttpURLConnection.logger.isLoggable(300)) {
/*  394 */           HttpURLConnection.logger.finest("Requesting Authentication: host =" + this.val$host + " url = " + paramURL);
/*      */         }
/*  396 */         PasswordAuthentication localPasswordAuthentication = Authenticator.requestPasswordAuthentication(this.val$host, paramInetAddress, paramInt, paramString2, paramString3, paramString4, paramURL, paramRequestorType);
/*      */ 
/*  399 */         if (HttpURLConnection.logger.isLoggable(300)) {
/*  400 */           HttpURLConnection.logger.finest("Authentication returned: " + (localPasswordAuthentication != null ? localPasswordAuthentication.toString() : "null"));
/*      */         }
/*  402 */         return localPasswordAuthentication;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private boolean isRestrictedHeader(String paramString1, String paramString2) {
/*  408 */     if (allowRestrictedHeaders) {
/*  409 */       return false;
/*      */     }
/*      */ 
/*  412 */     paramString1 = paramString1.toLowerCase();
/*  413 */     if (restrictedHeaderSet.contains(paramString1))
/*      */     {
/*  419 */       if ((paramString1.equals("connection")) && (paramString2.equalsIgnoreCase("close"))) {
/*  420 */         return false;
/*      */       }
/*  422 */       return true;
/*  423 */     }if (paramString1.startsWith("sec-")) {
/*  424 */       return true;
/*      */     }
/*  426 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean isExternalMessageHeaderAllowed(String paramString1, String paramString2)
/*      */   {
/*  435 */     checkMessageHeader(paramString1, paramString2);
/*  436 */     if (!isRestrictedHeader(paramString1, paramString2)) {
/*  437 */       return true;
/*      */     }
/*  439 */     return false;
/*      */   }
/*      */ 
/*      */   public static PlatformLogger getHttpLogger()
/*      */   {
/*  444 */     return logger;
/*      */   }
/*      */ 
/*      */   public Object authObj()
/*      */   {
/*  449 */     return this.authObj;
/*      */   }
/*      */ 
/*      */   public void authObj(Object paramObject) {
/*  453 */     this.authObj = paramObject;
/*      */   }
/*      */ 
/*      */   private void checkMessageHeader(String paramString1, String paramString2)
/*      */   {
/*  461 */     int i = 10;
/*  462 */     int j = paramString1.indexOf(i);
/*  463 */     if (j != -1) {
/*  464 */       throw new IllegalArgumentException("Illegal character(s) in message header field: " + paramString1);
/*      */     }
/*      */ 
/*  468 */     if (paramString2 == null) {
/*  469 */       return;
/*      */     }
/*      */ 
/*  472 */     j = paramString2.indexOf(i);
/*  473 */     while (j != -1) {
/*  474 */       j++;
/*  475 */       if (j < paramString2.length()) {
/*  476 */         int k = paramString2.charAt(j);
/*  477 */         if ((k == 32) || (k == 9))
/*      */         {
/*  479 */           j = paramString2.indexOf(i, j);
/*      */         }
/*      */       }
/*      */       else {
/*  483 */         throw new IllegalArgumentException("Illegal character(s) in message header value: " + paramString2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeRequests()
/*      */     throws IOException
/*      */   {
/*  498 */     if ((this.http.usingProxy) && (tunnelState() != TunnelState.TUNNELING)) {
/*  499 */       setPreemptiveProxyAuthentication(this.requests);
/*      */     }
/*  501 */     if (!this.setRequests)
/*      */     {
/*  513 */       if (!this.failedOnce) {
/*  514 */         this.requests.prepend(this.method + " " + getRequestURI() + " " + "HTTP/1.1", null);
/*      */       }
/*  516 */       if (!getUseCaches()) {
/*  517 */         this.requests.setIfNotSet("Cache-Control", "no-cache");
/*  518 */         this.requests.setIfNotSet("Pragma", "no-cache");
/*      */       }
/*  520 */       this.requests.setIfNotSet("User-Agent", userAgent);
/*  521 */       int i = this.url.getPort();
/*  522 */       String str2 = this.url.getHost();
/*  523 */       if ((i != -1) && (i != this.url.getDefaultPort())) {
/*  524 */         str2 = str2 + ":" + String.valueOf(i);
/*      */       }
/*  526 */       this.requests.setIfNotSet("Host", str2);
/*  527 */       this.requests.setIfNotSet("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
/*      */ 
/*  537 */       if ((!this.failedOnce) && (this.http.getHttpKeepAliveSet())) {
/*  538 */         if ((this.http.usingProxy) && (tunnelState() != TunnelState.TUNNELING))
/*  539 */           this.requests.setIfNotSet("Proxy-Connection", "keep-alive");
/*      */         else {
/*  541 */           this.requests.setIfNotSet("Connection", "keep-alive");
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  550 */         this.requests.setIfNotSet("Connection", "close");
/*      */       }
/*      */ 
/*  553 */       long l = getIfModifiedSince();
/*  554 */       if (l != 0L) {
/*  555 */         localObject1 = new Date(l);
/*      */ 
/*  558 */         SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
/*      */ 
/*  560 */         localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/*  561 */         this.requests.setIfNotSet("If-Modified-Since", localSimpleDateFormat.format((Date)localObject1));
/*      */       }
/*      */ 
/*  564 */       Object localObject1 = AuthenticationInfo.getServerAuth(this.url);
/*  565 */       if ((localObject1 != null) && (((AuthenticationInfo)localObject1).supportsPreemptiveAuthorization()))
/*      */       {
/*  567 */         this.requests.setIfNotSet(((AuthenticationInfo)localObject1).getHeaderName(), ((AuthenticationInfo)localObject1).getHeaderValue(this.url, this.method));
/*  568 */         this.currentServerCredentials = ((AuthenticationInfo)localObject1);
/*      */       }
/*      */ 
/*  571 */       if ((!this.method.equals("PUT")) && ((this.poster != null) || (streaming()))) {
/*  572 */         this.requests.setIfNotSet("Content-type", "application/x-www-form-urlencoded");
/*      */       }
/*      */ 
/*  576 */       int k = 0;
/*      */ 
/*  578 */       if (streaming()) {
/*  579 */         if (this.chunkLength != -1) {
/*  580 */           this.requests.set("Transfer-Encoding", "chunked");
/*  581 */           k = 1;
/*      */         }
/*  583 */         else if (this.fixedContentLengthLong != -1L) {
/*  584 */           this.requests.set("Content-Length", String.valueOf(this.fixedContentLengthLong));
/*      */         }
/*  586 */         else if (this.fixedContentLength != -1) {
/*  587 */           this.requests.set("Content-Length", String.valueOf(this.fixedContentLength));
/*      */         }
/*      */ 
/*      */       }
/*  591 */       else if (this.poster != null)
/*      */       {
/*  593 */         synchronized (this.poster)
/*      */         {
/*  595 */           this.poster.close();
/*  596 */           this.requests.set("Content-Length", String.valueOf(this.poster.size()));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  601 */       if ((k == 0) && 
/*  602 */         (this.requests.findValue("Transfer-Encoding") != null)) {
/*  603 */         this.requests.remove("Transfer-Encoding");
/*  604 */         if (logger.isLoggable(900)) {
/*  605 */           logger.warning("use streaming mode for chunked encoding");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  613 */       setCookieHeader();
/*      */ 
/*  615 */       this.setRequests = true;
/*      */     }
/*  617 */     if (logger.isLoggable(500)) {
/*  618 */       logger.fine(this.requests.toString());
/*      */     }
/*  620 */     this.http.writeRequests(this.requests, this.poster, streaming());
/*  621 */     if (this.ps.checkError()) {
/*  622 */       String str1 = this.http.getProxyHostUsed();
/*  623 */       int j = this.http.getProxyPortUsed();
/*  624 */       disconnectInternal();
/*  625 */       if (this.failedOnce) {
/*  626 */         throw new IOException("Error writing to server");
/*      */       }
/*  628 */       this.failedOnce = true;
/*  629 */       if (str1 != null)
/*  630 */         setProxiedClient(this.url, str1, j);
/*      */       else {
/*  632 */         setNewClient(this.url);
/*      */       }
/*  634 */       this.ps = ((PrintStream)this.http.getOutputStream());
/*  635 */       this.connected = true;
/*  636 */       this.responses = new MessageHeader();
/*  637 */       this.setRequests = false;
/*  638 */       writeRequests();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void setNewClient(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  652 */     setNewClient(paramURL, false);
/*      */   }
/*      */ 
/*      */   protected void setNewClient(URL paramURL, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  664 */     this.http = HttpClient.New(paramURL, null, -1, paramBoolean, this.connectTimeout, this);
/*  665 */     this.http.setReadTimeout(this.readTimeout);
/*      */   }
/*      */ 
/*      */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt)
/*      */     throws IOException
/*      */   {
/*  680 */     setProxiedClient(paramURL, paramString, paramInt, false);
/*      */   }
/*      */ 
/*      */   protected void setProxiedClient(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  698 */     proxiedConnect(paramURL, paramString, paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   protected void proxiedConnect(URL paramURL, String paramString, int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  705 */     this.http = HttpClient.New(paramURL, paramString, paramInt, paramBoolean, this.connectTimeout, this);
/*      */ 
/*  707 */     this.http.setReadTimeout(this.readTimeout);
/*      */   }
/*      */ 
/*      */   protected HttpURLConnection(URL paramURL, Handler paramHandler)
/*      */     throws IOException
/*      */   {
/*  714 */     this(paramURL, null, paramHandler);
/*      */   }
/*      */ 
/*      */   public HttpURLConnection(URL paramURL, String paramString, int paramInt) {
/*  718 */     this(paramURL, new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(paramString, paramInt)));
/*      */   }
/*      */ 
/*      */   public HttpURLConnection(URL paramURL, Proxy paramProxy)
/*      */   {
/*  724 */     this(paramURL, paramProxy, new Handler());
/*      */   }
/*      */ 
/*      */   protected HttpURLConnection(URL paramURL, Proxy paramProxy, Handler paramHandler) {
/*  728 */     super(paramURL);
/*  729 */     this.requests = new MessageHeader();
/*  730 */     this.responses = new MessageHeader();
/*  731 */     this.handler = paramHandler;
/*  732 */     this.instProxy = paramProxy;
/*  733 */     if ((this.instProxy instanceof ApplicationProxy))
/*      */     {
/*      */       try
/*      */       {
/*  737 */         this.cookieHandler = CookieHandler.getDefault(); } catch (SecurityException localSecurityException) {
/*      */       }
/*      */     }
/*  740 */     else this.cookieHandler = ((CookieHandler)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public CookieHandler run() {
/*  743 */           return CookieHandler.getDefault();
/*      */         }
/*      */       }));
/*      */ 
/*  747 */     this.cacheHandler = ((ResponseCache)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ResponseCache run() {
/*  750 */         return ResponseCache.getDefault();
/*      */       }
/*      */     }));
/*      */   }
/*      */ 
/*      */   public static void setDefaultAuthenticator(HttpAuthenticator paramHttpAuthenticator)
/*      */   {
/*  759 */     defaultAuth = paramHttpAuthenticator;
/*      */   }
/*      */ 
/*      */   public static InputStream openConnectionCheckRedirects(URLConnection paramURLConnection)
/*      */     throws IOException
/*      */   {
/*  769 */     int j = 0;
/*      */     InputStream localInputStream;
/*      */     int i;
/*      */     do
/*      */     {
/*  773 */       if ((paramURLConnection instanceof HttpURLConnection)) {
/*  774 */         ((HttpURLConnection)paramURLConnection).setInstanceFollowRedirects(false);
/*      */       }
/*      */ 
/*  780 */       localInputStream = paramURLConnection.getInputStream();
/*  781 */       i = 0;
/*      */ 
/*  783 */       if ((paramURLConnection instanceof HttpURLConnection)) {
/*  784 */         HttpURLConnection localHttpURLConnection = (HttpURLConnection)paramURLConnection;
/*  785 */         int k = localHttpURLConnection.getResponseCode();
/*  786 */         if ((k >= 300) && (k <= 307) && (k != 306) && (k != 304))
/*      */         {
/*  788 */           URL localURL1 = localHttpURLConnection.getURL();
/*  789 */           String str = localHttpURLConnection.getHeaderField("Location");
/*  790 */           URL localURL2 = null;
/*  791 */           if (str != null) {
/*  792 */             localURL2 = new URL(localURL1, str);
/*      */           }
/*  794 */           localHttpURLConnection.disconnect();
/*  795 */           if ((localURL2 == null) || (!localURL1.getProtocol().equals(localURL2.getProtocol())) || (localURL1.getPort() != localURL2.getPort()) || (!hostsEqual(localURL1, localURL2)) || (j >= 5))
/*      */           {
/*  801 */             throw new SecurityException("illegal URL redirect");
/*      */           }
/*  803 */           i = 1;
/*  804 */           paramURLConnection = localURL2.openConnection();
/*  805 */           j++;
/*      */         }
/*      */       }
/*      */     }
/*  808 */     while (i != 0);
/*  809 */     return localInputStream;
/*      */   }
/*      */ 
/*      */   private static boolean hostsEqual(URL paramURL1, URL paramURL2)
/*      */   {
/*  817 */     String str1 = paramURL1.getHost();
/*  818 */     final String str2 = paramURL2.getHost();
/*      */ 
/*  820 */     if (str1 == null)
/*  821 */       return str2 == null;
/*  822 */     if (str2 == null)
/*  823 */       return false;
/*  824 */     if (str1.equalsIgnoreCase(str2)) {
/*  825 */       return true;
/*      */     }
/*      */ 
/*  829 */     final boolean[] arrayOfBoolean = { false };
/*      */ 
/*  831 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run() {
/*      */         try {
/*  835 */           InetAddress localInetAddress1 = InetAddress.getByName(this.val$h1);
/*  836 */           InetAddress localInetAddress2 = InetAddress.getByName(str2);
/*  837 */           arrayOfBoolean[0] = localInetAddress1.equals(localInetAddress2);
/*      */         } catch (UnknownHostException localUnknownHostException) {
/*      */         } catch (SecurityException localSecurityException) {
/*      */         }
/*  841 */         return null;
/*      */       }
/*      */     });
/*  845 */     return arrayOfBoolean[0];
/*      */   }
/*      */ 
/*      */   public void connect()
/*      */     throws IOException
/*      */   {
/*  851 */     plainConnect();
/*      */   }
/*      */ 
/*      */   private boolean checkReuseConnection() {
/*  855 */     if (this.connected) {
/*  856 */       return true;
/*      */     }
/*  858 */     if (this.reuseClient != null) {
/*  859 */       this.http = this.reuseClient;
/*  860 */       this.http.setReadTimeout(getReadTimeout());
/*  861 */       this.http.reuse = false;
/*  862 */       this.reuseClient = null;
/*  863 */       this.connected = true;
/*  864 */       return true;
/*      */     }
/*  866 */     return false;
/*      */   }
/*      */ 
/*      */   protected void plainConnect() throws IOException {
/*  870 */     if (this.connected) {
/*  871 */       return;
/*      */     }
/*      */ 
/*  874 */     if ((this.cacheHandler != null) && (getUseCaches())) {
/*      */       try {
/*  876 */         URI localURI1 = ParseUtil.toURI(this.url);
/*  877 */         if (localURI1 != null) {
/*  878 */           this.cachedResponse = this.cacheHandler.get(localURI1, getRequestMethod(), this.requests.getHeaders(EXCLUDE_HEADERS));
/*  879 */           if (("https".equalsIgnoreCase(localURI1.getScheme())) && (!(this.cachedResponse instanceof SecureCacheResponse)))
/*      */           {
/*  881 */             this.cachedResponse = null;
/*      */           }
/*  883 */           if (logger.isLoggable(300)) {
/*  884 */             logger.finest("Cache Request for " + localURI1 + " / " + getRequestMethod());
/*  885 */             logger.finest("From cache: " + (this.cachedResponse != null ? this.cachedResponse.toString() : "null"));
/*      */           }
/*  887 */           if (this.cachedResponse != null) {
/*  888 */             this.cachedHeaders = mapToMessageHeader(this.cachedResponse.getHeaders());
/*  889 */             this.cachedInputStream = this.cachedResponse.getBody();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException1) {
/*      */       }
/*  895 */       if ((this.cachedHeaders != null) && (this.cachedInputStream != null)) {
/*  896 */         this.connected = true;
/*  897 */         return;
/*      */       }
/*  899 */       this.cachedResponse = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  911 */       if (this.instProxy == null)
/*      */       {
/*  915 */         ProxySelector localProxySelector = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public ProxySelector run()
/*      */           {
/*  919 */             return ProxySelector.getDefault();
/*      */           }
/*      */         });
/*  922 */         if (localProxySelector != null) {
/*  923 */           URI localURI2 = ParseUtil.toURI(this.url);
/*  924 */           if (logger.isLoggable(300)) {
/*  925 */             logger.finest("ProxySelector Request for " + localURI2);
/*      */           }
/*  927 */           Iterator localIterator = localProxySelector.select(localURI2).iterator();
/*      */ 
/*  929 */           while (localIterator.hasNext()) {
/*  930 */             Proxy localProxy = (Proxy)localIterator.next();
/*      */             try {
/*  932 */               if (!this.failedOnce) {
/*  933 */                 this.http = getNewHttpClient(this.url, localProxy, this.connectTimeout);
/*  934 */                 this.http.setReadTimeout(this.readTimeout);
/*      */               }
/*      */               else
/*      */               {
/*  938 */                 this.http = getNewHttpClient(this.url, localProxy, this.connectTimeout, false);
/*  939 */                 this.http.setReadTimeout(this.readTimeout);
/*      */               }
/*  941 */               if ((logger.isLoggable(300)) && 
/*  942 */                 (localProxy != null)) {
/*  943 */                 logger.finest("Proxy used: " + localProxy.toString());
/*      */               }
/*      */             }
/*      */             catch (IOException localIOException3)
/*      */             {
/*  948 */               if (localProxy != Proxy.NO_PROXY) {
/*  949 */                 localProxySelector.connectFailed(localURI2, localProxy.address(), localIOException3);
/*  950 */                 if (!localIterator.hasNext())
/*      */                 {
/*  952 */                   this.http = getNewHttpClient(this.url, null, this.connectTimeout, false);
/*  953 */                   this.http.setReadTimeout(this.readTimeout);
/*  954 */                   break;
/*      */                 }
/*      */               } else {
/*  957 */                 throw localIOException3;
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*  964 */         else if (!this.failedOnce) {
/*  965 */           this.http = getNewHttpClient(this.url, null, this.connectTimeout);
/*  966 */           this.http.setReadTimeout(this.readTimeout);
/*      */         }
/*      */         else
/*      */         {
/*  970 */           this.http = getNewHttpClient(this.url, null, this.connectTimeout, false);
/*  971 */           this.http.setReadTimeout(this.readTimeout);
/*      */         }
/*      */ 
/*      */       }
/*  975 */       else if (!this.failedOnce) {
/*  976 */         this.http = getNewHttpClient(this.url, this.instProxy, this.connectTimeout);
/*  977 */         this.http.setReadTimeout(this.readTimeout);
/*      */       }
/*      */       else
/*      */       {
/*  981 */         this.http = getNewHttpClient(this.url, this.instProxy, this.connectTimeout, false);
/*  982 */         this.http.setReadTimeout(this.readTimeout);
/*      */       }
/*      */ 
/*  986 */       this.ps = ((PrintStream)this.http.getOutputStream());
/*      */     } catch (IOException localIOException2) {
/*  988 */       throw localIOException2;
/*      */     }
/*      */ 
/*  991 */     this.connected = true;
/*      */   }
/*      */ 
/*      */   protected HttpClient getNewHttpClient(URL paramURL, Proxy paramProxy, int paramInt)
/*      */     throws IOException
/*      */   {
/*  997 */     return HttpClient.New(paramURL, paramProxy, paramInt, this);
/*      */   }
/*      */ 
/*      */   protected HttpClient getNewHttpClient(URL paramURL, Proxy paramProxy, int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1004 */     return HttpClient.New(paramURL, paramProxy, paramInt, paramBoolean, this);
/*      */   }
/*      */ 
/*      */   private void expect100Continue()
/*      */     throws IOException
/*      */   {
/* 1010 */     int i = this.http.getReadTimeout();
/* 1011 */     int j = 0;
/* 1012 */     int k = 0;
/* 1013 */     if (i <= 0)
/*      */     {
/* 1016 */       this.http.setReadTimeout(5000);
/* 1017 */       j = 1;
/*      */     }
/*      */     try
/*      */     {
/* 1021 */       this.http.parseHTTP(this.responses, this.pi, this);
/*      */     } catch (SocketTimeoutException localSocketTimeoutException) {
/* 1023 */       if (j == 0) {
/* 1024 */         throw localSocketTimeoutException;
/*      */       }
/* 1026 */       k = 1;
/* 1027 */       this.http.setIgnoreContinue(true);
/*      */     }
/* 1029 */     if (k == 0)
/*      */     {
/* 1031 */       String str = this.responses.getValue(0);
/*      */ 
/* 1035 */       if ((str != null) && (str.startsWith("HTTP/"))) {
/* 1036 */         String[] arrayOfString = str.split("\\s+");
/* 1037 */         this.responseCode = -1;
/*      */         try
/*      */         {
/* 1040 */           if (arrayOfString.length > 1)
/* 1041 */             this.responseCode = Integer.parseInt(arrayOfString[1]);
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*      */         }
/*      */       }
/* 1045 */       if (this.responseCode != 100) {
/* 1046 */         throw new ProtocolException("Server rejected operation");
/*      */       }
/*      */     }
/*      */ 
/* 1050 */     this.http.setReadTimeout(i);
/*      */ 
/* 1052 */     this.responseCode = -1;
/* 1053 */     this.responses.reset();
/*      */   }
/*      */ 
/*      */   public synchronized OutputStream getOutputStream()
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1072 */       if (!this.doOutput) {
/* 1073 */         throw new ProtocolException("cannot write to a URLConnection if doOutput=false - call setDoOutput(true)");
/*      */       }
/*      */ 
/* 1077 */       if (this.method.equals("GET")) {
/* 1078 */         this.method = "POST";
/*      */       }
/* 1080 */       if ((!"POST".equals(this.method)) && (!"PUT".equals(this.method)) && ("http".equals(this.url.getProtocol())))
/*      */       {
/* 1082 */         throw new ProtocolException("HTTP method " + this.method + " doesn't support output");
/*      */       }
/*      */ 
/* 1087 */       if (this.inputStream != null) {
/* 1088 */         throw new ProtocolException("Cannot write output after reading input.");
/*      */       }
/*      */ 
/* 1091 */       if (!checkReuseConnection()) {
/* 1092 */         connect();
/*      */       }
/* 1094 */       int i = 0;
/* 1095 */       String str = this.requests.findValue("Expect");
/* 1096 */       if ("100-Continue".equalsIgnoreCase(str)) {
/* 1097 */         this.http.setIgnoreContinue(false);
/* 1098 */         i = 1;
/*      */       }
/*      */ 
/* 1101 */       if ((streaming()) && (this.strOutputStream == null)) {
/* 1102 */         writeRequests();
/*      */       }
/*      */ 
/* 1105 */       if (i != 0) {
/* 1106 */         expect100Continue();
/*      */       }
/* 1108 */       this.ps = ((PrintStream)this.http.getOutputStream());
/* 1109 */       if (streaming()) {
/* 1110 */         if (this.strOutputStream == null) {
/* 1111 */           if (this.chunkLength != -1) {
/* 1112 */             this.strOutputStream = new StreamingOutputStream(new ChunkedOutputStream(this.ps, this.chunkLength), -1L);
/*      */           }
/*      */           else {
/* 1115 */             long l = 0L;
/* 1116 */             if (this.fixedContentLengthLong != -1L)
/* 1117 */               l = this.fixedContentLengthLong;
/* 1118 */             else if (this.fixedContentLength != -1) {
/* 1119 */               l = this.fixedContentLength;
/*      */             }
/* 1121 */             this.strOutputStream = new StreamingOutputStream(this.ps, l);
/*      */           }
/*      */         }
/* 1124 */         return this.strOutputStream;
/*      */       }
/* 1126 */       if (this.poster == null) {
/* 1127 */         this.poster = new PosterOutputStream();
/*      */       }
/* 1129 */       return this.poster;
/*      */     }
/*      */     catch (RuntimeException localRuntimeException) {
/* 1132 */       disconnectInternal();
/* 1133 */       throw localRuntimeException;
/*      */     }
/*      */     catch (ProtocolException localProtocolException)
/*      */     {
/* 1137 */       int j = this.responseCode;
/* 1138 */       disconnectInternal();
/* 1139 */       this.responseCode = j;
/* 1140 */       throw localProtocolException;
/*      */     } catch (IOException localIOException) {
/* 1142 */       disconnectInternal();
/* 1143 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean streaming() {
/* 1148 */     return (this.fixedContentLength != -1) || (this.fixedContentLengthLong != -1L) || (this.chunkLength != -1);
/*      */   }
/*      */ 
/*      */   private void setCookieHeader()
/*      */     throws IOException
/*      */   {
/* 1157 */     if (this.cookieHandler != null)
/*      */     {
/* 1161 */       synchronized (this) {
/* 1162 */         if (this.setUserCookies) {
/* 1163 */           int i = this.requests.getKey("Cookie");
/* 1164 */           if (i != -1)
/* 1165 */             this.userCookies = this.requests.getValue(i);
/* 1166 */           i = this.requests.getKey("Cookie2");
/* 1167 */           if (i != -1)
/* 1168 */             this.userCookies2 = this.requests.getValue(i);
/* 1169 */           this.setUserCookies = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1174 */       this.requests.remove("Cookie");
/* 1175 */       this.requests.remove("Cookie2");
/*      */ 
/* 1177 */       ??? = ParseUtil.toURI(this.url);
/* 1178 */       if (??? != null) {
/* 1179 */         if (logger.isLoggable(300)) {
/* 1180 */           logger.finest("CookieHandler request for " + ???);
/*      */         }
/* 1182 */         Map localMap = this.cookieHandler.get((URI)???, this.requests.getHeaders(EXCLUDE_HEADERS));
/*      */ 
/* 1185 */         if (!localMap.isEmpty()) {
/* 1186 */           if (logger.isLoggable(300)) {
/* 1187 */             logger.finest("Cookies retrieved: " + localMap.toString());
/*      */           }
/*      */ 
/* 1190 */           for (Map.Entry localEntry : localMap.entrySet()) {
/* 1191 */             String str1 = (String)localEntry.getKey();
/*      */ 
/* 1194 */             if (("Cookie".equalsIgnoreCase(str1)) || ("Cookie2".equalsIgnoreCase(str1)))
/*      */             {
/* 1198 */               List localList = (List)localEntry.getValue();
/* 1199 */               if ((localList != null) && (!localList.isEmpty())) {
/* 1200 */                 StringBuilder localStringBuilder = new StringBuilder();
/* 1201 */                 for (String str2 : localList) {
/* 1202 */                   localStringBuilder.append(str2).append("; ");
/*      */                 }
/*      */                 try
/*      */                 {
/* 1206 */                   this.requests.add(str1, localStringBuilder.substring(0, localStringBuilder.length() - 2));
/*      */                 }
/*      */                 catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
/*      */                 {
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       int j;
/* 1214 */       if (this.userCookies != null)
/*      */       {
/* 1216 */         if ((j = this.requests.getKey("Cookie")) != -1)
/* 1217 */           this.requests.set("Cookie", this.requests.getValue(j) + ";" + this.userCookies);
/*      */         else
/* 1219 */           this.requests.set("Cookie", this.userCookies);
/*      */       }
/* 1221 */       if (this.userCookies2 != null)
/*      */       {
/* 1223 */         if ((j = this.requests.getKey("Cookie2")) != -1)
/* 1224 */           this.requests.set("Cookie2", this.requests.getValue(j) + ";" + this.userCookies2);
/*      */         else
/* 1226 */           this.requests.set("Cookie2", this.userCookies2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized InputStream getInputStream()
/*      */     throws IOException
/*      */   {
/* 1236 */     if (!this.doInput) {
/* 1237 */       throw new ProtocolException("Cannot read from URLConnection if doInput=false (call setDoInput(true))");
/*      */     }
/*      */ 
/* 1241 */     if (this.rememberedException != null) {
/* 1242 */       if ((this.rememberedException instanceof RuntimeException)) {
/* 1243 */         throw new RuntimeException(this.rememberedException);
/*      */       }
/* 1245 */       throw getChainedException((IOException)this.rememberedException);
/*      */     }
/*      */ 
/* 1249 */     if (this.inputStream != null) {
/* 1250 */       return this.inputStream;
/*      */     }
/*      */ 
/* 1253 */     if (streaming()) {
/* 1254 */       if (this.strOutputStream == null) {
/* 1255 */         getOutputStream();
/*      */       }
/*      */ 
/* 1258 */       this.strOutputStream.close();
/* 1259 */       if (!this.strOutputStream.writtenOK()) {
/* 1260 */         throw new IOException("Incomplete output stream");
/*      */       }
/*      */     }
/*      */ 
/* 1264 */     int i = 0;
/* 1265 */     int j = 0;
/* 1266 */     long l = -1L;
/* 1267 */     Object localObject1 = null;
/* 1268 */     AuthenticationInfo localAuthenticationInfo = null;
/* 1269 */     AuthenticationHeader localAuthenticationHeader = null;
/*      */ 
/* 1291 */     int k = 0;
/* 1292 */     int m = 0;
/*      */ 
/* 1295 */     this.isUserServerAuth = (this.requests.getKey("Authorization") != -1);
/* 1296 */     this.isUserProxyAuth = (this.requests.getKey("Proxy-Authorization") != -1);
/*      */     try
/*      */     {
/*      */       do {
/* 1300 */         if (!checkReuseConnection()) {
/* 1301 */           connect();
/*      */         }
/* 1303 */         if (this.cachedInputStream != null) {
/* 1304 */           return this.cachedInputStream;
/*      */         }
/*      */ 
/* 1308 */         boolean bool1 = ProgressMonitor.getDefault().shouldMeterInput(this.url, this.method);
/*      */ 
/* 1310 */         if (bool1) {
/* 1311 */           this.pi = new ProgressSource(this.url, this.method);
/* 1312 */           this.pi.beginTracking();
/*      */         }
/*      */ 
/* 1319 */         this.ps = ((PrintStream)this.http.getOutputStream());
/*      */ 
/* 1321 */         if (!streaming()) {
/* 1322 */           writeRequests();
/*      */         }
/* 1324 */         this.http.parseHTTP(this.responses, this.pi, this);
/* 1325 */         if (logger.isLoggable(500)) {
/* 1326 */           logger.fine(this.responses.toString());
/*      */         }
/*      */ 
/* 1329 */         boolean bool2 = this.responses.filterNTLMResponses("WWW-Authenticate");
/* 1330 */         boolean bool3 = this.responses.filterNTLMResponses("Proxy-Authenticate");
/* 1331 */         if (((bool2) || (bool3)) && 
/* 1332 */           (logger.isLoggable(500))) {
/* 1333 */           logger.fine(">>>> Headers are filtered");
/* 1334 */           logger.fine(this.responses.toString());
/*      */         }
/*      */ 
/* 1338 */         this.inputStream = this.http.getInputStream();
/*      */ 
/* 1340 */         j = getResponseCode();
/* 1341 */         if (j == -1) {
/* 1342 */           disconnectInternal();
/* 1343 */           throw new IOException("Invalid Http response");
/*      */         }
/*      */         boolean bool4;
/*      */         Object localObject4;
/*      */         Object localObject5;
/*      */         Object localObject6;
/* 1345 */         if (j == 407) {
/* 1346 */           if (streaming()) {
/* 1347 */             disconnectInternal();
/* 1348 */             throw new HttpRetryException("cannot retry due to proxy authentication, in streaming mode", 407);
/*      */           }
/*      */ 
/* 1353 */           bool4 = false;
/* 1354 */           localObject4 = this.responses.multiValueIterator("Proxy-Authenticate");
/* 1355 */           while (((Iterator)localObject4).hasNext()) {
/* 1356 */             localObject5 = ((String)((Iterator)localObject4).next()).trim();
/* 1357 */             if ((((String)localObject5).equalsIgnoreCase("Negotiate")) || (((String)localObject5).equalsIgnoreCase("Kerberos")))
/*      */             {
/* 1359 */               if (m == 0) {
/* 1360 */                 m = 1; break;
/*      */               }
/* 1362 */               bool4 = true;
/* 1363 */               this.doingNTLMp2ndStage = false;
/* 1364 */               localAuthenticationInfo = null;
/*      */ 
/* 1366 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1376 */           localObject5 = new AuthenticationHeader("Proxy-Authenticate", this.responses, new HttpCallerInfo(this.url, this.http.getProxyHostUsed(), this.http.getProxyPortUsed()), bool4);
/*      */ 
/* 1383 */           if (!this.doingNTLMp2ndStage) {
/* 1384 */             localAuthenticationInfo = resetProxyAuthentication(localAuthenticationInfo, (AuthenticationHeader)localObject5);
/*      */ 
/* 1386 */             if (localAuthenticationInfo != null) {
/* 1387 */               i++;
/* 1388 */               disconnectInternal();
/* 1389 */               continue;
/*      */             }
/*      */           }
/*      */           else {
/* 1393 */             localObject6 = this.responses.findValue("Proxy-Authenticate");
/* 1394 */             reset();
/* 1395 */             if (!localAuthenticationInfo.setHeaders(this, ((AuthenticationHeader)localObject5).headerParser(), (String)localObject6))
/*      */             {
/* 1397 */               disconnectInternal();
/* 1398 */               throw new IOException("Authentication failure");
/*      */             }
/* 1400 */             if ((localObject1 != null) && (localAuthenticationHeader != null) && (!((AuthenticationInfo)localObject1).setHeaders(this, localAuthenticationHeader.headerParser(), (String)localObject6)))
/*      */             {
/* 1403 */               disconnectInternal();
/* 1404 */               throw new IOException("Authentication failure");
/*      */             }
/* 1406 */             this.authObj = null;
/* 1407 */             this.doingNTLMp2ndStage = false;
/* 1408 */             continue;
/*      */           }
/*      */         } else {
/* 1411 */           m = 0;
/* 1412 */           this.doingNTLMp2ndStage = false;
/* 1413 */           if (!this.isUserProxyAuth) {
/* 1414 */             this.requests.remove("Proxy-Authorization");
/*      */           }
/*      */         }
/*      */ 
/* 1418 */         if (localAuthenticationInfo != null)
/*      */         {
/* 1420 */           localAuthenticationInfo.addToCache();
/*      */         }
/*      */ 
/* 1423 */         if (j == 401) {
/* 1424 */           if (streaming()) {
/* 1425 */             disconnectInternal();
/* 1426 */             throw new HttpRetryException("cannot retry due to server authentication, in streaming mode", 401);
/*      */           }
/*      */ 
/* 1431 */           bool4 = false;
/* 1432 */           localObject4 = this.responses.multiValueIterator("WWW-Authenticate");
/* 1433 */           while (((Iterator)localObject4).hasNext()) {
/* 1434 */             localObject5 = ((String)((Iterator)localObject4).next()).trim();
/* 1435 */             if ((((String)localObject5).equalsIgnoreCase("Negotiate")) || (((String)localObject5).equalsIgnoreCase("Kerberos")))
/*      */             {
/* 1437 */               if (k == 0) {
/* 1438 */                 k = 1; break;
/*      */               }
/* 1440 */               bool4 = true;
/* 1441 */               this.doingNTLM2ndStage = false;
/* 1442 */               localObject1 = null;
/*      */ 
/* 1444 */               break;
/*      */             }
/*      */           }
/*      */ 
/* 1448 */           localAuthenticationHeader = new AuthenticationHeader("WWW-Authenticate", this.responses, new HttpCallerInfo(this.url), bool4);
/*      */ 
/* 1454 */           localObject5 = localAuthenticationHeader.raw();
/* 1455 */           if (!this.doingNTLM2ndStage) {
/* 1456 */             if ((localObject1 != null) && (((AuthenticationInfo)localObject1).getAuthScheme() != AuthScheme.NTLM))
/*      */             {
/* 1458 */               if (((AuthenticationInfo)localObject1).isAuthorizationStale((String)localObject5))
/*      */               {
/* 1460 */                 disconnectWeb();
/* 1461 */                 i++;
/* 1462 */                 this.requests.set(((AuthenticationInfo)localObject1).getHeaderName(), ((AuthenticationInfo)localObject1).getHeaderValue(this.url, this.method));
/*      */ 
/* 1464 */                 this.currentServerCredentials = ((AuthenticationInfo)localObject1);
/* 1465 */                 setCookieHeader();
/* 1466 */                 continue;
/*      */               }
/* 1468 */               ((AuthenticationInfo)localObject1).removeFromCache();
/*      */             }
/*      */ 
/* 1471 */             localObject1 = getServerAuthentication(localAuthenticationHeader);
/* 1472 */             this.currentServerCredentials = ((AuthenticationInfo)localObject1);
/*      */ 
/* 1474 */             if (localObject1 != null) {
/* 1475 */               disconnectWeb();
/* 1476 */               i++;
/* 1477 */               setCookieHeader();
/* 1478 */               continue;
/*      */             }
/*      */           } else {
/* 1481 */             reset();
/*      */ 
/* 1483 */             if (!((AuthenticationInfo)localObject1).setHeaders(this, null, (String)localObject5)) {
/* 1484 */               disconnectWeb();
/* 1485 */               throw new IOException("Authentication failure");
/*      */             }
/* 1487 */             this.doingNTLM2ndStage = false;
/* 1488 */             this.authObj = null;
/* 1489 */             setCookieHeader();
/* 1490 */             continue;
/*      */           }
/*      */         }
/*      */ 
/* 1494 */         if (localObject1 != null)
/*      */         {
/*      */           Object localObject2;
/* 1496 */           if ((!(localObject1 instanceof DigestAuthentication)) || (this.domain == null))
/*      */           {
/* 1498 */             if ((localObject1 instanceof BasicAuthentication))
/*      */             {
/* 1500 */               localObject2 = AuthenticationInfo.reducePath(this.url.getPath());
/* 1501 */               localObject4 = ((AuthenticationInfo)localObject1).path;
/* 1502 */               if ((!((String)localObject4).startsWith((String)localObject2)) || (((String)localObject2).length() >= ((String)localObject4).length()))
/*      */               {
/* 1504 */                 localObject2 = BasicAuthentication.getRootPath((String)localObject4, (String)localObject2);
/*      */               }
/*      */ 
/* 1507 */               localObject5 = (BasicAuthentication)((AuthenticationInfo)localObject1).clone();
/*      */ 
/* 1509 */               ((AuthenticationInfo)localObject1).removeFromCache();
/* 1510 */               ((BasicAuthentication)localObject5).path = ((String)localObject2);
/* 1511 */               localObject1 = localObject5;
/*      */             }
/* 1513 */             ((AuthenticationInfo)localObject1).addToCache();
/*      */           }
/*      */           else {
/* 1516 */             localObject2 = (DigestAuthentication)localObject1;
/*      */ 
/* 1518 */             localObject4 = new StringTokenizer(this.domain, " ");
/* 1519 */             localObject5 = ((DigestAuthentication)localObject2).realm;
/* 1520 */             localObject6 = ((DigestAuthentication)localObject2).pw;
/* 1521 */             this.digestparams = ((DigestAuthentication)localObject2).params;
/* 1522 */             while (((StringTokenizer)localObject4).hasMoreTokens()) {
/* 1523 */               String str2 = ((StringTokenizer)localObject4).nextToken();
/*      */               try
/*      */               {
/* 1526 */                 URL localURL = new URL(this.url, str2);
/* 1527 */                 DigestAuthentication localDigestAuthentication = new DigestAuthentication(false, localURL, (String)localObject5, "Digest", (PasswordAuthentication)localObject6, this.digestparams);
/*      */ 
/* 1529 */                 localDigestAuthentication.addToCache();
/*      */               }
/*      */               catch (Exception localException2)
/*      */               {
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1538 */         k = 0;
/* 1539 */         m = 0;
/*      */ 
/* 1542 */         this.doingNTLMp2ndStage = false;
/* 1543 */         this.doingNTLM2ndStage = false;
/* 1544 */         if (!this.isUserServerAuth)
/* 1545 */           this.requests.remove("Authorization");
/* 1546 */         if (!this.isUserProxyAuth) {
/* 1547 */           this.requests.remove("Proxy-Authorization");
/*      */         }
/* 1549 */         if (j == 200)
/* 1550 */           checkResponseCredentials(false);
/*      */         else {
/* 1552 */           this.needToCheck = false;
/*      */         }
/*      */ 
/* 1556 */         this.needToCheck = true;
/*      */ 
/* 1558 */         if (followRedirect())
/*      */         {
/* 1563 */           i++;
/*      */ 
/* 1567 */           setCookieHeader();
/*      */         }
/*      */         else
/*      */         {
/*      */           try
/*      */           {
/* 1573 */             l = Long.parseLong(this.responses.findValue("content-length"));
/*      */           } catch (Exception localException1) {
/*      */           }
/* 1576 */           if ((this.method.equals("HEAD")) || (l == 0L) || (j == 304) || (j == 204))
/*      */           {
/* 1580 */             if (this.pi != null) {
/* 1581 */               this.pi.finishTracking();
/* 1582 */               this.pi = null;
/*      */             }
/* 1584 */             this.http.finished();
/* 1585 */             this.http = null;
/* 1586 */             this.inputStream = new EmptyInputStream();
/* 1587 */             this.connected = false;
/*      */           }
/*      */           Object localObject3;
/* 1590 */           if ((j == 200) || (j == 203) || (j == 206) || (j == 300) || (j == 301) || (j == 410))
/*      */           {
/* 1592 */             if (this.cacheHandler != null)
/*      */             {
/* 1594 */               localObject3 = ParseUtil.toURI(this.url);
/* 1595 */               if (localObject3 != null) {
/* 1596 */                 localObject4 = this;
/* 1597 */                 if ("https".equalsIgnoreCase(((URI)localObject3).getScheme()))
/*      */                 {
/*      */                   try
/*      */                   {
/* 1602 */                     localObject4 = (URLConnection)getClass().getField("httpsURLConnection").get(this);
/*      */                   }
/*      */                   catch (IllegalAccessException localIllegalAccessException) {
/*      */                   }
/*      */                   catch (NoSuchFieldException localNoSuchFieldException) {
/*      */                   }
/*      */                 }
/* 1609 */                 CacheRequest localCacheRequest = this.cacheHandler.put((URI)localObject3, (URLConnection)localObject4);
/*      */ 
/* 1611 */                 if ((localCacheRequest != null) && (this.http != null)) {
/* 1612 */                   this.http.setCacheRequest(localCacheRequest);
/* 1613 */                   this.inputStream = new HttpInputStream(this.inputStream, localCacheRequest);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/* 1619 */           if (!(this.inputStream instanceof HttpInputStream)) {
/* 1620 */             this.inputStream = new HttpInputStream(this.inputStream);
/*      */           }
/*      */ 
/* 1623 */           if (j >= 400) {
/* 1624 */             if ((j == 404) || (j == 410)) {
/* 1625 */               throw new FileNotFoundException(this.url.toString());
/*      */             }
/* 1627 */             throw new IOException("Server returned HTTP response code: " + j + " for URL: " + this.url.toString());
/*      */           }
/*      */ 
/* 1632 */           this.poster = null;
/* 1633 */           this.strOutputStream = null;
/* 1634 */           return this.inputStream; } 
/* 1635 */       }while (i < maxRedirects);
/*      */ 
/* 1637 */       throw new ProtocolException("Server redirected too many  times (" + i + ")");
/*      */     }
/*      */     catch (RuntimeException localRuntimeException) {
/* 1640 */       disconnectInternal();
/* 1641 */       this.rememberedException = localRuntimeException;
/* 1642 */       throw localRuntimeException;
/*      */     } catch (IOException localIOException) {
/* 1644 */       this.rememberedException = localIOException;
/*      */ 
/* 1648 */       String str1 = this.responses.findValue("Transfer-Encoding");
/* 1649 */       if ((this.http != null) && (this.http.isKeepingAlive()) && (enableESBuffer) && ((l > 0L) || ((str1 != null) && (str1.equalsIgnoreCase("chunked")))))
/*      */       {
/* 1651 */         this.errorStream = ErrorStream.getErrorStream(this.inputStream, l, this.http);
/*      */       }
/* 1653 */       throw localIOException;
/*      */     } finally {
/* 1655 */       if (this.proxyAuthKey != null) {
/* 1656 */         AuthenticationInfo.endAuthRequest(this.proxyAuthKey);
/*      */       }
/* 1658 */       if (this.serverAuthKey != null)
/* 1659 */         AuthenticationInfo.endAuthRequest(this.serverAuthKey);
/*      */     }
/*      */   }
/*      */ 
/*      */   private IOException getChainedException(final IOException paramIOException)
/*      */   {
/*      */     try
/*      */     {
/* 1671 */       final Object[] arrayOfObject = { paramIOException.getMessage() };
/* 1672 */       IOException localIOException = (IOException)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public IOException run() throws Exception
/*      */         {
/* 1676 */           return (IOException)paramIOException.getClass().getConstructor(new Class[] { String.class }).newInstance(arrayOfObject);
/*      */         }
/*      */       });
/* 1682 */       localIOException.initCause(paramIOException);
/* 1683 */       return localIOException; } catch (Exception localException) {
/*      */     }
/* 1685 */     return paramIOException;
/*      */   }
/*      */ 
/*      */   public InputStream getErrorStream()
/*      */   {
/* 1691 */     if ((this.connected) && (this.responseCode >= 400))
/*      */     {
/* 1693 */       if (this.errorStream != null)
/* 1694 */         return this.errorStream;
/* 1695 */       if (this.inputStream != null) {
/* 1696 */         return this.inputStream;
/*      */       }
/*      */     }
/* 1699 */     return null;
/*      */   }
/*      */ 
/*      */   private AuthenticationInfo resetProxyAuthentication(AuthenticationInfo paramAuthenticationInfo, AuthenticationHeader paramAuthenticationHeader)
/*      */     throws IOException
/*      */   {
/* 1710 */     if ((paramAuthenticationInfo != null) && (paramAuthenticationInfo.getAuthScheme() != AuthScheme.NTLM))
/*      */     {
/* 1712 */       String str1 = paramAuthenticationHeader.raw();
/* 1713 */       if (paramAuthenticationInfo.isAuthorizationStale(str1))
/*      */       {
/*      */         String str2;
/* 1716 */         if ((paramAuthenticationInfo instanceof DigestAuthentication)) {
/* 1717 */           DigestAuthentication localDigestAuthentication = (DigestAuthentication)paramAuthenticationInfo;
/*      */ 
/* 1719 */           if (tunnelState() == TunnelState.SETUP)
/* 1720 */             str2 = localDigestAuthentication.getHeaderValue(connectRequestURI(this.url), HTTP_CONNECT);
/*      */           else
/* 1722 */             str2 = localDigestAuthentication.getHeaderValue(getRequestURI(), this.method);
/*      */         }
/*      */         else {
/* 1725 */           str2 = paramAuthenticationInfo.getHeaderValue(this.url, this.method);
/*      */         }
/* 1727 */         this.requests.set(paramAuthenticationInfo.getHeaderName(), str2);
/* 1728 */         this.currentProxyCredentials = paramAuthenticationInfo;
/* 1729 */         return paramAuthenticationInfo;
/*      */       }
/* 1731 */       paramAuthenticationInfo.removeFromCache();
/*      */     }
/*      */ 
/* 1734 */     paramAuthenticationInfo = getHttpProxyAuthentication(paramAuthenticationHeader);
/* 1735 */     this.currentProxyCredentials = paramAuthenticationInfo;
/* 1736 */     return paramAuthenticationInfo;
/*      */   }
/*      */ 
/*      */   TunnelState tunnelState()
/*      */   {
/* 1745 */     return this.tunnelState;
/*      */   }
/*      */ 
/*      */   public void setTunnelState(TunnelState paramTunnelState)
/*      */   {
/* 1754 */     this.tunnelState = paramTunnelState;
/*      */   }
/*      */ 
/*      */   public synchronized void doTunneling()
/*      */     throws IOException
/*      */   {
/* 1761 */     int i = 0;
/* 1762 */     String str1 = "";
/* 1763 */     int j = 0;
/* 1764 */     AuthenticationInfo localAuthenticationInfo = null;
/* 1765 */     String str2 = null;
/* 1766 */     int k = -1;
/*      */ 
/* 1769 */     MessageHeader localMessageHeader = this.requests;
/* 1770 */     this.requests = new MessageHeader();
/*      */ 
/* 1773 */     int m = 0;
/*      */     try
/*      */     {
/* 1777 */       setTunnelState(TunnelState.SETUP);
/*      */       do
/*      */       {
/* 1780 */         if (!checkReuseConnection()) {
/* 1781 */           proxiedConnect(this.url, str2, k, false);
/*      */         }
/*      */ 
/* 1785 */         sendCONNECTRequest();
/* 1786 */         this.responses.reset();
/*      */ 
/* 1790 */         this.http.parseHTTP(this.responses, null, this);
/*      */ 
/* 1793 */         if (logger.isLoggable(500)) {
/* 1794 */           logger.fine(this.responses.toString());
/*      */         }
/*      */ 
/* 1797 */         if ((this.responses.filterNTLMResponses("Proxy-Authenticate")) && 
/* 1798 */           (logger.isLoggable(500))) {
/* 1799 */           logger.fine(">>>> Headers are filtered");
/* 1800 */           logger.fine(this.responses.toString());
/*      */         }
/*      */ 
/* 1804 */         str1 = this.responses.getValue(0);
/* 1805 */         StringTokenizer localStringTokenizer = new StringTokenizer(str1);
/* 1806 */         localStringTokenizer.nextToken();
/* 1807 */         j = Integer.parseInt(localStringTokenizer.nextToken().trim());
/* 1808 */         if (j == 407)
/*      */         {
/* 1810 */           boolean bool = false;
/* 1811 */           Iterator localIterator = this.responses.multiValueIterator("Proxy-Authenticate");
/* 1812 */           while (localIterator.hasNext()) {
/* 1813 */             localObject1 = ((String)localIterator.next()).trim();
/* 1814 */             if ((((String)localObject1).equalsIgnoreCase("Negotiate")) || (((String)localObject1).equalsIgnoreCase("Kerberos")))
/*      */             {
/* 1816 */               if (m == 0) {
/* 1817 */                 m = 1; break;
/*      */               }
/* 1819 */               bool = true;
/* 1820 */               this.doingNTLMp2ndStage = false;
/* 1821 */               localAuthenticationInfo = null;
/*      */ 
/* 1823 */               break;
/*      */             }
/*      */           }
/*      */ 
/* 1827 */           Object localObject1 = new AuthenticationHeader("Proxy-Authenticate", this.responses, new HttpCallerInfo(this.url, this.http.getProxyHostUsed(), this.http.getProxyPortUsed()), bool);
/*      */ 
/* 1833 */           if (!this.doingNTLMp2ndStage) {
/* 1834 */             localAuthenticationInfo = resetProxyAuthentication(localAuthenticationInfo, (AuthenticationHeader)localObject1);
/*      */ 
/* 1836 */             if (localAuthenticationInfo != null) {
/* 1837 */               str2 = this.http.getProxyHostUsed();
/* 1838 */               k = this.http.getProxyPortUsed();
/* 1839 */               disconnectInternal();
/* 1840 */               i++;
/* 1841 */               continue;
/*      */             }
/*      */           } else {
/* 1844 */             String str3 = this.responses.findValue("Proxy-Authenticate");
/* 1845 */             reset();
/* 1846 */             if (!localAuthenticationInfo.setHeaders(this, ((AuthenticationHeader)localObject1).headerParser(), str3))
/*      */             {
/* 1848 */               disconnectInternal();
/* 1849 */               throw new IOException("Authentication failure");
/*      */             }
/* 1851 */             this.authObj = null;
/* 1852 */             this.doingNTLMp2ndStage = false;
/* 1853 */             continue;
/*      */           }
/*      */         }
/*      */ 
/* 1857 */         if (localAuthenticationInfo != null)
/*      */         {
/* 1859 */           localAuthenticationInfo.addToCache();
/*      */         }
/*      */ 
/* 1862 */         if (j == 200) {
/* 1863 */           setTunnelState(TunnelState.TUNNELING);
/* 1864 */           break;
/*      */         }
/*      */ 
/* 1868 */         disconnectInternal();
/* 1869 */         setTunnelState(TunnelState.NONE);
/* 1870 */         break;
/* 1871 */       }while (i < maxRedirects);
/*      */ 
/* 1873 */       if ((i >= maxRedirects) || (j != 200)) {
/* 1874 */         throw new IOException("Unable to tunnel through proxy. Proxy returns \"" + str1 + "\"");
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1879 */       if (this.proxyAuthKey != null) {
/* 1880 */         AuthenticationInfo.endAuthRequest(this.proxyAuthKey);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1885 */     this.requests = localMessageHeader;
/*      */ 
/* 1888 */     this.responses.reset();
/*      */   }
/*      */ 
/*      */   static String connectRequestURI(URL paramURL) {
/* 1892 */     String str = paramURL.getHost();
/* 1893 */     int i = paramURL.getPort();
/* 1894 */     i = i != -1 ? i : paramURL.getDefaultPort();
/*      */ 
/* 1896 */     return str + ":" + i;
/*      */   }
/*      */ 
/*      */   private void sendCONNECTRequest()
/*      */     throws IOException
/*      */   {
/* 1903 */     int i = this.url.getPort();
/*      */ 
/* 1905 */     this.requests.set(0, HTTP_CONNECT + " " + connectRequestURI(this.url) + " " + "HTTP/1.1", null);
/*      */ 
/* 1907 */     this.requests.setIfNotSet("User-Agent", userAgent);
/*      */ 
/* 1909 */     String str = this.url.getHost();
/* 1910 */     if ((i != -1) && (i != this.url.getDefaultPort())) {
/* 1911 */       str = str + ":" + String.valueOf(i);
/*      */     }
/* 1913 */     this.requests.setIfNotSet("Host", str);
/*      */ 
/* 1916 */     this.requests.setIfNotSet("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
/*      */ 
/* 1918 */     if (this.http.getHttpKeepAliveSet()) {
/* 1919 */       this.requests.setIfNotSet("Proxy-Connection", "keep-alive");
/*      */     }
/*      */ 
/* 1922 */     setPreemptiveProxyAuthentication(this.requests);
/*      */ 
/* 1925 */     if (logger.isLoggable(500)) {
/* 1926 */       logger.fine(this.requests.toString());
/*      */     }
/*      */ 
/* 1929 */     this.http.writeRequests(this.requests, null);
/*      */   }
/*      */ 
/*      */   private void setPreemptiveProxyAuthentication(MessageHeader paramMessageHeader)
/*      */     throws IOException
/*      */   {
/* 1936 */     AuthenticationInfo localAuthenticationInfo = AuthenticationInfo.getProxyAuth(this.http.getProxyHostUsed(), this.http.getProxyPortUsed());
/*      */ 
/* 1939 */     if ((localAuthenticationInfo != null) && (localAuthenticationInfo.supportsPreemptiveAuthorization()))
/*      */     {
/*      */       String str;
/* 1941 */       if ((localAuthenticationInfo instanceof DigestAuthentication)) {
/* 1942 */         DigestAuthentication localDigestAuthentication = (DigestAuthentication)localAuthenticationInfo;
/* 1943 */         if (tunnelState() == TunnelState.SETUP) {
/* 1944 */           str = localDigestAuthentication.getHeaderValue(connectRequestURI(this.url), HTTP_CONNECT);
/*      */         }
/*      */         else
/* 1947 */           str = localDigestAuthentication.getHeaderValue(getRequestURI(), this.method);
/*      */       }
/*      */       else {
/* 1950 */         str = localAuthenticationInfo.getHeaderValue(this.url, this.method);
/*      */       }
/*      */ 
/* 1954 */       paramMessageHeader.set(localAuthenticationInfo.getHeaderName(), str);
/* 1955 */       this.currentProxyCredentials = localAuthenticationInfo;
/*      */     }
/*      */   }
/*      */ 
/*      */   private AuthenticationInfo getHttpProxyAuthentication(AuthenticationHeader paramAuthenticationHeader)
/*      */   {
/* 1965 */     Object localObject1 = null;
/* 1966 */     String str1 = paramAuthenticationHeader.raw();
/* 1967 */     String str2 = this.http.getProxyHostUsed();
/* 1968 */     int i = this.http.getProxyPortUsed();
/* 1969 */     if ((str2 != null) && (paramAuthenticationHeader.isPresent())) {
/* 1970 */       HeaderParser localHeaderParser = paramAuthenticationHeader.headerParser();
/* 1971 */       String str3 = localHeaderParser.findValue("realm");
/* 1972 */       String str4 = paramAuthenticationHeader.scheme();
/* 1973 */       AuthScheme localAuthScheme = AuthScheme.UNKNOWN;
/* 1974 */       if ("basic".equalsIgnoreCase(str4)) {
/* 1975 */         localAuthScheme = AuthScheme.BASIC;
/* 1976 */       } else if ("digest".equalsIgnoreCase(str4)) {
/* 1977 */         localAuthScheme = AuthScheme.DIGEST;
/* 1978 */       } else if ("ntlm".equalsIgnoreCase(str4)) {
/* 1979 */         localAuthScheme = AuthScheme.NTLM;
/* 1980 */         this.doingNTLMp2ndStage = true;
/* 1981 */       } else if ("Kerberos".equalsIgnoreCase(str4)) {
/* 1982 */         localAuthScheme = AuthScheme.KERBEROS;
/* 1983 */         this.doingNTLMp2ndStage = true;
/* 1984 */       } else if ("Negotiate".equalsIgnoreCase(str4)) {
/* 1985 */         localAuthScheme = AuthScheme.NEGOTIATE;
/* 1986 */         this.doingNTLMp2ndStage = true;
/*      */       }
/*      */ 
/* 1989 */       if (str3 == null)
/* 1990 */         str3 = "";
/* 1991 */       this.proxyAuthKey = AuthenticationInfo.getProxyAuthKey(str2, i, str3, localAuthScheme);
/* 1992 */       localObject1 = AuthenticationInfo.getProxyAuth(this.proxyAuthKey);
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 1993 */       if (localObject1 == null) {
/* 1994 */         switch (8.$SwitchMap$sun$net$www$protocol$http$AuthScheme[localAuthScheme.ordinal()]) {
/*      */         case 1:
/* 1996 */           localObject2 = null;
/*      */           try {
/* 1998 */             final String str5 = str2;
/* 1999 */             localObject2 = (InetAddress)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */             {
/*      */               public InetAddress run() throws UnknownHostException
/*      */               {
/* 2003 */                 return InetAddress.getByName(str5);
/*      */               }
/*      */             });
/*      */           }
/*      */           catch (PrivilegedActionException localPrivilegedActionException) {
/*      */           }
/* 2009 */           localObject3 = privilegedRequestPasswordAuthentication(str2, (InetAddress)localObject2, i, "http", str3, str4, this.url, Authenticator.RequestorType.PROXY);
/*      */ 
/* 2013 */           if (localObject3 != null)
/* 2014 */             localObject1 = new BasicAuthentication(true, str2, i, str3, (PasswordAuthentication)localObject3); break;
/*      */         case 2:
/* 2018 */           localObject3 = privilegedRequestPasswordAuthentication(str2, null, i, this.url.getProtocol(), str3, str4, this.url, Authenticator.RequestorType.PROXY);
/*      */ 
/* 2021 */           if (localObject3 != null) {
/* 2022 */             DigestAuthentication.Parameters localParameters = new DigestAuthentication.Parameters();
/*      */ 
/* 2024 */             localObject1 = new DigestAuthentication(true, str2, i, str3, str4, (PasswordAuthentication)localObject3, localParameters);
/*      */           }
/* 2026 */           break;
/*      */         case 3:
/* 2029 */           if (NTLMAuthenticationProxy.supported)
/*      */           {
/* 2033 */             if (this.tryTransparentNTLMProxy) {
/* 2034 */               this.tryTransparentNTLMProxy = NTLMAuthenticationProxy.supportsTransparentAuth;
/*      */ 
/* 2041 */               if ((this.tryTransparentNTLMProxy) && (this.useProxyResponseCode)) {
/* 2042 */                 this.tryTransparentNTLMProxy = false;
/*      */               }
/*      */             }
/* 2045 */             localObject3 = null;
/* 2046 */             if (this.tryTransparentNTLMProxy)
/* 2047 */               logger.finest("Trying Transparent NTLM authentication");
/*      */             else {
/* 2049 */               localObject3 = privilegedRequestPasswordAuthentication(str2, null, i, this.url.getProtocol(), "", str4, this.url, Authenticator.RequestorType.PROXY);
/*      */             }
/*      */ 
/* 2059 */             if ((this.tryTransparentNTLMProxy) || ((!this.tryTransparentNTLMProxy) && (localObject3 != null)))
/*      */             {
/* 2061 */               localObject1 = NTLMAuthenticationProxy.proxy.create(true, str2, i, (PasswordAuthentication)localObject3);
/*      */             }
/*      */ 
/* 2065 */             this.tryTransparentNTLMProxy = false; } break;
/*      */         case 4:
/* 2069 */           localObject1 = new NegotiateAuthentication(new HttpCallerInfo(paramAuthenticationHeader.getHttpCallerInfo(), "Negotiate"));
/* 2070 */           break;
/*      */         case 5:
/* 2072 */           localObject1 = new NegotiateAuthentication(new HttpCallerInfo(paramAuthenticationHeader.getHttpCallerInfo(), "Kerberos"));
/* 2073 */           break;
/*      */         case 6:
/* 2075 */           logger.finest("Unknown/Unsupported authentication scheme: " + str4);
/*      */         default:
/* 2077 */           throw new AssertionError("should not reach here");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2083 */       if ((localObject1 == null) && (defaultAuth != null) && (defaultAuth.schemeSupported(str4))) {
/*      */         try
/*      */         {
/* 2086 */           localObject2 = new URL("http", str2, i, "/");
/* 2087 */           localObject3 = defaultAuth.authString((URL)localObject2, str4, str3);
/* 2088 */           if (localObject3 != null)
/* 2089 */             localObject1 = new BasicAuthentication(true, str2, i, str3, (String)localObject3);
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException)
/*      */         {
/*      */         }
/*      */       }
/* 2095 */       if ((localObject1 != null) && 
/* 2096 */         (!((AuthenticationInfo)localObject1).setHeaders(this, localHeaderParser, str1))) {
/* 2097 */         localObject1 = null;
/*      */       }
/*      */     }
/*      */ 
/* 2101 */     if (logger.isLoggable(400)) {
/* 2102 */       logger.finer("Proxy Authentication for " + paramAuthenticationHeader.toString() + " returned " + (localObject1 != null ? localObject1.toString() : "null"));
/*      */     }
/* 2104 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private AuthenticationInfo getServerAuthentication(AuthenticationHeader paramAuthenticationHeader)
/*      */   {
/* 2115 */     Object localObject1 = null;
/* 2116 */     String str1 = paramAuthenticationHeader.raw();
/*      */ 
/* 2118 */     if (paramAuthenticationHeader.isPresent()) {
/* 2119 */       HeaderParser localHeaderParser = paramAuthenticationHeader.headerParser();
/* 2120 */       String str2 = localHeaderParser.findValue("realm");
/* 2121 */       String str3 = paramAuthenticationHeader.scheme();
/* 2122 */       AuthScheme localAuthScheme = AuthScheme.UNKNOWN;
/* 2123 */       if ("basic".equalsIgnoreCase(str3)) {
/* 2124 */         localAuthScheme = AuthScheme.BASIC;
/* 2125 */       } else if ("digest".equalsIgnoreCase(str3)) {
/* 2126 */         localAuthScheme = AuthScheme.DIGEST;
/* 2127 */       } else if ("ntlm".equalsIgnoreCase(str3)) {
/* 2128 */         localAuthScheme = AuthScheme.NTLM;
/* 2129 */         this.doingNTLM2ndStage = true;
/* 2130 */       } else if ("Kerberos".equalsIgnoreCase(str3)) {
/* 2131 */         localAuthScheme = AuthScheme.KERBEROS;
/* 2132 */         this.doingNTLM2ndStage = true;
/* 2133 */       } else if ("Negotiate".equalsIgnoreCase(str3)) {
/* 2134 */         localAuthScheme = AuthScheme.NEGOTIATE;
/* 2135 */         this.doingNTLM2ndStage = true;
/*      */       }
/*      */ 
/* 2138 */       this.domain = localHeaderParser.findValue("domain");
/* 2139 */       if (str2 == null)
/* 2140 */         str2 = "";
/* 2141 */       this.serverAuthKey = AuthenticationInfo.getServerAuthKey(this.url, str2, localAuthScheme);
/* 2142 */       localObject1 = AuthenticationInfo.getServerAuth(this.serverAuthKey);
/* 2143 */       InetAddress localInetAddress = null;
/* 2144 */       if (localObject1 == null) {
/*      */         try {
/* 2146 */           localInetAddress = InetAddress.getByName(this.url.getHost());
/*      */         }
/*      */         catch (UnknownHostException localUnknownHostException)
/*      */         {
/*      */         }
/*      */       }
/* 2152 */       int i = this.url.getPort();
/* 2153 */       if (i == -1)
/* 2154 */         i = this.url.getDefaultPort();
/*      */       Object localObject2;
/* 2156 */       if (localObject1 == null) {
/* 2157 */         switch (8.$SwitchMap$sun$net$www$protocol$http$AuthScheme[localAuthScheme.ordinal()]) {
/*      */         case 5:
/* 2159 */           localObject1 = new NegotiateAuthentication(new HttpCallerInfo(paramAuthenticationHeader.getHttpCallerInfo(), "Kerberos"));
/* 2160 */           break;
/*      */         case 4:
/* 2162 */           localObject1 = new NegotiateAuthentication(new HttpCallerInfo(paramAuthenticationHeader.getHttpCallerInfo(), "Negotiate"));
/* 2163 */           break;
/*      */         case 1:
/* 2165 */           localObject2 = privilegedRequestPasswordAuthentication(this.url.getHost(), localInetAddress, i, this.url.getProtocol(), str2, str3, this.url, Authenticator.RequestorType.SERVER);
/*      */ 
/* 2169 */           if (localObject2 != null)
/* 2170 */             localObject1 = new BasicAuthentication(false, this.url, str2, (PasswordAuthentication)localObject2); break;
/*      */         case 2:
/* 2174 */           localObject2 = privilegedRequestPasswordAuthentication(this.url.getHost(), localInetAddress, i, this.url.getProtocol(), str2, str3, this.url, Authenticator.RequestorType.SERVER);
/*      */ 
/* 2177 */           if (localObject2 != null) {
/* 2178 */             this.digestparams = new DigestAuthentication.Parameters();
/* 2179 */             localObject1 = new DigestAuthentication(false, this.url, str2, str3, (PasswordAuthentication)localObject2, this.digestparams); } break;
/*      */         case 3:
/* 2183 */           if (NTLMAuthenticationProxy.supported) {
/*      */             URL localURL;
/*      */             try {
/* 2186 */               localURL = new URL(this.url, "/");
/*      */             } catch (Exception localException) {
/* 2188 */               localURL = this.url;
/*      */             }
/*      */ 
/* 2194 */             if (this.tryTransparentNTLMServer) {
/* 2195 */               this.tryTransparentNTLMServer = NTLMAuthenticationProxy.supportsTransparentAuth;
/*      */ 
/* 2200 */               if (this.tryTransparentNTLMServer) {
/* 2201 */                 this.tryTransparentNTLMServer = NTLMAuthenticationProxy.isTrustedSite(this.url);
/*      */               }
/*      */             }
/*      */ 
/* 2205 */             localObject2 = null;
/* 2206 */             if (this.tryTransparentNTLMServer)
/* 2207 */               logger.finest("Trying Transparent NTLM authentication");
/*      */             else {
/* 2209 */               localObject2 = privilegedRequestPasswordAuthentication(this.url.getHost(), localInetAddress, i, this.url.getProtocol(), "", str3, this.url, Authenticator.RequestorType.SERVER);
/*      */             }
/*      */ 
/* 2220 */             if ((this.tryTransparentNTLMServer) || ((!this.tryTransparentNTLMServer) && (localObject2 != null)))
/*      */             {
/* 2222 */               localObject1 = NTLMAuthenticationProxy.proxy.create(false, localURL, (PasswordAuthentication)localObject2);
/*      */             }
/*      */ 
/* 2226 */             this.tryTransparentNTLMServer = false;
/* 2227 */           }break;
/*      */         case 6:
/* 2230 */           logger.finest("Unknown/Unsupported authentication scheme: " + str3);
/*      */         default:
/* 2232 */           throw new AssertionError("should not reach here");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2239 */       if ((localObject1 == null) && (defaultAuth != null) && (defaultAuth.schemeSupported(str3)))
/*      */       {
/* 2241 */         localObject2 = defaultAuth.authString(this.url, str3, str2);
/* 2242 */         if (localObject2 != null) {
/* 2243 */           localObject1 = new BasicAuthentication(false, this.url, str2, (String)localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2248 */       if ((localObject1 != null) && 
/* 2249 */         (!((AuthenticationInfo)localObject1).setHeaders(this, localHeaderParser, str1))) {
/* 2250 */         localObject1 = null;
/*      */       }
/*      */     }
/*      */ 
/* 2254 */     if (logger.isLoggable(400)) {
/* 2255 */       logger.finer("Server Authentication for " + paramAuthenticationHeader.toString() + " returned " + (localObject1 != null ? localObject1.toString() : "null"));
/*      */     }
/* 2257 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void checkResponseCredentials(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 2267 */       if (!this.needToCheck)
/*      */         return;
/*      */       String str;
/*      */       DigestAuthentication localDigestAuthentication;
/* 2269 */       if ((validateProxy) && (this.currentProxyCredentials != null) && ((this.currentProxyCredentials instanceof DigestAuthentication)))
/*      */       {
/* 2271 */         str = this.responses.findValue("Proxy-Authentication-Info");
/* 2272 */         if ((paramBoolean) || (str != null)) {
/* 2273 */           localDigestAuthentication = (DigestAuthentication)this.currentProxyCredentials;
/*      */ 
/* 2275 */           localDigestAuthentication.checkResponse(str, this.method, getRequestURI());
/* 2276 */           this.currentProxyCredentials = null;
/*      */         }
/*      */       }
/* 2279 */       if ((validateServer) && (this.currentServerCredentials != null) && ((this.currentServerCredentials instanceof DigestAuthentication)))
/*      */       {
/* 2281 */         str = this.responses.findValue("Authentication-Info");
/* 2282 */         if ((paramBoolean) || (str != null)) {
/* 2283 */           localDigestAuthentication = (DigestAuthentication)this.currentServerCredentials;
/*      */ 
/* 2285 */           localDigestAuthentication.checkResponse(str, this.method, this.url);
/* 2286 */           this.currentServerCredentials = null;
/*      */         }
/*      */       }
/* 2289 */       if ((this.currentServerCredentials == null) && (this.currentProxyCredentials == null))
/* 2290 */         this.needToCheck = false;
/*      */     }
/*      */     catch (IOException localIOException) {
/* 2293 */       disconnectInternal();
/* 2294 */       this.connected = false;
/* 2295 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   String getRequestURI()
/*      */     throws IOException
/*      */   {
/* 2306 */     if (this.requestURI == null) {
/* 2307 */       this.requestURI = this.http.getURLFile();
/*      */     }
/* 2309 */     return this.requestURI;
/*      */   }
/*      */ 
/*      */   private boolean followRedirect()
/*      */     throws IOException
/*      */   {
/* 2318 */     if (!getInstanceFollowRedirects()) {
/* 2319 */       return false;
/*      */     }
/*      */ 
/* 2322 */     int i = getResponseCode();
/* 2323 */     if ((i < 300) || (i > 307) || (i == 306) || (i == 304))
/*      */     {
/* 2325 */       return false;
/*      */     }
/* 2327 */     String str1 = getHeaderField("Location");
/* 2328 */     if (str1 == null)
/*      */     {
/* 2332 */       return false;
/*      */     }
/*      */     URL localURL;
/*      */     try {
/* 2336 */       localURL = new URL(str1);
/* 2337 */       if (!this.url.getProtocol().equalsIgnoreCase(localURL.getProtocol())) {
/* 2338 */         return false;
/*      */       }
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException)
/*      */     {
/* 2343 */       localURL = new URL(this.url, str1);
/*      */     }
/* 2345 */     disconnectInternal();
/* 2346 */     if (streaming()) {
/* 2347 */       throw new HttpRetryException("cannot retry due to redirection, in streaming mode", i, str1);
/*      */     }
/* 2349 */     if (logger.isLoggable(500)) {
/* 2350 */       logger.fine("Redirected from " + this.url + " to " + localURL);
/*      */     }
/*      */ 
/* 2354 */     this.responses = new MessageHeader();
/* 2355 */     if (i == 305)
/*      */     {
/* 2364 */       String str2 = localURL.getHost();
/* 2365 */       int k = localURL.getPort();
/*      */ 
/* 2367 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 2368 */       if (localSecurityManager != null) {
/* 2369 */         localSecurityManager.checkConnect(str2, k);
/*      */       }
/*      */ 
/* 2372 */       setProxiedClient(this.url, str2, k);
/* 2373 */       this.requests.set(0, this.method + " " + getRequestURI() + " " + "HTTP/1.1", null);
/*      */ 
/* 2375 */       this.connected = true;
/*      */ 
/* 2379 */       this.useProxyResponseCode = true;
/*      */     }
/*      */     else
/*      */     {
/* 2383 */       this.url = localURL;
/* 2384 */       this.requestURI = null;
/* 2385 */       if ((this.method.equals("POST")) && (!Boolean.getBoolean("http.strictPostRedirect")) && (i != 307))
/*      */       {
/* 2403 */         this.requests = new MessageHeader();
/* 2404 */         this.setRequests = false;
/* 2405 */         setRequestMethod("GET");
/* 2406 */         this.poster = null;
/* 2407 */         if (!checkReuseConnection())
/* 2408 */           connect();
/*      */       } else {
/* 2410 */         if (!checkReuseConnection()) {
/* 2411 */           connect();
/*      */         }
/*      */ 
/* 2421 */         if (this.http != null) {
/* 2422 */           this.requests.set(0, this.method + " " + getRequestURI() + " " + "HTTP/1.1", null);
/*      */ 
/* 2424 */           int j = this.url.getPort();
/* 2425 */           String str3 = this.url.getHost();
/* 2426 */           if ((j != -1) && (j != this.url.getDefaultPort())) {
/* 2427 */             str3 = str3 + ":" + String.valueOf(j);
/*      */           }
/* 2429 */           this.requests.set("Host", str3);
/*      */         }
/*      */       }
/*      */     }
/* 2433 */     return true;
/*      */   }
/*      */ 
/*      */   private void reset()
/*      */     throws IOException
/*      */   {
/* 2443 */     this.http.reuse = true;
/*      */ 
/* 2445 */     this.reuseClient = this.http;
/* 2446 */     InputStream localInputStream = this.http.getInputStream();
/* 2447 */     if (!this.method.equals("HEAD"))
/*      */     {
/*      */       try
/*      */       {
/* 2453 */         if (((localInputStream instanceof ChunkedInputStream)) || ((localInputStream instanceof MeteredStream)));
/* 2456 */         while (localInputStream.read(this.cdata) > 0) { continue;
/*      */ 
/* 2461 */           long l1 = 0L;
/* 2462 */           i = 0;
/* 2463 */           String str = this.responses.findValue("Content-Length");
/* 2464 */           if (str != null) {
/*      */             try {
/* 2466 */               l1 = Long.parseLong(str);
/*      */             } catch (NumberFormatException localNumberFormatException) {
/* 2468 */               l1 = 0L;
/*      */             }
/*      */           }
/* 2471 */           for (l2 = 0L; (l2 < l1) && 
/* 2472 */             ((i = localInputStream.read(this.cdata)) != -1); )
/*      */           {
/* 2475 */             l2 += i;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException1)
/*      */       {
/*      */         int i;
/*      */         long l2;
/* 2480 */         this.http.reuse = false;
/* 2481 */         this.reuseClient = null;
/* 2482 */         disconnectInternal();
/* 2483 */         return;
/*      */       }
/*      */       try {
/* 2486 */         if ((localInputStream instanceof MeteredStream))
/* 2487 */           localInputStream.close();
/*      */       } catch (IOException localIOException2) {
/*      */       }
/*      */     }
/* 2491 */     this.responseCode = -1;
/* 2492 */     this.responses = new MessageHeader();
/* 2493 */     this.connected = false;
/*      */   }
/*      */ 
/*      */   private void disconnectWeb()
/*      */     throws IOException
/*      */   {
/* 2502 */     if ((usingProxy()) && (this.http.isKeepingAlive())) {
/* 2503 */       this.responseCode = -1;
/*      */ 
/* 2506 */       reset();
/*      */     } else {
/* 2508 */       disconnectInternal();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void disconnectInternal()
/*      */   {
/* 2516 */     this.responseCode = -1;
/* 2517 */     this.inputStream = null;
/* 2518 */     if (this.pi != null) {
/* 2519 */       this.pi.finishTracking();
/* 2520 */       this.pi = null;
/*      */     }
/* 2522 */     if (this.http != null) {
/* 2523 */       this.http.closeServer();
/* 2524 */       this.http = null;
/* 2525 */       this.connected = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void disconnect()
/*      */   {
/* 2534 */     this.responseCode = -1;
/* 2535 */     if (this.pi != null) {
/* 2536 */       this.pi.finishTracking();
/* 2537 */       this.pi = null;
/*      */     }
/*      */ 
/* 2540 */     if (this.http != null)
/*      */     {
/* 2566 */       if (this.inputStream != null) {
/* 2567 */         HttpClient localHttpClient = this.http;
/*      */ 
/* 2570 */         boolean bool = localHttpClient.isKeepingAlive();
/*      */         try
/*      */         {
/* 2573 */           this.inputStream.close();
/*      */         }
/*      */         catch (IOException localIOException)
/*      */         {
/*      */         }
/*      */ 
/* 2581 */         if (bool) {
/* 2582 */           localHttpClient.closeIdleConnection();
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2590 */         this.http.setDoNotRetry(true);
/*      */ 
/* 2592 */         this.http.closeServer();
/*      */       }
/*      */ 
/* 2596 */       this.http = null;
/* 2597 */       this.connected = false;
/*      */     }
/* 2599 */     this.cachedInputStream = null;
/* 2600 */     if (this.cachedHeaders != null)
/* 2601 */       this.cachedHeaders.reset();
/*      */   }
/*      */ 
/*      */   public boolean usingProxy()
/*      */   {
/* 2606 */     if (this.http != null) {
/* 2607 */       return this.http.getProxyHostUsed() != null;
/*      */     }
/* 2609 */     return false;
/*      */   }
/*      */ 
/*      */   private String filterHeaderField(String paramString1, String paramString2)
/*      */   {
/* 2623 */     if (paramString2 == null) {
/* 2624 */       return null;
/*      */     }
/* 2626 */     if (("set-cookie".equalsIgnoreCase(paramString1)) || ("set-cookie2".equalsIgnoreCase(paramString1)))
/*      */     {
/* 2630 */       if (this.cookieHandler == null) {
/* 2631 */         return paramString2;
/*      */       }
/* 2633 */       JavaNetHttpCookieAccess localJavaNetHttpCookieAccess = SharedSecrets.getJavaNetHttpCookieAccess();
/*      */ 
/* 2635 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2636 */       List localList = localJavaNetHttpCookieAccess.parse(paramString2);
/* 2637 */       int i = 0;
/* 2638 */       for (HttpCookie localHttpCookie : localList)
/*      */       {
/* 2640 */         if (!localHttpCookie.isHttpOnly())
/*      */         {
/* 2642 */           if (i != 0)
/* 2643 */             localStringBuilder.append(',');
/* 2644 */           localStringBuilder.append(localJavaNetHttpCookieAccess.header(localHttpCookie));
/* 2645 */           i = 1;
/*      */         }
/*      */       }
/* 2648 */       return localStringBuilder.length() == 0 ? "" : localStringBuilder.toString();
/*      */     }
/*      */ 
/* 2651 */     return paramString2;
/*      */   }
/*      */ 
/*      */   private Map<String, List<String>> getFilteredHeaderFields()
/*      */   {
/* 2659 */     if (this.filteredHeaders != null) {
/* 2660 */       return this.filteredHeaders;
/*      */     }
/* 2662 */     HashMap localHashMap = new HashMap();
/*      */     Map localMap;
/* 2664 */     if (this.cachedHeaders != null)
/* 2665 */       localMap = this.cachedHeaders.getHeaders();
/*      */     else {
/* 2667 */       localMap = this.responses.getHeaders();
/*      */     }
/* 2669 */     for (Map.Entry localEntry : localMap.entrySet()) {
/* 2670 */       String str1 = (String)localEntry.getKey();
/* 2671 */       List localList = (List)localEntry.getValue(); ArrayList localArrayList = new ArrayList();
/* 2672 */       for (String str2 : localList) {
/* 2673 */         String str3 = filterHeaderField(str1, str2);
/* 2674 */         if (str3 != null)
/* 2675 */           localArrayList.add(str3);
/*      */       }
/* 2677 */       if (!localArrayList.isEmpty()) {
/* 2678 */         localHashMap.put(str1, Collections.unmodifiableList(localArrayList));
/*      */       }
/*      */     }
/* 2681 */     return this.filteredHeaders = Collections.unmodifiableMap(localHashMap);
/*      */   }
/*      */ 
/*      */   public String getHeaderField(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 2691 */       getInputStream();
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 2694 */     if (this.cachedHeaders != null) {
/* 2695 */       return filterHeaderField(paramString, this.cachedHeaders.findValue(paramString));
/*      */     }
/*      */ 
/* 2698 */     return filterHeaderField(paramString, this.responses.findValue(paramString));
/*      */   }
/*      */ 
/*      */   public Map<String, List<String>> getHeaderFields()
/*      */   {
/*      */     try
/*      */     {
/* 2714 */       getInputStream();
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 2717 */     return getFilteredHeaderFields();
/*      */   }
/*      */ 
/*      */   public String getHeaderField(int paramInt)
/*      */   {
/*      */     try
/*      */     {
/* 2727 */       getInputStream();
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 2730 */     if (this.cachedHeaders != null) {
/* 2731 */       return filterHeaderField(this.cachedHeaders.getKey(paramInt), this.cachedHeaders.getValue(paramInt));
/*      */     }
/*      */ 
/* 2734 */     return filterHeaderField(this.responses.getKey(paramInt), this.responses.getValue(paramInt));
/*      */   }
/*      */ 
/*      */   public String getHeaderFieldKey(int paramInt)
/*      */   {
/*      */     try
/*      */     {
/* 2744 */       getInputStream();
/*      */     } catch (IOException localIOException) {
/*      */     }
/* 2747 */     if (this.cachedHeaders != null) {
/* 2748 */       return this.cachedHeaders.getKey(paramInt);
/*      */     }
/*      */ 
/* 2751 */     return this.responses.getKey(paramInt);
/*      */   }
/*      */ 
/*      */   public void setRequestProperty(String paramString1, String paramString2)
/*      */   {
/* 2761 */     if (this.connected)
/* 2762 */       throw new IllegalStateException("Already connected");
/* 2763 */     if (paramString1 == null) {
/* 2764 */       throw new NullPointerException("key is null");
/*      */     }
/* 2766 */     if (isExternalMessageHeaderAllowed(paramString1, paramString2))
/* 2767 */       this.requests.set(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public void addRequestProperty(String paramString1, String paramString2)
/*      */   {
/* 2784 */     if (this.connected)
/* 2785 */       throw new IllegalStateException("Already connected");
/* 2786 */     if (paramString1 == null) {
/* 2787 */       throw new NullPointerException("key is null");
/*      */     }
/* 2789 */     if (isExternalMessageHeaderAllowed(paramString1, paramString2))
/* 2790 */       this.requests.add(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public void setAuthenticationProperty(String paramString1, String paramString2)
/*      */   {
/* 2799 */     checkMessageHeader(paramString1, paramString2);
/* 2800 */     this.requests.set(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public synchronized String getRequestProperty(String paramString)
/*      */   {
/* 2805 */     if (paramString == null) {
/* 2806 */       return null;
/*      */     }
/*      */ 
/* 2810 */     for (int i = 0; i < EXCLUDE_HEADERS.length; i++) {
/* 2811 */       if (paramString.equalsIgnoreCase(EXCLUDE_HEADERS[i])) {
/* 2812 */         return null;
/*      */       }
/*      */     }
/* 2815 */     if (!this.setUserCookies) {
/* 2816 */       if (paramString.equalsIgnoreCase("Cookie")) {
/* 2817 */         return this.userCookies;
/*      */       }
/* 2819 */       if (paramString.equalsIgnoreCase("Cookie2")) {
/* 2820 */         return this.userCookies2;
/*      */       }
/*      */     }
/* 2823 */     return this.requests.findValue(paramString);
/*      */   }
/*      */ 
/*      */   public synchronized Map<String, List<String>> getRequestProperties()
/*      */   {
/* 2840 */     if (this.connected) {
/* 2841 */       throw new IllegalStateException("Already connected");
/*      */     }
/*      */ 
/* 2844 */     if (this.setUserCookies) {
/* 2845 */       return this.requests.getHeaders(EXCLUDE_HEADERS);
/*      */     }
/*      */ 
/* 2851 */     HashMap localHashMap = null;
/* 2852 */     if ((this.userCookies != null) || (this.userCookies2 != null)) {
/* 2853 */       localHashMap = new HashMap();
/* 2854 */       if (this.userCookies != null) {
/* 2855 */         localHashMap.put("Cookie", this.userCookies);
/*      */       }
/* 2857 */       if (this.userCookies2 != null) {
/* 2858 */         localHashMap.put("Cookie2", this.userCookies2);
/*      */       }
/*      */     }
/* 2861 */     return this.requests.filterAndAddHeaders(EXCLUDE_HEADERS2, localHashMap);
/*      */   }
/*      */ 
/*      */   public void setConnectTimeout(int paramInt)
/*      */   {
/* 2866 */     if (paramInt < 0)
/* 2867 */       throw new IllegalArgumentException("timeouts can't be negative");
/* 2868 */     this.connectTimeout = paramInt;
/*      */   }
/*      */ 
/*      */   public int getConnectTimeout()
/*      */   {
/* 2886 */     return this.connectTimeout < 0 ? 0 : this.connectTimeout;
/*      */   }
/*      */ 
/*      */   public void setReadTimeout(int paramInt)
/*      */   {
/* 2911 */     if (paramInt < 0)
/* 2912 */       throw new IllegalArgumentException("timeouts can't be negative");
/* 2913 */     this.readTimeout = paramInt;
/*      */   }
/*      */ 
/*      */   public int getReadTimeout()
/*      */   {
/* 2929 */     return this.readTimeout < 0 ? 0 : this.readTimeout;
/*      */   }
/*      */ 
/*      */   public CookieHandler getCookieHandler() {
/* 2933 */     return this.cookieHandler;
/*      */   }
/*      */ 
/*      */   String getMethod() {
/* 2937 */     return this.method;
/*      */   }
/*      */ 
/*      */   private MessageHeader mapToMessageHeader(Map<String, List<String>> paramMap) {
/* 2941 */     MessageHeader localMessageHeader = new MessageHeader();
/* 2942 */     if ((paramMap == null) || (paramMap.isEmpty())) {
/* 2943 */       return localMessageHeader;
/*      */     }
/* 2945 */     for (Map.Entry localEntry : paramMap.entrySet()) {
/* 2946 */       str1 = (String)localEntry.getKey();
/* 2947 */       List localList = (List)localEntry.getValue();
/* 2948 */       for (String str2 : localList)
/* 2949 */         if (str1 == null)
/* 2950 */           localMessageHeader.prepend(str1, str2);
/*      */         else
/* 2952 */           localMessageHeader.add(str1, str2);
/*      */     }
/*      */     String str1;
/* 2956 */     return localMessageHeader;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  198 */     maxRedirects = ((Integer)AccessController.doPrivileged(new GetIntegerAction("http.maxRedirects", 20))).intValue();
/*      */ 
/*  201 */     version = (String)AccessController.doPrivileged(new GetPropertyAction("java.version"));
/*      */ 
/*  203 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("http.agent"));
/*      */ 
/*  205 */     if (str == null)
/*  206 */       str = "Java/" + version;
/*      */     else {
/*  208 */       str = str + " Java/" + version;
/*      */     }
/*  210 */     userAgent = str;
/*  211 */     validateProxy = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("http.auth.digest.validateProxy"))).booleanValue();
/*      */ 
/*  214 */     validateServer = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("http.auth.digest.validateServer"))).booleanValue();
/*      */ 
/*  218 */     enableESBuffer = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.net.http.errorstream.enableBuffering"))).booleanValue();
/*      */ 
/*  221 */     timeout4ESBuffer = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.net.http.errorstream.timeout", 300))).intValue();
/*      */ 
/*  224 */     if (timeout4ESBuffer <= 0) {
/*  225 */       timeout4ESBuffer = 300;
/*      */     }
/*      */ 
/*  228 */     bufSize4ES = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.net.http.errorstream.bufferSize", 4096))).intValue();
/*      */ 
/*  231 */     if (bufSize4ES <= 0) {
/*  232 */       bufSize4ES = 4096;
/*      */     }
/*      */ 
/*  235 */     allowRestrictedHeaders = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.net.http.allowRestrictedHeaders"))).booleanValue();
/*      */ 
/*  238 */     if (!allowRestrictedHeaders) {
/*  239 */       restrictedHeaderSet = new HashSet(restrictedHeaders.length);
/*  240 */       for (int i = 0; i < restrictedHeaders.length; i++)
/*  241 */         restrictedHeaderSet.add(restrictedHeaders[i].toLowerCase());
/*      */     }
/*      */     else {
/*  244 */       restrictedHeaderSet = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ErrorStream extends InputStream
/*      */   {
/*      */     ByteBuffer buffer;
/*      */     InputStream is;
/*      */ 
/*      */     private ErrorStream(ByteBuffer paramByteBuffer)
/*      */     {
/* 3250 */       this.buffer = paramByteBuffer;
/* 3251 */       this.is = null;
/*      */     }
/*      */ 
/*      */     private ErrorStream(ByteBuffer paramByteBuffer, InputStream paramInputStream) {
/* 3255 */       this.buffer = paramByteBuffer;
/* 3256 */       this.is = paramInputStream;
/*      */     }
/*      */ 
/*      */     public static InputStream getErrorStream(InputStream paramInputStream, long paramLong, HttpClient paramHttpClient)
/*      */     {
/* 3264 */       if (paramLong == 0L) {
/* 3265 */         return null;
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 3271 */         int i = paramHttpClient.getReadTimeout();
/* 3272 */         paramHttpClient.setReadTimeout(HttpURLConnection.timeout4ESBuffer / 5);
/*      */ 
/* 3274 */         long l = 0L;
/* 3275 */         int j = 0;
/*      */ 
/* 3277 */         if (paramLong < 0L) {
/* 3278 */           l = HttpURLConnection.bufSize4ES;
/* 3279 */           j = 1;
/*      */         } else {
/* 3281 */           l = paramLong;
/*      */         }
/* 3283 */         if (l <= HttpURLConnection.bufSize4ES) {
/* 3284 */           int k = (int)l;
/* 3285 */           byte[] arrayOfByte = new byte[k];
/* 3286 */           int m = 0; int n = 0; int i1 = 0;
/*      */           do
/*      */             try {
/* 3289 */               i1 = paramInputStream.read(arrayOfByte, m, arrayOfByte.length - m);
/*      */ 
/* 3291 */               if (i1 < 0) {
/* 3292 */                 if (j != 0)
/*      */                 {
/*      */                   break;
/*      */                 }
/*      */ 
/* 3299 */                 throw new IOException("the server closes before sending " + paramLong + " bytes of data");
/*      */               }
/*      */ 
/* 3303 */               m += i1;
/*      */             } catch (SocketTimeoutException localSocketTimeoutException) {
/* 3305 */               n += HttpURLConnection.timeout4ESBuffer / 5;
/*      */             }
/* 3307 */           while ((m < k) && (n < HttpURLConnection.timeout4ESBuffer));
/*      */ 
/* 3310 */           paramHttpClient.setReadTimeout(i);
/*      */ 
/* 3314 */           if (m == 0)
/*      */           {
/* 3318 */             return null;
/* 3319 */           }if (((m == l) && (j == 0)) || ((j != 0) && (i1 < 0)))
/*      */           {
/* 3322 */             paramInputStream.close();
/* 3323 */             return new ErrorStream(ByteBuffer.wrap(arrayOfByte, 0, m));
/*      */           }
/*      */ 
/* 3326 */           return new ErrorStream(ByteBuffer.wrap(arrayOfByte, 0, m), paramInputStream);
/*      */         }
/*      */ 
/* 3330 */         return null;
/*      */       } catch (IOException localIOException) {
/*      */       }
/* 3333 */       return null;
/*      */     }
/*      */ 
/*      */     public int available()
/*      */       throws IOException
/*      */     {
/* 3339 */       if (this.is == null) {
/* 3340 */         return this.buffer.remaining();
/*      */       }
/* 3342 */       return this.buffer.remaining() + this.is.available();
/*      */     }
/*      */ 
/*      */     public int read() throws IOException
/*      */     {
/* 3347 */       byte[] arrayOfByte = new byte[1];
/* 3348 */       int i = read(arrayOfByte);
/* 3349 */       return i == -1 ? i : arrayOfByte[0] & 0xFF;
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte) throws IOException
/*      */     {
/* 3354 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 3359 */       int i = this.buffer.remaining();
/* 3360 */       if (i > 0) {
/* 3361 */         int j = i < paramInt2 ? i : paramInt2;
/* 3362 */         this.buffer.get(paramArrayOfByte, paramInt1, j);
/* 3363 */         return j;
/*      */       }
/* 3365 */       if (this.is == null) {
/* 3366 */         return -1;
/*      */       }
/* 3368 */       return this.is.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/* 3375 */       this.buffer = null;
/* 3376 */       if (this.is != null)
/* 3377 */         this.is.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   class HttpInputStream extends FilterInputStream
/*      */   {
/*      */     private CacheRequest cacheRequest;
/*      */     private OutputStream outputStream;
/* 2966 */     private boolean marked = false;
/* 2967 */     private int inCache = 0;
/* 2968 */     private int markCount = 0;
/*      */     private byte[] skipBuffer;
/*      */     private static final int SKIP_BUFFER_SIZE = 8096;
/*      */ 
/*      */     public HttpInputStream(InputStream arg2)
/*      */     {
/* 2971 */       super();
/* 2972 */       this.cacheRequest = null;
/* 2973 */       this.outputStream = null;
/*      */     }
/*      */ 
/*      */     public HttpInputStream(InputStream paramCacheRequest, CacheRequest arg3) {
/* 2977 */       super();
/*      */       Object localObject;
/* 2978 */       this.cacheRequest = localObject;
/*      */       try {
/* 2980 */         this.outputStream = localObject.getBody();
/*      */       } catch (IOException localIOException) {
/* 2982 */         this.cacheRequest.abort();
/* 2983 */         this.cacheRequest = null;
/* 2984 */         this.outputStream = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public synchronized void mark(int paramInt)
/*      */     {
/* 3007 */       super.mark(paramInt);
/* 3008 */       if (this.cacheRequest != null) {
/* 3009 */         this.marked = true;
/* 3010 */         this.markCount = 0;
/*      */       }
/*      */     }
/*      */ 
/*      */     public synchronized void reset()
/*      */       throws IOException
/*      */     {
/* 3037 */       super.reset();
/* 3038 */       if (this.cacheRequest != null) {
/* 3039 */         this.marked = false;
/* 3040 */         this.inCache += this.markCount;
/*      */       }
/*      */     }
/*      */ 
/*      */     public int read() throws IOException
/*      */     {
/*      */       try {
/* 3047 */         byte[] arrayOfByte = new byte[1];
/* 3048 */         int i = read(arrayOfByte);
/* 3049 */         return i == -1 ? i : arrayOfByte[0] & 0xFF;
/*      */       } catch (IOException localIOException) {
/* 3051 */         if (this.cacheRequest != null) {
/* 3052 */           this.cacheRequest.abort();
/*      */         }
/* 3054 */         throw localIOException;
/*      */       }
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte) throws IOException
/*      */     {
/* 3060 */       return read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/*      */       try {
/* 3066 */         int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */         int j;
/* 3069 */         if (this.inCache > 0) {
/* 3070 */           if (this.inCache >= i) {
/* 3071 */             this.inCache -= i;
/* 3072 */             j = 0;
/*      */           } else {
/* 3074 */             j = i - this.inCache;
/* 3075 */             this.inCache = 0;
/*      */           }
/*      */         }
/* 3078 */         else j = i;
/*      */ 
/* 3080 */         if ((j > 0) && (this.outputStream != null))
/* 3081 */           this.outputStream.write(paramArrayOfByte, paramInt1 + (i - j), j);
/* 3082 */         if (this.marked) {
/* 3083 */           this.markCount += i;
/*      */         }
/* 3085 */         return i;
/*      */       } catch (IOException localIOException) {
/* 3087 */         if (this.cacheRequest != null) {
/* 3088 */           this.cacheRequest.abort();
/*      */         }
/* 3090 */         throw localIOException;
/*      */       }
/*      */     }
/*      */ 
/*      */     public long skip(long paramLong)
/*      */       throws IOException
/*      */     {
/* 3103 */       long l = paramLong;
/*      */ 
/* 3105 */       if (this.skipBuffer == null) {
/* 3106 */         this.skipBuffer = new byte[8096];
/*      */       }
/* 3108 */       byte[] arrayOfByte = this.skipBuffer;
/*      */ 
/* 3110 */       if (paramLong <= 0L) {
/* 3111 */         return 0L;
/*      */       }
/*      */ 
/* 3114 */       while (l > 0L) {
/* 3115 */         int i = read(arrayOfByte, 0, (int)Math.min(8096L, l));
/*      */ 
/* 3117 */         if (i < 0) {
/*      */           break;
/*      */         }
/* 3120 */         l -= i;
/*      */       }
/*      */ 
/* 3123 */       return paramLong - l;
/*      */     }
/*      */ 
/*      */     public void close() throws IOException
/*      */     {
/*      */       try {
/* 3129 */         if (this.outputStream != null) {
/* 3130 */           if (read() != -1)
/* 3131 */             this.cacheRequest.abort();
/*      */           else {
/* 3133 */             this.outputStream.close();
/*      */           }
/*      */         }
/* 3136 */         super.close();
/*      */       } catch (IOException localIOException) {
/* 3138 */         if (this.cacheRequest != null) {
/* 3139 */           this.cacheRequest.abort();
/*      */         }
/* 3141 */         throw localIOException;
/*      */       } finally {
/* 3143 */         HttpURLConnection.this.http = null;
/* 3144 */         HttpURLConnection.this.checkResponseCredentials(true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class StreamingOutputStream extends FilterOutputStream
/*      */   {
/*      */     long expected;
/*      */     long written;
/*      */     boolean closed;
/*      */     boolean error;
/*      */     IOException errorExcp;
/*      */ 
/*      */     StreamingOutputStream(OutputStream paramLong, long arg3)
/*      */     {
/* 3164 */       super();
/*      */       Object localObject;
/* 3165 */       this.expected = localObject;
/* 3166 */       this.written = 0L;
/* 3167 */       this.closed = false;
/* 3168 */       this.error = false;
/*      */     }
/*      */ 
/*      */     public void write(int paramInt) throws IOException
/*      */     {
/* 3173 */       checkError();
/* 3174 */       this.written += 1L;
/* 3175 */       if ((this.expected != -1L) && (this.written > this.expected)) {
/* 3176 */         throw new IOException("too many bytes written");
/*      */       }
/* 3178 */       this.out.write(paramInt);
/*      */     }
/*      */ 
/*      */     public void write(byte[] paramArrayOfByte) throws IOException
/*      */     {
/* 3183 */       write(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */     }
/*      */ 
/*      */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 3188 */       checkError();
/* 3189 */       this.written += paramInt2;
/* 3190 */       if ((this.expected != -1L) && (this.written > this.expected)) {
/* 3191 */         this.out.close();
/* 3192 */         throw new IOException("too many bytes written");
/*      */       }
/* 3194 */       this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     void checkError() throws IOException {
/* 3198 */       if (this.closed) {
/* 3199 */         throw new IOException("Stream is closed");
/*      */       }
/* 3201 */       if (this.error) {
/* 3202 */         throw this.errorExcp;
/*      */       }
/* 3204 */       if (((PrintStream)this.out).checkError())
/* 3205 */         throw new IOException("Error writing request body to server");
/*      */     }
/*      */ 
/*      */     boolean writtenOK()
/*      */     {
/* 3214 */       return (this.closed) && (!this.error);
/*      */     }
/*      */ 
/*      */     public void close() throws IOException
/*      */     {
/* 3219 */       if (this.closed) {
/* 3220 */         return;
/*      */       }
/* 3222 */       this.closed = true;
/* 3223 */       if (this.expected != -1L)
/*      */       {
/* 3225 */         if (this.written != this.expected) {
/* 3226 */           this.error = true;
/* 3227 */           this.errorExcp = new IOException("insufficient data written");
/* 3228 */           this.out.close();
/* 3229 */           throw this.errorExcp;
/*      */         }
/* 3231 */         super.flush();
/*      */       }
/*      */       else {
/* 3234 */         super.close();
/*      */ 
/* 3236 */         OutputStream localOutputStream = HttpURLConnection.this.http.getOutputStream();
/* 3237 */         localOutputStream.write(13);
/* 3238 */         localOutputStream.write(10);
/* 3239 */         localOutputStream.flush();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum TunnelState
/*      */   {
/*  355 */     NONE, 
/*      */ 
/*  358 */     SETUP, 
/*      */ 
/*  361 */     TUNNELING;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.HttpURLConnection
 * JD-Core Version:    0.6.2
 */