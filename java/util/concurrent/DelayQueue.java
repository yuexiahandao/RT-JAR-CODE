/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ public class DelayQueue<E extends Delayed> extends AbstractQueue<E>
/*     */   implements BlockingQueue<E>
/*     */ {
/*  71 */   private final transient ReentrantLock lock = new ReentrantLock();
/*  72 */   private final PriorityQueue<E> q = new PriorityQueue();
/*     */ 
/*  90 */   private Thread leader = null;
/*     */ 
/*  97 */   private final Condition available = this.lock.newCondition();
/*     */ 
/*     */   public DelayQueue()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DelayQueue(Collection<? extends E> paramCollection)
/*     */   {
/* 113 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 124 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 135 */     ReentrantLock localReentrantLock = this.lock;
/* 136 */     localReentrantLock.lock();
/*     */     try {
/* 138 */       this.q.offer(paramE);
/* 139 */       if (this.q.peek() == paramE) {
/* 140 */         this.leader = null;
/* 141 */         this.available.signal();
/*     */       }
/* 143 */       return true;
/*     */     } finally {
/* 145 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void put(E paramE)
/*     */   {
/* 157 */     offer(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*     */   {
/* 171 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 182 */     ReentrantLock localReentrantLock = this.lock;
/* 183 */     localReentrantLock.lock();
/*     */     try {
/* 185 */       Delayed localDelayed = (Delayed)this.q.peek();
/*     */       Object localObject1;
/* 186 */       if ((localDelayed == null) || (localDelayed.getDelay(TimeUnit.NANOSECONDS) > 0L)) {
/* 187 */         return null;
/*     */       }
/* 189 */       return (Delayed)this.q.poll();
/*     */     } finally {
/* 191 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E take()
/*     */     throws InterruptedException
/*     */   {
/* 203 */     ReentrantLock localReentrantLock = this.lock;
/* 204 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/*     */       while (true) {
/* 207 */         Delayed localDelayed = (Delayed)this.q.peek();
/* 208 */         if (localDelayed == null) {
/* 209 */           this.available.await();
/*     */         } else {
/* 211 */           long l = localDelayed.getDelay(TimeUnit.NANOSECONDS);
/*     */           Object localObject1;
/* 212 */           if (l <= 0L)
/* 213 */             return (Delayed)this.q.poll();
/* 214 */           if (this.leader != null) {
/* 215 */             this.available.await();
/*     */           } else {
/* 217 */             localObject1 = Thread.currentThread();
/* 218 */             this.leader = ((Thread)localObject1);
/*     */             try {
/* 220 */               this.available.awaitNanos(l);
/*     */ 
/* 222 */               if (this.leader == localObject1)
/* 223 */                 this.leader = null;
/*     */             }
/*     */             finally
/*     */             {
/* 222 */               if (this.leader == localObject1)
/* 223 */                 this.leader = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } finally {
/* 229 */       if ((this.leader == null) && (this.q.peek() != null))
/* 230 */         this.available.signal();
/* 231 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E poll(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 246 */     long l1 = paramTimeUnit.toNanos(paramLong);
/* 247 */     ReentrantLock localReentrantLock = this.lock;
/* 248 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/*     */       while (true) {
/* 251 */         Delayed localDelayed = (Delayed)this.q.peek();
/* 252 */         if (localDelayed == null) {
/* 253 */           if (l1 <= 0L) {
/* 254 */             return null;
/*     */           }
/* 256 */           l1 = this.available.awaitNanos(l1);
/*     */         } else {
/* 258 */           long l2 = localDelayed.getDelay(TimeUnit.NANOSECONDS);
/*     */           Object localObject1;
/* 259 */           if (l2 <= 0L)
/* 260 */             return (Delayed)this.q.poll();
/* 261 */           if (l1 <= 0L)
/* 262 */             return null;
/* 263 */           if ((l1 < l2) || (this.leader != null)) {
/* 264 */             l1 = this.available.awaitNanos(l1);
/*     */           } else {
/* 266 */             localObject1 = Thread.currentThread();
/* 267 */             this.leader = ((Thread)localObject1);
/*     */             try {
/* 269 */               long l3 = this.available.awaitNanos(l2);
/* 270 */               l1 -= l2 - l3;
/*     */ 
/* 272 */               if (this.leader == localObject1)
/* 273 */                 this.leader = null;
/*     */             }
/*     */             finally
/*     */             {
/* 272 */               if (this.leader == localObject1)
/* 273 */                 this.leader = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } finally {
/* 279 */       if ((this.leader == null) && (this.q.peek() != null))
/* 280 */         this.available.signal();
/* 281 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 296 */     ReentrantLock localReentrantLock = this.lock;
/* 297 */     localReentrantLock.lock();
/*     */     try {
/* 299 */       return (Delayed)this.q.peek();
/*     */     } finally {
/* 301 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size() {
/* 306 */     ReentrantLock localReentrantLock = this.lock;
/* 307 */     localReentrantLock.lock();
/*     */     try {
/* 309 */       return this.q.size();
/*     */     } finally {
/* 311 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection)
/*     */   {
/* 322 */     if (paramCollection == null)
/* 323 */       throw new NullPointerException();
/* 324 */     if (paramCollection == this)
/* 325 */       throw new IllegalArgumentException();
/* 326 */     ReentrantLock localReentrantLock = this.lock;
/* 327 */     localReentrantLock.lock();
/*     */     try { Delayed localDelayed1 = 0;
/*     */       Delayed localDelayed2;
/*     */       while (true) { localDelayed2 = (Delayed)this.q.peek();
/* 332 */         if ((localDelayed2 == null) || (localDelayed2.getDelay(TimeUnit.NANOSECONDS) > 0L))
/*     */           break;
/* 334 */         paramCollection.add(this.q.poll());
/* 335 */         localDelayed1++;
/*     */       }
/* 337 */       return localDelayed1;
/*     */     } finally {
/* 339 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*     */   {
/* 350 */     if (paramCollection == null)
/* 351 */       throw new NullPointerException();
/* 352 */     if (paramCollection == this)
/* 353 */       throw new IllegalArgumentException();
/* 354 */     if (paramInt <= 0)
/* 355 */       return 0;
/* 356 */     ReentrantLock localReentrantLock = this.lock;
/* 357 */     localReentrantLock.lock();
/*     */     try {
/* 359 */       int i = 0;
/* 360 */       while (i < paramInt) {
/* 361 */         Delayed localDelayed = (Delayed)this.q.peek();
/* 362 */         if ((localDelayed == null) || (localDelayed.getDelay(TimeUnit.NANOSECONDS) > 0L))
/*     */           break;
/* 364 */         paramCollection.add(this.q.poll());
/* 365 */         i++;
/*     */       }
/* 367 */       return i;
/*     */     } finally {
/* 369 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 380 */     ReentrantLock localReentrantLock = this.lock;
/* 381 */     localReentrantLock.lock();
/*     */     try {
/* 383 */       this.q.clear();
/*     */     } finally {
/* 385 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int remainingCapacity()
/*     */   {
/* 396 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 413 */     ReentrantLock localReentrantLock = this.lock;
/* 414 */     localReentrantLock.lock();
/*     */     try {
/* 416 */       return this.q.toArray();
/*     */     } finally {
/* 418 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 459 */     ReentrantLock localReentrantLock = this.lock;
/* 460 */     localReentrantLock.lock();
/*     */     try {
/* 462 */       return this.q.toArray(paramArrayOfT);
/*     */     } finally {
/* 464 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 473 */     ReentrantLock localReentrantLock = this.lock;
/* 474 */     localReentrantLock.lock();
/*     */     try {
/* 476 */       return this.q.remove(paramObject);
/*     */     } finally {
/* 478 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 497 */     return new Itr(toArray());
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     final Object[] array;
/*     */     int cursor;
/* 509 */     int lastRet = -1;
/*     */ 
/*     */     Itr(Object[] arg2)
/*     */     {
/*     */       Object localObject;
/* 510 */       this.array = localObject;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 514 */       return this.cursor < this.array.length;
/*     */     }
/*     */ 
/*     */     public E next()
/*     */     {
/* 519 */       if (this.cursor >= this.array.length)
/* 520 */         throw new NoSuchElementException();
/* 521 */       this.lastRet = this.cursor;
/* 522 */       return (Delayed)this.array[(this.cursor++)];
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 526 */       if (this.lastRet < 0)
/* 527 */         throw new IllegalStateException();
/* 528 */       Object localObject1 = this.array[this.lastRet];
/* 529 */       this.lastRet = -1;
/*     */ 
/* 532 */       DelayQueue.this.lock.lock();
/*     */       try {
/* 534 */         for (localIterator = DelayQueue.this.q.iterator(); localIterator.hasNext(); )
/* 535 */           if (localIterator.next() == localObject1) {
/* 536 */             localIterator.remove();
/*     */             return;
/*     */           }
/*     */       }
/*     */       finally
/*     */       {
/*     */         Iterator localIterator;
/* 541 */         DelayQueue.this.lock.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.DelayQueue
 * JD-Core Version:    0.6.2
 */