/*      */ package sun.nio.ch;
/*      */ 
/*      */ import java.io.FileDescriptor;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketOption;
/*      */ import java.net.StandardProtocolFamily;
/*      */ import java.net.StandardSocketOptions;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AlreadyBoundException;
/*      */ import java.nio.channels.AlreadyConnectedException;
/*      */ import java.nio.channels.AsynchronousCloseException;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.ConnectionPendingException;
/*      */ import java.nio.channels.NoConnectionPendingException;
/*      */ import java.nio.channels.NotYetConnectedException;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.nio.channels.spi.SelectorProvider;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import sun.misc.IoTrace;
/*      */ import sun.net.NetHooks;
/*      */ 
/*      */ class SocketChannelImpl extends SocketChannel
/*      */   implements SelChImpl
/*      */ {
/* 1037 */   private static NativeDispatcher nd = new SocketDispatcher();
/*      */   private final FileDescriptor fd;
/*      */   private final int fdVal;
/*   58 */   private volatile long readerThread = 0L;
/*   59 */   private volatile long writerThread = 0L;
/*      */ 
/*   62 */   private final Object readLock = new Object();
/*      */ 
/*   65 */   private final Object writeLock = new Object();
/*      */ 
/*   69 */   private final Object stateLock = new Object();
/*      */   private boolean isReuseAddress;
/*      */   private static final int ST_UNINITIALIZED = -1;
/*      */   private static final int ST_UNCONNECTED = 0;
/*      */   private static final int ST_PENDING = 1;
/*      */   private static final int ST_CONNECTED = 2;
/*      */   private static final int ST_KILLPENDING = 3;
/*      */   private static final int ST_KILLED = 4;
/*   83 */   private int state = -1;
/*      */   private InetSocketAddress localAddress;
/*      */   private InetSocketAddress remoteAddress;
/*   90 */   private boolean isInputOpen = true;
/*   91 */   private boolean isOutputOpen = true;
/*   92 */   private boolean readyToConnect = false;
/*      */   private Socket socket;
/*      */ 
/*      */   SocketChannelImpl(SelectorProvider paramSelectorProvider)
/*      */     throws IOException
/*      */   {
/*  103 */     super(paramSelectorProvider);
/*  104 */     this.fd = Net.socket(true);
/*  105 */     this.fdVal = IOUtil.fdVal(this.fd);
/*  106 */     this.state = 0;
/*      */   }
/*      */ 
/*      */   SocketChannelImpl(SelectorProvider paramSelectorProvider, FileDescriptor paramFileDescriptor, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  114 */     super(paramSelectorProvider);
/*  115 */     this.fd = paramFileDescriptor;
/*  116 */     this.fdVal = IOUtil.fdVal(paramFileDescriptor);
/*  117 */     this.state = 0;
/*  118 */     if (paramBoolean)
/*  119 */       this.localAddress = Net.localAddress(paramFileDescriptor);
/*      */   }
/*      */ 
/*      */   SocketChannelImpl(SelectorProvider paramSelectorProvider, FileDescriptor paramFileDescriptor, InetSocketAddress paramInetSocketAddress)
/*      */     throws IOException
/*      */   {
/*  128 */     super(paramSelectorProvider);
/*  129 */     this.fd = paramFileDescriptor;
/*  130 */     this.fdVal = IOUtil.fdVal(paramFileDescriptor);
/*  131 */     this.state = 2;
/*  132 */     this.localAddress = Net.localAddress(paramFileDescriptor);
/*  133 */     this.remoteAddress = paramInetSocketAddress;
/*      */   }
/*      */ 
/*      */   public Socket socket() {
/*  137 */     synchronized (this.stateLock) {
/*  138 */       if (this.socket == null)
/*  139 */         this.socket = SocketAdaptor.create(this);
/*  140 */       return this.socket;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress getLocalAddress() throws IOException
/*      */   {
/*  146 */     synchronized (this.stateLock) {
/*  147 */       if (!isOpen())
/*  148 */         throw new ClosedChannelException();
/*  149 */       return Net.getRevealedLocalAddress(this.localAddress);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress getRemoteAddress() throws IOException
/*      */   {
/*  155 */     synchronized (this.stateLock) {
/*  156 */       if (!isOpen())
/*  157 */         throw new ClosedChannelException();
/*  158 */       return this.remoteAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> SocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*      */     throws IOException
/*      */   {
/*  166 */     if (paramSocketOption == null)
/*  167 */       throw new NullPointerException();
/*  168 */     if (!supportedOptions().contains(paramSocketOption)) {
/*  169 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*      */     }
/*  171 */     synchronized (this.stateLock) {
/*  172 */       if (!isOpen()) {
/*  173 */         throw new ClosedChannelException();
/*      */       }
/*      */ 
/*  176 */       if (paramSocketOption == StandardSocketOptions.IP_TOS) {
/*  177 */         if (!Net.isIPv6Available())
/*  178 */           Net.setSocketOption(this.fd, StandardProtocolFamily.INET, paramSocketOption, paramT);
/*  179 */         return this;
/*  180 */       }if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*      */       {
/*  184 */         this.isReuseAddress = ((Boolean)paramT).booleanValue();
/*  185 */         return this;
/*      */       }
/*      */ 
/*  189 */       Net.setSocketOption(this.fd, Net.UNSPEC, paramSocketOption, paramT);
/*  190 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> T getOption(SocketOption<T> paramSocketOption)
/*      */     throws IOException
/*      */   {
/*  199 */     if (paramSocketOption == null)
/*  200 */       throw new NullPointerException();
/*  201 */     if (!supportedOptions().contains(paramSocketOption)) {
/*  202 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*      */     }
/*  204 */     synchronized (this.stateLock) {
/*  205 */       if (!isOpen()) {
/*  206 */         throw new ClosedChannelException();
/*      */       }
/*  208 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*      */       {
/*  212 */         return Boolean.valueOf(this.isReuseAddress);
/*      */       }
/*      */ 
/*  216 */       if (paramSocketOption == StandardSocketOptions.IP_TOS) {
/*  217 */         return Net.isIPv6Available() ? Integer.valueOf(0) : Net.getSocketOption(this.fd, StandardProtocolFamily.INET, paramSocketOption);
/*      */       }
/*      */ 
/*  222 */       return Net.getSocketOption(this.fd, Net.UNSPEC, paramSocketOption);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Set<SocketOption<?>> supportedOptions()
/*      */   {
/*  246 */     return DefaultOptionsHolder.defaultOptions;
/*      */   }
/*      */ 
/*      */   private boolean ensureReadOpen() throws ClosedChannelException {
/*  250 */     synchronized (this.stateLock) {
/*  251 */       if (!isOpen())
/*  252 */         throw new ClosedChannelException();
/*  253 */       if (!isConnected())
/*  254 */         throw new NotYetConnectedException();
/*  255 */       if (!this.isInputOpen) {
/*  256 */         return false;
/*      */       }
/*  258 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureWriteOpen() throws ClosedChannelException {
/*  263 */     synchronized (this.stateLock) {
/*  264 */       if (!isOpen())
/*  265 */         throw new ClosedChannelException();
/*  266 */       if (!this.isOutputOpen)
/*  267 */         throw new ClosedChannelException();
/*  268 */       if (!isConnected())
/*  269 */         throw new NotYetConnectedException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readerCleanup() throws IOException {
/*  274 */     synchronized (this.stateLock) {
/*  275 */       this.readerThread = 0L;
/*  276 */       if (this.state == 3)
/*  277 */         kill();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writerCleanup() throws IOException {
/*  282 */     synchronized (this.stateLock) {
/*  283 */       this.writerThread = 0L;
/*  284 */       if (this.state == 3)
/*  285 */         kill();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int read(ByteBuffer paramByteBuffer) throws IOException
/*      */   {
/*  291 */     if (paramByteBuffer == null) {
/*  292 */       throw new NullPointerException();
/*      */     }
/*  294 */     synchronized (this.readLock) {
/*  295 */       if (!ensureReadOpen())
/*  296 */         return -1;
/*  297 */       Object localObject1 = null;
/*  298 */       if (isBlocking()) {
/*  299 */         localObject1 = IoTrace.socketReadBegin();
/*      */       }
/*  301 */       int i = 0;
/*      */       try
/*      */       {
/*  307 */         begin();
/*      */ 
/*  309 */         synchronized (this.stateLock) {
/*  310 */           if (!isOpen())
/*      */           {
/*  318 */             int k = 0;
/*      */ 
/*  389 */             readerCleanup();
/*      */ 
/*  391 */             if (isBlocking()) {
/*  392 */               IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, i > 0 ? i : 0L);
/*      */             }
/*      */ 
/*  412 */             end((i > 0) || (i == -2));
/*      */ 
/*  416 */             synchronized (this.stateLock) {
/*  417 */               if ((i <= 0) && (!this.isInputOpen)) {
/*  418 */                 return -1;
/*      */               }
/*      */             }
/*  421 */             assert (IOStatus.check(i)); return k;
/*      */           }
/*  325 */           this.readerThread = NativeThread.current();
/*      */         }
/*      */ 
/*      */         do
/*      */         {
/*  379 */           i = IOUtil.read(this.fd, paramByteBuffer, -1L, nd);
/*  380 */         }while ((i == -3) && (isOpen()));
/*      */ 
/*  385 */         int j = IOStatus.normalize(i);
/*      */ 
/*  389 */         readerCleanup();
/*      */ 
/*  391 */         if (isBlocking()) {
/*  392 */           IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, i > 0 ? i : 0L);
/*      */         }
/*      */ 
/*  412 */         end((i > 0) || (i == -2));
/*      */ 
/*  416 */         synchronized (this.stateLock) {
/*  417 */           if ((i <= 0) && (!this.isInputOpen)) {
/*  418 */             return -1;
/*      */           }
/*      */         }
/*  421 */         assert (IOStatus.check(i)); return j;
/*      */       }
/*      */       finally
/*      */       {
/*  389 */         readerCleanup();
/*      */ 
/*  391 */         if (isBlocking()) {
/*  392 */           IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, i > 0 ? i : 0L);
/*      */         }
/*      */ 
/*  412 */         end((i > 0) || (i == -2));
/*      */ 
/*  416 */         synchronized (this.stateLock) {
/*  417 */           if ((i <= 0) && (!this.isInputOpen)) {
/*  418 */             return -1;
/*      */           }
/*      */         }
/*  421 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  430 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  431 */       throw new IndexOutOfBoundsException();
/*  432 */     synchronized (this.readLock) {
/*  433 */       if (!ensureReadOpen())
/*  434 */         return -1L;
/*  435 */       long l1 = 0L;
/*  436 */       Object localObject1 = null;
/*  437 */       if (isBlocking())
/*  438 */         localObject1 = IoTrace.socketReadBegin();
/*      */       try
/*      */       {
/*  441 */         begin();
/*  442 */         synchronized (this.stateLock) {
/*  443 */           if (!isOpen()) {
/*  444 */             long l3 = 0L;
/*      */ 
/*  455 */             readerCleanup();
/*  456 */             if (isBlocking()) {
/*  457 */               IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, l1 > 0L ? l1 : 0L);
/*      */             }
/*      */ 
/*  460 */             end((l1 > 0L) || (l1 == -2L));
/*  461 */             synchronized (this.stateLock) {
/*  462 */               if ((l1 <= 0L) && (!this.isInputOpen))
/*  463 */                 return -1L;
/*      */             }
/*  465 */             assert (IOStatus.check(l1)); return l3;
/*      */           }
/*  445 */           this.readerThread = NativeThread.current();
/*      */         }
/*      */         do
/*      */         {
/*  449 */           l1 = IOUtil.read(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, nd);
/*  450 */         }while ((l1 == -3L) && (isOpen()));
/*      */ 
/*  452 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  455 */         readerCleanup();
/*  456 */         if (isBlocking()) {
/*  457 */           IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, l1 > 0L ? l1 : 0L);
/*      */         }
/*      */ 
/*  460 */         end((l1 > 0L) || (l1 == -2L));
/*  461 */         synchronized (this.stateLock) {
/*  462 */           if ((l1 <= 0L) && (!this.isInputOpen))
/*  463 */             return -1L;
/*      */         }
/*  465 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  455 */         readerCleanup();
/*  456 */         if (isBlocking()) {
/*  457 */           IoTrace.socketReadEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), 0, l1 > 0L ? l1 : 0L);
/*      */         }
/*      */ 
/*  460 */         end((l1 > 0L) || (l1 == -2L));
/*  461 */         synchronized (this.stateLock) {
/*  462 */           if ((l1 <= 0L) && (!this.isInputOpen))
/*  463 */             return -1L;
/*      */         }
/*  465 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int write(ByteBuffer paramByteBuffer) throws IOException {
/*  471 */     if (paramByteBuffer == null)
/*  472 */       throw new NullPointerException();
/*  473 */     synchronized (this.writeLock) {
/*  474 */       ensureWriteOpen();
/*  475 */       int i = 0;
/*  476 */       Object localObject1 = IoTrace.socketWriteBegin();
/*      */       try
/*      */       {
/*  480 */         begin();
/*  481 */         synchronized (this.stateLock) {
/*  482 */           if (!isOpen()) {
/*  483 */             int k = 0;
/*      */ 
/*  493 */             writerCleanup();
/*  494 */             IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), i > 0 ? i : 0L);
/*      */ 
/*  496 */             end((i > 0) || (i == -2));
/*  497 */             synchronized (this.stateLock) {
/*  498 */               if ((i <= 0) && (!this.isOutputOpen))
/*      */                 throw new AsynchronousCloseException();
/*      */             }
/*  501 */             assert (IOStatus.check(i)); return k;
/*      */           }
/*  484 */           this.writerThread = NativeThread.current();
/*      */         }
/*      */         do
/*  487 */           i = IOUtil.write(this.fd, paramByteBuffer, -1L, nd);
/*  488 */         while ((i == -3) && (isOpen()));
/*      */ 
/*  490 */         int j = IOStatus.normalize(i);
/*      */ 
/*  493 */         writerCleanup();
/*  494 */         IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), i > 0 ? i : 0L);
/*      */ 
/*  496 */         end((i > 0) || (i == -2));
/*  497 */         synchronized (this.stateLock) {
/*  498 */           if ((i <= 0) && (!this.isOutputOpen))
/*      */             throw new AsynchronousCloseException();
/*      */         }
/*  501 */         assert (IOStatus.check(i)); return j;
/*      */       }
/*      */       finally
/*      */       {
/*  493 */         writerCleanup();
/*  494 */         IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), i > 0 ? i : 0L);
/*      */ 
/*  496 */         end((i > 0) || (i == -2));
/*  497 */         synchronized (this.stateLock) {
/*  498 */           if ((i <= 0) && (!this.isOutputOpen))
/*  499 */             throw new AsynchronousCloseException();
/*      */         }
/*  501 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public long write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  509 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/*  510 */       throw new IndexOutOfBoundsException();
/*  511 */     synchronized (this.writeLock) {
/*  512 */       ensureWriteOpen();
/*  513 */       long l1 = 0L;
/*  514 */       Object localObject1 = IoTrace.socketWriteBegin();
/*      */       try
/*      */       {
/*  517 */         begin();
/*  518 */         synchronized (this.stateLock) {
/*  519 */           if (!isOpen()) {
/*  520 */             long l3 = 0L;
/*      */ 
/*  530 */             writerCleanup();
/*  531 */             IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), l1 > 0L ? l1 : 0L);
/*      */ 
/*  533 */             end((l1 > 0L) || (l1 == -2L));
/*  534 */             synchronized (this.stateLock) {
/*  535 */               if ((l1 <= 0L) && (!this.isOutputOpen))
/*      */                 throw new AsynchronousCloseException();
/*      */             }
/*  538 */             assert (IOStatus.check(l1)); return l3;
/*      */           }
/*  521 */           this.writerThread = NativeThread.current();
/*      */         }
/*      */         do
/*  524 */           l1 = IOUtil.write(this.fd, paramArrayOfByteBuffer, paramInt1, paramInt2, nd);
/*  525 */         while ((l1 == -3L) && (isOpen()));
/*      */ 
/*  527 */         long l2 = IOStatus.normalize(l1);
/*      */ 
/*  530 */         writerCleanup();
/*  531 */         IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), l1 > 0L ? l1 : 0L);
/*      */ 
/*  533 */         end((l1 > 0L) || (l1 == -2L));
/*  534 */         synchronized (this.stateLock) {
/*  535 */           if ((l1 <= 0L) && (!this.isOutputOpen))
/*      */             throw new AsynchronousCloseException();
/*      */         }
/*  538 */         assert (IOStatus.check(l1)); return l2;
/*      */       }
/*      */       finally
/*      */       {
/*  530 */         writerCleanup();
/*  531 */         IoTrace.socketWriteEnd(localObject1, this.remoteAddress.getAddress(), this.remoteAddress.getPort(), l1 > 0L ? l1 : 0L);
/*      */ 
/*  533 */         end((l1 > 0L) || (l1 == -2L));
/*  534 */         synchronized (this.stateLock) {
/*  535 */           if ((l1 <= 0L) && (!this.isOutputOpen))
/*  536 */             throw new AsynchronousCloseException();
/*      */         }
/*  538 */         if ((!$assertionsDisabled) && (!IOStatus.check(l1))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   int sendOutOfBandData(byte paramByte)
/*      */     throws IOException
/*      */   {
/*  545 */     synchronized (this.writeLock) {
/*  546 */       ensureWriteOpen();
/*  547 */       int i = 0;
/*      */       try {
/*  549 */         begin();
/*  550 */         synchronized (this.stateLock) {
/*  551 */           if (!isOpen()) {
/*  552 */             int k = 0;
/*      */ 
/*  562 */             writerCleanup();
/*  563 */             end((i > 0) || (i == -2));
/*  564 */             synchronized (this.stateLock) {
/*  565 */               if ((i <= 0) && (!this.isOutputOpen))
/*      */                 throw new AsynchronousCloseException();
/*      */             }
/*  568 */             assert (IOStatus.check(i)); return k;
/*      */           }
/*  553 */           this.writerThread = NativeThread.current();
/*      */         }
/*      */         do
/*  556 */           i = sendOutOfBandData(this.fd, paramByte);
/*  557 */         while ((i == -3) && (isOpen()));
/*      */ 
/*  559 */         int j = IOStatus.normalize(i);
/*      */ 
/*  562 */         writerCleanup();
/*  563 */         end((i > 0) || (i == -2));
/*  564 */         synchronized (this.stateLock) {
/*  565 */           if ((i <= 0) && (!this.isOutputOpen))
/*      */             throw new AsynchronousCloseException();
/*      */         }
/*  568 */         assert (IOStatus.check(i)); return j;
/*      */       }
/*      */       finally
/*      */       {
/*  562 */         writerCleanup();
/*  563 */         end((i > 0) || (i == -2));
/*  564 */         synchronized (this.stateLock) {
/*  565 */           if ((i <= 0) && (!this.isOutputOpen))
/*  566 */             throw new AsynchronousCloseException();
/*      */         }
/*  568 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError(); 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException {
/*  574 */     IOUtil.configureBlocking(this.fd, paramBoolean);
/*      */   }
/*      */ 
/*      */   public InetSocketAddress localAddress() {
/*  578 */     synchronized (this.stateLock) {
/*  579 */       return this.localAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketAddress remoteAddress() {
/*  584 */     synchronized (this.stateLock) {
/*  585 */       return this.remoteAddress;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketChannel bind(SocketAddress paramSocketAddress) throws IOException
/*      */   {
/*  591 */     synchronized (this.readLock) {
/*  592 */       synchronized (this.writeLock) {
/*  593 */         synchronized (this.stateLock) {
/*  594 */           if (!isOpen())
/*  595 */             throw new ClosedChannelException();
/*  596 */           if (this.state == 1)
/*  597 */             throw new ConnectionPendingException();
/*  598 */           if (this.localAddress != null)
/*  599 */             throw new AlreadyBoundException();
/*  600 */           InetSocketAddress localInetSocketAddress = paramSocketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(paramSocketAddress);
/*      */ 
/*  602 */           SecurityManager localSecurityManager = System.getSecurityManager();
/*  603 */           if (localSecurityManager != null) {
/*  604 */             localSecurityManager.checkListen(localInetSocketAddress.getPort());
/*      */           }
/*  606 */           NetHooks.beforeTcpBind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*  607 */           Net.bind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*  608 */           this.localAddress = Net.localAddress(this.fd);
/*      */         }
/*      */       }
/*      */     }
/*  612 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isConnected() {
/*  616 */     synchronized (this.stateLock) {
/*  617 */       return this.state == 2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isConnectionPending() {
/*  622 */     synchronized (this.stateLock) {
/*  623 */       return this.state == 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void ensureOpenAndUnconnected() throws IOException {
/*  628 */     synchronized (this.stateLock) {
/*  629 */       if (!isOpen())
/*  630 */         throw new ClosedChannelException();
/*  631 */       if (this.state == 2)
/*  632 */         throw new AlreadyConnectedException();
/*  633 */       if (this.state == 1)
/*  634 */         throw new ConnectionPendingException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean connect(SocketAddress paramSocketAddress) throws IOException {
/*  639 */     int i = 0;
/*      */ 
/*  641 */     synchronized (this.readLock) {
/*  642 */       synchronized (this.writeLock) {
/*  643 */         ensureOpenAndUnconnected();
/*  644 */         InetSocketAddress localInetSocketAddress = Net.checkAddress(paramSocketAddress);
/*  645 */         SecurityManager localSecurityManager = System.getSecurityManager();
/*  646 */         if (localSecurityManager != null) {
/*  647 */           localSecurityManager.checkConnect(localInetSocketAddress.getAddress().getHostAddress(), localInetSocketAddress.getPort());
/*      */         }
/*  649 */         synchronized (blockingLock()) {
/*  650 */           int j = 0;
/*      */           try {
/*      */             try {
/*  653 */               begin();
/*  654 */               synchronized (this.stateLock) {
/*  655 */                 if (!isOpen()) {
/*  656 */                   boolean bool = false;
/*      */ 
/*  680 */                   readerCleanup();
/*  681 */                   end((j > 0) || (j == -2));
/*  682 */                   assert (IOStatus.check(j)); return bool;
/*      */                 }
/*  659 */                 if (this.localAddress == null) {
/*  660 */                   NetHooks.beforeTcpConnect(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/*      */                 }
/*      */ 
/*  664 */                 this.readerThread = NativeThread.current();
/*      */               }
/*      */               while (true) {
/*  667 */                 ??? = localInetSocketAddress.getAddress();
/*  668 */                 if (???.isAnyLocalAddress())
/*  669 */                   ??? = InetAddress.getLocalHost();
/*  670 */                 j = Net.connect(this.fd, ???, localInetSocketAddress.getPort());
/*      */ 
/*  673 */                 if ((j != -3) || (!isOpen())) {
/*      */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */             finally
/*      */             {
/*  680 */               readerCleanup();
/*  681 */               end((j > 0) || (j == -2));
/*  682 */               if ((!$assertionsDisabled) && (!IOStatus.check(j))) throw new AssertionError();
/*      */             }
/*      */ 
/*      */           }
/*      */           catch (IOException )
/*      */           {
/*  688 */             close();
/*  689 */             throw ???;
/*      */           }
/*  691 */           synchronized (this.stateLock) {
/*  692 */             this.remoteAddress = localInetSocketAddress;
/*  693 */             if (j > 0)
/*      */             {
/*  697 */               this.state = 2;
/*  698 */               if (isOpen())
/*  699 */                 this.localAddress = Net.localAddress(this.fd);
/*  700 */               return true;
/*      */             }
/*      */ 
/*  704 */             if (!isBlocking()) {
/*  705 */               this.state = 1;
/*      */             }
/*  707 */             else if (!$assertionsDisabled) throw new AssertionError();
/*      */           }
/*      */         }
/*  710 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean finishConnect() throws IOException {
/*  716 */     synchronized (this.readLock) {
/*  717 */       synchronized (this.writeLock) {
/*  718 */         synchronized (this.stateLock) {
/*  719 */           if (!isOpen())
/*  720 */             throw new ClosedChannelException();
/*  721 */           if (this.state == 2)
/*  722 */             return true;
/*  723 */           if (this.state != 1)
/*  724 */             throw new NoConnectionPendingException();
/*      */         }
/*  726 */         int i = 0;
/*      */         try {
/*      */           try {
/*  729 */             begin();
/*  730 */             synchronized (blockingLock()) {
/*  731 */               synchronized (this.stateLock) {
/*  732 */                 if (!isOpen()) {
/*  733 */                   boolean bool = false;
/*      */ 
/*  763 */                   synchronized (this.stateLock) {
/*  764 */                     this.readerThread = 0L;
/*  765 */                     if (this.state == 3) {
/*  766 */                       kill();
/*      */ 
/*  772 */                       i = 0;
/*      */                     }
/*      */                   }
/*  775 */                   end((i > 0) || (i == -2));
/*  776 */                   assert (IOStatus.check(i)); return bool;
/*      */                 }
/*  735 */                 this.readerThread = NativeThread.current();
/*      */               }
/*  737 */               if (!isBlocking()) {
/*      */                 while (true) {
/*  739 */                   i = checkConnect(this.fd, false, this.readyToConnect);
/*      */ 
/*  741 */                   if ((i != -3) || (!isOpen())) {
/*      */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/*      */               while (true)
/*      */               {
/*  748 */                 i = checkConnect(this.fd, true, this.readyToConnect);
/*      */ 
/*  750 */                 if (i != 0)
/*      */                 {
/*  755 */                   if ((i != -3) || (!isOpen()))
/*      */                     break;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           finally
/*      */           {
/*  763 */             synchronized (this.stateLock) {
/*  764 */               this.readerThread = 0L;
/*  765 */               if (this.state == 3) {
/*  766 */                 kill();
/*      */ 
/*  772 */                 i = 0;
/*      */               }
/*      */             }
/*  775 */             end((i > 0) || (i == -2));
/*  776 */             if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (IOException localObject1)
/*      */         {
/*  782 */           close();
/*  783 */           throw ???;
/*      */         }
/*  785 */         if (i > 0) {
/*  786 */           synchronized (this.stateLock) {
/*  787 */             this.state = 2;
/*  788 */             if (isOpen())
/*  789 */               this.localAddress = Net.localAddress(this.fd);
/*      */           }
/*  791 */           return true;
/*      */         }
/*  793 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketChannel shutdownInput() throws IOException
/*      */   {
/*  800 */     synchronized (this.stateLock) {
/*  801 */       if (!isOpen())
/*  802 */         throw new ClosedChannelException();
/*  803 */       if (!isConnected())
/*  804 */         throw new NotYetConnectedException();
/*  805 */       if (this.isInputOpen) {
/*  806 */         Net.shutdown(this.fd, 0);
/*  807 */         if (this.readerThread != 0L)
/*  808 */           NativeThread.signal(this.readerThread);
/*  809 */         this.isInputOpen = false;
/*      */       }
/*  811 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SocketChannel shutdownOutput() throws IOException
/*      */   {
/*  817 */     synchronized (this.stateLock) {
/*  818 */       if (!isOpen())
/*  819 */         throw new ClosedChannelException();
/*  820 */       if (!isConnected())
/*  821 */         throw new NotYetConnectedException();
/*  822 */       if (this.isOutputOpen) {
/*  823 */         Net.shutdown(this.fd, 1);
/*  824 */         if (this.writerThread != 0L)
/*  825 */           NativeThread.signal(this.writerThread);
/*  826 */         this.isOutputOpen = false;
/*      */       }
/*  828 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isInputOpen() {
/*  833 */     synchronized (this.stateLock) {
/*  834 */       return this.isInputOpen;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isOutputOpen() {
/*  839 */     synchronized (this.stateLock) {
/*  840 */       return this.isOutputOpen;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void implCloseSelectableChannel()
/*      */     throws IOException
/*      */   {
/*  850 */     synchronized (this.stateLock) {
/*  851 */       this.isInputOpen = false;
/*  852 */       this.isOutputOpen = false;
/*      */ 
/*  859 */       if (this.state != 4) {
/*  860 */         nd.preClose(this.fd);
/*      */       }
/*      */ 
/*  866 */       if (this.readerThread != 0L) {
/*  867 */         NativeThread.signal(this.readerThread);
/*      */       }
/*  869 */       if (this.writerThread != 0L) {
/*  870 */         NativeThread.signal(this.writerThread);
/*      */       }
/*      */ 
/*  882 */       if (!isRegistered())
/*  883 */         kill();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void kill() throws IOException {
/*  888 */     synchronized (this.stateLock) {
/*  889 */       if (this.state == 4)
/*  890 */         return;
/*  891 */       if (this.state == -1) {
/*  892 */         this.state = 4;
/*  893 */         return;
/*      */       }
/*  895 */       assert ((!isOpen()) && (!isRegistered()));
/*      */ 
/*  900 */       if ((this.readerThread == 0L) && (this.writerThread == 0L)) {
/*  901 */         nd.close(this.fd);
/*  902 */         this.state = 4;
/*      */       } else {
/*  904 */         this.state = 3;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean translateReadyOps(int paramInt1, int paramInt2, SelectionKeyImpl paramSelectionKeyImpl)
/*      */   {
/*  914 */     int i = paramSelectionKeyImpl.nioInterestOps();
/*  915 */     int j = paramSelectionKeyImpl.nioReadyOps();
/*  916 */     int k = paramInt2;
/*      */ 
/*  918 */     if ((paramInt1 & 0x20) != 0)
/*      */     {
/*  922 */       return false;
/*      */     }
/*      */ 
/*  925 */     if ((paramInt1 & 0x18) != 0)
/*      */     {
/*  927 */       k = i;
/*  928 */       paramSelectionKeyImpl.nioReadyOps(k);
/*      */ 
/*  931 */       this.readyToConnect = true;
/*  932 */       return (k & (j ^ 0xFFFFFFFF)) != 0;
/*      */     }
/*      */ 
/*  935 */     if (((paramInt1 & 0x1) != 0) && ((i & 0x1) != 0) && (this.state == 2))
/*      */     {
/*  938 */       k |= 1;
/*      */     }
/*  940 */     if (((paramInt1 & 0x2) != 0) && ((i & 0x8) != 0) && ((this.state == 0) || (this.state == 1)))
/*      */     {
/*  943 */       k |= 8;
/*  944 */       this.readyToConnect = true;
/*      */     }
/*      */ 
/*  947 */     if (((paramInt1 & 0x4) != 0) && ((i & 0x4) != 0) && (this.state == 2))
/*      */     {
/*  950 */       k |= 4;
/*      */     }
/*  952 */     paramSelectionKeyImpl.nioReadyOps(k);
/*  953 */     return (k & (j ^ 0xFFFFFFFF)) != 0;
/*      */   }
/*      */ 
/*      */   public boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/*  957 */     return translateReadyOps(paramInt, paramSelectionKeyImpl.nioReadyOps(), paramSelectionKeyImpl);
/*      */   }
/*      */ 
/*      */   public boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/*  961 */     return translateReadyOps(paramInt, 0, paramSelectionKeyImpl);
/*      */   }
/*      */ 
/*      */   public void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl)
/*      */   {
/*  968 */     int i = 0;
/*  969 */     if ((paramInt & 0x1) != 0)
/*  970 */       i |= 1;
/*  971 */     if ((paramInt & 0x4) != 0)
/*  972 */       i |= 4;
/*  973 */     if ((paramInt & 0x8) != 0)
/*  974 */       i |= 2;
/*  975 */     paramSelectionKeyImpl.selector.putEventOps(paramSelectionKeyImpl, i);
/*      */   }
/*      */ 
/*      */   public FileDescriptor getFD() {
/*  979 */     return this.fd;
/*      */   }
/*      */ 
/*      */   public int getFDVal() {
/*  983 */     return this.fdVal;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  988 */     StringBuffer localStringBuffer = new StringBuffer();
/*  989 */     localStringBuffer.append(getClass().getSuperclass().getName());
/*  990 */     localStringBuffer.append('[');
/*  991 */     if (!isOpen())
/*  992 */       localStringBuffer.append("closed");
/*      */     else {
/*  994 */       synchronized (this.stateLock) {
/*  995 */         switch (this.state) {
/*      */         case 0:
/*  997 */           localStringBuffer.append("unconnected");
/*  998 */           break;
/*      */         case 1:
/* 1000 */           localStringBuffer.append("connection-pending");
/* 1001 */           break;
/*      */         case 2:
/* 1003 */           localStringBuffer.append("connected");
/* 1004 */           if (!this.isInputOpen)
/* 1005 */             localStringBuffer.append(" ishut");
/* 1006 */           if (!this.isOutputOpen)
/* 1007 */             localStringBuffer.append(" oshut");
/*      */           break;
/*      */         }
/* 1010 */         InetSocketAddress localInetSocketAddress = localAddress();
/* 1011 */         if (localInetSocketAddress != null) {
/* 1012 */           localStringBuffer.append(" local=");
/* 1013 */           localStringBuffer.append(Net.getRevealedLocalAddressAsString(localInetSocketAddress));
/*      */         }
/* 1015 */         if (remoteAddress() != null) {
/* 1016 */           localStringBuffer.append(" remote=");
/* 1017 */           localStringBuffer.append(remoteAddress().toString());
/*      */         }
/*      */       }
/*      */     }
/* 1021 */     localStringBuffer.append(']');
/* 1022 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static native int checkConnect(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IOException;
/*      */ 
/*      */   private static native int sendOutOfBandData(FileDescriptor paramFileDescriptor, byte paramByte)
/*      */     throws IOException;
/*      */ 
/*      */   static
/*      */   {
/* 1036 */     Util.load();
/*      */   }
/*      */ 
/*      */   private static class DefaultOptionsHolder
/*      */   {
/*  227 */     static final Set<SocketOption<?>> defaultOptions = defaultOptions();
/*      */ 
/*      */     private static Set<SocketOption<?>> defaultOptions() {
/*  230 */       HashSet localHashSet = new HashSet(8);
/*  231 */       localHashSet.add(StandardSocketOptions.SO_SNDBUF);
/*  232 */       localHashSet.add(StandardSocketOptions.SO_RCVBUF);
/*  233 */       localHashSet.add(StandardSocketOptions.SO_KEEPALIVE);
/*  234 */       localHashSet.add(StandardSocketOptions.SO_REUSEADDR);
/*  235 */       localHashSet.add(StandardSocketOptions.SO_LINGER);
/*  236 */       localHashSet.add(StandardSocketOptions.TCP_NODELAY);
/*      */ 
/*  238 */       localHashSet.add(StandardSocketOptions.IP_TOS);
/*  239 */       localHashSet.add(ExtendedSocketOption.SO_OOBINLINE);
/*  240 */       return Collections.unmodifiableSet(localHashSet);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SocketChannelImpl
 * JD-Core Version:    0.6.2
 */