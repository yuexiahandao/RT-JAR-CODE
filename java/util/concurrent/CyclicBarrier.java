/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ public class CyclicBarrier
/*     */ {
/* 149 */   private final ReentrantLock lock = new ReentrantLock();
/*     */ 
/* 151 */   private final Condition trip = this.lock.newCondition();
/*     */   private final int parties;
/*     */   private final Runnable barrierCommand;
/* 157 */   private Generation generation = new Generation(null);
/*     */   private int count;
/*     */ 
/*     */   private void nextGeneration()
/*     */   {
/* 172 */     this.trip.signalAll();
/*     */ 
/* 174 */     this.count = this.parties;
/* 175 */     this.generation = new Generation(null);
/*     */   }
/*     */ 
/*     */   private void breakBarrier()
/*     */   {
/* 183 */     this.generation.broken = true;
/* 184 */     this.count = this.parties;
/* 185 */     this.trip.signalAll();
/*     */   }
/*     */ 
/*     */   private int dowait(boolean paramBoolean, long paramLong)
/*     */     throws InterruptedException, BrokenBarrierException, TimeoutException
/*     */   {
/* 194 */     ReentrantLock localReentrantLock = this.lock;
/* 195 */     localReentrantLock.lock();
/*     */     try {
/* 197 */       Generation localGeneration = this.generation;
/*     */ 
/* 199 */       if (localGeneration.broken) {
/* 200 */         throw new BrokenBarrierException();
/*     */       }
/* 202 */       if (Thread.interrupted()) {
/* 203 */         breakBarrier();
/* 204 */         throw new InterruptedException();
/*     */       }
/*     */ 
/* 207 */       InterruptedException localInterruptedException1 = --this.count;
/* 208 */       if (localInterruptedException1 == 0) {
/* 209 */         int i = 0;
/*     */         try {
/* 211 */           Runnable localRunnable = this.barrierCommand;
/* 212 */           if (localRunnable != null)
/* 213 */             localRunnable.run();
/* 214 */           i = 1;
/* 215 */           nextGeneration();
/* 216 */           int j = 0;
/*     */ 
/* 218 */           if (i == 0) {
/* 219 */             breakBarrier();
/*     */           }
/*     */ 
/* 254 */           return j;
/*     */         }
/*     */         finally
/*     */         {
/* 218 */           if (i == 0)
/* 219 */             breakBarrier();
/*     */         }
/*     */       }
/*     */       do
/*     */       {
/*     */         try
/*     */         {
/* 226 */           if (!paramBoolean)
/* 227 */             this.trip.await();
/* 228 */           else if (paramLong > 0L)
/* 229 */             paramLong = this.trip.awaitNanos(paramLong);
/*     */         } catch (InterruptedException localInterruptedException2) {
/* 231 */           if ((localGeneration == this.generation) && (!localGeneration.broken)) {
/* 232 */             breakBarrier();
/* 233 */             throw localInterruptedException2;
/*     */           }
/*     */ 
/* 238 */           Thread.currentThread().interrupt();
/*     */         }
/*     */ 
/* 242 */         if (localGeneration.broken) {
/* 243 */           throw new BrokenBarrierException();
/*     */         }
/* 245 */         if (localGeneration != this.generation)
/* 246 */           return localInterruptedException1;
/*     */       }
/* 248 */       while ((!paramBoolean) || (paramLong > 0L));
/* 249 */       breakBarrier();
/* 250 */       throw new TimeoutException();
/*     */     }
/*     */     finally
/*     */     {
/* 254 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public CyclicBarrier(int paramInt, Runnable paramRunnable)
/*     */   {
/* 271 */     if (paramInt <= 0) throw new IllegalArgumentException();
/* 272 */     this.parties = paramInt;
/* 273 */     this.count = paramInt;
/* 274 */     this.barrierCommand = paramRunnable;
/*     */   }
/*     */ 
/*     */   public CyclicBarrier(int paramInt)
/*     */   {
/* 287 */     this(paramInt, null);
/*     */   }
/*     */ 
/*     */   public int getParties()
/*     */   {
/* 296 */     return this.parties;
/*     */   }
/*     */ 
/*     */   public int await()
/*     */     throws InterruptedException, BrokenBarrierException
/*     */   {
/*     */     try
/*     */     {
/* 355 */       return dowait(false, 0L);
/*     */     } catch (TimeoutException localTimeoutException) {
/* 357 */       throw new Error(localTimeoutException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int await(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException, BrokenBarrierException, TimeoutException
/*     */   {
/* 427 */     return dowait(true, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public boolean isBroken()
/*     */   {
/* 439 */     ReentrantLock localReentrantLock = this.lock;
/* 440 */     localReentrantLock.lock();
/*     */     try {
/* 442 */       return this.generation.broken;
/*     */     } finally {
/* 444 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 458 */     ReentrantLock localReentrantLock = this.lock;
/* 459 */     localReentrantLock.lock();
/*     */     try {
/* 461 */       breakBarrier();
/* 462 */       nextGeneration();
/*     */     } finally {
/* 464 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumberWaiting()
/*     */   {
/* 475 */     ReentrantLock localReentrantLock = this.lock;
/* 476 */     localReentrantLock.lock();
/*     */     try {
/* 478 */       return this.parties - this.count;
/*     */     } finally {
/* 480 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Generation
/*     */   {
/* 145 */     boolean broken = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.CyclicBarrier
 * JD-Core Version:    0.6.2
 */