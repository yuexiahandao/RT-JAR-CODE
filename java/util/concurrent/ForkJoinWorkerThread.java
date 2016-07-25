/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Random;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class ForkJoinWorkerThread extends Thread
/*     */ {
/*     */   private static final int SMASK = 65535;
/*     */   private static final int INITIAL_QUEUE_CAPACITY = 8192;
/*     */   private static final int MAXIMUM_QUEUE_CAPACITY = 16777216;
/*     */   ForkJoinTask<?>[] queue;
/*     */   final ForkJoinPool pool;
/*     */   int queueTop;
/*     */   volatile int queueBase;
/*     */   int stealHint;
/*     */   final int poolIndex;
/*     */   int nextWait;
/*     */   volatile int eventCount;
/*     */   int seed;
/*     */   int stealCount;
/*     */   volatile boolean terminate;
/*     */   volatile boolean parked;
/*     */   final boolean locallyFifo;
/*     */   ForkJoinTask<?> currentSteal;
/*     */   ForkJoinTask<?> currentJoin;
/*     */   private static final int MAX_HELP = 16;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long ABASE;
/* 995 */   private static final int ASHIFT = 31 - Integer.numberOfLeadingZeros(i);
/*     */ 
/*     */   protected ForkJoinWorkerThread(ForkJoinPool paramForkJoinPool)
/*     */   {
/* 297 */     super(paramForkJoinPool.nextWorkerName());
/* 298 */     this.pool = paramForkJoinPool;
/* 299 */     int i = paramForkJoinPool.registerWorker(this);
/* 300 */     this.poolIndex = i;
/* 301 */     this.eventCount = ((i ^ 0xFFFFFFFF) & 0xFFFF);
/* 302 */     this.locallyFifo = paramForkJoinPool.locallyFifo;
/* 303 */     Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = paramForkJoinPool.ueh;
/* 304 */     if (localUncaughtExceptionHandler != null)
/* 305 */       setUncaughtExceptionHandler(localUncaughtExceptionHandler);
/* 306 */     setDaemon(true);
/*     */   }
/*     */ 
/*     */   public ForkJoinPool getPool()
/*     */   {
/* 317 */     return this.pool;
/*     */   }
/*     */ 
/*     */   public int getPoolIndex()
/*     */   {
/* 330 */     return this.poolIndex;
/*     */   }
/*     */ 
/*     */   private int nextSeed()
/*     */   {
/* 343 */     int i = this.seed;
/* 344 */     i ^= i << 13;
/* 345 */     i ^= i >>> 17;
/* 346 */     i ^= i << 5;
/* 347 */     return this.seed = i;
/*     */   }
/*     */ 
/*     */   protected void onStart()
/*     */   {
/* 362 */     this.queue = new ForkJoinTask[8192];
/* 363 */     int i = ForkJoinPool.workerSeedGenerator.nextInt();
/* 364 */     this.seed = (i == 0 ? 1 : i);
/*     */   }
/*     */ 
/*     */   protected void onTermination(Throwable paramThrowable)
/*     */   {
/*     */     try
/*     */     {
/* 377 */       this.terminate = true;
/* 378 */       cancelTasks();
/* 379 */       this.pool.deregisterWorker(this, paramThrowable);
/*     */     } catch (Throwable localThrowable) {
/* 381 */       if (paramThrowable == null)
/* 382 */         paramThrowable = localThrowable;
/*     */     } finally {
/* 384 */       if (paramThrowable != null)
/* 385 */         UNSAFE.throwException(paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 395 */     Object localObject1 = null;
/*     */     try {
/* 397 */       onStart();
/* 398 */       this.pool.work(this);
/*     */     } catch (Throwable localThrowable) {
/* 400 */       localObject1 = localThrowable;
/*     */     } finally {
/* 402 */       onTermination((Throwable)localObject1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final boolean casSlotNull(ForkJoinTask<?>[] paramArrayOfForkJoinTask, int paramInt, ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 439 */     return UNSAFE.compareAndSwapObject(paramArrayOfForkJoinTask, (paramInt << ASHIFT) + ABASE, paramForkJoinTask, null);
/*     */   }
/*     */ 
/*     */   private static final void writeSlot(ForkJoinTask<?>[] paramArrayOfForkJoinTask, int paramInt, ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 449 */     UNSAFE.putObjectVolatile(paramArrayOfForkJoinTask, (paramInt << ASHIFT) + ABASE, paramForkJoinTask);
/*     */   }
/*     */ 
/*     */   final void pushTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/*     */     ForkJoinTask[] arrayOfForkJoinTask;
/* 461 */     if ((arrayOfForkJoinTask = this.queue) != null)
/*     */     {
/*     */       int i;
/*     */       int j;
/* 462 */       long l = (((i = this.queueTop) & (j = arrayOfForkJoinTask.length - 1)) << ASHIFT) + ABASE;
/* 463 */       UNSAFE.putOrderedObject(arrayOfForkJoinTask, l, paramForkJoinTask);
/* 464 */       this.queueTop = (i + 1);
/* 465 */       if (i -= this.queueBase <= 2)
/* 466 */         this.pool.signalWork();
/* 467 */       else if (i == j)
/* 468 */         growQueue();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void growQueue()
/*     */   {
/* 478 */     ForkJoinTask[] arrayOfForkJoinTask1 = this.queue;
/* 479 */     int i = arrayOfForkJoinTask1 != null ? arrayOfForkJoinTask1.length << 1 : 8192;
/* 480 */     if (i > 16777216)
/* 481 */       throw new RejectedExecutionException("Queue capacity exceeded");
/* 482 */     if (i < 8192)
/* 483 */       i = 8192;
/* 484 */     ForkJoinTask[] arrayOfForkJoinTask2 = this.queue = new ForkJoinTask[i];
/* 485 */     int j = i - 1;
/* 486 */     int k = this.queueTop;
/*     */     int m;
/* 488 */     if ((arrayOfForkJoinTask1 != null) && ((m = arrayOfForkJoinTask1.length - 1) >= 0))
/* 489 */       for (int n = this.queueBase; n != k; n++) {
/* 490 */         long l = ((n & m) << ASHIFT) + ABASE;
/* 491 */         Object localObject = UNSAFE.getObjectVolatile(arrayOfForkJoinTask1, l);
/* 492 */         if ((localObject != null) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask1, l, localObject, null)))
/* 493 */           UNSAFE.putObjectVolatile(arrayOfForkJoinTask2, ((n & j) << ASHIFT) + ABASE, localObject);
/*     */       }
/*     */   }
/*     */ 
/*     */   final ForkJoinTask<?> deqTask()
/*     */   {
/*     */     int i;
/*     */     ForkJoinTask[] arrayOfForkJoinTask;
/*     */     int j;
/*     */     ForkJoinTask localForkJoinTask;
/* 508 */     if ((this.queueTop != (i = this.queueBase)) && ((arrayOfForkJoinTask = this.queue) != null) && ((j = arrayOfForkJoinTask.length - 1 & i) >= 0) && ((localForkJoinTask = arrayOfForkJoinTask[j]) != null) && (this.queueBase == i) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, (j << ASHIFT) + ABASE, localForkJoinTask, null)))
/*     */     {
/* 513 */       this.queueBase = (i + 1);
/* 514 */       return localForkJoinTask;
/*     */     }
/* 516 */     return null;
/*     */   }
/*     */ 
/*     */   final ForkJoinTask<?> locallyDeqTask()
/*     */   {
/* 527 */     ForkJoinTask[] arrayOfForkJoinTask = this.queue;
/*     */     int i;
/*     */     int j;
/* 528 */     while ((arrayOfForkJoinTask != null) && ((i = arrayOfForkJoinTask.length - 1) >= 0) && 
/* 529 */       (this.queueTop != (j = this.queueBase)))
/*     */     {
/*     */       int k;
/*     */       ForkJoinTask localForkJoinTask;
/* 530 */       if (((localForkJoinTask = arrayOfForkJoinTask[(k = i & j)]) != null) && (this.queueBase == j) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, (k << ASHIFT) + ABASE, localForkJoinTask, null)))
/*     */       {
/* 534 */         this.queueBase = (j + 1);
/* 535 */         return localForkJoinTask;
/*     */       }
/*     */     }
/*     */ 
/* 539 */     return null;
/*     */   }
/*     */ 
/*     */   private ForkJoinTask<?> popTask()
/*     */   {
/* 548 */     ForkJoinTask[] arrayOfForkJoinTask = this.queue;
/*     */     int i;
/* 549 */     if ((arrayOfForkJoinTask != null) && ((i = arrayOfForkJoinTask.length - 1) >= 0))
/*     */     {
/*     */       int j;
/* 550 */       while ((j = this.queueTop) != this.queueBase) {
/* 551 */         int k = i & --j;
/* 552 */         long l = (k << ASHIFT) + ABASE;
/* 553 */         ForkJoinTask localForkJoinTask = arrayOfForkJoinTask[k];
/* 554 */         if (localForkJoinTask == null)
/*     */           break;
/* 556 */         if (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, l, localForkJoinTask, null)) {
/* 557 */           this.queueTop = j;
/* 558 */           return localForkJoinTask;
/*     */         }
/*     */       }
/*     */     }
/* 562 */     return null;
/*     */   }
/*     */ 
/*     */   final boolean unpushTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/*     */     ForkJoinTask[] arrayOfForkJoinTask;
/*     */     int i;
/* 574 */     if (((arrayOfForkJoinTask = this.queue) != null) && ((i = this.queueTop) != this.queueBase) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, ((arrayOfForkJoinTask.length - 1 & --i) << ASHIFT) + ABASE, paramForkJoinTask, null)))
/*     */     {
/* 577 */       this.queueTop = i;
/* 578 */       return true;
/*     */     }
/* 580 */     return false;
/*     */   }
/*     */ 
/*     */   final ForkJoinTask<?> peekTask()
/*     */   {
/* 588 */     ForkJoinTask[] arrayOfForkJoinTask = this.queue;
/*     */     int i;
/* 589 */     if ((arrayOfForkJoinTask == null) || ((i = arrayOfForkJoinTask.length - 1) < 0))
/* 590 */       return null;
/* 591 */     int j = this.locallyFifo ? this.queueBase : this.queueTop - 1;
/* 592 */     return arrayOfForkJoinTask[(j & i)];
/*     */   }
/*     */ 
/*     */   final void execTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 601 */     this.currentSteal = paramForkJoinTask;
/*     */     while (true) {
/* 603 */       if (paramForkJoinTask != null)
/* 604 */         paramForkJoinTask.doExec();
/* 605 */       if (this.queueTop == this.queueBase)
/*     */         break;
/* 607 */       paramForkJoinTask = this.locallyFifo ? locallyDeqTask() : popTask();
/*     */     }
/* 609 */     this.stealCount += 1;
/* 610 */     this.currentSteal = null;
/*     */   }
/*     */ 
/*     */   final void cancelTasks()
/*     */   {
/* 618 */     ForkJoinTask localForkJoinTask1 = this.currentJoin;
/* 619 */     if ((localForkJoinTask1 != null) && (localForkJoinTask1.status >= 0))
/* 620 */       localForkJoinTask1.cancelIgnoringExceptions();
/* 621 */     ForkJoinTask localForkJoinTask2 = this.currentSteal;
/* 622 */     if ((localForkJoinTask2 != null) && (localForkJoinTask2.status >= 0))
/* 623 */       localForkJoinTask2.cancelIgnoringExceptions();
/* 624 */     while (this.queueBase != this.queueTop) {
/* 625 */       ForkJoinTask localForkJoinTask3 = deqTask();
/* 626 */       if (localForkJoinTask3 != null)
/* 627 */         localForkJoinTask3.cancelIgnoringExceptions();
/*     */     }
/*     */   }
/*     */ 
/*     */   final int drainTasksTo(Collection<? super ForkJoinTask<?>> paramCollection)
/*     */   {
/* 637 */     int i = 0;
/* 638 */     while (this.queueBase != this.queueTop) {
/* 639 */       ForkJoinTask localForkJoinTask = deqTask();
/* 640 */       if (localForkJoinTask != null) {
/* 641 */         paramCollection.add(localForkJoinTask);
/* 642 */         i++;
/*     */       }
/*     */     }
/* 645 */     return i;
/*     */   }
/*     */ 
/*     */   final int getQueueSize()
/*     */   {
/* 654 */     return this.queueTop - this.queueBase;
/*     */   }
/*     */ 
/*     */   final ForkJoinTask<?> pollLocalTask()
/*     */   {
/* 663 */     return this.locallyFifo ? locallyDeqTask() : popTask();
/*     */   }
/*     */ 
/*     */   final ForkJoinTask<?> pollTask()
/*     */   {
/* 673 */     ForkJoinTask localForkJoinTask = pollLocalTask();
/*     */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread;
/* 674 */     if ((localForkJoinTask != null) || ((arrayOfForkJoinWorkerThread = this.pool.workers) == null))
/* 675 */       return localForkJoinTask;
/* 676 */     int i = arrayOfForkJoinWorkerThread.length;
/* 677 */     int j = i << 1;
/* 678 */     int k = nextSeed();
/* 679 */     int m = 0;
/* 680 */     while (m < j) {
/* 681 */       ForkJoinWorkerThread localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[(m++ + k & i - 1)];
/* 682 */       if ((localForkJoinWorkerThread != null) && (localForkJoinWorkerThread.queueBase != localForkJoinWorkerThread.queueTop) && (localForkJoinWorkerThread.queue != null)) {
/* 683 */         if ((localForkJoinTask = localForkJoinWorkerThread.deqTask()) != null)
/* 684 */           return localForkJoinTask;
/* 685 */         m = 0;
/*     */       }
/*     */     }
/* 688 */     return null;
/*     */   }
/*     */ 
/*     */   final int joinTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 709 */     ForkJoinTask localForkJoinTask = this.currentJoin;
/* 710 */     this.currentJoin = paramForkJoinTask;
/* 711 */     int j = 16;
/*     */     while (true)
/*     */     {
/*     */       int i;
/* 712 */       if ((i = paramForkJoinTask.status) < 0) {
/* 713 */         this.currentJoin = localForkJoinTask;
/* 714 */         return i;
/*     */       }
/* 716 */       if (j > 0) {
/* 717 */         if (this.queueTop != this.queueBase) {
/* 718 */           if (!localHelpJoinTask(paramForkJoinTask))
/* 719 */             j = 0;
/*     */         }
/* 721 */         else if (j == 8) {
/* 722 */           j--;
/* 723 */           if (tryDeqAndExec(paramForkJoinTask) >= 0)
/* 724 */             Thread.yield();
/*     */         }
/*     */         else {
/* 727 */           j = helpJoinTask(paramForkJoinTask) ? 16 : j - 1;
/*     */         }
/*     */       } else {
/* 730 */         j = 16;
/* 731 */         this.pool.tryAwaitJoin(paramForkJoinTask);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean localHelpJoinTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/*     */     int i;
/*     */     ForkJoinTask[] arrayOfForkJoinTask;
/*     */     int j;
/*     */     ForkJoinTask localForkJoinTask;
/* 744 */     if (((i = this.queueTop) != this.queueBase) && ((arrayOfForkJoinTask = this.queue) != null) && ((j = arrayOfForkJoinTask.length - 1 & --i) >= 0) && ((localForkJoinTask = arrayOfForkJoinTask[j]) != null))
/*     */     {
/* 747 */       if ((localForkJoinTask != paramForkJoinTask) && (localForkJoinTask.status >= 0))
/* 748 */         return false;
/* 749 */       if (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, (j << ASHIFT) + ABASE, localForkJoinTask, null))
/*     */       {
/* 751 */         this.queueTop = i;
/* 752 */         localForkJoinTask.doExec();
/*     */       }
/*     */     }
/* 755 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean helpJoinTask(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 774 */     boolean bool = false;
/* 775 */     int i = this.pool.scanGuard & 0xFFFF;
/* 776 */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.pool.workers;
/* 777 */     if ((arrayOfForkJoinWorkerThread != null) && (arrayOfForkJoinWorkerThread.length > i) && (paramForkJoinTask.status >= 0)) {
/* 778 */       int j = 16;
/* 779 */       Object localObject1 = paramForkJoinTask;
/* 780 */       Object localObject2 = this;
/*     */       while (true) {
/* 782 */         ForkJoinWorkerThread localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[(localObject2.stealHint & i)];
/* 783 */         if ((localForkJoinWorkerThread == null) || (localForkJoinWorkerThread.currentSteal != localObject1)) {
/* 784 */           int k = 0;
/*     */           do { if (((localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[k]) != null) && (localForkJoinWorkerThread.currentSteal == localObject1)) {
/* 786 */               ((ForkJoinWorkerThread)localObject2).stealHint = k;
/* 787 */               break;
/*     */             }
/* 789 */             k++; } while (k <= i);
/* 790 */           break;
/*     */         }
/*     */ 
/*     */         while (true)
/*     */         {
/* 796 */           if (paramForkJoinTask.status < 0)
/*     */             break label332;
/*     */           int m;
/*     */           int n;
/* 798 */           if (((m = localForkJoinWorkerThread.queueBase) == localForkJoinWorkerThread.queueTop) || ((localObject3 = localForkJoinWorkerThread.queue) == null) || ((n = localObject3.length - 1 & m) < 0))
/*     */           {
/*     */             break;
/*     */           }
/* 802 */           long l = (n << ASHIFT) + ABASE;
/* 803 */           Object localObject4 = localObject3[n];
/* 804 */           if (((ForkJoinTask)localObject1).status < 0)
/*     */             break label332;
/* 806 */           if ((localObject4 != null) && (localForkJoinWorkerThread.queueBase == m) && (UNSAFE.compareAndSwapObject(localObject3, l, localObject4, null)))
/*     */           {
/* 808 */             localForkJoinWorkerThread.queueBase = (m + 1);
/* 809 */             localForkJoinWorkerThread.stealHint = this.poolIndex;
/* 810 */             ForkJoinTask localForkJoinTask = this.currentSteal;
/* 811 */             this.currentSteal = localObject4;
/* 812 */             localObject4.doExec();
/* 813 */             this.currentSteal = localForkJoinTask;
/* 814 */             bool = true;
/*     */           }
/*     */         }
/*     */ 
/* 818 */         Object localObject3 = localForkJoinWorkerThread.currentJoin;
/* 819 */         j--; if ((j <= 0) || (((ForkJoinTask)localObject1).status < 0) || (localObject3 == null) || (localObject3 == localObject1))
/*     */           break;
/* 821 */         localObject1 = localObject3;
/* 822 */         localObject2 = localForkJoinWorkerThread;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 828 */     label332: return bool;
/*     */   }
/*     */ 
/*     */   private int tryDeqAndExec(ForkJoinTask<?> paramForkJoinTask)
/*     */   {
/* 839 */     int i = this.pool.scanGuard & 0xFFFF;
/* 840 */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.pool.workers;
/* 841 */     if ((arrayOfForkJoinWorkerThread != null) && (arrayOfForkJoinWorkerThread.length > i) && (paramForkJoinTask.status >= 0)) {
/* 842 */       for (int j = 0; j <= i; j++)
/*     */       {
/* 844 */         ForkJoinWorkerThread localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[j];
/*     */         int k;
/*     */         ForkJoinTask[] arrayOfForkJoinTask;
/*     */         int m;
/* 845 */         if ((localForkJoinWorkerThread != null) && ((k = localForkJoinWorkerThread.queueBase) != localForkJoinWorkerThread.queueTop) && ((arrayOfForkJoinTask = localForkJoinWorkerThread.queue) != null) && ((m = arrayOfForkJoinTask.length - 1 & k) >= 0) && (arrayOfForkJoinTask[m] == paramForkJoinTask))
/*     */         {
/* 850 */           long l = (m << ASHIFT) + ABASE;
/* 851 */           if ((localForkJoinWorkerThread.queueBase != k) || (!UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, l, paramForkJoinTask, null)))
/*     */             break;
/* 853 */           localForkJoinWorkerThread.queueBase = (k + 1);
/* 854 */           localForkJoinWorkerThread.stealHint = this.poolIndex;
/* 855 */           ForkJoinTask localForkJoinTask = this.currentSteal;
/* 856 */           this.currentSteal = paramForkJoinTask;
/* 857 */           paramForkJoinTask.doExec();
/* 858 */           this.currentSteal = localForkJoinTask;
/* 859 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 864 */     return paramForkJoinTask.status;
/*     */   }
/*     */ 
/*     */   final int getEstimatedSurplusTaskCount()
/*     */   {
/* 916 */     return this.queueTop - this.queueBase - this.pool.idlePerActive();
/*     */   }
/*     */ 
/*     */   final void helpQuiescePool()
/*     */   {
/* 929 */     int i = 1;
/* 930 */     ForkJoinTask localForkJoinTask = this.currentSteal;
/* 931 */     ForkJoinPool localForkJoinPool = this.pool;
/* 932 */     localForkJoinPool.addQuiescerCount(1);
/*     */     while (true) {
/* 934 */       ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = localForkJoinPool.workers;
/* 935 */       Object localObject1 = null;
/*     */       Object localObject2;
/* 937 */       if (this.queueTop != this.queueBase) {
/* 938 */         localObject1 = this;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/* 939 */         if ((arrayOfForkJoinWorkerThread != null) && ((j = arrayOfForkJoinWorkerThread.length) > 1))
/*     */         {
/* 941 */           int k = nextSeed();
/* 942 */           int m = j << 1;
/* 943 */           for (int n = 0; n < m; n++)
/* 944 */             if (((localObject2 = arrayOfForkJoinWorkerThread[(n + k & j - 1)]) != null) && (((ForkJoinWorkerThread)localObject2).queueBase != ((ForkJoinWorkerThread)localObject2).queueTop))
/*     */             {
/* 946 */               localObject1 = localObject2;
/* 947 */               break;
/*     */             }
/*     */         }
/*     */       }
/* 951 */       if (localObject1 != null)
/*     */       {
/* 953 */         if (i == 0) {
/* 954 */           i = 1;
/* 955 */           localForkJoinPool.addActiveCount(1);
/*     */         }
/* 957 */         if ((localObject2 = this.locallyFifo ? locallyDeqTask() : localObject1 != this ? ((ForkJoinWorkerThread)localObject1).deqTask() : popTask()) != null)
/*     */         {
/* 959 */           this.currentSteal = ((ForkJoinTask)localObject2);
/* 960 */           ((ForkJoinTask)localObject2).doExec();
/* 961 */           this.currentSteal = localForkJoinTask;
/*     */         }
/*     */       }
/*     */       else {
/* 965 */         if (i != 0) {
/* 966 */           i = 0;
/* 967 */           localForkJoinPool.addActiveCount(-1);
/*     */         }
/* 969 */         if (localForkJoinPool.isQuiescent()) {
/* 970 */           localForkJoinPool.addActiveCount(1);
/* 971 */           localForkJoinPool.addQuiescerCount(-1);
/* 972 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     int i;
/*     */     try
/*     */     {
/* 986 */       UNSAFE = Unsafe.getUnsafe();
/* 987 */       ForkJoinTask[] arrayOfForkJoinTask = [Ljava.util.concurrent.ForkJoinTask.class;
/* 988 */       ABASE = UNSAFE.arrayBaseOffset(arrayOfForkJoinTask);
/* 989 */       i = UNSAFE.arrayIndexScale(arrayOfForkJoinTask);
/*     */     } catch (Exception localException) {
/* 991 */       throw new Error(localException);
/*     */     }
/* 993 */     if ((i & i - 1) != 0)
/* 994 */       throw new Error("data type scale not a power of two");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ForkJoinWorkerThread
 * JD-Core Version:    0.6.2
 */