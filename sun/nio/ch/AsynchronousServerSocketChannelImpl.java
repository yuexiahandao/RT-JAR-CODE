/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.channels.AlreadyBoundException;
/*     */ import java.nio.channels.AsynchronousServerSocketChannel;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import sun.net.NetHooks;
/*     */ 
/*     */ abstract class AsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannel
/*     */   implements Cancellable, Groupable
/*     */ {
/*     */   protected final FileDescriptor fd;
/*  54 */   protected volatile InetSocketAddress localAddress = null;
/*     */ 
/*  57 */   private final Object stateLock = new Object();
/*     */ 
/*  60 */   private ReadWriteLock closeLock = new ReentrantReadWriteLock();
/*  61 */   private volatile boolean open = true;
/*     */   private volatile boolean acceptKilled;
/*     */   private boolean isReuseAddress;
/*     */ 
/*     */   AsynchronousServerSocketChannelImpl(AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl)
/*     */   {
/*  70 */     super(paramAsynchronousChannelGroupImpl.provider());
/*  71 */     this.fd = Net.serverSocket(true);
/*     */   }
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/*  76 */     return this.open;
/*     */   }
/*     */ 
/*     */   final void begin()
/*     */     throws IOException
/*     */   {
/*  83 */     this.closeLock.readLock().lock();
/*  84 */     if (!isOpen())
/*  85 */       throw new ClosedChannelException();
/*     */   }
/*     */ 
/*     */   final void end()
/*     */   {
/*  92 */     this.closeLock.readLock().unlock();
/*     */   }
/*     */ 
/*     */   abstract void implClose()
/*     */     throws IOException;
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/* 103 */     this.closeLock.writeLock().lock();
/*     */     try {
/* 105 */       if (!this.open)
/*     */         return;
/* 107 */       this.open = false;
/*     */     } finally {
/* 109 */       this.closeLock.writeLock().unlock();
/*     */     }
/* 111 */     implClose();
/*     */   }
/*     */ 
/*     */   abstract Future<AsynchronousSocketChannel> implAccept(Object paramObject, CompletionHandler<AsynchronousSocketChannel, Object> paramCompletionHandler);
/*     */ 
/*     */   public final Future<AsynchronousSocketChannel> accept()
/*     */   {
/* 124 */     return implAccept(null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void accept(A paramA, CompletionHandler<AsynchronousSocketChannel, ? super A> paramCompletionHandler)
/*     */   {
/* 132 */     if (paramCompletionHandler == null)
/* 133 */       throw new NullPointerException("'handler' is null");
/* 134 */     implAccept(paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   final boolean isAcceptKilled() {
/* 138 */     return this.acceptKilled;
/*     */   }
/*     */ 
/*     */   public final void onCancel(PendingFuture<?, ?> paramPendingFuture)
/*     */   {
/* 143 */     this.acceptKilled = true;
/*     */   }
/*     */ 
/*     */   public final AsynchronousServerSocketChannel bind(SocketAddress paramSocketAddress, int paramInt)
/*     */     throws IOException
/*     */   {
/* 150 */     InetSocketAddress localInetSocketAddress = paramSocketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(paramSocketAddress);
/*     */ 
/* 152 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 153 */     if (localSecurityManager != null)
/* 154 */       localSecurityManager.checkListen(localInetSocketAddress.getPort());
/*     */     try
/*     */     {
/* 157 */       begin();
/* 158 */       synchronized (this.stateLock) {
/* 159 */         if (this.localAddress != null)
/* 160 */           throw new AlreadyBoundException();
/* 161 */         NetHooks.beforeTcpBind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 162 */         Net.bind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 163 */         Net.listen(this.fd, paramInt < 1 ? 50 : paramInt);
/* 164 */         this.localAddress = Net.localAddress(this.fd);
/*     */       }
/*     */     } finally {
/* 167 */       end();
/*     */     }
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   public final SocketAddress getLocalAddress() throws IOException
/*     */   {
/* 174 */     if (!isOpen())
/* 175 */       throw new ClosedChannelException();
/* 176 */     return Net.getRevealedLocalAddress(this.localAddress);
/*     */   }
/*     */ 
/*     */   public final <T> AsynchronousServerSocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException
/*     */   {
/* 184 */     if (paramSocketOption == null)
/* 185 */       throw new NullPointerException();
/* 186 */     if (!supportedOptions().contains(paramSocketOption))
/* 187 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*     */     try
/*     */     {
/* 190 */       begin();
/* 191 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 195 */         this.isReuseAddress = ((Boolean)paramT).booleanValue();
/*     */       }
/* 197 */       else Net.setSocketOption(this.fd, Net.UNSPEC, paramSocketOption, paramT);
/*     */ 
/* 199 */       return this;
/*     */     } finally {
/* 201 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final <T> T getOption(SocketOption<T> paramSocketOption)
/*     */     throws IOException
/*     */   {
/* 208 */     if (paramSocketOption == null)
/* 209 */       throw new NullPointerException();
/* 210 */     if (!supportedOptions().contains(paramSocketOption))
/* 211 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*     */     try
/*     */     {
/* 214 */       begin();
/*     */       Object localObject1;
/* 215 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 219 */         return Boolean.valueOf(this.isReuseAddress);
/*     */       }
/* 221 */       return Net.getSocketOption(this.fd, Net.UNSPEC, paramSocketOption);
/*     */     } finally {
/* 223 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Set<SocketOption<?>> supportedOptions()
/*     */   {
/* 240 */     return DefaultOptionsHolder.defaultOptions;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 245 */     StringBuilder localStringBuilder = new StringBuilder();
/* 246 */     localStringBuilder.append(getClass().getName());
/* 247 */     localStringBuilder.append('[');
/* 248 */     if (!isOpen()) {
/* 249 */       localStringBuilder.append("closed");
/*     */     }
/* 251 */     else if (this.localAddress == null)
/* 252 */       localStringBuilder.append("unbound");
/*     */     else {
/* 254 */       localStringBuilder.append(Net.getRevealedLocalAddressAsString(this.localAddress));
/*     */     }
/*     */ 
/* 257 */     localStringBuilder.append(']');
/* 258 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static class DefaultOptionsHolder
/*     */   {
/* 228 */     static final Set<SocketOption<?>> defaultOptions = defaultOptions();
/*     */ 
/*     */     private static Set<SocketOption<?>> defaultOptions() {
/* 231 */       HashSet localHashSet = new HashSet(2);
/* 232 */       localHashSet.add(StandardSocketOptions.SO_RCVBUF);
/* 233 */       localHashSet.add(StandardSocketOptions.SO_REUSEADDR);
/* 234 */       return Collections.unmodifiableSet(localHashSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.AsynchronousServerSocketChannelImpl
 * JD-Core Version:    0.6.2
 */