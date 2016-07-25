/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.channels.AcceptPendingException;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.NotYetBoundException;
/*     */ import java.nio.channels.ShutdownChannelGroupException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsAsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannelImpl
/*     */   implements Iocp.OverlappedChannel
/*     */ {
/*  45 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final int DATA_BUFFER_SIZE = 88;
/*     */   private final long handle;
/*     */   private final int completionKey;
/*     */   private final Iocp iocp;
/*     */   private final PendingIoCache ioCache;
/*     */   private final long dataBuffer;
/*  65 */   private AtomicBoolean accepting = new AtomicBoolean();
/*     */ 
/*     */   WindowsAsynchronousServerSocketChannelImpl(Iocp paramIocp) throws IOException {
/*  69 */     super(paramIocp);
/*     */ 
/*  72 */     long l = IOUtil.fdVal(this.fd);
/*     */     int i;
/*     */     try {
/*  75 */       i = paramIocp.associate(this, l);
/*     */     } catch (IOException localIOException) {
/*  77 */       closesocket0(l);
/*  78 */       throw localIOException;
/*     */     }
/*     */ 
/*  81 */     this.handle = l;
/*  82 */     this.completionKey = i;
/*  83 */     this.iocp = paramIocp;
/*  84 */     this.ioCache = new PendingIoCache();
/*  85 */     this.dataBuffer = unsafe.allocateMemory(88L);
/*     */   }
/*     */ 
/*     */   public <V, A> PendingFuture<V, A> getByOverlapped(long paramLong)
/*     */   {
/*  90 */     return this.ioCache.remove(paramLong);
/*     */   }
/*     */ 
/*     */   void implClose()
/*     */     throws IOException
/*     */   {
/*  96 */     closesocket0(this.handle);
/*     */ 
/*  99 */     this.ioCache.close();
/*     */ 
/* 102 */     this.iocp.disassociate(this.completionKey);
/*     */ 
/* 105 */     unsafe.freeMemory(this.dataBuffer);
/*     */   }
/*     */ 
/*     */   public AsynchronousChannelGroupImpl group()
/*     */   {
/* 110 */     return this.iocp;
/*     */   }
/*     */ 
/*     */   Future<AsynchronousSocketChannel> implAccept(Object paramObject, CompletionHandler<AsynchronousSocketChannel, Object> paramCompletionHandler)
/*     */   {
/* 294 */     if (!isOpen()) {
/* 295 */       localObject1 = new ClosedChannelException();
/* 296 */       if (paramCompletionHandler == null)
/* 297 */         return CompletedFuture.withFailure((Throwable)localObject1);
/* 298 */       Invoker.invokeIndirectly(this, paramCompletionHandler, paramObject, null, (Throwable)localObject1);
/* 299 */       return null;
/*     */     }
/* 301 */     if (isAcceptKilled()) {
/* 302 */       throw new RuntimeException("Accept not allowed due to cancellation");
/*     */     }
/*     */ 
/* 305 */     if (this.localAddress == null) {
/* 306 */       throw new NotYetBoundException();
/*     */     }
/*     */ 
/* 312 */     Object localObject1 = null;
/* 313 */     Object localObject2 = null;
/*     */     try {
/* 315 */       begin();
/* 316 */       localObject1 = new WindowsAsynchronousSocketChannelImpl(this.iocp, false);
/*     */     } catch (IOException localIOException) {
/* 318 */       localObject2 = localIOException;
/*     */     } finally {
/* 320 */       end();
/*     */     }
/* 322 */     if (localObject2 != null) {
/* 323 */       if (paramCompletionHandler == null)
/* 324 */         return CompletedFuture.withFailure(localObject2);
/* 325 */       Invoker.invokeIndirectly(this, paramCompletionHandler, paramObject, null, localObject2);
/* 326 */       return null;
/*     */     }
/*     */ 
/* 332 */     AccessControlContext localAccessControlContext = System.getSecurityManager() == null ? null : AccessController.getContext();
/*     */ 
/* 335 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramObject);
/*     */ 
/* 337 */     AcceptTask localAcceptTask = new AcceptTask((WindowsAsynchronousSocketChannelImpl)localObject1, localAccessControlContext, localPendingFuture);
/* 338 */     localPendingFuture.setContext(localAcceptTask);
/*     */ 
/* 341 */     if (!this.accepting.compareAndSet(false, true)) {
/* 342 */       throw new AcceptPendingException();
/*     */     }
/*     */ 
/* 345 */     if (Iocp.supportsThreadAgnosticIo())
/* 346 */       localAcceptTask.run();
/*     */     else {
/* 348 */       Invoker.invokeOnThreadInThreadPool(this, localAcceptTask);
/*     */     }
/* 350 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native int accept0(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void updateAcceptContext(long paramLong1, long paramLong2)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void closesocket0(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/* 366 */     Util.load();
/* 367 */     initIDs();
/*     */   }
/*     */ 
/*     */   private class AcceptTask
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final WindowsAsynchronousSocketChannelImpl channel;
/*     */     private final AccessControlContext acc;
/*     */     private final PendingFuture<AsynchronousSocketChannel, Object> result;
/*     */ 
/*     */     AcceptTask(AccessControlContext paramPendingFuture, PendingFuture<AsynchronousSocketChannel, Object> arg3)
/*     */     {
/* 125 */       this.channel = paramPendingFuture;
/*     */       Object localObject1;
/* 126 */       this.acc = localObject1;
/*     */       Object localObject2;
/* 127 */       this.result = localObject2;
/*     */     }
/*     */ 
/*     */     void enableAccept() {
/* 131 */       WindowsAsynchronousServerSocketChannelImpl.this.accepting.set(false);
/*     */     }
/*     */ 
/*     */     void closeChildChannel() {
/*     */       try {
/* 136 */         this.channel.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     void finishAccept()
/*     */       throws IOException
/*     */     {
/* 147 */       WindowsAsynchronousServerSocketChannelImpl.updateAcceptContext(WindowsAsynchronousServerSocketChannelImpl.this.handle, this.channel.handle());
/*     */ 
/* 149 */       InetSocketAddress localInetSocketAddress1 = Net.localAddress(this.channel.fd);
/* 150 */       final InetSocketAddress localInetSocketAddress2 = Net.remoteAddress(this.channel.fd);
/* 151 */       this.channel.setConnected(localInetSocketAddress1, localInetSocketAddress2);
/*     */ 
/* 154 */       if (this.acc != null)
/* 155 */         AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Void run() {
/* 157 */             SecurityManager localSecurityManager = System.getSecurityManager();
/* 158 */             localSecurityManager.checkAccept(localInetSocketAddress2.getAddress().getHostAddress(), localInetSocketAddress2.getPort());
/*     */ 
/* 160 */             return null;
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 171 */       long l = 0L;
/*     */       try
/*     */       {
/* 175 */         WindowsAsynchronousServerSocketChannelImpl.this.begin();
/*     */         try
/*     */         {
/* 180 */           this.channel.begin();
/*     */ 
/* 182 */           synchronized (this.result) {
/* 183 */             l = WindowsAsynchronousServerSocketChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 185 */             int i = WindowsAsynchronousServerSocketChannelImpl.accept0(WindowsAsynchronousServerSocketChannelImpl.this.handle, this.channel.handle(), l, WindowsAsynchronousServerSocketChannelImpl.this.dataBuffer);
/* 186 */             if (i == -2) {
/* 199 */               this.channel.end();
/*     */               return;
/*     */             }
/* 191 */             finishAccept();
/*     */ 
/* 194 */             enableAccept();
/* 195 */             this.result.setResult(this.channel);
/*     */           }
/*     */         }
/*     */         finally {
/* 199 */           this.channel.end();
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable) {
/* 203 */         if (l != 0L)
/* 204 */           WindowsAsynchronousServerSocketChannelImpl.this.ioCache.remove(l);
/* 205 */         closeChildChannel();
/*     */         Object localObject1;
/* 206 */         if ((localThrowable instanceof ClosedChannelException))
/* 207 */           localObject1 = new AsynchronousCloseException();
/* 208 */         if ((!(localObject1 instanceof IOException)) && (!(localObject1 instanceof SecurityException)))
/* 209 */           localObject1 = new IOException((Throwable)localObject1);
/* 210 */         enableAccept();
/* 211 */         this.result.setFailure((Throwable)localObject1);
/*     */       }
/*     */       finally {
/* 214 */         WindowsAsynchronousServerSocketChannelImpl.this.end();
/*     */       }
/*     */ 
/* 220 */       if (this.result.isCancelled()) {
/* 221 */         closeChildChannel();
/*     */       }
/*     */ 
/* 225 */       Invoker.invokeIndirectly(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/*     */       try
/*     */       {
/* 235 */         if (WindowsAsynchronousServerSocketChannelImpl.this.iocp.isShutdown()) {
/* 236 */           throw new IOException(new ShutdownChannelGroupException());
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 241 */           WindowsAsynchronousServerSocketChannelImpl.this.begin();
/*     */           try {
/* 243 */             this.channel.begin();
/* 244 */             finishAccept();
/*     */           } finally {
/* 246 */             this.channel.end();
/*     */           }
/*     */         } finally {
/* 249 */           WindowsAsynchronousServerSocketChannelImpl.this.end();
/*     */         }
/*     */ 
/* 253 */         enableAccept();
/* 254 */         this.result.setResult(this.channel);
/*     */       } catch (Throwable localThrowable) {
/* 256 */         enableAccept();
/* 257 */         closeChildChannel();
/*     */         Object localObject2;
/* 258 */         if ((localThrowable instanceof ClosedChannelException))
/* 259 */           localObject2 = new AsynchronousCloseException();
/* 260 */         if ((!(localObject2 instanceof IOException)) && (!(localObject2 instanceof SecurityException)))
/* 261 */           localObject2 = new IOException((Throwable)localObject2);
/* 262 */         this.result.setFailure((Throwable)localObject2);
/*     */       }
/*     */ 
/* 267 */       if (this.result.isCancelled()) {
/* 268 */         closeChildChannel();
/*     */       }
/*     */ 
/* 272 */       Invoker.invokeIndirectly(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 277 */       enableAccept();
/* 278 */       closeChildChannel();
/*     */ 
/* 281 */       if (WindowsAsynchronousServerSocketChannelImpl.this.isOpen())
/* 282 */         this.result.setFailure(paramIOException);
/*     */       else {
/* 284 */         this.result.setFailure(new AsynchronousCloseException());
/*     */       }
/* 286 */       Invoker.invokeIndirectly(this.result);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.WindowsAsynchronousServerSocketChannelImpl
 * JD-Core Version:    0.6.2
 */