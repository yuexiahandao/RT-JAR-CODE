/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public class Executors
/*     */ {
/*     */   public static ExecutorService newFixedThreadPool(int paramInt)
/*     */   {
/*  89 */     return new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
/*     */   }
/*     */ 
/*     */   public static ExecutorService newFixedThreadPool(int paramInt, ThreadFactory paramThreadFactory)
/*     */   {
/* 114 */     return new ThreadPoolExecutor(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), paramThreadFactory);
/*     */   }
/*     */ 
/*     */   public static ExecutorService newSingleThreadExecutor()
/*     */   {
/* 134 */     return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()));
/*     */   }
/*     */ 
/*     */   public static ExecutorService newSingleThreadExecutor(ThreadFactory paramThreadFactory)
/*     */   {
/* 155 */     return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), paramThreadFactory));
/*     */   }
/*     */ 
/*     */   public static ExecutorService newCachedThreadPool()
/*     */   {
/* 179 */     return new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue());
/*     */   }
/*     */ 
/*     */   public static ExecutorService newCachedThreadPool(ThreadFactory paramThreadFactory)
/*     */   {
/* 194 */     return new ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), paramThreadFactory);
/*     */   }
/*     */ 
/*     */   public static ScheduledExecutorService newSingleThreadScheduledExecutor()
/*     */   {
/* 214 */     return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1));
/*     */   }
/*     */ 
/*     */   public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory paramThreadFactory)
/*     */   {
/* 235 */     return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1, paramThreadFactory));
/*     */   }
/*     */ 
/*     */   public static ScheduledExecutorService newScheduledThreadPool(int paramInt)
/*     */   {
/* 248 */     return new ScheduledThreadPoolExecutor(paramInt);
/*     */   }
/*     */ 
/*     */   public static ScheduledExecutorService newScheduledThreadPool(int paramInt, ThreadFactory paramThreadFactory)
/*     */   {
/* 264 */     return new ScheduledThreadPoolExecutor(paramInt, paramThreadFactory);
/*     */   }
/*     */ 
/*     */   public static ExecutorService unconfigurableExecutorService(ExecutorService paramExecutorService)
/*     */   {
/* 279 */     if (paramExecutorService == null)
/* 280 */       throw new NullPointerException();
/* 281 */     return new DelegatedExecutorService(paramExecutorService);
/*     */   }
/*     */ 
/*     */   public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService paramScheduledExecutorService)
/*     */   {
/* 295 */     if (paramScheduledExecutorService == null)
/* 296 */       throw new NullPointerException();
/* 297 */     return new DelegatedScheduledExecutorService(paramScheduledExecutorService);
/*     */   }
/*     */ 
/*     */   public static ThreadFactory defaultThreadFactory()
/*     */   {
/* 317 */     return new DefaultThreadFactory();
/*     */   }
/*     */ 
/*     */   public static ThreadFactory privilegedThreadFactory()
/*     */   {
/* 353 */     return new PrivilegedThreadFactory();
/*     */   }
/*     */ 
/*     */   public static <T> Callable<T> callable(Runnable paramRunnable, T paramT)
/*     */   {
/* 367 */     if (paramRunnable == null)
/* 368 */       throw new NullPointerException();
/* 369 */     return new RunnableAdapter(paramRunnable, paramT);
/*     */   }
/*     */ 
/*     */   public static Callable<Object> callable(Runnable paramRunnable)
/*     */   {
/* 380 */     if (paramRunnable == null)
/* 381 */       throw new NullPointerException();
/* 382 */     return new RunnableAdapter(paramRunnable, null);
/*     */   }
/*     */ 
/*     */   public static Callable<Object> callable(PrivilegedAction<?> paramPrivilegedAction)
/*     */   {
/* 393 */     if (paramPrivilegedAction == null)
/* 394 */       throw new NullPointerException();
/* 395 */     return new Callable() {
/* 396 */       public Object call() { return this.val$action.run(); }
/*     */ 
/*     */     };
/*     */   }
/*     */ 
/*     */   public static Callable<Object> callable(PrivilegedExceptionAction<?> paramPrivilegedExceptionAction)
/*     */   {
/* 408 */     if (paramPrivilegedExceptionAction == null)
/* 409 */       throw new NullPointerException();
/* 410 */     return new Callable() {
/* 411 */       public Object call() throws Exception { return this.val$action.run(); }
/*     */ 
/*     */     };
/*     */   }
/*     */ 
/*     */   public static <T> Callable<T> privilegedCallable(Callable<T> paramCallable)
/*     */   {
/* 429 */     if (paramCallable == null)
/* 430 */       throw new NullPointerException();
/* 431 */     return new PrivilegedCallable(paramCallable);
/*     */   }
/*     */ 
/*     */   public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> paramCallable)
/*     */   {
/* 453 */     if (paramCallable == null)
/* 454 */       throw new NullPointerException();
/* 455 */     return new PrivilegedCallableUsingCurrentClassLoader(paramCallable);
/*     */   }
/*     */ 
/*     */   static class DefaultThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/* 557 */     private static final AtomicInteger poolNumber = new AtomicInteger(1);
/*     */     private final ThreadGroup group;
/* 559 */     private final AtomicInteger threadNumber = new AtomicInteger(1);
/*     */     private final String namePrefix;
/*     */ 
/*     */     DefaultThreadFactory()
/*     */     {
/* 563 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 564 */       this.group = (localSecurityManager != null ? localSecurityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
/*     */ 
/* 566 */       this.namePrefix = ("pool-" + poolNumber.getAndIncrement() + "-thread-");
/*     */     }
/*     */ 
/*     */     public Thread newThread(Runnable paramRunnable)
/*     */     {
/* 572 */       Thread localThread = new Thread(this.group, paramRunnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
/*     */ 
/* 575 */       if (localThread.isDaemon())
/* 576 */         localThread.setDaemon(false);
/* 577 */       if (localThread.getPriority() != 5)
/* 578 */         localThread.setPriority(5);
/* 579 */       return localThread;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DelegatedExecutorService extends AbstractExecutorService
/*     */   {
/*     */     private final ExecutorService e;
/*     */ 
/*     */     DelegatedExecutorService(ExecutorService paramExecutorService)
/*     */     {
/* 627 */       this.e = paramExecutorService; } 
/* 628 */     public void execute(Runnable paramRunnable) { this.e.execute(paramRunnable); } 
/* 629 */     public void shutdown() { this.e.shutdown(); } 
/* 630 */     public List<Runnable> shutdownNow() { return this.e.shutdownNow(); } 
/* 631 */     public boolean isShutdown() { return this.e.isShutdown(); } 
/* 632 */     public boolean isTerminated() { return this.e.isTerminated(); }
/*     */ 
/*     */     public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
/* 635 */       return this.e.awaitTermination(paramLong, paramTimeUnit);
/*     */     }
/*     */     public Future<?> submit(Runnable paramRunnable) {
/* 638 */       return this.e.submit(paramRunnable);
/*     */     }
/*     */     public <T> Future<T> submit(Callable<T> paramCallable) {
/* 641 */       return this.e.submit(paramCallable);
/*     */     }
/*     */     public <T> Future<T> submit(Runnable paramRunnable, T paramT) {
/* 644 */       return this.e.submit(paramRunnable, paramT);
/*     */     }
/*     */ 
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection) throws InterruptedException {
/* 648 */       return this.e.invokeAll(paramCollection);
/*     */     }
/*     */ 
/*     */     public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*     */     {
/* 653 */       return this.e.invokeAll(paramCollection, paramLong, paramTimeUnit);
/*     */     }
/*     */ 
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection) throws InterruptedException, ExecutionException {
/* 657 */       return this.e.invokeAny(paramCollection);
/*     */     }
/*     */ 
/*     */     public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException
/*     */     {
/* 662 */       return this.e.invokeAny(paramCollection, paramLong, paramTimeUnit);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DelegatedScheduledExecutorService extends Executors.DelegatedExecutorService
/*     */     implements ScheduledExecutorService
/*     */   {
/*     */     private final ScheduledExecutorService e;
/*     */ 
/*     */     DelegatedScheduledExecutorService(ScheduledExecutorService paramScheduledExecutorService)
/*     */     {
/* 685 */       super();
/* 686 */       this.e = paramScheduledExecutorService;
/*     */     }
/*     */     public ScheduledFuture<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
/* 689 */       return this.e.schedule(paramRunnable, paramLong, paramTimeUnit);
/*     */     }
/*     */     public <V> ScheduledFuture<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit) {
/* 692 */       return this.e.schedule(paramCallable, paramLong, paramTimeUnit);
/*     */     }
/*     */     public ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
/* 695 */       return this.e.scheduleAtFixedRate(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
/*     */     }
/*     */     public ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit) {
/* 698 */       return this.e.scheduleWithFixedDelay(paramRunnable, paramLong1, paramLong2, paramTimeUnit);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class FinalizableDelegatedExecutorService extends Executors.DelegatedExecutorService
/*     */   {
/*     */     FinalizableDelegatedExecutorService(ExecutorService paramExecutorService)
/*     */     {
/* 669 */       super();
/*     */     }
/*     */     protected void finalize() {
/* 672 */       super.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PrivilegedCallable<T>
/*     */     implements Callable<T>
/*     */   {
/*     */     private final Callable<T> task;
/*     */     private final AccessControlContext acc;
/*     */ 
/*     */     PrivilegedCallable(Callable<T> paramCallable)
/*     */     {
/* 484 */       this.task = paramCallable;
/* 485 */       this.acc = AccessController.getContext();
/*     */     }
/*     */ 
/*     */     public T call() throws Exception {
/*     */       try {
/* 490 */         return AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public T run() throws Exception {
/* 493 */             return Executors.PrivilegedCallable.this.task.call();
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/* 497 */         throw localPrivilegedActionException.getException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PrivilegedCallableUsingCurrentClassLoader<T> implements Callable<T>
/*     */   {
/*     */     private final Callable<T> task;
/*     */     private final AccessControlContext acc;
/*     */     private final ClassLoader ccl;
/*     */ 
/*     */     PrivilegedCallableUsingCurrentClassLoader(Callable<T> paramCallable)
/*     */     {
/* 512 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 513 */       if (localSecurityManager != null)
/*     */       {
/* 517 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*     */ 
/* 521 */         localSecurityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
/*     */       }
/* 523 */       this.task = paramCallable;
/* 524 */       this.acc = AccessController.getContext();
/* 525 */       this.ccl = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */ 
/*     */     public T call() throws Exception {
/*     */       try {
/* 530 */         return AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public T run() throws Exception {
/* 533 */             Thread localThread = Thread.currentThread();
/* 534 */             ClassLoader localClassLoader = localThread.getContextClassLoader();
/* 535 */             if (Executors.PrivilegedCallableUsingCurrentClassLoader.this.ccl == localClassLoader) {
/* 536 */               return Executors.PrivilegedCallableUsingCurrentClassLoader.this.task.call();
/*     */             }
/* 538 */             localThread.setContextClassLoader(Executors.PrivilegedCallableUsingCurrentClassLoader.this.ccl);
/*     */             try {
/* 540 */               return Executors.PrivilegedCallableUsingCurrentClassLoader.this.task.call();
/*     */             } finally {
/* 542 */               localThread.setContextClassLoader(localClassLoader);
/*     */             }
/*     */           }
/*     */         }
/*     */         , this.acc);
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/* 548 */         throw localPrivilegedActionException.getException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class PrivilegedThreadFactory extends Executors.DefaultThreadFactory
/*     */   {
/*     */     private final AccessControlContext acc;
/*     */     private final ClassLoader ccl;
/*     */ 
/*     */     PrivilegedThreadFactory()
/*     */     {
/* 592 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 593 */       if (localSecurityManager != null)
/*     */       {
/* 597 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*     */ 
/* 600 */         localSecurityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
/*     */       }
/* 602 */       this.acc = AccessController.getContext();
/* 603 */       this.ccl = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */ 
/*     */     public Thread newThread(final Runnable paramRunnable) {
/* 607 */       return super.newThread(new Runnable() {
/*     */         public void run() {
/* 609 */           AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Void run() {
/* 611 */               Thread.currentThread().setContextClassLoader(Executors.PrivilegedThreadFactory.this.ccl);
/* 612 */               Executors.PrivilegedThreadFactory.1.this.val$r.run();
/* 613 */               return null;
/*     */             }
/*     */           }
/*     */           , Executors.PrivilegedThreadFactory.this.acc);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class RunnableAdapter<T>
/*     */     implements Callable<T>
/*     */   {
/*     */     final Runnable task;
/*     */     final T result;
/*     */ 
/*     */     RunnableAdapter(Runnable paramRunnable, T paramT)
/*     */     {
/* 467 */       this.task = paramRunnable;
/* 468 */       this.result = paramT;
/*     */     }
/*     */     public T call() {
/* 471 */       this.task.run();
/* 472 */       return this.result;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Executors
 * JD-Core Version:    0.6.2
 */