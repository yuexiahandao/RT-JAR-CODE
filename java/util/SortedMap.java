package java.util;

public abstract interface SortedMap<K, V> extends Map<K, V>
{
  public abstract Comparator<? super K> comparator();

  public abstract SortedMap<K, V> subMap(K paramK1, K paramK2);

  public abstract SortedMap<K, V> headMap(K paramK);

  public abstract SortedMap<K, V> tailMap(K paramK);

  public abstract K firstKey();

  public abstract K lastKey();

  public abstract Set<K> keySet();

  public abstract Collection<V> values();

  public abstract Set<Map.Entry<K, V>> entrySet();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.SortedMap
 * JD-Core Version:    0.6.2
 */