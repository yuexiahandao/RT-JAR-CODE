/*     */ package com.sun.beans.util;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public abstract class Cache<K, V>
/*     */ {
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*     */   private final boolean identity;
/*     */   private final Kind keyKind;
/*     */   private final Kind valueKind;
/*  48 */   private final ReferenceQueue<Object> queue = new ReferenceQueue();
/*     */ 
/*  50 */   private volatile Cache<K, V>[].CacheEntry<K, V> table = newTable(8);
/*  51 */   private int threshold = 6;
/*     */   private int size;
/*     */ 
/*     */   public abstract V create(K paramK);
/*     */ 
/*     */   public Cache(Kind paramKind1, Kind paramKind2)
/*     */   {
/*  73 */     this(paramKind1, paramKind2, false);
/*     */   }
/*     */ 
/*     */   public Cache(Kind paramKind1, Kind paramKind2, boolean paramBoolean)
/*     */   {
/*  90 */     Objects.requireNonNull(paramKind1, "keyKind");
/*  91 */     Objects.requireNonNull(paramKind2, "valueKind");
/*  92 */     this.keyKind = paramKind1;
/*  93 */     this.valueKind = paramKind2;
/*  94 */     this.identity = paramBoolean;
/*     */   }
/*     */ 
/*     */   public final V get(K paramK)
/*     */   {
/* 109 */     Objects.requireNonNull(paramK, "key");
/* 110 */     removeStaleEntries();
/* 111 */     int i = hash(paramK);
/*     */ 
/* 114 */     CacheEntry[] arrayOfCacheEntry = this.table;
/* 115 */     Object localObject1 = getEntryValue(paramK, i, arrayOfCacheEntry[index(i, arrayOfCacheEntry)]);
/* 116 */     if (localObject1 != null) {
/* 117 */       return localObject1;
/*     */     }
/* 119 */     synchronized (this.queue)
/*     */     {
/* 122 */       int j = index(i, this.table);
/* 123 */       localObject1 = getEntryValue(paramK, i, this.table[j]);
/* 124 */       if (localObject1 != null) {
/* 125 */         return localObject1;
/*     */       }
/* 127 */       Object localObject2 = create(paramK);
/* 128 */       Objects.requireNonNull(localObject2, "value");
/* 129 */       this.table[j] = new CacheEntry(i, paramK, localObject2, this.table[j], null);
/* 130 */       if (++this.size >= this.threshold) {
/* 131 */         if (this.table.length == 1073741824) {
/* 132 */           this.threshold = 2147483647;
/*     */         } else {
/* 134 */           removeStaleEntries();
/* 135 */           arrayOfCacheEntry = newTable(this.table.length << 1);
/* 136 */           transfer(this.table, arrayOfCacheEntry);
/*     */ 
/* 140 */           if (this.size >= this.threshold / 2) {
/* 141 */             this.table = arrayOfCacheEntry;
/* 142 */             this.threshold <<= 1;
/*     */           } else {
/* 144 */             transfer(arrayOfCacheEntry, this.table);
/*     */           }
/* 146 */           removeStaleEntries();
/*     */         }
/*     */       }
/* 149 */       return localObject2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void remove(K paramK)
/*     */   {
/* 159 */     if (paramK != null)
/* 160 */       synchronized (this.queue) {
/* 161 */         removeStaleEntries();
/* 162 */         int i = hash(paramK);
/* 163 */         int j = index(i, this.table);
/* 164 */         Object localObject1 = this.table[j];
/* 165 */         Object localObject2 = localObject1;
/* 166 */         while (localObject2 != null) {
/* 167 */           CacheEntry localCacheEntry = localObject2.next;
/* 168 */           if (localObject2.matches(i, paramK)) {
/* 169 */             if (localObject2 == localObject1)
/* 170 */               this.table[j] = localCacheEntry;
/*     */             else {
/* 172 */               ((CacheEntry)localObject1).next = localCacheEntry;
/*     */             }
/* 174 */             localObject2.unlink();
/* 175 */             break;
/*     */           }
/* 177 */           localObject1 = localObject2;
/* 178 */           localObject2 = localCacheEntry;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 189 */     synchronized (this.queue) {
/* 190 */       int i = this.table.length;
/* 191 */       while (0 < i--) {
/* 192 */         Object localObject1 = this.table[i];
/* 193 */         while (localObject1 != null) {
/* 194 */           CacheEntry localCacheEntry = ((CacheEntry)localObject1).next;
/* 195 */           ((CacheEntry)localObject1).unlink();
/* 196 */           localObject1 = localCacheEntry;
/*     */         }
/* 198 */         this.table[i] = null;
/*     */       }
/* 200 */       while (null != this.queue.poll());
/*     */     }
/*     */   }
/*     */ 
/*     */   private int hash(Object paramObject)
/*     */   {
/* 217 */     if (this.identity) {
/* 218 */       i = System.identityHashCode(paramObject);
/* 219 */       return (i << 1) - (i << 8);
/*     */     }
/* 221 */     int i = paramObject.hashCode();
/*     */ 
/* 225 */     i ^= i >>> 20 ^ i >>> 12;
/* 226 */     return i ^ i >>> 7 ^ i >>> 4;
/*     */   }
/*     */ 
/*     */   private static int index(int paramInt, Object[] paramArrayOfObject)
/*     */   {
/* 238 */     return paramInt & paramArrayOfObject.length - 1;
/*     */   }
/*     */ 
/*     */   private Cache<K, V>[].CacheEntry<K, V> newTable(int paramInt)
/*     */   {
/* 249 */     return (CacheEntry[])new CacheEntry[paramInt];
/*     */   }
/*     */ 
/*     */   private V getEntryValue(K paramK, int paramInt, Cache<K, V>.CacheEntry<K, V> paramCache) {
/* 253 */     while (paramCache != null) {
/* 254 */       if (paramCache.matches(paramInt, paramK)) {
/* 255 */         return paramCache.value.getReferent();
/*     */       }
/* 257 */       paramCache = paramCache.next;
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */   private void removeStaleEntries() {
/* 263 */     Reference localReference = this.queue.poll();
/* 264 */     if (localReference != null)
/* 265 */       synchronized (this.queue) {
/*     */         do {
/* 267 */           if ((localReference instanceof Ref)) {
/* 268 */             Ref localRef = (Ref)localReference;
/*     */ 
/* 270 */             CacheEntry localCacheEntry1 = (CacheEntry)localRef.getOwner();
/* 271 */             if (localCacheEntry1 != null) {
/* 272 */               int i = index(localCacheEntry1.hash, this.table);
/* 273 */               Object localObject1 = this.table[i];
/* 274 */               Object localObject2 = localObject1;
/* 275 */               while (localObject2 != null) {
/* 276 */                 CacheEntry localCacheEntry2 = localObject2.next;
/* 277 */                 if (localObject2 == localCacheEntry1) {
/* 278 */                   if (localObject2 == localObject1)
/* 279 */                     this.table[i] = localCacheEntry2;
/*     */                   else {
/* 281 */                     ((CacheEntry)localObject1).next = localCacheEntry2;
/*     */                   }
/* 283 */                   localObject2.unlink();
/* 284 */                   break;
/*     */                 }
/* 286 */                 localObject1 = localObject2;
/* 287 */                 localObject2 = localCacheEntry2;
/*     */               }
/*     */             }
/*     */           }
/* 291 */           localReference = this.queue.poll();
/*     */         }
/* 293 */         while (localReference != null);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void transfer(Cache<K, V>[].CacheEntry<K, V> paramArrayOfCache1, Cache<K, V>[].CacheEntry<K, V> paramArrayOfCache2)
/*     */   {
/* 299 */     int i = paramArrayOfCache1.length;
/* 300 */     while (0 < i--) {
/* 301 */       Object localObject = paramArrayOfCache1[i];
/* 302 */       paramArrayOfCache1[i] = null;
/* 303 */       while (localObject != null) {
/* 304 */         CacheEntry localCacheEntry = ((CacheEntry)localObject).next;
/* 305 */         if ((((CacheEntry)localObject).key.isStale()) || (((CacheEntry)localObject).value.isStale())) {
/* 306 */           ((CacheEntry)localObject).unlink();
/*     */         } else {
/* 308 */           int j = index(((CacheEntry)localObject).hash, paramArrayOfCache2);
/* 309 */           ((CacheEntry)localObject).next = paramArrayOfCache2[j];
/* 310 */           paramArrayOfCache2[j] = localObject;
/*     */         }
/* 312 */         localObject = localCacheEntry;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class CacheEntry<K, V>
/*     */   {
/*     */     private final int hash;
/*     */     private final Cache.Ref<K> key;
/*     */     private final Cache.Ref<V> value;
/*     */     private volatile Cache<K, V>.CacheEntry<K, V> next;
/*     */ 
/*     */     private CacheEntry(K paramV, V paramCache, Cache<K, V>.CacheEntry<K, V> arg4)
/*     */     {
/* 335 */       this.hash = paramV;
/* 336 */       this.key = Cache.this.keyKind.create(this, paramCache, Cache.this.queue);
/*     */       Object localObject1;
/* 337 */       this.value = Cache.this.valueKind.create(this, localObject1, Cache.this.queue);
/*     */       Object localObject2;
/* 338 */       this.next = localObject2;
/*     */     }
/*     */ 
/*     */     private boolean matches(int paramInt, Object paramObject)
/*     */     {
/* 350 */       if (this.hash != paramInt) {
/* 351 */         return false;
/*     */       }
/* 353 */       Object localObject = this.key.getReferent();
/* 354 */       return (localObject == paramObject) || ((!Cache.this.identity) && (localObject != null) && (localObject.equals(paramObject)));
/*     */     }
/*     */ 
/*     */     private void unlink()
/*     */     {
/* 361 */       this.next = null;
/* 362 */       this.key.removeOwner();
/* 363 */       this.value.removeOwner();
/* 364 */       Cache.access$1110(Cache.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract enum Kind
/*     */   {
/* 406 */     STRONG, 
/*     */ 
/* 411 */     SOFT, 
/*     */ 
/* 418 */     WEAK;
/*     */ 
/*     */     abstract <T> Cache.Ref<T> create(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue);
/*     */ 
/*     */     private static final class Soft<T> extends SoftReference<T>
/*     */       implements Cache.Ref<T>
/*     */     {
/*     */       private Object owner;
/*     */ 
/*     */       private Soft(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*     */       {
/* 515 */         super(paramReferenceQueue);
/* 516 */         this.owner = paramObject;
/*     */       }
/*     */ 
/*     */       public Object getOwner()
/*     */       {
/* 525 */         return this.owner;
/*     */       }
/*     */ 
/*     */       public T getReferent()
/*     */       {
/* 534 */         return get();
/*     */       }
/*     */ 
/*     */       public boolean isStale()
/*     */       {
/* 543 */         return null == get();
/*     */       }
/*     */ 
/*     */       public void removeOwner()
/*     */       {
/* 550 */         this.owner = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     private static final class Strong<T>
/*     */       implements Cache.Ref<T>
/*     */     {
/*     */       private Object owner;
/*     */       private final T referent;
/*     */ 
/*     */       private Strong(Object paramObject, T paramT)
/*     */       {
/* 456 */         this.owner = paramObject;
/* 457 */         this.referent = paramT;
/*     */       }
/*     */ 
/*     */       public Object getOwner()
/*     */       {
/* 466 */         return this.owner;
/*     */       }
/*     */ 
/*     */       public T getReferent()
/*     */       {
/* 475 */         return this.referent;
/*     */       }
/*     */ 
/*     */       public boolean isStale()
/*     */       {
/* 484 */         return false;
/*     */       }
/*     */ 
/*     */       public void removeOwner()
/*     */       {
/* 491 */         this.owner = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     private static final class Weak<T> extends WeakReference<T>
/*     */       implements Cache.Ref<T>
/*     */     {
/*     */       private Object owner;
/*     */ 
/*     */       private Weak(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*     */       {
/* 574 */         super(paramReferenceQueue);
/* 575 */         this.owner = paramObject;
/*     */       }
/*     */ 
/*     */       public Object getOwner()
/*     */       {
/* 584 */         return this.owner;
/*     */       }
/*     */ 
/*     */       public T getReferent()
/*     */       {
/* 593 */         return get();
/*     */       }
/*     */ 
/*     */       public boolean isStale()
/*     */       {
/* 602 */         return null == get();
/*     */       }
/*     */ 
/*     */       public void removeOwner()
/*     */       {
/* 609 */         this.owner = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract interface Ref<T>
/*     */   {
/*     */     public abstract Object getOwner();
/*     */ 
/*     */     public abstract T getReferent();
/*     */ 
/*     */     public abstract boolean isStale();
/*     */ 
/*     */     public abstract void removeOwner();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.util.Cache
 * JD-Core Version:    0.6.2
 */