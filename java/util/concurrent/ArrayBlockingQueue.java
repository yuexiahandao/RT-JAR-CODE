/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ public class ArrayBlockingQueue<E> extends AbstractQueue<E>
/*     */   implements BlockingQueue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -817911632652898426L;
/*     */   final Object[] items;
/*     */   int takeIndex;
/*     */   int putIndex;
/*     */   int count;
/*     */   final ReentrantLock lock;
/*     */   private final Condition notEmpty;
/*     */   private final Condition notFull;
/*     */ 
/*     */   final int inc(int paramInt)
/*     */   {
/* 116 */     paramInt++; return paramInt == this.items.length ? 0 : paramInt;
/*     */   }
/*     */ 
/*     */   final int dec(int paramInt)
/*     */   {
/* 123 */     return (paramInt == 0 ? this.items.length : paramInt) - 1;
/*     */   }
/*     */ 
/*     */   static <E> E cast(Object paramObject)
/*     */   {
/* 128 */     return paramObject;
/*     */   }
/*     */ 
/*     */   final E itemAt(int paramInt)
/*     */   {
/* 135 */     return cast(this.items[paramInt]);
/*     */   }
/*     */ 
/*     */   private static void checkNotNull(Object paramObject)
/*     */   {
/* 144 */     if (paramObject == null)
/* 145 */       throw new NullPointerException();
/*     */   }
/*     */ 
/*     */   private void insert(E paramE)
/*     */   {
/* 153 */     this.items[this.putIndex] = paramE;
/* 154 */     this.putIndex = inc(this.putIndex);
/* 155 */     this.count += 1;
/* 156 */     this.notEmpty.signal();
/*     */   }
/*     */ 
/*     */   private E extract()
/*     */   {
/* 164 */     Object[] arrayOfObject = this.items;
/* 165 */     Object localObject = cast(arrayOfObject[this.takeIndex]);
/* 166 */     arrayOfObject[this.takeIndex] = null;
/* 167 */     this.takeIndex = inc(this.takeIndex);
/* 168 */     this.count -= 1;
/* 169 */     this.notFull.signal();
/* 170 */     return localObject;
/*     */   }
/*     */ 
/*     */   void removeAt(int paramInt)
/*     */   {
/* 179 */     Object[] arrayOfObject = this.items;
/*     */ 
/* 181 */     if (paramInt == this.takeIndex) {
/* 182 */       arrayOfObject[this.takeIndex] = null;
/* 183 */       this.takeIndex = inc(this.takeIndex);
/*     */     }
/*     */     else {
/*     */       while (true) {
/* 187 */         int i = inc(paramInt);
/* 188 */         if (i != this.putIndex) {
/* 189 */           arrayOfObject[paramInt] = arrayOfObject[i];
/* 190 */           paramInt = i;
/*     */         } else {
/* 192 */           arrayOfObject[paramInt] = null;
/* 193 */           this.putIndex = paramInt;
/* 194 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 198 */     this.count -= 1;
/* 199 */     this.notFull.signal();
/*     */   }
/*     */ 
/*     */   public ArrayBlockingQueue(int paramInt)
/*     */   {
/* 210 */     this(paramInt, false);
/*     */   }
/*     */ 
/*     */   public ArrayBlockingQueue(int paramInt, boolean paramBoolean)
/*     */   {
/* 224 */     if (paramInt <= 0)
/* 225 */       throw new IllegalArgumentException();
/* 226 */     this.items = new Object[paramInt];
/* 227 */     this.lock = new ReentrantLock(paramBoolean);
/* 228 */     this.notEmpty = this.lock.newCondition();
/* 229 */     this.notFull = this.lock.newCondition();
/*     */   }
/*     */ 
/*     */   public ArrayBlockingQueue(int paramInt, boolean paramBoolean, Collection<? extends E> paramCollection)
/*     */   {
/* 250 */     this(paramInt, paramBoolean);
/*     */ 
/* 252 */     ReentrantLock localReentrantLock = this.lock;
/* 253 */     localReentrantLock.lock();
/*     */     try {
/* 255 */       int i = 0;
/*     */       try {
/* 257 */         for (localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/* 258 */           checkNotNull(localObject1);
/* 259 */           this.items[(i++)] = localObject1;
/*     */         }
/*     */       }
/*     */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */       {
/*     */         Iterator localIterator;
/* 262 */         throw new IllegalArgumentException();
/*     */       }
/* 264 */       this.count = i;
/* 265 */       this.putIndex = (i == paramInt ? 0 : i);
/*     */     } finally {
/* 267 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 283 */     return super.add(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 296 */     checkNotNull(paramE);
/* 297 */     ReentrantLock localReentrantLock = this.lock;
/* 298 */     localReentrantLock.lock();
/*     */     try
/*     */     {
/*     */       boolean bool;
/* 300 */       if (this.count == this.items.length) {
/* 301 */         return false;
/*     */       }
/* 303 */       insert(paramE);
/* 304 */       return true;
/*     */     }
/*     */     finally {
/* 307 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void put(E paramE)
/*     */     throws InterruptedException
/*     */   {
/* 319 */     checkNotNull(paramE);
/* 320 */     ReentrantLock localReentrantLock = this.lock;
/* 321 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/* 323 */       while (this.count == this.items.length)
/* 324 */         this.notFull.await();
/* 325 */       insert(paramE);
/*     */     } finally {
/* 327 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 342 */     checkNotNull(paramE);
/* 343 */     long l = paramTimeUnit.toNanos(paramLong);
/* 344 */     ReentrantLock localReentrantLock = this.lock;
/* 345 */     localReentrantLock.lockInterruptibly();
/*     */     try
/*     */     {
/*     */       boolean bool;
/* 347 */       while (this.count == this.items.length) {
/* 348 */         if (l <= 0L)
/* 349 */           return false;
/* 350 */         l = this.notFull.awaitNanos(l);
/*     */       }
/* 352 */       insert(paramE);
/* 353 */       return true;
/*     */     } finally {
/* 355 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E poll() {
/* 360 */     ReentrantLock localReentrantLock = this.lock;
/* 361 */     localReentrantLock.lock();
/*     */     try {
/* 363 */       return this.count == 0 ? null : extract();
/*     */     } finally {
/* 365 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E take() throws InterruptedException {
/* 370 */     ReentrantLock localReentrantLock = this.lock;
/* 371 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/* 373 */       while (this.count == 0)
/* 374 */         this.notEmpty.await();
/* 375 */       return extract();
/*     */     } finally {
/* 377 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
/* 382 */     long l = paramTimeUnit.toNanos(paramLong);
/* 383 */     ReentrantLock localReentrantLock = this.lock;
/* 384 */     localReentrantLock.lockInterruptibly();
/*     */     try
/*     */     {
/*     */       Object localObject1;
/* 386 */       while (this.count == 0) {
/* 387 */         if (l <= 0L)
/* 388 */           return null;
/* 389 */         l = this.notEmpty.awaitNanos(l);
/*     */       }
/* 391 */       return extract();
/*     */     } finally {
/* 393 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public E peek() {
/* 398 */     ReentrantLock localReentrantLock = this.lock;
/* 399 */     localReentrantLock.lock();
/*     */     try {
/* 401 */       return this.count == 0 ? null : itemAt(this.takeIndex);
/*     */     } finally {
/* 403 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 415 */     ReentrantLock localReentrantLock = this.lock;
/* 416 */     localReentrantLock.lock();
/*     */     try {
/* 418 */       return this.count;
/*     */     } finally {
/* 420 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int remainingCapacity()
/*     */   {
/* 438 */     ReentrantLock localReentrantLock = this.lock;
/* 439 */     localReentrantLock.lock();
/*     */     try {
/* 441 */       return this.items.length - this.count;
/*     */     } finally {
/* 443 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 465 */     if (paramObject == null) return false;
/* 466 */     Object[] arrayOfObject = this.items;
/* 467 */     ReentrantLock localReentrantLock = this.lock;
/* 468 */     localReentrantLock.lock();
/*     */     try {
/* 470 */       int i = this.takeIndex; for (int j = this.count; j > 0; j--) {
/* 471 */         if (paramObject.equals(arrayOfObject[i])) {
/* 472 */           removeAt(i);
/* 473 */           return true;
/*     */         }
/* 470 */         i = inc(i);
/*     */       }
/*     */ 
/* 476 */       return 0;
/*     */     } finally {
/* 478 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 491 */     if (paramObject == null) return false;
/* 492 */     Object[] arrayOfObject = this.items;
/* 493 */     ReentrantLock localReentrantLock = this.lock;
/* 494 */     localReentrantLock.lock();
/*     */     try {
/* 496 */       int i = this.takeIndex; for (int j = this.count; j > 0; j--) {
/* 497 */         if (paramObject.equals(arrayOfObject[i]))
/* 498 */           return true;
/* 496 */         i = inc(i);
/*     */       }
/*     */ 
/* 499 */       return 0;
/*     */     } finally {
/* 501 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 519 */     Object[] arrayOfObject1 = this.items;
/* 520 */     ReentrantLock localReentrantLock = this.lock;
/* 521 */     localReentrantLock.lock();
/*     */     try {
/* 523 */       int i = this.count;
/* 524 */       Object[] arrayOfObject2 = new Object[i];
/* 525 */       int j = this.takeIndex; for (int k = 0; k < i; k++) {
/* 526 */         arrayOfObject2[k] = arrayOfObject1[j];
/*     */ 
/* 525 */         j = inc(j);
/*     */       }
/* 527 */       return arrayOfObject2;
/*     */     } finally {
/* 529 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 571 */     Object[] arrayOfObject = this.items;
/* 572 */     ReentrantLock localReentrantLock = this.lock;
/* 573 */     localReentrantLock.lock();
/*     */     try {
/* 575 */       int i = this.count;
/* 576 */       int j = paramArrayOfT.length;
/* 577 */       if (j < i) {
/* 578 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*     */       }
/* 580 */       int k = this.takeIndex; for (int m = 0; m < i; m++) {
/* 581 */         paramArrayOfT[m] = arrayOfObject[k];
/*     */ 
/* 580 */         k = inc(k);
/*     */       }
/* 582 */       if (j > i)
/* 583 */         paramArrayOfT[i] = null;
/* 584 */       return paramArrayOfT;
/*     */     } finally {
/* 586 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 591 */     ReentrantLock localReentrantLock = this.lock;
/* 592 */     localReentrantLock.lock();
/*     */     try {
/* 594 */       int i = this.count;
/* 595 */       if (i == 0) {
/* 596 */         return "[]";
/*     */       }
/* 598 */       Object localObject1 = new StringBuilder();
/* 599 */       ((StringBuilder)localObject1).append('[');
/* 600 */       for (int j = this.takeIndex; ; j = inc(j)) {
/* 601 */         Object localObject2 = this.items[j];
/* 602 */         ((StringBuilder)localObject1).append(localObject2 == this ? "(this Collection)" : localObject2);
/* 603 */         i--; if (i == 0)
/* 604 */           return ']';
/* 605 */         ((StringBuilder)localObject1).append(',').append(' ');
/*     */       }
/*     */     } finally {
/* 608 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 617 */     Object[] arrayOfObject = this.items;
/* 618 */     ReentrantLock localReentrantLock = this.lock;
/* 619 */     localReentrantLock.lock();
/*     */     try {
/* 621 */       int i = this.takeIndex; for (int j = this.count; j > 0; j--) {
/* 622 */         arrayOfObject[i] = null;
/*     */ 
/* 621 */         i = inc(i);
/*     */       }
/* 623 */       this.count = 0;
/* 624 */       this.putIndex = 0;
/* 625 */       this.takeIndex = 0;
/* 626 */       this.notFull.signalAll();
/*     */     } finally {
/* 628 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection)
/*     */   {
/* 639 */     checkNotNull(paramCollection);
/* 640 */     if (paramCollection == this)
/* 641 */       throw new IllegalArgumentException();
/* 642 */     Object[] arrayOfObject = this.items;
/* 643 */     ReentrantLock localReentrantLock = this.lock;
/* 644 */     localReentrantLock.lock();
/*     */     try {
/* 646 */       int i = this.takeIndex;
/* 647 */       int j = 0;
/* 648 */       int k = this.count;
/* 649 */       while (j < k) {
/* 650 */         paramCollection.add(cast(arrayOfObject[i]));
/* 651 */         arrayOfObject[i] = null;
/* 652 */         i = inc(i);
/* 653 */         j++;
/*     */       }
/* 655 */       if (j > 0) {
/* 656 */         this.count = 0;
/* 657 */         this.putIndex = 0;
/* 658 */         this.takeIndex = 0;
/* 659 */         this.notFull.signalAll();
/*     */       }
/* 661 */       return j;
/*     */     } finally {
/* 663 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*     */   {
/* 674 */     checkNotNull(paramCollection);
/* 675 */     if (paramCollection == this)
/* 676 */       throw new IllegalArgumentException();
/* 677 */     if (paramInt <= 0)
/* 678 */       return 0;
/* 679 */     Object[] arrayOfObject = this.items;
/* 680 */     ReentrantLock localReentrantLock = this.lock;
/* 681 */     localReentrantLock.lock();
/*     */     try {
/* 683 */       int i = this.takeIndex;
/* 684 */       int j = 0;
/* 685 */       int k = paramInt < this.count ? paramInt : this.count;
/* 686 */       while (j < k) {
/* 687 */         paramCollection.add(cast(arrayOfObject[i]));
/* 688 */         arrayOfObject[i] = null;
/* 689 */         i = inc(i);
/* 690 */         j++;
/*     */       }
/* 692 */       if (j > 0) {
/* 693 */         this.count -= j;
/* 694 */         this.takeIndex = i;
/* 695 */         this.notFull.signalAll();
/*     */       }
/* 697 */       return j;
/*     */     } finally {
/* 699 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 717 */     return new Itr();
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     private int remaining;
/*     */     private int nextIndex;
/*     */     private E nextItem;
/*     */     private E lastItem;
/*     */     private int lastRet;
/*     */ 
/*     */     Itr()
/*     */     {
/* 744 */       ReentrantLock localReentrantLock = ArrayBlockingQueue.this.lock;
/* 745 */       localReentrantLock.lock();
/*     */       try {
/* 747 */         this.lastRet = -1;
/* 748 */         if ((this.remaining = ArrayBlockingQueue.this.count) > 0)
/* 749 */           this.nextItem = ArrayBlockingQueue.this.itemAt(this.nextIndex = ArrayBlockingQueue.this.takeIndex);
/*     */       } finally {
/* 751 */         localReentrantLock.unlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 756 */       return this.remaining > 0;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 760 */       ReentrantLock localReentrantLock = ArrayBlockingQueue.this.lock;
/* 761 */       localReentrantLock.lock();
/*     */       try {
/* 763 */         if (this.remaining <= 0)
/* 764 */           throw new NoSuchElementException();
/* 765 */         this.lastRet = this.nextIndex;
/* 766 */         Object localObject1 = ArrayBlockingQueue.this.itemAt(this.nextIndex);
/* 767 */         if (localObject1 == null) {
/* 768 */           localObject1 = this.nextItem;
/* 769 */           this.lastItem = null;
/*     */         }
/*     */ 
/* 773 */         while ((--this.remaining > 0) && ((this.nextItem = ArrayBlockingQueue.this.itemAt(this.nextIndex = ArrayBlockingQueue.this.inc(this.nextIndex))) == null));
/* 776 */         return localObject1;
/*     */       } finally {
/* 778 */         localReentrantLock.unlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 783 */       ReentrantLock localReentrantLock = ArrayBlockingQueue.this.lock;
/* 784 */       localReentrantLock.lock();
/*     */       try {
/* 786 */         int i = this.lastRet;
/* 787 */         if (i == -1)
/* 788 */           throw new IllegalStateException();
/* 789 */         this.lastRet = -1;
/* 790 */         Object localObject1 = this.lastItem;
/* 791 */         this.lastItem = null;
/*     */ 
/* 793 */         if ((localObject1 != null) && (localObject1 == ArrayBlockingQueue.this.items[i])) {
/* 794 */           int j = i == ArrayBlockingQueue.this.takeIndex ? 1 : 0;
/* 795 */           ArrayBlockingQueue.this.removeAt(i);
/* 796 */           if (j == 0)
/* 797 */             this.nextIndex = ArrayBlockingQueue.this.dec(this.nextIndex);
/*     */         }
/*     */       } finally {
/* 800 */         localReentrantLock.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ArrayBlockingQueue
 * JD-Core Version:    0.6.2
 */