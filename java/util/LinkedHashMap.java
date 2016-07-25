/*     */ package java.util;
/*     */ 
/*     */ public class LinkedHashMap<K, V> extends HashMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 3801124242820219131L;
/*     */   private transient Entry<K, V> header;
/*     */   private final boolean accessOrder;
/*     */ 
/*     */   public LinkedHashMap(int paramInt, float paramFloat)
/*     */   {
/* 177 */     super(paramInt, paramFloat);
/* 178 */     this.accessOrder = false;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap(int paramInt)
/*     */   {
/* 189 */     super(paramInt);
/* 190 */     this.accessOrder = false;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap()
/*     */   {
/* 199 */     this.accessOrder = false;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap(Map<? extends K, ? extends V> paramMap)
/*     */   {
/* 212 */     super(paramMap);
/* 213 */     this.accessOrder = false;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap(int paramInt, float paramFloat, boolean paramBoolean)
/*     */   {
/* 230 */     super(paramInt, paramFloat);
/* 231 */     this.accessOrder = paramBoolean;
/*     */   }
/*     */ 
/*     */   void init()
/*     */   {
/* 241 */     this.header = new Entry(-1, null, null, null);
/* 242 */     this.header.before = (this.header.after = this.header);
/*     */   }
/*     */ 
/*     */   void transfer(HashMap.Entry[] paramArrayOfEntry, boolean paramBoolean)
/*     */   {
/* 252 */     int i = paramArrayOfEntry.length;
/* 253 */     for (Entry localEntry = this.header.after; localEntry != this.header; localEntry = localEntry.after) {
/* 254 */       if (paramBoolean)
/* 255 */         localEntry.hash = (localEntry.key == null ? 0 : hash(localEntry.key));
/* 256 */       int j = indexFor(localEntry.hash, i);
/* 257 */       localEntry.next = paramArrayOfEntry[j];
/* 258 */       paramArrayOfEntry[j] = localEntry;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/*     */     Entry localEntry;
/* 273 */     if (paramObject == null)
/* 274 */       for (localEntry = this.header.after; localEntry != this.header; localEntry = localEntry.after)
/* 275 */         if (localEntry.value == null)
/* 276 */           return true;
/*     */     else {
/* 278 */       for (localEntry = this.header.after; localEntry != this.header; localEntry = localEntry.after)
/* 279 */         if (paramObject.equals(localEntry.value))
/* 280 */           return true;
/*     */     }
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   public V get(Object paramObject)
/*     */   {
/* 301 */     Entry localEntry = (Entry)getEntry(paramObject);
/* 302 */     if (localEntry == null)
/* 303 */       return null;
/* 304 */     localEntry.recordAccess(this);
/* 305 */     return localEntry.value;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 313 */     super.clear();
/* 314 */     this.header.before = (this.header.after = this.header);
/*     */   }
/*     */ 
/*     */   Iterator<K> newKeyIterator()
/*     */   {
/* 417 */     return new KeyIterator(null); } 
/* 418 */   Iterator<V> newValueIterator() { return new ValueIterator(null); } 
/* 419 */   Iterator<Map.Entry<K, V>> newEntryIterator() { return new EntryIterator(null); }
/*     */ 
/*     */ 
/*     */   void addEntry(int paramInt1, K paramK, V paramV, int paramInt2)
/*     */   {
/* 427 */     super.addEntry(paramInt1, paramK, paramV, paramInt2);
/*     */ 
/* 430 */     Entry localEntry = this.header.after;
/* 431 */     if (removeEldestEntry(localEntry))
/* 432 */       removeEntryForKey(localEntry.key);
/*     */   }
/*     */ 
/*     */   void createEntry(int paramInt1, K paramK, V paramV, int paramInt2)
/*     */   {
/* 441 */     HashMap.Entry localEntry = this.table[paramInt2];
/* 442 */     Entry localEntry1 = new Entry(paramInt1, paramK, paramV, localEntry);
/* 443 */     this.table[paramInt2] = localEntry1;
/* 444 */     localEntry1.addBefore(this.header);
/* 445 */     this.size += 1;
/*     */   }
/*     */ 
/*     */   protected boolean removeEldestEntry(Map.Entry<K, V> paramEntry)
/*     */   {
/* 490 */     return false;
/*     */   }
/*     */ 
/*     */   private static class Entry<K, V> extends HashMap.Entry<K, V>
/*     */   {
/*     */     Entry<K, V> before;
/*     */     Entry<K, V> after;
/*     */ 
/*     */     Entry(int paramInt, K paramK, V paramV, HashMap.Entry<K, V> paramEntry)
/*     */     {
/* 325 */       super(paramK, paramV, paramEntry);
/*     */     }
/*     */ 
/*     */     private void remove()
/*     */     {
/* 332 */       this.before.after = this.after;
/* 333 */       this.after.before = this.before;
/*     */     }
/*     */ 
/*     */     private void addBefore(Entry<K, V> paramEntry)
/*     */     {
/* 340 */       this.after = paramEntry;
/* 341 */       this.before = paramEntry.before;
/* 342 */       this.before.after = this;
/* 343 */       this.after.before = this;
/*     */     }
/*     */ 
/*     */     void recordAccess(HashMap<K, V> paramHashMap)
/*     */     {
/* 353 */       LinkedHashMap localLinkedHashMap = (LinkedHashMap)paramHashMap;
/* 354 */       if (localLinkedHashMap.accessOrder) {
/* 355 */         localLinkedHashMap.modCount += 1;
/* 356 */         remove();
/* 357 */         addBefore(localLinkedHashMap.header);
/*     */       }
/*     */     }
/*     */ 
/*     */     void recordRemoval(HashMap<K, V> paramHashMap) {
/* 362 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EntryIterator extends LinkedHashMap<K, V>.LinkedHashIterator<Map.Entry<K, V>>
/*     */   {
/*     */     private EntryIterator()
/*     */     {
/* 412 */       super(null); } 
/* 413 */     public Map.Entry<K, V> next() { return nextEntry(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   private class KeyIterator extends LinkedHashMap<K, V>.LinkedHashIterator<K>
/*     */   {
/*     */     private KeyIterator()
/*     */     {
/* 404 */       super(null); } 
/* 405 */     public K next() { return nextEntry().getKey(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   private abstract class LinkedHashIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/* 367 */     LinkedHashMap.Entry<K, V> nextEntry = LinkedHashMap.this.header.after;
/* 368 */     LinkedHashMap.Entry<K, V> lastReturned = null;
/*     */ 
/* 375 */     int expectedModCount = LinkedHashMap.this.modCount;
/*     */ 
/*     */     private LinkedHashIterator() {  } 
/* 378 */     public boolean hasNext() { return this.nextEntry != LinkedHashMap.this.header; }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 382 */       if (this.lastReturned == null)
/* 383 */         throw new IllegalStateException();
/* 384 */       if (LinkedHashMap.this.modCount != this.expectedModCount) {
/* 385 */         throw new ConcurrentModificationException();
/*     */       }
/* 387 */       LinkedHashMap.this.remove(this.lastReturned.key);
/* 388 */       this.lastReturned = null;
/* 389 */       this.expectedModCount = LinkedHashMap.this.modCount;
/*     */     }
/*     */ 
/*     */     LinkedHashMap.Entry<K, V> nextEntry() {
/* 393 */       if (LinkedHashMap.this.modCount != this.expectedModCount)
/* 394 */         throw new ConcurrentModificationException();
/* 395 */       if (this.nextEntry == LinkedHashMap.this.header) {
/* 396 */         throw new NoSuchElementException();
/*     */       }
/* 398 */       LinkedHashMap.Entry localEntry = this.lastReturned = this.nextEntry;
/* 399 */       this.nextEntry = localEntry.after;
/* 400 */       return localEntry;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ValueIterator extends LinkedHashMap<K, V>.LinkedHashIterator<V>
/*     */   {
/*     */     private ValueIterator()
/*     */     {
/* 408 */       super(null); } 
/* 409 */     public V next() { return nextEntry().value; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.LinkedHashMap
 * JD-Core Version:    0.6.2
 */