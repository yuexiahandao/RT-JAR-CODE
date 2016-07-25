/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.AsynchronousChannelGroup;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.spi.AsynchronousChannelProvider;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ abstract class AsynchronousChannelGroupImpl extends AsynchronousChannelGroup
/*     */   implements Executor
/*     */ {
/*  51 */   private static final int internalThreadCount = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.internalThreadPoolSize", 1))).intValue();
/*     */   private final ThreadPool pool;
/*  58 */   private final AtomicInteger threadCount = new AtomicInteger();
/*     */   private ScheduledThreadPoolExecutor timeoutExecutor;
/*     */   private final Queue<Runnable> taskQueue;
/*  68 */   private final AtomicBoolean shutdown = new AtomicBoolean();
/*  69 */   private final Object shutdownNowLock = new Object();
/*     */   private volatile boolean terminateInitiated;
/*     */ 
/*     */   AsynchronousChannelGroupImpl(AsynchronousChannelProvider paramAsynchronousChannelProvider, ThreadPool paramThreadPool)
/*     */   {
/*  75 */     super(paramAsynchronousChannelProvider);
/*  76 */     this.pool = paramThreadPool;
/*     */ 
/*  78 */     if (paramThreadPool.isFixedThreadPool())
/*  79 */       this.taskQueue = new ConcurrentLinkedQueue();
/*     */     else {
/*  81 */       this.taskQueue = null;
/*     */     }
/*     */ 
/*  86 */     this.timeoutExecutor = ((ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1, ThreadPool.defaultThreadFactory()));
/*     */ 
/*  88 */     this.timeoutExecutor.setRemoveOnCancelPolicy(true);
/*     */   }
/*     */ 
/*     */   final ExecutorService executor() {
/*  92 */     return this.pool.executor();
/*     */   }
/*     */ 
/*     */   final boolean isFixedThreadPool() {
/*  96 */     return this.pool.isFixedThreadPool();
/*     */   }
/*     */ 
/*     */   final int fixedThreadCount() {
/* 100 */     if (isFixedThreadPool()) {
/* 101 */       return this.pool.poolSize();
/*     */     }
/* 103 */     return this.pool.poolSize() + internalThreadCount;
/*     */   }
/*     */ 
/*     */   private Runnable bindToGroup(final Runnable paramRunnable)
/*     */   {
/* 108 */     final AsynchronousChannelGroupImpl localAsynchronousChannelGroupImpl = this;
/* 109 */     return new Runnable() {
/*     */       public void run() {
/* 111 */         Invoker.bindToGroup(localAsynchronousChannelGroupImpl);
/* 112 */         paramRunnable.run();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private void startInternalThread(final Runnable paramRunnable) {
/* 118 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/* 123 */         ThreadPool.defaultThreadFactory().newThread(paramRunnable).start();
/* 124 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected final void startThreads(Runnable paramRunnable)
/*     */   {
/*     */     int i;
/* 130 */     if (!isFixedThreadPool()) {
/* 131 */       for (i = 0; i < internalThreadCount; i++) {
/* 132 */         startInternalThread(paramRunnable);
/* 133 */         this.threadCount.incrementAndGet();
/*     */       }
/*     */     }
/* 136 */     if (this.pool.poolSize() > 0) {
/* 137 */       paramRunnable = bindToGroup(paramRunnable);
/*     */       try {
/* 139 */         for (i = 0; i < this.pool.poolSize(); i++) {
/* 140 */           this.pool.executor().execute(paramRunnable);
/* 141 */           this.threadCount.incrementAndGet();
/*     */         }
/*     */       }
/*     */       catch (RejectedExecutionException localRejectedExecutionException) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final int threadCount() {
/* 150 */     return this.threadCount.get();
/*     */   }
/*     */ 
/*     */   final int threadExit(Runnable paramRunnable, boolean paramBoolean)
/*     */   {
/* 157 */     if (paramBoolean)
/*     */       try {
/* 159 */         if (Invoker.isBoundToAnyGroup())
/*     */         {
/* 161 */           this.pool.executor().execute(bindToGroup(paramRunnable));
/*     */         }
/*     */         else {
/* 164 */           startInternalThread(paramRunnable);
/*     */         }
/* 166 */         return this.threadCount.get();
/*     */       }
/*     */       catch (RejectedExecutionException localRejectedExecutionException)
/*     */       {
/*     */       }
/* 171 */     return this.threadCount.decrementAndGet();
/*     */   }
/*     */ 
/*     */   abstract void executeOnHandlerTask(Runnable paramRunnable);
/*     */ 
/*     */   final void executeOnPooledThread(Runnable paramRunnable)
/*     */   {
/* 185 */     if (isFixedThreadPool())
/* 186 */       executeOnHandlerTask(paramRunnable);
/*     */     else
/* 188 */       this.pool.executor().execute(bindToGroup(paramRunnable));
/*     */   }
/*     */ 
/*     */   final void offerTask(Runnable paramRunnable)
/*     */   {
/* 193 */     this.taskQueue.offer(paramRunnable);
/*     */   }
/*     */ 
/*     */   final Runnable pollTask() {
/* 197 */     return this.taskQueue == null ? null : (Runnable)this.taskQueue.poll();
/*     */   }
/*     */ 
/*     */   final Future<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
/*     */     try {
/* 202 */       return this.timeoutExecutor.schedule(paramRunnable, paramLong, paramTimeUnit);
/*     */     } catch (RejectedExecutionException localRejectedExecutionException) {
/* 204 */       if (this.terminateInitiated)
/*     */       {
/* 206 */         return null;
/*     */       }
/* 208 */       throw new AssertionError(localRejectedExecutionException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isShutdown()
/*     */   {
/* 214 */     return this.shutdown.get();
/*     */   }
/*     */ 
/*     */   public final boolean isTerminated()
/*     */   {
/* 219 */     return this.pool.executor().isTerminated();
/*     */   }
/*     */ 
/*     */   abstract boolean isEmpty();
/*     */ 
/*     */   abstract Object attachForeignChannel(Channel paramChannel, FileDescriptor paramFileDescriptor)
/*     */     throws IOException;
/*     */ 
/*     */   abstract void detachForeignChannel(Object paramObject);
/*     */ 
/*     */   abstract void closeAllChannels()
/*     */     throws IOException;
/*     */ 
/*     */   abstract void shutdownHandlerTasks();
/*     */ 
/*     */   private void shutdownExecutors()
/*     */   {
/* 249 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/* 251 */         AsynchronousChannelGroupImpl.this.pool.executor().shutdown();
/* 252 */         AsynchronousChannelGroupImpl.this.timeoutExecutor.shutdown();
/* 253 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public final void shutdown()
/*     */   {
/* 260 */     if (this.shutdown.getAndSet(true))
/*     */     {
/* 262 */       return;
/*     */     }
/*     */ 
/* 266 */     if (!isEmpty()) {
/* 267 */       return;
/*     */     }
/*     */ 
/* 271 */     synchronized (this.shutdownNowLock) {
/* 272 */       if (!this.terminateInitiated) {
/* 273 */         this.terminateInitiated = true;
/* 274 */         shutdownHandlerTasks();
/* 275 */         shutdownExecutors();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void shutdownNow() throws IOException
/*     */   {
/* 282 */     this.shutdown.set(true);
/* 283 */     synchronized (this.shutdownNowLock) {
/* 284 */       if (!this.terminateInitiated) {
/* 285 */         this.terminateInitiated = true;
/* 286 */         closeAllChannels();
/* 287 */         shutdownHandlerTasks();
/* 288 */         shutdownExecutors();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final void detachFromThreadPool()
/*     */   {
/* 298 */     if (this.shutdown.getAndSet(true))
/* 299 */       throw new AssertionError("Already shutdown");
/* 300 */     if (!isEmpty())
/* 301 */       throw new AssertionError("Group not empty");
/* 302 */     shutdownHandlerTasks();
/*     */   }
/*     */ 
/*     */   public final boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 309 */     return this.pool.executor().awaitTermination(paramLong, paramTimeUnit);
/*     */   }
/*     */ 
/*     */   public final void execute(Runnable paramRunnable)
/*     */   {
/* 317 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 318 */     if (localSecurityManager != null)
/*     */     {
/* 321 */       final AccessControlContext localAccessControlContext = AccessController.getContext();
/* 322 */       final Runnable localRunnable = paramRunnable;
/* 323 */       paramRunnable = new Runnable()
/*     */       {
/*     */         public void run() {
/* 326 */           AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Void run() {
/* 329 */               AsynchronousChannelGroupImpl.4.this.val$delegate.run();
/* 330 */               return null;
/*     */             }
/*     */           }
/*     */           , localAccessControlContext);
/*     */         }
/*     */ 
/*     */       };
/*     */     }
/*     */ 
/* 336 */     executeOnPooledThread(paramRunnable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.AsynchronousChannelGroupImpl
 * JD-Core Version:    0.6.2
 */