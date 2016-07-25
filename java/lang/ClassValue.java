/*     */ package java.lang;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ public abstract class ClassValue<T>
/*     */ {
/* 197 */   private static final Entry<?>[] EMPTY_CACHE = { null };
/*     */ 
/* 249 */   final int hashCodeForCache = nextHashCode.getAndAdd(1640531527) & 0x3FFFFFFF;
/*     */ 
/* 252 */   private static final AtomicInteger nextHashCode = new AtomicInteger();
/*     */   private static final int HASH_INCREMENT = 1640531527;
/*     */   static final int HASH_MASK = 1073741823;
/* 271 */   final Identity identity = new Identity();
/*     */ 
/* 300 */   private volatile Version<T> version = new Version(this);
/*     */ 
/* 372 */   private static final Object CRITICAL_SECTION = new Object();
/*     */ 
/*     */   protected abstract T computeValue(Class<?> paramClass);
/*     */ 
/*     */   public T get(Class<?> paramClass)
/*     */   {
/*     */     Entry[] arrayOfEntry;
/* 102 */     Entry localEntry = ClassValueMap.probeHomeLocation(arrayOfEntry = getCacheCarefully(paramClass), this);
/*     */ 
/* 105 */     if (match(localEntry))
/*     */     {
/* 109 */       return localEntry.value();
/*     */     }
/*     */ 
/* 115 */     return getFromBackup(arrayOfEntry, paramClass);
/*     */   }
/*     */ 
/*     */   public void remove(Class<?> paramClass)
/*     */   {
/* 172 */     ClassValueMap localClassValueMap = getMap(paramClass);
/* 173 */     localClassValueMap.removeEntry(this);
/*     */   }
/*     */ 
/*     */   void put(Class<?> paramClass, T paramT)
/*     */   {
/* 178 */     ClassValueMap localClassValueMap = getMap(paramClass);
/* 179 */     localClassValueMap.changeEntry(this, paramT);
/*     */   }
/*     */ 
/*     */   private static Entry<?>[] getCacheCarefully(Class<?> paramClass)
/*     */   {
/* 189 */     ClassValueMap localClassValueMap = paramClass.classValueMap;
/* 190 */     if (localClassValueMap == null) return EMPTY_CACHE;
/* 191 */     Entry[] arrayOfEntry = localClassValueMap.getCache();
/* 192 */     return arrayOfEntry;
/*     */   }
/*     */ 
/*     */   private T getFromBackup(Entry<?>[] paramArrayOfEntry, Class<?> paramClass)
/*     */   {
/* 206 */     Entry localEntry = ClassValueMap.probeBackupLocations(paramArrayOfEntry, this);
/* 207 */     if (localEntry != null)
/* 208 */       return localEntry.value();
/* 209 */     return getFromHashMap(paramClass);
/*     */   }
/*     */ 
/*     */   Entry<T> castEntry(Entry<?> paramEntry)
/*     */   {
/* 214 */     return paramEntry;
/*     */   }
/*     */ 
/*     */   private T getFromHashMap(Class<?> paramClass)
/*     */   {
/* 220 */     ClassValueMap localClassValueMap = getMap(paramClass);
/*     */     while (true) {
/* 222 */       Entry localEntry = localClassValueMap.startEntry(this);
/* 223 */       if (!localEntry.isPromise())
/* 224 */         return localEntry.value();
/*     */       try
/*     */       {
/* 227 */         localEntry = makeEntry(localEntry.version(), computeValue(paramClass));
/*     */       }
/*     */       finally
/*     */       {
/* 231 */         localEntry = localClassValueMap.finishEntry(this, localEntry);
/*     */       }
/* 233 */       if (localEntry != null)
/* 234 */         return localEntry.value();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean match(Entry<?> paramEntry)
/*     */   {
/* 243 */     return (paramEntry != null) && (paramEntry.get() == this.version);
/*     */   }
/*     */ 
/*     */   Version<T> version()
/*     */   {
/* 301 */     return this.version; } 
/* 302 */   void bumpVersion() { this.version = new Version(this); }
/*     */ 
/*     */ 
/*     */   private static ClassValueMap getMap(Class<?> paramClass)
/*     */   {
/* 367 */     ClassValueMap localClassValueMap = paramClass.classValueMap;
/* 368 */     if (localClassValueMap != null) return localClassValueMap;
/* 369 */     return initializeMap(paramClass);
/*     */   }
/*     */ 
/*     */   private static ClassValueMap initializeMap(Class<?> paramClass)
/*     */   {
/*     */     ClassValueMap localClassValueMap;
/* 375 */     synchronized (CRITICAL_SECTION)
/*     */     {
/* 377 */       if ((localClassValueMap = paramClass.classValueMap) == null)
/* 378 */         paramClass.classValueMap = (localClassValueMap = new ClassValueMap(paramClass));
/*     */     }
/* 380 */     return localClassValueMap;
/*     */   }
/*     */ 
/*     */   static <T> Entry<T> makeEntry(Version<T> paramVersion, T paramT)
/*     */   {
/* 385 */     return new Entry(paramVersion, paramT);
/*     */   }
/*     */ 
/*     */   static class ClassValueMap extends WeakHashMap<ClassValue.Identity, ClassValue.Entry<?>>
/*     */   {
/*     */     private final Class<?> type;
/*     */     private ClassValue.Entry<?>[] cacheArray;
/*     */     private int cacheLoad;
/*     */     private int cacheLoadLimit;
/*     */     private static final int INITIAL_ENTRIES = 32;
/*     */     private static final int CACHE_LOAD_LIMIT = 67;
/*     */     private static final int PROBE_LIMIT = 6;
/*     */ 
/*     */     ClassValueMap(Class<?> paramClass)
/*     */     {
/* 420 */       this.type = paramClass;
/* 421 */       sizeCache(32);
/*     */     }
/*     */     ClassValue.Entry<?>[] getCache() {
/* 424 */       return this.cacheArray;
/*     */     }
/*     */ 
/*     */     synchronized <T> ClassValue.Entry<T> startEntry(ClassValue<T> paramClassValue)
/*     */     {
/* 430 */       ClassValue.Entry localEntry = (ClassValue.Entry)get(paramClassValue.identity);
/* 431 */       ClassValue.Version localVersion = paramClassValue.version();
/* 432 */       if (localEntry == null) {
/* 433 */         localEntry = localVersion.promise();
/*     */ 
/* 436 */         put(paramClassValue.identity, localEntry);
/*     */ 
/* 438 */         return localEntry;
/* 439 */       }if (localEntry.isPromise())
/*     */       {
/* 442 */         if (localEntry.version() != localVersion) {
/* 443 */           localEntry = localVersion.promise();
/* 444 */           put(paramClassValue.identity, localEntry);
/*     */         }
/* 446 */         return localEntry;
/*     */       }
/*     */ 
/* 449 */       if (localEntry.version() != localVersion)
/*     */       {
/* 452 */         localEntry = localEntry.refreshVersion(localVersion);
/* 453 */         put(paramClassValue.identity, localEntry);
/*     */       }
/*     */ 
/* 456 */       checkCacheLoad();
/* 457 */       addToCache(paramClassValue, localEntry);
/* 458 */       return localEntry;
/*     */     }
/*     */ 
/*     */     synchronized <T> ClassValue.Entry<T> finishEntry(ClassValue<T> paramClassValue, ClassValue.Entry<T> paramEntry)
/*     */     {
/* 466 */       ClassValue.Entry localEntry = (ClassValue.Entry)get(paramClassValue.identity);
/* 467 */       if (paramEntry == localEntry)
/*     */       {
/* 469 */         assert (paramEntry.isPromise());
/* 470 */         remove(paramClassValue.identity);
/* 471 */         return null;
/* 472 */       }if ((localEntry != null) && (localEntry.isPromise()) && (localEntry.version() == paramEntry.version()))
/*     */       {
/* 475 */         ClassValue.Version localVersion = paramClassValue.version();
/* 476 */         if (paramEntry.version() != localVersion)
/* 477 */           paramEntry = paramEntry.refreshVersion(localVersion);
/* 478 */         put(paramClassValue.identity, paramEntry);
/*     */ 
/* 480 */         checkCacheLoad();
/* 481 */         addToCache(paramClassValue, paramEntry);
/* 482 */         return paramEntry;
/*     */       }
/*     */ 
/* 485 */       return null;
/*     */     }
/*     */ 
/*     */     synchronized void removeEntry(ClassValue<?> paramClassValue)
/*     */     {
/* 492 */       ClassValue.Entry localEntry = (ClassValue.Entry)remove(paramClassValue.identity);
/* 493 */       if (localEntry != null)
/*     */       {
/* 495 */         if (localEntry.isPromise())
/*     */         {
/* 499 */           put(paramClassValue.identity, localEntry);
/*     */         }
/*     */         else {
/* 502 */           paramClassValue.bumpVersion();
/*     */ 
/* 504 */           removeStaleEntries(paramClassValue);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     synchronized <T> void changeEntry(ClassValue<T> paramClassValue, T paramT)
/*     */     {
/* 512 */       ClassValue.Entry localEntry1 = (ClassValue.Entry)get(paramClassValue.identity);
/* 513 */       ClassValue.Version localVersion = paramClassValue.version();
/* 514 */       if (localEntry1 != null) {
/* 515 */         if ((localEntry1.version() == localVersion) && (localEntry1.value() == paramT))
/*     */         {
/* 517 */           return;
/* 518 */         }paramClassValue.bumpVersion();
/* 519 */         removeStaleEntries(paramClassValue);
/*     */       }
/* 521 */       ClassValue.Entry localEntry2 = ClassValue.makeEntry(localVersion, paramT);
/* 522 */       put(paramClassValue.identity, localEntry2);
/*     */ 
/* 524 */       checkCacheLoad();
/* 525 */       addToCache(paramClassValue, localEntry2);
/*     */     }
/*     */ 
/*     */     static ClassValue.Entry<?> loadFromCache(ClassValue.Entry<?>[] paramArrayOfEntry, int paramInt)
/*     */     {
/* 538 */       return paramArrayOfEntry[(paramInt & paramArrayOfEntry.length - 1)];
/*     */     }
/*     */ 
/*     */     static <T> ClassValue.Entry<T> probeHomeLocation(ClassValue.Entry<?>[] paramArrayOfEntry, ClassValue<T> paramClassValue)
/*     */     {
/* 544 */       return paramClassValue.castEntry(loadFromCache(paramArrayOfEntry, paramClassValue.hashCodeForCache));
/*     */     }
/*     */ 
/*     */     static <T> ClassValue.Entry<T> probeBackupLocations(ClassValue.Entry<?>[] paramArrayOfEntry, ClassValue<T> paramClassValue)
/*     */     {
/* 551 */       int i = paramArrayOfEntry.length - 1;
/* 552 */       int j = paramClassValue.hashCodeForCache & i;
/* 553 */       ClassValue.Entry<?> localEntry1 = paramArrayOfEntry[j];
/* 554 */       if (localEntry1 == null) {
/* 555 */         return null;
/*     */       }
/*     */ 
/* 558 */       int k = -1;
/* 559 */       for (int m = j + 1; m < j + 6; m++) {
/* 560 */         ClassValue.Entry<?> localEntry2 = paramArrayOfEntry[(m & i)];
/* 561 */         if (localEntry2 == null) {
/*     */           break;
/*     */         }
/* 564 */         if (paramClassValue.match(localEntry2))
/*     */         {
/* 566 */           paramArrayOfEntry[j] = localEntry2;
/* 567 */           if (k >= 0)
/* 568 */             paramArrayOfEntry[(m & i)] = ClassValue.Entry.DEAD_ENTRY;
/*     */           else {
/* 570 */             k = m;
/*     */           }
/* 572 */           paramArrayOfEntry[(k & i)] = (entryDislocation(paramArrayOfEntry, k, localEntry1) < 6 ? localEntry1 : ClassValue.Entry.DEAD_ENTRY);
/*     */ 
/* 575 */           return paramClassValue.castEntry(localEntry2);
/*     */         }
/*     */ 
/* 578 */         if ((!localEntry2.isLive()) && (k < 0)) k = m;
/*     */       }
/* 580 */       return null;
/*     */     }
/*     */ 
/*     */     private static int entryDislocation(ClassValue.Entry<?>[] paramArrayOfEntry, int paramInt, ClassValue.Entry<?> paramEntry)
/*     */     {
/* 585 */       ClassValue localClassValue = paramEntry.classValueOrNull();
/* 586 */       if (localClassValue == null) return 0;
/* 587 */       int i = paramArrayOfEntry.length - 1;
/* 588 */       return paramInt - localClassValue.hashCodeForCache & i;
/*     */     }
/*     */ 
/*     */     private void sizeCache(int paramInt)
/*     */     {
/* 596 */       assert ((paramInt & paramInt - 1) == 0);
/* 597 */       this.cacheLoad = 0;
/* 598 */       this.cacheLoadLimit = ((int)(paramInt * 67.0D / 100.0D));
/* 599 */       this.cacheArray = new ClassValue.Entry[paramInt];
/*     */     }
/*     */ 
/*     */     private void checkCacheLoad()
/*     */     {
/* 604 */       if (this.cacheLoad >= this.cacheLoadLimit)
/* 605 */         reduceCacheLoad();
/*     */     }
/*     */ 
/*     */     private void reduceCacheLoad() {
/* 609 */       removeStaleEntries();
/* 610 */       if (this.cacheLoad < this.cacheLoadLimit)
/* 611 */         return;
/* 612 */       ClassValue.Entry[] arrayOfEntry1 = getCache();
/* 613 */       if (arrayOfEntry1.length > 1073741823)
/* 614 */         return;
/* 615 */       sizeCache(arrayOfEntry1.length * 2);
/* 616 */       for (ClassValue.Entry localEntry : arrayOfEntry1)
/* 617 */         if ((localEntry != null) && (localEntry.isLive()))
/* 618 */           addToCache(localEntry);
/*     */     }
/*     */ 
/*     */     private void removeStaleEntries(ClassValue.Entry<?>[] paramArrayOfEntry, int paramInt1, int paramInt2)
/*     */     {
/* 628 */       int i = paramArrayOfEntry.length - 1;
/* 629 */       int j = 0;
/* 630 */       for (int k = paramInt1; k < paramInt1 + paramInt2; k++) {
/* 631 */         ClassValue.Entry<?> localEntry = paramArrayOfEntry[(k & i)];
/* 632 */         if ((localEntry != null) && (!localEntry.isLive()))
/*     */         {
/* 634 */           ClassValue.Entry localEntry1 = null;
/*     */ 
/* 637 */           localEntry1 = findReplacement(paramArrayOfEntry, k);
/*     */ 
/* 639 */           paramArrayOfEntry[(k & i)] = localEntry1;
/* 640 */           if (localEntry1 == null) j++; 
/*     */         }
/*     */       }
/* 642 */       this.cacheLoad = Math.max(0, this.cacheLoad - j);
/*     */     }
/*     */ 
/*     */     private ClassValue.Entry<?> findReplacement(ClassValue.Entry<?>[] paramArrayOfEntry, int paramInt)
/*     */     {
/* 651 */       Object localObject = null;
/* 652 */       int i = -1; int j = 0;
/* 653 */       int k = paramArrayOfEntry.length - 1;
/* 654 */       for (int m = paramInt + 1; m < paramInt + 6; m++) {
/* 655 */         ClassValue.Entry<?> localEntry = paramArrayOfEntry[(m & k)];
/* 656 */         if (localEntry == null) break;
/* 657 */         if (localEntry.isLive()) {
/* 658 */           int n = entryDislocation(paramArrayOfEntry, m, localEntry);
/* 659 */           if (n != 0) {
/* 660 */             int i1 = m - n;
/* 661 */             if (i1 <= paramInt)
/*     */             {
/* 663 */               if (i1 == paramInt)
/*     */               {
/* 665 */                 i = 1;
/* 666 */                 j = m;
/* 667 */                 localObject = localEntry;
/* 668 */               } else if (i <= 0) {
/* 669 */                 i = 0;
/* 670 */                 j = m;
/* 671 */                 localObject = localEntry;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 676 */       if (i >= 0) {
/* 677 */         if (paramArrayOfEntry[(j + 1 & k)] != null)
/*     */         {
/* 679 */           paramArrayOfEntry[(j & k)] = ClassValue.Entry.DEAD_ENTRY;
/*     */         } else {
/* 681 */           paramArrayOfEntry[(j & k)] = null;
/* 682 */           this.cacheLoad -= 1;
/*     */         }
/*     */       }
/* 685 */       return localObject;
/*     */     }
/*     */ 
/*     */     private void removeStaleEntries(ClassValue<?> paramClassValue)
/*     */     {
/* 690 */       removeStaleEntries(getCache(), paramClassValue.hashCodeForCache, 6);
/*     */     }
/*     */ 
/*     */     private void removeStaleEntries()
/*     */     {
/* 695 */       ClassValue.Entry[] arrayOfEntry = getCache();
/* 696 */       removeStaleEntries(arrayOfEntry, 0, arrayOfEntry.length + 6 - 1);
/*     */     }
/*     */ 
/*     */     private <T> void addToCache(ClassValue.Entry<T> paramEntry)
/*     */     {
/* 701 */       ClassValue localClassValue = paramEntry.classValueOrNull();
/* 702 */       if (localClassValue != null)
/* 703 */         addToCache(localClassValue, paramEntry);
/*     */     }
/*     */ 
/*     */     private <T> void addToCache(ClassValue<T> paramClassValue, ClassValue.Entry<T> paramEntry)
/*     */     {
/* 710 */       ClassValue.Entry[] arrayOfEntry = getCache();
/* 711 */       int i = arrayOfEntry.length - 1;
/* 712 */       int j = paramClassValue.hashCodeForCache & i;
/* 713 */       ClassValue.Entry localEntry = placeInCache(arrayOfEntry, j, paramEntry, false);
/* 714 */       if (localEntry == null) return;
/*     */ 
/* 717 */       int k = entryDislocation(arrayOfEntry, j, localEntry);
/* 718 */       int m = j - k;
/* 719 */       for (int n = m; n < m + 6; n++)
/* 720 */         if (placeInCache(arrayOfEntry, n & i, localEntry, true) == null)
/* 721 */           return;
/*     */     }
/*     */ 
/*     */     private ClassValue.Entry<?> placeInCache(ClassValue.Entry<?>[] paramArrayOfEntry, int paramInt, ClassValue.Entry<?> paramEntry, boolean paramBoolean)
/*     */     {
/* 732 */       ClassValue.Entry localEntry = overwrittenEntry(paramArrayOfEntry[paramInt]);
/* 733 */       if ((paramBoolean) && (localEntry != null))
/*     */       {
/* 735 */         return paramEntry;
/*     */       }
/* 737 */       paramArrayOfEntry[paramInt] = paramEntry;
/* 738 */       return localEntry;
/*     */     }
/*     */ 
/*     */     private <T> ClassValue.Entry<T> overwrittenEntry(ClassValue.Entry<T> paramEntry)
/*     */     {
/* 749 */       if (paramEntry == null) this.cacheLoad += 1;
/* 750 */       else if (paramEntry.isLive()) return paramEntry;
/* 751 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Entry<T> extends WeakReference<ClassValue.Version<T>>
/*     */   {
/*     */     final Object value;
/* 359 */     static final Entry<?> DEAD_ENTRY = new Entry(null, null);
/*     */ 
/*     */     Entry(ClassValue.Version<T> paramVersion, T paramT)
/*     */     {
/* 326 */       super();
/* 327 */       this.value = paramT;
/*     */     }
/* 329 */     private void assertNotPromise() { assert (!isPromise()); }
/*     */ 
/*     */     Entry(ClassValue.Version<T> paramVersion) {
/* 332 */       super();
/* 333 */       this.value = this;
/*     */     }
/*     */ 
/*     */     T value() {
/* 337 */       assertNotPromise(); return this.value; } 
/* 338 */     boolean isPromise() { return this.value == this; } 
/* 339 */     ClassValue.Version<T> version() { return (ClassValue.Version)get(); } 
/*     */     ClassValue<T> classValueOrNull() {
/* 341 */       ClassValue.Version localVersion = version();
/* 342 */       return localVersion == null ? null : localVersion.classValue();
/*     */     }
/*     */     boolean isLive() {
/* 345 */       ClassValue.Version localVersion = version();
/* 346 */       if (localVersion == null) return false;
/* 347 */       if (localVersion.isLive()) return true;
/* 348 */       clear();
/* 349 */       return false;
/*     */     }
/*     */     Entry<T> refreshVersion(ClassValue.Version<T> paramVersion) {
/* 352 */       assertNotPromise();
/*     */ 
/* 354 */       Entry localEntry = new Entry(paramVersion, this.value);
/* 355 */       clear();
/*     */ 
/* 357 */       return localEntry;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Identity
/*     */   {
/*     */   }
/*     */ 
/*     */   static class Version<T>
/*     */   {
/*     */     private final ClassValue<T> classValue;
/* 305 */     private final ClassValue.Entry<T> promise = new ClassValue.Entry(this);
/*     */ 
/* 306 */     Version(ClassValue<T> paramClassValue) { this.classValue = paramClassValue; } 
/* 307 */     ClassValue<T> classValue() { return this.classValue; } 
/* 308 */     ClassValue.Entry<T> promise() { return this.promise; } 
/* 309 */     boolean isLive() { return this.classValue.version() == this; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ClassValue
 * JD-Core Version:    0.6.2
 */