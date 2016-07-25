/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class PriorityQueue<E> extends AbstractQueue<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7720805057305804111L;
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 11;
/*     */   private transient Object[] queue;
/* 100 */   private int size = 0;
/*     */   private final Comparator<? super E> comparator;
/* 112 */   private transient int modCount = 0;
/*     */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*     */ 
/*     */   public PriorityQueue()
/*     */   {
/* 120 */     this(11, null);
/*     */   }
/*     */ 
/*     */   public PriorityQueue(int paramInt)
/*     */   {
/* 133 */     this(paramInt, null);
/*     */   }
/*     */ 
/*     */   public PriorityQueue(int paramInt, Comparator<? super E> paramComparator)
/*     */   {
/* 151 */     if (paramInt < 1)
/* 152 */       throw new IllegalArgumentException();
/* 153 */     this.queue = new Object[paramInt];
/* 154 */     this.comparator = paramComparator;
/*     */   }
/*     */ 
/*     */   public PriorityQueue(Collection<? extends E> paramCollection)
/*     */   {
/*     */     Object localObject;
/* 175 */     if ((paramCollection instanceof SortedSet)) {
/* 176 */       localObject = (SortedSet)paramCollection;
/* 177 */       this.comparator = ((SortedSet)localObject).comparator();
/* 178 */       initElementsFromCollection((Collection)localObject);
/*     */     }
/* 180 */     else if ((paramCollection instanceof PriorityQueue)) {
/* 181 */       localObject = (PriorityQueue)paramCollection;
/* 182 */       this.comparator = ((PriorityQueue)localObject).comparator();
/* 183 */       initFromPriorityQueue((PriorityQueue)localObject);
/*     */     }
/*     */     else {
/* 186 */       this.comparator = null;
/* 187 */       initFromCollection(paramCollection);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PriorityQueue(PriorityQueue<? extends E> paramPriorityQueue)
/*     */   {
/* 207 */     this.comparator = paramPriorityQueue.comparator();
/* 208 */     initFromPriorityQueue(paramPriorityQueue);
/*     */   }
/*     */ 
/*     */   public PriorityQueue(SortedSet<? extends E> paramSortedSet)
/*     */   {
/* 226 */     this.comparator = paramSortedSet.comparator();
/* 227 */     initElementsFromCollection(paramSortedSet);
/*     */   }
/*     */ 
/*     */   private void initFromPriorityQueue(PriorityQueue<? extends E> paramPriorityQueue) {
/* 231 */     if (paramPriorityQueue.getClass() == PriorityQueue.class) {
/* 232 */       this.queue = paramPriorityQueue.toArray();
/* 233 */       this.size = paramPriorityQueue.size();
/*     */     } else {
/* 235 */       initFromCollection(paramPriorityQueue);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initElementsFromCollection(Collection<? extends E> paramCollection) {
/* 240 */     Object[] arrayOfObject = paramCollection.toArray();
/*     */ 
/* 242 */     if (arrayOfObject.getClass() != [Ljava.lang.Object.class)
/* 243 */       arrayOfObject = Arrays.copyOf(arrayOfObject, arrayOfObject.length, [Ljava.lang.Object.class);
/* 244 */     int i = arrayOfObject.length;
/* 245 */     if ((i == 1) || (this.comparator != null))
/* 246 */       for (int j = 0; j < i; j++)
/* 247 */         if (arrayOfObject[j] == null)
/* 248 */           throw new NullPointerException();
/* 249 */     this.queue = arrayOfObject;
/* 250 */     this.size = arrayOfObject.length;
/*     */   }
/*     */ 
/*     */   private void initFromCollection(Collection<? extends E> paramCollection)
/*     */   {
/* 259 */     initElementsFromCollection(paramCollection);
/* 260 */     heapify();
/*     */   }
/*     */ 
/*     */   private void grow(int paramInt)
/*     */   {
/* 277 */     int i = this.queue.length;
/*     */ 
/* 279 */     int j = i + (i < 64 ? i + 2 : i >> 1);
/*     */ 
/* 283 */     if (j - 2147483639 > 0)
/* 284 */       j = hugeCapacity(paramInt);
/* 285 */     this.queue = Arrays.copyOf(this.queue, j);
/*     */   }
/*     */ 
/*     */   private static int hugeCapacity(int paramInt) {
/* 289 */     if (paramInt < 0)
/* 290 */       throw new OutOfMemoryError();
/* 291 */     return paramInt > 2147483639 ? 2147483647 : 2147483639;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 306 */     return offer(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 319 */     if (paramE == null)
/* 320 */       throw new NullPointerException();
/* 321 */     this.modCount += 1;
/* 322 */     int i = this.size;
/* 323 */     if (i >= this.queue.length)
/* 324 */       grow(i + 1);
/* 325 */     this.size = (i + 1);
/* 326 */     if (i == 0)
/* 327 */       this.queue[0] = paramE;
/*     */     else
/* 329 */       siftUp(i, paramE);
/* 330 */     return true;
/*     */   }
/*     */ 
/*     */   public E peek() {
/* 334 */     if (this.size == 0)
/* 335 */       return null;
/* 336 */     return this.queue[0];
/*     */   }
/*     */ 
/*     */   private int indexOf(Object paramObject) {
/* 340 */     if (paramObject != null) {
/* 341 */       for (int i = 0; i < this.size; i++)
/* 342 */         if (paramObject.equals(this.queue[i]))
/* 343 */           return i;
/*     */     }
/* 345 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 360 */     int i = indexOf(paramObject);
/* 361 */     if (i == -1) {
/* 362 */       return false;
/*     */     }
/* 364 */     removeAt(i);
/* 365 */     return true;
/*     */   }
/*     */ 
/*     */   boolean removeEq(Object paramObject)
/*     */   {
/* 377 */     for (int i = 0; i < this.size; i++) {
/* 378 */       if (paramObject == this.queue[i]) {
/* 379 */         removeAt(i);
/* 380 */         return true;
/*     */       }
/*     */     }
/* 383 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 395 */     return indexOf(paramObject) != -1;
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 412 */     return Arrays.copyOf(this.queue, this.size);
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 453 */     if (paramArrayOfT.length < this.size)
/*     */     {
/* 455 */       return (Object[])Arrays.copyOf(this.queue, this.size, paramArrayOfT.getClass());
/* 456 */     }System.arraycopy(this.queue, 0, paramArrayOfT, 0, this.size);
/* 457 */     if (paramArrayOfT.length > this.size)
/* 458 */       paramArrayOfT[this.size] = null;
/* 459 */     return paramArrayOfT;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 469 */     return new Itr(null);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 555 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 563 */     this.modCount += 1;
/* 564 */     for (int i = 0; i < this.size; i++)
/* 565 */       this.queue[i] = null;
/* 566 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public E poll() {
/* 570 */     if (this.size == 0)
/* 571 */       return null;
/* 572 */     int i = --this.size;
/* 573 */     this.modCount += 1;
/* 574 */     Object localObject1 = this.queue[0];
/* 575 */     Object localObject2 = this.queue[i];
/* 576 */     this.queue[i] = null;
/* 577 */     if (i != 0)
/* 578 */       siftDown(0, localObject2);
/* 579 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private E removeAt(int paramInt)
/*     */   {
/* 595 */     assert ((paramInt >= 0) && (paramInt < this.size));
/* 596 */     this.modCount += 1;
/* 597 */     int i = --this.size;
/* 598 */     if (i == paramInt) {
/* 599 */       this.queue[paramInt] = null;
/*     */     } else {
/* 601 */       Object localObject = this.queue[i];
/* 602 */       this.queue[i] = null;
/* 603 */       siftDown(paramInt, localObject);
/* 604 */       if (this.queue[paramInt] == localObject) {
/* 605 */         siftUp(paramInt, localObject);
/* 606 */         if (this.queue[paramInt] != localObject)
/* 607 */           return localObject;
/*     */       }
/*     */     }
/* 610 */     return null;
/*     */   }
/*     */ 
/*     */   private void siftUp(int paramInt, E paramE)
/*     */   {
/* 626 */     if (this.comparator != null)
/* 627 */       siftUpUsingComparator(paramInt, paramE);
/*     */     else
/* 629 */       siftUpComparable(paramInt, paramE);
/*     */   }
/*     */ 
/*     */   private void siftUpComparable(int paramInt, E paramE) {
/* 633 */     Comparable localComparable = (Comparable)paramE;
/* 634 */     while (paramInt > 0) {
/* 635 */       int i = paramInt - 1 >>> 1;
/* 636 */       Object localObject = this.queue[i];
/* 637 */       if (localComparable.compareTo(localObject) >= 0)
/*     */         break;
/* 639 */       this.queue[paramInt] = localObject;
/* 640 */       paramInt = i;
/*     */     }
/* 642 */     this.queue[paramInt] = localComparable;
/*     */   }
/*     */ 
/*     */   private void siftUpUsingComparator(int paramInt, E paramE) {
/* 646 */     while (paramInt > 0) {
/* 647 */       int i = paramInt - 1 >>> 1;
/* 648 */       Object localObject = this.queue[i];
/* 649 */       if (this.comparator.compare(paramE, localObject) >= 0)
/*     */         break;
/* 651 */       this.queue[paramInt] = localObject;
/* 652 */       paramInt = i;
/*     */     }
/* 654 */     this.queue[paramInt] = paramE;
/*     */   }
/*     */ 
/*     */   private void siftDown(int paramInt, E paramE)
/*     */   {
/* 666 */     if (this.comparator != null)
/* 667 */       siftDownUsingComparator(paramInt, paramE);
/*     */     else
/* 669 */       siftDownComparable(paramInt, paramE);
/*     */   }
/*     */ 
/*     */   private void siftDownComparable(int paramInt, E paramE) {
/* 673 */     Comparable localComparable = (Comparable)paramE;
/* 674 */     int i = this.size >>> 1;
/* 675 */     while (paramInt < i) {
/* 676 */       int j = (paramInt << 1) + 1;
/* 677 */       Object localObject = this.queue[j];
/* 678 */       int k = j + 1;
/* 679 */       if ((k < this.size) && (((Comparable)localObject).compareTo(this.queue[k]) > 0))
/*     */       {
/* 681 */         localObject = this.queue[(j = k)];
/* 682 */       }if (localComparable.compareTo(localObject) <= 0)
/*     */         break;
/* 684 */       this.queue[paramInt] = localObject;
/* 685 */       paramInt = j;
/*     */     }
/* 687 */     this.queue[paramInt] = localComparable;
/*     */   }
/*     */ 
/*     */   private void siftDownUsingComparator(int paramInt, E paramE) {
/* 691 */     int i = this.size >>> 1;
/* 692 */     while (paramInt < i) {
/* 693 */       int j = (paramInt << 1) + 1;
/* 694 */       Object localObject = this.queue[j];
/* 695 */       int k = j + 1;
/* 696 */       if ((k < this.size) && (this.comparator.compare(localObject, this.queue[k]) > 0))
/*     */       {
/* 698 */         localObject = this.queue[(j = k)];
/* 699 */       }if (this.comparator.compare(paramE, localObject) <= 0)
/*     */         break;
/* 701 */       this.queue[paramInt] = localObject;
/* 702 */       paramInt = j;
/*     */     }
/* 704 */     this.queue[paramInt] = paramE;
/*     */   }
/*     */ 
/*     */   private void heapify()
/*     */   {
/* 712 */     for (int i = (this.size >>> 1) - 1; i >= 0; i--)
/* 713 */       siftDown(i, this.queue[i]);
/*     */   }
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 726 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 741 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 744 */     paramObjectOutputStream.writeInt(Math.max(2, this.size + 1));
/*     */ 
/* 747 */     for (int i = 0; i < this.size; i++)
/* 748 */       paramObjectOutputStream.writeObject(this.queue[i]);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 760 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 763 */     paramObjectInputStream.readInt();
/*     */ 
/* 765 */     this.queue = new Object[this.size];
/*     */ 
/* 768 */     for (int i = 0; i < this.size; i++) {
/* 769 */       this.queue[i] = paramObjectInputStream.readObject();
/*     */     }
/*     */ 
/* 773 */     heapify();
/*     */   }
/*     */ 
/*     */   private final class Itr
/*     */     implements Iterator<E>
/*     */   {
/* 477 */     private int cursor = 0;
/*     */ 
/* 484 */     private int lastRet = -1;
/*     */ 
/* 497 */     private ArrayDeque<E> forgetMeNot = null;
/*     */ 
/* 503 */     private E lastRetElt = null;
/*     */ 
/* 510 */     private int expectedModCount = PriorityQueue.this.modCount;
/*     */ 
/*     */     private Itr() {  } 
/* 513 */     public boolean hasNext() { return (this.cursor < PriorityQueue.this.size) || ((this.forgetMeNot != null) && (!this.forgetMeNot.isEmpty())); }
/*     */ 
/*     */ 
/*     */     public E next()
/*     */     {
/* 518 */       if (this.expectedModCount != PriorityQueue.this.modCount)
/* 519 */         throw new ConcurrentModificationException();
/* 520 */       if (this.cursor < PriorityQueue.this.size)
/* 521 */         return PriorityQueue.this.queue[(this.lastRet = this.cursor++)];
/* 522 */       if (this.forgetMeNot != null) {
/* 523 */         this.lastRet = -1;
/* 524 */         this.lastRetElt = this.forgetMeNot.poll();
/* 525 */         if (this.lastRetElt != null)
/* 526 */           return this.lastRetElt;
/*     */       }
/* 528 */       throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 532 */       if (this.expectedModCount != PriorityQueue.this.modCount)
/* 533 */         throw new ConcurrentModificationException();
/* 534 */       if (this.lastRet != -1) {
/* 535 */         Object localObject = PriorityQueue.this.removeAt(this.lastRet);
/* 536 */         this.lastRet = -1;
/* 537 */         if (localObject == null) {
/* 538 */           this.cursor -= 1;
/*     */         } else {
/* 540 */           if (this.forgetMeNot == null)
/* 541 */             this.forgetMeNot = new ArrayDeque();
/* 542 */           this.forgetMeNot.add(localObject);
/*     */         }
/* 544 */       } else if (this.lastRetElt != null) {
/* 545 */         PriorityQueue.this.removeEq(this.lastRetElt);
/* 546 */         this.lastRetElt = null;
/*     */       } else {
/* 548 */         throw new IllegalStateException();
/*     */       }
/* 550 */       this.expectedModCount = PriorityQueue.this.modCount;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.PriorityQueue
 * JD-Core Version:    0.6.2
 */