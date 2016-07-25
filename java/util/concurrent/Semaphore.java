/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*     */ 
/*     */ public class Semaphore
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3222578661600680210L;
/*     */   private final Sync sync;
/*     */ 
/*     */   public Semaphore(int paramInt)
/*     */   {
/* 270 */     this.sync = new NonfairSync(paramInt);
/*     */   }
/*     */ 
/*     */   public Semaphore(int paramInt, boolean paramBoolean)
/*     */   {
/* 285 */     this.sync = (paramBoolean ? new FairSync(paramInt) : new NonfairSync(paramInt));
/*     */   }
/*     */ 
/*     */   public void acquire()
/*     */     throws InterruptedException
/*     */   {
/* 317 */     this.sync.acquireSharedInterruptibly(1);
/*     */   }
/*     */ 
/*     */   public void acquireUninterruptibly()
/*     */   {
/* 340 */     this.sync.acquireShared(1);
/*     */   }
/*     */ 
/*     */   public boolean tryAcquire()
/*     */   {
/* 368 */     return this.sync.nonfairTryAcquireShared(1) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean tryAcquire(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 414 */     return this.sync.tryAcquireSharedNanos(1, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public void release()
/*     */   {
/* 431 */     this.sync.releaseShared(1);
/*     */   }
/*     */ 
/*     */   public void acquire(int paramInt)
/*     */     throws InterruptedException
/*     */   {
/* 471 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 472 */     this.sync.acquireSharedInterruptibly(paramInt);
/*     */   }
/*     */ 
/*     */   public void acquireUninterruptibly(int paramInt)
/*     */   {
/* 499 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 500 */     this.sync.acquireShared(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean tryAcquire(int paramInt)
/*     */   {
/* 531 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 532 */     return this.sync.nonfairTryAcquireShared(paramInt) >= 0;
/*     */   }
/*     */ 
/*     */   public boolean tryAcquire(int paramInt, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 587 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 588 */     return this.sync.tryAcquireSharedNanos(paramInt, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public void release(int paramInt)
/*     */   {
/* 614 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 615 */     this.sync.releaseShared(paramInt);
/*     */   }
/*     */ 
/*     */   public int availablePermits()
/*     */   {
/* 626 */     return this.sync.getPermits();
/*     */   }
/*     */ 
/*     */   public int drainPermits()
/*     */   {
/* 635 */     return this.sync.drainPermits();
/*     */   }
/*     */ 
/*     */   protected void reducePermits(int paramInt)
/*     */   {
/* 649 */     if (paramInt < 0) throw new IllegalArgumentException();
/* 650 */     this.sync.reducePermits(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isFair()
/*     */   {
/* 659 */     return this.sync instanceof FairSync;
/*     */   }
/*     */ 
/*     */   public final boolean hasQueuedThreads()
/*     */   {
/* 673 */     return this.sync.hasQueuedThreads();
/*     */   }
/*     */ 
/*     */   public final int getQueueLength()
/*     */   {
/* 686 */     return this.sync.getQueueLength();
/*     */   }
/*     */ 
/*     */   protected Collection<Thread> getQueuedThreads()
/*     */   {
/* 700 */     return this.sync.getQueuedThreads();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 711 */     return super.toString() + "[Permits = " + this.sync.getPermits() + "]";
/*     */   }
/*     */ 
/*     */   static final class FairSync extends Semaphore.Sync
/*     */   {
/*     */     private static final long serialVersionUID = 2014338818796000944L;
/*     */ 
/*     */     FairSync(int paramInt)
/*     */     {
/* 245 */       super();
/*     */     }
/*     */ 
/*     */     protected int tryAcquireShared(int paramInt) {
/*     */       while (true) {
/* 250 */         if (hasQueuedPredecessors())
/* 251 */           return -1;
/* 252 */         int i = getState();
/* 253 */         int j = i - paramInt;
/* 254 */         if ((j < 0) || (compareAndSetState(i, j)))
/*     */         {
/* 256 */           return j;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class NonfairSync extends Semaphore.Sync
/*     */   {
/*     */     private static final long serialVersionUID = -2694183684443567898L;
/*     */ 
/*     */     NonfairSync(int paramInt)
/*     */     {
/* 230 */       super();
/*     */     }
/*     */ 
/*     */     protected int tryAcquireShared(int paramInt) {
/* 234 */       return nonfairTryAcquireShared(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Sync extends AbstractQueuedSynchronizer
/*     */   {
/*     */     private static final long serialVersionUID = 1192457210091910933L;
/*     */ 
/*     */     Sync(int paramInt)
/*     */     {
/* 175 */       setState(paramInt);
/*     */     }
/*     */ 
/*     */     final int getPermits() {
/* 179 */       return getState();
/*     */     }
/*     */ 
/*     */     final int nonfairTryAcquireShared(int paramInt) {
/*     */       while (true) {
/* 184 */         int i = getState();
/* 185 */         int j = i - paramInt;
/* 186 */         if ((j < 0) || (compareAndSetState(i, j)))
/*     */         {
/* 188 */           return j;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected final boolean tryReleaseShared(int paramInt) {
/*     */       while (true) { int i = getState();
/* 195 */         int j = i + paramInt;
/* 196 */         if (j < i)
/* 197 */           throw new Error("Maximum permit count exceeded");
/* 198 */         if (compareAndSetState(i, j))
/* 199 */           return true; }
/*     */     }
/*     */ 
/*     */     final void reducePermits(int paramInt)
/*     */     {
/*     */       while (true) {
/* 205 */         int i = getState();
/* 206 */         int j = i - paramInt;
/* 207 */         if (j > i)
/* 208 */           throw new Error("Permit count underflow");
/* 209 */         if (compareAndSetState(i, j))
/* 210 */           return;
/*     */       }
/*     */     }
/*     */ 
/*     */     final int drainPermits() {
/*     */       while (true) {
/* 216 */         int i = getState();
/* 217 */         if ((i == 0) || (compareAndSetState(i, 0)))
/* 218 */           return i;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Semaphore
 * JD-Core Version:    0.6.2
 */