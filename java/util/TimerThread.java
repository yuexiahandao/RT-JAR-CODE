/*     */ package java.util;
/*     */ 
/*     */ class TimerThread extends Thread
/*     */ {
/* 489 */   boolean newTasksMayBeScheduled = true;
/*     */   private TaskQueue queue;
/*     */ 
/*     */   TimerThread(TaskQueue paramTaskQueue)
/*     */   {
/* 500 */     this.queue = paramTaskQueue;
/*     */   }
/*     */ 
/*     */   public void run() {
/*     */     try {
/* 505 */       mainLoop();
/*     */     }
/*     */     finally {
/* 508 */       synchronized (this.queue) {
/* 509 */         this.newTasksMayBeScheduled = false;
/* 510 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mainLoop()
/*     */   {
/*     */     while (true)
/*     */       try
/*     */       {
/*     */         TimerTask localTimerTask;
/*     */         int i;
/* 523 */         synchronized (this.queue)
/*     */         {
/* 525 */           if ((this.queue.isEmpty()) && (this.newTasksMayBeScheduled)) {
/* 526 */             this.queue.wait(); continue;
/* 527 */           }if (this.queue.isEmpty())
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 532 */           localTimerTask = this.queue.getMin();
/*     */           long l1;
/*     */           long l2;
/* 533 */           synchronized (localTimerTask.lock) {
/* 534 */             if (localTimerTask.state == 3) {
/* 535 */               this.queue.removeMin();
/* 536 */               continue;
/*     */             }
/* 538 */             l1 = System.currentTimeMillis();
/* 539 */             l2 = localTimerTask.nextExecutionTime;
/* 540 */             if ((i = l2 <= l1 ? 1 : 0) != 0) {
/* 541 */               if (localTimerTask.period == 0L) {
/* 542 */                 this.queue.removeMin();
/* 543 */                 localTimerTask.state = 2;
/*     */               } else {
/* 545 */                 this.queue.rescheduleMin(localTimerTask.period < 0L ? l1 - localTimerTask.period : l2 + localTimerTask.period);
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 551 */           if (i == 0)
/* 552 */             this.queue.wait(l2 - l1);
/*     */         }
/* 554 */         if (i != 0)
/* 555 */           localTimerTask.run();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.TimerThread
 * JD-Core Version:    0.6.2
 */