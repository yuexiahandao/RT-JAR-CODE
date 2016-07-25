/*     */ package sun.security.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ class MemoryCache extends Cache
/*     */ {
/*     */   private static final float LOAD_FACTOR = 0.75F;
/*     */   private static final boolean DEBUG = false;
/*     */   private final Map<Object, CacheEntry> cacheMap;
/*     */   private int maxSize;
/*     */   private long lifetime;
/*     */   private final ReferenceQueue queue;
/*     */ 
/*     */   public MemoryCache(boolean paramBoolean, int paramInt)
/*     */   {
/* 257 */     this(paramBoolean, paramInt, 0);
/*     */   }
/*     */ 
/*     */   public MemoryCache(boolean paramBoolean, int paramInt1, int paramInt2) {
/* 261 */     this.maxSize = paramInt1;
/* 262 */     this.lifetime = (paramInt2 * 1000);
/* 263 */     this.queue = (paramBoolean ? new ReferenceQueue() : null);
/* 264 */     int i = (int)(paramInt1 / 0.75F) + 1;
/* 265 */     this.cacheMap = new LinkedHashMap(i, 0.75F, true);
/*     */   }
/*     */ 
/*     */   private void emptyQueue()
/*     */   {
/* 277 */     if (this.queue == null) {
/* 278 */       return;
/*     */     }
/* 280 */     int i = this.cacheMap.size();
/*     */     while (true) {
/* 282 */       CacheEntry localCacheEntry1 = (CacheEntry)this.queue.poll();
/* 283 */       if (localCacheEntry1 == null) {
/*     */         break;
/*     */       }
/* 286 */       Object localObject = localCacheEntry1.getKey();
/* 287 */       if (localObject != null)
/*     */       {
/* 291 */         CacheEntry localCacheEntry2 = (CacheEntry)this.cacheMap.remove(localObject);
/*     */ 
/* 294 */         if ((localCacheEntry2 != null) && (localCacheEntry1 != localCacheEntry2))
/* 295 */           this.cacheMap.put(localObject, localCacheEntry2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void expungeExpiredEntries()
/*     */   {
/* 311 */     emptyQueue();
/* 312 */     if (this.lifetime == 0L) {
/* 313 */       return;
/*     */     }
/* 315 */     int i = 0;
/* 316 */     long l = System.currentTimeMillis();
/* 317 */     Iterator localIterator = this.cacheMap.values().iterator();
/* 318 */     while (localIterator.hasNext()) {
/* 319 */       CacheEntry localCacheEntry = (CacheEntry)localIterator.next();
/* 320 */       if (!localCacheEntry.isValid(l)) {
/* 321 */         localIterator.remove();
/* 322 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized int size()
/*     */   {
/* 334 */     expungeExpiredEntries();
/* 335 */     return this.cacheMap.size();
/*     */   }
/*     */ 
/*     */   public synchronized void clear() {
/* 339 */     if (this.queue != null)
/*     */     {
/* 342 */       for (CacheEntry localCacheEntry : this.cacheMap.values()) {
/* 343 */         localCacheEntry.invalidate();
/*     */       }
/*     */ 
/* 345 */       while (this.queue.poll() != null);
/*     */     }
/*     */ 
/* 349 */     this.cacheMap.clear();
/*     */   }
/*     */ 
/*     */   public synchronized void put(Object paramObject1, Object paramObject2) {
/* 353 */     emptyQueue();
/* 354 */     long l = this.lifetime == 0L ? 0L : System.currentTimeMillis() + this.lifetime;
/*     */ 
/* 356 */     CacheEntry localCacheEntry1 = newEntry(paramObject1, paramObject2, l, this.queue);
/* 357 */     CacheEntry localCacheEntry2 = (CacheEntry)this.cacheMap.put(paramObject1, localCacheEntry1);
/* 358 */     if (localCacheEntry2 != null) {
/* 359 */       localCacheEntry2.invalidate();
/* 360 */       return;
/*     */     }
/* 362 */     if ((this.maxSize > 0) && (this.cacheMap.size() > this.maxSize)) {
/* 363 */       expungeExpiredEntries();
/* 364 */       if (this.cacheMap.size() > this.maxSize) {
/* 365 */         Iterator localIterator = this.cacheMap.values().iterator();
/* 366 */         CacheEntry localCacheEntry3 = (CacheEntry)localIterator.next();
/*     */ 
/* 371 */         localIterator.remove();
/* 372 */         localCacheEntry3.invalidate();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized Object get(Object paramObject) {
/* 378 */     emptyQueue();
/* 379 */     CacheEntry localCacheEntry = (CacheEntry)this.cacheMap.get(paramObject);
/* 380 */     if (localCacheEntry == null) {
/* 381 */       return null;
/*     */     }
/* 383 */     long l = this.lifetime == 0L ? 0L : System.currentTimeMillis();
/* 384 */     if (!localCacheEntry.isValid(l))
/*     */     {
/* 388 */       this.cacheMap.remove(paramObject);
/* 389 */       return null;
/*     */     }
/* 391 */     return localCacheEntry.getValue();
/*     */   }
/*     */ 
/*     */   public synchronized void remove(Object paramObject) {
/* 395 */     emptyQueue();
/* 396 */     CacheEntry localCacheEntry = (CacheEntry)this.cacheMap.remove(paramObject);
/* 397 */     if (localCacheEntry != null)
/* 398 */       localCacheEntry.invalidate();
/*     */   }
/*     */ 
/*     */   public synchronized void setCapacity(int paramInt)
/*     */   {
/* 403 */     expungeExpiredEntries();
/* 404 */     if ((paramInt > 0) && (this.cacheMap.size() > paramInt)) {
/* 405 */       Iterator localIterator = this.cacheMap.values().iterator();
/* 406 */       for (int i = this.cacheMap.size() - paramInt; i > 0; i--) {
/* 407 */         CacheEntry localCacheEntry = (CacheEntry)localIterator.next();
/*     */ 
/* 412 */         localIterator.remove();
/* 413 */         localCacheEntry.invalidate();
/*     */       }
/*     */     }
/*     */ 
/* 417 */     this.maxSize = (paramInt > 0 ? paramInt : 0);
/*     */   }
/*     */ 
/*     */   public synchronized void setTimeout(int paramInt)
/*     */   {
/* 425 */     emptyQueue();
/* 426 */     this.lifetime = (paramInt > 0 ? paramInt * 1000L : 0L);
/*     */   }
/*     */ 
/*     */   public synchronized void accept(Cache.CacheVisitor paramCacheVisitor)
/*     */   {
/* 435 */     expungeExpiredEntries();
/* 436 */     Map localMap = getCachedEntries();
/*     */ 
/* 438 */     paramCacheVisitor.visit(localMap);
/*     */   }
/*     */ 
/*     */   private Map<Object, Object> getCachedEntries() {
/* 442 */     HashMap localHashMap = new HashMap(this.cacheMap.size());
/*     */ 
/* 444 */     for (CacheEntry localCacheEntry : this.cacheMap.values()) {
/* 445 */       localHashMap.put(localCacheEntry.getKey(), localCacheEntry.getValue());
/*     */     }
/*     */ 
/* 448 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   protected CacheEntry newEntry(Object paramObject1, Object paramObject2, long paramLong, ReferenceQueue paramReferenceQueue)
/*     */   {
/* 453 */     if (paramReferenceQueue != null) {
/* 454 */       return new SoftCacheEntry(paramObject1, paramObject2, paramLong, paramReferenceQueue);
/*     */     }
/* 456 */     return new HardCacheEntry(paramObject1, paramObject2, paramLong);
/*     */   }
/*     */ 
/*     */   private static abstract interface CacheEntry
/*     */   {
/*     */     public abstract boolean isValid(long paramLong);
/*     */ 
/*     */     public abstract void invalidate();
/*     */ 
/*     */     public abstract Object getKey();
/*     */ 
/*     */     public abstract Object getValue();
/*     */   }
/*     */ 
/*     */   private static class HardCacheEntry implements MemoryCache.CacheEntry {
/*     */     private Object key;
/*     */     private Object value;
/*     */     private long expirationTime;
/*     */ 
/*     */     HardCacheEntry(Object paramObject1, Object paramObject2, long paramLong) {
/* 478 */       this.key = paramObject1;
/* 479 */       this.value = paramObject2;
/* 480 */       this.expirationTime = paramLong;
/*     */     }
/*     */ 
/*     */     public Object getKey() {
/* 484 */       return this.key;
/*     */     }
/*     */ 
/*     */     public Object getValue() {
/* 488 */       return this.value;
/*     */     }
/*     */ 
/*     */     public boolean isValid(long paramLong) {
/* 492 */       boolean bool = paramLong <= this.expirationTime;
/* 493 */       if (!bool) {
/* 494 */         invalidate();
/*     */       }
/* 496 */       return bool;
/*     */     }
/*     */ 
/*     */     public void invalidate() {
/* 500 */       this.key = null;
/* 501 */       this.value = null;
/* 502 */       this.expirationTime = -1L;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SoftCacheEntry extends SoftReference implements MemoryCache.CacheEntry
/*     */   {
/*     */     private Object key;
/*     */     private long expirationTime;
/*     */ 
/*     */     SoftCacheEntry(Object paramObject1, Object paramObject2, long paramLong, ReferenceQueue paramReferenceQueue)
/*     */     {
/* 514 */       super(paramReferenceQueue);
/* 515 */       this.key = paramObject1;
/* 516 */       this.expirationTime = paramLong;
/*     */     }
/*     */ 
/*     */     public Object getKey() {
/* 520 */       return this.key;
/*     */     }
/*     */ 
/*     */     public Object getValue() {
/* 524 */       return get();
/*     */     }
/*     */ 
/*     */     public boolean isValid(long paramLong) {
/* 528 */       boolean bool = (paramLong <= this.expirationTime) && (get() != null);
/* 529 */       if (!bool) {
/* 530 */         invalidate();
/*     */       }
/* 532 */       return bool;
/*     */     }
/*     */ 
/*     */     public void invalidate() {
/* 536 */       clear();
/* 537 */       this.key = null;
/* 538 */       this.expirationTime = -1L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.MemoryCache
 * JD-Core Version:    0.6.2
 */