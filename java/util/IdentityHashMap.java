/*      */ package java.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.lang.reflect.Array;
/*      */ 
/*      */ public class IdentityHashMap<K, V> extends AbstractMap<K, V>
/*      */   implements Map<K, V>, Serializable, Cloneable
/*      */ {
/*      */   private static final int DEFAULT_CAPACITY = 32;
/*      */   private static final int MINIMUM_CAPACITY = 4;
/*      */   private static final int MAXIMUM_CAPACITY = 536870912;
/*      */   private transient Object[] table;
/*      */   private int size;
/*      */   private transient int modCount;
/*      */   private transient int threshold;
/*  186 */   private static final Object NULL_KEY = new Object();
/*      */ 
/*  918 */   private transient Set<Map.Entry<K, V>> entrySet = null;
/*      */   private static final long serialVersionUID = 8188218128353913216L;
/*      */ 
/*      */   private static Object maskNull(Object paramObject)
/*      */   {
/*  192 */     return paramObject == null ? NULL_KEY : paramObject;
/*      */   }
/*      */ 
/*      */   private static Object unmaskNull(Object paramObject)
/*      */   {
/*  199 */     return paramObject == NULL_KEY ? null : paramObject;
/*      */   }
/*      */ 
/*      */   public IdentityHashMap()
/*      */   {
/*  207 */     init(32);
/*      */   }
/*      */ 
/*      */   public IdentityHashMap(int paramInt)
/*      */   {
/*  220 */     if (paramInt < 0) {
/*  221 */       throw new IllegalArgumentException("expectedMaxSize is negative: " + paramInt);
/*      */     }
/*  223 */     init(capacity(paramInt));
/*      */   }
/*      */ 
/*      */   private int capacity(int paramInt)
/*      */   {
/*  236 */     int i = 3 * paramInt / 2;
/*      */     int j;
/*  240 */     if ((i > 536870912) || (i < 0)) {
/*  241 */       j = 536870912;
/*      */     } else {
/*  243 */       j = 4;
/*  244 */       while (j < i)
/*  245 */         j <<= 1;
/*      */     }
/*  247 */     return j;
/*      */   }
/*      */ 
/*      */   private void init(int paramInt)
/*      */   {
/*  260 */     this.threshold = (paramInt * 2 / 3);
/*  261 */     this.table = new Object[2 * paramInt];
/*      */   }
/*      */ 
/*      */   public IdentityHashMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  273 */     this((int)((1 + paramMap.size()) * 1.1D));
/*  274 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  283 */     return this.size;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  294 */     return this.size == 0;
/*      */   }
/*      */ 
/*      */   private static int hash(Object paramObject, int paramInt)
/*      */   {
/*  301 */     int i = System.identityHashCode(paramObject);
/*      */ 
/*  303 */     return (i << 1) - (i << 8) & paramInt - 1;
/*      */   }
/*      */ 
/*      */   private static int nextKeyIndex(int paramInt1, int paramInt2)
/*      */   {
/*  310 */     return paramInt1 + 2 < paramInt2 ? paramInt1 + 2 : 0;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/*  331 */     Object localObject1 = maskNull(paramObject);
/*  332 */     Object[] arrayOfObject = this.table;
/*  333 */     int i = arrayOfObject.length;
/*  334 */     int j = hash(localObject1, i);
/*      */     while (true) {
/*  336 */       Object localObject2 = arrayOfObject[j];
/*  337 */       if (localObject2 == localObject1)
/*  338 */         return arrayOfObject[(j + 1)];
/*  339 */       if (localObject2 == null)
/*  340 */         return null;
/*  341 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/*  355 */     Object localObject1 = maskNull(paramObject);
/*  356 */     Object[] arrayOfObject = this.table;
/*  357 */     int i = arrayOfObject.length;
/*  358 */     int j = hash(localObject1, i);
/*      */     while (true) {
/*  360 */       Object localObject2 = arrayOfObject[j];
/*  361 */       if (localObject2 == localObject1)
/*  362 */         return true;
/*  363 */       if (localObject2 == null)
/*  364 */         return false;
/*  365 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/*  379 */     Object[] arrayOfObject = this.table;
/*  380 */     for (int i = 1; i < arrayOfObject.length; i += 2) {
/*  381 */       if ((arrayOfObject[i] == paramObject) && (arrayOfObject[(i - 1)] != null))
/*  382 */         return true;
/*      */     }
/*  384 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean containsMapping(Object paramObject1, Object paramObject2)
/*      */   {
/*  396 */     Object localObject1 = maskNull(paramObject1);
/*  397 */     Object[] arrayOfObject = this.table;
/*  398 */     int i = arrayOfObject.length;
/*  399 */     int j = hash(localObject1, i);
/*      */     while (true) {
/*  401 */       Object localObject2 = arrayOfObject[j];
/*  402 */       if (localObject2 == localObject1)
/*  403 */         return arrayOfObject[(j + 1)] == paramObject2;
/*  404 */       if (localObject2 == null)
/*  405 */         return false;
/*  406 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/*  426 */     Object localObject1 = maskNull(paramK);
/*  427 */     Object[] arrayOfObject = this.table;
/*  428 */     int i = arrayOfObject.length;
/*  429 */     int j = hash(localObject1, i);
/*      */     Object localObject2;
/*  432 */     while ((localObject2 = arrayOfObject[j]) != null) {
/*  433 */       if (localObject2 == localObject1) {
/*  434 */         Object localObject3 = arrayOfObject[(j + 1)];
/*  435 */         arrayOfObject[(j + 1)] = paramV;
/*  436 */         return localObject3;
/*      */       }
/*  438 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */ 
/*  441 */     this.modCount += 1;
/*  442 */     arrayOfObject[j] = localObject1;
/*  443 */     arrayOfObject[(j + 1)] = paramV;
/*  444 */     if (++this.size >= this.threshold)
/*  445 */       resize(i);
/*  446 */     return null;
/*      */   }
/*      */ 
/*      */   private void resize(int paramInt)
/*      */   {
/*  456 */     int i = paramInt * 2;
/*      */ 
/*  458 */     Object[] arrayOfObject1 = this.table;
/*  459 */     int j = arrayOfObject1.length;
/*  460 */     if (j == 1073741824) {
/*  461 */       if (this.threshold == 536870911)
/*  462 */         throw new IllegalStateException("Capacity exhausted.");
/*  463 */       this.threshold = 536870911;
/*  464 */       return;
/*      */     }
/*  466 */     if (j >= i) {
/*  467 */       return;
/*      */     }
/*  469 */     Object[] arrayOfObject2 = new Object[i];
/*  470 */     this.threshold = (i / 3);
/*      */ 
/*  472 */     for (int k = 0; k < j; k += 2) {
/*  473 */       Object localObject1 = arrayOfObject1[k];
/*  474 */       if (localObject1 != null) {
/*  475 */         Object localObject2 = arrayOfObject1[(k + 1)];
/*  476 */         arrayOfObject1[k] = null;
/*  477 */         arrayOfObject1[(k + 1)] = null;
/*  478 */         int m = hash(localObject1, i);
/*  479 */         while (arrayOfObject2[m] != null)
/*  480 */           m = nextKeyIndex(m, i);
/*  481 */         arrayOfObject2[m] = localObject1;
/*  482 */         arrayOfObject2[(m + 1)] = localObject2;
/*      */       }
/*      */     }
/*  485 */     this.table = arrayOfObject2;
/*      */   }
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  497 */     int i = paramMap.size();
/*  498 */     if (i == 0)
/*  499 */       return;
/*  500 */     if (i > this.threshold) {
/*  501 */       resize(capacity(i));
/*      */     }
/*  503 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  504 */       put(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/*  517 */     Object localObject1 = maskNull(paramObject);
/*  518 */     Object[] arrayOfObject = this.table;
/*  519 */     int i = arrayOfObject.length;
/*  520 */     int j = hash(localObject1, i);
/*      */     while (true)
/*      */     {
/*  523 */       Object localObject2 = arrayOfObject[j];
/*  524 */       if (localObject2 == localObject1) {
/*  525 */         this.modCount += 1;
/*  526 */         this.size -= 1;
/*  527 */         Object localObject3 = arrayOfObject[(j + 1)];
/*  528 */         arrayOfObject[(j + 1)] = null;
/*  529 */         arrayOfObject[j] = null;
/*  530 */         closeDeletion(j);
/*  531 */         return localObject3;
/*      */       }
/*  533 */       if (localObject2 == null)
/*  534 */         return null;
/*  535 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean removeMapping(Object paramObject1, Object paramObject2)
/*      */   {
/*  549 */     Object localObject1 = maskNull(paramObject1);
/*  550 */     Object[] arrayOfObject = this.table;
/*  551 */     int i = arrayOfObject.length;
/*  552 */     int j = hash(localObject1, i);
/*      */     while (true)
/*      */     {
/*  555 */       Object localObject2 = arrayOfObject[j];
/*  556 */       if (localObject2 == localObject1) {
/*  557 */         if (arrayOfObject[(j + 1)] != paramObject2)
/*  558 */           return false;
/*  559 */         this.modCount += 1;
/*  560 */         this.size -= 1;
/*  561 */         arrayOfObject[j] = null;
/*  562 */         arrayOfObject[(j + 1)] = null;
/*  563 */         closeDeletion(j);
/*  564 */         return true;
/*      */       }
/*  566 */       if (localObject2 == null)
/*  567 */         return false;
/*  568 */       j = nextKeyIndex(j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void closeDeletion(int paramInt)
/*      */   {
/*  581 */     Object[] arrayOfObject = this.table;
/*  582 */     int i = arrayOfObject.length;
/*      */     Object localObject;
/*  589 */     for (int j = nextKeyIndex(paramInt, i); (localObject = arrayOfObject[j]) != null; 
/*  590 */       j = nextKeyIndex(j, i))
/*      */     {
/*  597 */       int k = hash(localObject, i);
/*  598 */       if (((j < k) && ((k <= paramInt) || (paramInt <= j))) || ((k <= paramInt) && (paramInt <= j))) {
/*  599 */         arrayOfObject[paramInt] = localObject;
/*  600 */         arrayOfObject[(paramInt + 1)] = arrayOfObject[(j + 1)];
/*  601 */         arrayOfObject[j] = null;
/*  602 */         arrayOfObject[(j + 1)] = null;
/*  603 */         paramInt = j;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  613 */     this.modCount += 1;
/*  614 */     Object[] arrayOfObject = this.table;
/*  615 */     for (int i = 0; i < arrayOfObject.length; i++)
/*  616 */       arrayOfObject[i] = null;
/*  617 */     this.size = 0;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  638 */     if (paramObject == this)
/*  639 */       return true;
/*      */     Object localObject1;
/*  640 */     if ((paramObject instanceof IdentityHashMap)) {
/*  641 */       localObject1 = (IdentityHashMap)paramObject;
/*  642 */       if (((IdentityHashMap)localObject1).size() != this.size) {
/*  643 */         return false;
/*      */       }
/*  645 */       Object[] arrayOfObject = ((IdentityHashMap)localObject1).table;
/*  646 */       for (int i = 0; i < arrayOfObject.length; i += 2) {
/*  647 */         Object localObject2 = arrayOfObject[i];
/*  648 */         if ((localObject2 != null) && (!containsMapping(localObject2, arrayOfObject[(i + 1)])))
/*  649 */           return false;
/*      */       }
/*  651 */       return true;
/*  652 */     }if ((paramObject instanceof Map)) {
/*  653 */       localObject1 = (Map)paramObject;
/*  654 */       return entrySet().equals(((Map)localObject1).entrySet());
/*      */     }
/*  656 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  680 */     int i = 0;
/*  681 */     Object[] arrayOfObject = this.table;
/*  682 */     for (int j = 0; j < arrayOfObject.length; j += 2) {
/*  683 */       Object localObject1 = arrayOfObject[j];
/*  684 */       if (localObject1 != null) {
/*  685 */         Object localObject2 = unmaskNull(localObject1);
/*  686 */         i += (System.identityHashCode(localObject2) ^ System.identityHashCode(arrayOfObject[(j + 1)]));
/*      */       }
/*      */     }
/*      */ 
/*  690 */     return i;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  701 */       IdentityHashMap localIdentityHashMap = (IdentityHashMap)super.clone();
/*  702 */       localIdentityHashMap.entrySet = null;
/*  703 */       localIdentityHashMap.table = ((Object[])this.table.clone());
/*  704 */       return localIdentityHashMap; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  706 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/*  959 */     Set localSet = this.keySet;
/*  960 */     if (localSet != null) {
/*  961 */       return localSet;
/*      */     }
/*  963 */     return this.keySet = new KeySet(null);
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/* 1028 */     Collection localCollection = this.values;
/* 1029 */     if (localCollection != null) {
/* 1030 */       return localCollection;
/*      */     }
/* 1032 */     return this.values = new Values(null);
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1098 */     Set localSet = this.entrySet;
/* 1099 */     if (localSet != null) {
/* 1100 */       return localSet;
/*      */     }
/* 1102 */     return this.entrySet = new EntrySet(null);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1183 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1186 */     paramObjectOutputStream.writeInt(this.size);
/*      */ 
/* 1189 */     Object[] arrayOfObject = this.table;
/* 1190 */     for (int i = 0; i < arrayOfObject.length; i += 2) {
/* 1191 */       Object localObject = arrayOfObject[i];
/* 1192 */       if (localObject != null) {
/* 1193 */         paramObjectOutputStream.writeObject(unmaskNull(localObject));
/* 1194 */         paramObjectOutputStream.writeObject(arrayOfObject[(i + 1)]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1206 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1209 */     int i = paramObjectInputStream.readInt();
/*      */ 
/* 1212 */     init(capacity(i * 4 / 3));
/*      */ 
/* 1215 */     for (int j = 0; j < i; j++) {
/* 1216 */       Object localObject1 = paramObjectInputStream.readObject();
/* 1217 */       Object localObject2 = paramObjectInputStream.readObject();
/* 1218 */       putForCreate(localObject1, localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void putForCreate(K paramK, V paramV)
/*      */     throws IOException
/*      */   {
/* 1229 */     Object localObject1 = maskNull(paramK);
/* 1230 */     Object[] arrayOfObject = this.table;
/* 1231 */     int i = arrayOfObject.length;
/* 1232 */     int j = hash(localObject1, i);
/*      */     Object localObject2;
/* 1235 */     while ((localObject2 = arrayOfObject[j]) != null) {
/* 1236 */       if (localObject2 == localObject1)
/* 1237 */         throw new StreamCorruptedException();
/* 1238 */       j = nextKeyIndex(j, i);
/*      */     }
/* 1240 */     arrayOfObject[j] = localObject1;
/* 1241 */     arrayOfObject[(j + 1)] = paramV;
/*      */   }
/*      */ 
/*      */   private class EntryIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<Map.Entry<K, V>>
/*      */   {
/*      */     private IdentityHashMap<K, V>.EntryIterator.Entry lastReturnedEntry;
/*      */ 
/*      */     private EntryIterator()
/*      */     {
/*  832 */       super(null);
/*      */ 
/*  835 */       this.lastReturnedEntry = null;
/*      */     }
/*      */     public Map.Entry<K, V> next() {
/*  838 */       this.lastReturnedEntry = new Entry(nextIndex(), null);
/*  839 */       return this.lastReturnedEntry;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  843 */       this.lastReturnedIndex = (null == this.lastReturnedEntry ? -1 : this.lastReturnedEntry.index);
/*      */ 
/*  845 */       super.remove();
/*  846 */       this.lastReturnedEntry.index = this.lastReturnedIndex;
/*  847 */       this.lastReturnedEntry = null;
/*      */     }
/*      */ 
/*      */     private class Entry
/*      */       implements Map.Entry<K, V>
/*      */     {
/*      */       private int index;
/*      */ 
/*      */       private Entry(int arg2)
/*      */       {
/*      */         int i;
/*  854 */         this.index = i;
/*      */       }
/*      */ 
/*      */       public K getKey() {
/*  858 */         checkIndexForEntryUse();
/*  859 */         return IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index]);
/*      */       }
/*      */ 
/*      */       public V getValue() {
/*  863 */         checkIndexForEntryUse();
/*  864 */         return IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
/*      */       }
/*      */ 
/*      */       public V setValue(V paramV) {
/*  868 */         checkIndexForEntryUse();
/*  869 */         Object localObject = IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
/*  870 */         IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)] = paramV;
/*      */ 
/*  872 */         if (IdentityHashMap.EntryIterator.this.traversalTable != IdentityHashMap.this.table)
/*  873 */           IdentityHashMap.this.put(IdentityHashMap.EntryIterator.this.traversalTable[this.index], paramV);
/*  874 */         return localObject;
/*      */       }
/*      */ 
/*      */       public boolean equals(Object paramObject) {
/*  878 */         if (this.index < 0) {
/*  879 */           return super.equals(paramObject);
/*      */         }
/*  881 */         if (!(paramObject instanceof Map.Entry))
/*  882 */           return false;
/*  883 */         Map.Entry localEntry = (Map.Entry)paramObject;
/*  884 */         return (localEntry.getKey() == IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index])) && (localEntry.getValue() == IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)]);
/*      */       }
/*      */ 
/*      */       public int hashCode()
/*      */       {
/*  889 */         if (IdentityHashMap.EntryIterator.this.lastReturnedIndex < 0) {
/*  890 */           return super.hashCode();
/*      */         }
/*  892 */         return System.identityHashCode(IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index])) ^ System.identityHashCode(IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)]);
/*      */       }
/*      */ 
/*      */       public String toString()
/*      */       {
/*  897 */         if (this.index < 0) {
/*  898 */           return super.toString();
/*      */         }
/*  900 */         return IdentityHashMap.unmaskNull(IdentityHashMap.EntryIterator.this.traversalTable[this.index]) + "=" + IdentityHashMap.EntryIterator.this.traversalTable[(this.index + 1)];
/*      */       }
/*      */ 
/*      */       private void checkIndexForEntryUse()
/*      */       {
/*  905 */         if (this.index < 0)
/*  906 */           throw new IllegalStateException("Entry was removed");
/*      */       }
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
/* 1107 */       return new IdentityHashMap.EntryIterator(IdentityHashMap.this, null);
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1110 */       if (!(paramObject instanceof Map.Entry))
/* 1111 */         return false;
/* 1112 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1113 */       return IdentityHashMap.this.containsMapping(localEntry.getKey(), localEntry.getValue());
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1116 */       if (!(paramObject instanceof Map.Entry))
/* 1117 */         return false;
/* 1118 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1119 */       return IdentityHashMap.this.removeMapping(localEntry.getKey(), localEntry.getValue());
/*      */     }
/*      */     public int size() {
/* 1122 */       return IdentityHashMap.this.size;
/*      */     }
/*      */     public void clear() {
/* 1125 */       IdentityHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     public boolean removeAll(Collection<?> paramCollection)
/*      */     {
/* 1133 */       boolean bool = false;
/* 1134 */       for (Iterator localIterator = iterator(); localIterator.hasNext(); ) {
/* 1135 */         if (paramCollection.contains(localIterator.next())) {
/* 1136 */           localIterator.remove();
/* 1137 */           bool = true;
/*      */         }
/*      */       }
/* 1140 */       return bool;
/*      */     }
/*      */ 
/*      */     public Object[] toArray() {
/* 1144 */       int i = size();
/* 1145 */       Object[] arrayOfObject = new Object[i];
/* 1146 */       Iterator localIterator = iterator();
/* 1147 */       for (int j = 0; j < i; j++)
/* 1148 */         arrayOfObject[j] = new AbstractMap.SimpleEntry((Map.Entry)localIterator.next());
/* 1149 */       return arrayOfObject;
/*      */     }
/*      */ 
/*      */     public <T> T[] toArray(T[] paramArrayOfT)
/*      */     {
/* 1154 */       int i = size();
/* 1155 */       if (paramArrayOfT.length < i) {
/* 1156 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*      */       }
/* 1158 */       Iterator localIterator = iterator();
/* 1159 */       for (int j = 0; j < i; j++)
/* 1160 */         paramArrayOfT[j] = new AbstractMap.SimpleEntry((Map.Entry)localIterator.next());
/* 1161 */       if (paramArrayOfT.length > i)
/* 1162 */         paramArrayOfT[i] = null;
/* 1163 */       return paramArrayOfT;
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class IdentityHashMapIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*  711 */     int index = IdentityHashMap.this.size != 0 ? 0 : IdentityHashMap.this.table.length;
/*  712 */     int expectedModCount = IdentityHashMap.this.modCount;
/*  713 */     int lastReturnedIndex = -1;
/*      */     boolean indexValid;
/*  715 */     Object[] traversalTable = IdentityHashMap.this.table;
/*      */ 
/*      */     private IdentityHashMapIterator() {  } 
/*  718 */     public boolean hasNext() { Object[] arrayOfObject = this.traversalTable;
/*  719 */       for (int i = this.index; i < arrayOfObject.length; i += 2) {
/*  720 */         Object localObject = arrayOfObject[i];
/*  721 */         if (localObject != null) {
/*  722 */           this.index = i;
/*  723 */           return this.indexValid = 1;
/*      */         }
/*      */       }
/*  726 */       this.index = arrayOfObject.length;
/*  727 */       return false; }
/*      */ 
/*      */     protected int nextIndex()
/*      */     {
/*  731 */       if (IdentityHashMap.this.modCount != this.expectedModCount)
/*  732 */         throw new ConcurrentModificationException();
/*  733 */       if ((!this.indexValid) && (!hasNext())) {
/*  734 */         throw new NoSuchElementException();
/*      */       }
/*  736 */       this.indexValid = false;
/*  737 */       this.lastReturnedIndex = this.index;
/*  738 */       this.index += 2;
/*  739 */       return this.lastReturnedIndex;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  743 */       if (this.lastReturnedIndex == -1)
/*  744 */         throw new IllegalStateException();
/*  745 */       if (IdentityHashMap.this.modCount != this.expectedModCount) {
/*  746 */         throw new ConcurrentModificationException();
/*      */       }
/*  748 */       this.expectedModCount = IdentityHashMap.access$204(IdentityHashMap.this);
/*  749 */       int i = this.lastReturnedIndex;
/*  750 */       this.lastReturnedIndex = -1;
/*      */ 
/*  752 */       this.index = i;
/*  753 */       this.indexValid = false;
/*      */ 
/*  767 */       Object[] arrayOfObject1 = this.traversalTable;
/*  768 */       int j = arrayOfObject1.length;
/*      */ 
/*  770 */       int k = i;
/*  771 */       Object localObject1 = arrayOfObject1[k];
/*  772 */       arrayOfObject1[k] = null;
/*  773 */       arrayOfObject1[(k + 1)] = null;
/*      */ 
/*  777 */       if (arrayOfObject1 != IdentityHashMap.this.table) {
/*  778 */         IdentityHashMap.this.remove(localObject1);
/*  779 */         this.expectedModCount = IdentityHashMap.this.modCount;
/*  780 */         return;
/*      */       }
/*      */ 
/*  783 */       IdentityHashMap.access$010(IdentityHashMap.this);
/*      */       Object localObject2;
/*  786 */       for (int m = IdentityHashMap.nextKeyIndex(k, j); (localObject2 = arrayOfObject1[m]) != null; 
/*  787 */         m = IdentityHashMap.nextKeyIndex(m, j)) {
/*  788 */         int n = IdentityHashMap.hash(localObject2, j);
/*      */ 
/*  790 */         if (((m < n) && ((n <= k) || (k <= m))) || ((n <= k) && (k <= m)))
/*      */         {
/*  800 */           if ((m < i) && (k >= i) && (this.traversalTable == IdentityHashMap.this.table))
/*      */           {
/*  802 */             int i1 = j - i;
/*  803 */             Object[] arrayOfObject2 = new Object[i1];
/*  804 */             System.arraycopy(arrayOfObject1, i, arrayOfObject2, 0, i1);
/*      */ 
/*  806 */             this.traversalTable = arrayOfObject2;
/*  807 */             this.index = 0;
/*      */           }
/*      */ 
/*  810 */           arrayOfObject1[k] = localObject2;
/*  811 */           arrayOfObject1[(k + 1)] = arrayOfObject1[(m + 1)];
/*  812 */           arrayOfObject1[m] = null;
/*  813 */           arrayOfObject1[(m + 1)] = null;
/*  814 */           k = m;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  820 */   private class KeyIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<K> { private KeyIterator() { super(null); } 
/*      */     public K next() {
/*  822 */       return IdentityHashMap.unmaskNull(this.traversalTable[nextIndex()]);
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
/*  968 */       return new IdentityHashMap.KeyIterator(IdentityHashMap.this, null);
/*      */     }
/*      */     public int size() {
/*  971 */       return IdentityHashMap.this.size;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/*  974 */       return IdentityHashMap.this.containsKey(paramObject);
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/*  977 */       int i = IdentityHashMap.this.size;
/*  978 */       IdentityHashMap.this.remove(paramObject);
/*  979 */       return IdentityHashMap.this.size != i;
/*      */     }
/*      */ 
/*      */     public boolean removeAll(Collection<?> paramCollection)
/*      */     {
/*  987 */       boolean bool = false;
/*  988 */       for (Iterator localIterator = iterator(); localIterator.hasNext(); ) {
/*  989 */         if (paramCollection.contains(localIterator.next())) {
/*  990 */           localIterator.remove();
/*  991 */           bool = true;
/*      */         }
/*      */       }
/*  994 */       return bool;
/*      */     }
/*      */     public void clear() {
/*  997 */       IdentityHashMap.this.clear();
/*      */     }
/*      */     public int hashCode() {
/* 1000 */       int i = 0;
/* 1001 */       for (Iterator localIterator = iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1002 */         i += System.identityHashCode(localObject); }
/* 1003 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ValueIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<V>
/*      */   {
/*      */     private ValueIterator()
/*      */     {
/*  826 */       super(null);
/*      */     }
/*  828 */     public V next() { return this.traversalTable[(nextIndex() + 1)]; }
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
/* 1037 */       return new IdentityHashMap.ValueIterator(IdentityHashMap.this, null);
/*      */     }
/*      */     public int size() {
/* 1040 */       return IdentityHashMap.this.size;
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1043 */       return IdentityHashMap.this.containsValue(paramObject);
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1046 */       for (Iterator localIterator = iterator(); localIterator.hasNext(); ) {
/* 1047 */         if (localIterator.next() == paramObject) {
/* 1048 */           localIterator.remove();
/* 1049 */           return true;
/*      */         }
/*      */       }
/* 1052 */       return false;
/*      */     }
/*      */     public void clear() {
/* 1055 */       IdentityHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.IdentityHashMap
 * JD-Core Version:    0.6.2
 */