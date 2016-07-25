/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.channels.AlreadyBoundException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NotYetBoundException;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import sun.net.NetHooks;
/*     */ 
/*     */ class ServerSocketChannelImpl extends ServerSocketChannel
/*     */   implements SelChImpl
/*     */ {
/* 399 */   private static NativeDispatcher nd = new SocketDispatcher();
/*     */   private final FileDescriptor fd;
/*     */   private int fdVal;
/*  57 */   private volatile long thread = 0L;
/*     */ 
/*  60 */   private final Object lock = new Object();
/*     */ 
/*  64 */   private final Object stateLock = new Object();
/*     */   private static final int ST_UNINITIALIZED = -1;
/*     */   private static final int ST_INUSE = 0;
/*     */   private static final int ST_KILLED = 1;
/*  72 */   private int state = -1;
/*     */   private InetSocketAddress localAddress;
/*     */   private boolean isReuseAddress;
/*     */   ServerSocket socket;
/*     */ 
/*     */   ServerSocketChannelImpl(SelectorProvider paramSelectorProvider)
/*     */     throws IOException
/*     */   {
/*  87 */     super(paramSelectorProvider);
/*  88 */     this.fd = Net.serverSocket(true);
/*  89 */     this.fdVal = IOUtil.fdVal(this.fd);
/*  90 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   ServerSocketChannelImpl(SelectorProvider paramSelectorProvider, FileDescriptor paramFileDescriptor, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  98 */     super(paramSelectorProvider);
/*  99 */     this.fd = paramFileDescriptor;
/* 100 */     this.fdVal = IOUtil.fdVal(paramFileDescriptor);
/* 101 */     this.state = 0;
/* 102 */     if (paramBoolean)
/* 103 */       this.localAddress = Net.localAddress(paramFileDescriptor);
/*     */   }
/*     */ 
/*     */   public ServerSocket socket() {
/* 107 */     synchronized (this.stateLock) {
/* 108 */       if (this.socket == null)
/* 109 */         this.socket = ServerSocketAdaptor.create(this);
/* 110 */       return this.socket;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SocketAddress getLocalAddress() throws IOException
/*     */   {
/* 116 */     synchronized (this.stateLock) {
/* 117 */       if (!isOpen())
/* 118 */         throw new ClosedChannelException();
/* 119 */       return this.localAddress == null ? this.localAddress : Net.getRevealedLocalAddress(Net.asInetSocketAddress(this.localAddress));
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> ServerSocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException
/*     */   {
/* 129 */     if (paramSocketOption == null)
/* 130 */       throw new NullPointerException();
/* 131 */     if (!supportedOptions().contains(paramSocketOption))
/* 132 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/* 133 */     synchronized (this.stateLock) {
/* 134 */       if (!isOpen())
/* 135 */         throw new ClosedChannelException();
/* 136 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 140 */         this.isReuseAddress = ((Boolean)paramT).booleanValue();
/*     */       }
/*     */       else {
/* 143 */         Net.setSocketOption(this.fd, Net.UNSPEC, paramSocketOption, paramT);
/*     */       }
/* 145 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T getOption(SocketOption<T> paramSocketOption)
/*     */     throws IOException
/*     */   {
/* 154 */     if (paramSocketOption == null)
/* 155 */       throw new NullPointerException();
/* 156 */     if (!supportedOptions().contains(paramSocketOption)) {
/* 157 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*     */     }
/* 159 */     synchronized (this.stateLock) {
/* 160 */       if (!isOpen())
/* 161 */         throw new ClosedChannelException();
/* 162 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 166 */         return Boolean.valueOf(this.isReuseAddress);
/*     */       }
/*     */ 
/* 169 */       return Net.getSocketOption(this.fd, Net.UNSPEC, paramSocketOption);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Set<SocketOption<?>> supportedOptions()
/*     */   {
/* 186 */     return DefaultOptionsHolder.defaultOptions;
/*     */   }
/*     */ 
/*     */   public boolean isBound() {
/* 190 */     synchronized (this.stateLock) {
/* 191 */       return this.localAddress != null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetSocketAddress localAddress() {
/* 196 */     synchronized (this.stateLock) {
/* 197 */       return this.localAddress;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerSocketChannel bind(SocketAddress paramSocketAddress, int paramInt) throws IOException
/*     */   {
/* 203 */     synchronized (this.lock) {
/* 204 */       if (!isOpen())
/* 205 */         throw new ClosedChannelException();
/* 206 */       if (isBound())
/* 207 */         throw new AlreadyBoundException();
/* 208 */       InetSocketAddress localInetSocketAddress = paramSocketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(paramSocketAddress);
/*     */ 
/* 210 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 211 */       if (localSecurityManager != null)
/* 212 */         localSecurityManager.checkListen(localInetSocketAddress.getPort());
/* 213 */       NetHooks.beforeTcpBind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 214 */       Net.bind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 215 */       Net.listen(this.fd, paramInt < 1 ? 50 : paramInt);
/* 216 */       synchronized (this.stateLock) {
/* 217 */         this.localAddress = Net.localAddress(this.fd);
/*     */       }
/*     */     }
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   public SocketChannel accept() throws IOException {
/* 224 */     synchronized (this.lock) {
/* 225 */       if (!isOpen())
/* 226 */         throw new ClosedChannelException();
/* 227 */       if (!isBound())
/* 228 */         throw new NotYetBoundException();
/* 229 */       SocketChannelImpl localSocketChannelImpl = null;
/*     */ 
/* 231 */       int i = 0;
/* 232 */       FileDescriptor localFileDescriptor = new FileDescriptor();
/* 233 */       InetSocketAddress[] arrayOfInetSocketAddress = new InetSocketAddress[1];
/*     */       try
/*     */       {
/* 236 */         begin();
/* 237 */         if (!isOpen()) {
/* 238 */           localObject1 = null;
/*     */ 
/* 247 */           this.thread = 0L;
/* 248 */           end(i > 0);
/* 249 */           assert (IOStatus.check(i)); return localObject1;
/*     */         }
/* 239 */         this.thread = NativeThread.current();
/*     */         while (true) {
/* 241 */           i = accept0(this.fd, localFileDescriptor, arrayOfInetSocketAddress);
/* 242 */           if ((i != -3) || (!isOpen()))
/*     */             break;
/*     */         }
/*     */       }
/*     */       finally {
/* 247 */         this.thread = 0L;
/* 248 */         end(i > 0);
/* 249 */         if ((!$assertionsDisabled) && (!IOStatus.check(i))) throw new AssertionError();
/*     */       }
/*     */ 
/* 252 */       if (i < 1) {
/* 253 */         return null;
/*     */       }
/* 255 */       IOUtil.configureBlocking(localFileDescriptor, true);
/* 256 */       Object localObject1 = arrayOfInetSocketAddress[0];
/* 257 */       localSocketChannelImpl = new SocketChannelImpl(provider(), localFileDescriptor, (InetSocketAddress)localObject1);
/* 258 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 259 */       if (localSecurityManager != null) {
/*     */         try {
/* 261 */           localSecurityManager.checkAccept(((InetSocketAddress)localObject1).getAddress().getHostAddress(), ((InetSocketAddress)localObject1).getPort());
/*     */         }
/*     */         catch (SecurityException localSecurityException) {
/* 264 */           localSocketChannelImpl.close();
/* 265 */           throw localSecurityException;
/*     */         }
/*     */       }
/* 268 */       return localSocketChannelImpl;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void implConfigureBlocking(boolean paramBoolean) throws IOException
/*     */   {
/* 274 */     IOUtil.configureBlocking(this.fd, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void implCloseSelectableChannel() throws IOException {
/* 278 */     synchronized (this.stateLock) {
/* 279 */       if (this.state != 1)
/* 280 */         nd.preClose(this.fd);
/* 281 */       long l = this.thread;
/* 282 */       if (l != 0L)
/* 283 */         NativeThread.signal(l);
/* 284 */       if (!isRegistered())
/* 285 */         kill();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void kill() throws IOException {
/* 290 */     synchronized (this.stateLock) {
/* 291 */       if (this.state == 1)
/* 292 */         return;
/* 293 */       if (this.state == -1) {
/* 294 */         this.state = 1;
/* 295 */         return;
/*     */       }
/* 297 */       assert ((!isOpen()) && (!isRegistered()));
/* 298 */       nd.close(this.fd);
/* 299 */       this.state = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean translateReadyOps(int paramInt1, int paramInt2, SelectionKeyImpl paramSelectionKeyImpl)
/*     */   {
/* 308 */     int i = paramSelectionKeyImpl.nioInterestOps();
/* 309 */     int j = paramSelectionKeyImpl.nioReadyOps();
/* 310 */     int k = paramInt2;
/*     */ 
/* 312 */     if ((paramInt1 & 0x20) != 0)
/*     */     {
/* 316 */       return false;
/*     */     }
/*     */ 
/* 319 */     if ((paramInt1 & 0x18) != 0)
/*     */     {
/* 321 */       k = i;
/* 322 */       paramSelectionKeyImpl.nioReadyOps(k);
/* 323 */       return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */     }
/*     */ 
/* 326 */     if (((paramInt1 & 0x1) != 0) && ((i & 0x10) != 0))
/*     */     {
/* 328 */       k |= 16;
/*     */     }
/* 330 */     paramSelectionKeyImpl.nioReadyOps(k);
/* 331 */     return (k & (j ^ 0xFFFFFFFF)) != 0;
/*     */   }
/*     */ 
/*     */   public boolean translateAndUpdateReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 335 */     return translateReadyOps(paramInt, paramSelectionKeyImpl.nioReadyOps(), paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public boolean translateAndSetReadyOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl) {
/* 339 */     return translateReadyOps(paramInt, 0, paramSelectionKeyImpl);
/*     */   }
/*     */ 
/*     */   public void translateAndSetInterestOps(int paramInt, SelectionKeyImpl paramSelectionKeyImpl)
/*     */   {
/* 346 */     int i = 0;
/*     */ 
/* 349 */     if ((paramInt & 0x10) != 0) {
/* 350 */       i |= 1;
/*     */     }
/* 352 */     paramSelectionKeyImpl.selector.putEventOps(paramSelectionKeyImpl, i);
/*     */   }
/*     */ 
/*     */   public FileDescriptor getFD() {
/* 356 */     return this.fd;
/*     */   }
/*     */ 
/*     */   public int getFDVal() {
/* 360 */     return this.fdVal;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 364 */     StringBuffer localStringBuffer = new StringBuffer();
/* 365 */     localStringBuffer.append(getClass().getName());
/* 366 */     localStringBuffer.append('[');
/* 367 */     if (!isOpen())
/* 368 */       localStringBuffer.append("closed");
/*     */     else {
/* 370 */       synchronized (this.stateLock) {
/* 371 */         InetSocketAddress localInetSocketAddress = localAddress();
/* 372 */         if (localInetSocketAddress == null)
/* 373 */           localStringBuffer.append("unbound");
/*     */         else {
/* 375 */           localStringBuffer.append(Net.getRevealedLocalAddressAsString(localInetSocketAddress));
/*     */         }
/*     */       }
/*     */     }
/* 379 */     localStringBuffer.append(']');
/* 380 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private native int accept0(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, InetSocketAddress[] paramArrayOfInetSocketAddress)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 397 */     Util.load();
/* 398 */     initIDs();
/*     */   }
/*     */ 
/*     */   private static class DefaultOptionsHolder
/*     */   {
/* 174 */     static final Set<SocketOption<?>> defaultOptions = defaultOptions();
/*     */ 
/*     */     private static Set<SocketOption<?>> defaultOptions() {
/* 177 */       HashSet localHashSet = new HashSet(2);
/* 178 */       localHashSet.add(StandardSocketOptions.SO_RCVBUF);
/* 179 */       localHashSet.add(StandardSocketOptions.SO_REUSEADDR);
/* 180 */       return Collections.unmodifiableSet(localHashSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.ServerSocketChannelImpl
 * JD-Core Version:    0.6.2
 */