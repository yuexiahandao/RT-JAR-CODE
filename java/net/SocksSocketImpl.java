/*      */ package java.net;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import sun.net.SocksProxy;
/*      */ import sun.net.www.ParseUtil;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ class SocksSocketImpl extends PlainSocketImpl
/*      */   implements SocksConsts
/*      */ {
/*   44 */   private String server = null;
/*   45 */   private int serverPort = 1080;
/*      */   private InetSocketAddress external_address;
/*   47 */   private boolean useV4 = false;
/*   48 */   private Socket cmdsock = null;
/*   49 */   private InputStream cmdIn = null;
/*   50 */   private OutputStream cmdOut = null;
/*      */   private boolean applicationSetProxy;
/*      */ 
/*      */   SocksSocketImpl()
/*      */   {
/*      */   }
/*      */ 
/*      */   SocksSocketImpl(String paramString, int paramInt)
/*      */   {
/*   60 */     this.server = paramString;
/*   61 */     this.serverPort = (paramInt == -1 ? 1080 : paramInt);
/*      */   }
/*      */ 
/*      */   SocksSocketImpl(Proxy paramProxy) {
/*   65 */     SocketAddress localSocketAddress = paramProxy.address();
/*   66 */     if ((localSocketAddress instanceof InetSocketAddress)) {
/*   67 */       InetSocketAddress localInetSocketAddress = (InetSocketAddress)localSocketAddress;
/*      */ 
/*   69 */       this.server = localInetSocketAddress.getHostString();
/*   70 */       this.serverPort = localInetSocketAddress.getPort();
/*      */     }
/*      */   }
/*      */ 
/*      */   void setV4() {
/*   75 */     this.useV4 = true;
/*      */   }
/*      */ 
/*      */   private synchronized void privilegedConnect(final String paramString, final int paramInt1, final int paramInt2)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*   84 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Void run() throws IOException {
/*   87 */           SocksSocketImpl.this.superConnectServer(paramString, paramInt1, paramInt2);
/*   88 */           SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.getInputStream();
/*   89 */           SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.getOutputStream();
/*   90 */           return null;
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/*   94 */       throw ((IOException)localPrivilegedActionException.getException());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void superConnectServer(String paramString, int paramInt1, int paramInt2) throws IOException
/*      */   {
/*  100 */     super.connect(new InetSocketAddress(paramString, paramInt1), paramInt2);
/*      */   }
/*      */ 
/*      */   private static int remainingMillis(long paramLong) throws IOException {
/*  104 */     if (paramLong == 0L) {
/*  105 */       return 0;
/*      */     }
/*  107 */     long l = paramLong - System.currentTimeMillis();
/*  108 */     if (l > 0L) {
/*  109 */       return (int)l;
/*      */     }
/*  111 */     throw new SocketTimeoutException();
/*      */   }
/*      */ 
/*      */   private int readSocksReply(InputStream paramInputStream, byte[] paramArrayOfByte) throws IOException {
/*  115 */     return readSocksReply(paramInputStream, paramArrayOfByte, 0L);
/*      */   }
/*      */ 
/*      */   private int readSocksReply(InputStream paramInputStream, byte[] paramArrayOfByte, long paramLong) throws IOException {
/*  119 */     int i = paramArrayOfByte.length;
/*  120 */     int j = 0;
/*  121 */     for (int k = 0; (j < i) && (k < 3); k++) {
/*      */       int m;
/*      */       try {
/*  124 */         m = ((SocketInputStream)paramInputStream).read(paramArrayOfByte, j, i - j, remainingMillis(paramLong));
/*      */       } catch (SocketTimeoutException localSocketTimeoutException) {
/*  126 */         throw new SocketTimeoutException("Connect timed out");
/*      */       }
/*  128 */       if (m < 0)
/*  129 */         throw new SocketException("Malformed reply from SOCKS server");
/*  130 */       j += m;
/*      */     }
/*  132 */     return j;
/*      */   }
/*      */ 
/*      */   private boolean authenticate(byte paramByte, InputStream paramInputStream, BufferedOutputStream paramBufferedOutputStream)
/*      */     throws IOException
/*      */   {
/*  140 */     return authenticate(paramByte, paramInputStream, paramBufferedOutputStream, 0L);
/*      */   }
/*      */ 
/*      */   private boolean authenticate(byte paramByte, InputStream paramInputStream, BufferedOutputStream paramBufferedOutputStream, long paramLong)
/*      */     throws IOException
/*      */   {
/*  147 */     if (paramByte == 0) {
/*  148 */       return true;
/*      */     }
/*      */ 
/*  154 */     if (paramByte == 2)
/*      */     {
/*  156 */       String str2 = null;
/*  157 */       final InetAddress localInetAddress = InetAddress.getByName(this.server);
/*  158 */       PasswordAuthentication localPasswordAuthentication = (PasswordAuthentication)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public PasswordAuthentication run()
/*      */         {
/*  162 */           return Authenticator.requestPasswordAuthentication(SocksSocketImpl.this.server, localInetAddress, SocksSocketImpl.this.serverPort, "SOCKS5", "SOCKS authentication", null);
/*      */         }
/*      */       });
/*      */       String str1;
/*  166 */       if (localPasswordAuthentication != null) {
/*  167 */         str1 = localPasswordAuthentication.getUserName();
/*  168 */         str2 = new String(localPasswordAuthentication.getPassword());
/*      */       } else {
/*  170 */         str1 = (String)AccessController.doPrivileged(new GetPropertyAction("user.name"));
/*      */       }
/*      */ 
/*  173 */       if (str1 == null)
/*  174 */         return false;
/*  175 */       paramBufferedOutputStream.write(1);
/*  176 */       paramBufferedOutputStream.write(str1.length());
/*      */       try {
/*  178 */         paramBufferedOutputStream.write(str1.getBytes("ISO-8859-1"));
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*  180 */         if (!$assertionsDisabled) throw new AssertionError();
/*      */       }
/*  182 */       if (str2 != null) {
/*  183 */         paramBufferedOutputStream.write(str2.length());
/*      */         try {
/*  185 */           paramBufferedOutputStream.write(str2.getBytes("ISO-8859-1"));
/*      */         } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*  187 */           if (!$assertionsDisabled) throw new AssertionError(); 
/*      */         }
/*      */       }
/*  190 */       else { paramBufferedOutputStream.write(0); }
/*  191 */       paramBufferedOutputStream.flush();
/*  192 */       byte[] arrayOfByte = new byte[2];
/*  193 */       int i = readSocksReply(paramInputStream, arrayOfByte, paramLong);
/*  194 */       if ((i != 2) || (arrayOfByte[1] != 0))
/*      */       {
/*  197 */         paramBufferedOutputStream.close();
/*  198 */         paramInputStream.close();
/*  199 */         return false;
/*      */       }
/*      */ 
/*  202 */       return true;
/*      */     }
/*      */ 
/*  258 */     return false;
/*      */   }
/*      */ 
/*      */   private void connectV4(InputStream paramInputStream, OutputStream paramOutputStream, InetSocketAddress paramInetSocketAddress, long paramLong)
/*      */     throws IOException
/*      */   {
/*  264 */     if (!(paramInetSocketAddress.getAddress() instanceof Inet4Address)) {
/*  265 */       throw new SocketException("SOCKS V4 requires IPv4 only addresses");
/*      */     }
/*  267 */     paramOutputStream.write(4);
/*  268 */     paramOutputStream.write(1);
/*  269 */     paramOutputStream.write(paramInetSocketAddress.getPort() >> 8 & 0xFF);
/*  270 */     paramOutputStream.write(paramInetSocketAddress.getPort() >> 0 & 0xFF);
/*  271 */     paramOutputStream.write(paramInetSocketAddress.getAddress().getAddress());
/*  272 */     String str = getUserName();
/*      */     try {
/*  274 */       paramOutputStream.write(str.getBytes("ISO-8859-1"));
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  276 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/*  278 */     paramOutputStream.write(0);
/*  279 */     paramOutputStream.flush();
/*  280 */     byte[] arrayOfByte = new byte[8];
/*  281 */     int i = readSocksReply(paramInputStream, arrayOfByte, paramLong);
/*  282 */     if (i != 8)
/*  283 */       throw new SocketException("Reply from SOCKS server has bad length: " + i);
/*  284 */     if ((arrayOfByte[0] != 0) && (arrayOfByte[0] != 4))
/*  285 */       throw new SocketException("Reply from SOCKS server has bad version");
/*  286 */     SocketException localSocketException = null;
/*  287 */     switch (arrayOfByte[1])
/*      */     {
/*      */     case 90:
/*  290 */       this.external_address = paramInetSocketAddress;
/*  291 */       break;
/*      */     case 91:
/*  293 */       localSocketException = new SocketException("SOCKS request rejected");
/*  294 */       break;
/*      */     case 92:
/*  296 */       localSocketException = new SocketException("SOCKS server couldn't reach destination");
/*  297 */       break;
/*      */     case 93:
/*  299 */       localSocketException = new SocketException("SOCKS authentication failed");
/*  300 */       break;
/*      */     default:
/*  302 */       localSocketException = new SocketException("Reply from SOCKS server contains bad status");
/*      */     }
/*      */ 
/*  305 */     if (localSocketException != null) {
/*  306 */       paramInputStream.close();
/*  307 */       paramOutputStream.close();
/*  308 */       throw localSocketException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void connect(SocketAddress paramSocketAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*      */     long l1;
/*  330 */     if (paramInt == 0) {
/*  331 */       l1 = 0L;
/*      */     } else {
/*  333 */       long l2 = System.currentTimeMillis() + paramInt;
/*  334 */       l1 = l2 < 0L ? 9223372036854775807L : l2;
/*      */     }
/*      */ 
/*  337 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  338 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress)))
/*  339 */       throw new IllegalArgumentException("Unsupported address type");
/*  340 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/*  341 */     if (localSecurityManager != null) {
/*  342 */       if (localInetSocketAddress.isUnresolved()) {
/*  343 */         localSecurityManager.checkConnect(localInetSocketAddress.getHostName(), localInetSocketAddress.getPort());
/*      */       }
/*      */       else {
/*  346 */         localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */       }
/*      */     }
/*  349 */     if (this.server == null)
/*      */     {
/*  353 */       ProxySelector localProxySelector = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public ProxySelector run() {
/*  356 */           return ProxySelector.getDefault();
/*      */         }
/*      */       });
/*  359 */       if (localProxySelector == null)
/*      */       {
/*  363 */         super.connect(localInetSocketAddress, remainingMillis(l1));
/*  364 */         return;
/*      */       }
/*      */ 
/*  368 */       localObject2 = localInetSocketAddress.getHostString();
/*      */ 
/*  370 */       if (((localInetSocketAddress.getAddress() instanceof Inet6Address)) && (!((String)localObject2).startsWith("[")) && (((String)localObject2).indexOf(":") >= 0))
/*      */       {
/*  372 */         localObject2 = "[" + (String)localObject2 + "]";
/*      */       }
/*      */       try {
/*  375 */         localObject1 = new URI("socket://" + ParseUtil.encodePath((String)localObject2) + ":" + localInetSocketAddress.getPort());
/*      */       }
/*      */       catch (URISyntaxException localURISyntaxException) {
/*  378 */         if (!$assertionsDisabled) throw new AssertionError(localURISyntaxException);
/*  379 */         localObject1 = null;
/*      */       }
/*  381 */       Proxy localProxy = null;
/*  382 */       Object localObject3 = null;
/*  383 */       Iterator localIterator = null;
/*  384 */       localIterator = localProxySelector.select((URI)localObject1).iterator();
/*  385 */       if ((localIterator == null) || (!localIterator.hasNext())) {
/*  386 */         super.connect(localInetSocketAddress, remainingMillis(l1));
/*  387 */         return;
/*      */       }
/*  389 */       while (localIterator.hasNext()) {
/*  390 */         localProxy = (Proxy)localIterator.next();
/*  391 */         if ((localProxy == null) || (localProxy == Proxy.NO_PROXY)) {
/*  392 */           super.connect(localInetSocketAddress, remainingMillis(l1));
/*  393 */           return;
/*      */         }
/*  395 */         if (localProxy.type() != Proxy.Type.SOCKS)
/*  396 */           throw new SocketException("Unknown proxy type : " + localProxy.type());
/*  397 */         if (!(localProxy.address() instanceof InetSocketAddress)) {
/*  398 */           throw new SocketException("Unknow address type for proxy: " + localProxy);
/*      */         }
/*  400 */         this.server = ((InetSocketAddress)localProxy.address()).getHostString();
/*  401 */         this.serverPort = ((InetSocketAddress)localProxy.address()).getPort();
/*  402 */         if (((localProxy instanceof SocksProxy)) && 
/*  403 */           (((SocksProxy)localProxy).protocolVersion() == 4)) {
/*  404 */           this.useV4 = true;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  410 */           privilegedConnect(this.server, this.serverPort, remainingMillis(l1));
/*      */         }
/*      */         catch (IOException localIOException2)
/*      */         {
/*  415 */           localProxySelector.connectFailed((URI)localObject1, localProxy.address(), localIOException2);
/*  416 */           this.server = null;
/*  417 */           this.serverPort = -1;
/*  418 */           localObject3 = localIOException2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  427 */       if (this.server == null)
/*  428 */         throw new SocketException("Can't connect to SOCKS proxy:" + localObject3.getMessage());
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/*  434 */         privilegedConnect(this.server, this.serverPort, remainingMillis(l1));
/*      */       } catch (IOException localIOException1) {
/*  436 */         throw new SocketException(localIOException1.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  441 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(this.cmdOut, 512);
/*  442 */     Object localObject1 = this.cmdIn;
/*      */ 
/*  444 */     if (this.useV4)
/*      */     {
/*  447 */       if (localInetSocketAddress.isUnresolved())
/*  448 */         throw new UnknownHostException(localInetSocketAddress.toString());
/*  449 */       connectV4((InputStream)localObject1, localBufferedOutputStream, localInetSocketAddress, l1);
/*  450 */       return;
/*      */     }
/*      */ 
/*  454 */     localBufferedOutputStream.write(5);
/*  455 */     localBufferedOutputStream.write(2);
/*  456 */     localBufferedOutputStream.write(0);
/*  457 */     localBufferedOutputStream.write(2);
/*  458 */     localBufferedOutputStream.flush();
/*  459 */     Object localObject2 = new byte[2];
/*  460 */     int i = readSocksReply((InputStream)localObject1, (byte[])localObject2, l1);
/*  461 */     if ((i != 2) || (localObject2[0] != 5))
/*      */     {
/*  466 */       if (localInetSocketAddress.isUnresolved())
/*  467 */         throw new UnknownHostException(localInetSocketAddress.toString());
/*  468 */       connectV4((InputStream)localObject1, localBufferedOutputStream, localInetSocketAddress, l1);
/*  469 */       return;
/*      */     }
/*  471 */     if (localObject2[1] == -1)
/*  472 */       throw new SocketException("SOCKS : No acceptable methods");
/*  473 */     if (!authenticate(localObject2[1], (InputStream)localObject1, localBufferedOutputStream, l1)) {
/*  474 */       throw new SocketException("SOCKS : authentication failed");
/*      */     }
/*  476 */     localBufferedOutputStream.write(5);
/*  477 */     localBufferedOutputStream.write(1);
/*  478 */     localBufferedOutputStream.write(0);
/*      */ 
/*  480 */     if (localInetSocketAddress.isUnresolved()) {
/*  481 */       localBufferedOutputStream.write(3);
/*  482 */       localBufferedOutputStream.write(localInetSocketAddress.getHostName().length());
/*      */       try {
/*  484 */         localBufferedOutputStream.write(localInetSocketAddress.getHostName().getBytes("ISO-8859-1"));
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  486 */         if (!$assertionsDisabled) throw new AssertionError();
/*      */       }
/*  488 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 8 & 0xFF);
/*  489 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 0 & 0xFF);
/*  490 */     } else if ((localInetSocketAddress.getAddress() instanceof Inet6Address)) {
/*  491 */       localBufferedOutputStream.write(4);
/*  492 */       localBufferedOutputStream.write(localInetSocketAddress.getAddress().getAddress());
/*  493 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 8 & 0xFF);
/*  494 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 0 & 0xFF);
/*      */     } else {
/*  496 */       localBufferedOutputStream.write(1);
/*  497 */       localBufferedOutputStream.write(localInetSocketAddress.getAddress().getAddress());
/*  498 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 8 & 0xFF);
/*  499 */       localBufferedOutputStream.write(localInetSocketAddress.getPort() >> 0 & 0xFF);
/*      */     }
/*  501 */     localBufferedOutputStream.flush();
/*  502 */     localObject2 = new byte[4];
/*  503 */     i = readSocksReply((InputStream)localObject1, (byte[])localObject2, l1);
/*  504 */     if (i != 4)
/*  505 */       throw new SocketException("Reply from SOCKS server has bad length");
/*  506 */     SocketException localSocketException = null;
/*      */ 
/*  509 */     switch (localObject2[1])
/*      */     {
/*      */     case 0:
/*      */       byte[] arrayOfByte1;
/*      */       int j;
/*  512 */       switch (localObject2[3]) {
/*      */       case 1:
/*  514 */         arrayOfByte1 = new byte[4];
/*  515 */         i = readSocksReply((InputStream)localObject1, arrayOfByte1, l1);
/*  516 */         if (i != 4)
/*  517 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  518 */         localObject2 = new byte[2];
/*  519 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2, l1);
/*  520 */         if (i != 2)
/*  521 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*      */         break;
/*      */       case 3:
/*  524 */         j = localObject2[1];
/*  525 */         byte[] arrayOfByte2 = new byte[j];
/*  526 */         i = readSocksReply((InputStream)localObject1, arrayOfByte2, l1);
/*  527 */         if (i != j)
/*  528 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  529 */         localObject2 = new byte[2];
/*  530 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2, l1);
/*  531 */         if (i != 2)
/*  532 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*      */         break;
/*      */       case 4:
/*  535 */         j = localObject2[1];
/*  536 */         arrayOfByte1 = new byte[j];
/*  537 */         i = readSocksReply((InputStream)localObject1, arrayOfByte1, l1);
/*  538 */         if (i != j)
/*  539 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  540 */         localObject2 = new byte[2];
/*  541 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2, l1);
/*  542 */         if (i != 2)
/*  543 */           throw new SocketException("Reply from SOCKS server badly formatted"); break;
/*      */       case 2:
/*      */       default:
/*  546 */         localSocketException = new SocketException("Reply from SOCKS server contains wrong code");
/*      */       }
/*      */ 
/*  549 */       break;
/*      */     case 1:
/*  551 */       localSocketException = new SocketException("SOCKS server general failure");
/*  552 */       break;
/*      */     case 2:
/*  554 */       localSocketException = new SocketException("SOCKS: Connection not allowed by ruleset");
/*  555 */       break;
/*      */     case 3:
/*  557 */       localSocketException = new SocketException("SOCKS: Network unreachable");
/*  558 */       break;
/*      */     case 4:
/*  560 */       localSocketException = new SocketException("SOCKS: Host unreachable");
/*  561 */       break;
/*      */     case 5:
/*  563 */       localSocketException = new SocketException("SOCKS: Connection refused");
/*  564 */       break;
/*      */     case 6:
/*  566 */       localSocketException = new SocketException("SOCKS: TTL expired");
/*  567 */       break;
/*      */     case 7:
/*  569 */       localSocketException = new SocketException("SOCKS: Command not supported");
/*  570 */       break;
/*      */     case 8:
/*  572 */       localSocketException = new SocketException("SOCKS: address type not supported");
/*      */     }
/*      */ 
/*  575 */     if (localSocketException != null) {
/*  576 */       ((InputStream)localObject1).close();
/*  577 */       localBufferedOutputStream.close();
/*  578 */       throw localSocketException;
/*      */     }
/*  580 */     this.external_address = localInetSocketAddress;
/*      */   }
/*      */ 
/*      */   private void bindV4(InputStream paramInputStream, OutputStream paramOutputStream, InetAddress paramInetAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*  586 */     if (!(paramInetAddress instanceof Inet4Address)) {
/*  587 */       throw new SocketException("SOCKS V4 requires IPv4 only addresses");
/*      */     }
/*  589 */     super.bind(paramInetAddress, paramInt);
/*  590 */     byte[] arrayOfByte1 = paramInetAddress.getAddress();
/*      */ 
/*  592 */     InetAddress localInetAddress = paramInetAddress;
/*  593 */     if (localInetAddress.isAnyLocalAddress()) {
/*  594 */       localInetAddress = (InetAddress)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public InetAddress run() {
/*  597 */           return SocksSocketImpl.this.cmdsock.getLocalAddress();
/*      */         }
/*      */       });
/*  601 */       arrayOfByte1 = localInetAddress.getAddress();
/*      */     }
/*  603 */     paramOutputStream.write(4);
/*  604 */     paramOutputStream.write(2);
/*  605 */     paramOutputStream.write(super.getLocalPort() >> 8 & 0xFF);
/*  606 */     paramOutputStream.write(super.getLocalPort() >> 0 & 0xFF);
/*  607 */     paramOutputStream.write(arrayOfByte1);
/*  608 */     String str = getUserName();
/*      */     try {
/*  610 */       paramOutputStream.write(str.getBytes("ISO-8859-1"));
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  612 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */     }
/*  614 */     paramOutputStream.write(0);
/*  615 */     paramOutputStream.flush();
/*  616 */     byte[] arrayOfByte2 = new byte[8];
/*  617 */     int i = readSocksReply(paramInputStream, arrayOfByte2);
/*  618 */     if (i != 8)
/*  619 */       throw new SocketException("Reply from SOCKS server has bad length: " + i);
/*  620 */     if ((arrayOfByte2[0] != 0) && (arrayOfByte2[0] != 4))
/*  621 */       throw new SocketException("Reply from SOCKS server has bad version");
/*  622 */     SocketException localSocketException = null;
/*  623 */     switch (arrayOfByte2[1])
/*      */     {
/*      */     case 90:
/*  626 */       this.external_address = new InetSocketAddress(paramInetAddress, paramInt);
/*  627 */       break;
/*      */     case 91:
/*  629 */       localSocketException = new SocketException("SOCKS request rejected");
/*  630 */       break;
/*      */     case 92:
/*  632 */       localSocketException = new SocketException("SOCKS server couldn't reach destination");
/*  633 */       break;
/*      */     case 93:
/*  635 */       localSocketException = new SocketException("SOCKS authentication failed");
/*  636 */       break;
/*      */     default:
/*  638 */       localSocketException = new SocketException("Reply from SOCKS server contains bad status");
/*      */     }
/*      */ 
/*  641 */     if (localSocketException != null) {
/*  642 */       paramInputStream.close();
/*  643 */       paramOutputStream.close();
/*  644 */       throw localSocketException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized void socksBind(InetSocketAddress paramInetSocketAddress)
/*      */     throws IOException
/*      */   {
/*  658 */     if (this.socket != null)
/*      */     {
/*  661 */       return;
/*      */     }
/*      */ 
/*  666 */     if (this.server == null)
/*      */     {
/*  670 */       ProxySelector localProxySelector = (ProxySelector)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public ProxySelector run() {
/*  673 */           return ProxySelector.getDefault();
/*      */         }
/*      */       });
/*  676 */       if (localProxySelector == null)
/*      */       {
/*  680 */         return;
/*      */       }
/*      */ 
/*  684 */       localObject2 = paramInetSocketAddress.getHostString();
/*      */ 
/*  686 */       if (((paramInetSocketAddress.getAddress() instanceof Inet6Address)) && (!((String)localObject2).startsWith("[")) && (((String)localObject2).indexOf(":") >= 0))
/*      */       {
/*  688 */         localObject2 = "[" + (String)localObject2 + "]";
/*      */       }
/*      */       try {
/*  691 */         localObject1 = new URI("serversocket://" + ParseUtil.encodePath((String)localObject2) + ":" + paramInetSocketAddress.getPort());
/*      */       }
/*      */       catch (URISyntaxException localURISyntaxException) {
/*  694 */         if (!$assertionsDisabled) throw new AssertionError(localURISyntaxException);
/*  695 */         localObject1 = null;
/*      */       }
/*  697 */       Proxy localProxy = null;
/*  698 */       Object localObject3 = null;
/*  699 */       Iterator localIterator = null;
/*  700 */       localIterator = localProxySelector.select((URI)localObject1).iterator();
/*  701 */       if ((localIterator == null) || (!localIterator.hasNext())) {
/*  702 */         return;
/*      */       }
/*  704 */       while (localIterator.hasNext()) {
/*  705 */         localProxy = (Proxy)localIterator.next();
/*  706 */         if ((localProxy == null) || (localProxy == Proxy.NO_PROXY)) {
/*  707 */           return;
/*      */         }
/*  709 */         if (localProxy.type() != Proxy.Type.SOCKS)
/*  710 */           throw new SocketException("Unknown proxy type : " + localProxy.type());
/*  711 */         if (!(localProxy.address() instanceof InetSocketAddress)) {
/*  712 */           throw new SocketException("Unknow address type for proxy: " + localProxy);
/*      */         }
/*  714 */         this.server = ((InetSocketAddress)localProxy.address()).getHostString();
/*  715 */         this.serverPort = ((InetSocketAddress)localProxy.address()).getPort();
/*  716 */         if (((localProxy instanceof SocksProxy)) && 
/*  717 */           (((SocksProxy)localProxy).protocolVersion() == 4)) {
/*  718 */           this.useV4 = true;
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  724 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Void run() throws Exception {
/*  727 */               SocksSocketImpl.this.cmdsock = new Socket(new PlainSocketImpl());
/*  728 */               SocksSocketImpl.this.cmdsock.connect(new InetSocketAddress(SocksSocketImpl.this.server, SocksSocketImpl.this.serverPort));
/*  729 */               SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.cmdsock.getInputStream();
/*  730 */               SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.cmdsock.getOutputStream();
/*  731 */               return null;
/*      */             }
/*      */           });
/*      */         }
/*      */         catch (Exception localException2) {
/*  736 */           localProxySelector.connectFailed((URI)localObject1, localProxy.address(), new SocketException(localException2.getMessage()));
/*  737 */           this.server = null;
/*  738 */           this.serverPort = -1;
/*  739 */           this.cmdsock = null;
/*  740 */           localObject3 = localException2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  749 */       if ((this.server == null) || (this.cmdsock == null))
/*  750 */         throw new SocketException("Can't connect to SOCKS proxy:" + localObject3.getMessage());
/*      */     }
/*      */     else
/*      */     {
/*      */       try {
/*  755 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public Void run() throws Exception {
/*  758 */             SocksSocketImpl.this.cmdsock = new Socket(new PlainSocketImpl());
/*  759 */             SocksSocketImpl.this.cmdsock.connect(new InetSocketAddress(SocksSocketImpl.this.server, SocksSocketImpl.this.serverPort));
/*  760 */             SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.cmdsock.getInputStream();
/*  761 */             SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.cmdsock.getOutputStream();
/*  762 */             return null;
/*      */           } } );
/*      */       }
/*      */       catch (Exception localException1) {
/*  766 */         throw new SocketException(localException1.getMessage());
/*      */       }
/*      */     }
/*  769 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(this.cmdOut, 512);
/*  770 */     Object localObject1 = this.cmdIn;
/*  771 */     if (this.useV4) {
/*  772 */       bindV4((InputStream)localObject1, localBufferedOutputStream, paramInetSocketAddress.getAddress(), paramInetSocketAddress.getPort());
/*  773 */       return;
/*      */     }
/*  775 */     localBufferedOutputStream.write(5);
/*  776 */     localBufferedOutputStream.write(2);
/*  777 */     localBufferedOutputStream.write(0);
/*  778 */     localBufferedOutputStream.write(2);
/*  779 */     localBufferedOutputStream.flush();
/*  780 */     Object localObject2 = new byte[2];
/*  781 */     int i = readSocksReply((InputStream)localObject1, (byte[])localObject2);
/*  782 */     if ((i != 2) || (localObject2[0] != 5))
/*      */     {
/*  785 */       bindV4((InputStream)localObject1, localBufferedOutputStream, paramInetSocketAddress.getAddress(), paramInetSocketAddress.getPort());
/*  786 */       return;
/*      */     }
/*  788 */     if (localObject2[1] == -1)
/*  789 */       throw new SocketException("SOCKS : No acceptable methods");
/*  790 */     if (!authenticate(localObject2[1], (InputStream)localObject1, localBufferedOutputStream)) {
/*  791 */       throw new SocketException("SOCKS : authentication failed");
/*      */     }
/*      */ 
/*  794 */     localBufferedOutputStream.write(5);
/*  795 */     localBufferedOutputStream.write(2);
/*  796 */     localBufferedOutputStream.write(0);
/*  797 */     int j = paramInetSocketAddress.getPort();
/*  798 */     if (paramInetSocketAddress.isUnresolved()) {
/*  799 */       localBufferedOutputStream.write(3);
/*  800 */       localBufferedOutputStream.write(paramInetSocketAddress.getHostName().length());
/*      */       try {
/*  802 */         localBufferedOutputStream.write(paramInetSocketAddress.getHostName().getBytes("ISO-8859-1"));
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  804 */         if (!$assertionsDisabled) throw new AssertionError();
/*      */       }
/*  806 */       localBufferedOutputStream.write(j >> 8 & 0xFF);
/*  807 */       localBufferedOutputStream.write(j >> 0 & 0xFF);
/*  808 */     } else if ((paramInetSocketAddress.getAddress() instanceof Inet4Address)) {
/*  809 */       localObject4 = paramInetSocketAddress.getAddress().getAddress();
/*  810 */       localBufferedOutputStream.write(1);
/*  811 */       localBufferedOutputStream.write((byte[])localObject4);
/*  812 */       localBufferedOutputStream.write(j >> 8 & 0xFF);
/*  813 */       localBufferedOutputStream.write(j >> 0 & 0xFF);
/*  814 */       localBufferedOutputStream.flush();
/*  815 */     } else if ((paramInetSocketAddress.getAddress() instanceof Inet6Address)) {
/*  816 */       localObject4 = paramInetSocketAddress.getAddress().getAddress();
/*  817 */       localBufferedOutputStream.write(4);
/*  818 */       localBufferedOutputStream.write((byte[])localObject4);
/*  819 */       localBufferedOutputStream.write(j >> 8 & 0xFF);
/*  820 */       localBufferedOutputStream.write(j >> 0 & 0xFF);
/*  821 */       localBufferedOutputStream.flush();
/*      */     } else {
/*  823 */       this.cmdsock.close();
/*  824 */       throw new SocketException("unsupported address type : " + paramInetSocketAddress);
/*      */     }
/*  826 */     localObject2 = new byte[4];
/*  827 */     i = readSocksReply((InputStream)localObject1, (byte[])localObject2);
/*  828 */     Object localObject4 = null;
/*      */ 
/*  831 */     switch (localObject2[1])
/*      */     {
/*      */     case 0:
/*      */       byte[] arrayOfByte1;
/*      */       int m;
/*      */       int k;
/*  834 */       switch (localObject2[3]) {
/*      */       case 1:
/*  836 */         arrayOfByte1 = new byte[4];
/*  837 */         i = readSocksReply((InputStream)localObject1, arrayOfByte1);
/*  838 */         if (i != 4)
/*  839 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  840 */         localObject2 = new byte[2];
/*  841 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2);
/*  842 */         if (i != 2)
/*  843 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  844 */         m = (localObject2[0] & 0xFF) << 8;
/*  845 */         m += (localObject2[1] & 0xFF);
/*  846 */         this.external_address = new InetSocketAddress(new Inet4Address("", arrayOfByte1), m);
/*      */ 
/*  848 */         break;
/*      */       case 3:
/*  850 */         k = localObject2[1];
/*  851 */         byte[] arrayOfByte2 = new byte[k];
/*  852 */         i = readSocksReply((InputStream)localObject1, arrayOfByte2);
/*  853 */         if (i != k)
/*  854 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  855 */         localObject2 = new byte[2];
/*  856 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2);
/*  857 */         if (i != 2)
/*  858 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  859 */         m = (localObject2[0] & 0xFF) << 8;
/*  860 */         m += (localObject2[1] & 0xFF);
/*  861 */         this.external_address = new InetSocketAddress(new String(arrayOfByte2), m);
/*  862 */         break;
/*      */       case 4:
/*  864 */         k = localObject2[1];
/*  865 */         arrayOfByte1 = new byte[k];
/*  866 */         i = readSocksReply((InputStream)localObject1, arrayOfByte1);
/*  867 */         if (i != k)
/*  868 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  869 */         localObject2 = new byte[2];
/*  870 */         i = readSocksReply((InputStream)localObject1, (byte[])localObject2);
/*  871 */         if (i != 2)
/*  872 */           throw new SocketException("Reply from SOCKS server badly formatted");
/*  873 */         m = (localObject2[0] & 0xFF) << 8;
/*  874 */         m += (localObject2[1] & 0xFF);
/*  875 */         this.external_address = new InetSocketAddress(new Inet6Address("", arrayOfByte1), m);
/*      */       case 2:
/*      */       }
/*      */ 
/*  879 */       break;
/*      */     case 1:
/*  881 */       localObject4 = new SocketException("SOCKS server general failure");
/*  882 */       break;
/*      */     case 2:
/*  884 */       localObject4 = new SocketException("SOCKS: Bind not allowed by ruleset");
/*  885 */       break;
/*      */     case 3:
/*  887 */       localObject4 = new SocketException("SOCKS: Network unreachable");
/*  888 */       break;
/*      */     case 4:
/*  890 */       localObject4 = new SocketException("SOCKS: Host unreachable");
/*  891 */       break;
/*      */     case 5:
/*  893 */       localObject4 = new SocketException("SOCKS: Connection refused");
/*  894 */       break;
/*      */     case 6:
/*  896 */       localObject4 = new SocketException("SOCKS: TTL expired");
/*  897 */       break;
/*      */     case 7:
/*  899 */       localObject4 = new SocketException("SOCKS: Command not supported");
/*  900 */       break;
/*      */     case 8:
/*  902 */       localObject4 = new SocketException("SOCKS: address type not supported");
/*      */     }
/*      */ 
/*  905 */     if (localObject4 != null) {
/*  906 */       ((InputStream)localObject1).close();
/*  907 */       localBufferedOutputStream.close();
/*  908 */       this.cmdsock.close();
/*  909 */       this.cmdsock = null;
/*  910 */       throw ((Throwable)localObject4);
/*      */     }
/*  912 */     this.cmdIn = ((InputStream)localObject1);
/*  913 */     this.cmdOut = localBufferedOutputStream;
/*      */   }
/*      */ 
/*      */   protected void acceptFrom(SocketImpl paramSocketImpl, InetSocketAddress paramInetSocketAddress)
/*      */     throws IOException
/*      */   {
/*  926 */     if (this.cmdsock == null)
/*      */     {
/*  928 */       return;
/*      */     }
/*  930 */     InputStream localInputStream = this.cmdIn;
/*      */ 
/*  932 */     socksBind(paramInetSocketAddress);
/*  933 */     localInputStream.read();
/*  934 */     int i = localInputStream.read();
/*  935 */     localInputStream.read();
/*  936 */     SocketException localSocketException = null;
/*      */ 
/*  939 */     InetSocketAddress localInetSocketAddress = null;
/*  940 */     switch (i)
/*      */     {
/*      */     case 0:
/*  943 */       i = localInputStream.read();
/*      */       byte[] arrayOfByte;
/*      */       int j;
/*  944 */       switch (i) {
/*      */       case 1:
/*  946 */         arrayOfByte = new byte[4];
/*  947 */         readSocksReply(localInputStream, arrayOfByte);
/*  948 */         j = localInputStream.read() << 8;
/*  949 */         j += localInputStream.read();
/*  950 */         localInetSocketAddress = new InetSocketAddress(new Inet4Address("", arrayOfByte), j);
/*      */ 
/*  952 */         break;
/*      */       case 3:
/*  954 */         int k = localInputStream.read();
/*  955 */         arrayOfByte = new byte[k];
/*  956 */         readSocksReply(localInputStream, arrayOfByte);
/*  957 */         j = localInputStream.read() << 8;
/*  958 */         j += localInputStream.read();
/*  959 */         localInetSocketAddress = new InetSocketAddress(new String(arrayOfByte), j);
/*  960 */         break;
/*      */       case 4:
/*  962 */         arrayOfByte = new byte[16];
/*  963 */         readSocksReply(localInputStream, arrayOfByte);
/*  964 */         j = localInputStream.read() << 8;
/*  965 */         j += localInputStream.read();
/*  966 */         localInetSocketAddress = new InetSocketAddress(new Inet6Address("", arrayOfByte), j);
/*      */       case 2:
/*      */       }
/*      */ 
/*  970 */       break;
/*      */     case 1:
/*  972 */       localSocketException = new SocketException("SOCKS server general failure");
/*  973 */       break;
/*      */     case 2:
/*  975 */       localSocketException = new SocketException("SOCKS: Accept not allowed by ruleset");
/*  976 */       break;
/*      */     case 3:
/*  978 */       localSocketException = new SocketException("SOCKS: Network unreachable");
/*  979 */       break;
/*      */     case 4:
/*  981 */       localSocketException = new SocketException("SOCKS: Host unreachable");
/*  982 */       break;
/*      */     case 5:
/*  984 */       localSocketException = new SocketException("SOCKS: Connection refused");
/*  985 */       break;
/*      */     case 6:
/*  987 */       localSocketException = new SocketException("SOCKS: TTL expired");
/*  988 */       break;
/*      */     case 7:
/*  990 */       localSocketException = new SocketException("SOCKS: Command not supported");
/*  991 */       break;
/*      */     case 8:
/*  993 */       localSocketException = new SocketException("SOCKS: address type not supported");
/*      */     }
/*      */ 
/*  996 */     if (localSocketException != null) {
/*  997 */       this.cmdIn.close();
/*  998 */       this.cmdOut.close();
/*  999 */       this.cmdsock.close();
/* 1000 */       this.cmdsock = null;
/* 1001 */       throw localSocketException;
/*      */     }
/*      */ 
/* 1009 */     if ((paramSocketImpl instanceof SocksSocketImpl)) {
/* 1010 */       ((SocksSocketImpl)paramSocketImpl).external_address = localInetSocketAddress;
/*      */     }
/* 1012 */     if ((paramSocketImpl instanceof PlainSocketImpl)) {
/* 1013 */       PlainSocketImpl localPlainSocketImpl = (PlainSocketImpl)paramSocketImpl;
/* 1014 */       localPlainSocketImpl.setInputStream((SocketInputStream)localInputStream);
/* 1015 */       localPlainSocketImpl.setFileDescriptor(this.cmdsock.getImpl().getFileDescriptor());
/* 1016 */       localPlainSocketImpl.setAddress(this.cmdsock.getImpl().getInetAddress());
/* 1017 */       localPlainSocketImpl.setPort(this.cmdsock.getImpl().getPort());
/* 1018 */       localPlainSocketImpl.setLocalPort(this.cmdsock.getImpl().getLocalPort());
/*      */     } else {
/* 1020 */       paramSocketImpl.fd = this.cmdsock.getImpl().fd;
/* 1021 */       paramSocketImpl.address = this.cmdsock.getImpl().address;
/* 1022 */       paramSocketImpl.port = this.cmdsock.getImpl().port;
/* 1023 */       paramSocketImpl.localport = this.cmdsock.getImpl().localport;
/*      */     }
/*      */ 
/* 1030 */     this.cmdsock = null;
/*      */   }
/*      */ 
/*      */   protected InetAddress getInetAddress()
/*      */   {
/* 1042 */     if (this.external_address != null) {
/* 1043 */       return this.external_address.getAddress();
/*      */     }
/* 1045 */     return super.getInetAddress();
/*      */   }
/*      */ 
/*      */   protected int getPort()
/*      */   {
/* 1056 */     if (this.external_address != null) {
/* 1057 */       return this.external_address.getPort();
/*      */     }
/* 1059 */     return super.getPort();
/*      */   }
/*      */ 
/*      */   protected int getLocalPort()
/*      */   {
/* 1064 */     if (this.socket != null)
/* 1065 */       return super.getLocalPort();
/* 1066 */     if (this.external_address != null) {
/* 1067 */       return this.external_address.getPort();
/*      */     }
/* 1069 */     return super.getLocalPort();
/*      */   }
/*      */ 
/*      */   protected void close() throws IOException
/*      */   {
/* 1074 */     if (this.cmdsock != null)
/* 1075 */       this.cmdsock.close();
/* 1076 */     this.cmdsock = null;
/* 1077 */     super.close();
/*      */   }
/*      */ 
/*      */   private String getUserName() {
/* 1081 */     String str = "";
/* 1082 */     if (this.applicationSetProxy)
/*      */       try {
/* 1084 */         str = System.getProperty("user.name");
/*      */       } catch (SecurityException localSecurityException) {
/*      */       }
/* 1087 */     else str = (String)AccessController.doPrivileged(new GetPropertyAction("user.name"));
/*      */ 
/* 1090 */     return str;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.SocksSocketImpl
 * JD-Core Version:    0.6.2
 */