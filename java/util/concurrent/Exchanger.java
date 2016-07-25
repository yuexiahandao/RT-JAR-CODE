/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ 
/*     */ public class Exchanger<V>
/*     */ {
/* 213 */   private static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */   private static final int CAPACITY = 32;
/* 233 */   private static final int FULL = Math.max(0, Math.min(32, NCPU / 2) - 1);
/*     */ 
/* 249 */   private static final int SPINS = NCPU == 1 ? 0 : 2000;
/*     */ 
/* 258 */   private static final int TIMED_SPINS = SPINS / 20;
/*     */ 
/* 266 */   private static final Object CANCEL = new Object();
/*     */ 
/* 273 */   private static final Object NULL_ITEM = new Object();
/*     */ 
/* 315 */   private volatile Slot[] arena = new Slot[32];
/*     */ 
/* 324 */   private final AtomicInteger max = new AtomicInteger();
/*     */ 
/*     */   private Object doExchange(Object paramObject, boolean paramBoolean, long paramLong)
/*     */   {
/* 338 */     Node localNode = new Node(paramObject);
/* 339 */     int i = hashIndex();
/* 340 */     int j = 0;
/*     */     while (true)
/*     */     {
/* 344 */       Slot localSlot = this.arena[i];
/* 345 */       if (localSlot == null) {
/* 346 */         createSlot(i);
/*     */       }
/*     */       else
/*     */       {
/*     */         Object localObject1;
/*     */         Object localObject2;
/* 347 */         if (((localObject1 = localSlot.get()) != null) && (localSlot.compareAndSet(localObject1, null)))
/*     */         {
/* 349 */           localObject2 = (Node)localObject1;
/* 350 */           if (((Node)localObject2).compareAndSet(null, paramObject)) {
/* 351 */             LockSupport.unpark(((Node)localObject2).waiter);
/* 352 */             return ((Node)localObject2).item;
/*     */           }
/*     */         }
/* 355 */         else if ((localObject1 == null) && (localSlot.compareAndSet(null, localNode)))
/*     */         {
/* 357 */           if (i == 0) {
/* 358 */             return paramBoolean ? awaitNanos(localNode, localSlot, paramLong) : await(localNode, localSlot);
/*     */           }
/*     */ 
/* 361 */           localObject2 = spinWait(localNode, localSlot);
/* 362 */           if (localObject2 != CANCEL)
/* 363 */             return localObject2;
/* 364 */           localNode = new Node(paramObject);
/* 365 */           int m = this.max.get();
/* 366 */           if (m > i >>>= 1)
/* 367 */             this.max.compareAndSet(m, m - 1);
/*     */         } else {
/* 369 */           j++; if (j > 1) {
/* 370 */             int k = this.max.get();
/* 371 */             if ((j > 3) && (k < FULL) && (this.max.compareAndSet(k, k + 1))) {
/* 372 */               i = k + 1; } else {
/* 373 */               i--; if (i < 0)
/* 374 */                 i = k;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final int hashIndex()
/*     */   {
/* 402 */     long l = Thread.currentThread().getId();
/* 403 */     int i = ((int)(l ^ l >>> 32) ^ 0x811C9DC5) * 16777619;
/*     */ 
/* 405 */     int j = this.max.get();
/* 406 */     int k = -1024 >> j & 0x4 | 504 >>> j & 0x2 | -65294 >>> j & 0x1;
/*     */     int m;
/* 410 */     while ((m = i & (1 << k) - 1) > j)
/* 411 */       i = i >>> k | i << 33 - k;
/* 412 */     return m;
/*     */   }
/*     */ 
/*     */   private void createSlot(int paramInt)
/*     */   {
/* 425 */     Slot localSlot = new Slot(null);
/* 426 */     Slot[] arrayOfSlot = this.arena;
/* 427 */     synchronized (arrayOfSlot) {
/* 428 */       if (arrayOfSlot[paramInt] == null)
/* 429 */         arrayOfSlot[paramInt] = localSlot;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean tryCancel(Node paramNode, Slot paramSlot)
/*     */   {
/* 443 */     if (!paramNode.compareAndSet(null, CANCEL))
/* 444 */       return false;
/* 445 */     if (paramSlot.get() == paramNode)
/* 446 */       paramSlot.compareAndSet(paramNode, null);
/* 447 */     return true;
/*     */   }
/*     */ 
/*     */   private static Object spinWait(Node paramNode, Slot paramSlot)
/*     */   {
/* 462 */     int i = SPINS;
/*     */     while (true) {
/* 464 */       Object localObject = paramNode.get();
/* 465 */       if (localObject != null)
/* 466 */         return localObject;
/* 467 */       if (i > 0)
/* 468 */         i--;
/*     */       else
/* 470 */         tryCancel(paramNode, paramSlot);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object await(Node paramNode, Slot paramSlot)
/*     */   {
/* 492 */     Thread localThread = Thread.currentThread();
/* 493 */     int i = SPINS;
/*     */     while (true) {
/* 495 */       Object localObject = paramNode.get();
/* 496 */       if (localObject != null)
/* 497 */         return localObject;
/* 498 */       if (i > 0)
/* 499 */         i--;
/* 500 */       else if (paramNode.waiter == null)
/* 501 */         paramNode.waiter = localThread;
/* 502 */       else if (localThread.isInterrupted())
/* 503 */         tryCancel(paramNode, paramSlot);
/*     */       else
/* 505 */         LockSupport.park(paramNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object awaitNanos(Node paramNode, Slot paramSlot, long paramLong)
/*     */   {
/* 519 */     int i = TIMED_SPINS;
/* 520 */     long l1 = 0L;
/* 521 */     Thread localThread = null;
/*     */     while (true) {
/* 523 */       Object localObject = paramNode.get();
/* 524 */       if (localObject != null)
/* 525 */         return localObject;
/* 526 */       long l2 = System.nanoTime();
/* 527 */       if (localThread == null)
/* 528 */         localThread = Thread.currentThread();
/*     */       else
/* 530 */         paramLong -= l2 - l1;
/* 531 */       l1 = l2;
/* 532 */       if (paramLong > 0L) {
/* 533 */         if (i > 0)
/* 534 */           i--;
/* 535 */         else if (paramNode.waiter == null)
/* 536 */           paramNode.waiter = localThread;
/* 537 */         else if (localThread.isInterrupted())
/* 538 */           tryCancel(paramNode, paramSlot);
/*     */         else
/* 540 */           LockSupport.parkNanos(paramNode, paramLong);
/*     */       }
/* 542 */       else if ((tryCancel(paramNode, paramSlot)) && (!localThread.isInterrupted()))
/* 543 */         return scanOnTimeout(paramNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Object scanOnTimeout(Node paramNode)
/*     */   {
/* 563 */     for (int i = this.arena.length - 1; i >= 0; i--) {
/* 564 */       Slot localSlot = this.arena[i];
/* 565 */       if (localSlot != null)
/*     */       {
/*     */         Object localObject;
/* 566 */         while ((localObject = localSlot.get()) != null) {
/* 567 */           if (localSlot.compareAndSet(localObject, null)) {
/* 568 */             Node localNode = (Node)localObject;
/* 569 */             if (localNode.compareAndSet(null, paramNode.item)) {
/* 570 */               LockSupport.unpark(localNode.waiter);
/* 571 */               return localNode.item;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 577 */     return CANCEL;
/*     */   }
/*     */ 
/*     */   public V exchange(V paramV)
/*     */     throws InterruptedException
/*     */   {
/* 620 */     if (!Thread.interrupted()) {
/* 621 */       Object localObject = doExchange(paramV == null ? NULL_ITEM : paramV, false, 0L);
/* 622 */       if (localObject == NULL_ITEM)
/* 623 */         return null;
/* 624 */       if (localObject != CANCEL)
/* 625 */         return localObject;
/* 626 */       Thread.interrupted();
/*     */     }
/* 628 */     throw new InterruptedException();
/*     */   }
/*     */ 
/*     */   public V exchange(V paramV, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException, TimeoutException
/*     */   {
/* 675 */     if (!Thread.interrupted()) {
/* 676 */       Object localObject = doExchange(paramV == null ? NULL_ITEM : paramV, true, paramTimeUnit.toNanos(paramLong));
/*     */ 
/* 678 */       if (localObject == NULL_ITEM)
/* 679 */         return null;
/* 680 */       if (localObject != CANCEL)
/* 681 */         return localObject;
/* 682 */       if (!Thread.interrupted())
/* 683 */         throw new TimeoutException();
/*     */     }
/* 685 */     throw new InterruptedException();
/*     */   }
/*     */ 
/*     */   private static final class Node extends AtomicReference<Object>
/*     */   {
/*     */     public final Object item;
/*     */     public volatile Thread waiter;
/*     */ 
/*     */     public Node(Object paramObject)
/*     */     {
/* 294 */       this.item = paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Slot extends AtomicReference<Object>
/*     */   {
/*     */     long q0;
/*     */     long q1;
/*     */     long q2;
/*     */     long q3;
/*     */     long q4;
/*     */     long q5;
/*     */     long q6;
/*     */     long q7;
/*     */     long q8;
/*     */     long q9;
/*     */     long qa;
/*     */     long qb;
/*     */     long qc;
/*     */     long qd;
/*     */     long qe;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.Exchanger
 * JD-Core Version:    0.6.2
 */