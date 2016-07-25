/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public final class Fiber
/*     */   implements Runnable
/*     */ {
/* 102 */   private Tube[] conts = new Tube[16];
/*     */   private int contsSize;
/*     */   private Tube next;
/*     */   private Packet packet;
/*     */   private Throwable throwable;
/*     */   public final Engine owner;
/* 142 */   private volatile int suspendedCount = 0;
/*     */   private volatile boolean completed;
/*     */   private boolean synchronous;
/*     */   private boolean interrupted;
/*     */   private final int id;
/*     */   private List<FiberContextSwitchInterceptor> interceptors;
/*     */   private InterceptorHandler interceptorHandler;
/*     */   private boolean needsToReenter;
/*     */ 
/*     */   @Nullable
/*     */   private ClassLoader contextClassLoader;
/*     */ 
/*     */   @Nullable
/*     */   private CompletionCallback completionCallback;
/*     */   private boolean started;
/* 793 */   private static final ThreadLocal<Fiber> CURRENT_FIBER = new ThreadLocal();
/*     */ 
/* 798 */   private static final AtomicInteger iotaGen = new AtomicInteger();
/*     */ 
/* 804 */   private static final Logger LOGGER = Logger.getLogger(Fiber.class.getName());
/*     */ 
/* 807 */   private static final ReentrantLock serializedExecutionLock = new ReentrantLock();
/*     */ 
/* 813 */   public static volatile boolean serializeExecution = Boolean.getBoolean(Fiber.class.getName() + ".serialize");
/*     */ 
/*     */   Fiber(Engine engine)
/*     */   {
/* 211 */     this.owner = engine;
/* 212 */     if (isTraceEnabled()) {
/* 213 */       this.id = iotaGen.incrementAndGet();
/* 214 */       LOGGER.fine(getName() + " created");
/*     */     } else {
/* 216 */       this.id = -1;
/*     */     }
/*     */ 
/* 221 */     this.contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */   public void start(@NotNull Tube tubeline, @NotNull Packet request, @Nullable CompletionCallback completionCallback)
/*     */   {
/* 241 */     this.next = tubeline;
/* 242 */     this.packet = request;
/* 243 */     this.completionCallback = completionCallback;
/* 244 */     this.started = true;
/* 245 */     this.owner.addRunnable(this);
/*     */   }
/*     */ 
/*     */   public void runAsync(@NotNull Tube tubeline, @NotNull Packet request, @Nullable CompletionCallback completionCallback) {
/* 249 */     this.next = tubeline;
/* 250 */     this.packet = request;
/* 251 */     this.completionCallback = completionCallback;
/* 252 */     this.started = true;
/* 253 */     run();
/*     */   }
/*     */ 
/*     */   public synchronized void resume(@NotNull Packet resumePacket)
/*     */   {
/* 280 */     if (isTraceEnabled())
/* 281 */       LOGGER.fine(getName() + " resumed");
/* 282 */     this.packet = resumePacket;
/* 283 */     if (--this.suspendedCount == 0)
/* 284 */       if (this.synchronous)
/* 285 */         notifyAll();
/*     */       else
/* 287 */         this.owner.addRunnable(this);
/*     */   }
/*     */ 
/*     */   public synchronized void resume(@NotNull Throwable throwable)
/*     */   {
/* 310 */     if (isTraceEnabled()) {
/* 311 */       LOGGER.fine(getName() + " resumed");
/*     */     }
/*     */ 
/* 314 */     this.throwable = throwable;
/* 315 */     if (--this.suspendedCount == 0)
/* 316 */       if (this.synchronous)
/* 317 */         notifyAll();
/*     */       else
/* 319 */         this.owner.addRunnable(this);
/*     */   }
/*     */ 
/*     */   private synchronized void suspend()
/*     */   {
/* 331 */     if (isTraceEnabled())
/* 332 */       LOGGER.fine(getName() + " suspended");
/* 333 */     this.suspendedCount += 1;
/*     */   }
/*     */ 
/*     */   public void addInterceptor(@NotNull FiberContextSwitchInterceptor interceptor)
/*     */   {
/* 356 */     if (this.interceptors == null) {
/* 357 */       this.interceptors = new ArrayList();
/* 358 */       this.interceptorHandler = new InterceptorHandler(null);
/*     */     }
/* 360 */     this.interceptors.add(interceptor);
/* 361 */     this.needsToReenter = true;
/*     */   }
/*     */ 
/*     */   public boolean removeInterceptor(@NotNull FiberContextSwitchInterceptor interceptor)
/*     */   {
/* 389 */     if ((this.interceptors != null) && (this.interceptors.remove(interceptor))) {
/* 390 */       this.needsToReenter = true;
/* 391 */       return true;
/*     */     }
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public ClassLoader getContextClassLoader()
/*     */   {
/* 400 */     return this.contextClassLoader;
/*     */   }
/*     */ 
/*     */   public ClassLoader setContextClassLoader(@Nullable ClassLoader contextClassLoader)
/*     */   {
/* 407 */     ClassLoader r = this.contextClassLoader;
/* 408 */     this.contextClassLoader = contextClassLoader;
/* 409 */     return r;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void run()
/*     */   {
/* 418 */     assert (!this.synchronous);
/* 419 */     this.next = doRun(this.next);
/* 420 */     completionCheck();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public synchronized Packet runSync(@NotNull Tube tubeline, @NotNull Packet request)
/*     */   {
/* 455 */     Tube[] oldCont = this.conts;
/* 456 */     int oldContSize = this.contsSize;
/* 457 */     boolean oldSynchronous = this.synchronous;
/*     */ 
/* 459 */     if (oldContSize > 0) {
/* 460 */       this.conts = new Tube[16];
/* 461 */       this.contsSize = 0;
/*     */     }
/*     */     try
/*     */     {
/* 465 */       this.synchronous = true;
/* 466 */       this.packet = request;
/* 467 */       doRun(tubeline);
/* 468 */       if (this.throwable != null) {
/* 469 */         if ((this.throwable instanceof RuntimeException)) {
/* 470 */           throw ((RuntimeException)this.throwable);
/*     */         }
/* 472 */         if ((this.throwable instanceof Error)) {
/* 473 */           throw ((Error)this.throwable);
/*     */         }
/*     */ 
/* 476 */         throw new AssertionError(this.throwable);
/*     */       }
/* 478 */       return this.packet;
/*     */     } finally {
/* 480 */       this.conts = oldCont;
/* 481 */       this.contsSize = oldContSize;
/* 482 */       this.synchronous = oldSynchronous;
/* 483 */       if (this.interrupted) {
/* 484 */         Thread.currentThread().interrupt();
/* 485 */         this.interrupted = false;
/*     */       }
/* 487 */       if (!this.started)
/* 488 */         completionCheck();
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void completionCheck() {
/* 493 */     if (this.contsSize == 0) {
/* 494 */       if (isTraceEnabled())
/* 495 */         LOGGER.fine(getName() + " completed");
/* 496 */       this.completed = true;
/* 497 */       notifyAll();
/* 498 */       if (this.completionCallback != null)
/* 499 */         if (this.throwable != null)
/* 500 */           this.completionCallback.onCompletion(this.throwable);
/*     */         else
/* 502 */           this.completionCallback.onCompletion(this.packet);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Tube doRun(Tube next)
/*     */   {
/* 557 */     Thread currentThread = Thread.currentThread();
/*     */ 
/* 559 */     if (isTraceEnabled()) {
/* 560 */       LOGGER.fine(getName() + " running by " + currentThread.getName());
/*     */     }
/* 562 */     if (serializeExecution) {
/* 563 */       serializedExecutionLock.lock();
/*     */       try {
/* 565 */         return _doRun(next);
/*     */       } finally {
/* 567 */         serializedExecutionLock.unlock();
/*     */       }
/*     */     }
/* 570 */     return _doRun(next);
/*     */   }
/*     */ 
/*     */   private Tube _doRun(Tube next)
/*     */   {
/* 575 */     Thread currentThread = Thread.currentThread();
/*     */ 
/* 577 */     ClassLoader old = currentThread.getContextClassLoader();
/* 578 */     currentThread.setContextClassLoader(this.contextClassLoader);
/*     */     try {
/*     */       do {
/* 581 */         this.needsToReenter = false;
/*     */ 
/* 584 */         if (this.interceptorHandler == null)
/* 585 */           next = __doRun(next);
/*     */         else
/* 587 */           next = this.interceptorHandler.invoke(next); 
/*     */       }
/* 588 */       while (this.needsToReenter);
/*     */ 
/* 590 */       return next;
/*     */     } finally {
/* 592 */       currentThread.setContextClassLoader(old);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Tube __doRun(Tube next)
/*     */   {
/* 602 */     Fiber old = (Fiber)CURRENT_FIBER.get();
/* 603 */     CURRENT_FIBER.set(this);
/*     */ 
/* 606 */     boolean traceEnabled = LOGGER.isLoggable(Level.FINER);
/*     */     try
/*     */     {
/* 609 */       while ((!isBlocking()) && (!this.needsToReenter)) {
/*     */         try
/*     */         {
/*     */           Tube localTube1;
/*     */           NextAction na;
/*     */           Tube last;
/*     */           NextAction na;
/* 613 */           if (this.throwable != null) {
/* 614 */             if (this.contsSize == 0)
/*     */             {
/* 616 */               return null;
/*     */             }
/* 618 */             Tube last = popCont();
/* 619 */             if (traceEnabled)
/* 620 */               LOGGER.finer(getName() + ' ' + last + ".processException(" + this.throwable + ')');
/* 621 */             na = last.processException(this.throwable);
/*     */           }
/*     */           else
/*     */           {
/*     */             Tube last;
/* 623 */             if (next != null) {
/* 624 */               if (traceEnabled)
/* 625 */                 LOGGER.finer(getName() + ' ' + next + ".processRequest(" + this.packet + ')');
/* 626 */               NextAction na = next.processRequest(this.packet);
/* 627 */               last = next;
/*     */             } else {
/* 629 */               if (this.contsSize == 0)
/*     */               {
/* 631 */                 return null;
/*     */               }
/* 633 */               last = popCont();
/* 634 */               if (traceEnabled)
/* 635 */                 LOGGER.finer(getName() + ' ' + last + ".processResponse(" + this.packet + ')');
/* 636 */               na = last.processResponse(this.packet);
/*     */             }
/*     */           }
/*     */ 
/* 640 */           if (traceEnabled) {
/* 641 */             LOGGER.finer(getName() + ' ' + last + " returned with " + na);
/*     */           }
/*     */ 
/* 645 */           if (na.kind != 4) {
/* 646 */             this.packet = na.packet;
/* 647 */             this.throwable = na.throwable;
/*     */           }
/*     */ 
/* 650 */           switch (na.kind) {
/*     */           case 0:
/* 652 */             pushCont(last);
/*     */           case 1:
/* 655 */             next = na.next;
/* 656 */             break;
/*     */           case 2:
/*     */           case 3:
/* 659 */             next = null;
/* 660 */             break;
/*     */           case 4:
/* 662 */             pushCont(last);
/* 663 */             next = na.next;
/* 664 */             suspend();
/* 665 */             break;
/*     */           default:
/* 667 */             throw new AssertionError();
/*     */           }
/*     */         } catch (RuntimeException t) {
/* 670 */           if (traceEnabled)
/* 671 */             LOGGER.log(Level.FINER, getName() + " Caught " + t + ". Start stack unwinding", t);
/* 672 */           this.throwable = t;
/*     */         } catch (Error t) {
/* 674 */           if (traceEnabled)
/* 675 */             LOGGER.log(Level.FINER, getName() + " Caught " + t + ". Start stack unwinding", t);
/* 676 */           this.throwable = t;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 681 */       return next;
/*     */     } finally {
/* 683 */       CURRENT_FIBER.set(old);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pushCont(Tube tube) {
/* 688 */     this.conts[(this.contsSize++)] = tube;
/*     */ 
/* 691 */     int len = this.conts.length;
/* 692 */     if (this.contsSize == len) {
/* 693 */       Tube[] newBuf = new Tube[len * 2];
/* 694 */       System.arraycopy(this.conts, 0, newBuf, 0, len);
/* 695 */       this.conts = newBuf;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Tube popCont() {
/* 700 */     return this.conts[(--this.contsSize)];
/*     */   }
/*     */ 
/*     */   private boolean isBlocking()
/*     */   {
/* 708 */     if (this.synchronous) {
/* 709 */       while (this.suspendedCount == 1)
/*     */         try {
/* 711 */           if (isTraceEnabled()) {
/* 712 */             LOGGER.fine(getName() + " is blocking thread " + Thread.currentThread().getName());
/*     */           }
/* 714 */           wait();
/*     */         }
/*     */         catch (InterruptedException e)
/*     */         {
/* 719 */           this.interrupted = true;
/*     */         }
/* 721 */       return false;
/*     */     }
/*     */ 
/* 724 */     return this.suspendedCount == 1;
/*     */   }
/*     */ 
/*     */   private String getName() {
/* 728 */     return "engine-" + this.owner.id + "fiber-" + this.id;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 733 */     return getName();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public Packet getPacket()
/*     */   {
/* 743 */     return this.packet;
/*     */   }
/*     */ 
/*     */   public boolean isAlive()
/*     */   {
/* 750 */     return !this.completed;
/*     */   }
/*     */ 
/*     */   public static boolean isSynchronous()
/*     */   {
/* 776 */     return current().synchronous;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Fiber current()
/*     */   {
/* 787 */     Fiber fiber = (Fiber)CURRENT_FIBER.get();
/* 788 */     if (fiber == null)
/* 789 */       throw new IllegalStateException("Can be only used from fibers");
/* 790 */     return fiber;
/*     */   }
/*     */ 
/*     */   private static boolean isTraceEnabled()
/*     */   {
/* 801 */     return LOGGER.isLoggable(Level.FINE);
/*     */   }
/*     */ 
/*     */   public static abstract interface CompletionCallback
/*     */   {
/*     */     public abstract void onCompletion(@NotNull Packet paramPacket);
/*     */ 
/*     */     public abstract void onCompletion(@NotNull Throwable paramThrowable);
/*     */   }
/*     */ 
/*     */   private class InterceptorHandler
/*     */     implements FiberContextSwitchInterceptor.Work<Tube, Tube>
/*     */   {
/*     */     private int idx;
/*     */ 
/*     */     private InterceptorHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     Tube invoke(Tube next)
/*     */     {
/* 529 */       this.idx = 0;
/* 530 */       return execute(next);
/*     */     }
/*     */ 
/*     */     public Tube execute(Tube next) {
/* 534 */       if (this.idx == Fiber.this.interceptors.size()) {
/* 535 */         return Fiber.this.__doRun(next);
/*     */       }
/* 537 */       FiberContextSwitchInterceptor interceptor = (FiberContextSwitchInterceptor)Fiber.this.interceptors.get(this.idx++);
/* 538 */       return (Tube)interceptor.execute(Fiber.this, next, this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.Fiber
 * JD-Core Version:    0.6.2
 */