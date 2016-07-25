/*      */ package java.net;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import sun.net.ApplicationProxy;
/*      */ 
/*      */ public class Socket
/*      */   implements Closeable
/*      */ {
/*   59 */   private boolean created = false;
/*   60 */   private boolean bound = false;
/*   61 */   private boolean connected = false;
/*   62 */   private boolean closed = false;
/*   63 */   private Object closeLock = new Object();
/*   64 */   private boolean shutIn = false;
/*   65 */   private boolean shutOut = false;
/*      */   SocketImpl impl;
/*   75 */   private boolean oldImpl = false;
/*      */ 
/* 1592 */   private static SocketImplFactory factory = null;
/*      */ 
/*      */   public Socket()
/*      */   {
/*   85 */     setImpl();
/*      */   }
/*      */ 
/*      */   public Socket(Proxy paramProxy)
/*      */   {
/*  118 */     if (paramProxy == null) {
/*  119 */       throw new IllegalArgumentException("Invalid Proxy");
/*      */     }
/*  121 */     ApplicationProxy localApplicationProxy = paramProxy == Proxy.NO_PROXY ? Proxy.NO_PROXY : ApplicationProxy.create(paramProxy);
/*  122 */     if (localApplicationProxy.type() == Proxy.Type.SOCKS) {
/*  123 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  124 */       InetSocketAddress localInetSocketAddress = (InetSocketAddress)localApplicationProxy.address();
/*  125 */       if (localInetSocketAddress.getAddress() != null) {
/*  126 */         checkAddress(localInetSocketAddress.getAddress(), "Socket");
/*      */       }
/*  128 */       if (localSecurityManager != null) {
/*  129 */         if (localInetSocketAddress.isUnresolved())
/*  130 */           localInetSocketAddress = new InetSocketAddress(localInetSocketAddress.getHostName(), localInetSocketAddress.getPort());
/*  131 */         if (localInetSocketAddress.isUnresolved())
/*  132 */           localSecurityManager.checkConnect(localInetSocketAddress.getHostName(), localInetSocketAddress.getPort());
/*      */         else {
/*  134 */           localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */         }
/*      */       }
/*  137 */       this.impl = new SocksSocketImpl(localApplicationProxy);
/*  138 */       this.impl.setSocket(this);
/*      */     }
/*  140 */     else if (localApplicationProxy == Proxy.NO_PROXY) {
/*  141 */       if (factory == null) {
/*  142 */         this.impl = new PlainSocketImpl();
/*  143 */         this.impl.setSocket(this);
/*      */       } else {
/*  145 */         setImpl();
/*      */       }
/*      */     } else { throw new IllegalArgumentException("Invalid Proxy"); }
/*      */ 
/*      */   }
/*      */ 
/*      */   protected Socket(SocketImpl paramSocketImpl)
/*      */     throws SocketException
/*      */   {
/*  163 */     this.impl = paramSocketImpl;
/*  164 */     if (paramSocketImpl != null) {
/*  165 */       checkOldImpl();
/*  166 */       this.impl.setSocket(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Socket(String paramString, int paramInt)
/*      */     throws UnknownHostException, IOException
/*      */   {
/*  208 */     this(paramString != null ? new InetSocketAddress(paramString, paramInt) : new InetSocketAddress(InetAddress.getByName(null), paramInt), (SocketAddress)null, true);
/*      */   }
/*      */ 
/*      */   public Socket(InetAddress paramInetAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*  241 */     this(paramInetAddress != null ? new InetSocketAddress(paramInetAddress, paramInt) : null, (SocketAddress)null, true);
/*      */   }
/*      */ 
/*      */   public Socket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  280 */     this(paramString != null ? new InetSocketAddress(paramString, paramInt1) : new InetSocketAddress(InetAddress.getByName(null), paramInt1), new InetSocketAddress(paramInetAddress, paramInt2), true);
/*      */   }
/*      */ 
/*      */   public Socket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  319 */     this(paramInetAddress1 != null ? new InetSocketAddress(paramInetAddress1, paramInt1) : null, new InetSocketAddress(paramInetAddress2, paramInt2), true);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Socket(String paramString, int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  365 */     this(paramString != null ? new InetSocketAddress(paramString, paramInt) : new InetSocketAddress(InetAddress.getByName(null), paramInt), (SocketAddress)null, paramBoolean);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Socket(InetAddress paramInetAddress, int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  408 */     this(paramInetAddress != null ? new InetSocketAddress(paramInetAddress, paramInt) : null, new InetSocketAddress(0), paramBoolean);
/*      */   }
/*      */ 
/*      */   private Socket(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  414 */     setImpl();
/*      */ 
/*  417 */     if (paramSocketAddress1 == null)
/*  418 */       throw new NullPointerException();
/*      */     try
/*      */     {
/*  421 */       createImpl(paramBoolean);
/*  422 */       if (paramSocketAddress2 != null)
/*  423 */         bind(paramSocketAddress2);
/*  424 */       if (paramSocketAddress1 != null)
/*  425 */         connect(paramSocketAddress1);
/*      */     } catch (IOException localIOException) {
/*  427 */       close();
/*  428 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   void createImpl(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/*  441 */     if (this.impl == null)
/*  442 */       setImpl();
/*      */     try {
/*  444 */       this.impl.create(paramBoolean);
/*  445 */       this.created = true;
/*      */     } catch (IOException localIOException) {
/*  447 */       throw new SocketException(localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkOldImpl() {
/*  452 */     if (this.impl == null) {
/*  453 */       return;
/*      */     }
/*      */ 
/*  457 */     this.oldImpl = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/*  460 */         Class[] arrayOfClass = new Class[2];
/*  461 */         arrayOfClass[0] = SocketAddress.class;
/*  462 */         arrayOfClass[1] = Integer.TYPE;
/*  463 */         Class localClass = Socket.this.impl.getClass();
/*      */         while (true)
/*      */           try {
/*  466 */             localClass.getDeclaredMethod("connect", arrayOfClass);
/*  467 */             return Boolean.FALSE;
/*      */           } catch (NoSuchMethodException localNoSuchMethodException) {
/*  469 */             localClass = localClass.getSuperclass();
/*      */ 
/*  473 */             if (localClass.equals(SocketImpl.class))
/*  474 */               return Boolean.TRUE;
/*      */           }
/*      */       }
/*      */     })).booleanValue();
/*      */   }
/*      */ 
/*      */   void setImpl()
/*      */   {
/*  487 */     if (factory != null) {
/*  488 */       this.impl = factory.createSocketImpl();
/*  489 */       checkOldImpl();
/*      */     }
/*      */     else
/*      */     {
/*  493 */       this.impl = new SocksSocketImpl();
/*      */     }
/*  495 */     if (this.impl != null)
/*  496 */       this.impl.setSocket(this);
/*      */   }
/*      */ 
/*      */   SocketImpl getImpl()
/*      */     throws SocketException
/*      */   {
/*  509 */     if (!this.created)
/*  510 */       createImpl(true);
/*  511 */     return this.impl;
/*      */   }
/*      */ 
/*      */   public void connect(SocketAddress paramSocketAddress)
/*      */     throws IOException
/*      */   {
/*  528 */     connect(paramSocketAddress, 0);
/*      */   }
/*      */ 
/*      */   public void connect(SocketAddress paramSocketAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*  549 */     if (paramSocketAddress == null) {
/*  550 */       throw new IllegalArgumentException("connect: The address can't be null");
/*      */     }
/*  552 */     if (paramInt < 0) {
/*  553 */       throw new IllegalArgumentException("connect: timeout can't be negative");
/*      */     }
/*  555 */     if (isClosed()) {
/*  556 */       throw new SocketException("Socket is closed");
/*      */     }
/*  558 */     if ((!this.oldImpl) && (isConnected())) {
/*  559 */       throw new SocketException("already connected");
/*      */     }
/*  561 */     if (!(paramSocketAddress instanceof InetSocketAddress)) {
/*  562 */       throw new IllegalArgumentException("Unsupported address type");
/*      */     }
/*  564 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/*  565 */     InetAddress localInetAddress = localInetSocketAddress.getAddress();
/*  566 */     int i = localInetSocketAddress.getPort();
/*  567 */     checkAddress(localInetAddress, "connect");
/*      */ 
/*  569 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  570 */     if (localSecurityManager != null) {
/*  571 */       if (localInetSocketAddress.isUnresolved())
/*  572 */         localSecurityManager.checkConnect(localInetSocketAddress.getHostName(), i);
/*      */       else
/*  574 */         localSecurityManager.checkConnect(localInetAddress.getHostAddress(), i);
/*      */     }
/*  576 */     if (!this.created)
/*  577 */       createImpl(true);
/*  578 */     if (!this.oldImpl)
/*  579 */       this.impl.connect(localInetSocketAddress, paramInt);
/*  580 */     else if (paramInt == 0) {
/*  581 */       if (localInetSocketAddress.isUnresolved())
/*  582 */         this.impl.connect(localInetAddress.getHostName(), i);
/*      */       else
/*  584 */         this.impl.connect(localInetAddress, i);
/*      */     }
/*  586 */     else throw new UnsupportedOperationException("SocketImpl.connect(addr, timeout)");
/*  587 */     this.connected = true;
/*      */ 
/*  592 */     this.bound = true;
/*      */   }
/*      */ 
/*      */   public void bind(SocketAddress paramSocketAddress)
/*      */     throws IOException
/*      */   {
/*  611 */     if (isClosed())
/*  612 */       throw new SocketException("Socket is closed");
/*  613 */     if ((!this.oldImpl) && (isBound())) {
/*  614 */       throw new SocketException("Already bound");
/*      */     }
/*  616 */     if ((paramSocketAddress != null) && (!(paramSocketAddress instanceof InetSocketAddress)))
/*  617 */       throw new IllegalArgumentException("Unsupported address type");
/*  618 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/*  619 */     if ((localInetSocketAddress != null) && (localInetSocketAddress.isUnresolved()))
/*  620 */       throw new SocketException("Unresolved address");
/*  621 */     if (localInetSocketAddress == null) {
/*  622 */       localInetSocketAddress = new InetSocketAddress(0);
/*      */     }
/*  624 */     InetAddress localInetAddress = localInetSocketAddress.getAddress();
/*  625 */     int i = localInetSocketAddress.getPort();
/*  626 */     checkAddress(localInetAddress, "bind");
/*  627 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  628 */     if (localSecurityManager != null) {
/*  629 */       localSecurityManager.checkListen(i);
/*      */     }
/*  631 */     getImpl().bind(localInetAddress, i);
/*  632 */     this.bound = true;
/*      */   }
/*      */ 
/*      */   private void checkAddress(InetAddress paramInetAddress, String paramString) {
/*  636 */     if (paramInetAddress == null) {
/*  637 */       return;
/*      */     }
/*  639 */     if ((!(paramInetAddress instanceof Inet4Address)) && (!(paramInetAddress instanceof Inet6Address)))
/*  640 */       throw new IllegalArgumentException(paramString + ": invalid address type");
/*      */   }
/*      */ 
/*      */   final void postAccept()
/*      */   {
/*  648 */     this.connected = true;
/*  649 */     this.created = true;
/*  650 */     this.bound = true;
/*      */   }
/*      */ 
/*      */   void setCreated() {
/*  654 */     this.created = true;
/*      */   }
/*      */ 
/*      */   void setBound() {
/*  658 */     this.bound = true;
/*      */   }
/*      */ 
/*      */   void setConnected() {
/*  662 */     this.connected = true;
/*      */   }
/*      */ 
/*      */   public InetAddress getInetAddress()
/*      */   {
/*  676 */     if (!isConnected())
/*  677 */       return null;
/*      */     try {
/*  679 */       return getImpl().getInetAddress();
/*      */     } catch (SocketException localSocketException) {
/*      */     }
/*  682 */     return null;
/*      */   }
/*      */ 
/*      */   public InetAddress getLocalAddress()
/*      */   {
/*  695 */     if (!isBound())
/*  696 */       return InetAddress.anyLocalAddress();
/*  697 */     InetAddress localInetAddress = null;
/*      */     try {
/*  699 */       localInetAddress = (InetAddress)getImpl().getOption(15);
/*      */ 
/*  701 */       if (!NetUtil.doRevealLocalAddress()) {
/*  702 */         SecurityManager localSecurityManager = System.getSecurityManager();
/*  703 */         if (localSecurityManager != null)
/*  704 */           localSecurityManager.checkConnect(localInetAddress.getHostAddress(), -1);
/*      */       }
/*  706 */       if (localInetAddress.isAnyLocalAddress())
/*  707 */         localInetAddress = InetAddress.anyLocalAddress();
/*      */     }
/*      */     catch (SecurityException localSecurityException) {
/*  710 */       localInetAddress = InetAddress.getLoopbackAddress();
/*      */     } catch (Exception localException) {
/*  712 */       localInetAddress = InetAddress.anyLocalAddress();
/*      */     }
/*  714 */     return localInetAddress;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  728 */     if (!isConnected())
/*  729 */       return 0;
/*      */     try {
/*  731 */       return getImpl().getPort();
/*      */     }
/*      */     catch (SocketException localSocketException) {
/*      */     }
/*  735 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLocalPort()
/*      */   {
/*  749 */     if (!isBound())
/*  750 */       return -1;
/*      */     try {
/*  752 */       return getImpl().getLocalPort();
/*      */     }
/*      */     catch (SocketException localSocketException) {
/*      */     }
/*  756 */     return -1;
/*      */   }
/*      */ 
/*      */   public SocketAddress getRemoteSocketAddress()
/*      */   {
/*  777 */     if (!isConnected())
/*  778 */       return null;
/*  779 */     return new InetSocketAddress(getInetAddress(), getPort());
/*      */   }
/*      */ 
/*      */   public SocketAddress getLocalSocketAddress()
/*      */   {
/*  803 */     if (!isBound())
/*  804 */       return null;
/*  805 */     return new InetSocketAddress(getLocalAddress(), getLocalPort());
/*      */   }
/*      */ 
/*      */   public SocketChannel getChannel()
/*      */   {
/*  826 */     return null;
/*      */   }
/*      */ 
/*      */   public InputStream getInputStream()
/*      */     throws IOException
/*      */   {
/*  875 */     if (isClosed())
/*  876 */       throw new SocketException("Socket is closed");
/*  877 */     if (!isConnected())
/*  878 */       throw new SocketException("Socket is not connected");
/*  879 */     if (isInputShutdown())
/*  880 */       throw new SocketException("Socket input is shutdown");
/*  881 */     Socket localSocket = this;
/*  882 */     InputStream localInputStream = null;
/*      */     try {
/*  884 */       localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public InputStream run() throws IOException {
/*  887 */           return Socket.this.impl.getInputStream();
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/*  891 */       throw ((IOException)localPrivilegedActionException.getException());
/*      */     }
/*  893 */     return localInputStream;
/*      */   }
/*      */ 
/*      */   public OutputStream getOutputStream()
/*      */     throws IOException
/*      */   {
/*  915 */     if (isClosed())
/*  916 */       throw new SocketException("Socket is closed");
/*  917 */     if (!isConnected())
/*  918 */       throw new SocketException("Socket is not connected");
/*  919 */     if (isOutputShutdown())
/*  920 */       throw new SocketException("Socket output is shutdown");
/*  921 */     Socket localSocket = this;
/*  922 */     OutputStream localOutputStream = null;
/*      */     try {
/*  924 */       localOutputStream = (OutputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public OutputStream run() throws IOException {
/*  927 */           return Socket.this.impl.getOutputStream();
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/*  931 */       throw ((IOException)localPrivilegedActionException.getException());
/*      */     }
/*  933 */     return localOutputStream;
/*      */   }
/*      */ 
/*      */   public void setTcpNoDelay(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/*  950 */     if (isClosed())
/*  951 */       throw new SocketException("Socket is closed");
/*  952 */     getImpl().setOption(1, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public boolean getTcpNoDelay()
/*      */     throws SocketException
/*      */   {
/*  965 */     if (isClosed())
/*  966 */       throw new SocketException("Socket is closed");
/*  967 */     return ((Boolean)getImpl().getOption(1)).booleanValue();
/*      */   }
/*      */ 
/*      */   public void setSoLinger(boolean paramBoolean, int paramInt)
/*      */     throws SocketException
/*      */   {
/*  985 */     if (isClosed())
/*  986 */       throw new SocketException("Socket is closed");
/*  987 */     if (!paramBoolean) {
/*  988 */       getImpl().setOption(128, new Boolean(paramBoolean));
/*      */     } else {
/*  990 */       if (paramInt < 0) {
/*  991 */         throw new IllegalArgumentException("invalid value for SO_LINGER");
/*      */       }
/*  993 */       if (paramInt > 65535)
/*  994 */         paramInt = 65535;
/*  995 */       getImpl().setOption(128, new Integer(paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getSoLinger()
/*      */     throws SocketException
/*      */   {
/* 1012 */     if (isClosed())
/* 1013 */       throw new SocketException("Socket is closed");
/* 1014 */     Object localObject = getImpl().getOption(128);
/* 1015 */     if ((localObject instanceof Integer)) {
/* 1016 */       return ((Integer)localObject).intValue();
/*      */     }
/* 1018 */     return -1;
/*      */   }
/*      */ 
/*      */   public void sendUrgentData(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1033 */     if (!getImpl().supportsUrgentData()) {
/* 1034 */       throw new SocketException("Urgent data not supported");
/*      */     }
/* 1036 */     getImpl().sendUrgentData(paramInt);
/*      */   }
/*      */ 
/*      */   public void setOOBInline(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/* 1063 */     if (isClosed())
/* 1064 */       throw new SocketException("Socket is closed");
/* 1065 */     getImpl().setOption(4099, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public boolean getOOBInline()
/*      */     throws SocketException
/*      */   {
/* 1078 */     if (isClosed())
/* 1079 */       throw new SocketException("Socket is closed");
/* 1080 */     return ((Boolean)getImpl().getOption(4099)).booleanValue();
/*      */   }
/*      */ 
/*      */   public synchronized void setSoTimeout(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1100 */     if (isClosed())
/* 1101 */       throw new SocketException("Socket is closed");
/* 1102 */     if (paramInt < 0) {
/* 1103 */       throw new IllegalArgumentException("timeout can't be negative");
/*      */     }
/* 1105 */     getImpl().setOption(4102, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getSoTimeout()
/*      */     throws SocketException
/*      */   {
/* 1118 */     if (isClosed())
/* 1119 */       throw new SocketException("Socket is closed");
/* 1120 */     Object localObject = getImpl().getOption(4102);
/*      */ 
/* 1122 */     if ((localObject instanceof Integer)) {
/* 1123 */       return ((Integer)localObject).intValue();
/*      */     }
/* 1125 */     return 0;
/*      */   }
/*      */ 
/*      */   public synchronized void setSendBufferSize(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1153 */     if (paramInt <= 0) {
/* 1154 */       throw new IllegalArgumentException("negative send size");
/*      */     }
/* 1156 */     if (isClosed())
/* 1157 */       throw new SocketException("Socket is closed");
/* 1158 */     getImpl().setOption(4097, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getSendBufferSize()
/*      */     throws SocketException
/*      */   {
/* 1174 */     if (isClosed())
/* 1175 */       throw new SocketException("Socket is closed");
/* 1176 */     int i = 0;
/* 1177 */     Object localObject = getImpl().getOption(4097);
/* 1178 */     if ((localObject instanceof Integer)) {
/* 1179 */       i = ((Integer)localObject).intValue();
/*      */     }
/* 1181 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void setReceiveBufferSize(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1225 */     if (paramInt <= 0) {
/* 1226 */       throw new IllegalArgumentException("invalid receive size");
/*      */     }
/* 1228 */     if (isClosed())
/* 1229 */       throw new SocketException("Socket is closed");
/* 1230 */     getImpl().setOption(4098, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getReceiveBufferSize()
/*      */     throws SocketException
/*      */   {
/* 1246 */     if (isClosed())
/* 1247 */       throw new SocketException("Socket is closed");
/* 1248 */     int i = 0;
/* 1249 */     Object localObject = getImpl().getOption(4098);
/* 1250 */     if ((localObject instanceof Integer)) {
/* 1251 */       i = ((Integer)localObject).intValue();
/*      */     }
/* 1253 */     return i;
/*      */   }
/*      */ 
/*      */   public void setKeepAlive(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/* 1266 */     if (isClosed())
/* 1267 */       throw new SocketException("Socket is closed");
/* 1268 */     getImpl().setOption(8, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public boolean getKeepAlive()
/*      */     throws SocketException
/*      */   {
/* 1281 */     if (isClosed())
/* 1282 */       throw new SocketException("Socket is closed");
/* 1283 */     return ((Boolean)getImpl().getOption(8)).booleanValue();
/*      */   }
/*      */ 
/*      */   public void setTrafficClass(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1332 */     if ((paramInt < 0) || (paramInt > 255)) {
/* 1333 */       throw new IllegalArgumentException("tc is not in range 0 -- 255");
/*      */     }
/* 1335 */     if (isClosed())
/* 1336 */       throw new SocketException("Socket is closed");
/* 1337 */     getImpl().setOption(3, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public int getTrafficClass()
/*      */     throws SocketException
/*      */   {
/* 1356 */     return ((Integer)getImpl().getOption(3)).intValue();
/*      */   }
/*      */ 
/*      */   public void setReuseAddress(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/* 1394 */     if (isClosed())
/* 1395 */       throw new SocketException("Socket is closed");
/* 1396 */     getImpl().setOption(4, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public boolean getReuseAddress()
/*      */     throws SocketException
/*      */   {
/* 1409 */     if (isClosed())
/* 1410 */       throw new SocketException("Socket is closed");
/* 1411 */     return ((Boolean)getImpl().getOption(4)).booleanValue();
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws IOException
/*      */   {
/* 1437 */     synchronized (this.closeLock) {
/* 1438 */       if (isClosed())
/* 1439 */         return;
/* 1440 */       if (this.created)
/* 1441 */         this.impl.close();
/* 1442 */       this.closed = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void shutdownInput()
/*      */     throws IOException
/*      */   {
/* 1465 */     if (isClosed())
/* 1466 */       throw new SocketException("Socket is closed");
/* 1467 */     if (!isConnected())
/* 1468 */       throw new SocketException("Socket is not connected");
/* 1469 */     if (isInputShutdown())
/* 1470 */       throw new SocketException("Socket input is already shutdown");
/* 1471 */     getImpl().shutdownInput();
/* 1472 */     this.shutIn = true;
/*      */   }
/*      */ 
/*      */   public void shutdownOutput()
/*      */     throws IOException
/*      */   {
/* 1495 */     if (isClosed())
/* 1496 */       throw new SocketException("Socket is closed");
/* 1497 */     if (!isConnected())
/* 1498 */       throw new SocketException("Socket is not connected");
/* 1499 */     if (isOutputShutdown())
/* 1500 */       throw new SocketException("Socket output is already shutdown");
/* 1501 */     getImpl().shutdownOutput();
/* 1502 */     this.shutOut = true;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 1512 */       if (isConnected())
/* 1513 */         return "Socket[addr=" + getImpl().getInetAddress() + ",port=" + getImpl().getPort() + ",localport=" + getImpl().getLocalPort() + "]";
/*      */     }
/*      */     catch (SocketException localSocketException)
/*      */     {
/*      */     }
/* 1518 */     return "Socket[unconnected]";
/*      */   }
/*      */ 
/*      */   public boolean isConnected()
/*      */   {
/* 1534 */     return (this.connected) || (this.oldImpl);
/*      */   }
/*      */ 
/*      */   public boolean isBound()
/*      */   {
/* 1551 */     return (this.bound) || (this.oldImpl);
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/* 1562 */     synchronized (this.closeLock) {
/* 1563 */       return this.closed;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isInputShutdown()
/*      */   {
/* 1575 */     return this.shutIn;
/*      */   }
/*      */ 
/*      */   public boolean isOutputShutdown()
/*      */   {
/* 1586 */     return this.shutOut;
/*      */   }
/*      */ 
/*      */   public static synchronized void setSocketImplFactory(SocketImplFactory paramSocketImplFactory)
/*      */     throws IOException
/*      */   {
/* 1621 */     if (factory != null) {
/* 1622 */       throw new SocketException("factory already defined");
/*      */     }
/* 1624 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1625 */     if (localSecurityManager != null) {
/* 1626 */       localSecurityManager.checkSetFactory();
/*      */     }
/* 1628 */     factory = paramSocketImplFactory;
/*      */   }
/*      */ 
/*      */   public void setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Socket
 * JD-Core Version:    0.6.2
 */