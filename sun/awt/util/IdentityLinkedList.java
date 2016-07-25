/*     */ package sun.awt.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class IdentityLinkedList<E> extends AbstractSequentialList<E>
/*     */   implements List<E>, Deque<E>
/*     */ {
/*  93 */   private transient Entry<E> header = new Entry(null, null, null);
/*  94 */   private transient int size = 0;
/*     */ 
/*     */   public IdentityLinkedList()
/*     */   {
/* 100 */     this.header.next = (this.header.previous = this.header);
/*     */   }
/*     */ 
/*     */   public IdentityLinkedList(Collection<? extends E> paramCollection)
/*     */   {
/* 112 */     this();
/* 113 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public E getFirst()
/*     */   {
/* 123 */     if (this.size == 0) {
/* 124 */       throw new NoSuchElementException();
/*     */     }
/* 126 */     return this.header.next.element;
/*     */   }
/*     */ 
/*     */   public E getLast()
/*     */   {
/* 136 */     if (this.size == 0) {
/* 137 */       throw new NoSuchElementException();
/*     */     }
/* 139 */     return this.header.previous.element;
/*     */   }
/*     */ 
/*     */   public E removeFirst()
/*     */   {
/* 149 */     return remove(this.header.next);
/*     */   }
/*     */ 
/*     */   public E removeLast()
/*     */   {
/* 159 */     return remove(this.header.previous);
/*     */   }
/*     */ 
/*     */   public void addFirst(E paramE)
/*     */   {
/* 168 */     addBefore(paramE, this.header.next);
/*     */   }
/*     */ 
/*     */   public void addLast(E paramE)
/*     */   {
/* 179 */     addBefore(paramE, this.header);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 192 */     return indexOf(paramObject) != -1;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 201 */     return this.size;
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 213 */     addBefore(paramE, this.header);
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 230 */     for (Entry localEntry = this.header.next; localEntry != this.header; localEntry = localEntry.next) {
/* 231 */       if (paramObject == localEntry.element) {
/* 232 */         remove(localEntry);
/* 233 */         return true;
/*     */       }
/*     */     }
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection)
/*     */   {
/* 252 */     return addAll(this.size, paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*     */   {
/* 271 */     if ((paramInt < 0) || (paramInt > this.size)) {
/* 272 */       throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + this.size);
/*     */     }
/* 274 */     Object[] arrayOfObject = paramCollection.toArray();
/* 275 */     int i = arrayOfObject.length;
/* 276 */     if (i == 0)
/* 277 */       return false;
/* 278 */     this.modCount += 1;
/*     */ 
/* 280 */     Entry localEntry1 = paramInt == this.size ? this.header : entry(paramInt);
/* 281 */     Object localObject = localEntry1.previous;
/* 282 */     for (int j = 0; j < i; j++) {
/* 283 */       Entry localEntry2 = new Entry(arrayOfObject[j], localEntry1, (Entry)localObject);
/* 284 */       ((Entry)localObject).next = localEntry2;
/* 285 */       localObject = localEntry2;
/*     */     }
/* 287 */     localEntry1.previous = ((Entry)localObject);
/*     */ 
/* 289 */     this.size += i;
/* 290 */     return true;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 297 */     Object localObject = this.header.next;
/* 298 */     while (localObject != this.header) {
/* 299 */       Entry localEntry = ((Entry)localObject).next;
/* 300 */       ((Entry)localObject).next = (((Entry)localObject).previous = null);
/* 301 */       ((Entry)localObject).element = null;
/* 302 */       localObject = localEntry;
/*     */     }
/* 304 */     this.header.next = (this.header.previous = this.header);
/* 305 */     this.size = 0;
/* 306 */     this.modCount += 1;
/*     */   }
/*     */ 
/*     */   public E get(int paramInt)
/*     */   {
/* 320 */     return entry(paramInt).element;
/*     */   }
/*     */ 
/*     */   public E set(int paramInt, E paramE)
/*     */   {
/* 333 */     Entry localEntry = entry(paramInt);
/* 334 */     Object localObject = localEntry.element;
/* 335 */     localEntry.element = paramE;
/* 336 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE)
/*     */   {
/* 349 */     addBefore(paramE, paramInt == this.size ? this.header : entry(paramInt));
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt)
/*     */   {
/* 362 */     return remove(entry(paramInt));
/*     */   }
/*     */ 
/*     */   private Entry<E> entry(int paramInt)
/*     */   {
/* 369 */     if ((paramInt < 0) || (paramInt >= this.size)) {
/* 370 */       throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + this.size);
/*     */     }
/* 372 */     Entry localEntry = this.header;
/*     */     int i;
/* 373 */     if (paramInt < this.size >> 1)
/* 374 */       for (i = 0; i <= paramInt; i++)
/* 375 */         localEntry = localEntry.next;
/*     */     else {
/* 377 */       for (i = this.size; i > paramInt; i--)
/* 378 */         localEntry = localEntry.previous;
/*     */     }
/* 380 */     return localEntry;
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 398 */     int i = 0;
/* 399 */     for (Entry localEntry = this.header.next; localEntry != this.header; localEntry = localEntry.next) {
/* 400 */       if (paramObject == localEntry.element) {
/* 401 */         return i;
/*     */       }
/* 403 */       i++;
/*     */     }
/* 405 */     return -1;
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 420 */     int i = this.size;
/* 421 */     for (Entry localEntry = this.header.previous; localEntry != this.header; localEntry = localEntry.previous) {
/* 422 */       i--;
/* 423 */       if (paramObject == localEntry.element) {
/* 424 */         return i;
/*     */       }
/*     */     }
/* 427 */     return -1;
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 438 */     if (this.size == 0)
/* 439 */       return null;
/* 440 */     return getFirst();
/*     */   }
/*     */ 
/*     */   public E element()
/*     */   {
/* 450 */     return getFirst();
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 459 */     if (this.size == 0)
/* 460 */       return null;
/* 461 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   public E remove()
/*     */   {
/* 472 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   public boolean offer(E paramE)
/*     */   {
/* 483 */     return add(paramE);
/*     */   }
/*     */ 
/*     */   public boolean offerFirst(E paramE)
/*     */   {
/* 495 */     addFirst(paramE);
/* 496 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean offerLast(E paramE)
/*     */   {
/* 507 */     addLast(paramE);
/* 508 */     return true;
/*     */   }
/*     */ 
/*     */   public E peekFirst()
/*     */   {
/* 520 */     if (this.size == 0)
/* 521 */       return null;
/* 522 */     return getFirst();
/*     */   }
/*     */ 
/*     */   public E peekLast()
/*     */   {
/* 534 */     if (this.size == 0)
/* 535 */       return null;
/* 536 */     return getLast();
/*     */   }
/*     */ 
/*     */   public E pollFirst()
/*     */   {
/* 548 */     if (this.size == 0)
/* 549 */       return null;
/* 550 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   public E pollLast()
/*     */   {
/* 562 */     if (this.size == 0)
/* 563 */       return null;
/* 564 */     return removeLast();
/*     */   }
/*     */ 
/*     */   public void push(E paramE)
/*     */   {
/* 577 */     addFirst(paramE);
/*     */   }
/*     */ 
/*     */   public E pop()
/*     */   {
/* 592 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   public boolean removeFirstOccurrence(Object paramObject)
/*     */   {
/* 605 */     return remove(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean removeLastOccurrence(Object paramObject)
/*     */   {
/* 618 */     for (Entry localEntry = this.header.previous; localEntry != this.header; localEntry = localEntry.previous) {
/* 619 */       if (paramObject == localEntry.element) {
/* 620 */         remove(localEntry);
/* 621 */         return true;
/*     */       }
/*     */     }
/* 624 */     return false;
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator(int paramInt)
/*     */   {
/* 649 */     return new ListItr(paramInt);
/*     */   }
/*     */ 
/*     */   private Entry<E> addBefore(E paramE, Entry<E> paramEntry)
/*     */   {
/* 760 */     Entry localEntry = new Entry(paramE, paramEntry, paramEntry.previous);
/* 761 */     localEntry.previous.next = localEntry;
/* 762 */     localEntry.next.previous = localEntry;
/* 763 */     this.size += 1;
/* 764 */     this.modCount += 1;
/* 765 */     return localEntry;
/*     */   }
/*     */ 
/*     */   private E remove(Entry<E> paramEntry) {
/* 769 */     if (paramEntry == this.header) {
/* 770 */       throw new NoSuchElementException();
/*     */     }
/* 772 */     Object localObject = paramEntry.element;
/* 773 */     paramEntry.previous.next = paramEntry.next;
/* 774 */     paramEntry.next.previous = paramEntry.previous;
/* 775 */     paramEntry.next = (paramEntry.previous = null);
/* 776 */     paramEntry.element = null;
/* 777 */     this.size -= 1;
/* 778 */     this.modCount += 1;
/* 779 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Iterator<E> descendingIterator()
/*     */   {
/* 786 */     return new DescendingIterator(null);
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 818 */     Object[] arrayOfObject = new Object[this.size];
/* 819 */     int i = 0;
/* 820 */     for (Entry localEntry = this.header.next; localEntry != this.header; localEntry = localEntry.next)
/* 821 */       arrayOfObject[(i++)] = localEntry.element;
/* 822 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/* 864 */     if (paramArrayOfT.length < this.size) {
/* 865 */       paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), this.size);
/*     */     }
/* 867 */     int i = 0;
/* 868 */     T[] arrayOfT = paramArrayOfT;
/* 869 */     for (Entry localEntry = this.header.next; localEntry != this.header; localEntry = localEntry.next) {
/* 870 */       arrayOfT[(i++)] = localEntry.element;
/*     */     }
/* 872 */     if (paramArrayOfT.length > this.size) {
/* 873 */       paramArrayOfT[this.size] = null;
/*     */     }
/* 875 */     return paramArrayOfT;
/*     */   }
/*     */ 
/*     */   private class DescendingIterator
/*     */     implements Iterator
/*     */   {
/* 791 */     final IdentityLinkedList<E>.ListItr itr = new IdentityLinkedList.ListItr(IdentityLinkedList.this, IdentityLinkedList.this.size());
/*     */ 
/*     */     private DescendingIterator() {  } 
/* 793 */     public boolean hasNext() { return this.itr.hasPrevious(); }
/*     */ 
/*     */     public E next() {
/* 796 */       return this.itr.previous();
/*     */     }
/*     */     public void remove() {
/* 799 */       this.itr.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Entry<E>
/*     */   {
/*     */     E element;
/*     */     Entry<E> next;
/*     */     Entry<E> previous;
/*     */ 
/*     */     Entry(E paramE, Entry<E> paramEntry1, Entry<E> paramEntry2)
/*     */     {
/* 753 */       this.element = paramE;
/* 754 */       this.next = paramEntry1;
/* 755 */       this.previous = paramEntry2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ListItr
/*     */     implements ListIterator<E>
/*     */   {
/* 653 */     private IdentityLinkedList.Entry<E> lastReturned = IdentityLinkedList.this.header;
/*     */     private IdentityLinkedList.Entry<E> next;
/*     */     private int nextIndex;
/* 656 */     private int expectedModCount = IdentityLinkedList.this.modCount;
/*     */ 
/*     */     ListItr(int arg2)
/*     */     {
/*     */       int i;
/* 659 */       if ((i < 0) || (i > IdentityLinkedList.this.size)) {
/* 660 */         throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + IdentityLinkedList.this.size);
/*     */       }
/* 662 */       if (i < IdentityLinkedList.this.size >> 1) {
/* 663 */         this.next = IdentityLinkedList.this.header.next;
/* 664 */         for (this.nextIndex = 0; this.nextIndex < i; this.nextIndex += 1)
/* 665 */           this.next = this.next.next;
/*     */       }
/* 667 */       this.next = IdentityLinkedList.this.header;
/* 668 */       for (this.nextIndex = IdentityLinkedList.this.size; this.nextIndex > i; this.nextIndex -= 1)
/* 669 */         this.next = this.next.previous;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 674 */       return this.nextIndex != IdentityLinkedList.this.size;
/*     */     }
/*     */ 
/*     */     public E next() {
/* 678 */       checkForComodification();
/* 679 */       if (this.nextIndex == IdentityLinkedList.this.size) {
/* 680 */         throw new NoSuchElementException();
/*     */       }
/* 682 */       this.lastReturned = this.next;
/* 683 */       this.next = this.next.next;
/* 684 */       this.nextIndex += 1;
/* 685 */       return this.lastReturned.element;
/*     */     }
/*     */ 
/*     */     public boolean hasPrevious() {
/* 689 */       return this.nextIndex != 0;
/*     */     }
/*     */ 
/*     */     public E previous() {
/* 693 */       if (this.nextIndex == 0) {
/* 694 */         throw new NoSuchElementException();
/*     */       }
/* 696 */       this.lastReturned = (this.next = this.next.previous);
/* 697 */       this.nextIndex -= 1;
/* 698 */       checkForComodification();
/* 699 */       return this.lastReturned.element;
/*     */     }
/*     */ 
/*     */     public int nextIndex() {
/* 703 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     public int previousIndex() {
/* 707 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 711 */       checkForComodification();
/* 712 */       IdentityLinkedList.Entry localEntry = this.lastReturned.next;
/*     */       try {
/* 714 */         IdentityLinkedList.this.remove(this.lastReturned);
/*     */       } catch (NoSuchElementException localNoSuchElementException) {
/* 716 */         throw new IllegalStateException();
/*     */       }
/* 718 */       if (this.next == this.lastReturned)
/* 719 */         this.next = localEntry;
/*     */       else
/* 721 */         this.nextIndex -= 1;
/* 722 */       this.lastReturned = IdentityLinkedList.this.header;
/* 723 */       this.expectedModCount += 1;
/*     */     }
/*     */ 
/*     */     public void set(E paramE) {
/* 727 */       if (this.lastReturned == IdentityLinkedList.this.header)
/* 728 */         throw new IllegalStateException();
/* 729 */       checkForComodification();
/* 730 */       this.lastReturned.element = paramE;
/*     */     }
/*     */ 
/*     */     public void add(E paramE) {
/* 734 */       checkForComodification();
/* 735 */       this.lastReturned = IdentityLinkedList.this.header;
/* 736 */       IdentityLinkedList.this.addBefore(paramE, this.next);
/* 737 */       this.nextIndex += 1;
/* 738 */       this.expectedModCount += 1;
/*     */     }
/*     */ 
/*     */     final void checkForComodification() {
/* 742 */       if (IdentityLinkedList.this.modCount != this.expectedModCount)
/* 743 */         throw new ConcurrentModificationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.util.IdentityLinkedList
 * JD-Core Version:    0.6.2
 */