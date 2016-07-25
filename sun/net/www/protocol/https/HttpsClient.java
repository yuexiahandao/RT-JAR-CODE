/*     */ package sun.net.www.protocol.https;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Proxy.Type;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.net.ssl.HandshakeCompletedEvent;
/*     */ import javax.net.ssl.HandshakeCompletedListener;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import sun.net.www.http.HttpClient;
/*     */ import sun.net.www.http.KeepAliveCache;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ import sun.net.www.protocol.http.HttpURLConnection.TunnelState;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.ssl.SSLSocketImpl;
/*     */ import sun.security.util.HostnameChecker;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ final class HttpsClient extends HttpClient
/*     */   implements HandshakeCompletedListener
/*     */ {
/*     */   private static final int httpsPortNumber = 443;
/*     */   private static final String defaultHVCanonicalName = "javax.net.ssl.HttpsURLConnection.DefaultHostnameVerifier";
/*     */   private HostnameVerifier hv;
/*     */   private SSLSocketFactory sslSocketFactory;
/*     */   private SSLSession session;
/*     */ 
/*     */   protected int getDefaultPort()
/*     */   {
/* 122 */     return 443;
/*     */   }
/*     */ 
/*     */   private String[] getCipherSuites()
/*     */   {
/* 142 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("https.cipherSuites"));
/*     */     String[] arrayOfString;
/* 145 */     if ((str == null) || ("".equals(str))) {
/* 146 */       arrayOfString = null;
/*     */     }
/*     */     else {
/* 149 */       Vector localVector = new Vector();
/*     */ 
/* 151 */       StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
/* 152 */       while (localStringTokenizer.hasMoreTokens())
/* 153 */         localVector.addElement(localStringTokenizer.nextToken());
/* 154 */       arrayOfString = new String[localVector.size()];
/* 155 */       for (int i = 0; i < arrayOfString.length; i++)
/* 156 */         arrayOfString[i] = ((String)localVector.elementAt(i));
/*     */     }
/* 158 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private String[] getProtocols()
/*     */   {
/* 166 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("https.protocols"));
/*     */     String[] arrayOfString;
/* 169 */     if ((str == null) || ("".equals(str))) {
/* 170 */       arrayOfString = null;
/*     */     }
/*     */     else {
/* 173 */       Vector localVector = new Vector();
/*     */ 
/* 175 */       StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
/* 176 */       while (localStringTokenizer.hasMoreTokens())
/* 177 */         localVector.addElement(localStringTokenizer.nextToken());
/* 178 */       arrayOfString = new String[localVector.size()];
/* 179 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 180 */         arrayOfString[i] = ((String)localVector.elementAt(i));
/*     */       }
/*     */     }
/* 183 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private String getUserAgent() {
/* 187 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("https.agent"));
/*     */ 
/* 189 */     if ((str == null) || (str.length() == 0)) {
/* 190 */       str = "JSSE";
/*     */     }
/* 192 */     return str;
/*     */   }
/*     */ 
/*     */   private static Proxy newHttpProxy(String paramString, int paramInt)
/*     */   {
/* 197 */     InetSocketAddress localInetSocketAddress = null;
/* 198 */     String str = paramString;
/* 199 */     final int i = paramInt < 0 ? 443 : paramInt;
/*     */     try {
/* 201 */       localInetSocketAddress = (InetSocketAddress)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public InetSocketAddress run() {
/* 204 */           return new InetSocketAddress(this.val$phost, i);
/*     */         } } );
/*     */     } catch (PrivilegedActionException localPrivilegedActionException) {
/*     */     }
/* 208 */     return new Proxy(Proxy.Type.HTTP, localInetSocketAddress);
/*     */   }
/*     */ 
/*     */   private HttpsClient(SSLSocketFactory paramSSLSocketFactory, URL paramURL)
/*     */     throws IOException
/*     */   {
/* 233 */     this(paramSSLSocketFactory, paramURL, (String)null, -1);
/*     */   }
/*     */ 
/*     */   HttpsClient(SSLSocketFactory paramSSLSocketFactory, URL paramURL, String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 242 */     this(paramSSLSocketFactory, paramURL, paramString, paramInt, -1);
/*     */   }
/*     */ 
/*     */   HttpsClient(SSLSocketFactory paramSSLSocketFactory, URL paramURL, String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 252 */     this(paramSSLSocketFactory, paramURL, paramString == null ? null : newHttpProxy(paramString, paramInt1), paramInt2);
/*     */   }
/*     */ 
/*     */   HttpsClient(SSLSocketFactory paramSSLSocketFactory, URL paramURL, Proxy paramProxy, int paramInt)
/*     */     throws IOException
/*     */   {
/* 264 */     this.proxy = paramProxy;
/* 265 */     setSSLSocketFactory(paramSSLSocketFactory);
/* 266 */     this.proxyDisabled = true;
/*     */ 
/* 268 */     this.host = paramURL.getHost();
/* 269 */     this.url = paramURL;
/* 270 */     this.port = paramURL.getPort();
/* 271 */     if (this.port == -1) {
/* 272 */       this.port = getDefaultPort();
/*     */     }
/* 274 */     setConnectTimeout(paramInt);
/* 275 */     openServer();
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 285 */     return New(paramSSLSocketFactory, paramURL, paramHostnameVerifier, true, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, boolean paramBoolean, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 292 */     return New(paramSSLSocketFactory, paramURL, paramHostnameVerifier, (String)null, -1, paramBoolean, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, String paramString, int paramInt, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 302 */     return New(paramSSLSocketFactory, paramURL, paramHostnameVerifier, paramString, paramInt, true, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, String paramString, int paramInt, boolean paramBoolean, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 309 */     return New(paramSSLSocketFactory, paramURL, paramHostnameVerifier, paramString, paramInt, paramBoolean, -1, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, String paramString, int paramInt1, boolean paramBoolean, int paramInt2, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 318 */     return New(paramSSLSocketFactory, paramURL, paramHostnameVerifier, paramString == null ? null : newHttpProxy(paramString, paramInt1), paramBoolean, paramInt2, paramHttpURLConnection);
/*     */   }
/*     */ 
/*     */   static HttpClient New(SSLSocketFactory paramSSLSocketFactory, URL paramURL, HostnameVerifier paramHostnameVerifier, Proxy paramProxy, boolean paramBoolean, int paramInt, HttpURLConnection paramHttpURLConnection)
/*     */     throws IOException
/*     */   {
/* 329 */     if (paramProxy == null) {
/* 330 */       paramProxy = Proxy.NO_PROXY;
/*     */     }
/* 332 */     HttpsClient localHttpsClient = null;
/* 333 */     if (paramBoolean)
/*     */     {
/* 335 */       localHttpsClient = (HttpsClient)kac.get(paramURL, paramSSLSocketFactory);
/* 336 */       if ((localHttpsClient != null) && (paramHttpURLConnection != null) && (paramHttpURLConnection.streaming()) && (paramHttpURLConnection.getRequestMethod() == "POST"))
/*     */       {
/* 339 */         if (!localHttpsClient.available()) {
/* 340 */           localHttpsClient = null;
/*     */         }
/*     */       }
/* 343 */       if (localHttpsClient != null) {
/* 344 */         if (((localHttpsClient.proxy != null) && (localHttpsClient.proxy.equals(paramProxy))) || ((localHttpsClient.proxy == null) && (paramProxy == null)))
/*     */         {
/* 346 */           synchronized (localHttpsClient) {
/* 347 */             localHttpsClient.cachedHttpClient = true;
/* 348 */             assert (localHttpsClient.inCache);
/* 349 */             localHttpsClient.inCache = false;
/* 350 */             if ((paramHttpURLConnection != null) && (localHttpsClient.needsTunneling()))
/* 351 */               paramHttpURLConnection.setTunnelState(HttpURLConnection.TunnelState.TUNNELING);
/* 352 */             PlatformLogger localPlatformLogger = HttpURLConnection.getHttpLogger();
/* 353 */             if (localPlatformLogger.isLoggable(300)) {
/* 354 */               localPlatformLogger.finest("KeepAlive stream retrieved from the cache, " + localHttpsClient);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 362 */           synchronized (localHttpsClient) {
/* 363 */             localHttpsClient.inCache = false;
/* 364 */             localHttpsClient.closeServer();
/*     */           }
/* 366 */           localHttpsClient = null;
/*     */         }
/*     */       }
/*     */     }
/* 370 */     if (localHttpsClient == null) {
/* 371 */       localHttpsClient = new HttpsClient(paramSSLSocketFactory, paramURL, paramProxy, paramInt);
/*     */     } else {
/* 373 */       ??? = System.getSecurityManager();
/* 374 */       if (??? != null) {
/* 375 */         if ((localHttpsClient.proxy == Proxy.NO_PROXY) || (localHttpsClient.proxy == null))
/* 376 */           ((SecurityManager)???).checkConnect(InetAddress.getByName(paramURL.getHost()).getHostAddress(), paramURL.getPort());
/*     */         else {
/* 378 */           ((SecurityManager)???).checkConnect(paramURL.getHost(), paramURL.getPort());
/*     */         }
/*     */       }
/* 381 */       localHttpsClient.url = paramURL;
/*     */     }
/* 383 */     localHttpsClient.setHostnameVerifier(paramHostnameVerifier);
/*     */ 
/* 385 */     return localHttpsClient;
/*     */   }
/*     */ 
/*     */   void setHostnameVerifier(HostnameVerifier paramHostnameVerifier)
/*     */   {
/* 390 */     this.hv = paramHostnameVerifier;
/*     */   }
/*     */ 
/*     */   void setSSLSocketFactory(SSLSocketFactory paramSSLSocketFactory) {
/* 394 */     this.sslSocketFactory = paramSSLSocketFactory;
/*     */   }
/*     */ 
/*     */   SSLSocketFactory getSSLSocketFactory() {
/* 398 */     return this.sslSocketFactory;
/*     */   }
/*     */ 
/*     */   protected Socket createSocket()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 409 */       return this.sslSocketFactory.createSocket();
/*     */     }
/*     */     catch (SocketException localSocketException)
/*     */     {
/* 417 */       Throwable localThrowable = localSocketException.getCause();
/* 418 */       if ((localThrowable != null) && ((localThrowable instanceof UnsupportedOperationException))) {
/* 419 */         return super.createSocket();
/*     */       }
/* 421 */       throw localSocketException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean needsTunneling()
/*     */   {
/* 429 */     return (this.proxy != null) && (this.proxy.type() != Proxy.Type.DIRECT) && (this.proxy.type() != Proxy.Type.SOCKS);
/*     */   }
/*     */ 
/*     */   public void afterConnect()
/*     */     throws IOException, UnknownHostException
/*     */   {
/* 435 */     if (!isCachedConnection()) {
/* 436 */       SSLSocket localSSLSocket = null;
/* 437 */       SSLSocketFactory localSSLSocketFactory = this.sslSocketFactory;
/*     */       try {
/* 439 */         if (!(this.serverSocket instanceof SSLSocket)) {
/* 440 */           localSSLSocket = (SSLSocket)localSSLSocketFactory.createSocket(this.serverSocket, this.host, this.port, true);
/*     */         }
/*     */         else {
/* 443 */           localSSLSocket = (SSLSocket)this.serverSocket;
/* 444 */           if ((localSSLSocket instanceof SSLSocketImpl)) {
/* 445 */             ((SSLSocketImpl)localSSLSocket).setHost(this.host);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/*     */         try
/*     */         {
/* 453 */           localSSLSocket = (SSLSocket)localSSLSocketFactory.createSocket(this.host, this.port);
/*     */         } catch (IOException localIOException2) {
/* 455 */           throw localIOException1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 464 */       String[] arrayOfString1 = getProtocols();
/* 465 */       String[] arrayOfString2 = getCipherSuites();
/* 466 */       if (arrayOfString1 != null) {
/* 467 */         localSSLSocket.setEnabledProtocols(arrayOfString1);
/*     */       }
/* 469 */       if (arrayOfString2 != null) {
/* 470 */         localSSLSocket.setEnabledCipherSuites(arrayOfString2);
/*     */       }
/* 472 */       localSSLSocket.addHandshakeCompletedListener(this);
/*     */ 
/* 522 */       int i = 1;
/* 523 */       String str = localSSLSocket.getSSLParameters().getEndpointIdentificationAlgorithm();
/*     */ 
/* 525 */       if ((str != null) && (str.length() != 0)) {
/* 526 */         if (str.equalsIgnoreCase("HTTPS"))
/*     */         {
/* 530 */           i = 0;
/*     */         }
/*     */       }
/*     */       else {
/* 534 */         int j = 0;
/*     */         Object localObject;
/* 539 */         if (this.hv != null) {
/* 540 */           localObject = this.hv.getClass().getCanonicalName();
/* 541 */           if ((localObject != null) && (((String)localObject).equalsIgnoreCase("javax.net.ssl.HttpsURLConnection.DefaultHostnameVerifier")))
/*     */           {
/* 543 */             j = 1;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 549 */           j = 1;
/*     */         }
/*     */ 
/* 552 */         if (j != 0)
/*     */         {
/* 555 */           localObject = localSSLSocket.getSSLParameters();
/* 556 */           ((SSLParameters)localObject).setEndpointIdentificationAlgorithm("HTTPS");
/* 557 */           localSSLSocket.setSSLParameters((SSLParameters)localObject);
/*     */ 
/* 559 */           i = 0;
/*     */         }
/*     */       }
/*     */ 
/* 563 */       localSSLSocket.startHandshake();
/* 564 */       this.session = localSSLSocket.getSession();
/*     */ 
/* 566 */       this.serverSocket = localSSLSocket;
/*     */       try {
/* 568 */         this.serverOutput = new PrintStream(new BufferedOutputStream(this.serverSocket.getOutputStream()), false, encoding);
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */       {
/* 572 */         throw new InternalError(encoding + " encoding not found");
/*     */       }
/*     */ 
/* 576 */       if (i != 0) {
/* 577 */         checkURLSpoofing(this.hv);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 583 */       this.session = ((SSLSocket)this.serverSocket).getSession();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkURLSpoofing(HostnameVerifier paramHostnameVerifier)
/*     */     throws IOException
/*     */   {
/* 594 */     String str1 = this.url.getHost();
/*     */ 
/* 597 */     if ((str1 != null) && (str1.startsWith("[")) && (str1.endsWith("]"))) {
/* 598 */       str1 = str1.substring(1, str1.length() - 1);
/*     */     }
/*     */ 
/* 601 */     Certificate[] arrayOfCertificate = null;
/* 602 */     String str2 = this.session.getCipherSuite();
/*     */     try {
/* 604 */       HostnameChecker localHostnameChecker = HostnameChecker.getInstance((byte)1);
/*     */ 
/* 608 */       if (str2.startsWith("TLS_KRB5")) {
/* 609 */         if (!HostnameChecker.match(str1, getPeerPrincipal())) {
/* 610 */           throw new SSLPeerUnverifiedException("Hostname checker failed for Kerberos");
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 616 */         arrayOfCertificate = this.session.getPeerCertificates();
/*     */         java.security.cert.X509Certificate localX509Certificate;
/* 619 */         if ((arrayOfCertificate[0] instanceof java.security.cert.X509Certificate))
/*     */         {
/* 621 */           localX509Certificate = (java.security.cert.X509Certificate)arrayOfCertificate[0];
/*     */         }
/* 623 */         else throw new SSLPeerUnverifiedException("");
/*     */ 
/* 625 */         localHostnameChecker.match(str1, localX509Certificate);
/*     */       }
/*     */ 
/* 629 */       return;
/*     */     }
/*     */     catch (SSLPeerUnverifiedException localSSLPeerUnverifiedException)
/*     */     {
/*     */     }
/*     */     catch (CertificateException localCertificateException)
/*     */     {
/*     */     }
/*     */ 
/* 642 */     if ((str2 != null) && (str2.indexOf("_anon_") != -1))
/* 643 */       return;
/* 644 */     if ((paramHostnameVerifier != null) && (paramHostnameVerifier.verify(str1, this.session)))
/*     */     {
/* 646 */       return;
/*     */     }
/*     */ 
/* 649 */     this.serverSocket.close();
/* 650 */     this.session.invalidate();
/*     */ 
/* 652 */     throw new IOException("HTTPS hostname wrong:  should be <" + this.url.getHost() + ">");
/*     */   }
/*     */ 
/*     */   protected void putInKeepAliveCache()
/*     */   {
/* 658 */     if (this.inCache) {
/* 659 */       if (!$assertionsDisabled) throw new AssertionError("Duplicate put to keep alive cache");
/* 660 */       return;
/*     */     }
/* 662 */     this.inCache = true;
/* 663 */     kac.put(this.url, this.sslSocketFactory, this);
/*     */   }
/*     */ 
/*     */   public void closeIdleConnection()
/*     */   {
/* 671 */     HttpClient localHttpClient = kac.get(this.url, this.sslSocketFactory);
/* 672 */     if (localHttpClient != null)
/* 673 */       localHttpClient.closeServer();
/*     */   }
/*     */ 
/*     */   String getCipherSuite()
/*     */   {
/* 681 */     return this.session.getCipherSuite();
/*     */   }
/*     */ 
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 689 */     return this.session.getLocalCertificates();
/*     */   }
/*     */ 
/*     */   Certificate[] getServerCertificates()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 700 */     return this.session.getPeerCertificates();
/*     */   }
/*     */ 
/*     */   javax.security.cert.X509Certificate[] getServerCertificateChain()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/* 710 */     return this.session.getPeerCertificateChain();
/*     */   }
/*     */ 
/*     */   Principal getPeerPrincipal()
/*     */     throws SSLPeerUnverifiedException
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 723 */       localObject = this.session.getPeerPrincipal();
/*     */     }
/*     */     catch (AbstractMethodError localAbstractMethodError)
/*     */     {
/* 727 */       Certificate[] arrayOfCertificate = this.session.getPeerCertificates();
/*     */ 
/* 729 */       localObject = ((java.security.cert.X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */     }
/*     */ 
/* 732 */     return localObject;
/*     */   }
/*     */ 
/*     */   Principal getLocalPrincipal()
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 743 */       localObject = this.session.getLocalPrincipal();
/*     */     } catch (AbstractMethodError localAbstractMethodError) {
/* 745 */       localObject = null;
/*     */ 
/* 748 */       Certificate[] arrayOfCertificate = this.session.getLocalCertificates();
/*     */ 
/* 750 */       if (arrayOfCertificate != null) {
/* 751 */         localObject = ((java.security.cert.X509Certificate)arrayOfCertificate[0]).getSubjectX500Principal();
/*     */       }
/*     */     }
/*     */ 
/* 755 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void handshakeCompleted(HandshakeCompletedEvent paramHandshakeCompletedEvent)
/*     */   {
/* 768 */     this.session = paramHandshakeCompletedEvent.getSession();
/*     */   }
/*     */ 
/*     */   public String getProxyHostUsed()
/*     */   {
/* 777 */     if (!needsTunneling()) {
/* 778 */       return null;
/*     */     }
/* 780 */     return super.getProxyHostUsed();
/*     */   }
/*     */ 
/*     */   public int getProxyPortUsed()
/*     */   {
/* 790 */     return (this.proxy == null) || (this.proxy.type() == Proxy.Type.DIRECT) || (this.proxy.type() == Proxy.Type.SOCKS) ? -1 : ((InetSocketAddress)this.proxy.address()).getPort();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.https.HttpsClient
 * JD-Core Version:    0.6.2
 */