/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor
/*      */   implements ScheduledExecutorService
/*      */ {
/*      */   private volatile boolean continueExistingPeriodicTasksAfterShutdown;
/*  158 */   private volatile boolean executeExistingDelayedTasksAfterShutdown = true;
/*      */ 
/*  163 */   private volatile boolean removeOnCancel = false;
/*      */ 
/*  169 */   private static final AtomicLong sequencer = new AtomicLong(0L);
/*      */ 
/*      */   final long now()
/*      */   {
/*  175 */     return System.nanoTime();
/*      */   }
/*      */ 
/*      */   boolean canRunInCurrentRunState(boolean paramBoolean)
/*      */   {
/*  307 */     return isRunningOrShutdown(paramBoolean ? this.continueExistingPeriodicTasksAfterShutdown : this.executeExistingDelayedTasksAfterShutdown);
/*      */   }
/*      */ 
/*      */   private void delayedExecute(RunnableScheduledFuture<?> paramRunnableScheduledFuture)
/*      */   {
/*  324 */     if (isShutdown()) {
/*  325 */       reject(paramRunnableScheduledFuture);
/*      */     } else {
/*  327 */       super.getQueue().add(paramRunnableScheduledFuture);
/*  328 */       if ((isShutdown()) && (!canRunInCurrentRunState(paramRunnableScheduledFuture.isPeriodic())) && (remove(paramRunnableScheduledFuture)))
/*      */       {
/*  331 */         paramRunnableScheduledFuture.cancel(false);
/*      */       }
/*  333 */       else ensurePrestart();
/*      */     }
/*      */   }
/*      */ 
/*      */   void reExecutePeriodic(RunnableScheduledFuture<?> paramRunnableScheduledFuture)
/*      */   {
/*  344 */     if (canRunInCurrentRunState(true)) {
/*  345 */       super.getQueue().add(paramRunnableScheduledFuture);
/*  346 */       if ((!canRunInCurrentRunState(true)) && (remove(paramRunnableScheduledFuture)))
/*  347 */         paramRunnableScheduledFuture.cancel(false);
/*      */       else
/*  349 */         ensurePrestart();
/*      */     }
/*      */   }
/*      */ 
/*      */   void onShutdown()
/*      */   {
/*  358 */     BlockingQueue localBlockingQueue = super.getQueue();
/*  359 */     boolean bool1 = getExecuteExistingDelayedTasksAfterShutdownPolicy();
/*      */ 
/*  361 */     boolean bool2 = getContinueExistingPeriodicTasksAfterShutdownPolicy();
/*      */     Object localObject;
/*  363 */     if ((!bool1) && (!bool2)) {
/*  364 */       for (localObject : localBlockingQueue.toArray())
/*  365 */         if ((localObject instanceof RunnableScheduledFuture))
/*  366 */           ((RunnableScheduledFuture)localObject).cancel(false);
/*  367 */       localBlockingQueue.clear();
/*      */     }
/*      */     else
/*      */     {
/*  371 */       for (localObject : localBlockingQueue.toArray()) {
/*  372 */         if ((localObject instanceof RunnableScheduledFuture)) {
/*  373 */           RunnableScheduledFuture localRunnableScheduledFuture = (RunnableScheduledFuture)localObject;
/*      */ 
/*  375 */           if (localRunnableScheduledFuture.isPeriodic() ? bool2 : bool1) { if (!localRunnableScheduledFuture.isCancelled()); }
/*  377 */           else if (localBlockingQueue.remove(localRunnableScheduledFuture)) {
/*  378 */             localRunnableScheduledFuture.cancel(false);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  383 */     tryTerminate();
/*      */   }
/*      */ 
/*      */   protected <V> RunnableScheduledFuture<V> decorateTask(Runnable paramRunnable, RunnableScheduledFuture<V> paramRunnableScheduledFuture)
/*      */   {
/*  399 */     return paramRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> paramCallable, RunnableScheduledFuture<V> paramRunnableScheduledFuture)
/*      */   {
/*  415 */     return paramRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   public ScheduledThreadPoolExecutor(int paramInt)
/*      */   {
/*  427 */     super(paramInt, 2147483647, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue());
/*      */   }
/*      */ 
/*      */   public ScheduledThreadPoolExecutor(int paramInt, ThreadFactory paramThreadFactory)
/*      */   {
/*  444 */     super(paramInt, 2147483647, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), paramThreadFactory);
/*      */   }
/*      */ 
/*      */   public ScheduledThreadPoolExecutor(int paramInt, RejectedExecutionHandler paramRejectedExecutionHandler)
/*      */   {
/*  461 */     super(paramInt, 2147483647, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), paramRejectedExecutionHandler);
/*      */   }
/*      */ 
/*      */   public ScheduledThreadPoolExecutor(int paramInt, ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler)
/*      */   {
/*  482 */     super(paramInt, 2147483647, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), paramThreadFactory, paramRejectedExecutionHandler);
/*      */   }
/*      */ 
/*      */   private long triggerTime(long paramLong, TimeUnit paramTimeUnit)
/*      */   {
/*  490 */     return triggerTime(paramTimeUnit.toNanos(paramLong < 0L ? 0L : paramLong));
/*      */   }
/*      */ 
/*      */   long triggerTime(long paramLong)
/*      */   {
/*  497 */     return now() + (paramLong < 4611686018427387903L ? paramLong : overflowFree(paramLong));
/*      */   }
/*      */ 
/*      */   private long overflowFree(long paramLong)
/*      */   {
/*  509 */     Delayed localDelayed = (Delayed)super.getQueue().peek();
/*  510 */     if (localDelayed != null) {
/*  511 */       long l = localDelayed.getDelay(TimeUnit.NANOSECONDS);
/*  512 */       if ((l < 0L) && (paramLong - l < 0L))
/*  513 */         paramLong = 9223372036854775807L + l;
/*      */     }
/*  515 */     return paramLong;
/*      */   }
/*      */ 
/*      */   public ScheduledFuture<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit)
/*      */   {
/*  525 */     if ((paramRunnable == null) || (paramTimeUnit == null))
/*  526 */       throw new NullPointerException();
/*  527 */     RunnableScheduledFuture localRunnableScheduledFuture = decorateTask(paramRunnable, new ScheduledFutureTask(paramRunnable, null, triggerTime(paramLong, paramTimeUnit)));
/*      */ 
/*  530 */     delayedExecute(localRunnableScheduledFuture);
/*  531 */     return localRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   public <V> ScheduledFuture<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit)
/*      */   {
/*  541 */     if ((paramCallable == null) || (paramTimeUnit == null))
/*  542 */       throw new NullPointerException();
/*  543 */     RunnableScheduledFuture localRunnableScheduledFuture = decorateTask(paramCallable, new ScheduledFutureTask(paramCallable, triggerTime(paramLong, paramTimeUnit)));
/*      */ 
/*  546 */     delayedExecute(localRunnableScheduledFuture);
/*  547 */     return localRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit)
/*      */   {
/*  559 */     if ((paramRunnable == null) || (paramTimeUnit == null))
/*  560 */       throw new NullPointerException();
/*  561 */     if (paramLong2 <= 0L)
/*  562 */       throw new IllegalArgumentException();
/*  563 */     ScheduledFutureTask localScheduledFutureTask = new ScheduledFutureTask(paramRunnable, null, triggerTime(paramLong1, paramTimeUnit), paramTimeUnit.toNanos(paramLong2));
/*      */ 
/*  568 */     RunnableScheduledFuture localRunnableScheduledFuture = decorateTask(paramRunnable, localScheduledFutureTask);
/*  569 */     localScheduledFutureTask.outerTask = localRunnableScheduledFuture;
/*  570 */     delayedExecute(localRunnableScheduledFuture);
/*  571 */     return localRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit)
/*      */   {
/*  583 */     if ((paramRunnable == null) || (paramTimeUnit == null))
/*  584 */       throw new NullPointerException();
/*  585 */     if (paramLong2 <= 0L)
/*  586 */       throw new IllegalArgumentException();
/*  587 */     ScheduledFutureTask localScheduledFutureTask = new ScheduledFutureTask(paramRunnable, null, triggerTime(paramLong1, paramTimeUnit), paramTimeUnit.toNanos(-paramLong2));
/*      */ 
/*  592 */     RunnableScheduledFuture localRunnableScheduledFuture = decorateTask(paramRunnable, localScheduledFutureTask);
/*  593 */     localScheduledFutureTask.outerTask = localRunnableScheduledFuture;
/*  594 */     delayedExecute(localRunnableScheduledFuture);
/*  595 */     return localRunnableScheduledFuture;
/*      */   }
/*      */ 
/*      */   public void execute(Runnable paramRunnable)
/*      */   {
/*  619 */     schedule(paramRunnable, 0L, TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */   public Future<?> submit(Runnable paramRunnable)
/*      */   {
/*  629 */     return schedule(paramRunnable, 0L, TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */   public <T> Future<T> submit(Runnable paramRunnable, T paramT)
/*      */   {
/*  637 */     return schedule(Executors.callable(paramRunnable, paramT), 0L, TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */   public <T> Future<T> submit(Callable<T> paramCallable)
/*      */   {
/*  646 */     return schedule(paramCallable, 0L, TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */   public void setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean paramBoolean)
/*      */   {
/*  661 */     this.continueExistingPeriodicTasksAfterShutdown = paramBoolean;
/*  662 */     if ((!paramBoolean) && (isShutdown()))
/*  663 */       onShutdown();
/*      */   }
/*      */ 
/*      */   public boolean getContinueExistingPeriodicTasksAfterShutdownPolicy()
/*      */   {
/*  678 */     return this.continueExistingPeriodicTasksAfterShutdown;
/*      */   }
/*      */ 
/*      */   public void setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean paramBoolean)
/*      */   {
/*  693 */     this.executeExistingDelayedTasksAfterShutdown = paramBoolean;
/*  694 */     if ((!paramBoolean) && (isShutdown()))
/*  695 */       onShutdown();
/*      */   }
/*      */ 
/*      */   public boolean getExecuteExistingDelayedTasksAfterShutdownPolicy()
/*      */   {
/*  710 */     return this.executeExistingDelayedTasksAfterShutdown;
/*      */   }
/*      */ 
/*      */   public void setRemoveOnCancelPolicy(boolean paramBoolean)
/*      */   {
/*  723 */     this.removeOnCancel = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getRemoveOnCancelPolicy()
/*      */   {
/*  737 */     return this.removeOnCancel;
/*      */   }
/*      */ 
/*      */   public void shutdown()
/*      */   {
/*  759 */     super.shutdown();
/*      */   }
/*      */ 
/*      */   public List<Runnable> shutdownNow()
/*      */   {
/*  784 */     return super.shutdownNow();
/*      */   }
/*      */ 
/*      */   public BlockingQueue<Runnable> getQueue()
/*      */   {
/*  799 */     return super.getQueue();
/*      */   }
/*      */ 
/*      */   static class DelayedWorkQueue extends AbstractQueue<Runnable>
/*      */     implements BlockingQueue<Runnable>
/*      */   {
/*      */     private static final int INITIAL_CAPACITY = 16;
/*      */     private RunnableScheduledFuture[] queue;
/*      */     private final ReentrantLock lock;
/*      */     private int size;
/*      */     private Thread leader;
/*      */     private final Condition available;
/*      */ 
/*      */     DelayedWorkQueue()
/*      */     {
/*  834 */       this.queue = new RunnableScheduledFuture[16];
/*      */ 
/*  836 */       this.lock = new ReentrantLock();
/*  837 */       this.size = 0;
/*      */ 
/*  855 */       this.leader = null;
/*      */ 
/*  861 */       this.available = this.lock.newCondition();
/*      */     }
/*      */ 
/*      */     private void setIndex(RunnableScheduledFuture paramRunnableScheduledFuture, int paramInt)
/*      */     {
/*  867 */       if ((paramRunnableScheduledFuture instanceof ScheduledThreadPoolExecutor.ScheduledFutureTask))
/*  868 */         ((ScheduledThreadPoolExecutor.ScheduledFutureTask)paramRunnableScheduledFuture).heapIndex = paramInt;
/*      */     }
/*      */ 
/*      */     private void siftUp(int paramInt, RunnableScheduledFuture paramRunnableScheduledFuture)
/*      */     {
/*  876 */       while (paramInt > 0) {
/*  877 */         int i = paramInt - 1 >>> 1;
/*  878 */         RunnableScheduledFuture localRunnableScheduledFuture = this.queue[i];
/*  879 */         if (paramRunnableScheduledFuture.compareTo(localRunnableScheduledFuture) >= 0)
/*      */           break;
/*  881 */         this.queue[paramInt] = localRunnableScheduledFuture;
/*  882 */         setIndex(localRunnableScheduledFuture, paramInt);
/*  883 */         paramInt = i;
/*      */       }
/*  885 */       this.queue[paramInt] = paramRunnableScheduledFuture;
/*  886 */       setIndex(paramRunnableScheduledFuture, paramInt);
/*      */     }
/*      */ 
/*      */     private void siftDown(int paramInt, RunnableScheduledFuture paramRunnableScheduledFuture)
/*      */     {
/*  894 */       int i = this.size >>> 1;
/*  895 */       while (paramInt < i) {
/*  896 */         int j = (paramInt << 1) + 1;
/*  897 */         RunnableScheduledFuture localRunnableScheduledFuture = this.queue[j];
/*  898 */         int k = j + 1;
/*  899 */         if ((k < this.size) && (localRunnableScheduledFuture.compareTo(this.queue[k]) > 0))
/*  900 */           localRunnableScheduledFuture = this.queue[(j = k)];
/*  901 */         if (paramRunnableScheduledFuture.compareTo(localRunnableScheduledFuture) <= 0)
/*      */           break;
/*  903 */         this.queue[paramInt] = localRunnableScheduledFuture;
/*  904 */         setIndex(localRunnableScheduledFuture, paramInt);
/*  905 */         paramInt = j;
/*      */       }
/*  907 */       this.queue[paramInt] = paramRunnableScheduledFuture;
/*  908 */       setIndex(paramRunnableScheduledFuture, paramInt);
/*      */     }
/*      */ 
/*      */     private void grow()
/*      */     {
/*  915 */       int i = this.queue.length;
/*  916 */       int j = i + (i >> 1);
/*  917 */       if (j < 0)
/*  918 */         j = 2147483647;
/*  919 */       this.queue = ((RunnableScheduledFuture[])Arrays.copyOf(this.queue, j));
/*      */     }
/*      */ 
/*      */     private int indexOf(Object paramObject)
/*      */     {
/*  926 */       if (paramObject != null)
/*      */       {
/*      */         int i;
/*  927 */         if ((paramObject instanceof ScheduledThreadPoolExecutor.ScheduledFutureTask)) {
/*  928 */           i = ((ScheduledThreadPoolExecutor.ScheduledFutureTask)paramObject).heapIndex;
/*      */ 
/*  931 */           if ((i >= 0) && (i < this.size) && (this.queue[i] == paramObject))
/*  932 */             return i;
/*      */         } else {
/*  934 */           for (i = 0; i < this.size; i++)
/*  935 */             if (paramObject.equals(this.queue[i]))
/*  936 */               return i;
/*      */         }
/*      */       }
/*  939 */       return -1;
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/*  943 */       ReentrantLock localReentrantLock = this.lock;
/*  944 */       localReentrantLock.lock();
/*      */       try {
/*  946 */         return indexOf(paramObject) != -1;
/*      */       } finally {
/*  948 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/*  953 */       ReentrantLock localReentrantLock = this.lock;
/*  954 */       localReentrantLock.lock();
/*      */       try {
/*  956 */         int i = indexOf(paramObject);
/*  957 */         if (i < 0) {
/*  958 */           return false;
/*      */         }
/*  960 */         setIndex(this.queue[i], -1);
/*  961 */         int j = --this.size;
/*  962 */         RunnableScheduledFuture localRunnableScheduledFuture = this.queue[j];
/*  963 */         this.queue[j] = null;
/*  964 */         if (j != i) {
/*  965 */           siftDown(i, localRunnableScheduledFuture);
/*  966 */           if (this.queue[i] == localRunnableScheduledFuture)
/*  967 */             siftUp(i, localRunnableScheduledFuture);
/*      */         }
/*  969 */         return true;
/*      */       } finally {
/*  971 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public int size() {
/*  976 */       ReentrantLock localReentrantLock = this.lock;
/*  977 */       localReentrantLock.lock();
/*      */       try {
/*  979 */         return this.size;
/*      */       } finally {
/*  981 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isEmpty() {
/*  986 */       return size() == 0;
/*      */     }
/*      */ 
/*      */     public int remainingCapacity() {
/*  990 */       return 2147483647;
/*      */     }
/*      */ 
/*      */     public RunnableScheduledFuture peek() {
/*  994 */       ReentrantLock localReentrantLock = this.lock;
/*  995 */       localReentrantLock.lock();
/*      */       try {
/*  997 */         return this.queue[0];
/*      */       } finally {
/*  999 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean offer(Runnable paramRunnable) {
/* 1004 */       if (paramRunnable == null)
/* 1005 */         throw new NullPointerException();
/* 1006 */       RunnableScheduledFuture localRunnableScheduledFuture = (RunnableScheduledFuture)paramRunnable;
/* 1007 */       ReentrantLock localReentrantLock = this.lock;
/* 1008 */       localReentrantLock.lock();
/*      */       try {
/* 1010 */         int i = this.size;
/* 1011 */         if (i >= this.queue.length)
/* 1012 */           grow();
/* 1013 */         this.size = (i + 1);
/* 1014 */         if (i == 0) {
/* 1015 */           this.queue[0] = localRunnableScheduledFuture;
/* 1016 */           setIndex(localRunnableScheduledFuture, 0);
/*      */         } else {
/* 1018 */           siftUp(i, localRunnableScheduledFuture);
/*      */         }
/* 1020 */         if (this.queue[0] == localRunnableScheduledFuture) {
/* 1021 */           this.leader = null;
/* 1022 */           this.available.signal();
/*      */         }
/*      */       } finally {
/* 1025 */         localReentrantLock.unlock();
/*      */       }
/* 1027 */       return true;
/*      */     }
/*      */ 
/*      */     public void put(Runnable paramRunnable) {
/* 1031 */       offer(paramRunnable);
/*      */     }
/*      */ 
/*      */     public boolean add(Runnable paramRunnable) {
/* 1035 */       return offer(paramRunnable);
/*      */     }
/*      */ 
/*      */     public boolean offer(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit) {
/* 1039 */       return offer(paramRunnable);
/*      */     }
/*      */ 
/*      */     private RunnableScheduledFuture finishPoll(RunnableScheduledFuture paramRunnableScheduledFuture)
/*      */     {
/* 1049 */       int i = --this.size;
/* 1050 */       RunnableScheduledFuture localRunnableScheduledFuture = this.queue[i];
/* 1051 */       this.queue[i] = null;
/* 1052 */       if (i != 0)
/* 1053 */         siftDown(0, localRunnableScheduledFuture);
/* 1054 */       setIndex(paramRunnableScheduledFuture, -1);
/* 1055 */       return paramRunnableScheduledFuture;
/*      */     }
/*      */ 
/*      */     public RunnableScheduledFuture poll() {
/* 1059 */       ReentrantLock localReentrantLock = this.lock;
/* 1060 */       localReentrantLock.lock();
/*      */       try {
/* 1062 */         RunnableScheduledFuture localRunnableScheduledFuture1 = this.queue[0];
/*      */         RunnableScheduledFuture localRunnableScheduledFuture2;
/* 1063 */         if ((localRunnableScheduledFuture1 == null) || (localRunnableScheduledFuture1.getDelay(TimeUnit.NANOSECONDS) > 0L)) {
/* 1064 */           return null;
/*      */         }
/* 1066 */         return finishPoll(localRunnableScheduledFuture1);
/*      */       } finally {
/* 1068 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public RunnableScheduledFuture take() throws InterruptedException {
/* 1073 */       ReentrantLock localReentrantLock = this.lock;
/* 1074 */       localReentrantLock.lockInterruptibly();
/*      */       try {
/*      */         while (true) {
/* 1077 */           RunnableScheduledFuture localRunnableScheduledFuture = this.queue[0];
/* 1078 */           if (localRunnableScheduledFuture == null) {
/* 1079 */             this.available.await();
/*      */           } else {
/* 1081 */             long l = localRunnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS);
/*      */             Object localObject1;
/* 1082 */             if (l <= 0L)
/* 1083 */               return finishPoll(localRunnableScheduledFuture);
/* 1084 */             if (this.leader != null) {
/* 1085 */               this.available.await();
/*      */             } else {
/* 1087 */               localObject1 = Thread.currentThread();
/* 1088 */               this.leader = ((Thread)localObject1);
/*      */               try {
/* 1090 */                 this.available.awaitNanos(l);
/*      */ 
/* 1092 */                 if (this.leader == localObject1)
/* 1093 */                   this.leader = null;
/*      */               }
/*      */               finally
/*      */               {
/* 1092 */                 if (this.leader == localObject1)
/* 1093 */                   this.leader = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } finally {
/* 1099 */         if ((this.leader == null) && (this.queue[0] != null))
/* 1100 */           this.available.signal();
/* 1101 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public RunnableScheduledFuture poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*      */     {
/* 1107 */       long l1 = paramTimeUnit.toNanos(paramLong);
/* 1108 */       ReentrantLock localReentrantLock = this.lock;
/* 1109 */       localReentrantLock.lockInterruptibly();
/*      */       try {
/*      */         while (true) {
/* 1112 */           RunnableScheduledFuture localRunnableScheduledFuture1 = this.queue[0];
/* 1113 */           if (localRunnableScheduledFuture1 == null) {
/* 1114 */             if (l1 <= 0L) {
/* 1115 */               return null;
/*      */             }
/* 1117 */             l1 = this.available.awaitNanos(l1);
/*      */           } else {
/* 1119 */             long l2 = localRunnableScheduledFuture1.getDelay(TimeUnit.NANOSECONDS);
/*      */             Object localObject1;
/* 1120 */             if (l2 <= 0L)
/* 1121 */               return finishPoll(localRunnableScheduledFuture1);
/* 1122 */             if (l1 <= 0L)
/* 1123 */               return null;
/* 1124 */             if ((l1 < l2) || (this.leader != null)) {
/* 1125 */               l1 = this.available.awaitNanos(l1);
/*      */             } else {
/* 1127 */               localObject1 = Thread.currentThread();
/* 1128 */               this.leader = ((Thread)localObject1);
/*      */               try {
/* 1130 */                 long l3 = this.available.awaitNanos(l2);
/* 1131 */                 l1 -= l2 - l3;
/*      */ 
/* 1133 */                 if (this.leader == localObject1)
/* 1134 */                   this.leader = null;
/*      */               }
/*      */               finally
/*      */               {
/* 1133 */                 if (this.leader == localObject1)
/* 1134 */                   this.leader = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       } finally {
/* 1140 */         if ((this.leader == null) && (this.queue[0] != null))
/* 1141 */           this.available.signal();
/* 1142 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1147 */       ReentrantLock localReentrantLock = this.lock;
/* 1148 */       localReentrantLock.lock();
/*      */       try {
/* 1150 */         for (int i = 0; i < this.size; i++) {
/* 1151 */           RunnableScheduledFuture localRunnableScheduledFuture = this.queue[i];
/* 1152 */           if (localRunnableScheduledFuture != null) {
/* 1153 */             this.queue[i] = null;
/* 1154 */             setIndex(localRunnableScheduledFuture, -1);
/*      */           }
/*      */         }
/* 1157 */         this.size = 0;
/*      */       } finally {
/* 1159 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     private RunnableScheduledFuture pollExpired()
/*      */     {
/* 1168 */       RunnableScheduledFuture localRunnableScheduledFuture = this.queue[0];
/* 1169 */       if ((localRunnableScheduledFuture == null) || (localRunnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS) > 0L))
/* 1170 */         return null;
/* 1171 */       return finishPoll(localRunnableScheduledFuture);
/*      */     }
/*      */ 
/*      */     public int drainTo(Collection<? super Runnable> paramCollection) {
/* 1175 */       if (paramCollection == null)
/* 1176 */         throw new NullPointerException();
/* 1177 */       if (paramCollection == this)
/* 1178 */         throw new IllegalArgumentException();
/* 1179 */       ReentrantLock localReentrantLock = this.lock;
/* 1180 */       localReentrantLock.lock();
/*      */       try
/*      */       {
/* 1183 */         int i = 0;
/*      */         RunnableScheduledFuture localRunnableScheduledFuture;
/* 1184 */         while ((localRunnableScheduledFuture = pollExpired()) != null) {
/* 1185 */           paramCollection.add(localRunnableScheduledFuture);
/* 1186 */           i++;
/*      */         }
/* 1188 */         return i;
/*      */       } finally {
/* 1190 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public int drainTo(Collection<? super Runnable> paramCollection, int paramInt) {
/* 1195 */       if (paramCollection == null)
/* 1196 */         throw new NullPointerException();
/* 1197 */       if (paramCollection == this)
/* 1198 */         throw new IllegalArgumentException();
/* 1199 */       if (paramInt <= 0)
/* 1200 */         return 0;
/* 1201 */       ReentrantLock localReentrantLock = this.lock;
/* 1202 */       localReentrantLock.lock();
/*      */       try
/*      */       {
/* 1205 */         int i = 0;
/*      */         RunnableScheduledFuture localRunnableScheduledFuture;
/* 1206 */         while ((i < paramInt) && ((localRunnableScheduledFuture = pollExpired()) != null)) {
/* 1207 */           paramCollection.add(localRunnableScheduledFuture);
/* 1208 */           i++;
/*      */         }
/* 1210 */         return i;
/*      */       } finally {
/* 1212 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object[] toArray() {
/* 1217 */       ReentrantLock localReentrantLock = this.lock;
/* 1218 */       localReentrantLock.lock();
/*      */       try {
/* 1220 */         return Arrays.copyOf(this.queue, this.size, [Ljava.lang.Object.class);
/*      */       } finally {
/* 1222 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT)
/*      */     {
/* 1228 */       ReentrantLock localReentrantLock = this.lock;
/* 1229 */       localReentrantLock.lock();
/*      */       try
/*      */       {
/*      */         Object localObject1;
/* 1231 */         if (paramArrayOfT.length < this.size)
/* 1232 */           return (Object[])Arrays.copyOf(this.queue, this.size, paramArrayOfT.getClass());
/* 1233 */         System.arraycopy(this.queue, 0, paramArrayOfT, 0, this.size);
/* 1234 */         if (paramArrayOfT.length > this.size)
/* 1235 */           paramArrayOfT[this.size] = null;
/* 1236 */         return paramArrayOfT;
/*      */       } finally {
/* 1238 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public Iterator<Runnable> iterator() {
/* 1243 */       return new Itr((RunnableScheduledFuture[])Arrays.copyOf(this.queue, this.size));
/*      */     }
/*      */ 
/*      */     private class Itr
/*      */       implements Iterator<Runnable>
/*      */     {
/*      */       final RunnableScheduledFuture[] array;
/* 1251 */       int cursor = 0;
/* 1252 */       int lastRet = -1;
/*      */ 
/*      */       Itr(RunnableScheduledFuture[] arg2)
/*      */       {
/*      */         Object localObject;
/* 1255 */         this.array = localObject;
/*      */       }
/*      */ 
/*      */       public boolean hasNext() {
/* 1259 */         return this.cursor < this.array.length;
/*      */       }
/*      */ 
/*      */       public Runnable next() {
/* 1263 */         if (this.cursor >= this.array.length)
/* 1264 */           throw new NoSuchElementException();
/* 1265 */         this.lastRet = this.cursor;
/* 1266 */         return this.array[(this.cursor++)];
/*      */       }
/*      */ 
/*      */       public void remove() {
/* 1270 */         if (this.lastRet < 0)
/* 1271 */           throw new IllegalStateException();
/* 1272 */         ScheduledThreadPoolExecutor.DelayedWorkQueue.this.remove(this.array[this.lastRet]);
/* 1273 */         this.lastRet = -1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ScheduledFutureTask<V> extends FutureTask<V>
/*      */     implements RunnableScheduledFuture<V>
/*      */   {
/*      */     private final long sequenceNumber;
/*      */     private long time;
/*      */     private final long period;
/*  196 */     RunnableScheduledFuture<V> outerTask = this;
/*      */     int heapIndex;
/*      */ 
/*      */     ScheduledFutureTask(V paramLong, long arg3)
/*      */     {
/*  207 */       super(localObject1);
/*      */       Object localObject2;
/*  208 */       this.time = localObject2;
/*  209 */       this.period = 0L;
/*  210 */       this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
/*      */     }
/*      */ 
/*      */     ScheduledFutureTask(V paramLong1, long arg3, long arg5)
/*      */     {
/*  217 */       super(???);
/*  218 */       this.time = paramLong2;
/*      */       Object localObject;
/*  219 */       this.period = localObject;
/*  220 */       this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
/*      */     }
/*      */ 
/*      */     ScheduledFutureTask(long arg2)
/*      */     {
/*  227 */       super();
/*      */       Object localObject;
/*  228 */       this.time = localObject;
/*  229 */       this.period = 0L;
/*  230 */       this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
/*      */     }
/*      */ 
/*      */     public long getDelay(TimeUnit paramTimeUnit) {
/*  234 */       return paramTimeUnit.convert(this.time - ScheduledThreadPoolExecutor.this.now(), TimeUnit.NANOSECONDS);
/*      */     }
/*      */ 
/*      */     public int compareTo(Delayed paramDelayed) {
/*  238 */       if (paramDelayed == this)
/*  239 */         return 0;
/*  240 */       if ((paramDelayed instanceof ScheduledFutureTask)) {
/*  241 */         ScheduledFutureTask localScheduledFutureTask = (ScheduledFutureTask)paramDelayed;
/*  242 */         long l2 = this.time - localScheduledFutureTask.time;
/*  243 */         if (l2 < 0L)
/*  244 */           return -1;
/*  245 */         if (l2 > 0L)
/*  246 */           return 1;
/*  247 */         if (this.sequenceNumber < localScheduledFutureTask.sequenceNumber) {
/*  248 */           return -1;
/*      */         }
/*  250 */         return 1;
/*      */       }
/*  252 */       long l1 = getDelay(TimeUnit.NANOSECONDS) - paramDelayed.getDelay(TimeUnit.NANOSECONDS);
/*      */ 
/*  254 */       return l1 < 0L ? -1 : l1 == 0L ? 0 : 1;
/*      */     }
/*      */ 
/*      */     public boolean isPeriodic()
/*      */     {
/*  263 */       return this.period != 0L;
/*      */     }
/*      */ 
/*      */     private void setNextRunTime()
/*      */     {
/*  270 */       long l = this.period;
/*  271 */       if (l > 0L)
/*  272 */         this.time += l;
/*      */       else
/*  274 */         this.time = ScheduledThreadPoolExecutor.this.triggerTime(-l);
/*      */     }
/*      */ 
/*      */     public boolean cancel(boolean paramBoolean) {
/*  278 */       boolean bool = super.cancel(paramBoolean);
/*  279 */       if ((bool) && (ScheduledThreadPoolExecutor.this.removeOnCancel) && (this.heapIndex >= 0))
/*  280 */         ScheduledThreadPoolExecutor.this.remove(this);
/*  281 */       return bool;
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*  288 */       boolean bool = isPeriodic();
/*  289 */       if (!ScheduledThreadPoolExecutor.this.canRunInCurrentRunState(bool)) {
/*  290 */         cancel(false);
/*  291 */       } else if (!bool) {
/*  292 */         super.run();
/*  293 */       } else if (super.runAndReset()) {
/*  294 */         setNextRunTime();
/*  295 */         ScheduledThreadPoolExecutor.this.reExecutePeriodic(this.outerTask);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ScheduledThreadPoolExecutor
 * JD-Core Version:    0.6.2
 */