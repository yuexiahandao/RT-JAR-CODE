/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class TimerThread extends Thread
/*     */ {
/* 351 */   public static boolean debug = false;
/*     */   static TimerThread timerThread;
/* 363 */   static boolean notified = false;
/*     */ 
/* 416 */   static Timer timerQueue = null;
/*     */ 
/*     */   protected TimerThread()
/*     */   {
/* 366 */     super("TimerThread");
/* 367 */     timerThread = this;
/* 368 */     start();
/*     */   }
/*     */ 
/*     */   public synchronized void run()
/*     */   {
/*     */     while (true)
/* 375 */       if (timerQueue == null) {
/*     */         try {
/* 377 */           wait();
/*     */         } catch (InterruptedException localInterruptedException1) {
/*     */         }
/*     */       }
/*     */       else {
/* 382 */         notified = false;
/* 383 */         long l1 = timerQueue.sleepUntil - System.currentTimeMillis();
/* 384 */         if (l1 > 0L) {
/*     */           try {
/* 386 */             wait(l1);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException2)
/*     */           {
/*     */           }
/*     */         }
/* 392 */         if (!notified) {
/* 393 */           Timer localTimer = timerQueue;
/* 394 */           timerQueue = timerQueue.next;
/* 395 */           TimerTickThread localTimerTickThread = TimerTickThread.call(localTimer, localTimer.sleepUntil);
/*     */ 
/* 397 */           if (debug) {
/* 398 */             long l2 = System.currentTimeMillis() - localTimer.sleepUntil;
/* 399 */             System.out.println("tick(" + localTimerTickThread.getName() + "," + localTimer.interval + "," + l2 + ")");
/*     */ 
/* 401 */             if (l2 > 250L)
/* 402 */               System.out.println("*** BIG DELAY ***");
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected static void enqueue(Timer paramTimer)
/*     */   {
/* 427 */     Object localObject = null;
/* 428 */     Timer localTimer = timerQueue;
/*     */ 
/* 430 */     if ((localTimer == null) || (paramTimer.sleepUntil <= localTimer.sleepUntil))
/*     */     {
/* 432 */       paramTimer.next = timerQueue;
/* 433 */       timerQueue = paramTimer;
/* 434 */       notified = true;
/* 435 */       timerThread.notify();
/*     */     } else {
/*     */       do {
/* 438 */         localObject = localTimer;
/* 439 */         localTimer = localTimer.next;
/* 440 */       }while ((localTimer != null) && (paramTimer.sleepUntil > localTimer.sleepUntil));
/*     */ 
/* 442 */       paramTimer.next = localTimer;
/* 443 */       localObject.next = paramTimer;
/*     */     }
/* 445 */     if (debug) {
/* 446 */       long l1 = System.currentTimeMillis();
/*     */ 
/* 448 */       System.out.print(Thread.currentThread().getName() + ": enqueue " + paramTimer.interval + ": ");
/*     */ 
/* 450 */       localTimer = timerQueue;
/* 451 */       while (localTimer != null) {
/* 452 */         long l2 = localTimer.sleepUntil - l1;
/* 453 */         System.out.print(localTimer.interval + "(" + l2 + ") ");
/* 454 */         localTimer = localTimer.next;
/*     */       }
/* 456 */       System.out.println();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static boolean dequeue(Timer paramTimer)
/*     */   {
/* 466 */     Object localObject = null;
/* 467 */     Timer localTimer = timerQueue;
/*     */ 
/* 469 */     while ((localTimer != null) && (localTimer != paramTimer)) {
/* 470 */       localObject = localTimer;
/* 471 */       localTimer = localTimer.next;
/*     */     }
/* 473 */     if (localTimer == null) {
/* 474 */       if (debug) {
/* 475 */         System.out.println(Thread.currentThread().getName() + ": dequeue " + paramTimer.interval + ": no-op");
/*     */       }
/*     */ 
/* 478 */       return false;
/* 479 */     }if (localObject == null) {
/* 480 */       timerQueue = paramTimer.next;
/* 481 */       notified = true;
/* 482 */       timerThread.notify();
/*     */     } else {
/* 484 */       localObject.next = paramTimer.next;
/*     */     }
/* 486 */     paramTimer.next = null;
/* 487 */     if (debug) {
/* 488 */       long l1 = System.currentTimeMillis();
/*     */ 
/* 490 */       System.out.print(Thread.currentThread().getName() + ": dequeue " + paramTimer.interval + ": ");
/*     */ 
/* 492 */       localTimer = timerQueue;
/* 493 */       while (localTimer != null) {
/* 494 */         long l2 = localTimer.sleepUntil - l1;
/* 495 */         System.out.print(localTimer.interval + "(" + l2 + ") ");
/* 496 */         localTimer = localTimer.next;
/*     */       }
/* 498 */       System.out.println();
/*     */     }
/* 500 */     return true;
/*     */   }
/*     */ 
/*     */   protected static void requeue(Timer paramTimer)
/*     */   {
/* 511 */     if (!paramTimer.stopped) {
/* 512 */       long l = System.currentTimeMillis();
/* 513 */       if (paramTimer.regular)
/* 514 */         paramTimer.sleepUntil += paramTimer.interval;
/*     */       else {
/* 516 */         paramTimer.sleepUntil = (l + paramTimer.interval);
/*     */       }
/* 518 */       enqueue(paramTimer);
/* 519 */     } else if (debug) {
/* 520 */       System.out.println(Thread.currentThread().getName() + ": requeue " + paramTimer.interval + ": no-op");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.TimerThread
 * JD-Core Version:    0.6.2
 */