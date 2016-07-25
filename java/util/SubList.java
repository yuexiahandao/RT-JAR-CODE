/*     */ package java.util;
/*     */ 
/*     */ class SubList<E> extends AbstractList<E>
/*     */ {
/*     */   private final AbstractList<E> l;
/*     */   private final int offset;
/*     */   private int size;
/*     */ 
/*     */   SubList(AbstractList<E> paramAbstractList, int paramInt1, int paramInt2)
/*     */   {
/* 619 */     if (paramInt1 < 0)
/* 620 */       throw new IndexOutOfBoundsException("fromIndex = " + paramInt1);
/* 621 */     if (paramInt2 > paramAbstractList.size())
/* 622 */       throw new IndexOutOfBoundsException("toIndex = " + paramInt2);
/* 623 */     if (paramInt1 > paramInt2) {
/* 624 */       throw new IllegalArgumentException("fromIndex(" + paramInt1 + ") > toIndex(" + paramInt2 + ")");
/*     */     }
/* 626 */     this.l = paramAbstractList;
/* 627 */     this.offset = paramInt1;
/* 628 */     this.size = (paramInt2 - paramInt1);
/* 629 */     this.modCount = this.l.modCount;
/*     */   }
/*     */ 
/*     */   public E set(int paramInt, E paramE) {
/* 633 */     rangeCheck(paramInt);
/* 634 */     checkForComodification();
/* 635 */     return this.l.set(paramInt + this.offset, paramE);
/*     */   }
/*     */ 
/*     */   public E get(int paramInt) {
/* 639 */     rangeCheck(paramInt);
/* 640 */     checkForComodification();
/* 641 */     return this.l.get(paramInt + this.offset);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 645 */     checkForComodification();
/* 646 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE) {
/* 650 */     rangeCheckForAdd(paramInt);
/* 651 */     checkForComodification();
/* 652 */     this.l.add(paramInt + this.offset, paramE);
/* 653 */     this.modCount = this.l.modCount;
/* 654 */     this.size += 1;
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt) {
/* 658 */     rangeCheck(paramInt);
/* 659 */     checkForComodification();
/* 660 */     Object localObject = this.l.remove(paramInt + this.offset);
/* 661 */     this.modCount = this.l.modCount;
/* 662 */     this.size -= 1;
/* 663 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected void removeRange(int paramInt1, int paramInt2) {
/* 667 */     checkForComodification();
/* 668 */     this.l.removeRange(paramInt1 + this.offset, paramInt2 + this.offset);
/* 669 */     this.modCount = this.l.modCount;
/* 670 */     this.size -= paramInt2 - paramInt1;
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection) {
/* 674 */     return addAll(this.size, paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection) {
/* 678 */     rangeCheckForAdd(paramInt);
/* 679 */     int i = paramCollection.size();
/* 680 */     if (i == 0) {
/* 681 */       return false;
/*     */     }
/* 683 */     checkForComodification();
/* 684 */     this.l.addAll(this.offset + paramInt, paramCollection);
/* 685 */     this.modCount = this.l.modCount;
/* 686 */     this.size += i;
/* 687 */     return true;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator() {
/* 691 */     return listIterator();
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator(final int paramInt) {
/* 695 */     checkForComodification();
/* 696 */     rangeCheckForAdd(paramInt);
/*     */ 
/* 698 */     return new ListIterator() {
/* 699 */       private final ListIterator<E> i = SubList.this.l.listIterator(paramInt + SubList.this.offset);
/*     */ 
/*     */       public boolean hasNext() {
/* 702 */         return nextIndex() < SubList.this.size;
/*     */       }
/*     */ 
/*     */       public E next() {
/* 706 */         if (hasNext()) {
/* 707 */           return this.i.next();
/*     */         }
/* 709 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public boolean hasPrevious() {
/* 713 */         return previousIndex() >= 0;
/*     */       }
/*     */ 
/*     */       public E previous() {
/* 717 */         if (hasPrevious()) {
/* 718 */           return this.i.previous();
/*     */         }
/* 720 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       public int nextIndex() {
/* 724 */         return this.i.nextIndex() - SubList.this.offset;
/*     */       }
/*     */ 
/*     */       public int previousIndex() {
/* 728 */         return this.i.previousIndex() - SubList.this.offset;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 732 */         this.i.remove();
/* 733 */         SubList.this.modCount = SubList.this.l.modCount;
/* 734 */         SubList.access$210(SubList.this);
/*     */       }
/*     */ 
/*     */       public void set(E paramAnonymousE) {
/* 738 */         this.i.set(paramAnonymousE);
/*     */       }
/*     */ 
/*     */       public void add(E paramAnonymousE) {
/* 742 */         this.i.add(paramAnonymousE);
/* 743 */         SubList.this.modCount = SubList.this.l.modCount;
/* 744 */         SubList.access$208(SubList.this);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public List<E> subList(int paramInt1, int paramInt2) {
/* 750 */     return new SubList(this, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void rangeCheck(int paramInt) {
/* 754 */     if ((paramInt < 0) || (paramInt >= this.size))
/* 755 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*     */   }
/*     */ 
/*     */   private void rangeCheckForAdd(int paramInt) {
/* 759 */     if ((paramInt < 0) || (paramInt > this.size))
/* 760 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*     */   }
/*     */ 
/*     */   private String outOfBoundsMsg(int paramInt) {
/* 764 */     return "Index: " + paramInt + ", Size: " + this.size;
/*     */   }
/*     */ 
/*     */   private void checkForComodification() {
/* 768 */     if (this.modCount != this.l.modCount)
/* 769 */       throw new ConcurrentModificationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.SubList
 * JD-Core Version:    0.6.2
 */