/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ 
/*      */ public class Collections
/*      */ {
/*      */   private static final int BINARYSEARCH_THRESHOLD = 5000;
/*      */   private static final int REVERSE_THRESHOLD = 18;
/*      */   private static final int SHUFFLE_THRESHOLD = 5;
/*      */   private static final int FILL_THRESHOLD = 25;
/*      */   private static final int ROTATE_THRESHOLD = 100;
/*      */   private static final int COPY_THRESHOLD = 10;
/*      */   private static final int REPLACEALL_THRESHOLD = 11;
/*      */   private static final int INDEXOFSUBLIST_THRESHOLD = 35;
/*      */   private static Random r;
/* 3100 */   public static final Set EMPTY_SET = new EmptySet(null);
/*      */ 
/* 3160 */   public static final List EMPTY_LIST = new EmptyList(null);
/*      */ 
/* 3234 */   public static final Map EMPTY_MAP = new EmptyMap(null);
/*      */ 
/*      */   public static <T extends Comparable<? super T>> void sort(List<T> paramList)
/*      */   {
/*  154 */     Object[] arrayOfObject = paramList.toArray();
/*  155 */     Arrays.sort(arrayOfObject);
/*  156 */     ListIterator localListIterator = paramList.listIterator();
/*  157 */     for (int i = 0; i < arrayOfObject.length; i++) {
/*  158 */       localListIterator.next();
/*  159 */       localListIterator.set((Comparable)arrayOfObject[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static <T> void sort(List<T> paramList, Comparator<? super T> paramComparator)
/*      */   {
/*  216 */     Object[] arrayOfObject = paramList.toArray();
/*  217 */     Arrays.sort(arrayOfObject, paramComparator);
/*  218 */     ListIterator localListIterator = paramList.listIterator();
/*  219 */     for (int i = 0; i < arrayOfObject.length; i++) {
/*  220 */       localListIterator.next();
/*  221 */       localListIterator.set(arrayOfObject[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static <T> int binarySearch(List<? extends Comparable<? super T>> paramList, T paramT)
/*      */   {
/*  258 */     if (((paramList instanceof RandomAccess)) || (paramList.size() < 5000)) {
/*  259 */       return indexedBinarySearch(paramList, paramT);
/*      */     }
/*  261 */     return iteratorBinarySearch(paramList, paramT);
/*      */   }
/*      */ 
/*      */   private static <T> int indexedBinarySearch(List<? extends Comparable<? super T>> paramList, T paramT)
/*      */   {
/*  267 */     int i = 0;
/*  268 */     int j = paramList.size() - 1;
/*      */ 
/*  270 */     while (i <= j) {
/*  271 */       int k = i + j >>> 1;
/*  272 */       Comparable localComparable = (Comparable)paramList.get(k);
/*  273 */       int m = localComparable.compareTo(paramT);
/*      */ 
/*  275 */       if (m < 0)
/*  276 */         i = k + 1;
/*  277 */       else if (m > 0)
/*  278 */         j = k - 1;
/*      */       else
/*  280 */         return k;
/*      */     }
/*  282 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   private static <T> int iteratorBinarySearch(List<? extends Comparable<? super T>> paramList, T paramT)
/*      */   {
/*  288 */     int i = 0;
/*  289 */     int j = paramList.size() - 1;
/*  290 */     ListIterator localListIterator = paramList.listIterator();
/*      */ 
/*  292 */     while (i <= j) {
/*  293 */       int k = i + j >>> 1;
/*  294 */       Comparable localComparable = (Comparable)get(localListIterator, k);
/*  295 */       int m = localComparable.compareTo(paramT);
/*      */ 
/*  297 */       if (m < 0)
/*  298 */         i = k + 1;
/*  299 */       else if (m > 0)
/*  300 */         j = k - 1;
/*      */       else
/*  302 */         return k;
/*      */     }
/*  304 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   private static <T> T get(ListIterator<? extends T> paramListIterator, int paramInt)
/*      */   {
/*  312 */     Object localObject = null;
/*  313 */     int i = paramListIterator.nextIndex();
/*  314 */     if (i <= paramInt)
/*      */       do
/*  316 */         localObject = paramListIterator.next();
/*  317 */       while (i++ < paramInt);
/*      */     else {
/*      */       do {
/*  320 */         localObject = paramListIterator.previous();
/*  321 */         i--; } while (i > paramInt);
/*      */     }
/*  323 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static <T> int binarySearch(List<? extends T> paramList, T paramT, Comparator<? super T> paramComparator)
/*      */   {
/*  361 */     if (paramComparator == null) {
/*  362 */       return binarySearch(paramList, paramT);
/*      */     }
/*  364 */     if (((paramList instanceof RandomAccess)) || (paramList.size() < 5000)) {
/*  365 */       return indexedBinarySearch(paramList, paramT, paramComparator);
/*      */     }
/*  367 */     return iteratorBinarySearch(paramList, paramT, paramComparator);
/*      */   }
/*      */ 
/*      */   private static <T> int indexedBinarySearch(List<? extends T> paramList, T paramT, Comparator<? super T> paramComparator) {
/*  371 */     int i = 0;
/*  372 */     int j = paramList.size() - 1;
/*      */ 
/*  374 */     while (i <= j) {
/*  375 */       int k = i + j >>> 1;
/*  376 */       Object localObject = paramList.get(k);
/*  377 */       int m = paramComparator.compare(localObject, paramT);
/*      */ 
/*  379 */       if (m < 0)
/*  380 */         i = k + 1;
/*  381 */       else if (m > 0)
/*  382 */         j = k - 1;
/*      */       else
/*  384 */         return k;
/*      */     }
/*  386 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   private static <T> int iteratorBinarySearch(List<? extends T> paramList, T paramT, Comparator<? super T> paramComparator) {
/*  390 */     int i = 0;
/*  391 */     int j = paramList.size() - 1;
/*  392 */     ListIterator localListIterator = paramList.listIterator();
/*      */ 
/*  394 */     while (i <= j) {
/*  395 */       int k = i + j >>> 1;
/*  396 */       Object localObject = get(localListIterator, k);
/*  397 */       int m = paramComparator.compare(localObject, paramT);
/*      */ 
/*  399 */       if (m < 0)
/*  400 */         i = k + 1;
/*  401 */       else if (m > 0)
/*  402 */         j = k - 1;
/*      */       else
/*  404 */         return k;
/*      */     }
/*  406 */     return -(i + 1);
/*      */   }
/*      */ 
/*      */   public static void reverse(List<?> paramList)
/*      */   {
/*  422 */     int i = paramList.size();
/*      */     int m;
/*  423 */     if ((i < 18) || ((paramList instanceof RandomAccess))) {
/*  424 */       int j = 0; int k = i >> 1; for (m = i - 1; j < k; m--) {
/*  425 */         swap(paramList, j, m);
/*      */ 
/*  424 */         j++;
/*      */       }
/*      */     } else {
/*  427 */       ListIterator localListIterator1 = paramList.listIterator();
/*  428 */       ListIterator localListIterator2 = paramList.listIterator(i);
/*  429 */       m = 0; for (int n = paramList.size() >> 1; m < n; m++) {
/*  430 */         Object localObject = localListIterator1.next();
/*  431 */         localListIterator1.set(localListIterator2.previous());
/*  432 */         localListIterator2.set(localObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void shuffle(List<?> paramList)
/*      */   {
/*  466 */     Random localRandom = r;
/*  467 */     if (localRandom == null)
/*  468 */       r = localRandom = new Random();
/*  469 */     shuffle(paramList, localRandom);
/*      */   }
/*      */ 
/*      */   public static void shuffle(List<?> paramList, Random paramRandom)
/*      */   {
/*  497 */     int i = paramList.size();
/*  498 */     if ((i < 5) || ((paramList instanceof RandomAccess))) {
/*  499 */       for (int j = i; j > 1; j--)
/*  500 */         swap(paramList, j - 1, paramRandom.nextInt(j));
/*      */     } else {
/*  502 */       Object[] arrayOfObject = paramList.toArray();
/*      */ 
/*  505 */       for (int k = i; k > 1; k--) {
/*  506 */         swap(arrayOfObject, k - 1, paramRandom.nextInt(k));
/*      */       }
/*      */ 
/*  509 */       ListIterator localListIterator = paramList.listIterator();
/*  510 */       for (int m = 0; m < arrayOfObject.length; m++) {
/*  511 */         localListIterator.next();
/*  512 */         localListIterator.set(arrayOfObject[m]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void swap(List<?> paramList, int paramInt1, int paramInt2)
/*      */   {
/*  531 */     List<?> localList = paramList;
/*  532 */     localList.set(paramInt1, localList.set(paramInt2, localList.get(paramInt1)));
/*      */   }
/*      */ 
/*      */   private static void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*      */   {
/*  539 */     Object localObject = paramArrayOfObject[paramInt1];
/*  540 */     paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
/*  541 */     paramArrayOfObject[paramInt2] = localObject;
/*      */   }
/*      */ 
/*      */   public static <T> void fill(List<? super T> paramList, T paramT)
/*      */   {
/*  556 */     int i = paramList.size();
/*      */ 
/*  558 */     if ((i < 25) || ((paramList instanceof RandomAccess))) {
/*  559 */       for (int j = 0; j < i; j++)
/*  560 */         paramList.set(j, paramT);
/*      */     } else {
/*  562 */       ListIterator localListIterator = paramList.listIterator();
/*  563 */       for (int k = 0; k < i; k++) {
/*  564 */         localListIterator.next();
/*  565 */         localListIterator.set(paramT);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static <T> void copy(List<? super T> paramList, List<? extends T> paramList1)
/*      */   {
/*  587 */     int i = paramList1.size();
/*  588 */     if (i > paramList.size()) {
/*  589 */       throw new IndexOutOfBoundsException("Source does not fit in dest");
/*      */     }
/*  591 */     if ((i < 10) || (((paramList1 instanceof RandomAccess)) && ((paramList instanceof RandomAccess))))
/*      */     {
/*  593 */       for (int j = 0; j < i; j++)
/*  594 */         paramList.set(j, paramList1.get(j));
/*      */     } else {
/*  596 */       ListIterator localListIterator1 = paramList.listIterator();
/*  597 */       ListIterator localListIterator2 = paramList1.listIterator();
/*  598 */       for (int k = 0; k < i; k++) {
/*  599 */         localListIterator1.next();
/*  600 */         localListIterator1.set(localListIterator2.next());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static <T,  extends Comparable<? super T>> T min(Collection<? extends T> paramCollection)
/*      */   {
/*  627 */     Iterator localIterator = paramCollection.iterator();
/*  628 */     Object localObject1 = localIterator.next();
/*      */ 
/*  630 */     while (localIterator.hasNext()) {
/*  631 */       Object localObject2 = localIterator.next();
/*  632 */       if (((Comparable)localObject2).compareTo(localObject1) < 0)
/*  633 */         localObject1 = localObject2;
/*      */     }
/*  635 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static <T> T min(Collection<? extends T> paramCollection, Comparator<? super T> paramComparator)
/*      */   {
/*  661 */     if (paramComparator == null) {
/*  662 */       return min(paramCollection);
/*      */     }
/*  664 */     Iterator localIterator = paramCollection.iterator();
/*  665 */     Object localObject1 = localIterator.next();
/*      */ 
/*  667 */     while (localIterator.hasNext()) {
/*  668 */       Object localObject2 = localIterator.next();
/*  669 */       if (paramComparator.compare(localObject2, localObject1) < 0)
/*  670 */         localObject1 = localObject2;
/*      */     }
/*  672 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static <T,  extends Comparable<? super T>> T max(Collection<? extends T> paramCollection)
/*      */   {
/*  697 */     Iterator localIterator = paramCollection.iterator();
/*  698 */     Object localObject1 = localIterator.next();
/*      */ 
/*  700 */     while (localIterator.hasNext()) {
/*  701 */       Object localObject2 = localIterator.next();
/*  702 */       if (((Comparable)localObject2).compareTo(localObject1) > 0)
/*  703 */         localObject1 = localObject2;
/*      */     }
/*  705 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static <T> T max(Collection<? extends T> paramCollection, Comparator<? super T> paramComparator)
/*      */   {
/*  731 */     if (paramComparator == null) {
/*  732 */       return max(paramCollection);
/*      */     }
/*  734 */     Iterator localIterator = paramCollection.iterator();
/*  735 */     Object localObject1 = localIterator.next();
/*      */ 
/*  737 */     while (localIterator.hasNext()) {
/*  738 */       Object localObject2 = localIterator.next();
/*  739 */       if (paramComparator.compare(localObject2, localObject1) > 0)
/*  740 */         localObject1 = localObject2;
/*      */     }
/*  742 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public static void rotate(List<?> paramList, int paramInt)
/*      */   {
/*  801 */     if (((paramList instanceof RandomAccess)) || (paramList.size() < 100))
/*  802 */       rotate1(paramList, paramInt);
/*      */     else
/*  804 */       rotate2(paramList, paramInt);
/*      */   }
/*      */ 
/*      */   private static <T> void rotate1(List<T> paramList, int paramInt) {
/*  808 */     int i = paramList.size();
/*  809 */     if (i == 0)
/*  810 */       return;
/*  811 */     paramInt %= i;
/*  812 */     if (paramInt < 0)
/*  813 */       paramInt += i;
/*  814 */     if (paramInt == 0) {
/*  815 */       return;
/*      */     }
/*  817 */     int j = 0; for (int k = 0; k != i; j++) {
/*  818 */       Object localObject = paramList.get(j);
/*  819 */       int m = j;
/*      */       do {
/*  821 */         m += paramInt;
/*  822 */         if (m >= i)
/*  823 */           m -= i;
/*  824 */         localObject = paramList.set(m, localObject);
/*  825 */         k++;
/*  826 */       }while (m != j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void rotate2(List<?> paramList, int paramInt) {
/*  831 */     int i = paramList.size();
/*  832 */     if (i == 0)
/*  833 */       return;
/*  834 */     int j = -paramInt % i;
/*  835 */     if (j < 0)
/*  836 */       j += i;
/*  837 */     if (j == 0) {
/*  838 */       return;
/*      */     }
/*  840 */     reverse(paramList.subList(0, j));
/*  841 */     reverse(paramList.subList(j, i));
/*  842 */     reverse(paramList);
/*      */   }
/*      */ 
/*      */   public static <T> boolean replaceAll(List<T> paramList, T paramT1, T paramT2)
/*      */   {
/*  864 */     boolean bool = false;
/*  865 */     int i = paramList.size();
/*  866 */     if ((i < 11) || ((paramList instanceof RandomAccess)))
/*      */     {
/*      */       int j;
/*  867 */       if (paramT1 == null) {
/*  868 */         for (j = 0; j < i; j++)
/*  869 */           if (paramList.get(j) == null) {
/*  870 */             paramList.set(j, paramT2);
/*  871 */             bool = true;
/*      */           }
/*      */       }
/*      */       else
/*  875 */         for (j = 0; j < i; j++)
/*  876 */           if (paramT1.equals(paramList.get(j))) {
/*  877 */             paramList.set(j, paramT2);
/*  878 */             bool = true;
/*      */           }
/*      */     }
/*      */     else
/*      */     {
/*  883 */       ListIterator localListIterator = paramList.listIterator();
/*      */       int k;
/*  884 */       if (paramT1 == null) {
/*  885 */         for (k = 0; k < i; k++)
/*  886 */           if (localListIterator.next() == null) {
/*  887 */             localListIterator.set(paramT2);
/*  888 */             bool = true;
/*      */           }
/*      */       }
/*      */       else {
/*  892 */         for (k = 0; k < i; k++) {
/*  893 */           if (paramT1.equals(localListIterator.next())) {
/*  894 */             localListIterator.set(paramT2);
/*  895 */             bool = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  900 */     return bool;
/*      */   }
/*      */ 
/*      */   public static int indexOfSubList(List<?> paramList1, List<?> paramList2)
/*      */   {
/*  924 */     int i = paramList1.size();
/*  925 */     int j = paramList2.size();
/*  926 */     int k = i - j;
/*      */     int n;
/*  928 */     if ((i < 35) || (((paramList1 instanceof RandomAccess)) && ((paramList2 instanceof RandomAccess))))
/*      */     {
/*  931 */       label99: for (int m = 0; m <= k; m++) {
/*  932 */         n = 0; for (int i1 = m; n < j; i1++) {
/*  933 */           if (!eq(paramList2.get(n), paramList1.get(i1)))
/*      */             break label99;
/*  932 */           n++;
/*      */         }
/*      */ 
/*  935 */         return m;
/*      */       }
/*      */     } else {
/*  938 */       ListIterator localListIterator1 = paramList1.listIterator();
/*      */ 
/*  940 */       label199: for (n = 0; n <= k; n++) {
/*  941 */         ListIterator localListIterator2 = paramList2.listIterator();
/*  942 */         for (int i2 = 0; i2 < j; i2++) {
/*  943 */           if (!eq(localListIterator2.next(), localListIterator1.next()))
/*      */           {
/*  945 */             for (int i3 = 0; i3 < i2; i3++)
/*  946 */               localListIterator1.previous();
/*  947 */             break label199;
/*      */           }
/*      */         }
/*  950 */         return n;
/*      */       }
/*      */     }
/*  953 */     return -1;
/*      */   }
/*      */ 
/*      */   public static int lastIndexOfSubList(List<?> paramList1, List<?> paramList2)
/*      */   {
/*  977 */     int i = paramList1.size();
/*  978 */     int j = paramList2.size();
/*  979 */     int k = i - j;
/*      */     int n;
/*  981 */     if ((i < 35) || ((paramList1 instanceof RandomAccess)))
/*      */     {
/*  984 */       label91: for (int m = k; m >= 0; m--) {
/*  985 */         n = 0; for (int i1 = m; n < j; i1++) {
/*  986 */           if (!eq(paramList2.get(n), paramList1.get(i1)))
/*      */             break label91;
/*  985 */           n++;
/*      */         }
/*      */ 
/*  988 */         return m;
/*      */       }
/*      */     } else {
/*  991 */       if (k < 0)
/*  992 */         return -1;
/*  993 */       ListIterator localListIterator1 = paramList1.listIterator(k);
/*      */ 
/*  995 */       label206: for (n = k; n >= 0; n--) {
/*  996 */         ListIterator localListIterator2 = paramList2.listIterator();
/*  997 */         for (int i2 = 0; i2 < j; i2++) {
/*  998 */           if (!eq(localListIterator2.next(), localListIterator1.next())) {
/*  999 */             if (n == 0)
/*      */               break label206;
/* 1001 */             for (int i3 = 0; i3 <= i2 + 1; i3++)
/* 1002 */               localListIterator1.previous();
/* 1001 */             break label206;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1007 */         return n;
/*      */       }
/*      */     }
/* 1010 */     return -1;
/*      */   }
/*      */ 
/*      */   public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> paramCollection)
/*      */   {
/* 1038 */     return new UnmodifiableCollection(paramCollection);
/*      */   }
/*      */ 
/*      */   public static <T> Set<T> unmodifiableSet(Set<? extends T> paramSet)
/*      */   {
/* 1112 */     return new UnmodifiableSet(paramSet);
/*      */   }
/*      */ 
/*      */   public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> paramSortedSet)
/*      */   {
/* 1144 */     return new UnmodifiableSortedSet(paramSortedSet);
/*      */   }
/*      */ 
/*      */   public static <T> List<T> unmodifiableList(List<? extends T> paramList)
/*      */   {
/* 1190 */     return (paramList instanceof RandomAccess) ? new UnmodifiableRandomAccessList(paramList) : new UnmodifiableList(paramList);
/*      */   }
/*      */ 
/*      */   public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/* 1318 */     return new UnmodifiableMap(paramMap);
/*      */   }
/*      */ 
/*      */   public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> paramSortedMap)
/*      */   {
/* 1522 */     return new UnmodifiableSortedMap(paramSortedMap);
/*      */   }
/*      */ 
/*      */   public static <T> Collection<T> synchronizedCollection(Collection<T> paramCollection)
/*      */   {
/* 1588 */     return new SynchronizedCollection(paramCollection);
/*      */   }
/*      */ 
/*      */   static <T> Collection<T> synchronizedCollection(Collection<T> paramCollection, Object paramObject) {
/* 1592 */     return new SynchronizedCollection(paramCollection, paramObject);
/*      */   }
/*      */ 
/*      */   public static <T> Set<T> synchronizedSet(Set<T> paramSet)
/*      */   {
/* 1691 */     return new SynchronizedSet(paramSet);
/*      */   }
/*      */ 
/*      */   static <T> Set<T> synchronizedSet(Set<T> paramSet, Object paramObject) {
/* 1695 */     return new SynchronizedSet(paramSet, paramObject);
/*      */   }
/*      */ 
/*      */   public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> paramSortedSet)
/*      */   {
/* 1761 */     return new SynchronizedSortedSet(paramSortedSet);
/*      */   }
/*      */ 
/*      */   public static <T> List<T> synchronizedList(List<T> paramList)
/*      */   {
/* 1839 */     return (paramList instanceof RandomAccess) ? new SynchronizedRandomAccessList(paramList) : new SynchronizedList(paramList);
/*      */   }
/*      */ 
/*      */   static <T> List<T> synchronizedList(List<T> paramList, Object paramObject)
/*      */   {
/* 1845 */     return (paramList instanceof RandomAccess) ? new SynchronizedRandomAccessList(paramList, paramObject) : new SynchronizedList(paramList, paramObject);
/*      */   }
/*      */ 
/*      */   public static <K, V> Map<K, V> synchronizedMap(Map<K, V> paramMap)
/*      */   {
/* 1999 */     return new SynchronizedMap(paramMap);
/*      */   }
/*      */ 
/*      */   public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> paramSortedMap)
/*      */   {
/* 2140 */     return new SynchronizedSortedMap(paramSortedMap);
/*      */   }
/*      */ 
/*      */   public static <E> Collection<E> checkedCollection(Collection<E> paramCollection, Class<E> paramClass)
/*      */   {
/* 2257 */     return new CheckedCollection(paramCollection, paramClass);
/*      */   }
/*      */ 
/*      */   static <T> T[] zeroLengthArray(Class<T> paramClass)
/*      */   {
/* 2262 */     return (Object[])Array.newInstance(paramClass, 0);
/*      */   }
/*      */ 
/*      */   public static <E> Set<E> checkedSet(Set<E> paramSet, Class<E> paramClass)
/*      */   {
/* 2389 */     return new CheckedSet(paramSet, paramClass);
/*      */   }
/*      */ 
/*      */   public static <E> SortedSet<E> checkedSortedSet(SortedSet<E> paramSortedSet, Class<E> paramClass)
/*      */   {
/* 2435 */     return new CheckedSortedSet(paramSortedSet, paramClass);
/*      */   }
/*      */ 
/*      */   public static <E> List<E> checkedList(List<E> paramList, Class<E> paramClass)
/*      */   {
/* 2494 */     return (paramList instanceof RandomAccess) ? new CheckedRandomAccessList(paramList, paramClass) : new CheckedList(paramList, paramClass);
/*      */   }
/*      */ 
/*      */   public static <K, V> Map<K, V> checkedMap(Map<K, V> paramMap, Class<K> paramClass, Class<V> paramClass1)
/*      */   {
/* 2620 */     return new CheckedMap(paramMap, paramClass, paramClass1);
/*      */   }
/*      */ 
/*      */   public static <K, V> SortedMap<K, V> checkedSortedMap(SortedMap<K, V> paramSortedMap, Class<K> paramClass, Class<V> paramClass1)
/*      */   {
/* 2937 */     return new CheckedSortedMap(paramSortedMap, paramClass, paramClass1);
/*      */   }
/*      */ 
/*      */   public static <T> Iterator<T> emptyIterator()
/*      */   {
/* 2998 */     return EmptyIterator.EMPTY_ITERATOR;
/*      */   }
/*      */ 
/*      */   public static <T> ListIterator<T> emptyListIterator()
/*      */   {
/* 3044 */     return EmptyListIterator.EMPTY_ITERATOR;
/*      */   }
/*      */ 
/*      */   public static <T> Enumeration<T> emptyEnumeration()
/*      */   {
/* 3083 */     return EmptyEnumeration.EMPTY_ENUMERATION;
/*      */   }
/*      */ 
/*      */   public static final <T> Set<T> emptySet()
/*      */   {
/* 3120 */     return EMPTY_SET;
/*      */   }
/*      */ 
/*      */   public static final <T> List<T> emptyList()
/*      */   {
/* 3179 */     return EMPTY_LIST;
/*      */   }
/*      */ 
/*      */   public static final <K, V> Map<K, V> emptyMap()
/*      */   {
/* 3253 */     return EMPTY_MAP;
/*      */   }
/*      */ 
/*      */   public static <T> Set<T> singleton(T paramT)
/*      */   {
/* 3296 */     return new SingletonSet(paramT);
/*      */   }
/*      */ 
/*      */   static <E> Iterator<E> singletonIterator(E paramE) {
/* 3300 */     return new Iterator() {
/* 3301 */       private boolean hasNext = true;
/*      */ 
/* 3303 */       public boolean hasNext() { return this.hasNext; }
/*      */ 
/*      */       public E next() {
/* 3306 */         if (this.hasNext) {
/* 3307 */           this.hasNext = false;
/* 3308 */           return this.val$e;
/*      */         }
/* 3310 */         throw new NoSuchElementException();
/*      */       }
/*      */       public void remove() {
/* 3313 */         throw new UnsupportedOperationException();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static <T> List<T> singletonList(T paramT)
/*      */   {
/* 3349 */     return new SingletonList(paramT);
/*      */   }
/*      */ 
/*      */   public static <K, V> Map<K, V> singletonMap(K paramK, V paramV)
/*      */   {
/* 3391 */     return new SingletonMap(paramK, paramV);
/*      */   }
/*      */ 
/*      */   public static <T> List<T> nCopies(int paramInt, T paramT)
/*      */   {
/* 3463 */     if (paramInt < 0)
/* 3464 */       throw new IllegalArgumentException("List length = " + paramInt);
/* 3465 */     return new CopiesList(paramInt, paramT);
/*      */   }
/*      */ 
/*      */   public static <T> Comparator<T> reverseOrder()
/*      */   {
/* 3563 */     return ReverseComparator.REVERSE_ORDER;
/*      */   }
/*      */ 
/*      */   public static <T> Comparator<T> reverseOrder(Comparator<T> paramComparator)
/*      */   {
/* 3601 */     if (paramComparator == null) {
/* 3602 */       return reverseOrder();
/*      */     }
/* 3604 */     if ((paramComparator instanceof ReverseComparator2)) {
/* 3605 */       return ((ReverseComparator2)paramComparator).cmp;
/*      */     }
/* 3607 */     return new ReverseComparator2(paramComparator);
/*      */   }
/*      */ 
/*      */   public static <T> Enumeration<T> enumeration(Collection<T> paramCollection)
/*      */   {
/* 3657 */     return new Enumeration() {
/* 3658 */       private final Iterator<T> i = this.val$c.iterator();
/*      */ 
/*      */       public boolean hasMoreElements() {
/* 3661 */         return this.i.hasNext();
/*      */       }
/*      */ 
/*      */       public T nextElement() {
/* 3665 */         return this.i.next();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static <T> ArrayList<T> list(Enumeration<T> paramEnumeration)
/*      */   {
/* 3686 */     ArrayList localArrayList = new ArrayList();
/* 3687 */     while (paramEnumeration.hasMoreElements())
/* 3688 */       localArrayList.add(paramEnumeration.nextElement());
/* 3689 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   static boolean eq(Object paramObject1, Object paramObject2)
/*      */   {
/* 3696 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*      */   }
/*      */ 
/*      */   public static int frequency(Collection<?> paramCollection, Object paramObject)
/*      */   {
/* 3712 */     int i = 0;
/*      */     Iterator localIterator;
/*      */     Object localObject;
/* 3713 */     if (paramObject == null)
/* 3714 */       for (localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { localObject = localIterator.next();
/* 3715 */         if (localObject == null)
/* 3716 */           i++; }
/*      */     else
/* 3718 */       for (localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { localObject = localIterator.next();
/* 3719 */         if (paramObject.equals(localObject))
/* 3720 */           i++;
/*      */       }
/* 3722 */     return i;
/*      */   }
/*      */ 
/*      */   public static boolean disjoint(Collection<?> paramCollection1, Collection<?> paramCollection2)
/*      */   {
/* 3766 */     Collection<?> localCollection1 = paramCollection2;
/*      */ 
/* 3772 */     Collection<?> localCollection2 = paramCollection1;
/*      */ 
/* 3779 */     if ((paramCollection1 instanceof Set))
/*      */     {
/* 3782 */       localCollection2 = paramCollection2;
/* 3783 */       localCollection1 = paramCollection1;
/* 3784 */     } else if (!(paramCollection2 instanceof Set))
/*      */     {
/* 3791 */       int i = paramCollection1.size();
/* 3792 */       int j = paramCollection2.size();
/* 3793 */       if ((i == 0) || (j == 0))
/*      */       {
/* 3795 */         return true;
/*      */       }
/*      */ 
/* 3798 */       if (i > j) {
/* 3799 */         localCollection2 = paramCollection2;
/* 3800 */         localCollection1 = paramCollection1;
/*      */       }
/*      */     }
/*      */ 
/* 3804 */     for (Iterator localIterator = localCollection2.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 3805 */       if (localCollection1.contains(localObject))
/*      */       {
/* 3807 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3812 */     return true;
/*      */   }
/*      */ 
/*      */   @SafeVarargs
/*      */   public static <T> boolean addAll(Collection<? super T> paramCollection, T[] paramArrayOfT)
/*      */   {
/* 3843 */     boolean bool = false;
/* 3844 */     for (T ? : paramArrayOfT)
/* 3845 */       bool |= paramCollection.add(?);
/* 3846 */     return bool;
/*      */   }
/*      */ 
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> paramMap)
/*      */   {
/* 3879 */     return new SetFromMap(paramMap);
/*      */   }
/*      */ 
/*      */   public static <T> Queue<T> asLifoQueue(Deque<T> paramDeque)
/*      */   {
/* 3943 */     return new AsLIFOQueue(paramDeque);
/*      */   }
/*      */ 
/*      */   static class AsLIFOQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1802017725587941708L;
/*      */     private final Deque<E> q;
/*      */ 
/*      */     AsLIFOQueue(Deque<E> paramDeque) {
/* 3953 */       this.q = paramDeque; } 
/* 3954 */     public boolean add(E paramE) { this.q.addFirst(paramE); return true; } 
/* 3955 */     public boolean offer(E paramE) { return this.q.offerFirst(paramE); } 
/* 3956 */     public E poll() { return this.q.pollFirst(); } 
/* 3957 */     public E remove() { return this.q.removeFirst(); } 
/* 3958 */     public E peek() { return this.q.peekFirst(); } 
/* 3959 */     public E element() { return this.q.getFirst(); } 
/* 3960 */     public void clear() { this.q.clear(); } 
/* 3961 */     public int size() { return this.q.size(); } 
/* 3962 */     public boolean isEmpty() { return this.q.isEmpty(); } 
/* 3963 */     public boolean contains(Object paramObject) { return this.q.contains(paramObject); } 
/* 3964 */     public boolean remove(Object paramObject) { return this.q.remove(paramObject); } 
/* 3965 */     public Iterator<E> iterator() { return this.q.iterator(); } 
/* 3966 */     public Object[] toArray() { return this.q.toArray(); } 
/* 3967 */     public <T> T[] toArray(T[] paramArrayOfT) { return this.q.toArray(paramArrayOfT); } 
/* 3968 */     public String toString() { return this.q.toString(); } 
/* 3969 */     public boolean containsAll(Collection<?> paramCollection) { return this.q.containsAll(paramCollection); } 
/* 3970 */     public boolean removeAll(Collection<?> paramCollection) { return this.q.removeAll(paramCollection); } 
/* 3971 */     public boolean retainAll(Collection<?> paramCollection) { return this.q.retainAll(paramCollection); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class CheckedCollection<E>
/*      */     implements Collection<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1578914078182001775L;
/*      */     final Collection<E> c;
/*      */     final Class<E> type;
/* 2323 */     private E[] zeroLengthElementArray = null;
/*      */ 
/*      */     void typeCheck(Object paramObject)
/*      */     {
/* 2275 */       if ((paramObject != null) && (!this.type.isInstance(paramObject)))
/* 2276 */         throw new ClassCastException(badElementMsg(paramObject));
/*      */     }
/*      */ 
/*      */     private String badElementMsg(Object paramObject) {
/* 2280 */       return "Attempt to insert " + paramObject.getClass() + " element into collection with element type " + this.type;
/*      */     }
/*      */ 
/*      */     CheckedCollection(Collection<E> paramCollection, Class<E> paramClass)
/*      */     {
/* 2285 */       if ((paramCollection == null) || (paramClass == null))
/* 2286 */         throw new NullPointerException();
/* 2287 */       this.c = paramCollection;
/* 2288 */       this.type = paramClass;
/*      */     }
/*      */     public int size() {
/* 2291 */       return this.c.size(); } 
/* 2292 */     public boolean isEmpty() { return this.c.isEmpty(); } 
/* 2293 */     public boolean contains(Object paramObject) { return this.c.contains(paramObject); } 
/* 2294 */     public Object[] toArray() { return this.c.toArray(); } 
/* 2295 */     public <T> T[] toArray(T[] paramArrayOfT) { return this.c.toArray(paramArrayOfT); } 
/* 2296 */     public String toString() { return this.c.toString(); } 
/* 2297 */     public boolean remove(Object paramObject) { return this.c.remove(paramObject); } 
/* 2298 */     public void clear() { this.c.clear(); }
/*      */ 
/*      */     public boolean containsAll(Collection<?> paramCollection) {
/* 2301 */       return this.c.containsAll(paramCollection);
/*      */     }
/*      */     public boolean removeAll(Collection<?> paramCollection) {
/* 2304 */       return this.c.removeAll(paramCollection);
/*      */     }
/*      */     public boolean retainAll(Collection<?> paramCollection) {
/* 2307 */       return this.c.retainAll(paramCollection);
/*      */     }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 2311 */       final Iterator localIterator = this.c.iterator();
/* 2312 */       return new Iterator() {
/* 2313 */         public boolean hasNext() { return localIterator.hasNext(); } 
/* 2314 */         public E next() { return localIterator.next(); } 
/* 2315 */         public void remove() { localIterator.remove(); } } ;
/*      */     }
/*      */ 
/*      */     public boolean add(E paramE) {
/* 2319 */       typeCheck(paramE);
/* 2320 */       return this.c.add(paramE);
/*      */     }
/*      */ 
/*      */     private E[] zeroLengthElementArray()
/*      */     {
/* 2326 */       return this.zeroLengthElementArray = Collections.zeroLengthArray(this.type); } 
/* 2332 */     Collection<E> checkedCopyOf(Collection<? extends E> paramCollection) { Object[] arrayOfObject1 = null;
/*      */       Object[] arrayOfObject3;
/*      */       int i;
/*      */       int j;
/*      */       try { Object[] arrayOfObject2 = zeroLengthElementArray();
/* 2335 */         arrayOfObject1 = paramCollection.toArray(arrayOfObject2);
/*      */ 
/* 2337 */         if (arrayOfObject1.getClass() != arrayOfObject2.getClass()) {
/* 2338 */           arrayOfObject1 = Arrays.copyOf(arrayOfObject1, arrayOfObject1.length, arrayOfObject2.getClass());
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (ArrayStoreException localArrayStoreException)
/*      */       {
/* 2345 */         arrayOfObject1 = (Object[])paramCollection.toArray().clone();
/* 2346 */         arrayOfObject3 = arrayOfObject1; i = arrayOfObject3.length; j = 0; } for (; j < i; j++) { Object localObject = arrayOfObject3[j];
/* 2347 */         typeCheck(localObject);
/*      */       }
/*      */ 
/* 2350 */       return Arrays.asList(arrayOfObject1);
/*      */     }
/*      */ 
/*      */     public boolean addAll(Collection<? extends E> paramCollection)
/*      */     {
/* 2358 */       return this.c.addAll(checkedCopyOf(paramCollection));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CheckedList<E> extends Collections.CheckedCollection<E>
/*      */     implements List<E>
/*      */   {
/*      */     private static final long serialVersionUID = 65247728283967356L;
/*      */     final List<E> list;
/*      */ 
/*      */     CheckedList(List<E> paramList, Class<E> paramClass)
/*      */     {
/* 2510 */       super(paramClass);
/* 2511 */       this.list = paramList;
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/* 2514 */       return (paramObject == this) || (this.list.equals(paramObject)); } 
/* 2515 */     public int hashCode() { return this.list.hashCode(); } 
/* 2516 */     public E get(int paramInt) { return this.list.get(paramInt); } 
/* 2517 */     public E remove(int paramInt) { return this.list.remove(paramInt); } 
/* 2518 */     public int indexOf(Object paramObject) { return this.list.indexOf(paramObject); } 
/* 2519 */     public int lastIndexOf(Object paramObject) { return this.list.lastIndexOf(paramObject); }
/*      */ 
/*      */     public E set(int paramInt, E paramE) {
/* 2522 */       typeCheck(paramE);
/* 2523 */       return this.list.set(paramInt, paramE);
/*      */     }
/*      */ 
/*      */     public void add(int paramInt, E paramE) {
/* 2527 */       typeCheck(paramE);
/* 2528 */       this.list.add(paramInt, paramE);
/*      */     }
/*      */ 
/*      */     public boolean addAll(int paramInt, Collection<? extends E> paramCollection) {
/* 2532 */       return this.list.addAll(paramInt, checkedCopyOf(paramCollection));
/*      */     }
/* 2534 */     public ListIterator<E> listIterator() { return listIterator(0); }
/*      */ 
/*      */     public ListIterator<E> listIterator(int paramInt) {
/* 2537 */       final ListIterator localListIterator = this.list.listIterator(paramInt);
/*      */ 
/* 2539 */       return new ListIterator() {
/* 2540 */         public boolean hasNext() { return localListIterator.hasNext(); } 
/* 2541 */         public E next() { return localListIterator.next(); } 
/* 2542 */         public boolean hasPrevious() { return localListIterator.hasPrevious(); } 
/* 2543 */         public E previous() { return localListIterator.previous(); } 
/* 2544 */         public int nextIndex() { return localListIterator.nextIndex(); } 
/* 2545 */         public int previousIndex() { return localListIterator.previousIndex(); } 
/* 2546 */         public void remove() { localListIterator.remove(); }
/*      */ 
/*      */         public void set(E paramAnonymousE) {
/* 2549 */           Collections.CheckedList.this.typeCheck(paramAnonymousE);
/* 2550 */           localListIterator.set(paramAnonymousE);
/*      */         }
/*      */ 
/*      */         public void add(E paramAnonymousE) {
/* 2554 */           Collections.CheckedList.this.typeCheck(paramAnonymousE);
/* 2555 */           localListIterator.add(paramAnonymousE);
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 2561 */       return new CheckedList(this.list.subList(paramInt1, paramInt2), this.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CheckedMap<K, V>
/*      */     implements Map<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 5742860141034234728L;
/*      */     private final Map<K, V> m;
/*      */     final Class<K> keyType;
/*      */     final Class<V> valueType;
/* 2701 */     private transient Set<Map.Entry<K, V>> entrySet = null;
/*      */ 
/*      */     private void typeCheck(Object paramObject1, Object paramObject2)
/*      */     {
/* 2637 */       if ((paramObject1 != null) && (!this.keyType.isInstance(paramObject1))) {
/* 2638 */         throw new ClassCastException(badKeyMsg(paramObject1));
/*      */       }
/* 2640 */       if ((paramObject2 != null) && (!this.valueType.isInstance(paramObject2)))
/* 2641 */         throw new ClassCastException(badValueMsg(paramObject2));
/*      */     }
/*      */ 
/*      */     private String badKeyMsg(Object paramObject) {
/* 2645 */       return "Attempt to insert " + paramObject.getClass() + " key into map with key type " + this.keyType;
/*      */     }
/*      */ 
/*      */     private String badValueMsg(Object paramObject)
/*      */     {
/* 2650 */       return "Attempt to insert " + paramObject.getClass() + " value into map with value type " + this.valueType;
/*      */     }
/*      */ 
/*      */     CheckedMap(Map<K, V> paramMap, Class<K> paramClass, Class<V> paramClass1)
/*      */     {
/* 2655 */       if ((paramMap == null) || (paramClass == null) || (paramClass1 == null))
/* 2656 */         throw new NullPointerException();
/* 2657 */       this.m = paramMap;
/* 2658 */       this.keyType = paramClass;
/* 2659 */       this.valueType = paramClass1;
/*      */     }
/*      */     public int size() {
/* 2662 */       return this.m.size(); } 
/* 2663 */     public boolean isEmpty() { return this.m.isEmpty(); } 
/* 2664 */     public boolean containsKey(Object paramObject) { return this.m.containsKey(paramObject); } 
/* 2665 */     public boolean containsValue(Object paramObject) { return this.m.containsValue(paramObject); } 
/* 2666 */     public V get(Object paramObject) { return this.m.get(paramObject); } 
/* 2667 */     public V remove(Object paramObject) { return this.m.remove(paramObject); } 
/* 2668 */     public void clear() { this.m.clear(); } 
/* 2669 */     public Set<K> keySet() { return this.m.keySet(); } 
/* 2670 */     public Collection<V> values() { return this.m.values(); } 
/* 2671 */     public boolean equals(Object paramObject) { return (paramObject == this) || (this.m.equals(paramObject)); } 
/* 2672 */     public int hashCode() { return this.m.hashCode(); } 
/* 2673 */     public String toString() { return this.m.toString(); }
/*      */ 
/*      */     public V put(K paramK, V paramV) {
/* 2676 */       typeCheck(paramK, paramV);
/* 2677 */       return this.m.put(paramK, paramV);
/*      */     }
/*      */ 
/*      */     public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */     {
/* 2687 */       Object[] arrayOfObject = paramMap.entrySet().toArray();
/* 2688 */       ArrayList localArrayList = new ArrayList(arrayOfObject.length);
/* 2689 */       for (Object localObject2 : arrayOfObject) {
/* 2690 */         Map.Entry localEntry2 = (Map.Entry)localObject2;
/* 2691 */         Object localObject3 = localEntry2.getKey();
/* 2692 */         Object localObject4 = localEntry2.getValue();
/* 2693 */         typeCheck(localObject3, localObject4);
/* 2694 */         localArrayList.add(new AbstractMap.SimpleImmutableEntry(localObject3, localObject4));
/*      */       }
/*      */ 
/* 2697 */       for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { Map.Entry localEntry1 = (Map.Entry)((Iterator)???).next();
/* 2698 */         this.m.put(localEntry1.getKey(), localEntry1.getValue());
/*      */       }
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet()
/*      */     {
/* 2704 */       if (this.entrySet == null)
/* 2705 */         this.entrySet = new CheckedEntrySet(this.m.entrySet(), this.valueType);
/* 2706 */       return this.entrySet;
/*      */     }
/*      */ 
/*      */     static class CheckedEntrySet<K, V>
/*      */       implements Set<Map.Entry<K, V>>
/*      */     {
/*      */       private final Set<Map.Entry<K, V>> s;
/*      */       private final Class<V> valueType;
/*      */ 
/*      */       CheckedEntrySet(Set<Map.Entry<K, V>> paramSet, Class<V> paramClass)
/*      */       {
/* 2722 */         this.s = paramSet;
/* 2723 */         this.valueType = paramClass;
/*      */       }
/*      */       public int size() {
/* 2726 */         return this.s.size(); } 
/* 2727 */       public boolean isEmpty() { return this.s.isEmpty(); } 
/* 2728 */       public String toString() { return this.s.toString(); } 
/* 2729 */       public int hashCode() { return this.s.hashCode(); } 
/* 2730 */       public void clear() { this.s.clear(); }
/*      */ 
/*      */       public boolean add(Map.Entry<K, V> paramEntry) {
/* 2733 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       public boolean addAll(Collection<? extends Map.Entry<K, V>> paramCollection) {
/* 2736 */         throw new UnsupportedOperationException();
/*      */       }
/*      */ 
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 2740 */         final Iterator localIterator = this.s.iterator();
/* 2741 */         final Class localClass = this.valueType;
/*      */ 
/* 2743 */         return new Iterator() {
/* 2744 */           public boolean hasNext() { return localIterator.hasNext(); } 
/* 2745 */           public void remove() { localIterator.remove(); }
/*      */ 
/*      */           public Map.Entry<K, V> next() {
/* 2748 */             return Collections.CheckedMap.CheckedEntrySet.checkedEntry((Map.Entry)localIterator.next(), localClass);
/*      */           }
/*      */         };
/*      */       }
/*      */ 
/*      */       public Object[] toArray()
/*      */       {
/* 2755 */         Object[] arrayOfObject1 = this.s.toArray();
/*      */ 
/* 2761 */         Object[] arrayOfObject2 = CheckedEntry.class.isInstance(arrayOfObject1.getClass().getComponentType()) ? arrayOfObject1 : new Object[arrayOfObject1.length];
/*      */ 
/* 2765 */         for (int i = 0; i < arrayOfObject1.length; i++) {
/* 2766 */           arrayOfObject2[i] = checkedEntry((Map.Entry)arrayOfObject1[i], this.valueType);
/*      */         }
/* 2768 */         return arrayOfObject2;
/*      */       }
/*      */ 
/*      */       public <T> T[] toArray(T[] paramArrayOfT)
/*      */       {
/* 2776 */         Object[] arrayOfObject = this.s.toArray(paramArrayOfT.length == 0 ? paramArrayOfT : Arrays.copyOf(paramArrayOfT, 0));
/*      */ 
/* 2778 */         for (int i = 0; i < arrayOfObject.length; i++) {
/* 2779 */           arrayOfObject[i] = checkedEntry((Map.Entry)arrayOfObject[i], this.valueType);
/*      */         }
/* 2781 */         if (arrayOfObject.length > paramArrayOfT.length) {
/* 2782 */           return arrayOfObject;
/*      */         }
/* 2784 */         System.arraycopy(arrayOfObject, 0, paramArrayOfT, 0, arrayOfObject.length);
/* 2785 */         if (paramArrayOfT.length > arrayOfObject.length)
/* 2786 */           paramArrayOfT[arrayOfObject.length] = null;
/* 2787 */         return paramArrayOfT;
/*      */       }
/*      */ 
/*      */       public boolean contains(Object paramObject)
/*      */       {
/* 2797 */         if (!(paramObject instanceof Map.Entry))
/* 2798 */           return false;
/* 2799 */         Map.Entry localEntry = (Map.Entry)paramObject;
/* 2800 */         return this.s.contains((localEntry instanceof CheckedEntry) ? localEntry : checkedEntry(localEntry, this.valueType));
/*      */       }
/*      */ 
/*      */       public boolean containsAll(Collection<?> paramCollection)
/*      */       {
/* 2810 */         for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 2811 */           if (!contains(localObject))
/* 2812 */             return false; }
/* 2813 */         return true;
/*      */       }
/*      */ 
/*      */       public boolean remove(Object paramObject) {
/* 2817 */         if (!(paramObject instanceof Map.Entry))
/* 2818 */           return false;
/* 2819 */         return this.s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry)paramObject));
/*      */       }
/*      */ 
/*      */       public boolean removeAll(Collection<?> paramCollection)
/*      */       {
/* 2824 */         return batchRemove(paramCollection, false);
/*      */       }
/*      */       public boolean retainAll(Collection<?> paramCollection) {
/* 2827 */         return batchRemove(paramCollection, true);
/*      */       }
/*      */       private boolean batchRemove(Collection<?> paramCollection, boolean paramBoolean) {
/* 2830 */         boolean bool = false;
/* 2831 */         Iterator localIterator = iterator();
/* 2832 */         while (localIterator.hasNext()) {
/* 2833 */           if (paramCollection.contains(localIterator.next()) != paramBoolean) {
/* 2834 */             localIterator.remove();
/* 2835 */             bool = true;
/*      */           }
/*      */         }
/* 2838 */         return bool;
/*      */       }
/*      */ 
/*      */       public boolean equals(Object paramObject) {
/* 2842 */         if (paramObject == this)
/* 2843 */           return true;
/* 2844 */         if (!(paramObject instanceof Set))
/* 2845 */           return false;
/* 2846 */         Set localSet = (Set)paramObject;
/* 2847 */         return (localSet.size() == this.s.size()) && (containsAll(localSet));
/*      */       }
/*      */ 
/*      */       static <K, V, T> CheckedEntry<K, V, T> checkedEntry(Map.Entry<K, V> paramEntry, Class<T> paramClass)
/*      */       {
/* 2853 */         return new CheckedEntry(paramEntry, paramClass);
/*      */       }
/*      */ 
/*      */       private static class CheckedEntry<K, V, T>
/*      */         implements Map.Entry<K, V>
/*      */       {
/*      */         private final Map.Entry<K, V> e;
/*      */         private final Class<T> valueType;
/*      */ 
/*      */         CheckedEntry(Map.Entry<K, V> paramEntry, Class<T> paramClass)
/*      */         {
/* 2868 */           this.e = paramEntry;
/* 2869 */           this.valueType = paramClass;
/*      */         }
/*      */         public K getKey() {
/* 2872 */           return this.e.getKey(); } 
/* 2873 */         public V getValue() { return this.e.getValue(); } 
/* 2874 */         public int hashCode() { return this.e.hashCode(); } 
/* 2875 */         public String toString() { return this.e.toString(); }
/*      */ 
/*      */         public V setValue(V paramV) {
/* 2878 */           if ((paramV != null) && (!this.valueType.isInstance(paramV)))
/* 2879 */             throw new ClassCastException(badValueMsg(paramV));
/* 2880 */           return this.e.setValue(paramV);
/*      */         }
/*      */ 
/*      */         private String badValueMsg(Object paramObject) {
/* 2884 */           return "Attempt to insert " + paramObject.getClass() + " value into map with value type " + this.valueType;
/*      */         }
/*      */ 
/*      */         public boolean equals(Object paramObject)
/*      */         {
/* 2889 */           if (paramObject == this)
/* 2890 */             return true;
/* 2891 */           if (!(paramObject instanceof Map.Entry))
/* 2892 */             return false;
/* 2893 */           return this.e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry)paramObject));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CheckedRandomAccessList<E> extends Collections.CheckedList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private static final long serialVersionUID = 1638200125423088369L;
/*      */ 
/*      */     CheckedRandomAccessList(List<E> paramList, Class<E> paramClass)
/*      */     {
/* 2574 */       super(paramClass);
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 2578 */       return new CheckedRandomAccessList(this.list.subList(paramInt1, paramInt2), this.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CheckedSet<E> extends Collections.CheckedCollection<E>
/*      */     implements Set<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 4694047833775013803L;
/*      */ 
/*      */     CheckedSet(Set<E> paramSet, Class<E> paramClass)
/*      */     {
/* 2400 */       super(paramClass);
/*      */     }
/* 2402 */     public boolean equals(Object paramObject) { return (paramObject == this) || (this.c.equals(paramObject)); } 
/* 2403 */     public int hashCode() { return this.c.hashCode(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class CheckedSortedMap<K, V> extends Collections.CheckedMap<K, V>
/*      */     implements SortedMap<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1599671320688067438L;
/*      */     private final SortedMap<K, V> sm;
/*      */ 
/*      */     CheckedSortedMap(SortedMap<K, V> paramSortedMap, Class<K> paramClass, Class<V> paramClass1)
/*      */     {
/* 2952 */       super(paramClass, paramClass1);
/* 2953 */       this.sm = paramSortedMap;
/*      */     }
/*      */     public Comparator<? super K> comparator() {
/* 2956 */       return this.sm.comparator(); } 
/* 2957 */     public K firstKey() { return this.sm.firstKey(); } 
/* 2958 */     public K lastKey() { return this.sm.lastKey(); }
/*      */ 
/*      */     public SortedMap<K, V> subMap(K paramK1, K paramK2) {
/* 2961 */       return Collections.checkedSortedMap(this.sm.subMap(paramK1, paramK2), this.keyType, this.valueType);
/*      */     }
/*      */ 
/*      */     public SortedMap<K, V> headMap(K paramK) {
/* 2965 */       return Collections.checkedSortedMap(this.sm.headMap(paramK), this.keyType, this.valueType);
/*      */     }
/*      */     public SortedMap<K, V> tailMap(K paramK) {
/* 2968 */       return Collections.checkedSortedMap(this.sm.tailMap(paramK), this.keyType, this.valueType);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CheckedSortedSet<E> extends Collections.CheckedSet<E>
/*      */     implements SortedSet<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1599911165492914959L;
/*      */     private final SortedSet<E> ss;
/*      */ 
/*      */     CheckedSortedSet(SortedSet<E> paramSortedSet, Class<E> paramClass)
/*      */     {
/* 2448 */       super(paramClass);
/* 2449 */       this.ss = paramSortedSet;
/*      */     }
/*      */     public Comparator<? super E> comparator() {
/* 2452 */       return this.ss.comparator(); } 
/* 2453 */     public E first() { return this.ss.first(); } 
/* 2454 */     public E last() { return this.ss.last(); }
/*      */ 
/*      */     public SortedSet<E> subSet(E paramE1, E paramE2) {
/* 2457 */       return Collections.checkedSortedSet(this.ss.subSet(paramE1, paramE2), this.type);
/*      */     }
/*      */     public SortedSet<E> headSet(E paramE) {
/* 2460 */       return Collections.checkedSortedSet(this.ss.headSet(paramE), this.type);
/*      */     }
/*      */     public SortedSet<E> tailSet(E paramE) {
/* 2463 */       return Collections.checkedSortedSet(this.ss.tailSet(paramE), this.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CopiesList<E> extends AbstractList<E>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2739099268398711800L;
/*      */     final int n;
/*      */     final E element;
/*      */ 
/*      */     CopiesList(int paramInt, E paramE)
/*      */     {
/* 3481 */       assert (paramInt >= 0);
/* 3482 */       this.n = paramInt;
/* 3483 */       this.element = paramE;
/*      */     }
/*      */ 
/*      */     public int size() {
/* 3487 */       return this.n;
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 3491 */       return (this.n != 0) && (Collections.eq(paramObject, this.element));
/*      */     }
/*      */ 
/*      */     public int indexOf(Object paramObject) {
/* 3495 */       return contains(paramObject) ? 0 : -1;
/*      */     }
/*      */ 
/*      */     public int lastIndexOf(Object paramObject) {
/* 3499 */       return contains(paramObject) ? this.n - 1 : -1;
/*      */     }
/*      */ 
/*      */     public E get(int paramInt) {
/* 3503 */       if ((paramInt < 0) || (paramInt >= this.n)) {
/* 3504 */         throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: " + this.n);
/*      */       }
/* 3506 */       return this.element;
/*      */     }
/*      */ 
/*      */     public Object[] toArray() {
/* 3510 */       Object[] arrayOfObject = new Object[this.n];
/* 3511 */       if (this.element != null)
/* 3512 */         Arrays.fill(arrayOfObject, 0, this.n, this.element);
/* 3513 */       return arrayOfObject;
/*      */     }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 3517 */       int i = this.n;
/* 3518 */       if (paramArrayOfT.length < i) {
/* 3519 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*      */ 
/* 3521 */         if (this.element != null)
/* 3522 */           Arrays.fill(paramArrayOfT, 0, i, this.element);
/*      */       } else {
/* 3524 */         Arrays.fill(paramArrayOfT, 0, i, this.element);
/* 3525 */         if (paramArrayOfT.length > i)
/* 3526 */           paramArrayOfT[i] = null;
/*      */       }
/* 3528 */       return paramArrayOfT;
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 3532 */       if (paramInt1 < 0)
/* 3533 */         throw new IndexOutOfBoundsException("fromIndex = " + paramInt1);
/* 3534 */       if (paramInt2 > this.n)
/* 3535 */         throw new IndexOutOfBoundsException("toIndex = " + paramInt2);
/* 3536 */       if (paramInt1 > paramInt2) {
/* 3537 */         throw new IllegalArgumentException("fromIndex(" + paramInt1 + ") > toIndex(" + paramInt2 + ")");
/*      */       }
/* 3539 */       return new CopiesList(paramInt2 - paramInt1, this.element);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class EmptyEnumeration<E>
/*      */     implements Enumeration<E>
/*      */   {
/* 3087 */     static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration();
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 3090 */       return false; } 
/* 3091 */     public E nextElement() { throw new NoSuchElementException(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class EmptyIterator<E>
/*      */     implements Iterator<E>
/*      */   {
/* 3002 */     static final EmptyIterator<Object> EMPTY_ITERATOR = new EmptyIterator();
/*      */ 
/*      */     public boolean hasNext() {
/* 3005 */       return false; } 
/* 3006 */     public E next() { throw new NoSuchElementException(); } 
/* 3007 */     public void remove() { throw new IllegalStateException(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class EmptyList<E> extends AbstractList<E>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 8842843931221139166L;
/*      */ 
/*      */     public Iterator<E> iterator()
/*      */     {
/* 3191 */       return Collections.emptyIterator();
/*      */     }
/*      */     public ListIterator<E> listIterator() {
/* 3194 */       return Collections.emptyListIterator();
/*      */     }
/*      */     public int size() {
/* 3197 */       return 0; } 
/* 3198 */     public boolean isEmpty() { return true; } 
/*      */     public boolean contains(Object paramObject) {
/* 3200 */       return false; } 
/* 3201 */     public boolean containsAll(Collection<?> paramCollection) { return paramCollection.isEmpty(); } 
/*      */     public Object[] toArray() {
/* 3203 */       return new Object[0];
/*      */     }
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 3206 */       if (paramArrayOfT.length > 0)
/* 3207 */         paramArrayOfT[0] = null;
/* 3208 */       return paramArrayOfT;
/*      */     }
/*      */ 
/*      */     public E get(int paramInt) {
/* 3212 */       throw new IndexOutOfBoundsException("Index: " + paramInt);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 3216 */       return ((paramObject instanceof List)) && (((List)paramObject).isEmpty());
/*      */     }
/*      */     public int hashCode() {
/* 3219 */       return 1;
/*      */     }
/*      */ 
/*      */     private Object readResolve() {
/* 3223 */       return Collections.EMPTY_LIST;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class EmptyListIterator<E> extends Collections.EmptyIterator<E>
/*      */     implements ListIterator<E>
/*      */   {
/* 3051 */     static final EmptyListIterator<Object> EMPTY_ITERATOR = new EmptyListIterator();
/*      */ 
/*      */     private EmptyListIterator()
/*      */     {
/* 3047 */       super();
/*      */     }
/*      */ 
/*      */     public boolean hasPrevious()
/*      */     {
/* 3054 */       return false; } 
/* 3055 */     public E previous() { throw new NoSuchElementException(); } 
/* 3056 */     public int nextIndex() { return 0; } 
/* 3057 */     public int previousIndex() { return -1; } 
/* 3058 */     public void set(E paramE) { throw new IllegalStateException(); } 
/* 3059 */     public void add(E paramE) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private static class EmptyMap<K, V> extends AbstractMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 6428348081105594320L;
/*      */ 
/*      */     public int size()
/*      */     {
/* 3265 */       return 0; } 
/* 3266 */     public boolean isEmpty() { return true; } 
/* 3267 */     public boolean containsKey(Object paramObject) { return false; } 
/* 3268 */     public boolean containsValue(Object paramObject) { return false; } 
/* 3269 */     public V get(Object paramObject) { return null; } 
/* 3270 */     public Set<K> keySet() { return Collections.emptySet(); } 
/* 3271 */     public Collection<V> values() { return Collections.emptySet(); } 
/* 3272 */     public Set<Map.Entry<K, V>> entrySet() { return Collections.emptySet(); }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 3275 */       return ((paramObject instanceof Map)) && (((Map)paramObject).isEmpty());
/*      */     }
/*      */     public int hashCode() {
/* 3278 */       return 0;
/*      */     }
/*      */ 
/*      */     private Object readResolve() {
/* 3282 */       return Collections.EMPTY_MAP;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class EmptySet<E> extends AbstractSet<E>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1582296315990362920L;
/*      */ 
/*      */     public Iterator<E> iterator()
/*      */     {
/* 3132 */       return Collections.emptyIterator();
/*      */     }
/* 3134 */     public int size() { return 0; } 
/* 3135 */     public boolean isEmpty() { return true; } 
/*      */     public boolean contains(Object paramObject) {
/* 3137 */       return false; } 
/* 3138 */     public boolean containsAll(Collection<?> paramCollection) { return paramCollection.isEmpty(); } 
/*      */     public Object[] toArray() {
/* 3140 */       return new Object[0];
/*      */     }
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 3143 */       if (paramArrayOfT.length > 0)
/* 3144 */         paramArrayOfT[0] = null;
/* 3145 */       return paramArrayOfT;
/*      */     }
/*      */ 
/*      */     private Object readResolve()
/*      */     {
/* 3150 */       return Collections.EMPTY_SET;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ReverseComparator
/*      */     implements Comparator<Comparable<Object>>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 7207038068494060240L;
/* 3574 */     static final ReverseComparator REVERSE_ORDER = new ReverseComparator();
/*      */ 
/*      */     public int compare(Comparable<Object> paramComparable1, Comparable<Object> paramComparable2)
/*      */     {
/* 3578 */       return paramComparable2.compareTo(paramComparable1);
/*      */     }
/*      */     private Object readResolve() {
/* 3581 */       return Collections.reverseOrder();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ReverseComparator2<T>
/*      */     implements Comparator<T>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 4374092139857L;
/*      */     final Comparator<T> cmp;
/*      */ 
/*      */     ReverseComparator2(Comparator<T> paramComparator)
/*      */     {
/* 3628 */       assert (paramComparator != null);
/* 3629 */       this.cmp = paramComparator;
/*      */     }
/*      */ 
/*      */     public int compare(T paramT1, T paramT2) {
/* 3633 */       return this.cmp.compare(paramT2, paramT1);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 3637 */       return (paramObject == this) || (((paramObject instanceof ReverseComparator2)) && (this.cmp.equals(((ReverseComparator2)paramObject).cmp)));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 3643 */       return this.cmp.hashCode() ^ 0x80000000;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract interface SelfComparable extends Comparable<SelfComparable>
/*      */   {
/*      */   }
/*      */ 
/*      */   private static class SetFromMap<E> extends AbstractSet<E>
/*      */     implements Set<E>, Serializable
/*      */   {
/*      */     private final Map<E, Boolean> m;
/*      */     private transient Set<E> s;
/*      */     private static final long serialVersionUID = 2454657854757543876L;
/*      */ 
/*      */     SetFromMap(Map<E, Boolean> paramMap)
/*      */     {
/* 3892 */       if (!paramMap.isEmpty())
/* 3893 */         throw new IllegalArgumentException("Map is non-empty");
/* 3894 */       this.m = paramMap;
/* 3895 */       this.s = paramMap.keySet();
/*      */     }
/*      */     public void clear() {
/* 3898 */       this.m.clear(); } 
/* 3899 */     public int size() { return this.m.size(); } 
/* 3900 */     public boolean isEmpty() { return this.m.isEmpty(); } 
/* 3901 */     public boolean contains(Object paramObject) { return this.m.containsKey(paramObject); } 
/* 3902 */     public boolean remove(Object paramObject) { return this.m.remove(paramObject) != null; } 
/* 3903 */     public boolean add(E paramE) { return this.m.put(paramE, Boolean.TRUE) == null; } 
/* 3904 */     public Iterator<E> iterator() { return this.s.iterator(); } 
/* 3905 */     public Object[] toArray() { return this.s.toArray(); } 
/* 3906 */     public <T> T[] toArray(T[] paramArrayOfT) { return this.s.toArray(paramArrayOfT); } 
/* 3907 */     public String toString() { return this.s.toString(); } 
/* 3908 */     public int hashCode() { return this.s.hashCode(); } 
/* 3909 */     public boolean equals(Object paramObject) { return (paramObject == this) || (this.s.equals(paramObject)); } 
/* 3910 */     public boolean containsAll(Collection<?> paramCollection) { return this.s.containsAll(paramCollection); } 
/* 3911 */     public boolean removeAll(Collection<?> paramCollection) { return this.s.removeAll(paramCollection); } 
/* 3912 */     public boolean retainAll(Collection<?> paramCollection) { return this.s.retainAll(paramCollection); }
/*      */ 
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws IOException, ClassNotFoundException
/*      */     {
/* 3920 */       paramObjectInputStream.defaultReadObject();
/* 3921 */       this.s = this.m.keySet();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SingletonList<E> extends AbstractList<E>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3093736618740652951L;
/*      */     private final E element;
/*      */ 
/*      */     SingletonList(E paramE)
/*      */     {
/* 3363 */       this.element = paramE;
/*      */     }
/*      */     public Iterator<E> iterator() {
/* 3366 */       return Collections.singletonIterator(this.element);
/*      */     }
/*      */     public int size() {
/* 3369 */       return 1;
/*      */     }
/* 3371 */     public boolean contains(Object paramObject) { return Collections.eq(paramObject, this.element); }
/*      */ 
/*      */     public E get(int paramInt) {
/* 3374 */       if (paramInt != 0)
/* 3375 */         throw new IndexOutOfBoundsException("Index: " + paramInt + ", Size: 1");
/* 3376 */       return this.element;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SingletonMap<K, V> extends AbstractMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -6979724477215052911L;
/*      */     private final K k;
/*      */     private final V v;
/* 3420 */     private transient Set<K> keySet = null;
/* 3421 */     private transient Set<Map.Entry<K, V>> entrySet = null;
/* 3422 */     private transient Collection<V> values = null;
/*      */ 
/*      */     SingletonMap(K paramK, V paramV)
/*      */     {
/* 3406 */       this.k = paramK;
/* 3407 */       this.v = paramV;
/*      */     }
/*      */     public int size() {
/* 3410 */       return 1;
/*      */     }
/* 3412 */     public boolean isEmpty() { return false; } 
/*      */     public boolean containsKey(Object paramObject) {
/* 3414 */       return Collections.eq(paramObject, this.k);
/*      */     }
/* 3416 */     public boolean containsValue(Object paramObject) { return Collections.eq(paramObject, this.v); } 
/*      */     public V get(Object paramObject) {
/* 3418 */       return Collections.eq(paramObject, this.k) ? this.v : null;
/*      */     }
/*      */ 
/*      */     public Set<K> keySet()
/*      */     {
/* 3425 */       if (this.keySet == null)
/* 3426 */         this.keySet = Collections.singleton(this.k);
/* 3427 */       return this.keySet;
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3431 */       if (this.entrySet == null) {
/* 3432 */         this.entrySet = Collections.singleton(new AbstractMap.SimpleImmutableEntry(this.k, this.v));
/*      */       }
/* 3434 */       return this.entrySet;
/*      */     }
/*      */ 
/*      */     public Collection<V> values() {
/* 3438 */       if (this.values == null)
/* 3439 */         this.values = Collections.singleton(this.v);
/* 3440 */       return this.values;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SingletonSet<E> extends AbstractSet<E>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3193687207550431679L;
/*      */     private final E element;
/*      */ 
/*      */     SingletonSet(E paramE)
/*      */     {
/* 3329 */       this.element = paramE;
/*      */     }
/*      */     public Iterator<E> iterator() {
/* 3332 */       return Collections.singletonIterator(this.element);
/*      */     }
/*      */     public int size() {
/* 3335 */       return 1;
/*      */     }
/* 3337 */     public boolean contains(Object paramObject) { return Collections.eq(paramObject, this.element); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class SynchronizedCollection<E>
/*      */     implements Collection<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3053995032091335093L;
/*      */     final Collection<E> c;
/*      */     final Object mutex;
/*      */ 
/*      */     SynchronizedCollection(Collection<E> paramCollection)
/*      */     {
/* 1605 */       if (paramCollection == null)
/* 1606 */         throw new NullPointerException();
/* 1607 */       this.c = paramCollection;
/* 1608 */       this.mutex = this;
/*      */     }
/*      */     SynchronizedCollection(Collection<E> paramCollection, Object paramObject) {
/* 1611 */       this.c = paramCollection;
/* 1612 */       this.mutex = paramObject;
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1616 */       synchronized (this.mutex) { return this.c.size(); } 
/*      */     }
/*      */ 
/* 1619 */     public boolean isEmpty() { synchronized (this.mutex) { return this.c.isEmpty(); } }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 1622 */       synchronized (this.mutex) { return this.c.contains(paramObject); } 
/*      */     }
/*      */ 
/* 1625 */     public Object[] toArray() { synchronized (this.mutex) { return this.c.toArray(); } }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 1628 */       synchronized (this.mutex) { return this.c.toArray(paramArrayOfT); }
/*      */     }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 1632 */       return this.c.iterator();
/*      */     }
/*      */ 
/*      */     public boolean add(E paramE) {
/* 1636 */       synchronized (this.mutex) { return this.c.add(paramE); } 
/*      */     }
/*      */ 
/* 1639 */     public boolean remove(Object paramObject) { synchronized (this.mutex) { return this.c.remove(paramObject); } }
/*      */ 
/*      */     public boolean containsAll(Collection<?> paramCollection)
/*      */     {
/* 1643 */       synchronized (this.mutex) { return this.c.containsAll(paramCollection); } 
/*      */     }
/*      */ 
/* 1646 */     public boolean addAll(Collection<? extends E> paramCollection) { synchronized (this.mutex) { return this.c.addAll(paramCollection); } }
/*      */ 
/*      */     public boolean removeAll(Collection<?> paramCollection) {
/* 1649 */       synchronized (this.mutex) { return this.c.removeAll(paramCollection); } 
/*      */     }
/*      */ 
/* 1652 */     public boolean retainAll(Collection<?> paramCollection) { synchronized (this.mutex) { return this.c.retainAll(paramCollection); } }
/*      */ 
/*      */     public void clear() {
/* 1655 */       synchronized (this.mutex) { this.c.clear(); } 
/*      */     }
/*      */ 
/* 1658 */     public String toString() { synchronized (this.mutex) { return this.c.toString(); } }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 1661 */       synchronized (this.mutex) { paramObjectOutputStream.defaultWriteObject(); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SynchronizedList<E> extends Collections.SynchronizedCollection<E>
/*      */     implements List<E>
/*      */   {
/*      */     private static final long serialVersionUID = -7754090372962971524L;
/*      */     final List<E> list;
/*      */ 
/*      */     SynchronizedList(List<E> paramList)
/*      */     {
/* 1861 */       super();
/* 1862 */       this.list = paramList;
/*      */     }
/*      */     SynchronizedList(List<E> paramList, Object paramObject) {
/* 1865 */       super(paramObject);
/* 1866 */       this.list = paramList;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1870 */       if (this == paramObject)
/* 1871 */         return true;
/* 1872 */       synchronized (this.mutex) { return this.list.equals(paramObject); } 
/*      */     }
/*      */ 
/* 1875 */     public int hashCode() { synchronized (this.mutex) { return this.list.hashCode(); } }
/*      */ 
/*      */     public E get(int paramInt)
/*      */     {
/* 1879 */       synchronized (this.mutex) { return this.list.get(paramInt); } 
/*      */     }
/*      */ 
/* 1882 */     public E set(int paramInt, E paramE) { synchronized (this.mutex) { return this.list.set(paramInt, paramE); } }
/*      */ 
/*      */     public void add(int paramInt, E paramE) {
/* 1885 */       synchronized (this.mutex) { this.list.add(paramInt, paramE); } 
/*      */     }
/*      */ 
/* 1888 */     public E remove(int paramInt) { synchronized (this.mutex) { return this.list.remove(paramInt); } }
/*      */ 
/*      */     public int indexOf(Object paramObject)
/*      */     {
/* 1892 */       synchronized (this.mutex) { return this.list.indexOf(paramObject); } 
/*      */     }
/*      */ 
/* 1895 */     public int lastIndexOf(Object paramObject) { synchronized (this.mutex) { return this.list.lastIndexOf(paramObject); } }
/*      */ 
/*      */     public boolean addAll(int paramInt, Collection<? extends E> paramCollection)
/*      */     {
/* 1899 */       synchronized (this.mutex) { return this.list.addAll(paramInt, paramCollection); }
/*      */     }
/*      */ 
/*      */     public ListIterator<E> listIterator() {
/* 1903 */       return this.list.listIterator();
/*      */     }
/*      */ 
/*      */     public ListIterator<E> listIterator(int paramInt) {
/* 1907 */       return this.list.listIterator(paramInt);
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1911 */       synchronized (this.mutex) {
/* 1912 */         return new SynchronizedList(this.list.subList(paramInt1, paramInt2), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Object readResolve()
/*      */     {
/* 1930 */       return (this.list instanceof RandomAccess) ? new Collections.SynchronizedRandomAccessList(this.list) : this;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SynchronizedMap<K, V>
/*      */     implements Map<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1978198479659022715L;
/*      */     private final Map<K, V> m;
/*      */     final Object mutex;
/* 2053 */     private transient Set<K> keySet = null;
/* 2054 */     private transient Set<Map.Entry<K, V>> entrySet = null;
/* 2055 */     private transient Collection<V> values = null;
/*      */ 
/*      */     SynchronizedMap(Map<K, V> paramMap)
/*      */     {
/* 2013 */       if (paramMap == null)
/* 2014 */         throw new NullPointerException();
/* 2015 */       this.m = paramMap;
/* 2016 */       this.mutex = this;
/*      */     }
/*      */ 
/*      */     SynchronizedMap(Map<K, V> paramMap, Object paramObject) {
/* 2020 */       this.m = paramMap;
/* 2021 */       this.mutex = paramObject;
/*      */     }
/*      */ 
/*      */     public int size() {
/* 2025 */       synchronized (this.mutex) { return this.m.size(); } 
/*      */     }
/*      */ 
/* 2028 */     public boolean isEmpty() { synchronized (this.mutex) { return this.m.isEmpty(); } }
/*      */ 
/*      */     public boolean containsKey(Object paramObject) {
/* 2031 */       synchronized (this.mutex) { return this.m.containsKey(paramObject); } 
/*      */     }
/*      */ 
/* 2034 */     public boolean containsValue(Object paramObject) { synchronized (this.mutex) { return this.m.containsValue(paramObject); } }
/*      */ 
/*      */     public V get(Object paramObject) {
/* 2037 */       synchronized (this.mutex) { return this.m.get(paramObject); }
/*      */     }
/*      */ 
/*      */     public V put(K paramK, V paramV) {
/* 2041 */       synchronized (this.mutex) { return this.m.put(paramK, paramV); } 
/*      */     }
/*      */ 
/* 2044 */     public V remove(Object paramObject) { synchronized (this.mutex) { return this.m.remove(paramObject); } }
/*      */ 
/*      */     public void putAll(Map<? extends K, ? extends V> paramMap) {
/* 2047 */       synchronized (this.mutex) { this.m.putAll(paramMap); } 
/*      */     }
/*      */ 
/* 2050 */     public void clear() { synchronized (this.mutex) { this.m.clear(); }
/*      */ 
/*      */     }
/*      */ 
/*      */     public Set<K> keySet()
/*      */     {
/* 2058 */       synchronized (this.mutex) {
/* 2059 */         if (this.keySet == null)
/* 2060 */           this.keySet = new Collections.SynchronizedSet(this.m.keySet(), this.mutex);
/* 2061 */         return this.keySet;
/*      */       }
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 2066 */       synchronized (this.mutex) {
/* 2067 */         if (this.entrySet == null)
/* 2068 */           this.entrySet = new Collections.SynchronizedSet(this.m.entrySet(), this.mutex);
/* 2069 */         return this.entrySet;
/*      */       }
/*      */     }
/*      */ 
/*      */     public Collection<V> values() {
/* 2074 */       synchronized (this.mutex) {
/* 2075 */         if (this.values == null)
/* 2076 */           this.values = new Collections.SynchronizedCollection(this.m.values(), this.mutex);
/* 2077 */         return this.values;
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 2082 */       if (this == paramObject)
/* 2083 */         return true;
/* 2084 */       synchronized (this.mutex) { return this.m.equals(paramObject); } 
/*      */     }
/*      */ 
/* 2087 */     public int hashCode() { synchronized (this.mutex) { return this.m.hashCode(); } }
/*      */ 
/*      */     public String toString() {
/* 2090 */       synchronized (this.mutex) { return this.m.toString(); } 
/*      */     }
/*      */ 
/* 2093 */     private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException { synchronized (this.mutex) { paramObjectOutputStream.defaultWriteObject(); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SynchronizedRandomAccessList<E> extends Collections.SynchronizedList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private static final long serialVersionUID = 1530674583602358482L;
/*      */ 
/*      */     SynchronizedRandomAccessList(List<E> paramList)
/*      */     {
/* 1944 */       super();
/*      */     }
/*      */ 
/*      */     SynchronizedRandomAccessList(List<E> paramList, Object paramObject) {
/* 1948 */       super(paramObject);
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1952 */       synchronized (this.mutex) {
/* 1953 */         return new SynchronizedRandomAccessList(this.list.subList(paramInt1, paramInt2), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Object writeReplace()
/*      */     {
/* 1967 */       return new Collections.SynchronizedList(this.list);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SynchronizedSet<E> extends Collections.SynchronizedCollection<E>
/*      */     implements Set<E>
/*      */   {
/*      */     private static final long serialVersionUID = 487447009682186044L;
/*      */ 
/*      */     SynchronizedSet(Set<E> paramSet)
/*      */     {
/* 1707 */       super();
/*      */     }
/*      */     SynchronizedSet(Set<E> paramSet, Object paramObject) {
/* 1710 */       super(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1714 */       if (this == paramObject)
/* 1715 */         return true;
/* 1716 */       synchronized (this.mutex) { return this.c.equals(paramObject); } 
/*      */     }
/*      */ 
/* 1719 */     public int hashCode() { synchronized (this.mutex) { return this.c.hashCode(); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SynchronizedSortedMap<K, V> extends Collections.SynchronizedMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = -8798146769416483793L;
/*      */     private final SortedMap<K, V> sm;
/*      */ 
/*      */     SynchronizedSortedMap(SortedMap<K, V> paramSortedMap)
/*      */     {
/* 2156 */       super();
/* 2157 */       this.sm = paramSortedMap;
/*      */     }
/*      */     SynchronizedSortedMap(SortedMap<K, V> paramSortedMap, Object paramObject) {
/* 2160 */       super(paramObject);
/* 2161 */       this.sm = paramSortedMap;
/*      */     }
/*      */ 
/*      */     public Comparator<? super K> comparator() {
/* 2165 */       synchronized (this.mutex) { return this.sm.comparator(); }
/*      */     }
/*      */ 
/*      */     public SortedMap<K, V> subMap(K paramK1, K paramK2) {
/* 2169 */       synchronized (this.mutex) {
/* 2170 */         return new SynchronizedSortedMap(this.sm.subMap(paramK1, paramK2), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/*      */     public SortedMap<K, V> headMap(K paramK) {
/* 2175 */       synchronized (this.mutex) {
/* 2176 */         return new SynchronizedSortedMap(this.sm.headMap(paramK), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/* 2180 */     public SortedMap<K, V> tailMap(K paramK) { synchronized (this.mutex) {
/* 2181 */         return new SynchronizedSortedMap(this.sm.tailMap(paramK), this.mutex);
/*      */       } }
/*      */ 
/*      */     public K firstKey()
/*      */     {
/* 2186 */       synchronized (this.mutex) { return this.sm.firstKey(); } 
/*      */     }
/*      */ 
/* 2189 */     public K lastKey() { synchronized (this.mutex) { return this.sm.lastKey(); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SynchronizedSortedSet<E> extends Collections.SynchronizedSet<E>
/*      */     implements SortedSet<E>
/*      */   {
/*      */     private static final long serialVersionUID = 8695801310862127406L;
/*      */     private final SortedSet<E> ss;
/*      */ 
/*      */     SynchronizedSortedSet(SortedSet<E> paramSortedSet)
/*      */     {
/* 1776 */       super();
/* 1777 */       this.ss = paramSortedSet;
/*      */     }
/*      */     SynchronizedSortedSet(SortedSet<E> paramSortedSet, Object paramObject) {
/* 1780 */       super(paramObject);
/* 1781 */       this.ss = paramSortedSet;
/*      */     }
/*      */ 
/*      */     public Comparator<? super E> comparator() {
/* 1785 */       synchronized (this.mutex) { return this.ss.comparator(); }
/*      */     }
/*      */ 
/*      */     public SortedSet<E> subSet(E paramE1, E paramE2) {
/* 1789 */       synchronized (this.mutex) {
/* 1790 */         return new SynchronizedSortedSet(this.ss.subSet(paramE1, paramE2), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/*      */     public SortedSet<E> headSet(E paramE) {
/* 1795 */       synchronized (this.mutex) {
/* 1796 */         return new SynchronizedSortedSet(this.ss.headSet(paramE), this.mutex);
/*      */       }
/*      */     }
/*      */ 
/* 1800 */     public SortedSet<E> tailSet(E paramE) { synchronized (this.mutex) {
/* 1801 */         return new SynchronizedSortedSet(this.ss.tailSet(paramE), this.mutex);
/*      */       } }
/*      */ 
/*      */     public E first()
/*      */     {
/* 1806 */       synchronized (this.mutex) { return this.ss.first(); } 
/*      */     }
/*      */ 
/* 1809 */     public E last() { synchronized (this.mutex) { return this.ss.last(); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnmodifiableCollection<E>
/*      */     implements Collection<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1820017752578914078L;
/*      */     final Collection<? extends E> c;
/*      */ 
/*      */     UnmodifiableCollection(Collection<? extends E> paramCollection)
/*      */     {
/* 1050 */       if (paramCollection == null)
/* 1051 */         throw new NullPointerException();
/* 1052 */       this.c = paramCollection;
/*      */     }
/*      */     public int size() {
/* 1055 */       return this.c.size(); } 
/* 1056 */     public boolean isEmpty() { return this.c.isEmpty(); } 
/* 1057 */     public boolean contains(Object paramObject) { return this.c.contains(paramObject); } 
/* 1058 */     public Object[] toArray() { return this.c.toArray(); } 
/* 1059 */     public <T> T[] toArray(T[] paramArrayOfT) { return this.c.toArray(paramArrayOfT); } 
/* 1060 */     public String toString() { return this.c.toString(); }
/*      */ 
/*      */     public Iterator<E> iterator() {
/* 1063 */       return new Iterator() {
/* 1064 */         private final Iterator<? extends E> i = Collections.UnmodifiableCollection.this.c.iterator();
/*      */ 
/* 1066 */         public boolean hasNext() { return this.i.hasNext(); } 
/* 1067 */         public E next() { return this.i.next(); } 
/*      */         public void remove() {
/* 1069 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public boolean add(E paramE) {
/* 1075 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1078 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public boolean containsAll(Collection<?> paramCollection) {
/* 1082 */       return this.c.containsAll(paramCollection);
/*      */     }
/*      */     public boolean addAll(Collection<? extends E> paramCollection) {
/* 1085 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public boolean removeAll(Collection<?> paramCollection) {
/* 1088 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public boolean retainAll(Collection<?> paramCollection) {
/* 1091 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void clear() {
/* 1094 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnmodifiableList<E> extends Collections.UnmodifiableCollection<E>
/*      */     implements List<E>
/*      */   {
/*      */     private static final long serialVersionUID = -283967356065247728L;
/*      */     final List<? extends E> list;
/*      */ 
/*      */     UnmodifiableList(List<? extends E> paramList)
/*      */     {
/* 1204 */       super();
/* 1205 */       this.list = paramList;
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/* 1208 */       return (paramObject == this) || (this.list.equals(paramObject)); } 
/* 1209 */     public int hashCode() { return this.list.hashCode(); } 
/*      */     public E get(int paramInt) {
/* 1211 */       return this.list.get(paramInt);
/*      */     }
/* 1213 */     public E set(int paramInt, E paramE) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */     public void add(int paramInt, E paramE) {
/* 1216 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public E remove(int paramInt) {
/* 1219 */       throw new UnsupportedOperationException();
/*      */     }
/* 1221 */     public int indexOf(Object paramObject) { return this.list.indexOf(paramObject); } 
/* 1222 */     public int lastIndexOf(Object paramObject) { return this.list.lastIndexOf(paramObject); } 
/*      */     public boolean addAll(int paramInt, Collection<? extends E> paramCollection) {
/* 1224 */       throw new UnsupportedOperationException();
/*      */     }
/* 1226 */     public ListIterator<E> listIterator() { return listIterator(0); }
/*      */ 
/*      */     public ListIterator<E> listIterator(final int paramInt) {
/* 1229 */       return new ListIterator() {
/* 1230 */         private final ListIterator<? extends E> i = Collections.UnmodifiableList.this.list.listIterator(paramInt);
/*      */ 
/*      */         public boolean hasNext() {
/* 1233 */           return this.i.hasNext(); } 
/* 1234 */         public E next() { return this.i.next(); } 
/* 1235 */         public boolean hasPrevious() { return this.i.hasPrevious(); } 
/* 1236 */         public E previous() { return this.i.previous(); } 
/* 1237 */         public int nextIndex() { return this.i.nextIndex(); } 
/* 1238 */         public int previousIndex() { return this.i.previousIndex(); }
/*      */ 
/*      */         public void remove() {
/* 1241 */           throw new UnsupportedOperationException();
/*      */         }
/*      */         public void set(E paramAnonymousE) {
/* 1244 */           throw new UnsupportedOperationException();
/*      */         }
/*      */         public void add(E paramAnonymousE) {
/* 1247 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1253 */       return new UnmodifiableList(this.list.subList(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     private Object readResolve()
/*      */     {
/* 1269 */       return (this.list instanceof RandomAccess) ? new Collections.UnmodifiableRandomAccessList(this.list) : this;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class UnmodifiableMap<K, V>
/*      */     implements Map<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -1034234728574286014L;
/*      */     private final Map<? extends K, ? extends V> m;
/* 1354 */     private transient Set<K> keySet = null;
/* 1355 */     private transient Set<Map.Entry<K, V>> entrySet = null;
/* 1356 */     private transient Collection<V> values = null;
/*      */ 
/*      */     UnmodifiableMap(Map<? extends K, ? extends V> paramMap)
/*      */     {
/* 1330 */       if (paramMap == null)
/* 1331 */         throw new NullPointerException();
/* 1332 */       this.m = paramMap;
/*      */     }
/*      */     public int size() {
/* 1335 */       return this.m.size(); } 
/* 1336 */     public boolean isEmpty() { return this.m.isEmpty(); } 
/* 1337 */     public boolean containsKey(Object paramObject) { return this.m.containsKey(paramObject); } 
/* 1338 */     public boolean containsValue(Object paramObject) { return this.m.containsValue(paramObject); } 
/* 1339 */     public V get(Object paramObject) { return this.m.get(paramObject); }
/*      */ 
/*      */     public V put(K paramK, V paramV) {
/* 1342 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public V remove(Object paramObject) {
/* 1345 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void putAll(Map<? extends K, ? extends V> paramMap) {
/* 1348 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void clear() {
/* 1351 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     public Set<K> keySet()
/*      */     {
/* 1359 */       if (this.keySet == null)
/* 1360 */         this.keySet = Collections.unmodifiableSet(this.m.keySet());
/* 1361 */       return this.keySet;
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1365 */       if (this.entrySet == null)
/* 1366 */         this.entrySet = new UnmodifiableEntrySet(this.m.entrySet());
/* 1367 */       return this.entrySet;
/*      */     }
/*      */ 
/*      */     public Collection<V> values() {
/* 1371 */       if (this.values == null)
/* 1372 */         this.values = Collections.unmodifiableCollection(this.m.values());
/* 1373 */       return this.values;
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/* 1376 */       return (paramObject == this) || (this.m.equals(paramObject)); } 
/* 1377 */     public int hashCode() { return this.m.hashCode(); } 
/* 1378 */     public String toString() { return this.m.toString(); }
/*      */ 
/*      */ 
/*      */     static class UnmodifiableEntrySet<K, V> extends Collections.UnmodifiableSet<Map.Entry<K, V>>
/*      */     {
/*      */       private static final long serialVersionUID = 7854390611657943733L;
/*      */ 
/*      */       UnmodifiableEntrySet(Set<? extends Map.Entry<? extends K, ? extends V>> paramSet)
/*      */       {
/* 1393 */         super();
/*      */       }
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 1396 */         return new Iterator() {
/* 1397 */           private final Iterator<? extends Map.Entry<? extends K, ? extends V>> i = Collections.UnmodifiableMap.UnmodifiableEntrySet.this.c.iterator();
/*      */ 
/*      */           public boolean hasNext() {
/* 1400 */             return this.i.hasNext();
/*      */           }
/*      */           public Map.Entry<K, V> next() {
/* 1403 */             return new Collections.UnmodifiableMap.UnmodifiableEntrySet.UnmodifiableEntry((Map.Entry)this.i.next());
/*      */           }
/*      */           public void remove() {
/* 1406 */             throw new UnsupportedOperationException();
/*      */           }
/*      */         };
/*      */       }
/*      */ 
/*      */       public Object[] toArray() {
/* 1412 */         Object[] arrayOfObject = this.c.toArray();
/* 1413 */         for (int i = 0; i < arrayOfObject.length; i++)
/* 1414 */           arrayOfObject[i] = new UnmodifiableEntry((Map.Entry)arrayOfObject[i]);
/* 1415 */         return arrayOfObject;
/*      */       }
/*      */ 
/*      */       public <T> T[] toArray(T[] paramArrayOfT)
/*      */       {
/* 1422 */         Object[] arrayOfObject = this.c.toArray(paramArrayOfT.length == 0 ? paramArrayOfT : Arrays.copyOf(paramArrayOfT, 0));
/*      */ 
/* 1424 */         for (int i = 0; i < arrayOfObject.length; i++) {
/* 1425 */           arrayOfObject[i] = new UnmodifiableEntry((Map.Entry)arrayOfObject[i]);
/*      */         }
/* 1427 */         if (arrayOfObject.length > paramArrayOfT.length) {
/* 1428 */           return (Object[])arrayOfObject;
/*      */         }
/* 1430 */         System.arraycopy(arrayOfObject, 0, paramArrayOfT, 0, arrayOfObject.length);
/* 1431 */         if (paramArrayOfT.length > arrayOfObject.length)
/* 1432 */           paramArrayOfT[arrayOfObject.length] = null;
/* 1433 */         return paramArrayOfT;
/*      */       }
/*      */ 
/*      */       public boolean contains(Object paramObject)
/*      */       {
/* 1443 */         if (!(paramObject instanceof Map.Entry))
/* 1444 */           return false;
/* 1445 */         return this.c.contains(new UnmodifiableEntry((Map.Entry)paramObject));
/*      */       }
/*      */ 
/*      */       public boolean containsAll(Collection<?> paramCollection)
/*      */       {
/* 1455 */         for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1456 */           if (!contains(localObject))
/* 1457 */             return false;
/*      */         }
/* 1459 */         return true;
/*      */       }
/*      */       public boolean equals(Object paramObject) {
/* 1462 */         if (paramObject == this) {
/* 1463 */           return true;
/*      */         }
/* 1465 */         if (!(paramObject instanceof Set))
/* 1466 */           return false;
/* 1467 */         Set localSet = (Set)paramObject;
/* 1468 */         if (localSet.size() != this.c.size())
/* 1469 */           return false;
/* 1470 */         return containsAll(localSet);
/*      */       }
/*      */ 
/*      */       private static class UnmodifiableEntry<K, V>
/*      */         implements Map.Entry<K, V>
/*      */       {
/*      */         private Map.Entry<? extends K, ? extends V> e;
/*      */ 
/*      */         UnmodifiableEntry(Map.Entry<? extends K, ? extends V> paramEntry)
/*      */         {
/* 1483 */           this.e = paramEntry;
/*      */         }
/* 1485 */         public K getKey() { return this.e.getKey(); } 
/* 1486 */         public V getValue() { return this.e.getValue(); } 
/*      */         public V setValue(V paramV) {
/* 1488 */           throw new UnsupportedOperationException();
/*      */         }
/* 1490 */         public int hashCode() { return this.e.hashCode(); } 
/*      */         public boolean equals(Object paramObject) {
/* 1492 */           if (this == paramObject)
/* 1493 */             return true;
/* 1494 */           if (!(paramObject instanceof Map.Entry))
/* 1495 */             return false;
/* 1496 */           Map.Entry localEntry = (Map.Entry)paramObject;
/* 1497 */           return (Collections.eq(this.e.getKey(), localEntry.getKey())) && (Collections.eq(this.e.getValue(), localEntry.getValue()));
/*      */         }
/*      */         public String toString() {
/* 1500 */           return this.e.toString();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnmodifiableRandomAccessList<E> extends Collections.UnmodifiableList<E>
/*      */     implements RandomAccess
/*      */   {
/*      */     private static final long serialVersionUID = -2542308836966382001L;
/*      */ 
/*      */     UnmodifiableRandomAccessList(List<? extends E> paramList)
/*      */     {
/* 1282 */       super();
/*      */     }
/*      */ 
/*      */     public List<E> subList(int paramInt1, int paramInt2) {
/* 1286 */       return new UnmodifiableRandomAccessList(this.list.subList(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*      */     private Object writeReplace()
/*      */     {
/* 1299 */       return new Collections.UnmodifiableList(this.list);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnmodifiableSet<E> extends Collections.UnmodifiableCollection<E>
/*      */     implements Set<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -9215047833775013803L;
/*      */ 
/*      */     UnmodifiableSet(Set<? extends E> paramSet)
/*      */     {
/* 1122 */       super(); } 
/* 1123 */     public boolean equals(Object paramObject) { return (paramObject == this) || (this.c.equals(paramObject)); } 
/* 1124 */     public int hashCode() { return this.c.hashCode(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class UnmodifiableSortedMap<K, V> extends Collections.UnmodifiableMap<K, V>
/*      */     implements SortedMap<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -8806743815996713206L;
/*      */     private final SortedMap<K, ? extends V> sm;
/*      */ 
/*      */     UnmodifiableSortedMap(SortedMap<K, ? extends V> paramSortedMap)
/*      */     {
/* 1535 */       super(); this.sm = paramSortedMap;
/*      */     }
/* 1537 */     public Comparator<? super K> comparator() { return this.sm.comparator(); }
/*      */ 
/*      */     public SortedMap<K, V> subMap(K paramK1, K paramK2) {
/* 1540 */       return new UnmodifiableSortedMap(this.sm.subMap(paramK1, paramK2));
/*      */     }
/*      */     public SortedMap<K, V> headMap(K paramK) {
/* 1543 */       return new UnmodifiableSortedMap(this.sm.headMap(paramK));
/*      */     }
/*      */     public SortedMap<K, V> tailMap(K paramK) {
/* 1546 */       return new UnmodifiableSortedMap(this.sm.tailMap(paramK));
/*      */     }
/*      */     public K firstKey() {
/* 1549 */       return this.sm.firstKey(); } 
/* 1550 */     public K lastKey() { return this.sm.lastKey(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static class UnmodifiableSortedSet<E> extends Collections.UnmodifiableSet<E>
/*      */     implements SortedSet<E>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -4929149591599911165L;
/*      */     private final SortedSet<E> ss;
/*      */ 
/*      */     UnmodifiableSortedSet(SortedSet<E> paramSortedSet)
/*      */     {
/* 1156 */       super(); this.ss = paramSortedSet;
/*      */     }
/* 1158 */     public Comparator<? super E> comparator() { return this.ss.comparator(); }
/*      */ 
/*      */     public SortedSet<E> subSet(E paramE1, E paramE2) {
/* 1161 */       return new UnmodifiableSortedSet(this.ss.subSet(paramE1, paramE2));
/*      */     }
/*      */     public SortedSet<E> headSet(E paramE) {
/* 1164 */       return new UnmodifiableSortedSet(this.ss.headSet(paramE));
/*      */     }
/*      */     public SortedSet<E> tailSet(E paramE) {
/* 1167 */       return new UnmodifiableSortedSet(this.ss.tailSet(paramE));
/*      */     }
/*      */     public E first() {
/* 1170 */       return this.ss.first(); } 
/* 1171 */     public E last() { return this.ss.last(); }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Collections
 * JD-Core Version:    0.6.2
 */