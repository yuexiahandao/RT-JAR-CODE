/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ public class LinkedBlockingQueue<E> extends AbstractQueue<E>
/*     */   implements BlockingQueue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6903933977591709194L;
/*     */   private final int capacity;
/* 138 */   private final AtomicInteger count = new AtomicInteger(0);
/*     */   private transient Node<E> head;
/*     */   private transient Node<E> last;
/* 153 */   private final ReentrantLock takeLock = new ReentrantLock();
/*     */ 
/* 156 */   private final Condition notEmpty = this.takeLock.newCondition();
/*     */ 
/* 159 */   private final ReentrantLock putLock = new ReentrantLock();
/*     */ 
/* 162 */   private final Condition notFull = this.putLock.newCondition();
/*     */ 
/*     */   private void signalNotEmpty()
/*     */   {
/* 169 */     ReentrantLock localReentrantLock = this.takeLock;
/* 170 */     localReentrantLock.lock();
/*     */     try {
/* 172 */       this.notEmpty.signal();
/*     */     } finally {
/* 174 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void signalNotFull()
/*     */   {
/* 182 */     ReentrantLock localReentrantLock = this.putLock;
/* 183 */     localReentrantLock.lock();
/*     */     try {
/* 185 */       this.notFull.signal();
/*     */     } finally {
/* 187 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void enqueue(Node<E> paramNode)
/*     */   {
/* 199 */     this.last = (this.last.next = paramNode);
/*     */   }
/*     */ 
/*     */   private E dequeue()
/*     */   {
/* 210 */     Node localNode1 = this.head;
/* 211 */     Node localNode2 = localNode1.next;
/* 212 */     localNode1.next = localNode1;
/* 213 */     this.head = localNode2;
/* 214 */     Object localObject = localNode2.item;
/* 215 */     localNode2.item = null;
/* 216 */     return localObject;
/*     */   }
/*     */ 
/*     */   void fullyLock()
/*     */   {
/* 223 */     this.putLock.lock();
/* 224 */     this.takeLock.lock();
/*     */   }
/*     */ 
/*     */   void fullyUnlock()
/*     */   {
/* 231 */     this.takeLock.unlock();
/* 232 */     this.putLock.unlock();
/*     */   }
/*     */ 
/*     */   public LinkedBlockingQueue()
/*     */   {
/* 248 */     this(2147483647);
/*     */   }
/*     */ 
/*     */   public LinkedBlockingQueue(int paramInt)
/*     */   {
/* 259 */     if (paramInt <= 0) throw new IllegalArgumentException();
/* 260 */     this.capacity = paramInt;
/* 261 */     this.last = (this.head = new Node(null));
/*     */   }
/*     */ 
/*     */   public LinkedBlockingQueue(Collection<? extends E> paramCollection)
/*     */   {
/* 275 */     this(2147483647);
/* 276 */     ReentrantLock localReentrantLock = this.putLock;
/* 277 */     localReentrantLock.lock();
/*     */     try {
/* 279 */       int i = 0;
/* 280 */       for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject1 = localIterator.next();
/* 281 */         if (localObject1 == null)
/* 282 */           throw new NullPointerException();
/* 283 */         if (i == this.capacity)
/* 284 */           throw new IllegalStateException("Queue full");
/* 285 */         enqueue(new Node(localObject1));
/* 286 */         i++;
/*     */       }
/* 288 */       this.count.set(i);
/*     */     } finally {
/* 290 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 303 */     return this.count.get();
/*     */   }
/*     */ 
/*     */   public int remainingCapacity()
/*     */   {
/* 320 */     return this.capacity - this.count.get();
/*     */   }
/*     */ 
/*     */   public void put(E paramE)
/*     */     throws InterruptedException
/*     */   {
/* 331 */     if (paramE == null) throw new NullPointerException();
/*     */ 
/* 334 */     int i = -1;
/* 335 */     Node localNode = new Node(paramE);
/* 336 */     ReentrantLock localReentrantLock = this.putLock;
/* 337 */     AtomicInteger localAtomicInteger = this.count;
/* 338 */     localReentrantLock.lockInterruptibly();
/*     */     try
/*     */     {
/* 348 */       while (localAtomicInteger.get() == this.capacity) {
/* 349 */         this.notFull.await();
/*     */       }
/* 351 */       enqueue(localNode);
/* 352 */       i = localAtomicInteger.getAndIncrement();
/* 353 */       if (i + 1 < this.capacity)
/* 354 */         this.notFull.signal();
/*     */     } finally {
/* 356 */       localReentrantLock.unlock();
/*     */     }
/* 358 */     if (i == 0)
/* 359 */       signalNotEmpty();
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 374 */     if (paramE == null) throw new NullPointerException();
/* 375 */     long l = paramTimeUnit.toNanos(paramLong);
/* 376 */     int i = -1;
/* 377 */     ReentrantLock localReentrantLock = this.putLock;
/* 378 */     AtomicInteger localAtomicInteger = this.count;
/* 379 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/* 381 */       while (localAtomicInteger.get() == this.capacity) {
/* 382 */         if (l <= 0L)
/* 383 */           return false;
/* 384 */         l = this.notFull.awaitNanos(l);
/*     */       }
/* 386 */       enqueue(new Node(paramE));
/* 387 */       i = localAtomicInteger.getAndIncrement();
/* 388 */       if (i + 1 < this.capacity)
/* 389 */         this.notFull.signal();
/*     */     } finally {
/* 391 */       localReentrantLock.unlock();
/*     */     }
/* 393 */     if (i == 0)
/* 394 */       signalNotEmpty();
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 410 */     if (paramE == null) throw new NullPointerException();
/* 411 */     AtomicInteger localAtomicInteger = this.count;
/* 412 */     if (localAtomicInteger.get() == this.capacity)
/* 413 */       return false;
/* 414 */     int i = -1;
/* 415 */     Node localNode = new Node(paramE);
/* 416 */     ReentrantLock localReentrantLock = this.putLock;
/* 417 */     localReentrantLock.lock();
/*     */     try {
/* 419 */       if (localAtomicInteger.get() < this.capacity) {
/* 420 */         enqueue(localNode);
/* 421 */         i = localAtomicInteger.getAndIncrement();
/* 422 */         if (i + 1 < this.capacity)
/* 423 */           this.notFull.signal();
/*     */       }
/*     */     } finally {
/* 426 */       localReentrantLock.unlock();
/*     */     }
/* 428 */     if (i == 0)
/* 429 */       signalNotEmpty();
/* 430 */     return i >= 0;
/*     */   }
/* 436 */   public E take() throws InterruptedException { int i = -1;
/* 437 */     AtomicInteger localAtomicInteger = this.count;
/* 438 */     ReentrantLock localReentrantLock = this.takeLock;
/* 439 */     localReentrantLock.lockInterruptibly();
/*     */     Object localObject1;
/*     */     try { while (localAtomicInteger.get() == 0) {
/* 442 */         this.notEmpty.await();
/*     */       }
/* 444 */       localObject1 = dequeue();
/* 445 */       i = localAtomicInteger.getAndDecrement();
/* 446 */       if (i > 1)
/* 447 */         this.notEmpty.signal();
/*     */     } finally {
/* 449 */       localReentrantLock.unlock();
/*     */     }
/* 451 */     if (i == this.capacity)
/* 452 */       signalNotFull();
/* 453 */     return localObject1; }
/*     */ 
/*     */   public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*     */   {
/* 457 */     Object localObject1 = null;
/* 458 */     int i = -1;
/* 459 */     long l = paramTimeUnit.toNanos(paramLong);
/* 460 */     AtomicInteger localAtomicInteger = this.count;
/* 461 */     ReentrantLock localReentrantLock = this.takeLock;
/* 462 */     localReentrantLock.lockInterruptibly();
/*     */     try {
/* 464 */       while (localAtomicInteger.get() == 0) {
/* 465 */         if (l <= 0L)
/* 466 */           return null;
/* 467 */         l = this.notEmpty.awaitNanos(l);
/*     */       }
/* 469 */       localObject1 = dequeue();
/* 470 */       i = localAtomicInteger.getAndDecrement();
/* 471 */       if (i > 1)
/* 472 */         this.notEmpty.signal();
/*     */     } finally {
/* 474 */       localReentrantLock.unlock();
/*     */     }
/* 476 */     if (i == this.capacity)
/* 477 */       signalNotFull();
/* 478 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public E poll() {
/* 482 */     AtomicInteger localAtomicInteger = this.count;
/* 483 */     if (localAtomicInteger.get() == 0)
/* 484 */       return null;
/* 485 */     Object localObject1 = null;
/* 486 */     int i = -1;
/* 487 */     ReentrantLock localReentrantLock = this.takeLock;
/* 488 */     localReentrantLock.lock();
/*     */     try {
/* 490 */       if (localAtomicInteger.get() > 0) {
/* 491 */         localObject1 = dequeue();
/* 492 */         i = localAtomicInteger.getAndDecrement();
/* 493 */         if (i > 1)
/* 494 */           this.notEmpty.signal();
/*     */       }
/*     */     } finally {
/* 497 */       localReentrantLock.unlock();
/*     */     }
/* 499 */     if (i == this.capacity)
/* 500 */       signalNotFull();
/* 501 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public E peek() {
/* 505 */     if (this.count.get() == 0)
/* 506 */       return null;
/* 507 */     ReentrantLock localReentrantLock = this.takeLock;
/* 508 */     localReentrantLock.lock();
/*     */     try {
/* 510 */       Node localNode = this.head.next;
/*     */       Object localObject1;
/* 511 */       if (localNode == null) {
/* 512 */         return null;
/*     */       }
/* 514 */       return localNode.item;
/*     */     } finally {
/* 516 */       localReentrantLock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   void unlink(Node<E> paramNode1, Node<E> paramNode2)
/*     */   {
/* 527 */     paramNode1.item = null;
/* 528 */     paramNode2.next = paramNode1.next;
/* 529 */     if (this.last == paramNode1)
/* 530 */       this.last = paramNode2;
/* 531 */     if (this.count.getAndDecrement() == this.capacity)
/* 532 */       this.notFull.signal();
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 547 */     if (paramObject == null) return false;
/* 548 */     fullyLock();
/*     */     try {
/* 550 */       Object localObject1 = this.head; for (Node localNode = ((Node)localObject1).next; 
/* 551 */         localNode != null; 
/* 552 */         localNode = localNode.next) {
/* 553 */         if (paramObject.equals(localNode.item)) {
/* 554 */           unlink(localNode, (Node)localObject1);
/* 555 */           return true;
/*     */         }
/* 552 */         localObject1 = localNode;
/*     */       }
/*     */ 
/* 558 */       return false;
/*     */     } finally {
/* 560 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 573 */     if (paramObject == null) return false;
/* 574 */     fullyLock();
/*     */     try {
/* 576 */       for (Node localNode = this.head.next; localNode != null; localNode = localNode.next)
/* 577 */         if (paramObject.equals(localNode.item))
/* 578 */           return true;
/* 579 */       return false;
/*     */     } finally {
/* 581 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 599 */     fullyLock();
/*     */     try {
/* 601 */       int i = this.count.get();
/* 602 */       Object[] arrayOfObject = new Object[i];
/* 603 */       int j = 0;
/* 604 */       for (Object localObject1 = this.head.next; localObject1 != null; localObject1 = ((Node)localObject1).next)
/* 605 */         arrayOfObject[(j++)] = ((Node)localObject1).item;
/* 606 */       return arrayOfObject;
/*     */     } finally {
/* 608 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 650 */     fullyLock();
/*     */     try {
/* 652 */       int i = this.count.get();
/* 653 */       if (paramArrayOfT.length < i) {
/* 654 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*     */       }
/*     */ 
/* 657 */       int j = 0;
/* 658 */       for (Object localObject1 = this.head.next; localObject1 != null; localObject1 = ((Node)localObject1).next)
/* 659 */         paramArrayOfT[(j++)] = ((Node)localObject1).item;
/* 660 */       if (paramArrayOfT.length > j)
/* 661 */         paramArrayOfT[j] = null;
/* 662 */       return paramArrayOfT;
/*     */     } finally {
/* 664 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 669 */     fullyLock();
/*     */     try {
/* 671 */       Node localNode = this.head.next;
/* 672 */       if (localNode == null) {
/* 673 */         return "[]";
/*     */       }
/* 675 */       Object localObject1 = new StringBuilder();
/* 676 */       ((StringBuilder)localObject1).append('[');
/*     */       while (true) {
/* 678 */         Object localObject2 = localNode.item;
/* 679 */         ((StringBuilder)localObject1).append(localObject2 == this ? "(this Collection)" : localObject2);
/* 680 */         localNode = localNode.next;
/* 681 */         if (localNode == null)
/* 682 */           return ']';
/* 683 */         ((StringBuilder)localObject1).append(',').append(' ');
/*     */       }
/*     */     } finally {
/* 686 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 695 */     fullyLock();
/*     */     try
/*     */     {
/*     */       Node localNode;
/* 697 */       for (Object localObject1 = this.head; (localNode = ((Node)localObject1).next) != null; localObject1 = localNode) {
/* 698 */         ((Node)localObject1).next = ((Node)localObject1);
/* 699 */         localNode.item = null;
/*     */       }
/* 701 */       this.head = this.last;
/*     */ 
/* 703 */       if (this.count.getAndSet(0) == this.capacity)
/* 704 */         this.notFull.signal();
/*     */     } finally {
/* 706 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection)
/*     */   {
/* 717 */     return drainTo(paramCollection, 2147483647);
/*     */   }
/*     */ 
/*     */   public int drainTo(Collection<? super E> paramCollection, int paramInt)
/*     */   {
/* 727 */     if (paramCollection == null)
/* 728 */       throw new NullPointerException();
/* 729 */     if (paramCollection == this)
/* 730 */       throw new IllegalArgumentException();
/* 731 */     int i = 0;
/* 732 */     ReentrantLock localReentrantLock = this.takeLock;
/* 733 */     localReentrantLock.lock();
/*     */     try {
/* 735 */       Node localNode1 = Math.min(paramInt, this.count.get());
/*     */ 
/* 737 */       Object localObject1 = this.head;
/* 738 */       int j = 0;
/*     */       try {
/* 740 */         while (j < localNode1) {
/* 741 */           localNode2 = ((Node)localObject1).next;
/* 742 */           paramCollection.add(localNode2.item);
/* 743 */           localNode2.item = null;
/* 744 */           ((Node)localObject1).next = ((Node)localObject1);
/* 745 */           localObject1 = localNode2;
/* 746 */           j++;
/*     */         }
/* 748 */         Node localNode2 = localNode1;
/*     */ 
/* 751 */         if (j > 0)
/*     */         {
/* 753 */           this.head = ((Node)localObject1);
/* 754 */           i = this.count.getAndAdd(-j) == this.capacity ? 1 : 0;
/*     */         }
/*     */ 
/* 760 */         return localNode2;
/*     */       }
/*     */       finally
/*     */       {
/* 751 */         if (j > 0)
/*     */         {
/* 753 */           this.head = ((Node)localObject1);
/* 754 */           i = this.count.getAndAdd(-j) == this.capacity ? 1 : 0;
/*     */         }
/*     */       }
/*     */     } finally {
/* 758 */       localReentrantLock.unlock();
/* 759 */       if (i != 0)
/* 760 */         signalNotFull();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 778 */     return new Itr();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 871 */     fullyLock();
/*     */     try
/*     */     {
/* 874 */       paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 877 */       for (Node localNode = this.head.next; localNode != null; localNode = localNode.next) {
/* 878 */         paramObjectOutputStream.writeObject(localNode.item);
/*     */       }
/*     */ 
/* 881 */       paramObjectOutputStream.writeObject(null);
/*     */     } finally {
/* 883 */       fullyUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 896 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 898 */     this.count.set(0);
/* 899 */     this.last = (this.head = new Node(null));
/*     */     while (true)
/*     */     {
/* 904 */       Object localObject = paramObjectInputStream.readObject();
/* 905 */       if (localObject == null)
/*     */         break;
/* 907 */       add(localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/*     */     private LinkedBlockingQueue.Node<E> current;
/*     */     private LinkedBlockingQueue.Node<E> lastRet;
/*     */     private E currentElement;
/*     */ 
/*     */     Itr()
/*     */     {
/* 792 */       LinkedBlockingQueue.this.fullyLock();
/*     */       try {
/* 794 */         this.current = LinkedBlockingQueue.this.head.next;
/* 795 */         if (this.current != null)
/* 796 */           this.currentElement = this.current.item;
/*     */       } finally {
/* 798 */         LinkedBlockingQueue.this.fullyUnlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 803 */       return this.current != null;
/*     */     }
/*     */ 
/*     */     private LinkedBlockingQueue.Node<E> nextNode(LinkedBlockingQueue.Node<E> paramNode)
/*     */     {
/*     */       while (true)
/*     */       {
/* 815 */         LinkedBlockingQueue.Node localNode = paramNode.next;
/* 816 */         if (localNode == paramNode)
/* 817 */           return LinkedBlockingQueue.this.head.next;
/* 818 */         if ((localNode == null) || (localNode.item != null))
/* 819 */           return localNode;
/* 820 */         paramNode = localNode;
/*     */       }
/*     */     }
/*     */ 
/*     */     public E next() {
/* 825 */       LinkedBlockingQueue.this.fullyLock();
/*     */       try {
/* 827 */         if (this.current == null)
/* 828 */           throw new NoSuchElementException();
/* 829 */         Object localObject1 = this.currentElement;
/* 830 */         this.lastRet = this.current;
/* 831 */         this.current = nextNode(this.current);
/* 832 */         this.currentElement = (this.current == null ? null : this.current.item);
/* 833 */         return localObject1;
/*     */       } finally {
/* 835 */         LinkedBlockingQueue.this.fullyUnlock();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 840 */       if (this.lastRet == null)
/* 841 */         throw new IllegalStateException();
/* 842 */       LinkedBlockingQueue.this.fullyLock();
/*     */       try {
/* 844 */         LinkedBlockingQueue.Node localNode1 = this.lastRet;
/* 845 */         this.lastRet = null;
/* 846 */         Object localObject1 = LinkedBlockingQueue.this.head; for (LinkedBlockingQueue.Node localNode2 = ((LinkedBlockingQueue.Node)localObject1).next; 
/* 847 */           localNode2 != null; 
/* 848 */           localNode2 = localNode2.next) {
/* 849 */           if (localNode2 == localNode1) {
/* 850 */             LinkedBlockingQueue.this.unlink(localNode2, (LinkedBlockingQueue.Node)localObject1);
/* 851 */             break;
/*     */           }
/* 848 */           localObject1 = localNode2;
/*     */         }
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/* 855 */         LinkedBlockingQueue.this.fullyUnlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Node<E>
/*     */   {
/*     */     E item;
/*     */     Node<E> next;
/*     */ 
/*     */     Node(E paramE)
/*     */     {
/* 131 */       this.item = paramE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.LinkedBlockingQueue
 * JD-Core Version:    0.6.2
 */