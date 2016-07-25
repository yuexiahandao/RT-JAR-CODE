/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class ForkJoinPool extends AbstractExecutorService
/*      */ {
/*      */   public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
/*      */   private static final RuntimePermission modifyThreadPermission;
/* 2145 */   private static final AtomicInteger poolNumberGenerator = new AtomicInteger();
/* 2146 */   static final Random workerSeedGenerator = new Random();
/*      */   ForkJoinWorkerThread[] workers;
/*      */   private static final int INITIAL_QUEUE_CAPACITY = 8;
/*      */   private static final int MAXIMUM_QUEUE_CAPACITY = 16777216;
/*      */   private ForkJoinTask<?>[] submissionQueue;
/*      */   private final ReentrantLock submissionLock;
/*      */   private final Condition termination;
/*      */   private final ForkJoinWorkerThreadFactory factory;
/*      */   final Thread.UncaughtExceptionHandler ueh;
/*      */   private final String workerNamePrefix;
/*      */   private volatile long stealCount;
/*      */   volatile long ctl;
/*      */   private static final int AC_SHIFT = 48;
/*      */   private static final int TC_SHIFT = 32;
/*      */   private static final int ST_SHIFT = 31;
/*      */   private static final int EC_SHIFT = 16;
/*      */   private static final int MAX_ID = 32767;
/*      */   private static final int SMASK = 65535;
/*      */   private static final int SHORT_SIGN = 32768;
/*      */   private static final int INT_SIGN = -2147483648;
/*      */   private static final long STOP_BIT = 2147483648L;
/*      */   private static final long AC_MASK = -281474976710656L;
/*      */   private static final long TC_MASK = 281470681743360L;
/*      */   private static final long TC_UNIT = 4294967296L;
/*      */   private static final long AC_UNIT = 281474976710656L;
/*      */   private static final int UAC_SHIFT = 16;
/*      */   private static final int UTC_SHIFT = 0;
/*      */   private static final int UAC_MASK = -65536;
/*      */   private static final int UTC_MASK = 65535;
/*      */   private static final int UAC_UNIT = 65536;
/*      */   private static final int UTC_UNIT = 1;
/*      */   private static final int E_MASK = 2147483647;
/*      */   private static final int EC_UNIT = 65536;
/*      */   final int parallelism;
/*      */   volatile int queueBase;
/*      */   int queueTop;
/*      */   volatile boolean shutdown;
/*      */   final boolean locallyFifo;
/*      */   volatile int quiescerCount;
/*      */   volatile int blockedCount;
/*      */   private volatile int nextWorkerNumber;
/*      */   private int nextWorkerIndex;
/*      */   volatile int scanGuard;
/*      */   private static final int SG_UNIT = 65536;
/*      */   private static final long SHRINK_RATE = 4000000000L;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long ctlOffset;
/*      */   private static final long stealCountOffset;
/*      */   private static final long blockedCountOffset;
/*      */   private static final long quiescerCountOffset;
/*      */   private static final long scanGuardOffset;
/*      */   private static final long nextWorkerNumberOffset;
/*      */   private static final long ABASE;
/* 2174 */   private static final int ASHIFT = 31 - Integer.numberOfLeadingZeros(i);
/*      */ 
/*      */   private static void checkPermission()
/*      */   {
/*  412 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  413 */     if (localSecurityManager != null)
/*  414 */       localSecurityManager.checkPermission(modifyThreadPermission);
/*      */   }
/*      */ 
/*      */   final void work(ForkJoinWorkerThread paramForkJoinWorkerThread)
/*      */   {
/*  641 */     boolean bool = false;
/*      */     long l;
/*  643 */     while ((!paramForkJoinWorkerThread.terminate) && ((int)(l = this.ctl) >= 0))
/*      */     {
/*      */       int i;
/*  645 */       if ((!bool) && ((i = (int)(l >> 48)) <= 0))
/*  646 */         bool = scan(paramForkJoinWorkerThread, i);
/*  647 */       else if (tryAwaitWork(paramForkJoinWorkerThread, l))
/*  648 */         bool = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void signalWork()
/*      */   {
/*      */     long l1;
/*      */     int i;
/*      */     int j;
/*  671 */     while (((((i = (int)(l1 = this.ctl)) | (j = (int)(l1 >>> 32))) & 0x80008000) == -2147450880) && (i >= 0))
/*  672 */       if (i > 0)
/*      */       {
/*      */         ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread;
/*      */         int k;
/*      */         ForkJoinWorkerThread localForkJoinWorkerThread;
/*  674 */         if (((arrayOfForkJoinWorkerThread = this.workers) != null) && ((k = (i ^ 0xFFFFFFFF) & 0xFFFF) < arrayOfForkJoinWorkerThread.length) && ((localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[k]) != null))
/*      */         {
/*  678 */           long l2 = localForkJoinWorkerThread.nextWait & 0x7FFFFFFF | j + 65536 << 32;
/*      */ 
/*  680 */           if ((localForkJoinWorkerThread.eventCount == i) && (UNSAFE.compareAndSwapLong(this, ctlOffset, l1, l2)))
/*      */           {
/*  682 */             localForkJoinWorkerThread.eventCount = (i + 65536 & 0x7FFFFFFF);
/*  683 */             if (!localForkJoinWorkerThread.parked) break;
/*  684 */             UNSAFE.unpark(localForkJoinWorkerThread);
/*      */           }
/*      */         }
/*      */       }
/*  688 */       else if (UNSAFE.compareAndSwapLong(this, ctlOffset, l1, (j + 1 & 0xFFFF | j + 65536 & 0xFFFF0000) << 32))
/*      */       {
/*  692 */         addWorker();
/*      */       }
/*      */   }
/*      */ 
/*      */   private boolean tryReleaseWaiter()
/*      */   {
/*      */     long l1;
/*      */     int i;
/*      */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread;
/*      */     int j;
/*      */     ForkJoinWorkerThread localForkJoinWorkerThread;
/*  706 */     if (((i = (int)(l1 = this.ctl)) > 0) && ((int)(l1 >> 48) < 0) && ((arrayOfForkJoinWorkerThread = this.workers) != null) && ((j = (i ^ 0xFFFFFFFF) & 0xFFFF) < arrayOfForkJoinWorkerThread.length) && ((localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[j]) != null))
/*      */     {
/*  711 */       long l2 = localForkJoinWorkerThread.nextWait & 0x7FFFFFFF | l1 + 281474976710656L & 0x0;
/*      */ 
/*  713 */       if ((localForkJoinWorkerThread.eventCount != i) || (!UNSAFE.compareAndSwapLong(this, ctlOffset, l1, l2)))
/*      */       {
/*  715 */         return false;
/*  716 */       }localForkJoinWorkerThread.eventCount = (i + 65536 & 0x7FFFFFFF);
/*  717 */       if (localForkJoinWorkerThread.parked)
/*  718 */         UNSAFE.unpark(localForkJoinWorkerThread);
/*      */     }
/*  720 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean scan(ForkJoinWorkerThread paramForkJoinWorkerThread, int paramInt)
/*      */   {
/*  745 */     int i = this.scanGuard;
/*  746 */     int j = (this.parallelism == 1 - paramInt) && (this.blockedCount == 0) ? 0 : i & 0xFFFF;
/*  747 */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.workers;
/*  748 */     if ((arrayOfForkJoinWorkerThread == null) || (arrayOfForkJoinWorkerThread.length <= j))
/*  749 */       return false;
/*  750 */     int k = paramForkJoinWorkerThread.seed; int m = k; for (int n = -(j + j); n <= j + j; n++)
/*      */     {
/*  752 */       ForkJoinWorkerThread localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[(m & j)];
/*      */       int i2;
/*      */       ForkJoinTask[] arrayOfForkJoinTask2;
/*      */       int i3;
/*  753 */       if ((localForkJoinWorkerThread != null) && ((i2 = localForkJoinWorkerThread.queueBase) != localForkJoinWorkerThread.queueTop) && ((arrayOfForkJoinTask2 = localForkJoinWorkerThread.queue) != null) && ((i3 = arrayOfForkJoinTask2.length - 1 & i2) >= 0))
/*      */       {
/*  755 */         long l2 = (i3 << ASHIFT) + ABASE;
/*      */         ForkJoinTask localForkJoinTask;
/*  756 */         if (((localForkJoinTask = arrayOfForkJoinTask2[i3]) != null) && (localForkJoinWorkerThread.queueBase == i2) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask2, l2, localForkJoinTask, null)))
/*      */         {
/*  758 */           int i4 = (localForkJoinWorkerThread.queueBase = i2 + 1) - localForkJoinWorkerThread.queueTop;
/*  759 */           localForkJoinWorkerThread.stealHint = paramForkJoinWorkerThread.poolIndex;
/*  760 */           if (i4 != 0)
/*  761 */             signalWork();
/*  762 */           paramForkJoinWorkerThread.execTask(localForkJoinTask);
/*      */         }
/*  764 */         k ^= k << 13; k ^= k >>> 17; paramForkJoinWorkerThread.seed = (k ^ k << 5);
/*  765 */         return false;
/*      */       }
/*  767 */       if (n < 0) {
/*  768 */         k ^= k << 13; k ^= k >>> 17; m = k ^= k << 5;
/*      */       }
/*      */       else {
/*  771 */         m++;
/*      */       }
/*      */     }
/*  773 */     if (this.scanGuard != i)
/*  774 */       return false;
/*      */     ForkJoinTask[] arrayOfForkJoinTask1;
/*      */     int i1;
/*  777 */     if (((n = this.queueBase) != this.queueTop) && ((arrayOfForkJoinTask1 = this.submissionQueue) != null) && ((i1 = arrayOfForkJoinTask1.length - 1 & n) >= 0))
/*      */     {
/*  780 */       long l1 = (i1 << ASHIFT) + ABASE;
/*      */       Object localObject;
/*  781 */       if (((localObject = arrayOfForkJoinTask1[i1]) != null) && (this.queueBase == n) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask1, l1, localObject, null)))
/*      */       {
/*  783 */         this.queueBase = (n + 1);
/*  784 */         paramForkJoinWorkerThread.execTask(localObject);
/*      */       }
/*  786 */       return false;
/*      */     }
/*  788 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean tryAwaitWork(ForkJoinWorkerThread paramForkJoinWorkerThread, long paramLong)
/*      */   {
/*  813 */     int i = paramForkJoinWorkerThread.eventCount;
/*  814 */     paramForkJoinWorkerThread.nextWait = ((int)paramLong);
/*  815 */     long l1 = i & 0x7FFFFFFF | paramLong - 281474976710656L & 0x0;
/*  816 */     if ((this.ctl != paramLong) || (!UNSAFE.compareAndSwapLong(this, ctlOffset, paramLong, l1))) {
/*  817 */       long l2 = this.ctl;
/*  818 */       return ((int)l2 != (int)paramLong) && ((l2 - paramLong & 0x0) >= 0L);
/*      */     }
/*  820 */     for (int j = paramForkJoinWorkerThread.stealCount; j != 0; ) {
/*  821 */       long l3 = this.stealCount;
/*  822 */       if (UNSAFE.compareAndSwapLong(this, stealCountOffset, l3, l3 + j))
/*  823 */         j = paramForkJoinWorkerThread.stealCount = 0;
/*  824 */       else if (paramForkJoinWorkerThread.eventCount != i)
/*  825 */         return true;
/*      */     }
/*  827 */     if (((!this.shutdown) || (!tryTerminate(false))) && ((int)paramLong != 0) && (this.parallelism + (int)(l1 >> 48) == 0) && (this.blockedCount == 0) && (this.quiescerCount == 0))
/*      */     {
/*  830 */       idleAwaitWork(paramForkJoinWorkerThread, l1, paramLong, i);
/*  831 */     }j = 0;
/*      */     while (true) { if (paramForkJoinWorkerThread.eventCount != i)
/*  833 */         return true;
/*  834 */       if (j == 0) {
/*  835 */         int k = this.scanGuard; int m = k & 0xFFFF;
/*  836 */         ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.workers;
/*  837 */         if ((arrayOfForkJoinWorkerThread != null) && (m < arrayOfForkJoinWorkerThread.length)) {
/*  838 */           j = 1;
/*  839 */           for (int n = 0; n <= m; n++) {
/*  840 */             ForkJoinWorkerThread localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[n];
/*  841 */             if (localForkJoinWorkerThread != null) {
/*  842 */               if ((localForkJoinWorkerThread.queueBase != localForkJoinWorkerThread.queueTop) && (!tryReleaseWaiter()))
/*      */               {
/*  844 */                 j = 0;
/*  845 */               }if (paramForkJoinWorkerThread.eventCount != i)
/*  846 */                 return true;
/*      */             }
/*      */           }
/*      */         }
/*  850 */         if ((this.scanGuard != k) || ((this.queueBase != this.queueTop) && (!tryReleaseWaiter())))
/*      */         {
/*  852 */           j = 0;
/*  853 */         }if (j == 0)
/*  854 */           Thread.yield();
/*      */         else
/*  856 */           Thread.interrupted();
/*      */       }
/*      */       else {
/*  859 */         paramForkJoinWorkerThread.parked = true;
/*  860 */         if (paramForkJoinWorkerThread.eventCount != i) {
/*  861 */           paramForkJoinWorkerThread.parked = false;
/*  862 */           return true;
/*      */         }
/*  864 */         LockSupport.park(this);
/*  865 */         j = paramForkJoinWorkerThread.parked = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void idleAwaitWork(ForkJoinWorkerThread paramForkJoinWorkerThread, long paramLong1, long paramLong2, int paramInt)
/*      */   {
/*  886 */     if (paramForkJoinWorkerThread.eventCount == paramInt) {
/*  887 */       if (this.shutdown)
/*  888 */         tryTerminate(false);
/*  889 */       ForkJoinTask.helpExpungeStaleExceptions();
/*  890 */       while (this.ctl == paramLong1) {
/*  891 */         long l = System.nanoTime();
/*  892 */         paramForkJoinWorkerThread.parked = true;
/*  893 */         if (paramForkJoinWorkerThread.eventCount == paramInt)
/*  894 */           LockSupport.parkNanos(this, 4000000000L);
/*  895 */         paramForkJoinWorkerThread.parked = false;
/*  896 */         if (paramForkJoinWorkerThread.eventCount != paramInt)
/*      */           break;
/*  898 */         if (System.nanoTime() - l < 3600000000L)
/*      */         {
/*  900 */           Thread.interrupted();
/*  901 */         } else if (UNSAFE.compareAndSwapLong(this, ctlOffset, paramLong1, paramLong2))
/*      */         {
/*  903 */           paramForkJoinWorkerThread.terminate = true;
/*  904 */           paramForkJoinWorkerThread.eventCount = ((int)paramLong1 + 65536 & 0x7FFFFFFF);
/*  905 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addSubmission(ForkJoinTask<?> paramForkJoinTask)
/*      */   {
/*  920 */     ReentrantLock localReentrantLock = this.submissionLock;
/*  921 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*      */       ForkJoinTask[] arrayOfForkJoinTask;
/*  924 */       if ((arrayOfForkJoinTask = this.submissionQueue) != null)
/*      */       {
/*      */         int i;
/*      */         int j;
/*  925 */         long l = (((i = this.queueTop) & (j = arrayOfForkJoinTask.length - 1)) << ASHIFT) + ABASE;
/*  926 */         UNSAFE.putOrderedObject(arrayOfForkJoinTask, l, paramForkJoinTask);
/*  927 */         this.queueTop = (i + 1);
/*  928 */         if (i - this.queueBase == j)
/*  929 */           growSubmissionQueue();
/*      */       }
/*      */     } finally {
/*  932 */       localReentrantLock.unlock();
/*      */     }
/*  934 */     signalWork();
/*      */   }
/*      */ 
/*      */   private void growSubmissionQueue()
/*      */   {
/*  944 */     ForkJoinTask[] arrayOfForkJoinTask1 = this.submissionQueue;
/*  945 */     int i = arrayOfForkJoinTask1 != null ? arrayOfForkJoinTask1.length << 1 : 8;
/*  946 */     if (i > 16777216)
/*  947 */       throw new RejectedExecutionException("Queue capacity exceeded");
/*  948 */     if (i < 8)
/*  949 */       i = 8;
/*  950 */     ForkJoinTask[] arrayOfForkJoinTask2 = this.submissionQueue = new ForkJoinTask[i];
/*  951 */     int j = i - 1;
/*  952 */     int k = this.queueTop;
/*      */     int m;
/*  954 */     if ((arrayOfForkJoinTask1 != null) && ((m = arrayOfForkJoinTask1.length - 1) >= 0))
/*  955 */       for (int n = this.queueBase; n != k; n++) {
/*  956 */         long l = ((n & m) << ASHIFT) + ABASE;
/*  957 */         Object localObject = UNSAFE.getObjectVolatile(arrayOfForkJoinTask1, l);
/*  958 */         if ((localObject != null) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask1, l, localObject, null)))
/*  959 */           UNSAFE.putObjectVolatile(arrayOfForkJoinTask2, ((n & j) << ASHIFT) + ABASE, localObject);
/*      */       }
/*      */   }
/*      */ 
/*      */   private boolean tryPreBlock()
/*      */   {
/*  976 */     int i = this.blockedCount;
/*  977 */     if (UNSAFE.compareAndSwapInt(this, blockedCountOffset, i, i + 1)) {
/*  978 */       int j = this.parallelism;
/*      */       do
/*      */       {
/*  982 */         long l1 = this.ctl;
/*  983 */         int i2 = (int)(l1 >>> 32);
/*      */         int k;
/*  984 */         if ((k = (int)l1) >= 0)
/*      */         {
/*      */           int m;
/*      */           ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread;
/*      */           int i1;
/*      */           ForkJoinWorkerThread localForkJoinWorkerThread;
/*      */           long l2;
/*  987 */           if (((m = i2 >> 16) <= 0) && (k != 0) && ((arrayOfForkJoinWorkerThread = this.workers) != null) && ((i1 = (k ^ 0xFFFFFFFF) & 0xFFFF) < arrayOfForkJoinWorkerThread.length) && ((localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[i1]) != null))
/*      */           {
/*  991 */             l2 = localForkJoinWorkerThread.nextWait & 0x7FFFFFFF | l1 & 0x0;
/*      */ 
/*  993 */             if ((localForkJoinWorkerThread.eventCount == k) && (UNSAFE.compareAndSwapLong(this, ctlOffset, l1, l2)))
/*      */             {
/*  995 */               localForkJoinWorkerThread.eventCount = (k + 65536 & 0x7FFFFFFF);
/*  996 */               if (localForkJoinWorkerThread.parked)
/*  997 */                 UNSAFE.unpark(localForkJoinWorkerThread);
/*  998 */               return true;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*      */             int n;
/* 1001 */             if (((n = (short)(i2 >>> 0)) >= 0) && (m + j > 1)) {
/* 1002 */               l2 = l1 - 281474976710656L & 0x0 | l1 & 0xFFFFFFFF;
/* 1003 */               if (UNSAFE.compareAndSwapLong(this, ctlOffset, l1, l2))
/* 1004 */                 return true;
/*      */             }
/* 1006 */             else if (n + j < 32767) {
/* 1007 */               l2 = l1 + 4294967296L & 0x0 | l1 & 0xFFFFFFFF;
/* 1008 */               if (UNSAFE.compareAndSwapLong(this, ctlOffset, l1, l2)) {
/* 1009 */                 addWorker();
/* 1010 */                 return true;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1014 */       while (!UNSAFE.compareAndSwapInt(this, blockedCountOffset, i = this.blockedCount, i - 1));
/*      */     }
/*      */ 
/* 1017 */     return false;
/*      */   }
/*      */ 
/*      */   private void postBlock()
/*      */   {
/*      */     long l;
/* 1025 */     while (!UNSAFE.compareAndSwapLong(this, ctlOffset, l = this.ctl, l + 281474976710656L));
/*      */     int i;
/* 1028 */     while (!UNSAFE.compareAndSwapInt(this, blockedCountOffset, i = this.blockedCount, i - 1));
/*      */   }
/*      */ 
/*      */   final void tryAwaitJoin(ForkJoinTask<?> paramForkJoinTask)
/*      */   {
/* 1040 */     Thread.interrupted();
/* 1041 */     if (paramForkJoinTask.status >= 0)
/* 1042 */       if (tryPreBlock()) {
/* 1043 */         paramForkJoinTask.tryAwaitDone(0L);
/* 1044 */         postBlock();
/*      */       }
/* 1046 */       else if ((this.ctl & 0x80000000) != 0L) {
/* 1047 */         paramForkJoinTask.cancelIgnoringExceptions();
/*      */       }
/*      */   }
/*      */ 
/*      */   final void timedAwaitJoin(ForkJoinTask<?> paramForkJoinTask, long paramLong)
/*      */   {
/* 1059 */     while (paramForkJoinTask.status >= 0) {
/* 1060 */       Thread.interrupted();
/* 1061 */       if ((this.ctl & 0x80000000) != 0L) {
/* 1062 */         paramForkJoinTask.cancelIgnoringExceptions();
/*      */       }
/* 1065 */       else if (tryPreBlock()) {
/* 1066 */         long l1 = System.nanoTime();
/* 1067 */         while (paramForkJoinTask.status >= 0) {
/* 1068 */           long l2 = TimeUnit.NANOSECONDS.toMillis(paramLong);
/* 1069 */           if (l2 <= 0L)
/*      */             break;
/* 1071 */           paramForkJoinTask.tryAwaitDone(l2);
/* 1072 */           if (paramForkJoinTask.status < 0)
/*      */             break;
/* 1074 */           if ((this.ctl & 0x80000000) != 0L) {
/* 1075 */             paramForkJoinTask.cancelIgnoringExceptions();
/* 1076 */             break;
/*      */           }
/* 1078 */           long l3 = System.nanoTime();
/* 1079 */           paramLong -= l3 - l1;
/* 1080 */           l1 = l3;
/*      */         }
/* 1082 */         postBlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void awaitBlocker(ManagedBlocker paramManagedBlocker)
/*      */     throws InterruptedException
/*      */   {
/* 1093 */     while (!paramManagedBlocker.isReleasable())
/* 1094 */       if (tryPreBlock())
/*      */         try {
/*      */           do if (paramManagedBlocker.isReleasable()) break; while (!paramManagedBlocker.block());
/*      */         } finally {
/* 1098 */           postBlock();
/*      */         }
/*      */   }
/*      */ 
/*      */   private void addWorker()
/*      */   {
/* 1112 */     Object localObject = null;
/* 1113 */     ForkJoinWorkerThread localForkJoinWorkerThread = null;
/*      */     try {
/* 1115 */       localForkJoinWorkerThread = this.factory.newThread(this);
/*      */     } catch (Throwable localThrowable) {
/* 1117 */       localObject = localThrowable;
/*      */     }
/* 1119 */     if (localForkJoinWorkerThread == null)
/*      */     {
/*      */       long l;
/* 1121 */       while (!UNSAFE.compareAndSwapLong(this, ctlOffset, l = this.ctl, l - 281474976710656L & 0x0 | l - 4294967296L & 0x0 | l & 0xFFFFFFFF));
/* 1127 */       if ((!tryTerminate(false)) && (localObject != null) && (!(Thread.currentThread() instanceof ForkJoinWorkerThread)))
/*      */       {
/* 1129 */         UNSAFE.throwException(localObject);
/*      */       }
/*      */     } else {
/* 1132 */       localForkJoinWorkerThread.start();
/*      */     }
/*      */   }
/*      */ 
/*      */   final String nextWorkerName()
/*      */   {
/*      */     int i;
/* 1141 */     while (!UNSAFE.compareAndSwapInt(this, nextWorkerNumberOffset, i = this.nextWorkerNumber, ++i));
/* 1143 */     return this.workerNamePrefix + i;
/*      */   }
/*      */ 
/*      */   final int registerWorker(ForkJoinWorkerThread paramForkJoinWorkerThread)
/*      */   {
/*      */     while (true)
/*      */     {
/*      */       int i;
/*      */       ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread;
/*      */       int k;
/*      */       int m;
/* 1165 */       if ((((i = this.scanGuard) & 0x10000) == 0) && (UNSAFE.compareAndSwapInt(this, scanGuardOffset, i, i | 0x10000)))
/*      */       {
/* 1168 */         int j = this.nextWorkerIndex;
/*      */         try {
/* 1170 */           if ((arrayOfForkJoinWorkerThread = this.workers) != null) {
/* 1171 */             k = arrayOfForkJoinWorkerThread.length;
/* 1172 */             if ((j < 0) || (j >= k) || (arrayOfForkJoinWorkerThread[j] != null)) {
/* 1173 */               for (j = 0; (j < k) && (arrayOfForkJoinWorkerThread[j] != null); j++);
/* 1175 */               if (j == k)
/* 1176 */                 arrayOfForkJoinWorkerThread = this.workers = (ForkJoinWorkerThread[])Arrays.copyOf(arrayOfForkJoinWorkerThread, k << 1);
/*      */             }
/* 1178 */             arrayOfForkJoinWorkerThread[j] = paramForkJoinWorkerThread;
/* 1179 */             this.nextWorkerIndex = (j + 1);
/* 1180 */             m = i & 0xFFFF;
/* 1181 */             i = j > m ? (m << 1) + 1 & 0xFFFF : i + 131072;
/*      */           }
/*      */         } finally {
/* 1184 */           this.scanGuard = i;
/*      */         }
/* 1186 */         return j;
/*      */       }
/* 1188 */       if ((arrayOfForkJoinWorkerThread = this.workers) != null)
/* 1189 */         for (Object localObject3 : arrayOfForkJoinWorkerThread)
/* 1190 */           if ((localObject3 != null) && (localObject3.queueBase != localObject3.queueTop) && 
/* 1191 */             (tryReleaseWaiter()))
/*      */             break;
/*      */     }
/*      */   }
/*      */ 
/*      */   final void deregisterWorker(ForkJoinWorkerThread paramForkJoinWorkerThread, Throwable paramThrowable)
/*      */   {
/* 1207 */     int i = paramForkJoinWorkerThread.poolIndex;
/* 1208 */     int j = paramForkJoinWorkerThread.stealCount;
/* 1209 */     int k = 0;
/*      */     do
/*      */     {
/*      */       int m;
/* 1215 */       if ((k == 0) && (((m = this.scanGuard) & 0x10000) == 0) && (UNSAFE.compareAndSwapInt(this, scanGuardOffset, m, m |= 65536)))
/*      */       {
/* 1218 */         ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.workers;
/* 1219 */         if ((arrayOfForkJoinWorkerThread != null) && (i >= 0) && (i < arrayOfForkJoinWorkerThread.length) && (arrayOfForkJoinWorkerThread[i] == paramForkJoinWorkerThread))
/*      */         {
/* 1221 */           arrayOfForkJoinWorkerThread[i] = null;
/* 1222 */         }this.nextWorkerIndex = i;
/* 1223 */         this.scanGuard = (m + 65536);
/* 1224 */         k = 1;
/*      */       }
/*      */       long l2;
/* 1226 */       if ((k == 1) && (UNSAFE.compareAndSwapLong(this, ctlOffset, l2 = this.ctl, l2 - 281474976710656L & 0x0 | l2 - 4294967296L & 0x0 | l2 & 0xFFFFFFFF)))
/*      */       {
/* 1231 */         k = 2;
/*      */       }
/*      */       long l1;
/* 1232 */       if ((j != 0) && (UNSAFE.compareAndSwapLong(this, stealCountOffset, l1 = this.stealCount, l1 + j)))
/*      */       {
/* 1235 */         j = 0; } 
/* 1236 */     }while ((k != 2) || (j != 0));
/* 1237 */     if (!tryTerminate(false))
/* 1238 */       if (paramThrowable != null)
/* 1239 */         signalWork();
/*      */       else
/* 1241 */         tryReleaseWaiter();
/*      */   }
/*      */ 
/*      */   private boolean tryTerminate(boolean paramBoolean)
/*      */   {
/*      */     long l;
/* 1256 */     while (((l = this.ctl) & 0x80000000) == 0L)
/* 1257 */       if (!paramBoolean) {
/* 1258 */         if ((int)(l >> 48) != -this.parallelism)
/* 1259 */           return false;
/* 1260 */         if ((!this.shutdown) || (this.blockedCount != 0) || (this.quiescerCount != 0) || (this.queueBase != this.queueTop))
/*      */         {
/* 1262 */           if (this.ctl != l) continue;
/* 1263 */           return false;
/*      */         }
/*      */ 
/*      */       }
/* 1267 */       else if (UNSAFE.compareAndSwapLong(this, ctlOffset, l, l | 0x80000000)) {
/* 1268 */         startTerminating();
/*      */       }
/* 1270 */     if ((short)(int)(l >>> 32) == -this.parallelism) {
/* 1271 */       ReentrantLock localReentrantLock = this.submissionLock;
/* 1272 */       localReentrantLock.lock();
/*      */       try {
/* 1274 */         this.termination.signalAll();
/*      */       } finally {
/* 1276 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/* 1279 */     return true;
/*      */   }
/*      */ 
/*      */   private void startTerminating()
/*      */   {
/* 1291 */     cancelSubmissions();
/* 1292 */     for (int i = 0; i < 3; i++) {
/* 1293 */       ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread1 = this.workers;
/* 1294 */       if (arrayOfForkJoinWorkerThread1 != null) {
/* 1295 */         for (ForkJoinWorkerThread localForkJoinWorkerThread : arrayOfForkJoinWorkerThread1)
/* 1296 */           if (localForkJoinWorkerThread != null) {
/* 1297 */             localForkJoinWorkerThread.terminate = true;
/* 1298 */             if (i > 0) {
/* 1299 */               localForkJoinWorkerThread.cancelTasks();
/* 1300 */               if ((i > 1) && (!localForkJoinWorkerThread.isInterrupted()))
/*      */                 try {
/* 1302 */                   localForkJoinWorkerThread.interrupt();
/*      */                 }
/*      */                 catch (SecurityException localSecurityException)
/*      */                 {
/*      */                 }
/*      */             }
/*      */           }
/* 1309 */         terminateWaiters();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void cancelSubmissions()
/*      */   {
/* 1318 */     while (this.queueBase != this.queueTop) {
/* 1319 */       ForkJoinTask localForkJoinTask = pollSubmission();
/* 1320 */       if (localForkJoinTask != null)
/*      */         try {
/* 1322 */           localForkJoinTask.cancel(false);
/*      */         }
/*      */         catch (Throwable localThrowable)
/*      */         {
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void terminateWaiters()
/*      */   {
/* 1334 */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread = this.workers;
/* 1335 */     if (arrayOfForkJoinWorkerThread != null)
/*      */     {
/* 1337 */       int k = arrayOfForkJoinWorkerThread.length;
/*      */       long l;
/*      */       int j;
/*      */       int i;
/*      */       ForkJoinWorkerThread localForkJoinWorkerThread;
/* 1339 */       while (((i = ((j = (int)(l = this.ctl)) ^ 0xFFFFFFFF) & 0xFFFF) < k) && ((localForkJoinWorkerThread = arrayOfForkJoinWorkerThread[i]) != null) && (localForkJoinWorkerThread.eventCount == (j & 0x7FFFFFFF)))
/* 1340 */         if (UNSAFE.compareAndSwapLong(this, ctlOffset, l, localForkJoinWorkerThread.nextWait & 0x7FFFFFFF | l + 281474976710656L & 0x0 | l & 0x80000000))
/*      */         {
/* 1344 */           localForkJoinWorkerThread.terminate = true;
/* 1345 */           localForkJoinWorkerThread.eventCount = (j + 65536);
/* 1346 */           if (localForkJoinWorkerThread.parked)
/* 1347 */             UNSAFE.unpark(localForkJoinWorkerThread);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void addQuiescerCount(int paramInt)
/*      */   {
/*      */     int i;
/* 1364 */     while (!UNSAFE.compareAndSwapInt(this, quiescerCountOffset, i = this.quiescerCount, i + paramInt));
/*      */   }
/*      */ 
/*      */   final void addActiveCount(int paramInt)
/*      */   {
/* 1376 */     long l1 = paramInt < 0 ? -281474976710656L : 281474976710656L;
/*      */     long l2;
/* 1378 */     while (!UNSAFE.compareAndSwapLong(this, ctlOffset, l2 = this.ctl, l2 + l1 & 0x0 | l2 & 0xFFFFFFFF));
/*      */   }
/*      */ 
/*      */   final int idlePerActive()
/*      */   {
/* 1389 */     int i = this.parallelism;
/* 1390 */     int j = i + (int)(this.ctl >> 48);
/* 1391 */     return j > i >>>= 1 ? 4 : j > i >>>= 1 ? 2 : j > i >>>= 1 ? 1 : j > i >>>= 1 ? 0 : 8;
/*      */   }
/*      */ 
/*      */   public ForkJoinPool()
/*      */   {
/* 1414 */     this(Runtime.getRuntime().availableProcessors(), defaultForkJoinWorkerThreadFactory, null, false);
/*      */   }
/*      */ 
/*      */   public ForkJoinPool(int paramInt)
/*      */   {
/* 1433 */     this(paramInt, defaultForkJoinWorkerThreadFactory, null, false);
/*      */   }
/*      */ 
/*      */   public ForkJoinPool(int paramInt, ForkJoinWorkerThreadFactory paramForkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler paramUncaughtExceptionHandler, boolean paramBoolean)
/*      */   {
/* 1464 */     checkPermission();
/* 1465 */     if (paramForkJoinWorkerThreadFactory == null)
/* 1466 */       throw new NullPointerException();
/* 1467 */     if ((paramInt <= 0) || (paramInt > 32767))
/* 1468 */       throw new IllegalArgumentException();
/* 1469 */     this.parallelism = paramInt;
/* 1470 */     this.factory = paramForkJoinWorkerThreadFactory;
/* 1471 */     this.ueh = paramUncaughtExceptionHandler;
/* 1472 */     this.locallyFifo = paramBoolean;
/* 1473 */     long l = -paramInt;
/* 1474 */     this.ctl = (l << 48 & 0x0 | l << 32 & 0x0);
/* 1475 */     this.submissionQueue = new ForkJoinTask[8];
/*      */ 
/* 1477 */     int i = paramInt << 1;
/* 1478 */     if (i >= 32767) {
/* 1479 */       i = 32767;
/*      */     } else {
/* 1481 */       i |= i >>> 1; i |= i >>> 2; i |= i >>> 4; i |= i >>> 8;
/*      */     }
/* 1483 */     this.workers = new ForkJoinWorkerThread[i + 1];
/* 1484 */     this.submissionLock = new ReentrantLock();
/* 1485 */     this.termination = this.submissionLock.newCondition();
/* 1486 */     StringBuilder localStringBuilder = new StringBuilder("ForkJoinPool-");
/* 1487 */     localStringBuilder.append(poolNumberGenerator.incrementAndGet());
/* 1488 */     localStringBuilder.append("-worker-");
/* 1489 */     this.workerNamePrefix = localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public <T> T invoke(ForkJoinTask<T> paramForkJoinTask)
/*      */   {
/* 1511 */     Thread localThread = Thread.currentThread();
/* 1512 */     if (paramForkJoinTask == null)
/* 1513 */       throw new NullPointerException();
/* 1514 */     if (this.shutdown)
/* 1515 */       throw new RejectedExecutionException();
/* 1516 */     if (((localThread instanceof ForkJoinWorkerThread)) && (((ForkJoinWorkerThread)localThread).pool == this))
/*      */     {
/* 1518 */       return paramForkJoinTask.invoke();
/*      */     }
/* 1520 */     addSubmission(paramForkJoinTask);
/* 1521 */     return paramForkJoinTask.join();
/*      */   }
/*      */ 
/*      */   private <T> void forkOrSubmit(ForkJoinTask<T> paramForkJoinTask)
/*      */   {
/* 1531 */     Thread localThread = Thread.currentThread();
/* 1532 */     if (this.shutdown)
/* 1533 */       throw new RejectedExecutionException();
/*      */     ForkJoinWorkerThread localForkJoinWorkerThread;
/* 1534 */     if (((localThread instanceof ForkJoinWorkerThread)) && ((localForkJoinWorkerThread = (ForkJoinWorkerThread)localThread).pool == this))
/*      */     {
/* 1536 */       localForkJoinWorkerThread.pushTask(paramForkJoinTask);
/*      */     }
/* 1538 */     else addSubmission(paramForkJoinTask);
/*      */   }
/*      */ 
/*      */   public void execute(ForkJoinTask<?> paramForkJoinTask)
/*      */   {
/* 1550 */     if (paramForkJoinTask == null)
/* 1551 */       throw new NullPointerException();
/* 1552 */     forkOrSubmit(paramForkJoinTask);
/*      */   }
/*      */ 
/*      */   public void execute(Runnable paramRunnable)
/*      */   {
/* 1563 */     if (paramRunnable == null)
/* 1564 */       throw new NullPointerException();
/*      */     ForkJoinTask localForkJoinTask;
/* 1566 */     if ((paramRunnable instanceof ForkJoinTask))
/* 1567 */       localForkJoinTask = (ForkJoinTask)paramRunnable;
/*      */     else
/* 1569 */       localForkJoinTask = ForkJoinTask.adapt(paramRunnable, null);
/* 1570 */     forkOrSubmit(localForkJoinTask);
/*      */   }
/*      */ 
/*      */   public <T> ForkJoinTask<T> submit(ForkJoinTask<T> paramForkJoinTask)
/*      */   {
/* 1583 */     if (paramForkJoinTask == null)
/* 1584 */       throw new NullPointerException();
/* 1585 */     forkOrSubmit(paramForkJoinTask);
/* 1586 */     return paramForkJoinTask;
/*      */   }
/*      */ 
/*      */   public <T> ForkJoinTask<T> submit(Callable<T> paramCallable)
/*      */   {
/* 1595 */     if (paramCallable == null)
/* 1596 */       throw new NullPointerException();
/* 1597 */     ForkJoinTask localForkJoinTask = ForkJoinTask.adapt(paramCallable);
/* 1598 */     forkOrSubmit(localForkJoinTask);
/* 1599 */     return localForkJoinTask;
/*      */   }
/*      */ 
/*      */   public <T> ForkJoinTask<T> submit(Runnable paramRunnable, T paramT)
/*      */   {
/* 1608 */     if (paramRunnable == null)
/* 1609 */       throw new NullPointerException();
/* 1610 */     ForkJoinTask localForkJoinTask = ForkJoinTask.adapt(paramRunnable, paramT);
/* 1611 */     forkOrSubmit(localForkJoinTask);
/* 1612 */     return localForkJoinTask;
/*      */   }
/*      */ 
/*      */   public ForkJoinTask<?> submit(Runnable paramRunnable)
/*      */   {
/* 1621 */     if (paramRunnable == null)
/* 1622 */       throw new NullPointerException();
/*      */     ForkJoinTask localForkJoinTask;
/* 1624 */     if ((paramRunnable instanceof ForkJoinTask))
/* 1625 */       localForkJoinTask = (ForkJoinTask)paramRunnable;
/*      */     else
/* 1627 */       localForkJoinTask = ForkJoinTask.adapt(paramRunnable, null);
/* 1628 */     forkOrSubmit(localForkJoinTask);
/* 1629 */     return localForkJoinTask;
/*      */   }
/*      */ 
/*      */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection)
/*      */   {
/* 1637 */     ArrayList localArrayList = new ArrayList(paramCollection.size());
/*      */ 
/* 1639 */     for (Object localObject = paramCollection.iterator(); ((Iterator)localObject).hasNext(); ) { Callable localCallable = (Callable)((Iterator)localObject).next();
/* 1640 */       localArrayList.add(ForkJoinTask.adapt(localCallable)); }
/* 1641 */     invoke(new InvokeAll(localArrayList));
/*      */ 
/* 1644 */     localObject = localArrayList;
/* 1645 */     return localObject;
/*      */   }
/*      */ 
/*      */   public ForkJoinWorkerThreadFactory getFactory()
/*      */   {
/* 1664 */     return this.factory;
/*      */   }
/*      */ 
/*      */   public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler()
/*      */   {
/* 1674 */     return this.ueh;
/*      */   }
/*      */ 
/*      */   public int getParallelism()
/*      */   {
/* 1683 */     return this.parallelism;
/*      */   }
/*      */ 
/*      */   public int getPoolSize()
/*      */   {
/* 1695 */     return this.parallelism + (short)(int)(this.ctl >>> 32);
/*      */   }
/*      */ 
/*      */   public boolean getAsyncMode()
/*      */   {
/* 1705 */     return this.locallyFifo;
/*      */   }
/*      */ 
/*      */   public int getRunningThreadCount()
/*      */   {
/* 1717 */     int i = this.parallelism + (int)(this.ctl >> 48);
/* 1718 */     return i <= 0 ? 0 : i;
/*      */   }
/*      */ 
/*      */   public int getActiveThreadCount()
/*      */   {
/* 1729 */     int i = this.parallelism + (int)(this.ctl >> 48) + this.blockedCount;
/* 1730 */     return i <= 0 ? 0 : i;
/*      */   }
/*      */ 
/*      */   public boolean isQuiescent()
/*      */   {
/* 1745 */     return this.parallelism + (int)(this.ctl >> 48) + this.blockedCount == 0;
/*      */   }
/*      */ 
/*      */   public long getStealCount()
/*      */   {
/* 1760 */     return this.stealCount;
/*      */   }
/*      */ 
/*      */   public long getQueuedTaskCount()
/*      */   {
/* 1774 */     long l = 0L;
/*      */     ForkJoinWorkerThread[] arrayOfForkJoinWorkerThread1;
/* 1776 */     if (((short)(int)(this.ctl >>> 32) > -this.parallelism) && ((arrayOfForkJoinWorkerThread1 = this.workers) != null))
/*      */     {
/* 1778 */       for (ForkJoinWorkerThread localForkJoinWorkerThread : arrayOfForkJoinWorkerThread1)
/* 1779 */         if (localForkJoinWorkerThread != null)
/* 1780 */           l -= localForkJoinWorkerThread.queueBase - localForkJoinWorkerThread.queueTop;
/*      */     }
/* 1782 */     return l;
/*      */   }
/*      */ 
/*      */   public int getQueuedSubmissionCount()
/*      */   {
/* 1793 */     return -this.queueBase + this.queueTop;
/*      */   }
/*      */ 
/*      */   public boolean hasQueuedSubmissions()
/*      */   {
/* 1803 */     return this.queueBase != this.queueTop;
/*      */   }
/*      */ 
/*      */   protected ForkJoinTask<?> pollSubmission()
/*      */   {
/*      */     int i;
/*      */     ForkJoinTask[] arrayOfForkJoinTask;
/*      */     int j;
/* 1816 */     while (((i = this.queueBase) != this.queueTop) && ((arrayOfForkJoinTask = this.submissionQueue) != null) && ((j = arrayOfForkJoinTask.length - 1 & i) >= 0))
/*      */     {
/* 1818 */       long l = (j << ASHIFT) + ABASE;
/*      */       ForkJoinTask localForkJoinTask;
/* 1819 */       if (((localForkJoinTask = arrayOfForkJoinTask[j]) != null) && (this.queueBase == i) && (UNSAFE.compareAndSwapObject(arrayOfForkJoinTask, l, localForkJoinTask, null)))
/*      */       {
/* 1822 */         this.queueBase = (i + 1);
/* 1823 */         return localForkJoinTask;
/*      */       }
/*      */     }
/* 1826 */     return null;
/*      */   }
/*      */ 
/*      */   protected int drainTasksTo(Collection<? super ForkJoinTask<?>> paramCollection)
/*      */   {
/* 1847 */     int i = 0;
/*      */     Object localObject1;
/* 1848 */     while (this.queueBase != this.queueTop) {
/* 1849 */       localObject1 = pollSubmission();
/* 1850 */       if (localObject1 != null) {
/* 1851 */         paramCollection.add(localObject1);
/* 1852 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1856 */     if (((short)(int)(this.ctl >>> 32) > -this.parallelism) && ((localObject1 = this.workers) != null))
/*      */     {
/* 1858 */       for (Object localObject3 : localObject1)
/* 1859 */         if (localObject3 != null)
/* 1860 */           i += localObject3.drainTasksTo(paramCollection);
/*      */     }
/* 1862 */     return i;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1873 */     long l1 = getStealCount();
/* 1874 */     long l2 = getQueuedTaskCount();
/* 1875 */     long l3 = getQueuedSubmissionCount();
/* 1876 */     int i = this.parallelism;
/* 1877 */     long l4 = this.ctl;
/* 1878 */     int j = i + (short)(int)(l4 >>> 32);
/* 1879 */     int k = i + (int)(l4 >> 48);
/* 1880 */     if (k < 0)
/* 1881 */       k = 0;
/* 1882 */     int m = k + this.blockedCount;
/*      */     String str;
/* 1884 */     if ((l4 & 0x80000000) != 0L)
/* 1885 */       str = j == 0 ? "Terminated" : "Terminating";
/*      */     else
/* 1887 */       str = this.shutdown ? "Shutting down" : "Running";
/* 1888 */     return super.toString() + "[" + str + ", parallelism = " + i + ", size = " + j + ", active = " + m + ", running = " + k + ", steals = " + l1 + ", tasks = " + l2 + ", submissions = " + l3 + "]";
/*      */   }
/*      */ 
/*      */   public void shutdown()
/*      */   {
/* 1913 */     checkPermission();
/* 1914 */     this.shutdown = true;
/* 1915 */     tryTerminate(false);
/*      */   }
/*      */ 
/*      */   public List<Runnable> shutdownNow()
/*      */   {
/* 1935 */     checkPermission();
/* 1936 */     this.shutdown = true;
/* 1937 */     tryTerminate(true);
/* 1938 */     return Collections.emptyList();
/*      */   }
/*      */ 
/*      */   public boolean isTerminated()
/*      */   {
/* 1947 */     long l = this.ctl;
/* 1948 */     return ((l & 0x80000000) != 0L) && ((short)(int)(l >>> 32) == -this.parallelism);
/*      */   }
/*      */ 
/*      */   public boolean isTerminating()
/*      */   {
/* 1966 */     long l = this.ctl;
/* 1967 */     return ((l & 0x80000000) != 0L) && ((short)(int)(l >>> 32) != -this.parallelism);
/*      */   }
/*      */ 
/*      */   final boolean isAtLeastTerminating()
/*      */   {
/* 1975 */     return (this.ctl & 0x80000000) != 0L;
/*      */   }
/*      */ 
/*      */   public boolean isShutdown()
/*      */   {
/* 1984 */     return this.shutdown;
/*      */   }
/*      */ 
/*      */   public boolean awaitTermination(long paramLong, TimeUnit paramTimeUnit)
/*      */     throws InterruptedException
/*      */   {
/* 2000 */     long l = paramTimeUnit.toNanos(paramLong);
/* 2001 */     ReentrantLock localReentrantLock = this.submissionLock;
/* 2002 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*      */       while (true)
/*      */       {
/*      */         boolean bool;
/* 2005 */         if (isTerminated())
/* 2006 */           return true;
/* 2007 */         if (l <= 0L)
/* 2008 */           return false;
/* 2009 */         l = this.termination.awaitNanos(l);
/*      */       }
/*      */     } finally {
/* 2012 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void managedBlock(ManagedBlocker paramManagedBlocker)
/*      */     throws InterruptedException
/*      */   {
/* 2111 */     Thread localThread = Thread.currentThread();
/* 2112 */     if ((localThread instanceof ForkJoinWorkerThread)) {
/* 2113 */       ForkJoinWorkerThread localForkJoinWorkerThread = (ForkJoinWorkerThread)localThread;
/* 2114 */       localForkJoinWorkerThread.pool.awaitBlocker(paramManagedBlocker);
/*      */     }
/*      */     else {
/* 2117 */       while ((!paramManagedBlocker.isReleasable()) && (!paramManagedBlocker.block()));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected <T> RunnableFuture<T> newTaskFor(Runnable paramRunnable, T paramT)
/*      */   {
/* 2126 */     return (RunnableFuture)ForkJoinTask.adapt(paramRunnable, paramT);
/*      */   }
/*      */ 
/*      */   protected <T> RunnableFuture<T> newTaskFor(Callable<T> paramCallable) {
/* 2130 */     return (RunnableFuture)ForkJoinTask.adapt(paramCallable);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 2147 */     modifyThreadPermission = new RuntimePermission("modifyThread");
/* 2148 */     defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
/*      */     int i;
/*      */     try
/*      */     {
/* 2152 */       UNSAFE = Unsafe.getUnsafe();
/* 2153 */       ForkJoinPool localForkJoinPool = ForkJoinPool.class;
/* 2154 */       ctlOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("ctl"));
/*      */ 
/* 2156 */       stealCountOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("stealCount"));
/*      */ 
/* 2158 */       blockedCountOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("blockedCount"));
/*      */ 
/* 2160 */       quiescerCountOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("quiescerCount"));
/*      */ 
/* 2162 */       scanGuardOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("scanGuard"));
/*      */ 
/* 2164 */       nextWorkerNumberOffset = UNSAFE.objectFieldOffset(localForkJoinPool.getDeclaredField("nextWorkerNumber"));
/*      */ 
/* 2166 */       ForkJoinTask[] arrayOfForkJoinTask = [Ljava.util.concurrent.ForkJoinTask.class;
/* 2167 */       ABASE = UNSAFE.arrayBaseOffset(arrayOfForkJoinTask);
/* 2168 */       i = UNSAFE.arrayIndexScale(arrayOfForkJoinTask);
/*      */     } catch (Exception localException) {
/* 2170 */       throw new Error(localException);
/*      */     }
/* 2172 */     if ((i & i - 1) != 0)
/* 2173 */       throw new Error("data type scale not a power of two");
/*      */   }
/*      */ 
/*      */   static class DefaultForkJoinWorkerThreadFactory
/*      */     implements ForkJoinPool.ForkJoinWorkerThreadFactory
/*      */   {
/*      */     public ForkJoinWorkerThread newThread(ForkJoinPool paramForkJoinPool)
/*      */     {
/*  390 */       return new ForkJoinWorkerThread(paramForkJoinPool);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface ForkJoinWorkerThreadFactory
/*      */   {
/*      */     public abstract ForkJoinWorkerThread newThread(ForkJoinPool paramForkJoinPool);
/*      */   }
/*      */ 
/*      */   static final class InvokeAll<T> extends RecursiveAction
/*      */   {
/*      */     final ArrayList<ForkJoinTask<T>> tasks;
/*      */     private static final long serialVersionUID = -7914297376763021607L;
/*      */ 
/*      */     InvokeAll(ArrayList<ForkJoinTask<T>> paramArrayList)
/*      */     {
/* 1650 */       this.tasks = paramArrayList;
/*      */     }
/*      */     public void compute() { try { invokeAll(this.tasks); }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface ManagedBlocker
/*      */   {
/*      */     public abstract boolean block()
/*      */       throws InterruptedException;
/*      */ 
/*      */     public abstract boolean isReleasable();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ForkJoinPool
 * JD-Core Version:    0.6.2
 */