/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class ArrayList<E> extends AbstractList<E>
/*      */   implements List<E>, RandomAccess, Cloneable, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 8683452581122892189L;
/*      */   private static final int DEFAULT_CAPACITY = 10;
/*  115 */   private static final Object[] EMPTY_ELEMENTDATA = new Object[0];
/*      */   private transient Object[] elementData;
/*      */   private int size;
/*      */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*      */ 
/*      */   public ArrayList(int paramInt)
/*      */   {
/*  141 */     if (paramInt < 0) {
/*  142 */       throw new IllegalArgumentException("Illegal Capacity: " + paramInt);
/*      */     }
/*  144 */     this.elementData = new Object[paramInt];
/*      */   }
/*      */ 
/*      */   public ArrayList()
/*      */   {
/*  152 */     this.elementData = EMPTY_ELEMENTDATA;
/*      */   }
/*      */ 
/*      */   public ArrayList(Collection<? extends E> paramCollection)
/*      */   {
/*  164 */     this.elementData = paramCollection.toArray();
/*  165 */     this.size = this.elementData.length;
/*      */ 
/*  167 */     if (this.elementData.getClass() != [Ljava.lang.Object.class)
/*  168 */       this.elementData = Arrays.copyOf(this.elementData, this.size, [Ljava.lang.Object.class);
/*      */   }
/*      */ 
/*      */   public void trimToSize()
/*      */   {
/*  177 */     this.modCount += 1;
/*  178 */     if (this.size < this.elementData.length)
/*  179 */       this.elementData = Arrays.copyOf(this.elementData, this.size);
/*      */   }
/*      */ 
/*      */   public void ensureCapacity(int paramInt)
/*      */   {
/*  191 */     int i = this.elementData != EMPTY_ELEMENTDATA ? 0 : 10;
/*      */ 
/*  198 */     if (paramInt > i)
/*  199 */       ensureExplicitCapacity(paramInt);
/*      */   }
/*      */ 
/*      */   private void ensureCapacityInternal(int paramInt)
/*      */   {
/*  204 */     if (this.elementData == EMPTY_ELEMENTDATA) {
/*  205 */       paramInt = Math.max(10, paramInt);
/*      */     }
/*      */ 
/*  208 */     ensureExplicitCapacity(paramInt);
/*      */   }
/*      */ 
/*      */   private void ensureExplicitCapacity(int paramInt) {
/*  212 */     this.modCount += 1;
/*      */ 
/*  215 */     if (paramInt - this.elementData.length > 0)
/*  216 */       grow(paramInt);
/*      */   }
/*      */ 
/*      */   private void grow(int paramInt)
/*      */   {
/*  235 */     int i = this.elementData.length;
/*  236 */     int j = i + (i >> 1);
/*  237 */     if (j - paramInt < 0)
/*  238 */       j = paramInt;
/*  239 */     if (j - 2147483639 > 0) {
/*  240 */       j = hugeCapacity(paramInt);
/*      */     }
/*  242 */     this.elementData = Arrays.copyOf(this.elementData, j);
/*      */   }
/*      */ 
/*      */   private static int hugeCapacity(int paramInt) {
/*  246 */     if (paramInt < 0)
/*  247 */       throw new OutOfMemoryError();
/*  248 */     return paramInt > 2147483639 ? 2147483647 : 2147483639;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  259 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  268 */     return this.size == 0;
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  281 */     return indexOf(paramObject) >= 0;
/*      */   }
/*      */ 
/*      */   public int indexOf(Object paramObject)
/*      */   {
/*      */     int i;
/*  292 */     if (paramObject == null)
/*  293 */       for (i = 0; i < this.size; i++)
/*  294 */         if (this.elementData[i] == null)
/*  295 */           return i;
/*      */     else {
/*  297 */       for (i = 0; i < this.size; i++)
/*  298 */         if (paramObject.equals(this.elementData[i]))
/*  299 */           return i;
/*      */     }
/*  301 */     return -1;
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(Object paramObject)
/*      */   {
/*      */     int i;
/*  312 */     if (paramObject == null)
/*  313 */       for (i = this.size - 1; i >= 0; i--)
/*  314 */         if (this.elementData[i] == null)
/*  315 */           return i;
/*      */     else {
/*  317 */       for (i = this.size - 1; i >= 0; i--)
/*  318 */         if (paramObject.equals(this.elementData[i]))
/*  319 */           return i;
/*      */     }
/*  321 */     return -1;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  333 */       ArrayList localArrayList = (ArrayList)super.clone();
/*  334 */       localArrayList.elementData = Arrays.copyOf(this.elementData, this.size);
/*  335 */       localArrayList.modCount = 0;
/*  336 */       return localArrayList;
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  339 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/*  358 */     return Arrays.copyOf(this.elementData, this.size);
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/*  387 */     if (paramArrayOfT.length < this.size)
/*      */     {
/*  389 */       return (Object[])Arrays.copyOf(this.elementData, this.size, paramArrayOfT.getClass());
/*  390 */     }System.arraycopy(this.elementData, 0, paramArrayOfT, 0, this.size);
/*  391 */     if (paramArrayOfT.length > this.size)
/*  392 */       paramArrayOfT[this.size] = null;
/*  393 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   E elementData(int paramInt)
/*      */   {
/*  400 */     return this.elementData[paramInt];
/*      */   }
/*      */ 
/*      */   public E get(int paramInt)
/*      */   {
/*  411 */     rangeCheck(paramInt);
/*      */ 
/*  413 */     return elementData(paramInt);
/*      */   }
/*      */ 
/*      */   public E set(int paramInt, E paramE)
/*      */   {
/*  426 */     rangeCheck(paramInt);
/*      */ 
/*  428 */     Object localObject = elementData(paramInt);
/*  429 */     this.elementData[paramInt] = paramE;
/*  430 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/*  440 */     ensureCapacityInternal(this.size + 1);
/*  441 */     this.elementData[(this.size++)] = paramE;
/*  442 */     return true;
/*      */   }
/*      */ 
/*      */   public void add(int paramInt, E paramE)
/*      */   {
/*  455 */     rangeCheckForAdd(paramInt);
/*      */ 
/*  457 */     ensureCapacityInternal(this.size + 1);
/*  458 */     System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + 1, this.size - paramInt);
/*      */ 
/*  460 */     this.elementData[paramInt] = paramE;
/*  461 */     this.size += 1;
/*      */   }
/*      */ 
/*      */   public E remove(int paramInt)
/*      */   {
/*  474 */     rangeCheck(paramInt);
/*      */ 
/*  476 */     this.modCount += 1;
/*  477 */     Object localObject = elementData(paramInt);
/*      */ 
/*  479 */     int i = this.size - paramInt - 1;
/*  480 */     if (i > 0) {
/*  481 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*      */     }
/*  483 */     this.elementData[(--this.size)] = null;
/*      */ 
/*  485 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*      */     int i;
/*  502 */     if (paramObject == null)
/*  503 */       for (i = 0; i < this.size; i++)
/*  504 */         if (this.elementData[i] == null) {
/*  505 */           fastRemove(i);
/*  506 */           return true;
/*      */         }
/*      */     else {
/*  509 */       for (i = 0; i < this.size; i++)
/*  510 */         if (paramObject.equals(this.elementData[i])) {
/*  511 */           fastRemove(i);
/*  512 */           return true;
/*      */         }
/*      */     }
/*  515 */     return false;
/*      */   }
/*      */ 
/*      */   private void fastRemove(int paramInt)
/*      */   {
/*  523 */     this.modCount += 1;
/*  524 */     int i = this.size - paramInt - 1;
/*  525 */     if (i > 0) {
/*  526 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*      */     }
/*  528 */     this.elementData[(--this.size)] = null;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  536 */     this.modCount += 1;
/*      */ 
/*  539 */     for (int i = 0; i < this.size; i++) {
/*  540 */       this.elementData[i] = null;
/*      */     }
/*  542 */     this.size = 0;
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection<? extends E> paramCollection)
/*      */   {
/*  559 */     Object[] arrayOfObject = paramCollection.toArray();
/*  560 */     int i = arrayOfObject.length;
/*  561 */     ensureCapacityInternal(this.size + i);
/*  562 */     System.arraycopy(arrayOfObject, 0, this.elementData, this.size, i);
/*  563 */     this.size += i;
/*  564 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*      */   {
/*  583 */     rangeCheckForAdd(paramInt);
/*      */ 
/*  585 */     Object[] arrayOfObject = paramCollection.toArray();
/*  586 */     int i = arrayOfObject.length;
/*  587 */     ensureCapacityInternal(this.size + i);
/*      */ 
/*  589 */     int j = this.size - paramInt;
/*  590 */     if (j > 0) {
/*  591 */       System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + i, j);
/*      */     }
/*      */ 
/*  594 */     System.arraycopy(arrayOfObject, 0, this.elementData, paramInt, i);
/*  595 */     this.size += i;
/*  596 */     return i != 0;
/*      */   }
/*      */ 
/*      */   protected void removeRange(int paramInt1, int paramInt2)
/*      */   {
/*  614 */     this.modCount += 1;
/*  615 */     int i = this.size - paramInt2;
/*  616 */     System.arraycopy(this.elementData, paramInt2, this.elementData, paramInt1, i);
/*      */ 
/*  620 */     int j = this.size - (paramInt2 - paramInt1);
/*  621 */     for (int k = j; k < this.size; k++) {
/*  622 */       this.elementData[k] = null;
/*      */     }
/*  624 */     this.size = j;
/*      */   }
/*      */ 
/*      */   private void rangeCheck(int paramInt)
/*      */   {
/*  634 */     if (paramInt >= this.size)
/*  635 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */   }
/*      */ 
/*      */   private void rangeCheckForAdd(int paramInt)
/*      */   {
/*  642 */     if ((paramInt > this.size) || (paramInt < 0))
/*  643 */       throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */   }
/*      */ 
/*      */   private String outOfBoundsMsg(int paramInt)
/*      */   {
/*  652 */     return "Index: " + paramInt + ", Size: " + this.size;
/*      */   }
/*      */ 
/*      */   public boolean removeAll(Collection<?> paramCollection)
/*      */   {
/*  671 */     return batchRemove(paramCollection, false);
/*      */   }
/*      */ 
/*      */   public boolean retainAll(Collection<?> paramCollection)
/*      */   {
/*  691 */     return batchRemove(paramCollection, true);
/*      */   }
/*      */ 
/*      */   private boolean batchRemove(Collection<?> paramCollection, boolean paramBoolean) {
/*  695 */     Object[] arrayOfObject = this.elementData;
/*  696 */     int i = 0; int j = 0;
/*  697 */     boolean bool = false;
/*      */     try {
/*  699 */       for (; i < this.size; i++) {
/*  700 */         if (paramCollection.contains(arrayOfObject[i]) == paramBoolean) {
/*  701 */           arrayOfObject[(j++)] = arrayOfObject[i];
/*      */         }
/*      */       }
/*      */ 
/*  705 */       if (i != this.size) {
/*  706 */         System.arraycopy(arrayOfObject, i, arrayOfObject, j, this.size - i);
/*      */ 
/*  709 */         j += this.size - i;
/*      */       }
/*  711 */       if (j != this.size)
/*      */       {
/*  713 */         for (int k = j; k < this.size; k++)
/*  714 */           arrayOfObject[k] = null;
/*  715 */         this.modCount += this.size - j;
/*  716 */         this.size = j;
/*  717 */         bool = true;
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/*  705 */       if (i != this.size) {
/*  706 */         System.arraycopy(arrayOfObject, i, arrayOfObject, j, this.size - i);
/*      */ 
/*  709 */         j += this.size - i;
/*      */       }
/*  711 */       if (j != this.size)
/*      */       {
/*  713 */         for (int m = j; m < this.size; m++)
/*  714 */           arrayOfObject[m] = null;
/*  715 */         this.modCount += this.size - j;
/*  716 */         this.size = j;
/*  717 */         bool = true;
/*      */       }
/*      */     }
/*  720 */     return bool;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  734 */     int i = this.modCount;
/*  735 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/*  738 */     paramObjectOutputStream.writeInt(this.size);
/*      */ 
/*  741 */     for (int j = 0; j < this.size; j++) {
/*  742 */       paramObjectOutputStream.writeObject(this.elementData[j]);
/*      */     }
/*      */ 
/*  745 */     if (this.modCount != i)
/*  746 */       throw new ConcurrentModificationException();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  756 */     this.elementData = EMPTY_ELEMENTDATA;
/*      */ 
/*  759 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/*  762 */     paramObjectInputStream.readInt();
/*      */ 
/*  764 */     if (this.size > 0)
/*      */     {
/*  766 */       ensureCapacityInternal(this.size);
/*      */ 
/*  768 */       Object[] arrayOfObject = this.elementData;
/*      */ 
/*  770 */       for (int i = 0; i < this.size; i++)
/*  771 */         arrayOfObject[i] = paramObjectInputStream.readObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ListIterator<E> listIterator(int paramInt)
/*      */   {
/*  789 */     if ((paramInt < 0) || (paramInt > this.size))
/*  790 */       throw new IndexOutOfBoundsException("Index: " + paramInt);
/*  791 */     return new ListItr(paramInt);
/*      */   }
/*      */ 
/*      */   public ListIterator<E> listIterator()
/*      */   {
/*  803 */     return new ListItr(0);
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/*  814 */     return new Itr(null);
/*      */   }
/*      */ 
/*      */   public List<E> subList(int paramInt1, int paramInt2)
/*      */   {
/*  954 */     subListRangeCheck(paramInt1, paramInt2, this.size);
/*  955 */     return new SubList(this, 0, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   static void subListRangeCheck(int paramInt1, int paramInt2, int paramInt3) {
/*  959 */     if (paramInt1 < 0)
/*  960 */       throw new IndexOutOfBoundsException("fromIndex = " + paramInt1);
/*  961 */     if (paramInt2 > paramInt3)
/*  962 */       throw new IndexOutOfBoundsException("toIndex = " + paramInt2);
/*  963 */     if (paramInt1 > paramInt2)
/*  964 */       throw new IllegalArgumentException("fromIndex(" + paramInt1 + ") > toIndex(" + paramInt2 + ")");
/*      */   }
/*      */ 
/*      */   private class Itr
/*      */     implements Iterator<E>
/*      */   {
/*      */     int cursor;
/*  822 */     int lastRet = -1;
/*  823 */     int expectedModCount = ArrayList.this.modCount;
/*      */ 
/*      */     private Itr() {  } 
/*  826 */     public boolean hasNext() { return this.cursor != ArrayList.this.size; }
/*      */ 
/*      */ 
/*      */     public E next()
/*      */     {
/*  831 */       checkForComodification();
/*  832 */       int i = this.cursor;
/*  833 */       if (i >= ArrayList.this.size)
/*  834 */         throw new NoSuchElementException();
/*  835 */       Object[] arrayOfObject = ArrayList.this.elementData;
/*  836 */       if (i >= arrayOfObject.length)
/*  837 */         throw new ConcurrentModificationException();
/*  838 */       this.cursor = (i + 1);
/*  839 */       return arrayOfObject[(this.lastRet = i)];
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  843 */       if (this.lastRet < 0)
/*  844 */         throw new IllegalStateException();
/*  845 */       checkForComodification();
/*      */       try
/*      */       {
/*  848 */         ArrayList.this.remove(this.lastRet);
/*  849 */         this.cursor = this.lastRet;
/*  850 */         this.lastRet = -1;
/*  851 */         this.expectedModCount = ArrayList.this.modCount;
/*      */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*  853 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */ 
/*      */     final void checkForComodification() {
/*  858 */       if (ArrayList.this.modCount != this.expectedModCount)
/*  859 */         throw new ConcurrentModificationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ListItr extends ArrayList<E>.Itr
/*      */     implements ListIterator<E>
/*      */   {
/*      */     ListItr(int arg2)
/*      */     {
/*  868 */       super(null);
/*      */       int i;
/*  869 */       this.cursor = i;
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/*  873 */       return this.cursor != 0;
/*      */     }
/*      */ 
/*      */     public int nextIndex() {
/*  877 */       return this.cursor;
/*      */     }
/*      */ 
/*      */     public int previousIndex() {
/*  881 */       return this.cursor - 1;
/*      */     }
/*      */ 
/*      */     public E previous()
/*      */     {
/*  886 */       checkForComodification();
/*  887 */       int i = this.cursor - 1;
/*  888 */       if (i < 0)
/*  889 */         throw new NoSuchElementException();
/*  890 */       Object[] arrayOfObject = ArrayList.this.elementData;
/*  891 */       if (i >= arrayOfObject.length)
/*  892 */         throw new ConcurrentModificationException();
/*  893 */       this.cursor = i;
/*  894 */       return arrayOfObject[(this.lastRet = i)];
/*      */     }
/*      */ 
/*      */     public void set(E paramE) {
/*  898 */       if (this.lastRet < 0)
/*  899 */         throw new IllegalStateException();
/*  900 */       checkForComodification();
/*      */       try
/*      */       {
/*  903 */         ArrayList.this.set(this.lastRet, paramE);
/*      */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*  905 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void add(E paramE) {
/*  910 */       checkForComodification();
/*      */       try
/*      */       {
/*  913 */         int i = this.cursor;
/*  914 */         ArrayList.this.add(i, paramE);
/*  915 */         this.cursor = (i + 1);
/*  916 */         this.lastRet = -1;
/*  917 */         this.expectedModCount = ArrayList.this.modCount;
/*      */       } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*  919 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SubList extends AbstractList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private final AbstractList<E> parent;
/*      */     private final int parentOffset;
/*      */     private final int offset;
/*      */     int size;
/*      */ 
/*      */     SubList(int paramInt1, int paramInt2, int arg4)
/*      */     {
/*  976 */       this.parent = paramInt1;
/*      */       int i;
/*  977 */       this.parentOffset = i;
/*  978 */       this.offset = (paramInt2 + i);
/*      */       int j;
/*  979 */       this.size = (j - i);
/*  980 */       this.modCount = ArrayList.this.modCount;
/*      */     }
/*      */ 
/*      */     public E set(int paramInt, E paramE) {
/*  984 */       rangeCheck(paramInt);
/*  985 */       checkForComodification();
/*  986 */       Object localObject = ArrayList.this.elementData(this.offset + paramInt);
/*  987 */       ArrayList.this.elementData[(this.offset + paramInt)] = paramE;
/*  988 */       return localObject;
/*      */     }
/*      */ 
/*      */     public E get(int paramInt) {
/*  992 */       rangeCheck(paramInt);
/*  993 */       checkForComodification();
/*  994 */       return ArrayList.this.elementData(this.offset + paramInt);
/*      */     }
/*      */ 
/*      */     public int size() {
/*  998 */       checkForComodification();
/*  999 */       return this.size;
/*      */     }
/*      */ 
/*      */     public void add(int paramInt, E paramE) {
/* 1003 */       rangeCheckForAdd(paramInt);
/* 1004 */       checkForComodification();
/* 1005 */       this.parent.add(this.parentOffset + paramInt, paramE);
/* 1006 */       this.modCount = this.parent.modCount;
/* 1007 */       this.size += 1;
/*      */     }
/*      */ 
/*      */     public E remove(int paramInt) {
/* 1011 */       rangeCheck(paramInt);
/* 1012 */       checkForComodification();
/* 1013 */       Object localObject = this.parent.remove(this.parentOffset + paramInt);
/* 1014 */       this.modCount = this.parent.modCount;
/* 1015 */       this.size -= 1;
/* 1016 */       return localObject;
/*      */     }
/*      */ 
/*      */     protected void removeRange(int paramInt1, int paramInt2) {
/* 1020 */       checkForComodification();
/* 1021 */       this.parent.removeRange(this.parentOffset + paramInt1, this.parentOffset + paramInt2);
/*      */ 
/* 1023 */       this.modCount = this.parent.modCount;
/* 1024 */       this.size -= paramInt2 - paramInt1;
/*      */     }
/*      */ 
/*      */     public boolean addAll(Collection<? extends E> paramCollection) {
/* 1028 */       return addAll(this.size, paramCollection);
/*      */     }
/*      */ 
/*      */     public boolean addAll(int paramInt, Collection<? extends E> paramCollection) {
/* 1032 */       rangeCheckForAdd(paramInt);
/* 1033 */       int i = paramCollection.size();
/* 1034 */       if (i == 0) {
/* 1035 */         return false;
/*      */       }
/* 1037 */       checkForComodification();
/* 1038 */       this.parent.addAll(this.parentOffset + paramInt, paramCollection);
/* 1039 */       this.modCount = this.parent.modCount;
/* 1040 */       this.size += i;
/* 1041 */       return true;
/*      */     }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 1045 */       return listIterator();
/*      */     }
/*      */ 
/*      */     public ListIterator<E> listIterator(final int paramInt) {
/* 1049 */       checkForComodification();
/* 1050 */       rangeCheckForAdd(paramInt);
/* 1051 */       final int i = this.offset;
/*      */ 
/* 1053 */       return new ListIterator() {
/* 1054 */         int cursor = paramInt;
/* 1055 */         int lastRet = -1;
/* 1056 */         int expectedModCount = ArrayList.this.modCount;
/*      */ 
/*      */         public boolean hasNext() {
/* 1059 */           return this.cursor != ArrayList.SubList.this.size;
/*      */         }
/*      */ 
/*      */         public E next()
/*      */         {
/* 1064 */           checkForComodification();
/* 1065 */           int i = this.cursor;
/* 1066 */           if (i >= ArrayList.SubList.this.size)
/* 1067 */             throw new NoSuchElementException();
/* 1068 */           Object[] arrayOfObject = ArrayList.this.elementData;
/* 1069 */           if (i + i >= arrayOfObject.length)
/* 1070 */             throw new ConcurrentModificationException();
/* 1071 */           this.cursor = (i + 1);
/* 1072 */           return arrayOfObject[(i + (this.lastRet = i))];
/*      */         }
/*      */ 
/*      */         public boolean hasPrevious() {
/* 1076 */           return this.cursor != 0;
/*      */         }
/*      */ 
/*      */         public E previous()
/*      */         {
/* 1081 */           checkForComodification();
/* 1082 */           int i = this.cursor - 1;
/* 1083 */           if (i < 0)
/* 1084 */             throw new NoSuchElementException();
/* 1085 */           Object[] arrayOfObject = ArrayList.this.elementData;
/* 1086 */           if (i + i >= arrayOfObject.length)
/* 1087 */             throw new ConcurrentModificationException();
/* 1088 */           this.cursor = i;
/* 1089 */           return arrayOfObject[(i + (this.lastRet = i))];
/*      */         }
/*      */ 
/*      */         public int nextIndex() {
/* 1093 */           return this.cursor;
/*      */         }
/*      */ 
/*      */         public int previousIndex() {
/* 1097 */           return this.cursor - 1;
/*      */         }
/*      */ 
/*      */         public void remove() {
/* 1101 */           if (this.lastRet < 0)
/* 1102 */             throw new IllegalStateException();
/* 1103 */           checkForComodification();
/*      */           try
/*      */           {
/* 1106 */             ArrayList.SubList.this.remove(this.lastRet);
/* 1107 */             this.cursor = this.lastRet;
/* 1108 */             this.lastRet = -1;
/* 1109 */             this.expectedModCount = ArrayList.this.modCount;
/*      */           } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 1111 */             throw new ConcurrentModificationException();
/*      */           }
/*      */         }
/*      */ 
/*      */         public void set(E paramAnonymousE) {
/* 1116 */           if (this.lastRet < 0)
/* 1117 */             throw new IllegalStateException();
/* 1118 */           checkForComodification();
/*      */           try
/*      */           {
/* 1121 */             ArrayList.this.set(i + this.lastRet, paramAnonymousE);
/*      */           } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 1123 */             throw new ConcurrentModificationException();
/*      */           }
/*      */         }
/*      */ 
/*      */         public void add(E paramAnonymousE) {
/* 1128 */           checkForComodification();
/*      */           try
/*      */           {
/* 1131 */             int i = this.cursor;
/* 1132 */             ArrayList.SubList.this.add(i, paramAnonymousE);
/* 1133 */             this.cursor = (i + 1);
/* 1134 */             this.lastRet = -1;
/* 1135 */             this.expectedModCount = ArrayList.this.modCount;
/*      */           } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 1137 */             throw new ConcurrentModificationException();
/*      */           }
/*      */         }
/*      */ 
/*      */         final void checkForComodification() {
/* 1142 */           if (this.expectedModCount != ArrayList.this.modCount)
/* 1143 */             throw new ConcurrentModificationException();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1149 */       ArrayList.subListRangeCheck(paramInt1, paramInt2, this.size);
/* 1150 */       return new SubList(ArrayList.this, this, this.offset, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     private void rangeCheck(int paramInt) {
/* 1154 */       if ((paramInt < 0) || (paramInt >= this.size))
/* 1155 */         throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */     }
/*      */ 
/*      */     private void rangeCheckForAdd(int paramInt) {
/* 1159 */       if ((paramInt < 0) || (paramInt > this.size))
/* 1160 */         throw new IndexOutOfBoundsException(outOfBoundsMsg(paramInt));
/*      */     }
/*      */ 
/*      */     private String outOfBoundsMsg(int paramInt) {
/* 1164 */       return "Index: " + paramInt + ", Size: " + this.size;
/*      */     }
/*      */ 
/*      */     private void checkForComodification() {
/* 1168 */       if (ArrayList.this.modCount != this.modCount)
/* 1169 */         throw new ConcurrentModificationException();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ArrayList
 * JD-Core Version:    0.6.2
 */