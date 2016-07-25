/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ public class ThreadPoolExecutor extends AbstractExecutorService
/*      */ {
/*  375 */   private final AtomicInteger ctl = new AtomicInteger(ctlOf(-536870912, 0));
/*      */   private static final int COUNT_BITS = 29;
/*      */   private static final int CAPACITY = 536870911;
/*      */   private static final int RUNNING = -536870912;
/*      */   private static final int SHUTDOWN = 0;
/*      */   private static final int STOP = 536870912;
/*      */   private static final int TIDYING = 1073741824;
/*      */   private static final int TERMINATED = 1610612736;
/*      */   private final BlockingQueue<Runnable> workQueue;
/*  457 */   private final ReentrantLock mainLock = new ReentrantLock();
/*      */ 
/*  463 */   private final HashSet<Worker> workers = new HashSet();
/*      */ 
/*  468 */   private final Condition termination = this.mainLock.newCondition();
/*      */   private int largestPoolSize;
/*      */   private long completedTaskCount;
/*      */   private volatile ThreadFactory threadFactory;
/*      */   private volatile RejectedExecutionHandler handler;
/*      */   private volatile long keepAliveTime;
/*      */   private volatile boolean allowCoreThreadTimeOut;
/*      */   private volatile int corePoolSize;
/*      */   private volatile int maximumPoolSize;
/*  544 */   private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
/*      */ 
/*  567 */   private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
/*      */   private static final boolean ONLY_ONE = true;
/*      */ 
/*      */   private static int runStateOf(int paramInt)
/*      */   {
/*  387 */     return paramInt & 0xE0000000; } 
/*  388 */   private static int workerCountOf(int paramInt) { return paramInt & 0x1FFFFFFF; } 
/*  389 */   private static int ctlOf(int paramInt1, int paramInt2) { return paramInt1 | paramInt2; }
/*      */ 
/*      */ 
/*      */   private static boolean runStateLessThan(int paramInt1, int paramInt2)
/*      */   {
/*  397 */     return paramInt1 < paramInt2;
/*      */   }
/*      */ 
/*      */   private static boolean runStateAtLeast(int paramInt1, int paramInt2) {
/*  401 */     return paramInt1 >= paramInt2;
/*      */   }
/*      */ 
/*      */   private static boolean isRunning(int paramInt) {
/*  405 */     return paramInt < 0;
/*      */   }
/*      */ 
/*      */   private boolean compareAndIncrementWorkerCount(int paramInt)
/*      */   {
/*  412 */     return this.ctl.compareAndSet(paramInt, paramInt + 1);
/*      */   }
/*      */ 
/*      */   private boolean compareAndDecrementWorkerCount(int paramInt)
/*      */   {
/*  419 */     return this.ctl.compareAndSet(paramInt, paramInt - 1);
/*      */   }
/*      */ 
/*      */   private void decrementWorkerCount()
/*      */   {
/*  428 */     while (!compareAndDecrementWorkerCount(this.ctl.get()));
/*      */   }
/*      */ 
/*      */   private void advanceRunState(int paramInt)
/*      */   {
/*      */     while (true)
/*      */     {
/*  670 */       int i = this.ctl.get();
/*  671 */       if ((runStateAtLeast(i, paramInt)) || (this.ctl.compareAndSet(i, ctlOf(paramInt, workerCountOf(i)))))
/*      */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void tryTerminate()
/*      */   {
/*      */     while (true)
/*      */     {
/*  689 */       int i = this.ctl.get();
/*  690 */       if ((isRunning(i)) || (runStateAtLeast(i, 1073741824)) || ((runStateOf(i) == 0) && (!this.workQueue.isEmpty())))
/*      */       {
/*  693 */         return;
/*  694 */       }if (workerCountOf(i) != 0) {
/*  695 */         interruptIdleWorkers(true);
/*  696 */         return;
/*      */       }
/*      */ 
/*  699 */       ReentrantLock localReentrantLock = this.mainLock;
/*  700 */       localReentrantLock.lock();
/*      */       try {
/*  702 */         if (this.ctl.compareAndSet(i, ctlOf(1073741824, 0))) {
/*      */           try {
/*      */           }
/*      */           finally {
/*  706 */             this.ctl.set(ctlOf(1610612736, 0));
/*  707 */             this.termination.signalAll();
/*      */           }
/*      */           return;
/*      */         }
/*      */       } finally {
/*  712 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkShutdownAccess()
/*      */   {
/*  731 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  732 */     if (localSecurityManager != null) {
/*  733 */       localSecurityManager.checkPermission(shutdownPerm);
/*  734 */       ReentrantLock localReentrantLock = this.mainLock;
/*  735 */       localReentrantLock.lock();
/*      */       try {
/*  737 */         for (Worker localWorker : this.workers)
/*  738 */           localSecurityManager.checkAccess(localWorker.thread);
/*      */       } finally {
/*  740 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void interruptWorkers()
/*      */   {
/*  750 */     ReentrantLock localReentrantLock = this.mainLock;
/*  751 */     localReentrantLock.lock();
/*      */     try {
/*  753 */       for (Worker localWorker : this.workers)
/*  754 */         localWorker.interruptIfStarted();
/*      */     } finally {
/*  756 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void interruptIdleWorkers(boolean paramBoolean)
/*      */   {
/*  780 */     ReentrantLock localReentrantLock = this.mainLock;
/*  781 */     localReentrantLock.lock();
/*      */     try {
/*  783 */       for (Worker localWorker : this.workers) {
/*  784 */         Thread localThread = localWorker.thread;
/*  785 */         if ((!localThread.isInterrupted()) && (localWorker.tryLock()))
/*      */           try
/*      */           {
/*      */           }
/*      */           catch (SecurityException localSecurityException) {
/*      */           }
/*      */           finally {
/*      */           }
/*  793 */         if (paramBoolean) break;
/*      */       }
/*      */     }
/*      */     finally {
/*  797 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void interruptIdleWorkers()
/*      */   {
/*  806 */     interruptIdleWorkers(false);
/*      */   }
/*      */ 
/*      */   final void reject(Runnable paramRunnable)
/*      */   {
/*  821 */     this.handler.rejectedExecution(paramRunnable, this);
/*      */   }
/*      */ 
/*      */   void onShutdown()
/*      */   {
/*      */   }
/*      */ 
/*      */   final boolean isRunningOrShutdown(boolean paramBoolean)
/*      */   {
/*  839 */     int i = runStateOf(this.ctl.get());
/*  840 */     return (i == -536870912) || ((i == 0) && (paramBoolean));
/*      */   }
/*      */ 
/*      */   private List<Runnable> drainQueue()
/*      */   {
/*  850 */     BlockingQueue localBlockingQueue = this.workQueue;
/*  851 */     ArrayList localArrayList = new ArrayList();
/*  852 */     localBlockingQueue.drainTo(localArrayList);
/*  853 */     if (!localBlockingQueue.isEmpty()) {
/*  854 */       for (Runnable localRunnable : (Runnable[])localBlockingQueue.toArray(new Runnable[0])) {
/*  855 */         if (localBlockingQueue.remove(localRunnable))
/*  856 */           localArrayList.add(localRunnable);
/*      */       }
/*      */     }
/*  859 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private boolean addWorker(Runnable paramRunnable, boolean paramBoolean)
/*      */   {
/*  895 */     int i = this.ctl.get();
/*  896 */     int j = runStateOf(i);
/*      */ 
/*  899 */     if ((j >= 0) && ((j != 0) || (paramRunnable != null) || (this.workQueue.isEmpty())))
/*      */     {
/*  903 */       return false;
/*      */     }
/*      */     while (true) {
/*  906 */       int k = workerCountOf(i);
/*  907 */       if (k < 536870911) { if (k < (paramBoolean ? this.corePoolSize : this.maximumPoolSize));
/*      */       } else
/*  909 */         return false;
/*  910 */       if (compareAndIncrementWorkerCount(i))
/*      */         break label111;
/*  912 */       i = this.ctl.get();
/*  913 */       if (runStateOf(i) != j)
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*  919 */     label111: i = 0;
/*  920 */     j = 0;
/*  921 */     Worker localWorker = null;
/*      */     try {
/*  923 */       ReentrantLock localReentrantLock = this.mainLock;
/*  924 */       localWorker = new Worker(paramRunnable);
/*  925 */       Thread localThread = localWorker.thread;
/*  926 */       if (localThread != null) {
/*  927 */         localReentrantLock.lock();
/*      */         try
/*      */         {
/*  932 */           int m = this.ctl.get();
/*  933 */           int n = runStateOf(m);
/*      */ 
/*  935 */           if ((n < 0) || ((n == 0) && (paramRunnable == null)))
/*      */           {
/*  937 */             if (localThread.isAlive())
/*  938 */               throw new IllegalThreadStateException();
/*  939 */             this.workers.add(localWorker);
/*  940 */             int i1 = this.workers.size();
/*  941 */             if (i1 > this.largestPoolSize)
/*  942 */               this.largestPoolSize = i1;
/*  943 */             j = 1;
/*      */           }
/*      */         } finally {
/*  946 */           localReentrantLock.unlock();
/*      */         }
/*  948 */         if (j != 0) {
/*  949 */           localThread.start();
/*  950 */           i = 1;
/*      */         }
/*      */       }
/*      */     } finally {
/*  954 */       if (i == 0)
/*  955 */         addWorkerFailed(localWorker);
/*      */     }
/*  957 */     return i;
/*      */   }
/*      */ 
/*      */   private void addWorkerFailed(Worker paramWorker)
/*      */   {
/*  968 */     ReentrantLock localReentrantLock = this.mainLock;
/*  969 */     localReentrantLock.lock();
/*      */     try {
/*  971 */       if (paramWorker != null)
/*  972 */         this.workers.remove(paramWorker);
/*  973 */       decrementWorkerCount();
/*  974 */       tryTerminate();
/*      */     } finally {
/*  976 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void processWorkerExit(Worker paramWorker, boolean paramBoolean)
/*      */   {
/*  994 */     if (paramBoolean) {
/*  995 */       decrementWorkerCount();
/*      */     }
/*  997 */     ReentrantLock localReentrantLock = this.mainLock;
/*  998 */     localReentrantLock.lock();
/*      */     try {
/* 1000 */       this.completedTaskCount += paramWorker.completedTasks;
/* 1001 */       this.workers.remove(paramWorker);
/*      */     } finally {
/* 1003 */       localReentrantLock.unlock();
/*      */     }
/*      */ 
/* 1006 */     tryTerminate();
/*      */ 
/* 1008 */     int i = this.ctl.get();
/* 1009 */     if (runStateLessThan(i, 536870912)) {
/* 1010 */       if (!paramBoolean) {
/* 1011 */         int j = this.allowCoreThreadTimeOut ? 0 : this.corePoolSize;
/* 1012 */         if ((j == 0) && (!this.workQueue.isEmpty()))
/* 1013 */           j = 1;
/* 1014 */         if (workerCountOf(i) >= j)
/* 1015 */           return;
/*      */       }
/* 1017 */       addWorker(null, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Runnable getTask()
/*      */   {
/* 1038 */     int i = 0;
/*      */     while (true)
/*      */     {
/* 1042 */       int j = this.ctl.get();
/* 1043 */       int k = runStateOf(j);
/*      */ 
/* 1046 */       if ((k >= 0) && ((k >= 536870912) || (this.workQueue.isEmpty()))) {
/* 1047 */         decrementWorkerCount();
/* 1048 */         return null;
/*      */       }
/*      */ 
/*      */       int m;
/*      */       while (true)
/*      */       {
/* 1054 */         int n = workerCountOf(j);
/* 1055 */         m = (this.allowCoreThreadTimeOut) || (n > this.corePoolSize) ? 1 : 0;
/*      */ 
/* 1057 */         if ((n <= this.maximumPoolSize) && ((i == 0) || (m == 0)))
/*      */           break label125;
/* 1059 */         if (compareAndDecrementWorkerCount(j))
/* 1060 */           return null;
/* 1061 */         j = this.ctl.get();
/* 1062 */         if (runStateOf(j) != k) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1068 */         label125: Runnable localRunnable = m != 0 ? (Runnable)this.workQueue.poll(this.keepAliveTime, TimeUnit.NANOSECONDS) : (Runnable)this.workQueue.take();
/*      */ 
/* 1071 */         if (localRunnable != null)
/* 1072 */           return localRunnable;
/* 1073 */         i = 1;
/*      */       } catch (InterruptedException localInterruptedException) {
/* 1075 */         i = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void runWorker(Worker paramWorker)
/*      */   {
/* 1124 */     Thread localThread = Thread.currentThread();
/* 1125 */     Runnable localRunnable = paramWorker.firstTask;
/* 1126 */     paramWorker.firstTask = null;
/* 1127 */     paramWorker.unlock();
/* 1128 */     boolean bool = true;
/*      */     try {
/* 1130 */       while ((localRunnable != null) || ((localRunnable = getTask()) != null)) {
/* 1131 */         paramWorker.lock();
/*      */ 
/* 1136 */         if (((runStateAtLeast(this.ctl.get(), 536870912)) || ((Thread.interrupted()) && (runStateAtLeast(this.ctl.get(), 536870912)))) && (!localThread.isInterrupted()))
/*      */         {
/* 1140 */           localThread.interrupt();
/*      */         }try {
/* 1142 */           beforeExecute(localThread, localRunnable);
/* 1143 */           Object localObject1 = null;
/*      */           try {
/* 1145 */             localRunnable.run();
/*      */           } catch (RuntimeException localRuntimeException) {
/* 1147 */             localObject1 = localRuntimeException; throw localRuntimeException;
/*      */           } catch (Error localError) {
/* 1149 */             localObject1 = localError; throw localError;
/*      */           } catch (Throwable localThrowable) {
/* 1151 */             localObject1 = localThrowable; throw new Error(localThrowable);
/*      */           } finally {
/* 1153 */             afterExecute(localRunnable, (Throwable)localObject1);
/*      */           }
/*      */         } finally {
/* 1156 */           localRunnable = null;
/* 1157 */           paramWorker.completedTasks += 1L;
/* 1158 */           paramWorker.unlock();
/*      */         }
/*      */       }
/* 1161 */       bool = false;
/*      */     } finally {
/* 1163 */       processWorkerExit(paramWorker, bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ThreadPoolExecutor(int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, BlockingQueue<Runnable> paramBlockingQueue)
/*      */   {
/* 1198 */     this(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, Executors.defaultThreadFactory(), defaultHandler);
/*      */   }
/*      */ 
/*      */   public ThreadPoolExecutor(int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, BlockingQueue<Runnable> paramBlockingQueue, ThreadFactory paramThreadFactory)
/*      */   {
/* 1233 */     this(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramThreadFactory, defaultHandler);
/*      */   }
/*      */ 
/*      */   public ThreadPoolExecutor(int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, BlockingQueue<Runnable> paramBlockingQueue, RejectedExecutionHandler paramRejectedExecutionHandler)
/*      */   {
/* 1268 */     this(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, Executors.defaultThreadFactory(), paramRejectedExecutionHandler);
/*      */   }
/*      */ 
/*      */   public ThreadPoolExecutor(int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit, BlockingQueue<Runnable> paramBlockingQueue, ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler)
/*      */   {
/* 1306 */     if ((paramInt1 < 0) || (paramInt2 <= 0) || (paramInt2 < paramInt1) || (paramLong < 0L))
/*      */     {
/* 1310 */       throw new IllegalArgumentException();
/* 1311 */     }if ((paramBlockingQueue == null) || (paramThreadFactory == null) || (paramRejectedExecutionHandler == null))
/* 1312 */       throw new NullPointerException();
/* 1313 */     this.corePoolSize = paramInt1;
/* 1314 */     this.maximumPoolSize = paramInt2;
/* 1315 */     this.workQueue = paramBlockingQueue;
/* 1316 */     this.keepAliveTime = paramTimeUnit.toNanos(paramLong);
/* 1317 */     this.threadFactory = paramThreadFactory;
/* 1318 */     this.handler = paramRejectedExecutionHandler;
/*      */   }
/*      */ 
/*      */   public void execute(Runnable paramRunnable)
/*      */   {
/* 1336 */     if (paramRunnable == null) {
/* 1337 */       throw new NullPointerException();
/*      */     }
/*      */ 
/* 1358 */     int i = this.ctl.get();
/* 1359 */     if (workerCountOf(i) < this.corePoolSize) {
/* 1360 */       if (addWorker(paramRunnable, true))
/* 1361 */         return;
/* 1362 */       i = this.ctl.get();
/*      */     }
/* 1364 */     if ((isRunning(i)) && (this.workQueue.offer(paramRunnable))) {
/* 1365 */       int j = this.ctl.get();
/* 1366 */       if ((!isRunning(j)) && (remove(paramRunnable)))
/* 1367 */         reject(paramRunnable);
/* 1368 */       else if (workerCountOf(j) == 0)
/* 1369 */         addWorker(null, false);
/*      */     }
/* 1371 */     else if (!addWorker(paramRunnable, false)) {
/* 1372 */       reject(paramRunnable);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void shutdown()
/*      */   {
/* 1387 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1388 */     localReentrantLock.lock();
/*      */     try {
/* 1390 */       checkShutdownAccess();
/* 1391 */       advanceRunState(0);
/* 1392 */       interruptIdleWorkers();
/* 1393 */       onShutdown();
/*      */     } finally {
/* 1395 */       localReentrantLock.unlock();
/*      */     }
/* 1397 */     tryTerminate();
/*      */   }
/*      */ 
/*      */   public List<Runnable> shutdownNow()
/*      */   {
/* 1419 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1420 */     localReentrantLock.lock();
/*      */     List localList;
/*      */     try
/*      */     {
/* 1422 */       checkShutdownAccess();
/* 1423 */       advanceRunState(536870912);
/* 1424 */       interruptWorkers();
/* 1425 */       localList = drainQueue();
/*      */     } finally {
/* 1427 */       localReentrantLock.unlock();
/*      */     }
/* 1429 */     tryTerminate();
/* 1430 */     return localList;
/*      */   }
/*      */ 
/*      */   public boolean isShutdown() {
/* 1434 */     return !isRunning(this.ctl.get());
/*      */   }
/*      */ 
/*      */   public boolean isTerminating()
/*      */   {
/* 1449 */     int i = this.ctl.get();
/* 1450 */     return (!isRunning(i)) && (runStateLessThan(i, 1610612736));
/*      */   }
/*      */ 
/*      */   public boolean isTerminated() {
/* 1454 */     return runStateAtLeast(this.ctl.get(), 1610612736);
/*      */   }
/*      */ 
/*      */   public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*      */   {
/* 1459 */     long l = paramTimeUnit.toNanos(paramLong);
/* 1460 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1461 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*      */       while (true)
/*      */       {
/*      */         boolean bool;
/* 1464 */         if (runStateAtLeast(this.ctl.get(), 1610612736))
/* 1465 */           return true;
/* 1466 */         if (l <= 0L)
/* 1467 */           return false;
/* 1468 */         l = this.termination.awaitNanos(l);
/*      */       }
/*      */     } finally {
/* 1471 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/* 1480 */     shutdown();
/*      */   }
/*      */ 
/*      */   public void setThreadFactory(ThreadFactory paramThreadFactory)
/*      */   {
/* 1491 */     if (paramThreadFactory == null)
/* 1492 */       throw new NullPointerException();
/* 1493 */     this.threadFactory = paramThreadFactory;
/*      */   }
/*      */ 
/*      */   public ThreadFactory getThreadFactory()
/*      */   {
/* 1503 */     return this.threadFactory;
/*      */   }
/*      */ 
/*      */   public void setRejectedExecutionHandler(RejectedExecutionHandler paramRejectedExecutionHandler)
/*      */   {
/* 1514 */     if (paramRejectedExecutionHandler == null)
/* 1515 */       throw new NullPointerException();
/* 1516 */     this.handler = paramRejectedExecutionHandler;
/*      */   }
/*      */ 
/*      */   public RejectedExecutionHandler getRejectedExecutionHandler()
/*      */   {
/* 1526 */     return this.handler;
/*      */   }
/*      */ 
/*      */   public void setCorePoolSize(int paramInt)
/*      */   {
/* 1541 */     if (paramInt < 0)
/* 1542 */       throw new IllegalArgumentException();
/* 1543 */     int i = paramInt - this.corePoolSize;
/* 1544 */     this.corePoolSize = paramInt;
/* 1545 */     if (workerCountOf(this.ctl.get()) > paramInt) {
/* 1546 */       interruptIdleWorkers();
/* 1547 */     } else if (i > 0)
/*      */     {
/* 1552 */       int j = Math.min(i, this.workQueue.size());
/* 1553 */       while ((j-- > 0) && (addWorker(null, true)))
/* 1554 */         if (this.workQueue.isEmpty())
/* 1555 */           break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getCorePoolSize()
/*      */   {
/* 1567 */     return this.corePoolSize;
/*      */   }
/*      */ 
/*      */   public boolean prestartCoreThread()
/*      */   {
/* 1579 */     return (workerCountOf(this.ctl.get()) < this.corePoolSize) && (addWorker(null, true));
/*      */   }
/*      */ 
/*      */   void ensurePrestart()
/*      */   {
/* 1588 */     int i = workerCountOf(this.ctl.get());
/* 1589 */     if (i < this.corePoolSize)
/* 1590 */       addWorker(null, true);
/* 1591 */     else if (i == 0)
/* 1592 */       addWorker(null, false);
/*      */   }
/*      */ 
/*      */   public int prestartAllCoreThreads()
/*      */   {
/* 1603 */     int i = 0;
/* 1604 */     while (addWorker(null, true))
/* 1605 */       i++;
/* 1606 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean allowsCoreThreadTimeOut()
/*      */   {
/* 1623 */     return this.allowCoreThreadTimeOut;
/*      */   }
/*      */ 
/*      */   public void allowCoreThreadTimeOut(boolean paramBoolean)
/*      */   {
/* 1644 */     if ((paramBoolean) && (this.keepAliveTime <= 0L))
/* 1645 */       throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
/* 1646 */     if (paramBoolean != this.allowCoreThreadTimeOut) {
/* 1647 */       this.allowCoreThreadTimeOut = paramBoolean;
/* 1648 */       if (paramBoolean)
/* 1649 */         interruptIdleWorkers();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMaximumPoolSize(int paramInt)
/*      */   {
/* 1666 */     if ((paramInt <= 0) || (paramInt < this.corePoolSize))
/* 1667 */       throw new IllegalArgumentException();
/* 1668 */     this.maximumPoolSize = paramInt;
/* 1669 */     if (workerCountOf(this.ctl.get()) > paramInt)
/* 1670 */       interruptIdleWorkers();
/*      */   }
/*      */ 
/*      */   public int getMaximumPoolSize()
/*      */   {
/* 1680 */     return this.maximumPoolSize;
/*      */   }
/*      */ 
/*      */   public void setKeepAliveTime(long paramLong, TimeUnit paramTimeUnit)
/*      */   {
/* 1698 */     if (paramLong < 0L)
/* 1699 */       throw new IllegalArgumentException();
/* 1700 */     if ((paramLong == 0L) && (allowsCoreThreadTimeOut()))
/* 1701 */       throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
/* 1702 */     long l1 = paramTimeUnit.toNanos(paramLong);
/* 1703 */     long l2 = l1 - this.keepAliveTime;
/* 1704 */     this.keepAliveTime = l1;
/* 1705 */     if (l2 < 0L)
/* 1706 */       interruptIdleWorkers();
/*      */   }
/*      */ 
/*      */   public long getKeepAliveTime(TimeUnit paramTimeUnit)
/*      */   {
/* 1719 */     return paramTimeUnit.convert(this.keepAliveTime, TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */   public BlockingQueue<Runnable> getQueue()
/*      */   {
/* 1733 */     return this.workQueue;
/*      */   }
/*      */ 
/*      */   public boolean remove(Runnable paramRunnable)
/*      */   {
/* 1753 */     boolean bool = this.workQueue.remove(paramRunnable);
/* 1754 */     tryTerminate();
/* 1755 */     return bool;
/*      */   }
/*      */ 
/*      */   public void purge()
/*      */   {
/* 1769 */     BlockingQueue localBlockingQueue = this.workQueue;
/*      */     Object localObject1;
/*      */     int i;
/*      */     int j;
/*      */     try
/*      */     {
/* 1771 */       Iterator localIterator = localBlockingQueue.iterator();
/* 1772 */       while (localIterator.hasNext()) {
/* 1773 */         localObject1 = (Runnable)localIterator.next();
/* 1774 */         if (((localObject1 instanceof Future)) && (((Future)localObject1).isCancelled())) {
/* 1775 */           localIterator.remove();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ConcurrentModificationException localConcurrentModificationException)
/*      */     {
/* 1781 */       localObject1 = localBlockingQueue.toArray(); i = localObject1.length; j = 0; } for (; j < i; j++) { Object localObject2 = localObject1[j];
/* 1782 */       if (((localObject2 instanceof Future)) && (((Future)localObject2).isCancelled())) {
/* 1783 */         localBlockingQueue.remove(localObject2);
/*      */       }
/*      */     }
/* 1786 */     tryTerminate();
/*      */   }
/*      */ 
/*      */   public int getPoolSize()
/*      */   {
/* 1797 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1798 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/* 1802 */       return runStateAtLeast(this.ctl.get(), 1073741824) ? 0 : this.workers.size();
/*      */     }
/*      */     finally {
/* 1805 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getActiveCount()
/*      */   {
/* 1816 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1817 */     localReentrantLock.lock();
/*      */     try {
/* 1819 */       Iterator localIterator1 = 0;
/* 1820 */       for (Worker localWorker : this.workers)
/* 1821 */         if (localWorker.isLocked())
/* 1822 */           localIterator1++;
/* 1823 */       return localIterator1;
/*      */     } finally {
/* 1825 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getLargestPoolSize()
/*      */   {
/* 1836 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1837 */     localReentrantLock.lock();
/*      */     try {
/* 1839 */       return this.largestPoolSize;
/*      */     } finally {
/* 1841 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public long getTaskCount()
/*      */   {
/* 1854 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1855 */     localReentrantLock.lock();
/*      */     try {
/* 1857 */       long l1 = this.completedTaskCount;
/* 1858 */       for (Worker localWorker : this.workers) {
/* 1859 */         l1 += localWorker.completedTasks;
/* 1860 */         if (localWorker.isLocked())
/* 1861 */           l1 += 1L;
/*      */       }
/* 1863 */       return l1 + this.workQueue.size();
/*      */     } finally {
/* 1865 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public long getCompletedTaskCount()
/*      */   {
/* 1879 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1880 */     localReentrantLock.lock();
/*      */     try {
/* 1882 */       long l1 = this.completedTaskCount;
/* 1883 */       for (Worker localWorker : this.workers)
/* 1884 */         l1 += localWorker.completedTasks;
/* 1885 */       return l1;
/*      */     } finally {
/* 1887 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1901 */     ReentrantLock localReentrantLock = this.mainLock;
/* 1902 */     localReentrantLock.lock();
/*      */     long l;
/*      */     int j;
/*      */     int i;
/*      */     try
/*      */     {
/* 1904 */       l = this.completedTaskCount;
/* 1905 */       j = 0;
/* 1906 */       i = this.workers.size();
/* 1907 */       for (localIterator = this.workers.iterator(); localIterator.hasNext(); ) { localObject1 = (Worker)localIterator.next();
/* 1908 */         l += ((Worker)localObject1).completedTasks;
/* 1909 */         if (((Worker)localObject1).isLocked())
/* 1910 */           j++;
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*      */       Iterator localIterator;
/* 1913 */       localReentrantLock.unlock();
/*      */     }
/* 1915 */     int k = this.ctl.get();
/* 1916 */     Object localObject1 = runStateAtLeast(k, 1610612736) ? "Terminated" : runStateLessThan(k, 0) ? "Running" : "Shutting down";
/*      */ 
/* 1919 */     return super.toString() + "[" + (String)localObject1 + ", pool size = " + i + ", active threads = " + j + ", queued tasks = " + this.workQueue.size() + ", completed tasks = " + l + "]";
/*      */   }
/*      */ 
/*      */   protected void beforeExecute(Thread paramThread, Runnable paramRunnable)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void afterExecute(Runnable paramRunnable, Throwable paramThrowable)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void terminated()
/*      */   {
/*      */   }
/*      */ 
/*      */   public static class AbortPolicy
/*      */     implements RejectedExecutionHandler
/*      */   {
/*      */     public void rejectedExecution(Runnable paramRunnable, ThreadPoolExecutor paramThreadPoolExecutor)
/*      */     {
/* 2048 */       throw new RejectedExecutionException("Task " + paramRunnable.toString() + " rejected from " + paramThreadPoolExecutor.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CallerRunsPolicy
/*      */     implements RejectedExecutionHandler
/*      */   {
/*      */     public void rejectedExecution(Runnable paramRunnable, ThreadPoolExecutor paramThreadPoolExecutor)
/*      */     {
/* 2024 */       if (!paramThreadPoolExecutor.isShutdown())
/* 2025 */         paramRunnable.run();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DiscardOldestPolicy
/*      */     implements RejectedExecutionHandler
/*      */   {
/*      */     public void rejectedExecution(Runnable paramRunnable, ThreadPoolExecutor paramThreadPoolExecutor)
/*      */     {
/* 2095 */       if (!paramThreadPoolExecutor.isShutdown()) {
/* 2096 */         paramThreadPoolExecutor.getQueue().poll();
/* 2097 */         paramThreadPoolExecutor.execute(paramRunnable);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DiscardPolicy
/*      */     implements RejectedExecutionHandler
/*      */   {
/*      */     public void rejectedExecution(Runnable paramRunnable, ThreadPoolExecutor paramThreadPoolExecutor)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class Worker extends AbstractQueuedSynchronizer
/*      */     implements Runnable
/*      */   {
/*      */     private static final long serialVersionUID = 6138294804551838833L;
/*      */     final Thread thread;
/*      */     Runnable firstTask;
/*      */     volatile long completedTasks;
/*      */ 
/*      */     Worker(Runnable arg2)
/*      */     {
/*  608 */       setState(-1);
/*      */       Object localObject;
/*  609 */       this.firstTask = localObject;
/*  610 */       this.thread = ThreadPoolExecutor.this.getThreadFactory().newThread(this);
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*  615 */       ThreadPoolExecutor.this.runWorker(this);
/*      */     }
/*      */ 
/*      */     protected boolean isHeldExclusively()
/*      */     {
/*  624 */       return getState() != 0;
/*      */     }
/*      */ 
/*      */     protected boolean tryAcquire(int paramInt) {
/*  628 */       if (compareAndSetState(0, 1)) {
/*  629 */         setExclusiveOwnerThread(Thread.currentThread());
/*  630 */         return true;
/*      */       }
/*  632 */       return false;
/*      */     }
/*      */ 
/*      */     protected boolean tryRelease(int paramInt) {
/*  636 */       setExclusiveOwnerThread(null);
/*  637 */       setState(0);
/*  638 */       return true;
/*      */     }
/*      */     public void lock() {
/*  641 */       acquire(1); } 
/*  642 */     public boolean tryLock() { return tryAcquire(1); } 
/*  643 */     public void unlock() { release(1); } 
/*  644 */     public boolean isLocked() { return isHeldExclusively(); }
/*      */ 
/*      */ 
/*      */     void interruptIfStarted()
/*      */     {
/*      */       Thread localThread;
/*  648 */       if ((getState() >= 0) && ((localThread = this.thread) != null) && (!localThread.isInterrupted()))
/*      */         try {
/*  650 */           localThread.interrupt();
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ThreadPoolExecutor
 * JD-Core Version:    0.6.2
 */