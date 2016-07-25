/*     */ package sun.util.locale;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ public abstract class LocaleObjectCache<K, V>
/*     */ {
/*     */   private ConcurrentMap<K, CacheEntry<K, V>> map;
/*  41 */   private ReferenceQueue<V> queue = new ReferenceQueue();
/*     */ 
/*     */   public LocaleObjectCache() {
/*  44 */     this(16, 0.75F, 16);
/*     */   }
/*     */ 
/*     */   public LocaleObjectCache(int paramInt1, float paramFloat, int paramInt2) {
/*  48 */     this.map = new ConcurrentHashMap(paramInt1, paramFloat, paramInt2);
/*     */   }
/*     */ 
/*     */   public V get(K paramK) {
/*  52 */     Object localObject1 = null;
/*     */ 
/*  54 */     cleanStaleEntries();
/*  55 */     CacheEntry localCacheEntry1 = (CacheEntry)this.map.get(paramK);
/*  56 */     if (localCacheEntry1 != null) {
/*  57 */       localObject1 = localCacheEntry1.get();
/*     */     }
/*  59 */     if (localObject1 == null) {
/*  60 */       Object localObject2 = createObject(paramK);
/*     */ 
/*  63 */       paramK = normalizeKey(paramK);
/*  64 */       if ((paramK == null) || (localObject2 == null))
/*     */       {
/*  66 */         return null;
/*     */       }
/*     */ 
/*  69 */       CacheEntry localCacheEntry2 = new CacheEntry(paramK, localObject2, this.queue);
/*     */ 
/*  71 */       localCacheEntry1 = (CacheEntry)this.map.putIfAbsent(paramK, localCacheEntry2);
/*  72 */       if (localCacheEntry1 == null) {
/*  73 */         localObject1 = localObject2;
/*     */       } else {
/*  75 */         localObject1 = localCacheEntry1.get();
/*  76 */         if (localObject1 == null) {
/*  77 */           this.map.put(paramK, localCacheEntry2);
/*  78 */           localObject1 = localObject2;
/*     */         }
/*     */       }
/*     */     }
/*  82 */     return localObject1;
/*     */   }
/*     */ 
/*     */   protected V put(K paramK, V paramV) {
/*  86 */     CacheEntry localCacheEntry1 = new CacheEntry(paramK, paramV, this.queue);
/*  87 */     CacheEntry localCacheEntry2 = (CacheEntry)this.map.put(paramK, localCacheEntry1);
/*  88 */     return localCacheEntry2 == null ? null : localCacheEntry2.get();
/*     */   }
/*     */ 
/*     */   private void cleanStaleEntries()
/*     */   {
/*     */     CacheEntry localCacheEntry;
/*  94 */     while ((localCacheEntry = (CacheEntry)this.queue.poll()) != null)
/*  95 */       this.map.remove(localCacheEntry.getKey());
/*     */   }
/*     */ 
/*     */   protected abstract V createObject(K paramK);
/*     */ 
/*     */   protected K normalizeKey(K paramK)
/*     */   {
/* 102 */     return paramK;
/*     */   }
/*     */ 
/*     */   private static class CacheEntry<K, V> extends SoftReference<V> {
/*     */     private K key;
/*     */ 
/*     */     CacheEntry(K paramK, V paramV, ReferenceQueue<V> paramReferenceQueue) {
/* 109 */       super(paramReferenceQueue);
/* 110 */       this.key = paramK;
/*     */     }
/*     */ 
/*     */     K getKey() {
/* 114 */       return this.key;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.locale.LocaleObjectCache
 * JD-Core Version:    0.6.2
 */