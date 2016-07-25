/*     */ package java.util.concurrent.locks;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public class ReentrantLock
/*     */   implements Lock, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7373984872572414699L;
/*     */   private final Sync sync;
/*     */ 
/*     */   public ReentrantLock()
/*     */   {
/* 262 */     this.sync = new NonfairSync();
/*     */   }
/*     */ 
/*     */   public ReentrantLock(boolean paramBoolean)
/*     */   {
/* 272 */     this.sync = (paramBoolean ? new FairSync() : new NonfairSync());
/*     */   }
/*     */ 
/*     */   public void lock()
/*     */   {
/* 290 */     this.sync.lock();
/*     */   }
/*     */ 
/*     */   public void lockInterruptibly()
/*     */     throws InterruptedException
/*     */   {
/* 340 */     this.sync.acquireInterruptibly(1);
/*     */   }
/*     */ 
/*     */   public boolean tryLock()
/*     */   {
/* 370 */     return this.sync.nonfairTryAcquire(1);
/*     */   }
/*     */ 
/*     */   public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 445 */     return this.sync.tryAcquireNanos(1, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public void unlock()
/*     */   {
/* 460 */     this.sync.release(1);
/*     */   }
/*     */ 
/*     */   public Condition newCondition()
/*     */   {
/* 503 */     return this.sync.newCondition();
/*     */   }
/*     */ 
/*     */   public int getHoldCount()
/*     */   {
/* 537 */     return this.sync.getHoldCount();
/*     */   }
/*     */ 
/*     */   public boolean isHeldByCurrentThread()
/*     */   {
/* 584 */     return this.sync.isHeldExclusively();
/*     */   }
/*     */ 
/*     */   public boolean isLocked()
/*     */   {
/* 596 */     return this.sync.isLocked();
/*     */   }
/*     */ 
/*     */   public final boolean isFair()
/*     */   {
/* 605 */     return this.sync instanceof FairSync;
/*     */   }
/*     */ 
/*     */   protected Thread getOwner()
/*     */   {
/* 622 */     return this.sync.getOwner();
/*     */   }
/*     */ 
/*     */   public final boolean hasQueuedThreads()
/*     */   {
/* 636 */     return this.sync.hasQueuedThreads();
/*     */   }
/*     */ 
/*     */   public final boolean hasQueuedThread(Thread paramThread)
/*     */   {
/* 652 */     return this.sync.isQueued(paramThread);
/*     */   }
/*     */ 
/*     */   public final int getQueueLength()
/*     */   {
/* 667 */     return this.sync.getQueueLength();
/*     */   }
/*     */ 
/*     */   protected Collection<Thread> getQueuedThreads()
/*     */   {
/* 682 */     return this.sync.getQueuedThreads();
/*     */   }
/*     */ 
/*     */   public boolean hasWaiters(Condition paramCondition)
/*     */   {
/* 701 */     if (paramCondition == null)
/* 702 */       throw new NullPointerException();
/* 703 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 704 */       throw new IllegalArgumentException("not owner");
/* 705 */     return this.sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*     */   }
/*     */ 
/*     */   public int getWaitQueueLength(Condition paramCondition)
/*     */   {
/* 724 */     if (paramCondition == null)
/* 725 */       throw new NullPointerException();
/* 726 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 727 */       throw new IllegalArgumentException("not owner");
/* 728 */     return this.sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*     */   }
/*     */ 
/*     */   protected Collection<Thread> getWaitingThreads(Condition paramCondition)
/*     */   {
/* 749 */     if (paramCondition == null)
/* 750 */       throw new NullPointerException();
/* 751 */     if (!(paramCondition instanceof AbstractQueuedSynchronizer.ConditionObject))
/* 752 */       throw new IllegalArgumentException("not owner");
/* 753 */     return this.sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)paramCondition);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 765 */     Thread localThread = this.sync.getOwner();
/* 766 */     return super.toString() + (localThread == null ? "[Unlocked]" : new StringBuilder().append("[Locked by thread ").append(localThread.getName()).append("]").toString());
/*     */   }
/*     */ 
/*     */   static final class FairSync extends ReentrantLock.Sync
/*     */   {
/*     */     private static final long serialVersionUID = -3000897897090466540L;
/*     */ 
/*     */     final void lock()
/*     */     {
/* 229 */       acquire(1);
/*     */     }
/*     */ 
/*     */     protected final boolean tryAcquire(int paramInt)
/*     */     {
/* 237 */       Thread localThread = Thread.currentThread();
/* 238 */       int i = getState();
/* 239 */       if (i == 0) {
/* 240 */         if ((!hasQueuedPredecessors()) && (compareAndSetState(0, paramInt)))
/*     */         {
/* 242 */           setExclusiveOwnerThread(localThread);
/* 243 */           return true;
/*     */         }
/*     */       }
/* 246 */       else if (localThread == getExclusiveOwnerThread()) {
/* 247 */         int j = i + paramInt;
/* 248 */         if (j < 0)
/* 249 */           throw new Error("Maximum lock count exceeded");
/* 250 */         setState(j);
/* 251 */         return true;
/*     */       }
/* 253 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NonfairSync extends ReentrantLock.Sync
/*     */   {
/*     */     private static final long serialVersionUID = 7316153563782823691L;
/*     */ 
/*     */     final void lock()
/*     */     {
/* 211 */       if (compareAndSetState(0, 1))
/* 212 */         setExclusiveOwnerThread(Thread.currentThread());
/*     */       else
/* 214 */         acquire(1);
/*     */     }
/*     */ 
/*     */     protected final boolean tryAcquire(int paramInt) {
/* 218 */       return nonfairTryAcquire(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Sync extends AbstractQueuedSynchronizer
/*     */   {
/*     */     private static final long serialVersionUID = -5179523762034025860L;
/*     */ 
/*     */     abstract void lock();
/*     */ 
/*     */     final boolean nonfairTryAcquire(int paramInt)
/*     */     {
/* 134 */       Thread localThread = Thread.currentThread();
/* 135 */       int i = getState();
/* 136 */       if (i == 0) {
/* 137 */         if (compareAndSetState(0, paramInt)) {
/* 138 */           setExclusiveOwnerThread(localThread);
/* 139 */           return true;
/*     */         }
/*     */       }
/* 142 */       else if (localThread == getExclusiveOwnerThread()) {
/* 143 */         int j = i + paramInt;
/* 144 */         if (j < 0)
/* 145 */           throw new Error("Maximum lock count exceeded");
/* 146 */         setState(j);
/* 147 */         return true;
/*     */       }
/* 149 */       return false;
/*     */     }
/*     */ 
/*     */     protected final boolean tryRelease(int paramInt) {
/* 153 */       int i = getState() - paramInt;
/* 154 */       if (Thread.currentThread() != getExclusiveOwnerThread())
/* 155 */         throw new IllegalMonitorStateException();
/* 156 */       boolean bool = false;
/* 157 */       if (i == 0) {
/* 158 */         bool = true;
/* 159 */         setExclusiveOwnerThread(null);
/*     */       }
/* 161 */       setState(i);
/* 162 */       return bool;
/*     */     }
/*     */ 
/*     */     protected final boolean isHeldExclusively()
/*     */     {
/* 168 */       return getExclusiveOwnerThread() == Thread.currentThread();
/*     */     }
/*     */ 
/*     */     final AbstractQueuedSynchronizer.ConditionObject newCondition() {
/* 172 */       return new AbstractQueuedSynchronizer.ConditionObject(this);
/*     */     }
/*     */ 
/*     */     final Thread getOwner()
/*     */     {
/* 178 */       return getState() == 0 ? null : getExclusiveOwnerThread();
/*     */     }
/*     */ 
/*     */     final int getHoldCount() {
/* 182 */       return isHeldExclusively() ? getState() : 0;
/*     */     }
/*     */ 
/*     */     final boolean isLocked() {
/* 186 */       return getState() != 0;
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 195 */       paramObjectInputStream.defaultReadObject();
/* 196 */       setState(0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.ReentrantLock
 * JD-Core Version:    0.6.2
 */