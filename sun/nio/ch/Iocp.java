/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.ShutdownChannelGroupException;
/*     */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*     */ import java.security.AccessController;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import sun.misc.Unsafe;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class Iocp extends AsynchronousChannelGroupImpl
/*     */ {
/*  47 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long INVALID_HANDLE_VALUE = -1L;
/* 453 */   private static final boolean supportsThreadAgnosticIo = Integer.parseInt(arrayOfString[0]) >= 6;
/*     */ 
/*  52 */   private final ReadWriteLock keyToChannelLock = new ReentrantReadWriteLock();
/*  53 */   private final Map<Integer, OverlappedChannel> keyToChannel = new HashMap();
/*     */   private int nextCompletionKey;
/*     */   private final long port;
/*     */   private boolean closed;
/*  66 */   private final Set<Long> staleIoSet = new HashSet();
/*     */ 
/*     */   Iocp(AsynchronousChannelProvider paramAsynchronousChannelProvider, ThreadPool paramThreadPool)
/*     */     throws IOException
/*     */   {
/*  71 */     super(paramAsynchronousChannelProvider, paramThreadPool);
/*  72 */     this.port = createIoCompletionPort(-1L, 0L, 0, fixedThreadCount());
/*     */ 
/*  74 */     this.nextCompletionKey = 1;
/*     */   }
/*     */ 
/*     */   Iocp start() {
/*  78 */     startThreads(new EventHandlerTask(null));
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   static boolean supportsThreadAgnosticIo()
/*     */   {
/*  97 */     return supportsThreadAgnosticIo;
/*     */   }
/*     */ 
/*     */   void implClose()
/*     */   {
/* 102 */     synchronized (this) {
/* 103 */       if (this.closed)
/* 104 */         return;
/* 105 */       this.closed = true;
/*     */     }
/* 107 */     close0(this.port);
/* 108 */     synchronized (this.staleIoSet) {
/* 109 */       for (Long localLong : this.staleIoSet) {
/* 110 */         unsafe.freeMemory(localLong.longValue());
/*     */       }
/* 112 */       this.staleIoSet.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isEmpty()
/*     */   {
/* 118 */     this.keyToChannelLock.writeLock().lock();
/*     */     try {
/* 120 */       return this.keyToChannel.isEmpty();
/*     */     } finally {
/* 122 */       this.keyToChannelLock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   final Object attachForeignChannel(final Channel paramChannel, FileDescriptor paramFileDescriptor)
/*     */     throws IOException
/*     */   {
/* 130 */     int i = associate(new OverlappedChannel() {
/*     */       public <V, A> PendingFuture<V, A> getByOverlapped(long paramAnonymousLong) {
/* 132 */         return null;
/*     */       }
/*     */       public void close() throws IOException {
/* 135 */         paramChannel.close();
/*     */       }
/*     */     }
/*     */     , 0L);
/*     */ 
/* 138 */     return Integer.valueOf(i);
/*     */   }
/*     */ 
/*     */   final void detachForeignChannel(Object paramObject)
/*     */   {
/* 143 */     disassociate(((Integer)paramObject).intValue());
/*     */   }
/*     */ 
/*     */   void closeAllChannels()
/*     */   {
/* 157 */     OverlappedChannel[] arrayOfOverlappedChannel = new OverlappedChannel[32];
/*     */     int i;
/*     */     do
/*     */     {
/* 161 */       this.keyToChannelLock.writeLock().lock();
/* 162 */       i = 0;
/*     */       try {
/* 164 */         for (Integer localInteger : this.keyToChannel.keySet()) {
/* 165 */           arrayOfOverlappedChannel[(i++)] = ((OverlappedChannel)this.keyToChannel.get(localInteger));
/* 166 */           if (i >= 32) break;
/*     */         }
/*     */       }
/*     */       finally {
/* 170 */         this.keyToChannelLock.writeLock().unlock();
/*     */       }
/*     */ 
/* 174 */       for (int j = 0; j < i; j++)
/*     */         try {
/* 176 */           arrayOfOverlappedChannel[j].close(); } catch (IOException localIOException) {
/*     */         }
/*     */     }
/* 179 */     while (i > 0);
/*     */   }
/*     */ 
/*     */   private void wakeup() {
/*     */     try {
/* 184 */       postQueuedCompletionStatus(this.port, 0);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 187 */       throw new AssertionError(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void executeOnHandlerTask(Runnable paramRunnable)
/*     */   {
/* 193 */     synchronized (this) {
/* 194 */       if (this.closed)
/* 195 */         throw new RejectedExecutionException();
/* 196 */       offerTask(paramRunnable);
/* 197 */       wakeup();
/*     */     }
/*     */   }
/*     */ 
/*     */   void shutdownHandlerTasks()
/*     */   {
/* 205 */     int i = threadCount();
/* 206 */     while (i-- > 0)
/* 207 */       wakeup();
/*     */   }
/*     */ 
/*     */   int associate(OverlappedChannel paramOverlappedChannel, long paramLong)
/*     */     throws IOException
/*     */   {
/* 215 */     this.keyToChannelLock.writeLock().lock();
/*     */     int i;
/*     */     try
/*     */     {
/* 220 */       if (isShutdown()) {
/* 221 */         throw new ShutdownChannelGroupException();
/*     */       }
/*     */       do
/*     */       {
/* 225 */         i = this.nextCompletionKey++;
/* 226 */       }while ((i == 0) || (this.keyToChannel.containsKey(Integer.valueOf(i))));
/*     */ 
/* 229 */       if (paramLong != 0L) {
/* 230 */         createIoCompletionPort(paramLong, this.port, i, 0);
/*     */       }
/*     */ 
/* 234 */       this.keyToChannel.put(Integer.valueOf(i), paramOverlappedChannel);
/*     */     } finally {
/* 236 */       this.keyToChannelLock.writeLock().unlock();
/*     */     }
/* 238 */     return i;
/*     */   }
/*     */ 
/*     */   void disassociate(int paramInt)
/*     */   {
/* 245 */     int i = 0;
/*     */ 
/* 247 */     this.keyToChannelLock.writeLock().lock();
/*     */     try {
/* 249 */       this.keyToChannel.remove(Integer.valueOf(paramInt));
/*     */ 
/* 252 */       if (this.keyToChannel.isEmpty())
/* 253 */         i = 1;
/*     */     }
/*     */     finally {
/* 256 */       this.keyToChannelLock.writeLock().unlock();
/*     */     }
/*     */ 
/* 260 */     if ((i != 0) && (isShutdown()))
/*     */       try {
/* 262 */         shutdownNow();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   void makeStale(Long paramLong)
/*     */   {
/* 272 */     synchronized (this.staleIoSet) {
/* 273 */       this.staleIoSet.add(paramLong);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkIfStale(long paramLong)
/*     */   {
/* 281 */     synchronized (this.staleIoSet) {
/* 282 */       boolean bool = this.staleIoSet.remove(Long.valueOf(paramLong));
/* 283 */       if (bool)
/* 284 */         unsafe.freeMemory(paramLong);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static IOException translateErrorToIOException(int paramInt)
/*     */   {
/* 306 */     String str = getErrorMessage(paramInt);
/* 307 */     if (str == null)
/* 308 */       str = "Unknown error: 0x0" + Integer.toHexString(paramInt);
/* 309 */     return new IOException(str);
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private static native long createIoCompletionPort(long paramLong1, long paramLong2, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void close0(long paramLong);
/*     */ 
/*     */   private static native void getQueuedCompletionStatus(long paramLong, CompletionStatus paramCompletionStatus)
/*     */     throws IOException;
/*     */ 
/*     */   private static native void postQueuedCompletionStatus(long paramLong, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   private static native String getErrorMessage(int paramInt);
/*     */ 
/*     */   static
/*     */   {
/* 446 */     Util.load();
/* 447 */     initIDs();
/*     */ 
/* 450 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("os.version"));
/*     */ 
/* 452 */     String[] arrayOfString = str.split("\\.");
/*     */   }
/*     */ 
/*     */   private static class CompletionStatus
/*     */   {
/*     */     private int error;
/*     */     private int bytesTransferred;
/*     */     private int completionKey;
/*     */     private long overlapped;
/*     */ 
/*     */     int error()
/*     */     {
/* 422 */       return this.error; } 
/* 423 */     int bytesTransferred() { return this.bytesTransferred; } 
/* 424 */     int completionKey() { return this.completionKey; } 
/* 425 */     long overlapped() { return this.overlapped; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private class EventHandlerTask
/*     */     implements Runnable
/*     */   {
/*     */     private EventHandlerTask()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 317 */       Invoker.GroupAndInvokeCount localGroupAndInvokeCount = Invoker.getGroupAndInvokeCount();
/*     */ 
/* 319 */       boolean bool1 = localGroupAndInvokeCount != null;
/* 320 */       Iocp.CompletionStatus localCompletionStatus = new Iocp.CompletionStatus(null);
/* 321 */       boolean bool2 = false;
/*     */       try
/*     */       {
/*     */         while (true)
/*     */         {
/* 326 */           if (localGroupAndInvokeCount != null) {
/* 327 */             localGroupAndInvokeCount.resetInvokeCount();
/* 331 */           }
/*     */ bool2 = false;
/*     */           int i;
/*     */           try {
/* 333 */             Iocp.getQueuedCompletionStatus(Iocp.this.port, localCompletionStatus);
/*     */           }
/*     */           catch (IOException localIOException) {
/* 336 */             localIOException.printStackTrace();
/*     */ 
/* 404 */             i = Iocp.this.threadExit(this, bool2);
/* 405 */             if ((i == 0) && (Iocp.this.isShutdown()))
/* 406 */               Iocp.this.implClose();
/*     */             return;
/*     */           }
/*     */           Object localObject1;
/* 341 */           if ((localCompletionStatus.completionKey() == 0) && (localCompletionStatus.overlapped() == 0L))
/*     */           {
/* 344 */             localObject1 = Iocp.this.pollTask();
/* 345 */             if (localObject1 == null)
/*     */             {
/*     */               return;
/*     */             }
/*     */ 
/* 352 */             bool2 = true;
/* 353 */             ((Runnable)localObject1).run();
/*     */           }
/*     */           else
/*     */           {
/* 358 */             localObject1 = null;
/* 359 */             Iocp.this.keyToChannelLock.readLock().lock();
/*     */             try {
/* 361 */               localObject1 = (Iocp.OverlappedChannel)Iocp.this.keyToChannel.get(Integer.valueOf(localCompletionStatus.completionKey()));
/* 362 */               if (localObject1 == null) {
/* 363 */                 Iocp.this.checkIfStale(localCompletionStatus.overlapped());
/*     */ 
/* 367 */                 Iocp.this.keyToChannelLock.readLock().unlock(); continue; }  } finally { Iocp.this.keyToChannelLock.readLock().unlock(); }
/*     */ 
/*     */ 
/* 371 */             PendingFuture localPendingFuture = ((Iocp.OverlappedChannel)localObject1).getByOverlapped(localCompletionStatus.overlapped());
/* 372 */             if (localPendingFuture == null)
/*     */             {
/* 379 */               Iocp.this.checkIfStale(localCompletionStatus.overlapped());
/*     */             }
/*     */             else
/*     */             {
/* 385 */               synchronized (localPendingFuture) {
/* 386 */                 if (localPendingFuture.isDone()) {
/* 387 */                   continue;
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 393 */               int j = localCompletionStatus.error();
/* 394 */               Iocp.ResultHandler localResultHandler = (Iocp.ResultHandler)localPendingFuture.getContext();
/* 395 */               bool2 = true;
/* 396 */               if (j == 0)
/* 397 */                 localResultHandler.completed(localCompletionStatus.bytesTransferred(), bool1);
/*     */               else
/* 399 */                 localResultHandler.failed(j, Iocp.translateErrorToIOException(j));
/*     */             }
/*     */           }
/*     */         }
/*     */       } finally {
/* 404 */         int k = Iocp.this.threadExit(this, bool2);
/* 405 */         if ((k == 0) && (Iocp.this.isShutdown()))
/* 406 */           Iocp.this.implClose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface OverlappedChannel extends Closeable
/*     */   {
/*     */     public abstract <V, A> PendingFuture<V, A> getByOverlapped(long paramLong);
/*     */   }
/*     */ 
/*     */   static abstract interface ResultHandler
/*     */   {
/*     */     public abstract void completed(int paramInt, boolean paramBoolean);
/*     */ 
/*     */     public abstract void failed(int paramInt, IOException paramIOException);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Iocp
 * JD-Core Version:    0.6.2
 */