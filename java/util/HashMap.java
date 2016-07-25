/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import sun.misc.Hashing;
/*      */ import sun.misc.VM;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class HashMap<K, V> extends AbstractMap<K, V>
/*      */   implements Map<K, V>, Cloneable, Serializable
/*      */ {
/*      */   static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*  149 */   static final Entry<?, ?>[] EMPTY_TABLE = new Entry[0];
/*      */ 
/*  154 */   transient Entry<K, V>[] table = (Entry[])EMPTY_TABLE;
/*      */   transient int size;
/*      */   int threshold;
/*      */   final float loadFactor;
/*      */   transient int modCount;
/*      */   static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = 2147483647;
/*  239 */   transient int hashSeed = 0;
/*      */ 
/*  980 */   private transient Set<Map.Entry<K, V>> entrySet = null;
/*      */   private static final long serialVersionUID = 362498820763181265L;
/*      */ 
/*      */   public HashMap(int paramInt, float paramFloat)
/*      */   {
/*  251 */     if (paramInt < 0) {
/*  252 */       throw new IllegalArgumentException("Illegal initial capacity: " + paramInt);
/*      */     }
/*  254 */     if (paramInt > 1073741824)
/*  255 */       paramInt = 1073741824;
/*  256 */     if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
/*  257 */       throw new IllegalArgumentException("Illegal load factor: " + paramFloat);
/*      */     }
/*      */ 
/*  260 */     this.loadFactor = paramFloat;
/*  261 */     this.threshold = paramInt;
/*  262 */     init();
/*      */   }
/*      */ 
/*      */   public HashMap(int paramInt)
/*      */   {
/*  273 */     this(paramInt, 0.75F);
/*      */   }
/*      */ 
/*      */   public HashMap()
/*      */   {
/*  281 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */   public HashMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  294 */     this(Math.max((int)(paramMap.size() / 0.75F) + 1, 16), 0.75F);
/*      */ 
/*  296 */     inflateTable(this.threshold);
/*      */ 
/*  298 */     putAllForCreate(paramMap);
/*      */   }
/*      */ 
/*      */   private static int roundUpToPowerOf2(int paramInt)
/*      */   {
/*  303 */     return paramInt > 1 ? Integer.highestOneBit(paramInt - 1 << 1) : paramInt >= 1073741824 ? 1073741824 : 1;
/*      */   }
/*      */ 
/*      */   private void inflateTable(int paramInt)
/*      */   {
/*  313 */     int i = roundUpToPowerOf2(paramInt);
/*      */ 
/*  315 */     this.threshold = ((int)Math.min(i * this.loadFactor, 1.073742E+009F));
/*  316 */     this.table = new Entry[i];
/*  317 */     initHashSeedAsNeeded(i);
/*      */   }
/*      */ 
/*      */   void init()
/*      */   {
/*      */   }
/*      */ 
/*      */   final boolean initHashSeedAsNeeded(int paramInt)
/*      */   {
/*  337 */     int i = this.hashSeed != 0 ? 1 : 0;
/*  338 */     int j = (VM.isBooted()) && (paramInt >= Holder.ALTERNATIVE_HASHING_THRESHOLD) ? 1 : 0;
/*      */ 
/*  340 */     boolean bool = i ^ j;
/*  341 */     if (bool) {
/*  342 */       this.hashSeed = (j != 0 ? Hashing.randomHashSeed(this) : 0);
/*      */     }
/*      */ 
/*  346 */     return bool;
/*      */   }
/*      */ 
/*      */   final int hash(Object paramObject)
/*      */   {
/*  357 */     int i = this.hashSeed;
/*  358 */     if ((0 != i) && ((paramObject instanceof String))) {
/*  359 */       return Hashing.stringHash32((String)paramObject);
/*      */     }
/*      */ 
/*  362 */     i ^= paramObject.hashCode();
/*      */ 
/*  367 */     i ^= i >>> 20 ^ i >>> 12;
/*  368 */     return i ^ i >>> 7 ^ i >>> 4;
/*      */   }
/*      */ 
/*      */   static int indexFor(int paramInt1, int paramInt2)
/*      */   {
/*  376 */     return paramInt1 & paramInt2 - 1;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  385 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  394 */     return this.size == 0;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/*  415 */     if (paramObject == null)
/*  416 */       return getForNullKey();
/*  417 */     Entry localEntry = getEntry(paramObject);
/*      */ 
/*  419 */     return null == localEntry ? null : localEntry.getValue();
/*      */   }
/*      */ 
/*      */   private V getForNullKey()
/*      */   {
/*  430 */     if (this.size == 0) {
/*  431 */       return null;
/*      */     }
/*  433 */     for (Entry localEntry = this.table[0]; localEntry != null; localEntry = localEntry.next) {
/*  434 */       if (localEntry.key == null)
/*  435 */         return localEntry.value;
/*      */     }
/*  437 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/*  449 */     return getEntry(paramObject) != null;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> getEntry(Object paramObject)
/*      */   {
/*  458 */     if (this.size == 0) {
/*  459 */       return null;
/*      */     }
/*      */ 
/*  462 */     int i = paramObject == null ? 0 : hash(paramObject);
/*  463 */     for (Entry localEntry = this.table[indexFor(i, this.table.length)]; 
/*  464 */       localEntry != null; 
/*  465 */       localEntry = localEntry.next)
/*      */     {
/*      */       Object localObject;
/*  467 */       if ((localEntry.hash == i) && (((localObject = localEntry.key) == paramObject) || ((paramObject != null) && (paramObject.equals(localObject)))))
/*      */       {
/*  469 */         return localEntry;
/*      */       }
/*      */     }
/*  471 */     return null;
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/*  487 */     if (this.table == EMPTY_TABLE) {
/*  488 */       inflateTable(this.threshold);
/*      */     }
/*  490 */     if (paramK == null)
/*  491 */       return putForNullKey(paramV);
/*  492 */     int i = hash(paramK);
/*  493 */     int j = indexFor(i, this.table.length);
/*  494 */     for (Entry localEntry = this.table[j]; localEntry != null; localEntry = localEntry.next)
/*      */     {
/*      */       Object localObject1;
/*  496 */       if ((localEntry.hash == i) && (((localObject1 = localEntry.key) == paramK) || (paramK.equals(localObject1)))) {
/*  497 */         Object localObject2 = localEntry.value;
/*  498 */         localEntry.value = paramV;
/*  499 */         localEntry.recordAccess(this);
/*  500 */         return localObject2;
/*      */       }
/*      */     }
/*      */ 
/*  504 */     this.modCount += 1;
/*  505 */     addEntry(i, paramK, paramV, j);
/*  506 */     return null;
/*      */   }
/*      */ 
/*      */   private V putForNullKey(V paramV)
/*      */   {
/*  513 */     for (Entry localEntry = this.table[0]; localEntry != null; localEntry = localEntry.next) {
/*  514 */       if (localEntry.key == null) {
/*  515 */         Object localObject = localEntry.value;
/*  516 */         localEntry.value = paramV;
/*  517 */         localEntry.recordAccess(this);
/*  518 */         return localObject;
/*      */       }
/*      */     }
/*  521 */     this.modCount += 1;
/*  522 */     addEntry(0, null, paramV, 0);
/*  523 */     return null;
/*      */   }
/*      */ 
/*      */   private void putForCreate(K paramK, V paramV)
/*      */   {
/*  533 */     int i = null == paramK ? 0 : hash(paramK);
/*  534 */     int j = indexFor(i, this.table.length);
/*      */ 
/*  541 */     for (Entry localEntry = this.table[j]; localEntry != null; localEntry = localEntry.next)
/*      */     {
/*      */       Object localObject;
/*  543 */       if ((localEntry.hash == i) && (((localObject = localEntry.key) == paramK) || ((paramK != null) && (paramK.equals(localObject)))))
/*      */       {
/*  545 */         localEntry.value = paramV;
/*  546 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  550 */     createEntry(i, paramK, paramV, j);
/*      */   }
/*      */ 
/*      */   private void putAllForCreate(Map<? extends K, ? extends V> paramMap) {
/*  554 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  555 */       putForCreate(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   void resize(int paramInt)
/*      */   {
/*  573 */     Entry[] arrayOfEntry1 = this.table;
/*  574 */     int i = arrayOfEntry1.length;
/*  575 */     if (i == 1073741824) {
/*  576 */       this.threshold = 2147483647;
/*  577 */       return;
/*      */     }
/*      */ 
/*  580 */     Entry[] arrayOfEntry2 = new Entry[paramInt];
/*  581 */     transfer(arrayOfEntry2, initHashSeedAsNeeded(paramInt));
/*  582 */     this.table = arrayOfEntry2;
/*  583 */     this.threshold = ((int)Math.min(paramInt * this.loadFactor, 1.073742E+009F));
/*      */   }
/*      */ 
/*      */   void transfer(Entry[] paramArrayOfEntry, boolean paramBoolean)
/*      */   {
/*  590 */     int i = paramArrayOfEntry.length;
/*  591 */     for (Object localObject : this.table)
/*  592 */       while (null != localObject) {
/*  593 */         Entry localEntry = ((Entry)localObject).next;
/*  594 */         if (paramBoolean) {
/*  595 */           ((Entry)localObject).hash = (null == ((Entry)localObject).key ? 0 : hash(((Entry)localObject).key));
/*      */         }
/*  597 */         int m = indexFor(((Entry)localObject).hash, i);
/*  598 */         ((Entry)localObject).next = paramArrayOfEntry[m];
/*  599 */         paramArrayOfEntry[m] = localObject;
/*  600 */         localObject = localEntry;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  614 */     int i = paramMap.size();
/*  615 */     if (i == 0) {
/*  616 */       return;
/*      */     }
/*  618 */     if (this.table == EMPTY_TABLE) {
/*  619 */       inflateTable((int)Math.max(i * this.loadFactor, this.threshold));
/*      */     }
/*      */ 
/*  631 */     if (i > this.threshold) {
/*  632 */       int j = (int)(i / this.loadFactor + 1.0F);
/*  633 */       if (j > 1073741824)
/*  634 */         j = 1073741824;
/*  635 */       int k = this.table.length;
/*  636 */       while (k < j)
/*  637 */         k <<= 1;
/*  638 */       if (k > this.table.length) {
/*  639 */         resize(k);
/*      */       }
/*      */     }
/*  642 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  643 */       put(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/*  656 */     Entry localEntry = removeEntryForKey(paramObject);
/*  657 */     return localEntry == null ? null : localEntry.value;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> removeEntryForKey(Object paramObject)
/*      */   {
/*  666 */     if (this.size == 0) {
/*  667 */       return null;
/*      */     }
/*  669 */     int i = paramObject == null ? 0 : hash(paramObject);
/*  670 */     int j = indexFor(i, this.table.length);
/*  671 */     Object localObject1 = this.table[j];
/*  672 */     Object localObject2 = localObject1;
/*      */ 
/*  674 */     while (localObject2 != null) {
/*  675 */       Entry localEntry = localObject2.next;
/*      */       Object localObject3;
/*  677 */       if ((localObject2.hash == i) && (((localObject3 = localObject2.key) == paramObject) || ((paramObject != null) && (paramObject.equals(localObject3)))))
/*      */       {
/*  679 */         this.modCount += 1;
/*  680 */         this.size -= 1;
/*  681 */         if (localObject1 == localObject2)
/*  682 */           this.table[j] = localEntry;
/*      */         else
/*  684 */           ((Entry)localObject1).next = localEntry;
/*  685 */         localObject2.recordRemoval(this);
/*  686 */         return localObject2;
/*      */       }
/*  688 */       localObject1 = localObject2;
/*  689 */       localObject2 = localEntry;
/*      */     }
/*      */ 
/*  692 */     return localObject2;
/*      */   }
/*      */ 
/*      */   final Entry<K, V> removeMapping(Object paramObject)
/*      */   {
/*  700 */     if ((this.size == 0) || (!(paramObject instanceof Map.Entry))) {
/*  701 */       return null;
/*      */     }
/*  703 */     Map.Entry localEntry = (Map.Entry)paramObject;
/*  704 */     Object localObject1 = localEntry.getKey();
/*  705 */     int i = localObject1 == null ? 0 : hash(localObject1);
/*  706 */     int j = indexFor(i, this.table.length);
/*  707 */     Object localObject2 = this.table[j];
/*  708 */     Object localObject3 = localObject2;
/*      */ 
/*  710 */     while (localObject3 != null) {
/*  711 */       Entry localEntry1 = localObject3.next;
/*  712 */       if ((localObject3.hash == i) && (localObject3.equals(localEntry))) {
/*  713 */         this.modCount += 1;
/*  714 */         this.size -= 1;
/*  715 */         if (localObject2 == localObject3)
/*  716 */           this.table[j] = localEntry1;
/*      */         else
/*  718 */           ((Entry)localObject2).next = localEntry1;
/*  719 */         localObject3.recordRemoval(this);
/*  720 */         return localObject3;
/*      */       }
/*  722 */       localObject2 = localObject3;
/*  723 */       localObject3 = localEntry1;
/*      */     }
/*      */ 
/*  726 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  734 */     this.modCount += 1;
/*  735 */     Arrays.fill(this.table, null);
/*  736 */     this.size = 0;
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/*  748 */     if (paramObject == null) {
/*  749 */       return containsNullValue();
/*      */     }
/*  751 */     Entry[] arrayOfEntry = this.table;
/*  752 */     for (int i = 0; i < arrayOfEntry.length; i++)
/*  753 */       for (Entry localEntry = arrayOfEntry[i]; localEntry != null; localEntry = localEntry.next)
/*  754 */         if (paramObject.equals(localEntry.value))
/*  755 */           return true;
/*  756 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean containsNullValue()
/*      */   {
/*  763 */     Entry[] arrayOfEntry = this.table;
/*  764 */     for (int i = 0; i < arrayOfEntry.length; i++)
/*  765 */       for (Entry localEntry = arrayOfEntry[i]; localEntry != null; localEntry = localEntry.next)
/*  766 */         if (localEntry.value == null)
/*  767 */           return true;
/*  768 */     return false;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  778 */     HashMap localHashMap = null;
/*      */     try {
/*  780 */       localHashMap = (HashMap)super.clone();
/*      */     }
/*      */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  784 */     if (localHashMap.table != EMPTY_TABLE) {
/*  785 */       localHashMap.inflateTable(Math.min((int)Math.min(this.size * Math.min(1.0F / this.loadFactor, 4.0F), 1.073742E+009F), this.table.length));
/*      */     }
/*      */ 
/*  792 */     localHashMap.entrySet = null;
/*  793 */     localHashMap.modCount = 0;
/*  794 */     localHashMap.size = 0;
/*  795 */     localHashMap.init();
/*  796 */     localHashMap.putAllForCreate(this);
/*      */ 
/*  798 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   void addEntry(int paramInt1, K paramK, V paramV, int paramInt2)
/*      */   {
/*  878 */     if ((this.size >= this.threshold) && (null != this.table[paramInt2])) {
/*  879 */       resize(2 * this.table.length);
/*  880 */       paramInt1 = null != paramK ? hash(paramK) : 0;
/*  881 */       paramInt2 = indexFor(paramInt1, this.table.length);
/*      */     }
/*      */ 
/*  884 */     createEntry(paramInt1, paramK, paramV, paramInt2);
/*      */   }
/*      */ 
/*      */   void createEntry(int paramInt1, K paramK, V paramV, int paramInt2)
/*      */   {
/*  896 */     Entry localEntry = this.table[paramInt2];
/*  897 */     this.table[paramInt2] = new Entry(paramInt1, paramK, paramV, localEntry);
/*  898 */     this.size += 1;
/*      */   }
/*      */ 
/*      */   Iterator<K> newKeyIterator()
/*      */   {
/*  968 */     return new KeyIterator(null);
/*      */   }
/*      */   Iterator<V> newValueIterator() {
/*  971 */     return new ValueIterator(null);
/*      */   }
/*      */   Iterator<Map.Entry<K, V>> newEntryIterator() {
/*  974 */     return new EntryIterator(null);
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/*  996 */     Set localSet = this.keySet;
/*  997 */     return this.keySet = new KeySet(null);
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/* 1032 */     Collection localCollection = this.values;
/* 1033 */     return this.values = new Values(null);
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1068 */     return entrySet0();
/*      */   }
/*      */ 
/*      */   private Set<Map.Entry<K, V>> entrySet0() {
/* 1072 */     Set localSet = this.entrySet;
/* 1073 */     return this.entrySet = new EntrySet(null);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1113 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1116 */     if (this.table == EMPTY_TABLE)
/* 1117 */       paramObjectOutputStream.writeInt(roundUpToPowerOf2(this.threshold));
/*      */     else {
/* 1119 */       paramObjectOutputStream.writeInt(this.table.length);
/*      */     }
/*      */ 
/* 1123 */     paramObjectOutputStream.writeInt(this.size);
/*      */ 
/* 1126 */     if (this.size > 0)
/* 1127 */       for (Map.Entry localEntry : entrySet0()) {
/* 1128 */         paramObjectOutputStream.writeObject(localEntry.getKey());
/* 1129 */         paramObjectOutputStream.writeObject(localEntry.getValue());
/*      */       }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1144 */     paramObjectInputStream.defaultReadObject();
/* 1145 */     if ((this.loadFactor <= 0.0F) || (Float.isNaN(this.loadFactor))) {
/* 1146 */       throw new InvalidObjectException("Illegal load factor: " + this.loadFactor);
/*      */     }
/*      */ 
/* 1151 */     this.table = ((Entry[])EMPTY_TABLE);
/*      */ 
/* 1154 */     paramObjectInputStream.readInt();
/*      */ 
/* 1157 */     int i = paramObjectInputStream.readInt();
/* 1158 */     if (i < 0) {
/* 1159 */       throw new InvalidObjectException("Illegal mappings count: " + i);
/*      */     }
/*      */ 
/* 1163 */     int j = (int)Math.min(i * Math.min(1.0F / this.loadFactor, 4.0F), 1.073742E+009F);
/*      */ 
/* 1169 */     if (i > 0)
/* 1170 */       inflateTable(j);
/*      */     else {
/* 1172 */       this.threshold = j;
/*      */     }
/*      */ 
/* 1175 */     init();
/*      */ 
/* 1178 */     for (int k = 0; k < i; k++) {
/* 1179 */       Object localObject1 = paramObjectInputStream.readObject();
/* 1180 */       Object localObject2 = paramObjectInputStream.readObject();
/* 1181 */       putForCreate(localObject1, localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   int capacity() {
/* 1186 */     return this.table.length; } 
/* 1187 */   float loadFactor() { return this.loadFactor; }
/*      */ 
/*      */ 
/*      */   static class Entry<K, V>
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     final K key;
/*      */     V value;
/*      */     Entry<K, V> next;
/*      */     int hash;
/*      */ 
/*      */     Entry(int paramInt, K paramK, V paramV, Entry<K, V> paramEntry)
/*      */     {
/*  811 */       this.value = paramV;
/*  812 */       this.next = paramEntry;
/*  813 */       this.key = paramK;
/*  814 */       this.hash = paramInt;
/*      */     }
/*      */ 
/*      */     public final K getKey() {
/*  818 */       return this.key;
/*      */     }
/*      */ 
/*      */     public final V getValue() {
/*  822 */       return this.value;
/*      */     }
/*      */ 
/*      */     public final V setValue(V paramV) {
/*  826 */       Object localObject = this.value;
/*  827 */       this.value = paramV;
/*  828 */       return localObject;
/*      */     }
/*      */ 
/*      */     public final boolean equals(Object paramObject) {
/*  832 */       if (!(paramObject instanceof Map.Entry))
/*  833 */         return false;
/*  834 */       Map.Entry localEntry = (Map.Entry)paramObject;
/*  835 */       Object localObject1 = getKey();
/*  836 */       Object localObject2 = localEntry.getKey();
/*  837 */       if ((localObject1 == localObject2) || ((localObject1 != null) && (localObject1.equals(localObject2)))) {
/*  838 */         Object localObject3 = getValue();
/*  839 */         Object localObject4 = localEntry.getValue();
/*  840 */         if ((localObject3 == localObject4) || ((localObject3 != null) && (localObject3.equals(localObject4))))
/*  841 */           return true;
/*      */       }
/*  843 */       return false;
/*      */     }
/*      */ 
/*      */     public final int hashCode() {
/*  847 */       return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
/*      */     }
/*      */ 
/*      */     public final String toString() {
/*  851 */       return getKey() + "=" + getValue();
/*      */     }
/*      */ 
/*      */     void recordAccess(HashMap<K, V> paramHashMap)
/*      */     {
/*      */     }
/*      */ 
/*      */     void recordRemoval(HashMap<K, V> paramHashMap)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class EntryIterator extends HashMap<K, V>.HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     private EntryIterator()
/*      */     {
/*  960 */       super();
/*      */     }
/*  962 */     public Map.Entry<K, V> next() { return nextEntry(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private final class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<Map.Entry<K, V>> iterator()
/*      */     {
/* 1078 */       return HashMap.this.newEntryIterator();
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1081 */       if (!(paramObject instanceof Map.Entry))
/* 1082 */         return false;
/* 1083 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1084 */       HashMap.Entry localEntry1 = HashMap.this.getEntry(localEntry.getKey());
/* 1085 */       return (localEntry1 != null) && (localEntry1.equals(localEntry));
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1088 */       return HashMap.this.removeMapping(paramObject) != null;
/*      */     }
/*      */     public int size() {
/* 1091 */       return HashMap.this.size;
/*      */     }
/*      */     public void clear() {
/* 1094 */       HashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class HashIterator<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     HashMap.Entry<K, V> next;
/*      */     int expectedModCount;
/*      */     int index;
/*      */     HashMap.Entry<K, V> current;
/*      */ 
/*      */     HashIterator()
/*      */     {
/*  908 */       this.expectedModCount = HashMap.this.modCount;
/*  909 */       if (HashMap.this.size > 0) {
/*  910 */         HashMap.Entry[] arrayOfEntry = HashMap.this.table;
/*  911 */         while ((this.index < arrayOfEntry.length) && ((this.next = arrayOfEntry[(this.index++)]) == null));
/*      */       }
/*      */     }
/*      */ 
/*      */     public final boolean hasNext() {
/*  917 */       return this.next != null;
/*      */     }
/*      */ 
/*      */     final HashMap.Entry<K, V> nextEntry() {
/*  921 */       if (HashMap.this.modCount != this.expectedModCount)
/*  922 */         throw new ConcurrentModificationException();
/*  923 */       HashMap.Entry localEntry = this.next;
/*  924 */       if (localEntry == null) {
/*  925 */         throw new NoSuchElementException();
/*      */       }
/*  927 */       if ((this.next = localEntry.next) == null) {
/*  928 */         HashMap.Entry[] arrayOfEntry = HashMap.this.table;
/*  929 */         while ((this.index < arrayOfEntry.length) && ((this.next = arrayOfEntry[(this.index++)]) == null));
/*      */       }
/*  932 */       this.current = localEntry;
/*  933 */       return localEntry;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  937 */       if (this.current == null)
/*  938 */         throw new IllegalStateException();
/*  939 */       if (HashMap.this.modCount != this.expectedModCount)
/*  940 */         throw new ConcurrentModificationException();
/*  941 */       Object localObject = this.current.key;
/*  942 */       this.current = null;
/*  943 */       HashMap.this.removeEntryForKey(localObject);
/*  944 */       this.expectedModCount = HashMap.this.modCount;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Holder
/*      */   {
/*  230 */     static final int ALTERNATIVE_HASHING_THRESHOLD = i;
/*      */ 
/*      */     static
/*      */     {
/*  208 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("jdk.map.althashing.threshold"));
/*      */       int i;
/*      */       try
/*      */       {
/*  214 */         i = null != str ? Integer.parseInt(str) : 2147483647;
/*      */ 
/*  219 */         if (i == -1) {
/*  220 */           i = 2147483647;
/*      */         }
/*      */ 
/*  223 */         if (i < 0)
/*  224 */           throw new IllegalArgumentException("value must be positive integer.");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*  227 */         throw new Error("Illegal value for 'jdk.map.althashing.threshold'", localIllegalArgumentException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class KeyIterator extends HashMap<K, V>.HashIterator<K>
/*      */   {
/*      */     private KeyIterator()
/*      */     {
/*  954 */       super();
/*      */     }
/*  956 */     public K next() { return nextEntry().getKey(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   private final class KeySet extends AbstractSet<K>
/*      */   {
/*      */     private KeySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<K> iterator()
/*      */     {
/* 1002 */       return HashMap.this.newKeyIterator();
/*      */     }
/*      */     public int size() {
/* 1005 */       return HashMap.this.size;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1008 */       return HashMap.this.containsKey(paramObject);
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1011 */       return HashMap.this.removeEntryForKey(paramObject) != null;
/*      */     }
/*      */     public void clear() {
/* 1014 */       HashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class ValueIterator extends HashMap<K, V>.HashIterator<V>
/*      */   {
/*      */     private ValueIterator()
/*      */     {
/*  948 */       super();
/*      */     }
/*  950 */     public V next() { return nextEntry().value; }
/*      */ 
/*      */   }
/*      */ 
/*      */   private final class Values extends AbstractCollection<V>
/*      */   {
/*      */     private Values()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<V> iterator()
/*      */     {
/* 1038 */       return HashMap.this.newValueIterator();
/*      */     }
/*      */     public int size() {
/* 1041 */       return HashMap.this.size;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1044 */       return HashMap.this.containsValue(paramObject);
/*      */     }
/*      */     public void clear() {
/* 1047 */       HashMap.this.clear();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.HashMap
 * JD-Core Version:    0.6.2
 */