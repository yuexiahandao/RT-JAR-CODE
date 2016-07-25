/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.nio.channels.AsynchronousFileChannel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.channels.NonReadableChannelException;
/*     */ import java.nio.channels.NonWritableChannelException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class WindowsAsynchronousFileChannelImpl extends AsynchronousFileChannelImpl
/*     */   implements Iocp.OverlappedChannel, Groupable
/*     */ {
/*     */   private static final JavaIOFileDescriptorAccess fdAccess;
/*     */   private static final int ERROR_HANDLE_EOF = 38;
/*     */   private static final FileDispatcher nd;
/*     */   private final long handle;
/*     */   private final int completionKey;
/*     */   private final Iocp iocp;
/*     */   private final boolean isDefaultIocp;
/*     */   private final PendingIoCache ioCache;
/*     */   static final int NO_LOCK = -1;
/*     */   static final int LOCKED = 0;
/*     */ 
/*     */   private WindowsAsynchronousFileChannelImpl(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, Iocp paramIocp, boolean paramBoolean3)
/*     */     throws IOException
/*     */   {
/*  90 */     super(paramFileDescriptor, paramBoolean1, paramBoolean2, paramIocp.executor());
/*  91 */     this.handle = fdAccess.getHandle(paramFileDescriptor);
/*  92 */     this.iocp = paramIocp;
/*  93 */     this.isDefaultIocp = paramBoolean3;
/*  94 */     this.ioCache = new PendingIoCache();
/*  95 */     this.completionKey = paramIocp.associate(this, this.handle);
/*     */   }
/*     */ 
/*     */   public static AsynchronousFileChannel open(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2, ThreadPool paramThreadPool)
/*     */     throws IOException
/*     */   {
/*     */     Iocp localIocp;
/*     */     boolean bool;
/* 106 */     if (paramThreadPool == null) {
/* 107 */       localIocp = DefaultIocpHolder.defaultIocp;
/* 108 */       bool = true;
/*     */     } else {
/* 110 */       localIocp = new Iocp(null, paramThreadPool).start();
/* 111 */       bool = false;
/*     */     }
/*     */     try {
/* 114 */       return new WindowsAsynchronousFileChannelImpl(paramFileDescriptor, paramBoolean1, paramBoolean2, localIocp, bool);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 118 */       if (!bool)
/* 119 */         localIocp.implClose();
/* 120 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public <V, A> PendingFuture<V, A> getByOverlapped(long paramLong)
/*     */   {
/* 126 */     return this.ioCache.remove(paramLong);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 131 */     this.closeLock.writeLock().lock();
/*     */     try {
/* 133 */       if (this.closed)
/*     */         return;
/* 135 */       this.closed = true;
/*     */     } finally {
/* 137 */       this.closeLock.writeLock().unlock();
/*     */     }
/*     */ 
/* 141 */     invalidateAllLocks();
/*     */ 
/* 144 */     close0(this.handle);
/*     */ 
/* 147 */     this.ioCache.close();
/*     */ 
/* 150 */     this.iocp.disassociate(this.completionKey);
/*     */ 
/* 153 */     if (!this.isDefaultIocp)
/* 154 */       this.iocp.detachFromThreadPool();
/*     */   }
/*     */ 
/*     */   public AsynchronousChannelGroupImpl group()
/*     */   {
/* 159 */     return this.iocp;
/*     */   }
/*     */ 
/*     */   private static IOException toIOException(Throwable paramThrowable)
/*     */   {
/* 166 */     if ((paramThrowable instanceof IOException)) {
/* 167 */       if ((paramThrowable instanceof ClosedChannelException))
/* 168 */         paramThrowable = new AsynchronousCloseException();
/* 169 */       return (IOException)paramThrowable;
/*     */     }
/* 171 */     return new IOException(paramThrowable);
/*     */   }
/*     */ 
/*     */   public long size() throws IOException
/*     */   {
/*     */     try {
/* 177 */       begin();
/* 178 */       return nd.size(this.fdObj);
/*     */     } finally {
/* 180 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AsynchronousFileChannel truncate(long paramLong) throws IOException
/*     */   {
/* 186 */     if (paramLong < 0L)
/* 187 */       throw new IllegalArgumentException("Negative size");
/* 188 */     if (!this.writing)
/* 189 */       throw new NonWritableChannelException();
/*     */     try {
/* 191 */       begin();
/* 192 */       if (paramLong > nd.size(this.fdObj))
/* 193 */         return this;
/* 194 */       nd.truncate(this.fdObj, paramLong);
/*     */     } finally {
/* 196 */       end();
/*     */     }
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */   public void force(boolean paramBoolean) throws IOException
/*     */   {
/*     */     try {
/* 204 */       begin();
/* 205 */       nd.force(this.fdObj, paramBoolean);
/*     */     } finally {
/* 207 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   <A> Future<FileLock> implLock(long paramLong1, long paramLong2, boolean paramBoolean, A paramA, CompletionHandler<FileLock, ? super A> paramCompletionHandler)
/*     */   {
/* 301 */     if ((paramBoolean) && (!this.reading))
/* 302 */       throw new NonReadableChannelException();
/* 303 */     if ((!paramBoolean) && (!this.writing)) {
/* 304 */       throw new NonWritableChannelException();
/*     */     }
/*     */ 
/* 307 */     FileLockImpl localFileLockImpl = addToFileLockTable(paramLong1, paramLong2, paramBoolean);
/* 308 */     if (localFileLockImpl == null) {
/* 309 */       localObject1 = new ClosedChannelException();
/* 310 */       if (paramCompletionHandler == null)
/* 311 */         return CompletedFuture.withFailure((Throwable)localObject1);
/* 312 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, (Throwable)localObject1);
/* 313 */       return null;
/*     */     }
/*     */ 
/* 317 */     Object localObject1 = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */ 
/* 319 */     LockTask localLockTask = new LockTask(paramLong1, localFileLockImpl, (PendingFuture)localObject1);
/* 320 */     ((PendingFuture)localObject1).setContext(localLockTask);
/*     */ 
/* 323 */     if (Iocp.supportsThreadAgnosticIo()) {
/* 324 */       localLockTask.run();
/*     */     } else {
/* 326 */       int i = 0;
/*     */       try {
/* 328 */         Invoker.invokeOnThreadInThreadPool(this, localLockTask);
/* 329 */         i = 1;
/*     */       } finally {
/* 331 */         if (i == 0)
/*     */         {
/* 333 */           removeFromFileLockTable(localFileLockImpl);
/*     */         }
/*     */       }
/*     */     }
/* 337 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public FileLock tryLock(long paramLong1, long paramLong2, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 347 */     if ((paramBoolean) && (!this.reading))
/* 348 */       throw new NonReadableChannelException();
/* 349 */     if ((!paramBoolean) && (!this.writing)) {
/* 350 */       throw new NonWritableChannelException();
/*     */     }
/*     */ 
/* 353 */     FileLockImpl localFileLockImpl = addToFileLockTable(paramLong1, paramLong2, paramBoolean);
/* 354 */     if (localFileLockImpl == null) {
/* 355 */       throw new ClosedChannelException();
/*     */     }
/* 357 */     int i = 0;
/*     */     try {
/* 359 */       begin();
/*     */ 
/* 361 */       int j = nd.lock(this.fdObj, false, paramLong1, paramLong2, paramBoolean);
/*     */       Object localObject1;
/* 362 */       if (j == -1)
/* 363 */         return null;
/* 364 */       i = 1;
/* 365 */       return localFileLockImpl;
/*     */     } finally {
/* 367 */       if (i == 0)
/* 368 */         removeFromFileLockTable(localFileLockImpl);
/* 369 */       end();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void implRelease(FileLockImpl paramFileLockImpl) throws IOException
/*     */   {
/* 375 */     nd.release(this.fdObj, paramFileLockImpl.position(), paramFileLockImpl.size());
/*     */   }
/*     */ 
/*     */   <A> Future<Integer> implRead(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 525 */     if (!this.reading)
/* 526 */       throw new NonReadableChannelException();
/* 527 */     if (paramLong < 0L)
/* 528 */       throw new IllegalArgumentException("Negative position");
/* 529 */     if (paramByteBuffer.isReadOnly()) {
/* 530 */       throw new IllegalArgumentException("Read-only buffer");
/*     */     }
/*     */ 
/* 533 */     if (!isOpen()) {
/* 534 */       ClosedChannelException localClosedChannelException = new ClosedChannelException();
/* 535 */       if (paramCompletionHandler == null)
/* 536 */         return CompletedFuture.withFailure(localClosedChannelException);
/* 537 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, localClosedChannelException);
/* 538 */       return null;
/*     */     }
/*     */ 
/* 541 */     int i = paramByteBuffer.position();
/* 542 */     int j = paramByteBuffer.limit();
/* 543 */     assert (i <= j);
/* 544 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 547 */     if (k == 0) {
/* 548 */       if (paramCompletionHandler == null)
/* 549 */         return CompletedFuture.withResult(Integer.valueOf(0));
/* 550 */       Invoker.invoke(this, paramCompletionHandler, paramA, Integer.valueOf(0), null);
/* 551 */       return null;
/*     */     }
/*     */ 
/* 555 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */ 
/* 557 */     ReadTask localReadTask = new ReadTask(paramByteBuffer, i, k, paramLong, localPendingFuture);
/* 558 */     localPendingFuture.setContext(localReadTask);
/*     */ 
/* 561 */     if (Iocp.supportsThreadAgnosticIo())
/* 562 */       localReadTask.run();
/*     */     else {
/* 564 */       Invoker.invokeOnThreadInThreadPool(this, localReadTask);
/*     */     }
/* 566 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   <A> Future<Integer> implWrite(ByteBuffer paramByteBuffer, long paramLong, A paramA, CompletionHandler<Integer, ? super A> paramCompletionHandler)
/*     */   {
/* 701 */     if (!this.writing)
/* 702 */       throw new NonWritableChannelException();
/* 703 */     if (paramLong < 0L) {
/* 704 */       throw new IllegalArgumentException("Negative position");
/*     */     }
/*     */ 
/* 707 */     if (!isOpen()) {
/* 708 */       ClosedChannelException localClosedChannelException = new ClosedChannelException();
/* 709 */       if (paramCompletionHandler == null)
/* 710 */         return CompletedFuture.withFailure(localClosedChannelException);
/* 711 */       Invoker.invoke(this, paramCompletionHandler, paramA, null, localClosedChannelException);
/* 712 */       return null;
/*     */     }
/*     */ 
/* 715 */     int i = paramByteBuffer.position();
/* 716 */     int j = paramByteBuffer.limit();
/* 717 */     assert (i <= j);
/* 718 */     int k = i <= j ? j - i : 0;
/*     */ 
/* 721 */     if (k == 0) {
/* 722 */       if (paramCompletionHandler == null)
/* 723 */         return CompletedFuture.withResult(Integer.valueOf(0));
/* 724 */       Invoker.invoke(this, paramCompletionHandler, paramA, Integer.valueOf(0), null);
/* 725 */       return null;
/*     */     }
/*     */ 
/* 729 */     PendingFuture localPendingFuture = new PendingFuture(this, paramCompletionHandler, paramA);
/*     */ 
/* 731 */     WriteTask localWriteTask = new WriteTask(paramByteBuffer, i, k, paramLong, localPendingFuture);
/* 732 */     localPendingFuture.setContext(localWriteTask);
/*     */ 
/* 735 */     if (Iocp.supportsThreadAgnosticIo())
/* 736 */       localWriteTask.run();
/*     */     else {
/* 738 */       Invoker.invokeOnThreadInThreadPool(this, localWriteTask);
/*     */     }
/* 740 */     return localPendingFuture;
/*     */   }
/*     */ 
/*     */   private static native int readFile(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int writeFile(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4)
/*     */     throws IOException;
/*     */ 
/*     */   private static native int lockFile(long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean, long paramLong4)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void close0(long paramLong);
/*     */ 
/*     */   static
/*     */   {
/*  45 */     fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */ 
/*  66 */     nd = new FileDispatcherImpl();
/*     */ 
/* 757 */     Util.load();
/*     */   }
/*     */ 
/*     */   private static class DefaultIocpHolder
/*     */   {
/*  53 */     static final Iocp defaultIocp = defaultIocp();
/*     */ 
/*     */     private static Iocp defaultIocp() {
/*     */       try { return new Iocp(null, ThreadPool.createDefault()).start();
/*     */       } catch (IOException localIOException) {
/*  58 */         InternalError localInternalError = new InternalError();
/*  59 */         localInternalError.initCause(localIOException);
/*  60 */         throw localInternalError;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class LockTask<A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final long position;
/*     */     private final FileLockImpl fli;
/*     */     private final PendingFuture<FileLock, A> result;
/*     */ 
/*     */     LockTask(FileLockImpl arg3, PendingFuture<FileLock, A> arg4)
/*     */     {
/* 225 */       this.position = paramPendingFuture;
/*     */       Object localObject1;
/* 226 */       this.fli = localObject1;
/*     */       Object localObject2;
/* 227 */       this.result = localObject2;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 232 */       long l = 0L;
/* 233 */       int i = 0;
/*     */       try {
/* 235 */         WindowsAsynchronousFileChannelImpl.this.begin();
/*     */ 
/* 238 */         l = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 242 */         synchronized (this.result) {
/* 243 */           int j = WindowsAsynchronousFileChannelImpl.lockFile(WindowsAsynchronousFileChannelImpl.this.handle, this.position, this.fli.size(), this.fli.isShared(), l);
/*     */ 
/* 245 */           if (j == -2)
/*     */           {
/* 247 */             i = 1;
/*     */ 
/* 259 */             if ((i == 0) && (l != 0L))
/* 260 */               WindowsAsynchronousFileChannelImpl.this.ioCache.remove(l);
/* 261 */             WindowsAsynchronousFileChannelImpl.this.end();
/*     */             return;
/*     */           }
/* 251 */           this.result.setResult(this.fli);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 256 */         WindowsAsynchronousFileChannelImpl.this.removeFromFileLockTable(this.fli);
/* 257 */         this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(localThrowable));
/*     */       } finally {
/* 259 */         if ((i == 0) && (l != 0L))
/* 260 */           WindowsAsynchronousFileChannelImpl.this.ioCache.remove(l);
/* 261 */         WindowsAsynchronousFileChannelImpl.this.end();
/*     */       }
/*     */ 
/* 265 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 271 */       this.result.setResult(this.fli);
/* 272 */       if (paramBoolean)
/* 273 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 275 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 282 */       WindowsAsynchronousFileChannelImpl.this.removeFromFileLockTable(this.fli);
/*     */ 
/* 285 */       if (WindowsAsynchronousFileChannelImpl.this.isOpen())
/* 286 */         this.result.setFailure(paramIOException);
/*     */       else {
/* 288 */         this.result.setFailure(new AsynchronousCloseException());
/*     */       }
/* 290 */       Invoker.invoke(this.result);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ReadTask<A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final ByteBuffer dst;
/*     */     private final int pos;
/*     */     private final int rem;
/*     */     private final long position;
/*     */     private final PendingFuture<Integer, A> result;
/*     */     private volatile ByteBuffer buf;
/*     */ 
/*     */     ReadTask(int paramInt1, int paramLong, long arg4, PendingFuture<Integer, A> arg6)
/*     */     {
/* 396 */       this.dst = paramInt1;
/* 397 */       this.pos = paramLong;
/* 398 */       this.rem = ???;
/* 399 */       this.position = paramPendingFuture;
/*     */       Object localObject;
/* 400 */       this.result = localObject;
/*     */     }
/*     */ 
/*     */     void releaseBufferIfSubstituted() {
/* 404 */       if (this.buf != this.dst)
/* 405 */         Util.releaseTemporaryDirectBuffer(this.buf);
/*     */     }
/*     */ 
/*     */     void updatePosition(int paramInt)
/*     */     {
/* 410 */       if (paramInt > 0)
/* 411 */         if (this.buf == this.dst) {
/*     */           try {
/* 413 */             this.dst.position(this.pos + paramInt);
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException) {
/*     */           }
/*     */         }
/*     */         else {
/* 419 */           this.buf.position(paramInt).flip();
/*     */           try {
/* 421 */             this.dst.put(this.buf);
/*     */           }
/*     */           catch (BufferOverflowException localBufferOverflowException)
/*     */           {
/*     */           }
/*     */         }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 431 */       int i = -1;
/* 432 */       long l1 = 0L;
/*     */       long l2;
/* 436 */       if ((this.dst instanceof DirectBuffer)) {
/* 437 */         this.buf = this.dst;
/* 438 */         l2 = ((DirectBuffer)this.dst).address() + this.pos;
/*     */       } else {
/* 440 */         this.buf = Util.getTemporaryDirectBuffer(this.rem);
/* 441 */         l2 = ((DirectBuffer)this.buf).address();
/*     */       }
/*     */ 
/* 444 */       int j = 0;
/*     */       try {
/* 446 */         WindowsAsynchronousFileChannelImpl.this.begin();
/*     */ 
/* 449 */         l1 = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 452 */         i = WindowsAsynchronousFileChannelImpl.readFile(WindowsAsynchronousFileChannelImpl.this.handle, l2, this.rem, this.position, l1);
/* 453 */         if (i == -2) {
/* 455 */           j = 1;
/*     */           return;
/* 457 */         }if (i == -1)
/* 458 */           this.result.setResult(Integer.valueOf(i));
/*     */         else {
/* 460 */           throw new InternalError("Unexpected result: " + i);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 465 */         this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(localThrowable));
/*     */       } finally {
/* 467 */         if (j == 0)
/*     */         {
/* 469 */           if (l1 != 0L)
/* 470 */             WindowsAsynchronousFileChannelImpl.this.ioCache.remove(l1);
/* 471 */           releaseBufferIfSubstituted();
/*     */         }
/* 473 */         WindowsAsynchronousFileChannelImpl.this.end();
/*     */       }
/*     */ 
/* 477 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 485 */       updatePosition(paramInt);
/*     */ 
/* 488 */       releaseBufferIfSubstituted();
/*     */ 
/* 491 */       this.result.setResult(Integer.valueOf(paramInt));
/* 492 */       if (paramBoolean)
/* 493 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 495 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 502 */       if (paramInt == 38) {
/* 503 */         completed(-1, false);
/*     */       }
/*     */       else {
/* 506 */         releaseBufferIfSubstituted();
/*     */ 
/* 509 */         if (WindowsAsynchronousFileChannelImpl.this.isOpen())
/* 510 */           this.result.setFailure(paramIOException);
/*     */         else {
/* 512 */           this.result.setFailure(new AsynchronousCloseException());
/*     */         }
/* 514 */         Invoker.invoke(this.result);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WriteTask<A>
/*     */     implements Runnable, Iocp.ResultHandler
/*     */   {
/*     */     private final ByteBuffer src;
/*     */     private final int pos;
/*     */     private final int rem;
/*     */     private final long position;
/*     */     private final PendingFuture<Integer, A> result;
/*     */     private volatile ByteBuffer buf;
/*     */ 
/*     */     WriteTask(int paramInt1, int paramLong, long arg4, PendingFuture<Integer, A> arg6)
/*     */     {
/* 587 */       this.src = paramInt1;
/* 588 */       this.pos = paramLong;
/* 589 */       this.rem = ???;
/* 590 */       this.position = paramPendingFuture;
/*     */       Object localObject;
/* 591 */       this.result = localObject;
/*     */     }
/*     */ 
/*     */     void releaseBufferIfSubstituted() {
/* 595 */       if (this.buf != this.src)
/* 596 */         Util.releaseTemporaryDirectBuffer(this.buf);
/*     */     }
/*     */ 
/*     */     void updatePosition(int paramInt)
/*     */     {
/* 601 */       if (paramInt > 0)
/*     */         try {
/* 603 */           this.src.position(this.pos + paramInt);
/*     */         }
/*     */         catch (IllegalArgumentException localIllegalArgumentException)
/*     */         {
/*     */         }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 612 */       int i = -1;
/* 613 */       long l1 = 0L;
/*     */       long l2;
/* 617 */       if ((this.src instanceof DirectBuffer)) {
/* 618 */         this.buf = this.src;
/* 619 */         l2 = ((DirectBuffer)this.src).address() + this.pos;
/*     */       } else {
/* 621 */         this.buf = Util.getTemporaryDirectBuffer(this.rem);
/* 622 */         this.buf.put(this.src);
/* 623 */         this.buf.flip();
/*     */ 
/* 626 */         this.src.position(this.pos);
/* 627 */         l2 = ((DirectBuffer)this.buf).address();
/*     */       }
/*     */       try
/*     */       {
/* 631 */         WindowsAsynchronousFileChannelImpl.this.begin();
/*     */ 
/* 634 */         l1 = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
/*     */ 
/* 637 */         i = WindowsAsynchronousFileChannelImpl.writeFile(WindowsAsynchronousFileChannelImpl.this.handle, l2, this.rem, this.position, l1);
/* 638 */         if (i == -2)
/*     */         {
/*     */           return;
/*     */         }
/* 642 */         throw new InternalError("Unexpected result: " + i);
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/* 647 */         this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(localThrowable));
/*     */ 
/* 650 */         if (l1 != 0L)
/* 651 */           WindowsAsynchronousFileChannelImpl.this.ioCache.remove(l1);
/* 652 */         releaseBufferIfSubstituted();
/*     */       }
/*     */       finally {
/* 655 */         WindowsAsynchronousFileChannelImpl.this.end();
/*     */       }
/*     */ 
/* 659 */       Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void completed(int paramInt, boolean paramBoolean)
/*     */     {
/* 667 */       updatePosition(paramInt);
/*     */ 
/* 670 */       releaseBufferIfSubstituted();
/*     */ 
/* 673 */       this.result.setResult(Integer.valueOf(paramInt));
/* 674 */       if (paramBoolean)
/* 675 */         Invoker.invokeUnchecked(this.result);
/*     */       else
/* 677 */         Invoker.invoke(this.result);
/*     */     }
/*     */ 
/*     */     public void failed(int paramInt, IOException paramIOException)
/*     */     {
/* 684 */       releaseBufferIfSubstituted();
/*     */ 
/* 687 */       if (WindowsAsynchronousFileChannelImpl.this.isOpen())
/* 688 */         this.result.setFailure(paramIOException);
/*     */       else {
/* 690 */         this.result.setFailure(new AsynchronousCloseException());
/*     */       }
/* 692 */       Invoker.invoke(this.result);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.WindowsAsynchronousFileChannelImpl
 * JD-Core Version:    0.6.2
 */