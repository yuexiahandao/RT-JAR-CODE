/*     */ package sun.misc;
/*     */ 
/*     */ class TimerTickThread extends Thread
/*     */ {
/*     */   static final int MAX_POOL_SIZE = 3;
/* 549 */   static int curPoolSize = 0;
/*     */ 
/* 554 */   static TimerTickThread pool = null;
/*     */ 
/* 559 */   TimerTickThread next = null;
/*     */   Timer timer;
/*     */   long lastSleepUntil;
/*     */ 
/*     */   protected static synchronized TimerTickThread call(Timer paramTimer, long paramLong)
/*     */   {
/* 583 */     TimerTickThread localTimerTickThread = pool;
/*     */ 
/* 585 */     if (localTimerTickThread == null)
/*     */     {
/* 587 */       localTimerTickThread = new TimerTickThread();
/* 588 */       localTimerTickThread.timer = paramTimer;
/* 589 */       localTimerTickThread.lastSleepUntil = paramLong;
/* 590 */       localTimerTickThread.start();
/*     */     } else {
/* 592 */       pool = pool.next;
/* 593 */       localTimerTickThread.timer = paramTimer;
/* 594 */       localTimerTickThread.lastSleepUntil = paramLong;
/* 595 */       synchronized (localTimerTickThread) {
/* 596 */         localTimerTickThread.notify();
/*     */       }
/*     */     }
/* 599 */     return localTimerTickThread;
/*     */   }
/*     */ 
/*     */   private boolean returnToPool()
/*     */   {
/* 611 */     synchronized (getClass()) {
/* 612 */       if (curPoolSize >= 3) {
/* 613 */         return false;
/*     */       }
/* 615 */       this.next = pool;
/* 616 */       pool = this;
/* 617 */       curPoolSize += 1;
/* 618 */       this.timer = null;
/*     */     }
/* 620 */     while (this.timer == null)
/* 621 */       synchronized (this) {
/*     */         try {
/* 623 */           wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */       }
/* 629 */     synchronized (getClass()) {
/* 630 */       curPoolSize -= 1;
/*     */     }
/* 632 */     return true;
/*     */   }
/*     */ 
/*     */   public void run() {
/*     */     do {
/* 637 */       this.timer.owner.tick(this.timer);
/* 638 */       synchronized (TimerThread.timerThread) {
/* 639 */         synchronized (this.timer) {
/* 640 */           if (this.lastSleepUntil == this.timer.sleepUntil)
/* 641 */             TimerThread.requeue(this.timer);
/*     */         }
/*     */       }
/*     */     }
/* 645 */     while (returnToPool());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.TimerTickThread
 * JD-Core Version:    0.6.2
 */