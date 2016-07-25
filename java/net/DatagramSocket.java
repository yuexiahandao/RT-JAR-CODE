/*      */ package java.net;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.nio.channels.DatagramChannel;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ 
/*      */ public class DatagramSocket
/*      */   implements Closeable
/*      */ {
/*   73 */   private boolean created = false;
/*   74 */   private boolean bound = false;
/*   75 */   private boolean closed = false;
/*   76 */   private Object closeLock = new Object();
/*      */   DatagramSocketImpl impl;
/*   86 */   boolean oldImpl = false;
/*      */ 
/*   97 */   private boolean explicitFilter = false;
/*      */   private int bytesLeftToFilter;
/*      */   static final int ST_NOT_CONNECTED = 0;
/*      */   static final int ST_CONNECTED = 1;
/*      */   static final int ST_CONNECTED_NO_IMPL = 2;
/*  109 */   int connectState = 0;
/*      */ 
/*  114 */   InetAddress connectedAddress = null;
/*  115 */   int connectedPort = -1;
/*      */ 
/*  327 */   static Class implClass = null;
/*      */   static DatagramSocketImplFactory factory;
/*      */ 
/*      */   private synchronized void connectInternal(InetAddress paramInetAddress, int paramInt)
/*      */     throws SocketException
/*      */   {
/*  126 */     if ((paramInt < 0) || (paramInt > 65535)) {
/*  127 */       throw new IllegalArgumentException("connect: " + paramInt);
/*      */     }
/*  129 */     if (paramInetAddress == null) {
/*  130 */       throw new IllegalArgumentException("connect: null address");
/*      */     }
/*  132 */     checkAddress(paramInetAddress, "connect");
/*  133 */     if (isClosed())
/*  134 */       return;
/*  135 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  136 */     if (localSecurityManager != null) {
/*  137 */       if (paramInetAddress.isMulticastAddress()) {
/*  138 */         localSecurityManager.checkMulticast(paramInetAddress);
/*      */       } else {
/*  140 */         localSecurityManager.checkConnect(paramInetAddress.getHostAddress(), paramInt);
/*  141 */         localSecurityManager.checkAccept(paramInetAddress.getHostAddress(), paramInt);
/*      */       }
/*      */     }
/*      */ 
/*  145 */     if (!isBound()) {
/*  146 */       bind(new InetSocketAddress(0));
/*      */     }
/*      */ 
/*  149 */     if ((this.oldImpl) || (((this.impl instanceof AbstractPlainDatagramSocketImpl)) && (((AbstractPlainDatagramSocketImpl)this.impl).nativeConnectDisabled())))
/*      */     {
/*  151 */       this.connectState = 2;
/*      */     }
/*      */     else try {
/*  154 */         getImpl().connect(paramInetAddress, paramInt);
/*      */ 
/*  157 */         this.connectState = 1;
/*      */ 
/*  159 */         int i = getImpl().dataAvailable();
/*  160 */         if (i == -1) {
/*  161 */           throw new SocketException();
/*      */         }
/*  163 */         this.explicitFilter = (i > 0);
/*  164 */         if (this.explicitFilter) {
/*  165 */           this.bytesLeftToFilter = getReceiveBufferSize();
/*      */         }
/*      */       }
/*      */       catch (SocketException localSocketException)
/*      */       {
/*  170 */         this.connectState = 2;
/*      */       }
/*      */ 
/*      */ 
/*  174 */     this.connectedAddress = paramInetAddress;
/*  175 */     this.connectedPort = paramInt;
/*      */   }
/*      */ 
/*      */   public DatagramSocket()
/*      */     throws SocketException
/*      */   {
/*  199 */     createImpl();
/*      */     try {
/*  201 */       bind(new InetSocketAddress(0));
/*      */     } catch (SocketException localSocketException) {
/*  203 */       throw localSocketException;
/*      */     } catch (IOException localIOException) {
/*  205 */       throw new SocketException(localIOException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DatagramSocket(DatagramSocketImpl paramDatagramSocketImpl)
/*      */   {
/*  218 */     if (paramDatagramSocketImpl == null)
/*  219 */       throw new NullPointerException();
/*  220 */     this.impl = paramDatagramSocketImpl;
/*  221 */     checkOldImpl();
/*      */   }
/*      */ 
/*      */   public DatagramSocket(SocketAddress paramSocketAddress)
/*      */     throws SocketException
/*      */   {
/*  249 */     createImpl();
/*  250 */     if (paramSocketAddress != null)
/*  251 */       bind(paramSocketAddress);
/*      */   }
/*      */ 
/*      */   public DatagramSocket(int paramInt)
/*      */     throws SocketException
/*      */   {
/*  276 */     this(paramInt, null);
/*      */   }
/*      */ 
/*      */   public DatagramSocket(int paramInt, InetAddress paramInetAddress)
/*      */     throws SocketException
/*      */   {
/*  304 */     this(new InetSocketAddress(paramInetAddress, paramInt));
/*      */   }
/*      */ 
/*      */   private void checkOldImpl() {
/*  308 */     if (this.impl == null) {
/*  309 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  313 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public Void run() throws NoSuchMethodException {
/*  316 */           Class[] arrayOfClass = new Class[1];
/*  317 */           arrayOfClass[0] = DatagramPacket.class;
/*  318 */           DatagramSocket.this.impl.getClass().getDeclaredMethod("peekData", arrayOfClass);
/*  319 */           return null;
/*      */         } } );
/*      */     }
/*      */     catch (PrivilegedActionException localPrivilegedActionException) {
/*  323 */       this.oldImpl = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   void createImpl()
/*      */     throws SocketException
/*      */   {
/*  330 */     if (this.impl == null) {
/*  331 */       if (factory != null) {
/*  332 */         this.impl = factory.createDatagramSocketImpl();
/*  333 */         checkOldImpl();
/*      */       } else {
/*  335 */         boolean bool = (this instanceof MulticastSocket);
/*  336 */         this.impl = DefaultDatagramSocketImplFactory.createDatagramSocketImpl(bool);
/*      */ 
/*  338 */         checkOldImpl();
/*      */       }
/*      */     }
/*      */ 
/*  342 */     this.impl.create();
/*  343 */     this.created = true;
/*      */   }
/*      */ 
/*      */   DatagramSocketImpl getImpl()
/*      */     throws SocketException
/*      */   {
/*  356 */     if (!this.created)
/*  357 */       createImpl();
/*  358 */     return this.impl;
/*      */   }
/*      */ 
/*      */   public synchronized void bind(SocketAddress paramSocketAddress)
/*      */     throws SocketException
/*      */   {
/*  377 */     if (isClosed())
/*  378 */       throw new SocketException("Socket is closed");
/*  379 */     if (isBound())
/*  380 */       throw new SocketException("already bound");
/*  381 */     if (paramSocketAddress == null)
/*  382 */       paramSocketAddress = new InetSocketAddress(0);
/*  383 */     if (!(paramSocketAddress instanceof InetSocketAddress))
/*  384 */       throw new IllegalArgumentException("Unsupported address type!");
/*  385 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/*  386 */     if (localInetSocketAddress.isUnresolved())
/*  387 */       throw new SocketException("Unresolved address");
/*  388 */     InetAddress localInetAddress = localInetSocketAddress.getAddress();
/*  389 */     int i = localInetSocketAddress.getPort();
/*  390 */     checkAddress(localInetAddress, "bind");
/*  391 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  392 */     if (localSecurityManager != null)
/*  393 */       localSecurityManager.checkListen(i);
/*      */     try
/*      */     {
/*  396 */       getImpl().bind(i, localInetAddress);
/*      */     } catch (SocketException localSocketException) {
/*  398 */       getImpl().close();
/*  399 */       throw localSocketException;
/*      */     }
/*  401 */     this.bound = true;
/*      */   }
/*      */ 
/*      */   void checkAddress(InetAddress paramInetAddress, String paramString) {
/*  405 */     if (paramInetAddress == null) {
/*  406 */       return;
/*      */     }
/*  408 */     if ((!(paramInetAddress instanceof Inet4Address)) && (!(paramInetAddress instanceof Inet6Address)))
/*  409 */       throw new IllegalArgumentException(paramString + ": invalid address type");
/*      */   }
/*      */ 
/*      */   public void connect(InetAddress paramInetAddress, int paramInt)
/*      */   {
/*      */     try
/*      */     {
/*  462 */       connectInternal(paramInetAddress, paramInt);
/*      */     } catch (SocketException localSocketException) {
/*  464 */       throw new Error("connect failed", localSocketException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void connect(SocketAddress paramSocketAddress)
/*      */     throws SocketException
/*      */   {
/*  491 */     if (paramSocketAddress == null)
/*  492 */       throw new IllegalArgumentException("Address can't be null");
/*  493 */     if (!(paramSocketAddress instanceof InetSocketAddress))
/*  494 */       throw new IllegalArgumentException("Unsupported address type");
/*  495 */     InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
/*  496 */     if (localInetSocketAddress.isUnresolved())
/*  497 */       throw new SocketException("Unresolved address");
/*  498 */     connectInternal(localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*      */   }
/*      */ 
/*      */   public void disconnect()
/*      */   {
/*  508 */     synchronized (this) {
/*  509 */       if (isClosed())
/*  510 */         return;
/*  511 */       if (this.connectState == 1) {
/*  512 */         this.impl.disconnect();
/*      */       }
/*  514 */       this.connectedAddress = null;
/*  515 */       this.connectedPort = -1;
/*  516 */       this.connectState = 0;
/*  517 */       this.explicitFilter = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isBound()
/*      */   {
/*  532 */     return this.bound;
/*      */   }
/*      */ 
/*      */   public boolean isConnected()
/*      */   {
/*  546 */     return this.connectState != 0;
/*      */   }
/*      */ 
/*      */   public InetAddress getInetAddress()
/*      */   {
/*  560 */     return this.connectedAddress;
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/*  574 */     return this.connectedPort;
/*      */   }
/*      */ 
/*      */   public SocketAddress getRemoteSocketAddress()
/*      */   {
/*  594 */     if (!isConnected())
/*  595 */       return null;
/*  596 */     return new InetSocketAddress(getInetAddress(), getPort());
/*      */   }
/*      */ 
/*      */   public SocketAddress getLocalSocketAddress()
/*      */   {
/*  611 */     if (isClosed())
/*  612 */       return null;
/*  613 */     if (!isBound())
/*  614 */       return null;
/*  615 */     return new InetSocketAddress(getLocalAddress(), getLocalPort());
/*      */   }
/*      */ 
/*      */   public void send(DatagramPacket paramDatagramPacket)
/*      */     throws IOException
/*      */   {
/*  659 */     InetAddress localInetAddress = null;
/*  660 */     synchronized (paramDatagramPacket) {
/*  661 */       if (isClosed())
/*  662 */         throw new SocketException("Socket is closed");
/*  663 */       checkAddress(paramDatagramPacket.getAddress(), "send");
/*  664 */       if (this.connectState == 0)
/*      */       {
/*  666 */         SecurityManager localSecurityManager = System.getSecurityManager();
/*      */ 
/*  672 */         if (localSecurityManager != null) {
/*  673 */           if (paramDatagramPacket.getAddress().isMulticastAddress())
/*  674 */             localSecurityManager.checkMulticast(paramDatagramPacket.getAddress());
/*      */           else {
/*  676 */             localSecurityManager.checkConnect(paramDatagramPacket.getAddress().getHostAddress(), paramDatagramPacket.getPort());
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  682 */         localInetAddress = paramDatagramPacket.getAddress();
/*  683 */         if (localInetAddress == null) {
/*  684 */           paramDatagramPacket.setAddress(this.connectedAddress);
/*  685 */           paramDatagramPacket.setPort(this.connectedPort);
/*  686 */         } else if ((!localInetAddress.equals(this.connectedAddress)) || (paramDatagramPacket.getPort() != this.connectedPort))
/*      */         {
/*  688 */           throw new IllegalArgumentException("connected address and packet address differ");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  694 */       if (!isBound()) {
/*  695 */         bind(new InetSocketAddress(0));
/*      */       }
/*  697 */       getImpl().send(paramDatagramPacket);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void receive(DatagramPacket paramDatagramPacket)
/*      */     throws IOException
/*      */   {
/*  733 */     synchronized (paramDatagramPacket) {
/*  734 */       if (!isBound())
/*  735 */         bind(new InetSocketAddress(0));
/*      */       DatagramPacket localDatagramPacket;
/*  736 */       if (this.connectState == 0)
/*      */       {
/*  738 */         localObject1 = System.getSecurityManager();
/*  739 */         if (localObject1 != null) {
/*      */           while (true) {
/*  741 */             String str = null;
/*  742 */             int j = 0;
/*      */             Object localObject2;
/*  744 */             if (!this.oldImpl)
/*      */             {
/*  746 */               localObject2 = new DatagramPacket(new byte[1], 1);
/*  747 */               j = getImpl().peekData((DatagramPacket)localObject2);
/*  748 */               str = ((DatagramPacket)localObject2).getAddress().getHostAddress();
/*      */             } else {
/*  750 */               localObject2 = new InetAddress();
/*  751 */               j = getImpl().peek((InetAddress)localObject2);
/*  752 */               str = ((InetAddress)localObject2).getHostAddress();
/*      */             }
/*      */             try {
/*  755 */               ((SecurityManager)localObject1).checkAccept(str, j);
/*      */             }
/*      */             catch (SecurityException localSecurityException)
/*      */             {
/*  762 */               localDatagramPacket = new DatagramPacket(new byte[1], 1);
/*  763 */               getImpl().receive(localDatagramPacket);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  776 */       Object localObject1 = null;
/*  777 */       if ((this.connectState == 2) || (this.explicitFilter))
/*      */       {
/*  783 */         int i = 0;
/*  784 */         while (i == 0) {
/*  785 */           InetAddress localInetAddress = null;
/*  786 */           int k = -1;
/*      */ 
/*  788 */           if (!this.oldImpl)
/*      */           {
/*  790 */             localDatagramPacket = new DatagramPacket(new byte[1], 1);
/*  791 */             k = getImpl().peekData(localDatagramPacket);
/*  792 */             localInetAddress = localDatagramPacket.getAddress();
/*      */           }
/*      */           else {
/*  795 */             localInetAddress = new InetAddress();
/*  796 */             k = getImpl().peek(localInetAddress);
/*      */           }
/*  798 */           if ((!this.connectedAddress.equals(localInetAddress)) || (this.connectedPort != k))
/*      */           {
/*  801 */             localObject1 = new DatagramPacket(new byte[1024], 1024);
/*      */ 
/*  803 */             getImpl().receive((DatagramPacket)localObject1);
/*  804 */             if ((this.explicitFilter) && 
/*  805 */               (checkFiltering((DatagramPacket)localObject1)))
/*  806 */               i = 1;
/*      */           }
/*      */           else
/*      */           {
/*  810 */             i = 1;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  816 */       getImpl().receive(paramDatagramPacket);
/*  817 */       if ((this.explicitFilter) && (localObject1 == null))
/*      */       {
/*  819 */         checkFiltering(paramDatagramPacket);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkFiltering(DatagramPacket paramDatagramPacket) throws SocketException
/*      */   {
/*  826 */     this.bytesLeftToFilter -= paramDatagramPacket.getLength();
/*  827 */     if ((this.bytesLeftToFilter <= 0) || (getImpl().dataAvailable() <= 0)) {
/*  828 */       this.explicitFilter = false;
/*  829 */       return true;
/*      */     }
/*  831 */     return false;
/*      */   }
/*      */ 
/*      */   public InetAddress getLocalAddress()
/*      */   {
/*  853 */     if (isClosed())
/*  854 */       return null;
/*  855 */     InetAddress localInetAddress = null;
/*      */     try {
/*  857 */       localInetAddress = (InetAddress)getImpl().getOption(15);
/*  858 */       if (localInetAddress.isAnyLocalAddress()) {
/*  859 */         localInetAddress = InetAddress.anyLocalAddress();
/*      */       }
/*  861 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  862 */       if (localSecurityManager != null)
/*  863 */         localSecurityManager.checkConnect(localInetAddress.getHostAddress(), -1);
/*      */     }
/*      */     catch (Exception localException) {
/*  866 */       localInetAddress = InetAddress.anyLocalAddress();
/*      */     }
/*  868 */     return localInetAddress;
/*      */   }
/*      */ 
/*      */   public int getLocalPort()
/*      */   {
/*  880 */     if (isClosed())
/*  881 */       return -1;
/*      */     try {
/*  883 */       return getImpl().getLocalPort(); } catch (Exception localException) {
/*      */     }
/*  885 */     return 0;
/*      */   }
/*      */ 
/*      */   public synchronized void setSoTimeout(int paramInt)
/*      */     throws SocketException
/*      */   {
/*  905 */     if (isClosed())
/*  906 */       throw new SocketException("Socket is closed");
/*  907 */     getImpl().setOption(4102, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getSoTimeout()
/*      */     throws SocketException
/*      */   {
/*  920 */     if (isClosed())
/*  921 */       throw new SocketException("Socket is closed");
/*  922 */     if (getImpl() == null)
/*  923 */       return 0;
/*  924 */     Object localObject = getImpl().getOption(4102);
/*      */ 
/*  926 */     if ((localObject instanceof Integer)) {
/*  927 */       return ((Integer)localObject).intValue();
/*      */     }
/*  929 */     return 0;
/*      */   }
/*      */ 
/*      */   public synchronized void setSendBufferSize(int paramInt)
/*      */     throws SocketException
/*      */   {
/*  964 */     if (paramInt <= 0) {
/*  965 */       throw new IllegalArgumentException("negative send size");
/*      */     }
/*  967 */     if (isClosed())
/*  968 */       throw new SocketException("Socket is closed");
/*  969 */     getImpl().setOption(4097, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getSendBufferSize()
/*      */     throws SocketException
/*      */   {
/*  982 */     if (isClosed())
/*  983 */       throw new SocketException("Socket is closed");
/*  984 */     int i = 0;
/*  985 */     Object localObject = getImpl().getOption(4097);
/*  986 */     if ((localObject instanceof Integer)) {
/*  987 */       i = ((Integer)localObject).intValue();
/*      */     }
/*  989 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void setReceiveBufferSize(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1022 */     if (paramInt <= 0) {
/* 1023 */       throw new IllegalArgumentException("invalid receive size");
/*      */     }
/* 1025 */     if (isClosed())
/* 1026 */       throw new SocketException("Socket is closed");
/* 1027 */     getImpl().setOption(4098, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getReceiveBufferSize()
/*      */     throws SocketException
/*      */   {
/* 1040 */     if (isClosed())
/* 1041 */       throw new SocketException("Socket is closed");
/* 1042 */     int i = 0;
/* 1043 */     Object localObject = getImpl().getOption(4098);
/* 1044 */     if ((localObject instanceof Integer)) {
/* 1045 */       i = ((Integer)localObject).intValue();
/*      */     }
/* 1047 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void setReuseAddress(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/* 1085 */     if (isClosed()) {
/* 1086 */       throw new SocketException("Socket is closed");
/*      */     }
/* 1088 */     if (this.oldImpl)
/* 1089 */       getImpl().setOption(4, new Integer(paramBoolean ? -1 : 0));
/*      */     else
/* 1091 */       getImpl().setOption(4, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public synchronized boolean getReuseAddress()
/*      */     throws SocketException
/*      */   {
/* 1104 */     if (isClosed())
/* 1105 */       throw new SocketException("Socket is closed");
/* 1106 */     Object localObject = getImpl().getOption(4);
/* 1107 */     return ((Boolean)localObject).booleanValue();
/*      */   }
/*      */ 
/*      */   public synchronized void setBroadcast(boolean paramBoolean)
/*      */     throws SocketException
/*      */   {
/* 1128 */     if (isClosed())
/* 1129 */       throw new SocketException("Socket is closed");
/* 1130 */     getImpl().setOption(32, Boolean.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public synchronized boolean getBroadcast()
/*      */     throws SocketException
/*      */   {
/* 1142 */     if (isClosed())
/* 1143 */       throw new SocketException("Socket is closed");
/* 1144 */     return ((Boolean)getImpl().getOption(32)).booleanValue();
/*      */   }
/*      */ 
/*      */   public synchronized void setTrafficClass(int paramInt)
/*      */     throws SocketException
/*      */   {
/* 1185 */     if ((paramInt < 0) || (paramInt > 255)) {
/* 1186 */       throw new IllegalArgumentException("tc is not in range 0 -- 255");
/*      */     }
/* 1188 */     if (isClosed())
/* 1189 */       throw new SocketException("Socket is closed");
/* 1190 */     getImpl().setOption(3, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   public synchronized int getTrafficClass()
/*      */     throws SocketException
/*      */   {
/* 1210 */     if (isClosed())
/* 1211 */       throw new SocketException("Socket is closed");
/* 1212 */     return ((Integer)getImpl().getOption(3)).intValue();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/* 1228 */     synchronized (this.closeLock) {
/* 1229 */       if (isClosed())
/* 1230 */         return;
/* 1231 */       this.impl.close();
/* 1232 */       this.closed = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/* 1243 */     synchronized (this.closeLock) {
/* 1244 */       return this.closed;
/*      */     }
/*      */   }
/*      */ 
/*      */   public DatagramChannel getChannel()
/*      */   {
/* 1263 */     return null;
/*      */   }
/*      */ 
/*      */   public static synchronized void setDatagramSocketImplFactory(DatagramSocketImplFactory paramDatagramSocketImplFactory)
/*      */     throws IOException
/*      */   {
/* 1303 */     if (factory != null) {
/* 1304 */       throw new SocketException("factory already defined");
/*      */     }
/* 1306 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1307 */     if (localSecurityManager != null) {
/* 1308 */       localSecurityManager.checkSetFactory();
/*      */     }
/* 1310 */     factory = paramDatagramSocketImplFactory;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.DatagramSocket
 * JD-Core Version:    0.6.2
 */