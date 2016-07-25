/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public abstract class ForkJoinTask<V>
/*      */   implements Future<V>, Serializable
/*      */ {
/*      */   volatile int status;
/*      */   private static final int NORMAL = -1;
/*      */   private static final int CANCELLED = -2;
/*      */   private static final int EXCEPTIONAL = -3;
/*      */   private static final int SIGNAL = 1;
/*      */   private static final ExceptionNode[] exceptionTable;
/* 1374 */   private static final ReentrantLock exceptionTableLock = new ReentrantLock();
/* 1375 */   private static final ReferenceQueue<Object> exceptionTableRefQueue = new ReferenceQueue();
/*      */   private static final int EXCEPTION_MAP_CAPACITY = 32;
/*      */   private static final long serialVersionUID = -7721805057305804111L;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long statusOffset;
/*      */ 
/*      */   private int setCompletion(int paramInt)
/*      */   {
/*      */     int i;
/*      */     do
/*  238 */       if ((i = this.status) < 0)
/*  239 */         return i;
/*  240 */     while (!UNSAFE.compareAndSwapInt(this, statusOffset, i, paramInt));
/*  241 */     if (i != 0)
/*  242 */       synchronized (this) { notifyAll(); }
/*  243 */     return paramInt;
/*      */   }
/*      */ 
/*      */   final void tryAwaitDone(long paramLong)
/*      */   {
/*      */     try
/*      */     {
/*      */       int i;
/*  258 */       if ((((i = this.status) > 0) || ((i == 0) && (UNSAFE.compareAndSwapInt(this, statusOffset, 0, 1)))) && (this.status > 0))
/*      */       {
/*  262 */         synchronized (this) {
/*  263 */           if (this.status > 0)
/*  264 */             wait(paramLong);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (InterruptedException localInterruptedException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private int externalAwaitDone()
/*      */   {
/*      */     int i;
/*  278 */     if ((i = this.status) >= 0) {
/*  279 */       int j = 0;
/*  280 */       synchronized (this) {
/*  281 */         while ((i = this.status) >= 0) {
/*  282 */           if (i == 0)
/*  283 */             UNSAFE.compareAndSwapInt(this, statusOffset, 0, 1);
/*      */           else {
/*      */             try
/*      */             {
/*  287 */               wait();
/*      */             } catch (InterruptedException localInterruptedException) {
/*  289 */               j = 1;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  294 */       if (j != 0)
/*  295 */         Thread.currentThread().interrupt();
/*      */     }
/*  297 */     return i;
/*      */   }
/*      */ 
/*      */   private int externalInterruptibleAwaitDone(long paramLong)
/*      */     throws InterruptedException
/*      */   {
/*  306 */     if (Thread.interrupted())
/*  307 */       throw new InterruptedException();
/*      */     int i;
/*  308 */     if ((i = this.status) >= 0) {
/*  309 */       synchronized (this) {
/*  310 */         while ((i = this.status) >= 0) {
/*  311 */           if (i == 0) {
/*  312 */             UNSAFE.compareAndSwapInt(this, statusOffset, 0, 1);
/*      */           }
/*      */           else {
/*  315 */             wait(paramLong);
/*  316 */             if (paramLong > 0L)
/*  317 */               break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  322 */     return i;
/*      */   }
/*      */ 
/*      */   final void doExec()
/*      */   {
/*  331 */     if (this.status >= 0) {
/*      */       boolean bool;
/*      */       try {
/*  334 */         bool = exec();
/*      */       } catch (Throwable localThrowable) {
/*  336 */         setExceptionalCompletion(localThrowable);
/*  337 */         return;
/*      */       }
/*  339 */       if (bool)
/*  340 */         setCompletion(-1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int doJoin()
/*      */   {
/*      */     Thread localThread;
/*  350 */     if (((localThread = Thread.currentThread()) instanceof ForkJoinWorkerThread))
/*      */     {
/*      */       int i;
/*  351 */       if ((i = this.status) < 0)
/*  352 */         return i;
/*      */       ForkJoinWorkerThread localForkJoinWorkerThread;
/*  353 */       if ((localForkJoinWorkerThread = (ForkJoinWorkerThread)localThread).unpushTask(this)) {
/*      */         boolean bool;
/*      */         try { bool = exec();
/*      */         } catch (Throwable localThrowable) {
/*  357 */           return setExceptionalCompletion(localThrowable);
/*      */         }
/*  359 */         if (bool)
/*  360 */           return setCompletion(-1);
/*      */       }
/*  362 */       return localForkJoinWorkerThread.joinTask(this);
/*      */     }
/*      */ 
/*  365 */     return externalAwaitDone();
/*      */   }
/*      */ 
/*      */   private int doInvoke()
/*      */   {
/*      */     int i;
/*  374 */     if ((i = this.status) < 0)
/*  375 */       return i; boolean bool;
/*      */     try {
/*  377 */       bool = exec();
/*      */     } catch (Throwable localThrowable) {
/*  379 */       return setExceptionalCompletion(localThrowable);
/*      */     }
/*  381 */     if (bool) {
/*  382 */       return setCompletion(-1);
/*      */     }
/*  384 */     return doJoin();
/*      */   }
/*      */ 
/*      */   private int setExceptionalCompletion(Throwable paramThrowable)
/*      */   {
/*  437 */     int i = System.identityHashCode(this);
/*  438 */     ReentrantLock localReentrantLock = exceptionTableLock;
/*  439 */     localReentrantLock.lock();
/*      */     try {
/*  441 */       expungeStaleExceptions();
/*  442 */       ExceptionNode[] arrayOfExceptionNode = exceptionTable;
/*  443 */       int j = i & arrayOfExceptionNode.length - 1;
/*  444 */       for (ExceptionNode localExceptionNode = arrayOfExceptionNode[j]; ; localExceptionNode = localExceptionNode.next)
/*  445 */         if (localExceptionNode == null) {
/*  446 */           arrayOfExceptionNode[j] = new ExceptionNode(this, paramThrowable, arrayOfExceptionNode[j]);
/*      */         }
/*      */         else
/*  449 */           if (localExceptionNode.get() == this)
/*      */             break;
/*      */     }
/*      */     finally {
/*  453 */       localReentrantLock.unlock();
/*      */     }
/*  455 */     return setCompletion(-3);
/*      */   }
/*      */ 
/*      */   private void clearExceptionalCompletion()
/*      */   {
/*  462 */     int i = System.identityHashCode(this);
/*  463 */     ReentrantLock localReentrantLock = exceptionTableLock;
/*  464 */     localReentrantLock.lock();
/*      */     try {
/*  466 */       ExceptionNode[] arrayOfExceptionNode = exceptionTable;
/*  467 */       int j = i & arrayOfExceptionNode.length - 1;
/*  468 */       Object localObject1 = arrayOfExceptionNode[j];
/*  469 */       Object localObject2 = null;
/*  470 */       while (localObject1 != null) {
/*  471 */         ExceptionNode localExceptionNode = ((ExceptionNode)localObject1).next;
/*  472 */         if (((ExceptionNode)localObject1).get() == this) {
/*  473 */           if (localObject2 == null) {
/*  474 */             arrayOfExceptionNode[j] = localExceptionNode; break;
/*      */           }
/*  476 */           localObject2.next = localExceptionNode;
/*  477 */           break;
/*      */         }
/*  479 */         localObject2 = localObject1;
/*  480 */         localObject1 = localExceptionNode;
/*      */       }
/*  482 */       expungeStaleExceptions();
/*  483 */       this.status = 0;
/*      */     } finally {
/*  485 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Throwable getThrowableException()
/*      */   {
/*  504 */     if (this.status != -3)
/*  505 */       return null; int i = System.identityHashCode(this);
/*      */ 
/*  508 */     ReentrantLock localReentrantLock = exceptionTableLock;
/*  509 */     localReentrantLock.lock();
/*      */     Object localObject1;
/*      */     ExceptionNode localExceptionNode;
/*      */     try { expungeStaleExceptions();
/*  512 */       localObject1 = exceptionTable;
/*  513 */       localExceptionNode = localObject1[(i & localObject1.length - 1)];
/*  514 */       while ((localExceptionNode != null) && (localExceptionNode.get() != this))
/*  515 */         localExceptionNode = localExceptionNode.next;
/*      */     } finally {
/*  517 */       localReentrantLock.unlock();
/*      */     }
/*      */ 
/*  520 */     if ((localExceptionNode == null) || ((localObject1 = localExceptionNode.ex) == null))
/*  521 */       return null;
/*  522 */     if (localExceptionNode.thrower != Thread.currentThread().getId()) {
/*  523 */       Class localClass = localObject1.getClass();
/*      */       try {
/*  525 */         Object localObject3 = null;
/*  526 */         Constructor[] arrayOfConstructor = localClass.getConstructors();
/*  527 */         for (int j = 0; j < arrayOfConstructor.length; j++) {
/*  528 */           Constructor localConstructor = arrayOfConstructor[j];
/*  529 */           Class[] arrayOfClass = localConstructor.getParameterTypes();
/*  530 */           if (arrayOfClass.length == 0)
/*  531 */             localObject3 = localConstructor;
/*  532 */           else if ((arrayOfClass.length == 1) && (arrayOfClass[0] == Throwable.class))
/*  533 */             return (Throwable)localConstructor.newInstance(new Object[] { localObject1 });
/*      */         }
/*  535 */         if (localObject3 != null) {
/*  536 */           Throwable localThrowable = (Throwable)localObject3.newInstance(new Object[0]);
/*  537 */           localThrowable.initCause((Throwable)localObject1);
/*  538 */           return localThrowable;
/*      */         }
/*      */       } catch (Exception localException) {
/*      */       }
/*      */     }
/*  543 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static void expungeStaleExceptions()
/*      */   {
/*      */     Reference localReference;
/*  550 */     while ((localReference = exceptionTableRefQueue.poll()) != null)
/*  551 */       if ((localReference instanceof ExceptionNode)) {
/*  552 */         ForkJoinTask localForkJoinTask = (ForkJoinTask)((ExceptionNode)localReference).get();
/*  553 */         ExceptionNode[] arrayOfExceptionNode = exceptionTable;
/*  554 */         int i = System.identityHashCode(localForkJoinTask) & arrayOfExceptionNode.length - 1;
/*  555 */         Object localObject1 = arrayOfExceptionNode[i];
/*  556 */         Object localObject2 = null;
/*  557 */         while (localObject1 != null) {
/*  558 */           ExceptionNode localExceptionNode = ((ExceptionNode)localObject1).next;
/*  559 */           if (localObject1 == localReference) {
/*  560 */             if (localObject2 == null) {
/*  561 */               arrayOfExceptionNode[i] = localExceptionNode; break;
/*      */             }
/*  563 */             localObject2.next = localExceptionNode;
/*  564 */             break;
/*      */           }
/*  566 */           localObject2 = localObject1;
/*  567 */           localObject1 = localExceptionNode;
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   static final void helpExpungeStaleExceptions()
/*      */   {
/*  578 */     ReentrantLock localReentrantLock = exceptionTableLock;
/*  579 */     if (localReentrantLock.tryLock())
/*      */       try {
/*  581 */         expungeStaleExceptions();
/*      */       } finally {
/*  583 */         localReentrantLock.unlock();
/*      */       }
/*      */   }
/*      */ 
/*      */   private V reportResult()
/*      */   {
/*      */     int i;
/*  594 */     if ((i = this.status) == -2)
/*  595 */       throw new CancellationException();
/*      */     Throwable localThrowable;
/*  596 */     if ((i == -3) && ((localThrowable = getThrowableException()) != null))
/*  597 */       UNSAFE.throwException(localThrowable);
/*  598 */     return getRawResult();
/*      */   }
/*      */ 
/*      */   public final ForkJoinTask<V> fork()
/*      */   {
/*  622 */     ((ForkJoinWorkerThread)Thread.currentThread()).pushTask(this);
/*      */ 
/*  624 */     return this;
/*      */   }
/*      */ 
/*      */   public final V join()
/*      */   {
/*  639 */     if (doJoin() != -1) {
/*  640 */       return reportResult();
/*      */     }
/*  642 */     return getRawResult();
/*      */   }
/*      */ 
/*      */   public final V invoke()
/*      */   {
/*  654 */     if (doInvoke() != -1) {
/*  655 */       return reportResult();
/*      */     }
/*  657 */     return getRawResult();
/*      */   }
/*      */ 
/*      */   public static void invokeAll(ForkJoinTask<?> paramForkJoinTask1, ForkJoinTask<?> paramForkJoinTask2)
/*      */   {
/*  684 */     paramForkJoinTask2.fork();
/*  685 */     paramForkJoinTask1.invoke();
/*  686 */     paramForkJoinTask2.join();
/*      */   }
/*      */ 
/*      */   public static void invokeAll(ForkJoinTask<?>[] paramArrayOfForkJoinTask)
/*      */   {
/*  711 */     Object localObject = null;
/*  712 */     int i = paramArrayOfForkJoinTask.length - 1;
/*      */     ForkJoinTask<?> localForkJoinTask;
/*  713 */     for (int j = i; j >= 0; j--) {
/*  714 */       localForkJoinTask = paramArrayOfForkJoinTask[j];
/*  715 */       if (localForkJoinTask == null) {
/*  716 */         if (localObject == null)
/*  717 */           localObject = new NullPointerException();
/*      */       }
/*  719 */       else if (j != 0)
/*  720 */         localForkJoinTask.fork();
/*  721 */       else if ((localForkJoinTask.doInvoke() < -1) && (localObject == null))
/*  722 */         localObject = localForkJoinTask.getException();
/*      */     }
/*  724 */     for (j = 1; j <= i; j++) {
/*  725 */       localForkJoinTask = paramArrayOfForkJoinTask[j];
/*  726 */       if (localForkJoinTask != null) {
/*  727 */         if (localObject != null)
/*  728 */           localForkJoinTask.cancel(false);
/*  729 */         else if ((localForkJoinTask.doJoin() < -1) && (localObject == null))
/*  730 */           localObject = localForkJoinTask.getException();
/*      */       }
/*      */     }
/*  733 */     if (localObject != null)
/*  734 */       UNSAFE.throwException((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> paramCollection)
/*      */   {
/*  761 */     if ((!(paramCollection instanceof RandomAccess)) || (!(paramCollection instanceof List))) {
/*  762 */       invokeAll((ForkJoinTask[])paramCollection.toArray(new ForkJoinTask[paramCollection.size()]));
/*  763 */       return paramCollection;
/*      */     }
/*      */ 
/*  766 */     List localList = (List)paramCollection;
/*      */ 
/*  768 */     Object localObject = null;
/*  769 */     int i = localList.size() - 1;
/*      */     ForkJoinTask localForkJoinTask;
/*  770 */     for (int j = i; j >= 0; j--) {
/*  771 */       localForkJoinTask = (ForkJoinTask)localList.get(j);
/*  772 */       if (localForkJoinTask == null) {
/*  773 */         if (localObject == null)
/*  774 */           localObject = new NullPointerException();
/*      */       }
/*  776 */       else if (j != 0)
/*  777 */         localForkJoinTask.fork();
/*  778 */       else if ((localForkJoinTask.doInvoke() < -1) && (localObject == null))
/*  779 */         localObject = localForkJoinTask.getException();
/*      */     }
/*  781 */     for (j = 1; j <= i; j++) {
/*  782 */       localForkJoinTask = (ForkJoinTask)localList.get(j);
/*  783 */       if (localForkJoinTask != null) {
/*  784 */         if (localObject != null)
/*  785 */           localForkJoinTask.cancel(false);
/*  786 */         else if ((localForkJoinTask.doJoin() < -1) && (localObject == null))
/*  787 */           localObject = localForkJoinTask.getException();
/*      */       }
/*      */     }
/*  790 */     if (localObject != null)
/*  791 */       UNSAFE.throwException((Throwable)localObject);
/*  792 */     return paramCollection;
/*      */   }
/*      */ 
/*      */   public boolean cancel(boolean paramBoolean)
/*      */   {
/*  823 */     return setCompletion(-2) == -2;
/*      */   }
/*      */ 
/*      */   final void cancelIgnoringExceptions()
/*      */   {
/*      */     try
/*      */     {
/*  834 */       cancel(false);
/*      */     } catch (Throwable localThrowable) {
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean isDone() {
/*  840 */     return this.status < 0;
/*      */   }
/*      */ 
/*      */   public final boolean isCancelled() {
/*  844 */     return this.status == -2;
/*      */   }
/*      */ 
/*      */   public final boolean isCompletedAbnormally()
/*      */   {
/*  853 */     return this.status < -1;
/*      */   }
/*      */ 
/*      */   public final boolean isCompletedNormally()
/*      */   {
/*  864 */     return this.status == -1;
/*      */   }
/*      */ 
/*      */   public final Throwable getException()
/*      */   {
/*  875 */     int i = this.status;
/*  876 */     return i == -2 ? new CancellationException() : i >= -1 ? null : getThrowableException();
/*      */   }
/*      */ 
/*      */   public void completeExceptionally(Throwable paramThrowable)
/*      */   {
/*  896 */     setExceptionalCompletion(((paramThrowable instanceof RuntimeException)) || ((paramThrowable instanceof Error)) ? paramThrowable : new RuntimeException(paramThrowable));
/*      */   }
/*      */ 
/*      */   public void complete(V paramV)
/*      */   {
/*      */     try
/*      */     {
/*  916 */       setRawResult(paramV);
/*      */     } catch (Throwable localThrowable) {
/*  918 */       setExceptionalCompletion(localThrowable);
/*  919 */       return;
/*      */     }
/*  921 */     setCompletion(-1);
/*      */   }
/*      */ 
/*      */   public final V get()
/*      */     throws InterruptedException, ExecutionException
/*      */   {
/*  936 */     int i = (Thread.currentThread() instanceof ForkJoinWorkerThread) ? doJoin() : externalInterruptibleAwaitDone(0L);
/*      */ 
/*  939 */     if (i == -2)
/*  940 */       throw new CancellationException();
/*      */     Throwable localThrowable;
/*  941 */     if ((i == -3) && ((localThrowable = getThrowableException()) != null))
/*  942 */       throw new ExecutionException(localThrowable);
/*  943 */     return getRawResult();
/*      */   }
/*      */ 
/*      */   public final V get(long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException, ExecutionException, TimeoutException
/*      */   {
/*  962 */     Thread localThread = Thread.currentThread();
/*  963 */     if ((localThread instanceof ForkJoinWorkerThread)) {
/*  964 */       ForkJoinWorkerThread localForkJoinWorkerThread = (ForkJoinWorkerThread)localThread;
/*  965 */       long l2 = paramTimeUnit.toNanos(paramLong);
/*  966 */       if (this.status >= 0) {
/*  967 */         boolean bool = false;
/*  968 */         if (localForkJoinWorkerThread.unpushTask(this)) {
/*      */           try {
/*  970 */             bool = exec();
/*      */           } catch (Throwable localThrowable2) {
/*  972 */             setExceptionalCompletion(localThrowable2);
/*      */           }
/*      */         }
/*  975 */         if (bool)
/*  976 */           setCompletion(-1);
/*  977 */         else if ((this.status >= 0) && (l2 > 0L))
/*  978 */           localForkJoinWorkerThread.pool.timedAwaitJoin(this, l2);
/*      */       }
/*      */     }
/*      */     else {
/*  982 */       long l1 = paramTimeUnit.toMillis(paramLong);
/*  983 */       if (l1 > 0L)
/*  984 */         externalInterruptibleAwaitDone(l1);
/*      */     }
/*  986 */     int i = this.status;
/*  987 */     if (i != -1)
/*      */     {
/*  989 */       if (i == -2)
/*  990 */         throw new CancellationException();
/*  991 */       if (i != -3)
/*  992 */         throw new TimeoutException();
/*      */       Throwable localThrowable1;
/*  993 */       if ((localThrowable1 = getThrowableException()) != null)
/*  994 */         throw new ExecutionException(localThrowable1);
/*      */     }
/*  996 */     return getRawResult();
/*      */   }
/*      */ 
/*      */   public final void quietlyJoin()
/*      */   {
/* 1006 */     doJoin();
/*      */   }
/*      */ 
/*      */   public final void quietlyInvoke()
/*      */   {
/* 1015 */     doInvoke();
/*      */   }
/*      */ 
/*      */   public static void helpQuiesce()
/*      */   {
/* 1032 */     ((ForkJoinWorkerThread)Thread.currentThread()).helpQuiescePool();
/*      */   }
/*      */ 
/*      */   public void reinitialize()
/*      */   {
/* 1053 */     if (this.status == -3)
/* 1054 */       clearExceptionalCompletion();
/*      */     else
/* 1056 */       this.status = 0;
/*      */   }
/*      */ 
/*      */   public static ForkJoinPool getPool()
/*      */   {
/* 1067 */     Thread localThread = Thread.currentThread();
/* 1068 */     return (localThread instanceof ForkJoinWorkerThread) ? ((ForkJoinWorkerThread)localThread).pool : null;
/*      */   }
/*      */ 
/*      */   public static boolean inForkJoinPool()
/*      */   {
/* 1081 */     return Thread.currentThread() instanceof ForkJoinWorkerThread;
/*      */   }
/*      */ 
/*      */   public boolean tryUnfork()
/*      */   {
/* 1101 */     return ((ForkJoinWorkerThread)Thread.currentThread()).unpushTask(this);
/*      */   }
/*      */ 
/*      */   public static int getQueuedTaskCount()
/*      */   {
/* 1120 */     return ((ForkJoinWorkerThread)Thread.currentThread()).getQueueSize();
/*      */   }
/*      */ 
/*      */   public static int getSurplusQueuedTaskCount()
/*      */   {
/* 1143 */     return ((ForkJoinWorkerThread)Thread.currentThread()).getEstimatedSurplusTaskCount();
/*      */   }
/*      */ 
/*      */   public abstract V getRawResult();
/*      */ 
/*      */   protected abstract void setRawResult(V paramV);
/*      */ 
/*      */   protected abstract boolean exec();
/*      */ 
/*      */   protected static ForkJoinTask<?> peekNextLocalTask()
/*      */   {
/* 1201 */     return ((ForkJoinWorkerThread)Thread.currentThread()).peekTask();
/*      */   }
/*      */ 
/*      */   protected static ForkJoinTask<?> pollNextLocalTask()
/*      */   {
/* 1220 */     return ((ForkJoinWorkerThread)Thread.currentThread()).pollLocalTask();
/*      */   }
/*      */ 
/*      */   protected static ForkJoinTask<?> pollTask()
/*      */   {
/* 1243 */     return ((ForkJoinWorkerThread)Thread.currentThread()).pollTask();
/*      */   }
/*      */ 
/*      */   public static ForkJoinTask<?> adapt(Runnable paramRunnable)
/*      */   {
/* 1311 */     return new AdaptedRunnable(paramRunnable, null);
/*      */   }
/*      */ 
/*      */   public static <T> ForkJoinTask<T> adapt(Runnable paramRunnable, T paramT)
/*      */   {
/* 1324 */     return new AdaptedRunnable(paramRunnable, paramT);
/*      */   }
/*      */ 
/*      */   public static <T> ForkJoinTask<T> adapt(Callable<? extends T> paramCallable)
/*      */   {
/* 1337 */     return new AdaptedCallable(paramCallable);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1353 */     paramObjectOutputStream.defaultWriteObject();
/* 1354 */     paramObjectOutputStream.writeObject(getException());
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1364 */     paramObjectInputStream.defaultReadObject();
/* 1365 */     Object localObject = paramObjectInputStream.readObject();
/* 1366 */     if (localObject != null)
/* 1367 */       setExceptionalCompletion((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1376 */     exceptionTable = new ExceptionNode[32];
/*      */     try {
/* 1378 */       UNSAFE = Unsafe.getUnsafe();
/* 1379 */       statusOffset = UNSAFE.objectFieldOffset(ForkJoinTask.class.getDeclaredField("status"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1382 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class AdaptedCallable<T> extends ForkJoinTask<T>
/*      */     implements RunnableFuture<T>
/*      */   {
/*      */     final Callable<? extends T> callable;
/*      */     T result;
/*      */     private static final long serialVersionUID = 2838392045355241008L;
/*      */ 
/*      */     AdaptedCallable(Callable<? extends T> paramCallable)
/*      */     {
/* 1281 */       if (paramCallable == null) throw new NullPointerException();
/* 1282 */       this.callable = paramCallable;
/*      */     }
/* 1284 */     public T getRawResult() { return this.result; } 
/* 1285 */     public void setRawResult(T paramT) { this.result = paramT; } 
/*      */     public boolean exec() {
/*      */       try {
/* 1288 */         this.result = this.callable.call();
/* 1289 */         return true;
/*      */       } catch (Error localError) {
/* 1291 */         throw localError;
/*      */       } catch (RuntimeException localRuntimeException) {
/* 1293 */         throw localRuntimeException;
/*      */       } catch (Exception localException) {
/* 1295 */         throw new RuntimeException(localException);
/*      */       }
/*      */     }
/* 1298 */     public void run() { invoke(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class AdaptedRunnable<T> extends ForkJoinTask<T>
/*      */     implements RunnableFuture<T>
/*      */   {
/*      */     final Runnable runnable;
/*      */     final T resultOnCompletion;
/*      */     T result;
/*      */     private static final long serialVersionUID = 5232453952276885070L;
/*      */ 
/*      */     AdaptedRunnable(Runnable paramRunnable, T paramT)
/*      */     {
/* 1258 */       if (paramRunnable == null) throw new NullPointerException();
/* 1259 */       this.runnable = paramRunnable;
/* 1260 */       this.resultOnCompletion = paramT;
/*      */     }
/* 1262 */     public T getRawResult() { return this.result; } 
/* 1263 */     public void setRawResult(T paramT) { this.result = paramT; } 
/*      */     public boolean exec() {
/* 1265 */       this.runnable.run();
/* 1266 */       this.result = this.resultOnCompletion;
/* 1267 */       return true;
/*      */     }
/* 1269 */     public void run() { invoke(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class ExceptionNode extends WeakReference<ForkJoinTask<?>>
/*      */   {
/*      */     final Throwable ex;
/*      */     ExceptionNode next;
/*      */     final long thrower;
/*      */ 
/*      */     ExceptionNode(ForkJoinTask<?> paramForkJoinTask, Throwable paramThrowable, ExceptionNode paramExceptionNode)
/*      */     {
/*  424 */       super(ForkJoinTask.exceptionTableRefQueue);
/*  425 */       this.ex = paramThrowable;
/*  426 */       this.next = paramExceptionNode;
/*  427 */       this.thrower = Thread.currentThread().getId();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ForkJoinTask
 * JD-Core Version:    0.6.2
 */