package com.sun.beans;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public final class WeakCache<K, V> {
    private final Map<K, Reference<V>> map = new WeakHashMap();

    public V get(K paramK) {
        Reference localReference = (Reference) this.map.get(paramK);
        if (localReference == null) {
            return null;
        }
        Object localObject = localReference.get();
        if (localObject == null) {
            this.map.remove(paramK);
        }
        return localObject;
    }

    public void put(K paramK, V paramV) {
        if (paramV != null) {
            this.map.put(paramK, new WeakReference(paramV));
        } else
            this.map.remove(paramK);
    }

    public void clear() {
        this.map.clear();
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.WeakCache
 * JD-Core Version:    0.6.2
 */