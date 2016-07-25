/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.SortedSet;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class PriorityBlockingQueue<E> extends AbstractQueue<E>
/*     */   implements BlockingQueue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5595510919245408276L;
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 11;
/*     */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*     */   private transient Object[] queue;
/*     */   private transient int size;
/*     */   private transient Comparator<? super E> comparator;
/*     */   private final ReentrantLock lock;
/*     */   private final Condition notEmpty;
/*     */   private volatile transient int allocationSpinLock;
/*     */   private PriorityQueue q;
/*     */   private static final Unsafe UNSAFE;
/*     */   private static final long allocationSpinLockOffset;
/*     */ 
/*     */   public PriorityBlockingQueue()
/*     */   {
/* 180 */     this(11, null);
/*     */   }
/*     */ 
/*     */   public PriorityBlockingQueue(int paramInt)
/*     */   {
/* 193 */     this(paramInt, null);
/*     */   }
/*     */ 
/*     */   public PriorityBlockingQueue(int paramInt, Comparator<? super E> paramComparator)
/*     */   {
/* 210 */     if (paramInt < 1)
/* 211 */       throw new IllegalArgumentException();
/* 212 */     this.lock = new ReentrantLock();
/* 213 */     this.notEmpty = this.lock.newCondition();
/* 214 */     this.comparator = paramComparator;
/* 215 */     this.queue = new Object[paramInt];
/*     */   }
/*     */ 
/*     */   public PriorityBlockingQueue(Collection<? extends E> paramCollection)
/*     */   {
/* 235 */     this.lock = new ReentrantLock();
/* 236 */     this.notEmpty = this.lock.newCondition();
/* 237 */     int i = 1;
/* 238 */     int j = 1;
/* 239 */     if ((paramCollection instanceof SortedSet)) {
/* 240 */       localObject = (SortedSet)paramCollection;
/* 241 */       this.comparator = ((SortedSet)localObject).comparator();
/* 242 */       i = 0;
/*     */     }
/* 244 */     else if ((paramCollection instanceof PriorityBlockingQueue)) {
/* 245 */       localObject = (PriorityBlockingQueue)paramCollection;
/*     */ 
/* 247 */       this.comparator = ((PriorityBlockingQueue)localObject).comparator();
/* 248 */       j = 0;
/* 249 */       if (localObject.getClass() == PriorityBlockingQueue.class)
/* 250 */         i = 0;
/*     */     }
/* 252 */     Object localObject = paramCollection.toArray();
/* 253 */     int k = localObject.length;
/*     */ 
/* 255 */     if (localObject.getClass() != [Ljava.lang.Object.class)
/* 256 */       localObject = Arrays.copyOf((Object[])localObject, k, [Ljava.lang.Object.class);
/* 257 */     if ((j != 0) && ((k == 1) || (this.comparator != null))) {
/* 258 */       for (int m = 0; m < k; m++)
/* 259 */         if (localObject[m] == null)
/* 260 */           throw new NullPointerException();
/*     */     }
/* 262 */     this.queue = ((Object[])localObject);
/* 263 */     this.size = k;
/* 264 */     if (i != 0)
/* 265 */       heapify();
/*     */   }
/*     */ 
/*     */   private void tryGrow(Object[] paramArrayOfObject, int paramInt)
/*     */   {
/* 278 */     this.lock.unlock();
/* 279 */     Object[] arrayOfObject = null;
/* 280 */     if ((this.allocationSpinLock == 0) && (UNSAFE.compareAndSwapInt(this, allocationSpinLockOffset, 0, 1)))
/*     */     {
/*     */       try
/*     */       {
/* 284 */         int i = paramInt + (paramInt < 64 ? paramInt + 2 : paramInt >> 1);
/*     */ 
/* 287 */         if (i - 2147483639 > 0) {
/* 288 */           int j = paramInt + 1;
/* 289 */           if ((j < 0) || (j > 2147483639))
/* 290 */             throw new OutOfMemoryError();
/* 291 */           i = 2147483639;
/*     */         }
/* 293 */         if ((i > paramInt) && (this.queue == paramArrayOfObject))
/* 294 */           arrayOfObject = new Object[i];
/*     */       } finally {
/* 296 */         this.allocationSpinLock = 0;
/*     */       }
/*     */     }
/* 299 */     if (arrayOfObject == null)
/* 300 */       Thread.yield();
/* 301 */     this.lock.lock();
/* 302 */     if ((arrayOfObject != null) && (this.queue == paramArrayOfObject)) {
/* 303 */       this.queue = arrayOfObject;
/* 304 */       System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private E dequeue()
/*     */   {
/* 312 */     int i = this.size - 1;
/* 313 */     if (i < 0) {
/* 314 */       return null;
/*     */     }
/* 316 */     Object[] arrayOfObject = this.queue;
/* 317 */     Object localObject1 = arrayOfObject[0];
/* 318 */     Object localObject2 = arrayOfObject[i];
/* 319 */     arrayOfObject[i] = null;
/* 320 */     Comparator localComparator = this.comparator;
/* 321 */     if (localComparator == null)
/* 322 */       siftDownComparable(0, localObject2, arrayOfObject, i);
/*     */     else
/* 324 */       siftDownUsingComparator(0, localObject2, arrayOfObject, i, localComparator);
/* 325 */     this.size = i;
/* 326 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private static <T> void siftUpComparable(int paramInt, T paramT, Object[] paramArrayOfObject)
/*     */   {
/* 347 */     Comparable localComparable = (Comparable)paramT;
/* 348 */     while (paramInt > 0) {
/* 349 */       int i = paramInt - 1 >>> 1;
/* 350 */       Object localObject = paramArrayOfObject[i];
/* 351 */       if (localComparable.compareTo(localObject) >= 0)
/*     */         break;
/* 353 */       paramArrayOfObject[paramInt] = localObject;
/* 354 */       paramInt = i;
/*     */     }
/* 356 */     paramArrayOfObject[paramInt] = localComparable;
/*     */   }
/*     */ 
/*     */   private static <T> void siftUpUsingComparator(int paramInt, T paramT, Object[] paramArrayOfObject, Comparator<? super T> paramComparator)
/*     */   {
/* 361 */     while (paramInt > 0) {
/* 362 */       int i = paramInt - 1 >>> 1;
/* 363 */       Object localObject = paramArrayOfObject[i];
/* 364 */       if (paramComparator.compare(paramT, localObject) >= 0)
/*     */         break;
/* 366 */       paramArrayOfObject[paramInt] = localObject;
/* 367 */       paramInt = i;
/*     */     }
/* 369 */     paramArrayOfObject[paramInt] = paramT;
/*     */   }
/*     */ 
/*     */   private static <T> void siftDownComparable(int paramInt1, T paramT, Object[] paramArrayOfObject, int paramInt2)
/*     */   {
/* 384 */     if (paramInt2 > 0) {
/* 385 */       Comparable localComparable = (Comparable)paramT;
/* 386 */       int i = paramInt2 >>> 1;
/* 387 */       while (paramInt1 < i) {
/* 388 */         int j = (paramInt1 << 1) + 1;
/* 389 */         Object localObject = paramArrayOfObject[j];
/* 390 */         int k = j + 1;
/* 391 */         if ((k < paramInt2) && (((Comparable)localObject).compareTo(paramArrayOfObject[k]) > 0))
/*     */         {
/* 393 */           localObject = paramArrayOfObject[(j = k)];
/* 394 */         }if (localComparable.compareTo(localObject) <= 0)
/*     */           break;
/* 396 */         paramArrayOfObject[paramInt1] = localObject;
/* 397 */         paramInt1 = j;
/*     */       }
/* 399 */       paramArrayOfObject[paramInt1] = localComparable;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static <T> void siftDownUsingComparator(int paramInt1, T paramT, Object[] paramArrayOfObject, int paramInt2, Comparator<? super T> paramComparator)
/*     */   {
/* 406 */     if (paramInt2 > 0) {
/* 407 */       int i = paramInt2 >>> 1;
/* 408 */       while (paramInt1 < i) {
/* 409 */         int j = (paramInt1 << 1) + 1;
/* 410 */         Object localObject = paramArrayOfObject[j];
/* 411 */         int k = j + 1;
/* 412 */         if ((k < paramInt2) && (paramComparator.compare(localObject, paramArrayOfObject[k]) > 0))
/* 413 */           localObject = paramArrayOfObject[(j = k)];
/* 414 */         if (paramComparator.compare(paramT, localObject) <= 0)
/*     */           break;
/* 416 */         paramArrayOfObject[paramInt1] = localObject;
/* 417 */         paramInt1 = j;
/*     */       }
/* 419 */       paramArrayOfObject[paramInt1] = paramT;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void heapify()
/*     */   {
/* 428 */     Object[] arrayOfObject = this.queue;
/* 429 */     int i = this.size;
/* 430 */     int j = (i >>> 1) - 1;
/* 431 */     Comparator localComparator = this.comparator;
/*     */     int k;
/* 432 */     if (localComparator == null) {
/* 433 */       for (k = j; k >= 0; k--)
/* 434 */         siftDownComparable(k, arrayOfObject[k], arrayOfObject, i);
/*     */     }
/*     */     else
/* 437 */       for (k = j; k >= 0; k--)
/* 438 */         siftDownUsingComparator(k, arrayOfObject[k], arrayOfObject, i, localComparator);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 453 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 468 */     if (paramE == null)
/* 469 */       throw new NullPointerException();
/* 470 */     ReentrantLock localReentrantLock = this.lock;
/* 471 */     localReentrantLock.lock();
/*     */     int i;
/*     */     Object[] arrayOfObject;
/*     */     int j;
/* 474 */     while ((i = this.size) >= (j = (arrayOfObject = this.queue).length))
/* 475 */       tryGrow(arrayOfObject, j);
/*     */     try {
/* 477 */       Comparator localComparator = this.comparator;
/* 478 */       if (localComparator == null)
/* 479 */         siftUpComparable(i, paramE, arrayOfObject);
/*     */       else
/* 481 */         siftUpUsingComparator(i, paramE, arrayOfObject, localComparator);
/* 482 */       this.size = (i + 1);
/* 483 */       this.notEmpty.signal();
/*     */     } finally {
/* 485 */       localReentrantLock.unlock();
/*     */     }
/* 487 */     return true;
/*     */   }
/*     */ 
/*     */   public void put(E paramE)
/*     */   {
/* 501 */     offer(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*     */   {
/* 520 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   public E poll() {
/* 524 */     ReentrantLock localReentrantLock = this.lock;
/* 525 */     localReentrantLock.lock();
/*     */     try {
/* 527 */       return dequeue();
/*     */     } finally {
/* 529 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/* 534 */   public E take() throws InterruptedException { ReentrantLock localReentrantLock = this.lock;
/* 535 */     localReentrantLock.lockInterruptibly();
/*     */     Object localObject1;
/*     */     try { while ((localObject1 = dequeue()) == null)
/* 539 */         this.notEmpty.await();
/*     */     } finally {
/* 541 */       localReentrantLock.unlock();
/*     */     }
/* 543 */     return localObject1; } 
/*     */   public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
/* 547 */     long l = paramTimeUnit.toNanos(paramLong);
/* 548 */     ReentrantLock localReentrantLock = this.lock;
/* 549 */     localReentrantLock.lockInterruptibly();
/*     */     Object localObject1;
/*     */     try { while (((localObject1 = dequeue()) == null) && (l > 0L))
/* 553 */         l = this.notEmpty.awaitNanos(l);
/*     */     } finally {
/* 555 */       localReentrantLock.unlock();
/*     */     }
/* 557 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public E peek() {
/* 561 */     ReentrantLock localReentrantLock = this.lock;
/* 562 */     localReentrantLock.lock();
/*     */     try {
/* 564 */       return this.size == 0 ? null : this.queue[0];
/*     */     } finally {
/* 566 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 580 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 584 */     ReentrantLock localReentrantLock = this.lock;
/* 585 */     localReentrantLock.lock();
/*     */     try {
/* 587 */       return this.size;
/*     */     } finally {
/* 589 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int remainingCapacity()
/*     */   {
/* 599 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   private int indexOf(Object paramObject) {
/* 603 */     if (paramObject != null) {
/* 604 */       Object[] arrayOfObject = this.queue;
/* 605 */       int i = this.size;
/* 606 */       for (int j = 0; j < i; j++)
/* 607 */         if (paramObject.equals(arrayOfObject[j]))
/* 608 */           return j;
/*     */     }
/* 610 */     return -1;
/*     */   }
/*     */ 
/*     */   private void removeAt(int paramInt)
/*     */   {
/* 617 */     Object[] arrayOfObject = this.queue;
/* 618 */     int i = this.size - 1;
/* 619 */     if (i == paramInt) {
/* 620 */       arrayOfObject[paramInt] = null;
/*     */     } else {
/* 622 */       Object localObject = arrayOfObject[i];
/* 623 */       arrayOfObject[i] = null;
/* 624 */       Comparator localComparator = this.comparator;
/* 625 */       if (localComparator == null)
/* 626 */         siftDownComparable(paramInt, localObject, arrayOfObject, i);
/*     */       else
/* 628 */         siftDownUsingComparator(paramInt, localObject, arrayOfObject, i, localComparator);
/* 629 */       if (arrayOfObject[paramInt] == localObject) {
/* 630 */         if (localComparator == null)
/* 631 */           siftUpComparable(paramInt, localObject, arrayOfObject);
/*     */         else
/* 633 */           siftUpUsingComparator(paramInt, localObject, arrayOfObject, localComparator);
/*     */       }
/*     */     }
/* 636 */     this.size = i;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 651 */     ReentrantLock localReentrantLock = this.lock;
/* 652 */     localReentrantLock.lock();
/*     */     try {
/* 654 */       int i = indexOf(paramObject);
/*     */       boolean bool;
/* 655 */       if (i == -1)
/* 656 */         return false;
/* 657 */       removeAt(i);
/* 658 */       return true;
/*     */     } finally {
/* 660 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeEQ(Object paramObject)
/*     */   {
/* 668 */     ReentrantLock localReentrantLock = this.lock;
/* 669 */     localReentrantLock.lock();
/*     */     try {
/* 671 */       Object[] arrayOfObject = this.queue;
/* 672 */       int i = 0; for (int j = this.size; i < j; i++)
/* 673 */         if (paramObject == arrayOfObject[i]) {
/* 674 */           removeAt(i);
/* 675 */           break;
/*     */         }
/*     */     }
/*     */     finally {
/* 679 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 692 */     ReentrantLock localReentrantLock = this.lock;
/* 693 */     localReentrantLock.lock();
/*     */     try {
/* 695 */       return indexOf(paramObject) != -1;
/*     */     } finally {
/* 697 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 715 */     ReentrantLock localReentrantLock = this.lock;
/* 716 */     localReentrantLock.lock();
/*     */     try {
/* 718 */       return Arrays.copyOf(this.queue, this.size);
/*     */     } finally {
/* 720 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 725 */     ReentrantLock localReentrantLock = this.lock;
/* 726 */     localReentrantLock.lock();
/*     */     try {
/* 728 */       int i = this.size;
/* 729 */       if (i == 0)
/* 730 */         return "[]";
/* 731 */       Object localObject1 = new StringBuilder();
/* 732 */       ((StringBuilder)localObject1).append('[');
/* 733 */       for (int j = 0; j < i; j++) {
/* 734 */         Object localObject2 = this.queue[j];
/* 735 */         ((StringBuilder)localObject1).append(localObject2 == this ? "(this Collection)" : localObject2);
/* 736 */         if (j != i - 1)
/* 737 */           ((StringBuilder)localObject1).append(',').append(' ');
/*     */       }
/* 739 */       return ']';
/*     */     } finally {
/* 741 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection)
/*     */   {
/* 752 */     return drainTo(paramCollection, 2147483647);
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*     */   {
/* 762 */     if (paramCollection == null)
/* 763 */       throw new NullPointerException();
/* 764 */     if (paramCollection == this)
/* 765 */       throw new IllegalArgumentException();
/* 766 */     if (paramInt <= 0)
/* 767 */       return 0;
/* 768 */     ReentrantLock localReentrantLock = this.lock;
/* 769 */     localReentrantLock.lock();
/*     */     try {
/* 771 */       int i = Math.min(this.size, paramInt);
/* 772 */       for (int j = 0; j < i; j++) {
/* 773 */         paramCollection.add(this.queue[0]);
/* 774 */         dequeue();
/*     */       }
/* 776 */       return i;
/*     */     } finally {
/* 778 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 787 */     ReentrantLock localReentrantLock = this.lock;
/* 788 */     localReentrantLock.lock();
/*     */     try {
/* 790 */       Object[] arrayOfObject = this.queue;
/* 791 */       int i = this.size;
/* 792 */       this.size = 0;
/* 793 */       for (int j = 0; j < i; j++)
/* 794 */         arrayOfObject[j] = null;
/*     */     } finally {
/* 796 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 837 */     ReentrantLock localReentrantLock = this.lock;
/* 838 */     localReentrantLock.lock();
/*     */     try {
/* 840 */       int i = this.size;
/*     */       Object localObject1;
/* 841 */       if (paramArrayOfT.length < i)
/*     */       {
/* 843 */         return (Object[])Arrays.copyOf(this.queue, this.size, paramArrayOfT.getClass());
/* 844 */       }System.arraycopy(this.queue, 0, paramArrayOfT, 0, i);
/* 845 */       if (paramArrayOfT.length > i)
/* 846 */         paramArrayOfT[i] = null;
/* 847 */       return paramArrayOfT;
/*     */     } finally {
/* 849 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 867 */     return new Itr(toArray());
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 911 */     this.lock.lock();
/*     */     try
/*     */     {
/* 914 */       this.q = new PriorityQueue(Math.max(this.size, 1), this.comparator);
/* 915 */       this.q.addAll(this);
/* 916 */       paramObjectOutputStream.defaultWriteObject();
/*     */     } finally {
/* 918 */       this.q = null;
/* 919 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 929 */       paramObjectInputStream.defaultReadObject();
/* 930 */       this.queue = new Object[this.q.size()];
/* 931 */       this.comparator = this.q.comparator();
/* 932 */       addAll(this.q);
/*     */     } finally {
/* 934 */       this.q = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 943 */       UNSAFE = Unsafe.getUnsafe();
/* 944 */       PriorityBlockingQueue localPriorityBlockingQueue = PriorityBlockingQueue.class;
/* 945 */       allocationSpinLockOffset = UNSAFE.objectFieldOffset(localPriorityBlockingQueue.getDeclaredField("allocationSpinLock"));
/*     */     }
/*     */     catch (Exception localException) {
/* 948 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   final class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     final Object[] array;
/*     */     int cursor;
/* 879 */     int lastRet = -1;
/*     */ 
/*     */     Itr(Object[] arg2)
/*     */     {
/*     */       Object localObject;
/* 880 */       this.array = localObject;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 884 */       return this.cursor < this.array.length;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 888 */       if (this.cursor >= this.array.length)
/* 889 */         throw new NoSuchElementException();
/* 890 */       this.lastRet = this.cursor;
/* 891 */       return this.array[(this.cursor++)];
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 895 */       if (this.lastRet < 0)
/* 896 */         throw new IllegalStateException();
/* 897 */       PriorityBlockingQueue.this.removeEQ(this.array[this.lastRet]);
/* 898 */       this.lastRet = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.PriorityBlockingQueue
 * JD-Core Version:    0.6.2
 */