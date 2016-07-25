/*     */ package java.util;
/*     */ 
/*     */ public abstract class AbstractList<E> extends AbstractCollection<E>
/*     */   implements List<E>
/*     */ {
/* 601 */   protected transient int modCount = 0;
/*     */ 
/*     */   public boolean add(E paramE)
/*     */   {
/* 108 */     add(size(), paramE);
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   public abstract E get(int paramInt);
/*     */ 
/*     */   public E set(int paramInt, E paramE)
/*     */   {
/* 132 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE)
/*     */   {
/* 148 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt)
/*     */   {
/* 161 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 178 */     ListIterator localListIterator = listIterator();
/* 179 */     if (paramObject == null) {
/*     */       do if (!localListIterator.hasNext())
/*     */           break; while (localListIterator.next() != null);
/* 182 */       return localListIterator.previousIndex();
/*     */     }
/* 184 */     while (localListIterator.hasNext()) {
/* 185 */       if (paramObject.equals(localListIterator.next()))
/* 186 */         return localListIterator.previousIndex();
/*     */     }
/* 188 */     return -1;
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 203 */     ListIterator localListIterator = listIterator(size());
/* 204 */     if (paramObject == null) {
/*     */       do if (!localListIterator.hasPrevious())
/*     */           break; while (localListIterator.previous() != null);
/* 207 */       return localListIterator.nextIndex();
/*     */     }
/* 209 */     while (localListIterator.hasPrevious()) {
/* 210 */       if (paramObject.equals(localListIterator.previous()))
/* 211 */         return localListIterator.nextIndex();
/*     */     }
/* 213 */     return -1;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 234 */     removeRange(0, size());
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*     */   {
/* 257 */     rangeCheckForAdd(paramInt);
/* 258 */     boolean bool = false;
/* 259 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 260 */       add(paramInt++, localObject);
/* 261 */       bool = true;
/*     */     }
/* 263 */     return bool;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 288 */     return new Itr(null);
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator()
/*     */   {
/* 299 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator(int paramInt)
/*     */   {
/* 325 */     rangeCheckForAdd(paramInt);
/*     */ 
/* 327 */     return new ListItr(paramInt);
/*     */   }
/*     */ 
/*     */   public List<E> subList(int paramInt1, int paramInt2)
/*     */   {
/* 484 */     return (this instanceof RandomAccess) ? new RandomAccessSubList(this, paramInt1, paramInt2) : new SubList(this, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 513 */     if (paramObject == this)
/* 514 */       return true;
/* 515 */     if (!(paramObject instanceof List)) {
/* 516 */       return false;
/*     */     }
/* 518 */     ListIterator localListIterator1 = listIterator();
/* 519 */     ListIterator localListIterator2 = ((List)paramObject).listIterator();
/* 520 */     while ((localListIterator1.hasNext()) && (localListIterator2.hasNext())) {
/* 521 */       Object localObject1 = localListIterator1.next();
/* 522 */       Object localObject2 = localListIterator2.next();
/* 523 */       if (localObject1 == null ? localObject2 != null : !localObject1.equals(localObject2))
/* 524 */         return false;
/*     */     }
/* 526 */     return (!localListIterator1.hasNext()) && (!localListIterator2.hasNext());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 539 */     int i = 1;
/* 540 */     for (Iterator localIterator = iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 541 */       i = 31 * i + (localObject == null ? 0 : localObject.hashCode()); }
/* 542 */     return i;
/*     */   }
/*     */ 
/*     */   protected void removeRange(int paramInt1, int paramInt2)
/*     */   {
/* 568 */     ListIterator localListIterator = listIterator(paramInt1);
/* 569 */     int i = 0; for (int j = paramInt2 - paramInt1; i < j; i++) {
/* 570 */       localListIterator.next();
/* 571 */       localListIterator.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void rangeCheckForAdd(int paramInt)
/*     */   {
/* 604 */     if ((paramInt < 0) || (paramInt > size()))
/* 605 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*     */   }
/*     */ 
/*     */   private String outOfBoundsMsg(int paramInt) {
/* 609 */     return "Index: " + paramInt + ", Size: " + size();
/*     */   }
/*     */ 
/*     */   private class Itr
/*     */     implements Iterator<E>
/*     */   {
/* 334 */     int cursor = 0;
/*     */ 
/* 341 */     int lastRet = -1;
/*     */ 
/* 348 */     int expectedModCount = AbstractList.this.modCount;
/*     */ 
/*     */     private Itr() {  } 
/* 351 */     public boolean hasNext() { return this.cursor != AbstractList.this.size(); }
/*     */ 
/*     */     public E next()
/*     */     {
/* 355 */       checkForComodification();
/*     */       try {
/* 357 */         int i = this.cursor;
/* 358 */         Object localObject = AbstractList.this.get(i);
/* 359 */         this.lastRet = i;
/* 360 */         this.cursor = (i + 1);
/* 361 */         return localObject;
/*     */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 363 */         checkForComodification();
/* 364 */       }throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 369 */       if (this.lastRet < 0)
/* 370 */         throw new IllegalStateException();
/* 371 */       checkForComodification();
/*     */       try
/*     */       {
/* 374 */         AbstractList.this.remove(this.lastRet);
/* 375 */         if (this.lastRet < this.cursor)
/* 376 */           this.cursor -= 1;
/* 377 */         this.lastRet = -1;
/* 378 */         this.expectedModCount = AbstractList.this.modCount;
/*     */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 380 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     final void checkForComodification() {
/* 385 */       if (AbstractList.this.modCount != this.expectedModCount)
/* 386 */         throw new ConcurrentModificationException(); 
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ListItr extends AbstractList<E>.Itr implements ListIterator<E> {
/* 391 */     ListItr(int arg2) { super(null);
/*     */       int i;
/* 392 */       this.cursor = i; }
/*     */ 
/*     */     public boolean hasPrevious()
/*     */     {
/* 396 */       return this.cursor != 0;
/*     */     }
/*     */ 
/*     */     public E previous() {
/* 400 */       checkForComodification();
/*     */       try {
/* 402 */         int i = this.cursor - 1;
/* 403 */         Object localObject = AbstractList.this.get(i);
/* 404 */         this.lastRet = (this.cursor = i);
/* 405 */         return localObject;
/*     */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 407 */         checkForComodification();
/* 408 */       }throw new NoSuchElementException();
/*     */     }
/*     */ 
/*     */     public int nextIndex()
/*     */     {
/* 413 */       return this.cursor;
/*     */     }
/*     */ 
/*     */     public int previousIndex() {
/* 417 */       return this.cursor - 1;
/*     */     }
/*     */ 
/*     */     public void set(E paramE) {
/* 421 */       if (this.lastRet < 0)
/* 422 */         throw new IllegalStateException();
/* 423 */       checkForComodification();
/*     */       try
/*     */       {
/* 426 */         AbstractList.this.set(this.lastRet, paramE);
/* 427 */         this.expectedModCount = AbstractList.this.modCount;
/*     */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 429 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void add(E paramE) {
/* 434 */       checkForComodification();
/*     */       try
/*     */       {
/* 437 */         int i = this.cursor;
/* 438 */         AbstractList.this.add(i, paramE);
/* 439 */         this.lastRet = -1;
/* 440 */         this.cursor = (i + 1);
/* 441 */         this.expectedModCount = AbstractList.this.modCount;
/*     */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 443 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.AbstractList
 * JD-Core Version:    0.6.2
 */