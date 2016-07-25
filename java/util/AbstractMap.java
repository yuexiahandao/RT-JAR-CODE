/*     */ package java.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class AbstractMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/* 299 */   volatile transient Set<K> keySet = null;
/* 300 */   volatile transient Collection<V> values = null;
/*     */ 
/*     */   public int size()
/*     */   {
/*  84 */     return entrySet().size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  93 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 109 */     Iterator localIterator = entrySet().iterator();
/*     */     Map.Entry localEntry;
/* 110 */     if (paramObject == null) {
/* 111 */       while (localIterator.hasNext()) {
/* 112 */         localEntry = (Map.Entry)localIterator.next();
/* 113 */         if (localEntry.getValue() == null)
/* 114 */           return true;
/*     */       }
/*     */     }
/* 117 */     while (localIterator.hasNext()) {
/* 118 */       localEntry = (Map.Entry)localIterator.next();
/* 119 */       if (paramObject.equals(localEntry.getValue())) {
/* 120 */         return true;
/*     */       }
/*     */     }
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 140 */     Iterator localIterator = entrySet().iterator();
/*     */     Map.Entry localEntry;
/* 141 */     if (paramObject == null) {
/* 142 */       while (localIterator.hasNext()) {
/* 143 */         localEntry = (Map.Entry)localIterator.next();
/* 144 */         if (localEntry.getKey() == null)
/* 145 */           return true;
/*     */       }
/*     */     }
/* 148 */     while (localIterator.hasNext()) {
/* 149 */       localEntry = (Map.Entry)localIterator.next();
/* 150 */       if (paramObject.equals(localEntry.getKey())) {
/* 151 */         return true;
/*     */       }
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public V get(Object paramObject)
/*     */   {
/* 171 */     Iterator localIterator = entrySet().iterator();
/*     */     Map.Entry localEntry;
/* 172 */     if (paramObject == null) {
/* 173 */       while (localIterator.hasNext()) {
/* 174 */         localEntry = (Map.Entry)localIterator.next();
/* 175 */         if (localEntry.getKey() == null)
/* 176 */           return localEntry.getValue();
/*     */       }
/*     */     }
/* 179 */     while (localIterator.hasNext()) {
/* 180 */       localEntry = (Map.Entry)localIterator.next();
/* 181 */       if (paramObject.equals(localEntry.getKey())) {
/* 182 */         return localEntry.getValue();
/*     */       }
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */   public V put(K paramK, V paramV)
/*     */   {
/* 203 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public V remove(Object paramObject)
/*     */   {
/* 228 */     Iterator localIterator = entrySet().iterator();
/* 229 */     Object localObject1 = null;
/* 230 */     if (paramObject == null) {
/* 231 */       while ((localObject1 == null) && (localIterator.hasNext())) {
/* 232 */         localObject2 = (Map.Entry)localIterator.next();
/* 233 */         if (((Map.Entry)localObject2).getKey() == null)
/* 234 */           localObject1 = localObject2;
/*     */       }
/*     */     }
/* 237 */     while ((localObject1 == null) && (localIterator.hasNext())) {
/* 238 */       localObject2 = (Map.Entry)localIterator.next();
/* 239 */       if (paramObject.equals(((Map.Entry)localObject2).getKey())) {
/* 240 */         localObject1 = localObject2;
/*     */       }
/*     */     }
/*     */ 
/* 244 */     Object localObject2 = null;
/* 245 */     if (localObject1 != null) {
/* 246 */       localObject2 = localObject1.getValue();
/* 247 */       localIterator.remove();
/*     */     }
/* 249 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends K, ? extends V> paramMap)
/*     */   {
/* 272 */     for (Map.Entry localEntry : paramMap.entrySet())
/* 273 */       put(localEntry.getKey(), localEntry.getValue());
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 288 */     entrySet().clear();
/*     */   }
/*     */ 
/*     */   public Set<K> keySet()
/*     */   {
/* 318 */     if (this.keySet == null) {
/* 319 */       this.keySet = new AbstractSet() {
/*     */         public Iterator<K> iterator() {
/* 321 */           return new Iterator() {
/* 322 */             private Iterator<Map.Entry<K, V>> i = AbstractMap.this.entrySet().iterator();
/*     */ 
/*     */             public boolean hasNext() {
/* 325 */               return this.i.hasNext();
/*     */             }
/*     */ 
/*     */             public K next() {
/* 329 */               return ((Map.Entry)this.i.next()).getKey();
/*     */             }
/*     */ 
/*     */             public void remove() {
/* 333 */               this.i.remove();
/*     */             }
/*     */           };
/*     */         }
/*     */ 
/*     */         public int size() {
/* 339 */           return AbstractMap.this.size();
/*     */         }
/*     */ 
/*     */         public boolean isEmpty() {
/* 343 */           return AbstractMap.this.isEmpty();
/*     */         }
/*     */ 
/*     */         public void clear() {
/* 347 */           AbstractMap.this.clear();
/*     */         }
/*     */ 
/*     */         public boolean contains(Object paramAnonymousObject) {
/* 351 */           return AbstractMap.this.containsKey(paramAnonymousObject);
/*     */         }
/*     */       };
/*     */     }
/* 355 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   public Collection<V> values()
/*     */   {
/* 374 */     if (this.values == null) {
/* 375 */       this.values = new AbstractCollection() {
/*     */         public Iterator<V> iterator() {
/* 377 */           return new Iterator() {
/* 378 */             private Iterator<Map.Entry<K, V>> i = AbstractMap.this.entrySet().iterator();
/*     */ 
/*     */             public boolean hasNext() {
/* 381 */               return this.i.hasNext();
/*     */             }
/*     */ 
/*     */             public V next() {
/* 385 */               return ((Map.Entry)this.i.next()).getValue();
/*     */             }
/*     */ 
/*     */             public void remove() {
/* 389 */               this.i.remove();
/*     */             }
/*     */           };
/*     */         }
/*     */ 
/*     */         public int size() {
/* 395 */           return AbstractMap.this.size();
/*     */         }
/*     */ 
/*     */         public boolean isEmpty() {
/* 399 */           return AbstractMap.this.isEmpty();
/*     */         }
/*     */ 
/*     */         public void clear() {
/* 403 */           AbstractMap.this.clear();
/*     */         }
/*     */ 
/*     */         public boolean contains(Object paramAnonymousObject) {
/* 407 */           return AbstractMap.this.containsValue(paramAnonymousObject);
/*     */         }
/*     */       };
/*     */     }
/* 411 */     return this.values;
/*     */   }
/*     */ 
/*     */   public abstract Set<Map.Entry<K, V>> entrySet();
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 441 */     if (paramObject == this) {
/* 442 */       return true;
/*     */     }
/* 444 */     if (!(paramObject instanceof Map))
/* 445 */       return false;
/* 446 */     Map localMap = (Map)paramObject;
/* 447 */     if (localMap.size() != size())
/* 448 */       return false;
/*     */     try
/*     */     {
/* 451 */       Iterator localIterator = entrySet().iterator();
/* 452 */       while (localIterator.hasNext()) {
/* 453 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 454 */         Object localObject1 = localEntry.getKey();
/* 455 */         Object localObject2 = localEntry.getValue();
/* 456 */         if (localObject2 == null) {
/* 457 */           if ((localMap.get(localObject1) != null) || (!localMap.containsKey(localObject1)))
/* 458 */             return false;
/*     */         }
/* 460 */         else if (!localObject2.equals(localMap.get(localObject1)))
/* 461 */           return false;
/*     */       }
/*     */     }
/*     */     catch (ClassCastException localClassCastException) {
/* 465 */       return false;
/*     */     } catch (NullPointerException localNullPointerException) {
/* 467 */       return false;
/*     */     }
/*     */ 
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 491 */     int i = 0;
/* 492 */     Iterator localIterator = entrySet().iterator();
/* 493 */     while (localIterator.hasNext())
/* 494 */       i += ((Map.Entry)localIterator.next()).hashCode();
/* 495 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 511 */     Iterator localIterator = entrySet().iterator();
/* 512 */     if (!localIterator.hasNext()) {
/* 513 */       return "{}";
/*     */     }
/* 515 */     StringBuilder localStringBuilder = new StringBuilder();
/* 516 */     localStringBuilder.append('{');
/*     */     while (true) {
/* 518 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 519 */       Object localObject1 = localEntry.getKey();
/* 520 */       Object localObject2 = localEntry.getValue();
/* 521 */       localStringBuilder.append(localObject1 == this ? "(this Map)" : localObject1);
/* 522 */       localStringBuilder.append('=');
/* 523 */       localStringBuilder.append(localObject2 == this ? "(this Map)" : localObject2);
/* 524 */       if (!localIterator.hasNext())
/* 525 */         return '}';
/* 526 */       localStringBuilder.append(',').append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 537 */     AbstractMap localAbstractMap = (AbstractMap)super.clone();
/* 538 */     localAbstractMap.keySet = null;
/* 539 */     localAbstractMap.values = null;
/* 540 */     return localAbstractMap;
/*     */   }
/*     */ 
/*     */   private static boolean eq(Object paramObject1, Object paramObject2)
/*     */   {
/* 548 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   public static class SimpleEntry<K, V>
/*     */     implements Map.Entry<K, V>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8499721149061103585L;
/*     */     private final K key;
/*     */     private V value;
/*     */ 
/*     */     public SimpleEntry(K paramK, V paramV)
/*     */     {
/* 585 */       this.key = paramK;
/* 586 */       this.value = paramV;
/*     */     }
/*     */ 
/*     */     public SimpleEntry(Map.Entry<? extends K, ? extends V> paramEntry)
/*     */     {
/* 596 */       this.key = paramEntry.getKey();
/* 597 */       this.value = paramEntry.getValue();
/*     */     }
/*     */ 
/*     */     public K getKey()
/*     */     {
/* 606 */       return this.key;
/*     */     }
/*     */ 
/*     */     public V getValue()
/*     */     {
/* 615 */       return this.value;
/*     */     }
/*     */ 
/*     */     public V setValue(V paramV)
/*     */     {
/* 626 */       Object localObject = this.value;
/* 627 */       this.value = paramV;
/* 628 */       return localObject;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 653 */       if (!(paramObject instanceof Map.Entry))
/* 654 */         return false;
/* 655 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 656 */       return (AbstractMap.eq(this.key, localEntry.getKey())) && (AbstractMap.eq(this.value, localEntry.getValue()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 673 */       return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 686 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SimpleImmutableEntry<K, V>
/*     */     implements Map.Entry<K, V>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 7138329143949025153L;
/*     */     private final K key;
/*     */     private final V value;
/*     */ 
/*     */     public SimpleImmutableEntry(K paramK, V paramV)
/*     */     {
/* 715 */       this.key = paramK;
/* 716 */       this.value = paramV;
/*     */     }
/*     */ 
/*     */     public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> paramEntry)
/*     */     {
/* 726 */       this.key = paramEntry.getKey();
/* 727 */       this.value = paramEntry.getValue();
/*     */     }
/*     */ 
/*     */     public K getKey()
/*     */     {
/* 736 */       return this.key;
/*     */     }
/*     */ 
/*     */     public V getValue()
/*     */     {
/* 745 */       return this.value;
/*     */     }
/*     */ 
/*     */     public V setValue(V paramV)
/*     */     {
/* 759 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 784 */       if (!(paramObject instanceof Map.Entry))
/* 785 */         return false;
/* 786 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 787 */       return (AbstractMap.eq(this.key, localEntry.getKey())) && (AbstractMap.eq(this.value, localEntry.getValue()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 804 */       return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 817 */       return this.key + "=" + this.value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.AbstractMap
 * JD-Core Version:    0.6.2
 */