/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractMap.SimpleEntry;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import sun.misc.Hashing;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.misc.VM;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class ConcurrentHashMap<K, V> extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 7249069246763182397L;
/*      */   static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MIN_SEGMENT_TABLE_CAPACITY = 2;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int RETRIES_BEFORE_LOCK = 2;
/*  228 */   private final transient int hashSeed = randomHashSeed(this);
/*      */   final int segmentMask;
/*      */   final int segmentShift;
/*      */   final Segment<K, V>[] segments;
/*      */   transient Set<K> keySet;
/*      */   transient Set<Map.Entry<K, V>> entrySet;
/*      */   transient Collection<V> values;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long SBASE;
/* 1615 */   private static final int SSHIFT = 31 - Integer.numberOfLeadingZeros(i);
/*      */   private static final long TBASE;
/* 1616 */   private static final int TSHIFT = 31 - Integer.numberOfLeadingZeros(j);
/*      */   private static final long HASHSEED_OFFSET;
/*      */   private static final long SEGSHIFT_OFFSET;
/*      */   private static final long SEGMASK_OFFSET;
/*      */   private static final long SEGMENTS_OFFSET;
/*      */ 
/*      */   private static int randomHashSeed(ConcurrentHashMap paramConcurrentHashMap)
/*      */   {
/*  231 */     if ((VM.isBooted()) && (Holder.ALTERNATIVE_HASHING)) {
/*  232 */       return Hashing.randomHashSeed(paramConcurrentHashMap);
/*      */     }
/*      */ 
/*  235 */     return 0;
/*      */   }
/*      */ 
/*      */   static final <K, V> HashEntry<K, V> entryAt(HashEntry<K, V>[] paramArrayOfHashEntry, int paramInt)
/*      */   {
/*  305 */     return paramArrayOfHashEntry == null ? null : (HashEntry)UNSAFE.getObjectVolatile(paramArrayOfHashEntry, (paramInt << TSHIFT) + TBASE);
/*      */   }
/*      */ 
/*      */   static final <K, V> void setEntryAt(HashEntry<K, V>[] paramArrayOfHashEntry, int paramInt, HashEntry<K, V> paramHashEntry)
/*      */   {
/*  316 */     UNSAFE.putOrderedObject(paramArrayOfHashEntry, (paramInt << TSHIFT) + TBASE, paramHashEntry);
/*      */   }
/*      */ 
/*      */   private int hash(Object paramObject)
/*      */   {
/*  327 */     int i = this.hashSeed;
/*      */ 
/*  329 */     if ((0 != i) && ((paramObject instanceof String))) {
/*  330 */       return Hashing.stringHash32((String)paramObject);
/*      */     }
/*      */ 
/*  333 */     i ^= paramObject.hashCode();
/*      */ 
/*  337 */     i += (i << 15 ^ 0xFFFFCD7D);
/*  338 */     i ^= i >>> 10;
/*  339 */     i += (i << 3);
/*  340 */     i ^= i >>> 6;
/*  341 */     i += (i << 2) + (i << 14);
/*  342 */     return i ^ i >>> 16;
/*      */   }
/*      */ 
/*      */   static final <K, V> Segment<K, V> segmentAt(Segment<K, V>[] paramArrayOfSegment, int paramInt)
/*      */   {
/*  724 */     long l = (paramInt << SSHIFT) + SBASE;
/*  725 */     return paramArrayOfSegment == null ? null : (Segment)UNSAFE.getObjectVolatile(paramArrayOfSegment, l);
/*      */   }
/*      */ 
/*      */   private Segment<K, V> ensureSegment(int paramInt)
/*      */   {
/*  738 */     Segment[] arrayOfSegment = this.segments;
/*  739 */     long l = (paramInt << SSHIFT) + SBASE;
/*      */     Object localObject;
/*  741 */     if ((localObject = (Segment)UNSAFE.getObjectVolatile(arrayOfSegment, l)) == null) {
/*  742 */       Segment localSegment1 = arrayOfSegment[0];
/*  743 */       int i = localSegment1.table.length;
/*  744 */       float f = localSegment1.loadFactor;
/*  745 */       int j = (int)(i * f);
/*  746 */       HashEntry[] arrayOfHashEntry = (HashEntry[])new HashEntry[i];
/*  747 */       if ((localObject = (Segment)UNSAFE.getObjectVolatile(arrayOfSegment, l)) == null)
/*      */       {
/*  749 */         Segment localSegment2 = new Segment(f, j, arrayOfHashEntry);
/*      */ 
/*  751 */         while ((localObject = (Segment)UNSAFE.getObjectVolatile(arrayOfSegment, l)) == null) {
/*  752 */           if (UNSAFE.compareAndSwapObject(arrayOfSegment, l, null, localObject = localSegment2))
/*  753 */             break;
/*      */         }
/*      */       }
/*      */     }
/*  757 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Segment<K, V> segmentForHash(int paramInt)
/*      */   {
/*  767 */     long l = ((paramInt >>> this.segmentShift & this.segmentMask) << SSHIFT) + SBASE;
/*  768 */     return (Segment)UNSAFE.getObjectVolatile(this.segments, l);
/*      */   }
/*      */ 
/*      */   static final <K, V> HashEntry<K, V> entryForHash(Segment<K, V> paramSegment, int paramInt)
/*      */   {
/*      */     HashEntry[] arrayOfHashEntry;
/*  777 */     return (paramSegment == null) || ((arrayOfHashEntry = paramSegment.table) == null) ? null : (HashEntry)UNSAFE.getObjectVolatile(arrayOfHashEntry, ((arrayOfHashEntry.length - 1 & paramInt) << TSHIFT) + TBASE);
/*      */   }
/*      */ 
/*      */   public ConcurrentHashMap(int paramInt1, float paramFloat, int paramInt2)
/*      */   {
/*  803 */     if ((paramFloat <= 0.0F) || (paramInt1 < 0) || (paramInt2 <= 0))
/*  804 */       throw new IllegalArgumentException();
/*  805 */     if (paramInt2 > 65536) {
/*  806 */       paramInt2 = 65536;
/*      */     }
/*  808 */     int i = 0;
/*  809 */     int j = 1;
/*  810 */     while (j < paramInt2) {
/*  811 */       i++;
/*  812 */       j <<= 1;
/*      */     }
/*  814 */     this.segmentShift = (32 - i);
/*  815 */     this.segmentMask = (j - 1);
/*  816 */     if (paramInt1 > 1073741824)
/*  817 */       paramInt1 = 1073741824;
/*  818 */     int k = paramInt1 / j;
/*  819 */     if (k * j < paramInt1)
/*  820 */       k++;
/*  821 */     int m = 2;
/*  822 */     while (m < k) {
/*  823 */       m <<= 1;
/*      */     }
/*  825 */     Segment localSegment = new Segment(paramFloat, (int)(m * paramFloat), (HashEntry[])new HashEntry[m]);
/*      */ 
/*  828 */     Segment[] arrayOfSegment = (Segment[])new Segment[j];
/*  829 */     UNSAFE.putOrderedObject(arrayOfSegment, SBASE, localSegment);
/*  830 */     this.segments = arrayOfSegment;
/*      */   }
/*      */ 
/*      */   public ConcurrentHashMap(int paramInt, float paramFloat)
/*      */   {
/*  848 */     this(paramInt, paramFloat, 16);
/*      */   }
/*      */ 
/*      */   public ConcurrentHashMap(int paramInt)
/*      */   {
/*  861 */     this(paramInt, 0.75F, 16);
/*      */   }
/*      */ 
/*      */   public ConcurrentHashMap()
/*      */   {
/*  869 */     this(16, 0.75F, 16);
/*      */   }
/*      */ 
/*      */   public ConcurrentHashMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/*  881 */     this(Math.max((int)(paramMap.size() / 0.75F) + 1, 16), 0.75F, 16);
/*      */ 
/*  884 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  902 */     long l = 0L;
/*  903 */     Segment[] arrayOfSegment = this.segments;
/*      */     Segment localSegment;
/*  904 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/*  905 */       localSegment = segmentAt(arrayOfSegment, i);
/*  906 */       if (localSegment != null) {
/*  907 */         if (localSegment.count != 0)
/*  908 */           return false;
/*  909 */         l += localSegment.modCount;
/*      */       }
/*      */     }
/*  912 */     if (l != 0L) {
/*  913 */       for (i = 0; i < arrayOfSegment.length; i++) {
/*  914 */         localSegment = segmentAt(arrayOfSegment, i);
/*  915 */         if (localSegment != null) {
/*  916 */           if (localSegment.count != 0)
/*  917 */             return false;
/*  918 */           l -= localSegment.modCount;
/*      */         }
/*      */       }
/*  921 */       if (l != 0L)
/*  922 */         return false;
/*      */     }
/*  924 */     return true;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  937 */     Segment[] arrayOfSegment = this.segments;
/*      */ 
/*  941 */     long l2 = 0L;
/*  942 */     int k = -1;
/*      */     int i;
/*      */     int j;
/*      */     try
/*      */     {
/*      */       int m;
/*      */       while (true)
/*      */       {
/*  945 */         if (k++ == 2) {
/*  946 */           for (m = 0; m < arrayOfSegment.length; m++)
/*  947 */             ensureSegment(m).lock();
/*      */         }
/*  949 */         long l1 = 0L;
/*  950 */         i = 0;
/*  951 */         j = 0;
/*  952 */         for (m = 0; m < arrayOfSegment.length; m++) {
/*  953 */           Segment localSegment = segmentAt(arrayOfSegment, m);
/*  954 */           if (localSegment != null) {
/*  955 */             l1 += localSegment.modCount;
/*  956 */             int n = localSegment.count;
/*  957 */             if ((n < 0) || (i += n < 0))
/*  958 */               j = 1;
/*      */           }
/*      */         }
/*  961 */         if (l1 == l2)
/*      */           break;
/*  963 */         l2 = l1;
/*      */       }
/*      */ 
/*  966 */       if (k > 2)
/*  967 */         for (m = 0; m < arrayOfSegment.length; m++)
/*  968 */           segmentAt(arrayOfSegment, m).unlock();
/*      */     }
/*      */     finally
/*      */     {
/*  966 */       if (k > 2) {
/*  967 */         for (int i1 = 0; i1 < arrayOfSegment.length; i1++)
/*  968 */           segmentAt(arrayOfSegment, i1).unlock();
/*      */       }
/*      */     }
/*  971 */     return j != 0 ? 2147483647 : i;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/*  988 */     int i = hash(paramObject);
/*  989 */     long l = ((i >>> this.segmentShift & this.segmentMask) << SSHIFT) + SBASE;
/*      */     Segment localSegment;
/*      */     HashEntry[] arrayOfHashEntry;
/*  990 */     if (((localSegment = (Segment)UNSAFE.getObjectVolatile(this.segments, l)) != null) && ((arrayOfHashEntry = localSegment.table) != null))
/*      */     {
/*  992 */       for (HashEntry localHashEntry = (HashEntry)UNSAFE.getObjectVolatile(arrayOfHashEntry, ((arrayOfHashEntry.length - 1 & i) << TSHIFT) + TBASE); 
/*  994 */         localHashEntry != null; localHashEntry = localHashEntry.next)
/*      */       {
/*      */         Object localObject;
/*  996 */         if (((localObject = localHashEntry.key) == paramObject) || ((localHashEntry.hash == i) && (paramObject.equals(localObject))))
/*  997 */           return localHashEntry.value;
/*      */       }
/*      */     }
/* 1000 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/* 1016 */     int i = hash(paramObject);
/* 1017 */     long l = ((i >>> this.segmentShift & this.segmentMask) << SSHIFT) + SBASE;
/*      */     Segment localSegment;
/*      */     HashEntry[] arrayOfHashEntry;
/* 1018 */     if (((localSegment = (Segment)UNSAFE.getObjectVolatile(this.segments, l)) != null) && ((arrayOfHashEntry = localSegment.table) != null))
/*      */     {
/* 1020 */       for (HashEntry localHashEntry = (HashEntry)UNSAFE.getObjectVolatile(arrayOfHashEntry, ((arrayOfHashEntry.length - 1 & i) << TSHIFT) + TBASE); 
/* 1022 */         localHashEntry != null; localHashEntry = localHashEntry.next)
/*      */       {
/*      */         Object localObject;
/* 1024 */         if (((localObject = localHashEntry.key) == paramObject) || ((localHashEntry.hash == i) && (paramObject.equals(localObject))))
/* 1025 */           return true;
/*      */       }
/*      */     }
/* 1028 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/* 1044 */     if (paramObject == null)
/* 1045 */       throw new NullPointerException();
/* 1046 */     Segment[] arrayOfSegment = this.segments;
/* 1047 */     boolean bool = false;
/* 1048 */     long l1 = 0L;
/* 1049 */     int i = -1;
/*      */     try {
/*      */       while (true) {
/* 1052 */         if (i++ == 2) {
/* 1053 */           for (int j = 0; j < arrayOfSegment.length; j++)
/* 1054 */             ensureSegment(j).lock();
/*      */         }
/* 1056 */         long l2 = 0L;
/* 1057 */         int m = 0;
/* 1058 */         for (int n = 0; n < arrayOfSegment.length; n++)
/*      */         {
/* 1060 */           Segment localSegment = segmentAt(arrayOfSegment, n);
/*      */           HashEntry[] arrayOfHashEntry;
/* 1061 */           if ((localSegment != null) && ((arrayOfHashEntry = localSegment.table) != null)) {
/* 1062 */             for (int i1 = 0; i1 < arrayOfHashEntry.length; i1++)
/*      */             {
/* 1064 */               for (HashEntry localHashEntry = entryAt(arrayOfHashEntry, i1); localHashEntry != null; localHashEntry = localHashEntry.next) {
/* 1065 */                 Object localObject1 = localHashEntry.value;
/* 1066 */                 if ((localObject1 != null) && (paramObject.equals(localObject1))) {
/* 1067 */                   bool = true;
/* 1068 */                   break label207;
/*      */                 }
/*      */               }
/*      */             }
/* 1072 */             m += localSegment.modCount;
/*      */           }
/*      */         }
/* 1075 */         if ((i > 0) && (m == l1))
/*      */           break;
/* 1077 */         l1 = m;
/*      */       }
/*      */ 
/* 1080 */       label207: if (i > 2)
/* 1081 */         for (int k = 0; k < arrayOfSegment.length; k++)
/* 1082 */           segmentAt(arrayOfSegment, k).unlock();
/*      */     }
/*      */     finally
/*      */     {
/* 1080 */       if (i > 2) {
/* 1081 */         for (int i2 = 0; i2 < arrayOfSegment.length; i2++)
/* 1082 */           segmentAt(arrayOfSegment, i2).unlock();
/*      */       }
/*      */     }
/* 1085 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/* 1104 */     return containsValue(paramObject);
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/* 1123 */     if (paramV == null)
/* 1124 */       throw new NullPointerException();
/* 1125 */     int i = hash(paramK);
/* 1126 */     int j = i >>> this.segmentShift & this.segmentMask;
/*      */     Segment localSegment;
/* 1127 */     if ((localSegment = (Segment)UNSAFE.getObject(this.segments, (j << SSHIFT) + SBASE)) == null)
/*      */     {
/* 1129 */       localSegment = ensureSegment(j);
/* 1130 */     }return localSegment.put(paramK, i, paramV, false);
/*      */   }
/*      */ 
/*      */   public V putIfAbsent(K paramK, V paramV)
/*      */   {
/* 1143 */     if (paramV == null)
/* 1144 */       throw new NullPointerException();
/* 1145 */     int i = hash(paramK);
/* 1146 */     int j = i >>> this.segmentShift & this.segmentMask;
/*      */     Segment localSegment;
/* 1147 */     if ((localSegment = (Segment)UNSAFE.getObject(this.segments, (j << SSHIFT) + SBASE)) == null)
/*      */     {
/* 1149 */       localSegment = ensureSegment(j);
/* 1150 */     }return localSegment.put(paramK, i, paramV, true);
/*      */   }
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*      */   {
/* 1161 */     for (Map.Entry localEntry : paramMap.entrySet())
/* 1162 */       put(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/* 1175 */     int i = hash(paramObject);
/* 1176 */     Segment localSegment = segmentForHash(i);
/* 1177 */     return localSegment == null ? null : localSegment.remove(paramObject, i, null);
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject1, Object paramObject2)
/*      */   {
/* 1186 */     int i = hash(paramObject1);
/*      */     Segment localSegment;
/* 1188 */     return (paramObject2 != null) && ((localSegment = segmentForHash(i)) != null) && (localSegment.remove(paramObject1, i, paramObject2) != null);
/*      */   }
/*      */ 
/*      */   public boolean replace(K paramK, V paramV1, V paramV2)
/*      */   {
/* 1198 */     int i = hash(paramK);
/* 1199 */     if ((paramV1 == null) || (paramV2 == null))
/* 1200 */       throw new NullPointerException();
/* 1201 */     Segment localSegment = segmentForHash(i);
/* 1202 */     return (localSegment != null) && (localSegment.replace(paramK, i, paramV1, paramV2));
/*      */   }
/*      */ 
/*      */   public V replace(K paramK, V paramV)
/*      */   {
/* 1213 */     int i = hash(paramK);
/* 1214 */     if (paramV == null)
/* 1215 */       throw new NullPointerException();
/* 1216 */     Segment localSegment = segmentForHash(i);
/* 1217 */     return localSegment == null ? null : localSegment.replace(paramK, i, paramV);
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1224 */     Segment[] arrayOfSegment = this.segments;
/* 1225 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/* 1226 */       Segment localSegment = segmentAt(arrayOfSegment, i);
/* 1227 */       if (localSegment != null)
/* 1228 */         localSegment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/* 1249 */     Set localSet = this.keySet;
/* 1250 */     return this.keySet = new KeySet();
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/* 1270 */     Collection localCollection = this.values;
/* 1271 */     return this.values = new Values();
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1291 */     Set localSet = this.entrySet;
/* 1292 */     return this.entrySet = new EntrySet();
/*      */   }
/*      */ 
/*      */   public Enumeration<K> keys()
/*      */   {
/* 1302 */     return new KeyIterator();
/*      */   }
/*      */ 
/*      */   public Enumeration<V> elements()
/*      */   {
/* 1312 */     return new ValueIterator();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1506 */     for (int i = 0; i < this.segments.length; i++)
/* 1507 */       ensureSegment(i);
/* 1508 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1510 */     Segment[] arrayOfSegment = this.segments;
/* 1511 */     for (int j = 0; j < arrayOfSegment.length; j++) {
/* 1512 */       Segment localSegment = segmentAt(arrayOfSegment, j);
/* 1513 */       localSegment.lock();
/*      */       try {
/* 1515 */         HashEntry[] arrayOfHashEntry = localSegment.table;
/* 1516 */         for (int k = 0; k < arrayOfHashEntry.length; k++)
/*      */         {
/* 1518 */           for (HashEntry localHashEntry = entryAt(arrayOfHashEntry, k); localHashEntry != null; localHashEntry = localHashEntry.next) {
/* 1519 */             paramObjectOutputStream.writeObject(localHashEntry.key);
/* 1520 */             paramObjectOutputStream.writeObject(localHashEntry.value);
/*      */           }
/*      */         }
/*      */       } finally {
/* 1524 */         localSegment.unlock();
/*      */       }
/*      */     }
/* 1527 */     paramObjectOutputStream.writeObject(null);
/* 1528 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1540 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1541 */     Segment[] arrayOfSegment1 = (Segment[])localGetField.get("segments", null);
/*      */ 
/* 1543 */     int i = arrayOfSegment1.length;
/* 1544 */     if ((i < 1) || (i > 65536) || ((i & i - 1) != 0))
/*      */     {
/* 1546 */       throw new InvalidObjectException("Bad number of segments:" + i);
/*      */     }
/* 1548 */     int j = 0; int k = i;
/* 1549 */     while (k > 1) {
/* 1550 */       j++;
/* 1551 */       k >>>= 1;
/*      */     }
/* 1553 */     UNSAFE.putIntVolatile(this, SEGSHIFT_OFFSET, 32 - j);
/* 1554 */     UNSAFE.putIntVolatile(this, SEGMASK_OFFSET, i - 1);
/* 1555 */     UNSAFE.putObjectVolatile(this, SEGMENTS_OFFSET, arrayOfSegment1);
/*      */ 
/* 1558 */     UNSAFE.putIntVolatile(this, HASHSEED_OFFSET, randomHashSeed(this));
/*      */ 
/* 1561 */     int m = 2;
/* 1562 */     Segment[] arrayOfSegment2 = this.segments;
/*      */     Object localObject2;
/* 1563 */     for (int n = 0; n < arrayOfSegment2.length; n++) {
/* 1564 */       localObject2 = arrayOfSegment2[n];
/* 1565 */       if (localObject2 != null) {
/* 1566 */         ((Segment)localObject2).threshold = ((int)(m * ((Segment)localObject2).loadFactor));
/* 1567 */         ((Segment)localObject2).table = ((HashEntry[])new HashEntry[m]);
/*      */       }
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/* 1573 */       Object localObject1 = paramObjectInputStream.readObject();
/* 1574 */       localObject2 = paramObjectInputStream.readObject();
/* 1575 */       if (localObject1 == null)
/*      */         break;
/* 1577 */       put(localObject1, localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     int j;
/*      */     int i;
/*      */     try
/*      */     {
/* 1595 */       UNSAFE = Unsafe.getUnsafe();
/* 1596 */       HashEntry[] arrayOfHashEntry = [Ljava.util.concurrent.ConcurrentHashMap.HashEntry.class;
/* 1597 */       Segment[] arrayOfSegment = [Ljava.util.concurrent.ConcurrentHashMap.Segment.class;
/* 1598 */       TBASE = UNSAFE.arrayBaseOffset(arrayOfHashEntry);
/* 1599 */       SBASE = UNSAFE.arrayBaseOffset(arrayOfSegment);
/* 1600 */       j = UNSAFE.arrayIndexScale(arrayOfHashEntry);
/* 1601 */       i = UNSAFE.arrayIndexScale(arrayOfSegment);
/* 1602 */       HASHSEED_OFFSET = UNSAFE.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("hashSeed"));
/*      */ 
/* 1604 */       SEGSHIFT_OFFSET = UNSAFE.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("segmentShift"));
/*      */ 
/* 1606 */       SEGMASK_OFFSET = UNSAFE.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("segmentMask"));
/*      */ 
/* 1608 */       SEGMENTS_OFFSET = UNSAFE.objectFieldOffset(ConcurrentHashMap.class.getDeclaredField("segments"));
/*      */     }
/*      */     catch (Exception localException) {
/* 1611 */       throw new Error(localException);
/*      */     }
/* 1613 */     if (((i & i - 1) != 0) || ((j & j - 1) != 0))
/* 1614 */       throw new Error("data type scale not a power of two");
/*      */   }
/*      */ 
/*      */   final class EntryIterator extends ConcurrentHashMap<K, V>.HashIterator
/*      */     implements Iterator<Map.Entry<K, V>>
/*      */   {
/*      */     EntryIterator()
/*      */     {
/* 1416 */       super();
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> next()
/*      */     {
/* 1421 */       ConcurrentHashMap.HashEntry localHashEntry = super.nextEntry();
/* 1422 */       return new ConcurrentHashMap.WriteThroughEntry(ConcurrentHashMap.this, localHashEntry.key, localHashEntry.value);
/*      */     }
/*      */   }
/*      */ 
/*      */   final class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     EntrySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<Map.Entry<K, V>> iterator()
/*      */     {
/* 1467 */       return new ConcurrentHashMap.EntryIterator(ConcurrentHashMap.this);
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1470 */       if (!(paramObject instanceof Map.Entry))
/* 1471 */         return false;
/* 1472 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1473 */       Object localObject = ConcurrentHashMap.this.get(localEntry.getKey());
/* 1474 */       return (localObject != null) && (localObject.equals(localEntry.getValue()));
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1477 */       if (!(paramObject instanceof Map.Entry))
/* 1478 */         return false;
/* 1479 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 1480 */       return ConcurrentHashMap.this.remove(localEntry.getKey(), localEntry.getValue());
/*      */     }
/*      */     public int size() {
/* 1483 */       return ConcurrentHashMap.this.size();
/*      */     }
/*      */     public boolean isEmpty() {
/* 1486 */       return ConcurrentHashMap.this.isEmpty();
/*      */     }
/*      */     public void clear() {
/* 1489 */       ConcurrentHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class HashEntry<K, V>
/*      */   {
/*      */     final int hash;
/*      */     final K key;
/*      */     volatile V value;
/*      */     volatile HashEntry<K, V> next;
/*      */     static final Unsafe UNSAFE;
/*      */     static final long nextOffset;
/*      */ 
/*      */     HashEntry(int paramInt, K paramK, V paramV, HashEntry<K, V> paramHashEntry)
/*      */     {
/*  269 */       this.hash = paramInt;
/*  270 */       this.key = paramK;
/*  271 */       this.value = paramV;
/*  272 */       this.next = paramHashEntry;
/*      */     }
/*      */ 
/*      */     final void setNext(HashEntry<K, V> paramHashEntry)
/*      */     {
/*  280 */       UNSAFE.putOrderedObject(this, nextOffset, paramHashEntry);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  288 */         UNSAFE = Unsafe.getUnsafe();
/*  289 */         HashEntry localHashEntry = HashEntry.class;
/*  290 */         nextOffset = UNSAFE.objectFieldOffset(localHashEntry.getDeclaredField("next"));
/*      */       }
/*      */       catch (Exception localException) {
/*  293 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class HashIterator
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     ConcurrentHashMap.HashEntry<K, V>[] currentTable;
/*      */     ConcurrentHashMap.HashEntry<K, V> nextEntry;
/*      */     ConcurrentHashMap.HashEntry<K, V> lastReturned;
/*      */ 
/*      */     HashIterator()
/*      */     {
/* 1325 */       this.nextSegmentIndex = (ConcurrentHashMap.this.segments.length - 1);
/* 1326 */       this.nextTableIndex = -1;
/* 1327 */       advance();
/*      */     }
/*      */ 
/*      */     final void advance()
/*      */     {
/*      */       while (true)
/* 1336 */         if (this.nextTableIndex >= 0) {
/* 1337 */           if ((this.nextEntry = ConcurrentHashMap.entryAt(this.currentTable, this.nextTableIndex--)) != null)
/*      */           {
/* 1339 */             break;
/*      */           }
/*      */         } else { if (this.nextSegmentIndex < 0) break;
/* 1342 */           ConcurrentHashMap.Segment localSegment = ConcurrentHashMap.segmentAt(ConcurrentHashMap.this.segments, this.nextSegmentIndex--);
/* 1343 */           if ((localSegment != null) && ((this.currentTable = localSegment.table) != null))
/* 1344 */             this.nextTableIndex = (this.currentTable.length - 1);
/*      */         }
/*      */     }
/*      */ 
/*      */     final ConcurrentHashMap.HashEntry<K, V> nextEntry()
/*      */     {
/* 1352 */       ConcurrentHashMap.HashEntry localHashEntry = this.nextEntry;
/* 1353 */       if (localHashEntry == null)
/* 1354 */         throw new NoSuchElementException();
/* 1355 */       this.lastReturned = localHashEntry;
/* 1356 */       if ((this.nextEntry = localHashEntry.next) == null)
/* 1357 */         advance();
/* 1358 */       return localHashEntry;
/*      */     }
/*      */     public final boolean hasNext() {
/* 1361 */       return this.nextEntry != null; } 
/* 1362 */     public final boolean hasMoreElements() { return this.nextEntry != null; }
/*      */ 
/*      */     public final void remove() {
/* 1365 */       if (this.lastReturned == null)
/* 1366 */         throw new IllegalStateException();
/* 1367 */       ConcurrentHashMap.this.remove(this.lastReturned.key);
/* 1368 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Holder
/*      */   {
/*  220 */     static final boolean ALTERNATIVE_HASHING = i <= 1073741824;
/*      */ 
/*      */     static
/*      */     {
/*  199 */       String str = (String)AccessController.doPrivileged(new GetPropertyAction("jdk.map.althashing.threshold"));
/*      */       int i;
/*      */       try
/*      */       {
/*  205 */         i = null != str ? Integer.parseInt(str) : 2147483647;
/*      */ 
/*  210 */         if (i == -1) {
/*  211 */           i = 2147483647;
/*      */         }
/*      */ 
/*  214 */         if (i < 0)
/*  215 */           throw new IllegalArgumentException("value must be positive integer.");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*  218 */         throw new Error("Illegal value for 'jdk.map.althashing.threshold'", localIllegalArgumentException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class KeyIterator extends ConcurrentHashMap<K, V>.HashIterator
/*      */     implements Iterator<K>, Enumeration<K>
/*      */   {
/*      */     KeyIterator()
/*      */     {
/* 1372 */       super();
/*      */     }
/*      */ 
/*      */     public final K next() {
/* 1376 */       return super.nextEntry().key; } 
/* 1377 */     public final K nextElement() { return super.nextEntry().key; }
/*      */ 
/*      */   }
/*      */ 
/*      */   final class KeySet extends AbstractSet<K>
/*      */   {
/*      */     KeySet()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<K> iterator()
/*      */     {
/* 1428 */       return new ConcurrentHashMap.KeyIterator(ConcurrentHashMap.this);
/*      */     }
/*      */     public int size() {
/* 1431 */       return ConcurrentHashMap.this.size();
/*      */     }
/*      */     public boolean isEmpty() {
/* 1434 */       return ConcurrentHashMap.this.isEmpty();
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1437 */       return ConcurrentHashMap.this.containsKey(paramObject);
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 1440 */       return ConcurrentHashMap.this.remove(paramObject) != null;
/*      */     }
/*      */     public void clear() {
/* 1443 */       ConcurrentHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Segment<K, V> extends ReentrantLock
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2249069246763182397L;
/*  386 */     static final int MAX_SCAN_RETRIES = Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;
/*      */     volatile transient ConcurrentHashMap.HashEntry<K, V>[] table;
/*      */     transient int count;
/*      */     transient int modCount;
/*      */     transient int threshold;
/*      */     final float loadFactor;
/*      */ 
/*      */     Segment(float paramFloat, int paramInt, ConcurrentHashMap.HashEntry<K, V>[] paramArrayOfHashEntry)
/*      */     {
/*  426 */       this.loadFactor = paramFloat;
/*  427 */       this.threshold = paramInt;
/*  428 */       this.table = paramArrayOfHashEntry;
/*      */     }
/*      */ 
/*      */     final V put(K paramK, int paramInt, V paramV, boolean paramBoolean) {
/*  432 */       ConcurrentHashMap.HashEntry localHashEntry1 = tryLock() ? null : scanAndLockForPut(paramK, paramInt, paramV);
/*      */       Object localObject1;
/*      */       try {
/*  436 */         ConcurrentHashMap.HashEntry[] arrayOfHashEntry = this.table;
/*  437 */         int i = arrayOfHashEntry.length - 1 & paramInt;
/*  438 */         ConcurrentHashMap.HashEntry localHashEntry2 = ConcurrentHashMap.entryAt(arrayOfHashEntry, i);
/*  439 */         ConcurrentHashMap.HashEntry localHashEntry3 = localHashEntry2;
/*  440 */         while (localHashEntry3 != null)
/*      */         {
/*      */           Object localObject2;
/*  442 */           if (((localObject2 = localHashEntry3.key) == paramK) || ((localHashEntry3.hash == paramInt) && (paramK.equals(localObject2))))
/*      */           {
/*  444 */             localObject1 = localHashEntry3.value;
/*  445 */             if (paramBoolean) break label218;
/*  446 */             localHashEntry3.value = paramV;
/*  447 */             this.modCount += 1; break label218;
/*      */           }
/*      */ 
/*  451 */           localHashEntry3 = localHashEntry3.next;
/*      */         }
/*      */ 
/*  454 */         if (localHashEntry1 != null)
/*  455 */           localHashEntry1.setNext(localHashEntry2);
/*      */         else
/*  457 */           localHashEntry1 = new ConcurrentHashMap.HashEntry(paramInt, paramK, paramV, localHashEntry2);
/*  458 */         int j = this.count + 1;
/*  459 */         if ((j > this.threshold) && (arrayOfHashEntry.length < 1073741824))
/*  460 */           rehash(localHashEntry1);
/*      */         else
/*  462 */           ConcurrentHashMap.setEntryAt(arrayOfHashEntry, i, localHashEntry1);
/*  463 */         this.modCount += 1;
/*  464 */         this.count = j;
/*  465 */         localObject1 = null;
/*      */       }
/*      */       finally
/*      */       {
/*  470 */         label218: unlock();
/*      */       }
/*  472 */       return localObject1;
/*      */     }
/*      */ 
/*      */     private void rehash(ConcurrentHashMap.HashEntry<K, V> paramHashEntry)
/*      */     {
/*  497 */       ConcurrentHashMap.HashEntry[] arrayOfHashEntry1 = this.table;
/*  498 */       int i = arrayOfHashEntry1.length;
/*  499 */       int j = i << 1;
/*  500 */       this.threshold = ((int)(j * this.loadFactor));
/*  501 */       ConcurrentHashMap.HashEntry[] arrayOfHashEntry2 = (ConcurrentHashMap.HashEntry[])new ConcurrentHashMap.HashEntry[j];
/*      */ 
/*  503 */       int k = j - 1;
/*  504 */       for (int m = 0; m < i; m++) {
/*  505 */         ConcurrentHashMap.HashEntry localHashEntry1 = arrayOfHashEntry1[m];
/*  506 */         if (localHashEntry1 != null) {
/*  507 */           ConcurrentHashMap.HashEntry localHashEntry2 = localHashEntry1.next;
/*  508 */           int n = localHashEntry1.hash & k;
/*  509 */           if (localHashEntry2 == null) {
/*  510 */             arrayOfHashEntry2[n] = localHashEntry1;
/*      */           } else {
/*  512 */             Object localObject1 = localHashEntry1;
/*  513 */             int i1 = n;
/*  514 */             for (ConcurrentHashMap.HashEntry localHashEntry3 = localHashEntry2; 
/*  515 */               localHashEntry3 != null; 
/*  516 */               localHashEntry3 = localHashEntry3.next) {
/*  517 */               int i2 = localHashEntry3.hash & k;
/*  518 */               if (i2 != i1) {
/*  519 */                 i1 = i2;
/*  520 */                 localObject1 = localHashEntry3;
/*      */               }
/*      */             }
/*  523 */             arrayOfHashEntry2[i1] = localObject1;
/*      */ 
/*  525 */             for (localHashEntry3 = localHashEntry1; localHashEntry3 != localObject1; localHashEntry3 = localHashEntry3.next) {
/*  526 */               Object localObject2 = localHashEntry3.value;
/*  527 */               int i3 = localHashEntry3.hash;
/*  528 */               int i4 = i3 & k;
/*  529 */               ConcurrentHashMap.HashEntry localHashEntry4 = arrayOfHashEntry2[i4];
/*  530 */               arrayOfHashEntry2[i4] = new ConcurrentHashMap.HashEntry(i3, localHashEntry3.key, localObject2, localHashEntry4);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  535 */       m = paramHashEntry.hash & k;
/*  536 */       paramHashEntry.setNext(arrayOfHashEntry2[m]);
/*  537 */       arrayOfHashEntry2[m] = paramHashEntry;
/*  538 */       this.table = arrayOfHashEntry2;
/*      */     }
/*      */ 
/*      */     private ConcurrentHashMap.HashEntry<K, V> scanAndLockForPut(K paramK, int paramInt, V paramV)
/*      */     {
/*  552 */       Object localObject1 = ConcurrentHashMap.entryForHash(this, paramInt);
/*  553 */       Object localObject2 = localObject1;
/*  554 */       ConcurrentHashMap.HashEntry localHashEntry1 = null;
/*  555 */       int i = -1;
/*  556 */       while (!tryLock())
/*      */       {
/*  558 */         if (i < 0) {
/*  559 */           if (localObject2 == null) {
/*  560 */             if (localHashEntry1 == null)
/*  561 */               localHashEntry1 = new ConcurrentHashMap.HashEntry(paramInt, paramK, paramV, null);
/*  562 */             i = 0;
/*      */           }
/*  564 */           else if (paramK.equals(((ConcurrentHashMap.HashEntry)localObject2).key)) {
/*  565 */             i = 0;
/*      */           } else {
/*  567 */             localObject2 = ((ConcurrentHashMap.HashEntry)localObject2).next;
/*      */           }
/*      */         } else { i++; if (i > MAX_SCAN_RETRIES) {
/*  570 */             lock();
/*  571 */             break;
/*      */           }
/*      */           ConcurrentHashMap.HashEntry localHashEntry2;
/*  573 */           if (((i & 0x1) == 0) && ((localHashEntry2 = ConcurrentHashMap.entryForHash(this, paramInt)) != localObject1))
/*      */           {
/*  575 */             localObject2 = localObject1 = localHashEntry2;
/*  576 */             i = -1;
/*      */           } }
/*      */       }
/*  579 */       return localHashEntry1;
/*      */     }
/*      */ 
/*      */     private void scanAndLock(Object paramObject, int paramInt)
/*      */     {
/*  591 */       Object localObject1 = ConcurrentHashMap.entryForHash(this, paramInt);
/*  592 */       Object localObject2 = localObject1;
/*  593 */       int i = -1;
/*  594 */       while (!tryLock())
/*      */       {
/*  596 */         if (i < 0) {
/*  597 */           if ((localObject2 == null) || (paramObject.equals(((ConcurrentHashMap.HashEntry)localObject2).key)))
/*  598 */             i = 0;
/*      */           else
/*  600 */             localObject2 = ((ConcurrentHashMap.HashEntry)localObject2).next;
/*      */         } else {
/*  602 */           i++; if (i > MAX_SCAN_RETRIES) {
/*  603 */             lock();
/*  604 */             break;
/*      */           }
/*      */           ConcurrentHashMap.HashEntry localHashEntry;
/*  606 */           if (((i & 0x1) == 0) && ((localHashEntry = ConcurrentHashMap.entryForHash(this, paramInt)) != localObject1))
/*      */           {
/*  608 */             localObject2 = localObject1 = localHashEntry;
/*  609 */             i = -1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     final V remove(Object paramObject1, int paramInt, Object paramObject2)
/*      */     {
/*  618 */       if (!tryLock())
/*  619 */         scanAndLock(paramObject1, paramInt);
/*  620 */       Object localObject1 = null;
/*      */       try {
/*  622 */         ConcurrentHashMap.HashEntry[] arrayOfHashEntry = this.table;
/*  623 */         int i = arrayOfHashEntry.length - 1 & paramInt;
/*  624 */         Object localObject2 = ConcurrentHashMap.entryAt(arrayOfHashEntry, i);
/*  625 */         Object localObject3 = null;
/*  626 */         while (localObject2 != null)
/*      */         {
/*  628 */           ConcurrentHashMap.HashEntry localHashEntry = ((ConcurrentHashMap.HashEntry)localObject2).next;
/*      */           Object localObject4;
/*  629 */           if (((localObject4 = ((ConcurrentHashMap.HashEntry)localObject2).key) == paramObject1) || ((((ConcurrentHashMap.HashEntry)localObject2).hash == paramInt) && (paramObject1.equals(localObject4))))
/*      */           {
/*  631 */             Object localObject5 = ((ConcurrentHashMap.HashEntry)localObject2).value;
/*  632 */             if ((paramObject2 != null) && (paramObject2 != localObject5) && (!paramObject2.equals(localObject5))) break;
/*  633 */             if (localObject3 == null)
/*  634 */               ConcurrentHashMap.setEntryAt(arrayOfHashEntry, i, localHashEntry);
/*      */             else
/*  636 */               localObject3.setNext(localHashEntry);
/*  637 */             this.modCount += 1;
/*  638 */             this.count -= 1;
/*  639 */             localObject1 = localObject5; break;
/*      */           }
/*      */ 
/*  643 */           localObject3 = localObject2;
/*  644 */           localObject2 = localHashEntry;
/*      */         }
/*      */       } finally {
/*  647 */         unlock();
/*      */       }
/*  649 */       return localObject1;
/*      */     }
/*      */ 
/*      */     final boolean replace(K paramK, int paramInt, V paramV1, V paramV2) {
/*  653 */       if (!tryLock())
/*  654 */         scanAndLock(paramK, paramInt);
/*  655 */       boolean bool = false;
/*      */       try
/*      */       {
/*  658 */         for (ConcurrentHashMap.HashEntry localHashEntry = ConcurrentHashMap.entryForHash(this, paramInt); localHashEntry != null; localHashEntry = localHashEntry.next)
/*      */         {
/*      */           Object localObject1;
/*  660 */           if (((localObject1 = localHashEntry.key) == paramK) || ((localHashEntry.hash == paramInt) && (paramK.equals(localObject1))))
/*      */           {
/*  662 */             if (!paramV1.equals(localHashEntry.value)) break;
/*  663 */             localHashEntry.value = paramV2;
/*  664 */             this.modCount += 1;
/*  665 */             bool = true; break;
/*      */           }
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*  671 */         unlock();
/*      */       }
/*  673 */       return bool;
/*      */     }
/*      */ 
/*      */     final V replace(K paramK, int paramInt, V paramV) {
/*  677 */       if (!tryLock())
/*  678 */         scanAndLock(paramK, paramInt);
/*  679 */       Object localObject1 = null;
/*      */       try
/*      */       {
/*  682 */         for (ConcurrentHashMap.HashEntry localHashEntry = ConcurrentHashMap.entryForHash(this, paramInt); localHashEntry != null; localHashEntry = localHashEntry.next)
/*      */         {
/*      */           Object localObject2;
/*  684 */           if (((localObject2 = localHashEntry.key) == paramK) || ((localHashEntry.hash == paramInt) && (paramK.equals(localObject2))))
/*      */           {
/*  686 */             localObject1 = localHashEntry.value;
/*  687 */             localHashEntry.value = paramV;
/*  688 */             this.modCount += 1;
/*  689 */             break;
/*      */           }
/*      */         }
/*      */       } finally {
/*  693 */         unlock();
/*      */       }
/*  695 */       return localObject1;
/*      */     }
/*      */ 
/*      */     final void clear() {
/*  699 */       lock();
/*      */       try {
/*  701 */         ConcurrentHashMap.HashEntry[] arrayOfHashEntry = this.table;
/*  702 */         for (int i = 0; i < arrayOfHashEntry.length; i++)
/*  703 */           ConcurrentHashMap.setEntryAt(arrayOfHashEntry, i, null);
/*  704 */         this.modCount += 1;
/*  705 */         this.count = 0;
/*      */       } finally {
/*  707 */         unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class ValueIterator extends ConcurrentHashMap<K, V>.HashIterator
/*      */     implements Iterator<V>, Enumeration<V>
/*      */   {
/*      */     ValueIterator()
/*      */     {
/* 1380 */       super();
/*      */     }
/*      */ 
/*      */     public final V next() {
/* 1384 */       return super.nextEntry().value; } 
/* 1385 */     public final V nextElement() { return super.nextEntry().value; }
/*      */ 
/*      */   }
/*      */ 
/*      */   final class Values extends AbstractCollection<V>
/*      */   {
/*      */     Values()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Iterator<V> iterator()
/*      */     {
/* 1449 */       return new ConcurrentHashMap.ValueIterator(ConcurrentHashMap.this);
/*      */     }
/*      */     public int size() {
/* 1452 */       return ConcurrentHashMap.this.size();
/*      */     }
/*      */     public boolean isEmpty() {
/* 1455 */       return ConcurrentHashMap.this.isEmpty();
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 1458 */       return ConcurrentHashMap.this.containsValue(paramObject);
/*      */     }
/*      */     public void clear() {
/* 1461 */       ConcurrentHashMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   final class WriteThroughEntry extends AbstractMap.SimpleEntry<K, V>
/*      */   {
/*      */     WriteThroughEntry(V arg2)
/*      */     {
/* 1396 */       super(localObject2);
/*      */     }
/*      */ 
/*      */     public V setValue(V paramV)
/*      */     {
/* 1409 */       if (paramV == null) throw new NullPointerException();
/* 1410 */       Object localObject = super.setValue(paramV);
/* 1411 */       ConcurrentHashMap.this.put(getKey(), paramV);
/* 1412 */       return localObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentHashMap
 * JD-Core Version:    0.6.2
 */