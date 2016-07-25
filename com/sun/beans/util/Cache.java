package com.sun.beans.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class Cache<K, V> {
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private final boolean identity;
    private final Kind keyKind;
    private final Kind valueKind;
    private final ReferenceQueue<Object> queue = new ReferenceQueue();

    private volatile Cache<K, V>[].
    CacheEntry<K, V> table = newTable(8);
    private int threshold = 6;
    private int size;

    public abstract V create(K paramK);

    public Cache(Kind paramKind1, Kind paramKind2) {
        this(paramKind1, paramKind2, false);
    }

    public Cache(Kind paramKind1, Kind paramKind2, boolean paramBoolean) {
        Objects.requireNonNull(paramKind1, "keyKind");
        Objects.requireNonNull(paramKind2, "valueKind");
        this.keyKind = paramKind1;
        this.valueKind = paramKind2;
        this.identity = paramBoolean;
    }

    public final V get(K paramK) {
        Objects.requireNonNull(paramK, "key");
        removeStaleEntries();
        int i = hash(paramK);

        CacheEntry[] arrayOfCacheEntry = this.table;
        Object localObject1 = getEntryValue(paramK, i, arrayOfCacheEntry[index(i, arrayOfCacheEntry)]);
        if (localObject1 != null) {
            return localObject1;
        }
        synchronized (this.queue) {
            int j = index(i, this.table);
            localObject1 = getEntryValue(paramK, i, this.table[j]);
            if (localObject1 != null) {
                return localObject1;
            }
            Object localObject2 = create(paramK);
            Objects.requireNonNull(localObject2, "value");
            this.table[j] = new CacheEntry(i, paramK, localObject2, this.table[j], null);
            if (++this.size >= this.threshold) {
                if (this.table.length == 1073741824) {
                    this.threshold = 2147483647;
                } else {
                    removeStaleEntries();
                    arrayOfCacheEntry = newTable(this.table.length << 1);
                    transfer(this.table, arrayOfCacheEntry);

                    if (this.size >= this.threshold / 2) {
                        this.table = arrayOfCacheEntry;
                        this.threshold <<= 1;
                    } else {
                        transfer(arrayOfCacheEntry, this.table);
                    }
                    removeStaleEntries();
                }
            }
            return localObject2;
        }
    }

    public final void remove(K paramK) {
        if (paramK != null)
            synchronized (this.queue) {
                removeStaleEntries();
                int i = hash(paramK);
                int j = index(i, this.table);
                Object localObject1 = this.table[j];
                Object localObject2 = localObject1;
                while (localObject2 != null) {
                    CacheEntry localCacheEntry = localObject2.next;
                    if (localObject2.matches(i, paramK)) {
                        if (localObject2 == localObject1)
                            this.table[j] = localCacheEntry;
                        else {
                            ((CacheEntry) localObject1).next = localCacheEntry;
                        }
                        localObject2.unlink();
                        break;
                    }
                    localObject1 = localObject2;
                    localObject2 = localCacheEntry;
                }
            }
    }

    public final void clear() {
        synchronized (this.queue) {
            int i = this.table.length;
            while (0 < i--) {
                Object localObject1 = this.table[i];
                while (localObject1 != null) {
                    CacheEntry localCacheEntry = ((CacheEntry) localObject1).next;
                    ((CacheEntry) localObject1).unlink();
                    localObject1 = localCacheEntry;
                }
                this.table[i] = null;
            }
            while (null != this.queue.poll()) ;
        }
    }

    private int hash(Object paramObject) {
        if (this.identity) {
            i = System.identityHashCode(paramObject);
            return (i << 1) - (i << 8);
        }
        int i = paramObject.hashCode();

        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int index(int paramInt, Object[] paramArrayOfObject) {
        return paramInt & paramArrayOfObject.length - 1;
    }

    private Cache<K, V>[].

    CacheEntry<K, V> newTable(int paramInt) {
        return (CacheEntry[]) new CacheEntry[paramInt];
    }

    private V getEntryValue(K paramK, int paramInt, Cache<K, V>.CacheEntry<K, V> paramCache) {
        while (paramCache != null) {
            if (paramCache.matches(paramInt, paramK)) {
                return paramCache.value.getReferent();
            }
            paramCache = paramCache.next;
        }
        return null;
    }

    private void removeStaleEntries() {
        Reference localReference = this.queue.poll();
        if (localReference != null)
            synchronized (this.queue) {
                do {
                    if ((localReference instanceof Ref)) {
                        Ref localRef = (Ref) localReference;

                        CacheEntry localCacheEntry1 = (CacheEntry) localRef.getOwner();
                        if (localCacheEntry1 != null) {
                            int i = index(localCacheEntry1.hash, this.table);
                            Object localObject1 = this.table[i];
                            Object localObject2 = localObject1;
                            while (localObject2 != null) {
                                CacheEntry localCacheEntry2 = localObject2.next;
                                if (localObject2 == localCacheEntry1) {
                                    if (localObject2 == localObject1)
                                        this.table[i] = localCacheEntry2;
                                    else {
                                        ((CacheEntry) localObject1).next = localCacheEntry2;
                                    }
                                    localObject2.unlink();
                                    break;
                                }
                                localObject1 = localObject2;
                                localObject2 = localCacheEntry2;
                            }
                        }
                    }
                    localReference = this.queue.poll();
                }
                while (localReference != null);
            }
    }

    private void transfer(Cache<K, V>[].CacheEntry<K, V>paramArrayOfCache1, Cache<K, V>[].CacheEntry<K, V>paramArrayOfCache2) {
        int i = paramArrayOfCache1.length;
        while (0 < i--) {
            Object localObject = paramArrayOfCache1[i];
            paramArrayOfCache1[i] = null;
            while (localObject != null) {
                CacheEntry localCacheEntry = ((CacheEntry) localObject).next;
                if ((((CacheEntry) localObject).key.isStale()) || (((CacheEntry) localObject).value.isStale())) {
                    ((CacheEntry) localObject).unlink();
                } else {
                    int j = index(((CacheEntry) localObject).hash, paramArrayOfCache2);
                    ((CacheEntry) localObject).next = paramArrayOfCache2[j];
                    paramArrayOfCache2[j] = localObject;
                }
                localObject = localCacheEntry;
            }
        }
    }

    private final class CacheEntry<K, V> {
        private final int hash;
        private final Cache.Ref<K> key;
        private final Cache.Ref<V> value;
        private volatile Cache<K, V>.CacheEntry<K, V> next;

        private CacheEntry(K paramV, V paramCache, Cache<K, V>.CacheEntry<K, V> arg4) {
            this.hash = paramV;
            this.key = Cache.this.keyKind.create(this, paramCache, Cache.this.queue);
            Object localObject1;
            this.value = Cache.this.valueKind.create(this, localObject1, Cache.this.queue);
            Object localObject2;
            this.next = localObject2;
        }

        private boolean matches(int paramInt, Object paramObject) {
            if (this.hash != paramInt) {
                return false;
            }
            Object localObject = this.key.getReferent();
            return (localObject == paramObject) || ((!Cache.this.identity) && (localObject != null) && (localObject.equals(paramObject)));
        }

        private void unlink() {
            this.next = null;
            this.key.removeOwner();
            this.value.removeOwner();
            Cache.access$1110(Cache.this);
        }
    }

    public static abstract enum Kind {
        STRONG,

        SOFT,

        WEAK;

        abstract <T> Cache.Ref<T> create(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue);

        private static final class Soft<T> extends SoftReference<T>
                implements Cache.Ref<T> {
            private Object owner;

            private Soft(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue) {
                super(paramReferenceQueue);
                this.owner = paramObject;
            }

            public Object getOwner() {
                return this.owner;
            }

            public T getReferent() {
                return get();
            }

            public boolean isStale() {
                return null == get();
            }

            public void removeOwner() {
                this.owner = null;
            }
        }

        private static final class Strong<T>
                implements Cache.Ref<T> {
            private Object owner;
            private final T referent;

            private Strong(Object paramObject, T paramT) {
                this.owner = paramObject;
                this.referent = paramT;
            }

            public Object getOwner() {
                return this.owner;
            }

            public T getReferent() {
                return this.referent;
            }

            public boolean isStale() {
                return false;
            }

            public void removeOwner() {
                this.owner = null;
            }
        }

        private static final class Weak<T> extends WeakReference<T>
                implements Cache.Ref<T> {
            private Object owner;

            private Weak(Object paramObject, T paramT, ReferenceQueue<? super T> paramReferenceQueue) {
                super(paramReferenceQueue);
                this.owner = paramObject;
            }

            public Object getOwner() {
                return this.owner;
            }

            public T getReferent() {
                return get();
            }

            public boolean isStale() {
                return null == get();
            }

            public void removeOwner() {
                this.owner = null;
            }
        }
    }

    private static abstract interface Ref<T> {
        public abstract Object getOwner();

        public abstract T getReferent();

        public abstract boolean isStale();

        public abstract void removeOwner();
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.util.Cache
 * JD-Core Version:    0.6.2
 */