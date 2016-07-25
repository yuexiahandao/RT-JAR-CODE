/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class CopyOnWriteArrayList<E>
/*      */   implements List<E>, RandomAccess, Cloneable, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 8673264195747942595L;
/*   83 */   final transient ReentrantLock lock = new ReentrantLock();
/*      */   private volatile transient Object[] array;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long lockOffset;
/*      */ 
/*      */   final Object[] getArray()
/*      */   {
/*   93 */     return this.array;
/*      */   }
/*      */ 
/*      */   final void setArray(Object[] paramArrayOfObject)
/*      */   {
/*  100 */     this.array = paramArrayOfObject;
/*      */   }
/*      */ 
/*      */   public CopyOnWriteArrayList()
/*      */   {
/*  107 */     setArray(new Object[0]);
/*      */   }
/*      */ 
/*      */   public CopyOnWriteArrayList(Collection<? extends E> paramCollection)
/*      */   {
/*  119 */     Object[] arrayOfObject = paramCollection.toArray();
/*      */ 
/*  121 */     if (arrayOfObject.getClass() != [Ljava.lang.Object.class)
/*  122 */       arrayOfObject = Arrays.copyOf(arrayOfObject, arrayOfObject.length, [Ljava.lang.Object.class);
/*  123 */     setArray(arrayOfObject);
/*      */   }
/*      */ 
/*      */   public CopyOnWriteArrayList(E[] paramArrayOfE)
/*      */   {
/*  134 */     setArray(Arrays.copyOf(paramArrayOfE, paramArrayOfE.length, [Ljava.lang.Object.class));
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  143 */     return getArray().length;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  152 */     return size() == 0;
/*      */   }
/*      */ 
/*      */   private static boolean eq(Object paramObject1, Object paramObject2)
/*      */   {
/*  159 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*      */   }
/*      */ 
/*      */   private static int indexOf(Object paramObject, Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*      */   {
/*      */     int i;
/*  173 */     if (paramObject == null)
/*  174 */       for (i = paramInt1; i < paramInt2; i++)
/*  175 */         if (paramArrayOfObject[i] == null)
/*  176 */           return i;
/*      */     else {
/*  178 */       for (i = paramInt1; i < paramInt2; i++)
/*  179 */         if (paramObject.equals(paramArrayOfObject[i]))
/*  180 */           return i;
/*      */     }
/*  182 */     return -1;
/*      */   }
/*      */ 
/*      */   private static int lastIndexOf(Object paramObject, Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*      */     int i;
/*  193 */     if (paramObject == null)
/*  194 */       for (i = paramInt; i >= 0; i--)
/*  195 */         if (paramArrayOfObject[i] == null)
/*  196 */           return i;
/*      */     else {
/*  198 */       for (i = paramInt; i >= 0; i--)
/*  199 */         if (paramObject.equals(paramArrayOfObject[i]))
/*  200 */           return i;
/*      */     }
/*  202 */     return -1;
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  215 */     Object[] arrayOfObject = getArray();
/*  216 */     return indexOf(paramObject, arrayOfObject, 0, arrayOfObject.length) >= 0;
/*      */   }
/*      */ 
/*      */   public int indexOf(Object paramObject)
/*      */   {
/*  223 */     Object[] arrayOfObject = getArray();
/*  224 */     return indexOf(paramObject, arrayOfObject, 0, arrayOfObject.length);
/*      */   }
/*      */ 
/*      */   public int indexOf(E paramE, int paramInt)
/*      */   {
/*  243 */     Object[] arrayOfObject = getArray();
/*  244 */     return indexOf(paramE, arrayOfObject, paramInt, arrayOfObject.length);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(Object paramObject)
/*      */   {
/*  251 */     Object[] arrayOfObject = getArray();
/*  252 */     return lastIndexOf(paramObject, arrayOfObject, arrayOfObject.length - 1);
/*      */   }
/*      */ 
/*      */   public int lastIndexOf(E paramE, int paramInt)
/*      */   {
/*  272 */     Object[] arrayOfObject = getArray();
/*  273 */     return lastIndexOf(paramE, arrayOfObject, paramInt);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  284 */       CopyOnWriteArrayList localCopyOnWriteArrayList = (CopyOnWriteArrayList)super.clone();
/*  285 */       localCopyOnWriteArrayList.resetLock();
/*  286 */       return localCopyOnWriteArrayList;
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  289 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/*  307 */     Object[] arrayOfObject = getArray();
/*  308 */     return Arrays.copyOf(arrayOfObject, arrayOfObject.length);
/*      */   }
/*      */ 
/*      */   public <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/*  352 */     Object[] arrayOfObject = getArray();
/*  353 */     int i = arrayOfObject.length;
/*  354 */     if (paramArrayOfT.length < i) {
/*  355 */       return (Object[])Arrays.copyOf(arrayOfObject, i, paramArrayOfT.getClass());
/*      */     }
/*  357 */     System.arraycopy(arrayOfObject, 0, paramArrayOfT, 0, i);
/*  358 */     if (paramArrayOfT.length > i)
/*  359 */       paramArrayOfT[i] = null;
/*  360 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   private E get(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  368 */     return paramArrayOfObject[paramInt];
/*      */   }
/*      */ 
/*      */   public E get(int paramInt)
/*      */   {
/*  377 */     return get(getArray(), paramInt);
/*      */   }
/*      */ 
/*      */   public E set(int paramInt, E paramE)
/*      */   {
/*  387 */     ReentrantLock localReentrantLock = this.lock;
/*  388 */     localReentrantLock.lock();
/*      */     try {
/*  390 */       Object[] arrayOfObject1 = getArray();
/*  391 */       Object localObject1 = get(arrayOfObject1, paramInt);
/*      */ 
/*  393 */       if (localObject1 != paramE) {
/*  394 */         int i = arrayOfObject1.length;
/*  395 */         Object[] arrayOfObject2 = Arrays.copyOf(arrayOfObject1, i);
/*  396 */         arrayOfObject2[paramInt] = paramE;
/*  397 */         setArray(arrayOfObject2);
/*      */       }
/*      */       else {
/*  400 */         setArray(arrayOfObject1);
/*      */       }
/*  402 */       return localObject1;
/*      */     } finally {
/*  404 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean add(E paramE)
/*      */   {
/*  415 */     ReentrantLock localReentrantLock = this.lock;
/*  416 */     localReentrantLock.lock();
/*      */     try {
/*  418 */       Object[] arrayOfObject1 = getArray();
/*  419 */       int i = arrayOfObject1.length;
/*  420 */       Object[] arrayOfObject2 = Arrays.copyOf(arrayOfObject1, i + 1);
/*  421 */       arrayOfObject2[i] = paramE;
/*  422 */       setArray(arrayOfObject2);
/*  423 */       return true;
/*      */     } finally {
/*  425 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void add(int paramInt, E paramE)
/*      */   {
/*  437 */     ReentrantLock localReentrantLock = this.lock;
/*  438 */     localReentrantLock.lock();
/*      */     try {
/*  440 */       Object[] arrayOfObject1 = getArray();
/*  441 */       int i = arrayOfObject1.length;
/*  442 */       if ((paramInt > i) || (paramInt < 0)) {
/*  443 */         throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + i);
/*      */       }
/*      */ 
/*  446 */       int j = i - paramInt;
/*      */       Object[] arrayOfObject2;
/*  447 */       if (j == 0) {
/*  448 */         arrayOfObject2 = Arrays.copyOf(arrayOfObject1, i + 1);
/*      */       } else {
/*  450 */         arrayOfObject2 = new Object[i + 1];
/*  451 */         System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, paramInt);
/*  452 */         System.arraycopy(arrayOfObject1, paramInt, arrayOfObject2, paramInt + 1, j);
/*      */       }
/*      */ 
/*  455 */       arrayOfObject2[paramInt] = paramE;
/*  456 */       setArray(arrayOfObject2);
/*      */     } finally {
/*  458 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public E remove(int paramInt)
/*      */   {
/*  470 */     ReentrantLock localReentrantLock = this.lock;
/*  471 */     localReentrantLock.lock();
/*      */     try {
/*  473 */       Object[] arrayOfObject = getArray();
/*  474 */       int i = arrayOfObject.length;
/*  475 */       Object localObject1 = get(arrayOfObject, paramInt);
/*  476 */       int j = i - paramInt - 1;
/*      */       Object localObject2;
/*  477 */       if (j == 0) {
/*  478 */         setArray(Arrays.copyOf(arrayOfObject, i - 1));
/*      */       } else {
/*  480 */         localObject2 = new Object[i - 1];
/*  481 */         System.arraycopy(arrayOfObject, 0, localObject2, 0, paramInt);
/*  482 */         System.arraycopy(arrayOfObject, paramInt + 1, localObject2, paramInt, j);
/*      */ 
/*  484 */         setArray((Object[])localObject2);
/*      */       }
/*  486 */       return localObject1;
/*      */     } finally {
/*  488 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*  506 */     ReentrantLock localReentrantLock = this.lock;
/*  507 */     localReentrantLock.lock();
/*      */     try {
/*  509 */       Object[] arrayOfObject1 = getArray();
/*  510 */       int i = arrayOfObject1.length;
/*      */       int j;
/*  511 */       if (i != 0)
/*      */       {
/*  514 */         j = i - 1;
/*  515 */         Object[] arrayOfObject2 = new Object[j];
/*      */ 
/*  517 */         for (int k = 0; k < j; k++) {
/*  518 */           if (eq(paramObject, arrayOfObject1[k]))
/*      */           {
/*  520 */             for (int m = k + 1; m < i; m++)
/*  521 */               arrayOfObject2[(m - 1)] = arrayOfObject1[m];
/*  522 */             setArray(arrayOfObject2);
/*  523 */             return 1;
/*      */           }
/*  525 */           arrayOfObject2[k] = arrayOfObject1[k];
/*      */         }
/*      */ 
/*  529 */         if (eq(paramObject, arrayOfObject1[j])) {
/*  530 */           setArray(arrayOfObject2);
/*  531 */           return true;
/*      */         }
/*      */       }
/*  534 */       return 0;
/*      */     } finally {
/*  536 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeRange(int paramInt1, int paramInt2)
/*      */   {
/*  553 */     ReentrantLock localReentrantLock = this.lock;
/*  554 */     localReentrantLock.lock();
/*      */     try {
/*  556 */       Object[] arrayOfObject1 = getArray();
/*  557 */       int i = arrayOfObject1.length;
/*      */ 
/*  559 */       if ((paramInt1 < 0) || (paramInt2 > i) || (paramInt2 < paramInt1))
/*  560 */         throw new IndexOutOfBoundsException();
/*  561 */       int j = i - (paramInt2 - paramInt1);
/*  562 */       int k = i - paramInt2;
/*  563 */       if (k == 0) {
/*  564 */         setArray(Arrays.copyOf(arrayOfObject1, j));
/*      */       } else {
/*  566 */         Object[] arrayOfObject2 = new Object[j];
/*  567 */         System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, paramInt1);
/*  568 */         System.arraycopy(arrayOfObject1, paramInt2, arrayOfObject2, paramInt1, k);
/*      */ 
/*  570 */         setArray(arrayOfObject2);
/*      */       }
/*      */     } finally {
/*  573 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addIfAbsent(E paramE)
/*      */   {
/*  584 */     ReentrantLock localReentrantLock = this.lock;
/*  585 */     localReentrantLock.lock();
/*      */     try
/*      */     {
/*  589 */       Object[] arrayOfObject1 = getArray();
/*  590 */       int i = arrayOfObject1.length;
/*  591 */       Object[] arrayOfObject2 = new Object[i + 1];
/*  592 */       for (int j = 0; j < i; j++) {
/*  593 */         if (eq(paramE, arrayOfObject1[j])) {
/*  594 */           return false;
/*      */         }
/*  596 */         arrayOfObject2[j] = arrayOfObject1[j];
/*      */       }
/*  598 */       arrayOfObject2[i] = paramE;
/*  599 */       setArray(arrayOfObject2);
/*  600 */       return 1;
/*      */     } finally {
/*  602 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean containsAll(Collection<?> paramCollection)
/*      */   {
/*  617 */     Object[] arrayOfObject = getArray();
/*  618 */     int i = arrayOfObject.length;
/*  619 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/*  620 */       if (indexOf(localObject, arrayOfObject, 0, i) < 0)
/*  621 */         return false;
/*      */     }
/*  623 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean removeAll(Collection<?> paramCollection)
/*      */   {
/*  643 */     ReentrantLock localReentrantLock = this.lock;
/*  644 */     localReentrantLock.lock();
/*      */     try {
/*  646 */       Object[] arrayOfObject1 = getArray();
/*  647 */       int i = arrayOfObject1.length;
/*      */       int j;
/*  648 */       if (i != 0)
/*      */       {
/*  650 */         j = 0;
/*  651 */         Object[] arrayOfObject2 = new Object[i];
/*  652 */         for (int k = 0; k < i; k++) {
/*  653 */           Object localObject1 = arrayOfObject1[k];
/*  654 */           if (!paramCollection.contains(localObject1))
/*  655 */             arrayOfObject2[(j++)] = localObject1;
/*      */         }
/*  657 */         if (j != i) {
/*  658 */           setArray(Arrays.copyOf(arrayOfObject2, j));
/*  659 */           return 1;
/*      */         }
/*      */       }
/*  662 */       return 0;
/*      */     } finally {
/*  664 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean retainAll(Collection<?> paramCollection)
/*      */   {
/*  685 */     ReentrantLock localReentrantLock = this.lock;
/*  686 */     localReentrantLock.lock();
/*      */     try {
/*  688 */       Object[] arrayOfObject1 = getArray();
/*  689 */       int i = arrayOfObject1.length;
/*      */       int j;
/*  690 */       if (i != 0)
/*      */       {
/*  692 */         j = 0;
/*  693 */         Object[] arrayOfObject2 = new Object[i];
/*  694 */         for (int k = 0; k < i; k++) {
/*  695 */           Object localObject1 = arrayOfObject1[k];
/*  696 */           if (paramCollection.contains(localObject1))
/*  697 */             arrayOfObject2[(j++)] = localObject1;
/*      */         }
/*  699 */         if (j != i) {
/*  700 */           setArray(Arrays.copyOf(arrayOfObject2, j));
/*  701 */           return 1;
/*      */         }
/*      */       }
/*  704 */       return 0;
/*      */     } finally {
/*  706 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int addAllAbsent(Collection<? extends E> paramCollection)
/*      */   {
/*  722 */     Object[] arrayOfObject1 = paramCollection.toArray();
/*  723 */     if (arrayOfObject1.length == 0)
/*  724 */       return 0;
/*  725 */     Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
/*  726 */     ReentrantLock localReentrantLock = this.lock;
/*  727 */     localReentrantLock.lock();
/*      */     try {
/*  729 */       Object[] arrayOfObject3 = getArray();
/*  730 */       int i = arrayOfObject3.length;
/*  731 */       Object[] arrayOfObject4 = 0;
/*  732 */       for (int j = 0; j < arrayOfObject1.length; j++) {
/*  733 */         Object localObject1 = arrayOfObject1[j];
/*  734 */         if ((indexOf(localObject1, arrayOfObject3, 0, i) < 0) && (indexOf(localObject1, arrayOfObject2, 0, arrayOfObject4) < 0))
/*      */         {
/*  736 */           arrayOfObject2[(arrayOfObject4++)] = localObject1;
/*      */         }
/*      */       }
/*      */       Object[] arrayOfObject5;
/*  738 */       if (arrayOfObject4 > 0) {
/*  739 */         arrayOfObject5 = Arrays.copyOf(arrayOfObject3, i + arrayOfObject4);
/*  740 */         System.arraycopy(arrayOfObject2, 0, arrayOfObject5, i, arrayOfObject4);
/*  741 */         setArray(arrayOfObject5);
/*      */       }
/*  743 */       return arrayOfObject4;
/*      */     } finally {
/*  745 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  754 */     ReentrantLock localReentrantLock = this.lock;
/*  755 */     localReentrantLock.lock();
/*      */     try {
/*  757 */       setArray(new Object[0]);
/*      */     } finally {
/*  759 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection<? extends E> paramCollection)
/*      */   {
/*  774 */     Object[] arrayOfObject1 = paramCollection.toArray();
/*  775 */     if (arrayOfObject1.length == 0)
/*  776 */       return false;
/*  777 */     ReentrantLock localReentrantLock = this.lock;
/*  778 */     localReentrantLock.lock();
/*      */     try {
/*  780 */       Object[] arrayOfObject2 = getArray();
/*  781 */       int i = arrayOfObject2.length;
/*  782 */       Object[] arrayOfObject3 = Arrays.copyOf(arrayOfObject2, i + arrayOfObject1.length);
/*  783 */       System.arraycopy(arrayOfObject1, 0, arrayOfObject3, i, arrayOfObject1.length);
/*  784 */       setArray(arrayOfObject3);
/*  785 */       return true;
/*      */     } finally {
/*  787 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*      */   {
/*  808 */     Object[] arrayOfObject1 = paramCollection.toArray();
/*  809 */     ReentrantLock localReentrantLock = this.lock;
/*  810 */     localReentrantLock.lock();
/*      */     try {
/*  812 */       Object[] arrayOfObject2 = getArray();
/*  813 */       int i = arrayOfObject2.length;
/*  814 */       if ((paramInt > i) || (paramInt < 0)) {
/*  815 */         throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + i);
/*      */       }
/*  817 */       if (arrayOfObject1.length == 0)
/*  818 */         return false;
/*  819 */       int j = i - paramInt;
/*      */       Object[] arrayOfObject3;
/*  821 */       if (j == 0) {
/*  822 */         arrayOfObject3 = Arrays.copyOf(arrayOfObject2, i + arrayOfObject1.length);
/*      */       } else {
/*  824 */         arrayOfObject3 = new Object[i + arrayOfObject1.length];
/*  825 */         System.arraycopy(arrayOfObject2, 0, arrayOfObject3, 0, paramInt);
/*  826 */         System.arraycopy(arrayOfObject2, paramInt, arrayOfObject3, paramInt + arrayOfObject1.length, j);
/*      */       }
/*      */ 
/*  830 */       System.arraycopy(arrayOfObject1, 0, arrayOfObject3, paramInt, arrayOfObject1.length);
/*  831 */       setArray(arrayOfObject3);
/*  832 */       return true;
/*      */     } finally {
/*  834 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  849 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/*  851 */     Object[] arrayOfObject1 = getArray();
/*      */ 
/*  853 */     paramObjectOutputStream.writeInt(arrayOfObject1.length);
/*      */ 
/*  856 */     for (Object localObject : arrayOfObject1)
/*  857 */       paramObjectOutputStream.writeObject(localObject);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  868 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/*  871 */     resetLock();
/*      */ 
/*  874 */     int i = paramObjectInputStream.readInt();
/*  875 */     Object[] arrayOfObject = new Object[i];
/*      */ 
/*  878 */     for (int j = 0; j < i; j++)
/*  879 */       arrayOfObject[j] = paramObjectInputStream.readObject();
/*  880 */     setArray(arrayOfObject);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  894 */     return Arrays.toString(getArray());
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  913 */     if (paramObject == this)
/*  914 */       return true;
/*  915 */     if (!(paramObject instanceof List)) {
/*  916 */       return false;
/*      */     }
/*  918 */     List localList = (List)paramObject;
/*  919 */     Iterator localIterator = localList.iterator();
/*  920 */     Object[] arrayOfObject = getArray();
/*  921 */     int i = arrayOfObject.length;
/*  922 */     for (int j = 0; j < i; j++)
/*  923 */       if ((!localIterator.hasNext()) || (!eq(arrayOfObject[j], localIterator.next())))
/*  924 */         return false;
/*  925 */     if (localIterator.hasNext())
/*  926 */       return false;
/*  927 */     return true;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  938 */     int i = 1;
/*  939 */     Object[] arrayOfObject = getArray();
/*  940 */     int j = arrayOfObject.length;
/*  941 */     for (int k = 0; k < j; k++) {
/*  942 */       Object localObject = arrayOfObject[k];
/*  943 */       i = 31 * i + (localObject == null ? 0 : localObject.hashCode());
/*      */     }
/*  945 */     return i;
/*      */   }
/*      */ 
/*      */   public Iterator<E> iterator()
/*      */   {
/*  959 */     return new COWIterator(getArray(), 0, null);
/*      */   }
/*      */ 
/*      */   public ListIterator<E> listIterator()
/*      */   {
/*  971 */     return new COWIterator(getArray(), 0, null);
/*      */   }
/*      */ 
/*      */   public ListIterator<E> listIterator(int paramInt)
/*      */   {
/*  985 */     Object[] arrayOfObject = getArray();
/*  986 */     int i = arrayOfObject.length;
/*  987 */     if ((paramInt < 0) || (paramInt > i)) {
/*  988 */       throw new IndexOutOfBoundsException("Index: " + paramInt);
/*      */     }
/*  990 */     return new COWIterator(arrayOfObject, paramInt, null);
/*      */   }
/*      */ 
/*      */   public List<E> subList(int paramInt1, int paramInt2)
/*      */   {
/* 1078 */     ReentrantLock localReentrantLock = this.lock;
/* 1079 */     localReentrantLock.lock();
/*      */     try {
/* 1081 */       Object[] arrayOfObject = getArray();
/* 1082 */       int i = arrayOfObject.length;
/* 1083 */       if ((paramInt1 < 0) || (paramInt2 > i) || (paramInt1 > paramInt2))
/* 1084 */         throw new IndexOutOfBoundsException();
/* 1085 */       return new COWSubList(this, paramInt1, paramInt2);
/*      */     } finally {
/* 1087 */       localReentrantLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetLock()
/*      */   {
/* 1326 */     UNSAFE.putObjectVolatile(this, lockOffset, new ReentrantLock());
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try {
/* 1332 */       UNSAFE = Unsafe.getUnsafe();
/* 1333 */       CopyOnWriteArrayList localCopyOnWriteArrayList = CopyOnWriteArrayList.class;
/* 1334 */       lockOffset = UNSAFE.objectFieldOffset(localCopyOnWriteArrayList.getDeclaredField("lock"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1337 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class COWIterator<E>
/*      */     implements ListIterator<E>
/*      */   {
/*      */     private final Object[] snapshot;
/*      */     private int cursor;
/*      */ 
/*      */     private COWIterator(Object[] paramArrayOfObject, int paramInt)
/*      */     {
/* 1000 */       this.cursor = paramInt;
/* 1001 */       this.snapshot = paramArrayOfObject;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1005 */       return this.cursor < this.snapshot.length;
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/* 1009 */       return this.cursor > 0;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/* 1014 */       if (!hasNext())
/* 1015 */         throw new NoSuchElementException();
/* 1016 */       return this.snapshot[(this.cursor++)];
/*      */     }
/*      */ 
/*      */     public E previous()
/*      */     {
/* 1021 */       if (!hasPrevious())
/* 1022 */         throw new NoSuchElementException();
/* 1023 */       return this.snapshot[(--this.cursor)];
/*      */     }
/*      */ 
/*      */     public int nextIndex() {
/* 1027 */       return this.cursor;
/*      */     }
/*      */ 
/*      */     public int previousIndex() {
/* 1031 */       return this.cursor - 1;
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/* 1040 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public void set(E paramE)
/*      */     {
/* 1049 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public void add(E paramE)
/*      */     {
/* 1058 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class COWSubList<E> extends AbstractList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private final CopyOnWriteArrayList<E> l;
/*      */     private final int offset;
/*      */     private int size;
/*      */     private Object[] expectedArray;
/*      */ 
/*      */     COWSubList(CopyOnWriteArrayList<E> paramCopyOnWriteArrayList, int paramInt1, int paramInt2)
/*      */     {
/* 1118 */       this.l = paramCopyOnWriteArrayList;
/* 1119 */       this.expectedArray = this.l.getArray();
/* 1120 */       this.offset = paramInt1;
/* 1121 */       this.size = (paramInt2 - paramInt1);
/*      */     }
/*      */ 
/*      */     private void checkForComodification()
/*      */     {
/* 1126 */       if (this.l.getArray() != this.expectedArray)
/* 1127 */         throw new ConcurrentModificationException();
/*      */     }
/*      */ 
/*      */     private void rangeCheck(int paramInt)
/*      */     {
/* 1132 */       if ((paramInt < 0) || (paramInt >= this.size))
/* 1133 */         throw new IndexOutOfBoundsException("Index: " + paramInt + ",Size: " + this.size);
/*      */     }
/*      */ 
/*      */     public E set(int paramInt, E paramE)
/*      */     {
/* 1138 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1139 */       localReentrantLock.lock();
/*      */       try {
/* 1141 */         rangeCheck(paramInt);
/* 1142 */         checkForComodification();
/* 1143 */         Object localObject1 = this.l.set(paramInt + this.offset, paramE);
/* 1144 */         this.expectedArray = this.l.getArray();
/* 1145 */         return localObject1;
/*      */       } finally {
/* 1147 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public E get(int paramInt) {
/* 1152 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1153 */       localReentrantLock.lock();
/*      */       try {
/* 1155 */         rangeCheck(paramInt);
/* 1156 */         checkForComodification();
/* 1157 */         return this.l.get(paramInt + this.offset);
/*      */       } finally {
/* 1159 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1164 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1165 */       localReentrantLock.lock();
/*      */       try {
/* 1167 */         checkForComodification();
/* 1168 */         return this.size;
/*      */       } finally {
/* 1170 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void add(int paramInt, E paramE) {
/* 1175 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1176 */       localReentrantLock.lock();
/*      */       try {
/* 1178 */         checkForComodification();
/* 1179 */         if ((paramInt < 0) || (paramInt > this.size))
/* 1180 */           throw new IndexOutOfBoundsException();
/* 1181 */         this.l.add(paramInt + this.offset, paramE);
/* 1182 */         this.expectedArray = this.l.getArray();
/* 1183 */         this.size += 1;
/*      */       } finally {
/* 1185 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1190 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1191 */       localReentrantLock.lock();
/*      */       try {
/* 1193 */         checkForComodification();
/* 1194 */         this.l.removeRange(this.offset, this.offset + this.size);
/* 1195 */         this.expectedArray = this.l.getArray();
/* 1196 */         this.size = 0;
/*      */       } finally {
/* 1198 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public E remove(int paramInt) {
/* 1203 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1204 */       localReentrantLock.lock();
/*      */       try {
/* 1206 */         rangeCheck(paramInt);
/* 1207 */         checkForComodification();
/* 1208 */         Object localObject1 = this.l.remove(paramInt + this.offset);
/* 1209 */         this.expectedArray = this.l.getArray();
/* 1210 */         this.size -= 1;
/* 1211 */         return localObject1;
/*      */       } finally {
/* 1213 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/* 1218 */       int i = indexOf(paramObject);
/* 1219 */       if (i == -1)
/* 1220 */         return false;
/* 1221 */       remove(i);
/* 1222 */       return true;
/*      */     }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 1226 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1227 */       localReentrantLock.lock();
/*      */       try {
/* 1229 */         checkForComodification();
/* 1230 */         return new CopyOnWriteArrayList.COWSubListIterator(this.l, 0, this.offset, this.size);
/*      */       } finally {
/* 1232 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public ListIterator<E> listIterator(int paramInt) {
/* 1237 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1238 */       localReentrantLock.lock();
/*      */       try {
/* 1240 */         checkForComodification();
/* 1241 */         if ((paramInt < 0) || (paramInt > this.size)) {
/* 1242 */           throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + this.size);
/*      */         }
/* 1244 */         return new CopyOnWriteArrayList.COWSubListIterator(this.l, paramInt, this.offset, this.size);
/*      */       } finally {
/* 1246 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1251 */       ReentrantLock localReentrantLock = this.l.lock;
/* 1252 */       localReentrantLock.lock();
/*      */       try {
/* 1254 */         checkForComodification();
/* 1255 */         if ((paramInt1 < 0) || (paramInt2 > this.size))
/* 1256 */           throw new IndexOutOfBoundsException();
/* 1257 */         return new COWSubList(this.l, paramInt1 + this.offset, paramInt2 + this.offset);
/*      */       }
/*      */       finally {
/* 1260 */         localReentrantLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class COWSubListIterator<E> implements ListIterator<E> {
/*      */     private final ListIterator<E> i;
/*      */     private final int index;
/*      */     private final int offset;
/*      */     private final int size;
/*      */ 
/*      */     COWSubListIterator(List<E> paramList, int paramInt1, int paramInt2, int paramInt3) {
/* 1275 */       this.index = paramInt1;
/* 1276 */       this.offset = paramInt2;
/* 1277 */       this.size = paramInt3;
/* 1278 */       this.i = paramList.listIterator(paramInt1 + paramInt2);
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1282 */       return nextIndex() < this.size;
/*      */     }
/*      */ 
/*      */     public E next() {
/* 1286 */       if (hasNext()) {
/* 1287 */         return this.i.next();
/*      */       }
/* 1289 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/* 1293 */       return previousIndex() >= 0;
/*      */     }
/*      */ 
/*      */     public E previous() {
/* 1297 */       if (hasPrevious()) {
/* 1298 */         return this.i.previous();
/*      */       }
/* 1300 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     public int nextIndex() {
/* 1304 */       return this.i.nextIndex() - this.offset;
/*      */     }
/*      */ 
/*      */     public int previousIndex() {
/* 1308 */       return this.i.previousIndex() - this.offset;
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1312 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public void set(E paramE) {
/* 1316 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public void add(E paramE) {
/* 1320 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.CopyOnWriteArrayList
 * JD-Core Version:    0.6.2
 */