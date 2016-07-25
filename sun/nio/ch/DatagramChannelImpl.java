/*      */ package sun.nio.ch;
/*      */ 
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.IOException;
/*      */ import java.net.DatagramSocket;
/*      */ import java.net.Inet4Address;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.NetworkInterface;
/*      */ import java.net.PortUnreachableException;
/*      */ import java.net.ProtocolFamily;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketOption;
/*      */ import java.net.StandardProtocolFamily;
/*      */ import java.net.StandardSocketOptions;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AlreadyBoundException;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.DatagramChannel;
/*      */ import java.nio.channels.MembershipKey;
/*      */ import java.nio.channels.NotYetConnectedException;
/*      */ import java.nio.channels.UnsupportedAddressTypeException;
/*      */ import java.nio.channels.spi.SelectorProvider;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import sun.net.ResourceManager;
/*      */ 
/*      */ class DatagramChannelImpl extends DatagramChannel
/*      */   implements SelChImpl
/*      */ {
/*      */   private static NativeDispatcher nd;
/*      */   private final FileDescriptor fd;
/*      */   private final int fdVal;
/*      */   private final ProtocolFamily family;
/*   61 */   private volatile long readerThread = 0L;
/*   62 */   private volatile long writerThread = 0L;
/*      */   private InetAddress cachedSenderInetAddress;
/*      */   private int cachedSenderPort;
/*   70 */   private final Object readLock = new Object();
/*      */ 
/*   73 */   private final Object writeLock = new Object();
/*      */ 
/*   77 */   private final Object stateLock = new Object();
/*      */   private static final int ST_UNINITIALIZED = -1;
/*      */   private static final int ST_UNCONNECTED = 0;
/*      */   private static final int ST_CONNECTED = 1;
/*      */   private static final int ST_KILLED = 2;
/*   86 */   private int state = -1;
/*      */   private InetSocketAddress localAddress;
/*      */   private InetSocketAddress remoteAddress;
/*      */   private DatagramSocket socket;
/*      */   private MembershipRegistry registry;
/*      */   private boolean reuseAddressEmulated;
/*      */   private boolean isReuseAddress;
/*      */   private SocketAddress sender;
/*      */ 
/*      */   public DatagramChannelImpl(SelectorProvider paramSelectorProvider)
/*      */     throws IOException
/*      */   {
/*  110 */     super(paramSelectorProvider);
/*  111 */     ResourceManager.beforeUdpCreate();
/*      */     try {
/*  113 */       this.family = (Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET);
/*      */ 
/*  115 */       this.fd = Net.socket(this.family, false);
/*  116 */       this.fdVal = IOUtil.fdVal(this.fd);
/*  117 */       this.state = 0;
/*      */     } catch (IOException localIOException) {
/*  119 */       ResourceManager.afterUdpClose();
/*  120 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public DatagramChannelImpl(SelectorProvider paramSelectorProvider, ProtocolFamily paramProtocolFamily)
/*      */     throws IOException
/*      */   {
/*  127 */     super(paramSelectorProvider);
/*  128 */     if ((paramProtocolFamily != StandardProtocolFamily.INET) && (paramProtocolFamily != StandardProtocolFamily.INET6))
/*      */     {
/*  131 */       if (paramProtocolFamily == null) {
/*  132 */         throw new NullPointerException("'family' is null");
/*      */       }
/*  134 */       throw new UnsupportedOperationException("Protocol family not supported");
/*      */     }
/*  136 */     if ((paramProtocolFamily == StandardProtocolFamily.INET6) && 
/*  137 */       (!Net.isIPv6Available())) {
/*  138 */       throw new UnsupportedOperationException("IPv6 not available");
/*      */     }
/*      */ 
/*  141 */     this.family = paramProtocolFamily;
/*  142 */     this.fd = Net.socket(paramProtocolFamily, false);
/*  143 */     this.fdVal = IOUtil.fdVal(this.fd);
/*  144 */     this.state = 0;
/*      */   }
/*      */ 
/*      */   public DatagramChannelImpl(SelectorProvider paramSelectorProvider, FileDescriptor paramFileDescriptor)
/*      */     throws IOException
/*      */   {
/*  150 */     super(paramSelectorProvider);
/*  151 */     this.family = (Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET);
/*      */ 
/*  153 */     this.fd = paramFileDescriptor;
/*  154 */     this.fdVal = IOUtil.fdVal(paramFileDescriptor);
/*  155 */     this.state = 0;
/*  156 */     this.localAddress = Net.localAddress(paramFileDescriptor);
/*      */   }
/*      */ 
/*      */   public DatagramSocket socket() {
/*  160 */     synchronized (this.stateLock) {
/*  161 */       if (this.socket == null)
/*  162 */         this.socket = DatagramSocketAdaptor.create(this);
/*  163 */       return this.socket;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress getLocalAddress() throws IOException
/*      */   {
/*  169 */     synchronized (this.stateLock) {
/*  170 */       if (!isOpen())
/*  171 */         throw new ClosedChannelException();
/*  172 */       return Net.getRevealedLocalAddress(this.localAddress);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress getRemoteAddress() throws IOException
/*      */   {
/*  178 */     synchronized (this.stateLock) {
/*  179 */       if (!isOpen())
/*  180 */         throw new ClosedChannelException();
/*  181 */       return this.remoteAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> DatagramChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*      */     throws IOException
/*      */   {
/*  189 */     if (paramSocketOption == null)
/*  190 */       throw new NullPointerException();
/*  191 */     if (!supportedOptions().contains(paramSocketOption)) {
/*  192 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*      */     }
/*  194 */     synchronized (this.stateLock) {
/*  195 */       ensureOpen();
/*      */ 
/*  197 */       if (paramSocketOption == StandardSocketOptions.IP_TOS)
/*      */       {
/*  199 */         if (this.family == StandardProtocolFamily.INET) {
/*  200 */           Net.setSocketOption(this.fd, this.family, paramSocketOption, paramT);
/*      */         }
/*  202 */         return this;
/*      */       }
/*      */ 
/*  205 */       if ((paramSocketOption == StandardSocketOptions.IP_MULTICAST_TTL) || (paramSocketOption == StandardSocketOptions.IP_MULTICAST_LOOP))
/*      */       {
/*  209 */         Net.setSocketOption(this.fd, this.family, paramSocketOption, paramT);
/*  210 */         return this;
/*      */       }
/*      */ 
/*  213 */       if (paramSocketOption == StandardSocketOptions.IP_MULTICAST_IF) {
/*  214 */         if (paramT == null)
/*  215 */           throw new IllegalArgumentException("Cannot set IP_MULTICAST_IF to 'null'");
/*  216 */         NetworkInterface localNetworkInterface = (NetworkInterface)paramT;
/*  217 */         if (this.family == StandardProtocolFamily.INET6) {
/*  218 */           int i = localNetworkInterface.getIndex();
/*  219 */           if (i == -1)
/*  220 */             throw new IOException("Network interface cannot be identified");
/*  221 */           Net.setInterface6(this.fd, i);
/*      */         }
/*      */         else {
/*  224 */           Inet4Address localInet4Address = Net.anyInet4Address(localNetworkInterface);
/*  225 */           if (localInet4Address == null)
/*  226 */             throw new IOException("Network interface not configured for IPv4");
/*  227 */           int j = Net.inet4AsInt(localInet4Address);
/*  228 */           Net.setInterface4(this.fd, j);
/*      */         }
/*  230 */         return this;
/*      */       }
/*  232 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()) && (this.localAddress != null))
/*      */       {
/*  235 */         this.reuseAddressEmulated = true;
/*  236 */         this.isReuseAddress = ((Boolean)paramT).booleanValue();
/*      */       }
/*      */ 
/*  240 */       Net.setSocketOption(this.fd, Net.UNSPEC, paramSocketOption, paramT);
/*  241 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T getOption(SocketOption<T> paramSocketOption)
/*      */     throws IOException
/*      */   {
/*  250 */     if (paramSocketOption == null)
/*  251 */       throw new NullPointerException();
/*  252 */     if (!supportedOptions().contains(paramSocketOption)) {
/*  253 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*      */     }
/*  255 */     synchronized (this.stateLock) {
/*  256 */       ensureOpen();
/*      */ 
/*  258 */       if (paramSocketOption == StandardSocketOptions.IP_TOS)
/*      */       {
/*  260 */         if (this.family == StandardProtocolFamily.INET) {
/*  261 */           return Net.getSocketOption(this.fd, this.family, paramSocketOption);
/*      */         }
/*  263 */         return Integer.valueOf(0);
/*      */       }
/*      */ 
/*  267 */       if ((paramSocketOption == StandardSocketOptions.IP_MULTICAST_TTL) || (paramSocketOption == StandardSocketOptions.IP_MULTICAST_LOOP))
/*      */       {
/*  270 */         return Net.getSocketOption(this.fd, this.family, paramSocketOption);
/*      */       }
/*      */ 
/*  273 */       if (paramSocketOption == StandardSocketOptions.IP_MULTICAST_IF) {
/*  274 */         if (this.family == StandardProtocolFamily.INET) {
/*  275 */           i = Net.getInterface4(this.fd);
/*  276 */           if (i == 0) {
/*  277 */             return null;
/*      */           }
/*  279 */           localObject1 = Net.inet4FromInt(i);
/*  280 */           NetworkInterface localNetworkInterface = NetworkInterface.getByInetAddress((InetAddress)localObject1);
/*  281 */           if (localNetworkInterface == null)
/*  282 */             throw new IOException("Unable to map address to interface");
/*  283 */           return localNetworkInterface;
/*      */         }
/*  285 */         int i = Net.getInterface6(this.fd);
/*  286 */         if (i == 0) {
/*  287 */           return null;
/*      */         }
/*  289 */         Object localObject1 = NetworkInterface.getByIndex(i);
/*  290 */         if (localObject1 == null)
/*  291 */           throw new IOException("Unable to map index to interface");
/*  292 */         return localObject1;
/*      */       }
/*      */ 
/*  296 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (this.reuseAddressEmulated))
/*      */       {
/*  299 */         return Boolean.valueOf(this.isReuseAddress);
/*      */       }
/*      */ 
/*  303 */       return Net.getSocketOption(this.fd, Net.UNSPEC, paramSocketOption);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Set<SocketOption<?>> supportedOptions()
/*      */   {
/*  326 */     return DefaultOptionsHolder.defaultOptions;
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws ClosedChannelException {
/*  330 */     if (!isOpen())
/*  331 */       throw new ClosedChannelException();
/*      */   }
/*      */ 
/*      */   public SocketAddress receive(ByteBuffer paramByteBuffer)
/*      */     throws IOException
/*      */   {
/*  337 */     if (paramByteBuffer.isReadOnly())
/*  338 */       throw new IllegalArgumentException("Read-only buffer");
/*  339 */     if (paramByteBuffer == null)
/*  340 */       throw new NullPointerException();
/*  341 */     synchronized (this.readLock) {
/*  342 */       ensureOpen();
/*      */ 
/*  344 */       if (localAddress() == null)
/*  345 */         bind(null);
/*  346 */       int i = 0;
/*  347 */       ByteBuffer localByteBuffer = null;
/*      */       try {
/*  349 */         begin();
/*  350 */         if (!isOpen()) {
/*  351 */           localObject1 = null;
/*      */ 
/*  386 */           if (localByteBuffer != null)
/*  387 */             Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*  388 */           this.readerThread = 0L;
/*  389 */           end((i > 0) || (i == -2));
/*  390 */           assert (IOStatus.check(i)); return localObject1;
/*      */         }
/*  352 */         Object localObject1 = System.getSecurityManager();
/*  353 */         this.readerThread = NativeThread.current();
/*  354 */         if ((isConnected()) || (localObject1 == null)) {
/*      */           do
/*  356 */             i = receive(this.fd, paramByteBuffer);
/*  357 */           while ((i == -3) && (isOpen()));
/*  358 */           if (i == -2) {
/*  359 */             localObject2 = null;
/*      */ 
/*  386 */             if (localByteBuffer != null)
/*  387 */               Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*  388 */             this.readerThread = 0L;
/*  389 */             end((i > 0) || (i == -2));
/*  390 */             assert (IOStatus.check(i)); return localObject2;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  361 */           localByteBuffer = Util.getTemporaryDirectBuffer(paramByteBuffer.remaining());
/*      */           while (true)
/*      */           {
/*  364 */             i = receive(this.fd, localByteBuffer);
/*  365 */             if ((i != -3) || (!isOpen())) {
/*  366 */               if (i == -2) {
/*  367 */                 localObject2 = null;
/*      */ 
/*  386 */                 if (localByteBuffer != null)
/*  387 */                   Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*  388 */                 this.readerThread = 0L;
/*  389 */                 end((i > 0) || (i == -2));
/*  390 */                 assert (IOStatus.check(i)); return localObject2;
/*      */               }
/*  368 */               localObject2 = (InetSocketAddress)this.sender;
/*      */               try {
/*  370 */                 ((SecurityManager)localObject1).checkAccept(((InetSocketAddress)localObject2).getAddress().getHostAddress(), ((InetSocketAddress)localObject2).getPort());
/*      */               }
/*      */               catch (SecurityException localSecurityException)
/*      */               {
/*  375 */                 localByteBuffer.clear();
/*  376 */                 i = 0;
/*      */               }
/*      */             }
/*      */           }
/*  379 */           localByteBuffer.flip();
/*  380 */           paramByteBuffer.put(localByteBuffer);
/*      */         }
/*      */ 
/*  384 */         Object localObject2 = this.sender;
/*      */ 
/*  386 */         if (localByteBuffer != null)
/*  387 */           Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*  388 */         this.readerThread = 0L;
/*  389 */         end((i > 0) || (i == -2));
/*  390 */         assert (IOStatus.check(i)); return localObject2;
/*      */       }
/*      */       finally
/*      */       {
/*  386 */         if (localByteBuffer != null)
/*  387 */           Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*  388 */         this.readerThread = 0L;
/*  389 */         end((i > 0) || (i == -2));
/*  390 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int receive(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer)
/*      */     throws IOException
/*      */   {
/*  398 */     int i = paramByteBuffer.position();
/*  399 */     int j = paramByteBuffer.limit();
/*  400 */     assert (i <= j);
/*  401 */     int k = i <= j ? j - i : 0;
/*  402 */     if (((paramByteBuffer instanceof DirectBuffer)) && (k > 0)) {
/*  403 */       return receiveIntoNativeBuffer(paramFileDescriptor, paramByteBuffer, k, i);
/*      */     }
/*      */ 
/*  408 */     int m = Math.max(k, 1);
/*  409 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(m);
/*      */     try {
/*  411 */       int n = receiveIntoNativeBuffer(paramFileDescriptor, localByteBuffer, m, 0);
/*  412 */       localByteBuffer.flip();
/*  413 */       if ((n > 0) && (k > 0))
/*  414 */         paramByteBuffer.put(localByteBuffer);
/*  415 */       return n;
/*      */     } finally {
/*  417 */       Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int receiveIntoNativeBuffer(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  425 */     int i = receive0(paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + paramInt2, paramInt1, isConnected());
/*      */ 
/*  427 */     if (i > 0)
/*  428 */       paramByteBuffer.position(paramInt2 + i);
/*  429 */     return i;
/*      */   }
/*      */ 
/*      */   public int send(ByteBuffer paramByteBuffer, SocketAddress paramSocketAddress)
/*      */     throws IOException
/*      */   {
/*  435 */     if (paramByteBuffer == null) {
/*  436 */       throw new NullPointerException();
/*      */     }
/*  438 */     synchronized (this.writeLock) {
/*  439 */       ensureOpen();
/*  440 */       InetSocketAddress localInetSocketAddress = Net.checkAddress(paramSocketAddress);
/*  441 */       InetAddress localInetAddress = localInetSocketAddress.getAddress();
/*  442 */       if (localInetAddress == null)
/*  443 */         throw new IOException("Target address not resolved");
/*  444 */       synchronized (this.stateLock) {
/*  445 */         if (!isConnected()) {
/*  446 */           if (paramSocketAddress == null)
/*  447 */             throw new NullPointerException();
/*  448 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  449 */           if (localSecurityManager != null) {
/*  450 */             if (localInetAddress.isMulticastAddress())
/*  451 */               localSecurityManager.checkMulticast(localInetAddress);
/*      */             else
/*  453 */               localSecurityManager.checkConnect(localInetAddress.getHostAddress(), localInetSocketAddress.getPort());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  458 */           if (!paramSocketAddress.equals(this.remoteAddress)) {
/*  459 */             throw new IllegalArgumentException("Connected address not equal to target address");
/*      */           }
/*      */ 
/*  462 */           return write(paramByteBuffer);
/*      */         }
/*      */       }
/*      */ 
/*  466 */       int i = 0;
/*      */       try {
/*  468 */         begin();
/*  469 */         if (!isOpen()) {
/*  470 */           int j = 0;
/*      */ 
/*  483 */           this.writerThread = 0L;
/*  484 */           end((i > 0) || (i == -2));
/*  485 */           assert (IOStatus.check(i)); return j;
/*      */         }
/*  471 */         this.writerThread = NativeThread.current();
/*      */         do
/*  473 */           i = send(this.fd, paramByteBuffer, localInetSocketAddress);
/*  474 */         while ((i == -3) && (isOpen()));
/*      */ 
/*  476 */         synchronized (this.stateLock) {
/*  477 */           if ((isOpen()) && (this.localAddress == null)) {
/*  478 */             this.localAddress = Net.localAddress(this.fd);
/*      */           }
/*      */         }
/*  481 */         int k = IOStatus.normalize(i);
/*      */ 
/*  483 */         this.writerThread = 0L;
/*  484 */         end((i > 0) || (i == -2));
/*  485 */         assert (IOStatus.check(i)); return k;
/*      */       }
/*      */       finally
/*      */       {
/*  483 */         this.writerThread = 0L;
/*  484 */         end((i > 0) || (i == -2));
/*  485 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int send(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, InetSocketAddress paramInetSocketAddress)
/*      */     throws IOException
/*      */   {
/*  493 */     if ((paramByteBuffer instanceof DirectBuffer)) {
/*  494 */       return sendFromNativeBuffer(paramFileDescriptor, paramByteBuffer, paramInetSocketAddress);
/*      */     }
/*      */ 
/*  497 */     int i = paramByteBuffer.position();
/*  498 */     int j = paramByteBuffer.limit();
/*  499 */     assert (i <= j);
/*  500 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  502 */     ByteBuffer localByteBuffer = Util.getTemporaryDirectBuffer(k);
/*      */     try {
/*  504 */       localByteBuffer.put(paramByteBuffer);
/*  505 */       localByteBuffer.flip();
/*      */ 
/*  507 */       paramByteBuffer.position(i);
/*      */ 
/*  509 */       int m = sendFromNativeBuffer(paramFileDescriptor, localByteBuffer, paramInetSocketAddress);
/*  510 */       if (m > 0)
/*      */       {
/*  512 */         paramByteBuffer.position(i + m);
/*      */       }
/*  514 */       return m;
/*      */     } finally {
/*  516 */       Util.releaseTemporaryDirectBuffer(localByteBuffer);
/*      */     }
/*      */   }
/*      */   private int sendFromNativeBuffer(FileDescriptor paramFileDescriptor, ByteBuffer paramByteBuffer, InetSocketAddress paramInetSocketAddress) throws IOException {
/*  524 */     int i = paramByteBuffer.position();
/*  525 */     int j = paramByteBuffer.limit();
/*  526 */     assert (i <= j);
/*  527 */     int k = i <= j ? j - i : 0;
/*      */ 
/*  529 */     boolean bool = this.family != StandardProtocolFamily.INET;
/*      */     int m;
/*      */     try { m = send0(bool, paramFileDescriptor, ((DirectBuffer)paramByteBuffer).address() + i, k, paramInetSocketAddress.getAddress(), paramInetSocketAddress.getPort());
/*      */     } catch (PortUnreachableException localPortUnreachableException)
/*      */     {
/*  535 */       if (isConnected())
/*  536 */         throw localPortUnreachableException;
/*  537 */       m = k;
/*      */     }
/*  539 */     if (m > 0)
/*  540 */       paramByteBuffer.position(i + m);
/*  541 */     return m;
/*      */   }
/*      */ 
/*      */   public int read(ByteBuffer paramByteBuffer) throws IOException {
/*  545 */     if (paramByteBuffer == null)
/*  546 */       throw new NullPointerException();
/*  547 */     synchronized (this.readLock) {
/*  548 */       synchronized (this.stateLock) {
/*  549 */         ensureOpen();
/*  550 */         if (!isConnected())
/*  551 */           throw new NotYetConnectedException();
/*      */       }
/*  553 */       int i = 0;
/*      */       try {
/*  555 */         begin();
/*  556 */         if (!isOpen()) {
/*  557 */           j = 0;
/*      */ 
/*  564 */           this.readerThread = 0L;
/*  565 */           end((i > 0) || (i == -2));
/*  566 */           assert (IOStatus.check(i)); return j;
/*      */         }
/*  558 */         this.readerThread = NativeThread.current();
/*      */         do
/*  560 */           i = IOUtil.read(this.fd, paramByteBuffer, -1L, nd);
/*  561 */         while ((i == -3) && (isOpen()));
/*  562 */         int j = IOStatus.normalize(i);
/*      */ 
/*  564 */         this.readerThread = 0L;
/*  565 */         end((i > 0) || (i == -2));
/*  566 */         assert (IOStatus.check(i)); return j;
/*      */       }
/*      */       finally
/*      */       {
/*  564 */         this.readerThread = 0L;
/*  565 */         end((i > 0) || (i == -2));
/*  566 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  574 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  575 */       throw new IndexOutOfBoundsException();
/*  576 */     synchronized (this.readLock) {
/*  577 */       synchronized (this.stateLock) {
/*  578 */         ensureOpen();
/*  579 */         if (!isConnected())
/*  580 */           throw new NotYetConnectedException();
/*      */       }
/*  582 */       long l1 = 0L;
/*      */       try {
/*  584 */         begin();
/*  585 */         if (!isOpen()) {
/*  586 */           l2 = 0L;
/*      */ 
/*  593 */           this.readerThread = 0L;
/*  594 */           end((l1 > 0L) || (l1 == -2L));
/*  595 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*  587 */         this.readerThread = NativeThread.current();
/*      */         do
/*  589 */           l1 = IOUtil.read(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, nd);
/*  590 */         while ((l1 == -3L) && (isOpen()));
/*  591 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  593 */         this.readerThread = 0L;
/*  594 */         end((l1 > 0L) || (l1 == -2L));
/*  595 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  593 */         this.readerThread = 0L;
/*  594 */         end((l1 > 0L) || (l1 == -2L));
/*  595 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int write(ByteBuffer paramByteBuffer) throws IOException {
/*  601 */     if (paramByteBuffer == null)
/*  602 */       throw new NullPointerException();
/*  603 */     synchronized (this.writeLock) {
/*  604 */       synchronized (this.stateLock) {
/*  605 */         ensureOpen();
/*  606 */         if (!isConnected())
/*  607 */           throw new NotYetConnectedException();
/*      */       }
/*  609 */       int i = 0;
/*      */       try {
/*  611 */         begin();
/*  612 */         if (!isOpen()) {
/*  613 */           j = 0;
/*      */ 
/*  620 */           this.writerThread = 0L;
/*  621 */           end((i > 0) || (i == -2));
/*  622 */           assert (IOStatus.check(i)); return j;
/*      */         }
/*  614 */         this.writerThread = NativeThread.current();
/*      */         do
/*  616 */           i = IOUtil.write(this.fd, paramByteBuffer, -1L, nd);
/*  617 */         while ((i == -3) && (isOpen()));
/*  618 */         int j = IOStatus.normalize(i);
/*      */ 
/*  620 */         this.writerThread = 0L;
/*  621 */         end((i > 0) || (i == -2));
/*  622 */         assert (IOStatus.check(i)); return j;
/*      */       }
/*      */       finally
/*      */       {
/*  620 */         this.writerThread = 0L;
/*  621 */         end((i > 0) || (i == -2));
/*  622 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  630 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  631 */       throw new IndexOutOfBoundsException();
/*  632 */     synchronized (this.writeLock) {
/*  633 */       synchronized (this.stateLock) {
/*  634 */         ensureOpen();
/*  635 */         if (!isConnected())
/*  636 */           throw new NotYetConnectedException();
/*      */       }
/*  638 */       long l1 = 0L;
/*      */       try {
/*  640 */         begin();
/*  641 */         if (!isOpen()) {
/*  642 */           l2 = 0L;
/*      */ 
/*  649 */           this.writerThread = 0L;
/*  650 */           end((l1 > 0L) || (l1 == -2L));
/*  651 */           assert (IOStatus.check(l1)); return l2;
/*      */         }
/*  643 */         this.writerThread = NativeThread.current();
/*      */         do
/*  645 */           l1 = IOUtil.write(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, nd);
/*  646 */         while ((l1 == -3L) && (isOpen()));
/*  647 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  649 */         this.writerThread = 0L;
/*  650 */         end((l1 > 0L) || (l1 == -2L));
/*  651 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  649 */         this.writerThread = 0L;
/*  650 */         end((l1 > 0L) || (l1 == -2L));
/*  651 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException {
/*  657 */     IOUtil.configureBlocking(this.fd, paramBoolean);
/*      */   }
/*      */ 
/*      */   public SocketAddress localAddress() {
/*  661 */     synchronized (this.stateLock) {
/*  662 */       return this.localAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress remoteAddress() {
/*  667 */     synchronized (this.stateLock) {
/*  668 */       return this.remoteAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public DatagramChannel bind(SocketAddress paramSocketAddress) throws IOException
/*      */   {
/*  674 */     synchronized (this.readLock) {
/*  675 */       synchronized (this.writeLock) {
/*  676 */         synchronized (this.stateLock) {
/*  677 */           ensureOpen();
/*  678 */           if (this.localAddress != null)
/*  679 */             throw new AlreadyBoundException();
/*      */           InetSocketAddress localInetSocketAddress;
/*  681 */           if (paramSocketAddress == null)
/*      */           {
/*  683 */             if (this.family == StandardProtocolFamily.INET)
/*  684 */               localInetSocketAddress = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 0);
/*      */             else
/*  686 */               localInetSocketAddress = new InetSocketAddress(0);
/*      */           }
/*      */           else {
/*  689 */             localInetSocketAddress = Net.checkAddress(paramSocketAddress);
/*      */ 
/*  692 */             if (this.family == StandardProtocolFamily.INET) {
/*  693 */               localObject1 = localInetSocketAddress.getAddress();
/*  694 */               if (!(localObject1 instanceof Inet4Address))
/*  695 */                 throw new UnsupportedAddressTypeException();
/*      */             }
/*      */           }
/*  698 */           Object localObject1 = System.getSecurityManager();
/*  699 */           if (localObject1 != null) {
/*  700 */             ((SecurityManager)localObject1).checkListen(localInetSocketAddress.getPort());
/*      */           }
/*  702 */           Net.bind(this.family, this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*  703 */           this.localAddress = Net.localAddress(this.fd);
/*      */         }
/*      */       }
/*      */     }
/*  707 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isConnected() {
/*  711 */     synchronized (this.stateLock) {
/*  712 */       return this.state == 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void ensureOpenAndUnconnected() throws IOException {
/*  717 */     synchronized (this.stateLock) {
/*  718 */       if (!isOpen())
/*  719 */         throw new ClosedChannelException();
/*  720 */       if (this.state != 0)
/*  721 */         throw new IllegalStateException("Connect already invoked");
/*      */     }
/*      */   }
/*      */ 
/*      */   public DatagramChannel connect(SocketAddress paramSocketAddress) throws IOException
/*      */   {
/*  727 */     int i = 0;
/*      */ 
/*  729 */     synchronized (this.readLock) {
/*  730 */       synchronized (this.writeLock) {
/*  731 */         synchronized (this.stateLock) {
/*  732 */           ensureOpenAndUnconnected();
/*  733 */           InetSocketAddress localInetSocketAddress = Net.checkAddress(paramSocketAddress);
/*  734 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  735 */           if (localSecurityManager != null) {
/*  736 */             localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */           }
/*  738 */           int j = Net.connect(this.family, this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*      */ 
/*  742 */           if (j <= 0) {
/*  743 */             throw new Error();
/*      */           }
/*      */ 
/*  746 */           this.state = 1;
/*  747 */           this.remoteAddress = localInetSocketAddress;
/*  748 */           this.sender = localInetSocketAddress;
/*  749 */           this.cachedSenderInetAddress = localInetSocketAddress.getAddress();
/*  750 */           this.cachedSenderPort = localInetSocketAddress.getPort();
/*      */ 
/*  753 */           this.localAddress = Net.localAddress(this.fd);
/*      */ 
/*  756 */           boolean bool = false;
/*  757 */           synchronized (blockingLock()) {
/*      */             try {
/*  759 */               bool = isBlocking();
/*      */ 
/*  761 */               ByteBuffer localByteBuffer = ByteBuffer.allocate(1);
/*  762 */               if (bool) {
/*  763 */                 configureBlocking(false);
/*      */               }
/*      */               do
/*  766 */                 localByteBuffer.clear();
/*  767 */               while (receive(localByteBuffer) != null);
/*      */             } finally {
/*  769 */               if (bool) {
/*  770 */                 configureBlocking(true);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  777 */     return this;
/*      */   }
/*      */ 
/*      */   public DatagramChannel disconnect() throws IOException {
/*  781 */     synchronized (this.readLock) {
/*  782 */       synchronized (this.writeLock) {
/*  783 */         synchronized (this.stateLock) {
/*  784 */           if ((!isConnected()) || (!isOpen()))
/*  785 */             return this;
/*  786 */           InetSocketAddress localInetSocketAddress = this.remoteAddress;
/*  787 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  788 */           if (localSecurityManager != null) {
/*  789 */             localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */           }
/*  791 */           boolean bool = this.family == StandardProtocolFamily.INET6;
/*  792 */           disconnect0(this.fd, bool);
/*  793 */           this.remoteAddress = null;
/*  794 */           this.state = 0;
/*      */ 
/*  797 */           this.localAddress = Net.localAddress(this.fd);
/*      */         }
/*      */       }
/*      */     }
/*  801 */     return this;
/*      */   }
/*      */ 
/*      */   private MembershipKey innerJoin(InetAddress paramInetAddress1, NetworkInterface paramNetworkInterface, InetAddress paramInetAddress2)
/*      */     throws IOException
/*      */   {
/*  813 */     if (!paramInetAddress1.isMulticastAddress()) {
/*  814 */       throw new IllegalArgumentException("Group not a multicast address");
/*      */     }
/*      */ 
/*  817 */     if ((paramInetAddress1 instanceof Inet4Address)) {
/*  818 */       if ((this.family == StandardProtocolFamily.INET6) && (!Net.canIPv6SocketJoinIPv4Group()))
/*  819 */         throw new IllegalArgumentException("IPv6 socket cannot join IPv4 multicast group");
/*  820 */     } else if ((paramInetAddress1 instanceof Inet6Address)) {
/*  821 */       if (this.family != StandardProtocolFamily.INET6)
/*  822 */         throw new IllegalArgumentException("Only IPv6 sockets can join IPv6 multicast group");
/*      */     }
/*  824 */     else throw new IllegalArgumentException("Address type not supported");
/*      */ 
/*  828 */     if (paramInetAddress2 != null) {
/*  829 */       if (paramInetAddress2.isAnyLocalAddress())
/*  830 */         throw new IllegalArgumentException("Source address is a wildcard address");
/*  831 */       if (paramInetAddress2.isMulticastAddress())
/*  832 */         throw new IllegalArgumentException("Source address is multicast address");
/*  833 */       if (paramInetAddress2.getClass() != paramInetAddress1.getClass()) {
/*  834 */         throw new IllegalArgumentException("Source address is different type to group");
/*      */       }
/*      */     }
/*  837 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  838 */     if (localSecurityManager != null) {
/*  839 */       localSecurityManager.checkMulticast(paramInetAddress1);
/*      */     }
/*  841 */     synchronized (this.stateLock) {
/*  842 */       if (!isOpen())
/*  843 */         throw new ClosedChannelException();
/*      */       Object localObject1;
/*  846 */       if (this.registry == null) {
/*  847 */         this.registry = new MembershipRegistry();
/*      */       }
/*      */       else {
/*  850 */         localObject1 = this.registry.checkMembership(paramInetAddress1, paramNetworkInterface, paramInetAddress2);
/*  851 */         if (localObject1 != null)
/*  852 */           return localObject1;
/*      */       }
/*      */       int m;
/*  856 */       if ((this.family == StandardProtocolFamily.INET6) && (((paramInetAddress1 instanceof Inet6Address)) || (Net.canJoin6WithIPv4Group())))
/*      */       {
/*  859 */         int i = paramNetworkInterface.getIndex();
/*  860 */         if (i == -1) {
/*  861 */           throw new IOException("Network interface cannot be identified");
/*      */         }
/*      */ 
/*  864 */         byte[] arrayOfByte1 = Net.inet6AsByteArray(paramInetAddress1);
/*  865 */         byte[] arrayOfByte2 = paramInetAddress2 == null ? null : Net.inet6AsByteArray(paramInetAddress2);
/*      */ 
/*  869 */         m = Net.join6(this.fd, arrayOfByte1, i, arrayOfByte2);
/*  870 */         if (m == -2) {
/*  871 */           throw new UnsupportedOperationException();
/*      */         }
/*  873 */         localObject1 = new MembershipKeyImpl.Type6(this, paramInetAddress1, paramNetworkInterface, paramInetAddress2, arrayOfByte1, i, arrayOfByte2);
/*      */       }
/*      */       else
/*      */       {
/*  878 */         Inet4Address localInet4Address = Net.anyInet4Address(paramNetworkInterface);
/*  879 */         if (localInet4Address == null) {
/*  880 */           throw new IOException("Network interface not configured for IPv4");
/*      */         }
/*  882 */         int j = Net.inet4AsInt(paramInetAddress1);
/*  883 */         int k = Net.inet4AsInt(localInet4Address);
/*  884 */         m = paramInetAddress2 == null ? 0 : Net.inet4AsInt(paramInetAddress2);
/*      */ 
/*  887 */         int n = Net.join4(this.fd, j, k, m);
/*  888 */         if (n == -2) {
/*  889 */           throw new UnsupportedOperationException();
/*      */         }
/*  891 */         localObject1 = new MembershipKeyImpl.Type4(this, paramInetAddress1, paramNetworkInterface, paramInetAddress2, j, k, m);
/*      */       }
/*      */ 
/*  895 */       this.registry.add((MembershipKeyImpl)localObject1);
/*  896 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MembershipKey join(InetAddress paramInetAddress, NetworkInterface paramNetworkInterface)
/*      */     throws IOException
/*      */   {
/*  905 */     return innerJoin(paramInetAddress, paramNetworkInterface, null);
/*      */   }
/*      */ 
/*      */   public MembershipKey join(InetAddress paramInetAddress1, NetworkInterface paramNetworkInterface, InetAddress paramInetAddress2)
/*      */     throws IOException
/*      */   {
/*  914 */     if (paramInetAddress2 == null)
/*  915 */       throw new NullPointerException("source address is null");
/*  916 */     return innerJoin(paramInetAddress1, paramNetworkInterface, paramInetAddress2);
/*      */   }
/*      */ 
/*      */   void drop(MembershipKeyImpl paramMembershipKeyImpl)
/*      */   {
/*  921 */     assert (paramMembershipKeyImpl.channel() == this);
/*      */ 
/*  923 */     synchronized (this.stateLock) {
/*  924 */       if (!paramMembershipKeyImpl.isValid())
/*  925 */         return;
/*      */       try
/*      */       {
/*      */         Object localObject1;
/*  928 */         if ((paramMembershipKeyImpl instanceof MembershipKeyImpl.Type6)) {
/*  929 */           localObject1 = (MembershipKeyImpl.Type6)paramMembershipKeyImpl;
/*      */ 
/*  931 */           Net.drop6(this.fd, ((MembershipKeyImpl.Type6)localObject1).groupAddress(), ((MembershipKeyImpl.Type6)localObject1).index(), ((MembershipKeyImpl.Type6)localObject1).source());
/*      */         } else {
/*  933 */           localObject1 = (MembershipKeyImpl.Type4)paramMembershipKeyImpl;
/*  934 */           Net.drop4(this.fd, ((MembershipKeyImpl.Type4)localObject1).groupAddress(), ((MembershipKeyImpl.Type4)localObject1).interfaceAddress(), ((MembershipKeyImpl.Type4)localObject1).source());
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*  939 */         throw new AssertionError(localIOException);
/*      */       }
/*      */ 
/*  942 */       paramMembershipKeyImpl.invalidate();
/*  943 */       this.registry.remove(paramMembershipKeyImpl);
/*      */     }
/*      */   }
/*      */ 
/*      */   void block(MembershipKeyImpl paramMembershipKeyImpl, InetAddress paramInetAddress)
/*      */     throws IOException
/*      */   {
/*  954 */     assert (paramMembershipKeyImpl.channel() == this);
/*  955 */     assert (paramMembershipKeyImpl.sourceAddress() == null);
/*      */ 
/*  957 */     synchronized (this.stateLock) {
/*  958 */       if (!paramMembershipKeyImpl.isValid())
/*  959 */         throw new IllegalStateException("key is no longer valid");
/*  960 */       if (paramInetAddress.isAnyLocalAddress())
/*  961 */         throw new IllegalArgumentException("Source address is a wildcard address");
/*  962 */       if (paramInetAddress.isMulticastAddress())
/*  963 */         throw new IllegalArgumentException("Source address is multicast address");
/*  964 */       if (paramInetAddress.getClass() != paramMembershipKeyImpl.group().getClass())
/*  965 */         throw new IllegalArgumentException("Source address is different type to group");
/*      */       Object localObject1;
/*      */       int i;
/*  968 */       if ((paramMembershipKeyImpl instanceof MembershipKeyImpl.Type6)) {
/*  969 */         localObject1 = (MembershipKeyImpl.Type6)paramMembershipKeyImpl;
/*      */ 
/*  971 */         i = Net.block6(this.fd, ((MembershipKeyImpl.Type6)localObject1).groupAddress(), ((MembershipKeyImpl.Type6)localObject1).index(), Net.inet6AsByteArray(paramInetAddress));
/*      */       }
/*      */       else {
/*  974 */         localObject1 = (MembershipKeyImpl.Type4)paramMembershipKeyImpl;
/*      */ 
/*  976 */         i = Net.block4(this.fd, ((MembershipKeyImpl.Type4)localObject1).groupAddress(), ((MembershipKeyImpl.Type4)localObject1).interfaceAddress(), Net.inet4AsInt(paramInetAddress));
/*      */       }
/*      */ 
/*  979 */       if (i == -2)
/*      */       {
/*  981 */         throw new UnsupportedOperationException();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void unblock(MembershipKeyImpl paramMembershipKeyImpl, InetAddress paramInetAddress)
/*      */   {
/*  990 */     assert (paramMembershipKeyImpl.channel() == this);
/*  991 */     assert (paramMembershipKeyImpl.sourceAddress() == null);
/*      */ 
/*  993 */     synchronized (this.stateLock) {
/*  994 */       if (!paramMembershipKeyImpl.isValid())
/*  995 */         throw new IllegalStateException("key is no longer valid");
/*      */       try
/*      */       {
/*      */         Object localObject1;
/*  998 */         if ((paramMembershipKeyImpl instanceof MembershipKeyImpl.Type6)) {
/*  999 */           localObject1 = (MembershipKeyImpl.Type6)paramMembershipKeyImpl;
/*      */ 
/* 1001 */           Net.unblock6(this.fd, ((MembershipKeyImpl.Type6)localObject1).groupAddress(), ((MembershipKeyImpl.Type6)localObject1).index(), Net.inet6AsByteArray(paramInetAddress));
/*      */         }
/*      */         else {
/* 1004 */           localObject1 = (MembershipKeyImpl.Type4)paramMembershipKeyImpl;
/*      */ 
/* 1006 */           Net.unblock4(this.fd, ((MembershipKeyImpl.Type4)localObject1).groupAddress(), ((MembershipKeyImpl.Type4)localObject1).interfaceAddress(), Net.inet4AsInt(paramInetAddress));
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1011 */         throw new AssertionError(localIOException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void implCloseSelectableChannel() throws IOException {
/* 1017 */     synchronized (this.stateLock) {
/* 1018 */       if (this.state != 2)
/* 1019 */         nd.preClose(this.fd);
/* 1020 */       ResourceManager.afterUdpClose();
/*      */ 
/* 1023 */       if (this.registry != null)
/* 1024 */         this.registry.invalidateAll();
/*      */       long l;
/* 1027 */       if ((l = this.readerThread) != 0L)
/* 1028 */         NativeThread.signal(l);
/* 1029 */       if ((l = this.writerThread) != 0L)
/* 1030 */         NativeThread.signal(l);
/* 1031 */       if (!isRegistered())
/* 1032 */         kill();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void kill() throws IOException {
/* 1037 */     synchronized (this.stateLock) {
/* 1038 */       if (this.state == 2)
/* 1039 */         return;
/* 1040 */       if (this.state == -1) {
/* 1041 */         this.state = 2;
/* 1042 */         return;
/*      */       }
/* 1044 */       assert ((!isOpen()) && (!isRegistered()));
/* 1045 */       nd.close(this.fd);
/* 1046 */       this.state = 2;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void finalize() throws IOException
/*      */   {
/* 1052 */     if (this.fd != null)
/* 1053 */       close();
/*      */   }
/*      */ 
/*      */   public boolean translateReadyOps(int paramInt1, int paramInt2, SelectionKeyImpl paramSelectionKeyImpl)
/*      */   {
/* 1061 */     int i = paramSelectionKeyImpl.nioInterestOps();
/* 1062 */     int j = paramSelectionKeyImpl.nioReadyOps();
/* 1063 */     int k = paramInt2;
/*      */ 
/* 1065 */     if ((paramInt1 & 0x20) != 0)
/*      */     {
/* 1069 */       return false;
/*      */     }
/*      */ 
/* 1072 */     if ((paramInt1 & 0x18) != 0)
/*      */     {
/* 1074 */       k = i;
/* 1075 */       paramSelectionKeyImpl.nioReadyOps(k);
/* 1076 */       return (k & (j ^ 0xFFFFFFFF)) != 0;
/*      */     }
/*      */ 
/* 1079 */     if (((paramInt1 & 0x1) != 0) && ((i & 0x1) != 0))
/*      */     {
/* 1081 */       k |= 1;
/*      */     }
/* 1083 */     if (((paramInt1 & 0x4) != 0) && ((i & 0x4) != 0))
/*      */     {
/* 1085 */       k |= 4;
/*      */     }
/* 1087 */     paramSelectionKeyImpl.nioReadyOps(k);
/* 1088 */     return (k & (j ^ 0xFFFFFFFF)) != 0;
/*      */   }
/*      */ 
/*      */   public boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 1092 */     return translateReadyOps(paramInt, paramSelectionKeyImpl.nioReadyOps(), paramSelectionKeyImpl);
/*      */   }
/*      */ 
/*      */   public boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 1096 */     return translateReadyOps(paramInt, 0, paramSelectionKeyImpl);
/*      */   }
/*      */ 
/*      */   public void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl)
/*      */   {
/* 1103 */     int i = 0;
/*      */ 
/* 1105 */     if ((paramInt & 0x1) != 0)
/* 1106 */       i |= 1;
/* 1107 */     if ((paramInt & 0x4) != 0)
/* 1108 */       i |= 4;
/* 1109 */     if ((paramInt & 0x8) != 0)
/* 1110 */       i |= 1;
/* 1111 */     paramSelectionKeyImpl.selector.putEventOps(paramSelectionKeyImpl, i);
/*      */   }
/*      */ 
/*      */   public FileDescriptor getFD() {
/* 1115 */     return this.fd;
/*      */   }
/*      */ 
/*      */   public int getFDVal() {
/* 1119 */     return this.fdVal;
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   private static native void disconnect0(FileDescriptor paramFileDescriptor, boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   private native int receive0(FileDescriptor paramFileDescriptor, long paramLong, int paramInt, boolean paramBoolean)
/*      */     throws IOException;
/*      */ 
/*      */   private native int send0(boolean paramBoolean, FileDescriptor paramFileDescriptor, long paramLong, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*      */     throws IOException;
/*      */ 
/*      */   static
/*      */   {
/*   48 */     nd = new DatagramDispatcher();
/*      */ 
/* 1139 */     Util.load();
/* 1140 */     initIDs();
/*      */   }
/*      */ 
/*      */   private static class DefaultOptionsHolder
/*      */   {
/*  308 */     static final Set<SocketOption<?>> defaultOptions = defaultOptions();
/*      */ 
/*      */     private static Set<SocketOption<?>> defaultOptions() {
/*  311 */       HashSet localHashSet = new HashSet(8);
/*  312 */       localHashSet.add(StandardSocketOptions.SO_SNDBUF);
/*  313 */       localHashSet.add(StandardSocketOptions.SO_RCVBUF);
/*  314 */       localHashSet.add(StandardSocketOptions.SO_REUSEADDR);
/*  315 */       localHashSet.add(StandardSocketOptions.SO_BROADCAST);
/*  316 */       localHashSet.add(StandardSocketOptions.IP_TOS);
/*  317 */       localHashSet.add(StandardSocketOptions.IP_MULTICAST_IF);
/*  318 */       localHashSet.add(StandardSocketOptions.IP_MULTICAST_TTL);
/*  319 */       localHashSet.add(StandardSocketOptions.IP_MULTICAST_LOOP);
/*  320 */       return Collections.unmodifiableSet(localHashSet);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.DatagramChannelImpl
 * JD-Core Version:    0.6.2
 */