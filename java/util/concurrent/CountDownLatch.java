/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*     */ 
/*     */ public class CountDownLatch
/*     */ {
/*     */   private final Sync sync;
/*     */ 
/*     */   public CountDownLatch(int paramInt)
/*     */   {
/* 204 */     if (paramInt < 0) throw new IllegalArgumentException("count < 0");
/* 205 */     this.sync = new Sync(paramInt);
/*     */   }
/*     */ 
/*     */   public void await()
/*     */     throws InterruptedException
/*     */   {
/* 236 */     this.sync.acquireSharedInterruptibly(1);
/*     */   }
/*     */ 
/*     */   public boolean await(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 282 */     return this.sync.tryAcquireSharedNanos(1, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public void countDown()
/*     */   {
/* 296 */     this.sync.releaseShared(1);
/*     */   }
/*     */ 
/*     */   public long getCount()
/*     */   {
/* 307 */     return this.sync.getCount();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 318 */     return super.toString() + "[Count = " + this.sync.getCount() + "]";
/*     */   }
/*     */ 
/*     */   private static final class Sync extends AbstractQueuedSynchronizer
/*     */   {
/*     */     private static final long serialVersionUID = 4982264981922014374L;
/*     */ 
/*     */     Sync(int paramInt)
/*     */     {
/* 170 */       setState(paramInt);
/*     */     }
/*     */ 
/*     */     int getCount() {
/* 174 */       return getState();
/*     */     }
/*     */ 
/*     */     protected int tryAcquireShared(int paramInt) {
/* 178 */       return getState() == 0 ? 1 : -1;
/*     */     }
/*     */ 
/*     */     protected boolean tryReleaseShared(int paramInt)
/*     */     {
/*     */       while (true) {
/* 184 */         int i = getState();
/* 185 */         if (i == 0)
/* 186 */           return false;
/* 187 */         int j = i - 1;
/* 188 */         if (compareAndSetState(i, j))
/* 189 */           return j == 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.CountDownLatch
 * JD-Core Version:    0.6.2
 */