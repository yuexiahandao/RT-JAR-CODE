/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public class TreeMap<K, V> extends AbstractMap<K, V>
/*      */   implements NavigableMap<K, V>, Cloneable, Serializable
/*      */ {
/*      */   private final Comparator<? super K> comparator;
/*  118 */   private transient Entry<K, V> root = null;
/*      */ 
/*  123 */   private transient int size = 0;
/*      */ 
/*  128 */   private transient int modCount = 0;
/*      */ 
/*  779 */   private transient TreeMap<K, V>.EntrySet entrySet = null;
/*  780 */   private transient KeySet<K> navigableKeySet = null;
/*  781 */   private transient NavigableMap<K, V> descendingMap = null;
/*      */ 
/* 1232 */   private static final Object UNBOUNDED = new Object();
/*      */   private static final boolean RED = false;
/*      */   private static final boolean BLACK = true;
/*      */   private static final long serialVersionUID = 919286545866124006L;
/*      */ 
/*      */   public TreeMap()
/*      */   {
/*  143 */     this.comparator = null;
/*      */   }
/*      */ 
/*      */   public TreeMap(Comparator<? super K> paramComparator)
/*      */   {
/*  161 */     this.comparator = paramComparator;
/*      */   }
/*      */ 
/*      */   public TreeMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  179 */     this.comparator = null;
/*  180 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public TreeMap(SortedMap<K, ? extends V> paramSortedMap)
/*      */   {
/*  193 */     this.comparator = paramSortedMap.comparator();
/*      */     try {
/*  195 */       buildFromSorted(paramSortedMap.size(), paramSortedMap.entrySet().iterator(), null, null);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  210 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/*  227 */     return getEntry(paramObject) != null;
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/*  244 */     for (Entry localEntry = getFirstEntry(); localEntry != null; localEntry = successor(localEntry))
/*  245 */       if (valEquals(paramObject, localEntry.value))
/*  246 */         return true;
/*  247 */     return false;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/*  273 */     Entry localEntry = getEntry(paramObject);
/*  274 */     return localEntry == null ? null : localEntry.value;
/*      */   }
/*      */ 
/*      */   public Comparator<? super K> comparator() {
/*  278 */     return this.comparator;
/*      */   }
/*      */ 
/*      */   public K firstKey()
/*      */   {
/*  285 */     return key(getFirstEntry());
/*      */   }
/*      */ 
/*      */   public K lastKey()
/*      */   {
/*  292 */     return key(getLastEntry());
/*      */   }
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  308 */     int i = paramMap.size();
/*  309 */     if ((this.size == 0) && (i != 0) && ((paramMap instanceof SortedMap))) {
/*  310 */       Comparator localComparator = ((SortedMap)paramMap).comparator();
/*  311 */       if ((localComparator == this.comparator) || ((localComparator != null) && (localComparator.equals(this.comparator)))) {
/*  312 */         this.modCount += 1;
/*      */         try {
/*  314 */           buildFromSorted(i, paramMap.entrySet().iterator(), null, null);
/*      */         } catch (IOException localIOException) {
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {
/*      */         }
/*  319 */         return;
/*      */       }
/*      */     }
/*  322 */     super.putAll(paramMap);
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getEntry(Object paramObject)
/*      */   {
/*  339 */     if (this.comparator != null)
/*  340 */       return getEntryUsingComparator(paramObject);
/*  341 */     if (paramObject == null)
/*  342 */       throw new NullPointerException();
/*  343 */     Comparable localComparable = (Comparable)paramObject;
/*  344 */     Entry localEntry = this.root;
/*  345 */     while (localEntry != null) {
/*  346 */       int i = localComparable.compareTo(localEntry.key);
/*  347 */       if (i < 0)
/*  348 */         localEntry = localEntry.left;
/*  349 */       else if (i > 0)
/*  350 */         localEntry = localEntry.right;
/*      */       else
/*  352 */         return localEntry;
/*      */     }
/*  354 */     return null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getEntryUsingComparator(Object paramObject)
/*      */   {
/*  364 */     Object localObject = paramObject;
/*  365 */     Comparator localComparator = this.comparator;
/*  366 */     if (localComparator != null) {
/*  367 */       Entry localEntry = this.root;
/*  368 */       while (localEntry != null) {
/*  369 */         int i = localComparator.compare(localObject, localEntry.key);
/*  370 */         if (i < 0)
/*  371 */           localEntry = localEntry.left;
/*  372 */         else if (i > 0)
/*  373 */           localEntry = localEntry.right;
/*      */         else
/*  375 */           return localEntry;
/*      */       }
/*      */     }
/*  378 */     return null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getCeilingEntry(K paramK)
/*      */   {
/*  388 */     Entry localEntry1 = this.root;
/*  389 */     while (localEntry1 != null) {
/*  390 */       int i = compare(paramK, localEntry1.key);
/*  391 */       if (i < 0) {
/*  392 */         if (localEntry1.left != null)
/*  393 */           localEntry1 = localEntry1.left;
/*      */         else
/*  395 */           return localEntry1;
/*  396 */       } else if (i > 0) {
/*  397 */         if (localEntry1.right != null) {
/*  398 */           localEntry1 = localEntry1.right;
/*      */         } else {
/*  400 */           Entry localEntry2 = localEntry1.parent;
/*  401 */           Entry localEntry3 = localEntry1;
/*  402 */           while ((localEntry2 != null) && (localEntry3 == localEntry2.right)) {
/*  403 */             localEntry3 = localEntry2;
/*  404 */             localEntry2 = localEntry2.parent;
/*      */           }
/*  406 */           return localEntry2;
/*      */         }
/*      */       }
/*  409 */       else return localEntry1;
/*      */     }
/*  411 */     return null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getFloorEntry(K paramK)
/*      */   {
/*  420 */     Entry localEntry1 = this.root;
/*  421 */     while (localEntry1 != null) {
/*  422 */       int i = compare(paramK, localEntry1.key);
/*  423 */       if (i > 0) {
/*  424 */         if (localEntry1.right != null)
/*  425 */           localEntry1 = localEntry1.right;
/*      */         else
/*  427 */           return localEntry1;
/*  428 */       } else if (i < 0) {
/*  429 */         if (localEntry1.left != null) {
/*  430 */           localEntry1 = localEntry1.left;
/*      */         } else {
/*  432 */           Entry localEntry2 = localEntry1.parent;
/*  433 */           Entry localEntry3 = localEntry1;
/*  434 */           while ((localEntry2 != null) && (localEntry3 == localEntry2.left)) {
/*  435 */             localEntry3 = localEntry2;
/*  436 */             localEntry2 = localEntry2.parent;
/*      */           }
/*  438 */           return localEntry2;
/*      */         }
/*      */       }
/*  441 */       else return localEntry1;
/*      */     }
/*      */ 
/*  444 */     return null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getHigherEntry(K paramK)
/*      */   {
/*  454 */     Entry localEntry1 = this.root;
/*  455 */     while (localEntry1 != null) {
/*  456 */       int i = compare(paramK, localEntry1.key);
/*  457 */       if (i < 0) {
/*  458 */         if (localEntry1.left != null)
/*  459 */           localEntry1 = localEntry1.left;
/*      */         else
/*  461 */           return localEntry1;
/*      */       }
/*  463 */       else if (localEntry1.right != null) {
/*  464 */         localEntry1 = localEntry1.right;
/*      */       } else {
/*  466 */         Entry localEntry2 = localEntry1.parent;
/*  467 */         Entry localEntry3 = localEntry1;
/*  468 */         while ((localEntry2 != null) && (localEntry3 == localEntry2.right)) {
/*  469 */           localEntry3 = localEntry2;
/*  470 */           localEntry2 = localEntry2.parent;
/*      */         }
/*  472 */         return localEntry2;
/*      */       }
/*      */     }
/*      */ 
/*  476 */     return null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getLowerEntry(K paramK)
/*      */   {
/*  485 */     Entry localEntry1 = this.root;
/*  486 */     while (localEntry1 != null) {
/*  487 */       int i = compare(paramK, localEntry1.key);
/*  488 */       if (i > 0) {
/*  489 */         if (localEntry1.right != null)
/*  490 */           localEntry1 = localEntry1.right;
/*      */         else
/*  492 */           return localEntry1;
/*      */       }
/*  494 */       else if (localEntry1.left != null) {
/*  495 */         localEntry1 = localEntry1.left;
/*      */       } else {
/*  497 */         Entry localEntry2 = localEntry1.parent;
/*  498 */         Entry localEntry3 = localEntry1;
/*  499 */         while ((localEntry2 != null) && (localEntry3 == localEntry2.left)) {
/*  500 */           localEntry3 = localEntry2;
/*  501 */           localEntry2 = localEntry2.parent;
/*      */         }
/*  503 */         return localEntry2;
/*      */       }
/*      */     }
/*      */ 
/*  507 */     return null;
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/*  529 */     Entry localEntry1 = this.root;
/*  530 */     if (localEntry1 == null) {
/*  531 */       compare(paramK, paramK);
/*      */ 
/*  533 */       this.root = new Entry(paramK, paramV, null);
/*  534 */       this.size = 1;
/*  535 */       this.modCount += 1;
/*  536 */       return null;
/*      */     }
/*      */ 
/*  541 */     Comparator localComparator = this.comparator;
/*      */     Entry localEntry2;
/*      */     int i;
/*  542 */     if (localComparator != null) {
/*      */       do {
/*  544 */         localEntry2 = localEntry1;
/*  545 */         i = localComparator.compare(paramK, localEntry1.key);
/*  546 */         if (i < 0)
/*  547 */           localEntry1 = localEntry1.left;
/*  548 */         else if (i > 0)
/*  549 */           localEntry1 = localEntry1.right;
/*      */         else
/*  551 */           return localEntry1.setValue(paramV); 
/*      */       }
/*  552 */       while (localEntry1 != null);
/*      */     }
/*      */     else {
/*  555 */       if (paramK == null)
/*  556 */         throw new NullPointerException();
/*  557 */       localObject = (Comparable)paramK;
/*      */       do {
/*  559 */         localEntry2 = localEntry1;
/*  560 */         i = ((Comparable)localObject).compareTo(localEntry1.key);
/*  561 */         if (i < 0)
/*  562 */           localEntry1 = localEntry1.left;
/*  563 */         else if (i > 0)
/*  564 */           localEntry1 = localEntry1.right;
/*      */         else
/*  566 */           return localEntry1.setValue(paramV); 
/*      */       }
/*  567 */       while (localEntry1 != null);
/*      */     }
/*  569 */     Object localObject = new Entry(paramK, paramV, localEntry2);
/*  570 */     if (i < 0)
/*  571 */       localEntry2.left = ((Entry)localObject);
/*      */     else
/*  573 */       localEntry2.right = ((Entry)localObject);
/*  574 */     fixAfterInsertion((Entry)localObject);
/*  575 */     this.size += 1;
/*  576 */     this.modCount += 1;
/*  577 */     return null;
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/*  595 */     Entry localEntry = getEntry(paramObject);
/*  596 */     if (localEntry == null) {
/*  597 */       return null;
/*      */     }
/*  599 */     Object localObject = localEntry.value;
/*  600 */     deleteEntry(localEntry);
/*  601 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  609 */     this.modCount += 1;
/*  610 */     this.size = 0;
/*  611 */     this.root = null;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  621 */     TreeMap localTreeMap = null;
/*      */     try {
/*  623 */       localTreeMap = (TreeMap)super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  625 */       throw new InternalError();
/*      */     }
/*      */ 
/*  629 */     localTreeMap.root = null;
/*  630 */     localTreeMap.size = 0;
/*  631 */     localTreeMap.modCount = 0;
/*  632 */     localTreeMap.entrySet = null;
/*  633 */     localTreeMap.navigableKeySet = null;
/*  634 */     localTreeMap.descendingMap = null;
/*      */     try
/*      */     {
/*  638 */       localTreeMap.buildFromSorted(this.size, entrySet().iterator(), null, null);
/*      */     } catch (IOException localIOException) {
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/*      */     }
/*  643 */     return localTreeMap;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> firstEntry()
/*      */   {
/*  652 */     return exportEntry(getFirstEntry());
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> lastEntry()
/*      */   {
/*  659 */     return exportEntry(getLastEntry());
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> pollFirstEntry()
/*      */   {
/*  666 */     Entry localEntry = getFirstEntry();
/*  667 */     Map.Entry localEntry1 = exportEntry(localEntry);
/*  668 */     if (localEntry != null)
/*  669 */       deleteEntry(localEntry);
/*  670 */     return localEntry1;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> pollLastEntry()
/*      */   {
/*  677 */     Entry localEntry = getLastEntry();
/*  678 */     Map.Entry localEntry1 = exportEntry(localEntry);
/*  679 */     if (localEntry != null)
/*  680 */       deleteEntry(localEntry);
/*  681 */     return localEntry1;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> lowerEntry(K paramK)
/*      */   {
/*  692 */     return exportEntry(getLowerEntry(paramK));
/*      */   }
/*      */ 
/*      */   public K lowerKey(K paramK)
/*      */   {
/*  703 */     return keyOrNull(getLowerEntry(paramK));
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> floorEntry(K paramK)
/*      */   {
/*  714 */     return exportEntry(getFloorEntry(paramK));
/*      */   }
/*      */ 
/*      */   public K floorKey(K paramK)
/*      */   {
/*  725 */     return keyOrNull(getFloorEntry(paramK));
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> ceilingEntry(K paramK)
/*      */   {
/*  736 */     return exportEntry(getCeilingEntry(paramK));
/*      */   }
/*      */ 
/*      */   public K ceilingKey(K paramK)
/*      */   {
/*  747 */     return keyOrNull(getCeilingEntry(paramK));
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> higherEntry(K paramK)
/*      */   {
/*  758 */     return exportEntry(getHigherEntry(paramK));
/*      */   }
/*      */ 
/*      */   public K higherKey(K paramK)
/*      */   {
/*  769 */     return keyOrNull(getHigherEntry(paramK));
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/*  798 */     return navigableKeySet();
/*      */   }
/*      */ 
/*      */   public NavigableSet<K> navigableKeySet()
/*      */   {
/*  805 */     KeySet localKeySet = this.navigableKeySet;
/*  806 */     return this.navigableKeySet = new KeySet(this);
/*      */   }
/*      */ 
/*      */   public NavigableSet<K> descendingKeySet()
/*      */   {
/*  813 */     return descendingMap().navigableKeySet();
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/*  832 */     Collection localCollection = this.values;
/*  833 */     return this.values = new Values();
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/*  852 */     EntrySet localEntrySet = this.entrySet;
/*  853 */     return this.entrySet = new EntrySet();
/*      */   }
/*      */ 
/*      */   public NavigableMap<K, V> descendingMap()
/*      */   {
/*  860 */     NavigableMap localNavigableMap = this.descendingMap;
/*  861 */     return this.descendingMap = new DescendingSubMap(this, true, null, true, true, null, true);
/*      */   }
/*      */ 
/*      */   public NavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */   {
/*  877 */     return new AscendingSubMap(this, false, paramK1, paramBoolean1, false, paramK2, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public NavigableMap<K, V> headMap(K paramK, boolean paramBoolean)
/*      */   {
/*  891 */     return new AscendingSubMap(this, true, null, true, false, paramK, paramBoolean);
/*      */   }
/*      */ 
/*      */   public NavigableMap<K, V> tailMap(K paramK, boolean paramBoolean)
/*      */   {
/*  905 */     return new AscendingSubMap(this, false, paramK, paramBoolean, true, null, true);
/*      */   }
/*      */ 
/*      */   public SortedMap<K, V> subMap(K paramK1, K paramK2)
/*      */   {
/*  918 */     return subMap(paramK1, true, paramK2, false);
/*      */   }
/*      */ 
/*      */   public SortedMap<K, V> headMap(K paramK)
/*      */   {
/*  929 */     return headMap(paramK, false);
/*      */   }
/*      */ 
/*      */   public SortedMap<K, V> tailMap(K paramK)
/*      */   {
/*  940 */     return tailMap(paramK, true);
/*      */   }
/*      */ 
/*      */   Iterator<K> keyIterator()
/*      */   {
/* 1018 */     return new KeyIterator(getFirstEntry());
/*      */   }
/*      */ 
/*      */   Iterator<K> descendingKeyIterator() {
/* 1022 */     return new DescendingKeyIterator(getLastEntry());
/*      */   }
/*      */ 
/*      */   final int compare(Object paramObject1, Object paramObject2)
/*      */   {
/* 1188 */     return this.comparator == null ? ((Comparable)paramObject1).compareTo(paramObject2) : this.comparator.compare(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   static final boolean valEquals(Object paramObject1, Object paramObject2)
/*      */   {
/* 1197 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*      */   }
/*      */ 
/*      */   static <K, V> Map.Entry<K, V> exportEntry(Entry<K, V> paramEntry)
/*      */   {
/* 1204 */     return paramEntry == null ? null : new AbstractMap.SimpleImmutableEntry(paramEntry);
/*      */   }
/*      */ 
/*      */   static <K, V> K keyOrNull(Entry<K, V> paramEntry)
/*      */   {
/* 1212 */     return paramEntry == null ? null : paramEntry.key;
/*      */   }
/*      */ 
/*      */   static <K> K key(Entry<K, ?> paramEntry)
/*      */   {
/* 1220 */     if (paramEntry == null)
/* 1221 */       throw new NoSuchElementException();
/* 1222 */     return paramEntry.key;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getFirstEntry()
/*      */   {
/* 1962 */     Entry localEntry = this.root;
/* 1963 */     if (localEntry != null)
/* 1964 */       while (localEntry.left != null)
/* 1965 */         localEntry = localEntry.left;
/* 1966 */     return localEntry;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getLastEntry()
/*      */   {
/* 1974 */     Entry localEntry = this.root;
/* 1975 */     if (localEntry != null)
/* 1976 */       while (localEntry.right != null)
/* 1977 */         localEntry = localEntry.right;
/* 1978 */     return localEntry;
/*      */   }
/*      */ 
/*      */   static <K, V> Entry<K, V> successor(Entry<K, V> paramEntry)
/*      */   {
/* 1985 */     if (paramEntry == null)
/* 1986 */       return null;
/* 1987 */     if (paramEntry.right != null) {
/* 1988 */       localEntry = paramEntry.right;
/* 1989 */       while (localEntry.left != null)
/* 1990 */         localEntry = localEntry.left;
/* 1991 */       return localEntry;
/*      */     }
/* 1993 */     Entry localEntry = paramEntry.parent;
/* 1994 */     Object localObject = paramEntry;
/* 1995 */     while ((localEntry != null) && (localObject == localEntry.right)) {
/* 1996 */       localObject = localEntry;
/* 1997 */       localEntry = localEntry.parent;
/*      */     }
/* 1999 */     return localEntry;
/*      */   }
/*      */ 
/*      */   static <K, V> Entry<K, V> predecessor(Entry<K, V> paramEntry)
/*      */   {
/* 2007 */     if (paramEntry == null)
/* 2008 */       return null;
/* 2009 */     if (paramEntry.left != null) {
/* 2010 */       localEntry = paramEntry.left;
/* 2011 */       while (localEntry.right != null)
/* 2012 */         localEntry = localEntry.right;
/* 2013 */       return localEntry;
/*      */     }
/* 2015 */     Entry localEntry = paramEntry.parent;
/* 2016 */     Object localObject = paramEntry;
/* 2017 */     while ((localEntry != null) && (localObject == localEntry.left)) {
/* 2018 */       localObject = localEntry;
/* 2019 */       localEntry = localEntry.parent;
/*      */     }
/* 2021 */     return localEntry;
/*      */   }
/*      */ 
/*      */   private static <K, V> boolean colorOf(Entry<K, V> paramEntry)
/*      */   {
/* 2036 */     return paramEntry == null ? true : paramEntry.color;
/*      */   }
/*      */ 
/*      */   private static <K, V> Entry<K, V> parentOf(Entry<K, V> paramEntry) {
/* 2040 */     return paramEntry == null ? null : paramEntry.parent;
/*      */   }
/*      */ 
/*      */   private static <K, V> void setColor(Entry<K, V> paramEntry, boolean paramBoolean) {
/* 2044 */     if (paramEntry != null)
/* 2045 */       paramEntry.color = paramBoolean;
/*      */   }
/*      */ 
/*      */   private static <K, V> Entry<K, V> leftOf(Entry<K, V> paramEntry) {
/* 2049 */     return paramEntry == null ? null : paramEntry.left;
/*      */   }
/*      */ 
/*      */   private static <K, V> Entry<K, V> rightOf(Entry<K, V> paramEntry) {
/* 2053 */     return paramEntry == null ? null : paramEntry.right;
/*      */   }
/*      */ 
/*      */   private void rotateLeft(Entry<K, V> paramEntry)
/*      */   {
/* 2058 */     if (paramEntry != null) {
/* 2059 */       Entry localEntry = paramEntry.right;
/* 2060 */       paramEntry.right = localEntry.left;
/* 2061 */       if (localEntry.left != null)
/* 2062 */         localEntry.left.parent = paramEntry;
/* 2063 */       localEntry.parent = paramEntry.parent;
/* 2064 */       if (paramEntry.parent == null)
/* 2065 */         this.root = localEntry;
/* 2066 */       else if (paramEntry.parent.left == paramEntry)
/* 2067 */         paramEntry.parent.left = localEntry;
/*      */       else
/* 2069 */         paramEntry.parent.right = localEntry;
/* 2070 */       localEntry.left = paramEntry;
/* 2071 */       paramEntry.parent = localEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void rotateRight(Entry<K, V> paramEntry)
/*      */   {
/* 2077 */     if (paramEntry != null) {
/* 2078 */       Entry localEntry = paramEntry.left;
/* 2079 */       paramEntry.left = localEntry.right;
/* 2080 */       if (localEntry.right != null) localEntry.right.parent = paramEntry;
/* 2081 */       localEntry.parent = paramEntry.parent;
/* 2082 */       if (paramEntry.parent == null)
/* 2083 */         this.root = localEntry;
/* 2084 */       else if (paramEntry.parent.right == paramEntry)
/* 2085 */         paramEntry.parent.right = localEntry;
/* 2086 */       else paramEntry.parent.left = localEntry;
/* 2087 */       localEntry.right = paramEntry;
/* 2088 */       paramEntry.parent = localEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fixAfterInsertion(Entry<K, V> paramEntry)
/*      */   {
/* 2094 */     paramEntry.color = false;
/*      */ 
/* 2096 */     while ((paramEntry != null) && (paramEntry != this.root) && (!paramEntry.parent.color))
/*      */     {
/*      */       Entry localEntry;
/* 2097 */       if (parentOf(paramEntry) == leftOf(parentOf(parentOf(paramEntry)))) {
/* 2098 */         localEntry = rightOf(parentOf(parentOf(paramEntry)));
/* 2099 */         if (!colorOf(localEntry)) {
/* 2100 */           setColor(parentOf(paramEntry), true);
/* 2101 */           setColor(localEntry, true);
/* 2102 */           setColor(parentOf(parentOf(paramEntry)), false);
/* 2103 */           paramEntry = parentOf(parentOf(paramEntry));
/*      */         } else {
/* 2105 */           if (paramEntry == rightOf(parentOf(paramEntry))) {
/* 2106 */             paramEntry = parentOf(paramEntry);
/* 2107 */             rotateLeft(paramEntry);
/*      */           }
/* 2109 */           setColor(parentOf(paramEntry), true);
/* 2110 */           setColor(parentOf(parentOf(paramEntry)), false);
/* 2111 */           rotateRight(parentOf(parentOf(paramEntry)));
/*      */         }
/*      */       } else {
/* 2114 */         localEntry = leftOf(parentOf(parentOf(paramEntry)));
/* 2115 */         if (!colorOf(localEntry)) {
/* 2116 */           setColor(parentOf(paramEntry), true);
/* 2117 */           setColor(localEntry, true);
/* 2118 */           setColor(parentOf(parentOf(paramEntry)), false);
/* 2119 */           paramEntry = parentOf(parentOf(paramEntry));
/*      */         } else {
/* 2121 */           if (paramEntry == leftOf(parentOf(paramEntry))) {
/* 2122 */             paramEntry = parentOf(paramEntry);
/* 2123 */             rotateRight(paramEntry);
/*      */           }
/* 2125 */           setColor(parentOf(paramEntry), true);
/* 2126 */           setColor(parentOf(parentOf(paramEntry)), false);
/* 2127 */           rotateLeft(parentOf(parentOf(paramEntry)));
/*      */         }
/*      */       }
/*      */     }
/* 2131 */     this.root.color = true;
/*      */   }
/*      */ 
/*      */   private void deleteEntry(Entry<K, V> paramEntry)
/*      */   {
/* 2138 */     this.modCount += 1;
/* 2139 */     this.size -= 1;
/*      */ 
/* 2143 */     if ((paramEntry.left != null) && (paramEntry.right != null)) {
/* 2144 */       localEntry = successor(paramEntry);
/* 2145 */       paramEntry.key = localEntry.key;
/* 2146 */       paramEntry.value = localEntry.value;
/* 2147 */       paramEntry = localEntry;
/*      */     }
/*      */ 
/* 2151 */     Entry localEntry = paramEntry.left != null ? paramEntry.left : paramEntry.right;
/*      */ 
/* 2153 */     if (localEntry != null)
/*      */     {
/* 2155 */       localEntry.parent = paramEntry.parent;
/* 2156 */       if (paramEntry.parent == null)
/* 2157 */         this.root = localEntry;
/* 2158 */       else if (paramEntry == paramEntry.parent.left)
/* 2159 */         paramEntry.parent.left = localEntry;
/*      */       else {
/* 2161 */         paramEntry.parent.right = localEntry;
/*      */       }
/*      */ 
/* 2164 */       paramEntry.left = (paramEntry.right = paramEntry.parent = null);
/*      */ 
/* 2167 */       if (paramEntry.color == true)
/* 2168 */         fixAfterDeletion(localEntry);
/* 2169 */     } else if (paramEntry.parent == null) {
/* 2170 */       this.root = null;
/*      */     } else {
/* 2172 */       if (paramEntry.color == true) {
/* 2173 */         fixAfterDeletion(paramEntry);
/*      */       }
/* 2175 */       if (paramEntry.parent != null) {
/* 2176 */         if (paramEntry == paramEntry.parent.left)
/* 2177 */           paramEntry.parent.left = null;
/* 2178 */         else if (paramEntry == paramEntry.parent.right)
/* 2179 */           paramEntry.parent.right = null;
/* 2180 */         paramEntry.parent = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void fixAfterDeletion(Entry<K, V> paramEntry)
/*      */   {
/* 2187 */     while ((paramEntry != this.root) && (colorOf(paramEntry) == true))
/*      */     {
/*      */       Entry localEntry;
/* 2188 */       if (paramEntry == leftOf(parentOf(paramEntry))) {
/* 2189 */         localEntry = rightOf(parentOf(paramEntry));
/*      */ 
/* 2191 */         if (!colorOf(localEntry)) {
/* 2192 */           setColor(localEntry, true);
/* 2193 */           setColor(parentOf(paramEntry), false);
/* 2194 */           rotateLeft(parentOf(paramEntry));
/* 2195 */           localEntry = rightOf(parentOf(paramEntry));
/*      */         }
/*      */ 
/* 2198 */         if ((colorOf(leftOf(localEntry)) == true) && (colorOf(rightOf(localEntry)) == true))
/*      */         {
/* 2200 */           setColor(localEntry, false);
/* 2201 */           paramEntry = parentOf(paramEntry);
/*      */         } else {
/* 2203 */           if (colorOf(rightOf(localEntry)) == true) {
/* 2204 */             setColor(leftOf(localEntry), true);
/* 2205 */             setColor(localEntry, false);
/* 2206 */             rotateRight(localEntry);
/* 2207 */             localEntry = rightOf(parentOf(paramEntry));
/*      */           }
/* 2209 */           setColor(localEntry, colorOf(parentOf(paramEntry)));
/* 2210 */           setColor(parentOf(paramEntry), true);
/* 2211 */           setColor(rightOf(localEntry), true);
/* 2212 */           rotateLeft(parentOf(paramEntry));
/* 2213 */           paramEntry = this.root;
/*      */         }
/*      */       } else {
/* 2216 */         localEntry = leftOf(parentOf(paramEntry));
/*      */ 
/* 2218 */         if (!colorOf(localEntry)) {
/* 2219 */           setColor(localEntry, true);
/* 2220 */           setColor(parentOf(paramEntry), false);
/* 2221 */           rotateRight(parentOf(paramEntry));
/* 2222 */           localEntry = leftOf(parentOf(paramEntry));
/*      */         }
/*      */ 
/* 2225 */         if ((colorOf(rightOf(localEntry)) == true) && (colorOf(leftOf(localEntry)) == true))
/*      */         {
/* 2227 */           setColor(localEntry, false);
/* 2228 */           paramEntry = parentOf(paramEntry);
/*      */         } else {
/* 2230 */           if (colorOf(leftOf(localEntry)) == true) {
/* 2231 */             setColor(rightOf(localEntry), true);
/* 2232 */             setColor(localEntry, false);
/* 2233 */             rotateLeft(localEntry);
/* 2234 */             localEntry = leftOf(parentOf(paramEntry));
/*      */           }
/* 2236 */           setColor(localEntry, colorOf(parentOf(paramEntry)));
/* 2237 */           setColor(parentOf(paramEntry), true);
/* 2238 */           setColor(leftOf(localEntry), true);
/* 2239 */           rotateRight(parentOf(paramEntry));
/* 2240 */           paramEntry = this.root;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2245 */     setColor(paramEntry, true);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2265 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 2268 */     paramObjectOutputStream.writeInt(this.size);
/*      */ 
/* 2271 */     for (Iterator localIterator = entrySet().iterator(); localIterator.hasNext(); ) {
/* 2272 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 2273 */       paramObjectOutputStream.writeObject(localEntry.getKey());
/* 2274 */       paramObjectOutputStream.writeObject(localEntry.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2285 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2288 */     int i = paramObjectInputStream.readInt();
/*      */ 
/* 2290 */     buildFromSorted(i, null, paramObjectInputStream, null);
/*      */   }
/*      */ 
/*      */   void readTreeSet(int paramInt, ObjectInputStream paramObjectInputStream, V paramV)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2296 */     buildFromSorted(paramInt, null, paramObjectInputStream, paramV);
/*      */   }
/*      */ 
/*      */   void addAllForTreeSet(SortedSet<? extends K> paramSortedSet, V paramV)
/*      */   {
/*      */     try {
/* 2302 */       buildFromSorted(paramSortedSet.size(), paramSortedSet.iterator(), null, paramV);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void buildFromSorted(int paramInt, Iterator paramIterator, ObjectInputStream paramObjectInputStream, V paramV)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2343 */     this.size = paramInt;
/* 2344 */     this.root = buildFromSorted(0, 0, paramInt - 1, computeRedLevel(paramInt), paramIterator, paramObjectInputStream, paramV);
/*      */   }
/*      */ 
/*      */   private final Entry<K, V> buildFromSorted(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Iterator paramIterator, ObjectInputStream paramObjectInputStream, V paramV)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2380 */     if (paramInt3 < paramInt2) return null;
/*      */ 
/* 2382 */     int i = paramInt2 + paramInt3 >>> 1;
/*      */ 
/* 2384 */     Entry localEntry1 = null;
/* 2385 */     if (paramInt2 < i)
/* 2386 */       localEntry1 = buildFromSorted(paramInt1 + 1, paramInt2, i - 1, paramInt4, paramIterator, paramObjectInputStream, paramV);
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2392 */     if (paramIterator != null) {
/* 2393 */       if (paramV == null) {
/* 2394 */         localObject3 = (Map.Entry)paramIterator.next();
/* 2395 */         localObject1 = ((Map.Entry)localObject3).getKey();
/* 2396 */         localObject2 = ((Map.Entry)localObject3).getValue();
/*      */       } else {
/* 2398 */         localObject1 = paramIterator.next();
/* 2399 */         localObject2 = paramV;
/*      */       }
/*      */     } else {
/* 2402 */       localObject1 = paramObjectInputStream.readObject();
/* 2403 */       localObject2 = paramV != null ? paramV : paramObjectInputStream.readObject();
/*      */     }
/*      */ 
/* 2406 */     Object localObject3 = new Entry(localObject1, localObject2, null);
/*      */ 
/* 2409 */     if (paramInt1 == paramInt4) {
/* 2410 */       ((Entry)localObject3).color = false;
/*      */     }
/* 2412 */     if (localEntry1 != null) {
/* 2413 */       ((Entry)localObject3).left = localEntry1;
/* 2414 */       localEntry1.parent = ((Entry)localObject3);
/*      */     }
/*      */ 
/* 2417 */     if (i < paramInt3) {
/* 2418 */       Entry localEntry2 = buildFromSorted(paramInt1 + 1, i + 1, paramInt3, paramInt4, paramIterator, paramObjectInputStream, paramV);
/*      */ 
/* 2420 */       ((Entry)localObject3).right = localEntry2;
/* 2421 */       localEntry2.parent = ((Entry)localObject3);
/*      */     }
/*      */ 
/* 2424 */     return localObject3;
/*      */   }
/*      */ 
/*      */   private static int computeRedLevel(int paramInt)
/*      */   {
/* 2437 */     int i = 0;
/* 2438 */     for (int j = paramInt - 1; j >= 0; j = j / 2 - 1)
/* 2439 */       i++;
/* 2440 */     return i;
/*      */   }
/*      */ 
/*      */   static final class AscendingSubMap<K, V> extends TreeMap.NavigableSubMap<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 912986545866124060L;
/*      */ 
/*      */     AscendingSubMap(TreeMap<K, V> paramTreeMap, boolean paramBoolean1, K paramK1, boolean paramBoolean2, boolean paramBoolean3, K paramK2, boolean paramBoolean4)
/*      */     {
/* 1699 */       super(paramBoolean1, paramK1, paramBoolean2, paramBoolean3, paramK2, paramBoolean4);
/*      */     }
/*      */ 
/*      */     public Comparator<? super K> comparator() {
/* 1703 */       return this.m.comparator();
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */     {
/* 1708 */       if (!inRange(paramK1, paramBoolean1))
/* 1709 */         throw new IllegalArgumentException("fromKey out of range");
/* 1710 */       if (!inRange(paramK2, paramBoolean2))
/* 1711 */         throw new IllegalArgumentException("toKey out of range");
/* 1712 */       return new AscendingSubMap(this.m, false, paramK1, paramBoolean1, false, paramK2, paramBoolean2);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> headMap(K paramK, boolean paramBoolean)
/*      */     {
/* 1718 */       if (!inRange(paramK, paramBoolean))
/* 1719 */         throw new IllegalArgumentException("toKey out of range");
/* 1720 */       return new AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, paramK, paramBoolean);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> tailMap(K paramK, boolean paramBoolean)
/*      */     {
/* 1726 */       if (!inRange(paramK, paramBoolean))
/* 1727 */         throw new IllegalArgumentException("fromKey out of range");
/* 1728 */       return new AscendingSubMap(this.m, false, paramK, paramBoolean, this.toEnd, this.hi, this.hiInclusive);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> descendingMap()
/*      */     {
/* 1734 */       NavigableMap localNavigableMap = this.descendingMapView;
/* 1735 */       return this.descendingMapView = new TreeMap.DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
/*      */     }
/*      */ 
/*      */     Iterator<K> keyIterator()
/*      */     {
/* 1743 */       return new TreeMap.NavigableSubMap.SubMapKeyIterator(this, absLowest(), absHighFence());
/*      */     }
/*      */ 
/*      */     Iterator<K> descendingKeyIterator() {
/* 1747 */       return new TreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this, absHighest(), absLowFence());
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet()
/*      */     {
/* 1757 */       TreeMap.NavigableSubMap.EntrySetView localEntrySetView = this.entrySetView;
/* 1758 */       return localEntrySetView != null ? localEntrySetView : new AscendingEntrySetView();
/*      */     }
/*      */     TreeMap.Entry<K, V> subLowest() {
/* 1761 */       return absLowest(); } 
/* 1762 */     TreeMap.Entry<K, V> subHighest() { return absHighest(); } 
/* 1763 */     TreeMap.Entry<K, V> subCeiling(K paramK) { return absCeiling(paramK); } 
/* 1764 */     TreeMap.Entry<K, V> subHigher(K paramK) { return absHigher(paramK); } 
/* 1765 */     TreeMap.Entry<K, V> subFloor(K paramK) { return absFloor(paramK); } 
/* 1766 */     TreeMap.Entry<K, V> subLower(K paramK) { return absLower(paramK); }
/*      */ 
/*      */ 
/*      */     final class AscendingEntrySetView extends TreeMap.NavigableSubMap<K, V>.EntrySetView
/*      */     {
/*      */       AscendingEntrySetView()
/*      */       {
/* 1750 */         super();
/*      */       }
/* 1752 */       public Iterator<Map.Entry<K, V>> iterator() { return new TreeMap.NavigableSubMap.SubMapEntryIterator(TreeMap.AscendingSubMap.this, TreeMap.AscendingSubMap.this.absLowest(), TreeMap.AscendingSubMap.this.absHighFence()); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   final class DescendingKeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K>
/*      */   {
/*      */     DescendingKeyIterator()
/*      */     {
/* 1175 */       super(localEntry);
/*      */     }
/*      */     public K next() {
/* 1178 */       return prevEntry().key;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class DescendingSubMap<K, V> extends TreeMap.NavigableSubMap<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 912986545866120460L;
/* 1780 */     private final Comparator<? super K> reverseComparator = Collections.reverseOrder(this.m.comparator);
/*      */ 
/*      */     DescendingSubMap(TreeMap<K, V> paramTreeMap, boolean paramBoolean1, K paramK1, boolean paramBoolean2, boolean paramBoolean3, K paramK2, boolean paramBoolean4)
/*      */     {
/* 1777 */       super(paramBoolean1, paramK1, paramBoolean2, paramBoolean3, paramK2, paramBoolean4);
/*      */     }
/*      */ 
/*      */     public Comparator<? super K> comparator()
/*      */     {
/* 1784 */       return this.reverseComparator;
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */     {
/* 1789 */       if (!inRange(paramK1, paramBoolean1))
/* 1790 */         throw new IllegalArgumentException("fromKey out of range");
/* 1791 */       if (!inRange(paramK2, paramBoolean2))
/* 1792 */         throw new IllegalArgumentException("toKey out of range");
/* 1793 */       return new DescendingSubMap(this.m, false, paramK2, paramBoolean2, false, paramK1, paramBoolean1);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> headMap(K paramK, boolean paramBoolean)
/*      */     {
/* 1799 */       if (!inRange(paramK, paramBoolean))
/* 1800 */         throw new IllegalArgumentException("toKey out of range");
/* 1801 */       return new DescendingSubMap(this.m, false, paramK, paramBoolean, this.toEnd, this.hi, this.hiInclusive);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> tailMap(K paramK, boolean paramBoolean)
/*      */     {
/* 1807 */       if (!inRange(paramK, paramBoolean))
/* 1808 */         throw new IllegalArgumentException("fromKey out of range");
/* 1809 */       return new DescendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, false, paramK, paramBoolean);
/*      */     }
/*      */ 
/*      */     public NavigableMap<K, V> descendingMap()
/*      */     {
/* 1815 */       NavigableMap localNavigableMap = this.descendingMapView;
/* 1816 */       return this.descendingMapView = new TreeMap.AscendingSubMap(this.m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
/*      */     }
/*      */ 
/*      */     Iterator<K> keyIterator()
/*      */     {
/* 1824 */       return new TreeMap.NavigableSubMap.DescendingSubMapKeyIterator(this, absHighest(), absLowFence());
/*      */     }
/*      */ 
/*      */     Iterator<K> descendingKeyIterator() {
/* 1828 */       return new TreeMap.NavigableSubMap.SubMapKeyIterator(this, absLowest(), absHighFence());
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet()
/*      */     {
/* 1838 */       TreeMap.NavigableSubMap.EntrySetView localEntrySetView = this.entrySetView;
/* 1839 */       return localEntrySetView != null ? localEntrySetView : new DescendingEntrySetView();
/*      */     }
/*      */     TreeMap.Entry<K, V> subLowest() {
/* 1842 */       return absHighest(); } 
/* 1843 */     TreeMap.Entry<K, V> subHighest() { return absLowest(); } 
/* 1844 */     TreeMap.Entry<K, V> subCeiling(K paramK) { return absFloor(paramK); } 
/* 1845 */     TreeMap.Entry<K, V> subHigher(K paramK) { return absLower(paramK); } 
/* 1846 */     TreeMap.Entry<K, V> subFloor(K paramK) { return absCeiling(paramK); } 
/* 1847 */     TreeMap.Entry<K, V> subLower(K paramK) { return absHigher(paramK); }
/*      */ 
/*      */ 
/*      */     final class DescendingEntrySetView extends TreeMap.NavigableSubMap<K, V>.EntrySetView
/*      */     {
/*      */       DescendingEntrySetView()
/*      */       {
/* 1831 */         super();
/*      */       }
/* 1833 */       public Iterator<Map.Entry<K, V>> iterator() { return new TreeMap.NavigableSubMap.DescendingSubMapEntryIterator(TreeMap.DescendingSubMap.this, TreeMap.DescendingSubMap.this.absHighest(), TreeMap.DescendingSubMap.this.absLowFence()); }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Entry<K, V>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     K key;
/*      */     V value;
/* 1892 */     Entry<K, V> left = null;
/* 1893 */     Entry<K, V> right = null;
/*      */     Entry<K, V> parent;
/* 1895 */     boolean color = true;
/*      */ 
/*      */     Entry(K paramK, V paramV, Entry<K, V> paramEntry)
/*      */     {
/* 1902 */       this.key = paramK;
/* 1903 */       this.value = paramV;
/* 1904 */       this.parent = paramEntry;
/*      */     }
/*      */ 
/*      */     public K getKey()
/*      */     {
/* 1913 */       return this.key;
/*      */     }
/*      */ 
/*      */     public V getValue()
/*      */     {
/* 1922 */       return this.value;
/*      */     }
/*      */ 
/*      */     public V setValue(V paramV)
/*      */     {
/* 1933 */       Object localObject = this.value;
/* 1934 */       this.value = paramV;
/* 1935 */       return localObject;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1939 */       if (!(paramObject instanceof Map.Entry))
/* 1940 */         return false;
/* 1941 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*      */ 
/* 1943 */       return (TreeMap.valEquals(this.key, localEntry.getKey())) && (TreeMap.valEquals(this.value, localEntry.getValue()));
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/* 1947 */       int i = this.key == null ? 0 : this.key.hashCode();
/* 1948 */       int j = this.value == null ? 0 : this.value.hashCode();
/* 1949 */       return i ^ j;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1953 */       return this.key + "=" + this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */   final class EntryIterator extends TreeMap<K, V>.PrivateEntryIterator<Map.Entry<K, V>>
/*      */   {
/*      */     EntryIterator()
/*      */     {
/* 1148 */       super(localEntry);
/*      */     }
/*      */     public Map.Entry<K, V> next() {
/* 1151 */       return nextEntry();
/*      */     }
/*      */   }
/*      */ 
/*      */   class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     EntrySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<Map.Entry<K, V>> iterator()
/*      */     {
/*  975 */       return new TreeMap.EntryIterator(TreeMap.this, TreeMap.this.getFirstEntry());
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/*  979 */       if (!(paramObject instanceof Map.Entry))
/*  980 */         return false;
/*  981 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  982 */       Object localObject = localEntry.getValue();
/*  983 */       TreeMap.Entry localEntry1 = TreeMap.this.getEntry(localEntry.getKey());
/*  984 */       return (localEntry1 != null) && (TreeMap.valEquals(localEntry1.getValue(), localObject));
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/*  988 */       if (!(paramObject instanceof Map.Entry))
/*  989 */         return false;
/*  990 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  991 */       Object localObject = localEntry.getValue();
/*  992 */       TreeMap.Entry localEntry1 = TreeMap.this.getEntry(localEntry.getKey());
/*  993 */       if ((localEntry1 != null) && (TreeMap.valEquals(localEntry1.getValue(), localObject))) {
/*  994 */         TreeMap.this.deleteEntry(localEntry1);
/*  995 */         return true;
/*      */       }
/*  997 */       return false;
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1001 */       return TreeMap.this.size();
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1005 */       TreeMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   final class KeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K>
/*      */   {
/*      */     KeyIterator()
/*      */     {
/* 1166 */       super(localEntry);
/*      */     }
/*      */     public K next() {
/* 1169 */       return nextEntry().key;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class KeySet<E> extends AbstractSet<E>
/*      */     implements NavigableSet<E>
/*      */   {
/*      */     private final NavigableMap<E, Object> m;
/*      */ 
/*      */     KeySet(NavigableMap<E, Object> paramNavigableMap)
/*      */     {
/* 1027 */       this.m = paramNavigableMap;
/*      */     }
/*      */     public Iterator<E> iterator() {
/* 1030 */       if ((this.m instanceof TreeMap)) {
/* 1031 */         return ((TreeMap)this.m).keyIterator();
/*      */       }
/* 1033 */       return ((TreeMap.NavigableSubMap)this.m).keyIterator();
/*      */     }
/*      */ 
/*      */     public Iterator<E> descendingIterator() {
/* 1037 */       if ((this.m instanceof TreeMap)) {
/* 1038 */         return ((TreeMap)this.m).descendingKeyIterator();
/*      */       }
/* 1040 */       return ((TreeMap.NavigableSubMap)this.m).descendingKeyIterator();
/*      */     }
/*      */     public int size() {
/* 1043 */       return this.m.size(); } 
/* 1044 */     public boolean isEmpty() { return this.m.isEmpty(); } 
/* 1045 */     public boolean contains(Object paramObject) { return this.m.containsKey(paramObject); } 
/* 1046 */     public void clear() { this.m.clear(); } 
/* 1047 */     public E lower(E paramE) { return this.m.lowerKey(paramE); } 
/* 1048 */     public E floor(E paramE) { return this.m.floorKey(paramE); } 
/* 1049 */     public E ceiling(E paramE) { return this.m.ceilingKey(paramE); } 
/* 1050 */     public E higher(E paramE) { return this.m.higherKey(paramE); } 
/* 1051 */     public E first() { return this.m.firstKey(); } 
/* 1052 */     public E last() { return this.m.lastKey(); } 
/* 1053 */     public Comparator<? super E> comparator() { return this.m.comparator(); } 
/*      */     public E pollFirst() {
/* 1055 */       Map.Entry localEntry = this.m.pollFirstEntry();
/* 1056 */       return localEntry == null ? null : localEntry.getKey();
/*      */     }
/*      */     public E pollLast() {
/* 1059 */       Map.Entry localEntry = this.m.pollLastEntry();
/* 1060 */       return localEntry == null ? null : localEntry.getKey();
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1063 */       int i = size();
/* 1064 */       this.m.remove(paramObject);
/* 1065 */       return size() != i;
/*      */     }
/*      */ 
/*      */     public NavigableSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2) {
/* 1069 */       return new KeySet(this.m.subMap(paramE1, paramBoolean1, paramE2, paramBoolean2));
/*      */     }
/*      */ 
/*      */     public NavigableSet<E> headSet(E paramE, boolean paramBoolean) {
/* 1073 */       return new KeySet(this.m.headMap(paramE, paramBoolean));
/*      */     }
/*      */     public NavigableSet<E> tailSet(E paramE, boolean paramBoolean) {
/* 1076 */       return new KeySet(this.m.tailMap(paramE, paramBoolean));
/*      */     }
/*      */     public SortedSet<E> subSet(E paramE1, E paramE2) {
/* 1079 */       return subSet(paramE1, true, paramE2, false);
/*      */     }
/*      */     public SortedSet<E> headSet(E paramE) {
/* 1082 */       return headSet(paramE, false);
/*      */     }
/*      */     public SortedSet<E> tailSet(E paramE) {
/* 1085 */       return tailSet(paramE, true);
/*      */     }
/*      */     public NavigableSet<E> descendingSet() {
/* 1088 */       return new KeySet(this.m.descendingMap());
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class NavigableSubMap<K, V> extends AbstractMap<K, V>
/*      */     implements NavigableMap<K, V>, Serializable
/*      */   {
/*      */     final TreeMap<K, V> m;
/*      */     final K lo;
/*      */     final K hi;
/*      */     final boolean fromStart;
/*      */     final boolean toEnd;
/*      */     final boolean loInclusive;
/*      */     final boolean hiInclusive;
/* 1484 */     transient NavigableMap<K, V> descendingMapView = null;
/* 1485 */     transient NavigableSubMap<K, V>.EntrySetView entrySetView = null;
/* 1486 */     transient TreeMap.KeySet<K> navigableKeySetView = null;
/*      */ 
/*      */     NavigableSubMap(TreeMap<K, V> paramTreeMap, boolean paramBoolean1, K paramK1, boolean paramBoolean2, boolean paramBoolean3, K paramK2, boolean paramBoolean4)
/*      */     {
/* 1259 */       if ((!paramBoolean1) && (!paramBoolean3)) {
/* 1260 */         if (paramTreeMap.compare(paramK1, paramK2) > 0)
/* 1261 */           throw new IllegalArgumentException("fromKey > toKey");
/*      */       } else {
/* 1263 */         if (!paramBoolean1)
/* 1264 */           paramTreeMap.compare(paramK1, paramK1);
/* 1265 */         if (!paramBoolean3) {
/* 1266 */           paramTreeMap.compare(paramK2, paramK2);
/*      */         }
/*      */       }
/* 1269 */       this.m = paramTreeMap;
/* 1270 */       this.fromStart = paramBoolean1;
/* 1271 */       this.lo = paramK1;
/* 1272 */       this.loInclusive = paramBoolean2;
/* 1273 */       this.toEnd = paramBoolean3;
/* 1274 */       this.hi = paramK2;
/* 1275 */       this.hiInclusive = paramBoolean4;
/*      */     }
/*      */ 
/*      */     final boolean tooLow(Object paramObject)
/*      */     {
/* 1281 */       if (!this.fromStart) {
/* 1282 */         int i = this.m.compare(paramObject, this.lo);
/* 1283 */         if ((i < 0) || ((i == 0) && (!this.loInclusive)))
/* 1284 */           return true;
/*      */       }
/* 1286 */       return false;
/*      */     }
/*      */ 
/*      */     final boolean tooHigh(Object paramObject) {
/* 1290 */       if (!this.toEnd) {
/* 1291 */         int i = this.m.compare(paramObject, this.hi);
/* 1292 */         if ((i > 0) || ((i == 0) && (!this.hiInclusive)))
/* 1293 */           return true;
/*      */       }
/* 1295 */       return false;
/*      */     }
/*      */ 
/*      */     final boolean inRange(Object paramObject) {
/* 1299 */       return (!tooLow(paramObject)) && (!tooHigh(paramObject));
/*      */     }
/*      */ 
/*      */     final boolean inClosedRange(Object paramObject) {
/* 1303 */       return ((this.fromStart) || (this.m.compare(paramObject, this.lo) >= 0)) && ((this.toEnd) || (this.m.compare(this.hi, paramObject) >= 0));
/*      */     }
/*      */ 
/*      */     final boolean inRange(Object paramObject, boolean paramBoolean)
/*      */     {
/* 1308 */       return paramBoolean ? inRange(paramObject) : inClosedRange(paramObject);
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absLowest()
/*      */     {
/* 1318 */       TreeMap.Entry localEntry = this.loInclusive ? this.m.getCeilingEntry(this.lo) : this.fromStart ? this.m.getFirstEntry() : this.m.getHigherEntry(this.lo);
/*      */ 
/* 1322 */       return (localEntry == null) || (tooHigh(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absHighest() {
/* 1326 */       TreeMap.Entry localEntry = this.hiInclusive ? this.m.getFloorEntry(this.hi) : this.toEnd ? this.m.getLastEntry() : this.m.getLowerEntry(this.hi);
/*      */ 
/* 1330 */       return (localEntry == null) || (tooLow(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absCeiling(K paramK) {
/* 1334 */       if (tooLow(paramK))
/* 1335 */         return absLowest();
/* 1336 */       TreeMap.Entry localEntry = this.m.getCeilingEntry(paramK);
/* 1337 */       return (localEntry == null) || (tooHigh(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absHigher(K paramK) {
/* 1341 */       if (tooLow(paramK))
/* 1342 */         return absLowest();
/* 1343 */       TreeMap.Entry localEntry = this.m.getHigherEntry(paramK);
/* 1344 */       return (localEntry == null) || (tooHigh(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absFloor(K paramK) {
/* 1348 */       if (tooHigh(paramK))
/* 1349 */         return absHighest();
/* 1350 */       TreeMap.Entry localEntry = this.m.getFloorEntry(paramK);
/* 1351 */       return (localEntry == null) || (tooLow(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absLower(K paramK) {
/* 1355 */       if (tooHigh(paramK))
/* 1356 */         return absHighest();
/* 1357 */       TreeMap.Entry localEntry = this.m.getLowerEntry(paramK);
/* 1358 */       return (localEntry == null) || (tooLow(localEntry.key)) ? null : localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absHighFence()
/*      */     {
/* 1363 */       return this.hiInclusive ? this.m.getHigherEntry(this.hi) : this.toEnd ? null : this.m.getCeilingEntry(this.hi);
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> absLowFence()
/*      */     {
/* 1370 */       return this.loInclusive ? this.m.getLowerEntry(this.lo) : this.fromStart ? null : this.m.getFloorEntry(this.lo);
/*      */     }
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subLowest();
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subHighest();
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subCeiling(K paramK);
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subHigher(K paramK);
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subFloor(K paramK);
/*      */ 
/*      */     abstract TreeMap.Entry<K, V> subLower(K paramK);
/*      */ 
/*      */     abstract Iterator<K> keyIterator();
/*      */ 
/*      */     abstract Iterator<K> descendingKeyIterator();
/*      */ 
/*      */     public boolean isEmpty()
/*      */     {
/* 1394 */       return (this.fromStart) && (this.toEnd) ? this.m.isEmpty() : entrySet().isEmpty();
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1398 */       return (this.fromStart) && (this.toEnd) ? this.m.size() : entrySet().size();
/*      */     }
/*      */ 
/*      */     public final boolean containsKey(Object paramObject) {
/* 1402 */       return (inRange(paramObject)) && (this.m.containsKey(paramObject));
/*      */     }
/*      */ 
/*      */     public final V put(K paramK, V paramV) {
/* 1406 */       if (!inRange(paramK))
/* 1407 */         throw new IllegalArgumentException("key out of range");
/* 1408 */       return this.m.put(paramK, paramV);
/*      */     }
/*      */ 
/*      */     public final V get(Object paramObject) {
/* 1412 */       return !inRange(paramObject) ? null : this.m.get(paramObject);
/*      */     }
/*      */ 
/*      */     public final V remove(Object paramObject) {
/* 1416 */       return !inRange(paramObject) ? null : this.m.remove(paramObject);
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> ceilingEntry(K paramK) {
/* 1420 */       return TreeMap.exportEntry(subCeiling(paramK));
/*      */     }
/*      */ 
/*      */     public final K ceilingKey(K paramK) {
/* 1424 */       return TreeMap.keyOrNull(subCeiling(paramK));
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> higherEntry(K paramK) {
/* 1428 */       return TreeMap.exportEntry(subHigher(paramK));
/*      */     }
/*      */ 
/*      */     public final K higherKey(K paramK) {
/* 1432 */       return TreeMap.keyOrNull(subHigher(paramK));
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> floorEntry(K paramK) {
/* 1436 */       return TreeMap.exportEntry(subFloor(paramK));
/*      */     }
/*      */ 
/*      */     public final K floorKey(K paramK) {
/* 1440 */       return TreeMap.keyOrNull(subFloor(paramK));
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> lowerEntry(K paramK) {
/* 1444 */       return TreeMap.exportEntry(subLower(paramK));
/*      */     }
/*      */ 
/*      */     public final K lowerKey(K paramK) {
/* 1448 */       return TreeMap.keyOrNull(subLower(paramK));
/*      */     }
/*      */ 
/*      */     public final K firstKey() {
/* 1452 */       return TreeMap.key(subLowest());
/*      */     }
/*      */ 
/*      */     public final K lastKey() {
/* 1456 */       return TreeMap.key(subHighest());
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> firstEntry() {
/* 1460 */       return TreeMap.exportEntry(subLowest());
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> lastEntry() {
/* 1464 */       return TreeMap.exportEntry(subHighest());
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> pollFirstEntry() {
/* 1468 */       TreeMap.Entry localEntry = subLowest();
/* 1469 */       Map.Entry localEntry1 = TreeMap.exportEntry(localEntry);
/* 1470 */       if (localEntry != null)
/* 1471 */         this.m.deleteEntry(localEntry);
/* 1472 */       return localEntry1;
/*      */     }
/*      */ 
/*      */     public final Map.Entry<K, V> pollLastEntry() {
/* 1476 */       TreeMap.Entry localEntry = subHighest();
/* 1477 */       Map.Entry localEntry1 = TreeMap.exportEntry(localEntry);
/* 1478 */       if (localEntry != null)
/* 1479 */         this.m.deleteEntry(localEntry);
/* 1480 */       return localEntry1;
/*      */     }
/*      */ 
/*      */     public final NavigableSet<K> navigableKeySet()
/*      */     {
/* 1489 */       TreeMap.KeySet localKeySet = this.navigableKeySetView;
/* 1490 */       return this.navigableKeySetView = new TreeMap.KeySet(this);
/*      */     }
/*      */ 
/*      */     public final Set<K> keySet()
/*      */     {
/* 1495 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1499 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     public final SortedMap<K, V> subMap(K paramK1, K paramK2) {
/* 1503 */       return subMap(paramK1, true, paramK2, false);
/*      */     }
/*      */ 
/*      */     public final SortedMap<K, V> headMap(K paramK) {
/* 1507 */       return headMap(paramK, false);
/*      */     }
/*      */ 
/*      */     public final SortedMap<K, V> tailMap(K paramK) {
/* 1511 */       return tailMap(paramK, true);
/*      */     }
/*      */ 
/*      */     final class DescendingSubMapEntryIterator extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>>
/*      */     {
/*      */       DescendingSubMapEntryIterator(TreeMap.Entry<K, V> arg2)
/*      */       {
/* 1665 */         super(localEntry1, localEntry2);
/*      */       }
/*      */ 
/*      */       public Map.Entry<K, V> next() {
/* 1669 */         return prevEntry();
/*      */       }
/*      */       public void remove() {
/* 1672 */         removeDescending();
/*      */       }
/*      */     }
/*      */ 
/*      */     final class DescendingSubMapKeyIterator extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<K>
/*      */     {
/*      */       DescendingSubMapKeyIterator(TreeMap.Entry<K, V> arg2) {
/* 1679 */         super(localEntry1, localEntry2);
/*      */       }
/*      */       public K next() {
/* 1682 */         return prevEntry().key;
/*      */       }
/*      */       public void remove() {
/* 1685 */         removeDescending();
/*      */       }
/*      */     }
/*      */ 
/*      */     abstract class EntrySetView extends AbstractSet<Map.Entry<K, V>>
/*      */     {
/* 1517 */       private transient int size = -1;
/*      */       private transient int sizeModCount;
/*      */ 
/*      */       EntrySetView()
/*      */       {
/*      */       }
/*      */ 
/*      */       public int size()
/*      */       {
/* 1520 */         if ((TreeMap.NavigableSubMap.this.fromStart) && (TreeMap.NavigableSubMap.this.toEnd))
/* 1521 */           return TreeMap.NavigableSubMap.this.m.size();
/* 1522 */         if ((this.size == -1) || (this.sizeModCount != TreeMap.NavigableSubMap.this.m.modCount)) {
/* 1523 */           this.sizeModCount = TreeMap.NavigableSubMap.this.m.modCount;
/* 1524 */           this.size = 0;
/* 1525 */           Iterator localIterator = iterator();
/* 1526 */           while (localIterator.hasNext()) {
/* 1527 */             this.size += 1;
/* 1528 */             localIterator.next();
/*      */           }
/*      */         }
/* 1531 */         return this.size;
/*      */       }
/*      */ 
/*      */       public boolean isEmpty() {
/* 1535 */         TreeMap.Entry localEntry = TreeMap.NavigableSubMap.this.absLowest();
/* 1536 */         return (localEntry == null) || (TreeMap.NavigableSubMap.this.tooHigh(localEntry.key));
/*      */       }
/*      */ 
/*      */       public boolean contains(Object paramObject) {
/* 1540 */         if (!(paramObject instanceof Map.Entry))
/* 1541 */           return false;
/* 1542 */         Map.Entry localEntry = (Map.Entry)paramObject;
/* 1543 */         Object localObject = localEntry.getKey();
/* 1544 */         if (!TreeMap.NavigableSubMap.this.inRange(localObject))
/* 1545 */           return false;
/* 1546 */         TreeMap.Entry localEntry1 = TreeMap.NavigableSubMap.this.m.getEntry(localObject);
/* 1547 */         return (localEntry1 != null) && (TreeMap.valEquals(localEntry1.getValue(), localEntry.getValue()));
/*      */       }
/*      */ 
/*      */       public boolean remove(Object paramObject)
/*      */       {
/* 1552 */         if (!(paramObject instanceof Map.Entry))
/* 1553 */           return false;
/* 1554 */         Map.Entry localEntry = (Map.Entry)paramObject;
/* 1555 */         Object localObject = localEntry.getKey();
/* 1556 */         if (!TreeMap.NavigableSubMap.this.inRange(localObject))
/* 1557 */           return false;
/* 1558 */         TreeMap.Entry localEntry1 = TreeMap.NavigableSubMap.this.m.getEntry(localObject);
/* 1559 */         if ((localEntry1 != null) && (TreeMap.valEquals(localEntry1.getValue(), localEntry.getValue())))
/*      */         {
/* 1561 */           TreeMap.NavigableSubMap.this.m.deleteEntry(localEntry1);
/* 1562 */           return true;
/*      */         }
/* 1564 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     final class SubMapEntryIterator extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>>
/*      */     {
/*      */       SubMapEntryIterator(TreeMap.Entry<K, V> arg2)
/*      */       {
/* 1639 */         super(localEntry1, localEntry2);
/*      */       }
/*      */       public Map.Entry<K, V> next() {
/* 1642 */         return nextEntry();
/*      */       }
/*      */       public void remove() {
/* 1645 */         removeAscending();
/*      */       }
/*      */     }
/*      */ 
/*      */     abstract class SubMapIterator<T>
/*      */       implements Iterator<T>
/*      */     {
/* 1580 */       TreeMap.Entry<K, V> lastReturned = null;
/*      */       TreeMap.Entry<K, V> next;
/*      */       final Object fenceKey;
/* 1579 */       int expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
/*      */ 
/*      */       SubMapIterator(TreeMap.Entry<K, V> arg2)
/*      */       {
/*      */         Object localObject1;
/* 1581 */         this.next = localObject1;
/*      */         Object localObject2;
/* 1582 */         this.fenceKey = (localObject2 == null ? TreeMap.UNBOUNDED : localObject2.key);
/*      */       }
/*      */ 
/*      */       public final boolean hasNext() {
/* 1586 */         return (this.next != null) && (this.next.key != this.fenceKey);
/*      */       }
/*      */ 
/*      */       final TreeMap.Entry<K, V> nextEntry() {
/* 1590 */         TreeMap.Entry localEntry = this.next;
/* 1591 */         if ((localEntry == null) || (localEntry.key == this.fenceKey))
/* 1592 */           throw new NoSuchElementException();
/* 1593 */         if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount)
/* 1594 */           throw new ConcurrentModificationException();
/* 1595 */         this.next = TreeMap.successor(localEntry);
/* 1596 */         this.lastReturned = localEntry;
/* 1597 */         return localEntry;
/*      */       }
/*      */ 
/*      */       final TreeMap.Entry<K, V> prevEntry() {
/* 1601 */         TreeMap.Entry localEntry = this.next;
/* 1602 */         if ((localEntry == null) || (localEntry.key == this.fenceKey))
/* 1603 */           throw new NoSuchElementException();
/* 1604 */         if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount)
/* 1605 */           throw new ConcurrentModificationException();
/* 1606 */         this.next = TreeMap.predecessor(localEntry);
/* 1607 */         this.lastReturned = localEntry;
/* 1608 */         return localEntry;
/*      */       }
/*      */ 
/*      */       final void removeAscending() {
/* 1612 */         if (this.lastReturned == null)
/* 1613 */           throw new IllegalStateException();
/* 1614 */         if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount) {
/* 1615 */           throw new ConcurrentModificationException();
/*      */         }
/* 1617 */         if ((this.lastReturned.left != null) && (this.lastReturned.right != null))
/* 1618 */           this.next = this.lastReturned;
/* 1619 */         TreeMap.NavigableSubMap.this.m.deleteEntry(this.lastReturned);
/* 1620 */         this.lastReturned = null;
/* 1621 */         this.expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
/*      */       }
/*      */ 
/*      */       final void removeDescending() {
/* 1625 */         if (this.lastReturned == null)
/* 1626 */           throw new IllegalStateException();
/* 1627 */         if (TreeMap.NavigableSubMap.this.m.modCount != this.expectedModCount)
/* 1628 */           throw new ConcurrentModificationException();
/* 1629 */         TreeMap.NavigableSubMap.this.m.deleteEntry(this.lastReturned);
/* 1630 */         this.lastReturned = null;
/* 1631 */         this.expectedModCount = TreeMap.NavigableSubMap.this.m.modCount;
/*      */       }
/*      */     }
/*      */ 
/*      */     final class SubMapKeyIterator extends TreeMap.NavigableSubMap<K, V>.SubMapIterator<K>
/*      */     {
/*      */       SubMapKeyIterator(TreeMap.Entry<K, V> arg2)
/*      */       {
/* 1652 */         super(localEntry1, localEntry2);
/*      */       }
/*      */       public K next() {
/* 1655 */         return nextEntry().key;
/*      */       }
/*      */       public void remove() {
/* 1658 */         removeAscending();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class PrivateEntryIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     TreeMap.Entry<K, V> next;
/* 1102 */     TreeMap.Entry<K, V> lastReturned = null;
/*      */ 
/* 1101 */     int expectedModCount = TreeMap.this.modCount;
/*      */ 
/*      */     PrivateEntryIterator()
/*      */     {
/*      */       Object localObject;
/* 1103 */       this.next = localObject;
/*      */     }
/*      */ 
/*      */     public final boolean hasNext() {
/* 1107 */       return this.next != null;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> nextEntry() {
/* 1111 */       TreeMap.Entry localEntry = this.next;
/* 1112 */       if (localEntry == null)
/* 1113 */         throw new NoSuchElementException();
/* 1114 */       if (TreeMap.this.modCount != this.expectedModCount)
/* 1115 */         throw new ConcurrentModificationException();
/* 1116 */       this.next = TreeMap.successor(localEntry);
/* 1117 */       this.lastReturned = localEntry;
/* 1118 */       return localEntry;
/*      */     }
/*      */ 
/*      */     final TreeMap.Entry<K, V> prevEntry() {
/* 1122 */       TreeMap.Entry localEntry = this.next;
/* 1123 */       if (localEntry == null)
/* 1124 */         throw new NoSuchElementException();
/* 1125 */       if (TreeMap.this.modCount != this.expectedModCount)
/* 1126 */         throw new ConcurrentModificationException();
/* 1127 */       this.next = TreeMap.predecessor(localEntry);
/* 1128 */       this.lastReturned = localEntry;
/* 1129 */       return localEntry;
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1133 */       if (this.lastReturned == null)
/* 1134 */         throw new IllegalStateException();
/* 1135 */       if (TreeMap.this.modCount != this.expectedModCount) {
/* 1136 */         throw new ConcurrentModificationException();
/*      */       }
/* 1138 */       if ((this.lastReturned.left != null) && (this.lastReturned.right != null))
/* 1139 */         this.next = this.lastReturned;
/* 1140 */       TreeMap.this.deleteEntry(this.lastReturned);
/* 1141 */       this.expectedModCount = TreeMap.this.modCount;
/* 1142 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SubMap extends AbstractMap<K, V>
/*      */     implements SortedMap<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -6520786458950516097L;
/* 1862 */     private boolean fromStart = false; private boolean toEnd = false;
/*      */     private K fromKey;
/*      */     private K toKey;
/*      */ 
/*      */     private SubMap()
/*      */     {
/*      */     }
/*      */ 
/*      */     private Object readResolve()
/*      */     {
/* 1865 */       return new TreeMap.AscendingSubMap(TreeMap.this, this.fromStart, this.fromKey, true, this.toEnd, this.toKey, false);
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1869 */       throw new InternalError(); } 
/* 1870 */     public K lastKey() { throw new InternalError(); } 
/* 1871 */     public K firstKey() { throw new InternalError(); } 
/* 1872 */     public SortedMap<K, V> subMap(K paramK1, K paramK2) { throw new InternalError(); } 
/* 1873 */     public SortedMap<K, V> headMap(K paramK) { throw new InternalError(); } 
/* 1874 */     public SortedMap<K, V> tailMap(K paramK) { throw new InternalError(); } 
/* 1875 */     public Comparator<? super K> comparator() { throw new InternalError(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   final class ValueIterator extends TreeMap<K, V>.PrivateEntryIterator<V>
/*      */   {
/*      */     ValueIterator()
/*      */     {
/* 1157 */       super(localEntry);
/*      */     }
/*      */     public V next() {
/* 1160 */       return nextEntry().value;
/*      */     }
/*      */   }
/*      */ 
/*      */   class Values extends AbstractCollection<V>
/*      */   {
/*      */     Values()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<V> iterator()
/*      */     {
/*  947 */       return new TreeMap.ValueIterator(TreeMap.this, TreeMap.this.getFirstEntry());
/*      */     }
/*      */ 
/*      */     public int size() {
/*  951 */       return TreeMap.this.size();
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/*  955 */       return TreeMap.this.containsValue(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/*  959 */       for (TreeMap.Entry localEntry = TreeMap.this.getFirstEntry(); localEntry != null; localEntry = TreeMap.successor(localEntry)) {
/*  960 */         if (TreeMap.valEquals(localEntry.getValue(), paramObject)) {
/*  961 */           TreeMap.this.deleteEntry(localEntry);
/*  962 */           return true;
/*      */         }
/*      */       }
/*  965 */       return false;
/*      */     }
/*      */ 
/*      */     public void clear() {
/*  969 */       TreeMap.this.clear();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.TreeMap
 * JD-Core Version:    0.6.2
 */