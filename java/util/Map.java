package java.util;

public abstract interface Map<K, V>
{
  public abstract int size();

  public abstract boolean isEmpty();

  public abstract boolean containsKey(Object paramObject);

  public abstract boolean containsValue(Object paramObject);

  public abstract V get(Object paramObject);

  public abstract V put(K paramK, V paramV);

  public abstract V remove(Object paramObject);

  public abstract void putAll(Map<? extends K, ? extends V> paramMap);

  public abstract void clear();

  public abstract Set<K> keySet();

  public abstract Collection<V> values();

  public abstract Set<Entry<K, V>> entrySet();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public static abstract interface Entry<K, V>
  {
    public abstract K getKey();

    public abstract V getValue();

    public abstract V setValue(V paramV);

    public abstract boolean equals(Object paramObject);

    public abstract int hashCode();
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Map
 * JD-Core Version:    0.6.2
 */