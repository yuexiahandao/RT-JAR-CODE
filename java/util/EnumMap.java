/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V>
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private final Class<K> keyType;
/*     */   private transient K[] keyUniverse;
/*     */   private transient Object[] vals;
/* 104 */   private transient int size = 0;
/*     */ 
/* 109 */   private static final Object NULL = new Object() {
/*     */     public int hashCode() {
/* 111 */       return 0;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 115 */       return "java.util.EnumMap.NULL"; }  } ;
/*     */ 
/* 127 */   private static final Enum[] ZERO_LENGTH_ENUM_ARRAY = new Enum[0];
/*     */ 
/* 370 */   private transient Set<Map.Entry<K, V>> entrySet = null;
/*     */   private static final long serialVersionUID = 458661240069192865L;
/*     */ 
/* 120 */   private Object maskNull(Object paramObject) { return paramObject == null ? NULL : paramObject; }
/*     */ 
/*     */   private V unmaskNull(Object paramObject)
/*     */   {
/* 124 */     return paramObject == NULL ? null : paramObject;
/*     */   }
/*     */ 
/*     */   public EnumMap(Class<K> paramClass)
/*     */   {
/* 136 */     this.keyType = paramClass;
/* 137 */     this.keyUniverse = getKeyUniverse(paramClass);
/* 138 */     this.vals = new Object[this.keyUniverse.length];
/*     */   }
/*     */ 
/*     */   public EnumMap(EnumMap<K, ? extends V> paramEnumMap)
/*     */   {
/* 149 */     this.keyType = paramEnumMap.keyType;
/* 150 */     this.keyUniverse = paramEnumMap.keyUniverse;
/* 151 */     this.vals = ((Object[])paramEnumMap.vals.clone());
/* 152 */     this.size = paramEnumMap.size;
/*     */   }
/*     */ 
/*     */   public EnumMap(Map<K, ? extends V> paramMap)
/*     */   {
/* 168 */     if ((paramMap instanceof EnumMap)) {
/* 169 */       EnumMap localEnumMap = (EnumMap)paramMap;
/* 170 */       this.keyType = localEnumMap.keyType;
/* 171 */       this.keyUniverse = localEnumMap.keyUniverse;
/* 172 */       this.vals = ((Object[])localEnumMap.vals.clone());
/* 173 */       this.size = localEnumMap.size;
/*     */     } else {
/* 175 */       if (paramMap.isEmpty())
/* 176 */         throw new IllegalArgumentException("Specified map is empty");
/* 177 */       this.keyType = ((Enum)paramMap.keySet().iterator().next()).getDeclaringClass();
/* 178 */       this.keyUniverse = getKeyUniverse(this.keyType);
/* 179 */       this.vals = new Object[this.keyUniverse.length];
/* 180 */       putAll(paramMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 192 */     return this.size;
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 203 */     paramObject = maskNull(paramObject);
/*     */ 
/* 205 */     for (Object localObject : this.vals) {
/* 206 */       if (paramObject.equals(localObject))
/* 207 */         return true;
/*     */     }
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 221 */     return (isValidKey(paramObject)) && (this.vals[((Enum)paramObject).ordinal()] != null);
/*     */   }
/*     */ 
/*     */   private boolean containsMapping(Object paramObject1, Object paramObject2) {
/* 225 */     return (isValidKey(paramObject1)) && (maskNull(paramObject2).equals(this.vals[((Enum)paramObject1).ordinal()]));
/*     */   }
/*     */ 
/*     */   public V get(Object paramObject)
/*     */   {
/* 245 */     return isValidKey(paramObject) ? unmaskNull(this.vals[((Enum)paramObject).ordinal()]) : null;
/*     */   }
/*     */ 
/*     */   public V put(K paramK, V paramV)
/*     */   {
/* 266 */     typeCheck(paramK);
/*     */ 
/* 268 */     int i = paramK.ordinal();
/* 269 */     Object localObject = this.vals[i];
/* 270 */     this.vals[i] = maskNull(paramV);
/* 271 */     if (localObject == null)
/* 272 */       this.size += 1;
/* 273 */     return unmaskNull(localObject);
/*     */   }
/*     */ 
/*     */   public V remove(Object paramObject)
/*     */   {
/* 286 */     if (!isValidKey(paramObject))
/* 287 */       return null;
/* 288 */     int i = ((Enum)paramObject).ordinal();
/* 289 */     Object localObject = this.vals[i];
/* 290 */     this.vals[i] = null;
/* 291 */     if (localObject != null)
/* 292 */       this.size -= 1;
/* 293 */     return unmaskNull(localObject);
/*     */   }
/*     */ 
/*     */   private boolean removeMapping(Object paramObject1, Object paramObject2) {
/* 297 */     if (!isValidKey(paramObject1))
/* 298 */       return false;
/* 299 */     int i = ((Enum)paramObject1).ordinal();
/* 300 */     if (maskNull(paramObject2).equals(this.vals[i])) {
/* 301 */       this.vals[i] = null;
/* 302 */       this.size -= 1;
/* 303 */       return true;
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isValidKey(Object paramObject)
/*     */   {
/* 313 */     if (paramObject == null) {
/* 314 */       return false;
/*     */     }
/*     */ 
/* 317 */     Class localClass = paramObject.getClass();
/* 318 */     return (localClass == this.keyType) || (localClass.getSuperclass() == this.keyType);
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*     */   {
/* 333 */     if ((paramMap instanceof EnumMap)) {
/* 334 */       EnumMap localEnumMap = (EnumMap)paramMap;
/*     */ 
/* 336 */       if (localEnumMap.keyType != this.keyType) {
/* 337 */         if (localEnumMap.isEmpty())
/* 338 */           return;
/* 339 */         throw new ClassCastException(localEnumMap.keyType + " != " + this.keyType);
/*     */       }
/*     */ 
/* 342 */       for (int i = 0; i < this.keyUniverse.length; i++) {
/* 343 */         Object localObject = localEnumMap.vals[i];
/* 344 */         if (localObject != null) {
/* 345 */           if (this.vals[i] == null)
/* 346 */             this.size += 1;
/* 347 */           this.vals[i] = localObject;
/*     */         }
/*     */       }
/*     */     } else {
/* 351 */       super.putAll(paramMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 359 */     Arrays.fill(this.vals, null);
/* 360 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public Set<K> keySet()
/*     */   {
/* 382 */     Set localSet = this.keySet;
/* 383 */     if (localSet != null) {
/* 384 */       return localSet;
/*     */     }
/* 386 */     return this.keySet = new KeySet(null);
/*     */   }
/*     */ 
/*     */   public Collection<V> values()
/*     */   {
/* 420 */     Collection localCollection = this.values;
/* 421 */     if (localCollection != null) {
/* 422 */       return localCollection;
/*     */     }
/* 424 */     return this.values = new Values(null);
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 464 */     Set localSet = this.entrySet;
/* 465 */     if (localSet != null) {
/* 466 */       return localSet;
/*     */     }
/* 468 */     return this.entrySet = new EntrySet(null);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 655 */     if (this == paramObject)
/* 656 */       return true;
/* 657 */     if ((paramObject instanceof EnumMap))
/* 658 */       return equals((EnumMap)paramObject);
/* 659 */     if (!(paramObject instanceof Map)) {
/* 660 */       return false;
/*     */     }
/* 662 */     Map localMap = (Map)paramObject;
/* 663 */     if (this.size != localMap.size()) {
/* 664 */       return false;
/*     */     }
/* 666 */     for (int i = 0; i < this.keyUniverse.length; i++) {
/* 667 */       if (null != this.vals[i]) {
/* 668 */         Enum localEnum = this.keyUniverse[i];
/* 669 */         Object localObject = unmaskNull(this.vals[i]);
/* 670 */         if (null == localObject) {
/* 671 */           if ((null != localMap.get(localEnum)) || (!localMap.containsKey(localEnum)))
/* 672 */             return false;
/*     */         }
/* 674 */         else if (!localObject.equals(localMap.get(localEnum))) {
/* 675 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 680 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equals(EnumMap paramEnumMap) {
/* 684 */     if (paramEnumMap.keyType != this.keyType) {
/* 685 */       return (this.size == 0) && (paramEnumMap.size == 0);
/*     */     }
/*     */ 
/* 688 */     for (int i = 0; i < this.keyUniverse.length; i++) {
/* 689 */       Object localObject1 = this.vals[i];
/* 690 */       Object localObject2 = paramEnumMap.vals[i];
/* 691 */       if ((localObject2 != localObject1) && ((localObject2 == null) || (!localObject2.equals(localObject1))))
/*     */       {
/* 693 */         return false;
/*     */       }
/*     */     }
/* 695 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 703 */     int i = 0;
/*     */ 
/* 705 */     for (int j = 0; j < this.keyUniverse.length; j++) {
/* 706 */       if (null != this.vals[j]) {
/* 707 */         i += entryHashCode(j);
/*     */       }
/*     */     }
/*     */ 
/* 711 */     return i;
/*     */   }
/*     */ 
/*     */   private int entryHashCode(int paramInt) {
/* 715 */     return this.keyUniverse[paramInt].hashCode() ^ this.vals[paramInt].hashCode();
/*     */   }
/*     */ 
/*     */   public EnumMap<K, V> clone()
/*     */   {
/* 725 */     EnumMap localEnumMap = null;
/*     */     try {
/* 727 */       localEnumMap = (EnumMap)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 729 */       throw new AssertionError();
/*     */     }
/* 731 */     localEnumMap.vals = ((Object[])localEnumMap.vals.clone());
/* 732 */     localEnumMap.entrySet = null;
/* 733 */     return localEnumMap;
/*     */   }
/*     */ 
/*     */   private void typeCheck(K paramK)
/*     */   {
/* 740 */     Class localClass = paramK.getClass();
/* 741 */     if ((localClass != this.keyType) && (localClass.getSuperclass() != this.keyType))
/* 742 */       throw new ClassCastException(localClass + " != " + this.keyType);
/*     */   }
/*     */ 
/*     */   private static <K extends Enum<K>> K[] getKeyUniverse(Class<K> paramClass)
/*     */   {
/* 750 */     return SharedSecrets.getJavaLangAccess().getEnumConstantsShared(paramClass);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 769 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 772 */     paramObjectOutputStream.writeInt(this.size);
/*     */ 
/* 775 */     int i = this.size;
/* 776 */     for (int j = 0; i > 0; j++)
/* 777 */       if (null != this.vals[j]) {
/* 778 */         paramObjectOutputStream.writeObject(this.keyUniverse[j]);
/* 779 */         paramObjectOutputStream.writeObject(unmaskNull(this.vals[j]));
/* 780 */         i--;
/*     */       }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 793 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 795 */     this.keyUniverse = getKeyUniverse(this.keyType);
/* 796 */     this.vals = new Object[this.keyUniverse.length];
/*     */ 
/* 799 */     int i = paramObjectInputStream.readInt();
/*     */ 
/* 802 */     for (int j = 0; j < i; j++) {
/* 803 */       Enum localEnum = (Enum)paramObjectInputStream.readObject();
/* 804 */       Object localObject = paramObjectInputStream.readObject();
/* 805 */       put(localEnum, localObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EntryIterator extends EnumMap<K, V>.EnumMapIterator<Map.Entry<K, V>>
/*     */   {
/*     */     private EnumMap<K, V>.EntryIterator.Entry lastReturnedEntry;
/*     */ 
/*     */     private EntryIterator()
/*     */     {
/* 564 */       super(null);
/* 565 */       this.lastReturnedEntry = null;
/*     */     }
/*     */     public Map.Entry<K, V> next() {
/* 568 */       if (!hasNext())
/* 569 */         throw new NoSuchElementException();
/* 570 */       this.lastReturnedEntry = new Entry(this.index++, null);
/* 571 */       return this.lastReturnedEntry;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 575 */       this.lastReturnedIndex = (null == this.lastReturnedEntry ? -1 : this.lastReturnedEntry.index);
/*     */ 
/* 577 */       super.remove();
/* 578 */       this.lastReturnedEntry.index = this.lastReturnedIndex;
/* 579 */       this.lastReturnedEntry = null;
/*     */     }
/*     */ 
/*     */     private class Entry
/*     */       implements Map.Entry<K, V>
/*     */     {
/*     */       private int index;
/*     */ 
/*     */       private Entry(int arg2)
/*     */       {
/*     */         int i;
/* 586 */         this.index = i;
/*     */       }
/*     */ 
/*     */       public K getKey() {
/* 590 */         checkIndexForEntryUse();
/* 591 */         return EnumMap.this.keyUniverse[this.index];
/*     */       }
/*     */ 
/*     */       public V getValue() {
/* 595 */         checkIndexForEntryUse();
/* 596 */         return EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
/*     */       }
/*     */ 
/*     */       public V setValue(V paramV) {
/* 600 */         checkIndexForEntryUse();
/* 601 */         Object localObject = EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
/* 602 */         EnumMap.this.vals[this.index] = EnumMap.this.maskNull(paramV);
/* 603 */         return localObject;
/*     */       }
/*     */ 
/*     */       public boolean equals(Object paramObject) {
/* 607 */         if (this.index < 0) {
/* 608 */           return paramObject == this;
/*     */         }
/* 610 */         if (!(paramObject instanceof Map.Entry)) {
/* 611 */           return false;
/*     */         }
/* 613 */         Map.Entry localEntry = (Map.Entry)paramObject;
/* 614 */         Object localObject1 = EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
/* 615 */         Object localObject2 = localEntry.getValue();
/* 616 */         return (localEntry.getKey() == EnumMap.this.keyUniverse[this.index]) && ((localObject1 == localObject2) || ((localObject1 != null) && (localObject1.equals(localObject2))));
/*     */       }
/*     */ 
/*     */       public int hashCode()
/*     */       {
/* 622 */         if (this.index < 0) {
/* 623 */           return super.hashCode();
/*     */         }
/* 625 */         return EnumMap.this.entryHashCode(this.index);
/*     */       }
/*     */ 
/*     */       public String toString() {
/* 629 */         if (this.index < 0) {
/* 630 */           return super.toString();
/*     */         }
/* 632 */         return EnumMap.this.keyUniverse[this.index] + "=" + EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
/*     */       }
/*     */ 
/*     */       private void checkIndexForEntryUse()
/*     */       {
/* 637 */         if (this.index < 0)
/* 638 */           throw new IllegalStateException("Entry was removed");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EntrySet extends AbstractSet<Map.Entry<K, V>>
/*     */   {
/*     */     private EntrySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<Map.Entry<K, V>> iterator()
/*     */     {
/* 473 */       return new EnumMap.EntryIterator(EnumMap.this, null);
/*     */     }
/*     */ 
/*     */     public boolean contains(Object paramObject) {
/* 477 */       if (!(paramObject instanceof Map.Entry))
/* 478 */         return false;
/* 479 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 480 */       return EnumMap.this.containsMapping(localEntry.getKey(), localEntry.getValue());
/*     */     }
/*     */     public boolean remove(Object paramObject) {
/* 483 */       if (!(paramObject instanceof Map.Entry))
/* 484 */         return false;
/* 485 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 486 */       return EnumMap.this.removeMapping(localEntry.getKey(), localEntry.getValue());
/*     */     }
/*     */     public int size() {
/* 489 */       return EnumMap.this.size;
/*     */     }
/*     */     public void clear() {
/* 492 */       EnumMap.this.clear();
/*     */     }
/*     */     public Object[] toArray() {
/* 495 */       return fillEntryArray(new Object[EnumMap.this.size]);
/*     */     }
/*     */ 
/*     */     public <T> T[] toArray(T[] paramArrayOfT) {
/* 499 */       int i = size();
/* 500 */       if (paramArrayOfT.length < i) {
/* 501 */         paramArrayOfT = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
/*     */       }
/* 503 */       if (paramArrayOfT.length > i)
/* 504 */         paramArrayOfT[i] = null;
/* 505 */       return (Object[])fillEntryArray(paramArrayOfT);
/*     */     }
/*     */     private Object[] fillEntryArray(Object[] paramArrayOfObject) {
/* 508 */       int i = 0;
/* 509 */       for (int j = 0; j < EnumMap.this.vals.length; j++) {
/* 510 */         if (EnumMap.this.vals[j] != null)
/* 511 */           paramArrayOfObject[(i++)] = new AbstractMap.SimpleEntry(EnumMap.this.keyUniverse[j], EnumMap.this.unmaskNull(EnumMap.this.vals[j]));
/*     */       }
/* 513 */       return paramArrayOfObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private abstract class EnumMapIterator<T> implements Iterator<T>
/*     */   {
/* 519 */     int index = 0;
/*     */ 
/* 522 */     int lastReturnedIndex = -1;
/*     */ 
/*     */     private EnumMapIterator() {  } 
/* 525 */     public boolean hasNext() { while ((this.index < EnumMap.this.vals.length) && (EnumMap.this.vals[this.index] == null))
/* 526 */         this.index += 1;
/* 527 */       return this.index != EnumMap.this.vals.length; }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 531 */       checkLastReturnedIndex();
/*     */ 
/* 533 */       if (EnumMap.this.vals[this.lastReturnedIndex] != null) {
/* 534 */         EnumMap.this.vals[this.lastReturnedIndex] = null;
/* 535 */         EnumMap.access$210(EnumMap.this);
/*     */       }
/* 537 */       this.lastReturnedIndex = -1;
/*     */     }
/*     */ 
/*     */     private void checkLastReturnedIndex() {
/* 541 */       if (this.lastReturnedIndex < 0)
/* 542 */         throw new IllegalStateException(); 
/*     */     }
/*     */   }
/*     */ 
/* 546 */   private class KeyIterator extends EnumMap<K, V>.EnumMapIterator<K> { private KeyIterator() { super(null); } 
/*     */     public K next() {
/* 548 */       if (!hasNext())
/* 549 */         throw new NoSuchElementException();
/* 550 */       this.lastReturnedIndex = (this.index++);
/* 551 */       return EnumMap.this.keyUniverse[this.lastReturnedIndex];
/*     */     }
/*     */   }
/*     */ 
/*     */   private class KeySet extends AbstractSet<K>
/*     */   {
/*     */     private KeySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<K> iterator()
/*     */     {
/* 391 */       return new EnumMap.KeyIterator(EnumMap.this, null);
/*     */     }
/*     */     public int size() {
/* 394 */       return EnumMap.this.size;
/*     */     }
/*     */     public boolean contains(Object paramObject) {
/* 397 */       return EnumMap.this.containsKey(paramObject);
/*     */     }
/*     */     public boolean remove(Object paramObject) {
/* 400 */       int i = EnumMap.this.size;
/* 401 */       EnumMap.this.remove(paramObject);
/* 402 */       return EnumMap.this.size != i;
/*     */     }
/*     */     public void clear() {
/* 405 */       EnumMap.this.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ValueIterator extends EnumMap<K, V>.EnumMapIterator<V>
/*     */   {
/*     */     private ValueIterator()
/*     */     {
/* 555 */       super(null);
/*     */     }
/* 557 */     public V next() { if (!hasNext())
/* 558 */         throw new NoSuchElementException();
/* 559 */       this.lastReturnedIndex = (this.index++);
/* 560 */       return EnumMap.this.unmaskNull(EnumMap.this.vals[this.lastReturnedIndex]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Values extends AbstractCollection<V>
/*     */   {
/*     */     private Values()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<V> iterator()
/*     */     {
/* 429 */       return new EnumMap.ValueIterator(EnumMap.this, null);
/*     */     }
/*     */     public int size() {
/* 432 */       return EnumMap.this.size;
/*     */     }
/*     */     public boolean contains(Object paramObject) {
/* 435 */       return EnumMap.this.containsValue(paramObject);
/*     */     }
/*     */     public boolean remove(Object paramObject) {
/* 438 */       paramObject = EnumMap.this.maskNull(paramObject);
/*     */ 
/* 440 */       for (int i = 0; i < EnumMap.this.vals.length; i++) {
/* 441 */         if (paramObject.equals(EnumMap.this.vals[i])) {
/* 442 */           EnumMap.this.vals[i] = null;
/* 443 */           EnumMap.access$210(EnumMap.this);
/* 444 */           return true;
/*     */         }
/*     */       }
/* 447 */       return false;
/*     */     }
/*     */     public void clear() {
/* 450 */       EnumMap.this.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.EnumMap
 * JD-Core Version:    0.6.2
 */