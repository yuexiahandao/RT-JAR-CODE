/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class Vector<E> extends AbstractList<E>
/*      */   implements List<E>, RandomAccess, Cloneable, Serializable
/*      */ {
/*      */   protected Object[] elementData;
/*      */   protected int elementCount;
/*      */   protected int capacityIncrement;
/*      */   private static final long serialVersionUID = -2767605614048989439L;
/*      */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*      */ 
/*      */   public Vector(int paramInt1, int paramInt2)
/*      */   {
/*  128 */     if (paramInt1 < 0) {
/*  129 */       throw new IllegalArgumentException("Illegal Capacity: " + paramInt1);
/*      */     }
/*  131 */     this.elementData = new Object[paramInt1];
/*  132 */     this.capacityIncrement = paramInt2;
/*      */   }
/*      */ 
/*      */   public Vector(int paramInt)
/*      */   {
/*  144 */     this(paramInt, 0);
/*      */   }
/*      */ 
/*      */   public Vector()
/*      */   {
/*  153 */     this(10);
/*      */   }
/*      */ 
/*      */   public Vector(Collection<? extends E> paramCollection)
/*      */   {
/*  167 */     this.elementData = paramCollection.toArray();
/*  168 */     this.elementCount = this.elementData.length;
/*      */ 
/*  170 */     if (this.elementData.getClass() != [Ljava.lang.Object.class)
/*  171 */       this.elementData = Arrays.copyOf(this.elementData, this.elementCount, [Ljava.lang.Object.class);
/*      */   }
/*      */ 
/*      */   public synchronized void copyInto(Object[] paramArrayOfObject)
/*      */   {
/*  188 */     System.arraycopy(this.elementData, 0, paramArrayOfObject, 0, this.elementCount);
/*      */   }
/*      */ 
/*      */   public synchronized void trimToSize()
/*      */   {
/*  200 */     this.modCount += 1;
/*  201 */     int i = this.elementData.length;
/*  202 */     if (this.elementCount < i)
/*  203 */       this.elementData = Arrays.copyOf(this.elementData, this.elementCount);
/*      */   }
/*      */ 
/*      */   public synchronized void ensureCapacity(int paramInt)
/*      */   {
/*  225 */     if (paramInt > 0) {
/*  226 */       this.modCount += 1;
/*  227 */       ensureCapacityHelper(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void ensureCapacityHelper(int paramInt)
/*      */   {
/*  241 */     if (paramInt - this.elementData.length > 0)
/*  242 */       grow(paramInt);
/*      */   }
/*      */ 
/*      */   private void grow(int paramInt)
/*      */   {
/*  255 */     int i = this.elementData.length;
/*  256 */     int j = i + (this.capacityIncrement > 0 ? this.capacityIncrement : i);
/*      */ 
/*  258 */     if (j - paramInt < 0)
/*  259 */       j = paramInt;
/*  260 */     if (j - 2147483639 > 0)
/*  261 */       j = hugeCapacity(paramInt);
/*  262 */     this.elementData = Arrays.copyOf(this.elementData, j);
/*      */   }
/*      */ 
/*      */   private static int hugeCapacity(int paramInt) {
/*  266 */     if (paramInt < 0)
/*  267 */       throw new OutOfMemoryError();
/*  268 */     return paramInt > 2147483639 ? 2147483647 : 2147483639;
/*      */   }
/*      */ 
/*      */   public synchronized void setSize(int paramInt)
/*      */   {
/*  283 */     this.modCount += 1;
/*  284 */     if (paramInt > this.elementCount)
/*  285 */       ensureCapacityHelper(paramInt);
/*      */     else {
/*  287 */       for (int i = paramInt; i < this.elementCount; i++) {
/*  288 */         this.elementData[i] = null;
/*      */       }
/*      */     }
/*  291 */     this.elementCount = paramInt;
/*      */   }
/*      */ 
/*      */   public synchronized int capacity()
/*      */   {
/*  302 */     return this.elementData.length;
/*      */   }
/*      */ 
/*      */   public synchronized int size()
/*      */   {
/*  311 */     return this.elementCount;
/*      */   }
/*      */ 
/*      */   public synchronized boolean isEmpty()
/*      */   {
/*  322 */     return this.elementCount == 0;
/*      */   }
/*      */ 
/*      */   public Enumeration<E> elements()
/*      */   {
/*  335 */     return new Enumeration() {
/*  336 */       int count = 0;
/*      */ 
/*      */       public boolean hasMoreElements() {
/*  339 */         return this.count < Vector.this.elementCount;
/*      */       }
/*      */ 
/*      */       public E nextElement() {
/*  343 */         synchronized (Vector.this) {
/*  344 */           if (this.count < Vector.this.elementCount) {
/*  345 */             return Vector.this.elementData(this.count++);
/*      */           }
/*      */         }
/*  348 */         throw new NoSuchElementException("Vector Enumeration");
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  363 */     return indexOf(paramObject, 0) >= 0;
/*      */   }
/*      */ 
/*      */   public int indexOf(Object paramObject)
/*      */   {
/*  378 */     return indexOf(paramObject, 0);
/*      */   }
/*      */ 
/*      */   public synchronized int indexOf(Object paramObject, int paramInt)
/*      */   {
/*      */     int i;
/*  398 */     if (paramObject == null)
/*  399 */       for (i = paramInt; i < this.elementCount; i++)
/*  400 */         if (this.elementData[i] == null)
/*  401 */           return i;
/*      */     else {
/*  403 */       for (i = paramInt; i < this.elementCount; i++)
/*  404 */         if (paramObject.equals(this.elementData[i]))
/*  405 */           return i;
/*      */     }
/*  407 */     return -1;
/*      */   }
/*      */ 
/*      */   public synchronized int lastIndexOf(Object paramObject)
/*      */   {
/*  422 */     return lastIndexOf(paramObject, this.elementCount - 1);
/*      */   }
/*      */ 
/*      */   public synchronized int lastIndexOf(Object paramObject, int paramInt)
/*      */   {
/*  442 */     if (paramInt >= this.elementCount)
/*  443 */       throw new IndexOutOfBoundsException(paramInt + " >= " + this.elementCount);
/*      */     int i;
/*  445 */     if (paramObject == null)
/*  446 */       for (i = paramInt; i >= 0; i--)
/*  447 */         if (this.elementData[i] == null)
/*  448 */           return i;
/*      */     else {
/*  450 */       for (i = paramInt; i >= 0; i--)
/*  451 */         if (paramObject.equals(this.elementData[i]))
/*  452 */           return i;
/*      */     }
/*  454 */     return -1;
/*      */   }
/*      */ 
/*      */   public synchronized E elementAt(int paramInt)
/*      */   {
/*  469 */     if (paramInt >= this.elementCount) {
/*  470 */       throw new ArrayIndexOutOfBoundsException(paramInt + " >= " + this.elementCount);
/*      */     }
/*      */ 
/*  473 */     return elementData(paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized E firstElement()
/*      */   {
/*  484 */     if (this.elementCount == 0) {
/*  485 */       throw new NoSuchElementException();
/*      */     }
/*  487 */     return elementData(0);
/*      */   }
/*      */ 
/*      */   public synchronized E lastElement()
/*      */   {
/*  498 */     if (this.elementCount == 0) {
/*  499 */       throw new NoSuchElementException();
/*      */     }
/*  501 */     return elementData(this.elementCount - 1);
/*      */   }
/*      */ 
/*      */   public synchronized void setElementAt(E paramE, int paramInt)
/*      */   {
/*  525 */     if (paramInt >= this.elementCount) {
/*  526 */       throw new ArrayIndexOutOfBoundsException(paramInt + " >= " + this.elementCount);
/*      */     }
/*      */ 
/*  529 */     this.elementData[paramInt] = paramE;
/*      */   }
/*      */ 
/*      */   public synchronized void removeElementAt(int paramInt)
/*      */   {
/*  552 */     this.modCount += 1;
/*  553 */     if (paramInt >= this.elementCount) {
/*  554 */       throw new ArrayIndexOutOfBoundsException(paramInt + " >= " + this.elementCount);
/*      */     }
/*      */ 
/*  557 */     if (paramInt < 0) {
/*  558 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/*  560 */     int i = this.elementCount - paramInt - 1;
/*  561 */     if (i > 0) {
/*  562 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*      */     }
/*  564 */     this.elementCount -= 1;
/*  565 */     this.elementData[this.elementCount] = null;
/*      */   }
/*      */ 
/*      */   public synchronized void insertElementAt(E paramE, int paramInt)
/*      */   {
/*  592 */     this.modCount += 1;
/*  593 */     if (paramInt > this.elementCount) {
/*  594 */       throw new ArrayIndexOutOfBoundsException(paramInt + " > " + this.elementCount);
/*      */     }
/*      */ 
/*  597 */     ensureCapacityHelper(this.elementCount + 1);
/*  598 */     System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + 1, this.elementCount - paramInt);
/*  599 */     this.elementData[paramInt] = paramE;
/*  600 */     this.elementCount += 1;
/*      */   }
/*      */ 
/*      */   public synchronized void addElement(E paramE)
/*      */   {
/*  615 */     this.modCount += 1;
/*  616 */     ensureCapacityHelper(this.elementCount + 1);
/*  617 */     this.elementData[(this.elementCount++)] = paramE;
/*      */   }
/*      */ 
/*      */   public synchronized boolean removeElement(Object paramObject)
/*      */   {
/*  636 */     this.modCount += 1;
/*  637 */     int i = indexOf(paramObject);
/*  638 */     if (i >= 0) {
/*  639 */       removeElementAt(i);
/*  640 */       return true;
/*      */     }
/*  642 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized void removeAllElements()
/*      */   {
/*  652 */     this.modCount += 1;
/*      */ 
/*  654 */     for (int i = 0; i < this.elementCount; i++) {
/*  655 */       this.elementData[i] = null;
/*      */     }
/*  657 */     this.elementCount = 0;
/*      */   }
/*      */ 
/*      */   public synchronized Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  670 */       Vector localVector = (Vector)super.clone();
/*  671 */       localVector.elementData = Arrays.copyOf(this.elementData, this.elementCount);
/*  672 */       localVector.modCount = 0;
/*  673 */       return localVector;
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  676 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public synchronized Object[] toArray()
/*      */   {
/*  687 */     return Arrays.copyOf(this.elementData, this.elementCount);
/*      */   }
/*      */ 
/*      */   public synchronized <T> T[] toArray(T[] paramArrayOfT)
/*      */   {
/*  715 */     if (paramArrayOfT.length < this.elementCount) {
/*  716 */       return (Object[])Arrays.copyOf(this.elementData, this.elementCount, paramArrayOfT.getClass());
/*      */     }
/*  718 */     System.arraycopy(this.elementData, 0, paramArrayOfT, 0, this.elementCount);
/*      */ 
/*  720 */     if (paramArrayOfT.length > this.elementCount) {
/*  721 */       paramArrayOfT[this.elementCount] = null;
/*      */     }
/*  723 */     return paramArrayOfT;
/*      */   }
/*      */ 
/*      */   E elementData(int paramInt)
/*      */   {
/*  730 */     return this.elementData[paramInt];
/*      */   }
/*      */ 
/*      */   public synchronized E get(int paramInt)
/*      */   {
/*  743 */     if (paramInt >= this.elementCount) {
/*  744 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/*  746 */     return elementData(paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized E set(int paramInt, E paramE)
/*      */   {
/*  761 */     if (paramInt >= this.elementCount) {
/*  762 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/*  764 */     Object localObject = elementData(paramInt);
/*  765 */     this.elementData[paramInt] = paramE;
/*  766 */     return localObject;
/*      */   }
/*      */ 
/*      */   public synchronized boolean add(E paramE)
/*      */   {
/*  777 */     this.modCount += 1;
/*  778 */     ensureCapacityHelper(this.elementCount + 1);
/*  779 */     this.elementData[(this.elementCount++)] = paramE;
/*  780 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*  795 */     return removeElement(paramObject);
/*      */   }
/*      */ 
/*      */   public void add(int paramInt, E paramE)
/*      */   {
/*  810 */     insertElementAt(paramE, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized E remove(int paramInt)
/*      */   {
/*  825 */     this.modCount += 1;
/*  826 */     if (paramInt >= this.elementCount)
/*  827 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*  828 */     Object localObject = elementData(paramInt);
/*      */ 
/*  830 */     int i = this.elementCount - paramInt - 1;
/*  831 */     if (i > 0) {
/*  832 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*      */     }
/*  834 */     this.elementData[(--this.elementCount)] = null;
/*      */ 
/*  836 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  846 */     removeAllElements();
/*      */   }
/*      */ 
/*      */   public synchronized boolean containsAll(Collection<?> paramCollection)
/*      */   {
/*  862 */     return super.containsAll(paramCollection);
/*      */   }
/*      */ 
/*      */   public synchronized boolean addAll(Collection<? extends E> paramCollection)
/*      */   {
/*  879 */     this.modCount += 1;
/*  880 */     Object[] arrayOfObject = paramCollection.toArray();
/*  881 */     int i = arrayOfObject.length;
/*  882 */     ensureCapacityHelper(this.elementCount + i);
/*  883 */     System.arraycopy(arrayOfObject, 0, this.elementData, this.elementCount, i);
/*  884 */     this.elementCount += i;
/*  885 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public synchronized boolean removeAll(Collection<?> paramCollection)
/*      */   {
/*  906 */     return super.removeAll(paramCollection);
/*      */   }
/*      */ 
/*      */   public synchronized boolean retainAll(Collection<?> paramCollection)
/*      */   {
/*  929 */     return super.retainAll(paramCollection);
/*      */   }
/*      */ 
/*      */   public synchronized boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*      */   {
/*  950 */     this.modCount += 1;
/*  951 */     if ((paramInt < 0) || (paramInt > this.elementCount)) {
/*  952 */       throw new ArrayIndexOutOfBoundsException(paramInt);
/*      */     }
/*  954 */     Object[] arrayOfObject = paramCollection.toArray();
/*  955 */     int i = arrayOfObject.length;
/*  956 */     ensureCapacityHelper(this.elementCount + i);
/*      */ 
/*  958 */     int j = this.elementCount - paramInt;
/*  959 */     if (j > 0) {
/*  960 */       System.arraycopy(this.elementData, paramInt, this.elementData, paramInt + i, j);
/*      */     }
/*      */ 
/*  963 */     System.arraycopy(arrayOfObject, 0, this.elementData, paramInt, i);
/*  964 */     this.elementCount += i;
/*  965 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public synchronized boolean equals(Object paramObject)
/*      */   {
/*  981 */     return super.equals(paramObject);
/*      */   }
/*      */ 
/*      */   public synchronized int hashCode()
/*      */   {
/*  988 */     return super.hashCode();
/*      */   }
/*      */ 
/*      */   public synchronized String toString()
/*      */   {
/*  996 */     return super.toString();
/*      */   }
/*      */ 
/*      */   public synchronized List<E> subList(int paramInt1, int paramInt2)
/*      */   {
/* 1034 */     return Collections.synchronizedList(super.subList(paramInt1, paramInt2), this);
/*      */   }
/*      */ 
/*      */   protected synchronized void removeRange(int paramInt1, int paramInt2)
/*      */   {
/* 1046 */     this.modCount += 1;
/* 1047 */     int i = this.elementCount - paramInt2;
/* 1048 */     System.arraycopy(this.elementData, paramInt2, this.elementData, paramInt1, i);
/*      */ 
/* 1052 */     int j = this.elementCount - (paramInt2 - paramInt1);
/* 1053 */     while (this.elementCount != j)
/* 1054 */       this.elementData[(--this.elementCount)] = null;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1065 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/*      */     Object[] arrayOfObject;
/* 1067 */     synchronized (this) {
/* 1068 */       localPutField.put("capacityIncrement", this.capacityIncrement);
/* 1069 */       localPutField.put("elementCount", this.elementCount);
/* 1070 */       arrayOfObject = (Object[])this.elementData.clone();
/*      */     }
/* 1072 */     localPutField.put("elementData", arrayOfObject);
/* 1073 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   public synchronized ListIterator<E> listIterator(int paramInt)
/*      */   {
/* 1089 */     if ((paramInt < 0) || (paramInt > this.elementCount))
/* 1090 */       throw new IndexOutOfBoundsException("Index: " + paramInt);
/* 1091 */     return new ListItr(paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized ListIterator<E> listIterator()
/*      */   {
/* 1103 */     return new ListItr(0);
/*      */   }
/*      */ 
/*      */   public synchronized Iterator<E> iterator()
/*      */   {
/* 1114 */     return new Itr(null);
/*      */   }
/*      */ 
/*      */   private class Itr
/*      */     implements Iterator<E>
/*      */   {
/*      */     int cursor;
/* 1122 */     int lastRet = -1;
/* 1123 */     int expectedModCount = Vector.this.modCount;
/*      */ 
/*      */     private Itr() {
/*      */     }
/*      */     public boolean hasNext() {
/* 1128 */       return this.cursor != Vector.this.elementCount;
/*      */     }
/*      */ 
/*      */     public E next() {
/* 1132 */       synchronized (Vector.this) {
/* 1133 */         checkForComodification();
/* 1134 */         int i = this.cursor;
/* 1135 */         if (i >= Vector.this.elementCount)
/* 1136 */           throw new NoSuchElementException();
/* 1137 */         this.cursor = (i + 1);
/* 1138 */         return Vector.this.elementData(this.lastRet = i);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1143 */       if (this.lastRet == -1)
/* 1144 */         throw new IllegalStateException();
/* 1145 */       synchronized (Vector.this) {
/* 1146 */         checkForComodification();
/* 1147 */         Vector.this.remove(this.lastRet);
/* 1148 */         this.expectedModCount = Vector.this.modCount;
/*      */       }
/* 1150 */       this.cursor = this.lastRet;
/* 1151 */       this.lastRet = -1;
/*      */     }
/*      */ 
/*      */     final void checkForComodification() {
/* 1155 */       if (Vector.this.modCount != this.expectedModCount)
/* 1156 */         throw new ConcurrentModificationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   final class ListItr extends Vector<E>.Itr
/*      */     implements ListIterator<E>
/*      */   {
/*      */     ListItr(int arg2)
/*      */     {
/* 1165 */       super(null);
/*      */       int i;
/* 1166 */       this.cursor = i;
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious() {
/* 1170 */       return this.cursor != 0;
/*      */     }
/*      */ 
/*      */     public int nextIndex() {
/* 1174 */       return this.cursor;
/*      */     }
/*      */ 
/*      */     public int previousIndex() {
/* 1178 */       return this.cursor - 1;
/*      */     }
/*      */ 
/*      */     public E previous() {
/* 1182 */       synchronized (Vector.this) {
/* 1183 */         checkForComodification();
/* 1184 */         int i = this.cursor - 1;
/* 1185 */         if (i < 0)
/* 1186 */           throw new NoSuchElementException();
/* 1187 */         this.cursor = i;
/* 1188 */         return Vector.this.elementData(this.lastRet = i);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void set(E paramE) {
/* 1193 */       if (this.lastRet == -1)
/* 1194 */         throw new IllegalStateException();
/* 1195 */       synchronized (Vector.this) {
/* 1196 */         checkForComodification();
/* 1197 */         Vector.this.set(this.lastRet, paramE);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void add(E paramE) {
/* 1202 */       int i = this.cursor;
/* 1203 */       synchronized (Vector.this) {
/* 1204 */         checkForComodification();
/* 1205 */         Vector.this.add(i, paramE);
/* 1206 */         this.expectedModCount = Vector.this.modCount;
/*      */       }
/* 1208 */       this.cursor = (i + 1);
/* 1209 */       this.lastRet = -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Vector
 * JD-Core Version:    0.6.2
 */