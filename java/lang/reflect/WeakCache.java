/*     */
package java.lang.reflect;
/*     */ 
/*     */

import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;

/*     */
/*     */ final class WeakCache<K, P, V>
/*     */ {
    /*  73 */   private final ReferenceQueue<K> refQueue = new ReferenceQueue();
    /*     */
/*  76 */   private final ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> map = new ConcurrentHashMap();
    /*     */
/*  78 */   private final ConcurrentMap<Supplier<V>, Boolean> reverseMap = new ConcurrentHashMap();
    /*     */   private final BiFunction<K, P, ?> subKeyFactory;
    /*     */   private final BiFunction<K, P, V> valueFactory;

    /*     */
/*     */
    public WeakCache(BiFunction<K, P, ?> paramBiFunction, BiFunction<K, P, V> paramBiFunction1)
/*     */ {
/*  95 */
        this.subKeyFactory = ((BiFunction) Objects.requireNonNull(paramBiFunction));
/*  96 */
        this.valueFactory = ((BiFunction) Objects.requireNonNull(paramBiFunction1));
/*     */
    }

    /*     */
/*     */
    public V get(K paramK, P paramP)
/*     */ {
/* 115 */
        Objects.requireNonNull(paramP);
/*     */ 
/* 117 */
        expungeStaleEntries();
/*     */ 
/* 119 */
        Object localObject1 = CacheKey.valueOf(paramK, this.refQueue);
/*     */ 
/* 122 */
        Object localObject2 = (ConcurrentMap) this.map.get(localObject1);
/* 123 */
        if (localObject2 == null) {
/* 124 */
            localObject3 = (ConcurrentMap) this.map.putIfAbsent(localObject1, localObject2 = new ConcurrentHashMap());
/*     */ 
/* 127 */
            if (localObject3 != null) {
/* 128 */
                localObject2 = localObject3;
/*     */
            }
/*     */ 
/*     */
        }
/*     */ 
/* 134 */
        Object localObject3 = Objects.requireNonNull(this.subKeyFactory.apply(paramK, paramP));
/* 135 */
        Object localObject4 = (Supplier) ((ConcurrentMap) localObject2).get(localObject3);
/* 136 */
        Factory localFactory = null;
/*     */
        while (true)
/*     */ {
/* 139 */
            if (localObject4 != null)
/*     */ {
/* 141 */
                Object localObject5 = ((Supplier) localObject4).get();
/* 142 */
                if (localObject5 != null) {
/* 143 */
                    return localObject5;
/*     */
                }
/*     */ 
/*     */
            }
/*     */ 
/* 151 */
            if (localFactory == null) {
/* 152 */
                localFactory = new Factory(paramK, paramP, localObject3, (ConcurrentMap) localObject2);
/*     */
            }
/*     */ 
/* 155 */
            if (localObject4 == null) {
/* 156 */
                localObject4 = (Supplier) ((ConcurrentMap) localObject2).putIfAbsent(localObject3, localFactory);
/* 157 */
                if (localObject4 == null)
/*     */ {
/* 159 */
                    localObject4 = localFactory;
/*     */
                }
/*     */ 
/*     */
            }
/* 163 */
            else if (((ConcurrentMap) localObject2).replace(localObject3, localObject4, localFactory))
/*     */ {
/* 167 */
                localObject4 = localFactory;
/*     */
            }
/*     */
            else {
/* 170 */
                localObject4 = (Supplier) ((ConcurrentMap) localObject2).get(localObject3);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public boolean containsValue(V paramV)
/*     */ {
/* 186 */
        Objects.requireNonNull(paramV);
/*     */ 
/* 188 */
        expungeStaleEntries();
/* 189 */
        return this.reverseMap.containsKey(new LookupValue(paramV));
/*     */
    }

    /*     */
/*     */
    public int size()
/*     */ {
/* 197 */
        expungeStaleEntries();
/* 198 */
        return this.reverseMap.size();
/*     */
    }

    /*     */
/*     */
    private void expungeStaleEntries()
/*     */ {
/*     */
        CacheKey localCacheKey;
/* 203 */
        while ((localCacheKey = (CacheKey) this.refQueue.poll()) != null)
/* 204 */ localCacheKey.expungeFrom(this.map, this.reverseMap);
/*     */
    }

    /*     */
/*     */   static abstract interface BiFunction<T, U, R>
/*     */ {
        /*     */
        public abstract R apply(T paramT, U paramU);
/*     */
    }

    /*     */
/*     */   private static final class CacheKey<K> extends WeakReference<K>
/*     */ {
        /* 344 */     private static final Object NULL_KEY = new Object();
        /*     */     private final int hash;

        /*     */
/*     */
        static <K> Object valueOf(K paramK, ReferenceQueue<K> paramReferenceQueue)
/*     */ {
/* 347 */
            return paramK == null ? NULL_KEY : new CacheKey(paramK, paramReferenceQueue);
/*     */
        }

        /*     */
/*     */
        private CacheKey(K paramK, ReferenceQueue<K> paramReferenceQueue)
/*     */ {
/* 358 */
            super(paramReferenceQueue);
/* 359 */
            this.hash = System.identityHashCode(paramK);
/*     */
        }

        /*     */
/*     */
        public int hashCode()
/*     */ {
/* 364 */
            return this.hash;
/*     */
        }

        /*     */
/*     */
        public boolean equals(Object paramObject)
/*     */ {
/*     */
            Object localObject;
/* 370 */
            return (paramObject == this) || ((paramObject != null) && (paramObject.getClass() == getClass()) && ((localObject = get()) != null) && (localObject == ((CacheKey) paramObject).get()));
/*     */
        }

        /*     */
/*     */     void expungeFrom(ConcurrentMap<?, ? extends ConcurrentMap<?, ?>> paramConcurrentMap, ConcurrentMap<?, Boolean> paramConcurrentMap1)
/*     */ {
/* 384 */
            ConcurrentMap localConcurrentMap = (ConcurrentMap) paramConcurrentMap.remove(this);
/*     */
            Iterator localIterator;
/* 386 */
            if (localConcurrentMap != null)
/* 387 */ for (localIterator = localConcurrentMap.values().iterator(); localIterator.hasNext(); ) {
                Object localObject = localIterator.next();
/* 388 */
                paramConcurrentMap1.remove(localObject);
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static final class CacheValue<V> extends WeakReference<V>
/*     */ implements WeakCache.Value<V>
/*     */ {
        /*     */     private final int hash;

        /*     */
/*     */     CacheValue(V paramV)
/*     */ {
/* 316 */
            super();
/* 317 */
            this.hash = System.identityHashCode(paramV);
/*     */
        }

        /*     */
/*     */
        public int hashCode()
/*     */ {
/* 322 */
            return this.hash;
/*     */
        }

        /*     */
/*     */
        public boolean equals(Object paramObject)
/*     */ {
/*     */
            Object localObject;
/* 328 */
            return (paramObject == this) || (((paramObject instanceof WeakCache.Value)) && ((localObject = get()) != null) && (localObject == ((WeakCache.Value) paramObject).get()));
/*     */
        }
/*     */
    }

    /*     */
/*     */   private final class Factory
/*     */ implements WeakCache.Supplier<V>
/*     */ {
        /*     */     private final K key;
        /*     */     private final P parameter;
        /*     */     private final Object subKey;
        /*     */     private final ConcurrentMap<Object, WeakCache.Supplier<V>> valuesMap;

        /*     */
/*     */     Factory(P paramObject, Object paramConcurrentMap, ConcurrentMap<Object, WeakCache.Supplier<V>> arg4)
/*     */ {
/* 221 */
            this.key = paramObject;
/* 222 */
            this.parameter = paramConcurrentMap;
/*     */
            Object localObject1;
/* 223 */
            this.subKey = localObject1;
/*     */
            Object localObject2;
/* 224 */
            this.valuesMap = localObject2;
/*     */
        }

        /*     */
/*     */
        public synchronized V get()
/*     */ {
/* 230 */
            WeakCache.Supplier localSupplier = (WeakCache.Supplier) this.valuesMap.get(this.subKey);
/* 231 */
            if (localSupplier != this)
/*     */ {
/* 237 */
                return null;
/*     */
            }
/*     */ 
/* 242 */
            Object localObject1 = null;
/*     */
            try {
/* 244 */
                localObject1 = Objects.requireNonNull(WeakCache.this.valueFactory.apply(this.key, this.parameter));
/*     */
            } finally {
/* 246 */
                if (localObject1 == null) {
/* 247 */
                    this.valuesMap.remove(this.subKey, this);
/*     */
                }
/*     */
            }
/*     */ 
/* 251 */
            assert (localObject1 != null);
/*     */ 
/* 254 */
            WeakCache.CacheValue localCacheValue = new WeakCache.CacheValue(localObject1);
/*     */ 
/* 257 */
            if (this.valuesMap.replace(this.subKey, this, localCacheValue))
/*     */ {
/* 259 */
                WeakCache.this.reverseMap.put(localCacheValue, Boolean.TRUE);
/*     */
            }
/* 261 */
            else throw new AssertionError("Should not reach here");
/*     */ 
/* 266 */
            return localObject1;
/*     */
        }
/*     */
    }

    /*     */
/*     */   private static final class LookupValue<V>
/*     */ implements WeakCache.Value<V>
/*     */ {
        /*     */     private final V value;

        /*     */
/*     */     LookupValue(V paramV)
/*     */ {
/* 286 */
            this.value = paramV;
/*     */
        }

        /*     */
/*     */
        public V get()
/*     */ {
/* 291 */
            return this.value;
/*     */
        }

        /*     */
/*     */
        public int hashCode()
/*     */ {
/* 296 */
            return System.identityHashCode(this.value);
/*     */
        }

        /*     */
/*     */
        public boolean equals(Object paramObject)
/*     */ {
/* 301 */
            return (paramObject == this) || (((paramObject instanceof WeakCache.Value)) && (this.value == ((WeakCache.Value) paramObject).get()));
/*     */
        }
/*     */
    }

    /*     */
/*     */   static abstract interface Supplier<T>
/*     */ {
        /*     */
        public abstract T get();
/*     */
    }

    /*     */
/*     */   private static abstract interface Value<V> extends WeakCache.Supplier<V>
/*     */ {
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.WeakCache
 * JD-Core Version:    0.6.2
 */