/*      */ package java.util;
/*      */ 
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessController;
/*      */ import sun.misc.Hashing;
/*      */ import sun.misc.VM;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class WeakHashMap<K, V> extends AbstractMap<K, V>
/*      */   implements Map<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   Entry<K, V>[] table;
/*      */   private int size;
/*      */   private int threshold;
/*      */   private final float loadFactor;
/*  175 */   private final ReferenceQueue<Object> queue = new ReferenceQueue();
/*      */   int modCount;
/*      */   static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = 2147483647;
/*      */   transient boolean useAltHashing;
/*  246 */   final transient int hashSeed = Hashing.randomHashSeed(this);
/*      */ 
/*  323 */   private static final Object NULL_KEY = new Object();
/*      */ 
/*  927 */   private transient Set<Map.Entry<K, V>> entrySet = null;
/*      */ 
/*      */   private Entry<K, V>[] newTable(int paramInt)
/*      */   {
/*  250 */     return (Entry[])new Entry[paramInt];
/*      */   }
/*      */ 
/*      */   public WeakHashMap(int paramInt, float paramFloat)
/*      */   {
/*  263 */     if (paramInt < 0) {
/*  264 */       throw new IllegalArgumentException("Illegal Initial Capacity: " + paramInt);
/*      */     }
/*  266 */     if (paramInt > 1073741824) {
/*  267 */       paramInt = 1073741824;
/*      */     }
/*  269 */     if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
/*  270 */       throw new IllegalArgumentException("Illegal Load factor: " + paramFloat);
/*      */     }
/*  272 */     int i = 1;
/*  273 */     while (i < paramInt)
/*  274 */       i <<= 1;
/*  275 */     this.table = newTable(i);
/*  276 */     this.loadFactor = paramFloat;
/*  277 */     this.threshold = ((int)(i * paramFloat));
/*  278 */     this.useAltHashing = ((VM.isBooted()) && (i >= Holder.ALTERNATIVE_HASHING_THRESHOLD));
/*      */   }
/*      */ 
/*      */   public WeakHashMap(int paramInt)
/*      */   {
/*  290 */     this(paramInt, 0.75F);
/*      */   }
/*      */ 
/*      */   public WeakHashMap()
/*      */   {
/*  298 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */   public WeakHashMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  312 */     this(Math.max((int)(paramMap.size() / 0.75F) + 1, 16), 0.75F);
/*      */ 
/*  315 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   private static Object maskNull(Object paramObject)
/*      */   {
/*  329 */     return paramObject == null ? NULL_KEY : paramObject;
/*      */   }
/*      */ 
/*      */   static Object unmaskNull(Object paramObject)
/*      */   {
/*  336 */     return paramObject == NULL_KEY ? null : paramObject;
/*      */   }
/*      */ 
/*      */   private static boolean eq(Object paramObject1, Object paramObject2)
/*      */   {
/*  344 */     return (paramObject1 == paramObject2) || (paramObject1.equals(paramObject2));
/*      */   }
/*      */ 
/*      */   int hash(Object paramObject)
/*      */   {
/*      */     int i;
/*  357 */     if (this.useAltHashing) {
/*  358 */       i = this.hashSeed;
/*  359 */       if ((paramObject instanceof String)) {
/*  360 */         return Hashing.stringHash32((String)paramObject);
/*      */       }
/*  362 */       i ^= paramObject.hashCode();
/*      */     }
/*      */     else {
/*  365 */       i = paramObject.hashCode();
/*      */     }
/*      */ 
/*  371 */     i ^= i >>> 20 ^ i >>> 12;
/*  372 */     return i ^ i >>> 7 ^ i >>> 4;
/*      */   }
/*      */ 
/*      */   private static int indexFor(int paramInt1, int paramInt2)
/*      */   {
/*  379 */     return paramInt1 & paramInt2 - 1;
/*      */   }
/*      */ 
/*      */   private void expungeStaleEntries()
/*      */   {
/*      */     Reference localReference;
/*  386 */     while ((localReference = this.queue.poll()) != null)
/*  387 */       synchronized (this.queue)
/*      */       {
/*  389 */         Entry localEntry1 = (Entry)localReference;
/*  390 */         int i = indexFor(localEntry1.hash, this.table.length);
/*      */ 
/*  392 */         Object localObject1 = this.table[i];
/*  393 */         Object localObject2 = localObject1;
/*  394 */         while (localObject2 != null) {
/*  395 */           Entry localEntry2 = localObject2.next;
/*  396 */           if (localObject2 == localEntry1) {
/*  397 */             if (localObject1 == localEntry1)
/*  398 */               this.table[i] = localEntry2;
/*      */             else {
/*  400 */               ((Entry)localObject1).next = localEntry2;
/*      */             }
/*      */ 
/*  403 */             localEntry1.value = null;
/*  404 */             this.size -= 1;
/*  405 */             break;
/*      */           }
/*  407 */           localObject1 = localObject2;
/*  408 */           localObject2 = localEntry2;
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private Entry<K, V>[] getTable()
/*      */   {
/*  418 */     expungeStaleEntries();
/*  419 */     return this.table;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  429 */     if (this.size == 0)
/*  430 */       return 0;
/*  431 */     expungeStaleEntries();
/*  432 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  442 */     return size() == 0;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/*  463 */     Object localObject = maskNull(paramObject);
/*  464 */     int i = hash(localObject);
/*  465 */     Entry[] arrayOfEntry = getTable();
/*  466 */     int j = indexFor(i, arrayOfEntry.length);
/*  467 */     Entry localEntry = arrayOfEntry[j];
/*  468 */     while (localEntry != null) {
/*  469 */       if ((localEntry.hash == i) && (eq(localObject, localEntry.get())))
/*  470 */         return localEntry.value;
/*  471 */       localEntry = localEntry.next;
/*      */     }
/*  473 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/*  485 */     return getEntry(paramObject) != null;
/*      */   }
/*      */ 
/*      */   Entry<K, V> getEntry(Object paramObject)
/*      */   {
/*  493 */     Object localObject = maskNull(paramObject);
/*  494 */     int i = hash(localObject);
/*  495 */     Entry[] arrayOfEntry = getTable();
/*  496 */     int j = indexFor(i, arrayOfEntry.length);
/*  497 */     Entry localEntry = arrayOfEntry[j];
/*  498 */     while ((localEntry != null) && ((localEntry.hash != i) || (!eq(localObject, localEntry.get()))))
/*  499 */       localEntry = localEntry.next;
/*  500 */     return localEntry;
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/*  516 */     Object localObject1 = maskNull(paramK);
/*  517 */     int i = hash(localObject1);
/*  518 */     Entry[] arrayOfEntry = getTable();
/*  519 */     int j = indexFor(i, arrayOfEntry.length);
/*      */ 
/*  521 */     for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next) {
/*  522 */       if ((i == localEntry.hash) && (eq(localObject1, localEntry.get()))) {
/*  523 */         Object localObject2 = localEntry.value;
/*  524 */         if (paramV != localObject2)
/*  525 */           localEntry.value = paramV;
/*  526 */         return localObject2;
/*      */       }
/*      */     }
/*      */ 
/*  530 */     this.modCount += 1;
/*  531 */     localEntry = arrayOfEntry[j];
/*  532 */     arrayOfEntry[j] = new Entry(localObject1, paramV, this.queue, i, localEntry);
/*  533 */     if (++this.size >= this.threshold)
/*  534 */       resize(arrayOfEntry.length * 2);
/*  535 */     return null;
/*      */   }
/*      */ 
/*      */   void resize(int paramInt)
/*      */   {
/*  553 */     Entry[] arrayOfEntry1 = getTable();
/*  554 */     int i = arrayOfEntry1.length;
/*  555 */     if (i == 1073741824) {
/*  556 */       this.threshold = 2147483647;
/*  557 */       return;
/*      */     }
/*      */ 
/*  560 */     Entry[] arrayOfEntry2 = newTable(paramInt);
/*  561 */     boolean bool1 = this.useAltHashing;
/*  562 */     this.useAltHashing |= ((VM.isBooted()) && (paramInt >= Holder.ALTERNATIVE_HASHING_THRESHOLD));
/*      */ 
/*  564 */     boolean bool2 = bool1 ^ this.useAltHashing;
/*  565 */     transfer(arrayOfEntry1, arrayOfEntry2, bool2);
/*  566 */     this.table = arrayOfEntry2;
/*      */ 
/*  573 */     if (this.size >= this.threshold / 2) {
/*  574 */       this.threshold = ((int)(paramInt * this.loadFactor));
/*      */     } else {
/*  576 */       expungeStaleEntries();
/*  577 */       transfer(arrayOfEntry2, arrayOfEntry1, false);
/*  578 */       this.table = arrayOfEntry1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void transfer(Entry<K, V>[] paramArrayOfEntry1, Entry<K, V>[] paramArrayOfEntry2, boolean paramBoolean)
/*      */   {
/*  584 */     for (int i = 0; i < paramArrayOfEntry1.length; i++) {
/*  585 */       Object localObject1 = paramArrayOfEntry1[i];
/*  586 */       paramArrayOfEntry1[i] = null;
/*  587 */       while (localObject1 != null) {
/*  588 */         Entry localEntry = ((Entry)localObject1).next;
/*  589 */         Object localObject2 = ((Entry)localObject1).get();
/*  590 */         if (localObject2 == null) {
/*  591 */           ((Entry)localObject1).next = null;
/*  592 */           ((Entry)localObject1).value = null;
/*  593 */           this.size -= 1;
/*      */         } else {
/*  595 */           if (paramBoolean) {
/*  596 */             ((Entry)localObject1).hash = hash(localObject2);
/*      */           }
/*  598 */           int j = indexFor(((Entry)localObject1).hash, paramArrayOfEntry2.length);
/*  599 */           ((Entry)localObject1).next = paramArrayOfEntry2[j];
/*  600 */           paramArrayOfEntry2[j] = localObject1;
/*      */         }
/*  602 */         localObject1 = localEntry;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  616 */     int i = paramMap.size();
/*  617 */     if (i == 0) {
/*  618 */       return;
/*      */     }
/*      */ 
/*  629 */     if (i > this.threshold) {
/*  630 */       int j = (int)(i / this.loadFactor + 1.0F);
/*  631 */       if (j > 1073741824)
/*  632 */         j = 1073741824;
/*  633 */       int k = this.table.length;
/*  634 */       while (k < j)
/*  635 */         k <<= 1;
/*  636 */       if (k > this.table.length) {
/*  637 */         resize(k);
/*      */       }
/*      */     }
/*  640 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  641 */       put(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/*  665 */     Object localObject1 = maskNull(paramObject);
/*  666 */     int i = hash(localObject1);
/*  667 */     Entry[] arrayOfEntry = getTable();
/*  668 */     int j = indexFor(i, arrayOfEntry.length);
/*  669 */     Object localObject2 = arrayOfEntry[j];
/*  670 */     Object localObject3 = localObject2;
/*      */ 
/*  672 */     while (localObject3 != null) {
/*  673 */       Entry localEntry = localObject3.next;
/*  674 */       if ((i == localObject3.hash) && (eq(localObject1, localObject3.get()))) {
/*  675 */         this.modCount += 1;
/*  676 */         this.size -= 1;
/*  677 */         if (localObject2 == localObject3)
/*  678 */           arrayOfEntry[j] = localEntry;
/*      */         else
/*  680 */           ((Entry)localObject2).next = localEntry;
/*  681 */         return localObject3.value;
/*      */       }
/*  683 */       localObject2 = localObject3;
/*  684 */       localObject3 = localEntry;
/*      */     }
/*      */ 
/*  687 */     return null;
/*      */   }
/*      */ 
/*      */   boolean removeMapping(Object paramObject)
/*      */   {
/*  692 */     if (!(paramObject instanceof Map.Entry))
/*  693 */       return false;
/*  694 */     Entry[] arrayOfEntry = getTable();
/*  695 */     Map.Entry localEntry = (Map.Entry)paramObject;
/*  696 */     Object localObject1 = maskNull(localEntry.getKey());
/*  697 */     int i = hash(localObject1);
/*  698 */     int j = indexFor(i, arrayOfEntry.length);
/*  699 */     Object localObject2 = arrayOfEntry[j];
/*  700 */     Object localObject3 = localObject2;
/*      */ 
/*  702 */     while (localObject3 != null) {
/*  703 */       Entry localEntry1 = localObject3.next;
/*  704 */       if ((i == localObject3.hash) && (localObject3.equals(localEntry))) {
/*  705 */         this.modCount += 1;
/*  706 */         this.size -= 1;
/*  707 */         if (localObject2 == localObject3)
/*  708 */           arrayOfEntry[j] = localEntry1;
/*      */         else
/*  710 */           ((Entry)localObject2).next = localEntry1;
/*  711 */         return true;
/*      */       }
/*  713 */       localObject2 = localObject3;
/*  714 */       localObject3 = localEntry1;
/*      */     }
/*      */ 
/*  717 */     return false;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  727 */     while (this.queue.poll() != null);
/*  730 */     this.modCount += 1;
/*  731 */     Arrays.fill(this.table, null);
/*  732 */     this.size = 0;
/*      */ 
/*  737 */     while (this.queue.poll() != null);
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/*  750 */     if (paramObject == null) {
/*  751 */       return containsNullValue();
/*      */     }
/*  753 */     Entry[] arrayOfEntry = getTable();
/*  754 */     for (int i = arrayOfEntry.length; i-- > 0; )
/*  755 */       for (Entry localEntry = arrayOfEntry[i]; localEntry != null; localEntry = localEntry.next)
/*  756 */         if (paramObject.equals(localEntry.value))
/*  757 */           return true;
/*  758 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean containsNullValue()
/*      */   {
/*  765 */     Entry[] arrayOfEntry = getTable();
/*  766 */     for (int i = arrayOfEntry.length; i-- > 0; )
/*  767 */       for (Entry localEntry = arrayOfEntry[i]; localEntry != null; localEntry = localEntry.next)
/*  768 */         if (localEntry.value == null)
/*  769 */           return true;
/*  770 */     return false;
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/*  943 */     Set localSet = this.keySet;
/*  944 */     return this.keySet = new KeySet(null);
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/*  988 */     Collection localCollection = this.values;
/*  989 */     return this.values = new Values(null);
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1025 */     Set localSet = this.entrySet;
/* 1026 */     return this.entrySet = new EntrySet(null);
/*      */   }
/*      */ 
/*      */   private static class Entry<K, V> extends WeakReference<Object>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     V value;
/*      */     int hash;
/*      */     Entry<K, V> next;
/*      */ 
/*      */     Entry(Object paramObject, V paramV, ReferenceQueue<Object> paramReferenceQueue, int paramInt, Entry<K, V> paramEntry)
/*      */     {
/*  788 */       super(paramReferenceQueue);
/*  789 */       this.value = paramV;
/*  790 */       this.hash = paramInt;
/*  791 */       this.next = paramEntry;
/*      */     }
/*      */ 
/*      */     public K getKey()
/*      */     {
/*  796 */       return WeakHashMap.unmaskNull(get());
/*      */     }
/*      */ 
/*      */     public V getValue() {
/*  800 */       return this.value;
/*      */     }
/*      */ 
/*      */     public V setValue(V paramV) {
/*  804 */       Object localObject = this.value;
/*  805 */       this.value = paramV;
/*  806 */       return localObject;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  810 */       if (!(paramObject instanceof Map.Entry))
/*  811 */         return false;
/*  812 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  813 */       Object localObject1 = getKey();
/*  814 */       Object localObject2 = localEntry.getKey();
/*  815 */       if ((localObject1 == localObject2) || ((localObject1 != null) && (localObject1.equals(localObject2)))) {
/*  816 */         Object localObject3 = getValue();
/*  817 */         Object localObject4 = localEntry.getValue();
/*  818 */         if ((localObject3 == localObject4) || ((localObject3 != null) && (localObject3.equals(localObject4))))
/*  819 */           return true;
/*      */       }
/*  821 */       return false;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  825 */       Object localObject1 = getKey();
/*  826 */       Object localObject2 = getValue();
/*  827 */       return (localObject1 == null ? 0 : localObject1.hashCode()) ^ (localObject2 == null ? 0 : localObject2.hashCode());
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  832 */       return getKey() + "=" + getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class EntryIterator extends WeakHashMap<K, V>.HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     private EntryIterator()
/*      */     {
/*  919 */       super();
/*      */     }
/*  921 */     public Map.Entry<K, V> next() { return nextEntry(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<Map.Entry<K, V>> iterator()
/*      */     {
/* 1031 */       return new WeakHashMap.EntryIterator(WeakHashMap.this, null);
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 1035 */       if (!(paramObject instanceof Map.Entry))
/* 1036 */         return false;
/* 1037 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1038 */       WeakHashMap.Entry localEntry1 = WeakHashMap.this.getEntry(localEntry.getKey());
/* 1039 */       return (localEntry1 != null) && (localEntry1.equals(localEntry));
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/* 1043 */       return WeakHashMap.this.removeMapping(paramObject);
/*      */     }
/*      */ 
/*      */     public int size() {
/* 1047 */       return WeakHashMap.this.size();
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1051 */       WeakHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     private List<Map.Entry<K, V>> deepCopy() {
/* 1055 */       ArrayList localArrayList = new ArrayList(size());
/* 1056 */       for (Map.Entry localEntry : this)
/* 1057 */         localArrayList.add(new AbstractMap.SimpleEntry(localEntry));
/* 1058 */       return localArrayList;
/*      */     }
/*      */ 
/*      */     public Object[] toArray() {
/* 1062 */       return deepCopy().toArray();
/*      */     }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 1066 */       return deepCopy().toArray(paramArrayOfT);
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     private int index;
/*  838 */     private WeakHashMap.Entry<K, V> entry = null;
/*  839 */     private WeakHashMap.Entry<K, V> lastReturned = null;
/*  840 */     private int expectedModCount = WeakHashMap.this.modCount;
/*      */ 
/*  846 */     private Object nextKey = null;
/*      */ 
/*  852 */     private Object currentKey = null;
/*      */ 
/*      */     HashIterator() {
/*  855 */       this.index = (WeakHashMap.this.isEmpty() ? 0 : WeakHashMap.this.table.length);
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  859 */       WeakHashMap.Entry[] arrayOfEntry = WeakHashMap.this.table;
/*      */ 
/*  861 */       while (this.nextKey == null) {
/*  862 */         WeakHashMap.Entry localEntry = this.entry;
/*  863 */         int i = this.index;
/*  864 */         while ((localEntry == null) && (i > 0))
/*  865 */           localEntry = arrayOfEntry[(--i)];
/*  866 */         this.entry = localEntry;
/*  867 */         this.index = i;
/*  868 */         if (localEntry == null) {
/*  869 */           this.currentKey = null;
/*  870 */           return false;
/*      */         }
/*  872 */         this.nextKey = localEntry.get();
/*  873 */         if (this.nextKey == null)
/*  874 */           this.entry = this.entry.next;
/*      */       }
/*  876 */       return true;
/*      */     }
/*      */ 
/*      */     protected WeakHashMap.Entry<K, V> nextEntry()
/*      */     {
/*  881 */       if (WeakHashMap.this.modCount != this.expectedModCount)
/*  882 */         throw new ConcurrentModificationException();
/*  883 */       if ((this.nextKey == null) && (!hasNext())) {
/*  884 */         throw new NoSuchElementException();
/*      */       }
/*  886 */       this.lastReturned = this.entry;
/*  887 */       this.entry = this.entry.next;
/*  888 */       this.currentKey = this.nextKey;
/*  889 */       this.nextKey = null;
/*  890 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  894 */       if (this.lastReturned == null)
/*  895 */         throw new IllegalStateException();
/*  896 */       if (WeakHashMap.this.modCount != this.expectedModCount) {
/*  897 */         throw new ConcurrentModificationException();
/*      */       }
/*  899 */       WeakHashMap.this.remove(this.currentKey);
/*  900 */       this.expectedModCount = WeakHashMap.this.modCount;
/*  901 */       this.lastReturned = null;
/*  902 */       this.currentKey = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Holder
/*      */   {
/*  232 */     static final int ALTERNATIVE_HASHING_THRESHOLD = i;
/*      */ 
/*      */     static
/*      */     {
/*  211 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("jdk.map.althashing.threshold"));
/*      */       int i;
/*      */       try
/*      */       {
/*  217 */         i = null != str ? Integer.parseInt(str) : 2147483647;
/*      */ 
/*  222 */         if (i == -1) {
/*  223 */           i = 2147483647;
/*      */         }
/*      */ 
/*  226 */         if (i < 0)
/*  227 */           throw new IllegalArgumentException("value must be positive integer.");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*  230 */         throw new Error("Illegal value for 'jdk.map.althashing.threshold'", localIllegalArgumentException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class KeyIterator extends WeakHashMap<K, V>.HashIterator<K>
/*      */   {
/*      */     private KeyIterator()
/*      */     {
/*  913 */       super();
/*      */     }
/*  915 */     public K next() { return nextEntry().getKey(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class KeySet extends AbstractSet<K>
/*      */   {
/*      */     private KeySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<K> iterator()
/*      */     {
/*  949 */       return new WeakHashMap.KeyIterator(WeakHashMap.this, null);
/*      */     }
/*      */ 
/*      */     public int size() {
/*  953 */       return WeakHashMap.this.size();
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/*  957 */       return WeakHashMap.this.containsKey(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/*  961 */       if (WeakHashMap.this.containsKey(paramObject)) {
/*  962 */         WeakHashMap.this.remove(paramObject);
/*  963 */         return true;
/*      */       }
/*      */ 
/*  966 */       return false;
/*      */     }
/*      */ 
/*      */     public void clear() {
/*  970 */       WeakHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ValueIterator extends WeakHashMap<K, V>.HashIterator<V>
/*      */   {
/*      */     private ValueIterator()
/*      */     {
/*  907 */       super();
/*      */     }
/*  909 */     public V next() { return nextEntry().value; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private class Values extends AbstractCollection<V>
/*      */   {
/*      */     private Values()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<V> iterator()
/*      */     {
/*  994 */       return new WeakHashMap.ValueIterator(WeakHashMap.this, null);
/*      */     }
/*      */ 
/*      */     public int size() {
/*  998 */       return WeakHashMap.this.size();
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 1002 */       return WeakHashMap.this.containsValue(paramObject);
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 1006 */       WeakHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.WeakHashMap
 * JD-Core Version:    0.6.2
 */