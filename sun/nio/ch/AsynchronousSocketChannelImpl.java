/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketOption;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AlreadyBoundException;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.ConnectionPendingException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.ReadPendingException;
/*     */ import java.nio.channels.WritePendingException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import sun.net.NetHooks;
/*     */ 
/*     */ abstract class AsynchronousSocketChannelImpl extends AsynchronousSocketChannel
/*     */   implements Cancellable, Groupable
/*     */ {
/*     */   protected final FileDescriptor fd;
/*  54 */   protected final Object stateLock = new Object();
/*     */ 
/*  56 */   protected volatile InetSocketAddress localAddress = null;
/*  57 */   protected volatile InetSocketAddress remoteAddress = null;
/*     */   static final int ST_UNINITIALIZED = -1;
/*     */   static final int ST_UNCONNECTED = 0;
/*     */   static final int ST_PENDING = 1;
/*     */   static final int ST_CONNECTED = 2;
/*  64 */   protected volatile int state = -1;
/*     */ 
/*  67 */   private final Object readLock = new Object();
/*     */   private boolean reading;
/*     */   private boolean readShutdown;
/*     */   private boolean readKilled;
/*  73 */   private final Object writeLock = new Object();
/*     */   private boolean writing;
/*     */   private boolean writeShutdown;
/*     */   private boolean writeKilled;
/*  79 */   private final ReadWriteLock closeLock = new ReentrantReadWriteLock();
/*  80 */   private volatile boolean open = true;
/*     */   private boolean isReuseAddress;
/*     */ 
/*     */   AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl)
/*     */     throws IOException
/*     */   {
/*  88 */     super(paramAsynchronousChannelGroupImpl.provider());
/*  89 */     this.fd = Net.socket(true);
/*  90 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl, FileDescriptor paramFileDescriptor, InetSocketAddress paramInetSocketAddress)
/*     */     throws IOException
/*     */   {
/*  99 */     super(paramAsynchronousChannelGroupImpl.provider());
/* 100 */     this.fd = paramFileDescriptor;
/* 101 */     this.state = 2;
/* 102 */     this.localAddress = Net.localAddress(paramFileDescriptor);
/* 103 */     this.remoteAddress = paramInetSocketAddress;
/*     */   }
/*     */ 
/*     */   public final boolean isOpen()
/*     */   {
/* 108 */     return this.open;
/*     */   }
/*     */ 
/*     */   final void begin()
/*     */     throws IOException
/*     */   {
/* 115 */     this.closeLock.readLock().lock();
/* 116 */     if (!isOpen())
/* 117 */       throw new ClosedChannelException();
/*     */   }
/*     */ 
/*     */   final void end()
/*     */   {
/* 124 */     this.closeLock.readLock().unlock();
/*     */   }
/*     */ 
/*     */   abstract void implClose()
/*     */     throws IOException;
/*     */ 
/*     */   public final void close()
/*     */     throws IOException
/*     */   {
/* 135 */     this.closeLock.writeLock().lock();
/*     */     try {
/* 137 */       if (!this.open)
/*     */         return;
/* 139 */       this.open = false;
/*     */     } finally {
/* 141 */       this.closeLock.writeLock().unlock();
/*     */     }
/* 143 */     implClose();
/*     */   }
/*     */ 
/*     */   final void enableReading(boolean paramBoolean) {
/* 147 */     synchronized (this.readLock) {
/* 148 */       this.reading = false;
/* 149 */       if (paramBoolean)
/* 150 */         this.readKilled = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void enableReading() {
/* 155 */     enableReading(false);
/*     */   }
/*     */ 
/*     */   final void enableWriting(boolean paramBoolean) {
/* 159 */     synchronized (this.writeLock) {
/* 160 */       this.writing = false;
/* 161 */       if (paramBoolean)
/* 162 */         this.writeKilled = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void enableWriting() {
/* 167 */     enableWriting(false);
/*     */   }
/*     */ 
/*     */   final void killReading() {
/* 171 */     synchronized (this.readLock) {
/* 172 */       this.readKilled = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void killWriting() {
/* 177 */     synchronized (this.writeLock) {
/* 178 */       this.writeKilled = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void killConnect()
/*     */   {
/* 185 */     killReading();
/* 186 */     killWriting();
/*     */   }
/*     */ 
/*     */   abstract <A> Future<Void> implConnect(SocketAddress paramSocketAddress, A paramA, CompletionHandler<Void, ? super A> paramCompletionHandler);
/*     */ 
/*     */   public final Future<Void> connect(SocketAddress paramSocketAddress)
/*     */   {
/* 198 */     return implConnect(paramSocketAddress, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void connect(SocketAddress paramSocketAddress, A paramA, CompletionHandler<Void, ? super A> paramCompletionHandler)
/*     */   {
/* 206 */     if (paramCompletionHandler == null)
/* 207 */       throw new NullPointerException("'handler' is null");
/* 208 */     implConnect(paramSocketAddress, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   abstract <V extends Number, A> Future<V> implRead(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler);
/*     */ 
/*     */   private <V extends Number, A> Future<V> read(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler)
/*     */   {
/* 231 */     if (!isOpen()) {
/* 232 */       ClosedChannelException localClosedChannelException = new ClosedChannelException();
/* 233 */       if (paramCompletionHandler == null)
/* 234 */         return CompletedFuture.withFailure(localClosedChannelException);
/* 235 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, localClosedChannelException);
/* 236 */       return null;
/*     */     }
/*     */ 
/* 239 */     if (this.remoteAddress == null) {
/* 240 */       throw new NotYetConnectedException();
/*     */     }
/* 242 */     int i = (paramBoolean) || (paramByteBuffer.hasRemaining()) ? 1 : 0;
/* 243 */     int j = 0;
/*     */ 
/* 246 */     synchronized (this.readLock) {
/* 247 */       if (this.readKilled)
/* 248 */         throw new IllegalStateException("Reading not allowed due to timeout or cancellation");
/* 249 */       if (this.reading)
/* 250 */         throw new ReadPendingException();
/* 251 */       if (this.readShutdown) {
/* 252 */         j = 1;
/*     */       }
/* 254 */       else if (i != 0) {
/* 255 */         this.reading = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 262 */     if ((j != 0) || (i == 0))
/*     */     {
/* 264 */       if (paramBoolean)
/* 265 */         ??? = j != 0 ? Long.valueOf(-1L) : Long.valueOf(0L);
/*     */       else {
/* 267 */         ??? = Integer.valueOf(j != 0 ? -1 : 0);
/*     */       }
/* 269 */       if (paramCompletionHandler == null)
/* 270 */         return CompletedFuture.withResult(???);
/* 271 */       Invoker.invoke(this, paramCompletionHandler, paramA, ???, null);
/* 272 */       return null;
/*     */     }
/*     */ 
/* 275 */     return implRead(paramBoolean, paramByteBuffer, paramArrayOfByteBuffer, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public final Future<Integer> read(ByteBuffer paramByteBuffer)
/*     */   {
/* 280 */     if (paramByteBuffer.isReadOnly())
/* 281 */       throw new IllegalArgumentException("Read-only buffer");
/* 282 */     return read(false, paramByteBuffer, null, 0L, TimeUnit.MILLISECONDS, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void read(ByteBuffer paramByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 292 */     if (paramCompletionHandler == null)
/* 293 */       throw new NullPointerException("'handler' is null");
/* 294 */     if (paramByteBuffer.isReadOnly())
/* 295 */       throw new IllegalArgumentException("Read-only buffer");
/* 296 */     read(false, paramByteBuffer, null, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public final <A> void read(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<Long, ? super A> paramCompletionHandler)
/*     */   {
/* 308 */     if (paramCompletionHandler == null)
/* 309 */       throw new NullPointerException("'handler' is null");
/* 310 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/* 311 */       throw new IndexOutOfBoundsException();
/* 312 */     ByteBuffer[] arrayOfByteBuffer = Util.subsequence(paramArrayOfByteBuffer, paramInt1, paramInt2);
/* 313 */     for (int i = 0; i < arrayOfByteBuffer.length; i++) {
/* 314 */       if (arrayOfByteBuffer[i].isReadOnly())
/* 315 */         throw new IllegalArgumentException("Read-only buffer");
/*     */     }
/* 317 */     read(true, null, arrayOfByteBuffer, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   abstract <V extends Number, A> Future<V> implWrite(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler);
/*     */ 
/*     */   private <V extends Number, A> Future<V> write(boolean paramBoolean, ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<V, ? super A> paramCompletionHandler)
/*     */   {
/* 340 */     int i = (paramBoolean) || (paramByteBuffer.hasRemaining()) ? 1 : 0;
/*     */ 
/* 342 */     int j = 0;
/* 343 */     if (isOpen()) {
/* 344 */       if (this.remoteAddress == null) {
/* 345 */         throw new NotYetConnectedException();
/*     */       }
/* 347 */       synchronized (this.writeLock) {
/* 348 */         if (this.writeKilled)
/* 349 */           throw new IllegalStateException("Writing not allowed due to timeout or cancellation");
/* 350 */         if (this.writing)
/* 351 */           throw new WritePendingException();
/* 352 */         if (this.writeShutdown) {
/* 353 */           j = 1;
/*     */         }
/* 355 */         else if (i != 0)
/* 356 */           this.writing = true;
/*     */       }
/*     */     }
/*     */     else {
/* 360 */       j = 1;
/*     */     }
/*     */ 
/* 364 */     if (j != 0) {
/* 365 */       ??? = new ClosedChannelException();
/* 366 */       if (paramCompletionHandler == null)
/* 367 */         return CompletedFuture.withFailure((Throwable)???);
/* 368 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, (Throwable)???);
/* 369 */       return null;
/*     */     }
/*     */ 
/* 373 */     if (i == 0) {
/* 374 */       ??? = paramBoolean ? Long.valueOf(0L) : Integer.valueOf(0);
/* 375 */       if (paramCompletionHandler == null)
/* 376 */         return CompletedFuture.withResult(???);
/* 377 */       Invoker.invoke(this, paramCompletionHandler, paramA, ???, null);
/* 378 */       return null;
/*     */     }
/*     */ 
/* 381 */     return implWrite(paramBoolean, paramByteBuffer, paramArrayOfByteBuffer, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public final Future<Integer> write(ByteBuffer paramByteBuffer)
/*     */   {
/* 386 */     return write(false, paramByteBuffer, null, 0L, TimeUnit.MILLISECONDS, null, null);
/*     */   }
/*     */ 
/*     */   public final <A> void write(ByteBuffer paramByteBuffer, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 396 */     if (paramCompletionHandler == null)
/* 397 */       throw new NullPointerException("'handler' is null");
/* 398 */     write(false, paramByteBuffer, null, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public final <A> void write(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, A paramA, CompletionHandler<Long, ? super A> paramCompletionHandler)
/*     */   {
/* 410 */     if (paramCompletionHandler == null)
/* 411 */       throw new NullPointerException("'handler' is null");
/* 412 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByteBuffer.length - paramInt2))
/* 413 */       throw new IndexOutOfBoundsException();
/* 414 */     paramArrayOfByteBuffer = Util.subsequence(paramArrayOfByteBuffer, paramInt1, paramInt2);
/* 415 */     write(true, null, paramArrayOfByteBuffer, paramLong, paramTimeUnit, paramA, paramCompletionHandler);
/*     */   }
/*     */ 
/*     */   public final AsynchronousSocketChannel bind(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 423 */       begin();
/* 424 */       synchronized (this.stateLock) {
/* 425 */         if (this.state == 1)
/* 426 */           throw new ConnectionPendingException();
/* 427 */         if (this.localAddress != null)
/* 428 */           throw new AlreadyBoundException();
/* 429 */         InetSocketAddress localInetSocketAddress = paramSocketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(paramSocketAddress);
/*     */ 
/* 431 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 432 */         if (localSecurityManager != null) {
/* 433 */           localSecurityManager.checkListen(localInetSocketAddress.getPort());
/*     */         }
/* 435 */         NetHooks.beforeTcpBind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 436 */         Net.bind(this.fd, localInetSocketAddress.getAddress(), localInetSocketAddress.getPort());
/* 437 */         this.localAddress = Net.localAddress(this.fd);
/*     */       }
/*     */     } finally {
/* 440 */       end();
/*     */     }
/* 442 */     return this;
/*     */   }
/*     */ 
/*     */   public final SocketAddress getLocalAddress() throws IOException
/*     */   {
/* 447 */     if (!isOpen())
/* 448 */       throw new ClosedChannelException();
/* 449 */     return Net.getRevealedLocalAddress(this.localAddress);
/*     */   }
/*     */ 
/*     */   public final <T> AsynchronousSocketChannel setOption(SocketOption<T> paramSocketOption, T paramT)
/*     */     throws IOException
/*     */   {
/* 456 */     if (paramSocketOption == null)
/* 457 */       throw new NullPointerException();
/* 458 */     if (!supportedOptions().contains(paramSocketOption))
/* 459 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*     */     try
/*     */     {
/* 462 */       begin();
/* 463 */       if (this.writeShutdown)
/* 464 */         throw new IOException("Connection has been shutdown for writing");
/* 465 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 469 */         this.isReuseAddress = ((Boolean)paramT).booleanValue();
/*     */       }
/* 471 */       else Net.setSocketOption(this.fd, Net.UNSPEC, paramSocketOption, paramT);
/*     */ 
/* 473 */       return this;
/*     */     } finally {
/* 475 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final <T> T getOption(SocketOption<T> paramSocketOption)
/*     */     throws IOException
/*     */   {
/* 482 */     if (paramSocketOption == null)
/* 483 */       throw new NullPointerException();
/* 484 */     if (!supportedOptions().contains(paramSocketOption))
/* 485 */       throw new UnsupportedOperationException("'" + paramSocketOption + "' not supported");
/*     */     try
/*     */     {
/* 488 */       begin();
/*     */       Object localObject1;
/* 489 */       if ((paramSocketOption == StandardSocketOptions.SO_REUSEADDR) && (Net.useExclusiveBind()))
/*     */       {
/* 493 */         return Boolean.valueOf(this.isReuseAddress);
/*     */       }
/* 495 */       return Net.getSocketOption(this.fd, Net.UNSPEC, paramSocketOption);
/*     */     } finally {
/* 497 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Set<SocketOption<?>> supportedOptions()
/*     */   {
/* 517 */     return DefaultOptionsHolder.defaultOptions;
/*     */   }
/*     */ 
/*     */   public final SocketAddress getRemoteAddress() throws IOException
/*     */   {
/* 522 */     if (!isOpen())
/* 523 */       throw new ClosedChannelException();
/* 524 */     return this.remoteAddress;
/*     */   }
/*     */ 
/*     */   public final AsynchronousSocketChannel shutdownInput() throws IOException
/*     */   {
/*     */     try {
/* 530 */       begin();
/* 531 */       if (this.remoteAddress == null)
/* 532 */         throw new NotYetConnectedException();
/* 533 */       synchronized (this.readLock) {
/* 534 */         if (!this.readShutdown) {
/* 535 */           Net.shutdown(this.fd, 0);
/* 536 */           this.readShutdown = true;
/*     */         }
/*     */       }
/*     */     } finally {
/* 540 */       end();
/*     */     }
/* 542 */     return this;
/*     */   }
/*     */ 
/*     */   public final AsynchronousSocketChannel shutdownOutput() throws IOException
/*     */   {
/*     */     try {
/* 548 */       begin();
/* 549 */       if (this.remoteAddress == null)
/* 550 */         throw new NotYetConnectedException();
/* 551 */       synchronized (this.writeLock) {
/* 552 */         if (!this.writeShutdown) {
/* 553 */           Net.shutdown(this.fd, 1);
/* 554 */           this.writeShutdown = true;
/*     */         }
/*     */       }
/*     */     } finally {
/* 558 */       end();
/*     */     }
/* 560 */     return this;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 565 */     StringBuilder localStringBuilder = new StringBuilder();
/* 566 */     localStringBuilder.append(getClass().getName());
/* 567 */     localStringBuilder.append('[');
/* 568 */     synchronized (this.stateLock) {
/* 569 */       if (!isOpen()) {
/* 570 */         localStringBuilder.append("closed");
/*     */       } else {
/* 572 */         switch (this.state) {
/*     */         case 0:
/* 574 */           localStringBuilder.append("unconnected");
/* 575 */           break;
/*     */         case 1:
/* 577 */           localStringBuilder.append("connection-pending");
/* 578 */           break;
/*     */         case 2:
/* 580 */           localStringBuilder.append("connected");
/* 581 */           if (this.readShutdown)
/* 582 */             localStringBuilder.append(" ishut");
/* 583 */           if (this.writeShutdown)
/* 584 */             localStringBuilder.append(" oshut");
/*     */           break;
/*     */         }
/* 587 */         if (this.localAddress != null) {
/* 588 */           localStringBuilder.append(" local=");
/* 589 */           localStringBuilder.append(Net.getRevealedLocalAddressAsString(this.localAddress));
/*     */         }
/*     */ 
/* 592 */         if (this.remoteAddress != null) {
/* 593 */           localStringBuilder.append(" remote=");
/* 594 */           localStringBuilder.append(this.remoteAddress.toString());
/*     */         }
/*     */       }
/*     */     }
/* 598 */     localStringBuilder.append(']');
/* 599 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static class DefaultOptionsHolder
/*     */   {
/* 502 */     static final Set<SocketOption<?>> defaultOptions = defaultOptions();
/*     */ 
/*     */     private static Set<SocketOption<?>> defaultOptions() {
/* 505 */       HashSet localHashSet = new HashSet(5);
/* 506 */       localHashSet.add(StandardSocketOptions.SO_SNDBUF);
/* 507 */       localHashSet.add(StandardSocketOptions.SO_RCVBUF);
/* 508 */       localHashSet.add(StandardSocketOptions.SO_KEEPALIVE);
/* 509 */       localHashSet.add(StandardSocketOptions.SO_REUSEADDR);
/* 510 */       localHashSet.add(StandardSocketOptions.TCP_NODELAY);
/* 511 */       return Collections.unmodifiableSet(localHashSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.AsynchronousSocketChannelImpl
 * JD-Core Version:    0.6.2
 */