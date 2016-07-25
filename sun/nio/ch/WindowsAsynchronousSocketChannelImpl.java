/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyConnectedException;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.InterruptedByTimeoutException;
/*     */ import java.nio.channels.ShutdownChannelGroupException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsAsynchronousSocketChannelImpl extends AsynchronousSocketChannelImpl
/*     */   implements Iocp.OverlappedChannel
/*     */ {
/*  46 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*  47 */   private static int addressSize = unsafe.addressSize();
/*     */ 
/*  59 */   private static final int SIZEOF_WSABUF = dependsArch(8, 16);
/*     */   private static final int OFFSETOF_LEN = 0;
/*  61 */   private static final int OFFSETOF_BUF = dependsArch(4, 8);
/*     */   private static final int MAX_WSABUF = 16;
/*  66 */   private static final int SIZEOF_WSABUFARRAY = 16 * SIZEOF_WSABUF;
/*     */   final long handle;
/*     */   private final Iocp iocp;
/*     */   private final int completionKey;
/*     */   private final PendingIoCache ioCache;
/*     */   private final long readBufferArray;
/*     */   private final long writeBufferArray;
/*     */ 
/*     */   private static int dependsArch(int paramInt1, int paramInt2)
/*     */   {
/*  50 */     return addressSize == 4 ? paramInt1 : paramInt2;
/*     */   }
/*     */ 
/*     */   WindowsAsynchronousSocketChannelImpl(Iocp paramIocp, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  92 */     super(paramIocp);
/*     */ 
/*  95 */     long l = IOUtil.fdVal(this.fd);
/*  96 */     int i = 0;
/*     */     try {
/*  98 */       i = paramIocp.associate(this, l);
/*     */     } catch (ShutdownChannelGroupException localShutdownChannelGroupException) {
/* 100 */       if (paramBoolean) {
/* 101 */         closesocket0(l);
/* 102 */         throw localShutdownChannelGroupException;
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 105 */       closesocket0(l);
/* 106 */       throw localIOException;
/*     */     }
/*     */ 
/* 109 */     this.handle = l;
/* 110 */     this.iocp = paramIocp;
/* 111 */     this.completionKey = i;
/* 112 */     this.ioCache = new PendingIoCache();
/*     */ 
/* 115 */     this.readBufferArray = unsafe.allocateMemory(SIZEOF_WSABUFARRAY);
/* 116 */     this.writeBufferArray = unsafe.allocateMemory(SIZEOF_WSABUFARRAY);
/*     */   }
/*     */ 
/*     */   WindowsAsynchronousSocketChannelImpl(Iocp paramIocp) throws IOException {
/* 120 */     this(paramIocp, true);
/*     */   }
/*     */ 
/*     */   public AsynchronousChannelGroupImpl group()
/*     */   {
/* 125 */     return this.iocp;
/*     */   }
/*     */ 
/*     */   public <V, A> PendingFuture<V, A> getByOverlapped(long paramLong)
/*     */   {
/* 133 */     return this.ioCache.remove(paramLong);
/*     */   }
/*     */ 
/*     */   long handle()
/*     */   {
/* 138 */     return this.handle;
/*     */   }
/*     */ 
/*     */   void setConnected(InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2)
/*     */   {
/* 146 */     synchronized (this.stateLock) {
/* 147 */       this.state = 2;
/* 148 */       this.localAddress = paramInetSocketAddress1;
/* 149 */       this.remoteAddress = paramInetSocketAddress2;
/*     */     }
/*     */   }
/*     */ 
/*     */   void implClose()
/*     */     throws IOException
/*     */   {
/* 156 */     closesocket0(this.handle);
/*     */ 
/* 159 */     this.ioCache.close();
/*     */ 
/* 162 */     unsafe.freeMemory(this.readBufferArray);
/* 163 */     unsafe.freeMemory(this.writeBufferArray);
/*     */ 
/* 167 */     if (this.completionKey != 0)
/* 168 */       this.iocp.disassociate(this.completionKey);
/*     */   }
/*     */ 
/*     */   public void onCancel(PendingFuture<?, ?> paramPendingFuture)
/*     */   {
/* 173 */     if ((paramPendingFuture.getContext() instanceof ConnectTask))
/* 174 */       killConnect();
/* 175 */     if ((paramPendingFuture.getContext() instanceof ReadTask))
/* 176 */       killReading();
/* 177 */     if ((paramPendingFuture.getContext() instanceof WriteTask))
/* 178 */       killWriting();
/*     */   }
/*     */ 
/*     */   private void doPrivilegedBind(final SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 308 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Void run() throws IOException {
/* 310 */           WindowsAsynchronousSocketChannelImpl.this.bind(paramSocketAddress);
/* 311 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 315 */       throw ((IOException)localPrivilegedActionException.getException());
/*     */     }
/*     */   }
/*     */ 
/*     */   <A> Future<Void> implConnect(SocketAddress paramSocketAddress, A paramA, CompletionHandler<Void, ? super A> paramCompletionHandler)
/*     */   {
/* 324 */     if (!isOpen()) {
/* 325 */       localObject1 = new ClosedChannelException();
/* 326 */       if (paramCompletionHandler == null)
/* 327 */         return CompletedFuture.withFailure((Throwable)localObject1);
/* 328 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, (Throwable)localObject1);
/* 329 */       return null;
/*     */     }
/*     */ 
/* 332 */     Object localObject1 = Net.checkAddress(paramSocketAddress);
/*     */ 
/* 335 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 336 */     if (localSecurityManager != null) {
/* 337 */       localSecurityManager.checkConnect(((InetSocketAddress)localObject1).getAddress().getHostAddress(), ((InetSocketAddress)localObject1).getPort());
/*     */     }
/*     */ 
/* 341 */     Object localObject2 = null;
/* 342 */     synchronized (this.stateLock) {
/* 343 */       if (this.state == 2)
/* 344 */         throw new AlreadyConnectedException();
/* 345 */       if (this.state == 1)
/* 346 */         throw new ConnectionPendingException();
/* 347 */       if (this.localAddress == null) {
/*     */         try {
/* 349 */           InetSocketAddress localInetSocketAddress = new InetSocketAddress(0);
/* 350 */           if (localSecurityManager == null)
/* 351 */             bind(localInetSocketAddress);
/*     */           else
/* 353 */             doPrivilegedBind(localInetSocketAddress);
/*     */         }
/*     */         catch (IOException localIOException2) {
/* 356 */           localObject2 = localIOException2;
/*     */         }
/*     */       }
/* 359 */       if (localObject2 == null) {
/* 360 */         this.state = 1;
/*     */       }
/*     */     }
/*     */ 
/* 364 */     if (localObject2 != null) {
/*     */       try {
/* 366 */         close(); } catch (IOException localIOException1) {
/*     */       }
/* 368 */       if (paramCompletionHandler == null)
/* 369 */         return CompletedFuture.withFailure(localObject2);
/* 370 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, localObject2);
/* 371 */       return null;
/*     */     }
/*     */ 
/* 375 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */ 
/* 377 */     ConnectTask localConnectTask = new ConnectTask((InetSocketAddress)localObject1, localPendingFuture);
/* 378 */     localPendingFuture.setContext(localConnectTask);
/*     */ 
/* 381 */     if (Iocp.supportsThreadAgnosticIo())
/* 382 */       localConnectTask.run();
/*     */     else {
/* 384 */       Invoker.invokeOnThreadInThreadPool(this, localConnectTask);
/*     */     }
/* 386 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   <V extends Number, A> Future<V> implRead(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler)
/*     */   {
/* 632 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */     ByteBuffer[] arrayOfByteBuffer;
/* 635 */     if (paramBoolean) {
/* 636 */       arrayOfByteBuffer = paramArrayOfByteBuffer;
/*     */     } else {
/* 638 */       arrayOfByteBuffer = new ByteBuffer[1];
/* 639 */       arrayOfByteBuffer[0] = paramByteBuffer;
/*     */     }
/* 641 */     final ReadTask localReadTask = new ReadTask(arrayOfByteBuffer, paramBoolean, localPendingFuture);
/* 642 */     localPendingFuture.setContext(localReadTask);
/*     */ 
/* 645 */     if (paramLong > 0L) {
/* 646 */       Future localFuture = this.iocp.schedule(new Runnable() {
/*     */         public void run() {
/* 648 */           localReadTask.timeout();
/*     */         }
/*     */       }
/*     */       , paramLong, paramTimeUnit);
/*     */ 
/* 651 */       localPendingFuture.setTimeoutTask(localFuture);
/*     */     }
/*     */ 
/* 655 */     if (Iocp.supportsThreadAgnosticIo())
/* 656 */       localReadTask.run();
/*     */     else {
/* 658 */       Invoker.invokeOnThreadInThreadPool(this, localReadTask);
/*     */     }
/* 660 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   <V extends Number, A> Future<V> implWrite(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler)
/*     */   {
/* 889 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */     ByteBuffer[] arrayOfByteBuffer;
/* 892 */     if (paramBoolean) {
/* 893 */       arrayOfByteBuffer = paramArrayOfByteBuffer;
/*     */     } else {
/* 895 */       arrayOfByteBuffer = new ByteBuffer[1];
/* 896 */       arrayOfByteBuffer[0] = paramByteBuffer;
/*     */     }
/* 898 */     final WriteTask localWriteTask = new WriteTask(arrayOfByteBuffer, paramBoolean, localPendingFuture);
/* 899 */     localPendingFuture.setContext(localWriteTask);
/*     */ 
/* 902 */     if (paramLong > 0L) {
/* 903 */       Future localFuture = this.iocp.schedule(new Runnable() {
/*     */         public void run() {
/* 905 */           localWriteTask.timeout();
/*     */         }
/*     */       }
/*     */       , paramLong, paramTimeUnit);
/*     */ 
/* 908 */       localPendingFuture.setTimeoutTask(localFuture);
/*     */     }
/*     */ 
/* 913 */     if (Iocp.supportsThreadAgnosticIo())
/* 914 */       localWriteTask.run();
/*     */     else {
/* 916 */       Invoker.invokeOnThreadInThreadPool(this, localWriteTask);
/*     */     }
/* 918 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native int connect0(long paramLong1, boolean paramBoolean, InetAddress paramInetAddress, int paramInt, long paramLong2)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void updateConnectContext(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int read0(long paramLong1, int paramInt, long paramLong2, long paramLong3)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int write0(long paramLong1, int paramInt, long paramLong2, long paramLong3)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void shutdown0(long paramLong, int paramInt) throws IOException;
/*     */ 
/*     */   private static native void closesocket0(long paramLong) throws IOException;
/*     */ 
/*     */   static
/*     */   {
/* 941 */     Util.load();
/* 942 */     initIDs();
/*     */   }
/*     */ 
/*     */   private class ConnectTask<A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final InetSocketAddress remote;
/*     */     private final PendingFuture<Void, A> result;
/*     */ 
/*     */     ConnectTask(PendingFuture<Void, A> arg2)
/*     */     {
/*     */       Object localObject1;
/* 190 */       this.remote = localObject1;
/*     */       Object localObject2;
/* 191 */       this.result = localObject2;
/*     */     }
/*     */ 
/*     */     private void closeChannel() {
/*     */       try {
/* 196 */         WindowsAsynchronousSocketChannelImpl.this.close(); } catch (IOException localIOException) {
/*     */       }
/*     */     }
/*     */ 
/*     */     private IOException toIOException(Throwable paramThrowable) {
/* 201 */       if ((paramThrowable instanceof IOException)) {
/* 202 */         if ((paramThrowable instanceof ClosedChannelException))
/* 203 */           paramThrowable = new AsynchronousCloseException();
/* 204 */         return (IOException)paramThrowable;
/*     */       }
/* 206 */       return new IOException(paramThrowable);
/*     */     }
/*     */ 
/*     */     private void afterConnect()
/*     */       throws IOException
/*     */     {
/* 213 */       WindowsAsynchronousSocketChannelImpl.updateConnectContext(WindowsAsynchronousSocketChannelImpl.this.handle);
/* 214 */       synchronized (WindowsAsynchronousSocketChannelImpl.this.stateLock) {
/* 215 */         WindowsAsynchronousSocketChannelImpl.this.state = 2;
/* 216 */         WindowsAsynchronousSocketChannelImpl.this.remoteAddress = this.remote;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 225 */       long l = 0L;
/* 226 */       Object localObject1 = null;
/*     */       try {
/* 228 */         WindowsAsynchronousSocketChannelImpl.this.begin();
/*     */ 
/* 232 */         synchronized (this.result) {
/* 233 */           l = WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 235 */           int i = WindowsAsynchronousSocketChannelImpl.connect0(WindowsAsynchronousSocketChannelImpl.this.handle, Net.isIPv6Available(), this.remote.getAddress(), this.remote.getPort(), l);
/*     */ 
/* 237 */           if (i == -2)
/*     */           {
/*     */             return;
/*     */           }
/*     */ 
/* 243 */           afterConnect();
/* 244 */           this.result.setResult(null);
/*     */         }
/*     */       } catch (Throwable localThrowable) {
/* 247 */         if (l != 0L)
/* 248 */           WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(l);
/* 249 */         localObject1 = localThrowable;
/*     */       } finally {
/* 251 */         WindowsAsynchronousSocketChannelImpl.this.end();
/*     */       }
/*     */ 
/* 254 */       if (localObject1 != null) {
/* 255 */         closeChannel();
/* 256 */         this.result.setFailure(toIOException(localObject1));
/*     */       }
/* 258 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 266 */       Object localObject1 = null;
/*     */       try {
/* 268 */         WindowsAsynchronousSocketChannelImpl.this.begin();
/* 269 */         afterConnect();
/* 270 */         this.result.setResult(null);
/*     */       }
/*     */       catch (Throwable localThrowable) {
/* 273 */         localObject1 = localThrowable;
/*     */       } finally {
/* 275 */         WindowsAsynchronousSocketChannelImpl.this.end();
/*     */       }
/*     */ 
/* 279 */       if (localObject1 != null) {
/* 280 */         closeChannel();
/* 281 */         this.result.setFailure(toIOException(localObject1));
/*     */       }
/*     */ 
/* 284 */       if (paramBoolean)
/* 285 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 287 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 296 */       if (WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
/* 297 */         closeChannel();
/* 298 */         this.result.setFailure(paramIOException);
/*     */       } else {
/* 300 */         this.result.setFailure(new AsynchronousCloseException());
/*     */       }
/* 302 */       Invoker.invoke(this.result);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ReadTask<V, A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final ByteBuffer[] bufs;
/*     */     private final int numBufs;
/*     */     private final boolean scatteringRead;
/*     */     private final PendingFuture<V, A> result;
/*     */     private ByteBuffer[] shadow;
/*     */ 
/*     */     ReadTask(boolean paramPendingFuture, PendingFuture<V, A> arg3)
/*     */     {
/* 406 */       this.bufs = paramPendingFuture;
/* 407 */       this.numBufs = (paramPendingFuture.length > 16 ? 16 : paramPendingFuture.length);
/*     */       boolean bool;
/* 408 */       this.scatteringRead = bool;
/*     */       Object localObject;
/* 409 */       this.result = localObject;
/*     */     }
/*     */ 
/*     */     void prepareBuffers()
/*     */     {
/* 417 */       this.shadow = new ByteBuffer[this.numBufs];
/* 418 */       long l1 = WindowsAsynchronousSocketChannelImpl.this.readBufferArray;
/* 419 */       for (int i = 0; i < this.numBufs; i++) {
/* 420 */         ByteBuffer localByteBuffer1 = this.bufs[i];
/* 421 */         int j = localByteBuffer1.position();
/* 422 */         int k = localByteBuffer1.limit();
/* 423 */         assert (j <= k);
/* 424 */         int m = j <= k ? k - j : 0;
/*     */         long l2;
/* 426 */         if (!(localByteBuffer1 instanceof DirectBuffer))
/*     */         {
/* 428 */           ByteBuffer localByteBuffer2 = Util.getTemporaryDirectBuffer(m);
/* 429 */           this.shadow[i] = localByteBuffer2;
/* 430 */           l2 = ((DirectBuffer)localByteBuffer2).address();
/*     */         } else {
/* 432 */           this.shadow[i] = localByteBuffer1;
/* 433 */           l2 = ((DirectBuffer)localByteBuffer1).address() + j;
/*     */         }
/* 435 */         WindowsAsynchronousSocketChannelImpl.unsafe.putAddress(l1 + WindowsAsynchronousSocketChannelImpl.OFFSETOF_BUF, l2);
/* 436 */         WindowsAsynchronousSocketChannelImpl.unsafe.putInt(l1 + 0L, m);
/* 437 */         l1 += WindowsAsynchronousSocketChannelImpl.SIZEOF_WSABUF;
/*     */       }
/*     */     }
/*     */ 
/*     */     void updateBuffers(int paramInt)
/*     */     {
/* 446 */       for (int i = 0; i < this.numBufs; i++) {
/* 447 */         ByteBuffer localByteBuffer = this.shadow[i];
/* 448 */         int j = localByteBuffer.position();
/* 449 */         int k = localByteBuffer.remaining();
/*     */         int m;
/* 450 */         if (paramInt >= k) {
/* 451 */           paramInt -= k;
/* 452 */           m = j + k;
/*     */           try {
/* 454 */             localByteBuffer.position(m);
/*     */           } catch (IllegalArgumentException localIllegalArgumentException1) {
/*     */           }
/*     */         }
/*     */         else {
/* 459 */           if (paramInt <= 0) break;
/* 460 */           assert (j + paramInt < 2147483647L);
/* 461 */           m = j + paramInt;
/*     */           try {
/* 463 */             localByteBuffer.position(m);
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException2) {
/*     */           }
/* 467 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 473 */       for (i = 0; i < this.numBufs; i++)
/* 474 */         if (!(this.bufs[i] instanceof DirectBuffer)) {
/* 475 */           this.shadow[i].flip();
/*     */           try {
/* 477 */             this.bufs[i].put(this.shadow[i]);
/*     */           }
/*     */           catch (BufferOverflowException localBufferOverflowException)
/*     */           {
/*     */           }
/*     */         }
/*     */     }
/*     */ 
/*     */     void releaseBuffers() {
/* 486 */       for (int i = 0; i < this.numBufs; i++)
/* 487 */         if (!(this.bufs[i] instanceof DirectBuffer))
/* 488 */           Util.releaseTemporaryDirectBuffer(this.shadow[i]);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 496 */       long l = 0L;
/* 497 */       int i = 0;
/* 498 */       int j = 0;
/*     */       try
/*     */       {
/* 501 */         WindowsAsynchronousSocketChannelImpl.this.begin();
/*     */ 
/* 504 */         prepareBuffers();
/* 505 */         i = 1;
/*     */ 
/* 508 */         l = WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 511 */         int k = WindowsAsynchronousSocketChannelImpl.read0(WindowsAsynchronousSocketChannelImpl.this.handle, this.numBufs, WindowsAsynchronousSocketChannelImpl.this.readBufferArray, l);
/* 512 */         if (k == -2) {
/* 514 */           j = 1;
/*     */           return;
/*     */         }
/* 517 */         if (k == -1)
/*     */         {
/* 519 */           WindowsAsynchronousSocketChannelImpl.this.enableReading();
/* 520 */           if (this.scatteringRead)
/* 521 */             this.result.setResult(Long.valueOf(-1L));
/*     */           else
/* 523 */             this.result.setResult(Integer.valueOf(-1));
/*     */         }
/*     */         else {
/* 526 */           throw new InternalError("Read completed immediately");
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 531 */         WindowsAsynchronousSocketChannelImpl.this.enableReading();
/*     */         Object localObject1;
/* 532 */         if ((localThrowable instanceof ClosedChannelException))
/* 533 */           localObject1 = new AsynchronousCloseException();
/* 534 */         if (!(localObject1 instanceof IOException))
/* 535 */           localObject1 = new IOException((Throwable)localObject1);
/* 536 */         this.result.setFailure((Throwable)localObject1);
/*     */       }
/*     */       finally {
/* 539 */         if (j == 0) {
/* 540 */           if (l != 0L)
/* 541 */             WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(l);
/* 542 */           if (i != 0)
/* 543 */             releaseBuffers();
/*     */         }
/* 545 */         WindowsAsynchronousSocketChannelImpl.this.end();
/*     */       }
/*     */ 
/* 549 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 558 */       if (paramInt == 0)
/* 559 */         paramInt = -1;
/*     */       else {
/* 561 */         updateBuffers(paramInt);
/*     */       }
/*     */ 
/* 565 */       releaseBuffers();
/*     */ 
/* 568 */       synchronized (this.result) {
/* 569 */         if (this.result.isDone())
/* 570 */           return;
/* 571 */         WindowsAsynchronousSocketChannelImpl.this.enableReading();
/* 572 */         if (this.scatteringRead)
/* 573 */           this.result.setResult(Long.valueOf(paramInt));
/*     */         else {
/* 575 */           this.result.setResult(Integer.valueOf(paramInt));
/*     */         }
/*     */       }
/* 578 */       if (paramBoolean)
/* 579 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 581 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 588 */       releaseBuffers();
/*     */ 
/* 591 */       if (!WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
/* 592 */         paramIOException = new AsynchronousCloseException();
/*     */       }
/* 594 */       synchronized (this.result) {
/* 595 */         if (this.result.isDone())
/* 596 */           return;
/* 597 */         WindowsAsynchronousSocketChannelImpl.this.enableReading();
/* 598 */         this.result.setFailure(paramIOException);
/*     */       }
/* 600 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     void timeout()
/*     */     {
/* 608 */       synchronized (this.result) {
/* 609 */         if (this.result.isDone()) {
/* 610 */           return;
/*     */         }
/*     */ 
/* 613 */         WindowsAsynchronousSocketChannelImpl.this.enableReading(true);
/* 614 */         this.result.setFailure(new InterruptedByTimeoutException());
/*     */       }
/*     */ 
/* 618 */       Invoker.invoke(this.result);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WriteTask<V, A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final ByteBuffer[] bufs;
/*     */     private final int numBufs;
/*     */     private final boolean gatheringWrite;
/*     */     private final PendingFuture<V, A> result;
/*     */     private ByteBuffer[] shadow;
/*     */ 
/*     */     WriteTask(boolean paramPendingFuture, PendingFuture<V, A> arg3)
/*     */     {
/* 680 */       this.bufs = paramPendingFuture;
/* 681 */       this.numBufs = (paramPendingFuture.length > 16 ? 16 : paramPendingFuture.length);
/*     */       boolean bool;
/* 682 */       this.gatheringWrite = bool;
/*     */       Object localObject;
/* 683 */       this.result = localObject;
/*     */     }
/*     */ 
/*     */     void prepareBuffers()
/*     */     {
/* 691 */       this.shadow = new ByteBuffer[this.numBufs];
/* 692 */       long l1 = WindowsAsynchronousSocketChannelImpl.this.writeBufferArray;
/* 693 */       for (int i = 0; i < this.numBufs; i++) {
/* 694 */         ByteBuffer localByteBuffer1 = this.bufs[i];
/* 695 */         int j = localByteBuffer1.position();
/* 696 */         int k = localByteBuffer1.limit();
/* 697 */         assert (j <= k);
/* 698 */         int m = j <= k ? k - j : 0;
/*     */         long l2;
/* 700 */         if (!(localByteBuffer1 instanceof DirectBuffer))
/*     */         {
/* 702 */           ByteBuffer localByteBuffer2 = Util.getTemporaryDirectBuffer(m);
/* 703 */           localByteBuffer2.put(localByteBuffer1);
/* 704 */           localByteBuffer2.flip();
/* 705 */           localByteBuffer1.position(j);
/* 706 */           this.shadow[i] = localByteBuffer2;
/* 707 */           l2 = ((DirectBuffer)localByteBuffer2).address();
/*     */         } else {
/* 709 */           this.shadow[i] = localByteBuffer1;
/* 710 */           l2 = ((DirectBuffer)localByteBuffer1).address() + j;
/*     */         }
/* 712 */         WindowsAsynchronousSocketChannelImpl.unsafe.putAddress(l1 + WindowsAsynchronousSocketChannelImpl.OFFSETOF_BUF, l2);
/* 713 */         WindowsAsynchronousSocketChannelImpl.unsafe.putInt(l1 + 0L, m);
/* 714 */         l1 += WindowsAsynchronousSocketChannelImpl.SIZEOF_WSABUF;
/*     */       }
/*     */     }
/*     */ 
/*     */     void updateBuffers(int paramInt)
/*     */     {
/* 724 */       for (int i = 0; i < this.numBufs; i++) {
/* 725 */         ByteBuffer localByteBuffer = this.bufs[i];
/* 726 */         int j = localByteBuffer.position();
/* 727 */         int k = localByteBuffer.limit();
/* 728 */         int m = j <= k ? k - j : k;
/*     */         int n;
/* 729 */         if (paramInt >= m) {
/* 730 */           paramInt -= m;
/* 731 */           n = j + m;
/*     */           try {
/* 733 */             localByteBuffer.position(n);
/*     */           } catch (IllegalArgumentException localIllegalArgumentException1) {
/*     */           }
/*     */         }
/*     */         else {
/* 738 */           if (paramInt <= 0) break;
/* 739 */           assert (j + paramInt < 2147483647L);
/* 740 */           n = j + paramInt;
/*     */           try {
/* 742 */             localByteBuffer.position(n);
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException2) {
/*     */           }
/* 746 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     void releaseBuffers()
/*     */     {
/* 753 */       for (int i = 0; i < this.numBufs; i++)
/* 754 */         if (!(this.bufs[i] instanceof DirectBuffer))
/* 755 */           Util.releaseTemporaryDirectBuffer(this.shadow[i]);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 763 */       long l = 0L;
/* 764 */       int i = 0;
/* 765 */       int j = 0;
/* 766 */       int k = 0;
/*     */       try
/*     */       {
/* 769 */         WindowsAsynchronousSocketChannelImpl.this.begin();
/*     */ 
/* 772 */         prepareBuffers();
/* 773 */         i = 1;
/*     */ 
/* 776 */         l = WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result);
/* 777 */         int m = WindowsAsynchronousSocketChannelImpl.write0(WindowsAsynchronousSocketChannelImpl.this.handle, this.numBufs, WindowsAsynchronousSocketChannelImpl.this.writeBufferArray, l);
/* 778 */         if (m == -2) {
/* 780 */           j = 1;
/*     */           return;
/*     */         }
/* 783 */         if (m == -1)
/*     */         {
/* 785 */           k = 1;
/* 786 */           throw new ClosedChannelException();
/*     */         }
/*     */ 
/* 789 */         throw new InternalError("Write completed immediately");
/*     */       }
/*     */       catch (Throwable localThrowable) {
/* 792 */         WindowsAsynchronousSocketChannelImpl.this.enableWriting();
/*     */         Object localObject1;
/* 793 */         if ((k == 0) && ((localThrowable instanceof ClosedChannelException)))
/* 794 */           localObject1 = new AsynchronousCloseException();
/* 795 */         if (!(localObject1 instanceof IOException))
/* 796 */           localObject1 = new IOException((Throwable)localObject1);
/* 797 */         this.result.setFailure((Throwable)localObject1);
/*     */       }
/*     */       finally {
/* 800 */         if (j == 0) {
/* 801 */           if (l != 0L)
/* 802 */             WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(l);
/* 803 */           if (i != 0)
/* 804 */             releaseBuffers();
/*     */         }
/* 806 */         WindowsAsynchronousSocketChannelImpl.this.end();
/*     */       }
/*     */ 
/* 810 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 819 */       updateBuffers(paramInt);
/*     */ 
/* 822 */       releaseBuffers();
/*     */ 
/* 825 */       synchronized (this.result) {
/* 826 */         if (this.result.isDone())
/* 827 */           return;
/* 828 */         WindowsAsynchronousSocketChannelImpl.this.enableWriting();
/* 829 */         if (this.gatheringWrite)
/* 830 */           this.result.setResult(Long.valueOf(paramInt));
/*     */         else {
/* 832 */           this.result.setResult(Integer.valueOf(paramInt));
/*     */         }
/*     */       }
/* 835 */       if (paramBoolean)
/* 836 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 838 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 845 */       releaseBuffers();
/*     */ 
/* 848 */       if (!WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
/* 849 */         paramIOException = new AsynchronousCloseException();
/*     */       }
/* 851 */       synchronized (this.result) {
/* 852 */         if (this.result.isDone())
/* 853 */           return;
/* 854 */         WindowsAsynchronousSocketChannelImpl.this.enableWriting();
/* 855 */         this.result.setFailure(paramIOException);
/*     */       }
/* 857 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     void timeout()
/*     */     {
/* 865 */       synchronized (this.result) {
/* 866 */         if (this.result.isDone()) {
/* 867 */           return;
/*     */         }
/*     */ 
/* 870 */         WindowsAsynchronousSocketChannelImpl.this.enableWriting(true);
/* 871 */         this.result.setFailure(new InterruptedByTimeoutException());
/*     */       }
/*     */ 
/* 875 */       Invoker.invoke(this.result);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.WindowsAsynchronousSocketChannelImpl
 * JD-Core Version:    0.6.2
 */