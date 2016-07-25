/*     */ package java.util;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ public class Timer
/*     */ {
/*  96 */   private final TaskQueue queue = new TaskQueue();
/*     */ 
/* 101 */   private final TimerThread thread = new TimerThread(this.queue);
/*     */ 
/* 110 */   private final Object threadReaper = new Object() {
/*     */     protected void finalize() throws Throwable {
/* 112 */       synchronized (Timer.this.queue) {
/* 113 */         Timer.this.thread.newTasksMayBeScheduled = false;
/* 114 */         Timer.this.queue.notify();
/*     */       }
/*     */     }
/* 110 */   };
/*     */ 
/* 122 */   private static final AtomicInteger nextSerialNumber = new AtomicInteger(0);
/*     */ 
/* 124 */   private static int serialNumber() { return nextSerialNumber.getAndIncrement(); }
/*     */ 
/*     */ 
/*     */   public Timer()
/*     */   {
/* 132 */     this("Timer-" + serialNumber());
/*     */   }
/*     */ 
/*     */   public Timer(boolean paramBoolean)
/*     */   {
/* 146 */     this("Timer-" + serialNumber(), paramBoolean);
/*     */   }
/*     */ 
/*     */   public Timer(String paramString)
/*     */   {
/* 159 */     this.thread.setName(paramString);
/* 160 */     this.thread.start();
/*     */   }
/*     */ 
/*     */   public Timer(String paramString, boolean paramBoolean)
/*     */   {
/* 174 */     this.thread.setName(paramString);
/* 175 */     this.thread.setDaemon(paramBoolean);
/* 176 */     this.thread.start();
/*     */   }
/*     */ 
/*     */   public void schedule(TimerTask paramTimerTask, long paramLong)
/*     */   {
/* 191 */     if (paramLong < 0L)
/* 192 */       throw new IllegalArgumentException("Negative delay.");
/* 193 */     sched(paramTimerTask, System.currentTimeMillis() + paramLong, 0L);
/*     */   }
/*     */ 
/*     */   public void schedule(TimerTask paramTimerTask, Date paramDate)
/*     */   {
/* 208 */     sched(paramTimerTask, paramDate.getTime(), 0L);
/*     */   }
/*     */ 
/*     */   public void schedule(TimerTask paramTimerTask, long paramLong1, long paramLong2)
/*     */   {
/* 244 */     if (paramLong1 < 0L)
/* 245 */       throw new IllegalArgumentException("Negative delay.");
/* 246 */     if (paramLong2 <= 0L)
/* 247 */       throw new IllegalArgumentException("Non-positive period.");
/* 248 */     sched(paramTimerTask, System.currentTimeMillis() + paramLong1, -paramLong2);
/*     */   }
/*     */ 
/*     */   public void schedule(TimerTask paramTimerTask, Date paramDate, long paramLong)
/*     */   {
/* 285 */     if (paramLong <= 0L)
/* 286 */       throw new IllegalArgumentException("Non-positive period.");
/* 287 */     sched(paramTimerTask, paramDate.getTime(), -paramLong);
/*     */   }
/*     */ 
/*     */   public void scheduleAtFixedRate(TimerTask paramTimerTask, long paramLong1, long paramLong2)
/*     */   {
/* 324 */     if (paramLong1 < 0L)
/* 325 */       throw new IllegalArgumentException("Negative delay.");
/* 326 */     if (paramLong2 <= 0L)
/* 327 */       throw new IllegalArgumentException("Non-positive period.");
/* 328 */     sched(paramTimerTask, System.currentTimeMillis() + paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   public void scheduleAtFixedRate(TimerTask paramTimerTask, Date paramDate, long paramLong)
/*     */   {
/* 368 */     if (paramLong <= 0L)
/* 369 */       throw new IllegalArgumentException("Non-positive period.");
/* 370 */     sched(paramTimerTask, paramDate.getTime(), paramLong);
/*     */   }
/*     */ 
/*     */   private void sched(TimerTask paramTimerTask, long paramLong1, long paramLong2)
/*     */   {
/* 387 */     if (paramLong1 < 0L) {
/* 388 */       throw new IllegalArgumentException("Illegal execution time.");
/*     */     }
/*     */ 
/* 392 */     if (Math.abs(paramLong2) > 4611686018427387903L) {
/* 393 */       paramLong2 >>= 1;
/*     */     }
/* 395 */     synchronized (this.queue) {
/* 396 */       if (!this.thread.newTasksMayBeScheduled) {
/* 397 */         throw new IllegalStateException("Timer already cancelled.");
/*     */       }
/* 399 */       synchronized (paramTimerTask.lock) {
/* 400 */         if (paramTimerTask.state != 0) {
/* 401 */           throw new IllegalStateException("Task already scheduled or cancelled");
/*     */         }
/* 403 */         paramTimerTask.nextExecutionTime = paramLong1;
/* 404 */         paramTimerTask.period = paramLong2;
/* 405 */         paramTimerTask.state = 1;
/*     */       }
/*     */ 
/* 408 */       this.queue.add(paramTimerTask);
/* 409 */       if (this.queue.getMin() == paramTimerTask)
/* 410 */         this.queue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cancel()
/*     */   {
/* 429 */     synchronized (this.queue) {
/* 430 */       this.thread.newTasksMayBeScheduled = false;
/* 431 */       this.queue.clear();
/* 432 */       this.queue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int purge()
/*     */   {
/* 457 */     int i = 0;
/*     */ 
/* 459 */     synchronized (this.queue) {
/* 460 */       for (int j = this.queue.size(); j > 0; j--) {
/* 461 */         if (this.queue.get(j).state == 3) {
/* 462 */           this.queue.quickRemove(j);
/* 463 */           i++;
/*     */         }
/*     */       }
/*     */ 
/* 467 */       if (i != 0) {
/* 468 */         this.queue.heapify();
/*     */       }
/*     */     }
/* 471 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Timer
 * JD-Core Version:    0.6.2
 */