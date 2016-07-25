package java.util.concurrent;

import java.util.NavigableMap;
import java.util.NavigableSet;

public abstract interface ConcurrentNavigableMap<K, V> extends ConcurrentMap<K, V>, NavigableMap<K, V>
{
  public abstract ConcurrentNavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2);

  public abstract ConcurrentNavigableMap<K, V> headMap(K paramK, boolean paramBoolean);

  public abstract ConcurrentNavigableMap<K, V> tailMap(K paramK, boolean paramBoolean);

  public abstract ConcurrentNavigableMap<K, V> subMap(K paramK1, K paramK2);

  public abstract ConcurrentNavigableMap<K, V> headMap(K paramK);

  public abstract ConcurrentNavigableMap<K, V> tailMap(K paramK);

  public abstract ConcurrentNavigableMap<K, V> descendingMap();

  public abstract NavigableSet<K> navigableKeySet();

  public abstract NavigableSet<K> keySet();

  public abstract NavigableSet<K> descendingKeySet();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentNavigableMap
 * JD-Core Version:    0.6.2
 */