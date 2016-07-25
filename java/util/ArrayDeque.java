/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ public class ArrayDeque<E> extends AbstractCollection<E>
/*     */   implements Deque<E>, Cloneable, Serializable
/*     */ {
/*     */   private transient E[] elements;
/*     */   private transient int head;
/*     */   private transient int tail;
/*     */   private static final int MIN_INITIAL_CAPACITY = 8;
/*     */   private static final long serialVersionUID = 2340985798034038923L;
/*     */ 
/*     */   private void allocateElements(int paramInt)
/*     */   {
/* 125 */     int i = 8;
/*     */ 
/* 128 */     if (paramInt >= i) {
/* 129 */       i = paramInt;
/* 130 */       i |= i >>> 1;
/* 131 */       i |= i >>> 2;
/* 132 */       i |= i >>> 4;
/* 133 */       i |= i >>> 8;
/* 134 */       i |= i >>> 16;
/* 135 */       i++;
/*     */ 
/* 137 */       if (i < 0)
/* 138 */         i >>>= 1;
/*     */     }
/* 140 */     this.elements = ((Object[])new Object[i]);
/*     */   }
/*     */ 
/*     */   private void doubleCapacity()
/*     */   {
/* 148 */     assert (this.head == this.tail);
/* 149 */     int i = this.head;
/* 150 */     int j = this.elements.length;
/* 151 */     int k = j - i;
/* 152 */     int m = j << 1;
/* 153 */     if (m < 0)
/* 154 */       throw new IllegalStateException("Sorry, deque too big");
/* 155 */     Object[] arrayOfObject = new Object[m];
/* 156 */     System.arraycopy(this.elements, i, arrayOfObject, 0, k);
/* 157 */     System.arraycopy(this.elements, 0, arrayOfObject, k, i);
/* 158 */     this.elements = ((Object[])arrayOfObject);
/* 159 */     this.head = 0;
/* 160 */     this.tail = j;
/*     */   }
/*     */ 
/*     */   private <T> T[] copyElements(T[] paramArrayOfT)
/*     */   {
/* 171 */     if (this.head < this.tail) {
/* 172 */       System.arraycopy(this.elements, this.head, paramArrayOfT, 0, size());
/* 173 */     } else if (this.head > this.tail) {
/* 174 */       int i = this.elements.length - this.head;
/* 175 */       System.arraycopy(this.elements, this.head, paramArrayOfT, 0, i);
/* 176 */       System.arraycopy(this.elements, 0, paramArrayOfT, i, this.tail);
/*     */     }
/* 178 */     return paramArrayOfT;
/*     */   }
/*     */ 
/*     */   public ArrayDeque()
/*     */   {
/* 186 */     this.elements = ((Object[])new Object[16]);
/*     */   }
/*     */ 
/*     */   public ArrayDeque(int paramInt)
/*     */   {
/* 196 */     allocateElements(paramInt);
/*     */   }
/*     */ 
/*     */   public ArrayDeque(Collection<? extends E> paramCollection)
/*     */   {
/* 210 */     allocateElements(paramCollection.size());
/* 211 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public void addFirst(E paramE)
/*     */   {
/* 225 */     if (paramE == null)
/* 226 */       throw new NullPointerException();
/*     */     int tmp31_30 = (this.head - 1 & this.elements.length - 1); this.head = tmp31_30; this.elements[tmp31_30] = paramE;
/* 228 */     if (this.head == this.tail)
/* 229 */       doubleCapacity();
/*     */   }
/*     */ 
/*     */   public void addLast(E paramE)
/*     */   {
/* 241 */     if (paramE == null)
/* 242 */       throw new NullPointerException();
/* 243 */     this.elements[this.tail] = paramE;
/* 244 */     if ((this.tail = this.tail + 1 & this.elements.length - 1) == this.head)
/* 245 */       doubleCapacity();
/*     */   }
/*     */ 
/*     */   public boolean offerFirst(E paramE)
/*     */   {
/* 256 */     addFirst(paramE);
/* 257 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean offerLast(E paramE)
/*     */   {
/* 268 */     addLast(paramE);
/* 269 */     return true;
/*     */   }
/*     */ 
/*     */   public E removeFirst()
/*     */   {
/* 276 */     Object localObject = pollFirst();
/* 277 */     if (localObject == null)
/* 278 */       throw new NoSuchElementException();
/* 279 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E removeLast()
/*     */   {
/* 286 */     Object localObject = pollLast();
/* 287 */     if (localObject == null)
/* 288 */       throw new NoSuchElementException();
/* 289 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E pollFirst() {
/* 293 */     int i = this.head;
/* 294 */     Object localObject = this.elements[i];
/* 295 */     if (localObject == null)
/* 296 */       return null;
/* 297 */     this.elements[i] = null;
/* 298 */     this.head = (i + 1 & this.elements.length - 1);
/* 299 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E pollLast() {
/* 303 */     int i = this.tail - 1 & this.elements.length - 1;
/* 304 */     Object localObject = this.elements[i];
/* 305 */     if (localObject == null)
/* 306 */       return null;
/* 307 */     this.elements[i] = null;
/* 308 */     this.tail = i;
/* 309 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E getFirst()
/*     */   {
/* 316 */     Object localObject = this.elements[this.head];
/* 317 */     if (localObject == null)
/* 318 */       throw new NoSuchElementException();
/* 319 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E getLast()
/*     */   {
/* 326 */     Object localObject = this.elements[(this.tail - 1 & this.elements.length - 1)];
/* 327 */     if (localObject == null)
/* 328 */       throw new NoSuchElementException();
/* 329 */     return localObject;
/*     */   }
/*     */ 
/*     */   public E peekFirst() {
/* 333 */     return this.elements[this.head];
/*     */   }
/*     */ 
/*     */   public E peekLast() {
/* 337 */     return this.elements[(this.tail - 1 & this.elements.length - 1)];
/*     */   }
/*     */ 
/*     */   public boolean removeFirstOccurrence(Object paramObject)
/*     */   {
/* 353 */     if (paramObject == null)
/* 354 */       return false;
/* 355 */     int i = this.elements.length - 1;
/* 356 */     int j = this.head;
/*     */     Object localObject;
/* 358 */     while ((localObject = this.elements[j]) != null) {
/* 359 */       if (paramObject.equals(localObject)) {
/* 360 */         delete(j);
/* 361 */         return true;
/*     */       }
/* 363 */       j = j + 1 & i;
/*     */     }
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean removeLastOccurrence(Object paramObject)
/*     */   {
/* 381 */     if (paramObject == null)
/* 382 */       return false;
/* 383 */     int i = this.elements.length - 1;
/* 384 */     int j = this.tail - 1 & i;
/*     */     Object localObject;
/* 386 */     while ((localObject = this.elements[j]) != null) {
/* 387 */       if (paramObject.equals(localObject)) {
/* 388 */         delete(j);
/* 389 */         return true;
/*     */       }
/* 391 */       j = j - 1 & i;
/*     */     }
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 408 */     addLast(paramE);
/* 409 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 422 */     return offerLast(paramE);
/*     */   }
/*     */ 
/*     */   public E remove()
/*     */   {
/* 437 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 451 */     return pollFirst();
/*     */   }
/*     */ 
/*     */   public E element()
/*     */   {
/* 465 */     return getFirst();
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 478 */     return peekFirst();
/*     */   }
/*     */ 
/*     */   public void push(E paramE)
/*     */   {
/* 493 */     addFirst(paramE);
/*     */   }
/*     */ 
/*     */   public E pop()
/*     */   {
/* 507 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   private void checkInvariants() {
/* 511 */     assert (this.elements[this.tail] == null);
/* 512 */     assert (this.head == this.tail ? this.elements[this.head] != null : (this.elements[this.head] != null) && (this.elements[(this.tail - 1 & this.elements.length - 1)] != null));
/*     */ 
/* 515 */     assert (this.elements[(this.head - 1 & this.elements.length - 1)] == null);
/*     */   }
/*     */ 
/*     */   private boolean delete(int paramInt)
/*     */   {
/* 529 */     checkInvariants();
/* 530 */     Object[] arrayOfObject = this.elements;
/* 531 */     int i = arrayOfObject.length - 1;
/* 532 */     int j = this.head;
/* 533 */     int k = this.tail;
/* 534 */     int m = paramInt - j & i;
/* 535 */     int n = k - paramInt & i;
/*     */ 
/* 538 */     if (m >= (k - j & i)) {
/* 539 */       throw new ConcurrentModificationException();
/*     */     }
/*     */ 
/* 542 */     if (m < n) {
/* 543 */       if (j <= paramInt) {
/* 544 */         System.arraycopy(arrayOfObject, j, arrayOfObject, j + 1, m);
/*     */       } else {
/* 546 */         System.arraycopy(arrayOfObject, 0, arrayOfObject, 1, paramInt);
/* 547 */         arrayOfObject[0] = arrayOfObject[i];
/* 548 */         System.arraycopy(arrayOfObject, j, arrayOfObject, j + 1, i - j);
/*     */       }
/* 550 */       arrayOfObject[j] = null;
/* 551 */       this.head = (j + 1 & i);
/* 552 */       return false;
/*     */     }
/* 554 */     if (paramInt < k) {
/* 555 */       System.arraycopy(arrayOfObject, paramInt + 1, arrayOfObject, paramInt, n);
/* 556 */       this.tail = (k - 1);
/*     */     } else {
/* 558 */       System.arraycopy(arrayOfObject, paramInt + 1, arrayOfObject, paramInt, i - paramInt);
/* 559 */       arrayOfObject[i] = arrayOfObject[0];
/* 560 */       System.arraycopy(arrayOfObject, 1, arrayOfObject, 0, k);
/* 561 */       this.tail = (k - 1 & i);
/*     */     }
/* 563 */     return true;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 575 */     return this.tail - this.head & this.elements.length - 1;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 584 */     return this.head == this.tail;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 596 */     return new DeqIterator(null);
/*     */   }
/*     */ 
/*     */   public Iterator<E> descendingIterator() {
/* 600 */     return new DescendingIterator(null);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 694 */     if (paramObject == null)
/* 695 */       return false;
/* 696 */     int i = this.elements.length - 1;
/* 697 */     int j = this.head;
/*     */     Object localObject;
/* 699 */     while ((localObject = this.elements[j]) != null) {
/* 700 */       if (paramObject.equals(localObject))
/* 701 */         return true;
/* 702 */       j = j + 1 & i;
/*     */     }
/* 704 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 721 */     return removeFirstOccurrence(paramObject);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 729 */     int i = this.head;
/* 730 */     int j = this.tail;
/* 731 */     if (i != j) {
/* 732 */       this.head = (this.tail = 0);
/* 733 */       int k = i;
/* 734 */       int m = this.elements.length - 1;
/*     */       do {
/* 736 */         this.elements[k] = null;
/* 737 */         k = k + 1 & m;
/* 738 */       }while (k != j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 756 */     return copyElements(new Object[size()]);
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 797 */     int i = size();
/* 798 */     if (paramArrayOfT.length < i) {
/* 799 */       paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*     */     }
/* 801 */     copyElements(paramArrayOfT);
/* 802 */     if (paramArrayOfT.length > i)
/* 803 */       paramArrayOfT[i] = null;
/* 804 */     return paramArrayOfT;
/*     */   }
/*     */ 
/*     */   public ArrayDeque<E> clone()
/*     */   {
/*     */     try
/*     */     {
/* 816 */       ArrayDeque localArrayDeque = (ArrayDeque)super.clone();
/* 817 */       localArrayDeque.elements = Arrays.copyOf(this.elements, this.elements.length);
/* 818 */       return localArrayDeque;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 821 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 838 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 841 */     paramObjectOutputStream.writeInt(size());
/*     */ 
/* 844 */     int i = this.elements.length - 1;
/* 845 */     for (int j = this.head; j != this.tail; j = j + 1 & i)
/* 846 */       paramObjectOutputStream.writeObject(this.elements[j]);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 854 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 857 */     int i = paramObjectInputStream.readInt();
/* 858 */     allocateElements(i);
/* 859 */     this.head = 0;
/* 860 */     this.tail = i;
/*     */ 
/* 863 */     for (int j = 0; j < i; j++)
/* 864 */       this.elements[j] = paramObjectInputStream.readObject();
/*     */   }
/*     */ 
/*     */   private class DeqIterator
/*     */     implements Iterator<E>
/*     */   {
/* 607 */     private int cursor = ArrayDeque.this.head;
/*     */ 
/* 613 */     private int fence = ArrayDeque.this.tail;
/*     */ 
/* 619 */     private int lastRet = -1;
/*     */ 
/*     */     private DeqIterator() {  } 
/* 622 */     public boolean hasNext() { return this.cursor != this.fence; }
/*     */ 
/*     */     public E next()
/*     */     {
/* 626 */       if (this.cursor == this.fence)
/* 627 */         throw new NoSuchElementException();
/* 628 */       Object localObject = ArrayDeque.this.elements[this.cursor];
/*     */ 
/* 631 */       if ((ArrayDeque.this.tail != this.fence) || (localObject == null))
/* 632 */         throw new ConcurrentModificationException();
/* 633 */       this.lastRet = this.cursor;
/* 634 */       this.cursor = (this.cursor + 1 & ArrayDeque.this.elements.length - 1);
/* 635 */       return localObject;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 639 */       if (this.lastRet < 0)
/* 640 */         throw new IllegalStateException();
/* 641 */       if (ArrayDeque.this.delete(this.lastRet)) {
/* 642 */         this.cursor = (this.cursor - 1 & ArrayDeque.this.elements.length - 1);
/* 643 */         this.fence = ArrayDeque.this.tail;
/*     */       }
/* 645 */       this.lastRet = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class DescendingIterator
/*     */     implements Iterator<E>
/*     */   {
/* 655 */     private int cursor = ArrayDeque.this.tail;
/* 656 */     private int fence = ArrayDeque.this.head;
/* 657 */     private int lastRet = -1;
/*     */ 
/*     */     private DescendingIterator() {  } 
/* 660 */     public boolean hasNext() { return this.cursor != this.fence; }
/*     */ 
/*     */     public E next()
/*     */     {
/* 664 */       if (this.cursor == this.fence)
/* 665 */         throw new NoSuchElementException();
/* 666 */       this.cursor = (this.cursor - 1 & ArrayDeque.this.elements.length - 1);
/* 667 */       Object localObject = ArrayDeque.this.elements[this.cursor];
/* 668 */       if ((ArrayDeque.this.head != this.fence) || (localObject == null))
/* 669 */         throw new ConcurrentModificationException();
/* 670 */       this.lastRet = this.cursor;
/* 671 */       return localObject;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 675 */       if (this.lastRet < 0)
/* 676 */         throw new IllegalStateException();
/* 677 */       if (!ArrayDeque.this.delete(this.lastRet)) {
/* 678 */         this.cursor = (this.cursor + 1 & ArrayDeque.this.elements.length - 1);
/* 679 */         this.fence = ArrayDeque.this.head;
/*     */       }
/* 681 */       this.lastRet = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ArrayDeque
 * JD-Core Version:    0.6.2
 */