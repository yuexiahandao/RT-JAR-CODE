/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class FutureTask<V>
/*     */   implements RunnableFuture<V>
/*     */ {
/*     */   private volatile int state;
/*     */   private static final int NEW = 0;
/*     */   private static final int COMPLETING = 1;
/*     */   private static final int NORMAL = 2;
/*     */   private static final int EXCEPTIONAL = 3;
/*     */   private static final int CANCELLED = 4;
/*     */   private static final int INTERRUPTING = 5;
/*     */   private static final int INTERRUPTED = 6;
/*     */   private Callable<V> callable;
/*     */   private Object outcome;
/*     */   private volatile Thread runner;
/*     */   private volatile WaitNode waiters;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long stateOffset;
/*     */   private static final long runnerOffset;
/*     */   private static final long waitersOffset;
/*     */ 
/*     */   private V report(int paramInt)
/*     */     throws ExecutionException
/*     */   {
/* 117 */     Object localObject = this.outcome;
/* 118 */     if (paramInt == 2)
/* 119 */       return localObject;
/* 120 */     if (paramInt >= 4)
/* 121 */       throw new CancellationException();
/* 122 */     throw new ExecutionException((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   public FutureTask(Callable<V> paramCallable)
/*     */   {
/* 133 */     if (paramCallable == null)
/* 134 */       throw new NullPointerException();
/* 135 */     this.callable = paramCallable;
/* 136 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   public FutureTask(Runnable paramRunnable, V paramV)
/*     */   {
/* 152 */     this.callable = Executors.callable(paramRunnable, paramV);
/* 153 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   public boolean isCancelled() {
/* 157 */     return this.state >= 4;
/*     */   }
/*     */ 
/*     */   public boolean isDone() {
/* 161 */     return this.state != 0;
/*     */   }
/*     */ 
/*     */   public boolean cancel(boolean paramBoolean) {
/* 165 */     if (this.state != 0)
/* 166 */       return false;
/* 167 */     if (paramBoolean) {
/* 168 */       if (!UNSAFE.compareAndSwapInt(this, stateOffset, 0, 5))
/* 169 */         return false;
/* 170 */       Thread localThread = this.runner;
/* 171 */       if (localThread != null)
/* 172 */         localThread.interrupt();
/* 173 */       UNSAFE.putOrderedInt(this, stateOffset, 6);
/*     */     }
/* 175 */     else if (!UNSAFE.compareAndSwapInt(this, stateOffset, 0, 4)) {
/* 176 */       return false;
/* 177 */     }finishCompletion();
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   public V get()
/*     */     throws InterruptedException, ExecutionException
/*     */   {
/* 185 */     int i = this.state;
/* 186 */     if (i <= 1)
/* 187 */       i = awaitDone(false, 0L);
/* 188 */     return report(i);
/*     */   }
/*     */ 
/*     */   public V get(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 196 */     if (paramTimeUnit == null)
/* 197 */       throw new NullPointerException();
/* 198 */     int i = this.state;
/* 199 */     if ((i <= 1) && ((i = awaitDone(true, paramTimeUnit.toNanos(paramLong))) <= 1))
/*     */     {
/* 201 */       throw new TimeoutException();
/* 202 */     }return report(i);
/*     */   }
/*     */ 
/*     */   protected void done()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void set(V paramV)
/*     */   {
/* 226 */     if (UNSAFE.compareAndSwapInt(this, stateOffset, 0, 1)) {
/* 227 */       this.outcome = paramV;
/* 228 */       UNSAFE.putOrderedInt(this, stateOffset, 2);
/* 229 */       finishCompletion();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setException(Throwable paramThrowable)
/*     */   {
/* 244 */     if (UNSAFE.compareAndSwapInt(this, stateOffset, 0, 1)) {
/* 245 */       this.outcome = paramThrowable;
/* 246 */       UNSAFE.putOrderedInt(this, stateOffset, 3);
/* 247 */       finishCompletion();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run() {
/* 252 */     if ((this.state != 0) || (!UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())))
/*     */     {
/* 255 */       return;
/*     */     }try {
/* 257 */       Callable localCallable = this.callable;
/* 258 */       if ((localCallable != null) && (this.state == 0)) {
/*     */         Object localObject1;
/*     */         int j;
/*     */         try { localObject1 = localCallable.call();
/* 263 */           j = 1;
/*     */         } catch (Throwable localThrowable) {
/* 265 */           localObject1 = null;
/* 266 */           j = 0;
/* 267 */           setException(localThrowable);
/*     */         }
/* 269 */         if (j != 0) {
/* 270 */           set(localObject1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 275 */       this.runner = null;
/*     */ 
/* 278 */       int i = this.state;
/* 279 */       if (i >= 5)
/* 280 */         handlePossibleCancellationInterrupt(i);
/*     */     }
/*     */     finally
/*     */     {
/* 275 */       this.runner = null;
/*     */ 
/* 278 */       int k = this.state;
/* 279 */       if (k >= 5)
/* 280 */         handlePossibleCancellationInterrupt(k);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean runAndReset()
/*     */   {
/* 294 */     if ((this.state != 0) || (!UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())))
/*     */     {
/* 297 */       return false;
/* 298 */     }int i = 0;
/* 299 */     int j = this.state;
/*     */     try {
/* 301 */       Callable localCallable = this.callable;
/* 302 */       if ((localCallable != null) && (j == 0)) {
/*     */         try {
/* 304 */           localCallable.call();
/* 305 */           i = 1;
/*     */         } catch (Throwable localThrowable) {
/* 307 */           setException(localThrowable);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 313 */       this.runner = null;
/*     */ 
/* 316 */       j = this.state;
/* 317 */       if (j >= 5)
/* 318 */         handlePossibleCancellationInterrupt(j);
/*     */     }
/*     */     finally
/*     */     {
/* 313 */       this.runner = null;
/*     */ 
/* 316 */       j = this.state;
/* 317 */       if (j >= 5)
/* 318 */         handlePossibleCancellationInterrupt(j);
/*     */     }
/* 320 */     return (i != 0) && (j == 0);
/*     */   }
/*     */ 
/*     */   private void handlePossibleCancellationInterrupt(int paramInt)
/*     */   {
/* 330 */     if (paramInt == 5)
/* 331 */       while (this.state == 5)
/* 332 */         Thread.yield();
/*     */   }
/*     */ 
/*     */   private void finishCompletion()
/*     */   {
/*     */     Object localObject;
/* 362 */     while ((localObject = this.waiters) != null) {
/* 363 */       if (UNSAFE.compareAndSwapObject(this, waitersOffset, localObject, null)) {
/*     */         while (true) {
/* 365 */           Thread localThread = ((WaitNode)localObject).thread;
/* 366 */           if (localThread != null) {
/* 367 */             ((WaitNode)localObject).thread = null;
/* 368 */             LockSupport.unpark(localThread);
/*     */           }
/* 370 */           WaitNode localWaitNode = ((WaitNode)localObject).next;
/* 371 */           if (localWaitNode == null)
/*     */             break;
/* 373 */           ((WaitNode)localObject).next = null;
/* 374 */           localObject = localWaitNode;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 380 */     done();
/*     */ 
/* 382 */     this.callable = null;
/*     */   }
/*     */ 
/*     */   private int awaitDone(boolean paramBoolean, long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 394 */     long l = paramBoolean ? System.nanoTime() + paramLong : 0L;
/* 395 */     WaitNode localWaitNode = null;
/* 396 */     boolean bool = false;
/*     */     while (true) {
/* 398 */       if (Thread.interrupted()) {
/* 399 */         removeWaiter(localWaitNode);
/* 400 */         throw new InterruptedException();
/*     */       }
/*     */ 
/* 403 */       int i = this.state;
/* 404 */       if (i > 1) {
/* 405 */         if (localWaitNode != null)
/* 406 */           localWaitNode.thread = null;
/* 407 */         return i;
/*     */       }
/* 409 */       if (i == 1) {
/* 410 */         Thread.yield();
/* 411 */       } else if (localWaitNode == null) {
/* 412 */         localWaitNode = new WaitNode();
/* 413 */       } else if (!bool) {
/* 414 */         bool = UNSAFE.compareAndSwapObject(this, waitersOffset, localWaitNode.next = this.waiters, localWaitNode);
/*     */       }
/* 416 */       else if (paramBoolean) {
/* 417 */         paramLong = l - System.nanoTime();
/* 418 */         if (paramLong <= 0L) {
/* 419 */           removeWaiter(localWaitNode);
/* 420 */           return this.state;
/*     */         }
/* 422 */         LockSupport.parkNanos(this, paramLong);
/*     */       }
/*     */       else {
/* 425 */         LockSupport.park(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeWaiter(WaitNode paramWaitNode)
/*     */   {
/* 440 */     if (paramWaitNode != null) {
/* 441 */       paramWaitNode.thread = null;
/*     */ 
/* 444 */       Object localObject1 = null;
/*     */       WaitNode localWaitNode;
/* 444 */       for (Object localObject2 = this.waiters; ; localObject2 = localWaitNode) { if (localObject2 == null) return;
/* 445 */         localWaitNode = ((WaitNode)localObject2).next;
/* 446 */         if (((WaitNode)localObject2).thread != null) {
/* 447 */           localObject1 = localObject2; } else {
/* 448 */           if (localObject1 != null) {
/* 449 */             localObject1.next = localWaitNode;
/* 450 */             if (localObject1.thread != null) continue;
/* 451 */             break;
/*     */           }
/* 453 */           if (!UNSAFE.compareAndSwapObject(this, waitersOffset, localObject2, localWaitNode))
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 469 */       UNSAFE = Unsafe.getUnsafe();
/* 470 */       FutureTask localFutureTask = FutureTask.class;
/* 471 */       stateOffset = UNSAFE.objectFieldOffset(localFutureTask.getDeclaredField("state"));
/*     */ 
/* 473 */       runnerOffset = UNSAFE.objectFieldOffset(localFutureTask.getDeclaredField("runner"));
/*     */ 
/* 475 */       waitersOffset = UNSAFE.objectFieldOffset(localFutureTask.getDeclaredField("waiters"));
/*     */     }
/*     */     catch (Exception localException) {
/* 478 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class WaitNode
/*     */   {
/* 353 */     volatile Thread thread = Thread.currentThread();
/*     */     volatile WaitNode next;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.FutureTask
 * JD-Core Version:    0.6.2
 */