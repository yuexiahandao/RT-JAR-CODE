/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.security.AccessController;
/*      */ import sun.misc.Hashing;
/*      */ import sun.misc.VM;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class Hashtable<K, V> extends Dictionary<K, V>
/*      */   implements Map<K, V>, Cloneable, Serializable
/*      */ {
/*      */   private transient Entry<K, V>[] table;
/*      */   private transient int count;
/*      */   private int threshold;
/*      */   private float loadFactor;
/*  161 */   private transient int modCount = 0;
/*      */   private static final long serialVersionUID = 1421746759512286392L;
/*      */   static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = 2147483647;
/*      */   transient int hashSeed;
/*      */   private static final int MAX_ARRAY_SIZE = 2147483639;
/*  684 */   private volatile transient Set<K> keySet = null;
/*  685 */   private volatile transient Set<Map.Entry<K, V>> entrySet = null;
/*  686 */   private volatile transient Collection<V> values = null;
/*      */   private static final int KEYS = 0;
/*      */   private static final int VALUES = 1;
/*      */   private static final int ENTRIES = 2;
/*      */ 
/*      */   final boolean initHashSeedAsNeeded(int paramInt)
/*      */   {
/*  225 */     int i = this.hashSeed != 0 ? 1 : 0;
/*  226 */     int j = (VM.isBooted()) && (paramInt >= Holder.ALTERNATIVE_HASHING_THRESHOLD) ? 1 : 0;
/*      */ 
/*  228 */     boolean bool = i ^ j;
/*  229 */     if (bool) {
/*  230 */       this.hashSeed = (j != 0 ? Hashing.randomHashSeed(this) : 0);
/*      */     }
/*      */ 
/*  234 */     return bool;
/*      */   }
/*      */ 
/*      */   private int hash(Object paramObject)
/*      */   {
/*  239 */     return this.hashSeed ^ paramObject.hashCode();
/*      */   }
/*      */ 
/*      */   public Hashtable(int paramInt, float paramFloat)
/*      */   {
/*  252 */     if (paramInt < 0) {
/*  253 */       throw new IllegalArgumentException("Illegal Capacity: " + paramInt);
/*      */     }
/*  255 */     if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
/*  256 */       throw new IllegalArgumentException("Illegal Load: " + paramFloat);
/*      */     }
/*  258 */     if (paramInt == 0)
/*  259 */       paramInt = 1;
/*  260 */     this.loadFactor = paramFloat;
/*  261 */     this.table = new Entry[paramInt];
/*  262 */     this.threshold = ((int)Math.min(paramInt * paramFloat, 2.147484E+009F));
/*  263 */     initHashSeedAsNeeded(paramInt);
/*      */   }
/*      */ 
/*      */   public Hashtable(int paramInt)
/*      */   {
/*  275 */     this(paramInt, 0.75F);
/*      */   }
/*      */ 
/*      */   public Hashtable()
/*      */   {
/*  283 */     this(11, 0.75F);
/*      */   }
/*      */ 
/*      */   public Hashtable(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  296 */     this(Math.max(2 * paramMap.size(), 11), 0.75F);
/*  297 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public synchronized int size()
/*      */   {
/*  306 */     return this.count;
/*      */   }
/*      */ 
/*      */   public synchronized boolean isEmpty()
/*      */   {
/*  316 */     return this.count == 0;
/*      */   }
/*      */ 
/*      */   public synchronized Enumeration<K> keys()
/*      */   {
/*  329 */     return getEnumeration(0);
/*      */   }
/*      */ 
/*      */   public synchronized Enumeration<V> elements()
/*      */   {
/*  344 */     return getEnumeration(1);
/*      */   }
/*      */ 
/*      */   public synchronized boolean contains(Object paramObject)
/*      */   {
/*  364 */     if (paramObject == null) {
/*  365 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  368 */     Entry[] arrayOfEntry = this.table;
/*  369 */     for (int i = arrayOfEntry.length; i-- > 0; ) {
/*  370 */       for (Entry localEntry = arrayOfEntry[i]; localEntry != null; localEntry = localEntry.next) {
/*  371 */         if (localEntry.value.equals(paramObject)) {
/*  372 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  376 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/*  392 */     return contains(paramObject);
/*      */   }
/*      */ 
/*      */   public synchronized boolean containsKey(Object paramObject)
/*      */   {
/*  406 */     Entry[] arrayOfEntry = this.table;
/*  407 */     int i = hash(paramObject);
/*  408 */     int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*  409 */     for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next) {
/*  410 */       if ((localEntry.hash == i) && (localEntry.key.equals(paramObject))) {
/*  411 */         return true;
/*      */       }
/*      */     }
/*  414 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized V get(Object paramObject)
/*      */   {
/*  433 */     Entry[] arrayOfEntry = this.table;
/*  434 */     int i = hash(paramObject);
/*  435 */     int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*  436 */     for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next) {
/*  437 */       if ((localEntry.hash == i) && (localEntry.key.equals(paramObject))) {
/*  438 */         return localEntry.value;
/*      */       }
/*      */     }
/*  441 */     return null;
/*      */   }
/*      */ 
/*      */   protected void rehash()
/*      */   {
/*  460 */     int i = this.table.length;
/*  461 */     Entry[] arrayOfEntry1 = this.table;
/*      */ 
/*  464 */     int j = (i << 1) + 1;
/*  465 */     if (j - 2147483639 > 0) {
/*  466 */       if (i == 2147483639)
/*      */       {
/*  468 */         return;
/*  469 */       }j = 2147483639;
/*      */     }
/*  471 */     Entry[] arrayOfEntry2 = new Entry[j];
/*      */ 
/*  473 */     this.modCount += 1;
/*  474 */     this.threshold = ((int)Math.min(j * this.loadFactor, 2.147484E+009F));
/*  475 */     boolean bool = initHashSeedAsNeeded(j);
/*      */ 
/*  477 */     this.table = arrayOfEntry2;
/*      */ 
/*  479 */     for (int k = i; k-- > 0; )
/*  480 */       for (localEntry1 = arrayOfEntry1[k]; localEntry1 != null; ) {
/*  481 */         Entry localEntry2 = localEntry1;
/*  482 */         localEntry1 = localEntry1.next;
/*      */ 
/*  484 */         if (bool) {
/*  485 */           localEntry2.hash = hash(localEntry2.key);
/*      */         }
/*  487 */         int m = (localEntry2.hash & 0x7FFFFFFF) % j;
/*  488 */         localEntry2.next = arrayOfEntry2[m];
/*  489 */         arrayOfEntry2[m] = localEntry2;
/*      */       }
/*      */     Entry localEntry1;
/*      */   }
/*      */ 
/*      */   public synchronized V put(K paramK, V paramV)
/*      */   {
/*  513 */     if (paramV == null) {
/*  514 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  518 */     Entry[] arrayOfEntry = this.table;
/*  519 */     int i = hash(paramK);
/*  520 */     int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*  521 */     for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next) {
/*  522 */       if ((localEntry.hash == i) && (localEntry.key.equals(paramK))) {
/*  523 */         Object localObject = localEntry.value;
/*  524 */         localEntry.value = paramV;
/*  525 */         return localObject;
/*      */       }
/*      */     }
/*      */ 
/*  529 */     this.modCount += 1;
/*  530 */     if (this.count >= this.threshold)
/*      */     {
/*  532 */       rehash();
/*      */ 
/*  534 */       arrayOfEntry = this.table;
/*  535 */       i = hash(paramK);
/*  536 */       j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*      */     }
/*      */ 
/*  540 */     localEntry = arrayOfEntry[j];
/*  541 */     arrayOfEntry[j] = new Entry(i, paramK, paramV, localEntry);
/*  542 */     this.count += 1;
/*  543 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized V remove(Object paramObject)
/*      */   {
/*  556 */     Entry[] arrayOfEntry = this.table;
/*  557 */     int i = hash(paramObject);
/*  558 */     int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*  559 */     Entry localEntry1 = arrayOfEntry[j]; for (Entry localEntry2 = null; localEntry1 != null; localEntry1 = localEntry1.next) {
/*  560 */       if ((localEntry1.hash == i) && (localEntry1.key.equals(paramObject))) {
/*  561 */         this.modCount += 1;
/*  562 */         if (localEntry2 != null)
/*  563 */           localEntry2.next = localEntry1.next;
/*      */         else {
/*  565 */           arrayOfEntry[j] = localEntry1.next;
/*      */         }
/*  567 */         this.count -= 1;
/*  568 */         Object localObject = localEntry1.value;
/*  569 */         localEntry1.value = null;
/*  570 */         return localObject;
/*      */       }
/*  559 */       localEntry2 = localEntry1;
/*      */     }
/*      */ 
/*  573 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  586 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  587 */       put(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   public synchronized void clear()
/*      */   {
/*  594 */     Entry[] arrayOfEntry = this.table;
/*  595 */     this.modCount += 1;
/*  596 */     int i = arrayOfEntry.length;
/*      */     while (true) { i--; if (i < 0) break;
/*  597 */       arrayOfEntry[i] = null; }
/*  598 */     this.count = 0;
/*      */   }
/*      */ 
/*      */   public synchronized Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  610 */       Hashtable localHashtable = (Hashtable)super.clone();
/*  611 */       localHashtable.table = new Entry[this.table.length];
/*  612 */       for (int i = this.table.length; i-- > 0; ) {
/*  613 */         localHashtable.table[i] = (this.table[i] != null ? (Entry)this.table[i].clone() : null);
/*      */       }
/*      */ 
/*  616 */       localHashtable.keySet = null;
/*  617 */       localHashtable.entrySet = null;
/*  618 */       localHashtable.values = null;
/*  619 */       localHashtable.modCount = 0;
/*  620 */       return localHashtable;
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  623 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public synchronized String toString()
/*      */   {
/*  638 */     int i = size() - 1;
/*  639 */     if (i == -1) {
/*  640 */       return "{}";
/*      */     }
/*  642 */     StringBuilder localStringBuilder = new StringBuilder();
/*  643 */     Iterator localIterator = entrySet().iterator();
/*      */ 
/*  645 */     localStringBuilder.append('{');
/*  646 */     for (int j = 0; ; j++) {
/*  647 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  648 */       Object localObject1 = localEntry.getKey();
/*  649 */       Object localObject2 = localEntry.getValue();
/*  650 */       localStringBuilder.append(localObject1 == this ? "(this Map)" : localObject1.toString());
/*  651 */       localStringBuilder.append('=');
/*  652 */       localStringBuilder.append(localObject2 == this ? "(this Map)" : localObject2.toString());
/*      */ 
/*  654 */       if (j == i)
/*  655 */         return '}';
/*  656 */       localStringBuilder.append(", ");
/*      */     }
/*      */   }
/*      */ 
/*      */   private <T> Enumeration<T> getEnumeration(int paramInt)
/*      */   {
/*  662 */     if (this.count == 0) {
/*  663 */       return Collections.emptyEnumeration();
/*      */     }
/*  665 */     return new Enumerator(paramInt, false);
/*      */   }
/*      */ 
/*      */   private <T> Iterator<T> getIterator(int paramInt)
/*      */   {
/*  670 */     if (this.count == 0) {
/*  671 */       return Collections.emptyIterator();
/*      */     }
/*  673 */     return new Enumerator(paramInt, true);
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/*  704 */     if (this.keySet == null)
/*  705 */       this.keySet = Collections.synchronizedSet(new KeySet(null), this);
/*  706 */     return this.keySet;
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/*  744 */     if (this.entrySet == null)
/*  745 */       this.entrySet = Collections.synchronizedSet(new EntrySet(null), this);
/*  746 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/*  824 */     if (this.values == null) {
/*  825 */       this.values = Collections.synchronizedCollection(new ValueCollection(null), this);
/*      */     }
/*  827 */     return this.values;
/*      */   }
/*      */ 
/*      */   public synchronized boolean equals(Object paramObject)
/*      */   {
/*  857 */     if (paramObject == this) {
/*  858 */       return true;
/*      */     }
/*  860 */     if (!(paramObject instanceof Map))
/*  861 */       return false;
/*  862 */     Map localMap = (Map)paramObject;
/*  863 */     if (localMap.size() != size())
/*  864 */       return false;
/*      */     try
/*      */     {
/*  867 */       Iterator localIterator = entrySet().iterator();
/*  868 */       while (localIterator.hasNext()) {
/*  869 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  870 */         Object localObject1 = localEntry.getKey();
/*  871 */         Object localObject2 = localEntry.getValue();
/*  872 */         if (localObject2 == null) {
/*  873 */           if ((localMap.get(localObject1) != null) || (!localMap.containsKey(localObject1)))
/*  874 */             return false;
/*      */         }
/*  876 */         else if (!localObject2.equals(localMap.get(localObject1)))
/*  877 */           return false;
/*      */       }
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  881 */       return false;
/*      */     } catch (NullPointerException localNullPointerException) {
/*  883 */       return false;
/*      */     }
/*      */ 
/*  886 */     return true;
/*      */   }
/*      */ 
/*      */   public synchronized int hashCode()
/*      */   {
/*  907 */     int i = 0;
/*  908 */     if ((this.count == 0) || (this.loadFactor < 0.0F)) {
/*  909 */       return i;
/*      */     }
/*  911 */     this.loadFactor = (-this.loadFactor);
/*  912 */     Entry[] arrayOfEntry1 = this.table;
/*  913 */     for (Entry localEntry : arrayOfEntry1)
/*  914 */       while (localEntry != null) {
/*  915 */         i += localEntry.hashCode();
/*  916 */         localEntry = localEntry.next;
/*      */       }
/*  918 */     this.loadFactor = (-this.loadFactor);
/*      */ 
/*  920 */     return i;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  935 */     Entry localEntry1 = null;
/*      */ 
/*  937 */     synchronized (this)
/*      */     {
/*  939 */       paramObjectOutputStream.defaultWriteObject();
/*      */ 
/*  942 */       paramObjectOutputStream.writeInt(this.table.length);
/*  943 */       paramObjectOutputStream.writeInt(this.count);
/*      */ 
/*  946 */       for (int i = 0; i < this.table.length; i++) {
/*  947 */         Entry localEntry2 = this.table[i];
/*      */ 
/*  949 */         while (localEntry2 != null) {
/*  950 */           localEntry1 = new Entry(0, localEntry2.key, localEntry2.value, localEntry1);
/*      */ 
/*  952 */           localEntry2 = localEntry2.next;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  958 */     while (localEntry1 != null) {
/*  959 */       paramObjectOutputStream.writeObject(localEntry1.key);
/*  960 */       paramObjectOutputStream.writeObject(localEntry1.value);
/*  961 */       localEntry1 = localEntry1.next;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  972 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/*  975 */     int i = paramObjectInputStream.readInt();
/*  976 */     int j = paramObjectInputStream.readInt();
/*      */ 
/*  982 */     int k = (int)(j * this.loadFactor) + j / 20 + 3;
/*  983 */     if ((k > j) && ((k & 0x1) == 0))
/*  984 */       k--;
/*  985 */     if ((i > 0) && (k > i)) {
/*  986 */       k = i;
/*      */     }
/*  988 */     Entry[] arrayOfEntry = new Entry[k];
/*  989 */     this.threshold = ((int)Math.min(k * this.loadFactor, 2.147484E+009F));
/*  990 */     this.count = 0;
/*  991 */     initHashSeedAsNeeded(k);
/*      */ 
/*  994 */     for (; j > 0; j--) {
/*  995 */       Object localObject1 = paramObjectInputStream.readObject();
/*  996 */       Object localObject2 = paramObjectInputStream.readObject();
/*      */ 
/*  998 */       reconstitutionPut(arrayOfEntry, localObject1, localObject2);
/*      */     }
/* 1000 */     this.table = arrayOfEntry;
/*      */   }
/*      */ 
/*      */   private void reconstitutionPut(Entry<K, V>[] paramArrayOfEntry, K paramK, V paramV)
/*      */     throws StreamCorruptedException
/*      */   {
/* 1017 */     if (paramV == null) {
/* 1018 */       throw new StreamCorruptedException();
/*      */     }
/*      */ 
/* 1022 */     int i = hash(paramK);
/* 1023 */     int j = (i & 0x7FFFFFFF) % paramArrayOfEntry.length;
/* 1024 */     for (Object localObject = paramArrayOfEntry[j]; localObject != null; localObject = ((Entry)localObject).next) {
/* 1025 */       if ((((Entry)localObject).hash == i) && (((Entry)localObject).key.equals(paramK))) {
/* 1026 */         throw new StreamCorruptedException();
/*      */       }
/*      */     }
/*      */ 
/* 1030 */     localObject = paramArrayOfEntry[j];
/* 1031 */     paramArrayOfEntry[j] = new Entry(i, paramK, paramV, (Entry)localObject);
/* 1032 */     this.count += 1;
/*      */   }
/*      */ 
/*      */   private static class Entry<K, V> implements Map.Entry<K, V> {
/*      */     int hash;
/*      */     final K key;
/*      */     V value;
/*      */     Entry<K, V> next;
/*      */ 
/*      */     protected Entry(int paramInt, K paramK, V paramV, Entry<K, V> paramEntry) {
/* 1045 */       this.hash = paramInt;
/* 1046 */       this.key = paramK;
/* 1047 */       this.value = paramV;
/* 1048 */       this.next = paramEntry;
/*      */     }
/*      */ 
/*      */     protected Object clone() {
/* 1052 */       return new Entry(this.hash, this.key, this.value, this.next == null ? null : (Entry)this.next.clone());
/*      */     }
/*      */ 
/*      */     public K getKey()
/*      */     {
/* 1059 */       return this.key;
/*      */     }
/*      */ 
/*      */     public V getValue() {
/* 1063 */       return this.value;
/*      */     }
/*      */ 
/*      */     public V setValue(V paramV) {
/* 1067 */       if (paramV == null) {
/* 1068 */         throw new NullPointerException();
/*      */       }
/* 1070 */       Object localObject = this.value;
/* 1071 */       this.value = paramV;
/* 1072 */       return localObject;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 1076 */       if (!(paramObject instanceof Map.Entry))
/* 1077 */         return false;
/* 1078 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*      */ 
/* 1080 */       return (this.key.equals(localEntry.getKey())) && (this.value.equals(localEntry.getValue()));
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/* 1084 */       return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1088 */       return this.key.toString() + "=" + this.value.toString();
/*      */     }
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
/*  751 */       return Hashtable.this.getIterator(2);
/*      */     }
/*      */ 
/*      */     public boolean add(Map.Entry<K, V> paramEntry) {
/*  755 */       return super.add(paramEntry);
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/*  759 */       if (!(paramObject instanceof Map.Entry))
/*  760 */         return false;
/*  761 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  762 */       Object localObject = localEntry.getKey();
/*  763 */       Hashtable.Entry[] arrayOfEntry = Hashtable.this.table;
/*  764 */       int i = Hashtable.this.hash(localObject);
/*  765 */       int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*      */ 
/*  767 */       for (Hashtable.Entry localEntry1 = arrayOfEntry[j]; localEntry1 != null; localEntry1 = localEntry1.next)
/*  768 */         if ((localEntry1.hash == i) && (localEntry1.equals(localEntry)))
/*  769 */           return true;
/*  770 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject) {
/*  774 */       if (!(paramObject instanceof Map.Entry))
/*  775 */         return false;
/*  776 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  777 */       Object localObject = localEntry.getKey();
/*  778 */       Hashtable.Entry[] arrayOfEntry = Hashtable.this.table;
/*  779 */       int i = Hashtable.this.hash(localObject);
/*  780 */       int j = (i & 0x7FFFFFFF) % arrayOfEntry.length;
/*      */ 
/*  782 */       Hashtable.Entry localEntry1 = arrayOfEntry[j]; for (Hashtable.Entry localEntry2 = null; localEntry1 != null; 
/*  783 */         localEntry1 = localEntry1.next) {
/*  784 */         if ((localEntry1.hash == i) && (localEntry1.equals(localEntry))) {
/*  785 */           Hashtable.access$608(Hashtable.this);
/*  786 */           if (localEntry2 != null)
/*  787 */             localEntry2.next = localEntry1.next;
/*      */           else {
/*  789 */             arrayOfEntry[j] = localEntry1.next;
/*      */           }
/*  791 */           Hashtable.access$210(Hashtable.this);
/*  792 */           localEntry1.value = null;
/*  793 */           return true;
/*      */         }
/*  783 */         localEntry2 = localEntry1;
/*      */       }
/*      */ 
/*  796 */       return false;
/*      */     }
/*      */ 
/*      */     public int size() {
/*  800 */       return Hashtable.this.count;
/*      */     }
/*      */ 
/*      */     public void clear() {
/*  804 */       Hashtable.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Enumerator<T>
/*      */     implements Enumeration<T>, Iterator<T>
/*      */   {
/* 1105 */     Hashtable.Entry[] table = Hashtable.this.table;
/* 1106 */     int index = this.table.length;
/* 1107 */     Hashtable.Entry<K, V> entry = null;
/* 1108 */     Hashtable.Entry<K, V> lastReturned = null;
/*      */     int type;
/*      */     boolean iterator;
/* 1122 */     protected int expectedModCount = Hashtable.this.modCount;
/*      */ 
/*      */     Enumerator(int paramBoolean, boolean arg3) {
/* 1125 */       this.type = paramBoolean;
/*      */       boolean bool;
/* 1126 */       this.iterator = bool;
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements() {
/* 1130 */       Hashtable.Entry localEntry = this.entry;
/* 1131 */       int i = this.index;
/* 1132 */       Hashtable.Entry[] arrayOfEntry = this.table;
/*      */ 
/* 1134 */       while ((localEntry == null) && (i > 0)) {
/* 1135 */         localEntry = arrayOfEntry[(--i)];
/*      */       }
/* 1137 */       this.entry = localEntry;
/* 1138 */       this.index = i;
/* 1139 */       return localEntry != null;
/*      */     }
/*      */ 
/*      */     public T nextElement() {
/* 1143 */       Hashtable.Entry localEntry1 = this.entry;
/* 1144 */       int i = this.index;
/* 1145 */       Hashtable.Entry[] arrayOfEntry = this.table;
/*      */ 
/* 1147 */       while ((localEntry1 == null) && (i > 0)) {
/* 1148 */         localEntry1 = arrayOfEntry[(--i)];
/*      */       }
/* 1150 */       this.entry = localEntry1;
/* 1151 */       this.index = i;
/* 1152 */       if (localEntry1 != null) {
/* 1153 */         Hashtable.Entry localEntry2 = this.lastReturned = this.entry;
/* 1154 */         this.entry = localEntry2.next;
/* 1155 */         return this.type == 1 ? localEntry2.value : this.type == 0 ? localEntry2.key : localEntry2;
/*      */       }
/* 1157 */       throw new NoSuchElementException("Hashtable Enumerator");
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/* 1162 */       return hasMoreElements();
/*      */     }
/*      */ 
/*      */     public T next() {
/* 1166 */       if (Hashtable.this.modCount != this.expectedModCount)
/* 1167 */         throw new ConcurrentModificationException();
/* 1168 */       return nextElement();
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1172 */       if (!this.iterator)
/* 1173 */         throw new UnsupportedOperationException();
/* 1174 */       if (this.lastReturned == null)
/* 1175 */         throw new IllegalStateException("Hashtable Enumerator");
/* 1176 */       if (Hashtable.this.modCount != this.expectedModCount) {
/* 1177 */         throw new ConcurrentModificationException();
/*      */       }
/* 1179 */       synchronized (Hashtable.this) {
/* 1180 */         Hashtable.Entry[] arrayOfEntry = Hashtable.this.table;
/* 1181 */         int i = (this.lastReturned.hash & 0x7FFFFFFF) % arrayOfEntry.length;
/*      */ 
/* 1183 */         Hashtable.Entry localEntry1 = arrayOfEntry[i]; for (Hashtable.Entry localEntry2 = null; localEntry1 != null; 
/* 1184 */           localEntry1 = localEntry1.next) {
/* 1185 */           if (localEntry1 == this.lastReturned) {
/* 1186 */             Hashtable.access$608(Hashtable.this);
/* 1187 */             this.expectedModCount += 1;
/* 1188 */             if (localEntry2 == null)
/* 1189 */               arrayOfEntry[i] = localEntry1.next;
/*      */             else
/* 1191 */               localEntry2.next = localEntry1.next;
/* 1192 */             Hashtable.access$210(Hashtable.this);
/* 1193 */             this.lastReturned = null;
/*      */             return;
/*      */           }
/* 1184 */           localEntry2 = localEntry1;
/*      */         }
/*      */ 
/* 1197 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Holder
/*      */   {
/*  211 */     static final int ALTERNATIVE_HASHING_THRESHOLD = i;
/*      */ 
/*      */     static
/*      */     {
/*  189 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("jdk.map.althashing.threshold"));
/*      */       int i;
/*      */       try
/*      */       {
/*  195 */         i = null != str ? Integer.parseInt(str) : 2147483647;
/*      */ 
/*  200 */         if (i == -1) {
/*  201 */           i = 2147483647;
/*      */         }
/*      */ 
/*  204 */         if (i < 0)
/*  205 */           throw new IllegalArgumentException("value must be positive integer.");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*  208 */         throw new Error("Illegal value for 'jdk.map.althashing.threshold'", localIllegalArgumentException);
/*      */       }
/*      */     }
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
/*  711 */       return Hashtable.this.getIterator(0);
/*      */     }
/*      */     public int size() {
/*  714 */       return Hashtable.this.count;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/*  717 */       return Hashtable.this.containsKey(paramObject);
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/*  720 */       return Hashtable.this.remove(paramObject) != null;
/*      */     }
/*      */     public void clear() {
/*  723 */       Hashtable.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ValueCollection extends AbstractCollection<V>
/*      */   {
/*      */     private ValueCollection()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<V> iterator()
/*      */     {
/*  832 */       return Hashtable.this.getIterator(1);
/*      */     }
/*      */     public int size() {
/*  835 */       return Hashtable.this.count;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/*  838 */       return Hashtable.this.containsValue(paramObject);
/*      */     }
/*      */     public void clear() {
/*  841 */       Hashtable.this.clear();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Hashtable
 * JD-Core Version:    0.6.2
 */