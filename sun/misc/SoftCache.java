/*     */ package sun.misc;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SoftCache extends AbstractMap
/*     */   implements Map
/*     */ {
/*     */   private Map hash;
/* 160 */   private ReferenceQueue queue = new ReferenceQueue();
/*     */ 
/* 449 */   private Set entrySet = null;
/*     */ 
/*     */   private void processQueue()
/*     */   {
/*     */     ValueCell localValueCell;
/* 170 */     while ((localValueCell = (ValueCell)this.queue.poll()) != null)
/* 171 */       if (localValueCell.isValid()) this.hash.remove(localValueCell.key); else
/* 172 */         ValueCell.access$210();
/*     */   }
/*     */ 
/*     */   public SoftCache(int paramInt, float paramFloat)
/*     */   {
/* 192 */     this.hash = new HashMap(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public SoftCache(int paramInt)
/*     */   {
/* 205 */     this.hash = new HashMap(paramInt);
/*     */   }
/*     */ 
/*     */   public SoftCache()
/*     */   {
/* 213 */     this.hash = new HashMap();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 224 */     return entrySet().size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 231 */     return entrySet().isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 242 */     return ValueCell.strip(this.hash.get(paramObject), false) != null;
/*     */   }
/*     */ 
/*     */   protected Object fill(Object paramObject)
/*     */   {
/* 266 */     return null;
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 286 */     processQueue();
/* 287 */     Object localObject = this.hash.get(paramObject);
/* 288 */     if (localObject == null) {
/* 289 */       localObject = fill(paramObject);
/* 290 */       if (localObject != null) {
/* 291 */         this.hash.put(paramObject, ValueCell.create(paramObject, localObject, this.queue));
/* 292 */         return localObject;
/*     */       }
/*     */     }
/* 295 */     return ValueCell.strip(localObject, false);
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 313 */     processQueue();
/* 314 */     ValueCell localValueCell = ValueCell.create(paramObject1, paramObject2, this.queue);
/* 315 */     return ValueCell.strip(this.hash.put(paramObject1, localValueCell), true);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 328 */     processQueue();
/* 329 */     return ValueCell.strip(this.hash.remove(paramObject), true);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 336 */     processQueue();
/* 337 */     this.hash.clear();
/*     */   }
/*     */ 
/*     */   private static boolean valEquals(Object paramObject1, Object paramObject2)
/*     */   {
/* 344 */     return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
/*     */   }
/*     */ 
/*     */   public Set entrySet()
/*     */   {
/* 455 */     if (this.entrySet == null) this.entrySet = new EntrySet(null);
/* 456 */     return this.entrySet;
/*     */   }
/*     */ 
/*     */   private class Entry
/*     */     implements Map.Entry
/*     */   {
/*     */     private Map.Entry ent;
/*     */     private Object value;
/*     */ 
/*     */     Entry(Map.Entry paramObject, Object arg3)
/*     */     {
/* 358 */       this.ent = paramObject;
/*     */       Object localObject;
/* 359 */       this.value = localObject;
/*     */     }
/*     */ 
/*     */     public Object getKey() {
/* 363 */       return this.ent.getKey();
/*     */     }
/*     */ 
/*     */     public Object getValue() {
/* 367 */       return this.value;
/*     */     }
/*     */ 
/*     */     public Object setValue(Object paramObject) {
/* 371 */       return this.ent.setValue(SoftCache.ValueCell.access$400(this.ent.getKey(), paramObject, SoftCache.this.queue));
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 375 */       if (!(paramObject instanceof Map.Entry)) return false;
/* 376 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 377 */       return (SoftCache.valEquals(this.ent.getKey(), localEntry.getKey())) && (SoftCache.valEquals(this.value, localEntry.getValue()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/*     */       Object localObject;
/* 383 */       return ((localObject = getKey()) == null ? 0 : localObject.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EntrySet extends AbstractSet
/*     */   {
/* 392 */     Set hashEntries = SoftCache.this.hash.entrySet();
/*     */ 
/*     */     private EntrySet() {
/*     */     }
/* 396 */     public Iterator iterator() { return new Iterator() {
/* 397 */         Iterator hashIterator = SoftCache.EntrySet.this.hashEntries.iterator();
/* 398 */         SoftCache.Entry next = null;
/*     */ 
/*     */         public boolean hasNext() {
/* 401 */           while (this.hashIterator.hasNext()) {
/* 402 */             Map.Entry localEntry = (Map.Entry)this.hashIterator.next();
/* 403 */             SoftCache.ValueCell localValueCell = (SoftCache.ValueCell)localEntry.getValue();
/* 404 */             Object localObject = null;
/* 405 */             if ((localValueCell == null) || ((localObject = localValueCell.get()) != null))
/*     */             {
/* 409 */               this.next = new SoftCache.Entry(SoftCache.this, localEntry, localObject);
/* 410 */               return true;
/*     */             }
/*     */           }
/* 412 */           return false;
/*     */         }
/*     */ 
/*     */         public Object next() {
/* 416 */           if ((this.next == null) && (!hasNext()))
/* 417 */             throw new NoSuchElementException();
/* 418 */           SoftCache.Entry localEntry = this.next;
/* 419 */           this.next = null;
/* 420 */           return localEntry;
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 424 */           this.hashIterator.remove();
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 431 */       return !iterator().hasNext();
/*     */     }
/*     */ 
/*     */     public int size() {
/* 435 */       int i = 0;
/* 436 */       for (Iterator localIterator = iterator(); localIterator.hasNext(); localIterator.next()) i++;
/* 437 */       return i;
/*     */     }
/*     */ 
/*     */     public boolean remove(Object paramObject) {
/* 441 */       SoftCache.this.processQueue();
/* 442 */       if ((paramObject instanceof SoftCache.Entry)) return this.hashEntries.remove(((SoftCache.Entry)paramObject).ent);
/* 443 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ValueCell extends SoftReference
/*     */   {
/* 119 */     private static Object INVALID_KEY = new Object();
/* 120 */     private static int dropped = 0;
/*     */     private Object key;
/*     */ 
/*     */     private ValueCell(Object paramObject1, Object paramObject2, ReferenceQueue paramReferenceQueue)
/*     */     {
/* 124 */       super(paramReferenceQueue);
/* 125 */       this.key = paramObject1;
/*     */     }
/*     */ 
/*     */     private static ValueCell create(Object paramObject1, Object paramObject2, ReferenceQueue paramReferenceQueue)
/*     */     {
/* 131 */       if (paramObject2 == null) return null;
/* 132 */       return new ValueCell(paramObject1, paramObject2, paramReferenceQueue);
/*     */     }
/*     */ 
/*     */     private static Object strip(Object paramObject, boolean paramBoolean) {
/* 136 */       if (paramObject == null) return null;
/* 137 */       ValueCell localValueCell = (ValueCell)paramObject;
/* 138 */       Object localObject = localValueCell.get();
/* 139 */       if (paramBoolean) localValueCell.drop();
/* 140 */       return localObject;
/*     */     }
/*     */ 
/*     */     private boolean isValid() {
/* 144 */       return this.key != INVALID_KEY;
/*     */     }
/*     */ 
/*     */     private void drop() {
/* 148 */       super.clear();
/* 149 */       this.key = INVALID_KEY;
/* 150 */       dropped += 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.SoftCache
 * JD-Core Version:    0.6.2
 */