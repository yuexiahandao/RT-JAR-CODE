/*      */ package java.util.concurrent.locks;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ 
/*      */ public class ReentrantReadWriteLock
/*      */   implements ReadWriteLock, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -6992448646407690164L;
/*      */   private final ReadLock readerLock;
/*      */   private final WriteLock writerLock;
/*      */   final Sync sync;
/*      */ 
/*      */   public ReentrantReadWriteLock()
/*      */   {
/*  233 */     this(false);
/*      */   }
/*      */ 
/*      */   public ReentrantReadWriteLock(boolean paramBoolean)
/*      */   {
/*  243 */     this.sync = (paramBoolean ? new FairSync() : new NonfairSync());
/*  244 */     this.readerLock = new ReadLock(this);
/*  245 */     this.writerLock = new WriteLock(this);
/*      */   }
/*      */   public WriteLock writeLock() {
/*  248 */     return this.writerLock; } 
/*  249 */   public ReadLock readLock() { return this.readerLock; }
/*      */ 
/*      */ 
/*      */   public final boolean isFair()
/*      */   {
/* 1232 */     return this.sync instanceof FairSync;
/*      */   }
/*      */ 
/*      */   protected Thread getOwner()
/*      */   {
/* 1249 */     return this.sync.getOwner();
/*      */   }
/*      */ 
/*      */   public int getReadLockCount()
/*      */   {
/* 1259 */     return this.sync.getReadLockCount();
/*      */   }
/*      */ 
/*      */   public boolean isWriteLocked()
/*      */   {
/* 1271 */     return this.sync.isWriteLocked();
/*      */   }
/*      */ 
/*      */   public boolean isWriteLockedByCurrentThread()
/*      */   {
/* 1281 */     return this.sync.isHeldExclusively();
/*      */   }
/*      */ 
/*      */   public int getWriteHoldCount()
/*      */   {
/* 1293 */     return this.sync.getWriteHoldCount();
/*      */   }
/*      */ 
/*      */   public int getReadHoldCount()
/*      */   {
/* 1306 */     return this.sync.getReadHoldCount();
/*      */   }
/*      */ 
/*      */   protected Collection<Thread> getQueuedWriterThreads()
/*      */   {
/* 1321 */     return this.sync.getExclusiveQueuedThreads();
/*      */   }
/*      */ 
/*      */   protected Collection<Thread> getQueuedReaderThreads()
/*      */   {
/* 1336 */     return this.sync.getSharedQueuedThreads();
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedThreads()
/*      */   {
/* 1350 */     return this.sync.hasQueuedThreads();
/*      */   }
/*      */ 
/*      */   public final boolean hasQueuedThread(Thread paramThread)
/*      */   {
/* 1365 */     return this.sync.isQueued(paramThread);
/*      */   }
/*      */ 
/*      */   public final int getQueueLength()
/*      */   {
/* 1379 */     return this.sync.getQueueLength();
/*      */   }
/*      */ 
/*      */   protected Collection<Thread> getQueuedThreads()
/*      */   {
/* 1394 */     return this.sync.getQueuedThreads();
/*      */   }
/*      */ 
/*      */   public boolean hasWaiters(Condition paramCondition)
/*      */   {
/* 1413 */     if (paramCondition == null)
/* 1414 */       throw new NullPointerException();
/* 1415 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 1416 */       throw new IllegalArgumentException("not owner");
/* 1417 */     return this.sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*      */   }
/*      */ 
/*      */   public int getWaitQueueLength(Condition paramCondition)
/*      */   {
/* 1436 */     if (paramCondition == null)
/* 1437 */       throw new NullPointerException();
/* 1438 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 1439 */       throw new IllegalArgumentException("not owner");
/* 1440 */     return this.sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*      */   }
/*      */ 
/*      */   protected Collection<Thread> getWaitingThreads(Condition paramCondition)
/*      */   {
/* 1461 */     if (paramCondition == null)
/* 1462 */       throw new NullPointerException();
/* 1463 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 1464 */       throw new IllegalArgumentException("not owner");
/* 1465 */     return this.sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1478 */     int i = this.sync.getCount();
/* 1479 */     int j = Sync.exclusiveCount(i);
/* 1480 */     int k = Sync.sharedCount(i);
/*      */ 
/* 1482 */     return super.toString() + "[Write locks = " + j + ", Read locks = " + k + "]";
/*      */   }
/*      */ 
/*      */   static final class FairSync extends ReentrantReadWriteLock.Sync
/*      */   {
/*      */     private static final long serialVersionUID = -2274990926593161451L;
/*      */ 
/*      */     final boolean writerShouldBlock()
/*      */     {
/*  696 */       return hasQueuedPredecessors();
/*      */     }
/*      */     final boolean readerShouldBlock() {
/*  699 */       return hasQueuedPredecessors();
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class NonfairSync extends ReentrantReadWriteLock.Sync
/*      */   {
/*      */     private static final long serialVersionUID = -8159625535654395037L;
/*      */ 
/*      */     final boolean writerShouldBlock()
/*      */     {
/*  676 */       return false;
/*      */     }
/*      */ 
/*      */     final boolean readerShouldBlock()
/*      */     {
/*  686 */       return apparentlyFirstQueuedIsExclusive();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ReadLock
/*      */     implements Lock, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -5992448646407690164L;
/*      */     private final ReentrantReadWriteLock.Sync sync;
/*      */ 
/*      */     protected ReadLock(ReentrantReadWriteLock paramReentrantReadWriteLock)
/*      */     {
/*  717 */       this.sync = paramReentrantReadWriteLock.sync;
/*      */     }
/*      */ 
/*      */     public void lock()
/*      */     {
/*  731 */       this.sync.acquireShared(1);
/*      */     }
/*      */ 
/*      */     public void lockInterruptibly()
/*      */       throws InterruptedException
/*      */     {
/*  776 */       this.sync.acquireSharedInterruptibly(1);
/*      */     }
/*      */ 
/*      */     public boolean tryLock()
/*      */     {
/*  803 */       return this.sync.tryReadLock();
/*      */     }
/*      */ 
/*      */     public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
/*      */       throws InterruptedException
/*      */     {
/*  873 */       return this.sync.tryAcquireSharedNanos(1, paramTimeUnit.toNanos(paramLong));
/*      */     }
/*      */ 
/*      */     public void unlock()
/*      */     {
/*  883 */       this.sync.releaseShared(1);
/*      */     }
/*      */ 
/*      */     public Condition newCondition()
/*      */     {
/*  893 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  904 */       int i = this.sync.getReadLockCount();
/*  905 */       return super.toString() + "[Read locks = " + i + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class Sync extends AbstractQueuedSynchronizer
/*      */   {
/*      */     private static final long serialVersionUID = 6317671515068378041L;
/*      */     static final int SHARED_SHIFT = 16;
/*      */     static final int SHARED_UNIT = 65536;
/*      */     static final int MAX_COUNT = 65535;
/*      */     static final int EXCLUSIVE_MASK = 65535;
/*      */     private transient ThreadLocalHoldCounter readHolds;
/*      */     private transient HoldCounter cachedHoldCounter;
/*  337 */     private transient Thread firstReader = null;
/*      */     private transient int firstReaderHoldCount;
/*      */ 
/*      */     static int sharedCount(int paramInt)
/*      */     {
/*  271 */       return paramInt >>> 16;
/*      */     }
/*  273 */     static int exclusiveCount(int paramInt) { return paramInt & 0xFFFF; }
/*      */ 
/*      */ 
/*      */     Sync()
/*      */     {
/*  341 */       this.readHolds = new ThreadLocalHoldCounter();
/*  342 */       setState(getState());
/*      */     }
/*      */ 
/*      */     abstract boolean readerShouldBlock();
/*      */ 
/*      */     abstract boolean writerShouldBlock();
/*      */ 
/*      */     protected final boolean tryRelease(int paramInt)
/*      */     {
/*  373 */       if (!isHeldExclusively())
/*  374 */         throw new IllegalMonitorStateException();
/*  375 */       int i = getState() - paramInt;
/*  376 */       boolean bool = exclusiveCount(i) == 0;
/*  377 */       if (bool)
/*  378 */         setExclusiveOwnerThread(null);
/*  379 */       setState(i);
/*  380 */       return bool;
/*      */     }
/*      */ 
/*      */     protected final boolean tryAcquire(int paramInt)
/*      */     {
/*  395 */       Thread localThread = Thread.currentThread();
/*  396 */       int i = getState();
/*  397 */       int j = exclusiveCount(i);
/*  398 */       if (i != 0)
/*      */       {
/*  400 */         if ((j == 0) || (localThread != getExclusiveOwnerThread()))
/*  401 */           return false;
/*  402 */         if (j + exclusiveCount(paramInt) > 65535) {
/*  403 */           throw new Error("Maximum lock count exceeded");
/*      */         }
/*  405 */         setState(i + paramInt);
/*  406 */         return true;
/*      */       }
/*  408 */       if ((writerShouldBlock()) || (!compareAndSetState(i, i + paramInt)))
/*      */       {
/*  410 */         return false;
/*  411 */       }setExclusiveOwnerThread(localThread);
/*  412 */       return true;
/*      */     }
/*      */ 
/*      */     protected final boolean tryReleaseShared(int paramInt) {
/*  416 */       Thread localThread = Thread.currentThread();
/*      */       int j;
/*  417 */       if (this.firstReader == localThread)
/*      */       {
/*  419 */         if (this.firstReaderHoldCount == 1)
/*  420 */           this.firstReader = null;
/*      */         else
/*  422 */           this.firstReaderHoldCount -= 1;
/*      */       } else {
/*  424 */         HoldCounter localHoldCounter = this.cachedHoldCounter;
/*  425 */         if ((localHoldCounter == null) || (localHoldCounter.tid != localThread.getId()))
/*  426 */           localHoldCounter = (HoldCounter)this.readHolds.get();
/*  427 */         j = localHoldCounter.count;
/*  428 */         if (j <= 1) {
/*  429 */           this.readHolds.remove();
/*  430 */           if (j <= 0)
/*  431 */             throw unmatchedUnlockException();
/*      */         }
/*  433 */         localHoldCounter.count -= 1;
/*      */       }
/*      */       while (true) {
/*  436 */         int i = getState();
/*  437 */         j = i - 65536;
/*  438 */         if (compareAndSetState(i, j))
/*      */         {
/*  442 */           return j == 0;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  447 */     private IllegalMonitorStateException unmatchedUnlockException() { return new IllegalMonitorStateException("attempt to unlock read lock, not locked by current thread"); }
/*      */ 
/*      */ 
/*      */     protected final int tryAcquireShared(int paramInt)
/*      */     {
/*  467 */       Thread localThread = Thread.currentThread();
/*  468 */       int i = getState();
/*  469 */       if ((exclusiveCount(i) != 0) && (getExclusiveOwnerThread() != localThread))
/*      */       {
/*  471 */         return -1;
/*  472 */       }int j = sharedCount(i);
/*  473 */       if ((!readerShouldBlock()) && (j < 65535) && (compareAndSetState(i, i + 65536)))
/*      */       {
/*  476 */         if (j == 0) {
/*  477 */           this.firstReader = localThread;
/*  478 */           this.firstReaderHoldCount = 1;
/*  479 */         } else if (this.firstReader == localThread) {
/*  480 */           this.firstReaderHoldCount += 1;
/*      */         } else {
/*  482 */           HoldCounter localHoldCounter = this.cachedHoldCounter;
/*  483 */           if ((localHoldCounter == null) || (localHoldCounter.tid != localThread.getId()))
/*  484 */             this.cachedHoldCounter = (localHoldCounter = (HoldCounter)this.readHolds.get());
/*  485 */           else if (localHoldCounter.count == 0)
/*  486 */             this.readHolds.set(localHoldCounter);
/*  487 */           localHoldCounter.count += 1;
/*      */         }
/*  489 */         return 1;
/*      */       }
/*  491 */       return fullTryAcquireShared(localThread);
/*      */     }
/*      */ 
/*      */     final int fullTryAcquireShared(Thread paramThread)
/*      */     {
/*  505 */       HoldCounter localHoldCounter = null;
/*      */       while (true) {
/*  507 */         int i = getState();
/*  508 */         if (exclusiveCount(i) != 0) {
/*  509 */           if (getExclusiveOwnerThread() != paramThread) {
/*  510 */             return -1;
/*      */           }
/*      */         }
/*  513 */         else if (readerShouldBlock())
/*      */         {
/*  515 */           if (this.firstReader != paramThread)
/*      */           {
/*  518 */             if (localHoldCounter == null) {
/*  519 */               localHoldCounter = this.cachedHoldCounter;
/*  520 */               if ((localHoldCounter == null) || (localHoldCounter.tid != paramThread.getId())) {
/*  521 */                 localHoldCounter = (HoldCounter)this.readHolds.get();
/*  522 */                 if (localHoldCounter.count == 0)
/*  523 */                   this.readHolds.remove();
/*      */               }
/*      */             }
/*  526 */             if (localHoldCounter.count == 0)
/*  527 */               return -1;
/*      */           }
/*      */         }
/*  530 */         if (sharedCount(i) == 65535)
/*  531 */           throw new Error("Maximum lock count exceeded");
/*  532 */         if (compareAndSetState(i, i + 65536)) {
/*  533 */           if (sharedCount(i) == 0) {
/*  534 */             this.firstReader = paramThread;
/*  535 */             this.firstReaderHoldCount = 1;
/*  536 */           } else if (this.firstReader == paramThread) {
/*  537 */             this.firstReaderHoldCount += 1;
/*      */           } else {
/*  539 */             if (localHoldCounter == null)
/*  540 */               localHoldCounter = this.cachedHoldCounter;
/*  541 */             if ((localHoldCounter == null) || (localHoldCounter.tid != paramThread.getId()))
/*  542 */               localHoldCounter = (HoldCounter)this.readHolds.get();
/*  543 */             else if (localHoldCounter.count == 0)
/*  544 */               this.readHolds.set(localHoldCounter);
/*  545 */             localHoldCounter.count += 1;
/*  546 */             this.cachedHoldCounter = localHoldCounter;
/*      */           }
/*  548 */           return 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     final boolean tryWriteLock()
/*      */     {
/*  559 */       Thread localThread = Thread.currentThread();
/*  560 */       int i = getState();
/*  561 */       if (i != 0) {
/*  562 */         int j = exclusiveCount(i);
/*  563 */         if ((j == 0) || (localThread != getExclusiveOwnerThread()))
/*  564 */           return false;
/*  565 */         if (j == 65535)
/*  566 */           throw new Error("Maximum lock count exceeded");
/*      */       }
/*  568 */       if (!compareAndSetState(i, i + 1))
/*  569 */         return false;
/*  570 */       setExclusiveOwnerThread(localThread);
/*  571 */       return true;
/*      */     }
/*      */ 
/*      */     final boolean tryReadLock()
/*      */     {
/*  580 */       Thread localThread = Thread.currentThread();
/*      */       while (true) {
/*  582 */         int i = getState();
/*  583 */         if ((exclusiveCount(i) != 0) && (getExclusiveOwnerThread() != localThread))
/*      */         {
/*  585 */           return false;
/*  586 */         }int j = sharedCount(i);
/*  587 */         if (j == 65535)
/*  588 */           throw new Error("Maximum lock count exceeded");
/*  589 */         if (compareAndSetState(i, i + 65536)) {
/*  590 */           if (j == 0) {
/*  591 */             this.firstReader = localThread;
/*  592 */             this.firstReaderHoldCount = 1;
/*  593 */           } else if (this.firstReader == localThread) {
/*  594 */             this.firstReaderHoldCount += 1;
/*      */           } else {
/*  596 */             HoldCounter localHoldCounter = this.cachedHoldCounter;
/*  597 */             if ((localHoldCounter == null) || (localHoldCounter.tid != localThread.getId()))
/*  598 */               this.cachedHoldCounter = (localHoldCounter = (HoldCounter)this.readHolds.get());
/*  599 */             else if (localHoldCounter.count == 0)
/*  600 */               this.readHolds.set(localHoldCounter);
/*  601 */             localHoldCounter.count += 1;
/*      */           }
/*  603 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected final boolean isHeldExclusively()
/*      */     {
/*  611 */       return getExclusiveOwnerThread() == Thread.currentThread();
/*      */     }
/*      */ 
/*      */     final AbstractQueuedSynchronizer.ConditionObject newCondition()
/*      */     {
/*  617 */       return new AbstractQueuedSynchronizer.ConditionObject(this);
/*      */     }
/*      */ 
/*      */     final Thread getOwner()
/*      */     {
/*  622 */       return exclusiveCount(getState()) == 0 ? null : getExclusiveOwnerThread();
/*      */     }
/*      */ 
/*      */     final int getReadLockCount()
/*      */     {
/*  628 */       return sharedCount(getState());
/*      */     }
/*      */ 
/*      */     final boolean isWriteLocked() {
/*  632 */       return exclusiveCount(getState()) != 0;
/*      */     }
/*      */ 
/*      */     final int getWriteHoldCount() {
/*  636 */       return isHeldExclusively() ? exclusiveCount(getState()) : 0;
/*      */     }
/*      */ 
/*      */     final int getReadHoldCount() {
/*  640 */       if (getReadLockCount() == 0) {
/*  641 */         return 0;
/*      */       }
/*  643 */       Thread localThread = Thread.currentThread();
/*  644 */       if (this.firstReader == localThread) {
/*  645 */         return this.firstReaderHoldCount;
/*      */       }
/*  647 */       HoldCounter localHoldCounter = this.cachedHoldCounter;
/*  648 */       if ((localHoldCounter != null) && (localHoldCounter.tid == localThread.getId())) {
/*  649 */         return localHoldCounter.count;
/*      */       }
/*  651 */       int i = ((HoldCounter)this.readHolds.get()).count;
/*  652 */       if (i == 0) this.readHolds.remove();
/*  653 */       return i;
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/*  662 */       paramObjectInputStream.defaultReadObject();
/*  663 */       this.readHolds = new ThreadLocalHoldCounter();
/*  664 */       setState(0);
/*      */     }
/*      */     final int getCount() {
/*  667 */       return getState();
/*      */     }
/*      */ 
/*      */     static final class HoldCounter
/*      */     {
/*  280 */       int count = 0;
/*      */ 
/*  282 */       final long tid = Thread.currentThread().getId();
/*      */     }
/*      */ 
/*      */     static final class ThreadLocalHoldCounter extends ThreadLocal<ReentrantReadWriteLock.Sync.HoldCounter>
/*      */     {
/*      */       public ReentrantReadWriteLock.Sync.HoldCounter initialValue()
/*      */       {
/*  292 */         return new ReentrantReadWriteLock.Sync.HoldCounter();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class WriteLock
/*      */     implements Lock, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -4992448646407690164L;
/*      */     private final ReentrantReadWriteLock.Sync sync;
/*      */ 
/*      */     protected WriteLock(ReentrantReadWriteLock paramReentrantReadWriteLock)
/*      */     {
/*  924 */       this.sync = paramReentrantReadWriteLock.sync;
/*      */     }
/*      */ 
/*      */     public void lock()
/*      */     {
/*  945 */       this.sync.acquire(1);
/*      */     }
/*      */ 
/*      */     public void lockInterruptibly()
/*      */       throws InterruptedException
/*      */     {
/* 1000 */       this.sync.acquireInterruptibly(1);
/*      */     }
/*      */ 
/*      */     public boolean tryLock()
/*      */     {
/* 1033 */       return this.sync.tryWriteLock();
/*      */     }
/*      */ 
/*      */     public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
/*      */       throws InterruptedException
/*      */     {
/* 1115 */       return this.sync.tryAcquireNanos(1, paramTimeUnit.toNanos(paramLong));
/*      */     }
/*      */ 
/*      */     public void unlock()
/*      */     {
/* 1131 */       this.sync.release(1);
/*      */     }
/*      */ 
/*      */     public Condition newCondition()
/*      */     {
/* 1178 */       return this.sync.newCondition();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1190 */       Thread localThread = this.sync.getOwner();
/* 1191 */       return super.toString() + (localThread == null ? "[Unlocked]" : new StringBuilder().append("[Locked by thread ").append(localThread.getName()).append("]").toString());
/*      */     }
/*      */ 
/*      */     public boolean isHeldByCurrentThread()
/*      */     {
/* 1206 */       return this.sync.isHeldExclusively();
/*      */     }
/*      */ 
/*      */     public int getHoldCount()
/*      */     {
/* 1220 */       return this.sync.getWriteHoldCount();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.ReentrantReadWriteLock
 * JD-Core Version:    0.6.2
 */